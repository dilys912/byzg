package nc.ui.po.query;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.FramePanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.querymodel.QEQueryDlg;
import nc.ui.pub.report.ReportItem;
import nc.ui.pub.rino.DbUtil;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.querymodel.QueryModelDef;

import com.borland.dx.dataset.StorageDataSet;

@SuppressWarnings("serial")
public class PrayReportUI extends nc.ui.pub.querymodel.QEReportBase {

	public PrayReportUI(FramePanel fp) {
		super(fp);
		getReportBase().getBody_Item("DEF1").setWidth(1000);
		ReportItem[] items = getReportBase().getBody_Items();
		super.getReportBase().setBody_Items(items);
		getReportBase().hideColumn(new String[] {
				"CPRAYBILL_BID", "PK_INVBASDOC", "PK_INVMANDOC", "NUM1", "NUM7", "NUM10", "NUM11", "NUM12", "NUM13", "NUM14", "NUM15", "NUM16", "NUM17", "NUM18", "NUM19", "NUM20", "NUM99", "NUM98", "NUM97", "NUM96", "NUM95", "NUM94", "NUM93", "NUM92", "NUM91", "NUM90", "NUM88", "NUM87", "NUM86", "NUM85", "NUM84", "NUM83", "NUM82", "NUM81", "NUM80", "DEF2", "DEF3", "DEF4", "DEF5", "DEF6", "DEF7", "DEF8", "DEF9", "DEF10", "DEF11", "DEF12", "DEF13", "DEF14", "DEF15", "DEF16", "DEF17", "DEF18", "DEF19", "DEF20"
		});
	}

	@SuppressWarnings({
			"rawtypes", "unchecked"
	})
	@Override
	protected void onQuery() throws Exception {
		String m_queryId = getParam_Query();
		if (m_queryId == null) {
			MessageDialog.showWarningDlg(this, null, "未注册查询ID参数");
			return;
		}
		String m_dsNameForDef = getParam_Dsname();
		if (m_dsNameForDef == null) {
			MessageDialog.showWarningDlg(this, null, "未注册查询定义数据源参数");
			return;
		}
		QueryModelDef qmd_origin = getQueryModelDef(m_queryId, m_dsNameForDef);
		if (qmd_origin == null) {
			MessageDialog.showWarningDlg(this, null, "未找到查询定义");
			return;
		}
		QueryModelDef qmd = qmd_origin.cloneQmd();
		Hashtable hashParam = getHashParam(qmd, m_dsNameForDef);
		if (hashParam == null) return;
		System.out.println("execute query.....................");
		String pk_corp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
		String pk_calbody = (String) DbUtil.getDMO().getObject("select pk_calbody from bd_calbody where pk_corp='" + pk_corp + "' and bodycode='01'");
		QEQueryDlg dlg = (QEQueryDlg) getQryDlg();
		//		String dpraydate1 = new UFDate().toString();
		//		ConditionVO[] conditionVOs = dlg.getConditionVOsByFieldCode("po_praybill.dpraydate1");
		//		if (conditionVOs != null) {
		//			ConditionVO conditionVOCurr = conditionVOs[0];
		//			if (conditionVOCurr != null && conditionVOCurr.getValue() != null && !"".equals(conditionVOCurr.getValue())) {
		//				dpraydate1 = conditionVOCurr.getValue();
		//			}
		//		}
		String where = dlg.getWhereSQL();
		where = where.replaceAll("po_praybill.dpraydate1", "po_praybill.dpraydate").replaceAll("po_praybill.dpraydate2", "po_praybill.dpraydate");
		StringBuilder sql = new StringBuilder();
		sql.append("select po_praybill_b.cpraybill_bid,");
		sql.append("        po_praybill_b.cpraybillid,");
		sql.append("        bd_invcl.invclassname,");
		sql.append("        bd_invbasdoc.pk_invbasdoc,");
		sql.append("        bd_invmandoc.pk_invmandoc,");
		sql.append("        bd_invbasdoc.invcode,");
		sql.append("        bd_invbasdoc.invname,");
		sql.append("        po_praybill.vpraycode as pordercode,");
		sql.append("        bd_psndoc.psnname as ppsnname,");
		sql.append("        bd_deptdoc.deptname as pdeptname,");
		sql.append("        bd_invmandoc.def4 as llh,");
		sql.append("        bd_invbasdoc.invspec,");
		sql.append("        bd_measdoc.measname,");
		sql.append("        bd_produce.maxstornum,");
		sql.append("        bd_produce.zdhd");
		sql.append(" from");
		sql.append("   po_praybill_b,");
		sql.append("   po_praybill,");
		sql.append("   bd_invbasdoc,");
		sql.append("   bd_invmandoc,");
		sql.append("   bd_corp,");
		sql.append("   bd_deptdoc,");
		sql.append("   bd_produce,");
		sql.append("   bd_psndoc,");
		sql.append("   bd_measdoc,");
		sql.append("   bd_invcl");
		sql.append(" where po_praybill_b.cpraybillid=po_praybill.cpraybillid");
		sql.append("       and po_praybill_b.cbaseid=bd_invbasdoc.pk_invbasdoc");
		sql.append("       and po_praybill.pk_corp=bd_corp.pk_corp");
		sql.append("       and po_praybill.cdeptid=bd_deptdoc.pk_deptdoc(+)");
		sql.append("       and po_praybill.cpraypsn=bd_psndoc.pk_psndoc(+)");
		sql.append("       and bd_invbasdoc.pk_invbasdoc=bd_invmandoc.pk_invbasdoc");
		sql.append("       and bd_invbasdoc.pk_measdoc=bd_measdoc.pk_measdoc(+)");
		sql.append("       and bd_invbasdoc.pk_invcl=bd_invcl.pk_invcl");
		sql.append("       and bd_invmandoc.pk_invmandoc=bd_produce.pk_invmandoc(+)");
		sql.append("	   and bd_corp.pk_corp=bd_invmandoc.pk_corp");
		sql.append("       and nvl(po_praybill_b.dr,0)=0 and nvl(po_praybill.dr,0)=0");
		sql.append("       and nvl(bd_invbasdoc.dr,0)=0 and nvl(bd_invmandoc.dr,0)=0");
		sql.append("       and nvl(bd_corp.dr,0)=0 and nvl(bd_deptdoc.dr,0)=0");
		sql.append("       and nvl(bd_produce.dr,0)=0 and nvl(bd_psndoc.dr,0)=0");
		sql.append("       and nvl(bd_measdoc.dr,0)=0 and nvl(bd_invcl.dr,0)=0");
		sql.append(" 	   and ").append(where);
		sql.append("	   and po_praybill.pk_corp='" + pk_corp + "'");
		List<PrayReportVO> pos = (List<PrayReportVO>) DbUtil.getDMO().getBeanList(sql.toString(), PrayReportVO.class);
		if (pos == null || pos.size() <= 0) return;

		// ======================================= start 根据请购单每一个表体行查询下游所有单据的数量
		for (int i = 0; i < pos.size(); i++) {
			PrayReportVO vo = pos.get(i);
			String cpraybill_bid = vo.getCpraybill_bid();
			//=============== start 请购单未审批数量
			String sql99 = new StringBuilder().append("select sum(npraynum)").append(" from po_praybill_b,po_praybill").append(" where po_praybill_b.cpraybillid=po_praybill.cpraybillid").append(" and po_praybill.ibillstatus in (0,2)").append(" and po_praybill_b.cpraybill_bid='" + cpraybill_bid + "'").toString();
			Object obj99 = DbUtil.getDMO().getObject(sql99);
			UFDouble num99 = new UFDouble(obj99 == null ? "0" : obj99.toString()).setScale(2, UFDouble.ROUND_HALF_UP);
			vo.setNum99(num99);
			//=============== start 请购单已审批数量
			String sql98 = new StringBuilder().append("select sum(npraynum)").append(" from po_praybill_b,po_praybill").append(" where po_praybill_b.cpraybillid=po_praybill.cpraybillid").append(" and po_praybill.ibillstatus in (3)").append(" and po_praybill_b.cpraybill_bid='" + cpraybill_bid + "'").toString();
			Object obj98 = DbUtil.getDMO().getObject(sql98);
			UFDouble num98 = new UFDouble(obj98 == null ? "0" : obj98.toString()).setScale(2, UFDouble.ROUND_HALF_UP);
			vo.setNum98(num98);
			//=============== start 采购订单未审批数量
			String sql97 = new StringBuilder().append("select sum(po_order_b.nordernum)").append(" from po_order_b,po_order").append(" where po_order_b.corderid=po_order.corderid").append(" and po_order.forderstatus in (0,2)").append(" and po_order_b.csourcerowid='" + cpraybill_bid + "'").toString();
			Object obj97 = DbUtil.getDMO().getObject(sql97);
			UFDouble num97 = new UFDouble(obj97 == null ? "0" : obj97.toString()).setScale(2, UFDouble.ROUND_HALF_UP);
			vo.setNum97(num97);
			//=============== start 采购订单已审批数量
			String sql96 = new StringBuilder().append("select sum(po_order_b.nordernum)").append(" from po_order_b,po_order").append(" where po_order_b.corderid=po_order.corderid").append(" and po_order.forderstatus in (3)").append(" and po_order_b.csourcerowid='" + cpraybill_bid + "'").toString();
			Object obj96 = DbUtil.getDMO().getObject(sql96);
			UFDouble num96 = new UFDouble(obj96 == null ? "0" : obj96.toString()).setScale(2, UFDouble.ROUND_HALF_UP);
			vo.setNum96(num96);
			//=============== start 到货单未检验数量
			String sql95 = new StringBuilder().append("select sum(po_arriveorder_b.narrvnum)").append(" from po_arriveorder_b,po_arriveorder,po_order_b").append(" where po_arriveorder_b.carriveorderid=po_arriveorder.carriveorderid").append(" and po_arriveorder_b.corder_bid=po_order_b.corder_bid").append(" and po_order_b.csourcerowid='" + cpraybill_bid + "'").toString();
			Object obj95 = DbUtil.getDMO().getObject(sql95);
			UFDouble num95 = new UFDouble(obj95 == null ? "0" : obj95.toString()).setScale(2, UFDouble.ROUND_HALF_UP);
			vo.setNum95(num95);
			//=============== start 到货单已检验数量
			String sql94 = new StringBuilder().append("select sum(po_arriveorder_b.nelignum)").append(" from po_arriveorder_b,po_arriveorder,po_order_b").append(" where po_arriveorder_b.carriveorderid=po_arriveorder.carriveorderid").append(" and po_arriveorder_b.corder_bid=po_order_b.corder_bid").append(" and po_order_b.csourcerowid='" + cpraybill_bid + "'").toString();
			Object obj94 = DbUtil.getDMO().getObject(sql94);
			UFDouble num94 = new UFDouble(obj94 == null ? "0" : obj94.toString()).setScale(2, UFDouble.ROUND_HALF_UP);
			vo.setNum94(num94);
			//=============== start 库存采购入库单数量
			String sql93 = new StringBuilder().append("select sum(ic_general_b.ninnum)").append(" from").append(" ic_general_b,ic_general_h,").append(" po_arriveorder_b,po_order_b").append(" where").append(" ic_general_b.cgeneralhid=ic_general_h.cgeneralhid").append(" and ic_general_b.csourcebillbid=po_arriveorder_b.carriveorder_bid").append(" and po_arriveorder_b.corder_bid=po_order_b.corder_bid").append(" and po_order_b.csourcerowid='" + cpraybill_bid + "'").toString();
			Object obj93 = DbUtil.getDMO().getObject(sql93);
			UFDouble num93 = new UFDouble(obj93 == null ? "0" : obj93.toString()).setScale(2, UFDouble.ROUND_HALF_UP);
			vo.setNum93(num93);
		}
		// ======================================= end 根据请购单每一个表体行查询下游所有单据的数量

		// ======================================= start 按照维度进行汇总
		String[] collKeyFields = new String[] {
				"invcode", "pordercode"
		};
		String[] collValueFields = new String[] {
				"num99", "num98", "num97", "num96", "num95", "num94", "num93"
		};
		Map<String, PrayReportVO> collVOMap = new HashMap<String, PrayReportVO>();
		for (PrayReportVO vo : pos) {
			String key = vo.getAttributeValue(collKeyFields[0]) == null ? "" : vo.getAttributeValue(collKeyFields[0]).toString();
			for (int i = 1; i < collKeyFields.length; i++) {
				key += vo.getAttributeValue(collKeyFields[i]) == null ? "" : vo.getAttributeValue(collKeyFields[i]).toString();
			}
			PrayReportVO collVO;
			if (collVOMap.containsKey(key)) {
				collVO = collVOMap.get(key);
				for (String collField : collValueFields) {
					collVO.setAttributeValue(collField, (collVO.getAttributeValue(collField) == null ? new UFDouble(0) : (UFDouble) collVO.getAttributeValue(collField)).add(vo.getAttributeValue(collField) == null ? new UFDouble(0) : (UFDouble) vo.getAttributeValue(collField)));
				}
			} else {
				collVO = (PrayReportVO) vo.clone();
			}
			collVOMap.put(key, collVO);
		}
		// ======================================= end 按照维度进行汇总

		// ======================================= start 根据存货编码查询现存量,已冻结数量,消耗量
		PrayReportVO[] collVOs = collVOMap.values().toArray(new PrayReportVO[] {});
		for (PrayReportVO vo : collVOs) {
			// 查询现存量
			String sql89 = "select sum(nonhandnum) from ic_onhandnum where cinvbasid='" + vo.getPk_invbasdoc() + "' and pk_corp='" + pk_corp + "';";
			Object obj89 = DbUtil.getDMO().getObject(sql89);
			UFDouble num89 = new UFDouble(obj89 == null ? "0" : obj89.toString()).setScale(2, UFDouble.ROUND_HALF_UP);
			vo.setNum89(num89);

			// 查询已冻结数量
			String sql6 = "select sum(nvl(nfreezenum,0)) as rst from ic_freeze where nvl(dr, 0) = 0 and pk_corp = '" + pk_corp + "' and cinvbasid = '" + vo.getPk_invbasdoc() + "' and cthawpersonid is null and cspaceid is not null";
			Object obj6 = DbUtil.getDMO().getObject(sql6);
			UFDouble num6 = new UFDouble(obj6 == null ? "0" : obj6.toString()).setScale(2, UFDouble.ROUND_HALF_UP);
			vo.setNum6(num6);

			// 查询消耗量 def1
			UFDate currDate = new UFDate(System.currentTimeMillis());
			UFDate fromDate = new UFDate((currDate.getYear() - 1) + "-" + currDate.getStrMonth() + "-01");
			String sqlDef1 = new StringBuilder().append("select substr(a.dbilldate, 1, 7) as fld1, nvl(sum(b.noutnum), 0) as fld2").append("   from ic_general_h a").append("   left join ic_general_b b on a.cgeneralhid = b.cgeneralhid").append("   left join bd_invbasdoc c on b.cinvbasid = c.pk_invbasdoc").append("  where nvl(a.dr, 0) = 0 and nvl(b.dr,0)=0").append("    and a.pk_corp ='" + pk_corp + "'").append("    and a.pk_calbody = '" + pk_calbody + "'").append("    and a.cbilltypecode in ('4D', '4I', '4F', '4C', '4G', '4Y')").append("    and a.dbilldate >= '" + fromDate + "'").append("    and c.pk_invbasdoc = '" + vo.getPk_invbasdoc() + "'").append("  group by c.invcode, substr(a.dbilldate, 1, 7)").append("  order by c.invcode, substr(a.dbilldate, 1, 7)").toString();
			List<Map<String, Object>> mapList = DbUtil.getDMO().getMapList(sqlDef1.toString());
			StringBuilder def1 = new StringBuilder();
			for (int i = 0; i < mapList.size(); i++) {
				Map<String, Object> map = mapList.get(i);
				String fld1 = (String) map.get("fld1");
				Object fld2 = map.get("fld2");
				if (fld1.equals(fromDate.getYear() + "-" + fromDate.getStrMonth())) {
					def1.insert(0, "本月:" + (fld2 == null ? "0" : fld2.toString()) + " ");
				} else {
					fromDate = fromDate.getDateBefore(1);
					for (int j = 0; j < 12; j++) {
						def1.append(fromDate.getStrMonth()).append(":").append(fld2 == null ? "0" : fld2.toString());
						fromDate = fromDate.getDateBefore(UFDate.getDaysMonth(fromDate.getYear(), fromDate.getMonth()));
					}
				}
			}
			vo.setDef1(def1.toString());
		}
		// ======================================= end 根据存货编码查询现存量,已冻结数量,消耗量

		// ======================================= start 数量字段参与计算
		for (PrayReportVO vo : collVOs) {
			vo.setNum2(vo.getNum99()); // 已申购未审批 2=98
			vo.setNum3(vo.getNum98().sub(vo.getNum97().add(vo.getNum96()))); //已审批未订购 3=98-(97+96)
			vo.setNum4(vo.getNum97().add(vo.getNum96().sub(vo.getNum95()))); // 已订购未到货 4=97+96-95
			vo.setNum5(vo.getNum95()); // 已到货未检验 5=95
			vo.setNum8(vo.getNum94().add(vo.getNum95())); // 已到货数量 8=94+95
			vo.setNum9(vo.getNum93()); // 已入库数量 9=93
		}
		// ======================================= start 数量字段参与计算

		// ======================================= start 排序
		Arrays.sort(collVOs, new Comparator<PrayReportVO>() {
			public int compare(PrayReportVO o1, PrayReportVO o2) {
				return o1.getPordercode().compareToIgnoreCase(o2.getPordercode());
			}
		});
		// ======================================= end 排序
		setBodyDataVO(collVOs, false);
	}

	@Override
	protected void setBodyDataVO(CircularlyAccessibleValueObject[] dataVO, boolean isLoadFormula) {
		super.setBodyDataVO(dataVO, isLoadFormula);
	}

	@Override
	protected void setBodyData(StorageDataSet dataSet) {
		super.setBodyData(dataSet);
	}

}
