package nc.ui.pi.invoice;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nc.bs.bd.b21.BusinessCurrencyRateUtil;
import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.exception.ComponentException;
import nc.com.google.gson.Gson;
import nc.itf.arap.pub.IArapBillPublic;
import nc.itf.arap.pub.IArapForGYLPublic;
import nc.itf.ia.bill.IBill;
import nc.itf.pi.IDataPowerForInv;
import nc.itf.pi.IInvoiceD;
import nc.itf.pub.rino.IPubDMO;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.itf.uap.pfxx.IPFxxEJBService;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.net.sf.json.JSONArray;
import nc.net.sf.json.JSONObject;
import nc.ui.bd.CorpBO_Client;
import nc.ui.bd.b39.PhaseRefModel;
import nc.ui.ic.pub.BillFormulaContainer;
import nc.ui.ml.NCLangRes;
import nc.ui.pf.query.ICheckRetVO;
import nc.ui.pi.InvoiceHelper;
import nc.ui.pi.pub.AccountsForVendorRefModel;
import nc.ui.pi.pub.PiPqPublicUIClass;
import nc.ui.po.pub.PoPublicUIClass;
import nc.ui.pps.PricStlHelper;
import nc.ui.pu.pub.CheckISSellProxy;
import nc.ui.pu.pub.POPubSetUI;
import nc.ui.pu.pub.POPubSetUI2;
import nc.ui.pu.pub.PuTool;
import nc.ui.pu.pub.PubHelper;
import nc.ui.pu.pub.PurPsnRefModel;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIMenuItem;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.bill.BillBodyMenuListener;
import nc.ui.pub.bill.BillCardBeforeEditListener;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillMouseEnent;
import nc.ui.pub.bill.BillTableMouseListener;
import nc.ui.pub.bill.DefaultCurrTypeBizDecimalListener;
import nc.ui.pub.bill.DefaultCurrTypeDecimalAdapter;
import nc.ui.pub.bill.IBillRelaSortListener2;
import nc.ui.pub.linkoperate.ILinkAdd;
import nc.ui.pub.linkoperate.ILinkAddData;
import nc.ui.pub.linkoperate.ILinkApprove;
import nc.ui.pub.linkoperate.ILinkApproveData;
import nc.ui.pub.linkoperate.ILinkMaintain;
import nc.ui.pub.linkoperate.ILinkMaintainData;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.pub.para.SysInitBO_Client;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.pub.workflownote.FlowStateDlg;
import nc.ui.scm.file.DocumentManager;
import nc.ui.scm.ic.freeitem.FreeItemRefPane;
import nc.ui.scm.pub.BusiBillManageTool;
import nc.ui.scm.pub.CacheTool;
import nc.ui.scm.pub.CollectSettingDlg;
import nc.ui.scm.pub.FreeVOParse;
import nc.ui.scm.pub.bill.ButtonTree;
import nc.ui.scm.pub.bill.IBillExtendFun;
import nc.ui.scm.pub.def.DefSetTool;
import nc.ui.scm.pub.panel.RelationsCal;
import nc.ui.scm.pub.print.ISetBillVO;
import nc.ui.scm.pub.print.ScmPrintTool;
import nc.ui.scm.pub.query.SCMQueryConditionDlg;
import nc.ui.scm.pub.report.BillRowNo;
import nc.ui.scm.sourcebill.SourceBillFlowDlg;
import nc.ui.so.pub.FetchValueBO_Client;
import nc.utils.modify.is.IdetermineService;
import nc.vo.bd.CorpVO;
import nc.vo.bd.b06.PsndocVO;
import nc.vo.ep.dj.DJZBHeaderVO;
import nc.vo.ep.dj.DJZBItemVO;
import nc.vo.ep.dj.DJZBVO;
import nc.vo.fts.pub.exception.FTSBusinessException;
import nc.vo.ia.bill.BillHeaderVO;
import nc.vo.ia.bill.BillItemVO;
import nc.vo.ia.bill.BillVO;
import nc.vo.ic.pub.check.CHECKVO;
import nc.vo.ic.pub.vmi.VmiSumHeaderVO;
import nc.vo.ic.pub.vmi.VmiSumVO;
import nc.vo.pfxx.util.FileUtils;
import nc.vo.pi.HintMessageException;
import nc.vo.pi.InvoiceHeaderVO;
import nc.vo.pi.InvoiceItemVO;
import nc.vo.pi.InvoiceVO;
import nc.vo.pi.NormalCondVO;
import nc.vo.pps.PricParaVO;
import nc.vo.pu.exception.RwtPiToPoException;
import nc.vo.pu.exception.RwtPiToScException;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.ValidationException;
import nc.vo.pub.formulaset.util.StringUtil;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.ic.bill.FreeVO;
import nc.vo.scm.ic.bill.InvVO;
import nc.vo.scm.pu.BillStatus;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pu.Timer;
import nc.vo.scm.pu.VariableConst;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.wfengine.engine.exception.EngineException;
import nc.vo.wfengine.engine.exception.TaskInvalidateException;

import org.w3c.dom.Document;
import com.sun.java_cup.internal.internal_error;

/**
 * 2012-12-24.xiaolong_fan.增加打印预览合并存货功能. 2012-12-28.xiaolong_fan.供应商寄存业务修改.
 * 
 * @author fans
 * 
 */
public class InvoiceUI extends ToftPanel implements BillEditListener,
		BillEditListener2, BillCardBeforeEditListener, BillTableMouseListener,
		BillBodyMenuListener, ICheckRetVO, ListSelectionListener, ISetBillVO,
		IBillExtendFun, IBillRelaSortListener2, ILinkMaintain, ILinkAdd,
		ILinkApprove, ILinkQuery {
	private ButtonTree m_btnTree = null;
	private static final int INV_PANEL_CARD = 0;
	private static final int INV_PANEL_LIST = 1;
	protected ArrayList alcorp = null; // 列表数据//yqq
	
	BillFormulaContainer m_billFormulaContain;// 单据公式容器 yqq
	protected String m_sBillTypeCode = nc.vo.ic.pub.BillTypeConst.PO_Invoice;//yqq
	protected int m_iCurPanel = 0;  //yqq
	protected int m_iCurPanel1 = 1;  //yqq
	private boolean m_bCouldMaked;
	private boolean m_bEverQueryed = false;

	private boolean m_bAdd = false;

	private boolean isAllowedModifyByOther = false;

	private String m_sAuditControlMode = null;

	private ButtonObject m_bizButton = null;

	private ButtonObject m_btnAudit = new ButtonObject(NCLangRes.getInstance()
			.getStrByID("common", "UC001-0000027"), NCLangRes.getInstance()
			.getStrByID("common", "UC001-0000027"), 2, "审批");
	private ButtonObject m_btnUnAudit = new ButtonObject(NCLangRes
			.getInstance().getStrByID("common", "UC001-0000028"), NCLangRes
			.getInstance().getStrByID("40040401", "UPP40040401-000149"), 5,
			"弃审");

	private ButtonObject m_btnInvBillBusiType = null;

	private ButtonObject m_btnInvBillNew = null;

	private ButtonObject m_btnInvBillSave = null;

	private ButtonObject m_btnBillMaintain = null;
	private ButtonObject m_btnInvBillModify = null;
	private ButtonObject m_btnInvBillCancel = null;
	private ButtonObject m_btnInvBillDiscard = null;
	private ButtonObject m_btnInvBillCopy = null;
	private ButtonObject m_btnInvBillConversion = null;

	private ButtonObject m_btnInvBillRowOperation = null;
	private ButtonObject m_btnInvBillAddRow = null;
	private ButtonObject m_btnInvBillDeleteRow = null;
	private ButtonObject m_btnInvBillInsertRow = null;
	private ButtonObject m_btnInvBillCopyRow = null;
	private ButtonObject m_btnInvBillPasteRow = null;

	private ButtonObject m_btnInvBillExcute = null;
	private ButtonObject m_btnSendAudit = null;
	private ButtonObject m_btnInvBillAudit = null;
	private ButtonObject m_btnInvBillUnAudit = null;

	private ButtonObject m_btnInvBillQuery = null;

	private ButtonObject m_btnBillBrowsed = null;
	private ButtonObject m_btnInvBillRefresh = null;
	private ButtonObject m_btnInvBillGoFirstOne = null;
	private ButtonObject m_btnInvBillGoNextOne = null;
	private ButtonObject m_btnInvBillGoPreviousOne = null;
	private ButtonObject m_btnInvBillGoLastOne = null;
	private ButtonObject m_btnInvSelectAll = null;
	private ButtonObject m_btnInvDeselectAll = null;

	private ButtonObject m_btnInvShift = null;

	private ButtonObject m_btnPrints = null;
	private ButtonObject m_btnInvBillPreview = null;
	private ButtonObject m_btnInvBillPrint = null;
	private ButtonObject btnBillCombin = null;

	private ButtonObject m_btnInvBillAssist = null;
	private ButtonObject m_btnLnkQuery = null;
	private ButtonObject m_btnQueryForAudit = null;
	private ButtonObject m_btnHqhp = null;

	private ButtonObject m_btnOthersFuncs = null;
	private ButtonObject m_btnDocManage = null;

	// add by cm start
	/**
	 * 参照入库单取数
	 */
	private ButtonObject m_btnZiLaoW = null;
	/**
	 * 取运费单价
	 */
	private ButtonObject m_btnZiNumW = null;
	
	//add by danxionghui 
	private ButtonObject m_btnPiGai = null;
	//按相同料号修改 by danxionghui
	private ButtonObject m_btnAllGai = null;
	private ButtonObject m_xmlDc = null;    //yqq
	public static final String strBatchModify = "批改";
	
	// public int zifalg = 0;
	// add by cm end
	private String m_cauditid = null;

	private ButtonObject m_btnAuditFlowAssist = null;
	private ButtonObject[] aryForAudit = { this.m_btnAudit, this.m_btnUnAudit };

	private InvBillPanel m_InvBillPanel = null;
	private InvListPanel m_InvListPanel = null;

	private InvoiceUIQueDlg m_InvQueDlg = null;
	private InvoiceVO[] m_InvVOs;
	private boolean m_isMakedBill = false;
	private int m_nBillBrowseState;
	private int m_nCurInvVOPos;
	private int m_nCurOperState;
	private int m_nCurPanelMode = 0;
	private int m_nListOperState;
	private int m_nLstInvVOPos;
	private int m_nSelectedRowCount;
	private int m_oldCurrMoneyDigit = 2;

	protected ArrayList listSelectBillsPos = null;

	private final String m_sInvMngIDItemKey = "invcode";
	private String m_strCurBizeType;
	private String m_strBtnTag;
	private String m_strHeadHintText = NCLangRes.getInstance().getStrByID(
			"40040401", "UPP40040401-000101")
			+ "    "
			+ NCLangRes.getInstance().getStrByID("40040401",
					"UPP40040401-000026")
			+ NCLangRes.getInstance().getStrByID("40040401",
					"UPP40040401-000102");
	
	private ScmPrintTool printList = null;
	private ScmPrintTool printCard = null;

	private final int STATE_BROWSE_NORMAL = 2;
	private final int STATE_EDIT = 1;

	private final int STATE_INIT = 0;
	private final int STATE_LIST_FROM_BILLS = 5;
	private final int STATE_LIST_NORMAL = 4;
	private boolean m_bCopy = false;

	private String m_sOrder2InvoiceSettleMode = null;

	private String m_sStock2InvoiceSettleMode = null;

	private String m_sZGYF = null;
	private Hashtable hBillStatusBeforeEdit = new Hashtable();
	private int cunpos = 0;

	private int iMaxMnyDigit = 8;

	private int currentPos = 0;
	private boolean splitFlag = false;

	Hashtable cBizTypeTable = new Hashtable();

	private String[] VOsPos = null;

	private POPubSetUI2 m_cardPoPubSetUI2 = null;

	private POPubSetUI2 m_listPoPubSetUI2 = null;

	private int isEditCur = 0;

	private Vector m_vSavedVO = null;

	private String[] m_itemMny_fi = { "noriginalpaymentmny", "npaymentmny",
			"nassistpaymny" };

	private String[] m_itemMny_bu = { "noriginalcurmny", "noriginaltaxmny",
			"noriginalsummny", "nmoney", "ntaxmny", "nsummny", "nassistcurmny",
			"nassisttaxmny", "nassistsummny" };

	public InvoiceUI() {
		initi();
	}

	private void createBtnInstances() {
		this.m_btnInvBillBusiType = getBtnTree().getButton("业务类型");

		this.m_btnInvBillNew = getBtnTree().getButton("增加");

		this.m_btnInvBillSave = getBtnTree().getButton("保存");

		this.m_btnBillMaintain = getBtnTree().getButton("维护");
		this.m_btnInvBillModify = getBtnTree().getButton("修改");
		this.m_btnInvBillCancel = getBtnTree().getButton("取消");
		this.m_btnInvBillDiscard = getBtnTree().getButton("删除");
		this.m_btnInvBillCopy = getBtnTree().getButton("复制");

		this.m_btnInvBillRowOperation = getBtnTree().getButton("行操作");
		this.m_btnInvBillAddRow = getBtnTree().getButton("增行");
		this.m_btnInvBillDeleteRow = getBtnTree().getButton("删行");
		this.m_btnInvBillInsertRow = getBtnTree().getButton("插入行");
		this.m_btnInvBillCopyRow = getBtnTree().getButton("复制行");
		this.m_btnInvBillPasteRow = getBtnTree().getButton("粘贴行");

		this.m_btnInvBillExcute = getBtnTree().getButton("执行");
		this.m_btnSendAudit = getBtnTree().getButton("送审");
		this.m_btnInvBillAudit = getBtnTree().getButton("审核");
		this.m_btnInvBillUnAudit = getBtnTree().getButton("弃审");
		this.m_btnInvBillConversion = getBtnTree().getButton("放弃转单");

		this.m_btnInvBillQuery = getBtnTree().getButton("查询");

		this.m_btnBillBrowsed = getBtnTree().getButton("浏览");
		this.m_btnInvBillRefresh = getBtnTree().getButton("刷新");
		this.m_btnInvBillGoFirstOne = getBtnTree().getButton("首页");
		this.m_btnInvBillGoNextOne = getBtnTree().getButton("下页");
		this.m_btnInvBillGoPreviousOne = getBtnTree().getButton("上页");
		this.m_btnInvBillGoLastOne = getBtnTree().getButton("末页");

		this.m_btnInvSelectAll = getBtnTree().getButton("全选");
		this.m_btnInvDeselectAll = getBtnTree().getButton("全消");

		this.m_btnInvShift = getBtnTree().getButton("卡片显示/列表显示");

		this.m_btnPrints = getBtnTree().getButton("打印管理");
		this.m_btnInvBillPreview = getBtnTree().getButton("预览");
		this.m_btnInvBillPrint = getBtnTree().getButton("打印");
		this.btnBillCombin = getBtnTree().getButton("合并显示");

		this.m_btnInvBillAssist = getBtnTree().getButton("辅助查询");
		this.m_btnLnkQuery = getBtnTree().getButton("联查");
		this.m_btnQueryForAudit = getBtnTree().getButton("审批流状态");
		this.m_btnHqhp = getBtnTree().getButton("优质优价取价");

		this.m_btnOthersFuncs = getBtnTree().getButton("辅助功能");
		this.m_btnDocManage = getBtnTree().getButton("文档管理");

		// add by cm
		this.m_btnZiLaoW = getBtnTree().getButton("参照入库单取数");
		this.m_btnZiNumW = getBtnTree().getButton("取运费单价");
		
		//add by danxionghui
		this.m_btnPiGai = getBtnTree().getButton("批量修改");
		this.m_btnAllGai = getBtnTree().getButton("按料号修改");
		this.m_xmlDc = getBtnTree().getButton("XML导出");   //yqq
		// this.m_btnZiLaoW.setCode("参照入库单取数");
		// this.m_btnZiLaoW.setName("参照入库单取数");
	}

	private ButtonTree getBtnTree() {
		if (this.m_btnTree == null) {
			try {
				this.m_btnTree = new ButtonTree(getModuleCode());
			} catch (BusinessException be) {
				showHintMessage(be.getMessage());
				return null;
			}
		}
		return this.m_btnTree;
	}

	private void setMaxMnyDigit(int iMaxDigit) {
		getInvBillPanel().getBodyItem("noriginalcurmny").setDecimalDigits(
				iMaxDigit);
		getInvBillPanel().getBodyItem("noriginaltaxmny").setDecimalDigits(
				iMaxDigit);
		getInvBillPanel().getBodyItem("noriginalsummny").setDecimalDigits(
				iMaxDigit);
		getInvBillPanel().getBodyItem("noriginalpaymentmny").setDecimalDigits(
				iMaxDigit);
		getInvBillPanel().getBodyItem("nmoney").setDecimalDigits(iMaxDigit);
		getInvBillPanel().getBodyItem("ntaxmny").setDecimalDigits(iMaxDigit);
		getInvBillPanel().getBodyItem("nsummny").setDecimalDigits(iMaxDigit);
		getInvBillPanel().getBodyItem("npaymentmny")
				.setDecimalDigits(iMaxDigit);
		getInvBillPanel().getBodyItem("nassistcurmny").setDecimalDigits(
				iMaxDigit);
		getInvBillPanel().getBodyItem("nassisttaxmny").setDecimalDigits(
				iMaxDigit);
		getInvBillPanel().getBodyItem("nassistsummny").setDecimalDigits(
				iMaxDigit);
		getInvBillPanel().getBodyItem("nassistpaymny").setDecimalDigits(
				iMaxDigit);
		getInvBillPanel().getBodyItem("nexchangeotoarate").setDecimalDigits(
				iMaxDigit);
		getInvBillPanel().getBodyItem("nexchangeotobrate").setDecimalDigits(
				iMaxDigit);
	}

	private void setMaxMnyDigitList(int iMaxDigit) {
		getInvListPanel().getBodyItem("noriginalcurmny").setDecimalDigits(
				iMaxDigit);
		getInvListPanel().getBodyItem("noriginaltaxmny").setDecimalDigits(
				iMaxDigit);
		getInvListPanel().getBodyItem("noriginalsummny").setDecimalDigits(
				iMaxDigit);
		getInvListPanel().getBodyItem("noriginalpaymentmny").setDecimalDigits(
				iMaxDigit);
		getInvListPanel().getBodyItem("nmoney").setDecimalDigits(iMaxDigit);
		getInvListPanel().getBodyItem("ntaxmny").setDecimalDigits(iMaxDigit);
		getInvListPanel().getBodyItem("nsummny").setDecimalDigits(iMaxDigit);
		getInvListPanel().getBodyItem("npaymentmny")
				.setDecimalDigits(iMaxDigit);
		getInvListPanel().getBodyItem("nassistcurmny").setDecimalDigits(
				iMaxDigit);
		getInvListPanel().getBodyItem("nassisttaxmny").setDecimalDigits(
				iMaxDigit);
		getInvListPanel().getBodyItem("nassistsummny").setDecimalDigits(
				iMaxDigit);
		getInvListPanel().getBodyItem("nassistpaymny").setDecimalDigits(
				iMaxDigit);
		getInvListPanel().getBodyItem("nexchangeotoarate").setDecimalDigits(
				iMaxDigit);
		getInvListPanel().getBodyItem("nexchangeotobrate").setDecimalDigits(
				iMaxDigit);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == ((UIRefPane) getInvBillPanel().getHeadItem(
				"cemployeeid").getComponent()).getUIButton()) {
			actionPerformedEmployee(e);
		} else if (e.getSource() == ((UIRefPane) getInvBillPanel().getBodyItem(
				"cprojectphasename").getComponent()).getUIButton())
			actionPerformedProjectPhase(e);
		else if (e.getSource() == ((UIRefPane) getInvBillPanel().getBodyItem(
				"cwarehousename").getComponent()).getUIButton())
			PuTool.restrictWarehouseRefByStoreOrg(getInvBillPanel(),
					getPk_corp(), getInvBillPanel().getHeadItem(
							"cstoreorganization").getValue(), "cwarehousename");
	}

	private void actionPerformedEmployee(ActionEvent e) {
		UIRefPane pane = (UIRefPane) getInvBillPanel().getHeadItem(
				"cemployeeid").getComponent();
		pane.setRefModel(new PurPsnRefModel(getPk_corp(), null));
	}

	private void afterEditWhenBodyCurrency(BillEditEvent e) {
		int iRow = e.getRow();

		setExchangeRateBody(iRow, true, null);

		if ((e.getValue() == null)
				|| (e.getValue().toString().trim().equals(""))) {
			return;
		}

		String sCurrTypeId = PuPubVO
				.getString_TrimZeroLenAsNull(getInvBillPanel().getBodyValueAt(
						iRow, "ccurrencytypeid"));

		if (sCurrTypeId == null) {
			return;
		}

		UFDouble ninvoicenum = (UFDouble) getInvBillPanel().getBodyValueAt(
				iRow, "nexchangeotobrate");
		UFDouble ncurprice = (UFDouble) getInvBillPanel().getBodyValueAt(iRow,
				"noriginalcurprice");
		UFDouble summy = (UFDouble) getInvBillPanel().getBodyValueAt(iRow,
				"noriginalsummny");
		String sChangedKey = null;
		if ((ninvoicenum != null) && (ninvoicenum.doubleValue() > 0.0D)
				&& (ncurprice != null) && (ncurprice.doubleValue() > 0.0D))
			sChangedKey = "ninvoicenum";
		else if ((summy != null) && (summy.doubleValue() > 0.0D))
			sChangedKey = "noriginalsummny";
		else {
			sChangedKey = "ninvoicenum";
		}
		afterEditWhenBodyRelationsCal(new BillEditEvent(getInvBillPanel()
				.getBodyItem(sChangedKey), getInvBillPanel().getBodyValueAt(
				iRow, sChangedKey), sChangedKey, iRow, 1));

		updateUI();
	}

	private void afterEditWhenBodyRelationsCal(BillEditEvent e) {
		int iRow = e.getRow();
		if ((!e.getKey().equals("idiscounttaxtype")) && (e.getValue() != null)
				&& (!e.getValue().toString().trim().equals(""))) {
			UFDouble ndata = new UFDouble(e.getValue().toString().trim());
			if (ndata.doubleValue() < 0.0D) {
				if (e.getKey().equals("ntaxrate")) {
					MessageDialog.showWarningDlg(this, NCLangRes.getInstance()
							.getStrByID("SCMCOMMON", "UPPSCMCommon-000270"),
							NCLangRes.getInstance().getStrByID("40040401",
									"UPP40040401-000238"));
					getInvBillPanel().getBillModel().setValueAt(null, iRow,
							"ntaxrate");
					return;
				}
				if (e.getKey().equals("noriginalcurprice")) {
					MessageDialog.showWarningDlg(this, NCLangRes.getInstance()
							.getStrByID("SCMCOMMON", "UPPSCMCommon-000270"),
							NCLangRes.getInstance().getStrByID("40040401",
									"UPP40040401-000239"));
					getInvBillPanel().getBillModel().setValueAt(null, iRow,
							"noriginalcurprice");
					return;
				}
			}
			if ((e.getKey().equals("ndiscountrate"))
					&& (ndata.compareTo(VariableConst.ZERO) < 0)) {
				MessageDialog.showWarningDlg(this, NCLangRes.getInstance()
						.getStrByID("SCMCOMMON", "UPPSCMCommon-000270"),
						NCLangRes.getInstance().getStrByID("40040401",
								"UPP40040401-000240"));
				getInvBillPanel().getBillModel().setValueAt(null, iRow,
						"ndiscountrate");
				return;
			}

		}

		afterEditInvBillRelations(e);
	}

	private void setExchangeRateBody(int iRow, boolean bResetExchValue,
			InvoiceItemVO items) {
		String dInvoiceDate = getInvBillPanel().getHeadItem("dinvoicedate")
				.getValue();
		String sCurrId = (String) getInvBillPanel().getBodyValueAt(iRow,
				"ccurrencytypeid");
		if ((sCurrId == null) || (sCurrId.trim().length() == 0)) {
			sCurrId = getInvBillPanel().getHeadItem("ccurrencytypeid")
					.getValue();
		}

		setRowDigits_ExchangeRate(getPk_corp(), iRow, getInvBillPanel()
				.getBillModel(), this.m_cardPoPubSetUI2);
		UFDouble nexchangeotobrate = null;
		UFDouble nexchangeotoarate = null;
		if (items != null) {
			nexchangeotobrate = items.getNexchangeotobrate();
			nexchangeotoarate = items.getNexchangeotoarate();
		} else {
			nexchangeotobrate = (UFDouble) getInvBillPanel().getBodyValueAt(
					iRow, "nexchangeotobrate");
			nexchangeotoarate = (UFDouble) getInvBillPanel().getBodyValueAt(
					iRow, "nexchangeotoarate");
		}

		if ((bResetExchValue) && (dInvoiceDate != null)
				&& (dInvoiceDate.trim().length() > 0)) {
			UFDouble[] daRate = null;
			String strCurrDate = dInvoiceDate;
			if ((strCurrDate == null) || (strCurrDate.trim().length() == 0)) {
				strCurrDate = PoPublicUIClass.getLoginDate() + "";
			}
			daRate = this.m_cardPoPubSetUI2.getBothExchRateValue(getPk_corp(),
					sCurrId, new UFDate(dInvoiceDate));

			if (this.isEditCur == 1) {
				getInvBillPanel().setBodyValueAt(daRate[0], iRow,
						"nexchangeotobrate");
				getInvBillPanel().setBodyValueAt(daRate[1], iRow,
						"nexchangeotoarate");
			} else {
				if (nexchangeotobrate != null) {
					getInvBillPanel().setBodyValueAt(nexchangeotobrate, iRow,
							"nexchangeotobrate");
				} else {
					getInvBillPanel().setBodyValueAt(daRate[0], iRow,
							"nexchangeotobrate");
				}

				if (nexchangeotoarate != null) {
					getInvBillPanel().setBodyValueAt(nexchangeotoarate, iRow,
							"nexchangeotoarate");
				} else {
					getInvBillPanel().setBodyValueAt(daRate[1], iRow,
							"nexchangeotoarate");
				}
			}
		} else if ((nexchangeotobrate != null)
				&& (nexchangeotobrate.doubleValue() > 0.0D)) {
			getInvBillPanel().setBodyValueAt(nexchangeotobrate, iRow,
					"nexchangeotobrate");
			getInvBillPanel().setBodyValueAt(nexchangeotoarate, iRow,
					"nexchangeotoarate");
		} else if ((nexchangeotobrate == null) && (dInvoiceDate != null)
				&& (dInvoiceDate.trim().length() > 0)) {
			UFDouble[] daRate = null;
			String strCurrDate = dInvoiceDate;
			if ((strCurrDate == null) || (strCurrDate.trim().length() == 0)) {
				strCurrDate = PoPublicUIClass.getLoginDate() + "";
			}
			daRate = this.m_cardPoPubSetUI2.getBothExchRateValue(getPk_corp(),
					sCurrId, new UFDate(dInvoiceDate));
			getInvBillPanel().setBodyValueAt(daRate[0], iRow,
					"nexchangeotobrate");
			getInvBillPanel().setBodyValueAt(daRate[1], iRow,
					"nexchangeotoarate");
		}

		boolean[] baEditable = null;

		baEditable = this.m_cardPoPubSetUI2.getBothExchRateEditable(
				getPk_corp(), sCurrId);

		getInvBillPanel().getBillModel().setCellEditable(iRow,
				"nexchangeotobrate", baEditable[0]);

		getInvBillPanel().getBillModel().setCellEditable(iRow,
				"nexchangeotoarate", baEditable[1]);

		getInvBillPanel().getBillModel().setRowState(iRow, 2);
	}

	/** @deprecated */
	protected static void setRowDigits_ExchangeRate(String sPk_corp, int iRow,
			BillModel billModel, POPubSetUI2 setUi) {
		String sCurrId = (String) billModel.getValueAt(iRow, "ccurrencytypeid");
		int[] iaExchRateDigit = null;

		iaExchRateDigit = setUi.getBothExchRateDigit(sPk_corp, sCurrId);
		if ((iaExchRateDigit == null) || (iaExchRateDigit.length == 0)) {
			billModel.getItemByKey("nexchangeotobrate").setDecimalDigits(2);
			billModel.getItemByKey("nexchangeotoarate").setDecimalDigits(2);
		} else {
			billModel.getItemByKey("nexchangeotobrate").setDecimalDigits(
					iaExchRateDigit[0]);
			billModel.getItemByKey("nexchangeotoarate").setDecimalDigits(
					iaExchRateDigit[1]);
		}
	}

	private void actionPerformedProjectPhase(ActionEvent e) {
		int nRow = getInvBillPanel().getBillTable().getSelectedRow();

		String strProjectMngPk = (String) getInvBillPanel().getBodyValueAt(
				nRow, "cprojectid");

		UIRefPane pane = (UIRefPane) getInvBillPanel().getBodyItem(
				"cprojectphasename").getComponent();

		pane.setRefModel(new PhaseRefModel(strProjectMngPk));
	}

	private int addNewOneIntoInvVOs(InvoiceVO newVO) {
		if (getInvVOs() == null) {
			setInvVOs(new InvoiceVO[] { newVO });
			setCurVOPos(0);
			return 0;
		}

		InvoiceVO[] vos = new InvoiceVO[getInvVOs().length + 1];
		for (int i = 0; i < getInvVOs().length; ++i) {
			vos[i] = getInvVOs()[i];
		}
		vos[(vos.length - 1)] = newVO;

		setInvVOs(vos);
		setCurVOPos(getInvVOs().length - 1);
		return getCurVOPos();
	}

	public void afterEdit(BillEditEvent e) {

	    String corps = PoPublicUIClass.getLoginPk_corp();
	    //获取公司是否是国内的结果
	    IdetermineService idetermineService =(IdetermineService) NCLocator.getInstance().lookup(IdetermineService.class.getName());
	    Boolean result1 = idetermineService.check(corps);
		if (getCurPanelMode() == 0) {
			setHeadDefPK(e);
			if (e.getSource() == getInvBillPanel().getHeadItem("cvendormangid")
					.getComponent()) {
				afterEditInvBillVendor(e);
			}
			else if (e.getSource() == getInvBillPanel()
					.getHeadItem("cdeptid").getComponent()) {
				afterEditInvBillDept(e);
			} else if (e.getSource() == getInvBillPanel().getHeadItem(
					"cemployeeid").getComponent()) {
				afterEditInvBillEmployee(e);
			} else if (e.getSource() == getInvBillPanel().getHeadItem(
					"caccountbankid").getComponent()) {
				afterEditInvBillBank(e);
			} else if (e.getSource() == getInvBillPanel().getHeadItem(
					"cfreecustid").getComponent()) {
				afterEditInvBillFreeCust(e);
			} else if (e.getSource() == getInvBillPanel().getHeadItem(
					"ccurrencytypeid").getComponent()) {
				afterEditWhenHeadCurrency(e);
			} else if (e.getSource() == getInvBillPanel().getHeadItem(
					"ntaxrate").getComponent()) {
				if (getInvBillPanel().getHeadItem("ntaxrate") != null)
					getInvBillPanel().setHeadItem("ntaxrate", getInvBillPanel().getHeadItem("ntaxrate").getValue());
				//项目主键	idiscounttaxtype
			} else if (e.getSource() == getInvBillPanel().getHeadItem( "darrivedate").getComponent()) { 
				afterEditInvBillDArriveDate(e);
			} else if (e.getSource() == getInvBillPanel().getHeadItem(
					"cstoreorganization").getComponent()) {
				PuTool.afterEditStoreOrgToWarehouse(getInvBillPanel(), e,
						getPk_corp(), getInvBillPanel().getHeadItem(
								"cstoreorganization").getValue(),
						"cwarehousename", new String[] { "cwarehouseid" });

				int size = getInvBillPanel().getRowCount();
				String[] sMangIds = new String[size];

				String sCstoreorganization = getInvBillPanel().getHeadItem(
						"cstoreorganization").getValue();
				for (int i = 0; i < size; ++i) {
					sMangIds[i] = ((String) getInvBillPanel().getBillModel()
							.getValueAt(i, "cmangid"));
				}
				UFDouble[] uPrice = queryPlanPrices(sMangIds,
						sCstoreorganization);
				if (uPrice != null)
					for (int i = 0; i < size; ++i)
						getInvBillPanel().getBillModel().setValueAt(uPrice[i],
								i, "nplanprice");
			} else if (e.getSource() == getInvBillPanel().getHeadItem(
					"nexchangeotobrate").getComponent()) {
				try {
					int size = getInvBillPanel().getRowCount();
					for (int i = 0; i < size; ++i)
						computeValueFrmOriginal(i);
				} catch (Exception ex) {
					reportException(ex);
					PuTool.outException(this, ex);
					int size = getInvBillPanel().getRowCount();
					for (int i = 0; i < size; ++i) {
						getInvBillPanel().setBodyValueAt(null, i, "nmoney");
						getInvBillPanel().setBodyValueAt(null, i, "ntaxmny");
						getInvBillPanel().setBodyValueAt(null, i, "nsummny");
					}
					return;
				}
				System.out.println(e.getKey());
			} else if (e.getPos() == 1) {
				if (e.getKey().equals("cprojectname")) {
					afterEditInvBillProject(e);
				} else if ((e.getKey().equals("idiscounttaxtype"))
						|| (e.getKey().equals("ninvoicenum"))
						|| (e.getKey().equals("noriginalcurprice"))
						|| (e.getKey().equals("norgnettaxprice"))
						|| (e.getKey().equals("ntaxrate"))
						|| (e.getKey().equals("noriginalcurmny"))
						|| (e.getKey().equals("noriginaltaxmny"))
						|| (e.getKey().equals("noriginalsummny"))
						|| (e.getKey().equals("ndiscountrate"))
						|| (e.getKey().equals("npricediscountrate"))
						|| (e.getKey().equals("nexchangeotobrate"))
						|| (e.getKey().equals("nexchangeotoarate"))) {
					try {
						afterEditInvBillRelations(e);
						computeValueFrmOriginal(e.getRow());
					} catch (Exception ex) {
						reportException(ex);
						PuTool.outException(this, ex);
						getInvBillPanel().setBodyValueAt(null, e.getRow(),
								"nmoney");
						getInvBillPanel().setBodyValueAt(null, e.getRow(),
								"ntaxmny");
						getInvBillPanel().setBodyValueAt(null, e.getRow(),
								"nsummny");
						return;
					}
					System.out.println(e.getSource());
				/**
				 *  add 1016公司 折扣额 彭佳佳 2018年6月15日15:55:28
				 */
				} 
				else if (e.getKey().equals("b_cjje2")) {    
					int size = this.getInvBillPanel().getRowCount();
					UFDouble summ=new UFDouble(0.0);
				//	价税合计=金额+税额-实际抵扣金额
					for (int j = 0; j < size; j++){		
						UFDouble noriginalcurmny = getInvBillPanel().getBodyValueAt(j,"noriginalcurmny")== null ? 
								new UFDouble(0.0): (UFDouble)(getInvBillPanel().getBodyValueAt(j,"noriginalcurmny")); 
						UFDouble noriginaltaxmny = getInvBillPanel().getBodyValueAt(j,"noriginaltaxmny")== null ? 
								new UFDouble(0.0): (UFDouble)(getInvBillPanel().getBodyValueAt(j,"noriginaltaxmny")); 	
			 	        String b_cjje2 = getInvBillPanel().getBodyValueAt(j,"b_cjje2")==null?"0":getInvBillPanel().getBodyValueAt(j,"b_cjje2").toString();   
			 	        summ = noriginalcurmny.add((noriginaltaxmny).sub(new UFDouble(b_cjje2)));
						getInvBillPanel().setBodyValueAt(summ, j, "noriginalsummny");  				         
					}
				/**
				 * 结束位置
				 */
										
				}  else if (e.getKey().equals("vfree0")) {
					afterEditInvBillFree(e);
				} else if (e.getKey().equals("vmemo")) {
					afterEditInvBillBodyMemo(e);
				} else if (e.getKey().equals("invcode")) {
					afterEditInvBillInvcode(e);
				} else if (e.getKey().equals("currname")) {
					this.isEditCur = 1;
					afterEditWhenBodyCurrency(e);
					this.isEditCur = 0;
				}

				setBodyDefPK(e);
			}
		}
		if (e.getKey().equals("crowno"))
			BillRowNo.afterEditWhenRowNo(getInvBillPanel(), e, "25");
		if(result1){
		if (e.getKey().equals("cvoucherid")
		   ){    
		  try {

		  DecimalFormat df = new DecimalFormat("##.00");
			  String zfje =new String ();
			  UFDouble summ =new UFDouble(0.0);
			  //根据采购订单获取核销金额
			//   Object corderid = getInvBillPanel().getHeadItem("corderid").getValueObject();
			//获取核销的值
		   Object hxbalance = getInvBillPanel().getHeadItem("cvoucherid").getValueObject();
		   //获取采购订单ID
		   int  selCol=getInvBillPanel().getBillTable().getSelectedColumn();
		   Object corderid =getInvBillPanel().getBodyValueAt(0, "corderid");
		   System.out.println("采购订单ID："+corderid);
		   //Object corderid = getInvBillPanel().getBodyItem("corderid").getValueObject();
		   //公司
		   Object corp = getInvBillPanel().getHeadItem("pk_corp").getValueObject();
		   System.out.println("corderid:"+corderid);
		   String a =String.valueOf(hxbalance);
		   UFDouble uf = new UFDouble(hxbalance.toString());
		   //查看核销记录表中是否有本次核销的过的金额
		   StringBuffer sqls = new StringBuffer();
		    sqls.append("select yfbalance from po_check where corderid ='"+corderid+"' and dr=0 and pk_corp ='"+corp+"' ");
		   IUAPQueryBS  uaps = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		   List  lists = (List<String>) uaps.executeQuery(sqls.toString(), new MapListProcessor());
		   if(lists==null||lists.size()==0){		   
			   //查看预付款单中的金额
			   StringBuffer sql = new StringBuffer();
			   sql.append("select zfje from arap_fksqd where VDEF1 = '"+corderid+"' and nvl(dr,0)=0 and pk_corp = '"+corp+"'");
			   System.out.println(sql);

			   IUAPQueryBS  uap = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());        
			   List list;
			   list = (List<String>) uap.executeQuery(sql.toString(), new MapListProcessor());
			   JSONObject js = new JSONObject();
			   js = JSONObject.fromObject(list.get(0));    
			   zfje = (String) js.get("zfje");		  
			   UFDouble ufd = new UFDouble(zfje);
			   System.out.println(hxbalance);
			   System.out.println(zfje);
			   if(Double.valueOf(hxbalance.toString())>Double.valueOf(zfje.toString())){
				   showWarningMessage("核销金额不能大于预付金额");
				   getInvBillPanel().setHeadItem("cvoucherid", null);
		    		return;
			   }
			   summ = ufd.sub(uf);
			   Double value = Double.valueOf(zfje) -Double.valueOf(a);
			  String result = String.format("%.2f", value);
			   getInvBillPanel().setHeadItem("cfreecustid",result);
		   }else{
			   //如果已经核销过 就从核销余额中查找
		   StringBuffer sql = new StringBuffer();
		   sql.append("select yfbalance from po_check where corderid ='"+corderid+"' and dr=0 and pk_corp='"+corp+"' ORDER BY ts desc");
		   IUAPQueryBS  uap = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
		   List list = (List<String>) uap.executeQuery(sql.toString(), new MapListProcessor());
		 		   
		   Map map = (Map) list.get(0);
		   String value =map.get("yfbalance").toString();
		   UFDouble ufd = new UFDouble(value);
		   if(Double.valueOf(value)==0&&Double.valueOf(hxbalance.toString())!=0){
			   showWarningMessage("此预付款已经核销完");
			   getInvBillPanel().setHeadItem("cvoucherid", null);
	    		return;
		   }
		   if(Double.valueOf(hxbalance.toString())>Double.valueOf(value.toString())){
			   showWarningMessage("核销金额不能大于预付金额");
			   getInvBillPanel().setHeadItem("cfreecustid", null);

	    		return;
		   }
		   
		   summ = ufd.sub(uf);

		   Double results = Double.valueOf(value.toString())- Double.valueOf(hxbalance.toString());
		   getInvBillPanel().setHeadItem("cfreecustid",String.format("%.2f", results));
		   }
		  } catch (BusinessException e1) {
		   // TODO Auto-generated catch block
		   e1.printStackTrace();
		  }
		}
		}
	}

	private void afterEditInvBillBank(BillEditEvent e) {
		UIRefPane pane = (UIRefPane) getInvBillPanel().getHeadItem(
				"caccountbankid").getComponent();
		getInvBillPanel().setHeadItem("cvendoraccount", pane.getRefCode());
	}

	private void afterEditInvBillBodyMemo(BillEditEvent e) {
		String str = ((UIRefPane) getInvBillPanel().getBodyItem("vmemo")
				.getComponent()).getRefName();
		if ((str != null) && (!str.trim().equals("")))
			getInvBillPanel().setBodyValueAt(str, e.getRow(), "vmemo");
		else
			getInvBillPanel().setBodyValueAt(
					((UIRefPane) getInvBillPanel().getBodyItem("vmemo")
							.getComponent()).getText(), e.getRow(), "vmemo");
	}

	private void afterEditWhenHeadCurrency(BillEditEvent e) {
		boolean bOldNeedCalc = getInvBillPanel().getBillModel()
				.isNeedCalculate();
		getInvBillPanel().getBillModel().setNeedCalculate(false);
		try {
			String sCurrId = getInvBillPanel().getHeadItem("ccurrencytypeid")
					.getValue();
			String dInvoiceDate = getInvBillPanel().getHeadItem("dinvoicedate")
					.getValue();

			setExchangeRateHead(dInvoiceDate, sCurrId);

			setCurrMoneyDigitAndReCalToBill(sCurrId);

			if ((sCurrId == null) || (sCurrId.trim().equals(""))
					|| (getInvBillPanel().getBillModel().getRowCount() == 0)) {
				return;
			}
			ArrayList listAllCurrId = new ArrayList();
			listAllCurrId.add(sCurrId);
			BusinessCurrencyRateUtil bca = new BusinessCurrencyRateUtil(
					getPk_corp());

			if ((bca.getLocalCurrPK() != null)
					&& (!listAllCurrId.contains(bca.getLocalCurrPK()))) {
				listAllCurrId.add(bca.getLocalCurrPK());
			}
			if ((bca.getFracCurrPK() != null)
					&& (!listAllCurrId.contains(bca.getFracCurrPK()))) {
				listAllCurrId.add(bca.getFracCurrPK());
			}

			String sBodyCurrId = null;
			int iLen = getInvBillPanel().getRowCount();
			for (int i = 0; i < iLen; ++i) {
				sBodyCurrId = (String) getInvBillPanel().getBillModel()
						.getValueAt(i, "ccurrencytypeid");
				if ((sBodyCurrId == null) || (sBodyCurrId.equals(""))) {
					getInvBillPanel().getBillModel().setValueAt(sCurrId, i,
							"ccurrencytypeid");

					afterEditWhenBodyCurrency(new BillEditEvent(
							getInvBillPanel().getBodyItem("ccurrencytypeid")
									.getComponent(), sCurrId,
							"ccurrencytypeid", i));

					getInvBillPanel().getBillModel().setRowState(i, 2);
				}
			}
			getInvBillPanel().getBillModel().execEditFormulaByKey(-1,
					"currname");
			try {
				int size = getInvBillPanel().getRowCount();
				for (int i = 0; i < size; ++i)
					computeValueFrmOriginal(i);
			} catch (Exception ex) {
				reportException(ex);
				PuTool.outException(this, ex);
				int size = getInvBillPanel().getRowCount();
				for (int i = 0; i < size; ++i) {
					getInvBillPanel().setBodyValueAt(null, i, "nmoney");
					getInvBillPanel().setBodyValueAt(null, i, "ntaxmny");
					getInvBillPanel().setBodyValueAt(null, i, "nsummny");
				}
			}
		} catch (Exception exp) {
			PuTool.outException(this, exp);

			getInvBillPanel().getBillModel().setNeedCalculate(bOldNeedCalc);
			return;
		}

		getInvBillPanel().getBillModel().setNeedCalculate(bOldNeedCalc);
	}

	private void setExchangeRateHead(String dInvoicedate, String sCurrId) {
		sCurrId = ((sCurrId == null) || (sCurrId.trim().length() == 0)) ? null
				: sCurrId;

		int[] iaExchRateDigit = this.m_cardPoPubSetUI2.getBothExchRateDigit(
				getPk_corp(), sCurrId);

		getInvBillPanel().getHeadItem("nexchangeotobrate").setDecimalDigits(
				iaExchRateDigit[0]);
		getInvBillPanel().getHeadItem("nexchangeotoarate").setDecimalDigits(
				iaExchRateDigit[1]);

		UFDouble[] daRate = this.m_cardPoPubSetUI2.getBothExchRateValue(
				getPk_corp(), sCurrId, new UFDate(dInvoicedate));

		getInvBillPanel().getHeadItem("nexchangeotobrate").setValue(daRate[0]);
		getInvBillPanel().getHeadItem("nexchangeotoarate").setValue(daRate[1]);

		boolean[] iaEditable = this.m_cardPoPubSetUI2.getBothExchRateEditable(
				getPk_corp(), sCurrId);

		getInvBillPanel().getHeadItem("nexchangeotobrate").setEnabled(
				iaEditable[0]);
		getInvBillPanel().getHeadItem("nexchangeotoarate").setEnabled(
				iaEditable[1]);
	}

	private void afterEditInvBillDArriveDate(BillEditEvent e) {
		String strCurr = getInvBillPanel().getHeadItem("ccurrencytypeid")
				.getValue();
		setCurrRateToBillHead(strCurr, null, null);
	}

	private void afterEditInvBillDept(BillEditEvent e) {
		UIRefPane pane = (UIRefPane) getInvBillPanel().getHeadItem("cdeptid")
				.getComponent();
		if ((pane.getRefPK() == null) || (pane.getRefPK().trim().length() < 1))
			return;
	}

	private void afterEditInvBillEmployee(BillEditEvent e) {
		String cemployeeid = ((UIRefPane) getInvBillPanel().getHeadItem(
				"cemployeeid").getComponent()).getRefPK();
		if ((cemployeeid == null) || (cemployeeid.trim().length() < 1)) {
			return;
		}
		String newDeptid = (String) PiPqPublicUIClass.getAResultFromTable(
				"bd_psndoc", "pk_deptdoc", "pk_psndoc", cemployeeid);

		UIRefPane pane = (UIRefPane) getInvBillPanel().getHeadItem("cdeptid")
				.getComponent();
		pane.setPK(newDeptid);
	}

	private void afterEditInvBillFree(BillEditEvent e) {
		FreeVO freeVO = ((FreeItemRefPane) getInvBillPanel().getBodyItem(
				"vfree0").getComponent()).getFreeVO();
		if (freeVO == null) {
			for (int i = 0; i < 5; ++i) {
				String str = "vfree" + new Integer(i + 1).toString();
				getInvBillPanel().setBodyValueAt(null, e.getRow(), str);
			}
		} else {
			for (int i = 0; i < 5; ++i) {
				String strName = "vfreename" + new Integer(i + 1).toString();
				if (freeVO.getAttributeValue(strName) != null) {
					String str = "vfree" + new Integer(i + 1).toString();
					Object ob = freeVO.getAttributeValue(str);
					getInvBillPanel().setBodyValueAt(ob, e.getRow(), str);
				}
			}

		}

		InvVO invVO = new InvVO();
		((FreeItemRefPane) getInvBillPanel().getBodyItem("vfree0")
				.getComponent()).setFreeItemParam(invVO);
	}

	private void afterEditInvBillFreeCust(BillEditEvent e) {
		String freeId = getInvBillPanel().getHeadItem("cfreecustid").getValue();
		setDefaultInfoForAFreeCust(freeId);
	}

	private void afterEditInvBillInvcode(BillEditEvent e) {
		UIRefPane refpane = (UIRefPane) getInvBillPanel()
				.getBodyItem("invcode").getComponent();
		Object[] saMangId = (Object[]) (Object[]) refpane
				.getRefValues("bd_invmandoc.pk_invmandoc");

		int nRow = getInvBillPanel().getRowCount();
		InvoiceVO VO = new InvoiceVO(nRow);
		getInvBillPanel().getBillValueVO(VO);

		int iInsertLen = (saMangId == null) ? 0 : saMangId.length - 1;
		int iBeginRow = e.getRow();
		if (iBeginRow == getInvBillPanel().getRowCount() - 1) {
			for (int i = 0; i < iInsertLen; ++i) {
				onActionAppendLine();
			}
		} else {
			onActionInsertLines(iBeginRow, iBeginRow + 1, iInsertLen);
		}
		int iEndRow = iBeginRow + iInsertLen;

		String sCurrId = getInvBillPanel().getHeadItem("ccurrencytypeid")
				.getValue();
		String sBodyCurrId = null;
		for (int i = iBeginRow; i <= iEndRow; ++i) {
			getInvBillPanel().setBodyValueAt(null, i, "vfree0");
			getInvBillPanel().setBodyValueAt(null, i, "vfree1");
			getInvBillPanel().setBodyValueAt(null, i, "vfree2");
			getInvBillPanel().setBodyValueAt(null, i, "vfree3");
			getInvBillPanel().setBodyValueAt(null, i, "vfree4");
			getInvBillPanel().setBodyValueAt(null, i, "vfree5");

			sBodyCurrId = (String) getInvBillPanel().getBillModel().getValueAt(
					i, "ccurrencytypeid");
			if ((sBodyCurrId == null) || (sBodyCurrId.equals(""))) {
				getInvBillPanel().getBillModel().setValueAt(sCurrId, i,
						"ccurrencytypeid");

				afterEditWhenBodyCurrency(new BillEditEvent(getInvBillPanel()
						.getBodyItem("ccurrencytypeid").getComponent(),
						sCurrId, "ccurrencytypeid", i));

				getInvBillPanel().getBillModel().setRowState(i, 2);
				getInvBillPanel().getBillModel().getValueAt(i,
						"ccurrencytypeid");
			}

			setExchangeRateBody(i, true, null);
		}

		setInvEditFormulaInfo(getInvBillPanel(), refpane, iBeginRow, iEndRow);
		getInvBillPanel().getBillModel().execEditFormulaByKey(-1, "currname");

		setRelated_Taxrate(iBeginRow, iEndRow);

		for (int i = iBeginRow; i <= iEndRow; ++i) {
			String sMangId = getInvBillPanel().getBodyValueAt(i, "cmangid")
					.toString();
			if ((sMangId == null) || (sMangId.trim().length() == 0))
				getInvBillPanel().getBillModel().setCellEditable(i,
						"vproducenum", false);
			else {
				getInvBillPanel().getBillModel().setCellEditable(i,
						"vproducenum", PuTool.isBatchManaged(sMangId));
			}

			getInvBillPanel().setBodyValueAt(null, i, "vproducenum");
		}

		String sCstoreorganization = getInvBillPanel().getHeadItem(
				"cstoreorganization").getValue();
		int size = saMangId.length;
		String[] sMangIds = new String[size];
		for (int i = 0; i < size; ++i) {
			sMangIds[i] = saMangId[i].toString();
		}
		UFDouble[] uPrice = queryPlanPrices(sMangIds, sCstoreorganization);
		if (uPrice != null) {
			for (int i = iBeginRow; i <= iEndRow; ++i) {
				getInvBillPanel().getBillModel().setValueAt(
						uPrice[(i - iBeginRow)], i, "nplanprice");
			}
		}
		BillItem it = getInvBillPanel().getBodyItem(e.getKey());
		if ((it.getEditFormulas() != null) && (it.getEditFormulas().length > 0))
			getInvBillPanel().getBillModel().execFormulas(it.getEditFormulas(),
					iBeginRow, iEndRow);
	}

	private void afterEditInvBillProject(BillEditEvent e) {
		int n = e.getRow();
		if (n < 0) {
			return;
		}
		Object oTemp = getInvBillPanel().getBodyValueAt(n, "cprojectname");
		if ((oTemp == null) || (oTemp.toString().length() == 0)) {
			getInvBillPanel().getBillModel().setCellEditable(n,
					"cprojectphasename", false);
			getInvBillPanel().setBodyValueAt(null, n, "cprojectphaseid");
			getInvBillPanel().setBodyValueAt(null, n, "cprojectphasename");
		} else {
			getInvBillPanel().getBillModel().setCellEditable(n,
					"cprojectphasename", true);
		}
	}

	private void afterEditInvBillRelations(BillEditEvent e) {
		int iRow = e.getRow();

		int[] descriptions = { 0, 1, 2, 7, 8, 11, 13, 12, 14 };

		InvoiceVO tempVO = new InvoiceVO(getInvBillPanel().getRowCount());
		getInvBillPanel().getBillValueVO(tempVO);

		String s = "应税内含";
		InvoiceItemVO[] bodyVO = tempVO.getBodyVO();

		if ((bodyVO != null) && (bodyVO.length > 0)) {
			if ((bodyVO[e.getRow()].getIdiscounttaxtype() == null)
					|| (bodyVO[e.getRow()].getIdiscounttaxtype().toString()
							.trim().length() == 0)) {
				if (((UIComboBox) getInvBillPanel().getHeadItem(
						"idiscounttaxtype").getComponent()).getSelectedItem() == null)
					bodyVO[e.getRow()].setIdiscounttaxtype(new Integer(1));
				else if (((UIComboBox) getInvBillPanel().getHeadItem(
						"idiscounttaxtype").getComponent()).getSelectedItem()
						.equals("应税内含"))
					bodyVO[e.getRow()].setIdiscounttaxtype(new Integer(0));
				else if (((UIComboBox) getInvBillPanel().getHeadItem(
						"idiscounttaxtype").getComponent()).getSelectedItem()
						.equals("应税外加"))
					bodyVO[e.getRow()].setIdiscounttaxtype(new Integer(1));
				else if (((UIComboBox) getInvBillPanel().getHeadItem(
						"idiscounttaxtype").getComponent()).getSelectedItem()
						.equals("不计税")) {
					bodyVO[e.getRow()].setIdiscounttaxtype(new Integer(2));
				}

			} else if (bodyVO[e.getRow()].getIdiscounttaxtype().intValue() == 1)
				s = "应税外加";
			else if (bodyVO[e.getRow()].getIdiscounttaxtype().intValue() == 2)
				s = "不计税";
		}

		String[] keys = { s, "idiscounttaxtype", "ninvoicenum",
				"noriginalcurprice", "norgnettaxprice", "ntaxrate",
				"noriginalcurmny", "noriginaltaxmny", "noriginalsummny" };

		RelationsCal.calculate(e, getInvBillPanel(), new int[] { PuTool
				.getPricePriorPolicy(getPk_corp()) }, descriptions, keys,
				InvoiceItemVO.class.getName());
	}

	private void afterEditInvBillVendor(BillEditEvent e) {
		if ((e.getValue() == null) || (e.getValue().equals(""))) {
			getInvBillPanel().setHeadItem("ccurrencytypeid", null);

			getInvBillPanel().setHeadItem("ctermprotocolid", null);

			getInvBillPanel().getHeadItem("caccountbankid").setValue(null);
			getInvBillPanel().getHeadItem("caccountbankid").setEnabled(false);
			getInvBillPanel().setHeadItem("cvendoraccount", null);
			getInvBillPanel().setHeadItem("cvendorphone", null);
		} else {
			String cvendormangid = getInvBillPanel().getHeadItem(
					"cvendormangid").getValue();

			String cvendorbaseid = (String) PiPqPublicUIClass
					.getAResultFromTable("bd_cumandoc", "pk_cubasdoc",
							"pk_cumandoc", cvendormangid);
			getInvBillPanel().setHeadItem("cvendorbaseid", cvendorbaseid);

			String strCurr = (String) PiPqPublicUIClass
					.getAResultFromTable("bd_cumandoc", "pk_currtype1",
							"pk_cumandoc", cvendormangid);
			if ((strCurr != null) && (strCurr.trim().length() > 1)) {
				getInvBillPanel().setHeadItem("ccurrencytypeid", strCurr);
				setCurrRateToBillHead(strCurr, null, null);
				setCurrMoneyDigitAndReCalToBill(strCurr);

				afterEditWhenHeadCurrency(e);
			}

			String payTermId = getInvBillPanel().getHeadItem("ctermprotocolid")
					.getValue();
			if ((payTermId == null) || (payTermId.trim().length() < 1)) {
				payTermId = (String) PiPqPublicUIClass.getAResultFromTable(
						"bd_cumandoc", "pk_payterm", "pk_cumandoc",
						cvendormangid);
				getInvBillPanel().setHeadItem("ctermprotocolid", payTermId);
			}

			String sPayUnit = getInvBillPanel().getHeadItem("cpayunit")
					.getValue();
			if ((sPayUnit == null) || (sPayUnit.trim().length() == 0)) {
				getInvBillPanel().setHeadItem("cpayunit", cvendormangid);
			}

			String strFreeFlag = (String) PiPqPublicUIClass
					.getAResultFromTable("bd_cubasdoc", "freecustflag",
							"pk_cubasdoc", cvendorbaseid);
			if (strFreeFlag.equals("N")) {
				getInvBillPanel().getHeadItem("cfreecustid").setEnabled(false);
				getInvBillPanel().getHeadItem("cfreecustid").setValue(null);
				((UIRefPane) getInvBillPanel().getHeadItem("cfreecustid")
						.getComponent()).getUITextField().setText(null);
				((UIRefPane) getInvBillPanel().getHeadItem("caccountbankid")
						.getComponent()).setButtonVisible(true);
				getInvBillPanel().getHeadItem("caccountbankid")
						.setEnabled(true);

				setDefaultBankAccountForAVendor(cvendorbaseid);

				setDefaultPhoneForAVendor(cvendorbaseid);
			} else {
				getInvBillPanel().getHeadItem("cfreecustid").setEnabled(true);

				((UIRefPane) getInvBillPanel().getHeadItem("caccountbankid")
						.getComponent()).setButtonVisible(false);
				getInvBillPanel().getHeadItem("caccountbankid").setEnabled(
						false);

				getInvBillPanel().setHeadItem("cvendorphone", null);
				getInvBillPanel().setHeadItem("caccountbankid", null);
				((UIRefPane) getInvBillPanel().getHeadItem("caccountbankid")
						.getComponent()).getUITextField().setText(null);
				getInvBillPanel().setHeadItem("cvendoraccount", null);
			}

			if (getInvBillPanel().getHeadItem("cdeptid").getValue() == null) {
				String deptID = (String) PiPqPublicUIClass.getAResultFromTable(
						"bd_cumandoc", "pk_respdept1", "pk_cumandoc",
						cvendormangid);
				getInvBillPanel().setHeadItem("cdeptid", deptID);
			}

			if (getInvBillPanel().getHeadItem("cemployeeid").getValue() != null)
				return;
			String employeeID = (String) PiPqPublicUIClass.getAResultFromTable(
					"bd_cumandoc", "pk_resppsn1", "pk_cumandoc", cvendormangid);
			getInvBillPanel().setHeadItem("cemployeeid", employeeID);
		}
	}

	public boolean beforeEdit(BillEditEvent e) {
		if ((getCurPanelMode() == 0) && (e.getPos() == 1)) {
			if (e.getKey().equals("vfree0")) {
				return PuTool.beforeEditInvBillBodyFree(getInvBillPanel(), e,
						new String[] { "cmangid", "invcode", "invname",
								"invspec", "invtype" }, new String[] {
								"vfree0", "vfree1", "vfree2", "vfree3",
								"vfree4", "vfree5" });
			}

			if (e.getKey().equals("vmemo")) {
				return beforeEditInvBillBodyMemo(e);
			}

			if (e.getKey().equals("nexchangeotobrate")) {
				setExchangeRateBody(e.getRow(), true, null);
			} else if (e.getKey().equals("nexchangeotoarate")) {
				setExchangeRateBody(e.getRow(), true, null);
			} else if ((e.getKey().equals("noriginalcurmny"))
					|| (e.getKey().equals("noriginaltaxmny"))
					|| (e.getKey().equals("noriginalsummny"))
					|| (e.getKey().equals("nmoney"))
					|| (e.getKey().equals("ntaxmny"))
					|| (e.getKey().equals("nsummny"))) {
				try {
					computeValueFrmOriginal(e.getRow());
				} catch (Exception ex) {
					SCMEnv.out(e);
				}

			} else if (e.getKey().equals("vproducenum")) {
				PuTool.beforeEditWhenBodyBatch(getInvBillPanel(), getPk_corp(),
						e, new String[] { "cmangid", "invcode", "invname",
								"invspec", "invtype", "measname", null, null,
								"cwarehouseid" }, "vfree");
			} else if (e.getKey().equals("cwarehousename")) {
				Object oCupsourceBillType = getInvBillPanel().getBodyValueAt(
						e.getRow(), "cupsourcebilltype");
				if ((oCupsourceBillType != null)
						&& (oCupsourceBillType.toString().trim().length() > 0))
					if (oCupsourceBillType.toString().equals("45")) {
						showHintMessage(NCLangRes.getInstance().getStrByID(
								"40040401", "UPP40040401-000009"));
						getInvBillPanel().getBodyItem("cwarehousename")
								.setEnabled(false);
					} else {
						getInvBillPanel().getBodyItem("cwarehousename")
								.setEnabled(true);
					}
			} else if (e.getKey().equals("invcode")) {
				String csourcebilltype;
				if ((getInvVOs() != null)
						&& (getInvVOs()[getCurVOPos()] != null)) {
					InvoiceVO vo = getInvVOs()[getCurVOPos()];
					if ((vo != null) && (vo.getBodyVO() != null)) {
						csourcebilltype = vo.getBodyVO()[0]
								.getCsourcebilltype();
					}

				}

				UIRefPane refpane = (UIRefPane) getInvBillPanel().getBodyItem(
						"invcode").getComponent();
				refpane.setPK(null);
			} else if (e.getKey().equals("cprojectphasename")) {
				Object oTemp = getInvBillPanel().getBodyValueAt(e.getRow(),
						"cprojectid");
				if ((oTemp == null) || (oTemp.toString().trim().length() == 0)) {
					getInvBillPanel().setCellEditable(e.getRow(),
							"cprojectphasename", false);
				} else {
					getInvBillPanel().setCellEditable(e.getRow(),
							"cprojectphasename", true);
					UIRefPane refpane = (UIRefPane) getInvBillPanel()
							.getBodyItem("cprojectphasename").getComponent();
					String s = refpane.getRefModel().getWherePart();
					s = s + " and bd_jobobjpha.pk_jobmngfil = '"
							+ oTemp.toString() + "' ";
					refpane.setWhereString(s);
				}
			}
		}

		return true;
	}

	public boolean beforeEdit(BillItemEvent e) {
		if (e.getSource().equals(
				getInvBillPanel().getHeadItem("cstoreorganization"))) {
			PuTool.restrictStoreOrg(getInvBillPanel().getHeadItem(
					"cstoreorganization").getComponent(), false);
		}

		return true;
	}

	private boolean beforeEditInvBillBodyMemo(BillEditEvent e) {
		getInvBillPanel().stopEditing();

		Object ob = getInvBillPanel().getBodyValueAt(e.getRow(), "vmemo");
		((UIRefPane) getInvBillPanel().getBodyItem("vmemo").getComponent())
				.setText((String) ob);

		return true;
	}

	public void bodyRowChange(BillEditEvent e) {
	}

	private boolean calNativeAndAssistCurrValue(InvoiceVO invVO) {
		UFDouble dTemp = null;
		String pk_corp = invVO.getHeadVO().getPk_corp();
		try {
			if (POPubSetUI.getCurrArith_Busi(pk_corp).getLocalCurrPK() == null) {
				MessageDialog.showErrorDlg(this, NCLangRes.getInstance()
						.getStrByID("40040401", "UPPSCMCommon-000059"),
						NCLangRes.getInstance().getStrByID("40040401",
								"UPP40040401-000010"));
				return false;
			}

			if ((POPubSetUI.getCurrArith_Busi(pk_corp).isBlnLocalFrac())
					&& (POPubSetUI.getCurrArith_Busi(pk_corp).getFracCurrPK() == null)) {
				MessageDialog.showErrorDlg(this, NCLangRes.getInstance()
						.getStrByID("40040401", "UPPSCMCommon-000059"),
						NCLangRes.getInstance().getStrByID("40040401",
								"UPP40040401-000011"));
				return false;
			}

			String sHint = PiPqPublicUIClass.checkBothExchRateNull(invVO);
			if (sHint != null) {
				MessageDialog.showErrorDlg(this, NCLangRes.getInstance()
						.getStrByID("40040401", "UPPSCMCommon-000059"), sHint);
				return false;
			}

			String strRateDate = invVO.getHeadVO().getDarrivedate().toString();

			for (int i = 0; i < invVO.getBodyVO().length; ++i) {
				InvoiceItemVO itemVO = invVO.getBodyVO()[i];

				if (!POPubSetUI.getCurrArith_Busi(pk_corp).isBlnLocalFrac()) {
					UFDouble nmoney = POPubSetUI.getCurrArith_Busi(pk_corp)
							.getAmountByOpp(
									itemVO.getCcurrencytypeid(),
									POPubSetUI.getCurrArith_Busi(pk_corp)
											.getLocalCurrPK(),
									itemVO.getNoriginalcurmny(),
									itemVO.getNexchangeotobrate(), strRateDate);

					UFDouble nsummny = POPubSetUI.getCurrArith_Busi(pk_corp)
							.getAmountByOpp(
									itemVO.getCcurrencytypeid(),
									POPubSetUI.getCurrArith_Busi(pk_corp)
											.getLocalCurrPK(),
									itemVO.getNoriginalsummny(),
									itemVO.getNexchangeotobrate(), strRateDate);

					invVO.getBodyVO()[i].setNmoney(nmoney);
					invVO.getBodyVO()[i].setNsummny(nsummny);
					if ((nsummny != null) && (nmoney != null)) {
						invVO.getBodyVO()[i].setNtaxmny(nsummny.sub(nmoney));
					}

					nmoney = POPubSetUI.getCurrArith_Finance(pk_corp)
							.getAmountByOpp(
									itemVO.getCcurrencytypeid(),
									POPubSetUI.getCurrArith_Busi(pk_corp)
											.getLocalCurrPK(),
									itemVO.getNoriginalpaymentmny(),
									itemVO.getNexchangeotobrate(), strRateDate);

					invVO.getBodyVO()[i].setNpaymentmny(nmoney);
				}

				if ((!POPubSetUI.getCurrArith_Busi(pk_corp).isBlnLocalFrac())
						|| (POPubSetUI.getCurrArith_Busi(pk_corp)
								.getLocalCurrPK().equals(itemVO
								.getCcurrencytypeid()))) {
					itemVO.setNexchangeotoarate(dTemp);
					itemVO.setNassistcurmny(dTemp);
					itemVO.setNassistsummny(dTemp);
					itemVO.setNassisttaxmny(dTemp);
				} else {
					UFDouble nmoney = POPubSetUI.getCurrArith_Busi(pk_corp)
							.getAmountByOpp(
									itemVO.getCcurrencytypeid(),
									POPubSetUI.getCurrArith_Busi(pk_corp)
											.getFracCurrPK(),
									itemVO.getNoriginalcurmny(),
									itemVO.getNexchangeotoarate(), strRateDate);

					UFDouble nsummny = POPubSetUI.getCurrArith_Busi(pk_corp)
							.getAmountByOpp(
									itemVO.getCcurrencytypeid(),
									POPubSetUI.getCurrArith_Busi(pk_corp)
											.getFracCurrPK(),
									itemVO.getNoriginalsummny(),
									itemVO.getNexchangeotoarate(), strRateDate);

					itemVO.setNassistcurmny(nmoney);
					itemVO.setNassistsummny(nsummny);
					if ((nsummny != null) && (nmoney != null)) {
						itemVO.setNassisttaxmny(nsummny.sub(nmoney));
					}

					nmoney = POPubSetUI.getCurrArith_Finance(pk_corp)
							.getAmountByOpp(
									itemVO.getCcurrencytypeid(),
									POPubSetUI.getCurrArith_Busi(pk_corp)
											.getFracCurrPK(),
									itemVO.getNoriginalpaymentmny(),
									itemVO.getNexchangeotoarate(), strRateDate);

					itemVO.setNassistpaymny(nmoney);

					nmoney = POPubSetUI.getCurrArith_Busi(pk_corp)
							.getAmountByOpp(
									POPubSetUI.getCurrArith_Busi(pk_corp)
											.getFracCurrPK(),
									POPubSetUI.getCurrArith_Busi(pk_corp)
											.getLocalCurrPK(),
									itemVO.getNassistcurmny(),
									itemVO.getNexchangeotobrate(), strRateDate);

					nsummny = POPubSetUI.getCurrArith_Busi(pk_corp)
							.getAmountByOpp(
									POPubSetUI.getCurrArith_Busi(pk_corp)
											.getFracCurrPK(),
									POPubSetUI.getCurrArith_Busi(pk_corp)
											.getLocalCurrPK(),
									itemVO.getNassistsummny(),
									itemVO.getNexchangeotobrate(), strRateDate);

					invVO.getBodyVO()[i].setNmoney(nmoney);
					invVO.getBodyVO()[i].setNsummny(nsummny);
					if ((nsummny != null) && (nmoney != null)) {
						invVO.getBodyVO()[i].setNtaxmny(nsummny.sub(nmoney));
					}

					nmoney = POPubSetUI.getCurrArith_Finance(pk_corp)
							.getAmountByOpp(
									POPubSetUI.getCurrArith_Busi(pk_corp)
											.getFracCurrPK(),
									POPubSetUI.getCurrArith_Busi(pk_corp)
											.getLocalCurrPK(),
									itemVO.getNassistpaymny(),
									itemVO.getNexchangeotobrate(), strRateDate);

					invVO.getBodyVO()[i].setNpaymentmny(nmoney);
				}
			}
		} catch (Exception e) {
			SCMEnv.out(e);
			MessageDialog
					.showErrorDlg(this, NCLangRes.getInstance().getStrByID(
							"40040401", "UPPSCMCommon-000059"), NCLangRes
							.getInstance().getStrByID("40040401",
									"UPP40040401-000012"));
			return false;
		}

		return true;
	}

	private void computeValueFrmOriginal(int row) throws Exception {
		Object bRate = getInvBillPanel().getBodyValueAt(row,
				"nexchangeotobrate");
		if ((bRate == null) || (bRate.toString().trim().length() == 0)) {
			bRate = getInvBillPanel().getHeadItem("nexchangeotobrate")
					.getValue();
			getInvBillPanel().setBodyValueAt(bRate, row, "nexchangeotobrate");
		}
		UFDouble ufBRate = new UFDouble(bRate.toString());

		UFDouble ufNoriginalcurmny = PuPubVO
				.getUFDouble_ZeroAsNull(getInvBillPanel().getBodyValueAt(row,
						"noriginalcurmny"));
		UFDouble ufMoney = null;

		UFDouble ufNoriginaltaxmny = PuPubVO
				.getUFDouble_ZeroAsNull(getInvBillPanel().getBodyValueAt(row,
						"noriginaltaxmny"));
		UFDouble ufTaxMny = null;

		UFDouble ufNoriginalsummny = PuPubVO
				.getUFDouble_ZeroAsNull(getInvBillPanel().getBodyValueAt(row,
						"noriginalsummny"));
		UFDouble ufSumMny = null;

		if ((getInvBillPanel().getBodyValueAt(row, "ccurrencytypeid") == null)
				|| (getInvBillPanel().getBodyValueAt(row, "ccurrencytypeid")
						.toString().trim().equals(""))) {
			String strCurrTypeId = ((UIRefPane) getInvBillPanel().getHeadItem(
					"ccurrencytypeid").getComponent()).getRefPK();
			getInvBillPanel().setBodyValueAt(strCurrTypeId, row,
					"ccurrencytypeid");
		}
		try {
			if ((ufNoriginalcurmny == null)
					|| (getInvBillPanel()
							.getBodyValueAt(row, "ccurrencytypeid") == null))
				ufMoney = null;
			else {
				ufMoney = POPubSetUI.getCurrArith_Busi(getPk_corp())
						.getAmountByOpp(
								getInvBillPanel().getBodyValueAt(row,
										"ccurrencytypeid").toString(),
								POPubSetUI.getCurrArith_Busi(getPk_corp())
										.getLocalCurrPK(), ufNoriginalcurmny,
								ufBRate,
								PoPublicUIClass.getLoginDate().toString());
			}

			getInvBillPanel().setBodyValueAt(ufMoney, row, "nmoney");

			if ((ufNoriginaltaxmny == null)
					|| (getInvBillPanel()
							.getBodyValueAt(row, "ccurrencytypeid") == null))
				ufTaxMny = null;
			else {
				ufTaxMny = POPubSetUI.getCurrArith_Busi(getPk_corp())
						.getAmountByOpp(
								getInvBillPanel().getBodyValueAt(row,
										"ccurrencytypeid").toString(),
								POPubSetUI.getCurrArith_Busi(getPk_corp())
										.getLocalCurrPK(), ufNoriginaltaxmny,
								ufBRate,
								PoPublicUIClass.getLoginDate().toString());
			}

			getInvBillPanel().setBodyValueAt(ufTaxMny, row, "ntaxmny");

			if ((ufNoriginalsummny == null)
					|| (getInvBillPanel()
							.getBodyValueAt(row, "ccurrencytypeid") == null))
				ufSumMny = null;
			else {
				ufSumMny = POPubSetUI.getCurrArith_Busi(getPk_corp())
						.getAmountByOpp(
								getInvBillPanel().getBodyValueAt(row,
										"ccurrencytypeid").toString(),
								POPubSetUI.getCurrArith_Busi(getPk_corp())
										.getLocalCurrPK(), ufNoriginalsummny,
								ufBRate,
								PoPublicUIClass.getLoginDate().toString());
			}

			getInvBillPanel().setBodyValueAt(ufSumMny, row, "nsummny");
		} catch (Exception ex) {
			reportException(ex);
			throw ex;
		}
	}

	private void computeValueFrmOtherBill(InvoiceVO vo) throws Exception {
		InvoiceItemVO[] items = vo.getBodyVO();
		try {
			for (int i = 0; i < items.length; ++i) {
				InvoiceItemVO itemVO = items[i];

				UFDouble ufNoriginalcurmny = PuPubVO
						.getUFDouble_ZeroAsNull(itemVO.getNoriginalcurmny());
				UFDouble ufMoney = null;

				UFDouble ufNoriginaltaxmny = PuPubVO
						.getUFDouble_ZeroAsNull(itemVO.getNoriginaltaxmny());
				UFDouble ufTaxMny = null;

				UFDouble ufNoriginalsummny = PuPubVO
						.getUFDouble_ZeroAsNull(itemVO.getNoriginalsummny());
				UFDouble ufSumMny = null;
				String strCurrTypeId = itemVO.getCcurrencytypeid();

				UFDate dOrderDate = vo.getHeadVO().getDinvoicedate();

				UFDouble ufBRate = null;
				UFDouble ufARate = null;
				if (dOrderDate != null) {
					UFDouble[] daRate = null;
					String strCurrDate = dOrderDate.toString();
					if ((strCurrDate == null)
							|| (strCurrDate.trim().length() == 0)) {
						strCurrDate = PoPublicUIClass.getLoginDate() + "";
					}
					daRate = this.m_listPoPubSetUI2.getBothExchRateValue(
							getPk_corp(), strCurrTypeId,
							new UFDate(strCurrDate));

					ufBRate = daRate[0];
					itemVO.setNexchangeotobrate(ufBRate);

					ufARate = daRate[1];
					itemVO.setNexchangeotoarate(ufARate);
				}

				if (ufNoriginalcurmny == null)
					ufMoney = null;
				else {
					ufMoney = POPubSetUI.getCurrArith_Busi(getPk_corp())
							.getAmountByOpp(
									items[i].getCcurrencytypeid(),
									POPubSetUI.getCurrArith_Busi(getPk_corp())
											.getLocalCurrPK(),
									ufNoriginalcurmny, ufBRate,
									PoPublicUIClass.getLoginDate().toString());
				}

				itemVO.setNmoney(ufMoney);

				if (ufNoriginaltaxmny == null)
					ufTaxMny = null;
				else {
					ufTaxMny = POPubSetUI.getCurrArith_Busi(getPk_corp())
							.getAmountByOpp(
									items[i].getCcurrencytypeid(),
									POPubSetUI.getCurrArith_Busi(getPk_corp())
											.getLocalCurrPK(),
									ufNoriginaltaxmny, ufBRate,
									PoPublicUIClass.getLoginDate().toString());
				}

				itemVO.setNtaxmny(ufTaxMny);

				if (ufNoriginalsummny == null)
					ufSumMny = null;
				else {
					ufSumMny = POPubSetUI.getCurrArith_Busi(getPk_corp())
							.getAmountByOpp(
									items[i].getCcurrencytypeid(),
									POPubSetUI.getCurrArith_Busi(getPk_corp())
											.getLocalCurrPK(),
									ufNoriginalsummny, ufBRate,
									PoPublicUIClass.getLoginDate().toString());
				}

				itemVO.setNsummny(ufSumMny);
			}
		} catch (Exception ex) {
			reportException(ex);
			throw ex;
		}
	}

	private void dealWhenCopy() {
		
		getInvBillPanel().getHeadItem("vinvoicecode").setValue(null);
		getInvBillPanel().getHeadItem("vinvoicecode").setEdit(true);
		getInvBillPanel().getHeadItem("cinvoiceid").setValue(null);
		getInvBillPanel().getHeadItem("ibillstatus").setValue(new Integer(0));
		getInvBillPanel().getHeadItem("ts").setValue(null);
		getInvBillPanel().getHeadItem("dinvoicedate").setValue(
				ClientEnvironment.getInstance().getDate());
		getInvBillPanel().getHeadItem("darrivedate").setValue(
				ClientEnvironment.getInstance().getDate());

		((UIRefPane) getInvBillPanel().getTailItem("coperator").getComponent())
				.setPK(ClientEnvironment.getInstance().getUser()
						.getPrimaryKey());
		((UIRefPane) getInvBillPanel().getTailItem("dauditdate").getComponent())
				.setValue(null);
		getInvBillPanel().getTailItem("cauditpsn").setValue(null);
		getInvBillPanel().getTailItem("tmaketime").setValue(null);
		getInvBillPanel().getTailItem("taudittime").setValue(null);
		getInvBillPanel().getTailItem("tlastmaketime").setValue(null);

		BillModel bm = getInvBillPanel().getBillModel();
		for (int i = 0; i < bm.getRowCount(); ++i) {
			bm.setRowState(i, 1);

			bm.setValueAt(null, i, "naccumorignsettmny");
			bm.setValueAt(null, i, "naccumsettmny");
			bm.setValueAt(null, i, "naccumsettnum");
			bm.setValueAt(null, i, "nassistsettmny");

			bm.setValueAt(null, i, "nassistpaymny");
			bm.setValueAt(null, i, "npaymentmny");
			bm.setValueAt(null, i, "noriginalpaymentmny");
			bm.setValueAt(null, i, "cinvoice_bid");
			bm.setValueAt(null, i, "cinvoiceid");

			bm.setValueAt(null, i, "ts");
		}
		getInvBillPanel().getBodyItem("invcode").setEnabled(true);
	}

	private void execBillQuery(InvQueDlg curDlg) {
		showHintMessage(getHeadHintText()
				+ NCLangRes.getInstance().getStrByID("40040401",
						"UPP40040401-000013") + "|" + "<#" + "......." + "#>");
		
		NormalCondVO[] normalVOs = curDlg.getNormalCondVOs();
		ConditionVO[] definedVOs = curDlg.getConditionVO();

		PiPqPublicUIClass.processLIKEInCondVOs(definedVOs);

		InvoiceVO[] VOs = null;
		try {
			VOs = InvoiceHelper.queryInvoiceVOsByCondsMy(normalVOs, definedVOs);
		} catch (Exception e) {
			SCMEnv.out(e);
			MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID(
					"40040401", "UPPSCMCommon-000270"), NCLangRes.getInstance()
					.getStrByID("40040401", "UPP40040401-000014"));
			return;
		}

		if (VOs == null) {
			setInvVOs(null);
			MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID(
					"40040401", "UPPSCMCommon-000270"), NCLangRes.getInstance()
					.getStrByID("40040401", "UPP40040401-000015"));
			getInvBillPanel().addNew();
			return;
		}

		for (int i = 0; i < VOs.length; ++i) {
			VOs[i].setSource(4);
		}
		setInvVOs(VOs);
		setCurVOPos(0);

		setBillBrowseState(2);
		setListOperState(4);

		setVOToBillPanel();
	}

	public void execHeadTailFormula(InvoiceVO vo) {
		if (vo == null) {
			return;
		}

		InvoiceHeaderVO voHead = vo.getHeadVO();
		UIRefPane refpane = null;

		String[] saFormula = { "getColValue(bd_calbody,bodyname,pk_calbody,cstoreorganization)" };
		PuTool.getFormulaParse().setExpressArray(saFormula);

		int iFormulaLen = saFormula.length;
		for (int i = 0; i < iFormulaLen; ++i) {
			PuTool.getFormulaParse().addVariable(
					"cstoreorganization",
					StringUtil.toString(new String[] { voHead
							.getCstoreorganization() }));
		}

		String[][] saRet = PuTool.getFormulaParse().getValueSArray();

		refpane = (UIRefPane) getInvBillPanel().getHeadItem(
				"cstoreorganization").getComponent();

		if ((PuPubVO.getString_TrimZeroLenAsNull(refpane.getUITextField()
				.getText()) != null)
				|| (saRet == null) || (saRet[0] == null))
			return;
		refpane.getUITextField().setText(saRet[0][0]);
	}

	private void execListQuery(InvQueDlg curDlg) {
		showHintMessage(getHeadHintText()
				+ NCLangRes.getInstance().getStrByID("40040401",
						"UPP40040401-000013") + ".......");

		NormalCondVO[] normalVOs = curDlg.getNormalCondVOs();
		ConditionVO[] definedVOs = curDlg.getConditionVO();

		PiPqPublicUIClass.processLIKEInCondVOs(definedVOs);

		InvoiceVO[] VOs = null;
		try {
			VOs = InvoiceHelper.queryInvoiceVOsByCondsMy(normalVOs, definedVOs);
		} catch (Exception e) {
			SCMEnv.out(e);
			MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID(
					"40040401", "UPPSCMCommon-000270"), NCLangRes.getInstance()
					.getStrByID("40040401", "UPP40040401-000014"));
			return;
		}

		if (VOs == null) {
			setInvVOs(null);
			MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID(
					"40040401", "UPPSCMCommon-000270"), NCLangRes.getInstance()
					.getStrByID("40040401", "UPP40040401-000015"));
			setVOsToListPanel();
			return;
		}

		for (int i = 0; i < VOs.length; ++i) {
			VOs[i].setSource(4);
		}
		setInvVOs(VOs);

		setCurVOPos(0);
		setVOsToListPanel();

		setBillBrowseState(2);
		setListOperState(4);
		setCurOperState(getListOperState());
	}

	private void filterNullLine() {
		Object oTempValue = null;

		BillModel bmBill = getInvBillPanel().getBillModel();

		int iInvCol = bmBill.getBodyColByKey("invcode");

		if ((bmBill != null) && (iInvCol >= 0)
				&& (iInvCol < bmBill.getColumnCount())) {
			int iRowCount = getInvBillPanel().getRowCount();

			for (int line = iRowCount - 1; line >= 0; --line) {
				oTempValue = bmBill.getValueAt(line, iInvCol);
				if ((oTempValue != null)
						&& (oTempValue.toString().trim().length() != 0))
					continue;
				getInvBillPanel().getBillModel().delLine(new int[] { line });
			}
		}
		if ((bmBill.getRowCount() <= 0) && (isCouldMaked()))
			onAppendLine();
	}

	private int getBillBrowseState() {
		return this.m_nBillBrowseState;
	}

	private String getCauditid() {
		return this.m_cauditid;
	}

	private String getCurBizeType() {
		return this.m_strCurBizeType;
	}

	private String getCurOperator() {
		return ClientEnvironment.getInstance().getUser().getPrimaryKey();
	}

	private int getCurOperState() {
		return this.m_nCurOperState;
	}

	/** @deprecated */
	private Component getCurPanel() {
		if (getCurPanelMode() == 0)
			return getInvBillPanel();
		if (getCurPanelMode() == 1)
			return getInvListPanel();
		return null;
	}

	private int getCurPanelMode() {
		return this.m_nCurPanelMode;
	}

	private int getCurVOPos() {
		return this.m_nCurInvVOPos;
	}

	private String getHeadHintText() {
		return this.m_strHeadHintText;
	}

	private InvBillPanel getInvBillPanel() {
		if (this.m_InvBillPanel == null) {
			try {
				this.m_InvBillPanel = new InvBillPanel(ClientEnvironment
						.getInstance());
			} catch (Exception e) {
				SCMEnv.out(e);
				MessageDialog.showHintDlg(this, NCLangRes.getInstance()
						.getStrByID("40040401", "UPPSCMCommon-000270"), e
						.getMessage());
				return null;
			}

			new DefaultCurrTypeBizDecimalListener(this.m_InvBillPanel
					.getBillModel(), "ccurrencytypeid", this.m_itemMny_bu);
			new DefaultCurrTypeDecimalAdapter(this.m_InvBillPanel
					.getBillModel(), "ccurrencytypeid", this.m_itemMny_fi);
		}

		return this.m_InvBillPanel;
	}

	private InvoiceHeaderVO[] getInvHVOs() {
		if (getInvVOs() == null) {
			return null;
		}
		Vector vec = new Vector();
		for (int i = 0; i < getInvVOs().length; ++i) {
			vec.addElement(getInvVOs()[i].getHeadVO());
		}
		InvoiceHeaderVO[] hVO = new InvoiceHeaderVO[getInvVOs().length];
		vec.copyInto(hVO);

		return hVO;
	}

	/** @deprecated */
	private InvListPanel getInvListPanel() {
		if (this.m_listPoPubSetUI2 == null) {
			this.m_listPoPubSetUI2 = new POPubSetUI2();
		}

		if (this.m_InvListPanel == null) {
			try {
				this.m_InvListPanel = new InvListPanel(ClientEnvironment
						.getInstance());

				this.m_InvListPanel.getHeadBillModel()
						.addSortRelaObjectListener2(this);
			} catch (Throwable ivjExc) {
				SCMEnv.out(ivjExc.toString());
				MessageDialog.showHintDlg(this, NCLangRes.getInstance()
						.getStrByID("40040401", "UPP40040401-000016"),
						NCLangRes.getInstance().getStrByID("40040401",
								"UPP40040401-000017"));
				return null;
			}
			this.m_InvListPanel.getHeadItem("cauditpsn").setShow(false);
			initListListener();

			new DefaultCurrTypeBizDecimalListener(this.m_InvListPanel
					.getBodyBillModel(), "ccurrencytypeid", this.m_itemMny_bu);
			new DefaultCurrTypeDecimalAdapter(this.m_InvListPanel
					.getBodyBillModel(), "ccurrencytypeid", this.m_itemMny_fi);
		}

		return this.m_InvListPanel;
	}

	private InvoiceVO[] getInvVOs() {
		return this.m_InvVOs;
	}

	private int getListOperState() {
		return this.m_nListOperState;
	}

	public String getModuleCode() {
		return "40040401";
	}

	private int getOldCurrMoneyDigit() {
		return this.m_oldCurrMoneyDigit;
	}

	private String getPk_corp() {
		return ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
	}

	private InvoiceUIQueDlg getQueDlg() {
		if (this.m_InvQueDlg == null) {
			this.m_InvQueDlg = new InvoiceUIQueDlg(this, getModuleCode(), null,
					getInvBillPanel().getBillType(), getCurOperator(),
					getPk_corp());
			this.m_InvQueDlg.setValueRef("po_invoice.dinvoicedate", "日历");
			this.m_InvQueDlg.setDefaultValue("po_invoice.dinvoicedate",
					"po_invoice.dinvoicedate", ClientEnvironment.getInstance()
							.getDate().toString());

			DefSetTool.updateQueryConditionClientUserDef(this.m_InvQueDlg,
					getPk_corp(), "25", "po_invoice.vdef", "po_invoice_b.vdef");
		}

		return this.m_InvQueDlg;
	}

	private InvoiceVO getSavedInvVOFromBill() {
		int bodyNum = getInvBillPanel().getRowCount();
		if (bodyNum < 1) {
			MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID(
					"40040401", "UPPSCMCommon-000270"), NCLangRes.getInstance()
					.getStrByID("40040401", "UPP40040401-000018"));
			return null;
		}
		InvoiceVO billVO = new InvoiceVO(bodyNum);
		getInvBillPanel().getBillValueVO(billVO);

		InvoiceHeaderVO head = billVO.getHeadVO();

		head.setCbiztype(getCurBizeType());

		if ((getInvVOs() != null) && (getInvVOs().length > 0)) {
			InvoiceVO vo = getInvVOs()[getCurVOPos()];
			billVO.setSource(vo.getSource());
		}

		UIComboBox comItem = (UIComboBox) getInvBillPanel().getHeadItem(
				"iinvoicetype").getComponent();
		head.setIinvoicetype(new Integer(comItem.getSelectedIndex()));

		UICheckBox initCheck = (UICheckBox) getInvBillPanel().getHeadItem(
				"finitflag").getComponent();
		if (initCheck.isSelected())
			head.setFinitflag(new Integer(1));
		else {
			head.setFinitflag(new Integer(0));
		}

		if ((((head.getCvendorbaseid() == null) || (head.getCvendorbaseid()
				.trim().equals(""))))
				&& (head.getCvendormangid() != null)
				&& (head.getCvendormangid().trim() != "")) {
			String cvendorbaseid = (String) PiPqPublicUIClass
					.getAResultFromTable("bd_cumandoc", "pk_cubasdoc",
							"pk_cumandoc", head.getCvendormangid());
			head.setCvendorbaseid(cvendorbaseid);
		}

		if ((((head.getCvendormangid() == null) || (head.getCvendormangid()
				.trim().equals(""))))
				&& (head.getCvendorbaseid() != null)
				&& (head.getCvendorbaseid().trim() != "")) {
			String cvendormangeid = (String) PiPqPublicUIClass
					.getAResultFromTable("bd_cumandoc", "pk_cumandoc",
							"pk_cubasdoc", head.getCvendorbaseid());
			head.setCvendormangid(cvendormangeid);
		}

		head.setPk_corp(ClientEnvironment.getInstance().getCorporation()
				.getPrimaryKey());
		head.setCaccountyear(ClientEnvironment.getInstance().getAccountYear());

		if ((head.getCinvoiceid() == null)
				|| (head.getCinvoiceid().trim().equals(""))) {
			head.setCoperator(ClientEnvironment.getInstance().getUser()
					.getPrimaryKey());
		}

		head.setIbillstatus(new Integer(0));
		head.setDr(new Integer(0));

		head.setCbilltype("25");
		int iLen = billVO.getBodyVO().length;

		UIRefPane nRefPanel = (UIRefPane) getInvBillPanel()
				.getHeadItem("vmemo").getComponent();

		UITextField vMemoField = nRefPanel.getUITextField();
		head.setVmemo(vMemoField.getText());

		for (int i = 0; i < iLen; ++i) {
			billVO.getBodyVO()[i].setPk_corp(head.getPk_corp());
			billVO.getBodyVO()[i].setDr(head.getDr());

			if ((billVO.getBodyVO()[i].getCbaseid() == null)
					|| (billVO.getBodyVO()[i].getCbaseid().trim().equals(""))) {
				String cbaseid = (String) PiPqPublicUIClass
						.getAResultFromTable("bd_invmandoc", "pk_invbasdoc",
								"pk_invmandoc", billVO.getBodyVO()[i]
										.getCmangid());
				billVO.getBodyVO()[i].setCbaseid(cbaseid);
			}

			UFDouble dTemp = new UFDouble(0.0D);
			if (billVO.getBodyVO()[i].getNtaxrate() == null)
				billVO.getBodyVO()[i].setNtaxrate(dTemp);
			if (billVO.getBodyVO()[i].getNoriginalcurmny() == null)
				billVO.getBodyVO()[i].setNoriginalcurmny(dTemp);
			if (billVO.getBodyVO()[i].getNoriginalcurprice() == null)
				billVO.getBodyVO()[i].setNoriginalcurprice(dTemp);
			if (billVO.getBodyVO()[i].getNoriginalpaymentmny() == null)
				billVO.getBodyVO()[i].setNoriginalpaymentmny(dTemp);
			if (billVO.getBodyVO()[i].getNoriginalsummny() == null)
				billVO.getBodyVO()[i].setNoriginalsummny(dTemp);
			if (billVO.getBodyVO()[i].getNoriginaltaxmny() == null)
				billVO.getBodyVO()[i].setNoriginaltaxmny(dTemp);
			setExchangeRateBody(i, true, null);

			UFDouble nexchangeotobrate = billVO.getBodyVO()[i]
					.getNexchangeotobrate();

			UFDouble nexchangeotoarate = billVO.getBodyVO()[i]
					.getNexchangeotoarate();

			String pk_corp = billVO.getHeadVO().getPk_corp();
			String[] value = { new Integer(i + 1).toString() };
			if ((nexchangeotobrate == null)
					|| ((nexchangeotobrate != null) && (((nexchangeotobrate
							.doubleValue() < 0.0D) || (nexchangeotobrate
							.doubleValue() == 0.0D))))) {
				MessageDialog.showWarningDlg(this, NCLangRes.getInstance()
						.getStrByID("SCMCOMMON", "UPPSCMCommon-000270"),
						NCLangRes.getInstance().getStrByID("40040401",
								"UPP40040401-000236", null, value));
				return null;
			}

			try {
				if ((POPubSetUI.getCurrArith_Busi(pk_corp).isBlnLocalFrac())
						&& (!POPubSetUI.getCurrArith_Busi(pk_corp)
								.isLocalCurrType(
										billVO.getBodyVO()[i]
												.getCcurrencytypeid()))
						&& (!POPubSetUI.getCurrArith_Busi(pk_corp)
								.getLocalCurrPK().equals(
										billVO.getBodyVO()[i]
												.getCcurrencytypeid()))
						&& (((nexchangeotoarate == null) || ((nexchangeotoarate != null) && (((nexchangeotoarate
								.doubleValue() < 0.0D) || (nexchangeotoarate
								.doubleValue() == 0.0D))))))) {
					MessageDialog.showWarningDlg(this, NCLangRes.getInstance()
							.getStrByID("SCMCOMMON", "UPPSCMCommon-000270"),
							NCLangRes.getInstance().getStrByID("40040401",
									"UPP40040401-000237", null, value));
					return null;
				}
			} catch (Exception ee) {
				SCMEnv.out(ee);
				return null;
			}

		}

		try {
			PuTool.validateNotNullField(getInvBillPanel());
		} catch (Exception e) {
			MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID(
					"40040401", "UPPSCMCommon-000270"), e.getMessage());
			return null;
		}

		if (!calNativeAndAssistCurrValue(billVO)) {
			return null;
		}

		try {
			billVO.validate();
		} catch (ValidationException e) {
			MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID(
					"40040401", "UPPSCMCommon-000270"), e.getMessage());
			return null;
		} catch (HintMessageException e) {
			int ret = MessageDialog.showYesNoDlg(this, NCLangRes.getInstance()
					.getStrByID("40040401", "UPPSCMCommon-000270"), e
					.getMessage()
					+ NCLangRes.getInstance().getStrByID("40040401",
							"UPP40040401-000159"));
			if (ret == 8) {
				return null;
			}
		}

		InvoiceItemVO[] changedBodyVOs = (InvoiceItemVO[]) (InvoiceItemVO[]) getInvBillPanel()
				.getBillModel().getBodyValueChangeVOs(
						InvoiceItemVO.class.getName());
		if ((changedBodyVOs != null) && (changedBodyVOs.length != 0)) {
			Vector vec = new Vector();
			for (int i = 0; i < changedBodyVOs.length; ++i) {
				if ((changedBodyVOs[i].getStatus() == 3)
						&& (changedBodyVOs[i].getCinvoice_bid() != null)
						&& (changedBodyVOs[i].getCinvoice_bid().trim().length() > 0)) {
					vec.addElement(changedBodyVOs[i]);
				}
			}
			if (vec.size() != 0) {
				for (int i = 0; i < billVO.getBodyVO().length; ++i) {
					vec.addElement(billVO.getBodyVO()[i]);
				}
				InvoiceItemVO[] itemVOs = new InvoiceItemVO[vec.size()];
				vec.copyInto(itemVOs);
				billVO.setChildrenVO(itemVOs);
			}
		}

		return billVO;
	}

	private InvoiceVO getSavedVO() {
		InvoiceVO retVO = getSavedInvVOFromBill();
		if (retVO == null) {
			return null;
		}
		String sKey = retVO.getHeadVO().getCinvoiceid();
		String sUpsourceBillType = retVO.getBodyVO()[0].getCupsourcebilltype();
		if ((sUpsourceBillType != null)
				&& (sUpsourceBillType.trim().length() > 0)
				&& (sUpsourceBillType.equals("50"))) {
			retVO.setSource(1);
		}

		if ((sKey != null) && (sKey.trim().length() > 0)) {
			retVO.setSource(4);
		} else if ((sUpsourceBillType == null)
				|| (sUpsourceBillType.trim().length() == 0))
			retVO.setSource(0);
		else if ((sUpsourceBillType != null)
				&& (sUpsourceBillType.trim().length() > 0)) {
			if ((sUpsourceBillType.equals("21"))
					|| (sUpsourceBillType.equals("61")))
				retVO.setSource(1);
			else if ((sUpsourceBillType.equals("45"))
					|| (sUpsourceBillType.equals("47"))
					|| (sUpsourceBillType.equals("4T")))
				retVO.setSource(2);
			else
				retVO.setSource(3);
		}
		if (this.m_bCopy) {
			retVO.setSource(0);
		}

		if (retVO.getSource() == 0) {
			if ((getInvVOs() == null) || (getInvVOs().length == 0)) {
				addNewOneIntoInvVOs(retVO);
			} else {
				String sUpBillType = getInvVOs()[getCurVOPos()].getBodyVO()[0]
						.getCupsourcebilltype();
				if ((sUpBillType != null) && (sUpBillType.trim().length() > 0)) {
					addNewOneIntoInvVOs(retVO);
				}
			}
		}
		return retVO;
	}

	private ArrayList getSelectedBills() {
		ArrayList aryRet = new ArrayList();
		InvoiceVO[] allvos = null;

		int iPos = 0;
		for (int i = 0; i < getInvListPanel().getHeadBillModel().getRowCount(); ++i) {
			if (getInvListPanel().getHeadBillModel().getRowState(i) == 4) {
				iPos = i;
				iPos = PuTool.getIndexBeforeSort(getInvListPanel(), iPos);
				aryRet.add(getInvVOs()[iPos]);
			}
		}
		allvos = (InvoiceVO[]) (InvoiceVO[]) aryRet
				.toArray(new InvoiceVO[aryRet.size()]);

		InvoiceVO curVO = null;
		for (int j = 0; j < allvos.length; ++j) {
			curVO = allvos[j];
			if (loadItemsForInvoiceVOs(new InvoiceVO[] { curVO })) {
				continue;
			}

		}

		return aryRet;
	}

	private int getSelectedRowCount() {
		return this.m_nSelectedRowCount;
	}

	public String getTitle() {
		String title = NCLangRes.getInstance().getStrByID("40040401",
				"UPP40040401-000160");

		if ((this.m_InvBillPanel != null)
				&& (!ClientEnvironment.getInstance().getCorporation()
						.getPk_corp().equals("@@@@"))) {
			title = this.m_InvBillPanel.getTitle();
		}
		return title;
	}

	private UFDate getToday() {
		return ClientEnvironment.getInstance().getDate();
	}

	public AggregatedValueObject getVo() throws Exception {
		showHintMessage(getHeadHintText()
				+ NCLangRes.getInstance().getStrByID("40040401",
						"UPP40040401-000013") + ".......");

		InvoiceVO[] VOs = null;
		try {
			VOs = InvoiceHelper.queryVoByHid(getCauditid());

			if ((VOs != null) && (VOs.length > 0)) {
				loadItemsForInvoiceVOs(new InvoiceVO[] { VOs[0] });
			}
		} catch (Exception e) {
			SCMEnv.out(e);
			throw e;
		}

		if (VOs == null) {
			return null;
		}

		return VOs[0];
	}

	public AggregatedValueObject getLinkQueryVo(String pk_corp)
			throws Exception {
		showHintMessage(getHeadHintText()
				+ NCLangRes.getInstance().getStrByID("40040401",
						"UPP40040401-000013") + ".......");

		NormalCondVO[] normalVOs = new NormalCondVO[2];

		normalVOs[0] = new NormalCondVO("单据ID", getCauditid());
		normalVOs[1] = new NormalCondVO("公司", pk_corp);

		InvoiceVO[] VOs = null;
		try {
			VOs = InvoiceHelper.queryInvoiceVOsByCondsMy(normalVOs, null);

			if ((VOs != null) && (VOs.length > 0)) {
				loadItemsForInvoiceVOs(new InvoiceVO[] { VOs[0] });
			}
		} catch (Exception e) {
			SCMEnv.out(e);
			throw e;
		}

		if (VOs == null) {
			return null;
		}

		return VOs[0];
	}

	public void handleException(Throwable exception) {
		SCMEnv.out("--------- 未捕捉到的异常 ---------");
		SCMEnv.out(exception);
	}

	private void initBillListener() {
		getInvBillPanel().addEditListener(this);
		getInvBillPanel().addBodyEditListener2(this);

		((UIRefPane) getInvBillPanel().getBodyItem("cprojectphasename")
				.getComponent()).getUIButton().addActionListener(this);

		((UIRefPane) getInvBillPanel().getBodyItem("vfree0").getComponent())
				.getUITextField().addActionListener(this);

		((UIRefPane) getInvBillPanel().getHeadItem("cemployeeid")
				.getComponent()).getUIButton().addActionListener(this);

		getInvBillPanel().addBodyMenuListener(this);

		((UIRefPane) getInvBillPanel().getBodyItem("cwarehousename")
				.getComponent()).getUIButton().addActionListener(this);
	}

	private void initButtons() {
		PfUtilClient.retBusinessBtn(this.m_btnInvBillBusiType,
				ClientEnvironment.getInstance().getCorporation().getPk_corp(),
				"25");

		if (this.m_btnInvBillBusiType.getChildButtonGroup().length == 0) {
			this.m_btnInvBillBusiType.setVisible(false);
		} else
			this.m_btnInvBillBusiType.setVisible(true);

		this.m_btnBillMaintain.setEnabled(true);
		this.m_btnBillBrowsed.setEnabled(true);
		this.m_btnInvBillConversion.setEnabled(false);

		this.m_btnInvBillNew.setEnabled(true);
		ButtonObject[] btns = this.m_btnInvBillBusiType.getChildButtonGroup();
		if ((btns != null) && (btns.length > 0) && (btns[0] != null)) {
			setCurBizeType(btns[0].getTag());
			this.m_bizButton = btns[0];
		}
		PuTool.initBusiAddBtns(this.m_btnInvBillBusiType, this.m_btnInvBillNew,
				"25", getPk_corp());

		setButtonsList();
		setButtons(this.m_btnTree.getButtonArray());
	}

	/** @deprecated */
	private void initCard() {
		setLayout(new BorderLayout());
		add(getInvBillPanel(), "Center");
		this.m_btnInvShift.setName(NCLangRes.getInstance().getStrByID(
				"SCMCOMMON", "UPPSCMCommon-000464"));
	}

	private String getCorpId() {
		String corpid = null;
		corpid = ClientEnvironment.getInstance().getCorporation()
				.getPrimaryKey();
		if ((corpid == null) || (corpid.trim().equals(""))) {
			corpid = getCorpPrimaryKey();
		}
		return corpid;
	}

	/** @deprecated */
	private void initi() {
		setCurPanelMode(0);

		createBtnInstances();
		initButtons();
		initRefpane(getInvBillPanel().getBillData());
		initCard();
		initState();
		initBillListener();

		getPoPubSetUi2();

		BillRowNo.loadRowNoItem(getInvBillPanel(), "crowno");
		try {
			Hashtable t = SysInitBO_Client.queryBatchParaValues(PoPublicUIClass
					.getLoginPk_corp(), new String[] { "PO060", "PO30", "PO46",
					"PO52" });

			if ((t != null) && (t.size() > 0)) {
				Object oTemp = t.get("PO060");
				this.isAllowedModifyByOther = ((oTemp != null) && (!oTemp
						.equals("N")));
				oTemp = t.get("PO30");
				this.m_sStock2InvoiceSettleMode = ((oTemp == null) ? null
						: oTemp.toString());
				oTemp = t.get("PO46");
				this.m_sOrder2InvoiceSettleMode = ((oTemp == null) ? null
						: oTemp.toString());
				oTemp = t.get("PO52");
				this.m_sZGYF = ((oTemp == null) ? null : oTemp.toString());
			}
		} catch (Exception e) {
			SCMEnv.out(e);
		}
		setMaxMnyDigit(this.iMaxMnyDigit);
	}

	/** @deprecated */
	private void initList() {
		setLayout(new BorderLayout());
		add(getInvListPanel(), "Center");
		setMaxMnyDigitList(this.iMaxMnyDigit);
	}

	private void initRefpane(BillData bd) {
		UIRefPane refpane = null;
		String pk_corp = getClientEnvironment().getCorporation()
				.getPrimaryKey();

		refpane = (UIRefPane) bd.getHeadItem("cdeptid").getComponent();

		refpane.getRefModel().setPk_corp(pk_corp);

		UIRefPane refBiz = (UIRefPane) bd.getHeadItem("cemployeeid")
				.getComponent();

		refBiz.getRefModel().setPk_corp(pk_corp);
	}

	/** @deprecated */
	private void initListListener() {
		this.m_InvListPanel.getHeadTable().setCellSelectionEnabled(false);
		this.m_InvListPanel.getHeadTable().setSelectionMode(2);
		this.m_InvListPanel.getHeadTable().getSelectionModel()
				.addListSelectionListener(this);

		this.m_InvListPanel.addEditListener(this);
		this.m_InvListPanel.addMouseListener(this);
	}

	private void initState() {
		setCurPanelMode(0);
		setCurOperState(0);
		setListOperState(4);
		setBillBrowseState(2);
		setButtonsAndPanelState();
	}

	private boolean isCouldMaked() {
		return this.m_bCouldMaked;
	}

	private boolean isCouldMakedForABizType(String strBizType) {
		return true;
	}

	private boolean isEverQueryed() {
		return this.m_bEverQueryed;
	}

	private void loadBDData() {
		String strFormula = null;
		String strVarValue = null;
		String strValue = null;
		UIRefPane refpnl = null;
		InvoiceVO vo = getInvVOs()[getCurVOPos()];
		if ((vo == null) || (vo.getParentVO() == null)) {
			return;
		}

		strVarValue = (String) vo.getParentVO().getAttributeValue("cbiztype");
		refpnl = (UIRefPane) getInvBillPanel().getHeadItem("cbiztype")
				.getComponent();
		strValue = refpnl.getUITextField().getText();
		if ((strVarValue != null)
				&& (((strValue == null) || (strValue.trim().equals(""))))) {
			strFormula = "cbiztypename->getColValue(bd_busitype,businame,pk_busitype,cbiztype)";
			Object ob = getInvBillPanel().execHeadFormula(strFormula);
			refpnl.getUITextField().setText((String) ob);
		}

		strVarValue = (String) vo.getParentVO().getAttributeValue(
				"cstoreorganization");
		refpnl = (UIRefPane) getInvBillPanel()
				.getHeadItem("cstoreorganization").getComponent();
		strValue = refpnl.getUITextField().getText();
		if ((strVarValue != null)
				&& (((strValue == null) || (strValue.trim().equals(""))))) {
			strFormula = "cstoreorganizationname->getColValue(bd_calbody,bodyname,pk_calbody,cstoreorganization)";
			Object ob = getInvBillPanel().execHeadFormula(strFormula);
			refpnl.getUITextField().setText((String) ob);
		}

		strVarValue = (String) vo.getParentVO().getAttributeValue(
				"cvendorbaseid");
		refpnl = (UIRefPane) getInvBillPanel().getHeadItem("cvendormangid")
				.getComponent();
		strValue = refpnl.getUITextField().getText();
		if ((strVarValue != null)
				&& (((strValue == null) || (strValue.trim().equals(""))))) {
			strFormula = "vendor->getColValue(bd_cubasdoc,custname,pk_cubasdoc,cvendorbaseid)";
			Object ob = getInvBillPanel().execHeadFormula(strFormula);
			refpnl.getUITextField().setText((String) ob);
		}

		strVarValue = (String) vo.getParentVO()
				.getAttributeValue("cfreecustid");
		refpnl = (UIRefPane) getInvBillPanel().getHeadItem("cfreecustid")
				.getComponent();
		strValue = refpnl.getUITextField().getText();
		if ((strVarValue != null)
				&& (((strValue == null) || (strValue.trim().equals(""))))) {
			strFormula = "cfreecustname->getColValue(so_freecust,vcustname,cfreecustid,cfreecustid)";
			Object ob = getInvBillPanel().execHeadFormula(strFormula);
			refpnl.getUITextField().setText((String) ob);
		}

		strVarValue = (String) vo.getParentVO().getAttributeValue(
				"ctermprotocolid");
		refpnl = (UIRefPane) getInvBillPanel().getHeadItem("ctermprotocolid")
				.getComponent();
		strValue = refpnl.getUITextField().getText();
		if ((strVarValue != null)
				&& (((strValue == null) || (strValue.trim().equals(""))))) {
			strFormula = "ctermprotocolname->getColValue(bd_payterm,termname,pk_payterm,ctermprotocolid)";
			Object ob = getInvBillPanel().execHeadFormula(strFormula);
			refpnl.getUITextField().setText((String) ob);
		}

		strVarValue = (String) vo.getParentVO().getAttributeValue(
				"ccurrencytypeid");
		refpnl = (UIRefPane) getInvBillPanel().getHeadItem("ccurrencytypeid")
				.getComponent();
		strValue = refpnl.getUITextField().getText();
		if ((strVarValue != null)
				&& (((strValue == null) || (strValue.trim().equals(""))))) {
			strFormula = "currname->getColValue(bd_currtype,currtypename,pk_currtype,ccurrencytypeid)";
			Object ob = getInvBillPanel().execHeadFormula(strFormula);
			refpnl.getUITextField().setText((String) ob);
		}
	}

	public boolean loadItemsForInvoiceVOs(InvoiceVO[] invs) {
		if ((invs == null) || (invs.length == 0)) {
			return true;
		}

		Vector v = new Vector();
		Hashtable hash = new Hashtable();
		for (int i = 0; i < invs.length; ++i) {
			if (invs == null)
				continue;
			InvoiceHeaderVO head = invs[i].getHeadVO();
			InvoiceItemVO[] items = (InvoiceItemVO[]) (InvoiceItemVO[]) invs[i]
					.getChildrenVO();
			if ((head != null) && (head.getPrimaryKey() != null)
					&& (((items == null) || (items.length == 0)))) {
				InvoiceHeaderVO lightHead = new InvoiceHeaderVO();

				lightHead.setPrimaryKey(head.getPrimaryKey());
				lightHead.setTs(head.getTs());

				InvoiceVO lightVO = new InvoiceVO();
				lightVO.setParentVO(lightHead);
				v.add(lightVO);
				hash.put(head.getPrimaryKey(), invs[i]);
			}

		}

		if (v.size() == 0) {
			return true;
		}

		InvoiceVO[] lightVOs = null;
		lightVOs = new InvoiceVO[v.size()];
		v.copyInto(lightVOs);
		try {
			NormalCondVO[] normalVOs = getQueDlg().getNormalCondVOs();
			ConditionVO[] definedVOs = getQueDlg().getConditionVO();
			lightVOs = InvoiceHelper.queryItemsForInvoices(normalVOs,
					definedVOs, lightVOs);
		} catch (BusinessException e) {
			SCMEnv.out(e);
			MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID(
					"40040401", "UPPSCMCommon-000270"), e.getMessage());
			return false;
		} catch (Exception e2) {
			SCMEnv.out(e2);
			return false;
		}
		if ((lightVOs != null) && (lightVOs.length > 0)) {
			for (int i = 0; i < lightVOs.length; ++i) {
				InvoiceVO vo = (InvoiceVO) hash.get(lightVOs[i].getHeadVO()
						.getPrimaryKey());
				vo.setChildrenVO(lightVOs[i].getChildrenVO());
			}
		}
		return true;
	}

	public void mouse_doubleclick(BillMouseEnent e) {
		onListBill();

		setButtonsAndPanelState();

		updateButtons();
	}

	public void onActionAppendLine() {
		getInvBillPanel().addLine();

		BillRowNo.addLineRowNo(getInvBillPanel(), "25", "crowno");
		int iNewLine = getInvBillPanel().getBillModel().getRowCount() - 1;

		setNewLineDefaultValue(iNewLine);
		setDefaultBody(iNewLine);
	}

	public void onActionInsertLines(int iBeginRow, int iEndRow, int iInsertCount) {
		if (getInvBillPanel().getBillTable().getSelectedRowCount() <= 0) {
			showHintMessage(NCLangRes.getInstance().getStrByID("40040401",
					"UPPSCMCommon-000354"));
			return;
		}
		int iCurRow = 0;
		for (int i = 0; i < iInsertCount; ++i) {
			iCurRow = iBeginRow + i;
			getInvBillPanel().getBillModel().insertRow(iCurRow + 1);

			setDefaultBody(iCurRow + 1);
			setNewLineDefaultValue(iCurRow + 1);
		}

		int iFinalEndRow = iBeginRow + iInsertCount + 1;

		BillRowNo.insertLineRowNos(getInvBillPanel(), "25", "crowno",
				iFinalEndRow, iInsertCount);
	}

	private void rightButtonRightControl() {
		if ((this.m_btnInvBillRowOperation == null)
				|| (this.m_btnInvBillRowOperation.getChildCount() == 0)) {
			getInvBillPanel().getBodyPanel().getMiAddLine().setEnabled(false);
			getInvBillPanel().getBodyPanel().getMiCopyLine().setEnabled(false);
			getInvBillPanel().getBodyPanel().getMiDelLine().setEnabled(false);
			getInvBillPanel().getBodyPanel().getMiInsertLine()
					.setEnabled(false);
			getInvBillPanel().getBodyPanel().getMiPasteLine().setEnabled(false);
			getInvBillPanel().getBodyPanel().getMiPasteLineToTail().setEnabled(
					false);
		} else {
			getInvBillPanel().getBodyPanel().getMiAddLine().setEnabled(
					this.m_btnInvBillAddRow.isPower());
			getInvBillPanel().getBodyPanel().getMiCopyLine().setEnabled(
					this.m_btnInvBillCopyRow.isPower());
			getInvBillPanel().getBodyPanel().getMiDelLine().setEnabled(
					this.m_btnInvBillDeleteRow.isPower());
			getInvBillPanel().getBodyPanel().getMiInsertLine().setEnabled(
					this.m_btnInvBillInsertRow.isPower());
			getInvBillPanel().getBodyPanel().getMiPasteLine().setEnabled(
					this.m_btnInvBillPasteRow.isPower());

			getInvBillPanel().getBodyPanel().getMiPasteLineToTail().setEnabled(
					this.m_btnInvBillPasteRow.isPower());
		}
	}

	private void onAdd() {
		this.m_bAdd = true;

		getInvBillPanel().addNew();
		getInvBillPanel().setEnabled(true);

		setCurOperState(1);
		setButtonsAndPanelState();

		updateButtons();

		rightButtonRightControl();

		UIRefPane busiRef = (UIRefPane) getInvBillPanel().getHeadItem(
				"cbiztype").getComponent();
		busiRef.setValue(this.m_bizButton.getName());

		getInvBillPanel().getHeadItem("ibillstatus").setValue(new Integer(0));

		getInvBillPanel().getHeadItem("dinvoicedate").setValue(getToday());
		getInvBillPanel().getHeadItem("darrivedate").setValue(getToday());
		getInvBillPanel().getHeadItem("iinvoicetype").setValue(new Integer(0));
		getInvBillPanel().getHeadItem("idiscounttaxtype").setValue(
				new Integer(1));

		((UIRefPane) getInvBillPanel().getTailItem("coperator").getComponent())
				.setPK(ClientEnvironment.getInstance().getUser()
						.getPrimaryKey());
		try {
			String sLocalCurrId = POPubSetUI.getCurrArith_Busi(getPk_corp())
					.getLocalCurrPK();
			getInvBillPanel().getHeadItem("ccurrencytypeid").setValue(
					sLocalCurrId);

			setCurrRateToBillHead(sLocalCurrId, null, null);
		} catch (Exception e) {
			SCMEnv.out(e);
		}

		setCouldMaked(true);

		String cBizType = getCurBizeType();
		CheckISSellProxy checkISSellProxy = new CheckISSellProxy();
		boolean checker = false;
		try {
			if (!this.cBizTypeTable.containsKey(cBizType)) {
				checkISSellProxy = new CheckISSellProxy();
				Object oTemp = CacheTool.getCellValue("bd_busitype",
						"pk_busitype", "verifyrule", cBizType);
				if (oTemp != null) {
					Object[] o = (Object[]) (Object[]) oTemp;
					if ((o != null) && (o.length > 0) && (o[0] != null)
							&& (o[0].equals("S")))
						checker = true;
				}
			} else {
				checker = new UFBoolean(this.cBizTypeTable.get(cBizType)
						.toString()).booleanValue();
			}

			if (checker) {
				String sql = " and (sellproxyflag = 'Y')";
				UIRefPane refCinventorycode = (UIRefPane) (UIRefPane) getInvBillPanel()
						.getBodyItem("invcode").getComponent();
				refCinventorycode.getRefModel().addWherePart(sql);
				if (!this.cBizTypeTable.containsKey(cBizType))
					this.cBizTypeTable.put(cBizType, String.valueOf(checker));
			} else {
				String sql = " and ( 1 =1 )";
				UIRefPane refCinventorycode = (UIRefPane) (UIRefPane) getInvBillPanel()
						.getBodyItem("invcode").getComponent();
				refCinventorycode.getRefModel().addWherePart(sql);
			}

			setDefaultValueByUser();
		} catch (Exception e) {
			SCMEnv.out(e);
		}

		SCMEnv.out("m_btnInvBillDeleteRow=="
				+ this.m_btnInvBillDeleteRow.isPower());
	}

	private void onAppendLine() {
		getInvBillPanel().addLine();
		int iNewRow = getInvBillPanel().getRowCount() - 1;
		setNewLineDefaultValueForAddLine(iNewRow);
		BillRowNo.addLineRowNo(getInvBillPanel(), "25", "crowno");
		SCMEnv.out("m_btnInvBillDeleteRow=="
				+ this.m_btnInvBillDeleteRow.isPower());

		rightButtonRightControl();
	}

	public void onUnAudit() {
		InvoiceVO[] proceVOs = { getInvVOs()[getCurVOPos()] };

		HashMap mapAuditInfo = new HashMap();
		ArrayList listAuditInfo = null;
		try {
			for (int i = 0; i < proceVOs.length; ++i) {
				if (PuPubVO.getString_TrimZeroLenAsNull(proceVOs[i].getHeadVO()
						.getCauditpsn()) != null) {
					listAuditInfo = new ArrayList();
					listAuditInfo.add(proceVOs[i].getHeadVO().getCauditpsn());
					listAuditInfo.add(proceVOs[i].getHeadVO().getDauditdate());
					mapAuditInfo.put(proceVOs[i].getHeadVO().getPrimaryKey(),
							listAuditInfo);
				}
				proceVOs[i].getHeadVO().setCauditpsn(
						ClientEnvironment.getInstance().getUser()
								.getPrimaryKey());
				proceVOs[i].getHeadVO().setCuserid(
						ClientEnvironment.getInstance().getUser()
								.getPrimaryKey());
			}
			if (!loadItemsForInvoiceVOs(proceVOs)) {
				showHintMessage("");
				return;
			}

			if ((this.m_sZGYF == null)
					|| (!new UFBoolean(this.m_sZGYF).booleanValue())) {
				InvoiceVO invoiceVO = new InvoiceVO();
				for (int i = 0; i < proceVOs.length; ++i) {
					invoiceVO = proceVOs[i];
					Object oTemp = CacheTool.getCellValue("bd_busitype",
							"pk_busitype", "verifyrule", invoiceVO.getHeadVO()
									.getCbiztype());
					if (oTemp != null) {
						Object[] oTemp1 = (Object[]) (Object[]) oTemp;
						if (oTemp1[0].equals("S"))
							continue;
						if (!oTemp1[0].equals("Z")) {
							if (invoiceVO.getHeadVO().getIinvoicetype()
									.intValue() != 3) {
								InvoiceItemVO[] tempBodyVO = invoiceVO
										.getBodyVO();
								for (int j = 0; j < tempBodyVO.length; ++j)
									if (tempBodyVO[j].getCupsourcebilltype() != null) {
										if (((!tempBodyVO[j]
												.getCupsourcebilltype().equals(
														"45")) && (!tempBodyVO[j]
												.getCupsourcebilltype().equals(
														"47")))
												|| (this.m_sOrder2InvoiceSettleMode == null)
												|| (!this.m_sStock2InvoiceSettleMode
														.equals("审批时自动结算"))
												|| (tempBodyVO[j]
														.getNaccumsettmny() == null)
												|| (tempBodyVO[j]
														.getNaccumsettmny()
														.doubleValue() == 0.0D))
											continue;
										MessageDialog
												.showHintDlg(
														this,
														NCLangRes
																.getInstance()
																.getStrByID(
																		"40040401",
																		"UPPSCMCommon-000270"),
														NCLangRes
																.getInstance()
																.getStrByID(
																		"40040401",
																		"UPP40040401-000022"));
										showHintMessage("");
										return;
									}
							}
						}
					}
				}
			}
			PfUtilClient.processBatch(
					"UNAPPROVE"
							+ ClientEnvironment.getInstance().getUser()
									.getPrimaryKey(), "25", ClientEnvironment
							.getInstance().getDate().toString(), proceVOs);

			if (PfUtilClient.isSuccess()) {
				InvoiceHeaderVO headVO = getInvVOs()[getCurVOPos()].getHeadVO();
				ArrayList arrRet = InvoiceHelper.queryForSaveAudit(headVO
						.getPrimaryKey());
				headVO.setDauditdate(null);
				headVO.setCauditpsn(null);
				headVO.setIbillstatus((Integer) arrRet.get(2));
				headVO.setTs((String) arrRet.get(3));

				setVOToBillPanel();

				this.m_btnAudit.setEnabled(true);
				this.m_btnUnAudit.setEnabled(false);
				updateButtons();
			} else if (mapAuditInfo.size() > 0) {
				for (int i = 0; i < proceVOs.length; ++i)
					if (!mapAuditInfo.containsKey(proceVOs[i].getHeadVO()
							.getPrimaryKey())) {
						proceVOs[i].getHeadVO().setCauditpsn(null);
						proceVOs[i].getHeadVO().setDauditdate(null);
					} else {
						listAuditInfo = (ArrayList) mapAuditInfo
								.get(proceVOs[i].getHeadVO().getPrimaryKey());
						proceVOs[i].getHeadVO().setCauditpsn(
								(String) listAuditInfo.get(0));
						proceVOs[i].getHeadVO().setDauditdate(
								(UFDate) listAuditInfo.get(1));
					}
			}
			
			// 查询是否有回冲应付单,如果有则删除，并回写暂估应付回冲标志
			 // 王凯飞
			 // 2014-11-07
			// edit--2015-02-06--wkf
			 
			String pk_corp = ClientEnvironment.getInstance().getCorporation().getPk_corp();
			InvoiceItemVO[] bvoss = (InvoiceItemVO[]) proceVOs[0].getChildrenVO();
			String checktype = bvoss[0].getCupsourcebilltype();
			if(checktype.equals("47")){
				if(("1078".equals(pk_corp))||("1108".equals(pk_corp))){//edit by  mcw
					String invoice = (String) proceVOs[0].getParentVO().getAttributeValue(
							"cinvoiceid");
					DJZBVO[] delete = queryDJZBfromZxy(invoice);
					if(delete.length >0){
						//删除负的暂估应付单
						IArapBillPublic beanfu = (IArapBillPublic) NCLocator.getInstance()
								.lookup(IArapBillPublic.class.getName());
						for (int i = 0; i < delete.length; i++) {
							beanfu.deleteArapBill(delete[i]);
						}
						DJZBVO[] backdj = queryDJfromdel(delete);
						IArapBillPublic uArap = (IArapBillPublic) NCLocator.getInstance().lookup(IArapBillPublic.class.getName());
						for (int i = 0; i < delete.length; i++) {
							DJZBVO updatetmp = new DJZBVO();
							DJZBHeaderVO djheadvo = (DJZBHeaderVO) backdj[i].getParentVO();
//						djheadvo.setZyx28(null);
							
							DJZBItemVO[] djbodyvo = (DJZBItemVO[]) backdj[i].getChildrenVO();
							for (int j = 0; j < djbodyvo.length; j++) {
								djbodyvo[j].setZyx28(null);
							}
							updatetmp.setParentVO(djheadvo);
							updatetmp.setChildrenVO(djbodyvo);
							uArap.editArapBill(updatetmp);
						}
//					//删除存货核算采购入库单回冲单据//2015-04-24制盖业务变更，不用生成存货核算，没有生成回冲，此方法删除--wkf--
//					BillVO[] delete1 = queryBillfromZG(invoice,pk_corp);
//					if(delete1.length>0){
//						//删除存货核算采购入库单回冲单据
//						IBill billim = (IBill) NCLocator.getInstance().lookup(IBill.class.getName());
//						ClientLink cl = new ClientLink(ClientEnvironment.getInstance());
//						billim.deleteArray(cl, delete1, cl.getUser());
//						
//						//还原存货核算采购入库单vdef19->'已暂估且未结算'
//						String sssql = getUpdateSql(delete1,pk_corp);
//						IPubDMO ipubdmo = (IPubDMO)NCLocator.getInstance().lookup(IPubDMO.class.getName());
//						ipubdmo.executeUpdate(sssql);
//					}
						//回写
						backICStatus_wwjg1(delete);
					}
				}
			}
			
			//end 		
			
		} catch (Exception e) {
			String strErrMsg = e.getMessage();
			if ((e instanceof TaskInvalidateException)
					|| (e instanceof EngineException)) {
				MessageDialog.showHintDlg(this, NCLangRes.getInstance()
						.getStrByID("40040401", "UPPSCMCommon-000270"),
						strErrMsg);
			} else if ((strErrMsg != null)
					&& (((strErrMsg.indexOf(NCLangRes.getInstance().getStrByID(
							"40040401", "UPP40040401-000163")) >= 0) || (strErrMsg
							.indexOf(NCLangRes.getInstance().getStrByID(
									"40040401", "UPP40040401-000164")) >= 0)))) {
				strErrMsg = NCLangRes.getInstance().getStrByID("40040401",
						"UPP40040401-000023");
				MessageDialog.showHintDlg(this, NCLangRes.getInstance()
						.getStrByID("40040401", "UPPSCMCommon-000270"),
						strErrMsg);
			} else if (e instanceof BusinessException) {
				MessageDialog.showHintDlg(this, NCLangRes.getInstance()
						.getStrByID("40040401", "UPPSCMCommon-000270"),
						strErrMsg);
			} else {
				strErrMsg = NCLangRes.getInstance().getStrByID("40040401",
						"UPP40040401-000024");
				SCMEnv.out("!!! 以下信息可发给系统管理员参考：\n");
				SCMEnv.out(e);
				MessageDialog.showHintDlg(this, NCLangRes.getInstance()
						.getStrByID("40040401", "UPPSCMCommon-000270"),
						strErrMsg);
			}
			showHintMessage("");
			return;
		} finally {
			if (mapAuditInfo.size() > 0) {
				for (int i = 0; i < proceVOs.length; ++i) {
					if (!mapAuditInfo.containsKey(proceVOs[i].getHeadVO()
							.getPrimaryKey())) {
						proceVOs[i].getHeadVO().setCauditpsn(null);
						proceVOs[i].getHeadVO().setDauditdate(null);
					} else {
						listAuditInfo = (ArrayList) mapAuditInfo
								.get(proceVOs[i].getHeadVO().getPrimaryKey());
						proceVOs[i].getHeadVO().setCauditpsn(
								(String) listAuditInfo.get(0));
						proceVOs[i].getHeadVO().setDauditdate(
								(UFDate) listAuditInfo.get(1));
					}
				}
			}
		}
		showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH011"));
	}

	public void onAudit() {
		InvoiceVO[] proceVOs = { getInvVOs()[getCurVOPos()] };

		String strOperPk = ClientEnvironment.getInstance().getUser()
				.getPrimaryKey();
		UFDate today = ClientEnvironment.getInstance().getDate();

		HashMap mapAuditInfo = new HashMap();
		ArrayList listAuditInfo = null;

		for (int i = 0; i < proceVOs.length; ++i) {
			if (PuPubVO.getString_TrimZeroLenAsNull(proceVOs[i].getHeadVO()
					.getCauditpsn()) != null) {
				listAuditInfo = new ArrayList();
				listAuditInfo.add(proceVOs[i].getHeadVO().getCauditpsn());
				listAuditInfo.add(proceVOs[i].getHeadVO().getDauditdate());
				mapAuditInfo.put(proceVOs[i].getHeadVO().getPrimaryKey(),
						listAuditInfo);
			}
			proceVOs[i].getHeadVO().setCauditpsn(strOperPk);
			proceVOs[i].getHeadVO().setDauditdate(today);
			proceVOs[i].getHeadVO().setCuserid(strOperPk);
		}

		ClientLink cl = new ClientLink(ClientEnvironment.getInstance());
		String errMsg = PuTool.getAuditLessThanMakeMsg(proceVOs,
				"dinvoicedate", "vinvoicecode", cl.getLogonDate(), "25");
		if (errMsg != null) {
			MessageDialog.showErrorDlg(this, NCLangRes.getInstance()
					.getStrByID("40040401", "UPPSCMCommon-000270"), errMsg);
			showHintMessage("");
			return;
		}

		try {
			PfUtilClient.processBatchFlow(this, "APPROVE", "25",
					ClientEnvironment.getInstance().getDate().toString(),
					proceVOs, null);
			if (PfUtilClient.isSuccess()) {
				setInvVOs(new InvoiceVO[] { (InvoiceVO) getVo() });
				setCurVOPos(0);

				setVOToAuditedBillPanel();

				this.m_btnAudit.setEnabled(false);
				this.m_btnUnAudit.setEnabled(true);
				updateButtons();
			} else if (mapAuditInfo.size() > 0) {
				for (int i = 0; i < proceVOs.length; ++i)
					if (!mapAuditInfo.containsKey(proceVOs[i].getHeadVO()
							.getPrimaryKey())) {
						proceVOs[i].getHeadVO().setCauditpsn(null);
						proceVOs[i].getHeadVO().setDauditdate(null);
					} else {
						listAuditInfo = (ArrayList) mapAuditInfo
								.get(proceVOs[i].getHeadVO().getPrimaryKey());
						proceVOs[i].getHeadVO().setCauditpsn(
								(String) listAuditInfo.get(0));
						proceVOs[i].getHeadVO().setDauditdate(
								(UFDate) listAuditInfo.get(1));
					}
			}
			
			// 采购发票审核时，如果来源单据单据类型为委外加工入库，则判断并执行回冲暂估--2014-11-05--王凯飞
						//edit --2015-02-06--wkf
						String pk_corp = ClientEnvironment.getInstance().getCorporation().getPk_corp();
						if(("1078".equals(pk_corp))||("1108".equals(pk_corp))){//edit by  mcw
							//应付单回冲
							DJZBVO[] checkDJvo = queryDJFBfromWW(proceVOs);//反回需要回冲的应付单VO
							String dvoddlx = proceVOs[0].getHeadVO().getCinvoiceid();//采购发票主键
							if(checkDJvo.length > 0){
								//执行应付单回冲并回写
								exeARAPandBack(checkDJvo,dvoddlx,cl);
								
//								//存货核算采购入库回冲    //2015-04-24制盖业务变更，没有生成存货核算的采购入库单，故不需要回冲，删除些方法
//								BillVO[] iabills =billQueryWW(proceVOs);
//								if(iabills.length>0){
//									//执行存货核算采购入库单加冲并回写
//									exeBILLandBack(iabills,dvoddlx,cl);
//								}
								//回写委外加工入库单vuserdef19 = "已暂估部分结算"/"已结算"
								backICStatus_wwjg(checkDJvo);
								
							}
							
						}
			
		} catch (Exception e) {
			SCMEnv.out(e);
			String strErrMsg = e.getMessage();
			if ((e instanceof TaskInvalidateException)
					|| (e instanceof EngineException))
				MessageDialog.showHintDlg(this, NCLangRes.getInstance()
						.getStrByID("40040401", "UPPSCMCommon-000270"),
						strErrMsg);
			else if ((strErrMsg != null)
					&& (strErrMsg.indexOf(NCLangRes.getInstance().getStrByID(
							"40040401", "UPP40040401-000161")) >= 0))
				MessageDialog.showHintDlg(this, NCLangRes.getInstance()
						.getStrByID("40040401", "UPPSCMCommon-000270"),
						NCLangRes.getInstance().getStrByID("40040401",
								"UPP40040401-000019"));
			else if ((e instanceof BusinessException)
					|| (e instanceof RemoteException))
				MessageDialog.showHintDlg(this, NCLangRes.getInstance()
						.getStrByID("40040401", "UPPSCMCommon-000270"), e
						.getMessage());
			else {
				MessageDialog.showHintDlg(this, NCLangRes.getInstance()
						.getStrByID("40040401", "UPPSCMCommon-000270"),
						NCLangRes.getInstance().getStrByID("40040401",
								"UPP40040401-000020"));
			}
			showHintMessage("");
			return;
		} finally {
			if (mapAuditInfo.size() > 0) {
				for (int i = 0; i < proceVOs.length; ++i) {
					if (!mapAuditInfo.containsKey(proceVOs[i].getHeadVO()
							.getPrimaryKey())) {
						proceVOs[i].getHeadVO().setCauditpsn(null);
						proceVOs[i].getHeadVO().setDauditdate(null);
					} else {
						listAuditInfo = (ArrayList) mapAuditInfo
								.get(proceVOs[i].getHeadVO().getPrimaryKey());
						proceVOs[i].getHeadVO().setCauditpsn(
								(String) listAuditInfo.get(0));
						proceVOs[i].getHeadVO().setDauditdate(
								(UFDate) listAuditInfo.get(1));
					}
				}
			}
		}
		showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH010"));
	}

	private void onBillAudit() {
		
		Timer timer = new Timer();
		timer.start("发票审批操作开始");
		
		
	
		int iInit = getInvVOs()[getCurVOPos()].getHeadVO().getFinitflag()
				.intValue();
		if (iInit == 1) {
			MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID(
					"40040401", "UPPSCMCommon-000270"), NCLangRes.getInstance()
					.getStrByID("40040401", "UPP40040401-000021"));
			showHintMessage("");
			return;
		}
		InvoiceVO[] proceVOs = { getInvVOs()[getCurVOPos()] };

		String strOperPk = ClientEnvironment.getInstance().getUser()
				.getPrimaryKey();
		UFDate today = ClientEnvironment.getInstance().getDate();

		ClientLink cl = new ClientLink(ClientEnvironment.getInstance());
		String errMsg = PuTool.getAuditLessThanMakeMsg(proceVOs,
				"dinvoicedate", "vinvoicecode", cl.getLogonDate(), "25");
		if (errMsg != null) {
			MessageDialog.showErrorDlg(this, NCLangRes.getInstance()
					.getStrByID("40040401", "UPPSCMCommon-000270"), errMsg);
			showHintMessage("");
			return;
		}
		for (int i = 0; i < proceVOs.length; ++i) {
			proceVOs[i].getHeadVO().setCauditpsn(strOperPk);
			proceVOs[i].getHeadVO().setDauditdate(today);
			proceVOs[i].getHeadVO().setCuserid(strOperPk);
			proceVOs[i].getHeadVO().setTaudittime(
					new UFDateTime(new Date()).toString());
		}
		try {
			
			PfUtilClient.processBatchFlow(this, "APPROVE", "25",
					ClientEnvironment.getInstance().getDate().toString(),
					proceVOs, null);

			timer.addExecutePhase("执行审批操作APPROVE");
			InvoiceVO resultVO = null;
			Integer iBillStatus = null;
			if (PfUtilClient.isSuccess()) {
				resultVO = InvoiceHelper
						.findByPrimaryKey(getInvVOs()[getCurVOPos()]
								.getHeadVO().getPrimaryKey());
				getInvVOs()[getCurVOPos()] = resultVO;
				iBillStatus = new Integer(88);
				if ((resultVO != null) && (resultVO.getHeadVO() != null)
						&& (resultVO.getHeadVO().getIbillstatus() != null)) {
					iBillStatus = resultVO.getHeadVO().getIbillstatus();
				}

				setVOToBillPanel();
		
			}
			
			timer.addExecutePhase("setVOToBillPanel");
			timer.showAllExecutePhase("发票审批操作结束");
			if ((iBillStatus.compareTo(BillStatus.FREE) == 0)
					|| (iBillStatus.compareTo(BillStatus.AUDITFAIL) == 0)) {
				showHintMessage(NCLangRes.getInstance().getStrByID("40040401",
						"UPP40040401-000234"));
			} else if ((iBillStatus.compareTo(BillStatus.AUDITED) == 0)
					|| (iBillStatus.compareTo(BillStatus.AUDITING) == 0))
				showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON",
						"UPPSCMCommon-000236"));
			
			
			// 采购发票审核时，如果来源单据单据类型为委外加工入库，则判断并执行回冲暂估--2014-11-05--王凯飞
			//edit --2015-02-06--wkf
			String pk_corp = ClientEnvironment.getInstance().getCorporation().getPk_corp();
			if(("1078".equals(pk_corp))||("1108".equals(pk_corp))){//edit by  mcw
				//应付单回冲
				DJZBVO[] checkDJvo = queryDJFBfromWW(proceVOs);//反回需要回冲的应付单VO
				String dvoddlx = proceVOs[0].getHeadVO().getCinvoiceid();//采购发票主键
				if(checkDJvo!=null&&checkDJvo.length > 0){
					//执行应付单回冲并回写
					exeARAPandBack(checkDJvo,dvoddlx,cl);
					
//					//存货核算采购入库回冲    //2015-04-24制盖业务变更，没有生成存货核算的采购入库单，故不需要回冲，删除些方法
//					BillVO[] iabills =billQueryWW(proceVOs);
//					if(iabills.length>0){
//						//执行存货核算采购入库单加冲并回写
//						exeBILLandBack(iabills,dvoddlx,cl);
//					}
					//回写委外加工入库单vuserdef19 = "已暂估部分结算"/"已结算"
					backICStatus_wwjg(checkDJvo);
					
				}
				
			}
			
		} catch (Exception e) {
			for (int i = 0; i < proceVOs.length; ++i) {
				proceVOs[i].getHeadVO().setCauditpsn(null);
				proceVOs[i].getHeadVO().setDauditdate(null);
				proceVOs[i].getHeadVO().setCuserid(null);
				proceVOs[i].getHeadVO().setTaudittime(null);
			}

			SCMEnv.out(e);
			String strErrMsg = e.getMessage();
			if ((strErrMsg != null)
					&& (strErrMsg.indexOf(NCLangRes.getInstance().getStrByID(
							"40040401", "UPP40040401-000161")) >= 0))
				MessageDialog.showHintDlg(this, NCLangRes.getInstance()
						.getStrByID("40040401", "UPPSCMCommon-000270"),
						NCLangRes.getInstance().getStrByID("40040401",
								"UPP40040401-000019"));
			else if ((e instanceof BusinessException)
					|| (e instanceof RemoteException))
				MessageDialog.showHintDlg(this, NCLangRes.getInstance()
						.getStrByID("40040401", "UPPSCMCommon-000270"), e
						.getMessage());
			else {
				MessageDialog.showHintDlg(this, NCLangRes.getInstance()
						.getStrByID("40040401", "UPPSCMCommon-000270"),
						NCLangRes.getInstance().getStrByID("40040401",
								"UPP40040401-000020"));
			}
			showHintMessage("");
			return;
		}
		
		
		
		
		showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH010"));
	}
	
	//执行应付单回冲并回写
	private void exeARAPandBack(DJZBVO[] checkDJvo,String dvoddlx,ClientLink cl) throws BusinessException {
		//组装回冲VO
		DJZBVO[] insetdjvo = zuzhuangInsetVO(checkDJvo,dvoddlx,cl);
		
		IArapForGYLPublic iArap = (IArapForGYLPublic) NCLocator.getInstance().lookup(IArapForGYLPublic.class.getName());
		for(int j = 0; j < insetdjvo.length; j++){
			iArap.saveEffForCG(insetdjvo[j]);
		}
		IArapBillPublic uArap = (IArapBillPublic) NCLocator.getInstance().lookup(IArapBillPublic.class.getName());
		for (int i = 0; i < checkDJvo.length; i++) {
			DJZBVO updatetmp = new DJZBVO();
			UFDouble zro = new UFDouble(0);
			DJZBHeaderVO djheadervo = (DJZBHeaderVO) checkDJvo[i].getParentVO();
//			djheadervo.setZyx28("已回冲");
			
			DJZBItemVO[] djbodyvo = (DJZBItemVO[]) checkDJvo[i].getChildrenVO();
			for (int j = 0; j < djbodyvo.length; j++) {
				djbodyvo[j].setZyx28("已回冲");
			}
			updatetmp.setParentVO(djheadervo);
			updatetmp.setChildrenVO(djbodyvo);
			uArap.editArapBill(updatetmp);
		}
	}
	
	//执行存货核算采购入库单加冲并回写
	private void exeBILLandBack(BillVO[] iabills,String dvoddlx,ClientLink cl) throws BusinessException, CloneNotSupportedException{
		//组装回冲VO
		BillVO[] insetdjvo = zzBillInsetVO(iabills,dvoddlx,cl);
		//生成负的存货核算采购入库单
		IBill billim = (IBill) NCLocator.getInstance().lookup(IBill.class.getName());
		billim.insertArray(insetdjvo);
		
		String pk_corp =iabills[0].getPk_corp();
		StringBuffer upsql = new StringBuffer();
		for (int i = 0; i < iabills.length; i++) {
			BillItemVO[] billbodyvo = (BillItemVO[]) iabills[i].getChildrenVO();
			for (int j = 0; j < billbodyvo.length; j++) {
				String cbillbid = billbodyvo[j].getCbill_bid();
				upsql.append("'"+cbillbid+"',");
			}
		}
		String usql = upsql.toString();
		usql = usql.substring(0, upsql.length()-1);
		IPubDMO ipubdmo = (IPubDMO)NCLocator.getInstance().lookup(IPubDMO.class.getName());
		StringBuffer exesql = new StringBuffer();
		exesql.append(" update ia_bill_b ") 
		.append("    set vdef19 = '已结算' ") 
		.append("  where nvl(dr,0)=0  ") 
		.append("    and nnumber > 0   ") 
		.append("    and pk_corp = '"+pk_corp+"' ") 
		.append("    and cbill_bid in ("+usql+") ") ;
	//更改暂估生成的存货核算采购入库单vdef19='已结算'
		ipubdmo.executeUpdate(exesql.toString());
	}
	
	
	
	/**
	 * 获得存货核算采购入库单
	 * 用与生成负的存货肯核算采购入库单 
	 * wkf--2015-04-22
	 * */
	private BillVO[]  billQueryWW(InvoiceVO[] invoices) {
		String hids = "";
		String bids = "";
		String pk_corp = invoices[0].getPk_corp();
		for (int i = 0; i < invoices.length; i++) {
			InvoiceItemVO[] bodyvo = (InvoiceItemVO[]) invoices[i].getChildrenVO();
			for (int j = 0; j < bodyvo.length; j++) {
				String ddlx = bodyvo[j].getCupsourcebillid();//来源单据主表id
				String ddhh = bodyvo[j].getCupsourcebillrowid();//来源单据子表id
				hids = hids+"'"+ddlx+"',";
				bids = bids+"'"+ddhh+"',";
			}
		}
		hids = hids.substring(0, hids.length()-1);
		bids = bids.substring(0, bids.length()-1);
		
		StringBuffer qsql = new StringBuffer();
		StringBuffer bsql = new StringBuffer();
		bsql.append(" select b.* from ia_bill h ") 
		.append("        left join ia_bill_b b ") 
		.append("        on h.cbillid = b.cbillid ") 
		.append("        where nvl(b.dr,0)=0  ") 
		.append("        and nvl(h.dr,0)=0 ") 
		.append("        and h.cbilltypecode = 'I2' ") 
		.append("        and csourcemodulename = 'PO' ") 
		.append("        and b.nmoney >0 ") 
		.append("        and h.pk_corp = '"+pk_corp+"' ") 
		.append("        and nvl(b.vdef19,'已暂估且未结算') = '已暂估且未结算' ") 
		.append("        and b.csourcebillitemid in ("+bids+") ");

		
		qsql.append(" select distinct h.* from ia_bill h ") 
		.append("        left join ia_bill_b b ") 
		.append("        on h.cbillid = b.cbillid ") 
		.append("        where nvl(b.dr,0)=0  ") 
		.append("        and nvl(h.dr,0)=0 ") 
		.append("        and h.cbilltypecode = 'I2' ") 
		.append("        and csourcemodulename = 'PO' ") 
		.append("        and b.nmoney >0 ") 
		.append("        and h.pk_corp = '"+pk_corp+"' ") 
		.append("        and nvl(b.vdef19,'已暂估且未结算') = '已暂估且未结算' ") 
		.append("        and b.csourcebillitemid in ("+bids+") ");
		//BillItemVO
		
		ArrayList headerlist = null;
		ArrayList bodylist = null;
		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		try {
			headerlist  = (ArrayList) query.executeQuery(qsql.toString(), new BeanListProcessor(BillHeaderVO.class));
		} catch (BusinessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		BillVO[] delVOs = new BillVO[headerlist.size()]; 
		if(headerlist.size()>0){
			for (int i = 0; i < headerlist.size(); i++) {
				BillVO invo = new BillVO(); 
				BillHeaderVO headtmpvo = (BillHeaderVO) headerlist.get(i);
				invo.setParentVO(headtmpvo);
				String tmpsql = " and h.cbillid = '"+headtmpvo.getCbillid()+"' ";
				bsql.append(tmpsql);
				try {
					bodylist  = (ArrayList) query.executeQuery(bsql.toString(), new BeanListProcessor(BillItemVO.class));
				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(bodylist.size()>0 && bodylist !=null){
					BillItemVO[] bodywvo = (BillItemVO[]) bodylist.toArray(new BillItemVO[bodylist.size()]);
					invo.setChildrenVO(bodywvo);
				}
				delVOs[i]=invo;
			}
		}
		
		return delVOs;
	}
	
	/**
	 * 王凯飞
	 * 回写委外加工入库单vuserdef19
	 * 审核
	 */
	public void backICStatus_wwjg(DJZBVO[] vOs) throws BusinessException{
		if(vOs!=null&&vOs.length>0){
			String status = "已结算";
			IPubDMO ipubdmo = (IPubDMO)NCLocator.getInstance().lookup(IPubDMO.class.getName());
			for(int i=0;i<vOs.length;i++){
				DJZBItemVO[] bod = (DJZBItemVO[]) vOs[i].getChildrenVO();
				String ddhh = "";
				for (int j = 0; j < bod.length; j++) {
					String ddhht = bod[0].getDdhh()==null?"":bod[j].getDdhh().toString();
					ddhh = ddhh+"'"+ddhht+"',";
				}
				ddhh = ddhh.substring(0,ddhh.length()-1);
				//回写
				String sql = "update ic_general_b set vuserdef19='"+status+"' where cgeneralbid in ("+ddhh+")  and pk_corp = '"+getPk_corp()+"'";
				ipubdmo.executeUpdate(sql);
			}
		}
	}
	/**
	 * 王凯飞
	 * 回写委外加工入库单vuserdef19
	 * 弃审
	 */
	public void backICStatus_wwjg1(DJZBVO[] vOs) throws BusinessException{
		if(vOs!=null&&vOs.length>0){
			String status = "已暂估且未结算";
			IPubDMO ipubdmo = (IPubDMO)NCLocator.getInstance().lookup(IPubDMO.class.getName());
			for(int i=0;i<vOs.length;i++){
				DJZBItemVO[] bod = (DJZBItemVO[]) vOs[i].getChildrenVO();
				String ddhh ="";
				for (int j = 0; j < bod.length; j++) {
					String ddhht = bod[0].getDdhh()==null?"":bod[j].getDdhh().toString();
					ddhh = ddhh+"'"+ddhht+"',";
				}
				ddhh = ddhh.substring(0,ddhh.length()-1);
				//回写
				String sql = "update ic_general_b set vuserdef19='"+status+"',isok='N'  where cgeneralbid in ("+ddhh+") and pk_corp = '"+getPk_corp()+"' ";
//				String sql1 = "update ic_general_bb3 set naccountnum1='0'  where cgeneralbid in ("+ddhh+") and  pk_corp = '"+getPk_corp()+"' ";//审核发票后会生成结算单回写的此字段，删除结算单，没有回写所字段，测试时用的。
				ipubdmo.executeUpdate(sql);
//				ipubdmo.executeUpdate(sql1);
			}
		}
	}
	/**王凯飞
	 * 检查vo发票是否有暂估（委外加工费）*/
	private DJZBVO[] queryDJFBfromWW(InvoiceVO[] Invoicevo) {
		String checkbilltype = "";
		String hids = "";
		String bids = "";
		String pk_corp = Invoicevo[0].getPk_corp();
		for (int i = 0; i < Invoicevo.length; i++) {
			InvoiceItemVO[] bodyvo = (InvoiceItemVO[]) Invoicevo[i].getChildrenVO();
			for (int j = 0; j < bodyvo.length; j++) {
				String ddlx = bodyvo[j].getCupsourcebillid();//来源单据主表id
				String ddhh = bodyvo[j].getCupsourcebillrowid();//来源单据子表id
				checkbilltype = bodyvo[j].getCupsourcebilltype();//来源单据类型
				hids = hids+"'"+ddlx+"',";
				bids = bids+"'"+ddhh+"',";
			}
		}
		if(checkbilltype.equals("47")){//校验来源单据类型为委外加工入库	
			hids = hids.substring(0, hids.length()-1);
			bids = bids.substring(0, bids.length()-1);
			
			StringBuffer qsql = new StringBuffer();
			StringBuffer bsql = new StringBuffer();
			qsql.append(" select distinct h.* ")
			.append("       from arap_djzb h  ")
			.append("        inner join arap_djfb b ") 
			.append("        on h.vouchid = b.vouchid ")
			.append("        where nvl(b.dr,0)=0 ") 
			.append("        and nvl(h.dr,0)=0 ") 
			.append("        and h.bbje > 0 ")
			.append("        and h.dwbm = '"+pk_corp+"' ")
			.append("        and nvl(b.zyx28,'未回冲') = '未回冲' ")
			.append("        and b.ddhh in ("+bids+") ");
			
			bsql.append(" select b.* ")
			.append("       from arap_djzb h  ")
			.append("        inner join arap_djfb b ") 
			.append("        on h.vouchid = b.vouchid ")
			.append("        where nvl(b.dr,0)=0 ") 
			.append("        and nvl(h.dr,0)=0 ") 
			.append("        and h.bbje > 0 ")
			.append("        and h.dwbm = '"+pk_corp+"' ")
			.append("        and nvl(b.zyx28,'未回冲') = '未回冲' ")
			.append("        and b.ddhh in ("+bids+") ");
			
			ArrayList headerlist = null;
			ArrayList bodylist = null;
			IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			try {
				headerlist  = (ArrayList) query.executeQuery(qsql.toString(), new BeanListProcessor(DJZBHeaderVO.class));
			} catch (BusinessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			DJZBVO[] delVOs = new DJZBVO[headerlist.size()]; 
			if(headerlist.size()>0){
				for (int i = 0; i < headerlist.size(); i++) {
					DJZBVO invo = new DJZBVO(); 
					DJZBHeaderVO headtmpvo = (DJZBHeaderVO) headerlist.get(i);   //存值
					invo.setParentVO(headtmpvo);                        //将headtmpvo写入invo的主表vo
					String tmpsql = " and h.vouchid = '"+headtmpvo.getVouchid()+"' ";   //单据主表ID
			//      bsql.append(tmpsql); //子表bsql数据带tmpsql的条件查找
					
					String newsql = bsql.toString();  //add by yqq  2016-6-15 将bsql转换为newsql
					newsql = newsql+tmpsql;   //add by yqq  2016-6-15newsql加tmpsql的条件查找
					try {
            //		bodylist  = (ArrayList) query.executeQuery(bsql.toString(), new BeanListProcessor(DJZBItemVO.class)); //查找后得到bsql保存到bodylist集合
			//      edit by yqq 2016-6-15 bsql执行第二次的时候组装bodylist集合里无值
						bodylist  = (ArrayList) query.executeQuery(newsql.toString(), new BeanListProcessor(DJZBItemVO.class)); //查找后得到newsql保存到bodylist集合
					} catch (BusinessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(bodylist.size()>0 && bodylist !=null){
						DJZBItemVO[] bodywvo = (DJZBItemVO[]) bodylist.toArray(new DJZBItemVO[bodylist.size()]);
						invo.setChildrenVO(bodywvo);
					}
					delVOs[i]=invo;
				}
			}
			
			return delVOs;
		}else{
			return null;
		}
		
		
	}
	
	
	private void onListAudit() {
		Vector validVOsVEC = new Vector();

		Vector validIndexVEC = new Vector();
		for (int i = 0; i < getInvVOs().length; ++i) {
			if (this.m_InvListPanel.getHeadBillModel().getRowState(i) != 4)
				continue;
			int nCurIndex = PuTool.getIndexBeforeSort(this.m_InvListPanel, i);

			int iInit = getInvVOs()[nCurIndex].getHeadVO().getFinitflag()
					.intValue();
			if (iInit == 1) {
				MessageDialog.showHintDlg(this, NCLangRes.getInstance()
						.getStrByID("SCMCOMMON", "UPPSCMCommon-000270"),
						NCLangRes.getInstance().getStrByID("40040402",
								"UPP40040402-000002"));
				showHintMessage("");
				return;
			}

			validIndexVEC.addElement(new Integer(nCurIndex));
			validVOsVEC.addElement(getInvVOs()[nCurIndex]);
		}

		if (validVOsVEC.size() == 0) {
			MessageDialog
					.showHintDlg(this, NCLangRes.getInstance().getStrByID(
							"SCMCOMMON", "UPPSCMCommon-000270"), NCLangRes
							.getInstance().getStrByID("40040402",
									"UPP40040402-000003"));
			setSelectedRowCount(0);
			showHintMessage("");
			return;
		}
		if (validVOsVEC.size() > 0) {
			for (int i = 0; i < validVOsVEC.size(); ++i) {
				if (PuPubVO
						.getString_TrimZeroLenAsNull(((InvoiceVO) validVOsVEC
								.get(i)).getHeadVO().getCinvoiceid()) == null) {
					MessageDialog.showHintDlg(this, NCLangRes.getInstance()
							.getStrByID("SCMCOMMON", "UPPSCMCommon-000270"),
							NCLangRes.getInstance().getStrByID("40040402",
									"UPP40040402-000003"));
					setSelectedRowCount(0);
					showHintMessage("");
					return;
				}
			}

		}

		InvoiceVO[] proceVOs = new InvoiceVO[validVOsVEC.size()];
		validVOsVEC.copyInto(proceVOs);

		String strOperPk = ClientEnvironment.getInstance().getUser()
				.getPrimaryKey();
		UFDate today = ClientEnvironment.getInstance().getDate();

		HashMap mapAuditInfo = new HashMap();
		ArrayList listAuditInfo = null;

		ClientLink cl = new ClientLink(ClientEnvironment.getInstance());
		String errMsg = PuTool.getAuditLessThanMakeMsg(proceVOs,
				"dinvoicedate", "vinvoicecode", cl.getLogonDate(), "25");
		if (errMsg != null) {
			MessageDialog.showErrorDlg(this, NCLangRes.getInstance()
					.getStrByID("40040401", "UPPSCMCommon-000270"), errMsg);
			showHintMessage("");
			return;
		}

		for (int i = 0; i < proceVOs.length; ++i) {
			if (PuPubVO.getString_TrimZeroLenAsNull(proceVOs[i].getHeadVO()
					.getCauditpsn()) != null) {
				listAuditInfo = new ArrayList();
				listAuditInfo.add(proceVOs[i].getHeadVO().getCauditpsn());
				listAuditInfo.add(proceVOs[i].getHeadVO().getDauditdate());
				mapAuditInfo.put(proceVOs[i].getHeadVO().getPrimaryKey(),
						listAuditInfo);
			}
			proceVOs[i].getHeadVO().setCauditpsn(strOperPk);
			proceVOs[i].getHeadVO().setDauditdate(today);
			proceVOs[i].getHeadVO().setCuserid(strOperPk);
			proceVOs[i].getHeadVO().setTaudittime(
					new UFDateTime(new Date()).toString());
		}
		try {
			if (!loadItemsForInvoiceVOs(proceVOs)) {
				showHintMessage("");
				return;
			}
			PfUtilClient.processBatchFlow(this, "APPROVE", "25",
					ClientEnvironment.getInstance().getDate().toString(),
					proceVOs, null);
			
			// 采购发票审核时，如果来源单据单据类型为委外加工入库，则判断并执行回冲暂估--2014-11-05--王凯飞
			//edit --2015-02-06--wkf
						String pk_corp = ClientEnvironment.getInstance().getCorporation().getPk_corp();
						if(("1078".equals(pk_corp))||("1108".equals(pk_corp))){//edit by  mcw
							//应付单回冲
							DJZBVO[] checkDJvo = queryDJFBfromWW(proceVOs);//反回需要回冲的应付单VO
							String dvoddlx = proceVOs[0].getHeadVO().getCinvoiceid();//采购发票主键
							if(checkDJvo.length > 0){
								//执行应付单回冲并回写
								exeARAPandBack(checkDJvo,dvoddlx,cl);
								
//								//存货核算采购入库回冲    //2015-04-24制盖业务变更，没有生成存货核算的采购入库单，故不需要回冲，删除些方法
//								BillVO[] iabills =billQueryWW(proceVOs);
//								if(iabills.length>0){
//									//执行存货核算采购入库单加冲并回写
//									exeBILLandBack(iabills,dvoddlx,cl);
//								}
								//回写委外加工入库单vuserdef19 = "已暂估部分结算"/"已结算"
								backICStatus_wwjg(checkDJvo);
								
							}
							
						}
		} catch (Exception e) {
			for (int i = 0; i < proceVOs.length; ++i) {
				proceVOs[i].getHeadVO().setCauditpsn(null);
				proceVOs[i].getHeadVO().setDauditdate(null);
				proceVOs[i].getHeadVO().setCuserid(null);
				proceVOs[i].getHeadVO().setTaudittime(null);
			}

			SCMEnv.out(e);
			String strErrMsg = e.getMessage();
			if ((strErrMsg != null)
					&& (strErrMsg.indexOf(NCLangRes.getInstance().getStrByID(
							"40040402", "UPP40040402-000019")) >= 0))
				MessageDialog.showHintDlg(this, NCLangRes.getInstance()
						.getStrByID("SCMCOMMON", "UPPSCMCommon-000270"),
						NCLangRes.getInstance().getStrByID("40040402",
								"UPP40040402-000004"));
			else if ((e instanceof BusinessException)
					|| (e instanceof RemoteException))
				MessageDialog.showHintDlg(this, NCLangRes.getInstance()
						.getStrByID("SCMCOMMON", "UPPSCMCommon-000270"), e
						.getMessage());
			else {
				MessageDialog.showHintDlg(this, NCLangRes.getInstance()
						.getStrByID("SCMCOMMON", "UPPSCMCommon-000270"),
						NCLangRes.getInstance().getStrByID("40040402",
								"UPP40040402-000005"));
			}
			showHintMessage("");
			return;
		} finally {
			if (mapAuditInfo.size() > 0) {
				for (int i = 0; i < proceVOs.length; ++i) {
					if (!mapAuditInfo.containsKey(proceVOs[i].getHeadVO()
							.getPrimaryKey())) {
						proceVOs[i].getHeadVO().setCauditpsn(null);
						proceVOs[i].getHeadVO().setDauditdate(null);
					} else {
						listAuditInfo = (ArrayList) mapAuditInfo
								.get(proceVOs[i].getHeadVO().getPrimaryKey());
						proceVOs[i].getHeadVO().setCauditpsn(
								(String) listAuditInfo.get(0));
						proceVOs[i].getHeadVO().setDauditdate(
								(UFDate) listAuditInfo.get(1));
					}
				}
			}
		}
		if (PfUtilClient.isSuccess()) {
			Integer[] iaIndex = new Integer[validIndexVEC.size()];
			validIndexVEC.copyInto(iaIndex);

			removeSomeFromInvVOs(iaIndex);
			setVOsToListPanel();

			showHintMessage(NCLangRes.getInstance().getStrByID("common",
					"UCH010"));
			return;
		}

		if (mapAuditInfo.size() > 0) {
			for (int i = 0; i < proceVOs.length; ++i) {
				if (!mapAuditInfo.containsKey(proceVOs[i].getHeadVO()
						.getPrimaryKey())) {
					proceVOs[i].getHeadVO().setCauditpsn(null);
					proceVOs[i].getHeadVO().setDauditdate(null);
				} else {
					listAuditInfo = (ArrayList) mapAuditInfo.get(proceVOs[i]
							.getHeadVO().getPrimaryKey());
					proceVOs[i].getHeadVO().setCauditpsn(
							(String) listAuditInfo.get(0));
					proceVOs[i].getHeadVO().setDauditdate(
							(UFDate) listAuditInfo.get(1));
				}
			}
		}

		showHintMessage("");
	}
	
	/**
	   * @returnDJZBVO[]
	   * 组装要回冲的应付单VO
	   * @author 王凯飞
	   * */
	  private DJZBVO[] zuzhuangInsetVO(DJZBVO[] djzbvo,String dvoddlx,ClientLink cl) {
		  String month = cl.getAccountMonth();
		  String year = cl.getAccountYear();
		  UFDate date = cl.getLogonDate();
		  
		  DJZBVO[] insetdjvo = new DJZBVO[djzbvo.length];
		  for (int i = 0; i < djzbvo.length; i++) {
				DJZBVO insettmp = new DJZBVO();
				UFDouble zro = new UFDouble(0);
				
				UFDouble bbjesum = new UFDouble(0);
				UFDouble fbjesum = new UFDouble(0);
				UFDouble ybjesum = new UFDouble(0);
				
				DJZBVO djzbs = (DJZBVO) djzbvo[i].clone();//克隆VO
				
				DJZBItemVO[] djbodyvo = (DJZBItemVO[]) djzbs.getChildrenVO().clone();
				for (int j = 0; j < djbodyvo.length; j++) {
					UFDouble bbye = djbodyvo[j].getBbye();//本币余额
					UFDouble dfbbje = djbodyvo[j].getDfbbje();
					UFDouble dfbbsj = djbodyvo[j].getDfbbsj();
					UFDouble dfbbwsje = djbodyvo[j].getDfbbwsje();
					UFDouble dfybje = djbodyvo[j].getDfybje();
					UFDouble dfybsj = djbodyvo[j].getDfybsj();
					UFDouble dfybwsje = djbodyvo[j].getDfybwsje();
					UFDouble hsdj = djbodyvo[j].getHsdj();
					UFDouble ybye = djbodyvo[j].getYbye();
					UFDouble fbjeb = djbodyvo[j].getDfbbje();
					
					bbjesum = bbjesum.add(dfbbje);
					fbjesum = fbjesum.add(fbjeb);
					ybjesum = ybjesum.add(dfybje);
		
					djbodyvo[j].setBbye(zro.sub(bbye));
					djbodyvo[j].setDfbbje(zro.sub(dfbbje));
					djbodyvo[j].setDfbbsj(zro.sub(dfbbsj));
					djbodyvo[j].setDfbbwsje(zro.sub(dfbbwsje));
					djbodyvo[j].setDfybje(zro.sub(dfybje));
					djbodyvo[j].setDfybsj(zro.sub(dfybsj));
					djbodyvo[j].setDfybwsje(zro.sub(dfybwsje));
					djbodyvo[j].setYbye(zro.sub(ybye));
					djbodyvo[j].setZyx28(dvoddlx);//应加回冲的运费发票主键
					djbodyvo[j].setBilldate(date);
					djbodyvo[j].setOthersysflag("委外加工费红冲");
				}
				
				DJZBHeaderVO djheadervo = (DJZBHeaderVO) djzbs.getParentVO().clone();
				djheadervo.setDjbh(null);
				djheadervo.setDjrq(date);
				djheadervo.setZdrq(date);
				djheadervo.setTs(null);
				djheadervo.setZgyf(new Integer(2));//红冲暂估标志
				djheadervo.setBbje(zro.sub(bbjesum));
				djheadervo.setFbje(zro.sub(fbjesum));
				djheadervo.setYbje(zro.sub(ybjesum));
				
				insettmp.setParentVO(djheadervo);
				insettmp.setChildrenVO(djbodyvo);
				insetdjvo[i]=insettmp;
			}
		  
		return insetdjvo;
	  }
	
	  /**
	   * @return BillVO[]
	   * 组装要回冲的存货核算VO
	   * @author 王凯飞
	 * @throws CloneNotSupportedException 
	   * */
	private BillVO[] zzBillInsetVO(BillVO[] iabills,String dvoddlx,ClientLink cl) throws CloneNotSupportedException{
		
		String month = cl.getAccountMonth();
		String year = cl.getAccountYear();
		UFDate date = cl.getLogonDate();
		
		BillVO[] insetbillvo = new BillVO[iabills.length];
		  for (int i = 0; i < iabills.length; i++) {
			  	BillVO insettmp = new BillVO();
				UFDouble zro = new UFDouble(0);
				
				BillVO bills = (BillVO) iabills[i].clone();//克隆VO
				//BillItemVO
				
				BillItemVO[] billbodyvo = (BillItemVO[]) bills.getChildrenVO().clone();
				for (int j=0; j < billbodyvo.length; j++) {
					UFDouble number = billbodyvo[j].getNnumber();
					UFDouble money = billbodyvo[j].getNmoney();
					UFDouble nsimulatemny = billbodyvo[j].getNsimulatemny();
					
					billbodyvo[j].setNnumber(zro.sub(number));
					billbodyvo[j].setNmoney(zro.sub(money));
					billbodyvo[j].setNsimulatemny(zro.sub(nsimulatemny));
					
					billbodyvo[j].setVdef19(dvoddlx);//应加回冲的发票主键
					billbodyvo[j].setDbizdate(date);
					
				}
				
				BillHeaderVO billheadervo = (BillHeaderVO) bills.getParentVO().clone();
				billheadervo.setCbillid(null);
				billheadervo.setDbilldate(date);
				billheadervo.setVbillcode(null);
				
				insettmp.setParentVO(billheadervo);
				insettmp.setChildrenVO(billbodyvo);
				insetbillvo[i]=insettmp;
			}
		  
		return insetbillvo;
		
	}
	  
	private void onListUnAudit() {
		Vector validVOsVEC = new Vector();

		Vector validIndexVEC = new Vector();
		for (int i = 0; i < getInvVOs().length; ++i) {
			if (getInvListPanel().getHeadBillModel().getRowState(i) != 4)
				continue;
			int nCurIndex = PuTool.getIndexBeforeSort(getInvListPanel(), i);

			validIndexVEC.addElement(new Integer(nCurIndex));
			validVOsVEC.addElement(getInvVOs()[nCurIndex]);
		}

		if (validVOsVEC.size() == 0) {
			MessageDialog
					.showHintDlg(this, NCLangRes.getInstance().getStrByID(
							"SCMCOMMON", "UPPSCMCommon-000270"), NCLangRes
							.getInstance().getStrByID("40040402",
									"UPP40040402-000006"));
			setSelectedRowCount(0);
			showHintMessage("");
			return;
		}

		InvoiceVO[] proceVOs = new InvoiceVO[validVOsVEC.size()];
		validVOsVEC.copyInto(proceVOs);

		String[] strAuditIDs = new String[proceVOs.length];

		HashMap mapAuditInfo = new HashMap();
		ArrayList listAuditInfo = null;
		try {
			for (int i = 0; i < proceVOs.length; ++i) {
				strAuditIDs[i] = proceVOs[i].getHeadVO().getCauditpsn();

				if (PuPubVO.getString_TrimZeroLenAsNull(strAuditIDs[i]) != null) {
					listAuditInfo = new ArrayList();
					listAuditInfo.add(proceVOs[i].getHeadVO().getCauditpsn());
					listAuditInfo.add(proceVOs[i].getHeadVO().getDauditdate());
					mapAuditInfo.put(proceVOs[i].getHeadVO().getPrimaryKey(),
							listAuditInfo);
				}
				proceVOs[i].getHeadVO().setCauditpsn(
						ClientEnvironment.getInstance().getUser()
								.getPrimaryKey());
				proceVOs[i].getHeadVO().setCuserid(
						ClientEnvironment.getInstance().getUser()
								.getPrimaryKey());
				proceVOs[i].getHeadVO().setTaudittime(null);
			}
			if (!loadItemsForInvoiceVOs(proceVOs)) {
				showHintMessage("");
				return;
			}

			if ((this.m_sZGYF == null)
					|| (!new UFBoolean(this.m_sZGYF).booleanValue())) {
				InvoiceVO invoiceVO = new InvoiceVO();
				for (int i = 0; i < proceVOs.length; ++i) {
					invoiceVO = proceVOs[i];
					Object oTemp = CacheTool.getCellValue("bd_busitype",
							"pk_busitype", "verifyrule", invoiceVO.getHeadVO()
									.getCbiztype());
					if (oTemp != null) {
						Object[] oTemp1 = (Object[]) (Object[]) oTemp;
						if (oTemp1[0].equals("S"))
							continue;
						if (!oTemp1[0].equals("Z")) {
							if (invoiceVO.getHeadVO().getIinvoicetype()
									.intValue() != 3) {
								InvoiceItemVO[] tempBodyVO = invoiceVO
										.getBodyVO();
								for (int j = 0; j < tempBodyVO.length; ++j)
									if (tempBodyVO[j].getCupsourcebilltype() != null) {
										if (((!tempBodyVO[j]
												.getCupsourcebilltype().equals(
														"45")) && (!tempBodyVO[j]
												.getCupsourcebilltype().equals(
														"47")))
												|| (this.m_sOrder2InvoiceSettleMode == null)
												|| (!this.m_sStock2InvoiceSettleMode
														.equals("审批时自动结算"))
												|| (tempBodyVO[j]
														.getNaccumsettmny() == null)
												|| (tempBodyVO[j]
														.getNaccumsettmny()
														.doubleValue() == 0.0D))
											continue;
										MessageDialog
												.showHintDlg(
														this,
														NCLangRes
																.getInstance()
																.getStrByID(
																		"40040401",
																		"UPPSCMCommon-000270"),
														NCLangRes
																.getInstance()
																.getStrByID(
																		"40040401",
																		"UPP40040401-000022"));
										showHintMessage("");
										return;
									}
							}
						}
					}
				}
			}
			PfUtilClient.processBatch("UNAPPROVE", "25", ClientEnvironment
					.getInstance().getDate().toString(), proceVOs);
			
			// 查询是否有回冲应付单,如果有则删除，并回写暂估应付回冲标志
			 // 王凯飞
			 // 2014-11-07
			// edit--2015-02-06--wkf
			 
			String pk_corp = ClientEnvironment.getInstance().getCorporation().getPk_corp();
			InvoiceItemVO[] bvoss = (InvoiceItemVO[]) proceVOs[0].getChildrenVO();
			String checktype = bvoss[0].getCupsourcebilltype();
			if(checktype.equals("47")){
				if(("1078".equals(pk_corp))||("1108".equals(pk_corp))){//edit by  mcw
					String invoice = (String) proceVOs[0].getParentVO().getAttributeValue(
							"cinvoiceid");
					DJZBVO[] delete = queryDJZBfromZxy(invoice);
					if(delete.length >0){
						//删除负的暂估应付单
						IArapBillPublic beanfu = (IArapBillPublic) NCLocator.getInstance()
								.lookup(IArapBillPublic.class.getName());
						for (int i = 0; i < delete.length; i++) {
							beanfu.deleteArapBill(delete[i]);
						}
						DJZBVO[] backdj = queryDJfromdel(delete);
						IArapBillPublic uArap = (IArapBillPublic) NCLocator.getInstance().lookup(IArapBillPublic.class.getName());
						for (int i = 0; i < delete.length; i++) {
							DJZBVO updatetmp = new DJZBVO();
							DJZBHeaderVO djheadvo = (DJZBHeaderVO) backdj[i].getParentVO();
//						djheadvo.setZyx28(null);
							
							DJZBItemVO[] djbodyvo = (DJZBItemVO[]) backdj[i].getChildrenVO();
							for (int j = 0; j < djbodyvo.length; j++) {
								djbodyvo[j].setZyx28(null);
							}
							updatetmp.setParentVO(djheadvo);
							updatetmp.setChildrenVO(djbodyvo);
							uArap.editArapBill(updatetmp);
						}
//					//删除存货核算采购入库单回冲单据//2015-04-24制盖业务变更，不用生成存货核算，没有生成回冲，此方法删除--wkf--
//					BillVO[] delete1 = queryBillfromZG(invoice,pk_corp);
//					if(delete1.length>0){
//						//删除存货核算采购入库单回冲单据
//						IBill billim = (IBill) NCLocator.getInstance().lookup(IBill.class.getName());
//						ClientLink cl = new ClientLink(ClientEnvironment.getInstance());
//						billim.deleteArray(cl, delete1, cl.getUser());
//						
//						//还原存货核算采购入库单vdef19->'已暂估且未结算'
//						String sssql = getUpdateSql(delete1,pk_corp);
//						IPubDMO ipubdmo = (IPubDMO)NCLocator.getInstance().lookup(IPubDMO.class.getName());
//						ipubdmo.executeUpdate(sssql);
//					}
						//回写
						backICStatus_wwjg1(delete);
					}
				}
			}
			
			//end 		
		} catch (Exception e) {
			String strErrMsg = e.getMessage();
			if ((strErrMsg != null)
					&& (((strErrMsg.indexOf(NCLangRes.getInstance().getStrByID(
							"40040402", "UPP40040402-000020")) >= 0) || (strErrMsg
							.indexOf(NCLangRes.getInstance().getStrByID(
									"40040402", "UPP40040402-000021")) >= 0)))) {
				strErrMsg = NCLangRes.getInstance().getStrByID("40040402",
						"UPP40040402-000007");
				MessageDialog.showHintDlg(this, NCLangRes.getInstance()
						.getStrByID("SCMCOMMON", "UPPSCMCommon-000270"),
						strErrMsg);
			} else if (e instanceof BusinessException) {
				MessageDialog.showHintDlg(this, NCLangRes.getInstance()
						.getStrByID("SCMCOMMON", "UPPSCMCommon-000270"),
						strErrMsg);
			} else {
				strErrMsg = NCLangRes.getInstance().getStrByID("40040402",
						"UPP40040402-000008");
				SCMEnv.out("!!! 以下信息可发给系统管理员参考：\n");
				SCMEnv.out(e);
				MessageDialog.showHintDlg(this, NCLangRes.getInstance()
						.getStrByID("SCMCOMMON", "UPPSCMCommon-000270"),
						strErrMsg);
			}
			showHintMessage("");
			return;
		} finally {
			if (mapAuditInfo.size() > 0) {
				for (int i = 0; i < proceVOs.length; ++i) {
					if (!mapAuditInfo.containsKey(proceVOs[i].getHeadVO()
							.getPrimaryKey())) {
						proceVOs[i].getHeadVO().setCauditpsn(null);
						proceVOs[i].getHeadVO().setDauditdate(null);
					} else {
						listAuditInfo = (ArrayList) mapAuditInfo
								.get(proceVOs[i].getHeadVO().getPrimaryKey());
						proceVOs[i].getHeadVO().setCauditpsn(
								(String) listAuditInfo.get(0));
						proceVOs[i].getHeadVO().setDauditdate(
								(UFDate) listAuditInfo.get(1));
					}
				}
			}
		}

		if (PfUtilClient.isSuccess()) {
			Integer[] iaIndex = new Integer[validIndexVEC.size()];
			validIndexVEC.copyInto(iaIndex);
			removeSomeFromInvVOs(iaIndex);
			setVOsToListPanel();

			showHintMessage(NCLangRes.getInstance().getStrByID("common",
					"UCH011"));
			return;
		}

		if (mapAuditInfo.size() > 0) {
			for (int i = 0; i < proceVOs.length; ++i) {
				if (!mapAuditInfo.containsKey(proceVOs[i].getHeadVO()
						.getPrimaryKey())) {
					proceVOs[i].getHeadVO().setCauditpsn(null);
					proceVOs[i].getHeadVO().setDauditdate(null);
				} else {
					listAuditInfo = (ArrayList) mapAuditInfo.get(proceVOs[i]
							.getHeadVO().getPrimaryKey());
					proceVOs[i].getHeadVO().setCauditpsn(
							(String) listAuditInfo.get(0));
					proceVOs[i].getHeadVO().setDauditdate(
							(UFDate) listAuditInfo.get(1));
				}
			}
		}
		showHintMessage("");
	}

	/** @deprecated */
	private void onBillCancel() {
		int result = 4;
		if (result == 4) {
			int nVOStatus = 0;
			if (getInvVOs() != null) {
				nVOStatus = getInvVOs()[getCurVOPos()].getSource();
			}

			if ((getInvVOs() != null) && (nVOStatus != 4)) {
				removeOneFromInvVOs(getCurVOPos());
			}

			if (getInvVOs() == null) {
				setListOperState(4);
				setBillBrowseState(2);
			}

			if ((nVOStatus != 0) && (nVOStatus != 4)) {
				setCurOperState(getListOperState());
				shiftShowModeTo(1);
				setVOsToListPanel();
				this.m_bCopy = false;
				return;
			}

			if (getInvVOs() == null) {
				getInvBillPanel().addNew();
				getInvBillPanel().setEnabled(false);
				setCurOperState(getBillBrowseState());
			} else {
				if ((getInvVOs() != null) && (getInvVOs().length > 0)) {
					InvoiceVO curVO = getInvVOs()[getCurVOPos()];
					String id = curVO.getHeadVO().getCinvoiceid();
					if (id != null) {
						Object o = this.hBillStatusBeforeEdit.get(id);
						if (o != null) {
							Integer iBillStatus = new Integer(o.toString());
							if (iBillStatus.intValue() == 4) {
								curVO.getHeadVO()
										.setIbillstatus(new Integer(4));
								this.hBillStatusBeforeEdit.put(id, new Integer(
										0));
							}
						}
					}
				}
				setCurOperState(getBillBrowseState());
				setVOToBillPanel();
			}
		} else if (result == 8) {
			onSave();
		}
		this.m_bAdd = false;
		this.m_bCopy = false;
	}

	private void onBillCombin() {
		CollectSettingDlg dlg = new CollectSettingDlg(this);
		dlg.initData(getInvBillPanel(), new String[] { "invcode", "invname",
				"invspec", "invtype", "cproducearea" }, null, new String[] {
				"ninvoicenum", "noriginalcurprice", "noriginalcurmny",
				"noriginaltaxpricemny" }, null, new String[] {
				"noriginalcurprice", "norgnettaxprice" }, "ninvoicenum");

		dlg.showModal();
	}

	private void onBillQuery() {
		getQueDlg();
		this.m_InvQueDlg.showModal();

		this.m_InvQueDlg.setRefsDataPowerConVOs(getCurOperator(),
				new String[] { getPk_corp() }, IDataPowerForInv.REFNAMES,
				IDataPowerForInv.REFKEYS, IDataPowerForInv.RETURNTYPES);

		if (!this.m_InvQueDlg.isCloseOK()) {
			return;
		}

		execBillQuery(this.m_InvQueDlg);

		setEverQueryed(true);
	}

	/** @deprecated */
	private void onBillRefresh() {
		execBillQuery(getQueDlg());
	}

	private void onBillUnAudit() {
		Timer timer = new Timer();
		timer.start("采购发票弃审开始");

		InvoiceVO[] proceVOs = { getInvVOs()[getCurVOPos()] };

		HashMap mapAuditInfo = new HashMap();
		ArrayList listAuditInfo = null;
		try {
			
			for (int i = 0; i < proceVOs.length; ++i) {
				if (PuPubVO.getString_TrimZeroLenAsNull(proceVOs[i].getHeadVO()
						.getCauditpsn()) != null) {
					listAuditInfo = new ArrayList();
					listAuditInfo.add(proceVOs[i].getHeadVO().getCauditpsn());
					listAuditInfo.add(proceVOs[i].getHeadVO().getDauditdate());
					mapAuditInfo.put(proceVOs[i].getHeadVO().getPrimaryKey(),
							listAuditInfo);
				}
				proceVOs[i].getHeadVO().setCauditpsn(
						ClientEnvironment.getInstance().getUser()
								.getPrimaryKey());
				proceVOs[i].getHeadVO().setCuserid(
						ClientEnvironment.getInstance().getUser()
								.getPrimaryKey());
			}
			if (!loadItemsForInvoiceVOs(proceVOs)) {
				showHintMessage("");
				return;
			}
			timer.addExecutePhase("动态加载表体loadItemsForInvoiceVOs");

			if ((this.m_sZGYF == null)
					|| (!new UFBoolean(this.m_sZGYF).booleanValue())) {
				InvoiceVO invoiceVO = new InvoiceVO();
				for (int i = 0; i < proceVOs.length; ++i) {
					invoiceVO = proceVOs[i];
					Object oTemp = CacheTool.getCellValue("bd_busitype",
							"pk_busitype", "verifyrule", invoiceVO.getHeadVO()
									.getCbiztype());
					if (oTemp != null) {
						Object[] oTemp1 = (Object[]) (Object[]) oTemp;
						if (oTemp1[0].equals("S"))
							continue;
						if (!oTemp1[0].equals("Z")) {
							if (invoiceVO.getHeadVO().getIinvoicetype()
									.intValue() != 3) {
								InvoiceItemVO[] tempBodyVO = invoiceVO
										.getBodyVO();
								for (int j = 0; j < tempBodyVO.length; ++j)
									if (tempBodyVO[j].getCupsourcebilltype() != null) {
										if (((!tempBodyVO[j]
												.getCupsourcebilltype().equals(
														"45")) && (!tempBodyVO[j]
												.getCupsourcebilltype().equals(
														"47")))
												|| (this.m_sOrder2InvoiceSettleMode == null)
												|| (!this.m_sStock2InvoiceSettleMode
														.equals("审批时自动结算"))
												|| (tempBodyVO[j]
														.getNaccumsettmny() == null)
												|| (tempBodyVO[j]
														.getNaccumsettmny()
														.doubleValue() == 0.0D))
											continue;
										MessageDialog
												.showHintDlg(
														this,
														NCLangRes
																.getInstance()
																.getStrByID(
																		"40040401",
																		"UPPSCMCommon-000270"),
														NCLangRes
																.getInstance()
																.getStrByID(
																		"40040401",
																		"UPP40040401-000022"));
										showHintMessage("");
										return;
									}
							}
						}
					}
				}
			}
			PfUtilClient.processBatch(
					"UNAPPROVE"
							+ ClientEnvironment.getInstance().getUser()
									.getPrimaryKey(), "25", ClientEnvironment
							.getInstance().getDate().toString(), proceVOs);

			InvoiceVO resultVO = null;
			if (PfUtilClient.isSuccess()) {
				resultVO = InvoiceHelper
						.findByPrimaryKey(getInvVOs()[getCurVOPos()]
								.getHeadVO().getPrimaryKey());
				getInvVOs()[getCurVOPos()] = resultVO;
			} else if (mapAuditInfo.size() > 0) {
				for (int i = 0; i < proceVOs.length; ++i) {
					if (!mapAuditInfo.containsKey(proceVOs[i].getHeadVO()
							.getPrimaryKey())) {
						proceVOs[i].getHeadVO().setCauditpsn(null);
						proceVOs[i].getHeadVO().setDauditdate(null);
					} else {
						listAuditInfo = (ArrayList) mapAuditInfo
								.get(proceVOs[i].getHeadVO().getPrimaryKey());
						proceVOs[i].getHeadVO().setCauditpsn(
								(String) listAuditInfo.get(0));
						proceVOs[i].getHeadVO().setDauditdate(
								(UFDate) listAuditInfo.get(1));
					}
				}
			}

			timer.addExecutePhase("执行UNAPPROVE脚本");

			if (PfUtilClient.isSuccess()) {
				InvoiceHeaderVO headVO = getInvVOs()[getCurVOPos()].getHeadVO();
				ArrayList arrRet = InvoiceHelper.queryForSaveAudit(headVO
						.getPrimaryKey());
				headVO.setDauditdate(null);
				headVO.setCauditpsn(null);
				headVO.setTaudittime(null);
				headVO.setIbillstatus((Integer) arrRet.get(2));
				headVO.setTs((String) arrRet.get(3));

				timer.addExecutePhase("查询queryForSaveAudit操作");

				setVOToBillPanel();

				this.m_btnInvBillUnAudit.setEnabled(false);

				timer.addExecutePhase("setVOToBillPanel");
				timer.showAllExecutePhase("采购发票弃审结束");
			}
			
			// 查询是否有回冲应付单,如果有则删除，并回写暂估应付回冲标志
			 // 王凯飞
			 // 2014-11-07
			// edit--2015-02-06--wkf
			 
			String pk_corp = ClientEnvironment.getInstance().getCorporation().getPk_corp();
			InvoiceItemVO[] bvoss = (InvoiceItemVO[]) proceVOs[0].getChildrenVO();
			String checktype = bvoss[0].getCupsourcebilltype();
			if(checktype.equals("47")){
				if(("1078".equals(pk_corp))||("1108".equals(pk_corp))){//edit by  mcw
					String invoice = (String) proceVOs[0].getParentVO().getAttributeValue(
							"cinvoiceid");
					DJZBVO[] delete = queryDJZBfromZxy(invoice);
					if(delete.length >0){
						//删除负的暂估应付单
						IArapBillPublic beanfu = (IArapBillPublic) NCLocator.getInstance()
								.lookup(IArapBillPublic.class.getName());
						for (int i = 0; i < delete.length; i++) {
							beanfu.deleteArapBill(delete[i]);
						}
						DJZBVO[] backdj = queryDJfromdel(delete);
						IArapBillPublic uArap = (IArapBillPublic) NCLocator.getInstance().lookup(IArapBillPublic.class.getName());
						for (int i = 0; i < delete.length; i++) {
							DJZBVO updatetmp = new DJZBVO();
							DJZBHeaderVO djheadvo = (DJZBHeaderVO) backdj[i].getParentVO();
//						djheadvo.setZyx28(null);
							
							DJZBItemVO[] djbodyvo = (DJZBItemVO[]) backdj[i].getChildrenVO();
							for (int j = 0; j < djbodyvo.length; j++) {
								djbodyvo[j].setZyx28(null);
							}
							updatetmp.setParentVO(djheadvo);
							updatetmp.setChildrenVO(djbodyvo);
							uArap.editArapBill(updatetmp);
						}
//					//删除存货核算采购入库单回冲单据//2015-04-24制盖业务变更，不用生成存货核算，没有生成回冲，此方法删除--wkf--
//					BillVO[] delete1 = queryBillfromZG(invoice,pk_corp);
//					if(delete1.length>0){
//						//删除存货核算采购入库单回冲单据
//						IBill billim = (IBill) NCLocator.getInstance().lookup(IBill.class.getName());
//						ClientLink cl = new ClientLink(ClientEnvironment.getInstance());
//						billim.deleteArray(cl, delete1, cl.getUser());
//						
//						//还原存货核算采购入库单vdef19->'已暂估且未结算'
//						String sssql = getUpdateSql(delete1,pk_corp);
//						IPubDMO ipubdmo = (IPubDMO)NCLocator.getInstance().lookup(IPubDMO.class.getName());
//						ipubdmo.executeUpdate(sssql);
//					}
						//回写
						backICStatus_wwjg1(delete);
					}
				}
			}
			
			//end 			 
			
		} catch (Exception e) {
			String strErrMsg = e.getMessage();
			if ((strErrMsg != null)
					&& (((strErrMsg.indexOf(NCLangRes.getInstance().getStrByID(
							"40040401", "UPP40040401-000163")) >= 0) || (strErrMsg
							.indexOf(NCLangRes.getInstance().getStrByID(
									"40040401", "UPP40040401-000164")) >= 0)))) {
				strErrMsg = NCLangRes.getInstance().getStrByID("40040401",
						"UPP40040401-000023");
				MessageDialog.showHintDlg(this, NCLangRes.getInstance()
						.getStrByID("40040401", "UPPSCMCommon-000270"),
						strErrMsg);
			} else if (e instanceof BusinessException) {
				MessageDialog.showHintDlg(this, NCLangRes.getInstance()
						.getStrByID("40040401", "UPPSCMCommon-000270"),
						strErrMsg);
			} else {
				strErrMsg = NCLangRes.getInstance().getStrByID("40040401",
						"UPP40040401-000024");
				SCMEnv.out("!!! 以下信息可发给系统管理员参考：\n");
				SCMEnv.out(e);
				MessageDialog.showHintDlg(this, NCLangRes.getInstance()
						.getStrByID("40040401", "UPPSCMCommon-000270"),
						strErrMsg);
			}
			showHintMessage("");
			return;
		} finally {
			if (mapAuditInfo.size() > 0) {
				for (int i = 0; i < proceVOs.length; ++i) {
					if (!mapAuditInfo.containsKey(proceVOs[i].getHeadVO()
							.getPrimaryKey())) {
						proceVOs[i].getHeadVO().setCauditpsn(null);
						proceVOs[i].getHeadVO().setDauditdate(null);
					} else {
						listAuditInfo = (ArrayList) mapAuditInfo
								.get(proceVOs[i].getHeadVO().getPrimaryKey());
						proceVOs[i].getHeadVO().setCauditpsn(
								(String) listAuditInfo.get(0));
						proceVOs[i].getHeadVO().setDauditdate(
								(UFDate) listAuditInfo.get(1));
					}
				}
			}
		}
		showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH011"));
	}
	// start 2014-11-07
	  public BaseDAO dao ;
	  public BaseDAO getBaseDAO(){
		  if(dao==null){
			  dao = new BaseDAO();
 		  }
		  return dao;
	  }
	  //end 
	  
	  //根据查出的回冲单据VO生成update脚本d
	  private String getUpdateSql(BillVO[] delete1,String pk_corp) {
		String tmps = "";
		HashMap hbillcodes = new HashMap<String, String>();
		for (int i = 0; i < delete1.length; i++) {
			BillItemVO[] bvos = (BillItemVO[]) delete1[i].getChildrenVO();
			for (int j = 0; j < bvos.length; j++) {
				String aaa = bvos[j].getCsourcebillitemid();
				tmps +="'"+aaa+"',";
				String vbillcode = bvos[j].getVbillcode();
				if(!hbillcodes.containsKey(vbillcode)){
					hbillcodes.put(vbillcode, vbillcode);
				}
			}
		}
		String vbcss = "";
		Iterator iterator = hbillcodes.keySet().iterator();
		while(iterator.hasNext()) {
			String sqlhs = (String) hbillcodes.get(iterator.next());
			vbcss +="'"+sqlhs+"',";
		}
		vbcss = vbcss.substring(0, vbcss.length()-1);
		tmps = tmps.substring(0,tmps.length()-1);
		StringBuffer sqlsb = new StringBuffer();
		sqlsb.append(" update ia_bill_b ") 
		.append("     set vdef19 = '已暂估且未结算' ") 
		.append("   where nvl(dr, 0) = 0 ") 
		.append("     and pk_corp = '"+pk_corp+"' ") 
		.append("     and nmoney > 0 ") 
		.append("     and vbillcode in ("+tmps+") ") 
		.append("     and csourcebillitemid in ("+tmps+") ") ;

		return sqlsb.toString();
	}
	  
	  
	  /**
	   * 查询是否有回冲存货核算采购入库单生成
	   * 王凯飞
	   * 2015-04-22
	   * */
	  private BillVO[] queryBillfromZG(String invoice,String pk_corp) {
		//1.根据发票id查出应付单主表的id
		  StringBuffer qsql = new StringBuffer();
		  StringBuffer bsql = new StringBuffer();
		  qsql.append(" select distinct h.* from ia_bill h ") 
		  .append("        left join ia_bill_b b ") 
		  .append("        on h.cbillid = b.cbillid ") 
		  .append("        where nvl(b.dr,0)=0  ") 
		  .append("        and nvl(h.dr,0)=0 ") 
		  .append("        and h.cbilltypecode = 'I2' ") 
		  .append("        and csourcemodulename = 'PO' ") 
		  .append("        and b.nmoney <0 ") 
		  .append("        and h.pk_corp = '"+pk_corp+"' ") 
		  .append("        and b.vdef19 = '"+invoice+"' ") ;

		  
		  bsql.append(" select b.* from ia_bill h ") 
		  .append("        left join ia_bill_b b ") 
		  .append("        on h.cbillid = b.cbillid ") 
		  .append("        where nvl(b.dr,0)=0  ") 
		  .append("        and nvl(h.dr,0)=0 ") 
		  .append("        and h.cbilltypecode = 'I2' ") 
		  .append("        and csourcemodulename = 'PO' ") 
		  .append("        and b.nmoney <0 ") 
		  .append("        and h.pk_corp = '"+pk_corp+"' ") 
		  .append("        and b.vdef19 = '"+invoice+"' ") ;

	  	ArrayList headerlist = null;
	  	IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
	  	try {
			headerlist  = (ArrayList) query.executeQuery(qsql.toString(), new BeanListProcessor(BillHeaderVO.class));
		} catch (BusinessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	  	BillVO[] djzbvo =  new BillVO[headerlist.size()];
	  	if(headerlist.size()>0){
	  		for (int j = 0; j < headerlist.size(); j++) {
	  			BillVO itemdjvo = new BillVO();
	  			BillHeaderVO headtmpvo = (BillHeaderVO) headerlist.get(j);
	  			bsql.append("  and h.cbillid ='"+headtmpvo.getCbillid()+"' ");
	  			ArrayList<BillItemVO> itemlist =null;
	  			try {
					itemlist  = (ArrayList<BillItemVO>) query.executeQuery(bsql.toString(), new BeanListProcessor(BillItemVO.class));
				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	  			BillItemVO[] bodyvo = itemlist.toArray(new BillItemVO[itemlist.size()]);
	  			itemdjvo.setParentVO(headtmpvo);
	  			itemdjvo.setChildrenVO(bodyvo);
	  			djzbvo[j]=itemdjvo;
	  		}
	  	}
	  	
	  	return djzbvo;
	}
	  
	  
	 /**
	   * 查询是否有回冲应付单生成
	   * 王凯飞
	   * 2014-11-07
	   * */
	  private DJZBVO[] queryDJZBfromZxy(String invoice) {
		//1.根据发票id查出应付单主表的id
		  StringBuffer qsql = new StringBuffer();
		  StringBuffer bsql = new StringBuffer();
		  qsql.append(" select distinct h.* ")
	      .append("       from arap_djzb h  ")
	      .append("        inner join arap_djfb b ") 
	      .append("        on h.vouchid = b.vouchid ")
	      .append("        where nvl(b.dr,0)=0 ") 
	      .append("        and nvl(h.dr,0)=0 ") 
	     // .append("        and h.bbje > 0 ")
	      .append("        and b.zyx28 = '"+invoice+"' ");
		  
		  bsql.append(" select  b.* ")
	      .append("       from arap_djzb h  ")
	      .append("        inner join arap_djfb b ") 
	      .append("        on h.vouchid = b.vouchid ")
	      .append("        where nvl(b.dr,0)=0 ") 
	      .append("        and nvl(h.dr,0)=0 ") 
	    //  .append("        and h.bbje > 0 ")
	      .append("        and b.zyx28 = '"+invoice+"' ");
	  	ArrayList headerlist = null;
	  	IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
	  	try {
			headerlist  = (ArrayList) query.executeQuery(qsql.toString(), new BeanListProcessor(DJZBHeaderVO.class));
		} catch (BusinessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	  	DJZBVO[] djzbvo =  new DJZBVO[headerlist.size()];
	  	if(headerlist.size()>0){
	  		for (int j = 0; j < headerlist.size(); j++) {
	  			DJZBVO itemdjvo = new DJZBVO();
	  			DJZBHeaderVO headtmpvo = (DJZBHeaderVO) headerlist.get(j);
	  			bsql.append("  and h.vouchid='"+headtmpvo.getVouchid()+"' ");
	  			ArrayList<DJZBItemVO> itemlist =null;
	  			try {
					itemlist  = (ArrayList<DJZBItemVO>) query.executeQuery(bsql.toString(), new BeanListProcessor(DJZBItemVO.class));
				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	  			DJZBItemVO[] bodyvo = itemlist.toArray(new DJZBItemVO[itemlist.size()]);
	  			itemdjvo.setParentVO(headtmpvo);
	  			itemdjvo.setChildrenVO(bodyvo);
	  			djzbvo[j]=itemdjvo;
	  		}
	  	}
	  	
	  	return djzbvo;
	}
	 
	  /**
	   * 根据回冲vo，查得要回冲的VO
	   * 王凯飞
	   * 2014-11-07
	   * */
	  private DJZBVO[] queryDJfromdel(DJZBVO[] djzbvo) {
//		  HashMap<String, String> tmpmap = new HashMap<String, String>();
		  String headsqltmp = ""; 
		  for (int i = 0; i < djzbvo.length; i++) {
			  DJZBItemVO[] bodytmpvo = (DJZBItemVO[]) djzbvo[i].getChildrenVO();
			  for (int j = 0; j < bodytmpvo.length; j++) {
				  String ddhh = bodytmpvo[j].getDdhh();
				  headsqltmp+="'"+ddhh+"',";
			  }
		  }
		  headsqltmp = headsqltmp.substring(0, headsqltmp.length()-1);//去掉最后一个逗号
		  StringBuffer headsql = new StringBuffer();
		  headsql.append(" select distinct zb.* from arap_djzb zb ") 
		  .append("        left join arap_djfb fb ") 
		  .append("        on zb.vouchid = fb.vouchid ") 
		  .append("        where nvl(zb.dr,0)=0  ") 
		  .append("        and nvl(fb.dr,0)=0 ") 
		  .append("        and bbje>0  ") 
		  .append("        and fb.ddhh in ("+headsqltmp+") ") ;
		  
		  StringBuffer bodysql = new StringBuffer();
		  bodysql.append(" select distinct fb.* from arap_djzb zb ") 
		  .append("        left join arap_djfb fb ") 
		  .append("        on zb.vouchid = fb.vouchid ") 
		  .append("        where nvl(zb.dr,0)=0  ") 
		  .append("        and nvl(fb.dr,0)=0 ") 
		  .append("        and bbje>0  ") 
		  .append("        and fb.ddhh in ("+headsqltmp+") ") ;
//		  String bodysql=" select * from  arap_djfb where nvl(dr,0)=0 ";
		  ArrayList headerlist = null;
		  IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		  try {
			headerlist  = (ArrayList) query.executeQuery(headsql.toString(), new BeanListProcessor(DJZBHeaderVO.class));
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  	DJZBVO[] djvoaa =  new DJZBVO[headerlist.size()];
		  	if(headerlist.size()>0){
		  		for (int j = 0; j < headerlist.size(); j++) {
		  			DJZBVO itemdjvo = new DJZBVO();
		  			DJZBHeaderVO headtmpvo = (DJZBHeaderVO) headerlist.get(j);
		  			bodysql.append("  and fb.vouchid='"+headtmpvo.getVouchid()+"'  ");
		  			ArrayList<DJZBItemVO> itemlist =null;
		  			try {
						itemlist  = (ArrayList<DJZBItemVO>) query.executeQuery(bodysql.toString(), new BeanListProcessor(DJZBItemVO.class));
					} catch (BusinessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		  			DJZBItemVO[] bodyvo = itemlist.toArray(new DJZBItemVO[itemlist.size()]);
		  			itemdjvo.setParentVO(headtmpvo);
		  			itemdjvo.setChildrenVO(bodyvo);
		  			djvoaa[j]=itemdjvo;
		  		}
		  	}
		  return djvoaa;
	  }
	  
	  
	public void onButtonClicked(ButtonObject btn) {
		if(btn.getName().equals("自制单据")||btn.getName().equals("库存采购入库")){
			this.getButtonObjectByCode("签呈抵扣").setEnabled(true);
		}else{
			this.getButtonObjectByCode("签呈抵扣").setEnabled(false);
		}
		if(btn.getName().equals("签呈抵扣")){
			List <Map> mapresult = new ArrayList<Map>();
	    	List list = new ArrayList();
	    	list = this.onQueryQC();
			BankBatEditDlg bank = new BankBatEditDlg();
			bank.setReturnValue(list);
			bank.showModal();
			mapresult = bank.connEtoC1();
			onQCassign(mapresult);
		}
		if (getCurPanelMode() == 0)
			onButtonClickedBill(btn);
		else if (getCurPanelMode() == 1) {
			onButtonClickedList(btn);
		}

		setButtonsAndPanelState();

		updateButtons();

		if (this.m_isMakedBill)
			getInvBillPanel().transferFocusTo(0);
	}
	/**
	 * 获取抵扣签呈
	 * @return
	 */
	public List onQueryQC(){
		InvoiceVO retVO = (InvoiceVO) this.getInvBillPanel().getBillData().getBillValueVO(InvoiceVO.class.getName(), InvoiceHeaderVO.class.getName(), InvoiceItemVO.class.getName());
		//获取公司编码
		String pk_corp = (String) retVO.getParentVO().getAttributeValue("pk_corp");
		//获取客商id
		String custcode = (String) retVO.getParentVO().getAttributeValue("cvendorbaseid");
		//获取主键
		String pk = new String();
		try {
			 pk = retVO.getParentVO().getPrimaryKey();
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		String result = this.contorService(custcode, pk_corp,pk);
		JSONArray jsonarray = new JSONArray();
		jsonarray = new JSONArray().fromObject(result);
		JSONObject jsonObject = new JSONObject();
		Map mls = new HashMap();
		List listMap = new ArrayList();
		for (int j = 0; j < jsonarray.size(); j++) {
			try {
				jsonObject = jsonarray.getJSONObject(j);
				Iterator iterator = jsonObject.keys();
				String key = null;
				String value = null;
				while (iterator.hasNext()) {
					key = (String) iterator.next();
					value = jsonObject.getString(key);
					if (key.equals("bodylist")) {
						JSONArray jsonArray = jsonObject.getJSONArray(key);
						for (int i = 0; i < jsonArray.size(); i++) {
							String key1 = new String();
							String value1 = new String();
							JSONObject jsonOb = jsonArray.getJSONObject(i);
							Map map1 = new HashMap();
							Iterator iter = jsonOb.keys();
							while (iter.hasNext()) {
								key1 = (String) iter.next();
								value1 = jsonOb.getString(key1);
								map1.put(key1, value1);
							}
							listMap.add(map1);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return listMap;	
	}
	/**
	 * 签呈分配 
	 * @return
	 */
	public void onQCassign(List list){
		if(list.size()>0){
			InvoiceVO retVO = (InvoiceVO) this.getInvBillPanel().getBillValueVO(InvoiceVO.class.getName(), InvoiceHeaderVO.class.getName(), InvoiceItemVO.class.getName());
			InvoiceItemVO[] bvo = retVO.getBodyVO();
			Map map = new HashMap();
			UFDouble sjdkje = new UFDouble(0);
			for(int a =0;a<list.size();a++){
				map = (Map) list.get(a);
				//实际抵扣金额
				String sjdk = map.get("sjdkje")==null?"0": map.get("sjdkje").toString();//30
				sjdkje = new UFDouble(sjdk).add(sjdkje);
				String qc = map.get("qcje")==null?"0":map.get("qcje").toString();
				String sj = map.get("sjdkje")==null?"0":map.get("sjdkje").toString();
				UFDouble qc1 = new UFDouble(qc);
				UFDouble sj1 = new UFDouble(sj);
				if(qc1.sub(sj1).doubleValue()<0){
					return;
				}
			}
			if(bvo.length>1){
				for(int i =0;i<bvo.length;i++){
					//存放cvm签呈数据
					getInvBillPanel().setBodyValueAt(list.toString(), i, "b_cjje1");
					bvo[i].setB_cjje1(String.valueOf(list.toString()));
					UFDouble noriginalsummny = bvo[i].getNoriginalsummny();
					if(sjdkje.sub(noriginalsummny).doubleValue()>=0){
						getInvBillPanel().setBodyValueAt(noriginalsummny, i, "b_cjje3");
						bvo[i].setB_cjje3(String.valueOf(noriginalsummny));
						sjdkje = sjdkje.sub(noriginalsummny);
					}else{
						if(sjdkje.sub(noriginalsummny).doubleValue()<0){
							getInvBillPanel().setBodyValueAt(String.valueOf(sjdkje), i, "b_cjje3");
							bvo[i].setB_cjje3(String.valueOf(sjdkje));
							sjdkje = sjdkje.sub(sjdkje);
					}
				}	
			}
		}else{
			for(int i =0;i<bvo.length;i++){
				getInvBillPanel().setBodyValueAt(list.toString(), i, "b_cjje1");
				bvo[i].setB_cjje1(String.valueOf(list.toString()));
				UFDouble noriginalsummny = bvo[i].getNoriginalsummny();
				if(sjdkje.sub(noriginalsummny).doubleValue()>=0){
					MessageDialog.showErrorDlg(this, "错误提示","所选择签呈金额不能大于发票总金额!");
				}else{
					getInvBillPanel().setBodyValueAt(String.valueOf(sjdkje), i, "b_cjje3");
					bvo[i].setB_cjje3(String.valueOf(sjdkje));
				}
			}
		}
	}
}
	
	
	private InvoiceVO[] processAfterChange(String busiType,
			AggregatedValueObject[] arySourceVOs) {
		InvoiceVO[] retVOs = getDistributedICVMISumVOs(arySourceVOs);
		return retVOs;
	}

	private InvoiceVO[] getDistributedICVMISumVOs(
			AggregatedValueObject[] arySourceVOs) {
		Hashtable ordTable = new Hashtable();

		int SourceVOsLength = arySourceVOs.length;
		VmiSumHeaderVO headVO = null;
		for (int i = 0; i < SourceVOsLength; ++i) {
			headVO = (VmiSumHeaderVO) arySourceVOs[i].getParentVO();

			String curKey = ((headVO.getCvendorid() == null) || (headVO
					.getCvendorid().trim().equals(""))) ? "NULL" : headVO
					.getCvendorid();

			String isplitmode = ((headVO.getAttributeValue("isplitmode") == null) || (headVO
					.getAttributeValue("isplitmode").toString().trim()
					.equals(""))) ? curKey : headVO.getAttributeValue(
					"isplitmode").toString().trim();

			if (isplitmode.trim().equals("1"))
				curKey = curKey + headVO.getCinventoryid();
			else if (isplitmode.trim().equals("2")) {
				curKey = headVO.getPrimaryKey();
			}

			if (!ordTable.containsKey(curKey)) {
				Vector vec = new Vector();
				vec.addElement(headVO);
				ordTable.put(curKey, vec);
			} else {
				Vector vec = (Vector) ordTable.get(curKey);
				vec.addElement(headVO);
			}

		}

		InvoiceVO[] allVOs = null;
		InvoiceHeaderVO invoiceHeadVO = null;
		InvoiceItemVO[] items = null;
		if (ordTable.size() > 0) {
			allVOs = new InvoiceVO[ordTable.size()];
			Enumeration elems = ordTable.keys();
			int i = 0;
			ClientLink cl = new ClientLink(ClientEnvironment.getInstance());
			while (elems.hasMoreElements()) {
				Object curKey = elems.nextElement();
				Vector vec = (Vector) ordTable.get(curKey);

				VmiSumHeaderVO[] headVOs = new VmiSumHeaderVO[vec.size()];
				vec.copyInto(headVOs);

				int headVOsLength = headVOs.length;

				invoiceHeadVO = new InvoiceHeaderVO();

				invoiceHeadVO.setAttributeValue("dinvoicedate", cl
						.getLogonDate());

				invoiceHeadVO.setAttributeValue("darrivedate", cl
						.getLogonDate());

				invoiceHeadVO.setAttributeValue("pk_corp", ((headVOs[0]
						.getAttributeValue("pk_corp") == null) || (headVOs[0]
						.getAttributeValue("pk_corp").toString().trim()
						.equals(""))) ? "" : headVOs[0].getAttributeValue(
						"pk_corp").toString().trim());

				invoiceHeadVO.setAttributeValue("finitfalg", new Integer(0));

				invoiceHeadVO.setAttributeValue("cbiztype", this.m_strBtnTag);

				invoiceHeadVO.setAttributeValue("iinvoicetype", new Integer(0));

				invoiceHeadVO.setAttributeValue("cstoreorganization",
						headVOs[0].getCcalbodyid());

				invoiceHeadVO.setAttributeValue("cvendormangid", headVOs[0]
						.getCvendorid());

				invoiceHeadVO.setAttributeValue("cvendorbaseid", headVOs[0]
						.getAttributeValue("cvendorbasid"));

				invoiceHeadVO.setAttributeValue("cpayunit", headVOs[0]
						.getCvendorid());

				setDefaultBankAccountForAVendor(invoiceHeadVO
						.getCvendorbaseid(), invoiceHeadVO);

				String payTermId = (String) PiPqPublicUIClass
						.getAResultFromTable("bd_cumandoc", "pk_payterm",
								"pk_cumandoc", invoiceHeadVO.getCvendormangid());
				invoiceHeadVO.setAttributeValue("ctermprotocolid", payTermId);

				String cemployeeid = (String) PiPqPublicUIClass
						.getAResultFromTable("bd_cumandoc", "pk_resppsn1",
								"pk_cumandoc", invoiceHeadVO.getCvendormangid());
				invoiceHeadVO.setAttributeValue("cemployeeid", cemployeeid);

				String cdeptid = (String) PiPqPublicUIClass
						.getAResultFromTable("bd_cumandoc", "pk_respdept1",
								"pk_cumandoc", invoiceHeadVO.getCvendormangid());
				invoiceHeadVO.setAttributeValue("cdeptid", cdeptid);

				String ccurrencytypeid = (String) PiPqPublicUIClass
						.getAResultFromTable("bd_cumandoc", "pk_currtype1",
								"pk_cumandoc", invoiceHeadVO.getCvendormangid());
				invoiceHeadVO.setAttributeValue("ccurrencytypeid",
						ccurrencytypeid);

				invoiceHeadVO.setAttributeValue("coperator", cl.getUser());

				invoiceHeadVO.setIbillstatus(new Integer(0));

				items = new InvoiceItemVO[headVOsLength];
				for (int k = 0; k < headVOsLength; ++k) {
					items[k] = new InvoiceItemVO();
				}

				for (int j = 0; j < headVOsLength; ++j) {
					items[j]
							.setCbaseid(((headVOs[j]
									.getAttributeValue("cinvbasdocid") == null) || (headVOs[j]
									.getAttributeValue("cinvbasdocid")
									.toString().trim().equals(""))) ? ""
									: headVOs[j].getAttributeValue(
											"cinvbasdocid").toString().trim());

					items[j]
							.setCmangid(((headVOs[j]
									.getAttributeValue("cinventoryid") == null) || (headVOs[j]
									.getAttributeValue("cinventoryid")
									.toString().trim().equals(""))) ? ""
									: headVOs[j].getAttributeValue(
											"cinventoryid").toString().trim());

					items[j]
							.setNinvoicenum(((headVOs[j]
									.getAttributeValue("ninvoicenum") == null) || (headVOs[j]
									.getAttributeValue("ninvoicenum")
									.toString().trim().equals(""))) ? new UFDouble(
									0)
									: new UFDouble(headVOs[j]
											.getAttributeValue("ninvoicenum")
											.toString().trim()));

					items[j].setCcurrencytypeid(ccurrencytypeid);

					UFDouble[] daRate = null;
					String strCurrDate = invoiceHeadVO.getAttributeValue(
							"dinvoicedate").toString();
					if ((strCurrDate == null)
							|| (strCurrDate.trim().length() == 0)) {
						strCurrDate = PoPublicUIClass.getLoginDate() + "";
					}
					daRate = this.m_cardPoPubSetUI2.getBothExchRateValue(
							getPk_corp(), ccurrencytypeid, new UFDate(
									strCurrDate));
					items[j].setNexchangeotobrate(daRate[0]);
					items[j].setNexchangeotoarate(daRate[1]);

					items[j].setCsourcebilltype("50");

					items[j].setCsourcebillid(headVOs[j].getPrimaryKey());

					items[j].setCsourcebillrowid(null);

					items[j].setCupsourcebilltype("50");

					items[j].setCupsourcebillid(headVOs[j].getPrimaryKey());

					items[j].setCupsourcebillrowid(null);

					items[j].setCupsourcehts(headVOs[j].getTs());
					items[j].setCupsourcebts(headVOs[j].getTs());
				}

				allVOs[i] = new InvoiceVO();
				allVOs[i].setParentVO(invoiceHeadVO);
				allVOs[i].setChildrenVO(items);
				allVOs[i].setSource(3);

				BillRowNo.setVORowNoByRule(allVOs[i], "25", "crowno");

				String sCstoreorganization = allVOs[i].getHeadVO()
						.getCstoreorganization();
				int size = allVOs[i].getChildrenVO().length;
				String[] sMangIds = new String[size];
				for (int p = 0; p < size; ++p) {
					sMangIds[p] = items[p].getCmangid();
				}

				UFDouble[] uPrice = queryPlanPrices(sMangIds,
						sCstoreorganization);
				if (uPrice != null) {
					for (int p = 0; p < size; ++p) {
						items[p].setNplanprice(uPrice[p]);
					}
				}
				++i;
			}
		}
		return allVOs;
	}

	private void setDefaultBankAccountForAVendor(String strVendorBase,
			InvoiceHeaderVO invoiceHeadVO) {
		if ((strVendorBase == null) || (strVendorBase.trim().equals(""))) {
			return;
		}

		Object[][] retOb = CacheTool.getMultiColValue("bd_custbank",
				"pk_cubasdoc", new String[] { "pk_custbank", "account",
						"defflag" }, new String[] { strVendorBase });
		Vector v1 = new Vector();
		Vector v2 = new Vector();
		if ((retOb != null) && (retOb.length > 0) && (retOb[0] != null)
				&& (retOb[0].length > 1)) {
			for (int i = 0; i < retOb.length; ++i) {
				if ((retOb[i][2] != null) && (retOb[i][2].equals("Y"))) {
					v1.add(retOb[i][0]);
					v2.add(retOb[i][1]);
				}
			}

		}

		UIRefPane pane = (UIRefPane) getInvBillPanel().getHeadItem(
				"caccountbankid").getComponent();
		((AccountsForVendorRefModel) pane.getRef().getRefModel())
				.setCvendorbaseid(strVendorBase);

		if (v1.size() == 0) {
			invoiceHeadVO.setAttributeValue("caccountbankid", null);

			invoiceHeadVO.setAttributeValue("cvendoraccount", null);
		} else {
			invoiceHeadVO.setAttributeValue("caccountbankid", (String) v1
					.elementAt(0));

			invoiceHeadVO.setAttributeValue("cvendoraccount", (String) v1
					.elementAt(0));
		}
	}

	//add by danxionghui 2013/12/16 No 17
	// 批量修改
	public void onBatchEdit(){
		InvoiceItemVO selVo = null;
		int selRow =  getInvBillPanel().getBillTable().getSelectedRow();
		int selCol = getInvBillPanel().getBillTable().getSelectedColumn();
		if(selRow<0){
			 MessageDialog.showHintDlg(this, null, "请选择表体行");
			 return;
		}
		String colKey = getInvBillPanel().getBillModel().getBodyKeyByCol(selCol);
		int rowCount = getInvBillPanel().getBillModel().getRowCount();
		if(rowCount <= 1) return;
		Object srcObj = getInvBillPanel().getBillModel().getValueAt(selRow, colKey);
		for (int i = 0; i < rowCount; i++) {
			if(i == selRow) continue;
			getInvBillPanel().setBodyValueAt(srcObj, i, colKey);
			afterEdit(new BillEditEvent(getInvBillPanel().getBillModel().getItemByKey(colKey),srcObj,colKey,i,1));
		}
	}
	
	// 按料号批改
	public void onAllEdit(){
		InvoiceItemVO selVo = null;
		int selRow =  getInvBillPanel().getBillTable().getSelectedRow();
		int selCol = getInvBillPanel().getBillTable().getSelectedColumn();
		if(selRow<0){
			 MessageDialog.showHintDlg(this, null, "请选择表体行");
			 return;
		}
		String colKey = getInvBillPanel().getBillModel().getBodyKeyByCol(selCol);
		int rowCount = getInvBillPanel().getBillModel().getRowCount();
		if(rowCount <= 1) return;
		Object srcObj = getInvBillPanel().getBillModel().getValueAt(selRow, colKey);
		String pk_invbasdoc= (String) getInvBillPanel().getBillModel().getValueAt(selRow, "cbaseid");
		for (int i = 0; i < rowCount; i++) {
			if(i == selRow) continue;
			String currRowInvbasdoc = (String) getInvBillPanel().getBillModel().getValueAt(i, "cbaseid");
			if(currRowInvbasdoc == null || !pk_invbasdoc.equals(currRowInvbasdoc)) continue;
			getInvBillPanel().setBodyValueAt(srcObj, i, colKey);
			afterEdit(new BillEditEvent(getInvBillPanel().getBillModel().getItemByKey(colKey),srcObj,colKey,i,1));
		}
	}
	// end
	
	private void onButtonClickedBill(ButtonObject btn) {
		//add by danxionghui
		if(btn == this.m_btnPiGai){
			onBatchEdit();
		}//end
		//按料号修改
		if(btn == this.m_btnAllGai){
			onAllEdit();
		}

		// add by cm start
		if (btn == this.m_btnZiNumW) {
			if (getInvBillPanel().getBillModel().getRowCount() <= 0) {
				showErrorMessage("表体无数据，请增行！！");
				return;
			}
			String cvendormangid = getInvBillPanel().getHeadItem(
					"cvendormangid").getValueObject() == null ? ""
					: getInvBillPanel().getHeadItem("cvendormangid")
							.getValueObject().toString();// 供 应 商
			if (cvendormangid.equals("")) {
				showErrorMessage("供应商为空，不能取数，请输入供应商！！");
				return;
			}
			// IIcbItf bo =
			// (IIcbItf)NCLocator.getInstance().lookup(IIcbItf.class.getName());
			// (dm_baseprice.pk_transcust = '1016AA1000000000XSTX') and--承运商主键
			// (dm_baseprice.pk_sendtype = '1016A21000000000WLS9') and--发运方式主键
			// (dm_baseprice.pkfromaddress = '1016AA1000000000XSTS') and--发货地点主键
			// (dm_baseprice.pktoaddress = '1016AA1000000000XSTT') and--到货地点主键
			String where = " nvl(dr,0)=0 and pk_transcust = '1016AA1000000000XSTX' and pk_sendtype = '1016A21000000000WLS9' and pkfromaddress = '1016AA1000000000XSTS' and pktoaddress = '1016AA1000000000XSTT' ";
			String taxprice = null;
			try {
				taxprice = FetchValueBO_Client.getColValue("dm_baseprice",
						"taxprice", where);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			UFDouble price = new UFDouble(taxprice);
			getInvBillPanel().setBodyValueAt(price, 0, "noriginalcurprice");// 单价
			BillEditEvent event = new BillEditEvent(getInvBillPanel()
					.getBillTable(), price, "noriginalcurprice", 0);
			afterEditInvBillRelations(event);

			// FormulaParseFather f = new nc.ui.pub.formulaparse.FormulaParse();
			// String[] formulas = new String[]
			// {
			// "viewmny1->viewnum*viewprice-rate*0.23 ",
			// "viewmny2-> viewnum*viewprice-rate*0.24"
			// };
			// f.setExpressArray(formulas);
			// getInvBillPanel().getBillModel().execLoadFormula();
		} else if (btn == this.m_btnZiLaoW) {// add by cm
			// zifalg = 1;
			if (getInvBillPanel().getBillModel().getRowCount() <= 0) {
				showErrorMessage("表体无数据，请增行！！");
				return;
			}
//			PfUtilClient.childButtonClicked1(btn, ClientEnvironment
//					.getInstance().getCorporation().getPk_corp(),
//					getModuleCode(), ClientEnvironment.getInstance().getUser()
//							.getPrimaryKey(), "25", this);
			PfUtilClient.childButtonClicked(btn, ClientEnvironment
					.getInstance().getCorporation().getPk_corp(),
					getModuleCode(), ClientEnvironment.getInstance().getUser()
							.getPrimaryKey(), "25", this);
			if (PfUtilClient.isCloseOK()) {
				String sbid = InvFrmStoUI1.getStr_bid();
				String[] sbids = sbid.split(",");
				getInvBillPanel()
						.setBodyValueAt(sbids.length, 0, "ninvoicenum");// 数量
				// getInvBillPanel().setBodyValueAt(new UFDouble(9), 0,
				// "ninvoicenum");//数量
				BillEditEvent event = new BillEditEvent(getInvBillPanel()
						.getBillTable(), new UFDouble(sbids.length),
						"ninvoicenum", 0);
				afterEditInvBillRelations(event);
				// getInvBillPanel().setBodyValueAt(InvFrmStoUI1.getStr_bid(),
				// 0, "");

			}
		} else
		// end by cm
		if (btn.getParent() == this.m_btnInvBillBusiType) {
			showHintMessage(NCLangRes.getInstance().getStrByID("40040401",
					"UPPSCMCommon-000293"));

			setCurOperState(2);

			setCurBizeType(btn.getTag());
			this.m_strBtnTag = btn.getTag();

			StringBuffer sbufHint = new StringBuffer("");
			setHeadHintText(sbufHint.toString());

			PfUtilClient.retAddBtn(this.m_btnInvBillNew, ClientEnvironment
					.getInstance().getCorporation().getPrimaryKey(), "25", btn);

			btn.setSelected(true);
			this.m_btnInvBillBusiType.setEnabled(true);

			setButtons(this.m_btnTree.getButtonArray());

			this.m_bizButton = btn;
			updateButtons();
		} else if (btn.getParent() == this.m_btnInvBillNew) {
			showHintMessage(getHeadHintText()
					+ NCLangRes.getInstance().getStrByID("common", "UCH028"));

			if (btn.getName().equals(
					NCLangRes.getInstance().getStrByID("SCMCOMMON",
							"UPPSCMCommon-000341"))) {
				setCurOperState(1);
				onAdd();

				this.m_isMakedBill = true;

				getInvBillPanel().getHeadItem("pk_purcorp").setValue(
						getPk_corp());
			} else {
				String tag = btn.getTag();
				int index = tag.indexOf(":");
				String strUpBillType = tag.substring(0, index);

				if ("50".equalsIgnoreCase(strUpBillType)) {
					PfUtilClient.childButtonClicked(btn, getCorpPrimaryKey(),
							"400404", ClientEnvironment.getInstance().getUser()
									.getPrimaryKey(), "25", this);
				} else {
					PfUtilClient.childButtonClicked(btn, ClientEnvironment
							.getInstance().getCorporation().getPk_corp(),
							getModuleCode(), ClientEnvironment.getInstance()
									.getUser().getPrimaryKey(), "25", this);
				}

				if (PfUtilClient.isCloseOK()) {
					InvoiceVO[] vos = null;
					if ("50".equalsIgnoreCase(strUpBillType)) {
						AggregatedValueObject[] arySourceVOs = PfUtilClient
								.getRetVos();
						vos = processAfterChange("50", arySourceVOs);
						for (int i = 0; i < vos.length; ++i) {
							if (PuPubVO.getString_TrimZeroLenAsNull(vos[i]
									.getHeadVO().getPk_purcorp()) == null)
								vos[i].getHeadVO().setPk_purcorp(getPk_corp());
						}
						/**
						 * 供应商寄存采购(VMI)业务中，在依据【消耗汇总表】生成采购发票时，
						 * 【采购发票】的单价默认在该业务所在会计期间内，在采购合同有效期内， 依据“相同存货、相同供应商
						 * “2个条件取《采购合同》中的单价。 add by xiaolong_fan.2012-12-28.
						 */
						if (getParentCorpCode1().equals("10395")) {
							if (arySourceVOs != null
									&& arySourceVOs instanceof VmiSumVO[]) {
								SimpleDateFormat sf = new SimpleDateFormat(
										"yyyy-MM-dd");
								for (int i = 0; i < vos.length; i++) {
									InvoiceVO tempvo = vos[i];
									// 票到日期
									UFDate arrivedate = tempvo.getHeadVO()
											.getDarrivedate();
									String dfDate = sf.format(arrivedate
											.toDate());
									// 供应商管理档案ID
									String vendorbaseid = tempvo.getHeadVO()
											.getCvendormangid();
									InvoiceItemVO[] itemvos = (InvoiceItemVO[]) tempvo
											.getChildrenVO();
									for (int j = 0; j < itemvos.length; j++) {
										InvoiceItemVO tempitemvo = itemvos[j];
										// 存货管理ID
										String cbaseid = tempitemvo
												.getCmangid();
										// 查原币无税单价
										String oritaxpriceSql = "select a.currid as currid,b.oriprice as oriprice,b.oritaxprice as oritaxprice,b.taxration as taxration  " +
												" from ct_manage a left outer join ct_manage_b b on a.pk_ct_manage= b.pk_ct_manage " +
												" where a.custid=? and b.invid=? " +
												" and to_date(?,'yyyy-MM-dd')>=to_date(a.valdate,'yyyy-MM-dd') " +
												" and to_date(?,'yyyy-MM-dd')<=to_date(a.invallidate,'yyyy-MM-dd') " +
												" and nvl(a.ctflag, 0) = 2 ";//add by shikun 2014-04-17 合同生效态
										try {
											SQLParameter param = new SQLParameter();
											param.addParam(vendorbaseid);
											param.addParam(cbaseid);
											param.addParam(dfDate);
											param.addParam(dfDate);
											Object obj = getUAPQuery()
													.executeQuery(
															oritaxpriceSql,
															param,
															new MapProcessor());
											if (obj != null
													&& obj instanceof HashMap) {
												HashMap map = (HashMap) obj;
												tempvo
														.getHeadVO()
														.setCcurrencytypeid(
																String
																		.valueOf(map
																				.get("currid")));
												// 税率
												tempitemvo
														.setNtaxrate(new UFDouble(
																String
																		.valueOf(map
																				.get("taxration"))));
												// 币别
												tempitemvo
														.setCcurrencytypeid(String
																.valueOf(map
																		.get("currid")));
												// 无税单价
												UFDouble oriprice = new UFDouble(
														String
																.valueOf(map
																		.get("oriprice")));
												tempitemvo
														.setNoriginalcurprice(oriprice);
												// 数量
												UFDouble ninvoicenum = tempitemvo
														.getNinvoicenum();
												// 金额
												UFDouble noriginalcurmny = ninvoicenum
														.multiply(oriprice);
												tempitemvo
														.setNoriginalcurmny(ninvoicenum
																.multiply(oriprice));
												// 含税单价
												UFDouble oritaxprice = new UFDouble(
														String
																.valueOf(map
																		.get("oritaxprice")));
												tempitemvo
														.setNorgnettaxprice(new UFDouble(
																String
																		.valueOf(map
																				.get("oritaxprice"))));
												// 价税合计
												UFDouble noriginalsummny = ninvoicenum
														.multiply(oritaxprice);
												tempitemvo
														.setNoriginalsummny(ninvoicenum
																.multiply(oritaxprice));
												// 税额
												tempitemvo
														.setNoriginaltaxmny(noriginalsummny
																.sub(noriginalcurmny));
											}
										} catch (FTSBusinessException e) {
											e.printStackTrace();
										} catch (BusinessException e) {
											e.printStackTrace();
										}
									}
								}
							}
						}
						// end by xiaolong_fan.
					} else {
						vos = (InvoiceVO[]) (InvoiceVO[]) PfUtilClient
								.getRetVos();
						if (vos == null) {
							MessageDialog.showHintDlg(this, NCLangRes
									.getInstance().getStrByID("40040401",
											"UPPSCMCommon-000270"), NCLangRes
									.getInstance().getStrByID("40040401",
											"UPP40040401-000003"));
							return;
						}
						BillItem item = getInvListPanel().getBodyBillModel()
								.getItemByKey("nplanprice");
						for (int i = 0; i < vos.length; ++i) {
							if (PuPubVO.getString_TrimZeroLenAsNull(vos[i]
									.getHeadVO().getPk_purcorp()) == null) {
								vos[i].getHeadVO().setPk_purcorp(getPk_corp());
							}

							BillRowNo.setVORowNoByRule(vos[i], "25", "crowno");
							setHeadCurrency(vos[i]);
							try {
								InvoiceItemVO[] items = vos[i].getBodyVO();

								computeValueFrmOtherBill(vos[i]);

								String sCvendormangid = vos[i].getHeadVO()
										.getCvendormangid();

								String payTermId = vos[i].getHeadVO()
										.getCtermprotocolid();
								if ((payTermId == null)
										|| (payTermId.trim().length() < 1)) {
									Object oTemp = CacheTool.getCellValue(
											"bd_cumandoc", "pk_cumandoc",
											"pk_payterm", sCvendormangid);
									if (oTemp != null)
										vos[i].getHeadVO().setCtermprotocolid(
												oTemp.toString());
								}

								String sPayUnit = vos[i].getHeadVO()
										.getCpayunit();
								if ((sPayUnit == null)
										|| (sPayUnit.trim().length() == 0)) {
									vos[i].getHeadVO().setCpayunit(
											sCvendormangid);
								}

								if ((item != null) && (item.isShow())) {
									String sCstoreorganization = vos[i]
											.getHeadVO()
											.getCstoreorganization();
									int size = items.length;
									String[] sMangIds = new String[size];
									for (int j = 0; j < size; ++j) {
										sMangIds[j] = items[j].getCmangid();
									}
									UFDouble[] uPrice = queryPlanPrices(
											sMangIds, sCstoreorganization);
									if (uPrice != null)
										for (int j = 0; j < size; ++j)
											items[j].setNplanprice(uPrice[j]);
								}
							} catch (Exception ex) {
								reportException(ex);
								PuTool.outException(this, ex);
							}
						}

					}

					setCurVOPos(0);
					setInvVOs(vos);

					setListOperState(5);

					setCurOperState(getListOperState());
					shiftShowModeTo(1);

					setVOsToListPanel();

					this.m_vSavedVO = new Vector();
				}
			}

		} else if (btn == this.m_btnInvBillModify) {
			showHintMessage(getHeadHintText()
					+ NCLangRes.getInstance().getStrByID("common", "UCH027"));
			setCurOperState(1);
			onModify();

			this.m_btnInvBillAudit.setEnabled(false);
			updateButtons();

			getInvBillPanel().transferFocusTo(0);
		} else if (btn == this.m_btnInvBillSave) {
					boolean bSucceed = onSave();
					if (bSucceed)
						showHintMessage(NCLangRes.getInstance().getStrByID("common",
								"UCH005"));
					else {
						showHintMessage("");
					}

		}else if (btn == this.m_btnInvBillDiscard) {
			showHintMessage(getHeadHintText()
					+ NCLangRes.getInstance().getStrByID("common", "UCH051"));

			onDiscard();
		} else if (btn == this.m_btnInvBillCancel) {
			showHintMessage(getHeadHintText()
					+ NCLangRes.getInstance().getStrByID("common", "UCH045"));

			onBillCancel();
			showHintMessage(NCLangRes.getInstance().getStrByID("common",
					"UCH008"));
		} else if (btn == this.m_btnInvBillAddRow) {
			showHintMessage(NCLangRes.getInstance().getStrByID("common",
					"UCH056"));
			onAppendLine();
			showHintMessage(NCLangRes.getInstance().getStrByID("common",
					"UCH036"));
		} else if (btn == this.m_btnInvBillDeleteRow) {
			showHintMessage(NCLangRes.getInstance().getStrByID("common",
					"UCH057"));
			onDeleteLine();
			showHintMessage(NCLangRes.getInstance().getStrByID("common",
					"UCH037"));
		} else if (btn == this.m_btnInvBillInsertRow) {
			showHintMessage(NCLangRes.getInstance().getStrByID("common",
					"UCH058"));
			onInsertLine();
			showHintMessage(NCLangRes.getInstance().getStrByID("common",
					"UCH038"));
		} else if (btn == this.m_btnInvBillCopyRow) {
			showHintMessage(NCLangRes.getInstance().getStrByID("common",
					"UCH059"));
			onCopyLine();
			showHintMessage(NCLangRes.getInstance().getStrByID("common",
					"UCH039"));
		} else if (btn == this.m_btnInvBillPasteRow) {
			showHintMessage(NCLangRes.getInstance().getStrByID("common",
					"UCH060"));
			onPasteLine();
			showHintMessage(NCLangRes.getInstance().getStrByID("common",
					"UCH040"));
		} else if (btn == this.m_btnInvBillGoFirstOne) {
			showHintMessage(getHeadHintText()
					+ NCLangRes.getInstance().getStrByID("common", "UCH031"));
			setCurOperState(getBillBrowseState());
			onFirst();
		} else if (btn == this.m_btnInvBillGoLastOne) {
			showHintMessage(getHeadHintText()
					+ NCLangRes.getInstance().getStrByID("common", "UCH032"));
			setCurOperState(getBillBrowseState());
			onLast();
		} else if (btn == this.m_btnInvBillGoPreviousOne) {
			showHintMessage(getHeadHintText()
					+ NCLangRes.getInstance().getStrByID("common", "UCH033"));
			setCurOperState(getBillBrowseState());
			onPrevious();
		} else if (btn == this.m_btnInvBillGoNextOne) {
			showHintMessage(getHeadHintText()
					+ NCLangRes.getInstance().getStrByID("common", "UCH034"));

			setCurOperState(getBillBrowseState());
			onNext();
		} else if (btn == this.m_btnInvShift) {
			showHintMessage(getHeadHintText()
					+ NCLangRes.getInstance().getStrByID("common", "UCH055"));

			setCurOperState(getListOperState());
			onList();
		} else if (btn == this.m_btnInvBillQuery) {
			showHintMessage(getHeadHintText()
					+ NCLangRes.getInstance().getStrByID("common", "UCH046"));

			setCurOperState(2);
			onBillQuery();
			showHintMessage(NCLangRes.getInstance().getStrByID("common",
					"UCH009"));
		} else if (btn == this.m_btnInvBillCopy) {
			showHintMessage(getHeadHintText()
					+ NCLangRes.getInstance().getStrByID("40040401",
							"UPP40040401-000035"));

			setCurOperState(1);
			onCopy();
		} else if (btn == this.m_btnInvBillPreview) {
			showHintMessage(getHeadHintText()
					+ NCLangRes.getInstance().getStrByID("common", "UCH061"));
			setCurOperState(getBillBrowseState());

			onCardPrintPreview();

			showHintMessage(NCLangRes.getInstance().getStrByID("common",
					"UCH041"));
		} else if (btn == this.m_btnInvBillPrint) {
			showHintMessage(getHeadHintText()
					+ NCLangRes.getInstance().getStrByID("40040401",
							"UPP40040401-000036"));
			setCurOperState(getBillBrowseState());

			onCardPrint();

			showHintMessage(NCLangRes.getInstance().getStrByID("common",
					"UCH041"));
		} else if (btn == this.m_btnInvBillRefresh) {
			showHintMessage(getHeadHintText()
					+ NCLangRes.getInstance().getStrByID("common", "UCH046"));

			setCurOperState(2);
			onBillRefresh();
			showHintMessage(NCLangRes.getInstance().getStrByID("common",
					"UCH007"));
		} else if (btn == this.m_btnLnkQuery) {
			setCurOperState(2);
			onLnkQuery();
		} else if (btn == this.m_btnInvBillAudit) {
			/**
			 * add by gt 2019-08-23
			 * 采购发票审核时，校验是否预付=‘是’，若等于‘是’，则校验采购订单是否存在对应的预付款申请单，若不存在，审核不通过
			 */
			//国内公司校验
			String corp = PoPublicUIClass.getLoginPk_corp();
			  IdetermineService idetermineService =(IdetermineService) NCLocator.getInstance().lookup(IdetermineService.class.getName());
			  Boolean result = idetermineService.check(corp);
			  if(result)//判断当前公司是否为国内公司，否则不执行
			  {
	        //取表头中是否预付（vdef17）值
			Object yfObj=getInvBillPanel().getHeadItem("vdef17").getValueObject();
			List<String> cglList=new ArrayList<String>();
				//判断字段‘是否预付款’有没有值且值是不是‘是’，根据表体中的订单号，查询有没有对应的预付款申请单
				String flag = yfObj==null?"N":yfObj.toString();
				if(flag.equals("Y")){
				
					//获取表体的行数
					int num=this.getInvBillPanel().getRowCount();
					String cgArr[]=new String[num];
					String cgdd=null;
					Object cgObject=null;
					StringBuffer cgString=new StringBuffer();
					
					//校验所有的订单号，若有的订单没有对应的预付申请单，就将该订单号加到List结果集中
					for(int i=0;i<cgArr.length;i++){
						//获取订单id
						 cgdd=(String) getInvBillPanel().getBodyValueAt(i,"corderid");
						 StringBuffer cgsql = new StringBuffer();
						 cgsql.append("select pk_fksqd  from  arap_fksqd  where vdef1='"+cgdd+"' ");
						 IUAPQueryBS  cguap = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
						 try {
								cgObject=cguap.executeQuery(cgsql.toString(), new ColumnProcessor());
								if(cgObject == null){
									cglList.add(cgdd);
								}
							} catch (BusinessException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} 
					}			
					//判断List中是否为空，若不为空则代表该发票中存在没有对应预付款申请单的订单	
					if(cglList.size()==0){
						showHintMessage(NCLangRes.getInstance().getStrByID("common",
						"UCH048"));
				        onBillAudit();
				        setCurOperState(2);
					}else {	
						for(int i=0;i<cglList.size();i++){	
						   cgString.append(cglList.get(i)+",");
						} 
						showErrorMessage("未找到订单："+cgString+"的预付款申请单信息!");
						
					}	
				}else{
					showHintMessage(NCLangRes.getInstance().getStrByID("common",
					"UCH048"));
			         onBillAudit();
			         setCurOperState(2);
				}
			  }else{
					showHintMessage(NCLangRes.getInstance().getStrByID("common",
					"UCH048"));
			         onBillAudit();
			         setCurOperState(2);
				}
				//end by gt  2019-08-23
			
		} else if (btn == this.m_btnInvBillUnAudit) {
			showHintMessage(NCLangRes.getInstance().getStrByID("common",
					"UCH049"));
			onBillUnAudit();
		} else if (btn == this.m_btnDocManage) {
			showHintMessage(getHeadHintText()
					+ NCLangRes.getInstance().getStrByID("40040401",
							"UPPSCMCommon-000278"));

			onDocManage();
		} else if (btn == this.m_btnHqhp) {
			onHqhp();
		} else if (btn == this.m_btnSendAudit) {
			showHintMessage(NCLangRes.getInstance().getStrByID("common",
					"UCH047"));
			if ((getInvVOs() != null) && (getInvVOs()[getCurVOPos()] != null)) {
				if (this.m_bAdd) {
					onSendAudit(null);
				} else
					onSendAudit(getInvVOs()[getCurVOPos()]);
			} else {
				onSendAudit(null);
			}
			if ((getInvVOs() == null) || (getInvVOs()[getCurVOPos()] == null)) {
				if (this.m_bAdd) {
					boolean isNeedSendToAuditQ = BusiBillManageTool
							.isNeedSendToAudit("25", getPk_corp(),
									getCurBizeType(), null, getCurOperator());

					Integer iBillStatus = new Integer(88);
					if (getInvVOs() != null) {
						InvoiceVO vo = getInvVOs()[getCurVOPos()];

						if ((vo != null) && (vo.getHeadVO() != null)) {
							iBillStatus = vo.getHeadVO().getIbillstatus();
						}
					}

					if ((isNeedSendToAuditQ)
							&& (((iBillStatus.compareTo(BillStatus.FREE) == 0) || (iBillStatus
									.compareTo(BillStatus.AUDITFAIL) == 0)))) {
						this.m_btnSendAudit.setEnabled(true);
					}

				}

			}

			setVOToBillPanel();
			if (getCurOperState() != 5)
				setCurOperState(2);
			showHintMessage(NCLangRes.getInstance().getStrByID("common",
					"UCH023"));
		} else if (btn == this.btnBillCombin) {
			onBillCombin();
		} else if (btn == this.m_btnAudit) {    //审核

			showHintMessage(NCLangRes.getInstance().getStrByID("common",
					"UCH048"));
			        onAudit();
		} else if (btn == this.m_btnQueryForAudit) {
			onQueryForAudit();
			
			
		}else if(btn == this.m_xmlDc){
				onxmlDc();    //yqq   卡片
			 
		} else if (btn == this.m_btnUnAudit) {
			showHintMessage(NCLangRes.getInstance().getStrByID("common",
					"UCH049"));
			onUnAudit();
		} else if (PuTool.isExist(getExtendBtns(), btn)) {
			onExtendBtnsClick(btn);
		} else if (btn == this.m_btnInvBillConversion) {
			showHintMessage(getHeadHintText()
					+ NCLangRes.getInstance().getStrByID("40040401",
							"UPP40040401-000039"));

			onGiveupBillConversion();
		}
	}

	private UFDouble[] queryPlanPrices(String[] cMangID, String pk_calbody) {
		if ((cMangID == null) || (cMangID.length == 0))
			return null;
		UFDouble[] nPrice = new UFDouble[cMangID.length];

		if ((pk_calbody != null) && (pk_calbody.trim().length() > 0)) {
			for (int i = 0; i < cMangID.length; ++i) {
				Object[][] oTemp = CacheTool.getAnyValue2("bd_produce", "jhj",
						"PK_CALBODY='" + pk_calbody + "' and PK_INVMANDOC='"
								+ cMangID[i] + "'");
				if ((oTemp != null) && (oTemp.length > 0) && (oTemp[0] != null)
						&& (oTemp[0].length > 0) && (oTemp[0][0] != null))
					nPrice[i] = new UFDouble(oTemp[0][0].toString());
			}
		} else {
			try {
				Object o = CacheTool.getColumnValue("bd_invmandoc",
						"pk_invmandoc", "planprice", cMangID);
				if (o != null) {
					Object[] oTemp = (Object[]) (Object[]) o;
					for (int i = 0; i < cMangID.length; ++i) {
						if ((oTemp[i] == null)
								|| (oTemp[i].toString().trim().length() <= 0))
							continue;
						nPrice[i] = new UFDouble(oTemp[i].toString());
					}
				}
			} catch (Exception e) {
				SCMEnv.out(e);
			}
		}

		return nPrice;
	}

	private void onButtonClickedList(ButtonObject btn) {
		if (btn == this.m_btnInvBillQuery) {
			showHintMessage(getHeadHintText()
					+ NCLangRes.getInstance().getStrByID("common", "UCH046"));

			onListQuery();
			showHintMessage(NCLangRes.getInstance().getStrByID("common",
					"UCH009"));
		} else if (btn == this.m_btnInvSelectAll) {
			showHintMessage(getHeadHintText()
					+ NCLangRes.getInstance().getStrByID("40040401",
							"UPP40040401-000032"));

			onSelectAll();
			showHintMessage(NCLangRes.getInstance().getStrByID("common",
					"4004COMMON000000033"));
		} else if (btn == this.m_btnInvDeselectAll) {
			showHintMessage(getHeadHintText()
					+ NCLangRes.getInstance().getStrByID("40040401",
							"UPP40040401-000032"));

			onSelectNo();
			showHintMessage(NCLangRes.getInstance().getStrByID("common",
					"4004COMMON000000034"));
		} else if (btn == this.m_btnInvBillDiscard) {
			showHintMessage(getHeadHintText()
					+ NCLangRes.getInstance().getStrByID("common", "UCH051"));

			onListDiscard();
			showHintMessage(NCLangRes.getInstance().getStrByID("common",
					"UCH006"));
		} else if (btn == this.m_btnInvBillModify) {
			showHintMessage(getHeadHintText()
					+ NCLangRes.getInstance().getStrByID("common", "UCH027"));

			onListModify();

			getInvBillPanel().transferFocusTo(0);
		} else if (btn == this.m_btnInvShift) {
			showHintMessage(getHeadHintText()
					+ NCLangRes.getInstance().getStrByID("common", "UCH055"));

			onListBill();
		} else if (btn == this.m_btnInvBillRefresh) {
			showHintMessage(getHeadHintText()
					+ NCLangRes.getInstance().getStrByID("common", "UCH046"));

			onListRefresh();
			showHintMessage(NCLangRes.getInstance().getStrByID("common",
					"UCH007"));
		} else if (btn == this.m_btnInvBillConversion) {
			showHintMessage(getHeadHintText()
					+ NCLangRes.getInstance().getStrByID("40040401",
							"UPP40040401-000039"));

			onGiveupBillConversion();
		} else if (btn == this.m_btnDocManage) {
			showHintMessage(getHeadHintText()
					+ NCLangRes.getInstance().getStrByID("40040401",
							"UPPSCMCommon-000278"));

			onDocManage();
		} else if (btn == this.m_btnQueryForAudit) {
			onQueryForAudit();
		} else if (btn == this.m_btnInvBillPrint) {
			onBatchPrint();
		} else if (btn == this.m_btnInvBillPreview) {
			onBatchPrintPreview();
		} else if (PuTool.isExist(getExtendBtns(), btn)) {
			onExtendBtnsClick(btn);
		} else if (btn == this.m_btnInvBillCopy) {
			showHintMessage(getHeadHintText()
					+ NCLangRes.getInstance().getStrByID("40040401",
							"UPP40040401-000035"));
			setCurOperState(1);
			onCopy();
			shiftShowModeTo(0);
		} else if (btn == this.btnBillCombin) {
			onBillCombin();
		} else if (btn == this.m_btnLnkQuery) {
			setCurOperState(2);
			onLnkQuery();
		} else if (btn == this.m_btnInvBillAudit) {
			showHintMessage(NCLangRes.getInstance().getStrByID("common",
					"UCH048"));
			onListAudit();
			setCurOperState(2);

		}else if(btn == this.m_xmlDc){
			onxmlDch();    //yqq  列表
			
		} else if (btn == this.m_btnInvBillUnAudit) {
			showHintMessage(NCLangRes.getInstance().getStrByID("common",
					"UCH049"));
			onListUnAudit();
		}
	}

	public boolean onClosing() {
		if (getListOperState() == 5) {
			int ret = MessageDialog
					.showYesNoCancelDlg(this, NCLangRes.getInstance()
							.getStrByID("40040401", "UPPSCMCommon-000270"),
							NCLangRes.getInstance().getStrByID("40040401",
									"UPP40040401-000040"));

			return ret == 4;
		}

		if (getCurOperState() == 1) {
			int ret = MessageDialog.showYesNoCancelDlg(this, NCLangRes
					.getInstance()
					.getStrByID("40040401", "UPPSCMCommon-000270"), NCLangRes
					.getInstance().getStrByID("common", "UCH001"));
			if (ret == 4)
				if (onSave())
					return true;
				else if (ret == 8) {
					return true;
				}
			return false;
		}

		return true;
	}

	private void onCopy() {
		setCurOperState(1);

		dealWhenCopy();

		setCouldMaked(true);
		this.m_bAdd = true;

		setButtonsAndPanelState();
		updateButtons();

		this.m_bCopy = true;
	}

	private void onCopyLine() {
		getInvBillPanel().copyLine();
	}

	/** @deprecated */
	private void onDeleteLine() {
		if (getInvBillPanel().getBillModel().getRowCount() <= 0) {
			return;
		}

		getInvBillPanel().delLine();
	}

	private void onDiscard() {

		System.out.println("删除");
		Object vinvoicecode = getInvBillPanel().getHeadItem("vinvoicecode").getValueObject();
		Object corp = getInvBillPanel().getHeadItem("pk_corp").getValueObject();
		IPubDMO ipubdmo = (IPubDMO)NCLocator.getInstance().lookup(IPubDMO.class.getName());
		StringBuffer sql=new StringBuffer();
		sql.append("update po_check set dr='1' where vinvoicecode='"+vinvoicecode+"' and pk_corp='"+corp+"'");
		System.out.println("删除sql"+sql);
		 try {
			ipubdmo.executeUpdate(sql.toString());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int ret = MessageDialog.showYesNoDlg(this, NCLangRes.getInstance()
				.getStrByID("40040401", "UPP40040401-000042"), NCLangRes
				.getInstance().getStrByID("common", "UCH002"));
		if ((ret == 8) || (ret == 2)) {
			showHintMessage("");
			return;
		}

		ArrayList paraList = new ArrayList();
		paraList.add(null);

		InvoiceVO invVO = getInvVOs()[getCurVOPos()];
		for (int i = 0; i < invVO.getBodyVO().length; ++i) {
			invVO.getBodyVO()[i].setNShowRow(i + 1);
		}
		invVO.getHeadVO().setCuserid(
				ClientEnvironment.getInstance().getUser().getPrimaryKey());

		((InvoiceHeaderVO) invVO.getParentVO())
				.setCoperatoridnow(ClientEnvironment.getInstance().getUser()
						.getPrimaryKey());

		showHintMessage(getHeadHintText() + "|"
				+ NCLangRes.getInstance().getStrByID("common", "UC001-0000005")
				+ "|" + "<#" + "......" + "#>");

		boolean bConfirm = false;
		invVO.setUserConfirmFlag(new UFBoolean(false));
		do {
			if (invVO.getUserConfirmFlag().booleanValue())
				break;
			try {
				if (bConfirm)
					invVO.setUserConfirmFlag(new UFBoolean(true));
				PfUtilClient.processBatch("DISCARD", "25", getToday()
						.toString(), new InvoiceVO[] { invVO },
						new Object[] { paraList });
			} catch (RwtPiToPoException ex) {
				SCMEnv.out(ex);
				if (MessageDialog.showYesNoDlg(this, NCLangRes.getInstance()
						.getStrByID("40040401", "UPPSCMCommon-000270"), ex
						.getMessage()) == 4) {
					bConfirm = true;
				} else {
					showHintMessage("");
					return;
				}
			} catch (RwtPiToScException ex) {
				SCMEnv.out(ex);
				if (MessageDialog.showYesNoDlg(this, NCLangRes.getInstance()
						.getStrByID("40040401", "UPPSCMCommon-000270"), ex
						.getMessage()) == 4) {
					bConfirm = true;
				} else {
					showHintMessage("");
					return;
				}
			} catch (Exception e) {
				SCMEnv.out(e);
				MessageDialog.showErrorDlg(this, NCLangRes.getInstance()
						.getStrByID("40040401", "UPP40040401-000016"),
						NCLangRes.getInstance().getStrByID("40040401",
								"UPP40040401-000044"));
				showHintMessage("");
				return;
			}
		} while (bConfirm);

		if (PfUtilClient.isSuccess()) {
			showHintMessage(NCLangRes.getInstance().getStrByID("common",
					"UCH006"));

			removeOneFromInvVOs(getCurVOPos());

			setVOToBillPanel();
		}
		
		showHintMessage("");
	}

	private void onDocManage() {
		if ((getInvVOs() != null) && (getInvVOs().length > 0)) {
			String[] billIDs = null;
			String[] billCodes = null;
			Vector v1 = new Vector();
			Vector v2 = new Vector();

			for (int i = 0; i < getInvVOs().length; ++i) {
				if (getInvVOs()[i].getHeadVO().getPrimaryKey() == null)
					continue;
				v1.add(getInvVOs()[i].getHeadVO().getPrimaryKey());
				v2.add(getInvVOs()[i].getHeadVO().getVinvoicecode());
			}

			if ((v1.size() > 0) && (v1.size() == v2.size())) {
				billIDs = new String[v1.size()];
				billCodes = new String[v2.size()];
				v1.copyInto(billIDs);
				v2.copyInto(billCodes);
				DocumentManager.showDM(this, billIDs, billCodes);
			}
		}
	}

	private void onHqhp() {
		if (getCurOperState() != 1) {
			return;
		}

		int bodyNum = getInvBillPanel().getRowCount();
		if (bodyNum < 1) {
			return;
		}

		InvoiceVO billVO = new InvoiceVO(bodyNum);
		getInvBillPanel().getBillValueVO(billVO);
		int iLen = billVO.getBodyVO().length;
		String lStr_NativeCurrencyID = PiPqPublicUIClass.getNativeCurrencyID();
		Vector lVec_RowIndex = new Vector();
		Vector lVec_Temp = new Vector();
		for (int i = 0; i < iLen; ++i) {
			if ((!billVO.getBodyVO()[i].getCupsourcebilltype().equals("45"))
					|| (!billVO.getBodyVO()[i].getCcurrencytypeid().equals(
							lStr_NativeCurrencyID)))
				continue;
			PricParaVO tempVO = new PricParaVO();
			tempVO
					.setCgeneralbid(billVO.getBodyVO()[i]
							.getCupsourcebillrowid());
			lVec_Temp.addElement(tempVO);

			lVec_RowIndex.addElement(new Integer(i));
		}

		PricParaVO[] VOs = new PricParaVO[lVec_Temp.size()];
		lVec_Temp.copyInto(VOs);
		try {
			VOs = PricStlHelper.queryPricStlPrices(VOs);
		} catch (Exception e) {
			SCMEnv.out(e);
		}

		if ((VOs != null) && (VOs.length > 0))
			for (int i = 0; i < VOs.length; ++i) {
				if (VOs[i].getNtaxprice() != null) {
					int lint_row = ((Integer) lVec_RowIndex.elementAt(i))
							.intValue();
					getInvBillPanel().setBodyValueAt(VOs[i].getNtaxprice(),
							lint_row, "norgnettaxprice");
					BillEditEvent event = new BillEditEvent(getInvBillPanel()
							.getBillTable(), VOs[i].getNtaxprice(),
							"norgnettaxprice", lint_row);
					afterEditInvBillRelations(event);
				}
			}
	}

	/** @deprecated */
	private void onFirst() {
		this.m_nLstInvVOPos = getCurVOPos();

		setCurVOPos(0);

		setMaxMnyDigit(this.iMaxMnyDigit);

		setVOToBillPanel();
	}

	private void onGiveupBillConversion() {
		Vector v = new Vector();
		for (int i = 0; (getInvVOs() != null) && (i < getInvVOs().length); ++i) {
			if (getInvVOs()[i].getPrimaryKey() == null) {
				v.add(new Integer(i));
			}
		}

		Integer[] indexes = null;
		if (v.size() > 0) {
			indexes = new Integer[v.size()];
			v.copyInto(indexes);
		}
		if (indexes != null) {
			removeSomeFromInvVOs(indexes);
		}

		if ((this.m_vSavedVO != null) && (this.m_vSavedVO.size() > 0)) {
			InvoiceVO[] tempVO = new InvoiceVO[this.m_vSavedVO.size()];
			this.m_vSavedVO.copyInto(tempVO);
			setInvVOs(tempVO);
		} else {
			setInvVOs(null);
		}
		this.m_vSavedVO = null;

		if (getInvVOs() == null) {
			setCurVOPos(-1);

			setVOToBillPanel();
		} else {
			setCurVOPos(getInvVOs().length - 1);
			setVOToBillPanel();
		}

		setListOperState(4);
		setCurOperState(getBillBrowseState());
		shiftShowModeTo(0);
		this.m_btnInvBillConversion.setEnabled(false);
	}

	private void onInsertLine() {
		int iRow = getInvBillPanel().getBillTable().getSelectedRow();

		getInvBillPanel().insertLine();

		if (iRow >= 0) {
			setNewLineDefaultValue(iRow);
		}

		BillRowNo.insertLineRowNo(getInvBillPanel(), "25", "crowno");
	}

	/** @deprecated */
	private void onLast() {
		this.m_nLstInvVOPos = getCurVOPos();

		setCurVOPos(getInvVOs().length - 1);

		setMaxMnyDigit(this.iMaxMnyDigit);

		setVOToBillPanel();
	}

	/** @deprecated */
	private void onList() {
		if (getInvVOs() != null) {
			int nVOStatus = 0;
			int len = getInvVOs().length;
			for (int i = 0; i < len; ++i) {
				nVOStatus = getInvVOs()[i].getSource();

				if ((PuPubVO.getString_TrimZeroLenAsNull(getInvVOs()[i]
						.getHeadVO().getVinvoicecode()) == null)
						|| (PuPubVO.getString_TrimZeroLenAsNull(getInvVOs()[i]
								.getHeadVO().getVinvoicecode()) == null)) {
					removeOneFromInvVOs(i);
				}
			}

		}

		shiftShowModeTo(1);

		setVOsToListPanel();

		setCurOperState(getListOperState());
	}

	private void onListBill() {
		if (getCurOperState() == 5) {
			return;
		}
		if (getInvVOs() == null) {
			getInvBillPanel().addNew();
		} else {
			int row = -1;
			for (int i = 0; i < getInvVOs().length; ++i) {
				if (getInvListPanel().getHeadBillModel().getRowState(i) == 4) {
					row = i;
					break;
				}
			}
			row = PuTool.getIndexBeforeSort(getInvListPanel(), row);

			if (getInvVOs()[row].getBodyVO() == null) {
				return;
			}
			setCurVOPos(row);

			setMaxMnyDigit(this.iMaxMnyDigit);

			setVOToBillPanel();

			getInvBillPanel().getHeadItem("nexchangeotobrate").setEdit(false);
			getInvBillPanel().getHeadItem("nexchangeotoarate").setEdit(false);
		}
		setCurOperState(getBillBrowseState());
		shiftShowModeTo(0);
	}

	private void onListDiscard() {
		Vector vDiscardVO = new Vector();
		Vector vDiscardIndex = new Vector();
		for (int i = 0; i < getInvVOs().length; ++i) {
			if (getInvListPanel().getHeadBillModel().getRowState(i) != 4)
				continue;
			int nCurIndex = PuTool.getIndexBeforeSort(getInvListPanel(), i);
			vDiscardVO.add(getInvVOs()[nCurIndex]);
			vDiscardIndex.add(new Integer(nCurIndex));
		}

		InvoiceVO[] vos = null;
		if (vDiscardVO.size() > 0) {
			vos = new InvoiceVO[vDiscardVO.size()];
			vDiscardVO.copyInto(vos);
			if (!loadItemsForInvoiceVOs(vos)) {
				return;
			}
		}

		for (int i = 0; (vos != null) && (i < vos.length); ++i) {
			Integer iBillStatus = vos[i].getHeadVO().getIbillstatus();
			if ((iBillStatus.compareTo(BillStatus.AUDITED) == 0)
					|| (iBillStatus.compareTo(BillStatus.AUDITING) == 0)) {
				MessageDialog.showHintDlg(this, NCLangRes.getInstance()
						.getStrByID("40040401", "UPPSCMCommon-000270"),
						NCLangRes.getInstance().getStrByID("40040401",
								"UPP40040401-000046"));
				return;
			}

			if (vos[i].isVirtual()) {
				MessageDialog.showHintDlg(this, NCLangRes.getInstance()
						.getStrByID("40040401", "UPPSCMCommon-000270"),
						NCLangRes.getInstance().getStrByID("40040401",
								"UPP40040401-000047"));
				return;
			}

			InvoiceItemVO[] voaItem = vos[i].getBodyVO();
			int iSettle = -1;
			int iLen = voaItem.length;
			for (int j = 0; j < iLen; ++j) {
				if ((voaItem[j].getNaccumsettmny() != null)
						&& (voaItem[j].getNaccumsettmny().compareTo(
								VariableConst.ZERO) != 0)) {
					iSettle = i + 1;
					break;
				}
			}
			if (iSettle != -1) {
				MessageDialog.showHintDlg(this, NCLangRes.getInstance()
						.getStrByID("40040401", "UPPSCMCommon-000270"),
						NCLangRes.getInstance().getStrByID("40040401",
								"UPP40040401-000048"));
				return;
			}
		}

		if (vDiscardVO.size() == 0) {
			MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID(
					"40040401", "UPPSCMCommon-000270"), NCLangRes.getInstance()
					.getStrByID("40040401", "UPP40040401-000049"));
			setSelectedRowCount(0);
			return;
		}

		int ret = MessageDialog.showYesNoDlg(this, NCLangRes.getInstance()
				.getStrByID("common", "UCH002"), NCLangRes.getInstance()
				.getStrByID("40040401", "UPP40040401-000050"));

		if ((ret == 8) || (ret == 2)) {
			return;
		}

		InvoiceVO[] proceVOs = new InvoiceVO[vDiscardVO.size()];
		vDiscardVO.copyInto(proceVOs);

		int len = proceVOs.length;
		for (int i = 0; i < len; ++i) {
			int bLen = proceVOs[i].getBodyVO().length;
			for (int j = 0; j < bLen; ++j) {
				proceVOs[i].getBodyVO()[j].setNShowRow(j + 1);
			}

		}

		Object[] paraLists = new Object[len];
		len = paraLists.length;
		for (int i = 0; i < len; ++i) {
			paraLists[i] = new ArrayList();
			((ArrayList) paraLists[i]).add(null);
		}

		for (int i = 0; i < proceVOs.length; ++i) {
			proceVOs[i].getHeadVO().setCuserid(
					ClientEnvironment.getInstance().getUser().getPrimaryKey());

			((InvoiceHeaderVO) proceVOs[i].getParentVO())
					.setCoperatoridnow(ClientEnvironment.getInstance()
							.getUser().getPrimaryKey());
		}

		showHintMessage(getHeadHintText()
				+ "|"
				+ NCLangRes.getInstance().getStrByID("40040401",
						"UPP40040401-000051") + "|" + "<#" + "......" + "#>");
		try {
			PfUtilClient.processBatch("DISCARD", "25", ClientEnvironment
					.getInstance().getDate().toString(), proceVOs, paraLists);
		} catch (BusinessException e) {
			SCMEnv.out(e);
			MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID(
					"40040401", "UPPSCMCommon-000270"), e.getMessage());
			return;
		} catch (Exception e) {
			SCMEnv.out(e);
			MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID(
					"40040401", "UPP40040401-000016"), NCLangRes.getInstance()
					.getStrByID("40040401", "UPP40040401-000052"));
			return;
		}

		if (!PfUtilClient.isSuccess())
			return;
		Integer[] iaIndex = new Integer[vDiscardIndex.size()];
		vDiscardIndex.copyInto(iaIndex);
		removeSomeFromInvVOs(iaIndex);
		setVOsToListPanel();
	}

	private void onListModify() {
		int nCurIndex = -1;
		int VOPos = -1;
		for (int i = 0; i < getInvVOs().length; ++i) {
			if (getInvListPanel().getHeadBillModel().getRowState(i) == 4) {
				nCurIndex = i;
				break;
			}

		}

		nCurIndex = PuTool.getIndexBeforeSort(getInvListPanel(), nCurIndex);
		VOPos = nCurIndex;
		if ((this.VOsPos != null) && (this.VOsPos.length > 0)) {
			nCurIndex = new Integer(this.VOsPos[nCurIndex]).intValue();
			this.VOsPos = null;
		}

		if (getCurOperState() != 5) {
			Integer iStatus = getInvVOs()[nCurIndex].getHeadVO()
					.getIbillstatus();
			if ((iStatus != null) && (iStatus.compareTo(BillStatus.FREE) != 0)
					&& (iStatus.compareTo(BillStatus.AUDITFAIL) != 0)) {
				MessageDialog.showHintDlg(this, NCLangRes.getInstance()
						.getStrByID("40040401", "UPPSCMCommon-000270"),
						NCLangRes.getInstance().getStrByID("40040401",
								"UPP40040401-000055"));
				return;
			}

			for (int i = 0; i < getInvVOs()[nCurIndex].getBodyVO().length; ++i) {
				if ((getInvVOs()[nCurIndex].getBodyVO()[i].getNaccumsettmny() != null)
						&& (getInvVOs()[nCurIndex].getBodyVO()[i]
								.getNaccumsettmny().doubleValue() != 0.0D)) {
					MessageDialog.showHintDlg(this, NCLangRes.getInstance()
							.getStrByID("40040401", "UPPSCMCommon-000270"),
							NCLangRes.getInstance().getStrByID("40040401",
									"UPP40040401-000056"));
					return;
				}
			}

			if (getInvVOs()[nCurIndex].isVirtual()) {
				MessageDialog.showHintDlg(this, NCLangRes.getInstance()
						.getStrByID("40040401", "UPPSCMCommon-000270"),
						NCLangRes.getInstance().getStrByID("40040401",
								"UPP40040401-000057"));
				return;
			}

			setCurBizeType(getInvVOs()[nCurIndex].getHeadVO().getCbiztype());
		}
		if ((getInvListPanel().getHeadBillModel().getValueAt(VOPos,
				"cinvoiceid") == null)
				&& (this.splitFlag)) {
			setCurVOPos(this.currentPos + VOPos);

			this.splitFlag = false;
		} else {
			setCurVOPos(nCurIndex);
		}
		setCurOperState(1);
		shiftShowModeTo(0);

		setVOToBillPanel();

		String cBizType = getInvBillPanel().getHeadItem("cbiztype").getValue();
		if ((cBizType == null) || (cBizType.toString().trim().length() == 0)) {
			cBizType = getCurBizeType();
			getInvBillPanel().setHeadItem("cbiztype", cBizType);
		}

		CheckISSellProxy checkISSellProxy = new CheckISSellProxy();
		boolean checker = false;
		try {
			if (!this.cBizTypeTable.containsKey(cBizType)) {
				checkISSellProxy = new CheckISSellProxy();

				Object oTemp = CacheTool.getCellValue("bd_busitype",
						"pk_busitype", "verifyrule", cBizType);
				if (oTemp != null) {
					Object[] o = (Object[]) (Object[]) oTemp;
					if ((o != null) && (o.length > 0) && (o[0] != null)
							&& (o[0].equals("S")))
						checker = true;
				}
			} else {
				checker = new UFBoolean(this.cBizTypeTable.get(cBizType)
						.toString()).booleanValue();
			}

			if (checker) {
				String sql = " and (sellproxyflag = 'Y')";
				UIRefPane refCinventorycode = (UIRefPane) (UIRefPane) getInvBillPanel()
						.getBodyItem("invcode").getComponent();
				refCinventorycode.getRefModel().addWherePart(sql);
				if (!this.cBizTypeTable.containsKey(cBizType))
					this.cBizTypeTable.put(cBizType, String.valueOf(checker));
			} else {
				String sql = " and ( 1 =1 )";
				UIRefPane refCinventorycode = (UIRefPane) (UIRefPane) getInvBillPanel()
						.getBodyItem("invcode").getComponent();
				refCinventorycode.getRefModel().addWherePart(sql);
			}
		} catch (Exception e) {
			SCMEnv.out(e);
		}

		setDefaultValueByUser();

		rightButtonRightControl();
		updateButtons();
	}

	private void onListQuery() {
		InvQueDlg curDlg = getQueDlg();
		curDlg.showModal();

		if (!curDlg.isCloseOK()) {
			return;
		}

		execListQuery(curDlg);

		setEverQueryed(true);
	}

	private void onListRefresh() {
		execListQuery(getQueDlg());
	}

	/** @deprecated */
	private void onLnkQuery() {
		InvoiceVO vo = getInvVOs()[getCurVOPos()];

		if ((vo == null) || (vo.getParentVO() == null)) {
			return;
		}

		SourceBillFlowDlg soureDlg = new SourceBillFlowDlg(this, "25",
				((InvoiceHeaderVO) vo.getParentVO()).getPrimaryKey(), null,
				ClientEnvironment.getInstance().getUser().getPrimaryKey(),
				getPk_corp());

		soureDlg.showModal();
	}

	public void onMenuItemClick(ActionEvent e) {
		UIMenuItem activeItem = (UIMenuItem) e.getSource();
		if (activeItem == getInvBillPanel().getAddLineMenuItem()) {
			onAppendLine();
		} else if (activeItem == getInvBillPanel().getDelLineMenuItem()) {
			onDeleteLine();
		} else if (activeItem == getInvBillPanel().getCopyLineMenuItem()) {
			onCopyLine();
		} else if (activeItem == getInvBillPanel().getPasteLineMenuItem()) {
			onPasteLine();
		} else if (activeItem == getInvBillPanel().getInsertLineMenuItem()) {
			onInsertLine();
		} else if (activeItem == getInvBillPanel().getPasteLineToTailMenuItem())
			onPasteLineToTail();
	}

	/** @deprecated */
	private void onModify() {
		String cBizType = getInvBillPanel().getHeadItem("cbiztype").getValue();
		CheckISSellProxy checkISSellProxy = new CheckISSellProxy();
		boolean checker = false;
		try {
			if (!this.cBizTypeTable.containsKey(cBizType)) {
				checkISSellProxy = new CheckISSellProxy();

				Object oTemp = CacheTool.getCellValue("bd_busitype",
						"pk_busitype", "verifyrule", cBizType);
				if (oTemp != null) {
					Object[] o = (Object[]) (Object[]) oTemp;
					if ((o != null) && (o.length > 0) && (o[0] != null)
							&& (o[0].equals("S")))
						checker = true;
				}
			} else {
				checker = new UFBoolean(this.cBizTypeTable.get(cBizType)
						.toString()).booleanValue();
			}

			if (checker) {
				String sql = " and (sellproxyflag = 'Y')";
				UIRefPane refCinventorycode = (UIRefPane) (UIRefPane) getInvBillPanel()
						.getBodyItem("invcode").getComponent();
				refCinventorycode.getRefModel().addWherePart(sql);
				if (!this.cBizTypeTable.containsKey(cBizType))
					this.cBizTypeTable.put(cBizType, String.valueOf(checker));
			} else {
				String sql = " and ( 1 =1 )";
				UIRefPane refCinventorycode = (UIRefPane) (UIRefPane) getInvBillPanel()
						.getBodyItem("invcode").getComponent();
				refCinventorycode.getRefModel().addWherePart(sql);
			}
		} catch (Exception e) {
			SCMEnv.out(e);
		}

		rightButtonRightControl();
		setButtonsAndPanelState();
		updateButtons();
	}

	/** @deprecated */
	private void onNext() {
		this.m_nLstInvVOPos = getCurVOPos();

		setCurVOPos(getCurVOPos() + 1);

		setMaxMnyDigit(this.iMaxMnyDigit);

		setVOToBillPanel();
	}

	private void onPasteLine() {
		int nRowCount1 = getInvBillPanel().getRowCount();
		getInvBillPanel().pasteLine();

		int nRowCount2 = getInvBillPanel().getRowCount();

		int nPastRowCount = nRowCount2 - nRowCount1;

		if (nPastRowCount <= 0)
			return;
		String strCurr = getInvBillPanel().getHeadItem("ccurrencytypeid")
				.getValue();
		int iSelectRow = getInvBillPanel().getBillTable().getSelectedRow();
		for (int i = iSelectRow - nPastRowCount; i < iSelectRow; ++i) {
			getInvBillPanel().setBodyValueAt(null, i, "cinvoiceid");
			getInvBillPanel().setBodyValueAt(null, i, "cinvoice_bid");

			getInvBillPanel().setBodyValueAt(null, i, "ts");

			getInvBillPanel().setBodyValueAt(strCurr, i, "ccurrencytypeid");

			setExchangeRateBody(i, true, null);
		}
		BillRowNo.pasteLineRowNo(getInvBillPanel(), "25", "crowno",
				nPastRowCount);
	}

	private void onPasteLineToTail() {
		int iOldRowCnt = getInvBillPanel().getRowCount();
		getInvBillPanel().pasteLineToTail();
		int iNewRowCnt = getInvBillPanel().getRowCount();
		if ((iOldRowCnt > 0) && (iNewRowCnt > 0) && (iOldRowCnt == iNewRowCnt)) {
			showHintMessage(NCLangRes.getInstance().getStrByID("40040401",
					"UPPSCMCommon-000424"));
		} else {
			int nPastRowCount = iNewRowCnt - iOldRowCnt;

			if (nPastRowCount > 0) {
				String strCurr = getInvBillPanel().getHeadItem(
						"ccurrencytypeid").getValue();
				for (int i = iOldRowCnt; i < iNewRowCnt; ++i) {
					getInvBillPanel().setBodyValueAt(null, i, "cinvoiceid");
					getInvBillPanel().setBodyValueAt(null, i, "cinvoice_bid");

					getInvBillPanel().setBodyValueAt(null, i, "ts");

					getInvBillPanel().setBodyValueAt(strCurr, i,
							"ccurrencytypeid");

					setExchangeRateBody(i, true, null);
				}

				BillRowNo.addLineRowNos(getInvBillPanel(), "25", "crowno",
						nPastRowCount);
			}
			showHintMessage(NCLangRes.getInstance().getStrByID("40040401",
					"UPP40040401-000058"));
		}
	}

	/** @deprecated */
	private void onPrevious() {
		this.m_nLstInvVOPos = getCurVOPos();

		setCurVOPos(getCurVOPos() - 1);

		setMaxMnyDigit(this.iMaxMnyDigit);

		setVOToBillPanel();
	}

	public void onQueryForAudit() {
		if ((getInvVOs() != null) && (getInvVOs().length > 0)) {
			FlowStateDlg approvestatedlg = new FlowStateDlg(this, "25",
					getInvVOs()[getCurVOPos()].getHeadVO().getPrimaryKey());
			approvestatedlg.showModal();
			showHintMessage(NCLangRes.getInstance().getStrByID("common",
					"UCH035"));
		} else {
			showHintMessage(NCLangRes.getInstance().getStrByID("40040101",
					"UPP40040101-000539"));
		}
	}

	private boolean onSave() {
		Timer timer = new Timer();
		timer.start("采购发票保存开始");
		Object result2 = null;
		IUAPQueryBS  uap2 = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
		Object corderid =getInvBillPanel().getBodyValueAt(0, "corderid");
		//采购发票ID
		Object cinvoiceid = getInvBillPanel().getBodyItem("cinvoiceid").getValueObject();
		String val = "select por.vdef17	 from  po_invoice_b pob inner join po_order por on  por.corderid = pob.corderid where  pob.cinvoiceid ='"+cinvoiceid+"'" ;
		try {
			 result2 =  uap2.executeQuery(val.toString(), new ColumnProcessor ());
			 if(result2==null){
					result2="";
				}
		} catch (BusinessException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		
		System.out.println("是否预付款："+result2);
		if("Y".equals(result2.toString().trim())){
	    String corps = ClientEnvironment
		.getInstance().getCorporation().getPk_corp();
	    //获取公司是否是国内的结果
	    IdetermineService idetermineService =(IdetermineService) NCLocator.getInstance().lookup(IdetermineService.class.getName());
	    Boolean result = idetermineService.check(corps);
	    System.out.println("获取当前登录公司"+corps);
	    if(result){
		//判断预付余额是否为0 为0时不在进行插入操作
		List list3 = new ArrayList();
		

		try {
			
			//发票号
			Object vinvoicecode = getInvBillPanel().getHeadItem("vinvoicecode").getValueObject();
			//公司
			Object corp = getInvBillPanel().getHeadItem("pk_corp").getValueObject();
			StringBuffer sql2s = new StringBuffer();//将指定的字符串追加到此字符序列
			sql2s.append("select yfbalance from po_check where dr=0 and corderid ='"+corderid+"' and pk_corp ='"+corp+"' ORDER BY TS  desc, vinvoicecode desc");
			System.out.println(sql2s);
	    	IUAPQueryBS  uap2s = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
			list3 = (List<Object>) uap2s.executeQuery(sql2s.toString(), new MapListProcessor ());
		} catch (BusinessException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		List listvalue = new ArrayList();
		String value2 = new String();
		Map map2 = new HashMap();
		if(list3==null||list3.size()==0||list3.equals("")){
			listvalue.add("-1");
			
		}else{		
			map2 = (Map)list3.get(0);
			listvalue.add(map2.get("yfbalance").toString());		
		}
		value2 =listvalue.get(0).toString();
		if("-1".equals(value2.trim())||Double.valueOf(value2)!=0.00){
		try {
			//获取采购订单ID
			// int  selCol=getInvBillPanel().getBillTable().getSelectedColumn();
			  
			System.out.println("保存开始");
			IVOPersistence ivo = (IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName());
			UFDouble Uf = new UFDouble();
			
			
			//获取核销预付款金额
			
			Object hxbalance = getInvBillPanel().getHeadItem("cvoucherid").getValueObject();
			UFDouble ufhxbalance = new UFDouble(String.valueOf(hxbalance));
			//获取预付款余额
			Object yfbalance = getInvBillPanel().getHeadItem("cfreecustid").getValueObject();	
			UFDouble ufyfbalance = new UFDouble(String.valueOf(yfbalance));
			
			//发票号
			Object vinvoicecode = getInvBillPanel().getHeadItem("vinvoicecode").getValueObject();
			
			
			//公司
			Object corp = getInvBillPanel().getHeadItem("pk_corp").getValueObject();
		
			
			//获取预付款金额
			List<String> list = new ArrayList<String>();
			StringBuffer sql = new StringBuffer();
			System.out.println("corderid:"+corderid+"");
			sql.append("select zfje from arap_fksqd where VDEF1 = '"+corderid+"' and nvl(dr,0)=0 and pk_corp = "+corp+"");
	    	IUAPQueryBS  uap = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());       	
			list =   (List<String>) uap.executeQuery(sql.toString(), new MapListProcessor());
			JSONObject js = new JSONObject();
			js = JSONObject.fromObject(list.get(0));
			
			String zfje = (String) js.get("zfje");
			System.out.println(zfje);
		
			UFDouble ufYfmoney = new UFDouble(zfje.toString());
			//获取采购订单号
			List lists = null;
			StringBuffer sqls = new StringBuffer();//将指定的字符串追加到此字符序列
			sqls.append("select VORDERCODE from PO_ORDER WHERE CORDERID = '"+corderid+"' and dr=0");
	    	IUAPQueryBS  uaps = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());       	
			lists = (List<Object>) uaps.executeQuery(sqls.toString(), new MapListProcessor ());
			JSONObject jsb = new JSONObject();
			jsb = JSONObject.fromObject(lists.get(0));
			
			
			String VORDERCODE = (String) jsb.get("vordercode");
			System.out.println(VORDERCODE);
			//插入核销的VO中
			CHECKVO checkVo = new CHECKVO(); 
			checkVo.setCinvoiceid(String.valueOf(cinvoiceid)); //
			checkVo.setCorderid(String.valueOf(corderid));
			checkVo.setPk_corp(String.valueOf(corp));
			checkVo.setYfbalance(ufyfbalance);
			checkVo.setHxmoney(ufhxbalance);
			checkVo.setVinvoicecode(String.valueOf(vinvoicecode));
			checkVo.setVordercode(VORDERCODE);
			checkVo.setYfmoney(ufYfmoney);
			StringBuffer Sql = new StringBuffer();
			Sql.append("select * from po_check  where dr =0 and cinvoiceid = '"+cinvoiceid+"'");

			List lists2 = (List<Object>) uaps.executeQuery(Sql.toString(), new MapListProcessor ());
			IPubDMO ipubdmo = (IPubDMO)NCLocator.getInstance().lookup(IPubDMO.class.getName());
			StringBuffer sql3=new StringBuffer();
			System.out.println("预付余额："+yfbalance);
			 sql3.append("update po_invoice set cfreecustid='"+yfbalance+"',cvoucherid='"+hxbalance+"' where VINVOICECODE='"+vinvoicecode+"'  and PK_CORP ='"+corp+"'");
			 System.out.println(sql3);
			 ipubdmo.executeUpdate(sql3.toString());
			System.out.println(yfbalance.toString()+yfbalance.toString().length());
			System.out.println("核销金额："+hxbalance);
			if("0.00".equals(hxbalance.toString())){
				System.out.println("核销为0不保存");
			
			}else if(yfbalance.toString().length()==0){
				MessageDialog.showErrorDlg(this,"保存失败", "请修改核销预付金额");
				
			}else{
				
			//插入
			ivo.insertVO(checkVo);
	  		System.out.println("插入保存结束");
			}
  			
  		} catch (BusinessException e1) {
  			// TODO Auto-generated catch block
  			e1.printStackTrace();
  			}
				}
	    			}
		}
		getInvBillPanel().stopEditing();

		filterNullLine();

		if (!BillRowNo.verifyRowNosCorrect(getInvBillPanel(), "crowno")) {
			return false;
		}

		int nRow = getInvBillPanel().getRowCount();
		for (int i = 0; i < nRow; ++i) {
			UFDouble nNum = new UFDouble(0);
			UFDouble nMoney = new UFDouble(0);
			Object oTemp = getInvBillPanel().getBodyValueAt(i, "ninvoicenum");
			if (oTemp != null)
				nNum = new UFDouble(oTemp.toString());
			oTemp = getInvBillPanel().getBodyValueAt(i, "noriginalcurmny");
			if (oTemp != null) nMoney = new UFDouble(oTemp.toString());
			if (nNum.doubleValue() * nMoney.doubleValue() < 0.0D) { MessageDialog.showWarningDlg(this, NCLangRes.getInstance() .getStrByID("40040401", "UPPSCMCommon-000132"),
						NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000253"));
				return false;
			}

		}

		InvoiceVO willSavedVO = getSavedVO();
		if (willSavedVO == null) {
			showHintMessage(getHeadHintText() + "|" + NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000060") + "|");
			return false;
		}

		timer.addExecutePhase("得到需要保存的VO");

		ArrayList paraList = new ArrayList();
		paraList.add(null);
		paraList.add(null);

		ArrayList retList = null;

		InvoiceVO vos = null;
		try {
			if (paraList.size() != 3) {
				ArrayList forSettleList = new ArrayList();
				forSettleList.add(ClientEnvironment.getInstance().getDate());
				forSettleList.add(new UFBoolean(ClientEnvironment.getInstance()
						.isGroup()));
				String sKey = willSavedVO.getHeadVO().getCinvoiceid();
				if ((sKey == null) || (sKey.trim().length() == 0))
					forSettleList.add(new UFBoolean(false));
				else {
					forSettleList.add(new UFBoolean(true));
				}
				paraList.add(forSettleList);
			}

			if ((willSavedVO != null)
					&& (willSavedVO.getHeadVO() != null)
					&& (!this.isAllowedModifyByOther)
					&& (((((InvoiceHeaderVO) willSavedVO.getParentVO())
							.getIbillstatus() != BillStatus.AUDITING) || (((InvoiceHeaderVO) willSavedVO
							.getParentVO()).getIbillstatus() != BillStatus.AUDITFAIL)))) {
				((InvoiceHeaderVO) willSavedVO.getParentVO())
						.setCoperator(ClientEnvironment.getInstance().getUser()
								.getPrimaryKey());
			}
			((InvoiceHeaderVO) willSavedVO.getParentVO()).setCuserid(ClientEnvironment.getInstance().getUser().getPrimaryKey());
			
			((InvoiceHeaderVO) willSavedVO.getParentVO()).setCoperatoridnow(ClientEnvironment.getInstance().getUser().getPrimaryKey());

			paraList.add(new Integer(0));
			paraList.add("cvendormangid");

			InvoiceVO oldVO = getInvVOs()[getCurVOPos()];
			if (oldVO != null)
				paraList.add(oldVO);	
			else {
				paraList.add(null);
			}

			timer.addExecutePhase(NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000103"));

			String ntaxrate = getInvBillPanel().getHeadItem("ntaxrate").getValue();
			
			InvoiceItemVO[] tempVO = willSavedVO.getBodyVO();
			if (tempVO[0].getCupsourcebilltype() != null) {
				boolean bConfirm = false;
				willSavedVO.setUserConfirmFlag(new UFBoolean(false));
				do {
					if (willSavedVO.getUserConfirmFlag().booleanValue())
						break;
					try {
						if (bConfirm)
							willSavedVO.setUserConfirmFlag(new UFBoolean(true));
						retList = (ArrayList) PfUtilClient
								.processActionNoSendMessage(this, "SAVEBASE",
										"25", ClientEnvironment.getInstance()
												.getDate().toString(),
										willSavedVO, paraList, null, null);
					} catch (RwtPiToPoException ex) {
						SCMEnv.out(ex);
						if (MessageDialog
								.showYesNoDlg(this, NCLangRes.getInstance()
										.getStrByID("40040401",
												"UPPSCMCommon-000270"), ex
										.getMessage()) == 4) {
							bConfirm = true;
						} else
							return false;
					} catch (RwtPiToScException ex) {
						SCMEnv.out(ex);
						if (MessageDialog
								.showYesNoDlg(this, NCLangRes.getInstance()
										.getStrByID("40040401",
												"UPPSCMCommon-000270"), ex
										.getMessage()) == 4) {
							bConfirm = true;
						} else
							return false;
					}
				}

				while (bConfirm);
			} else {
				retList = (ArrayList) PfUtilClient.processActionNoSendMessage(
						this, "SAVEBASE", "25", ClientEnvironment.getInstance()
								.getDate().toString(), willSavedVO, paraList,
						null, null);
			}

			timer.addExecutePhase(NCLangRes.getInstance().getStrByID(
					"40040401", "UPP40040401-000029"));

			timer.addExecutePhase(NCLangRes.getInstance().getStrByID(
					"40040401", "UPP40040401-000104"));
			vos = (InvoiceVO) retList.get(1);

			((InvoiceHeaderVO) vos.getParentVO()).setDauditdate(vos.getHeadVO()
					.getDauditdate());
			((InvoiceHeaderVO) vos.getParentVO()).setCauditpsn(vos.getHeadVO()
					.getCauditpsn());
			((InvoiceHeaderVO) vos.getParentVO()).setIbillstatus(vos
					.getHeadVO().getIbillstatus());
			((InvoiceHeaderVO) vos.getParentVO())
					.setTs(vos.getHeadVO().getTs());

			UFDouble ntaxrateUF = null;
			if ((ntaxrate != null) && (ntaxrate.length() > 0)) {
				ntaxrateUF = new UFDouble(ntaxrate);
				((InvoiceHeaderVO) vos.getParentVO()).setNtaxrate(ntaxrateUF);
			}

			if (vos != null) {
				InvoiceVO[] tempVOs = getInvVOs();
				tempVOs[getCurVOPos()] = vos;
				setInvVOs(tempVOs);
			}
		} catch (ValidationException e) {
			SCMEnv.out(e);
			MessageDialog.showErrorDlg(this, NCLangRes.getInstance()
					.getStrByID("40040401", "UPPSCMCommon-000270"), e
					.getMessage());
			return false;
		} catch (Exception e) {
			SCMEnv.out(e);
			if ((e instanceof BusinessException)
					|| (e instanceof RemoteException)
					|| ((e.getMessage() != null) && (((e.getMessage().indexOf(
							NCLangRes.getInstance().getStrByID("common",
									"UC000-0000275")) >= 0)
							|| (e.getMessage().indexOf(
									NCLangRes.getInstance().getStrByID(
											"40040401", "UPP40040401-000165")) >= 0)
							|| (e.getMessage().indexOf(
									NCLangRes.getInstance().getStrByID(
											"40040401", "UPP40040401-000166")) >= 0)
							|| (e.getMessage().indexOf(
									NCLangRes.getInstance().getStrByID(
											"40040401", "UPP40040401-000110")) >= 0)
							|| (e.getMessage().indexOf(
									NCLangRes.getInstance().getStrByID(
											"40040401", "UPP40040401-000167")) >= 0) || (e
							.getMessage().indexOf(
									NCLangRes.getInstance().getStrByID(
											"40040401", "UPP40040401-000168")) >= 0))))) {
				MessageDialog.showErrorDlg(this, NCLangRes.getInstance()
						.getStrByID("40040401", "UPPSCMCommon-000270"), e
						.getMessage());
			} else
				MessageDialog.showErrorDlg(this, NCLangRes.getInstance()
						.getStrByID("40040401", "UPPSCMCommon-000270"),
						NCLangRes.getInstance().getStrByID("40040401",
								"UPP40040401-000064"));

			return false;
		}

		if (willSavedVO.getSource() == 0) {
			InvoiceVO resultVO = null;
			if (!PfUtilClient.isSuccess()) {
				removeOneFromInvVOs(getCurVOPos());

				setCurOperState(getBillBrowseState());
				setVOToBillPanel();
			} else {
				resultVO = vos;
				resultVO.setSource(4);
				getInvVOs()[getCurVOPos()] = resultVO;
				setCurOperState(getBillBrowseState());

				setVOToBillPanel();
			}
		} else if (willSavedVO.getSource() == 4) {
			InvoiceVO resultVO = null;
			if (!PfUtilClient.isSuccess()) {
				setCurOperState(getBillBrowseState());

				setVOToBillPanel();
			} else {
				resultVO = vos;
				getInvVOs()[getCurVOPos()] = resultVO;
				setCurOperState(getBillBrowseState());

				setVOToBillPanel();
			}

		} else {
			InvoiceVO resultVO = vos;
			resultVO.setSource(4);

			if (this.cunpos == 0)
				getInvVOs()[getCurVOPos()] = resultVO;
			else {
				getInvVOs()[this.cunpos] = resultVO;
			}
			if (this.m_vSavedVO == null)
				this.m_vSavedVO = new Vector();
			this.m_vSavedVO.addElement(resultVO);
			removeOneFromInvVOs(getCurVOPos());

			if ((getInvVOs() != null) && (getInvVOs().length > 0)) {
				setCurOperState(getListOperState());
				shiftShowModeTo(1);

				setSpiltVOsToListPanel();
			} else {
				setCurOperState(getBillBrowseState());
				setListOperState(4);

				InvoiceVO[] tempVO = new InvoiceVO[this.m_vSavedVO.size()];
				this.m_vSavedVO.copyInto(tempVO);
				setInvVOs(tempVO);
				setCurVOPos(tempVO.length - 1);
				this.m_vSavedVO = null;

				setVOToBillPanel();
			}
			this.cunpos = 0;
		}
		
		
		timer.addExecutePhase("保存后的显示操作");
		
		/*
		 * 
		 * 发票保存后将核销记录表的预付余额显示到界面上
		 */
		//获取预付款金额
		IUAPQueryBS  uap = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
//		Object cinvoiceid = getInvBillPanel().getHeadItem("cinvoiceid").getValueObject();//发票ID
//		String val ="select  pod.vdef17  from  po_invoice_b pob inner join po_order pod   on pod.corderid = pob.corderid  where pob.cinvoiceid ='"+cinvoiceid+"'";
		
		
		List<String> list = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		Object vinvoicecode = getInvBillPanel().getHeadItem("vinvoicecode").getValueObject();
		Object corp = getInvBillPanel().getHeadItem("pk_corp").getValueObject();
		sql.append("select 	hxmoney,yfbalance from po_check where dr=0 and pk_corp='"+corp+"' and vinvoicecode ='"+vinvoicecode+"' ORDER BY TS  desc, CINVOICEID desc");
    	      	
		try {
			list =   (List<String>) uap.executeQuery(sql.toString(), new MapListProcessor());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(list!=null&&list.size()>0){
			JSONObject js1 = new JSONObject();
			js1 = JSONObject.fromObject(list.get(0));
			String hxmoney = js1.getString("hxmoney").toString();
			System.out.println("hemoney"+hxmoney);
			String yfbalance = (String) js1.getString("yfbalance");
			UFDouble hx =new UFDouble(hxmoney);
			UFDouble yf =new UFDouble(yfbalance);
			getInvBillPanel().setHeadItem("cvoucherid", hx.doubleValue());
			getInvBillPanel().setHeadItem("cfreecustid", yf.doubleValue()); //设置界面值
		}
		
		
		timer.showAllExecutePhase("采购发票保存结束");
		/**
		 * start pjj 2018年1月26日14:34:23
		 * 反馈实际抵扣金额
		 */
		String pk_corp = willSavedVO.getHeadVO().getPk_corp();
		if (pk_corp.equals("1016") || pk_corp.equals("1071")
				|| pk_corp.equals("1103") || pk_corp.equals("1097")
				|| pk_corp.equals("1017") || pk_corp.equals("1018")|| pk_corp.equals("1019")
				|| pk_corp.equals("1107")) {
			StringBuffer sb = new StringBuffer();
			String cvmjson = new String();
			for (int a = 0 ;a<willSavedVO.getChildrenVO().length;a++){
				cvmjson = willSavedVO.getChildrenVO()[a].getAttributeValue("b_cjje1")==null?"":willSavedVO.getChildrenVO()[a].getAttributeValue("b_cjje1").toString();
				if(cvmjson.length()<=0){
	        		  continue;
	        	  }else{
	        		  sb.append(cvmjson);
	        	  }
	         }
			if(sb.length()>0){
				this.invoice(willSavedVO);
			}
		}
		
		
		/**
		 * end 彭佳佳  2018年6月15日16:32:09
		 */
		
		this.m_bAdd = false;
		this.m_bCopy = false;
		return true;
		
	}


	/**
	 * 调用cvm采购发票接口来获取抵扣总金额
	 * add by 彭佳佳 2018年4月3日12:39:42
	 */
	
	   private String  contorService(String custid,String pk_corp,String PrimaryKey){
			String  resutl = null;
			IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			StringBuffer sql =new StringBuffer();
			//查询客商信息
		 sql.append("     select cubas.custcode,corp.unitcode  ") 
			.append("       from bd_cumandoc cuman ") 
			.append("       join bd_cubasdoc cubas ") 
			.append("         on cuman.pk_cubasdoc = cubas.pk_cubasdoc ") 
			.append("       left join bd_corp corp  ") 
			.append("         on cuman.pk_corp = corp.pk_corp  ") 
			.append("       where cubas.pk_cubasdoc ='"+custid+"' and cuman.pk_corp ='"+pk_corp+"' ")
			.append("	    and nvl(cubas.dr,0) = 0 and corp.pk_corp = '"+pk_corp+"'");
		 //接收cvm抵扣总金额
		    String dkje = null;
			Map custMap = new HashMap();
			try {
				List custList = (List) bs.executeQuery(sql.toString(),new MapListProcessor());
				if(custList.size()>0){
					custMap = (Map) custList.get(0);
				}
				Map JsonMap = new HashMap();
				JsonMap.put("username", "baosteel");
				JsonMap.put("password", "123456");
				JsonMap.put("corp", (String)custMap.get("unitcode"));
				JsonMap.put("kscode", (String)custMap.get("custcode"));
				JsonMap.put("ncpk", PrimaryKey);
				Gson gson = new Gson();
				Object ss = gson.toJson(JsonMap);
				StringBuffer jsonsb = new StringBuffer();
				jsonsb.append("[").append(ss).append("]");
				URL url;
				try {
					//调用CVM HTTP接口
					url = new URL("http://10.70.26.23/cvm/CVMService/NC005");
				    resutl = this.httpconnect(url, jsonsb);
				}catch (Exception e){
					e.printStackTrace();
					return "[{\"status\":\"error\",\"message\":\"连接CVM接口异常(URL协议)'"+e.getMessage()+"'\"}]";
				}
			} catch (BusinessException e) {
				e.printStackTrace();
				return "[{\"status\":\"error\",\"message\":\"数据库查询异常'"+e.getMessage()+"'\"}]";
			}
			return resutl;
		}
	   /**
	    * 反馈采购发票实际抵扣额实现方法回传给cvm
	    * @param Cinvoiceid
	    * @return
	    */
	   private String invoice(InvoiceVO vos) {
		   IUAPQueryBS bs = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		   InvoiceHeaderVO hvo = (InvoiceHeaderVO) vos.getParentVO();
		   InvoiceItemVO[] bvo = (InvoiceItemVO[]) vos.getChildrenVO();
		   String corp  = hvo.getPk_corp();
		   String cinvoiceid = hvo.getCinvoiceid();
		   UFDouble cjje3 = new UFDouble(0);//实际抵扣金额
		   String CVMjsonstr = new String();  //cvm签呈json
		   String mess = new String();
		 //获取表体数据
			for(int i =0;i<bvo.length;i++){
				CVMjsonstr = bvo[i].getB_cjje1();
				//cvm签呈金额
				UFDouble cvmdkje =new UFDouble(bvo[i].getB_cjje3()==null?"0":bvo[i].getB_cjje3());
				cjje3 = cjje3.add(cvmdkje);
			}
			StringBuffer sb = new StringBuffer();
			sb.append("  select corp.unitcode,cubas.custcode,inv.vinvoicecode  ") 
			.append("          from po_invoice_b invb    ") 
			.append("          join po_invoice inv    ") 
			.append("          on invb.cinvoiceid = inv.cinvoiceid   ") 
			.append("          left join bd_cumandoc cuman    ") 
			.append("          on cuman.pk_cumandoc = inv.cvendormangid    ") 
			.append("          left join bd_cubasdoc cubas   ") 
			.append("          on cuman.pk_cubasdoc = cubas.pk_cubasdoc  ") 
			.append("          left join bd_corp corp ") 
			.append("          on inv.pk_corp = corp.pk_corp   ") 
			.append("          where inv.cinvoiceid  ='"+cinvoiceid+"' and inv.pk_corp ='"+corp+"' and nvl(inv.dr,0)=0 ") ;
			Map JsonMap = new HashMap();
			try {
				
				List list = null;
				try {
					list = (List) bs.executeQuery(sb.toString(), new MapListProcessor());
					if(list.size()>0){
						JsonMap = (Map) list.get(0);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				String gongsi = (String) JsonMap.get("unitcode");
				String keshang =(String) JsonMap.get("custcode");
				String vinvoicecode = (String) JsonMap.get("vinvoicecode");
				JSONObject jsonObj = new JSONObject();
				Map body = new HashMap();
				JSONArray  reqjsonArr = new JSONArray();
				JSONArray  jsonArr = new JSONArray();
				JSONArray bodyArr = new JSONArray();
				jsonArr = JSONArray.fromObject(CVMjsonstr);
				Map map = new HashMap();
				for(int a = 0;a<jsonArr.size();a++){
					map = (Map) jsonArr.get(a);
					//签呈编码
					String qcbh = map.get("qcbm")==null?"":map.get("qcbm").toString();
					//签呈金额
					String qcje = map.get("sjdkje")==null?"0":map.get("sjdkje").toString();
					if(cjje3.sub(new UFDouble(qcje)).doubleValue()>=0){
						jsonObj.put("username", "baosteel");
						jsonObj.put("password", "123456");
						jsonObj.put("gscode", gongsi);
						jsonObj.put("kscode", keshang);
						jsonObj.put("ncpk", cinvoiceid);
						jsonObj.put("ncbh", vinvoicecode);
						body.put("qcbh", qcbh);
						body.put("dkje", qcje);
						bodyArr.add(body);
						cjje3 = cjje3.sub(new UFDouble(qcje));
					}
				}
				jsonObj.put("bodylist", bodyArr);
				reqjsonArr.add(jsonObj);
				//调用CVM接口反馈实际折扣金额
				URL url = new URL("http://10.70.26.23/cvm/CVMService/NC006");
				mess = this.httpconnect(url, reqjsonArr);
			} catch (DAOException e) {
				e.printStackTrace();
				return "[{\"status\":\"error\",\"message\":\"数据库查询错误'"+e.getMessage()+"'\"}]";
			}catch(MalformedURLException e){
				e.printStackTrace();
				return "[{\"status\":\"error\",\"message\":\"连接CVM接口异常(URL协议)'"+e.getMessage()+"'\"}]";
			}catch(Exception e){
				e.printStackTrace();
				return "[{\"status\":\"error\",\"message\":\"连接CVM接口异常(URL协议)'"+e.getMessage()+"'\"}]";
			}
			return mess;
		}
	   /**
        * 调用cvm接口
        * @param url
        * @param json
        * @return
        * @throws Exception
        */
		private String httpconnect(URL url,Object json) throws Exception {
		        //创建HTTP链接
		        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
		    
		        //设置请求的方法类型
		        httpURLConnection.setRequestMethod("POST");
		        
		        //设置请求的内容类型
		        httpURLConnection.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
		        
		        //设置发送数据
		        httpURLConnection.setDoOutput(true);
		        //设置接受数据
		        httpURLConnection.setDoInput(true);
		        
		        //发送数据,使用输出流
		        OutputStream outputStream = httpURLConnection.getOutputStream();
		        //发送的soap协议的数据
		        String content = "jsonData="+URLEncoder.encode(json.toString(), "UTF-8");
		        //发送数据
		        outputStream.write(content.getBytes());
		    
		        //接收数据
		        InputStream inputStream = httpURLConnection.getInputStream();
		        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));  
		        StringBuffer buffer = new StringBuffer();  
		        String line = "";  
		        while ((line = in.readLine()) != null){  
		          buffer.append(line);  
		        }  
		        String reustle =null;
		        reustle = buffer.toString();
		        in.close();
		        //解析CVM返回JSON数据
		        return reustle;
		  }
		
	/** @deprecated */
	private void onSelectAll() {
		int iLen = getInvVOs().length;

		getInvListPanel().getHeadTable().setRowSelectionInterval(0, iLen - 1);
	}

	/** @deprecated */
	private void onSelectNo() {
		int iLen = getInvVOs().length;

		getInvListPanel().getHeadTable()
				.removeRowSelectionInterval(0, iLen - 1);
	}

	private void onSendAudit(InvoiceVO vo) {
		String billid = null;
		if ((vo != null) && (vo.getHeadVO() != null)) {
			billid = vo.getHeadVO().getCinvoiceid();
		}

		boolean isNeedSendToAuditQ = BusiBillManageTool.isNeedSendToAudit("25",
				getPk_corp(), getCurBizeType(), billid, getCurOperator());
		try {
			if ((vo == null) && (this.m_btnBillMaintain.isEnabled() == true)) {
				boolean bContinue = onSave();
				if (!bContinue) {
					return;
				}
				vo = getInvVOs()[getCurVOPos()];
			} else if ((getCurOperState() == 1)
					&& (isNeedSendToAuditQ)
					&& (vo.getHeadVO().getIbillstatus() != null)
					&& (((vo.getHeadVO().getIbillstatus().intValue() == 0) || (vo
							.getHeadVO().getIbillstatus().intValue() == 4)))) {
				boolean bContinue = onSave();
				if (!bContinue) {
					return;
				}
				vo = getInvVOs()[getCurVOPos()];
			}

			if ((isNeedSendToAuditQ)
					&& (vo.getHeadVO().getIbillstatus() != null)
					&& (((vo.getHeadVO().getIbillstatus().intValue() == 0) || (vo
							.getHeadVO().getIbillstatus().intValue() == 4)))) {
				ArrayList retList = (ArrayList) PfUtilClient.processAction(
						"SAVE", "25", ClientEnvironment.getInstance().getDate()
								.toString(), vo);

				InvoiceVO resultVO = null;
				if (PfUtilClient.isSuccess()) {
					resultVO = InvoiceHelper
							.findByPrimaryKey(getInvVOs()[getCurVOPos()]
									.getHeadVO().getPrimaryKey());
					getInvVOs()[getCurVOPos()] = resultVO;
				}

			}

			this.m_btnSendAudit.setEnabled(false);
			this.m_btnInvBillAudit.setEnabled(false);
			this.m_btnInvBillUnAudit.setEnabled(true);
		} catch (Exception e) {
			SCMEnv.out(e);
			MessageDialog
					.showErrorDlg(this, NCLangRes.getInstance().getStrByID(
							"40040401", "UPPSCMCommon-000270"), NCLangRes
							.getInstance().getStrByID("40040401",
									"UPP40040401-000064"));
		}
	}

	private int removeOneFromInvVOs(int removedPos) {
		if ((removedPos < 0) || (removedPos >= getInvVOs().length)) {
			return -1;
		}
		if (getInvVOs().length == 1) {
			setInvVOs(null);
			setCurVOPos(-1);
			return -1;
		}
		InvoiceVO[] vos = new InvoiceVO[getInvVOs().length - 1];
		int j = 0;
		for (int i = 0; i < getInvVOs().length; ++i) {
			if (i != removedPos)
				vos[(j++)] = getInvVOs()[i];
		}
		setInvVOs(vos);

		if (removedPos == getInvVOs().length) {
			setCurVOPos(getInvVOs().length - 1);
			return 0;
		}
		setCurVOPos(removedPos);
		return removedPos;
	}

	private int removeSomeFromInvVOs(Integer[] removedPos) {
		if (removedPos == null) {
			return 0;
		}

		Hashtable hashRevIndex = new Hashtable();
		for (int i = 0; i < removedPos.length; ++i) {
			hashRevIndex.put(removedPos[i], "");
		}

		Vector reservedVEC = new Vector();
		for (int i = 0; i < getInvVOs().length; ++i) {
			if (!hashRevIndex.containsKey(new Integer(i))) {
				reservedVEC.addElement(getInvVOs()[i]);
			}
		}

		if (reservedVEC.size() == 0) {
			setInvVOs(null);
			return -1;
		}

		InvoiceVO[] vos = new InvoiceVO[reservedVEC.size()];
		reservedVEC.copyInto(vos);
		setInvVOs(vos);
		setCurVOPos(0);
		return 0;
	}

	private void setBillBrowseState(int newBillBrowseState) {
		this.m_nBillBrowseState = newBillBrowseState;
	}

	private void setButtonsAndPanelState() {
		int iVal = -999;
		boolean isNeedSendToAuditQ = false;
		if (getCurOperState() == 0) {
			setButtonsStateInit();
			getInvBillPanel().setEnabled(false);
			iVal = 0;
		} else if (getCurOperState() == 2) {
			isNeedSendToAuditQ = setSendAuditBtnState();
			setButtonsStateBrowseNormal();
			Integer iBillStatus = new Integer(88);
			String billid = null;
			if (getInvVOs() != null) {
				InvoiceVO vo = getInvVOs()[getCurVOPos()];

				if ((vo != null) && (vo.getHeadVO() != null)) {
					iBillStatus = vo.getHeadVO().getIbillstatus();
					billid = vo.getHeadVO().getCinvoiceid();
				}

			}

			if ((!isNeedSendToAuditQ)
					&& (iBillStatus.compareTo(BillStatus.AUDITING) == 0)) {
				this.m_btnInvBillAudit.setEnabled(true);
				this.m_btnInvBillUnAudit.setEnabled(false);
			}

			getInvBillPanel().setEnabled(false);
			iVal = 1;
			if ((getInvVOs() == null) || (getInvVOs().length == 0))
				setButtonsStateInit();
		} else if (getCurOperState() == 1) {
			setButtonsStateEdit();

			getInvBillPanel().setEnabled(true);

			Integer iBillStatus = new Integer(88);
			String cbizType = null;
			String billid = null;
			if (getInvVOs() != null) {
				InvoiceVO vo = getInvVOs()[getCurVOPos()];
				if ((vo != null) && (vo.getHeadVO() != null)) {
					iBillStatus = vo.getHeadVO().getIbillstatus();
					cbizType = vo.getHeadVO().getCbiztype();
					billid = vo.getHeadVO().getCinvoiceid();
				}
			}
			if (this.m_bAdd) {
				isNeedSendToAuditQ = BusiBillManageTool.isNeedSendToAudit("25",
						getPk_corp(), getCurBizeType(), null,
						getClientEnvironment().getUser().getPrimaryKey());
			} else {
				isNeedSendToAuditQ = setSendAuditBtnState();
			}

			if ((this.m_bAdd) || (iBillStatus.compareTo(BillStatus.FREE) == 0)
					|| (iBillStatus.compareTo(BillStatus.AUDITFAIL) == 0)) {
				this.m_btnSendAudit.setEnabled(isNeedSendToAuditQ);
			}

			getInvBillPanel().getBodyItem("nmoney").setEnabled(false);
			getInvBillPanel().getBodyItem("ntaxmny").setEnabled(false);
			getInvBillPanel().getBodyItem("nsummny").setEnabled(false);
			this.m_btnAudit.setEnabled(false);

			setEditableWhenEdit();
			iVal = 2;
		} else if (getCurOperState() == 4) {
			setButtonsStateListNormal();
			getInvBillPanel().setEnabled(false);
			setButtonsStateBrowseNormal();
			iVal = 3;
		} else if (getCurOperState() == 5) {
			setButtonsStateListFromBills();
			getInvBillPanel().setEnabled(false);
			iVal = 4;
		}
		setExtendBtnsStat(iVal);
	}

	private void setButtonsStateBrowseNormal() {
		if (getCurPanelMode() == 0) {
			this.m_btnInvBillBusiType.setEnabled(true);
			this.m_btnInvSelectAll.setEnabled(false);
			this.m_btnInvDeselectAll.setEnabled(false);
			this.m_btnInvBillConversion.setEnabled(false);
			this.m_btnInvShift.setName(NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000464"));
		} else if (getCurPanelMode() == 1) {
			this.m_btnInvBillBusiType.setEnabled(false);
			this.m_btnInvSelectAll.setEnabled(true);
			this.m_btnInvDeselectAll.setEnabled(true);
			this.m_btnInvShift.setName(NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000463"));
		}

		this.m_btnInvBillExcute.setEnabled(true);

		if ((getCurPanelMode() == 1)
				|| (!this.m_btnInvBillBusiType.isVisible())
				|| (getCurBizeType() == null)) {
			this.m_btnInvBillNew.setEnabled(false);
		} else if (getCurPanelMode() == 0) {
			this.m_btnInvBillNew.setEnabled(true);
		}

		this.m_btnZiLaoW.setEnabled(false);// add by cm
		this.m_btnZiNumW.setEnabled(false);// add by cm
		
		this.m_btnPiGai.setEnabled(false);//add by danxionghui
		this.m_btnAllGai.setEnabled(false);//add by danxionghui

		this.m_btnInvBillSave.setEnabled(false);

		this.m_btnInvBillCancel.setEnabled(false);

		this.m_btnInvBillRowOperation.setEnabled(false);

		this.m_btnInvBillPrint.setEnabled(true);

		this.m_btnInvBillQuery.setEnabled(true);

		this.m_btnInvShift.setEnabled(true);

		this.m_btnHqhp.setEnabled(false);

		if (isEverQueryed())
			this.m_btnInvBillRefresh.setEnabled(true);
		else {
			this.m_btnInvBillRefresh.setEnabled(false);
		}

		this.m_btnQueryForAudit.setEnabled(true);

		this.btnBillCombin.setEnabled(true);

		if (getInvVOs() == null) {
			this.m_btnInvBillDiscard.setEnabled(false);

			this.m_btnInvBillModify.setEnabled(false);

			this.m_btnInvBillCopy.setEnabled(false);

			this.m_btnInvBillGoFirstOne.setEnabled(false);
			this.m_btnInvBillGoPreviousOne.setEnabled(false);
			this.m_btnInvBillGoNextOne.setEnabled(false);
			this.m_btnInvBillGoLastOne.setEnabled(false);

			this.m_btnDocManage.setEnabled(false);

			this.m_btnLnkQuery.setEnabled(false);
		} else {
			this.m_btnLnkQuery.setEnabled(true);

			this.m_btnDocManage.setEnabled(true);

			if ((getInvVOs() != null)
					&& (getInvVOs().length > 0)
					&& (getCurVOPos() > -1)
					&& (isCouldMakedForABizType(getInvVOs()[getCurVOPos()]
							.getHeadVO().getCbiztype()))
					&& (!getInvVOs()[getCurVOPos()].isVirtual())
					&& (getCurPanelMode() != 1)) {
				this.m_btnInvBillCopy.setEnabled(true);
			} else
				this.m_btnInvBillCopy.setEnabled(false);

			Integer iBillStatus = Integer.valueOf(-1);
			if (getInvVOs() != null) {
				InvoiceVO vo = getInvVOs()[getCurVOPos()];

				if ((vo != null) && (vo.getHeadVO() != null)) {
					iBillStatus = vo.getHeadVO().getIbillstatus();
				}
			}
			boolean isNeedSendToAuditQ = isNeedSendAudit(iBillStatus.intValue());

			if (getCurVOPos() != -1) {
				int intBillStatus = getInvVOs()[getCurVOPos()].getHeadVO()
						.getIbillstatus().intValue();
				if (intBillStatus == 0) {
					if (this.m_btnSendAudit.isEnabled()) {
						this.m_btnInvBillAudit.setEnabled(false);
						this.m_btnInvBillUnAudit.setEnabled(false);
					} else if (this.m_btnInvBillAudit.isEnabled()) {
						this.m_btnInvBillUnAudit.setEnabled(false);
					} else if (this.m_btnInvBillUnAudit.isEnabled()) {
						this.m_btnInvBillAudit.setEnabled(false);
					} else if (!this.m_btnInvBillUnAudit.isEnabled()) {
						this.m_btnInvBillAudit.setEnabled(true);
					}

					if ((iBillStatus.compareTo(BillStatus.FREE) == 0)
							|| (iBillStatus.compareTo(BillStatus.AUDITFAIL) == 0)) {
						if (isNeedSendToAuditQ) {
							this.m_btnSendAudit.setEnabled(true);
						} else {
							this.m_btnSendAudit.setEnabled(false);
							this.m_btnInvBillAudit.setEnabled(true);
							this.m_btnInvBillUnAudit.setEnabled(false);
							this.m_btnInvBillModify.setEnabled(true);
							this.m_btnInvBillDiscard.setEnabled(true);
						}
					}
					this.m_btnInvBillModify.setEnabled(true);
					this.m_btnInvBillDiscard.setEnabled(true);
				} else if ((intBillStatus == 0) || (intBillStatus == 4)) {
					this.m_btnInvBillAudit.setEnabled(true);
					this.m_btnInvBillUnAudit.setEnabled(false);
					this.m_btnInvBillModify.setEnabled(true);
					this.m_btnInvBillDiscard.setEnabled(true);
				} else if (intBillStatus == 2) {
					this.m_btnInvBillAudit.setEnabled(true);
					this.m_btnInvBillUnAudit.setEnabled(false);
				} else if (intBillStatus == 3) {
					this.m_btnInvBillAudit.setEnabled(false);
					this.m_btnInvBillUnAudit.setEnabled(true);
					this.m_btnInvBillModify.setEnabled(false);
					this.m_btnInvBillDiscard.setEnabled(false);
				}

			}

			this.m_btnInvBillModify.setEnabled(true);
			this.m_btnInvBillDiscard.setEnabled(true);

			if (getCurVOPos() < 0) {
				this.m_btnInvBillModify.setEnabled(false);
				return;
			}

			double d1 = 0.0D;
			InvoiceItemVO[] bodyVO = getInvVOs()[getCurVOPos()].getBodyVO();
			if ((bodyVO != null) && (bodyVO.length > 0)) {
				for (int i = 0; i < bodyVO.length; ++i) {
					d1 = 0.0D;
					if (getInvVOs()[getCurVOPos()].getBodyVO()[i]
							.getNaccumsettmny() != null) {
						d1 = getInvVOs()[getCurVOPos()].getBodyVO()[i]
								.getNaccumsettmny().doubleValue();
					}
					if (d1 != 0.0D) {
						this.m_btnInvBillModify.setEnabled(false);
						this.m_btnInvBillDiscard.setEnabled(false);
						break;
					}
				}
			}

			if ((getInvVOs()[getCurVOPos()].getHeadVO().getCinvoiceid() == null)
					|| (getInvVOs()[getCurVOPos()].getHeadVO().getCinvoiceid()
							.trim().length() < 1)) {
				this.m_btnInvBillDiscard.setEnabled(false);
			}

			Integer iStatus = getInvVOs()[getCurVOPos()].getHeadVO()
					.getIbillstatus();
			if ((iStatus != null) && (iStatus.compareTo(BillStatus.FREE) != 0)
					&& (iStatus.compareTo(BillStatus.AUDITFAIL) != 0)) {
				this.m_btnInvBillModify.setEnabled(false);
				this.m_btnInvBillDiscard.setEnabled(false);
			}

			if (getInvVOs()[getCurVOPos()].isVirtual()) {
				this.m_btnInvBillModify.setEnabled(false);
				this.m_btnInvBillDiscard.setEnabled(false);
			}

			if (getInvVOs().length == 1) {
				this.m_btnInvBillGoFirstOne.setEnabled(false);
				this.m_btnInvBillGoPreviousOne.setEnabled(false);
				this.m_btnInvBillGoNextOne.setEnabled(false);
				this.m_btnInvBillGoLastOne.setEnabled(false);
			} else if (getCurVOPos() == 0) {
				this.m_btnInvBillGoFirstOne.setEnabled(false);
				this.m_btnInvBillGoPreviousOne.setEnabled(false);
				this.m_btnInvBillGoNextOne.setEnabled(true);
				this.m_btnInvBillGoLastOne.setEnabled(true);
			} else if (getCurVOPos() == getInvVOs().length - 1) {
				this.m_btnInvBillGoFirstOne.setEnabled(true);
				this.m_btnInvBillGoPreviousOne.setEnabled(true);
				this.m_btnInvBillGoNextOne.setEnabled(false);
				this.m_btnInvBillGoLastOne.setEnabled(false);
			} else {
				this.m_btnInvBillGoFirstOne.setEnabled(true);
				this.m_btnInvBillGoPreviousOne.setEnabled(true);
				this.m_btnInvBillGoNextOne.setEnabled(true);
				this.m_btnInvBillGoLastOne.setEnabled(true);
			}
		}

		setButtonsList();
	}

	/** @deprecated */
	private void setButtonsStateEdit() {
		this.m_btnInvBillBusiType.setEnabled(false);

		this.m_btnInvBillNew.setEnabled(false);

		this.m_btnInvBillModify.setEnabled(false);

		this.m_btnInvBillDiscard.setEnabled(false);

		this.m_btnZiLaoW.setEnabled(true);// add by cm
		this.m_btnZiNumW.setEnabled(true);// add by cm

		this.m_btnPiGai.setEnabled(true);//add by danxionghui
		this.m_btnAllGai.setEnabled(true);//add by danxionghui
		
		this.m_btnInvBillSave.setEnabled(true);

		this.m_btnInvBillCopy.setEnabled(false);

		this.m_btnInvBillCancel.setEnabled(true);

		this.m_btnInvBillAudit.setEnabled(false);

		this.m_btnInvBillUnAudit.setEnabled(false);

		this.m_btnInvBillRowOperation.setEnabled(true);

		setCouldMaked(isCouldMakedForABizType(getCurBizeType()));

		this.m_btnInvBillAddRow.setEnabled(isCouldMaked());
		this.m_btnInvBillCopyRow.setEnabled(isCouldMaked());
		this.m_btnInvBillPasteRow.setEnabled(isCouldMaked());
		this.m_btnInvBillInsertRow.setEnabled(isCouldMaked());

		if (getInvBillPanel().getRowCount() <= 0)
			this.m_btnInvBillDeleteRow.setEnabled(false);
		else {
			this.m_btnInvBillDeleteRow.setEnabled(true);
		}

		this.m_btnInvBillPrint.setEnabled(false);

		this.m_btnInvBillQuery.setEnabled(false);

		this.m_btnInvShift.setEnabled(false);

		this.m_btnInvBillGoFirstOne.setEnabled(false);
		this.m_btnInvBillGoPreviousOne.setEnabled(false);
		this.m_btnInvBillGoNextOne.setEnabled(false);
		this.m_btnInvBillGoLastOne.setEnabled(false);

		if (this.m_bAdd) {
			this.m_btnInvSelectAll.setEnabled(false);
			this.m_btnInvDeselectAll.setEnabled(false);
		} else {
			this.m_btnInvSelectAll.setEnabled(true);
			this.m_btnInvDeselectAll.setEnabled(true);
			this.m_btnInvBillAudit.setEnabled(false);
			this.m_btnInvBillAssist.setEnabled(true);
			this.m_btnInvBillRefresh.setEnabled(false);
			this.btnBillCombin.setEnabled(false);
			this.m_btnQueryForAudit.setEnabled(false);
			this.m_btnInvBillPrint.setEnabled(false);
			this.m_btnInvBillPreview.setEnabled(false);
			this.m_btnLnkQuery.setEnabled(false);
			this.m_btnDocManage.setEnabled(false);
			this.m_btnHqhp.setEnabled(true);
		}

		this.m_btnInvBillConversion.setEnabled(false);

		Integer iBillStatus = Integer.valueOf(-1);
		if (getInvVOs() != null) {
			InvoiceVO vo = getInvVOs()[getCurVOPos()];

			if ((vo != null) && (vo.getHeadVO() != null)) {
				iBillStatus = vo.getHeadVO().getIbillstatus();
			}
		}
		boolean isNeedSendToAuditQ = isNeedSendAudit(iBillStatus.intValue());

		if ((!isNeedSendToAuditQ)
				|| ((iBillStatus.compareTo(BillStatus.FREE) != 0) && (iBillStatus
						.compareTo(BillStatus.AUDITFAIL) != 0))) {
			return;
		}
		this.m_btnSendAudit.setEnabled(true);
	}

	/** @deprecated */
	private void setButtonsStateInit() {
		this.m_btnInvBillCopy.setEnabled(false);

		this.m_btnInvBillRefresh.setEnabled(false);
		this.m_btnLnkQuery.setEnabled(false);

		if (getCurPanelMode() == 0) {
			this.m_btnInvBillBusiType.setEnabled(true);
		} else if (getCurPanelMode() == 1) {
			this.m_btnInvBillBusiType.setEnabled(false);
		}

		if ((getCurPanelMode() == 1)
				|| (!this.m_btnInvBillBusiType.isVisible())
				|| (getCurBizeType() == null)) {
			this.m_btnInvBillNew.setEnabled(false);
		} else if (getCurPanelMode() == 0) {
			this.m_btnInvBillNew.setEnabled(true);
		}

		this.m_btnInvBillModify.setEnabled(false);

		this.m_btnZiLaoW.setEnabled(false);// add by cm
		this.m_btnZiNumW.setEnabled(false);// add by cm
		this.m_btnPiGai.setEnabled(false);//add by danxionghui
		this.m_btnAllGai.setEnabled(false);//add by danxionghui

		this.m_btnInvBillSave.setEnabled(false);

		this.m_btnInvBillDiscard.setEnabled(false);

		this.m_btnInvBillCancel.setEnabled(false);

		this.m_btnInvBillAudit.setEnabled(false);

		this.m_btnInvBillUnAudit.setEnabled(false);

		this.m_btnInvBillRowOperation.setEnabled(false);

		this.m_btnHqhp.setEnabled(false);

		this.m_btnInvBillQuery.setEnabled(true);

		this.m_btnInvShift.setEnabled(true);

		this.m_btnInvBillGoFirstOne.setEnabled(false);
		this.m_btnInvBillGoPreviousOne.setEnabled(false);
		this.m_btnInvBillGoNextOne.setEnabled(false);
		this.m_btnInvBillGoLastOne.setEnabled(false);

		this.m_btnDocManage.setEnabled(false);

		this.m_btnSendAudit.setEnabled(false);

		this.btnBillCombin.setEnabled(false);

		this.m_btnQueryForAudit.setEnabled(false);
	}

	private void setButtonsList() {
		if ((getInvVOs() != null) && (getInvVOs().length > 0)) {
			this.m_btnInvBillPreview.setEnabled(true);
			this.m_btnInvBillPrint.setEnabled(true);
			this.btnBillCombin.setEnabled(true);
		} else {
			this.m_btnInvBillPreview.setEnabled(false);
			this.m_btnInvBillPrint.setEnabled(false);
			this.btnBillCombin.setEnabled(false);
			this.m_btnInvBillAudit.setEnabled(false);
		}

		if (getCurPanelMode() == 0)
			return;
		if (getCurPanelMode() == 1) {
			this.m_btnInvBillGoFirstOne.setEnabled(false);
			this.m_btnInvBillGoNextOne.setEnabled(false);
			this.m_btnInvBillGoPreviousOne.setEnabled(false);
			this.m_btnInvBillGoLastOne.setEnabled(false);
		}
	}

	/** @deprecated */
	private void setButtonsStateListFromBills() {
		if (getSelectedRowCount() == 0) {
			this.m_btnInvBillModify.setEnabled(false);
			this.m_btnInvDeselectAll.setEnabled(false);

			if ((getInvVOs() != null) && (getInvVOs().length > 0))
				this.m_btnInvSelectAll.setEnabled(true);
			else
				this.m_btnInvSelectAll.setEnabled(false);
		} else {
			if (getSelectedRowCount() == 1)
				this.m_btnInvBillModify.setEnabled(true);
			else {
				this.m_btnInvBillModify.setEnabled(false);
			}

			this.m_btnInvDeselectAll.setEnabled(true);

			if ((getInvVOs() != null)
					&& (getInvVOs().length > getSelectedRowCount()))
				this.m_btnInvSelectAll.setEnabled(true);
			else {
				this.m_btnInvSelectAll.setEnabled(false);
			}
		}

		this.m_btnInvBillBusiType.setEnabled(false);
		this.m_btnInvBillNew.setEnabled(false);

		this.m_btnZiLaoW.setEnabled(false);// add by cm
		this.m_btnZiNumW.setEnabled(false);// add by cm
		
		this.m_btnPiGai.setEnabled(false);// add by danxionghui
		this.m_btnAllGai.setEnabled(false);//add by danxionghui

		this.m_btnInvBillConversion.setEnabled(true);

		this.m_btnInvBillAudit.setEnabled(false);

		this.m_btnSendAudit.setEnabled(false);

		this.m_btnInvBillQuery.setEnabled(false);

		this.m_btnInvBillDiscard.setEnabled(false);

		this.m_btnInvBillRefresh.setEnabled(false);

		this.m_btnInvShift.setEnabled(false);

		this.m_btnDocManage.setEnabled(false);

		this.m_btnInvBillCancel.setEnabled(false);

		this.m_btnInvBillCopy.setEnabled(false);

		this.m_btnInvBillSave.setEnabled(false);
	}

	/** @deprecated */
	private void setButtonsStateListNormal() {
		if (getInvVOs() == null) {
			this.m_btnInvSelectAll.setEnabled(false);
			this.m_btnInvDeselectAll.setEnabled(false);
			this.m_btnInvBillDiscard.setEnabled(false);
			this.m_btnInvBillModify.setEnabled(false);
			this.m_btnInvShift.setEnabled(true);
			this.m_btnDocManage.setEnabled(false);
			this.m_btnQueryForAudit.setEnabled(false);
		} else if (getSelectedRowCount() == 0) {
			this.m_btnInvSelectAll.setEnabled(true);
			this.m_btnInvDeselectAll.setEnabled(false);
			this.m_btnInvBillDiscard.setEnabled(false);
			this.m_btnInvBillModify.setEnabled(false);
			this.m_btnInvShift.setEnabled(false);
			this.m_btnDocManage.setEnabled(false);
			this.m_btnQueryForAudit.setEnabled(false);
		} else {
			this.m_btnDocManage.setEnabled(true);
			this.m_btnInvDeselectAll.setEnabled(true);

			if (getSelectedRowCount() == getInvVOs().length)
				this.m_btnInvSelectAll.setEnabled(false);
			else {
				this.m_btnInvSelectAll.setEnabled(true);
			}

			boolean bModify = true;
			if ((getSelectedRowCount() == 1)
					&& (getInvVOs()[getCurVOPos()].getBodyVO() != null)) {
				bModify = isModifyButnEditableList(new InvoiceVO[] { getInvVOs()[getCurVOPos()] });
				this.m_btnInvShift.setEnabled(true);
			} else {
				ArrayList list = getSelectedBills();
				InvoiceVO[] VO = new InvoiceVO[list.size()];
				list.toArray(VO);
				bModify = isModifyButnEditableList(VO);
				this.m_btnInvShift.setEnabled(false);
			}
			this.m_btnInvBillModify.setEnabled(bModify);

			if (getSelectedRowCount() == 1) {
				this.m_btnQueryForAudit.setEnabled(true);
				this.m_btnLnkQuery.setEnabled(true);
			} else {
				this.m_btnQueryForAudit.setEnabled(false);
				this.m_btnInvBillModify.setEnabled(false);
			}

		}

		this.m_btnInvBillQuery.setEnabled(true);
		this.m_btnInvBillConversion.setEnabled(false);

		if (isEverQueryed())
			this.m_btnInvBillRefresh.setEnabled(true);
		else {
			this.m_btnInvBillRefresh.setEnabled(false);
		}

		setButtonsList();
	}

	private boolean isModifyButnEditableList(InvoiceVO[] VO) {
		boolean bModify = true;

		for (int i = 0; i < VO.length; ++i) {
			if (VO[i].getHeadVO().getIbillstatus().intValue() != 0) {
				bModify = false;
				break;
			}
			InvoiceItemVO[] bodyVO = VO[i].getBodyVO();
			for (int j = 0; j < bodyVO.length; ++j) {
				if ((bodyVO[j].getNaccumsettmny() != null)
						&& (Math
								.abs(bodyVO[j].getNaccumsettmny().doubleValue()) > 0.0D)) {
					bModify = false;
					break;
				}
			}

		}

		return bModify;
	}

	private void setCauditid(String newId) {
		this.m_cauditid = newId;
	}

	private void setCouldMaked(boolean newBoolean) {
		this.m_bCouldMaked = newBoolean;
	}

	private void setCurBizeType(String newCurBizeType) {
		this.m_strCurBizeType = newCurBizeType;
	}

	private void setCurOperState(int newCurOperState) {
		this.m_nCurOperState = newCurOperState;
	}

	private void setCurPanelMode(int newCurPaneModel) {
		this.m_nCurPanelMode = newCurPaneModel;
	}

	/** @deprecated */
	private void setCurrMoneyDigitAndReCalToBill(String strCurr) {
		if ((strCurr == null) || (strCurr.length() < 1)) {
			return;
		}

		int nDigit = POPubSetUI.getMoneyDigitByCurr_Busi(strCurr);

		int nDigit1 = POPubSetUI.getMoneyDigitByCurr_Finance(strCurr);

		getInvBillPanel().getBodyItem("noriginalcurmny").setDecimalDigits(
				nDigit);
		getInvBillPanel().getBodyItem("noriginaltaxmny").setDecimalDigits(
				nDigit);
		getInvBillPanel().getBodyItem("noriginalsummny").setDecimalDigits(
				nDigit);
		getInvBillPanel().getBodyItem("noriginalpaymentmny").setDecimalDigits(
				nDigit1);

		getInvBillPanel().getBodyItem("nmoney").setDecimalDigits(nDigit);
		getInvBillPanel().getBodyItem("ntaxmny").setDecimalDigits(nDigit);
		getInvBillPanel().getBodyItem("nsummny").setDecimalDigits(nDigit);
		getInvBillPanel().getBodyItem("npaymentmny").setDecimalDigits(nDigit1);

		if (getOldCurrMoneyDigit() == nDigit) {
			return;
		}
		setOldCurrMoneyDigit(nDigit);

		if (getInvBillPanel().getBillModel().getRowCount() <= 0) {
			return;
		}
		for (int i = 0; i < getInvBillPanel().getBillModel().getRowCount(); ++i)
			afterEditInvBillRelations(new BillEditEvent(getInvBillPanel()
					.getBodyItem("ninvoicenum"), getInvBillPanel()
					.getBodyValueAt(i, "ninvoicenum"), "ninvoicenum", i, 1));
	}

	/** @deprecated */
	private void setCurrMoneyDigitToBill(String strCurr) {
		if ((strCurr == null) || (strCurr.length() < 1)) {
			return;
		}

		int nDigit = POPubSetUI.getMoneyDigitByCurr_Busi(strCurr);

		int nDigit1 = POPubSetUI.getMoneyDigitByCurr_Finance(strCurr);

		getInvBillPanel().getBodyItem("noriginalcurmny").setDecimalDigits(
				nDigit);
		getInvBillPanel().getBodyItem("noriginaltaxmny").setDecimalDigits(
				nDigit);
		getInvBillPanel().getBodyItem("noriginalsummny").setDecimalDigits(
				nDigit);
		getInvBillPanel().getBodyItem("noriginalpaymentmny").setDecimalDigits(
				nDigit1);

		getInvBillPanel().getBodyItem("nmoney").setDecimalDigits(nDigit);
		getInvBillPanel().getBodyItem("ntaxmny").setDecimalDigits(nDigit);
		getInvBillPanel().getBodyItem("nsummny").setDecimalDigits(nDigit);
		getInvBillPanel().getBodyItem("npaymentmny").setDecimalDigits(nDigit1);
	}

	private void setCurrRateToBillHead(String sCurr, UFDouble dBRate,
			UFDouble dARate) {
		String sRateDate = getInvBillPanel().getHeadItem("darrivedate")
				.getValue();
		UFDate dOrderDate = null;
		if ((sRateDate != null) && (!sRateDate.trim().equals(""))) {
			dOrderDate = new UFDate(sRateDate);
		}

		int[] iaDigit = POPubSetUI.getBothExchRateDigit(getPk_corp(), sCurr);
		getInvBillPanel().getHeadItem("nexchangeotobrate").setDecimalDigits(
				iaDigit[0]);
		getInvBillPanel().getHeadItem("nexchangeotoarate").setDecimalDigits(
				iaDigit[1]);

		boolean[] baEditable = POPubSetUI.getBothExchRateEditable(getPk_corp(),
				sCurr);
		getInvBillPanel().getHeadItem("nexchangeotobrate").setEnabled(
				baEditable[0]);
		getInvBillPanel().getHeadItem("nexchangeotoarate").setEnabled(
				baEditable[1]);

		UFDouble[] daValue = null;
		if (dBRate == null) {
			daValue = POPubSetUI.getBothExchRateValue(getPk_corp(), sCurr,
					dOrderDate);
			getInvBillPanel().setHeadItem("nexchangeotobrate", daValue[0]);
		} else {
			getInvBillPanel().setHeadItem("nexchangeotobrate", dBRate);
		}
		if (dARate == null) {
			if (daValue == null) {
				daValue = POPubSetUI.getBothExchRateValue(getPk_corp(), sCurr,
						dOrderDate);
			}
			getInvBillPanel().setHeadItem("nexchangeotoarate", daValue[1]);
		} else {
			getInvBillPanel().setHeadItem("nexchangeotoarate", dARate);
		}
	}

	private void setCurVOPos(int pos) {
		this.m_nCurInvVOPos = pos;
	}

	private void setDefaultBankAccountForAVendor(String strVendorBase) {
		if ((strVendorBase == null) || (strVendorBase.trim().equals(""))) {
			return;
		}

		Object[][] retOb = CacheTool.getMultiColValue("bd_custbank",
				"pk_cubasdoc", new String[] { "pk_custbank", "account",
						"defflag" }, new String[] { strVendorBase });
		Vector v1 = new Vector();
		Vector v2 = new Vector();
		if (retOb != null) {
			for (int i = 0; i < retOb.length; ++i) {
				if ((retOb[i] != null) && (retOb[i][2] != null)
						&& (retOb[i][2].equals("Y"))) {
					v1.add(retOb[i][0]);
					v2.add(retOb[i][1]);
				}
			}

		}

		UIRefPane pane = (UIRefPane) getInvBillPanel().getHeadItem(
				"caccountbankid").getComponent();
		((AccountsForVendorRefModel) pane.getRef().getRefModel())
				.setCvendorbaseid(strVendorBase);

		if (v1.size() == 0) {
			getInvBillPanel().setHeadItem("caccountbankid", null);

			getInvBillPanel().setHeadItem("cvendoraccount", null);
		} else {
			getInvBillPanel().setHeadItem("caccountbankid",
					(String) v1.elementAt(0));

			getInvBillPanel().setHeadItem("cvendoraccount",
					(String) v1.elementAt(0));
		}
	}

	private void setDefaultBody(int iCurRow) {
		getInvBillPanel().setBodyValueAt(null, iCurRow, "cinvoice_bid");
		getInvBillPanel().setBodyValueAt(null, iCurRow, "cinvoiceid");

		getInvBillPanel().setBodyValueAt(getPk_corp(), iCurRow, "pk_corp");

		updateUI();
	}

	private void setDefaultInfoForAFreeCust(String cfreeId) {
		if (cfreeId == null) {
			getInvBillPanel().setHeadItem("cvendorphone", null);
			getInvBillPanel().setHeadItem("caccountbankid", null);
			getInvBillPanel().setHeadItem("cvendoraccount", null);
			return;
		}

		Object[][] strInfo = (Object[][]) null;
		try {
			strInfo = PubHelper.queryResultsFromAnyTable("so_freecust",
					new String[] { "vphone", "vaccname", "vaccount" },
					"cfreecustid='" + cfreeId + "'");
		} catch (Exception e1) {
			SCMEnv.out(e1);
			return;
		}
		if (strInfo != null) {
			getInvBillPanel().setHeadItem("cvendorphone", strInfo[0][0]);
			((UIRefPane) getInvBillPanel().getHeadItem("caccountbankid")
					.getComponent()).getUITextField().setText(
					(String) strInfo[0][1]);
			getInvBillPanel().setHeadItem("cvendoraccount", strInfo[0][2]);
		}
	}

	private void setDefaultPhoneForAVendor(String strVendorBase) {
		if ((strVendorBase == null) || (strVendorBase.trim().equals(""))) {
			return;
		}

		BillItem item = getInvBillPanel().getHeadItem("cvendorphone");
		if ((item == null) || (!item.isShow()))
			return;

		Object[][] retOb = CacheTool.getMultiColValue("bd_cubasdoc",
				"pk_cubasdoc", new String[] { "phone1", "phone2", "phone3" },
				new String[] { strVendorBase });

		if ((retOb == null) || (retOb.length == 0)) {
			getInvBillPanel().setHeadItem("cvendorphone", null);
		} else {
			String phone = null;
			for (int i = 0; i < retOb.length; ++i) {
				if ((retOb[i][0] != null)
						&& (retOb[i][0].toString().trim().length() > 0)) {
					phone = retOb[i][0].toString();
					break;
				}
			}
			getInvBillPanel().setHeadItem("cvendorphone", phone);
		}
	}

	private void setEditableWhenEdit() {
		setEditableWhenEdit_Vinvoicecode();

		setEditableWhenEdit_FreeCustAndBank();

		setEditableWhenEdit_ExchRate();

		setEditableWhenEdit_Inventory();

		setEditableWhenEdit_VProduceNum();
	}

	private void setEditableWhenEdit_ExchRate() {
		String sCurr = getInvBillPanel().getHeadItem("ccurrencytypeid")
				.getValue();
		boolean[] baEditable = POPubSetUI.getBothExchRateEditable(getPk_corp(),
				sCurr);
		getInvBillPanel().getHeadItem("nexchangeotobrate").setEnabled(
				baEditable[0]);
		getInvBillPanel().getHeadItem("nexchangeotoarate").setEnabled(
				baEditable[1]);
	}

	private void setEditableWhenEdit_FreeCustAndBank() {
		if (getInvBillPanel().getHeadItem("cfreecustid").getValue() == null) {
			if ((getInvBillPanel().getHeadItem("cvendorbaseid").getValue() == null)
					|| (getInvBillPanel().getHeadItem("cvendorbaseid")
							.getValue().trim().length() < 1)) {
				getInvBillPanel().getHeadItem("cfreecustid").setEnabled(false);
				getInvBillPanel().getHeadItem("caccountbankid").setEnabled(
						false);
			} else {
				String sFreeFlag = "N";
				Object oTemp = getInvBillPanel().getHeadItem("cvendorbaseid")
						.getValue();
				if (oTemp != null)
					oTemp = PiPqPublicUIClass.getAResultFromTable(
							"bd_cubasdoc", "freecustflag", "pk_cubasdoc", oTemp
									.toString());
				if (oTemp != null)
					sFreeFlag = oTemp.toString();
				if ((sFreeFlag != null) && (sFreeFlag.trim() != null)
						&& (sFreeFlag.equals("Y"))) {
					getInvBillPanel().getHeadItem("cfreecustid").setEnabled(
							true);
					getInvBillPanel().getHeadItem("caccountbankid").setEnabled(
							false);
				} else {
					getInvBillPanel().getHeadItem("cfreecustid").setEnabled(
							false);
					getInvBillPanel().getHeadItem("caccountbankid").setEnabled(
							true);
				}
			}
		} else {
			getInvBillPanel().getHeadItem("cfreecustid").setEnabled(true);
			getInvBillPanel().getHeadItem("caccountbankid").setEnabled(false);
		}

		getInvBillPanel().getHeadItem("cvendorphone").getComponent()
				.setEnabled(false);
	}

	private void setEditableWhenEdit_Inventory() {
		int iCount = getInvBillPanel().getRowCount();
		for (int i = 0; i < iCount; ++i) {
			String sUpSourceBillType = (String) getInvBillPanel()
					.getBodyValueAt(i, "cupsourcebilltype");
			if ((sUpSourceBillType != null)
					&& (((sUpSourceBillType.trim().equals("45"))
							|| (sUpSourceBillType.trim().equals("47"))
							|| (sUpSourceBillType.trim().equals("4T")) || (sUpSourceBillType
							.trim().equals("50"))))) {
				getInvBillPanel().setCellEditable(i, "invcode", false);
			} else
				getInvBillPanel().setCellEditable(i, "invcode", true);
		}
	}

	private void setEditableWhenEdit_Vinvoicecode() {
		if ((getInvBillPanel().getHeadItem("vinvoicecode").getValue() == null)
				|| (getInvBillPanel().getHeadItem("vinvoicecode").getValue()
						.trim().equals(""))) {
			getInvBillPanel().getHeadItem("vinvoicecode").setEnabled(true);
		} else if ((getInvBillPanel().getHeadItem("cinvoiceid").getValue() == null)
				|| (getInvBillPanel().getHeadItem("cinvoiceid").getValue()
						.trim().equals("")))
			getInvBillPanel().getHeadItem("vinvoicecode").setEnabled(true);
		else
			getInvBillPanel().getHeadItem("vinvoicecode").setEnabled(true);
	}

	private void setEditableWhenEdit_VProduceNum() {
		int iRow = getInvBillPanel().getRowCount();
		if (iRow <= 0) {
			return;
		}
		for (int i = 0; i < iRow; ++i) {
			String sMangId = (String) getInvBillPanel().getBodyValueAt(i,
					"cmangid");
			if ((sMangId == null) || (sMangId.trim().length() < 1)) {
				getInvBillPanel().setCellEditable(i, "vproducenum", false);
			} else
				getInvBillPanel().setCellEditable(i, "vproducenum",
						PuTool.isBatchManaged(sMangId));
		}
	}

	private void setEverQueryed(boolean newQueryed) {
		this.m_bEverQueryed = newQueryed;
	}

	public void setHeadCurrency(InvoiceVO inv) {
		InvoiceHeaderVO head = inv.getHeadVO();
		InvoiceItemVO[] items = (InvoiceItemVO[]) (InvoiceItemVO[]) inv
				.getChildrenVO();

		if ((head != null) && (items != null) && (items.length > 0)) {
			if (this.m_listPoPubSetUI2 == null) {
				this.m_listPoPubSetUI2 = new POPubSetUI2();
			}

			int[] iaExchRateDigit = null;
			for (int i = 0; i < items.length; ++i) {
				String sCvendormangid = head.getCvendormangid();
				String upSourceType = items[i].getCupsourcebilltype();
				String currencytypeid = items[i].getCcurrencytypeid();
				if (currencytypeid != null) {
					head.setCcurrencytypeid(items[i].getCcurrencytypeid());
				} else if ((upSourceType != null)
						&& (upSourceType.trim().length() > 0)
						&& (((upSourceType.equals("45"))
								|| (upSourceType.equals("47")) || (upSourceType
								.equals("4T")))) && (sCvendormangid != null)
						&& (sCvendormangid.trim().length() > 0)) {
					try {
						Object oTemp = CacheTool.getCellValue("bd_cumandoc",
								"pk_cumandoc", "pk_currtype1", sCvendormangid);
						if (oTemp != null) {
							head.setCcurrencytypeid(oTemp.toString());
							items[i].setCcurrencytypeid(oTemp.toString());
						}
					} catch (Exception e) {
						SCMEnv.out(e);
					}

				}

				String sCurrId = items[i].getCcurrencytypeid();

				iaExchRateDigit = this.m_listPoPubSetUI2.getBothExchRateDigit(
						getPk_corp(), sCurrId);
				if ((iaExchRateDigit == null) || (iaExchRateDigit.length == 0)) {
					getInvBillPanel().getBillModel().getItemByKey(
							"nexchangeotobrate").setDecimalDigits(2);
					getInvBillPanel().getBillModel().getItemByKey(
							"nexchangeotoarate").setDecimalDigits(2);
				} else {
					getInvBillPanel().getBillModel().getItemByKey(
							"nexchangeotobrate").setDecimalDigits(
							iaExchRateDigit[0]);
					getInvBillPanel().getBillModel().getItemByKey(
							"nexchangeotoarate").setDecimalDigits(
							iaExchRateDigit[1]);
				}
			}

			if ((iaExchRateDigit == null) || (iaExchRateDigit.length == 0)) {
				getInvBillPanel().getHeadItem("nexchangeotobrate")
						.setDecimalDigits(2);
				getInvBillPanel().getHeadItem("nexchangeotoarate")
						.setDecimalDigits(2);
			} else {
				getInvBillPanel().getHeadItem("nexchangeotobrate")
						.setDecimalDigits(iaExchRateDigit[0]);
				getInvBillPanel().getHeadItem("nexchangeotoarate")
						.setDecimalDigits(iaExchRateDigit[1]);
			}

			UFDouble[] d = POPubSetUI.getBothExchRateValue(getPk_corp(), head
					.getCcurrencytypeid(), head.getDarrivedate());
			head.setNexchangeotobrate(d[0]);
			head.setNexchangeotoarate(d[1]);
		}
	}

	private void setHeadHintText(String strHint) {
		this.m_strHeadHintText = strHint;
	}

	private void setInvEditFormulaInfo(BillCardPanel pnlBillCard,
			UIRefPane refpaneInv, int iBeginRow, int iEndRow) {
		if ((pnlBillCard == null) || (refpaneInv == null)) {
			SCMEnv.out("传入参数不正确！");
			return;
		}
		try {
			Object[] saMangIdRef = (Object[]) (Object[]) refpaneInv
					.getRefValues("bd_invmandoc.pk_invmandoc");
			Object[] saBaseIdRef = (Object[]) (Object[]) refpaneInv
					.getRefValues("bd_invmandoc.pk_invbasdoc");
			Object[] saMeasUnitRef = (Object[]) (Object[]) refpaneInv
					.getRefValues("bd_invbasdoc.pk_measdoc");
			Object[] saMangId = new Object[saMangIdRef.length];
			Object[] saBaseId = new Object[saBaseIdRef.length];
			Object[] saMeasUnit = new Object[saMeasUnitRef.length];
			if ((saMangId == null) || (saBaseId == null)
					|| (saMangId.length != saBaseId.length)) {
				SCMEnv.out("数据错误：存货管理档案ID为空或存货档案ID为空或二者长度不等，直接返回");
				return;
			}
			for (int i = 0; i < saMangId.length; ++i) {
				saMangId[i] = saMangIdRef[i];
				saBaseId[i] = saBaseIdRef[i];
				saMeasUnit[i] = saMeasUnitRef[i];
			}
			for (int i = 0; i < saMangId.length; ++i) {
				saMangIdRef[i] = ((String) saMangIdRef[i]);
				saBaseIdRef[i] = ((String) saBaseIdRef[i]);
				saMeasUnitRef[i] = ((String) saMeasUnitRef[i]);
			}
			int iLen = saMangId.length;

			String[] saFormula = { "getColValue(bd_measdoc,measname,pk_measdoc,cmessureunit)" };
			PuTool.getFormulaParse().setExpressArray(saFormula);
			int iFormulaLen = saFormula.length;

			String[] sMeasUnits = new String[saMangId.length];
			String[] sMangIds = new String[saMangId.length];
			for (int i = 0; i < saMangId.length; ++i) {
				sMeasUnits[i] = sMeasUnits[i];
				sMangIds[i] = ((String) saMangId[i]);
			}
			for (int i = 0; i < iFormulaLen; ++i) {
				PuTool.getFormulaParse().addVariable("cmessureunit",
						saMeasUnitRef);
			}

			String[][] saRet = PuTool.getFormulaParse().getValueSArray();
			String[] saUnitName = new String[iLen];
			if (saRet != null) {
				for (int i = 0; i < iLen; ++i) {
					if (saRet[0] != null) {
						saUnitName[i] = saRet[0][i];
					}
				}
			}

			saFormula = new String[] { "getColValue(bd_invmandoc,prodarea,pk_invmandoc,cmangid)" };
			iFormulaLen = saFormula.length;
			PuTool.getFormulaParse().setExpressArray(saFormula);

			for (int i = 0; i < iFormulaLen; ++i) {
				PuTool.getFormulaParse().addVariable("cmangid", saMangIdRef);
			}

			saRet = PuTool.getFormulaParse().getValueSArray();

			String[] saArea = new String[iLen];
			if (saRet != null) {
				for (int i = 0; i < iLen; ++i) {
					if (saRet[0] != null) {
						saArea[i] = saRet[0][i];
					}
				}
			}

			Object[] saCode = (Object[]) (Object[]) refpaneInv
					.getRefValues("bd_invbasdoc.invcode");
			Object[] saName = (Object[]) (Object[]) refpaneInv
					.getRefValues("bd_invbasdoc.invname");
			Object[] saSpec = (Object[]) (Object[]) refpaneInv
					.getRefValues("bd_invbasdoc.invspec");
			Object[] saType = (Object[]) (Object[]) refpaneInv
					.getRefValues("bd_invbasdoc.invtype");

			int iPkIndex = 0;
			for (int i = iBeginRow; i <= iEndRow; ++i) {
				iPkIndex = i - iBeginRow;

				pnlBillCard.setBodyValueAt(saMangId[iPkIndex], i, "cmangid");

				pnlBillCard.setBodyValueAt(saBaseId[iPkIndex], i, "cbaseid");

				pnlBillCard.setBodyValueAt(saCode[iPkIndex], i, "invcode");

				pnlBillCard.setBodyValueAt(saName[iPkIndex], i, "invname");

				pnlBillCard.setBodyValueAt(saSpec[iPkIndex], i, "invspec");

				pnlBillCard.setBodyValueAt(saType[iPkIndex], i, "invtype");

				pnlBillCard.setBodyValueAt(saUnitName[iPkIndex], i, "measname");

				pnlBillCard.setBodyValueAt(saArea[iPkIndex], i, "cproducearea");
			}
		} catch (Exception e) {
			SCMEnv.out("录入多存货时设置出错");
		}
	}

	private void setInvoiceTypeComItem() {
		UIComboBox comItem = (UIComboBox) getInvBillPanel().getHeadItem(
				"iinvoicetype").getComponent();
		if (getCurOperState() == 1) {
			if (comItem.getItemCount() == 4) {
				comItem.removeItem(NCLangRes.getInstance().getStrByID("scmpub",
						"UPPscmpub-001149"));
			}
		} else if (comItem.getItemCount() == 3)
			comItem.addItem(NCLangRes.getInstance().getStrByID("scmpub",
					"UPPscmpub-001149"));
	}

	private void setInvVOs(InvoiceVO[] newInvVOs) {
		this.m_InvVOs = newInvVOs;
	}

	private void setListOperState(int newListOperState) {
		this.m_nListOperState = newListOperState;
	}

	private void setNewLineDefaultValue(int iNewRow) {
		getInvBillPanel().setBodyValueAt(
				((UIComboBox) getInvBillPanel().getHeadItem("idiscounttaxtype")
						.getComponent()).getSelectedItem(), iNewRow,
				"idiscounttaxtype");

		if (getInvBillPanel().getHeadItem("ntaxrate").getValue() != null) {
			getInvBillPanel().setBodyValueAt(
					getInvBillPanel().getHeadItem("ntaxrate").getValue(),
					iNewRow, "ntaxrate");
		}

		String strCurrTypeId = ((UIRefPane) getInvBillPanel().getHeadItem(
				"ccurrencytypeid").getComponent()).getRefPK();
		getInvBillPanel().setBodyValueAt(strCurrTypeId, iNewRow,
				"ccurrencytypeid");
		getInvBillPanel().setBodyValueAt(
				((UIRefPane) getInvBillPanel().getHeadItem("ccurrencytypeid")
						.getComponent()).getRefName(), iNewRow, "currname");

		setExchangeRateBody(iNewRow, true, null);

		getInvBillPanel().setBodyValueAt("", iNewRow, "vmemo");

		getInvBillPanel().setBodyValueAt("", iNewRow, "noriginalcurmny");
		getInvBillPanel().setBodyValueAt("", iNewRow, "noriginaltaxmny");
		getInvBillPanel().setBodyValueAt("", iNewRow, "noriginalsummny");
		getInvBillPanel().setBodyValueAt("", iNewRow, "nmoney");
		getInvBillPanel().setBodyValueAt("", iNewRow, "ntaxmny");
		getInvBillPanel().setBodyValueAt("", iNewRow, "nsummny");
		getInvBillPanel().setBodyValueAt("", iNewRow, "nassistcurmny");
		getInvBillPanel().setBodyValueAt("", iNewRow, "nassisttaxmny");
		getInvBillPanel().setBodyValueAt("", iNewRow, "nassistsummny");

		getInvBillPanel().setBodyValueAt("", iNewRow, "cinvoiceid");
		getInvBillPanel().setBodyValueAt("", iNewRow, "cinvoice_bid");

		getInvBillPanel().setBodyValueAt(null, iNewRow, "corderid");
		getInvBillPanel().setBodyValueAt(null, iNewRow, "corder_bid");
		getInvBillPanel().setBodyValueAt(null, iNewRow, "csourcebillid");
		getInvBillPanel().setBodyValueAt(null, iNewRow, "csourcebillrowid");
		getInvBillPanel().setBodyValueAt(null, iNewRow, "csourcebilltype");
		getInvBillPanel().setBodyValueAt(null, iNewRow, "cupsourcebillid");
		getInvBillPanel().setBodyValueAt(null, iNewRow, "cupsourcebillrowid");
		getInvBillPanel().setBodyValueAt(null, iNewRow, "cupsourcebilltype");
	}

	private void setNewLineDefaultValueForAddLine(int iNewRow) {
		getInvBillPanel().setBodyValueAt(
				((UIComboBox) getInvBillPanel().getHeadItem("idiscounttaxtype")
						.getComponent()).getSelectedItem(), iNewRow,
				"idiscounttaxtype");

		if (getInvBillPanel().getHeadItem("ntaxrate").getValue() != null) {
			getInvBillPanel().setBodyValueAt(
					getInvBillPanel().getHeadItem("ntaxrate").getValue(),
					iNewRow, "ntaxrate");
		}

		String strCurrTypeId = ((UIRefPane) getInvBillPanel().getHeadItem(
				"ccurrencytypeid").getComponent()).getRefPK();
		getInvBillPanel().setBodyValueAt(strCurrTypeId, iNewRow,
				"ccurrencytypeid");
		getInvBillPanel().setBodyValueAt(
				((UIRefPane) getInvBillPanel().getHeadItem("ccurrencytypeid")
						.getComponent()).getRefName(), iNewRow, "currname");

		setExchangeRateBody(iNewRow, true, null);

		getInvBillPanel().setBodyValueAt("", iNewRow, "vmemo");

		getInvBillPanel().setBodyValueAt("", iNewRow, "cmangid");
		getInvBillPanel().setBodyValueAt("", iNewRow, "cbaseid");
		getInvBillPanel().setBodyValueAt("", iNewRow, "invcode");
		getInvBillPanel().setBodyValueAt("", iNewRow, "invname");
		getInvBillPanel().setBodyValueAt("", iNewRow, "invspec");
		getInvBillPanel().setBodyValueAt("", iNewRow, "invtype");
		getInvBillPanel().setBodyValueAt("", iNewRow, "measname");
		getInvBillPanel().setBodyValueAt("", iNewRow, "noriginalcurmny");
		getInvBillPanel().setBodyValueAt("", iNewRow, "noriginaltaxmny");
		getInvBillPanel().setBodyValueAt("", iNewRow, "noriginalsummny");
		getInvBillPanel().setBodyValueAt("", iNewRow, "nmoney");
		getInvBillPanel().setBodyValueAt("", iNewRow, "ntaxmny");
		getInvBillPanel().setBodyValueAt("", iNewRow, "nsummny");
		getInvBillPanel().setBodyValueAt("", iNewRow, "nassistcurmny");
		getInvBillPanel().setBodyValueAt("", iNewRow, "nassisttaxmny");
		getInvBillPanel().setBodyValueAt("", iNewRow, "nassistsummny");

		getInvBillPanel().setBodyValueAt("", iNewRow, "cinvoiceid");
		getInvBillPanel().setBodyValueAt("", iNewRow, "cinvoice_bid");

		getInvBillPanel().setBodyValueAt(null, iNewRow, "corderid");
		getInvBillPanel().setBodyValueAt(null, iNewRow, "corder_bid");
		getInvBillPanel().setBodyValueAt(null, iNewRow, "csourcebillid");
		getInvBillPanel().setBodyValueAt(null, iNewRow, "csourcebillrowid");
		getInvBillPanel().setBodyValueAt(null, iNewRow, "csourcebilltype");
		getInvBillPanel().setBodyValueAt(null, iNewRow, "cupsourcebillid");
		getInvBillPanel().setBodyValueAt(null, iNewRow, "cupsourcebillrowid");
		getInvBillPanel().setBodyValueAt(null, iNewRow, "cupsourcebilltype");
	}

	private void setOldCurrMoneyDigit(int newValue) {
		this.m_oldCurrMoneyDigit = newValue;
	}

	private void setRelated_Taxrate(int iBeginRow, int iEndRow) {
		HashMap mapId = new HashMap();
		Vector vecRow = new Vector();
		for (int i = iBeginRow; i <= iEndRow; ++i) {
			String sBaseId = PuPubVO
					.getString_TrimZeroLenAsNull((String) getInvBillPanel()
							.getBodyValueAt(i, "cbaseid"));
			if (sBaseId != null) {
				mapId.put(sBaseId, "");
				vecRow.add(new Integer(i));
			}
		}
		int iSize = vecRow.size();
		if (iSize == 0) {
			return;
		}

		PuTool.loadBatchTaxrate((String[]) (String[]) mapId.keySet().toArray(
				new String[iSize]));

		int iRow = 0;
		for (int i = 0; i < iSize; ++i) {
			iRow = ((Integer) vecRow.get(i)).intValue();

			String sBaseId = (String) getInvBillPanel().getBodyValueAt(iRow,
					"cbaseid");

			UFDouble dCurTaxRate = PuTool.getInvTaxRate(sBaseId);
			if (dCurTaxRate != null) {
				getInvBillPanel().setBodyValueAt(dCurTaxRate, iRow, "ntaxrate");

				BillEditEvent tempE = new BillEditEvent(getInvBillPanel()
						.getBodyItem("ntaxrate"), dCurTaxRate, "ntaxrate", iRow);

				afterEditInvBillRelations(tempE);
			}
		}
	}

	private void setSelectedRowCount(int newSelectedRow) {
		this.m_nSelectedRowCount = newSelectedRow;
	}

	private boolean setSendAuditBtnState() {
		boolean b = false;
		if ((getInvVOs() == null) || (getInvVOs().length <= 0)) {
			this.m_btnSendAudit.setEnabled(false);
			return b;
		}
		InvoiceVO curVO = getInvVOs()[getCurVOPos()];

		if (curVO == null)
			return b;
		if (curVO.getHeadVO().getCoperator() == null) {
			curVO.getHeadVO().setCoperator(
					ClientEnvironment.getInstance().getUser().getPrimaryKey());
		}
		if ((curVO.getHeadVO().getIbillstatus().intValue() == 4)
				&& (getCurOperState() == 1)) {
			curVO.getHeadVO().setIbillstatus(new Integer(0));
			this.hBillStatusBeforeEdit.put(curVO.getHeadVO().getCinvoiceid(),
					new Integer(4));
		}

		b = PuTool.isNeedSendToAudit("25", curVO);

		return b;
	}

	private void setVoBodyToListPanle(int nCurIndex) {
		if ((nCurIndex < 0) || (nCurIndex >= getInvVOs().length)) {
			getInvListPanel().setBodyValueVO(null);
		} else {
			InvoiceItemVO[] bodyVO = getInvVOs()[nCurIndex].getBodyVO();
			Vector vTemp = new Vector();
			for (int i = 0; i < bodyVO.length; ++i) {
				if ((bodyVO[i].getVfree1() == null)
						&& (bodyVO[i].getVfree2() == null)
						&& (bodyVO[i].getVfree3() == null)
						&& (bodyVO[i].getVfree4() == null)
						&& (bodyVO[i].getVfree5() == null))
					continue;
				vTemp.addElement(bodyVO[i]);
			}
			if (vTemp.size() > 0) {
				InvoiceItemVO[] tempbodyVO = new InvoiceItemVO[vTemp.size()];
				vTemp.copyInto(tempbodyVO);
				new FreeVOParse().setFreeVO(tempbodyVO, "vfree0", "vfree",
						null, "cmangid", false);
			}

			setMaxMnyDigitList(this.iMaxMnyDigit);

			getInvListPanel()
					.setBodyValueVO(getInvVOs()[nCurIndex].getBodyVO());
			try {
				resetBodyValueRelated_Curr(getPk_corp(), getInvVOs()[nCurIndex]
						.getHeadVO().getCcurrencytypeid(), getInvListPanel()
						.getBodyBillModel(), new BusinessCurrencyRateUtil(
						getPk_corp()), getInvListPanel().getBodyBillModel()
						.getRowCount(), this.m_listPoPubSetUI2);
			} catch (Exception e) {
				SCMEnv.out(e);
			}

			getInvListPanel().getBodyBillModel().execLoadFormula();

			PuTool.loadSourceInfoAll(getInvListPanel(), "25");
			getInvListPanel().getBodyTable().clearSelection();
		}
	}

	private void setSpiltVOsToListPanel() {
		this.cunpos = 0;
		getInvListPanel().getHeadBillModel().clearBodyData();
		getInvListPanel().getBodyBillModel().clearBodyData();

		if (getInvVOs() == null) {
			return;
		}

		if (getCurVOPos() == -1)
			setCurVOPos(0);
		int pos = 0;
		int j = 0;
		int count = 0;
		Vector contains = new Vector();
		Vector poss = new Vector();

		for (int i = 0; i < getInvHVOs().length; ++i) {
			if (getInvVOs()[i].getHeadVO().getCinvoiceid() != null) {
				continue;
			}
			getInvListPanel().getHeadBillModel().addLine();

			if ((getInvHVOs()[i].getNexchangeotobrate() == null)
					&& (getInvHVOs()[i].getNexchangeotoarate() == null)) {
				UFDouble[] d = POPubSetUI.getBothExchRateValue(getPk_corp(),
						getInvHVOs()[i].getCcurrencytypeid(), getInvHVOs()[i]
								.getDarrivedate());

				getInvHVOs()[i].setNexchangeotobrate(d[0]);

				getInvHVOs()[i].setNexchangeotoarate(d[1]);
			}

			PiPqPublicUIClass.setCurrRateDigitToListHead(getInvListPanel(),
					getInvHVOs()[i].getCcurrencytypeid());

			if ((j == 0) && (count == 0)) {
				getInvListPanel().getHeadBillModel().setBodyRowVO(
						getInvHVOs()[i], 0);
				pos = i;
			} else {
				getInvListPanel().getHeadBillModel().setBodyRowVO(
						getInvHVOs()[i], j);
			}

			++j;
			++count;
			contains.add(getInvHVOs()[i]);
			poss.add(String.valueOf(i));
		}

		this.VOsPos = null;
		if (count > 0) {
			this.VOsPos = new String[poss.size()];
			poss.copyInto(this.VOsPos);
		}

		getInvListPanel().getHeadBillModel().execLoadFormula();
		if (count > 0) {
			InvoiceHeaderVO[] invoiceHeaderVOs = new InvoiceHeaderVO[contains
					.size()];
			contains.copyInto(invoiceHeaderVOs);
			InvoiceHeaderVO invoiceHeaderVO = new InvoiceHeaderVO();
			for (int i = 0; i < count; ++i) {
				invoiceHeaderVO = invoiceHeaderVOs[i];
				if (invoiceHeaderVO.getCinvoiceid() == null) {
					if ((invoiceHeaderVO.getFinitflag() == null)
							|| (invoiceHeaderVO.getFinitflag().intValue() == 0))
						getInvListPanel().getHeadBillModel().setValueAt(
								new Boolean(false), i, "finitflag");
					else if (invoiceHeaderVO.getFinitflag().intValue() == 1) {
						getInvListPanel().getHeadBillModel().setValueAt(
								new Boolean(true), i, "finitflag");
					}

					if ((invoiceHeaderVO.getIbillstatus() != null)
							&& (invoiceHeaderVO.getIbillstatus().intValue() == 3))
						getInvListPanel().getHeadBillModel().setValueAt(
								new Boolean(true), i, "isaudited");
					else {
						getInvListPanel().getHeadBillModel().setValueAt(
								new Boolean(false), i, "isaudited");
					}

				}

			}

		}

		getInvListPanel().getHeadTable().setRowSelectionInterval(0, 0);

		dispPlanPrice(false);
		setVoBodyToListPanle(pos);
		this.cunpos = pos;
		this.currentPos = pos;
		this.splitFlag = true;
	}

	private void setVOsToListPanel() {
		getInvListPanel().getHeadBillModel().clearBodyData();
		getInvListPanel().getBodyBillModel().clearBodyData();

		if (getInvVOs() == null) {
			return;
		}

		if (getCurVOPos() == -1) {
			setCurVOPos(0);
		}
		for (int i = 0; i < getInvHVOs().length; ++i) {
			getInvListPanel().getHeadBillModel().addLine();

			if (getInvHVOs()[i].getAttributeValue("ccurrencytypeid") == null) {
				try {
					Object oTemp = CacheTool.getCellValue("bd_cumandoc",
							"pk_cumandoc", "pk_currtype1", getInvHVOs()[i]
									.getCvendormangid());
					if (oTemp != null)
						getInvHVOs()[i].setAttributeValue("ccurrencytypeid",
								oTemp.toString());
				} catch (Exception e) {
					SCMEnv.out(e);
				}
			}

			PiPqPublicUIClass.setCurrRateDigitToListHead(getInvListPanel(),
					getInvHVOs()[i].getCcurrencytypeid());

			if ((getInvHVOs()[i].getNexchangeotobrate() == null)
					&& (getInvHVOs()[i].getNexchangeotoarate() == null)) {
				UFDouble[] d = POPubSetUI.getBothExchRateValue(getPk_corp(),
						getInvHVOs()[i].getCcurrencytypeid(), getInvHVOs()[i]
								.getDarrivedate());
				getInvHVOs()[i].setNexchangeotobrate(d[0]);
				getInvHVOs()[i].setNexchangeotoarate(d[1]);
			}

			getInvListPanel().getHeadBillModel().setBodyRowVO(getInvHVOs()[i],
					i);
		}

		getInvListPanel().getHeadBillModel().execLoadFormula();

		for (int i = 0; i < getInvVOs().length; ++i) {
			if ((getInvVOs()[i].getHeadVO().getFinitflag() == null)
					|| (getInvVOs()[i].getHeadVO().getFinitflag().intValue() == 0))
				getInvListPanel().getHeadBillModel().setValueAt(
						new Boolean(false), i, "finitflag");
			else if (getInvVOs()[i].getHeadVO().getFinitflag().intValue() == 1) {
				getInvListPanel().getHeadBillModel().setValueAt(
						new Boolean(true), i, "finitflag");
			}

			if ((getInvVOs()[i].getHeadVO().getIbillstatus() != null)
					&& (getInvVOs()[i].getHeadVO().getIbillstatus().intValue() == 3))
				getInvListPanel().getHeadBillModel().setValueAt(
						new Boolean(true), i, "isaudited");
			else {
				getInvListPanel().getHeadBillModel().setValueAt(
						new Boolean(false), i, "isaudited");
			}
		}

		getInvListPanel().getHeadTable().setRowSelectionInterval(getCurVOPos(),
				getCurVOPos());

		dispPlanPrice(false);
	}

	private void setVOToAuditedBillPanel() {
		getInvBillPanel().addNew();
		InvoiceVO curVO = getInvVOs()[getCurVOPos()];
		InvoiceHeaderVO head = getInvVOs()[getCurVOPos()].getHeadVO();

		if ((((head.getCcurrencytypeid() == null) || (head.getCcurrencytypeid()
				.trim().length() < 1)))
				&& (curVO.getBodyVO() != null)) {
			head.setCcurrencytypeid(curVO.getBodyVO()[0].getCcurrencytypeid());
			head.setNexchangeotobrate(curVO.getBodyVO()[0]
					.getNexchangeotobrate());
			head.setNexchangeotoarate(curVO.getBodyVO()[0]
					.getNexchangeotoarate());
		}

		InvoiceItemVO[] bodyVO = curVO.getBodyVO();
		Vector vTemp = new Vector();
		for (int i = 0; i < bodyVO.length; ++i) {
			if ((bodyVO[i].getVfree1() == null)
					&& (bodyVO[i].getVfree2() == null)
					&& (bodyVO[i].getVfree3() == null)
					&& (bodyVO[i].getVfree4() == null)
					&& (bodyVO[i].getVfree5() == null))
				continue;
			vTemp.addElement(bodyVO[i]);
		}
		if (vTemp.size() > 0) {
			InvoiceItemVO[] tempbodyVO = new InvoiceItemVO[vTemp.size()];
			vTemp.copyInto(tempbodyVO);
			new FreeVOParse().setFreeVO(tempbodyVO, "vfree0", "vfree", null,
					"cmangid", false);
		}

		String strVendorBase = head.getCvendorbaseid();
		if ((head.getCvendormangid() != null)
				&& (((strVendorBase == null) || (strVendorBase.trim()
						.equals(""))))) {
			strVendorBase = (String) PiPqPublicUIClass.getAResultFromTable(
					"bd_cumandoc", "pk_cubasdoc", "pk_cumandoc", head
							.getCvendormangid());
			head.setCvendorbaseid(strVendorBase);
		}

		setCurrMoneyDigitToBill(head.getCcurrencytypeid());

		getInvBillPanel().setBillValueVO(curVO);

		setCurrRateToBillHead(head.getCcurrencytypeid(), head
				.getNexchangeotobrate(), head.getNexchangeotoarate());

		if (curVO.getHeadVO().getCfreecustid() == null) {
			getInvBillPanel().getHeadItem("cfreecustid").setValue(null);
			((UIRefPane) getInvBillPanel().getHeadItem("caccountbankid")
					.getComponent()).setButtonVisible(true);

			setDefaultPhoneForAVendor(strVendorBase);

			UIRefPane pane1 = (UIRefPane) getInvBillPanel().getHeadItem(
					"caccountbankid").getComponent();
			((AccountsForVendorRefModel) pane1.getRef().getRefModel())
					.setCvendorbaseid(strVendorBase);
			if (head.getCaccountbankid() != null) {
				pane1.setPK(head.getCaccountbankid());
				String strAccount = null;
				try {
					Object[][] retOb = PubHelper.queryResultsFromAnyTable(
							"bd_custbank", new String[] { "account" },
							"pk_custbank='" + head.getCaccountbankid() + "'");
					strAccount = (String) retOb[0][0];
				} catch (Exception e) {
					SCMEnv.out(e);
					SCMEnv.out("取供应商默认银行时出现异常!");
				}

				getInvBillPanel().setHeadItem("cvendoraccount", strAccount);
			} else if (getCurOperState() == 1) {
				setDefaultBankAccountForAVendor(strVendorBase);
			}

			if (head.getCaccountbankid() != null)
				getInvBillPanel().setHeadItem(
						"cvendoraccount",
						PiPqPublicUIClass.getAResultFromTable("bd_custbank",
								"account", "pk_custbank", head
										.getCaccountbankid()));
		} else {
			((UIRefPane) getInvBillPanel().getHeadItem("caccountbankid")
					.getComponent()).setButtonVisible(false);

			setDefaultInfoForAFreeCust(curVO.getHeadVO().getCfreecustid());
		}

		UIRefPane pane2 = (UIRefPane) getInvBillPanel().getHeadItem(
				"cemployeeid").getComponent();

		String pk_corp = head.getPk_purcorp();
		String s = " bd_psndoc.indocflag='Y' and (bd_psndoc.pk_deptdoc in (select bd_deptdoc.pk_deptdoc from bd_deptdoc where (bd_deptdoc.deptattr = '2' or bd_deptdoc.deptattr = '4') and bd_deptdoc.pk_corp ='"
				+ pk_corp + "')) ";

		pane2.getRefModel().setWherePart(s);

		if (head.getCemployeeid() != null) {
			pane2.setPK(head.getCemployeeid());
		}

		UICheckBox initCheck = (UICheckBox) getInvBillPanel().getHeadItem(
				"finitflag").getComponent();
		if ((head.getFinitflag() == null)
				|| (head.getFinitflag().intValue() == 0))
			initCheck.setSelected(false);
		else {
			initCheck.setSelected(true);
		}

		int selectedIndex = ((UIComboBox) getInvBillPanel().getHeadItem(
				"idiscounttaxtype").getComponent()).getSelectedIndex();
		((UIComboBox) getInvBillPanel().getHeadItem("idiscounttaxtype")
				.getComponent()).setSelectedIndex(selectedIndex);

		((UIRefPane) getInvBillPanel().getHeadItem("vmemo").getComponent())
				.setText(head.getVmemo());

		if (curVO.getBodyVO() != null) {
			getInvBillPanel().getBillModel().execLoadFormula();
		}

		if (curVO.getBodyVO() != null) {
			for (int i = 0; i < curVO.getBodyVO().length; ++i) {
				if (curVO.getBodyVO()[i].getVmemo() == null) {
					getInvBillPanel().setBodyValueAt("", i, "vmemo");
				}

			}

		}

		PuTool.loadSourceInfoAll(getInvBillPanel(), "25");

		execHeadTailFormula(curVO);

		getInvBillPanel().getBillModel().updateValue();

		if (getCurOperState() == 1)
			showHintMessage(getHeadHintText()
					+ "|"
					+ NCLangRes.getInstance().getStrByID("40040401",
							"UPP40040401-000060") + "|");
		else
			showHintMessage(getHeadHintText()
					+ "|"
					+ NCLangRes.getInstance().getStrByID("40040401",
							"UPP40040401-000032") + "|");
	}

	private void setVOToBillPanel() {
		Timer timer = new Timer();
		timer.start("setVOToBillPanel");

		setInvoiceTypeComItem();

		if ((getInvVOs() == null) || (getInvVOs().length == 0)) {
			getInvBillPanel().addNew();

			dispPlanPrice(true);
			return;
		}

		InvoiceVO curVO = getInvVOs()[getCurVOPos()];
		InvoiceHeaderVO head = getInvVOs()[getCurVOPos()].getHeadVO();
		if ((curVO.getChildrenVO() == null)
				|| (curVO.getChildrenVO().length == 0)) {
			if (!loadItemsForInvoiceVOs(new InvoiceVO[] { curVO })) {
				setCurVOPos(this.m_nLstInvVOPos);
				dispPlanPrice(true);
				return;
			}
		}
		timer.addExecutePhase("loadItemsForInvoiceVOs");

		setCurBizeType(head.getCbiztype());

		InvoiceItemVO[] items = (InvoiceItemVO[]) (InvoiceItemVO[]) curVO
				.getChildrenVO();
		Vector vTemp = new Vector();
		if ((items != null) && (items.length > 0)) {
			for (int i = 0; i < items.length; ++i) {
				if ((items[i].getVfree1() == null)
						&& (items[i].getVfree2() == null)
						&& (items[i].getVfree3() == null)
						&& (items[i].getVfree4() == null)
						&& (items[i].getVfree5() == null)) {
					continue;
				}
				vTemp.addElement(items[i]);
			}

			if (vTemp.size() > 0) {
				InvoiceItemVO[] bodyVO = new InvoiceItemVO[vTemp.size()];
				vTemp.copyInto(bodyVO);
				new FreeVOParse().setFreeVO(bodyVO, "vfree0", "vfree", null,
						"cmangid", false);
			}
		}

		timer.addExecutePhase("计算自由项");

		String strVendorBase = head.getCvendorbaseid();
		if ((head.getCvendormangid() != null)
				&& (((strVendorBase == null) || (strVendorBase.trim()
						.equals(""))))) {
			Object oTemp = null;
			try {
				oTemp = CacheTool
						.getColumnValue("bd_cumandoc", "pk_cumandoc",
								"pk_cubasdoc", new String[] { head
										.getCvendormangid() });
			} catch (Exception e) {
				SCMEnv.out(e);
			}
			if (oTemp != null) {
				Object[] oo = (Object[]) (Object[]) oTemp;
				if ((oo != null) && (oo.length > 0) && (oo[0] != null))
					strVendorBase = oo[0].toString();
			}
			head.setCvendorbaseid(strVendorBase);
		}

		timer.addExecutePhase("得到供应商基本PiPqPublicUIClass.getAResultFromTable");

		String pk_corp = head.getPk_purcorp();
		UIRefPane pane2 = (UIRefPane) getInvBillPanel().getHeadItem("cdeptid")
				.getComponent();

		pane2.getRefModel().setPk_corp(pk_corp);

		pane2 = (UIRefPane) getInvBillPanel().getHeadItem("cemployeeid")
				.getComponent();

		pane2.getRefModel().setPk_corp(pk_corp);

		if (head.getCemployeeid() != null) {
			pane2.setPK(head.getCemployeeid());
		}

		getInvBillPanel().setBillValueVO(curVO);

		InvoiceItemVO voFirstItem = curVO.getBodyVO()[0];

		getInvBillPanel().getHeadItem("ccurrencytypeid").setValue(
				voFirstItem.getCcurrencytypeid());

		head.setCcurrencytypeid(voFirstItem.getCcurrencytypeid());

		resetHeadCurrDigits();
		getInvBillPanel().getHeadItem("nexchangeotobrate").setValue(
				voFirstItem.getNexchangeotobrate());
		head.setNexchangeotobrate(voFirstItem.getNexchangeotobrate());
		getInvBillPanel().getHeadItem("nexchangeotoarate").setValue(
				voFirstItem.getNexchangeotoarate());
		head.setNexchangeotoarate(voFirstItem.getNexchangeotoarate());

		getInvBillPanel().getHeadItem("idiscounttaxtype").setValue(
				voFirstItem.getIdiscounttaxtype());
		getInvBillPanel().getHeadItem("ntaxrate").setValue(
				voFirstItem.getNtaxrate());
		setCurrRateToBillHead(head.getCcurrencytypeid(), head
				.getNexchangeotobrate(), head.getNexchangeotoarate());

		if (curVO.getHeadVO().getCfreecustid() == null) {
			getInvBillPanel().getHeadItem("cfreecustid").setValue(null);
			((UIRefPane) getInvBillPanel().getHeadItem("caccountbankid")
					.getComponent()).setButtonVisible(true);

			setDefaultPhoneForAVendor(strVendorBase);

			UIRefPane pane1 = (UIRefPane) getInvBillPanel().getHeadItem(
					"caccountbankid").getComponent();
			((AccountsForVendorRefModel) pane1.getRef().getRefModel())
					.setCvendorbaseid(strVendorBase);
			if (head.getCaccountbankid() != null) {
				pane1.setPK(head.getCaccountbankid());
				String strAccount = null;
				try {
					Object oTemp = CacheTool.getColumnValue("bd_custbank",
							"pk_custbank", "account", new String[] { head
									.getCaccountbankid() });
					if (oTemp != null)
						strAccount = ((Object[]) (Object[]) oTemp)[0]
								.toString();

					timer.addExecutePhase("queryResultsFromAnyTable");
				} catch (Exception e) {
					SCMEnv.out(e);
					SCMEnv.out("取供应商默认银行时出现异常!");
				}

				getInvBillPanel().setHeadItem("cvendoraccount", strAccount);
			} else if (getCurOperState() == 1) {
				setDefaultBankAccountForAVendor(strVendorBase);
			}

			if (head.getCaccountbankid() != null) {
				Object oTemp = null;
				try {
					oTemp = CacheTool.getColumnValue("bd_custbank",
							"pk_custbank", "account", new String[] { head
									.getCaccountbankid() });
				} catch (Exception e) {
					SCMEnv.out(e);
				}
				if (oTemp != null)
					getInvBillPanel().setHeadItem("cvendoraccount",
							((Object[]) (Object[]) oTemp)[0].toString());

				timer.addExecutePhase("PiPqPublicUIClass.getAResultFromTable");
			}
		} else {
			((UIRefPane) getInvBillPanel().getHeadItem("caccountbankid")
					.getComponent()).setButtonVisible(false);

			setDefaultInfoForAFreeCust(curVO.getHeadVO().getCfreecustid());
		}

		getInvBillPanel().getHeadItem("nexchangeotobrate").getValue();

		loadBDData();

		timer.addExecutePhase("loadBDData");

		UICheckBox initCheck = (UICheckBox) getInvBillPanel().getHeadItem(
				"finitflag").getComponent();
		if ((head.getFinitflag() == null)
				|| (head.getFinitflag().intValue() == 0))
			initCheck.setSelected(false);
		else {
			initCheck.setSelected(true);
		}

		int selectedIndex = ((UIComboBox) getInvBillPanel().getHeadItem(
				"idiscounttaxtype").getComponent()).getSelectedIndex();
		((UIComboBox) getInvBillPanel().getHeadItem("idiscounttaxtype")
				.getComponent()).setSelectedIndex(selectedIndex);

		((UIRefPane) getInvBillPanel().getHeadItem("vmemo").getComponent())
				.setText(head.getVmemo());

		if (curVO.getBodyVO() != null) {
			getInvBillPanel().getBillModel().execLoadFormula();
		}

		timer.addExecutePhase("execLoadFormula");

		getInvBillPanel().getHeadItem("nexchangeotobrate").getValue();

		setMaxMnyDigit(this.iMaxMnyDigit);

		String sCurrId = getInvBillPanel().getHeadItem("ccurrencytypeid")
				.getValue();
		String sBodyCurrId = null;
		if (curVO.getBodyVO() != null) {
			for (int i = 0; i < curVO.getBodyVO().length; ++i) {
				if (curVO.getBodyVO()[i].getVmemo() == null) {
					getInvBillPanel().setBodyValueAt("", i, "vmemo");
				}

				sBodyCurrId = (String) getInvBillPanel().getBillModel()
						.getValueAt(i, "ccurrencytypeid");
				if ((sBodyCurrId == null) || (sBodyCurrId.equals(""))) {
					getInvBillPanel().getBillModel().setValueAt(sCurrId, i,
							"ccurrencytypeid");

					afterEditWhenBodyCurrency(new BillEditEvent(
							getInvBillPanel().getBodyItem("ccurrencytypeid")
									.getComponent(), sCurrId,
							"ccurrencytypeid", i));

					getInvBillPanel().getBillModel().setRowState(i, 2);
					getInvBillPanel().getBillModel().getValueAt(i,
							"ccurrencytypeid");
					setExchangeRateBody(i, true, null);
				}
				setExchangeRateBody(i, false, curVO.getBodyVO()[i]);

				getInvBillPanel().setBodyValueAt(
						getInvBillPanel().getBodyValueAt(i, "noriginalcurmny"),
						i, "noriginalcurmny");
				getInvBillPanel().setBodyValueAt(
						getInvBillPanel().getBodyValueAt(i, "noriginaltaxmny"),
						i, "noriginaltaxmny");
				getInvBillPanel().setBodyValueAt(
						getInvBillPanel().getBodyValueAt(i, "noriginalsummny"),
						i, "noriginalsummny");
				getInvBillPanel().setBodyValueAt(
						getInvBillPanel().getBodyValueAt(i,
								"noriginalpaymentmny"), i,
						"noriginalpaymentmny");

				getInvBillPanel().setBodyValueAt(
						getInvBillPanel().getBodyValueAt(i, "nmoney"), i,
						"nmoney");
				getInvBillPanel().setBodyValueAt(
						getInvBillPanel().getBodyValueAt(i, "ntaxmny"), i,
						"ntaxmny");
				getInvBillPanel().setBodyValueAt(
						getInvBillPanel().getBodyValueAt(i, "nsummny"), i,
						"nsummny");
				getInvBillPanel().setBodyValueAt(
						getInvBillPanel().getBodyValueAt(i, "npaymentmny"), i,
						"npaymentmny");

				getInvBillPanel().setBodyValueAt(
						getInvBillPanel().getBodyValueAt(i, "nassistcurmny"),
						i, "nassistcurmny");
				getInvBillPanel().setBodyValueAt(
						getInvBillPanel().getBodyValueAt(i, "nassisttaxmny"),
						i, "nassisttaxmny");
				getInvBillPanel().setBodyValueAt(
						getInvBillPanel().getBodyValueAt(i, "nassistsummny"),
						i, "nassistsummny");
				getInvBillPanel().setBodyValueAt(
						getInvBillPanel().getBodyValueAt(i, "nassistpaymny"),
						i, "nassistpaymny");
			}

		}

		PuTool.loadSourceInfoAll(getInvBillPanel(), "25");
		timer.addExecutePhase("加载来源、源头");

		getInvBillPanel().getBillModel().updateValue();

		execHeadTailFormula(curVO);

		timer.addExecutePhase("execHeadTailFormula");

		if (getCurOperState() == 1) {
			showHintMessage(getHeadHintText()
					+ "|"
					+ NCLangRes.getInstance().getStrByID("40040401",
							"UPP40040401-000060") + "|");
		} else
			showHintMessage(getHeadHintText()
					+ "|"
					+ NCLangRes.getInstance().getStrByID("40040401",
							"UPP40040401-000032") + "|");

		timer.addExecutePhase("其他操作");

		timer.showAllExecutePhase("setVOToBillPanel");

		dispPlanPrice(true);
	}

	private void resetHeadCurrDigits() {
		String sCurrId = getInvBillPanel().getHeadItem("ccurrencytypeid")
				.getValue();
		if ((sCurrId == null) || (sCurrId.trim().length() <= 0))
			return;
		int[] iaExchRateDigit = this.m_cardPoPubSetUI2.getBothExchRateDigit(
				getPk_corp(), sCurrId);

		getInvBillPanel().getHeadItem("nexchangeotobrate").setDecimalDigits(
				iaExchRateDigit[0]);

		getInvBillPanel().getHeadItem("nexchangeotoarate").setDecimalDigits(
				iaExchRateDigit[1]);
	}

	protected static void resetBodyValueRelated_Curr(String pk_corp,
			String strHeadCurId, BillModel billModel,
			BusinessCurrencyRateUtil bca, int iLen, POPubSetUI2 setUi) {
		String[] saMnyItem = { "noriginalcurmny", "noriginaltaxmny",
				"noriginalsummny", "noriginalpaymentmny", "nmoney", "ntaxmny",
				"nsummny", "npaymentmny", "nassistcurmny", "nassisttaxmny",
				"nassistsummny", "nassistpaymny" };

		int iMnyLen = saMnyItem.length;

		int iMaxMnyDigit = 0;

		boolean bOldNeedCalculate = billModel.isNeedCalculate();
		billModel.setNeedCalculate(false);

		for (int i = 0; i < iLen; ++i) {
			setRowDigits_ExchangeRate(pk_corp, i, billModel, setUi);
			billModel.setValueAt(billModel.getValueAt(i, "nexchangeotobrate"),
					i, "nexchangeotobrate");

			billModel.setValueAt(billModel.getValueAt(i, "nexchangeotoarate"),
					i, "nexchangeotoarate");

			for (int j = 0; j < iMnyLen; ++j) {
				billModel.setValueAt(billModel.getValueAt(i, saMnyItem[j]), i,
						saMnyItem[j]);
			}

			if (billModel.getItemByKey(saMnyItem[0]).getDecimalDigits() > iMaxMnyDigit) {
				iMaxMnyDigit = billModel.getItemByKey(saMnyItem[0])
						.getDecimalDigits();
			}

		}

		for (int i = 0; i < iMnyLen; ++i) {
			billModel.getItemByKey(saMnyItem[0]).setDecimalDigits(iMaxMnyDigit);
			billModel.reCalcurate(billModel.getBodyColByKey(saMnyItem[i]));
		}
		billModel.setNeedCalculate(bOldNeedCalculate);
	}

	private void dispPlanPrice(boolean bCard) {
		if ((bCard) && (getInvBillPanel().getRowCount() > 0)) {
			BillItem item = getInvBillPanel().getBodyItem("nplanprice");
			if ((item != null) && (item.isShow())) {
				int nRowCount = getInvBillPanel().getRowCount();
				String[] cMangID = new String[nRowCount];
				for (int i = 0; i < nRowCount; ++i)
					cMangID[i] = ((String) getInvBillPanel().getBodyValueAt(i,
							"cmangid"));
				UFDouble[] nPrice = queryPlanPrices(cMangID, getInvBillPanel()
						.getHeadItem("cstoreorganization").getValue());
				if (nPrice != null) {
					for (int i = 0; i < nRowCount; ++i)
						getInvBillPanel().setBodyValueAt(nPrice[i], i,
								"nplanprice");
				}
			}
		} else if ((!bCard)
				&& (getInvListPanel().getBodyBillModel().getRowCount() > 0)) {
			BillItem item = getInvListPanel().getBodyItem("nplanprice");
			if ((item != null) && (item.isShow())) {
				int nRowCount = getInvListPanel().getBodyBillModel()
						.getRowCount();
				String[] cMangID = new String[nRowCount];
				for (int i = 0; i < nRowCount; ++i)
					cMangID[i] = ((String) getInvListPanel().getBodyBillModel()
							.getValueAt(i, "cmangid"));
				UFDouble[] nPrice = queryPlanPrices(
						cMangID,
						(String) getInvListPanel()
								.getHeadBillModel()
								.getValueAt(getCurVOPos(), "cstoreorganization"));
				if (nPrice != null)
					for (int i = 0; i < nRowCount; ++i)
						getInvListPanel().getBodyBillModel().setValueAt(
								nPrice[i], i, "nplanprice");
			}
		}
	}

	private void shiftShowModeTo(int stateIndex) {
		if ((stateIndex == 1) && (getCurPanelMode() != 1)) {
			remove(getCurPanel());
			setCurPanelMode(1);
			initList();
			updateUI();
			setButtons(this.m_btnTree.getButtonArray());

			updateButtons();
		} else {
			if ((stateIndex != 0) || (getCurPanelMode() == 0)) {
				return;
			}

			remove(getCurPanel());
			setCurPanelMode(0);
			initCard();
			updateUI();
			setButtons(this.m_btnTree.getButtonArray());

			updateButtons();
		}
	}

	private void showSelectedInvoice() {
		int row = getCurVOPos();

		if ((row >= 0) && (row < getInvVOs().length)) {
			InvoiceVO curVO = getInvVOs()[row];
			if (!loadItemsForInvoiceVOs(new InvoiceVO[] { curVO })) {
				setVoBodyToListPanle(-1);
			} else
				setVoBodyToListPanle(row);
		} else {
			setVoBodyToListPanle(-1);
		}
	}

	public void valueChanged(ListSelectionEvent e) {
		int iCount = getInvListPanel().getHeadTable().getRowCount();
		for (int i = 0; i < iCount; ++i) {
			getInvListPanel().getHeadBillModel().setRowState(i, 0);
		}

		int[] iaSelectedRow = getInvListPanel().getHeadTable()
				.getSelectedRows();
		if ((iaSelectedRow == null) || (iaSelectedRow.length == 0)) {
			setSelectedRowCount(0);
		} else {
			iCount = iaSelectedRow.length;

			for (int i = 0; i < iCount; ++i) {
				getInvListPanel().getHeadBillModel().setRowState(
						iaSelectedRow[i], 4);
			}
			setSelectedRowCount(iCount);
			if (iCount == 1) {
				int nCurIndex = PuTool.getIndexBeforeSort(getInvListPanel(),
						iaSelectedRow[0]);
				setCurVOPos(nCurIndex);
			} else {
				setCurVOPos(-1);
			}
			showSelectedInvoice();
		}

		setButtonsAndPanelState();
		updateButtons();
	}

	private void setBodyDefPK(BillEditEvent event) {
		if (event.getKey().equals("vdef1")) {
			DefSetTool.afterEditBody(getInvBillPanel().getBillModel(), event
					.getRow(), "vdef1", "pk_defdoc1");
		} else if (event.getKey().equals("vdef2")) {
			DefSetTool.afterEditBody(getInvBillPanel().getBillModel(), event
					.getRow(), "vdef2", "pk_defdoc2");
		} else if (event.getKey().equals("vdef3")) {
			DefSetTool.afterEditBody(getInvBillPanel().getBillModel(), event
					.getRow(), "vdef3", "pk_defdoc3");
		} else if (event.getKey().equals("vdef4")) {
			DefSetTool.afterEditBody(getInvBillPanel().getBillModel(), event
					.getRow(), "vdef4", "pk_defdoc4");
		} else if (event.getKey().equals("vdef5")) {
			DefSetTool.afterEditBody(getInvBillPanel().getBillModel(), event
					.getRow(), "vdef5", "pk_defdoc5");
		} else if (event.getKey().equals("vdef6")) {
			DefSetTool.afterEditBody(getInvBillPanel().getBillModel(), event
					.getRow(), "vdef6", "pk_defdoc6");
		} else if (event.getKey().equals("vdef7")) {
			DefSetTool.afterEditBody(getInvBillPanel().getBillModel(), event
					.getRow(), "vdef7", "pk_defdoc7");
		} else if (event.getKey().equals("vdef8")) {
			DefSetTool.afterEditBody(getInvBillPanel().getBillModel(), event
					.getRow(), "vdef8", "pk_defdoc8");
		} else if (event.getKey().equals("vdef9")) {
			DefSetTool.afterEditBody(getInvBillPanel().getBillModel(), event
					.getRow(), "vdef9", "pk_defdoc9");
		} else if (event.getKey().equals("vdef10")) {
			DefSetTool.afterEditBody(getInvBillPanel().getBillModel(), event
					.getRow(), "vdef10", "pk_defdoc10");
		} else if (event.getKey().equals("vdef11")) {
			DefSetTool.afterEditBody(getInvBillPanel().getBillModel(), event
					.getRow(), "vdef11", "pk_defdoc11");
		} else if (event.getKey().equals("vdef12")) {
			DefSetTool.afterEditBody(getInvBillPanel().getBillModel(), event
					.getRow(), "vdef12", "pk_defdoc12");
		} else if (event.getKey().equals("vdef13")) {
			DefSetTool.afterEditBody(getInvBillPanel().getBillModel(), event
					.getRow(), "vdef13", "pk_defdoc13");
		} else if (event.getKey().equals("vdef14")) {
			DefSetTool.afterEditBody(getInvBillPanel().getBillModel(), event
					.getRow(), "vdef14", "pk_defdoc14");
		} else if (event.getKey().equals("vdef15")) {
			DefSetTool.afterEditBody(getInvBillPanel().getBillModel(), event
					.getRow(), "vdef15", "pk_defdoc15");
		} else if (event.getKey().equals("vdef16")) {
			DefSetTool.afterEditBody(getInvBillPanel().getBillModel(), event
					.getRow(), "vdef16", "pk_defdoc16");
		} else if (event.getKey().equals("vdef17")) {
			DefSetTool.afterEditBody(getInvBillPanel().getBillModel(), event
					.getRow(), "vdef17", "pk_defdoc17");
		} else if (event.getKey().equals("vdef18")) {
			DefSetTool.afterEditBody(getInvBillPanel().getBillModel(), event
					.getRow(), "vdef18", "pk_defdoc18");
		} else if (event.getKey().equals("vdef19")) {
			DefSetTool.afterEditBody(getInvBillPanel().getBillModel(), event
					.getRow(), "vdef19", "pk_defdoc19");
		} else if (event.getKey().equals("vdef20"))
			DefSetTool.afterEditBody(getInvBillPanel().getBillModel(), event
					.getRow(), "vdef20", "pk_defdoc20");
	}

	private void setHeadDefPK(BillEditEvent event) {
		if (event.getKey().equals("vdef1")) {
			DefSetTool.afterEditHead(getInvBillPanel().getBillData(), "vdef1",
					"pk_defdoc1");
		} else if (event.getKey().equals("vdef2")) {
			DefSetTool.afterEditHead(getInvBillPanel().getBillData(), "vdef2",
					"pk_defdoc2");
		} else if (event.getKey().equals("vdef3")) {
			DefSetTool.afterEditHead(getInvBillPanel().getBillData(), "vdef3",
					"pk_defdoc3");
		} else if (event.getKey().equals("vdef4")) {
			DefSetTool.afterEditHead(getInvBillPanel().getBillData(), "vdef4",
					"pk_defdoc4");
		} else if (event.getKey().equals("vdef5")) {
			DefSetTool.afterEditHead(getInvBillPanel().getBillData(), "vdef5",
					"pk_defdoc5");
		} else if (event.getKey().equals("vdef6")) {
			DefSetTool.afterEditHead(getInvBillPanel().getBillData(), "vdef6",
					"pk_defdoc6");
		} else if (event.getKey().equals("vdef7")) {
			DefSetTool.afterEditHead(getInvBillPanel().getBillData(), "vdef7",
					"pk_defdoc7");
		} else if (event.getKey().equals("vdef8")) {
			DefSetTool.afterEditHead(getInvBillPanel().getBillData(), "vdef8",
					"pk_defdoc8");
		} else if (event.getKey().equals("vdef9")) {
			DefSetTool.afterEditHead(getInvBillPanel().getBillData(), "vdef9",
					"pk_defdoc9");
		} else if (event.getKey().equals("vdef10")) {
			DefSetTool.afterEditHead(getInvBillPanel().getBillData(), "vdef10",
					"pk_defdoc10");
		} else if (event.getKey().equals("vdef11")) {
			DefSetTool.afterEditHead(getInvBillPanel().getBillData(), "vdef11",
					"pk_defdoc11");
		} else if (event.getKey().equals("vdef12")) {
			DefSetTool.afterEditHead(getInvBillPanel().getBillData(), "vdef12",
					"pk_defdoc12");
		} else if (event.getKey().equals("vdef13")) {
			DefSetTool.afterEditHead(getInvBillPanel().getBillData(), "vdef13",
					"pk_defdoc13");
		} else if (event.getKey().equals("vdef14")) {
			DefSetTool.afterEditHead(getInvBillPanel().getBillData(), "vdef14",
					"pk_defdoc14");
		} else if (event.getKey().equals("vdef15")) {
			DefSetTool.afterEditHead(getInvBillPanel().getBillData(), "vdef15",
					"pk_defdoc15");
		} else if (event.getKey().equals("vdef16")) {
			DefSetTool.afterEditHead(getInvBillPanel().getBillData(), "vdef16",
					"pk_defdoc16");
		} else if (event.getKey().equals("vdef17")) {
			DefSetTool.afterEditHead(getInvBillPanel().getBillData(), "vdef17",
					"pk_defdoc17");
		} else if (event.getKey().equals("vdef18")) {
			DefSetTool.afterEditHead(getInvBillPanel().getBillData(), "vdef18",
					"pk_defdoc18");
		} else if (event.getKey().equals("vdef19")) {
			DefSetTool.afterEditHead(getInvBillPanel().getBillData(), "vdef19",
					"pk_defdoc19");
		} else if (event.getKey().equals("vdef20"))
			DefSetTool.afterEditHead(getInvBillPanel().getBillData(), "vdef20",
					"pk_defdoc20");
	}

	/**
	 * @功能:返回公司的上级公司编码
	 * @return add by cm
	 */
	public String getParentCorpCode1() {

		String ParentCorp = new String();
		String key = ClientEnvironment.getInstance().getCorporation()
				.getFathercorp();
		try {
			CorpVO corpVO = CorpBO_Client.findByPrimaryKey(key);
			ParentCorp = corpVO.getUnitcode();
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ParentCorp;
	}

	private void onCardPrint() {
		InvoiceVO vo = getInvVOs()[getCurVOPos()];
		ArrayList aryRslt = new ArrayList();
		// aryRslt.add(vo);
		// 增加打印预览按照存货和规格合并,xiaolong_fan.2012-12-24.
		InvoiceVO tempVO = null;
		CircularlyAccessibleValueObject[] vos = null;
		if (getParentCorpCode1().equals("10395")) {
			tempVO = (InvoiceVO) vo.clone();
			vos = vo.getChildrenVO().clone();
			int length = vo.getChildrenVO().length;
			Map dataMap = new HashMap();
			for (int i = 0; i < length; i++) {
				InvoiceItemVO itemVO = (InvoiceItemVO) vo.getChildrenVO()[i];
				// 存货ID
				String basID = itemVO.getCbaseid() != null ? itemVO
						.getCbaseid().trim() : "";
				// 入库日期
				String cinvoice_bid = itemVO.getPrimaryKey() != null ? itemVO
						.getPrimaryKey().trim() : "";
				Object obj = null;
				try {
					obj = getUAPQuery()
							.executeQuery(
									"select dbilldate from ic_general_h where cgeneralhid=(select cupsourcebillid from po_invoice_b where cinvoice_bid='"
											+ cinvoice_bid + "')",
									new ColumnProcessor());
				} catch (FTSBusinessException e) {
					e.printStackTrace();
				} catch (BusinessException e) {
					e.printStackTrace();
				}
				String date = obj != null ? obj.toString() : "";
				String key = basID + date;
				if (dataMap.containsKey(key)) {
					InvoiceItemVO tempItemVO = new InvoiceItemVO();
					InvoiceItemVO oldvo = (InvoiceItemVO) dataMap.get(key);
					String[] fields = tempItemVO.getAttributeNames();
					for (int j = 0; j < fields.length; j++) {
						tempItemVO.setAttributeValue(fields[j], (oldvo)
								.getAttributeValue(fields[j]));
					}

					// InvoiceItemVO tempItemVO = (InvoiceItemVO)
					// dataMap.get(key);
					// 发票数量
					tempItemVO.setNinvoicenum(tempItemVO.getNinvoicenum().add(
							itemVO.getNinvoicenum()));
					// 金额
					tempItemVO.setNoriginalcurmny(tempItemVO
							.getNoriginalcurmny().add(
									itemVO.getNoriginalcurmny()));
					// 税前金额
					// 税金
					tempItemVO.setNoriginaltaxmny(tempItemVO
							.getNoriginaltaxmny().add(
									itemVO.getNoriginaltaxmny()));
					// 税后金额(价税合计)
					tempItemVO.setNoriginalsummny(tempItemVO
							.getNoriginalsummny().add(
									itemVO.getNoriginalsummny()));

					dataMap.put(key, tempItemVO);
				} else {
					dataMap.put(key, itemVO);
				}
			}
			InvoiceItemVO[] itemVOs = (InvoiceItemVO[]) dataMap.values()
					.toArray(new InvoiceItemVO[dataMap.size()]);
			vo.setChildrenVO(itemVOs);
		}
		// end by xiaolong_fan.
		aryRslt.add(vo);
		if (this.printCard == null)
			this.printCard = new ScmPrintTool(this, getInvBillPanel(), aryRslt,
					getModuleCode());
		else
			try {
				this.printCard.setData(aryRslt);
			} catch (Exception e1) {
				SCMEnv.out(e1);
				return;
			}
		try {
			this.printCard.onCardPrint(getInvBillPanel(), getInvListPanel(),
					"25");
			MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000270"), this.printCard
					.getPrintMessage());
		} catch (BusinessException e) {
			SCMEnv.out(e);
		}
		// 更新界面.xiaolong_fan.2012-12-24.
		if (getParentCorpCode1().equals("10395")) {
			vo.setChildrenVO(vos);
			getInvBillPanel().setBillValueVO(tempVO);
			getInvBillPanel().getBillModel().setBodyDataVO(vos);
			getInvBillPanel().getBillModel().execLoadFormula();
			getInvBillPanel().updateUI();
		}
		// end by xiaolong_fan.
	}

	private void onCardPrintPreview() {
		InvoiceVO vo = getInvVOs()[getCurVOPos()];
		ArrayList aryRslt = new ArrayList();
		// 增加打印预览按照存货和规格合并,xiaolong_fan.2012-12-24.
		InvoiceVO tempVO = null;
		CircularlyAccessibleValueObject[] vos = null;
		if (getParentCorpCode1().equals("10395")) {
			tempVO = (InvoiceVO) vo.clone();
			vos = vo.getChildrenVO().clone();
			int length = vo.getChildrenVO().length;
			Map dataMap = new HashMap();
			for (int i = 0; i < length; i++) {
				InvoiceItemVO itemVO = (InvoiceItemVO) vo.getChildrenVO()[i];
				// 存货ID
				String basID = itemVO.getCbaseid() != null ? itemVO
						.getCbaseid().trim() : "";
				// 入库日期
				String cinvoice_bid = itemVO.getPrimaryKey() != null ? itemVO
						.getPrimaryKey().trim() : "";
				Object obj = null;
				try {
					obj = getUAPQuery()
							.executeQuery(
									"select dbilldate from ic_general_h where cgeneralhid=(select cupsourcebillid from po_invoice_b where cinvoice_bid='"
											+ cinvoice_bid + "')",
									new ColumnProcessor());
				} catch (FTSBusinessException e) {
					e.printStackTrace();
				} catch (BusinessException e) {
					e.printStackTrace();
				}
				String date = obj != null ? obj.toString() : "";
				String key = basID + date;
				if (dataMap.containsKey(key)) {
					InvoiceItemVO tempItemVO = new InvoiceItemVO();
					InvoiceItemVO oldvo = (InvoiceItemVO) dataMap.get(key);
					String[] fields = tempItemVO.getAttributeNames();
					for (int j = 0; j < fields.length; j++) {
						tempItemVO.setAttributeValue(fields[j], (oldvo)
								.getAttributeValue(fields[j]));
					}

					// InvoiceItemVO tempItemVO = (InvoiceItemVO)
					// dataMap.get(key);
					// 发票数量
					tempItemVO.setNinvoicenum(tempItemVO.getNinvoicenum().add(
							itemVO.getNinvoicenum()));
					// 金额
					tempItemVO.setNoriginalcurmny(tempItemVO
							.getNoriginalcurmny().add(
									itemVO.getNoriginalcurmny()));
					// 税前金额
					// 税金
					tempItemVO.setNoriginaltaxmny(tempItemVO
							.getNoriginaltaxmny().add(
									itemVO.getNoriginaltaxmny()));
					// 税后金额(价税合计)
					tempItemVO.setNoriginalsummny(tempItemVO
							.getNoriginalsummny().add(
									itemVO.getNoriginalsummny()));

					dataMap.put(key, tempItemVO);
				} else {
					dataMap.put(key, itemVO);
				}
			}
			InvoiceItemVO[] itemVOs = (InvoiceItemVO[]) dataMap.values()
					.toArray(new InvoiceItemVO[dataMap.size()]);
			vo.setChildrenVO(itemVOs);
		}
		// end by xiaolong_fan.
		if (this.printCard == null)
			this.printCard = new ScmPrintTool(this, getInvBillPanel(), aryRslt,
					getModuleCode());
		else
			try {
				this.printCard.setData(aryRslt);
			} catch (Exception e1) {
				SCMEnv.out(e1);
				return;
			}
		try {
			this.printCard.onCardPrintPreview(getInvBillPanel(),
					getInvListPanel(), "25");
			MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000270"), this.printCard
					.getPrintMessage());
		} catch (BusinessException e) {
			SCMEnv.out(e);
		}
		// 更新界面.xiaolong_fan.2012-12-24.
		if (getParentCorpCode1().equals("10395")) {
			vo.setChildrenVO(vos);
			getInvBillPanel().setBillValueVO(tempVO);
			getInvBillPanel().getBillModel().setBodyDataVO(vos);
			getInvBillPanel().getBillModel().execLoadFormula();
			getInvBillPanel().updateUI();
		}
		// end by xiaolong_fan.

	}

	/** 查询工具 */
	private IUAPQueryBS uapQueryBS;

	/**
	 * <p>
	 * 但表查询服务
	 * 
	 * @return
	 * @throws FTSBusinessException
	 */
	protected IUAPQueryBS getUAPQuery() throws FTSBusinessException {
		if (uapQueryBS == null)
			try {
				uapQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(
						IUAPQueryBS.class.getName());
			} catch (ComponentException e) {
				throw new FTSBusinessException("IUAPQueryBS not found!");
			}
		return uapQueryBS;
	}

	private void onBatchPrint() {
		if (this.printList == null)
			this.printList = new ScmPrintTool(this, getInvBillPanel(),
					getSelectedBills(), getModuleCode());
		else
			try {
				this.printList.setData(getSelectedBills());
			} catch (Exception e1) {
				SCMEnv.out(e1);
				return;
			}
		try {
			this.printList.onBatchPrint(getInvListPanel(), getInvBillPanel(),
					"25");
			MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID(
					"40040401", "UPPSCMCommon-000270"), this.printList
					.getPrintMessage());
		} catch (BusinessException e) {
			SCMEnv.out(e);
		}
	}

	private void onBatchPrintPreview() {
		if (this.printList == null)
			this.printList = new ScmPrintTool(this, getInvBillPanel(),
					getSelectedBills(), getModuleCode());
		else
			try {
				this.printList.setData(getSelectedBills());
			} catch (Exception e1) {
				SCMEnv.out(e1);
				return;
			}
		try {
			this.printList.onBatchPrintPreview(getInvListPanel(),
					getInvBillPanel(), "25");
			MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000270"), this.printList
					.getPrintMessage());
		} catch (BusinessException e) {
			SCMEnv.out(e);
		}
	}

	public void setBillVO(AggregatedValueObject vo) {
		this.m_nLstInvVOPos = getCurVOPos();

		if (getCurPanelMode() == 1)
			setCurVOPos(0);

		setVOToBillPanel();
	}

	public POPubSetUI2 getPoPubSetUi2() {
		if (this.m_cardPoPubSetUI2 == null) {
			this.m_cardPoPubSetUI2 = new POPubSetUI2();
		}
		return this.m_cardPoPubSetUI2;
	}

	public ButtonObject[] getExtendBtns() {
		return null;
	}

	public void onExtendBtnsClick(ButtonObject bo) {
	}

	public void setExtendBtnsStat(int iState) {
	}

	public Object[] getRelaSortObjectArray() {
		return this.m_InvVOs;
	}

	public void doMaintainAction(ILinkMaintainData maintaindata) {
		initi();
		String billID = maintaindata.getBillID();

		setCauditid(billID);

		InvoiceVO vo = null;
		try {
			vo = (InvoiceVO) getVo();
		} catch (Exception e) {
			SCMEnv.out(e);
			MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID(
					"40040401", "UPPSCMCommon-000270"), NCLangRes.getInstance()
					.getStrByID("40040401", "UPPSCMCommon-000428"));
		}

		if (vo == null) {
			setInvVOs(null);
			MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID(
					"40040401", "UPPSCMCommon-000270"), NCLangRes.getInstance()
					.getStrByID("40040401", "UPP40040401-000008"));
			getInvBillPanel().addNew();
			return;
		}

		UIComboBox comItem = (UIComboBox) getInvBillPanel().getHeadItem(
				"iinvoicetype").getComponent();
		getInvBillPanel().getHeadItem("iinvoicetype").setWithIndex(true);
		comItem.addItem(NCLangRes.getInstance().getStrByID("scmpub",
				"UPPscmpub-001146"));
		comItem.addItem(NCLangRes.getInstance().getStrByID("scmpub",
				"UPPscmpub-001147"));
		comItem.addItem(NCLangRes.getInstance().getStrByID("scmpub",
				"UPPscmpub-001148"));
		comItem.addItem(NCLangRes.getInstance().getStrByID("scmpub",
				"UPPscmpub-001149"));
		comItem.setTranslate(true);

		setInvVOs(new InvoiceVO[] { vo });
		setCurVOPos(0);

		setVOToAuditedBillPanel();
		InvoiceItemVO[] bodyVO = vo.getBodyVO();
		for (int i = 0; i < bodyVO.length; ++i) {
			if ((bodyVO[i].getNaccumsettmny() != null)
					&& (Math.abs(bodyVO[i].getNaccumsettmny().doubleValue()) > 0.0D))
				return;

		}

		setButtonsStateBrowseNormal();

		this.m_btnInvBillAudit.setEnabled(false);
		this.m_btnInvSelectAll.setEnabled(false);
		this.m_btnInvDeselectAll.setEnabled(false);
		updateButtons();

		getInvBillPanel().transferFocusTo(0);
	}

	public void doQueryAction(ILinkQueryData querydata) {
		initi();
		String billID = querydata.getBillID();
		String pk_corp = querydata.getPkOrg();

		InvoiceVO vo = null;
		try {
			vo = InvoiceHelper.findByPrimaryKey(billID);
			if (vo == null) {
				MessageDialog.showHintDlg(this, NCLangRes.getInstance()
						.getStrByID("SCMCOMMON", "UPPSCMCommon-000270"),
						NCLangRes.getInstance().getStrByID("SCMCOMMON",
								"UPPSCMCommon-000397"));

				return;
			}
			String strPkCorp = vo.getPk_corp();

			if ((pk_corp != null) && (pk_corp.trim().length() > 0)
					&& (getCorpPrimaryKey() != null)
					&& (getCorpPrimaryKey().trim().length() > 0)
					&& (!pk_corp.equals(getCorpPrimaryKey()))) {
				ButtonObject[] arrButtonObject = this.m_btnTree
						.getButtonArray();
				for (int i = 0; i < arrButtonObject.length; ++i) {
					arrButtonObject[i].setVisible(false);
					updateButton(arrButtonObject[i]);
				}

			}

			SCMQueryConditionDlg queryDlg = new SCMQueryConditionDlg(this);
			if ((queryDlg.getAllTempletDatas() == null)
					|| (queryDlg.getAllTempletDatas().length <= 0)) {
				queryDlg.setTempletID(querydata.getPkOrg(), getModuleCode(),
						ClientEnvironment.getInstance().getUser()
								.getPrimaryKey(), null);
			}

			ArrayList alcorp = new ArrayList();
			alcorp.add(ClientEnvironment.getInstance().getCorporation()
					.getPrimaryKey());
			queryDlg.initCorpRef("po_invoice.pk_purcorp_vir", ClientEnvironment
					.getInstance().getCorporation().getPrimaryKey(), alcorp);
			queryDlg.setCorpRefs("po_invoice.pk_purcorp_vir",
					IDataPowerForInv.REFKEYS);

			ConditionVO[] voaCond = queryDlg.getDataPowerConVOs(strPkCorp,
					IDataPowerForInv.REFKEYS);

			InvoiceVO[] VOs = null;
			VOs = InvoiceHelper.queryInvoiceVOsByCondsMy(new NormalCondVO[] {
					new NormalCondVO("公司", strPkCorp),
					new NormalCondVO("单据ID", billID) }, voaCond);
			if ((VOs == null) || (VOs.length <= 0) || (VOs[0] == null)) {
				MessageDialog.showHintDlg(this, NCLangRes.getInstance()
						.getStrByID("SCMCOMMON", "UPPSCMCommon-000270"),
						NCLangRes.getInstance().getStrByID("common",
								"SCMCOMMON000000161"));

				return;
			}
			billID = VOs[0].getPrimaryKey();
		} catch (Exception e) {
			SCMEnv.out(e);
			MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID(
					"40040401", "UPPSCMCommon-000270"), NCLangRes.getInstance()
					.getStrByID("40040401", "UPPSCMCommon-000428"));
		}

		setCauditid(billID);

		UIComboBox comItem = (UIComboBox) getInvBillPanel().getHeadItem(
				"iinvoicetype").getComponent();
		getInvBillPanel().getHeadItem("iinvoicetype").setWithIndex(true);
		comItem.addItem(NCLangRes.getInstance().getStrByID("scmpub",
				"UPPscmpub-001146"));
		comItem.addItem(NCLangRes.getInstance().getStrByID("scmpub",
				"UPPscmpub-001147"));
		comItem.addItem(NCLangRes.getInstance().getStrByID("scmpub",
				"UPPscmpub-001148"));
		comItem.addItem(NCLangRes.getInstance().getStrByID("scmpub",
				"UPPscmpub-001149"));
		comItem.setTranslate(true);

		setInvVOs(new InvoiceVO[] { vo });
		setCurVOPos(0);

		setVOToAuditedBillPanel();

		getInvBillPanel().setEnabled(false);
	}

	public void doAddAction(ILinkAddData adddata) {
	}

	public void doApproveAction(ILinkApproveData approvedata) {
		if (approvedata == null) {
			return;
		}
		String billID = approvedata.getBillID();
		String pk_corp = approvedata.getPkOrg();

		initi();

		setCauditid(billID);

		InvoiceVO vo = null;
		try {
			vo = (InvoiceVO) getVo();
			setInvVOs(new InvoiceVO[] { vo });
		} catch (Exception e) {
			SCMEnv.out(e);
			MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID(
					"40040401", "UPPSCMCommon-000270"), NCLangRes.getInstance()
					.getStrByID("40040401", "UPPSCMCommon-000428"));
		}

		if (vo == null) {
			setInvVOs(null);
			MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID(
					"40040401", "UPPSCMCommon-000270"), NCLangRes.getInstance()
					.getStrByID("40040401", "UPP40040401-000008"));
			getInvBillPanel().addNew();

			for (int i = 0; i < this.aryForAudit.length; ++i) {
				this.aryForAudit[i].setEnabled(false);
				if (this.aryForAudit[i].getChildCount() > 0) {
					for (int j = 0; j < this.aryForAudit[i].getChildCount(); ++j)
						this.aryForAudit[i].getChildButtonGroup()[j]
								.setEnabled(false);
				}
			}
			for (int i = 0; i < this.aryForAudit.length; ++i) {
				updateButton(this.aryForAudit[i]);
			}
			return;
		}

		for (int i = 0; i < this.aryForAudit.length; ++i) {
			this.aryForAudit[i].setEnabled(true);
			if (this.aryForAudit[i].getChildCount() > 0) {
				for (int j = 0; j < this.aryForAudit[i].getChildCount(); ++j)
					this.aryForAudit[i].getChildButtonGroup()[j]
							.setEnabled(true);
			}
		}
		for (int i = 0; i < this.aryForAudit.length; ++i) {
			updateButton(this.aryForAudit[i]);
		}

		setInvVOs(new InvoiceVO[] { vo });
		setCurVOPos(0);

		setCurOperState(2);

		setVOToAuditedBillPanel();

		getInvBillPanel().setEnabled(false);

		if ((pk_corp != null) && (pk_corp.trim().length() > 0)
				&& (getCorpPrimaryKey() != null)
				&& (getCorpPrimaryKey().trim().length() > 0))
			if (pk_corp.equals(getCorpPrimaryKey())) {
				setButtons(this.m_btnTree.getButtonArray());

				setButtonsStateInit();
				setButtonsStateBrowseNormal();
				updateButtons();
			} else {
				setButtons(this.aryForAudit);
			}
	}

	private boolean negativeAndPlusCtrl(InvoiceVO billVO) throws Exception {
		InvoiceItemVO[] newItems = (InvoiceItemVO[]) (InvoiceItemVO[]) billVO
				.getChildrenVO();

		InvoiceVO oldVO = getInvVOs()[getCurVOPos()];
		InvoiceItemVO[] oldItems = (InvoiceItemVO[]) (InvoiceItemVO[]) oldVO
				.getChildrenVO();

		Vector vTemp = new Vector();
		String sUpSourceRowID = null;
		String sUpSourceType = null;
		for (int i = 0; i < newItems.length; ++i) {
			sUpSourceRowID = newItems[i].getCupsourcebillrowid();
			if (sUpSourceRowID != null)
				vTemp.addElement(sUpSourceRowID);
			if (newItems[i].getCupsourcebilltype() == null)
				continue;
			sUpSourceType = newItems[i].getCupsourcebilltype();
		}

		if ((vTemp.size() == 0) || (sUpSourceType == null))
			return true;

		String[] sUpSourceRowIDs = new String[vTemp.size()];
		vTemp.copyInto(sUpSourceRowIDs);
		if ((!sUpSourceType.equals("21")) && (!sUpSourceType.equals("61"))
				&& (!sUpSourceType.equals("45"))
				&& (!sUpSourceType.equals("47")))
			return true;

		Object[][] oTemp = (Object[][]) null;
		try {
			String beanName = IInvoiceD.class.getName();
			IInvoiceD bo = (IInvoiceD) NCLocator.getInstance().lookup(beanName);
			Object o = bo.queryRelatedData(sUpSourceType, sUpSourceRowIDs);
			if (o != null)
				oTemp = (Object[][]) (Object[][]) o;
		} catch (Exception e) {
			SCMEnv.out(e);
			throw e;
		}

		if (oTemp == null)
			return true;
		for (int i = 0; i < newItems.length; ++i) {
			sUpSourceRowID = newItems[i].getCupsourcebillrowid();
			if (sUpSourceRowID == null)
				continue;
			if (newItems[i].getStatus() != 3) {
				UFDouble dInvoiceNum = newItems[i].getNinvoicenum();
				if (dInvoiceNum != null) {
					UFDouble dOldInvoiceNum = new UFDouble(0);
					for (int j = 0; j < oldItems.length; ++j) {
						if ((sUpSourceRowID.equals(oldItems[j]
								.getCupsourcebillrowid()))
								&& (oldItems[j].getCinvoice_bid() != null)) {
							dOldInvoiceNum = oldItems[j].getNinvoicenum();
							break;
						}
					}
					for (int j = 0; j < sUpSourceRowIDs.length; ++j)
						if (sUpSourceRowID.equals(sUpSourceRowIDs[j])) {
							Object o1 = oTemp[j][0];
							Object o2 = oTemp[j][1];
							if ((o1 != null) && (o2 != null)) {
								UFDouble dAccumInvoiceNum = new UFDouble(o1
										.toString());
								UFDouble nNum = new UFDouble(o2.toString());
								if ((dInvoiceNum.doubleValue()
										- dOldInvoiceNum.doubleValue() + dAccumInvoiceNum
										.doubleValue())
										* nNum.doubleValue() < 0.0D)
									return false;
							}
						}
				}
			}
		}
		return true;
	}

	private void setDefaultValueByUser() {
		UIRefPane cemployeeid = (UIRefPane) getInvBillPanel().getHeadItem(
				"cemployeeid").getComponent();

		if ((cemployeeid != null) && (cemployeeid.getRefPK() == null)) {
			IUserManageQuery qrySrv = (IUserManageQuery) NCLocator
					.getInstance().lookup(IUserManageQuery.class.getName());
			PsndocVO voPsnDoc = null;
			try {
				voPsnDoc = qrySrv.getPsndocByUserid(ClientEnvironment
						.getInstance().getCorporation().getPrimaryKey(),
						getCurOperator());
			} catch (BusinessException be) {
				SCMEnv.out(be);
			}
			if (voPsnDoc != null) {
				cemployeeid.setPK(voPsnDoc.getPk_psndoc());

				UIRefPane cdeptid = (UIRefPane) getInvBillPanel().getHeadItem(
						"cdeptid").getComponent();
				cdeptid.setPK(voPsnDoc.getPk_deptdoc());
			}
		}
	}

	private boolean isNeedSendAudit(int iBillStatus) {
		boolean isNeedSendToAuditQ = iBillStatus == BillStatus.AUDITFAIL
				.intValue();

		if ((iBillStatus == -1) && (this.m_bizButton != null)
				&& (this.m_bizButton.getTag() != null)) {
			isNeedSendToAuditQ = BusiBillManageTool.isNeedSendToAudit("25",
					getClientEnvironment().getCorporation().getPrimaryKey(),
					this.m_bizButton.getTag(), null, getClientEnvironment()
							.getUser().getPrimaryKey());
		}

		if (iBillStatus == BillStatus.FREE.intValue()) {
			String billid = getInvVOs()[getCurVOPos()].getHeadVO()
					.getCinvoiceid();
			String cbizType = getInvVOs()[getCurVOPos()].getHeadVO()
					.getCbiztype();
			if (PuPubVO.getString_TrimZeroLenAsNull(cbizType) != null) {
				isNeedSendToAuditQ = BusiBillManageTool
						.isNeedSendToAudit("25", getClientEnvironment()
								.getCorporation().getPrimaryKey(), cbizType,
								billid, getClientEnvironment().getUser()
										.getPrimaryKey());
			}

		}

		this.m_btnSendAudit.setEnabled(isNeedSendToAuditQ);
		updateButton(this.m_btnSendAudit);

		return isNeedSendToAuditQ;
	}
	

	
	//edit by yqq xml导出  2016-11-02
	 
	public void onxmlDc() {

		String sVinvoiceCode = null;
    	String sFilePathDir = null;
	
			// 打开文件
			if (getChooser().showSaveDialog(this) == javax.swing.JFileChooser.CANCEL_OPTION) return;
			sFilePathDir = getChooser().getSelectedFile().toString();
		
		if (sFilePathDir == null) {
			showHintMessage("请输入文件名保存!");
			return;
		}

		try {

			// 依当前是列表还是卡片界面而定导出内容
			if (m_iCurPanel == INV_PANEL_CARD) { // 浏览
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000119")/** @res "正在导出，请稍候..."*/);
				
				// 准备数据
				InvoiceVO voBill = (InvoiceVO)getInvBillPanel().getBillValueVO(InvoiceVO.class.getName(), InvoiceHeaderVO.class.getName(), InvoiceItemVO.class.getName());

				if (voBill.getParentVO() == null) {
					voBill.setParentVO(new InvoiceHeaderVO());
				}
				if ((voBill.getChildrenVO() == null) || (voBill.getChildrenVO().length == 0) || (voBill.getChildrenVO()[0] == null)) {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000049")/** @res* "请先查询或录入单据。"*/);
		//			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH003")/** @res "请选择要处理的数据！"*/);
					return;
				}


					sVinvoiceCode = voBill.getHeadVO().getVinvoicecode();
					onExportXML(new InvoiceVO[] {voBill}, sFilePathDir);
					showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000120")/** @res* "导出完成"*/);
					return;				
			} 

			
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000121")/* @res "导出出错" */);
			showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000121")/* @res "导出出错" */
					+ nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000330")/* @res "：" */
					+ e.getMessage() + "," + nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000331")/* @res "文件路径" */
					+ ":" + sFilePathDir);
		}
	}
	

	protected javax.swing.JFileChooser m_chooser = null;	
	protected javax.swing.JFileChooser getChooser() {
		if (m_chooser == null) {
			m_chooser = new JFileChooser();
			m_chooser.setDialogType(JFileChooser.SAVE_DIALOG);
		}
		return m_chooser;
	}
	
   // 导出XML yqq 2016-11-03 
	 
	public void onExportXML(InvoiceVO[] billvos, String filename) {
		if (billvos == null || billvos.length <= 0 || filename == null) return;
		try {

			MessageDialog.showInputDlg(this, "请输入外部系统编码", "请输入外部系统编码:", "25", 5);

			IPFxxEJBService export = (IPFxxEJBService) NCLocator.getInstance().lookup(IPFxxEJBService.class.getName());
			Document outdocs = export.exportBills(billvos, getClientEnvironment().getAccount().getAccountCode(), getClientEnvironment().getCorporation().getPrimaryKey(), "25", "25");

			FileUtils.writeDocToXMLFile(outdocs, filename);
		} catch (Exception e) {
			showErrorMessage(e.getMessage());
		}
	}
	
	
	
	/*
	 * Edit 列表导出xml 2016-11-16 yqq 
	 */		
	public void onxmlDch() {

		String sVinvoiceCode = null;
    	String sFilePathDir = null;
	
			// 打开文件
			if (getChooser().showSaveDialog(this) == javax.swing.JFileChooser.CANCEL_OPTION) return;
			sFilePathDir = getChooser().getSelectedFile().toString();
		
		if (sFilePathDir == null) {
			showHintMessage("请输入文件名保存!");
			return;
		}
		try {

			// 依当前是列表还是卡片界面而定导出内容
			if (m_iCurPanel1 == INV_PANEL_LIST) {
				int iSelListHeadRowCount = getInvListPanel().getHeadTable().getSelectedRowCount();
				
				ArrayList alBill = getSelectedBills();  //取得列表界面值
				
				if (alBill == null || alBill.size() <= 0 || alBill.get(0) == null) {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000049")/** @res* "请先查询或录入单据。"*/);
	//				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH003")/** @res "请先查询或录入单据！"*/);
					return;
				}
				
					sVinvoiceCode = ((InvoiceVO) alBill.get(0)).getHeadVO().getVinvoicecode();
					onExportXML((InvoiceVO[]) alBill.toArray(new InvoiceVO[alBill.size()]), sFilePathDir);
					showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000120")/** @res* "导出完成"*/);
					return;
			}
			
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000121")/* @res "导出出错" */);
			showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPP4008bill-000121")/* @res "导出出错" */
					+ nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000330")/* @res "：" */
					+ e.getMessage() + "," + nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000331")/* @res "文件路径" */
					+ ":" + sFilePathDir);
		}
	}
//	public String Save(String key,String value){
//		
//		if("2".equals(key)){
//		return value;
//		}
//		return null;
//	}
	//end by yqq 2016-11-08
	
}
 
