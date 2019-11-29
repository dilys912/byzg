/**
 * 
 */
package nc.ui.ic.klreport;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable; 
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.ic.ic637.QueryConditionDlg;
import nc.ui.ic.ic637.StockAgeHelper;
import nc.ui.ic.pub.bill.ICBcurrArithUI;
import nc.ui.ic.pub.report.ICReportHelper;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.report.ReportBaseClass;
import nc.ui.pub.report.ReportItem;
import nc.ui.trade.business.HYPubBO_Client;
import nc.uif.pub.exception.UifException;
import nc.vo.bd.def.DefVO;
import nc.vo.ic.ic637.StockAgeHeaderVO;
import nc.vo.ic.ic637.StockAgeItemVO;
import nc.vo.ic.ic637.StockAgeVO;
import nc.vo.ic.pub.GenMethod;
import nc.vo.ic.pub.ICGenVO;
import nc.vo.ic.pub.bill.DoubleScale;
import nc.vo.pub.BusinessException;
import nc.vo.pub.cquery.FldgroupVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.report.QryOrderVO;

/**
 * @author Administrator
 * 
 */
public class KlRepUI extends nc.ui.ic.pub.report.IcBaseReport {
	private ReportBaseClass ivjReportBase = null;
	private QueryConditionDlg ivjQueryConditionDlg = null;

	private String m_sRNodeName = "40083414SYS";
	private String m_sQTempletID = "11113206400000303714";
	private String m_sCorpID = null; // 公司ID
	private String m_sUserID = null; // 当前使用者ID
	private String m_sTitle = nc.ui.ml.NCLangRes.getInstance().getStrByID(
			"4008report", "UPP4008report-000075")/* @res "库龄分析" */;
	private String m_sPNodeCode = "40083414";
	private String m_sVOName = "nc.vo.ic.ic637.StockAgeVO";
	private String m_sHeaderVOName = "nc.vo.ic.ic637.StockAgeHeaderVO";
	private String m_sItemVOName = "nc.vo.ic.ic637.StockAgeItemVO";

	private Hashtable m_htShowFlag = new Hashtable();

	private ButtonObject m_boQuery = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
					"UC001-0000006")/* @res "查询" */, nc.ui.ml.NCLangRes
					.getInstance().getStrByID("common", "UC001-0000006")/*
																		 * @res
																		 * "查询"
																		 */, 2,
			"查询"); /* -=notranslate=- */

	// 按钮组
	private ButtonObject[] m_MainButtonGroup = { m_boQuery

	};
	// xcl 现存量 nplanmny0计划金额 nmny0金额
	// 分析方式标志，用于传入BS端
	private ArrayList m_alFxfs = new ArrayList();
	// 查出来的该公司的库龄时段数组
	private String[] m_sKlsds = null; // 含有“天”
	private String[] m_sKlsdday = null; // 只为日期，不含“天”，比上一个数组短一个长度
	// 非共有列的hashtable
	private Hashtable m_htOtherColumns = new Hashtable();
	// 报表内的VO
	private StockAgeVO m_tvoStockAgeVO = new StockAgeVO();
	// 登录时的日期
	private String m_sLoginDate = "";

	// 币钟
	private ICBcurrArithUI currType = null;

	private HashMap hsItemPos = null;

	private boolean isShowNplanmny1 = false;// 动态多表头是否显示计划金额
	private boolean isShowNmny1 = false;// 动态多表头是否显示金额
	private String invcode = "";
	private String invclasscode  = "";

 
	/**
	 * ClientUI 构造子注解。
	 */
	public KlRepUI() {
		super();
		initialize();
	}

	/**
	 * 创建者：仲瑞庆 功能：合并两个MachineAccountVO，将tvoFromVO中的非空表头VO合入tvoToVO中的非空表体VO 参数：
	 * 返回： 例外： 日期：(2001-8-3 下午 7:47) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @param tvoToVO
	 *            nc.vo.ic.ic611.MachineAccountVO
	 * @param tvoFromVO
	 *            nc.vo.ic.ic611.MachineAccountVO
	 */
	protected void combineHeaderVO(StockAgeVO tvoToVO, StockAgeVO tvoFromVO,
			nc.vo.pub.query.ConditionVO[] voCons) {

		StockAgeHeaderVO thvoFromVO = (StockAgeHeaderVO) tvoFromVO
				.getParentVO();
		StockAgeHeaderVO thvoToVO = (StockAgeHeaderVO) tvoToVO.getParentVO();

		// 整合两个HeaderVO
		String[] names = thvoFromVO.getAttributeNames();
		for (int j = 0; j < names.length; j++) {
			if ((null != thvoFromVO.getAttributeValue(names[j].trim()))
					&& (thvoFromVO.getAttributeValue(names[j].trim())
							.toString().trim().length() != 0)) {
				thvoToVO.setAttributeValue(names[j].trim(), thvoFromVO
						.getAttributeValue(names[j].trim()));
			}
		}

		// 改写HeaderVO
		for (int i = 0; i < voCons.length; i++) {
			// 一般处理,对名称相同的
			try {
				thvoToVO.setAttributeValue(voCons[i].getFieldCode().trim(),
						voCons[i].getValue().trim());
			} catch (Exception e) {
			}
			// 特殊处理,依节点而变
			if (voCons[i].getFieldCode().trim().equals("pk_corp")) {
				thvoToVO.setAttributeValue("corpname", voCons[i].getRefResult()
						.getRefName().trim());
			} else if (voCons[i].getFieldCode().trim().equals(
					"cwarehouseclassid")) {
				if (voCons[i].getRefResult() == null)
					continue;
				thvoToVO.setAttributeValue("cwarehouseclassname", voCons[i]
						.getRefResult().getRefName().trim());
			} else if (voCons[i].getFieldCode().trim().equals("cwarehouseid")) {
				if (voCons[i].getRefResult() == null)
					continue;
				thvoToVO.setAttributeValue("cwarehousename", voCons[i]
						.getRefResult().getRefName().trim());
			} else if (voCons[i].getFieldCode().trim().equals(
					"cinventoryclasscode")) {
				thvoToVO.setAttributeValue("cinventoryclassname", voCons[i]
						.getRefResult().getRefName().trim());
				// thvoToVO.setAttributeValue("cinventoryclasscode",
				// voCons[i].getRefResult().getRefCode().trim());
			} else if (voCons[i].getFieldCode().trim().equals("cinventorycode")) {
				// thvoToVO.setAttributeValue(
				// "cinventorycode",
				// voCons[i].getRefResult().getRefCode().trim());
				if (voCons[i].getRefResult() != null
						&& voCons[i].getRefResult().getRefName() != null)
					thvoToVO.setAttributeValue("invname", voCons[i]
							.getRefResult().getRefName().trim());
				// } else if
				// (voCons[i].getFieldCode().trim().equals("castunitid")) {
				// thvoToVO.setAttributeValue(
				// "castunitname",
				// voCons[i].getRefResult().getRefName().trim());
				// } else if
				// (voCons[i].getFieldCode().trim().equals("vbatchcode")) {
				// thvoToVO.setAttributeValue("vbatchcode",
				// voCons[i].getValue().trim());
			}
		}

		thvoToVO.setAttributeValue("nowdate", m_sLoginDate);
		tvoToVO.setParentVO(thvoToVO);
	}

	/**
	 * 创建者：ybg 功能：将Object addojb添加到Object beforeobj后 参数： 返回： 例外： 日期：(2001-8-21
	 * 16:20:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @param iFxfs
	 *            int
	 * @param sAddedItems
	 *            java.lang.String[]
	 */
	protected boolean addItemToListAfter(Vector list, ReportItem ri) {

		boolean retb = false;
		if (list == null || ri == null)
			return retb;

		if (ri == null)
			return retb;

		ReportItem ritmp = null;

		for (int i = 0, loop = list.size(); i < loop; i++) {
			ritmp = (ReportItem) list.get(i);
			if (ritmp != null && ritmp.getShowOrder() > ri.getShowOrder()) {
				list.add(i, ri);
				retb = true;
				break;
			}
		}
		if (!retb) {
			list.add(ri);
			retb = true;
		}

		return retb;

	}

	/**
	 * 创建者：ybg 功能：将Object addojb添加到Object beforeobj后 参数： 返回： 例外： 日期：(2001-8-21
	 * 16:20:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @param iFxfs
	 *            int
	 * @param sAddedItems
	 *            java.lang.String[]
	 */
	protected static void addObjToListAfter(List list, Object afterobj,
			Object addojb, int defaultpos) {

		if (list == null || addojb == null)
			return;
		int ipos = getPosByObj(list, afterobj, -1);
		if (ipos < 0)
			ipos = defaultpos;
		else
			ipos++;
		if (ipos < 0 || ipos >= list.size())
			list.add(addojb);
		else
			list.add(ipos, addojb);

	}

	/**
	 * 创建者：ybg 功能：查找列表中Object的位置pos 参数： 返回： 例外： 日期：(2001-8-21 16:20:32)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @param iFxfs
	 *            int
	 * @param sAddedItems
	 *            java.lang.String[]
	 */
	protected static int getPosByObj(List list, Object obj, int nofindretpos) {
		if (list == null || obj == null)
			return nofindretpos;
		Iterator iter = list.iterator();

		int ipos = 0;
		while (iter.hasNext()) {
			if (obj == iter.next())
				return ipos;
			ipos++;
		}
		return nofindretpos;
	}

	/**
	 * 创建者：仲瑞庆 功能：重置报表模板 参数： 返回： 例外： 日期：(2001-8-21 16:20:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @param iFxfs
	 *            int
	 * @param sAddedItems
	 *            java.lang.String[]
	 */
	protected void setItemPosToInit(ReportItem[] biReport) {
		if (biReport == null || biReport.length <= 0)
			return;

		if (hsItemPos == null) {
			hsItemPos = new HashMap();
			for (int i = 0; i < biReport.length; i++) {
				hsItemPos.put(biReport[i].getKey(), new Integer(biReport[i]
						.getShowOrder()));
			}
			return;
		}
		Integer pos = null;
		for (int i = 0; i < biReport.length; i++) {
			pos = (Integer) hsItemPos.get(biReport[i].getKey());
			if (pos != null)
				biReport[i].setShowOrder(pos.intValue());
		}

	}

	/**
	 * 创建者：仲瑞庆 功能：重置报表模板 参数： 返回： 例外： 日期：(2001-8-21 16:20:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @param iFxfs
	 *            int
	 * @param sAddedItems
	 *            java.lang.String[]
	 */
	protected void createReportTemplet(int iFxfs, String[] sAddedItems) {
		ReportItem[] biReport = getReportBaseClass().getBody_Items();
		if (biReport == null || biReport.length == 0) {
			return;
		}

		setItemPosToInit(biReport);

		Vector vReportItem = new Vector(1, 1); // 前置列
		Vector vFieldGroup = new Vector(); // 多表头列字段

		String[] sColKeys = new String[sAddedItems.length];
		String[] sColNames = sAddedItems;
		for (int i = 0; i < sAddedItems.length; i++) {
			sColKeys[i] = StockAgeItemVO.SPREV_dynamic_num
					+ Integer.toString(i).trim();
		}

		for (int i = 0; i < biReport.length; i++) {
			if (biReport[i] != null && biReport[i].isShow()) {
				String sIDColName = biReport[i].getKey().trim();
				if (sIDColName.startsWith(StockAgeItemVO.SPREV_dynamic_num)
						|| sIDColName
								.startsWith(StockAgeItemVO.SPREV_dynamic_planmny)
						|| sIDColName
								.startsWith(StockAgeItemVO.SPREV_dynamic_mny)) {
					// 滤掉多表头
				} else {
					// if (iFxfs == 0) {
					// //库龄分析
					if ((sIDColName.equals("castunitname"))
							|| (sIDColName.equals("vfree0"))
							|| (sIDColName.equals("vbatchcode"))
							|| (sIDColName.equals("warnoutdays"))
							|| (sIDColName.equals("averageinnum7days"))
							|| (sIDColName.equals("averageinnum30days"))
							|| (sIDColName.equals("nplanmny0"))
							|| (sIDColName.equals("nmny0"))
							|| (sIDColName.equals("nplanmny1"))
							|| (sIDColName.equals("nprice"))
							|| (sIDColName.equals("nmny1"))
							|| (sIDColName.equals("nplanmny2"))
							|| (sIDColName.equals("nmny2"))
							|| (sIDColName.equals("vplanpsn"))
							|| (sIDColName.equals("abctype"))
							|| (sIDColName.startsWith("def"))) {
						m_htOtherColumns.put(sIDColName, biReport[i]);
					} else {
						vReportItem.add(biReport[i]);
					}
					// } else {
					// //预警分析
					// if ((sIDColName.equals("castunitname"))
					// || (sIDColName.equals("vfree0"))
					// || (sIDColName.equals("vbatchcode"))
					// || (sIDColName.equals("warnoutdays"))
					// || (sIDColName.equals("averageinnum7days"))
					// || (sIDColName.equals("averageinnum30days"))) {
					// m_htOtherColumns.put(sIDColName, biReport[i]);
					// } else {
					// vReportItem.add(biReport[i]);
					// }
					// }
				}
			}
		}

		if (iFxfs == 0) {

			// ReportItem ri =
			// getReportBaseClass().getBody_Item("cwarehousename");
			// if(ri==null)
			// getReportBaseClass().getBody_Item("ccalbodyname");
			// addObjToListAfter(vReportItem,ri,m_htOtherColumns.get("averageinnum30days"),ri==null?0:-1);
			// addObjToListAfter(vReportItem,m_htOtherColumns.get("averageinnum30days"),m_htOtherColumns.get("nplanmny0"),-1);
			// addObjToListAfter(vReportItem,m_htOtherColumns.get("nplanmny0"),m_htOtherColumns.get("nmny0"),-1);
			// ri = getReportBaseClass().getBody_Item("xcl");
			// addObjToListAfter(vReportItem,ri,m_htOtherColumns.get("nplanprice"),-1);
			// addObjToListAfter(vReportItem,m_htOtherColumns.get("nplanprice"),m_htOtherColumns.get("nplanmny1"),-1);
			// addObjToListAfter(vReportItem,m_htOtherColumns.get("nplanmny1"),m_htOtherColumns.get("nprice"),-1);
			// addObjToListAfter(vReportItem,m_htOtherColumns.get("nprice"),m_htOtherColumns.get("nmny1"),-1);

			addItemToListAfter(vReportItem, (ReportItem) m_htOtherColumns
					.get("averageinnum30days"));
			addItemToListAfter(vReportItem, (ReportItem) m_htOtherColumns
					.get("nplanmny0"));
			addItemToListAfter(vReportItem, (ReportItem) m_htOtherColumns
					.get("nmny0"));
			addItemToListAfter(vReportItem, (ReportItem) m_htOtherColumns
					.get("abctype"));
			for (int i = 1; i <= 20; i++)
				addItemToListAfter(vReportItem, (ReportItem) m_htOtherColumns
						.get("def" + i));
			addItemToListAfter(vReportItem, (ReportItem) m_htOtherColumns
					.get("nplanprice"));
			addItemToListAfter(vReportItem, (ReportItem) m_htOtherColumns
					.get("nplanmny1"));
			addItemToListAfter(vReportItem, (ReportItem) m_htOtherColumns
					.get("nprice"));
			addItemToListAfter(vReportItem, (ReportItem) m_htOtherColumns
					.get("nmny1"));

			int igroupsubcount = 1;
			if (isShowNplanmny1)
				igroupsubcount++;

			if (isShowNmny1)
				igroupsubcount++;

			if (igroupsubcount == 1) {
				// 库龄分析
				procFieldGroupData(-1, vReportItem, vFieldGroup,
						nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"4008report", "UPP4008report-000173"),/* 库龄时段及数量 */
						nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"4008report", "UPP4008report-000173"),
						sColKeys, sColNames, true, null);
			} else {

				String numname = nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"common", "UC000-0002282");
				String nplanmnyname = nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("common", "UC000-0003522");
				String nmnyname = nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"common", "UC000-0004112");
				// 处理分组
				String[] groupsubcodes = new String[igroupsubcount];
				String[] groupsubnames = new String[igroupsubcount];

				for (int i = 0, loop = sColNames.length; i < loop; i++) {

					groupsubcodes[0] = StockAgeItemVO.SPREV_dynamic_num + i;
					groupsubnames[0] = numname;
					if (isShowNplanmny1) {
						groupsubcodes[1] = StockAgeItemVO.SPREV_dynamic_planmny
								+ i;
						groupsubnames[1] = nplanmnyname;
					}

					if (isShowNmny1) {
						groupsubcodes[2] = StockAgeItemVO.SPREV_dynamic_mny + i;
						groupsubnames[2] = nmnyname;
					}

					procFieldGroupData(-1, vReportItem, vFieldGroup,
							sColNames[i], sColNames[i], groupsubcodes,
							groupsubnames, true, null);
				}

			}

			// 加入列尾字段
			addItemToListAfter(vReportItem, (ReportItem) m_htOtherColumns
					.get("averageinnum7days"));
			addItemToListAfter(vReportItem, (ReportItem) m_htOtherColumns
					.get("nplanmny2"));
			addItemToListAfter(vReportItem, (ReportItem) m_htOtherColumns
					.get("nmny2"));
			addItemToListAfter(vReportItem, (ReportItem) m_htOtherColumns
					.get("vplanpsn"));

			// vReportItem.addElement(m_htOtherColumns.get("averageinnum7days"));
			//				
			// addObjToListAfter(vReportItem,m_htOtherColumns.get("averageinnum7days"),m_htOtherColumns.get("nplanmny2"),-1);
			// addObjToListAfter(vReportItem,m_htOtherColumns.get("nplanmny2"),m_htOtherColumns.get("nmny2"),-1);

			// vReportItem.addElement(m_htOtherColumns.get("averageinnum30days"));

			ReportItem riaResult[] = new ReportItem[vReportItem.size()];
			vReportItem.copyInto(riaResult);
			FldgroupVO voaFg[] = new FldgroupVO[vFieldGroup.size()];
			vFieldGroup.copyInto(voaFg);
			getReportBaseClass().setFieldGroup(voaFg);
			getReportBaseClass().setBody_Items(riaResult);

			// 设置单价金额精度
			setPriceMnyDigit();

		} else {
			// 预警分析
			// vReportItem.add(7, m_htOtherColumns.get("castunitname"));
			// vReportItem.add(7, m_htOtherColumns.get("vfree0"));
			// vReportItem.add(8, m_htOtherColumns.get("vbatchcode"));

			addItemToListAfter(vReportItem, (ReportItem) m_htOtherColumns
					.get("vfree0"));
			addItemToListAfter(vReportItem, (ReportItem) m_htOtherColumns
					.get("vbatchcode"));

			addItemToListAfter(vReportItem, (ReportItem) m_htOtherColumns
					.get("abctype"));
			for (int i = 1; i <= 20; i++)
				addItemToListAfter(vReportItem, (ReportItem) m_htOtherColumns
						.get("def" + i));

			// vReportItem.addElement(m_htOtherColumns.get("warnoutdays"));
			addItemToListAfter(vReportItem, (ReportItem) m_htOtherColumns
					.get("warnoutdays"));

			addItemToListAfter(vReportItem, (ReportItem) m_htOtherColumns
					.get("vplanpsn"));

			ReportItem riaResult[] = new ReportItem[vReportItem.size()];
			vReportItem.copyInto(riaResult);
			// getReportBaseClass().setBody_Items(null);
			getReportBaseClass().setFieldGroup(null);
			getReportBaseClass().setBody_Items(riaResult);
		}
		getReportBaseClass().getBillTable().addSortListener();

	}

	/**
	 * 创建者：王乃军 功能：得到环境初始数据，如制单人等。 参数： 返回： 例外： 日期：(2001-5-9 9:23:32)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	protected void getCEnvInfo() {
		try {
			nc.ui.pub.ClientEnvironment ce = nc.ui.pub.ClientEnvironment
					.getInstance();

			// 当前使用者ID
			try {
				m_sUserID = ce.getUser().getPrimaryKey();
			} catch (Exception e) {
				nc.vo.scm.pub.SCMEnv.out("test user id is 2011000001");
				m_sUserID = "2011000001";
			}

			// //当前使用者姓名
			// try {
			// m_sUserName= ce.getUser().getUserName();
			// } catch (Exception e) {
			// nc.vo.scm.pub.SCMEnv.out("test user name is 张三");
			// m_sUserName= "张三";
			// }

			// 公司ID
			try {
				m_sCorpID = ce.getCorporation().getPrimaryKey();
				nc.vo.scm.pub.SCMEnv.out("---->corp id is " + m_sCorpID);
			} catch (Exception e) {

			}
			// 日期
			try {
				if (ce.getDate() != null)
					m_sLoginDate = ce.getDate().toString();
			} catch (Exception e) {

			}

		} catch (Exception e) {
		}
	}

	/**
	 * 返回 QueryConditionClient1 特性值。
	 * 
	 * @return nc.ui.pub.query.QueryConditionClient
	 */
	private QueryConditionDlg getConditionDlg() {
		if (ivjQueryConditionDlg == null) {
			ivjQueryConditionDlg = new QueryConditionDlg(this);
			// ivjQueryConditionDlg.setDefaultCloseOperation(
			// ivjQueryConditionDlg.HIDE_ON_CLOSE);

			// 读查询模版数据
			ivjQueryConditionDlg.setTempletID(m_sCorpID, getPNodeCode(),
					m_sUserID, null);

			// 设定自由项的显示
			// ivjQueryConditionDlg.setFreeItem("vfree0", "cinventoryid");
			// 设定自动清零的字段
			String[] sThenClear = new String[] { "cwarehouseid",
					"cinventorycode" };
			ivjQueryConditionDlg.setAutoClear("pk_corp", sThenClear);
			// 分析方式
			sThenClear = new String[] { "warningdays" };
			ivjQueryConditionDlg.setAutoClear("analysestyle", sThenClear);
			// 设定辅计量
			// ivjQueryConditionDlg.setAstUnit("castunitid", new String[] {
			// "pk_corp","cinventoryid" });
			// 设置下拉框显示
			ivjQueryConditionDlg.setCombox("analysestyle", new String[][] {
					{
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"4008report", "UPP4008report-000075")/*
																		 * @res
																		 * "库龄分析"
																		 */,
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"4008report", "UPP4008report-000075") /*
																		 * @res
																		 * "库龄分析"
																		 */},
					{
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"4008report", "UPP4008report-000183")/*
																		 * @res
																		 * "预警分析"
																		 */,
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"4008report", "UPP4008report-000183") /*
																		 * @res
																		 * "预警分析"
																		 */
					} });

			ArrayList alCorpIDs = new ArrayList();
			try {
				alCorpIDs = ICReportHelper.queryCorpIDs(m_sUserID);
			} catch (Exception e) {
				nc.vo.scm.pub.SCMEnv.error(e);
			}

			// 以下为对参照的初始化
			ivjQueryConditionDlg.initQueryDlgRef();
			ivjQueryConditionDlg.initCorpRef("pk_corp", m_sCorpID, alCorpIDs);

			nc.ui.scm.pub.def.DefSetTool.updateQueryConditionForInvbasdoc(
					ivjQueryConditionDlg, m_sCorpID, "def");
			ivjQueryConditionDlg.setCorpRefs("pk_corp", new String[] {
					"cwarehouseid", "cinventorycode" });
		}
		return ivjQueryConditionDlg;
	}

	/**
	 * 返回 ReportBaseClass 特性值。
	 * 
	 * @return nc.ui.pub.report.ReportBaseClass
	 */
	/* 警告：此方法将重新生成。 */
	public ReportBaseClass getReportBaseClass() {
		if (ivjReportBase == null) {
			try {
				ivjReportBase = new nc.ui.pub.report.ReportBaseClass();
				ivjReportBase.setName("ReportBase");
				// user code begin {1}

			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				// handleException(ivjExc);
			}
		}
		return ivjReportBase;
	}

	/**
	 * 创建者：王乃军 功能：得到一个初始化的动态添加的列 参数： 返回： 例外： 日期：(2001-8-17 13:13:51)
	 * 修改日期，修改人，修改原因，注释标志：
	 */
	protected ReportItem getReportItem(String sKey, String sTitle) {
		ReportItem biAddItem = new ReportItem();
		biAddItem.setName(sTitle);
		biAddItem.setKey(sKey);
		biAddItem.setDataType(2);
		biAddItem.setLength(DoubleScale.INT_LENGTH + 1
				+ m_iaScale[DoubleScale.NUM]);
		biAddItem.setDecimalDigits(m_iaScale[DoubleScale.NUM]);
		biAddItem.setWidth(80);
		biAddItem.setEnabled(false);
		biAddItem.setShow(true);
		// biAddItem.setShowOrder(iStartCol + 1);
		if (sKey.startsWith(StockAgeItemVO.SPREV_dynamic_num)
				|| sKey.startsWith(StockAgeItemVO.SPREV_dynamic_mny)
				|| sKey.startsWith(StockAgeItemVO.SPREV_dynamic_planmny))
			biAddItem.setTatol(true);
		biAddItem.setPos(1);
		return biAddItem;
	}

	/**
	 * 子类实现该方法，返回业务界面的标题。
	 * 
	 * @version (00-6-6 13:33:25)
	 * 
	 * @return java.lang.String
	 */
	public String getTitle() {
		if (getReportBaseClass().getReportTitle() != null)
			return getReportBaseClass().getReportTitle();
		else
			return nc.ui.ml.NCLangRes.getInstance().getStrByID("4008report",
					"UPP4008report-000075")/* @res "库龄分析" */;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-7-24 20:03:46)
	 */
	private void initialize() {

		setName("ClientUI");
		setLayout(new java.awt.BorderLayout());
		setSize(774, 419);
		add(getReportBaseClass(), "Center");

		// 得到环境变量，并保存到成员变量。
		getCEnvInfo();
		// 置报表精度
		initSysParam();
		// 设置按钮组
		setButtons(getButtonArray(m_MainButtonGroup));
		// 初始化模板
		initReportTemplet(m_sRNodeName);
		// 初始化自定义项
		initInvBasDocDef();
		getReportBaseClass().setRowNOShow(true);
		getReportBaseClass().setTatolRowShow(true);
		// 库存参数表IC036:库龄时段中的天
		String sKlsd = "";
		try {
			sKlsd = nc.ui.pub.para.SysInitBO_Client.getParaString(m_sCorpID,
					"IC036");
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv
					.out("can not fetch the param of IC for Tracked Bill!");
		}

		if (sKlsd != null && sKlsd.trim().length() != 0) {
			sKlsd = sKlsd.trim();
			int number = 1;
			Vector vKlsd = new Vector();
			Vector vKlsdday = new Vector();
			int iFirstIndex = 0;
			int iIndex = 0;
			while (true) {
				iIndex = sKlsd.indexOf(",", iFirstIndex);
				if (iIndex < 0)
					break;
				number++;
				vKlsd.addElement(sKlsd.substring(iFirstIndex, iIndex)
						+ nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"4008report", "UPP4008report-000184")/*
																	 * @res "天"
																	 */);
				vKlsdday.addElement(sKlsd.substring(iFirstIndex, iIndex));
				iFirstIndex = iIndex + 1;
			}
			vKlsd.add(sKlsd.substring(iFirstIndex)
					+ nc.ui.ml.NCLangRes.getInstance().getStrByID("4008report",
							"UPP4008report-000184")/* @res "天" */);
			vKlsdday.add(sKlsd.substring(iFirstIndex));
			vKlsdday.add("1171");
			vKlsd.add(">"
					+ sKlsd.substring(iFirstIndex)
					+ nc.ui.ml.NCLangRes.getInstance().getStrByID("4008report",
							"UPP4008report-000184")/* @res "天" */);
			m_sKlsds = new String[vKlsd.size()];
			m_sKlsdday = new String[vKlsdday.size()];
			vKlsd.copyInto(m_sKlsds);
			vKlsdday.copyInto(m_sKlsdday);
			// createReportTemplet(1, new String[] { "15天", "30天", ">30天" });
			createReportTemplet(0, m_sKlsds);
		}
		// public ButtonObject m_btnPrint = new ButtonObject("模板打印", "使用模板打印",
		// 5);
		// public ButtonObject m_btnPreview = new ButtonObject("模板打印预览",
		// "模板打印预览", 5);
		nc.ui.pub.ButtonObject[] hb = { m_btnPrint, m_btnPreview };
		hideButtons(hb);
		setScale();
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-7-24 18:42:06)
	 * 
	 * @param sNodeName
	 *            java.lang.String
	 */
	public void initReportTemplet(String sNodeName) {

		// getReportBaseClass().setNodeCode(sNodeName);
		// 读取模版数据
		try {
			getReportBaseClass().setTempletID(m_sCorpID, getPNodeCode(),
					m_sUserID, null);
		} catch (Exception e) {
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000019")/*
														 * @res
														 * "不能得到模版，请与系统管理员联系！"
														 */);
			return;
		}
		nc.ui.pub.bill.BillData bd = getReportBaseClass().getBillData();
		if (bd == null) {
			nc.vo.scm.pub.SCMEnv.out("--> billdata null.");
			return;
		}

		// 获得表体、表头、表尾元素信息
		String[] strBodyFields = getReportBaseClass().getBodyFields();
		String[] strHeadFields = getReportBaseClass().getHeadFields();
		String[] strTailFields = getReportBaseClass().getTailFields();
		// 获得字段分组、排序、汇总VO数组
		QryOrderVO[] groupVOs = getReportBaseClass().getGroupVOs();
		QryOrderVO[] orderVOs = getReportBaseClass().getOrderVOs();
		String[] strSums = getReportBaseClass().getSums();
		// 获得报表标题、副标题
		String[] strTitles = new String[] {
				getReportBaseClass().getReportTitle(),
				getReportBaseClass().getReportSubtitle() };

		if (strBodyFields != null) {
			for (int i = 0; i < strBodyFields.length; i++) {
				if (strBodyFields[i].endsWith("flag")) {
					String sField = strBodyFields[i].substring(0,
							strBodyFields[i].length() - 4);
					if (m_htShowFlag.containsKey(sField))
						continue;
					else
						m_htShowFlag.put(sField, new Boolean(true));
				} else
					m_htShowFlag.put(strBodyFields[i], new Boolean(true));

			}
		}

		// 处理上述数据
		// nc.vo.scm.pub.SCMEnv.out();
		// print(strBodyFields, "表体字段（静态列）");
		// print(strHeadFields, "表头字段");
		// print(strTailFields, "表尾字段");
		// print(groupVOs, "分组字段");
		// print(orderVOs, "排序字段");
		// print(strSums, "汇总字段");
		// print(strTitles, "报表标题");

		// 准备表体VO数据
		// TestBodyVO[] voBodys = getVOBody_1();
		// nc.vo.sm.funcreg.FuncRegisterVO[] voBodys = getVOBody_2();
		// 准备表头VO数据
		// TestHeadVO[] voHeads = getVOHead_1();

		// 动态加列
		// ReportItem[] items = new ReportItem[1];
		// items[0] = getItem();
		// getReportBaseClass().addBodyItem(items);

		// 设置表体VO
		// getReportBaseClass().setBodyDataVO(null);
		// 设置表头VO
		// getReportBaseClass().setHeadDataVO(null);

		ReportItem ri = getReportBaseClass().getBody_Item("nplanmny1");
		if (ri != null)
			isShowNplanmny1 = ri.isShow();

		ri = getReportBaseClass().getBody_Item("nmny1");
		if (ri != null)
			isShowNmny1 = ri.isShow();

		ri = getReportBaseClass().getBody_Item("nprice");
		if (ri != null)
			ri.setTatol(false);

		ri = getReportBaseClass().getBody_Item("nplanprice");
		if (ri != null)
			ri.setTatol(false);

		return;

	}

	/**
	 * 子类实现该方法，响应按钮事件。
	 * 
	 * @version (00-6-1 10:32:59)
	 * 
	 * @param bo
	 *            ButtonObject
	 */
	public void onButtonClicked(nc.ui.pub.ButtonObject bo) {
		if (bo == m_boQuery)
			onQuery(true);

		else
			super.onButtonClicked(bo);
	}

	
	public void onQuery(boolean bQuery) {
		getConditionDlg().hideNormal();
		if (bQuery || !m_bEverQry) {
			getConditionDlg().showModal();
			m_bEverQry = true;
		} else {
			getConditionDlg().onButtonConfig();
		}

		if (!getConditionDlg().isCloseOK())
			return;
		setDlgSubTotal(null);
		resetPanelShowState();

		nc.vo.pub.query.ConditionVO[] voCons = getConditionDlg()
				.getConditionVO();
		resetConditionVO(voCons);

		// 压缩条件VO
		nc.vo.pub.query.ConditionVO[] cvoIn = packConditionVO(voCons);

		List<ConditionVO> list = new ArrayList<ConditionVO>();
		String tempDate = null;
		if (cvoIn != null && cvoIn.length > 0) {
			for (int i = 0; i < cvoIn.length; i++) {
				String itemcode = cvoIn[i].getFieldCode();
				String comcode = cvoIn[i].getOperaCode();
				
				 if(itemcode.equals("invclasscode")){
					 invclasscode = cvoIn[i].getValue();
					 invclasscode = invclasscode.substring(1, invclasscode.length());

					  getReportBaseClass().setBodyDataVO(getInvclByInvcode1(invclasscode));	
				
/*				if (itemcode.equals("invclasscode") && comcode.equals("like")) {
					String tempvalue = cvoIn[i].getValue();
					tempvalue = tempvalue.substring(1, tempvalue.length());
					cvoIn[i].setValue(tempvalue);
					list.add(cvoIn[i]);*/
				} else if (itemcode.equals("cscode") && comcode.equals("like")) {
					String tempvalue = cvoIn[i].getValue();
					tempvalue = tempvalue.substring(1, tempvalue.length());
					cvoIn[i].setValue(tempvalue);
					list.add(cvoIn[i]);
				} else if (itemcode.equals("dbizdate")) {
					// 当日期有值的时候，取出日期，不加入list集合中
					tempDate = cvoIn[i].getValue();
				} else if (itemcode.equals("cinventorycode")) {
					invcode = cvoIn[i].getValue();
					updateUI();
					getReportBaseClass().setBodyDataVO(getInvclByInvcode(invcode));
				} else {
					list.add(cvoIn[i]);
				}

			}

		}
		// 遍历list集合，加入到新的ConditionVO[]数组中，之后重新将newcvoIn的值赋给 cvoIn
		ConditionVO[] newcvoIn = new ConditionVO[list.size()];
		for (int i = 0; i < list.size(); i++) {
			newcvoIn[i] = list.get(i);
		}
		cvoIn = newcvoIn;

		String sWhereClause = getConditionDlg().getWhereSQL(cvoIn);
		nc.vo.scm.pub.SCMEnv.out(sWhereClause);

		if (tempDate != null && tempDate.length() > 9) {
			m_sLoginDate = tempDate;
		} 
/*		updateUI();
		getReportBaseClass().setBodyDataVO(getInvclByInvcode(invcode));*/
	}
	
	/**
	 * 此处插入方法说明。 创建日期：(2001-7-24 18:45:22)
	 */
	/*public void onQuery(boolean bQuery) {
		// ivjQueryConditionDlg= null;

		getConditionDlg().hideNormal();
		if (bQuery || !m_bEverQry) {
			getConditionDlg().showModal();
			m_bEverQry = true;
		} else {
			getConditionDlg().onButtonConfig();
		}

		if (!getConditionDlg().isCloseOK())
			return;
		setDlgSubTotal(null);
		resetPanelShowState();
		try {
			nc.vo.pub.query.ConditionVO[] voCons = getConditionDlg()
					.getConditionVO();
			resetConditionVO(voCons);

			// 压缩条件VO
			nc.vo.pub.query.ConditionVO[] cvoIn = packConditionVO(voCons);

			*//**************************************** add by yhj 2014-03-10 START ***********************************//*
			*//**
			 * 此段代码用途：
			 * 1.用于将系统的like条件里的值进行转换，目前系统只要是like的话，值就是以“%XXX%”形式出现，根据实际情况
			 * ，需要将格式变成“XXX%” 2.如果查询条件中选择了日期的话，在cvoIn数组中需要将日期条件去掉
			 *//*
			List<ConditionVO> list = new ArrayList<ConditionVO>();
			String tempDate = null;
			if (cvoIn != null && cvoIn.length > 0) {
				for (int i = 0; i < cvoIn.length; i++) {
					String itemcode = cvoIn[i].getFieldCode();
					String comcode = cvoIn[i].getOperaCode();
					if (itemcode.equals("invclasscode")
							&& comcode.equals("like")) {
						String tempvalue = cvoIn[i].getValue();
						tempvalue = tempvalue.substring(1, tempvalue.length());
						cvoIn[i].setValue(tempvalue);
						list.add(cvoIn[i]);
					} else if (itemcode.equals("cscode")
							&& comcode.equals("like")) {
						String tempvalue = cvoIn[i].getValue();
						tempvalue = tempvalue.substring(1, tempvalue.length());
						cvoIn[i].setValue(tempvalue);
						list.add(cvoIn[i]);
					} else if (itemcode.equals("dbizdate")) {
						// 当日期有值的时候，取出日期，不加入list集合中
						tempDate = cvoIn[i].getValue();
					}else if (itemcode.equals("invcode")){
						invcode = cvoIn[i].getValue();
					} else {
						list.add(cvoIn[i]);
					}

				}

			}
			// 遍历list集合，加入到新的ConditionVO[]数组中，之后重新将newcvoIn的值赋给 cvoIn
			ConditionVO[] newcvoIn = new ConditionVO[list.size()];
			for (int i = 0; i < list.size(); i++) {
				newcvoIn[i] = list.get(i);
			}
			cvoIn = newcvoIn;

			*//**************************************** add by yhj 2014-03-10 END *****************************************//*

			String sWhereClause = getConditionDlg().getWhereSQL(cvoIn);
			nc.vo.scm.pub.SCMEnv.out(sWhereClause);

			*//********************** edit by yhj 2014-03-08 START *********************************//*
			*//**
			 * 此处要判断 ，查询框中的日期条件是否为空，如果为空，直接取当前登录日期，如果不为空，则取查询框中的日期
			 *//*
			if (tempDate != null && tempDate.length() > 9) {
				m_sLoginDate = tempDate;
			}
			*//********************** edit by yhj 2014-03-08 END *********************************//*
			StockAgeVO tvo = StockAgeHelper.queryKlfxInfo(m_alFxfs, m_sKlsdday,
					cvoIn, sWhereClause, m_sLoginDate);
			if ((tvo == null) || (null == tvo.getParentVO())) {
				// showErrorMessage("没有任何数据!");
				tvo = new StockAgeVO();
				// return;
			}
			if (null == tvo.getChildrenVO()) {
				StockAgeItemVO[] thivotemp = new StockAgeItemVO[1];
				thivotemp[0] = new StockAgeItemVO();
				tvo.setChildrenVO(thivotemp);
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008report", "UPP4008report-000030") @res "没有任何数据!" );
			} else
				showHintMessage("查询完毕，共返回 " + tvo.getChildrenVO().length
						+ " 行。");

			nc.ui.scm.pub.FreeVOParse freeVOParse = new nc.ui.scm.pub.FreeVOParse();
			freeVOParse.setFreeVO(tvo.getChildrenVO(), "pk_invbasdoc",
					"cinventoryid", false);

			// 合并结果VO为MachineAccountVO
			m_tvoStockAgeVO = new StockAgeVO();
			m_tvoStockAgeVO.setParentVO(new StockAgeHeaderVO());
			// 表体
			m_tvoStockAgeVO.setChildrenVO(tvo.getChildrenVO());
			// 表头
			combineHeaderVO(m_tvoStockAgeVO, tvo, voCons);

			// 修改界面
			if (m_alFxfs.get(0).toString().trim().equals("0")) {
				createReportTemplet(0, m_sKlsds);
			} else {
				createReportTemplet(1, m_sKlsds);
			}

			m_sTotalKeys = getSumCol(getReportBaseClass());
			// 置入界面
			StockAgeHeaderVO tmpHead = (StockAgeHeaderVO) m_tvoStockAgeVO
					.getParentVO();
			getReportBaseClass().setHeadDataVO(tmpHead);
			getReportBaseClass().setBodyDataVO(m_tvoStockAgeVO.getChildrenVO());

			// calculateTotal();
			getReportBaseClass().getBillModel().reCalcurateAll();
			// showHintMessage("查询完毕，共返回 " +
			// m_tvoStockAgeVO.getChildrenVO().length + " 行。");

		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
			clearInterface();
			showErrorMessage(e.getMessage());
		}

		return;

	}*/
	
	
	
	public StockAgeItemVO[] getInvclByInvcode1(String invclasscode){
		List<StockAgeItemVO[]> arraylist = new ArrayList<StockAgeItemVO[]>();
		
		 ArrayList list1 = getInvcode(invclasscode);
		 if(list1.size()>0){
		 	for(int j =0;j<list1.size();j++){
			 	  Map map = (Map) list1.get(j);
				    invcode = map.get("invcode") == null ? "" : map.get("invcode").toString();
		
/*				StringBuffer sql = new StringBuffer();
				sql.append(" select invcl.invclasscode from bd_invcl invcl ") 
				.append(" left join bd_invbasdoc bas ") 
				.append(" on bas.pk_invcl = invcl.pk_invcl ") 
				.append(" left join bd_invmandoc man ") 
				.append(" on man.pk_invbasdoc = bas.pk_invbasdoc ") 
				.append(" where bas.invcode = '"+invcode+"' ") 
				.append(" and nvl(bas.dr,0) = 0 ") 
				.append(" and nvl(man.dr,0) = 0 ") 
				.append(" and nvl(invcl.dr,0) = 0 ") 
				.append(" and man.pk_corp = '"+m_sCorpID+"' ") ;

				String invcl = "";
				IUAPQueryBS uAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
				try {
					invcl = (String)uAPQueryBS.executeQuery(sql.toString(), new ColumnProcessor());
				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				
				String invstr = invcode.substring(0,2);
				String invstr2 = invcode.substring(0,4);
				String invclstr = invcode.substring(0,6);
				StockAgeItemVO[] gljvo = null;
				StockAgeItemVO[] cpvo = null;
				StockAgeItemVO[] truevo = null;
				StockAgeItemVO[] falsevo = null;
								
				//钢铝卷取数:250101、250102、250111、250112、250113、250114、14030101、14030102、14030105、14030106、14030107、14030108
				//成品取数:21、22、23、1405
				//其余库龄：分为批次和非批次
				if (invclstr.equals("250101") || invclstr.equals("250102")
						|| invclstr.equals("250111") || invclstr.equals("250112")
						|| invclstr.equals("250113") || invclstr.equals("250114")
						|| invclstr.equals("14030101") || invclstr.equals("14030102")
						|| invclstr.equals("14030105") || invclstr.equals("14030106")
						|| invclstr.equals("14030107") || invclstr.equals("14030108")) {
					// 钢铝卷取数
					gljvo = getGljKl(invcode);// add by wbp 2017-12-15 解析方法注释
					arraylist.add(gljvo);
				} else if (invstr.equals("21") || invstr.equals("22")
						|| invstr.equals("23") || invstr2.equals("1405")) {
					cpvo = getCpKl(invcode);
					arraylist.add(cpvo);
				}else if(getInvFlag(invcode)==true){  //判断存货是否批次启用
					truevo = getTrueKl(invcode);       //启用批次
					arraylist.add(truevo);
				}else if(getInvFlag(invcode)==false){  //判断存货是否批次启用
					falsevo = getFalseKl(invcode);    //未启用批次
					arraylist.add(falsevo);
				}
				 	}
				 }
				 
				 ArrayList<StockAgeItemVO> sumlist = new ArrayList<StockAgeItemVO>();		 
				for (int i = 0;i<arraylist.size();i++){
					StockAgeItemVO[] vos=arraylist.get(i);
					for(int j = 0;j<vos.length;j++){
						sumlist.add(vos[j]);
					}
				}
		 
		/*		ArrayList<StockAgeItemVO> list = new ArrayList<StockAgeItemVO>();
				if(gljvo!=null&&gljvo.length>0){
					for(int i = 0;i<gljvo.length;i++){
						list.add(gljvo[i]);
					}
				}
				if(cpvo!=null&&cpvo.length>0){
					for(int i = 0;i<cpvo.length;i++){
						list.add(cpvo[i]);
					}
				}
				if(truevo!=null&&truevo.length>0){
					for(int i = 0;i<truevo.length;i++){
						list.add(truevo[i]);
					}
				}
				if(falsevo!=null&&falsevo.length>0){
					for(int i = 0;i<falsevo.length;i++){
						list.add(falsevo[i]);
					}
				}*/
				
				StockAgeItemVO[] vo = new StockAgeItemVO[sumlist.size()];
				for(int k = 0;k<sumlist.size();k++){
					vo[k]=sumlist.get(k);
				}
				return vo;
			}
	
	
	public ArrayList getInvcode(String invclasscode){		
		StringBuffer sql = new StringBuffer();
		sql.append(" select bas.invcode ") 
		.append("   from bd_invcl inv ") 
		.append("   left join bd_invbasdoc bas ") 
		.append("     on inv.pk_invcl = bas.pk_invcl ") 
		.append("   left join bd_invmandoc man ") 
		.append("     on bas.pk_invbasdoc=man.pk_invbasdoc ") 
		.append("  where inv.invclasscode like '"+invclasscode+"' ") 
		.append("     and man.pk_corp='"+m_sCorpID+"' ") 
		.append("     and nvl(bas.dr,0) = 0 ") 
		.append("     and nvl(man.dr,0) = 0 ") 
		.append("     and nvl(inv.dr,0) = 0 ");	
		ArrayList list = new ArrayList();
		IUAPQueryBS uAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		try {
			list = (ArrayList)uAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;	  
	}
	
	

	/**
	 * 创建者：仲瑞庆 功能：压缩条件VO，生成向BS端传入的参数 参数：由查询窗口得到的条件VO 返回：压缩后的条件VO 例外：
	 * 日期：(2001-8-2 下午 3:18) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return boolean
	 * @param cvo
	 *            nc.vo.pub.query.QueryConditionVO
	 */
	protected ConditionVO[] packConditionVO(ConditionVO[] cvonow) {
		ConditionVO[] cvo = new ConditionVO[cvonow.length];
		for (int i = 0; i < cvonow.length; i++) {
			cvo[i] = (ConditionVO) (cvonow[i].clone());
		}

		m_alFxfs = new ArrayList(2);
		m_alFxfs.add(null);
		m_alFxfs.add(null);
		// 预定为库龄分析
		m_alFxfs.set(0, new Integer(0));

		ArrayList alcvo = new ArrayList();
		ConditionVO[] cvoFromAlcvo = null;

		for (int i = 0; i < cvo.length; i++) {
			// 预警天数
			if (cvo[i].getFieldCode().trim().equals("warningdays")) {
				// 预警分析
				m_alFxfs.set(0, new Integer(1));
				m_alFxfs.set(1, cvo[i].getValue().trim());
				// 补上对括弧的保留
				cvo[i].setFieldCode("1");
				cvo[i].setOperaCode("=");
				cvo[i].setValue("1");
				cvo[i].setDataType(1);
				alcvo.add(cvo[i]);
				continue;
			} else if (cvo[i].getFieldCode().trim().equals("analysestyle")) {
				// 补上对括弧的保留
				cvo[i].setFieldCode("1");
				cvo[i].setOperaCode("=");
				cvo[i].setValue("1");
				cvo[i].setDataType(1);
				alcvo.add(cvo[i]);
				// } else if
				// (cvo[i].getFieldCode().trim().equals("cwarehouseclassid")) {
				// //对库存组织的变更
				// cvo[i].setFieldCode("c.pk_calbody");
				// alcvo.add(cvo[i]);
			} else {
				alcvo.add(cvo[i]);
			}
		}

		cvoFromAlcvo = new ConditionVO[alcvo.size()];
		for (int i = 0; i < alcvo.size(); i++) {
			cvoFromAlcvo[i] = (ConditionVO) alcvo.get(i);
		}

		return cvoFromAlcvo;
	}

	/**
	 * 创建者：王乃军 功能：处理分组数据 参数： int iKeyIndex,
	 * //小于零则按照实际传入的itemkey写到AddReportItem,否则在指定的itemkey后加上iKeyIndex.
	 * 如：-1，saColKey[0]="MyKey"------>新增的ReportItem的itemkey是MyKey
	 * 20，saColKey[0]="MyKey"------>新增的ReportItem的itemkey是MyKey20 Vector
	 * vAddReportItem, //动态加的列，不能为空 Vector vFieldGroup, //分组字段Vector,不能为空 String
	 * sTitle, //顶层列 String sSubTitle, //中层列，如果和顶层列相等，则中层和顶层合为一层。 String[]
	 * saColKey, //底层列的itemkey String[] saColTitle, //底层列的标题 boolean
	 * bRetTopLevel, //最后一个fieldgroup的顶级标题标志 ArrayList alOtherParam) 返回： 例外：
	 * 日期：(2001-8-17 13:13:51) 修改日期，修改人，修改原因，注释标志：
	 */
	public void procFieldGroupData(int iKeyIndex, Vector vAddReportItem,
			Vector vFieldGroup, String sTitle, String sSubTitle,
			String[] saColKey, String[] saColTitle, boolean bRetTopLevel,
			ArrayList alOtherParam) {

		if (sTitle == null || saColTitle == null || saColTitle.length == 0
				|| saColKey == null || saColKey.length != saColTitle.length
				|| vAddReportItem == null || vFieldGroup == null) {
			nc.vo.scm.pub.SCMEnv.out("param null");
			return;
		}
		ReportItem riTemp = null;
		if (iKeyIndex >= 0)
			riTemp = getReportItem(saColKey[0] + iKeyIndex, saColTitle[0]);
		else
			riTemp = getReportItem(saColKey[0], saColTitle[0]);
		vAddReportItem.addElement(riTemp);

		// 子列个数
		int iColNum = saColTitle.length;

		FldgroupVO voFg = null;
		// 只有一列
		if (iColNum == 1) {
			voFg = new FldgroupVO();
			voFg.setGroupid(new Integer(0));
			voFg.setItem1("" + (vAddReportItem.size() - 1));
			voFg.setItem2(sSubTitle);
			voFg.setGrouptype("1");
			if (bRetTopLevel)
				voFg.setToplevelflag("Y");
			else
				voFg.setToplevelflag("N");
			voFg.setGroupname(sTitle);
			vFieldGroup.addElement(voFg);
			return;
		}
		// 第二列
		if (iColNum > 1) {
			if (iKeyIndex >= 0)
				riTemp = getReportItem(saColKey[1] + iKeyIndex, saColTitle[1]);
			else
				riTemp = getReportItem(saColKey[1], saColTitle[1]);
			vAddReportItem.addElement(riTemp);
			// 分组数据设置---------------
			voFg = new FldgroupVO();
			voFg.setGroupid(new Integer(0));
			voFg.setItem1("" + (vAddReportItem.size() - 1));
			voFg.setItem2("" + (vAddReportItem.size() - 2));
			voFg.setGrouptype("0"); // 基本列合并
			if (iColNum == 2 && bRetTopLevel && sTitle.equals(sSubTitle)) // 只有两列,并且要求返回顶层
				voFg.setToplevelflag("Y");
			else
				voFg.setToplevelflag("N");
			voFg.setGroupname(sSubTitle);

			vFieldGroup.addElement(voFg);
			if (iColNum == 2 && bRetTopLevel && sTitle.equals(sSubTitle)) // 只有两列,并且要求返回顶层
				return;
		}

		// 中间的随便加
		for (int col = 2; col < iColNum; col++) {
			if (iKeyIndex >= 0)
				riTemp = getReportItem(saColKey[col] + iKeyIndex,
						saColTitle[col]);
			else
				riTemp = getReportItem(saColKey[col], saColTitle[col]);
			vAddReportItem.addElement(riTemp);

			// 分组数据设置---------------
			voFg = new FldgroupVO();
			voFg.setGroupid(new Integer(0));
			voFg.setItem1("" + (vAddReportItem.size() - 1));
			voFg.setItem2(sSubTitle);
			voFg.setGrouptype("1");
			// 最后一个时，当sTitle=sSubTitle，需特殊处理
			if (col == (iColNum - 1) && sTitle.equals(sSubTitle)) {
				if (bRetTopLevel)
					voFg.setToplevelflag("Y");
			} else
				voFg.setToplevelflag("N");
			voFg.setGroupname(sSubTitle);
			vFieldGroup.addElement(voFg);
			// 最后一个时，当sTitle=sSubTitle，需特殊处理
			if (col == (iColNum - 1) && sTitle.equals(sSubTitle))
				return;
		}
		// 如果sTitle不和sSubTitle相等,并且还没有sTitle的group field，先产生一个
		if (sTitle != sSubTitle) {
			int iSearch = 0;
			FldgroupVO voTemporaryField = null;
			for (iSearch = 0; iSearch < vFieldGroup.size(); iSearch++)
				if (vFieldGroup.elementAt(iSearch) != null) {
					voTemporaryField = (FldgroupVO) vFieldGroup
							.elementAt(iSearch);
					if (sTitle.equals(voTemporaryField.getGroupname()))
						break;
				}
			// 未找到,加上个临时的VO
			if (iSearch >= vFieldGroup.size()) {
				voFg = new FldgroupVO();
				voFg.setGroupid(new Integer(0));
				voFg.setToplevelflag("N");
				voFg.setItem1(sSubTitle);
				voFg.setItem2(sSubTitle);
				voFg.setGrouptype("3");
				voFg.setGroupname(sTitle);
				vFieldGroup.addElement(voFg);
			} else { // 找到了,
				// 如果是临时的，就删除原来的，追加上新的VO
				if (voTemporaryField.getItem1().equals(
						voTemporaryField.getItem2()))
					vFieldGroup.removeElementAt(iSearch);
				voFg = new FldgroupVO();
				voFg.setGroupid(new Integer(0));
				if (bRetTopLevel)
					voFg.setToplevelflag("Y");
				else
					voFg.setToplevelflag("N");
				voFg.setItem1(sSubTitle);
				if (voTemporaryField.getItem1().equals(
						voTemporaryField.getItem2()))
					voFg.setItem2(voTemporaryField.getItem1());
				else
					// 已经有了，则继续追加
					voFg.setItem2(sTitle);
				voFg.setGrouptype("3");
				voFg.setGroupname(sTitle);
				vFieldGroup.addElement(voFg);
			}
		}
	}

	// 小数精度定义--->
	// 数量小数位 2
	// 辅计量数量小数位 2
	// 换算率 2
	// 存货成本单价小数位 2
	protected int m_iaScale[] = new int[] { 2, 2, 2, 2, 2 };

	// 合计列
	String[] m_sTotalKeys = null;
	private nc.vo.pub.AggregatedValueObject m_voReport = null;

	/**
	 * 创建者：仲瑞庆 功能：清界面 参数： 返回： 例外： 日期：(2001-12-13 14:53:16) 修改日期，修改人，修改原因，注释标志：
	 */
	protected void clearInterface() {
		try {
			// 置入界面
			StockAgeHeaderVO tmpHead = new StockAgeHeaderVO();
			getReportBaseClass().setHeadDataVO(tmpHead);
			StockAgeItemVO[] tempBodys = new StockAgeItemVO[1];
			tempBodys[0] = new StockAgeItemVO();
			getReportBaseClass().setBodyDataVO(tempBodys);
			m_voReport = (nc.vo.pub.AggregatedValueObject) Class.forName(
					m_sVOName).newInstance();
			m_voReport.setParentVO(getReportBaseClass().getHeadDataVO());
			m_voReport.setChildrenVO(getReportBaseClass().getBodyDataVO());
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
		}
	}

	/**
	 * 创建者：仲瑞庆 功能： 参数： 返回： 例外： 日期：(2001-12-25 13:27:18) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return java.lang.String[]
	 * @param biBillItem
	 *            nc.ui.pub.bill.BillItem[]
	 */
	protected String[] getSumCol(nc.ui.pub.report.ReportBaseClass rep) {
		// Vector vKey= new Vector();
		// for (int i= 0; i < biBillItem.length; i++) {
		// if (biBillItem[i].isTotal()) {
		// vKey.add(biBillItem[i].getKey());
		// }
		// }
		// String[] sKey= new String[vKey.size()];
		// vKey.copyInto(sKey);
		// return sKey;
		String[] sKey = rep.getSums();
		int iFirstLength = sKey.length;
		String[] sValue = new String[iFirstLength + m_sKlsds.length];
		for (int i = 0; i < iFirstLength; i++) {
			sValue[i] = sKey[i];
		}
		for (int i = 0; i < m_sKlsds.length; i++) {
			sValue[i + iFirstLength] = "numberinday"
					+ Integer.toString(i).trim();
		}
		return sValue;
	}

	/**
	 * 创建者：王乃军 功能：读系统参数 参数： 返回： 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	protected void initSysParam() {
		// m_sTrackedBillFlag = "N";
		// m_sSaveAndSign = "N";
		try {
			// 库存参数表IC028:出库时是否指定入库单;批次参照跟踪是否到单据号.
			// IC010 是否保存即签字。

			// 参数编码 含义 缺省值
			// BD501 数量小数位 2
			// BD502 辅计量数量小数位 2
			// BD503 换算率 2
			// BD504 存货成本单价小数位 2
			String[] saParam = new String[] { "IC028", "IC010", "BD501",
					"BD502", "BD503", "BD504", "BD301" };

			// 传入的参数
			ArrayList alAllParam = new ArrayList();
			// 查参数的必须数据
			ArrayList alParam = new ArrayList();
			alParam.add(m_sCorpID); // 第一个是公司
			alParam.add(saParam); // 待查的参数
			alAllParam.add(alParam);
			// 查用户对应公司的必须参数
			alAllParam.add(m_sUserID);

			ArrayList alRetData = (ArrayList) nc.ui.ic.pub.bill.GeneralBillHelper
					.queryInfo(new Integer(
							nc.vo.ic.pub.bill.QryInfoConst.INIT_PARAM),
							alAllParam);

			// 目前读两个。
			if (alRetData == null || alRetData.size() < 2) {
				showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008report", "UPP4008report-000031")/* @res "初始化参数错误！" */);
				return;
			}
			// 读回的参数值
			String[] saParamValue = (String[]) alRetData.get(0);
			// 追踪到单据参数,默认设置为"N"
			if (saParamValue != null
					&& saParamValue.length >= alAllParam.size()) {
				// if(saParamValue[0]!=null)
				// m_sTrackedBillFlag = saParamValue[0].toUpperCase().trim();
				// 是否保存即签字。默认设置为"N"
				// if(saParamValue[1]!=null)
				// m_sSaveAndSign = saParamValue[1].toUpperCase().trim();
				// BD501 数量小数位 2
				if (saParamValue[2] != null)
					m_iaScale[0] = Integer.parseInt(saParamValue[2]);
				// BD502 辅计量数量小数位 2
				if (saParamValue[3] != null)
					m_iaScale[1] = Integer.parseInt(saParamValue[3]);
				// BD503 换算率 2
				if (saParamValue[4] != null)
					m_iaScale[2] = Integer.parseInt(saParamValue[4]);
				// BD504 存货成本单价小数位 2
				if (saParamValue[5] != null)
					m_iaScale[3] = Integer.parseInt(saParamValue[5]);
				// BD301 本币小数位
				if (saParamValue[6] != null)
					m_iaScale[4] = Integer.parseInt(saParamValue[6]);
			}
			// 读回的操作对应的公司
			// m_alUserCorpID = (ArrayList) alRetData.get(1);

		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out("can not get para" + e.getMessage());
			if (e instanceof nc.vo.pub.BusinessException)
				showErrorMessage(e.getMessage());
		}
	}

	/**
	 * 创建者：王乃军 功能：设置表体/表尾的小数位数 参数： 返回： 例外： 日期：(2001-11-23 18:11:18)
	 * 修改日期，修改人，修改原因，注释标志：
	 */
	protected void setScale() {

		// 考虑到报表模版内部的效率，单独拿出来效率更高些。因为表体数量较大，用哈希表实现.
		BillData bd = getReportBaseClass().getBillData();
		nc.ui.pub.bill.BillItem[] biaCardBody = bd.getBodyItems();
		Hashtable htCardBody = new Hashtable();
		for (int i = 0; i < biaCardBody.length; i++)
			htCardBody.put(biaCardBody[i].getKey(), new Integer(i));

		// 表头与表体加长长度
		// 表头
		for (int i = 0; i < biaCardBody.length; i++) {
			biaCardBody[i].setLength(100);
		}
		// 表体
		for (int i = 0; i < bd.getHeadShowItems().length; i++) {
			bd.getHeadShowItems()[i].setLength(100);
		}

		// 表体数量
		String[] saBodyNumItemKey = { "ninnum", "noutnum", "nendnum",
				"nmaxstocknum", "nminstocknum", "xcl", "noutstocknum",
				"lastoutnum", "nplanedinnum", "nplanedoutnum", "allnum",
				"childsnum", "maxcanmakenum", "ncombinenum", "nstripnum",
				"nnumfrom", "nnumto", "averageinnum7days", "averageinnum30days" };
		for (int k = 0; k < saBodyNumItemKey.length; k++) {
			// 如果是此列
			if (htCardBody.containsKey(saBodyNumItemKey[k])) {
				biaCardBody[Integer.valueOf(
						htCardBody.get(saBodyNumItemKey[k]).toString())
						.intValue()].setLength(DoubleScale.INT_LENGTH + 1
						+ m_iaScale[DoubleScale.NUM]);
				biaCardBody[Integer.valueOf(
						htCardBody.get(saBodyNumItemKey[k]).toString())
						.intValue()]
						.setDecimalDigits(m_iaScale[DoubleScale.NUM]);
			}
		}
		// 表体辅数量
		String[] saBodyAstNumItemKey = { "ninastnum", "noutastnum",
				"nendastnum", "ncombineastnum", "nstripastnum", "nastnumfrom",
				"nastnumto" };
		for (int k = 0; k < saBodyAstNumItemKey.length; k++) {
			// 如果是此列
			if (htCardBody.containsKey(saBodyAstNumItemKey[k])) {
				biaCardBody[Integer.valueOf(
						htCardBody.get(saBodyAstNumItemKey[k]).toString())
						.intValue()].setLength(DoubleScale.INT_LENGTH + 1
						+ m_iaScale[DoubleScale.ASSIST_NUM]);
				biaCardBody[Integer.valueOf(
						htCardBody.get(saBodyAstNumItemKey[k]).toString())
						.intValue()]
						.setDecimalDigits(m_iaScale[DoubleScale.ASSIST_NUM]);
			}
		}
		// 表体单价
		String[] saBodyPrice = {};
		for (int k = 0; k < saBodyPrice.length; k++) {
			// 如果是此列
			if (htCardBody.containsKey(saBodyPrice[k])) {
				biaCardBody[Integer.valueOf(
						htCardBody.get(saBodyPrice[k]).toString()).intValue()]
						.setLength(DoubleScale.INT_LENGTH + 1
								+ m_iaScale[DoubleScale.PRICE]);
				biaCardBody[Integer.valueOf(
						htCardBody.get(saBodyPrice[k]).toString()).intValue()]
						.setDecimalDigits(m_iaScale[DoubleScale.PRICE]);
			}
		}
		// 表体金额---->防止单据模版修改缺省小数长度
		String[] saBodyMny = { "ninje", "noutje", "nendje" };
		for (int k = 0; k < saBodyMny.length; k++) {
			// 如果是此列
			if (htCardBody.containsKey(saBodyMny[k])) {
				biaCardBody[Integer.valueOf(
						htCardBody.get(saBodyMny[k]).toString()).intValue()]
						.setLength(DoubleScale.INT_LENGTH + 1
								+ m_iaScale[DoubleScale.MNY]);
				biaCardBody[Integer.valueOf(
						htCardBody.get(saBodyMny[k]).toString()).intValue()]
						.setDecimalDigits(m_iaScale[DoubleScale.MNY]);
			}
		}
		// 表头数量
		String[] saHeaderNumItemKey = { "nmaxstocknum", "nminstocknum",
				"nsafestocknum", "ctjallnum", "ctjmaxcanmakenum" };
		for (int k = 0; k < saHeaderNumItemKey.length; k++) {
			// 如果是此列
			try {
				bd.getHeadItem(saHeaderNumItemKey[k])
						.setLength(
								DoubleScale.INT_LENGTH + 1
										+ m_iaScale[DoubleScale.NUM]);
				bd.getHeadItem(saHeaderNumItemKey[k]).setDecimalDigits(
						m_iaScale[DoubleScale.NUM]);
			} catch (Exception e) {

			}
		}
		// 表头单价
		String[] saHeaderPrice = { "nprice" };
		for (int k = 0; k < saHeaderPrice.length; k++) {
			// 如果是此列
			try {
				bd.getHeadItem(saHeaderPrice[k]).setLength(
						DoubleScale.INT_LENGTH + 1
								+ m_iaScale[DoubleScale.PRICE]);
				bd.getHeadItem(saHeaderPrice[k]).setDecimalDigits(
						m_iaScale[DoubleScale.PRICE]);
			} catch (Exception e) {

			}
		}

		// 表头固定精度
		String[] saHeaderFixNum = { "ndullstandard", "neardays" };
		for (int k = 0; k < saHeaderFixNum.length; k++) {
			// 如果是此列
			try {
				bd.getHeadItem(saHeaderFixNum[k]).setDecimalDigits(1);
			} catch (Exception e) {

			}
		}

		// 表体固定精度
		String[] saBodyFixNum = { "maxcycledays", "truecycledays",
				"differencecycleday", "warnoutdays", "warningdays" };
		for (int k = 0; k < saBodyFixNum.length; k++) {
			// 如果是此列
			try {
				bd.getBodyItem(saBodyFixNum[k]).setDecimalDigits(1);
			} catch (Exception e) {

			}
		}
		if (htCardBody.containsKey("hsl")) {
			biaCardBody[Integer.valueOf(htCardBody.get("hsl").toString())
					.intValue()].setLength(DoubleScale.INT_LENGTH + 1
					+ m_iaScale[DoubleScale.CONVERT_RATE]);
			biaCardBody[Integer.valueOf(htCardBody.get("hsl").toString())
					.intValue()]
					.setDecimalDigits(m_iaScale[DoubleScale.CONVERT_RATE]);
		}
		// getReportBaseClass().setBillData(bd);

	}

	/**
	 * ClientUI 构造子注解。
	 */
	public KlRepUI(nc.ui.pub.FramePanel ff) {
		super();
		setFrame(ff);
		initialize();
	}

	/**
	 * 此处插入方法说明。 创建日期：(2003-9-24 14:20:02)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getCorpID() {
		return m_sCorpID;
	}

	/**
	 * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2002-11-5 10:38:55) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return java.lang.String
	 */
	public String getDefaultPNodeCode() {
		return m_sPNodeCode;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2003-9-24 14:21:06)
	 * 
	 * @return nc.vo.pub.AggregatedValueObject
	 */
	public nc.vo.pub.AggregatedValueObject getReportVO() {
		// 准备数据
		StockAgeVO vo = (StockAgeVO) getReportBaseClass().getBillValueVO(
				m_sVOName, m_sHeaderVOName, m_sItemVOName);

		if (null == vo) {
			vo = new StockAgeVO();
		}
		if (null == vo.getParentVO()) {
			vo.setParentVO(new StockAgeHeaderVO());
		}

		return vo;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2003-9-24 14:20:02)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getUserID() {
		return m_sUserID;
	}

	/**
	 * 类型说明： 创建日期：(2002-11-21 14:46:22) 作者：王亮 修改日期： 修改人： 修改原因： 算法说明：
	 */
	private void resetConditionVO(nc.vo.pub.query.ConditionVO[] conVO) {

		if (conVO != null && conVO.length != 0) {
			for (int i = 0; i < conVO.length; i++) {
				if ("like".equals(conVO[i].getOperaCode().trim())
						&& conVO[i].getFieldCode() != null) {

					conVO[i].setValue("%" + conVO[i].getValue() + "%");
				}
			}
		}
	}

	/**
	 * 小计 功能： 参数： 返回： 例外： 日期：(2002-10-18 9:07:05) 修改日期，修改人，修改原因，注释标志：
	 */
	public void onSubTotal() {
		super.onSubTotal();
		UFDouble ntolnum = GenMethod.ZERO, ntolmny = GenMethod.ZERO, ntolplanmny = GenMethod.ZERO, ntemp = null;
		Object otemp = null;
		// GenMethod.ZERO
		for (int i = 0, loop = getReportBaseClass().getRowCount(); i < loop; i++) {

			otemp = getReportBaseClass().getBodyValueAt(i, "cinventorycode");
			if (otemp == null || otemp.toString().trim().length() <= 0) {
				otemp = getReportBaseClass().getBodyValueAt(i, "nprice");
				if (otemp != null && otemp.toString().trim().length() > 0) {
					getReportBaseClass().setBodyValueAt(
							ICGenVO.div(ntolmny, ntolnum), i, "nprice");
					getReportBaseClass().setBodyValueAt(
							ICGenVO.div(ntolplanmny, ntolnum), i, "nplanprice");
				}
				ntolnum = GenMethod.ZERO;
				ntolmny = GenMethod.ZERO;
				ntolplanmny = GenMethod.ZERO;
				continue;
			}

			otemp = getReportBaseClass().getBodyValueAt(i, "xcl");
			if (otemp != null) {
				ntolnum = ICGenVO.add(ntolnum, new UFDouble(otemp.toString()
						.trim()));
			}
			if (ntolnum == null)
				ntolnum = GenMethod.ZERO;

			otemp = getReportBaseClass().getBodyValueAt(i, "nmny1");
			if (otemp != null) {
				ntolmny = ICGenVO.add(ntolmny, new UFDouble(otemp.toString()
						.trim()));
			}
			if (ntolmny == null)
				ntolmny = GenMethod.ZERO;

			otemp = getReportBaseClass().getBodyValueAt(i, "nplanmny1");
			if (otemp != null) {
				ntolplanmny = ICGenVO.add(ntolplanmny, new UFDouble(otemp
						.toString().trim()));
			}
			if (ntolplanmny == null)
				ntolplanmny = GenMethod.ZERO;

		}

	}

	/**
	 * 获取币种。
	 */
	public ICBcurrArithUI getCurrType() {
		if (currType == null) {
			// currType = new
			// nc.ui.bd.b21.BcurrArith(getClientEnvironment().getCorporation().getPk_corp());
			currType = new ICBcurrArithUI(getClientEnvironment()
					.getCorporation().getPk_corp());
		}
		return currType;
	}

	/**
	 * 设置币种，单价精度。
	 */
	public void setPriceMnyDigit() {
		try {

			ICBcurrArithUI currtype = getCurrType();
			if (currtype == null)
				return;

			int iDigit = 4;
			Integer Digit = currtype
					.getBusiCurrDigit(currtype.getLocalCurrPK());
			iDigit = Digit == null ? 2 : Digit.intValue();
			// BD504 存货成本单价小数位 2
			// m_iaScale[3] = Integer.parseInt(saParamValue[5]);
			// 单价小数位
			ReportItem bi = getReportBaseClass().getBody_Item("nplanprice");
			if (bi != null)
				bi.setDecimalDigits(m_iaScale[3]);
			bi = getReportBaseClass().getBody_Item("nprice");
			if (bi != null)
				bi.setDecimalDigits(m_iaScale[3]);

			ReportItem[] bis = getReportBaseClass().getBody_Items();

			for (int i = 0, loop = bis.length; i < loop; i++) {
				String sIDColName = bis[i].getKey().trim();
				if (sIDColName.startsWith(StockAgeItemVO.SPREV_dynamic_planmny)
						|| sIDColName
								.startsWith(StockAgeItemVO.SPREV_dynamic_mny)
						|| (sIDColName.equals("nplanmny0"))
						|| (sIDColName.equals("nmny0"))
						|| (sIDColName.equals("nplanmny1"))
						|| (sIDColName.equals("nmny1"))
						|| (sIDColName.equals("nplanmny2"))
						|| (sIDColName.equals("nmny2"))

				) {
					bis[i].setDecimalDigits(iDigit);
				}
			}

		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
		}
	}

	/**
	 * 设置存货自定义项。
	 */
	public void initInvBasDocDef() {
		try {
			// 查询对应公司的存货档案的自定义项 "存货管理档案"
			DefVO[] defs = nc.ui.bd.service.BDDef.queryDefVO("存货档案",
					getClientEnvironment().getCorporation().getPk_corp());/*
																		 * -=notranslate
																		 * =-
																		 */
			if (defs == null || defs.length <= 0)
				return;
			ReportItem[] bis = getReportBaseClass().getBody_Items();
			int index = 0;
			for (int i = 0, loop = bis.length; i < loop; i++) {
				String sIDColName = bis[i].getKey().trim();
				if (sIDColName.startsWith("def")) {
					try {
						index = Integer.parseInt(sIDColName.substring(3)) - 1;
					} catch (Exception e) {
						nc.vo.scm.pub.SCMEnv.error(e);
						continue;
					}

					if (index >= 0 && index < defs.length
							&& defs[index] != null) {
						bis[i].setName(defs[index].getDefname());
					}
				}
			}
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
		}
	}

	public StockAgeItemVO[] getInvclByInvcode(String invcode){
		StringBuffer sql = new StringBuffer();
		sql.append(" select invcl.invclasscode from bd_invcl invcl ") 
		.append(" left join bd_invbasdoc bas ") 
		.append(" on bas.pk_invcl = invcl.pk_invcl ") 
		.append(" left join bd_invmandoc man ") 
		.append(" on man.pk_invbasdoc = bas.pk_invbasdoc ") 
		.append(" where bas.invcode = '"+invcode+"' ") 
		.append(" and nvl(bas.dr,0) = 0 ") 
		.append(" and nvl(man.dr,0) = 0 ") 
		.append(" and nvl(invcl.dr,0) = 0 ") 
		.append(" and man.pk_corp = '"+m_sCorpID+"' ") ;

		String invcl = "";
		IUAPQueryBS uAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		try {
			invcl = (String)uAPQueryBS.executeQuery(sql.toString(), new ColumnProcessor());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String invstr = invcl.substring(0,2);
		String invstr2 = invcl.substring(0,4);
		String invclstr = invcl.substring(0,6);
		StockAgeItemVO[] gljvo = null;
		StockAgeItemVO[] cpvo = null;
		StockAgeItemVO[] truevo = null;
		StockAgeItemVO[] falsevo = null;
		//钢铝卷取数:250101、250102、250111、250112、250113、250114、14030101、14030102、14030105、14030106、14030107、14030108
		//成品取数:21、22、23、1405
		//其余库龄：分为批次和非批次
		if (invclstr.equals("250101") || invclstr.equals("250102")
				|| invclstr.equals("250111") || invclstr.equals("250112")
				|| invclstr.equals("250113") || invclstr.equals("250114")
				|| invclstr.equals("14030101") || invclstr.equals("14030102")
				|| invclstr.equals("14030105") || invclstr.equals("14030106")
				|| invclstr.equals("14030107") || invclstr.equals("14030108")) {
			// 钢铝卷取数
			gljvo = getGljKl(invcode);
		} else if (invstr.equals("21") || invstr.equals("22")
				|| invstr.equals("23") || invstr2.equals("1405")) {
			cpvo = getCpKl(invcode);
		}else if(getInvFlag(invcode)==true){
			truevo = getTrueKl(invcode);
		}else if(getInvFlag(invcode)==false){
			falsevo = getFalseKl(invcode);
		}
 
		ArrayList<StockAgeItemVO> list = new ArrayList<StockAgeItemVO>();
		if(gljvo!=null&&gljvo.length>0){
			for(int i = 0;i<gljvo.length;i++){
				list.add(gljvo[i]);
			}
		}
		if(cpvo!=null&&cpvo.length>0){
			for(int i = 0;i<cpvo.length;i++){
				list.add(cpvo[i]);
			}
		}
		if(truevo!=null&&truevo.length>0){
			for(int i = 0;i<truevo.length;i++){
				list.add(truevo[i]);
			}
		}
		if(falsevo!=null&&falsevo.length>0){
			for(int i = 0;i<falsevo.length;i++){
				list.add(falsevo[i]);
			}
		}
		
		StockAgeItemVO[] vo = new StockAgeItemVO[list.size()];
		for(int k = 0;k<list.size();k++){
			vo[k]=list.get(k);
		}
		return vo;
	}
	
	/**
	 * 获取现存量
	 * @param invcode
	 * @param corp
	 * @param sydate
	 * @param ymdate
	 * @param nowdate
	 * @return
	 */
	public ArrayList getOnhandnum(String invcode,String corp,String sydate,String ymdate,String nowdate){
		StringBuffer sql = new StringBuffer();
		
		/*//edit by zwx
		sql.append(" SELECT cinventoryid, ") 
		.append("        pk_corp, ") 
		.append("        cwarehouseid, ") 
		.append("        vbatchcode, ") 
		.append("        vfree1, ") 
		.append("        nonhandnum as  num, ") 
		.append("        nonhandassistnum as  nonhandassistnum,ngrossnum as ngrossnum,cbilltypecode ") 
//		.append("        sum(nonhandnum) num, ") 
//		.append("        sum(nonhandassistnum) nonhandassistnum, ") 
//		.append("        sum(ngrossnum) ngrossnum,cbilltypecode ") 
		.append("   FROM ((SELECT hand.cinventoryid, ") 
		.append("                 hand.pk_corp, ") 
		.append("                 hand.cwarehouseid, ") 
		.append("                 hand.vbatchcode, ") 
		.append("                 hand.vfree1, ") 
		.append("                 nvl(sum(nonhandnum), 0.0) nonhandnum, ") 
		.append("                 nvl(sum(nonhandassistnum), 0.0) nonhandassistnum, ") 
		.append("                 nvl(sum(ngrossnum), 0.0) ngrossnum,'' as cbilltypecode ") 
		.append("            FROM ic_month_hand hand, bd_invmandoc man, bd_invbasdoc inv ") 
		.append("           where hand.cinventoryid = man.pk_invmandoc ") 
		.append("             and man.pk_invbasdoc = inv.pk_invbasdoc ") 
		.append("             and (dyearmonth = ") 
		.append("                 (select max(dyearmonth) ") 
		.append("                     from ic_month_exec ") 
		.append("                    where bonhandflag = 'Y' ") 
		.append("                      and (dyearmonth < substr('"+nowdate+"', 0, 7) or ") 
		.append("                          '"+nowdate+"' is null)) and (1 = 1) and (1 = 1) and ") 
		.append("                 (1 = 1) and (inv.invcode = '"+invcode+"') and ") 
		.append("                 (hand.pk_corp in ('"+m_sCorpID+"')) and hand.dr=0) ") 
		.append("           GROUP BY hand.cinventoryid, ") 
		.append("                    hand.pk_corp, ") 
		.append("                    hand.cwarehouseid, ") 
		.append("                    hand.vbatchcode, ") 
		.append("                    hand.vfree1) UNION ALL ") 
		.append("         (SELECT rec.cinventoryid, ") 
		.append("                 rec.pk_corp, ") 
		.append("                 rec.cwarehouseid, ") 
		.append("                 rec.vbatchcode, ") 
		.append("                 rec.vfree1, ") 
		.append("                 sum(nvl(ninnum, 0.0) - nvl(noutnum, 0.0)) nonhandnum, ") 
		.append("                 sum(nvl(ninassistnum, 0.0) - nvl(noutassistnum, 0.0)) nonhandassistnum, ") 
		.append("                 sum(nvl(ningrossnum, 0.0) - nvl(noutgrossnum, 0.0)) ngrossnum,rec.cbilltypecode ") 
		.append("            FROM ic_month_record rec, bd_invmandoc man, bd_invbasdoc inv ") 
		.append("           where rec.cinventoryid = man.pk_invmandoc ") 
		.append("             and man.pk_invbasdoc = inv.pk_invbasdoc ") 
		.append("             and (dyearmonth >= (select to_char(add_months(to_date(max(dyearmonth), ") 
		.append("                                                     'YYYY-MM'), ") 
		.append("                                             1), ") 
		.append("                                  'YYYY-MM') ") 
		.append("                     from ic_month_exec ") 
		.append("                    where bonhandflag = 'Y' ") 
		.append("                      and (dyearmonth < substr('"+nowdate+"', 0, 7) or ") 
		.append("                          '"+nowdate+"' is null)) and dyearmonth <= (select max(dyearmonth) ") 
		.append("                     from ic_month_record ") 
		.append("                    where  (dyearmonth < substr('"+nowdate+"', 0, 7) or ") 
		.append("                          '"+nowdate+"' is null)) and ") 
		.append("                 (1 = 1) and (1 = 1) and (1 = 1) and ") 
		.append("                 (inv.invcode = '"+invcode+"') and ") 
		.append("                 (rec.pk_corp in ('"+m_sCorpID+"')) and rec.dr=0 and ") 
		.append("                 ((rec.cbilltypecode = '40' OR rec.cbilltypecode = '41' OR ") 
		.append("                 rec.cbilltypecode = '44' OR rec.cbilltypecode = '4E' OR ") 
		.append("                 rec.cbilltypecode = '45' OR rec.cbilltypecode = '46' OR ") 
		.append("                 rec.cbilltypecode = '47' OR rec.cbilltypecode = '48' OR ") 
		.append("                 rec.cbilltypecode = '49' OR rec.cbilltypecode = '4A' OR ") 
		.append("                 rec.cbilltypecode = '4B') AND ninnum IS NOT NULL OR ") 
		.append("                 (rec.cbilltypecode = '4C' OR rec.cbilltypecode = '4D' OR ") 
		.append("                 rec.cbilltypecode = '4Y' OR rec.cbilltypecode = '4F' OR ") 
		.append("                 rec.cbilltypecode = '4G' OR rec.cbilltypecode = '4H' OR ") 
		.append("                 rec.cbilltypecode = '4I' OR rec.cbilltypecode = '4J' OR ") 
		.append("                 rec.cbilltypecode = '4O') AND noutnum IS NOT NULL or ") 
		.append("                 rec.cbilltypecode = '4Q')) ") 
		.append("           GROUP BY rec.cinventoryid, rec.pk_corp, rec.cwarehouseid,rec.vbatchcode,rec.vfree1,rec.cbilltypecode) union all ") 
		.append("         (SELECT b.cinventoryid, ") 
		.append("                 h.pk_corp, ") 
		.append("                 h.cwarehouseid, ") 
		.append("                 b.vbatchcode, ") 
		.append("                 b.vfree1, ") 
		.append("                 sum(nvl(ninnum, 0.0) - nvl(noutnum, 0.0)) nonhandnum, ") 
		.append("                 sum(nvl(ninassistnum, 0.0) - nvl(noutassistnum, 0.0)) nonhandassistnum, ") 
		.append("                 sum(nvl(ningrossnum, 0.0) - nvl(noutgrossnum, 0.0)) ngrossnum,h.cbilltypecode ") 
		.append("            FROM ic_general_h h, ") 
		.append("                 ic_general_b b, ") 
		.append("                 bd_invmandoc man, ") 
		.append("                 bd_invbasdoc inv ") 
		.append("           where h.cgeneralhid = b.cgeneralhid ") 
		.append("             and b.cinventoryid = man.pk_invmandoc ") 
		.append("             and man.pk_invbasdoc = inv.pk_invbasdoc ") 
		.append("             and (dbizdate > ") 
		.append("                 (select to_char(add_months(to_date(max(dyearmonth) || '-01', ") 
		.append("                                                     'YYYY-MM-DD'), ") 
		.append("                                             1), ") 
		.append("                                  'YYYY-MM-DD') ") 
		.append("                     from ic_month_record ") 
		.append("                    where  (dyearmonth < substr('"+nowdate+"', 0, 7) or ") 
		.append("                          '"+nowdate+"' is null)) and dbizdate < '"+nowdate+"' and ") 
		.append("                 (1 = 1) and (1 = 1) and (1 = 1) and ") 
		.append("                 (inv.invcode = '"+invcode+"') and (h.pk_corp in ('"+m_sCorpID+"')) and h.dr=0 and ") 
		.append("                 ((h.cbilltypecode = '40' OR h.cbilltypecode = '41' OR ") 
		.append("                 h.cbilltypecode = '44' OR h.cbilltypecode = '4E' OR ") 
		.append("                 h.cbilltypecode = '45' OR h.cbilltypecode = '46' OR ") 
		.append("                 h.cbilltypecode = '47' OR h.cbilltypecode = '48' OR ") 
		.append("                 h.cbilltypecode = '49' OR h.cbilltypecode = '4A' OR ") 
		.append("                 h.cbilltypecode = '4B') AND ninnum IS NOT NULL OR ") 
		.append("                 (h.cbilltypecode = '4C' OR h.cbilltypecode = '4D' OR ") 
		.append("                 h.cbilltypecode = '4Y' OR h.cbilltypecode = '4F' OR ") 
		.append("                 h.cbilltypecode = '4G' OR h.cbilltypecode = '4H' OR ") 
		.append("                 h.cbilltypecode = '4I' OR h.cbilltypecode = '4J' OR ") 
		.append("                 h.cbilltypecode = '4O') AND noutnum IS NOT NULL or ") 
		.append("                 h.cbilltypecode = '4Q') and ") 
		.append("                 (h.cbiztype IN (SELECT pk_busitype ") 
		.append("                                    FROM bd_busitype ") 
		.append("                                   WHERE verifyrule <> 'J' ") 
		.append("                                     and verifyrule <> 'C') OR ") 
		.append("                 h.cbiztype IS NULL) and ") 
		.append("                 h.cbilltypecode not in ('40', '41', '42', '43', '44') and ") 
		.append("                 h.dr = 0 and b.dr = 0) ") 
		.append("           GROUP BY b.cinventoryid, ") 
		.append("                    h.pk_corp, ") 
		.append("                    h.cwarehouseid, ") 
		.append("                    b.vbatchcode, ") 
		.append("                    b.vfree1,h.cbilltypecode)) qichu ") ;
//		.append("  GROUP BY cinventoryid, pk_corp, cwarehouseid, vbatchcode, vfree1,cbilltypecode ");
//		.append("  HAVING(SUM(nonhandnum) <>0 )") 
		//end by zwx
*/		
		sql.append(" select a.pk_corp, ") 
		.append("        a.ccalbodyid, ") 
		.append("        a.cwarehouseid, ") 
		.append("        a.cinventoryid, ") 
		.append("        a.vbatchcode, ")
		.append("        a.vfree1, ") 
		.append("        a.vfree2, ") 
		.append("        a.vfree3, ") 
		.append("        a.vfree4, ") 
		.append("        a.vfree5, ") 
		.append("        a.vfree6, ") 
		.append("        a.vfree7, ") 
		.append("        a.vfree8, ") 
		.append("        a.vfree9, ") 
		.append("        a.vfree10, ") 
		.append("        a.num, ") 
		.append("        b.freezenum, ") 
		.append("        a.ngrossnum, ") 
		.append("        b.ngrossnum nfreezegrossnum ") 
		.append("   from (select kp.pk_corp, ") 
		.append("                kp.ccalbodyid, ") 
		.append("                kp.cwarehouseid, ") 
		.append("                kp.cinventoryid, ") 
		.append("                kp.vbatchcode, ")
		.append("                kp.vfree1, ") 
		.append("                kp.vfree2, ") 
		.append("                kp.vfree3, ") 
		.append("                kp.vfree4, ") 
		.append("                kp.vfree5, ") 
		.append("                kp.vfree6, ") 
		.append("                kp.vfree7, ") 
		.append("                kp.vfree8, ") 
		.append("                kp.vfree9, ") 
		.append("                kp.vfree10, ") 
		.append("                SUM(nvl(ninspacenum, 0.0)) - SUM(nvl(noutspacenum, 0.0)) num, ") 
		.append("                SUM(nvl(ningrossnum, 0.0) - nvl(noutgrossnum, 0.0)) ngrossnum ") 
		.append("           from v_ic_onhandnum6 kp, bd_invmandoc man, bd_invbasdoc inv ") 
		.append("          where kp.cinventoryid = man.pk_invmandoc ") 
		.append("            and man.pk_invbasdoc = inv.pk_invbasdoc ") 
		.append("            and ((0 = 0) and (inv.invcode = '"+invcode+"') and (0 = 0) and ") 
		.append("                (0 = 0) and (0 = 0) and (0 = 0) and (0 = 0) and (0 = 0) and ") 
		.append("                (kp.pk_corp = '"+m_sCorpID+"')) ") 
		.append("          group by kp.pk_corp, ") 
		.append("                   kp.ccalbodyid, ") 
		.append("                   kp.cwarehouseid, ") 
		.append("                   kp.cinventoryid, ")
		.append("                   kp.vbatchcode, ")
		.append("                   kp.vfree1, ") 
		.append("                   kp.vfree2, ") 
		.append("                   kp.vfree3, ") 
		.append("                   kp.vfree4, ") 
		.append("                   kp.vfree5, ") 
		.append("                   kp.vfree6, ") 
		.append("                   kp.vfree7, ") 
		.append("                   kp.vfree8, ") 
		.append("                   kp.vfree9, ") 
		.append("                   kp.vfree10) a, ") 
		.append("        (select kp.pk_corp, ") 
		.append("                kp.ccalbodyid, ") 
		.append("                kp.cwarehouseid, ") 
		.append("                kp.cinventoryid, ") 
		.append("                kp.vbatchcode, ")
		.append("                kp.vfree1, ") 
		.append("                kp.vfree2, ") 
		.append("                kp.vfree3, ") 
		.append("                kp.vfree4, ") 
		.append("                kp.vfree5, ") 
		.append("                kp.vfree6, ") 
		.append("                kp.vfree7, ") 
		.append("                kp.vfree8, ") 
		.append("                kp.vfree9, ") 
		.append("                kp.vfree10, ") 
		.append("                sum(nvl(nfreezenum, 0)) freezenum, ") 
		.append("                sum(nvl(ngrossnum, 0)) ngrossnum ") 
		.append("           from ic_freeze kp, bd_invmandoc man, bd_invbasdoc inv ") 
		.append("          where kp.cinventoryid = man.pk_invmandoc ") 
		.append("            and man.pk_invbasdoc = inv.pk_invbasdoc ") 
		.append("            and (cthawpersonid is null and (0 = 0) and ") 
		.append("                (inv.invcode = '"+invcode+"') and (0 = 0) and (0 = 0) and ") 
		.append("                (0 = 0) and (0 = 0) and (0 = 0) and (0 = 0) and ") 
		.append("                (kp.pk_corp = '"+m_sCorpID+"')) ") 
		.append("          group by kp.pk_corp, ") 
		.append("                   kp.ccalbodyid, ") 
		.append("                   kp.cwarehouseid, ") 
		.append("                   kp.cinventoryid, ") 
		.append("                   kp.vbatchcode, ")
		.append("                   kp.vfree1, ") 
		.append("                   kp.vfree2, ") 
		.append("                   kp.vfree3, ") 
		.append("                   kp.vfree4, ") 
		.append("                   kp.vfree5, ") 
		.append("                   kp.vfree6, ") 
		.append("                   kp.vfree7, ") 
		.append("                   kp.vfree8, ") 
		.append("                   kp.vfree9, ") 
		.append("                   kp.vfree10) b ") 
		.append("  where a.pk_corp = b.pk_corp(+) ") 
		.append("    and a.ccalbodyid = b.ccalbodyid(+) ") 
		.append("    and a.cwarehouseid = b.cwarehouseid(+) ") 
		.append("    and a.cinventoryid = b.cinventoryid(+) ") 
		.append("    and nvl(a.vfree1, ' ') = nvl(b.vfree1(+), ' ') ") 
		.append("    and nvl(a.vfree2, ' ') = nvl(b.vfree2(+), ' ') ") 
		.append("    and nvl(a.vfree3, ' ') = nvl(b.vfree3(+), ' ') ") 
		.append("    and nvl(a.vfree4, ' ') = nvl(b.vfree4(+), ' ') ") 
		.append("    and nvl(a.vfree5, ' ') = nvl(b.vfree5(+), ' ') ") 
		.append("    and nvl(a.vfree6, ' ') = nvl(b.vfree6(+), ' ') ") 
		.append("    and nvl(a.vfree7, ' ') = nvl(b.vfree7(+), ' ') ") 
		.append("    and nvl(a.vfree8, ' ') = nvl(b.vfree8(+), ' ') ") 
		.append("    and nvl(a.vfree9, ' ') = nvl(b.vfree9(+), ' ') ") 
		.append("    and nvl(a.vfree10, ' ') = nvl(b.vfree10(+), ' ') ") 
		.append("    and (a.num <> 0) ") ;

		/*sql.append(" SELECT cinventoryid, ") 
		.append("        pk_corp, ") 
		.append("        cwarehouseid, ") 
		.append("        sum(nonhandnum) as num, ") 
		.append("        vfree1, ") 
		.append("        vbatchcode ") 
		.append("   FROM ((SELECT hand.cinventoryid, ") 
		.append("                 hand.pk_corp, ") 
		.append("                 hand.cwarehouseid, ") 
		.append("                 nvl(sum(nonhandnum), 0.0) nonhandnum, ") 
		.append("                 hand.vfree1, ") 
		.append("                 hand.vbatchcode ") 
		.append("            FROM ic_month_hand hand, bd_invmandoc man, bd_invbasdoc inv ") 
		.append("           where hand.cinventoryid = man.pk_invmandoc ") 
		.append("             and man.pk_invbasdoc = inv.pk_invbasdoc ") 
		.append("             and (dyearmonth = '2012-12' and (1 = 1) and (1 = 1) and (1 = 1) and ") 
		.append("                 (inv.invcode = '"+invcode+"') and ") 
		.append("                 (hand.pk_corp in ('"+corp+"'))) ") 
		.append("           GROUP BY hand.cinventoryid, ") 
		.append("                    hand.pk_corp, ") 
		.append("                    hand.cwarehouseid, ") 
		.append("                    hand.vfree1, ") 
		.append("                    hand.vbatchcode) UNION ALL ") 
		.append("         (SELECT b.cinventoryid, ") 
		.append("                 h.pk_corp, ") 
		.append("                 h.cwarehouseid, ") 
		.append("                 sum(nvl(ninnum, 0.0) - nvl(noutnum, 0.0)) nonhandnum, ") 
		.append("                 b.vfree1, ") 
		.append("                 b.vbatchcode ") 
		.append("            FROM ic_general_h h, ") 
		.append("                 ic_general_b b, ") 
		.append("                 bd_invmandoc man, ") 
		.append("                 bd_invbasdoc inv ") 
		.append("           where h.cgeneralhid = b.cgeneralhid ") 
		.append("             and b.cinventoryid = man.pk_invmandoc ") 
		.append("             and man.pk_invbasdoc = inv.pk_invbasdoc ") 
		.append("             and (dbizdate > '2013-01-01' and dbizdate < '"+nowdate+"' and ") 
		.append("                 (1 = 1) and (1 = 1) and (1 = 1) and ") 
		.append("                 (inv.invcode = '"+invcode+"') and (h.pk_corp in ('"+corp+"')) and ") 
		.append("                 ((h.cbilltypecode = '40' OR h.cbilltypecode = '41' OR ") 
		.append("                 h.cbilltypecode = '44' OR h.cbilltypecode = '4E' OR ") 
		.append("                 h.cbilltypecode = '45' OR h.cbilltypecode = '46' OR ") 
		.append("                 h.cbilltypecode = '47' OR h.cbilltypecode = '48' OR ") 
		.append("                 h.cbilltypecode = '49' OR h.cbilltypecode = '4A' OR ") 
		.append("                 h.cbilltypecode = '4B') AND ninnum IS NOT NULL OR ") 
		.append("                 (h.cbilltypecode = '4C' OR h.cbilltypecode = '4D' OR ") 
		.append("                 h.cbilltypecode = '4Y' OR h.cbilltypecode = '4F' OR ") 
		.append("                 h.cbilltypecode = '4G' OR h.cbilltypecode = '4H' OR ") 
		.append("                 h.cbilltypecode = '4I' OR h.cbilltypecode = '4J' OR ") 
		.append("                 h.cbilltypecode = '4O') AND noutnum IS NOT NULL or ") 
		.append("                 h.cbilltypecode = '4Q') and ") 
		.append("                 (h.cbiztype IN (SELECT pk_busitype ") 
		.append("                                    FROM bd_busitype ") 
		.append("                                   WHERE verifyrule <> 'J' ") 
		.append("                                     and verifyrule <> 'C') OR ") 
		.append("                 h.cbiztype IS NULL) and ") 
		.append("                 h.cbilltypecode not in ('40', '41', '42', '43', '44') and ") 
		.append("                 h.dr = 0 and b.dr = 0) ") 
		.append("           GROUP BY b.cinventoryid, ") 
		.append("                    h.pk_corp, ") 
		.append("                    h.cwarehouseid, ") 
		.append("                    b.vfree1, ") 
		.append("                    b.vbatchcode)) qichu ") 
		.append("  GROUP BY cinventoryid, pk_corp, cwarehouseid, vfree1, vbatchcode ") ;*/

		ArrayList list = new ArrayList();
		IUAPQueryBS uAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		try {
			list = (ArrayList)uAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	
	
	
	//获取钢铝卷库龄
	public StockAgeItemVO[] getGljKl(String invcode){
		String pk_corp = getClientEnvironment().getCorporation().getPk_corp();
		String firstdate = m_sLoginDate.substring(0,7)+"-01";
		String ymdate = new UFDate(firstdate).getDateBefore(1).toString().substring(0,7);
		ArrayList list = getOnhandnum(invcode,pk_corp,ymdate,firstdate,m_sLoginDate);//现存量结果
		StringBuffer vfreeStr = new StringBuffer("");
		ArrayList alResultData = new ArrayList(); //结果
		HashMap inv = new HashMap();
		HashMap invxcl = new HashMap(); 
//		Map invfree = new HashMap();
		if(list.size()>0){
			for(int i =0;i<list.size();i++){
				Map map = (Map) list.get(i);
				String vfree1 = map.get("vfree1") == null ? "" : map.get(
						"vfree1").toString();
				if (i == (list.size() - 1)) {
					vfreeStr.append("'").append(vfree1).append("'");
				} else {
					vfreeStr.append("'").append(vfree1).append("',");
				}
				
				
				if(map.get("cinventoryid")!=null){
					String cwarehouseclassid = map.get("cwarehouseid")==null?"":map.get("cwarehouseid").toString();
					String cinventoryid = map.get("cinventoryid")==null?"":map.get("cinventoryid").toString();
 					String pk_corp1 = map.get("pk_corp")==null?"":map.get("pk_corp").toString();
					String cwarehousecode="";
					String cwarehousename="";
					String cinventorycode="";
					String invname="";
					String invspec="";
					String invtype="";
					String cinvbasid="";
					String measdocname=""; 
					String ccalbodycode = "";
					String ccalbodyname = "";
					String ccalbodyid = "";
		    		try {
		    			cwarehousecode = (String) ( HYPubBO_Client.findColValue("bd_stordoc","storcode","pk_stordoc='"+cwarehouseclassid+"'"));
		    			cwarehousename = (String) ( HYPubBO_Client.findColValue("bd_stordoc","storname","pk_stordoc='"+cwarehouseclassid+"'"));
		    			cinventorycode = (String) ( HYPubBO_Client.findColValue("bd_invbasdoc bas ,bd_invmandoc man ","bas.invcode","bas.pk_invbasdoc = man.pk_invbasdoc and man.pk_corp = '"+map.get("pk_corp")+"' and man.pk_invmandoc='"+cinventoryid+"'"));
		    			cinvbasid = (String) ( HYPubBO_Client.findColValue("bd_invmandoc  ","pk_invbasdoc"," pk_corp = '"+map.get("pk_corp")+"' and pk_invmandoc='"+cinventoryid+"'"));
		    			invname = (String) ( HYPubBO_Client.findColValue("bd_invbasdoc ","invname","pk_invbasdoc = '"+cinvbasid+"'"));
		    			invspec = (String) ( HYPubBO_Client.findColValue("bd_invbasdoc ","invspec","pk_invbasdoc = '"+cinvbasid+"'"));
		    			invtype = (String) ( HYPubBO_Client.findColValue("bd_invbasdoc ","invtype","pk_invbasdoc = '"+cinvbasid+"'"));
		    			measdocname = (String) ( HYPubBO_Client.findColValue("bd_invbasdoc bas,bd_measdoc mea","mea.measname ","bas.pk_measdoc =mea.pk_measdoc and bas.pk_invbasdoc = '"+cinvbasid+"'"));
		    			ccalbodyid = (String) ( HYPubBO_Client.findColValue("bd_calbody","pk_calbody","pk_corp = '"+pk_corp1+"'"));
		    			ccalbodycode = (String) ( HYPubBO_Client.findColValue("bd_calbody","bodycode","pk_calbody = '"+ccalbodyid+"'"));
		    			ccalbodyname = (String) ( HYPubBO_Client.findColValue("bd_calbody","bodyname","pk_calbody = '"+ccalbodyid+"'"));
		    			
		    			
		    		} catch (UifException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    		//add by wbp 注释：判断仓库、存货、vfree1是否有重复
					if(inv.containsKey(map.get("cinventoryid").toString()+map.get("cwarehouseid").toString()+map.get("vfree1").toString())){
						StockAgeItemVO vo = (StockAgeItemVO) inv.get(map.get("cinventoryid").toString()+map.get("cwarehouseid").toString()+map.get("vfree1").toString());
						UFDouble xcl =vo.getAttributeValue("xcl")==null?new UFDouble(0.0):new UFDouble(vo.getAttributeValue("xcl").toString());
						
						UFDouble num =map.get("num")==null?new UFDouble(0.0):new UFDouble(map.get("num").toString());
				//		Integer num =  map.get("num")==null?new Integer(0):(Integer)map.get("num");
//						Integer freezenum =  map.get("freezenum")==null?new Integer(0):(Integer)map.get("freezenum");
//						num = num+freezenum;
						num = num.add(xcl);
					//	num = num+Integer.parseInt(xcl.toString());
						vo.setAttributeValue("xcl",num);  
						inv.put(map.get("cinventoryid").toString()+map.get("cwarehouseid").toString()+map.get("vfree1").toString(), vo);
					}else{
						StockAgeItemVO pivo = new StockAgeItemVO();
						pivo.setPk_corp(map.get("pk_corp").toString());
						pivo.setAttributeValue("cwarehouseclassid", map.get("cwarehouseid"));
						pivo.setAttributeValue("cwarehousecode",cwarehousecode);
						pivo.setAttributeValue("cwarehousename",cwarehousename);
						pivo.setAttributeValue("cinventorycode",cinventorycode);
						pivo.setAttributeValue("invname",invname);
						pivo.setAttributeValue("invspec",invspec);
						pivo.setAttributeValue("invtype",invtype);
						pivo.setAttributeValue("cinvbasid",cinvbasid);
						pivo.setAttributeValue("measdocname",measdocname);  
						pivo.setAttributeValue("ccalbodycode", ccalbodycode);
						pivo.setAttributeValue("ccalbodyname", ccalbodyname);
						pivo.setAttributeValue("cinventoryid",map.get("cinventoryid").toString());
						
						double num = map.get("num")==null?0:Double.parseDouble(map.get("num").toString()); 
						pivo.setAttributeValue("xcl",num); 
						
						inv.put(map.get("cinventoryid").toString()+map.get("cwarehouseid").toString()+map.get("vfree1").toString(), pivo);
//						List vlist = new ArrayList();
//						vlist.add(map.get("vfree1"));
//						invfree.put(map.get("cinventoryid").toString()+map.get("cwarehouseid").toString()+map.get("vfree1").toString(), vlist);
					}
					// add by wbp 仓库和存货是否有重复
					if(invxcl.containsKey(map.get("cinventoryid").toString()+map.get("cwarehouseid").toString())){
						StockAgeItemVO vo = (StockAgeItemVO) invxcl.get(map.get("cinventoryid").toString()+map.get("cwarehouseid").toString());
						UFDouble xcl =vo.getAttributeValue("xcl")==null?new UFDouble(0.0):new UFDouble(vo.getAttributeValue("xcl").toString());
						
						UFDouble num =map.get("num")==null?new UFDouble(0.0):new UFDouble(map.get("num").toString());
					//	Integer num =  map.get("num")==null?new Integer(0):(Integer)map.get("num");
//						Integer freezenum =  map.get("freezenum")==null?new Integer(0):(Integer)map.get("freezenum");
//						num = num+freezenum;
						num = num.add(xcl);
					//	num = num+Integer.parseInt(xcl.toString());
						vo.setAttributeValue("xcl",num);  
						invxcl.put(map.get("cinventoryid").toString()+map.get("cwarehouseid").toString(), vo);
					}else{
						StockAgeItemVO pivo = new StockAgeItemVO();
						pivo.setPk_corp(map.get("pk_corp").toString());
						pivo.setAttributeValue("cwarehouseclassid", map.get("cwarehouseid"));
						pivo.setAttributeValue("cwarehousecode",cwarehousecode);
						pivo.setAttributeValue("cwarehousename",cwarehousename);
						pivo.setAttributeValue("cinventorycode",cinventorycode);
						pivo.setAttributeValue("invname",invname);
						pivo.setAttributeValue("invspec",invspec);
						pivo.setAttributeValue("invtype",invtype);
						pivo.setAttributeValue("cinvbasid",cinvbasid);
						pivo.setAttributeValue("measdocname",measdocname);  
						pivo.setAttributeValue("ccalbodycode", ccalbodycode);
						pivo.setAttributeValue("ccalbodyname", ccalbodyname);
						pivo.setAttributeValue("cinventoryid",map.get("cinventoryid").toString());
						
						UFDouble num =map.get("num")==null?new UFDouble(0.0):new UFDouble(map.get("num").toString());
				//		Integer num =  map.get("num")==null?new Integer(0):(Integer)map.get("num");
//						Integer freezenum =  map.get("freezenum")==null?new Integer(0):(Integer)map.get("freezenum");
//						num = num+freezenum;
						pivo.setAttributeValue("xcl",num); 
						invxcl.put(map.get("cinventoryid").toString()+map.get("cwarehouseid").toString(), pivo);
					}
					
				}
			}
		} 

		//将汇总结果封装至list，转换为数组

		HashMap invxclmap = new HashMap();
		invxclmap = clone(invxcl);//对象类型深层copy使用clone，否则会随着inv变化而产生变化

		HashMap map = new HashMap();
 		Iterator it = inv.entrySet().iterator();  
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
 			StockAgeItemVO svo = (StockAgeItemVO) entry.getValue();
 			String cinv = svo.getCinventorycode();
			Map mapday = getVfreeDays(cinv, vfreeStr.toString());  
			UFDouble price = getInvPrice(cinv);
			svo.setAttributeValue("nprice", price);
			if(mapday.containsKey(entry.getKey())){
				String day = mapday.get(entry.getKey())==null?"":(String)mapday.get(entry.getKey());
				if(!(day.length()<9)){
				
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				int days = 0;
				try {
					Date date1 = format.parse(day);
					Date date2 = format.parse(m_sLoginDate);
					days=new Long((date2.getTime()-date1.getTime())/(1000*60*60*24)).intValue();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				for(int m = 0;m<m_sKlsdday.length;m++){
					String vkey = svo.getCinventoryid()+svo.getCwarehouseclassid();
					if(m==0){
						int num = Integer.parseInt(m_sKlsdday[m]);
						if(days>=0&&days<=num){
							
							if(map.containsKey(vkey)){
								StockAgeItemVO xclvo = (StockAgeItemVO)map.get(vkey);
								Object obj  = xclvo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m);
								if(obj!=null){
									UFDouble xclnum = (UFDouble)obj;
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													xclnum.add(svo.getXcl())));
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(xclnum.add(svo.getXcl()))));
									/*//现存量金额= 现存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_planmny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													xclnum.add(svo.getXcl())));
									map.put(vkey, xclvo);*/
								}else{  
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													svo.getXcl())); 
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(svo.getXcl())));
									map.put(vkey, xclvo);
								}
								
							}else{
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
										svo.getXcl()));
								//库龄库存金额 = 库龄库存量*单价
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
												price.multiply(svo.getXcl())));
								map.put(vkey, svo);
							}
							break;
						}
						
						
					}else if(m==1){
						int num = Integer.parseInt(m_sKlsdday[m]);
						if(days>=15&&days<=num){
							if(map.containsKey(vkey)){
								StockAgeItemVO xclvo = (StockAgeItemVO)map.get(vkey);
								Object obj  = xclvo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m);
								if(obj!=null){
									UFDouble xclnum = (UFDouble)obj;
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													xclnum.add(svo.getXcl())));
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(xclnum.add(svo.getXcl()))));
									/*//现存量金额= 现存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_planmny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													xclnum.add(svo.getXcl())));
									map.put(vkey, xclvo);*/
								}else{  
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													svo.getXcl())); 
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(svo.getXcl())));
									map.put(vkey, xclvo);
								}
								
							}else{
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
										svo.getXcl()));
								//库龄库存金额 = 库龄库存量*单价
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
												price.multiply(svo.getXcl())));
								map.put(vkey, svo);
							}
							break;
						}
							
						
					}else if(m==2){
						int num = Integer.parseInt(m_sKlsdday[m]);
						if(days>=30&&days<=num){
							if(map.containsKey(vkey)){
								StockAgeItemVO xclvo = (StockAgeItemVO)map.get(vkey);
								Object obj  = xclvo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m);
								if(obj!=null){
									UFDouble xclnum = (UFDouble)obj;
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													xclnum.add(svo.getXcl())));
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(xclnum.add(svo.getXcl()))));
									/*//现存量金额= 现存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_planmny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													xclnum.add(svo.getXcl())));
									map.put(vkey, xclvo);*/
								}else{  
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													svo.getXcl())); 
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(svo.getXcl())));
									map.put(vkey, xclvo);
								}
								
							}else{
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
										svo.getXcl()));
								//库龄库存金额 = 库龄库存量*单价
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
												price.multiply(svo.getXcl())));
								map.put(vkey, svo);
							}
							break;
						}					
						
						
						
						
					}else if(m==3){
						int num = Integer.parseInt(m_sKlsdday[m]);
						if(days>=60&&days<=num){
							if(map.containsKey(vkey)){
								StockAgeItemVO xclvo = (StockAgeItemVO)map.get(vkey);
								Object obj  = xclvo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m);
								if(obj!=null){
									UFDouble xclnum = (UFDouble)obj;
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													xclnum.add(svo.getXcl())));
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(xclnum.add(svo.getXcl()))));
									/*//现存量金额= 现存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_planmny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													xclnum.add(svo.getXcl())));
									map.put(vkey, xclvo);*/
								}else{  
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													svo.getXcl())); 
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(svo.getXcl())));
									map.put(vkey, xclvo);
								}
								
							}else{
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
										svo.getXcl()));
								//库龄库存金额 = 库龄库存量*单价
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
												price.multiply(svo.getXcl())));
								map.put(vkey, svo);
							}
							break;
						}		
						
						
					}else if(m==4){
						int num = Integer.parseInt(m_sKlsdday[m]);
						if(days>=90&&days<=num){
							if(map.containsKey(vkey)){
								StockAgeItemVO xclvo = (StockAgeItemVO)map.get(vkey);
								Object obj  = xclvo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m);
								if(obj!=null){
									UFDouble xclnum = (UFDouble)obj;
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													xclnum.add(svo.getXcl())));
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(xclnum.add(svo.getXcl()))));
									/*//现存量金额= 现存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_planmny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													xclnum.add(svo.getXcl())));
									map.put(vkey, xclvo);*/
								}else{  
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													svo.getXcl())); 
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(svo.getXcl())));
									map.put(vkey, xclvo);
								}
								
							}else{
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
										svo.getXcl()));
								//库龄库存金额 = 库龄库存量*单价
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
												price.multiply(svo.getXcl())));
								map.put(vkey, svo);
							}
							break;
						}	
						
						
						
					}else if(m==5){
						int num = Integer.parseInt(m_sKlsdday[m]);
						if(days>=180&&days<=num){
							if(map.containsKey(vkey)){
								StockAgeItemVO xclvo = (StockAgeItemVO)map.get(vkey);
								Object obj  = xclvo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m);
								if(obj!=null){
									UFDouble xclnum = (UFDouble)obj;
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													xclnum.add(svo.getXcl())));
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(xclnum.add(svo.getXcl()))));
									/*//现存量金额= 现存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_planmny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													xclnum.add(svo.getXcl())));
									map.put(vkey, xclvo);*/
								}else{  
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													svo.getXcl())); 
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(svo.getXcl())));
									map.put(vkey, xclvo);
								}
								
							}else{
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
										svo.getXcl()));
								//库龄库存金额 = 库龄库存量*单价
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
												price.multiply(svo.getXcl())));
								map.put(vkey, svo);
							}
							break;
						}
						
						
						
					}else if(m==6){
						int num = Integer.parseInt(m_sKlsdday[m]);
						if(days>=360&&days<=num){
							if(map.containsKey(vkey)){
								StockAgeItemVO xclvo = (StockAgeItemVO)map.get(vkey);
								Object obj  = xclvo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m);
								if(obj!=null){
									UFDouble xclnum = (UFDouble)obj;
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													xclnum.add(svo.getXcl())));
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(xclnum.add(svo.getXcl()))));
									/*//现存量金额= 现存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_planmny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													xclnum.add(svo.getXcl())));
									map.put(vkey, xclvo);*/
								}else{  
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													svo.getXcl())); 
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(svo.getXcl())));
									map.put(vkey, xclvo);
								}
								
							}else{
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
										svo.getXcl()));
								//库龄库存金额 = 库龄库存量*单价
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
												price.multiply(svo.getXcl())));
								map.put(vkey, svo);
							}
							break;
						}
						
						
					}else if(m==7){
						int num = Integer.parseInt(m_sKlsdday[m]);
						if(days>=540&&days<=num){
							if(map.containsKey(vkey)){
								StockAgeItemVO xclvo = (StockAgeItemVO)map.get(vkey);
								Object obj  = xclvo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m);
								if(obj!=null){
									UFDouble xclnum = (UFDouble)obj;
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													xclnum.add(svo.getXcl())));
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(xclnum.add(svo.getXcl()))));
									/*//现存量金额= 现存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_planmny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													xclnum.add(svo.getXcl())));
									map.put(vkey, xclvo);*/
								}else{  
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													svo.getXcl())); 
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(svo.getXcl())));
									map.put(vkey, xclvo);
								}
								
							}else{
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
										svo.getXcl()));
								//库龄库存金额 = 库龄库存量*单价
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
												price.multiply(svo.getXcl())));
								map.put(vkey, svo);
							}
							break;
						}
						
						
					}else if(m==8){
						int num = Integer.parseInt(m_sKlsdday[m]);
						if(days>=720&&days<=num){
							if(map.containsKey(vkey)){
								StockAgeItemVO xclvo = (StockAgeItemVO)map.get(vkey);
								Object obj  = xclvo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m);
								if(obj!=null){
									UFDouble xclnum = (UFDouble)obj;
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													xclnum.add(svo.getXcl())));
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(xclnum.add(svo.getXcl()))));
									/*//现存量金额= 现存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_planmny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													xclnum.add(svo.getXcl())));
									map.put(vkey, xclvo);*/
								}else{  
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													svo.getXcl())); 
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(svo.getXcl())));
									map.put(vkey, xclvo);
								}
								
							}else{
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
										svo.getXcl()));
								//库龄库存金额 = 库龄库存量*单价
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
												price.multiply(svo.getXcl())));
								map.put(vkey, svo);
							}
							break;
						}
						
						
					}else if(m==9){
						int num = Integer.parseInt(m_sKlsdday[m]);
						if(days>=1080&&days<=num){
							if(map.containsKey(vkey)){
								StockAgeItemVO xclvo = (StockAgeItemVO)map.get(vkey);
								Object obj  = xclvo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m);
								if(obj!=null){
									UFDouble xclnum = (UFDouble)obj;
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													xclnum.add(svo.getXcl())));
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(xclnum.add(svo.getXcl()))));
									/*//现存量金额= 现存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_planmny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													xclnum.add(svo.getXcl())));
									map.put(vkey, xclvo);*/
								}else{  
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													svo.getXcl())); 
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(svo.getXcl())));
									map.put(vkey, xclvo);
								}
								
							}else{
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
										svo.getXcl()));
								//库龄库存金额 = 库龄库存量*单价
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
												price.multiply(svo.getXcl())));
								map.put(vkey, svo);
							}
							break;
						}
						
						
					}else if(m==m_sKlsdday.length-1){
						int num = Integer.parseInt(m_sKlsdday[m]);
						if(days>=num){
							if(map.containsKey(vkey)){
								StockAgeItemVO xclvo = (StockAgeItemVO)map.get(vkey);
								Object obj  = xclvo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m);
								if(obj!=null){
									UFDouble xclnum = (UFDouble)obj;
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													xclnum.add(svo.getXcl())));
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(xclnum.add(svo.getXcl()))));
									/*//现存量金额= 现存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_planmny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													xclnum.add(svo.getXcl())));
									map.put(vkey, xclvo);*/
								}else{  
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													svo.getXcl())); 
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(svo.getXcl())));
									map.put(vkey, xclvo);
								}
								
							}else{
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
										svo.getXcl()));
								//库龄库存金额 = 库龄库存量*单价
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
												price.multiply(svo.getXcl())));
								map.put(vkey, svo);
							}
							break;
						}
					}else{
						int num = Integer.parseInt(m_sKlsdday[m]);
						int numbig = Integer.parseInt(m_sKlsdday[m+1]);
						if(days>num&&days<=numbig){
							m=m+1;
							if(map.containsKey(vkey)){
								StockAgeItemVO xclvo = (StockAgeItemVO)map.get(vkey);
								Object obj  = xclvo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m);
								if(obj!=null){
									UFDouble xclnum = (UFDouble)obj;
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													xclnum.add(svo.getXcl())));
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(xclnum.add(svo.getXcl()))));
									/*//现存量金额= 现存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_planmny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													xclnum.add(svo.getXcl())));
									map.put(vkey, xclvo);*/
								}else{  
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													svo.getXcl())); 
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(svo.getXcl())));
									map.put(vkey, xclvo);
								}
								
							}else{
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
										svo.getXcl()));
								//库龄库存金额 = 库龄库存量*单价
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
												price.multiply(svo.getXcl())));
								map.put(vkey, svo);
							}
							break;
						}
						
					}
				} 
			  }
			}
		}
		
		Iterator itmap = map.entrySet().iterator();
		List listinv = new ArrayList();
		while (itmap.hasNext()) {
			Map.Entry entry = (Map.Entry) itmap.next();
			StockAgeItemVO value = (StockAgeItemVO) entry.getValue();
			if(invxclmap.containsKey(entry.getKey())){
				StockAgeItemVO vo = (StockAgeItemVO)invxclmap.get(entry.getKey());
				value.setXcl(vo.getXcl());
			}
			listinv.add(value);
		}
		StockAgeItemVO[] storvo =(StockAgeItemVO[]) listinv.toArray(new StockAgeItemVO[listinv.size()]);  
		//表体
		m_tvoStockAgeVO.setChildrenVO(storvo);
		return (StockAgeItemVO[]) m_tvoStockAgeVO.getChildrenVO();
//		getReportBaseClass().setBodyDataVO(m_tvoStockAgeVO.getChildrenVO());
	}
	
	//获取成品库龄
	public StockAgeItemVO[] getCpKl(String invcode){
		String pk_corp = getClientEnvironment().getCorporation().getPk_corp();
		String firstdate = m_sLoginDate.substring(0,7)+"-01";
		String ymdate = new UFDate(firstdate).getDateBefore(1).toString().substring(0,7);
		ArrayList list = getOnhandnum(invcode,pk_corp,ymdate,firstdate,m_sLoginDate);//现存量结果
		StringBuffer vfreeStr = new StringBuffer("");
		ArrayList alResultData = new ArrayList(); //结果
 		HashMap inv = new HashMap();
		HashMap invxcl = new HashMap();
		if(list.size()>0){
			for(int i =0;i<list.size();i++){
				Map map = (Map) list.get(i);
				String vfree1 = map.get("vfree1") == null ? "" : map.get(
						"vfree1").toString();
				if (i == (list.size() - 1)) {
					vfreeStr.append("'").append(vfree1).append("'");
				} else {
					vfreeStr.append("'").append(vfree1).append("',");
				}
				
				
				if(map.get("cinventoryid")!=null){
					String cwarehouseclassid = map.get("cwarehouseid")==null?"":map.get("cwarehouseid").toString();
					String cinventoryid = map.get("cinventoryid")==null?"":map.get("cinventoryid").toString();
 					String pk_corp1 = map.get("pk_corp")==null?"":map.get("pk_corp").toString();
					String cwarehousecode="";
					String cwarehousename="";
					String cinventorycode="";
					String invname="";
					String invspec="";
					String invtype="";
					String cinvbasid="";
					String measdocname=""; 
					String ccalbodycode = "";
					String ccalbodyname = "";
					String ccalbodyid = "";
		    		try {
		    			cwarehousecode = (String) ( HYPubBO_Client.findColValue("bd_stordoc","storcode","pk_stordoc='"+cwarehouseclassid+"'"));
		    			cwarehousename = (String) ( HYPubBO_Client.findColValue("bd_stordoc","storname","pk_stordoc='"+cwarehouseclassid+"'"));
		    			cinventorycode = (String) ( HYPubBO_Client.findColValue("bd_invbasdoc bas ,bd_invmandoc man ","bas.invcode","bas.pk_invbasdoc = man.pk_invbasdoc and man.pk_corp = '"+map.get("pk_corp")+"' and man.pk_invmandoc='"+cinventoryid+"'"));
		    			cinvbasid = (String) ( HYPubBO_Client.findColValue("bd_invmandoc  ","pk_invbasdoc"," pk_corp = '"+map.get("pk_corp")+"' and pk_invmandoc='"+cinventoryid+"'"));
		    			invname = (String) ( HYPubBO_Client.findColValue("bd_invbasdoc ","invname","pk_invbasdoc = '"+cinvbasid+"'"));
		    			invspec = (String) ( HYPubBO_Client.findColValue("bd_invbasdoc ","invspec","pk_invbasdoc = '"+cinvbasid+"'"));
		    			invtype = (String) ( HYPubBO_Client.findColValue("bd_invbasdoc ","invtype","pk_invbasdoc = '"+cinvbasid+"'"));
		    			measdocname = (String) ( HYPubBO_Client.findColValue("bd_invbasdoc bas,bd_measdoc mea","mea.measname ","bas.pk_measdoc =mea.pk_measdoc and bas.pk_invbasdoc = '"+cinvbasid+"'"));
		    			ccalbodyid = (String) ( HYPubBO_Client.findColValue("bd_calbody","pk_calbody","pk_corp = '"+pk_corp1+"'"));
		    			ccalbodycode = (String) ( HYPubBO_Client.findColValue("bd_calbody","bodycode","pk_calbody = '"+ccalbodyid+"'"));
		    			ccalbodyname = (String) ( HYPubBO_Client.findColValue("bd_calbody","bodyname","pk_calbody = '"+ccalbodyid+"'"));
		    			
		    			
		    		} catch (UifException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(inv.containsKey(map.get("cinventoryid").toString()+map.get("cwarehouseid").toString()+map.get("vfree1").toString())){
						StockAgeItemVO vo = (StockAgeItemVO) inv.get(map.get("cinventoryid").toString()+map.get("cwarehouseid").toString()+map.get("vfree1").toString());
						UFDouble xcl =  vo.getAttributeValue("xcl")==null?new UFDouble(0.0):new UFDouble(vo.getAttributeValue("xcl").toString());
						UFDouble num =map.get("num")==null?new UFDouble(0.0):new UFDouble(map.get("num").toString());
					//	Integer num =  map.get("num")==null?new Integer(0):(Integer)map.get("num");
//						Integer freezenum =  map.get("freezenum")==null?new Integer(0):(Integer)map.get("freezenum");
//						num = num+freezenum;
						num = num.add(xcl);
					//	num = num+Integer.parseInt(xcl.toString());
						vo.setAttributeValue("xcl",num);  
						inv.put(map.get("cinventoryid").toString()+map.get("cwarehouseid").toString()+map.get("vfree1").toString(), vo);
					}else{
						StockAgeItemVO pivo = new StockAgeItemVO();
						pivo.setPk_corp(map.get("pk_corp").toString());
						pivo.setAttributeValue("cwarehouseclassid", map.get("cwarehouseid"));
						pivo.setAttributeValue("cwarehousecode",cwarehousecode);
						pivo.setAttributeValue("cwarehousename",cwarehousename);
						pivo.setAttributeValue("cinventorycode",cinventorycode);
						pivo.setAttributeValue("invname",invname);
						pivo.setAttributeValue("invspec",invspec);
						pivo.setAttributeValue("invtype",invtype);
						pivo.setAttributeValue("cinvbasid",cinvbasid);
						pivo.setAttributeValue("measdocname",measdocname);  
						pivo.setAttributeValue("ccalbodycode", ccalbodycode);
						pivo.setAttributeValue("ccalbodyname", ccalbodyname);
						pivo.setAttributeValue("cinventoryid",map.get("cinventoryid").toString());
						
						UFDouble num =map.get("num")==null?new UFDouble(0.0):new UFDouble(map.get("num").toString());
				//		Integer num =  map.get("num")==null?new Integer(0):(Integer)map.get("num");
//						Integer freezenum =  map.get("freezenum")==null?new Integer(0):(Integer)map.get("freezenum");
//						num = num+freezenum;
						pivo.setAttributeValue("xcl",num); 
						inv.put(map.get("cinventoryid").toString()+map.get("cwarehouseid").toString()+map.get("vfree1").toString(), pivo);
					}
					
					if(invxcl.containsKey(map.get("cinventoryid").toString()+map.get("cwarehouseid").toString())){
						StockAgeItemVO vo = (StockAgeItemVO) invxcl.get(map.get("cinventoryid").toString()+map.get("cwarehouseid").toString());
						UFDouble xcl =vo.getAttributeValue("xcl")==null?new UFDouble(0.0):new UFDouble(vo.getAttributeValue("xcl").toString());
						UFDouble num =map.get("num")==null?new UFDouble(0.0):new UFDouble(map.get("num").toString());
					//	Integer num =  map.get("num")==null?new Integer(0):(Integer)map.get("num");
//						Integer freezenum =  map.get("freezenum")==null?new Integer(0):(Integer)map.get("freezenum");
//						num = num+freezenum;
						num = num.add(xcl);
					//	num = num+Integer.parseInt(xcl.toString());
						vo.setAttributeValue("xcl",num);  
						invxcl.put(map.get("cinventoryid").toString()+map.get("cwarehouseid").toString(), vo);
					}else{
						StockAgeItemVO pivo = new StockAgeItemVO();
						pivo.setPk_corp(map.get("pk_corp").toString());
						pivo.setAttributeValue("cwarehouseclassid", map.get("cwarehouseid"));
						pivo.setAttributeValue("cwarehousecode",cwarehousecode);
						pivo.setAttributeValue("cwarehousename",cwarehousename);
						pivo.setAttributeValue("cinventorycode",cinventorycode);
						pivo.setAttributeValue("invname",invname);
						pivo.setAttributeValue("invspec",invspec);
						pivo.setAttributeValue("invtype",invtype);
						pivo.setAttributeValue("cinvbasid",cinvbasid);
						pivo.setAttributeValue("measdocname",measdocname);  
						pivo.setAttributeValue("ccalbodycode", ccalbodycode);
						pivo.setAttributeValue("ccalbodyname", ccalbodyname);
						pivo.setAttributeValue("cinventoryid",map.get("cinventoryid").toString());
						
						
						UFDouble num =map.get("num")==null?new UFDouble(0.0):new UFDouble(map.get("num").toString());
			//			Integer num =  map.get("num")==null?new Integer(0):(Integer)map.get("num");
//						Integer freezenum =  map.get("freezenum")==null?new Integer(0):(Integer)map.get("freezenum");
//						num = num+freezenum;
						pivo.setAttributeValue("xcl",num); 
						invxcl.put(map.get("cinventoryid").toString()+map.get("cwarehouseid").toString(), pivo);
					}
					
				}
			}
		}
		
		//将汇总结果封装至list，转换为数组
		HashMap invxclmap = new HashMap();
		invxclmap = clone(invxcl);//对象类型深层copy使用clone，否则会随着inv变化而产生变化

		HashMap map = new HashMap();
 		Iterator it = inv.entrySet().iterator();  
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
 			StockAgeItemVO svo = (StockAgeItemVO) entry.getValue();
 			String cinv = svo.getCinventorycode();
			UFDouble price = getInvPrice(cinv);
			svo.setAttributeValue("nprice", price);
			Map mapday = getVfreeDays(cinv, vfreeStr.toString());  
			if(mapday.containsKey(entry.getKey())){
				String day = mapday.get(entry.getKey())==null?"":(String)mapday.get(entry.getKey());
				if(!(day.length()<9)){
				
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				int days = 0;
				try {
					Date date1 = format.parse(day);
					Date date2 = format.parse(m_sLoginDate);
					days=new Long((date2.getTime()-date1.getTime())/(1000*60*60*24)).intValue();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				for(int m = 0;m<m_sKlsdday.length;m++){
					String vkey = svo.getCinventoryid()+svo.getCwarehouseclassid();
					if(m==0){
						int num = Integer.parseInt(m_sKlsdday[m]);
						if(days>=0&&days<=num){
							
							if(map.containsKey(vkey)){
								StockAgeItemVO xclvo = (StockAgeItemVO)map.get(vkey);
								Object obj  = xclvo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m);
								if(obj!=null){
									UFDouble xclnum = (UFDouble)obj;
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													xclnum.add(svo.getXcl())));
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(xclnum.add(svo.getXcl()))));
									/*//现存量金额= 现存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_planmny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													xclnum.add(svo.getXcl())));
									map.put(vkey, xclvo);*/
								}else{  
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													svo.getXcl())); 
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(svo.getXcl())));
									map.put(vkey, xclvo);
								}
								
							}else{
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
										svo.getXcl()));
								//库龄库存金额 = 库龄库存量*单价
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
												price.multiply(svo.getXcl())));
								map.put(vkey, svo);
							}
							break;
						}
						
						
						
					}else if(m==1){
						int num = Integer.parseInt(m_sKlsdday[m]);
						if(days>=15&&days<=num){
							if(map.containsKey(vkey)){
								StockAgeItemVO xclvo = (StockAgeItemVO)map.get(vkey);
								Object obj  = xclvo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m);
								if(obj!=null){
									UFDouble xclnum = (UFDouble)obj;
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													xclnum.add(svo.getXcl())));
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(xclnum.add(svo.getXcl()))));
									/*//现存量金额= 现存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_planmny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													xclnum.add(svo.getXcl())));
									map.put(vkey, xclvo);*/
								}else{  
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													svo.getXcl())); 
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(svo.getXcl())));
									map.put(vkey, xclvo);
								}
								
							}else{
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
										svo.getXcl()));
								//库龄库存金额 = 库龄库存量*单价
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
												price.multiply(svo.getXcl())));
								map.put(vkey, svo);
							}
							break;
						}
							
						
					}else if(m==2){
						int num = Integer.parseInt(m_sKlsdday[m]);
						if(days>=30&&days<=num){
							if(map.containsKey(vkey)){
								StockAgeItemVO xclvo = (StockAgeItemVO)map.get(vkey);
								Object obj  = xclvo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m);
								if(obj!=null){
									UFDouble xclnum = (UFDouble)obj;
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													xclnum.add(svo.getXcl())));
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(xclnum.add(svo.getXcl()))));
									/*//现存量金额= 现存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_planmny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													xclnum.add(svo.getXcl())));
									map.put(vkey, xclvo);*/
								}else{  
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													svo.getXcl())); 
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(svo.getXcl())));
									map.put(vkey, xclvo);
								}
								
							}else{
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
										svo.getXcl()));
								//库龄库存金额 = 库龄库存量*单价
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
												price.multiply(svo.getXcl())));
								map.put(vkey, svo);
							}
							break;
						}					
						
						
						
						
					}else if(m==3){
						int num = Integer.parseInt(m_sKlsdday[m]);
						if(days>=60&&days<=num){
							if(map.containsKey(vkey)){
								StockAgeItemVO xclvo = (StockAgeItemVO)map.get(vkey);
								Object obj  = xclvo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m);
								if(obj!=null){
									UFDouble xclnum = (UFDouble)obj;
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													xclnum.add(svo.getXcl())));
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(xclnum.add(svo.getXcl()))));
									/*//现存量金额= 现存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_planmny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													xclnum.add(svo.getXcl())));
									map.put(vkey, xclvo);*/
								}else{  
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													svo.getXcl())); 
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(svo.getXcl())));
									map.put(vkey, xclvo);
								}
								
							}else{
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
										svo.getXcl()));
								//库龄库存金额 = 库龄库存量*单价
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
												price.multiply(svo.getXcl())));
								map.put(vkey, svo);
							}
							break;
						}		
						
						
					}else if(m==4){
						int num = Integer.parseInt(m_sKlsdday[m]);
						if(days>=90&&days<=num){
							if(map.containsKey(vkey)){
								StockAgeItemVO xclvo = (StockAgeItemVO)map.get(vkey);
								Object obj  = xclvo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m);
								if(obj!=null){
									UFDouble xclnum = (UFDouble)obj;
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													xclnum.add(svo.getXcl())));
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(xclnum.add(svo.getXcl()))));
									/*//现存量金额= 现存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_planmny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													xclnum.add(svo.getXcl())));
									map.put(vkey, xclvo);*/
								}else{  
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													svo.getXcl())); 
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(svo.getXcl())));
									map.put(vkey, xclvo);
								}
								
							}else{
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
										svo.getXcl()));
								//库龄库存金额 = 库龄库存量*单价
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
												price.multiply(svo.getXcl())));
								map.put(vkey, svo);
							}
							break;
						}	
						
						
						
					}else if(m==5){
						int num = Integer.parseInt(m_sKlsdday[m]);
						if(days>=180&&days<=num){
							if(map.containsKey(vkey)){
								StockAgeItemVO xclvo = (StockAgeItemVO)map.get(vkey);
								Object obj  = xclvo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m);
								if(obj!=null){
									UFDouble xclnum = (UFDouble)obj;
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													xclnum.add(svo.getXcl())));
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(xclnum.add(svo.getXcl()))));
									/*//现存量金额= 现存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_planmny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													xclnum.add(svo.getXcl())));
									map.put(vkey, xclvo);*/
								}else{  
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													svo.getXcl())); 
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(svo.getXcl())));
									map.put(vkey, xclvo);
								}
								
							}else{
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
										svo.getXcl()));
								//库龄库存金额 = 库龄库存量*单价
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
												price.multiply(svo.getXcl())));
								map.put(vkey, svo);
							}
							break;
						}
						
						
						
					}else if(m==6){
						int num = Integer.parseInt(m_sKlsdday[m]);
						if(days>=360&&days<=num){
							if(map.containsKey(vkey)){
								StockAgeItemVO xclvo = (StockAgeItemVO)map.get(vkey);
								Object obj  = xclvo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m);
								if(obj!=null){
									UFDouble xclnum = (UFDouble)obj;
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													xclnum.add(svo.getXcl())));
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(xclnum.add(svo.getXcl()))));
									/*//现存量金额= 现存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_planmny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													xclnum.add(svo.getXcl())));
									map.put(vkey, xclvo);*/
								}else{  
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													svo.getXcl())); 
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(svo.getXcl())));
									map.put(vkey, xclvo);
								}
								
							}else{
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
										svo.getXcl()));
								//库龄库存金额 = 库龄库存量*单价
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
												price.multiply(svo.getXcl())));
								map.put(vkey, svo);
							}
							break;
						}
						
						
					}else if(m==7){
						int num = Integer.parseInt(m_sKlsdday[m]);
						if(days>=540&&days<=num){
							if(map.containsKey(vkey)){
								StockAgeItemVO xclvo = (StockAgeItemVO)map.get(vkey);
								Object obj  = xclvo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m);
								if(obj!=null){
									UFDouble xclnum = (UFDouble)obj;
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													xclnum.add(svo.getXcl())));
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(xclnum.add(svo.getXcl()))));
									/*//现存量金额= 现存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_planmny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													xclnum.add(svo.getXcl())));
									map.put(vkey, xclvo);*/
								}else{  
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													svo.getXcl())); 
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(svo.getXcl())));
									map.put(vkey, xclvo);
								}
								
							}else{
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
										svo.getXcl()));
								//库龄库存金额 = 库龄库存量*单价
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
												price.multiply(svo.getXcl())));
								map.put(vkey, svo);
							}
							break;
						}
						
						
					}else if(m==8){
						int num = Integer.parseInt(m_sKlsdday[m]);
						if(days>=720&&days<=num){
							if(map.containsKey(vkey)){
								StockAgeItemVO xclvo = (StockAgeItemVO)map.get(vkey);
								Object obj  = xclvo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m);
								if(obj!=null){
									UFDouble xclnum = (UFDouble)obj;
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													xclnum.add(svo.getXcl())));
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(xclnum.add(svo.getXcl()))));
									/*//现存量金额= 现存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_planmny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													xclnum.add(svo.getXcl())));
									map.put(vkey, xclvo);*/
								}else{  
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													svo.getXcl())); 
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(svo.getXcl())));
									map.put(vkey, xclvo);
								}
								
							}else{
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
										svo.getXcl()));
								//库龄库存金额 = 库龄库存量*单价
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
												price.multiply(svo.getXcl())));
								map.put(vkey, svo);
							}
							break;
						}
						
						
					}else if(m==9){
						int num = Integer.parseInt(m_sKlsdday[m]);
						if(days>=1080&&days<=num){
							if(map.containsKey(vkey)){
								StockAgeItemVO xclvo = (StockAgeItemVO)map.get(vkey);
								Object obj  = xclvo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m);
								if(obj!=null){
									UFDouble xclnum = (UFDouble)obj;
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													xclnum.add(svo.getXcl())));
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(xclnum.add(svo.getXcl()))));
									/*//现存量金额= 现存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_planmny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													xclnum.add(svo.getXcl())));
									map.put(vkey, xclvo);*/
								}else{  
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													svo.getXcl())); 
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(svo.getXcl())));
									map.put(vkey, xclvo);
								}
								
							}else{
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
										svo.getXcl()));
								//库龄库存金额 = 库龄库存量*单价
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
												price.multiply(svo.getXcl())));
								map.put(vkey, svo);
							}
							break;
						}
						
				
					}else if(m==m_sKlsdday.length-1){
						int num = Integer.parseInt(m_sKlsdday[m]);
						if(days>=num){
							if(map.containsKey(vkey)){
								StockAgeItemVO xclvo = (StockAgeItemVO)map.get(vkey);
								Object obj  = xclvo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m);
								if(obj!=null){
									UFDouble xclnum = (UFDouble)obj;
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													xclnum.add(svo.getXcl())));
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(xclnum.add(svo.getXcl()))));
									/*//现存量金额= 现存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_planmny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													xclnum.add(svo.getXcl())));
									map.put(vkey, xclvo);*/
								}else{  
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													svo.getXcl())); 
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(svo.getXcl())));
									map.put(vkey, xclvo);
								}
								
							}else{
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
										svo.getXcl()));
								//库龄库存金额 = 库龄库存量*单价
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
												price.multiply(svo.getXcl())));
								map.put(vkey, svo);
							}
							break;
						}
					}else{
						int num = Integer.parseInt(m_sKlsdday[m]);
						int numbig = Integer.parseInt(m_sKlsdday[m+1]);
						if(days>num&&days<=numbig){
							m=m+1;
							if(map.containsKey(vkey)){
								StockAgeItemVO xclvo = (StockAgeItemVO)map.get(vkey);
								Object obj  = xclvo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m);
								if(obj!=null){
									UFDouble xclnum = (UFDouble)obj;
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													xclnum.add(svo.getXcl())));
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(xclnum.add(svo.getXcl()))));
									/*//现存量金额= 现存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_planmny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													xclnum.add(svo.getXcl())));
									map.put(vkey, xclvo);*/
								}else{  
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													svo.getXcl())); 
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(svo.getXcl())));
									map.put(vkey, xclvo);
								}
								
							}else{
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
										svo.getXcl()));
								//库龄库存金额 = 库龄库存量*单价
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
												price.multiply(svo.getXcl())));
								map.put(vkey, svo);
							}
							break;
						}
						
					}
				} 
			  }
			}
		}
		
		Iterator itmap = map.entrySet().iterator();
		List listinv = new ArrayList();
		while (itmap.hasNext()) {
			Map.Entry entry = (Map.Entry) itmap.next();
			StockAgeItemVO value = (StockAgeItemVO) entry.getValue();
			if(invxclmap.containsKey(entry.getKey())){
				StockAgeItemVO vo = (StockAgeItemVO)invxclmap.get(entry.getKey());
				value.setXcl(vo.getXcl());
			}
			listinv.add(value);
		}
		StockAgeItemVO[] storvo =(StockAgeItemVO[]) listinv.toArray(new StockAgeItemVO[listinv.size()]); 
		
		//表体
		m_tvoStockAgeVO.setChildrenVO(storvo);
		return (StockAgeItemVO[]) m_tvoStockAgeVO.getChildrenVO();
//		getReportBaseClass().setBodyDataVO(m_tvoStockAgeVO.getChildrenVO());
	
	}
	
	//其余库龄：启用批次
	public StockAgeItemVO[] getTrueKl(String invcode){

		String pk_corp = getClientEnvironment().getCorporation().getPk_corp();
		String firstdate = m_sLoginDate.substring(0,7)+"-01";
		String ymdate = new UFDate(firstdate).getDateBefore(1).toString().substring(0,7);
		ArrayList list = getOnhandnum(invcode,pk_corp,ymdate,firstdate,m_sLoginDate);//现存量结果
		StringBuffer vbatStr = new StringBuffer("");
		ArrayList alResultData = new ArrayList(); //结果
		Map inv = new HashMap();
		HashMap invxcl = new HashMap(); 
		if(list.size()>0){
			for(int i =0;i<list.size();i++){
				Map map = (Map) list.get(i);
				String vbatchcode = map.get("vbatchcode") == null ? "" : map.get(
						"vbatchcode").toString();
				if (i == (list.size() - 1)) {
					vbatStr.append("'").append(vbatchcode).append("'");
				} else {
					vbatStr.append("'").append(vbatchcode).append("',");
				}
				
				
				if(map.get("cinventoryid")!=null){
					String cwarehouseclassid = map.get("cwarehouseid")==null?"":map.get("cwarehouseid").toString();
					String cinventoryid = map.get("cinventoryid")==null?"":map.get("cinventoryid").toString();
					String ccalbodyid = map.get("ccalbodyid")==null?"":map.get("ccalbodyid").toString();
					String cwarehousecode="";
					String cwarehousename="";
					String cinventorycode="";
					String invname="";
					String invspec="";
					String invtype="";
					String cinvbasid="";
					String measdocname=""; 
					String ccalbodycode = "";
					String ccalbodyname = "";
		    		try {
		    			cwarehousecode = (String) ( HYPubBO_Client.findColValue("bd_stordoc","storcode","pk_stordoc='"+cwarehouseclassid+"'"));
		    			cwarehousename = (String) ( HYPubBO_Client.findColValue("bd_stordoc","storname","pk_stordoc='"+cwarehouseclassid+"'"));
		    			cinventorycode = (String) ( HYPubBO_Client.findColValue("bd_invbasdoc bas ,bd_invmandoc man ","bas.invcode","bas.pk_invbasdoc = man.pk_invbasdoc and man.pk_corp = '"+map.get("pk_corp")+"' and man.pk_invmandoc='"+cinventoryid+"'"));
		    			cinvbasid = (String) ( HYPubBO_Client.findColValue("bd_invmandoc  ","pk_invbasdoc"," pk_corp = '"+map.get("pk_corp")+"' and pk_invmandoc='"+cinventoryid+"'"));
		    			invname = (String) ( HYPubBO_Client.findColValue("bd_invbasdoc ","invname","pk_invbasdoc = '"+cinvbasid+"'"));
		    			invspec = (String) ( HYPubBO_Client.findColValue("bd_invbasdoc ","invspec","pk_invbasdoc = '"+cinvbasid+"'"));
		    			invtype = (String) ( HYPubBO_Client.findColValue("bd_invbasdoc ","invtype","pk_invbasdoc = '"+cinvbasid+"'"));
		    			measdocname = (String) ( HYPubBO_Client.findColValue("bd_invbasdoc bas,bd_measdoc mea","mea.measname ","bas.pk_measdoc =mea.pk_measdoc and bas.pk_invbasdoc = '"+cinvbasid+"'"));
		    			ccalbodycode = (String) ( HYPubBO_Client.findColValue("bd_calbody","bodycode","pk_calbody = '"+ccalbodyid+"'"));
		    			ccalbodyname = (String) ( HYPubBO_Client.findColValue("bd_calbody","bodyname","pk_calbody = '"+ccalbodyid+"'"));
		    			
		    			
		    		} catch (UifException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(inv.containsKey(map.get("cinventoryid").toString()+map.get("cwarehouseid").toString()+map.get("vbatchcode").toString())){
						StockAgeItemVO vo = (StockAgeItemVO) inv.get(map.get("cinventoryid").toString()+map.get("cwarehouseid").toString());
						UFDouble xcl =vo.getAttributeValue("xcl")==null?new UFDouble(0.0):new UFDouble(vo.getAttributeValue("xcl").toString());
						Double num =  map.get("num")==null?0:Double.parseDouble(String.valueOf(map.get("num")));
//						Integer freezenum =  map.get("freezenum")==null?new Integer(0):(Integer)map.get("freezenum");
//						num = num+freezenum;
						num = num+Double.parseDouble(xcl.toString());
						vo.setAttributeValue("xcl",num);  
						inv.put(map.get("cinventoryid").toString()+map.get("cwarehouseid").toString()+map.get("vbatchcode").toString(), vo);
					}else{
						StockAgeItemVO pivo = new StockAgeItemVO();
						pivo.setPk_corp(map.get("pk_corp").toString());
						pivo.setAttributeValue("cwarehouseclassid", map.get("cwarehouseid"));
						pivo.setAttributeValue("cwarehousecode",cwarehousecode);
						pivo.setAttributeValue("cwarehousename",cwarehousename);
						pivo.setAttributeValue("cinventorycode",cinventorycode);
						pivo.setAttributeValue("invname",invname);
						pivo.setAttributeValue("invspec",invspec);
						pivo.setAttributeValue("invtype",invtype);
						pivo.setAttributeValue("cinvbasid",cinvbasid);
						pivo.setAttributeValue("measdocname",measdocname);  
						pivo.setAttributeValue("ccalbodycode", ccalbodycode);
						pivo.setAttributeValue("ccalbodyname", ccalbodyname);
						pivo.setAttributeValue("cinventoryid",map.get("cinventoryid").toString());
						
						Double num =  map.get("num")==null?0:Double.parseDouble(String.valueOf(map.get("num")));
//						Integer freezenum =  map.get("freezenum")==null?new Integer(0):(Integer)map.get("freezenum");
//						num = num+freezenum;
						pivo.setAttributeValue("xcl",num); 
						inv.put(map.get("cinventoryid").toString()+map.get("cwarehouseid").toString()+map.get("vbatchcode").toString(), pivo);
					}
					
					
					if(invxcl.containsKey(map.get("cinventoryid").toString()+map.get("cwarehouseid").toString())){
						StockAgeItemVO vo = (StockAgeItemVO) invxcl.get(map.get("cinventoryid").toString()+map.get("cwarehouseid").toString());
						UFDouble xcl =vo.getAttributeValue("xcl")==null?new UFDouble(0.0):new UFDouble(vo.getAttributeValue("xcl").toString());
						Double num = map.get("num")==null?0:Double.parseDouble(map.get("num").toString());
//						Integer freezenum =  map.get("freezenum")==null?new Integer(0):(Integer)map.get("freezenum");
//						num = num+freezenum;
						num = num+Double.parseDouble(xcl.toString());
						vo.setAttributeValue("xcl",num);  
						invxcl.put(map.get("cinventoryid").toString()+map.get("cwarehouseid").toString(), vo);
					}else{
						StockAgeItemVO pivo = new StockAgeItemVO();
						pivo.setPk_corp(map.get("pk_corp").toString());
						pivo.setAttributeValue("cwarehouseclassid", map.get("cwarehouseid"));
						pivo.setAttributeValue("cwarehousecode",cwarehousecode);
						pivo.setAttributeValue("cwarehousename",cwarehousename);
						pivo.setAttributeValue("cinventorycode",cinventorycode);
						pivo.setAttributeValue("invname",invname);
						pivo.setAttributeValue("invspec",invspec);
						pivo.setAttributeValue("invtype",invtype);
						pivo.setAttributeValue("cinvbasid",cinvbasid);
						pivo.setAttributeValue("measdocname",measdocname);  
						pivo.setAttributeValue("ccalbodycode", ccalbodycode);
						pivo.setAttributeValue("ccalbodyname", ccalbodyname);
						pivo.setAttributeValue("cinventoryid",map.get("cinventoryid").toString());
						
						Double num =  map.get("num")==null?0:Double.parseDouble(String.valueOf(map.get("num")));
//						Integer freezenum =  map.get("freezenum")==null?new Integer(0):(Integer)map.get("freezenum");
//						num = num+freezenum;
						pivo.setAttributeValue("xcl",num); 
						invxcl.put(map.get("cinventoryid").toString()+map.get("cwarehouseid").toString(), pivo);
					}
				}
			}
		}
		
		//将汇总结果封装至list，转换为数组

		HashMap invxclmap = new HashMap();
		invxclmap = clone(invxcl);//对象类型深层copy使用clone，否则会随着inv变化而产生变化

		HashMap map = new HashMap();
 		Iterator it = inv.entrySet().iterator();  
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
 			StockAgeItemVO svo = (StockAgeItemVO) entry.getValue();
 			String cinv = svo.getCinventorycode(); 
			Map mapday = getVfreePcDays(cinv, vbatStr.toString());  
			UFDouble price = getInvPrice(cinv);
			svo.setAttributeValue("nprice", price);
			if(mapday.containsKey(entry.getKey())){
				String day = mapday.get(entry.getKey())==null?"":(String)mapday.get(entry.getKey());
				if(!(day.length()<9)){
				
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				int days = 0;
				try {
					Date date1 = format.parse(day);
					Date date2 = format.parse(m_sLoginDate);
					days=new Long((date2.getTime()-date1.getTime())/(1000*60*60*24)).intValue();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				for(int m = 0;m<m_sKlsdday.length;m++){
					String vkey = svo.getCinventoryid()+svo.getCwarehouseclassid();
					if(m==0){
						int num = Integer.parseInt(m_sKlsdday[m]);
						if(days>=0&&days<=num){
							
							if(map.containsKey(vkey)){
								StockAgeItemVO xclvo = (StockAgeItemVO)map.get(vkey);
								Object obj  = xclvo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m);
								if(obj!=null){
									UFDouble xclnum = (UFDouble)obj;
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													xclnum.add(svo.getXcl())));
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(xclnum.add(svo.getXcl()))));
									/*//现存量金额= 现存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_planmny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													xclnum.add(svo.getXcl())));
									map.put(vkey, xclvo);*/
								}else{  
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													svo.getXcl())); 
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(svo.getXcl())));
									map.put(vkey, xclvo);
								}
								
							}else{
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
										svo.getXcl()));
								//库龄库存金额 = 库龄库存量*单价
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
												price.multiply(svo.getXcl())));
								map.put(vkey, svo);
							}
							break;
						}
						
					}else if(m==1){
						int num = Integer.parseInt(m_sKlsdday[m]);
						if(days>=15&&days<=num){
							if(map.containsKey(vkey)){
								StockAgeItemVO xclvo = (StockAgeItemVO)map.get(vkey);
								Object obj  = xclvo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m);
								if(obj!=null){
									UFDouble xclnum = (UFDouble)obj;
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													xclnum.add(svo.getXcl())));
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(xclnum.add(svo.getXcl()))));
									/*//现存量金额= 现存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_planmny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													xclnum.add(svo.getXcl())));
									map.put(vkey, xclvo);*/
								}else{  
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													svo.getXcl())); 
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(svo.getXcl())));
									map.put(vkey, xclvo);
								}
								
							}else{
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
										svo.getXcl()));
								//库龄库存金额 = 库龄库存量*单价
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
												price.multiply(svo.getXcl())));
								map.put(vkey, svo);
							}
							break;
						}
							
						
					}else if(m==2){
						int num = Integer.parseInt(m_sKlsdday[m]);
						if(days>=30&&days<=num){
							if(map.containsKey(vkey)){
								StockAgeItemVO xclvo = (StockAgeItemVO)map.get(vkey);
								Object obj  = xclvo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m);
								if(obj!=null){
									UFDouble xclnum = (UFDouble)obj;
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													xclnum.add(svo.getXcl())));
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(xclnum.add(svo.getXcl()))));
									/*//现存量金额= 现存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_planmny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													xclnum.add(svo.getXcl())));
									map.put(vkey, xclvo);*/
								}else{  
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													svo.getXcl())); 
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(svo.getXcl())));
									map.put(vkey, xclvo);
								}
								
							}else{
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
										svo.getXcl()));
								//库龄库存金额 = 库龄库存量*单价
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
												price.multiply(svo.getXcl())));
								map.put(vkey, svo);
							}
							break;
						}					
						
						
						
						
					}else if(m==3){
						int num = Integer.parseInt(m_sKlsdday[m]);
						if(days>=60&&days<=num){
							if(map.containsKey(vkey)){
								StockAgeItemVO xclvo = (StockAgeItemVO)map.get(vkey);
								Object obj  = xclvo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m);
								if(obj!=null){
									UFDouble xclnum = (UFDouble)obj;
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													xclnum.add(svo.getXcl())));
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(xclnum.add(svo.getXcl()))));
									/*//现存量金额= 现存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_planmny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													xclnum.add(svo.getXcl())));
									map.put(vkey, xclvo);*/
								}else{  
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													svo.getXcl())); 
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(svo.getXcl())));
									map.put(vkey, xclvo);
								}
								
							}else{
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
										svo.getXcl()));
								//库龄库存金额 = 库龄库存量*单价
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
												price.multiply(svo.getXcl())));
								map.put(vkey, svo);
							}
							break;
						}		
						
						
					}else if(m==4){
						int num = Integer.parseInt(m_sKlsdday[m]);
						if(days>=90&&days<=num){
							if(map.containsKey(vkey)){
								StockAgeItemVO xclvo = (StockAgeItemVO)map.get(vkey);
								Object obj  = xclvo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m);
								if(obj!=null){
									UFDouble xclnum = (UFDouble)obj;
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													xclnum.add(svo.getXcl())));
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(xclnum.add(svo.getXcl()))));
									/*//现存量金额= 现存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_planmny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													xclnum.add(svo.getXcl())));
									map.put(vkey, xclvo);*/
								}else{  
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													svo.getXcl())); 
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(svo.getXcl())));
									map.put(vkey, xclvo);
								}
								
							}else{
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
										svo.getXcl()));
								//库龄库存金额 = 库龄库存量*单价
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
												price.multiply(svo.getXcl())));
								map.put(vkey, svo);
							}
							break;
						}	
						
						
						
					}else if(m==5){
						int num = Integer.parseInt(m_sKlsdday[m]);
						if(days>=180&&days<=num){
							if(map.containsKey(vkey)){
								StockAgeItemVO xclvo = (StockAgeItemVO)map.get(vkey);
								Object obj  = xclvo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m);
								if(obj!=null){
									UFDouble xclnum = (UFDouble)obj;
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													xclnum.add(svo.getXcl())));
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(xclnum.add(svo.getXcl()))));
									/*//现存量金额= 现存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_planmny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													xclnum.add(svo.getXcl())));
									map.put(vkey, xclvo);*/
								}else{  
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													svo.getXcl())); 
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(svo.getXcl())));
									map.put(vkey, xclvo);
								}
								
							}else{
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
										svo.getXcl()));
								//库龄库存金额 = 库龄库存量*单价
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
												price.multiply(svo.getXcl())));
								map.put(vkey, svo);
							}
							break;
						}
						
						
						
					}else if(m==6){
						int num = Integer.parseInt(m_sKlsdday[m]);
						if(days>=360&&days<=num){
							if(map.containsKey(vkey)){
								StockAgeItemVO xclvo = (StockAgeItemVO)map.get(vkey);
								Object obj  = xclvo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m);
								if(obj!=null){
									UFDouble xclnum = (UFDouble)obj;
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													xclnum.add(svo.getXcl())));
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(xclnum.add(svo.getXcl()))));
									/*//现存量金额= 现存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_planmny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													xclnum.add(svo.getXcl())));
									map.put(vkey, xclvo);*/
								}else{  
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													svo.getXcl())); 
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(svo.getXcl())));
									map.put(vkey, xclvo);
								}
								
							}else{
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
										svo.getXcl()));
								//库龄库存金额 = 库龄库存量*单价
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
												price.multiply(svo.getXcl())));
								map.put(vkey, svo);
							}
							break;
						}
						
						
					}else if(m==7){
						int num = Integer.parseInt(m_sKlsdday[m]);
						if(days>=540&&days<=num){
							if(map.containsKey(vkey)){
								StockAgeItemVO xclvo = (StockAgeItemVO)map.get(vkey);
								Object obj  = xclvo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m);
								if(obj!=null){
									UFDouble xclnum = (UFDouble)obj;
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													xclnum.add(svo.getXcl())));
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(xclnum.add(svo.getXcl()))));
									/*//现存量金额= 现存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_planmny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													xclnum.add(svo.getXcl())));
									map.put(vkey, xclvo);*/
								}else{  
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													svo.getXcl())); 
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(svo.getXcl())));
									map.put(vkey, xclvo);
								}
								
							}else{
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
										svo.getXcl()));
								//库龄库存金额 = 库龄库存量*单价
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
												price.multiply(svo.getXcl())));
								map.put(vkey, svo);
							}
							break;
						}
						
						
					}else if(m==8){
						int num = Integer.parseInt(m_sKlsdday[m]);
						if(days>=720&&days<=num){
							if(map.containsKey(vkey)){
								StockAgeItemVO xclvo = (StockAgeItemVO)map.get(vkey);
								Object obj  = xclvo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m);
								if(obj!=null){
									UFDouble xclnum = (UFDouble)obj;
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													xclnum.add(svo.getXcl())));
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(xclnum.add(svo.getXcl()))));
									/*//现存量金额= 现存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_planmny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													xclnum.add(svo.getXcl())));
									map.put(vkey, xclvo);*/
								}else{  
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													svo.getXcl())); 
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(svo.getXcl())));
									map.put(vkey, xclvo);
								}
								
							}else{
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
										svo.getXcl()));
								//库龄库存金额 = 库龄库存量*单价
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
												price.multiply(svo.getXcl())));
								map.put(vkey, svo);
							}
							break;
						}
						
						
					}else if(m==9){
						int num = Integer.parseInt(m_sKlsdday[m]);
						if(days>=1080&&days<=num){
							if(map.containsKey(vkey)){
								StockAgeItemVO xclvo = (StockAgeItemVO)map.get(vkey);
								Object obj  = xclvo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m);
								if(obj!=null){
									UFDouble xclnum = (UFDouble)obj;
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													xclnum.add(svo.getXcl())));
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(xclnum.add(svo.getXcl()))));
									/*//现存量金额= 现存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_planmny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													xclnum.add(svo.getXcl())));
									map.put(vkey, xclvo);*/
								}else{  
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													svo.getXcl())); 
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(svo.getXcl())));
									map.put(vkey, xclvo);
								}
								
							}else{
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
										svo.getXcl()));
								//库龄库存金额 = 库龄库存量*单价
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
												price.multiply(svo.getXcl())));
								map.put(vkey, svo);
							}
							break;
						}	
						
						
						
					}else if(m==m_sKlsdday.length-1){
						int num = Integer.parseInt(m_sKlsdday[m]);
						if(days>=num){
							if(map.containsKey(vkey)){
								StockAgeItemVO xclvo = (StockAgeItemVO)map.get(vkey);
								Object obj  = xclvo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m);
								if(obj!=null){
									UFDouble xclnum = (UFDouble)obj;
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													xclnum.add(svo.getXcl())));
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(xclnum.add(svo.getXcl()))));
									/*//现存量金额= 现存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_planmny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													xclnum.add(svo.getXcl())));
									map.put(vkey, xclvo);*/
								}else{  
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													svo.getXcl())); 
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(svo.getXcl())));
									map.put(vkey, xclvo);
								}
								
							}else{
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
										svo.getXcl()));
								//库龄库存金额 = 库龄库存量*单价
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
												price.multiply(svo.getXcl())));
								map.put(vkey, svo);
							}
							break;
						}
					}else{
						int num = Integer.parseInt(m_sKlsdday[m-1]);
						int numbig = Integer.parseInt(m_sKlsdday[m]);
						if(days>num&&days<=numbig){
							
							if(map.containsKey(vkey)){
								StockAgeItemVO xclvo = (StockAgeItemVO)map.get(vkey);
								Object obj  = xclvo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m);
								if(obj!=null){
									UFDouble xclnum = (UFDouble)obj;
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													xclnum.add(svo.getXcl())));
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(xclnum.add(svo.getXcl()))));
									/*//现存量金额= 现存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_planmny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													xclnum.add(svo.getXcl())));
									map.put(vkey, xclvo);*/
								}else{  
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
													svo.getXcl())); 
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(svo.getXcl())));
									map.put(vkey, xclvo);
								}
								
							}else{
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
										svo.getXcl()));
								//库龄库存金额 = 库龄库存量*单价
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
												price.multiply(svo.getXcl())));
								map.put(vkey, svo);
							}
							break;
						}
						
					}
				} 
			  }
			}
		}
		
		Iterator itmap = map.entrySet().iterator();
		List listinv = new ArrayList();
		while (itmap.hasNext()) {
			Map.Entry entry = (Map.Entry) itmap.next();
			StockAgeItemVO value = (StockAgeItemVO) entry.getValue();
			if(invxclmap.containsKey(entry.getKey())){
				StockAgeItemVO vo = (StockAgeItemVO)invxclmap.get(entry.getKey());
				value.setXcl(vo.getXcl());
			}
			listinv.add(value);
		}
		StockAgeItemVO[] storvo =(StockAgeItemVO[]) listinv.toArray(new StockAgeItemVO[listinv.size()]);  
		//表体
		m_tvoStockAgeVO.setChildrenVO(storvo);
		return (StockAgeItemVO[]) m_tvoStockAgeVO.getChildrenVO();
//		getReportBaseClass().setBodyDataVO(m_tvoStockAgeVO.getChildrenVO());
		
	}
	
	/*//其余库龄：未启用批次
	public StockAgeItemVO[] getFalseKl(String invcode){
		ArrayList list = getOnhandnum(invcode);
		StringBuffer vbatStr = new StringBuffer("");
		ArrayList alResultData = new ArrayList(); //结果
		HashMap inv = new HashMap(); 
		HashMap invmap = new HashMap();
		if(list.size()>0){
			for(int i =0;i<list.size();i++){
				Map map = (Map) list.get(i);
				
				if(map.get("cinventoryid")!=null){
					String cwarehouseclassid = map.get("cwarehouseid")==null?"":map.get("cwarehouseid").toString();
					String cinventoryid = map.get("cinventoryid")==null?"":map.get("cinventoryid").toString();
					String ccalbodyid = map.get("ccalbodyid")==null?"":map.get("ccalbodyid").toString();
					String cwarehousecode="";
					String cwarehousename="";
					String cinventorycode="";
					String invname="";
					String invspec="";
					String invtype="";
					String cinvbasid="";
					String measdocname=""; 
					String ccalbodycode = "";
					String ccalbodyname = "";
		    		try {
		    			cwarehousecode = (String) ( HYPubBO_Client.findColValue("bd_stordoc","storcode","pk_stordoc='"+cwarehouseclassid+"'"));
		    			cwarehousename = (String) ( HYPubBO_Client.findColValue("bd_stordoc","storname","pk_stordoc='"+cwarehouseclassid+"'"));
		    			cinventorycode = (String) ( HYPubBO_Client.findColValue("bd_invbasdoc bas ,bd_invmandoc man ","bas.invcode","bas.pk_invbasdoc = man.pk_invbasdoc and man.pk_corp = '"+map.get("pk_corp")+"' and man.pk_invmandoc='"+cinventoryid+"'"));
		    			cinvbasid = (String) ( HYPubBO_Client.findColValue("bd_invmandoc  ","pk_invbasdoc"," pk_corp = '"+map.get("pk_corp")+"' and pk_invmandoc='"+cinventoryid+"'"));
		    			invname = (String) ( HYPubBO_Client.findColValue("bd_invbasdoc ","invname","pk_invbasdoc = '"+cinvbasid+"'"));
		    			invspec = (String) ( HYPubBO_Client.findColValue("bd_invbasdoc ","invspec","pk_invbasdoc = '"+cinvbasid+"'"));
		    			invtype = (String) ( HYPubBO_Client.findColValue("bd_invbasdoc ","invtype","pk_invbasdoc = '"+cinvbasid+"'"));
		    			measdocname = (String) ( HYPubBO_Client.findColValue("bd_invbasdoc bas,bd_measdoc mea","mea.measname ","bas.pk_measdoc =mea.pk_measdoc and bas.pk_invbasdoc = '"+cinvbasid+"'"));
		    			ccalbodycode = (String) ( HYPubBO_Client.findColValue("bd_calbody","bodycode","pk_calbody = '"+ccalbodyid+"'"));
		    			ccalbodyname = (String) ( HYPubBO_Client.findColValue("bd_calbody","bodyname","pk_calbody = '"+ccalbodyid+"'"));
		    			
		    			
		    		} catch (UifException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(inv.containsKey(map.get("cinventoryid").toString()+map.get("cwarehouseid").toString())){
						StockAgeItemVO vo = (StockAgeItemVO) inv.get(map.get("cinventoryid").toString()+map.get("cwarehouseid").toString());
						UFDouble xcl =vo.getAttributeValue("xcl")==null?new UFDouble(0.0):new UFDouble(vo.getAttributeValue("xcl").toString());
						Integer num =  map.get("num")==null?new Integer(0):(Integer)map.get("num");
//						Integer freezenum =  map.get("freezenum")==null?new Integer(0):(Integer)map.get("freezenum");
//						num = num+freezenum;
						num = num+Integer.parseInt(xcl.toString());
						vo.setAttributeValue("xcl",num);  
						inv.put(map.get("cinventoryid").toString()+map.get("cwarehouseid").toString(), vo);
					}else{
						StockAgeItemVO pivo = new StockAgeItemVO();
						pivo.setPk_corp(map.get("pk_corp").toString());
						pivo.setAttributeValue("cwarehouseclassid", map.get("cwarehouseid"));
						pivo.setAttributeValue("cwarehousecode",cwarehousecode);
						pivo.setAttributeValue("cwarehousename",cwarehousename);
						pivo.setAttributeValue("cinventorycode",cinventorycode);
						pivo.setAttributeValue("invname",invname);
						pivo.setAttributeValue("invspec",invspec);
						pivo.setAttributeValue("invtype",invtype);
						pivo.setAttributeValue("cinvbasid",cinvbasid);
						pivo.setAttributeValue("measdocname",measdocname);  
						pivo.setAttributeValue("ccalbodycode", ccalbodycode);
						pivo.setAttributeValue("ccalbodyname", ccalbodyname);
						pivo.setAttributeValue("cinventoryid",map.get("cinventoryid").toString());
						
						Integer num =  map.get("num")==null?new Integer(0):(Integer)map.get("num");
//						Integer freezenum =  map.get("freezenum")==null?new Integer(0):(Integer)map.get("freezenum");
//						num = num+freezenum;
						pivo.setAttributeValue("xcl",num); 
						inv.put(map.get("cinventoryid").toString()+map.get("cwarehouseid").toString(), pivo);
					}
					
				}
			}
		}
		 
		HashMap invxcl = new HashMap();
		invxcl = clone(inv);//对象类型深层copy使用clone，否则会随着inv变化而产生变化

		 
		StringBuffer sql = new StringBuffer();
		sql.append(" select sum(nvl(b.ninnum,0)-nvl(b.noutnum,0)) as num,h.cwarehouseid,b.dbizdate,man.pk_invmandoc  from ic_general_h h ") 
		.append(" left join ic_general_b b ") 
		.append(" on h.cgeneralhid = b.cgeneralhid ") 
		.append(" left join bd_invmandoc man ") 
		.append(" on man.pk_invmandoc  = b.cinventoryid ") 
		.append(" left join bd_invbasdoc bas ") 
		.append(" on bas.pk_invbasdoc = man.pk_invbasdoc  ") 
		.append(" left join bd_invcl invcl ") 
		.append(" on invcl.pk_invcl = bas.pk_invcl ")  
		.append(" where nvl(h.dr,0) = 0 ") 
		.append(" and nvl(b.dr,0) = 0 ") 
		.append(" and nvl(man.dr,0) = 0 ")  
		.append(" and man.pk_corp = '"+getCorpPrimaryKey()+"' ") 
		.append(" and h.pk_corp = '"+getCorpPrimaryKey()+"' ")
		.append(" and (bas.invcode = '"+invcode+"' or ' "+invcode+"' is null)  ") 
		.append(" and h.cbilltypecode in ('45','4A','40') ") 
		.append(" and nvl(b.ninnum,0)>0 ")
		.append("  group by b.dbizdate,man.pk_invmandoc,h.cwarehouseid  ")
		.append(" order by b.dbizdate desc") ;
		
		ArrayList obj = null;
		IUAPQueryBS uAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		
		try {
			obj = (ArrayList) uAPQueryBS.executeQuery(sql.toString(),new MapListProcessor());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		if (obj != null && obj.size() > 0){ 
			for (int i = 0; i < obj.size(); i++) { 
				Map listmap = (Map) obj.get(i);   
				String cinv = listmap.get("pk_invmandoc")==null?"":(String)listmap.get("pk_invmandoc");
				String cwarehouseid = listmap.get("cwarehouseid")==null?"":(String)listmap.get("cwarehouseid");
				String key = cinv+cwarehouseid;
				
				if(inv.containsKey(key)&&(!invmap.containsKey(key))){
					StockAgeItemVO vo = (StockAgeItemVO) inv.get(key);

					UFDouble sumnum = vo.getXcl();
					double num = listmap.get("num")==null?0:Double.parseDouble(listmap.get("num").toString());  
					sumnum = sumnum.sub(num);
					 
					long days = 0;
					if(sumnum.doubleValue()<=0){
						String date =  listmap.get("dbizdate")==null?"":(String)listmap.get("dbizdate");
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
						try {
							Date date1 = format.parse(date);
							Date date2 = format.parse(m_sLoginDate);
							days=(date2.getTime()-date1.getTime())/(1000*60*60*24);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} 
						vo.setWarnoutdays(new UFDouble(days));
						StockAgeItemVO ysvo = (StockAgeItemVO)invxcl.get(key);
						vo.setXcl(ysvo.getXcl());
						invmap.put(key, vo);
					}else{
						vo.setXcl(sumnum);
						inv.put(key, vo);
					}
				}
			}
		}
		
		//将汇总结果封装至list，转换为数组
 		Iterator it = invmap.entrySet().iterator();
		List listinv = new ArrayList();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			StockAgeItemVO value = (StockAgeItemVO) entry.getValue();
			StockAgeItemVO valuexcl = (StockAgeItemVO) invxcl.get(entry.getKey());
			value.setXcl(valuexcl.getXcl());
			listinv.add(value);
		}
		
		
		StockAgeItemVO[] storvo =(StockAgeItemVO[]) listinv.toArray(new StockAgeItemVO[listinv.size()]);
		for(int i = 0 ;i<listinv.size();i++){
			StockAgeItemVO  svo = (StockAgeItemVO) listinv.get(i);
			int days = svo.getWarnoutdays().intValue();  
			UFDouble price = getInvPrice(svo.getCinventorycode());
			svo.setAttributeValue("nprice", price);
			for(int m = 0;m<m_sKlsdday.length;m++){
				if(m==0){
					int num = Integer.parseInt(m_sKlsdday[m]);
					if(days>=0&&days<=num){
						svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
								ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
								svo.getXcl()));
						//库龄库存金额 = 库龄库存量*单价
						svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
								ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
										price.multiply(svo.getXcl())));
						break;
					}
				}else if(m==m_sKlsdday.length-1){
					int num = Integer.parseInt(m_sKlsdday[m]);
					if(days>=num){
						svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
								ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
								svo.getXcl()));
						//库龄库存金额 = 库龄库存量*单价
						svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
								ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
										price.multiply(svo.getXcl())));
						break;
					}
				}else{
					int num = Integer.parseInt(m_sKlsdday[m]);
					int numbig = Integer.parseInt(m_sKlsdday[m+1]);
					if(days>=num&&days<=numbig){
						m=m+1;
						svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
								ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
								svo.getXcl()));
						//库龄库存金额 = 库龄库存量*单价
						svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
								ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
										price.multiply(svo.getXcl())));
						break;
					}
					
				}
				
		
			}
			 
		}
		
		//表体
		m_tvoStockAgeVO.setChildrenVO(storvo);
		return (StockAgeItemVO[]) m_tvoStockAgeVO.getChildrenVO();
//		getReportBaseClass().setBodyDataVO(m_tvoStockAgeVO.getChildrenVO());
		
	
	}*/
	
	//判断存货是否批次启用
	public boolean getInvFlag(String invcode){
		StringBuffer sql = new StringBuffer();
		sql.append(" select distinct man.wholemanaflag from bd_invcl invcl ") 
		.append(" left join bd_invbasdoc bas ") 
		.append(" on bas.pk_invcl = invcl.pk_invcl ") 
		.append(" left join bd_invmandoc man ") 
		.append(" on man.pk_invbasdoc = bas.pk_invbasdoc ") 
		.append(" where bas.invcode = '"+invcode+"' ") 
		.append(" and nvl(bas.dr,0) = 0 ") 
		.append(" and nvl(man.dr,0) = 0 ") 
		.append(" and nvl(invcl.dr,0) = 0 ") 
		.append(" and man.pk_corp = '"+m_sCorpID+"' ") ;
	
		Object flagstr = null;
		IUAPQueryBS uAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		try {
			flagstr = uAPQueryBS.executeQuery(sql.toString(), new ColumnProcessor());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		boolean flag = false ;
		if(flagstr!=null){
			if(flagstr.toString().equals("Y")){
				flag = true;
			} 
		}
		return flag; 
	}
	
	
	//钢卷号
	public Map getVfreeDays(String invcode,String vfree1){
		StringBuffer sql = new StringBuffer();
		sql.append(" select min(h.dbilldate) as dbilldate,b.vfree1,b.cinventoryid,h.cwarehouseid  from ic_general_h h ") 
		.append(" left join ic_general_b b ") 
		.append(" on h.cgeneralhid = b.cgeneralhid ") 
		.append(" left join bd_invmandoc man ") 
		.append(" on man.pk_invmandoc = b.cinventoryid  ") 
		.append(" left join bd_invbasdoc bas ") 
		.append(" on bas.pk_invbasdoc = man.pk_invbasdoc ") 
		.append(" where h.pk_corp = '"+m_sCorpID+"'  ") 
		.append(" and man.pk_corp = '"+m_sCorpID+"'")
		.append(" and bas.invcode = '"+invcode+"' ") 
		.append(" and h.dbilldate is not null ")
		.append(" and nvl(h.dr, 0) = 0  ") 
		.append(" and nvl(b.dr, 0) = 0  ") 
		.append(" and nvl(man.dr, 0) = 0  ") 
		.append(" and nvl(bas.dr, 0) = 0  ") 
	//	.append(" and h.cbilltypecode in ('45')  ") 
 	//	.append(" and nvl(b.ninnum,0)>0 ")
 		.append(" and (nvl(b.ninnum,0)>0 or nvl(b.noutnum,0)<0)")

		.append(" and b.vfree1 in ( "+vfree1+" )   ") 
		.append(" group by b.vfree1,b.cinventoryid,h.cwarehouseid");

		ArrayList list = null;
		IUAPQueryBS uAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		try {
			list = (ArrayList) uAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map vfree = new HashMap();
		if (list != null && list.size() > 0){ 
			for (int i = 0; i < list.size(); i++) { 
				Map listmap = (Map) list.get(i);  
				String cinventoryid = listmap.get("cinventoryid")==null?"":listmap.get("cinventoryid").toString();
				String cwarehouseid = listmap.get("cwarehouseid")==null?"":listmap.get("cwarehouseid").toString();
				String vfr = listmap.get("vfree1")==null?"":listmap.get("vfree1").toString();
				String key = cinventoryid+cwarehouseid+vfr;
				String value = listmap.get("dbilldate")==null?"":listmap.get("dbilldate").toString();
				vfree.put(key, value);
			}
		}
		 
		return vfree;
	}
	
	/*//成品
	public String getVfreeCpDays(String invcode,String vbat){
		StringBuffer sql = new StringBuffer();
		sql.append(" select min(h.dbilldate),b.vfree1 from ic_general_h h ") 
		.append(" left join ic_general_b b ") 
		.append(" on h.cgeneralhid = b.cgeneralhid ") 
		.append(" left join bd_invmandoc man ") 
		.append(" on man.pk_invmandoc = b.cinventoryid  ") 
		.append(" left join bd_invbasdoc bas ") 
		.append(" on bas.pk_invbasdoc = man.pk_invbasdoc ") 
		.append(" where h.pk_corp = '"+m_sCorpID+"'  ") 
		.append(" and man.pk_corp = '"+m_sCorpID+"'")
		.append(" and bas.invcode = '"+invcode+"' ") 
		.append(" and h.dbilldate is not null ")
		.append(" and nvl(h.dr, 0) = 0  ") 
		.append(" and nvl(b.dr, 0) = 0  ") 
		.append(" and nvl(man.dr, 0) = 0  ") 
		.append(" and nvl(bas.dr, 0) = 0  ") 
		.append(" and h.cbilltypecode in ('45', '46')  ") 
		.append(" and nvl(b.ninnum,0)>0 ")
		.append(" and b.vfree1 in ( "+vbat+" )   ")
		.append(" group by b.vfree1");
		
		Object date = null; 
		IUAPQueryBS uAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		try {
			date = uAPQueryBS.executeQuery(sql.toString(), new MapListProcessor()); 
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		String day;
		if(date!=null){
			day = date.toString();
		}else{
			day= "" ;
		}
		return day;
	}
	*/
	
	/*//其余批次
	public String getVfreePcDays(String invcode,String vbatchcode){
		StringBuffer sql = new StringBuffer();
		sql.append(" select dbilldate from (select min(h.dbilldate) as dbilldate,b.vbatchcode  from ic_general_h h ") 
		.append(" left join ic_general_b b ") 
		.append(" on h.cgeneralhid = b.cgeneralhid ") 
		.append(" left join bd_invmandoc man ") 
		.append(" on man.pk_invmandoc = b.cinventoryid  ") 
		.append(" left join bd_invbasdoc bas ") 
		.append(" on bas.pk_invbasdoc = man.pk_invbasdoc ") 
		.append(" where h.pk_corp = '"+m_sCorpID+"'  ") 
		.append(" and man.pk_corp = '"+m_sCorpID+"'")
		.append(" and bas.invcode = '"+invcode+"' ") 
		.append(" and h.dbilldate is not null ")
		.append(" and nvl(h.dr, 0) = 0  ") 
		.append(" and nvl(b.dr, 0) = 0  ") 
		.append(" and nvl(man.dr, 0) = 0  ") 
		.append(" and nvl(bas.dr, 0) = 0  ") 
		.append(" and h.cbilltypecode in ('45')  ") 
		.append(" and nvl(b.ninnum,0)>0 ")
		.append(" and b.vbatchcode in ( "+vbatchcode+" )   ") 
		.append(" group by b.vbatchcode)");
		
		Object date = null;
		IUAPQueryBS uAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		try {
			date = uAPQueryBS.executeQuery(sql.toString(), new ColumnProcessor());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String day;
		if(date!=null){
			day = date.toString();
		}else{
			day= "" ;
		}
		return day;
	}
	*/
	
	//其余批次 
	public Map getVfreePcDays(String invcode,String vbatchcode){
		StringBuffer sql = new StringBuffer();
		sql.append(" select max(h.dbilldate) as dbilldate,b.vbatchcode,b.cinventoryid,h.cwarehouseid   from ic_general_h h ") 
		.append(" left join ic_general_b b ") 
		.append(" on h.cgeneralhid = b.cgeneralhid ") 
		.append(" left join bd_invmandoc man ") 
		.append(" on man.pk_invmandoc = b.cinventoryid  ") 
		.append(" left join bd_invbasdoc bas ") 
		.append(" on bas.pk_invbasdoc = man.pk_invbasdoc ") 
		.append(" where h.pk_corp = '"+m_sCorpID+"'  ") 
		.append(" and man.pk_corp = '"+m_sCorpID+"'")
		.append(" and bas.invcode = '"+invcode+"' ") 
		.append(" and h.dbilldate is not null ")
		.append(" and nvl(h.dr, 0) = 0  ") 
		.append(" and nvl(b.dr, 0) = 0  ") 
		.append(" and nvl(man.dr, 0) = 0  ") 
		.append(" and nvl(bas.dr, 0) = 0  ") 
		.append(" and h.cbilltypecode in ('45')  ") 
		.append(" and nvl(b.ninnum,0)>0 ")
		.append(" and b.vbatchcode in ( "+vbatchcode+" )   ") 
		.append(" group by b.vbatchcode,b.cinventoryid,h.cwarehouseid");
		
		ArrayList list = null;
		IUAPQueryBS uAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		try {
			list = (ArrayList) uAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map vfree = new HashMap();
		if (list != null && list.size() > 0){ 
			for (int i = 0; i < list.size(); i++) { 
				Map listmap = (Map) list.get(i);  
				String cinventoryid = listmap.get("cinventoryid")==null?"":listmap.get("cinventoryid").toString();
				String cwarehouseid = listmap.get("cwarehouseid")==null?"":listmap.get("cwarehouseid").toString();
				String vbatch = listmap.get("vbatchcode")==null?"":listmap.get("vbatchcode").toString();
				String key = cinventoryid+cwarehouseid+vbatch;
				String value = listmap.get("dbilldate")==null?"":listmap.get("dbilldate").toString();
				vfree.put(key, value);
			}
		}
		 
		return vfree;
	}
	
	
	//获取存货最新单价
	public UFDouble getInvPrice(String invcode){
		StringBuffer sql = new StringBuffer();
		sql.append(" select b.nprice ") 
		.append("   from ia_bill_b b ") 
		.append("  inner join bd_invmandoc man ") 
		.append("     on man.pk_invmandoc = b.cinventoryid ") 
		.append("  inner join bd_invbasdoc bas ") 
		.append("     on bas.pk_invbasdoc = man.pk_invbasdoc ") 
		.append(" where b.dbizdate in (select max(dbizdate) ") 
		.append("           from (select b.dbizdate, b.nprice, bas.invcode, h.pk_corp ") 
		.append("                   from ia_bill h ") 
		.append("                  inner join ia_bill_b b ") 
		.append("                     on h.cbillid = b.cbillid ") 
		.append("                  inner join bd_invmandoc man ") 
		.append("                     on man.pk_invmandoc = b.cinventoryid ") 
		.append("                  inner join bd_invbasdoc bas ") 
		.append("                     on bas.pk_invbasdoc = man.pk_invbasdoc ") 
		.append("                  where b.pk_corp = '"+m_sCorpID+"' ") 
		.append("                    and nvl(h.dr, 0) = 0 ") 
		.append("                    and nvl(b.dr, 0) = 0 ") 
		.append("                    and bas.invcode = '"+invcode+"' ") 
		.append("                    and nprice is not null ") 
		.append("                  group by b.dbizdate, b.nprice, bas.invcode, h.pk_corp ") 
		.append("                  order by b.dbizdate, b.nprice, bas.invcode, h.pk_corp)) and b.pk_corp = '"+m_sCorpID+"'                ") 
		.append("                    and nvl(b.dr, 0) = 0 ") 
		.append("                    and bas.invcode = '"+invcode+"' ") 
		.append("                    and nprice is not null ") ;

		
		
		
		
		
/*		sql.append(" select nprice ") 
		.append("   from (select max(h.dbilldate) as dbilldate, b.nprice ") 
		.append("           from ia_bill h ") 
		.append("           left join ia_bill_b b ") 
		.append("             on h.cbillid = b.cbillid ") 
		.append("           left join bd_invmandoc man ") 
		.append("             on man.pk_invmandoc = b.cinventoryid ") 
		.append("           left join bd_invbasdoc bas ") 
		.append("             on bas.pk_invbasdoc = man.pk_invbasdoc ") 
		.append("          where h.pk_corp = '"+m_sCorpID+"' ") 
		.append("            and nvl(h.dr, 0) = 0 ") 
		.append("            and nvl(b.dr, 0) = 0 ") 
		.append("            and bas.invcode = '"+invcode+"' ") 
		.append("            and nprice is not null ") 
		.append("          group by b.nprice) ") ;*/
		Object price = null;
		IUAPQueryBS uAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		try {
			price = uAPQueryBS.executeQuery(sql.toString(), new ColumnProcessor());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		UFDouble nprice = price==null?new UFDouble(0.0):new UFDouble(Double.parseDouble(price.toString()));
		return nprice;
	}
	
	 public static <T extends Serializable> T clone(T obj) {
		T clonedObj = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(obj);
			oos.close();
			ByteArrayInputStream bais = new ByteArrayInputStream(baos
					.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			clonedObj = (T) ois.readObject();
			ois.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return clonedObj;

	}

	 
	 //edit by zwx 2016-7-11
	//其余库龄：未启用批次
		public StockAgeItemVO[] getFalseKl(String invcode){
			String pk_corp = getClientEnvironment().getCorporation().getPk_corp();
			String firstdate = m_sLoginDate.substring(0,7)+"-01";
			String ymdate = new UFDate(firstdate).getDateBefore(1).toString().substring(0,7);
			ArrayList list = getOnhandnum(invcode,pk_corp,ymdate,firstdate,m_sLoginDate);//现存量结果
			StringBuffer vbatStr = new StringBuffer("");
			ArrayList alResultData = new ArrayList(); //结果
			HashMap inv = new HashMap(); 
//			HashMap invmap = new HashMap();
			if(list.size()>0){
				for(int i =0;i<list.size();i++){
					Map map = (Map) list.get(i);
					
					if(map.get("cinventoryid")!=null){
						String cwarehouseclassid = map.get("cwarehouseid")==null?"":map.get("cwarehouseid").toString();
						String cinventoryid = map.get("cinventoryid")==null?"":map.get("cinventoryid").toString();
 						String pk_corp1 = map.get("pk_corp")==null?"":map.get("pk_corp").toString();
						String cwarehousecode="";
						String cwarehousename="";
						String cinventorycode="";
						String invname="";
						String invspec="";
						String invtype="";
						String cinvbasid="";
						String measdocname=""; 
						String ccalbodycode = "";
						String ccalbodyname = "";
						String ccalbodyid = "";
			    		try {
			    			cwarehousecode = (String) ( HYPubBO_Client.findColValue("bd_stordoc","storcode","pk_stordoc='"+cwarehouseclassid+"'"));
			    			cwarehousename = (String) ( HYPubBO_Client.findColValue("bd_stordoc","storname","pk_stordoc='"+cwarehouseclassid+"'"));
			    			cinventorycode = (String) ( HYPubBO_Client.findColValue("bd_invbasdoc bas ,bd_invmandoc man ","bas.invcode","bas.pk_invbasdoc = man.pk_invbasdoc and man.pk_corp = '"+map.get("pk_corp")+"' and man.pk_invmandoc='"+cinventoryid+"'"));
			    			cinvbasid = (String) ( HYPubBO_Client.findColValue("bd_invmandoc  ","pk_invbasdoc"," pk_corp = '"+map.get("pk_corp")+"' and pk_invmandoc='"+cinventoryid+"'"));
			    			invname = (String) ( HYPubBO_Client.findColValue("bd_invbasdoc ","invname","pk_invbasdoc = '"+cinvbasid+"'"));
			    			invspec = (String) ( HYPubBO_Client.findColValue("bd_invbasdoc ","invspec","pk_invbasdoc = '"+cinvbasid+"'"));
			    			invtype = (String) ( HYPubBO_Client.findColValue("bd_invbasdoc ","invtype","pk_invbasdoc = '"+cinvbasid+"'"));
			    			measdocname = (String) ( HYPubBO_Client.findColValue("bd_invbasdoc bas,bd_measdoc mea","mea.measname ","bas.pk_measdoc =mea.pk_measdoc and bas.pk_invbasdoc = '"+cinvbasid+"'"));
			    			ccalbodyid = (String) ( HYPubBO_Client.findColValue("bd_calbody","pk_calbody","pk_corp = '"+pk_corp1+"'"));
			    			ccalbodycode = (String) ( HYPubBO_Client.findColValue("bd_calbody","bodycode","pk_calbody = '"+ccalbodyid+"'"));
			    			ccalbodyname = (String) ( HYPubBO_Client.findColValue("bd_calbody","bodyname","pk_calbody = '"+ccalbodyid+"'"));
			    			
			    			
			    		} catch (UifException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if(inv.containsKey(map.get("cinventoryid").toString()+map.get("cwarehouseid").toString())){
							StockAgeItemVO vo = (StockAgeItemVO) inv.get(map.get("cinventoryid").toString()+map.get("cwarehouseid").toString());
							UFDouble xcl =vo.getAttributeValue("xcl")==null?new UFDouble(0.0):new UFDouble(vo.getAttributeValue("xcl").toString());
							
							UFDouble num =map.get("num")==null?new UFDouble(0.0):new UFDouble(map.get("num").toString());
						//	Integer num =  map.get("num")==null?new Integer(0):(Integer)map.get("num");
//							Integer freezenum =  map.get("freezenum")==null?new Integer(0):(Integer)map.get("freezenum");
//							num = num+freezenum;
							num = num.add(xcl);
						//	num = num+Integer.parseInt(xcl.toString());
							vo.setAttributeValue("xcl",num);  
							inv.put(map.get("cinventoryid").toString()+map.get("cwarehouseid").toString(), vo);
						}else{
							StockAgeItemVO pivo = new StockAgeItemVO();
							pivo.setPk_corp(map.get("pk_corp").toString());
							pivo.setAttributeValue("cwarehouseclassid", map.get("cwarehouseid"));
							pivo.setAttributeValue("cwarehousecode",cwarehousecode);
							pivo.setAttributeValue("cwarehousename",cwarehousename);
							pivo.setAttributeValue("cinventorycode",cinventorycode);
							pivo.setAttributeValue("invname",invname);
							pivo.setAttributeValue("invspec",invspec);
							pivo.setAttributeValue("invtype",invtype);
							pivo.setAttributeValue("cinvbasid",cinvbasid);
							pivo.setAttributeValue("measdocname",measdocname);  
							pivo.setAttributeValue("ccalbodycode", ccalbodycode);
							pivo.setAttributeValue("ccalbodyname", ccalbodyname);
							pivo.setAttributeValue("cinventoryid",map.get("cinventoryid").toString());
							
							UFDouble num =map.get("num")==null?new UFDouble(0.0):new UFDouble(map.get("num").toString());
						//	Integer num =  map.get("num")==null?new Integer(0):(Integer)map.get("num");
//							Integer freezenum =  map.get("freezenum")==null?new Integer(0):(Integer)map.get("freezenum");
//							num = num+freezenum;
							pivo.setAttributeValue("xcl",num); 
							inv.put(map.get("cinventoryid").toString()+map.get("cwarehouseid").toString(), pivo);
						}
						
					}
				}
			}
			 
			HashMap invxcl = new HashMap();
			invxcl = clone(inv);//对象类型深层copy使用clone，否则会随着inv变化而产生变化

			 
			StringBuffer sql = new StringBuffer();
			sql.append(" select sum(nvl(b.ninnum,0)-nvl(b.noutnum,0)) as num,h.cwarehouseid,b.dbizdate,man.pk_invmandoc  from ic_general_h h ") 
			.append(" left join ic_general_b b ") 
			.append(" on h.cgeneralhid = b.cgeneralhid ") 
			.append(" left join bd_invmandoc man ") 
			.append(" on man.pk_invmandoc  = b.cinventoryid ") 
			.append(" left join bd_invbasdoc bas ") 
			.append(" on bas.pk_invbasdoc = man.pk_invbasdoc  ") 
			.append(" left join bd_invcl invcl ") 
			.append(" on invcl.pk_invcl = bas.pk_invcl ")  
			.append(" where nvl(h.dr,0) = 0 ") 
			.append(" and nvl(b.dr,0) = 0 ") 
			.append(" and nvl(man.dr,0) = 0 ")  
			.append(" and man.pk_corp = '"+getCorpPrimaryKey()+"' ") 
			.append(" and h.pk_corp = '"+getCorpPrimaryKey()+"' ")
			.append(" and (bas.invcode = '"+invcode+"' or ' "+invcode+"' is null)  ") 
			.append(" and h.cbilltypecode in ('45','4A','40') ") 
			.append(" and nvl(b.ninnum,0)>0 ")
			.append("  group by b.dbizdate,man.pk_invmandoc,h.cwarehouseid  ")
			.append(" order by b.dbizdate desc") ;
			
			ArrayList obj = null;
			IUAPQueryBS uAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			
			try {
				obj = (ArrayList) uAPQueryBS.executeQuery(sql.toString(),new MapListProcessor());
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
			ArrayList listinout = new ArrayList();
			if (obj != null && obj.size() > 0){ 
				for (int i = 0; i < obj.size(); i++) { 
					Map listmap = (Map) obj.get(i);   
					String cinv = listmap.get("pk_invmandoc")==null?"":(String)listmap.get("pk_invmandoc");
					String cwarehouseid = listmap.get("cwarehouseid")==null?"":(String)listmap.get("cwarehouseid");
					String key = cinv+cwarehouseid;
					
					if(inv.containsKey(key)){
						StockAgeItemVO vo = (StockAgeItemVO) inv.get(key); 

						UFDouble sumnum = vo.getXcl(); 
						UFDouble ysnum = clone(sumnum);
						double num = listmap.get("num")==null?0:Double.parseDouble(listmap.get("num").toString());  
						sumnum = sumnum.sub(num);
						String date =  listmap.get("dbizdate")==null?"":(String)listmap.get("dbizdate");
						long days = 0;
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
						try {
							Date date1 = format.parse(date);
							Date date2 = format.parse(m_sLoginDate);
							days=(date2.getTime()-date1.getTime())/(1000*60*60*24);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} 
						vo.setWarnoutdays(new UFDouble(days));
						 
						if(sumnum.doubleValue()<=0){ 
							vo.setXcl(ysnum);
							listinout.add(vo);
							inv.remove(key);
						}else{
							vo.setXcl(new UFDouble(num));
							listinout.add(clone(vo));
							vo.setXcl(sumnum);
 							inv.put(key, vo);
						}
					}
				}
			}
			
		/*	//将汇总结果封装至list，转换为数组
	 		Iterator it = invmap.entrySet().iterator();
			List listinv = new ArrayList();
			while (it.hasNext()) {
				Map.Entry entry = (Map.Entry) it.next();
				StockAgeItemVO value = (StockAgeItemVO) entry.getValue();
				StockAgeItemVO valuexcl = (StockAgeItemVO) invxcl.get(entry.getKey());
				value.setXcl(valuexcl.getXcl());
				listinv.add(value);
			}*/
			
			
//			StockAgeItemVO[] storvo =(StockAgeItemVO[]) listinout.toArray(new StockAgeItemVO[listinout.size()]);
			HashMap map = new HashMap();//key:存货+仓库 value:list{SPREV_dynamic_num,num}
			for(int i = 0 ;i<listinout.size();i++){
				StockAgeItemVO  svo = (StockAgeItemVO) listinout.get(i);
				int days = svo.getWarnoutdays().intValue();  
				UFDouble price = getInvPrice(svo.getCinventorycode());
				svo.setAttributeValue("nprice", price);
				for(int m = 0;m<m_sKlsdday.length;m++){
					if(m==0){
						int num = Integer.parseInt(m_sKlsdday[m]);
						if(days>=0&&days<=num){
							
							if(map.containsKey(svo.getCinventoryid()+svo.getCwarehouseclassid())){
								
								List volist = (ArrayList) map.get(svo.getCinventoryid()+svo.getCwarehouseclassid());
								String dynamicnum = volist.get(0)==null?"":volist.get(0).toString();
								StockAgeItemVO vo = (StockAgeItemVO) volist.get(1);
								if(dynamicnum.equals(StockAgeItemVO.SPREV_dynamic_num+m)){
									UFDouble dynum = vo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m)==null?new UFDouble(0.0):new UFDouble(vo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m).toString());
									UFDouble hjnum = svo.getXcl().add(dynum); 
									vo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
											hjnum));
									//库龄库存金额 = 库龄库存量*单价
									vo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(hjnum)));
								}else{
									vo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
											svo.getXcl()));
									//库龄库存金额 = 库龄库存量*单价
									vo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(svo.getXcl())));
								}
								List listmap = new ArrayList();
								listmap.add(StockAgeItemVO.SPREV_dynamic_num+m);
								listmap.add(vo); 
								map.put(svo.getCinventoryid()+svo.getCwarehouseclassid(), listmap);
							}else{
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
										svo.getXcl()));
								//库龄库存金额 = 库龄库存量*单价
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
												price.multiply(svo.getXcl())));
								List listmap = new ArrayList();
								listmap.add(StockAgeItemVO.SPREV_dynamic_num+m);
								listmap.add(svo);
								map.put(svo.getCinventoryid()+svo.getCwarehouseclassid(),listmap);
								
							}
							break;
						}
						
						
	
					}else if(m==1){
							int num = Integer.parseInt(m_sKlsdday[m]);
							if(days>=15&&days<=num){
								
								if(map.containsKey(svo.getCinventoryid()+svo.getCwarehouseclassid())){
									
									List volist = (ArrayList) map.get(svo.getCinventoryid()+svo.getCwarehouseclassid());
									String dynamicnum = volist.get(0)==null?"":volist.get(0).toString();
									StockAgeItemVO vo = (StockAgeItemVO) volist.get(1);
									if(dynamicnum.equals(StockAgeItemVO.SPREV_dynamic_num+m)){
										UFDouble dynum = vo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m)==null?new UFDouble(0.0):new UFDouble(vo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m).toString());
										UFDouble hjnum = svo.getXcl().add(dynum); 
										vo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
												ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
												hjnum));
										//库龄库存金额 = 库龄库存量*单价
										vo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
												ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
														price.multiply(hjnum)));
									}else{
										vo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
												ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
												svo.getXcl()));
										//库龄库存金额 = 库龄库存量*单价
										vo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
												ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
														price.multiply(svo.getXcl())));
									}
									List listmap = new ArrayList();
									listmap.add(StockAgeItemVO.SPREV_dynamic_num+m);
									listmap.add(vo); 
									map.put(svo.getCinventoryid()+svo.getCwarehouseclassid(), listmap);
								}else{
									svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
											svo.getXcl()));
									//库龄库存金额 = 库龄库存量*单价
									svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(svo.getXcl())));
									List listmap = new ArrayList();
									listmap.add(StockAgeItemVO.SPREV_dynamic_num+m);
									listmap.add(svo);
									map.put(svo.getCinventoryid()+svo.getCwarehouseclassid(),listmap);
									
								}
								break;
							}					
						
						
					}else if(m==2){
						int num = Integer.parseInt(m_sKlsdday[m]);
						if(days>=30&&days<=num){
							
							if(map.containsKey(svo.getCinventoryid()+svo.getCwarehouseclassid())){
								
								List volist = (ArrayList) map.get(svo.getCinventoryid()+svo.getCwarehouseclassid());
								String dynamicnum = volist.get(0)==null?"":volist.get(0).toString();
								StockAgeItemVO vo = (StockAgeItemVO) volist.get(1);
								if(dynamicnum.equals(StockAgeItemVO.SPREV_dynamic_num+m)){
									UFDouble dynum = vo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m)==null?new UFDouble(0.0):new UFDouble(vo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m).toString());
									UFDouble hjnum = svo.getXcl().add(dynum); 
									vo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
											hjnum));
									//库龄库存金额 = 库龄库存量*单价
									vo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(hjnum)));
								}else{
									vo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
											svo.getXcl()));
									//库龄库存金额 = 库龄库存量*单价
									vo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(svo.getXcl())));
								}
								List listmap = new ArrayList();
								listmap.add(StockAgeItemVO.SPREV_dynamic_num+m);
								listmap.add(vo); 
								map.put(svo.getCinventoryid()+svo.getCwarehouseclassid(), listmap);
							}else{
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
										svo.getXcl()));
								//库龄库存金额 = 库龄库存量*单价
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
												price.multiply(svo.getXcl())));
								List listmap = new ArrayList();
								listmap.add(StockAgeItemVO.SPREV_dynamic_num+m);
								listmap.add(svo);
								map.put(svo.getCinventoryid()+svo.getCwarehouseclassid(),listmap);
								
							}
							break;
						}					
						
						
					}else if(m==3){
						int num = Integer.parseInt(m_sKlsdday[m]);
						if(days>=60&&days<=num){
							
							if(map.containsKey(svo.getCinventoryid()+svo.getCwarehouseclassid())){
								
								List volist = (ArrayList) map.get(svo.getCinventoryid()+svo.getCwarehouseclassid());
								String dynamicnum = volist.get(0)==null?"":volist.get(0).toString();
								StockAgeItemVO vo = (StockAgeItemVO) volist.get(1);
								if(dynamicnum.equals(StockAgeItemVO.SPREV_dynamic_num+m)){
									UFDouble dynum = vo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m)==null?new UFDouble(0.0):new UFDouble(vo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m).toString());
									UFDouble hjnum = svo.getXcl().add(dynum); 
									vo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
											hjnum));
									//库龄库存金额 = 库龄库存量*单价
									vo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(hjnum)));
								}else{
									vo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
											svo.getXcl()));
									//库龄库存金额 = 库龄库存量*单价
									vo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(svo.getXcl())));
								}
								List listmap = new ArrayList();
								listmap.add(StockAgeItemVO.SPREV_dynamic_num+m);
								listmap.add(vo); 
								map.put(svo.getCinventoryid()+svo.getCwarehouseclassid(), listmap);
							}else{
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
										svo.getXcl()));
								//库龄库存金额 = 库龄库存量*单价
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
												price.multiply(svo.getXcl())));
								List listmap = new ArrayList();
								listmap.add(StockAgeItemVO.SPREV_dynamic_num+m);
								listmap.add(svo);
								map.put(svo.getCinventoryid()+svo.getCwarehouseclassid(),listmap);
								
							}
							break;
						}	
						
						
					}else if(m==4){
						int num = Integer.parseInt(m_sKlsdday[m]);
						if(days>=90&&days<=num){
							
							if(map.containsKey(svo.getCinventoryid()+svo.getCwarehouseclassid())){
								
								List volist = (ArrayList) map.get(svo.getCinventoryid()+svo.getCwarehouseclassid());
								String dynamicnum = volist.get(0)==null?"":volist.get(0).toString();
								StockAgeItemVO vo = (StockAgeItemVO) volist.get(1);
								if(dynamicnum.equals(StockAgeItemVO.SPREV_dynamic_num+m)){
									UFDouble dynum = vo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m)==null?new UFDouble(0.0):new UFDouble(vo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m).toString());
									UFDouble hjnum = svo.getXcl().add(dynum); 
									vo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
											hjnum));
									//库龄库存金额 = 库龄库存量*单价
									vo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(hjnum)));
								}else{
									vo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
											svo.getXcl()));
									//库龄库存金额 = 库龄库存量*单价
									vo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(svo.getXcl())));
								}
								List listmap = new ArrayList();
								listmap.add(StockAgeItemVO.SPREV_dynamic_num+m);
								listmap.add(vo); 
								map.put(svo.getCinventoryid()+svo.getCwarehouseclassid(), listmap);
							}else{
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
										svo.getXcl()));
								//库龄库存金额 = 库龄库存量*单价
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
												price.multiply(svo.getXcl())));
								List listmap = new ArrayList();
								listmap.add(StockAgeItemVO.SPREV_dynamic_num+m);
								listmap.add(svo);
								map.put(svo.getCinventoryid()+svo.getCwarehouseclassid(),listmap);
								
							}
							break;
						}	
						
						
					}else if(m==5){
						int num = Integer.parseInt(m_sKlsdday[m]);
						if(days>=180&&days<=num){
							
							if(map.containsKey(svo.getCinventoryid()+svo.getCwarehouseclassid())){
								
								List volist = (ArrayList) map.get(svo.getCinventoryid()+svo.getCwarehouseclassid());
								String dynamicnum = volist.get(0)==null?"":volist.get(0).toString();
								StockAgeItemVO vo = (StockAgeItemVO) volist.get(1);
								if(dynamicnum.equals(StockAgeItemVO.SPREV_dynamic_num+m)){
									UFDouble dynum = vo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m)==null?new UFDouble(0.0):new UFDouble(vo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m).toString());
									UFDouble hjnum = svo.getXcl().add(dynum); 
									vo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
											hjnum));
									//库龄库存金额 = 库龄库存量*单价
									vo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(hjnum)));
								}else{
									vo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
											svo.getXcl()));
									//库龄库存金额 = 库龄库存量*单价
									vo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(svo.getXcl())));
								}
								List listmap = new ArrayList();
								listmap.add(StockAgeItemVO.SPREV_dynamic_num+m);
								listmap.add(vo); 
								map.put(svo.getCinventoryid()+svo.getCwarehouseclassid(), listmap);
							}else{
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
										svo.getXcl()));
								//库龄库存金额 = 库龄库存量*单价
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
												price.multiply(svo.getXcl())));
								List listmap = new ArrayList();
								listmap.add(StockAgeItemVO.SPREV_dynamic_num+m);
								listmap.add(svo);
								map.put(svo.getCinventoryid()+svo.getCwarehouseclassid(),listmap);
								
							}
							break;
						}		
						
						
					}else if(m==6){
						int num = Integer.parseInt(m_sKlsdday[m]);
						if(days>=360&&days<=num){
							
							if(map.containsKey(svo.getCinventoryid()+svo.getCwarehouseclassid())){
								
								List volist = (ArrayList) map.get(svo.getCinventoryid()+svo.getCwarehouseclassid());
								String dynamicnum = volist.get(0)==null?"":volist.get(0).toString();
								StockAgeItemVO vo = (StockAgeItemVO) volist.get(1);
								if(dynamicnum.equals(StockAgeItemVO.SPREV_dynamic_num+m)){
									UFDouble dynum = vo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m)==null?new UFDouble(0.0):new UFDouble(vo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m).toString());
									UFDouble hjnum = svo.getXcl().add(dynum); 
									vo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
											hjnum));
									//库龄库存金额 = 库龄库存量*单价
									vo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(hjnum)));
								}else{
									vo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
											svo.getXcl()));
									//库龄库存金额 = 库龄库存量*单价
									vo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(svo.getXcl())));
								}
								List listmap = new ArrayList();
								listmap.add(StockAgeItemVO.SPREV_dynamic_num+m);
								listmap.add(vo); 
								map.put(svo.getCinventoryid()+svo.getCwarehouseclassid(), listmap);
							}else{
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
										svo.getXcl()));
								//库龄库存金额 = 库龄库存量*单价
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
												price.multiply(svo.getXcl())));
								List listmap = new ArrayList();
								listmap.add(StockAgeItemVO.SPREV_dynamic_num+m);
								listmap.add(svo);
								map.put(svo.getCinventoryid()+svo.getCwarehouseclassid(),listmap);
								
							}
							break;
						}	
						
						
						
					}else if(m==7){
						int num = Integer.parseInt(m_sKlsdday[m]);
						if(days>=540&&days<=num){
							
							if(map.containsKey(svo.getCinventoryid()+svo.getCwarehouseclassid())){
								
								List volist = (ArrayList) map.get(svo.getCinventoryid()+svo.getCwarehouseclassid());
								String dynamicnum = volist.get(0)==null?"":volist.get(0).toString();
								StockAgeItemVO vo = (StockAgeItemVO) volist.get(1);
								if(dynamicnum.equals(StockAgeItemVO.SPREV_dynamic_num+m)){
									UFDouble dynum = vo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m)==null?new UFDouble(0.0):new UFDouble(vo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m).toString());
									UFDouble hjnum = svo.getXcl().add(dynum); 
									vo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
											hjnum));
									//库龄库存金额 = 库龄库存量*单价
									vo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(hjnum)));
								}else{
									vo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
											svo.getXcl()));
									//库龄库存金额 = 库龄库存量*单价
									vo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(svo.getXcl())));
								}
								List listmap = new ArrayList();
								listmap.add(StockAgeItemVO.SPREV_dynamic_num+m);
								listmap.add(vo); 
								map.put(svo.getCinventoryid()+svo.getCwarehouseclassid(), listmap);
							}else{
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
										svo.getXcl()));
								//库龄库存金额 = 库龄库存量*单价
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
												price.multiply(svo.getXcl())));
								List listmap = new ArrayList();
								listmap.add(StockAgeItemVO.SPREV_dynamic_num+m);
								listmap.add(svo);
								map.put(svo.getCinventoryid()+svo.getCwarehouseclassid(),listmap);
								
							}
							break;
						}
						
						
					}else if(m==8){
						int num = Integer.parseInt(m_sKlsdday[m]);
						if(days>=720&&days<=num){
							
							if(map.containsKey(svo.getCinventoryid()+svo.getCwarehouseclassid())){
								
								List volist = (ArrayList) map.get(svo.getCinventoryid()+svo.getCwarehouseclassid());
								String dynamicnum = volist.get(0)==null?"":volist.get(0).toString();
								StockAgeItemVO vo = (StockAgeItemVO) volist.get(1);
								if(dynamicnum.equals(StockAgeItemVO.SPREV_dynamic_num+m)){
									UFDouble dynum = vo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m)==null?new UFDouble(0.0):new UFDouble(vo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m).toString());
									UFDouble hjnum = svo.getXcl().add(dynum); 
									vo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
											hjnum));
									//库龄库存金额 = 库龄库存量*单价
									vo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(hjnum)));
								}else{
									vo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
											svo.getXcl()));
									//库龄库存金额 = 库龄库存量*单价
									vo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(svo.getXcl())));
								}
								List listmap = new ArrayList();
								listmap.add(StockAgeItemVO.SPREV_dynamic_num+m);
								listmap.add(vo); 
								map.put(svo.getCinventoryid()+svo.getCwarehouseclassid(), listmap);
							}else{
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
										svo.getXcl()));
								//库龄库存金额 = 库龄库存量*单价
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
												price.multiply(svo.getXcl())));
								List listmap = new ArrayList();
								listmap.add(StockAgeItemVO.SPREV_dynamic_num+m);
								listmap.add(svo);
								map.put(svo.getCinventoryid()+svo.getCwarehouseclassid(),listmap);
								
							}
							break;
						}	
						
						
					}else if(m==9){
						int num = Integer.parseInt(m_sKlsdday[m]);
						if(days>=1080&&days<=num){
							
							if(map.containsKey(svo.getCinventoryid()+svo.getCwarehouseclassid())){
								
								List volist = (ArrayList) map.get(svo.getCinventoryid()+svo.getCwarehouseclassid());
								String dynamicnum = volist.get(0)==null?"":volist.get(0).toString();
								StockAgeItemVO vo = (StockAgeItemVO) volist.get(1);
								if(dynamicnum.equals(StockAgeItemVO.SPREV_dynamic_num+m)){
									UFDouble dynum = vo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m)==null?new UFDouble(0.0):new UFDouble(vo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m).toString());
									UFDouble hjnum = svo.getXcl().add(dynum); 
									vo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
											hjnum));
									//库龄库存金额 = 库龄库存量*单价
									vo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(hjnum)));
								}else{
									vo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
											svo.getXcl()));
									//库龄库存金额 = 库龄库存量*单价
									vo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(svo.getXcl())));
								}
								List listmap = new ArrayList();
								listmap.add(StockAgeItemVO.SPREV_dynamic_num+m);
								listmap.add(vo); 
								map.put(svo.getCinventoryid()+svo.getCwarehouseclassid(), listmap);
							}else{
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
										svo.getXcl()));
								//库龄库存金额 = 库龄库存量*单价
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
												price.multiply(svo.getXcl())));
								List listmap = new ArrayList();
								listmap.add(StockAgeItemVO.SPREV_dynamic_num+m);
								listmap.add(svo);
								map.put(svo.getCinventoryid()+svo.getCwarehouseclassid(),listmap);
								
							}
							break;
						}
						
						
						
					}else if(m==m_sKlsdday.length-1){
						int num = Integer.parseInt(m_sKlsdday[m]);
						if(days>=num){
							if(map.containsKey(svo.getCinventoryid()+svo.getCwarehouseclassid())){
								List volist = (ArrayList) map.get(svo.getCinventoryid()+svo.getCwarehouseclassid());
								String dynamicnum = volist.get(0)==null?"":volist.get(0).toString();
								StockAgeItemVO vo = (StockAgeItemVO) volist.get(1);
								if(dynamicnum.equals(StockAgeItemVO.SPREV_dynamic_num+m)){
									UFDouble dynum = vo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m)==null?new UFDouble(0.0):new UFDouble(vo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m).toString());
									UFDouble hjnum = svo.getXcl().add(dynum); 
									vo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
											hjnum));
									//库龄库存金额 = 库龄库存量*单价
									vo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(hjnum)));
								}else{
									vo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
											svo.getXcl()));
									//库龄库存金额 = 库龄库存量*单价
									vo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(svo.getXcl())));
								}
								List listmap = new ArrayList();
								listmap.add(StockAgeItemVO.SPREV_dynamic_num+m);
								listmap.add(vo); 
								map.put(svo.getCinventoryid()+svo.getCwarehouseclassid(), listmap);
							}else{
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
										svo.getXcl()));
								//库龄库存金额 = 库龄库存量*单价
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
												price.multiply(svo.getXcl())));
								List listmap = new ArrayList();
								listmap.add(StockAgeItemVO.SPREV_dynamic_num+m);
								listmap.add(svo);
								map.put(svo.getCinventoryid()+svo.getCwarehouseclassid(),listmap);
								
							}
							break;
						}
					}else{
						int num = Integer.parseInt(m_sKlsdday[m]);
						int numbig = Integer.parseInt(m_sKlsdday[m+1]);
						if(days>=num&&days<=numbig){
							m=m+1;
							if(map.containsKey(svo.getCinventoryid()+svo.getCwarehouseclassid())){
								List volist = (ArrayList) map.get(svo.getCinventoryid()+svo.getCwarehouseclassid());
								String dynamicnum = volist.get(0)==null?"":volist.get(0).toString();
								StockAgeItemVO vo = (StockAgeItemVO) volist.get(1);
								if(dynamicnum.equals(StockAgeItemVO.SPREV_dynamic_num+m)){
									UFDouble dynum = vo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m)==null?new UFDouble(0.0):new UFDouble(vo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m).toString());
									UFDouble hjnum = svo.getXcl().add(dynum); 
									vo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
											hjnum));
									//库龄库存金额 = 库龄库存量*单价
									vo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(hjnum)));
								}else{
									vo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
											svo.getXcl()));
									//库龄库存金额 = 库龄库存量*单价
									vo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
													price.multiply(svo.getXcl())));
								}
								List listmap = new ArrayList();
								listmap.add(StockAgeItemVO.SPREV_dynamic_num+m);
								listmap.add(vo); 
								map.put(svo.getCinventoryid()+svo.getCwarehouseclassid(), listmap);
							}else{
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_num+m),
										svo.getXcl()));
								//库龄库存金额 = 库龄库存量*单价
								svo.setAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(StockAgeItemVO.SPREV_dynamic_mny+m),
												price.multiply(svo.getXcl())));
								List listmap = new ArrayList();
								listmap.add(StockAgeItemVO.SPREV_dynamic_num+m);
								listmap.add(svo);
								map.put(svo.getCinventoryid()+svo.getCwarehouseclassid(),listmap);
								
							}
							break;
						}
						
					}
					
			
				}
				 
			}
			 
			ArrayList listvo = new ArrayList();
			Iterator it = map.entrySet().iterator();
			while(it.hasNext()){
				Map.Entry entry = (Map.Entry) it.next(); 
				ArrayList val = (ArrayList) entry.getValue();
				StockAgeItemVO nowvo = (StockAgeItemVO)val.get(1);
				if(invxcl.containsKey(entry.getKey())){
					StockAgeItemVO vo = (StockAgeItemVO)invxcl.get(entry.getKey());
					nowvo.setXcl(vo.getXcl());
				}
				listvo.add(nowvo);
			}
			
			StockAgeItemVO[] voarr = new StockAgeItemVO[listvo.size()];
			for(int k=0;k<listvo.size();k++){
				voarr[k] = (StockAgeItemVO) listvo.get(k); 
			}
			//表体
			m_tvoStockAgeVO.setChildrenVO(voarr);
			return (StockAgeItemVO[]) m_tvoStockAgeVO.getChildrenVO();
//			getReportBaseClass().setBodyDataVO(m_tvoStockAgeVO.getChildrenVO());
			
		
		}
		
		//end by zwx
}
