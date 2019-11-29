package nc.bs.dm.dm004;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import javax.naming.NamingException;
import nc.bs.dm.pub.DmDMO;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.pub.SystemException;
import nc.bs.pub.formulaparse.FormulaParse;
import nc.bs.scm.pub.smart.RichDMO;
import nc.bs.scm.pub.smart.SmartDMO;
import nc.vo.dm.dm004.ConstForBasePrice;
import nc.vo.dm.dm004.DmBasepriceAggVO;
import nc.vo.dm.dm004.DmBasepriceVO;
import nc.vo.dm.dm004.DmFeeitempriceAggVO;
import nc.vo.dm.dm004.DmFeeitempriceVO;
import nc.vo.dm.dm004.ValueRangeHashtableBaseprice;
import nc.vo.dm.dm016.ConstForLevelHead;
import nc.vo.dm.dm016.PriceType;
import nc.vo.dm.dm016.VRHQuantityLevelBody;
import nc.vo.dm.pub.DMBillTypeConst;
import nc.vo.dm.pub.DMDataVO;
import nc.vo.dm.pub.DMMath;
import nc.vo.dm.pub.DMVO;
import nc.vo.dm.pub.tools.StringTools;
import nc.vo.dm.pub.tools.VOTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.datatype.DataTypeConst;
import nc.vo.scm.datatype.DataTypeTool;
import nc.vo.scm.pub.session.ClientLink;

public class BasepriceDMO extends DmDMO {
	public BasepriceDMO() throws NamingException, SystemException {
		super();
		// TODO Auto-generated constructor stub
	}

	private boolean _DEBUG = false;

	protected void findSameKeysPutPrice(DMDataVO dataVO, DMDataVO dataVOOrder, DMDataVO[] dataVOCon, int iStart, String pricetypename) {
		String sKey = getVOKeys(dataVO, dataVOOrder);
		for (int i = iStart + 1; i < dataVOCon.length; ++i) {
			String sKCon = getVOKeys(dataVOCon[i], dataVOOrder);
			if ((!(sKCon.equals(sKey))) || (dataVOCon[i].getAttributeValue(pricetypename) != null)) continue;
			dataVOCon[i].setAttributeValue(pricetypename, dataVO.getAttributeValue(pricetypename));
		}
	}

	protected ArrayList firstGetPrice(String pkDelivOrg, String PKsendtype, String PKtrancust, String PKvhcltype, String[] PKinv, String[] PKcontainer) throws SQLException, javax.transaction.SystemException, BusinessException {
		ArrayList alResult = new ArrayList();
		StringBuffer sb = new StringBuffer();

		sb.append(" select bd_invmandoc.pk_invmandoc,resultnow.pk_transcontainer,resultnow.dbaseprice from (");

		sb.append(" select dm_baseprice.pk_inventory,dm_baseprice.pk_transcontainer,dm_baseprice.dbaseprice from dm_baseprice where dr=0 and dm_baseprice.pkdelivorg='" + pkDelivOrg + "' and dm_baseprice.pk_sendtype='" + PKsendtype);

		if (PKtrancust.trim().length() != 0) sb.append("' and dm_baseprice.pk_transcust='" + PKtrancust);
		else sb.append("' and dm_baseprice.pk_transcust is null ");
		if (PKvhcltype.trim().length() != 0) sb.append("' and dm_baseprice.pk_vehicletype='" + PKvhcltype);
		else sb.append("' and dm_baseprice.pk_vehicletype is null ");
		sb.append("' and dm_baseprice.pk_inventory in ( ");
		sb.append(" select pk_invbasdoc from bd_invmandoc ");
		for (int i = 0; i < PKinv.length; ++i) {
			if (i == 0) sb.append(" where pk_invmandoc in ('" + PKinv[i] + "'");
			else sb.append(",'" + PKinv[i] + "'");
			if (i == PKinv.length - 1) sb.append(" ) ");
		}
		sb.append(" ) ");
		sb.append(" ) resultnow inner join bd_invmandoc on bd_invmandoc.pk_invbasdoc=resultnow.pk_inventory");

		alResult = queryExecute(sb);

		return alResult;
	}

	protected DMDataVO[] getBasicPrice(String pkDelivOrg, String pksendtype, String[] pksrccalbodyar, String[] pkarrivearea) throws SQLException, javax.transaction.SystemException, BusinessException {
		UFDouble ufdBasePrice = new UFDouble();
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT dbaseprice,pkfromarea,pktoarea FROM dm_baseprice where  dr=0 ").append(" and (pkpacksort is null or len(rtrim(pkpacksort))=0)").append(" and (pk_transcontainer is null or len(rtrim(pk_transcontainer))=0)").append(" and pkdelivorg = '").append(pkDelivOrg).append("' and pk_sendtype = '").append(pksendtype).append("'");
		int i;
		if ((pksrccalbodyar != null) && (pksrccalbodyar.length != 0) && (pksrccalbodyar[0] != null)) {
			sb.append(" and pkfromarea in (");
			for (i = 0; i < pksrccalbodyar.length; ++i) {
				if (i == 0) sb.append("'" + pksrccalbodyar[i] + "'");
				else sb.append(",'" + pksrccalbodyar[i] + "'");
			}
			sb.append(")");
		}

		if ((pkarrivearea != null) && (pkarrivearea.length != 0) && (pkarrivearea[0] != null)) {
			sb.append(" and pktoarea in (");
			for (i = 0; i < pkarrivearea.length; ++i) {
				if (i == 0) sb.append("'" + pkarrivearea[i] + "'");
				else sb.append(",'" + pkarrivearea[i] + "'");
			}
			sb.append(" )");
		}

		DMDataVO[] dmdvos = query(sb);

		return dmdvos;
	}

	protected DMDataVO[] getBasicPrice(String pkDelivOrg, String pksendtype, String pksrccalbodyar, String pkarrivearea, String[] keys, String keyname) throws SQLException, javax.transaction.SystemException, BusinessException {
		if ((keys == null) || (keys.length == 0)) return new DMDataVO[0];
		UFDouble ufdBasePrice = new UFDouble();
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT dbaseprice," + keyname + " as pkpacksort FROM dm_baseprice where dr=0 and pkdelivorg = '").append(pkDelivOrg).append("' and pk_sendtype = '").append(pksendtype).append("' and pkfromarea = '").append(pksrccalbodyar).append("' and pktoarea = '").append(pkarrivearea).append("' and ").append(keyname).append(" in (");

		for (int i = 0; i < keys.length; ++i) {
			if (i == 0) sb.append("'" + keys[i] + "'");
			else sb.append(",'" + keys[i] + "'");
		}
		sb.append(" )");

		DMDataVO[] dmdvos = query(sb);

		return dmdvos;
	}

	protected String getInvclassKeysByOrder(DMDataVO orderVO) {
		String keyswithcoma = "";

		String[] sKeys = getKeysByOrderWithInvclass(orderVO);
		for (int i = 0; i < sKeys.length; ++i) {
			keyswithcoma = keyswithcoma + sKeys[i] + " DESC ";
			if (i != sKeys.length - 1) {
				keyswithcoma = keyswithcoma + ",";
			}
		}
		return keyswithcoma;
	}

	public UFDouble getJZX001(DMVO vo, UFDouble[] ufdForRow, Boolean bIsnotNeedHandSplit) throws Exception {
		UFDouble dtotal = new UFDouble(0);

		String pkdelivorg = (String) vo.getParentVO().getAttributeValue("pkdelivorg");

		String pkdelivmode = (String) vo.getParentVO().getAttributeValue("pkdelivmode");

		String pktrancust = (String) vo.getParentVO().getAttributeValue("pktrancust");

		DMDataVO[] packvos = (DMDataVO[]) (DMDataVO[]) vo.getParentVO().getAttributeValue("packagebill");

		Vector vForCalculate = new Vector();
		int j;
		for (int i = 0; i < packvos.length; ++i) {
			String[] keys = packvos[i].getAttributeNames();
			String pkfromarea = (String) packvos[i].getAttributeValue("pksendarea");

			String pktoarea = (String) packvos[i].getAttributeValue("pkarrivearea");

			for (j = 0; j < keys.length; ++j) {
				if ((keys[j].equals("vsendarea")) || (keys[j].equals("pksrccalbodyar")) || (keys[j].equals("pksendarea")) || (keys[j].equals("varrivearea")) || (keys[j].equals("pk_delivpacknum")) || (keys[j].equals("pk_delivbill_h"))) continue;
				if (keys[j].equals("pkarrivearea")) {
					continue;
				}

				DMDataVO dmdvoRow = new DMDataVO();
				dmdvoRow.setAttributeValue("pkdelivorg", pkdelivorg);
				dmdvoRow.setAttributeValue("pkdelivmode", pkdelivmode);
				dmdvoRow.setAttributeValue("pktrancust", pktrancust);
				dmdvoRow.setAttributeValue("pkfromarea", pkfromarea);
				dmdvoRow.setAttributeValue("pktoarea", pktoarea);
				dmdvoRow.setAttributeValue("pk_transcontainer", keys[j]);
				dmdvoRow.setAttributeValue("number", packvos[i].getAttributeValue(keys[j]));
				try {
					if (packvos[i].getAttributeValue(keys[j]) != null) {
						UFDouble ufd = new UFDouble(packvos[i].getAttributeValue(keys[j]).toString());

						vForCalculate.add(dmdvoRow);
					}
				} catch (Exception e) {
				}
			}
		}

		DMDataVO[] dmdvosForCalculate = new DMDataVO[vForCalculate.size()];
		vForCalculate.copyInto(dmdvosForCalculate);

		DMDataVO dmdvoOrder = new DMDataVO();
		dmdvoOrder.setAttributeValue("pkdelivorg", new Integer(0));
		dmdvoOrder.setAttributeValue("pkdelivmode", new Integer(0));
		dmdvoOrder.setAttributeValue("pkfromarea", new Integer(0));
		dmdvoOrder.setAttributeValue("pktoarea", new Integer(0));
		dmdvoOrder.setAttributeValue("pk_transcontainer", new Integer(0));
		dmdvoOrder.setAttributeValue("pktrancust", new Integer(1));

		queryPrice(dmdvosForCalculate, dmdvoOrder, "dbaseprice");

		if ((null != dmdvosForCalculate) && (dmdvosForCalculate.length != 0)) {
			UFDouble dbaseprice = new UFDouble(0);
			UFDouble dnum = new UFDouble(0);
			for (j = 0; j < dmdvosForCalculate.length; ++j) {
				if ((null != dmdvosForCalculate[j].getAttributeValue("dbaseprice")) && (dmdvosForCalculate[j].getAttributeValue("dbaseprice").toString().trim().length() != 0)) {
					dbaseprice = new UFDouble(dmdvosForCalculate[j].getAttributeValue("dbaseprice").toString());
				} else dbaseprice = new UFDouble(0);
				if (null != dmdvosForCalculate[j].getAttributeValue("number")) {
					dnum = new UFDouble(dmdvosForCalculate[j].getAttributeValue("number").toString());
				} else dnum = new UFDouble(0);
				dtotal = dtotal.add(dbaseprice.multiply(dnum));
			}
		}
		return dtotal;
	}

	protected String[] getKeysByOrderWithInvclass(DMDataVO orderVO) {
		String[] sKeyNames = orderVO.getAttributeNames();
		String[] select = new String[sKeyNames.length];
		DMVO dmvo = new DMVO(sKeyNames.length);
		for (int i = 0; i < sKeyNames.length; ++i) {
			dmvo.getBodyVOs()[i].setAttributeValue("orderkey", sKeyNames[i]);
			dmvo.getBodyVOs()[i].setAttributeValue("ordernum", orderVO.getAttributeValue(sKeyNames[i]));
		}

		int[] iAsc = new int[1];
		iAsc[0] = 1;
		dmvo.sortByKeys(new String[] {
			"ordernum"
		}, iAsc);

		for (int i = 0; i < dmvo.getBodyVOs().length; ++i) {
			String sTr = translateTransBillKeyToPriceKey(dmvo.getBodyVOs()[i].getAttributeValue("orderkey").toString());

			if ((sTr != null) && (sTr.trim().length() != 0) && (sTr.equals("pk_inventory"))) {
				sTr = "codefl  ";
			}
			select[i] = sTr;
		}

		return select;
	}

	protected String[] getKeysByOrderWithoutInvclass(DMDataVO orderVO) {
		String[] sKeyNames = orderVO.getAttributeNames();
		String[] select = new String[sKeyNames.length];
		DMVO dmvo = new DMVO(sKeyNames.length);
		for (int i = 0; i < sKeyNames.length; ++i) {
			dmvo.getBodyVOs()[i].setAttributeValue("orderkey", sKeyNames[i]);
			dmvo.getBodyVOs()[i].setAttributeValue("ordernum", orderVO.getAttributeValue(sKeyNames[i]));
		}

		int[] iAsc = new int[1];
		iAsc[0] = 1;
		dmvo.sortByKeys(new String[] {
			"ordernum"
		}, iAsc);

		for (int i = 0; i < dmvo.getBodyVOs().length; ++i) {
			String sTr = translateTransBillKeyToPriceKey(dmvo.getBodyVOs()[i].getAttributeValue("orderkey").toString());

			select[i] = sTr;
		}

		return select;
	}

	public UFDouble getLD001(DMVO voDelivBill, UFDouble[] ufdForRow, Boolean bIsnotNeedHandSplit) throws Exception {
		String sPkdelivorg = (String) voDelivBill.getParentVO().getAttributeValue("pkdelivorg");

		String sPkdelivmode = (String) voDelivBill.getParentVO().getAttributeValue("pkdelivmode");

		String sPktrancust = (String) voDelivBill.getParentVO().getAttributeValue("pktrancust");

		String sPkvehicletype = (String) voDelivBill.getParentVO().getAttributeValue("pkvehicletype");

		DMDataVO[] voaPack = (DMDataVO[]) (DMDataVO[]) voDelivBill.getParentVO().getAttributeValue("packagebill");

		UFDouble dTotal = DataTypeConst.UFDOUBLE_0;

		DMDataVO[] voaForCalculate = null;
		if (this._DEBUG) {
			voaForCalculate = test_packvoToSave(voDelivBill, PriceType.iWeight + "", true);
		} else {
			Vector vForCalculate = new Vector();
			for (int i = 0; i < voaPack.length; ++i) {
				String[] keys = voaPack[i].getAttributeNames();
				String pkfromarea = (String) voaPack[i].getAttributeValue("pksendarea");

				String pktoarea = (String) voaPack[i].getAttributeValue("pkarrivearea");

				String pkfromaddress = (String) voaPack[i].getAttributeValue("pksendaddress");

				String pktoaddress = (String) voaPack[i].getAttributeValue("pkarriveaddress");

				for (int j = 0; j < keys.length; ++j) {
					if ((keys[j].equals("vsendarea")) || (keys[j].equals("pksrccalbodyar")) || (keys[j].equals("pksendarea")) || (keys[j].equals("varrivearea")) || (keys[j].equals("pk_delivpacknum")) || (keys[j].equals("pk_delivbill_h")) || (keys[j].equals("pkarrivearea")) || (keys[j].equals("deststockorgname")) || (keys[j].equals("pkdeststockorg")) || (keys[j].equals("crownumber")) || (keys[j].equals("vsendarea")) || (keys[j].equals("pkcustbasdoc")) || (keys[j].equals("deststockorgcode")) || (keys[j].equals("varrivearea")) || (keys[j].equals("custname")) || (keys[j].equals("custcode")) || (keys[j].equals("pksendaddress")) || (keys[j].equals("pkarriveaddress"))) continue;
					if (keys[j].indexOf("_") > 0) {
						continue;
					}

					DMDataVO dmdvoRow = new DMDataVO();
					dmdvoRow.setAttributeValue("pkdelivorg", sPkdelivorg);
					dmdvoRow.setAttributeValue("pkdelivmode", sPkdelivmode);
					dmdvoRow.setAttributeValue("pktrancust", sPktrancust);
					dmdvoRow.setAttributeValue("pkfromarea", pkfromarea);
					dmdvoRow.setAttributeValue("pktoarea", pktoarea);
					dmdvoRow.setAttributeValue("pkfromaddress", pkfromaddress);
					dmdvoRow.setAttributeValue("pktoaddress", pktoaddress);
					dmdvoRow.setAttributeValue("pkpacksort", keys[j]);
					dmdvoRow.setAttributeValue("dpacknum", voaPack[i].getAttributeValue(keys[j]));

					dmdvoRow.setAttributeValue("pkvehicletype", sPkvehicletype);
					try {
						if (voaPack[i].getAttributeValue(keys[j]) != null) {
							UFDouble ufd = new UFDouble(voaPack[i].getAttributeValue(keys[j]).toString());

							vForCalculate.add(dmdvoRow);
						}
					} catch (Exception e) {
					}
				}
			}

			voaForCalculate = new DMDataVO[vForCalculate.size()];
			vForCalculate.copyInto(voaForCalculate);
		}

		DMDataVO dmdvoOrder = new DMDataVO();
		dmdvoOrder.setAttributeValue("pkdelivorg", DataTypeConst.INTEGER_0);
		dmdvoOrder.setAttributeValue("pkdelivmode", DataTypeConst.INTEGER_0);
		dmdvoOrder.setAttributeValue("pktrancust", DataTypeConst.INTEGER_0);
		dmdvoOrder.setAttributeValue("pkpacksort", DataTypeConst.INTEGER_0);

		dmdvoOrder.setAttributeValue("pkvehicletype", DataTypeConst.INTEGER_1);
		dmdvoOrder.setAttributeValue("pkfromaddress", DataTypeConst.INTEGER_1);
		dmdvoOrder.setAttributeValue("pktoaddress", DataTypeConst.INTEGER_2);

		DMDataVO[] voaBasePrice = queryPriceVO(voaForCalculate, dmdvoOrder, "dbaseprice");

		if (voaBasePrice == null) { return dTotal; }

		UFDouble dBasePrice = null;
		UFDouble dPackNum = null;
		int iLen = voaBasePrice.length;
		for (int i = 0; i < iLen; ++i) {
			if (voaBasePrice[i] == null) {
				continue;
			}
			dBasePrice = DataTypeTool.getUFDouble_NullAs0(voaBasePrice[i].getAttributeValue("dbaseprice"));

			dPackNum = DataTypeTool.getUFDouble_NullAs0(voaForCalculate[i].getAttributeValue("dpacknum"));

			dTotal = dTotal.add(dBasePrice.multiply(dPackNum));
		}
		return dTotal;
	}

	protected String getNormalKeysByOrder(DMDataVO orderVO) {
		String[] sKeys = getKeysByOrderWithoutInvclass(orderVO);

		String keyswithcoma = "";

		for (int i = 0; i < sKeys.length; ++i) {
			String sKey = null;
			if (sKeys[i].equals("ipricetype")) sKey = "ipricetype";
			else {
				sKey = "coalesce(" + sKeys[i] + ",' ') as " + sKeys[i];
			}

			keyswithcoma = keyswithcoma + sKey;
			if (i != sKeys.length - 1) {
				keyswithcoma = keyswithcoma + ",";
			}
		}

		return keyswithcoma;
	}

	protected String getNormalOrderKeysByOrder(DMDataVO orderVO) {
		String keyswithcoma = "";

		String[] sKeys = getKeysByOrderWithoutInvclass(orderVO);
		for (int i = 0; i < sKeys.length; ++i) {
			if (sKeys[i].indexOf("ipricetype") >= 0) keyswithcoma = keyswithcoma + sKeys[i] + " DESC ";
			else {
				keyswithcoma = keyswithcoma + "ISNULL(" + sKeys[i] + ",' ') DESC ";
			}

			if (i != sKeys.length - 1) {
				keyswithcoma = keyswithcoma + ",";
			}
		}

		return keyswithcoma;
	}

	protected UFDouble getPrice(DMDataVO dataVOCon, DMDataVO order, String pricetypename) throws Exception {
		String sSelect = null;
		String sWhere = null;
		String sFrom = null;
		String sOrderBy = null;
		StringBuffer sql = new StringBuffer();

		Integer iForInv = new Integer(0);

		if (isExtInvInOrder(order)) {
			iForInv = (Integer) order.getAttributeValue("pkinv");
			order.setAttributeValue("pkinv", new Integer(0));
		}

		sSelect = getNormalKeysByOrder(order) + "," + pricetypename;
		sWhere = getNormalWhereByOrder(order, dataVOCon, pricetypename);
		if (isExtInvInOrder(order)) {
			sFrom = " dm_baseprice inner join bd_invbasdoc  on dm_baseprice.pk_inventory=bd_invbasdoc.pk_invbasdoc  inner join bd_invmandoc on bd_invmandoc.pk_invbasdoc = bd_invbasdoc.pk_invbasdoc ";
		} else {
			sFrom = " dm_baseprice ";
		}
		sOrderBy = getNormalOrderKeysByOrder(order);

		sql.append("select " + sSelect + " from " + sFrom + " where " + sWhere + " order by " + sOrderBy);

		DMDataVO[] dmdataVOrt = query(sql);

		if ((null != dmdataVOrt) && (dmdataVOrt.length > 0)) {
			if ((dmdataVOrt[0].getAttributeValue(pricetypename) == null) || (dmdataVOrt[0].getAttributeValue(pricetypename).toString().trim().length() == 0)) { return new UFDouble(0); }
			return new UFDouble(dmdataVOrt[0].getAttributeValue(pricetypename).toString());
		}

		if (isExtInvInOrder(order)) {
			sSelect = getSelInvclassKeysByOrder(order) + ", dm_baseprice." + pricetypename;

			sWhere = getInvclassWhereByOrder(order, dataVOCon, pricetypename);
			sFrom = " dm_baseprice inner join bd_invcl invcl on dm_baseprice.pk_invclass=invcl.pk_invcl ";

			sOrderBy = getInvclassKeysByOrder(order);

			sql = new StringBuffer();
			sql.append("select " + sSelect + " from " + sFrom + " where " + sWhere + " order by " + sOrderBy);

			dmdataVOrt = query(sql);

			if ((null != dmdataVOrt) && (dmdataVOrt.length > 0)) {
				if ((dmdataVOrt[0].getAttributeValue(pricetypename) == null) || (dmdataVOrt[0].getAttributeValue(pricetypename).toString().trim().length() == 0)) { return new UFDouble(0); }
				return new UFDouble(dmdataVOrt[0].getAttributeValue(pricetypename).toString());
			}

			if (iForInv.intValue() != 0) {
				order.setAttributeValue("pkinv", iForInv);
				sSelect = getNormalKeysByOrder(order) + ", " + pricetypename;
				sWhere = getNormalWhereByOrder(order, dataVOCon, pricetypename);
				sFrom = " dm_baseprice inner join bd_invbasdoc  on dm_baseprice.pk_inventory=bd_invbasdoc.pk_invbasdoc  inner join bd_invmandoc on bd_invmandoc.pk_invbasdoc = bd_invbasdoc.pk_invbasdoc ";

				sOrderBy = getNormalOrderKeysByOrder(order);

				sql = new StringBuffer();
				sql.append(" select " + sSelect + " from " + sFrom + " where " + sWhere + " order by " + sOrderBy);

				dmdataVOrt = query(sql);

				if ((null != dmdataVOrt) && (dmdataVOrt.length > 0)) {
					if ((dmdataVOrt[0].getAttributeValue(pricetypename) == null) || (dmdataVOrt[0].getAttributeValue(pricetypename).toString().trim().length() == 0)) { return new UFDouble(0); }
					return new UFDouble(dmdataVOrt[0].getAttributeValue(pricetypename).toString());
				}

			}

		}

		return new UFDouble(0);
	}

	public UFDouble getPRICE001(DMVO voDelivBill, UFDouble[] ufdForRow, Boolean bIsnotNeedHandSplit) throws Exception {
		String sMethodName = "nc.bs.dm.dm004.BasepriceDMO.getPrice001(DMVO, UFDouble [], Boolean)";

		if (voDelivBill == null) {
			String[] sValue = {
				sMethodName
			};
			throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40140216", "UPP40140216-000097", null, sValue));
		}

		String sPkdelivorg = (String) voDelivBill.getParentVO().getAttributeValue("pkdelivorg");

		String sPkdelivmode = (String) voDelivBill.getParentVO().getAttributeValue("pkdelivmode");

		String sPktrancust = (String) voDelivBill.getParentVO().getAttributeValue("pktrancust");

		DMDataVO voFeeItem = (DMDataVO) voDelivBill.getParentVO().getAttributeValue("feevo");

		if (voFeeItem == null) {
			String[] sValue = {
				sMethodName
			};
			throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40140216", "UPP40140216-000098", null, sValue));
		}

		String sFeeItemId = (String) voFeeItem.getAttributeValue("pk_feeitem");

		DMDataVO[] voaFeeItemPrice = new FeeItemPriceDMO().queryFeeItemPriceVOs(" FROM dm_feeitemprice WHERE pk_sendtype='" + sPkdelivmode + "'" + " AND pkdelivorg='" + sPkdelivorg + "'" + " AND pk_transcust='" + sPktrancust + "'" + " AND pk_feeitem='" + sFeeItemId + "'" + " AND DR=0");

		if ((voaFeeItemPrice == null) || (voaFeeItemPrice.length == 0)) { return DataTypeConst.UFDOUBLE_0; }
		return DataTypeTool.getUFDouble_NullAs0(voaFeeItemPrice[0].getAttributeValue("dfeeitemprice"));
	}

	public UFDouble getPZZC004(DMVO vo, UFDouble[] ufdForRow, Boolean bIsnotNeedHandSplit) throws SQLException, javax.transaction.SystemException, BusinessException {
		UFDouble dtotal = new UFDouble(0);
		String pkvehicle = (String) vo.getParentVO().getAttributeValue("pkvehicle");

		DMDataVO[] bodyvos = (DMDataVO[]) (DMDataVO[]) vo.getChildrenVO();

		Object oinvweight = null;
		UFDouble dtotalweight = new UFDouble();
		for (int i = 0; i < bodyvos.length; ++i) {
			oinvweight = bodyvos[i].getAttributeValue("dinvweight");
			dtotalweight = dtotalweight.add(new UFDouble(oinvweight.toString()));
		}

		StringBuffer sb = new StringBuffer();
		sb.append("SELECT doilconsume FROM dm_vehicle where dr=0 and pk_vehicle ='").append((pkvehicle == null) ? " " : pkvehicle).append("'");

		DMDataVO[] dmdvos = query(sb);
		if ((null != dmdvos) && (dmdvos.length != 0)) {
			UFDouble doilconsume = new UFDouble((dmdvos[0].getAttributeValue("doilconsume") == null) ? "0" : dmdvos[0].getAttributeValue("doilconsume").toString());

			doilconsume = doilconsume.div(100.0D);

			dtotal = doilconsume;

			for (int i = 0; i < bodyvos.length; ++i) {
				ufdForRow[i] = dtotal.div(bodyvos.length);
			}
		}
		return dtotal;
	}

	public UFDouble getQY001(DMVO vo, UFDouble[] ufdForRow, Boolean bIsnotNeedHandSplit) throws SQLException, javax.transaction.SystemException, BusinessException {
		UFDouble dMax = new UFDouble(0);
		String pkdelivroute = (String) vo.getParentVO().getAttributeValue("pkdelivroute");

		StringBuffer sb = new StringBuffer();
		sb.append("SELECT dmileage FROM bd_routeaddr where dr=0 and pkroute =  '").append((pkdelivroute == null) ? " " : pkdelivroute).append("'");

		DMDataVO[] dmdvos = query(sb);
		if ((null != dmdvos) && (dmdvos.length != 0)) {
			UFDouble dmileage = new UFDouble();
			for (int i = 0; i < dmdvos.length; ++i) {
				Object objNum = dmdvos[i].getAttributeValue("dmileage");
				dmileage = new UFDouble(((objNum == null) || (objNum.toString().trim().length() == 0)) ? "0" : objNum.toString());

				if (dMax.doubleValue() < dmileage.doubleValue()) dMax = dmileage;
			}
			for (int i = 0; i < ufdForRow.length; ++i) {
				ufdForRow[i] = dMax.div(ufdForRow.length);
			}
		}
		return dMax;
	}

	protected String getSelInvclassKeysByOrder(DMDataVO orderVO) {
		String[] sKeyNames = orderVO.getAttributeNames();
		String[] sKeys = new String[sKeyNames.length];
		DMVO dmvo = new DMVO(sKeyNames.length);
		for (int i = 0; i < sKeyNames.length; ++i) {
			dmvo.getBodyVOs()[i].setAttributeValue("orderkey", sKeyNames[i]);
			dmvo.getBodyVOs()[i].setAttributeValue("ordernum", orderVO.getAttributeValue(sKeyNames[i]));
		}

		int[] iAsc = new int[1];
		iAsc[0] = 1;
		dmvo.sortByKeys(new String[] {
			"ordernum"
		}, iAsc);

		for (int i = 0; i < dmvo.getBodyVOs().length; ++i) {
			String sTr = translateTransBillKeyToPriceKey(dmvo.getBodyVOs()[i].getAttributeValue("orderkey").toString());

			if ((sTr != null) && (sTr.trim().length() != 0) && (sTr.equals("pk_inventory"))) {
				sTr = "len(invcl.invclasscode) as codefl";
			}
			sKeys[i] = sTr;
		}

		String keyswithcoma = "";

		for (int i = 0; i < sKeys.length; ++i) {
			if (sKeys[i].equals("len(invcl.invclasscode) as codefl")) keyswithcoma = keyswithcoma + sKeys[i];
			else {
				keyswithcoma = keyswithcoma + "coalesce(" + sKeys[i] + ",' ') as " + sKeys[i];
			}

			if (i != sKeys.length - 1) {
				keyswithcoma = keyswithcoma + ",";
			}
		}
		return keyswithcoma;
	}

	public UFDouble getTY001(DMVO voDelivBill, UFDouble[] ufdForRow, Boolean bIsnotNeedHandSplit) throws Exception {
		UFDouble dTotal = DataTypeConst.UFDOUBLE_0;

		String sPkdelivorg = (String) voDelivBill.getParentVO().getAttributeValue("pkdelivorg");

		String sPkdelivmode = (String) voDelivBill.getParentVO().getAttributeValue("pkdelivmode");

		String sPktrancust = (String) voDelivBill.getParentVO().getAttributeValue("pktrancust");

		String sPkvehicletype = (String) voDelivBill.getParentVO().getAttributeValue("pkvehicletype");

		DMVO dmVoClone = (DMVO) voDelivBill.clone();
		VOTools.filterZeroValueRow(dmVoClone, new String[] {
			"dinvweight"
		});
		DMDataVO[] voaDelivBody = dmVoClone.getBodyVOs();
		if ((voaDelivBody == null) || (voaDelivBody.length == 0)) { return dTotal; }

		for (int i = 0; i < voaDelivBody.length; ++i) {
			voaDelivBody[i].setAttributeValue("pkdelivorg", sPkdelivorg);
			voaDelivBody[i].setAttributeValue("pkdelivmode", sPkdelivmode);
			voaDelivBody[i].setAttributeValue("pktrancust", sPktrancust);
			voaDelivBody[i].setAttributeValue("pkfromaddress", (String) voaDelivBody[i].getAttributeValue("pksendaddress"));

			voaDelivBody[i].setAttributeValue("pktoaddress", (String) voaDelivBody[i].getAttributeValue("pkarriveaddress"));

			voaDelivBody[i].setAttributeValue("pkvehicletype", sPkvehicletype);
		}

		DMDataVO dmdvoOrder = new DMDataVO();
		dmdvoOrder.setAttributeValue("pkdelivorg", DataTypeConst.INTEGER_0);
		dmdvoOrder.setAttributeValue("pkdelivmode", DataTypeConst.INTEGER_0);
		dmdvoOrder.setAttributeValue("pktrancust", DataTypeConst.INTEGER_0);

		dmdvoOrder.setAttributeValue("pkpacksort", DataTypeConst.INTEGER_0);

		dmdvoOrder.setAttributeValue("pkvehicletype", DataTypeConst.INTEGER_1);
		dmdvoOrder.setAttributeValue("pkfromaddress", DataTypeConst.INTEGER_1);
		dmdvoOrder.setAttributeValue("pktoaddress", DataTypeConst.INTEGER_2);

		DMDataVO[] voaBasePrice = queryPriceVO(voaDelivBody, dmdvoOrder, "dbaseprice");

		if (voaBasePrice == null) { return dTotal; }

		UFDouble dBasePrice = null;
		UFDouble dRowInvWeight = null;
		int iLen = voaDelivBody.length;
		for (int i = 0; i < iLen; ++i) {
			if (voaBasePrice[i] == null) {
				continue;
			}

			dRowInvWeight = DataTypeTool.getUFDouble_NullAs0(voaDelivBody[i].getAttributeValue("dinvweight"));

			dBasePrice = DataTypeTool.getUFDouble_NullAs0(voaBasePrice[i].getAttributeValue("dbaseprice"));

			dTotal = dTotal.add(dRowInvWeight.multiply(dBasePrice));
		}

		return dTotal;
	}

	public UFDouble getMaxMilePrice(DMVO voDelivBill, UFDouble[] ufdForRow, Boolean bIsnotNeedHandSplit) throws Exception {
		UFDouble dTotal = DataTypeConst.UFDOUBLE_0;

		String sPkdelivorg = (String) voDelivBill.getParentVO().getAttributeValue("pkdelivorg");

		String sPkdelivmode = (String) voDelivBill.getParentVO().getAttributeValue("pkdelivmode");

		String sPktrancust = (String) voDelivBill.getParentVO().getAttributeValue("pktrancust");

		String sPkvehicletype = (String) voDelivBill.getParentVO().getAttributeValue("pkvehicletype");

		DMVO dmVoClone = (DMVO) voDelivBill.clone();
		DMDataVO voaDeliv = new DMDataVO();

		voaDeliv.setAttributeValue("pkdelivorg", sPkdelivorg);
		voaDeliv.setAttributeValue("pkdelivmode", sPkdelivmode);
		voaDeliv.setAttributeValue("pktrancust", sPktrancust);
		voaDeliv.setAttributeValue("pkvehicletype", sPkvehicletype);
		DMDataVO[] voaDelivBody = new DMDataVO[1];
		voaDelivBody[0] = voaDeliv;

		DMDataVO dmdvoOrder = new DMDataVO();
		dmdvoOrder.setAttributeValue("pkdelivorg", DataTypeConst.INTEGER_0);
		dmdvoOrder.setAttributeValue("pkdelivmode", DataTypeConst.INTEGER_0);
		dmdvoOrder.setAttributeValue("pktrancust", DataTypeConst.INTEGER_0);
		dmdvoOrder.setAttributeValue("pkdelivroute", DataTypeConst.INTEGER_0);

		dmdvoOrder.setAttributeValue("pkvehicletype", DataTypeConst.INTEGER_0);

		DMDataVO[] voaBasePrice = queryPriceVO(voaDelivBody, dmdvoOrder, "dbaseprice");

		if (voaBasePrice == null) { return dTotal; }

		UFDouble dBasePrice = null;
		UFDouble dRowMaxMile = getQY001(dmVoClone, new UFDouble[0], new Boolean(false));

		int iLen = voaDelivBody.length;
		for (int i = 0; i < iLen; ++i) {
			if (voaBasePrice[i] == null) {
				continue;
			}

			dBasePrice = DataTypeTool.getUFDouble_NullAs0(voaBasePrice[i].getAttributeValue("dbaseprice"));

			dTotal = dTotal.add(dRowMaxMile.multiply(dBasePrice));
		}

		return dTotal;
	}

	public DMDataVO getMaxmileAndPriceForPrint(DMVO voDelivBill, UFDouble[] ufdForRow, Boolean bIsnotNeedHandSplit) throws Exception {
		UFDouble dTotal = DataTypeConst.UFDOUBLE_0;

		String sPkdelivorg = (String) voDelivBill.getParentVO().getAttributeValue("pkdelivorg");

		String sPkdelivmode = (String) voDelivBill.getParentVO().getAttributeValue("pkdelivmode");

		String sPktrancust = (String) voDelivBill.getParentVO().getAttributeValue("pktrancust");

		String sPkvehicletype = (String) voDelivBill.getParentVO().getAttributeValue("pkvehicletype");

		DMVO dmVoClone = (DMVO) voDelivBill.clone();
		DMDataVO voaDeliv = new DMDataVO();

		voaDeliv.setAttributeValue("pkdelivorg", sPkdelivorg);
		voaDeliv.setAttributeValue("pkdelivmode", sPkdelivmode);
		voaDeliv.setAttributeValue("pktrancust", sPktrancust);
		voaDeliv.setAttributeValue("pkvehicletype", sPkvehicletype);
		DMDataVO[] voaDelivBody = new DMDataVO[1];
		voaDelivBody[0] = voaDeliv;

		DMDataVO dmdvoOrder = new DMDataVO();
		dmdvoOrder.setAttributeValue("pkdelivorg", DataTypeConst.INTEGER_0);
		dmdvoOrder.setAttributeValue("pkdelivmode", DataTypeConst.INTEGER_0);
		dmdvoOrder.setAttributeValue("pktrancust", DataTypeConst.INTEGER_0);
		dmdvoOrder.setAttributeValue("pkdelivroute", DataTypeConst.INTEGER_0);
		dmdvoOrder.setAttributeValue("pkvehicletype", DataTypeConst.INTEGER_0);

		DMDataVO[] voaBasePrice = queryPriceVO(voaDelivBody, dmdvoOrder, "dbaseprice");

		UFDouble dBasePrice = DataTypeTool.getUFDouble_NullAs0((voaBasePrice[0] != null) ? voaBasePrice[0].getAttributeValue("dbaseprice") : null);

		UFDouble dRowMaxMile = getQY001(dmVoClone, new UFDouble[0], new Boolean(false));

		DMDataVO dmdRe = new DMDataVO();
		dmdRe.setAttributeValue("dmaxmileprice", dBasePrice);
		dmdRe.setAttributeValue("dmaxmile", dRowMaxMile);
		return dmdRe;
	}

	public UFDouble getTY002(DMVO vo, UFDouble[] ufdForRow, Boolean bIsnotNeedHandSplit) throws SQLException, javax.transaction.SystemException, BusinessException {
		UFDouble dtotal = new UFDouble(0);
		Object omoney = null;
		DMDataVO[] bodyvos = (DMDataVO[]) (DMDataVO[]) vo.getChildrenVO();
		for (int i = 0; i < bodyvos.length; ++i) {
			omoney = bodyvos[i].getAttributeValue("dmoney");
			ufdForRow[i] = ((omoney == null) ? new UFDouble(0) : (UFDouble) omoney);

			if (ufdForRow[i].doubleValue() < 0.0D) {
				ufdForRow[i] = ufdForRow[i].multiply(-1.0D);
			}
			dtotal = dtotal.add(ufdForRow[i]);
		}
		return dtotal;
	}

	public UFDouble getTY003(DMVO vo, UFDouble[] ufdForRow, Boolean bIsnotNeedHandSplit) throws SQLException, javax.transaction.SystemException, BusinessException {
		UFDouble dtotal = new UFDouble(0);
		Object oinvweight = null;
		DMDataVO[] bodyvos = (DMDataVO[]) (DMDataVO[]) vo.getChildrenVO();
		for (int i = 0; i < bodyvos.length; ++i) {
			oinvweight = bodyvos[i].getAttributeValue("dinvweight");
			ufdForRow[i] = ((oinvweight == null) ? new UFDouble(0) : (UFDouble) oinvweight);

			dtotal = dtotal.add(ufdForRow[i]);
		}
		return dtotal;
	}

	public UFDouble getTY005(DMVO vo, UFDouble[] ufdForRow, Boolean bIsnotNeedHandSplit) throws Exception {
		UFDouble dTotal = DataTypeConst.UFDOUBLE_0;

		String sPkdelivorg = (String) vo.getParentVO().getAttributeValue("pkdelivorg");

		String sPkdelivmode = (String) vo.getParentVO().getAttributeValue("pkdelivmode");

		String sPktrancust = (String) vo.getParentVO().getAttributeValue("pktrancust");

		String sPkvehicletype = (String) vo.getParentVO().getAttributeValue("pkvehicletype");

		DMVO dmvo = (DMVO) vo.clone();

		DMDataVO[] bodyvos = dmvo.getBodyVOs();
		if ((bodyvos == null) || (bodyvos.length == 0)) { return dTotal; }

		for (int i = 0; i < bodyvos.length; ++i) {
			bodyvos[i].setAttributeValue("pkdelivorg", sPkdelivorg);
			bodyvos[i].setAttributeValue("pkdelivmode", sPkdelivmode);
			bodyvos[i].setAttributeValue("pktrancust", sPktrancust);
			bodyvos[i].setAttributeValue("pkvehicletype", sPkvehicletype);
			bodyvos[i].setAttributeValue("pkfromaddress", (String) bodyvos[i].getAttributeValue("pksendaddress"));

			bodyvos[i].setAttributeValue("pktoaddress", (String) bodyvos[i].getAttributeValue("pkarriveaddress"));
		}

		DMDataVO dmdvoOrder = new DMDataVO();
		dmdvoOrder.setAttributeValue("pkdelivorg", DataTypeConst.INTEGER_0);
		dmdvoOrder.setAttributeValue("pkdelivmode", DataTypeConst.INTEGER_0);
		dmdvoOrder.setAttributeValue("pktrancust", DataTypeConst.INTEGER_0);

		dmdvoOrder.setAttributeValue("pkpacksort", DataTypeConst.INTEGER_0);

		dmdvoOrder.setAttributeValue("pkvehicletype", DataTypeConst.INTEGER_1);
		dmdvoOrder.setAttributeValue("pkfromaddress", DataTypeConst.INTEGER_1);
		dmdvoOrder.setAttributeValue("pktoaddress", DataTypeConst.INTEGER_2);

		DMDataVO[] voaBasePrice = queryPriceVO(bodyvos, dmdvoOrder, "dbaseprice");

		if (voaBasePrice == null) { return dTotal; }

		int iLen = ufdForRow.length;

		UFDouble dInvNum = null;
		UFDouble dBasePrice = null;
		UFDouble[] daInvNum = new UFDouble[iLen];

		for (int i = 0; i < iLen; ++i) {
			if (voaBasePrice[i] == null) {
				continue;
			}

			dInvNum = DataTypeTool.getUFDouble_NullAs0(bodyvos[i].getAttributeValue("dinvnum"));

			dBasePrice = DataTypeTool.getUFDouble_NullAs0(voaBasePrice[i].getAttributeValue("dbaseprice"));

			ufdForRow[i] = dInvNum.multiply(dBasePrice);
			dTotal = dTotal.add(ufdForRow[i]);
		}

		return dTotal;
	}

	protected String getVOKeys(DMDataVO DataVO, DMDataVO dataVOOrder) {
		if ((null == dataVOOrder) || (dataVOOrder.getAttributeNames() == null) || (DataVO == null)) { return ""; }
		String sSep = "!@^%#";
		StringBuffer sbK = new StringBuffer();

		String[] sKeys = dataVOOrder.getAttributeNames();
		for (int j = 0; j < sKeys.length; ++j) {
			if (null == DataVO.getAttributeValue(sKeys[j])) sbK.append(sSep);
			else {
				sbK.append(sSep).append(DataVO.getAttributeValue(sKeys[j]).toString());
			}

		}

		return sbK.toString();
	}

	protected boolean isExtInvInOrder(DMDataVO dataVOPri) {
		return (null != dataVOPri.getAttributeValue("pkinv"));
	}

	protected UFDouble nextGetPrice(String pkDelivOrg, String pksendtype, String pktrancust, String sPKVhcltype, String sPKInv, String sPKContainer) throws SQLException, javax.transaction.SystemException, BusinessException {
		ArrayList alResult = new ArrayList();
		StringBuffer sb = new StringBuffer();

		sb.append(" select dbaseprice from (");
		sb.append(" select * from (");
		sb.append(" select firstcheck.pk_invclass,firstcheck.pk_transcontainer,firstcheck.dbaseprice,bd_invcl.invclasscode from (");

		sb.append(" select pk_invclass,pk_transcontainer,dbaseprice from dm_baseprice where dr=0 and pkdelivorg='" + pkDelivOrg + "' and pk_sendtype='" + pksendtype);

		if (pktrancust.trim().length() != 0) sb.append("' and pk_transcust='" + pktrancust);
		else sb.append("' and pk_transcust is null ");
		if (sPKVhcltype.trim().length() != 0) sb.append("' and pk_vehicletype='" + sPKVhcltype);
		else sb.append("' and pk_vehicletype is null ");
		if (sPKContainer.trim().length() != 0) sb.append("' and pk_transcontainer='" + sPKContainer);
		else sb.append("' and pk_transcontainer is null ");
		sb.append(" ) firstcheck left join bd_invcl on bd_invcl.pk_invcl=firstcheck.pk_invclass ");

		sb.append(" ) seccheck ");
		sb.append(" where ");

		sb.append(" '" + sPKInv + "' in ( ");
		sb.append(" SELECT bd_invmandoc.pk_invmandoc as cinventoryid ").append(" FROM bd_invcl bd_invcl1 INNER JOIN bd_invbasdoc ON bd_invcl1.pk_invcl = bd_invbasdoc.pk_invcl ").append(" INNER JOIN bd_invmandoc ").append(" ON bd_invbasdoc.pk_invbasdoc = bd_invmandoc.pk_invbasdoc ").append(" where bd_invcl1.invclasscode like seccheck.pk_invclass||'%' ").append(" ) ");

		sb.append(" ) thirdcheck order by len(rtrim(pk_invclass)) desc ");

		alResult = queryExecute(sb);

		if (alResult.size() > 0) {
			Object obj = ((DMDataVO) alResult.get(0)).getAttributeValue("dbaseprice");

			if ((null != obj) && (obj.toString().trim().length() != 0)) { return new UFDouble(obj.toString()); }
		}

		return null;
	}

	protected void queryPrice(DMDataVO[] dataVOCon, DMDataVO dataVOOrder, String pricetypename) throws Exception {
		if ((null == dataVOOrder) || (null == dataVOOrder.getAttributeNames()) || (null == dataVOCon) || (dataVOCon.length == 0)) { return; }
		DMDataVO[] dataVOrt = new DMDataVO[dataVOCon.length];
		Hashtable ht = new Hashtable();

		for (int i = 0; i < dataVOCon.length; ++i) {
			dataVOCon[i].setAttributeValue(pricetypename, null);
		}

		for (int i = 0; i < dataVOCon.length; ++i) {
			if (null != dataVOCon[i].getAttributeValue(pricetypename)) {
				continue;
			}

			String sKey = getNormalWhereByOrder(dataVOOrder, dataVOCon[i], pricetypename) + getInvclassWhereByOrder(dataVOOrder, dataVOCon[i], pricetypename);

			UFDouble dPrice = (UFDouble) ht.get(sKey);
			if (dPrice == null) {
				dPrice = getPrice(dataVOCon[i], dataVOOrder, pricetypename);
				ht.put(sKey, dPrice);
			}

			dataVOCon[i].setAttributeValue(pricetypename, dPrice);

			findSameKeysPutPrice(dataVOCon[i], dataVOOrder, dataVOCon, i, pricetypename);
		}
	}

	protected String translateTransBillKeyToPriceKey(String sTransBillKey) {
		String sTableName = "dm_baseprice.";

		DMDataVO dmdvo = new DMDataVO();

		dmdvo.setAttributeValue("pkdelivorg", sTableName + "pkdelivorg");
		dmdvo.setAttributeValue("pkdelivmode", sTableName + "pk_sendtype");
		dmdvo.setAttributeValue("pktrancust", sTableName + "pk_transcust");

		dmdvo.setAttributeValue("pksrccalbodyar", sTableName + "pkfromarea");
		dmdvo.setAttributeValue("pkarrivearea", sTableName + "pktoarea");

		dmdvo.setAttributeValue("pkinv", "pk_inventory");
		dmdvo.setAttributeValue("pk_inventory", "bd_invmandoc.pk_invmandoc");

		dmdvo.setAttributeValue("pkvehicletype", sTableName + "pk_vehicletype");

		dmdvo.setAttributeValue("pkdelivroute", sTableName + "pkroute");

		dmdvo.setAttributeValue("ipricetype", sTableName + "ipricetype");

		dmdvo.setAttributeValue("pkfromaddress", sTableName + "pkfromaddress");
		dmdvo.setAttributeValue("pktoaddress", sTableName + "pktoaddress");

		dmdvo.setAttributeValue("pkpacksort", sTableName + "pkpacksort");

		if (null != dmdvo.getAttributeValue(sTransBillKey)) { return ((String) dmdvo.getAttributeValue(sTransBillKey)); }
		return sTransBillKey;
	}

	public void checkOnePriceType(DmBasepriceVO[] vos) throws Exception {
		if ((vos == null) || (vos.length <= 0)) { return; }
		String errmsg = null;

		HashMap hs = new HashMap();
		String key = null;
		Integer iPriceType = null;
		DmBasepriceVO dmdatavo = null;
		for (int i = 0; i < vos.length; ++i) {
			if (vos[i].getStatus() == 3) continue;
			if (vos[i].getStatus() == 0) {
				continue;
			}
			key = "";
			key = key + vos[i].getAttributeValue("pkdelivorg");
			key = key + vos[i].getAttributeValue("pk_transcust");
			key = key + vos[i].getAttributeValue("pk_sendtype");
			iPriceType = (Integer) vos[i].getAttributeValue("ipricetype");
			key = key + iPriceType;

			dmdatavo = (DmBasepriceVO) hs.get(key);
			if (dmdatavo == null) {
				hs.put(key, vos[i]);
			} else {
				if ((iPriceType == null) || (iPriceType.equals(dmdatavo.getAttributeValue("ipricetype")))) {
					continue;
				}
				String[] sValue = {
						(String) vos[i].getAttributeValue("vtranscustname"), (String) vos[i].getAttributeValue("vtranscustname")
				};

				errmsg = NCLangResOnserver.getInstance().getStrByID("40140216", "UPP40140216-000096", null, sValue);

				throw new BusinessException(errmsg);
			}
		}

		if (hs.size() <= 0) { return; }
		vos = (DmBasepriceVO[]) (DmBasepriceVO[]) hs.values().toArray(new DmBasepriceVO[hs.size()]);

		for (int i = 0; i < vos.length; ++i) {
			if (vos[i].getStatus() == 3) continue;
			if (vos[i].getStatus() == 0) {
				continue;
			}
			String pkdelivorg = null;
			String pk_transcust = null;
			String pk_sendtype = null;

			pkdelivorg = (String) vos[i].getAttributeValue("pkdelivorg");
			pk_transcust = (String) vos[i].getAttributeValue("pk_transcust");

			pk_sendtype = (String) vos[i].getAttributeValue("pk_sendtype");

			String sql = " select ipricetype from dm_baseprice  where dr = 0 and pkdelivorg='" + pkdelivorg + "'  and pk_transcust='" + pk_transcust + "' and  pk_sendtype='" + pk_sendtype + "' ";

			SmartDMO sDMO = new SmartDMO();

			DmBasepriceVO[] ddvos = (DmBasepriceVO[]) (DmBasepriceVO[]) sDMO.selectBySql2(sql.toString(), DmBasepriceVO.class);
			if ((ddvos != null) && (ddvos.length != 0)) {
				DmBasepriceVO dvo = ddvos[0];
				Integer itemp = null;

				Object otemp = null;
				Integer ipricetype = null;
				otemp = dvo.getIpricetype();
				if (otemp == null) continue;
				if (otemp.toString().length() <= 0) continue;
				ipricetype = new Integer(otemp.toString().trim());
				if (itemp == null) {
					itemp = ipricetype;
				} else {
					if (!(itemp.equals(ipricetype))) {
						String[] value = {
								vos[i].getAttributeValue("vtranscustname").toString(), vos[i].getAttributeValue("vtranscustname").toString()
						};
						errmsg = NCLangResOnserver.getInstance().getStrByID("40140216", "UPP40140216-000096", null, value);
					}

					if ((errmsg != null) && (errmsg.trim().length() > 0)) throw new BusinessException(errmsg);
				}
			}
		}
	}

	private void dmvoDelivToPackSave(DMVO vo, String sPriceType) throws SQLException, javax.transaction.SystemException, BusinessException {
		String pkdelivorg = (String) vo.getParentVO().getAttributeValue("pkdelivorg");

		String pkdelivmode = (String) vo.getParentVO().getAttributeValue("pkdelivmode");

		String pktrancust = (String) vo.getParentVO().getAttributeValue("pktrancust");

		DMDataVO[] delivbvos = vo.getBodyVOs();

		Vector vForCalculate = new Vector();
		for (int i = 0; i < delivbvos.length; ++i) {
			String pkfromarea = (String) delivbvos[i].getAttributeValue("pksrccalbodyar");

			String pktoarea = (String) delivbvos[i].getAttributeValue("pkarrivearea");

			Hashtable htPackSortKeys = new Hashtable();

			DMDataVO dmdvoRow = new DMDataVO();
			dmdvoRow.setAttributeValue("pkdelivorg", pkdelivorg);
			dmdvoRow.setAttributeValue("pkdelivmode", pkdelivmode);
			dmdvoRow.setAttributeValue("pktranscust", pktrancust);
			dmdvoRow.setAttributeValue("pkfromarea", pkfromarea);
			dmdvoRow.setAttributeValue("pktoarea", pktoarea);
			dmdvoRow.setAttributeValue("ipricetype", sPriceType);

			dmdvoRow.setAttributeValue("dpacknum", "1");
			dmdvoRow.setAttributeValue("doneweight", delivbvos[i].getAttributeValue("dinvweight"));

			dmdvoRow.setAttributeValue("pkinv", delivbvos[i].getAttributeValue("pkinv"));

			vForCalculate.add(dmdvoRow);
		}
		DMDataVO[] dmdvosForCalculate = new DMDataVO[vForCalculate.size()];
		vForCalculate.copyInto(dmdvosForCalculate);
		DMDataVO headvo = new DMDataVO();
		headvo.setAttributeValue("billytpe", DMBillTypeConst.m_delivDelivBill);
		vo = new DMVO();
		vo.setParentVO(headvo);
		vo.setChildrenVO(dmdvosForCalculate);
	}

	public String getDELIVDATE(DMVO vo, UFDouble[] ufdForRow, Boolean bIsnotNeedHandSplit) throws SQLException, javax.transaction.SystemException, BusinessException {
		String DELIVDATE = vo.getParentVO().getAttributeValue("senddate").toString();

		return "\"" + DELIVDATE + "\"";
	}

	protected String getInvclassKeysByOrderBatch(DMDataVO orderVO) {
		String keyswithcoma = "";

		String[] sKeys = getKeysByOrderWithoutInvclassBatch(orderVO);
		for (int i = 0; i < sKeys.length; ++i) {
			keyswithcoma = keyswithcoma + sKeys[i] + " DESC ";
			if (i != sKeys.length - 1) {
				keyswithcoma = keyswithcoma + ",";
			}
		}
		return keyswithcoma;
	}

	protected String getInvclassWhereByOrder(DMDataVO order, DMDataVO dataWhere, String pricetypename) {
		String sWhRT = " dm_baseprice.dr=0 and " + pricetypename + " is not null and ";

		sWhRT = sWhRT + " dm_baseprice.bsltfrmlevel='" + ConstForBasePrice.BSltFrmLevel_Base.toString() + "' AND ";

		String sWhere = null;
		String[] sSel = order.getAttributeNames();

		for (int i = 0; i < sSel.length; ++i) {
			sWhere = translateTransBillKeyToPriceKey(sSel[i]);

			if (dataWhere.getAttributeValue(sSel[i]) == null) {
				sWhRT = sWhRT + "(" + sWhere + " is null ) ";
			} else if ((((Integer) order.getAttributeValue(sSel[i])).intValue() == 0) && (!(sWhere.equals("pk_inventory")))) {
				sWhRT = sWhRT + "(" + sWhere + " = '" + dataWhere.getAttributeValue(sSel[i]).toString() + "') ";
			} else if (sWhere.equals("pk_inventory")) {
				sWhere = translateTransBillKeyToPriceKey(sWhere);
				sWhRT = sWhRT + "( invcl.invclasscode in (select substring(bd_invcl.invclasscode,1, " + " len(invcl.invclasscode)) from bd_invbasdoc " + " inner join bd_invcl on bd_invbasdoc.pk_invcl=bd_invcl.pk_invcl " + " inner join bd_invmandoc on bd_invbasdoc.pk_invbasdoc = bd_invmandoc.pk_invbasdoc " + " where  " + sWhere + " = '" + dataWhere.getAttributeValue(sSel[i]).toString() + "'  ) " + ") ";
			} else {
				sWhRT = sWhRT + "(" + sWhere + " = '" + dataWhere.getAttributeValue(sSel[i]).toString() + "' or " + sWhere + " is null or  len(rtrim(" + sWhere + "))=0)  ";
			}

			if (i != sSel.length - 1) {
				sWhRT = sWhRT + " and ";
			}
		}
		return sWhRT;
	}

	protected String getInvclassWhereByOrderBatch(DMDataVO order, DMDataVO dataWhere, String pricetypename) {
		String sWhRT = " dm_quantitylevel_h.dr=0 and";
		if (DataTypeTool.isEqual("dprice", pricetypename)) sWhRT = " (dprice is not null OR dmoney is not null)";
		else {
			sWhRT = sWhRT + " " + pricetypename + " is not null ";
		}
		sWhRT = sWhRT + " and ";

		String sWhere = null;
		String[] sSel = order.getAttributeNames();

		for (int i = 0; i < sSel.length; ++i) {
			if (DataTypeTool.isEqual(sSel[i], "iuplimittype")) {
				continue;
			}

			sWhere = translateTransBillKeyToPriceKeyBatch(sSel[i]);

			String sPriceType = dataWhere.getAttributeValue("ipricetype").toString();

			if (sWhere.equals("ipricetype")) {
				sWhRT = sWhRT + "(ipricetype = " + sPriceType + ") and ";
				String sValue = null;
				if ((DataTypeTool.isEqual(sPriceType, PriceType.getTypeString(PriceType.iPackNum))) && (dataWhere.getAttributeValue("dpacknum") != null)) {
					sValue = dataWhere.getAttributeValue("dpacknum").toString();
				} else {
					sValue = dataWhere.getAttributeValue("dweight").toString();
				}
				sWhRT = sWhRT + " (dminnum < " + sValue + " and dmaxnum >= " + sValue + ") ";
			}

			if (dataWhere.getAttributeValue(sSel[i]) == null) {
				sWhRT = sWhRT + "(" + sWhere + " is null ) ";
			} else if ((((Integer) order.getAttributeValue(sSel[i])).intValue() == 0) && (!(sWhere.equals("pkinvclass")))) {
				sWhRT = sWhRT + "(" + sWhere + " = '" + dataWhere.getAttributeValue(sSel[i]).toString() + "') ";
			} else if (sWhere.equals("pkinvclass")) {
				sWhere = translateTransBillKeyToPriceKeyBatch(sWhere);
				sWhRT = sWhRT + "( invcl.invclasscode in (select substring(bd_invcl.invclasscode,1, " + " len(invcl.invclasscode)) from bd_invcl " + " where  " + " bd_invcl.pk_invcl " + " = '" + dataWhere.getAttributeValue(sSel[i]).toString() + "'  ) " + ") ";
			} else {
				sWhRT = sWhRT + "(" + sWhere + " = '" + dataWhere.getAttributeValue(sSel[i]).toString() + "' or " + sWhere + " is null or  len(rtrim(" + sWhere + "))=0)  ";
			}

			sWhRT = sWhRT + " and ";
		}
		String sPriceType = dataWhere.getAttributeValue("ipricetype").toString();

		sWhRT = sWhRT + "(ipricetype = " + sPriceType + ") and ";
		String sValue = null;
		if ((DataTypeTool.isEqual(sPriceType, PriceType.getTypeString(PriceType.iPackNum))) && (dataWhere.getAttributeValue("dpacknum") != null)) {
			sValue = dataWhere.getAttributeValue("dpacknum").toString();
		} else if ((DataTypeTool.isEqual(sPriceType, PriceType.getTypeString(PriceType.iVolumn))) && (dataWhere.getAttributeValue("dvolumn") != null)) {
			sValue = dataWhere.getAttributeValue("dvolumn").toString();
		} else {
			sValue = dataWhere.getAttributeValue("dweight").toString();
		}

		sWhRT = sWhRT + " (" + " \t(dm_quantitylevel_h.ilevelbndjudge=" + ConstForLevelHead.ILevelBndJudge_FullSum + " AND (dminnum < " + sValue + " and dmaxnum >= " + sValue + ") )" + " OR" + "\t(dm_quantitylevel_h.ilevelbndjudge=" + ConstForLevelHead.ILevelBndJudge_SectionSum + " AND (dmaxnum<" + sValue + "  OR (dminnum < " + sValue + " and dmaxnum >= " + sValue + ")) )" + ")";

		return sWhRT;
	}

	protected String[] getKeysByOrderWithoutInvclassBatch(DMDataVO orderVO) {
		String[] sKeyNames = orderVO.getAttributeNames();
		String[] select = new String[sKeyNames.length];
		DMVO dmvo = new DMVO(sKeyNames.length);
		for (int i = 0; i < sKeyNames.length; ++i) {
			dmvo.getBodyVOs()[i].setAttributeValue("orderkey", sKeyNames[i]);
			dmvo.getBodyVOs()[i].setAttributeValue("ordernum", orderVO.getAttributeValue(sKeyNames[i]));
		}

		int[] iAsc = new int[1];
		iAsc[0] = 1;
		dmvo.sortByKeys(new String[] {
			"ordernum"
		}, iAsc);

		for (int i = 0; i < dmvo.getBodyVOs().length; ++i) {
			String sTr = translateTransBillKeyToPriceKeyBatch(dmvo.getBodyVOs()[i].getAttributeValue("orderkey").toString());

			select[i] = sTr;
		}

		return select;
	}

	protected String getNormalKeysByOrderBatch(DMDataVO orderVO) {
		String keyswithcoma = "";

		String[] sKeys = getKeysByOrderWithoutInvclassBatch(orderVO);
		for (int i = 0; i < sKeys.length; ++i) {
			if (DataTypeTool.isEqual(sKeys[i], "iuplimittype")) {
				continue;
			}

			keyswithcoma = keyswithcoma + "coalesce(" + sKeys[i] + ",' ') as " + sKeys[i];

			if (i != sKeys.length - 1) {
				keyswithcoma = keyswithcoma + ",";
			}
		}

		return keyswithcoma;
	}

	protected String getNormalOrderKeysByOrderBatch(DMDataVO orderVO) {
		String keyswithcoma = "";

		String[] sKeys = getKeysByOrderWithoutInvclassBatch(orderVO);
		for (int i = 0; i < sKeys.length; ++i) {
			if ((sKeys[i].indexOf("ipricetype") >= 0) || (sKeys[i].indexOf("iuplimittype") >= 0)) {
				keyswithcoma = keyswithcoma + sKeys[i] + " DESC ";
			} else keyswithcoma = keyswithcoma + "ISNULL(" + sKeys[i] + ",' ') DESC ";

			if (i != sKeys.length - 1) {
				keyswithcoma = keyswithcoma + ",";
			}
		}

		keyswithcoma = keyswithcoma + ",dm_quantitylevel_b.dmaxnum DESC ";

		return keyswithcoma;
	}

	protected String getNormalWhereByOrder(DMDataVO order, DMDataVO dataWhere, String pricetypename) {
		String sWhRT = " dm_baseprice.dr=0 and " + pricetypename + " is not null and ";

		sWhRT = sWhRT + " dm_baseprice.bsltfrmlevel='" + ConstForBasePrice.BSltFrmLevel_Base.toString() + "' AND ";

		String sWhere = null;
		String[] sSel = order.getAttributeNames();
		for (int i = 0; i < sSel.length; ++i) {
			sWhere = translateTransBillKeyToPriceKey(sSel[i]);

			if (sWhere.equals("pk_inventory")) sWhere = translateTransBillKeyToPriceKey(sWhere);
			if (dataWhere.getAttributeValue(sSel[i]) == null) {
				sWhRT = sWhRT + "(" + sWhere + " is null ) ";
			} else {
				Object oValue = dataWhere.getAttributeValue(sSel[i]);
				if (oValue instanceof String) {
					oValue = "'" + oValue + "'";
				}

				if (((Integer) order.getAttributeValue(sSel[i])).intValue() != 0) {
					if (sWhere.equals("pkpacktype")) sWhere = "pkpacksort";
					sWhRT = sWhRT + "(" + sWhere + " = " + oValue + " or " + sWhere + " is null or len(rtrim(" + sWhere + "))=0)  ";
				} else {
					sWhRT = sWhRT + "(" + sWhere + " = " + oValue + ") ";
				}
			}

			if (i == sSel.length - 1) continue;
			sWhRT = sWhRT + " and ";
		}

		return sWhRT;
	}

	protected String getNormalWhereByOrderBatch(DMDataVO order, DMDataVO dataWhere, String pricetypename) {
		String sWhRT = " dm_quantitylevel_h.dr=0 and";
		if (DataTypeTool.isEqual("dprice", pricetypename)) sWhRT = sWhRT + " (dprice is not null OR dmoney is not null)";
		else {
			sWhRT = sWhRT + " " + pricetypename + " is not null ";
		}
		sWhRT = sWhRT + " and ";

		String sWhere = null;
		String[] sSel = order.getAttributeNames();

		for (int i = 0; i < sSel.length; ++i) {
			if (DataTypeTool.isEqual(sSel[i], "iuplimittype")) {
				continue;
			}

			sWhere = translateTransBillKeyToPriceKeyBatch(sSel[i]);
			if (dataWhere.getAttributeValue(sSel[i]) == null) {
				sWhRT = sWhRT + "(" + sWhere + " is null ) ";
			} else if (((Integer) order.getAttributeValue(sSel[i])).intValue() != 0) {
				sWhRT = sWhRT + "(" + sWhere + " = '" + dataWhere.getAttributeValue(sSel[i]).toString() + "' or " + sWhere + " is null or len(rtrim(" + sWhere + "))=0)  ";
			} else {
				sWhRT = sWhRT + "(" + sWhere + " = '" + dataWhere.getAttributeValue(sSel[i]).toString() + "') ";
			}

			sWhRT = sWhRT + " and ";
		}

		String sPriceType = dataWhere.getAttributeValue("ipricetype").toString();

		String sValue = null;
		if ((DataTypeTool.isEqual(sPriceType, PriceType.getTypeString(PriceType.iPackNum))) && (dataWhere.getAttributeValue("dpacknum") != null)) {
			sValue = dataWhere.getAttributeValue("dpacknum").toString();
		} else if ((DataTypeTool.isEqual(sPriceType, PriceType.getTypeString(PriceType.iWeight))) && (dataWhere.getAttributeValue("dweight") != null)) {
			sValue = dataWhere.getAttributeValue("dweight").toString();
		} else if ((DataTypeTool.isEqual(sPriceType, PriceType.getTypeString(PriceType.iVolumn))) && (dataWhere.getAttributeValue("dvolumn") != null)) {
			sValue = dataWhere.getAttributeValue("dvolumn").toString();
		}

		sWhRT = sWhRT + " (" + " \t(dm_quantitylevel_h.ilevelbndjudge=" + ConstForLevelHead.ILevelBndJudge_FullSum + " AND (dminnum < " + sValue + " and dmaxnum >= " + sValue + ") )" + " OR" + "\t(dm_quantitylevel_h.ilevelbndjudge=" + ConstForLevelHead.ILevelBndJudge_SectionSum + " AND (dmaxnum<" + sValue + "  OR (dminnum < " + sValue + " and dmaxnum >= " + sValue + ")) )" + ")";

		return sWhRT;
	}

	public UFDouble getPL001(DMVO voDelivBill, UFDouble[] ufdForRow, Boolean bIsnotNeedHandSplit) throws Exception {
		String sMethodName = "nc.bs.dm.dm004.BasepriceDMO.getPL001(DMVO, UFDouble [], Boolean)";

		if (voDelivBill == null) {
			String[] sValue = {
				sMethodName
			};
			throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40140216", "UPP40140216-000097", null, sValue));
		}

		String sPkvehicletype = (String) voDelivBill.getParentVO().getAttributeValue("pkvehicletype");

		UFDouble dTotal = DataTypeConst.UFDOUBLE_0;

		DMDataVO[] voaDisplayPack = (DMDataVO[]) (DMDataVO[]) voDelivBill.getParentVO().getAttributeValue("packagebill");

		if ((voaDisplayPack == null) || (voaDisplayPack.length == 0)) { return dTotal; }

		DMDataVO[] voaForCalculate = null;
		if (this._DEBUG) {
			voaForCalculate = test_packvoToSave(voDelivBill, PriceType.iWeight + "", false);

			int iPackLen = voaDisplayPack.length;
			for (int i = 0; i < iPackLen; ++i) {
				voaForCalculate[i].setAttributeValue("dweight", DataTypeTool.getUFDouble_NullAs0(voaForCalculate[i].getAttributeValue("dpacknum")).multiply(DataTypeTool.getUFDouble_NullAs0(voaForCalculate[i].getAttributeValue("doneweight"))));
			}

		} else {
			voaForCalculate = packvoToSave(voDelivBill, PriceType.iWeight + "");
		}

		DMVO voValid = new DMVO();
		voValid.setChildrenVO(voaForCalculate);
		voValid.filterNullValueRow(new String[] {
			"dweight"
		});
		VOTools.filterZeroValueRow(voValid, new String[] {
			"dweight"
		});
		if ((voValid.getBodyVOs() == null) || (voValid.getBodyVOs().length == 0)) { return dTotal; }

		String[] saGroupKey = {
				"pkdelivorg", "pkdelivmode", "pktrancust", "pkfromaddress", "pktoaddress", "ipricetype", "pkvehicletype"
		};

		DMVO[] voaAfterGroup = voValid.getGroupKeyVOs(saGroupKey);
		ArrayList listDataVO = new ArrayList();
		for (int i = 0; i < voaAfterGroup.length; ++i) {
			DMVO dmvoSum = voaAfterGroup[i].getNumSum(saGroupKey, new String[] {
				"dweight"
			});

			dmvoSum.getBodyVOs()[0].setAttributeValue("pkvehicletype", sPkvehicletype);

			listDataVO.add(dmvoSum.getBodyVOs()[0]);
		}
		voaForCalculate = (DMDataVO[]) (DMDataVO[]) listDataVO.toArray(new DMDataVO[listDataVO.size()]);

		DMDataVO dmdvoOrder = new DMDataVO();
		dmdvoOrder.setAttributeValue("pkdelivorg", DataTypeConst.INTEGER_0);
		dmdvoOrder.setAttributeValue("pkdelivmode", DataTypeConst.INTEGER_0);
		dmdvoOrder.setAttributeValue("pktrancust", DataTypeConst.INTEGER_0);

		dmdvoOrder.setAttributeValue("pkpacktype", DataTypeConst.INTEGER_0);

		dmdvoOrder.setAttributeValue("pkvehicletype", DataTypeConst.INTEGER_1);
		dmdvoOrder.setAttributeValue("pkfromaddress", DataTypeConst.INTEGER_1);
		dmdvoOrder.setAttributeValue("pktoaddress", DataTypeConst.INTEGER_2);

		DMDataVO[][] voa2BatchPrice = queryPriceBatchVO(voaForCalculate, dmdvoOrder, "dprice");

		dTotal = parseMnyFrmBatchVOs(voa2BatchPrice, voaForCalculate, "dweight");

		return dTotal;
	}

	public UFDouble getPriceVolumnLev(DMVO voDelivBill, UFDouble[] ufdForRow, Boolean bIsnotNeedHandSplit) throws Exception {
		String sMethodName = "nc.bs.dm.dm004.BasepriceDMO.getPriceVolumnLev(DMVO, UFDouble [], Boolean)";

		if (voDelivBill == null) {
			String[] sValue = {
				sMethodName
			};
			throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40140216", "UPP40140216-000097", null, sValue));
		}

		String sPkvehicletype = (String) voDelivBill.getParentVO().getAttributeValue("pkvehicletype");

		UFDouble dTotal = DataTypeConst.UFDOUBLE_0;

		DMDataVO[] voaDisplayPack = (DMDataVO[]) (DMDataVO[]) voDelivBill.getParentVO().getAttributeValue("packagebill");

		if ((voaDisplayPack == null) || (voaDisplayPack.length == 0)) { return dTotal; }

		DMDataVO[] voaForCalculate = null;
		if (this._DEBUG) {
			voaForCalculate = test_packvoToSave(voDelivBill, PriceType.iWeight + "", false);

			int iPackLen = voaDisplayPack.length;
			for (int i = 0; i < iPackLen; ++i) {
				voaForCalculate[i].setAttributeValue("dweight", DataTypeTool.getUFDouble_NullAs0(voaForCalculate[i].getAttributeValue("dpacknum")).multiply(DataTypeTool.getUFDouble_NullAs0(voaForCalculate[i].getAttributeValue("doneweight"))));
			}

		} else {
			voaForCalculate = packvoToSave(voDelivBill, PriceType.iVolumn + "");
		}

		DMVO voValid = new DMVO();
		voValid.setChildrenVO(voaForCalculate);
		voValid.filterNullValueRow(new String[] {
			"dvolumn"
		});
		VOTools.filterZeroValueRow(voValid, new String[] {
			"dvolumn"
		});
		if ((voValid.getBodyVOs() == null) || (voValid.getBodyVOs().length == 0)) { return dTotal; }

		String[] saGroupKey = {
				"pkdelivorg", "pkdelivmode", "pktrancust", "pkfromaddress", "pktoaddress", "ipricetype", "pkvehicletype", "pkpacktype"
		};

		DMVO[] voaAfterGroup = voValid.getGroupKeyVOs(saGroupKey);
		ArrayList listDataVO = new ArrayList();
		for (int i = 0; i < voaAfterGroup.length; ++i) {
			DMVO dmvoSum = voaAfterGroup[i].getNumSum(saGroupKey, new String[] {
				"dvolumn"
			});

			dmvoSum.getBodyVOs()[0].setAttributeValue("pkvehicletype", sPkvehicletype);

			listDataVO.add(dmvoSum.getBodyVOs()[0]);
		}
		voaForCalculate = (DMDataVO[]) (DMDataVO[]) listDataVO.toArray(new DMDataVO[listDataVO.size()]);

		DMDataVO dmdvoOrder = new DMDataVO();
		dmdvoOrder.setAttributeValue("pkdelivorg", DataTypeConst.INTEGER_0);
		dmdvoOrder.setAttributeValue("pkdelivmode", DataTypeConst.INTEGER_0);
		dmdvoOrder.setAttributeValue("pktrancust", DataTypeConst.INTEGER_0);

		dmdvoOrder.setAttributeValue("pkvehicletype", DataTypeConst.INTEGER_1);
		dmdvoOrder.setAttributeValue("pkfromaddress", DataTypeConst.INTEGER_1);
		dmdvoOrder.setAttributeValue("pktoaddress", DataTypeConst.INTEGER_2);
		dmdvoOrder.setAttributeValue("pkpacktype", DataTypeConst.INTEGER_2);

		DMDataVO[][] voa2BatchPrice = queryPriceBatchVO(voaForCalculate, dmdvoOrder, "dprice");

		dTotal = parseMnyFrmBatchVOs(voa2BatchPrice, voaForCalculate, "dvolumn");

		return dTotal;
	}

	public UFDouble getPL002(DMVO vo, UFDouble[] ufdForRow, Boolean bIsnotNeedHandSplit) throws Exception {
		UFDouble dtotal = new UFDouble(0);

		DMDataVO[] packvos = (DMDataVO[]) (DMDataVO[]) vo.getParentVO().getAttributeValue("packagebill");

		if ((packvos == null) || (packvos.length == 0)) { return dtotal; }

		DMDataVO[] dmdvosForCalculate = packvoToSave(vo, PriceType.iWeight + "");

		DMVO dmvo = new DMVO();
		dmvo.setChildrenVO(dmdvosForCalculate);
		dmvo.filterNullValueRow(new String[] {
			"dpacknum"
		});
		dmvo.filterNullValueRow(new String[] {
			"doneweight"
		});
		VOTools.filterZeroValueRow(dmvo, new String[] {
			"dpacknum"
		});
		VOTools.filterZeroValueRow(dmvo, new String[] {
			"doneweight"
		});
		dmdvosForCalculate = dmvo.getBodyVOs();

		DMDataVO dmdvoOrder = new DMDataVO();
		dmdvoOrder.setAttributeValue("pkdelivorg", new Integer(0));
		dmdvoOrder.setAttributeValue("pkdelivmode", new Integer(0));
		dmdvoOrder.setAttributeValue("pkfromarea", new Integer(0));
		dmdvoOrder.setAttributeValue("pktoarea", new Integer(0));
		dmdvoOrder.setAttributeValue("pkpacktype", new Integer(4));
		dmdvoOrder.setAttributeValue("pktranscust", new Integer(0));
		dmdvoOrder.setAttributeValue("pkinvclass", new Integer(3));
		dmdvoOrder.setAttributeValue("pkfromaddress", new Integer(1));
		dmdvoOrder.setAttributeValue("pktoaddress", new Integer(2));

		queryPriceBatch(dmdvosForCalculate, dmdvoOrder, "vformulacode");

		FormulaParse fparse = new FormulaParse();
		if ((null != dmdvosForCalculate) && (dmdvosForCalculate.length != 0)) {
			UFDouble dbaseprice = new UFDouble(0);
			UFDouble dnum = new UFDouble(0);
			for (int i = 0; i < dmdvosForCalculate.length; ++i) {
				String sFormula = dmdvosForCalculate[i].getAttributeValue("vformulacode").toString();

				Hashtable htOneWeight = new Hashtable();
				htOneWeight.put("num", dmdvosForCalculate[i].getAttributeValue("doneweight").toString());

				fparse.setExpress(sFormula);
				fparse.setData(htOneWeight);
				String sValue = fparse.getValue();

				dmdvosForCalculate[i].setAttributeValue("dprice", sValue);

				if ((null != sValue) && (sValue.trim().length() != 0)) dbaseprice = new UFDouble(sValue);
				else dbaseprice = new UFDouble(0);
				if (null != dmdvosForCalculate[i].getAttributeValue("dpacknum")) {
					dnum = new UFDouble(dmdvosForCalculate[i].getAttributeValue("dpacknum").toString());
				} else dnum = new UFDouble(0);
				dtotal = dtotal.add(dbaseprice.multiply(dnum));
			}
		}
		return dtotal;
	}

	public UFDouble getPL003(DMVO vo, UFDouble[] ufdForRow, Boolean bIsnotNeedHandSplit) throws Exception {
		UFDouble dtotal = new UFDouble(0);

		DMDataVO[] packvos = (DMDataVO[]) (DMDataVO[]) vo.getParentVO().getAttributeValue("packagebill");

		if ((packvos == null) || (packvos.length == 0)) { return dtotal; }

		DMDataVO[] dmdvosForCalculate = packvoToSave(vo, PriceType.iWeight + "");

		DMVO dmvoForCalculate = new DMVO();
		dmvoForCalculate.setChildrenVO(dmdvosForCalculate);
		dmvoForCalculate.filterNullValueRow(new String[] {
			"dpacknum"
		});
		dmvoForCalculate.filterNullValueRow(new String[] {
			"doneweight"
		});
		VOTools.filterZeroValueRow(dmvoForCalculate, new String[] {
			"dpacknum"
		});

		VOTools.filterZeroValueRow(dmvoForCalculate, new String[] {
			"doneweight"
		});

		dmdvosForCalculate = dmvoForCalculate.getBodyVOs();

		DMDataVO dmdvoOrder = new DMDataVO();
		dmdvoOrder.setAttributeValue("pkdelivorg", new Integer(0));
		dmdvoOrder.setAttributeValue("pkdelivmode", new Integer(0));
		dmdvoOrder.setAttributeValue("pkfromarea", new Integer(0));
		dmdvoOrder.setAttributeValue("pktoarea", new Integer(0));
		dmdvoOrder.setAttributeValue("pkpacktype", new Integer(4));
		dmdvoOrder.setAttributeValue("pktranscust", new Integer(0));
		dmdvoOrder.setAttributeValue("pkinvclass", new Integer(3));
		dmdvoOrder.setAttributeValue("pkfromaddress", new Integer(1));
		dmdvoOrder.setAttributeValue("pktoaddress", new Integer(2));

		queryPriceBatch(dmdvosForCalculate, dmdvoOrder, "vformulacode");

		FormulaParse fparse = new FormulaParse();
		if ((null != dmdvosForCalculate) && (dmdvosForCalculate.length != 0)) {
			UFDouble dbaseprice = new UFDouble(0);
			for (int i = 0; i < dmdvosForCalculate.length; ++i) {
				String sFormula = dmdvosForCalculate[i].getAttributeValue("vformulacode").toString();

				Hashtable htOneWeight = new Hashtable();

				htOneWeight.put("num", dmdvosForCalculate[i].getAttributeValue("doneweight").toString());

				fparse.setExpress(sFormula);
				fparse.setData(htOneWeight);
				String sValue = fparse.getValue();

				dmdvosForCalculate[i].setAttributeValue("dprice", sValue);
			}

		}

		DMDataVO[] dmdvosForCalculateClones = new DMDataVO[dmdvosForCalculate.length];
		for (int i = 0; i < dmdvosForCalculate.length; ++i) {
			dmdvosForCalculateClones[i] = new DMDataVO();
			dmdvosForCalculateClones[i] = ((DMDataVO) dmdvosForCalculate[i].clone());
		}

		DMVO dmvoForCalculateClone = new DMVO();
		dmvoForCalculateClone.setChildrenVO(dmdvosForCalculateClones);

		DMVO dmvo_i = dmvoForCalculateClone.getNumSum(new String[] {
				"pkdelivorg", "pkdelivmode", "pktranscust", "pkfromarea", "pktoarea", "pkfromaddress", "pktoaddress", "ipricetype"
		}, new String[] {
			"dpacknum"
		});

		dmdvosForCalculateClones = dmvo_i.getBodyVOs();

		for (int i = 0; i < dmdvosForCalculateClones.length; ++i) {
			dmdvosForCalculateClones[i].setAttributeValue("ipricetype", PriceType.iPackNum + "");
		}

		dmdvoOrder = new DMDataVO();
		dmdvoOrder.setAttributeValue("pkdelivorg", new Integer(0));
		dmdvoOrder.setAttributeValue("pkdelivmode", new Integer(0));
		dmdvoOrder.setAttributeValue("pkfromarea", new Integer(0));
		dmdvoOrder.setAttributeValue("pktoarea", new Integer(0));

		dmdvoOrder.setAttributeValue("pktranscust", new Integer(0));

		dmdvoOrder.setAttributeValue("pkfromaddress", new Integer(1));
		dmdvoOrder.setAttributeValue("pktoaddress", new Integer(2));

		queryPriceBatch(dmdvosForCalculateClones, dmdvoOrder, "ddiscountrate");

		for (int i = 0; i < dmdvosForCalculate.length; ++i) {
			for (int j = 0; j < dmdvosForCalculateClones.length; ++j) {
				if (!(VOTools.isSameRow(dmdvosForCalculate[i], dmdvosForCalculateClones[j], new String[] {
						"pkdelivorg", "pkdelivmode", "pktranscust", "pkfromarea", "pktoarea", "pkfromaddress", "pktoaddress"
				}))) {
					continue;
				}

				dmdvosForCalculate[i].setAttributeValue("ddiscountrate", dmdvosForCalculateClones[j].getAttributeValue("ddiscountrate"));
			}

		}

		for (int i = 0; i < dmdvosForCalculate.length; ++i) {
			String sPacknum = dmdvosForCalculate[i].getAttributeValue("dpacknum").toString();

			UFDouble dPacknum = null;
			if ((null != sPacknum) && (sPacknum.trim().length() != 0)) dPacknum = new UFDouble(sPacknum);
			else {
				dPacknum = new UFDouble(0);
			}
			String sValue = dmdvosForCalculate[i].getAttributeValue("dprice").toString();

			UFDouble dbaseprice = null;
			if ((null != sValue) && (sValue.trim().length() != 0)) dbaseprice = new UFDouble(sValue);
			else {
				dbaseprice = new UFDouble(0);
			}
			String sDiscount = dmdvosForCalculate[i].getAttributeValue("ddiscountrate").toString();

			UFDouble dDiscount = null;
			if ((null != sValue) && (sValue.trim().length() != 0)) dDiscount = new UFDouble(sDiscount);
			else {
				dDiscount = new UFDouble(0);
			}
			dDiscount = dDiscount.div(new UFDouble(100));
			UFDouble ufdResult = dbaseprice.multiply(dPacknum).multiply(dDiscount);

			dtotal = dtotal.add(ufdResult);
		}
		return dtotal;
	}

	public UFDouble getPL004(DMVO voDelivBill, UFDouble[] ufdForRow, Boolean bIsnotNeedHandSplit) throws Exception {
		String sMethodName = "nc.bs.dm.dm004.BasepriceDMO.getPL004(DMVO, UFDouble [], Boolean)";

		UFDouble dTotal = DataTypeConst.UFDOUBLE_0;

		if (voDelivBill == null) { throw new BusinessException(sMethodName + NCLangResOnserver.getInstance().getStrByID("40140216", "UPP40140216-000097")); }

		DMDataVO[] voaDisplayPack = (DMDataVO[]) (DMDataVO[]) voDelivBill.getParentVO().getAttributeValue("packagebill");

		if ((voaDisplayPack == null) || (voaDisplayPack.length == 0)) { return dTotal; }

		DMDataVO[] voaForCalculate = null;
		if (this._DEBUG) {
			voaForCalculate = test_packvoToSave(voDelivBill, PriceType.iPackNum + "", false);

			int iPackLen = voaDisplayPack.length;
			for (int i = 0; i < iPackLen; ++i) {
				voaForCalculate[i].setAttributeValue("dweight", DataTypeTool.getUFDouble_NullAs0(voaForCalculate[i].getAttributeValue("dpacknum")).multiply(DataTypeTool.getUFDouble_NullAs0(voaForCalculate[i].getAttributeValue("doneweight"))));
			}

		} else {
			voaForCalculate = packvoToSave(voDelivBill, PriceType.iPackNum + "");
		}

		DMVO voValid = new DMVO();
		voValid.setChildrenVO(voaForCalculate);
		voValid.filterNullValueRow(new String[] {
			"dpacknum"
		});
		VOTools.filterZeroValueRow(voValid, new String[] {
			"dpacknum"
		});

		String[] saGroupKey = {
				"pkdelivorg", "pkdelivmode", "pktrancust", "pkfromaddress", "pktoaddress", "ipricetype", "pkvehicletype"
		};

		DMVO voSumTemp = voValid.getNumSum(saGroupKey, new String[] {
				"dpacknum", "dweight"
		});

		voaForCalculate = voSumTemp.getBodyVOs();

		DMDataVO dmdvoOrder = new DMDataVO();
		dmdvoOrder.setAttributeValue("pkdelivorg", DataTypeConst.INTEGER_0);
		dmdvoOrder.setAttributeValue("pkdelivmode", DataTypeConst.INTEGER_0);
		dmdvoOrder.setAttributeValue("pktrancust", DataTypeConst.INTEGER_0);

		dmdvoOrder.setAttributeValue("pkpacktype", DataTypeConst.INTEGER_0);

		dmdvoOrder.setAttributeValue("iuplimittype", DataTypeConst.INTEGER_0);

		dmdvoOrder.setAttributeValue("pkvehicletype", DataTypeConst.INTEGER_1);
		dmdvoOrder.setAttributeValue("pkfromaddress", DataTypeConst.INTEGER_1);
		dmdvoOrder.setAttributeValue("pktoaddress", DataTypeConst.INTEGER_2);

		DMDataVO[][] voa2BatchPrice = queryPriceBatchVO(voaForCalculate, dmdvoOrder, "dprice");

		dTotal = parseMnyFrmBatchVOs(voa2BatchPrice, voaForCalculate, "dpacknum");

		return dTotal;
	}

	public UFDouble getPL005(DMVO voDelivBill, UFDouble[] ufdForRow, Boolean bIsnotNeedHandSplit) throws Exception {
		String sMethodName = "nc.bs.dm.dm004.BasepriceDMO.getPL005(DMVO, UFDouble [], Boolean)";

		UFDouble dTotal = DataTypeConst.UFDOUBLE_0;

		if (voDelivBill == null) { throw new BusinessException(sMethodName + NCLangResOnserver.getInstance().getStrByID("40140216", "UPP40140216-000097")); }

		DMDataVO[] voaDisplayPack = (DMDataVO[]) (DMDataVO[]) voDelivBill.getParentVO().getAttributeValue("packagebill");

		if ((voaDisplayPack == null) || (voaDisplayPack.length == 0)) { return dTotal; }

		DMDataVO[] voaForCalculate = null;
		if (this._DEBUG) {
			voaForCalculate = test_packvoToSave(voDelivBill, PriceType.iPackNum + "", false);

			int iPackLen = voaDisplayPack.length;
			for (int i = 0; i < iPackLen; ++i) {
				voaForCalculate[i].setAttributeValue("dweight", DataTypeTool.getUFDouble_NullAs0(voaForCalculate[i].getAttributeValue("dpacknum")).multiply(DataTypeTool.getUFDouble_NullAs0(voaForCalculate[i].getAttributeValue("doneweight"))));
			}

		} else {
			voaForCalculate = packvoToSave(voDelivBill, PriceType.iPackNum + "");
		}

		DMVO voValid = new DMVO();
		voValid.setChildrenVO(voaForCalculate);
		voValid.filterNullValueRow(new String[] {
			"dpacknum"
		});
		VOTools.filterZeroValueRow(voValid, new String[] {
			"dpacknum"
		});

		String[] saGroupKey = {
				"pkdelivorg", "pkdelivmode", "pktrancust", "pkfromaddress", "pktoaddress", "ipricetype", "pkpacktype", "pkvehicletype"
		};

		DMVO voSumTemp = voValid.getNumSum(saGroupKey, new String[] {
				"dpacknum", "dweight"
		});

		voaForCalculate = voSumTemp.getBodyVOs();

		DMDataVO dmdvoOrder = new DMDataVO();
		dmdvoOrder.setAttributeValue("pkdelivorg", DataTypeConst.INTEGER_0);
		dmdvoOrder.setAttributeValue("pkdelivmode", DataTypeConst.INTEGER_0);
		dmdvoOrder.setAttributeValue("pktrancust", DataTypeConst.INTEGER_0);

		dmdvoOrder.setAttributeValue("pkpacktype", DataTypeConst.INTEGER_0);

		dmdvoOrder.setAttributeValue("iuplimittype", DataTypeConst.INTEGER_0);

		dmdvoOrder.setAttributeValue("pkvehicletype", DataTypeConst.INTEGER_1);
		dmdvoOrder.setAttributeValue("pkfromaddress", DataTypeConst.INTEGER_1);
		dmdvoOrder.setAttributeValue("pktoaddress", DataTypeConst.INTEGER_2);

		DMDataVO[][] voa2BatchPrice = queryPriceBatchVO(voaForCalculate, dmdvoOrder, "dprice");

		dTotal = parseMnyFrmBatchVOs(voa2BatchPrice, voaForCalculate, "dpacknum");

		return dTotal;
	}

	protected String getPriceBatch(DMDataVO dataVOCon, DMDataVO order, String pricetypename) throws Exception {
		String sSelect = null;
		String sWhere = null;
		String sFrom = null;
		String sOrderBy = null;
		StringBuffer sql = new StringBuffer();

		Integer iForInv = DataTypeConst.INTEGER_0;
		Integer iForInvClass = DataTypeConst.INTEGER_0;

		if (isExtInvInOrder(order)) {
			iForInv = (Integer) order.getAttributeValue("pkinv");
			order.setAttributeValue("pkinv", DataTypeConst.INTEGER_0);
		}
		if (isExtInvClassInOrder(order)) {
			iForInvClass = (Integer) order.getAttributeValue("pkinvclass");
			order.setAttributeValue("pkinvclass", DataTypeConst.INTEGER_0);
		}

		sSelect = getNormalKeysByOrderBatch(order) + "," + pricetypename;
		sWhere = getNormalWhereByOrderBatch(order, dataVOCon, pricetypename);
		if (isExtInvInOrder(order)) {
			sFrom = " dm_quantitylevel_h inner join dm_quantitylevel_b on dm_quantitylevel_h.pk_quantitylevel_h = dm_quantitylevel_b.pk_quantitylevel_h  inner join bd_invbasdoc  on dm_quantitylevel_h.pk_inv=bd_invbasdoc.pk_invbasdoc  inner join bd_invmandoc on bd_invmandoc.pk_invbasdoc = bd_invbasdoc.pk_invbasdoc ";
		} else {
			sFrom = "dm_quantitylevel_h inner join dm_quantitylevel_b on dm_quantitylevel_h.pk_quantitylevel_h = dm_quantitylevel_b.pk_quantitylevel_h ";
		}
		sOrderBy = getNormalOrderKeysByOrderBatch(order);

		sql.append("select  " + sSelect + " from " + sFrom + " where " + sWhere + " order by " + sOrderBy);

		DMDataVO[] dmdataVOrt = query(sql);

		if ((null != dmdataVOrt) && (dmdataVOrt.length > 0)) { return dmdataVOrt[0].getAttributeValue(pricetypename).toString(); }

		if ((isExtInvInOrder(order)) || (isExtInvClassInOrder(order))) {
			sSelect = getSelInvclassKeysByOrderBatch(order) + ", dm_quantitylevel_b." + pricetypename;

			sWhere = getInvclassWhereByOrderBatch(order, dataVOCon, pricetypename);

			sFrom = " dm_quantitylevel_h inner join dm_quantitylevel_b on dm_quantitylevel_h.pk_quantitylevel_h = dm_quantitylevel_b.pk_quantitylevel_h  inner join bd_invcl invcl on dm_quantitylevel_h.pkinvclass=invcl.pk_invcl ";

			sOrderBy = getInvclassKeysByOrderBatch(order);

			sql = new StringBuffer();
			sql.append("select  " + sSelect + " from " + sFrom + " where " + sWhere + " order by " + sOrderBy);

			dmdataVOrt = query(sql);

			if ((null != dmdataVOrt) && (dmdataVOrt.length > 0)) { return dmdataVOrt[0].getAttributeValue(pricetypename).toString(); }

			if ((iForInv.intValue() != 0) || (iForInvClass.intValue() != 0)) {
				if (iForInv.intValue() != 0) order.setAttributeValue("pkinv", iForInv);
				if (iForInvClass.intValue() != 0) order.setAttributeValue("pkinvclass", iForInvClass);
				sSelect = getNormalKeysByOrderBatch(order) + ", " + pricetypename;

				sWhere = getNormalWhereByOrderBatch(order, dataVOCon, pricetypename);

				sFrom = " dm_quantitylevel_h left outer join bd_invbasdoc  on dm_quantitylevel_h.pkinv=bd_invbasdoc.pk_invbasdoc  inner join bd_invmandoc on bd_invmandoc.pk_invbasdoc = bd_invbasdoc.pk_invbasdoc  inner join dm_quantitylevel_b on dm_quantitylevel_h.pk_quantitylevel_h = dm_quantitylevel_b.pk_quantitylevel_h ";

				sOrderBy = getNormalOrderKeysByOrderBatch(order);

				sql = new StringBuffer();
				sql.append(" select  " + sSelect + " from " + sFrom + " where " + sWhere + " order by " + sOrderBy);

				dmdataVOrt = query(sql);

				if ((null != dmdataVOrt) && (dmdataVOrt.length > 0)) { return dmdataVOrt[0].getAttributeValue(pricetypename).toString(); }
			}

		}

		return "0";
	}

	protected DMDataVO[] getPriceBatchVOs(DMDataVO dataVOCon, DMDataVO order, String sPriceTypeName) throws Exception {
		String sWhere = null;
		String sFrom = null;
		String sOrderBy = null;
		StringBuffer sql = new StringBuffer();

		Integer iForInv = DataTypeConst.INTEGER_0;
		Integer iForInvClass = DataTypeConst.INTEGER_0;

		if (isExtInvInOrder(order)) {
			iForInv = (Integer) order.getAttributeValue("pkinv");
			order.setAttributeValue("pkinv", DataTypeConst.INTEGER_0);
		}
		if (isExtInvClassInOrder(order)) {
			iForInvClass = (Integer) order.getAttributeValue("pkinvclass");
			order.setAttributeValue("pkinvclass", DataTypeConst.INTEGER_0);
		}

		String[] saBLevelDbField = StringTools.getDbFields("dm_quantitylevel_b", new VRHQuantityLevelBody());

		String sSelect = StringTools.getSelectStringByFields(null, saBLevelDbField);

		sSelect = sSelect + ",dm_quantitylevel_h.ilevelbndjudge";

		if (order.getAttributeValue("iuplimittype") != null) {
			sSelect = sSelect + ",dm_baseprice.iuplimittype,dm_baseprice.nuplimitnum,dm_baseprice.noveruplmtprice";
		}

		sWhere = getNormalWhereByOrderBatch(order, dataVOCon, sPriceTypeName);

		if (isExtInvInOrder(order)) {
			sFrom = " dm_quantitylevel_h inner join dm_quantitylevel_b on dm_quantitylevel_h.pk_quantitylevel_h = dm_quantitylevel_b.pk_quantitylevel_h  inner join bd_invbasdoc  on dm_quantitylevel_h.pk_inv=bd_invbasdoc.pk_invbasdoc  inner join bd_invmandoc on bd_invmandoc.pk_invbasdoc = bd_invbasdoc.pk_invbasdoc ";
		} else {
			sFrom = "dm_quantitylevel_h inner join dm_quantitylevel_b on dm_quantitylevel_h.pk_quantitylevel_h = dm_quantitylevel_b.pk_quantitylevel_h ";
			if (order.getAttributeValue("iuplimittype") != null) {
				sFrom = sFrom + " JOIN dm_baseprice ON dm_quantitylevel_h.pkbasicprice=dm_baseprice.pk_basicprice";
			}
		}

		sOrderBy = getNormalOrderKeysByOrderBatch(order);
		sql.append("select  " + sSelect + " from " + sFrom + " where " + sWhere + " order by " + sOrderBy);

		DMDataVO[] dmdataVOrt = query(sql);

		if ((((dmdataVOrt == null) || (dmdataVOrt.length == 0))) && (((isExtInvInOrder(order)) || (isExtInvClassInOrder(order))))) {
			sSelect = getSelInvclassKeysByOrderBatch(order) + ", dm_quantitylevel_b." + sPriceTypeName;

			sWhere = getInvclassWhereByOrderBatch(order, dataVOCon, sPriceTypeName);

			sFrom = " dm_quantitylevel_h inner join dm_quantitylevel_b on dm_quantitylevel_h.pk_quantitylevel_h = dm_quantitylevel_b.pk_quantitylevel_h  inner join bd_invcl invcl on dm_quantitylevel_h.pkinvclass=invcl.pk_invcl ";

			sOrderBy = getInvclassKeysByOrderBatch(order);

			sql = new StringBuffer();
			sql.append("select  " + sSelect + " from " + sFrom + " where " + sWhere + " order by " + sOrderBy);

			dmdataVOrt = query(sql);

			if ((((dmdataVOrt == null) || (dmdataVOrt.length == 0))) && (((iForInv.intValue() != 0) || (iForInvClass.intValue() != 0)))) {
				if (iForInv.intValue() != 0) order.setAttributeValue("pkinv", iForInv);
				if (iForInvClass.intValue() != 0) order.setAttributeValue("pkinvclass", iForInvClass);
				sSelect = getNormalKeysByOrderBatch(order) + ", " + sPriceTypeName;

				sWhere = getNormalWhereByOrderBatch(order, dataVOCon, sPriceTypeName);

				sFrom = " dm_quantitylevel_h left outer join bd_invbasdoc  on dm_quantitylevel_h.pkinv=bd_invbasdoc.pk_invbasdoc  left outer join bd_invmandoc on bd_invmandoc.pk_invbasdoc = bd_invbasdoc.pk_invbasdoc  inner join dm_quantitylevel_b on dm_quantitylevel_h.pk_quantitylevel_h = dm_quantitylevel_b.pk_quantitylevel_h ";

				sOrderBy = getNormalOrderKeysByOrderBatch(order);

				sql = new StringBuffer();
				sql.append(" select  " + sSelect + " from " + sFrom + " where " + sWhere + " order by " + sOrderBy);

				dmdataVOrt = query(sql);
			}

		}

		if ((null != dmdataVOrt) && (dmdataVOrt.length > 0)) { return dmdataVOrt; }

		return null;
	}

	protected DMDataVO getPriceVO(DMDataVO dataVOCon, DMDataVO order, String pricetypename) throws Exception {
		Integer iForInv = DataTypeConst.INTEGER_0;

		if (isExtInvInOrder(order)) {
			iForInv = (Integer) order.getAttributeValue("pkinv");
			order.setAttributeValue("pkinv", DataTypeConst.INTEGER_0);
		}

		String sFrom = null;
		String sOrderBy = null;
		StringBuffer sql = new StringBuffer();

		String[] saBaseDbField = StringTools.getDbFields("dm_baseprice", new ValueRangeHashtableBaseprice());

		String sSelect = StringTools.getSelectStringByFields(null, saBaseDbField);

		String sWhere = getNormalWhereByOrder(order, dataVOCon, pricetypename);
		if (isExtInvInOrder(order)) {
			sFrom = " dm_baseprice inner join bd_invbasdoc  on dm_baseprice.pk_inventory=bd_invbasdoc.pk_invbasdoc  inner join bd_invmandoc on bd_invmandoc.pk_invbasdoc = bd_invbasdoc.pk_invbasdoc ";
		} else {
			sFrom = " dm_baseprice ";
		}
		sOrderBy = getNormalOrderKeysByOrder(order);

		sql.append("select  " + sSelect + " from " + sFrom + " where " + sWhere);

		DMDataVO[] dmdataVOrt = query(sql);

		if ((null != dmdataVOrt) && (dmdataVOrt.length > 0)) {
			if ((dmdataVOrt[0].getAttributeValue(pricetypename) == null) || (dmdataVOrt[0].getAttributeValue(pricetypename).toString().trim().length() == 0)) { return null; }
			return dmdataVOrt[0];
		}

		if (isExtInvInOrder(order)) {
			sSelect = getSelInvclassKeysByOrder(order) + ", dm_baseprice." + pricetypename;

			sWhere = getInvclassWhereByOrder(order, dataVOCon, pricetypename);
			sFrom = " dm_baseprice inner join bd_invcl invcl on dm_baseprice.pk_invclass=invcl.pk_invcl ";

			sOrderBy = getInvclassKeysByOrder(order);

			sql = new StringBuffer();
			sql.append("select  " + sSelect + " from " + sFrom + " where " + sWhere + " order by " + sOrderBy);

			dmdataVOrt = query(sql);

			if ((null != dmdataVOrt) && (dmdataVOrt.length > 0)) {
				if ((dmdataVOrt[0].getAttributeValue(pricetypename) == null) || (dmdataVOrt[0].getAttributeValue(pricetypename).toString().trim().length() == 0)) { return dmdataVOrt[0]; }
				return dmdataVOrt[0];
			}

			if (iForInv.intValue() != 0) {
				order.setAttributeValue("pkinv", iForInv);
				sSelect = getNormalKeysByOrder(order) + ", " + pricetypename;
				sWhere = getNormalWhereByOrder(order, dataVOCon, pricetypename);
				sFrom = " dm_baseprice inner join bd_invbasdoc  on dm_baseprice.pk_inventory=bd_invbasdoc.pk_invbasdoc  inner join bd_invmandoc on bd_invmandoc.pk_invbasdoc = bd_invbasdoc.pk_invbasdoc ";

				sOrderBy = getNormalOrderKeysByOrder(order);

				sql = new StringBuffer();
				sql.append(" select  " + sSelect + " from " + sFrom + " where " + sWhere + " order by " + sOrderBy);

				dmdataVOrt = query(sql);

				if ((null != dmdataVOrt) && (dmdataVOrt.length > 0)) {
					if ((dmdataVOrt[0].getAttributeValue(pricetypename) == null) || (dmdataVOrt[0].getAttributeValue(pricetypename).toString().trim().length() == 0)) { return null; }
					return dmdataVOrt[0];
				}
			}

		}

		return null;
	}

	protected String getSelInvclassKeysByOrderBatch(DMDataVO orderVO) {
		String[] sKeyNames = orderVO.getAttributeNames();
		String[] sKeys = new String[sKeyNames.length];
		DMVO dmvo = new DMVO(sKeyNames.length);
		for (int i = 0; i < sKeyNames.length; ++i) {
			dmvo.getBodyVOs()[i].setAttributeValue("orderkey", sKeyNames[i]);
			dmvo.getBodyVOs()[i].setAttributeValue("ordernum", orderVO.getAttributeValue(sKeyNames[i]));
		}

		int[] iAsc = new int[1];
		iAsc[0] = 1;
		dmvo.sortByKeys(new String[] {
			"ordernum"
		}, iAsc);

		for (int i = 0; i < dmvo.getBodyVOs().length; ++i) {
			String sTr = translateTransBillKeyToPriceKeyBatch(dmvo.getBodyVOs()[i].getAttributeValue("orderkey").toString());

			if ((sTr != null) && (sTr.trim().length() != 0) && (((sTr.equals("pk_inventory")) || (sTr.equals("pkinvclass"))))) {
				sTr = "len(invcl.invclasscode) as codefl";
			}
			sKeys[i] = sTr;
		}

		String keyswithcoma = "";
		for (int i = 0; i < sKeys.length; ++i) {
			if (sKeys[i].equals("len(invcl.invclasscode) as codefl")) keyswithcoma = keyswithcoma + sKeys[i];
			else {
				keyswithcoma = keyswithcoma + "coalesce(" + sKeys[i] + ",' ') as " + sKeys[i];
			}

			if (i != sKeys.length - 1) {
				keyswithcoma = keyswithcoma + ",";
			}
		}
		return keyswithcoma;
	}

	public UFDouble getTY004(DMVO voDelivBill, UFDouble[] ufdForRow, Boolean bIsnotNeedHandSplit) throws Exception {
		String sMethodName = "nc.bs.dm.dm004.BasepriceDMO.getTY004(DMVO, UFDouble [], Boolean)";

		if (voDelivBill == null) { throw new BusinessException(sMethodName + NCLangResOnserver.getInstance().getStrByID("40140216", "UPP40140216-000098")); }

		return ((UFDouble) voDelivBill.getParentVO().getAttributeValue("dallpacknum"));
	}

	public UFDouble getTY006(DMVO voDelivBill, UFDouble[] ufdForRow, Boolean bIsnotNeedHandSplit) throws Exception {
		String sMethodName = "nc.bs.dm.dm004.BasepriceDMO.getTY004(DMVO, UFDouble [], Boolean)";

		if (voDelivBill == null) { throw new BusinessException(sMethodName + NCLangResOnserver.getInstance().getStrByID("40140216", "UPP40140216-000098")); }

		return DataTypeTool.getUFDouble_NullAs0(voDelivBill.getParentVO().getAttributeValue("dallweight"));
	}

	public UFDouble getTY007(DMVO voDelivBill, UFDouble[] ufdForRow, Boolean bIsnotNeedHandSplit) throws Exception {
		UFDouble dTotal = DataTypeConst.UFDOUBLE_0;

		String sPkdelivorg = (String) voDelivBill.getParentVO().getAttributeValue("pkdelivorg");

		String sPkdelivmode = (String) voDelivBill.getParentVO().getAttributeValue("pkdelivmode");

		String sPktrancust = (String) voDelivBill.getParentVO().getAttributeValue("pktrancust");

		String sPkvehicletype = (String) voDelivBill.getParentVO().getAttributeValue("pkvehicletype");

		DMDataVO[] voaDisplayPack = (DMDataVO[]) (DMDataVO[]) voDelivBill.getParentVO().getAttributeValue("packagebill");

		if ((voaDisplayPack == null) || (voaDisplayPack.length == 0)) { return dTotal; }

		DMDataVO[] voaForCalculate = null;
		if (this._DEBUG) {
			voaForCalculate = test_packvoToSave(voDelivBill, PriceType.iWeight + "", true);

			int iPackLen = voaDisplayPack.length;
			for (int i = 0; i < iPackLen; ++i) {
				voaForCalculate[i].setAttributeValue("dweight", DataTypeTool.getUFDouble_NullAs0(voaForCalculate[i].getAttributeValue("dpacknum")).multiply(DataTypeTool.getUFDouble_NullAs0(voaForCalculate[i].getAttributeValue("doneweight"))));
			}

		} else {
			voaForCalculate = packvoToSave(voDelivBill, PriceType.iWeight + "");
		}

		DMVO voValid = new DMVO();
		voValid.setChildrenVO(voaForCalculate);
		voValid.filterNullValueRow(new String[] {
			"dweight"
		});
		VOTools.filterZeroValueRow(voValid, new String[] {
			"dweight"
		});

		String[] saGroupKey = {
				"pkdelivorg", "pkdelivmode", "pktrancust", "pkfromaddress", "pktoaddress", "ipricetype", "pkvehicletype"
		};

		DMVO[] voaAfterGroup = voValid.getGroupKeyVOs(saGroupKey);
		ArrayList listDataVO = new ArrayList();
		for (int i = 0; i < voaAfterGroup.length; ++i) {
			DMVO dmvoSum = voaAfterGroup[i].getNumSum(saGroupKey, new String[] {
				"dweight"
			});

			dmvoSum.getBodyVOs()[0].setAttributeValue("pkvehicletype", sPkvehicletype);

			listDataVO.add(dmvoSum.getBodyVOs()[0]);
		}
		voaForCalculate = (DMDataVO[]) (DMDataVO[]) listDataVO.toArray(new DMDataVO[listDataVO.size()]);

		DMDataVO dmdvoOrder = new DMDataVO();
		dmdvoOrder.setAttributeValue("pkdelivorg", DataTypeConst.INTEGER_0);
		dmdvoOrder.setAttributeValue("pkdelivmode", DataTypeConst.INTEGER_0);
		dmdvoOrder.setAttributeValue("pktrancust", DataTypeConst.INTEGER_0);

		dmdvoOrder.setAttributeValue("pkpacksort", DataTypeConst.INTEGER_0);

		dmdvoOrder.setAttributeValue("pkvehicletype", DataTypeConst.INTEGER_1);
		dmdvoOrder.setAttributeValue("pkfromaddress", DataTypeConst.INTEGER_1);
		dmdvoOrder.setAttributeValue("pktoaddress", DataTypeConst.INTEGER_2);

		DMDataVO[] voaBasePrice = queryPriceVO(voaForCalculate, dmdvoOrder, "dbaseprice");

		if (voaBasePrice == null) { return dTotal; }

		UFDouble dBasePrice = null;
		UFDouble dWeight = null;
		int iLen = voaForCalculate.length;
		for (int i = 0; i < iLen; ++i) {
			if (voaBasePrice[i] == null) {
				continue;
			}

			dWeight = DataTypeTool.getUFDouble_NullAs0(voaForCalculate[i].getAttributeValue("dweight"));

			dBasePrice = DataTypeTool.getUFDouble_NullAs0(voaBasePrice[i].getAttributeValue("dbaseprice"));

			dTotal = dTotal.add(dWeight.multiply(dBasePrice));
		}

		return dTotal;
	}

	public UFDouble getTrainPrice(DMVO voDelivBill, UFDouble[] ufdForRow, Boolean bIsnotNeedHandSplit) throws Exception {
		UFDouble dTotal = DataTypeConst.UFDOUBLE_0;

		String sPkdelivorg = (String) voDelivBill.getParentVO().getAttributeValue("pkdelivorg");

		String sPkdelivmode = (String) voDelivBill.getParentVO().getAttributeValue("pkdelivmode");

		String sPktrancust = (String) voDelivBill.getParentVO().getAttributeValue("pktrancust");

		DMDataVO[] voaDisplayPack = (DMDataVO[]) (DMDataVO[]) voDelivBill.getParentVO().getAttributeValue("packagebill");

		if ((voaDisplayPack == null) || (voaDisplayPack.length == 0)) { return dTotal; }

		DMDataVO[] voaForCalculate = packvoToSave(voDelivBill, PriceType.iWeight + "");

		DMVO voValid = new DMVO();
		voValid.setChildrenVO(voaForCalculate);
		voValid.filterNullValueRow(new String[] {
			"dweight"
		});
		VOTools.filterZeroValueRow(voValid, new String[] {
			"dweight"
		});

		String[] saGroupKey = {
				"pkdelivorg", "pkdelivmode", "pktrancust", "pkfromaddress", "pktoaddress", "ipricetype"
		};

		DMVO[] voaAfterGroup = voValid.getGroupKeyVOs(saGroupKey);
		ArrayList listDataVO = new ArrayList();
		for (int i = 0; i < voaAfterGroup.length; ++i) {
			DMVO dmvoSum = voaAfterGroup[i].getNumSum(saGroupKey, new String[] {
				"dweight"
			});

			listDataVO.add(dmvoSum.getBodyVOs()[0]);
		}
		voaForCalculate = (DMDataVO[]) (DMDataVO[]) listDataVO.toArray(new DMDataVO[listDataVO.size()]);

		DMDataVO dmdvoOrder = new DMDataVO();
		dmdvoOrder.setAttributeValue("pkdelivorg", DataTypeConst.INTEGER_0);
		dmdvoOrder.setAttributeValue("pkdelivmode", DataTypeConst.INTEGER_0);
		dmdvoOrder.setAttributeValue("pktrancust", DataTypeConst.INTEGER_0);
		dmdvoOrder.setAttributeValue("pkfromaddress", DataTypeConst.INTEGER_1);
		dmdvoOrder.setAttributeValue("pktoaddress", DataTypeConst.INTEGER_2);

		DMDataVO[] voaBasePrice = queryPriceVO(voaForCalculate, dmdvoOrder, "dbaseprice");

		if (voaBasePrice == null) { return dTotal; }

		UFDouble dBasePrice = null;
		UFDouble dWeight = null;
		int iLen = voaForCalculate.length;
		for (int i = 0; i < iLen; ++i) {
			if (voaBasePrice[i] == null) {
				continue;
			}
			dWeight = DataTypeTool.getUFDouble_NullAs0(voaForCalculate[i].getAttributeValue("dweight"));

			dWeight = DMMath.convert5to10(dWeight);
			dBasePrice = DataTypeTool.getUFDouble_NullAs0(voaBasePrice[i].getAttributeValue("dbaseprice"));

			dTotal = dTotal.add(dWeight.multiply(dBasePrice));
		}
		return dTotal;
	}

	public UFDouble getPriceVolumn(DMVO voDelivBill, UFDouble[] ufdForRow, Boolean bIsnotNeedHandSplit) throws Exception {
		UFDouble dTotal = DataTypeConst.UFDOUBLE_0;

		String sPkdelivorg = (String) voDelivBill.getParentVO().getAttributeValue("pkdelivorg");

		String sPkdelivmode = (String) voDelivBill.getParentVO().getAttributeValue("pkdelivmode");

		String sPktrancust = (String) voDelivBill.getParentVO().getAttributeValue("pktrancust");

		String sPkvehicletype = (String) voDelivBill.getParentVO().getAttributeValue("pkvehicletype");

		DMDataVO[] voaDisplayPack = (DMDataVO[]) (DMDataVO[]) voDelivBill.getParentVO().getAttributeValue("packagebill");

		if ((voaDisplayPack == null) || (voaDisplayPack.length == 0)) { return dTotal; }

		DMDataVO[] voaForCalculate = null;
		voaForCalculate = packvoToSave(voDelivBill, PriceType.iVolumn + "");

		DMVO voValid = new DMVO();
		voValid.setChildrenVO(voaForCalculate);
		voValid.filterNullValueRow(new String[] {
			"dvolumn"
		});
		VOTools.filterZeroValueRow(voValid, new String[] {
			"dvolumn"
		});

		String[] saGroupKey = {
				"pkdelivorg", "pkdelivmode", "pktrancust", "pkfromaddress", "pktoaddress", "ipricetype", "pkvehicletype", "pkpacktype"
		};

		DMVO[] voaAfterGroup = voValid.getGroupKeyVOs(saGroupKey);
		ArrayList listDataVO = new ArrayList();
		for (int i = 0; i < voaAfterGroup.length; ++i) {
			DMVO dmvoSum = voaAfterGroup[i].getNumSum(saGroupKey, new String[] {
				"dvolumn"
			});

			dmvoSum.getBodyVOs()[0].setAttributeValue("pkvehicletype", sPkvehicletype);

			listDataVO.add(dmvoSum.getBodyVOs()[0]);
		}
		voaForCalculate = (DMDataVO[]) (DMDataVO[]) listDataVO.toArray(new DMDataVO[listDataVO.size()]);

		DMDataVO dmdvoOrder = new DMDataVO();
		dmdvoOrder.setAttributeValue("pkdelivorg", DataTypeConst.INTEGER_0);
		dmdvoOrder.setAttributeValue("pkdelivmode", DataTypeConst.INTEGER_0);
		dmdvoOrder.setAttributeValue("pktrancust", DataTypeConst.INTEGER_0);

		dmdvoOrder.setAttributeValue("pkvehicletype", DataTypeConst.INTEGER_1);
		dmdvoOrder.setAttributeValue("pkfromaddress", DataTypeConst.INTEGER_1);
		dmdvoOrder.setAttributeValue("pktoaddress", DataTypeConst.INTEGER_2);
		dmdvoOrder.setAttributeValue("pkpacktype", DataTypeConst.INTEGER_2);

		DMDataVO[] voaBasePrice = queryPriceVO(voaForCalculate, dmdvoOrder, "dbaseprice");

		if (voaBasePrice == null) { return dTotal; }

		UFDouble dBasePrice = null;
		UFDouble dVolumn = null;
		int iLen = voaForCalculate.length;
		for (int i = 0; i < iLen; ++i) {
			if (voaBasePrice[i] == null) {
				continue;
			}

			dVolumn = DataTypeTool.getUFDouble_NullAs0(voaForCalculate[i].getAttributeValue("dvolumn"));

			dBasePrice = DataTypeTool.getUFDouble_NullAs0(voaBasePrice[i].getAttributeValue("dbaseprice"));

			dTotal = dTotal.add(dVolumn.multiply(dBasePrice));
		}

		return dTotal;
	}

	public UFDouble getZCJG(DMVO voDeliv, UFDouble[] ufdForRow, Boolean bIsnotNeedHandSplit) throws Exception {
		String sMethodName = "nc.bs.dm.dm004.BasepriceDMO.getZCJG(DMVO, UFDouble [], Boolean)";

		if (voDeliv == null) { throw new BusinessException(sMethodName + NCLangResOnserver.getInstance().getStrByID("40140216", "UPP40140216-000097")); }

		String sPkdelivorg = (String) voDeliv.getParentVO().getAttributeValue("pkdelivorg");

		String sPkdelivmode = (String) voDeliv.getParentVO().getAttributeValue("pkdelivmode");

		String sPktrancust = (String) voDeliv.getParentVO().getAttributeValue("pktrancust");

		String sPkvehicletype = (String) voDeliv.getParentVO().getAttributeValue("pkvehicletype");

		String sPkdelivroute = (String) voDeliv.getParentVO().getAttributeValue("pkdelivroute");

		DMVO voCloneDeliv = (DMVO) voDeliv.clone();
		DMDataVO[] bodyvos = voCloneDeliv.getBodyVOs();

		for (int i = 0; i < bodyvos.length; ++i) {
			bodyvos[i].setAttributeValue("pkdelivorg", sPkdelivorg);
			bodyvos[i].setAttributeValue("pkdelivmode", sPkdelivmode);
			bodyvos[i].setAttributeValue("pktrancust", sPktrancust);
			bodyvos[i].setAttributeValue("pkvehicletype", sPkvehicletype);
			bodyvos[i].setAttributeValue("pkdelivroute", sPkdelivroute);
			bodyvos[i].setAttributeValue("ipricetype", ConstForBasePrice.IPriceType_WholeVehicle);

			bodyvos[i].setAttributeValue("pkfromaddress", bodyvos[i].getAttributeValue("pksendaddress"));

			bodyvos[i].setAttributeValue("pktoaddress", bodyvos[i].getAttributeValue("pkarriveaddress"));
		}

		DMDataVO dmdvoOrder = new DMDataVO();
		dmdvoOrder.setAttributeValue("pkdelivorg", DataTypeConst.INTEGER_0);
		dmdvoOrder.setAttributeValue("pkdelivmode", DataTypeConst.INTEGER_0);
		dmdvoOrder.setAttributeValue("pktrancust", DataTypeConst.INTEGER_0);
		dmdvoOrder.setAttributeValue("pkdelivroute", DataTypeConst.INTEGER_0);
		dmdvoOrder.setAttributeValue("pkvehicletype", DataTypeConst.INTEGER_0);
		dmdvoOrder.setAttributeValue("ipricetype", DataTypeConst.INTEGER_0);

		dmdvoOrder.setAttributeValue("pkfromaddress", DataTypeConst.INTEGER_1);
		dmdvoOrder.setAttributeValue("pktoaddress", DataTypeConst.INTEGER_2);

		UFDouble dTotal = DataTypeConst.UFDOUBLE_0;

		DMDataVO[] voaBasePrice = queryPriceVO(bodyvos, dmdvoOrder, "dbaseprice");

		if (voaBasePrice[0] == null) { return dTotal; }

		if ((null != bodyvos) && (bodyvos.length != 0) && (ufdForRow != null) && (ufdForRow.length != 0)) {
			dTotal = DataTypeTool.getUFDouble_NullAs0(voaBasePrice[0].getAttributeValue("dbaseprice"));

			for (int i = 0; i < ufdForRow.length; ++i) {
				ufdForRow[i] = dTotal.div(1.0D * ufdForRow.length);
			}
		}
		return dTotal;
	}

	protected boolean isExtInvClassInOrder(DMDataVO dataVOPri) {
		return (null != dataVOPri.getAttributeValue("pkinvclass"));
	}

	private DMDataVO[] packvoToSave(DMVO voDelivBill, String sPriceType) throws Exception {
		String sPkdelivorg = (String) voDelivBill.getParentVO().getAttributeValue("pkdelivorg");

		String sPkdelivmode = (String) voDelivBill.getParentVO().getAttributeValue("pkdelivmode");

		String sPktrancust = (String) voDelivBill.getParentVO().getAttributeValue("pktrancust");

		String sPkvehicletype = (String) voDelivBill.getParentVO().getAttributeValue("pkvehicletype");

		DMDataVO[] packvos = (DMDataVO[]) (DMDataVO[]) voDelivBill.getParentVO().getAttributeValue("packagebill");

		HashMap hmapContinueKey = new HashMap();
		hmapContinueKey.put("crownumber", null);
		hmapContinueKey.put("pksendarea", null);
		hmapContinueKey.put("pkarrivearea", null);
		hmapContinueKey.put("vsendarea", null);
		hmapContinueKey.put("varrivearea", null);
		hmapContinueKey.put("pksendaddress", null);
		hmapContinueKey.put("pkarriveaddress", null);
		hmapContinueKey.put("vsendaddress", null);
		hmapContinueKey.put("varriveaddress", null);
		hmapContinueKey.put("pkcust", null);
		hmapContinueKey.put("pkcustbasdoc", null);
		hmapContinueKey.put("custcode", null);
		hmapContinueKey.put("custname", null);
		hmapContinueKey.put("pkdeststockorg", null);
		hmapContinueKey.put("deststockorgcode", null);
		hmapContinueKey.put("deststockorgname", null);
		hmapContinueKey.put("pk_delivbill_h", null);

		Vector vForCalculate = new Vector();
		for (int i = 0; i < packvos.length; ++i) {
			String sPkfromarea = (String) packvos[i].getAttributeValue("pksendarea");

			String sPktoarea = (String) packvos[i].getAttributeValue("pkarrivearea");

			String sPkfromaddress = (String) packvos[i].getAttributeValue("pksendaddress");

			String sPktoaddress = (String) packvos[i].getAttributeValue("pkarriveaddress");

			String sCrownumber = (String) packvos[i].getAttributeValue("crownumber");

			String[] saPackKey = packvos[i].getAttributeNames();

			Hashtable htPackSortKeys = new Hashtable();
			for (int j = 0; j < saPackKey.length; ++j) {
				if ((hmapContinueKey.containsKey(saPackKey[j])) || (saPackKey[j].startsWith("invclasscode_")) || (saPackKey[j].startsWith("invclassname_")) || (saPackKey[j].startsWith("oneweight_")) || (saPackKey[j].startsWith("weight_"))) continue;
				if (saPackKey[j].startsWith("volumn_")) {
					continue;
				}

				String sPackSortKey = null;
				if (saPackKey[j].startsWith("pkinvclass_")) {
					sPackSortKey = saPackKey[j].substring("pkinvclass_".length());
				} else if (saPackKey[j].startsWith("weight_")) sPackSortKey = saPackKey[j].substring("weight_".length());
				else if (saPackKey[j].startsWith("volumn_")) sPackSortKey = saPackKey[j].substring("volumn_".length());
				else if (saPackKey[j].startsWith("oneweight_")) {
					sPackSortKey = saPackKey[j].substring("oneweight_".length());
				} else {
					sPackSortKey = saPackKey[j];
				}

				if (htPackSortKeys.containsKey(sPackSortKey)) continue;
				DMDataVO dmdvoRow = new DMDataVO();
				dmdvoRow.setAttributeValue("pkdelivorg", sPkdelivorg);
				dmdvoRow.setAttributeValue("pkdelivmode", sPkdelivmode);
				dmdvoRow.setAttributeValue("pktrancust", sPktrancust);
				dmdvoRow.setAttributeValue("pkfromarea", sPkfromarea);
				dmdvoRow.setAttributeValue("pktoarea", sPktoarea);
				dmdvoRow.setAttributeValue("pkfromaddress", sPkfromaddress);
				dmdvoRow.setAttributeValue("pktoaddress", sPktoaddress);
				dmdvoRow.setAttributeValue("ipricetype", sPriceType);

				dmdvoRow.setAttributeValue("pkpacktype", sPackSortKey);
				dmdvoRow.setAttributeValue("dpacknum", packvos[i].getAttributeValue(sPackSortKey));

				dmdvoRow.setAttributeValue("doneweight", packvos[i].getAttributeValue("oneweight_" + sPackSortKey));

				dmdvoRow.setAttributeValue("pkinvclass", packvos[i].getAttributeValue("pkinvclass_" + sPackSortKey));

				dmdvoRow.setAttributeValue("dweight", packvos[i].getAttributeValue("weight_" + sPackSortKey));

				dmdvoRow.setAttributeValue("dvolumn", packvos[i].getAttributeValue("volumn_" + sPackSortKey));

				dmdvoRow.setAttributeValue("pkvehicletype", sPkvehicletype);

				htPackSortKeys.put(sPackSortKey, "");
				vForCalculate.add(dmdvoRow);
			}
		}

		DMDataVO[] dmdvosForCalculate = new DMDataVO[vForCalculate.size()];
		vForCalculate.copyInto(dmdvosForCalculate);
		return dmdvosForCalculate;
	}

	private UFDouble parseMnyFrmBatchVOs(DMDataVO[][] voa2BatchPrice, DMDataVO[] voaCondData, String sCondAttriName) throws Exception {
		UFDouble dTotal = DataTypeConst.UFDOUBLE_0;
		if ((voa2BatchPrice == null) || (voa2BatchPrice.length == 0) || (voaCondData == null) || (voaCondData.length == 0) || (DataTypeTool.getString_Trim0LenAsNull(sCondAttriName) == null)) { return dTotal; }

		UFDouble dCondAttriNum = null;

		boolean bFullSum = true;
		UFDouble dPrice = null;
		UFDouble dMoney = null;
		UFDouble dSectionNum = null;
		UFDouble dMaxNum = null;
		UFDouble dMinNum = null;

		UFDouble dUpLmtValue = null;
		UFDouble dOverLmtPrice = null;
		int iLen = voa2BatchPrice.length;

		for (int i = 0; i < iLen; ++i) {
			if (voa2BatchPrice[i] == null) {
				continue;
			}

			Object oUpLmtType = voa2BatchPrice[i][0].getAttributeValue("iuplimittype");

			if (oUpLmtType != null) {
				dUpLmtValue = DataTypeTool.getUFDouble_NullAs0(voa2BatchPrice[i][0].getAttributeValue("nuplimitnum"));

				Integer iUpLmtType = DataTypeTool.getInteger_NullAs(oUpLmtType, ConstForBasePrice.IUpLimitType_Null);

				if (iUpLmtType.compareTo(ConstForBasePrice.IUpLimitType_Weight) == 0) {
					dSectionNum = DataTypeTool.getUFDouble_NullAs0(voaCondData[i].getAttributeValue("dweight"));
				}

				if ((dSectionNum != null) && (dSectionNum.compareTo(dUpLmtValue) > 0)) {
					dOverLmtPrice = DataTypeTool.getUFDouble_NullAs0(voa2BatchPrice[i][0].getAttributeValue("noveruplmtprice"));

					dTotal = dTotal.add(dSectionNum.multiply(dOverLmtPrice));
				}

			} else {
				bFullSum = DataTypeTool.getInteger_NullAs(voa2BatchPrice[i][0].getAttributeValue("ilevelbndjudge"), ConstForLevelHead.ILevelBndJudge_FullSum).equals(ConstForLevelHead.ILevelBndJudge_FullSum);

				dCondAttriNum = DataTypeTool.getUFDouble_NullAs0(voaCondData[i].getAttributeValue(sCondAttriName));

				int iJLen = voa2BatchPrice[i].length;
				if (bFullSum) {
					iJLen = 1;
				}

				for (int j = 0; j < iJLen; ++j) {
					if (bFullSum) {
						dSectionNum = dCondAttriNum;
					} else {
						dMinNum = DataTypeTool.getUFDouble_NullAs0(voa2BatchPrice[i][j].getAttributeValue("dminnum"));

						dMaxNum = DataTypeTool.getUFDouble_NullAs0(voa2BatchPrice[i][j].getAttributeValue("dmaxnum"));

						if (dMaxNum.compareTo(dCondAttriNum) >= 0) {
							dSectionNum = dCondAttriNum.sub(dMinNum);
						} else {
							dSectionNum = dMaxNum.sub(dMinNum);
						}

					}

					dPrice = DataTypeTool.getUFDouble_ValueAsValue(voa2BatchPrice[i][j].getAttributeValue("dprice"));

					if (dPrice == null) {
						dMoney = DataTypeTool.getUFDouble_ValueAsValue(voa2BatchPrice[i][j].getAttributeValue("dmoney"));

						dTotal = dTotal.add(dMoney);
						dSectionNum = null;
					} else {
						dTotal = dTotal.add(dSectionNum.multiply(dPrice));
						dSectionNum = null;
					}
				}
			}
		}
		return dTotal;
	}

	protected void queryPriceBatch(DMDataVO[] dataVOCon, DMDataVO dataVOOrder, String pricetypename) throws Exception {
		if ((null == dataVOOrder) || (null == dataVOOrder.getAttributeNames()) || (null == dataVOCon) || (dataVOCon.length == 0)) { return; }
		DMDataVO[] dataVOrt = new DMDataVO[dataVOCon.length];
		Hashtable ht = new Hashtable();

		for (int i = 0; i < dataVOCon.length; ++i) {
			dataVOCon[i].setAttributeValue(pricetypename, null);
		}

		for (int i = 0; i < dataVOCon.length; ++i) {
			if (null != dataVOCon[i].getAttributeValue(pricetypename)) continue;
			String sKey = getNormalWhereByOrderBatch(dataVOOrder, dataVOCon[i], pricetypename) + getInvclassWhereByOrderBatch(dataVOOrder, dataVOCon[i], pricetypename);

			String sFormulaCode = (String) ht.get(sKey);
			if (sFormulaCode == null) {
				sFormulaCode = getPriceBatch(dataVOCon[i], dataVOOrder, pricetypename);

				ht.put(sKey, sFormulaCode);
			}
			dataVOCon[i].setAttributeValue(pricetypename, sFormulaCode);
		}
	}

	protected DMDataVO[][] queryPriceBatchVO(DMDataVO[] dataVOCon, DMDataVO dataVOOrder, String pricetypename) throws Exception {
		if ((null == dataVOOrder) || (null == dataVOOrder.getAttributeNames()) || (null == dataVOCon) || (dataVOCon.length == 0)) { return ((DMDataVO[][]) null); }

		DMDataVO[] dataVOrt = new DMDataVO[dataVOCon.length];
		HashMap hmapRetVO = new HashMap();

		int iLen = dataVOCon.length;
		DMDataVO[][] voaData = new DMDataVO[iLen][];
		for (int i = 0; i < iLen; ++i) {
			String sKey = getNormalWhereByOrderBatch(dataVOOrder, dataVOCon[i], pricetypename) + getInvclassWhereByOrderBatch(dataVOOrder, dataVOCon[i], pricetypename);

			Object oValue = hmapRetVO.get(sKey);
			if (oValue == null) {
				oValue = getPriceBatchVOs(dataVOCon[i], dataVOOrder, pricetypename);

				if (oValue == null) hmapRetVO.put(sKey, "");
				else {
					hmapRetVO.put(sKey, (DMDataVO[]) (DMDataVO[]) oValue);
				}

			}

			if (oValue instanceof String) voaData[i] = null;
			else {
				voaData[i] = ((DMDataVO[]) (DMDataVO[]) oValue);
			}
		}

		return voaData;
	}

	protected DMDataVO[] queryPriceVO(DMDataVO[] dataVOCon, DMDataVO dataVOOrder, String pricetypename) throws Exception {
		if ((null == dataVOOrder) || (null == dataVOOrder.getAttributeNames()) || (null == dataVOCon) || (dataVOCon.length == 0)) { return null; }

		DMDataVO[] dataVOrt = new DMDataVO[dataVOCon.length];
		HashMap hmapRetVO = new HashMap();

		int iLen = dataVOCon.length;
		DMDataVO[] voaData = new DMDataVO[iLen];
		for (int i = 0; i < iLen; ++i) {
			String sKey = getNormalWhereByOrder(dataVOOrder, dataVOCon[i], pricetypename) + getInvclassWhereByOrder(dataVOOrder, dataVOCon[i], pricetypename);

			Object oValue = hmapRetVO.get(sKey);
			if (oValue == null) {
				oValue = getPriceVO(dataVOCon[i], dataVOOrder, pricetypename);
				if (oValue == null) hmapRetVO.put(sKey, "");
				else {
					hmapRetVO.put(sKey, (DMDataVO) oValue);
				}

			}

			if (oValue instanceof String) voaData[i] = null;
			else {
				voaData[i] = ((DMDataVO) oValue);
			}
		}

		return voaData;
	}

	private DMDataVO[] test_packvoToSave(DMVO vo, String sPriceType, boolean bBasePrice) throws Exception {
		String pkdelivorg = (String) vo.getParentVO().getAttributeValue("pkdelivorg");

		String pkdelivmode = (String) vo.getParentVO().getAttributeValue("pkdelivmode");

		String pktrancust = (String) vo.getParentVO().getAttributeValue("pktrancust");

		DMDataVO[] packvos = (DMDataVO[]) (DMDataVO[]) vo.getParentVO().getAttributeValue("packagebill");

		Vector vForCalculate = new Vector();
		for (int i = 0; i < packvos.length; ++i) {
			String[] keys = packvos[i].getAttributeNames();
			String pkfromarea = (String) packvos[i].getAttributeValue("pksendarea");

			String pktoarea = (String) packvos[i].getAttributeValue("pkarrivearea");

			String pkfromaddress = (String) packvos[i].getAttributeValue("pksendaddress");

			String pktoaddress = (String) packvos[i].getAttributeValue("pkarriveaddress");

			String sCrownumber = (String) packvos[i].getAttributeValue("crownumber");

			String sPkPackSort = (String) packvos[i].getAttributeValue("pk_packsort");

			DMDataVO dmdvoRow = (DMDataVO) packvos[i].clone();
			dmdvoRow.setAttributeValue("pkdelivorg", pkdelivorg);
			dmdvoRow.setAttributeValue("pkdelivmode", pkdelivmode);

			dmdvoRow.setAttributeValue("pktrancust", pktrancust);
			dmdvoRow.setAttributeValue("pkfromarea", pkfromarea);
			dmdvoRow.setAttributeValue("pktoarea", pktoarea);
			dmdvoRow.setAttributeValue("pkfromaddress", pkfromaddress);
			dmdvoRow.setAttributeValue("pktoaddress", pktoaddress);
			dmdvoRow.setAttributeValue("ipricetype", sPriceType);
			dmdvoRow.setAttributeValue("pkpacksort", sPkPackSort);

			vForCalculate.add(dmdvoRow);
		}
		DMDataVO[] dmdvosForCalculate = new DMDataVO[vForCalculate.size()];
		vForCalculate.copyInto(dmdvosForCalculate);
		return dmdvosForCalculate;
	}

	protected String translateTransBillKeyToPriceKeyBatch(String sTransBillKey) {
		String sHTableName = "dm_quantitylevel_h.";

		DMDataVO dmdvo = new DMDataVO();

		dmdvo.setAttributeValue("pkdelivorg", sHTableName + "pkdelivorg");
		dmdvo.setAttributeValue("pkdelivmode", sHTableName + "pksendtype");
		dmdvo.setAttributeValue("pktrancust", sHTableName + "pktranscust");

		dmdvo.setAttributeValue("pksrccalbodyar", sHTableName + "pkfromarea");
		dmdvo.setAttributeValue("pkarrivearea", sHTableName + "pktoarea");

		dmdvo.setAttributeValue("pkinv", "pk_inventory");
		dmdvo.setAttributeValue("pkinvclass", "pkinvclass");

		dmdvo.setAttributeValue("pkvehicletype", sHTableName + "pkvehicletype");
		dmdvo.setAttributeValue("pkdelivroute", sHTableName + "pkroute");

		dmdvo.setAttributeValue("pkfromaddress", sHTableName + "pkfromaddress");
		dmdvo.setAttributeValue("pktoaddress", sHTableName + "pktoaddress");

		dmdvo.setAttributeValue("pkpacksort", sHTableName + "pkpacktype");

		if (null != dmdvo.getAttributeValue(sTransBillKey)) { return ((String) dmdvo.getAttributeValue(sTransBillKey)); }
		return sTransBillKey;
	}

	public DmBasepriceVO[] query(String sWhereClause) throws BusinessException {
		StringBuffer sb = new StringBuffer();

		// sb.append(" select * from dm_baseprice ");
		sb.append("	SELECT dm_baseprice.pk_basicprice, dm_baseprice.pkdelivorg, ").append("	    dm_baseprice.pk_transcust, dm_baseprice.pk_sendtype, ").append("	    dm_baseprice.pk_vehicletype, dm_baseprice.pk_transcontainer, ").append("	    dm_baseprice.pk_invclass, dm_baseprice.pk_inventory, dm_baseprice.vpriceunit, ").append("	    dm_baseprice.dbaseprice, dm_baseprice.ts,dm_baseprice.pkfromarea ,a.areaclname as fromarea,").append("	    dm_baseprice.pktoarea   ,b.areaclname as toarea,").append("	    dm_baseprice.pkpacksort, dm_packsort.packsortname as packsort, ")

		.append("	    dm_baseprice.pkroute, ")
		// 整车价格,路线

		// 承运商
		.append("	    bd_cubasdoc.custcode AS vtranscustcode, ").append("	    bd_cubasdoc.custname AS vtranscustname,")
		// 车型
		.append("		  dm_vehicletype.vvhcltypecode AS vvhcltypecode, ").append("		  dm_vehicletype.vvhcltypename AS vvhcltypename,")
		// 运输仓
		.append("	  	dm_transcontainer.vclasscode AS vclasscode, ").append("	  	dm_transcontainer.vclassname AS vclassname,")
		// 发运方式
		.append("			bd_sendtype.sendcode AS vsendtypecode, ").append("		  bd_sendtype.sendname AS vsendtypename, ")
		// 存货分类
		.append("	    bd_invcl.invclasscode AS vinvclasscode, ").append("	    bd_invcl.invclassname AS vinvclassname,")
		// 存货
		.append("	    bd_invbasdoc.invcode AS vinvcode,").append("	    bd_invbasdoc.invname AS vinvname,")
		// 发运组织
		.append("	    bd_delivorg.vdoname AS vdoname, ")

		.append("dm_baseprice.pkfromaddress, dm_baseprice.pktoaddress,  ")
		// 发货地点主键 到货地点主键
		//eric 2012-08-09 含税单价 税率 生效日期 失效日期
		.append("dm_baseprice.taxprice,dm_baseprice.rate,dm_baseprice.effectdate,dm_baseprice.expirationdate,").append("dm_baseprice.ipricetype,dm_baseprice.bsltfrmlevel,dm_baseprice.iuplimittype,dm_baseprice.nuplimitnum,dm_baseprice.noveruplmtprice  ")
		//*****************add by yhj 2014-02-24 START************************/
		.append(",dm_baseprice.memo  ")
		
		//*****************add by yhj 2014-02-24 END************************/
		// 表名
		.append("	FROM dm_baseprice LEFT OUTER JOIN")
		// 关联承运商
		.append("	    dm_trancust ON ").append("	    dm_baseprice.pk_transcust = dm_trancust.pk_trancust LEFT OUTER JOIN").append("	    bd_cumandoc ON ").append("	    dm_trancust.pkcusmandoc = bd_cumandoc.pk_cumandoc LEFT OUTER JOIN").append("	    bd_cubasdoc ON ").append("	    bd_cumandoc.pk_cubasdoc = bd_cubasdoc.pk_cubasdoc LEFT OUTER JOIN")
		// 车型
		.append("	  	dm_vehicletype ON ").append("	  	dm_baseprice.pk_vehicletype = dm_vehicletype.pk_vehicletype LEFT OUTER JOIN")
		// 运输仓
		.append("	  	dm_transcontainer ON ").append("	  	dm_baseprice.pk_transcontainer = dm_transcontainer.pk_transcontainer LEFT OUTER JOIN")
		// 发运方式
		.append("		  bd_sendtype ON ").append("	 	  dm_baseprice.pk_sendtype = bd_sendtype.pk_sendtype LEFT OUTER JOIN")
		// 存货分类
		.append("	    bd_invcl ON ").append("	    dm_baseprice.pk_invclass = bd_invcl.pk_invcl LEFT OUTER JOIN")
		// 存货
		.append("	    bd_invbasdoc ON ").append("	    dm_baseprice.pk_inventory = bd_invbasdoc.pk_invbasdoc  LEFT OUTER JOIN")
		// 包装分类
		.append("	    dm_packsort ON ").append("	    dm_baseprice.pkpacksort = dm_packsort.pk_packsort  LEFT OUTER JOIN")
		// 发货站
		.append("	    bd_areacl as a ON ").append("	    dm_baseprice.pkfromarea = a.pk_areacl  LEFT OUTER JOIN")
		// 到货站
		.append("	    bd_areacl as b ON ").append("	    dm_baseprice.pktoarea = b.pk_areacl  LEFT OUTER JOIN")
		// 发运组织
		.append("	    bd_delivorg ON ").append("	    dm_baseprice.pkdelivorg = bd_delivorg.pk_delivorg where dm_baseprice.dr = 0  ");

		if (sWhereClause != null && sWhereClause.trim().length() != 0) {
			sb.append(" and " + sWhereClause);
		}

		DmBasepriceVO[] dvo = null;
		try {
			SmartDMO sDMO = new SmartDMO();
			dvo = (DmBasepriceVO[]) sDMO.selectBySql2(sb.toString(), DmBasepriceVO.class);
		} catch (Exception e) {
			throw new BusinessException(e);
		}
		return dvo;
	}

	public DmBasepriceVO[] baseprice_save(DmBasepriceVO[] dvo) throws BusinessException {
		if ((dvo == null) || (dvo.length == 0)) { return dvo; }
		String pk_corp = (String) dvo[0].getAttributeValue("pkcorp");
		ClientLink cl = new ClientLink(pk_corp, null, null, null, null, null, null, null, null, false, null, null, null);
		try {
			RichDMO rDMO = new RichDMO();
			for (int i = 0; i < dvo.length; ++i)
				rDMO.model1_EditBatch(cl, new DmBasepriceVO[] {
					dvo[i]
				}, true);
		} catch (Exception e) {
			throw new BusinessException(e);
		}
		return dvo;
	}

	public DmBasepriceAggVO baseprice_save(DmBasepriceAggVO vo) throws SQLException, nc.bs.pub.SystemException, NamingException {
		DmBasepriceVO[] dvo = (DmBasepriceVO[]) (DmBasepriceVO[]) vo.getChildrenVO();
		String pk_corp = (String) dvo[0].getAttributeValue("pkcorp");
		ClientLink cl = new ClientLink(pk_corp, null, null, null, null, null, null, null, null, false, null, null, null);
		RichDMO rDMO = new RichDMO();
		rDMO.model2_EditBatch(cl, new DmBasepriceAggVO[] {
			vo
		}, null, null, null, true);
		return vo;
	}

	public DmBasepriceVO[] queryDB(StringBuffer Sql) throws BusinessException {
		DmBasepriceVO[] dvo = null;
		try {
			SmartDMO sDMO = new SmartDMO();
			dvo = (DmBasepriceVO[]) (DmBasepriceVO[]) sDMO.selectBySql2(Sql.toString(), DmBasepriceVO.class);
		} catch (Exception e) {
			throw new BusinessException(e);
		}
		return dvo;
	}

	public DmFeeitempriceVO[] queryFee(String sFromWhere) throws BusinessException {
		StringBuffer sb = new StringBuffer();
		sb.append("select pk_feeitemprice,pkdelivorg,pk_sendtype,pk_transcust,pk_feeitem,dfeeitemprice,vnote,ts,dr ");
		sb.append(sFromWhere);
		DmFeeitempriceVO[] ddvos = null;
		try {
			SmartDMO sDMO = new SmartDMO();
			ddvos = (DmFeeitempriceVO[]) (DmFeeitempriceVO[]) sDMO.selectBySql2(sb.toString(), DmFeeitempriceVO.class);
		} catch (Exception e) {
			throw new BusinessException(e);
		}
		return ddvos;
	}

	public DmFeeitempriceAggVO saveFeeItemPrice(DmFeeitempriceAggVO vo) throws nc.bs.pub.SystemException, NamingException, SQLException {
		DmFeeitempriceVO[] dvo = (DmFeeitempriceVO[]) (DmFeeitempriceVO[]) vo.getChildrenVO();
		String pk_corp = (String) dvo[0].getAttributeValue("pkcorp");
		ClientLink cl = new ClientLink(pk_corp, null, null, null, null, null, null, null, null, false, null, null, null);
		RichDMO rDMO = new RichDMO();
		for (int i = 0; i < dvo.length; ++i) {
			rDMO.model1_EditBatch(cl, new DmFeeitempriceVO[] {
				dvo[i]
			}, true);
		}
		vo.setChildrenVO(dvo);
		return vo;
	}
}