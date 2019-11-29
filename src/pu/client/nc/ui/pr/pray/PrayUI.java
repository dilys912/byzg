package nc.ui.pr.pray;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang.StringUtils;

import nc.bs.framework.common.NCLocator;
import nc.bs.framework.exception.ComponentException;
import nc.bs.logging.Logger;
import nc.bs.pub.formulaparse.FormulaParse;
import nc.itf.pub.rino.IPubDMO;
import nc.itf.scm.cenpur.service.CentrPurchaseUtil;
import nc.itf.scm.cenpurchase.ICentralPurRule;
import nc.itf.scm.cenpurchase.IScmPosInv;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.itf.uap.sf.ICreateCorpQueryService;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.bd.ref.busi.InvmandocDefaultRefModel;
import nc.ui.ic.ic212.TestConn;
import nc.ui.ic.pub.lot.LotNumbRefPane;
import nc.ui.ml.NCLangRes;
import nc.ui.pf.query.ICheckRetVO;
import nc.ui.po.pub.InvAttrCellRenderer;
import nc.ui.po.pub.PoPublicUIClass;
import nc.ui.po.ref.OtherRefModel;
import nc.ui.pr.pub.PrTool;
import nc.ui.pr.pub.ProjectPhase;
import nc.ui.pu.pub.ATPForOneInvMulCorpUI;
import nc.ui.pu.pub.PuGetUIValueTool;
import nc.ui.pu.pub.PuTool;
import nc.ui.pu.pub.PurPsnRefModel;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIMenuItem;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITable;
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
import nc.ui.pub.bill.BillListData;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillMouseEnent;
import nc.ui.pub.bill.BillSortListener;
import nc.ui.pub.bill.BillTableMouseListener;
import nc.ui.pub.bill.IBillData;
import nc.ui.pub.bill.IBillModelSortPrepareListener;
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
import nc.ui.pub.print.PrintEntry;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.rc.pub.PurchasePrintDS;
import nc.ui.scm.ic.freeitem.FreeItemRefPane;
import nc.ui.scm.pu.ParaVOForBatch;
import nc.ui.scm.pub.BusiBillManageTool;
import nc.ui.scm.pub.CollectSettingDlg;
import nc.ui.scm.pub.bill.ButtonTree;
import nc.ui.scm.pub.bill.IBillExtendFun;
import nc.ui.scm.pub.cache.CacheTool;
import nc.ui.scm.pub.def.DefSetTool;
import nc.ui.scm.pub.print.ISetBillVO;
import nc.ui.scm.pub.print.ScmPrintTool;
import nc.ui.scm.pub.report.BillRowNo;
import nc.vo.bd.b06.PsndocVO;
import nc.vo.fts.pub.exception.FTSBusinessException;
import nc.vo.pr.pray.AdDayVO;
import nc.vo.pr.pray.PraybillHeaderVO;
import nc.vo.pr.pray.PraybillItemVO;
import nc.vo.pr.pray.PraybillVO;
import nc.vo.pr.pray.PriceInfosVO;
import nc.vo.pu.exception.RwtPoToPrException;
import nc.vo.pu.pr.PrayPubVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.ValidationException;
import nc.vo.pub.formulaset.VarryVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.query.QueryConditionVO;
import nc.vo.scm.cenpurchase.CentralResultVO;
import nc.vo.scm.cenpurchase.IsCentralVO;
import nc.vo.scm.constant.ScmConst;
import nc.vo.scm.datapower.BtnPowerVO;
import nc.vo.scm.ic.bill.FreeVO;
import nc.vo.scm.ic.bill.InvVO;
import nc.vo.scm.pu.BillStatus;
import nc.vo.scm.pu.BillTypeConst;
import nc.vo.scm.pu.PuBillLineOprType;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.service.ServcallVO;

/**
 * 功能描述:请购单维护界面
 */
public class PrayUI extends nc.ui.pub.ToftPanel implements BillEditListener,
		BillTableMouseListener, ListSelectionListener, BillBodyMenuListener,
		ICheckRetVO, BillEditListener2, IBillModelSortPrepareListener,
		ISetBillVO, IBillExtendFun, BillCardBeforeEditListener,
		IBillRelaSortListener2, ILinkMaintain,// 关联修改
		ILinkAdd,// 关联新增
		ILinkApprove,// 审批流
		ILinkQuery,// 逐级联查
		BillSortListener// 排序监听
{
	// 列表是否加载过
	private boolean m_bLoaded = false;
	// 按钮树实例,since v51
	private ButtonTree m_btnTree = null;
	/**
	 * 界面控制按钮定义
	 */
	private ButtonObject m_btnBusiTypes = null;// 业务类型
	private ButtonObject m_btnAdds = null;// 增加
	// 行操作
	private ButtonObject m_btnLines = null;
	private ButtonObject m_btnAddLine = null;
	private ButtonObject m_btnDelLine = null;
	private ButtonObject m_btnInsLine = null;
	private ButtonObject m_btnCpyLine = null;
	private ButtonObject m_btnPstLine = null;
	// 维护
	private ButtonObject m_btnMaintains = null;
	private ButtonObject m_btnModify = null;
	private ButtonObject m_btnSave = null;
	private ButtonObject m_btnCancel = null;
	private ButtonObject m_btnDiscard = null;
	private ButtonObject m_btnCopy = null;
	private ButtonObject m_btnSendAudit = null;
	// 执行/
	private ButtonObject m_btnActions = null;
	private ButtonObject m_btnApprove = null;
	private ButtonObject m_btnUnApprove = null;
	private ButtonObject m_btnClose = null;
	private ButtonObject m_btnOpen = null;
	// 浏览
	private ButtonObject m_btnBrowses = null;
	private ButtonObject m_btnQuery = null;
	// add by zip:2014/4/4 No 24
	private ButtonObject btnCKKCXX = new ButtonObject("参考库存信息", "参考库存信息","参考库存信息");;
	// add end
	private ButtonObject m_btnFirst = null;
	private ButtonObject m_btnPrev = null;
	private ButtonObject m_btnNext = null;
	private ButtonObject m_btnLast = null;
	private ButtonObject m_btnRefresh = null;
	private ButtonObject m_btnRefreshList = null;
	//
	private ButtonObject m_btnList = null;
	/* 卡片及消息中心共用 */
	private ButtonObject m_btnOthersFuncs = null;// "辅助功能"
	private ButtonObject m_btnDocument = null;// "文档管理"
	private ButtonObject m_btnOthersQry = null;// "辅助查询"
	private ButtonObject m_btnPriceInfo = null;// "价格论证表"
	private ButtonObject m_btnWorkFlowBrowse = null; // 状态查询
	private ButtonObject m_btnUsable = null;// "存量查询"
	public ButtonObject m_btnCombin = null;// "合并显示"
	private ButtonObject m_btnLinkBillsBrowse = null;// "联查"
	// 打印
	private ButtonObject m_btnPrints = null;// "打印"
	private ButtonObject m_btnPrint = null;// "打印"
	private ButtonObject m_btnPrintPreview = null;// "预览"
	private ButtonObject m_btnPrintList = null;// "列表打印"
	private ButtonObject m_btnPrintListPreview = null;// "预览"
	// 列表按钮定义
	private ButtonObject m_btnSelectAll = null;// "全选"
	private ButtonObject m_btnSelectNo = null;// "全消"
	private ButtonObject m_btnModifyList = null;// "列表修改"
	private ButtonObject m_btnDiscardList = null;// "列表作废"
	// 列表查询
	private ButtonObject m_btnQueryList = null;// "列表查询"
	private ButtonObject m_btnCard = null;// "切换"
	private ButtonObject m_btnQueryForAuditList = null;// "列表状态查询"
	private ButtonObject m_btnUsableList = null;// "列表存量查询"
	private ButtonObject m_btnDocumentList = null;// "列表文档管理"
	// 消息中心按钮组(审批,状态查询,文档管理)
	private ButtonObject m_btnAudit = null;// "审批"
	private ButtonObject m_btnUnAudit = null;// "弃审"
	private ButtonObject m_btnOthersAuditCenter = null;// "辅助"
	private ButtonObject[] m_btnsAuditCenter = null;// 消息中心按钮

	// 采购公司(记录编辑前值)---可能在并发较高的情况下会有问题
	private static String m_strPurCorpIdOld = null;

	// 记录是否查询过，查询过才启用“刷新”功能
	private boolean m_bQueried = false;

	class MyBillData implements IBillData {
		public void prepareBillData(nc.ui.pub.bill.BillData bd) {
			PrayUI.this.initBillBeforeLoad(bd);
		}
	}

	// 是否可以自动增行(从消息中心的请购单不能自动增行)
	private boolean isCanAutoAddLine = true;

	/** 复制时的特殊处理 */
	private boolean isFrmCopy = false;

	private ATPForOneInvMulCorpUI m_atpDlg = null;

	// 是否增加请购单(包括复制请购单)
	private boolean m_bAdd = false;

	// 取消按钮是否已经作用
	private boolean m_bCancel = false;

	private boolean m_bEdit = false;

	// 是否保留最初制单人
	private boolean isAllowedModifyByOther = false;

	// 单据及下拉框
	private BillCardPanel m_billPanel = null;

	// 是否按下浮动菜单
	private boolean m_bIsSubMenuPressed = false;

	// 当前操作的业务类型
	private ButtonObject m_bizButton = null;

	// 多语翻译工具类
	private static NCLangRes m_lanResTool = NCLangRes.getInstance();

	// 表体排序监听
	class IBillRelaSortListener2Body implements IBillRelaSortListener2 {
		public Object[] getRelaSortObjectArray() {
			return PrayUI.this.getRelaSortObjectArrayBody();
		}
	}

	/** 审批流用到单据ID */
	private String m_cauditid = null;

	private UIComboBox m_comPraySource = null;

	private UIComboBox m_comPraySource1 = null;

	private UIComboBox m_comPrayType = null;

	private UIComboBox m_comPrayType1 = null;

	// 查询模板
	private PrayUIQueryDlg m_condClient = null;

	// 换算率小数位
	// private nc.vo.pub.para.SysInitVO m_exchangeInitVO = null;
	// 自由项
	private FreeItemRefPane m_freeItem = null;

	/* 新增单据初始状态的行数 */
	// private int m_iInitRowCount = 20;
	/* 记录执行按钮组在卡片按钮组中的位置 */
	private final int m_iPOS_m_btnAction = 5;

	// 是否为价格论证表
	private BillListPanel m_listPanel = null;

	// 当前的请购单序号，为上下翻页服务
	private int m_nPresentRecord = 0;

	// 单据列表/卡片状态
	private int m_nUIState = 0;

	// 价格论证表查询对话框
	private QueryConditionClient m_priceDlg = null;

	private PrintEntry m_print = null;

	// 批量打印保存列表表头单据行号
	protected ArrayList listSelectBillsPos = null;

	// 请购部门ID缓存
	private String m_sDeptID = null;

	// 存货管理档案字段名
	private final String m_sInvMngIDItemKey = "cinventorycode";

	// 单位编码，系统应提供方法获取
	private String m_sLoginCorpId = getCorpPrimaryKey();

	// 缓存，存放请购单集合
	private PraybillVO[] m_VOs = null;

	private int nAssistUnitDecimal = 2;

	private int nExchangeDecimal = 2;

	/** 数据精度 */
	private int nMeasDecimal = 2;

	private int nMoneyDecimal = 2;

	private int nPriceDecimal = 2;

	/* 批量打印工具 */
	private ScmPrintTool printList = null; // 类变量

	private ScmPrintTool printCard = null;

	/* 当前登录操作员有权限的公司[] */
	private String[] saPkCorp = null;

	// 是否需要送审
	private PraybillVO m_SaveVOs = null;

	private boolean isAlreadySendToAudit = false;

	// 当前登录人和登陆日期
	private String m_sLoginDate = getClientEnvironment().getDate().toString();

	// 存放受托代销业务类型
	Hashtable m_hashBizType = new Hashtable();

	Hashtable isWorkFlow = new Hashtable();

	Hashtable m_hashInvbasIds = new Hashtable();

	// 存货默认参照
	UIRefPane invrefpane = null;

	// add by zip:2013/12/3
	// no 54
	@SuppressWarnings("rawtypes")
	public void setFixedData() throws Exception {
		IUAPQueryBS jdbc = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		String pk_corp = ClientEnvironment.getInstance().getCorporation().getPk_corp();
		String sql = "select pk_calbody,bodycode,bodyname from bd_calbody where pk_corp='"+pk_corp+"' and bodycode='01'";
		Object obj = jdbc.executeQuery(sql, new MapProcessor());
		// 需求库存组织
		getBillCardPanel().getBodyItem("pk_reqstoorg").setDefaultValue((String)((Map)obj).get("pk_calbody"));
		getBillCardPanel().getBodyItem("reqstoorgname").setDefaultValue((String)((Map)obj).get("bodyname"));
		// 需求仓库
		sql = "select pk_stordoc,storcode,storname from bd_stordoc where pk_corp='"+pk_corp+"' and pk_calbody='"+(String)((Map)obj).get("pk_calbody")+"' and storcode='B01'";
		obj = jdbc.executeQuery(sql, new MapProcessor());
		getBillCardPanel().getBodyItem("cwarehouseid").setDefaultValue((String)((Map)obj).get("pk_stordoc"));
		getBillCardPanel().getBodyItem("cwarehousename").setDefaultValue((String)((Map)obj).get("storname"));
	}
	// end
	
	/**
	 * PraybillClient 构造子注解。
	 */
	public PrayUI() {
		super();
		init();
		// add by zip: 2013/12/3
		// no 54
		try {
			setFixedData();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		// end
	}

	/**
	 * PrayUI 构造子注解。
	 */
	public PrayUI(String pk_corp, String billType, String businessType,
			String operator, String billID) {

		super();

		initi();

		PraybillVO vo = null;

		try {
			vo = PraybillHelper.queryPrayVoByHid(billID);
			if (vo != null) {
				// Logger.debug("查询到单据");
				m_VOs = new PraybillVO[] { vo };
				m_nPresentRecord = 0;
				setVoToBillCard(m_nPresentRecord, "");
				Logger.debug("成功显示单据");
			}
		} catch (Exception e) {
			SCMEnv.out(e);
		}
	}

	/**
	 * 功能描述:响应事件监听
	 * 
	 * @param event
	 *            nc.ui.pub.bill.BillEditEvent
	 */
	public void actionPerformed(java.awt.event.ActionEvent event) {
	}

	/**
	 * 功能描述:自定义项保存PK(表头)
	 */
	public static void afterEditWhenHeadDefPK(BillCardPanel bcp, BillEditEvent e) {
		DefSetTool.afterEditHead(bcp.getBillData(), e.getKey(), "pk_defdoc"
				+ e.getKey().substring("vdef".length(), e.getKey().length()));
	}

	/**
	 * 功能描述:自定义项保存PK(表体)
	 */
	public static void afterEditWhenBodyDefPK(BillCardPanel bcp, BillEditEvent e) {

		DefSetTool.afterEditBody(bcp.getBillModel(), e.getRow(), e.getKey(),
				"pk_defdoc"
						+ e.getKey().substring("vdef".length(),
								e.getKey().length()));
	}

	/**
	 * 功能描述:编辑后事件
	 * 
	 * @param e
	 *            nc.ui.pub.bill.BillEditEvent
	 */
	public void afterEdit(BillEditEvent e) {

		showHintMessage(m_lanResTool
				.getStrByID("common", "4004COMMON000000075")/* @res "正在编辑" */);

		if (m_nUIState == 0 && m_bEdit) {
			if (e.getPos() == BillItem.HEAD) {
				// 自定义项保存PK
				afterEditWhenHeadDefPK(getBillCardPanel(), e);
				//
				if (e.getKey().equals("cdeptid")) {
					afterEditWhenHeadDept(getBillCardPanel(), m_sDeptID,
							m_sLoginCorpId, e);
				} else if (e.getKey().equals("cpraypsn")) {
					afterEditWhenHeadPsn(getBillCardPanel(), m_sDeptID, e);
				} else if (e.getKey().equals("cprojectidhead")) {
					afterEditHeadCproject(getBillCardPanel(), e);
				}
			} else if (e.getPos() == BillItem.BODY) {
				// 自定义项保存PK
				afterEditWhenBodyDefPK(getBillCardPanel(), e);
				// 采购员
				if ("cemployeename".equals(e.getKey())) {
					afterEditWhenBodyEmployee(this, getBillCardPanel(),
							m_sLoginCorpId, e);
				}
				// 项目
				else if ("cprojectname".equals(e.getKey())) {
					afterEditWhenBodyProj(getBillCardPanel(), e);
				}
				// 自由项
				else if (e.getKey().trim().equals("vfree")) {
					afterEditWhenBodyFree(getBillCardPanel(), e);
				}
				// 存货编码
				else if (e.getKey().trim().equals("cinventorycode")) {
					afterEditWhenBodyInventory(this, getBillCardPanel(),
							m_sLoginCorpId, getClientEnvironment()
									.getCorporation().getUnitname(), e);
				}
				// 备注
				else if (e.getKey().trim().equals("vmemo")) {
					afterEditWhenBodyMemo(getBillCardPanel(), e);
				}
				// 行号
				else if (e.getKey().equals("crowno")) {
					afterEditWhenBodyRowNo(getBillCardPanel(), e, "crowno");
				}
				// 采购组织
				else if (e.getKey().equals("cpurorganizationname")) {
					afterEditWhenBodyPurOrg(this, getBillCardPanel(),
							m_sLoginCorpId, e);
				}
				// 采购公司
				else if (e.getKey().equals("pk_purcorp")) {
					afterEditWhenBodyPurCorp(getBillCardPanel(), e);
				}
				// 需求库存组织
				else if (e.getKey().equals("reqstoorgname")) {
					afterEditWhenBodyReqStoOrg(getBillCardPanel(), e);
				}
				// 需求仓库
				else if (e.getKey().equals("cwarehousename")) {
					afterEditWhenBodyReqWareHouse(getBillCardPanel(), e);
				}
				// 建议供应商
				else if (e.getKey().equals("cvendorname")) {
					afterEditWhenBodyVendor(getBillCardPanel(), e);
				}
				// 需求日期
				else if (e.getKey().trim().equals("ddemanddate")) {
					afterEditWhenBodyDemandDate(this, getBillCardPanel(), e);
				}
				// 主数量
				else if (e.getKey().trim().equals("npraynum")) {
					afterEditWhenBodyNum(this, getBillCardPanel(), e);
				}
				// 辅数量
				else if (e.getKey().trim().equals("nassistnum")) {
					afterEditWhenBodyAssNum(this, getBillCardPanel(), e);
				}
				// 换算率
				else if (e.getKey().trim().equals("nexchangerate")) {
					afterEditWhenBodyRate(this, getBillCardPanel(), e);
				}
				// 辅计量
				else if (e.getKey().trim().equals("cassistunitname")) {
					afterEditWhenBodyAssist(this, getBillCardPanel(), e);
				}
				// 建议单价
				else if (e.getKey().trim().equals("nsuggestprice")) {
					afterEditWhenBodySuggPrice(this, getBillCardPanel(), e);
				}
				// 金额
				else if (e.getKey().trim().equals("nmoney")) {
					afterEditWhenBodyMoney(this, getBillCardPanel(), e);
				}
			}
		}
		// 无表体行
		if (nc.ui.pu.pub.PuTool.isLastCom(getBillCardPanel(), e)
				&& getBillCardPanel().getBillModel().getRowCount() <= 0) {
			onAppendLine(getBillCardPanel(), this);
		}
		PuTool.setFocusOnLastCom(getBillCardPanel(), e);
		// 存货参照新增后再次选择出现带出原存货的问题
		UIRefPane refpane = (UIRefPane) getBillCardPanel().getBodyItem(
				"cinventorycode").getComponent();
		refpane.setPK(null);
		UIRefPane refpaneM = (UIRefPane) getBillCardPanel().getBodyItem(
				"cvendorname").getComponent();
		refpaneM.setPK(null);
	}

	/**
	 * 编辑后事件--表体需求仓库
	 * 
	 * @param bcp
	 * @param e
	 */
	public static void afterEditWhenBodyReqWareHouse(BillCardPanel bcp,
			BillEditEvent e) {
		String strPkCalBody = PuPubVO.getString_TrimZeroLenAsNull(bcp
				.getBodyValueAt(e.getRow(), "pk_reqstoorg"));
		String strPkWare = PuPubVO.getString_TrimZeroLenAsNull(bcp
				.getBodyValueAt(e.getRow(), "cwarehouseid"));
		if (strPkWare == null) {
			SCMEnv.out("修改需求仓库时，带出需求库存组织、需求公司默认值,仓库为空，直接返回。");
			return;
		}
		Object[] oaRet = null;
		try {
			// 设置需求库存组织
			if (strPkCalBody == null) {
				oaRet = (Object[]) CacheTool.getCellValue("bd_stordoc",
						"pk_stordoc", "pk_calbody", strPkWare);
				if (oaRet == null || oaRet.length == 0 || oaRet[0] == null
						|| oaRet[0].toString().trim().length() == 0) {
					SCMEnv
							.out("根据仓库档案ID[ID值：“" + strPkWare
									+ "”]不能获取所属库存组织ID!");
				} else {
					strPkCalBody = oaRet[0].toString().trim();
					// -----------------
					bcp
							.setBodyValueAt(strPkCalBody, e.getRow(),
									"pk_reqstoorg");
					oaRet = (Object[]) CacheTool.getCellValue("bd_calbody",
							"pk_calbody", "bodyname", strPkCalBody);
					if (oaRet == null || oaRet.length == 0 || oaRet[0] == null
							|| oaRet[0].toString().trim().length() == 0) {
						SCMEnv.out("根据所属库存组织ID[ID值：“" + strPkCalBody
								+ "”]不能获取所属库存组织名称!");
					} else {
						// --------------
						bcp.setBodyValueAt(oaRet[0], e.getRow(),
								"reqstoorgname");
					}
				}
			}
		} catch (BusinessException be) {
			SCMEnv.out(be.getMessage());
		}
	}

	/**
	 * 编辑后事件--表体需求库存组织
	 * 
	 * @param bcp
	 * @param e
	 */
	public static void afterEditWhenBodyReqStoOrg(BillCardPanel bcp,
			BillEditEvent e) {
		bcp.getBillModel().setValueAt(null, e.getRow(), "cwarehousename");
		bcp.getBillModel().setValueAt(null, e.getRow(), "cwarehouseid");
	}

	/**
	 * 编辑后事件--表体备注
	 * 
	 * @param bcp
	 * @param e
	 */
	public static void afterEditWhenBodyMemo(BillCardPanel bcp, BillEditEvent e) {
		UIRefPane nRefPanel = (UIRefPane) bcp.getBodyItem("vmemo")
				.getComponent();
		bcp.setBodyValueAt(nRefPanel.getText(), e.getRow(), "vmemo");
	}

	/**
	 * 编辑后事件--行号
	 * 
	 * @param bcp
	 * @param e
	 */
	public static void afterEditWhenBodyRowNo(BillCardPanel bcp,
			BillEditEvent e, String strRowNoKey) {
		BillRowNo.afterEditWhenRowNo(bcp, e, strRowNoKey);
	}

	/**
	 * 编辑后事件--采购公司
	 * 
	 * @param bcp
	 * @param e
	 */
	public static void afterEditWhenBodyPurCorp(BillCardPanel bcp,
			BillEditEvent e) {
		bcp.getBillModel().setValueAt(null, e.getRow(), "cvendormangid");
		bcp.getBillModel().setValueAt(null, e.getRow(), "cvendorbaseid");
	}

	/**
	 * 编辑后事件--换算率
	 * 
	 * @param ui
	 * @param bcp
	 * @param e
	 */
	public static void afterEditWhenBodyRate(ToftPanel ui, BillCardPanel bcp,
			BillEditEvent e) {
		UFDouble nPrayNum = PuPubVO.getUFDouble_NullAsZero(bcp.getBodyValueAt(e
				.getRow(), "npraynum"));
		UFDouble nSuggestPrice = PuPubVO.getUFDouble_NullAsZero(bcp
				.getBodyValueAt(e.getRow(), "nsuggestprice"));
		String sBaseID = (String) bcp.getBillModel().getValueAt(e.getRow(),
				"cbaseid");
		String sCassId = (String) bcp.getBillModel().getValueAt(e.getRow(),
				"cassistunit");
		UFDouble nExchangeRate = nc.ui.pu.pub.PuTool.getInvConvRateValue(
				sBaseID, sCassId);
		// 修改计量换算率,请购数量变化
		Object oTemp = bcp.getBodyValueAt(e.getRow(), "nexchangerate");
		if (oTemp != null && oTemp.toString().length() > 0)
			nExchangeRate = (UFDouble) oTemp;
		else
			nExchangeRate = null;
		if (nExchangeRate != null) {
			if (nExchangeRate.doubleValue() < 0) {
				MessageDialog.showErrorDlg(ui, m_lanResTool.getStrByID(
						"40040101", "UPP40040101-000040")
				/*
				 * @res "计算数据"
				 */, m_lanResTool.getStrByID("40040101", "UPP40040101-000045")
				/*
				 * @res "计量换算率不能为负！"
				 */);
				return;
			}
			UFDouble nAssistNum = PuPubVO.getUFDouble_NullAsZero(bcp
					.getBodyValueAt(e.getRow(), "nassistnum"));
			if (nPrayNum.doubleValue() != 0) {
				bcp.setBodyValueAt(nPrayNum.div(nExchangeRate), e.getRow(),
						"nassistnum");
			} else {
				bcp.setBodyValueAt(nAssistNum.multiply(nExchangeRate), e
						.getRow(), "npraynum");
			}
			// 请购数量变化,金额自动变化
			nPrayNum = PuPubVO.getUFDouble_NullAsZero(bcp.getBodyValueAt(e
					.getRow(), "npraynum"));
			if (nSuggestPrice != null) {
				final double d = nPrayNum.doubleValue()
						* nSuggestPrice.doubleValue();
				bcp.setBodyValueAt(new UFDouble(d), e.getRow(), "nmoney");
			}
			// 根据请购数量和采购提前期，自动修改需求日期和建议订货日期
			// 如果需求日期已经存在,则需求日期不修改.
			final int nDays = getAdvanceDays(ui, bcp, e);
			UFDate dpraydate = new UFDate(bcp.getHeadItem("dpraydate")
					.getValue());
			Object temp = bcp.getBodyValueAt(e.getRow(), "ddemanddate");
			UFDate ddemanddate = null;
			if (temp != null && temp.toString().trim().length() > 0) {
				ddemanddate = new UFDate(bcp.getBodyValueAt(e.getRow(),
						"ddemanddate").toString());
			}
			if (nDays >= 0) {
				UFDate d1 = dpraydate.getDateAfter(nDays);
				if (!AdDayVO.isDateOverflow(d1)) {
					UFDate d2 = null;
					if (ddemanddate != null) {
						d2 = ddemanddate.getDateAfter(-nDays);
						if (!AdDayVO.isDateOverflow(d2)) {
							bcp.setBodyValueAt(d2, e.getRow(), "dsuggestdate");
						}
					}
				}
			}
		} else {
			bcp.setBodyValueAt(null, e.getRow(), "nmoney");
			bcp.setBodyValueAt(null, e.getRow(), "ddemanddate");
			bcp.setBodyValueAt(null, e.getRow(), "dsuggestdate");
		}
	}

	/**
	 * 编辑后事件--需求日期
	 * 
	 * @param bcp
	 * @param e
	 */
	public static void afterEditWhenBodyDemandDate(ToftPanel ui,
			BillCardPanel bcp, BillEditEvent e) {
		// 修改需求日期,建议订货日期自动变化
		final int nDays = getAdvanceDays(ui, bcp, e);
		if (nDays >= 0) {
			UFDate dateDemand = PuPubVO.getUFDate(bcp.getBodyValueAt(
					e.getRow(), "ddemanddate"));
			if (dateDemand != null) {
				UFDate d2 = dateDemand.getDateAfter(-nDays);
				if (!AdDayVO.isDateOverflow(d2)) {
					bcp.setBodyValueAt(d2, e.getRow(), "dsuggestdate");
				}
			}
		}
	}

	/**
	 * 编辑后事件--辅数量
	 * 
	 * @param ui
	 * @param bcp
	 * @param e
	 */
	public static void afterEditWhenBodyAssNum(ToftPanel ui, BillCardPanel bcp,
			BillEditEvent e) {
		UFDouble nPrayNum = PuPubVO.getUFDouble_NullAsZero(bcp.getBodyValueAt(e
				.getRow(), "npraynum"));
		UFDouble nSuggestPrice = PuPubVO.getUFDouble_NullAsZero(bcp
				.getBodyValueAt(e.getRow(), "nsuggestprice"));
		String sBaseID = (String) bcp.getBillModel().getValueAt(e.getRow(),
				"cbaseid");
		String sCassId = (String) bcp.getBillModel().getValueAt(e.getRow(),
				"cassistunit");
		UFDouble nExchangeRate = nc.ui.pu.pub.PuTool.getInvConvRateValue(
				sBaseID, sCassId);
		// 修改辅数量,请购数量变化
		Object oTemp = bcp.getBodyValueAt(e.getRow(), "nassistnum");
		UFDouble nAssistNum = null;
		if (oTemp != null && oTemp.toString().length() > 0)
			nAssistNum = (UFDouble) oTemp;
		if (nAssistNum != null) {
			if (nAssistNum.doubleValue() < 0) {
				MessageDialog
						.showErrorDlg(ui, m_lanResTool.getStrByID("40040101",
								"UPP40040101-000040")
						/*
						 * @res "计算数据"
						 */, m_lanResTool.getStrByID("40040101",
								"UPP40040101-000044")/* @res "辅数量不能为负！" */);
				return;
			}
			// 可能是非固定换算率，所以不能用 m_nExchangeRate, 要从模板中取
			Object exc = bcp.getBillModel().getValueAt(e.getRow(),
					"nexchangerate");
			if (exc != null && !exc.toString().trim().equals(""))
				nExchangeRate = new UFDouble(exc.toString().trim());
			if (nExchangeRate != null) {
				final double d = nAssistNum.doubleValue()
						* nExchangeRate.doubleValue();
				bcp.setBodyValueAt(new UFDouble(d), e.getRow(), "npraynum");
			}
			// 请购数量变化,金额自动变化
			nPrayNum = (UFDouble) bcp.getBodyValueAt(e.getRow(), "npraynum");
			if (nSuggestPrice != null && nPrayNum != null) {
				final double d = nPrayNum.doubleValue()
						* nSuggestPrice.doubleValue();
				bcp.setBodyValueAt(new UFDouble(d), e.getRow(), "nmoney");
			}
			// 根据请购数量和采购提前期，自动修改需求日期和建议订货日期
			// 如果需求日期已经存在,则需求日期不修改.
			final int nDays = getAdvanceDays(ui, bcp, e);
			UFDate dpraydate = new UFDate(bcp.getHeadItem("dpraydate")
					.getValue());
			Object temp = bcp.getBodyValueAt(e.getRow(), "ddemanddate");
			UFDate ddemanddate = null;
			if (temp != null && temp.toString().trim().length() > 0) {
				ddemanddate = new UFDate(bcp.getBodyValueAt(e.getRow(),
						"ddemanddate").toString());
			}
			if (nDays >= 0) {
				UFDate d1 = dpraydate.getDateAfter(nDays);
				if (!AdDayVO.isDateOverflow(d1)) {
					UFDate d2 = null;
					if (ddemanddate != null) {
						d2 = ddemanddate.getDateAfter(-nDays);
					} else if (ddemanddate == null) {
						bcp.setBodyValueAt(d1, e.getRow(), "ddemanddate");
						d2 = d1.getDateAfter(-nDays);
					}
					if (!AdDayVO.isDateOverflow(d2)) {
						bcp.setBodyValueAt(d2, e.getRow(), "dsuggestdate");
					}
				}
			}
		} else {
			bcp.setBodyValueAt(null, e.getRow(), "nmoney");
			bcp.setBodyValueAt(null, e.getRow(), "ddemanddate");
			bcp.setBodyValueAt(null, e.getRow(), "dsuggestdate");
			bcp.setBodyValueAt(null, e.getRow(), "npraynum");
		}
	}

	/**
	 * 编辑后事件--金额
	 * 
	 * @param ui
	 * @param bcp
	 * @param e
	 */
	public static void afterEditWhenBodyMoney(ToftPanel ui, BillCardPanel bcp,
			BillEditEvent e) {
		UFDouble nPrayNum = PuPubVO.getUFDouble_NullAsZero(bcp.getBodyValueAt(e
				.getRow(), "npraynum"));
		UFDouble nSuggestPrice = PuPubVO.getUFDouble_NullAsZero(bcp
				.getBodyValueAt(e.getRow(), "nsuggestprice"));
		UFDouble nMoney = PuPubVO.getUFDouble_NullAsZero(bcp.getBodyValueAt(e
				.getRow(), "nmoney"));
		String sBaseID = (String) bcp.getBillModel().getValueAt(e.getRow(),
				"cbaseid");
		String sCassId = (String) bcp.getBillModel().getValueAt(e.getRow(),
				"cassistunit");
		UFDouble nExchangeRate = nc.ui.pu.pub.PuTool.getInvConvRateValue(
				sBaseID, sCassId);
		boolean bFixedFlag = PuTool.isFixedConvertRate(sBaseID, sCassId);
		// 金额变化，建议单价自动变化
		if (nMoney != null) {
			if (nMoney.doubleValue() < 0) {
				MessageDialog.showErrorDlg(ui, m_lanResTool.getStrByID(
						"40040101", "UPP40040101-000040")/* "计算数据" */,
						m_lanResTool.getStrByID("40040101",
								"UPP40040101-000043")/* "金额不能为负！" */);
				return;
			}
			if (nPrayNum != null && nPrayNum.doubleValue() != 0.0) {
				final double d = nMoney.doubleValue() / nPrayNum.doubleValue();
				bcp
						.setBodyValueAt(new UFDouble(d), e.getRow(),
								"nsuggestprice");
			} else if (nSuggestPrice != null
					&& nSuggestPrice.doubleValue() != 0.0) {
				final double d = nMoney.doubleValue()
						/ nSuggestPrice.doubleValue();
				bcp.setBodyValueAt(new UFDouble(d), e.getRow(), "npraynum");

				// 固定换算率,辅数量随请购数量变化;否则,换算率随请购数量变化
				if (bFixedFlag) {
					if (nExchangeRate != null
							&& nExchangeRate.doubleValue() != 0.0) {
						final double d0 = nPrayNum.doubleValue()
								/ nExchangeRate.doubleValue();
						bcp.setBodyValueAt(new UFDouble(d0), e.getRow(),
								"nassistnum");
					}
				} else {
					UFDouble nAssistNum = (UFDouble) bcp.getBodyValueAt(e
							.getRow(), "nassistnum");
					if (nAssistNum != null && nAssistNum.doubleValue() != 0.0) {
						final double d0 = nPrayNum.doubleValue()
								/ nAssistNum.doubleValue();
						bcp.setBodyValueAt(new UFDouble(d0), e.getRow(),
								"nexchangerate");
					} else {
						bcp.setBodyValueAt(null, e.getRow(), "nexchangerate");
					}
				}
			}
		} else {
			bcp.setBodyValueAt(null, e.getRow(), "npraynum");
			bcp.setBodyValueAt(null, e.getRow(), "nassistnum");
		}
	}

	/**
	 * 编辑后事件--建议单价
	 * 
	 * @param ui
	 * @param bcp
	 * @param e
	 */
	public static void afterEditWhenBodySuggPrice(ToftPanel ui,
			BillCardPanel bcp, BillEditEvent e) {
		UFDouble nPrayNum = PuPubVO.getUFDouble_NullAsZero(bcp.getBodyValueAt(e
				.getRow(), "npraynum"));
		UFDouble nSuggestPrice = PuPubVO.getUFDouble_NullAsZero(bcp
				.getBodyValueAt(e.getRow(), "nsuggestprice"));
		UFDouble nMoney = PuPubVO.getUFDouble_NullAsZero(bcp.getBodyValueAt(e
				.getRow(), "nmoney"));
		String sBaseID = (String) bcp.getBillModel().getValueAt(e.getRow(),
				"cbaseid");
		String sCassId = (String) bcp.getBillModel().getValueAt(e.getRow(),
				"cassistunit");
		UFDouble nExchangeRate = nc.ui.pu.pub.PuTool.getInvConvRateValue(
				sBaseID, sCassId);
		boolean bFixedFlag = PuTool.isFixedConvertRate(sBaseID, sCassId);
		// 建议单价变化，金额自动变化
		if (nSuggestPrice != null) {
			if (nSuggestPrice.doubleValue() < 0) {
				MessageDialog.showErrorDlg(ui, m_lanResTool.getStrByID(
						"40040101", "UPP40040101-000040")/* "计算数据" */,
						m_lanResTool.getStrByID("40040101",
								"UPP40040101-000042")/* "建议单价不能为负！" */);
				return;
			}
			if (nPrayNum != null) {
				final double d = nPrayNum.doubleValue()
						* nSuggestPrice.doubleValue();
				bcp.setBodyValueAt(new UFDouble(d), e.getRow(), "nmoney");
			} else if (nMoney != null && nSuggestPrice.doubleValue() != 0.0) {
				final double d = nMoney.doubleValue()
						/ nSuggestPrice.doubleValue();
				bcp.setBodyValueAt(new UFDouble(d), e.getRow(), "npraynum");
				// 固定换算率,辅数量随请购数量变化;否则,换算率随请购数量变化
				if (bFixedFlag) {
					if (nExchangeRate != null
							&& nExchangeRate.doubleValue() != 0.0) {
						final double d0 = nPrayNum.doubleValue()
								/ nExchangeRate.doubleValue();
						bcp.setBodyValueAt(new UFDouble(d0), e.getRow(),
								"nassistnum");
					}
				} else {
					UFDouble nAssistNum = (UFDouble) bcp.getBodyValueAt(e
							.getRow(), "nassistnum");
					if (nAssistNum != null && nAssistNum.doubleValue() != 0.0) {
						final double d0 = nPrayNum.doubleValue()
								/ nAssistNum.doubleValue();
						bcp.setBodyValueAt(new UFDouble(d0), e.getRow(),
								"nexchangerate");
					} else {
						bcp.setBodyValueAt(null, e.getRow(), "nexchangerate");
					}
				}
			}
		} else
			bcp.setBodyValueAt(null, e.getRow(), "nmoney");
	}

	/**
	 * 编辑后事件--辅计量
	 * 
	 * @param bcp
	 * @param e
	 */
	public static void afterEditWhenBodyAssist(ToftPanel uiPanel,
			BillCardPanel bcp, BillEditEvent e) {
		int iRow = e.getRow();
		// 存货基础档案ID
		String sBaseID = (String) bcp.getBillModel()
				.getValueAt(iRow, "cbaseid");
		// 辅计量主键
		String sCassId = (String) bcp.getBillModel().getValueAt(iRow,
				"cassistunit");
		//
		if (e.getValue() == null
				|| e.getValue().toString().trim().length() == 0) {
			bcp.getBillModel().setValueAt(null, iRow, "nassistnum");
			bcp.getBillModel().setValueAt(null, iRow, "nexchangerate");
			bcp.getBillModel().setValueAt(null, iRow, "cassistunitname");
			return;
		}
		// 获取换算率
		UFDouble nExchangeRate = PuTool.getInvConvRateValue(sBaseID, sCassId);
		// //////////////////
		UIRefPane ref = (UIRefPane) bcp.getBodyItem("cassistunitname")
				.getComponent();
		String pk_measdoc = ref.getRefPK();
		String name = ref.getRefName();
		bcp.getBillModel().setValueAt(pk_measdoc, iRow, "cassistunit");
		bcp.getBillModel().setValueAt(name, iRow, "cassistunitname");
		sCassId = (String) bcp.getBillModel().getValueAt(iRow, "cassistunit");
		nExchangeRate = PuTool.getInvConvRateValue(sBaseID, sCassId);
		bcp.getBillModel().setValueAt(nExchangeRate, iRow, "nexchangerate");

		// 换算率改变，重新计算
		BillEditEvent tempE = new BillEditEvent(bcp
				.getBodyItem("nexchangerate"), bcp.getBodyValueAt(iRow,
				"nexchangerate"), "nexchangerate", iRow);
		afterEditWhenBodyRate(uiPanel, bcp, tempE);

		// 此处放开所在可编辑，在编辑前作具体控制
		bcp.setCellEditable(iRow, "npraynum", true);
		bcp.setCellEditable(iRow, "nmoney", true);
		bcp.setCellEditable(iRow, "nexchangerate", true);
		bcp.setCellEditable(iRow, "nassistnum", true);
		bcp.setCellEditable(iRow, "cassistunitname", true);
	}

	/**
	 * 编辑后事件--数量
	 * 
	 * @param bcp
	 * @param e
	 */
	public static void afterEditWhenBodyNum(ToftPanel ui, BillCardPanel bcp,
			BillEditEvent e) {
		// 需要计算的表体行
		UFDouble nSuggestPrice = PuPubVO.getUFDouble_NullAsZero(bcp
				.getBodyValueAt(e.getRow(), "nsuggestprice"));
		UFDouble nPrayNum = PuPubVO.getUFDouble_NullAsZero(bcp.getBodyValueAt(e
				.getRow(), "npraynum"));
		// 存货基础档案ID
		String sBaseID = (String) bcp.getBillModel().getValueAt(e.getRow(),
				"cbaseid");
		// 辅计量主键
		String sCassId = (String) bcp.getBillModel().getValueAt(e.getRow(),
				"cassistunit");
		// 获取换算率
		UFDouble nExchangeRate = PuTool.getInvConvRateValue(sBaseID, sCassId);
		// 是否固定换算率
		boolean bFixedFlag = PuTool.isFixedConvertRate(sBaseID, sCassId);
		// 修改表体数量后的相应变化
		int iRow = e.getRow();
		if (PuTool.isAssUnitManaged(sBaseID)) {
			Object cassistunit = bcp.getBillModel().getValueAt(iRow,
					"cassistunit");
			if ((cassistunit == null)) {
				// MessageDialog.showWarningDlg(ui,
				// m_lanResTool.getStrByID("SCMCOMMON",
				// "UPPSCMCommon-000270")/*"提示"*/,
				// m_lanResTool.getStrByID("40040101", "UPP40040101-000481",
				// null, new String[] { (iRow + 1) + ""
				// })/*"{0}行：是辅计量管理，必需先输入辅单位及辅数量！"*/);
				// bcp.getBillModel().setValueAt(null, iRow, e.getKey());
				bcp.getBillModel().setValueAt(null, iRow, "nassistnum");
				calculateAdvDays(ui, bcp, e);
				return;
			}
		}
		// 请购数量变化，金额自动变化
		if (nPrayNum != null) {
		
			if (nPrayNum.doubleValue() < 0) {
				MessageDialog.showErrorDlg(ui, m_lanResTool.getStrByID(
						"40040101", "UPP40040101-000040")/* "计算数据" */,
						m_lanResTool.getStrByID("40040101",
								"UPP40040101-000041")/* "请购数量不能为负！" */);
				bcp.getBillModel().setValueAt(null, iRow, e.getKey());
				return;
			}
			if (nSuggestPrice != null) {
				bcp.setBodyValueAt(nPrayNum.multiply(nSuggestPrice),
						e.getRow(), "nmoney");
			}

			// 固定换算率,辅数量随请购数量变化;否则,换算率随请购数量变化
			if (bFixedFlag) {
				if (nExchangeRate != null && nExchangeRate.doubleValue() != 0.0) {
					bcp.setBodyValueAt(nPrayNum.div(nExchangeRate), e.getRow(),
							"nassistnum");
				}
			} else {
				// 非固定换算率
				UFDouble nAssistNum = null;
				Object oTemp = bcp.getBodyValueAt(e.getRow(), "nassistnum");
				if (oTemp != null && oTemp.toString().length() > 0)
					nAssistNum = (UFDouble) oTemp;
				if (nAssistNum != null) {
					if (nAssistNum.doubleValue() != 0.0) {
						bcp.setBodyValueAt(nPrayNum.div(nAssistNum),
								e.getRow(), "nexchangerate");
					} else {
						// 辅数量为0,如果修改换算率,会出现主数量/换算率!=0,而辅数量为0的矛盾
						// 为此,修改辅数量,不改变换算率
						if (nExchangeRate != null
								&& nExchangeRate.doubleValue() != 0.0) {
							bcp.setBodyValueAt(nPrayNum.div(nExchangeRate), e
									.getRow(), "nassistnum");
						}
					}
				} else {
					Object objTmp = bcp.getBodyValueAt(e.getRow(),
							"nexchangerate");
					if (objTmp != null && !objTmp.toString().trim().equals("")) {
						nExchangeRate = new UFDouble(objTmp.toString());
						bcp.setBodyValueAt(nPrayNum.div(nExchangeRate), e
								.getRow(), "nassistnum");
					} else {
						bcp.setBodyValueAt(null, e.getRow(), "nassistnum");
						bcp.setBodyValueAt(null, e.getRow(), "nexchangerate");
					}
				}
			}
			calculateAdvDays(ui, bcp, e);
		} else {
			bcp.setBodyValueAt(null, e.getRow(), "nmoney");
			bcp.setBodyValueAt(null, e.getRow(), "ddemanddate");
			bcp.setBodyValueAt(null, e.getRow(), "dsuggestdate");
			if (bFixedFlag)
				bcp.setBodyValueAt(null, e.getRow(), "nassistnum");
			else
				bcp.setBodyValueAt(null, e.getRow(), "nexchangerate");
		}
	}

	/**
	 * 根据请购数量和采购提前期，自动修改需求日期和建议订货日期
	 */
	private static void calculateAdvDays(ToftPanel ui, BillCardPanel bcp,
			BillEditEvent e) {
		// 
		// 如果需求日期已经存在,则需求日期不修改.
		final int nDays = getAdvanceDays(ui, bcp, e);
		UFDate dpraydate = new UFDate(bcp.getHeadItem("dpraydate").getValue());
		UFDate ddemanddate = null;
		Object temp = bcp.getBodyValueAt(e.getRow(), "ddemanddate");
		if (temp != null && temp.toString().trim().length() > 0) {
			ddemanddate = new UFDate(bcp.getBodyValueAt(e.getRow(),
					"ddemanddate").toString());
		}
		if (nDays >= 0) {
			UFDate d1 = dpraydate.getDateAfter(nDays);
			if (!AdDayVO.isDateOverflow(d1)) {
				UFDate d2 = null;
				if (ddemanddate != null) {

					d2 = ddemanddate.getDateAfter(-nDays);
				} else if (ddemanddate == null) {
					bcp.setBodyValueAt(d1, e.getRow(), "ddemanddate");
					d2 = d1.getDateAfter(-nDays);
				}
				if (!AdDayVO.isDateOverflow(d2)) {
					bcp.setBodyValueAt(d2, e.getRow(), "dsuggestdate");
				}

			}
		}
	}

	/**
	 * 编辑后事件-表体项目
	 * 
	 * @param bcp
	 * @param e
	 */
	public static void afterEditWhenBodyProj(BillCardPanel bcp, BillEditEvent e) {

		int n = e.getRow();
		Object oTemp = bcp.getBodyValueAt(n, "cprojectname");
		if (oTemp == null || oTemp.toString().length() == 0) {
			bcp.getBillModel().setCellEditable(n, "cprojectphasename", false);
			bcp.setBodyValueAt(null, n, "cprojectphasename");
			bcp.setBodyValueAt(null, n, "cprojectphaseid");
		} else {
			bcp.getBillModel().setCellEditable(
					n,
					"cprojectphasename",
					bcp.getBillModel().getItemByKey("cprojectphasename")
							.isEdit());
			oTemp = bcp.getBodyValueAt(n, "cprojectid");
			if (oTemp != null && oTemp.toString().length() > 0) {
				UIRefPane nRefPanel = (UIRefPane) bcp.getBodyItem(
						"cprojectphasename").getComponent();
				nRefPanel.setIsCustomDefined(true);
				nRefPanel.setRefModel(new ProjectPhase((String) oTemp));
				bcp.setBodyValueAt(null, n, "cprojectphasename");
				bcp.setBodyValueAt(null, n, "cprojectphaseid");
			}
		}

	}

	/**
	 * 编辑后事件--自由项
	 * 
	 * @param e
	 */
	public static void afterEditWhenBodyFree(BillCardPanel bcp, BillEditEvent e) {

		FreeVO freeVO = ((FreeItemRefPane) bcp.getBodyItem("vfree")
				.getComponent()).getFreeVO();
		String sVfree = (String) bcp.getBodyValueAt(e.getRow(), "vfree");
		if (sVfree == null || sVfree.trim().length() <= 0) {
			String str = null;
			for (int i = 0; i < 5; i++) {
				str = "vfree" + new Integer(i + 1).toString();
				bcp.setBodyValueAt(null, e.getRow(), str);
			}
		} else {
			String strName = null;
			String str = null;
			Object ob = null;
			for (int i = 0; i < 5; i++) {
				strName = "vfreename" + new Integer(i + 1).toString();

				if (freeVO.getAttributeValue(strName) != null) {
					str = "vfree" + new Integer(i + 1).toString();
					ob = freeVO.getAttributeValue(str);
					bcp.setBodyValueAt(ob, e.getRow(), str);
				}
			}
		}
		// 以免当前值被代入下一个自由项参照
		InvVO invVO = new InvVO();
		((FreeItemRefPane) bcp.getBodyItem("vfree").getComponent())
				.setFreeItemParam(invVO);
	}

	/**
	 * 表头编辑后事件--请购人
	 * 
	 * @param e
	 */
	public static void afterEditWhenHeadPsn(BillCardPanel bcp,
			String sDeptIdOld, BillEditEvent e) {
		String sPsnID = bcp.getHeadItem("cpraypsn").getValue();
		if (sPsnID != null && sPsnID.length() > 0) {
			// 获得部门ID及部门名称
			UIRefPane nRefPanel = (UIRefPane) bcp.getHeadItem("cpraypsn")
					.getComponent();
			sDeptIdOld = (String) nRefPanel.getRefValue("bd_psndoc.pk_deptdoc");
			nRefPanel = (UIRefPane) bcp.getHeadItem("cdeptid").getComponent();
			nRefPanel.setPK(sDeptIdOld);
		}
	}

	/**
	 * 表头编辑后事件
	 * 
	 * @param e
	 */
	public static void afterEditWhenHeadDept(BillCardPanel bcp,
			String sDeptIdOld, String sLoginCorpId, BillEditEvent e) {

		String sDeptID = bcp.getHeadItem("cdeptid").getValue();
		UIRefPane nRefPanel = (UIRefPane) bcp.getHeadItem("cpraypsn")
				.getComponent();
		String sWhere = " bd_psndoc.pk_deptdoc = '" + sDeptID + "'";

		if (sDeptID != null && sDeptID.length() > 0) {
			nRefPanel.setWhereString(sWhere);

			if (!sDeptID.equals(sDeptIdOld)) {
				// 如果请购人不属于请购部门,清除请购人
				UIRefPane nRefPanel0 = (UIRefPane) bcp.getHeadItem("cpraypsn")
						.getComponent();
				nRefPanel0.setValue(null);
				nRefPanel0.setPK(null);
			}
		} else {
			nRefPanel.setWhereString("bd_deptdoc.pk_corp = '" + sLoginCorpId
					+ "' and bd_deptdoc.pk_deptdoc = bd_psndoc.pk_deptdoc");
		}
	}

	/**
	 * 请购类型保存前检查
	 */
	private boolean checkPraytype(PraybillItemVO bodyVO[]) {
		// 表体有所有行满足“需求公司=请购单所属公司=采购公司”时，请购类型才能选择非｛采购，外包_不带料｝中的任意一个
		boolean b = true;
		if (m_comPrayType.getSelectedIndex() == 0
				|| m_comPrayType.getSelectedIndex() == 3) {
			for (int i = 0; i < bodyVO.length; i++) {
				if (!bodyVO[i].getPk_purcorp().equals(m_sLoginCorpId)) {
					b = false;
					break;
				}
				if (!bodyVO[i].getPk_reqcorp().equals(m_sLoginCorpId)) {
					b = false;
					break;
				}
			}
		}

		if (!b) {
			MessageDialog.showWarningDlg(this, m_lanResTool.getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000270")/* @res"提示" */,
					m_lanResTool.getStrByID("40040101", "UPP40040101-000528",
							null, null)/*
										 * @res"只有表体所有行满足:需求公司=请购单所属公司=采购公司时,才能生成委外订单！"
										 */);
		}
		return b;
	}

	/**
	 * 功能描述:项目编辑后事件处理表体项目
	 */
	public static void afterEditHeadCproject(BillCardPanel bcp, BillEditEvent e) {
		UIRefPane ref = (UIRefPane) bcp.getHeadItem("cprojectidhead")
				.getComponent();
		String sPkCalBody = ref.getRefPK();
		// 库存组织变后，重置所有行的计划价
		final int size = bcp.getRowCount();
		for (int i = 0; i < size; i++) {
			bcp.getBillModel().setValueAt(sPkCalBody, i, "cprojectid");
		}
	}

	// 建议供应商改变, 如果采购员不存在, 取供应商专管业务员
	public static void afterEditWhenBodyVendor(BillCardPanel bcp,
			BillEditEvent event) {
		try {
			int nCurRow = event.getRow();
			Object oTemp = bcp.getBodyValueAt(nCurRow, "cvendormangid");
			if (oTemp != null) {
				Object oTemp1 = bcp.getBodyValueAt(nCurRow, "cemployeename");
				if (oTemp1 == null) {
					oTemp1 = CacheTool.getCellValue("bd_cumandoc",
							"pk_cumandoc", "pk_resppsn1", oTemp.toString());
					if (oTemp1 != null) {
						Object o[] = (Object[]) oTemp1;
						if (o[0] != null) {
							bcp.setBodyValueAt(o[0], nCurRow, "cemployeeid");
							oTemp1 = CacheTool.getCellValue("bd_psndoc",
									"pk_psndoc", "psnname", o[0].toString());
							o = (Object[]) oTemp1;
							bcp.setBodyValueAt(o[0], nCurRow, "cemployeename");
						}
					}
				}
			}
		} catch (Exception e) {
			SCMEnv.out(e);
		}
	}

	/**
	 * 采购员 编辑后
	 */
	public static void afterEditWhenBodyEmployee(ToftPanel uiPanel,
			BillCardPanel bcp, String sLoginCorpId, BillEditEvent event) {

		String sPsnId = PuPubVO.getString_TrimZeroLenAsNull(bcp.getBodyValueAt(
				event.getRow(), "cemployeeid"));
		if (sPsnId == null) {
			return;
		}
		// 设置业务员默认采购组织
		String strPurId = PuTool.getPurIdByPsnId(sPsnId);
		if (strPurId != null) {
			bcp.setBodyValueAt(strPurId, event.getRow(), "cpurorganization");
			m_strPurCorpIdOld = PuPubVO.getString_TrimZeroLenAsNull(bcp
					.getBodyValueAt(event.getRow(), "pk_purcorp"));
			if (bcp.getBodyItem("cpurorganizationname").getEditFormulas() != null) {
				bcp.getBillModel().execFormulas(
						bcp.getBodyItem("cpurorganizationname")
								.getEditFormulas());
			} else {
				bcp
						.getBillModel()
						.execFormulas(
								new String[] {
										"cpurorganization->getColValue(bd_purorg,pk_purorg,pk_purorg,cpurorganization)",
										"cpurorganizationname->getColValue(bd_purorg,name,pk_purorg,cpurorganization)",
										"pk_purcorp->getColValue(bd_purorg,ownercorp,pk_purorg,cpurorganization)",
										"purcorpname->getColValue(bd_corp,unitname,pk_corp,pk_purcorp)" });
			}
			BillEditEvent eNew = new BillEditEvent(bcp
					.getBodyItem("cpurorganizationname"), strPurId,
					"cpurorganizationname", event.getRow(), BillItem.BODY);
			afterEditWhenBodyPurOrg(uiPanel, bcp, sLoginCorpId, eNew);
		}
	}

	/**
	 * 采购组织 编辑后
	 */
	public static void afterEditWhenBodyPurOrg(ToftPanel uiPanel,
			BillCardPanel bcp, String sLoginCorpId, BillEditEvent event) {
		int currow = event.getRow();

		BillModel bm = bcp.getBillModel();

		String strPurOrgId = PuPubVO.getString_TrimZeroLenAsNull(bm.getValueAt(
				currow, "cpurorganization"));
		// 如果采购组织被清空，则取登录公司为采购公司
		if (strPurOrgId == null) {
			bcp.setBodyValueAt(sLoginCorpId, currow, "pk_purcorp");
			bcp.setBodyValueAt(ClientEnvironment.getInstance().getCorporation()
					.getUnitname(), currow, "purcorpname");
		}
		String strPurCorpCurr = (String) bm.getValueAt(event.getRow(),
				"pk_purcorp");
		// 采购公司变化，清空建议供应商；采购员
		if (!strPurCorpCurr.equals(m_strPurCorpIdOld)) {
			bm.setValueAt(null, currow, "cvendormangid");
			bm.setValueAt(null, currow, "cvendorbaseid");
			bm.setValueAt(null, currow, "cvendorname");
			bm.setValueAt(null, currow, "cemployeeid");
			bm.setValueAt(null, currow, "cemployeename");
		}
		// 请购公司
		String strCurrCorpId = PuPubVO.getString_TrimZeroLenAsNull(bm
				.getValueAt(event.getRow(), "pk_corp"));
		if (strCurrCorpId == null) {
			strCurrCorpId = ClientEnvironment.getInstance().getCorporation()
					.getPrimaryKey();
		}
		// 询价
		setRulePrice(uiPanel, bcp, new String[] { (String) bm.getValueAt(event
				.getRow(), "pk_purcorp") }, new String[] { (String) bm
				.getValueAt(event.getRow(), "cbaseid") }, PuPubVO
				.getString_TrimZeroLenAsNull(bm.getValueAt(event.getRow(),
						"pk_reqstoorg")), strCurrCorpId, event.getRow(), event
				.getRow() + 1);
	}

	/**
	 * 存货编码：参照当前登录公司存货管理档案，非空，可编辑，默认显示 a) 编辑后， i. 存货被清空，清空栏目仍按照V31SP1处理 ii.
	 * 存货改变值，如果采购组织无值，带出采购组织默认值： 1.
	 * 存货是自采，取登录公司负责当前存货采购的采购员（根据采购员存货关系）所对应的采购组织（根据采购组织分配的采购员）,任意取一个 2.
	 * 存货是集采，取集采规则中定义的采购组织（需求公司＋存货基本ID＝＞采购组织） iii. 存货改变值，如果采购员无值，带出采购员默认值： 1.
	 * 存货如果是自采，则取采购公司负责当前存货采购的采购员（根据采购员存货关系） 2.
	 * 存货如果是集采，取集采规则中定义的采购组织（需求公司＋存货基本ID＝＞采购组织）分配的业务员，任意取一个 iv.
	 * 存货改变值，如果建议供应商无值，带出建议供应商默认值： 1.
	 * 如果存货基本档案分配到采购组织所属公司，取“存货基本ID+采购组织所属公司”对应的主供应商
	 * 
	 * !!!!!!! 注意：本方法存在效率问题，需要进一步效率优化
	 */
	public static void afterEditWhenBodyInventory(ToftPanel uiPanel,
			BillCardPanel bcp, String strReqCorpId, String strCorpName,
			BillEditEvent event) {

		BillModel bm = bcp.getBillModel();
		
		// 关闭合计开关
		boolean bOldNeedCalc = bm.isNeedCalculate();
		bm.setNeedCalculate(false);

		UIRefPane refpane = (UIRefPane) bcp.getBodyItem("cinventorycode")
				.getComponent();

		Object[] oaMangId = ((Object[]) refpane
				.getRefValues("bd_invmandoc.pk_invmandoc"));
		Object[] oaBaseId = ((Object[]) refpane
				.getRefValues("bd_invmandoc.pk_invbasdoc"));

		String pk_reqstoorg = (String) bm.getValueAt(event.getRow(),
				"pk_reqstoorg");
		String pk_reqcorp = (String) bm
				.getValueAt(event.getRow(), "pk_reqcorp");

		if (oaMangId == null || oaBaseId == null
				|| oaBaseId.length != oaMangId.length) {
			int selectedRow = event.getRow();

			bm.setValueAt(null, selectedRow, "cinventorycode");
			bm.setValueAt(null, selectedRow, "cinventoryname");
			bm.setValueAt(null, selectedRow, "cbaseid");
			bm.setValueAt(null, selectedRow, "cmangid");
			bm.setValueAt(null, selectedRow, "cinventoryspec");
			bm.setValueAt(null, selectedRow, "cinventorytype");
			bm.setValueAt(null, selectedRow, "cassistunitname");
			bm.setValueAt(null, selectedRow, "cinventoryunit");
			bm.setValueAt(null, selectedRow, "cproductname");

			bm.setValueAt(null, selectedRow, "nassistnum");
			bm.setValueAt(null, selectedRow, "nexchangerate");
			bm.setValueAt(null, selectedRow, "npraynum");
			bm.setValueAt(null, selectedRow, "nsuggestprice");
			bm.setValueAt(null, selectedRow, "nmoney");
			bm.setValueAt(null, selectedRow, "ddemanddate");
			bm.setValueAt(null, selectedRow, "dsuggestdate");
			bm.setValueAt(null, selectedRow, "vfree");
			bm.setValueAt(null, selectedRow, "vfree1");
			bm.setValueAt(null, selectedRow, "vfree2");
			bm.setValueAt(null, selectedRow, "vfree3");
			bm.setValueAt(null, selectedRow, "vfree4");
			bm.setValueAt(null, selectedRow, "vfree5");
			bm.setValueAt(null, selectedRow, "cvendormangid");
			bm.setValueAt(null, selectedRow, "cvendorbaseid");
			bm.setValueAt(null, selectedRow, "cvendorname");

			return;
		}
		int iLen = ((oaMangId == null) ? 0 : oaMangId.length);
		String[] saMangId = new String[iLen];
		String[] saBaseId = new String[iLen];
		for (int i = 0; i < iLen; i++) {
			saMangId[i] = oaMangId[i] + "";
			saBaseId[i] = oaBaseId[i] + "";
		}
		int nRow = bcp.getRowCount();
		PraybillVO VO = new PraybillVO(nRow);
		bcp.getBillValueVO(VO);

		// 选中的行已是最后一行则增行否则插行
		int iBeginRow = event.getRow();
		int iEndRow = iBeginRow + iLen;
		int selectedCount = iBeginRow;
		if (iBeginRow == nRow - 1) {
			for (int i = 0; i < iLen; i++) {
				bcp.addLine();
				/* 处理项目自动协带 */
				PuTool.setBodyProjectByHeadProject(bcp, "cprojectidhead",
						"cprojectid", "cprojectname", PuBillLineOprType.ADD);
				/* 处理行号 */
				BillRowNo.addLineRowNo(bcp, BillTypeConst.PO_PRAY, "crowno");
				// 增行操作，自动带入当前登录公司
				bcp.setBodyValueAt(strReqCorpId, bcp.getRowCount() - 1,
						"pk_reqcorp");
				bcp.setBodyValueAt(strCorpName, bcp.getRowCount() - 1,
						"reqcorpname");
				bcp.setBodyValueAt(strReqCorpId, bcp.getRowCount() - 1,
						"pk_purcorp");
				bcp.setBodyValueAt(strCorpName, bcp.getRowCount() - 1,
						"purcorpname");
			}
		} else {
			if (selectedCount < 0) {
				uiPanel.showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
						"UPPSCMCommon-000354")/* @res"插行前请先选择表体行！" */);
				return;
			}

			for (int i = iBeginRow + 1; i < iBeginRow + iLen; i++) {
				bcp.getBillModel().insertRow(i);
			}
			for (int i = iBeginRow; i < iBeginRow + iLen; i++) {
				// 增行操作，自动带入当前登录公司
				bcp.setBodyValueAt(strReqCorpId, i, "pk_reqcorp");
				bcp.setBodyValueAt(strCorpName, i, "reqcorpname");
				bcp.setBodyValueAt(strReqCorpId, i, "pk_purcorp");
				bcp.setBodyValueAt(strCorpName, i, "purcorpname");
			}
			int iFinalEndRow = iBeginRow + iLen;

			// 设置行号
			BillRowNo.insertLineRowNos(bcp, BillTypeConst.PO_PRAY, "crowno",
					iFinalEndRow, iLen - 1);

		}
		// 执行表体公式（批量）
		setInvEditFormulaInfo(bcp, refpane, iBeginRow, iEndRow, strReqCorpId,
				strCorpName);

		// 批量执行存货编辑后公式
		BillItem it = bcp.getBodyItem(event.getKey());
		if (it.getEditFormulas() != null && it.getEditFormulas().length > 0) {
			bcp.getBillModel().execFormulas(it.getEditFormulas(), iBeginRow,
					iEndRow);
		}
		// 设置自由项自定义参照
		setEnabled_BodyFree(bcp, refpane, iBeginRow, iEndRow);

		ArrayList listPurOrgVendor = null;
		/*
		 * <p>Arraylist(按存货基本档案ID为序) <p> |--采购组织 <p> |--采购公司 <p> |--采购员 <p>
		 * |--建议供应商及相关信息(ArrayList)
		 */
		try {
			// 获得默认采购组织和供应商数据
			listPurOrgVendor = PraybillHelper.queryPurOrgAndVendor(saMangId,
					saBaseId, pk_reqstoorg, strReqCorpId);
			// 获取根据规则计算的价格
			// uPrice = PraybillHelper.getRulePrice(saMangId, saBaseId,
			// pk_reqstoorg, strPk_corp);
		} catch (Exception e) {
			MessageDialog.showErrorDlg(uiPanel, m_lanResTool.getStrByID(
					"40040101", "UPP40040101-000442")/* "默认采购组织和供应商" */, e
					.getMessage());
			return;
		}
		ArrayList listValAll = null;
		ArrayList listValVendor = null;
		String[] saPurCorpId = new String[saMangId.length];
		for (int i = iBeginRow; i < iEndRow; i++) {
			// 改变存货时,清除自由项,辅数量,数量,单价,金额,需求日期,建议订货日期
			bm.setValueAt(null, i, "nassistnum");
			bm.setValueAt(null, i, "nexchangerate");
			bm.setValueAt(null, i, "npraynum");
			bm.setValueAt(null, i, "nsuggestprice");
			bm.setValueAt(null, i, "nmoney");
			bm.setValueAt(null, i, "ddemanddate");
			bm.setValueAt(null, i, "dsuggestdate");
			bm.setValueAt(null, i, "vfree");
			bm.setValueAt(null, i, "vfree1");
			bm.setValueAt(null, i, "vfree2");
			bm.setValueAt(null, i, "vfree3");
			bm.setValueAt(null, i, "vfree4");
			bm.setValueAt(null, i, "vfree5");
			// 公司编码
			bcp.setBodyValueAt(strReqCorpId, i, "pk_corp");
			// 处理项目自动协带
			PuTool.setBodyProjectByHeadPro(bcp, "cprojectidhead", "cprojectid",
					"cprojectname", i);
			// 设置采购组织和供应商
			if (listPurOrgVendor != null) {
				listValAll = (ArrayList) listPurOrgVendor.get(i - iBeginRow);
				if (listValAll == null || listValAll.size() != 4) {
					continue;
				}
				bm.setValueAt(listValAll.get(0), i, "cpurorganization");
				if (listValAll.get(1) != null) {
					bm.setValueAt(listValAll.get(1), i, "pk_purcorp");
				}

				bm.setValueAt(listValAll.get(2), i, "cemployeeid");
				listValVendor = (ArrayList) listValAll.get(3);
				if (listValVendor != null && listValVendor.size() >= 3) {
					bm.setValueAt(listValVendor.get(0), i, "cvendormangid");
					bm.setValueAt(listValVendor.get(1), i, "cvendorbaseid");
					bm.setValueAt(listValVendor.get(2), i, "cvendorname");
				} else {
					bm.setValueAt(null, i, "cvendormangid");
					bm.setValueAt(null, i, "cvendorbaseid");
					bm.setValueAt(null, i, "cvendorname");
				}
			}
			// 批次号清空
			bm.setValueAt(null, i, "vproducenum");

			// 设置需求公司
			bm.setValueAt(pk_reqcorp, i, "pk_reqcorp");
			// 设置需求库存组织
			bm.setValueAt(pk_reqstoorg, i, "pk_reqstoorg");
			//
			saPurCorpId[i - iBeginRow] = (String) bcp.getBodyValueAt(i,
					"pk_purcorp");
		}
		// 执行表体编辑后公式
		BillItem itemEmployee = bm.getItemByKey("cemployeename");
		if (itemEmployee.getEditFormulas() != null
				&& itemEmployee.getEditFormulas().length > 0) {
			bcp.getBillModel().execFormulas(itemEmployee.getEditFormulas(),
					iBeginRow, iEndRow);
		}
		BillItem itemPurCorp = bm.getItemByKey("purcorpname");
		if (itemPurCorp.getEditFormulas() != null
				&& itemPurCorp.getEditFormulas().length > 0) {
			bcp.getBillModel().execFormulas(itemPurCorp.getEditFormulas(),
					iBeginRow, iEndRow);
		}
		BillItem itemReqCorp = bm.getItemByKey("reqcorpname");
		if (itemReqCorp.getEditFormulas() != null
				&& itemReqCorp.getEditFormulas().length > 0) {
			bcp.getBillModel().execFormulas(itemReqCorp.getEditFormulas(),
					iBeginRow, iEndRow);
		}
		bcp
				.getBillModel()
				.execFormulas(
						new String[] { "cpurorganizationname->getColValue(bd_purorg,name,pk_purorg,cpurorganization)" },
						iBeginRow, iEndRow);
		//edit dan xionghui   
//////////////////////////////////////////////////////////////
//try {
//String startDate = "2013-01-01";
//String dateto = new UFDate(System.currentTimeMillis()).toString();
//IUAPQueryBS queryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
//int bodyRowCount = bm.getRowCount();
//String pk_corp = (String) bcp.getCorp();
//for (int i = 0; i < bodyRowCount; i++) {
//String invbasid =(String) bm.getValueAt(i, "cbaseid");
//
////已申购未审批数量
//String sql1 = new StringBuilder().append("select sum(A.npraynum) as rst from po_praybill_b A,po_praybill B").append(" where A.cpraybillid=B.cpraybillid and nvl(A.dr,0)=0 and nvl(B.dr,0)=0").append(" and B.pk_corp='" + pk_corp + "' and B.ibillstatus=0").append(" and B.dpraydate<='" + dateto + "' and B.dpraydate>='" + startDate + "'").append(" and A.cbaseid='" + invbasid + "'").toString();
////已申购已审批数量
//String sql2 = new StringBuilder().append("select sum(A.npraynum) as rst from po_praybill_b A,po_praybill B").append(" where A.cpraybillid=B.cpraybillid and nvl(A.dr,0)=0 and nvl(B.dr,0)=0").append(" and B.pk_corp='" + pk_corp + "' and B.ibillstatus=3").append(" and B.dpraydate<='" + dateto + "' and B.dpraydate>='" + startDate + "'").append(" and A.cbaseid='" + invbasid + "'").toString();
////采购订单数量
//String sql3 = new StringBuilder().append("select sum(A.nordernum) as rst from po_order_b A,po_order B").append(" where A.corderid=B.corderid").append(" and nvl(A.dr,0)=0 and nvl(B.dr,0)=0").append(" and B.pk_corp='" + pk_corp + "'").append(" and B.dorderdate<='" + dateto + "' and B.dorderdate>='" + startDate + "'").append(" and A.cbaseid='" + invbasid + "'").toString();
////到货单数量
//String sql4 = new StringBuilder().append("select sum(A.narrvnum) as rst from po_arriveorder_b A,po_arriveorder B").append(" where A.carriveorderid=B.carriveorderid").append(" and nvl(A.dr,0)=0 and nvl(B.dr,0)=0").append(" and B.pk_corp='" + pk_corp + "'").append(" and B.dreceivedate<='" + dateto + "' and B.dreceivedate>='" + startDate + "'").append(" and A.cbaseid='" + invbasid + "'").toString();
////检验合格数量
//String sql5 = new StringBuilder().append("select sum(A.nelignum) as rst from po_arriveorder_b A,po_arriveorder B").append(" where A.carriveorderid=B.carriveorderid").append(" and nvl(A.dr,0)=0 and nvl(B.dr,0)=0").append(" and B.pk_corp='" + pk_corp + "'").append(" and B.dreceivedate<='" + dateto + "' and B.dreceivedate>='" + startDate + "'").append(" and A.cbaseid='" + invbasid + "'").toString();
////已冻结数量
//String sql6 = new StringBuilder().append("select sum(nvl(nfreezenum,0)) as rst from ic_freeze where nvl(dr, 0) = 0 and pk_corp = '" + pk_corp + "' and cinvbasid = '" + invbasid + "' and cthawpersonid is null and cspaceid is not null").toString();
////现存量
//String sql7 = new StringBuilder().append("select sum(nonhandnum) from ic_onhandnum where nvl(dr,0)=0 and cinvbasid='"+invbasid+"' and pk_corp='"+pk_corp+"'").toString();
////再订购点
//String sql8 = new StringBuilder().append("select zdhd from bd_produce where nvl(dr,0)=0 and pk_invbasdoc='"+invbasid+"' and pk_corp='"+pk_corp+"'").toString();
////最高库存
//String sql9 = new StringBuilder().append("select maxstornum from bd_produce where nvl(dr,0)=0 and pk_invbasdoc='"+invbasid+"' and pk_corp='"+pk_corp+"'").toString();
//double d1, d2, d3, d4, d5, d6,d7,d8,d9;
//Object obj = queryBS.executeQuery(sql1, new ColumnProcessor());
//d1 = obj == null ? 0 : Double.parseDouble(obj.toString());
//obj = queryBS.executeQuery(sql2, new ColumnProcessor());
//d2 = obj == null ? 0 : Double.parseDouble(obj.toString());
//obj = queryBS.executeQuery(sql3, new ColumnProcessor());
//d3 = obj == null ? 0 : Double.parseDouble(obj.toString());
//obj = queryBS.executeQuery(sql4, new ColumnProcessor());
//d4 = obj == null ? 0 : Double.parseDouble(obj.toString());
//obj = queryBS.executeQuery(sql5, new ColumnProcessor());
//d5 = obj == null ? 0 : Double.parseDouble(obj.toString());
//obj = queryBS.executeQuery(sql6, new ColumnProcessor());
//d6 = obj == null ? 0 : Double.parseDouble(obj.toString());
//obj = queryBS.executeQuery(sql7, new ColumnProcessor());
//d7 = obj == null ? 0 : Double.parseDouble(obj.toString());
//obj = queryBS.executeQuery(sql8, new ColumnProcessor());
//d8 = obj == null ? 0 : Double.parseDouble(obj.toString());
//obj = queryBS.executeQuery(sql9, new ColumnProcessor());
//d9 = obj == null ? 0 : Double.parseDouble(obj.toString());
//bm.setValueAt(d1+d2, i, "pk_defdoc10"); // 已申购(含未审批)
//bm.setValueAt(d3 - d4, i, "pk_defdoc11"); // 已订购未到货数量
//bm.setValueAt(d7-d6, i, "pk_defdoc12"); // 现存可用数量(现存量-已冻结)
//bm.setValueAt(d8, i, "pk_defdoc13"); // 再订购数量
//bm.setValueAt(d9, i, "pk_defdoc14"); // 最高库存数
//}
//} catch (Exception e2) {
//e2.printStackTrace();
//}
//
///////////////////////////////////////////////////////		
		// 设置请购价
		setRulePrice(uiPanel, bcp, saPurCorpId, saBaseId, pk_reqstoorg,
				strReqCorpId, iBeginRow, iEndRow);
		// 是否辅计量管理批量加载
		PuTool.loadBatchAssistManaged(saBaseId);
		// 是否批次号管理批量加载
		PuTool.loadBatchProdNumMngt(saMangId);
		// 输入提示 czp 20050303 力帆
		PraybillVO voCurr = (PraybillVO) bcp.getBillValueVO(PraybillVO.class
				.getName(), PraybillHeaderVO.class.getName(),
				PraybillItemVO.class.getName());
		InvAttrCellRenderer ficr = new InvAttrCellRenderer();
		ficr.setFreeItemRenderer(bcp, voCurr);
		// 打开合计开关
		bm.setNeedCalculate(bOldNeedCalc);
		refpane.setPK(null);
		// 此处放开所在可编辑，在编辑前作具体控制

		for (int iRow = iBeginRow; iRow < iEndRow; iRow++) {
			bcp.setCellEditable(iRow, "npraynum", true);
			bcp.setCellEditable(iRow, "nmoney", true);
			bcp.setCellEditable(iRow, "nexchangerate", true);
			bcp.setCellEditable(iRow, "nassistnum", true);
			bcp.setCellEditable(iRow, "cassistunitname", true);
		}

		// 设置辅计量及换算率相关信息
		setRelated_AssistUnit(bcp, uiPanel, iBeginRow, iEndRow);
	}

	/**
	 * 作者：王印芬 功能：存货、订单日期、币种修改后相应的最高限价变化 该函数由afterEdit存货、订单日期、币种改变后调用。 参数： int
	 * iBeginRow 需计算合同相关信息的表体开始行 int iEndRow 需计算合同相关信息的表体结束行 返回：无 例外：无
	 * 日期：(2002-3-13 11:39:21) 修改日期，修改人，修改原因，注释标志： 2003-11-14 wyf 批量加载
	 */
	private static void setRelated_AssistUnit(BillCardPanel bcp,
			ToftPanel uiPanel, int iBeginRow, int iEndRow) {
		BillModel bm = bcp.getBillModel();
		// 批量加载
		String[] saBaseId = (String[]) PuGetUIValueTool.getArrayNotNull(bm,
				"cbaseid", String.class, iBeginRow, iEndRow);
		PuTool.loadBatchAssistManaged(saBaseId);

		// 辅计量的行
		Vector vecAssistUnitIndex = new Vector();
		Vector vecBaseId = new Vector();
		Vector vecAssistId = new Vector();

		// 计算值

		// 设置默认辅计量
		String[] aryAssistunit = new String[] {
				"<formulaset><cachetype>FOREDBCACHE</cachetype></formulaset>cassistunit->getColValue(bd_invbasdoc,pk_measdoc2,pk_invbasdoc,cbaseid)",
				"cassistunitname->getColValue(bd_measdoc,measname,pk_measdoc,cassistunit)" };
		bm.execFormulas(aryAssistunit, iBeginRow, iEndRow);
		//
		String sBaseId = null;
		String sAssistUnit = null;
		for (int iRow = iBeginRow; iRow < iEndRow; iRow++) {
			sBaseId = (String) bcp.getBodyValueAt(iRow, "cbaseid");
			if (PuTool.isAssUnitManaged(sBaseId)) {
				sAssistUnit = (String) bcp.getBodyValueAt(iRow, "cassistunit");
				// 为批量加载作准备
				if (PuPubVO.getString_TrimZeroLenAsNull(sAssistUnit) != null) {
					vecAssistUnitIndex.add(new Integer(iRow));
					vecBaseId.add(sBaseId);
					vecAssistId.add(sAssistUnit);
				}
			} else {
				bm.setValueAt(null, iRow, "cassistunitname");
				bm.setValueAt(null, iRow, "cassistunit");
				bm.setValueAt(null, iRow, "nassistnum");
				bm.setValueAt(null, iRow, "nexchangerate");
			}
		}

		// 批量设置辅计量的行
		int iAssistUnitLen = vecAssistUnitIndex.size();
		if (iAssistUnitLen > 0) {

			// 批量加载
			PuTool.loadBatchInvConvRateInfo((String[]) vecBaseId
					.toArray(new String[iAssistUnitLen]),
					(String[]) vecAssistId.toArray(new String[iAssistUnitLen]));

			// String[] saCurrId =
			// getAllCurrIdFromCard(getRowCount(),getCorp(),getHeadItem("ccurrencytypeid").getValue(),bm);
			// HashMap mapRateMny =
			// m_cardPoPubSetUI2.getMoneyDigitByCurr_Busi(saCurrId);
			// BusinessCurrencyRateUtil bca =
			// m_cardPoPubSetUI2.getCurrArith_Busi(getCorp());

			// 循环执行
			int iRow = 0;
			for (int i = 0; i < iAssistUnitLen; i++) {
				iRow = ((Integer) vecAssistUnitIndex.get(i)).intValue();

				Object[] oConvRate = PuTool.getInvConvRateInfo(
						(String) vecBaseId.get(i), (String) vecAssistId.get(i));
				if (oConvRate == null) {
					bm.setValueAt(null, iRow, "nexchangerate");
				} else {
					bm.setValueAt((UFDouble) oConvRate[0], iRow,
							"nexchangerate");
				}

				// 换算率改变，重新计算
				BillEditEvent tempE = new BillEditEvent(bcp
						.getBodyItem("nexchangerate"), bcp.getBodyValueAt(iRow,
						"nexchangerate"), "nexchangerate", iRow);
				afterEditWhenBodyRate(uiPanel, bcp, tempE);
			}
		}

		// 设置可编辑性
		// setEnabled_BodyAssistUnitRelated(iBeginRow,iEndRow) ;

	}

	/**
	 * 按参数“PO29”设置建议单价
	 * 
	 * @param uiPanel
	 * @param bcp
	 * @param saMangId
	 * @param saBaseId
	 * @param pk_reqstoorg
	 * @param strPk_corp
	 * @param iBeginRow
	 * @param iEndRow
	 * @since V50
	 */
	private static void setRulePrice(ToftPanel uiPanel, BillCardPanel bcp,
			String[] saPurCorpId, String[] saBaseId, String pk_reqstoorg,
			String strPk_corp, int iBeginRow, int iEndRow) {

		// 获取根据规则计算的价格
		UFDouble[] uaPrice = null;
		int iLen = iEndRow - iBeginRow;
		try {
			uaPrice = PraybillHelper.getRulePrice(saPurCorpId, saBaseId,
					pk_reqstoorg, strPk_corp);
		} catch (Exception e) {
			MessageDialog.showErrorDlg(uiPanel, m_lanResTool.getStrByID(
					"40040101", "UPP40040101-000442")/* "默认采购组织和供应商" */, e
					.getMessage());
			return;
		}
		if (uaPrice == null || uaPrice.length != iLen) {
			return;
		}
		BillEditEvent e = null;
		for (int i = iBeginRow; i < iEndRow; i++) {
			// 原来有值，则用原来的值
			if (uaPrice[i - iBeginRow] == null) {
				continue;
			}
			bcp.getBillModel().setValueAt(uaPrice[i - iBeginRow], i,
					"nsuggestprice");
			e = new BillEditEvent(bcp.getBodyItem("nsuggestprice"), uaPrice[i
					- iBeginRow], "nsuggestprice", i);
			afterEditWhenBodySuggPrice(uiPanel, bcp, e);
		}
	}

	/**
	 * 编辑前处理:自由项 创建日期：(2001-3-23 2:02:27)
	 * 
	 * @param e
	 *            ufbill.BillEditEvent
	 */
	public boolean beforeEdit(BillEditEvent e) {

		showHintMessage(m_lanResTool
				.getStrByID("common", "4004COMMON000000075")/* @res "正在编辑" */);

		// 如果用户模板定义此项目不可编辑，则直接返回false
		switch (e.getPos()) {
		case 0:
			if (!getBillCardPanel().getHeadItem(e.getKey()).isEdit()) {
				return false;
			}
			break;
		case 1:
			if (!getBillCardPanel().getBodyItem(e.getKey()).isEdit()) {
				return false;
			}
			break;
		case 2:
			if (!getBillCardPanel().getTailItem(e.getKey()).isEdit()) {
				return false;
			}
			break;
		default:
			return false;
		}

		// 用户模板定义可修改情况下的处理
		if (e.getKey().equals("vfree")) {
			return PuTool.beforeEditInvBillBodyFree(getBillCardPanel(), e,
					new String[] { "cmangid", "cinventorycode",
							"cinventoryname", "cinventoryspec",
							"cinventorytype" }, new String[] { "vfree",
							"vfree1", "vfree2", "vfree3", "vfree4", "vfree5" });
		}
		// 采购组织
		else if (e.getKey().equals("cpurorganizationname")) {
			return beforeEditWhenBodyPurOrg(this, getBillCardPanel(),
					m_sLoginCorpId, e);
		}
		// 存货编码
		else if (e.getKey().equals("cinventorycode")) {
			beforeEditWhenBodyInventory(getBillCardPanel(), m_hashBizType,
					m_hashInvbasIds, m_sLoginCorpId, PoPublicUIClass
							.getLoginDate()
							+ "", e);
		}
		// 表体产品
		else if (e.getKey().equals("cproductname")) {
			beforeEditWhenBodyProduct(getBillCardPanel(), m_sLoginCorpId,
					m_sLoginDate, e);
		}
		// 需求仓库
		else if (e.getKey().equals("cwarehousename")) {
			beforeEditWhenBodyReqWare(getBillCardPanel(), e);
		}
		// 需求库存组织
		else if (e.getKey().equals("reqstoorgname")) {
			beforeEditWhenBodyReqStore(getBillCardPanel(), e);
		}
		// 建议供应商
		else if (e.getKey().equals("cvendorname")) {
			return beforeEditWhenBodyVendor(getBillCardPanel(), e);
		}
		// 采购员
		else if (e.getKey().equals("cemployeename")) {
			beforeEditWhenBodyEmployer(getBillCardPanel(), e);
		}
		// 项目
		else if (e.getKey().equals("cprojectname")) {
			beforeEditWhenBodyProject(getBillCardPanel(), e);
		}
		// 项目阶段
		else if (e.getKey().equals("cprojectphasename")) {
			beforeEditWhenBodyProjectPhase(getBillCardPanel(), e);
		}
		// 批次号
		else if (e.getKey().equals("vproducenum")) {
			return beforeEditWhenBodyProduceNum(this, getBillCardPanel(),
					m_sLoginCorpId, e);
		}
		// 需求公司
		else if (e.getKey().equals("reqcorpname")) {
			beforeEditWhenBodyReqcorp(getBillCardPanel(), e);
		}
		// 采购公司
		else if (e.getKey().equals("purcorpname")) {
			beforeEditWhenBodyPurcorp(getBillCardPanel(), e);
		}
		// 数量、金额、换算率、辅数量、辅计量
		else if ("npraynum".equals(e.getKey()) || "nmoney".equals(e.getKey())
				|| "nexchangerate".equals(e.getKey())
				|| "nassistnum".equals(e.getKey())
				|| "cassistunitname".equals(e.getKey())) {
			beforeEditBodyAssistUnitNumber(getBillCardPanel(), e.getRow());
		}
		return true;
	}

	/**
	 * <p>
	 * 采购组织：编辑可编辑性及参照内容设置
	 * <p>
	 * 1)、业务类型是公司业务类型：采购组织参照内容取采购组织所属公司为请购单所属公司的采购组织档案，可编辑
	 * <p>
	 * 2)、业务类型是集团业务类型：
	 * <p>
	 * i.需求公司未录入，参照内容取全集团所有采购组织档案，可编辑
	 * <p>
	 * ii.需求公司=请购单所属公司，存货未录入，不可编辑
	 * <p>
	 * iii.需求公司=请购单所属公司，自采购存货参照内容取全集团所有采购组织档案，可编辑；集采不可编辑(XY\WYF\CZP:
	 * 暂不区分公司自制基本档案存货、集团基本档案存货)
	 * <p>
	 * iv.需求公司≠请购单所属公司、无论存货是否录入，采购组织参照内容为｛请购单所属公司、需求公司｝，可编辑
	 * 
	 * 
	 * @详细需求描述： <p>
	 *          存货如果是自采，则取本公司负责当前存货采购的采购员（根据采购员存货关系）所对应的采购组织（根据采购组织分配的采购员），
	 *          可以参照选择采购组织档案修改；
	 *          <p>
	 *          
	 *          存货如果是集采，取集采规则中定义的采购组织，当前公司如果是采购组织的所属公司，则可以参照选择“所属公司”为当前公司和需求公司的采购组织修改
	 *          ，否则不能修改。
	 */
	public static boolean beforeEditWhenBodyPurOrg(ToftPanel uiPanel,
			BillCardPanel bcp, String strPk_corp, BillEditEvent e) {
		int currow = e.getRow();
		String pk_reqcorp = null;
		UIRefPane refpane = null;

		m_strPurCorpIdOld = PuPubVO.getString_TrimZeroLenAsNull(bcp
				.getBodyValueAt(e.getRow(), "pk_purcorp"));

		/* 公司业务类型 */
		String strBiztype = bcp.getHeadItem("cbiztype").getValue();
		try {
			if (!CentrPurchaseUtil.isGroupBusiType(strBiztype)) {
				refpane = (UIRefPane) bcp.getBillData().getBodyItem(
						"cpurorganizationname").getComponent();
				AbstractRefModel refModel = refpane.getRefModel();
				refModel.addWherePart(" and ownercorp = '" + strPk_corp + "' ");
				return true;
			}
		} catch (BusinessException ee) {
			MessageDialog
					.showHintDlg(uiPanel, ee.getMessage(), ee.getMessage());
		}

		/* 集团业务类型 */

		// 需求公司为空
		pk_reqcorp = PuPubVO.getString_TrimZeroLenAsNull(bcp.getBillModel()
				.getValueAt(currow, "pk_reqcorp"));
		if (pk_reqcorp == null) {
			Logger.debug("pk_reqcorp = null, 采购公司任意取值");/* -=notranslate=- */
			refpane = (UIRefPane) bcp.getBillData().getBodyItem(
					"cpurorganizationname").getComponent();
			AbstractRefModel refModel = refpane.getRefModel();
			refModel.addWherePart(" and 1>0 ");
			return true;
		}
		// 需求公司≠请购单所属公司
		if (!pk_reqcorp.equals(strPk_corp)) {
			// 请购单所属公司≠需求公司
			// 无论存货是否录入，采购组织参照内容为｛请购单所属公司、需求公司｝可编辑
			refpane = (UIRefPane) bcp.getBillData().getBodyItem(
					"cpurorganizationname").getComponent();
			AbstractRefModel refModel = refpane.getRefModel();
			refModel.addWherePart(" and bd_purorg.ownercorp in ('" + strPk_corp
					+ "','" + pk_reqcorp + "') ");

			return true;
		}
		// 需求公司=请购单所属公司
		if (pk_reqcorp.equals(strPk_corp)) {
			// 存货未录入
			if (PuPubVO.getString_TrimZeroLenAsNull(bcp.getBillModel()
					.getValueAt(currow, "cmangid")) == null) {
				// 存货未录入、不可编辑
				SCMEnv.out("存货未录入、不可编辑");/* -=notranslate=- */
				return false;
			}
			// 存货录入
			String beanName = ICentralPurRule.class.getName();
			ICentralPurRule bo = (ICentralPurRule) nc.bs.framework.common.NCLocator
					.getInstance().lookup(beanName);
			IsCentralVO para = new IsCentralVO();
			CentralResultVO[] result = null;

			para.setPk_corp(strPk_corp);
			para.setCrowid(new Integer(currow).toString());
			para.setPk_invbasdoc(PuPubVO.getString_TrimZeroLenAsNull(bcp
					.getBillModel().getValueAt(currow, "cbaseid")));
			try {
				result = bo.isCentralPur(new IsCentralVO[] { para });
			} catch (Exception e1) {
				uiPanel.showHintMessage(e1.getMessage());
				return false;
			}

			if (result != null && result.length > 0 && result[0] != null
					&& result[0].getCrowid() != null
					&& result[0].getCrowid().equals(para.getCrowid())) {
				if (result[0].getIsCentralPur()) {
					// 集采，采购组织默认取集采规则中定义的采购组织，请购单所属公司与采购公司相同，则可编辑，否则不可编辑
					refpane = (UIRefPane) bcp.getBillData().getBodyItem(
							"cpurorganizationname").getComponent();
					refpane.setPK(result[0].getPk_purorg());
					AbstractRefModel refModel = refpane.getRefModel();
					refModel.addWherePart(" and bd_purorg.ownercorp ='"
							+ strPk_corp + "' ");

					String strPurCorp = bcp.getBodyValueAt(e.getRow(),
							"pk_purcorp")
							+ "";
					return strPk_corp.equals(strPurCorp);
				}
				// 存货是自采
				refpane = (UIRefPane) bcp.getBillData().getBodyItem(
						"cpurorganizationname").getComponent();
				AbstractRefModel refModel = refpane.getRefModel();
				refModel.addWherePart(" and 1>0 ");
				return true;
			}
		}
		return true;
	}

	/**
	 * 需求仓库：可空，可编辑，默认不显示 a) 编辑前检查：如果需求公司为空或上层来源单据ID非空，则不可编辑 b)
	 * 编辑前参照设置：参照需求库存组织下的仓库档案
	 */
	public static void beforeEditWhenBodyReqWare(BillCardPanel bcp,
			BillEditEvent e) {
		String strReqCorp = PuPubVO.getString_TrimZeroLenAsNull(bcp
				.getBodyValueAt(e.getRow(), "pk_reqcorp"));
		if (strReqCorp == null) {
			Logger.debug("需求公司未录入,表体“需求仓库”项目不可编辑");
			bcp.getBillData().getBillModel().setCellEditable(e.getRow(),
					"reqstoorgname", false);
			return;
		}
		String strUpSrcBillId = PuPubVO.getString_TrimZeroLenAsNull(bcp
				.getBodyValueAt(e.getRow(), "cupsourcebillid"));
		if (strUpSrcBillId != null) {
			Logger.debug("有来源的单据行,表体“需求仓库”项目不可编辑");
			bcp.getBillData().getBillModel().setCellEditable(e.getRow(),
					"reqstoorgname", false);
			return;
		}
		bcp.getBillData().getBillModel().setCellEditable(
				e.getRow(),
				"reqstoorgname",
				bcp.getBillData().getBillModel().getItemByKey("reqstoorgname")
						.isEdit());
		String strReqStoOrg = PuPubVO.getString_TrimZeroLenAsNull(bcp
				.getBodyValueAt(e.getRow(), "pk_reqstoorg"));
		PuTool.restrictWarehouseRefByStoreOrg(bcp, strReqCorp, strReqStoOrg,
				"cwarehousename");
	}

	/*
	 * 需求公司，不可编辑
	 */
	public static void beforeEditWhenBodyReqcorp(BillCardPanel bcp,
			BillEditEvent e) {
		bcp.getBillData().getBillModel().setCellEditable(e.getRow(),
				"reqcorpname", false);
	}

	/*
	 * 采购公司，不可编辑
	 */
	public static void beforeEditWhenBodyPurcorp(BillCardPanel bcp,
			BillEditEvent e) {
		bcp.getBillData().getBillModel().setCellEditable(e.getRow(),
				"purcorpname", false);
	}

	/**
	 * 需求库存组织：参照需求公司下的库存组织档案，可空,可编辑，默认不显示 a) 编辑前检查：如果上层来源单据ID非空，不可编辑 b)
	 * 编辑前参照设置：参照需求公司库存组织
	 */
	public static void beforeEditWhenBodyReqStore(BillCardPanel bcp,
			BillEditEvent e) {
		// 编辑前检查：如果上层来源单据ID非空，不可编辑
		String strUpSrcBillId = PuPubVO.getString_TrimZeroLenAsNull(bcp
				.getBodyValueAt(e.getRow(), "cupsourcebillid"));
		if (strUpSrcBillId != null) {
			Logger.debug("有来源的单据行,表体“需求库存组织”项目不可编辑");
			bcp.getBillData().getBillModel().setCellEditable(e.getRow(),
					"reqstoorgname", false);
			return;
		} else {
			bcp.getBillData().getBillModel().setCellEditable(
					e.getRow(),
					"reqstoorgname",
					bcp.getBillData().getBillModel().getItemByKey(
							"reqstoorgname").isEdit());
		}

		// 编辑前参照设置：参照需求公司库存组织
		UIRefPane paneReqStoOrg = ((UIRefPane) bcp.getBodyItem("reqstoorgname")
				.getComponent());
		paneReqStoOrg.getRefModel().setSealedDataShow(false);
		Object oTemp = bcp.getBodyValueAt(e.getRow(), "pk_reqcorp");
		if (oTemp != null)
			paneReqStoOrg.getRefModel().addWherePart(
					" and bd_calbody.pk_corp = '" + oTemp.toString() + "' ");
	}

	/**
	 * 建议供应商 i. 如果采购组织未录入，不可编辑 ii. 参照内容取采购组织的“所属公司”下的客商档案
	 */
	public static boolean beforeEditWhenBodyVendor(BillCardPanel bcp,
			BillEditEvent e) {
		String strUpSrcBillId = PuPubVO.getString_TrimZeroLenAsNull(bcp
				.getBodyValueAt(e.getRow(), "pk_purcorp"));
		if (strUpSrcBillId == null) {
			Logger.debug("采购公司未录入，不可编辑");
			return false;
		}
		UIRefPane paneReqStoOrg = ((UIRefPane) bcp.getBodyItem("cvendorname")
				.getComponent());
		paneReqStoOrg.getRefModel().setSealedDataShow(false);
		Object oTemp = bcp.getBodyValueAt(e.getRow(), "pk_purcorp");
		if (oTemp != null) {
			paneReqStoOrg.getRefModel().setPk_corp(oTemp.toString());
			paneReqStoOrg.getRefModel().reloadData();
		}
		return true;
	}

	/**
	 * 项目阶段 编辑前设置参照，参照当前公司项目阶段档案
	 */
	public static void beforeEditWhenBodyProjectPhase(BillCardPanel bcp,
			BillEditEvent e) {
		// 编辑前参照设置
		UIRefPane paneReqStoOrg = ((UIRefPane) bcp.getBodyItem(
				"cprojectphasename").getComponent());
		int iRow = e.getRow();
		Object oTemp = bcp.getBodyValueAt(iRow, "cprojectid");
		if (oTemp != null) {
			ProjectPhase rmArrStoOrg = new ProjectPhase(oTemp.toString());
			paneReqStoOrg.setRefModel(rmArrStoOrg);
			bcp.getBillData().getBillModel().setCellEditable(
					iRow,
					"cprojectphasename",
					bcp.getBillData().getBillModel().getItemByKey(
							"cprojectphasename").isEdit());
		} else {
			bcp.getBillData().getBillModel().setCellEditable(iRow,
					"cprojectphasename", false);
		}
	}

	/**
	 * 项目 编辑前设置参照，参照当前公司项目档案
	 */
	public static void beforeEditWhenBodyProject(BillCardPanel bcp,
			BillEditEvent e) {
		// 编辑前参照设置：参照需求公司库存组织
		UIRefPane paneReqStoOrg = ((UIRefPane) bcp.getBodyItem("cprojectname")
				.getComponent());
		// JobmngfilDefaultRefModel rmArrStoOrg = new
		// JobmngfilDefaultRefModel("项目管理档案");
		AbstractRefModel rmArrStoOrg = paneReqStoOrg.getRefModel();
		rmArrStoOrg.setSealedDataShow(false);
		// paneReqStoOrg.setRefModel(rmArrStoOrg);

	}

	/**
	 * <p>
	 * 批次号：编辑前检查：如果需求公司为空或上层来源单据ID非空，则不可编辑(+V31SP1逻辑)
	 * 
	 * <p>
	 * 
	 * @修改： V5
	 */
	public static boolean beforeEditWhenBodyProduceNum(ToftPanel ui,
			BillCardPanel bcp, String sLoginCorpId, BillEditEvent e) {

		int iRow = e.getRow();

		// 存货不批次管理，直接返回
		if (!PuTool
				.isBatchManaged((String) bcp.getBodyValueAt(iRow, "cmangid"))) {
			ui.showHintMessage(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000531")/* @res "存货不是批次管理，不可编辑" */);
			return false;
		}
		//
		ParaVOForBatch vo = new ParaVOForBatch();

		// 传入FieldName
		vo.setMangIdField("cmangid");
		vo.setInvCodeField("cinventorycode");
		vo.setInvNameField("cinventoryname");
		vo.setSpecificationField("cinventoryspec");
		vo.setInvTypeField("cinventorytype");
		vo.setMainMeasureNameField("cinventoryunit");
		vo.setAssistUnitIDField("cassistunit");
		vo.setIsAstMg(new UFBoolean(PuTool.isAssUnitManaged((String) bcp
				.getBodyValueAt(iRow, "cbaseid"))));
		vo.setWarehouseIDField("cwarehouseid");
		vo.setFreePrefix("vfree");

		// 设置卡片模板,公司等
		vo.setCardPanel(bcp);
		vo.setPk_corp(sLoginCorpId);
		vo.setEvent(e);

		try {
			PuTool.beforeEditWhenBodyBatch(vo);
		} catch (Exception ex) {
			Logger.debug("设置批次号出错：" + ex.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * a) 采购员 编辑前: 参照内容取“采购公司”下的人员档案
	 */
	public static void beforeEditWhenBodyEmployer(BillCardPanel bcp,
			BillEditEvent e) {

		UIRefPane paneEmployee = ((UIRefPane) bcp.getBodyItem("cemployeename")
				.getComponent());
		AbstractRefModel rmArrStoOrg = paneEmployee.getRefModel();
		rmArrStoOrg.setSealedDataShow(false);// 不显示封存数据
		Object oTemp = bcp.getBodyValueAt(e.getRow(), "pk_purcorp");
		if (oTemp != null) {
			rmArrStoOrg.setPk_corp(oTemp.toString());
			rmArrStoOrg.reloadData();
		}
		paneEmployee.setRefModel(rmArrStoOrg);
	}

	/**
	 * 作者：周晓 功能：修改存货前动作 参数：BillEditEvent e 捕捉到的BillEditEvent事件 返回：无 例外：无
	 * 日期：(2005-6-23 11:39:21) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @修改：czp,V5,重构为支持修订调用
	 */
	public static void beforeEditWhenBodyInventory(BillCardPanel bcPanel,
			Hashtable hashBizType, Hashtable hashInvbasIds,
			String sLoginCorpId, String sLoginDate, BillEditEvent e) {
		String[] invmandoc = null;
		UIRefPane refpane = (UIRefPane) bcPanel.getBodyItem("cinventorycode")
				.getComponent();
		Object cproductmanid = bcPanel.getBodyValueAt(e.getRow(),
				"cproductmanid");
		Object cproductbasid = bcPanel.getBodyValueAt(e.getRow(),
				"cproductbasid");
		String sPsnKey[] = new String[1];
		StringBuffer con = new StringBuffer();
		String basid = null;
		// 过滤存货参照,保证在业务类型是受托代销业务类型时替换件参照是受托代销属性
		String CBizType = bcPanel.getHeadItem("cbiztype").getValue();
		boolean checker = false;
		try {
			if (!hashBizType.containsKey(CBizType)) {
				Object[] oaTemp = (Object[]) CacheTool.getCellValue(
						"bd_busitype", "pk_busitype", "verifyrule", CBizType);
				if (oaTemp != null && oaTemp.length > 0 && oaTemp[0] != null) {
					checker = "S".equalsIgnoreCase(oaTemp[0].toString().trim());
				}
				hashBizType.put(CBizType, String.valueOf(checker));
			} else {
				checker = new UFBoolean(hashBizType.get(CBizType).toString())
						.booleanValue();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (cproductmanid != null
				&& cproductmanid.toString().trim().length() > 0
				&& cproductbasid == null) {
			if (!hashInvbasIds.containsKey(cproductmanid)) {
				FormulaParse f = new FormulaParse();
				// 获得基本档案ID
				String sExpress1 = "getColValue(bd_invmandoc,pk_invbasdoc,pk_invmandoc,cproductmanid)";
				f.setExpressArray(new String[] { sExpress1 });
				VarryVO varry[] = f.getVarryArray();
				String sParam1[] = new String[1];
				sParam1[0] = cproductmanid.toString();
				f.addVariable(varry[0].getVarry()[0], sParam1);
				sPsnKey = f.getValueSArray()[0];
				if (sPsnKey != null && sPsnKey.length > 0) {
					basid = sPsnKey[0];
					hashInvbasIds.put(cproductmanid, basid);
				}
			} else {
				Object basId = hashInvbasIds.get(cproductmanid);
				if (basId != null && basId.toString().trim().length() > 0) {
					basid = basId.toString().trim();
				}
			}
		}
		// 产品不为空，则调用公共组件：查询子件；
		String wherePart = " upper(ISNULL(discountflag,'N')) = 'N' and upper(ISNULL(bd_invmandoc.sealflag,'N')) = 'N' and bd_invmandoc.pk_corp = '"
				+ sLoginCorpId
				+ "' and bd_invmandoc.pk_invbasdoc = bd_invbasdoc.pk_invbasdoc AND UPPER(ISNULL(bd_invmandoc.iscanpurchased,'Y')) = 'Y' ";
		if (checker) {
			wherePart += " and ( bd_invmandoc.sellproxyflag = 'Y') ";
		}
		if ((basid != null && basid.toString().trim().length() > 0)
				|| (cproductbasid != null && cproductbasid.toString().trim()
						.length() > 0)) {
			try {
				if (basid != null && basid.toString().trim().length() > 0) {
					invmandoc = PraybillHelper.queryForwardMultiple(
							sLoginCorpId, null, basid, sLoginDate);
				} else if (cproductbasid != null
						&& cproductbasid.toString().trim().length() > 0) {
					invmandoc = PraybillHelper.queryForwardMultiple(
							sLoginCorpId, null, cproductbasid.toString(),
							sLoginDate);
				}
			} catch (Exception e1) {
				SCMEnv.out(e);
			}
			if (invmandoc != null && invmandoc.length > 0) {
				for (int i = 0; i < invmandoc.length; i++) {
					if (i < invmandoc.length - 1) {
						con.append(" '" + invmandoc[i] + "',");
					} else if (i == invmandoc.length - 1) {
						con.append(" '" + invmandoc[i] + "' ");
					}
				}
			}
			// UIRefPane refpaneNew = new UIRefPane();
			wherePart += " and bd_invmandoc.pk_invbasdoc=bd_invbasdoc.pk_invbasdoc and bd_invmandoc.pk_corp ='"
					+ sLoginCorpId + "' ";
			if (con != null && con.length() > 1) {
				wherePart += "and bd_invmandoc.pk_invmandoc in("
						+ con.toString() + ") ";
			} else if (con.length() == 0) {
				wherePart += "and bd_invmandoc.pk_invmandoc is null ";
			}
		}
		InvmandocDefaultRefModel refModel = new InvmandocDefaultRefModel("存货档案");
		refModel.setWherePart(wherePart);
		refModel.setPk_corp(sLoginCorpId);
		refpane.setTreeGridNodeMultiSelected(true);
		refpane.setMultiSelectedEnabled(true);
		refpane.setCacheEnabled(false);
		refpane.setRefModel(refModel);
	}

	/**
	 * 作者：周晓 功能：修改产品前动作 参数：BillEditEvent e 捕捉到的BillEditEvent事件 返回：无 例外：无
	 * 日期：(2005-6-23 11:39:21) 修改日期，修改人，修改原因，注释标志：
	 */
	public static void beforeEditWhenBodyProduct(BillCardPanel bcPanel,
			String sLoginCorpId, String sLoginDate, BillEditEvent e) {

		bcPanel.stopEditing();

		final int n = e.getRow();
		UIRefPane refpane = null;

		String[] invmandoc = null;
		// String condition = null;
		StringBuffer con = new StringBuffer();
		refpane = (UIRefPane) bcPanel.getBodyItem("cproductname")
				.getComponent();
		// refpane = new UIRefPane();
		// Object cproductmanid = bcPanel.getBodyValueAt(n, "cproductmanid");
		// Object cproductname = bcPanel.getBodyValueAt(n, "cproductname");
		Object cbaseid = bcPanel.getBodyValueAt(n, "cbaseid");
		String basid = null;
		if (cbaseid != null && cbaseid.toString().trim().length() > 0) {
			basid = cbaseid.toString().trim();
		}
		// 调用公共组件：查询存货父项-产品

		try {
			invmandoc = PraybillHelper.queryBackMultiple(sLoginCorpId, null,
					basid, sLoginDate);
		} catch (Exception e1) {
			SCMEnv.out(e);
		}

		if (invmandoc != null && invmandoc.length > 0) {
			for (int i = 0; i < invmandoc.length; i++) {
				if (i < invmandoc.length - 1) {
					con.append(" '");
					con.append(invmandoc[i] + "',");
				} else if (i == invmandoc.length - 1) {
					con.append(" '");
					con.append(invmandoc[i] + "' ");
				}
			}

		}

		String wherePart = " AND bd_invmandoc.pk_invbasdoc=bd_invbasdoc.pk_invbasdoc   and bd_invmandoc.pk_corp ='"
				+ sLoginCorpId + "'";
		if (con != null && con.length() > 1) {
			wherePart = "and bd_invmandoc.pk_invmandoc in(" + con.toString()
					+ ")" + wherePart;

		} else if (con.length() == 0) {
			wherePart = "and bd_invmandoc.pk_invmandoc is null " + wherePart;
		}
		// NewInventoryRef refModel = new NewInventoryRef();
		nc.ui.bd.ref.DefaultRefModel refModel = new nc.ui.bd.ref.DefaultRefModel();
		refModel.setPk_corp(sLoginCorpId);
		refModel.setRefNodeName("存货档案");
		refModel.addWherePart(wherePart);
		refpane.setRefModel(refModel);
		// refModel.setPk_corp(getCorpId());

		// 封存不显示
		// refModel.addWherePart(wherePart);
		refpane.setRefModel(refModel);
	}

	/**
	 * 功能描述:行变换事件响应
	 * 
	 * @param event
	 *            nc.ui.pub.bill.BillEditEvent
	 */
	public void bodyRowChange(BillEditEvent event) {
		if ((UITable) event.getSource() == getBillCardPanel().getBillTable()) {
			// 设置项目阶段可编辑性
			setProjPhaseEditable(event);
		} else if ((UITable) event.getSource() == getBillListPanel()
				.getBodyTable()) {
			// 设置辅计量参照及辅信息可编辑性
			// setAssisUnitEditState2(event);
			// 不同表体行重新设置相应的项目阶段参照
			Object oTemp = getBillCardPanel().getBodyValueAt(event.getRow(),
					"cprojectid");
			if (oTemp != null) {
				UIRefPane nRefPanel = (UIRefPane) getBillCardPanel()
						.getBodyItem("cprojectphasename").getComponent();
				nRefPanel.setIsCustomDefined(true);
				nRefPanel.setRefModel(new ProjectPhase((String) oTemp));
			}
			// 不同表体行重新设置相应的自由项自定义参照
			InvVO invVO = getVOForFreeItem(event);
			invVO.setIsFreeItemMgt(new Integer(1));
			m_freeItem.setFreeItemParam(invVO);
		}
	}

	/**
	 * 功能描述:改变界面按钮状态
	 */
	private void updateButtonsAll() {
		int iLen = getBtnTree().getButtonArray().length;
		for (int i = 0; i < iLen; i++) {
			update(getBtnTree().getButtonArray()[i]);
		}
	}

	/**
	 * 创建日期： 2005-9-20 功能描述： 更新按钮状态（递归方式）
	 */
	private void update(ButtonObject bo) {
		updateButton(bo);
		if (bo.getChildCount() > 0) {
			for (int i = 0, len = bo.getChildCount(); i < len; i++)
				update(bo.getChildButtonGroup()[i]);
		}
	}

	/**
	 * 功能描述:存货为辅计量管理时,检查辅计量是否存在
	 * 
	 * @return java.lang.String
	 */
	private String checkAssistUnit() {
		String sMessage = "";
		final int nRow = getBillCardPanel().getRowCount();
		Object temp = null;
		Object oAssNum = null;
		Object oAssUnit = null;
		UFDouble ufdAssNum = null;
		Vector vLines = new Vector();
		for (int i = 0; i < nRow; i++) {
			temp = getBillCardPanel().getBodyValueAt(i, "cbaseid");
			if (temp == null || temp.toString().trim().equals(""))
				continue;
			if (nc.ui.pu.pub.PuTool.isAssUnitManaged(temp.toString().trim())) {
				oAssNum = getBillCardPanel().getBodyValueAt(i, "nassistnum");
				oAssUnit = getBillCardPanel().getBodyValueAt(i,
						"cassistunitname");
				ufdAssNum = (oAssNum == null || oAssNum.toString().trim()
						.equals("")) ? null : new UFDouble(oAssNum.toString()
						.trim());
				if (oAssUnit == null || oAssUnit.toString().trim().equals("")
						|| ufdAssNum == null || ufdAssNum.doubleValue() == 0) {
					if (vLines.size() > 0) {
						vLines.addElement(m_lanResTool.getStrByID("40040101",
								"UPPSCMCommon-000000")/*
													 * @res "、"
													 */);
					}
					vLines.addElement(new Integer(i + 1));
				}
			}
		}
		int vLinesSize = vLines.size();
		if (vLinesSize > 0) {
			sMessage += vLines.elementAt(0);
			for (int i = 1; i < vLinesSize; i++) {
				sMessage += ",";
				sMessage += vLines.elementAt(i);
			}
		}
		String[] value = new String[] { sMessage };
		if (value != null && value.length > 0 && value[0] != null
				&& !value[0].equals("")) {
			sMessage = (m_lanResTool.getStrByID("40040101",
					"UPP40040101-000437", null, value));
		}
		return sMessage;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2003-11-20 17:09:22)
	 */
	private boolean checkBeforeSave(PraybillVO VO) {

		/* 检查单据行号 */
		if (!BillRowNo.verifyRowNosCorrect(getBillCardPanel(), "crowno")) {
			showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
					"UPPSCMCommon-000010")/*
										 * @res "保存失败"
										 */);
			return false;
		}
		/* 检查单据模板非空项 */
		try {
			nc.ui.pu.pub.PuTool.validateNotNullField(getBillCardPanel());
		} catch (Exception e) {
			MessageDialog.showWarningDlg(this, m_lanResTool.getStrByID(
					"40040101", "UPP40040101-000035")/* "单据模板非空项检查" */, e
					.getMessage());
			showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
					"UPPSCMCommon-000010")/* @res"保存失败" */);
			return false;
		}

		// 合法性检查
		try {
			if (getBillCardPanel().getRowCount() < 1) {
				MessageDialog
						.showErrorDlg(this, m_lanResTool.getStrByID("common",
								"UC001-0000001")/* "保存" */, m_lanResTool
								.getStrByID("40040101", "UPP40040101-000036")/* "请购单表体为空，不能保存！" */);
				showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
						"UPPSCMCommon-000010")/* "保存失败" */);
				return false;
			}
			VO.validate();
		} catch (ValidationException e) {
			MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
					"40040101", "UPP40040101-000037")/* "合法性检查" */, e
					.getMessage());
			showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
					"UPPSCMCommon-000010")/* "保存失败" */);
			return false;
		}
		PraybillItemVO[] items = VO.getBodyVO();
		UFDate ddemanddate = null;
		UFDate dsuggestdate = null;
		UFDate dpraydate = null;
		if (VO.getHeadVO() != null) {
			dpraydate = VO.getHeadVO().getDpraydate();
		}
		if (items != null && items.length > 0) {
			for (int i = 0; i < items.length; i++) {
				ddemanddate = items[i].getDdemanddate();
				dsuggestdate = items[i].getDsuggestdate();
				if (PuPubVO.getString_TrimZeroLenAsNull(items[i]
						.getPk_purcorp()) == null) {
					MessageDialog
							.showErrorDlg(this, m_lanResTool.getStrByID(
									"common", "UC001-0000001")/* "保存" */,
									m_lanResTool.getStrByID("common",
											"4004COMMON000000015")/* @res*采购公司" */
											+ nc.vo.ml.NCLangRes4VoTransl
													.getNCLangRes().getStrByID(
															"smcomm",
															"UPP1005-000239")/*
																			 * @res
																			 * "不能为空。"
																			 */);
					return false;
				}
				if (dpraydate != null && ddemanddate != null
						&& dsuggestdate != null) {
					if (!((dpraydate.before(ddemanddate) || dpraydate
							.equals(ddemanddate)) && (dpraydate
							.before(dsuggestdate) || dpraydate
							.equals(dsuggestdate)))) {
						MessageDialog.showErrorDlg(this, m_lanResTool
								.getStrByID("common", "UC001-0000001")/* "保存" */,
								m_lanResTool.getStrByID("40040101",
										"UPP40040101-000524")/*
															 * 需求日期不能小于请购日期! "
															 */);
						showHintMessage(m_lanResTool.getStrByID("40040101",
								"UPP40040101-000524")/* 请购日期必须小于建议订货日期和需求日期!" */);
						return false;
					}
					if (dsuggestdate != null
							&& ddemanddate != null
							&& !(ddemanddate.after(dsuggestdate) || dsuggestdate
									.equals(ddemanddate))) {
						MessageDialog.showErrorDlg(this, m_lanResTool
								.getStrByID("common", "UC001-0000001")/* "保存" */,
								m_lanResTool.getStrByID("40040101",
										"UPP40040101-000525")/*
															 * 建议采购日期不能小于请购日期! "
															 */);
						showHintMessage(m_lanResTool.getStrByID("40040101",
								"UPP40040101-000525")/* 建议采购日期必须小于需求日期!" */);
						return false;
					}
				} else if (dpraydate != null && ddemanddate != null
						&& dsuggestdate == null) {
					if (!(dpraydate.before(ddemanddate) || dpraydate
							.equals(ddemanddate))) {
						MessageDialog.showErrorDlg(this, m_lanResTool
								.getStrByID("common", "UC001-0000001")/* "保存" */,
								m_lanResTool.getStrByID("40040101",
										"UPP40040101-000526")/*
															 * 请购日期必须小于需求日期! "
															 */);
						showHintMessage(m_lanResTool.getStrByID("40040101",
								"UPP40040101-000526")/* 请购日期必须小于需求日期!" */);
						return false;
					}
				}
			}
		}

		String sMessage = checkAssistUnit();
		if (sMessage.length() > 0) {
			MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
					"40040101", "UPP40040101-000037")/* "合法性检查" */, sMessage);
			showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
					"UPPSCMCommon-000010")/* "保存失败" */);
			return false;
		}
		return true;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2003-11-20 18:05:17)
	 */
	private void dispAfterSave(PraybillVO VO, ArrayList keys) {
		try {
			// 计算自由项
			new nc.ui.scm.pub.FreeVOParse().setFreeVO(VO.getBodyVO(), "vfree",
					"vfree", "cbaseid", "cmangid", true);

		} catch (Exception e) {
			reportException(e);
		}
		Vector vTemp = new Vector();
		if (m_VOs != null) {
			// 已经存在请购单(新增)
			if (m_bAdd) {
				for (int i = 0; i < m_VOs.length; i++) {
					vTemp.addElement(m_VOs[i]);
				}
				vTemp.addElement(VO);
				m_VOs = new PraybillVO[vTemp.size()];
				vTemp.copyInto(m_VOs);
				m_nPresentRecord = m_VOs.length - 1;
				// 已经存在请购单(修改)
			} else {
				m_VOs[m_nPresentRecord] = VO;
			}
		} else {
			/* 不存在请购单 */
			m_VOs = new PraybillVO[1];
			m_VOs[0] = VO;
			m_nPresentRecord = 0;
		}
		/* 设置保存，行操作，放弃为灰，其它正常 */
		getBillCardPanel().setEnabled(false);
		/* 单据重新装载数据，并更新界面 */
		for (int i = 0; i < 1; i++) {
			try {
				getBillCardPanel().setBillValueVO(m_VOs[m_nPresentRecord]);
			} catch (Exception e) {
				continue;
			}
		}

		nc.ui.pu.pub.PuTool.loadInvMaxPrice(getBillCardPanel());
		/* 显示状态图片 */
		// V5 Del : setImageTypeMy(m_nPresentRecord);
		getBillCardPanel().getBillModel().execLoadFormula();
		getBillCardPanel().updateValue();
		getBillCardPanel().setEnabled(false);
		m_bEdit = false;
		/* 显示备注 */
		UIRefPane nRefPanel3 = (UIRefPane) getBillCardPanel().getHeadItem(
				"vmemo").getComponent();
		nRefPanel3.setValue(m_VOs[m_nPresentRecord].getHeadVO().getVmemo());
		// 显示单据来源信息
		nc.ui.pu.pub.PuTool.loadSourceInfoAll(getBillCardPanel(),
				nc.vo.scm.pu.BillTypeConst.PO_PRAY);
		// 重置新增标志
		m_bAdd = false;
		setButtonsCard();
		m_nUIState = 0;
	}

	/**
	 * 作者：汪维敏 功能：填充表头公式数据，避免不可参照出的数据 参数： 返回： 例外： 日期：(2004-5-20 14:18:41)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @param vo
	 *            nc.vo.pr.pray.PraybillVO
	 */
	public void execHeadTailFormula(PraybillVO vo) {
		if (vo == null) {
			return;
		}
		// 单个公式
		PraybillHeaderVO voHead = vo.getHeadVO();
		UIRefPane refpaneA = null;

		String[] saFormulaA = new String[] { "getColValue(bd_busitype,businame,pk_busitype,cbiztype)" };
		PuTool.getFormulaParse().setExpressArray(saFormulaA);

		PuTool.getFormulaParse().addVariable("cbiztype",
				new String[] { voHead.getCbiztype() });
		String[][] saRetA = PuTool.getFormulaParse().getValueSArray();

		refpaneA = (UIRefPane) getBillCardPanel().getHeadItem("cbiztype")
				.getComponent();

		if (nc.vo.scm.pu.PuPubVO.getString_TrimZeroLenAsNull(refpaneA
				.getUITextField().getText()) == null) {
			if (saRetA != null && saRetA[0] != null)
				refpaneA.getUITextField().setText(saRetA[0][0]);
		}
	}

	/**
	 * 此处插入方法说明。 功能描述:过虑掉空行 作者：汪维敏 输入参数: 返回值: 异常处理: 日期:
	 */
	private void filterNullLine() {
		// 存货列值暂存
		Object oTempValue = null;
		// 表体model
		nc.ui.pub.bill.BillModel bmBill = getBillCardPanel().getBillModel();
		// 存货列号，效率高一些。
		final int iInvCol = bmBill.getBodyColByKey(m_sInvMngIDItemKey);

		// 必须有存货列
		if (bmBill != null && iInvCol >= 0 && iInvCol < bmBill.getColumnCount()) {
			// 行数
			final int iRowCount = getBillCardPanel().getRowCount();
			// 从后向前删
			for (int line = iRowCount - 1; line >= 0; line--) {
				// 存货未填
				oTempValue = bmBill.getValueAt(line, iInvCol);
				if (oTempValue == null
						|| oTempValue.toString().trim().length() == 0)
					// 删行
					getBillCardPanel().getBillModel().delLine(
							new int[] { line });
			}
		}
		if (bmBill.getRowCount() <= 0)
			onAppendLine(getBillCardPanel(), this);
	}

	/**
	 * 功能描述:获得存货的采购提前期
	 */
	private static int getAdvanceDays(ToftPanel ui, BillCardPanel bcp,
			BillEditEvent event) {
		final int nRow = bcp.getRowCount();
		PraybillVO VO = new PraybillVO(nRow);
		bcp.getBillValueVO(VO);
		PraybillItemVO[] bodyVO = VO.getBodyVO();

		final int n = event.getRow();
		Vector v = new Vector();
		try {
			String s0 = bodyVO[n].getPk_reqcorp();
			String s1 = bodyVO[n].getPk_reqstoorg();
			String s2 = bodyVO[n].getCbaseid();
			if (s1 != null && s1.length() > 0 && s2 != null && s2.length() > 0)
				v = PraybillHelper.queryAdvanceDays(s0, s1, s2);
			else
				return 0;
		} catch (java.sql.SQLException e) {
			MessageDialog.showErrorDlg(ui, m_lanResTool.getStrByID("40040101",
					"UPP40040101-000047")/* "采购提前期" */, m_lanResTool.getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000412")/* "SQL语句错误！" */);
			return -1;
		} catch (ArrayIndexOutOfBoundsException e) {
			MessageDialog.showErrorDlg(ui, m_lanResTool.getStrByID("40040101",
					"UPP40040101-000047")/* "采购提前期" */, m_lanResTool.getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000426")/* "数组越界错误！" */);
			return -1;
		} catch (NullPointerException e) {
			MessageDialog.showErrorDlg(ui, m_lanResTool.getStrByID("40040101",
					"UPP40040101-000047")/* "采购提前期" */, m_lanResTool.getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000427")/* "空指针错误！" */);
			return -1;
		} catch (Exception e) {
			MessageDialog.showErrorDlg(ui, m_lanResTool.getStrByID("40040101",
					"UPP40040101-000047")/* "采购提前期" */, e.getMessage());
			return -1;
		}
		if (v.size() == 0)
			return 0;
		UFDouble dFixedahead = (UFDouble) v.elementAt(0);
		UFDouble dAheadcoff = (UFDouble) v.elementAt(1);
		UFDouble dAheadbatch = (UFDouble) v.elementAt(2);
		UFDouble d = bodyVO[n].getNpraynum();

		// V5,Czp,重构，保证提前期算法只有一处实现
		return AdDayVO.getAdDaysArith(d, dFixedahead, dAheadcoff, dAheadbatch);
	}

	/**
	 * 功能：获取存量查询对话框
	 * 
	 */
	private ATPForOneInvMulCorpUI getAtpDlg() {
		if (null == m_atpDlg) {
			m_atpDlg = new ATPForOneInvMulCorpUI(this);
		}
		return m_atpDlg;
	}

	/**
	 * 功能描述:获得卡片单据控制
	 */
	private BillCardPanel getBillCardPanel() {
		if (m_billPanel == null) {
			try {
				m_billPanel = new BillCardPanel();

				m_billPanel.loadTemplet("20", null, getClientEnvironment()
						.getUser().getPrimaryKey(), m_sLoginCorpId,
						new MyBillData());
				// 设置千分位
				m_billPanel.setBodyShowThMark(true);
				// 合计行显示
				m_billPanel.setTatolRowShow(true);
				// 加载行号
				BillRowNo.loadRowNoItem(m_billPanel, "crowno");

				// 国际化
				nc.ui.pu.pub.PuTool.setTranslateRender(m_billPanel);

				// 处理自定义项
				nc.ui.scm.pub.def.DefSetTool.updateBillCardPanelUserDef(
						m_billPanel, getClientEnvironment().getCorporation()
								.getPk_corp(), ScmConst.PO_Pray, // 单据类型
						"vdef", "vdef");

				// 版本号:小数位显示一位
				m_billPanel.getHeadItem("nversion").setDecimalDigits(1);

			} catch (Exception e) {
				SCMEnv.out(e);
				MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
						"40040101", "UPP40040101-000048")/* "加载模板" */,
						m_lanResTool.getStrByID("40040101",
								"UPP40040101-000049")/* "模板不存在！" */);
				return null;
			}
		}
		return m_billPanel;
	}

	/**
	 * 功能描述:获得列表单据控制
	 */
	private BillListPanel getBillListPanel() {
		if (m_listPanel == null) {
			try {
				m_listPanel = new BillListPanel();
				m_listPanel.setName("ListPanel");
				try {
					m_listPanel.loadTemplet("20", null, getClientEnvironment()
							.getUser().getPrimaryKey(), m_sLoginCorpId);
				} catch (Exception e) {
					SCMEnv.out(e);
					m_listPanel.loadTemplet("40040101010000000000");
				}
				// 隐藏公司
				if (m_listPanel.getHeadItem("pk_corpname") != null)
					m_listPanel.hideHeadTableCol("pk_corpname");

				// 设置千分位
				m_listPanel.getParentListPanel().setShowThMark(true);
				m_listPanel.getChildListPanel().setShowThMark(true);

				BillListData bd = m_listPanel.getBillListData();
				// 初始化精度
				bd = initListDecimal(bd);

				// 加载列表数据
				m_listPanel.setListData(bd);

				m_listPanel.getHeadTable().setSelectionMode(
						ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

				m_listPanel.addEditListener(this);
				m_listPanel.addMouseListener(this);

				// 处理自定义项
				nc.ui.scm.pub.def.DefSetTool.updateBillListPanelUserDef(
						m_listPanel, getClientEnvironment().getCorporation()
								.getPk_corp(), ScmConst.PO_Pray, // 单据类型
						"vdef", "vdef");

				m_listPanel.updateUI();

				m_listPanel.getHeadTable().setCellSelectionEnabled(false);
				m_listPanel
						.getHeadTable()
						.setSelectionMode(
								javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				m_listPanel.getHeadTable().getSelectionModel()
						.addListSelectionListener(this);
				// 增加合计行
				m_listPanel.getChildListPanel().setTotalRowShow(true);

				// 增加行号排序监听
				m_listPanel.getBodyBillModel().setSortPrepareListener(this);

				// 排序监听
				m_listPanel.getHeadBillModel().addSortRelaObjectListener2(this);
			} catch (Exception e) {
				SCMEnv.out(e);
				MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
						"40040101", "UPP40040101-000048")/* "加载模板" */,
						m_lanResTool.getStrByID("40040101",
								"UPP40040101-000049")/* "模板不存在！" */);
				return null;
			}
		}

		return m_listPanel;
	}

	/**
	 * 子类实现该方法，返回业务界面的标题
	 */
	private String getCauditid() {

		return m_cauditid;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2003-10-28 14:53:42)
	 */
	private int getCurVoPos() {
		return m_nPresentRecord;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2003-10-28 15:02:52)
	 */
	private PraybillVO[] getPraybillVOs() {
		return m_VOs;
	}

	/**
	 * 功能：返回查询对话框 参数： 返回： 例外： 日期：(2002-9-13 9:47:11) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return nc.ui.pub.query.QueryConditionClient
	 */
	private PrayUIQueryDlg getQueryDlg() {

		if (m_condClient == null) {
			m_condClient = new PrayUIQueryDlg(this, m_sLoginCorpId);
			DefSetTool.updateQueryConditionClientUserDef(m_condClient,
					m_sLoginCorpId, ScmConst.PO_Pray, // 单据类型
					"po_praybill.def", "po_praybill_b.def");
		}

		return m_condClient;
	}

	/**
	 * 修改人：李金巧 功 能：去掉参照加载数据，提高效率 日 期：2002-05-30
	 */

	private UIRefPane getRefWherePart(UIRefPane refpane, String pk_corp,
			String key) {

		String wherepart = refpane.getRefModel().getWherePart();
		if ((wherepart != null) && (!(wherepart.trim().equals("")))
				&& (wherepart.indexOf("pk_corp") >= 0)) {
			// 单位编码
			final int index = wherepart.indexOf("pk_corp");
			final int indexbegin = wherepart.indexOf("'", index + 1);
			int indexend = -1;
			if (indexbegin >= 0) {
				indexend = wherepart.indexOf("'", indexbegin + 1);
			}
			if ((indexbegin >= 0) && (indexend >= 0)) {
				String str1 = wherepart.substring(0, indexbegin + 1);
				String str2 = wherepart.substring(indexend);
				wherepart = str1 + pk_corp + str2;
				refpane.getRefModel().clearData();
				refpane.getRefModel().setWherePart(wherepart);
			}
		} else {
			if (key.trim().equals("cbiztype")) {
				refpane.getRefModel().clearData();
				refpane.setWhereString("bd_busitype.pk_corp = '" + pk_corp
						+ "'");
			}
		}

		return refpane;

	}

	/**
	 * 此处插入方法说明。 创建日期：(2003-11-4 19:59:42)
	 */
	private ArrayList getSelectedBills() {
		Vector vAll = new Vector();
		PraybillVO[] allvos = null;
		// 全部选中询价单
		int iPos = 0;
		for (int i = 0; i < getBillListPanel().getHeadBillModel().getRowCount(); i++) {
			if (getBillListPanel().getHeadBillModel().getRowState(i) == BillModel.SELECTED) {
				iPos = i;
				iPos = nc.ui.pu.pub.PuTool.getIndexBeforeSort(
						getBillListPanel(), iPos);
				vAll.add(getPraybillVOs()[iPos]);
			}
		}
		allvos = new PraybillVO[vAll.size()];
		vAll.copyInto(allvos);

		// 查询未被浏览过的单据体
		try {
			allvos = PrTool.getRefreshedVOs(allvos);
		} catch (BusinessException b) {
			showHintMessage(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000444")/* "发现错误:" */
					+ b.getMessage());
		} catch (Exception e) {
			SCMEnv.out(e);
			showHintMessage(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000445")/* "发现未知错误" */);
		}
		ArrayList aryRslt = new ArrayList();
		if (allvos != null) {
			for (int i = 0; i < allvos.length; i++) {
				aryRslt.add(allvos[i]);
			}
		}
		return aryRslt;
	}

	/**
	 * 作者：汪维敏 功能：接口IBillModelSortPrepareListener 的实现方法 参数：String sItemKey
	 * ITEMKEY 返回：无 例外：无 日期：(2004-03-24 11:39:21) 修改日期，修改人，修改原因，注释标志：
	 */
	public int getSortTypeByBillItemKey(String sItemKey) {

		if ("crowno".equals(sItemKey)) {
			return BillItem.DECIMAL;
		} else if ("csourcebillrowno".equals(sItemKey)) {
			return BillItem.DECIMAL;
		} else if ("cancestorbillrowno".equals(sItemKey)) {
			return BillItem.DECIMAL;
		}

		return getBillCardPanel().getBillModel().getItemByKey(sItemKey)
				.getDataType();
	}

	/**
	 * 子类实现该方法，返回业务界面的标题。
	 * 
	 * @version (00-6-6 13:33:25)
	 * 
	 * @return java.lang.String
	 */
	public String getTitle() {
		// "维护请购单"*/;
		String title = m_lanResTool
				.getStrByID("40040101", "UPP40040101-000453")/* "维护请购单" */;
		if (m_billPanel != null
				&& !getClientEnvironment().getCorporation().getPk_corp()
						.equals("@@@@")) {
			title = m_billPanel.getTitle();
		}
		return title;
	}

	/**
	 * 功能：该方法是实现接口ICheckRetVO的方法 该接口为审批流设计，以实现在审批人点击单据时，出现整张发票
	 * 请不要随意删除及修改该方法，以避免错误 参数：无 返回：nc.vo.pub.AggregatedValueObject 为请购单VO 例外：无
	 * 日期：(2001-5-13 11:39:21) 修改日期，修改人，修改原因，注释标志：
	 */
	public nc.vo.pub.AggregatedValueObject getVo() throws Exception {
		PraybillVO vo = null;
		try {
			vo = PraybillHelper.queryPrayVoByHid(getCauditid());
		} catch (java.lang.Exception e) {
			SCMEnv.out(e);
			throw e;
		}
		// 没有符合条件的单据
		if (vo == null) {
			return null;
		}
		return vo;
	}

	/**
	 * 作者：熊海情 功能：获得存货编码，存货名称，存货规格，存货型号，10个自由项名称，10个自由项值 为自由项服务 参数：BillEditEvent
	 * e 捕捉到的BillEditEvent事件 返回：存货的自由项VO 例外：无 日期：(2002-3-13 11:39:21)
	 * 修改日期，修改人，修改原因，注释标志： 2002-08-08 wyf 修改对于空存货基本ID的处理
	 */
	private InvVO getVOForFreeItem(BillEditEvent event) {
		final int iRow = event.getRow();

		// 用于返回的VO
		InvVO tempVO = new InvVO();
		String sBaseId = (String) getBillCardPanel().getBodyValueAt(iRow,
				"cbaseid");
		if (sBaseId == null) {
			return tempVO;
		}

		// 获得存货编码，存货名称，存货规格，存货型号及存货基础ID
		tempVO.setCinventorycode((String) getBillCardPanel().getBodyValueAt(
				iRow, "cinventorycode"));
		tempVO.setInvname((String) getBillCardPanel().getBodyValueAt(iRow,
				"cinventoryname"));
		tempVO.setInvspec((String) getBillCardPanel().getBodyValueAt(iRow,
				"cinventoryspec"));
		tempVO.setInvtype((String) getBillCardPanel().getBodyValueAt(iRow,
				"cinventorytype"));
		tempVO.setCinventoryid((String) getBillCardPanel().getBodyValueAt(iRow,
				"cbaseid"));

		try {
			FreeVO freeVO = PraybillHelper.queryVOForFreeItem(tempVO
					.getCinventoryid());
			tempVO.setFreeItemVO(freeVO);
		} catch (Exception e) {
			SCMEnv.out(e);
			// return null;
		}

		return tempVO;
	}

	/**
	 * 功能描述:初始化
	 */
	private void init() {

		// 初始化参数
		initPara();

		// 显示单据
		setLayout(new java.awt.BorderLayout());
		add(getBillCardPanel(), java.awt.BorderLayout.CENTER);

		// ---------单据模板加载后的初始化－－－－－－－

		// 初始化按钮
		initButton();

		// 增加监听
		initListener();

		getBillCardPanel().setEnabled(false);
	}

	/**
	 * 功能描述:加载单据模板之前的初始化
	 */

	private void initBillBeforeLoad(BillData bd) {

		// ---------单据模板加载前的初始化－－－－－－－

		// 初始化参照
		initRefpane(bd);
		// 初始化ComboBox
		initComboBox(bd);
		// 初始化精度
		initDecimal(bd);
		// 初始化不可用列
		initiEnabledFalseItems(bd);

	}

	/**
	 * 作者：王印芬 功能：对不可编辑项进行设置 参数：无 返回：无 例外：无 日期：(2002-8-26 11:39:21)
	 * 修改日期，修改人，修改原因，注释标志： 2002-10-23 WYF 加入对开户银行码的限制
	 */
	private void initiEnabledFalseItems(BillData bd) {
		// 表头
		// 业务类型
		bd.getHeadItem("cbiztype").setEnabled(false);
		//
		// // 表体
		// //需求公司 不可编辑 默认不显示
		if (bd.getBodyItem("pk_reqcorp") != null) {
			bd.getBodyItem("pk_reqcorp").setEnabled(false);
		}

		return;
	}

	/**
	 * V51重构需要的匹配,按钮实例变量化。
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * 使用示例
	 * <p>
	 * <b>参数说明</b>
	 * <p>
	 * 
	 * @author czp
	 * @time 2007-3-13 下午01:15:06
	 */
	private void createBtnInstances() {
		// 业务类型
		m_btnBusiTypes = getBtnTree().getButton(
				IButtonConstPr.BTN_BUSINESS_TYPE);
		// 增加
		m_btnAdds = getBtnTree().getButton(IButtonConstPr.BTN_ADD);
		// 保存
		m_btnSave = getBtnTree().getButton(IButtonConstPr.BTN_SAVE);
		// 单据
		m_btnMaintains = getBtnTree().getButton(IButtonConstPr.BTN_BILL);
		m_btnModify = getBtnTree().getButton(IButtonConstPr.BTN_BILL_EDIT);
		m_btnModifyList = m_btnModify;
		m_btnCancel = getBtnTree().getButton(IButtonConstPr.BTN_BILL_CANCEL);
		m_btnDiscard = getBtnTree().getButton(IButtonConstPr.BTN_BILL_DELETE);
		m_btnDiscardList = m_btnDiscard;
		m_btnCopy = getBtnTree().getButton(IButtonConstPr.BTN_BILL_COPY);
		// 行操作
		m_btnLines = getBtnTree().getButton(IButtonConstPr.BTN_LINE);
		m_btnAddLine = getBtnTree().getButton(IButtonConstPr.BTN_LINE_ADD);
		m_btnDelLine = getBtnTree().getButton(IButtonConstPr.BTN_LINE_DELETE);
		m_btnInsLine = getBtnTree().getButton(IButtonConstPr.BTN_LINE_INSERT);
		m_btnCpyLine = getBtnTree().getButton(IButtonConstPr.BTN_LINE_COPY);
		m_btnPstLine = getBtnTree().getButton(IButtonConstPr.BTN_LINE_PASTE);
		// 审批
		m_btnAudit = getBtnTree().getButton(IButtonConstPr.BTN_AUDIT);
		m_btnApprove = m_btnAudit;
		m_btnApprove.setTag("APPROVE");
		// 执行
		m_btnActions = getBtnTree().getButton(IButtonConstPr.BTN_EXECUTE);
		m_btnUnAudit = getBtnTree().getButton(
				IButtonConstPr.BTN_EXECUTE_AUDIT_CANCEL);
		m_btnUnApprove = m_btnUnAudit;
		m_btnUnApprove.setTag("UNAPPROVE");
		m_btnSendAudit = getBtnTree().getButton(
				IButtonConstPr.BTN_EXECUTE_AUDIT);
		m_btnClose = getBtnTree().getButton(IButtonConstPr.BTN_EXECUTE_CLOSE);
		m_btnClose.setTag("CLOSE");
		m_btnOpen = getBtnTree().getButton(IButtonConstPr.BTN_EXECUTE_OPEN);
		m_btnOpen.setTag("OPEN");
		// 查询
		m_btnQuery = getBtnTree().getButton(IButtonConstPr.BTN_QUERY);
		m_btnQueryList = m_btnQuery;
		// 浏览
		m_btnBrowses = getBtnTree().getButton(IButtonConstPr.BTN_BROWSE);
		m_btnRefresh = getBtnTree()
				.getButton(IButtonConstPr.BTN_BROWSE_REFRESH);
		m_btnRefreshList = m_btnRefresh;
		m_btnFirst = getBtnTree().getButton(IButtonConstPr.BTN_BROWSE_TOP);
		m_btnPrev = getBtnTree().getButton(IButtonConstPr.BTN_BROWSE_PREVIOUS);
		m_btnNext = getBtnTree().getButton(IButtonConstPr.BTN_BROWSE_NEXT);
		m_btnLast = getBtnTree().getButton(IButtonConstPr.BTN_BROWSE_BOTTOM);
		m_btnSelectAll = getBtnTree().getButton(
				IButtonConstPr.BTN_BROWSE_SELECT_ALL);
		m_btnSelectNo = getBtnTree().getButton(
				IButtonConstPr.BTN_BROWSE_SELECT_NONE);
		// 卡片显示/列表显示
		m_btnCard = getBtnTree().getButton(IButtonConstPr.BTN_SWITCH);
		m_btnList = m_btnCard;
		// 打印
		m_btnPrints = getBtnTree().getButton(IButtonConstPr.BTN_PRINT);
		m_btnPrint = getBtnTree().getButton(IButtonConstPr.BTN_PRINT_PRINT);
		m_btnPrintList = m_btnPrint;
		m_btnPrintPreview = getBtnTree().getButton(
				IButtonConstPr.BTN_PRINT_PREVIEW);
		m_btnPrintListPreview = m_btnPrintPreview;
		m_btnCombin = getBtnTree().getButton(IButtonConstPr.BTN_PRINT_DISTINCT);
		// 辅助查询
		m_btnOthersQry = getBtnTree()
				.getButton(IButtonConstPr.BTN_ASSIST_QUERY);
		m_btnLinkBillsBrowse = getBtnTree().getButton(
				IButtonConstPr.BTN_ASSIST_QUERY_RELATED);
		m_btnUsable = getBtnTree().getButton(
				IButtonConstPr.BTN_ASSIST_QUERY_ONHAND);
		m_btnUsableList = m_btnUsable;
		m_btnWorkFlowBrowse = getBtnTree().getButton(
				IButtonConstPr.BTN_ASSIST_QUERY_WORKFLOW);
		m_btnQueryForAuditList = m_btnWorkFlowBrowse;
		m_btnPriceInfo = getBtnTree().getButton(
				IButtonConstPr.BTN_ASSIST_QUERY_PRICE_REPORT);
		// 辅助功能
		m_btnOthersFuncs = getBtnTree().getButton(
				IButtonConstPr.BTN_ASSIST_FUNC);
		m_btnDocument = getBtnTree().getButton(
				IButtonConstPr.BTN_ASSIST_FUNC_DOCUMENT);
		m_btnDocumentList = m_btnDocument;
		// 消息中心辅助按钮组
		m_btnOthersAuditCenter = getBtnTree().getButton(
				IButtonConstPr.BTN_ASSIST_FUNC);
		m_btnOthersAuditCenter.removeAllChildren();
		m_btnOthersAuditCenter.addChildButton(m_btnWorkFlowBrowse);
		m_btnOthersAuditCenter.addChildButton(m_btnDocument);
		m_btnOthersAuditCenter.addChildButton(m_btnLinkBillsBrowse);
		// 消息中心界面菜单
		m_btnsAuditCenter = new ButtonObject[] { m_btnAudit, m_btnUnAudit,
				m_btnOthersAuditCenter };

	}

	/**
	 * 获取按钮树，类唯一实例。
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * 使用示例
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @return <p>
	 * @author czp
	 * @time 2007-3-13 下午01:16:48
	 */
	private ButtonTree getBtnTree() {
		if (m_btnTree == null) {
			try {
				m_btnTree = new ButtonTree("40040101");
				// add by zip:2014/4/4 No 24
				m_btnTree.addMenu(btnCKKCXX);
				 //add end
			} catch (BusinessException be) {
				showHintMessage(be.getMessage());
				return null;
			}
		}
		return m_btnTree;
	}

	/**
	 * 卡片按钮显示前的特殊处理。
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * 使用示例
	 * <p>
	 * <b>参数说明</b>
	 * <p>
	 * 
	 * @author czp
	 * @time 2007-3-13 下午04:17:42
	 */
	private void dealBtnsBeforeCardShowing() {
		// 特殊功能
		m_btnList.setName(m_lanResTool.getStrByID("SCMCOMMON",
				"UPPSCMCommon-000464")/*
									 * @res "列表显示"
									 */);
		// 列表功能置灰
		m_btnSelectAll.setEnabled(false);
		m_btnSelectNo.setEnabled(false);
	}

	/**
	 * 作者：汪维敏 功能： 参数： 返回： 例外： 日期：(2004-9-8 10:18:55) 修改日期，修改人，修改原因，注释标志：
	 * 
	 */
	private void initButton() {
		// V51重构需要的匹配,按钮实例变量化
		createBtnInstances();

		// 加载业务类型按钮
		PfUtilClient.retBusinessBtn(m_btnTree
				.getButton(IButtonConstPr.BTN_BUSINESS_TYPE), m_sLoginCorpId,
				"20");

		// 业务类型按钮打钩处理
		PuTool.initBusiAddBtns(m_btnBusiTypes, m_btnAdds, "20", m_sLoginCorpId);
		if (m_btnBusiTypes != null && m_btnBusiTypes.getChildCount() > 0) {
			m_bizButton = m_btnBusiTypes.getChildButtonGroup()[0];
		}

		// 加载扩展按钮
		addExtendBtns();

		// 加载卡片按钮
		setButtons(m_btnTree.getButtonArray());

		// 按钮状态逻辑
		setButtonsCard();

		// 扩展按钮初始化
		setExtendBtnsStat(0);
		//
		m_nUIState = 0;
	}

	/**
	 * 作者：汪维敏 功能： 参数： 返回： 例外： 日期：(2004-9-8 10:18:55) 修改日期，修改人，修改原因，注释标志：
	 * 
	 */
	private void initComboBox(BillData bd) {
		// 请购类型
		bd.getHeadItem("ipraytype").setWithIndex(true);
		m_comPrayType = (UIComboBox) bd.getHeadItem("ipraytype").getComponent();
		m_comPrayType.setTranslate(true);
		m_comPrayType.addItem(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000454")/*
									 * @res "外包＿代料"
									 */);
		m_comPrayType.addItem(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000455")/*
									 * @res "外包＿不代料"
									 */);
		m_comPrayType.addItem(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000456")/*
									 * @res "采购"
									 */);
		m_comPrayType.addItem(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000457")/*
									 * @res "外协"
									 */);

		// 请购来源
		bd.getHeadItem("ipraysource").setWithIndex(true);
		m_comPraySource = (UIComboBox) bd.getHeadItem("ipraysource")
				.getComponent();
		m_comPraySource.setTranslate(true);
		m_comPraySource.addItem("MRP");
		m_comPraySource.addItem("MO");
		m_comPraySource.addItem("SCF");
		m_comPraySource.addItem(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000458")/*
									 * @res "销售订单"
									 */);
		m_comPraySource.addItem(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000459")/*
									 * @res "库存订货点"
									 */);
		m_comPraySource.addItem(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000460")/*
									 * @res "手工录入"
									 */);
		m_comPraySource.addItem("DRP");
		m_comPraySource.addItem(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000461")/*
									 * @res "调拨申请"
									 */);
		m_comPraySource.addItem(m_lanResTool.getStrByID("4004pub",
				"UPP4004pub-000204") /*
									 * @res "物资需求申请"
									 */);
	}

	/**
	 * 功能描述:初始化小数位
	 */
	private void initDecimal(BillData bd) {

		// 版本号:小数位显示一位
		bd.getHeadItem("nversion").setDecimalDigits(1);

		BillItem item1 = bd.getBodyItem("nsuggestprice");
		item1.setDecimalDigits(nPriceDecimal);

		BillItem item11 = bd.getBodyItem("nmaxprice");
		item11.setDecimalDigits(nPriceDecimal);

		BillItem item2 = bd.getBodyItem("npraynum");
		item2.setDecimalDigits(nMeasDecimal);

		BillItem item3 = bd.getBodyItem("nassistnum");
		item3.setDecimalDigits(nAssistUnitDecimal);

		BillItem item4 = bd.getBodyItem("nexchangerate");
		item4.setDecimalDigits(nExchangeDecimal);

		BillItem item5 = bd.getBodyItem("nmoney");
		item5.setDecimalDigits(nMoneyDecimal);

	}

	/**
	 * 函数的功能、用途、对属性的更改，以及函数执行前后对象的状态。
	 * 
	 * @param 参数说明
	 * @return 返回值
	 * @exception 异常描述
	 * @see 需要参见的其它内容
	 * @since 从类的那一个版本，此方法被添加进来。（可选）
	 * 
	 */
	private void initi() {

		// 初始化参数
		initPara();

		// 显示单据
		setLayout(new java.awt.BorderLayout());
		add(getBillCardPanel(), java.awt.BorderLayout.CENTER);
		getBillCardPanel().setEnabled(false);

		// ---------单据模板加载后的初始化－－－－－－－

		// 设置千分位
		getBillCardPanel().setBodyShowThMark(true);

		// 合计行显示
		getBillCardPanel().getBodyPanel().setTotalRowShow(true);

		// 加载行号
		BillRowNo.loadRowNoItem(getBillCardPanel(), "crowno");

		// 国际化
		nc.ui.pu.pub.PuTool.setTranslateRender(getBillCardPanel());

		// 处理自定义项
		nc.ui.scm.pub.def.DefSetTool.updateBillCardPanelUserDef(
				getBillCardPanel(), getClientEnvironment().getCorporation()
						.getPk_corp(), ScmConst.PO_Pray, // 单据类型
				"vdef", "vdef");

	}

	/**
	 * 作者：汪维敏 功能： 参数： 返回： 例外： 日期：(2004-9-8 10:18:55) 修改日期，修改人，修改原因，注释标志：
	 * 
	 */
	private void initListComboBox() {

		// 请购类型
		getBillListPanel().getBillListData().getHeadItem("ipraytype")
				.setWithIndex(true);
		m_comPrayType1 = (UIComboBox) getBillListPanel().getBillListData()
				.getHeadItem("ipraytype").getComponent();
		m_comPrayType1.setTranslate(true);
		m_comPrayType1.addItem(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000454")/*
									 * @res "外包＿代料"
									 */);
		m_comPrayType1.addItem(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000455")/*
									 * @res "外包＿不代料"
									 */);
		m_comPrayType1.addItem(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000456")/*
									 * @res "采购"
									 */);
		m_comPrayType1.addItem(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000457")/*
									 * @res "外协"
									 */);

		// 请购来源
		getBillListPanel().getBillListData().getHeadItem("ipraysource")
				.setWithIndex(true);
		m_comPraySource1 = (UIComboBox) getBillListPanel().getBillListData()
				.getHeadItem("ipraysource").getComponent();
		m_comPraySource1.setTranslate(true);
		m_comPraySource1.addItem("MRP");
		m_comPraySource1.addItem("MO");
		m_comPraySource1.addItem("SFC");
		m_comPraySource1.addItem(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000458")/*
									 * @res "销售订单"
									 */);
		m_comPraySource1.addItem(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000459")/*
									 * @res "库存订货点"
									 */);
		m_comPraySource1.addItem(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000460")/*
									 * @res "手工录入"
									 */);
		m_comPraySource1.addItem("DRP");
		m_comPraySource1.addItem(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000461")/*
									 * @res "调拨申请"
									 */);
		m_comPraySource1.addItem(m_lanResTool.getStrByID("4004pub",
				"UPP4004pub-000204") /*
									 * @res "物资需求申请"
									 */);
	}

	/**
	 * 功能描述:初始化小数位
	 */
	private BillListData initListDecimal(BillListData bd) {

		// 版本号:小数位显示一位
		bd.getHeadItem("nversion").setDecimalDigits(1);

		BillItem item1 = bd.getBodyItem("nsuggestprice");
		item1.setDecimalDigits(nPriceDecimal);

		BillItem item11 = bd.getBodyItem("nmaxprice");
		item11.setDecimalDigits(nPriceDecimal);

		BillItem item2 = bd.getBodyItem("npraynum");
		item2.setDecimalDigits(nMeasDecimal);

		BillItem item3 = bd.getBodyItem("nassistnum");
		item3.setDecimalDigits(nAssistUnitDecimal);

		BillItem item4 = bd.getBodyItem("nexchangerate");
		item4.setDecimalDigits(nExchangeDecimal);

		BillItem item5 = bd.getBodyItem("nmoney");
		item5.setDecimalDigits(nMoneyDecimal);

		// 版本号:小数位显示一位
		bd.getHeadItem("nversion").setDecimalDigits(1);

		return bd;
	}

	/**
	 * 功能描述:初始化
	 */
	public void initListener() {

		// 表体排序监听
		getBillCardPanel().getBodyPanel().addTableSortListener();
		getBillCardPanel().getBillModel().setRowSort(true);
		// 增加行号排序监听
		getBillCardPanel().getBillModel().setSortPrepareListener(this);
		// 增加仓库监听
		((UIRefPane) getBillCardPanel().getBodyItem("cwarehousename")
				.getComponent()).getUIButton().addActionListener(this);

		// 增加单据编辑监听
		getBillCardPanel().addEditListener(this);
		getBillCardPanel().addBodyMenuListener(this);

		// 自由项监听
		getBillCardPanel().addBodyEditListener2(this);
		// 自由项
		((UIRefPane) getBillCardPanel().getBodyItem("vfree").getComponent())
				.getUIButton().addActionListener(this);
		// 表头编辑前事件监听
		getBillCardPanel().setBillBeforeEditListenerHeadTail(this);
		// 排序监听
		getBillCardPanel().getBillModel().addSortListener(this);
		// 卡片表体排序监听
		getBillCardPanel().getBillModel().addSortRelaObjectListener2(
				new IBillRelaSortListener2Body());
		
		//add by zip 2013/11/27 No 5
		String pk_corp = ClientEnvironment.getInstance().getCorporation().getPk_corp();
		String usercode = ClientEnvironment.getInstance().getUser().getUserCode();
		/**
		 * edit by yhj 2014-03-20
		 * 开放yhj账号采购请购单的保存按钮权限
		 */
		//上海宝翼1016
		if ("1016".equals(pk_corp) && (!"zip".equals(usercode) && !"yhj".equals(usercode) && !"db".equals(usercode)&& !"dzy".equals(usercode))) {
			m_btnSave.setVisible(false);
		}
		//河南1097
		if ("1097".equals(pk_corp) && (!"zip".equals(usercode) && !"yhj".equals(usercode) && !"db".equals(usercode)&& !"dzy".equals(usercode))) {
			m_btnSave.setVisible(false);
		}
		//武汉1071
		if ("1071".equals(pk_corp) && (!"zip".equals(usercode) && !"yhj".equals(usercode) && !"db".equals(usercode)&& !"dzy".equals(usercode))) {
			m_btnSave.setVisible(false);
		}
		//佛山1019
		if ("1019".equals(pk_corp) && (!"zip".equals(usercode) && !"yhj".equals(usercode) && !"db".equals(usercode)&& !"dzy".equals(usercode))) {
			m_btnSave.setVisible(false);
		}
		//成都1018
		if ("1018".equals(pk_corp) && (!"zip".equals(usercode) && !"yhj".equals(usercode) && !"db".equals(usercode)&& !"dzy".equals(usercode))) {
			m_btnSave.setVisible(false);
		}
		//河北 1017
		if ("1017".equals(pk_corp) && (!"zip".equals(usercode) && !"yhj".equals(usercode) && !"db".equals(usercode)&& !"dzy".equals(usercode))) {
			m_btnSave.setVisible(false);
		}
		//河南1097 武汉1071 佛山1019 成都1018 河北 1017
		// end
		// 刷卡监听
		readCard();
	}

	/**
	 * 功能描述:初始化参数
	 */
	private void initPara() {
		try {
			// 初始化精度（数量、单价）
			// int[] iDigits = nc.ui.pu.pub.PuTool.getDigitBatch(m_sUnitCode,
			// new String[] { "BD502", "BD503", "BD501", "BD505" });
			// if (iDigits != null && iDigits.length == 4) {
			// nAssistUnitDecimal = iDigits[0];
			// nExchangeDecimal = iDigits[1];
			// nMeasDecimal = iDigits[2];
			// nPriceDecimal = iDigits[3];
			// }
			// 本币金额精度
			// nMoneyDecimal =
			// nc.ui.rc.pub.CPurchseMethods.getCurrDecimal(getCorpId())[0];

			// 库存是否启用
			// m_bICStartUp = nc.ui.sm.user.UserPowerUI.isEnabled(m_sUnitCode,
			// "IC");

			ServcallVO[] scDisc = new ServcallVO[2];
			// 初始化精度（数量、单价）
			scDisc[0] = new ServcallVO();
			scDisc[0].setBeanName("nc.itf.pu.pub.IPub");
			scDisc[0].setMethodName("getDigitBatch");
			scDisc[0].setParameter(new Object[] { m_sLoginCorpId,
					new String[] { "BD502", "BD503", "BD501", "BD505" } });
			scDisc[0].setParameterTypes(new Class[] { String.class,
					String[].class });

			scDisc[1] = new ServcallVO();
			scDisc[1].setBeanName("nc.itf.rc.receive.IArriveorder");
			scDisc[1].setMethodName("getCurrDecimal");
			scDisc[1].setParameter(new Object[] { m_sLoginCorpId });
			scDisc[1].setParameterTypes(new Class[] { String.class });

			// //自定义项远程调用描述类
			// ServcallVO[] scdsDef =
			// nc.ui.scm.pub.def.DefSetTool.getTwoSCDs(m_sUnitCode);
			// scDisc[3] = scdsDef[0];
			// scDisc[4] = scdsDef[1];

			// 后台一次调用
			Object[] oParaValue = nc.ui.scm.service.LocalCallService
					.callService(scDisc);
			if (oParaValue != null && oParaValue.length == scDisc.length) {
				// 数量、单据精度
				int[] iDigits = (int[]) oParaValue[0];
				if (iDigits != null && iDigits.length == 4) {
					nAssistUnitDecimal = iDigits[0];
					nExchangeDecimal = iDigits[1];
					nMeasDecimal = iDigits[2];
					nPriceDecimal = iDigits[3];
				}
				// 本币金额精度
				nMoneyDecimal = ((Integer) oParaValue[1]).intValue();

				// 库存是否启用
				// m_bICStartUp = ((Boolean) oParaValue[2]).booleanValue();

				// 自定义项预处理
				// nc.ui.scm.pub.def.DefSetTool.setTwoOBJs(new Object[] {
				// oParaValue[3], oParaValue[4] });

				// 保存是否允许修改删除别人的单据的参数
				String sRet = SysInitBO_Client.getParaString(m_sLoginCorpId,
						"PO060");

				isAllowedModifyByOther = (sRet == null || sRet.equals("N")) ? false
						: true;
			}
		} catch (Exception e) {
			reportException(e);
		}

		/** IC模块启用判断采用下面的接口方式 */
		ICreateCorpQueryService tt = (ICreateCorpQueryService) NCLocator
				.getInstance().lookup(ICreateCorpQueryService.class.getName());
		boolean bEnable = false;
		try {
			tt.isEnabled("1001", "IC");
		} catch (Exception e) {
			Logger.debug(e.getMessage());
		}
		Logger.debug(bEnable + "");

	}

	/**
	 * 作者：汪维敏 功能： 参数： 返回： 例外： 日期：(2004-9-8 10:18:55) 修改日期，修改人，修改原因，注释标志：
	 * 
	 */
	private void initRefpane(BillData bd) {
		UIRefPane refpane = null;

		// －－－－－－－－－表头－－－－－－－－－

		// 业务员(采购部门的)
		refpane = (UIRefPane) bd.getBodyItem("cemployeename").getComponent();
		refpane.setRefModel(new PurPsnRefModel(m_sLoginCorpId, bd.getHeadItem(
				"cdeptid").getValue()));

		// 项目
		refpane = (UIRefPane) bd.getBodyItem("cprojectname").getComponent();
		refpane
				.setWhereString(" bd_jobmngfil.pk_corp = '"
						+ m_sLoginCorpId
						+ "' and bd_jobmngfil.pk_jobbasfil = bd_jobbasfil.pk_jobbasfil and upper(isnull(bd_jobmngfil.sealflag,'N')) = 'N'");
		refpane.setCacheEnabled(false);

		// 备注
		refpane = (UIRefPane) bd.getHeadItem("vmemo").getComponent();
		refpane.setReturnCode(false);
		refpane.setAutoCheck(false);

		// －－－－－－－－－表体－－－－－－－－－

		// 存货编码
		refpane = (UIRefPane) bd.getBodyItem("cinventorycode").getComponent();
		String sWhere = " upper(ISNULL(discountflag,'N')) = 'N' and upper(ISNULL(bd_invmandoc.sealflag,'N')) = 'N' and bd_invmandoc.pk_corp = '"
				+ m_sLoginCorpId
				+ "' and bd_invmandoc.pk_invbasdoc = bd_invbasdoc.pk_invbasdoc";

		// 是否可采购
		sWhere += " AND UPPER(ISNULL(bd_invmandoc.iscanpurchased,'Y')) = 'Y'";
		refpane.setWhereString(sWhere);
		refpane.setTreeGridNodeMultiSelected(true);
		refpane.setMultiSelectedEnabled(true);
		refpane.setCacheEnabled(false);
		invrefpane = refpane;

		// 存货名称
		refpane = (UIRefPane) bd.getBodyItem("cvendorname").getComponent();
		sWhere = " bd_cumandoc.pk_corp='"
				+ m_sLoginCorpId
				+ "' and (bd_cumandoc.custflag='1' or bd_cumandoc.custflag='3') and bd_cumandoc.pk_cubasdoc = bd_cubasdoc.pk_cubasdoc and upper(frozenflag) = 'N'";
		refpane.setWhereString(sWhere);
		refpane.setCacheEnabled(false);

		// 辅计量数量
		refpane = (UIRefPane) bd.getBodyItem("nassistnum").getComponent();
		UITextField nAssistNumUI = refpane.getUITextField();
		nAssistNumUI.setDelStr("-");

		// 辅计量参照
		refpane = ((UIRefPane) (bd.getBodyItem("cassistunitname")
				.getComponent()));
		refpane.setIsCustomDefined(true);
		refpane.setRefModel(new OtherRefModel("辅计量单位"));
		refpane.setReturnCode(false);
		refpane.setRefInputType(1);
		refpane.setCacheEnabled(false);

		// 换算率
		refpane = (UIRefPane) bd.getBodyItem("nexchangerate").getComponent();
		UITextField nExchangeRateUI = refpane.getUITextField();
		nExchangeRateUI.setDelStr("-");

		// 批次号参照
		if (bd.getBodyItem("vproducenum").isShow()) {
			LotNumbRefPane lotRef = new LotNumbRefPane();
			lotRef.setMaxLength(bd.getBodyItem("vproducenum").getLength());
			lotRef.setIsCustomDefined(true);
			lotRef.setMaxLength(bd.getBodyItem("vproducenum").getLength());
			bd.getBodyItem("vproducenum").setComponent((JComponent) lotRef);
		}
		// 自由项参照
		m_freeItem = new FreeItemRefPane();
		m_freeItem.setMaxLength(bd.getBodyItem("vfree").getLength());
		bd.getBodyItem("vfree").setComponent(m_freeItem);

		// 建议价格
		refpane = (UIRefPane) bd.getBodyItem("nsuggestprice").getComponent();
		UITextField nSuggestPriceUI = refpane.getUITextField();
		nSuggestPriceUI.setMaxLength(16);
		nSuggestPriceUI.setDelStr("-");

		// 请购数量
		refpane = (UIRefPane) bd.getBodyItem("npraynum").getComponent();
		UITextField nPrayNumUI = refpane.getUITextField();
		nPrayNumUI.setMaxLength(20);// eric
		nPrayNumUI.setDelStr("-");

		// 金额
		refpane = (UIRefPane) bd.getBodyItem("nmoney").getComponent();
		UITextField nMoneyUI = refpane.getUITextField();
		nMoneyUI.setMaxLength(16);
		nMoneyUI.setDelStr("-");

		// 仓库名
		refpane = (UIRefPane) bd.getBodyItem("cwarehousename").getComponent();
		refpane.getRefModel().addWherePart(" and upper(gubflag) = 'N' ");
		refpane.setCacheEnabled(false);

		// 表体备注处理方法不同于表头
		refpane = (UIRefPane) bd.getBodyItem("vmemo").getComponent();
		// refpane.setTable(bd.getBillTable());
		refpane.getRefModel().setRefCodeField(
				refpane.getRefModel().getRefNameField());
		refpane.getRefModel().setBlurFields(
				new String[] { refpane.getRefModel().getRefNameField() });
		refpane.setAutoCheck(false);

	}

	/**
	 * 是否可弃审
	 */
	private boolean isCanAudit(PraybillVO vo) {
		if (vo == null)
			return false;
		if (vo.getParentVO() == null)
			return false;
		Integer status = null;
		status = ((PraybillHeaderVO) vo.getParentVO()).getIbillstatus();
		if (status == null)
			return false;
		if (status.intValue() == 0)
			return true;
		return false;
	}

	/**
	 * 是否可关闭
	 */
	private boolean isCanClose(PraybillVO vo) {
		if (vo == null)
			return false;
		if (vo.getParentVO() == null)
			return false;
		Integer status = null;
		status = ((PraybillHeaderVO) vo.getParentVO()).getIbillstatus();
		if (status == null)
			return false;
		if (status.intValue() == 3)
			return true;
		return false;
	}

	/**
	 * 是否可打开
	 */
	private boolean isCanOpen(PraybillVO vo) {
		if (vo == null)
			return false;
		if (vo.getParentVO() == null)
			return false;
		Integer status = null;
		status = ((PraybillHeaderVO) vo.getParentVO()).getIbillstatus();
		if (status == null)
			return false;
		// if (status.intValue() == 3 && status.intValue() == 1)
		if (status.intValue() == 1)
			return true;
		return false;
	}

	/**
	 * 是否可弃审
	 */
	private boolean isCanUnAudit(PraybillVO vo) {
		if (vo == null)
			return false;
		if (vo.getParentVO() == null)
			return false;
		Integer status = null;
		PraybillItemVO[] items = (PraybillItemVO[]) vo.getChildrenVO();
		//
		if (items == null || items.length <= 0)
			return false;
		/* 有订单生成,不能弃审 */
		UFDouble ufdNaccumulatenum = null;
		for (int i = 0; i < items.length; i++) {
			ufdNaccumulatenum = nc.vo.scm.pu.PuPubVO
					.getUFDouble_NullAsZero(items[i].getNaccumulatenum());
			if (!ufdNaccumulatenum.equals(nc.vo.scm.pu.VariableConst.ZERO))
				return false;
		}
		status = ((PraybillHeaderVO) vo.getParentVO()).getIbillstatus();
		if (status == null)
			return false;
		if (status.intValue() == 3)
			return true;
		return false;
	}

	/**
	 * 功能描述:鼠标双击事件响应
	 */
	public void mouse_doubleclick(BillMouseEnent event) {
		if (event.getPos() == BillItem.HEAD) {
			/* 如果没有单据体，则认为并发并直接返回 */
			PraybillItemVO[] items = (PraybillItemVO[]) getBillListPanel()
					.getBodyBillModel().getBodyValueVOs(
							PraybillItemVO.class.getName());
			if (items == null || items.length <= 0)
				return;
			/* 支持排序 */
			m_nPresentRecord = event.getRow();
			m_nPresentRecord = nc.ui.pu.pub.PuTool.getIndexBeforeSort(
					getBillListPanel(), m_nPresentRecord);
			if (m_nPresentRecord >= 0) {
				/* 加载单据卡片控件 */
				getBillListPanel().setVisible(false);
				getBillCardPanel().setVisible(true);
				m_nUIState = 0;
				/* 设置单据VO到卡片 */
				setVoToBillCard(m_nPresentRecord, null);
				/* 设置按钮逻辑 */
				setButtonsCard();
			}
		}
	}

	/**
	 * 功能描述:增加
	 */
	private void onAppend() {
		showHintMessage(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000050")/* @res"新增单据..." */);
		m_bAdd = true;
		m_bEdit = true;

		getBillCardPanel().addNew();
		getBillCardPanel().setEnabled(true);
		setPartNoEditable(getBillCardPanel());
		UIRefPane nRefPanel1 = (UIRefPane) getBillCardPanel().getHeadItem(
				"dpraydate").getComponent();
		nRefPanel1.setValue(getClientEnvironment().getDate().toString());

		UIRefPane nRefPanel2 = (UIRefPane) getBillCardPanel().getHeadItem(
				"cbiztype").getComponent();
		nRefPanel2.setPK(m_bizButton.getTag());
		nRefPanel2.setEnabled(false);

		UIRefPane nRefPanel3 = (UIRefPane) getBillCardPanel().getTailItem(
				"cauditpsn").getComponent();
		nRefPanel3.setEnabled(false);

		// 设置操作员
		String strUserId = getClientEnvironment().getUser().getPrimaryKey();
		UIRefPane nRefPanel6 = (UIRefPane) getBillCardPanel().getTailItem(
				"coperator").getComponent();
		nRefPanel6.setPK(strUserId);
		nRefPanel6.setEnabled(false);

		// 取操作员对应业务员，设置请购人(请购人无值时才设置)
		if (PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getHeadItem(
				"cpraypsn").getValueObject()) == null) {
			IUserManageQuery iSrvUser = (IUserManageQuery) NCLocator
					.getInstance().lookup(IUserManageQuery.class.getName());
			PsndocVO voPsnDoc = null;
			try {
				voPsnDoc = iSrvUser.getPsndocByUserid(getCorpPrimaryKey(),
						strUserId);
			} catch (BusinessException be) {
				SCMEnv.out(be);
			}
			if (voPsnDoc != null) {
				UIRefPane refPanePrayPsn = (UIRefPane) getBillCardPanel()
						.getHeadItem("cpraypsn").getComponent();
				refPanePrayPsn.setPK(voPsnDoc.getPrimaryKey());
				// 由请购人带出请购部门(如果请购部门无值时才带出)
				if (PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel()
						.getHeadItem("cdeptid").getValueObject()) == null) {
					afterEditWhenHeadPsn(getBillCardPanel(), null, null);
				}
			}
		}
		//
		btnCKKCXX.setEnabled(true);
		m_comPraySource.setSelectedIndex(5);
		m_comPraySource.setEnabled(false);
		m_comPrayType.setSelectedIndex(2);
		m_bCancel = false;
		setButtonsCardModify();
		m_nUIState = 0;
		updateButtonsAll();

		/* 设置右键菜单与按钮组“行操作”权限相同 */
		setPopMenuItemsEnable();

		// 易用性修改
		onAppendLine(getBillCardPanel(), this);

		// 置光标到表头第一个可编辑项目
		getBillCardPanel().transferFocusTo(BillCardPanel.HEAD);
		//
		showHintMessage(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000533")/* @res "增加、编辑单据" */);
	}

	/**
	 * 功能描述:增行
	 */
	private static void onAppendLine(BillCardPanel bcp, ToftPanel uiPanel) {
		uiPanel.showHintMessage("");
		bcp.addLine();
		bcp.setEnabled(true);
		/* 处理项目自动协带 */
		nc.ui.pu.pub.PuTool.setBodyProjectByHeadProject(bcp, "cprojectidhead",
				"cprojectid", "cprojectname",
				nc.vo.scm.pu.PuBillLineOprType.ADD);
		/* 处理行号 */
		BillRowNo.addLineRowNo(bcp, BillTypeConst.PO_PRAY, "crowno");
		// 增行操作，自动带入当前登录公司
		bcp.setBodyValueAt(PoPublicUIClass.getLoginPk_corp(),
				bcp.getRowCount() - 1, "pk_reqcorp");
		bcp.execBodyFormula(bcp.getRowCount() - 1, "reqcorpname");
		bcp.setBodyValueAt(PoPublicUIClass.getLoginPk_corp(),
				bcp.getRowCount() - 1, "pk_purcorp");
		bcp.execBodyFormula(bcp.getRowCount() - 1, "purcorpname");

		setPartNoEditable(bcp);
		//
		uiPanel.showHintMessage(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000535")/* @res "表体新增一行" */);
	}

	/**
	 * 获取审批后的最新VO
	 * 
	 * @param voAudit
	 *            待更新的VO
	 * @return
	 * @since v50
	 * @author czp
	 */
	private Integer setLastestInfosToVoAfterAuditted(PraybillVO voAudit)
			throws Exception {

		/* 审批成功后刷新 */
		ArrayList arrRet = PraybillHelper.queryPrayForSaveAudit(voAudit
				.getParentVO().getPrimaryKey());
		((PraybillHeaderVO) voAudit.getParentVO())
				.setDauditdate((UFDate) arrRet.get(0));
		((PraybillHeaderVO) voAudit.getParentVO()).setCauditpsn((String) arrRet
				.get(1));
		((PraybillHeaderVO) voAudit.getParentVO())
				.setIbillstatus((Integer) arrRet.get(2));
		((PraybillHeaderVO) voAudit.getParentVO())
				.setTs((String) arrRet.get(3));
		((PraybillHeaderVO) voAudit.getParentVO()).setTmaketime((String) arrRet
				.get(4));
		((PraybillHeaderVO) voAudit.getParentVO())
				.setTlastmaketime((String) arrRet.get(5));
		((PraybillHeaderVO) voAudit.getParentVO())
				.setTaudittime((String) arrRet.get(6));
		// 支持审批修改数量，同步表体时间戳
		ArrayList ts = (ArrayList) arrRet.get(7);
		HashMap<String, String> mapBidTs = new HashMap<String, String>();
		if (ts != null && ts.size() > 0) {
			ArrayList<String> listBidTs = null;
			for (int i = 0; i < ts.size(); i++) {
				listBidTs = (ArrayList) ts.get(i);
				if (listBidTs == null || listBidTs.size() < 2) {
					continue;
				}
				mapBidTs.put(listBidTs.get(1), listBidTs.get(0));
			}
		}
		PraybillItemVO[] items = voAudit.getBodyVO();
		int itemsLength = items.length;
		for (int i = 0; i < itemsLength; i++) {
			items[i].setTs((String) mapBidTs.get(items[i].getCpraybill_bid()));
		}
		voAudit.setChildrenVO(items);

		Integer iBillStatus = (Integer) arrRet.get(2);

		return iBillStatus;
	}

	/**
	 * 创建日期： 2005-9-28 功能描述： 获得版本号大于1的单据号 调用时机： 弃审
	 */
	private String getVersionOfCantUnaudit(PraybillVO[] vos) {
		String versions = "";

		for (int i = 0, len = vos.length; i < len; i++) {
			if (vos[i].getHeadVO().getNversion().intValue() > 1)
				versions += vos[i].getHeadVO().getVpraycode() + ", ";
		}

		return versions;
	}

	/**
	 * 功能描述:取消审批
	 */
	protected void onUnAuditList() {
		int iCnt = 0;
		String Cpraybillid = null;
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040102",
				"UPP40040102-000018")/* @res "正在弃审单据..." */);
		Integer nSelected[] = null;
		Vector v = new Vector();
		Vector vv = new Vector();
		int nRow = getBillListPanel().getHeadBillModel().getRowCount();
		for (int i = 0; i < nRow; i++) {
			int nStatus = getBillListPanel().getHeadBillModel().getRowState(i);
			if (nStatus == BillModel.SELECTED)
				v.addElement(new Integer(i));
			else
				vv.addElement(new Integer(i));
		}
		nSelected = new Integer[v.size()];
		v.copyInto(nSelected);
		if (nSelected == null || nSelected.length == 0) {
			MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("40040102", "UPP40040102-000019")/*
																 * @res
																 * "请购单取消审批"
																 */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40040102",
							"UPP40040102-000003")/* @res "未选中请购单！" */);
			return;
		}
		long tTime = System.currentTimeMillis();
		Vector vTemp = new Vector();
		for (int i = 0; i < nSelected.length; i++) {
			// 排序处理
			PraybillVO vo = m_VOs[nSelected[i].intValue()];
			vo.getHeadVO().setCuserid(
					getClientEnvironment().getUser().getPrimaryKey());
			vo.getHeadVO().setCauditpsn(
					getClientEnvironment().getUser().getPrimaryKey());

			

			vTemp.addElement(vo);
		}
		PraybillVO VOs[] = new PraybillVO[vTemp.size()];
		vTemp.copyInto(VOs);

		// -----------------------v31sp added zhongwei 版本号对弃审的限制
		String versions = getVersionOfCantUnaudit(VOs);
		if (versions != null && versions.trim().length() > 0) {
			showErrorMessage(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
					.getStrByID(
							"40040101",
							"UPP40040101-000527",
							null,
							new String[] { versions.substring(0, versions
									.length() - 2) })/*
													 * @res "修订后请购单不能弃审!\n
													 * 单据号：{0}"
													 */);
			return;
		}
		// -----------------------v31sp added zhongwei 版本号对弃审的限制

		try {
			// 给所有请购单赋操作员
			
			String strOpr = getClientEnvironment().getUser().getPrimaryKey();
			for (int i = 0; i < VOs.length; i++) {
				VOs[i].setCurrOperator(strOpr);
				VOs[i].getHeadVO().setCuserid(strOpr);
				VOs[i].getHeadVO().setTaudittime(null);
				Cpraybillid=VOs[i].getHeadVO().getCpraybillid();
			}
			// 操作真正发生时才加载表体

			VOs = PrTool.getRefreshedVOs(VOs);
			PfUtilClient.processBatch(this, "UNAPPROVE", "20",
					getClientEnvironment().getDate().toString(), VOs, null);
			if (!PfUtilClient.isSuccess()) {
				// 审批失败
				return;
			}
			iCnt = VOs.length;
			
		} catch (java.sql.SQLException e) {
			MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("40040102", "UPP40040102-000020")/*
																 * @res
																 * "取消请购单审批"
																 */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
							"UPPSCMCommon-000412")/* @res "SQL语句错误！" */);
			SCMEnv.out(e);
			return;
		} catch (ArrayIndexOutOfBoundsException e) {
			MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("40040102", "UPP40040102-000020")/*
																 * @res
																 * "取消请购单审批"
																 */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
							"UPPSCMCommon-000426")/* @res "数组越界错误！" */);
			SCMEnv.out(e);
			return;
		} catch (NullPointerException e) {
			MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("40040102", "UPP40040102-000020")/*
																 * @res
																 * "取消请购单审批"
																 */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
							"UPPSCMCommon-000427")/* @res "空指针错误！" */);
			SCMEnv.out(e);
			return;
		} catch (nc.vo.pub.BusinessException e) {
			MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("40040102", "UPP40040102-000020")/*
																 * @res
																 * "取消请购单审批"
																 */, e
					.getMessage());
			SCMEnv.out(e);
			return;
		} catch (Exception e) {
			MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("40040102", "UPP40040102-000020")/*
																 * @res
																 * "取消请购单审批"
																 */, e
					.getMessage());
			SCMEnv.out(e);
			return;
		}
		tTime = System.currentTimeMillis() - tTime;
		SCMEnv.out("取消审批时间：" + tTime + " ms!");
		//lcq add by 2015/6/19  移除列表备注
		String sql="UPDATE po_praybill SET Vmemo=' ' WHERE cpraybillid='"+Cpraybillid+"'";
		IPubDMO getdmo = (IPubDMO) NCLocator.getInstance().lookup(IPubDMO.class.getName());
		try {
			getdmo.executeUpdate(sql);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//lcq end by   移除列表备注
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
				"UCH011")/* @res "弃审成功" */);
		//
		onListRefresh();
	}

	/**
	 * 功能描述:审批--列表
	 */
	public void onAuditList() {
		int iCnt = 0;
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
				"SCMCOMMON", "UPPSCMCommon-000403")/* @res "正在审批单据..." */);
		Integer nSelected[] = null;
		Vector v = new Vector();
		Vector vv = new Vector();
		int nRow = getBillListPanel().getHeadBillModel().getRowCount();
		for (int i = 0; i < nRow; i++) {
			int nStatus = getBillListPanel().getHeadBillModel().getRowState(i);
			if (nStatus == BillModel.SELECTED)
				v.addElement(new Integer(i));
			else
				vv.addElement(new Integer(i));
		}
		nSelected = new Integer[v.size()];
		v.copyInto(nSelected);

		if (nSelected == null || nSelected.length == 0) {
			MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40040102",
							"UPP40040102-000002")/* @res "请购单审批" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40040102",
							"UPP40040102-000003")/* @res "未选中请购单！" */);
			return;
		}
		long tTime = System.currentTimeMillis();
		Vector vTemp = new Vector();
		for (int i = 0; i < nSelected.length; i++) {
			// 排序处理
			PraybillVO vo = m_VOs[nSelected[i].intValue()];
			vo.getHeadVO().setCuserid(
					getClientEnvironment().getUser().getPrimaryKey());
			vTemp.addElement(vo);
		}

		PraybillVO VOs[] = new PraybillVO[vTemp.size()];
		vTemp.copyInto(VOs);

		/*
		 * 修改：zhongwei v31-sp1-天音 审批日期不能小于请购日期
		 */
		Vector v_ids = new Vector();
		for (int i = 0; i < VOs.length; i++) {
			PraybillHeaderVO headVO = VOs[i].getHeadVO();

			if (headVO.getDpraydate().toString().compareTo(
					getClientEnvironment().getDate().toString()) > 0)
				// 保存不符合要求的请购单编号
				v_ids.add(headVO.getVpraycode());

			headVO.setCauditpsn(getClientEnvironment().getUser()
					.getPrimaryKey());
			headVO.setDauditdate(getClientEnvironment().getDate());
		}

		/*
		 * 显示错误信息并返回
		 */
		if (v_ids.size() > 0) {
			String message = "";
			for (int i = 0, len = v_ids.size(); i < len; i++)
				message += v_ids.get(i).toString() + ", ";

			showErrorMessage(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
					.getStrByID(
							"4004pub",
							"UPP4004pub-000199",
							null,
							new String[] {
									nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
											.getStrByID("common",
													"UC000-0003665"),
									message.substring(0, message.length() - 2),
									nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
											.getStrByID("common",
													"UC000-0003665") })/*
																		 * @res
																		 * "单据审批日期不能小于{0}!\n单据号：{1}。\n可重新登录后审批(要求：登录的业务日期大于等于{2}) "
																		 */);
			return;
		}

		try {
			// 给所有请购单赋操作员
			String strOpr = getClientEnvironment().getUser().getPrimaryKey();
			for (int i = 0; i < VOs.length; i++) {
				VOs[i].setCurrOperator(strOpr);
				VOs[i].getHeadVO().setCuserid(strOpr);
				VOs[i].getHeadVO().setTaudittime(
						(new UFDateTime(new Date())).toString());
			}
			// 操作真正发生时才加载表体
			VOs = PrTool.getRefreshedVOs(VOs);
			PfUtilClient.processBatchFlow(this, "APPROVE", "20",
					getClientEnvironment().getDate().toString(), VOs, null);
			if (!PfUtilClient.isSuccess()) {
				// 不审批请购单
				return;
			}
			iCnt = VOs.length;
		} catch (java.sql.SQLException e) {
			MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40040102",
							"UPP40040102-000002")/* @res "请购单审批" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
							"UPPSCMCommon-000412")/* @res "SQL语句错误！" */);
			SCMEnv.out(e);
			return;
		} catch (ArrayIndexOutOfBoundsException e) {
			MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40040102",
							"UPP40040102-000002")/* @res "请购单审批" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
							"UPPSCMCommon-000426")/* @res "数组越界错误！" */);
			SCMEnv.out(e);
			return;
		} catch (NullPointerException e) {
			MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40040102",
							"UPP40040102-000002")/* @res "请购单审批" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
							"UPPSCMCommon-000427")/* @res "空指针错误！" */);
			SCMEnv.out(e);
			return;
		} catch (nc.vo.pub.BusinessException e) {
			MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40040102",
							"UPP40040102-000002")/* @res "请购单审批" */, e
							.getMessage());
			SCMEnv.out(e);
			return;
		} catch (Exception e) {
			MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40040102",
							"UPP40040102-000002")/* @res "请购单审批" */, e
							.getMessage());
			SCMEnv.out(e);
			return;
		}
		tTime = System.currentTimeMillis() - tTime;
		SCMEnv.out("请购单审批时间：" + tTime + " ms!");

		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
				"SCMCOMMON", "UPPSCMCommon-000236")/* @res "审核成功" */);

		onListRefresh();
	}

	/**
	 * 功能:执行审批--卡片
	 */
	private void onAudit() {
		PraybillVO voAudit = null;
		boolean isCycle = true;
		while (isCycle) {
			isCycle = false;
			try {
				PraybillVO voCloned = null;
				if (isCanAutoAddLine) {
					voAudit = m_VOs[m_nPresentRecord];
					voCloned = (PraybillVO) m_VOs[m_nPresentRecord].clone();
					PraybillItemVO[] items = voAudit.getBodyVO();
					int itemsLength = items.length;
					for (int i = 0; i < itemsLength; i++) {

						String cmangid = items[i].getCmangid();
						String cbaseid = items[i].getCbaseid();
						if (cbaseid != null
								&& cbaseid.equals('"' + cbaseid.substring(1,
										cbaseid.length() - 1) + '"')) {
							items[i].setCbaseid(cbaseid.substring(1, cbaseid
									.length() - 1));
						}
						if (cmangid != null
								&& cbaseid.equals('"' + cbaseid.substring(1,
										cbaseid.length() - 1) + '"')) {
							items[i].setCmangid(cmangid.substring(1, cmangid
									.length() - 1));
						}
					}
				} else {
					voCloned = m_VOs[m_nPresentRecord];
					voAudit = (PraybillVO) getBillCardPanel().getBillValueVO(
							PraybillVO.class.getName(),
							PraybillHeaderVO.class.getName(),
							PraybillItemVO.class.getName());
				}
				/*
				 * 修改：zhongwei v31-sp1-天音 审批日期不能小于制单日期(请购日期)
				 */
				if (voAudit.getHeadVO().getDpraydate().toString().compareTo(
						getClientEnvironment().getDate().toString()) > 0) {
					throw new Exception(nc.vo.ml.NCLangRes4VoTransl
							.getNCLangRes().getStrByID(
									"4004pub",
									"UPP4004pub-000199",
									null,
									new String[] {
											nc.vo.ml.NCLangRes4VoTransl
													.getNCLangRes().getStrByID(
															"common",
															"UC000-0003665"),
											voAudit.getHeadVO().getVpraycode(),
											nc.vo.ml.NCLangRes4VoTransl
													.getNCLangRes().getStrByID(
															"common",
															"UC000-0003665") })/* "单据审批日期不能小于{0}!\n单据号：{1}。\n可重新登录后审批(要求：登录的业务日期大于等于{2}) " */);
				}

				showHintMessage(m_lanResTool.getStrByID("40040101",
						"UPP40040101-000051")/* "正在审批..." */);
				nc.vo.scm.pu.Timer timer = new nc.vo.scm.pu.Timer();
				timer.start("请购单审批操作onAudit（）开始");

				voAudit.getHeadVO().setCauditpsn(
						getClientEnvironment().getUser().getPrimaryKey());
				voAudit.getHeadVO().setDauditdate(
						getClientEnvironment().getDate());
				voAudit.setCurrOperator(getClientEnvironment().getUser()
						.getPrimaryKey());
				voAudit.getHeadVO().setCuserid(
						getClientEnvironment().getUser().getPrimaryKey());
				voAudit.getHeadVO().setTaudittime(
						(new UFDateTime(new Date())).toString());
				if (voAudit.getBodyVO() != null
						&& voAudit.getBodyVO().length > 0) {
					for (int i = 0; i < voAudit.getBodyVO().length; i++) {
						PraybillItemVO item = voAudit.getBodyVO()[i];
						int status = item.getStatus();
						if (status == 2) {
							voAudit.getBodyVO()[i].setStatus(0);
						}
					}
				}
				timer.addExecutePhase("审批前准备操作");
				/* 审批 */
				PraybillVO[] oaUserObj = new PraybillVO[] { voCloned };
				PfUtilClient.processBatchFlow(null, "APPROVE",
						nc.vo.scm.pu.BillTypeConst.PO_PRAY,
						getClientEnvironment().getDate().toString(),
						new PraybillVO[] { voAudit }, oaUserObj);
				if (!PfUtilClient.isSuccess()) {
					showHintMessage(m_lanResTool.getStrByID("40040101",
							"UPP40040101-000052")/* "审批未成功" */);
					return;
				}
				timer.addExecutePhase("执行APPROVE脚本操作");

				// 审批成功后刷新
				Integer iBillStatus = setLastestInfosToVoAfterAuditted(voAudit);
				timer.addExecutePhase("审批成功后刷新");
				m_VOs[m_nPresentRecord] = voAudit;

				/* 加载单据 */
				setVoToBillCard(m_nPresentRecord, m_lanResTool.getStrByID(
						"40040101", "UPP40040101-000447")/* "被审批" */);
				/* 刷新按钮状态 */
				setButtonsCard();
				timer.addExecutePhase("审批后显示");
				timer.showAllExecutePhase("请购单审批操作onAudit（）结束");
				getBillCardPanel().setEnabled(false);
				if (iBillStatus.compareTo(nc.vo.scm.pu.BillStatus.FREE) == 0
						|| iBillStatus
								.compareTo(nc.vo.scm.pu.BillStatus.AUDITFAIL) == 0) {
					showHintMessage(m_lanResTool.getStrByID("40040101",
							"UPP40040101-000052")/* "审批未成功" */);

				} else if (iBillStatus
						.compareTo(nc.vo.scm.pu.BillStatus.AUDITED) == 0
						|| iBillStatus
								.compareTo(nc.vo.scm.pu.BillStatus.AUDITING) == 0) {
					showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
							"UPPSCMCommon-000236")/* "审批成功" */);
				}
			} catch (Exception e) {
				// 处理推式生成采购订单超容差提示情况
				if (e instanceof RwtPoToPrException) {
					// 请购单累计数量超出提示
					int iRet = showYesNoMessage(e.getMessage());
					if (iRet == MessageDialog.ID_YES) {
						// 继续循环
						isCycle = true;
						// 是否用户确认
						voAudit.setUserConfirm(true);
					}
				} else {
					PuTool.outException(this, e);
				}
				showHintMessage(m_lanResTool.getStrByID("40040101",
						"UPP40040101-000054")/* "出现异常,审批失败" */);
			}
		}
	}

	/**
	 * getBatchPrintEntry()。 创建日期：(2004-12-09) 获得打印入口
	 * 
	 * @author：周晓
	 * @return PrintDataInterface
	 * @param null
	 */
	protected nc.ui.pub.print.PrintEntry getNewBatchPrintEntry() {
		if (null == m_print) {
			m_print = new nc.ui.pub.print.PrintEntry(null, null);
			m_print.setTemplateID(getCorpPrimaryKey(), "4004070101",
					getClientEnvironment().getUser().getUserCode(), null);
		}
		return m_print;
	}

	/**
	 * 业务类型--点击事件响应
	 * 
	 * @param bo
	 */
	private void onBusiType(ButtonObject bo) {

		PfUtilClient.retAddBtn(m_btnAdds, m_sLoginCorpId, "20", bo);
		setButtons(getBtnTree().getButtonArray());
		updateButtons();
		setButtonsEnabled(false);
		bo.setSelected(true);
		m_btnBusiTypes.setEnabled(true);
		int m_btnBusiTypeCount = m_btnBusiTypes.getChildCount();
		int m_btnAddCount = m_btnAdds.getChildCount();
		if (m_btnBusiTypeCount > 0) {
			for (int i = 0; i < m_btnBusiTypeCount; i++) {
				m_btnBusiTypes.getChildButtonGroup()[i].setEnabled(true);
			}
		}
		m_btnAdds.setEnabled(true);
		if (m_btnAddCount > 0) {
			for (int i = 0; i < m_btnAddCount; i++) {
				m_btnAdds.getChildButtonGroup()[i].setEnabled(true);
			}
		}

		m_btnList.setEnabled(true);
		m_btnOthersFuncs.setEnabled(true);
		m_btnQuery.setEnabled(true);
		m_btnPrint.setEnabled(true);
		m_btnPrintPreview.setEnabled(true);
		m_btnBrowses.setEnabled(true);
		m_btnMaintains.setEnabled(true);
		m_nUIState = 0;
		// 维护请购单页面，此时请购单保存未审核，『单据维护』下“修改”、“复制”、“作废”按钮可用，此时点击『业务类型』下某个类型，此时再查看『单据维护』，修改”、“复制”、“作废”按钮应该还可用(此时并未进入增加状态)。
		int iStatus = 0;
		if (m_VOs != null && m_VOs.length > 0
				&& m_VOs[getCurVoPos()].getHeadVO().getIbillstatus() != null) {
			iStatus = m_VOs[getCurVoPos()].getHeadVO().getIbillstatus()
					.intValue();
		}
		if ((iStatus == 0 || iStatus == 4) && m_VOs != null && m_VOs.length > 0) {
			m_btnModify.setEnabled(true);
			m_btnDiscard.setEnabled(true);
			m_btnCopy.setEnabled(true);
			m_btnActions.setEnabled(true);
			int m_btnActionLength = m_btnActions.getChildButtonGroup().length;
			for (int i = 0; i < m_btnActionLength; i++) {
				if ("APPROVE".equals(m_btnActions.getChildButtonGroup()[i]
						.getTag())) {
					if (isCanAudit(m_VOs[m_nPresentRecord])) {
						m_btnActions.getChildButtonGroup()[i].setEnabled(true);
					} else {
						m_btnActions.getChildButtonGroup()[i].setEnabled(false);
					}
				}
				if ("UNAPPROVE".equals(m_btnActions.getChildButtonGroup()[i]
						.getTag())) {
					if (isCanUnAudit(m_VOs[m_nPresentRecord])) {
						m_btnActions.getChildButtonGroup()[i].setEnabled(true);
					} else {
						m_btnActions.getChildButtonGroup()[i].setEnabled(false);
					}
				}
				if ("OPEN".equals(m_btnActions.getChildButtonGroup()[i]
						.getTag())) {
					if (isCanOpen(m_VOs[m_nPresentRecord])) {
						m_btnActions.getChildButtonGroup()[i].setEnabled(true);
					} else {
						m_btnActions.getChildButtonGroup()[i].setEnabled(false);
					}
				}
				if ("CLOSE".equals(m_btnActions.getChildButtonGroup()[i]
						.getTag())) {
					if (isCanClose(m_VOs[m_nPresentRecord])) {
						m_btnActions.getChildButtonGroup()[i].setEnabled(true);
					} else {
						m_btnActions.getChildButtonGroup()[i].setEnabled(false);
					}
				}
			}
		}
		updateButtonsAll();
		m_bizButton = bo;
		showHintMessage(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000532", null, new String[] { bo.getName() })/*
																		 * @res
																		 * "当前操作业务类型："
																		 */);
	}

	/**
	 * 送审
	 * 
	 * @modified by czp v50, 代码逻辑重构
	 */
	private void onSendAudit() {

		boolean bSaveFlag = getBillCardPanel().getBillData().getEnabled();
		PraybillVO vo = null;
		try {
			// 编辑状态送审＝“保存”＋“送审”
			if (bSaveFlag) {
				boolean bContinue = onSave();
				if (!bContinue) {
					return;
				}
			}
			// 获取刷新后的VO数据
			vo = getPraybillVOs()[getCurVoPos()];
			if (vo == null) {
				setButtonsCard();
				SCMEnv.out("请购单VO为空，保存成功，但不能继续送审");/* -=notranslate=- */
				showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
						"UPPSCMCommon-000408")/* @res"送审失败！" */);
				return;
			}
			// 送审
			PfUtilClient.processAction("SAVE", BillTypeConst.PO_PRAY,
					ClientEnvironment.getInstance().getDate().toString(), vo);

			// 刷新单据，支持送审即审批情况
			setLastestInfosToVoAfterAuditted(vo);

			// 刷新按钮状态
			setButtonsCard();

			/* 加载单据 */
			setVoToBillCard(m_nPresentRecord, m_lanResTool.getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000265")/* "送审" */);
			//
			updateButtonsAll();
			// 设置送审标志
			isAlreadySendToAudit = false;
			showHintMessage(m_lanResTool.getStrByID("common", "UCH023")/*
																		 * @res"已送审"
																		 */);
		} catch (Exception e) {
			SCMEnv.out(e);
			if (e instanceof BusinessException || e instanceof RuntimeException) {
				MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
						"40040101", "UPP40040101-000081")/* @res "请购单保存" */, e
						.getMessage());
			} else {
				MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
						"SCMCOMMON", "UPPSCMCommon-000270")/* @res "提示" */,
						m_lanResTool.getStrByID("SCMCOMMON",
								"UPPSCMCommon-000408")/* @res"送审失败！" */);
			}
		}

	}

	/**
	 * 卡片按钮响应。
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * 使用示例
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @param bo
	 *            <p>
	 * @author czp
	 * @time 2007-3-14 上午10:47:22
	 */
	private void onButtonClickedCard(ButtonObject bo) {

		if (bo.getParent() == m_btnBusiTypes) {
			onBusiType(bo);
		} else if (bo.getParent() == m_btnAdds) {
			onAppend();
		} else if (bo == m_btnSave) {
			onSave();
		} else if (bo == m_btnModify) {
			onModify(false);
		} else if (bo == m_btnCancel) {
			onCancel();
		} else if (bo == m_btnDiscard) {
			onDiscard();
		} else if (bo == m_btnCopy) {
			onCopy();
		} else if (bo == m_btnAddLine) {
			onAppendLine(getBillCardPanel(), this);
		} else if (bo == m_btnDelLine) {
			onDeleteLine();
		} else if (bo == m_btnInsLine) {
			onInsertLine();
		} else if (bo == m_btnCpyLine) {
			onCopyLine();
		} else if (bo == m_btnPstLine) {
			onPasteLine();
		} else if (bo == m_btnSendAudit) {
			onSendAudit();
		} else if ("APPROVE".equals(bo.getTag())) {
			onAudit();
		} else if ("UNAPPROVE".equals(bo.getTag())) {
			onUnAudit();
		} else if ("CLOSE".equals(bo.getTag())) {
			onCloseCard();
		} else if ("OPEN".equals(bo.getTag())) {
			onOpenCard();
		} else if (bo == m_btnQuery) {
			onCardQuery();
		} else if (bo == m_btnFirst) {
			onFirst();
		} else if (bo == m_btnPrev) {
			onPrevious();
		} else if (bo == m_btnNext) {
			onNext();
		} else if (bo == m_btnLast) {
			onLast();
		} else if (bo == m_btnCard) {
			onList();
		}
		/* 审批状态查询：与消息中心共用 */
		else if (bo == m_btnWorkFlowBrowse) {
			onQueryForAudit();
		} else if (bo == m_btnUsable) {
			onQueryInvOnHand();
		} else if (bo == m_btnPriceInfo) {
			onPriceInfos();
		}
		/* 文档管理：与消息中心共用 */
		else if (bo == m_btnDocument) {
			onDocument();
		}
		/* 逐级联查 */
		else if (bo == m_btnLinkBillsBrowse) {
			onLinkQuery();
		}
		/* 消息中心按钮 */
		else if (bo == m_btnAudit) {
			onAudit();
		} else if (bo == m_btnUnAudit) {
			onUnAudit();
		} else if (bo == m_btnPrint) {
			onPrint();
		} else if (bo == m_btnPrintPreview) {
			onPrintPreview();
		}
		// 合并显示、打印
		else if (bo == m_btnCombin) {
			onCombin();
		}
		// 刷新
		else if (bo == m_btnRefresh) {
			onCardRefresh();
		}
		else if (bo == btnCKKCXX){
			onCKKCXX();
		}
	}

	/**
	 * 列表按钮响应。
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * 使用示例
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @param bo
	 *            <p>
	 * @author czp
	 * @time 2007-3-14 上午10:47:42
	 */
	private void onButtonClickedList(ButtonObject bo) {
		if (bo == m_btnModify) {
			onModify(true);
		} else if (bo == m_btnDiscardList) {
			onDiscard();
		} else if (bo == m_btnCopy) {
			onCopyList();
		}
		// 送审
		else if (bo == m_btnSendAudit) {
			onSendAudit();
		} else if ("APPROVE".equals(bo.getTag())) {
			onAuditList();
		} else if ("UNAPPROVE".equals(bo.getTag())) {
			onUnAuditList();
		} else if ("CLOSE".equals(bo.getTag())) {
			onCloseList();
		} else if ("OPEN".equals(bo.getTag())) {
			onOpenList();
		}
		/* 切换到卡片 */
		else if (bo == m_btnCard) {
			onCard();
		} else if (bo == m_btnWorkFlowBrowse) {
			onQueryForAudit();
		} else if (bo == m_btnUsableList) {
			onQueryInvOnHand();
		} else if (bo == m_btnDocumentList) {
			onDocument();
		}
		/* 逐级联查 */
		else if (bo == m_btnLinkBillsBrowse) {
			onLinkQuery();
		}
		/* 全选 */
		else if (bo == m_btnSelectAll) {
			onSelectAll();
		}
		/* 全消 */
		else if (bo == m_btnSelectNo) {
			onSelectNo();
		}
		/* 查询-列表 */
		else if (bo == m_btnQueryList) {
			onListQuery();
		}
		// 列表打印
		else if (bo == m_btnPrintList) {
			onPrintList();
		}
		// 列表打印预览
		else if (bo == m_btnPrintListPreview) {
			onPrintListPreview();
		}
		// 刷新
		else if (bo == m_btnRefreshList) {
			onListRefresh();
		}
		else if (bo == btnCKKCXX){
			onCKKCXX();
		}

	}

	/**
	 * 子类实现该方法，响应按钮事件。
	 * 
	 * @version (00-6-1 10:32:59)
	 * 
	 * @param bo
	 *            ButtonObject
	 */
	public void onButtonClicked(ButtonObject bo) {
		// add by zip: 2014/4/4 No 24
//		if(bo == btnCKKCXX) {
//			onCKKCXX();
//			return;
//		}
		// add end
		
		boolean bCardShowing = getBillCardPanel().isShowing();
		
		if (bCardShowing) {
			onButtonClickedCard(bo);
		} else {
			onButtonClickedList(bo);
		}
		// 处理扩展按钮事件
		onExtendBtnsClick(bo);

		// 设置扩展按钮状态
		if (m_bEdit) {
			setExtendBtnsStat(2);
		} else {
			setExtendBtnsStat(1);
		}
		//
		updateButtonsAll();

	}
	
	// add by zip:2014/4/5 No 24
	private void onCKKCXX() {
		try {
			boolean bCardShowing = getBillCardPanel().isShowing();
			BillModel bodyBM;
			String pk_corp;
			if(bCardShowing) {
				//pk_corp = (String) getBillCardPanel().getHeadItem("pk_corp").getValueObject();
				pk_corp = (String) getBillCardPanel().getCorp();
				bodyBM =  getBillCardPanel().getBillModel();
			}else {
				bodyBM = getBillListPanel().getBodyBillModel();
				int headSelectedRow = getBillListPanel().getHeadTable().getSelectedRow();
				//pk_corp = (String) getBillListPanel().getHeadBillModel().getValueAt(headSelectedRow, "pk_corp");
				pk_corp = (String) getBillCardPanel().getCorp();
			}
//			if(StringUtils.isEmpty(pk_corp)) {
//				MessageDialog.showErrorDlg(this, null, "请选择需要查看的单据");
//				return;
//			}
			String startDate = "2013-01-01";
			String dateto = new UFDate(System.currentTimeMillis()).toString();
			IUAPQueryBS queryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			int bodyRowCount = bodyBM.getRowCount();
			for (int i = 0; i < bodyRowCount; i++) {
				String invbasid =(String) bodyBM.getValueAt(i, "cbaseid");
//				Object inv = getBillCardPanel().getBodyPanel().getTable().getValueAt(i, 1);
//				if(inv != null){
				if(invbasid != null&&!"".equals(invbasid)){ 
					// 已申购未审批数量
					String sql1 = new StringBuilder().append("select sum(A.npraynum) as rst from po_praybill_b A,po_praybill B").append(" where A.cpraybillid=B.cpraybillid and nvl(A.dr,0)=0 and nvl(B.dr,0)=0").append(" and B.pk_corp='" + pk_corp + "' and B.ibillstatus=0").append(" and B.dpraydate<='" + dateto + "' and B.dpraydate>='" + startDate + "'").append(" and A.cbaseid='" + invbasid + "'").toString();
					// 已申购已审批数量
					String sql2 = new StringBuilder().append("select sum(A.npraynum) as rst from po_praybill_b A,po_praybill B").append(" where A.cpraybillid=B.cpraybillid and nvl(A.dr,0)=0 and nvl(B.dr,0)=0").append(" and B.pk_corp='" + pk_corp + "' and B.ibillstatus=3").append(" and B.dpraydate<='" + dateto + "' and B.dpraydate>='" + startDate + "'").append(" and A.cbaseid='" + invbasid + "'").toString();
					// 采购订单数量 edit by  yhj 2014-09-10 不包含已关闭数量 and A.iisactive = 0  and B.breturn = 'N'
 					String sql3 = new StringBuilder().append("select sum(A.nordernum) as rst from po_order_b A,po_order B").append(" where A.corderid=B.corderid").append(" and nvl(A.dr,0)=0 and nvl(B.dr,0)=0").append(" and B.pk_corp='" + pk_corp + "' and A.iisactive = 0 and B.breturn = 'N'" ).append(" and B.dorderdate<='" + dateto + "' and B.dorderdate>='" + startDate + "'").append(" and A.cbaseid='" + invbasid + "'").toString();
					//add by zwx 
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
					.append("    and A.cbaseid = '" + invbasid + "') ") 
					.append("    and nvl(po.dr,0) = 0 ") 
					.append("    and nvl(pob.dr,0) = 0 ") 
					.append("    and po.pk_corp = '" + pk_corp + "' ") 
					.append("    and pob.cbaseid  = '" + invbasid + "' ") 
					.append("    and po.dorderdate <= '" + dateto + "' ") 
					.append("    and po.dorderdate >= '" + startDate + "' ")
					.append("    and pob.iisactive = 0 ") ;

					//add by yhj 2014-09-10 采购订单数量（包含已关闭数量） 
 					String closeNumSQL = new StringBuilder().append("select sum(A.nordernum) as rst from po_order_b A,po_order B").append(" where A.corderid=B.corderid").append(" and nvl(A.dr,0)=0 and nvl(B.dr,0)=0").append(" and B.pk_corp='" + pk_corp + "'").append(" and B.dorderdate<='" + dateto + "' and B.dorderdate>='" + startDate + "'").append(" and A.cbaseid='" + invbasid + "'").toString();
				//采购订单数量（包含已关闭数量）
 					StringBuffer closeNumSQLNew = new StringBuffer();
					closeNumSQLNew.append(" select sum(pob.nordernum ) from po_order po ") 
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
					.append("    and A.cbaseid = '" + invbasid + "' and B.ibillstatus in('0','3') ) ")  //edit by zwx 2015-12-24
					.append("    and nvl(po.dr,0) = 0 ") 
					.append("    and nvl(pob.dr,0) = 0 ") 
					.append("    and po.pk_corp = '" + pk_corp + "' ") 
					.append("    and pob.cbaseid  = '" + invbasid + "' ") 
					.append("    and po.dorderdate <= '" + dateto + "' ") 
					.append("    and po.dorderdate >= '" + startDate + "' ") ;
					
					//end
					// 到货单数量
//					String sql4 = new StringBuilder().append("select sum(A.narrvnum) as rst from po_arriveorder_b A,po_arriveorder B").append(" where A.carriveorderid=B.carriveorderid").append(" and nvl(A.dr,0)=0 and nvl(B.dr,0)=0").append(" and B.pk_corp='" + pk_corp + "'").append(" and B.dreceivedate<='" + dateto + "' and B.dreceivedate>='" + startDate + "'").append(" and A.cbaseid='" + invbasid + "'").toString();
					//edit by zwx  2015-9-13 过滤采购订单为2013年后未关闭的生成的到货单数量
					StringBuffer sql4=new StringBuffer();
					sql4.append(" select sum(A.narrvnum) as rst ") 
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
					// 现存量
					String sql7 = new StringBuilder().append("select sum(nonhandnum) from ic_onhandnum where nvl(dr,0)=0 and cinvbasid='"+invbasid+"' and pk_corp='"+pk_corp+"'").toString();
					// 再订购点
					String sql8 = new StringBuilder().append("select zdhd from bd_produce where nvl(dr,0)=0 and pk_invbasdoc='"+invbasid+"' and pk_corp='"+pk_corp+"'").toString();
					// 最高库存
					String sql9 = new StringBuilder().append("select maxstornum from bd_produce where nvl(dr,0)=0 and pk_invbasdoc='"+invbasid+"' and pk_corp='"+pk_corp+"'").toString();
					
					
					double d1, d2, d3, d4, d5, d6,d7,d8,d9,d10,new1,new2;
					Object obj = queryBS.executeQuery(sql1, new ColumnProcessor());
					d1 = obj == null ? 0 : Double.parseDouble(obj.toString());
					obj = queryBS.executeQuery(sql2, new ColumnProcessor());
					d2 = obj == null ? 0 : Double.parseDouble(obj.toString());
					obj = queryBS.executeQuery(sql3.toString(), new ColumnProcessor());
					d3 = obj == null ? 0 : Double.parseDouble(obj.toString());
					obj = queryBS.executeQuery(sql4.toString(), new ColumnProcessor());
					d4 = obj == null ? 0 : Double.parseDouble(obj.toString());
					obj = queryBS.executeQuery(sql5, new ColumnProcessor());
					d5 = obj == null ? 0 : Double.parseDouble(obj.toString());
					obj = queryBS.executeQuery(sql6, new ColumnProcessor());
					d6 = obj == null ? 0 : Double.parseDouble(obj.toString());
					obj = queryBS.executeQuery(sql7, new ColumnProcessor());
					d7 = obj == null ? 0 : Double.parseDouble(obj.toString());
					obj = queryBS.executeQuery(sql8, new ColumnProcessor());
					d8 = obj == null ? 0 : Double.parseDouble(obj.toString());
					obj = queryBS.executeQuery(sql9, new ColumnProcessor());
					d9 = obj == null ? 0 : Double.parseDouble(obj.toString());
					obj = queryBS.executeQuery(sql3New.toString(), new ColumnProcessor());
					new1 = obj == null ? 0 : Double.parseDouble(obj.toString());
					obj = queryBS.executeQuery(closeNumSQLNew.toString(), new ColumnProcessor());
					new2 = obj == null ? 0 : Double.parseDouble(obj.toString());
//					bodyBM.setValueAt(d1+d2, i, "pk_defdoc10"); // 已申购(含未审批)
					//edit by yhj 2014-09-10
					obj = queryBS.executeQuery(closeNumSQL.toString(), new ColumnProcessor());
					d10 = obj == null ? 0 : Double.parseDouble(obj.toString());
					//add by yhj 20140910  关联问题清单：全选存货点击按钮后显示对应数据，单选也出，增加存货行后再点击没有数据。对应任务内清单24号问题的备注解决方案
					Double b1 = d1+d2-new2;
 					Double b2 = d3 - d4;
					Double b3 = d7-d6;
					Double b4 = d8;
					Double b5 = d9;
					//end
					bodyBM.setValueAt(b1.toString(), i, "pk_defdoc10"); // 已申购(含未审批)-采购订单数量(包含关闭)
					bodyBM.setValueAt(b2.toString(), i, "pk_defdoc11"); // 已订购未到货数量
					bodyBM.setValueAt(b3.toString(), i, "pk_defdoc12"); // 现存可用数量(现存量-已冻结)
					bodyBM.setValueAt(b4.toString(), i, "pk_defdoc13"); // 再订购数量
					bodyBM.setValueAt(b5.toString(), i, "pk_defdoc14"); // 最高库存数
					//add by zwx 2015-9-12 
					bodyBM.setValueAt(new UFDouble(d7), i, "vdef8");//现存量
					//end by zwx
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	// add end

	/**
	 * 打印预览-列表
	 * 
	 */
	private void onPrintListPreview() {
		try {
			if (printList == null) {
				printList = new ScmPrintTool(this, getBillCardPanel(),
						getSelectedBills(), getModuleCode());
			} else {
				printList.setData(getSelectedBills());
			}
			printList.onBatchPrintPreview(getBillListPanel(),
					getBillCardPanel(), "20");
			MessageDialog.showHintDlg(this, m_lanResTool.getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000270")/* @res"提示" */,
					printList.getPrintMessage());
		} catch (BusinessException e) {
			PuTool.outException(this, e);
		}
	}

	/**
	 * 打印-列表
	 * 
	 */
	private void onPrintList() {

		try {
			if (printList == null) {
				printList = new ScmPrintTool(this, getBillCardPanel(),
						getSelectedBills(), getModuleCode());
			} else {
				printList.setData(getSelectedBills());
			}
			printList
					.onBatchPrint(getBillListPanel(), getBillCardPanel(), "20");
			MessageDialog.showHintDlg(this, m_lanResTool.getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000270")/* "提示" */, printList
					.getPrintMessage());
		} catch (BusinessException e) {
			PuTool.outException(this, e);
		}
	}

	/**
	 * 打印预览--卡片
	 */
	private void onPrintPreview() {

		Vector vAll = new Vector();
		PraybillVO[] oneBill = null;
		vAll.add(m_VOs[m_nPresentRecord]);
		oneBill = new PraybillVO[vAll.size()];
		// oneBill[0] = new PraybillVO();
		vAll.copyInto(oneBill);
		ArrayList aryRslt = new ArrayList();
		aryRslt.add(oneBill[0]);

		try {
			if (printCard == null) {
				// 目前还有南京蒲镇不想补空行
				if (nc.vo.scm.pub.CustomerConfigVO
						.getCustomerName()
						.equalsIgnoreCase(
								nc.vo.scm.pub.CustomerConfigVO.NAME_NANJINGPUZHEN)) {
					PurchasePrintDS printData = new PurchasePrintDS(
							getModuleCode(), getBillCardPanel());
					printCard = new ScmPrintTool(getBillCardPanel(), printData,
							aryRslt, getModuleCode());
				} else {
					printCard = new ScmPrintTool(this, getBillCardPanel(),
							aryRslt, getModuleCode());
				}
			} else {
				printCard.setData(aryRslt);
			}
			printCard.onCardPrintPreview(getBillCardPanel(),
					getBillListPanel(), "20");
			MessageDialog.showHintDlg(this, m_lanResTool.getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000270")/* "提示" */, printCard
					.getPrintMessage());
		} catch (Exception e1) {
			PuTool.outException(this, e1);
		}
	}

	/**
	 * 打印--卡片
	 */
	private void onPrint() {
		Vector vAll = new Vector();
		PraybillVO[] oneBill = null;
		vAll.add(m_VOs[m_nPresentRecord]);
		oneBill = new PraybillVO[vAll.size()];
		// oneBill[0] = new PraybillVO();
		vAll.copyInto(oneBill);
		ArrayList aryRslt = new ArrayList();
		aryRslt.add(oneBill[0]);

		try {
			if (printCard == null) {
				// 目前还有南京蒲镇不想补空行
				if (nc.vo.scm.pub.CustomerConfigVO
						.getCustomerName()
						.equalsIgnoreCase(
								nc.vo.scm.pub.CustomerConfigVO.NAME_NANJINGPUZHEN)) {
					PurchasePrintDS printData = new PurchasePrintDS(
							getModuleCode(), getBillCardPanel());
					printCard = new ScmPrintTool(getBillCardPanel(), printData,
							aryRslt, getModuleCode());
				} else {
					printCard = new ScmPrintTool(this, getBillCardPanel(),
							aryRslt, getModuleCode());
				}
			} else {
				printCard.setData(aryRslt);
			}
			printCard.onCardPrint(getBillCardPanel(), getBillListPanel(), "20");
			MessageDialog.showHintDlg(this, m_lanResTool.getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000270")/* "提示" */, printCard
					.getPrintMessage());
		} catch (Exception e1) {
			PuTool.outException(this, e1);
		}
	}

	/**
	 * 功能描述:放弃
	 */
	public void onCancel() {
		isFrmCopy = false;
		/* 不能连续取消 */
		if (m_bCancel)
			return;
		else
			m_bCancel = true;
		// 终止编辑
		if (getBillCardPanel().getBillTable().getEditingRow() >= 0) {
			getBillCardPanel().getBillTable().editingStopped(
					new ChangeEvent(getBillCardPanel().getBillTable()));
		}
		/*
		 * 增加标志为FALSE,编辑标志为FALSE,单据处于不可编辑状态 在增加状态下放弃，如果放弃前单据存在,显示相应的单据;否则,显示为空
		 * 在修改状态下放弃，显示放弃前的单据
		 */
		if (m_bAdd) {
			if (m_VOs != null && m_VOs.length > 0) {
				/* 如果放弃前单据存在,则显示相应的单据 */
				if (m_VOs != null
						&& m_VOs[m_nPresentRecord] != null
						&& m_VOs[m_nPresentRecord].getHeadVO().getCpraybillid() != null) {
					getBillCardPanel().setBillValueVO(m_VOs[m_nPresentRecord]);
					nc.ui.pu.pub.PuTool.loadInvMaxPrice(getBillCardPanel());
					/* 显示来源信息 */
					PuTool.loadSourceInfoAll(getBillCardPanel(),
							BillTypeConst.PO_PRAY);
					getBillCardPanel().getBillModel().execLoadFormula();
					getBillCardPanel().updateValue();
					getBillCardPanel().updateUI();
					/* 显示备注 */
					UIRefPane nRefPanel3 = (UIRefPane) getBillCardPanel()
							.getHeadItem("vmemo").getComponent();
					nRefPanel3.setValue(m_VOs[m_nPresentRecord].getHeadVO()
							.getVmemo());
				}
				if (m_VOs != null
						&& m_VOs.length == 1
						&& m_VOs[m_nPresentRecord] != null
						&& m_VOs[m_nPresentRecord].getHeadVO().getCpraybillid() == null) {
					getBillCardPanel().getBillData().clearViewData();
					getBillCardPanel().updateUI();
				}
			} else {
				/* 如果放弃前单据不存在,则清除界面 */
				getBillCardPanel().getBillData().clearViewData();
				getBillCardPanel().updateUI();
			}
		} else {
			getBillCardPanel().setBillValueVO(m_VOs[m_nPresentRecord]);
			nc.ui.pu.pub.PuTool.loadInvMaxPrice(getBillCardPanel());
			getBillCardPanel().getBillModel().execLoadFormula();
			/* 显示来源信息 */
			PuTool.loadSourceInfoAll(getBillCardPanel(), BillTypeConst.PO_PRAY);
			getBillCardPanel().updateValue();
			getBillCardPanel().updateUI();
			/* 显示备注 */
			UIRefPane nRefPanel3 = (UIRefPane) getBillCardPanel().getHeadItem(
					"vmemo").getComponent();
			nRefPanel3.setValue(m_VOs[m_nPresentRecord].getHeadVO().getVmemo());
		}
		setButtonsCard();
		/* 恢复按钮状态 */
		m_nUIState = 0;
		m_bAdd = false;
		m_bEdit = false;
		getBillCardPanel().setEnabled(false);

		// 晁志平 增加重庆力帆自由项输入提示功能
		nc.ui.po.pub.InvAttrCellRenderer ficr = new nc.ui.po.pub.InvAttrCellRenderer();
		ficr.setFreeItemRenderer(getBillCardPanel(), null);

		showHintMessage(m_lanResTool.getStrByID("common", "UCH008")/* "取消成功" */);
	}

	/**
	 * 功能描述:卡片状态查询
	 */
	private void onCardQuery() {

		getQueryDlg().showModal();

		if (getQueryDlg().isCloseOK()) {

			m_bQueried = true;

			// //设置数据权限, czp ,since v50
			// getQueryDlg().setRefsDataPowerConVOs(
			// PoPublicUIClass.getLoginUser(),
			// new String[]{PoPublicUIClass.getLoginPk_corp()},
			// PrTool.getPowerNodeNames(),
			// PrTool.getPowerCodes(),
			// PrTool.getPowerReturnTypes());

			onCardRefresh();
		}

		showHintMessage(m_lanResTool.getStrByID("common", "UCH009")/*
																	 * @res
																	 * "查询完成"
																	 */);
	}

	/**
	 * 卡片刷新
	 * 
	 */
	private void onCardRefresh() {
		showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
				"UPPSCMCommon-000360")/* "开始查询..." */);
		// 自定义条件
		ConditionVO[] conditionVO = getQueryDlg().getConditionVO();
		// 来源单据条件
		String strSubSql = getQueryDlg().getSubSQL();
		// 查询请购单
		try {
			m_VOs = PraybillHelper.queryAll(getQueryDlg()
					.getSelectedCorpIdString(), conditionVO, getQueryDlg()
					.getStatusCndStr(), null, strSubSql);
		} catch (BusinessException e) {
			showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
					"UPPSCMCommon-000038")/* "查询失败" */);
			MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
					"40040101", "UPP40040101-000055")/* "请购单查询" */, e
					.getMessage());
			return;
		} catch (Exception e) {
			showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
					"UPPSCMCommon-000038")/* "查询失败" */);
			MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
					"40040101", "UPP40040101-000055")/* "请购单查询" */, e
					.getMessage());
			return;
		}
		// 处理返回查询结果
		if (m_VOs != null && m_VOs.length > 0) {
			// 处理表头下拉列表
			Integer nTemp1 = (Integer) m_VOs[0].getHeadVO().getAttributeValue(
					"ipraysource");
			Integer nTemp2 = (Integer) m_VOs[0].getHeadVO().getAttributeValue(
					"ipraytype");

			m_comPraySource.setSelectedIndex(nTemp1.intValue());
			m_comPrayType.setSelectedIndex(nTemp2.intValue());

			Vector v = new Vector();
			for (int i = 0; i < m_VOs.length; i++) {
				v.addElement(m_VOs[i].getHeadVO());
			}
			PraybillHeaderVO[] headVO = new PraybillHeaderVO[v.size()];
			v.copyInto(headVO);

			// 加载单据到卡片
			m_nPresentRecord = 0;
			setVoToBillCard(m_nPresentRecord, null);
		} else {
			m_VOs = null;
			// 无返回查询结果
			MessageDialog.showHintDlg(this, m_lanResTool.getStrByID("40040101",
					"UPP40040101-000055")/* "请购单查询" */, m_lanResTool.getStrByID(
					"40040101", "UPP40040101-000056")/* "没有符合条件的请购单！" */);
			// 清空数据
			getBillCardPanel().getBillData().clearViewData();
		}
		// 设置按钮逻辑
		setButtonsCard();
		//
		m_nUIState = 0;
		// 界面下角提示信息
		int iCnt = 0;
		if (m_VOs != null && m_VOs.length > 0)
			iCnt = m_VOs.length;
		if (iCnt > 0) {
			String[] value = new String[] { String.valueOf(iCnt) };
			showHintMessage(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000478", null, value)/*
													 * "查询完毕，返回 " +iCnt + "张单据"
													 */);
		} else
			showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
					"UPPSCMCommon-000361")/* "查询完毕，没有查到数据" */);
		m_bAdd = false;
		m_bEdit = false;
		getBillCardPanel().setEnabled(false);
		showHintMessage(m_lanResTool.getStrByID("common", "UCH007")/*
																	 * @res
																	 * "刷新成功"
																	 */);
	}

	/**
	 * 功能描述:关闭当前的请购单
	 */
	private void onCloseList() {
		this.showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
				"UPPSCMCommon-000390")/* "正在关闭单据..." */);
		m_VOs[m_nPresentRecord].getHeadVO().setCuserid(
				getClientEnvironment().getUser().getPrimaryKey());
		long tTime = System.currentTimeMillis();
		try {
			// 赋操作员
			String strOpr = getClientEnvironment().getUser().getPrimaryKey();
			m_VOs[m_nPresentRecord].setCurrOperator(strOpr);
			ArrayList keys = (ArrayList) PfUtilClient
					.processActionNoSendMessage(this, "CLOSE",
							nc.vo.scm.pu.BillTypeConst.PO_PRAY,
							getClientEnvironment().getDate().toString(),
							m_VOs[m_nPresentRecord], null, null, null);
			m_VOs[m_nPresentRecord] = (PraybillVO) keys.get(0);

			m_VOs[m_nPresentRecord].getHeadVO().setIbillstatus(new Integer(1));
			// 单据关闭后金额被清空
			if (m_VOs[m_nPresentRecord] != null
					&& m_VOs[m_nPresentRecord].getBodyVO() != null) {
				int itemsLength = m_VOs[m_nPresentRecord].getBodyVO().length;
				UFDouble nMoney = null;
				UFDouble nPrayNum = null;
				UFDouble nSuggestPrice = null;

				for (int i = 0; i < itemsLength; i++) {
					nMoney = m_VOs[m_nPresentRecord].getBodyVO()[i].getNmoney();
					nPrayNum = m_VOs[m_nPresentRecord].getBodyVO()[i]
							.getNpraynum();
					nSuggestPrice = m_VOs[m_nPresentRecord].getBodyVO()[i]
							.getNsuggestprice();
					if (nMoney == null && nPrayNum != null
							&& nSuggestPrice != null) {
						nMoney = new UFDouble(nPrayNum.doubleValue()
								* nSuggestPrice.doubleValue());
					} else {
						nMoney = new UFDouble(0);
					}
					m_VOs[m_nPresentRecord].getBodyVO()[i].setNmoney(nMoney);
				}

			}
		} catch (Exception e) {
			MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
					"40040101", "UPP40040101-000057")/* "请购单关闭" */, e
					.getMessage());
			SCMEnv.out(e);
			showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
					"UPPSCMCommon-000391")/* "关闭失败" */);
			return;
		}
		tTime = System.currentTimeMillis() - tTime;
		Logger.debug("请购单关闭时间：" + tTime + " ms!");
		m_nUIState = 0;
		getBillListPanel().getHeadBillModel().setBodyRowVO(
				m_VOs[m_nPresentRecord].getHeadVO(), m_nPresentRecord);
		getBillListPanel().getBodyBillModel().setBodyDataVO(
				m_VOs[m_nPresentRecord].getBodyVO());
		updateUI();
		/* 刷新按钮状态 */
		setButtonsList();
		//
		showHintMessage(m_lanResTool.getStrByID("common", "UCH013")/*
																	 * @res
																	 * "已关闭"
																	 */);
		return;
	}

	/**
	 * 功能描述:关闭当前的请购单
	 */
	private void onCloseCard() {
		this.showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
				"UPPSCMCommon-000390")/* "正在关闭单据..." */);
		m_VOs[m_nPresentRecord].getHeadVO().setCuserid(
				getClientEnvironment().getUser().getPrimaryKey());
		long tTime = System.currentTimeMillis();
		try {
			// 赋操作员
			String strOpr = getClientEnvironment().getUser().getPrimaryKey();
			m_VOs[m_nPresentRecord].setCurrOperator(strOpr);
			ArrayList keys = (ArrayList) PfUtilClient
					.processActionNoSendMessage(this, "CLOSE",
							nc.vo.scm.pu.BillTypeConst.PO_PRAY,
							getClientEnvironment().getDate().toString(),
							m_VOs[m_nPresentRecord], null, null, null);
			m_VOs[m_nPresentRecord] = (PraybillVO) keys.get(0);

			m_VOs[m_nPresentRecord].getHeadVO().setIbillstatus(new Integer(1));
			// 单据关闭后金额被清空
			if (m_VOs[m_nPresentRecord] != null
					&& m_VOs[m_nPresentRecord].getBodyVO() != null) {
				int itemsLength = m_VOs[m_nPresentRecord].getBodyVO().length;
				UFDouble nMoney = null;
				UFDouble nPrayNum = null;
				UFDouble nSuggestPrice = null;

				for (int i = 0; i < itemsLength; i++) {
					nMoney = m_VOs[m_nPresentRecord].getBodyVO()[i].getNmoney();
					nPrayNum = m_VOs[m_nPresentRecord].getBodyVO()[i]
							.getNpraynum();
					nSuggestPrice = m_VOs[m_nPresentRecord].getBodyVO()[i]
							.getNsuggestprice();
					if (nMoney == null && nPrayNum != null
							&& nSuggestPrice != null) {
						nMoney = new UFDouble(nPrayNum.doubleValue()
								* nSuggestPrice.doubleValue());
					} else {
						nMoney = new UFDouble(0);
					}
					m_VOs[m_nPresentRecord].getBodyVO()[i].setNmoney(nMoney);
				}

			}
		} catch (Exception e) {
			MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
					"40040101", "UPP40040101-000057")/* "请购单关闭" */, e
					.getMessage());
			SCMEnv.out(e);
			showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
					"UPPSCMCommon-000391")/* "关闭失败" */);
			return;
		}
		tTime = System.currentTimeMillis() - tTime;
		Logger.debug("请购单关闭时间：" + tTime + " ms!");
		m_nUIState = 0;
		/* 加载单据 */
		setVoToBillCard(m_nPresentRecord, m_lanResTool.getStrByID("40040101",
				"UPP40040101-000448")/* "被关闭" */);
		/* 刷新按钮状态 */
		setButtonsCard();
		//
		showHintMessage(m_lanResTool.getStrByID("common", "UCH013")/*
																	 * @res
																	 * "已关闭"
																	 */);
		return;
	}

	/**
	 * 功能描述:退出系统
	 */
	public boolean onClosing() {
		// 正在编辑单据时退出提示
		if (m_bEdit) {
			int iRet = MessageDialog.showYesNoCancelDlg(this, m_lanResTool
					.getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/* "提示" */,
					m_lanResTool.getStrByID("common", "UCH001")/*
																 * @res
																 * "是否保存已修改的数据？"
																 */);
			// 保存成功后才退出
			if (iRet == MessageDialog.ID_YES) {
				return onSave();
			}
			// 退出
			else if (iRet == MessageDialog.ID_NO) {
				return true;
			}
			// 取消关闭
			else {
				return false;
			}
		}
		isclose = true;
		return true;
	}

	/**
	 * 列表拷贝功能。
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * 使用示例
	 * <p>
	 * <b>参数说明</b>
	 * <p>
	 * 
	 * @author czp
	 * @time 2007-3-14 上午11:02:12
	 */
	private void onCopyList() {
		// 先切换到卡片
		onCard();
		// 再走卡片拷贝功能
		onCopy();
	}

	/**
	 * 功能描述:复制
	 */
	private void onCopy() {
		showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
				"UPPSCMCommon-000350")/* "编辑单据..." */);

		nc.vo.scm.pu.Timer timer = new nc.vo.scm.pu.Timer();
		timer.start("请购单复制操作开始onCopy");

		isFrmCopy = true;
		// 加载最高限价
		nc.ui.pu.pub.PuTool.loadInvMaxPrice(getBillCardPanel());

		timer.addExecutePhase("加载最高限价loadInvMaxPrice");

		// 设置请购来源为手工录入,单据日期为当前日期(除了销售订单外，因为有回写关系)
		if (m_comPraySource.getSelectedIndex() != 3) {
			m_comPraySource.setSelectedIndex(5);
		}
		m_comPraySource.setEnabled(false);
		UIRefPane nRefPane = (UIRefPane) getBillCardPanel().getHeadItem(
				"dpraydate").getComponent();
		nRefPane.setValue(getClientEnvironment().getDate().toString());

		nRefPane = (UIRefPane) getBillCardPanel().getTailItem("tmaketime")
				.getComponent();
		nRefPane.setValue(null);
		nRefPane = (UIRefPane) getBillCardPanel().getTailItem("taudittime")
				.getComponent();
		nRefPane.setValue(null);
		nRefPane = (UIRefPane) getBillCardPanel().getTailItem("tlastmaketime")
				.getComponent();
		nRefPane.setValue(null);

		// 如果审批人存在,则清除审批人
		nRefPane = (UIRefPane) getBillCardPanel().getTailItem("cauditpsn")
				.getComponent();
		nRefPane.setValue(null);
		// 如果审批日期存在,则清除审批人
		nRefPane = (UIRefPane) getBillCardPanel().getTailItem("dauditdate")
				.getComponent();
		nRefPane.setValue(null);
		nRefPane = (UIRefPane) getBillCardPanel().getTailItem("coperator")
				.getComponent();
		nRefPane.setPK(getClientEnvironment().getUser().getPrimaryKey());
		getBillCardPanel().setEnabled(true);
		setPartNoEditable(getBillCardPanel());

		timer.addExecutePhase("一些赋值操作");

		// 设置表体辅计量状态
		// setAssisUnitEditState();

		timer.addExecutePhase("setAssisUnitEditState（）");

		m_bAdd = true;
		m_bEdit = true;
		m_bCancel = false;
		setButtonsCardCopy();
		m_nUIState = 0;
		updateButtonsAll();
		// 复制时,单据号为空
		UIRefPane nRefPanel3 = (UIRefPane) getBillCardPanel().getHeadItem(
				"vpraycode").getComponent();
		nRefPanel3.setValue(null);
		// 清空表头数据
		getBillCardPanel().getHeadItem("cpraybillid").setValue(null);
		getBillCardPanel().getHeadItem("ibillstatus").setValue(
				new Integer(BillStatus.FREE));
		getBillCardPanel().getHeadItem("ts").setValue(null);
		// 清空表体数据
		BillModel bm = getBillCardPanel().getBillModel();
		int bmCount = bm.getRowCount();
		String sMangId;
		for (int i = 0; i < bmCount; i++) {
			bm.setRowState(i, BillModel.ADD);
			bm.setValueAt(null, i, "cpraybill_bid");
			bm.setValueAt(null, i, "cpraybillid");
			bm.setValueAt(null, i, "naccumulatenum");

			bm.setValueAt(null, i, "ts");
			// 批次号
			sMangId = (String) bm.getValueAt(i, "cmangid");
			bm.setCellEditable(i, "vproducenum", PuTool.isBatchManaged(sMangId)
					&& bm.getItemByKey("vproducenum").isEdit());
		}

		// 晁志平 增加重庆力帆自由项输入提示功能
		nc.ui.po.pub.InvAttrCellRenderer ficr = new nc.ui.po.pub.InvAttrCellRenderer();
		ficr.setFreeItemRenderer(getBillCardPanel(), m_VOs[m_nPresentRecord]);

		timer.showAllExecutePhase("请购单复制操作结束onCopy");

		showHintMessage(m_lanResTool.getStrByID("common", "UCH029")/*
																	 * @res
																	 * "已复制"
																	 */);
	}

	/**
	 * 功能描述:行拷贝
	 */
	private void onCopyLine() {
		if (!m_bIsSubMenuPressed) {
			int[] nSelected = getBillCardPanel().getBillTable()
					.getSelectedRows();
			if (nSelected == null || nSelected.length == 0) {
				MessageDialog
						.showHintDlg(this, m_lanResTool.getStrByID("common",
								"UC001-0000015")/* @res "复制行" */, m_lanResTool
								.getStrByID("40040101", "UPP40040101-000059")/* "没有选中请购单表体行！" */);
				return;
			}
		}
		getBillCardPanel().copyLine();
		m_bCancel = false;
		m_nUIState = 0;
		updateButtonsAll();
		showHintMessage(m_lanResTool.getStrByID("common", "UCH039")/*
																	 * @res
																	 * "复制行成功"
																	 */);
	}

	/**
	 * 功能描述:删行
	 */
	private void onDeleteLine() {
		if (!getBillCardPanel().delLine()) {
			MessageDialog.showHintDlg(this, m_lanResTool.getStrByID("common",
					"UC001-0000013")/* "删行" */, m_lanResTool.getStrByID(
					"40040101", "UPP40040101-000059")/* "没有选中请购单表体行！" */);
			return;
		}
		m_bCancel = false;
		m_nUIState = 0;
		updateButtonsAll();
		// 输入提示 czp 20050303 力帆
		PraybillVO voCurr = (PraybillVO) getBillCardPanel().getBillValueVO(
				PraybillVO.class.getName(), PraybillHeaderVO.class.getName(),
				PraybillItemVO.class.getName());
		nc.ui.po.pub.InvAttrCellRenderer ficr = new nc.ui.po.pub.InvAttrCellRenderer();
		ficr.setFreeItemRenderer(getBillCardPanel(), voCurr);
		showHintMessage(m_lanResTool.getStrByID("common", "UCH037")/*
																	 * @res
																	 * "删行成功"
																	 */);
	}

	/**
	 * 功能描述:请购单作废(列表卡片共用)
	 */
	private void onDiscard() {
		showHintMessage(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000060")/* "作废单据..." */);
		int ret = MessageDialog.showYesNoDlg(this, m_lanResTool.getStrByID(
				"SCMCOMMON", "UPPSCMCommon-000219")/* @res "确定" */,
				m_lanResTool.getStrByID("common", "4004COMMON000000069")/*
																		 * @res
																		 * "是否确认要删除？"
																		 */,
				UIDialog.ID_NO);
		if (ret != MessageDialog.ID_YES) {
			return;
		}
		Integer[] nSelected = null;
		final boolean bCardShowing = getBillCardPanel().isVisible();
		Vector vv = new Vector();
		if (bCardShowing) {
			nSelected = new Integer[1];
			nSelected[0] = new Integer(m_nPresentRecord);
		} else {
			/* 列表 */
			Vector v = new Vector();
			final int nRow = getBillListPanel().getHeadBillModel()
					.getRowCount();
			int nStatus;
			for (int i = 0; i < nRow; i++) {
				nStatus = getBillListPanel().getHeadBillModel().getRowState(i);
				if (nStatus == BillModel.SELECTED) {
					v.addElement(new Integer(nc.ui.pu.pub.PuTool
							.getIndexBeforeSort(getBillListPanel(), i)));
				} else {
					vv.addElement(new Integer(nc.ui.pu.pub.PuTool
							.getIndexBeforeSort(getBillListPanel(), i)));
				}
			}
			nSelected = new Integer[v.size()];
			v.copyInto(nSelected);
		}

		if (nSelected == null || nSelected.length == 0) {
			MessageDialog
					.showErrorDlg(this, m_lanResTool.getStrByID("40040101",
							"UPP40040101-000061")/* "请购单作废" */, m_lanResTool
							.getStrByID("40040101", "UPP40040101-000062")/* "未选中请购单！" */);
			showHintMessage(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000063")/* "作废单据失败" */);
			return;
		}
		long tTime = System.currentTimeMillis();
		Vector v = new Vector();
		/*
		 * 检查请购单状态,如果是下列之一，不能作废: 关闭、正在审批、审批通过、作废
		 */
		StringBuffer sMessage = new StringBuffer();
		PraybillVO vo = null;
		int nBillStatus = 0;
		String[] sPraybillIds = new String[nSelected.length];
		// int nSelectedLength = nSelected.length;
		for (int i = 0; i < nSelected.length; i++) {
			vo = m_VOs[nSelected[i].intValue()];
			sPraybillIds[i] = vo.getParentVO().getAttributeValue("cpraybillid")
					.toString();
			vo.getHeadVO().setCuserid(
					getClientEnvironment().getUser().getPrimaryKey());
			v.addElement(vo);
			nBillStatus = vo.getHeadVO().getIbillstatus().intValue();
			if (nBillStatus == 1) {
				sMessage.append(vo.getHeadVO().getVpraycode()
						+ m_lanResTool.getStrByID("40040101",
								"UPP40040101-000463")/* 号请购单已经关闭，不能作废！\n" */);
			} else if (nBillStatus == 2) {
				sMessage.append(vo.getHeadVO().getVpraycode()
						+ m_lanResTool.getStrByID("40040101",
								"UPP40040101-000464")/* 号请购单正在审批，不能作废！\n" */);
			} else if (nBillStatus == 3) {
				sMessage.append(vo.getHeadVO().getVpraycode()
						+ m_lanResTool.getStrByID("40040101",
								"UPP40040101-000465")/* 号请购单已经审批，不能作废！\n" */);
			}
			nBillStatus = vo.getHeadVO().getDr();
			if (nBillStatus > 0) {
				sMessage.append(vo.getHeadVO().getVpraycode()
						+ m_lanResTool.getStrByID("40040101",
								"UPP40040101-000466")/* 号请购单已经作废！\n" */);
			}
		}
		if (sMessage.length() > 0) {
			MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
					"40040101", "UPP40040101-000061")/* "请购单作废" */, sMessage
					.toString());
			showHintMessage(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000063")/* "作废单据失败" */);
			return;
		}

		PraybillVO[] tempVOs = new PraybillVO[v.size()];
		v.copyInto(tempVOs);
		try {
			/*
			 * 制造计划定单维护增加“取消下达”功能， 目前生成请购单，删除该单不能取消下达 生成制造模块启用才调用此方法
			 */
			if (PuTool.isProductEnabled(nc.ui.po.pub.PoPublicUIClass
					.getLoginPk_corp(), nc.vo.pub.ProductCode.PROD_MM)) {
				nc.ui.pr.pray.PraybillHelper.onRearOrderDelete(sPraybillIds);
			}

			/* 赋操作员 */
			String strOpr = getClientEnvironment().getUser().getPrimaryKey();
			for (int i = 0; i < m_VOs.length; i++) {
				m_VOs[i].setCurrOperator(strOpr);
				// 为判断是否可修改、作废其他人单据
				m_VOs[i].getHeadVO().setCoperatoridnow(strOpr);
				Logger.debug("Coperator = "
						+ m_VOs[i].getHeadVO().getCoperator());
				Logger.debug("Coperatoridnow = "
						+ m_VOs[i].getHeadVO().getCoperatoridnow());
				m_VOs[i].getHeadVO().setCuserid(strOpr);
			}
			/* 在真正操作发生前才加载表体 */
			tempVOs = PrTool.getRefreshedVOs(tempVOs);
			/* 平台作废调用 */
			PfUtilClient.processBatch("DISCARD", "20", getClientEnvironment()
					.getDate().toString(), tempVOs);
			if (PfUtilClient.isSuccess()) {
				PraybillItemVO[] bodyVO;
				/* 作废成功，修改缓存的单据状态 */
				for (int i = 0; i < nSelected.length; i++) {
					m_VOs[nSelected[i].intValue()].getHeadVO().setDr(1);
					bodyVO = m_VOs[nSelected[i].intValue()].getBodyVO();
					for (int j = 0; j < bodyVO.length; j++)
						bodyVO[j].setDr(1);
				}
			} else {
				showHintMessage(m_lanResTool.getStrByID("40040101",
						"UPP40040101-000063")/* "作废单据失败" */);
				return;
			}
		} catch (nc.vo.pub.BusinessException e) {
			MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
					"40040101", "UPP40040101-000061")/* "请购单作废" */, e
					.getMessage());
			showHintMessage(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000063")/* "作废单据失败" */);
			return;
		} catch (Exception e) {
			MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
					"40040101", "UPP40040101-000061")/* "请购单作废" */, e
					.getMessage());
			SCMEnv.out(e);
			showHintMessage(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000063")/* "作废单据失败" */);
			return;
		}
		tTime = System.currentTimeMillis() - tTime;
		Logger.debug("请购单作废时间：" + tTime + " ms!");
		/* 请购单作废后,不再在界面显示 */
		Vector vTemp = new Vector();
		int vvSize = vv.size();
		int m_VOsLength = m_VOs.length;
		if (bCardShowing) {
			/* 卡片 */
			for (int i = 0; i < m_VOsLength; i++) {
				if (m_VOs[i].getHeadVO().getDr() == 0) {
					vTemp.addElement(m_VOs[i]);
				}
			}
		} else {
			/* 列表 */
			int n;
			for (int i = 0; i < vvSize; i++) {
				n = ((Integer) vv.elementAt(i)).intValue();
				vTemp.addElement(m_VOs[n]);
			}
		}
		int vTempSize = vTemp.size();
		if (vTempSize > 0) {
			if (m_nUIState == 0) {
				if (m_nPresentRecord == m_VOs.length - 1)
					m_nPresentRecord--;
			}
			m_VOs = new PraybillVO[vTempSize];
			vTemp.copyInto(m_VOs);
		} else {
			/* 所有单据已作废 */
			m_VOs = null;
			if (m_nUIState == 0) {
				getBillCardPanel().getBillData().clearViewData();
				getBillCardPanel().updateUI();
				setButtonsCard();
			} else {
				setButtonsList();
				getBillListPanel().getHeadBillModel().clearBodyData();
				getBillListPanel().getBodyBillModel().clearBodyData();
				getBillListPanel().updateUI();
			}
			showHintMessage(m_lanResTool.getStrByID("common", "UCH006")/*
																		 * @res
																		 * "删除成功"
																		 */);
			return;
		}
		/* 部分单据已作废 */
		if (m_nUIState == 0) {
			/* 加载单据到卡片 */
			setVoToBillCard(m_nPresentRecord, null);
			/* 设置按钮逻辑 */
			setButtonsCard();
		} else {
			/* 重新置列表表头显示单据位置 */
			m_nPresentRecord = 0;
			/* 获得该张单据的表体 */
			try {
				m_VOs[m_nPresentRecord] = PrTool
						.getRefreshedVO(m_VOs[m_nPresentRecord]);
			} catch (Exception be) {
				if (be instanceof BusinessException)
					MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
							"SCMCOMMON", "UPPSCMCommon-000422")/* "业务异常" */,
							be.getMessage());
				showHintMessage(m_lanResTool.getStrByID("40040101",
						"UPP40040101-000065")/* "作废单据成功完成后加载单据时出现错误" */);
				return;
			}
			/* 加载列表所有表头及第一张单据表体 */
			Vector vv0 = new Vector();
			m_VOsLength = m_VOs.length;
			for (int i = 0; i < m_VOsLength; i++)
				vv0.addElement(m_VOs[i].getHeadVO());
			if (vv0.size() > 0) {
				PraybillHeaderVO[] headVO = new PraybillHeaderVO[vv.size()];
				vv0.copyInto(headVO);
				getBillListPanel().getHeadBillModel().setBodyDataVO(headVO);
				getBillListPanel().getHeadBillModel().execLoadFormula();
				getBillListPanel().getBodyBillModel().setBodyDataVO(
						m_VOs[m_nPresentRecord].getBodyVO());
				nc.ui.pu.pub.PuTool.loadInvMaxPrice(getBillListPanel());
				getBillListPanel().getBodyBillModel().execLoadFormula();
				getBillListPanel().updateUI();
				/* 置默认显示为第一张 */
				getBillListPanel().getHeadBillModel().setRowState(
						m_nPresentRecord, BillModel.SELECTED);
				getBillListPanel().getHeadTable().setRowSelectionInterval(
						m_nPresentRecord, m_nPresentRecord);
				setButtonsList();
				int headVOLength = headVO.length;
				/* 显示备注 */
				for (int i = 0; i < headVOLength; i++) {
					getBillListPanel().getHeadBillModel().setValueAt(
							headVO[i].getVmemo(), i, "vmemo");
				}
				/* 显示单据来源信息 */
				PuTool.loadSourceInfoAll(getBillListPanel(),
						BillTypeConst.PO_PRAY);
			}
		}
		showHintMessage(m_lanResTool
				.getStrByID("common", "4004COMMON000000068")/* @res "作废成功" */);
	}

	/**
	 * 功能 ：文管管理 适用单据状态：“到货浏览”、“到货列表”
	 */
	private void onDocument() {
		String[] strPks = null;
		String[] strCodes = null;
		HashMap mapBtnPowerVo = new HashMap();
		Integer iBillStatus = null;
		//
		boolean isCard = getBillCardPanel().isDisplayable();
		/* 消息中心视同单据卡片 */
		if (!(getBillCardPanel().isDisplayable() || getBillListPanel()
				.isDisplayable())) {
			isCard = true;
		}
		// 卡片
		if (isCard) {
			if (m_VOs != null && m_VOs.length > 0
					&& m_VOs[m_nPresentRecord] != null
					&& m_VOs[m_nPresentRecord].getParentVO() != null) {
				strPks = new String[] { (String) m_VOs[m_nPresentRecord]
						.getParentVO().getAttributeValue("cpraybillid") };
				strCodes = new String[] { (String) m_VOs[m_nPresentRecord]
						.getParentVO().getAttributeValue("vpraycode") };
				// 处理文档管理框删除按钮是否可用
				BtnPowerVO pVo = new BtnPowerVO(strCodes[0]);
				iBillStatus = PuPubVO.getInteger_NullAs(m_VOs[m_nPresentRecord]
						.getParentVO().getAttributeValue("ibillstatus"),
						new Integer(BillStatus.FREE));
				if (iBillStatus.intValue() == 1 || iBillStatus.intValue() == 2
						|| iBillStatus.intValue() == 3) {
					pVo.setFileDelEnable("false");
				}
				mapBtnPowerVo.put(strCodes[0], pVo);
			}
		}
		// 列表
		final boolean isList = getBillListPanel().isDisplayable();
		if (isList) {
			if (m_VOs != null && m_VOs.length > 0) {
				PraybillHeaderVO[] headers = null;
				headers = (PraybillHeaderVO[]) getBillListPanel()
						.getHeadBillModel().getBodySelectedVOs(
								PraybillHeaderVO.class.getName());
				if (headers == null || headers.length <= 0) {
					showHintMessage(m_lanResTool.getStrByID("40040101",
							"UPP40040101-000066")/* "没有正确获取单据号,不能进行文档管理" */);
					return;
				}
				strPks = new String[headers.length];
				strCodes = new String[headers.length];
				BtnPowerVO pVo = null;
				for (int i = 0; i < headers.length; i++) {
					strPks[i] = headers[i].getPrimaryKey();
					strCodes[i] = headers[i].getVpraycode();
					// 处理文档管理框删除按钮是否可用
					pVo = new BtnPowerVO(strCodes[i]);
					iBillStatus = PuPubVO.getInteger_NullAs(headers[i]
							.getIbillstatus(), new Integer(0));
					if (iBillStatus.intValue() == 1
							|| iBillStatus.intValue() == 2
							|| iBillStatus.intValue() == 3) {
						pVo.setFileDelEnable("false");
					}
					mapBtnPowerVo.put(strCodes[i], pVo);
				}
			}
		}
		if (strPks == null || strPks.length <= 0) {
			showHintMessage(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000066")/* "没有正确获取单据号,不能进行文档管理" */);
			return;
		}
		// 调用文档管理对话框
		nc.ui.scm.file.DocumentManager.showDM(this, strPks, strCodes,
				mapBtnPowerVo);
		showHintMessage(m_lanResTool
				.getStrByID("common", "4004COMMON000000025")/* @res "文档管理成功" */);
	}

	/**
	 * 功能描述:首页
	 */
	private void onFirst() {
		/* 加载单据 */
		final int iRollBackPos = m_nPresentRecord;
		m_nPresentRecord = 0;
		setVoToBillCard(iRollBackPos, m_lanResTool.getStrByID("SCMCOMMON",
				"UPPSCMCommon-000248")/* "首张" */);
		/* 设置按钮逻辑 */
		setButtonsCard();
		getBillCardPanel().setEnabled(false);
		showHintMessage(m_lanResTool
				.getStrByID("common", "4004COMMON000000026")/* @res "成功显示首页" */);
	}

	/**
	 * 功能描述:插行
	 */
	private void onInsertLine() {
		int nSelectedRow = getBillCardPanel().getBillTable().getSelectedRow();
		if (!getBillCardPanel().insertLine()) {
			MessageDialog.showHintDlg(this, m_lanResTool.getStrByID("40040101",
					"UPP40040101-000068")/* "插行" */, m_lanResTool.getStrByID(
					"40040101", "UPP40040101-000059")/* "没有选中请购单表体行！" */);
			return;
		}
		/* 处理项目自动协带 */
		nc.ui.pu.pub.PuTool.setBodyProjectByHeadProject(getBillCardPanel(),
				"cprojectidhead", "cprojectid", "cprojectname",
				nc.vo.scm.pu.PuBillLineOprType.INSERT);
		/* 处理行号 */
		BillRowNo.insertLineRowNo(getBillCardPanel(),
				nc.vo.scm.pu.BillTypeConst.PO_PRAY, "crowno");

		if (nSelectedRow >= 0) {
			getBillCardPanel().setBodyValueAt(m_sLoginCorpId, nSelectedRow,
					"pk_reqcorp");
			getBillCardPanel().setBodyValueAt(
					getClientEnvironment().getCorporation().getUnitname(),
					nSelectedRow, "reqcorpname");
			getBillCardPanel().setBodyValueAt(m_sLoginCorpId, nSelectedRow,
					"pk_purcorp");
			getBillCardPanel().setBodyValueAt(
					getClientEnvironment().getCorporation().getUnitname(),
					nSelectedRow, "purcorpname");
		}

		// setAssisUnitEditState();
		m_bCancel = false;
		m_nUIState = 0;
		updateButtonsAll();

		// 输入提示 czp 20050303 力帆
		PraybillVO voCurr = (PraybillVO) getBillCardPanel().getBillValueVO(
				PraybillVO.class.getName(), PraybillHeaderVO.class.getName(),
				PraybillItemVO.class.getName());
		nc.ui.po.pub.InvAttrCellRenderer ficr = new nc.ui.po.pub.InvAttrCellRenderer();
		ficr.setFreeItemRenderer(getBillCardPanel(), voCurr);
		showHintMessage(m_lanResTool.getStrByID("common", "UCH038")/*
																	 * @res
																	 * "插入行成功"
																	 */);

	}

	/**
	 * 功能描述:末页
	 */
	private void onLast() {
		/* 加载单据 */
		final int iRollBackPos = m_nPresentRecord;
		m_nPresentRecord = m_VOs.length - 1;
		setVoToBillCard(iRollBackPos, m_lanResTool.getStrByID("SCMCOMMON",
				"UPPSCMCommon-000177")/* "末张" */);
		/* 设置按钮逻辑 */
		setButtonsCard();
		getBillCardPanel().setEnabled(false);
		showHintMessage(m_lanResTool
				.getStrByID("common", "4004COMMON000000029")/* @res "成功显示末页" */);
	}

	/**
	 * 功能描述:卡片到列表
	 */
	private void onList() {
		showHintMessage("");
		int iSortCol = getBillCardPanel().getBillModel().getSortColumn();
		boolean bSortAsc = getBillCardPanel().getBillModel().isSortAscending();
		getBillCardPanel().setVisible(false);
		if (!m_bLoaded) {
			add(getBillListPanel(), java.awt.BorderLayout.CENTER);
			m_bLoaded = true;
		}
		getBillListPanel().setVisible(true);
		/* 隐藏部分字段 */
		/**
		 * v5 库存组织（表头栏目）：所有相关处理删除
		 * getBillListPanel().hideHeadTableCol("cstoreorganization");
		 */
		getBillListPanel().hideHeadTableCol("cbiztype");
		getBillListPanel().hideHeadTableCol("cdeptid");
		getBillListPanel().hideHeadTableCol("cpraypsn");
		getBillListPanel().hideHeadTableCol("coperator");
		getBillListPanel().hideHeadTableCol("cauditpsn");
		getBillListPanel().hideHeadTableCol("ccustomerid");
		if (m_VOs != null && m_VOs.length > 0) {
			/* 显示卡片状态的当前单据 */
			Vector v = new Vector();
			for (int i = 0; i < m_VOs.length; i++) {
				v.addElement(m_VOs[i].getHeadVO());
			}
			PraybillHeaderVO[] hVO = new PraybillHeaderVO[v.size()];
			v.copyInto(hVO);

			PraybillItemVO[] bVO = m_VOs[m_nPresentRecord].getBodyVO();

			getBillListPanel().getBillListData().getHeadItem("ipraysource")
					.setWithIndex(true);
			m_comPraySource1 = (UIComboBox) getBillListPanel()
					.getBillListData().getHeadItem("ipraysource")
					.getComponent();
			m_comPraySource1.removeAllItems();
			m_comPraySource1.setTranslate(true);
			m_comPraySource1.addItem("MRP");
			m_comPraySource1.addItem("MO");
			m_comPraySource1.addItem("SFC");
			m_comPraySource1.addItem(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000458")/* "销售订单" */);
			m_comPraySource1.addItem(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000459")/* "库存订货点" */);
			m_comPraySource1.addItem(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000460")/* "手工录入" */);
			m_comPraySource1.addItem("DRP");
			m_comPraySource1.addItem(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000461")/* "调拨申请" */);
			m_comPraySource1.addItem(m_lanResTool.getStrByID("4004pub",
					"UPP4004pub-000204") /* "物资需求申请" */);

			getBillListPanel().getBillListData().getHeadItem("ipraytype")
					.setWithIndex(true);
			m_comPrayType1 = (UIComboBox) getBillListPanel().getBillListData()
					.getHeadItem("ipraytype").getComponent();
			m_comPrayType1.removeAllItems();
			m_comPrayType1.setTranslate(true);
			m_comPrayType1.addItem(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000454")/* "外包＿代料" */);
			m_comPrayType1.addItem(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000455")/* "外包＿不代料" */);
			m_comPrayType1.addItem(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000456")/* "采购" */);
			m_comPrayType1.addItem(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000457")/* "外协" */);

			Integer nTemp1 = (Integer) m_VOs[m_nPresentRecord].getParentVO()
					.getAttributeValue("ipraysource");
			Integer nTemp2 = (Integer) m_VOs[m_nPresentRecord].getParentVO()
					.getAttributeValue("ipraytype");
			m_comPraySource1.setSelectedIndex(nTemp1.intValue());
			m_comPrayType1.setSelectedIndex(nTemp2.intValue());

			getBillListPanel().getHeadBillModel().setBodyDataVO(hVO);
			getBillListPanel().getHeadBillModel().execLoadFormula();
			getBillListPanel().getBodyBillModel().setBodyDataVO(bVO);
			nc.ui.pu.pub.PuTool.loadInvMaxPrice(getBillListPanel());
			getBillListPanel().getBodyBillModel().execLoadFormula();
			getBillListPanel().getHeadBillModel().updateValue();
			getBillListPanel().getBodyBillModel().updateValue();
			getBillListPanel().updateUI();
			/* 列表显示单据处理 */
			getBillListPanel().getHeadBillModel().setRowState(m_nPresentRecord,
					BillModel.SELECTED);
			getBillListPanel().getHeadTable().setRowSelectionInterval(
					m_nPresentRecord, m_nPresentRecord);
			/* 显示来源信息（列表） */
			PuTool.loadSourceInfoAll(getBillListPanel(), BillTypeConst.PO_PRAY);
			/* 显示备注 */
			int mVOsLength = m_VOs.length;
			for (int i = 0; i < mVOsLength; i++) {
				getBillListPanel().getHeadBillModel().setValueAt(
						hVO[i].getVmemo(), i, "vmemo");
			}
			if (iSortCol >= 0) {
				getBillListPanel().getBodyBillModel().sortByColumn(iSortCol,
						bSortAsc);
			}
		} else {
			getBillListPanel().getHeadBillModel().clearBodyData();
			getBillListPanel().getBodyBillModel().clearBodyData();
			getBillListPanel().updateUI();
		}
		/* 列表按钮逻辑 */
		setButtonsList();
		m_nUIState = 1;
		getBillListPanel().setEnabled(false);
		showHintMessage(m_lanResTool.getStrByID("common", "UCH022")/*
																	 * @res
																	 * "列表显示"
																	 */);
	}

	/**
	 * 功能描述:列表状态查询
	 */
	private void onListQuery() {
		showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
				"UPPSCMCommon-000360")/* @res"开始查询..." */);
		// 显示对话框
		getQueryDlg().showModal();
		if (getQueryDlg().isCloseOK()) {
			m_bQueried = true;
			onListRefresh();
		}

		showHintMessage(m_lanResTool.getStrByID("common", "UCH009")/*
																	 * @res
																	 * "查询完成"
																	 */);
	}

	/* 列表刷新 */
	private void onListRefresh() {
		// 获取请购单查询条件
		ConditionVO[] conditionVO = getQueryDlg().getConditionVO();
		String strSubSql = getQueryDlg().getSubSQL();
		try {
			m_VOs = PraybillHelper.queryAll(getQueryDlg()
					.getSelectedCorpIdString(), conditionVO, getQueryDlg()
					.getStatusCndStr(), null, strSubSql);
		} catch (Exception e) {
			MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
					"40040101", "UPP40040101-000061")/* @res"请购单作废" */, e
					.getMessage());
			return;
		}
		if (m_VOs != null && m_VOs.length > 0) {
			// 重置缓存位置
			m_nPresentRecord = 0;
			try {
				m_VOs[m_nPresentRecord] = PrTool
						.getRefreshedVO(m_VOs[m_nPresentRecord]);
				// 计算自由项
				new nc.ui.scm.pub.FreeVOParse().setFreeVO(
						m_VOs[m_nPresentRecord].getBodyVO(), "vfree", "vfree",
						"cbaseid", "cmangid", true);
			} catch (Exception be) {
				if (be instanceof BusinessException)
					MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
							"SCMCOMMON", "UPPSCMCommon-000422")/* "业务异常" */,
							be.getMessage());
				return;
			}
			// 返回查询结果
			Vector v = new Vector();
			for (int i = 0; i < m_VOs.length; i++)
				v.addElement(m_VOs[i].getHeadVO());
			PraybillHeaderVO[] hVO = new PraybillHeaderVO[v.size()];
			v.copyInto(hVO);

			initListComboBox();

			Integer nTemp1 = (Integer) m_VOs[0].getParentVO()
					.getAttributeValue("ipraysource");
			Integer nTemp2 = (Integer) m_VOs[0].getParentVO()
					.getAttributeValue("ipraytype");
			m_comPraySource1.setSelectedIndex(nTemp1.intValue());
			m_comPrayType1.setSelectedIndex(nTemp2.intValue());

			// 选择第一行
			getBillListPanel().getHeadBillModel().setBodyDataVO(hVO);
			getBillListPanel().getHeadBillModel().execLoadFormula();
			getBillListPanel().getHeadBillModel().updateValue();

			// 列表显示单据处理
			getBillListPanel().getHeadBillModel().setRowState(m_nPresentRecord,
					BillModel.SELECTED);
			getBillListPanel().getHeadTable().setRowSelectionInterval(
					m_nPresentRecord, m_nPresentRecord);
			Logger.debug("$$---$---$$:" + m_nPresentRecord);
			getBillListPanel().updateUI();

			// 显示备注
			for (int i = 0; i < hVO.length; i++) {
				getBillListPanel().getHeadBillModel().setValueAt(
						hVO[i].getVmemo(), i, "vmemo");
			}
		} else {
			m_VOs = null;
			// 无返回查询结果
			MessageDialog.showHintDlg(this, m_lanResTool.getStrByID("40040101",
					"UPP40040101-000055")/* "请购单查询" */, m_lanResTool.getStrByID(
					"40040101", "UPP40040101-000056")/* "没有符合条件的请购单！" */);
			// 清空数据
			getBillListPanel().getHeadBillModel().clearBodyData();
			getBillListPanel().getBodyBillModel().clearBodyData();
			getBillListPanel().updateUI();
		}
		// 列表按钮逻辑
		setButtonsList();
		m_nUIState = 1;
		updateButtonsAll();
		getBillCardPanel().setEnabled(false);

		showHintMessage(m_lanResTool.getStrByID("common", "UCH007")/*
																	 * @res
																	 * "刷新成功"
																	 */);
	}

	/**
	 * 单据逐级联查
	 */
	private void onLinkQuery() {
		PraybillVO vo = m_VOs[m_nPresentRecord];
		if (vo == null || vo.getParentVO() == null)
			return;
		nc.ui.scm.sourcebill.SourceBillFlowDlg soureDlg = new nc.ui.scm.sourcebill.SourceBillFlowDlg(
				this, nc.vo.scm.pu.BillTypeConst.PO_PRAY,
				((PraybillHeaderVO) vo.getParentVO()).getPrimaryKey(), null,
				getClientEnvironment().getUser().getPrimaryKey(),
				m_sLoginCorpId);
		soureDlg.showModal();
		showHintMessage(m_lanResTool
				.getStrByID("common", "4004COMMON000000019")/* @res "联查成功" */);
	}

	/**
	 * 功能描述:处理浮动菜单
	 * 
	 * @param event
	 *            nc.ui.pub.bill.BillEditEvent
	 */
	public void onMenuItemClick(java.awt.event.ActionEvent event) {
		UIMenuItem menuItem = (UIMenuItem) event.getSource();

		if (menuItem.equals(getBillCardPanel().getAddLineMenuItem()))
			onAppendLine(getBillCardPanel(), this);

		if (menuItem.equals(getBillCardPanel().getCopyLineMenuItem())) {
			m_bIsSubMenuPressed = true;
			onCopyLine();
			m_bIsSubMenuPressed = false;
		}

		if (menuItem.equals(getBillCardPanel().getDelLineMenuItem()))
			onDeleteLine();

		if (menuItem.equals(getBillCardPanel().getInsertLineMenuItem()))
			onInsertLine();

		if (menuItem.equals(getBillCardPanel().getPasteLineMenuItem()))
			onPasteLine();
		if (menuItem.equals(getBillCardPanel().getPasteLineToTailMenuItem()))
			onPasteLineToTail();
	}

	/**
	 * 功能描述:更改
	 */
	private void onModify(boolean bListShowing) {
		if (bListShowing) {
			// 由列表转为卡片
			int rowCount = getBillListPanel().getHeadBillModel().getRowCount();
			int iList;
			int iCard;
			for (int i = 0; i < rowCount; i++) {
				if (getBillListPanel().getHeadBillModel().getRowState(i) == BillModel.SELECTED) {
					m_nPresentRecord = i;
					if (getBillListPanel().getHeadBillModel() != null
							&& getBillListPanel().getHeadBillModel()
									.getSortIndex() != null) {
						iList = m_nPresentRecord;
						iCard = getBillListPanel().getHeadBillModel()
								.getSortIndex()[iList];
						m_nPresentRecord = iCard;
					}
					break;
				}
			}
			if (m_nPresentRecord < 0) {
				MessageDialog.showHintDlg(this, m_lanResTool.getStrByID(
						"common", "UC001-0000045")/* @res"修改" */, m_lanResTool
						.getStrByID("40040101", "UPP40040101-000446")/*
																	 * @res"没有选中请购单表头行！"
																	 */);
				return;
			}
			getBillListPanel().setVisible(false);
			getBillCardPanel().setVisible(true);

			Integer nTemp1 = (Integer) m_VOs[m_nPresentRecord].getParentVO()
					.getAttributeValue("ipraysource");
			Integer nTemp2 = (Integer) m_VOs[m_nPresentRecord].getParentVO()
					.getAttributeValue("ipraytype");
			m_comPraySource.setSelectedIndex(nTemp1.intValue());
			m_comPrayType.setSelectedIndex(nTemp2.intValue());
			// 显示卡片单据
			getBillCardPanel().setBillValueVO(m_VOs[m_nPresentRecord]);
			nc.ui.pu.pub.PuTool.loadInvMaxPrice(getBillCardPanel());
			getBillCardPanel().getBillModel().execLoadFormula();
			// 加载来源单据信息
			nc.ui.pu.pub.PuTool.loadSourceInfoAll(getBillCardPanel(),
					nc.vo.scm.pu.BillTypeConst.PO_PRAY);
			getBillCardPanel().updateValue();
			getBillCardPanel().updateUI();
			// 显示备注
			UIRefPane nRefPanel3 = (UIRefPane) getBillCardPanel().getHeadItem(
					"vmemo").getComponent();
			nRefPanel3.setValue(m_VOs[m_nPresentRecord].getHeadVO().getVmemo());
		}
		// 已经审批的请购单不能修改
		int nBillStatus = 0;
		if (m_VOs[m_nPresentRecord].getHeadVO().getIbillstatus() != null) {
			nBillStatus = m_VOs[m_nPresentRecord].getHeadVO().getIbillstatus()
					.intValue();
		}
		if (nBillStatus == 3) {
			MessageDialog
					.showHintDlg(this, m_lanResTool.getStrByID("40040101",
							"UPP40040101-000467")/* "请购单修改" */,
							m_VOs[m_nPresentRecord].getHeadVO().getVpraycode()
									+ m_lanResTool.getStrByID("40040101",
											"UPP40040101-000468")/*
																 * @res "
																 * 号请购单已经审批
																 * ，不能修改！
																 * 请刷新界面再试"
																 */);
			return;
		}
		if (nBillStatus == 1) {
			MessageDialog
					.showHintDlg(this, m_lanResTool.getStrByID("40040101",
							"UPP40040101-000467")/* "请购单修改" */,
							m_VOs[m_nPresentRecord].getHeadVO().getVpraycode()
									+ m_lanResTool.getStrByID("40040101",
											"UPP40040101-000469")/*
																 * @res "
																 * 号请购单已经关闭
																 * ，不能修改！
																 * 请刷新界面再试"
																 */);
			return;
		}
		getBillCardPanel().setEnabled(true);
		showHintMessage(m_lanResTool
				.getStrByID("common", "4004COMMON000000030")/* @res "正在修改" */);
		// ***************fangy add 2002-12-04 begin****************/
		int rowCount = getBillCardPanel().getRowCount();
		for (int i = 0; i < rowCount; i++) {
			setVProduceNumEditState(i);
		}
		// ***************fangy add 2002-12-04 end******************/
		// 请购单号不可编辑
		// BillItem item = getBillCardPanel().getHeadItem("vpraycode");
		// item.setEnabled(false);
		// 修改请购单时，需求日期应该可以修改。
		BillItem ddemanddate = getBillCardPanel().getBodyItem("ddemanddate");
		ddemanddate.setEnabled(true);

		// yux 项目可以修改
		BillItem cprojectname = getBillCardPanel().getBodyItem("cprojectname");
		cprojectname.setEnabled(true);

		setPartNoEditable(getBillCardPanel());
		// setAssisUnitEditState();
		m_bEdit = true;
		m_bCancel = false;
		setButtonsCardModify();
		m_nUIState = 0;
		updateButtonsAll();

		/** 设置右键菜单与按钮组“行操作”权限相同 */
		setPopMenuItemsEnable();
		// 过滤存货参照,保证在业务类型是受托代销业务类型时替换件参照是受托代销属性

		// 取业务类型
		String Cbiztype = m_VOs[m_nPresentRecord].getHeadVO().getCbiztype();
		if (Cbiztype == null) {
			if (m_bizButton.getTag() != null
					&& m_bizButton.getTag().trim() != "") {
				m_VOs[m_nPresentRecord].getHeadVO().setCbiztype(
						m_bizButton.getTag());
			}
		}
		boolean checker = false;
		try {
			if (!m_hashBizType.containsKey(Cbiztype)) {
				Object[] oaTemp = (Object[]) CacheTool.getCellValue(
						"bd_busitype", "pk_busitype", "verifyrule", Cbiztype);
				if (oaTemp != null && oaTemp.length > 0 && oaTemp[0] != null) {
					checker = "S".equalsIgnoreCase(oaTemp[0].toString().trim());
				}
			} else {
				checker = new UFBoolean(m_hashBizType.get(Cbiztype).toString())
						.booleanValue();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 过滤存货参照
		if (checker) {
			String sql = " and (sellproxyflag = 'Y')";
			UIRefPane refCinventorycode = (UIRefPane) (getBillCardPanel()
					.getBodyItem("cinventorycode").getComponent());
			refCinventorycode.getRefModel().addWherePart(sql);
			if (!m_hashBizType.containsKey(Cbiztype)) {
				m_hashBizType.put(Cbiztype, String.valueOf(checker));
			}
		} else {
			String sql = " and ( 1 =1 )";
			UIRefPane refCinventorycode = (UIRefPane) (getBillCardPanel()
					.getBodyItem("cinventorycode").getComponent());
			refCinventorycode.getRefModel().addWherePart(sql);
		}
		// 置光标到表头第一个可编辑项目
		getBillCardPanel().transferFocusTo(BillCardPanel.HEAD);
		showHintMessage(m_lanResTool
				.getStrByID("common", "4004COMMON000000030")/* @res "正在修改" */);

		// 晁志平 增加重庆力帆自由项输入提示功能
		nc.ui.po.pub.InvAttrCellRenderer ficr = new nc.ui.po.pub.InvAttrCellRenderer();
		// 从当前界面取值，因为可能排序过序与缓存不一致：ficr.setFreeItemRenderer(getBillCardPanel(),
		// m_VOs[m_nPresentRecord]);
		PraybillVO voCurr = (PraybillVO) getBillCardPanel().getBillValueVO(
				PraybillVO.class.getName(), PraybillHeaderVO.class.getName(),
				PraybillItemVO.class.getName());
		ficr.setFreeItemRenderer(getBillCardPanel(), voCurr);
		getBillCardPanel().updateUI();
		// 置光标到表头第一个可编辑项目
		getBillCardPanel().transferFocusTo(BillCardPanel.HEAD);
		//
		showHintMessage(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000534")/* @res "编辑单据" */);
	}

	/**
	 * 作者：晁志平 功能：设置单据卡片右键菜单行操作与按钮组“行操作”权限相同 参数：无 返回：无 例外：无 日期：(2004-11-26
	 * 10:06:21) 修改日期，修改人，修改原因，注释标志：
	 */

	private void setPopMenuItemsEnable() {
		// 没有分配行操作权限
		if (m_btnLines == null || m_btnLines.getChildCount() == 0) {
			getBillCardPanel().getBodyPanel().getMiAddLine().setEnabled(false);
			getBillCardPanel().getBodyPanel().getMiCopyLine().setEnabled(false);
			getBillCardPanel().getBodyPanel().getMiDelLine().setEnabled(false);
			getBillCardPanel().getBodyPanel().getMiInsertLine().setEnabled(
					false);
			getBillCardPanel().getBodyPanel().getMiPasteLine()
					.setEnabled(false);
			getBillCardPanel().getBodyPanel().getMiPasteLineToTail()
					.setEnabled(false);
		}
		// 分配行操作权限
		else {
			getBillCardPanel().getBodyPanel().getMiAddLine().setEnabled(
					m_btnAddLine.isPower());
			getBillCardPanel().getBodyPanel().getMiCopyLine().setEnabled(
					m_btnCpyLine.isPower());
			getBillCardPanel().getBodyPanel().getMiDelLine().setEnabled(
					m_btnDelLine.isPower());
			getBillCardPanel().getBodyPanel().getMiInsertLine().setEnabled(
					m_btnInsLine.isPower());
			getBillCardPanel().getBodyPanel().getMiPasteLine().setEnabled(
					m_btnPstLine.isPower());
			// 粘贴到行尾与粘贴可用性逻辑相同
			getBillCardPanel().getBodyPanel().getMiPasteLineToTail()
					.setEnabled(m_btnPstLine.isPower());

		}
	}

	/**
	 * 功能描述:下页
	 */
	private void onNext() {
		/* 加载单据 */
		final int iRollBackPos = m_nPresentRecord;
		m_nPresentRecord++;
		setVoToBillCard(iRollBackPos, m_lanResTool.getStrByID("SCMCOMMON",
				"UPPSCMCommon-000281")/* "下张" */);
		/* 设置按钮逻辑 */
		setButtonsCard();
		getBillCardPanel().setEnabled(false);
		//
		showHintMessage(m_lanResTool
				.getStrByID("common", "4004COMMON000000028")/* @res "成功显示下一页" */);

	}

	/**
	 * 功能描述:打开当前的请购单--列表
	 */
	private void onOpenList() {
		this.showHintMessage(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000069")/* "正在打开单据..." */);
		m_VOs[m_nPresentRecord].getHeadVO().setCuserid(
				getClientEnvironment().getUser().getPrimaryKey());
		long tTime = System.currentTimeMillis();
		try {
			// 赋操作员
			String strOpr = getClientEnvironment().getUser().getPrimaryKey();
			m_VOs[m_nPresentRecord].setCurrOperator(strOpr);
			ArrayList keys = (ArrayList) PfUtilClient
					.processActionNoSendMessage(this, "OPEN",
							nc.vo.scm.pu.BillTypeConst.PO_PRAY,
							getClientEnvironment().getDate().toString(),
							m_VOs[m_nPresentRecord], null, null, null);
			m_VOs[m_nPresentRecord] = (PraybillVO) keys.get(0);
			// 打开单据时恢复到审批状态
			m_VOs[m_nPresentRecord].getHeadVO().setIbillstatus(new Integer(3));
			// 单据关闭后金额被清空
			if (m_VOs[m_nPresentRecord] != null
					&& m_VOs[m_nPresentRecord].getBodyVO() != null) {
				int itemsLength = m_VOs[m_nPresentRecord].getBodyVO().length;
				UFDouble nMoney = null;
				UFDouble nPrayNum = null;
				UFDouble nSuggestPrice = null;

				for (int i = 0; i < itemsLength; i++) {
					nMoney = m_VOs[m_nPresentRecord].getBodyVO()[i].getNmoney();
					nPrayNum = m_VOs[m_nPresentRecord].getBodyVO()[i]
							.getNpraynum();
					nSuggestPrice = m_VOs[m_nPresentRecord].getBodyVO()[i]
							.getNsuggestprice();
					if (nMoney == null && nPrayNum != null
							&& nSuggestPrice != null) {
						nMoney = new UFDouble(nPrayNum.doubleValue()
								* nSuggestPrice.doubleValue());
					} else {
						nMoney = new UFDouble(0);
					}
					m_VOs[m_nPresentRecord].getBodyVO()[i].setNmoney(nMoney);
				}
			}
		} catch (Exception e) {
			MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
					"40040101", "UPP40040101-000070")/* "请购单打开" */, e
					.getMessage());
			showHintMessage(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000071")/* "打开失败" */);
			return;
		}
		tTime = System.currentTimeMillis() - tTime;
		Logger.debug("请购单打开时间：" + tTime + " ms!");
		m_nUIState = 0;
		//
		getBillListPanel().getHeadBillModel().setBodyRowVO(
				m_VOs[m_nPresentRecord].getHeadVO(), m_nPresentRecord);
		getBillListPanel().getBodyBillModel().setBodyDataVO(
				m_VOs[m_nPresentRecord].getBodyVO());
		updateUI();
		/* 刷新按钮状态 */
		setButtonsList();
		//
		showHintMessage(m_lanResTool.getStrByID("common", "UCH012")/*
																	 * @res
																	 * "已打开"
																	 */);
		return;
	}

	/**
	 * 功能描述:打开当前的请购单
	 */
	private void onOpenCard() {
		this.showHintMessage(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000069")/* "正在打开单据..." */);
		m_VOs[m_nPresentRecord].getHeadVO().setCuserid(
				getClientEnvironment().getUser().getPrimaryKey());
		long tTime = System.currentTimeMillis();
		try {
			// 赋操作员
			String strOpr = getClientEnvironment().getUser().getPrimaryKey();
			m_VOs[m_nPresentRecord].setCurrOperator(strOpr);
			ArrayList keys = (ArrayList) PfUtilClient
					.processActionNoSendMessage(this, "OPEN",
							nc.vo.scm.pu.BillTypeConst.PO_PRAY,
							getClientEnvironment().getDate().toString(),
							m_VOs[m_nPresentRecord], null, null, null);
			m_VOs[m_nPresentRecord] = (PraybillVO) keys.get(0);
			// 打开单据时恢复到审批状态
			m_VOs[m_nPresentRecord].getHeadVO().setIbillstatus(new Integer(3));
			// 单据关闭后金额被清空
			if (m_VOs[m_nPresentRecord] != null
					&& m_VOs[m_nPresentRecord].getBodyVO() != null) {
				int itemsLength = m_VOs[m_nPresentRecord].getBodyVO().length;
				UFDouble nMoney = null;
				UFDouble nPrayNum = null;
				UFDouble nSuggestPrice = null;

				for (int i = 0; i < itemsLength; i++) {
					nMoney = m_VOs[m_nPresentRecord].getBodyVO()[i].getNmoney();
					nPrayNum = m_VOs[m_nPresentRecord].getBodyVO()[i]
							.getNpraynum();
					nSuggestPrice = m_VOs[m_nPresentRecord].getBodyVO()[i]
							.getNsuggestprice();
					if (nMoney == null && nPrayNum != null
							&& nSuggestPrice != null) {
						nMoney = new UFDouble(nPrayNum.doubleValue()
								* nSuggestPrice.doubleValue());
					} else {
						nMoney = new UFDouble(0);
					}
					m_VOs[m_nPresentRecord].getBodyVO()[i].setNmoney(nMoney);
				}
			}
		} catch (Exception e) {
			MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
					"40040101", "UPP40040101-000070")/* "请购单打开" */, e
					.getMessage());
			showHintMessage(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000071")/* "打开失败" */);
			return;
		}
		tTime = System.currentTimeMillis() - tTime;
		Logger.debug("请购单打开时间：" + tTime + " ms!");
		m_nUIState = 0;
		/* 刷新按钮状态 */
		setButtonsCard();
		/* 加载单据 */
		setVoToBillCard(m_nPresentRecord, m_lanResTool.getStrByID("40040101",
				"UPP40040101-000449")/* "被打开" */);
		//
		showHintMessage(m_lanResTool.getStrByID("common", "UCH012")/*
																	 * @res
																	 * "已打开"
																	 */);
		return;
	}

	/**
	 * 功能描述:粘贴行
	 */
	private void onPasteLine() {
		/* 粘贴前的行数 */
		final int iOrgRowCount = getBillCardPanel().getRowCount();
		getBillCardPanel().pasteLine();

		/* 增加的行数 */
		final int iPastedRowCount = getBillCardPanel().getRowCount()
				- iOrgRowCount;

		/* 处理行号 */
		BillRowNo.pasteLineRowNo(getBillCardPanel(),
				nc.vo.scm.pu.BillTypeConst.PO_PRAY, "crowno", iPastedRowCount);

		final int iEndRow = getBillCardPanel().getBillTable().getSelectedRow() - 1;
		final int iBeginRow = iEndRow - iPastedRowCount + 1;
		/* 清空新增时不必要的信息 */
		for (int iRow = iBeginRow; iRow <= iEndRow; iRow++) {
			getBillCardPanel().getBillModel().setRowState(iRow, BillModel.ADD);
			getBillCardPanel().setBodyValueAt(null, iRow, "cpraybill_bid");
			getBillCardPanel().setBodyValueAt(null, iRow, "cpraybillid");
			getBillCardPanel().setBodyValueAt(null, iRow, "naccumulatenum");

			getBillCardPanel().setBodyValueAt(null, iRow, "ts");
		}
		// setAssisUnitEditState();
		m_bCancel = false;
		m_nUIState = 0;
		updateButtonsAll();
		// 输入提示 czp 20050303 力帆
		PraybillVO voCurr = (PraybillVO) getBillCardPanel().getBillValueVO(
				PraybillVO.class.getName(), PraybillHeaderVO.class.getName(),
				PraybillItemVO.class.getName());
		nc.ui.po.pub.InvAttrCellRenderer ficr = new nc.ui.po.pub.InvAttrCellRenderer();
		ficr.setFreeItemRenderer(getBillCardPanel(), voCurr);
		showHintMessage(m_lanResTool.getStrByID("common", "UCH040")/*
																	 * @res
																	 * "粘贴行成功"
																	 */);

	}

	/**
	 * 此处插入方法说明。 创建日期：(2003-11-10 11:19:18)
	 */
	private void onPasteLineToTail() {
		final int iOldRowCnt = getBillCardPanel().getRowCount();
		getBillCardPanel().pasteLineToTail();
		final int iNewRowCnt = getBillCardPanel().getRowCount();
		if (iOldRowCnt > 0 && iNewRowCnt > 0 && iOldRowCnt == iNewRowCnt)
			showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
					"UPPSCMCommon-000424")/* "粘贴行到行尾未成功,可能原因：没有拷贝内容或未确定要粘贴的位置" */);
		else {
			for (int iRow = iOldRowCnt; iRow < iNewRowCnt; iRow++) {
				getBillCardPanel().getBillModel().setRowState(iRow,
						BillModel.ADD);
				getBillCardPanel().setBodyValueAt(null, iRow, "cpraybill_bid");
				getBillCardPanel().setBodyValueAt(null, iRow, "cpraybillid");
				getBillCardPanel().setBodyValueAt(null, iRow, "naccumulatenum");

				getBillCardPanel().setBodyValueAt(null, iRow, "ts");
			}
			String[] value = new String[] { String.valueOf(iNewRowCnt
					- iOldRowCnt) };
			showHintMessage(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000439", null, value));
			// 单据行号
			BillRowNo.addLineRowNos(getBillCardPanel(),
					nc.vo.scm.pu.BillTypeConst.PO_PRAY, "crowno", iNewRowCnt
							- iOldRowCnt);
		}
		// 输入提示 czp 20050303 力帆
		PraybillVO voCurr = (PraybillVO) getBillCardPanel().getBillValueVO(
				PraybillVO.class.getName(), PraybillHeaderVO.class.getName(),
				PraybillItemVO.class.getName());
		nc.ui.po.pub.InvAttrCellRenderer ficr = new nc.ui.po.pub.InvAttrCellRenderer();
		ficr.setFreeItemRenderer(getBillCardPanel(), voCurr);
		showHintMessage(m_lanResTool.getStrByID("common", "UCH040")/*
																	 * @res
																	 * "粘贴行成功"
																	 */);
	}

	/**
	 * 功能描述:上页
	 */
	private void onPrevious() {
		/* 加载单据 */
		final int iRollBackPos = m_nPresentRecord;
		m_nPresentRecord--;
		setVoToBillCard(iRollBackPos, m_lanResTool.getStrByID("SCMCOMMON",
				"UPPSCMCommon-000232")/* "上张" */);
		/* 设置按钮逻辑 */
		setButtonsCard();
		getBillCardPanel().setEnabled(false);
		showHintMessage(m_lanResTool
				.getStrByID("common", "4004COMMON000000027")/* @res "成功显示上一页" */);
	}

	/**
	 * 功能：价格论证表 参数： 返回： 例外： 日期：(2002-9-23 11:49:32) 修改日期，修改人，修改原因，注释标志：
	 */
	private void onPriceInfos() {
		// 显示查询对话框
		if (m_priceDlg == null) {
			m_priceDlg = new QueryConditionClient();
			m_priceDlg.setTempletID("40040103000000000000");
			m_priceDlg.hideNormal();
		}
		m_priceDlg.showModal();
		// 查询
		PriceInfosVO[] vos = null;
		if (m_priceDlg.isCloseOK()) {
			String wherePart = m_priceDlg.getWhereSQL();
			// 取得存货信息
			Vector tempV = new Vector();
			Object omangid = null;
			String cmangid = null;
			for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
				omangid = getBillCardPanel().getBodyValueAt(i, "cmangid");
				cmangid = null;
				if (omangid != null && omangid.toString().length() > 0) {
					cmangid = omangid.toString();
					tempV.addElement(cmangid);
				}
			}
			String[] cmangids = new String[tempV.size()];
			tempV.copyInto(cmangids);
			try {
				vos = PraybillHelper.queryPriceInfos(cmangids,
						getClientEnvironment().getDate(),
						new String[] { m_sLoginCorpId }, wherePart);
			} catch (Exception e) {
				SCMEnv.out(e);
				MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
						"SCMCOMMON", "UPPSCMCommon-000270")/* "提示" */, e
						.getMessage());
			}
			Hashtable table = new Hashtable();
			if (vos != null && vos.length > 0) {
				// 如果价格为空或小于等于0，则将供应商清空
				int vosLength = vos.length;
				for (int i = 0; i < vosLength; i++) {
					if (vos[i].getQuota1() == null
							|| vos[i].getQuota1().doubleValue() <= 0) {
						vos[i].setVendor1(null);
						vos[i].setQuota1(null);
					}
				}
			}
			// 处理当前单据存在多个相同存货，则价格论证也分多行显示；
			if (vos != null && vos.length < cmangids.length) {
				int vosLength = vos.length;
				for (int i = 0; i < vosLength; i++) {
					table.put(vos[i].getcmangid(), vos[i]);
				}
				vos = new PriceInfosVO[cmangids.length];
				int cmangidsLength = cmangids.length;
				for (int j = 0; j < cmangidsLength; j++) {
					if (table.containsKey(cmangids[j])) {
						vos[j] = (PriceInfosVO) table.get(cmangids[j]);
					} else {
						vos[j] = new PriceInfosVO();
						vos[j].setcmangid((String) getBillCardPanel()
								.getBodyValueAt(j, "cmangid"));
						vos[j].setInvcode((String) getBillCardPanel()
								.getBodyValueAt(j, "cinventorycode"));
						vos[j].setInvname((String) getBillCardPanel()
								.getBodyValueAt(j, "cinventoryname"));
						vos[j].setInvspec((String) getBillCardPanel()
								.getBodyValueAt(j, "cinventoryspec"));
						vos[j].setInvtype((String) getBillCardPanel()
								.getBodyValueAt(j, "cinventorytype"));
					}
				}
			}
			// 处理最新价
			UFDouble[] newPrices = null;
			try {
				newPrices = PraybillHelper.queryNewPriceArray(m_sLoginCorpId,
						cmangids);
			} catch (Exception e) {//
			}
			if (newPrices != null && newPrices.length > 0) {
				Hashtable hash = new Hashtable();
				for (int i = 0; i < cmangids.length; i++) {
					if (hash.containsKey(cmangids[i]))
						continue;
					else {
						if (newPrices[i] == null)
							newPrices[i] = new UFDouble(-1);
						hash.put(cmangids[i], newPrices[i]);
					}
				}
				UFDouble newPrice;
				for (int j = 0; j < vos.length; j++) {
					cmangid = vos[j].getcmangid();
					if (cmangid != null && cmangid.trim().length() > 0
							&& hash.containsKey(cmangid)) {
						newPrice = (UFDouble) hash.get(cmangid);
						if (newPrice.doubleValue() <= 0)
							newPrice = null;
						vos[j].setNewprice(newPrice);
					}
				}
			}
			/* 显示价格论证表 */
			PriceInfoDlg dlgPriceInfo = new PriceInfoDlg(this, vos,
					nPriceDecimal, getClientEnvironment());
			dlgPriceInfo.showModal();
			showHintMessage(m_lanResTool.getStrByID("common",
					"4004COMMON000000031")/* @res "成功显示价格论证表" */);
		}
	}

	/**
	 * 查询当前单据审批状态
	 */

	private void onQueryForAudit() {
		PraybillVO[] vos = m_VOs;
		// 当前界面存在单据
		if (vos != null && vos.length > 0) {
			// 如果该单据处于正在审批状态，执行下列语句：
			nc.ui.pub.workflownote.FlowStateDlg approvestatedlg = new nc.ui.pub.workflownote.FlowStateDlg(
					this, "20", vos[m_nPresentRecord].getHeadVO()
							.getPrimaryKey());
			approvestatedlg.showModal();

			showHintMessage(m_lanResTool.getStrByID("common", "UCH035")/*
																		 * @res
																		 * "审批状态查询成功"
																		 */);
		} else {
			showHintMessage(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000539")/* @res "界面无数据" */);
		}
	}

	/**
	 * 功能：存量查询 创建：(2002-10-31 19:45:39) 修改：2003-04-21/czp/统一走销售对话框
	 */
	private void onQueryInvOnHand() {
		PraybillVO voPara = null;
		PraybillItemVO item = null;
		PraybillItemVO[] items = null;
		/* 卡片 */
		if (getBillCardPanel().isVisible()) {
			voPara = (PraybillVO) getBillCardPanel().getBillValueVO(
					PraybillVO.class.getName(),
					PraybillHeaderVO.class.getName(),
					PraybillItemVO.class.getName());
			if (voPara == null || voPara.getParentVO() == null) {
				showHintMessage(m_lanResTool.getStrByID("40040101",
						"UPP40040101-000073")/* "未选取单据,不能查询存量" */);
				return;
			}
			/* 新增单据时设置当前登录公司 */
			if (voPara.getParentVO().getAttributeValue("pk_corp") == null
					|| voPara.getParentVO().getAttributeValue("pk_corp")
							.toString().trim().equals("")) {
				voPara.getParentVO().setAttributeValue("pk_corp",
						m_sLoginCorpId);
			}
			/* 表体信息完整性检查 */
			int[] iSelRows = getBillCardPanel().getBillTable()
					.getSelectedRows();
			if (iSelRows != null && iSelRows.length > 0) {
				/* 得到用户选取的第一行 */
				item = (PraybillItemVO) getBillCardPanel().getBillModel()
						.getBodyValueRowVO(iSelRows[0],
								PraybillItemVO.class.getName());
			} else {
				/* 用户未选择时，取单据第一行 */
				items = (PraybillItemVO[]) getBillCardPanel().getBillModel()
						.getBodyValueVOs(PraybillItemVO.class.getName());
				if (items == null || items.length <= 0) {
					showHintMessage(m_lanResTool.getStrByID("40040101",
							"UPP40040101-000074")/* "公司、存货、需求日期信息不完整,不能查询存量" */);
					return;
				}
				item = items[0];
			}
			/* 信息完整性检查 */
			if (voPara.getParentVO().getAttributeValue("pk_corp") == null
					|| voPara.getParentVO().getAttributeValue("pk_corp")
							.toString().trim().equals("")
					|| item.getAttributeValue("cinventoryid") == null
					|| item.getAttributeValue("cinventoryid").toString().trim()
							.equals("")
					|| item.getAttributeValue("ddemanddate") == null
					|| item.getAttributeValue("ddemanddate").toString().trim()
							.equals("")) {
				showHintMessage(m_lanResTool.getStrByID("40040101",
						"UPP40040101-000074")/* "公司、存货、需求日期信息不完整,不能查询存量" */);
				return;
			}
			/* 组合新VO初始化并调用存量查询对话框 */
			voPara.setChildrenVO(new PraybillItemVO[] { item });
			if (saPkCorp == null) {
				try {
					IUserManageQuery myService = (IUserManageQuery) nc.bs.framework.common.NCLocator
							.getInstance().lookup(
									IUserManageQuery.class.getName());
					nc.vo.bd.CorpVO[] vos = myService
							.queryAllCorpsByUserPK(getClientEnvironment()
									.getUser().getPrimaryKey());
					if (vos == null || vos.length == 0) {
						Logger.debug("未查询到有权限公司，直接返回!");
						return;
					}
					final int iLen = vos.length;
					saPkCorp = new String[iLen];
					for (int i = 0; i < iLen; i++) {
						saPkCorp[i] = vos[i].getPrimaryKey();
					}
				} catch (Exception e) {
					MessageDialog
							.showErrorDlg(
									this,
									m_lanResTool.getStrByID("40040101",
											"UPP40040101-000075")/* "获取有权限公司异常" */,
									m_lanResTool.getStrByID("40040101",
											"UPP40040101-000076")/* "获取有权限公司时出现异常(详细信息参见控制台日志)!" */);
					return;
				}
			}
			getAtpDlg().setPkCorps(saPkCorp);
			getAtpDlg().initData(voPara);
			getAtpDlg().showModal();
		}
		/* 列表 */
		else if (getBillListPanel().isVisible()) {
			/* 表头信息完整性检查 */
			PraybillHeaderVO head = null;
			if (getBillListPanel().getHeadBillModel().getBodySelectedVOs(
					PraybillHeaderVO.class.getName()) == null
					|| getBillListPanel().getHeadBillModel()
							.getBodySelectedVOs(
									PraybillHeaderVO.class.getName()).length <= 0) {
				showHintMessage(m_lanResTool.getStrByID("40040101",
						"UPP40040101-000073")/* "未选取单据,不能查询存量" */);
				return;
			}
			head = (PraybillHeaderVO) getBillListPanel().getHeadBillModel()
					.getBodySelectedVOs(PraybillHeaderVO.class.getName())[0];
			if (head == null
					|| head.getAttributeValue("pk_corp") == null
					|| head.getAttributeValue("pk_corp").toString().trim()
							.equals("")) {
				showHintMessage(m_lanResTool.getStrByID("40040101",
						"UPP40040101-000077")/* "未明确公司,不能查询存量" */);
				return;
			}
			/* 表体信息完整性检查 */
			int[] iSelRows = getBillListPanel().getBodyTable()
					.getSelectedRows();
			if (iSelRows != null && iSelRows.length > 0) {
				/* 得到用户选取的第一行 */
				item = (PraybillItemVO) getBillListPanel().getBodyBillModel()
						.getBodyValueRowVO(iSelRows[0],
								PraybillItemVO.class.getName());
			} else {
				/* 用户未选择时，取单据第一行 */
				items = (PraybillItemVO[]) getBillListPanel()
						.getBodyBillModel().getBodyValueVOs(
								PraybillItemVO.class.getName());
				if (items == null || items.length <= 0) {
					showHintMessage(m_lanResTool.getStrByID("40040101",
							"UPP40040101-000074")/* "公司、存货、需求日期信息不完整,不能查询存量" */);
					return;
				}
				item = items[0];
			}
			/* 信息完整性检查 */
			if (item.getAttributeValue("cinventoryid") == null
					|| item.getAttributeValue("cinventoryid").toString().trim()
							.equals("")
					|| item.getAttributeValue("ddemanddate") == null
					|| item.getAttributeValue("ddemanddate").toString().trim()
							.equals("")) {
				showHintMessage(m_lanResTool.getStrByID("40040101",
						"UPP40040101-000074")/* "公司、存货、需求日期信息不完整,不能查询存量" */);
				return;
			}
			/* 组合新VO初始化并调用存量查询对话框 */
			voPara = new PraybillVO();
			voPara.setParentVO(head);
			voPara.setChildrenVO(new PraybillItemVO[] { item });
			if (saPkCorp == null) {
				try {
					IUserManageQuery myService = (IUserManageQuery) nc.bs.framework.common.NCLocator
							.getInstance().lookup(
									IUserManageQuery.class.getName());
					nc.vo.bd.CorpVO[] vos = myService
							.queryAllCorpsByUserPK(getClientEnvironment()
									.getUser().getPrimaryKey());
					if (vos == null || vos.length == 0) {
						Logger.debug("未查询到有权限公司，直接返回!");
						return;
					}
					final int iLen = vos.length;
					saPkCorp = new String[iLen];
					for (int i = 0; i < iLen; i++) {
						saPkCorp[i] = vos[i].getPrimaryKey();
					}
				} catch (Exception e) {
					MessageDialog
							.showErrorDlg(
									this,
									m_lanResTool.getStrByID("40040101",
											"UPP40040101-000075")/* "获取有权限公司异常" */,
									m_lanResTool.getStrByID("40040101",
											"UPP40040101-000076")/* "获取有权限公司时出现异常(详细信息参见控制台日志)!" */);
					Logger.debug(e.getMessage());
					return;
				}
			}
			getAtpDlg().setPkCorps(saPkCorp);
			getAtpDlg().initData(voPara);
			getAtpDlg().showModal();
			showHintMessage(m_lanResTool.getStrByID("common",
					"4004COMMON000000032")/* @res "存量查询完成" */);
		}
	}

	/*
	 * 功能描述:保存
	 */
	private boolean onSave() {
		showHintMessage(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000078")/* "保存开始..." */);
		nc.vo.scm.pu.Timer timer = new nc.vo.scm.pu.Timer();
		timer.start("请购单保存操作onSave（）开始");

		// 终止编辑
		getBillCardPanel().stopEditing();
		// 过虑空行
		filterNullLine();

		// 增加对校验公式的支持,错误显示由UAP处理 since v501
		if (!getBillCardPanel().getBillData().execValidateFormulas()) {
			return false;
		}

		final int nRow = getBillCardPanel().getRowCount();
		PraybillVO VO = new PraybillVO(nRow);
		m_SaveVOs = new PraybillVO(nRow);
		getBillCardPanel().getBillValueVO(VO);
		// 为单据类型赋值
		String Cbiztype = VO.getHeadVO().getCbiztype();
		if (Cbiztype == null) {
			if (m_bizButton.getTag() != null
					&& m_bizButton.getTag().trim() != "") {
				VO.getHeadVO().setCbiztype(m_bizButton.getTag());
			}
		}
		// 保存前有效性检查
		if (!checkBeforeSave(VO))
			return false;
		if (!checkPraytype(VO.getBodyVO()))
			return false;

		timer.addExecutePhase("保存前有效性检查");

		if (m_bAdd) {

			// 增加请购单(包括复制请购单)
			PraybillHeaderVO headVO = VO.getHeadVO();
			// 表头设置
			headVO.setNversion(new Integer(1));

			headVO.setPk_corp(m_sLoginCorpId);
			headVO.setIpraysource(new Integer(m_comPraySource
					.getSelectedIndex()));
			headVO.setIpraytype(new Integer(m_comPrayType.getSelectedIndex()));
			headVO.setCaccountyear(getClientEnvironment().getAccountYear());
			headVO.setIbillstatus(new Integer(0));

			headVO.setCauditpsn(null);
			headVO.setDauditdate(null);

			// 制单人

			headVO.setCoperator(getClientEnvironment().getUser()
					.getPrimaryKey());
			if (m_bizButton != null)
				headVO.setCbiztype(m_bizButton.getTag());
			// 复制时的特殊处理
			if (isFrmCopy) {
				headVO.setCbiztype(((UIRefPane) getBillCardPanel().getHeadItem(
						"cbiztype").getComponent()).getRefModel().getPkValue());
			}
			try {
				// 新增时置当前操作员为制单人
				headVO.setCoperator(getClientEnvironment().getUser()
						.getPrimaryKey());
			} catch (Exception e) {
				Logger.debug(e.getMessage());
				MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
						"SCMCOMMON", "UPPSCMCommon-000355")/* "表头设置" */, e
						.getMessage());
				showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
						"UPPSCMCommon-000010")/* "保存失败" */);
				return false;
			}
			UIRefPane nRefPanel = (UIRefPane) getBillCardPanel().getHeadItem(
					"vmemo").getComponent();
			UITextField vMemoField = nRefPanel.getUITextField();
			headVO.setVmemo(vMemoField.getText());
			// 表体设置
			PraybillItemVO[] bodyVO = VO.getBodyVO();
			Object oTemp;
			for (int i = 0; i < bodyVO.length; i++) {
				bodyVO[i].setPk_corp(m_sLoginCorpId);

				oTemp = getBillCardPanel().getBodyValueAt(i, "vmemo");
				if (oTemp != null && oTemp.toString().length() > 0)
					bodyVO[i].setVmemo((String) oTemp);
				else
					bodyVO[i].setVmemo(null);
			}
			// 保存临时VO
			m_SaveVOs.setChildrenVO(bodyVO);

		} else {
			// 修改请购单
			PraybillHeaderVO headVO = VO.getHeadVO();
			Logger.debug("BCoperator=" + headVO.getCoperator());
			// 从缓存中重置制单人
			if (m_VOs != null && m_VOs[m_nPresentRecord] != null
					&& m_VOs[m_nPresentRecord].getHeadVO() != null
					&& !isAllowedModifyByOther) {
				headVO.setCoperator(getClientEnvironment().getUser()
						.getPrimaryKey());
			}
			Logger.debug("Coperator=" + headVO.getCoperator());
			Logger.debug("isAllowedModifyByOther=" + isAllowedModifyByOther);
			// 表头设置
			headVO.setPk_corp(m_sLoginCorpId);
			headVO.setIpraysource(new Integer(m_comPraySource
					.getSelectedIndex()));
			headVO.setIpraytype(new Integer(m_comPrayType.getSelectedIndex()));
			headVO.setIbillstatus(new Integer(0));
			headVO.setPrimaryKey((String) m_VOs[m_nPresentRecord].getHeadVO()
					.getAttributeValue("cpraybillid"));
			headVO.setCaccountyear(getClientEnvironment().getAccountYear());
			headVO.setHeadEditStatus(2);

			headVO.setCauditpsn(null);
			headVO.setDauditdate(null);
			headVO.setCoperatoridnow(getClientEnvironment().getUser()
					.getPrimaryKey());

			UIRefPane nRefPanel = (UIRefPane) getBillCardPanel().getHeadItem(
					"vmemo").getComponent();
			UITextField vMemoField = nRefPanel.getUITextField();
			headVO.setVmemo(vMemoField.getText());
			// 表体设置
			PraybillItemVO[] bodyVO = VO.getBodyVO();
			UFDouble d;
			Object oTemp;
			for (int i = 0; i < bodyVO.length; i++) {
				bodyVO[i].setPk_corp(m_sLoginCorpId);
				if (bodyVO[i].getStatus() == VOStatus.NEW) {
					bodyVO[i].setBodyEditStatus(1);
				} else if (bodyVO[i].getStatus() == VOStatus.UPDATED) {
					bodyVO[i].setBodyEditStatus(2);
				} else if (bodyVO[i].getStatus() == VOStatus.DELETED) {
					bodyVO[i].setBodyEditStatus(3);
				}

				d = bodyVO[i].getNsuggestprice();
				if (d == null || d.toString().length() == 0)
					bodyVO[i].setNsuggestprice(new UFDouble(0.0));
				d = bodyVO[i].getNaccumulatenum();
				if (d == null || d.toString().length() == 0)
					bodyVO[i].setNaccumulatenum(new UFDouble(0.0));
				oTemp = getBillCardPanel().getBodyValueAt(i, "vmemo");
				if (oTemp != null && oTemp.toString().length() > 0)
					bodyVO[i].setVmemo((String) oTemp);
				else
					bodyVO[i].setVmemo(null);

			}
			// 复制表体
			Vector vData = new Vector();
			for (int i = 0; i < bodyVO.length; i++) {
				vData.addElement(bodyVO[i]);
			}
			// 复制删除的表体
			if (getBillCardPanel().getBillModel().getBodyValueChangeVOs(
					PraybillItemVO.class.getName()) != null) {
				int changeVOsLength = getBillCardPanel().getBillModel()
						.getBodyValueChangeVOs(PraybillItemVO.class.getName()).length;
				for (int i = 0; i < changeVOsLength; i++) {
					if (getBillCardPanel().getBillModel()
							.getBodyValueChangeVOs(
									PraybillItemVO.class.getName())[i]
							.getStatus() == nc.ui.pub.bill.BillModel.DELETE) {
						vData.addElement(getBillCardPanel().getBillModel()
								.getBodyValueChangeVOs(
										PraybillItemVO.class.getName())[i]);
					}
				}
			}
			PraybillItemVO[] v = new PraybillItemVO[vData.size()];
			vData.copyInto(v);
			for (int i = vData.size() - 1; i >= nRow; i--) {
				v[i].setBodyEditStatus(3); // 设置删除状态
			}
			// 包括删除的所有表体设置为当前的表体
			VO.setChildrenVO(v);
			// 保存临时VO
			m_SaveVOs.setChildrenVO(VO.getChildrenVO());
		}

		ArrayList arrReturnFromBs = null;
		try {
			/* 是否需要回退单据号:新增且手工录入单据号 */
			if (m_bAdd) {
				Object sPraybillCode = VO.getParentVO().getAttributeValue(
						"vpraycode");
				if (VO.getParentVO() != null && sPraybillCode != null
						&& sPraybillCode.toString().trim().length() > 0) {
				}
			}
			/* 保存 */
			/* 赋操作员(无论新增还是修改) */
			String strOpr = getClientEnvironment().getUser().getPrimaryKey();
			VO.setCurrOperator(strOpr);
			VO.getHeadVO().setCuserid(strOpr);
			/* 重置单据状态为自由 */
			VO.getHeadVO().setIbillstatus(new Integer(0));
			m_SaveVOs.setParentVO(VO.getParentVO());
			/* 支持供应商核准检查 */
			ArrayList aryUserObj = new ArrayList();
			aryUserObj.add(new Integer(1));
			aryUserObj.add("cvendormangid");
			timer.addExecutePhase("保存前的准备操作");

			arrReturnFromBs = (ArrayList) PfUtilClient
					.processActionNoSendMessage(this, "SAVEBASE",
							nc.vo.scm.pu.BillTypeConst.PO_PRAY,
							getClientEnvironment().getDate().toString(), VO,
							aryUserObj, null, null);

			timer.addExecutePhase("执行SAVEBASE脚步操作");

			// 得到主键
			if (arrReturnFromBs == null || arrReturnFromBs.size() == 0) {
				showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
						"UPPSCMCommon-000010")/* "保存失败" */);
				return false;
			}

			timer.addExecutePhase("送审操作");

			// 晁志平 增加重庆力帆自由项输入提示功能
			nc.ui.po.pub.InvAttrCellRenderer ficr = new nc.ui.po.pub.InvAttrCellRenderer();
			ficr.setFreeItemRenderer(getBillCardPanel(), null);

			// V50，保存成功才设置此标志
			isFrmCopy = false;
			//
			showHintMessage(m_lanResTool.getStrByID("common", "UCH005")/*
																		 * @res"保存成功"
																		 */);
		} catch (Exception e) {
			SCMEnv.out(e);
			if (e instanceof BusinessException) {
				showHintMessage(m_lanResTool.getStrByID("40040101",
						"UPP40040101-000080")/* @res "保存失败！" */);
				MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
						"40040101", "UPP40040101-000081")/* @res "请购单保存" */, e
						.getMessage());
			} else {
				showHintMessage(m_lanResTool.getStrByID("40040101",
						"UPP40040101-000080")/* @res "保存失败！" */);
				MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
						"40040101", "UPP40040101-000081")/* @res "请购单保存" */, e
						.getMessage());
			}
			return false;
		}

		// 后台返回值处理
		Vector vDataTemp = new Vector();
		int itemsLength = 0;
		PraybillItemVO[] items = null;
		PraybillItemVO[] itemsTemp = null;
		PraybillItemVO item = null;
		int status = 0;
		String[] ts = null;
		ArrayList arr = new ArrayList();
		if (arrReturnFromBs != null && arrReturnFromBs.size() >= 3) {
			ts = (String[]) arrReturnFromBs.get(arrReturnFromBs.size() - 3);
			m_SaveVOs.getHeadVO().setTs(ts[0]);
			m_SaveVOs.getHeadVO().setVpraycode(
					(String) arrReturnFromBs.get(arrReturnFromBs.size() - 1));
			m_SaveVOs.getHeadVO().setCpraybillid(
					(String) arrReturnFromBs.get(0));
			items = m_SaveVOs.getBodyVO();
			itemsLength = items.length;
			// 表体ID与TS对应表
			HashMap<String, String> mapBidBts = new HashMap<String, String>();
			for (int i = 1; i < ts.length; i++) {
				mapBidBts.put(ts[i].substring(0, 20), ts[i].substring(20, 39));
			}
			int iPos = 1;
			// 和BS端引用协议：保证请购单新增的表体行主键是 arrReturn 的正数第二、三、...个元素
			for (int i = 0; i < itemsLength; i++) {
				item = items[i];
				status = item.getBodyEditStatus();
				if (item != null && !String.valueOf(status).equals("3")) {
					if (item.getCpraybillid() == null)
						item.setCpraybillid((String) arrReturnFromBs.get(0));
					if (item.getCpraybill_bid() == null) {
						item.setCpraybill_bid((String) arrReturnFromBs
								.get(iPos));
						iPos++;
					}
					item.setTs(mapBidBts.get(item.getCpraybill_bid()));
					vDataTemp.add(item);
				}
				items[i] = item;
			}
			itemsTemp = new PraybillItemVO[vDataTemp.size()];
			vDataTemp.copyInto(itemsTemp);
			m_SaveVOs.setChildrenVO(itemsTemp);
			// 用于审批流批配置了条件函数，当满足条件时制单人可以直接审批单据，这时需要刷新显示
			arr = (ArrayList) arrReturnFromBs.get(arrReturnFromBs.size() - 2);
			m_SaveVOs.getHeadVO().setDauditdate((UFDate) arr.get(0));
			m_SaveVOs.getHeadVO().setCauditpsn((String) arr.get(1));
			m_SaveVOs.getHeadVO().setIbillstatus((Integer) arr.get(2));
			m_SaveVOs.getHeadVO().setTs((String) arr.get(3));
			m_SaveVOs.getHeadVO().setTmaketime((String) arr.get(4));
			m_SaveVOs.getHeadVO().setTlastmaketime((String) arr.get(5));
			m_SaveVOs.getHeadVO().setTaudittime((String) arr.get(6));

			dispAfterSave(m_SaveVOs, arrReturnFromBs);
		}
		// // 手工排序卡片表体(恢复因解决并发问题而做人为排序处理)
		// if (iSortCol >= 0) {
		// getBillCardPanel().getBillModel().sortByColumn(iSortCol, bSortAsc);
		// }
		timer.addExecutePhase("保存后的显示操作");
		timer.showAllExecutePhase("请购单保存操作结束");

		showHintMessage(m_lanResTool.getStrByID("common", "UCH005")/*
																	 * @res
																	 * "保存成功"
																	 */);

		return true;
	}

	/**
	 * 功能描述:全选
	 */
	private void onSelectAll() {
		final int iLen = getBillListPanel().getHeadBillModel().getRowCount();
		if (iLen <= 0) {
			showHintMessage(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000541")/* @res "无数据选中" */);
			return;
		}
		getBillListPanel().getHeadTable().setRowSelectionInterval(0, iLen - 1);
		for (int i = 0; i < iLen; i++) {
			getBillListPanel().getHeadBillModel().setRowState(i,
					BillModel.SELECTED);
		}
		/* 设置按钮逻辑 */
		setButtonsList();
		showHintMessage(m_lanResTool
				.getStrByID("common", "4004COMMON000000033")/* @res "全部选中成功" */);
	}

	/**
	 * 功能描述:全消
	 */
	private void onSelectNo() {
		final int iLen = getBillListPanel().getHeadBillModel().getRowCount();
		if (iLen <= 0) {
			showHintMessage(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000541")/* @res "无数据选中" */);
			return;
		}
		getBillListPanel().getHeadTable().removeRowSelectionInterval(0,
				iLen - 1);
		for (int i = 0; i < iLen; i++) {
			getBillListPanel().getHeadBillModel().setRowState(i,
					BillModel.NORMAL);
		}
		/* 设置按钮逻辑 */
		setButtonsList();
		showHintMessage(m_lanResTool
				.getStrByID("common", "4004COMMON000000034")/* @res "全部取消成功" */);
	}

	/**
	 * 功能:列表到卡片
	 */
	private void onCard() {

		// 无请购单
		if (m_VOs == null || m_VOs.length == 0) {
			getBillListPanel().setVisible(false);
			getBillCardPanel().setVisible(true);
			getBillCardPanel().getBillData().clearViewData();
			setButtonsCard();
			m_nUIState = 0;
			showHintMessage(m_lanResTool.getStrByID("common", "UCH021")/*
																		 * @res
																		 * "卡片显示"
																		 */);
			return;
		}
		// 有请购单
		if (m_nPresentRecord >= 0) {
			int iSortCol = getBillListPanel().getBodyBillModel()
					.getSortColumn();
			boolean bSortAsc = getBillListPanel().getBodyBillModel()
					.isSortAscending();
			/* 加载单据卡片控件 */
			getBillListPanel().setVisible(false);
			getBillCardPanel().setVisible(true);
			m_nUIState = 0;
			/* 加载单据VO到卡片 */
			setVoToBillCard(m_nPresentRecord, null);
			// 手工排序卡片表体(同步列表表体排序结果)
			if (iSortCol >= 0) {
				getBillCardPanel().getBillModel().sortByColumn(iSortCol,
						bSortAsc);
			}
			/* 设置按钮逻辑 */
			setButtonsCard();
		}
		getBillCardPanel().execHeadTailLoadFormulas();
		getBillCardPanel().setEnabled(false);
		showHintMessage(m_lanResTool.getStrByID("common", "UCH021")/*
																	 * @res
																	 * "卡片显示"
																	 */);
	}

	/**
	 * 创建日期： 2005-9-20 功能描述： 增加扩展按钮到卡片界面
	 */
	private void addExtendBtns() {
		ButtonObject[] btnsExtend = getExtendBtns();
		if (btnsExtend == null || btnsExtend.length <= 0) {
			return;
		}
		ButtonObject boExtTop = getBtnTree().getExtTopButton();
		getBtnTree().addMenu(boExtTop);
		int iLen = btnsExtend.length;
		try {
			for (int j = 0; j < iLen; j++) {
				getBtnTree().addChildMenu(boExtTop, btnsExtend[j]);
			}
		} catch (BusinessException be) {
			showHintMessage(be.getMessage());
			return;
		}
	}

	/**
	 * 功能:执行弃审
	 */
	private void onUnAudit() {
		try {
			nc.vo.scm.pu.Timer timer = new nc.vo.scm.pu.Timer();
			timer.start("请购单弃审操作onUnAudit（）开始");
			String[] sPraybillIds = new String[1];
			showHintMessage(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000082")/*
										 * @res "正在弃审..."
										 */);
			PraybillVO vo = m_VOs[m_nPresentRecord];

			/* ---------------- added v31sp1 zhongwei 修订后请购单不能弃审 */
			if (vo.getHeadVO().getNversion().doubleValue() > 1)
				throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("40040101", "UPP40040101-000527", null,
								new String[] { vo.getHeadVO().getVpraycode() })/*
																				 * @res
																				 * "修订后请购单不能弃审!\n
																				 * 单据号
																				 * ：
																				 * {
																				 * 0
																				 * }
																				 * "
																				 */);
			/* ---------------- added v31sp1 zhongwei 修订后请购单不能弃审 */

			vo.getHeadVO().setCuserid(
					getClientEnvironment().getUser().getPrimaryKey());
			sPraybillIds[0] = vo.getHeadVO().getCpraybillid();
			// 配合审批流
			vo.getHeadVO().setCauditpsn(
					getClientEnvironment().getUser().getPrimaryKey());

			timer.addExecutePhase("弃审前准备操作");
			/* 弃审 */
			PfUtilClient.processBatchFlow(null, "UNAPPROVE"
					+ getClientEnvironment().getUser().getPrimaryKey(),
					nc.vo.scm.pu.BillTypeConst.PO_PRAY, getClientEnvironment()
							.getDate().toString(), new PraybillVO[] { vo },
					null);
			if (!PfUtilClient.isSuccess()) {
				showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
						"UPPSCMCommon-000409")/*
											 * @res "弃审未成功"
											 */);
				return;
			}
			timer.addExecutePhase("执行UNAPPROVE脚本操作");

			/* 弃审成功后刷新 */
			ArrayList arrRet = PraybillHelper.queryPrayForSaveAudit(vo
					.getParentVO().getPrimaryKey());
			((PraybillHeaderVO) vo.getParentVO()).setDauditdate((UFDate) arrRet
					.get(0));
			((PraybillHeaderVO) vo.getParentVO()).setCauditpsn((String) arrRet
					.get(1));
			((PraybillHeaderVO) vo.getParentVO())
					.setIbillstatus((Integer) arrRet.get(2));
			((PraybillHeaderVO) vo.getParentVO()).setTs((String) arrRet.get(3));
			((PraybillHeaderVO) vo.getParentVO()).setTaudittime(null);
			
			
			//lcq add by 2015/5/29  移除备注
			((PraybillHeaderVO) vo.getParentVO()).setVmemo("");
			; 
			String sql="UPDATE po_praybill SET Vmemo=' ' WHERE cpraybillid='"+sPraybillIds[0]+"'";
			IPubDMO getdmo = (IPubDMO) NCLocator.getInstance().lookup(IPubDMO.class.getName());
			getdmo.executeUpdate(sql);
			//lcq end 移除备注

			timer.addExecutePhase("弃审成功后刷新");
			m_VOs[m_nPresentRecord] = vo;
			/* 刷新按钮状态 */
			setButtonsCard();
			/* 加载单据 */
			setVoToBillCard(m_nPresentRecord, m_lanResTool.getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000410")/*
														 * @res "被弃审"
														 */);

			timer.addExecutePhase("弃审后显示");
			timer.showAllExecutePhase("请购单弃审操作onUnAudit（）结束");
			getBillCardPanel().setEnabled(false);
			showHintMessage(m_lanResTool.getStrByID("common", "UCH011")/*
																		 * @res
																		 * "弃审成功"
																		 */);
		} catch (Exception e) {
			PuTool.outException(this, e);
			showHintMessage(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000083")/*
										 * @res "出现异常,弃审失败"
										 */);
			SCMEnv.out(e);
		}
	}

	/**
	 * 处理执行动作组按钮逻辑
	 */
	private void refreshActionButtons1() {
		int m_btnActionLength = m_btnActions.getChildButtonGroup().length;
		// 执行按钮组处理
		if (m_btnActions != null && m_btnActions.getChildButtonGroup() != null
				&& m_btnActions.getChildButtonGroup().length > 0) {
			// 有执行动作组
			m_btnActions.setEnabled(true);
			for (int i = 0; i < m_btnActionLength; i++) {
				if ("APPROVE".equals(m_btnActions.getChildButtonGroup()[i]
						.getTag())) {
					if (isCanAudit(m_VOs[m_nPresentRecord])) {
						m_btnActions.getChildButtonGroup()[i].setEnabled(true);
					} else {
						m_btnActions.getChildButtonGroup()[i].setEnabled(false);
					}
				}
				if ("UNAPPROVE".equals(m_btnActions.getChildButtonGroup()[i]
						.getTag())) {
					if (isCanUnAudit(m_VOs[m_nPresentRecord])) {
						m_btnActions.getChildButtonGroup()[i].setEnabled(true);
					} else {
						m_btnActions.getChildButtonGroup()[i].setEnabled(false);
					}
				}
				if ("OPEN".equals(m_btnActions.getChildButtonGroup()[i]
						.getTag())) {
					if (isCanOpen(m_VOs[m_nPresentRecord])) {
						m_btnActions.getChildButtonGroup()[i].setEnabled(true);
					} else {
						m_btnActions.getChildButtonGroup()[i].setEnabled(false);
					}
				}
				if ("CLOSE".equals(m_btnActions.getChildButtonGroup()[i]
						.getTag())) {
					if (isCanClose(m_VOs[m_nPresentRecord])) {
						m_btnActions.getChildButtonGroup()[i].setEnabled(true);
					} else {
						m_btnActions.getChildButtonGroup()[i].setEnabled(false);
					}
				}
			}
		} else {
			// 无执行动作组
			m_btnActions.setEnabled(false);
			m_btnActions.setChildButtonGroup(null);
		}
		updateButton(m_btnActions);
		for (int i = 0; i < m_btnActionLength; i++) {
			updateButton(m_btnActions.getChildButtonGroup()[i]);
		}
	}

	/**
	 * 设置所有按钮是否可用。
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * 使用示例
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @param bEnabled
	 *            <p>
	 * @author czp
	 * @time 2007-3-13 下午01:04:45
	 */
	private void setButtonsEnabled(boolean bEnabled) {
		ButtonObject[] bos = m_btnTree.getButtonArray();
		int iLen = bos == null ? 0 : bos.length;
		int jLen = 0;
		for (int i = 0; i < iLen; i++) {
			bos[i].setEnabled(bEnabled);
			if (bos[i].getChildCount() > 0) {
				jLen = bos[i].getChildCount();
				for (int j = 0; j < jLen; j++) {
					bos[i].getChildButtonGroup()[j].setEnabled(bEnabled);
				}
			}
		}
		updateButtonsAll();
	}

	/**
	 * 功能：卡片浏览按钮逻辑
	 */
	private void setButtonsCard() {
		//
		setButtonsEnabled(true);
		//
		dealBtnsBeforeCardShowing();

		/* 保存、取消、行操作组 */
		m_btnSave.setEnabled(false);
		m_btnCancel.setEnabled(false);
		m_btnLines.setEnabled(false);
		if (m_btnLines.getChildCount() > 0) {
			for (int i = 0; i < m_btnLines.getChildButtonGroup().length; i++) {
				m_btnLines.getChildButtonGroup()[i].setEnabled(false);
			}
		}
		boolean bHaveRecs = (m_VOs != null && m_VOs.length > 0);
		// 复制
		m_btnCopy.setEnabled(bHaveRecs);
		/* 翻页 */
		if (!bHaveRecs) {
			m_btnFirst.setEnabled(false);
			m_btnPrev.setEnabled(false);
			m_btnNext.setEnabled(false);
			m_btnLast.setEnabled(false);
		} else if (m_VOs.length == 1) {
			m_btnFirst.setEnabled(false);
			m_btnPrev.setEnabled(false);
			m_btnNext.setEnabled(false);
			m_btnLast.setEnabled(false);
		} else if (m_VOs != null && m_nPresentRecord == m_VOs.length - 1) {
			m_btnNext.setEnabled(false);
			m_btnLast.setEnabled(false);
		} else if (m_nPresentRecord == 0) {
			m_btnFirst.setEnabled(false);
			m_btnPrev.setEnabled(false);
		}
		// 辅助功能
		m_btnDocument.setEnabled(bHaveRecs);
		// 辅助查询
		m_btnWorkFlowBrowse.setEnabled(bHaveRecs);
		m_btnUsable.setEnabled(bHaveRecs);
		m_btnPrint.setEnabled(bHaveRecs);
		m_btnPrintPreview.setEnabled(bHaveRecs);
		m_btnCombin.setEnabled(bHaveRecs);
		m_btnLinkBillsBrowse.setEnabled(bHaveRecs);
		m_btnPriceInfo.setEnabled(bHaveRecs);

		/* 送审、关闭、打开、修改、作废、审批、弃审 */
		int iBillStatus = -1;
		if (bHaveRecs && m_VOs[m_nPresentRecord] != null
				&& m_VOs[m_nPresentRecord].getHeadVO().getIbillstatus() != null) {
			iBillStatus = m_VOs[m_nPresentRecord].getHeadVO().getIbillstatus()
					.intValue();
		}
		// 送审
		boolean isNeedSendToAuditQ = isNeedSendAudit(iBillStatus);
		//
		if (iBillStatus == -1) {
			m_btnSendAudit.setEnabled(false);
			m_btnClose.setEnabled(false);
			m_btnOpen.setEnabled(false);
			m_btnModify.setEnabled(false);
			m_btnDiscard.setEnabled(false);
			m_btnApprove.setEnabled(false);
			m_btnUnApprove.setEnabled(false);
		} else if (iBillStatus == 0) {
			m_btnSendAudit.setEnabled(isNeedSendToAuditQ);
			m_btnClose.setEnabled(false);
			m_btnOpen.setEnabled(false);
			m_btnUnApprove.setEnabled(false);
		} else if (iBillStatus == 1) {// 已经关闭
			m_btnSendAudit.setEnabled(false);
			m_btnClose.setEnabled(false);
			m_btnModify.setEnabled(false);
			m_btnDiscard.setEnabled(false);
			m_btnApprove.setEnabled(false);
			m_btnUnApprove.setEnabled(false);
		} else if (iBillStatus == 2) {/* 请购单正在审批 */
			m_btnSendAudit.setEnabled(false);
			m_btnClose.setEnabled(false);
			m_btnOpen.setEnabled(false);
			m_btnModify.setEnabled(false);
			m_btnDiscard.setEnabled(false);
			m_btnUnApprove.setEnabled(false);
		} else if (iBillStatus == 3) {/* 请购单已审批通过 */
			m_btnSendAudit.setEnabled(false);
			m_btnOpen.setEnabled(false);
			m_btnModify.setEnabled(false);
			m_btnDiscard.setEnabled(false);
			m_btnApprove.setEnabled(false);
		} else if (iBillStatus == 4) {/* 请购单审批不通过 */
			m_btnSendAudit.setEnabled(false);
			m_btnClose.setEnabled(false);
			m_btnOpen.setEnabled(false);
			m_btnUnApprove.setEnabled(false);
		}
		// 刷新
		m_btnRefresh.setEnabled(m_bQueried);
		//
		updateButtonsAll();
	}

	/**
	 * 功能：无数据时初始化卡片按钮,设置除保存，行操作，放弃外所有按钮为正常 返回： 例外： 日期：(2002-5-14 11:24:40)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 */
	private void setButtonsCardModify() {
		//
		setButtonsEnabled(false);
		//
		dealBtnsBeforeCardShowing();
		// 行操作
		m_btnLines.setEnabled(true);
		if (m_btnLines.getChildCount() > 0) {
			for (int i = 0; i < m_btnLines.getChildButtonGroup().length; i++) {
				m_btnLines.getChildButtonGroup()[i].setEnabled(true);
			}
		}
		// 辅助功能
		m_btnOthersFuncs.setEnabled(true);
		// 辅助查询
		m_btnOthersQry.setEnabled(true);
		btnCKKCXX.setEnabled(true);
		// 存量查询
		m_btnUsable.setEnabled(true);
		// 单据维护
		m_btnMaintains.setEnabled(true);
		// 保存
		m_btnSave.setEnabled(true);
		// 取消
		m_btnCancel.setEnabled(true);
		// 送审
		int iBillStatus = -1;
		if (m_VOs != null && m_VOs.length > 0
				&& m_VOs[m_nPresentRecord] != null
				&& m_VOs[m_nPresentRecord].getHeadVO().getIbillstatus() != null) {
			iBillStatus = m_VOs[m_nPresentRecord].getHeadVO().getIbillstatus()
					.intValue();
		}
		m_btnSendAudit.setEnabled(isNeedSendAudit(iBillStatus));
		//
		updateButtonsAll();
	}

	/**
	 * 送审按钮逻辑处理
	 * 
	 * @author czp
	 * @since v50
	 * @date 2006-09-23
	 * 
	 * @注意：本方法的约定，新增单据时调用此方法，要传递状态值为 -1
	 * 
	 */
	private boolean isNeedSendAudit(int iBillStatus) {

		// 审批未通过
		boolean isNeedSendToAuditQ = (iBillStatus == BillStatus.AUDITFAIL);

		// 增加
		if (iBillStatus == -1) {
			if (m_bizButton != null && m_bizButton.getTag() != null) {
				isNeedSendToAuditQ = BusiBillManageTool.isNeedSendToAudit("20",
						m_sLoginCorpId, m_bizButton.getTag(), null,
						getClientEnvironment().getUser().getPrimaryKey());
			}
		}

		// 自由(修改情况)
		if (iBillStatus == BillStatus.FREE) {
			String billid = getPraybillVOs()[m_nPresentRecord].getHeadVO()
					.getCpraybillid();
			String cbizType = getPraybillVOs()[m_nPresentRecord].getHeadVO()
					.getCbiztype();
			if (PuPubVO.getString_TrimZeroLenAsNull(cbizType) != null) {
				isNeedSendToAuditQ = BusiBillManageTool.isNeedSendToAudit("20",
						m_sLoginCorpId, cbizType, billid,
						getClientEnvironment().getUser().getPrimaryKey());
			}
		}
		m_btnSendAudit.setEnabled(isNeedSendToAuditQ);
		updateButton(m_btnSendAudit);
		//
		return isNeedSendToAuditQ;
	}

	/**
	 * 功能：无数据时初始化卡片按钮,设置除保存，行操作，放弃外所有按钮为正常 返回： 例外： 日期：(2002-5-14 11:24:40)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 */
	private void setButtonsCardCopy() {
		//
		setButtonsEnabled(false);

		// 特殊功能
		m_btnList.setName(m_lanResTool.getStrByID("SCMCOMMON",
				"UPPSCMCommon-000464")/*
									 * @res "列表显示"
									 */);
		// 列表功能
		m_btnSelectAll.setEnabled(false);
		m_btnSelectNo.setEnabled(false);
		//
		m_btnLines.setEnabled(true);
		if (m_btnLines.getChildCount() > 0) {
			for (int i = 0; i < m_btnLines.getChildButtonGroup().length; i++) {
				m_btnLines.getChildButtonGroup()[i].setEnabled(true);
			}
		}
		/* 修改时可用:辅助{存量查询} */
		m_btnOthersFuncs.setEnabled(true);
		m_btnUsable.setEnabled(true);

		m_btnMaintains.setEnabled(true);
		m_btnSave.setEnabled(true);
		m_btnCancel.setEnabled(true);
		m_btnBrowses.setEnabled(true);
		int iBillStatus = 88;
		String cbizType = null;

		if (m_VOs != null && m_VOs.length > 0
				&& m_VOs[m_nPresentRecord] != null
				&& m_VOs[m_nPresentRecord].getHeadVO().getIbillstatus() != null) {
			iBillStatus = m_VOs[m_nPresentRecord].getHeadVO().getIbillstatus()
					.intValue();
			cbizType = m_VOs[m_nPresentRecord].getHeadVO().getCbiztype();
		}
		if (iBillStatus == 0) {
			setSendAuditBtnState();
		}

		boolean isNeedSendToAuditQ = false;
		if ((m_bAdd || ((new Integer(iBillStatus))
				.compareTo(nc.vo.scm.pu.BillStatus.FREE) == 0 && (new Integer(
				iBillStatus)).compareTo(nc.vo.scm.pu.BillStatus.AUDITFAIL) == 0))) {
			// 该操作人是否有审批流
			if (cbizType != null && cbizType.toString().trim().length() > 0) {
				isNeedSendToAuditQ = BusiBillManageTool.isNeedSendToAudit("20",
						m_sLoginCorpId, cbizType, null, getClientEnvironment()
								.getUser().getPrimaryKey());
			} else if (m_bizButton != null && m_bizButton.getTag() != null
					&& m_bizButton.getTag() != null) {
				isNeedSendToAuditQ = BusiBillManageTool.isNeedSendToAudit("20",
						m_sLoginCorpId, m_bizButton.getTag(), null,
						getClientEnvironment().getUser().getPrimaryKey());
			}
			m_btnSendAudit.setEnabled(isNeedSendToAuditQ);
		}
		// 刷新
		m_btnRefresh.setEnabled(false);
	}

	/**
	 * 子类实现该方法，返回业务界面的标题。
	 * 
	 * @version (00-6-6 13:33:25)
	 * 
	 * @return java.lang.String
	 */
	private void setCauditid(String newId) {

		m_cauditid = newId;
	}

	/**
	 * 功能：设置精度 2002-10-29 czp 加入最高限价精度设置
	 */
	private void setDecimalDigits(String pk_corp, int iflag) {
		int[] iaDigit = PoPublicUIClass.getShowDigits(pk_corp);
		final int nMoney = nc.ui.rc.pub.CPurchseMethods.getCurrDecimal(pk_corp);
		if (iflag == 0) {
			// CARD
			getBillCardPanel().getBodyItem("npraynum").setDecimalDigits(
					iaDigit[0]);
			getBillCardPanel().getBodyItem("nassistnum").setDecimalDigits(
					iaDigit[1]);
			getBillCardPanel().getBodyItem("nsuggestprice").setDecimalDigits(
					iaDigit[2]);
			getBillCardPanel().getBodyItem("nmaxprice").setDecimalDigits(
					iaDigit[2]);
			getBillCardPanel().getBodyItem("nmoney").setDecimalDigits(nMoney);
			getBillCardPanel().getBodyItem("nexchangerate").setDecimalDigits(
					iaDigit[3]);
		} else {
			// LIST
			getBillListPanel().getBodyItem("npraynum").setDecimalDigits(
					iaDigit[0]);
			getBillListPanel().getBodyItem("nassistnum").setDecimalDigits(
					iaDigit[1]);
			getBillListPanel().getBodyItem("nsuggestprice").setDecimalDigits(
					iaDigit[2]);
			getBillListPanel().getBodyItem("nmaxprice").setDecimalDigits(
					iaDigit[2]);
			getBillListPanel().getBodyItem("nmoney").setDecimalDigits(nMoney);
			getBillListPanel().getBodyItem("nexchangerate").setDecimalDigits(
					iaDigit[3]);
		}
	}

	/**
	 * 设置请购单卡片可编辑性 设置表项可编辑性，先要设置整表可编辑性然后再设置表项可编辑性
	 */
	private void setEdit(boolean b) {
		// 整张单据不可编辑
		getBillCardPanel().setEnabled(false);
		// 是否可编辑
		m_bEdit = b;
		// 数量是否可编辑
		getBillCardPanel().getBodyItem("npraynum").setEnabled(b);
		getBillCardPanel().getBodyItem("npraynum").setEdit(b);
	}

	/**
	 * 作者：汪维敏 功能：批次设置自由项的可编辑性 参数：iBeginRow，iEndRow 具体行号 返回：无 例外：无 日期：(2001-4-22
	 * 11:39:21) 修改日期，修改人，修改原因，注释标志： 2003-02-26 wyf 修改为取公共函数的方法，以提高效率
	 */
	private static void setEnabled_BodyFree(BillCardPanel pnlBillCard,
			UIRefPane refpaneInv, int iBeginRow, int iEndRow) {
		if (pnlBillCard == null || refpaneInv == null) {
			SCMEnv.out("传入参数不正确！");
			return;
		}
		Object[] saMangId = ((Object[]) refpaneInv
				.getRefValues("bd_invmandoc.pk_invmandoc"));
		final int size = saMangId.length;
		String[] sMangIds = new String[size];
		for (int i = 0; i < size; i++) {
			sMangIds[i] = saMangId[i].toString();
		}
		// 批次装入
		PuTool.loadBatchFreeVO(sMangIds);
		String sMangId;
		for (int i = iBeginRow; i <= iEndRow; i++) {
			sMangId = (String) pnlBillCard.getBillModel().getValueAt(i,
					"cmangid");

			// sMangId = sMangId.substring(1,sMangId.length()-1);
			pnlBillCard.getBillModel().setCellEditable(
					i,
					"vfree",
					PuTool.isFreeMngt(sMangId)
							&& pnlBillCard.getBillModel().getItemByKey("vfree")
									.isEdit());
		}

	}

	/**
	 * 作者：汪维敏 功能：批次设置存货的管理相关信息 参数: BillCardPanel pnlBillCard 单据模板 UIRefPane
	 * refpaneInv 存货参照 int iBeginRow 开始位置 int iEndRow 结束位置 返回：无 例外：无
	 * 日期：(2004-02-11 13:45:10) 修改日期，修改人，修改原因，注释标志：
	 */
	private static void setInvEditFormulaInfo(BillCardPanel pnlBillCard,
			UIRefPane refpaneInv, int iBeginRow, int iEndRow,
			String sLoginCorpId, String sLoginCorpName) {
		if (pnlBillCard == null || refpaneInv == null) {
			SCMEnv.out("传入参数不正确！");
			return;
		}
		try {
			Object[] saMangIdRef = ((Object[]) refpaneInv
					.getRefValues("bd_invmandoc.pk_invmandoc"));
			Object[] saBaseIdRef = ((Object[]) refpaneInv
					.getRefValues("bd_invmandoc.pk_invbasdoc"));
			Object[] saMeasUnitRef = ((Object[]) refpaneInv
					.getRefValues("bd_invbasdoc.pk_measdoc"));
			Object[] saMangId = new Object[saMangIdRef.length];
			Object[] saBaseId = new Object[saBaseIdRef.length];
			Object[] saMeasUnit = new Object[saMeasUnitRef.length];
			if (saMangId == null || saBaseId == null
					|| saMangId.length != saBaseId.length) {
				Logger.debug("数据错误：存货管理档案ID为空或存货档案ID为空或二者长度不等，直接返回");
				return;
			}

			// Object[] saPurOrg = new Object[saMangId.length];
			// Object[] saEmployee = new Object[saMangId.length];
			for (int i = 0; i < saMangId.length; i++) {
				saMangId[i] = saMangIdRef[i];
				saBaseId[i] = saBaseIdRef[i];
				saMeasUnit[i] = saMeasUnitRef[i];
				// saPurOrg[i] = pnlBillCard.getBodyValueAt(iBeginRow + i,
				// "cpurorganization");
				// saEmployee[i] = pnlBillCard.getBodyValueAt(iBeginRow + i,
				// "cemployeeid");
			}
			final int iLen = saMangId.length;

			// ================解析基本档案：计量档案,辅计量主键
			String[] saFormula = new String[] {
					"getColValue(bd_measdoc,measname,pk_measdoc,cmessureunit)",
					"getColValue(bd_invbasdoc,pk_measdoc2,pk_invbasdoc,cbaseid)"
			// "getColValue(bd_purorg,name,pk_purorg,cpurorganization)",
			// "getColValue(bd_purorg,ownercorp,pk_purorg,cpurorganization)",
			// "getColValue(bd_psndoc,psnname,pk_psndoc,cemployeeid)"
			};
			PuTool.getFormulaParse().setExpressArray(saFormula);
			int iFormulaLen = saFormula.length;
			for (int i = 0; i < iFormulaLen; i++) {
				PuTool.getFormulaParse().addVariable("cmessureunit",
						saMeasUnitRef);
				PuTool.getFormulaParse().addVariable("cbaseid", saBaseIdRef);
				// PuTool.getFormulaParse().addVariable("cpurorganization",
				// saPurOrg);
				// PuTool.getFormulaParse().addVariable("cemployeeid",
				// saEmployee);
			}

			String[][] saRet = PuTool.getFormulaParse().getValueSArray();
			String[] saUnitName = new String[iLen];
			// 辅计量主键
			String[] sAssisUnit = new String[iLen];
			String[] sAssisUnitRef = new String[iLen];
			// String[] sPurOrgName = new String[iLen];
			// String[] sOwnerCorp = new String[iLen];
			// String[] sEmployeeName = new String[iLen];
			if (saRet != null) {
				for (int i = 0; i < iLen; i++) {
					if (saRet[0] != null) {
						saUnitName[i] = saRet[0][i];
					}
					if (saRet[1] != null) {
						sAssisUnit[i] = saRet[1][i].toString();
						sAssisUnitRef[i] = saRet[1][i].toString();
					}
					// if (saRet[2] != null)
					// sPurOrgName[i] = saRet[2][i].toString();
					// if (saRet[3] != null) {
					// sOwnerCorp[i] = saRet[3][i].toString();
					// if (sOwnerCorp[i] == null ||
					// sOwnerCorp[i].trim().length() == 0)
					// sOwnerCorp[i] = sLoginCorpId;
					// }
					// if (saRet[4] != null)
					// sEmployeeName[i] = saRet[4][i].toString();
				}
			}
			// ================解析管理档案:产地、最高限价
			saFormula = new String[] {
					"getColValue(bd_invmandoc,prodarea,pk_invmandoc,cmangid)",
					"getColValue(bd_invmandoc,maxprice,pk_invmandoc,cmangid)",
					"getColValue(bd_measdoc,measname,pk_measdoc,cassistunit)"
			// "getColValue(bd_corp,unitname,pk_corp,ownercorp)"
			};
			iFormulaLen = saFormula.length;
			PuTool.getFormulaParse().setExpressArray(saFormula);

			for (int i = 0; i < iFormulaLen; i++) {
				PuTool.getFormulaParse().addVariable("cmangid", saMangIdRef);
				PuTool.getFormulaParse().addVariable("cassistunit",
						sAssisUnitRef);
				// PuTool.getFormulaParse().addVariable("ownercorp",
				// sOwnerCorp);
			}

			saRet = PuTool.getFormulaParse().getValueSArray();
			// 产地
			String[] saArea = new String[iLen];
			// 最高限价
			UFDouble[] uMaxPrice = new UFDouble[iLen];
			// 辅计量单位
			String[] sAssisUnitName = new String[iLen];
			// String[] sPurCorpName = new String[iLen];
			if (saRet != null) {
				for (int i = 0; i < iLen; i++) {
					if (saRet[0] != null) {
						saArea[i] = saRet[0][i];
					}
					if (saRet[1] != null) {
						uMaxPrice[i] = new UFDouble(saRet[1][i]);
					}
					if (saRet[2] != null) {
						sAssisUnitName[i] = saRet[2][i];
					}
					// if (saRet[3] != null)
					// sPurCorpName[i] = saRet[3][i].trim();
				}
			}
			// ================对表体各行设置值
			Object[] saCode = ((Object[]) refpaneInv
					.getRefValues("bd_invbasdoc.invcode"));
			Object[] saName = ((Object[]) refpaneInv
					.getRefValues("bd_invbasdoc.invname"));
			Object[] saSpec = ((Object[]) refpaneInv
					.getRefValues("bd_invbasdoc.invspec"));
			Object[] saType = ((Object[]) refpaneInv
					.getRefValues("bd_invbasdoc.invtype"));

			// 执行表体公式
			int iPkIndex = 0;
			for (int i = iBeginRow; i < iEndRow; i++) {
				iPkIndex = i - iBeginRow;
				// 管理ID
				pnlBillCard.setBodyValueAt(saMangId[iPkIndex], i, "cmangid");
				// 基本ID
				pnlBillCard.setBodyValueAt(saBaseId[iPkIndex], i, "cbaseid");
				// 编码
				pnlBillCard.setBodyValueAt(saCode[iPkIndex], i,
						"cinventorycode");
				// 名称
				pnlBillCard.setBodyValueAt(saName[iPkIndex], i,
						"cinventoryname");
				// 规格
				pnlBillCard.setBodyValueAt(saSpec[iPkIndex], i,
						"cinventoryspec");
				// 型号
				pnlBillCard.setBodyValueAt(saType[iPkIndex], i,
						"cinventorytype");
				// 计量单位NAME
				pnlBillCard.setBodyValueAt(saUnitName[iPkIndex], i,
						"cinventoryunit");
				// 产地
				pnlBillCard.setBodyValueAt(saArea[iPkIndex], i, "cprodarea");
				// 最高限价
				pnlBillCard.setBodyValueAt(uMaxPrice[iPkIndex], i, "nmaxprice");
				// 辅计量单位
				pnlBillCard.setBodyValueAt(sAssisUnitName[iPkIndex], i,
						"cassistunitname");
				// 辅计量ID
				pnlBillCard.setBodyValueAt(sAssisUnit[iPkIndex], i,
						"cassistunit");
			}
		} catch (Exception e) {
			Logger.debug("录入多存货时设置出错");
		}
	}

	/**
	 * 设置列表按钮逻辑
	 */
	private void setButtonsList() {

		// 列表特殊
		m_btnCard.setName(m_lanResTool.getStrByID("SCMCOMMON",
				"UPPSCMCommon-000463")/*
									 * @res "卡片显示"
									 */);
		// 卡片功能
		m_btnBusiTypes.setEnabled(false);
		//
		m_btnAdds.setEnabled(false);
		//
		m_btnLines.setEnabled(false);
		m_btnAddLine.setEnabled(false);
		m_btnDelLine.setEnabled(false);
		m_btnPstLine.setEnabled(false);
		m_btnCpyLine.setEnabled(false);
		//
		m_btnBrowses.setEnabled(false);
		m_btnFirst.setEnabled(false);
		m_btnPrev.setEnabled(false);
		m_btnNext.setEnabled(false);
		m_btnLast.setEnabled(false);
		//
		m_btnSave.setEnabled(false);
		m_btnCancel.setEnabled(false);

		/* 暂不提供列表价格论证表功能 */
		m_btnPriceInfo.setEnabled(false);
		/* 暂不支持的列表功能，基础控件没有列表方法 */
		m_btnCombin.setEnabled(false);

		// 总有效的
		m_btnMaintains.setEnabled(true);
		m_btnActions.setEnabled(true);
		m_btnQuery.setEnabled(true);
		m_btnBrowses.setEnabled(true);
		m_btnPrints.setEnabled(true);
		m_btnOthersQry.setEnabled(true);
		m_btnOthersFuncs.setEnabled(true);

		// 查询过才有效
		m_btnRefresh.setEnabled(m_bQueried);

		/*
		 * 其它数据相关按钮，包括：
		 * 
		 * 全消、预览、打印、文档管理
		 * 
		 * 全选、卡片显示、修改、作废、复制、送审、审批、弃审、关闭、打开、合并显示、联查、存量查询、审批流状态
		 */
		int iSelectedCnt = getBillListPanel().getHeadTable()
				.getSelectedRowCount();
		int iCacheDataCnt = getBillListPanel().getHeadBillModel().getRowCount();
		if (m_VOs == null || m_VOs.length <= 0) {
			iCacheDataCnt = 0;
		}
		/* 没有数据 */
		if (iCacheDataCnt <= 0) {
			m_btnCard.setEnabled(true);
			m_btnSelectNo.setEnabled(false);
			m_btnPrintPreview.setEnabled(false);
			m_btnPrint.setEnabled(false);
			m_btnDocument.setEnabled(false);
			m_btnSelectAll.setEnabled(false);
			m_btnModify.setEnabled(false);
			m_btnDiscard.setEnabled(false);
			m_btnCopy.setEnabled(false);
			m_btnSendAudit.setEnabled(false);
			m_btnApprove.setEnabled(false);
			m_btnUnApprove.setEnabled(false);
			m_btnClose.setEnabled(false);
			m_btnOpen.setEnabled(false);
			m_btnCombin.setEnabled(false);
//			m_btnLinkBillsBrowse.setEnabled(false);
			m_btnLinkBillsBrowse.setEnabled(true);
			m_btnUsable.setEnabled(false);
			m_btnWorkFlowBrowse.setEnabled(false);
			//
			updateButtonsAll();
			return;
		}
		/* 有数据但没有选取任何单据 */
		if (iSelectedCnt <= 0) {
			m_btnSelectAll.setEnabled(true);
			m_btnCard.setEnabled(false);
			m_btnSelectNo.setEnabled(false);
			m_btnPrintPreview.setEnabled(false);
			m_btnPrint.setEnabled(false);
			m_btnDocument.setEnabled(false);
			m_btnModify.setEnabled(false);
			m_btnDiscard.setEnabled(false);
			m_btnCopy.setEnabled(false);
			m_btnSendAudit.setEnabled(false);
			m_btnApprove.setEnabled(false);
			m_btnUnApprove.setEnabled(false);
			m_btnClose.setEnabled(false);
			m_btnOpen.setEnabled(false);
			m_btnCombin.setEnabled(false);
			m_btnLinkBillsBrowse.setEnabled(false);
			m_btnUsable.setEnabled(false);
			m_btnWorkFlowBrowse.setEnabled(false);
			//
			updateButtonsAll();
			return;
		}

		/* 有数据 且 选取单据数大于等于1 */
		m_btnSelectNo.setEnabled(true);
		m_btnPrintPreview.setEnabled(true);
		m_btnPrint.setEnabled(true);
		m_btnDocument.setEnabled(true);

		// 全选
		if (iSelectedCnt != iCacheDataCnt) {
			m_btnSelectAll.setEnabled(true);
		} else {
			m_btnSelectAll.setEnabled(false);
		}

		/* 卡片显示、复制、合并显示、联查、存量查询、审批流状态 */
		boolean bOnlyOneSelected = (iSelectedCnt == 1);
		m_btnCard.setEnabled(bOnlyOneSelected);
		m_btnCopy.setEnabled(bOnlyOneSelected);
		// 暂不支持的列表功能，基础控件没有列表方法
		// m_btnCombin.setEnabled(bOnlyOneSelected);
		m_btnLinkBillsBrowse.setEnabled(bOnlyOneSelected);
		m_btnUsable.setEnabled(bOnlyOneSelected);
		m_btnWorkFlowBrowse.setEnabled(bOnlyOneSelected);

		/* 修改、作废、送审、审批、弃审、关闭、打开 */
		if (bOnlyOneSelected) {
			/* 只有一张单据选中的情况 */
			int iBillStatus = -1;
			if (m_nPresentRecord >= 0
					&& m_VOs[m_nPresentRecord] != null
					&& m_VOs[m_nPresentRecord].getHeadVO().getIbillstatus() != null) {
				iBillStatus = m_VOs[m_nPresentRecord].getHeadVO()
						.getIbillstatus().intValue();
			}
			if (iBillStatus == 0) {
				m_btnModify.setEnabled(true);
				m_btnDiscard.setEnabled(true);
				m_btnSendAudit.setEnabled(isNeedSendAudit(iBillStatus));
				m_btnApprove.setEnabled(true);
				m_btnUnApprove.setEnabled(false);
				m_btnClose.setEnabled(false);
				m_btnOpen.setEnabled(false);
			} else if (iBillStatus == 1) {
				m_btnModify.setEnabled(false);
				m_btnDiscard.setEnabled(false);
				m_btnSendAudit.setEnabled(isNeedSendAudit(iBillStatus));
				m_btnApprove.setEnabled(false);
				m_btnUnApprove.setEnabled(false);
				m_btnClose.setEnabled(false);
				m_btnOpen.setEnabled(true);
			} else if (iBillStatus == 2) {
				m_btnModify.setEnabled(false);
				m_btnDiscard.setEnabled(false);
				m_btnSendAudit.setEnabled(isNeedSendAudit(iBillStatus));
				m_btnApprove.setEnabled(true);
				m_btnUnApprove.setEnabled(false);
				m_btnClose.setEnabled(false);
				m_btnOpen.setEnabled(false);
			} else if (iBillStatus == 3) {
				m_btnModify.setEnabled(false);
				m_btnDiscard.setEnabled(false);
				m_btnSendAudit.setEnabled(isNeedSendAudit(iBillStatus));
				m_btnApprove.setEnabled(false);
				m_btnUnApprove.setEnabled(true);
				m_btnClose.setEnabled(true);
				m_btnOpen.setEnabled(false);
			} else if (iBillStatus == 4) {
				m_btnModify.setEnabled(true);
				m_btnDiscard.setEnabled(true);
				m_btnSendAudit.setEnabled(isNeedSendAudit(iBillStatus));
				m_btnApprove.setEnabled(false);
				m_btnUnApprove.setEnabled(false);
				m_btnClose.setEnabled(false);
				m_btnOpen.setEnabled(false);
			}
		} else {
			m_btnModify.setEnabled(true);
			m_btnDiscard.setEnabled(true);
			m_btnApprove.setEnabled(true);
			m_btnUnApprove.setEnabled(true);
			// 暂不支持批量关闭、打开
			m_btnClose.setEnabled(false);
			m_btnOpen.setEnabled(false);
		}
		// 刷新按钮状态
		updateButtonsAll();
	}

	/**
	 * 列表换行时发生并发后列表按钮逻辑设置。
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * 使用示例
	 * <p>
	 * <b>参数说明</b>
	 * <p>
	 * 
	 * @author czp
	 * @time 2007-3-14 上午10:21:56
	 */
	private void setButtonsListWhenErr() {
		setButtonsEnabled(false);
		/* 可用:查询,刷新 */
		m_btnBrowses.setEnabled(true);
		m_btnQuery.setEnabled(true);
		m_btnRefresh.setEnabled(m_bQueried);
		/* 刷新按钮状态 */
		updateButtonsAll();
	}

	/**
	 * 功能描述:设置请购单表体：存货名称，存货规格，存货型号，产地，主计量，到货地址不可编辑
	 * 设置请购单表头：业务类型，请购来源，审批人，制单人，审批日期不可编辑
	 */
	private static void setPartNoEditable(BillCardPanel bcp) {
		BillItem item = bcp.getBodyItem("cinventoryname");
		item.setEnabled(false);
		item = bcp.getBodyItem("cinventoryspec");
		item.setEnabled(false);
		item = bcp.getBodyItem("cinventorytype");
		item.setEnabled(false);
		item = bcp.getBodyItem("cinventoryunit");
		item.setEnabled(false);
		item = bcp.getBodyItem("caddress");
		item.setEnabled(false);
		item = bcp.getBodyItem("cprodarea");
		item.setEnabled(false);

		item = bcp.getHeadItem("cbiztype");
		item.setEnabled(false);
		item = bcp.getHeadItem("ipraysource");
		item.setEnabled(false);

		item = bcp.getTailItem("cauditpsn");
		item.setEnabled(false);
		item = bcp.getTailItem("coperator");
		item.setEnabled(false);
		item = bcp.getTailItem("dauditdate");
		item.setEnabled(false);
	}

	/**
	 * 项目阶段可编辑性
	 */
	private void setProjPhaseEditable(BillEditEvent e) {
		final int n = e.getRow();
		if (n < 0)
			return;
		// 判断项目是否为空。若为空，则项目阶段不可编辑
		Object oTemp = getBillCardPanel().getBodyValueAt(n, "cprojectname");
		if ("cprojectname".equals(e.getKey())) {
			if (oTemp == null || oTemp.toString().length() == 0) {
				getBillCardPanel().getBillModel().setCellEditable(n,
						"cprojectphasename", false);
				getBillCardPanel().setBodyValueAt(null, n, "cprojectphasename");
			} else {
				getBillCardPanel().getBillModel().setCellEditable(
						n,
						"cprojectphasename",
						getBillCardPanel().getBillModel().getItemByKey(
								"cprojectphasename").isEdit());
				oTemp = getBillCardPanel().getBodyValueAt(n, "cprojectid");
				if (oTemp != null && oTemp.toString().length() > 0) {
					UIRefPane nRefPanel = (UIRefPane) getBillCardPanel()
							.getBodyItem("cprojectphasename").getComponent();
					nRefPanel.setIsCustomDefined(true);
					nRefPanel.setRefModel(new ProjectPhase((String) oTemp));

					getBillCardPanel().setBodyValueAt(null, n,
							"cprojectphasename");
				}
			}
		}
	}

	/**
	 * 设置辅计量参照
	 * 
	 * @param bcp
	 * @param row
	 * @since V50
	 */
	private static void setRefPaneAssistunit(BillCardPanel bcp, int row) {
		Object cbaseid = bcp.getBillModel().getValueAt(row, "cbaseid");
		bcp.getBillModel().getValueAt(row, "cinventoryunit");

		UIRefPane ref = (UIRefPane) bcp.getBodyItem("cassistunitname")
				.getComponent();
		String wherePart = "bd_convert.pk_invbasdoc='" + cbaseid + "' ";
		ref.setWhereString(wherePart);
		StringBuffer unionPart = new StringBuffer();
		unionPart.append(" union all \n");
		unionPart
				.append("(select bd_measdoc.shortname,bd_measdoc.measname,bd_invbasdoc.pk_measdoc \n");
		unionPart.append("from bd_invbasdoc \n");
		unionPart.append("left join bd_measdoc  \n");
		unionPart.append("on bd_invbasdoc.pk_measdoc=bd_measdoc.pk_measdoc \n");
		unionPart.append("where bd_invbasdoc.pk_invbasdoc='" + cbaseid
				+ "') \n");
		ref.getRefModel().setGroupPart(unionPart.toString());
	}

	/**
	 * 设置参照的限制条件
	 * 
	 * @return void
	 * @since 2001-04-28
	 */
	private void setRefPaneByPkcorp(String pk_corp) {

		if (pk_corp == null)
			return;

		BillData bd = getBillCardPanel().getBillData();
		int headItemsLength = bd.getHeadItems().length;
		String key;
		UIRefPane refpane;
		for (int i = 0; i < headItemsLength; i++) {
			if (bd.getHeadItems()[i].getDataType() == BillItem.UFREF) {
				key = bd.getHeadItems()[i].getKey();
				refpane = (UIRefPane) (bd.getHeadItem(key).getComponent());
				bd.getHeadItem(key).setComponent(
						getRefWherePart(refpane, pk_corp, key));
			}
		}
		int bodyItemsLength = bd.getBodyItems().length;
		for (int i = 0; i < bodyItemsLength; i++) {
			if (bd.getBodyItems()[i].getDataType() == BillItem.UFREF) {
				key = bd.getBodyItems()[i].getKey();
				refpane = (UIRefPane) (bd.getBodyItem(key).getComponent());
				bd.getBodyItem(key).setComponent(
						getRefWherePart(refpane, pk_corp, key));
			}
		}
		getBillCardPanel().setBillData(bd);
		nc.ui.pu.pub.PuTool.setTranslateRender(getBillCardPanel());
	}

	/**
	 * 功能:数量、金额、换算率、辅数量可编辑性、辅计量参照内容及可编辑性
	 * 
	 * @since V50
	 */
	public static void beforeEditBodyAssistUnitNumber(BillCardPanel bcp,
			int iRow) {
		try {
			String strCbaseid;
			String cassistunit;
			Object oTmp;
			String ass;
			String main;
			UFDouble ufdConv;
			// 是否进行辅计量管理
			strCbaseid = (String) bcp.getBillModel()
					.getValueAt(iRow, "cbaseid");
			// 有存货
			if (strCbaseid != null && !strCbaseid.trim().equals("")) {
				if (PuTool.isAssUnitManaged(strCbaseid)) {
					// 设置辅计量参照
					setRefPaneAssistunit(bcp, iRow);
					cassistunit = (String) bcp.getBillModel().getValueAt(iRow,
							"cassistunit");
					// 辅计量不为空
					oTmp = bcp.getBillModel().getValueAt(iRow, "nexchangerate");
					if (oTmp == null || oTmp.toString().trim().equals("")) {
						// 设置换算率(如果原来存在换算率则不设置，因为可能是已经改变了的非固定换算率)
						ufdConv = PuTool.getInvConvRateValue(strCbaseid,
								cassistunit);
						bcp.getBillModel().setValueAt(ufdConv, iRow,
								"nexchangerate");
					}
					// 设置可编辑性
					bcp.setCellEditable(iRow, "cassistunitname", true);
					bcp.setCellEditable(iRow, "npraynum", true);
					bcp.setCellEditable(iRow, "nassistnum", true);
					bcp.setCellEditable(iRow, "nmoney", true);
					bcp.setCellEditable(iRow, "nexchangerate", true);
					// 如果辅计量是固定换算率
					if (PuTool.isFixedConvertRate(strCbaseid, cassistunit)) {
						bcp.setCellEditable(iRow, "nexchangerate", false);
					} else {
						bcp.setCellEditable(iRow, "nexchangerate", true);
					}
					// 如果是主辅计量相同,则换算率不可编辑
					ass = (String) bcp.getBillModel().getValueAt(iRow,
							"cassistunitname");
					main = (String) bcp.getBillModel().getValueAt(iRow,
							"cinventoryunit");
					if (ass != null && ass.equals(main)) {
						bcp.getBillModel().setValueAt(new UFDouble(1), iRow,
								"nexchangerate");
						bcp.setCellEditable(iRow, "nexchangerate", false);
					}
				} else {
					bcp.setCellEditable(iRow, "npraynum", true);
					bcp.setCellEditable(iRow, "nmoney", true);
					bcp.setCellEditable(iRow, "nexchangerate", false);
					bcp.setCellEditable(iRow, "nassistnum", false);
					bcp.setCellEditable(iRow, "cassistunitname", false);
				}
			}
			// 无存货
			else {
				bcp.setCellEditable(iRow, "npraynum", false);
				bcp.setCellEditable(iRow, "nmoney", false);
				bcp.setCellEditable(iRow, "nexchangerate", false);
				bcp.setCellEditable(iRow, "nassistnum", false);
				bcp.setCellEditable(iRow, "cassistunitname", false);
			}
		} catch (Exception e) {
			Logger.debug("录入多存货时设置出错");
		}
	}

	/**
	 * 此处插入方法说明。 创建日期：(2003-10-28 14:42:54)
	 */
	private void setSendAuditBtnState() {
		if (getPraybillVOs() == null || getPraybillVOs().length <= 0) {
			m_btnSendAudit.setEnabled(false);
			return;
		}
		PraybillVO curVO = getPraybillVOs()[getCurVoPos()];
		if (curVO == null)
			return;
		// 送审
		if (PuTool.isNeedSendToAudit(nc.vo.scm.pu.BillTypeConst.PO_PRAY, curVO)
				&& !isAlreadySendToAudit) {
			m_btnSendAudit.setEnabled(true);
		} else {
			m_btnSendAudit.setEnabled(false);
		}
	}

	/**
	 * 功能：向单据模板中加入缓存VO(请购单维护浏览) 作者：晁志平 日期：(2003-3-12 10:27:34)
	 */
	private void setVoToBillCard(int iRollBackPos, String strmsg) {
		String strMsg = null;
		if (strmsg == null || strmsg.trim().equals("")) {
			strMsg = "";
		} else {
			strMsg = strmsg.trim();
		}
		if (m_VOs == null || m_VOs.length <= 0) {
			Logger.debug("缓存中无数据");
			m_nPresentRecord = iRollBackPos;
			String[] value = new String[] { strMsg };
			showHintMessage(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000440", null, value));
			return;
		}
		try {
			m_VOs[m_nPresentRecord] = PrTool
					.getRefreshedVO(m_VOs[m_nPresentRecord]);
			// 计算自由项
			new nc.ui.scm.pub.FreeVOParse().setFreeVO(m_VOs[m_nPresentRecord]
					.getBodyVO(), "vfree", "vfree", "cbaseid", "cmangid", true);
			getBillCardPanel().getBillModel().clearBodyData();
		} catch (Exception be) {
			if (be instanceof BusinessException)
				MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
						"SCMCOMMON", "UPPSCMCommon-000422")/* "业务异常" */, be
						.getMessage());
			m_nPresentRecord = iRollBackPos;
			String[] value = new String[] { strMsg };
			showHintMessage(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000440", null, value));
			return;
		}

		// 显示该张单据
		for (int i = 0; i < 1; i++) {
			try {
				getBillCardPanel().setBillValueVO(m_VOs[m_nPresentRecord]);
			} catch (Exception e) {
				continue;
			}
		}
		nc.ui.pu.pub.PuTool.loadInvMaxPrice(getBillCardPanel());
		getBillCardPanel().getBillModel().execLoadFormula();
		// 显示来源信息
		nc.ui.pu.pub.PuTool.loadSourceInfoAll(getBillCardPanel(),
				nc.vo.scm.pu.BillTypeConst.PO_PRAY);
		getBillCardPanel().updateValue();
		// 显示下拉列表信息
		Integer nTemp1 = (Integer) m_VOs[m_nPresentRecord].getParentVO()
				.getAttributeValue("ipraysource");
		Integer nTemp2 = (Integer) m_VOs[m_nPresentRecord].getParentVO()
				.getAttributeValue("ipraytype");
		m_comPraySource.setSelectedIndex(nTemp1.intValue());
		m_comPrayType.setSelectedIndex(nTemp2.intValue());
		getBillCardPanel().updateUI();
		// 显示备注
		UIRefPane nRefPanel3 = (UIRefPane) getBillCardPanel().getHeadItem(
				"vmemo").getComponent();
		nRefPanel3.setValue(m_VOs[m_nPresentRecord].getHeadVO().getVmemo());
		String[] value = new String[] { strMsg };
		showHintMessage(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000085", null, value));
		// 填充表头公式数据，避免不可参照出的数据
		execHeadTailFormula(m_VOs[m_nPresentRecord]);
	}

	/**
	 * 功能：向单据界面显示请购单(审批流程用) 注意：此时的 m_VOs 已经含用自由项信息 参数： 返回： 例外： 日期：(2002-6-27
	 * 14:06:10) 修改日期，修改人，修改原因，注释标志：
	 */
	private void setVoToBillPanel() {
		if (m_VOs != null && m_VOs.length > 0) {
			// 默认显示第一张
			m_nPresentRecord = 0;
			// 加载表体
			try {
				m_VOs[m_nPresentRecord] = PrTool
						.getRefreshedVO(m_VOs[m_nPresentRecord]);
				// 计算自由项
				new nc.ui.scm.pub.FreeVOParse().setFreeVO(
						m_VOs[m_nPresentRecord].getBodyVO(), "vfree", "vfree",
						"cbaseid", "cmangid", true);
			} catch (Exception be) {
				if (be instanceof BusinessException)
					MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
							"SCMCOMMON", "UPPSCMCommon-000422")/* "业务异常" */,
							be.getMessage());
				return;
			}
			if (!(m_VOs[m_nPresentRecord].getHeadVO().getPk_corp()
					.equals(m_sLoginCorpId))) {
				setRefPaneByPkcorp(m_VOs[m_nPresentRecord].getHeadVO()
						.getPk_corp());
				setDecimalDigits(m_VOs[m_nPresentRecord].getHeadVO()
						.getPk_corp(), 0);
			}
			// 返回查询结果
			Integer nTemp1 = (Integer) m_VOs[m_nPresentRecord].getHeadVO()
					.getAttributeValue("ipraysource");
			Integer nTemp2 = (Integer) m_VOs[m_nPresentRecord].getHeadVO()
					.getAttributeValue("ipraytype");
			m_comPraySource.setSelectedIndex(nTemp1.intValue());
			m_comPrayType.setSelectedIndex(nTemp2.intValue());
			Vector v = new Vector();
			for (int i = 0; i < m_VOs.length; i++)
				v.addElement(m_VOs[i].getHeadVO());
			PraybillHeaderVO[] headVO = new PraybillHeaderVO[v.size()];
			v.copyInto(headVO);
			getBillCardPanel().setBillValueVO(m_VOs[m_nPresentRecord]);
			nc.ui.pu.pub.PuTool.loadInvMaxPrice(getBillCardPanel());
			getBillCardPanel().getBillModel().execLoadFormula();
			// 显示来源信息
			nc.ui.pu.pub.PuTool.loadSourceInfoAll(getBillCardPanel(),
					nc.vo.scm.pu.BillTypeConst.PO_PRAY);
			// 填充表头公式数据，避免不可参照出的数据
			execHeadTailFormula(m_VOs[m_nPresentRecord]);
			getBillCardPanel().updateValue();
			// 显示备注
			UIRefPane nRefPanel3 = (UIRefPane) getBillCardPanel().getHeadItem(
					"vmemo").getComponent();
			nRefPanel3.setValue(m_VOs[m_nPresentRecord].getHeadVO().getVmemo());
			m_btnWorkFlowBrowse.setEnabled(true);
			m_btnAudit.setEnabled(true);
			updateButtons();
		} else {
			m_VOs = null;
			// 无返回查询结果
			MessageDialog.showHintDlg(this, m_lanResTool.getStrByID("40040101",
					"UPP40040101-000055")/* "请购单查询" */, m_lanResTool.getStrByID(
					"40040101", "UPP40040101-000056")/* "没有符合条件的请购单！" */);
			// 清空数据
			getBillCardPanel().getBillData().clearViewData();
			getBillCardPanel().updateUI();
			m_btnWorkFlowBrowse.setEnabled(false);
			m_btnAudit.setEnabled(false);
			updateButtons();
		}
	}

	/**
	 * 作者：方益 功能：设定某行的批次号的可编辑性． 参数：iRow int 批次号所在的行． 返回：无 例外：无 日期：(2002-6-25
	 * 11:39:21) 修改日期，修改人，修改原因，注释标志：
	 */
	private void setVProduceNumEditState(int iRow) {

		String sMangId = (String) getBillCardPanel().getBodyValueAt(iRow,
				"cmangid");
		if (sMangId == null || sMangId.trim().length() < 1) {
			// =====可编辑
			getBillCardPanel().setCellEditable(iRow, "vproducenum", true);

		} else {
			// =====是否可编辑
			getBillCardPanel().setCellEditable(iRow, "vproducenum",
					nc.ui.pu.pub.PuTool.isBatchManaged(sMangId));
		}
	}

	/**
	 * 作者：晁志平 功能：实现ListSelectionListener的监听方法 参数：ListSelectionEvent e 监听事件 返回：无
	 * 例外：无 日期：(2002-5-23 11:39:21) 修改日期，修改人，修改原因，注释标志：
	 */
	public void valueChanged(ListSelectionEvent e) {
		/**/
		boolean isErr = false;
		/* 选取的行数 */
		int iSelCnt = -1;
		int iSelFirstRow = -1;
		/* 先所有行初始为未选中 */
		final int iCount = getBillListPanel().getHeadTable().getRowCount();
		for (int i = 0; i < iCount; i++) {
			getBillListPanel().getHeadBillModel().setRowState(i,
					BillModel.NORMAL);
		}
		/* 得到被选中的行 */
		int[] selectedRows = getBillListPanel().getHeadTable()
				.getSelectedRows();
		if (selectedRows != null && selectedRows.length > 0) {
			iSelCnt = selectedRows.length;
			iSelFirstRow = selectedRows[0];
		}
		/* 再选中的行表示为打＊号 */
		if (iSelCnt > 0) {
			for (int i = 0; i < iSelCnt; i++) {
				getBillListPanel().getHeadBillModel().setRowState(
						selectedRows[i], BillModel.SELECTED);
			}
		}
		/* 显示被选中的行和表体 */
		if (iSelCnt != 1) {
			getBillListPanel().getBodyBillModel().setBodyDataVO(null);
		} else {
			/* 有且仅当表头只有一行被选择时:重置表体位置(支持排序) */
			m_nPresentRecord = nc.ui.pu.pub.PuTool.getIndexBeforeSort(
					getBillListPanel(), iSelFirstRow);
			/* 在浏览时才加载表体 */
			if (m_VOs != null && m_VOs.length > 0) {
				try {
					m_VOs[m_nPresentRecord] = PrTool
							.getRefreshedVO(m_VOs[m_nPresentRecord]);
					// 计算自由项
					new nc.ui.scm.pub.FreeVOParse().setFreeVO(
							m_VOs[m_nPresentRecord].getBodyVO(), "vfree",
							"vfree", "cbaseid", "cmangid", true);

				} catch (Exception be) {
					getBillListPanel().getBodyBillModel().clearBodyData();
					if (be instanceof BusinessException)
						MessageDialog.showErrorDlg(this,
								m_lanResTool.getStrByID("SCMCOMMON",
										"UPPSCMCommon-000422")/* "业务异常" */, be
										.getMessage());
					isErr = true;
				}
			}
			/* 表体加载 */
			PraybillItemVO[] bodyVO = m_VOs[m_nPresentRecord].getBodyVO();
			getBillListPanel().getBodyBillModel().setBodyDataVO(bodyVO);
			nc.ui.pu.pub.PuTool.loadInvMaxPrice(getBillListPanel());
			getBillListPanel().getBodyBillModel().execLoadFormula();
			/* 加载单据来源信息 */
			nc.ui.pu.pub.PuTool.loadSourceInfoAll(getBillListPanel(),
					nc.vo.scm.pu.BillTypeConst.PO_PRAY);
			getBillListPanel().getBodyBillModel().updateValue();
			getBillListPanel().updateUI();
		}
		/* 设置列表按钮逻辑 */
		setButtonsList();
		/* 置列表标志 */
		m_nUIState = 1;
		showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
				"UPPSCMCommon-000393")/* "换行浏览" */);
		if (isErr) {
			setButtonsListWhenErr();
			showHintMessage(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000086")/* "换行时加载表体出现并发" */);
		}
	}

	/**
	 * 批量打印接口，实现向卡片设置数据，并完成卡片公司、精度设置等
	 * 
	 * @see nc.ui.scm.pub.print.ISetBillVO#setBillVO(nc.vo.pub.AggregatedValueObject)
	 */
	public void setBillVO(AggregatedValueObject vo) {
		setVoToBillCard(m_nPresentRecord, "");
	}

	/** 为二次开发提供接口 ********************************** */
	/**
	 * 覆盖方法：nc.ui.pr.pray.PrayUI.java.getExtendBtns 创建日期：2005-9-22
	 * 功能描述：获取扩展按钮数组（只提供卡片界面按钮）
	 */
	public ButtonObject[] getExtendBtns() {
		return null;
	}

	/**
	 * 覆盖方法：nc.ui.pr.pray.PrayUI.java.onExtendBtnsClick 创建日期：2005-9-22
	 * 功能描述：控制扩展按钮的事件
	 */
	public void onExtendBtnsClick(ButtonObject bo) {
		// 子类实现
	}

	/**
	 * 覆盖方法：nc.ui.pr.pray.PrayUI.java.setExtendBtnsStat 创建日期：2005-9-22
	 * 功能描述：设置扩展按钮状态 状态值描述：0---初始化 1---卡片浏览 2---卡片编辑 3---列表浏览（暂不支持）
	 */
	public void setExtendBtnsStat(int iState) {
		// 子类实现
	}

	/**
	 * @功能：存货编辑后设置采购员、采购组织、采购公司
	 */
	public static void setInfosAfterInv(BillCardPanel bcp, int iBgn, int iEnd)
			throws BusinessException {

		try {
			String beanName = ICentralPurRule.class.getName();
			ICentralPurRule bo = (ICentralPurRule) nc.bs.framework.common.NCLocator
					.getInstance().lookup(beanName);
			String beanNameA = IScmPosInv.class.getName();
			IScmPosInv boA = (IScmPosInv) nc.bs.framework.common.NCLocator
					.getInstance().lookup(beanNameA);
			IsCentralVO[] para = null;
			IsCentralVO paraTemp = null;
			CentralResultVO[] result = null;
			CentralResultVO resultTemp = null;

			Vector v = new Vector(), v1 = new Vector(), v2 = new Vector();
			String cbaseids[] = null, pk_calbody[] = null;
			BillModel bm = bcp.getBillModel();
			// 取查询集采参数
			for (int i = iBgn; i < iEnd; i++) {
				paraTemp = new IsCentralVO();
				paraTemp.setCrowid(new Integer(i).toString());
				paraTemp.setPk_corp((String) bm.getValueAt(i, "pk_reqcorp"));
				paraTemp.setPk_invbasdoc((String) bm.getValueAt(i, "cbaseid"));
				v.addElement(paraTemp);
				v1.addElement(bm.getValueAt(i, "cbaseid"));
				v2.addElement(bm.getValueAt(i, "pk_reqcorp"));
			}
			if (v1.size() > 0) {
				cbaseids = new String[v1.size()];
				v1.copyInto(cbaseids);
				pk_calbody = new String[v2.size()];
				v2.copyInto(pk_calbody);
			}
			// 设置采购员、采购组织ID、采购公司
			if (v.size() > 0) {
				para = new IsCentralVO[v.size()];
				v.copyInto(para);
				result = bo.isCentralPur(para);
				if (result != null && result.length > 0
						&& result.length == iEnd - iBgn) {
					for (int i = 0; i < result.length; i++) {
						resultTemp = result[i];
						if (resultTemp.getIsCentralPur()) {
							// 采购员
							bm.setValueAt(boA.getPurchaser(resultTemp
									.getPk_corp(), (String) bm.getValueAt(i
									+ iBgn, "cbaseid")), i + iBgn,
									"cemployeeid");
							// 采购组织ID
							bm.setValueAt(resultTemp.getPk_purorg(), iBgn + i,
									"cpurorganization");
							// 采购公司
							bm.setValueAt(resultTemp.getPk_corp(), iBgn + i,
									"pk_purcorp");
						} else {
							// 采购员
							bm.setValueAt(boA.getPurchaser(bcp.getHeadItem(
									"pk_corp").getValue(), (String) bm
									.getValueAt(i + iBgn, "cbaseid")),
									i + iBgn, "cemployeeid");
							// 采购组织ID
							bm.setValueAt(resultTemp.getPk_purorg(), iBgn + i,
									"cpurorganization");
						}
					}
				}
			}
		} catch (Exception e) {
			PuTool.outException(e);
		}
	}

	/**
	 * V5 ,支持用户定义不可编辑项目修改
	 */
	public boolean beforeEdit(BillItemEvent e) {
		if (!e.getItem().isEdit()) {
			return false;
		}
		return true;
	}

	/**
	 * <p>
	 * 排序方法，返回要排序的缓存VO数组
	 * 
	 * @since V50
	 */
	public Object[] getRelaSortObjectArray() {
		return m_VOs;
	}

	/**
	 * <p>
	 * 排序方法，返回要排序的当前VO表体VO数组
	 * 
	 * @since V50
	 */
	public Object[] getRelaSortObjectArrayBody() {
		if (m_VOs != null && m_VOs.length > 0 && m_nPresentRecord >= 0
				&& m_VOs[m_nPresentRecord] != null
				&& m_VOs[m_nPresentRecord].getBodyVO() != null) {
			return m_VOs[m_nPresentRecord].getBodyVO();
		}
		return null;
	}

	/**
	 * 界面关联接口方法实现 -- 维护
	 */
	public void doMaintainAction(ILinkMaintainData maintaindata) {
		SCMEnv.out("进入维护接口...");

		String billID = maintaindata.getBillID();

		initi();

		PraybillVO vo = null;
		m_VOs = null;
		try {
			vo = PraybillHelper.queryPrayVoByHid(billID);
		} catch (Exception e) {
			SCMEnv.out(e);
		}
		// 处理返回查询结果

		// 如果当前登录公司不是操作员制单所在公司，则界面无操作按钮，仅提供浏览功能，by chao , xy , 2006-11-07
		String strLoginCorpId = PoPublicUIClass.getLoginPk_corp();
		String strPrayCorpId = vo == null ? null : vo.getHeadVO().getPk_corp();
		boolean bSameCorpFlag = strLoginCorpId.equals(strPrayCorpId);

		// 无数据处理
		if (vo == null) {
			MessageDialog.showHintDlg(this, m_lanResTool.getStrByID("40040101",
					"UPP40040101-000055")/* "请购单查询" */, m_lanResTool.getStrByID(
					"40040101", "UPP40040101-000056")/* "没有符合条件的请购单！" */);
			// 清空数据
			getBillCardPanel().getBillData().clearViewData();
			// 设置按钮逻辑
			if (bSameCorpFlag) {
				setButtonsCard();
			} else {
				setButtonsNull();
			}
			return;
		}

		// 有数据处理
		m_VOs = new PraybillVO[] { vo };
		m_nPresentRecord = 0;

		// 处理表头下拉列表
		Integer nTemp1 = (Integer) m_VOs[0].getHeadVO().getAttributeValue(
				"ipraysource");
		Integer nTemp2 = (Integer) m_VOs[0].getHeadVO().getAttributeValue(
				"ipraytype");
		m_comPraySource.setSelectedIndex(nTemp1.intValue());
		m_comPrayType.setSelectedIndex(nTemp2.intValue());

		// 加载单据到卡片
		m_nPresentRecord = 0;
		setVoToBillCard(m_nPresentRecord, null);

		// 设置按钮逻辑
		setButtonsCard();

		//
		m_nUIState = 0;
		// 刷新按钮状态
		updateButtonsAll();
		//
		m_bAdd = false;
		m_bEdit = false;
		getBillCardPanel().setEnabled(false);

		// 如果不是在本公司制单，则清空所有按钮
		if (bSameCorpFlag) {
			onModify(false);
		} else {
			setButtonsNull();
		}
	}

	/**
	 * 界面关联接口方法实现 -- 新增
	 */
	public void doAddAction(ILinkAddData adddata) {
		SCMEnv.out("进入新增接口...");

	}

	/**
	 * 界面关联接口方法实现 -- 审批
	 */
	public void doApproveAction(ILinkApproveData approvedata) {
		SCMEnv.out("进入审批接口...");
		if (approvedata == null)
			return;
		String billID = approvedata.getBillID();
		/* 初始化 */
		initi();
		isCanAutoAddLine = false;
		/* 平台需要 */
		setCauditid(billID);
		/* 查询请购单 */
		PraybillVO vo = null;
		try {
			vo = (PraybillVO) getVo();
		} catch (Exception e) {
			SCMEnv.out(e);
			MessageDialog
					.showHintDlg(this, m_lanResTool.getStrByID("SCMCOMMON",
							"UPPSCMCommon-000270")/* "提示" */, m_lanResTool
							.getStrByID("SCMCOMMON", "UPPSCMCommon-000428")/* "系统故障！" */);
		}

		/* 无单据的处理 */
		if (vo == null) {
			m_VOs = null;
			/* 各按钮不可用 */
			for (int i = 0; i < m_btnsAuditCenter.length; i++) {
				m_btnsAuditCenter[i].setEnabled(false);
			}
			for (int i = 0; i < m_btnsAuditCenter.length; i++) {
				updateButton(m_btnsAuditCenter[i]);
			}
			setButtonsNull();
			return;
		}
		/* 有单据的处理 */
		boolean bCorpSameFlag = getCorpPrimaryKey().equals(vo.getPk_corp());
		// 设置按钮组
		if (bCorpSameFlag) {
			setButtons(getBtnTree().getButtonArray());
			setButtonsCard();
		} else {
			setButtons(m_btnsAuditCenter);
			/* 各按钮可用 */
			for (int i = 0; i < m_btnsAuditCenter.length; i++) {
				m_btnsAuditCenter[i].setEnabled(true);
				if (m_btnsAuditCenter[i].getChildCount() > 0) {
					for (int j = 0; j < m_btnsAuditCenter[i].getChildCount(); j++) {
						m_btnsAuditCenter[i].getChildButtonGroup()[j]
								.setEnabled(true);
						updateButton(m_btnsAuditCenter[i].getChildButtonGroup()[j]);
					}
				}
			}
			for (int i = 0; i < m_btnsAuditCenter.length; i++) {
				updateButton(m_btnsAuditCenter[i]);
			}
		}
		/* 设置请购单VO数组 */
		m_VOs = (new PraybillVO[] { vo });
		m_nPresentRecord = 0;
		/* 设置卡片界面数据 */
		setVoToBillPanel();
		//
		setEdit(false);
	}

	/**
	 * 界面关联接口方法实现 -- 逐级联查
	 * 
	 * @modified by czp since v51, 增加数据权限
	 */
	public void doQueryAction(ILinkQueryData querydata) {
		SCMEnv.out("进入逐级联查接口...");

		String billID = querydata.getBillID();

		initi();

		PraybillVO vo = null;

		try {
			// 先按照单据PK查询单据所属的公司corpvalue
			vo = PraybillHelper.queryPrayVoByHid(billID);
			if (vo == null) {
				MessageDialog
						.showHintDlg(this, m_lanResTool.getStrByID("SCMCOMMON",
								"UPPSCMCommon-000270")/* "提示" */, m_lanResTool
								.getStrByID("common", "SCMCOMMON000000161")/* "没有察看单据的权限" */);
				return;
			}
			//
			String strPkCorp = vo.getPk_corp();
			// 按照单据所属公司加载查询模版
			PrayUIQueryDlg queryDlg = new PrayUIQueryDlg(this, strPkCorp);// 查询模板中没有公司时，要设置虚拟公司
			queryDlg.setDefaultValue("po_praybill.dpraydate",
					"po_praybill.dpraydate", "");
			queryDlg.initCorpRefs();
			// 调用公共方法获取该公司中控制权限的档案条件VO数组
			QueryConditionVO[] condVOs = queryDlg.getConditionDatas();
			PuTool.switchReturnType(PrayPubVO._Hash_PrayUI,
					PrayPubVO._Hash_PrayUI_Code_Flag, condVOs, true);
			ConditionVO[] voaCond = queryDlg.getDataPowerConVOs(strPkCorp,
					PrayUIQueryDlg.REFKEYS);
			PuTool.switchReturnType(PrayPubVO._Hash_PrayUI,
					PrayPubVO._Hash_PrayUI_Code_Flag, condVOs, false);
			// 组织第二次查询单据，按照权限和单据PK过滤
			PraybillVO[] voaRet = PraybillHelper.queryAll(null, voaCond, null,
					null, "po_praybill.cpraybillid = '" + billID + "' ");
			if (voaRet == null || voaRet.length <= 0 || voaRet[0] == null) {
				MessageDialog
						.showHintDlg(this, m_lanResTool.getStrByID("SCMCOMMON",
								"UPPSCMCommon-000270")/* "提示" */, m_lanResTool
								.getStrByID("common", "SCMCOMMON000000161")/* "没有察看单据的权限" */);
				setButtonsNull();
				return;
			}
			m_VOs = new PraybillVO[] { vo };
			m_nPresentRecord = 0;
			setVoToBillCard(m_nPresentRecord, "");
			Logger.debug("成功显示单据");
		} catch (Exception e) {
			SCMEnv.out(e);
			MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000270")/* "提示" */, e
					.getMessage());
			setButtonsNull();
			return;
		}
		boolean bCorpSameFlag = getCorpPrimaryKey().equals(vo.getPk_corp());
		// 设置按钮组
		if (bCorpSameFlag) {
			setButtons(getBtnTree().getButtonArray());
			setButtonsCard();
		} else {
			setButtonsNull();
		}
	}

	/**
	 * 清空当前界面按钮
	 */
	private void setButtonsNull() {
		ButtonObject[] objs = getButtons();
		int iLen = objs == null ? 0 : objs.length;
		for (int i = 0; i < iLen; i++) {
			if (objs[i] == null) {
				continue;
			}
			objs[i].setVisible(false);
			updateButton(objs[i]);
		}
	}

	/**
	 * 合并显示、打印功能
	 * 
	 * @since v50
	 */
	private void onCombin() {
		CollectSettingDlg dlg = new CollectSettingDlg(this, m_lanResTool
				.getStrByID("4004020201", "UPT4004020201-000084")/*
																 * @res "合并显示"
																 */);
		dlg.initData(getBillCardPanel(),
		// 固定分组列
				new String[] { "cinventorycode", "cinventoryname",
						"cinventoryspec", "cinventorytype", "cprodarea" },
				// 缺省分组列
				null,
				// 求和列
				new String[] { "nmoney", "npraynum" },
				// 求平均列
				null,
				// 求加权平均列
				new String[] { "nsuggestprice" },
				// 数量列
				"npraynum");
		dlg.showModal();
		showHintMessage(m_lanResTool
				.getStrByID("common", "4004COMMON000000039")/* @res "合并显示完成" */);
	}

	/**
	 * 排序后事件,主要是为了处理非BillModel内容的排序，如自定义的绘制器控件
	 * 
	 * @author czp
	 * @since v50
	 */
	public void afterSort(String key) {
		// 输入提示
		if (getBillCardPanel().getBillData().getEnabled()) {
			PraybillVO voCurr = (PraybillVO) getBillCardPanel().getBillValueVO(
					PraybillVO.class.getName(),
					PraybillHeaderVO.class.getName(),
					PraybillItemVO.class.getName());
			InvAttrCellRenderer ficr = new InvAttrCellRenderer();
			ficr.setFreeItemRenderer(getBillCardPanel(), voCurr);
		}
	}

	/**
	 * 读卡线程
	 */
	Thread readcard = null;
	/**
	 * 线程关闭标示
	 */
	volatile boolean isclose = false;
	/**
	 * 句柄标示
	 */
	int icdev = -1;

	/**
	 * 读卡线程
	 * 
	 * @param bo
	 */
	private void readCard() {
		Runnable run = new Runnable() {
			public void run() {
				try {
					Object obj = null;
					String cardnumber = null;
					TestConn conn = new TestConn();
					if (icdev < 0) {
						for (int i = 0; i < 4; i++) {
							icdev = TestConn.TestConnect(i, 9600);
							if (icdev > 0)
								break;
						}
						System.out.println("handle===" + icdev);
						if (icdev > 0) {
							conn.testBeep(icdev, 1000);
						}
					}
					isclose = false;
					while (true) {
						if (isclose) {
							conn.exit(icdev);
							icdev = -1;
							break;
						}
						Thread.sleep(500);
						cardnumber = String.valueOf(conn.card(icdev, 1));
						if ("1".equals(cardnumber) || "-132".equals(cardnumber)) {
							continue;
						} else {
							conn.testBeep(icdev, 500);
						}
						try {
							obj = getUAPQuery().executeQuery(
									"select pk_psndoc,psncode  from bd_psndoc a where nvl(a.dr,0)=0 and a.pk_corp = '"+getCorpPrimaryKey()+"' and a.def1='"
											+ cardnumber + "'",
									new MapListProcessor());
						} catch (FTSBusinessException e) {
							e.printStackTrace();
						} catch (BusinessException e) {
							e.printStackTrace();
						}
						if (obj == null) {
							continue;
						}
						String psndoc = null;
						String psncode = "";
						if (obj != null && obj instanceof ArrayList) {
							ArrayList<HashMap<String, Object>> mapList = (ArrayList<HashMap<String, Object>>) obj;
							int size = mapList.size();
							for (int i = 0; i < size; i++) {
								Map map = mapList.get(i);
								psndoc = (String) map.get("pk_psndoc");
								psncode = psncode + " " + map.get("psncode");
								if (i == size - 1 && size > 1) {
									MessageDialog.showErrorDlg(PrayUI.this,
											"错误", "您所刷的IC卡绑定了多个人员，人员编码："
													+ psncode);
									psndoc = null;
									continue;
								}
							}
						}
						if (psndoc == null || "".equals(psndoc)) {
							continue;
						}
						int flag = 0;
						try {
							if (m_btnSave.isEnabled()) {
								getBillCardPanel().setHeadItem("cpraypsn", psndoc);
								BillEditEvent event = new BillEditEvent(
										getBillCardPanel().getHeadItem("cpraypsn"),
										obj, "cpraypsn");
								afterEdit(event);
								onButtonClicked(m_btnSave);
								flag = 1;
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						// update by zip:2013/11/12
						// 取消刷卡保存后的自动审核功能
						/*
						if (flag == 1 && !m_btnSave.isEnabled()
								&& m_btnAudit.isEnabled()) {
							onButtonClicked(m_btnAudit);
						} else if (m_btnAudit.isEnabled()
								|| m_btnDiscard.isEnabled()) {
							obj = MessageDialog.showSelectDlg(PrayUI.this,
									"提示", "请选择操作类型",
									new String[] { "审核", "删除" }, 2);
							if ("审核".equals(obj.toString())) {
								onButtonClicked(m_btnAudit);
							} else if ("删除".equals(obj.toString())) {
								onButtonClicked(m_btnDiscard);
							}
						}
						*/
						// update end
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		};
		readcard = new Thread(run);
		readcard.setDaemon(true);
		readcard.start();
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

}