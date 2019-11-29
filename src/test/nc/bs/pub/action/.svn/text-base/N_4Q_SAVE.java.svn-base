package nc.bs.pub.action;

import java.util.ArrayList;
import java.util.Hashtable;
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.exp.BillCodeNotUnique;
import nc.vo.ic.pub.smallbill.SMGeneralBillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.scm.pub.IBillCode;

public class N_4Q_SAVE extends AbstractCompiler2 {
	private Hashtable m_methodReturnHas = new Hashtable();
	private Hashtable m_keyHas = null;

	public Object runComClass(PfParameterVO vo) throws BusinessException {
		try {
			this.m_tmpVo = vo;

			Object inCurObject = getVo();
			Object inPreObject = getUserObj();
			StringBuffer sErr = new StringBuffer();
			Object retObj = null;

			if (!(inCurObject instanceof GeneralBillVO))
				throw new BusinessException("Remote Call",
						new BusinessException("错误：您希望保存的库存货位调整类型不匹配"));
			if (inCurObject == null)
				throw new BusinessException("Remote Call",
						new BusinessException("错误：您希望保存的库存货位调整没有数据"));

			GeneralBillVO inCurVO = null;
			GeneralBillVO inPreVO = null;
			if (inCurObject != null)
				inCurVO = (GeneralBillVO) inCurObject;
			if (inPreObject != null)
				inPreVO = (GeneralBillVO) inPreObject;
			inCurObject = null;
			inPreObject = null;

			setParameter("INCURVO", inCurVO);
			setParameter("INPREVO", inPreVO);
			Object alLockedPK = null;
			String sBillCode = null;
			IBillCode ibc = inCurVO;
			setParameter("IBC", ibc);
			try {
				alLockedPK = runClass("nc.bs.ic.pub.bill.ICLockBO", "lockBill",
						"&INCURVO:nc.vo.pub.AggregatedValueObject", vo,
						this.m_keyHas, this.m_methodReturnHas);
				if (retObj != null) {
					this.m_methodReturnHas.put("lockBill", retObj);
				}

				runClass("nc.bs.ic.pub.check.CheckDMO", "checkTimeStamp",
						"&INCURVO:nc.vo.pub.AggregatedValueObject", vo,
						this.m_keyHas, this.m_methodReturnHas);
				if (retObj != null) {
					this.m_methodReturnHas.put("checkTimeStamp", retObj);
				}

				sBillCode = (String) runClass("nc.bs.ic.pub.check.CheckDMO",
						"setBillCode", "&IBC:nc.vo.scm.pub.IBillCode", vo,
						this.m_keyHas, this.m_methodReturnHas);
				if (retObj != null) {
					this.m_methodReturnHas.put("setBillCode", retObj);
				}

				runClass("nc.bs.ic.pub.check.CheckDMO", "checkBillCodeFore",
						"&INCURVO:nc.vo.pub.AggregatedValueObject", vo,
						this.m_keyHas, this.m_methodReturnHas);
				if (retObj != null) {
					this.m_methodReturnHas.put("checkBillCodeFore", retObj);
				}

				retObj = runClass(
						"nc.bs.ic.ic251.GeneralHBO",
						"saveBill",
						"&INCURVO:nc.vo.ic.pub.bill.GeneralBillVO,&INPREVO:nc.vo.ic.pub.bill.GeneralBillVO",
						vo, this.m_keyHas, this.m_methodReturnHas);
				if (retObj != null) {
					this.m_methodReturnHas.put("saveBill", retObj);
				}

				runClass("nc.bs.ic.pub.check.CheckDMO", "checkCalBodyInv_New",
						"&INCURVO:nc.vo.pub.AggregatedValueObject", vo,
						this.m_keyHas, this.m_methodReturnHas);
				if (retObj != null) {
					this.m_methodReturnHas.put("checkCalBodyInv_New", retObj);
				}

				runClass("nc.bs.ic.pub.check.CheckDMO", "checkDBL_New",
						"&INCURVO:nc.vo.pub.AggregatedValueObject", vo,
						this.m_keyHas, this.m_methodReturnHas);
				if (retObj != null) {
					this.m_methodReturnHas.put("checkDBL_New", retObj);
				}

				runClass("nc.bs.ic.pub.check.CheckDMO", "checkFixSpace",
						"&INCURVO:nc.vo.pub.AggregatedValueObject", vo,
						this.m_keyHas, this.m_methodReturnHas);
				if (retObj != null) {
					this.m_methodReturnHas.put("checkFixSpace", retObj);
				}

				if (retObj != null) {
					this.m_methodReturnHas.put("checkCargoVolumeOut", retObj);
				}

				runClass("nc.bs.ic.pub.check.CheckDMO", "checkPlaceAlone",
						"&INCURVO:nc.vo.pub.AggregatedValueObject", vo,
						this.m_keyHas, this.m_methodReturnHas);
				if (retObj != null) {
					this.m_methodReturnHas.put("checkPlaceAlone", retObj);
				}

				Object s1 = runClass("nc.bs.ic.pub.check.CheckDMO",
						"checkParam_new",
						"&INCURVO:nc.vo.pub.AggregatedValueObject", vo,
						this.m_keyHas, this.m_methodReturnHas);
				if (retObj != null) {
					this.m_methodReturnHas.put("checkParam_new", retObj);
				}
				if (s1 != null) {
					sErr.append((String) s1);
				}
				if ((retObj != null) && (!(retObj instanceof ArrayList)))
					throw new BusinessException("Remote Call",
							new BusinessException("错误：保存动作的返回值类型错误。"));
			} catch (Exception e) {
				if (sBillCode != null) {
					if ((e instanceof BusinessException)) {
						if ((((BusinessException) e).getCause() == null)
								|| ((((BusinessException) e).getCause() != null) && (!(((BusinessException) e)
										.getCause() instanceof BillCodeNotUnique))))
							runClass("nc.bs.ic.pub.check.CheckDMO",
									"returnBillCode",
									"&IBC:nc.vo.scm.pub.IBillCode", vo,
									this.m_keyHas, this.m_methodReturnHas);
						if (retObj != null)
							this.m_methodReturnHas
									.put("returnBillCode", retObj);
					} else {
						if (!(e instanceof BillCodeNotUnique))
							runClass("nc.bs.ic.pub.check.CheckDMO",
									"returnBillCode",
									"&IBC:nc.vo.scm.pub.IBillCode", vo,
									this.m_keyHas, this.m_methodReturnHas);
						if (retObj != null) {
							this.m_methodReturnHas
									.put("returnBillCode", retObj);
						}
					}
				}
				if ((e instanceof BusinessException)) {
					throw ((BusinessException) e);
				}
				throw new BusinessException("Remote Call", e);
			} finally {
				setParameter("ALLPK", (ArrayList) alLockedPK);
				if (alLockedPK != null)
					runClass(
							"nc.bs.ic.pub.bill.ICLockBO",
							"unlockBill",
							"&INCURVO:nc.vo.pub.AggregatedValueObject,&ALLPK:ArrayList",
							vo, this.m_keyHas, this.m_methodReturnHas);
				if (retObj != null) {
					this.m_methodReturnHas.put("unlockBill", retObj);
				}
			}

			ArrayList alRet = new ArrayList();
			if (sErr.toString().trim().length() == 0)
				alRet.add(null);
			else
				alRet.add(sErr.toString());
			alRet.add(retObj);

			SMGeneralBillVO smbillvo = inCurVO.getSmallBillVO();
			alRet.add(smbillvo);
			inCurVO = null;
			inPreVO = null;
			return alRet;
		} catch (Exception ex) {
			if ((ex instanceof BusinessException))
				throw ((BusinessException) ex);
			throw new BusinessException("Remote Call", ex);
		}
	}

	public String getCodeRemark() {
		return "\t//####本脚本必须含有返回值,返回DLG和PNL的组件不允许有返回值####\n//*************从平台取得由该动作传入的入口参数。本方法取得需要保存的VO。***********\nObject inCurObject=getVo();\nObject inPreObject=getUserObj();\nStringBuffer sErr=new StringBuffer();\nObject retObj=null;\n//1,首先检查传入参数类型是否合法，是否为空。\nif(!(inCurObject instanceof  nc.vo.ic.pub.bill.GeneralBillVO)) throw new nc.vo.pub.BusinessException(\"Remote Call\",new nc.vo.pub.BusinessException(\"错误：您希望保存的库存货位调整类型不匹配\"));\nif(inCurObject == null)  throw new nc.vo.pub.BusinessException(\"Remote Call\",new nc.vo.pub.BusinessException(\"错误：您希望保存的库存货位调整没有数据\"));\n//2,数据合法，把数据转换为库存货位调整。\nnc.vo.ic.pub.bill.GeneralBillVO inCurVO=null;\nnc.vo.ic.pub.bill.GeneralBillVO inPreVO=null;\nif(inCurObject !=null)\n inCurVO=(nc.vo.ic.pub.bill.GeneralBillVO)inCurObject;\nif(inPreObject !=null) inPreVO=(nc.vo.ic.pub.bill.GeneralBillVO)inPreObject;\ninCurObject=null;\ninPreObject=null;\n//获取平台传入的参数\nsetParameter(\"INCURVO\",inCurVO);\nsetParameter(\"INPREVO\",inPreVO);\nObject alLockedPK=null;\nString sBillCode=null;\nnc.vo.scm.pub.IBillCode ibc=(nc.vo.scm.pub.IBillCode)inCurVO;\nsetParameter(\"IBC\",ibc);\ntry{\n//####重要说明：生成的业务组件方法尽量不要进行修改####\n//方法说明:库存出入库单据加业务锁\nalLockedPK=runClassCom@\"nc.bs.ic.pub.bill.ICLockBO\",\"lockBill\",\"&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\n//##################################################\n//该方法<不可配置>\n//方法说明:检查库存单据时间戳\nrunClassCom@\"nc.bs.ic.pub.check.CheckDMO\",\"checkTimeStamp\",\"&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\n//该方法<不可配置>\n//方法说明:检查是否有单据号，如果没有，系统自动产生。\nsBillCode=(String)runClassCom@\"nc.bs.ic.pub.check.CheckDMO\",\"setBillCode\",\"&IBC:nc.vo.scm.pub.IBillCode\"@;\n//该方法<不可配置>\n//方法说明:检查库存单据号是否重复\nrunClassCom@\"nc.bs.ic.pub.check.CheckDMO\",\"checkBillCodeFore\",\"&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\n//方法说明:单据保存\nretObj=runClassCom@\"nc.bs.ic.ic251.GeneralHBO\",\"saveBill\",\"&INCURVO:nc.vo.ic.pub.bill.GeneralBillVO,&INPREVO:nc.vo.ic.pub.bill.GeneralBillVO\"@;\n//该方法<不可配置>\n//方法说明:检查存货是否已经分配到库存组织\nrunClassCom@\"nc.bs.ic.pub.check.CheckDMO\",\"checkCalBodyInv_New\",\"&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\n//该方法<不可配置>\n//方法说明:检查存货负结存\nrunClassCom@\"nc.bs.ic.pub.check.CheckDMO\",\"checkDBL_New\",\"&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\n//该方法<不可配置>\n//方法说明:检查存货固定货位\nrunClassCom@\"nc.bs.ic.pub.check.CheckDMO\",\"checkFixSpace\",\"&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\n//该方法<可配置>\n//方法说明:检查货位容量是否超出\n//runClassCom@\"nc.bs.ic.pub.check.CheckDMO\",\"checkCargoVolumeOut\",\"&INCURVO:nc.vo.ic.pub.bill.GeneralBillVO,&INPREVO:nc.vo.ic.pub.bill.GeneralBillVO\"@;\n//该方法<不可配置>\n//方法说明:检查存货单独存放\nrunClassCom@\"nc.bs.ic.pub.check.CheckDMO\",\"checkPlaceAlone\",\"&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\n//该方法<可配置>\n//方法说明:检查最高库存、最低库存、安全库存、再订购点\nObject s1=runClassCom@\"nc.bs.ic.pub.check.CheckDMO\",\"checkParam_new\",\"&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\nif(s1!=null)\n\tsErr.append((String)s1);\n//结果返回前必须检查类型是否匹配\nif(retObj != null && !(retObj instanceof ArrayList))  throw new nc.vo.pub.BusinessException(\"Remote Call\",new nc.vo.pub.BusinessException(\"错误：保存动作的返回值类型错误。\"));\n}catch(Exception e){\n  if(sBillCode!=null){\n\tif(e instanceof nc.vo.pub.BusinessException){\n\t\tif(((nc.vo.pub.BusinessException)e).getCause()== null ||(((nc.vo.pub.BusinessException)e).getCause()!= null && !(((nc.vo.pub.BusinessException)e).getCause() instanceof nc.vo.ic.pub.exp.BillCodeNotUnique)))\n         \t\t\trunClassCom@\"nc.bs.ic.pub.check.CheckDMO\",\"returnBillCode\",\"&IBC:nc.vo.scm.pub.IBillCode\"@;\n\t}else{\n\t\tif(!(e instanceof nc.vo.ic.pub.exp.BillCodeNotUnique))\n\t\t\trunClassCom@\"nc.bs.ic.pub.check.CheckDMO\",\"returnBillCode\",\"&IBC:nc.vo.scm.pub.IBillCode\"@;\n\t}\n}\n       if (e instanceof nc.vo.pub.BusinessException)\n\t\t\tthrow (nc.vo.pub.BusinessException) e;\n\t\telse\n\t\t\tthrow new nc.vo.pub.BusinessException(\"Remote Call\", e);\n}finally{\n//####重要说明：生成的业务组件方法尽量不要进行修改####\n//方法说明:库存出入库单据解业务锁\nsetParameter(\"ALLPK\",(ArrayList)alLockedPK);\nif(alLockedPK!=null)\nrunClassCom@\"nc.bs.ic.pub.bill.ICLockBO\",\"unlockBill\",\"&INCURVO:nc.vo.pub.AggregatedValueObject,&ALLPK:ArrayList\"@;\n//##################################################\n}\nArrayList alRet=new ArrayList();\nif(sErr.toString().trim().length()==0)\n\talRet.add(null);\nelse\n\talRet.add(sErr.toString());\nalRet.add(retObj);\n//添加小型单据VO向前台传递 \nnc.vo.ic.pub.smallbill.SMGeneralBillVO smbillvo = inCurVO.getSmallBillVO();\nalRet.add(smbillvo);\ninCurVO=null;\ninPreVO=null;\nreturn alRet;\n//************************************************************************\n";
	}

	private void setParameter(String key, Object val) {
		if (this.m_keyHas == null) {
			this.m_keyHas = new Hashtable();
		}
		if (val != null)
			this.m_keyHas.put(key, val);
	}
}