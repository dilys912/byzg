package nc.bs.pfxx;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import nc.bs.framework.adaptor.IHttpServletAdaptor;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.component.RemoteProcessComponetFactory;
import nc.bs.logging.Logger;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.pfxx.process.XChangeProcessor;
import nc.bs.pfxx.xxconfig.FileConfigInfoReadFacade;
import nc.bs.uap.lock.PKLock;
import nc.itf.uap.pfxx.IPFxxEJBService;
import nc.itf.uap.pfxx.IPFxxFileService;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.jcom.xml.XMLUtil;
import nc.vo.pfxx.exception.EnvInitException;
import nc.vo.pfxx.exception.FileConfigException;
import nc.vo.pfxx.util.FileUtils;
import nc.vo.pfxx.util.PfxxUtils;
import nc.vo.pfxx.xxconfig.RequestParameter;
import nc.vo.pfxx.xxconfig.SysConfigInfo;
import nc.vo.pub.BusinessException;
import org.w3c.dom.Document;

public class ServletForXchange
  implements IHttpServletAdaptor
{
  private static final long serialVersionUID = 3307L;
  private RemoteProcessComponetFactory factory;

  public ServletForXchange()
  {
    this.factory = null;
  }

  private void sendBackMessage(HttpServletResponse response, boolean bcompress)
  {
    try
    {
      XChangeContext context = PfxxMonitor.getCurrentContext();
      Document doc = context.getResponseMessage();

      if (FileConfigInfoReadFacade.getGlobalParameter().isBackupResponses()) {
        String filename = context.getFileName();
        PfxxUtils.lookUpPFxxFileService().backupResponse(FileUtils.getDocBinaryByteData(doc), filename);
      }

      Writer writer = null;
      String outputEncoding = FileConfigInfoReadFacade.getGlobalParameter().getOutputEncoding();
      if (bcompress) {
        writer = new OutputStreamWriter(new GZIPOutputStream(response.getOutputStream()), outputEncoding);
      }
      else {
        writer = new OutputStreamWriter(response.getOutputStream(), outputEncoding);
      }
      XMLUtil.printDOMTree(writer, doc, 0, outputEncoding);
      writer.close();
    }
    catch (Exception e) {
      Logger.error(e.getMessage(), e);
    }
  }

  private Document getDocumentFromInputStream(InputStream in, boolean bcompress)
    throws EnvInitException
  {
    Document doc = null;
    try
    {
      InputStream input = in;
      if (bcompress) {
        input = new GZIPInputStream(in);
      }
      doc = XMLUtil.getDocumentBuilder().parse(input);
    } catch (Exception e) {
      Logger.error(e.getMessage(), e);
      throw new EnvInitException("-31003", NCLangResOnserver.getInstance().getStrByID("pfxx", "UPPpfxx-000046"));
    }

    if (doc == null) {
      throw new EnvInitException("-31003", NCLangResOnserver.getInstance().getStrByID("pfxx", "UPPpfxx-000045"));
    }

    return doc;
  }

  public void doAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    Logger.init("pfxx");

    XChangeContext context = new XChangeContext();
    PfxxMonitor.registerContext(context);
    boolean bcompress = false;
    ResponseMessage rm;
    try {
      String clientAddress = request.getRemoteAddr();
      Logger.info("����ƽ̨�յ�����" + clientAddress + "������������,������ʼ......");
      Logger.info("��ȡURL�����еĲ���.");
      RequestParameter requestParameter = ConfigInfoAnalyser.initRequestParameter(request);

      requestParameter.setServerURL(request.getServerName() + ":" + request.getServerPort());

      bcompress = requestParameter.isBcompress();
      setResponseContentType(response);

      Logger.info("У�鷢�ͷ��ͻ��˵�ַ");
      checkClientAddress(clientAddress);
      Logger.info("У���ļ�����");
      int length = request.getContentLength();
      checkContentLength(length);

      Logger.info("��������ת��Ϊ�ĵ�.");
      Document doc = getDocumentFromInputStream(request.getInputStream(), bcompress);

      Logger.info("��ʼ��ʼ��������......");
      context.init(requestParameter, doc);
      Logger.info("��ʼ�����������!!!");
      try
      {
        Logger.info("��ʼ�����ĵ�...");

        if (FileConfigInfoReadFacade.getGlobalParameter().getDealRule() == 0)
        {
          PfxxUtils.lookUpPFxxEJBService().processMessage(doc);
        }
        else new XChangeProcessor().processMessage_Alone(doc);

        Logger.info("�����ĵ����!");
      } catch (FileConfigException e) {
        Logger.error(e.getMessage(), e);
        throw new EnvInitException(e);
      }
      Logger.info("����ƽ̨��������" + request.getRemoteAddr() + "���͵�������ɡ�");
    } catch (EnvInitException e) {
      rm = context.newResponseMessage();
      rm.setResultCode(e.getErrorCode());
      rm.appendResultDescription(e.getMessage());
      Logger.error("����ƽ̨��ʼ���쳣", e);
      context.setFileSuccessfulProcess(false);
    } catch (BusinessException e) {
      Logger.error(e.getMessage(), e);
      context.setFileSuccessfulProcess(false);
    } catch (Throwable e) {
      rm = context.newResponseMessage();
      rm.setResultCode("-40000");
      rm.appendResultDescription("��ʼ�������е�����δ���쳣:" + e.getMessage());
      Logger.error(e.getMessage(), e);
      context.setFileSuccessfulProcess(false);
    }
    finally
    {
      dealBillCode(context.isFileSuccessfulProcess());
      Logger.info("��ʼ���ͻ�ִ......");
      sendBackMessage(response, bcompress);
      Logger.info("���ͻ�ִ����!!!");
      PfxxUtils.releaseFormulaParser();
      PfxxMonitor.release();

      releaseAllLocks();
    }
  }

  private void releaseAllLocks()
  {
    try
    {
      PKLock.getInstance().releaseLocks("pfxx-user", null);
    } catch (Exception e) {
      Logger.error("Error", e);
    }
  }

  private void dealBillCode(boolean bExp)
  {
    RemoteProcessComponetFactory tmpfactory = getRemoteProcessFactory();
    if (tmpfactory == null)
      return;
    if (bExp)
      getRemoteProcessFactory().postProcess();
    else
      getRemoteProcessFactory().postErrorProcess(null);
    getRemoteProcessFactory().clearThreadScopePostProcess();
  }

  private RemoteProcessComponetFactory getRemoteProcessFactory()
  {
    if (this.factory != null)
      return this.factory;
    try
    {
      this.factory = ((RemoteProcessComponetFactory)NCLocator.getInstance().lookup("RemoteProcessComponetFactory"));
    }
    catch (Throwable throwable)
    {
      Logger.warn("RemoteCallPostProcess is not found");
    }
    return this.factory;
  }

  private void setResponseContentType(HttpServletResponse response)
    throws EnvInitException
  {
    try
    {
      response.setContentType("text/html; charset=" + FileConfigInfoReadFacade.getGlobalParameter().getOutputEncoding());
    } catch (FileConfigException e) {
      Logger.error(e.getMessage(), e);
      throw new EnvInitException(e);
    }
  }

  private void checkContentLength(int length)
    throws EnvInitException
  {
    int setLength;
    try
    {
      setLength = FileConfigInfoReadFacade.getGlobalParameter().getMaxTransferSize();
    }
    catch (FileConfigException e) {
      Logger.error(e.getMessage(), e);
      throw new EnvInitException(e);
    }
    if (length > setLength * 1024)
      throw new EnvInitException("-21106", NCLangResOnserver.getInstance().getStrByID("pfxx", "UPPpfxx-000044") + setLength + "KB");
  }

  private void checkClientAddress(String clientAddress) throws EnvInitException
  {
    String[] addresses = null;
    boolean isEffective;
    try {
      isEffective = FileConfigInfoReadFacade.getGlobalParameter().isEffective();

      addresses = FileConfigInfoReadFacade.getGlobalParameter().getAddresses();
    }
    catch (FileConfigException e) {
      Logger.error(e.getMessage(), e);
      throw new EnvInitException(e);
    }

    if (isEffective) {
      boolean b = false;
      if (addresses != null) {
        for (int i = 0; i < addresses.length; ++i)
        {
          b = (StringUtil.match(addresses[i], clientAddress)) ? true : b;
        }
      }

      if (!(b))
        throw new EnvInitException("-31201", NCLangResOnserver.getInstance().getStrByID("pfxx", "UPPpfxx-V50018"));
    }
  }
}