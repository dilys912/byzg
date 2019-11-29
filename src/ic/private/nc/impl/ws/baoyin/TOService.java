package nc.impl.ws.baoyin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.bs.ic.pub.bill.GeneralBillDMO;
import nc.bs.ic.pub.ictodm.IC2DMInterface;
import nc.bs.ic.pub.locator.LocatorDMO;
import nc.bs.pub.formulaparse.FormulaParse;
import nc.bs.pub.pf.PfUtilBO;
import nc.bs.uap.oid.OidGenerator;
import nc.itf.dm.dm104.IDelivbillQuery;
import nc.itf.scm.to.pub.IBill;
import nc.vo.dm.dm104.DelivbillHHeaderVO;
import nc.vo.dm.dm104.DelivbillHItemVO;
import nc.vo.dm.dm104.DelivbillHVO;
import nc.vo.ic.pub.SmartVOUtilExt;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.locator.LocatorVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.to.pub.BillHeaderVO;
import nc.vo.to.pub.BillItemVO;
import nc.vo.to.pub.BillVO;
import nc.vo.trade.pub.IBillStatus;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

/**
 * 调拨出入库服务类
 * @author Administrator
 * @date Mar 31, 2014 3:54:57 PM
 * @type nc.ws.baoyin.DCService
 * @corporation 上海锐鸟软件有限公司
 * @website www.rino123.com
 * @mail zap_168@163.com
 */
@SuppressWarnings("unchecked")
public class TOService extends BaseService {

	public String addDBCK(Map<String, Object> root) throws Exception {
		Map<String, Object> billMap = (Map<String, Object>) root.get("bill");
		Map<String, Object> headMap = (Map<String, Object>) billMap.get("head");
		if (headMap.get("rklx") != null && headMap.get("rklx").toString().equals("内库")) return addDBDD(root);
		UFDateTime currTime = new UFDateTime(System.currentTimeMillis());
		String pk_corp = (String) headMap.get("pk_corp");
		String pk_user = (String) headMap.get("pk_user");
		String pk_stordoc_out = getCK((String) headMap.get("outck"), pk_corp);
		GeneralBillDMO generalBillDMO = new GeneralBillDMO();
		String pk_delivbill_h = (String) headMap.get("pk_delivbill_h");
		IDelivbillQuery boFD = (IDelivbillQuery) NCLocator.getInstance().lookup(IDelivbillQuery.class.getName());
		DelivbillHVO billVO_FD = boFD.delivbill_findByPrimaryKey(pk_delivbill_h);
		DelivbillHHeaderVO headVO_FD = (DelivbillHHeaderVO) billVO_FD.getParentVO();
		DelivbillHItemVO[] bodyVO_FDs = (DelivbillHItemVO[]) billVO_FD.getChildrenVO();
		List<Map<String, Object>> bodyMapList;
		Object obj = ((Map<String, Object>) billMap.get("bodys")).get("body");
		if (obj instanceof Map) {
			bodyMapList = new ArrayList<Map<String, Object>>();
			bodyMapList.add((Map<String, Object>) obj);
		} else {
			bodyMapList = (List<Map<String, Object>>) obj;
		}
		// TODO by zip: 2014/8/15 这里也需要检查数量是否超出
		//		Map<String,UFDouble> lhSLMap = new HashMap<String, UFDouble>();
		//		for (Map<String, Object> map : bodyMapList) {
		//			String bodyRowLH = (String) map.get("lh");
		//			if(lhSLMap.containsKey(bodyRowLH)) {
		//				UFDouble sl = 
		//			}
		//		}
		DelivbillHItemVO bodyVO_FD0 = bodyVO_FDs[0];

		GeneralBillVO insertBillVO_DC = new GeneralBillVO();
		insertBillVO_DC.setLockOperatorid(pk_user);
		GeneralBillHeaderVO insertBillHeadVO_DC = new GeneralBillHeaderVO();
		String bill_OID_DC = OidGenerator.getInstance().nextOid();
		insertBillHeadVO_DC.setPrimaryKey(bill_OID_DC);
		insertBillHeadVO_DC.setStatus(VOStatus.NEW);
		setAttrValue(insertBillHeadVO_DC, "cbilltypecode", "4Y");
		setAttrValue(insertBillHeadVO_DC, "cdilivertypeid", headVO_FD.getPkdelivmode());
		// TODO 这里指定一个默认的销售出库单 部门
		String defaultCdptid = "1016A21000000000047T";
		//setAttrValue(insertBillHeadVO_DC, "cdptid", headVO_FD.getPktranorg());
		setAttrValue(insertBillHeadVO_DC, "cdptid", defaultCdptid);

		setAttrValue(insertBillHeadVO_DC, "fallocflag", 1);
		setAttrValue(insertBillHeadVO_DC, "cothercalbodyid", bodyVO_FD0.getPkdeststockorg());
		setAttrValue(insertBillHeadVO_DC, "coutcalbodyid", bodyVO_FD0.getPksendstockorg());
		setAttrValue(insertBillHeadVO_DC, "pk_calbody", bodyVO_FD0.getPksendstockorg());
		setAttrValue(insertBillHeadVO_DC, "cothercorpid", pk_corp);
		setAttrValue(insertBillHeadVO_DC, "coutcorpid", pk_corp);
		setAttrValue(insertBillHeadVO_DC, "pk_corp", pk_corp);
		setAttrValue(insertBillHeadVO_DC, "cotherwhid", bodyVO_FD0.getPkdestrep());
		setAttrValue(insertBillHeadVO_DC, "cwarehouseid", bodyVO_FD0.getPksendstock());
		setAttrValue(insertBillHeadVO_DC, "dbilldate", currTime.getDate());
		setAttrValue(insertBillHeadVO_DC, "vdiliveraddress", bodyVO_FD0.getVdestaddress());
		setAttrValue(insertBillHeadVO_DC, "vuserdef11", headVO_FD.getVuserdef10()); // 整垛数量
		setAttrValue(insertBillHeadVO_DC, "vuserdef7", headMap.get("cph"));// 车牌号
		setAttrValue(insertBillHeadVO_DC, "vuserdef16", headMap.get("jsy"));// 驾驶员
		String pk_cys = getCYS((String) headMap.get("cys"));
		setAttrValue(insertBillHeadVO_DC, "cwastewarehouseid", pk_cys); // 承运商
		setAttrValue(insertBillHeadVO_DC, "vuserdef13", headMap.get("cys"));// 承运商名称
		setAttrValue(insertBillHeadVO_DC, "coperatorid", pk_user);
		GeneralBillItemVO[] insertBillItemVOs_DC = new GeneralBillItemVO[bodyMapList.size()];
		String[] updateFYDNumSQLs = new String[bodyMapList.size()];
		
		double yfslhj = 0.00;
		for (int i = 0; i < bodyMapList.size(); i++) {
			yfslhj += new UFDouble(bodyMapList.get(i).get("sl").toString()).doubleValue();
		}
		
		for (int i = 0; i < bodyMapList.size(); i++) {
			Map<String, Object> bodyMap = bodyMapList.get(i);
			String invcode = (String) bodyMap.get("lh");
			String pk_invbasdoc = (String) getJdbc().getObject("select pk_invbasdoc from bd_invbasdoc where invcode='" + invcode + "'");
			DelivbillHItemVO bodyVO_FD = null;
			for (int j = 0; j < bodyVO_FDs.length; j++) {
				String pk_invbasdoc_FD = getInvBasePK(bodyVO_FDs[j].getPkinv());
				if (pk_invbasdoc_FD.equals(pk_invbasdoc)) {
					bodyVO_FD = bodyVO_FDs[j];
					break;
				}
			}
			GeneralBillItemVO insertBillItemVO_DC = new GeneralBillItemVO();
			insertBillItemVO_DC.setCgeneralhid(bill_OID_DC);
			insertBillItemVO_DC.setStatus(VOStatus.NEW);
			setAttrValue(insertBillItemVO_DC, "cfirstbillbid", bodyVO_FD.getPkorderrow());
			setAttrValue(insertBillItemVO_DC, "cfirstbillhid", bodyVO_FD.getPkorder());
			setAttrValue(insertBillItemVO_DC, "cfirsttype", bodyVO_FD.getVbilltype());
			setAttrValue(insertBillItemVO_DC, "cinvbasid", pk_invbasdoc);
			setAttrValue(insertBillItemVO_DC, "cdispatcherid", "1016A21000000000WNZO");
			setAttrValue(insertBillItemVO_DC, "cinventoryid", bodyVO_FD.getPkinv());
			String pk_measdoc = (String) getJdbc().getObject("select pk_measdoc from bd_invbasdoc where pk_invbasdoc='" + pk_invbasdoc + "'");
			setAttrValue(insertBillItemVO_DC, "cquoteunitid", bodyVO_FD.getCQuoteunitid() == null ? pk_measdoc : bodyVO_FD.getCQuoteunitid());
			setAttrValue(insertBillItemVO_DC, "crowno", (i + 1) * 10); // 行号
			setAttrValue(insertBillItemVO_DC, "csourcebillbid", bodyVO_FD.getPk_delivbill_b());
			setAttrValue(insertBillItemVO_DC, "csourcebillhid", headVO_FD.getPk_delivbill_h());
			setAttrValue(insertBillItemVO_DC, "csourcetype", "7F"); // 来源单据类型,默认:发运单
			setAttrValue(insertBillItemVO_DC, "dbizdate", currTime.getDate());
			UFDouble noutnum = new UFDouble(bodyMap.get("sl").toString());
			setAttrValue(insertBillItemVO_DC, "noutnum", noutnum); // 出库数量
			setAttrValue(insertBillItemVO_DC, "nquoteunitnum", noutnum);
			setAttrValue(insertBillItemVO_DC, "nquoteunitrate", 1); // 税率,默认为1
			setAttrValue(insertBillItemVO_DC, "nshouldoutnum", new UFDouble(yfslhj));
			setAttrValue(insertBillItemVO_DC, "pk_corp", pk_corp);
			setAttrValue(insertBillItemVO_DC, "vfirstbillcode", bodyVO_FD.getVordercode());
			setAttrValue(insertBillItemVO_DC, "vfirstrowno", getTranRowNo(bodyVO_FD.getPkorderrow())); // 调拨订单行号,需要从调拨单取
			setAttrValue(insertBillItemVO_DC, "vsourcebillcode", headVO_FD.getVdelivbillcode());
			setAttrValue(insertBillItemVO_DC, "vsourcerowno", bodyVO_FD.getIrownumber());
			setAttrValue(insertBillItemVO_DC, "vbatchcode", bodyMap.get("ph"));
			String vfree1 = (String) bodyMap.get("dh");
			setAttrValue(insertBillItemVO_DC, "vfree1", vfree1);
			String cspaceid = getCSpaceid(vfree1, pk_stordoc_out, pk_corp, pk_invbasdoc); // 根据自由项找到货位
			if (StringUtils.isEmpty(cspaceid)) throw new Exception("跺号未分配货位:" + vfree1);
			LocatorVO locatorVO = new LocatorVO(cspaceid);
			locatorVO.setNoutspacenum(noutnum);
			locatorVO.setPk_corp(pk_corp);
			insertBillItemVO_DC.setLocator(new LocatorVO[] {
				locatorVO
			});
			insertBillItemVOs_DC[i] = insertBillItemVO_DC;
			updateFYDNumSQLs[i] = "update dm_delivbill_b set doutnum = doutnum+" + noutnum + " where pk_delivbill_b = '" + bodyVO_FD.getPrimaryKey() + "'";
		}
		insertBillVO_DC.setParentVO(insertBillHeadVO_DC);
		insertBillVO_DC.setChildrenVO(insertBillItemVOs_DC);
		new IC2DMInterface().saveOutDM(new GeneralBillVO[] {
			insertBillVO_DC
		});

		// 解决原系统bug: 收发类别被公式执行为空, 默认:移动仓库出库, 承运商ID被清空
		getJdbc().executeUpdate("update ic_general_h set cdispatcherid='1016A21000000000WNZO',cwastewarehouseid='" + pk_cys + "' where cgeneralhid='" + bill_OID_DC + "'");
		for (int j = 0; j < updateFYDNumSQLs.length; j++) {
			getJdbc().executeUpdate(updateFYDNumSQLs[j]);
		}

		// ----------------------- start sign DC
		ArrayList<GeneralBillVO> signBillVOList_DC = generalBillDMO.queryBillByPks(new String[] {
			bill_OID_DC
		});
		GeneralBillVO signBillVO_DC = signBillVOList_DC.get(0);
		signBillVO_DC.setLockOperatorid(pk_user);
		new PfUtilBO().processAction("SIGN", "4Y", currTime.getDate().toString(), null, signBillVO_DC, null);
		// ----------------------- end sign DC

		StringBuilder ret = new StringBuilder();
		ret.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		ret.append("<root>");
		ret.append("<actiontype>").append(root.get("actiontype")).append("</actiontype>");
		ret.append("<sender>").append(root.get("sender")).append("</sender>");
		ret.append("<billid>").append(signBillVO_DC.getHeaderVO().getPrimaryKey()).append("</billid>");
		ret.append("<billno>").append(signBillVO_DC.getHeaderVO().getVbillcode()).append("</billno>");
		ret.append("</root>");
		return ret.toString();
	}

	public String addDBDD(Map<String, Object> root) throws Exception {
		Map<String, Object> billMap = (Map<String, Object>) root.get("bill");
		Map<String, Object> headMap = (Map<String, Object>) billMap.get("head");
		UFDateTime currTime = new UFDateTime(System.currentTimeMillis());
		String pk_corp = (String) headMap.get("pk_corp");
		String pk_user = (String) headMap.get("pk_user");
		String pk_stordoc_out = getCK((String) headMap.get("outck"), pk_corp);
		String pk_stordoc_in = getCK((String) headMap.get("inck"), pk_corp);
		String pk_cb_out = (String) getJdbc().getObject("select pk_calbody from bd_stordoc where pk_stordoc='" + pk_stordoc_out + "'");
		String pk_cb_in = (String) getJdbc().getObject("select pk_calbody from bd_stordoc where pk_stordoc='" + pk_stordoc_in + "'");

		// ---------------- 增加调拨订单
		BillHeaderVO toBillHeaderVO = new BillHeaderVO();
		toBillHeaderVO.setBaddplanflag(UFBoolean.FALSE);
		setAttrValue(toBillHeaderVO, "bpushbysoflag", UFBoolean.TRUE);
		toBillHeaderVO.setBretractflag(UFBoolean.FALSE);
		toBillHeaderVO.setCincbid(pk_cb_in);
		toBillHeaderVO.setCincorpid(pk_corp);
		toBillHeaderVO.setCinwhid(pk_stordoc_in);
		toBillHeaderVO.setCoutcbid(pk_cb_out);
		toBillHeaderVO.setCoutcorpid(pk_corp);
		// toBillHeaderVO.setCoutcurrtype(value);
		// 调拨类型:入库调拨,币种:,运输方式,折本汇率:1
		toBillHeaderVO.setFallocflag(1);
		toBillHeaderVO.setCoutcurrtype("00010000000000000001"); // TODO 默认币种
		toBillHeaderVO.setNexchangeotobrate(new UFDouble(1));
		toBillHeaderVO.setCoutwhid(pk_stordoc_out);
		toBillHeaderVO.setCtakeoutcbid(pk_cb_out);
		toBillHeaderVO.setCtakeoutcorpid(pk_corp);
		toBillHeaderVO.setCtypecode("5I");
		toBillHeaderVO.setDbilldate(currTime.getDate());
		setAttrValue(toBillHeaderVO, "pk_defdoc18", getCYS((String) headMap.get("cys")));// 承运商对应的客商管理档案主键
		//setAttrValue(toBillHeaderVO, "pk_defdoc2", ""); // 运输方式(对应自定义档案)
		// setAttrValue(toBillHeaderVO, "vdef18", "");// 承运商对应的客商管理档案关联客商基本档案名称
		// setAttrValue(toBillHeaderVO, "vdef2", "");// 运输方式编码(对应自定义档案)

		List<Map<String, Object>> bodyMapList;
		Object obj = ((Map<String, Object>) billMap.get("bodys")).get("body");
		if (obj instanceof Map) {
			bodyMapList = new ArrayList<Map<String, Object>>();
			bodyMapList.add((Map<String, Object>) obj);
		} else {
			bodyMapList = (List<Map<String, Object>>) obj;
		}
		BillItemVO[] toBillItemVOs = new BillItemVO[bodyMapList.size()];
		for (int i = 0; i < toBillItemVOs.length; i++) {
			Map<String, Object> bodyMap = bodyMapList.get(i);
			String invcode = (String) bodyMap.get("lh");
			UFDouble sl = new UFDouble(bodyMap.get("sl").toString());
			String pk_invbasdoc = (String) getJdbc().getObject("select pk_invbasdoc from bd_invbasdoc where invcode='" + invcode + "'");
			String pk_invmandoc = getInvManPK(invcode, pk_corp);
			toBillItemVOs[i] = new BillItemVO();
			toBillItemVOs[i].setCincbid(pk_cb_in);
			toBillItemVOs[i].setCincorpid(pk_corp);
			toBillItemVOs[i].setCininvid(pk_invmandoc);
			toBillItemVOs[i].setCinvbasid(pk_invbasdoc);
			toBillItemVOs[i].setCinwhid(pk_stordoc_in);
			toBillItemVOs[i].setCoutcbid(pk_cb_out);
			toBillItemVOs[i].setCoutcorpid(pk_corp);
			toBillItemVOs[i].setCoutinvid(pk_invmandoc);
			toBillItemVOs[i].setCoutwhid(pk_stordoc_out);
			toBillItemVOs[i].setCrowno(((i + 1) * 10) + "");
			toBillItemVOs[i].setCtakeoutcbid(pk_cb_out);
			toBillItemVOs[i].setCtakeoutcorpid(pk_corp);
			toBillItemVOs[i].setCtakeoutinvid(pk_invmandoc);
			toBillItemVOs[i].setCtakeoutwhid(pk_stordoc_out);
			toBillItemVOs[i].setCtypecode("5I");
			toBillItemVOs[i].setDplanarrivedate(currTime.getDate());
			toBillItemVOs[i].setDplanoutdate(currTime.getDate());
			toBillItemVOs[i].setFallocflag(1);
			toBillItemVOs[i].setNnum(sl);
			toBillItemVOs[i].setVfree1((String) bodyMap.get("dh"));
			//			toBillItemVOs[i].setCrelation_bid("0001A21000000000CPCQ");
			//			toBillItemVOs[i].setCrelationid("0001A21000000000CPCP");
			toBillItemVOs[i].setNquoteunitnum(sl);
		}
		BillVO toBillVO = new BillVO();
		toBillVO.setParentVO(toBillHeaderVO);
		toBillVO.setChildrenVO(toBillItemVOs);
		toBillVO.setOperator(pk_user);
		BillVO[] insertBillVO_TOs = new BillVO[] {
			toBillVO
		};
		ArrayList<Object> insertBillVO_Param_List = new ArrayList<Object>();
		insertBillVO_Param_List.add(insertBillVO_TOs);
		ClientLink cl = new ClientLink(pk_corp, pk_user, currTime.getDate(), null, null, null, null, null, null, false, null, null, null);
		insertBillVO_Param_List.add(cl);
		Object[] insertBillVO_TO_Insert_Ret = new PfUtilBO().processBatch("COMMIT", "5I", currTime.getDate().toString(), insertBillVO_TOs, new Object[] {
			insertBillVO_Param_List
		}, null);
		String pk_bill_to_inserted = (String) ((ArrayList<Object>) insertBillVO_TO_Insert_Ret[0]).get(0);

		// ---------------------- 审核调拨订单
		IBill toBillItf = (IBill) NCLocator.getInstance().lookup(IBill.class.getName());
		ArrayList<String> pk_approve_idList = new ArrayList<String>();
		pk_approve_idList.add(pk_bill_to_inserted);
		BillVO[] savedToBillVO = toBillItf.queryBillByIDs(pk_approve_idList);
		savedToBillVO[0].setOperator(pk_user);
		ArrayList<Object> param_approve_tobill = new ArrayList<Object>();
		ClientLink clApprove = new ClientLink(pk_corp, pk_user, currTime.getDate(), null, null, null, null, null, null, false, null, null, null);
		param_approve_tobill.add(clApprove);

		// 这里已经自动生成了调拨出库单
		new PfUtilBO().processBatch("APPROVE", "5I", currTime.getDate().toString(), savedToBillVO, new Object[] {
			param_approve_tobill
		}, null);

		// 修改调拨出库单
		LocatorDMO locatorDMO = new LocatorDMO();
		GeneralBillDMO generalBillDMO = new GeneralBillDMO();
		String pk_updateBillHID_SQL = "select cgeneralhid from ic_general_b where nvl(dr,0)=0 and csourcebillhid='" + pk_bill_to_inserted + "'";
		String pk_updateBillHID = (String) getJdbc().getObject(pk_updateBillHID_SQL);
		ArrayList<Object> updateBillList = generalBillDMO.queryBillByPks(new String[] {
			pk_updateBillHID
		});
		GeneralBillVO updateBill_SRC_VO = (GeneralBillVO) updateBillList.get(0);
		updateBill_SRC_VO.setLocators(locatorDMO.queryByBillPK(updateBill_SRC_VO.getParentVO().getPrimaryKey()));

		GeneralBillVO updateBill_TarVO = (GeneralBillVO) updateBill_SRC_VO.clone();
		GeneralBillHeaderVO updateBillHeadVO = updateBill_TarVO.getHeaderVO();
		updateBillHeadVO.setStatus(VOStatus.UPDATED);
		// TODO 这里指定一个默认的销售出库单 部门
		String defaultCdptid = "1016A21000000000047T";
		setAttrValue(updateBillHeadVO, "cdptid", defaultCdptid);
		setAttrValue(updateBillHeadVO, "vuserdef11", headMap.get("zdsl")); // 整垛数量
		setAttrValue(updateBillHeadVO, "vuserdef7", headMap.get("cph"));// 车牌号
		setAttrValue(updateBillHeadVO, "vuserdef16", headMap.get("jsy"));// 驾驶员
		String pk_cys = getCYS((String) headMap.get("cys"));
		//setAttrValue(updateBillHeadVO, "cwastewarehouseid", pk_cys); // 承运商
		setAttrValue(updateBillHeadVO, "vuserdef13", headMap.get("cys"));// 承运商名称
		setAttrValue(updateBillHeadVO, "pk_defdoc13", pk_cys); // 承运商
		GeneralBillItemVO[] updateBillItemVO_DCs = updateBill_TarVO.getItemVOs();
		GeneralBillItemVO[] updateBillItemTarVOs = new GeneralBillItemVO[updateBillItemVO_DCs.length];
		for (int i = 0; i < bodyMapList.size(); i++) {
			Map<String, Object> bodyMap = bodyMapList.get(i);
			String vfree1 = (String) bodyMap.get("dh");
			String invcode = (String) bodyMap.get("lh");
			String pk_invbasdoc = (String) getJdbc().getObject("select pk_invbasdoc from bd_invbasdoc where invcode='" + invcode + "'");
			GeneralBillItemVO insertBillItemVO_DC = null;
			for (int j = 0; j < updateBillItemVO_DCs.length; j++) {
				// 通过垛号找到对应行
				if (pk_invbasdoc.equals(updateBillItemVO_DCs[j].getAttributeValue("cinvbasid")) && vfree1.equals(updateBillItemVO_DCs[j].getVfree1())) {
					insertBillItemVO_DC = updateBillItemVO_DCs[j];
					break;
				}
			}
			UFDouble noutnum = new UFDouble(bodyMap.get("sl").toString());
			setAttrValue(insertBillItemVO_DC, "noutnum", noutnum); // 出库数量
			insertBillItemVO_DC.setDbizdate(currTime.getDate());
			//			setAttrValue(insertBillItemVO_DC, "nquoteunitnum", noutnum);
			setAttrValue(insertBillItemVO_DC, "vbatchcode", bodyMap.get("ph"));

			setAttrValue(insertBillItemVO_DC, "vfree1", vfree1);
			String cspaceid = getCSpaceid(vfree1, pk_stordoc_out, pk_corp, pk_invbasdoc); // 根据自由项找到货位
			if (StringUtils.isEmpty(cspaceid)) throw new Exception("跺号未分配货位:" + vfree1);
			LocatorVO locatorVO = new LocatorVO();
			locatorVO.setCspaceid(cspaceid);
			locatorVO.setNoutspacenum(noutnum);
			locatorVO.setPk_corp(pk_corp);
			locatorVO.setCgeneralbid(insertBillItemVO_DC.getCgeneralbid());
			insertBillItemVO_DC.setLocator(new LocatorVO[] {
				locatorVO
			});
			insertBillItemVO_DC.setStatus(VOStatus.UPDATED);
			//			insertBillItemVO_DC.setLocStatus(VOStatus.UPDATED);
			updateBillItemTarVOs[i] = insertBillItemVO_DC;
		}
		updateBill_TarVO.setStatus(VOStatus.UPDATED);
		updateBill_TarVO.setParentVO(updateBillHeadVO);
		updateBill_TarVO.setChildrenVO(updateBillItemTarVOs);
		updateBill_TarVO.setLockOperatorid(pk_user);
		updateBill_TarVO.m_voOld = updateBill_SRC_VO;

		new PfUtilBO().processAction("SAVE", "4Y", currTime.getDate().toString(), null, updateBill_TarVO, updateBill_SRC_VO);

		// ----------------------- start sign DC
		ArrayList<GeneralBillVO> signBillVOList_DC = generalBillDMO.queryBillByPks(new String[] {
			pk_updateBillHID
		});
		GeneralBillVO signBillVO_DC = signBillVOList_DC.get(0);
		signBillVO_DC.setLockOperatorid(pk_user);
		new PfUtilBO().processAction("SIGN", "4Y", currTime.getDate().toString(), null, signBillVO_DC, null);
		// ----------------------- end sign DC

		StringBuilder ret = new StringBuilder();
		ret.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		ret.append("<root>");
		ret.append("<actiontype>").append(root.get("actiontype")).append("</actiontype>");
		ret.append("<sender>").append(root.get("sender")).append("</sender>");
		ret.append("<billid>").append(signBillVO_DC.getHeaderVO().getPrimaryKey()).append("</billid>");
		ret.append("<billno>").append(signBillVO_DC.getHeaderVO().getVbillcode()).append("</billno>");
		ret.append("</root>");
		return ret.toString();
	}

	public String editDBCK(Map<String, Object> root) throws Exception {
		Map<String, Object> billMap = (Map<String, Object>) root.get("bill");
		Map<String, Object> headMap = (Map<String, Object>) billMap.get("head");
		UFDateTime currTime = new UFDateTime(System.currentTimeMillis());
		String billid_DC = (String) headMap.get("billid");
		String pk_corp = (String) headMap.get("pk_corp");
		String pk_user = (String) headMap.get("pk_user");
		String pk_stordoc_out = getCK((String) headMap.get("outck"), pk_corp);
		String billid_FR = (String) getJdbc().getObject("select cgeneralhid from ic_general_b where csourcebillhid='" + billid_DC + "' and nvl(dr,0)=0");
		List<Map<String, Object>> bodyMapList;
		Object obj = ((Map<String, Object>) billMap.get("bodys")).get("body");
		if (obj instanceof Map) {
			bodyMapList = new ArrayList<Map<String, Object>>();
			bodyMapList.add((Map<String, Object>) obj);
		} else {
			bodyMapList = (List<Map<String, Object>>) obj;
		}
		GeneralBillDMO billDMO = new GeneralBillDMO();

		// ------------------------ 取消签字调拨入库单
		ArrayList<GeneralBillVO> cancelSignBillVOList_FR = billDMO.queryBillByPks(new String[] {
			billid_FR
		});
		if (cancelSignBillVOList_FR != null && cancelSignBillVOList_FR.size() > 0) {
			GeneralBillVO cancelSignBillVO_FR = cancelSignBillVOList_FR.get(0);
			Integer cancelSignBillVO_FR_Fbillflag = cancelSignBillVO_FR.getHeaderVO().getFbillflag();
			if (cancelSignBillVO_FR_Fbillflag.intValue() == IBillStatus.COMMIT) {
				cancelSignBillVO_FR.setLockOperatorid(pk_user);
				new PfUtilBO().processAction("CANCELSIGN", "4E", currTime.getDate().toString(), null, cancelSignBillVO_FR, null);
			}
		}

		// ------------------------ 删除调拨入库单
		ArrayList<GeneralBillVO> deleteBillVOList_FR = billDMO.queryBillByPks(new String[] {
			billid_FR
		});
		if (deleteBillVOList_FR != null && deleteBillVOList_FR.size() > 0) {
			GeneralBillVO deleteBillVO_FR = deleteBillVOList_FR.get(0);
			deleteBillVO_FR.setLockOperatorid(pk_user);
			new PfUtilBO().processAction("DELETE", "4E", currTime.getDate().toString(), null, deleteBillVO_FR, null);
		}

		// ------------------------ 取消签字调拨出库单
		ArrayList<GeneralBillVO> cancelSignBillVOList_DC = billDMO.queryBillByPks(new String[] {
			billid_DC
		});
		if (cancelSignBillVOList_DC == null || cancelSignBillVOList_DC.size() < 0) throw new Exception("当前单据已经不存在!");
		GeneralBillVO cancelSignBillVO_DC = cancelSignBillVOList_DC.get(0);
		Integer cancelSignBillVO_DC_Fbillflag = cancelSignBillVO_DC.getHeaderVO().getFbillflag();
		if (cancelSignBillVO_DC_Fbillflag.intValue() == IBillStatus.COMMIT) {
			cancelSignBillVO_DC.setLockOperatorid(pk_user);
			new PfUtilBO().processAction("CANCELSIGN", "4Y", currTime.getDate().toString(), null, cancelSignBillVO_DC, null);
		}

		// ------------------------ 更新调拨出库单
		LocatorDMO locatorDMO = new LocatorDMO();
		ArrayList<GeneralBillVO> updateBillVOList_DC = billDMO.queryBillByPks(new String[] {
			billid_DC
		});
		GeneralBillVO updateSrcBillVO_DC = updateBillVOList_DC.get(0);
		updateSrcBillVO_DC.setLocators(locatorDMO.queryByBillPK(updateSrcBillVO_DC.getPrimaryKey()));
		GeneralBillVO updateBillVO_DC = (GeneralBillVO) updateSrcBillVO_DC.clone();
		updateBillVO_DC.setLockOperatorid(pk_user);
		GeneralBillItemVO[] updateBillItemVOs_DC = (GeneralBillItemVO[]) updateBillVO_DC.getChildrenVO();
		GeneralBillHeaderVO updateBillHeadVO = updateBillVO_DC.getHeaderVO();
		setAttrValue(updateBillHeadVO, "vuserdef7", headMap.get("cph"));// 车牌号
		setAttrValue(updateBillHeadVO, "vuserdef16", headMap.get("jsy"));// 驾驶员
		String pk_cys = getCYS((String) headMap.get("cys"));
		setAttrValue(updateBillHeadVO, "cwastewarehouseid", pk_cys); // 承运商
		setAttrValue(updateBillHeadVO, "vuserdef13", headMap.get("cys"));// 承运商名称

		// 已经存在的行做更新,源单据不存在了,则先删除,再增加新的传递过来的行
		Integer[] updateIdxs = new Integer[] {};
		for (int i = 0; i < bodyMapList.size(); i++) {
			Map<String, Object> bodyMap = bodyMapList.get(i);
			String cgeneralbid = (String) bodyMap.get("cgeneralbid");
			for (int j = 0; j < updateBillItemVOs_DC.length; j++) {
				String bid_src = updateBillItemVOs_DC[j].getCgeneralbid();
				if (cgeneralbid.equals(bid_src)) {
					updateIdxs = (Integer[]) ArrayUtils.add(updateIdxs, i);
					break;
				}
			}
		}
		// 源单据表体需要删除的行
		for (int i = 0; i < updateBillItemVOs_DC.length; i++) {
			String bid_src = updateBillItemVOs_DC[i].getCgeneralbid();
			boolean existsFlag = false;
			for (int j = 0; j < bodyMapList.size(); j++) {
				Map<String, Object> bodyMap = bodyMapList.get(j);
				String cgeneralbid = (String) bodyMap.get("cgeneralbid");
				if (cgeneralbid.equals(bid_src)) {
					existsFlag = true;
					break;
				}
			}
			if (!existsFlag) {
				updateBillItemVOs_DC[i].setStatus(VOStatus.DELETED);
				updateBillItemVOs_DC[i].setLocStatus(VOStatus.DELETED);
			}
		}
		
		double yfslhj = 0.00;
		for (int i = 0; i < bodyMapList.size(); i++) {
			yfslhj += new UFDouble(bodyMapList.get(i).get("sl").toString()).doubleValue();
		}

		for (int i = 0; i < bodyMapList.size(); i++) {
			Map<String, Object> bodyMap = bodyMapList.get(i);
			String invcode = (String) bodyMap.get("lh");
			String pk_invbasdoc = (String) getJdbc().getObject("select pk_invbasdoc from bd_invbasdoc where invcode='" + invcode + "'");
			GeneralBillItemVO itemVO_I = null;
			boolean existsFlag = false;
			for (int j = 0; j < updateIdxs.length; j++) {
				if (i == updateIdxs[j]) {
					existsFlag = true;
					break;
				}
			}
			if (existsFlag) continue;
			for (int j = 0; j < updateBillItemVOs_DC.length; j++) {
				String cinvbasid = getInvBasePK(updateBillItemVOs_DC[j].getCinventoryid());
				if (cinvbasid.equals(pk_invbasdoc)) {
					itemVO_I = (GeneralBillItemVO) updateBillItemVOs_DC[j].clone();
					break;
				}
			}
			if(itemVO_I == null) {
				itemVO_I = (GeneralBillItemVO) updateBillItemVOs_DC[0].clone();
				String invmanpk = getInvManPK(invcode, pk_corp);
				itemVO_I.setCinventoryid(invmanpk);
				itemVO_I.setCinvbasid(pk_invbasdoc);
			}
			itemVO_I.setStatus(VOStatus.NEW);
			itemVO_I.setLocStatus(VOStatus.NEW);
			UFDouble noutnum = new UFDouble(bodyMap.get("sl").toString());
			setAttrValue(itemVO_I, "noutnum", noutnum); // 出库数量
			setAttrValue(itemVO_I, "nquoteunitnum", noutnum);
			setAttrValue(itemVO_I, "nshouldoutnum", new UFDouble(yfslhj));
			setAttrValue(itemVO_I, "vbatchcode", bodyMap.get("ph"));
			String vfree1 = (String) bodyMap.get("dh");
			setAttrValue(itemVO_I, "vfree1", vfree1);
			String cspaceid = getCSpaceid(vfree1, pk_stordoc_out, pk_corp, pk_invbasdoc); // 根据自由项找到货位
			if (StringUtils.isEmpty(cspaceid)) throw new Exception("未分配货位:料号[" + invcode + "]跺号[" + vfree1 + "]");
			LocatorVO locatorVO;
			if(itemVO_I.getLocator() == null || itemVO_I.getLocator().length <= 0) {
				locatorVO = new LocatorVO();
			}else {
				locatorVO = itemVO_I.getLocator()[0];
			}
			locatorVO.setCspaceid(cspaceid);
			locatorVO.setNoutspacenum(noutnum);
			locatorVO.setPk_corp(pk_corp);
			itemVO_I.setLocator(new LocatorVO[] {
				locatorVO
			});
			updateBillItemVOs_DC = (GeneralBillItemVO[]) ArrayUtils.add(updateBillItemVOs_DC, itemVO_I);
		}
		FormulaParse f = new FormulaParse();
		String[] formulas = {
				"cinvbasid->getColValue(bd_invmandoc,pk_invbasdoc,pk_invmandoc,cinventoryid)",//
				"cquotecurrency->getColValue(so_saleorder_b,ccurrencytypeid,corder_bid,cfirstbillbid)",
				"nquoteprice->getColValue(so_saleorder_b,norgqttaxnetprc,corder_bid,cfirstbillbid)"
		};
		SmartVOUtilExt.execFormulas(formulas, updateBillItemVOs_DC, f);
		updateBillVO_DC.setChildrenVO(updateBillItemVOs_DC);
		updateBillVO_DC.m_voOld = updateSrcBillVO_DC;
		new PfUtilBO().processAction("SAVE", "4Y", currTime.getDate().toString(), null, updateBillVO_DC, updateSrcBillVO_DC);

		// 解决原系统bug: 收发类别被公式执行为空, 默认:移动仓库出库, 承运商ID被清空
		getJdbc().executeUpdate("update ic_general_h set cdispatcherid='1016A21000000000WNZO',cwastewarehouseid='" + pk_cys + "' where cgeneralhid='" + billid_DC + "'");

		// ----------------------- start sign DC
		ArrayList<GeneralBillVO> signBillVOList_DC = billDMO.queryBillByPks(new String[] {
			billid_DC
		});
		GeneralBillVO signBillVO_DC = signBillVOList_DC.get(0);
		signBillVO_DC.setLockOperatorid(pk_user);
		new PfUtilBO().processAction("SIGN", "4Y", currTime.getDate().toString(), null, signBillVO_DC, null);
		// ----------------------- end sign DC

		// ----------------------- start sign FR --------------------------
		// 不需要自动签字调拨入库单
		//		String sql_queryFRHeadId = "select cgeneralhid from ic_general_b where nvl(dr,0)=0 and csourcebillhid='" + billid_DC + "'";
		//		String bill_OID_FR = (String) getJdbc().getObject(sql_queryFRHeadId);
		//		ArrayList<GeneralBillVO> signBillVOList_FR = billDMO.queryBillByPks(new String[] {
		//			bill_OID_FR
		//		});
		//		GeneralBillVO signBillVO_FR = signBillVOList_FR.get(0);
		//		signBillVO_FR.setLockOperatorid(pk_user);
		//		new PfUtilBO().processAction("SIGN", "4E", currTime.getDate().toString(), null, signBillVO_FR, null);
		// ----------------------- end sign FR --------------------------

		StringBuilder ret = new StringBuilder();
		ret.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		ret.append("<root>");
		ret.append("<actiontype>").append(root.get("actiontype")).append("</actiontype>");
		ret.append("<sender>").append(root.get("sender")).append("</sender>");
		ret.append("<billid>").append(signBillVO_DC.getHeaderVO().getPrimaryKey()).append("</billid>");
		ret.append("<billno>").append(signBillVO_DC.getHeaderVO().getVbillcode()).append("</billno>");
		ret.append("</root>");
		return ret.toString();
	}

	public String deleteDBCK(Map<String, Object> root) throws Exception {
		String billid_DC = (String) root.get("billid");
		String pk_user = (String) root.get("pk_user");
		UFDateTime currTime = new UFDateTime(System.currentTimeMillis());
		String billid_FR = (String) getJdbc().getObject("select cgeneralhid from ic_general_b where csourcebillhid='" + billid_DC + "' and nvl(dr,0)=0");
		GeneralBillDMO billDMO = new GeneralBillDMO();
		ArrayList<GeneralBillVO> cancelSignBillVOList_FR = billDMO.queryBillByPks(new String[] {
			billid_FR
		});
		GeneralBillVO cancelSignBillVO_FR = cancelSignBillVOList_FR.get(0);
		cancelSignBillVO_FR.setLockOperatorid(pk_user);
		new PfUtilBO().processAction("CANCELSIGN", "4E", currTime.getDate().toString(), null, cancelSignBillVO_FR, null);

		ArrayList<GeneralBillVO> deleteBillVOList_FR = billDMO.queryBillByPks(new String[] {
			billid_FR
		});
		GeneralBillVO deleteBillVO_FR = deleteBillVOList_FR.get(0);
		deleteBillVO_FR.setLockOperatorid(pk_user);
		new PfUtilBO().processAction("DELETE", "4E", currTime.getDate().toString(), null, deleteBillVO_FR, null);

		ArrayList<GeneralBillVO> cancelSignBillVOList_DC = billDMO.queryBillByPks(new String[] {
			billid_DC
		});
		GeneralBillVO cancelSignBillVO_DC = cancelSignBillVOList_DC.get(0);
		String billno = cancelSignBillVO_DC.getHeaderVO().getVbillcode();
		cancelSignBillVO_DC.setLockOperatorid(pk_user);
		new PfUtilBO().processAction("CANCELSIGN", "4Y", currTime.getDate().toString(), null, cancelSignBillVO_DC, null);

		ArrayList<GeneralBillVO> deleteBillVOList_DC = billDMO.queryBillByPks(new String[] {
			billid_DC
		});
		GeneralBillVO deleteBillVO_DC = deleteBillVOList_DC.get(0);
		deleteBillVO_DC.setLockOperatorid(pk_user);
		new PfUtilBO().processAction("DELETE", "4Y", currTime.getDate().toString(), null, deleteBillVO_DC, null);

		StringBuilder ret = new StringBuilder();
		ret.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		ret.append("<root>");
		ret.append("<actiontype>").append(root.get("actiontype")).append("</actiontype>");
		ret.append("<sender>").append(root.get("sender")).append("</sender>");
		ret.append("<billid>").append(billid_DC).append("</billid>");
		ret.append("<billno>").append(billno).append("</billno>");
		ret.append("</root>");
		return ret.toString();
	}

	public String editDBRK(Map<String, Object> root) throws Exception {
		Map<String, Object> billMap = (Map<String, Object>) root.get("bill");
		Map<String, Object> headMap = (Map<String, Object>) billMap.get("head");
		String billid = (String) headMap.get("billid");
		String pk_user = (String) headMap.get("pk_user");
		String rq = (String) headMap.get("rq");
		List<Map<String, Object>> bodyMapList;
		Object obj = ((Map<String, Object>) billMap.get("bodys")).get("body");
		if (obj instanceof Map) {
			bodyMapList = new ArrayList<Map<String, Object>>();
			bodyMapList.add((Map<String, Object>) obj);
		} else {
			bodyMapList = (List<Map<String, Object>>) obj;
		}
		UFDateTime currTime = new UFDateTime(System.currentTimeMillis());

		GeneralBillDMO billDMO = new GeneralBillDMO();

		ArrayList<GeneralBillVO> cancelSignBillVOList = billDMO.queryBillByPks(new String[] {
			billid
		});
		GeneralBillVO cancelSignBillVO = cancelSignBillVOList.get(0);

		Integer fbillflag = cancelSignBillVO.getHeaderVO().getFbillflag();
		if (fbillflag.intValue() == IBillStatus.COMMIT) {
			cancelSignBillVO.setLockOperatorid(pk_user);
			new PfUtilBO().processAction("CANCELSIGN", "4E", currTime.getDate().toString(), null, cancelSignBillVO, null);
		}
		// 开始更新
		LocatorDMO locatorDMO = new LocatorDMO();
		ArrayList<GeneralBillVO> updateBillVOList = billDMO.queryBillByPks(new String[] {
			billid
		});
		GeneralBillVO updateSrcBillVO = updateBillVOList.get(0);
		updateSrcBillVO.setLocators(locatorDMO.queryByBillPK(updateSrcBillVO.getPrimaryKey()));
		GeneralBillVO updateBillVO = (GeneralBillVO) updateSrcBillVO.clone();
		updateBillVO.setLockOperatorid(pk_user);
		GeneralBillHeaderVO updateBillHeadVO = updateBillVO.getHeaderVO();
		String pk_stordoc = updateBillHeadVO.getCwarehouseid();
		setAttrValue(updateBillHeadVO, "dbilldate", rq);
		String billno = updateBillHeadVO.getVbillcode();
		GeneralBillItemVO[] updateBillItemVOs = updateBillVO.getItemVOs();
		for (GeneralBillItemVO updateBillItemVO : updateBillItemVOs) {
			String updateBillItem_OID = updateBillItemVO.getPrimaryKey();
			updateBillItemVO.setStatus(VOStatus.UPDATED);
			LocatorVO[] locatorVOs = updateBillItemVO.getLocator();
			if (locatorVOs == null || locatorVOs.length <= 0) throw new Exception("原单据货位信息错误,请删除重新制单!");
			for (int i = 0; i < bodyMapList.size(); i++) {
				String billbid = (String) bodyMapList.get(i).get("billbid");
				if (billbid.equals(updateBillItem_OID)) {
					String cspaceid = getHW((String) bodyMapList.get(i).get("hw"), pk_stordoc);
					if (StringUtils.isEmpty(cspaceid)) throw new Exception("调入货位不存在!");
					locatorVOs[0].setCspaceid(cspaceid);
					updateBillItemVO.setLocStatus(VOStatus.UPDATED);
					bodyMapList.remove(i);
					break;
				}
			}
			updateBillItemVO.setLocator(locatorVOs);
		}
		updateBillVO.m_voOld = updateSrcBillVO;
		new PfUtilBO().processAction("SAVE", "4E", currTime.getDate().toString(), null, updateBillVO, updateSrcBillVO);

		// ----------------------- start sign FR --------------------------
		ArrayList<GeneralBillVO> signBillVOList_FR = billDMO.queryBillByPks(new String[] {
			billid
		});
		GeneralBillVO signBillVO_FR = signBillVOList_FR.get(0);
		signBillVO_FR.setLockOperatorid(pk_user);
		new PfUtilBO().processAction("SIGN", "4E", currTime.getDate().toString(), null, signBillVO_FR, null);
		// ----------------------- end sign FR --------------------------

		StringBuilder ret = new StringBuilder();
		ret.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		ret.append("<root>");
		ret.append("<actiontype>").append(root.get("actiontype")).append("</actiontype>");
		ret.append("<sender>").append(root.get("sender")).append("</sender>");
		ret.append("<billid>").append(billid).append("</billid>");
		ret.append("<billno>").append(billno).append("</billno>");
		ret.append("</root>");
		return ret.toString();
	}

	public String deleteDBRK(Map<String, Object> root) throws Exception {
		String billid = (String) root.get("billid");
		String pk_user = (String) root.get("pk_user");
		UFDateTime currTime = new UFDateTime(System.currentTimeMillis());
		GeneralBillDMO billDMO = new GeneralBillDMO();
		ArrayList<GeneralBillVO> cancelSignBillVOList = billDMO.queryBillByPks(new String[] {
			billid
		});
		GeneralBillVO cancelSignBillVO = cancelSignBillVOList.get(0);
		String billno = cancelSignBillVO.getHeaderVO().getVbillcode();
		cancelSignBillVO.setLockOperatorid(pk_user);
		new PfUtilBO().processAction("CANCELSIGN", "4E", currTime.getDate().toString(), null, cancelSignBillVO, null);

		ArrayList<GeneralBillVO> deleteBillVOList = billDMO.queryBillByPks(new String[] {
			cancelSignBillVO.getHeaderVO().getPrimaryKey()
		});
		GeneralBillVO deleteBillVO = deleteBillVOList.get(0);
		deleteBillVO.setLockOperatorid(pk_user);
		new PfUtilBO().processAction("DELETE", "4E", currTime.getDate().toString(), null, deleteBillVO, null);
		StringBuilder ret = new StringBuilder();
		ret.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		ret.append("<root>");
		ret.append("<actiontype>").append(root.get("actiontype")).append("</actiontype>");
		ret.append("<sender>").append(root.get("sender")).append("</sender>");
		ret.append("<billid>").append(billid).append("</billid>");
		ret.append("<billno>").append(billno).append("</billno>");
		ret.append("</root>");
		return ret.toString();
	}

}
