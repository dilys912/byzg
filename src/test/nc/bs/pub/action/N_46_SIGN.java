package nc.bs.pub.action;

import java.util.ArrayList;
import java.util.Hashtable;
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.vo.ic.pub.ILockIC;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.smallbill.SMGeneralBillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.uap.pf.PFBusinessException;

public class N_46_SIGN extends AbstractCompiler2
{
  private Hashtable m_methodReturnHas = new Hashtable();
  private Hashtable m_keyHas = null;

  public Object runComClass(PfParameterVO vo)
    throws BusinessException
  {
    try
    {
      this.m_tmpVo = vo;

      StringBuffer sErr = new StringBuffer();
      AggregatedValueObject[] inCurObjects = getVos();
      Object retObj = null;
      if ((inCurObjects == null) || (inCurObjects.length == 0)) throw new BusinessException("Remote Call", new BusinessException("错误：单据没有数据"));

      if (!(inCurObjects[0] instanceof GeneralBillVO)) throw new BusinessException("Remote Call", new BusinessException("错误：单据类型不匹配"));

      GeneralBillVO[] inCurVOs = null;
      inCurVOs = (GeneralBillVO[])(GeneralBillVO[])inCurObjects;
      inCurObjects = null;

      setParameter("INCURVOS", inCurVOs);
      ArrayList alLockedPK = null;
      setParameter("ILOCKS", (ILockIC[])inCurVOs);
      try
      {
        alLockedPK = (ArrayList)runClass("nc.bs.ic.pub.bill.ICLockBO", "lockICBills", "&INCURVOS:nc.vo.pub.AggregatedValueObject[]", vo, this.m_keyHas, this.m_methodReturnHas);

        runClass("nc.bs.ic.ic2a3.AccountctrlDMO", "checkAccountStatus", "&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[]", vo, this.m_keyHas, this.m_methodReturnHas);

        runClass("nc.bs.ic.pub.check.CheckDMO", "checkTimeStamps", "&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[]", vo, this.m_keyHas, this.m_methodReturnHas);

        runClass("nc.bs.ic.ic202.GeneralHBO", "setOOSNum", "&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[]", vo, this.m_keyHas, this.m_methodReturnHas);

        SCMEnv.out("=====================================================");
        SCMEnv.out("=====================================================");
        SCMEnv.out("=====================================================");

        String sBarcodeCheckErr = (String)runClass("nc.bs.ic.pub.check.CheckBarcodeDMO", "checkBarcodeAbsent", "&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[]", vo, this.m_keyHas, this.m_methodReturnHas);
        if (sBarcodeCheckErr != null) {
          sErr.append(sBarcodeCheckErr);
        }

        retObj = runClass("nc.bs.ic.ic202.GeneralHBO", "signBills", "&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[]", vo, this.m_keyHas, this.m_methodReturnHas);

        setParameter("ERR", sErr.toString());
        runClass("nc.bs.ic.pub.check.CheckDMO", "insertBusinesslog", "&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[],&ERR:String", vo, this.m_keyHas, this.m_methodReturnHas);

        if ((retObj != null) && (!(retObj instanceof ArrayList))) throw new BusinessException("Remote Call", new BusinessException("错误：签字动作的返回值类型错误。"));

        setParameter("icbilltype", "46");
        setParameter("iabilltype", "I3");
        setParameter("PFPARAVO", vo);
        runClass("nc.bs.ic.pub.ictoia.Ic2IaDMO", "saveIABills", "&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[],&icbilltype:String,&iabilltype:String,&PFPARAVO:nc.vo.pub.compiler.PfParameterVO", vo, this.m_keyHas, this.m_methodReturnHas);
      }
      catch (Exception e)
      {
        setParameter("EXC", e.getMessage());
        setParameter("FUN", "签字 ");
        runClass("nc.bs.ic.pub.check.CheckBO", "insertBusinessExceptionlog", "&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[],&EXC:String,&FUN:String", vo, this.m_keyHas, this.m_methodReturnHas);

        for (int i = 0; i < inCurVOs.length; i++) {
          setParameter("IBC", inCurVOs[i]);
          if (inCurVOs[i].getHeaderVO().getStatus() == 2)
            runClass("nc.bs.ic.pub.check.CheckDMO", "returnBillCode", "&IBC:nc.vo.scm.pub.IBillCode", vo, this.m_keyHas, this.m_methodReturnHas);
        }
        if ((e instanceof BusinessException)) {
          throw ((BusinessException)e);
        }
        throw new BusinessException("Remote Call", e);
      }
      finally
      {
        if (alLockedPK != null) {
          setParameter("ALPK", alLockedPK);
          runClass("nc.bs.ic.pub.bill.ICLockBO", "unlockBills", "&INCURVOS:nc.vo.pub.AggregatedValueObject[],&ALPK:ArrayList", vo, this.m_keyHas, this.m_methodReturnHas);
        }
      }
      setParameter("ERR", sErr.toString());
      setParameter("FUN", "签字");
      runClass("nc.bs.ic.pub.check.CheckDMO", "insertBusinesslog", "&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[],&ERR:String,&FUN:String", vo, this.m_keyHas, this.m_methodReturnHas);

      ArrayList alRet = new ArrayList();
      if (sErr.toString().trim().length() == 0)
        alRet.add(null);
      else
        alRet.add(sErr.toString());
      alRet.add(retObj);

      SMGeneralBillVO smbillvo = inCurVOs[0].getSmallBillVO();
      alRet.add(smbillvo);
      inCurVOs = null;
      return new Object[] { alRet };
    }
    catch (Exception ex) {
      if ((ex instanceof BusinessException))
        throw ((BusinessException)ex);
      throw new PFBusinessException(ex.getMessage(), ex);
    }
    
  }

  public String getCodeRemark()
  {
    return "\t//####本脚本必须含有返回值,返回DLG和PNL的组件不允许有返回值####\n\t//*************从平台取得由该动作传入的入口参数。本方法取得需要保存的VO。***********\n//############################\nStringBuffer sErr=new StringBuffer();\nnc.vo.pub.AggregatedValueObject[] inCurObjects=getVos();\nObject retObj=null;\nif(inCurObjects== null||inCurObjects.length==0)  throw new BusinessException(\"Remote Call\",new nc.vo.pub.BusinessException(\"错误：单据没有数据\"));\n//1,首先检查传入参数类型是否合法，是否为空。\nif(!(inCurObjects[0] instanceof  nc.vo.ic.pub.bill.GeneralBillVO)) throw new BusinessException(\"Remote Call\",new nc.vo.pub.BusinessException(\"错误：单据类型不匹配\"));\n//2,数据合法，把数据转换为库存单据。\nnc.vo.ic.pub.bill.GeneralBillVO[] inCurVOs=null;\ninCurVOs=(nc.vo.ic.pub.bill.GeneralBillVO[])inCurObjects;\ninCurObjects=null;\n//获取平台传入的参数\nsetParameter(\"INCURVOS\",inCurVOs);\nArrayList alLockedPK=null;\nsetParameter(\"ILOCKS\",(nc.vo.ic.pub.ILockIC[])inCurVOs);\ntry{\n//####重要说明：生成的业务组件方法尽量不要进行修改####\n//方法说明:库存出入库单据加业务锁\nalLockedPK=(ArrayList)runClassCom@\"nc.bs.ic.pub.bill.ICLockBO\",\"lockICBills\",\"&INCURVOS:nc.vo.pub.AggregatedValueObject[]\"@;\n//####重要说明：生成的业务组件方法尽量不要进行修改####\n//方法说明:检查是否关帐。<可配置>\n//目前是根据单据表体业务日期检查。如果根据登录日期检查，请将checkAccountStatus改为checkAccountStatus1\nrunClassCom@\"nc.bs.ic.ic2a3.AccountctrlDMO\",\"checkAccountStatus\",\"&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[]\"@;\n//#############################################################\n//方法说明:库存出入库单据加业务锁\n//alLockedPK=runClassCom@\"nc.bs.ic.pub.bill.GeneralBillBO\",\"lockBills\",\"&INCURVOS:nc.vo.pub.AggregatedValueObject[]\"@;\n//##################################################\n//该方法<不可配置>\n//方法说明:检查库存单据时间戳\nrunClassCom@\"nc.bs.ic.pub.check.CheckDMO\",\"checkTimeStamps\",\"&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[]\"@;\n//####重要说明：生成的业务组件方法尽量不要进行修改####\n//方法说明:回写累计补缺货量\n//增加对于有来源单据的产成品入库单据回写补缺货数量接口的判断条件 20030212\nrunClassCom@\"nc.bs.ic.ic202.GeneralHBO\",\"setOOSNum\",\"&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[]\"@;\n//##################################################\nnc.vo.scm.pub.SCMEnv.out(\"=====================================================\");\nnc.vo.scm.pub.SCMEnv.out(\"=====================================================\");\nnc.vo.scm.pub.SCMEnv.out(\"=====================================================\");\n//####重要说明：生成的业务组件方法尽量不要进行修改####\n//方法说明:单据签字\n//校验条码数量和存货数量 该方法可以配置\nString sBarcodeCheckErr=(String)runClassCom@\"nc.bs.ic.pub.check.CheckBarcodeDMO\",\"checkBarcodeAbsent\",\"&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[]\"@;\nif (sBarcodeCheckErr!=null )\nsErr.append(sBarcodeCheckErr);\n//#################\n//#################\nretObj =runClassCom@ \"nc.bs.ic.ic202.GeneralHBO\", \"signBills\", \"&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[]\"@;\n//##################################################\n//############################\n//插入业务日志，该方法可以配置\nsetParameter(\"ERR\",sErr.toString());\nrunClassCom@\"nc.bs.ic.pub.check.CheckDMO\",\"insertBusinesslog\",\"&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[],&ERR:String\"@;\n//############################\n//结果返回前必须检查类型是否匹配\nif (retObj != null && ! (retObj instanceof ArrayList)) throw new BusinessException ( \"Remote Call\",new nc.vo.pub.BusinessException ( \"错误：签字动作的返回值类型错误。\"));\n//####重要说明：生成的业务组件方法尽量不要进行修改####\n//方法说明:生成存货单据接口\n//nc.bs.ic.pub.ictoia.Ic2IaDMO.saveIABills(inCurVOs,\"46\",\"I3\",vo);\nsetParameter(\"icbilltype\",\"46\");\nsetParameter(\"iabilltype\",\"I3\");\nsetParameter(\"PFPARAVO\",vo);\nrunClassCom@\"nc.bs.ic.pub.ictoia.Ic2IaDMO\",\"saveIABills\",\"&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[],&icbilltype:String,&iabilltype:String,&PFPARAVO:nc.vo.pub.compiler.PfParameterVO\"@;\n//##################################################\n}catch(Exception e){\n//############################\n//插入业务日志，该方法可以配置\nsetParameter(\"EXC\",e.getMessage());\nsetParameter(\"FUN\",\"签字 \");\nrunClassCom@\"nc.bs.ic.pub.check.CheckBO\",\"insertBusinessExceptionlog\",\"&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[],&EXC:String,&FUN:String\"@;\n//###########################\n//如果是新增状态，退回单据号\nfor(int i=0;i<inCurVOs.length;i++){\nsetParameter(\"IBC\",(nc.vo.scm.pub.IBillCode)inCurVOs[i]);\nif(inCurVOs[i].getHeaderVO().getStatus()==nc.vo.pub.VOStatus.NEW)\n         runClassCom@\"nc.bs.ic.pub.check.CheckDMO\",\"returnBillCode\",\"&IBC:nc.vo.scm.pub.IBillCode\"@;\n}\n          if (e instanceof BusinessException)\n\t\t\tthrow (BusinessException) e;\n\t\telse\n\t\t\tthrow new BusinessException(\"Remote Call\", e);\n}\nfinally {\n//####重要说明：生成的业务组件方法尽量不要进行修改####\n//方法说明:库存出入库单据解业务锁\nif(alLockedPK!=null){\nsetParameter(\"ALPK\",alLockedPK);\n runClassCom@\"nc.bs.ic.pub.bill.ICLockBO\",\"unlockBills\",\"&INCURVOS:nc.vo.pub.AggregatedValueObject[],&ALPK:ArrayList\"@;\n}}\n//插入业务日志，该方法可以配置\nsetParameter(\"ERR\",sErr.toString());\nsetParameter(\"FUN\",\"签字\");\nrunClassCom@\"nc.bs.ic.pub.check.CheckDMO\",\"insertBusinesslog\",\"&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[],&ERR:String,&FUN:String\"@;\n//############################\t\nArrayList alRet=new ArrayList();\nif(sErr.toString().trim().length()==0)\n\talRet.add(null);\nelse\n\talRet.add(sErr.toString());\nalRet.add(retObj);\n//添加小型单据VO向前台传递 \nnc.vo.ic.pub.smallbill.SMGeneralBillVO smbillvo = inCurVOs[0].getSmallBillVO();\nalRet.add(smbillvo);\ninCurVOs=null;\nreturn new Object[]{alRet};\n\t//************************************************************************\n";
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