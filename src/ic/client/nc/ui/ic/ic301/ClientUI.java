package nc.ui.ic.ic301;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.lang.ArrayUtils;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.bd.CorpBO_Client;
import nc.ui.bd.b999.PrayvsbusiBO_Client;
import nc.ui.ic.pub.bill.GeneralBillHelper;
import nc.ui.ic.pub.report.ICReportHelper;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.report.ReportBaseClass;
import nc.vo.bd.CorpVO;
import nc.vo.ic.ic301.OrderpointBillVO;
import nc.vo.ic.ic301.OrderpointHeadVO;
import nc.vo.ic.ic301.OrderpointVO;
import nc.vo.pr.pray.PraybillHeaderVO;
import nc.vo.pr.pray.PraybillItemVO;
import nc.vo.pr.pray.PraybillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

/**
 * 再定购申请客户端UI界面 创建日期：(2001-7-24 15:05:21)
 * 
 * @author：张欣
 */

public class ClientUI extends nc.ui.ic.pub.report.IcBaseReport implements BillEditListener {
	/** 报表模板界面类 */
	private ReportBaseClass ivjReportBase = null;
	/** 查询条件对话框 */
	private QueryConditionDlg ivjQueryConditionDlg = null;
	/** 报表模板节点名 */
	private String m_sRNodeName = "400826SYS";
	/** 功能节点编码 */
	private String m_sPNodeCode = "400826";
	/** BillVO */
	private String m_sVOName = "nc.vo.ic.ic301.OrderpointBillVO";
	/** BillHeaderVO */
	private String m_sHeaderVOName = "nc.vo.ic.ic301.OrderpointHeadVO";
	/** BillItemVO */
	private String m_sItemVOName = "nc.vo.ic.ic301.OrderpointVO";
	//
	private String m_sCorpID = null; // 公司ID
	private String m_sCorpCode = null;
	private String m_sCorpName = null;
	private String m_sUserID = null; // 当前使用者ID
	private String m_sLogDate = null; // 当前登录日期
	private String m_sUserName = null; // 当前使用者名称
	private String m_sUserCode = null;

	/** 用于放置所有的查询结果 */
	private ArrayList m_alAllData = null;
	/** 用于存放用户界面修改的VO数组 */
	private ArrayList m_alCloneData = null;
	/** 用于存放用户选择的确认请购的表体VO的ArrayList */
	private ArrayList m_alResult = null;

	private ArrayList m_alPray = null;
	private ArrayList m_alTran = null;
	// 单据界面按钮

	private ButtonObject m_boCancelAll = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000042")/* @res "全消" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000536")/* @res "取消全部选择" */, 0, "全消"); /*
																																																																 * -=notranslate
																																																																 * =-
																																																																 */
	private ButtonObject m_boCancelDelete = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000008")/* @res "取消" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000008")/*
																																																							 * @res
																																																							 * "取消"
																																																							 */, 0, "取消"); /* -=notranslate=- */
	private ButtonObject m_boDeleteRow = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000013")/* @res "删行" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000013")/*
																																																						 * @res
																																																						 * "删行"
																																																						 */, 0, "删行"); /* -=notranslate=- */
	private ButtonObject m_boOutput = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000249")/*
																																		* @res
																																		* "输出"
																																		*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000537")/* @res "输出查询结果" */, 0, "输出"); /*
																																																																							 * -=notranslate
																																																																							 * =-
																																																																							 */
	// private ButtonObject m_boPrint = new ButtonObject("打印", "打印", 0);
	private ButtonObject m_boQuery = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000006")/* @res "查询" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000006")/*
																																																					 * @res
																																																					 * "查询"
																																																					 */, 0, "查询"); /* -=notranslate=- */
	private ButtonObject m_boSelectAll = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000041")/* @res "全选" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000538")/* @res "选择全部" */, 0, "全选"); /*
																																																																								* -=notranslate
																																																																								* =-
																																																																								*/
	private ButtonObject m_boConfirmApply = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec", "UPT400826-000023")/*
																																		* @res
																																		* "确认申请"
																																		*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec", "UPT400826-000023")/* @res "确认申请" */, 0, "确认申请"); /*
																																																														 * -=notranslate
																																																														 * =-
																																																														 */

	// 界面主菜单按钮组
	private ButtonObject[] m_aryButtonGroup = {
			m_boQuery, m_boDeleteRow, m_boCancelDelete, m_boSelectAll, m_boCancelAll, m_boConfirmApply
	// m_boPrint,
	// m_boOutput
	};
	private int[] m_iaScale = null;
	// 取库存参数
	private String m_sIC049 = null;

	/**
	 * ClientUI 构造子注解。
	 */
	public ClientUI() {
		super();
		initialize();
	}

	/**
	 * ClientUI 构造子注解。
	 */
	public ClientUI(nc.ui.pub.FramePanel ff) {
		super();
		setFrame(ff);
		initialize();
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-8-21 9:06:34)
	 */
	void addLine() {
		getReportBaseClass().getBillModel().addLine();
		// getReportBase().setEnabled(true);

	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-3-23 2:02:27)
	 * 
	 * @param e
	 *            ufbill.BillEditEvent
	 */
	public void afterEdit(nc.ui.pub.bill.BillEditEvent e) {
		// add by zip:2014/3/4 No 30
		if (e.getKey().equals("selectedflag")) {
			if (Boolean.parseBoolean(e.getValue().toString())) {
				Object headReqdate = getReportBaseClass().getHeadItem("xyy_reqdate").getValueObject();
				getReportBaseClass().getBillModel().setValueAt(headReqdate, e.getRow(), "xyy_reqdate_b");
			} else {
				getReportBaseClass().getBillModel().setValueAt(null, e.getRow(), "xyy_reqdate_b");
			}
		}
		// end
		if (!e.getKey().equals("selectedflag") && !e.getKey().equals("memo")) setTotalRow();
		if (e.getKey().equals("memo")) {
			((OrderpointVO) m_alCloneData.get(e.getRow())).setAttributeValue("memo", getReportBaseClass().getBodyValueAt(e.getRow(), "memo"));
		}
		if (e.getKey().equals("napplyordernum")) {
			UFDouble napplyordernum = getReportBaseClass().getBodyValueAt(e.getRow(), "napplyordernum") == null ? new UFDouble(0) : new UFDouble(getReportBaseClass().getBodyValueAt(e.getRow(), "napplyordernum").toString());
			if (napplyordernum != null && napplyordernum.doubleValue() <= 0) {
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000436")/*
																												* @res
																												* "申请请购数量不能小于或等于零，请重新输入申请订购数量！"
																												*/);
				return;
			}
			showHintMessage("");
			((OrderpointVO) m_alCloneData.get(e.getRow())).setAttributeValue("napplyordernum", napplyordernum);
		}
		if (e.getKey().equals("napplyorderastnum")) {
			((OrderpointVO) m_alCloneData.get(e.getRow())).setAttributeValue("napplyorderastnum", getReportBaseClass().getBodyValueAt(e.getRow(), "napplyorderastnum"));
		}

	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-3-23 2:02:27)
	 * 
	 * @param e
	 *            ufbill.BillEditEvent
	 */
	public void bodyRowChange(nc.ui.pub.bill.BillEditEvent e) {
	}

	/**
	 * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2001-11-12 16:47:04) 修改日期，修改人，修改原因，注释标志：
	 */
	private String execFormular(String formula, String value) {
		nc.ui.pub.formulaparse.FormulaParse f = new nc.ui.pub.formulaparse.FormulaParse();
		if (formula != null && !formula.equals("")) {
			f.setExpress(formula);
			nc.vo.pub.formulaset.VarryVO varryVO = f.getVarry();
			Hashtable ht = new Hashtable();
			for (int j = 0; j < varryVO.getVarry().length; j++) {
				String[] vs = new String[] {
					value
				};
				ht.put(varryVO.getVarry()[j], vs);
			}
			f.setDataS(ht);
			if (varryVO.getFormulaName() != null && !varryVO.getFormulaName().trim().equals("")) return f.getValueS()[0];
			else return f.getValueS()[0];
		} else {
			return null;
		}
	}

	/**
	 * 此处插入方法说明。 功能描述:获得存货的采购提前期 输入参数: 返回值: 异常处理:
	 */
	private int getAdvanceDays(OrderpointVO ovo) {
		String pk_corp = (String) ovo.getAttributeValue("pk_corp");
		String pk_invmandoc = (String) ovo.getAttributeValue("cinventoryid");
		String pk_calbody = (String) ovo.getAttributeValue("storeorgid");
		// String querydate = ovo.getAttributeValue("dbilldate");
		String pk_invbasdoc = null;
		try {
			pk_invbasdoc = execFormular("getColValue(bd_invmandoc,pk_invbasdoc,pk_invmandoc,pk_invmandoc)", pk_invmandoc);
		} catch (Exception e) {
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000437")/* @res "转换存货时发生错误！" */);
			return -1;
		}
		Vector v = new Vector();
		try {

			if (pk_calbody != null && pk_calbody.length() > 0 && pk_invbasdoc != null && pk_invbasdoc.length() > 0) v = OrderPointHelper.queryAdvanceDays(pk_corp, pk_calbody, pk_invbasdoc);
			else return 0;
		} catch (java.sql.SQLException e) {
			// MessageDialog.showErrorDlg(this, "采购提前期", "SQL语句错误！");
			nc.vo.scm.pub.SCMEnv.error(e);
			return -1;
		} catch (ArrayIndexOutOfBoundsException e) {
			// MessageDialog.showErrorDlg(this, "采购提前期", "数组越界错误！");
			nc.vo.scm.pub.SCMEnv.error(e);
			return -1;
		} catch (NullPointerException e) {
			// MessageDialog.showErrorDlg(this, "采购提前期", "空指针错误！");
			nc.vo.scm.pub.SCMEnv.error(e);
			return -1;
		} catch (Exception e) {
			// MessageDialog.showErrorDlg(this, "采购提前期", "其它错误！");
			nc.vo.scm.pub.SCMEnv.error(e);
			return -1;
		}

		if (v.size() == 0) return 0;

		UFDouble dFixedahead = (UFDouble) v.elementAt(0);
		UFDouble dAheadcoff = (UFDouble) v.elementAt(1);
		UFDouble dAheadbatch = (UFDouble) v.elementAt(2);
		UFDouble d = ovo.getNapplyordernum();

		if (d != null && dFixedahead != null && dAheadcoff != null && dAheadbatch != null && dAheadbatch.doubleValue() != 0.0) {
			double d1 = d.doubleValue();
			double d2 = dFixedahead.doubleValue();
			double d3 = dAheadcoff.doubleValue();
			double d4 = dAheadbatch.doubleValue();
			if (d1 > d4) {
				double dd = d2 + (d1 - d4) * d3 / d4;
				int k = (int) dd;
				if (dd - k > 0) k++;
				return k;
			} else return (int) d2;
		} else return 0;
	}

	/**
	 * 创建者：张欣 功能：得到环境初始数据，如制单人等。 参数： 返回： 例外： 日期：(2001-5-9 9:23:32)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	protected void getCEnvInfo() {
		try {
			// 当前使用者ID
			nc.ui.pub.ClientEnvironment ce = nc.ui.pub.ClientEnvironment.getInstance();
			try {
				m_sUserID = ce.getUser().getPrimaryKey();
				m_sUserCode = ce.getUser().getUserCode();
			} catch (Exception e) {

			}
			// nc.vo.scm.pub.SCMEnv.out("test user id is 2011000001");
			// m_sUserID="2011000001";
			// 当前使用者姓名
			try {
				m_sUserName = ce.getUser().getUserName();
			} catch (Exception e) {

			}
			// nc.vo.scm.pub.SCMEnv.out("test user name is 张三");
			// m_sUserName="张三";
			// 公司ID
			try {
				m_sCorpID = ce.getCorporation().getPrimaryKey();
				m_sCorpName = ce.getCorporation().getUnitname();
				m_sCorpCode = ce.getCorporation().getUnitcode();
				nc.vo.scm.pub.SCMEnv.out("---->corp id is " + m_sCorpID);
			} catch (Exception e) {

			}
			// 日期
			try {
				if (ce.getDate() != null) m_sLogDate = ce.getDate().toString();
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
			ivjQueryConditionDlg.setTempletID(m_sCorpID, getPNodeCode(), m_sUserID, null);

			// 设定自由项的显示
			// ivjQueryConditionDlg.setFreeItem("vfree0", "cinventoryid");
			// 设定自动清零的字段
			// String[] sThenClear = new String[] { "vfree0", "vbatchcode",
			// "castunitid" };
			// ivjQueryConditionDlg.setAutoClear("cinventoryid", sThenClear);
			// 设定辅计量
			// ivjQueryConditionDlg.setAstUnit(
			// "castunitid",
			// new String[] { "pk_corp", "cinventoryid" });

			Object[][] arycombox = new Object[3][2];
			arycombox[0][0] = "BYJJPL";
			arycombox[0][1] = nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000438")/* @res "按经济批量订货" */;
			arycombox[1][0] = "BYZGKC";
			arycombox[1][1] = nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000439")/* @res "按最高库存补齐订货" */;
			arycombox[2][0] = "BYSJXH";
			arycombox[2][1] = nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000440")/* @res "按上期实际消耗量订货" */;
			ivjQueryConditionDlg.setCombox("orderrule", arycombox);
			// 以下为对参照的初始化
			ivjQueryConditionDlg.initQueryDlgRef();

			UFDate ufLastMon = new UFDate(m_sLogDate);
			int mon = ufLastMon.getMonth();
			int year = ufLastMon.getYear();
			// default set last first day as datefrom
			// remark by zhx 20021218. modified last month date to present month
			// date.
			// if (mon == 1)
			// {
			// mon = 12;
			// year = year - 1;
			// }
			// else
			// {
			// mon = mon - 1;
			// //year = year - 1;
			// }
			String sFD = String.valueOf(year) + "-" + (mon < 10 ? ("0" + String.valueOf(mon)) : String.valueOf(mon)) + "-" + "01";
			ivjQueryConditionDlg.setDefaultValue("datefrom", null, sFD);
			// default set last month last day as dateto
			UFDate ufDate = new UFDate(sFD);
			int lastday = ufDate.getDaysMonth();
			String sLD = String.valueOf(year) + "-" + (mon < 10 ? ("0" + String.valueOf(mon)) : String.valueOf(mon)) + "-" + String.valueOf(lastday);
			ivjQueryConditionDlg.setDefaultValue("dateto", null, sLD);
			// 加入对公司参照的过滤
			ArrayList alCorpIDs = new ArrayList();
			try {
				alCorpIDs = ICReportHelper.queryCorpIDs(m_sUserID);
			} catch (Exception e) {
				nc.vo.scm.pub.SCMEnv.error(e);
			}
			ivjQueryConditionDlg.initCorpRef("pk_corp", m_sCorpID, alCorpIDs);

			ivjQueryConditionDlg.hideNormal();
		}
		return ivjQueryConditionDlg;
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
	 * 取得生成请购单VO的必要数据项，并将再定购VO[] 转换为一个请购单VO 功能： 参数：PraybillVO pvo,
	 * OrderpointVO[] ovo 返回：void 例外：无 日期：(2001-11-15 20:35:17)
	 * 修改日期，修改人，修改原因，注释标志：
	 */
	private void getPrayVODataItem(PraybillVO pvo, OrderpointVO[] ovo) throws BusinessException {
		if (ovo == null || ovo.length == 0) return;
		// nc.vo.pub.lang.UFDate.isAllowDate()
		String pk_corp = (String) ovo[0].getAttributeValue("pk_corp");
		pvo.setParentVO(new PraybillHeaderVO());
		pvo.setChildrenVO(new PraybillItemVO[ovo.length]);
		int nDays = 1;
		try {
			nDays = getAdvanceDays(ovo[0]);
		} catch (Exception e) {
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000442")/* @res "得到采购提前期时出错！" */);
			return;

		}
		// get 采购业务类型。
		String sPBusiType = getReportBaseClass().getHeadItem("purchasebusitype") == null ? null : ((UIRefPane) getReportBaseClass().getHeadItem("purchasebusitype").getComponent()).getRefPK();
		UFDate dCurr = ovo[0].getAttributeValue("billdate") == null ? new nc.vo.pub.lang.UFDate(m_sLogDate) : (UFDate) ovo[0].getAttributeValue("billdate");
		UFDate d1 = dCurr.getDateAfter(nDays); // 需求日期
		UFDate d2 = dCurr; // 建议请购日期
		/** 通过基础数据类得到请购类型和业务类型 */
		// String[] sTemp = null;
		// try {
		// sTemp = nc.ui.bd.b999.PrayvsbusiBO_Client.getBusitypeDrp(pk_corp);
		// } catch (Exception e) {
		// throw new BusinessException("不能得到请购类型和业务类型！");
		// }
		Integer npraytype = null;
		String biztype = null;

		/**
		 * lastest 1224 请购单类型 0："外包＿代料"; 1："外包＿不代料"; 2："采购"; 3："外协";
		 */
		// if (sTemp != null && sTemp.length == 2) {
		// biztype = sTemp[1];
		// if (sTemp[0].equals("委外代料")) {
		// npraytype = new Integer(0);
		// } else if (sTemp[0].equals("委外不代料")) {
		// npraytype = new Integer(1);
		// } else if (sTemp[0].equals("采购")) {
		// npraytype = new Integer(2);
		// } else {
		// npraytype = new Integer(3);
		// }
		// } else {
		// throw new BusinessException("不能得到请购类型或业务类型！请重新定义基础档案的请购类型或业务类型！");
		// }
		Object[] sTemp = null;
		try {
			// nc.ui.bd.b999.
			sTemp = PrayvsbusiBO_Client.getBusitypeDrp1(pk_corp);
		} catch (Exception e) {
			throw new BusinessException(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000443")/*
																														* @res
																														* "不能得到请购类型和业务类型！"
																														*/);

		}
		if (sTemp != null && sTemp.length == 2 && sTemp[0] != null && sTemp[1] != null) {
			biztype = (String) sTemp[1];
			npraytype = (Integer) sTemp[0];
		} else {
			throw new BusinessException(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000444")/*
																														* @res
																														* "不能得到请购类型！请重新定义基础档案的请购类型！"
																														*/);

		}
		/* 置表头VO状态为新增 * */
		pvo.getHeadVO().setStatus(nc.vo.pub.VOStatus.NEW);
		/** 在定购申请VO到请购单VO的转换：1。 置请购单表头 */
		// 手工置单据为自由状态 0
		pvo.getHeadVO().setAttributeValue("ibillstatus", new Integer(0));
		// 采购业务类型 20020926
		pvo.getHeadVO().setAttributeValue("cbiztype", sPBusiType == null ? biztype : sPBusiType);
		// 申请公司ID
		pvo.getHeadVO().setAttributeValue("pk_corp", ovo[0].getAttributeValue("pk_corp"));
		// 请购日期
		pvo.getHeadVO().setAttributeValue("dpraydate", ovo[0].getAttributeValue("billdate") == null ? new nc.vo.pub.lang.UFDate(m_sLogDate) : (UFDate) ovo[0].getAttributeValue("billdate"));
		// 制单人
		pvo.getHeadVO().setAttributeValue("coperator", m_sUserID);
		pvo.getHeadVO().setAttributeValue("ipraytype", npraytype); // 请购类型
		pvo.getHeadVO().setAttributeValue("ipraysource", new Integer(4)); // 请购来源
		// 库存组织
		pvo.getHeadVO().setAttributeValue("cstoreorganization", ovo[0].getAttributeValue("storeorgid"));

		// 备注
		pvo.getHeadVO().setAttributeValue("vmemo", nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000445")/* @res "请购来源是库存再订购申请! " */
				+ (ovo[0].getAttributeValue("memo") == null ? "" : ovo[0].getAttributeValue("memo")) + "#@$^");
		// 请购单单据号
		// String billcode = null;
		// try {
		// billcode = getPrayBillCode(pvo);
		// } catch (Exception e) {
		// nc.vo.scm.pub.SCMEnv.error(e);
		// throw new
		// BusinessException(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000446")/*@res
		// "不能得到请购单的单据号，getPrayVODataItem() 中。"*/);
		//
		// }
		// if (billcode == null) {
		// try {
		// IServiceProviderSerivce bo
		// =(IServiceProviderSerivce)NCLocator.getInstance().lookup(IServiceProviderSerivce.class.getName());
		// // billcode =
		// nc.ui.pub.services.ServiceProviderBO_Client.getOID(m_sCorpID);
		// billcode = bo.getOID(m_sCorpID);
		// } catch (Exception e) {
		// nc.vo.scm.pub.SCMEnv.error(e);
		// throw new
		// BusinessException(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000447")/*@res
		// "不能得到默认的单据号，getPrayVODataItem() 中。"*/);
		//
		// }
		// }
		// //设置请购单据号
		// pvo.getHeadVO().setAttributeValue("vpraycode", billcode);

		/** 2。置请购单表体 */
		for (int i = 0; i < ovo.length; i++) {

			// 取存货是否为辅计量管理，若是，将查询出的主计量，作为辅计量
			String isCastUnitMgt = null;
			try {
				isCastUnitMgt = execFormular("getColValue(bd_invbasdoc,assistunit,pk_invbasdoc,pk_invbasdoc)", (String) ovo[i].getAttributeValue("pk_invbasdoc"));
			} catch (Exception e) {
				nc.vo.scm.pub.SCMEnv.error(e);
				nc.vo.scm.pub.SCMEnv.out("使用公式存货是否为辅计量管理时出错！");

			}

			// //
			pvo.getBodyVO()[i] = new PraybillItemVO();
			/* 置表体VO新增状态 * */
			pvo.getBodyVO()[i].setStatus(nc.vo.pub.VOStatus.NEW);
			// 如果参数是仓库,则将仓库传入请购单. 2003-04-02.01
			if (m_sIC049.equals("仓库")) {
				pvo.getBodyVO()[i].setCwarehouseid(ovo[i].getCwarehouseid());

			}
			// 如果存货是辅计量管理的存货，应该传入辅计量（默认为主计量ID）和辅数量
			if (isCastUnitMgt != null && isCastUnitMgt.trim().equals("Y")) {
				// 辅计量ID
				pvo.getBodyVO()[i].setAttributeValue("cassistunit", ovo[i].getAttributeValue("castunitid"));
				// 辅数量
				pvo.getBodyVO()[i].setAttributeValue("nassistnum", ovo[i].getAttributeValue("napplyordernum"));
			}
			// 取得采购组织：由公司ID，库存组织ID和存货基础ID决定.
			// String sPurOrgID = null;
			// try {
			// sPurOrgID =
			// OrderPointBO_Client.queryPurOrgID(
			// new String[] {
			// (String) ovo[i].getAttributeValue("pk_corp"),
			// (String) ovo[i].getAttributeValue("storeorgid"),
			// (String) ovo[i].getAttributeValue("pk_invbasdoc")});
			// } catch (Exception e) {
			// nc.vo.scm.pub.SCMEnv.error(e);
			// throw new BusinessException("不能得到采购组织！");

			// }
			// if (sPurOrgID == null || sPurOrgID.trim().length() == 0) {
			// throw new BusinessException("不能得到采购组织！请在生产档案管理中定义采购组织。");

			// }
			// 主供应商 20020925
			pvo.getBodyVO()[i].setCvendormangid((String) ovo[i].getAttributeValue("pk_cumandoc"));
			// 采购组织
			// pvo.getBodyVO()[i].setAttributeValue("cpurorganization",
			// sPurOrgID);
			// 存货管理档案ID
			pvo.getBodyVO()[i].setAttributeValue("cmangid", ovo[i].getAttributeValue("cinventoryid"));
			// 存货基础档案ID
			pvo.getBodyVO()[i].setAttributeValue("cbaseid", ovo[i].getAttributeValue("pk_invbasdoc"));
			// 公司PK
			pvo.getBodyVO()[i].setAttributeValue("pk_corp", ovo[i].getAttributeValue("pk_corp"));
			// v5 需求公司
			pvo.getBodyVO()[i].setAttributeValue("pk_reqcorp", ovo[i].getAttributeValue("pk_corp"));
			// v5 需求库存组织
			pvo.getBodyVO()[i].setAttributeValue("pk_reqstoorg", ovo[i].getAttributeValue("storeorgid"));

			// 请购数量
			pvo.getBodyVO()[i].setAttributeValue("npraynum", ovo[i].getAttributeValue("napplyordernum"));
			// 建议订货日期(UFDate dsuggestdate)
			pvo.getBodyVO()[i].setAttributeValue("dsuggestdate", d2);
			// 需求日期(UFDate ddemanddate)
			// update by zip:2014/3/4 No 30
			// pvo.getBodyVO()[i].setAttributeValue("ddemanddate", d1);
			pvo.getBodyVO()[i].setAttributeValue("ddemanddate", ovo[i].getAttributeValue("xyy_reqdate_b"));
			// update end
		}

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
				ivjReportBase.setRowNOShow(true);
				ivjReportBase.setBodyMenuShow(false);
				ivjReportBase.setTatolRowShow(true);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				// handleException(ivjExc);
			}
		}
		return ivjReportBase;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2003-9-24 14:21:06)
	 * 
	 * @return nc.vo.pub.AggregatedValueObject
	 */
	public nc.vo.pub.AggregatedValueObject getReportVO() {
		// 准备数据
		OrderpointBillVO vo = (OrderpointBillVO) getReportBaseClass().getBillValueVO(m_sVOName, m_sHeaderVOName, m_sItemVOName);

		if (null == vo) {
			vo = new OrderpointBillVO();
		}
		if (null == vo.getParentVO()) {
			vo.setParentVO(new OrderpointHeadVO());
		}
		return vo;
	}

	/**
	 * 创建者：王乃军 功能：读系统参数 参数： 返回： 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	protected void getScale() throws nc.vo.pub.BusinessException {
		try {

			// 参数编码 含义 缺省值
			// BD501 数量小数位 2
			// BD502 辅计量数量小数位 2
			// BD503 换算率 2
			// BD504 存货成本单价小数位 2
			// BD301 本位币小数位 2
			String[] saParam = new String[] {
					"BD501", "BD502", "BD503", "BD504", "BD301"
			};

			// 传入的参数
			ArrayList alAllParam = new ArrayList();
			// 查参数的必须数据
			ArrayList alParam = new ArrayList();
			alParam.add(m_sCorpID); // 第一个是公司
			alParam.add(saParam); // 待查的参数
			alAllParam.add(alParam);
			// 查用户对应公司的必须参数
			alAllParam.add(m_sUserID);

			// 返回的设置数据
			ArrayList alRetData = (ArrayList) GeneralBillHelper.queryInfo(new Integer(nc.vo.ic.pub.bill.QryInfoConst.INIT_PARAM), alAllParam);

			// 目前读两个。
			if (alRetData == null || alRetData.size() < 2) { throw new BusinessException(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000129")/*
																																										* @res
																																										* "初始化参数错误！"
																																										*/);

			}

			// 读回的参数值
			String[] saParamValue = (String[]) alRetData.get(0);
			if (saParamValue != null && saParamValue.length > 4) {
				m_iaScale = new int[saParamValue.length];
				// BD501 数量小数位 2
				m_iaScale[nc.vo.ic.pub.bill.DoubleScale.NUM] = Integer.parseInt(saParamValue[0]);
				// BD502 辅计量数量小数位 2
				m_iaScale[nc.vo.ic.pub.bill.DoubleScale.ASSIST_NUM] = Integer.parseInt(saParamValue[1]);
				// BD503 换算率 2
				m_iaScale[nc.vo.ic.pub.bill.DoubleScale.CONVERT_RATE] = Integer.parseInt(saParamValue[2]);
				// BD504 存货成本单价小数位 2
				m_iaScale[nc.vo.ic.pub.bill.DoubleScale.PRICE] = Integer.parseInt(saParamValue[3]);
				// BD301 本位币小数位 2
				m_iaScale[nc.vo.ic.pub.bill.DoubleScale.MNY] = Integer.parseInt(saParamValue[4]);
			}

		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out("can not get para" + e.getMessage());
			if (e instanceof nc.vo.pub.BusinessException) throw new BusinessException(e.getMessage());
		}
	}

	/**
	 * 此处插入方法说明。 功能描述: 输入参数: 返回值: 异常处理: 日期:
	 */
	void getSysParams() {

		try {

			/** 得到公司参数，用户定义存货管理是按 库存组织 还是 仓库 */
			m_sIC049 = nc.ui.pub.para.SysInitBO_Client.getParaString(m_sCorpID, "IC049");

			if (m_sIC049 != null) {
				m_sIC049 = m_sIC049.trim();
			} else {
				m_sIC049 = "仓库";
			}
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);

		}
	}

	/**
	 * 子类实现该方法，返回业务界面的标题。
	 * 
	 * @version (00-6-6 13:33:25)
	 * 
	 * @return java.lang.String
	 */
	public String getTitle() {
		if (getReportBaseClass().getReportTitle() != null) return getReportBaseClass().getReportTitle();
		else return nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000435")/* @res "再订购申请" */;
	}

	/**
	 * 取得生成内部调拨单VO的必要数据项，并将再定购VO[] 转换为一个内部调拨单VO 功能： 参数： tvo, OrderpointVO[] ovo
	 * 作者：李俊 修改日期，修改人，修改原因，注释标志：
	 */
	private void getTranVODataItem(nc.vo.to.pub.BillVO tvo, OrderpointVO[] ovo) throws BusinessException {
		if (ovo == null || ovo.length == 0) return;
		UFDate dCurr = ovo[0].getAttributeValue("billdate") == null ? new nc.vo.pub.lang.UFDate(m_sLogDate) : (UFDate) ovo[0].getAttributeValue("billdate");

		tvo.setOperator(m_sUserID);

		/** 1.置申请单表头 */
		tvo.setParentVO(new nc.vo.to.pub.BillHeaderVO());
		tvo.setChildrenVO(new nc.vo.to.pub.BillItemVO[ovo.length]);

		tvo.getHeaderVO().setStatus(nc.vo.pub.VOStatus.NEW);
		tvo.getHeaderVO().setAttributeValue("fstatusflag", new Integer(0));
		tvo.getHeaderVO().setAttributeValue("cincorpid", ovo[0].getAttributeValue("pk_corp"));
		tvo.getHeaderVO().setCincbid((String) ovo[0].getAttributeValue("storeorgid"));
		tvo.getHeaderVO().setAttributeValue("dbilldate", ovo[0].getAttributeValue("billdate") == null ? new nc.vo.pub.lang.UFDate(m_sLogDate) : (UFDate) ovo[0].getAttributeValue("billdate"));
		tvo.getHeaderVO().setAttributeValue("coperatorid", m_sUserID);
		tvo.getHeaderVO().setCtypecode("5A");
		tvo.getHeaderVO().setCsourcemodulename("IC");

		/** 2.置申请单表体 */
		for (int i = 0; i < ovo.length; i++) {
			tvo.getChildrenVO()[i] = new nc.vo.to.pub.BillItemVO();
			tvo.getChildrenVO()[i].setStatus(nc.vo.pub.VOStatus.NEW);

			tvo.getItemVOs()[i].setCtypecode("5A");
			tvo.getItemVOs()[i].setCincorpid((String) ovo[0].getAttributeValue("pk_corp"));
			tvo.getChildrenVO()[i].setAttributeValue("crowno", String.valueOf(i + 10));
			tvo.getChildrenVO()[i].setAttributeValue("fallocflag", Integer.valueOf("1"));
			// 传仓库,如果存量按库存组织, 或者按仓库
			if (m_sIC049.equals("仓库")) {
				tvo.getChildrenVO()[i].setAttributeValue("cinwhid", ovo[i].getAttributeValue("cwarehouseid"));
			}

			// 库存组织
			String sInvCal = (String) ovo[i].getAttributeValue("storeorgid");
			tvo.getChildrenVO()[i].setAttributeValue("cincbid", sInvCal);

			// 地址area
			String area = "";
			area = execFormular("getColValue(bd_calbody,area,pk_calbody,pk_calbody)", sInvCal);
			tvo.getChildrenVO()[i].setAttributeValue("vreceiveaddress", area);

			// 地区areacl
			String pk_areacl = "";
			pk_areacl = execFormular("getColValue(bd_calbody,pk_areacl,pk_calbody,pk_calbody)", sInvCal);
			tvo.getChildrenVO()[i].setAttributeValue("pk_arrivearea", pk_areacl);

			// 地点address
			String pk_address = "";
			pk_address = execFormular("getColValue(bd_calbody,pk_address,pk_calbody,pk_calbody)", sInvCal);
			tvo.getChildrenVO()[i].setAttributeValue("pk_areacl", pk_address);

			tvo.getChildrenVO()[i].setAttributeValue("cininvid", ovo[i].getAttributeValue("cinventoryid"));
			tvo.getChildrenVO()[i].setAttributeValue("cinvbasid", ovo[i].getAttributeValue("pk_invbasdoc"));
			tvo.getChildrenVO()[i].setAttributeValue("cinvcode", ovo[i].getAttributeValue("invcode"));
			tvo.getChildrenVO()[i].setAttributeValue("cinvname", ovo[i].getAttributeValue("invname"));
			tvo.getChildrenVO()[i].setAttributeValue("cinvspec", ovo[i].getAttributeValue("invspec"));
			tvo.getChildrenVO()[i].setAttributeValue("cinvtype", ovo[i].getAttributeValue("invtype"));
			tvo.getChildrenVO()[i].setAttributeValue("measname", ovo[i].getAttributeValue("measname"));
			// hsl = 1

			// 辅助计量
			String isCastUnitMgt = null;
			try {
				isCastUnitMgt = execFormular("getColValue(bd_invbasdoc,assistunit,pk_invbasdoc,pk_invbasdoc)", (String) ovo[i].getAttributeValue("pk_invbasdoc"));
			} catch (Exception e) {
				nc.vo.scm.pub.SCMEnv.error(e);
				nc.vo.scm.pub.SCMEnv.out("使用公式存货是否为辅计量管理时出错！");

			}
			if (isCastUnitMgt != null && isCastUnitMgt.trim().equals("Y")) {
				tvo.getChildrenVO()[i].setAttributeValue("castunitid", ovo[i].getAttributeValue("castunitid"));
				UFDouble ufDAssNum = (UFDouble) ovo[i].getAttributeValue("napplyordernum");
				if (ufDAssNum != null) {
					if (!(ufDAssNum.doubleValue() < 0)) tvo.getChildrenVO()[i].setAttributeValue("nassistnum", ufDAssNum);
				}
				tvo.getChildrenVO()[i].setAttributeValue("nchangerate", new Integer(1));

			}

			UFDouble ufDNum = (UFDouble) ovo[i].getAttributeValue("napplyordernum");

			if (ufDNum != null) {
				if (!(ufDNum.doubleValue() < 0)) tvo.getChildrenVO()[i].setAttributeValue("nnum", ufDNum);
			}
			// 需求计划到货日期定为当前日期
			tvo.getChildrenVO()[i].setAttributeValue("dplanarrivedate", dCurr);

		}

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
	 * 此处插入方法说明。 创建日期：(2001-7-24 20:03:46)
	 */
	private void initialize() {

		setName("ClientUI");
		setLayout(new java.awt.BorderLayout());
		setSize(774, 419);
		add(getReportBaseClass(), "Center");
		getCEnvInfo();
		// 设置按钮组
		setButtons(getButtonArray(m_aryButtonGroup));
		// 初始化模板
		initReportTemplet(m_sRNodeName);
		/** 将制表人，登陆单位，登陆日期置入报表的表尾 */
		setBillTailData();
		// 设置定义的数据精度
		nc.ui.ic.pub.scale.ScaleInit si = new nc.ui.ic.pub.scale.ScaleInit(m_sUserID, m_sCorpID);
		ArrayList alTemp = new ArrayList();
		//
		getReportBaseClass().setHeadItemsEditable(new String[] {
			"purchasebusitype"
		}, true);
		getReportBaseClass().getBodyItem("napplyordernum").setEdit(true);

		// add by zip: 2014/3/4 No 30
		getReportBaseClass().getHeadItem("xyy_reqdate").setEdit(true);
		getReportBaseClass().getBodyItem("xyy_reqdate_b").setEdit(true);
		getReportBaseClass().getHeadItem("xyy_reqdate").setValue(new UFDate(System.currentTimeMillis()));
		// end

		//
		alTemp.add(new String[] {
				"restnum", "norderpointnum", "npraynum", "nordernum", "nlastoutnum", "naccumchecknum", "napplyordernum", "nmaxstocknum", "nwwnum", "neconomicnum", "nforeinnum", "minbatchnum", "ntranapplynum"
		});
		/*
		 * 现存量,再订购点数量,请购数量,采购订单数量,上期实际消耗量,到货在检,申请订购数量,最大库存,委外数量,经济批量,预计入库数量,最小订购批量
		 */
		alTemp.add(null);
		alTemp.add(null);
		alTemp.add(null);
		alTemp.add(null);
		try {
			si.setScale(getReportBaseClass(), alTemp);
		} catch (Exception e) {
			if (e instanceof BusinessException) {
				showHintMessage(e.getMessage());
			} else {
				nc.vo.scm.pub.SCMEnv.error(e);
			}

		}
		try {
			getScale();
		} catch (Exception e) {
			if (e instanceof BusinessException) {
				showHintMessage(e.getMessage());
			} else {
				nc.vo.scm.pub.SCMEnv.error(e);
			}

		}

		// 得到系统参数: 仓库or 库存组织
		getSysParams();
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-7-24 18:42:06)
	 * 
	 * @param sNodeName
	 *            java.lang.String
	 */
	public void initReportTemplet(String sNodeName) {

		// 读取模版数据
		try {
			getReportBaseClass().setTempletID(m_sCorpID, getPNodeCode(), m_sUserID, null);
		} catch (Exception e) {
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000019")/*
																											 * @res
																											 * "不能得到模版，请与系统管理员联系！"
																											 */);
			return;
		}
		nc.ui.pub.bill.BillData bd = getReportBaseClass().getBillData();

		UIRefPane ref = new UIRefPane();
		ref.setRefNodeName("业务类型");
		ref.setWhereString(" pk_corp='" + m_sCorpID + "' ");
		BillItem bi = getReportBaseClass().getHeadItem("purchasebusitype");
		if (bi != null) {
			bi.setRefType(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000003")/* @res "业务类型" */);
			((UIRefPane) bi.getComponent()).setWhereString(" pk_corp='" + m_sCorpID + "' ");
			bi.setEdit(true);
		}
		if (bd == null) {
			nc.vo.scm.pub.SCMEnv.out("--> billdata null.");
			return;
		}
		getReportBaseClass().addEditListener(this);
		getReportBaseClass().setBodyMenuShow(false);
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
		showHintMessage(bo.getName());
		if (bo == m_boQuery) onQuery(true);
		else if (bo == m_boCancelAll) onCancelAll();
		else if (bo == m_boCancelDelete) onCancelDelete();
		// else if (bo == m_boPrint)
		// onPrint();
		else if (bo == m_boSelectAll) onSelectAll();
		else if (bo == m_boDeleteRow) onDeleteRow();
		else if (bo == m_boOutput) onOutput();
		else if (bo == m_boConfirmApply) onConfirmApply();
		else super.onButtonClicked(bo);
	}

	/**
	 * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2001-8-16 10:47:52) 修改日期，修改人，修改原因，注释标志：
	 */
	void onCancelAll() {
		if (m_alCloneData != null) {
			int row = m_alCloneData.size();
			getReportBaseClass().getBillModel().setNeedCalculate(false);
			for (int i = 0; i < row; i++) {
				getReportBaseClass().setBodyValueAt(new UFBoolean(false), i, "selectedflag");
				// add by zip:2014/3/4 No 30
				getReportBaseClass().setBodyValueAt("", i, "xyy_reqdate_b");
				// add end
			}

			getReportBaseClass().getBillModel().setNeedCalculate(true);
			getReportBaseClass().getBillModel().reCalcurateAll();
		}
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-8-27 14:24:23)
	 */
	void onCancelDelete() {
		OrderpointVO[] aryvos = null;

		if (m_alAllData != null && m_alAllData.size() > 0) {
			aryvos = new OrderpointVO[m_alAllData.size()];
			m_alAllData.toArray(aryvos);
			m_alCloneData = (ArrayList) m_alAllData.clone();

		}

		getReportBaseClass().setBodyDataVO(aryvos);
		// //showErrorMessage(getConditionDlg().getUnitCode());
		setTotalRow();

	}

	// begin ncm cuijf1 201212201326231607_包装实施_列表卡片转换时数据对不上
	public OrderpointVO getOpVO(String pk_invbasdoc) {
		int pos = findPos(pk_invbasdoc);
		if (pos < 0) return null;
		return getOpVO(pos);
	}

	public int findPos(String pk_invbasdoc) {
		if (pk_invbasdoc == null) return -1;
		OrderpointVO vo = null;
		for (int i = 0, loop = m_alCloneData.size(); i < loop; i++) {
			vo = (OrderpointVO) m_alCloneData.get(i);
			if (vo == null) continue;
			if (pk_invbasdoc.equals(vo.getAttributeValue("pk_invbasdoc"))) return i;
		}
		return -1;

	}

	private OrderpointVO getOpVO(int pos) {
		if (pos < 0 || pos >= m_alCloneData.size()) return null;
		OrderpointVO vo = (OrderpointVO) m_alCloneData.get(pos);
		if (vo == null) { return null; }
		return vo;
	}

	// end ncm cuijf1 201212201326231607_包装实施_列表卡片转换时数据对不上
	/**
	 * 确认请购数量，并生成采购请购单。 此处插入方法说明。 创建日期：(2001-8-21 11:09:23)
	 */
	void onConfirmApply() {
		if (m_alCloneData == null) {
			nc.vo.scm.pub.SCMEnv.out("没有选中的存货！");
			return;
		}
		// ncm cuijf1 201301091918464573_包装实施_生成请购单的存货与列表不一致
		OrderpointVO Op = null;
		int rownum = m_alCloneData.size();
		if (rownum > 0) {
			m_alResult = new ArrayList();
			for (int i = 0; i < rownum; i++) {
				boolean bFlag = getReportBaseClass().getBodyValueAt(i, "selectedflag") == null ? false : new UFBoolean(getReportBaseClass().getBodyValueAt(i, "selectedflag").toString()).booleanValue();
				if (bFlag) {
					getReportBaseClass().getBodyItem("napplyordernum").setEdit(true);
					// begin ncm cuijf1 201301091918464573_包装实施_生成请购单的存货与列表不一致
					String pk_invbasdoc = getReportBaseClass().getBodyValueAt(i, "pk_invbasdoc").toString();
					if (pk_invbasdoc != null) {
						Op = getOpVO(pk_invbasdoc);
					}
					// end ncm cuijf1 201301091918464573_包装实施_生成请购单的存货与列表不一致
					UFDouble napplyordernum = getReportBaseClass().getBodyValueAt(i, "napplyordernum") == null ? new UFDouble(0) : new UFDouble(getReportBaseClass().getBodyValueAt(i, "napplyordernum").toString());
					if (napplyordernum == null || napplyordernum.doubleValue() <= 0) {
						showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000448")/*
																														* @res
																														* "申请请购数量不能为空或小于、等于零！请重新输入申请订购数量！"
																														*/);
						return;
					}
					// begin ncm cuijf1 201301181002258005_包装实施_手动修改申请订购数量不生效
					else {
						Op.setNapplyordernum(napplyordernum);
					}
					// end ncm cuijf1 201301181002258005_包装实施_手动修改申请订购数量不生效
					// ncm cuijf1 201301091918464573_包装实施_生成请购单存货与列表不一致
					// m_alResult.add(m_alCloneData.get(i));

					// add by zip:2014/3/4 No 30
					Op.setAttributeValue("xyy_reqdate_b", getReportBaseClass().getBodyValueAt(i, "xyy_reqdate_b"));
					// add end
					m_alResult.add(Op);
				}
			}
		}
		if (m_alResult != null && m_alResult.size() > 0) {
			if (showOkCancelMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000449")/*
																													* @res
																													* "请确认所选择的存货将由系统生成请购单或者内部调拨申请单！"
																													*/) == nc.ui.pub.beans.UIDialog.ID_OK) {
				PraybillVO pvo = new PraybillVO();
				// //////////////////edit by ljun
				nc.vo.to.pub.BillVO tvo = new nc.vo.to.pub.BillVO();
				splitArrayList(m_alResult); // split to m_alPray and m_alTran

				OrderpointVO[] ordervo = new OrderpointVO[m_alPray.size()];
				m_alPray.toArray(ordervo);

				OrderpointVO[] ordervo2 = new OrderpointVO[m_alTran.size()];
				m_alTran.toArray(ordervo2);

				try {
					if (ordervo != null && ordervo.length != 0) {
						getPrayVODataItem(pvo, ordervo);
						try {
							nc.ui.pub.pf.PfUtilClient.processAction("PUSHSAVEVO", "20", m_sLogDate, pvo);
						} catch (Exception e) {
							nc.vo.scm.pub.SCMEnv.error(e);
							showErrorMessage(e.getMessage() + nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000450")/*
																																			* @res
																																			* "保存请购单时失败，请重试。"
																																			*/);
							return;
						}

						showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000451")/*
																														* @res
																														* "转换请购单成功！"
																														*/);
					}

				} catch (Exception e) {
					showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000452")/*
																													* @res
																													* "转换请购单数据时出错！"
																													*/
							+ e.getMessage());
					return;
				}

				try {
					if (ordervo2 != null && ordervo2.length != 0) {
						getTranVODataItem(tvo, ordervo2);
						try {
							ArrayList alParam = new ArrayList();
							alParam.add(null);
							alParam.add(new nc.vo.scm.pub.session.ClientLink(getClientEnvironment()));

							// nc.vo.to.pub.BillVO [] tVOs=new
							// nc.vo.to.pub.BillVO []{tvo};

							nc.ui.pub.pf.PfUtilClient.processBatch("PUSHSAVEVO", "5A", m_sLogDate, new nc.vo.to.pub.BillVO[] {
								tvo
							}, new Object[] {
								alParam
							});
							getReportBaseClass().getHeadItem("purchasebusitype").setEnabled(false);
						} catch (Exception e) {
							nc.vo.scm.pub.SCMEnv.error(e);
							showErrorMessage(e.getMessage() + nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000453")/*
																																			* @res
																																			* "保存调拨申请单时失败，请重试。"
																																			*/);
							return;
						}

						showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000454")/*
																														* @res
																														* "转换调拨申请单成功！"
																														*/);
					}

				} catch (Exception e) {
					showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000455")/*
																													* @res
																													* "转换调拨申请单数据时出错！"
																													*/
							+ e.getMessage());
					return;
				}
				// 除非在查询，否则只能请购一次。
				m_boConfirmApply.setEnabled(false);
				m_boSelectAll.setEnabled(false);
				m_boCancelAll.setEnabled(false);
				m_boDeleteRow.setEnabled(false);
				m_boCancelDelete.setEnabled(false);
				updateButton(m_boConfirmApply);
				updateButton(m_boSelectAll);
				updateButton(m_boCancelAll);
				updateButton(m_boDeleteRow);
				updateButton(m_boCancelDelete);

				// 将表头业务类型设置为不可编辑. 20030402
				if (getReportBaseClass().getHeadItem("purchasebusitype") != null) getReportBaseClass().getHeadItem("purchasebusitype").setEnabled(false);
				// 确认请购后,需要将界面变量清空
				m_alCloneData = null;
			}

		} else {
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000456")/* @res "请选中一行或多行存货以生成请购单！" */);
		}

	}

	/**
	 * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2001-8-16 10:48:11) 修改日期，修改人，修改原因，注释标志：
	 */
	void onDeleteRow() {
		int[] selrow = getReportBaseClass().getBillTable().getSelectedRows();
		if (selrow == null || m_alCloneData == null || m_alCloneData.size() == 0) // 没选中一行，返回
		return;
		int length = selrow.length;
		nc.vo.scm.pub.SCMEnv.out("count is " + length);
		if (length == 0) { // 没选中一行，返回
			return;
		}
		if (length == 1) { // 删除一行
			if (showOkCancelMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000457")/*
																													* @res
																													* "确定删除所选的行吗？"
																													*/) == nc.ui.pub.beans.UIDialog.ID_CANCEL) return;
			int allrownums = getReportBaseClass().getRowCount();
			int selrownum = getReportBaseClass().getBillTable().getSelectedRow();
			getReportBaseClass().delLine();
			/** 同步化ArrayList */
			m_alCloneData.remove(selrownum);

			if (selrow[0] + 1 > allrownums) {
				int iRowCount = getReportBaseClass().getRowCount();
				if (iRowCount > 0) getReportBaseClass().getBillTable().setRowSelectionInterval(selrow[0], selrow[0]);
			}
		} else { // 删除多行
			showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000457")/* @res "确定删除所选的行吗？" */);
			getReportBaseClass().getBillModel().delLine(selrow);
			/** 同步化ArrayList */
			for (int i = 0; i < selrow.length; i++) {
				m_alCloneData.remove(selrow[i]);
			}
			int iRowCount = getReportBaseClass().getRowCount();
			if (iRowCount > 0) getReportBaseClass().getBillTable().setRowSelectionInterval(0, 0);
		}
		/** 删除行后，需要重新计算合计 */
		setTotalRow();

	}

	/**
	 * 定位单据 功能： 参数： 返回： 例外： 日期：(2001-8-9 16:03:39) 修改日期，修改人，修改原因，注释标志：
	 */
	public void onLocate() {

	}

	/**
	 * 输出单据信息 功能： 参数： 返回： 例外： 日期：(2001-8-9 16:03:27) 修改日期，修改人，修改原因，注释标志：
	 */
	void onOutput() {
		getReportBaseClass().setCellEditable(1, "selectedflag", false);
		getReportBaseClass().setTatolRowShow(true);
	}

	private String getStringValue(String sKey) {
		if (sKey.equals("BYJJPL")) return nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000438")/* @res "按经济批量订货" */;
		else if (sKey.equals("BYZGKC")) return nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000439")/* @res "按最高库存补齐订货" */;
		else if (sKey.equals("BYSJXH")) return nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000440")/* @res "按上期实际消耗量订货" */;
		else return null;
	}

	/**
	 * 根据相关查询条件，查询单据 功能： 参数： 返回： 例外： 日期：(2001-8-9 16:03:48) 修改日期，修改人，修改原因，注释标志：
	 */
	public void onQuery(boolean bQuery) {

		if (bQuery || !m_bEverQry) {
			getConditionDlg().showModal();
			m_bEverQry = true;
		} else {
			getConditionDlg().onButtonConfig();
		}
		OrderpointHeadVO headvo = null;

		if (!getConditionDlg().isCloseOK()) return;

		try {
			nc.vo.pub.query.ConditionVO[] voCons = getConditionDlg().getExpandVOs(getConditionDlg().getConditionVO());
			getReportBaseClass().setTatolRowShow(true);
			String[] sItemKey = {
					"selectedflag", "napplyordernum", "napplyorderastnum", "memo"
			};
			getReportBaseClass().setBodyItemsEditable(sItemKey, true);

			ArrayList alTranParam = new ArrayList();
			alTranParam.add(m_sCorpID);
			alTranParam.add(voCons);
			alTranParam.add(m_sLogDate);
			m_alAllData = OrderPointHelper.queryOrderPoint(alTranParam);

			nc.vo.scm.pub.SCMEnv.out(getConditionDlg().getWhereSQL());

			OrderpointVO[] aryvos = null;

			if (m_alAllData != null && m_alAllData.size() > 0) {
				aryvos = new OrderpointVO[m_alAllData.size()];

				for (int i = 0; i < m_alAllData.size(); i++) {

					((OrderpointVO) m_alAllData.get(i)).setAttributeValue("applypsnid", m_sUserID);
					((OrderpointVO) m_alAllData.get(i)).setAttributeValue("applypsnname", m_sUserName);
				}
				m_alAllData.toArray(aryvos);
				m_alCloneData = (ArrayList) m_alAllData.clone();

			}

			// add by xiaolong_fan.增加13个月的存货消耗量的汇总.2013-1-8
			String pk_corp = null;
			String pk_calbody = null;
			if (getParentCorpCode().equals("10395")) {
				if ((this.m_alAllData != null) && (this.m_alAllData.size() > 0)) {
					aryvos = new OrderpointVO[this.m_alAllData.size()];
					StringBuffer sb = new StringBuffer();
					if (voCons != null && voCons.length > 0) {
						for (int i = 0; i < voCons.length; i++) {
							if (voCons[i].getFieldCode().equals("pk_corp")) {
								pk_corp = voCons[i].getValue();
							}
							if (voCons[i].getFieldCode().equals("ccalbodyid")) {
								pk_calbody = voCons[i].getValue();
							}
						}
					}

					for (int i = 0; i < this.m_alAllData.size(); ++i) {
						((OrderpointVO) this.m_alAllData.get(i)).setAttributeValue("applypsnid", this.m_sUserID);
						((OrderpointVO) this.m_alAllData.get(i)).setAttributeValue("applypsnname", this.m_sUserName);
						// add by xiaolong_fan,
						String invcode = ((OrderpointVO) this.m_alAllData.get(i)).getInvcode();

						sb.append("c.invcode='").append(invcode).append("'");
						if (i == this.m_alAllData.size() - 1) {
							continue;
						}
						sb.append(" or ");
						// String xhl = getxhl(pk_corp,pk_calbody,invcode);

						// ((OrderpointVO)this.m_alAllData.get(i)).setXhl(xhl);

					}
					Calendar cal = Calendar.getInstance();
					String month = String.valueOf((cal.get(Calendar.MONTH) + 1) < 10 ? "0" + (cal.get(Calendar.MONTH) + 1) : cal.get(Calendar.MONTH) + 1);

					String sql = "select c.invcode || substr(a.dbilldate, 1, 7),nvl(sum(b.noutnum), 0) num from ic_general_h a left join ic_general_b b on a.cgeneralhid = b.cgeneralhid left join bd_invbasdoc c on b.cinvbasid = c.pk_invbasdoc where nvl(a.dr, 0) = 0 and a.pk_corp = ? and a.pk_calbody = ? and a.cbilltypecode in ('4D', '4I', '4F', '4C', '4G', '4Y') and to_date(a.dbilldate, 'yyyy-MM-dd') >= to_date(?, 'yyyy-MM-dd') and (" + sb.toString() + ")  group by c.invcode, substr(a.dbilldate, 1, 7) order by c.invcode, substr(a.dbilldate, 1, 7) ";
					IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
					SQLParameter param = new SQLParameter();
					param.addParam(pk_corp);
					param.addParam(pk_calbody);
					param.addParam((cal.get(Calendar.YEAR) - 1) + "-" + month + "-" + "01");
					// param.addParam(sb.toString());
					ArrayList list = (ArrayList) iUAPQueryBS.executeQuery(sql, param, new ArrayListProcessor());
					Map map = new HashMap();
					for (int i = 0; i < list.size(); i++) {
						Object[] strs = (Object[]) list.get(i);
						map.put(strs[0], strs[1]);
					}
					for (int i = 0; i < this.m_alAllData.size(); ++i) {
						String invcode = ((OrderpointVO) this.m_alAllData.get(i)).getInvcode();
						String xhl = getXhl2(invcode, map);
						((OrderpointVO) this.m_alAllData.get(i)).setXhl(xhl);

					}

					this.m_alAllData.toArray(aryvos);
					this.m_alCloneData = ((ArrayList) this.m_alAllData.clone());
				}

			}
			// end by xiaolong_fan.
			if (voCons != null && voCons.length > 0) {
				headvo = new OrderpointHeadVO();
				headvo.setAttributeValue("billdate", m_sLogDate);
				for (int i = 0; i < voCons.length; i++) {
					if (voCons[i].getFieldCode().equals("pk_corp")) {
						headvo.setAttributeValue("pk_corp", voCons[i].getRefResult().getRefPK());
						headvo.setAttributeValue("corpname", voCons[i].getRefResult().getRefName());
						headvo.setAttributeValue("corpcode", voCons[i].getRefResult().getRefCode());
					}
					if (voCons[i].getFieldCode().equals("invcode")) {
						headvo.setAttributeValue("invcode", voCons[i].getValue());
						// 该参照在查询模板中定义为不进行检查，即允许用户手工输入，因此在该情况下，下面的语句会抛出NullPointerException
						// headvo.setAttributeValue("invname",
						// voCons[i].getRefResult().getRefName());
					}
					if (voCons[i].getFieldCode().equals("ccalbodyid")) {
						if (voCons[i].getRefResult() == null) continue;
						headvo.setAttributeValue("storeorgname", voCons[i].getRefResult().getRefName());
						headvo.setAttributeValue("storeorgid", voCons[i].getRefResult().getRefPK());
					}

					if (voCons[i].getFieldCode().equals("cwarehouseid")) {
						// headvo.setAttributeValue("cwarehouseid",
						// voCons[i].getRefResult().getRefPK());
						if (voCons[i].getRefResult() == null) continue;
						headvo.setAttributeValue("cwarehousename", voCons[i].getRefResult().getRefName());

					}

					if (voCons[i].getFieldCode().equals("invclasscode"/* "invcl" */)) {
						headvo.setAttributeValue("invclname", voCons[i].getValue());
						// 该参照在查询模板中定义为不进行检查，即允许用户手工输入，因此在该情况下，下面的语句会抛出NullPointerException
						// 此处注释掉
						/*
						 * headvo.setAttributeValue("invclcode",
						 * voCons[i].getRefResult().getRefCode());
						 * headvo.setAttributeValue("invclid",
						 * voCons[i].getRefResult().getRefPK());
						 * headvo.setAttributeValue("invclname",
						 * voCons[i].getRefResult().getRefName());
						 */
					}
					if (voCons[i].getFieldCode().equals("orderrule")) {
						headvo.setAttributeValue("orderrule", getStringValue(voCons[i].getValue()));

					}

				}

			}

			getReportBaseClass().setHeadDataVO(headvo);
			if (aryvos == null || aryvos.length <= 0) {
				// aryvos = new OrderpointVO[1];
				// aryvos[0] = new OrderpointVO();
				// getReportBaseClass().setEnabled(false);
				m_alCloneData = null;
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000458")/*
																												* @res
																												* "查询没有结果! 可能是仓库中没有达到再订购点的存货, 也可能是下列条件没有设置正确:1.物料生成档案中计划属性为再订购或MRP 2.批量规则为经济批量.  "
																												*/);
			}
			if (getReportBaseClass().getHeadItem("purchasebusitype") != null) getReportBaseClass().getHeadItem("purchasebusitype").setEnabled(true);

			// add by zip:2013/11/21 No 29
			// 设置存货的数量信息
			if (aryvos != null && aryvos.length > 0) {
				String dateto = null;
				for (int j = 0; j < voCons.length; j++) {
					if ("dateto".equals(voCons[j].getFieldCode())) {
						dateto = voCons[j].getValue();
					}
				}
				String startDate = "2013-01-01";
				for (int j = 0; j < aryvos.length; j++) {
					String invbasid = (String) aryvos[j].getAttributeValue("pk_invbasdoc");
					UFDouble xcl = aryvos[j].getRestnum();
					// 已申购未审批数量
					String sql1 = new StringBuilder().append("select sum(A.npraynum) as rst from po_praybill_b A,po_praybill B").append(" where A.cpraybillid=B.cpraybillid and nvl(A.dr,0)=0 and nvl(B.dr,0)=0").append(" and B.pk_corp='" + pk_corp + "' and B.ibillstatus=0").append(" and B.dpraydate<='" + dateto + "' and B.dpraydate>='" + startDate + "'").append(" and A.cbaseid='" + invbasid + "'").toString();
					// 已申购已审批数量
					String sql2 = new StringBuilder().append("select sum(A.npraynum) as rst from po_praybill_b A,po_praybill B").append(" where A.cpraybillid=B.cpraybillid and nvl(A.dr,0)=0 and nvl(B.dr,0)=0").append(" and B.pk_corp='" + pk_corp + "' and B.ibillstatus=3").append(" and B.dpraydate<='" + dateto + "' and B.dpraydate>='" + startDate + "'").append(" and A.cbaseid='" + invbasid + "'").toString();
					// 采购订单数量
					//edit by yhj 2014-08-06 关闭的订单数量不需要参与计算
					String sql3 = new StringBuilder().append("select sum(A.nordernum) as rst from po_order_b A,po_order B").append(" where A.corderid=B.corderid").append(" and nvl(A.dr,0)=0 and nvl(B.dr,0)=0").append(" and B.pk_corp='" + pk_corp + "' and A.iisactive = 0  and B.breturn = 'N' ").append(" and B.dorderdate<='" + dateto + "' and B.dorderdate>='" + startDate + "'").append(" and A.cbaseid='" + invbasid + "'").toString();
//					String sql3 = new StringBuilder().append("select sum(A.nordernum) as rst from po_order_b A,po_order B").append(" where A.corderid=B.corderid").append(" and nvl(A.dr,0)=0 and nvl(B.dr,0)=0").append(" and B.pk_corp='" + pk_corp + "'").append(" and B.dorderdate<='" + dateto + "' and B.dorderdate>='" + startDate + "'").append(" and A.cbaseid='" + invbasid + "'").toString();
					//end
					//add by zwx 2015-9-18
					StringBuffer sql3New=new StringBuffer();
					sql3New.append(" select sum(pob.nordernum ) from po_order po ") 
					.append(" left join po_order_b pob ") 
					.append(" on pob.corderid = po.corderid  ") 
					.append(" where pob.cupsourcebillid  in ") 
					.append(" (select  A.cpraybillid  as rst ") 
					.append("   from po_praybill_b A, po_praybill B ") 
					.append("  where A.cpraybillid = B.cpraybillid ") 
					.append("    and nvl(A.dr, 0) = 0 ") 
					.append("    and nvl(B.dr, 0) = 0 ") 
					.append("    and B.pk_corp = '" + pk_corp + "'  ") 
					.append("    and B.dpraydate <= '" + dateto + "' ") 
					.append("    and B.dpraydate >= '" + startDate + "' ")  
					.append("    and A.cbaseid = '" + invbasid + "' and B.ibillstatus in('0','3') ) ") //edit by zwx 2015-12-28 
					.append("    and nvl(po.dr,0) = 0 ") 
					.append("    and nvl(pob.dr,0) = 0 ") 
					.append("    and po.pk_corp = '" + pk_corp + "' ") 
					.append("    and pob.cbaseid  = '" + invbasid + "' ") 
					.append("    and po.dorderdate <= '" + dateto + "' ") 
					.append("    and po.dorderdate >= '" + startDate + "' ")
					.append("    and pob.iisactive = 0 ") ;
					
					//end by zwx
					// 到货单数量
					String sql4 = new StringBuilder().append("select sum(A.narrvnum) as rst from po_arriveorder_b A,po_arriveorder B").append(" where A.carriveorderid=B.carriveorderid").append(" and nvl(A.dr,0)=0 and nvl(B.dr,0)=0").append(" and B.pk_corp='" + pk_corp + "' ").append(" and B.dreceivedate<='" + dateto + "' and B.dreceivedate>='" + startDate + "'").append(" and A.cbaseid='" + invbasid + "'").toString();
					
					//add by zwx 2015-9-18 
					StringBuffer sql4New=new StringBuffer();
					sql4New.append(" select sum(A.narrvnum) as rst ") 
					.append("   from po_arriveorder_b A ") 
					.append("   left join po_arriveorder B ") 
					.append("     on A.carriveorderid = B.carriveorderid ") 
					.append("   left join po_order_b pob ") 
					.append("     on pob.corder_bid  = A.csourcebillrowid ") 
					.append("     left join po_order po ") 
					.append("     on po.corderid = pob.corderid ") 
					.append("  where nvl(A.dr, 0) = 0 ") 
					.append("    and nvl(B.dr, 0) = 0 ") 
					.append("    and nvl(po.dr,0) = 0 ") 
					.append("    and nvl(pob.dr,0) = 0 ") 
					.append("    and po.pk_corp = '" + pk_corp + "' ") 
					.append("    and B.pk_corp = '" + pk_corp + "' ") 
					.append("    and B.dreceivedate <= '" + dateto + "' ") 
					.append("    and B.dreceivedate >= '" + startDate + "' ") 
					.append("    and A.cbaseid = '" + invbasid + "' ") 
					.append("    and po.dorderdate >= '" + startDate + "' ") 
					.append("    and po.dorderdate <= '" + dateto + "' ") 
					.append("    and po.breturn = 'N' ")
					.append("    and pob.iisactive = 0 ") ;
					//end by zwx
					
					// 检验合格数量
					String sql5 = new StringBuilder().append("select sum(A.nelignum) as rst from po_arriveorder_b A,po_arriveorder B").append(" where A.carriveorderid=B.carriveorderid").append(" and nvl(A.dr,0)=0 and nvl(B.dr,0)=0").append(" and B.pk_corp='" + pk_corp + "'").append(" and B.dreceivedate<='" + dateto + "' and B.dreceivedate>='" + startDate + "'").append(" and A.cbaseid='" + invbasid + "'").toString();
					// 已冻结数量
					String sql6 = new StringBuilder().append("select sum(nvl(nfreezenum,0)) as rst from ic_freeze where nvl(dr, 0) = 0 and pk_corp = '" + pk_corp + "' and cinvbasid = '" + invbasid + "' and cthawpersonid is null and cspaceid is not null").toString();
					IUAPQueryBS queryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
					double d1, d2, d3, d4, d5, d6,new3,new4;
					Object obj = queryBS.executeQuery(sql1, new ColumnProcessor());
					d1 = obj == null ? 0 : Double.parseDouble(obj.toString());
					obj = queryBS.executeQuery(sql2, new ColumnProcessor());
					d2 = obj == null ? 0 : Double.parseDouble(obj.toString());
					obj = queryBS.executeQuery(sql3, new ColumnProcessor());
					d3 = obj == null ? 0 : Double.parseDouble(obj.toString());
					obj = queryBS.executeQuery(sql4, new ColumnProcessor());
					d4 = obj == null ? 0 : Double.parseDouble(obj.toString());
					obj = queryBS.executeQuery(sql5, new ColumnProcessor());
					d5 = obj == null ? 0 : Double.parseDouble(obj.toString());
					obj = queryBS.executeQuery(sql6, new ColumnProcessor());
					d6 = obj == null ? 0 : Double.parseDouble(obj.toString());
					//add by zwx 2015-9-18
					obj = queryBS.executeQuery(sql3New.toString(), new ColumnProcessor());
					new3 = obj == null ? 0 : Double.parseDouble(obj.toString());
					obj = queryBS.executeQuery(sql4New.toString(), new ColumnProcessor());
					new4 = obj == null ? 0 : Double.parseDouble(obj.toString());
					//end by zwx
					// add by zip: 2014/3/28 No 37 and 29
					double leftDbl = (d2 - d3) + (d3 - d4) + (d4 - d5) + (xcl.doubleValue() - d6) - d6;
					double rightDbl = aryvos[j].getAttributeValue("norderpointnum") == null ? 0 : Double.parseDouble(aryvos[j].getAttributeValue("norderpointnum").toString());
					if (leftDbl >= rightDbl) {
						aryvos = (OrderpointVO[]) ArrayUtils.remove(aryvos, j);
						j--;
					} else {
						aryvos[j].setAttributeValue("xyy_prayundo", d1); // 已申购未审批
//						aryvos[j].setAttributeValue("xyy_praydounorder", d2 - d3); // 已审批未订购
						aryvos[j].setAttributeValue("xyy_praydounorder", d1+d2 - new3); // 已审批未订购 改为请购单表体已申购 edit by zwx 
//						aryvos[j].setAttributeValue("xyy_orderunarr", d3 - d4); // 已订购未到货 
						aryvos[j].setAttributeValue("xyy_orderunarr", d3 - new4); // 已订购未到货 edit by zwx
						aryvos[j].setAttributeValue("xyy_arruncheck", d4 - d5); // 已到货未检验
						aryvos[j].setAttributeValue("xyy_free", d6); // 已冻结
						aryvos[j].setAttributeValue("xyy_canuse", xcl.doubleValue() - d6); // 可拨库存量
						// add by zip 2014/4/4: No 37
						// 订购点数量-(可拨库存量+已订购未到货+已申购未审批+已审批未定购+已到货未检验)
						UFDouble xyy_norderpointnum = (UFDouble) aryvos[j].getAttributeValue("norderpointnum");
//						UFDouble xyy_napplyordernum = new UFDouble(xyy_norderpointnum.doubleValue() - (xcl.doubleValue() - d6 + (d3 - d4) + d1 + (d2 - d3) + (d4 - d5)));
						//edit by zwx 2015-9-28 
						UFDouble xyy_napplyordernum = new UFDouble(xyy_norderpointnum.doubleValue() - (xcl.doubleValue() - d6 + (d3 - new4) + d1 + (d2 - new3) + (d4 - d5)));
						//end by zwx
						if(xyy_napplyordernum.doubleValue()<=0) {
							aryvos = (OrderpointVO[]) ArrayUtils.remove(aryvos, j);
							j--;
						}else {
							aryvos[j].setAttributeValue("napplyordernum", xyy_napplyordernum);
						}
						// add end
					}
					// add end
				}
			}
			// add end

			getReportBaseClass().setBodyDataVO(aryvos, true);

			// add by zip:2014/3/4 No 30
			getReportBaseClass().getHeadItem("xyy_reqdate").setValue(new UFDate(System.currentTimeMillis()));
			// add end 

			calculateTotal();
			// 这里置CheckBox可选(lijun)
			getReportBaseClass().getBillModel().setEnabled(true);
			// setTotalRow();
			// 置请购按钮可用
			m_boConfirmApply.setEnabled(true);
			m_boSelectAll.setEnabled(true);
			m_boCancelAll.setEnabled(true);
			m_boDeleteRow.setEnabled(true);
			m_boCancelDelete.setEnabled(true);
			updateButton(m_boConfirmApply);
			updateButton(m_boSelectAll);
			updateButton(m_boCancelAll);
			updateButton(m_boDeleteRow);
			updateButton(m_boCancelDelete);

		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
			showErrorMessage(e.getMessage());

		}

	}

	/**
	 * @功能:返回公司的上级公司编码
	 * @author ：cm
	 * @2012/9/5
	 * 
	 * @since v50
	 */
	public String getParentCorpCode() {

		String ParentCorp = new String();
		String key = ClientEnvironment.getInstance().getCorporation().getFathercorp();
		try {
			CorpVO corpVO = CorpBO_Client.findByPrimaryKey(key);
			ParentCorp = corpVO.getUnitcode();
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ParentCorp;
	}

	private String getXhl2(String invcode, Map map) {
		StringBuffer sb = new StringBuffer();
		Calendar cal = Calendar.getInstance();
		String month = String.valueOf((cal.get(Calendar.MONTH) + 1) < 10 ? "0" + (cal.get(Calendar.MONTH) + 1) : cal.get(Calendar.MONTH) + 1);

		sb.append("本月:").append(map.get(invcode + cal.get(Calendar.YEAR) + "-" + month) == null ? "0" : map.get(invcode + cal.get(Calendar.YEAR) + "-" + month)).append("  ");
		for (int i = 0; i < 12; i++) {
			cal.add(Calendar.MONTH, -1);
			month = String.valueOf((cal.get(Calendar.MONTH) + 1) < 10 ? "0" + (cal.get(Calendar.MONTH) + 1) : cal.get(Calendar.MONTH) + 1);
			if (cal.get(Calendar.YEAR) <= 2012) {
				break;
			}
			sb.append(month).append("月:").append(map.get(invcode + cal.get(Calendar.YEAR) + "-" + month) == null ? "0" : map.get(invcode + cal.get(Calendar.YEAR) + "-" + month)).append("  ");
		}
		return sb.toString();
	}

	/**
	 * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2001-8-16 10:48:03) 修改日期，修改人，修改原因，注释标志：
	 */
	void onSelectAll() {
		if (m_alCloneData != null) {

			getReportBaseClass().getBillModel().setNeedCalculate(false);
			int row = m_alCloneData.size();
			// update by zip: 2014/3/4 No 30
			Object headReqdate = getReportBaseClass().getHeadItem("xyy_reqdate").getValueObject();
			for (int i = 0; i < row; i++) {
				getReportBaseClass().setBodyValueAt(new UFBoolean(true), i, "selectedflag");
				getReportBaseClass().setBodyValueAt(headReqdate, i, "xyy_reqdate_b");
			}
			// add end

			getReportBaseClass().getBillModel().setNeedCalculate(true);
			getReportBaseClass().getBillModel().reCalcurateAll();
		}
	}

	/**
	 * 创建者：张欣 功能：设置新增单据的初始数据，如日期，制单人等。 参数： 返回： 例外： 日期：(2001-5-9 9:23:32)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	protected void setBillTailData() {
		try {
			// //nc.ui.pub.ClientEnvironment ce=
			// nc.ui.pub.ClientEnvironment.getInstance();
			// if (ce == null) {
			// nc.vo.scm.pub.SCMEnv.out("ce null ERR.");
			// return;
			// }
			try {
				getReportBaseClass().setTailItem("billdate", m_sLogDate);
			} catch (Exception e) {

			}
			try {
				getReportBaseClass().setTailItem("operatorcode", m_sUserCode);
				getReportBaseClass().setTailItem("operatorname", m_sUserName);

			} catch (Exception e) {

			}
			try {
				getReportBaseClass().setTailItem("unitcode", m_sCorpCode);
				getReportBaseClass().setTailItem("unitname", m_sCorpName);

			} catch (Exception e) {

			}

		} catch (Exception e) {

		}

	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-8-21 10:44:24)
	 */
	void setColEditable(String itemkey, boolean bEditable) {
		int rownum = getReportBaseClass().getBillTable().getRowCount();
		if (rownum <= 0) return;
		for (int i = 0; i < rownum; i++) {
			getReportBaseClass().setCellEditable(i, itemkey, bEditable);
		}
	}

	/**
	 * 求再定购点，结存，请购，订单，到货在检，申请定购数量， 申请定购辅数量之和。并将结果显示在结果行。 此处插入方法说明。
	 * 创建日期：(2001-8-21 12:57:06)
	 */
	void setTotalRow() {
		UFDouble sumorderpointnum = null, sumrestnum = null, sumpraynum = null, sumordernum = null, sumaccumchecknum = null, sumapplyordernum = null, sumapplyorderastnum = null;
		if (m_alCloneData != null && m_alCloneData.size() > 0 && m_iaScale != null) {
			sumorderpointnum = new UFDouble(0);
			sumrestnum = new UFDouble(0);
			sumpraynum = new UFDouble(0);
			sumordernum = new UFDouble(0);
			sumaccumchecknum = new UFDouble(0);
			sumapplyordernum = new UFDouble(0);
			sumapplyorderastnum = new UFDouble(0);
			for (int i = 0; i < getReportBaseClass().getBillTable().getRowCount(); i++) {
				sumorderpointnum = sumorderpointnum.add(getReportBaseClass().getBodyValueAt(i, "norderpointnum") == null ? new UFDouble(0) : new UFDouble(getReportBaseClass().getBodyValueAt(i, "norderpointnum").toString()));
				sumrestnum = sumrestnum.add(getReportBaseClass().getBodyValueAt(i, "restnum") == null ? new UFDouble(0) : new UFDouble(getReportBaseClass().getBodyValueAt(i, "restnum").toString()));
				sumpraynum = sumpraynum.add(getReportBaseClass().getBodyValueAt(i, "npraynum") == null ? new UFDouble(0) : new UFDouble(getReportBaseClass().getBodyValueAt(i, "npraynum").toString()));
				sumordernum = sumordernum.add(getReportBaseClass().getBodyValueAt(i, "nordernum") == null ? new UFDouble(0) : new UFDouble(getReportBaseClass().getBodyValueAt(i, "nordernum").toString()));
				sumaccumchecknum = sumaccumchecknum.add(getReportBaseClass().getBodyValueAt(i, "naccumchecknum") == null ? new UFDouble(0) : new UFDouble(getReportBaseClass().getBodyValueAt(i, "naccumchecknum").toString()));
				sumapplyordernum = sumapplyordernum.add(getReportBaseClass().getBodyValueAt(i, "napplyordernum") == null ? new UFDouble(0) : new UFDouble(getReportBaseClass().getBodyValueAt(i, "napplyordernum").toString()));
				sumapplyorderastnum = sumapplyorderastnum.add(getReportBaseClass().getBodyValueAt(i, "nwwnum") == null ? new UFDouble(0) : new UFDouble(getReportBaseClass().getBodyValueAt(i, "nwwnum").toString()));

			}

			getReportBaseClass().getTotalTableModel().setValueAt(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC000-0001146")/* @res "合计" */, 0, 0);
			getReportBaseClass().getTotalTableModel().setValueAt(sumorderpointnum.setScale(-m_iaScale[nc.vo.ic.pub.bill.DoubleScale.NUM], UFDouble.ROUND_HALF_UP), 0, 7);
			getReportBaseClass().getTotalTableModel().setValueAt(sumrestnum.setScale(-m_iaScale[nc.vo.ic.pub.bill.DoubleScale.NUM], UFDouble.ROUND_HALF_UP), 0, 11);
			getReportBaseClass().getTotalTableModel().setValueAt(sumpraynum.setScale(-m_iaScale[nc.vo.ic.pub.bill.DoubleScale.NUM], UFDouble.ROUND_HALF_UP), 0, 12);
			getReportBaseClass().getTotalTableModel().setValueAt(sumordernum.setScale(-m_iaScale[nc.vo.ic.pub.bill.DoubleScale.NUM], UFDouble.ROUND_HALF_UP), 0, 13);
			getReportBaseClass().getTotalTableModel().setValueAt(sumaccumchecknum.setScale(-m_iaScale[nc.vo.ic.pub.bill.DoubleScale.NUM], UFDouble.ROUND_HALF_UP), 0, 14);
			getReportBaseClass().getTotalTableModel().setValueAt(sumapplyorderastnum.setScale(-m_iaScale[nc.vo.ic.pub.bill.DoubleScale.NUM], UFDouble.ROUND_HALF_UP), 0, 15);
			getReportBaseClass().getTotalTableModel().setValueAt(sumapplyordernum.setScale(-m_iaScale[nc.vo.ic.pub.bill.DoubleScale.NUM], UFDouble.ROUND_HALF_UP), 0, 16);
			// getReportBase().getTotalTableModel().setValueAt(sumapplyorderastnum,
			// 0, 13);
		} else {
			getReportBaseClass().getTotalTableModel().setValueAt(null, 0, 0);
			getReportBaseClass().getTotalTableModel().setValueAt(null, 0, 7);
			getReportBaseClass().getTotalTableModel().setValueAt(null, 0, 11);
			getReportBaseClass().getTotalTableModel().setValueAt(null, 0, 12);
			getReportBaseClass().getTotalTableModel().setValueAt(null, 0, 13);
			getReportBaseClass().getTotalTableModel().setValueAt(null, 0, 14);
			getReportBaseClass().getTotalTableModel().setValueAt(null, 0, 15);
			getReportBaseClass().getTotalTableModel().setValueAt(null, 0, 16);
		}
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-4-15 15:40:33)
	 */
	private void splitArrayList(ArrayList al) {
		m_alTran = new ArrayList();
		m_alPray = new ArrayList();

		for (int i = 0; i < al.size(); i++) {
			OrderpointVO vo = (OrderpointVO) al.get(i);
			if (vo == null) continue;

			String str = (String) vo.getAttributeValue("matertype");
			if (str == null || str.trim() == "") continue;

			if (str.equals("DB")) m_alTran.add(vo);

			else m_alPray.add(vo);
		}

	}

	/**
	 * 将在定购的VO，转换为请购单的VO 功能： 参数： nc.vo.pr.pray.PraybillVO pvo, OrderpointVO ovo
	 * 返回： 例外： 日期：(2001-11-15 11:56:41) 修改日期，修改人，修改原因，注释标志：
	 */
	void transferVO(nc.vo.pr.pray.PraybillVO pvo, OrderpointVO ovo) {
		if (pvo == null || ovo == null) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000459")/*
																											* @res
																											* "VO转换时传入VO为空:transferVO()"
																											*/);
			return;
		}

	}
}