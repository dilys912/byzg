package nc.bs.pub.action;

import java.rmi.RemoteException;
import java.util.Hashtable;
import nc.bs.framework.common.NCLocator;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.itf.so.so120.IBillInvokeCreditManager;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.so.pub.BusiUtil;
import nc.vo.so.so002.SaleVO;
import nc.vo.so.so002.SaleinvoiceVO;
import nc.vo.so.so012.SquareVO;

public class N_32_APPROVE extends AbstractCompiler2
{
  private Hashtable m_methodReturnHas;
  private Hashtable m_keyHas;

  public N_32_APPROVE()
  {
    this.m_methodReturnHas = new Hashtable();
    this.m_keyHas = null;
  }

  public Object runComClass(PfParameterVO vo)
    throws BusinessException
  {
    try
    {
      this.m_tmpVo = vo;
      Object inObject = getVo();
      if (!(inObject instanceof SaleinvoiceVO))
        throw new RemoteException("Remote Call", new BusinessException(NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000262")));
      if (inObject == null)
        throw new RemoteException("Remote Call", new BusinessException(NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000263")));
      SaleinvoiceVO inVO = (SaleinvoiceVO)inObject;
      inObject = null;
      setParameter("INVO", inVO);
      Object retObj = null;
      Object bFlag = null;
      bFlag = runClass("nc.impl.scm.so.pub.DataControlDMO", "lockPkForVo", "&INVO:nc.vo.pub.AggregatedValueObject", vo, this.m_keyHas, this.m_methodReturnHas);
      if (retObj != null)
        this.m_methodReturnHas.put("lockPkForVo", retObj);
      try
      {
        inVO = (SaleinvoiceVO)runClass("nc.impl.scm.so.so002.SaleinvoiceDMO", "fillOrignInvoice", "&INVO:nc.vo.so.so002.SaleinvoiceVO", vo, this.m_keyHas, this.m_methodReturnHas);
        setParameter("INVO", inVO);
        runClass("nc.impl.scm.so.pub.ParallelCheckDMO", "checkVoNoChanged", "&INVO:nc.vo.pub.AggregatedValueObject", vo, this.m_keyHas, this.m_methodReturnHas);
        if (retObj != null)
          this.m_methodReturnHas.put("checkVoNoChanged", retObj);
        runClass("nc.impl.scm.so.pub.CheckStatusDMO", "isApproveStatus", "&INVO:nc.vo.pub.AggregatedValueObject", vo, this.m_keyHas, this.m_methodReturnHas);
        if (retObj != null)
          this.m_methodReturnHas.put("isApproveStatus", retObj);
        runClass("nc.impl.scm.so.so002.SaleinvoiceDMO", "setInvoiceCost", "&INVO:nc.vo.so.so002.SaleinvoiceVO", vo, this.m_keyHas, this.m_methodReturnHas);
        if (retObj != null) {
          this.m_methodReturnHas.put("setInvoiceCost", retObj);
        }
        inVO.getHeadVO().setDapprovedate(getUserDate().getDate());
        Object m_sysflowObj = procActionFlow(vo);
        if (m_sysflowObj != null)
        {
          Object obj = m_sysflowObj;
          Object localObject1 = obj;
          return localObject1;
        }
        IBillInvokeCreditManager creditManager = (IBillInvokeCreditManager)NCLocator.getInstance().lookup(IBillInvokeCreditManager.class.getName());
        setParameter("CreditManager", creditManager);
        inVO.setIAction(3);
        runClass("nc.impl.scm.so.so002.SaleinvoiceDMO", "renovateAR", "&CreditManager:nc.itf.so.so120.IBillInvokeCreditManager,&INVO:nc.vo.so.so002.SaleinvoiceVO", vo, this.m_keyHas, this.m_methodReturnHas);

        SaleVO header = (SaleVO)inVO.getParentVO();
        if (BusiUtil.isICToArap(header.getPk_corp(), header.getCbiztype())) {
          Object oret = runClass("nc.impl.scm.so.so002.SaleinvoiceDMO", "splitDisPart", "&INVO:nc.vo.so.so002.SaleinvoiceVO", vo, this.m_keyHas, this.m_methodReturnHas);
          if (oret != null) {
            SaleinvoiceVO partVO = (SaleinvoiceVO)oret;
            SquareVO sqVO = null;
            sqVO = (SquareVO)changeData(partVO, "32", "33");

            setParameter("PFACTION", "AutoIncomeBal");

            setParameter("PFBILLTYPE", "33");

            setParameter("PFDATE", getUserDate().toString());

            setParameter("PFVO", sqVO);

            retObj = runClass("nc.bs.pub.pf.PfUtilBO", "processAction", "&PFACTION:String,&PFBILLTYPE:String,&PFDATE:String,&PFFLOW:nc.vo.pub.pf.PfUtilWorkFlowVO,&PFVO:nc.vo.pub.AggregatedValueObject, &PFUSEROBJ:Object", vo, this.m_keyHas, this.m_methodReturnHas);
          }

        }

        runClass("nc.impl.scm.so.so002.SaleinvoiceDMO", "insertSaleData", "&INVO:nc.vo.so.so002.SaleinvoiceVO", vo, this.m_keyHas, this.m_methodReturnHas);
        if (retObj != null)
          this.m_methodReturnHas.put("insertSaleData", retObj);
        runClass("nc.impl.scm.so.so002.SaleinvoiceDMO", "setInvoiceCost", "&INVO:nc.vo.so.so002.SaleinvoiceVO", vo, this.m_keyHas, this.m_methodReturnHas);
        if (retObj != null)
          this.m_methodReturnHas.put("setInvoiceCost", retObj);
      }
      catch (Exception e)
      {
        if ((e instanceof BusinessException)) {
          throw ((BusinessException)e);
        }
        throw new BusinessException(e.getMessage());
      }
      finally
      {
        if ((bFlag != null) && (((UFBoolean)bFlag).booleanValue()))
        {
          runClass("nc.impl.scm.so.pub.DataControlDMO", "freePkForVo", "&INVO:nc.vo.pub.AggregatedValueObject", vo, this.m_keyHas, this.m_methodReturnHas);
          if (retObj != null)
            this.m_methodReturnHas.put("freePkForVo", retObj);
        }
      }
      inVO = null;
      return retObj;
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      throw new BusinessException(ex.getMessage());
    }
  }

  public String getCodeRemark()
  {
    return "\t//####���ű����뺬�з���ֵ,����DLG��PNL������������з���ֵ#### \n\t//*************��ƽ̨ȡ���ɸö����������ڲ�����*********** \n\tObject inObject  =getVo (); \n\t//1,���ȼ�鴫����������Ƿ�Ϸ����Ƿ�Ϊ�ա� \n\tif (! (inObject instanceof nc.vo.so.so002.SaleinvoiceVO)) throw new java.rmi.RemoteException ( \"Remote Call\",new nc.vo.pub.BusinessException ( \"������ϣ����������۷�Ʊ���Ͳ�ƥ��\")); \n\tif (inObject  == null) throw new java.rmi.RemoteException ( \"Remote Call\",new nc.vo.pub.BusinessException ( \"������ϣ����������۷�Ʊû������\")); \n\t//2,���ݺϷ���������ת���� \n\tnc.vo.so.so002.SaleinvoiceVO inVO  = (nc.vo.so.so002.SaleinvoiceVO)inObject; \n\tinObject  =null; \n\t//************************************************************************************************** \n\tsetParameter ( \"INVO\",inVO); \n\t//************************************************************************************************** \n\tObject retObj  =null; \n              \t//����˵��:����\n\tObject bFlag=null;\n\tbFlag=runClassCom@\"nc.bs.so.pub.DataControlDMO\",\"lockPkForVo\",\"&INVO:nc.vo.pub.AggregatedValueObject\"@;\n\t//##################################################\n\ttry{\n\t               //####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n\t               //����˵��:�������\n\t               runClassCom@ \"nc.bs.so.pub.ParallelCheckDMO\", \"checkVoNoChanged\", \"&INVO:nc.vo.pub.AggregatedValueObject\"@;\n\t               //##################################################\n\t               //####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�#### \n\t               //����˵��:������ \n\t               runClassCom@ \"nc.bs.so.pub.CheckStatusDMO\", \"isApproveStatus\", \"&INVO:nc.vo.pub.AggregatedValueObject\"@; \n\t               //################################################## \n\t               //####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n\t               //����˵��:��òɹ���Ʊ�ɱ�\n\t               runClassCom@\"nc.bs.so.so002.SaleinvoiceDMO\",\"setInvoiceCost\",\"&INVO:nc.vo.so.so002.SaleinvoiceVO\"@;\n\t               //##################################################\n\t               //####�����Ϊ����������������ʼ...���ܽ����޸�####\n\t               procActionFlow@@;\n\t               //####�����Ϊ�������������������...���ܽ����޸�####\n\t               //####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n\t               //����˵��:���۷�Ʊ���ʱ���ô��ɹ�\n\t               runClassCom@\"nc.bs.so.so002.SaleinvoiceDMO\",\"insertSaleData\",\"&INVO:nc.vo.so.so002.SaleinvoiceVO\"@;\n\t               //##################################################\n\t               //####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n\t               //����˵��:�ɲɹ���Ʊ������۳ɱ����\n\t               runClassCom@\"nc.bs.so.so002.SaleinvoiceDMO\",\"setInvoiceCost\",\"&INVO:nc.vo.so.so002.SaleinvoiceVO\"@;\n\t               //##################################################\n\t}\n\tcatch (Exception e) {\n\t\tif (e instanceof\tRemoteException) throw (RemoteException)e;\n\t\telse throw new RemoteException (e.getMessage());\n\t}\n\tfinally {\n\t\t//��ҵ����\n\t\t//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n\t\t//����˵��:����\n\t\tif(bFlag!=null && ((nc.vo.pub.lang.UFBoolean)bFlag).booleanValue()){\n\t\t\trunClassCom@ \"nc.bs.so.pub.DataControlDMO\", \"freePkForVo\", \"&INVO:nc.vo.pub.AggregatedValueObject\"@;\n\t\t\t//##################################################\n\t\t}\n\t}\n              //*********���ؽ��****************************************************** \n\tinVO  =null; \n\treturn retObj; \n\t//************************************************************************\n";
  }

  private void setParameter(String key, Object val)
  {
    if (this.m_keyHas == null)
      this.m_keyHas = new Hashtable();
    if (val != null)
      this.m_keyHas.put(key, val);
  }
}