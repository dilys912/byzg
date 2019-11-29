package nc.bs.ic.alert;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.gl.pfxx.XmlUtils;
import nc.bs.pub.pa.IBusinessPlugin;
import nc.bs.pub.pa.html.IAlertMessage;
import nc.bs.pub.pa.html.IAlertMessage2;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.pa.Key;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

public class SendxmlAlert
  implements IBusinessPlugin, IAlertMessage2
{
  String warntype = "发送PTS系统XML信息预警";
  String[][] Allbodyvalues;
  List Alllist;
  File file = new File("/uftest/ERP");
  //File file=new File("D://ERP");
  URL realURL;
  BaseDAO dao = new BaseDAO();

  int[] BodyColumnType = new int[3];

  public int getImplmentsType()
  {
    return 3;
  }

  public Key[] getKeys() {
    return null;
  }

  public String getTypeDescription() {
    return null;
  }

  public String getTypeName() {
    return null;
  }

  private String port()
    throws Exception
  {
    StringBuffer sq = new StringBuffer();
    sq.append(" select VALUE from pub_sysinit where pk_org='1078' and nvl(dr,0)=0 and initcode='PORT1' ");

    String VALUE = "";
    IUAPQueryBS uAPQueryBSQ = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
    try {
      VALUE = (String)uAPQueryBSQ.executeQuery(sq.toString(), new ColumnProcessor());
    }
    catch (BusinessException e)
    {
      e.printStackTrace();
    }

    String url = "http://10.43.8.132:" + VALUE + "/service/XChangeServlet?account=0001&receiver=10309";
    return url;
  }

  public IAlertMessage implementReturnFormatMsg(Key[] keys, String corpPK, UFDate clientLoginDate)
    throws BusinessException
  {
    if ("1078".equals(corpPK))
    {
      File cpfile = new File(this.file + "/CP");
      String[] cptest = cpfile.list();
      System.out.println("有" + (cptest.length - 3) + "条成品导入数据");
      for (int i = 0; i < cptest.length; i++) {
        if ((cptest[i].endsWith(".XML")) && 
          (cptest[i].startsWith("WS"))) {
          sendxml(cpfile, cptest[i]);
        }
      }

      for (int i = 0; i < cptest.length; i++) {
        if ((cptest[i].endsWith(".XML")) && 
          (!cptest[i].startsWith("WS"))) {
          sendxml(cpfile, cptest[i]);
        }

      }

      File kyfile = new File(this.file + "/KL");
      String[] kytest = kyfile.list();
      System.out.println("有" + (kytest.length - 3) + "条扣留导入数据");
      for (int i = 0; i < kytest.length; i++) {
        if (kytest[i].endsWith(".XML")) {
          sendxml(kyfile, kytest[i]);
        }

      }

      File fjfile = new File(this.file + "/FJ");
      String[] fjtest = fjfile.list();
      System.out.println("有" + (fjtest.length - 3) + "条分拣导入数据");
      for (int i = 0; i < fjtest.length; i++) {
        if (fjtest[i].endsWith(".XML")) {
          sendxml(fjfile, fjtest[i]);
        }

      }

      File wsfile = new File(this.file + "/WS");
      String[] wstest = wsfile.list();
      System.out.println("有" + (wstest.length - 3) + "条尾数导入数据");
      for (int i = 0; i < wstest.length; i++) {
        if (wstest[i].endsWith(".XML")) {
          sendxml(wsfile, wstest[i]);
        }
      }

      this.Alllist = null;
    }

    return this;
  }

  private void sendxml(File cpfile, String filename)
  {
	    try
	    {
	      this.realURL = new URL(port());

	      addNodeToXml(cpfile, filename);

	      boolean flag = distinctXmlCheck(filename, cpfile);
	      if (!flag) return;


	      HttpURLConnection connection = (HttpURLConnection)realURL.openConnection();
	      connection.setDoOutput(true);
	      connection.setDoInput(true);
	      connection.setRequestProperty("Content-type", "text/xml");
	      connection.setRequestMethod("POST");
	      System.out.println(cpfile);
	      BufferedOutputStream out = new BufferedOutputStream(connection.getOutputStream());
	      BufferedInputStream input = new BufferedInputStream(new FileInputStream(cpfile + "/" + filename));

	      byte[] buffer = new byte[1000];
	      int length;
	      while ((length = input.read(buffer, 0, 1000)) != -1)
	      {
	        //int length;
	        out.write(buffer, 0, length);
	        connection.getOutputStream();
	      }
	      input.close();
	      out.close();
	      InputStream inputStream = connection.getInputStream();
	      Document resDoc = XmlUtils.getDocumentBuilder().parse(inputStream);
	      TransformerFactory tfac = TransformerFactory.newInstance();
	      Transformer tra = tfac.newTransformer();
	      DOMSource doms = new DOMSource(resDoc);

	      File fnewpath = new File(cpfile + "/result/");

	      if (!fnewpath.exists())
	        fnewpath.mkdirs();
	      File resXML = new File(cpfile + "/result/result" + filename);
	      FileOutputStream outstream = new FileOutputStream(resXML);
	      StreamResult sr = new StreamResult(outstream);
	      tra.transform(doms, sr);

	      DocumentBuilderFactory domfac = DocumentBuilderFactory.newInstance();
	      DocumentBuilder dombuilder = domfac.newDocumentBuilder();
	      InputStream is = new FileInputStream(resXML);
	      Document doc = dombuilder.parse(is);
	      Element root = doc.getDocumentElement();
	      String successful = root.getAttributeNode("successful").getValue();
	      System.out.println(successful);
	      if ("Y".equals(successful)){
	    	  movefile(cpfile, "success", filename);
	      }
//	      else {
//	        movefile(cpfile, "faile", filename);
//	      }

	      inputStream.close();
	      outstream.close();
	    } catch (Exception e) {
	      e.printStackTrace();
	      //movefile(cpfile, "faile", filename);
	    }
}

  private void movefile(File cpfile, String a, String filename) {
    System.out.println("move start");

    File oldFile = new File(cpfile + "/" + filename);

    String newPath = cpfile + "/" + a + "/";

    File fnewpath = new File(newPath);

    if (!fnewpath.exists()) {
      fnewpath.mkdirs();
    }
    copyFile(oldFile.toString(), newPath + oldFile.getName());

    if ("faile".equals(a)) {
      File faile_bak_root = new File(cpfile + "/faile_bak/");
      if (!faile_bak_root.exists())
        faile_bak_root.mkdirs();
      File faile_bak = new File(cpfile + "/faile_bak/" + oldFile.getName());
      if (!faile_bak.exists()) {
        copyFile(oldFile.toString(), cpfile + "/faile_bak/" + oldFile.getName());
      }
    }

    deleteFile(oldFile.toString());
    System.out.println("move success");
  }

  public void deleteFile(String sPath)
  {
    System.out.println("delete start");
    File filea = new File(sPath);

    if ((filea.isFile()) && (filea.exists())) {
      filea.delete();
    }
    System.out.println("delete success");
  }

  public void copyFile(String oldPath, String newPath)
  {
    try
    {
      int bytesum = 0;
      int byteread = 0;
      File oldfile = new File(oldPath);
      if (oldfile.exists()) {
        InputStream inStream = new FileInputStream(oldPath);
        FileOutputStream fs = new FileOutputStream(newPath);
        byte[] buffer = new byte[4096];
        while ((byteread = inStream.read(buffer)) != -1) {
          bytesum += byteread;
          System.out.println(bytesum);
          fs.write(buffer, 0, byteread);
        }
        inStream.close();
        fs.close();
      }
    }
    catch (Exception e) {
      System.out.println("复制单个文件操作出错");
      e.printStackTrace();
    }
  }

  public void fileChannelCopy(File s, File t)
  {
    System.out.println("copy start");
    try
    {
      FileInputStream fi = null;
      FileOutputStream fo = null;
      FileChannel in = null;
      FileChannel out = null;
      fi = new FileInputStream(s);
      fo = new FileOutputStream(t);
      in = fi.getChannel();
      out = fo.getChannel();
      in.transferTo(0L, in.size(), out);
      fi.close();
      in.close();
      fo.close();
      out.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    System.out.println("copy success");
  }

  public String implementReturnMessage(Key[] keys, String corpPK, UFDate clientLoginDate)
    throws BusinessException
  {
    return null;
  }

  private String getStrValue(Object obj) {
    if (obj == null) {
      return "";
    }
    return obj.toString();
  }

  public Object implementReturnObject(Key[] keys, String corpPK, UFDate clientLoginDate)
    throws BusinessException
  {
    return null;
  }

  public boolean implementWriteFile(Key[] keys, String fileName, String corpPK, UFDate clientLoginDate)
    throws BusinessException
  {
    return false;
  }

  public String[] getBodyFields()
  {
    return new String[] { 
      "xml文件编号", 
      "单据日期", 
      "成功/失败" };
  }

  public String[][] getBodyValue()
  {
    if (this.Alllist == null) {
      return null;
    }

    this.Allbodyvalues = new String[this.Alllist.size()][3];
    for (int i = 0; i < this.Alllist.size(); i++) {
      Map b = (Map)this.Alllist.get(i);
      this.Allbodyvalues[i][0] = b.get("xmlcode").toString();
      this.Allbodyvalues[i][1] = b.get("dbilldate").toString();
      this.Allbodyvalues[i][2] = b.get("result").toString();
    }
    if (this.Alllist.size() <= 0) {
      return null;
    }
    return null;
  }

  public float[] getBodyWidths()
  {
    return null;
  }

  public String[] getBottom()
  {
    return new String[0];
  }

  public String getTitle()
  {
    return this.warntype;
  }

  public String[] getTop()
  {
    return new String[] { 
      "发送PTS系统XML信息预警：" };
  }

  public int[] getBodyColumnType()
  {
    return this.BodyColumnType;
  }

  public String getNullPresent()
  {
    return null;
  }

  public String getOmitPresent()
  {
    return null;
  }

  public void addNodeToXml(File cpfile, String filename)
  {
    try
    {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document document = builder.parse(cpfile + "/" + filename);
      Element root = document.getDocumentElement();
      NodeList childList = root.getElementsByTagName("ic_bill_head");
      Node node = childList.item(0);
      NodeList subNodeList = node.getChildNodes();
      boolean flag = false;
      for (int m = 0; m < subNodeList.getLength(); m++) {
        Node subnode = subNodeList.item(m);
        if (subnode.getNodeName().equals("xmlname")) {
          flag = true;
        }
      }
      if (!flag) {
        Element elem = document.createElement("xmlname");
        Text value = document.createTextNode(filename);
        elem.appendChild(value);
        childList.item(0).appendChild(elem);

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer former = tf.newTransformer();
        former.setParameter("encoding", "gbk");

        DOMSource xmlSource = new DOMSource(document);

        StreamResult outputTarget = 
          new StreamResult(new File(cpfile + "/" + filename));
        former.transform(xmlSource, outputTarget);
      }

    }
    catch (ParserConfigurationException e)
    {
      e.printStackTrace();
    }
    catch (SAXException e) {
      e.printStackTrace();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    catch (TransformerException e) {
      e.printStackTrace();
    }
  }

  public boolean distinctXmlCheck(String xmlname, File file)
  {
    try
    {
      String path = "existence";
      String filepath = file + "/" + path + "/";
      File fnewpath = new File(filepath);
      File oldFile = new File(file + "/" + xmlname);
      BaseDAO bdao = new BaseDAO();
      StringBuffer sql = new StringBuffer();
      sql.append(" select vuserdef15 from ic_general_b ")
        .append(" where pk_corp = '1078' ")
        .append(" and nvl(dr,0) = 0 ")
        .append(" and vuserdef15 = '" + xmlname + "' ");
      Map nameMap = (Map)bdao.executeQuery(sql.toString(), new MapProcessor());
      if (nameMap != null) {
        if (!fnewpath.exists()) {
          fnewpath.mkdirs();
        }
        copyFile(oldFile.toString(), filepath + oldFile.getName());
        deleteFile(oldFile.toString());
        return false;
      }
    } catch (DAOException e) {
      e.printStackTrace();
      return false;
    }

    return true;
  }
}