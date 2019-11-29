package nc.bs.pub.action;

import java.util.ArrayList;
import java.util.Hashtable;
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.exp.BillCodeNotUnique;
import nc.vo.ic.pub.smallbill.SMGeneralBillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.scm.pub.IBillCode;
import nc.vo.uap.pf.PFBusinessException;

public class N_4D_SAVE extends AbstractCompiler2
{
  private Hashtable m_methodReturnHas = new Hashtable();
  private Hashtable m_keyHas = null;
private Throwable ex;

  public Object runComClass(PfParameterVO vo)
    throws BusinessException
  {
    try
    {
      this.m_tmpVo = vo;

      Object inCurObject = getVo();
      Object inPreObject = getUserObj();
      StringBuffer sErr = new StringBuffer();
      Object retObj = null;
      Object retObjPk = null;

      if (!(inCurObject instanceof GeneralBillVO)) throw new BusinessException("Remote Call", new BusinessException("错误：您希望保存的库存销售出库类型不匹配"));
      if (inCurObject == null) throw new BusinessException("Remote Call", new BusinessException("错误：您希望保存的库存销售出库没有数据"));

      GeneralBillVO inCurVO = null;
      GeneralBillVO inPreVO = null;
      if (inCurObject != null)
        inCurVO = (GeneralBillVO)inCurObject;
      if (inPreObject != null) inPreVO = (GeneralBillVO)inPreObject;
      inCurObject = null;
      inPreObject = null;

      setParameter("INCURVO", inCurVO);
      setParameter("INPREVO", inPreVO);
      Object alLockedPK = null;
      String sBillCode = null;
      IBillCode ibc = inCurVO;
      setParameter("IBC", ibc);
      try
      {
        alLockedPK = runClass("nc.bs.ic.pub.bill.ICLockBO", "lockBill", "&INCURVO:nc.vo.pub.AggregatedValueObject", vo, this.m_keyHas, this.m_methodReturnHas);

        runClass("nc.bs.ic.pub.check.CheckDMO", "checkTimeStamp", "&INCURVO:nc.vo.pub.AggregatedValueObject", vo, this.m_keyHas, this.m_methodReturnHas);

        runClass("nc.bs.ic.ic2a3.AccountctrlDMO", "checkAccountStatus", "&INCURVO:nc.vo.ic.pub.bill.GeneralBillVO", vo, this.m_keyHas, this.m_methodReturnHas);

        runClass("nc.bs.ic.pub.check.CheckDMO", "isPicked", "&INCURVO:nc.vo.pub.AggregatedValueObject", vo, this.m_keyHas, this.m_methodReturnHas);

        runClass("nc.bs.ic.pub.check.CheckInvVendorDMO", "checkVmiVendorInput", "&INCURVO:nc.vo.pub.AggregatedValueObject", vo, this.m_keyHas, this.m_methodReturnHas);

        runClass("nc.bs.ic.pub.check.CheckDMO", "checkSourceBillTimeStamp_new", "&INCURVO:nc.vo.pub.AggregatedValueObject", vo, this.m_keyHas, this.m_methodReturnHas);

        sBillCode = (String)runClass("nc.bs.ic.pub.check.CheckDMO", "setBillCode", "&IBC:nc.vo.scm.pub.IBillCode", vo, this.m_keyHas, this.m_methodReturnHas);

        runClass("nc.bs.ic.pub.check.CheckDMO", "checkInvalidateDate", "&INCURVO:nc.vo.pub.AggregatedValueObject", vo, this.m_keyHas, this.m_methodReturnHas);

        runClass("nc.bs.ic.pub.check.CheckBusiDMO", "checkRelativeRespondBill", "&INCURVO:nc.vo.pub.AggregatedValueObject", vo, this.m_keyHas, this.m_methodReturnHas);

        setParameter("userID", inCurVO.getParentVO().getAttributeValue("coperatoridnow"));
        setParameter("sDate", getUserDate().toString().substring(0, 10));

        runClass("nc.bs.ic.pub.freeze.FreezeDMO", "unLockInv", "&INCURVO:nc.vo.ic.pub.bill.GeneralBillVO,&userID:STRING,&sDate:STRING", vo, this.m_keyHas, this.m_methodReturnHas);

        runClass("nc.bs.ic.pub.check.CheckDMO", "checkBillCodeFore", "&INCURVO:nc.vo.pub.AggregatedValueObject", vo, this.m_keyHas, this.m_methodReturnHas);

        String sBarcodeCheckErr = (String)runClass("nc.bs.ic.pub.check.CheckBarcodeDMO", "checkBarcodeAbsent", "&INCURVO:nc.vo.pub.AggregatedValueObject", vo, this.m_keyHas, this.m_methodReturnHas);
        if (sBarcodeCheckErr != null) {
          sErr.append(sBarcodeCheckErr);
        }
        runClass("nc.bs.ic.pub.bill.DesassemblyBO", "setMeasRateVO", "&INCURVO:nc.vo.pub.AggregatedValueObject,&INPREVO:nc.vo.pub.AggregatedValueObject", vo, this.m_keyHas, this.m_methodReturnHas);

        retObjPk = runClass("nc.bs.ic.ic212.GeneralHBO", "saveBill", "&INCURVO:nc.vo.ic.pub.bill.GeneralBillVO,&INPREVO:nc.vo.ic.pub.bill.GeneralBillVO", vo, this.m_keyHas, this.m_methodReturnHas);

        Object s2 = runClass("nc.bs.ic.pub.bill.GeneralBillBO", "makeBothToZeroOnly", "&INCURVO:nc.vo.pub.AggregatedValueObject", vo, this.m_keyHas, this.m_methodReturnHas);
        if (s2 != null) {
          sErr.append((String)s2);
        }
        runClass("nc.bs.ic.pub.bill.DesassemblyBO", "exeDesassembly", "&INCURVO:nc.vo.pub.AggregatedValueObject,&INPREVO:nc.vo.pub.AggregatedValueObject", vo, this.m_keyHas, this.m_methodReturnHas);

        runClass("nc.bs.ic.pub.RewriteDMO", "reWriteCorNum", "&INCURVO:nc.vo.ic.pub.bill.GeneralBillVO,&INPREVO:nc.vo.ic.pub.bill.GeneralBillVO", vo, this.m_keyHas, this.m_methodReturnHas);
        if (inCurVO.isHaveSourceBill())
        {
          runClass("nc.bs.ic.pub.bill.ICATP", "modifyATP", "&INCURVO:nc.vo.pub.AggregatedValueObject,&INPREVO:nc.vo.pub.AggregatedValueObject", vo, this.m_keyHas, this.m_methodReturnHas);
        }

        runClass("nc.bs.ic.pub.check.CheckDMO", "checkCalBodyInv_New", "&INCURVO:nc.vo.pub.AggregatedValueObject", vo, this.m_keyHas, this.m_methodReturnHas);

        runClass("nc.bs.ic.pub.check.CheckDMO", "checkDBL_New", "&INCURVO:nc.vo.pub.AggregatedValueObject,&INPREVO:nc.vo.pub.AggregatedValueObject", vo, this.m_keyHas, this.m_methodReturnHas);

        runClass("nc.bs.ic.pub.check.CheckDMO", "checkInOutTrace", "&INCURVO:nc.vo.pub.AggregatedValueObject", vo, this.m_keyHas, this.m_methodReturnHas);

        runClass("nc.bs.ic.pub.check.CheckDMO", "checkFixSpace", "&INCURVO:nc.vo.pub.AggregatedValueObject", vo, this.m_keyHas, this.m_methodReturnHas);

        runClass("nc.bs.ic.pub.check.CheckDMO", "checkPlaceAlone", "&INCURVO:nc.vo.pub.AggregatedValueObject", vo, this.m_keyHas, this.m_methodReturnHas);

        Object s1 = runClass("nc.bs.ic.pub.check.CheckDMO", "checkParam_new", "&INCURVO:nc.vo.pub.AggregatedValueObject", vo, this.m_keyHas, this.m_methodReturnHas);
        if (s1 != null) {
          sErr.append((String)s1);
        }
        runClass("nc.bs.ic.pub.RewriteDMO", "reWriteMMNewBatch", "&INCURVO:nc.vo.ic.pub.bill.GeneralBillVO,&INPREVO:nc.vo.ic.pub.bill.GeneralBillVO", vo, this.m_keyHas, this.m_methodReturnHas);

        runClass("nc.bs.ic.pub.RewriteDMO", "reWriteMROutNum", "&INCURVO:nc.vo.ic.pub.bill.GeneralBillVO,&INPREVO:nc.vo.ic.pub.bill.GeneralBillVO", vo, this.m_keyHas, this.m_methodReturnHas);

        setParameter("ERR", sErr.toString());
        setParameter("FUN", "保存");
        runClass("nc.bs.ic.pub.check.CheckDMO", "insertBusinesslog", "&INCURVO:nc.vo.pub.AggregatedValueObject,&ERR:String,&FUN:String", vo, this.m_keyHas, this.m_methodReturnHas);

        if (inCurVO.isHaveSourceBill())
        {
          runClass("nc.bs.ic.pub.bill.ICATP", "checkAtpInstantly", "&INCURVO:nc.vo.pub.AggregatedValueObject,&INPREVO:nc.vo.pub.AggregatedValueObject", vo, this.m_keyHas, this.m_methodReturnHas);
        }

        if ((retObjPk != null) && (!(retObjPk instanceof ArrayList))) throw new BusinessException("Remote Call", new BusinessException("错误：保存动作的返回值类型错误。"));
      }
      catch (Exception e)
      {
        setParameter("EXC", e.getMessage());
        setParameter("FUN", "保存");
        runClass("nc.bs.ic.pub.check.CheckBO", "insertBusinessExceptionlog", "&INCURVO:nc.vo.pub.AggregatedValueObject,&EXC:String,&FUN:String", vo, this.m_keyHas, this.m_methodReturnHas);

        if (sBillCode != null) {
          if ((e instanceof BusinessException)) {
            if ((((BusinessException)e).getCause() == null) || ((((BusinessException)e).getCause() != null) && (!(((BusinessException)e).getCause() instanceof BillCodeNotUnique))))
              runClass("nc.bs.ic.pub.check.CheckDMO", "returnBillCode", "&IBC:nc.vo.scm.pub.IBillCode", vo, this.m_keyHas, this.m_methodReturnHas);
          }
          else if (!(e instanceof BillCodeNotUnique)) {
            runClass("nc.bs.ic.pub.check.CheckDMO", "returnBillCode", "&IBC:nc.vo.scm.pub.IBillCode", vo, this.m_keyHas, this.m_methodReturnHas);
          }
        }
        if ((e instanceof BusinessException)) {
          throw ((BusinessException)e);
        }
        throw new BusinessException("Remote Call", e);
      }
      finally
      {
        setParameter("ALLPK", (ArrayList)alLockedPK);
        if (alLockedPK != null) {
          runClass("nc.bs.ic.pub.bill.ICLockBO", "unlockBill", "&INCURVO:nc.vo.pub.AggregatedValueObject,&ALLPK:ArrayList", vo, this.m_keyHas, this.m_methodReturnHas);
        }
      }
      ArrayList alRet = new ArrayList();
      if (sErr.toString().trim().length() == 0)
        alRet.add(null);
      else
        alRet.add(sErr.toString());
      alRet.add(retObjPk);

      SMGeneralBillVO smbillvo = inCurVO.getSmallBillVO();
      alRet.add(smbillvo);
      inCurVO = null;
      inPreVO = null;
      return alRet;
    }
    catch (Exception ex) {
      if ((ex instanceof BusinessException))
        throw ((BusinessException)ex);
    }
    throw new PFBusinessException(ex.getMessage(), ex);
  }

  public String getCodeRemark()
  {
    return "\t//####本脚本必须含有返回值,返回DLG和PNL的组件不允许有返回值####\n//*************从平台取得由该动作传入的入口参数。本方法取得需要保存的VO。***********\nObject inCurObject=getVo();\nObject inPreObject=getUserObj();\nStringBuffer sErr=new StringBuffer();\nObject retObj=null;\nObject retObjPk=null;\n//1,首先检查传入参数类型是否合法，是否为空。\nif(!(inCurObject instanceof  nc.vo.ic.pub.bill.GeneralBillVO)) throw new nc.vo.pub.BusinessException(\"Remote Call\",new nc.vo.pub.BusinessException(\"错误：您希望保存的库存销售出库类型不匹配\"));\nif(inCurObject == null)  throw new nc.vo.pub.BusinessException(\"Remote Call\",new nc.vo.pub.BusinessException(\"错误：您希望保存的库存销售出库没有数据\"));\n//2,数据合法，把数据转换为库存销售出库。\nnc.vo.ic.pub.bill.GeneralBillVO inCurVO=null;\nnc.vo.ic.pub.bill.GeneralBillVO inPreVO=null;\nif(inCurObject !=null)\n inCurVO=(nc.vo.ic.pub.bill.GeneralBillVO)inCurObject;\nif(inPreObject !=null) inPreVO=(nc.vo.ic.pub.bill.GeneralBillVO)inPreObject;\ninCurObject=null;\ninPreObject=null;\n//获取平台传入的参数\nsetParameter(\"INCURVO\",inCurVO);\nsetParameter(\"INPREVO\",inPreVO);\nObject alLockedPK=null;\nString sBillCode=null;\nnc.vo.scm.pub.IBillCode ibc=(nc.vo.scm.pub.IBillCode)inCurVO;\nsetParameter(\"IBC\",ibc);\ntry{\n//#############################################################\n//方法说明:库存出入库单据加业务锁\nalLockedPK=runClassCom@\"nc.bs.ic.pub.bill.ICLockBO\",\"lockBill\",\"&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\n//该方法<不可配置>\n//方法说明:检查库存单据时间戳\nrunClassCom@\"nc.bs.ic.pub.check.CheckDMO\",\"checkTimeStamp\",\"&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\n//####重要说明：生成的业务组件方法尽量不要进行修改####\n//方法说明:检查是否关帐。<可配置>\n//目前是根据单据表体业务日期检查。如果根据登录日期检查，请将checkAccountStatus改为checkAccountStatus1\nrunClassCom@\"nc.bs.ic.ic2a3.AccountctrlDMO\",\"checkAccountStatus\",\"&INCURVO:nc.vo.ic.pub.bill.GeneralBillVO\"@;\n//##################################################\n//该方法<不可配置>\n//方法说明:检查是否生成了拣货单\nrunClassCom@ \"nc.bs.ic.pub.check.CheckDMO\", \"isPicked\", \"&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\n//方法说明:检查vmi存货时，表头的供应商必填\nrunClassCom@ \"nc.bs.ic.pub.check.CheckInvVendorDMO\", \"checkVmiVendorInput\", \"&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\n//该方法<可配置>\n//方法说明:检查来源单据时间戳\nrunClassCom@\"nc.bs.ic.pub.check.CheckDMO\",\"checkSourceBillTimeStamp_new\",\"&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\n//该方法<不可配置>\n//方法说明:检查是否有单据号，如果没有，系统自动产生。\nsBillCode=(String)runClassCom@\"nc.bs.ic.pub.check.CheckDMO\",\"setBillCode\",\"&IBC:nc.vo.scm.pub.IBillCode\"@;\n//####重要说明：生成的业务组件方法尽量不要进行修改####\n//##################################################\n//该方法<不可配置>\n//方法说明:检查存货批次的失效日期是否一致\nrunClassCom@\"nc.bs.ic.pub.check.CheckDMO\",\"checkInvalidateDate\",\"&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\n//方法说明:检查存货出库是否跟踪入库\nrunClassCom@\"nc.bs.ic.pub.check.CheckBusiDMO\",\"checkRelativeRespondBill\",\"&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\n//##################################################\n//####重要说明：生成的业务组件方法尽量不要进行修改####\n//方法说明:将锁定存货及序列号解锁\nsetParameter(\"userID\",inCurVO.getParentVO().getAttributeValue(\"coperatoridnow\"));\nsetParameter(\"sDate\",getUserDate().toString().substring(0,10));\n//####重要说明：生成的业务组件方法尽量不要进行修改####\n//方法说明:出库单保存后将锁定存货及序列号解锁\nrunClassCom@\"nc.bs.ic.pub.freeze.FreezeDMO\",\"unLockInv\",\"&INCURVO:nc.vo.ic.pub.bill.GeneralBillVO,&userID:STRING,&sDate:STRING\"@;\n//该方法<不可配置>\n//方法说明:检查库存单据号是否重复\nrunClassCom@\"nc.bs.ic.pub.check.CheckDMO\",\"checkBillCodeFore\",\"&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\n//##################################################\n//校验条码数量和存货数量 该方法可以配置\nString sBarcodeCheckErr=(String)runClassCom@\"nc.bs.ic.pub.check.CheckBarcodeDMO\",\"checkBarcodeAbsent\",\"&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\nif (sBarcodeCheckErr!=null )\nsErr.append(sBarcodeCheckErr);\n//处理单据单据辅数量和库存单位之间的转换，如果没有这种业务，请实施人员注释掉。\nrunClassCom@\"nc.bs.ic.pub.bill.DesassemblyBO\",\"setMeasRateVO\",\"&INCURVO:nc.vo.pub.AggregatedValueObject,&INPREVO:nc.vo.pub.AggregatedValueObject\"@;\n//方法说明:单据保存\nretObjPk=runClassCom@\"nc.bs.ic.ic212.GeneralHBO\",\"saveBill\",\"&INCURVO:nc.vo.ic.pub.bill.GeneralBillVO,&INPREVO:nc.vo.ic.pub.bill.GeneralBillVO\"@;\n//该方法<可配置>\n//方法说明:数量倒挤\nObject s2=runClassCom@\"nc.bs.ic.pub.bill.GeneralBillBO\",\"makeBothToZeroOnly\",\"&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\nif(s2!=null)\n\tsErr.append((String)s2);\n//处理拆箱，如果没有这种业务，请实施人员注释掉。\nrunClassCom@\"nc.bs.ic.pub.bill.DesassemblyBO\",\"exeDesassembly\",\"&INCURVO:nc.vo.pub.AggregatedValueObject,&INPREVO:nc.vo.pub.AggregatedValueObject\"@;\n//回写累计出库数量\nrunClassCom@\"nc.bs.ic.pub.RewriteDMO\",\"reWriteCorNum\",\"&INCURVO:nc.vo.ic.pub.bill.GeneralBillVO,&INPREVO:nc.vo.ic.pub.bill.GeneralBillVO\"@;\nif(inCurVO.isHaveSourceBill()){\n//方法说明:库存单据保存时修改可用量\n\trunClassCom@\"nc.bs.ic.pub.bill.ICATP\",\"modifyATP\",\"&INCURVO:nc.vo.pub.AggregatedValueObject,&INPREVO:nc.vo.pub.AggregatedValueObject\"@;\n}\n//该方法<不可配置>\n//方法说明:检查存货是否已经分配到库存组织\nrunClassCom@\"nc.bs.ic.pub.check.CheckDMO\",\"checkCalBodyInv_New\",\"&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\n//该方法<不可配置>\n//方法说明:检查存货主辅数量方向一致,批次负结存,存货负结存\nrunClassCom@\"nc.bs.ic.pub.check.CheckDMO\",\"checkDBL_New\",\"&INCURVO:nc.vo.pub.AggregatedValueObject,&INPREVO:nc.vo.pub.AggregatedValueObject\"@;//该方法<不可配置>\n//该方法<可配置>\n//方法说明:检查供应商管理存货负结存\n//runClassCom@\"nc.bs.ic.pub.check.CheckInvVendorDMO\",\"checkInvQtyNewVendor_New\",\"&INCURVO:nc.vo.pub.AggregatedValueObject,&INPREVO:nc.vo.pub.AggregatedValueObject\"@;\n//方法说明:检查存货出库是否跟踪入库负结存\nrunClassCom@\"nc.bs.ic.pub.check.CheckDMO\",\"checkInOutTrace\",\"&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\n//该方法<可配置>\n//方法说明:检查货位容量是否超出\n//runClassCom@\"nc.bs.ic.pub.check.CheckDMO\",\"checkCargoVolumeOut\",\"&INCURVO:nc.vo.ic.pub.bill.GeneralBillVO,&INPREVO:nc.vo.ic.pub.bill.GeneralBillVO\"@;\n//该方法<不可配置>\n//方法说明:检查存货固定货位\nrunClassCom@\"nc.bs.ic.pub.check.CheckDMO\",\"checkFixSpace\",\"&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\n//该方法<不可配置>\n//方法说明:检查存货单独存放\nrunClassCom@\"nc.bs.ic.pub.check.CheckDMO\",\"checkPlaceAlone\",\"&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\n//该方法<可配置>\n//方法说明:检查最高库存、最低库存、安全库存、再订购点\nObject s1=runClassCom@\"nc.bs.ic.pub.check.CheckDMO\",\"checkParam_new\",\"&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\nif(s1!=null)\n\tsErr.append((String)s1);\n//方法说明:材料出库单回写生产单据出库数量\nrunClassCom@\"nc.bs.ic.pub.RewriteDMO\",\"reWriteMMNewBatch\",\"&INCURVO:nc.vo.ic.pub.bill.GeneralBillVO,&INPREVO:nc.vo.ic.pub.bill.GeneralBillVO\"@;\n//方法说明:材料出库单回写物资申请单据出库数量\nrunClassCom@\"nc.bs.ic.pub.RewriteDMO\",\"reWriteMROutNum\",\"&INCURVO:nc.vo.ic.pub.bill.GeneralBillVO,&INPREVO:nc.vo.ic.pub.bill.GeneralBillVO\"@;\n//##################################################\n//############################\n//插入业务日志，该方法可以配置\nsetParameter(\"ERR\",sErr.toString());\nsetParameter(\"FUN\",\"保存\");\nrunClassCom@\"nc.bs.ic.pub.check.CheckDMO\",\"insertBusinesslog\",\"&INCURVO:nc.vo.pub.AggregatedValueObject,&ERR:String,&FUN:String\"@;\n//############################\nif(inCurVO.isHaveSourceBill()){\n//方法说明:可用量及时检查,可配置\n        runClassCom@\"nc.bs.ic.pub.bill.ICATP\",\"checkAtpInstantly\",\"&INCURVO:nc.vo.pub.AggregatedValueObject,&INPREVO:nc.vo.pub.AggregatedValueObject\"@;\n}\n//结果返回前必须检查类型是否匹配\nif(retObjPk != null && !(retObjPk instanceof ArrayList))  throw new nc.vo.pub.BusinessException(\"Remote Call\",new nc.vo.pub.BusinessException(\"错误：保存动作的返回值类型错误。\"));\n}catch(Exception e){\n//############################\n//插入业务日志，该方法可以配置\nsetParameter(\"EXC\",e.getMessage());\nsetParameter(\"FUN\",\"保存\");\nrunClassCom@\"nc.bs.ic.pub.check.CheckBO\",\"insertBusinessExceptionlog\",\"&INCURVO:nc.vo.pub.AggregatedValueObject,&EXC:String,&FUN:String\"@;\n//############################\nif(sBillCode!=null){\n\tif(e instanceof nc.vo.pub.BusinessException){\n\t\tif(((nc.vo.pub.BusinessException)e).getCause()== null ||(((nc.vo.pub.BusinessException)e).getCause()!= null && !(((nc.vo.pub.BusinessException)e).getCause() instanceof nc.vo.ic.pub.exp.BillCodeNotUnique)))\n         \t\t\trunClassCom@\"nc.bs.ic.pub.check.CheckDMO\",\"returnBillCode\",\"&IBC:nc.vo.scm.pub.IBillCode\"@;\n\t}else{\n\t\tif(!(e instanceof nc.vo.ic.pub.exp.BillCodeNotUnique))\n\t\t\trunClassCom@\"nc.bs.ic.pub.check.CheckDMO\",\"returnBillCode\",\"&IBC:nc.vo.scm.pub.IBillCode\"@;\n\t}\n}\n       if (e instanceof nc.vo.pub.BusinessException)\n\t\t\tthrow (nc.vo.pub.BusinessException) e;\n\t\telse\n\t\t\tthrow new nc.vo.pub.BusinessException(\"Remote Call\", e);\n}finally{\n//####重要说明：生成的业务组件方法尽量不要进行修改####\n//方法说明:库存出入库单据解业务锁\nsetParameter(\"ALLPK\",(ArrayList)alLockedPK);\nif(alLockedPK!=null)\nrunClassCom@\"nc.bs.ic.pub.bill.ICLockBO\",\"unlockBill\",\"&INCURVO:nc.vo.pub.AggregatedValueObject,&ALLPK:ArrayList\"@;\n//##################################################\n}\nArrayList alRet=new ArrayList();\nif(sErr.toString().trim().length()==0)\n\talRet.add(null);\nelse\n\talRet.add(sErr.toString());\nalRet.add(retObjPk);\n//添加小型单据VO向前台传递 \nnc.vo.ic.pub.smallbill.SMGeneralBillVO smbillvo = inCurVO.getSmallBillVO();\nalRet.add(smbillvo);\ninCurVO=null;\ninPreVO=null;\nreturn alRet;\n//************************************************************************\n";
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