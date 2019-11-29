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
import nc.itf.dm.dm102.IDeliverydailyplanQuery;
import nc.itf.dm.dm104.IDelivbillQuery;
import nc.vo.dm.dm104.DelivbillHHeaderVO;
import nc.vo.dm.dm104.DelivbillHItemVO;
import nc.vo.dm.dm104.DelivbillHVO;
import nc.vo.dm.pub.DMDataVO;
import nc.vo.ic.pub.SmartVOUtilExt;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.locator.LocatorVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.IBillStatus;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

/**
 * 销售出库服务类
 * @author Administrator
 * @date Mar 31, 2014 3:45:37 PM
 * @type nc.ws.baoyin.XCService
 * @corporation 上海锐鸟软件有限公司
 * @website www.rino123.com
 * @mail zap_168@163.com
 */
@SuppressWarnings("unchecked")
public class SOService extends BaseService {

	public String addXSCK_SH(Map<String, Object> root) throws Exception {
		Map<String, Object> billMap = (Map<String, Object>) root.get("bill");
		Map<String, Object> headMap = (Map<String, Object>) billMap.get("head");
		UFDateTime currTime = new UFDateTime(System.currentTimeMillis());
		String pk_corp = (String) headMap.get("pk_corp");
		String pk_user = (String) headMap.get("pk_user");
		String xjxc = (String) headMap.get("xjxc");
		String pk_stordoc = getCK((String) headMap.get("ck"), pk_corp);
		String pk_delivbill_h = (String) headMap.get("pk_delivbill_h");
		IDelivbillQuery boFD = (IDelivbillQuery) NCLocator.getInstance().lookup(IDelivbillQuery.class.getName());
		DelivbillHVO billVO_FD = boFD.delivbill_findByPrimaryKey(pk_delivbill_h);
		if (billVO_FD == null) throw new Exception("当前发运单已被删除!");
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
		double yfslhj = 0.00;
		for (int i = 0; i < bodyMapList.size(); i++) {
			yfslhj += new UFDouble(bodyMapList.get(i).get("sl").toString()).doubleValue();
		}
		GeneralBillVO insertBillVO = new GeneralBillVO();
		insertBillVO.setLockOperatorid(pk_user);
		GeneralBillHeaderVO insertBillHeadVO = new GeneralBillHeaderVO();
		String billVO_OID = OidGenerator.getInstance().nextOid();
		insertBillHeadVO.setPrimaryKey(billVO_OID);
		insertBillHeadVO.setStatus(VOStatus.NEW);
		setAttrValue(insertBillHeadVO, "cbilltypecode", "4C");
		setAttrValue(insertBillHeadVO, "cbiztype", bodyVO_FDs[0].getCbiztype());
		setAttrValue(insertBillHeadVO, "ccustomerid", bodyVO_FDs[0].getPkcusmandoc());
		setAttrValue(insertBillHeadVO, "cdilivertypeid", headVO_FD.getPkdelivmode());
		setAttrValue(insertBillHeadVO, "cdptid", "1016A210000000000487");// 部门,默认:销售部
		setAttrValue(insertBillHeadVO, "cwarehouseid", pk_stordoc);
		setAttrValue(insertBillHeadVO, "cwastewarehouseid", getCYS((String) headMap.get("cys")));
		setAttrValue(insertBillHeadVO, "dbilldate", currTime.getDate());
		setAttrValue(insertBillHeadVO, "pk_calbody", bodyVO_FDs[0].getPksendstockorg());
		setAttrValue(insertBillHeadVO, "pk_corp", pk_corp);
		setAttrValue(insertBillHeadVO, "pk_defdoc8", headVO_FD.getPk_defdoc7());
		setAttrValue(insertBillHeadVO, "vdiliveraddress", bodyVO_FDs[0].getVdestaddress());
		setAttrValue(insertBillHeadVO, "vuserdef11", headVO_FD.getVuserdef10()); // 整垛数量
		setAttrValue(insertBillHeadVO, "vuserdef8", headVO_FD.getVuserdef7());
		setAttrValue(insertBillHeadVO, "vuserdef7", headMap.get("cph"));// 车牌号
		setAttrValue(insertBillHeadVO, "vuserdef16", headMap.get("jsy"));// 驾驶员
		setAttrValue(insertBillHeadVO, "ccustomerid", bodyVO_FDs[0].getPkcusmandoc());
		setAttrValue(insertBillHeadVO, "coperatorid", pk_user);
		setAttrValue(insertBillHeadVO, "coperatoridnow", pk_user);
		GeneralBillItemVO[] insertBillItemVOs = new GeneralBillItemVO[bodyMapList.size()];
		String[] updateFDSQLs = new String[bodyMapList.size()];
		for (int i = 0; i < bodyMapList.size(); i++) {
			Map<String, Object> bodyMap = bodyMapList.get(i);
			GeneralBillItemVO insertBillItemVO = new GeneralBillItemVO();
			insertBillItemVO.setCgeneralhid(billVO_OID);
			insertBillItemVO.setStatus(VOStatus.NEW);
			String invcode = (String) bodyMap.get("lh");
			String pk_invbasdoc = (String) getJdbc().getObject("select pk_invbasdoc from bd_invbasdoc where invcode='" + invcode + "'");
			DelivbillHItemVO bodyVO_FD = null;
			for (int j = 0; j < bodyVO_FDs.length; j++) {
				String delInvPK = getInvBasePK(bodyVO_FDs[j].getPkinv());
				if (delInvPK.equals(pk_invbasdoc)) {
					bodyVO_FD = bodyVO_FDs[j];
					break;
				}
			}
			setAttrValue(insertBillItemVO, "cfirstbillbid", bodyVO_FD.getPkorderrow());
			setAttrValue(insertBillItemVO, "cfirstbillhid", bodyVO_FD.getPkorder());
			setAttrValue(insertBillItemVO, "cfirsttype", bodyVO_FD.getVbilltype());
			setAttrValue(insertBillItemVO, "cfreezeid", bodyVO_FD.getPkorderrow());
			setAttrValue(insertBillItemVO, "cinventoryid", bodyVO_FD.getPkinv());
			setAttrValue(insertBillItemVO, "cquoteunitid", bodyVO_FD.getCQuoteunitid());
			setAttrValue(insertBillItemVO, "creceieveid", bodyVO_FD.getPkcusmandoc());
			setAttrValue(insertBillItemVO, "crowno", (i + 1) * 10); // 行号
			setAttrValue(insertBillItemVO, "csourcebillbid", bodyVO_FD.getPk_delivbill_b());
			setAttrValue(insertBillItemVO, "csourcebillhid", headVO_FD.getPk_delivbill_h());
			setAttrValue(insertBillItemVO, "csourcetype", "7F"); // 来源单据类型,默认:发运单
			setAttrValue(insertBillItemVO, "dbizdate", currTime.getDate());
			UFDouble noutnum = new UFDouble(bodyMap.get("sl").toString());
			setAttrValue(insertBillItemVO, "noutnum", noutnum); // 出库数量
			UFDouble nquoteprice = getSalePrice(bodyVO_FD.getPkorderrow());
			setAttrValue(insertBillItemVO, "nquoteprice", nquoteprice); // 单价,需要从销售订单获取
			setAttrValue(insertBillItemVO, "nquotemny", noutnum.multiply(nquoteprice).setScale(2, UFDouble.ROUND_HALF_UP)); // 金额,单价乘以数量保留2位小数
			setAttrValue(insertBillItemVO, "nquoteunitnum", noutnum);
			setAttrValue(insertBillItemVO, "nquoteunitrate", getSaleRate(bodyVO_FD.getPkorderrow())); // 税率,需要从销售订单取
			setAttrValue(insertBillItemVO, "nshouldoutnum", new UFDouble(yfslhj)); // 应发数量
			setAttrValue(insertBillItemVO, "pk_corp", pk_corp);
			setAttrValue(insertBillItemVO, "pk_defdoc13", bodyVO_FD.getPk_defdoc12());
			setAttrValue(insertBillItemVO, "vfirstbillcode", bodyVO_FD.getVordercode());
			setAttrValue(insertBillItemVO, "vfirstrowno", getSaleRowNo(bodyVO_FD.getPkorderrow())); // 销售订单行号,需要从销售订单取
			setAttrValue(insertBillItemVO, "vsourcebillcode", headVO_FD.getVdelivbillcode());
			setAttrValue(insertBillItemVO, "vsourcerowno", bodyVO_FD.getIrownumber());
			setAttrValue(insertBillItemVO, "vuserdef13", bodyVO_FD.getVuserdef12());
			setAttrValue(insertBillItemVO, "vuserdef18", 1);
			setAttrValue(insertBillItemVO, "vbatchcode", bodyMap.get("ph"));
			String vfree1 = (String) bodyMap.get("dh");
			setAttrValue(insertBillItemVO, "vfree1", vfree1);
			String cspaceid = getCSpaceid(vfree1, pk_stordoc, pk_corp, pk_invbasdoc); // 根据自由项找到货位
			if (StringUtils.isEmpty(cspaceid)) throw new Exception("未分配货位:料号[" + invcode + "],垛号[" + vfree1 + "]");
			LocatorVO locatorVO = new LocatorVO(cspaceid);
			locatorVO.setNoutspacenum(noutnum);
			locatorVO.setPk_corp(pk_corp);
			insertBillItemVO.setLocator(new LocatorVO[] {
				locatorVO
			});
			insertBillItemVOs[i] = insertBillItemVO;
			updateFDSQLs[i] = "update dm_delivbill_b set doutnum = doutnum-" + noutnum + " where pk_delivbill_b = '" + bodyVO_FD.getPrimaryKey() + "'";
		}

		// 校验先进先出
		if (xjxc.equals("Y")) {
			String[] dhs = new String[] {};
			for (int i = 0; i < insertBillItemVOs.length; i++) {
				dhs = (String[]) ArrayUtils.add(dhs, insertBillItemVOs[i].getVfree1());
			}
			checkXJXC(dhs);
		}
		// end

		insertBillVO.setParentVO(insertBillHeadVO);
		insertBillVO.setChildrenVO(insertBillItemVOs);
		new IC2DMInterface().saveOutDM(new GeneralBillVO[] {
			insertBillVO
		});
		for (int j = 0; j < updateFDSQLs.length; j++) {
			getJdbc().executeUpdate(updateFDSQLs[j]);
		}
		GeneralBillDMO generalBillDMO = new GeneralBillDMO();
		ArrayList<GeneralBillVO> signBillVOList = generalBillDMO.queryBillByPks(new String[] {
			billVO_OID
		});
		GeneralBillVO signBillVO = signBillVOList.get(0);
		signBillVO.setLockOperatorid(pk_user);
		new PfUtilBO().processAction("SIGN", "4C", currTime.getDate().toString(), null, signBillVO, null);
		StringBuilder ret = new StringBuilder();
		ret.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		ret.append("<root>");
		ret.append("<actiontype>").append(root.get("actiontype")).append("</actiontype>");
		ret.append("<sender>").append(root.get("sender")).append("</sender>");
		ret.append("<billid>").append(signBillVO.getParentVO().getPrimaryKey()).append("</billid>");
		ret.append("<billno>").append(signBillVO.getParentVO().getAttributeValue("vbillcode")).append("</billno>");
		ret.append("</root>");
		return ret.toString();
	}

	public String deleteXSCK_SH(Map<String, Object> root) throws Exception {
		String billid = (String) root.get("billid");
		String pk_user = (String) root.get("pk_user");
		UFDateTime currTime = new UFDateTime(System.currentTimeMillis());
		GeneralBillDMO generalBillDMO = new GeneralBillDMO();
		ArrayList<GeneralBillVO> cancelSignBillVOList = generalBillDMO.queryBillByPks(new String[] {
			billid
		});
		GeneralBillVO cancelSignBillVO = cancelSignBillVOList.get(0);
		String billno = cancelSignBillVO.getHeaderVO().getVbillcode();
		cancelSignBillVO.setLockOperatorid(pk_user);
		new PfUtilBO().processAction("CANCELSIGN", "4C", currTime.getDate().toString(), null, cancelSignBillVO, null);

		ArrayList<GeneralBillVO> deleteBillVOList = generalBillDMO.queryBillByPks(new String[] {
			cancelSignBillVO.getHeaderVO().getPrimaryKey()
		});
		GeneralBillVO deleteBillVO = deleteBillVOList.get(0);
		deleteBillVO.setLockOperatorid(pk_user);
		new PfUtilBO().processAction("DELETE", "4C", currTime.getDate().toString(), null, deleteBillVO, null);
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

	/**
	 * 新增的行没有增加到销售出库单表体,删除的行是可以的
	 */
	public String editXSCK_SH(Map<String, Object> root) throws Exception {
		Map<String, Object> billMap = (Map<String, Object>) root.get("bill");
		Map<String, Object> headMap = (Map<String, Object>) billMap.get("head");
		UFDateTime currTime = new UFDateTime(System.currentTimeMillis());
		String pk_corp = (String) headMap.get("pk_corp");
		String pk_user = (String) headMap.get("pk_user");
		String xjxc = (String) headMap.get("xjxc");
		String pk_stordoc = getCK((String) headMap.get("ck"), pk_corp);
		String cgeneralhid = (String) headMap.get("billid");
		GeneralBillDMO dmo = new GeneralBillDMO();

		//------------------- 取消签字销售出库单(送货) (系统会自动取消签字销售成本结转并删除)
		ArrayList<GeneralBillVO> cancelSignBillVOList = dmo.queryBillByPks(new String[] {
			cgeneralhid
		});
		if (cancelSignBillVOList == null || cancelSignBillVOList.size() <= 0) throw new Exception("当前单据已被删除,请确认!");
		GeneralBillVO cancelSignBillVO = cancelSignBillVOList.get(0);
		Integer cancelSignBillVO_Fbillflag = cancelSignBillVO.getHeaderVO().getFbillflag();
		if (cancelSignBillVO_Fbillflag.intValue() == IBillStatus.COMMIT) {
			cancelSignBillVO.setLockOperatorid(pk_user);
			cancelSignBillVO.getHeaderVO().setDaccountdate(currTime.getDate());
			new PfUtilBO().processAction("CANCELSIGN", "4C", currTime.getDate().toString(), null, cancelSignBillVO, null);
		}

		// -------------------- 更新销售出库单(送货)
		LocatorDMO locatorDMO = new LocatorDMO();
		ArrayList<GeneralBillVO> updateBillVOList = dmo.queryBillByPks(new String[] {
			cgeneralhid
		});
		GeneralBillVO updateSrcBillVO = updateBillVOList.get(0);
		updateSrcBillVO.setLocators(locatorDMO.queryByBillPK(updateSrcBillVO.getHeaderVO().getPrimaryKey()));
		GeneralBillVO updateBillVO = (GeneralBillVO) updateSrcBillVO.clone();
		updateBillVO.setLockOperatorid(pk_user);
		List<Map<String, Object>> bodyMapList;
		Object obj = ((Map<String, Object>) billMap.get("bodys")).get("body");
		if (obj instanceof Map) {
			bodyMapList = new ArrayList<Map<String, Object>>();
			bodyMapList.add((Map<String, Object>) obj);
		} else {
			bodyMapList = (List<Map<String, Object>>) obj;
		}
		GeneralBillHeaderVO updateBillHeadVO = updateBillVO.getHeaderVO();
		GeneralBillItemVO[] updateBillItemVOs = (GeneralBillItemVO[]) updateBillVO.getChildrenVO();
		setAttrValue(updateBillHeadVO, "cwastewarehouseid", getCYS((String) headMap.get("cys"))); // 承运商
		setAttrValue(updateBillHeadVO, "vuserdef7", headMap.get("cph"));// 车牌号
		setAttrValue(updateBillHeadVO, "vuserdef16", headMap.get("jsy"));// 驾驶员
		setAttrValue(updateBillHeadVO, "cwarehouseid", pk_stordoc); // 仓库
		
		
		// 已经存在的行做更新,源单据不存在了,则先删除,再增加新的传递过来的行
		Integer[] updateIdxs = new Integer[] {};
		for (int i = 0; i < bodyMapList.size(); i++) {
			Map<String, Object> bodyMap = bodyMapList.get(i);
			String cgeneralbid = (String) bodyMap.get("cgeneralbid");
			for (int j = 0; j < updateBillItemVOs.length; j++) {
				String bid_src = updateBillItemVOs[j].getCgeneralbid();
				if (cgeneralbid.equals(bid_src)) {
					updateIdxs = (Integer[]) ArrayUtils.add(updateIdxs, i);
					break;
				}
			}
		}
		// 源单据表体需要删除的行
		for (int i = 0; i < updateBillItemVOs.length; i++) {
			String bid_src = updateBillItemVOs[i].getCgeneralbid();
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
				updateBillItemVOs[i].setStatus(VOStatus.DELETED);
				updateBillItemVOs[i].setLocStatus(VOStatus.DELETED);
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
			for (int j = 0; j < updateBillItemVOs.length; j++) {
				String cinvbasid = getInvBasePK(updateBillItemVOs[j].getCinventoryid());
				if (cinvbasid.equals(pk_invbasdoc)) {
					itemVO_I = (GeneralBillItemVO) updateBillItemVOs[j].clone();
					break;
				}
			}
			if(itemVO_I == null) {
				itemVO_I = (GeneralBillItemVO) updateBillItemVOs[0].clone();
				String invmanpk = getInvManPK(invcode, pk_corp);
				itemVO_I.setCinventoryid(invmanpk);
				itemVO_I.setCinvbasid(pk_invbasdoc);
			}
			itemVO_I.setStatus(VOStatus.NEW);
			itemVO_I.setLocStatus(VOStatus.NEW);
			
			UFDouble noutnum = new UFDouble(bodyMap.get("sl").toString());
			setAttrValue(itemVO_I, "noutnum", noutnum); // 出库数量
			UFDouble nquoteprice = (UFDouble) itemVO_I.getAttributeValue("nquoteprice");
			setAttrValue(itemVO_I, "nquotemny", noutnum.multiply(nquoteprice).setScale(2, UFDouble.ROUND_HALF_UP)); // 金额,单价乘以数量保留2位小数
			setAttrValue(itemVO_I, "nquoteunitnum", noutnum);
			setAttrValue(itemVO_I, "nshouldoutnum", new UFDouble(yfslhj));
			setAttrValue(itemVO_I, "vbatchcode", bodyMap.get("ph"));
			String vfree1 = (String) bodyMap.get("dh");
			setAttrValue(itemVO_I, "vfree1", vfree1);
			String cspaceid = getCSpaceid(vfree1, pk_stordoc, pk_corp, pk_invbasdoc); // 根据自由项找到货位
			if (StringUtils.isEmpty(cspaceid)) throw new Exception("未分配货位:料号[" + invcode + "],垛号[" + vfree1 + "]");
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
			updateBillItemVOs = (GeneralBillItemVO[]) ArrayUtils.add(updateBillItemVOs, itemVO_I);
		}
		
		// 校验先进先出
		if (xjxc.equals("Y")) {
			String[] dhs = new String[] {};
			for (int i = 0; i < updateBillItemVOs.length; i++) {
				if (updateBillItemVOs[i].getStatus() == VOStatus.DELETED) continue;
				dhs = (String[]) ArrayUtils.add(dhs, updateBillItemVOs[i].getVfree1());
			}
			checkXJXC(dhs);
		}
		// end

		FormulaParse f = new FormulaParse();
		String[] formulas = {
				"cinvbasid->getColValue(bd_invmandoc,pk_invbasdoc,pk_invmandoc,cinventoryid)",//
				"cquotecurrency->getColValue(so_saleorder_b,ccurrencytypeid,corder_bid,cfirstbillbid)",
				"nquoteprice->getColValue(so_saleorder_b,norgqttaxnetprc,corder_bid,cfirstbillbid)"
		};
		SmartVOUtilExt.execFormulas(formulas, updateBillItemVOs, f);
		updateBillVO.setChildrenVO(updateBillItemVOs);
		updateBillVO.m_voOld = updateSrcBillVO;
		new PfUtilBO().processAction("SAVE", "4C", currTime.getDate().toString(), null, updateBillVO, updateSrcBillVO);

		ArrayList<GeneralBillVO> signBillVOList = dmo.queryBillByPks(new String[] {
			cgeneralhid
		});
		GeneralBillVO signBillVO = signBillVOList.get(0);
		signBillVO.setLockOperatorid(pk_user);
		new PfUtilBO().processAction("SIGN", "4C", currTime.getDate().toString(), null, signBillVO, null);

		StringBuilder ret = new StringBuilder();
		ret.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		ret.append("<root>");
		ret.append("<actiontype>").append(root.get("actiontype")).append("</actiontype>");
		ret.append("<sender>").append(root.get("sender")).append("</sender>");
		ret.append("<billid>").append(signBillVO.getParentVO().getPrimaryKey()).append("</billid>");
		ret.append("<billno>").append(signBillVO.getParentVO().getAttributeValue("vbillcode")).append("</billno>");
		ret.append("</root>");
		return ret.toString();
	}

	public String addXSCK_ZT(Map<String, Object> root) throws Exception {
		Map<String, Object> billMap = (Map<String, Object>) root.get("bill");
		Map<String, Object> headMap = (Map<String, Object>) billMap.get("head");
		UFDateTime currTime = new UFDateTime(System.currentTimeMillis());
		String pk_corp = (String) headMap.get("pk_corp");
		String pk_user = (String) headMap.get("pk_user");
		String xjxc = (String) headMap.get("xjxc");
		String pk_stordoc = getCK((String) headMap.get("ck"), pk_corp);
		List<Map<String, Object>> bodyMapList;
		Object obj = ((Map<String, Object>) billMap.get("bodys")).get("body");
		if (obj instanceof Map) {
			bodyMapList = new ArrayList<Map<String, Object>>();
			bodyMapList.add((Map<String, Object>) obj);
		} else {
			bodyMapList = (List<Map<String, Object>>) obj;
		}
		String[] pk_delivdaypls = new String[bodyMapList.size()];
		for (int i = 0; i < pk_delivdaypls.length; i++) {
			pk_delivdaypls[i] = (String) bodyMapList.get(i).get("pk_delivdaypl");
		}
		
		double yfslhj = 0.00;
		for (int i = 0; i < bodyMapList.size(); i++) {
			yfslhj += new UFDouble(bodyMapList.get(i).get("sl").toString()).doubleValue();
		}
		IDeliverydailyplanQuery dpQuery = (IDeliverydailyplanQuery) NCLocator.getInstance().lookup(IDeliverydailyplanQuery.class.getName());
		DMDataVO conditionVO = new DMDataVO();
		conditionVO.setAttributeValue("pk_delivdaypls", pk_delivdaypls);
		DMDataVO[] dmDataVOs = dpQuery.queryDeliverydailyplan(conditionVO, null);
		GeneralBillVO insertBillVO = new GeneralBillVO();
		insertBillVO.setLockOperatorid(pk_user);
		GeneralBillHeaderVO insertBillHeadVO = new GeneralBillHeaderVO();
		setAttrValue(insertBillHeadVO, "cbilltypecode", "4C");
		setAttrValue(insertBillHeadVO, "cbiztype", dmDataVOs[0].getAttributeValue("cbiztype"));
		setAttrValue(insertBillHeadVO, "ccustomerid", dmDataVOs[0].getAttributeValue("pkcust"));
		setAttrValue(insertBillHeadVO, "cdptid", "1016A210000000000487");// 部门,默认:销售部
		setAttrValue(insertBillHeadVO, "cwarehouseid", pk_stordoc);
		setAttrValue(insertBillHeadVO, "dbilldate", currTime.getDate());
		setAttrValue(insertBillHeadVO, "pk_calbody", "1016A21000000000WLM3"); // 默认库存组织
		setAttrValue(insertBillHeadVO, "pk_corp", pk_corp);
		setAttrValue(insertBillHeadVO, "pk_defdoc8", dmDataVOs[0].getAttributeValue("pk_defdoc7"));
		setAttrValue(insertBillHeadVO, "vdiliveraddress", dmDataVOs[0].getAttributeValue("vdestaddress"));
		setAttrValue(insertBillHeadVO, "vuserdef11", dmDataVOs[0].getAttributeValue("vuserdef10")); // 整垛数量
		setAttrValue(insertBillHeadVO, "vuserdef8", dmDataVOs[0].getAttributeValue("vuserdef7")); // 送货方式
		setAttrValue(insertBillHeadVO, "vuserdef7", headMap.get("cph"));// 车牌号
		setAttrValue(insertBillHeadVO, "vuserdef16", headMap.get("jsy"));// 驾驶员
		setAttrValue(insertBillHeadVO, "coperatorid", pk_user);
		setAttrValue(insertBillHeadVO, "coperatoridnow", pk_user);
		String billVO_OID = OidGenerator.getInstance().nextOid();
		insertBillHeadVO.setPrimaryKey(billVO_OID);
		insertBillHeadVO.setStatus(VOStatus.NEW);
		GeneralBillItemVO[] insertBillItemVOs = new GeneralBillItemVO[bodyMapList.size()];
		for (int i = 0; i < bodyMapList.size(); i++) {
			Map<String, Object> bodyMap = bodyMapList.get(i);
			GeneralBillItemVO insertBillItemVO = new GeneralBillItemVO();
			insertBillItemVO.setCgeneralhid(billVO_OID);
			insertBillItemVO.setStatus(VOStatus.NEW);
			String invcode = (String) bodyMap.get("lh");
			String pk_invbasdoc = (String) getJdbc().getObject("select pk_invbasdoc from bd_invbasdoc where invcode='" + invcode + "'");
			DMDataVO currDMVO = null;
			for (int j = 0; j < dmDataVOs.length; j++) {
				String invbaspk = getInvBasePK((String) dmDataVOs[j].getAttributeValue("pkinv"));
				if (invbaspk.equals(pk_invbasdoc)) {
					currDMVO = dmDataVOs[j];
					break;
				}
			}
			setAttrValue(insertBillItemVO, "cfirstbillbid", currDMVO.getAttributeValue("pkbillb"));
			setAttrValue(insertBillItemVO, "cfirstbillhid", currDMVO.getAttributeValue("pkbillh"));
			setAttrValue(insertBillItemVO, "cfirsttype", currDMVO.getAttributeValue("vbilltype"));
			setAttrValue(insertBillItemVO, "cinventoryid", currDMVO.getAttributeValue("pkinv"));
			setAttrValue(insertBillItemVO, "cquoteunitid", currDMVO.getAttributeValue("cquoteunitid"));
			setAttrValue(insertBillItemVO, "creceieveid", currDMVO.getAttributeValue("creceiptcorpid"));
			setAttrValue(insertBillItemVO, "crowno", (i + 1) * 10); // 行号
			setAttrValue(insertBillItemVO, "csourcebillbid", currDMVO.getAttributeValue("pk_delivdaypl"));
			setAttrValue(insertBillItemVO, "csourcebillhid", currDMVO.getAttributeValue("pk_delivdaypl"));
			setAttrValue(insertBillItemVO, "csourcetype", "7D"); // 来源单据类型,默认:发运单
			setAttrValue(insertBillItemVO, "dbizdate", currTime.getDate());
			UFDouble noutnum = new UFDouble(bodyMap.get("sl").toString());
			setAttrValue(insertBillItemVO, "noutnum", noutnum); // 出库数量
			setAttrValue(insertBillItemVO, "nquoteprice", currDMVO.getAttributeValue("dunitprice")); // 单价
			setAttrValue(insertBillItemVO, "nquotemny", noutnum.multiply(new UFDouble(currDMVO.getAttributeValue("dunitprice").toString())).setScale(2, UFDouble.ROUND_HALF_UP)); // 金额,单价乘以数量保留2位小数
			setAttrValue(insertBillItemVO, "nquoteunitnum", noutnum);
			setAttrValue(insertBillItemVO, "nquoteunitrate", currDMVO.getAttributeValue("nquoteunitrate")); // 税率
			setAttrValue(insertBillItemVO, "nshouldoutnum", new UFDouble(yfslhj)); // 应发数量
			setAttrValue(insertBillItemVO, "pk_corp", pk_corp);
			setAttrValue(insertBillItemVO, "pk_defdoc13", currDMVO.getAttributeValue("pk_defdoc12"));
			setAttrValue(insertBillItemVO, "vfirstbillcode", currDMVO.getAttributeValue("vsrcbillnum"));
			setAttrValue(insertBillItemVO, "vfirstrowno", getSaleRowNo((String) currDMVO.getAttributeValue("pkbillb"))); // 销售订单行号,需要从销售订单取
			setAttrValue(insertBillItemVO, "vsourcebillcode", currDMVO.getAttributeValue("vdelivdayplcode"));
			setAttrValue(insertBillItemVO, "vsourcerowno", (i + 1));
			setAttrValue(insertBillItemVO, "vuserdef13", currDMVO.getAttributeValue("vuserdef12"));
			setAttrValue(insertBillItemVO, "vuserdef18", 1); // 默认
			setAttrValue(insertBillItemVO, "vbatchcode", bodyMap.get("ph"));
			String vfree1 = (String) bodyMap.get("dh");
			setAttrValue(insertBillItemVO, "vfree1", vfree1);
			String cspaceid = getCSpaceid(vfree1, pk_stordoc, pk_corp, pk_invbasdoc); // 根据自由项找到货位
			if (StringUtils.isEmpty(cspaceid)) throw new Exception("未分配货位:料号[" + invcode + "],垛号[" + vfree1 + "]");
			LocatorVO locatorVO = new LocatorVO(cspaceid);
			locatorVO.setNoutspacenum(noutnum);
			locatorVO.setPk_corp(pk_corp);
			insertBillItemVO.setLocator(new LocatorVO[] {
				locatorVO
			});
			insertBillItemVOs[i] = insertBillItemVO;
		}

		// 校验先进先出
		if (xjxc.equals("Y")) {
			String[] dhs = new String[] {};
			for (int i = 0; i < insertBillItemVOs.length; i++) {
				dhs = (String[]) ArrayUtils.add(dhs, insertBillItemVOs[i].getVfree1());
			}
			checkXJXC(dhs);
		}
		// end

		insertBillVO.setParentVO(insertBillHeadVO);
		insertBillVO.setChildrenVO(insertBillItemVOs);
		new IC2DMInterface().saveOutDM(new GeneralBillVO[] {
			insertBillVO
		});

		// ------------------------ 签字
		GeneralBillDMO generalBillDMO = new GeneralBillDMO();
		ArrayList<GeneralBillVO> signBillVOList = generalBillDMO.queryBillByPks(new String[] {
			billVO_OID
		});
		GeneralBillVO signBillVO = signBillVOList.get(0);
		signBillVO.setLockOperatorid(pk_user);
		new PfUtilBO().processAction("SIGN", "4C", currTime.getDate().toString(), null, signBillVO, null);

		StringBuilder ret = new StringBuilder();
		ret.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		ret.append("<root>");
		ret.append("<actiontype>").append(root.get("actiontype")).append("</actiontype>");
		ret.append("<sender>").append(root.get("sender")).append("</sender>");
		ret.append("<billid>").append(signBillVO.getHeaderVO().getPrimaryKey()).append("</billid>");
		ret.append("<billno>").append(signBillVO.getHeaderVO().getVbillcode()).append("</billno>");
		ret.append("</root>");
		return ret.toString();
	}

	public String editXSCK_ZT(Map<String, Object> root) throws Exception {
		Map<String, Object> billMap = (Map<String, Object>) root.get("bill");
		Map<String, Object> headMap = (Map<String, Object>) billMap.get("head");
		UFDateTime currTime = new UFDateTime(System.currentTimeMillis());
		String pk_corp = (String) headMap.get("pk_corp");
		String pk_user = (String) headMap.get("pk_user");
		String xjxc = (String) headMap.get("xjxc");
		String pk_stordoc = getCK((String) headMap.get("ck"), pk_corp);
		String cgeneralhid = (String) headMap.get("billid");
		GeneralBillDMO dmo = new GeneralBillDMO();

		//------------------- 取消签字销售出库单(自提) (系统会自动取消签字销售成本结转并删除)
		ArrayList<GeneralBillVO> cancelSignBillVOList = dmo.queryBillByPks(new String[] {
			cgeneralhid
		});
		if (cancelSignBillVOList == null || cancelSignBillVOList.size() <= 0) throw new Exception("当前单据已被删除,请确认!");
		GeneralBillVO cancelSignBillVO = cancelSignBillVOList.get(0);
		Integer cancelSignBillVO_Fbillflag = cancelSignBillVO.getHeaderVO().getFbillflag();
		if (cancelSignBillVO_Fbillflag.intValue() == IBillStatus.COMMIT) {
			cancelSignBillVO.setLockOperatorid(pk_user);
			cancelSignBillVO.getHeaderVO().setDaccountdate(currTime.getDate());
			new PfUtilBO().processAction("CANCELSIGN", "4C", currTime.getDate().toString(), null, cancelSignBillVO, null);
		}

		// -------------------- 更新销售出库单(自提)
		LocatorDMO locatorDMO = new LocatorDMO();
		ArrayList<GeneralBillVO> updateBillVOList = dmo.queryBillByPks(new String[] {
			cgeneralhid
		});
		GeneralBillVO updateSrcBillVO = updateBillVOList.get(0);
		updateSrcBillVO.setLocators(locatorDMO.queryByBillPK(updateSrcBillVO.getHeaderVO().getPrimaryKey()));
		GeneralBillVO updateBillVO = (GeneralBillVO) updateSrcBillVO.clone();
		updateBillVO.setLockOperatorid(pk_user);
		List<Map<String, Object>> bodyMapList;
		Object obj = ((Map<String, Object>) billMap.get("bodys")).get("body");
		if (obj instanceof Map) {
			bodyMapList = new ArrayList<Map<String, Object>>();
			bodyMapList.add((Map<String, Object>) obj);
		} else {
			bodyMapList = (List<Map<String, Object>>) obj;
		}
		GeneralBillHeaderVO updateBillHeadVO = updateBillVO.getHeaderVO();
		GeneralBillItemVO[] updateBillItemVOs = (GeneralBillItemVO[]) updateBillVO.getChildrenVO();
		setAttrValue(updateBillHeadVO, "vuserdef7", headMap.get("cph"));// 车牌号
		setAttrValue(updateBillHeadVO, "vuserdef16", headMap.get("jsy"));// 驾驶员
		
		
		// 已经存在的行做更新,源单据不存在了,则先删除,再增加新的传递过来的行
		Integer[] updateIdxs = new Integer[] {};
		for (int i = 0; i < bodyMapList.size(); i++) {
			Map<String, Object> bodyMap = bodyMapList.get(i);
			String cgeneralbid = (String) bodyMap.get("cgeneralbid");
			for (int j = 0; j < updateBillItemVOs.length; j++) {
				String bid_src = updateBillItemVOs[j].getCgeneralbid();
				if (cgeneralbid.equals(bid_src)) {
					updateIdxs = (Integer[]) ArrayUtils.add(updateIdxs, i);
					break;
				}
			}
		}
		// 源单据表体需要删除的行
		for (int i = 0; i < updateBillItemVOs.length; i++) {
			String bid_src = updateBillItemVOs[i].getCgeneralbid();
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
				updateBillItemVOs[i].setStatus(VOStatus.DELETED);
				updateBillItemVOs[i].setLocStatus(VOStatus.DELETED);
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
			for (int j = 0; j < updateBillItemVOs.length; j++) {
				String cinvbasid = getInvBasePK(updateBillItemVOs[j].getCinventoryid());
				if (cinvbasid.equals(pk_invbasdoc)) {
					itemVO_I = (GeneralBillItemVO) updateBillItemVOs[j].clone();
					break;
				}
			}
			if(itemVO_I == null) {
				itemVO_I = (GeneralBillItemVO) updateBillItemVOs[0].clone();
				String invmanpk = getInvManPK(invcode, pk_corp);
				itemVO_I.setCinventoryid(invmanpk);
				itemVO_I.setCinvbasid(pk_invbasdoc);
			}
			itemVO_I.setStatus(VOStatus.NEW);
			itemVO_I.setLocStatus(VOStatus.NEW);

			UFDouble noutnum = new UFDouble(bodyMap.get("sl").toString());
			setAttrValue(itemVO_I, "noutnum", noutnum); // 出库数量
			UFDouble nquoteprice = (UFDouble) itemVO_I.getAttributeValue("nquoteprice");
			setAttrValue(itemVO_I, "nquotemny", noutnum.multiply(nquoteprice).setScale(2, UFDouble.ROUND_HALF_UP)); // 金额,单价乘以数量保留2位小数
			setAttrValue(itemVO_I, "nquoteunitnum", noutnum);
			setAttrValue(itemVO_I, "nshouldoutnum", new UFDouble(yfslhj));
			setAttrValue(itemVO_I, "vbatchcode", bodyMap.get("ph"));
			String vfree1 = (String) bodyMap.get("dh");
			setAttrValue(itemVO_I, "vfree1", vfree1);
			String cspaceid = getCSpaceid(vfree1, pk_stordoc, pk_corp, pk_invbasdoc); // 根据自由项找到货位
			if (StringUtils.isEmpty(cspaceid)) throw new Exception("未分配货位:料号[" + invcode + "],垛号[" + vfree1 + "]");
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
			updateBillItemVOs = (GeneralBillItemVO[]) ArrayUtils.add(updateBillItemVOs, itemVO_I);
		}

		// 校验先进先出
		if (xjxc.equals("Y")) {
			String[] dhs = new String[] {};
			for (int i = 0; i < updateBillItemVOs.length; i++) {
				if (updateBillItemVOs[i].getStatus() == VOStatus.DELETED) continue;
				dhs = (String[]) ArrayUtils.add(dhs, updateBillItemVOs[i].getVfree1());
			}
			checkXJXC(dhs);
		}
		// end

		FormulaParse f = new FormulaParse();
		String[] formulas = {
				"cinvbasid->getColValue(bd_invmandoc,pk_invbasdoc,pk_invmandoc,cinventoryid)",//
				"cquotecurrency->getColValue(so_saleorder_b,ccurrencytypeid,corder_bid,cfirstbillbid)",
				"nquoteprice->getColValue(so_saleorder_b,norgqttaxnetprc,corder_bid,cfirstbillbid)"
		};
		SmartVOUtilExt.execFormulas(formulas, updateBillItemVOs, f);
		updateBillVO.setChildrenVO(updateBillItemVOs);
		updateBillVO.m_voOld = updateSrcBillVO;
		new PfUtilBO().processAction("SAVE", "4C", currTime.getDate().toString(), null, updateBillVO, updateSrcBillVO);

		ArrayList<GeneralBillVO> signBillVOList = dmo.queryBillByPks(new String[] {
			cgeneralhid
		});
		GeneralBillVO signBillVO = signBillVOList.get(0);
		signBillVO.setLockOperatorid(pk_user);
		new PfUtilBO().processAction("SIGN", "4C", currTime.getDate().toString(), null, signBillVO, null);

		StringBuilder ret = new StringBuilder();
		ret.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		ret.append("<root>");
		ret.append("<actiontype>").append(root.get("actiontype")).append("</actiontype>");
		ret.append("<sender>").append(root.get("sender")).append("</sender>");
		ret.append("<billid>").append(signBillVO.getParentVO().getPrimaryKey()).append("</billid>");
		ret.append("<billno>").append(signBillVO.getParentVO().getAttributeValue("vbillcode")).append("</billno>");
		ret.append("</root>");
		return ret.toString();
	}

	public String deleteXSCK_ZT(Map<String, Object> root) throws Exception {
		String billid = (String) root.get("billid");
		String pk_user = (String) root.get("pk_user");
		UFDateTime currTime = new UFDateTime(System.currentTimeMillis());
		GeneralBillDMO generalBillDMO = new GeneralBillDMO();
		ArrayList<GeneralBillVO> cancelSignBillVOList = generalBillDMO.queryBillByPks(new String[] {
			billid
		});
		GeneralBillVO cancelSignBillVO = cancelSignBillVOList.get(0);
		String billno = cancelSignBillVO.getHeaderVO().getVbillcode();
		cancelSignBillVO.setLockOperatorid(pk_user);
		new PfUtilBO().processAction("CANCELSIGN", "4C", currTime.getDate().toString(), null, cancelSignBillVO, null);

		ArrayList<GeneralBillVO> deleteBillVOList = generalBillDMO.queryBillByPks(new String[] {
			cancelSignBillVO.getHeaderVO().getPrimaryKey()
		});
		GeneralBillVO deleteBillVO = deleteBillVOList.get(0);
		deleteBillVO.setLockOperatorid(pk_user);
		new PfUtilBO().processAction("DELETE", "4C", currTime.getDate().toString(), null, deleteBillVO, null);
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
