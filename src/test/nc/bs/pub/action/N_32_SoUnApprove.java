package nc.bs.pub.action;

import java.rmi.RemoteException;
import java.util.Hashtable;
import nc.bs.framework.common.NCLocator;
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.itf.ps.settle.ISettle;
import nc.itf.so.so120.IBillInvokeCreditManager;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.so.so002.SaleVO;
import nc.vo.so.so002.SaleinvoiceVO;

public class N_32_SoUnApprove extends AbstractCompiler2
{
  private Hashtable m_methodReturnHas = new Hashtable();
  private Hashtable m_keyHas = null;

  public Object runComClass(PfParameterVO vo)
    throws BusinessException
  {
    try
    {
      this.m_tmpVo = vo;

      Object inObject = getVo();

      if (!(inObject instanceof SaleinvoiceVO)) throw new RemoteException("Remote Call", new BusinessException("错误：您希望保存的销售发票类型不匹配"));
      if (inObject == null) throw new RemoteException("Remote Call", new BusinessException("错误：您希望保存的销售发票没有数据"));

      SaleinvoiceVO inVO = (SaleinvoiceVO)inObject;
      String pk_bill = ((SaleVO)inVO.getParentVO()).getCsaleid();
      String billtype = ((SaleVO)inVO.getParentVO()).getCreceipttype();
      inObject = null;

      setParameter("INVO", inVO);
      setParameter("PKBILL", pk_bill);
      setParameter("BILLTYPE", billtype);

      Object retObj = null;

      Object bFlag = null;
      bFlag = runClass("nc.impl.scm.so.pub.DataControlDMO", "lockPkForVo", "&INVO:nc.vo.pub.AggregatedValueObject", vo, this.m_keyHas, this.m_methodReturnHas);
      if (retObj != null) {
        this.m_methodReturnHas.put("lockPkForVo", retObj);
      }

      try
      {
        runClass("nc.impl.scm.so.pub.ParallelCheckDMO", "checkVoNoChanged", "&INVO:nc.vo.pub.AggregatedValueObject", vo, this.m_keyHas, this.m_methodReturnHas);
        if (retObj != null) {
          this.m_methodReturnHas.put("checkVoNoChanged", retObj);
        }

        runClass("nc.impl.scm.so.pub.CheckStatusDMO", "isUnApproveStatus", "&INVO:nc.vo.pub.AggregatedValueObject", vo, this.m_keyHas, this.m_methodReturnHas);
        if (retObj != null) {
          this.m_methodReturnHas.put("isUnApproveStatus", retObj);
        }

        retObj = runClass("nc.impl.scm.so.so012.SquareInputDMO", "setAfterInvoiceAbandonCheck", "&PKBILL:String", vo, this.m_keyHas, this.m_methodReturnHas);
        if (retObj != null) {
          this.m_methodReturnHas.put("setAfterInvoiceAbandonCheck", retObj);
        }

        runClass("nc.impl.scm.so.pub.CheckExecDMOImpl", "isUnSaleInvoice", "&INVO:nc.vo.pub.AggregatedValueObject", vo, this.m_keyHas, this.m_methodReturnHas);
        if (retObj != null) {
          this.m_methodReturnHas.put("isUnSaleInvoice", retObj);
        }

        ISettle bo = (ISettle)NCLocator.getInstance().lookup(ISettle.class.getName());
        bo.isUnauditableForSale(pk_bill);

        if (retObj != null) {
          this.m_methodReturnHas.put("isUnauditableForSale", retObj);
        }

        runClass("nc.impl.scm.so.pub.BusinessControlDMO", "setBillFree", "&PKBILL:String,&BILLTYPE:String", vo, this.m_keyHas, this.m_methodReturnHas);
        if (retObj != null) {
          this.m_methodReturnHas.put("setBillFree", retObj);
        }

        IBillInvokeCreditManager creditManager = (IBillInvokeCreditManager)NCLocator.getInstance().lookup(IBillInvokeCreditManager.class.getName());
        setParameter("CreditManager", creditManager);
        inVO.setIAction(4);
        runClass("nc.impl.scm.so.so002.SaleinvoiceDMO", "renovateAR", "&CreditManager:nc.itf.so.so120.IBillInvokeCreditManager,&INVO:nc.vo.so.so002.SaleinvoiceVO", vo, this.m_keyHas, this.m_methodReturnHas);
      }
      catch (Exception e)
      {
        if ((e instanceof RemoteException)) throw ((RemoteException)e);
        throw new RemoteException(e.getMessage());
      }
      finally
      {
        if ((bFlag != null) && (((UFBoolean)bFlag).booleanValue())) {
          runClass("nc.impl.scm.so.pub.DataControlDMO", "freePkForVo", "&INVO:nc.vo.pub.AggregatedValueObject", vo, this.m_keyHas, this.m_methodReturnHas);
          if (retObj != null) {
            this.m_methodReturnHas.put("freePkForVo", retObj);
          }

        }

      }

      inVO = null;
      pk_bill = null;
      billtype = null;
      return retObj;
    }
    catch (Exception ex) {
      ex.printStackTrace();
      throw new BusinessException(ex.getMessage());
    }
  }

  public String getCodeRemark()
  {
    return "\t//####本脚本必须含有返回值,返回DLG和PNL的组件不允许有返回值#### \n\t//*************从平台取得由该动作传入的入口参数。*********** \n\tObject inObject =getVo (); \n\t//1,首先检查传入参数类型是否合法，是否为空。 \n\tif (! (inObject instanceof nc.vo.so.so002.SaleinvoiceVO)) throw new java.rmi.RemoteException ( \"Remote Call\",new nc.vo.pub.BusinessException ( \"错误：您希望保存的销售发票类型不匹配\")); \n\tif (inObject  == null) throw new java.rmi.RemoteException ( \"Remote Call\",new nc.vo.pub.BusinessException ( \"错误：您希望保存的销售发票没有数据\")); \n\t//2,数据合法，把数据转换。 \n\tnc.vo.so.so002.SaleinvoiceVO inVO = (nc.vo.so.so002.SaleinvoiceVO)inObject; \n\tString pk_bill = ( (nc.vo.so.so002.SaleVO)inVO.getParentVO ()).getCsaleid (); \n\tString billtype = ( (nc.vo.so.so002.SaleVO)inVO.getParentVO ()).getCreceipttype (); \n\tinObject =null; \n\t//************************************************************************************************** \n\tsetParameter ( \"INVO\",inVO); \n\tsetParameter ( \"PKBILL\",pk_bill); \n\tsetParameter ( \"BILLTYPE\",billtype); \n\t//************************************************************************************************** \n\tObject retObj =null; \n\t//方法说明:加锁\n\tObject bFlag=null;\n\tbFlag=runClassCom@\"nc.impl.scm.so.pub.DataControlDMO\",\"lockPkForVo\",\"&INVO:nc.vo.pub.AggregatedValueObject\"@;\n\t//##################################################\n\ttry{\n\t              //####重要说明：生成的业务组件方法尽量不要进行修改####\n\t              //方法说明:并发检查\n\t              runClassCom@ \"nc.impl.scm.so.pub.ParallelCheckDMO\", \"checkVoNoChanged\", \"&INVO:nc.vo.pub.AggregatedValueObject\"@;\n\t              //##################################################\n\t              //####重要说明：生成的业务组件方法尽量不要进行修改#### \n\t              //方法说明:并发互斥检查 \n\t               runClassCom@ \"nc.impl.scm.so.pub.CheckStatusDMO\", \"isUnApproveStatus\", \"&INVO:nc.vo.pub.AggregatedValueObject\"@; \n\t              //################################################## \n\t              //####重要说明：生成的业务组件方法尽量不要进行修改#### \n\t              //方法说明:弃审检查 \n\t              runClassCom@ \"nc.impl.scm.so.pub.CheckExecDMO\", \"isUnSaleInvoice\", \"&INVO:nc.vo.pub.AggregatedValueObject\"@; \n\t              //################################################## \n\t              //####重要说明：生成的业务组件方法尽量不要进行修改####\n\t              //方法说明:销售发票传采购后是否可弃审\n\t              runClassCom@\"nc.bs.ps.settle.SettleDMO\",\"isUnauditableForSale\",\"&PKBILL:STRING\"@;\n\t              //##################################################\n\t              //####重要说明：生成的业务组件方法尽量不要进行修改#### \n\t              //方法说明:将单据状态改为“自由” \n\t              runClassCom@ \"nc.impl.scm.so.pub.BusinessControlDMO\", \"setBillFree\", \"&PKBILL:String,&BILLTYPE:String\"@; \n\t              //################################################## \n\t              //####重要说明：生成的业务组件方法尽量不要进行修改####\n\t              //方法说明:结算提交（反操作） \n\t              retObj =runClassCom@ \"nc.impl.scm.so.so012.SquareInputDMO\", \"setAfterInvoiceAbandonCheck\", \"&PKBILL:String\"@; \n\t              //################################################## \n\t}\n\tcatch (Exception e) {\n\t\tif (e instanceof\tRemoteException) throw (RemoteException)e;\n\t\telse throw new RemoteException (e.getMessage());\n\t}\n\tfinally {\n\t\t//解业务锁\n\t\t//####重要说明：生成的业务组件方法尽量不要进行修改####\n\t\t//方法说明:解锁\n\t\tif(bFlag!=null && ((nc.vo.pub.lang.UFBoolean)bFlag).booleanValue()){\n\t\t\trunClassCom@ \"nc.impl.scm.so.pub.DataControlDMO\", \"freePkForVo\", \"&INVO:nc.vo.pub.AggregatedValueObject\"@;\n\t\t\t//##################################################\n\t\t}\n\t}\n   \n              //*********返回结果****************************************************** \n\tinVO =null; \n\tpk_bill =null; \n\tbilltype =null; \n\treturn retObj; \n\t//************************************************************************\n";
  }

  private void setParameter(String key, Object val)
  {
    if (this.m_keyHas == null) {
      this.m_keyHas = new Hashtable();
    }
    if (val != null)
      this.m_keyHas.put(key, val);
  }
}