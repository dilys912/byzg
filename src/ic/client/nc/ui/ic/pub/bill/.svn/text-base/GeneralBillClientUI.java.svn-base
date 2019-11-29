package nc.ui.ic.pub.bill;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JFileChooser;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.exception.ComponentException;
import nc.bs.logging.Logger;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.pfxx.IPFxxEJBService;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.ui.bd.CorpBO_Client;
import nc.ui.bd.languagetransformations.Transformations;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.ic.ic001.BatchCodeDefSetTool;
import nc.ui.ic.ic001.BatchcodeHelper;
import nc.ui.ic.ic219.DataBuffer;
import nc.ui.ic.pub.BillFormulaContainer;
import nc.ui.ic.pub.ICCommonBusi;
import nc.ui.ic.pub.InvOnHandDialog;
import nc.ui.ic.pub.LongTimeTask;
import nc.ui.ic.pub.PageCtrlBtn;
import nc.ui.ic.pub.QueryInfo;
import nc.ui.ic.pub.QueryOnHandInfoPanel;
import nc.ui.ic.pub.barcodeoffline.ExcelReadCtrl;
import nc.ui.ic.pub.barcodeparse.BarcodeparseCtrlUI;
import nc.ui.ic.pub.bc.BarCodeDlg;
import nc.ui.ic.pub.bill.cell.FreeItemCellRender;
import nc.ui.ic.pub.bill.cell.LotItemRefCellRender;
import nc.ui.ic.pub.bill.initref.RefFilter;
import nc.ui.ic.pub.bill.query.QueryConditionDlgForBill;
import nc.ui.ic.pub.device.DevInputCtrl;
import nc.ui.ic.pub.locatorref.LocatorRefPane;
import nc.ui.ic.pub.pf.QryInICBillDlg;
import nc.ui.ic.pub.print.PrintDataInterface;
import nc.ui.ic.pub.progressbar.PrgBar;
import nc.ui.ic.pub.report.ICReportHelper;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIFileChooser;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIMenuItem;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardBeforeEditListener;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListData;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillModelCellEditableController;
import nc.ui.pub.bill.BillSortListener2;
import nc.ui.pub.bill.IBillItem;
import nc.ui.pub.change.PfChangeBO_Client;
import nc.ui.pub.linkoperate.ILinkAdd;
import nc.ui.pub.linkoperate.ILinkAddData;
import nc.ui.pub.linkoperate.ILinkApprove;
import nc.ui.pub.linkoperate.ILinkApproveData;
import nc.ui.pub.linkoperate.ILinkMaintain;
import nc.ui.pub.linkoperate.ILinkMaintainData;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.scm.extend.IFuncExtend;
import nc.ui.scm.file.DocumentManager;
import nc.ui.scm.ic.exp.GeneralMethod;
import nc.ui.scm.ic.exp.SetColor;
import nc.ui.scm.ic.freeitem.FreeItemRefPane;
import nc.ui.scm.ic.measurerate.InvMeasRate;
import nc.ui.scm.ic.setpart.SetPartDlg;
import nc.ui.scm.print.IFreshTsListener;
import nc.ui.scm.pub.AccreditLoginDialog;
import nc.ui.scm.pub.CollectSettingDlg;
import nc.ui.scm.pub.bill.ButtonTree;
import nc.ui.scm.pub.bill.IBillExtendFun;
import nc.ui.scm.pub.def.DefSetTool;
import nc.ui.trade.business.HYPubBO_Client;
import nc.vo.baoyin.alert.StordocVO;
import nc.vo.bd.CorpVO;
import nc.vo.bd.b27.CargdocVO;
import nc.vo.bd.def.DefVO;
import nc.vo.bill.pub.BillUtil;
import nc.vo.fts.pub.exception.FTSBusinessException;
import nc.vo.ic.ic001.BatchcodeVO;
import nc.vo.ic.ic700.ICDataSet;
import nc.vo.ic.pub.BillTypeConst;
import nc.vo.ic.pub.GenMethod;
import nc.vo.ic.pub.ICConst;
import nc.vo.ic.pub.ScaleKey;
import nc.vo.ic.pub.ScaleValue;
import nc.vo.ic.pub.SmartVOUtilExt;
import nc.vo.ic.pub.barcodeoffline.ExcelFileVO;
import nc.vo.ic.pub.barcodeparse.BarCodeGroupHeadVO;
import nc.vo.ic.pub.barcodeparse.BarCodeGroupVO;
import nc.vo.ic.pub.barcodeparse.BarCodeParseVO;
import nc.vo.ic.pub.barcodeparse.BarcodeparseCtrl;
import nc.vo.ic.pub.bc.BarCodeVO;
import nc.vo.ic.pub.bill.BillStatus;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.bill.IItemKey;
import nc.vo.ic.pub.bill.QryConditionVO;
import nc.vo.ic.pub.bill.QryInfoConst;
import nc.vo.ic.pub.check.VOCheck;
import nc.vo.ic.pub.check.VoCombine;
import nc.vo.ic.pub.exp.RightcheckException;
import nc.vo.ic.pub.lang.ResBase;
import nc.vo.ic.pub.locator.LocatorVO;
import nc.vo.ic.pub.lot.LotNumbRefVO;
import nc.vo.ic.pub.smallbill.SMGeneralBillVO;
import nc.vo.ic.pub.sn.SerialVO;
import nc.vo.pfxx.util.FileUtils;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.NullFieldException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.ValidationException;
import nc.vo.pub.formulaset.util.StringUtil;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.lang.UFTime;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.constant.ScmConst;
import nc.vo.scm.constant.ic.BillMode;
import nc.vo.scm.constant.ic.CONST;
import nc.vo.scm.constant.ic.InOutFlag;
import nc.vo.scm.ic.bill.FreeVO;
import nc.vo.scm.ic.bill.InvVO;
import nc.vo.scm.ic.bill.WhVO;
import nc.vo.scm.ic.exp.ATPNotEnoughException;
import nc.vo.scm.ic.exp.HeaderRenderer;
import nc.vo.scm.ic.exp.ICDateException;
import nc.vo.scm.ic.exp.ICHeaderNullFieldException;
import nc.vo.scm.ic.exp.ICLocatorException;
import nc.vo.scm.ic.exp.ICNullFieldException;
import nc.vo.scm.ic.exp.ICNumException;
import nc.vo.scm.ic.exp.ICPriceException;
import nc.vo.scm.ic.exp.ICRepeatException;
import nc.vo.scm.ic.exp.ICSNException;
import nc.vo.scm.print.PrintConst;
import nc.vo.scm.print.ScmPrintlogVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.smart.ObjectUtils;
import nc.vo.scm.pub.smart.SmartFieldMeta;
import nc.vo.so.so001.SaleorderBVO;
import nc.vo.so.so120.CreditNotEnoughException;
import nc.vo.so.so120.PeriodNotEnoughException;
import nc.vo.to.pub.BillVO;

import org.w3c.dom.Document;

public abstract class GeneralBillClientUI extends ToftPanel implements javax.swing.event.TableModelListener, nc.ui.pub.bill.BillEditListener, nc.ui.pub.bill.BillEditListener2, nc.ui.pub.bill.BillTableMouseListener, nc.ui.pub.bill.BillBodyMenuListener, nc.ui.pub.bill.BillTotalListener, nc.ui.ic.pub.barcode.BarCodeInputListenerNew, BillModelCellEditableController, BillCardBeforeEditListener, IBillExtendFun, ILinkAdd, ILinkMaintain, ILinkApprove, ILinkQuery, BillSortListener2, ActionListener {
	//单据导入定义--start--
	public static Workbook w   = null;
    public static int rows=0;
    public static SaleorderBVO[] wbvo = null;
    public static String pk_corp="";
    //单据导入定义--end--
    
	protected String m_sBnoutnumnull = null;

	// 指定单一货位
	// protected ButtonObject m_boSelectLocator;

	public static final int CANNOTSIGN = -1; // 不能签字

	public static final int NOTSIGNED = 0; // 未签字//单据签字状态

	public static final int SIGNED = 1; // 已签字

	protected String m_sBillTypeCode = nc.vo.ic.pub.BillTypeConst.m_purchaseIn;

	// 单据类型编码
	protected String m_sTitle = null; // 标题

	// protected final String m_sTempletID = "System987679310405"; // 模板id

	protected final int m_iInitRowCount = 1; // 新增初始状态下的行数

	protected String m_sCurBillOID = null; // 当前的单据id.

	protected int m_iMode = BillMode.Browse; // 当前的单据编辑状态.

	protected boolean m_bOnhandShowHidden = false; // 是否显示存量参照

	public String m_sMultiMode = MultiCardMode.CARD_PURE;// 多卡片状态

	protected ToftLayoutManager m_layoutManager = new ToftLayoutManager(this);// 布局管理器

	protected AfterEditCtrl m_afterEditCtrl = new AfterEditCtrl(this);

	protected int m_iCurPanel = BillMode.Card; // 当前显示的panel.

	// 列表形式处理
	protected int m_iBillQty = 0; // 当前列表形式下的单据数量

	protected ArrayList m_alListData = null; // 列表数据

	protected int m_iLastSelListHeadRow = -1; // 最后选中的列表表头行。

	protected int m_iCurDispBillNum = -1;

	// 表单形式下当前显示的单据序号，当列表---〉表单切换时，未改选其它单据，则无需重设表单数据，以提高效率。
	// vo名，用于读取修改数据时

	// 表单数据VO
	protected GeneralBillVO m_voBill = null;

	protected String m_sNumItemKey = "ninnum"; // 表体实入数量字段名

	protected String m_sAstItemKey = "ninassistnum"; // 表体辅数量字段名

	protected String m_sNgrossnum = "ningrossnum";// 毛重

	// 表体应辅数量字段名
	protected String m_sShouldAstItemKey = "nneedinassistnum";

	// 表体应入数量字段名
	protected String m_sShouldNumItemKey = "nshouldinnum";

	// 保存当前新增或修改的单据的货位分配数据...要不要保存列表形式下所有的数据？
	protected ArrayList m_alLocatorData = null;

	protected ArrayList m_alLocatorDataBackup = null;

	// 数据备份，在新增时保存 m_alLocatorData，取消时恢复m_alLocatorData.
	// 保存当前新增或修改的单据的序列号分配数据...要不要保存列表形式下所有的数据？
	protected ArrayList m_alSerialData = null;

	protected ArrayList m_alSerialDataBackup = null;

	// 序列号对话框
	protected nc.ui.ic.pub.sn.SerialAllocationDlg m_dlgSerialAllocation = null;

	// 货位对话框
	protected nc.ui.ic.pub.locator.SpaceAllocationDlg m_dlgSpaceAllocation = null;

	// 卡片
	protected nc.ui.pub.bill.BillCardPanel ivjBillCardPanel = null;

	// 列表
	protected nc.ui.pub.bill.BillListPanel ivjBillListPanel = null;

	// 自由项参照
	protected FreeItemRefPane ivjFreeItemRefPane = null;

	// 批次参照
	protected nc.ui.ic.pub.lot.LotNumbRefPane ivjLotNumbRefPane = null;

	// 车次参照
	protected nc.ui.ic.pub.tools.VehicleRefPanel ivjVehicleRefPane = null;

	protected int m_iFirstSelectRow = -1;

	protected int m_iFirstSelectCol = -1;

	protected String m_sCorpID = null; // 公司ID

	protected String m_sUserID = null; // 当前使用者ID

	protected String m_sLogDate = null; // 当前登录日期

	protected String m_sUserName = null; // 当前使用者名称

	// 行复制VO
	protected GeneralBillItemVO[] m_voaBillItem = null;

	// 状态条
	protected javax.swing.JTextField m_tfHintBar = null;

	// 对应入库单的参照//对应单据参照
	protected nc.ui.ic.pub.corbillref.ICCorBillRefPane m_aICCorBillRef = null;

	protected InvOnHandDialog m_iohdDlg = null;

	// 辅计量及换算率。
	protected InvMeasRate m_voInvMeas = new InvMeasRate();

	boolean m_isWhInvRef = false;

	// 货位参照
	private LocatorRefPane ivjLocatorRefPane = null;

	// 查询对话框
	protected QueryConditionDlgForBill ivjQueryConditionDlg = null;

	// 操作员对应的公司PK,初始化时读入
	protected ArrayList m_alUserCorpID = null;

	// added by zhx 单据中是否使用公式的标志;
	protected boolean m_bIsByFormula = true;

	// 单据号是否允许手输
	protected boolean m_bIsEditBillCode = false;

	// 是否期初单据,缺省不是。
	protected boolean m_bIsInitBill = false;

	// 是否需要单据参照录入菜单。
	protected boolean m_bNeedBillRef = true;

	// 是否用系统toftpanel缺省的错误显示对话框
	protected boolean m_bUserDefaultErrDlg = true;

	// 初始化打印接口
	protected PrintDataInterface m_dataSource = null;

	// 外设输入控制类。
	protected nc.ui.ic.pub.device.DevInputCtrl m_dictrl = null;

	/** 定位对话框 */
	protected nc.ui.ic.pub.orient.OrientDialog m_dlgOrient = null;

	// 新增时间
	protected nc.vo.pub.lang.UFDateTime m_dTime = null;

	public Hashtable m_htBItemEditFlag = null;

	// add by zhx
	// 初始化时,保存单据模板中定义的表头,表体数据项是否可编辑, 用于业务规则定义的数据项是否可编辑的判断.
	public Hashtable m_htHItemEditFlag = null;

	// 小数精度定义--->
	// 数量小数位 2
	// 辅计量数量小数位 2
	// 换算率 2
	// 存货成本单价小数位 2

	// protected int m_iaScale[] = new int[] { 2, 2, 2, 2, 2 };

	protected ScaleValue m_ScaleValue = new ScaleValue();
	protected ScaleKey m_ScaleKey = new ScaleKey();
	// 单据出入库属性
	protected int m_iBillInOutFlag = InOutFlag.IN;

	// /////////////////////////////////////////
	// 公式解析需要的相关全局变量 by hanwei 2003-06-26

	private InvoInfoBYFormula m_InvoInfoBYFormula;

	// ///////////////////////////////////////
	// 进度条
	public PrgBar m_pbProgressBar = null;

	protected nc.ui.pub.print.PrintEntry m_print = null;

	// 项目参照
	protected nc.ui.pub.beans.UIRefPane m_refJob = null;

	// protected nc.ui.bd.b39.JobRefTreeModel m_refJobModel = null;

	// 项目阶段参照
	protected nc.ui.pub.beans.UIRefPane m_refJobPhase = null;

	protected nc.ui.bd.b39.PhaseRefModel m_refJobPhaseModel = null;

	protected String m_sAstWhItemKey = "cwastewarehouseid";

	// 表头主键字段名
	protected final String m_sBillPkItemKey = "cgeneralhid";

	// 表体主键字段名
	protected final String m_sBillRowItemKey = "cgeneralbid";

	// 业务类型ID
	protected final String m_sBusiTypeItemKey = "cbiztype";

	// 对应单据号
	protected String m_sCorBillCodeItemKey = "ccorrespondcode";

	// 成本对象
	protected final String m_sCostObjectItemKey = "ccostobjectname";

	// 当前节点编码
	public String m_sCurrentBillNode = "40080602";

	// 集团id
	protected String m_sGroupID = null;

	// 存货编码字段名，参照放在这里
	protected final String m_sInvCodeItemKey = "cinventorycode";

	// 存货管理档案字段名
	protected final String m_sInvMngIDItemKey = "cinventoryid";

	// 库存组织
	protected String m_sMainCalbodyItemKey = "pk_calbody";

	protected String m_sMainCalbodywasteItemKey = "pk_calbodywaste";

	// 仓库字段1，（可以是cwarehouseid,cwastewarehouseid）
	protected String m_sMainWhItemKey = "cwarehouseid";

	// 表体计划金额字段名
	protected String m_sPlanMnyItemKey = "nplannedmny";

	// 表体计划单价字段名
	protected String m_sPlanPriceItemKey = "nplannedprice";

	// 是否保留最初的制单人
	// protected String m_sRemainOperator = null;

	// 是否跟踪到入库单标志
	// protected String m_sTrackedBillFlag = null;
	// 是否保存即签字
	// protected String m_sSaveAndSign = null;

	// 模块启用日期
	protected String m_sStartDate = null;

	// 仓库字段2，一般应是废品库
	protected String m_sWasteWhItemKey = "cwastewarehouseid";

	// 废品库
	/* 标志该单据是否为参照录入单据（默认为自制单据）* */
	// boolean bIsRefBill = false;
	/* 保存来源单据参照生成的单据VO* */
	protected GeneralBillVO m_voBillRefInput = null;

	// 最近一次的查询条件，可用于刷新。现用于列表形式下的打印。
	protected QryConditionVO m_voLastQryCond = null;

	// 支持二次开发的功能扩展
	protected nc.ui.scm.extend.IFuncExtend m_funcExtend = null;

	// 当换算率改变时，非固定换算率，默认是辅数量不变主数量变。否则相反。
	protected boolean m_bAstCalMain = false;

	// 如果是换算率触发afterAstNumEdit触发afterNumEdit,那么在afterNumEdit中就不需要再去触发afterAstNumEdit
	protected boolean m_isNeedNumEdit = true;

	// 是否曾经查询过的标识
	protected boolean m_bEverQry = false;

	// 是进行查询还是进行刷新（为了兼容以前的版本，特增加该变量用来标识操作的类型）
	protected boolean m_bQuery = true;

	private String[] m_sItemField = null;// 表体公式

	private ClientUISortCtl m_listHeadSortCtl;// 列表表头排序控制
	private ClientUISortCtl m_listBodySortCtl;// 列表表体排序控制
	private ClientUISortCtl m_cardBodySortCtl;// 卡片表体排序控制

	// 用于从“功能注册”中按照按钮的CLASS_NAME获得某个按钮的实例
	private ButtonTree m_buttonTree;

	/**
	 * ClientUI 构造子注解。
	 */
	public GeneralBillClientUI() {
		super();
	}

	/**
	 * 创建者：王乃军 功能： 参数： 返回： 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	public void addRowNums(int rownums) {
		if (BillMode.New == m_iMode) {
			for (int i = 1; i <= rownums; i++) {
				getBillCardPanel().addLine();
				// zhx added rowno process 030626
				nc.ui.scm.pub.report.BillRowNo.addNewRowNo(getBillCardPanel(), m_sBillTypeCode, m_sBillRowNo);
			}
		}

	}

	protected void afterCDptIDEdit(nc.ui.pub.bill.BillEditEvent e) {
		BillItem itDpt = getBillCardPanel().getHeadItem("cdptid");
		// 部门
		String sName = ((nc.ui.pub.beans.UIRefPane) itDpt.getComponent()).getRefName();
		// 2003-06-12 zhx add 部门编辑过滤后,需要按照部门过滤业务员
		// String sPK = ((nc.ui.pub.beans.UIRefPane) itDpt.getComponent())
		// .getRefPK();
		// BillItem it = getBillCardPanel().getHeadItem("cbizid");
		// v5: ljun : don't need to do things below.
		// if (sPK != null) {
		// if (it != null && it.getValue() == null) {
		// String[] saPK = new String[1];
		// saPK[0] = sPK;
		// RefFilter.filterPsnByDept(it, m_sCorpID, saPK);
		// }
		// } else
		// RefFilter.filterPsnByDept(it, m_sCorpID, null);

		// 保存名称以在列表形式下显示。
		if (m_voBill != null) m_voBill.setHeaderValue("cdptname", sName);

	}

	protected void afterCBizidEdit(nc.ui.pub.bill.BillEditEvent e) {
		// 业务员
		String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("cbizid").getComponent()).getRefName();
		String sPK = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("cbizid").getComponent()).getRefPK();
		// 需要根据业务员自动带出部门
		String sDeptPK = null;
		String sDeptName = null;
		if (sPK != null && sPK.trim().length() > 0) {
			try {
				sDeptPK = execFormular("getColValue(bd_psndoc,pk_deptdoc,pk_psndoc,pk_psndoc)", sPK);
			} catch (Exception ex) {
				nc.vo.scm.pub.SCMEnv.error(ex);
			}
			BillItem itDpt = getBillCardPanel().getHeadItem("cdptid");
			if (itDpt != null) {
				((nc.ui.pub.beans.UIRefPane) itDpt.getComponent()).setPK(sDeptPK);
				// 部门
				sDeptName = ((nc.ui.pub.beans.UIRefPane) itDpt.getComponent()).getRefName();
				// 2003-06-12 zhx add 部门编辑过滤后,需要按照部门过滤业务员
				// v5 : ljun
				// BillItem it = getBillCardPanel().getHeadItem("cbizid");
				//
				// if (sDeptPK != null) {
				//
				// String[] saPK = new String[1];
				// saPK[0] = sDeptPK;
				// RefFilter.filterPsnByDept(it, m_sCorpID, saPK);
				//
				// } else
				// RefFilter.filterPsnByDept(it, m_sCorpID, null);
			}
		}
		// 保存名称以在列表形式下显示。
		if (m_voBill != null) {
			m_voBill.setHeaderValue("cbizname", sName);
			m_voBill.setHeaderValue("cdptname", sDeptName);
		}
	}

	/**
	 * 创建者：王乃军 功能：单据编辑事件处理 参数：e单据编辑事件 返回： 例外： 日期：(2001-5-8 19:08:05)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */

	public void afterEdit(nc.ui.pub.bill.BillEditEvent e) {
		nc.vo.scm.pub.ctrl.GenMsgCtrl.printHint("afterEdit:" + e.getKey());

		// getBillCardPanel().rememberFocusComponent();

		// 行，选中表头字段时为-1
		int row = e.getRow();
		// 字段itemkey
		String sItemKey = e.getKey();
		// 字段，位置 0: head 1:table
		int pos = e.getPos();

		if (pos == nc.ui.pub.bill.BillItem.BODY && row < 0 || sItemKey == null || sItemKey.length() == 0) return;

		// ljun
		// 处理了毛重皮重
		m_afterEditCtrl.afterEdit(e);

		//冻结的数据不要在领用出库、移库、货位调整、发货界面出现 add by shikun //BillMode.New== m_iMode单据新增非编辑态
		if (sItemKey.equals("noutnum")&&getParentCorpCode().equals("10395")&&BillMode.New== m_iMode&&!m_sBillTypeCode.equals("4C")){
			UFDouble num = getWGLnum(row);
			UFDouble noutnum = getBillCardPanel().getBodyValueAt(row, "noutnum")==null? new UFDouble(0.00):new UFDouble(getBillCardPanel().getBodyValueAt(row, "noutnum").toString());
			if (noutnum.doubleValue()!=0&&noutnum.doubleValue()<num.doubleValue()) {//本地数量不为空，并且本地数量小于有效数量，那么本地数量不变；如果本地数量为空或本地数量大于有效数量，那取有效数量
				num = noutnum;
			}
			getBillCardPanel().setBodyValueAt(num, row, "noutnum");
		}
		//end shikun 
		
		if (sItemKey.equals("vbillcode")) {
			// 单据号

			// 保存名称以在列表形式下显示。
			if (m_voBill != null) m_voBill.setHeaderValue("vbillcode", getBillCardPanel().getHeadItem("vbillcode").getValueObject());
		} else if (sItemKey.equals("cdispatcherid")) {
			// 收发类别
			String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("cdispatcherid").getComponent()).getRefName();
			// 保存名称以在列表形式下显示。
			if (m_voBill != null) m_voBill.setHeaderValue("cdispatchername", sName);
		} else if (sItemKey.equals("cinventoryid")) {
			// 加工品
			String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("cinventoryid").getComponent()).getRefName();
			// 保存名称以在列表形式下显示。
			if (m_voBill != null) m_voBill.setHeaderValue("cinventoryname", sName);
		} else if (sItemKey.equals(m_sMainWhItemKey))
		// 仓库
		afterWhEdit(e);
		else if (sItemKey.equals(m_sMainCalbodyItemKey))
		// 库存组织
		afterCalbodyEdit(e);

		else if (sItemKey.equals("cwhsmanagerid")) {
			// 库管员

			String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("cwhsmanagerid").getComponent()).getRefName();
			// 保存名称以在列表形式下显示。
			if (m_voBill != null) m_voBill.setHeaderValue("cwhsmanagername", sName);
		} else if (sItemKey.equals("cdptid")) {  //部门ID
			afterCDptIDEdit(e);
		} else if (sItemKey.equals("cbizid")) {
			// 业务员
			afterCBizidEdit(e);
		} else if (sItemKey.equals("cproviderid")) {
			// 供应商
			afterProviderEdit(e);
		} else if (sItemKey.equals("ccustomerid")) {   //客户ID
			afterCustomerEdit(e);

		} else if (sItemKey.equals("cbiztype")) {
			// 业务类型

			String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("cbiztype").getComponent()).getRefName();
			String sPK = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("cbiztype").getComponent()).getRefPK();
			// 保存名称以在列表形式下显示。
			if (m_voBill != null) m_voBill.setHeaderValue("cbiztypename", sName);
			// 根据业务类型带出默认的收发类别 updated by cqw after v2.30
			if (sPK != null) {
				String sReceiptID = execFormular("getColValue(bd_busitype,receipttype,pk_busitype,pk_busitype)", sPK);
				if (sReceiptID != null && sReceiptID.trim().length() > 0) {
					BillItem it = getBillCardPanel().getHeadItem("cdispatcherid");
					if (it != null && it.getValueObject() == null) it.setValue(sReceiptID);
				}
			}
		} else if (sItemKey.equals("cdilivertypeid")) {
			// 发运方式

			String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("cdilivertypeid").getComponent()).getRefName();
			// 保存名称以在列表形式下显示。
			if (m_voBill != null) m_voBill.setHeaderValue("cdilivertypename", sName);
		} else if (sItemKey.equals("vdiliveraddress")) {
			// 发运地址

			String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("vdiliveraddress").getComponent()).getUITextField().getText();// getRefName();
			// 保存名称以在列表形式下显示。
			if (m_voBill != null) m_voBill.setHeaderValue("vdiliveraddress", sName);

			afterVdiliveraddress(e);

		}

		else if (sItemKey.equals("cotherwhid")) {
			// DW 2005-05-31 在改变其它仓库时维护其它库存组织其它公司
			try {
				String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("cotherwhid").getComponent()).getRefName();
				String sCode = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("cotherwhid").getComponent()).getRefPK();
				// 调拨单特殊处理 不清公司和库存组织
				if (m_sBillTypeCode != "4E" && m_sBillTypeCode != "4Y") {
					m_voBill.setHeaderValue("cothercalbodyid", null);
					m_voBill.setHeaderValue("cothercorpid", null);
					if (sCode != null) m_voBill.setHeaderValue("cothercorpid", m_sCorpID);
				}

				if (m_voBill != null && sCode != null) {
					m_voBill.setHeaderValue("cotherwhname", sName);
					WhVO voWh = getInvoInfoBYFormula().getWHVO(sCode, false);

					if (voWh.getPk_calbody() != null) m_voBill.setHeaderValue("cothercalbodyid", voWh.getPk_calbody());
				}
			} catch (Exception e2) {
				nc.vo.scm.pub.SCMEnv.error(e2);
			}
		}
		// 存货编码改变
		else if (sItemKey.equals("cinventorycode")) {
			afterInvMutiEdit(e);
		} else if (sItemKey.equals("vfree0")) afterFreeItemEdit(e);
		else if (sItemKey.equals("castunitname")) afterAstUOMEdit(e);
		else if (sItemKey.equals(m_sNumItemKey)) {
			afterNumEdit(e);

		} else if (sItemKey.equals(m_sAstItemKey)) {
			afterAstNumEdit(e);
		} else if (sItemKey.equals(m_sShouldNumItemKey)) afterShouldNumEdit(e);
		else if (sItemKey.equals(m_sShouldAstItemKey)) afterShouldAstNumEdit(e);
		else if (sItemKey.equals("ncountnum")) {
			m_voBill.setItemValue(row, "ncountnum", getBillCardPanel().getBodyValueAt(row, "ncountnum"));

		} else // 对应单据改变
		if (sItemKey.equals(m_sCorBillCodeItemKey)) afterCorBillEdit(e);
		else if (sItemKey.equals("vspacename")) afterSpaceEdit(e);

		// 生产日期
		else if (sItemKey.equals("scrq")) {
			nc.vo.scm.ic.bill.InvVO voInv = m_voBill.getItemInv(row);
			if (voInv != null && voInv.getIsValidateMgt() != null && voInv.getIsValidateMgt().intValue() == 1) {
				nc.vo.pub.lang.UFDate dScrq = null;
				if (e.getValue() != null && nc.vo.pub.lang.UFDate.isAllowDate(e.getValue().toString())) {
					dScrq = new nc.vo.pub.lang.UFDate(e.getValue().toString());
					getBillCardPanel().setBodyValueAt(dScrq.getDateAfter(voInv.getQualityDay().intValue()).toString(), row, "dvalidate");
				}
			}
		}
		// 失效日期
		else if (sItemKey.equals("dvalidate")) {
			nc.vo.scm.ic.bill.InvVO voInv = m_voBill.getItemInv(row);
			if (voInv != null && voInv.getIsValidateMgt() != null && voInv.getIsValidateMgt().intValue() == 1) {
				nc.vo.pub.lang.UFDate dvalidate = null;
				if (e.getValue() != null && nc.vo.pub.lang.UFDate.isAllowDate(e.getValue().toString())) {
					dvalidate = new nc.vo.pub.lang.UFDate(e.getValue().toString());
					getBillCardPanel().setBodyValueAt(dvalidate.getDateBefore(voInv.getQualityDay().intValue()).toString(), row, "scrq");
				}
			}
		} else if (sItemKey.equals("nmny")) {

			if (e.getValue() != null && e.getValue().toString().trim().length() > 0) {
				UFDouble nmny = new UFDouble(e.getValue().toString());
				Object ninnum = getBillCardPanel().getBodyValueAt(row, m_sNumItemKey);
				if (ninnum != null && ninnum.toString().trim().length() > 0) {
					ninnum = new UFDouble(ninnum.toString().trim());
					if (((UFDouble) ninnum).doubleValue() != 0.0) {
						UFDouble nprice = new UFDouble(nmny.doubleValue() / ((UFDouble) ninnum).doubleValue());
						getBillCardPanel().setBodyValueAt(nprice, row, "nprice");
					}

				}

			}
		} else if (sItemKey.equals("vbatchcode")) {
			afterLotEdit(e);
		}
		// 项目
		else if (sItemKey.equals("cprojectname")) {

			String sName = m_refJob.getRefName(); // uiref.getRefName();
			String sPK = m_refJob.getRefPK(); // uiref.getRefPK();
			getBillCardPanel().setBodyValueAt(null, e.getRow(), "cprojectphasename");
			getBillCardPanel().setBodyValueAt(null, e.getRow(), "cprojectphaseid");
			getBillCardPanel().setBodyValueAt(sName, e.getRow(), "cprojectname");
			getBillCardPanel().setBodyValueAt(sPK, e.getRow(), "cprojectid");
			// 保存名称以在列表形式下显示。
			if (m_voBill != null) {
				m_voBill.setItemValue(e.getRow(), "cprojectname", sName);
				m_voBill.setItemValue(e.getRow(), "cprojectid", sPK);

				m_voBill.setItemValue(e.getRow(), "cprojectphasename", null);
				m_voBill.setItemValue(e.getRow(), "cprojectphaseid", null);

			}
		}
		// 项目阶段
		else if (sItemKey.equals("cprojectphasename")) {
			// nc.ui.pub.beans.UIRefPane uiref =
			// (nc.ui.pub.beans.UIRefPane) getBillCardPanel()
			// .getBodyItem("cprojectname")
			// .getComponent();

			String sName = m_refJobPhase.getRefName(); // uiref.getRefName();
			String sPK = m_refJobPhase.getRefPK(); // uiref.getRefPK();
			// 保存名称以在列表形式下显示。
			if (m_voBill != null) {
				m_voBill.setItemValue(e.getRow(), "cprojectphasename", sName);
				m_voBill.setItemValue(e.getRow(), "cprojectphaseid", sPK);
				getBillCardPanel().setBodyValueAt(sName, e.getRow(), "cprojectphasename");
				getBillCardPanel().setBodyValueAt(sPK, e.getRow(), "cprojectphaseid");

			}
		} // 表体供应商
		else if (sItemKey.equals("vvendorname")) {

			String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getBodyItem("vvendorname").getComponent()).getRefName();
			String sPK = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getBodyItem("vvendorname").getComponent()).getRefPK();
			String sPk_cubasdoc = getPk_cubasdoc(sPK);

			getBillCardPanel().setBodyValueAt(sName, e.getRow(), "vvendorname");
			getBillCardPanel().setBodyValueAt(sPK, e.getRow(), "cvendorid");
			getBillCardPanel().setBodyValueAt(sPk_cubasdoc, e.getRow(), "pk_cubasdoc");

			// 保存名称以在列表形式下显示。
			if (m_voBill != null) {
				m_voBill.setItemValue(e.getRow(), "vvendorname", sName);
				m_voBill.setItemValue(e.getRow(), "cvendorid", sPK);
				m_voBill.setItemValue(e.getRow(), "pk_cubasdoc", sPk_cubasdoc);
			}
			// //zhy2005-08-24将afterProviderEdit中的移到此处
			// //如果是表体的供应商改变，而表体是出库属性，那么也要清掉序列号。
			// if(m_voBill.getItemVOs()[row].getInOutFlag() == InOutFlag.OUT)
			// clearLocSnData(row,"headprovider");//zhy此处的别名无所谓，改为bodyvendor也行，不过要修改clearLocSnData中的相应判断

		} else // 成本对象
		if (sItemKey.equals("ccostobjectname")) {
			String costobjectname = null;
			String costobjectid = null;
			UIRefPane ref = (UIRefPane) getBillCardPanel().getBodyItem("ccostobjectname").getComponent();
			if (ref != null) {
				costobjectname = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getBodyItem("ccostobjectname").getComponent()).getRefName();
				costobjectid = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getBodyItem("ccostobjectname").getComponent()).getRefPK();
				ref.setPK(null);
			}

			// 保存名称以在列表形式下显示。
			if (m_voBill != null) {
				m_voBill.setItemValue(e.getRow(), "ccostobjectname", costobjectname);
				m_voBill.setItemValue(e.getRow(), "ccostobject", costobjectid);
				getBillCardPanel().setBodyValueAt(costobjectname, e.getRow(), "ccostobjectname");
				getBillCardPanel().setBodyValueAt(costobjectid, e.getRow(), "ccostobject");

			}
			// ((UIRefPane)getBillCardPanel().getBodyItem("ccostobjectname").getComponent()).setPK(null);
		} else // 收货单位
		if (sItemKey.equals("creceieveid")) {
			String vrevcustname = null;
			String creceieveid = null;
			if (getBillCardPanel().getBodyItem("vrevcustname").getComponent() != null) {

				vrevcustname = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getBodyItem("vrevcustname").getComponent()).getRefName();
				creceieveid = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getBodyItem("vrevcustname").getComponent()).getRefPK();
			}

			// 保存名称以在列表形式下显示。
			if (m_voBill != null) {
				m_voBill.setItemValue(e.getRow(), "vrevcustname", vrevcustname);
				m_voBill.setItemValue(e.getRow(), "creceieveid", creceieveid);
				getBillCardPanel().setBodyValueAt(vrevcustname, e.getRow(), "vrevcustname");
				getBillCardPanel().setBodyValueAt(creceieveid, e.getRow(), "creceieveid");

			}
		}

		/** 换算率编辑后 */
		else if (sItemKey.equals("hsl")) afterHslEdit(e);

		// 备注
		else if (sItemKey.equals("vnotebody")) m_voBill.setItemValue(e.getRow(), "vnotebody", e.getValue());
		// 赠品
		else if (sItemKey.equals("flargess")) {
			UFBoolean ufFlargess = new UFBoolean(e.getValue().toString());
			m_voBill.setItemValue(e.getRow(), "flargess", ufFlargess);
			// 如果是赠品清除单据上的单价和金额 add by hanwei 2004-6-24
			if (ufFlargess.booleanValue()) {
				if (getBillCardPanel().getBodyItem("nmny") != null) {
					getBillCardPanel().setBodyValueAt(null, e.getRow(), "nmny");
				}
				if (getBillCardPanel().getBodyItem("nprice") != null) {
					getBillCardPanel().setBodyValueAt(null, e.getRow(), "nprice");
				}
			}
		}
		// 在途
		else if (sItemKey.equals("bonroadflag")) {
			afterOnRoadEdit(e);
		} else if (e.getKey().equals(m_sNumItemKey) || e.getKey().equals(m_sAstItemKey) || e.getKey().equals("hsl") || e.getKey().equals("castunitname")) {
			resetSpace(row);
		} else if (e.getKey().equals(m_sBillRowNo)) {// zhx added for bill
			// row
			// no, after edit process.
			nc.ui.scm.pub.report.BillRowNo.afterEditWhenRowNo(getBillCardPanel(), e, m_sBillTypeCode);
			// 同步化VO
			m_voBill.setItemValue(row, m_sBillRowNo, getBillCardPanel().getBodyValueAt(row, m_sBillRowNo));

		} else if (sItemKey.startsWith("vuserdef")) {// 自定义项处理zhy
			if (pos == 0) {// 表头
				String sVdefPkKey = "pk_defdoc" + sItemKey.substring("vuserdef".length());

				// 单据表头使用：afterEditHead(BillData bdata, String sVdefValueKey,
				// String sVdefPkKey)
				DefSetTool.afterEditHead(getBillCardPanel().getBillData(), sItemKey, sVdefPkKey);

				// 同步m_voBill
				m_voBill.setHeaderValue(sItemKey, getBillCardPanel().getHeadItem(sItemKey).getValueObject());
				m_voBill.setHeaderValue(sVdefPkKey, getBillCardPanel().getHeadItem(sVdefPkKey).getValueObject());
			} else if (pos == 1) {// 表体
				String sVdefPkKey = "pk_defdoc" + sItemKey.substring("vuserdef".length());

				// 单据表体使用：afterEditBody(BillModel billModel, int iRow,String
				// sVdefValueKey, String sVdefPkKey)
				DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), row, sItemKey, sVdefPkKey);

				// 同步m_voBill
				m_voBill.setItemValue(row, sItemKey, getBillCardPanel().getBodyValueAt(row, sItemKey));
				m_voBill.setItemValue(row, sVdefPkKey, getBillCardPanel().getBodyValueAt(row, sVdefPkKey));
			}
		} else if (sItemKey.startsWith(IItemKey.VBCUSER)) {// 处理批次号档案相关的自定义项
			String sVdefPkKey = IItemKey.PK_DEFDOCBC + sItemKey.substring(IItemKey.VBCUSER.length());

			// 单据表体使用：afterEditBody(BillModel billModel, int iRow,String
			// sVdefValueKey, String sVdefPkKey)
			DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), row, sItemKey, sVdefPkKey);

			// 同步m_voBill
			m_voBill.setItemValue(row, sItemKey, getBillCardPanel().getBodyValueAt(row, sItemKey));
			m_voBill.setItemValue(row, sVdefPkKey, getBillCardPanel().getBodyValueAt(row, sVdefPkKey));
		} else if (sItemKey.equals(IItemKey.CQUALITYLEVELNAME)) {
			String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getBodyItem(IItemKey.CQUALITYLEVELNAME).getComponent()).getRefName();
			String sPK = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getBodyItem(IItemKey.CQUALITYLEVELNAME).getComponent()).getRefPK();
			getBillCardPanel().setBodyValueAt(sName, e.getRow(), IItemKey.CQUALITYLEVELNAME);
			getBillCardPanel().setBodyValueAt(sPK, e.getRow(), IItemKey.CQUALITYLEVELID);
			// 保存名称以在列表形式下显示。
			if (m_voBill != null) {
				m_voBill.setItemValue(e.getRow(), IItemKey.CQUALITYLEVELNAME, sName);
				m_voBill.setItemValue(e.getRow(), IItemKey.CQUALITYLEVELID, sPK);
			}
		} else { // default set id col name:缺省的设置，编辑了参照置id列的值
			String sIdColName = null;
			nc.ui.pub.beans.UIRefPane ref = null;
			// 在表体
			if (getBillCardPanel().getBodyItem(sItemKey) != null) {

				sIdColName = getBillCardPanel().getBodyItem(sItemKey).getIDColName();
				if (sIdColName != null && getBillCardPanel().getBodyItem(sIdColName) != null && (getBillCardPanel().getBodyItem(sItemKey).getComponent()) != null) {
					ref = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getBodyItem(sItemKey).getComponent());
					// 置pk
					getBillCardPanel().setBodyValueAt(ref.getRefPK(), row, sIdColName);
					// 显示name
					getBillCardPanel().setBodyValueAt(ref.getRefName(), row, sItemKey);
					// 同步m_voBill
					m_voBill.setItemValue(row, sIdColName, ref.getRefPK());
				} else if (getBillCardPanel().getBodyItem(sItemKey) != null) {
					Object ovalue = getBillCardPanel().getBodyValueAt(row, sItemKey);
					m_voBill.setItemValue(row, sItemKey, ovalue);
				}
			} else if (getBillCardPanel().getHeadItem(sItemKey) != null) {
				// 在表头
				sIdColName = getBillCardPanel().getHeadItem(sItemKey).getIDColName();
				if (sIdColName != null && getBillCardPanel().getHeadItem(sIdColName) != null && getBillCardPanel().getHeadItem(sItemKey).getComponent() != null) {
					ref = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem(sItemKey).getComponent());
					// 置pk
					getBillCardPanel().getHeadItem(sIdColName).setValue(ref.getRefPK());

					// 显示name
					getBillCardPanel().getHeadItem(sItemKey).setValue(ref.getRefName());
				}
			}
		}

		// 清除对应行货位、序列号数据
		// zhy2005-08-25由于在入库单表体也增加了供应商，因此在此处清除货位序列号时需要判断下列条件
		if (!(sItemKey.equals("vvendorname") && m_voBill.getItemVOs()[row].getInOutFlag() != InOutFlag.OUT)) clearLocSnData(row, sItemKey);

		//
		if (e.getKey().equals(m_sNumItemKey) || e.getKey().equals(m_sAstItemKey) || e.getKey().equals("hsl") || e.getKey().equals("castunitname") || e.getKey().equals("vbatchcode") || e.getKey().equals(m_sNgrossnum) || e.getKey().equals("ntarenum")) {
			resetSpace(row);
		}
		// zhx added for bill row no, after edit process.
		if (e.getKey().equals(m_sBillRowNo)) {
			nc.ui.scm.pub.report.BillRowNo.afterEditWhenRowNo(getBillCardPanel(), e, m_sBillTypeCode);
			// 同步化VO
			m_voBill.setItemValue(row, m_sBillRowNo, getBillCardPanel().getBodyValueAt(row, m_sBillRowNo));

		}
		// 抽象类方法
		afterBillEdit(e);
		// getBillCardPanel().restoreFocusComponent();

//			if(sItemKey.equals("cwarehouseid")&&!m_sBillTypeCode.equals("4Q")/*货位调整的仓库进行单独处理*/){//仓库
//			Object cwarehouseid = getBillCardPanel().getHeadItem("cwarehouseid").getValueObject();
//			if (cwarehouseid==null) {
//				return;
//			}
//			IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
//			int rows = getBillCardPanel().getBillTable().getRowCount();
//			for (int i = 0; i < rows; i++) {
//				Object cinventoryid = getBillCardPanel().getBodyValueAt(i, "cinventoryid");
//				if (cinventoryid != null) {
//					StringBuffer str = new StringBuffer("");
//					str.append(" select wglsl, cspaceid ") 
//					.append("   from (select SUM(nvl(v1.ninspacenum, 0.0) - nvl(v1.noutspacenum, 0.0) - ") 
//					.append("                    nvl(icf.nfreezenum, 0.0) + nvl(icf.ndefrznum, 0.0)) wglsl, ") 
//					.append("                v1.cspaceid ") 
//					.append("           from v_ic_onhandnum6 v1 ") 
//					.append("           left join ic_freeze icf ") 
//					.append("             on v1.cinventoryid = icf.cinventoryid ") 
//					.append("            and icf.ccalbodyid = v1.ccalbodyid ") 
//					.append("            and icf.cwarehouseid = v1.cwarehouseid ") 
//					.append("            and icf.cspaceid = v1.cspaceid ") 
//					.append("            and icf.pk_corp = v1.pk_corp ") 
//					.append("            and v1.vfree1 = icf.vfree1 ") 
//					.append("            and v1.vbatchcode = icf.vbatchcode ") 
//					.append("            and nvl(icf.dr, 0) = 0 ") 
//					.append("          where 1 = 1 ") 
//					.append("            and v1.pk_corp = '"+getCorpPrimaryKey()+"' ") 
//					.append("            and v1.cwarehouseid = '"+cwarehouseid+"' ") 
//					.append("            and v1.cinventoryid = '"+cinventoryid+"' ") 
//					.append("          group by v1.cspaceid) ") 
//					.append("  where 1 = 1 ") 
//					.append("    and wglsl > 0 ") 
//					.append("    and wglsl in (select max(SUM(nvl(v1.ninspacenum, 0.0) - nvl(v1.noutspacenum, 0.0) - ") 
//					.append("                    nvl(icf.nfreezenum, 0.0)+nvl(icf.ndefrznum, 0.0))) wglsl ") 
//					.append("           from v_ic_onhandnum6 v1 ") 
//					.append("           left join ic_freeze icf ") 
//					.append("             on v1.cinventoryid = icf.cinventoryid ") 
//					.append("            and icf.ccalbodyid = v1.ccalbodyid ") 
//					.append("            and icf.cwarehouseid = v1.cwarehouseid ") 
//					.append("            and icf.cspaceid = v1.cspaceid ") 
//					.append("            and icf.pk_corp = v1.pk_corp ") 
//					.append("            and v1.vfree1 = icf.vfree1 ") 
//					.append("            and v1.vbatchcode = icf.vbatchcode ") 
//					.append("            and nvl(icf.dr, 0) = 0 ") 
//					.append("          where 1 = 1 ") 
//					.append("            and v1.pk_corp = '"+getCorpPrimaryKey()+"' ") 
//					.append("            and v1.cwarehouseid = '"+cwarehouseid+"' ") 
//					.append("            and v1.cinventoryid = '"+cinventoryid+"' ") 
//					.append("          group by v1.cspaceid) ");
//					MapListProcessor alp = new MapListProcessor();
//					Object obj = null;
//					try {
//						obj = query.executeQuery(str.toString(), alp);//执得查询
//					} catch (BusinessException e1) {
//						e1.printStackTrace();
//					}
//					ArrayList addrList = (ArrayList) obj;
//					if (addrList != null && addrList.size() > 0) {
//						Map addrMap = (Map) addrList.get(0);
//						UFDouble addrName = new UFDouble(addrMap.get("wglsl").toString());
//						String cspaceid = addrMap.get("cspaceid").toString();
//						getBillCardPanel().getBillModel().setValueAt(addrName, i, "noutnum");
//						getBillCardPanel().getBillModel().setValueAt(cspaceid, i, "cspaceid");
//					}else{
//						getBillCardPanel().getBillModel().setValueAt(null, i, "noutnum");
//						getBillCardPanel().getBillModel().setValueAt(null, i, "cspaceid");
//					}
//				}
//			}
//			getBillCardPanel().getBillModel().execLoadFormula();
//		}
//		else 
			if (("vfree1".equals(sItemKey) || "vfree0".equals(sItemKey))&&!m_sBillTypeCode.equals("4Q")/*货位调整的单件号进行单独处理*/) {
			//获得操作行的单件号
			String vfree = getBillCardPanel().getBodyValueAt(row, "vfree0") == null ? "" : getBillCardPanel().getBodyValueAt(row, "vfree0").toString();
			String Pathcoed = getvfree1(vfree);
			if("".equals(Pathcoed)){
				Pathcoed = getBillCardPanel().getBodyValueAt(row, "vfree1") == null ? "" : getBillCardPanel().getBodyValueAt(row, "vfree1").toString();
			}
			//获取表头字段仓库字段
			String cwarehouseid = getBillCardPanel().getHeadItem("cwarehouseid").getValueObject()==null?"":getBillCardPanel().getHeadItem("cwarehouseid").getValueObject().toString();
			//获得操作行的存货管理档案主键
			String cinventoryid = getBillCardPanel().getBodyValueAt(row, "cinventoryid")==null?"":getBillCardPanel().getBodyValueAt(row, "cinventoryid").toString();
			
			if (Pathcoed.equals("")||cwarehouseid.equals("")||cinventoryid.equals("")) { 
				return; 
			}
			
			String sql = getQuSql(cwarehouseid,cinventoryid,Pathcoed);
			IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			MapListProcessor alp = new MapListProcessor();
			setCspaceidAndVbatchcodeAndNoutnum(row,query,alp,sql);
		} 
//		else 
//			if (e.getKey().equals("cinventorycode")&&!m_sBillTypeCode.equals("4Q")/*货位调整的存货编码进行单独处理*/) {
//			Object cwarehouseid = getBillCardPanel().getHeadItem("cwarehouseid").getValueObject();//获取表头字段仓库字段
//			Object cinventoryid = getBillCardPanel().getBodyValueAt(row, "cinventoryid");//获得操作行的存货管理档案主键
//			if (cwarehouseid==null||cinventoryid==null) {
//				getBillCardPanel().getBillModel().setValueAt(null, row, "noutnum");
//				getBillCardPanel().getBillModel().setValueAt(null, row, "cspaceid");
//				return;
//			}
//			StringBuffer str = new StringBuffer("");//定义一个字符串缓存，用于装SQL语句
//			str.append(" select wglsl, cspaceid ") 
//			.append("   from (select SUM(nvl(v1.ninspacenum, 0.0) - nvl(v1.noutspacenum, 0.0) - ") 
//			.append("                    nvl(icf.nfreezenum, 0.0)+nvl(icf.ndefrznum, 0.0)) wglsl, ") 
//			.append("                v1.cspaceid ") 
//			.append("           from v_ic_onhandnum6 v1 ") 
//			.append("           left join ic_freeze icf ") 
//			.append("             on v1.cinventoryid = icf.cinventoryid ") 
//			.append("            and icf.ccalbodyid = v1.ccalbodyid ") 
//			.append("            and icf.cwarehouseid = v1.cwarehouseid ") 
//			.append("            and icf.cspaceid = v1.cspaceid ") 
//			.append("            and icf.pk_corp = v1.pk_corp ") 
//			.append("            and v1.vfree1 = icf.vfree1 ") 
//			.append("            and v1.vbatchcode = icf.vbatchcode ") 
//			.append("            and nvl(icf.dr, 0) = 0 ") 
//			.append("          where 1 = 1 ") 
//			.append("            and v1.pk_corp = '"+getCorpPrimaryKey()+"' ") 
//			.append("            and v1.cwarehouseid = '"+cwarehouseid+"' ") 
//			.append("            and v1.cinventoryid = '"+cinventoryid+"' ") 
//			.append("          group by v1.cspaceid) ") 
//			.append("  where 1 = 1 ") 
//			.append("    and wglsl > 0 ") 
//			.append("    and wglsl in (select max(SUM(nvl(v1.ninspacenum, 0.0) - nvl(v1.noutspacenum, 0.0) - ") 
//			.append("                    nvl(icf.nfreezenum, 0.0)+nvl(icf.ndefrznum, 0.0))) wglsl ") 
//			.append("           from v_ic_onhandnum6 v1 ") 
//			.append("           left join ic_freeze icf ") 
//			.append("             on v1.cinventoryid = icf.cinventoryid ") 
//			.append("            and icf.ccalbodyid = v1.ccalbodyid ") 
//			.append("            and icf.cwarehouseid = v1.cwarehouseid ") 
//			.append("            and icf.cspaceid = v1.cspaceid ") 
//			.append("            and icf.pk_corp = v1.pk_corp ") 
//			.append("            and v1.vfree1 = icf.vfree1 ") 
//			.append("            and v1.vbatchcode = icf.vbatchcode ") 
//			.append("            and nvl(icf.dr, 0) = 0 ") 
//			.append("          where 1 = 1 ") 
//			.append("            and v1.pk_corp = '"+getCorpPrimaryKey()+"' ") 
//			.append("            and v1.cwarehouseid = '"+cwarehouseid+"' ") 
//			.append("            and v1.cinventoryid = '"+cinventoryid+"' ") 
//			.append("          group by v1.cspaceid) ");
//			IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());//使用IUAPQueryBS服务组件，进得查询操作
//			MapListProcessor alp = new MapListProcessor();//集合处理器
//			Object obj = null;//定义一个对象，用于存放query.executeQuery()的查询结果
//			try {
//				obj = query.executeQuery(str.toString(), alp);//执得查询
//			} catch (BusinessException e1) {
//				e1.printStackTrace();
//			}
//			ArrayList addrList = (ArrayList) obj;//将查询结果转换成数组列表，放入addList中
//			if (addrList != null && addrList.size() > 0) {
//				Map addrMap = (Map) addrList.get(0);//取addrList数组中的第0行的值，放入addrMap中
//				UFDouble addrName = new UFDouble(addrMap.get("wglsl").toString());//用addName存放addMap中anum数量的值
//				String cspaceid = addrMap.get("cspaceid").toString();//用cspaceid存放addMap中cspaceid货位的值
//				getBillCardPanel().getBillModel().setValueAt(addrName, row, "noutnum");//将装有数量的值放入noutnum字段
//				getBillCardPanel().getBillModel().setValueAt(cspaceid, row, "cspaceid");//将装有货位的值放入cspaceid字段
//			}
//			getBillCardPanel().getBillModel().execLoadFormula();//选中以上的面板中的模型，执行公式
//		} 
		
	}

	/**
	 * 依据单件号获取对应的货位、批次号、数量（去除冻结数），并对当前操作行进行赋值 
	 * add by shikun 2014-04-12 
	 * @throws BusinessException 
	 * */
	@SuppressWarnings("unchecked")   //批次号
	private void setCspaceidAndVbatchcodeAndNoutnum(int row, IUAPQueryBS query, MapListProcessor alp, String sql) {
		ArrayList addrList;
		try {
			addrList = (ArrayList) query.executeQuery(sql, alp);
			if (addrList != null && addrList.size() > 0) {
				Map addrMap = (Map) addrList.get(0);
				String vbatchcode = addrMap.get("vbatchcode") == null ? "" : addrMap.get("vbatchcode").toString();
				String cspaceid = addrMap.get("cspaceid") == null ? "" : addrMap.get("cspaceid").toString();
				String csname = addrMap.get("csname") == null ? "" : addrMap.get("csname").toString();
				UFDouble wglsl = new UFDouble(addrMap.get("wglsl").toString()) == null ? new UFDouble(0.00) : new UFDouble(addrMap.get("wglsl").toString());
				if (wglsl.doubleValue()==0) {
					showErrorMessage("出库数量已经为0，请注意！");
				}
				getBillCardPanel().getBillModel().setValueAt(vbatchcode, row, "vbatchcode");
				BillEditEvent e1 = new BillEditEvent(getBillCardPanel().getBodyItem("vbatchcode").getComponent(), vbatchcode, "vbatchcode", row);
				afterLotEdit(e1);
				if (!"".equals(cspaceid)) {
					getBillCardPanel().getBillModel().setValueAt(cspaceid, row, "cspaceid");
					getBillCardPanel().getBillModel().setValueAt(csname, row, "vspacename");
					setSpace(cspaceid, csname, row);
				}else{
					m_alLocatorData.remove(row);
					m_alLocatorData.add(row, null);
					m_alSerialData.set(row, null);
				}
				UFDouble noutnum = getBillCardPanel().getBodyValueAt(row, "noutnum")==null? new UFDouble(0.00):new UFDouble(getBillCardPanel().getBodyValueAt(row, "noutnum").toString());
				if (noutnum.doubleValue()!=0&&noutnum.doubleValue()<wglsl.doubleValue()) {//本地数量不为空，并且本地数量小于有效数量，那么本地数量不变；如果本地数量为空或本地数量大于有效数量，那取有效数量
					wglsl = noutnum;
				}
				getBillCardPanel().getBillModel().setValueAt(wglsl, row, "noutnum");
				BillEditEvent e2 = new BillEditEvent(getBillCardPanel().getBodyItem("noutnum").getComponent(), wglsl, "noutnum", row);
				afterEdit(e2);
				
				getBillCardPanel().getBillModel().execLoadFormula();
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 依据单件号查询对应的货位、批次号、最大数量（去除冻结数），查询语句。
	 * add by shikun 2014-04-12 
	 * */
	private String getQuSql(String cwarehouseid, String cinventoryid, String pathcoed) {
		StringBuffer sb = new StringBuffer("");
		sb.append(" select wglsl, cspaceid,csname, vbatchcode ") 
		.append("   from (select SUM(nvl(v1.ninspacenum, 0.0) - nvl(v1.noutspacenum, 0.0) - ") 
		.append("                    nvl(icf.nfreezenum, 0.0) + nvl(icf.ndefrznum, 0.0)) wglsl, ") 
		.append("                v1.cspaceid, ") 
		.append("                ca.csname, ") 
		.append("                v1.vbatchcode ") 
		.append("           from v_ic_onhandnum6 v1 ") 
		.append("           left join bd_cargdoc ca on ca.pk_cargdoc = v1.cspaceid and nvl(ca.dr,0)=0 ") 
		.append("           left join ic_freeze icf on v1.cinventoryid = icf.cinventoryid ") 
		.append("                                  and icf.ccalbodyid = v1.ccalbodyid ") 
		.append("                                  and icf.cwarehouseid = v1.cwarehouseid ") 
		.append("                                  and nvl(icf.cspaceid,'byzgyh') = nvl(v1.cspaceid,'byzgyh') ") 
		.append("                                  and icf.pk_corp = v1.pk_corp ") 
		.append("                                  and nvl(v1.vfree1,'byzgyh') = nvl(icf.vfree1,'byzgyh') ") 
		.append("                                  and nvl(v1.vbatchcode,'byzgyh') = nvl(icf.vbatchcode,'byzgyh') ") 
		.append("                                  and nvl(icf.dr, 0) = 0 and nvl(icf.cthawpersonid,'')='' ") 
		.append("          where 1 = 1 ") 
		.append("            and v1.pk_corp = '"+getCorpPrimaryKey()+"' ") 
		.append("            and v1.vfree1 = '"+pathcoed+"' ") 
		.append("            and v1.cwarehouseid = '"+cwarehouseid+"' ") 
		.append("            and v1.cinventoryid = '"+cinventoryid+"' ") 
		.append("          group by v1.cspaceid,ca.csname, v1.vbatchcode) ") 
		.append("  where 1 = 1 ") 
		.append("    and wglsl > 0 ") 
		.append("    and wglsl = ") 
		.append("        (select max(SUM(nvl(v1.ninspacenum, 0.0) - nvl(v1.noutspacenum, 0.0) - ") 
		.append("                        nvl(icf.nfreezenum, 0.0) + nvl(icf.ndefrznum, 0.0))) wglsl ") 
		.append("           from v_ic_onhandnum6 v1 ") 
		.append("           left join ic_freeze icf on v1.cinventoryid = icf.cinventoryid ") 
		.append("                                  and icf.ccalbodyid = v1.ccalbodyid ") 
		.append("                                  and icf.cwarehouseid = v1.cwarehouseid ") 
		.append("                                  and nvl(icf.cspaceid,'byzgyh') = nvl(v1.cspaceid,'byzgyh') ") 
		.append("                                  and icf.pk_corp = v1.pk_corp ") 
		.append("                                  and nvl(v1.vfree1,'byzgyh') = nvl(icf.vfree1,'byzgyh') ") 
		.append("                                  and nvl(v1.vbatchcode,'byzgyh') = nvl(icf.vbatchcode,'byzgyh') ") 
		.append("                                  and nvl(icf.dr, 0) = 0 and nvl(icf.cthawpersonid,'')='' ") 
		.append("          where 1 = 1 ") 
		.append("            and v1.pk_corp = '"+getCorpPrimaryKey()+"' ") 
		.append("            and v1.vfree1 = '"+pathcoed+"' ") 
		.append("            and v1.cwarehouseid = '"+cwarehouseid+"' ") 
		.append("            and v1.cinventoryid = '"+cinventoryid+"' ") 
		.append("          group by v1.cspaceid, v1.vbatchcode) ") ;
		return sb.toString();
	}

	/**
	 * 创建者：王乃军 功能：自由项改变事件处理 参数：e单据编辑事件 返回： 例外： 日期：(2001-5-8 19:08:05)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	public void afterFreeItemEdit(nc.ui.pub.bill.BillEditEvent e) {
		try {
			FreeVO voFree = getFreeItemRefPane().getFreeVO();

			m_voBill.setItemFreeVO(e.getRow(), voFree);
			for (int i = 1; i <= FreeVO.FREE_ITEM_NUM; i++) {
				if (getBillCardPanel().getBodyItem("vfree" + i) != null) if (voFree != null) {
					getBillCardPanel().setBodyValueAt(voFree.getAttributeValue("vfree" + i), e.getRow(), "vfree" + i);
					m_voBill.setItemValue(e.getRow(), "vfree" + i, voFree.getAttributeValue("vfree" + i));
				} else {
					getBillCardPanel().setBodyValueAt(null, e.getRow(), "vfree" + i);
					m_voBill.setItemValue(e.getRow(), "vfree" + i, null);
				}
			}
			InvVO voInv = (InvVO) getBillCardPanel().getBodyValueAt(e.getRow(), "invvo");
			if (voInv != null) {
				voInv.setFreeItemVO(voFree);
				getBillCardPanel().setBodyValueAt(voInv, e.getRow(), "invvo");
			}
			// 清空相关的数据
			// 清空相关的数据
			if (!BillTypeConst.m_purchaseIn.equals(this.m_sBillTypeCode)) clearRowData(0, e.getRow(), "vfree0");

			clearRowData(0, e.getRow(), "vfree0");
			execEditFomulas(e.getRow(), e.getKey());

		} catch (Exception e2) {
			nc.vo.scm.pub.SCMEnv.error(e2);
		}

	}

	/**
	 * 创建者：王乃军 功能：仓库改变事件处理 参数：e单据编辑事件 返回： 例外： 日期：(2001-5-8 19:08:05)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	public void afterWhEdit(nc.ui.pub.bill.BillEditEvent e) {
		// 仓库
		afterWhEdit(e, null, null);

	}

	protected void setLastHeadRow(int row) {
		m_iLastSelListHeadRow = row;
	}

	/**
	 * 创建者：王乃军 功能：单据体、列表上表编辑事件处理 参数：e 单据编辑事件 返回： 例外： 日期：(2001-5-8 19:08:05)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	@SuppressWarnings("restriction")
	public void bodyRowChange(nc.ui.pub.bill.BillEditEvent e) {
		// 列表形式下的表头行选中
		getBillCardPanel().rememberFocusComponent();
		int row = e.getRow();
		if (e.getSource() == getBillListPanel().getHeadTable()) {

			if (row < 0 || m_alListData == null && row >= m_alListData.size()) {
				SCMEnv.out(" row now ERR ");
				return;
			}
			// 如果未改变行则返回
			if (m_iLastSelListHeadRow == row) return;
			// 清无关数据
			m_alLocatorData = null;
			m_alSerialData = null;
			// 置表体排序主键为空
			m_sLastKey = null;

			// SCMEnv.out(" Line changed to " + row);
			// 改变对应的表体显示
			setLastHeadRow(row);
			selectListBill(m_iLastSelListHeadRow);
			// 清除定位
			// clearOrientColor();
			// setBtnStatusSN(0);
			if (m_funcExtend != null) {
				// 支持功能扩展
				m_funcExtend.rowchange(this, getBillCardPanel(), getBillListPanel(), nc.ui.scm.extend.IFuncExtend.LIST, nc.ui.pub.bill.BillItem.HEAD);
			}
		} else if (e.getSource() == getBillListPanel().getBodyTable()) {
			setBtnStatusSN(row);
			if (m_funcExtend != null) {
				// 支持功能扩展
				m_funcExtend.rowchange(this, getBillCardPanel(), getBillListPanel(), nc.ui.scm.extend.IFuncExtend.LIST, nc.ui.pub.bill.BillItem.BODY);
			}
		}

		// 表单形式下的修改或选中。

		else if (e.getSource() == this.getBillCardPanel().getBillTable()) {
			if (row < 0) return;
			SCMEnv.out("line to " + e.getRow());
			setBtnStatusSN(row);
			setTailValue(row);
			if (m_funcExtend != null) {
				// 支持功能扩展
				m_funcExtend.rowchange(this, getBillCardPanel(), getBillListPanel(), nc.ui.scm.extend.IFuncExtend.CARD, nc.ui.pub.bill.BillItem.BODY);
			}

			// v5 lj 增加自由项目着色器
			getFreeItemCellRender().setRenderer("vfree0");
			getLotRefCellRender().setRenderer("vbatchcode");

			// 刷新现存量Panel显示
			if (m_bOnhandShowHidden) {
				showOnHandPnlInfo(e.getRow());
			}
			// 显示
			getOnHandRefDeal().setInvCtrlValue(e.getRow());
			setCardMode();
			m_layoutManager.show();

		}

		getBillCardPanel().restoreFocusComponent();

	}

	private FreeItemCellRender m_freeCellRender = null;

	protected FreeItemCellRender getFreeItemCellRender() {
		if (m_freeCellRender == null) m_freeCellRender = new FreeItemCellRender(getBillCardPanel());
		return m_freeCellRender;
	}

	private LotItemRefCellRender m_lotCellRender = null;

	protected LotItemRefCellRender getLotRefCellRender() {
		if (m_lotCellRender == null) m_lotCellRender = new LotItemRefCellRender(getBillCardPanel());
		return m_lotCellRender;
	}

	// private OnHandRefreshVO m_voLineOnHand = new OnHandRefreshVO();//
	// 行切换时现存量显示入口参数

	// /**
	// * 显示现存量参照的数据
	// *
	// *
	// * @param iRow
	// */
	// protected void showOnHandPnlInfo(int iRow) {
	// if (m_pnlBcAndOnHand == null || iRow < 0)
	// return;
	//
	// m_pnlBcAndOnHand.setVORefresh(getSelectedItemHandInfo(iRow));
	//
	// m_pnlBcAndOnHand.showData();
	//
	// }

	/**
	 * 创建者：王乃军 功能：清除指定行的数据 参数： 返回： 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	protected void clearRowData(int row) {
		nc.ui.pub.bill.BillModel bmBill = getBillCardPanel().getBillModel();
		int iRowCount = bmBill.getRowCount();
		BillItem[] items = getBillCardPanel().getBodyItems();
		if (items == null || row >= iRowCount) {
			SCMEnv.out("row too big.");
			return;
		}

		// 删除界面数据
		// 删除界面数据
		String sColKey = null;
		int iColCount = items.length;

		for (int col = 0; col < iColCount; col++) {

			sColKey = items[col].getKey();
			if (!m_sBillPkItemKey.equals(sColKey) && !m_sBillRowItemKey.equals(sColKey) && !"crowno".equals(sColKey)) bmBill.setValueAt(null, row, col);
		}
		// 同步vo
		m_voBill.clearItem(row);

	}

	/**
	 * 创建者：王乃军 功能：清除指定行、指定列的数据 参数： 返回： 例外： 日期：(2001-5-9 9:23:32)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	protected void clearRowData(int row, String[] saColKey) {
		nc.ui.pub.bill.BillModel bmBill = getBillCardPanel().getBillModel();
		int iRowCount = bmBill.getRowCount();
		if (row >= iRowCount || saColKey == null || saColKey.length == 0) {
			SCMEnv.out("row too big or needn't clear.");
			return;
		}
		// 删除界面数据
		nc.ui.pub.bill.BillItem biaBody[] = bmBill.getBodyItems();
		Hashtable<String, String> htBodyItem = new Hashtable<String, String>();
		// 把列放到hash中
		for (int col = 0; col < biaBody.length; col++)
			htBodyItem.put(biaBody[col].getKey(), "OK");

		for (int col = 0; col < saColKey.length; col++)
			if (saColKey[col] != null && getBillCardPanel().getBodyItem(saColKey[col]) != null) {
				try {
					// SCMEnv.out("clear "+saColKey[col]);
					try {
						// 如果有，清除之
						if (htBodyItem.containsKey(saColKey[col])) bmBill.setValueAt(null, row, saColKey[col]);
					} catch (Exception e3) {
					}

					// 同步vo
					m_voBill.setItemValue(row, saColKey[col], null);
					// 如果是自由项的话需同时清vfree1--->vfree10
					if (saColKey[col].equals("vfree0")) {
						for (int i = 1; i <= 10; i++) {
							// 如果有，清除之
							if (htBodyItem.contains(saColKey[col])) bmBill.setValueAt(null, row, "vfree" + i);
							// 同步vo
							m_voBill.setItemValue(row, "vfree" + i, null);
						}
					}
					// 在afterEdit中调用clearLocSN处理locator/sn
					// //清货位分配结果
					// if (saColKey[col].trim().equals("locator")) {
					// if (m_alLocatorData != null && m_alLocatorData.size() >
					// row)
					// m_alLocatorData.set(row, null);
					// m_voBill.getItemVOs()[row].setLocator(null);
					// }
					// //清序列号分配结果
					// if (saColKey[col].trim().equals("sn")) {
					// if (m_alSerialData != null && m_alSerialData.size() >
					// row)
					// m_alSerialData.set(row, null);
					// m_voBill.getItemVOs()[row].setSerial(null);
					// }

				} catch (Exception e) {
					// nc.vo.scm.pub.SCMEnv.error(e);
					SCMEnv.out("nc.ui.ic.pub.bill.GeneralBillClientUI.clearRowData(int, String [])：set value ERR.--->" + saColKey[col]);
				} finally {

				}
			}

	}

	/**
	 * 创建者：王乃军 功能：滤去表单形式下的空行，查询 参数： 返回： 例外： 日期：(2001-5-9 9:23:32)
	 * 修改日期，修改人，修改原因，注释标志： 2001/10/29,wnj,提高效率
	 * 
	 * 
	 * 
	 */
	public void filterNullLine() {

		// 存货列值暂存
		Object oTempValue = null;
		// 表体model
		nc.ui.pub.bill.BillModel bmBill = getBillCardPanel().getBillModel();
		// 存货列号，效率高一些。
		int iInvCol = bmBill.getBodyColByKey(m_sInvMngIDItemKey);

		// 必须有存货列
		if (bmBill != null && iInvCol >= 0 && iInvCol < bmBill.getColumnCount()) {
			// 行数
			int iRowCount = getBillCardPanel().getRowCount();
			// 从后向前删
			for (int line = iRowCount - 1; line >= 0; line--) {
				// 存货未填
				oTempValue = bmBill.getValueAt(line, iInvCol);
				if (oTempValue == null || oTempValue.toString().trim().length() == 0)
				// 删行
				getBillCardPanel().getBillModel().delLine(new int[] {
					line
				});
			}
		}
	}

	/**
	 * 返回 BillCardPanel1 特性值。
	 * 
	 * @return nc.ui.pub.bill.BillCardPanel 1.
	 *         新建一个billcardpanel,得到templetData并传递给BillData 2.
	 *         将自由项和批次号参照控件置入BillData（替换原Componet），项目参照也是 3. 过滤表体供应商 4.
	 *         设置表体m_sTitle和自定义项 5.
	 *         将BillData设置入billCardPanel,重新设置行号，返回billCardPanel
	 */
	/* 警告：此方法将重新生成。 */
	@SuppressWarnings("restriction")
	protected nc.ui.pub.bill.BillCardPanel getBillCardPanel() {
		if (ivjBillCardPanel == null) {
			try {
				ivjBillCardPanel = new nc.ui.pub.bill.BillCardPanel();
				ivjBillCardPanel.setName("BillCardPanel");
				// user code begin {1}
				// nc.vo.pub.bill.BillTempletVO btv =
				// 加载模版数据
				// ivjBillCardPanel.loadTemplet(m_sBillTypeCode, null,
				// m_sUserID, m_sCorpID);
				// BillData bd = ivjBillCardPanel.getBillData();
				BillData bd = new BillData(ivjBillCardPanel.getTempletData(m_sBillTypeCode, null, m_sUserID, m_sCorpID));

				if (bd == null) {
					SCMEnv.out("--> billdata null.");
					return ivjBillCardPanel;
				}
				// 自由项参照
				if (bd.getBodyItem("vfree0") != null) getFreeItemRefPane().setMaxLength(bd.getBodyItem("vfree0").getLength());

				// zhy 2005-04-13 表头批次参照
				if (bd.getHeadItem("vbatchcode") != null) {
					bd.getHeadItem("vbatchcode").setComponent(getLotNumbRefPane());
				}
				// zhy 2005-04-13 表头自由项
				if (bd.getHeadItem("vfree0") != null) {
					bd.getHeadItem("vfree0").setComponent(getFreeItemRefPane());
				}

				// 非期初单加批次号参照
				// zhy2006-04-18 由于加了批次号档案,故不对期初单据特殊处理
				// if (!m_bIsInitBill) {
				if (bd.getBodyItem("vbatchcode") != null) getLotNumbRefPane().setMaxLength(bd.getBodyItem("vbatchcode").getLength());
				if (bd.getBodyItem("vbatchcode") != null) bd.getBodyItem("vbatchcode").setComponent(getLotNumbRefPane());
				// }
				if (bd.getBodyItem("vvehiclecode") != null) bd.getBodyItem("vvehiclecode").setComponent(getVehicleRefPane());

				// 将自由项参照加入单据模板表体
				if (bd.getBodyItem("vfree0") != null) bd.getBodyItem("vfree0").setComponent(getFreeItemRefPane()); // 将自由项参照加入单据模板表体
				// 项目参照
				m_refJob = new nc.ui.pub.beans.UIRefPane();
				m_refJob.setRefNodeName("项目管理档案");
				m_refJob.getRefModel().setPk_corp(m_sCorpID);
				// m_refJob.setIsCustomDefined(true);
				// m_refJob.setRefType(2);
				// m_refJobModel = new nc.ui.bd.b39.JobRefTreeModel(m_sGroupID,
				// m_sCorpID, null);
				// m_refJob.setRefModel(m_refJobModel);

				if (bd.getBodyItem("cprojectname") != null) bd.getBodyItem("cprojectname").setComponent(m_refJob);

				// 项目阶段参照
				m_refJobPhase = new nc.ui.pub.beans.UIRefPane();
				m_refJobPhase.setIsCustomDefined(true);
				try {
					m_refJobPhaseModel = new nc.ui.bd.b39.PhaseRefModel();
				} catch (Exception e) {

				}
				m_refJobPhase.setRefModel(m_refJobPhaseModel);
				if (bd.getBodyItem("cprojectphasename") != null) bd.getBodyItem("cprojectphasename").setComponent(m_refJobPhase);

				// 出库单表体供应商
				if (bd.getBodyItem("vvendorname") != null) RefFilter.filtProvider(bd.getBodyItem("vvendorname"), m_sCorpID, null);
				if (bd.getBodyItem("vspacename") != null) bd.getBodyItem("vspacename").setComponent(getLocatorRefPane()); //

				// 修改自定义项

				bd = changeBillDataByUserDef(getDefHeadVO(), getDefBodyVO(), bd);
				ivjBillCardPanel.setBillData(bd);

				bd = BatchCodeDefSetTool.changeBillDataByBCUserDef(m_sCorpID, bd);
				ivjBillCardPanel.setBillData(bd);

				if (bd.getHeadItem("cbilltypecode") != null) m_sTitle = bd.getHeadItem("cbilltypecode").getName();
				if (ivjBillCardPanel.getTitle() != null && ivjBillCardPanel.getTitle().trim().length() > 0) m_sTitle = ivjBillCardPanel.getTitle();
				// zhx new add billrowno
				nc.ui.scm.pub.report.BillRowNo.loadRowNoItem(ivjBillCardPanel, m_sBillRowNo);
				// 将原单据模板的表体行隐藏!
				ivjBillCardPanel.getBodyPanel().setRowNOShow(nc.ui.ic.pub.bill.Setup.bShowBillRowNo);

				// 质量等级参照设置
				try {
					UIRefPane uiRefPanel = null;
					if (ivjBillCardPanel.getBodyItem("cqualitylevelname") != null) uiRefPanel = (UIRefPane) ivjBillCardPanel.getBodyItem("cqualitylevelname").getComponent();
					if (uiRefPanel != null) {
						uiRefPanel.setIsCustomDefined(true);
						uiRefPanel.setRefModel((AbstractRefModel) Class.forName("nc.ui.qc.pub.CheckstateDef").newInstance());
						uiRefPanel.getRefModel().setPk_corp(m_sCorpID);
						uiRefPanel.setReturnCode(false);
					}
					BillItem item = ivjBillCardPanel.getBodyItem("cmeaswarename");
					if (item != null) {
						AbstractRefModel refModel = (AbstractRefModel) Class.forName("nc.ui.mm.pub.pub1010.JlcRefModel").newInstance();
						UIRefPane ref = new UIRefPane();
						ref.setRefNodeName("自定义参照");
						ref.setIsCustomDefined(true);
						ref.setRefModel(refModel);
						item.setComponent(ref);
					}

				} catch (java.lang.Throwable e) {
				}
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBillCardPanel;
	}

	/**
	 * 返回 BillListPanel1 特性值。
	 * 
	 * @return nc.ui.pub.bill.BillListPanel 1.新疆billListPanel，加载模板数据
	 *         2.得到模板数据BillListData，修改自定义项，重新设入 3.设置表头选择模式，隐藏以listid开头的表头列
	 *         4.显示所有表体列，返回billListPanel
	 */
	/* 警告：此方法将重新生成。 */
	protected nc.ui.pub.bill.BillListPanel getBillListPanel() {
		if (ivjBillListPanel == null) {
			try {
				ivjBillListPanel = new nc.ui.pub.bill.BillListPanel();
				ivjBillListPanel.setName("BillListPanel");
				// user code begin {1}
				// ivjBillListPanel.loadTemplet(m_sTempletID);
				// 加载列表模版
				ivjBillListPanel.loadTemplet(m_sBillTypeCode, null, m_sUserID, m_sCorpID);

				BillListData bd = ivjBillListPanel.getBillListData();
				// 修改自定义项

				bd = changeBillListDataByUserDef(getDefHeadVO(), getDefBodyVO(), bd);

				bd = BatchCodeDefSetTool.changeBillListDataByBCUserDef(m_sCorpID, bd);

				ivjBillListPanel.setListData(bd);

				// 设置小数精度
				// setScaleOfListPanel();

				ivjBillListPanel.getHeadTable().setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				// 滤掉listid的列
				nc.ui.pub.bill.BillItem biListItem[] = ivjBillListPanel.getHeadBillModel().getBodyItems();
				if (biListItem != null) for (int col = biListItem.length - 1; col >= 0; col--)
					if (biListItem[col].getName() != null && biListItem[col].getName().startsWith("listid")) {
						try {
							hideListTableHeadCol(ivjBillListPanel.getParentListPanel(), col);
							// SCMEnv.out("hide
							// "+biListItem[col].getName());
						} catch (Exception e) {
						}
					}
				// user code end
				ivjBillListPanel.getChildListPanel().setTotalRowShow(true);
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBillListPanel;
	}

	/**
	 * 返回 FreeItemRefPane1 特性值。
	 * 
	 * @return nc.ui.ic.pub.freeitem.FreeItemRefPane
	 */
	/* 警告：此方法将重新生成。 */
	@SuppressWarnings("restriction")
	protected FreeItemRefPane getFreeItemRefPane() {
		if (ivjFreeItemRefPane == null) {
			try {
				ivjFreeItemRefPane = new FreeItemRefPane();
				ivjFreeItemRefPane.setName("FreeItemRefPane");
				ivjFreeItemRefPane.setLocation(209, 4);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjFreeItemRefPane;
	}

	/**
	 * 返回 LotNumbRefPane1 特性值。
	 * 
	 * @return nc.ui.ic.pub.lot.LotNumbRefPane
	 */
	/* 警告：此方法将重新生成。 */
	@SuppressWarnings("restriction")
	protected nc.ui.ic.pub.lot.LotNumbRefPane getLotNumbRefPane() {
		if (ivjLotNumbRefPane == null) {
			try {
				ivjLotNumbRefPane = new nc.ui.ic.pub.lot.LotNumbRefPane();
				ivjLotNumbRefPane.setName("LotNumbRefPane");
				ivjLotNumbRefPane.setLocation(38, 1);
				ivjLotNumbRefPane.setIsMutiSel(true);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjLotNumbRefPane;
	}

	/**
	 * 返回 SerialAllocationDlg1 特性值。
	 * 
	 * @return nc.ui.ic.pub.sn.SerialAllocationDlg
	 */
	/* 警告：此方法将重新生成。 */
	@SuppressWarnings("restriction")
	protected nc.ui.ic.pub.sn.SerialAllocationDlg getSerialAllocationDlg() {
		if (m_dlgSerialAllocation == null) {
			try {
				m_dlgSerialAllocation = new nc.ui.ic.pub.sn.SerialAllocationDlg(this);
				m_dlgSerialAllocation.setName("SerialAllocationDlg");
				// m_dlgSerialAllocation.setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
				// user code begin {1}
				// m_dlgSerialAllocation.setParent(this);
				// 小数精度设置
				m_dlgSerialAllocation.setScale(m_ScaleValue.getScaleValueArray());
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return m_dlgSerialAllocation;
	}

	/**
	 * 返回 SpaceAllocationDlg1 特性值。
	 * 
	 * @return nc.ui.ic.pub.locator.SpaceAllocationDlg
	 */
	/* 警告：此方法将重新生成。 */
	@SuppressWarnings("restriction")
	protected nc.ui.ic.pub.locator.SpaceAllocationDlg getSpaceAllocationDlg() {
		if (m_dlgSpaceAllocation == null) {
			try {
				m_dlgSpaceAllocation = new nc.ui.ic.pub.locator.SpaceAllocationDlg(this);
				m_dlgSpaceAllocation.setName("SpaceAllocationDlg");
				// m_dlgSpaceAllocation.setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
				// user code begin {1}
				// 小数精度设置
				m_dlgSpaceAllocation.setScale(m_ScaleValue.getScaleValueArray());
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return m_dlgSpaceAllocation;
	}

	/**
	 * 子类实现该方法，返回业务界面的标题。
	 * 
	 * @version (00-6-6 13:33:25)
	 * 
	 * @return java.lang.String
	 */
	public String getTitle() {
		return m_sTitle;
	}

	/**
	 * 创建者：王乃军 功能：返回仓库名，只适用于出入库单。特殊单要区分出/入仓库itemkey 参数： 返回： 例外： 日期：(2001-5-9
	 * 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	protected String getWhName() {
		if (m_voBill != null && m_voBill.getHeaderValue("cwarehousename") != null) return m_voBill.getHeaderValue("cwarehousename").toString();
		else return null;
	}

	/**
	 * 每当部件抛出异常时被调用
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 */
	protected void handleException(java.lang.Throwable exception) {

		/* 除去下列各行的注释，以将未捕捉到的异常打印至 stdout。 */
		SCMEnv.out("--------- 未捕捉到的异常 ---------");
		nc.vo.scm.pub.SCMEnv.error(exception);
	}

	/**
	 * 初始化类。
	 */
	/* 警告：此方法将重新生成。 */
	public void initialize() {
		m_timer.start("初始化类开始：");
		try {
			// 得到环境变量，并保存到成员变量。
			m_timer.showExecuteTime("getCEnvInfo()：");
			getCEnvInfo();

			m_timer.showExecuteTime("初始化开始：");
			// initButtons();
			m_timer.showExecuteTime("initButtons：");
			// ydy 04-05-12 简化代码，重用
			initialize(m_sCorpID, m_sUserID, m_sUserName, null, m_sGroupID, m_sLogDate);

			// getBillCardPanelSum().setVisible(true);

		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}

	}

	/**
	 * 删除列表下的一张单据
	 * 
	 * @author ljun
	 * @since v5 调用时机一： 参照多张单据生成时，卡片界面下保存后自动转入列表界面时，删除保存的单据
	 * 
	 */
	protected void delBillOfList(int iSel) {
		if (iSel < 0) return;
		if (m_alListData == null) return;
		// 如果删除后m_alListData.size()==0 , 则m_iLastSelListHeadRow为-1
		m_alListData.remove(iSel);

		// m_iCurDispBillNum在删除后为第一个列表单据
		if (BillMode.Card == m_iCurPanel) m_iCurDispBillNum = -1;
		else m_iCurDispBillNum = 0;

		if (m_alListData.size() == 0) setLastHeadRow(-1);
		else setLastHeadRow(0);

		m_iBillQty = m_alListData.size();

		// 刷新界面显示
		GeneralBillHeaderVO voaBillHeader[] = new GeneralBillHeaderVO[m_alListData.size()];
		for (int i = 0; i < m_alListData.size(); i++) {
			if (m_alListData.get(i) != null) voaBillHeader[i] = ((GeneralBillVO) m_alListData.get(i)).getHeaderVO();
			else {
				SCMEnv.out("list data error!-->" + i);
				return;
			}

		}
		if (m_alListData.size() <= 0) {
			getBillListPanel().setHeaderValueVO(null);
			getBillListPanel().setBodyValueVO(null);
		} else {
			setListHeadData(voaBillHeader);
			// 选中列表单据 ，以刷新表体显示
			selectListBill(m_iLastSelListHeadRow);
		}

	}

	/**
	 * 创建者：王乃军 功能：把表单形式下的单据插入到列表，用于新增、关联录入、复制保存后。
	 */
	@SuppressWarnings("unchecked")
	protected void insertBillToList(GeneralBillVO voBill) {
		if (voBill == null || voBill.getParentVO() == null || voBill.getChildrenVO() == null) {
			SCMEnv.out("Bill is null !");
			return;
		}

		// 当前没有单据
		if (m_alListData == null) m_alListData = new ArrayList();
		// 第一个或最后一个，追加单据
		// if (m_iLastSelListHeadRow < 0 || m_iLastSelListHeadRow == m_iBillQty
		// - 1)
		// ..................注意clone()...........................
		// 无需重算换算率
		voBill.setHaveCalConvRate(true);
		// ------------------
		m_alListData.add(voBill.clone());
		// else //插入
		// m_alListData.add(m_iLastSelListHeadRow + 1, voBill.clone());

		m_iBillQty = m_alListData.size();

		// 选中的新增的行。
		setLastHeadRow(m_iBillQty - 1);

		// 表单形式下当前显示的单据序号，当列表---〉表单切换时，未改选其它单据，则无需重设表单数据，以提高效率。
		if (BillMode.Card == m_iCurPanel) m_iCurDispBillNum = m_iLastSelListHeadRow;
		else m_iCurDispBillNum = 0;
		// 刷新界面显示
		GeneralBillHeaderVO voaBillHeader[] = new GeneralBillHeaderVO[m_alListData.size()];
		for (int i = 0; i < m_alListData.size(); i++) {
			if (m_alListData.get(i) != null) voaBillHeader[i] = ((GeneralBillVO) m_alListData.get(i)).getHeaderVO();
			else {
				SCMEnv.out("list data error!-->" + i);
				return;
			}

		}
		setListHeadData(voaBillHeader);
		// 选中列表单据 ，以刷新表体显示
		selectListBill(m_iLastSelListHeadRow);
	}

	/**
	 * 创建者：王乃军 功能：新增处理 参数： 返回： 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	public void onAdd() {
		// 当前是列表形式时，首先切换到表单形式
		onAdd(true, null);
	}

	/**
	 * 创建者：yb 功能：签字 参数： 返回： 例外： 日期：(2001-5-9 9:23:32)
	 * 
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 */
	@SuppressWarnings({ "restriction", "static-access", "unchecked" })
	public Object[] onBatchProcessInThread(GeneralBillVO[] voaBills, String sAction) throws Exception {
		if (voaBills == null || voaBills.length <= 0) return null;

		StringBuffer sb = new StringBuffer("");
		String stemp = null;
		int errcount = 0;
		final int isleep = 200;
		Object[] oret = new Object[2];
		ArrayList<String> pksecusslist = new ArrayList<String>();
		UFTime ufdPre1 = new UFTime(System.currentTimeMillis());// 系统当前时间
		UFDateTime ufdPre = new UFDateTime(m_sLogDate + " " + ufdPre1.toString());
		for (int i = 0; i < voaBills.length; i++) {
			stemp = null;
			try {
				if (ICConst.SIGN.equals(sAction)) {
					stemp = checkBillForAudit(voaBills[i]);
					if (stemp != null) {
						sb.append(stemp + "\n");
						LongTimeTask.showHintMsg(stemp);
						Thread.currentThread().sleep(isleep);
						continue;
					}
					voaBills[i] = getAuditVO(voaBills[i], ufdPre);
					// modify by liuzy 2007-04-28 reason:multi-language
					// LongTimeTask.showHintMsg("单据"+voaBills[i].getHeaderVO().getVbillcode()+"开始签字...");
					LongTimeTask.showHintMsg(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000513", null, new String[] {
						voaBills[i].getHeaderVO().getVbillcode()
					}));
					onAuditKernel(voaBills[i]);
					stemp = nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000514", null, new String[] {
						voaBills[i].getHeaderVO().getVbillcode()
					});
					LongTimeTask.showHintMsg(stemp);
				} else if (ICConst.CANCELSIGN.equals(sAction)) {
					stemp = checkBillForCancelAudit(voaBills[i]);
					if (stemp != null) {
						sb.append(stemp + "\n");
						LongTimeTask.showHintMsg(stemp);
						Thread.currentThread().sleep(isleep);
						continue;
					}
					voaBills[i] = getCancelAuditVO(voaBills[i]);
					LongTimeTask.showHintMsg(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000515", null, new String[] {
						voaBills[i].getHeaderVO().getVbillcode()
					}));
					nc.ui.pub.pf.PfUtilClient.processAction(ICConst.CANCELSIGN, m_sBillTypeCode, m_sLogDate, voaBills[i]);
					stemp = nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000516", null, new String[] {
						voaBills[i].getHeaderVO().getVbillcode()
					});
					LongTimeTask.showHintMsg(stemp);
				}
				if (stemp != null) sb.append(stemp + "\n");
				pksecusslist.add(voaBills[i].getHeaderVO().getCgeneralhid());
				Thread.currentThread().sleep(isleep);
			} catch (Exception e) {
				e = nc.ui.ic.pub.tools.GenMethod.handleException(null, null, e);
				if (ICConst.SIGN.equals(sAction)) stemp = nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000517", null, new String[] {
					voaBills[i].getHeaderVO().getVbillcode()
				});
				else if (ICConst.CANCELSIGN.equals(sAction)) stemp = nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000518", null, new String[] {
					voaBills[i].getHeaderVO().getVbillcode()
				});
				LongTimeTask.showHintMsg(stemp);
				sb.append(stemp + "[" + e.getMessage() + "]");
				sb.append("\n");
				errcount++;
				try {
					Thread.currentThread().sleep(isleep);
				} catch (Exception ee) {
					SCMEnv.out(ee.getMessage());
				}
			}
		}

		if (pksecusslist.size() > 0) {
			try {
				LongTimeTask.showHintMsg(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000519"));
				if (m_alListData == null) m_alListData = new ArrayList();
				GeneralBillUICtl.refreshBillVOsByPks(m_alListData, pksecusslist.toArray(new String[pksecusslist.size()]));
				setDataOnList(m_alListData, true);
			} catch (Exception e) {
				throw e;
			}
		}
		oret[0] = new Integer(errcount);
		// if(errcount>0){
		oret[1] = sb.toString();
		// }
		return oret;
	}

	/**
	 * 创建者：yb 功能：签字 参数： 返回： 例外： 日期：(2001-5-9 9:23:32)
	 * 
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 */
	@SuppressWarnings({ "unchecked", "restriction" })
	public void onBatchAction(String sAction) {

		ArrayList alvo = null;
		try {
			alvo = (ArrayList) LongTimeTask.procclongTime(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000522"), 1000, 3, this.getClass().getName(), this, "getSelectedBills", null, null);
		} catch (Exception e) {
			nc.ui.ic.pub.tools.GenMethod.handleException(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000523"), e);
			this.updateUI();
			return;
		}
		// getSelectedBills();
		if (alvo == null || alvo.size() <= 0) {
			this.updateUI();
			return;
		}
		GeneralBillVO voaBills[] = new GeneralBillVO[alvo.size()];

		try {

			for (int i = 0; i < alvo.size(); i++) {
				voaBills[i] = (GeneralBillVO) alvo.get(i);
				voaBills[i] = (GeneralBillVO) voaBills[i].clone();
			}

			String shint = null;
			if (ICConst.SIGN.equals(sAction)) shint = nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000520");
			else if (ICConst.CANCELSIGN.equals(sAction)) shint = nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000521");

			Object[] oret = (Object[]) LongTimeTask.procclongTime(this, shint, 10, 3, this.getClass().getName(), this, "onBatchProcessInThread", new Class[] {
					GeneralBillVO[].class, String.class
			}, new Object[] {
					voaBills, sAction
			});
			if (oret != null && oret[1] != null) showWarningMessage(oret[1].toString());
			// if(((Integer)oret[0]).intValue()!=voaBills.length){

			// m_bQuery = false;
			// LongTimeTask.procclongTime(this,"正在刷新界面...",0,
			// 3,this.getClass().getName(),this,"onQuery",
			// null,null);
			// }
		} catch (Exception e) {
			nc.ui.ic.pub.tools.GenMethod.handleException(this, null, e);
		}
		this.updateUI();
	}

	/**
	 * 创建者：yb 功能：删除处理 参数： 返回： 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	public String checkBillForAudit(GeneralBillVO vobill) {
		if (vobill == null) return null;
		if (vobill.getHeaderVO().getFbillflag() != null && vobill.getHeaderVO().getFbillflag().intValue() != BillStatus.IFREE) return "单据[" + vobill.getHeaderVO().getVbillcode() + "]不是自由状态，不能签字！";
		return null;
	}

	/**
	 * 创建者：王乃军 功能：签字 参数： 返回： 例外： 日期：(2001-5-9 9:23:32)
	 * 
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 */
	@SuppressWarnings("restriction")
	public GeneralBillVO getAuditVO(GeneralBillVO voAudit, UFDateTime sysdatetime) {

		// 设置条码状态未没有被修改
		setBillBCVOStatus(voAudit, nc.vo.pub.VOStatus.UNCHANGED);
		// 支持平台，clone一个，以便于以后的处理，同时防止修改了m_voBill
		GeneralBillHeaderVO voHead = voAudit.getHeaderVO();

		// 签字人
		voHead.setCregister(m_sUserID);
		// 可以不是当前登录单位的单据，所以不需要修改单据。
		// voHead.setPk_corp(m_sCorpID);
		voHead.setDaccountdate(new nc.vo.pub.lang.UFDate(m_sLogDate));
		voHead.setAttributeValue("taccounttime", sysdatetime.toString()); // 签字时间//zhy2005-06-15签字时间=登陆日期+系统时间

		// vo可能要传给平台，所以要做成和签字后的单据
		// voHead.setFbillflag(new
		// Integer(nc.vo.ic.pub.bill.BillStatus.SIGNED));
		voHead.setCoperatoridnow(m_sUserID); // 当前操作员2002-04-10.wnj
		voHead.setAttributeValue("clogdatenow", m_sLogDate); // 当前登录日期

		voAudit.setParentVO(voHead);

		// 根据仓库解析获得仓库是否存货核算属性 add by hanwei 2004-4-30
		getGenBillUICtl().setBillIscalculatedinvcost(voAudit);

		// 平台：需要表体带表头PK
		GeneralBillItemVO voaItem[] = voAudit.getItemVOs();
		int iRowCount = voAudit.getItemCount();
		for (int i = 0; i < iRowCount; i++) {
			// 表头PK
			voaItem[i].setCgeneralhid(voHead.getPrimaryKey());
			// set delete flag------- obligatory for ts test.
		}
		voAudit.setChildrenVO(voaItem);

		voAudit.setStatus(nc.vo.pub.VOStatus.UNCHANGED);
		setBillBCVOStatus(voAudit, nc.vo.pub.VOStatus.UNCHANGED);

		voAudit.setIsCheckCredit(true);
		voAudit.setIsCheckPeriod(true);
		voAudit.setIsCheckAtp(true);

		nc.vo.sm.log.OperatelogVO log = getNormalOperateLog();
		voAudit.setAccreditUserID(m_sUserID);
		voAudit.setOperatelogVO(log);

		// 帐期、信用信息
		voAudit.m_iActionInt = nc.vo.scm.pub.bill.CreditConst.ICREDIT_ACT_OTHER;
		voAudit.m_sActionCode = "SIGN";

		return voAudit;

	}
	


	/**
	 * 创建者：王乃军 功能：签字 参数： 返回： 例外： 日期：(2001-5-9 9:23:32)
	 * 
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 */
	@SuppressWarnings("restriction")
	public void onAudit() {

		m_timer.start("签字开始：");	
		try {

			if (BillMode.List == m_iCurPanel && getBillListPanel().getHeadTable().getSelectedRowCount() > 1) {
				onBatchAction(ICConst.SIGN);
				return;
			}

			if (m_iLastSelListHeadRow >= 0 && m_alListData != null && m_alListData.size() > m_iLastSelListHeadRow && m_alListData.get(m_iLastSelListHeadRow) != null)
			// 这里不能clone(),修改m_voBill同时修改list.
			m_voBill = (GeneralBillVO) ((GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow)).clone();
			
			if (m_voBill != null) {

				UFTime ufdPre1 = new UFTime(System.currentTimeMillis());// 系统当前时间
				UFDateTime ufdPre = new UFDateTime(m_sLogDate + " " + ufdPre1.toString());

				// 设置条码状态未没有被修改
				setBillBCVOStatus(m_voBill, nc.vo.pub.VOStatus.UNCHANGED);
				// 支持平台，clone一个，以便于以后的处理，同时防止修改了m_voBill
				GeneralBillVO voAudit = (GeneralBillVO) m_voBill.clone();
				voAudit = getAuditVO(voAudit, ufdPre);

				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000003")/*
																												* @res "正在签字，请稍候..."
																												*/);
				m_timer.showExecuteTime("before 平台签字：");

				
				while (true) {
					try {
						// 审核的核心方法
						onAuditKernel(voAudit);//78、85
						
						/****************************************add by yhj 2014-03-14 START*********************/
						/**
						 *此处代码已经转移到ia->client:nc.ui.ia.bill.BillClientUI.java
						 */
						//首先判断业务流程-》代加工的流程才会执行以下代码
//						if(voAudit != null && voAudit.getChildrenVO().length > 0){
//							//1016A210000000098ZO3 代加工成品销售业务流程
//							if(voAudit.getParentVO().getAttributeValue("cbiztype").equals("1016A210000000098ZO3")){
//								IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
//								GeneralBillVO gbvo = voAudit;
//								//取出销售出库单 公司、单据类型、
//								CircularlyAccessibleValueObject[] cvos = gbvo.getChildrenVO();
//								String temp_pk_corp = (String)voAudit.getParentVO().getAttributeValue("pk_corp");//公司
//								String temp_billtypecode = (String)voAudit.getParentVO().getAttributeValue("cbilltypecode");//单据类型
//								String temp_cfirstbillbid = null;
//								String temp_cinventoryid = null;
//								if(cvos != null && cvos.length > 0){
//									List<GeneralBillItemVO> itemvosList = new ArrayList<GeneralBillItemVO>();
//									for (int i = 0; i < cvos.length; i++) {
//										//取到销售出库单表体跺号、实际出库数量
//										String temp_dh = (String) cvos[i].getAttributeValue("vfree0");//跺号
//										UFDouble temp_num = (UFDouble)cvos[i].getAttributeValue("noutnum");//实际出库数量
//										temp_cinventoryid = (String)cvos[i].getAttributeValue("cinventoryid");//存货主键
//										temp_cfirstbillbid = (String)cvos[i].getAttributeValue("cfirstbillbid");//源头单据表体ID
//										//根据跺号-》取到产成品入库单的单价
//										/**
//										 * 因销售出库单界面的跺号是以这个形式显示的，所以还需要改变一下跺号的格式，去掉大括号，中文，
//										 * 及冒号-》[垛号:[垛号:20130922-01]]
//										 */
//										BigDecimal temp_nprice = null;
//										if(temp_dh != null && temp_dh.length() > 0){
//											String ref_temp_dh = temp_dh;
//											int count=0;
//											int index = ref_temp_dh.indexOf(":");
//											while( ref_temp_dh.indexOf(":")!=-1){
//											  count++;
//											  ref_temp_dh = ref_temp_dh.substring(ref_temp_dh.indexOf(":") +1);
//											} 
//											
//											if(count == 1){
//												String res = temp_dh.substring(1, temp_dh.length() - 1);
//												String[] str = res.split(":");
//												temp_dh = str[1];
//											}else if(count == 2){
//												//去掉外层[]
//												String res = temp_dh.substring(1, temp_dh.length() - 1);
//												String[] str = res.split(":");
//												//根据冒号截取外层跺号
//												String inner_dh  = str[1];
//												//去掉内层[]
//												temp_dh = inner_dh.substring(1, inner_dh.length() - 1);
//												//根据冒号截取内层跺号
//												String[] str2 = temp_dh.split(":");
//												temp_dh = str2[1];
//											}
//											String sql = "select nprice from po_arriveorder_b where vfree1='"+temp_dh+"' and cmangid='"+temp_cinventoryid+"' and nvl(dr,0) = 0 and pk_corp='"+temp_pk_corp+"'";
//											temp_nprice = (BigDecimal)iUAPQueryBS.executeQuery(sql, new ColumnProcessor());
//											 
//										}
//										
//										//计算销售成本结转单的金额
//										BigDecimal a2 = new BigDecimal(temp_num.toString());
//										BigDecimal rest = a2.subtract(temp_nprice.doubleValue()==0 ? new BigDecimal(0) : temp_nprice );
//										//取出销售成本结转单对应的主键
//										String str = "select cbill_bid from ia_bill_b where  vfree1='"+temp_dh+"' and pk_corp='"+temp_pk_corp+"'" +
//												" and nvl(dr,0) = 0 and csourcebilltypecode='"+temp_billtypecode+"' and cinventoryid='"+temp_cinventoryid+"' " +
//														"and cfirstbillitemid='"+temp_cfirstbillbid+"'";
//										String pk_cbill_bid = (String)iUAPQueryBS.executeQuery(str, new ColumnProcessor());
//										String rstsql = "update ia_bill_b set nprice='"+temp_nprice+"',nmoney='"+rest+"' where cbill_bid = '"+pk_cbill_bid+"'";
//										
//										ICommonData idata = (ICommonData)NCLocator.getInstance().lookup(nc.itf.ia.pub.ICommonData.class.getName());
//										idata.execData(rstsql);
//									}
//								}
//							}
//							
//						}
						/****************************************add by yhj 2014-03-14 END*********************/
						
						
						break;

					} catch (RightcheckException e) {
						showErrorMessage(e.getMessage() + nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000069")/*
																																		* @res
																																		* ".\n权限校验不通过,保存失败! "
																																		*/);
						getAccreditLoginDialog().setCorpID(m_sCorpID);
						getAccreditLoginDialog().clearPassWord();
						if (getAccreditLoginDialog().showModal() == nc.ui.pub.beans.UIDialog.ID_OK) {
							String sUserID = getAccreditLoginDialog().getUserID();
							if (sUserID == null) {
								throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000070")/*
																																	* @res
																																	* "权限校验不通过,保存失败. "
																																	*/);
							} else {
								voAudit.setAccreditUserID(sUserID);
								continue;
							}
						} else {
							throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000070")/*
																																* @res
																																* "权限校验不通过,保存失败. "
																																*/);

						}
					} catch (Exception ee1) {

						BusinessException realbe = nc.ui.ic.pub.tools.GenMethod.handleException(null, null, ee1);
						if (realbe != null && realbe.getClass() == CreditNotEnoughException.class) {
							// 错误信息显示，并询问用户“是否继续？”
							int iFlag = showYesNoMessage(realbe.getMessage() + " \r\n" + "是否继续？");
							// 如果用户选择继续
							if (iFlag == nc.ui.pub.beans.MessageDialog.ID_YES) {
								voAudit.setIsCheckCredit(false);
								continue;
							} else return;
						} else if (realbe != null && realbe.getClass() == PeriodNotEnoughException.class) {
							// 错误信息显示，并询问用户“是否继续？”
							int iFlag = showYesNoMessage(realbe.getMessage() + " \r\n" + "是否继续？");
							// 如果用户选择继续
							if (iFlag == nc.ui.pub.beans.MessageDialog.ID_YES) {
								voAudit.setIsCheckPeriod(false);
								continue;
							} else return;
						} else if (realbe != null && realbe.getClass() == ATPNotEnoughException.class) {
							ATPNotEnoughException atpe = (ATPNotEnoughException) realbe;
							if (atpe.getHint() == null) {
								showErrorMessage(atpe.getMessage());
								return;
							} else {
								// 错误信息显示，并询问用户“是否继续？”
								int iFlag = showYesNoMessage(atpe.getMessage() + " \r\n" + "是否继续？");
								// 如果用户选择继续
								if (iFlag == nc.ui.pub.beans.MessageDialog.ID_YES) {
									voAudit.setIsCheckAtp(false);
									continue;
								} else {
									return;
								}
							}
						} else {
							if (realbe != null) throw realbe;
							else throw ee1;

						}
					}
				}
				// showTime(lTime, "签字");
				m_timer.showExecuteTime("平台签字时间：");
				// 返回处理
				String sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000307")/* @res "签字成功！" */;
				// ---- old ------如果是保存即签字需要刷新界面,当然，数量此时是必输项，在保存时检查了。
				// ---- old ------只要能保存，说明数量已经正确录入了。
				// -- new ---因为走平台，保存完整动作执行完后，还要重读是否签字了。
				String sBillStatus = freshStatusTs(voAudit.getHeaderVO().getPrimaryKey());

				// 设置签字时间给当前缓存vo
				m_voBill.setHeaderValue("taccounttime", ufdPre.toString());
				((GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow)).setHeaderValue("taccounttime", ufdPre.toString());

				// 判断是否是手工具,如是手工具,生成手工具仓库的其他入库单.xiaolong_fan.2012-12-25.
				SMGeneralBillVO vo = null;
				if (getParentCorpCode1().equals("10395")) {
					try {
						if (isNeedCreateBill(voAudit)) {
							while (true) {
								if (nc.ui.pub.beans.MessageDialog.ID_YES != nc.ui.pub.beans.MessageDialog.showYesNoDlg(this, null, "该单据包含手工具类存货,是否需要自动生成对应的其他入库单?(选择是将自动生成其他入库单,选否请手动生成对应的其他入库单)", MessageDialog.ID_YES)) {
									break;
								}
								if (voAudit == null || voAudit.getHeaderVO() == null || voAudit.getHeaderVO().getCbizid() == null || "".equals(voAudit.getHeaderVO().getCbizid())) {
									showErrorMessage("自动生成失败,该出库单包含手工具类存货,必须填写领料员!");
									break;
								}
								if (cargdocVO == null || cargdocVO.getCsname() == null || "".equals(cargdocVO.getCsname())) {
									showErrorMessage("自动生成失败,手工具仓(编码S01)必须有和领料员相对应的货位(货位名称为领料员编码+领料员名称)!");
									break;
								}
								vo = createBill(voAudit.getHeaderVO().getPrimaryKey());
								break;
								// GeneralBillHeaderVO headVO =
								// voAudit.getHeaderVO();
								// GeneralBillVO oldVO = (GeneralBillVO)
								// m_voBill
								// .clone();
								// String fpk =
								// vo.getHeaderVO().getPrimaryKey();
								// voAudit.getHeaderVO().setAttributeValue(
								// "vuserdef6", fpk);
								// m_voBill.getHeaderVO().setAttributeValue(
								// "vuserdef6", fpk);
								// voAudit.getHeaderVO().setAutocreatebillid(fpk);
								// m_voBill.getHeaderVO().setAutocreatebillid(fpk);
								// ArrayList alPK = (ArrayList)
								// nc.ui.pub.pf.PfUtilClient
								// .processAction("SAVE", "4D", m_sLogDate,
								// voAudit, oldVO);
								// if (alPK != null && alPK.size() >= 3
								// && alPK.get(2) != null) {
								// SMGeneralBillVO billVO = (SMGeneralBillVO)
								// alPK
								// .get(2);
								// voAudit.getParentVO().setAttributeValue(
								// "ts",
								// billVO.getParentVO().getAttributeValue(
								// "ts"));
								// m_voBill.getHeaderVO().setAttributeValue(
								// "ts",
								// billVO.getParentVO().getAttributeValue(
								// "ts"));
								// if (BillMode.List == m_iCurPanel) {
								// voAudit.getHeaderVO().setAutocreatebillid(fpk);
								// }
								// }

							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						// 删除自动生成的入库单
						// showErrorMessage("该出库单包含手工具类存货,出库时自动生成手工具仓的入库单出错,请联系系统维护人员处理!");
					}
					if (vo != null && vo.getHeaderVO() != null) {
						showWarningMessage("该出库单包含手工具类存货,自动生成其他入库单,单号:" + vo.getHeaderVO().getVbillcode());
					}
				}
				// end by xiaolong_fan.

				// // 设置新的预留字段6(记录生成的其他入库单的ID)
				// m_voBill.setHeaderValue("autocreatebillid",
				// voAudit.getHeaderVO()
				// .getAutocreatebillid());
				// ((GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow))
				// .setHeaderValue("autocreatebillid", voAudit.getHeaderVO()
				// .getAutocreatebillid());

				m_timer.showExecuteTime("freshStatusTs");
				if (sBillStatus != null && !sBillStatus.equals(BillStatus.FREE) && !sBillStatus.equals(BillStatus.DELETED)) {
					SCMEnv.out("**** signed ***");
					freshAfterSignedOK(sBillStatus);
				} else sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000308")/* @res "签字出错！" */;

				showHintMessage(sMsg);
			}
		} catch (Exception be) {
			// ###################################################
			// 签字异常，后台日志 add by hanwei 2004-6-8
			nc.ui.ic.pub.bill.GeneralBillUICtl.insertOperatelogVO(m_voBill.getHeaderVO(), nc.ui.ml.NCLangRes.getInstance().getStrByID("40080402", "UPT40080402-000013")/*
																																										* @res "签字"
																																										*/, nc.vo.scm.funcs.Businesslog.MSGERROR + "" + be.getMessage());
			// ###################################################

			handleException(be);
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000004")/* @res "签字出错。" */
					+ be.getMessage());
			showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000005")/* @res "签字出错：" */
					+ be.getMessage());
		}

	}

	private Object String(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 需生成材料入库单的存货所在行map.
	 */
	@SuppressWarnings("unchecked")
	Map itemvomap = null;

	/**
	 * 货位PK
	 */
	CargdocVO cargdocVO = null;

	/**
	 * 判断材料出库单的存货是否包含手工具,包含true,反之false.如包含手工具,则需自动生成材料入库单.
	 * xiaolong_fan.2012-12-25.
	 * 
	 * @param voAudit
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	protected boolean isNeedCreateBill(GeneralBillVO voAudit) throws Exception {

		if (m_sBillTypeCode == null || !"4D".equals(m_sBillTypeCode)) { return false; }
		String bziid = voAudit.getHeaderVO().getCbizid();

		String cargdocSql = "select a.* from bd_cargdoc a, bd_stordoc b,bd_psndoc c where a.pk_stordoc = b.pk_stordoc and c.psncode||c.psnname = a.csname and b.storcode = 'S01' and c.pk_psndoc='" + bziid + "' and c.dr=0";
		Object obj = null;
		obj = getUAPQuery().executeQuery(cargdocSql, new BeanListProcessor(CargdocVO.class));
		cargdocVO = null;
		if (obj == null) {
			// showErrorMessage("该单据签字涉及手工具仓的货位,请确认该出库单的领料员及领料员对应的手工具仓的货位是否存在!");
			// throw new
			// Exception("该单据签字涉及手工具仓的货位,请确认该出库单的领料员及领料员对应的手工具仓的货位是否存在!");
		} else if (obj != null && obj instanceof ArrayList) {
			ArrayList list = (ArrayList) obj;
			cargdocVO = ((list != null && list.size() > 0) ? (CargdocVO) list.get(0) : null);
		}
		// 仓库ID
		String pk_calbody = null;
		if (voAudit.getHeaderVO() instanceof GeneralBillHeaderVO) {
			GeneralBillHeaderVO generalBillHeaderVO = voAudit.getHeaderVO();
			pk_calbody = generalBillHeaderVO.getPk_calbody();
		}
		itemvomap = new HashMap();
		CircularlyAccessibleValueObject[] vos = voAudit.getChildrenVO();
		for (int i = 0; i < vos.length; i++) {
			if (vos[i] instanceof GeneralBillItemVO) {
				GeneralBillItemVO itemvo = (GeneralBillItemVO) vos[i];
				String invmanid = itemvo.getCinvmanid();
				obj = getUAPQuery().executeQuery("select materclass from  bd_produce  where dr=0 and pk_calbody='" + pk_calbody + "' and pk_invbasdoc='" + invmanid + "'", new ColumnProcessor());
				String materclass = obj != null ? String.valueOf(obj) : null;
				if (obj != null && "1".equals(materclass)) {// 1对应物料分类为工具.
					itemvomap.put(i, itemvo);
				}
			}
		}
		if (itemvomap != null && itemvomap.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 自动生成材料入库单. xiaolong_fan.2012-12-25.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	protected SMGeneralBillVO createBill(String pk) throws Exception {
		// vomap不为空,则需生成其他入库单.
		if (itemvomap == null || itemvomap.size() <= 0) return null;
		GeneralBillVO billVO = new GeneralBillVO();
		billVO.setParentVO(createBillHeaderVO(pk));
		billVO.getHeaderVO().setStatus(2);
		List<GeneralBillItemVO> itemvolist = new ArrayList();
		Iterator it = itemvomap.values().iterator();
		while (it.hasNext()) {
			GeneralBillItemVO itemvo = (GeneralBillItemVO) it.next();
			GeneralBillItemVO tempvo = new GeneralBillItemVO();

			// 库位
			if (cargdocVO != null) {
				LocatorVO voSpace = new LocatorVO();
				LocatorVO[] lvos = new LocatorVO[1];
				voSpace.setCspaceid(cargdocVO.getPk_cargdoc());
				voSpace.setVspacename(cargdocVO.getCsname());
				voSpace.setNinspacenum(itemvo.getNoutnum());
				lvos[0] = voSpace;
				tempvo.setLocator(lvos);
				tempvo.setLocStatus(VOStatus.NEW);
			}
			tempvo.setCinvbasid(itemvo.getCinvbasid());
			tempvo.setCinvmanid(itemvo.getCinvmanid());
			tempvo.setCinventoryid(itemvo.getCinventoryid());
			// 其他入库单的实入数量为材料出库单的实出数量.
			tempvo.setNinnum(itemvo.getNoutnum());
			// 单价
			tempvo.setNprice(itemvo.getNprice());
			// 金额
			tempvo.setNmny(itemvo.getNmny());
			tempvo.setBarcodeClose(new UFBoolean(false));
			tempvo.setCrowno(itemvo.getCrowno());
			tempvo.setFchecked(0);
			tempvo.setFlargess(new UFBoolean(false));
			tempvo.setDesaType(0);
			tempvo.setIsok(new UFBoolean(false));
			tempvo.setNbarcodenum(new UFDouble(0));
			String[] strs = new String[] {
					"bonroadflag", "breturnprofit", "bsafeprice", "bsourcelargess", "bzgflag"
			};
			for (int i = 0; i < strs.length; i++) {
				tempvo.setAttributeValue(strs[i], new UFBoolean(false));
			}
			tempvo.setBarcodeClose(new UFBoolean(false));
			tempvo.setDbizdate(ClientEnvironment.getInstance().getDate());
			tempvo.setStatus(2);

			itemvolist.add(tempvo);
		}
		billVO.setChildrenVO(itemvolist.toArray(new GeneralBillItemVO[itemvolist.size()]));

		ArrayList alPK = null;
		try {
			alPK = (ArrayList) nc.ui.pub.pf.PfUtilClient.processAction("SAVE", "4A", m_sLogDate, billVO);
		} catch (Exception e) {
			e.printStackTrace();
			showErrorMessage("自动生成手工具仓的入库单出错,请手动生成或联系系统维护人员处理!");
			throw new Exception("自动生成手工具仓的入库单出错,请手动生成或联系系统维护人员处理!");
		}
		if (alPK != null && alPK.size() >= 3 && alPK.get(2) != null) {
			SMGeneralBillVO vo = (SMGeneralBillVO) alPK.get(2);
			billVO.getHeaderVO().setTs(vo.getHeaderVO().getTs());
			billVO.getHeaderVO().setPrimaryKey(vo.getHeaderVO().getPrimaryKey());
			for (int i = 0; i < billVO.getChildrenVO().length; i++) {
				billVO.getChildrenVO()[i].setPrimaryKey(vo.getChildrenVO()[i].getPrimaryKey());
			}
			UFTime ufdPre1 = new UFTime(System.currentTimeMillis());// 系统当前时间
			UFDateTime ufdPre = new UFDateTime(m_sLogDate + " " + ufdPre1.toString());
			billVO = getAuditVO(billVO, ufdPre);
			try {
				nc.ui.pub.pf.PfUtilClient.processBatch("SIGN", "4A", m_sLogDate, new GeneralBillVO[] {
					billVO
				});
			} catch (Exception e) {
				e.printStackTrace();
				showErrorMessage("该出库单包含手工具类存货,出库时自动生成手工具仓的入库单:" + vo.getHeaderVO().getVbillcode() + "签字出错,请手动签字或联系系统维护人员处理!");
				throw new Exception("该出库单包含手工具类存货,出库时自动生成手工具仓的入库单:" + vo.getHeaderVO().getVbillcode() + "签字出错,请手动签字或联系系统维护人员处理!");
			}
			return vo;
		}
		return null;
	}

	/**
	 * 创建材料入库单的表头VO.xiaolong_fan.
	 * 
	 * @return
	 */
	protected GeneralBillHeaderVO createBillHeaderVO(String pk) {
		GeneralBillHeaderVO billHeaderVO = new GeneralBillHeaderVO();
		billHeaderVO.setCbilltypecode("4A");
		billHeaderVO.setCgeneralhid("");
		billHeaderVO.setCgeneralhid("");
		billHeaderVO.setAttributeValue("autocreatebillid", pk);
		billHeaderVO.setAutocreatebillid(pk);
		billHeaderVO.setCoperatorid("");
		billHeaderVO.setCoperatorname("");
		Object obj = null;
		try {
			obj = getUAPQuery().executeQuery("select * from bd_stordoc where dr= 0 and storcode='S01'", new BeanProcessor(StordocVO.class));
		} catch (FTSBusinessException e) {
			e.printStackTrace();
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		if (obj != null && obj instanceof StordocVO) {
			StordocVO vo = (StordocVO) obj;
			// 仓库ID
			billHeaderVO.setCwarehouseid(vo.getPrimaryKey());
			// 库存组织PK
			billHeaderVO.setPk_calbody(vo.getPk_calbody());
		}
		billHeaderVO.setCwarehousename("");
		// 库管员ID
		// billHeaderVO.setCwhsmanagerid("1016A21000000000747Q");

		billHeaderVO.setCwhsmanagername("");
		billHeaderVO.setDbilldate(ClientEnvironment.getInstance().getDate());
		billHeaderVO.setFbillflag(2);
		billHeaderVO.setFreplenishflag(new UFBoolean(false));
		billHeaderVO.setIsLocatorMgt(1);
		billHeaderVO.setIsWasteWh(0);
		billHeaderVO.setIscalculatedinvcost(new UFBoolean(false));
		billHeaderVO.setIsdirectstore(new UFBoolean(false));
		billHeaderVO.setIsforeignstor(new UFBoolean(false));
		billHeaderVO.setIsgathersettle(new UFBoolean(false));
		billHeaderVO.setPk_corp(m_sCorpID);
		// billHeaderVO.settlastmoditime
		// billHeaderVO.settmaketime
		billHeaderVO.setVcalbodyname("");
		// Timestamp time = new Timestamp(System.currentTimeMillis());
		// DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// billHeaderVO.setTs(df.format(time));
		return billHeaderVO;
	}

	/**
	 * @功能:设置货位的值
	 * @author ：林桂莹
	 * @2012/9/5
	 * 
	 * @since v50
	 */
	@SuppressWarnings("unchecked")
	public void setSpace(String cspaceid, String vspacename, int i) {
		getBillCardPanel().setBodyValueAt(cspaceid, i, "cspaceid");
		getBillCardPanel().setBodyValueAt(vspacename, i, "vspacename");
		LocatorVO voSpace = new LocatorVO();
		LocatorVO[] lvos = new LocatorVO[1];
		lvos[0] = voSpace;
		voSpace.setCspaceid(cspaceid);
		voSpace.setVspacename(vspacename);

		m_alLocatorData.remove(i);
		m_alLocatorData.add(i, lvos);
		UFDouble assistnum = null;
		try {
			assistnum = (UFDouble) getBillCardPanel().getBodyValueAt(i, m_sAstItemKey);
		} catch (Exception e4) {
		}
		UFDouble num = null;
		try {
			num = (UFDouble) getBillCardPanel().getBodyValueAt(i, m_sNumItemKey);
		} catch (Exception e2) {
		}
		UFDouble ngrossnum = null;
		try {
			ngrossnum = (UFDouble) getBillCardPanel().getBodyValueAt(i, m_sNgrossnum);
		} catch (Exception e3) {
		}

		LocatorVO[] voLoc = (LocatorVO[]) m_alLocatorData.get(i);

		if (voLoc != null && voLoc.length == 1) {

			// 辅数量
			if (assistnum == null) {
				voLoc[0].setNinspaceassistnum(assistnum);
				voLoc[0].setNoutspaceassistnum(assistnum);
			} else {
				if (m_iBillInOutFlag > 0) {
					voLoc[0].setNinspaceassistnum(assistnum);
					voLoc[0].setNoutspaceassistnum(null);
				} else {
					voLoc[0].setNinspaceassistnum(null);
					voLoc[0].setNoutspaceassistnum(assistnum);
				}
			}
			// 数量
			if (num == null) {
				voLoc[0].setNinspacenum(num);
				voLoc[0].setNoutspacenum(num);
				if (m_alSerialData != null) m_alSerialData.set(i, null);
			} else {
				if (m_iBillInOutFlag > 0) {
					// 入库
					voLoc[0].setNoutspacenum(null);
					voLoc[0].setNinspacenum(num);
				} else {
					// 出库
					voLoc[0].setNinspacenum(null);
					voLoc[0].setNoutspacenum(num);
					if (m_alSerialData != null) m_alSerialData.set(i, null);
				}
			}
			// 毛重
			if (ngrossnum == null) {
				voLoc[0].setNingrossnum(ngrossnum);
				voLoc[0].setNoutgrossnum(ngrossnum);
				if (m_alSerialData != null) m_alSerialData.set(i, null);
			} else {
				if (m_iBillInOutFlag > 0) {
					// 入库
					voLoc[0].setNoutgrossnum(null);
					voLoc[0].setNingrossnum(ngrossnum);
				} else {
					// 出库
					voLoc[0].setNingrossnum(null);
					voLoc[0].setNoutgrossnum(ngrossnum);

				}

			}

		} else m_alLocatorData.set(i, null);

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
		if (uapQueryBS == null) try {
			uapQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		} catch (ComponentException e) {
			throw new FTSBusinessException("IUAPQueryBS not found!");
		}
		return uapQueryBS;
	}

	/**
	 * 子类实现该方法，响应按钮事件。
	 * 
	 * @version (00-6-1 10:32:59)
	 * 
	 * @param bo
	 *            ButtonObject
	 */
	@SuppressWarnings("restriction")
	public void onButtonClicked(nc.ui.pub.ButtonObject bo) {
		// getBillCardPanel().tableStopCellEditing();
		getBillCardPanel().stopEditing();//停止编辑

		// 清除定位，2003-07-21 ydy
		clearOrientColor();

		// 清状态条显示
		// showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
		// "SCMCOMMON", "UPPSCMCommon-000133")/* @res "就绪" */);
		//		
		showHintMessage(bo.getName());
		// 父菜单是<新增>
		// 没有单据参照的话，新增在单据编辑菜单，否则在业务类型后。
		if (m_bNeedBillRef) {
			if (bo.getParent() == getButtonTree().getButton(ICButtonConst.BTN_ADD)) {

				onBizType(bo);   //增加
				getBillCardPanel().transferFocusTo(nc.ui.pub.bill.BillCardPanel.HEAD);
			} else if (bo.getParent() == getButtonTree().getButton(ICButtonConst.BTN_BUSINESS_TYPE)) {
				onJointAdd(bo);     //业务类型
				getBillCardPanel().transferFocusTo(nc.ui.pub.bill.BillCardPanel.HEAD);
			}
		} else if (bo == getButtonTree().getButton(ICButtonConst.BTN_ADD) || bo == getButtonTree().getButton(ICButtonConst.BTN_BILL_ADD_MANUAL)) {
			onAdd();    //自制增加
			getBillCardPanel().transferFocusTo(nc.ui.pub.bill.BillCardPanel.HEAD);
		}

		if (bo == getButtonTree().getButton(ICButtonConst.BTN_LINE_ADD)) {
			onAddLine();   //增行

		} else if (bo == getButtonTree().getButton(ICButtonConst.BTN_LINE_DELETE)) onDeleteRow();  //删行
		else if (bo == getButtonTree().getButton(ICButtonConst.BTN_LINE_COPY)) {

			onCopyLine();   //复制行

		} else if (bo == getButtonTree().getButton(ICButtonConst.BTN_LINE_PASTE)) {
			onPasteLine();  //粘贴行

		} else if (bo == getButtonTree().getButton(ICButtonConst.BTN_LINE_INSERT)) {
			onInsertLine();   //插入行

		} else if (bo == getButtonTree().getButton(ICButtonConst.BTN_SAVE)) {
			setResIdIfNeed();    
			boolean ok = onSave();  //保存
			if (ok) saveResId();
		} else if (bo == getButtonTree().getButton(ICButtonConst.BTN_BILL_DELETE)) onDelete();  //删除
		else if (bo == getButtonTree().getButton(ICButtonConst.BTN_QUERY)) onQuery(true);   //查询
		else if (bo == getButtonTree().getButton(ICButtonConst.BTN_BROWSE_REFRESH)) onQuery(false);  //刷新
		else if (bo == getButtonTree().getButton(ICButtonConst.BTN_ASSIST_QUERY_RELATED)) onJointCheck();   //联查
		else if (bo == getButtonTree().getButton(ICButtonConst.BTN_BILL_EDIT)) {
			onUpdate();   //修改
			getBillCardPanel().transferFocusTo(nc.ui.pub.bill.BillCardPanel.HEAD);

		} else if (bo == getButtonTree().getButton(ICButtonConst.BTN_BILL_CANCEL)) onCancel();  //取消
		else if (bo == getButtonTree().getButton(ICButtonConst.BTN_SWITCH)) onSwitch();  //切换卡片显示/列表显示
		else if (bo == getButtonTree().getButton(ICButtonConst.BTN_EXECUTE_SIGN_CANCEL)) onCancelAudit();  //取消签字
		else if (bo == getButtonTree().getButton(ICButtonConst.BTN_SIGN)) onAudit();  //签字
		else if (bo == getButtonTree().getButton(ICButtonConst.BTN_BILL_COPY)) onCopy();  //复制
		else if (bo == getButtonTree().getButton(ICButtonConst.BTN_BROWSE_LOCATE)) onLocate();  //定位
		else if (bo == getButtonTree().getButton(ICButtonConst.BTN_LINE_SPACE)) onSpaceAssign();  //货位
		else if (bo == getButtonTree().getButton(ICButtonConst.BTN_LINE_SERIAL)) onSNAssign();    //序列号
		else if (bo == getButtonTree().getButton(ICButtonConst.BTN_ASSIST_QUERY_ONHAND)) onRowQuyQty();  //存量查询
		else if (bo == getButtonTree().getButton(ICButtonConst.BTN_ASSIST_QUERY_SUITE)) onSetpart();   //成套件信息
		else if (bo == getButtonTree().getButton(ICButtonConst.BTN_ASSIST_FUNC_ONHAND)) onOnHandShowHidden();  //存量显示/隐藏
		else if (bo == getButtonTree().getButton(ICButtonConst.BTN_PRINT_PRINT)) onPrint();  //打印
		else if (bo == getButtonTree().getButton(ICButtonConst.BTN_PRINT_PREVIEW)) onPreview();  //预览
		else if (bo == getButtonTree().getButton(ICButtonConst.BTN_PRINT_SUM)) onPrintSumRow();   //汇总打印

		else if (bo == getButtonTree().getButton(ICButtonConst.BTN_PRINT_SPACE)) onPrintLocSN(getWholeBill(m_iLastSelListHeadRow));  //打印货位
		else if (bo == m_pageBtn.getFirst()) onFirst();   //首页
		else if (bo == m_pageBtn.getPre()) onPrevious();  //上页
		else if (bo == m_pageBtn.getNext()) onNext();   //下页
		else if (bo == m_pageBtn.getLast()) onLast();   //末页
		if (bo == getButtonTree().getButton(ICButtonConst.BTN_ASSIST_FUNC_INV_CHECK)) onCheckData();  //核对存货
		else if (bo == getButtonTree().getButton(ICButtonConst.BTN_IMPORT_BILL)) onImportData();  //导入单据

		// 导入主次条码
		else if (bo == getButtonTree().getButton(ICButtonConst.BTN_IMPORT_BOTH_BARCODE))
		// onImportBarcodeAndSubFileData();
		onImptBCExcel(2);  //导入主次条码
		// 导入主
		else if (bo == getButtonTree().getButton(ICButtonConst.BTN_IMPORT_1ST_BARCODE)) onImptBCExcel(0);  //导入主条码
		// 导入次
		else if (bo == getButtonTree().getButton(ICButtonConst.BTN_IMPORT_2ND_BARCODE)) onImptBCExcel(1);  //导入次条码
		// 导入来源单据条码
		else if (bo == getButtonTree().getButton(ICButtonConst.BTN_IMPORT_SOURCE_BARCODE)) onImportBarcodeSourceBill();  //导入来源单据条码
		// 条码关闭
		else if (bo == getButtonTree().getButton(ICButtonConst.BTN_EXECUTE_BARCODE_CLOSE)) {
			onBarcodeOpenClose(0);   //条码关闭
		} else if (bo == getButtonTree().getButton(ICButtonConst.BTN_EXECUTE_BARCODE_OPEN)) {
			onBarcodeOpenClose(1);   //条码打开
		} else if (bo == getButtonTree().getButton(ICButtonConst.BTN_LINE_BARCODE)) {   //条形码
			onBarCodeEdit();
		} else if (bo == getButtonTree().getButton(ICButtonConst.BTN_ASSIST_FUNC_DOCUMENT)) onDocument();   //文档管理
		// else if (bo == m_boSelectLocator)
		// onSelLoc();
		// 由应发（收）自动填写实发（收）
		else if (bo == getButtonTree().getButton(ICButtonConst.BTN_LINE_AUTO_FILL)) onFillNum();   //自动取数
		else if (bo == getButtonTree().getButton(ICButtonConst.BTN_ASSIST_FUNC_PICKUP_AUTO)) onPickAuto();   //自动拣货
		else if (bo == getButtonTree().getButton(ICButtonConst.BTN_ASSIST_FUNC_REFER_IN)) onRefInICBill();   //参照入库单
		// 单据导出

		else if (bo == getButtonTree().getButton(ICButtonConst.BTN_EXPORT_TO_DIRECTORY)) onBillExcel(1);// 导出到指定目录
		else if (bo == getButtonTree().getButton(ICButtonConst.BTN_EXPORT_TO_XML)) // 导出到XML
		onBillExcel(3);
		else if (bo == getButtonTree().getButton(ICButtonConst.BTN_PRINT_DISTINCT)) {
			onMergeShow();   //合并显示
		} else if (m_funcExtend != null) {
			// 支持功能扩展
			if (BillMode.Card == m_iCurPanel) m_funcExtend.doAction(bo, this, getBillCardPanel(), getBillListPanel(), nc.ui.scm.extend.IFuncExtend.CARD);
			else if (BillMode.List == m_iCurPanel) m_funcExtend.doAction(bo, this, getBillCardPanel(), getBillListPanel(), nc.ui.scm.extend.IFuncExtend.LIST);
		} else {
			onExtendBtnsClick(bo);
		}
		//add by shikun 2014-04-08 新增按钮下的子按钮操作后动作：去除表体隔离数量
		if (bo.getParent() == getButtonTree().getButton(ICButtonConst.BTN_ADD)) {  //增加
//			subGLSL();
		}
		//end shikun 
	}

	private void setResIdIfNeed() {
		String moduleCode = getModuleCode();
		GeneralBillVO billVO = getBillVO();
		@SuppressWarnings("unused")
		String cbilltypecode = billVO.getHeaderVO().getCbilltypecode();
		@SuppressWarnings("unused")
		Object wwddid = billVO.getHeaderVO().getAttributeValue("pk_defdoc6");// 委外订单id
		try {
			GeneralBillItemVO items[] = (GeneralBillItemVO[]) billVO.getChildrenVO();
			for (int i = 0; i < items.length; i++) {
				@SuppressWarnings("unused")
				String bat = items[i].getVbatchcode();
				@SuppressWarnings("unused")
				String invid2 = items[i].getCinventoryid();
				@SuppressWarnings("unused")
				String invid = items[i].getCinvbasid();
				@SuppressWarnings("unused")
				String resid = items[i].getVuserdef1();// 资源号
				Object bcuser1 = items[i].getAttributeValue("vbcuser1");
				@SuppressWarnings("unused")
				String processBat = (String) items[i].getAttributeValue("pk_defdoc3");// 加工批次
				@SuppressWarnings("unused")
				String def6 = (String) items[i].getAttributeValue("pk_defdoc6");// 加工批次带出的资源号
				String def7 = (String) items[i].getAttributeValue("pk_defdoc7");// 委外发出批次号带出的资源号
				if (bcuser1 != null) {
					items[i].setVuserdef1((String) bcuser1);
					getBillCardPanel().getBillModel().setValueAt(bcuser1, i, "vuserdef1");
					getBillCardPanel().getBillModel().setRowState(i, BillModel.MODIFICATION);
				} else if ("800507".equals(moduleCode)) {
					String resNo = null;
					resNo = (def7 == null ? null : def7.trim());
					items[i].setVuserdef1(resNo);
					getBillCardPanel().getBillModel().setValueAt(resNo, i, "vuserdef1");
					getBillCardPanel().getBillModel().setRowState(i, BillModel.MODIFICATION);
					continue;
				}
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 采购入库资源号更新到批次号档案中
	 */
	private void saveResId() {
		GeneralBillVO billVO = getBillVO();
		String cbilltypecode = billVO.getHeaderVO().getCbilltypecode();
		Object wwddid = billVO.getHeaderVO().getAttributeValue("pk_defdoc6");// 委外订单id
		if (cbilltypecode.equals("45") || (cbilltypecode.equals("46") && wwddid != null && !wwddid.equals(""))) {
			try {
				GeneralBillItemVO items[] = (GeneralBillItemVO[]) billVO.getChildrenVO();
				boolean updated = false;
				for (int i = 0; i < items.length; i++) {
					String bat = items[i].getVbatchcode();
					@SuppressWarnings("unused")
					String invid2 = items[i].getCinventoryid();
					String invid = items[i].getCinvbasid();
					String resid = items[i].getVuserdef1();
					if (bat == null || invid == null || resid == null) continue;
					nc.vo.baoyin.ic.BatchcodeVO[] baoyinbatvos = (nc.vo.baoyin.ic.BatchcodeVO[]) HYPubBO_Client.queryByCondition(nc.vo.baoyin.ic.BatchcodeVO.class, "pk_invbasdoc='" + invid + "' and vbatchcode='" + bat + "'");
					if (baoyinbatvos != null && baoyinbatvos.length > 0 && baoyinbatvos[0] != null) {
						String dbvdef1 = baoyinbatvos[0].getVdef1();
						if (dbvdef1 == null && resid != null)// !dbvdef1.equals(resid)
						{
							baoyinbatvos[0].setVdef1(resid);
							HYPubBO_Client.update(baoyinbatvos[0]);
							updated = true;
						}
					}
					/*
					 * Object def1 =PublicUtil.getColValue(
					 * "select vdef1 from scm_batchcode where pk_invbasdoc='"
					 * +invid+"' and vbatchcode='"+bat+"'"); if(def1==null) {
					 * 
					 * ConditionVO cons[] = new ConditionVO[2]; cons[0] = new
					 * ConditionVO(); cons[0].setFieldCode("vbatchcode");
					 * cons[0].setOperaCode("="); cons[0].setValue(bat);
					 * 
					 * cons[1] = new ConditionVO();
					 * cons[1].setFieldCode("pk_invbasdoc");
					 * cons[1].setOperaCode("="); cons[1].setValue(invid2);
					 * BatchcodeVO[] batchcodeVOs =
					 * BatchcodeHelper.queryBatchcode(cons, Util.getCorp());
					 * if(batchcodeVOs!=null && batchcodeVOs.length>0) {
					 * batchcodeVOs[0].setVdef1(resid);
					 * BatchcodeHelper.saveBatchcode(batchcodeVOs,cl); } }
					 */
				}
				if (updated) onQuery(false);// 刷新
			} catch (Exception e) {
			}
		}
	}

	@SuppressWarnings("restriction")
	protected void onInsertLine() {
		getBillCardPanel().insertLine();
		nc.ui.scm.pub.report.BillRowNo.insertLineRowNo(getBillCardPanel(), m_sBillTypeCode, m_sBillRowNo);

	}

	@SuppressWarnings("restriction")
	protected void onPasteLine() {

		if (m_voaBillItem == null || m_voaBillItem.length <= 0) return;

		try {
			getBillCardPanel().getBillModel().setNeedCalculate(false);
			int iRowCount = getBillCardPanel().getBodyPanel().getTableModel().getRowCount();
			getBillCardPanel().pasteLine();
			int istartrow = getBillCardPanel().getBillTable().getSelectedRow() - m_voaBillItem.length;
			for (int i = 0; i < m_voaBillItem.length; i++)
				getBillCardPanel().getBillModel().setBodyRowVO((GeneralBillItemVO) m_voaBillItem[i].clone(), istartrow + i);

			// 增加的行数
			iRowCount = getBillCardPanel().getBodyPanel().getTableModel().getRowCount() - iRowCount;
			nc.ui.scm.pub.report.BillRowNo.pasteLineRowNo(getBillCardPanel(), m_sBillTypeCode, m_sBillRowNo, iRowCount);
			//
			voBillPastLine();
		} finally {
			getBillCardPanel().getBillModel().setNeedCalculate(true);
		}
	}

	protected void onCopyLine() {
		getBillCardPanel().copyLine();
		voBillCopyLine();

	}

	@SuppressWarnings("restriction")
	protected void onAddLine() {
		getBillCardPanel().addLine();
		nc.ui.scm.pub.report.BillRowNo.addLineRowNo(getBillCardPanel(), m_sBillTypeCode, m_sBillRowNo);
	}

	@SuppressWarnings("restriction")
	public void onCancel() {

		switch (nc.ui.pub.beans.MessageDialog.showYesNoDlg(this, null, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH067")/*
																																		 * @res
																																		 * "是否确定要取消？"
																																		 */
		, MessageDialog.ID_NO)) {

		case nc.ui.pub.beans.MessageDialog.ID_YES:
			break;
		default:
			return;
		}
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH045")/* @res "正在取消" */);

		// v5 lj 支持参照生成多张单据
		if (m_iCurPanel == BillMode.List) {
			if (m_bRefBills == true) {
				m_iMode = BillMode.Browse;
				setRefBillsFlag(false);
				setDataOnList(null, false);
				setRefBtnStatus();
				return;
			}
		}
		if (m_iCurPanel == BillMode.Card) {
			if (m_bRefBills == true) {
				onSwitch();
				return;
			}
		}
		// end

		if (m_iMode != BillMode.Browse) {

			// when edit, cancel should re-write the excel'file status
			if (m_iMode == BillMode.Update) {
				OffLineCtrl ctrl = new OffLineCtrl(this);
				ctrl.cancelEdit(m_voBill);
			}
			if (m_voBill != null) m_utfBarCode.setRemoveBarcode(m_voBill.getItemVOs());
			getBillCardPanel().setEnabled(false);
			m_iMode = BillMode.Browse;
			getBillCardPanel().resumeValue();
			// 恢复货位数据
			m_alLocatorData = m_alLocatorDataBackup;
			// 清备份数据
			m_alLocatorDataBackup = null;

			// 恢复序列号数据
			m_alSerialData = m_alSerialDataBackup;
			// 清序列号备份数据
			m_alSerialDataBackup = null;
			// 恢复billvo
			if (m_iLastSelListHeadRow >= 0 && m_alListData != null && m_alListData.size() > m_iLastSelListHeadRow && m_alListData.get(m_iLastSelListHeadRow) != null) {
				m_voBill = (GeneralBillVO) ((GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow)).clone();
				setBillVO(m_voBill);
				// resumeValue恢复错误，刷新表尾显示
				setTailValue(0);
			} else {
				// 否则情况表单界面！
				GeneralBillVO voNullBill = new GeneralBillVO();
				voNullBill.setParentVO(new GeneralBillHeaderVO());
				voNullBill.setChildrenVO(new GeneralBillItemVO[] {
					new GeneralBillItemVO()
				});
				getBillCardPanel().setBillValueVO(voNullBill);
				getBillCardPanel().getBillModel().clearBodyData();
			}
			if (m_voBill != null) m_utfBarCode.setAddBarcodes(m_voBill.getItemVOs());
			setButtonStatus(false);
			// 根据是否为来源单据控制单据界面
			ctrlSourceBillUI(true);

		}
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH008")/*
																						* @res "取消成功"
																						*/);
	}

	/**
	 * 李俊 显示或者隐藏现存量面板
	 * 
	 */
	protected void onOnHandShowHidden() {

		if (m_iCurPanel == BillMode.List) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000065")/* @res "请切换到卡片界面！" */);
			return;
		}

		m_bOnhandShowHidden = !m_bOnhandShowHidden;
		// if (m_bOnhandShowHidden)
		// m_boOnHandShowHidden.setName("隐藏存量框");
		// else
		// m_boOnHandShowHidden.setName("显示存量框");
		//        
		// updateButton(m_boOnHandShowHidden);

		if (m_bOnhandShowHidden) {
			m_sMultiMode = MultiCardMode.CARD_TAB;
		} else m_sMultiMode = MultiCardMode.CARD_PURE;

		m_layoutManager.show();

	}

	/**
	 * 创建者：yb 功能：删除处理 参数： 返回： 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	public String checkBillForCancelAudit(GeneralBillVO vobill) {
		if (vobill == null) return null;
		if (vobill.getHeaderVO().getFbillflag() != null && vobill.getHeaderVO().getFbillflag().intValue() != BillStatus.ISIGNED) return "单据[" + vobill.getHeaderVO().getVbillcode() + "]不是签字状态，不能取消签字！";
		return null;
	}

	/**
	 * 创建者：王乃军 功能：签字 参数： 返回： 例外： 日期：(2001-5-9 9:23:32)
	 * 
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 */
	public GeneralBillVO getCancelAuditVO(GeneralBillVO voAudit) {

		GeneralBillHeaderVO voHead = voAudit.getHeaderVO();
		// 签字人
		// voHead.setCregister(m_sUserID);
		// 可以不是当前登录单位的单据，所以不需要修改单据。
		// voHead.setPk_corp(m_sCorpID);
		// 该日期设置之后会造成库存月结取消签字未扣减数量
		// voHead.setDaccountdate(new nc.vo.pub.lang.UFDate(m_sLogDate));
		// vo可能要传给平台，所以要做成签字后的单据
		voHead.setFbillflag(new Integer(nc.vo.ic.pub.bill.BillStatus.SIGNED));
		voHead.setCoperatoridnow(m_sUserID); // 当前操作员2002-04-10.wnj
		voHead.setAttributeValue("clogdatenow", m_sLogDate); // 当前登录日期2003-01-05
		voAudit.setParentVO(voHead);

		// 平台：需要表体带表头PK
		GeneralBillItemVO voaItem[] = voAudit.getItemVOs();
		// 表体行数
		int iRowCount = voAudit.getItemCount();

		for (int i = 0; i < iRowCount; i++) {
			// 表头PK
			voaItem[i].setCgeneralhid(voHead.getPrimaryKey());

		}
		voAudit.setChildrenVO(voaItem);

		voAudit.setStatus(nc.vo.pub.VOStatus.UNCHANGED);

		voAudit.setIsCheckCredit(true);
		voAudit.setIsCheckPeriod(true);
		voAudit.setIsCheckAtp(true);

		voAudit.m_iActionInt = nc.vo.scm.pub.bill.CreditConst.ICREDIT_ACT_OTHER;
		voAudit.m_sActionCode = "CANCELSIGN";

		return voAudit;

	}

	/**
	 * 创建者：王乃军 功能：取消记账 参数： 返回： 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	public void onCancelAudit() {
		try {
			switch (nc.ui.pub.beans.MessageDialog.showYesNoDlg(this, null, ResBase.getIsCancleSign()/* @res "是否确定要取消签字？" */
			, MessageDialog.ID_NO)) {

			case nc.ui.pub.beans.MessageDialog.ID_YES:
				break;
			default:
				return;
			}
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000006")/*
																											* @res "正在取消签字，请稍候..."
																											*/);

			if (BillMode.List == m_iCurPanel && getBillListPanel().getHeadTable().getSelectedRowCount() > 1) {
				onBatchAction(ICConst.CANCELSIGN);
				return;
			}
			// 设置m_voBill,以读取控制数据。
			if (m_iLastSelListHeadRow >= 0 && m_alListData != null && m_alListData.size() > m_iLastSelListHeadRow && m_alListData.get(m_iLastSelListHeadRow) != null)
			// 这里不能clone(),修改m_voBill同时修改list.
			m_voBill = (GeneralBillVO) ((GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow)).clone();

			//取消签字时,判断是否存在自动生成的其他入库单.xiaolong_fan.
			if (existBill(m_voBill)) { return; }
			//end by xiaolong_fan.
			if (m_voBill != null) {
				// 设置条码状态未没有被修改
				setBillBCVOStatus(m_voBill, nc.vo.pub.VOStatus.UNCHANGED);
				// 支持平台，clone一个，以便于以后的处理，同时防止修改了m_voBill
				GeneralBillVO voAudit = (GeneralBillVO) m_voBill.clone();
				voAudit = getCancelAuditVO(voAudit);
				long lTime = System.currentTimeMillis();
				// 返回值
				ArrayList alRet = null;

				while (true) {
					try {

						alRet = (ArrayList) nc.ui.pub.pf.PfUtilClient.processAction("CANCELSIGN", m_sBillTypeCode, m_sLogDate, voAudit);
						break;

					} catch (Exception ee1) {

						BusinessException realbe = nc.ui.ic.pub.tools.GenMethod.handleException(null, null, ee1);
						if (realbe != null && realbe.getClass() == CreditNotEnoughException.class) {
							// 错误信息显示，并询问用户“是否继续？”
							int iFlag = showYesNoMessage(realbe.getMessage() + " \r\n" + "是否继续？");
							// 如果用户选择继续
							if (iFlag == nc.ui.pub.beans.MessageDialog.ID_YES) {
								voAudit.setIsCheckCredit(false);
								continue;
							} else return;
						} else if (realbe != null && realbe.getClass() == PeriodNotEnoughException.class) {
							// 错误信息显示，并询问用户“是否继续？”
							int iFlag = showYesNoMessage(realbe.getMessage() + " \r\n" + "是否继续？");
							// 如果用户选择继续
							if (iFlag == nc.ui.pub.beans.MessageDialog.ID_YES) {
								voAudit.setIsCheckPeriod(false);
								continue;
							} else return;
						} else if (realbe != null && realbe.getClass() == ATPNotEnoughException.class) {
							ATPNotEnoughException atpe = (ATPNotEnoughException) realbe;
							if (atpe.getHint() == null) {
								showErrorMessage(atpe.getMessage());
								return;
							} else {
								// 错误信息显示，并询问用户“是否继续？”
								int iFlag = showYesNoMessage(atpe.getMessage() + " \r\n" + "是否继续？");
								// 如果用户选择继续
								if (iFlag == nc.ui.pub.beans.MessageDialog.ID_YES) {
									voAudit.setIsCheckAtp(false);
									continue;
								} else {
									return;
								}
							}
						} else {

							if (realbe != null) throw realbe;
							else throw ee1;
						}
					}
				}

				showTime(lTime, "取消签字");
				// 返回处理
				String sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000309")/* @res "取消签字出错！" */;
				if (alRet != null && alRet.size() > 0 && alRet.get(0) != null) {
					Boolean bOK = (Boolean) alRet.get(0);
					if (bOK != null && bOK.booleanValue()) {
						int curmode = m_iCurPanel;
						freshAfterCancelSignedOK();
						if (curmode == BillMode.Card) onSwitch();
						sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000310")/*
																											* @res
																											* "取消签字成功！"
																											*/;
					}
				}
				// ---- old ------如果是保存即签字需要刷新界面,当然，数量此时是必输项，在保存时检查了。
				// ---- old ------只要能保存，说明数量已经正确录入了。
				// -- new ---因为走平台，保存完整动作执行完后，还要重读是否签字了。
				freshStatusTs(voAudit.getHeaderVO().getPrimaryKey());

				// 取消签字后，要根据有否来源单据对菜单删除按钮控制
				ctrlSourceBillButtons(true);

				showHintMessage(sMsg);
			}
		} catch (Exception e) {
			handleException(e);
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000007")/* @res "取消签字出错。" */);
			showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000008")/* @res "取消签字出错:" */
					+ e.getMessage());
		}

	}

	//	/**
	//	 * 判断是否存在自动生成的其他入库单,如存在,再判断是否需要删除.
	//	 * 
	//	 * @param voAudit
	//	 */
	//	protected void deleteBill(GeneralBillVO voAudit, SMGeneralBillVO voDelete) {
	//
	//		try {
	//			if (!isNeedCreateBill(voAudit))
	//				return;
	//			GeneralBillHeaderVO headvo = voAudit.getHeaderVO();
	////			String pk = headvo.getAutocreatebillid();
	//			String pk = null;
	//			if (pk == null || "".equals(pk))
	//				return;
	//			GeneralBillVO vo = new GeneralBillVO();
	//			GeneralBillHeaderVO hvo = (GeneralBillHeaderVO) getUAPQuery()
	//					.executeQuery(
	//							"select * from ic_general_h where dr =0 and cgeneralhid='"
	//									+ pk + "'",
	//							new BeanProcessor(GeneralBillHeaderVO.class));
	//			if (hvo == null)
	//				return;
	//			try {
	//				hvo.setCoperatoridnow(m_sUserID);
	//				hvo.setAttributeValue("clogdatenow", m_sLogDate);
	//				// 帐期、信用信息
	//				vo.m_iActionInt = nc.vo.scm.pub.bill.CreditConst.ICREDIT_ACT_DEL;
	//				vo.m_sActionCode = "DELETE";
	//				// 操作员日志
	//				nc.vo.sm.log.OperatelogVO log = getNormalOperateLog();
	//				vo.setOperatelogVO(log);
	//				vo.setIsCheckCredit(true);
	//				vo.setIsCheckPeriod(true);
	//				vo.setIsCheckAtp(true);
	//				vo.setIsRwtPuUserConfirmFlag(true);
	//				vo.m_iActionInt = nc.vo.scm.pub.bill.CreditConst.ICREDIT_ACT_DEL;
	//				vo.m_sActionCode = "DELETE";
	//
	//				GeneralBillVO[] vos = new GeneralBillVO[1];
	//				vo.setParentVO(hvo);
	//				vos[0] = vo;
	//				nc.ui.pub.pf.PfUtilClient.processBatch("DELETE", "4A",
	//						m_sLogDate, vos);
	//			} catch (Exception e) {
	//				e.printStackTrace();
	//				showErrorMessage("删除自动生成的其他入库单失败,请手动删除其他入库单.单号:"
	//						+ vo.getHeaderVO().getVbillcode());
	//			}
	//		} catch (Exception e) {
	//			e.printStackTrace();
	//			showErrorMessage("删除自动生成的其他入库单出错!");
	//		}
	//
	//	}

	/**
	 * 判断是否存在自动生成的其他入库单,xiaolong_fan.
	 * 
	 * @param voAudit
	 */
	protected boolean existBill(GeneralBillVO voAudit) {
		try {
			// 执行取消签字
			GeneralBillHeaderVO headvo = voAudit.getHeaderVO();
			String pk = headvo.getPrimaryKey();
			if (pk != null && !"".equals(pk)) {
				GeneralBillVO vo = new GeneralBillVO();
				GeneralBillHeaderVO hvo = (GeneralBillHeaderVO) getUAPQuery().executeQuery("select * from ic_general_h where nvl(dr,0)=0 and autocreatebillid='" + pk + "'", new BeanProcessor(GeneralBillHeaderVO.class));
				if (hvo != null) {
					showErrorMessage("取消签字时,请先删除相关联的其他入库单:" + hvo.getVbillcode());
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 创建者：王乃军 功能：复制当前单据 参数： 返回： 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	public void onCopy() {
		try {
			// 当前是列表形式时，首先切换到表单形式
			if (BillMode.List == m_iCurPanel) {
				onSwitch();

			}

			if (m_iLastSelListHeadRow < 0 || m_alListData == null || m_iLastSelListHeadRow >= m_alListData.size() || m_alListData.get(m_iLastSelListHeadRow) == null) {
				SCMEnv.out("sn error,or list null");
				return;
			}
			// 显示复制的单据内容
			m_voBill = (GeneralBillVO) ((GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow)).clone();
			// 初始化货位/序列号数据
			if (m_voBill != null) {

				// 新增
				getBillCardPanel().addNew();
				// 比新增(onAdd())去掉了增行动作。
				// 清表头ID
				m_voBill.getHeaderVO().setPrimaryKey(null);
				m_voBill.setHeaderValue("coperatorid", null);
				m_voBill.setHeaderValue("coperatorname", null);
				m_voBill.setHeaderValue("cregister", null);
				m_voBill.setHeaderValue("cregistername", null);
				m_voBill.setHeaderValue("cauditorid", null);
				m_voBill.setHeaderValue("cauditorname", null);
				m_voBill.setHeaderValue("ts", null);
				m_voBill.setHeaderValue("vbillcode", null);
				m_voBill.setHeaderValue("daccountdate", null);// zhy2005-04-05
				// 复制时应将daccountdate清空
				m_voBill.setHeaderValue("dauditdate", null);// zhy2005-04-08
				// 复制时应将dauditdate清空

				m_voBill.setHeaderValue("iprintcount", new Integer(0)); // 置打印次数

				m_voBill.setHeaderValue("taccounttime", null); // 置签字时间

				// 新增单据是自由态
				m_voBill.setHeaderValue("fbillflag", nc.vo.ic.pub.bill.BillStatus.FREE);
				// 置新单据号
				// 设置显示VO
				int iRowCount = m_voBill.getItemCount();
				UFDate dcurdate = new UFDate(m_sLogDate);
				GeneralBillItemVO voaMyItem[] = m_voBill.getItemVOs();
				for (int row = 0; row < iRowCount; row++) {
					// 清表体ID
					voaMyItem[row].setPrimaryKey(null);
					voaMyItem[row].setCgeneralhid(null);
					// 来源单据数据，
					voaMyItem[row].setCsourcebillbid(null);
					voaMyItem[row].setCsourcebillhid(null);
					voaMyItem[row].setCsourcetype(null);
					voaMyItem[row].setVsourcebillcode(null);
					voaMyItem[row].setVsourcerowno(null);
					voaMyItem[row].setAttributeValue("csourcetypename", null);
					voaMyItem[row].setCfirstbillbid(null);
					voaMyItem[row].setCfirstbillhid(null);
					voaMyItem[row].setCfirsttype(null);
					voaMyItem[row].setVfirstbillcode(null);
					voaMyItem[row].setAttributeValue("cfirsttypename", null);
					// 对应单单据数据
					voaMyItem[row].setCcorrespondbid(null);
					voaMyItem[row].setCcorrespondhid(null);
					voaMyItem[row].setCcorrespondtype(null);
					voaMyItem[row].setCcorrespondcode(null);
					voaMyItem[row].setAttributeValue("ncorrespondnum", null);
					voaMyItem[row].setAttributeValue("ncorrespondastnum", null);
					voaMyItem[row].setAttributeValue("ntranoutnum", null);
					voaMyItem[row].setAttributeValue("ntranoutastnum", null);
					voaMyItem[row].setAttributeValue("nretnum", null);
					voaMyItem[row].setAttributeValue("nretastnum", null);
					voaMyItem[row].setAttributeValue("nretgrossnum", null);
					voaMyItem[row].setAttributeValue("naccumtonum", null);
					voaMyItem[row].setAttributeValue("naccumwastnum", null);
					voaMyItem[row].setAttributeValue("cparentid", null);
					voaMyItem[row].setAttributeValue("nprice", null);
					voaMyItem[row].setAttributeValue("nmny", null);
					// 冻结数据
					voaMyItem[row].setCfreezeid(null);
					// ts
					voaMyItem[row].setTs(null);
					// 暂估标志：否
					voaMyItem[row].setIsok(new UFBoolean(false));
					// 被结算标志：否
					voaMyItem[row].setBzgflag(new UFBoolean(false));
					// 被结算标志：否
					voaMyItem[row].setAttributeValue("btoinzgflag", new UFBoolean(false));
					voaMyItem[row].setAttributeValue("btoouttoiaflag", new UFBoolean(false));
					// 复制单据时清空表体行标记（目前只有配套使用）
					voaMyItem[row].setFbillrowflag(null);
					// 复制单据时请空表体的条码 修改 by hanwei 2004-4-07
					voaMyItem[row].setBarCodeVOs(null);
					voaMyItem[row].setAttributeValue(IItemKey.NBARCODENUM, new UFDouble(0.0));
					voaMyItem[row].setBarcodeClose(new nc.vo.pub.lang.UFBoolean('N'));
					voaMyItem[row].setAttributeValue(IItemKey.NKDNUM, null);
					// zhy2005-0-27复制单据时清空表体其他来源单据号等信息
					voaMyItem[row].setAttributeValue("csrc2billtype", null);// 其他来源单据类型
					voaMyItem[row].setAttributeValue("csrc2billhid", null);// 其他来源单ID
					voaMyItem[row].setAttributeValue("vsrc2billcode", null);// 其他来源单据号
					voaMyItem[row].setAttributeValue("csrc2billbid", null);// 其他来源单行ID
					voaMyItem[row].setAttributeValue("vsrc2billrowno", null);// 其他来源单行号

					voaMyItem[row].setAttributeValue("csumid", null);// VMI汇总ID
					voaMyItem[row].setSerial(null);
					if (!m_bIsInitBill) voaMyItem[row].setDbizdate(dcurdate);

				}
				setBillVO(m_voBill);
				// 重置其它数据

				// 设置当前的条码框的条码 韩卫 2004-04-05
				if (m_utfBarCode != null) m_utfBarCode.setCurBillItem(null);

				nc.ui.pub.bill.BillModel bmTemp = getBillCardPanel().getBillModel();

				bmTemp.setNeedCalculate(false);
				for (int row = 0; row < iRowCount; row++) {
					// 设置行状态为新增
					if (bmTemp != null) {
						bmTemp.setRowState(row, nc.ui.pub.bill.BillModel.ADD);
						bmTemp.setValueAt(null, row, m_sBillRowItemKey);
					}

				}
				bmTemp.setNeedCalculate(true);

				// 设置新增单据的初始数据，如日期，制单人等。
				setNewBillInitData();
				getBillCardPanel().setEnabled(true);
				m_iMode = BillMode.New;
				// 设置单据号是否可编辑
				if (getBillCardPanel().getHeadItem("vbillcode") != null) getBillCardPanel().getHeadItem("vbillcode").setEnabled(m_bIsEditBillCode);

				setButtonStatus(true);
			}

		} catch (Exception e) {
			handleException(e);

		}

	}

	protected void onRefInICBill() {
		try { //
			QryInICBillDlg dlgBill = new QryInICBillDlg("cgeneralhid", m_sCorpID, m_sUserID, "40089908", "1=1", "4A", null, null, "4I", this);

			if (dlgBill == null) return;

			nc.ui.ic.pub.pf.ICBillQuery dlgQry = new nc.ui.ic.pub.pf.ICBillQuery(this);
			// 加载源查询模版
			// dlgQry.setParent(this);
			dlgQry.setTempletID(m_sCorpID, "40080608", m_sUserID, null, "40089908");

			dlgQry.initData(m_sCorpID, m_sUserID, "40089908", null, "4I", "4A", null);
			if (dlgQry.showModal() == nc.ui.pub.beans.MessageDialog.ID_OK) {

				// 需要注册
				nc.vo.pub.query.ConditionVO[] voCons = dlgQry.getConditionVO();

				// 获取查询条件
				StringBuffer sWhere = new StringBuffer(" 1=1 ");
				if (voCons != null && voCons.length > 0 && voCons[0] != null) {
					sWhere.append(" and " + dlgQry.getWhereSQL(voCons));
				}

				// 加载源参照对话框
				dlgBill.initVar("cgeneralhid", m_sCorpID, m_sUserID, null, sWhere.toString(), "4A", null, null, "4I", null, this);

				dlgBill.setStrWhere(sWhere.toString());
				dlgBill.getBillVO();
				dlgBill.loadHeadData();
				dlgBill.addBillUI();
				dlgBill.setQueyDlg(dlgQry);

				nc.vo.scm.pub.ctrl.GenMsgCtrl.printHint("will load qrybilldlg");
				if (dlgBill.showModal() == nc.ui.pub.beans.MessageDialog.ID_OK) {
					nc.vo.scm.pub.ctrl.GenMsgCtrl.printHint("qrybilldlg closeok");
					// 获取所选VO
					nc.vo.pub.AggregatedValueObject[] vos = dlgBill.getRetVos();
					nc.vo.scm.pub.ctrl.GenMsgCtrl.printHint("qrybilldlg getRetVos");

					if (vos == null) {
						nc.vo.scm.pub.ctrl.GenMsgCtrl.printHint("qrybilldlg getRetVos null");
						return;
					}

					// //
					nc.vo.scm.pub.ctrl.GenMsgCtrl.printHint("qrybilldlg getRetVos is not null");
					nc.vo.pub.AggregatedValueObject[] voRetvos = (nc.vo.pub.AggregatedValueObject[]) nc.ui.pub.change.PfChangeBO_Client.pfChangeBillToBillArray(vos, "4A", "4I");
					nc.vo.scm.pub.ctrl.GenMsgCtrl.printHint("qrybilldlg getRetVos pfChangeBillToBillArray ok");

					// 控制界面
					setBillRefResultVO(null, voRetvos);
					if (m_voBill.getItemVOs().length > 0 && m_voBill.getItemVOs()[0] != null && m_voBill.getItemVOs()[0].getNoutnum() != null) {
						m_alSerialData = m_voBill.getSNs();
						m_alLocatorData = m_voBill.getLocators();
					}

					nc.vo.scm.pub.ctrl.GenMsgCtrl.printHint("qrybilldlg getRetVos pfChangeBillToBillArray ok setBillRefResultVO ok");

				}
			}

		} catch (Exception e) {

			showErrorMessage(e.getMessage());
		}

	}

	public void onDelete() {
		m_timer.start("删除开始");

		nc.vo.ic.pub.bill.Timer t = new nc.vo.ic.pub.bill.Timer();
		t.start();

		int iSelListHeadRowCount = getBillListPanel().getHeadTable().getSelectedRowCount();
		if (iSelListHeadRowCount <= 0) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UCH003")/* @res "请选择要处理的数据！" */);
			return;
		}

		switch (nc.ui.pub.beans.MessageDialog.showYesNoDlg(this, null, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH002")/*
																																		 * @res
																																		 * "是否确认要删除？"
																																		 */
		, MessageDialog.ID_NO)) {

		case nc.ui.pub.beans.MessageDialog.ID_YES:
			break;
		default:
			return;
		}
		// 在列表下，多张单据删除;在表单形式下,只能每次删除一张单据,因为表单和列表下选中的单据必须
		// 同步，所以可以统一处理。
		// GeneralBillVO voaDeleteBill[] = new
		// GeneralBillVO[iSelListHeadRowCount];
		// GeneralBillItemVO voaItem[] = null;
		ArrayList alvo = getSelectedBills();
		GeneralBillVO voaDeleteBill[] = new GeneralBillVO[alvo.size()];

		// 操作员日志
		nc.vo.sm.log.OperatelogVO log = getNormalOperateLog();

		for (int i = 0; i < alvo.size(); i++) {
			voaDeleteBill[i] = (GeneralBillVO) alvo.get(i);
			// 当前操作员2002-04-10.wnj
			voaDeleteBill[i].getHeaderVO().setCoperatoridnow(m_sUserID);
			voaDeleteBill[i].getHeaderVO().setAttributeValue("clogdatenow", m_sLogDate);
			// 帐期、信用信息
			voaDeleteBill[i].m_iActionInt = nc.vo.scm.pub.bill.CreditConst.ICREDIT_ACT_DEL;
			voaDeleteBill[i].m_sActionCode = "DELETE";

			voaDeleteBill[i].setOperatelogVO(log);
		}
		String sError = checkBillsCanDeleted(alvo);
		if (sError != null && sError.length() > 0) {
			showWarningMessage(sError);
			return;
		}
		onDeleteKernel(voaDeleteBill);
		if (m_bOnhandShowHidden) {
			m_pnlQueryOnHand.clearCache();
			m_pnlQueryOnHand.fresh();
		}

	}

	/**
	 * 创建者：王乃军 功能：删除处理 参数： 返回： 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	private void onDeleteKernel(GeneralBillVO[] voaDeleteBill) {
		m_timer.start("删除开始");

		nc.vo.ic.pub.bill.Timer t = new nc.vo.ic.pub.bill.Timer();
		t.start();

		try {
			int iSelListHeadRowCount = getBillListPanel().getHeadTable().getSelectedRowCount();

			int[] arySelListHeadRows = new int[iSelListHeadRowCount];
			arySelListHeadRows = getBillListPanel().getHeadTable().getSelectedRows();

			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH051")/* @res "正在删除" */);

			for (int i = 0; i < voaDeleteBill.length; i++) {
				voaDeleteBill[i].setIsCheckCredit(true);
				voaDeleteBill[i].setIsCheckPeriod(true);
				voaDeleteBill[i].setIsCheckAtp(true);
				voaDeleteBill[i].setIsRwtPuUserConfirmFlag(true);
				voaDeleteBill[i].m_iActionInt = nc.vo.scm.pub.bill.CreditConst.ICREDIT_ACT_DEL;
				voaDeleteBill[i].m_sActionCode = "DELETE";
				// voaDeleteBill[i].setStatus(nc.vo.pub.VOStatus.DELETED);
			}
			Object sdaf = voaDeleteBill[0].getChildrenVO()[0].getAttributeValue("cvendorid");
			while (true) {
				try {

					nc.ui.pub.pf.PfUtilClient.processBatch("DELETE", m_sBillTypeCode, m_sLogDate, voaDeleteBill);
					break;

				} catch (Exception ee1) {

					BusinessException realbe = nc.ui.ic.pub.tools.GenMethod.handleException(null, null, ee1);
					if (realbe != null && realbe.getClass() == RightcheckException.class) {
						showErrorMessage(realbe.getMessage() + nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000069")/*
																																			* @res
																																			* ".\n权限校验不通过,保存失败! "
																																			*/);
						getAccreditLoginDialog().setCorpID(m_sCorpID);
						getAccreditLoginDialog().clearPassWord();
						if (getAccreditLoginDialog().showModal() == nc.ui.pub.beans.UIDialog.ID_OK) {
							String sUserID = getAccreditLoginDialog().getUserID();
							if (sUserID == null) {
								throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000070")/*
																																	* @res
																																	* "权限校验不通过,保存失败. "
																																	*/);
							} else {
								for (int i = 0; i < voaDeleteBill.length; i++)
									voaDeleteBill[i].setAccreditUserID(sUserID);
								continue;
							}
						} else {
							throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000070")/*
																																* @res
																																* "权限校验不通过,保存失败. "
																																*/);

						}
					} else if (realbe != null && realbe.getClass() == CreditNotEnoughException.class) {
						// 错误信息显示，并询问用户“是否继续？”
						int iFlag = showYesNoMessage(realbe.getMessage() + " \r\n" + "是否继续？");
						// 如果用户选择继续
						if (iFlag == nc.ui.pub.beans.MessageDialog.ID_YES) {
							for (int i = 0; i < voaDeleteBill.length; i++)
								voaDeleteBill[i].setIsCheckCredit(false);
							continue;
						} else return;
					} else if (realbe != null && realbe.getClass() == PeriodNotEnoughException.class) {
						// 错误信息显示，并询问用户“是否继续？”
						int iFlag = showYesNoMessage(realbe.getMessage() + " \r\n" + "是否继续？");
						// 如果用户选择继续
						if (iFlag == nc.ui.pub.beans.MessageDialog.ID_YES) {
							for (int i = 0; i < voaDeleteBill.length; i++)
								voaDeleteBill[i].setIsCheckPeriod(false);
							continue;
						} else return;
					} else if (realbe != null && realbe.getClass() == ATPNotEnoughException.class) {
						ATPNotEnoughException atpe = (ATPNotEnoughException) realbe;
						if (atpe.getHint() == null) {
							showErrorMessage(atpe.getMessage());
							return;
						} else {
							// 错误信息显示，并询问用户“是否继续？”
							int iFlag = showYesNoMessage(atpe.getMessage() + " \r\n" + "是否继续？");
							// 如果用户选择继续
							if (iFlag == nc.ui.pub.beans.MessageDialog.ID_YES) {
								for (int i = 0; i < voaDeleteBill.length; i++)
									voaDeleteBill[i].setIsCheckAtp(false);
								continue;
							} else {
								return;
							}
						}
					} else {
						if (realbe != null) throw realbe;
						else throw ee1;
					}
				}
			}

			m_timer.showExecuteTime("平台删除");
			// showTime(lTime, "删除");

			// 删除后列表界面处理,设置m_iLastSelListHeadRow。
			removeBillsOfList(arySelListHeadRows);
			m_timer.showExecuteTime("removeBillsOfList");
			// 在表单形式下删除，显示与列表相应的单据
			if (BillMode.Card == m_iCurPanel && m_iLastSelListHeadRow >= 0 && m_alListData != null && m_alListData.size() > m_iLastSelListHeadRow) {
				setBillVO((GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow));
				// 执行公式
				// getBillCardPanel().getBillModel().execLoadFormula();
				// getBillCardPanel().updateValue();
			} else if (m_alListData == null || m_alListData.size() == 0) {
				getBillCardPanel().getBillModel().clearBodyData();
				getBillCardPanel().updateValue();
			}
			// 清空当前的货位数据
			m_alLocatorData = null;
			m_alLocatorDataBackup = null;

			// 清空当前的序列号数据
			m_alSerialData = null;
			m_alSerialDataBackup = null;
			m_timer.showExecuteTime("Before 重设按钮状态");
			// 重设按钮状态
			setButtonStatus(false);
			m_timer.showExecuteTime("重设按钮状态");
			// 根据是否为来源单据控制单据界面
			ctrlSourceBillUI(true);
			m_timer.showExecuteTime("来源单据控制单据界面");

			// delete the excel barcode file
			OffLineCtrl ctrl = new OffLineCtrl(this);
			ctrl.deleteExcelFile(voaDeleteBill, getCorpPrimaryKey());

			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "删除成功")/* @res "删除成功" */);
		} catch (Exception e) {
			handleException(e);
			// showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
			// "4008bill", "UPP4008bill-000011")/* @res "删除出错。" */);
			showWarningMessage(e.getMessage());
		}
		t.stopAndShow("删除合计");
	}

	/**
	 * 创建者：王乃军 功能：表单形式下删除行处理 参数： 返回： 例外： 日期：(2001-5-9 9:23:32)
	 * 修改日期，修改人，修改原因，注释标志：
	 */
	public void onDeleteRow() {
		int[] selrow = getBillCardPanel().getBillTable().getSelectedRows();
		int length = selrow.length;
		SCMEnv.out("count is " + length);
		int iCurLine = 0; // 当前选中行

		// 删除条码数据 add by hanwei
		if (m_voBill != null && selrow != null) {
			for (int i = 0; i < selrow.length; i++) {
				if (m_voBill.getItemBarcodeVO(selrow[i]) != null) {
					m_utfBarCode.setRemoveBarcode(m_voBill.getItemBarcodeVO(selrow[i]));
				}
			}
		}

		if (length == 0) { // 没选中一行，返回
			return;
		} else if (length == 1) { // 删除一行
			int allrownums = getBillCardPanel().getRowCount();
			getBillCardPanel().delLine();
			if (selrow[0] + 1 > allrownums) {
				int iRowCount = getBillCardPanel().getRowCount();
				if (iRowCount > 0) {
					getBillCardPanel().getBillTable().setRowSelectionInterval(selrow[0], selrow[0]);
					iCurLine = selrow[0];

				}
			}
		} else { // 删除多行

			getBillCardPanel().getBillModel().delLine(selrow);

			int iRowCount = getBillCardPanel().getRowCount();
			if (iRowCount > 0) {
				getBillCardPanel().getBillTable().setRowSelectionInterval(0, 0);
				iCurLine = 0;
			}
		}
		// 重置序列号是否可用
		setBtnStatusSN(iCurLine, true);

		// 删除条码数据？待加
	}

	/**
	 * 创建者：王乃军 功能：定位单据 参数： 返回： 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 */
	protected void onLocate() {
		if (m_alListData == null || m_alListData.size() < 1) return;

		nc.ui.scm.pub.report.OrientDialog dlgOrient = null;
		if (m_iCurPanel == BillMode.Card) {
			dlgOrient = new nc.ui.scm.pub.report.OrientDialog(this, getBillCardPanel().getBillModel(), getBillCardPanel().getBodyItems(), getBillCardPanel().getBillTable());
			dlgOrient.showModal();
			if (dlgOrient.getResult() == nc.ui.pub.beans.UIDialog.ID_OK) {
				m_isLocated = true;
			}
		} else {
			dlgOrient = new nc.ui.scm.pub.report.OrientDialog(this, getBillListPanel().getHeadBillModel(), getBillListPanel().getBillListData().getHeadItems(), getBillListPanel().getHeadTable());

			dlgOrient.showModal();
			if (dlgOrient.getResult() == nc.ui.pub.beans.UIDialog.ID_OK) {
				m_isLocated = true;
			}
		}

	}

	/**
	 * 设置数据到列表,并执行前20条单据的公式,并将默认选择设置到第一个单据上,设置菜单状态
	 * 
	 * @param voa
	 */
	public void setDataOnList(AggregatedValueObject[] voa) {
		ArrayList al = new ArrayList();
		if (voa == null) return;

		for (int i = 0; i < voa.length; i++) {
			if (voa[i] == null) continue;
			al.add(voa[i]);

		}

		setDataOnList(al, false);
	}

	/**
	 * 设置数据到列表,并执行前20条单据的公式,并将默认选择设置到第一个单据上,设置菜单状态
	 * 
	 * @since v5
	 * @author ljun
	 * @param alData
	 *            单据集合
	 * @param bQuery
	 *            是否查询调用, 是查询调用为true, 不是为false
	 */
	public void setDataOnList(ArrayList alData, boolean bQuery) {

		m_timer.start();
		setAlistDataByFormula(GeneralBillVO.QRY_FIRST_ITEM_NUM, alData);
		m_timer.showExecuteTime("@@setAlistDataByFormula公式解析时间：");

		// 执行扩展公式.目前只被销售出库单UI重载.
		execExtendFormula(alData);

		if (alData != null && alData.size() > 0) {
			m_alListData = alData;
			setListHeadData();
			// 设置当前的单据数量/序号，用于按钮控制
			setLastHeadRow(0);
			// 当前是表单形式时，首先切换到列表形式
			if (BillMode.Card == m_iCurPanel) onSwitch();

			// 缺省表头指向第一张单据
			selectListBill(0);

			// 初始化当前单据序号，切换时用到！！！不宜主动设置表单的数据。
			m_iCurDispBillNum = -1;
			// 当前单据数
			m_iBillQty = m_alListData.size();

			if (bQuery) {
				String[] args = new String[1];
				args[0] = String.valueOf(m_iBillQty);
				String message = nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000339", null, args);

				/* @res "共查到{0}张单据！" */

				if (m_iBillQty > 0) showHintMessage(message);
				else showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000013")/*
																													* @res
																													* "未查到符合条件的单据。"
																													*/);

			}
			// 控制有来源单据的按钮、菜单等的状态。
			ctrlSourceBillUI(false);

		} else {
			dealNoData();
		}

		setButtonStatus(true);
	}

	/**
	 * 
	 * 方法功能描述：查询和刷新按钮的方法。
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * 查询按钮：onQuery(true);
	 * <p>
	 * 刷新按钮：onQuery(false);
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @param bQuery
	 *            如果是进行查询，为true；如果是进行刷新，为false
	 *            <p>
	 * @author duy
	 * @time 2007-3-2 下午03:07:52
	 */
	public void onQuery(boolean bQuery) {
		// 为了兼容以前的版本，加入了成员变量来存储是进行查询还是进行刷新
		m_bQuery = bQuery;

		// 执行查询（刷新）过程，在该方法中进行了查询和刷新的分别处理
		onQuery();
	}

	/**
	 * 创建者：王乃军 功能：查询处理 参数： 返回： 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志： 韩卫
	 * 2003-06-24 注释： 由于已经在
	 * nc.ui.ic.pub.bill.GeneralBillClientUI.setListHeadData(GeneralBillHeaderVO
	 * nc.ui.ic.pub.bill.GeneralBillClientUI.setAlistDataByFormula(int,
	 * ArrayList) 执行了表头、表体公式，所以切不可在代码中重复执行以下代码：
	 * getBillListPanel().getHeadBillModel().execLoadFormula();
	 * getBillListPanel().getBodyBillModel().execLoadFormula();
	 * 
	 * @deprecated Since version 5.1, 使用onQuery(boolean bQuery)
	 */
	public void onQuery() {
		// DUYONG 增加查询和刷新的代码
		try {
			nc.vo.ic.pub.bill.Timer timer = new nc.vo.ic.pub.bill.Timer();
			timer.start("@@查询开始：");
			m_sBnoutnumnull = null;
			QryConditionVO voCond = null;

			// 将原单据类型（卡片/列表）纪录下来，如果在卡片界面执行了刷新，则无须切换到列表界面
			int cardOrList = m_iCurPanel;
			// 如果是[(1)进行查询(2)从来没有进行查询过但是点击了刷新按钮]，则显示查询模板进行查询
			// 如果曾经查询过，并且点击了刷新按钮，则跳过此段代码
			if (m_bQuery || !m_bEverQry) {
				getConditionDlg().showModal();
				timer.showExecuteTime("@@getConditionDlg().showModal()：");

				if (getConditionDlg().getResult() != nc.ui.pub.beans.UIDialog.ID_OK)
				// 取消返回
				return;

				// 获得qrycontionVO的构造
				voCond = getQryConditionVO();

				// 记录查询条件备用
				m_voLastQryCond = voCond;

				// 如果是进行查询，则将“曾经查询过”的标识设置成true（这样，才能够进行“刷新”的操作）
				m_bEverQry = true;
				// 刷新按钮
				setButtonStatus(true);
			} else voCond = m_voLastQryCond;

			// DUYONG 此处应该是刷新按钮功能的开始执行处

			// 如果使用公式则必须传voaCond 到后台. 修改 zhangxing 2003-03-05
			nc.vo.pub.query.ConditionVO[] voaCond = getConditionDlg().getConditionVO();

			voCond.setParam(QryConditionVO.QRY_CONDITIONVO, voaCond);

			voCond.setIntParam(0, GeneralBillVO.QRY_HEAD_ONLY_PURE);

			if (m_sBnoutnumnull != null) {
				// 是否存在实发数量
				voCond.setParam(33, m_sBnoutnumnull);
			}

			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000012")/* @res "正在查询，请稍候..." */);
			timer.showExecuteTime("Before 查询：：");

			ArrayList alListData = (ArrayList) GeneralBillHelper.queryBills(m_sBillTypeCode, voCond);

			timer.showExecuteTime("查询时间：");

			setDataOnList(alListData, true);

			// DUYONG 当执行刷新操作，并且当前界面为卡片类型时，不应该切换到列表类型的界面中
			if (!m_bQuery && m_iCurPanel != cardOrList) {
				onSwitch();
			}
		} catch (Exception e) {
			handleException(e);
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000014")/* @res "查询出错。" */);
			showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000015")/* @res "查询出错：" */
					+ e.getMessage());
		}
	}

	/**
	 * 创建者：王乃军 功能：序列号分配 参数： 返回： 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	public void onSNAssign() {
		nc.vo.ic.pub.bill.Timer timer = new nc.vo.ic.pub.bill.Timer();
		timer.start("@@序列号分配开始：");
		// 浏览方式下先查询货位/序列号，当然qryLocSN对每张单据只查一次库。
		// if (BillMode.Browse == m_iMode) {
		qryLocSN(m_iLastSelListHeadRow, QryInfoConst.LOC_SN);
		// timer.showExecuteTime("@@浏览方式下查询货位/序列号查询时间：");
		// }
		// 当前选中的行
		int iCurSelBodyLine = -1;
		if (BillMode.Card == m_iCurPanel) {
			iCurSelBodyLine = getBillCardPanel().getBillTable().getSelectedRow();
			if (iCurSelBodyLine < 0) {
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000016")/*
																												* @res "请选中要进行序列号分配的行。"
																												*/);
				return;
			}
		} else {
			iCurSelBodyLine = getBillListPanel().getBodyTable().getSelectedRow();
			if (iCurSelBodyLine < 0) {
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000017")/*
																												* @res "请选中要查看序列号的行。"
																												*/);
				return;
			}
		}
		InvVO voInv = null;
		// 仓库
		WhVO voWh = null;
		// 单据VO,表单下是m_voBill，列表下读自m_alListData.
		GeneralBillVO voBill = null;

		// 浏览状态下察看，先读数据，所以一定要放在setInitVOs之前

		// 读表体vo,区分列表下还是表单下
		// 表单形式下
		if (BillMode.Card == m_iCurPanel) {
			if (m_voBill == null || m_voBill.getItemCount() <= 0) {
				SCMEnv.out("bill null E.");
				return;
			}
			voBill = m_voBill;
			// 数据VO
			GeneralBillItemVO voItem = getBodyVO(iCurSelBodyLine);
			// 属性VO
			GeneralBillItemVO voItemPty = voBill.getItemVOs()[iCurSelBodyLine];
			// 合并
			if (voItemPty != null) voItemPty.setIDItems(voItem);
			// 完整的存货行数据
			if (voItem != null) voInv = voItemPty.getInv();

			if (voBill != null) voWh = voBill.getWh();
			// 如果是非浏览模式，并且仓库是货位管理，则提醒入库时应走货位。
			if (m_iMode != BillMode.Browse) {
				if (voWh != null && voWh.getIsLocatorMgt() != null && voWh.getIsLocatorMgt().intValue() == 1 && voItem.getInOutFlag() == InOutFlag.IN && m_voBill.getItemValue(iCurSelBodyLine, "cspaceid") == null) {
					nc.ui.pub.beans.MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPPSCMCommon-000270")/*
																																					* @res "提示"
																																					*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000018")/*
																																																										* @res
																																																										* "入库时序列号分配请先执行“货位分配”，在货位界面上执行“序列号”。！"
																																																										*/);
					return;

				}
			}
			// //如果是非浏览模式，并且仓库是货位管理，则提醒入库时应走货位。
			// if (m_iMode != BillMode.Browse) {
			// if (voWh != null
			// && voWh.getIsLocatorMgt() != null
			// && voWh.getIsLocatorMgt().intValue() == 1
			// && voItem.getInOutFlag() == InOutFlag.IN) {
			// nc.ui.pub.beans.MessageDialog.showHintDlg(
			// this,
			// "提示",
			// "入库时序列号分配请先执行“货位分配”，在货位界面上执行“序列号”。");
			// return;

			// }
			// }

		} else
		// 列表形式下察看，先读数据，所以一定要放在setInitVOs之前
		if (m_iLastSelListHeadRow >= 0 && m_iLastSelListHeadRow < m_alListData.size()) {
			// 置入各行的货位分配等辅表数据
			voBill = (GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow);
			if (voBill == null) {
				SCMEnv.out("bill null E.");
				return;
			}
			voInv = voBill.getItemInv(iCurSelBodyLine);
			// warehouse
			voWh = voBill.getWh();
			// 公司PK
			voWh.setPk_corp(m_sCorpID);
		}

		if (voInv != null && voBill != null) {
			// 设置上当前单据的PK.
			voInv.setCgeneralhid(voBill.getHeaderVO().getPrimaryKey());
			String csrctype = voBill.getItemVOs()[iCurSelBodyLine].getCsourcetype();
			if ("A3".equals(csrctype)) voInv.setCfreezeid(voBill.getItemVOs()[iCurSelBodyLine].getCsourcebillbid());
			else if (csrctype != null && (csrctype.startsWith("5") || csrctype.startsWith("30"))) voInv.setCfreezeid(voBill.getItemVOs()[iCurSelBodyLine].getCfirstbillbid());
			// 对应的入库单表体行，可能为null，
			// 当出库跟踪入库不能为null
			String sCorrespondBodyItemPK = voInv.getCcorrespondbid();
			// 出库是否跟踪入库
			// zhx
			if (getIsInvTrackedBill(voInv) && voInv.getInOutFlag() == InOutFlag.OUT && (sCorrespondBodyItemPK == null || sCorrespondBodyItemPK.trim().length() == 0)) {
				showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000019")/*
																												* @res
																												* "请先指定对应入库单，然后再试。"
																												*/);
				return;
			}

			// 非浏览模式下数量不能为零
			if (BillMode.Card == m_iCurPanel && (BillMode.Update == m_iMode || BillMode.New == m_iMode)) {
				Object oQty = voInv.getAttributeValue(m_sNumItemKey);
				if (oQty == null || oQty.toString().trim().length() == 0) {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000020")/*
																													* @res
																													* "请先输入数量。"
																													*/);
					return;
				}
				// 数量必须不能小数 by hanwei 2003-07-20
				if (nc.vo.ic.pub.check.VOCheck.checkIsDecimal(oQty.toString())) {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000021")/*
																													* @res
																													* "序列号管理存货不允许输入小数。"
																													*/);
					return;
				}

			}

			// if (BillMode.Browse == m_iMode)
			// //置入各行的货位分配等辅表数据
			// resetBodyAssistData(m_iLastSelListHeadRow);
			// ydy2002-12-19
			LocatorVO aLoc = null;
			LocatorVO[] voLocs = null;
			if (m_alLocatorData != null && m_alLocatorData.size() > 0) voLocs = (LocatorVO[]) m_alLocatorData.get(iCurSelBodyLine);

			if (voLocs == null || voLocs.length != 1) {
				// showHintMessage("货位数据有误");
				aLoc = new LocatorVO();

			} else aLoc = voLocs[0];
			// end ydy
			// -浏览；非货位仓库的序列号入库；出库序列号
			getSerialAllocationDlg().setDataVO(m_iBillInOutFlag, voWh, aLoc, voInv, m_iMode, (SerialVO[]) m_alSerialData.get(iCurSelBodyLine), voLocs);

			// 当前的序列号们

			int result = getSerialAllocationDlg().showModal();
			if (nc.ui.pub.beans.UIDialog.ID_OK == result && (BillMode.New == m_iMode || BillMode.Update == m_iMode)) {
				SerialVO voaSN[] = getSerialAllocationDlg().getDataSNVO();
				// 保存分配结果
				m_alSerialData.set(iCurSelBodyLine, voaSN);
				// if (voaSN != null)
				// for (int i = 0; i < voaSN.length; i++)
				// SCMEnv.out("ret sn[" + i + "] is" +
				// voaSN[i].getVserialcode());
				// 如果是出库的话，还可能有货位分配数据
				ArrayList alRes = getSerialAllocationDlg().getDataSpaceVO();
				if (alRes != null && alRes.size() > 0) {
					SCMEnv.out("space is ready!");
					LocatorVO voaLoc[] = new LocatorVO[alRes.size()];

					UFDouble dTempGrossNum = null;
					UFDouble dTempNum = null;
					UFDouble dTemp = new UFDouble(0.0);
					Object oTempValue = getBillCardPanel().getBodyValueAt(iCurSelBodyLine, m_sNgrossnum);
					if (oTempValue != null && oTempValue.toString().trim().length() > 0) dTempGrossNum = new UFDouble(oTempValue.toString());
					oTempValue = getBillCardPanel().getBodyValueAt(iCurSelBodyLine, m_sNumItemKey);
					if (oTempValue != null && oTempValue.toString().trim().length() > 0) dTempNum = new UFDouble(oTempValue.toString());

					for (int i = 0; i < alRes.size(); i++) {
						voaLoc[i] = (LocatorVO) alRes.get(i);
						// 由于序列号参照上面没有毛重,此处特意重置一下
						if (dTempGrossNum != null && dTempNum != null) {
							UFDouble dGrossNum = null;
							UFDouble dNum = null;
							if (voaLoc[i].getNinspacenum() != null) dNum = voaLoc[i].getNinspacenum();
							else dNum = voaLoc[i].getNoutspacenum();

							if (dNum != null) {
								dGrossNum = dNum.div(dTempNum).multiply(dTempGrossNum);
								if (i == alRes.size() - 1) {
									dGrossNum = dTempGrossNum.sub(dTemp);
								}
								voaLoc[i].setNoutgrossnum(dGrossNum);
								dTemp = dTemp.add(dGrossNum);
							}
						}
					}

					// 保存到对应的行
					m_alLocatorData.set(iCurSelBodyLine, voaLoc);
					setBodySpace(iCurSelBodyLine);
					// 写界面货位
					// if(voaLoc.length==1&&voaLoc[0]!=null){
					// getBillCardPanel().setBodyValueAt(voaLoc[0].getVspacename(),iCurSelBodyLine,"vspacename");
					// getBillCardPanel().setBodyValueAt(voaLoc[0].getCspaceid(),iCurSelBodyLine,"cspaceid");
					// }
					// else{
					// getBillCardPanel().setBodyValueAt(null,iCurSelBodyLine,"vspacename");
					// getBillCardPanel().setBodyValueAt(null,iCurSelBodyLine,"cspaceid");
					// }

				}

			}
		} else {
			SCMEnv.out("----> no bill ERR.");
		}
	}

	/**
	 * 创建者：王乃军 功能：货位分配 参数： 返回： 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	public void onSpaceAssign() {
		// 浏览方式下先查询货位/序列号，当然qryLocSN对每张单据只查一次库。
		// m_dlgSpaceAllocation = null;

		// if (BillMode.Browse == m_iMode)
		qryLocSN(m_iLastSelListHeadRow, QryInfoConst.LOC_SN);

		// 这里是一次分配，实际上可以支持选行分配的。
		// 在先选择完序列号后剩余的需货位分配的行就要选行处理。
		// 处理方法：保存数据对应的行号，返回数据后set到相应的位置即可。

		InvVO[] voInv = null;
		// 仓库
		WhVO voWh = null;

		// 表单形式下
		if (BillMode.Card == m_iCurPanel) {
			// 滤去空行
			if (BillMode.Update == m_iMode || BillMode.New == m_iMode) filterNullLine();
			if (m_voBill != null) {
				// 属性VO
				GeneralBillItemVO voItemPty[] = m_voBill.getItemVOs();
				// ID
				GeneralBillItemVO voItemID[] = getBodyVOs();
				voInv = new InvVO[m_voBill.getItemCount()];
				// 合并属性、ID
				for (int row = 0; row < m_voBill.getItemCount(); row++) {
					voItemPty[row].setIDItems(voItemID[row]);
					// 带属性的InvVO
					voInv[row] = voItemPty[row].getInv();
				}
				voWh = m_voBill.getWh();
			}
		} else
		// 列表形式下察看，先读数据，所以一定要放在setInitVOs之前
		if (m_iLastSelListHeadRow >= 0 && m_iLastSelListHeadRow < m_alListData.size()) {
			// 置入各行的货位分配等辅表数据
			GeneralBillVO bvo = (GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow);
			if (bvo != null) {
				int iCount = bvo.getItemCount();
				voInv = new InvVO[iCount];
				for (int line = 0; line < iCount; line++)
					voInv[line] = bvo.getItemInv(line);
				// warehouse
				voWh = bvo.getWh();
			}

		}
		if (BillMode.Browse != m_iMode)
		// 置入各行的货位分配等辅表数据
		// resetBodyAssistData(m_iLastSelListHeadRow);
		// else
		// 出库的单据行时请先执行“序列号”，在序列号界面上汇总货位。
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000022")/*
																										* @res
																										* "出库的单据行时请先执行“序列号”，在序列号界面上汇总货位。"
																										*/);

		getSpaceAllocationDlg().setData(m_iBillInOutFlag, voWh, voInv, m_iMode, m_alLocatorData, m_alSerialData);
		int result = getSpaceAllocationDlg().showModal();

		if (nc.ui.pub.beans.UIDialog.ID_OK == result && (BillMode.New == m_iMode || BillMode.Update == m_iMode)) {
			ArrayList alRes = getSpaceAllocationDlg().getDataSpaceVO();
			// 保存
			// 分配的序列号,如果返回结果的某个element是null,表示是出库的存货，不通过货位分配界面分配序列号。所以不能置到序列号数据。
			ArrayList alSnRes = null;

			for (int row = 0; row < m_voBill.getItemCount(); row++) {
				if (m_voBill.getItemVOs()[row].getInOutFlag() == InOutFlag.IN) {
					alSnRes = getSpaceAllocationDlg().getDataSNVO(row);
					if (alSnRes != null && alSnRes.size() > 0) {
						SerialVO[] voaSN = new SerialVO[alSnRes.size()];
						// toArray ERR! ????
						for (int hh = 0; hh < alSnRes.size(); hh++)
							voaSN[hh] = (SerialVO) alSnRes.get(hh);

						m_alSerialData.set(row, voaSN);
						SCMEnv.out(row + " sn is set!");
					}
				}
			}
			// 设置货位
			if (alRes != null && alRes.size() > 0) {
				// 如果需要，初始化之
				if (m_alLocatorData == null) m_alLocatorData = new ArrayList(alRes.size());
				for (int i = 0; i < alRes.size(); i++) {
					LocatorVO voLoc[] = (LocatorVO[]) alRes.get(i);
					// 只设置有数据的，因为序列号也会修改货位数据
					if (voLoc != null && voLoc.length > 0) {
						if (m_alLocatorData.size() < i + 1) m_alLocatorData.add(i, voLoc);
						m_alLocatorData.set(i, voLoc);
					} else {
						m_alLocatorData.set(i, null);
					}
					setBodySpace(i);
					// if(voLoc.length==1&&voLoc[0]!=null){
					// getBillCardPanel().setBodyValueAt(voLoc[0].getVspacename(),i,"vspacename");
					// getBillCardPanel().setBodyValueAt(voLoc[0].getCspaceid(),i,"cspaceid");
					// }
					// else{
					// getBillCardPanel().setBodyValueAt(null,i,"vspacename");
					// getBillCardPanel().setBodyValueAt(null,i,"cspaceid");
					// }
					SCMEnv.out(i + " space is set!");
					// }
				}

			}
		}

	}

	/**
	 * 创建者：王乃军 功能：表单/列表形式切换 参数： 返回： 例外： 日期：(2001-5-9 9:23:32)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * v5: 支持列表下的多张单据参照生成时，修改按钮和双击表头都会在onSwitch中 处理为新增单据
	 * 
	 * 
	 */
	public void onSwitch() {

		if (BillMode.List == m_iCurPanel) {
			// 列表---〉表单切换
			m_iCurPanel = BillMode.Card;

			// 置排序主键为空
			m_sLastKey = null;

			getButtonTree().getButton(ICButtonConst.BTN_SWITCH).setHint(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000068")/* @res "切换到列表形式" */);
			updateButton(getButtonTree().getButton(ICButtonConst.BTN_SWITCH));

			// 当列表---〉表单切换时，未改选其它单据，则无需重设表单数据，以提高效率。
			if (m_sLastKey != null || m_iLastSelListHeadRow >= 0
			// && m_iCurDispBillNum != m_iLastSelListHeadRow
			&& m_alListData != null && m_alListData.size() > m_iLastSelListHeadRow && m_alListData.get(m_iLastSelListHeadRow) != null) {

				// zhx new add a varient to store the ini vocher VO
				m_voBillRefInput = (GeneralBillVO) ((GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow)).clone();
				m_voBill = (GeneralBillVO) ((GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow)).clone();

				// v5 lj 支持参照生成多单据
				if (m_bRefBills == true) {

					try {
						setBillRefResultVO(m_sBizTypeRef, new GeneralBillVO[] {
							m_voBill
						});
					} catch (Exception e1) {
						nc.vo.scm.pub.SCMEnv.error(e1);
					}
				}

				else {
					setBillVO(m_voBill);
				}

				// 当前单据序号
				m_iCurDispBillNum = m_iLastSelListHeadRow;
				// 当前单据数
				m_iBillQty = m_alListData.size();

				setCardMode();
			}
		} else {
			// 表单--->列表切换
			m_iCurPanel = BillMode.List;
			// 当前单据序号
			m_iCurDispBillNum = m_iLastSelListHeadRow;

			selectListBill(m_iLastSelListHeadRow);

			getButtonTree().getButton(ICButtonConst.BTN_SWITCH).setHint(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000312")/* @res "切换到表单形式" */);
			updateButton(getButtonTree().getButton(ICButtonConst.BTN_SWITCH));

			// v5 lj
			if (m_bRefBills == true) {
				m_iMode = BillMode.Browse;// 更改为浏览状态
				// 如果当前列表下的单据为0（保存后删除的），则修改m_bRefBills=false
				if (m_alListData == null || m_alListData.size() == 0) {
					setRefBillsFlag(false);
				}
			}
		}

		showBtnSwitch();

		// 界面显示
		m_layoutManager.show();

		setButtonStatus(false);
		// 如果来源单据不为空，设置业务类型等不可用
		ctrlSourceBillUI(false);
		ctrlSourceBillButtons(true);
	}

	/**
	 * 创建者：王乃军 功能：修改处理 参数： 返回： 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	public void onUpdate() {

		// v5 lj
		if (m_bRefBills == true) {
			onSwitch();
			return;
		}
		// end v5
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH027")/* @res "正在修改" */);
		// 读序列号及货位--如果需要的话。
		qryLocSN(m_iLastSelListHeadRow, QryInfoConst.LOC_SN);
		GeneralBillVO voMyBill = null;
		// arraylist 有的话用他的,没有话,就是新的.
		if (m_alListData != null && m_alListData.size() > m_iLastSelListHeadRow) voMyBill = (GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow);
		// 需要用原来的数据的clone，否在在编辑过程中，会同步修改原来的数据。
		if (voMyBill != null) {
			ArrayList alTemp = voMyBill.getLocatorsClone();
			if (alTemp != null) {
				m_alLocatorData = null;
				m_alLocatorData = alTemp;
			}
			alTemp = voMyBill.getSNsClone();
			if (alTemp != null) {
				m_alSerialData = null;
				m_alSerialData = voMyBill.getSNsClone();
			}
		}

		// 当前是列表形式时，首先切换到表单形式
		if (BillMode.List == m_iCurPanel) onSwitch();

		// 当前正显示单据，则置为可编辑方式。无单据显示时此按钮不可用。
		if (BillMode.Browse == m_iMode) {
			getBillCardPanel().updateValue();
			getBillCardPanel().setEnabled(true);

			// v5 lj
			if (m_bRefBills == true) m_iMode = BillMode.New;
			else m_iMode = BillMode.Update;

			setButtonStatus(false);
		}
		// 保存当前的货位数据，以防取消操作。 useless in fact
		m_alLocatorDataBackup = m_alLocatorData;
		// 保存当前的序列号数据，以防取消操作。 useless in fact
		m_alSerialDataBackup = m_alSerialData;
		// 如果来源单据不为空，设置业务类型等不可用

		ctrlSourceBillUI(false);
		// 修改状态时，仓库不可修改。
		if (getBillCardPanel().getHeadItem(m_sMainWhItemKey) != null) getBillCardPanel().getHeadItem(m_sMainWhItemKey).setEnabled(false);
		if (getBillCardPanel().getHeadItem(m_sWasteWhItemKey) != null) getBillCardPanel().getHeadItem(m_sWasteWhItemKey).setEnabled(false);
		// 修改状态时，库存组织不可修改。
		if (getBillCardPanel().getHeadItem(m_sMainCalbodyItemKey) != null) getBillCardPanel().getHeadItem(m_sMainCalbodyItemKey).setEnabled(false);
		if (getBillCardPanel().getHeadItem(m_sMainCalbodywasteItemKey) != null) getBillCardPanel().getHeadItem(m_sMainCalbodywasteItemKey).setEnabled(false);
		if (getBillCardPanel().getHeadItem("vbillcode") != null) getBillCardPanel().getHeadItem("vbillcode").setEnabled(false);

		// 更新按纽
		updateButtons();

		// 根据表头退库标志，确定退库状态，和单据上退库UI add by hanwei 2003-10-19
		nc.ui.ic.pub.bill.GeneralBillUICtl.setSendBackBillState(this);
		// showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill",
		// "UPP4008bill-000023")/* @res "就绪@" */);
		// 默认不是导入数据 add by hanwei 2003-10-30
		m_bIsImportData = false;

		// if current bill is barcode manage, check if flag is "open", then set
		// the file's flag to "close"; when save, first check
		OffLineCtrl ctrl = new OffLineCtrl(this);
		ctrl.onUpdateBill(voMyBill, getCorpPrimaryKey());

		setUpdateBillInitData();
	}

	/**
	 * 创建者：王乃军 功能：删除后续列表界面处理 参数： 返回： 例外： 日期：(2001-5-9 9:23:32)
	 * 修改日期，修改人，修改原因，注释标志：
	 */
	protected void removeBillsOfList(int[] iaDelLines) {

		if (iaDelLines != null && iaDelLines.length > 0) {
			// 删除界面上的数据
			getBillListPanel().getHeadBillModel().delLine(iaDelLines);
			for (int i = iaDelLines.length - 1; i >= 0; i--)
				// 从m_alListData中删除
				if (m_alListData != null && m_alListData.size() > iaDelLines[i]) m_alListData.remove(iaDelLines[i]);

			// 重置数量
			if (m_alListData != null) m_iBillQty = m_alListData.size();
			else {
				m_alListData = new ArrayList();
				m_iBillQty = 0;
			}
			// 如果删除了最后一张，或者同时删除多张，则指向第一张；
			// 如果只删除了其中一张，则指向下一张,还应该是m_iLastSelListHeadRow
			if (m_iBillQty > 0) {
				if (m_iLastSelListHeadRow >= m_iBillQty || iaDelLines.length > 1) setLastHeadRow(0);
				// 选中列表单据
				selectListBill(m_iLastSelListHeadRow);

			} else { // 全删除了，清界面
				m_alListData = new ArrayList();
				setLastHeadRow(-1);
				m_iCurDispBillNum = -1;
				m_iBillQty = 0;
				// 清空列表
				getBillListPanel().getHeadBillModel().clearBodyData();
				getBillListPanel().getBodyBillModel().clearBodyData();
				// 清空表单
				getBillCardPanel().getBillData().clearViewData();
			}

		}
	}

	/**
	 * 创建者：王乃军 功能：重置表体辅助--货位/序列号 参数： iBillNum：单据序号 返回： 例外： 日期：(2001-5-9 9:23:32)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	protected void resetBodyAssistData(int iBillNum) {
		// 置入各行的货位分配数据
		// 未改选其它单据，则无需重设数据，以提高效率。
		if (iBillNum >= 0 && m_alListData != null && m_alListData.size() > iBillNum && m_alListData.get(iBillNum) != null) { // 重置当前的货位数据
			m_alLocatorData = new ArrayList();
			// 重置当前的序列号数据
			m_alSerialData = new ArrayList();
			// 单据
			GeneralBillVO hvo = (GeneralBillVO) m_alListData.get(iBillNum);
			if (hvo != null) { // 单据体
				GeneralBillItemVO ivo[] = (GeneralBillItemVO[]) hvo.getChildrenVO();
				// 货位数据
				LocatorVO[] lvo = null;
				// 序列号数据
				SerialVO[] svo = null;
				if (ivo != null) for (int line = 0; line < ivo.length; line++) {
					lvo = ivo[line].getLocatorClone();
					m_alLocatorData.add(lvo);
					svo = ivo[line].getSerial();
					m_alSerialData.add(svo);
				}
			}

		}

	}

	/**
	 * 创建者：王乃军 功能：选中列表形式下的第sn张单据 参数：sn 单据序号
	 * 
	 * 返回： 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 */
	public void selectListBill(int sn) {
		nc.vo.ic.pub.bill.Timer timer = new nc.vo.ic.pub.bill.Timer();
		timer.start("@@方法selectListBill开始");
		if (sn < 0 || m_alListData == null || sn >= m_alListData.size() || m_alListData.get(sn) == null || getBillListPanel().getHeadTable().getRowCount() <= 0) {
			SCMEnv.out("sn error,or list null");
			return;
		}
		// 选中表头行
		getBillListPanel().getHeadTable().changeSelection(sn, 0, false, false);
		getBillListPanel().getHeadTable().setRowSelectionInterval(sn, sn);
		// 对应的表体数据
		GeneralBillVO voBill = (GeneralBillVO) m_alListData.get(sn);

		GeneralBillItemVO voi[] = voBill.getItemVOs();
		// if null,query the items.
		if (voi == null || voi.length == 0) {
			qryItems(new int[] {
				sn
			}, new String[] {
				voBill.getPrimaryKey()
			});
		}
		// re-get
		voBill = (GeneralBillVO) m_alListData.get(sn);
		getBillListPanel().getHeadBillModel().setBodyRowVO(voBill.getParentVO(), sn);
		// zhy 不重算换算率和生产日期
		// // 需要的话，重算非固定换算率
		// voBill.calConvRate();
		// // 需要的话，计算生产日期
		// voBill.calPrdDate();

		voi = voBill.getItemVOs();
		// 检查是否有表体，如果没有提示单据可能被删除了,但并不返回。
		if (voi == null || voi.length <= 0) {
			showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000024")/*
																											* @res "未找到表体信息，可能单据已被删除。"
																											*/);
		}
		// 执行批次档案公式
		BatchCodeDefSetTool.execFormulaForBatchCode(voi);
		// ------------
		setListBodyData(voi);
		// abstract method.
		// 显示货位
		dispSpace(sn);
		selectBillOnListPanel(sn);
		// 执行公式
		// getBillListPanel().getBodyBillModel().execLoadFormula();
		//
		// 设置货位按钮
		setBtnStatusSpace(false);

		setLastHeadRow(sn);

		// 抽象方法调用
		setButtonsStatus(m_iMode);
		setExtendBtnsStat(m_iMode);
		// 设置签字按钮是否可用。
		setBtnStatusSign(false);

		// 如果来源单据不为空，设置业务类型等不可用
		ctrlSourceBillButtons(true);

		updateButtons();

		// 设置条码框的当前条码数据VO add by hanwei 用于初始化条码框唯一校验数据
		if (m_utfBarCode != null) m_utfBarCode.setCurBillItem(voi);

		timer.showExecuteTime("@@方法selectListBill时间");

	}

	/**
	 * 
	 * 方法功能描述：递归修改某个按钮的所有下级按钮的状态。
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @param btn
	 *            要修改的按钮
	 * @param enabled
	 *            修改后的状态
	 *            <p>
	 * @author duy
	 * @time 2007-3-27 下午03:49:02
	 */
	private void setButtonStatusCascade(ButtonObject btn, boolean enabled) {
		if (btn == null) return;

		ButtonObject[] children = btn.getChildButtonGroup();
		for (int i = 0; i < children.length; i++) {
			setButtonStatusCascade(children[i], enabled);
			children[i].setEnabled(enabled);
		}
	}

	/**
	 * 支持参照生成多张单据的按纽控制 销售出,调拨,采购单据使用,只有修改和取消可用
	 * 
	 * @since v5
	 * @author ljun
	 * 
	 */
	public void setRefBtnStatus() {
		// 参照生成多张，且当前是列表，控制很多按钮不能用，只有取消和修改可用，同时双击表头列相当于切换操作
		if (m_bRefBills == true && m_iCurPanel == BillMode.List) {
			// 控制参照多张单据,且当前是列表状态时的按钮状态
			getButtonTree().getButton(ICButtonConst.BTN_BUSINESS_TYPE).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_ADD).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_BILL).setEnabled(true);
			setButtonStatusCascade(getButtonTree().getButton(ICButtonConst.BTN_BILL), false);
			getButtonTree().getButton(ICButtonConst.BTN_BILL_EDIT).setEnabled(true);
			getButtonTree().getButton(ICButtonConst.BTN_BILL_CANCEL).setEnabled(true);
			getButtonTree().getButton(ICButtonConst.BTN_QUERY).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_BROWSE).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_SWITCH).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_PRINT).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_ASSIST_FUNC).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_ASSIST_QUERY).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_EXPORT_IMPORT).setEnabled(false);

		}
		// 卡片下且是参照生成，切换按钮可用
		if (m_bRefBills == true && m_iCurPanel == BillMode.Card) {
			// 其余按钮类似新增时的按钮处理
			m_iMode = BillMode.New;

			// setButtonStatus(true);
			//
			getButtonTree().getButton(ICButtonConst.BTN_SWITCH).setEnabled(true);
			// updateButton(getButtonTree().getButton(ICButtonConst.BTN_SWITCH));

			// 拷贝了setButtonStatus(true)方法的部分代码
			getButtonTree().getButton(ICButtonConst.BTN_ADD).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_ASSIST_FUNC_PICKUP_AUTO).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_BUSINESS_TYPE).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_BILL_COPY).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_BILL_EDIT).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_BILL_DELETE).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_SAVE).setEnabled(true);
			getButtonTree().getButton(ICButtonConst.BTN_BILL_CANCEL).setEnabled(true);
			getButtonTree().getButton(ICButtonConst.BTN_QUERY).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_BROWSE_LOCATE).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_SWITCH).setEnabled(false);

			getButtonTree().getButton(ICButtonConst.BTN_LINE_ADD).setEnabled(true);
			getButtonTree().getButton(ICButtonConst.BTN_LINE_DELETE).setEnabled(true);
			getButtonTree().getButton(ICButtonConst.BTN_LINE_COPY).setEnabled(true);
			getButtonTree().getButton(ICButtonConst.BTN_LINE_PASTE).setEnabled(true);
			getButtonTree().getButton(ICButtonConst.BTN_LINE_INSERT).setEnabled(true);

			// getButtonTree().getButton(ICButtonConst.BTN_LINE_SPACE).setEnabled(true);
			// getButtonTree().getButton(ICButtonConst.BTN_LINE_SERIAL).setEnabled(true);

			getButtonTree().getButton(ICButtonConst.BTN_SIGN).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_EXECUTE_SIGN_CANCEL).setEnabled(false);

			getButtonTree().getButton(ICButtonConst.BTN_LINE).setEnabled(true);

			getButtonTree().getButton(ICButtonConst.BTN_PRINT).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_PRINT_PRINT).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_PRINT_PREVIEW).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_PRINT_SPACE).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_PRINT_SUM).setEnabled(false);

			getButtonTree().getButton(ICButtonConst.BTN_BROWSE).setEnabled(true);
			getButtonTree().getButton(ICButtonConst.BTN_ASSIST_FUNC).setEnabled(true);
			getButtonTree().getButton(ICButtonConst.BTN_ASSIST_QUERY).setEnabled(true);
			getButtonTree().getButton(ICButtonConst.BTN_EXPORT_IMPORT).setEnabled(true);

			// 外设输入支持
			getButtonTree().getButton(ICButtonConst.BTN_ASSIST_FUNC_REFER_IN).setEnabled(true);
			getButtonTree().getButton(ICButtonConst.BTN_IMPORT_BILL).setEnabled(true);
			getButtonTree().getButton(ICButtonConst.BTN_ASSIST_FUNC_INV_CHECK).setEnabled(true);
			getButtonTree().getButton(ICButtonConst.BTN_ASSIST_FUNC_DOCUMENT).setEnabled(false);
			// 控制翻页按钮的状态：
			m_pageBtn.setPageBtnVisible(false);
			getButtonTree().getButton(ICButtonConst.BTN_LINE_AUTO_FILL).setEnabled(true);
			getButtonTree().getButton(ICButtonConst.BTN_ASSIST_FUNC_PICKUP_AUTO).setEnabled(true);

			// 在新增情况下和修改情况条码框可以编辑
			if (m_utfBarCode != null) m_utfBarCode.setEnabled(true);

		}

	}

	/**
	 * 创建者：王乃军 功能：设置表体显示的 参数： 返回： 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	protected void setBodyInvValue(int row, InvVO voInv) {
		if (voInv == null) return;
		try {
			getBillCardPanel().setBodyValueAt((Object) voInv, row, "invvo");

			// if (getBillCardPanel().getBodyItem(m_sInvMngIDItemKey) != null)
			getBillCardPanel().setBodyValueAt(voInv.getCinventoryid(), row, m_sInvMngIDItemKey);

			getBillCardPanel().setBodyValueAt(voInv.getCinventorycode(), row, m_sInvCodeItemKey);

			// if (getBillCardPanel().getBodyItem("invname") != null)
			getBillCardPanel().setBodyValueAt(voInv.getInvname(), row, "invname");

			// if (getBillCardPanel().getBodyItem("invspec") != null)
			getBillCardPanel().setBodyValueAt(voInv.getInvspec(), row, "invspec");
			// if (getBillCardPanel().getBodyItem("invtype") != null)
			getBillCardPanel().setBodyValueAt(voInv.getInvtype(), row, "invtype");
			// if (getBillCardPanel().getBodyItem("measdocname") != null)
			getBillCardPanel().setBodyValueAt(voInv.getMeasdocname(), row, "measdocname");
			// if (getBillCardPanel().getBodyItem("castunitid") != null)
			getBillCardPanel().setBodyValueAt(voInv.getCastunitid(), row, "castunitid");

			if (getBillCardPanel().getBodyItem("npacknum") != null) {
				getBillCardPanel().setBodyValueAt(voInv.getNPacknum(), row, "npacknum");
				// m_voBill.setItemValue(row,"npacknum",voInv.getNPacknum());
			}
			if (getBillCardPanel().getBodyItem("vpacktypename") != null) {
				getBillCardPanel().setBodyValueAt(voInv.getVpacktypename(), row, "vpacktypename");
				// m_voBill.setItemValue(row,"vpacktypename",voInv.getVpacktypename());
			}
			if (getBillCardPanel().getBodyItem("pk_packsort") != null) {
				getBillCardPanel().setBodyValueAt(voInv.getPk_packsort(), row, "pk_packsort");
				// m_voBill.setItemValue(row,"pk_packsort",voInv.getPk_packsort());
			}

			// 缺省辅计量名称
			// if (getBillCardPanel().getBodyItem("castunitname") != null)
			getBillCardPanel().setBodyValueAt(voInv.getCastunitname(), row, "castunitname");
			getBillCardPanel().setBodyValueAt(voInv.getCinvmanid(), row, "cinvbasid");

			if (getBillCardPanel().getBodyItem("unitweight") != null) getBillCardPanel().setBodyValueAt(voInv.getNunitweight(), row, "unitweight");

			if (getBillCardPanel().getBodyItem("unitvolume") != null) getBillCardPanel().setBodyValueAt(voInv.getNunitvolume(), row, "unitvolume");

			if (voInv.getHsl() != null) {
				getBillCardPanel().setBodyValueAt(voInv.getHsl(), row, "hsl");
				// 换算率
				m_voBill.setItemValue(row, "hsl", voInv.getHsl());
			}
			// 是否固定换算
			m_voBill.setItemValue(row, "isSolidConvRate", voInv.getIsSolidConvRate());
			// 计划价
			if (voInv.getNplannedprice() != null) getBillCardPanel().setBodyValueAt(voInv.getNplannedprice(), row, m_sPlanPriceItemKey);
			// 计划金额
			Object oTempNum = getBillCardPanel().getBodyValueAt(row, m_sNumItemKey);
			UFDouble dNum = null;
			UFDouble dMny = null;

			// 同时有数量和单价时，才计算
			if (oTempNum != null && voInv.getNplannedprice() != null) {
				dNum = (UFDouble) oTempNum;
				dMny = dNum.multiply((UFDouble) voInv.getNplannedprice());
			} else dMny = null;

			if (dMny != null) getBillCardPanel().setBodyValueAt(dMny, row, m_sPlanMnyItemKey);

			// 清界面自由项显示。
			String sVfree0 = voInv.getFreeItemVO().getVfree0();
			if (sVfree0 != null && sVfree0.trim().length() > 0) getBillCardPanel().setBodyValueAt(sVfree0, row, "vfree0");
			else getBillCardPanel().setBodyValueAt("", row, "vfree0");

			if (voInv.getVbatchcode() != null) getBillCardPanel().setBodyValueAt(voInv.getVbatchcode(), row, "vbatchcode");

			execEditFomulas(row, m_sInvCodeItemKey);
			execEditFomulas(row, m_sInvMngIDItemKey);

			// 设置计量称

			nc.ui.pub.beans.UIRefPane refMeasware = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getBodyItem("cmeaswarename").getComponent());
			if (refMeasware != null) {
				if (m_iBillInOutFlag == InOutFlag.IN) refMeasware.setPK(voInv.getCrkjlc());
				else refMeasware.setPK(voInv.getCckjlc());
				/** 强制执行表体行，数量列的公式 */
				if (refMeasware.getRefModel() != null && refMeasware.getRefModel().getClass().getName().equals("nc.ui.mm.pub.pub1010.JlcRefModel")) execEditFomulas(row, "cmeaswarename");
			}

		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
		}
	}

	/**
	 * 创建者：王乃军 功能：用于修改后设置新增行的PK,并同时刷新传入的VO. 要保证VO中Item的顺序和界面数据一致。 参数： 返回： 例外：
	 * 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	protected void setBodyPkAfterUpdate(ArrayList alBodyPK) {
		nc.ui.pub.bill.BillModel bmTemp = getBillCardPanel().getBillModel();
		if (bmTemp == null) {
			SCMEnv.out("bm null ERROR!");
			return;
		}
		if (alBodyPK == null || alBodyPK.size() == 0) {
			SCMEnv.out("no row add.");
			return;
		}
		int rowCount = getBillCardPanel().getRowCount();
		// int length = 0;
		int iRowStatus = nc.ui.pub.bill.BillModel.ADD;

		int pk_count = 0;

		getBillCardPanel().getBillModel().setNeedCalculate(false);

		for (int row = 0; row < rowCount; row++) {
			iRowStatus = bmTemp.getRowState(row);
			if (nc.ui.pub.bill.BillModel.ADD == iRowStatus) {
				if ((pk_count + 1) >= alBodyPK.size()) {
					SCMEnv.out("return PK size err.");
					return;
				}
				// 第0个是表头PK,
				getBillCardPanel().setBodyValueAt(alBodyPK.get(pk_count + 1), row, m_sBillRowItemKey);
				pk_count++;
			}
		}
		getBillCardPanel().getBillModel().setNeedCalculate(true);
		getBillCardPanel().getBillModel().reCalcurateAll();

	}

	/**
	 * 创建者：王乃军 功能：设置按钮状态。 参数： 返回： 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */

	protected void setButtonStatus() {
		setButtonStatus(true);
	}

	/**
	 * 创建者：王乃军 功能：设置新增单据的初始数据，如日期，制单人等。 参数： 返回： 例外： 日期：(2001-5-9 9:23:32)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	protected void setNewBillInitData() {
		try {
			// ----------------------------- tail values
			// -----------------------------
			if (getBillCardPanel().getTailItem("coperatorid") != null) getBillCardPanel().setTailItem("coperatorid", m_sUserID);
			if (getBillCardPanel().getTailItem("coperatorname") != null) getBillCardPanel().setTailItem("coperatorname", m_sUserName);

			if (getBillCardPanel().getTailItem("clastmodiid") != null) getBillCardPanel().setTailItem("clastmodiid", m_sUserID);
			if (getBillCardPanel().getTailItem("clastmodiname") != null) getBillCardPanel().setTailItem("clastmodiname", m_sUserName);
			UFDateTime ufdPre = new UFDateTime(System.currentTimeMillis());
			if (getBillCardPanel().getTailItem("tlastmoditime") != null) getBillCardPanel().setTailItem("tlastmoditime", ufdPre.toString());

			if (getBillCardPanel().getTailItem(IItemKey.TFIRSTTIME) != null) getBillCardPanel().setTailItem(IItemKey.TFIRSTTIME, ufdPre.toString());

			if (m_voBill != null) {
				m_voBill.setHeaderValue("coperatorid", m_sUserID);
				m_voBill.setHeaderValue("coperatorname", m_sUserName);
				m_voBill.setHeaderValue("clastmodiid", m_sUserID);
				m_voBill.setHeaderValue("clastmodiname", m_sUserName);
				m_voBill.setHeaderValue("tlastmoditime", ufdPre.toString());
				m_voBill.setHeaderValue(IItemKey.TFIRSTTIME, ufdPre.toString());
			}
			// ------------------ head values --------------------------------
			if (getBillCardPanel().getHeadItem("dbilldate") != null) getBillCardPanel().setHeadItem("dbilldate", m_sLogDate);
			if (getBillCardPanel().getHeadItem("pk_corp") != null) getBillCardPanel().setHeadItem("pk_corp", m_sCorpID);
			if (!m_bIsEditBillCode && getBillCardPanel().getHeadItem("vbillcode") != null) getBillCardPanel().setHeadItem("vbillcode", m_sCorpID);
			if (m_voBill != null) {
				m_voBill.setHeaderValue("dbilldate", m_sLogDate);
				m_voBill.setHeaderValue("pk_corp", m_sCorpID);
				m_voBill.setHeaderValue("vbillcode", null);
			}

			GeneralBillUICtl.setWHSManagerAndDept(getBillCardPanel(), m_voBill);

		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);

		}

	}

	/**
	 * 创建者：cqw 功能：设置修改单据的初始数据 参数： 返回： 例外： 日期：(2005-04-04 19:23:32)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	protected void setUpdateBillInitData() {
		try {
			// ----------------------------- tail values
			// -----------------------------

			if (getBillCardPanel().getTailItem("clastmodiid") != null) getBillCardPanel().setTailItem("clastmodiid", m_sUserID);
			if (getBillCardPanel().getTailItem("clastmodiname") != null) getBillCardPanel().setTailItem("clastmodiname", m_sUserName);
			UFDateTime ufdPre = new UFDateTime(System.currentTimeMillis());
			if (getBillCardPanel().getTailItem("tlastmoditime") != null) getBillCardPanel().setTailItem("tlastmoditime", ufdPre.toString());
			if (m_voBill != null) {
				m_voBill.setHeaderValue("clastmodiid", m_sUserID);
				m_voBill.setHeaderValue("clastmodiname", m_sUserName);
				m_voBill.setHeaderValue("tlastmoditime", ufdPre.toString());
			}

		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);

		}

	}

	/**
	 * 创建者：yudaying 功能：设置表尾显示数据,从m_voBill中取数。浏览状态下要重读现存量 参数： 返回： 例外：
	 * 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	protected void setTailValue(int row) {
		if (m_voBill == null || row < 0 || row >= m_voBill.getItemCount()) {
			SCMEnv.out("no vobill.no taildata");
			return;
		}
		// 修改 by hanwei 2003-11-13
		if (m_voBill.getItemInv(row) == null || m_voBill.getItemInv(row).getCinventoryid() == null) {
			setTailValue(null);
			return;
		}

		InvVO voInv = m_voBill.getItemInv(row);
		// 表头的库存组织和仓库
		BillItem biCalbody = getBillCardPanel().getHeadItem(m_sMainCalbodyItemKey);
		BillItem biWarehouse = getBillCardPanel().getHeadItem(m_sMainWhItemKey);

		String pk_calbody = null;
		String sWhID = null;
		if (biCalbody != null) pk_calbody = (String) biCalbody.getValueObject();
		if (biWarehouse != null) sWhID = (String) biWarehouse.getValueObject();
		String invid = voInv.getCinventoryid();

		// if (m_sCorpID == null || sWhID == null || invid == null) {

		// return;
		// }

		// Object oWhQty = null;
		// Object oTotalQty = null;
		Object nmaxstocknum = null;
		Object nminstocknum = null;
		Object norderpointnum = null;
		Object nsafestocknum = null;
		Object nchzl = null;
		// 取当前vo中的数据
		// oWhQty = m_voBill.getItemValue(row, "bkxcl");
		// oTotalQty = m_voBill.getItemValue(row, "xczl");
		// nmaxstocknum = m_voBill.getItemValue(row, "nmaxstocknum");
		// nminstocknum = m_voBill.getItemValue(row, "nminstocknum");
		// norderpointnum = m_voBill.getItemValue(row, "norderpointnum");
		// nsafestocknum = m_voBill.getItemValue(row, "nsafestocknum");

		// 浏览状态下要刷新现存量，如果已经读过，则不必重读。
		// 在编辑--〉浏览切换时，切换浏览单据时要清空库存量。
		// 修改状态下，选中了原有的行也要读现存量。
		BillItem biMax = getBillCardPanel().getTailItem("nmaxstocknum");
		BillItem biMin = getBillCardPanel().getTailItem("nminstocknum");
		BillItem biOpt = getBillCardPanel().getTailItem("norderpointnum");
		BillItem biSafe = getBillCardPanel().getTailItem("nsafestocknum");
		BillItem biWhQty = getBillCardPanel().getTailItem("bkxcl");
		BillItem biBdQty = getBillCardPanel().getTailItem("xczl");

		int iFlag = 0;
		if (((biMax != null && biMax.isShow()) || (biMin != null && biMin.isShow()) || (biOpt != null && biOpt.isShow()) || (biSafe != null && biSafe.isShow())) && m_sCorpID != null && sWhID != null && invid != null) {

			// 查询控制存量
			nmaxstocknum = voInv.getNmaxstocknum();
			nminstocknum = voInv.getNminstocknum();
			norderpointnum = voInv.getNorderpointnum();
			nsafestocknum = voInv.getNsafestocknum();
			nchzl = m_voBill.getItemInv(row).getChzl();
			if (nmaxstocknum == null && nminstocknum == null && norderpointnum == null && nsafestocknum == null && voInv.getBkxcl() == null && voInv.getXczl() == null && nchzl == null) {

				iFlag += 1;
			}

		}
		if (((biWhQty != null && biWhQty.isShow()) || (biBdQty != null && biBdQty.isShow())) && m_sCorpID != null && sWhID != null && invid != null) {
			iFlag += 2;
		}
		switch (iFlag) {
		case 0:
			break;
		case 1:
			iFlag = QryInfoConst.QTY_CTRL;
			break;
		case 2:
			iFlag = QryInfoConst.QTY_ONHAND;
			break;
		case 3:
			iFlag = QryInfoConst.QTY_ALL;
			break;
		}
		if (iFlag > 0) {
			ArrayList alIDs = new ArrayList();
			alIDs.add(sWhID);
			alIDs.add(invid);
			alIDs.add(pk_calbody);
			alIDs.add(m_sCorpID);
			// 查询存货信息
			try {
				SCMEnv.out("setTailValue1(int) ,iflag= " + iFlag);
				InvVO voInvTmp = (InvVO) GeneralBillHelper.queryInfo(new Integer(iFlag), alIDs);
				if (voInvTmp != null) {
					if (iFlag == QryInfoConst.QTY_CTRL || iFlag == QryInfoConst.QTY_ALL) {
						voInv.setNmaxstocknum(voInvTmp.getNmaxstocknum());
						voInv.setNminstocknum(voInvTmp.getNminstocknum());
						voInv.setNorderpointnum(voInvTmp.getNorderpointnum());
						voInv.setNsafestocknum(voInvTmp.getNsafestocknum());
						voInv.setNplannedprice(voInvTmp.getNplannedprice());
					}
					if (iFlag == QryInfoConst.QTY_ONHAND || iFlag == QryInfoConst.QTY_ALL) {
						voInv.setBkxcl(voInvTmp.getBkxcl());
						voInv.setXczl(voInvTmp.getXczl());
					}
					voInv.setChzl(new UFDouble(0));
					m_voBill.setItemInv(row, voInv);
					// m_voBill.getItemInv(row).setChzl(new UFDouble(0));
				}
			} catch (Exception e) {
				nc.vo.scm.pub.SCMEnv.error(e);
			}

		}

		// 设置界面显示
		if (biMax != null) {
			biMax.setValue(voInv.getNmaxstocknum());
		}
		if (biMin != null) biMin.setValue(voInv.getNminstocknum());
		if (biOpt != null) biOpt.setValue(voInv.getNorderpointnum());
		if (biSafe != null) biSafe.setValue(voInv.getNsafestocknum());
		if (biWhQty != null) biWhQty.setValue(voInv.getBkxcl());
		if (biBdQty != null) biBdQty.setValue(voInv.getXczl());

		/*
		 * if ((BillMode.Browse == m_iMode || BillMode.Update == m_iMode) &&
		 * (oWhQty == null || oWhQty.toString().length() == 0) && (oTotalQty ==
		 * null || oTotalQty.toString().length() == 0) && (nmaxstocknum == null
		 * || nmaxstocknum.toString().length() == 0) && (nminstocknum == null ||
		 * nminstocknum.toString().length() == 0) && (norderpointnum == null ||
		 * norderpointnum.toString().length() == 0) && (nsafestocknum == null ||
		 * nsafestocknum.toString().length() == 0)) { //从vo中读存货id Object oInvID
		 * = m_voBill.getItemValue(row, "cinventoryid"); if (oInvID == null) {
		 * SCMEnv.out(row + "row data ERR"); return; }
		 * //((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
		 * //.getBodyItem("cinventorycode") //.getComponent()) //.getRefPK();
		 * Object oWhID = null; oWhID =
		 * m_voBill.getHeaderValue(m_sMainWhItemKey); //参数 ArrayList alIDs = new
		 * ArrayList(); alIDs.add(oWhID); alIDs.add(oInvID); ArrayList alQty =
		 * null; //读数 try { alQty = (ArrayList) invokeClient("queryInfo", new
		 * Class[] { Integer.class, Object.class }, new Object[] { new
		 * Integer(QryInfoConst.QTY), alIDs }); } catch (Exception e) {
		 * nc.vo.scm.pub.SCMEnv.error(e); } //
		 * 数据格式：本库现存量，现存总量，最高库存量，最低库存量，订购点库存量，安全库存量 if (alQty != null &&
		 * alQty.size() >= 6) {
		 * 
		 * oWhQty = alQty.get(0); oTotalQty = alQty.get(1); nmaxstocknum =
		 * alQty.get(2); nminstocknum = alQty.get(3); norderpointnum =
		 * alQty.get(4); nsafestocknum = alQty.get(5); //写入vo保存
		 * m_voBill.setItemValue(row, "bkxcl", oWhQty);
		 * m_voBill.setItemValue(row, "xczl", oTotalQty);
		 * m_voBill.setItemValue(row, "nmaxstocknum", nmaxstocknum);
		 * m_voBill.setItemValue(row, "nminstocknum", nminstocknum);
		 * m_voBill.setItemValue(row, "norderpointnum", norderpointnum);
		 * m_voBill.setItemValue(row, "nsafestocknum", nsafestocknum); } }
		 * 
		 * //本库现存量 nc.ui.pub.bill.BillItem biTail =
		 * getBillCardPanel().getTailItem("bkxcl"); if (biTail != null) if
		 * (oWhQty != null) biTail.setValue(new
		 * nc.vo.pub.lang.UFDouble(oWhQty.toString())); else
		 * biTail.setValue(null); //现存总量 biTail =
		 * getBillCardPanel().getTailItem("xczl"); if (biTail != null) if
		 * (oTotalQty != null) biTail.setValue(new
		 * nc.vo.pub.lang.UFDouble(oTotalQty.toString())); else
		 * biTail.setValue(null); //最高库存 biTail =
		 * getBillCardPanel().getTailItem("nmaxstocknum"); if (biTail != null)
		 * if (nmaxstocknum != null) biTail.setValue(new
		 * nc.vo.pub.lang.UFDouble(nmaxstocknum.toString())); else
		 * biTail.setValue(null); //最低库存 biTail =
		 * getBillCardPanel().getTailItem("nminstocknum"); if (biTail != null)
		 * if (nminstocknum != null) biTail.setValue(new
		 * nc.vo.pub.lang.UFDouble(nminstocknum.toString())); else
		 * biTail.setValue(null); //安全库存 biTail =
		 * getBillCardPanel().getTailItem("nsafestocknum"); if (biTail != null)
		 * if (nsafestocknum != null) biTail.setValue(new
		 * nc.vo.pub.lang.UFDouble(nsafestocknum.toString())); else
		 * biTail.setValue(null); //订货点量 biTail =
		 * getBillCardPanel().getTailItem("norderpointnum"); if (biTail != null)
		 * if (norderpointnum != null) biTail.setValue(new
		 * nc.vo.pub.lang.UFDouble(norderpointnum.toString())); else
		 * biTail.setValue(null);
		 */
	}

	/**
	 * 创建者：王乃军 功能：设置表尾显示数据,传入null则清空。 参数： 返回： 例外： 日期：(2001-5-9 9:23:32)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	protected void setTailValue(InvVO voInv) {
		// 本库现存量
		nc.ui.pub.bill.BillItem biTail = null;
		biTail = getBillCardPanel().getTailItem("bkxcl");
		if (biTail != null) if (voInv != null) biTail.setValue(voInv.getBkxcl());
		else biTail.setValue(null);
		// 现存总量
		biTail = getBillCardPanel().getTailItem("xczl");
		if (biTail != null) if (voInv != null) biTail.setValue(voInv.getBkxcl());
		else biTail.setValue(null);
		// 最高库存
		biTail = getBillCardPanel().getTailItem("nmaxstocknum");
		if (biTail != null) if (voInv != null) biTail.setValue(voInv.getNmaxstocknum());
		else biTail.setValue(null);
		// 最低库存
		biTail = getBillCardPanel().getTailItem("nminstocknum");
		if (biTail != null) if (voInv != null) biTail.setValue(voInv.getNminstocknum());
		else biTail.setValue(null);
		// 安全库存
		biTail = getBillCardPanel().getTailItem("nsafestocknum");
		if (biTail != null) if (voInv != null) biTail.setValue(voInv.getNsafestocknum());
		else biTail.setValue(null);
		// 订货点量
		biTail = getBillCardPanel().getTailItem("norderpointnum");
		if (biTail != null) if (voInv != null) biTail.setValue(voInv.getNorderpointnum());
		else biTail.setValue(null);

	}

	/**
	 * 创建者：王乃军 功能：同步行数据，如货位、序列号 参数： int iFirstLine,iLastLine 行号，start from 0 int
	 * iCol 列 start from 0 int type 1: add 0: update -1:delete
	 * 
	 * 返回： 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志： 2001-06-13. 同步VO
	 * 
	 * 
	 * 
	 */
	protected void synchLineData(int iFirstLine, int iLastLine, int iCol, int iType) {
		if (iFirstLine < 0 || iLastLine < 0) return;

		// SCMEnv.out("synch line "+iType);
		if (m_alLocatorData == null) {
			SCMEnv.out("init Locator data.");
			// m_alLocatorData=new ArrayList();
			// 初始化行数据，比如在复制单据时，m_alLocatorData==null 但单据行数不为0。
			m_alLocatorData = new ArrayList(getBillCardPanel().getBillModel().getRowCount());
		}
		if (m_alSerialData == null) {
			SCMEnv.out("init serial data.");
			// m_alSerialData=new ArrayList();
			// 初始化行数据，比如在复制单据时，m_alSerialData==null 但单据行数不为0。
			m_alSerialData = new ArrayList(getBillCardPanel().getBillModel().getRowCount());
		}
		if (m_voBill == null) m_voBill = new GeneralBillVO();

		switch (iType) {
		case javax.swing.event.TableModelEvent.INSERT:// 增行：插、追加、粘贴
			m_alLocatorData.add(iFirstLine, null);
			m_alSerialData.add(iFirstLine, null);
			m_voBill.insertItem(iFirstLine);
			break;
		case javax.swing.event.TableModelEvent.UPDATE:
			break;
		case javax.swing.event.TableModelEvent.DELETE:
			if (iFirstLine >= 0) {
				if (m_alLocatorData.size() > iFirstLine) m_alLocatorData.remove(iFirstLine);
				if (m_alSerialData.size() > iFirstLine) m_alSerialData.remove(iFirstLine);
				if (m_voBill.getItemCount() > iFirstLine) m_voBill.removeItem(iFirstLine);
			}
			break;
		}
	}

	/**
	 * This fine grain notification tells listeners the exact range of cells,
	 * rows, or columns that changed.
	 */
	public void tableChanged(javax.swing.event.TableModelEvent e) {
		// SCMEnv.out(
		// "heihei,edit Line "
		// + e.getFirstRow()
		// + " to "
		// + e.getLastRow()
		// + " type is "
		// + e.getType());
		// try的目的是保证addListener被执行。
		try {
			getBillCardPanel().getBillModel().removeTableModelListener(this);
			if (e.getType() != javax.swing.event.TableModelEvent.UPDATE && e.getSource() == getBillCardPanel().getBillModel()) synchLineData(e.getFirstRow(), e.getLastRow(), e.getColumn(), e.getType());

			// //期初单时，清空货位
			// if (e.getType() == javax.swing.event.TableModelEvent.INSERT
			// && e.getSource() == getBillCardPanel().getBillModel()) {
			// if (getBillCardPanel().getBodyItem("vspacename") != null)
			// getBillCardPanel().setBodyValueAt(null, e.getFirstRow(),
			// "vspacename");
			// if (getBillCardPanel().getBodyItem("cspaceid") != null)
			// getBillCardPanel().setBodyValueAt(null, e.getFirstRow(),
			// "cspaceid");
			// }

		} catch (Exception eeee) {
			SCMEnv.out(eeee.getMessage());
		} finally {
			getBillCardPanel().getBillModel().addTableModelListener(this);
		}
	}

	/**
	 * 创建者：王乃军 功能：用表单形式下的单据刷新列表数据，用于修改保存后。 参数： 返回： 例外： 日期：(2001-5-9 9:23:32)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	protected void updateBillToList(GeneralBillVO bvo) {
		if (m_iLastSelListHeadRow < 0 || bvo == null || bvo.getParentVO() == null || bvo.getChildrenVO() == null) {
			SCMEnv.out("Bill is null !");
			return;
		}
		// 刷新数据
		m_alListData.set(m_iLastSelListHeadRow, bvo.clone());
		// 刷新列表界面显示
		GeneralBillHeaderVO voh[] = new GeneralBillHeaderVO[m_alListData.size()];
		for (int i = 0; i < m_alListData.size(); i++) {
			if (m_alListData.get(i) != null) voh[i] = (GeneralBillHeaderVO) ((GeneralBillVO) m_alListData.get(i)).getParentVO();
			else SCMEnv.out("list data error!-->" + i);

		}

		setListHeadData(voh);
		// 选中列表单据，以刷新表体显示
		selectListBill(m_iLastSelListHeadRow);

	}

	/**
	 * 创建者：王乃军 功能：单据编辑事件处理 参数：e单据编辑事件 返回： 例外： 日期：(2001-5-8 19:08:05)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	public void afterAstUOMEdit(nc.ui.pub.bill.BillEditEvent e) {
		int rownum = e.getRow();
		String castunit = (String) getBillCardPanel().getBodyValueAt(rownum, "castunitname");

		clearRowData(0, rownum, "castunitid");

		boolean isFixFlag = false;
		Integer isSolidConvRate = null;
		Integer isstorebyconvert = null;
		Integer issupplierstock = null;

		if (castunit != null && castunit.trim().length() > 0) {

			UFDouble hsl = null;
			// 辅计量单位
			nc.ui.pub.beans.UIRefPane refCastunit = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getBodyItem("castunitname").getComponent());

			String sPK = refCastunit.getRefPK();
			String sName = refCastunit.getRefName();

			m_voBill.setItemValue(rownum, "castunitid", sPK);
			m_voBill.setItemValue(rownum, "castunitname", sName);
			getBillCardPanel().setBodyValueAt(sName, rownum, "castunitname");
			getBillCardPanel().setBodyValueAt(sPK, rownum, "castunitid");

			InvVO invvo = m_voBill.getItemInv(rownum);
			getInvoInfoBYFormula().getInVoOfHSLByHashCach(new InvVO[] {
				invvo
			});
			if (invvo != null) {
				hsl = invvo.getHsl();
				getBillCardPanel().setBodyValueAt(hsl, rownum, "hsl");
				hsl = (UFDouble) getBillCardPanel().getBodyValueAt(rownum, "hsl");
				m_voBill.setItemValue(rownum, "hsl", hsl);

				isSolidConvRate = invvo.getIsSolidConvRate();
				isstorebyconvert = invvo.getIsStoreByConvert();
				issupplierstock = invvo.getIssupplierstock();
				m_voBill.setItemValue(rownum, "isSolidConvRate", isSolidConvRate);
				m_voBill.getItemInv(rownum).setIsSolidConvRate(isSolidConvRate);
				m_voBill.setItemValue(rownum, "isstorebyconvert", isstorebyconvert);
				m_voBill.setItemValue(rownum, "issupplierstock", issupplierstock);
			}

			// nc.vo.bd.b15.MeasureRateVO voMeas = null;
			// m_voInvMeas.getMeasInfo(m_sCorpID, m_voBill.getItemInv(rownum)
			// .getCinventoryid());
			// voMeas = m_voInvMeas.getMeasureRate(m_voBill.getItemInv(rownum)
			// .getCinventoryid(), sPK);// zhy
			// if (voMeas != null) {
			//
			// hsl = voMeas.getMainmeasrate();
			// getBillCardPanel().setBodyValueAt(hsl, rownum, "hsl");
			// hsl = (UFDouble) getBillCardPanel().getBodyValueAt(rownum,
			// "hsl");
			// m_voBill.setItemValue(rownum, "hsl", hsl);
			// isFixFlag = voMeas.getFixedflag().booleanValue();
			// m_voBill.setItemValue(rownum, "isSolidConvRate", voMeas
			// .getFixedflag());
			// m_voBill.getItemInv(rownum).setIsSolidConvRate(
			// SmartVODataUtils.getInteger(voMeas.getFixedflag()));

			// }

		}

		String[] sfields = new String[] {
				"castunitid", "hsl", m_sAstItemKey, m_sNumItemKey
		};
		GeneralBillUICtl.changeNum(getBillCardPanel(), "castunitid", rownum, sfields, isFixFlag);
		GeneralBillUICtl.synUi2Vo(getBillCardPanel(), m_voBill, sfields, rownum);

		sfields = new String[] {
				"castunitid", "hsl", "ngrossastnum", "ngrossnum"
		};
		GeneralBillUICtl.changeNum(getBillCardPanel(), "castunitid", rownum, sfields, true);
		GeneralBillUICtl.synUi2Vo(getBillCardPanel(), m_voBill, sfields, rownum);

		sfields = new String[] {
				"castunitid", "hsl", "ntareastnum", "ntarenum"
		};
		GeneralBillUICtl.changeNum(getBillCardPanel(), "castunitid", rownum, sfields, true);
		GeneralBillUICtl.synUi2Vo(getBillCardPanel(), m_voBill, sfields, rownum);

		if (!m_bIsInitBill) {

			sfields = new String[] {
					"castunitid", "hsl", m_sShouldAstItemKey, m_sShouldNumItemKey
			};
			GeneralBillUICtl.changeNum(getBillCardPanel(), "castunitid", rownum, sfields, isFixFlag);
			GeneralBillUICtl.synUi2Vo(getBillCardPanel(), m_voBill, sfields, rownum);

		}

		nc.vo.ic.pub.DesassemblyVO voDesa = (nc.vo.ic.pub.DesassemblyVO) m_voBill.getItemValue(rownum, "desainfo");
		m_voBill.setItemValue(rownum, "idesatype", new Integer(voDesa.getDesaType()));
		getBillCardPanel().setBodyValueAt(new Integer(voDesa.getDesaType()), rownum, "idesatype");
		afterNumEdit_1(rownum);

	}

	/**
	 * 创建者：王乃军 功能：取消签字成功后处理 参数： 返回： 例外： 日期：(2001-5-8 19:08:05)
	 * 
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 */
	public void freshAfterCancelSignedOK() {
		try {

			refreshSelBill(m_iLastSelListHeadRow);
			// GeneralBillVO voBill = null;
			// // 设置m_voBill,以读取控制数据。
			// if (m_iLastSelListHeadRow >= 0 && m_alListData != null
			// && m_alListData.size() > m_iLastSelListHeadRow
			// && m_alListData.get(m_iLastSelListHeadRow) != null) {
			// // 这里不能clone(),改变了m_voBill同时改变m_alListData
			// voBill = (GeneralBillVO) m_alListData
			// .get(m_iLastSelListHeadRow);
			// // 把当前登录人设置到vo
			// if (voBill != null && voBill.getHeaderVO() != null) {
			// GeneralBillHeaderVO voHeader = voBill.getHeaderVO();
			// voHeader.setCregister(null);
			// voHeader.setCregistername(null);
			// voHeader.setDaccountdate(null);
			// voHeader.setAttributeValue("taccounttime", null);
			//
			// voHeader.setCauditorid(null);
			// voHeader.setCauditorname(null);
			// voHeader.setDauditdate(null);
			//
			// voHeader.setFbillflag(new Integer(BillStatus.FREE));
			// voBill.setParentVO(voHeader);
			// }
			// // 重置列表界面
			// m_alListData.set(m_iLastSelListHeadRow, voBill);
			// }
			// // 把当前登录人置空
			// // m_voBill刷新
			// if (m_voBill != null && m_voBill.getHeaderVO() != null) {
			// GeneralBillHeaderVO voHeader = m_voBill.getHeaderVO();
			// voHeader.setCregister(null);
			// voHeader.setCregistername(null);
			// voHeader.setDaccountdate(null);
			// voHeader.setAttributeValue("taccounttime", null);
			//
			// voHeader.setCauditorid(null);
			// voHeader.setCauditorname(null);
			// voHeader.setDauditdate(null);
			//
			// voHeader.setFbillflag(new Integer(BillStatus.FREE));
			// m_voBill.setParentVO(voHeader);
			// }
			//
			// // 把界面签字人名称置空
			// if (getBillCardPanel().getTailItem("cregister") != null)
			// getBillCardPanel().getTailItem("cregister").setValue(null);
			// if (getBillCardPanel().getTailItem("cregistername") != null)
			// getBillCardPanel().getTailItem("cregistername").setValue(null);
			// if (getBillCardPanel().getTailItem("daccountdate") != null)
			// getBillCardPanel().getTailItem("daccountdate").setValue(null);
			// if (getBillCardPanel().getTailItem("taccounttime") != null)
			// getBillCardPanel().getTailItem("taccounttime").setValue(null);
			//
			// if (getBillCardPanel().getTailItem("cauditorid") != null)
			// getBillCardPanel().getTailItem("cauditorid").setValue(null);
			// if (getBillCardPanel().getTailItem("cauditorname") != null)
			// getBillCardPanel().getTailItem("cauditorname").setValue(null);
			// if (getBillCardPanel().getTailItem("dauditdate") != null)
			// getBillCardPanel().getTailItem("dauditdate").setValue(null);
			//
			// if (getBillCardPanel().getHeadItem("fbillflag") != null)
			// getBillCardPanel().getHeadItem("fbillflag").setValue(
			// BillStatus.FREE);
			// // 刷新列表形式
			// setListHeadData();
			// selectListBill(m_iLastSelListHeadRow);
			// 设置按钮状态,签字可用，取消签字不可用
			// 还应进一步判断当前的单据是否已签字
			// 未签字，所以设置按钮状态,签字可用，取消签字不可用
			setButtonStatus(false);
			getButtonTree().getButton(ICButtonConst.BTN_SIGN).setEnabled(true);
			getButtonTree().getButton(ICButtonConst.BTN_EXECUTE_SIGN_CANCEL).setEnabled(false);
			// 可删、改
			if (isCurrentTypeBill()) {
				getButtonTree().getButton(ICButtonConst.BTN_BILL_EDIT).setEnabled(true);
				getButtonTree().getButton(ICButtonConst.BTN_BILL_DELETE).setEnabled(true);
			} else {
				getButtonTree().getButton(ICButtonConst.BTN_BILL_EDIT).setEnabled(false);
				getButtonTree().getButton(ICButtonConst.BTN_BILL_DELETE).setEnabled(false);
			}
			updateButtons();

		} catch (Exception e2) {
			nc.vo.scm.pub.SCMEnv.error(e2);
		}

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
			// 当前使用者ID
			nc.ui.pub.ClientEnvironment ce = nc.ui.pub.ClientEnvironment.getInstance();
			try {
				m_sUserID = ce.getUser().getPrimaryKey();
			} catch (Exception e) {

			}
			try {
				m_sGroupID = ce.getGroupId();
			} catch (Exception e) {

			}
			// SCMEnv.out("test user id is 2011000001");
			// m_sUserID="2011000001";
			// 当前使用者姓名
			try {
				m_sUserName = ce.getUser().getUserName();
			} catch (Exception e) {

			}
			// SCMEnv.out("test user name is 张三");
			// m_sUserName="张三";
			// 公司ID
			try {
				m_sCorpID = ce.getCorporation().getPrimaryKey();
				SCMEnv.out("---->corp id is " + m_sCorpID);
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
	 * 创建者：王乃军 功能：根据当前单据的待审状态决定签字/取消签字那个可用 参数： 返回： 例外： 日期：(2001-5-9 9:23:32)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	protected void setBtnStatusSign() {
		// 只在浏览状态下并且界面上有单据时控制
		setBtnStatusSign(true);
	}

	/**
	 * 创建者：王乃军 功能：根据当前存货的属性决定序列号分配是否可用 参数： row行号 返回： 例外： 日期：(2001-5-9 9:23:32)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	protected void setBtnStatusSN(int row) {
		setBtnStatusSN(row, true);
	}

	/**
	 * 创建者：王乃军 功能：根据当前仓库的状态决定货位分配是否可用 参数： 返回： 例外： 日期：(2001-5-9 9:23:32)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	protected void setBtnStatusSpace() {
		setBtnStatusSpace(true);
	}

	/**
	 * 创建者：王乃军 功能：刷新列表形式表头数据 参数： 返回： 例外： 日期：(2001-5-8 19:08:05)
	 * 
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 2003-02-27 韩卫 添加公式处理获取仓库和废品仓库信息
	 */
	public void setListHeadData() {
		if (m_alListData != null && m_alListData.size() > 0) {
			// 刷新列表形式表头数据
			GeneralBillHeaderVO voh[] = new GeneralBillHeaderVO[m_alListData.size()];
			for (int i = 0; i < m_alListData.size(); i++) {
				if (m_alListData.get(i) != null) voh[i] = (GeneralBillHeaderVO) ((GeneralBillVO) m_alListData.get(i)).getParentVO();
				else SCMEnv.out("list data error!-->" + i);

			}
			setListHeadData(voh);
		}

	}

	/**
	 * 创建者：余大英 功能：得到当前单据vo,包含货位/序列号,包括删除的行，未修改的行只传PK. 参数： 返回： 例外： 日期：(2001-5-9
	 * 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */

	protected GeneralBillVO getBillChangedVO() {
		GeneralBillVO billVO = new GeneralBillVO();
		getBillCardPanel().getBillData().getHeaderValueVO(billVO.getParentVO());
		billVO.setChildrenVO(getBodyChangedVOs());
		return billVO;
	}

	/**
	 * 创建者：余大英 功能：得到当前单据vo,包含货位/序列号和界面上所有的数据,不包括删除的行 参数： 返回： 例外： 日期：(2001-5-9
	 * 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */

	public GeneralBillVO getBillVO() {
		GeneralBillVO billVO = new GeneralBillVO();
		getBillCardPanel().getBillData().getHeaderValueVO(billVO.getParentVO());
		billVO.setChildrenVO(getBodyVOs());
		if (m_voBill != null) {
			m_voBill.setIDItems(billVO);
			return m_voBill;
		} else return billVO;
	}

	/**
	 * 创建者：王乃军 功能：得到完整的单据表体VO，包括删除的行。 参数： 返回： 例外： 日期：(2001-5-9 9:23:32)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	protected GeneralBillItemVO[] getBodyChangedVOs() {
		nc.ui.pub.bill.BillModel bmTemp = getBillCardPanel().getBillModel();
		if (bmTemp == null) {
			SCMEnv.out("bm null ERROR!");
			return null;
		}
		Vector vBodyData = bmTemp.getDataVector();
		if (vBodyData == null || vBodyData.size() == 0) {
			SCMEnv.out("bd null ERROR!");
			return null;
		}
		// 为了得到自由项。
		GeneralBillItemVO[] voaItemForFree = m_voBill.getItemVOs();
		// 删除的行

		// vo数组的长度==当前显示的行数+删除的总行数
		int rowCount = vBodyData.size();
		int length = 0;
		Vector vDeleteRow = bmTemp.getDeleteRow();
		if (vDeleteRow != null) length = rowCount + vDeleteRow.size();
		else length = rowCount;
		// 初始化返回的vo
		GeneralBillItemVO[] voaBody = new GeneralBillItemVO[length];

		int iRowStatus = nc.ui.pub.bill.BillModel.ADD;

		// 整理当前界面上显示的行，包括原行、修改后的行、新增的行。
		// 传所有数据
		for (int i = 0; i < vBodyData.size(); i++) {
			voaBody[i] = new GeneralBillItemVO();
			iRowStatus = bmTemp.getRowState(i);
			for (int j = 0; j < bmTemp.getBodyItems().length; j++) {
				nc.ui.pub.bill.BillItem item = bmTemp.getBodyItems()[j];

				Object aValue = bmTemp.getValueAt(i, item.getKey());

				aValue = item.converType(aValue);
				// SCMEnv.out(item.getKey()+aValue);
				voaBody[i].setAttributeValue(item.getKey(), aValue);
			}
			// 设置状态
			switch (iRowStatus) {
			case nc.ui.pub.bill.BillModel.ADD: // 新增的行
				voaBody[i].setStatus(nc.vo.pub.VOStatus.NEW);
				break;
			case nc.ui.pub.bill.BillModel.MODIFICATION: // 修改后的行
				voaBody[i].setStatus(nc.vo.pub.VOStatus.UPDATED);
				break;
			case nc.ui.pub.bill.BillModel.NORMAL: // 原行
				voaBody[i].setStatus(nc.vo.pub.VOStatus.UNCHANGED);
				break;
			}
			// 货位分配数据
			if (m_alLocatorData != null && m_alLocatorData.size() > i) voaBody[i].setLocator((LocatorVO[]) m_alLocatorData.get(i));
			// 序列号数据
			if (m_alSerialData != null && m_alSerialData.size() > i) voaBody[i].setSerial((SerialVO[]) m_alSerialData.get(i));
			// 自由项
			voaBody[i].setFreeItemVO(voaItemForFree[i].getFreeItemVO());

		}
		// 删除的行处理：2003-02-09 wnj:得从原有的单据中拷贝，否则货位和序列号都没有了。
		if (vDeleteRow != null && vDeleteRow.size() > 0) {
			if (m_iLastSelListHeadRow >= 0 && m_alListData != null && m_alListData.size() > m_iLastSelListHeadRow && m_alListData.get(m_iLastSelListHeadRow) != null) {
				// =========
				int col = bmTemp.getBodyColByKey(m_sBillRowItemKey); // 表体PK
				Vector rowVector = null;
				String sItemPK = null;
				GeneralBillVO voOriginalBill = (GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow);
				GeneralBillItemVO[] voaOriginalItem = voOriginalBill.getItemVOs();
				// 查询删除的行的pk,在原单据中查找之。
				// 如果单据行较多，可以优化为hastable.
				for (int del = 0; del < vDeleteRow.size(); del++) {
					rowVector = (Vector) vDeleteRow.elementAt(del);
					sItemPK = (String) rowVector.elementAt(col);
					// 在原单据中查找之。
					if (sItemPK != null) for (int item = 0; item < voaOriginalItem.length; item++)
						if (sItemPK.equals(voaOriginalItem[item].getPrimaryKey())) voaBody[del + rowCount] = (GeneralBillItemVO) voaOriginalItem[item].clone();
					// 设置状态
					voaBody[del + rowCount].setStatus(nc.vo.pub.VOStatus.DELETED);
				}
			} else SCMEnv.out("update err,cannot dup del rows.");

		}
		return voaBody;
	}

	/**
	 * 创建者：王乃军 功能：得到修改后的vo,用于修改后的保存 参数： 返回： 例外： 日期：(2001-5-9 9:23:32)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	protected GeneralBillItemVO[] getBodyVOs() {
		nc.ui.pub.bill.BillModel bmTemp = getBillCardPanel().getBillModel();
		if (bmTemp == null) {
			SCMEnv.out("bm null ERROR!");
			return null;
		}
		Vector vBodyData = bmTemp.getDataVector();
		if (vBodyData == null || vBodyData.size() == 0) {
			SCMEnv.out("bd null ERROR!");
			return null;
		}
		// 为了得到自由项。
		GeneralBillItemVO[] voaItemForFree = m_voBill.getItemVOs();

		// vo数组的长度==当前显示的行数
		int rowCount = vBodyData.size();
		// 初始化返回的vo
		GeneralBillItemVO[] voaBody = new GeneralBillItemVO[rowCount];
		int iRowStatus = nc.ui.pub.bill.BillModel.ADD;

		// 整理当前界面上显示的行，包括原行、修改后的行、新增的行。
		for (int i = 0; i < rowCount; i++) {
			voaBody[i] = new GeneralBillItemVO();
			iRowStatus = bmTemp.getRowState(i);
			for (int j = 0; j < bmTemp.getBodyItems().length; j++) {

				nc.ui.pub.bill.BillItem item = bmTemp.getBodyItems()[j];
				Object aValue = bmTemp.getValueAt(i, item.getKey());

				aValue = item.converType(aValue);
				// SCMEnv.out(item.getKey()+" "+aValue);
				voaBody[i].setAttributeValue(item.getKey(), aValue);
			}
			// 设置状态
			switch (iRowStatus) {
			case nc.ui.pub.bill.BillModel.ADD: // 新增的行
				voaBody[i].setStatus(nc.vo.pub.VOStatus.NEW);
				break;
			case nc.ui.pub.bill.BillModel.MODIFICATION: // 修改后的行
				voaBody[i].setStatus(nc.vo.pub.VOStatus.UPDATED);
				break;
			case nc.ui.pub.bill.BillModel.NORMAL: // 原行
				voaBody[i].setStatus(nc.vo.pub.VOStatus.UNCHANGED);
				break;
			}
			// 货位分配数据
			if (m_alLocatorData != null && m_alLocatorData.size() > i) voaBody[i].setLocator((LocatorVO[]) m_alLocatorData.get(i));

			// 序列号数据
			if (m_alSerialData != null && m_alSerialData.size() > i) {
				SerialVO[] serialVOs = (SerialVO[]) m_alSerialData.get(i);
				// 设置序列号日期
				voaBody[i].updateSerialDate(serialVOs);
				// 设置序列号
				voaBody[i].setSerial(serialVOs);
			}

			// 自由项
			voaBody[i].setFreeItemVO(voaItemForFree[i].getFreeItemVO());

			// 删除的行不传
		}
		return voaBody;
	}

	/**
	 * 创建者：王乃军 功能：得到当前单据是否已签字 参数： 返回： 已签字 1 未签字 0 不能操作 -1 例外： 日期：(2001-5-9
	 * 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	public int isSigned() {
		GeneralBillVO voBill = null;
		// 设置voBill,以读取控制数据。

		if (m_iCurPanel == BillMode.List && m_iLastSelListHeadRow >= 0 && m_alListData != null && m_alListData.size() > m_iLastSelListHeadRow && m_alListData.get(m_iLastSelListHeadRow) != null) voBill = (GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow);
		else voBill = m_voBill;

		if (voBill != null) {
			int iCount = voBill.getItemCount();
			int i = 0;
			GeneralBillItemVO voaItem[] = voBill.getItemVOs();
			for (i = 0; i < iCount; i++) {
				// 测试实出/入数量
				if ((voaItem[i].getNinnum() == null || voaItem[i].getNinnum().toString().length() == 0) && (voaItem[i].getNoutnum() == null || voaItem[i].getNoutnum().toString().length() == 0)) break;

			}

			if (i < iCount) // 无数量
			return CANNOTSIGN;

			// 是否有签字人
			String sSignerID = ((GeneralBillHeaderVO) voBill.getHeaderVO()).getCregister();
			if (sSignerID != null && sSignerID.trim().length() > 0) return SIGNED;
			else return NOTSIGNED;
		} else return CANNOTSIGN;
	}

	/**
	 * 创建者：王乃军 功能：在表单设置显示VO 参数： 返回： 例外： 日期：(2001-5-9 9:23:32)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	public void setBillVO(GeneralBillVO bvo) {
		setBillVO(bvo, true, true);
	}

	/**
	 * 创建者：仲瑞庆 功能：保存检查 参数： 返回： 例外： 日期：(2001-5-24 下午 5:17) 修改日期，修改人，修改原因，注释标志：
	 */
	public boolean checkVO() {
		try {
			String sAllErrorMessage = "";

			// 执行以下检查，将不具有的检查注释------------------------------------------------
			// ------------------------------------------------------------------------------
			// VO存在检查
			VOCheck.checkNullVO(m_voBill);
			// ------------------------------------------------------------------------------
			// 应发数量检查,要放在前面
			// 本节点使用=====================
			// 检查数量的方向和业务方向是否一致.退货退库的数量要为负,非退库退货数量为正
			boolean isRight = VOCheck.isNumDirectionRight(m_voBill);
			if (!isRight) {
				sAllErrorMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID("4008check", "UPP4008check-000213")/*
																													 * @res
																													 * "数量的方向和业务方向不一致！"
																													 */;
				showErrorMessage(sAllErrorMessage);
				return false;
			}
			// 数值输入全部性检查 v5 支持混填
			// VOCheck.checkNumInput(m_voBill.getChildrenVO(), m_sNumItemKey);

			// 本节点使用=====================
			// 表头表体非空检查
			try {
				VOCheck.validate(m_voBill, GeneralMethod.getHeaderCanotNullString(getBillCardPanel()), GeneralMethod.getBodyCanotNullString(getBillCardPanel()));
			} catch (ICNullFieldException e) {
				// 显示提示
				String sErrorMessage = GeneralMethod.getBodyErrorMessage(getBillCardPanel(), m_voBill, e.getErrorRowNums(), e.getHint());
				sAllErrorMessage = sAllErrorMessage + sErrorMessage + "\n";
			} catch (ICHeaderNullFieldException e) {
				// 显示提示
				String sErrorMessage = GeneralMethod.getHeaderErrorMessage(getBillCardPanel(), e.getErrorRowNums(), e.getHint());
				sAllErrorMessage = sAllErrorMessage + sErrorMessage + "\n";
			}

			/*
			 * // 采购订单退库表头VO里业务类型的非空项检查 try {
			 * VOCheck.validatePO_RETURN(m_voBill);
			 * 
			 * } catch (ICHeaderNullFieldException e) { // 显示提示 String
			 * sErrorMessage = GeneralMethod.getHeaderErrorMessage(
			 * getBillCardPanel(), e.getErrorRowNums(), e.getHint());
			 * sAllErrorMessage = sAllErrorMessage + sErrorMessage + "\n"; }
			 */

			// 有导入操作的导入数据校验
			if (m_bIsImportData) sAllErrorMessage = sAllErrorMessage + nc.ui.ic.pub.bill.GeneralBillUICtl.checkImportBodyVO(m_voBill.getChildrenVO());
			// ------------------------------------------------------------------------------
			// 业务项检查

			// 自由项
			try {
				VOCheck.checkFreeItemInput(m_voBill, m_sNumItemKey);

			} catch (ICNullFieldException e) {
				// 显示提示
				String sErrorMessage = GeneralMethod.getBodyErrorMessage(getBillCardPanel(), m_voBill, e.getErrorRowNums(), e.getHint());
				sAllErrorMessage = sAllErrorMessage + sErrorMessage + "\n";
			}
			// 辅计量
			try {
				VOCheck.checkAssistUnitInputByID(m_voBill, m_sNumItemKey, m_sAstItemKey);
			} catch (ICNullFieldException e) {
				// 显示提示
				String sErrorMessage = GeneralMethod.getBodyErrorMessage(getBillCardPanel(), m_voBill, e.getErrorRowNums(), e.getHint());
				sAllErrorMessage = sAllErrorMessage + sErrorMessage + "\n";
			}

			try {
				VOCheck.checkAssistUnitInputByID(m_voBill, m_sAstItemKey, m_sNumItemKey);
			} catch (ICNullFieldException e) {
				// 显示提示
				String sErrorMessage = GeneralMethod.getBodyErrorMessage(getBillCardPanel(), m_voBill, e.getErrorRowNums(), e.getHint());
				sAllErrorMessage = sAllErrorMessage + sErrorMessage + "\n";
			}

			// 入库日期
			try {
				if (getBillCardPanel().getBodyItem("dbizdate") != null) VOCheck.checkdbizdate(m_voBill, m_sNumItemKey);
			} catch (ICNullFieldException e) {
				// 显示提示
				String sErrorMessage = GeneralMethod.getBodyErrorMessage(getBillCardPanel(), m_voBill, e.getErrorRowNums(), e.getHint());
				sAllErrorMessage = sAllErrorMessage + sErrorMessage + "\n";
			}
			// 非期初单检查业务日期需大于系统启用日期
			if (!m_bIsInitBill) {
				if (getBillCardPanel().getBodyItem("dbizdate") != null) {
					String sBizdateColName = getBillCardPanel().getBodyItem("dbizdate").getName();
					try {
						VOCheck.checkStartDateAfter(m_voBill, "dbizdate", sBizdateColName, m_sStartDate);
					} catch (NullFieldException e) {
						String sErrorMessage = e.getHint();
						sAllErrorMessage = sAllErrorMessage + sErrorMessage + "\n";
					}
				}
			}
			// 价格>0检查
			try {
				VOCheck.checkGreaterThanZeroInput(m_voBill.getChildrenVO(), "nprice", nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC000-0000741")/* @res "单价" */);
			} catch (ICPriceException e) {
				// 显示提示
				String sErrorMessage = GeneralMethod.getBodyErrorMessage(getBillCardPanel(), m_voBill, e.getErrorRowNums(), e.getHint());
				sAllErrorMessage = sAllErrorMessage + sErrorMessage + "\n";
			}
			// 序列号检查
			try {
				if (!isBrwLendBiztype()) VOCheck.checkSNInput(m_voBill.getChildrenVO(), m_sNumItemKey);
			} catch (ICSNException e) {
				// 显示提示
				String sErrorMessage = GeneralMethod.getBodyErrorMessage(getBillCardPanel(), m_voBill, e.getErrorRowNums(), e.getHint());
				sAllErrorMessage = sAllErrorMessage + sErrorMessage + "\n";
			} catch (NullFieldException e) {
				String sErrorMessage = e.getHint();
				sAllErrorMessage = sAllErrorMessage + sErrorMessage + "\n";
			}

			// 检查对应单据号
			ArrayList alCheckString = new ArrayList();
			for (int i = 0; i < m_voBill.getItemVOs().length; i++) {
				boolean bCanAdded = false;
				if (m_iBillInOutFlag == InOutFlag.IN) {
					// 入库单
					if (getIsInvTrackedBill(m_voBill.getItemInv(i)) && m_voBill.getItemValue(i, m_sNumItemKey) != null && ((UFDouble) m_voBill.getItemValue(i, m_sNumItemKey)).doubleValue() <= 0) {
						// 数量<0
						bCanAdded = true;
					}
				} else {
					// 出库单
					if (getIsInvTrackedBill(m_voBill.getItemInv(i)) && (m_voBill.getItemValue(i, m_sNumItemKey) == null || ((UFDouble) m_voBill.getItemValue(i, m_sNumItemKey)).doubleValue() >= 0)) {
						// 数量>=0
						bCanAdded = true;
					}
				}
				if (bCanAdded) {
					ArrayList alCheckddd = new ArrayList();
					alCheckddd.add(0, new Integer(i));
					alCheckddd.add(1, m_sNumItemKey);
					if (getBillCardPanel().getBodyItem(m_sCorBillCodeItemKey) != null) {
						alCheckddd.add(2, m_sCorBillCodeItemKey); // 对应单据号字段1
						alCheckddd.add(3, "ccorrespondbid"); // 对应单据号字段2
					}
					alCheckString.add(alCheckddd);
				}
			}
			try {
				VOCheck.validateBody(m_voBill.getItemVOs(), alCheckString);
			} catch (ICNullFieldException e) {
				// 显示提示
				String sErrorMessage = GeneralMethod.getBodyErrorMessage(getBillCardPanel(), m_voBill, e.getErrorRowNums(), e.getHint());
				sAllErrorMessage = sAllErrorMessage + sErrorMessage + "\n";
			}
			// }

			// 自定校验前先行报错，退出
			if (sAllErrorMessage.trim().length() != 0) {
				showErrorMessage(sAllErrorMessage);
				return false;
			}

			// 校验条码数量不能大于实际收发数量
			GeneralBillItemVO[] voaItemtemp = (GeneralBillItemVO[]) m_voBill.getChildrenVO();
			String sMsg = BarcodeparseCtrl.checkNumWithBarNum(voaItemtemp, true);
			if (sMsg != null) {
				showErrorMessage(sMsg);
				return false;
			}
			// 检查业务类型是W的则仓库必须入外寄仓.

			try {
				checkVMIWh(m_voBill);

			} catch (Exception ex1) {
				showErrorMessage(ex1.getMessage());

				return false;

			}

			// 检查存货为按毛重管理库存的必须输入毛重
			try {
				VOCheck.checkGrossNumInput(m_voBill.getItemVOs(), m_sNgrossnum, m_sNumItemKey);
			} catch (ICNullFieldException e) {
				// 显示提示
				String sErrorMessage = GeneralMethod.getBodyErrorMessage(getBillCardPanel(), m_voBill, e.getErrorRowNums(), e.getHint());
				sAllErrorMessage = sAllErrorMessage + sErrorMessage + "\n";
			}

			// add by xiaolong_fan,2012-11-06.仓库档案中[供应商寄存]打勾的 ,改为可按照货位控制,不能为负库存.
			// 1.检查仓库是否寄存仓库.
			if (getParentCorpCode1().equals("10395")) {
				String cwarehouseid = m_voBill.getHeaderVO().getCwarehouseid();
				String sql = " select isgathersettle  from bd_stordoc  where pk_stordoc ='" + cwarehouseid + "'";
				Object res = null;
				IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
				try {
					res = query.executeQuery(sql, new ColumnProcessor());
				} catch (BusinessException e) {
					e.printStackTrace();
				}
				if (res != null && "Y".equals(res)) {// 是寄存仓库,按库位检查库存.
					// 检查是出库还是入库,入库是1,出库是-1,其他为0
					int inOutFlag = getBillInOutFlag();
					// int inOutFlag = m_voBill.getBillInOutFlag();

					StringBuffer errMsg = new StringBuffer();

					// 2.循环检查每行物料对应的库位,是否在货位的
					for (int i = 0; i < m_voBill.getItemVOs().length; i++) {
						double ninnum = 0d;
						// 数量
						if (m_voBill.getItemValue(i, m_sNumItemKey) != null) {
							ninnum = ((UFDouble) m_voBill.getItemValue(i, m_sNumItemKey)).doubleValue();
						}
						// 存货档案
						// m_voaBillItem
						// GeneralBillItemVO vo = new GeneralBillItemVO();
						// vo.getCinventorycode();
						// vo.getVspacename();
						String cinventorycode = String.valueOf(m_voBill.getItemValue(i, "cinventorycode"));
						// 库位
						String vspacename = String.valueOf(m_voBill.getItemValue(i, "cspaceid"));
						// 供应商 add by cm
						String cvendorid = String.valueOf(m_voBill.getItemValue(i, "cvendorid"));
						// 自由项add by cm
						String vfree1 = String.valueOf(m_voBill.getItemValue(i, "vfree1"));
						// 仓库 add by cm
						// String cwarehouseid =
						// String.valueOf(m_voBill.getItemValue(i,
						// "cwarehouseid"));
						// 批次add by cm vlotb
						String vlotb = String.valueOf(m_voBill.getItemValue(i, "vbatchcode"));
						// 行号add by cm
						String crowno = String.valueOf(m_voBill.getItemValue(i, "crowno"));
						// if(|| || )continue;
						StringBuffer sb = new StringBuffer("");
						if (!"null".equals(cinventorycode)) {
							sb.append(" and b.invcode='" + cinventorycode + "' ");
						}
						if (!"null".equals(vspacename)) {
							sb.append(" and a.cspaceid='" + vspacename + "' ");
						}
						if (!"null".equals(cvendorid)) {
							sb.append(" and a.cvendorid = '" + cvendorid + "' ");
						}
						if (!"null".equals(vfree1)) {
							sb.append("  and a.vfreeb1 = '" + vfree1 + "' ");
						}
						if (!"null".equals(vlotb)) {
							sb.append("   and vlotb = '" + vlotb + "' ");
						}
						String numSql = "select sum(nnum) from ic_onhandnum_b a,bd_invbasdoc b,ic_onhandnum c " + "where a.cinvbasid=b.pk_invbasdoc and a.pk_onhandnum = c.pk_onhandnum and cwarehouseid = '" + cwarehouseid + "' " + sb.toString() + " ";// add
						// by
						// cm
						Object nnumRes = null;
						try {
							nnumRes = query.executeQuery(numSql, new ColumnProcessor());
						} catch (BusinessException e) {
							e.printStackTrace();
						}
						if (nnumRes != null) {// update by cm 20121121
							UFDouble num = new UFDouble(String.valueOf(nnumRes));
							if ((inOutFlag == InOutFlag.IN) && (ninnum + num.doubleValue() < 0)) {
								// errMsg.append("第").append(i+1).append("行存货<"+cinventorycode+">在该货位的结存数量为：").append(num).append(".不能入库!\n");
								errMsg.append("行号为").append(crowno).append("的存货<" + cinventorycode + ">在该货位的结存数量为：").append(num).append(".不能入库!\n");
							} else if ((inOutFlag == InOutFlag.OUT) && (num.doubleValue() - ninnum < 0)) {
								errMsg.append("行号为").append(crowno).append("的行存货<" + cinventorycode + ">在该货位的结存数量为：").append(num).append(".不能出库!\n");
							}
						} else {
							// UFDouble num = new
							// UFDouble(String.valueOf(ninnum));
				  		     if (inOutFlag == InOutFlag.OUT) {
								errMsg.append("行号为").append(crowno).append("的行存货<" + cinventorycode + ">在该货位的结存数量为：").append(0).append(".不能出库!\n");
							}
						}
					}
					if (errMsg != null && errMsg.length() >= 5) {
						showErrorMessage(errMsg.toString());
						return false;
					}
				}
			}
			// end by xiaolong_fan.
			// add by xiaolong_fan.材料出库受控件.
			// 检查是出库还是入库,入库是1,出库是-1,其他为0
			int inOutFlag = getBillInOutFlag();
			if (getParentCorpCode1().equals("10395")) {
				if (inOutFlag == -1) {
					Object res = null;
					IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
					for (int j = 0; j < voaItemtemp.length; j++) {
						// 存货档案
						String cinventorycode = String.valueOf(m_voBill.getItemValue(j, "cinventorycode"));
						// 来源单据类型
						String csourcetype = String.valueOf(m_voBill.getItemValue(j, "csourcetype"));
						// 出库数量add by shikun 2014-06-12
						UFDouble noutnum = m_voBill.getItemValue(j, "noutnum")==null?new UFDouble(0):new UFDouble(m_voBill.getItemValue(j, "noutnum").toString());
						String sql = "select e.docname from bd_invmandoc c, bd_invbasdoc d,bd_defdoc e where c.pk_invbasdoc = d.pk_invbasdoc and e.pk_defdoc = c.def3 and d.dr = 0 and d.invcode = ? and c.pk_corp = ?";//add by zwx and c.pk_corp = ?  2014-11-21
						SQLParameter param = new SQLParameter();
						param.addParam(cinventorycode);
						//add by zwx 2014-11-21
						String pk_corp=ClientEnvironment.getInstance().getCorporation()
						.getPrimaryKey();
						param.addParam(pk_corp);
						try {
							res = query.executeQuery(sql, param, new ColumnProcessor());
						} catch (BusinessException e) {
							e.printStackTrace();
						}
						if (res != null && "是".equals(String.valueOf(res))) {
							if (csourcetype == null || !"422X".equals(csourcetype)) {
								//add by zwx 单据来源类型为4Q(货位调整单)
								String billtype = getBillTypeCode();
								if (noutnum.doubleValue()>=0&&!"4Q".equals(billtype)) {//add by shikun 2014-06-12 出库数量为负数，即入库退货不进行申请单校验
									int m = j + 1;
									showErrorMessage("第" + m + "行存货是受控物资，请先填制物资需求申请单，需审批通过后才可出库");
									return false;
								}
							}
						}

					}
				}
			}
			// end by xiaolong_fan.

			// /

			// ------------------------------------------------------------------------------

			// 自定校验
			// ------------------------------------------------------------------------------
			/*
			 * if (packageID.equals("221")) { //出入库仓库不能相同 //应当已进行了非空校验 if
			 * (m_voBill .getParentVO() .getAttributeValue("coutwarehouseid")
			 * .toString() .trim() .equals(
			 * m_voBill.getParentVO().getAttributeValue
			 * ("cinwarehouseid").toString().trim())) { //显示提示
			 * showHintMessage("出入库仓库不应相同..."); //showErrorMessage(
			 * "出入库仓库不应相同..."); //更改颜色为正常颜色 SetColor.SetTableColor(
			 * getBillCardPanel().getBillModel(),
			 * getBillCardPanel().getBillTable(), getBillCardPanel(), new
			 * ArrayList(), m_cNormalColor, m_cNormalColor, m_bExchangeColor,
			 * m_bLocateErrorColor, ""); //SetColor(
			 * //getBillCardPanel().getBillModel(),
			 * //getBillCardPanel().getBillTable(), //new ArrayList(),
			 * //m_cNormalColor, //null, //m_bExchangeColor,
			 * //m_bLocateErrorColor, //""); return false; } }
			 * //----------------
			 * --------------------------------------------------------------
			 * 
			 * //无任何异常 //更改颜色为正常颜色
			 * 
			 * SetColor.SetTableColor( getBillCardPanel().getBillModel(),
			 * getBillCardPanel().getBillTable(), getBillCardPanel(), new
			 * ArrayList(), m_cNormalColor, m_cNormalColor, m_bExchangeColor,
			 * m_bLocateErrorColor, ""); //SetColor(
			 * //getBillCardPanel().getBillModel(),
			 * //getBillCardPanel().getBillTable(), //new ArrayList(),
			 * //m_cNormalColor, //null, //m_bExchangeColor,
			 * //m_bLocateErrorColor, //"");
			 */
			return true;

		} catch (ICDateException e) {
			// 显示提示
			String sErrorMessage = GeneralMethod.getBodyErrorMessage(getBillCardPanel(), m_voBill, e.getErrorRowNums(), e.getHint());
			// String sErrorMessage= getBodyErrorMessage(e.getErrorRowNums(),
			// e.getHint());
			showErrorMessage(sErrorMessage);
			// showHintMessage(e.getHint());
			// 更改颜色
			/*
			 * SetColor.SetTableColor( getBillCardPanel().getBillModel(),
			 * getBillCardPanel().getBillTable(), getBillCardPanel(),
			 * e.getErrorRowNums(), m_cNormalColor, e.getExceptionColor(),
			 * m_bExchangeColor, m_bLocateErrorColor, e.getHint());
			 */
			// SetColor(
			// getBillCardPanel().getBillModel(),
			// getBillCardPanel().getBillTable(),
			// e.getErrorRowNums(),
			// m_cNormalColor,
			// e.getExceptionColor(),
			// m_bExchangeColor,
			// m_bLocateErrorColor,
			// e.getHint());
			return false;
		} catch (ICNullFieldException e) {
			// 显示提示
			String sErrorMessage = GeneralMethod.getBodyErrorMessage(getBillCardPanel(), m_voBill, e.getErrorRowNums(), e.getHint());
			// String sErrorMessage=
			// m_shvoBillSpecialHVO.getBodyErrorMessage(
			// getBillCardPanel(),
			// e.getErrorRowNums(),
			// e.getHint(),
			// m_sItemKeyID,
			// m_sItemKeyName);
			// String sErrorMessage= getErrorMessage(e.getErrorRowNums(),
			// e.getHint());
			showErrorMessage(sErrorMessage);
			// showHintMessage(e.getHint());
			// 更改颜色
			/*
			 * SetColor.SetTableColor( getBillCardPanel().getBillModel(),
			 * getBillCardPanel().getBillTable(), getBillCardPanel(),
			 * e.getErrorRowNums(), m_cNormalColor, e.getExceptionColor(),
			 * m_bExchangeColor, m_bLocateErrorColor, e.getHint());
			 */
			// SetColor(
			// getBillCardPanel().getBillModel(),
			// getBillCardPanel().getBillTable(),
			// e.getErrorRowNums(),
			// m_cNormalColor,
			// e.getExceptionColor(),
			// m_bExchangeColor,
			// m_bLocateErrorColor,
			// e.getHint());
			return false;
		} catch (ICNumException e) {
			// 显示提示
			String sErrorMessage = GeneralMethod.getBodyErrorMessage(getBillCardPanel(), m_voBill, e.getErrorRowNums(), e.getHint());
			// String sErrorMessage= getBodyErrorMessage(e.getErrorRowNums(),
			// e.getHint());
			showErrorMessage(sErrorMessage);
			// showHintMessage(e.getHint());
			/*
			 * SetColor.SetTableColor( getBillCardPanel().getBillModel(),
			 * getBillCardPanel().getBillTable(), getBillCardPanel(),
			 * e.getErrorRowNums(), m_cNormalColor, e.getExceptionColor(),
			 * m_bExchangeColor, m_bLocateErrorColor, e.getHint());
			 */
			// SetColor(
			// getBillCardPanel().getBillModel(),
			// getBillCardPanel().getBillTable(),
			// e.getErrorRowNums(),
			// m_cNormalColor,
			// e.getExceptionColor(),
			// m_bExchangeColor,
			// m_bLocateErrorColor,
			// e.getHint());
			return false;
		} catch (ICPriceException e) {
			// 显示提示
			String sErrorMessage = GeneralMethod.getBodyErrorMessage(getBillCardPanel(), m_voBill, e.getErrorRowNums(), e.getHint());
			// String sErrorMessage= getBodyErrorMessage(e.getErrorRowNums(),
			// e.getHint());
			showErrorMessage(sErrorMessage);
			// showHintMessage(e.getHint());
			/*
			 * SetColor.SetTableColor( getBillCardPanel().getBillModel(),
			 * getBillCardPanel().getBillTable(), getBillCardPanel(),
			 * e.getErrorRowNums(), m_cNormalColor, e.getExceptionColor(),
			 * m_bExchangeColor, m_bLocateErrorColor, e.getHint());
			 */
			// SetColor(
			// getBillCardPanel().getBillModel(),
			// getBillCardPanel().getBillTable(),
			// e.getErrorRowNums(),
			// m_cNormalColor,
			// e.getExceptionColor(),
			// m_bExchangeColor,
			// m_bLocateErrorColor,
			// e.getHint());
			return false;
		} catch (ICSNException e) {
			// 显示提示
			String sErrorMessage = GeneralMethod.getBodyErrorMessage(getBillCardPanel(), m_voBill, e.getErrorRowNums(), e.getHint());
			// String sErrorMessage= getBodyErrorMessage(e.getErrorRowNums(),
			// e.getHint());
			showErrorMessage(sErrorMessage);
			// showHintMessage(e.getHint());
			/*
			 * SetColor.SetTableColor( getBillCardPanel().getBillModel(),
			 * getBillCardPanel().getBillTable(), getBillCardPanel(),
			 * e.getErrorRowNums(), m_cNormalColor, e.getExceptionColor(),
			 * m_bExchangeColor, m_bLocateErrorColor, e.getHint());
			 */
			// SetColor(
			// getBillCardPanel().getBillModel(),
			// getBillCardPanel().getBillTable(),
			// e.getErrorRowNums(),
			// m_cNormalColor,
			// e.getExceptionColor(),
			// m_bExchangeColor,
			// m_bLocateErrorColor,
			// e.getHint());
			return false;
		} catch (ICLocatorException e) {
			// 显示提示
			String sErrorMessage = GeneralMethod.getBodyErrorMessage(getBillCardPanel(), m_voBill, e.getErrorRowNums(), e.getHint());
			// String sErrorMessage= getBodyErrorMessage(e.getErrorRowNums(),
			// e.getHint());
			showErrorMessage(sErrorMessage);
			// showHintMessage(e.getHint());
			/*
			 * SetColor.SetTableColor( getBillCardPanel().getBillModel(),
			 * getBillCardPanel().getBillTable(), getBillCardPanel(),
			 * e.getErrorRowNums(), m_cNormalColor, e.getExceptionColor(),
			 * m_bExchangeColor, m_bLocateErrorColor, e.getHint());
			 */
			// SetColor(
			// getBillCardPanel().getBillModel(),
			// getBillCardPanel().getBillTable(),
			// e.getErrorRowNums(),
			// m_cNormalColor,
			// e.getExceptionColor(),
			// m_bExchangeColor,
			// m_bLocateErrorColor,
			// e.getHint());
			return false;
		} catch (ICRepeatException e) {
			// 显示提示
			String sErrorMessage = GeneralMethod.getBodyErrorMessage(getBillCardPanel(), m_voBill, e.getErrorRowNums(), e.getHint());
			// String sErrorMessage= getBodyErrorMessage(e.getErrorRowNums(),
			// e.getHint());
			showErrorMessage(sErrorMessage);
			// showHintMessage(e.getHint());
			/*
			 * SetColor.SetTableColor( getBillCardPanel().getBillModel(),
			 * getBillCardPanel().getBillTable(), getBillCardPanel(),
			 * e.getErrorRowNums(), m_cNormalColor, e.getExceptionColor(),
			 * m_bExchangeColor, m_bLocateErrorColor, e.getHint());
			 */
			// SetColor(
			// getBillCardPanel().getBillModel(),
			// getBillCardPanel().getBillTable(),
			// e.getErrorRowNums(),
			// m_cNormalColor,
			// e.getExceptionColor(),
			// m_bExchangeColor,
			// m_bLocateErrorColor,
			// e.getHint());
			return false;
		} catch (ICHeaderNullFieldException e) {
			// 显示提示
			String sErrorMessage = GeneralMethod.getHeaderErrorMessage(getBillCardPanel(), e.getErrorRowNums(), e.getHint());
			// String sErrorMessage= getHeaderErrorMessage(e.getErrorRowNums(),
			// e.getHint());
			showErrorMessage(sErrorMessage);
			// showHintMessage(e.getHint());
			// showErrorMessage( e.getMessage());
			return false;
		} catch (NullFieldException e) {
			showErrorMessage(e.getHint());
			// fullScreen(getBillCardPanel().getBillModel(), m_iFirstAddRows);
			return false;
		} catch (ValidationException e) {
			SCMEnv.out("校验异常！其他未知故障...");
			handleException(e);
			return false;
		}
	}

	/**
	 * 创建者：王乃军 功能：得到修改后的vo,用于修改后的保存 参数： 返回： 例外： 日期：(2001-5-9 9:23:32)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */

	/**
	 * Invoked when an action occurs.
	 */
	public void actionPerformed(java.awt.event.ActionEvent e) {
		if (e.getSource() == getAddNewRowNoItem()) {
			onAddNewRowNo();
		}

	}

	/**
	 * 创建者：王乃军 功能： 参数： 返回： 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	/**
	 * 创建者：王乃军 功能：得到当前显示的panel 参数： 返回： 例外： 日期：(2001-5-9 9:23:32)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	public int getCurPanel() {
		return m_iCurPanel;
	}

	/**
	 * 创建者：王乃军 功能：单据表体右键菜单处理 参数： 返回： 例外： 日期：(2001-5-9 9:23:32)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	public void onMenuItemClick(java.awt.event.ActionEvent e) {
		// 源
		UIMenuItem item = (UIMenuItem) e.getSource();
		// 复制
		if (item == getBillCardPanel().getCopyLineMenuItem()) {
			onCopyLine();
		} else // 粘贴
		if (item == getBillCardPanel().getPasteLineMenuItem()) {
			onPasteLine();
		}
		// 粘贴到表尾时,设置行号
		else if (item == getBillCardPanel().getPasteLineToTailMenuItem()) {
			int iRowCount = getBillCardPanel().getBodyPanel().getTableModel().getRowCount();
			getBillCardPanel().pasteLineToTail();
			// 增加的行数
			int iRowCount1 = getBillCardPanel().getBodyPanel().getTableModel().getRowCount();
			nc.ui.scm.pub.report.BillRowNo.addLineRowNos(getBillCardPanel(), m_sBillTypeCode, m_sBillRowNo, iRowCount1 - iRowCount);
			voBillPastTailLine();

		} else // 增行
		if (item == getBillCardPanel().getAddLineMenuItem()) {
			onAddLine();

		} else // 删行
		if (item == getBillCardPanel().getDelLineMenuItem()) onDeleteRow();
		else // 插入行
		if (item == getBillCardPanel().getInsertLineMenuItem()) {
			onInsertLine();
		}

	}

	/**
	 * 创建者：仲瑞庆 功能：复制行 参数： 返回： 例外： 日期：(2001-6-26 下午 9:32) 修改日期，修改人，修改原因，注释标志：
	 */
	protected void voBillCopyLine() {
		int[] row = getBillCardPanel().getBillTable().getSelectedRows();
		if (row == null || row.length == 0) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH004")/* @res "请选择要处理的数据行" */);
			return;
		}
		m_voaBillItem = new GeneralBillItemVO[row.length];
		GeneralBillItemVO uicopyvo = null;
		for (int i = 0; i < row.length; i++) {
			m_voBill.getItemVOs()[row[i]].setLocator((LocatorVO[]) m_alLocatorData.get(row[i]));
			m_voaBillItem[i] = (GeneralBillItemVO) m_voBill.getItemVOs()[row[i]].clone();
			uicopyvo = (GeneralBillItemVO) getBillCardPanel().getBillModel().getBodyValueRowVO(row[i], GeneralBillItemVO.class.getName());
			uicopyvo = (GeneralBillItemVO) uicopyvo.clone();
			String[] keys = uicopyvo.getAttributeNames();
			SmartVOUtilExt.copyVOByVO(m_voaBillItem[i], keys, uicopyvo, keys);
			// 清除货位、序列号，这些数据是不复制的,和 m_alLoctorData,m_alSerialData保持一致
			// ydy 2004-07-02 货位复制
			// m_voaBillItem[i].setLocator((LocatorVO[])m_alLocatorData.get(row[i]));
			LocatorVO[] locatorvos = (LocatorVO[]) m_alLocatorData.get(row[i]);
			if (locatorvos != null) {
				LocatorVO[] tempvos = new LocatorVO[locatorvos.length];
				String id = null;
				for (int j = 0; j < locatorvos.length; j++) {
					LocatorVO locatorVO = locatorvos[j];
					LocatorVO tempvo = new LocatorVO();
					String names[] = locatorVO.getAttributeNames();
					for (int k = 0; k < names.length; k++) {
						tempvo.setAttributeValue(names[k], locatorVO.getAttributeValue(names[k]));
					}
					id = locatorVO.getCspaceid();
					tempvo.setAttributeValue("cspaceid", id);
					// tempvo.setPrimaryKey(null);
					tempvo.setCgeneralbid(null);
					// tempvo.setStatus(2);
					tempvos[j] = tempvo;
				}
				m_voaBillItem[i].setLocator(tempvos);
			}
			m_voaBillItem[i].setSerial(null);
			m_voaBillItem[i].setAttributeValue("cparentid", null);
			m_voaBillItem[i].setAttributeValue("ncorrespondnum", null);
			m_voaBillItem[i].setAttributeValue("ncorrespondastnum", null);

			// 清除条码数据 add by hanwei 2004-04-07
			m_voaBillItem[i].setBarCodeVOs(null);
			m_voaBillItem[i].setAttributeValue(IItemKey.NBARCODENUM, new UFDouble(0.0));

			m_voaBillItem[i].setBarcodeClose(new nc.vo.pub.lang.UFBoolean('N'));

			m_voaBillItem[i].setAttributeValue(IItemKey.NKDNUM, null);
		}

	}

	/**
	 * 创建者：仲瑞庆 功能：粘贴行 参数： 返回： 例外： 日期：(2001-6-26 下午 9:32) 修改日期，修改人，修改原因，注释标志：
	 */
	protected void voBillPastLine() {
		// 要求在已经增完行后执行
		if (m_voaBillItem != null) {
			int row = getBillCardPanel().getBillTable().getSelectedRow() - m_voaBillItem.length;
			voBillPastLine(row);
		}

	}

	/**
	 * 创建者：王乃军 功能：得到单据数量 参数： 返回： 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	public int getBillCount() {
		return m_iBillQty;
	}

	/**
	 * 创建者：王乃军 功能：得到指定行的VO 参数： 返回： 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	protected GeneralBillItemVO getBodyVO(int iLine) {
		nc.ui.pub.bill.BillModel bmTemp = getBillCardPanel().getBillModel();
		if (bmTemp == null) {
			SCMEnv.out("bm null E!");
			return null;
		}
		Vector vBodyData = bmTemp.getDataVector();
		if (vBodyData == null || vBodyData.size() == 0 || iLine >= vBodyData.size()) {
			SCMEnv.out("bd null or line big E!");
			return null;
		}
		// 为了得到自由项。
		GeneralBillItemVO voItemForFree = m_voBill.getItemVOs()[iLine];

		// vo数组的长度==当前显示的行数
		// int rowCount = vBodyData.size();
		// 初始化返回的vo
		GeneralBillItemVO voBody = new GeneralBillItemVO();

		int iRowStatus = nc.ui.pub.bill.BillModel.ADD;

		// 整理当前界面上显示的行，包括原行、修改后的行、新增的行。
		iRowStatus = bmTemp.getRowState(iLine);
		for (int j = 0; j < bmTemp.getBodyItems().length; j++) {
			nc.ui.pub.bill.BillItem item = bmTemp.getBodyItems()[j];
			Object aValue = bmTemp.getValueAt(iLine, item.getKey());
			aValue = item.converType(aValue);
			voBody.setAttributeValue(item.getKey(), aValue);
		}
		// 设置状态
		switch (iRowStatus) {
		case nc.ui.pub.bill.BillModel.ADD: // 新增的行
			voBody.setStatus(nc.vo.pub.VOStatus.NEW);
			break;
		case nc.ui.pub.bill.BillModel.MODIFICATION: // 修改后的行
			voBody.setStatus(nc.vo.pub.VOStatus.UPDATED);
			break;
		case nc.ui.pub.bill.BillModel.NORMAL: // 原行
			voBody.setStatus(nc.vo.pub.VOStatus.UNCHANGED);
			break;
		}
		// 货位分配数据
		if (m_alLocatorData != null && m_alLocatorData.size() > iLine) voBody.setLocator((LocatorVO[]) m_alLocatorData.get(iLine));
		// 序列号数据
		if (m_alSerialData != null && m_alSerialData.size() > iLine) voBody.setSerial((SerialVO[]) m_alSerialData.get(iLine));
		// 自由项
		voBody.setFreeItemVO(voItemForFree.getFreeItemVO());

		// 删除的行不传
		return voBody;
	}

	/**
	 * 创建者：王乃军 功能：得到当前单据的编辑状态 参数： 返回： 已签字 1 未签字 0 不能操作 -1 例外： 日期：(2001-5-9
	 * 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	public int getMode() {
		return m_iMode;
	}

	/**
	 * 创建者：王乃军 功能：当前选中行是否能序列号分配，要考虑列表/表单下的选中 参数： 返回： 例外： 日期：(2001-5-9 9:23:32)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	public boolean isSNmgt() {
		// 当前选中的行
		int iCurSelBodyLine = -1;
		if (BillMode.Card == m_iCurPanel) {
			iCurSelBodyLine = getBillCardPanel().getBillTable().getSelectedRow();
			if (iCurSelBodyLine < 0) {
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000016")/*
																												* @res "请选中要进行序列号分配的行。"
																												*/);
				return false;
			}
		} else {
			iCurSelBodyLine = getBillListPanel().getBodyTable().getSelectedRow();
			if (iCurSelBodyLine < 0) {
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000017")/*
																												* @res "请选中要查看序列号的行。"
																												*/);
				return false;
			}
		}
		InvVO voInv = null;
		// 读表体vo,区分列表下还是表单下
		// 表单形式下
		if (BillMode.Card == m_iCurPanel) {
			if (m_voBill == null) {
				SCMEnv.out("bill null E.");
				return false;
			}
			voInv = m_voBill.getItemInv(iCurSelBodyLine);
		} else // 列表形式下
		if (m_iLastSelListHeadRow >= 0 && m_iLastSelListHeadRow < m_alListData.size()) {
			GeneralBillVO bvo = (GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow);
			if (bvo == null) {
				SCMEnv.out("bill null E.");
				return false;
			}
			voInv = bvo.getItemInv(iCurSelBodyLine);
		}

		if (voInv != null && voInv.getIsSerialMgt() != null && voInv.getIsSerialMgt().intValue() == 1) return true;
		else return false;
	}

	/**
	 * 创建者：王乃军 功能：设置状态条，缺省情况下不用设置。 需要的话可以把提示信息重定向到指定的TextField 参数： 返回： 例外：
	 * 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	public void setHintBar(javax.swing.JTextField tfHint) {
		m_tfHintBar = tfHint;
	}

	/**
	 * 创建者：王乃军 功能：重载的显示提示信息功能 参数： 返回： 例外： 日期：(2001-5-9 9:23:32)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	public void showHintMessage(String sMsg) {
		if (m_tfHintBar != null) m_tfHintBar.setText(sMsg);
		else super.showHintMessage(sMsg);
	}

	/**
	 * 创建者：王乃军 功能：得到当前选中的单据 参数： 返回： 例外： 日期：(2001-5-9 9:23:32)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	public int getCurSelBill() {
		return m_iLastSelListHeadRow;
	}

	/**
	 * 创建者：王乃军 功能：是否是货位管理，只适用于出入库单。 参数： 返回： 例外： 日期：(2001-5-9 9:23:32)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	public boolean isLocatorMgt() {
		if (m_voBill != null && BillMode.Card == m_iCurPanel) {
			WhVO voWh = m_voBill.getWh();
			// 货位管理的仓库需要读货位数据
			if (voWh != null && voWh.getIsLocatorMgt() != null && voWh.getIsLocatorMgt().intValue() == 1) return true;
			else return false;

		}
		if (m_alListData == null || m_iLastSelListHeadRow < 0 || m_iLastSelListHeadRow >= m_alListData.size()) return false;
		GeneralBillVO vob = (GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow);
		if (vob != null) {
			WhVO voWh = vob.getWh();
			// 货位管理的仓库需要读货位数据
			if (voWh != null && voWh.getIsLocatorMgt() != null && voWh.getIsLocatorMgt().intValue() == 1) return true;
			else return false;
		} else return false;
	}

	/**
	 * 创建者：余大英 功能：应发辅数量编辑事件处理 参数：e单据编辑事件 返回： 例外： 日期：(2001-5-8 19:08:05)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 辅数量*换算率=数量
	 * 
	 */
	public void afterAstNumEdit(nc.ui.pub.bill.BillEditEvent e) {
		int row = e.getRow();

		String[] sfields = new String[] {
				"castunitid", "hsl", m_sAstItemKey, m_sNumItemKey
		};
		GeneralBillUICtl.changeNum(getBillCardPanel(), m_sAstItemKey, row, sfields, isFixFlag(row));
		GeneralBillUICtl.synUi2Vo(getBillCardPanel(), m_voBill, new String[] {
				"castunitid", "hsl", m_sAstItemKey, m_sNumItemKey
		}, row);
		afterNumEdit_1(row);

	}

	/**
	 * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2001-7-18 15:01:19) 修改日期，修改人，修改原因，注释标志：
	 * 
	 */
	public void afterCorBillEdit(nc.ui.pub.bill.BillEditEvent e) {
		int iSelrow = e.getRow();
		GeneralBillItemVO[] vos = ((nc.ui.ic.pub.corbillref.ICCorBillRefModel) getICCorBillRef().getRefModel()).getSelectedVOs();

		if (vos == null || vos[0] == null) {
			// 对应单据号
			getBillCardPanel().setBodyValueAt(null, iSelrow, m_sCorBillCodeItemKey);
			// 对应单据类型
			getBillCardPanel().setBodyValueAt(null, iSelrow, "ccorrespondtype");
			// 对应单据表头OID
			// 单据模板库中表体位置两个不显示列ccorrespondhid,ccorrespondbid,以保存带出的对应表头，表体OID
			getBillCardPanel().setBodyValueAt(null, iSelrow, "ccorrespondhid");
			// 对应单据表体OID
			getBillCardPanel().setBodyValueAt(null, iSelrow, "ccorrespondbid");
			m_voBill.setItemValue(iSelrow, "ccorrespondbid", null);

			m_voBill.setItemValue(iSelrow, "ccorrespondhid", null);

			m_voBill.setItemValue(iSelrow, m_sCorBillCodeItemKey, null);

			m_voBill.setItemValue(iSelrow, "ccorrespondtype", null);

			m_voBill.setItemValue(iSelrow, "vtransfercode", null);

			// m_voBill.setItemValue(iSelrow, "", oValue)
			return;
		}
		synline(vos[vos.length - 1], iSelrow, true);

		if (vos != null && vos.length > 1) {
			for (int j = 0; j < vos.length - 1; j++) {
				getBillCardPanel().copyLine();
				voBillCopyLine();

				getBillCardPanel().pasteLine();
				// 增加的行数
				voBillPastLine();

				int irow = getBillCardPanel().getBillTable().getSelectedRow() - 1;
				// getBillCardPanel().setBodyValueAt(null, irow, m_sNumItemKey);
				// getBillCardPanel().setBodyValueAt(null, irow, m_sAstItemKey);
				// getBillCardPanel().setBodyValueAt(null, irow, "cvendorid");
				// getBillCardPanel().setBodyValueAt(null, irow, "vvendorname");

				synline(vos[j], irow, false);
				// 如果是出库处理界面，那么行号就要重新生成。
				if ("40080822".equals(m_sCurrentBillNode)) {
					String crowno = DataBuffer.getRowno();
					getBillCardPanel().setBodyValueAt(crowno, irow, "crowno");
					m_voBill.setItemValue(irow, "crowno", crowno);
					getBillCardPanel().setBodyValueAt(null, irow, "cgeneralbid");
					m_voBill.setItemValue(irow, "cgeneralbid", null);
				}
			}
			// 如果是出库处理界面，那么行号就要重新生成。
			if ("40080822".equals(m_sCurrentBillNode)) { return;
			// 不用再处理行号了
			}
			int iRowCount = vos.length;
			if (e.getRow() + iRowCount == getBillCardPanel().getRowCount()) {
				nc.ui.scm.pub.report.BillRowNo.addLineRowNos(getBillCardPanel(), m_sBillTypeCode, m_sBillRowNo, iRowCount);

			} else {
				nc.ui.scm.pub.report.BillRowNo.insertLineRowNos(getBillCardPanel(), m_sBillTypeCode, m_sBillRowNo, e.getRow() + iRowCount - 1, iRowCount - 1);
			}

			// dw 设置完界面行号后更新 m_voBill
			for (int o = 0; o < getBillCardPanel().getRowCount(); o++) {
				m_voBill.setItemValue(o, m_sBillRowNo, getBillCardPanel().getBodyValueAt(o, m_sBillRowNo));
			}
		}
	}

	// add by zip: 2013/11/28: No 27
	public void afterLotEdit(nc.ui.pub.bill.BillEditEvent param, LotNumbRefVO[] voLot) {
		String sItemKey = param.getKey();
		int rownum = param.getRow();
		if (voLot == null || voLot.length <= 0) {
			nc.ui.ic.pub.lot.LotNumbRefPane lotRef = (nc.ui.ic.pub.lot.LotNumbRefPane) getBillCardPanel().getBodyItem("vbatchcode").getComponent();
			try {
				// 手工输入，可能会有异常。
				voLot = lotRef.getLotNumbRefVOs();
			} catch (Exception e) {
				nc.vo.scm.pub.SCMEnv.error(e);
			}
		}
		synlot(voLot[voLot.length - 1], rownum);
		getBillCardPanel().setBodyValueAt(voLot[0].getNinnum(), rownum, "noutnum");
		getBillCardPanel().setBodyValueAt(m_sLogDate,  rownum, "dbizdate");
		if (voLot != null && voLot.length > 1) {
			for (int j = 0; j < voLot.length - 1; j++) {
				getBillCardPanel().copyLine();
				voBillCopyLine();
				int iRowCount = getBillCardPanel().getBodyPanel().getTableModel().getRowCount();
				getBillCardPanel().pasteLine();
				// 增加的行数
				iRowCount = getBillCardPanel().getBodyPanel().getTableModel().getRowCount() - iRowCount;
				nc.ui.scm.pub.report.BillRowNo.pasteLineRowNo(getBillCardPanel(), m_sBillTypeCode, m_sBillRowNo, iRowCount);
				voBillPastLine();
				int iSelrow = getBillCardPanel().getBillTable().getSelectedRow() - 1;
				synlot(voLot[j], iSelrow);
				nc.ui.scm.pub.report.BillRowNo.addLineRowNo(getBillCardPanel(), m_sBillTypeCode, m_sBillRowNo);
			}
		}

	}
	// end

	/**
	 * 此处插入方法说明。 创建者：张欣 功能：批次号改变处理 参数： 返回： 例外： 日期：(2001-6-20 21:43:07)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @param param
	 *            nc.ui.pub.bill.BillEditEvent
	 */
	public void afterLotEdit(nc.ui.pub.bill.BillEditEvent param) {
		String sItemKey = param.getKey();
		int rownum = param.getRow();

		String sLot = (String) getBillCardPanel().getBodyValueAt(rownum, "vbatchcode");
		if (sLot == null || sLot.trim().length() == 0) {
			clearRowData(0, rownum, "vbatchcode");
			m_voBill.setItemValue(rownum, "vbatchcode", null);
		}
		nc.ui.ic.pub.lot.LotNumbRefPane lotRef = (nc.ui.ic.pub.lot.LotNumbRefPane) getBillCardPanel().getBodyItem("vbatchcode").getComponent();
		LotNumbRefVO[] voLot = null;
		try {
			// 手工输入，可能会有异常。
			voLot = lotRef.getLotNumbRefVOs();
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
		}

		m_voBill.setItemValue(rownum, "vbatchcode", sLot);
		BatchCodeDefSetTool.setBatchCodeInfo(getBillCardPanel(), rownum, (String) m_voBill.getItemValue(rownum, "cinventoryid"), sLot, m_sCorpID, m_voBill.getItemVOs()[rownum]);

		// // 先读数量，如果为空则置为0，按下面的判断返回。
		// UFDouble dNum = new
		// UFDouble(getBillCardPanel().getBodyValueAt(rownum,
		// m_sNumItemKey) == null ? "0" : getBillCardPanel()
		// .getBodyValueAt(rownum, m_sNumItemKey).toString().trim());
		// if ((m_iBillInOutFlag == InOutFlag.IN && dNum.doubleValue() >= 0)
		// || (m_iBillInOutFlag == InOutFlag.OUT && dNum.doubleValue() < 0 ||
		// m_bIsInitBill)) {
		// /** 如果存货入库，则不调用手查批次号的方法 */
		// return;
		// }
		// if (m_sBillTypeCode.equals("45"))// edited by liubaohua 2011/01/21
		// {
		// // 如果是采购入库单则不取批次号档案的自定义项作为资源号
		// Object csourcetype = m_voBill.getItemValue(rownum, "csourcetype");
		// if (csourcetype != null && "21".equals(csourcetype)) {
		// try {
		// Object csourcebillbid = m_voBill.getItemValue(rownum,
		// "csourcebillbid");
		// IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance()
		// .lookup(IUAPQueryBS.class.getName());
		// query.executeQuery(
		// "select vdef1 from po_order_b where corder_bid ='"
		// + csourcebillbid + "'",
		// new ColumnProcessor());
		// getBillCardPanel().setBodyValueAt(csourcebillbid, rownum,
		// "vbcuser1");
		// m_voBill.setItemValue(rownum, "vbcuser1", csourcebillbid);
		// } catch (Exception e) {
		// }
		// }
		// }
		// edited by liubaohua 2011/01/21
		getLotRefbyHand(sItemKey);

		if (!getLotNumbRefPane().isClicked()) {
			LotNumbRefVO vosel = getLotNumbRefPane().getLotNumbRefVO();
			if (vosel != null) synlot(vosel, rownum);
			return;
		}
		synlot(voLot[voLot.length - 1], rownum);

		if (voLot != null && voLot.length > 1) {
			for (int j = 0; j < voLot.length - 1; j++) {
				getBillCardPanel().copyLine();
				voBillCopyLine();
				int iRowCount = getBillCardPanel().getBodyPanel().getTableModel().getRowCount();
				getBillCardPanel().pasteLine();
				// 增加的行数
				iRowCount = getBillCardPanel().getBodyPanel().getTableModel().getRowCount() - iRowCount;
				nc.ui.scm.pub.report.BillRowNo.pasteLineRowNo(getBillCardPanel(), m_sBillTypeCode, m_sBillRowNo, iRowCount);
				voBillPastLine();

				int iSelrow = getBillCardPanel().getBillTable().getSelectedRow() - 1;

				synlot(voLot[j], iSelrow);
				// 如果是出库处理界面，那么行号就要重新生成。
				if ("40080822".equals(m_sCurrentBillNode)) {
					String crowno = DataBuffer.getRowno();
					getBillCardPanel().setBodyValueAt(crowno, iSelrow, "crowno");
					m_voBill.setItemValue(iSelrow, "crowno", crowno);
					getBillCardPanel().setBodyValueAt(null, iSelrow, "cgeneralbid");
					m_voBill.setItemValue(iSelrow, "cgeneralbid", null);
				}
			}
		}
		//add 2014-12-06
		onAddNewRowNo();
		//end 

	}

	/**
	 * 创建者：余大英 功能：数量改变事件处理 参数：e单据编辑事件 返回： 例外： 日期：(2001-5-8 19:08:05)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	public void afterNumEdit(nc.ui.pub.bill.BillEditEvent e) {

		int row = e.getRow();

		String[] sfields = new String[] {
				"castunitid", "hsl", m_sAstItemKey, m_sNumItemKey
		};
		GeneralBillUICtl.changeNum(getBillCardPanel(), m_sNumItemKey, row, sfields, isFixFlag(row));
		GeneralBillUICtl.synUi2Vo(getBillCardPanel(), m_voBill, new String[] {
				"castunitid", "hsl", m_sAstItemKey, m_sNumItemKey
		}, row);
		afterNumEdit_1(row);

	}

	/**
	 * 创建者：余大英 功能：数量改变事件处理 参数：e单据编辑事件 返回： 例外： 日期：(2001-5-8 19:08:05)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	public void afterNumEdit_1(int row) {

		/** 数量清空：if 固定换算率：清空辅数量。 */
		Object oNumValue = getBillCardPanel().getBodyValueAt(row, m_sNumItemKey);

		if (oNumValue == null || oNumValue.toString().trim().length() < 1) {

			if (!m_bIsInitBill) {
				// 清空本行的业务日期
				getBillCardPanel().setBodyValueAt(null, row, "dbizdate");

			}

			if (getBillCardPanel().getBodyItem("ncountnum") != null) {
				getBillCardPanel().setBodyValueAt(null, row, "ncountnum");

			}
		} else {

			UFDouble nNum = new UFDouble(oNumValue.toString().trim());

			// 如果来源单据为特殊单据，控制数量和辅助数量不应超过应收发数量
			// afterNumEditFromSpe(e);

			if (!m_bIsInitBill) {
				if (getBillCardPanel().getBodyValueAt(row, "dbizdate") == null)

				// 非期初单据自动带出业务日期
				getBillCardPanel().setBodyValueAt(m_sLogDate, row, "dbizdate");

			} else {
				if (getBillCardPanel().getBodyValueAt(row, "dbizdate") == null) {

					// 期初单据自动带出系统启动日期
					nc.vo.pub.lang.UFDate dstart = new nc.vo.pub.lang.UFDate(m_sStartDate);
					nc.vo.pub.lang.UFDate dbiz = dstart.getDateBefore(1);
					getBillCardPanel().setBodyValueAt(dbiz.toString(), row, "dbizdate");
				}
			}
			UFDouble npacknum = (UFDouble) m_voBill.getItemValue(row, "npacknum");  //包装件数
			if (npacknum != null && npacknum.doubleValue() != 0) {
				double ntemp = nNum.div(npacknum).abs().doubleValue();
				getBillCardPanel().setBodyValueAt(new UFDouble(Math.ceil(ntemp)), row, "ncountnum");
			}

		}
		// 如果数量原来的值不为空，并且改变了方向，清空对应单据
		if (m_voBill.getItemVOs()[row].getInOutFlag() == InOutFlag.IN) clearCorrBillInfo(row);

		GeneralBillUICtl.synUi2Vo(getBillCardPanel(), m_voBill, new String[] {
				"ncountnum", "dbizdate"
		}, row);
		execEditFomulas(row, m_sNumItemKey);
		afterShouldNumEdit(new BillEditEvent(this, null, null, m_sShouldNumItemKey, row, BillItem.BODY));

		resetSpace(row);
	}

	/**
	 * ?user> 功能： 参数： 返回： 例外： 日期：(2005-2-1 14:41:41) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @param e
	 *            nc.ui.pub.bill.BillEditEvent
	 */
	protected void afterOnRoadEdit(nc.ui.pub.bill.BillEditEvent e) {
		int iSelrow = e.getRow();

		UFBoolean bonroadflag = new UFBoolean(getBillCardPanel().getBodyValueAt(iSelrow, e.getKey()).toString());

		// 对应单据号
		getBillCardPanel().setBodyValueAt(null, iSelrow, m_sCorBillCodeItemKey);
		// 对应单据类型
		getBillCardPanel().setBodyValueAt(null, iSelrow, "ccorrespondtype");
		// 对应单据表头OID
		// 单据模板库中表体位置两个不显示列ccorrespondhid,ccorrespondbid,以保存带出的对应表头，表体OID
		getBillCardPanel().setBodyValueAt(null, iSelrow, "ccorrespondhid");
		// 对应单据表体OID
		getBillCardPanel().setBodyValueAt(null, iSelrow, "ccorrespondbid");
		m_voBill.setItemValue(iSelrow, "ccorrespondbid", null);
		// getICCorBillRef().getCorBillBid());
		m_voBill.setItemValue(iSelrow, "ccorrespondhid", null);
		// getICCorBillRef().getCorBillHid());
		m_voBill.setItemValue(iSelrow, m_sCorBillCodeItemKey, null);
		// getICCorBillRef().getCorBillCode());
		m_voBill.setItemValue(iSelrow, "ccorrespondtype", null);
		// getICCorBillRef().getCorBillType());
		// clearLocSnData(iSelrow,e.getKey());
		if (!bonroadflag.booleanValue()) {
			m_voBill.setItemValue(e.getRow(), e.getKey(), new UFBoolean("N"));

		} else {
			m_voBill.setItemValue(e.getRow(), e.getKey(), new UFBoolean("Y"));

		}

	}

	/**
	 * 创建者：王乃军 功能：清除指定行、指定列的数据 参数： 返回： 例外： 日期：(2001-5-9 9:23:32)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	protected void clearRowData(int iBillFlag, int row, String sItemKey) {
		// 得到需清除的itemkey
		String[] saColKey = getClearIDs(iBillFlag, sItemKey);
		if (saColKey != null && saColKey.length > 0) clearRowData(row, saColKey);

	}

	/**
	 * 创建者：余大英 功能：清除指定行、指定列的数据 参数：过滤辅计量 返回： 例外： 日期：(2001-5-9 9:23:32)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	protected void filterMeas(int row) {

		nc.vo.scm.ic.bill.InvVO voInv = m_voBill.getItemInv(row);
		if (voInv.getIsAstUOMmgt() == null || voInv.getIsAstUOMmgt().intValue() == 0) return;

		nc.ui.pub.beans.UIRefPane refCast = (nc.ui.pub.beans.UIRefPane) getBillCardPanel().getBodyItem("castunitname").getComponent();

		m_voInvMeas.filterMeas(m_sCorpID, voInv.getCinventoryid(), refCast);

		/*
		 * String tempID = "0>1"; try { nc.vo.scm.ic.bill.InvVO voInv =
		 * m_voBill.getItemInv(row); if (voInv.getIsAstUOMmgt() != null &&
		 * voInv.getIsAstUOMmgt().toString().equals("1")) { ArrayList alRes =
		 * null; if (htCastUnit.containsKey(voInv.getCinventoryid())) { alRes =
		 * (ArrayList) htCastUnit.get(voInv.getCinventoryid()); } else { alRes =
		 * ((ArrayList) (GeneralHBO_Client .queryInfo(new Integer(999),
		 * voInv.getCinventoryid()))); //"20110003")));// }
		 * 
		 * if (alRes != null && alRes.size() > 0) {
		 * getBillCardPanel().getBillData
		 * ().getBodyItem("castunitname").setEnabled(true);
		 * getBillCardPanel().getBillData
		 * ().getBodyItem(m_sAstItemKey).setEnabled(true);
		 * 
		 * ArrayList alUnit = (ArrayList) alRes.get(0); String sPK = (String)
		 * (alUnit.get(0)); tempID = "pk_measdoc='" + sPK + "'"; if
		 * (!htCastUnit.containsKey(voInv.getCinventoryid()))
		 * htCastUnit.put(voInv.getCinventoryid(), alRes); for (int i = 1; i <
		 * alRes.size(); i++) { alUnit = (ArrayList) alRes.get(i); sPK =
		 * (String) (alUnit.get(0)); // if(!htCastUnit.containsKey(sPK)) //
		 * htCastUnit.put(sPK,alUnit); tempID = tempID + " or pk_measdoc='" +
		 * sPK + "'"; } ( (nc.ui.pub.beans.UIRefPane) getBillCardPanel()
		 * .getBodyItem("castunitname") .getComponent()) .setWhereString(
		 * tempID); return; } else {
		 * getBillCardPanel().getBillData().getBodyItem
		 * ("castunitname").setEnabled(false);
		 * getBillCardPanel().getBillData().getBodyItem
		 * (m_sAstItemKey).setEnabled(false); } } } catch (Exception e) {
		 * showHintMessage("未查到该存货的辅计量"); }
		 */
	}

	/**
	 * 过滤单据参照 创建者：张欣 功能：初始化参照过滤 参数： 返回： 例外： 日期：(2001-7-17 10:33:20)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 */
	public void filterRef(String sCorpID) {
		try {
			// 过滤仓库参照
			nc.ui.pub.bill.BillItem bi = getBillCardPanel().getHeadItem(m_sMainWhItemKey);
			// v31sp1需求: 邵兵 2005-09-13
			// 出入库单的仓库参照中，过滤掉直运仓，对于系统补单的，直运仓不显示也没有问题。
			RefFilter.filtWh(bi, sCorpID, new String[] {
				"and isdirectstore = 'N'"
			});

			if (!(m_sBillTypeCode.equals("4Y") || m_sBillTypeCode.equals("4E"))) {
				bi = getBillCardPanel().getHeadItem("cotherwhid");
				RefFilter.filtWh(bi, sCorpID, null);
			}
			// 过滤库存组织 add by hanwei 2004-05-09
			bi = getBillCardPanel().getHeadItem("pk_calbody");
			RefFilter.filtCalbody(bi, sCorpID, null);

			// 过滤存货编码
			bi = getBillCardPanel().getBodyItem("cinventorycode");
			nc.ui.pub.beans.UIRefPane invRef = (nc.ui.pub.beans.UIRefPane) bi.getComponent();
			invRef.setTreeGridNodeMultiSelected(true);
			invRef.setMultiSelectedEnabled(true);
			// invRef.getRefModel().setIsDynamicCol(true);
			// invRef.getRefModel().setDynamicColClassName("nc.ui.scm.pub.RefDynamic");

			RefFilter.filtInv(bi, sCorpID, null);

			// 过滤表头存货编码
			bi = getBillCardPanel().getHeadItem("cinventoryid");
			RefFilter.filtInv(bi, sCorpID, null);
			// 过滤表头第二个仓库参照
			bi = getBillCardPanel().getHeadItem(m_sAstWhItemKey);
			if (bi != null && bi.getDataType() == BillItem.UFREF) RefFilter.filtWasteWh(bi, sCorpID, null);
			// 过滤客户参照
			bi = getBillCardPanel().getHeadItem("ccustomerid");
			RefFilter.filtCust(bi, sCorpID, null);

			// 过滤业务员参照 2003-11-20
			bi = getBillCardPanel().getHeadItem("cbizid");
			RefFilter.filterPsnByDept(bi, sCorpID, new String[] {
				null
			});

			// 过滤供应商参照
			bi = getBillCardPanel().getHeadItem("cproviderid");
			RefFilter.filtProvider(bi, sCorpID, null);
			// 过滤收发类型参照（出入库不一样）
			bi = getBillCardPanel().getHeadItem("cdispatcherid");
			if (m_iBillInOutFlag == InOutFlag.IN) RefFilter.filtDispatch(bi, sCorpID, 0, null);
			else RefFilter.filtDispatch(bi, sCorpID, 1, null);
			// 表头发运地址:不用自动检查，返回名称。
			if (getBillCardPanel().getHeadItem("vdiliveraddress") != null && getBillCardPanel().getHeadItem("vdiliveraddress").getComponent() != null) {
				nc.ui.pub.beans.UIRefPane vdlvr = (nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("vdiliveraddress").getComponent();
				vdlvr.setAutoCheck(false);
				vdlvr.setReturnCode(true);

				filterVdiliveraddressRef(true, -1);

				// 去掉该约束，应为应该用表体的收货客户约束
				getBillCardPanel().getHeadItem("vdiliveraddress").setDataType(nc.ui.pub.bill.BillItem.USERDEF);
				bi = getBillCardPanel().getHeadItem("ccustomerid");
				if (bi != null && bi.getComponent() != null) {
					String ccustomerid = ((nc.ui.pub.beans.UIRefPane) bi.getComponent()).getRefPK();
					if (ccustomerid == null && m_voBillRefInput != null && m_voBillRefInput.getHeaderVO() != null) ccustomerid = m_voBillRefInput.getHeaderVO().getCcustomerid();
					if (ccustomerid != null && vdlvr.getRefModel() instanceof nc.ui.scm.ref.prm.CustAddrRefModel) {
						((nc.ui.scm.ref.prm.CustAddrRefModel) vdlvr.getRefModel()).setCustId(ccustomerid);
						// vdlvr
						// .setWhereString("pk_cubasdoc = (select pk_cubasdoc from bd_cumandoc where pk_cumandoc = '"
						// + ccustomerid + "')");
					}
				}

			}
			// 过滤成本对象参照
			if (getBillCardPanel().getBodyItem(m_sCostObjectItemKey) != null && getBillCardPanel().getBodyItem(m_sCostObjectItemKey).getComponent() != null) {
				if (!m_sBillTypeCode.equals("4F")) {
					bi = getBillCardPanel().getBodyItem(m_sCostObjectItemKey);
					nc.ui.ic.pub.bill.initref.RefFilter.filterCostObject(bi);
					// filterCostObject();
				}
			}
			// // 过滤计量器具
			// nc.ui.pub.bill.BillItem bi2 = getBillCardPanel().getHeadItem(
			// "pk_measware");
			// // UIRefPane refCalbody =
			// //
			// (UIRefPane)getBillCardPanel().getHeadItem("pk_calbody").getComponent();
			// // String pk_calbody = refCalbody.getRefPK();
			// // String[] s = new String[1];
			// // s[0] = " and mm_jldoc.gcbm='" + pk_calbody + "'";
			// RefFilter.filtMeasware(bi2, sCorpID, null);

			// 表体发运地址
			bi = getBillCardPanel().getBodyItem("vdiliveraddress");
			if (bi != null) {
				((UIRefPane) bi.getComponent()).setAutoCheck(false);
			}

		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
		}
	}

	/**
	 * 创建者：仲瑞庆 功能：由传入的单据类型、字段，获得当该字段改变后，应改变的其他字段列表 参数：iBillFlag
	 * 单据类型，当为普通单据，传入0，当为特殊单据，传入1 已有 存货 cinventorycode， 表体仓库 cwarehousename， 自由项
	 * vfree0， 表头出库仓库 coutwarehouseid， 表头仓库 cwarehouseid 返回： 例外： 日期：(2001-7-18
	 * 上午 9:20) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return java.lang.String[]
	 * @param sWhatChange
	 *            java.lang.String
	 */
	protected String[] getClearIDs(int iBillFlag, String sWhatChange) {
		if (sWhatChange == null) return null;
		String[] saReturnString = null;
		sWhatChange = sWhatChange.trim();
		// 跟踪到入库单时要特殊处理
		// m_sCorBillCodeItemKey,
		// "ccorrespondtype","ccorrespondhid","ccorrespondbid"
		if (sWhatChange.equals("cinventorycode")) {
			// 存货
			saReturnString = new String[] {
					"vbatchcode", "vfree0", m_sNumItemKey, m_sAstItemKey, "scrq", "dvalidate", m_sCorBillCodeItemKey, "ccorrespondtype", "ccorrespondhid", "ccorrespondbid", m_sShouldAstItemKey, "castunitid", "hsl", "nprice", "nmny", "cqualitylevelid",// 质量等级
					"vvendbatchcode",// 供应商批次
					"tchecktime",
					"vdef1",
					"vdef2",
					"vdef3",
					"vdef4",
					"vdef5",
					"vdef6",
					"vdef7",
					"vdef8",
					"vdef9",
					"vdef10",
					"vdef11",
					"vdef12",
					"vdef13",
					"vdef14",
					"vdef15",
					"vdef16",
					"vdef17",
					"vdef18",
					"vdef19",
					"vdef20"// 检验时间
			};
		} else if ((sWhatChange.equals("cwarehousename")) && (iBillFlag == 1)) {
			// 特殊单的表体行内仓库
			saReturnString = new String[] {
					"vbatchcode", m_sNumItemKey, m_sAstItemKey, "scrq", "dvalidate", "vfree0", "cqualitylevelid",// 质量等级
					"vvendbatchcode",// 供应商批次
					"tchecktime",
					"vdef1",
					"vdef2",
					"vdef3",
					"vdef4",
					"vdef5",
					"vdef6",
					"vdef7",
					"vdef8",
					"vdef9",
					"vdef10",
					"vdef11",
					"vdef12",
					"vdef13",
					"vdef14",
					"vdef15",
					"vdef16",
					"vdef17",
					"vdef18",
					"vdef19",
					"vdef20"// 检验时间
			};
		} else if (sWhatChange.equals("vfree0")) {
			// 自由项
			saReturnString = new String[] {
					"vbatchcode", "scrq", "dvalidate", "cqualitylevelid",// 质量等级
					"vvendbatchcode",// 供应商批次
					"tchecktime"// 检验时间
			};
		} else if (sWhatChange.equals("coutwarehouseid")) {
			saReturnString = new String[] {
					"vbatchcode", m_sNumItemKey, m_sAstItemKey, "scrq", "dvalidate", "cqualitylevelid",// 质量等级
					"vvendbatchcode",// 供应商批次
					"tchecktime",
					"vdef1",
					"vdef2",
					"vdef3",
					"vdef4",
					"vdef5",
					"vdef6",
					"vdef7",
					"vdef8",
					"vdef9",
					"vdef10",
					"vdef11",
					"vdef12",
					"vdef13",
					"vdef14",
					"vdef15",
					"vdef16",
					"vdef17",
					"vdef18",
					"vdef19",
					"vdef20"// 检验时间
			};
		} else if ((sWhatChange.equals(m_sMainWhItemKey)) && (iBillFlag == 0)) {
			// 在afterEdit中调用clearLocSN处理locator/sn
			// saReturnString= new String[2];
			// saReturnString[0]= "locator";
			// saReturnString[1]= "sn";
			// saReturnString[2]= m_sAstItemKey;
			// saReturnString[3]= "scrq";
			// saReturnString[4]= "dvalidate";
		} else if (sWhatChange.equals("vbatchcode")) {
			// 批次
			saReturnString = new String[] {
					"scrq", "dvalidate", "cqualitylevelid",// 质量等级
					"vvendbatchcode",// 供应商批次
					"tchecktime",
					"vdef1",
					"vdef2",
					"vdef3",
					"vdef4",
					"vdef5",
					"vdef6",
					"vdef7",
					"vdef8",
					"vdef9",
					"vdef10",
					"vdef11",
					"vdef12",
					"vdef13",
					"vdef14",
					"vdef15",
					"vdef16",
					"vdef17",
					"vdef18",
					"vdef19",
					"vdef20"// 检验时间
			};
		} else if (sWhatChange.equals("castunitid")) {
			// 辅计量
			// if (m_sTrackedBillFlag.equals("Y")) {
			// saReturnString = new String[4];

			// saReturnString[0] = "ccorrespondtype";
			// saReturnString[1] = "ccorrespondhid";
			// saReturnString[2] = "ccorrespondbid";
			// saReturnString[3] = m_sCorBillCodeItemKey;
			// saReturnString[4] = "scrq";
			// saReturnString[5] = "dvalidate";
			// } else {
			// saReturnString = new String[2];

			// saReturnString[0] = "scrq";
			// saReturnString[1] = "dvalidate";

			// }
		}
		// if (saReturnString != null)
		// for (int i = 0; i < saReturnString.length; i++)
		// SCMEnv.out("will clear " + saReturnString[i]);
		return saReturnString;
	}

	/**
	 * 此处插入方法说明。 功能：得到对应单参照 参数： 返回： 例外： 日期：(2001-7-18 10:54:47)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return nc.ui.ic.pub.corbillref.ICCorBillRef
	 */
	public nc.ui.ic.pub.corbillref.ICCorBillRefPane getICCorBillRef() {
		if (m_aICCorBillRef == null) {
			m_aICCorBillRef = new nc.ui.ic.pub.corbillref.ICCorBillRefPane();
			m_aICCorBillRef.setReturnCode(true);
			m_aICCorBillRef.setMultiSelectedEnabled(true);

		}
		return m_aICCorBillRef;
	}

	private String[] getBodyFormula() {
		if (m_sItemField == null) {
			BillItem[] bodyItems = getBillCardPanel().getBillData().getBodyItems();
			m_sItemField = BillUtil.getFormulas(bodyItems, IBillItem.LOAD);
		}
		return m_sItemField;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-7-11 下午 11:19)
	 * 
	 * @return nc.ui.ic.pub.InvOnHandDialog
	 */
	protected nc.ui.ic.pub.InvOnHandDialog getIohdDlg() {
		if (null == m_iohdDlg) {
			m_iohdDlg = new InvOnHandDialog(this);
		}
		return m_iohdDlg;
	}

	/**
	 * 如果用户手工修改批次号，则查库，正确带出失效日期及对应单据号，不正确，清空。 创建者：张欣 功能： 参数： 返回： 例外：
	 * 日期：(2001-6-14 10:25:33) 修改日期，修改人，修改原因，注释标志：
	 * 
	 */
	public void getLotRefbyHand(String ColName) {
		int iSelrow = getBillCardPanel().getBillTable().getSelectedRow();
		String strColName = ColName;
		if (strColName == null) { return; }
		String sbatchcode = null;

		sbatchcode = (String) getBillCardPanel().getBodyValueAt(iSelrow, "vbatchcode");
		if (sbatchcode == null || sbatchcode.trim().length() <= 0 || getLotNumbRefPane().isClicked()) { return; }

		getLotNumbRefPane().checkData();
		// V501新需求 库存无实发批次录入处理 因此注释掉以下行
		// InvVO voInv = m_voBill.getItemInv(iSelrow);
		// if (!bOK) {
		// if (voInv.getNegallowed() == null
		// || !voInv.getNegallowed().booleanValue()) {
		// getBillCardPanel().setBodyValueAt(null, iSelrow, "vbatchcode");
		// getBillCardPanel().setBodyValueAt(null, iSelrow, "dvalidate"); //
		// 清空表体失效日期
		// getBillCardPanel().setBodyValueAt(null, iSelrow, "scrq"); // 清空表体生产日期
		// getBillCardPanel().setBodyValueAt(null, iSelrow, "cqualitylevelid");
		// // 清空表体质量等级
		// getBillCardPanel().setBodyValueAt(null, iSelrow, "vvendbatchcode");
		// // 清空表体供应商批次
		// getBillCardPanel().setBodyValueAt(null, iSelrow, "tchecktime"); //
		// 清空表体检验时间
		//				
		// String sKey=null;
		// for(int i=1;i<21;i++){
		// sKey="vdef"+String.valueOf(i).trim();
		// getBillCardPanel().setBodyValueAt(null, iSelrow, sKey); // 清空自定义项
		// }
		//				
		//				
		// }
		// }

	}

	/**
	 * 是否固定换算率
	 */
	protected boolean isFixFlag(int row) {

		boolean isFixFlag = false;
		if (row >= 0 && m_voBill.getItemVOs()[row] != null && m_voBill.getItemVOs()[row].getIsSolidConvRate() != null) isFixFlag = m_voBill.getItemVOs()[row].getIsSolidConvRate().intValue() == 1 ? true : false;

		return isFixFlag;

	}

	/**
	 * 存量查询 修改 创建日期：(2001-4-18 19:45:39)
	 */
	public void onRowQuyQty() { // finished
		nc.vo.ic.pub.bill.GeneralBillVO nowVObill = null;
		int rownow = -1;
		if (m_iCurPanel == BillMode.Card) {
			rownow = getBillCardPanel().getBillTable().getSelectedRow();
			nowVObill = m_voBill;
		} else {
			rownow = getBillListPanel().getBodyTable().getSelectedRow();
			nowVObill = (GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow);
		}

		String WhID = "";
		String InvID = "";

		if ((nowVObill != null) && (rownow >= 0)) {

			WhID = (String) nowVObill.getHeaderValue(m_sMainWhItemKey);
			InvID = (String) nowVObill.getItemValue(rownow, "cinventoryid");
		}

		getIohdDlg().setParam(WhID, InvID);

		getIohdDlg().onQuery();
		getIohdDlg().showModal();
	}

	/**
	 * 创建者：王乃军 功能：确认（保存）处理 参数：无 返回： true: 成功 false: 失败
	 * 
	 * 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 2001/10/29,wnj,拆分出保存新增/保存修改两个方法，使得更规范。
	 * 
	 * 
	 * 
	 */
	public boolean onSave() {
		long lStartTime = System.currentTimeMillis();
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH044")/* @res "正在保存" */);

		getBillCardPanel().getBillData().execBodyValidateFormulas();

		boolean bSave = false;
		bSave = onSaveBase();
		// 基类中保存后的动作
		if (bSave) {
			setBillBCVOStatus(m_voBill, nc.vo.pub.VOStatus.UNCHANGED);
		}
		if (m_bOnhandShowHidden) {
			m_pnlQueryOnHand.clearCache();
			m_pnlQueryOnHand.fresh();
		}
		// v5 如果是参照生长多张单据，保存成功后要删除列表下的对应单据
		if (m_bRefBills && bSave) {
			// 删除列表下的对应单据
			// delBillOfList(m_iLastSelListHeadRow);
			if (m_iLastSelListHeadRow >= 0) removeBillsOfList(new int[] {
				m_iLastSelListHeadRow
			});
			updateHeadTsWhenMutiBillSave(m_voBill);
			// 切换
			onSwitch();
		}
		nc.vo.scm.pub.SCMEnv.showTime(lStartTime, "Bill Save");

		return bSave;
	}

	/**
	 * 创建者：yangbo
	 * 
	 * 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 拉式生成多单时保存来源单据相同的单据，需要刷新相关单据的来源单据表头ts
	 * 
	 * 
	 * 
	 */
	private void updateHeadTsWhenMutiBillSave(GeneralBillVO billvo) {
		if (billvo == null || billvo.getItemVOs() == null) return;
		HashSet<String> idlist = new HashSet<String>(50);
		for (int i = 0; i < billvo.getItemVOs().length; i++) {
			String srcbilltype = billvo.getItemVOs()[i].getCsourcetype();
			String srcbillid = billvo.getItemVOs()[i].getCsourcebillhid();
			if (srcbilltype == null || srcbillid == null) continue;
			if (idlist.contains(srcbilltype + srcbillid)) continue;
			updateHeadTsWhenMutiBillSave(srcbilltype, srcbillid);
			idlist.add(srcbilltype + srcbillid);
		}
	}

	/**
	 * 创建者：yangbo
	 * 
	 * 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 拉式生成多单时保存来源单据相同的单据，需要刷新相关单据的来源单据表头ts
	 * 
	 * 
	 * 
	 */
	private void updateHeadTsWhenMutiBillSave(String srcbilltype, String srchid) {
		if (srcbilltype == null || srcbilltype.trim().length() <= 0 || srchid == null || srchid.trim().length() <= 0) return;
		if (m_alListData == null || m_alListData.size() <= 0) return;
		// ArrayList billvoslist = new ArrayList();
		// billvoslist.addAll(m_alListData);
		// if(m_voBill!=null && m_voBill.getHeaderVO()!=null &&
		// !billvoslist.contains(m_voBill))
		// billvoslist.add(m_voBill);

		ArrayList<GeneralBillHeaderVO> vohlist = new ArrayList<GeneralBillHeaderVO>(10);
		ArrayList<GeneralBillItemVO> voblist = new ArrayList<GeneralBillItemVO>(20);
		for (int i = 0; i < m_alListData.size(); i++) {
			GeneralBillVO billvo = (GeneralBillVO) m_alListData.get(i);
			GeneralBillItemVO[] bodyvos = billvo.getItemVOs();
			GeneralBillHeaderVO headvo = billvo.getHeaderVO();
			if (bodyvos == null || bodyvos.length <= 0) continue;
			for (int j = 0; j < bodyvos.length; j++)
				if (srcbilltype.equals(bodyvos[j].getCsourcetype()) && srchid.equals(bodyvos[j].getCsourcebillhid())) {
					if (!voblist.contains(bodyvos[j])) voblist.add(bodyvos[j]);
					if (!vohlist.contains(headvo)) vohlist.add(headvo);
				}
		}

		if (vohlist.size() <= 0 || voblist.size() <= 0) return;

		String stable = GenMethod.getHeadTableName(srcbilltype);
		String pkfieldname = GenMethod.getHeadColName(srcbilltype);
		if (stable == null || stable.trim().length() <= 0 || pkfieldname == null || pkfieldname.trim().length() <= 0) return;
		ICDataSet set = nc.ui.ic.pub.tools.GenMethod.queryData(stable, pkfieldname, new String[] {
			"ts"
		}, new int[] {
			SmartFieldMeta.JAVATYPE_UFDATETIME
		}, new String[] {
			srchid
		}, " dr=0 ");
		String ts = null;
		if (set != null && set.getRowCount() > 0 && set.getValueAt(0, 0) != null) {
			ts = set.getValueAt(0, 0).toString();
			if (ts == null || ts.trim().length() <= 0) return;
			for (int i = 0; i < voblist.size(); i++)
				voblist.get(i).setCsourceheadts(ts);
			for (int i = 0; i < vohlist.size(); i++)
				vohlist.get(i).setTs(ts);

		}
	}

	/**
	 * 当用户选择批次号后，自动带出，失效日期与对应单句号，单据类型。 创建者：张欣 功能： 参数： 返回： 例外： 日期：(2001-6-13
	 * 17:38:31) 修改日期，修改人，修改原因，注释标志：
	 * 
	 */
	public void pickupLotRef(String colname) {
		String s = colname;
		// 批次号参照带出失效日期和对应单据号及对应单据类型

		String sbatchcode = null;
		int iSelrow = getBillCardPanel().getBillTable().getSelectedRow();
		if (s == null) { return; }
		if (s.equals("vbatchcode")) {
			// 判断当批次号参数为空时,应该返回;
			// if(arytemp[0]==null||arytemp[3]==null){
			// return;
			// }

			sbatchcode = (String) getBillCardPanel().getBodyValueAt(iSelrow, "vbatchcode");

			if (sbatchcode != null && sbatchcode.trim().length() > 0) {
				nc.vo.scm.ic.bill.InvVO voInv = m_voBill.getItemInv(iSelrow);
				// /辅单位
				try {
					if (voInv.getIsAstUOMmgt().intValue() == 1) {
						nc.ui.pub.beans.UIRefPane refCastunit = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getBodyItem("castunitname").getComponent());
						Object oldvalue = getBillCardPanel().getBodyValueAt(iSelrow, "castunitid");
						m_voBill.setItemValue(iSelrow, "cselastunitid", getLotNumbRefPane().getLotNumbRefVO().getCastunitid());
						nc.vo.ic.pub.DesassemblyVO voDesa = (nc.vo.ic.pub.DesassemblyVO) m_voBill.getItemValue(iSelrow, "desainfo");
						if (voDesa.getMeasInfo() == null) {
							nc.vo.bd.b15.MeasureRateVO[] voMeasInfo = m_voInvMeas.getMeasInfo(m_sCorpID, voInv.getCinventoryid());
							voDesa.setMeasInfo(voMeasInfo);
						}
						m_voBill.setItemValue(iSelrow, "idesatype", new Integer(voDesa.getDesaType()));
						getBillCardPanel().setBodyValueAt(new Integer(voDesa.getDesaType()), iSelrow, "idesatype");
						getBillCardPanel().setBodyValueAt(getLotNumbRefPane().getLotNumbRefVO().getCastunitid(), iSelrow, "cselastunitid");
						if (voDesa.getDesaType() == nc.vo.ic.pub.DesassemblyVO.TYPE_NO) {
							refCastunit.setPK(getLotNumbRefPane().getLotNumbDlg().getAssUnit());
							if (getLotNumbRefPane().getLotNumbDlg().getAssUnit() == null) refCastunit.setName(null);

							Object source = getBillCardPanel().getBodyItem("castunitname");

							Object newvalue = getLotNumbRefPane().getRefTableBodyID();
							nc.ui.pub.bill.BillEditEvent even = new nc.ui.pub.bill.BillEditEvent(source, newvalue, oldvalue, "castunitname", iSelrow, BillItem.BODY);
							afterAstUOMEdit(even);
						}
					}
				} catch (Exception e) {
				}
				try {
					// 该批次的失效日期
					getBillCardPanel().setBodyValueAt(getLotNumbRefPane().getRefInvalideDate() == null ? "" : getLotNumbRefPane().getRefInvalideDate().toString(), iSelrow, "dvalidate");
				} catch (Exception e) {
				}
				if (getLotNumbRefPane().getRefBillCode() != null) {
					try {
						// 对应单据号
						getBillCardPanel().setBodyValueAt(getLotNumbRefPane().getRefBillCode() == null ? "" : getLotNumbRefPane().getRefBillCode(), iSelrow, m_sCorBillCodeItemKey);
					} catch (Exception e) {
					}
					try {
						// 对应单据类型
						getBillCardPanel().setBodyValueAt(getLotNumbRefPane().getRefBillType() == null ? "" : getLotNumbRefPane().getRefBillType(), iSelrow, "ccorrespondtype");
					} catch (Exception e) {
					}
					try {
						// 对应单据表头OID
						// 单据模板库中表体位置两个不显示列ccorrespondhid,ccorrespondbid,以保存带出的对应表头，表体OID
						getBillCardPanel().setBodyValueAt(getLotNumbRefPane().getRefTableHeaderID() == null ? "" : getLotNumbRefPane().getRefTableHeaderID(), iSelrow, "ccorrespondhid");
					} catch (Exception e) {
					}
					try {
						// 对应单据表体OID
						getBillCardPanel().setBodyValueAt(getLotNumbRefPane().getRefTableBodyID() == null ? "" : getLotNumbRefPane().getRefTableBodyID(), iSelrow, "ccorrespondbid");

					} catch (Exception e) {
					}
				}

				try {
					// //生产日期
					if (voInv.getIsValidateMgt().intValue() == 1) {
						nc.vo.pub.lang.UFDate dvalidate = getLotNumbRefPane().getRefInvalideDate();
						if (dvalidate != null) {
							getBillCardPanel().setBodyValueAt(dvalidate.getDateBefore(voInv.getQualityDay().intValue()).toString(), iSelrow, "scrq");
						}

					}
				} catch (Exception e) {
				}

				// /自由项
				try {
					if (voInv.getIsFreeItemMgt().intValue() == 1) {
						FreeVO freevo = getLotNumbRefPane().getLotNumbDlg().getFreeVO();
						if (freevo != null && freevo.getVfree0() != null) {
							InvVO invvo = m_voBill.getItemInv(iSelrow);
							if (invvo != null) invvo.setFreeItemVO(freevo);
							getFreeItemRefPane().setFreeItemParam(invvo);
							getBillCardPanel().setBodyValueAt(freevo.getVfree0(), iSelrow, "vfree0");
							for (int i = 1; i <= FreeVO.FREE_ITEM_NUM; i++) {
								if (getBillCardPanel().getBodyItem("vfree" + i) != null)

								getBillCardPanel().setBodyValueAt(freevo.getAttributeValue("vfree" + i), iSelrow, "vfree" + i);
								else getBillCardPanel().setBodyValueAt(null, iSelrow, "vfree" + i);
							}
						}
						m_voBill.setItemFreeVO(iSelrow, freevo);
					}
				} catch (Exception e) {

				}

				// 同步改变m_voBill
				m_voBill.setItemValue(iSelrow, "vbatchcode", getLotNumbRefPane().getRefLotNumb());
				m_voBill.setItemValue(iSelrow, "dvalidate", getLotNumbRefPane().getRefInvalideDate());
				if (getLotNumbRefPane().getRefBillCode() != null) {
					m_voBill.setItemValue(iSelrow, "ccorrespondbid", getLotNumbRefPane().getRefTableBodyID());
					m_voBill.setItemValue(iSelrow, "ccorrespondhid", getLotNumbRefPane().getRefTableHeaderID());
					m_voBill.setItemValue(iSelrow, m_sCorBillCodeItemKey, getLotNumbRefPane().getRefBillCode());
					m_voBill.setItemValue(iSelrow, "ccorrespondtype", getLotNumbRefPane().getRefBillType());
				}

			} else {
				// getBillCardPanel().setBodyValueAt("", iSelrow, "dvalidate");
				// //清空表体所有失效日期
				// getBillCardPanel().setBodyValueAt("", iSelrow,
				// m_sCorBillCodeItemKey);
				// //清空表体所有对应单据号
				// getBillCardPanel().setBodyValueAt("", iSelrow,
				// "ccorrespondtype");
				// //清空表体所有对应单据类型
				// getBillCardPanel().setBodyValueAt("", iSelrow,
				// "ccorrespondhid");
				// //清空表体所有对应单据表头OID
				// getBillCardPanel().setBodyValueAt("", iSelrow,
				// "ccorrespondbid");
				// //清空表体所有对应单据表体OID
				// getBillCardPanel().setBodyValueAt("", iSelrow, "scrq");
				// //清空表体所有失效日期
				// //同步改变m_voBill
				// m_voBill.setItemValue(iSelrow, "vbatchcode", null);
				// m_voBill.setItemValue(iSelrow, "dvalidate", null);
				// m_voBill.setItemValue(iSelrow, "ccorrespondbid", null);
				// m_voBill.setItemValue(iSelrow, "ccorrespondhid", null);
				// m_voBill.setItemValue(iSelrow, m_sCorBillCodeItemKey, null);
				// m_voBill.setItemValue(iSelrow, "ccorrespondtype", null);

			}
		}
	}

	/**
	 * 创建者：余大英 功能：应发辅数量编辑事件处理 参数：e单据编辑事件 返回： 例外： 日期：(2001-5-8 19:08:05)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 辅数量*换算率=数量
	 * 
	 */

	public void afterShouldAstNumEdit(nc.ui.pub.bill.BillEditEvent e) {
		int rownum = e.getRow();
		if (getBillCardPanel().getBodyItem(m_sShouldAstItemKey) == null || getBillCardPanel().getBodyItem(m_sShouldNumItemKey) == null) return;

		String[] sfields = new String[] {
				"castunitid", "hsl", m_sShouldAstItemKey, m_sShouldNumItemKey
		};
		GeneralBillUICtl.changeNum(getBillCardPanel(), m_sShouldAstItemKey, rownum, sfields, true);
		GeneralBillUICtl.synUi2Vo(getBillCardPanel(), m_voBill, sfields, rownum);

	}

	/**
	 * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2001-7-18 15:01:19) 修改日期，修改人，修改原因，注释标志：
	 * 
	 */

	public void afterShouldNumEdit(nc.ui.pub.bill.BillEditEvent e) {
		if (getBillCardPanel().getBodyItem(m_sShouldAstItemKey) == null || getBillCardPanel().getBodyItem(m_sShouldNumItemKey) == null) return;
		int rownum = e.getRow();

		String[] sfields = new String[] {
				"castunitid", "hsl", m_sShouldAstItemKey, m_sShouldNumItemKey
		};
		GeneralBillUICtl.changeNum(getBillCardPanel(), m_sShouldNumItemKey, rownum, sfields, true);
		GeneralBillUICtl.synUi2Vo(getBillCardPanel(), m_voBill, sfields, rownum);
	}

	/**
	 * 创建者：王乃军 功能：单据编辑后处理 参数： 返回： 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	abstract protected void afterBillEdit(nc.ui.pub.bill.BillEditEvent e);

	/**
	 * 创建者：王乃军 功能：表体行列选择改变 参数： 返回： 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	abstract protected void afterBillItemSelChg(int iRow, int iCol);

	/**
	 * 创建者：王乃军 功能：库存组织改变事件处理 参数：e单据编辑事件 返回： 例外： 日期：(2001-5-8 19:08:05)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	public void afterCalbodyEdit(nc.ui.pub.bill.BillEditEvent e) {
		try {
			String sNewID = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem(e.getKey()).getComponent()).getRefPK();
			// 清空了库存组织
			// 如果当前的仓库不属于
			// 过滤仓库参照
			String sConstraint[] = null;
			String sConstraint1[] = null;
			if (sNewID != null) {
				sConstraint = new String[1];
				sConstraint[0] = " AND isdirectstore = 'N' AND pk_calbody='" + sNewID + "'";
				sConstraint1 = new String[1];
				sConstraint1[0] = " and mm_jldoc.gcbm='" + sNewID + "'";
			}
			nc.ui.pub.bill.BillItem bi = getBillCardPanel().getHeadItem(m_sMainWhItemKey);
			RefFilter.filtWh(bi, m_sCorpID, sConstraint);
			// // 过滤计量器具
			// nc.ui.pub.bill.BillItem bi1 = getBillCardPanel().getHeadItem(
			// "pk_measware");
			// RefFilter.filtMeasware(bi1, m_sCorpID, sConstraint1);
			// 处理:clear warehouse
			nc.ui.pub.bill.BillItem biWh = getBillCardPanel().getHeadItem(m_sMainWhItemKey);
			if (biWh != null) biWh.setValue(null);

			// 过滤成本对象
			// filterCostObject();

		} catch (Exception e2) {
			SCMEnv.out(e2);
		}

	}

	/**
	 * 创建者：仲瑞庆 功能：换算率修改 参数： 返回： 例外： 日期：(2001-11-20 14:01:52) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @param row
	 *            int
	 */
	public void afterHslEdit(nc.ui.pub.bill.BillEditEvent e) {
		// 行，选中表头字段时为-1
		int rownum = e.getRow();

		boolean isFixFlag = isFixFlag(rownum);

		String[] sfields = new String[] {
				"castunitid", "hsl", m_sAstItemKey, m_sNumItemKey
		};
		GeneralBillUICtl.changeNum(getBillCardPanel(), "hsl", rownum, sfields, isFixFlag);
		GeneralBillUICtl.synUi2Vo(getBillCardPanel(), m_voBill, sfields, rownum);

		sfields = new String[] {
				"castunitid", "hsl", "ngrossastnum", "ngrossnum"
		};
		GeneralBillUICtl.changeNum(getBillCardPanel(), "hsl", rownum, sfields, true);
		GeneralBillUICtl.synUi2Vo(getBillCardPanel(), m_voBill, sfields, rownum);

		sfields = new String[] {
				"castunitid", "hsl", "ntareastnum", "ntarenum"
		};
		GeneralBillUICtl.changeNum(getBillCardPanel(), "hsl", rownum, sfields, true);
		GeneralBillUICtl.synUi2Vo(getBillCardPanel(), m_voBill, sfields, rownum);

		if (!m_bIsInitBill) {

			sfields = new String[] {
					"castunitid", "hsl", m_sShouldAstItemKey, m_sShouldNumItemKey
			};
			GeneralBillUICtl.changeNum(getBillCardPanel(), "hsl", rownum, sfields, isFixFlag);
			GeneralBillUICtl.synUi2Vo(getBillCardPanel(), m_voBill, sfields, rownum);

		}

		afterNumEdit_1(rownum);
	}

	/**
	 * 创建者：余大英 功能：货位修改事件处理 参数：e单据编辑事件 返回： 例外： 日期：(2001-5-8 19:08:05)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	public void afterSpaceEdit(nc.ui.pub.bill.BillEditEvent e) {
		nc.ui.pub.beans.UIRefPane refSpace = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getBodyItem("vspacename").getComponent());

		String cspaceid = refSpace.getRefPK();
		String csname = refSpace.getRefName();

		setRowSpaceData(e.getRow(), cspaceid, csname);

	}

	/**
	 * 创建者：王乃军 功能：单据表体编辑事件前触发处理 参数：e单据编辑事件 返回： 例外： 日期：(2001-5-8 19:08:05)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */

	abstract public boolean beforeBillItemEdit(nc.ui.pub.bill.BillEditEvent e);

	/**
	 * 创建者：王乃军 功能：表体行列选择改变 参数： 返回： 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	abstract protected void beforeBillItemSelChg(int iRow, int iCol);

	/**
	 * UAP提供的编辑前控制
	 * 
	 * @param value
	 * @param row
	 * @param itemkey
	 * @return
	 */
	public boolean isCellEditable(boolean value/* BillModel的isCellEditable的返回值 */, int row/* 界面行序号 */, String itemkey/* 当前列的itemkey */) {

		// nc.vo.scm.pub.ctrl.GenMsgCtrl.printHint("beforeEdit:" + e.getKey());
		if (m_iMode == BillMode.Browse) return false;
		getBillCardPanel().stopEditing();

		boolean isEditable = true;
		String sItemKey = itemkey;
		nc.ui.pub.bill.BillItem biCol = getBillCardPanel().getBodyItem(sItemKey);
		int iRow = row;
		// int iPos = e.getPos();
		// forHF,临时用。请客户化帮助。由于同一批次辅计量不同而造成afterEdit触发不了。
		Object oBatchcode = getBillCardPanel().getBodyValueAt(iRow, "vbatchcode");
		if (oBatchcode != null) {
			getBillCardPanel().setBodyValueAt(oBatchcode.toString().trim(), iRow, "vbatchcode");
		}

		if (sItemKey == null || biCol == null) { return false; }

		// 模版设置
		if (!biCol.isEdit() || !biCol.isEnabled()) {

		return false; }

		if (m_voBill == null) {
			// biCol.setEnabled(false);
			return false;
		}
		nc.vo.scm.ic.bill.WhVO voWh = m_voBill.getWh();
		if (voWh == null) voWh = m_voBill.getWasteWh();

		// 直运仓库、直接调拨,不可编辑
		if (voWh != null && (voWh.getIsdirectstore() != null && voWh.getIsdirectstore().booleanValue()) || (m_voBill.getHeaderVO().getBdirecttranflag() != null && m_voBill.getHeaderVO().getBdirecttranflag().booleanValue())) {
			// biCol.setEnabled(false);
			return false;
		}

		// 来源单据控制：
		String csourcetype = (String) m_voBill.getItemValue(iRow, "csourcetype");

		// 是否配套的其他入、出
		boolean isDispend = false;

		String sthistype = m_sBillTypeCode;
		// 其他出，来源是采购入
		if ((BillTypeConst.m_otherOut.equals(sthistype) && csourcetype != null && csourcetype.equals(BillTypeConst.m_purchaseIn)) || (BillTypeConst.m_otherIn.equals(sthistype) && csourcetype != null && csourcetype.equals(BillTypeConst.m_saleOut))) isDispend = true;

		// 来源于库存特殊单
		boolean isFromICSp = false;

		if (csourcetype != null && (csourcetype.equals(BillTypeConst.m_assembly) || csourcetype.equals(BillTypeConst.m_disassembly) || csourcetype.equals(BillTypeConst.m_transform) || csourcetype.equals(BillTypeConst.m_check) || isDispend)) isFromICSp = true;

		// 是否在途
		UFBoolean bonroadflag = (UFBoolean) m_voBill.getItemValue(iRow, "bonroadflag");
		if (bonroadflag == null) bonroadflag = new UFBoolean(false);

		// 存货列
		if (sItemKey.equals("cinventorycode")) {
			((nc.ui.pub.beans.UIRefPane) biCol.getComponent()).getUITextField().setEditable(true);
			// 过滤存货
			nc.ui.pub.bill.BillItem biBody = getBillCardPanel().getHeadItem(m_sMainCalbodyItemKey);
			nc.ui.pub.bill.BillItem biWh = getBillCardPanel().getHeadItem(m_sMainWhItemKey);
			StringBuffer swherebody = new StringBuffer();
			if (biCol != null && biBody != null) {
				String ccalbodyid = (String) biBody.getValueObject();
				if (ccalbodyid != null) {
					swherebody.append(" pk_invmandoc in (select pk_invmandoc from bd_produce where pk_calbody='" + ccalbodyid + "' and isused='Y')");
				}
			}
			StringBuffer swherewh = new StringBuffer();
			if (biCol != null && biWh != null) {
				String cwhid = (String) biWh.getValueObject();
				if (cwhid != null) {
					swherewh.append(" pk_invmandoc in (select cinventoryid from ic_numctl where cwarehouseid='" + cwhid + "' )");
				}
			}

			if (m_isWhInvRef && swherewh.length() > 0) {
				RefFilter.filtInv(biCol, m_sCorpID, new String[] {
					swherewh.toString()
				});
			} else if (swherebody.length() > 0) {
				RefFilter.filtInv(biCol, m_sCorpID, new String[] {
					swherebody.toString()
				});

			} else {
				RefFilter.filtInv(biCol, m_sCorpID, null);
			}

			if (csourcetype != null && (csourcetype.equals("23") || csourcetype.equals("21"))) {
				// v5:如果本行上游来源不是是赠品, 且本行是赠品, 则可以修改存货
				boolean isbsourcelargess = m_voBill.getItemVOs()[iRow].getBsourcelargess().booleanValue();
				UFBoolean bLargess = m_voBill.getItemVOs()[iRow].getFlargess();
				boolean isblargess = bLargess == null ? false : bLargess.booleanValue();

				if (isblargess && !isbsourcelargess) { return true; }

				return false;
			}
			if (isFromICSp) {
				// biCol.setEnabled(true);
				return false;

			} else if (csourcetype != null) {
				// csourcetype != null
				// && (!csourcetype.startsWith("4") || csourcetype
				// .equals(BillTypeConst.m_transfer))) {
				// 直接根据当前存货编码过滤，避免当单据修改时无法限制时是当前的存货
				// 修改 by hanwei 2003-11-09
				String sPk_invman = (String) m_voBill.getItemValue(iRow, "cinventoryid");
				// 过滤替换件
				RefFilter.filtReplaceInv(biCol, m_sCorpID, new String[] {
					sPk_invman
				});
				((nc.ui.pub.beans.UIRefPane) biCol.getComponent()).getUITextField().setEditable(false);
			}

		}
		// 非存货列，必须先输入存货
		else {

			// 存货编码
			Object oTempInvCode = getBillCardPanel().getBodyValueAt(iRow, "cinventorycode");
			// 存货名
			// Object oTempInvName = getBillCardPanel().getBodyValueAt(iRow,
			// "invname");
			// 如果本行未输入存货或清空存货则本行所有列不可编辑。
			if (oTempInvCode == null || oTempInvCode.toString().trim().length() == 0) {
				// biCol.setEnabled(false);
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000026")/* @res "请先输入存货!" */);
				return false;
			}
		}

		InvVO voInv = m_voBill.getItemInv(iRow);

		// 如果是辅计量管理的存货，控制先输入(应/实)辅数量:V31,不控制先输入辅助数量
		if (sItemKey.equals(m_sAstItemKey) || sItemKey.equals(m_sShouldAstItemKey) || sItemKey.equals(m_sNumItemKey) || sItemKey.equals(m_sShouldNumItemKey) || sItemKey.equals("ngrossastnum") || sItemKey.equals("ntareastnum")) {

			// if (isFromICSp)
			// isEditable = false;
			/*
			 * if ( voInv.getIsAstUOMmgt() != null &&
			 * voInv.getIsAstUOMmgt().intValue() == 1) { Object castunitid =
			 * getBillCardPanel().getBodyValueAt(iRow, "castunitid"); if
			 * (castunitid == null || castunitid.toString().trim().length() ==
			 * 0) { showHintMessage("请先输入辅计量!"); isEditable = false; } }
			 */
			// 应发数量,有来源
			if (csourcetype != null && (sItemKey.equals(m_sShouldAstItemKey) || sItemKey.equals(m_sShouldNumItemKey))) isEditable = false;
		}

		// 辅计量
		if (sItemKey.equals("castunitname") || sItemKey.equals(m_sShouldAstItemKey) || sItemKey.equals(m_sAstItemKey) || sItemKey.equals("hsl") || sItemKey.equals("ngrossastnum") || sItemKey.equals("ntareastnum")) {
			if (voInv.getIsAstUOMmgt() == null || voInv.getIsAstUOMmgt().intValue() != 1) {
				isEditable = false;
			}
			// 过滤辅单位
			else {
				if (sItemKey.equals("castunitname")) filterMeas(iRow);
				// 固定换算率不可编辑
				else if (sItemKey.equals("hsl") && m_voBill.getItemVOs()[iRow].getIsSolidConvRate() != null && m_voBill.getItemVOs()[iRow].getIsSolidConvRate().intValue() == 1) {
					isEditable = false;
				}
				// 如果存在对应入库单信息，则自由项目信息不能修改
				if (getBillCardPanel().getBodyItem(m_sCorBillCodeItemKey) != null && getBillCardPanel().getBodyValueAt(row, m_sCorBillCodeItemKey) != null && getBillCardPanel().getBodyValueAt(row, m_sCorBillCodeItemKey).toString().trim().length() > 0) {
					if (sItemKey.equals("castunitname") || (sItemKey.equals("hsl") && voInv.getIsStoreByConvert().intValue() == 1)) isEditable = false;
				}

			}

		}
		// 自由项
		else if (sItemKey.equals("vfree0")) {
			if (voInv.getIsFreeItemMgt() == null || voInv.getIsFreeItemMgt().intValue() != 1) {
				isEditable = false;
			}
			// 设置自由项参数
			else {
				// 向自由项参照传入数据
				getFreeItemRefPane().setFreeItemParam(voInv);
				// 如果存在对应入库单信息，则自由项目信息不能修改
				if (getBillCardPanel().getBodyItem(m_sCorBillCodeItemKey) != null && getBillCardPanel().getBodyValueAt(row, m_sCorBillCodeItemKey) != null && getBillCardPanel().getBodyValueAt(row, m_sCorBillCodeItemKey).toString().trim().length() > 0) {
					isEditable = false;
				} else isEditable = true;

			}
		}

		// 过滤货位参照
		else if (sItemKey.equals("vspacename")) {

			if (voWh != null && voWh.getIsLocatorMgt() != null && voWh.getIsLocatorMgt().intValue() == 1) {
				filterSpace(iRow);

			} else {
				isEditable = false;
			}
			// // 如果存在对应入库单信息，则自由项目信息不能修改
			// if (getBillCardPanel().getBodyItem(m_sCorBillCodeItemKey) != null
			// && getBillCardPanel().getBodyValueAt(row,
			// m_sCorBillCodeItemKey) != null
			// && getBillCardPanel().getBodyValueAt(row,
			// m_sCorBillCodeItemKey).toString().trim().length() > 0) {
			// isEditable = false;
			// }

		}
		// 批次
		else if (sItemKey.equals("vbatchcode")) {
			if (voInv.getIsLotMgt() != null && voInv.getIsLotMgt().intValue() == 1) {
				String ColName = biCol.getName();
				// 入库单、期初单据
				// zhy2006-04-16由于新增批次号档案,故此处不对期初单据特殊处理,注释以下各行
				// if (m_bIsInitBill) {
				// // ||(m_voBill.getItemVOs()[iRow].getInOutFlag()!=
				// // InOutFlag.OUT
				// // &&m_voBill.getBillInOutFlag()!=InOutFlag.OUT))
				// // {
				// /** 得到原来界面上的批次号 */
				// String sBatchCode = (String) getBillCardPanel()
				// .getBodyValueAt(iRow, "vbatchcode");
				// /** 以TextField 的CellEditor覆盖原参照的CellEditor */
				// nc.ui.pub.beans.UITextField tfLot = new
				// nc.ui.pub.beans.UITextField(
				// sBatchCode);
				// tfLot.setMaxLength(biCol.getLength());
				// getBillCardPanel().getBodyPanel().getTable().getColumn(
				// ColName).setCellEditor(
				// new nc.ui.pub.bill.BillCellEditor(tfLot));

				// } else {
				// reset length !!! 06-26
				// forHF,临时用。请客户化帮助。由于同一批次辅计量不同而造成afterEdit触发不了。
				if (oBatchcode != null) getBillCardPanel().setBodyValueAt(oBatchcode.toString().trim() + " ", iRow, "vbatchcode");
				getLotNumbRefPane().setMaxLength(biCol.getLength());
				getBillCardPanel().getBodyPanel().getTable().getColumn(ColName).setCellEditor(new nc.ui.pub.bill.BillCellEditor(getLotNumbRefPane()));
				getLotNumbRefPane().setParameter(voWh, voInv);
				if (BillTypeConst.m_saleOut.equals(getBillTypeCode()) || BillTypeConst.m_allocationOut.equals(getBillTypeCode())) {
					getLotNumbRefPane().setStrNowSrcBid((String) getBillCardPanel().getBodyValueAt(iRow, "cfirstbillbid"));
				} else {
					getLotNumbRefPane().setStrNowSrcBid((String) getBillCardPanel().getBodyValueAt(iRow, "csourcebillbid"));
				}
				// }

			} else {
				isEditable = false;
			}
			// 如果存在对应入库单信息，则自由项目信息不能修改
			if (getBillCardPanel().getBodyItem(m_sCorBillCodeItemKey) != null && getBillCardPanel().getBodyValueAt(row, m_sCorBillCodeItemKey) != null && getBillCardPanel().getBodyValueAt(row, m_sCorBillCodeItemKey).toString().trim().length() > 0) {
				isEditable = false;
			}

		}

		// 出库跟踪入库
		else if (sItemKey.equals(m_sCorBillCodeItemKey)) {
			// 下列情况下出库跟踪入库不可以编辑：
			// 1:入库单据不是退货，2：出库单据是退货；3：入库单据数量为正；4：出库单据数量为负
			UFBoolean isReplenish = m_voBill.getHeaderVO().getFreplenishflag();
			int iReplenish = 0;
			int iBillInoutFlag = m_voBill.getBillTypeInt();
			GeneralBillItemVO voItem = null;
			int iRowInoutFlag = 0;
			if (m_voBill.getItemVOs() != null && m_voBill.getItemVOs().length > 0) {
				voItem = m_voBill.getItemVOs()[iRow];
				iRowInoutFlag = voItem.getInOutFlag();
			}
			if (isReplenish.booleanValue()) iReplenish = -1;
			else iReplenish = 1;
			iBillInoutFlag = iBillInoutFlag * iReplenish;
			if (iBillInoutFlag == InOutFlag.IN && iRowInoutFlag == InOutFlag.IN) {
				isEditable = false;
			} else {
				// 可以编辑
				String ColName = biCol.getName();
				getBillCardPanel().getBodyPanel().getTable().getColumn(ColName).setCellEditor(new nc.ui.pub.bill.BillCellEditor(getICCorBillRef()));
				ArrayList alparams = new ArrayList();
				alparams.add(m_voBill.getHeaderValue("cgeneralhid"));
				// getICCorBillRef().setParams(voWh, voInv, alparams);
				// getICCorBillRef().setParam(voWh,
				// m_voBill.getItemVOs()[iRow]);
			}

		} else if (sItemKey.equals("dvalidate") || sItemKey.equals("scrq")) {

			if (voInv.getIsValidateMgt() != null && voInv.getIsValidateMgt().intValue() == 1) {
				/*
				 * //非期初单据并且不是入库,不能编辑 if (!m_bIsInitBill &&
				 * m_voBill.getItemVOs()[iRow].getInOutFlag() != InOutFlag.IN) {
				 * isEditable = false; }
				 */
				isEditable = true;
			} else isEditable = false;

			// zhy如果输入的批次号在批次号档案中存在,则生产日期和实效日期不允许编辑
			if (isEditable) {
				String vbatchcode = (String) getBillCardPanel().getBodyValueAt(iRow, "vbatchcode");
				if (vbatchcode != null && isExistInBatch(voInv.getCinventoryid(), vbatchcode)) isEditable = false;
			}
		}
		// 项目
		else if (sItemKey.equals("cprojectphasename") && (m_iMode == BillMode.New || m_iMode == BillMode.Update)) {
			String spk = (String) m_voBill.getItemValue(iRow, "cprojectphaseid");
			String sName = (String) m_voBill.getItemValue(iRow, "cprojectphasename");
			m_refJobPhase.setPK(spk);
			m_refJobPhase.setName(sName);
			String cprojectid = (String) getBillCardPanel().getBodyValueAt(iRow, "cprojectid");
			if (cprojectid != null) {
				m_refJobPhaseModel.setJobID(cprojectid);

			} else {
				isEditable = false;
			}

		}
		// 成本对象
		else if (sItemKey.equals(m_sCostObjectItemKey)) {
			// 委外发料不需要加工品为成本对象
			if (!m_sBillTypeCode.equals("4F")) {
				filterCostObject();
			}
			// get id if null then set refpk = null
			String costid = (String) getBillCardPanel().getBodyValueAt(row, "ccostobject");
			if (costid == null) ((UIRefPane) getBillCardPanel().getBodyItem(m_sCostObjectItemKey).getComponent()).setPK(null);
			else ((UIRefPane) getBillCardPanel().getBodyItem(m_sCostObjectItemKey).getComponent()).setPK(costid);
		}
		// 在途标记
		else if (sItemKey.equals("bonroadflag")) {

			if (m_voBill.getItemValue(iRow, "ncorrespondnum") != null) {

				UFDouble ncornum = new UFDouble((m_voBill.getItemValue(iRow, "ncorrespondnum").toString()));

				if (m_voBill.getItemValue(iRow, m_sNumItemKey) != null && ncornum != null) {
					if (m_iBillInOutFlag == InOutFlag.OUT) {
						ncornum = ncornum.multiply(-1);

					}

					if (((UFDouble) m_voBill.getItemValue(iRow, m_sNumItemKey)).sub(ncornum).doubleValue() == 0) {
						isEditable = false;
					}
				}
			}

		}
		// 赠品flargess
		else if (sItemKey.equals(IItemKey.FLARGESS)) {
			// 自制可以编辑，非自制不可编辑。
			// 对于非自制的情况:来源是采购订单，并且bsourcelargess为否的可以编辑，为是不可编辑
			if (csourcetype == null) isEditable = true;
			else if ((sthistype.equals("45") && !(isBrwLendBiztype())) || sthistype.equals("47")) {

				// 修改人：刘家清 修改日期：2007-05-21
				// 修改原因：根据需求，应该判断bsourcelargess，而不是flargess
				/*
				 * UFBoolean bLargess =
				 * m_voBill.getItemVOs()[iRow].getFlargess(); boolean isblargess
				 * = bLargess==null?false:bLargess.booleanValue();
				 * 
				 * 
				 * 
				 * 
				 * 
				 * if(isblargess||(m_voBill.getHeaderVO().getFreplenishflag()!=null
				 * &&m_voBill.getHeaderVO().getFreplenishflag().booleanValue()))
				 */
				UFBoolean bsourcelargess = m_voBill.getItemVOs()[iRow].getBsourcelargess();
				boolean isbsourcelargess = bsourcelargess == null ? false : bsourcelargess.booleanValue();

				if (isbsourcelargess || (m_voBill.getHeaderVO().getFreplenishflag() != null && m_voBill.getHeaderVO().getFreplenishflag().booleanValue())) isEditable = false;
				else isEditable = true;
				// // 2004-12-13 ydy 如果上游是赠品，则不可编辑赠品标志
				// boolean isbsourcelargess = m_voBill.getItemVOs()[iRow]
				// .getBsourcelargess().booleanValue();
				// if (isbsourcelargess)
				// isEditable = false;
				// else
				// isEditable = true;

			} else isEditable = false;

			// 特殊规则：csourcetype.startsWith("23"):来源单据为采购到货单的赠品不可以编辑为
			// if (csourcetype != null && csourcetype.equalsIgnoreCase("23"))
			// isEditable = false;

		} else if (sItemKey.endsWith("prcie") || sItemKey.endsWith("mny")) {
			if (m_voBill.getItemValue(iRow, "flargess") != null && ((UFBoolean) m_voBill.getItemValue(iRow, "flargess")).booleanValue()) isEditable = false;

		} else if (sItemKey.equals("cmeaswarename")) {
			// 过滤存货
			nc.ui.pub.bill.BillItem biBody = getBillCardPanel().getHeadItem(m_sMainCalbodyItemKey);
			if (biBody == null) return true;

			String pk_calbody = biBody.getValue();
			BillItem bi2 = getBillCardPanel().getBodyItem("cmeaswarename");
			String[] where = new String[1];
			if (pk_calbody != null) {
				if (bi2 != null && bi2.getComponent() != null && ((UIRefPane) bi2.getComponent()).getRefModel().getClass().getName().equals("nc.ui.mm.pub.pub1010.JlcRefModel")) {
					where[0] = " and mm_jldoc.gcbm='" + pk_calbody + "'";
					RefFilter.filtMeasware(bi2, m_sCorpID, where);
				}
			}
		}

		// 出库参照入库时候表体供应商不能编辑
		else if (sItemKey.equals("vvendorname")) {
			if (getBillCardPanel().getBodyItem(m_sCorBillCodeItemKey) != null && getBillCardPanel().getBodyValueAt(row, m_sCorBillCodeItemKey) != null && getBillCardPanel().getBodyValueAt(row, m_sCorBillCodeItemKey).toString().trim().length() > 0) {
				isEditable = false;
			}
		} else if (sItemKey.startsWith("vuserdef")) {
			nc.ui.pub.bill.BillItem item = getBillCardPanel().getBodyItem(sItemKey);
			if (item != null && item.getDataType() == nc.ui.pub.bill.BillItem.USERDEF) {
				String pk = null;
				pk = (String) m_voBill.getItemValue(iRow, "pk_defdoc" + sItemKey.substring("vuserdef".length()));
				if (pk != null && pk.length() > 0 && getBillCardPanel().getBodyValueAt(iRow, sItemKey) != null)
				;
				((UIRefPane) item.getComponent()).setPK(pk);
			} else if (getBillCardPanel().getBodyValueAt(iRow, sItemKey) == null) {
				((UIRefPane) item.getComponent()).setPK(null);
			}
		}

		return isEditable;

	}

	/**
	 * beforeEdit 方法注解。[处理表头编辑前事件]
	 */
	public boolean beforeEdit(nc.ui.pub.bill.BillItemEvent e) {
		getBillCardPanel().stopEditing();
		String sItemKey = e.getItem().getKey();
		BillItem bi = e.getItem();
		if (m_iMode == BillMode.Browse) return false;
		if (!bi.isEdit()) { return false; }
		if (!bi.isEnabled()) return false;

		// 仓库编辑前需要按照库存组织过滤
		if (sItemKey.equals(m_sMainWhItemKey)) {
			String sCalID = getBillCardPanel().getHeadItem("pk_calbody") == null ? null : (String) getBillCardPanel().getHeadItem("pk_calbody").getValueObject();
			if (sCalID != null) {
				String sConstraint = " AND isdirectstore = 'N' AND pk_calbody='" + sCalID + "'";
				RefFilter.filtWh(getBillCardPanel().getHeadItem(m_sMainWhItemKey), m_sCorpID, new String[] {
					sConstraint
				});
			}
		}

		// if (sItemKey.equals("pk_measware")) {
		// // 过滤计量器具
		// nc.ui.pub.bill.BillItem bi2 = getBillCardPanel().getHeadItem(
		// "pk_measware");
		// UIRefPane refCalbody = (UIRefPane) getBillCardPanel().getHeadItem(
		// "pk_calbody").getComponent();
		// String pk_calbody = refCalbody.getRefPK();
		// String[] s = new String[1];
		// s[0] = " and mm_jldoc.gcbm='" + pk_calbody + "'";
		// RefFilter.filtMeasware(bi2, m_sCorpID, s);
		// }
		return true;

	}

	/**
	 * 创建者：王乃军 功能：单据编辑事件处理 参数：e单据编辑事件 返回： 例外： 日期：(2001-5-8 19:08:05)
	 * 修改日期，修改人，修改原因，注释标志：
	 */
	public boolean beforeEdit(nc.ui.pub.bill.BillEditEvent e) {

		if (e.getKey().equals(m_sCorBillCodeItemKey) && isCellEditable(true, e.getRow(), e.getKey())) {

			nc.vo.scm.ic.bill.WhVO voWh = m_voBill.getWh();
			if (voWh == null) voWh = m_voBill.getWasteWh();

			getICCorBillRef().setParam(voWh, m_voBill.getItemVOs()[e.getRow()]);
		}

		// nc.vo.scm.pub.ctrl.GenMsgCtrl.printHint("beforeEdit:" + e.getKey());
		/*
		 * getBillCardPanel().stopEditing(); boolean isEditable = true; String
		 * sItemKey = e.getKey(); nc.ui.pub.bill.BillItem biCol =
		 * getBillCardPanel().getBodyItem(sItemKey); int iRow = e.getRow(); int
		 * iPos = e.getPos();
		 * 
		 * if (sItemKey == null || biCol == null) return false;
		 * 
		 * //模版设置 if (!biCol.isEdit()) {
		 * 
		 * return false; }
		 * 
		 * if (m_voBill == null) { biCol.setEnabled(false); return false; }
		 * nc.vo.scm.ic.bill.WhVO voWh = m_voBill.getWh(); if (voWh == null)
		 * voWh = m_voBill.getWasteWh();
		 * 
		 * //直运仓库、直接调拨,不可编辑 if (voWh!=null&&(voWh.getIsdirectstore() != null &&
		 * voWh.getIsdirectstore().booleanValue()) ||
		 * (m_voBill.getHeaderVO().getBdirecttranflag() != null &&
		 * m_voBill.getHeaderVO().getBdirecttranflag().booleanValue())) {
		 * biCol.setEnabled(false); return false; }
		 * 
		 * //来源单据控制： String csourcetype = (String) m_voBill.getItemValue(iRow,
		 * "csourcetype"); //是否配套的其他入、出 boolean isDispend=false;
		 * 
		 * String sthistype=m_voBill.getBillTypeCode(); //其他出，来源是采购入
		 * if((BillTypeConst
		 * .m_otherOut.equals(sthistype)&&csourcetype!=null&&csourcetype
		 * .equals(BillTypeConst.m_purchaseIn))
		 * ||(BillTypeConst.m_otherIn.equals
		 * (sthistype)&&csourcetype!=null&&csourcetype
		 * .equals(BillTypeConst.m_saleOut)) ) isDispend=true;
		 * 
		 * //来源于库存特殊单 boolean isFromICSp = false;
		 * 
		 * if (csourcetype != null &&
		 * (csourcetype.equals(BillTypeConst.m_assembly) ||
		 * csourcetype.equals(BillTypeConst.m_disassembly) ||
		 * csourcetype.equals(BillTypeConst.m_transform) ||
		 * csourcetype.equals(BillTypeConst.m_check)
		 * 
		 * ||isDispend ) ) isFromICSp = true;
		 * 
		 * 
		 * //存货列 if (sItemKey.equals("cinventorycode")) { //过滤存货
		 * nc.ui.pub.bill.BillItem biBody =
		 * getBillCardPanel().getHeadItem(m_sMainCalbodyItemKey);
		 * nc.ui.pub.bill.BillItem biWh =
		 * getBillCardPanel().getHeadItem(m_sMainWhItemKey); StringBuffer
		 * swherebody=new StringBuffer(); if (biCol != null && biBody != null) {
		 * String ccalbodyid = biBody.getValue(); if (ccalbodyid != null) {
		 * swherebody.append(" pk_invmandoc in (select pk_invmandoc from
		 * bd_produce where pk_calbody='" + ccalbodyid + "' and isused='Y')"); }
		 * } StringBuffer swherewh=new StringBuffer(); if (biCol != null && biWh
		 * != null) { String cwhid = biWh.getValue(); if (cwhid != null) {
		 * swherewh.append(" pk_invmandoc in (select cinventoryid from ic_numctl
		 * where cwarehouseid='" + cwhid + "' )"); } }
		 * 
		 * if(m_isWhInvRef&&swherewh.length()>0){ RefFilter.filtInv( biCol,
		 * m_sCorpID, new String[] {swherewh.toString() }); }else
		 * if(swherebody.length()>0){ RefFilter.filtInv( biCol, m_sCorpID, new
		 * String[] {swherebody.toString() });
		 * 
		 * }else{ RefFilter.filtInv( biCol, m_sCorpID,null); }
		 * 
		 * 
		 * 
		 * 
		 * 
		 * if (csourcetype != null && !csourcetype.startsWith("4")) {
		 * //直接根据当前存货编码过滤，避免当单据修改时无法限制时是当前的存货 // 修改 by hanwei 2003-11-09 String
		 * sPk_invman = (String) m_voBill.getItemValue(iRow, "cinventoryid");
		 * //过滤替换件 RefFilter.filtReplaceInv(biCol, m_sCorpID, new String[] {
		 * sPk_invman }); // ((nc.ui.pub.beans.UIRefPane)
		 * biCol.getComponent()).getUITextField().setEditable(false); } else if
		 * (isFromICSp) { biCol.setEnabled(true); return false; } }
		 * //非存货列，必须先输入存货 else {
		 * 
		 * //存货编码 Object oTempInvCode = getBillCardPanel().getBodyValueAt(iRow,
		 * "cinventorycode"); //存货名 Object oTempInvName =
		 * getBillCardPanel().getBodyValueAt(iRow, "invname");
		 * //如果本行未输入存货或清空存货则本行所有列不可编辑。 if (oTempInvCode == null ||
		 * oTempInvCode.toString().trim().length() == 0) {
		 * biCol.setEnabled(false); showHintMessage("请先输入存货!"); return false; }
		 * }
		 * 
		 * InvVO voInv = m_voBill.getItemInv(iRow);
		 * 
		 * //如果是辅计量管理的存货，控制先输入(应/实)辅数量 if (sItemKey.equals(m_sAstItemKey) ||
		 * sItemKey.equals(m_sShouldAstItemKey) ||
		 * sItemKey.equals(m_sNumItemKey) ||
		 * sItemKey.equals(m_sShouldNumItemKey)) {
		 * 
		 * if (isFromICSp) isEditable = false;
		 * 
		 * else if ( voInv.getIsAstUOMmgt() != null &&
		 * voInv.getIsAstUOMmgt().intValue() == 1) { Object castunitid =
		 * getBillCardPanel().getBodyValueAt(iRow, "castunitid"); if (castunitid
		 * == null || castunitid.toString().trim().length() == 0) {
		 * showHintMessage("请先输入辅计量!"); isEditable = false; } } //应发数量,有来源 //if
		 * (csourcetype != null //&& (sItemKey.equals(m_sShouldAstItemKey) ||
		 * sItemKey.equals(m_sShouldNumItemKey))) //isEditable = false; }
		 * 
		 * //辅计量 if (sItemKey.equals("castunitname") ||
		 * sItemKey.equals(m_sShouldAstItemKey) ||
		 * sItemKey.equals(m_sAstItemKey) || sItemKey.equals("hsl")) { if
		 * (isFromICSp || voInv.getIsAstUOMmgt() == null ||
		 * voInv.getIsAstUOMmgt().intValue() != 1) { isEditable = false; }
		 * //过滤辅单位 else { if (sItemKey.equals("castunitname")) filterMeas(iRow);
		 * //固定换算率不可编辑 else if ( sItemKey.equals("hsl") &&
		 * m_voBill.getItemValue(iRow, "isSolidConvRate") != null && ((Integer)
		 * m_voBill.getItemValue(iRow, "isSolidConvRate")).intValue() == 1) {
		 * isEditable = false; } } } //自由项 else if (sItemKey.equals("vfree0")) {
		 * if (isFromICSp || voInv.getIsFreeItemMgt() == null ||
		 * voInv.getIsFreeItemMgt().intValue() != 1) { isEditable = false; }
		 * //设置自由项参数 else { //向自由项参照传入数据
		 * getFreeItemRefPane().setFreeItemParam(voInv); } }
		 * 
		 * //过滤货位参照 else if (sItemKey.equals("vspacename")) {
		 * 
		 * if (voWh != null && voWh.getIsLocatorMgt() != null &&
		 * voWh.getIsLocatorMgt().intValue() == 1) filterSpace(iRow); else {
		 * isEditable = false; } } //批次 else if (sItemKey.equals("vbatchcode"))
		 * { if (voInv.getIsLotMgt() != null && voInv.getIsLotMgt().intValue()
		 * == 1) { String ColName = biCol.getName(); //入库单、期初单据 if
		 * (m_bIsInitBill){ //||(m_voBill.getItemVOs()[iRow].getInOutFlag()!=
		 * InOutFlag.OUT // &&m_voBill.getBillInOutFlag()!=InOutFlag.OUT)) {
		 * 
		 * String sBatchCode = (String) getBillCardPanel().getBodyValueAt(iRow,
		 * "vbatchcode");
		 * 
		 * nc.ui.pub.beans.UITextField tfLot = new
		 * nc.ui.pub.beans.UITextField(sBatchCode);
		 * tfLot.setMaxLength(biCol.getLength());
		 * getBillCardPanel().getBodyPanel
		 * ().getTable().getColumn(ColName).setCellEditor( new
		 * nc.ui.pub.bill.BillCellEditor(tfLot)); } else { //reset length !!!
		 * 06-26 getLotNumbRefPane().setMaxLength(biCol.getLength());
		 * getBillCardPanel
		 * ().getBodyPanel().getTable().getColumn(ColName).setCellEditor( new
		 * nc.ui.pub.bill.BillCellEditor(getLotNumbRefPane()));
		 * getLotNumbRefPane().setParameter(voWh, voInv); } } else { isEditable
		 * = false; } }
		 * 
		 * //出库跟踪入库 else if (sItemKey.equals(m_sCorBillCodeItemKey)) { //可以编辑
		 * String ColName = biCol.getName();
		 * getBillCardPanel().getBodyPanel().getTable
		 * ().getColumn(ColName).setCellEditor( new
		 * nc.ui.pub.bill.BillCellEditor(getICCorBillRef())); ArrayList alparams
		 * = new ArrayList();
		 * alparams.add(m_voBill.getHeaderValue("cgeneralhid"));
		 * //getICCorBillRef().setParams(voWh, voInv, alparams);
		 * getICCorBillRef().setParam(voWh,m_voBill.getItemVOs()[iRow]); }
		 * 
		 * else if (sItemKey.equals("dvalidate") || sItemKey.equals("scrq")) {
		 * 
		 * if (voInv.getIsValidateMgt() != null &&
		 * voInv.getIsValidateMgt().intValue() == 1) { //非期初单据并且不是入库,不能编辑 if
		 * (!m_bIsInitBill && m_voBill.getItemVOs()[iRow].getInOutFlag() !=
		 * InOutFlag.IN) { isEditable = false; } } else isEditable = false; }
		 * //项目 else if ( sItemKey.equals("cprojectphasename") && (m_iMode ==
		 * BillMode.New || m_iMode == BillMode.Update)) { String spk = (String)
		 * m_voBill.getItemValue(iRow, "cprojectphaseid"); String sName =
		 * (String) m_voBill.getItemValue(iRow, "cprojectphasename");
		 * m_refJobPhase.setPK(spk); m_refJobPhase.setName(sName); String
		 * cprojectid = (String) getBillCardPanel().getBodyValueAt(iRow,
		 * "cprojectid"); if (cprojectid != null) {
		 * m_refJobPhaseModel.setJobID(cprojectid); } else { isEditable = false;
		 * } } //成本对象 else if (sItemKey.equals(m_sCostObjectItemKey)) {
		 * filterCostObject(); }
		 * 
		 * //赠品flargess else if (sItemKey.equals("flargess")) {
		 * //自制可以编辑，非自制不可编辑。 //对于非自制的情况:来源是采购订单，并且bsourcelargess为否的可以编辑，为是不可编辑
		 * if(csourcetype==null) isEditable=true; else
		 * if(sthistype.equals("45")){ //2004-12-13 ydy 如果上游是赠品，则不可编辑赠品标志
		 * boolean isbsourcelargess =
		 * m_voBill.getItemVOs()[iRow].getBsourcelargess().booleanValue();
		 * if(isbsourcelargess) isEditable=false; else isEditable=true; }else
		 * isEditable=false;
		 * 
		 * 
		 * //特殊规则：csourcetype.startsWith("23"):来源单据为采购到货单的赠品不可以编辑为 if
		 * (csourcetype != null && csourcetype.equalsIgnoreCase("23"))
		 * isEditable = false; } else if
		 * (sItemKey.endsWith("prcie")||sItemKey.endsWith("mny")) {
		 * if(m_voBill.getItemValue
		 * (iRow,"flargess")!=null&&((UFBoolean)m_voBill.
		 * getItemValue(iRow,"flargess")).booleanValue()) isEditable=false; } //
		 * biCol.setEnabled(isEditable);
		 */
		return true;

	}

	/**
	 * 计算合计。 创建日期：(2001-10-24 16:33:58)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 * @param sItemKey
	 *            java.lang.String
	 */
	public nc.vo.pub.lang.UFDouble calcurateTotal(java.lang.String sItemKey) {
		UFDouble dTotal = new UFDouble(0.0);

		for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
			Object oValue = getBillCardPanel().getBodyValueAt(i, sItemKey);
			String sValue = (oValue == null || oValue.equals("")) ? "0" : oValue.toString();
			dTotal = dTotal.add(new UFDouble(sValue));
		}

		return dTotal;
	}

	/**
	 * 根据换算率计算毛重和批重。 创建日期：(2005-04-05 16:33:58)
	 * 
	 * @return void
	 * @param hsl
	 *            UFDouble
	 */
	public void calOtherNumByHsl(String sMainNum, String sAstNum, int rownum, UFDouble hsl) {
		if (hsl == null) return;

		// 以下是皮重毛重
		UFDouble nMain = null;
		UFDouble nAst = null;

		Object oMain = getBillCardPanel().getBodyValueAt(rownum, sMainNum);
		Object oAst = getBillCardPanel().getBodyValueAt(rownum, sAstNum);

		if (oMain != null) nMain = new UFDouble(oMain.toString().trim());
		if (oAst != null) nAst = new UFDouble(oAst.toString().trim());

		/* 辅计量：换算率；无论固定，变动换算率，按辅数量＊换算率来重新计算主数量 */
		if (m_bAstCalMain) {
			if (nAst != null) {
				nMain = nAst.multiply(hsl);
				getBillCardPanel().setBodyValueAt(nMain, rownum, sMainNum);
				m_voBill.setItemValue(rownum, sMainNum, nMain);

			} else {
				getBillCardPanel().setBodyValueAt(null, rownum, sMainNum);
				m_voBill.setItemValue(rownum, sMainNum, null);
			}
		} else {
			if (nMain != null) {
				nAst = nMain.div(hsl);
				getBillCardPanel().setBodyValueAt(nAst, rownum, sAstNum);
				m_voBill.setItemValue(rownum, sAstNum, nAst);
			} else {
				getBillCardPanel().setBodyValueAt(null, rownum, sAstNum);
				m_voBill.setItemValue(rownum, sAstNum, null);
			}
		}

	}

	/**
	 * 创建者：王乃军 功能：抽象方法：保存前的VO检查 参数：待保存单据 返回： 例外： 日期：(2001-5-24 下午 5:17)
	 * 修改日期，修改人，修改原因，注释标志：
	 */
	abstract protected boolean checkVO(GeneralBillVO voBill);

	/**
	 * 创建者：王乃军 功能：清空对应单数据 参数： 返回： 例外： 日期：(2001-11-23 18:11:18)
	 * 修改日期，修改人，修改原因，注释标志：
	 */
	public void clearCorrBillInfo(int rownum) {
		// 清空对应单据
		// 对应单据号
		try {
			getBillCardPanel().setBodyValueAt(null, rownum, m_sCorBillCodeItemKey);
		} catch (Exception e3) {
		}
		try {
			// 对应单据类型
			getBillCardPanel().setBodyValueAt(null, rownum, "ccorrespondtype");
		} catch (Exception e4) {
		}
		try {
			// 对应单据表头OID
			// 单据模板库中表体位置两个不显示列ccorrespondhid,ccorrespondbid,以保存带出的对应表头，表体OID
			getBillCardPanel().setBodyValueAt(null, rownum, "ccorrespondhid");
		} catch (Exception e5) {
		}
		try {
			// 对应单据表体OID
			getBillCardPanel().setBodyValueAt(null, rownum, "ccorrespondbid");
		} catch (Exception e6) {
		}
		// 同步改变m_voBill
		m_voBill.setItemValue(rownum, "ccorrespondbid", null);
		m_voBill.setItemValue(rownum, "ccorrespondhid", null);
		m_voBill.setItemValue(rownum, m_sCorBillCodeItemKey, null);
		m_voBill.setItemValue(rownum, "ccorrespondtype", null);

	}

	/**
	 * 创建者：王乃军 功能：清除对应行货位、序列号数据 参数： 返回： 例外： 日期：(2001-5-9 9:23:32)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	protected void clearLocSnData(int row, String sItemKey) {
		// 因为仓库修改后，row==-1,所以这里不检查row合法性
		if (sItemKey == null || sItemKey.length() == 0) return;
		// 提示信息
		String sHintMsg = "";
		// 是否清货位，序列号
		boolean bClearLoc = false, bClearSn = false;

		if (sItemKey.equals("bonroadflag")) {
			bClearLoc = true;
			// bClearSn=true;
		}

		// 仓库
		if (sItemKey.equals(m_sMainWhItemKey)) {
			// 需要清空 ---------- 所有 ----------- 货位。!!!
			if (m_alLocatorData != null && m_alSerialData != null) {
				for (int q = 0; q < m_alLocatorData.size(); q++) {
					m_alLocatorData.set(q, null);
					// 序列号和货位相关，也应该全部清除
					m_alSerialData.set(q, null);
				}
			}
			if (m_voBill != null) {
				for (int q = 0; q < m_voBill.getItemVOs().length; q++) {
					m_voBill.getItemVOs()[q].setLocator(null);
					m_voBill.getItemVOs()[q].setSerial(null);
				}
			}

		} else {
			// 仓库是否货位管理---非货位管理时在仓库改变时清空。
			boolean bIsLocatorMgt = isLocatorMgt();
			// 当前行是否序列号管理---无条件去掉。
			// boolean bIsSNmgt = isSNmgt(row);

			// 以下的判断不用else语句
			// 存货
			if (sItemKey.equals("cinventorycode") || sItemKey.equals(m_sNumItemKey) || sItemKey.equals(m_sAstItemKey) || sItemKey.equals(m_sCorBillCodeItemKey) || sItemKey.equals("vvendorname") || sItemKey.equals("headprovider")) {
				// //非期初单时需要清货位。
				// if (!m_bIsInitBill)
				// bClearLoc = true;
				bClearSn = true;
			}
			// 自由项，批次
			if (!bClearSn && (sItemKey.equals("vfree0") || sItemKey.equals("vbatchcode"))) bClearSn = true;

			// 如果出库时，需要清序列号，就需要同时清货位
			// if(bClearSn&&m_voBill.getItemInv(row)!=null&&m_voBill.getItemInv(row).getInOutFlag()!=InOutFlag.IN)
			// bClearLoc=true;
			// //如果是货位管理并且需要清数据
			if (bIsLocatorMgt && bClearLoc) {
				if (m_alLocatorData != null

				&& row >= 0 && row < m_alLocatorData.size() && m_alLocatorData.get(row) != null) {
					m_alLocatorData.set(row, null);
					getBillCardPanel().setBodyValueAt(null, row, "vspacename");
					getBillCardPanel().setBodyValueAt(null, row, "cspaceid");
					sHintMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC000-0003830")/* @res "货位" */;
				}
				if (m_voBill != null && m_voBill.getItemVOs().length > row && m_voBill.getItemVOs()[row] != null) {

					m_voBill.getItemVOs()[row].setLocator(null);

				}

			}

			// ----------------- 如果是序列号管理并且需要清数据
			// bIsSNmgt
			// &&
			if (bClearSn) {
				if (m_alSerialData != null && row >= 0 && row < m_alSerialData.size()

				&& m_alSerialData.get(row) != null) {
					m_alSerialData.set(row, null);
					// 如果清了货位
					if (sHintMsg != null && sHintMsg.length() > 0) sHintMsg = sHintMsg + nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000311")/*
																																										* @res
																																										* "和"
																																										*/;
					sHintMsg = sHintMsg + nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC000-0001819")/* @res "序列号" */;
				}
				if (m_voBill != null && m_voBill.getItemVOs().length > row && m_voBill.getItemVOs()[row] != null) {

					m_voBill.getItemVOs()[row].setSerial(null);

				}

			}
		}
		// if (sHintMsg != null && sHintMsg.length() > 0)
		// showHintMessage(
		// "清空了第 " + (row + 1) + " 行的" + sHintMsg + "数据，请重新执行" + sHintMsg +
		// "分配。");

	}

	/**
	 * 创建者：王乃军 功能：清空列表和表单界面 参数： 返回： 例外： 日期：(2001-5-9 9:23:32)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	protected void clearUi() {
		try {
			// clear card panel()
			GeneralBillVO voNullBill = new GeneralBillVO();
			voNullBill.setParentVO(new GeneralBillHeaderVO());
			voNullBill.setChildrenVO(new GeneralBillItemVO[] {
				new GeneralBillItemVO()
			});
			getBillCardPanel().setBillValueVO(voNullBill);
			getBillCardPanel().getBillModel().clearBodyData();
			// clear list panel()
			getBillListPanel().getHeadBillModel().clearBodyData();
			getBillListPanel().getBodyBillModel().clearBodyData();

		} catch (Exception e) {

		}

	}

	/**
	 * 来源单据是转库单时的界面控制方法 功能： 参数： 返回： 例外： 日期：(2001-10-19 09:43:22)
	 * 修改日期，修改人，修改原因，注释标志：
	 */
	protected void ctrlSourceBillButtons() {
		ctrlSourceBillButtons(true);
	}

	/**
	 * 来源单据是转库单时的界面控制方法 功能： 参数： 返回： 例外： 日期：(2001-10-19 09:43:22)
	 * 修改日期，修改人，修改原因，注释标志：
	 */
	protected void ctrlSourceBillUI() {
		ctrlSourceBillUI(true);
	}

	/**
	 * 此处插入方法说明。 作者：余大英 创建日期：(2001-6-21 15:11:22)
	 * 
	 * @param int bill
	 */
	protected void dispSpace(int bill) {
		// 查询当前表单的表体货位
		// ydy
		if (m_alListData == null) return;

		if (getBillCardPanel().getBodyItem("vspacename") == null || !(getBillCardPanel().getBodyItem("vspacename").isShow())) {
			if (getBillListPanel().getBodyTable().getRowCount() > 0) getBillListPanel().getBodyTable().setRowSelectionInterval(0, 0);
			return;
		}

		GeneralBillVO voBill = (GeneralBillVO) m_alListData.get(bill);
		appendLocator(voBill);

		setListBodyData(voBill.getItemVOs());

		// 选中表体第一行
		// 表体不可能为空
		if (getBillListPanel().getBodyTable().getRowCount() > 0) getBillListPanel().getBodyTable().setRowSelectionInterval(0, 0);
		// end ydy

	}

	protected void appendLocator(GeneralBillVO voBill) {
		GeneralBillItemVO[] voItems = (GeneralBillItemVO[]) voBill.getChildrenVO();
		boolean isQueried = false;

		if (voItems != null) {
			SCMEnv.out("body rows=" + voItems.length);
			for (int i = 0; i < voItems.length; i++) {
				LocatorVO[] lvos = ((LocatorVO[]) voItems[i].getLocator());
				if (lvos == null && !isQueried) {
					qryLocSN(voBill, QryInfoConst.LOC);
					lvos = ((LocatorVO[]) voItems[i].getLocator());
					isQueried = true;
				}
				if (lvos != null && lvos.length == 1) {
					voItems[i].setCspaceid(lvos[0].getCspaceid());
					voItems[i].setVspacename(lvos[0].getVspacename());
				} else {
					voItems[i].setCspaceid(null);
					voItems[i].setVspacename(null);
				}
			}
		}
	}

	/**
	 * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2001-12-5 16:27:59) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @param row
	 *            int
	 * @param formulas
	 *            java.lang.String[]
	 */
	protected void execEditFomulas(int row, String itemKey) {
		/** 强制执行表体行，数量列的公式 */
		nc.ui.pub.bill.BillItem bi = getBillCardPanel().getBodyItem(itemKey);
		if (bi != null) {
			String[] formulas = bi.getEditFormulas();
			getBillCardPanel().execBodyFormulas(row, formulas);
		}
	}

	/**
	 * 类型说明：
	 * 
	 * 执行表头、表尾的公式们。
	 * 
	 * 创建日期：(2002-11-6 13:23:02) 作者： 修改日期： 修改人： 修改原因： 算法说明：
	 */
	public void execHeadTailFormulas() {

		getBillCardPanel().execHeadTailLoadFormulas();

	}

	/**
	 * 创建者：王乃军 功能：滤掉不需要的条件字段，然后返回之 参数： 返回： 例外： 日期：(2001-8-17 13:13:51)
	 * 修改日期，修改人，修改原因，注释标志：
	 */
	public void filterCondVO2(nc.vo.pub.query.ConditionVO[] voaCond, String[] saItemKey) {
		if (voaCond == null || saItemKey == null) return;
		// 暂存器
		int j = 0;
		// 数组长
		int len = saItemKey.length;
		for (int i = 0; i < voaCond.length; i++)
			// 找
			if (voaCond[i] != null) for (j = 0; j < len; j++) {
				if (saItemKey[j] != null && voaCond[i].getFieldCode() != null && saItemKey[j].trim().equals(voaCond[i].getFieldCode().trim())) {
					// 补上对括弧的保留
					voaCond[i].setFieldCode("1");
					voaCond[i].setOperaCode("=");
					voaCond[i].setDataType(1);
					voaCond[i].setValue("1");
				}
			}
	}

	/**
	 * 过滤单据参照 创建者：张欣 功能：过滤成本对象 参数： 返回： 例外： 日期：(2001-7-17 10:33:20)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 */
	public void filterCostObject() {
		try {
			// 根据库存组织过滤
			String sCalbodyID = null;
			if (getBillCardPanel().getHeadItem("pk_calbody") != null && getBillCardPanel().getBodyItem(m_sCostObjectItemKey) != null) {
				sCalbodyID = (String) getBillCardPanel().getHeadItem("pk_calbody").getValueObject();
				UIRefPane ref = (nc.ui.pub.beans.UIRefPane) getBillCardPanel().getBodyItem(m_sCostObjectItemKey).getComponent();
				ref.setWhereString(" bd_produce.pk_calbody='" + sCalbodyID + "' and bd_produce.pk_invmandoc = bd_invmandoc.pk_invmandoc and bd_produce.sfcbdx = 'Y' ");

			}
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
		}
	}

	/**
	 * 如果是固定货位的存货，过滤出存货的固定货位 作者：余大英 创建日期：(2001-7-6 16:53:38)
	 */
	private void filterSpace(int row) {
		if (m_voBill == null) return;
		nc.vo.scm.ic.bill.WhVO voWh = m_voBill.getWh();
		if (voWh == null || voWh.getIsLocatorMgt() == null || voWh.getIsLocatorMgt().intValue() == 0) {
			getBillCardPanel().getBillData().getBodyItem("vspacename").setEnabled(false);
			return;
		}
		getBillCardPanel().getBillData().getBodyItem("vspacename").setEnabled(true);

		String sName = (String) getBillCardPanel().getBodyValueAt(row, "vspacename");
		String spk = (String) getBillCardPanel().getBodyValueAt(row, "cspaceid");

		if (GenMethod.isNull(spk)) {
			getBillCardPanel().setBodyValueAt(null, row, "cspaceid");

		}

		getLocatorRefPane().setOldValue(sName, null, spk);

		nc.vo.scm.ic.bill.InvVO invvo = m_voBill.getItemInv(row);
		String selastunitid = (String) m_voBill.getItemValue(row, "cselastunitid");
		invvo.setCselastunitid(selastunitid);

		getLocatorRefPane().setParam(voWh, invvo);
		return;

	}

	/**
	 * 创建者：王乃军 功能：签字成功后处理 参数： 返回： 例外： 日期：(2001-5-8 19:08:05)
	 * 
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 */
	public void freshAfterSignedOK(String sBillStatus) {
		try {
			GeneralBillVO voBill = null;
			// 刷新列表形式
			if (m_iLastSelListHeadRow >= 0 && m_alListData != null && m_alListData.size() > m_iLastSelListHeadRow && m_alListData.get(m_iLastSelListHeadRow) != null) {
				// 这里不能clone(),改变了m_voBill同时改变m_alListData???
				voBill = (GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow);

				// 把当前登录人设置到vo
				if (voBill != null && voBill.getHeaderVO() != null) {
					GeneralBillHeaderVO voHeader = voBill.getHeaderVO();
					voHeader.setCregister(m_sUserID);
					voHeader.setCregistername(m_sUserName);
					voHeader.setDaccountdate(new nc.vo.pub.lang.UFDate(m_sLogDate));

					if (sBillStatus != null && sBillStatus.equals(BillStatus.AUDITED)) {
						voHeader.setCauditorid(m_sUserID);
						voHeader.setCauditorname(m_sUserName);
						voHeader.setDauditdate(new nc.vo.pub.lang.UFDate(m_sLogDate));
					}

					voHeader.setFbillflag(new Integer(sBillStatus));
					voBill.setParentVO(voHeader);
				}
				// 重置列表界面
				m_alListData.set(m_iLastSelListHeadRow, voBill);
			}
			// m_voBill刷新
			// 把当前登录人设置到vo
			if (m_voBill != null && m_voBill.getHeaderVO() != null) {
				GeneralBillHeaderVO voHeader = m_voBill.getHeaderVO();
				voHeader.setCregister(m_sUserID);
				voHeader.setCregistername(m_sUserName);
				voHeader.setDaccountdate(new nc.vo.pub.lang.UFDate(m_sLogDate));

				if (sBillStatus != null && sBillStatus.equals(BillStatus.AUDITED)) {
					voHeader.setCauditorid(m_sUserID);
					voHeader.setCauditorname(m_sUserName);
					voHeader.setDauditdate(new nc.vo.pub.lang.UFDate(m_sLogDate));
				}
				voHeader.setFbillflag(new Integer(sBillStatus));
				m_voBill.setParentVO(voHeader);
			}

			// 把当前登录人名称显示到界面
			if (getBillCardPanel().getTailItem("cregister") != null) getBillCardPanel().getTailItem("cregister").setValue(m_sUserID);
			if (getBillCardPanel().getTailItem("cregistername") != null) getBillCardPanel().getTailItem("cregistername").setValue(m_sUserName);
			if (getBillCardPanel().getTailItem("daccountdate") != null) getBillCardPanel().getTailItem("daccountdate").setValue(m_sLogDate);
			if (getBillCardPanel().getTailItem("taccounttime") != null) getBillCardPanel().getTailItem("taccounttime").setValue(m_voBill.getHeaderValue("taccounttime"));

			if (sBillStatus != null && sBillStatus.equals(BillStatus.AUDITED)) {
				if (getBillCardPanel().getTailItem("cauditorid") != null) getBillCardPanel().getTailItem("cauditorid").setValue(m_sUserID);
				if (getBillCardPanel().getTailItem("cauditorname") != null) getBillCardPanel().getTailItem("cauditorname").setValue(m_sUserName);
				if (getBillCardPanel().getTailItem("dauditdate") != null) getBillCardPanel().getTailItem("dauditdate").setValue(m_sLogDate);
			}
			if (getBillCardPanel().getHeadItem("fbillflag") != null) getBillCardPanel().getHeadItem("fbillflag").setValue(sBillStatus);
			// 刷新列表形式
			setListHeadData();
			selectListBill(m_iLastSelListHeadRow);
			// 设置按钮状态,签字不可用，取消签字可用
			// 已签字，所以设置按钮状态,签字不可用，取消签字可用
			setButtonStatus(false);

			getButtonTree().getButton(ICButtonConst.BTN_SIGN).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_EXECUTE_SIGN_CANCEL).setEnabled(true);
			// 不可删、改
			getButtonTree().getButton(ICButtonConst.BTN_BILL_EDIT).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_BILL_DELETE).setEnabled(false);

			updateButtons();

		} catch (Exception e2) {
			nc.vo.scm.pub.SCMEnv.error(e2);
		}

	}

	/**
	 * 创建者：王乃军 功能：刷新计划价 参数： 返回： 例外： 日期：(2001-5-8 19:08:05) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	public void freshPlanprice(ArrayList alNewPlanprice) {
		try {
			// 行数
			int iRowCount = getBillCardPanel().getRowCount();
			if (alNewPlanprice == null || alNewPlanprice.size() < iRowCount || m_voBill == null) {
				SCMEnv.out("alallinv nvl");
				return;
			}
			// 按行取，不管是否为空
			nc.ui.pub.bill.BillModel bmBill = getBillCardPanel().getBillModel();
			// 数量，用于计算金额
			Object oTempNum = null;
			UFDouble dNum = null;
			UFDouble dMny = null;
			GeneralBillItemVO[] voaBillItem = m_voBill.getItemVOs();
			for (int row = 0; row < iRowCount; row++) {
				bmBill.setValueAt(alNewPlanprice.get(row), row, m_sPlanPriceItemKey);
				// 需要同步m_voBill
				if (alNewPlanprice.get(row) != null) voaBillItem[row].setNplannedprice((UFDouble) alNewPlanprice.get(row));
				else voaBillItem[row].setNplannedprice(null);
				oTempNum = bmBill.getValueAt(row, m_sNumItemKey);
				// 同时有数量和单价时，才计算
				if (oTempNum != null && alNewPlanprice.get(row) != null) {
					dNum = (UFDouble) oTempNum;
					dMny = dNum.multiply((UFDouble) alNewPlanprice.get(row));
				} else dMny = null;

				bmBill.setValueAt(dMny, row, m_sPlanMnyItemKey);
				// 需要同步m_voBill
				voaBillItem[row].setNplannedmny(dMny);

			}

		} catch (Exception e2) {
			nc.vo.scm.pub.SCMEnv.error(e2);
		}
	}

	/**
	 * 此处插入方法说明。 功能：刷新ts,得到单据状态 参数： 返回： 例外： 日期：(2002-6-4 19:54:51)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return java.lang.String
	 * @param sBillPK
	 *            java.lang.String
	 */
	protected String freshStatusTs(String sBillPK) throws Exception {
		String sBillStatus = null;
		try {
			// query
			ArrayList alFreshRet = (ArrayList) GeneralBillHelper.queryInfo(new Integer(QryInfoConst.BILL_STATUS_TS), sBillPK);
			if (alFreshRet == null || alFreshRet.get(0) == null) {
				SCMEnv.out("Err,ret");
				return null;
			}
			// set
			// ts
			if (alFreshRet != null && alFreshRet.size() >= 2 && alFreshRet.get(1) != null) {
				ArrayList alTs = (ArrayList) alFreshRet.get(1);
				freshTs(alTs);
			}
			// first is billstatus
			sBillStatus = alFreshRet.get(0).toString();
			// third is vbillcode
			if (alFreshRet != null && alFreshRet.size() >= 3) {
				if (m_sCorpID.equals(m_voBill.getVBillCode())) {
					String billcode = (String) alFreshRet.get(2);
					getBillCardPanel().getHeadItem("vbillcode").setValue(billcode);
					m_voBill.setVBillCode(billcode);
					((GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow)).setVBillCode(billcode);
				}
			}

		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
		}
		return sBillStatus;
	}

	/**
	 * 创建者：王乃军 功能：在表单设置显示VO,不更新界面状态updateValue() 参数： 返回： 例外： 日期：(2001-5-9
	 * 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	public void freshTs(ArrayList alTs) throws Exception {
		if (alTs == null || alTs.size() == 0) throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000027")/* @res "传入的ts为空！" */);
		setTs(m_voBill, alTs);
		setTs(m_iLastSelListHeadRow, alTs);
		setUiTs(alTs);
	}

	/**
	 * 创建者：王乃军 功能：得到单据出入库属性 参数： 返回： 例外： 日期：(2001-5-9 9:23:32)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	public int getBillInOutFlag() {
		return m_iBillInOutFlag;
	}

	/**
	 * 创建者：王乃军 功能： 参数： 返回： 例外： 日期：(2001-11-23 18:05:45) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getBillTypeCode() {
		return m_sBillTypeCode;
	}

	/**
	 * 创建者：王乃军 功能：得到查询对话框 参数： 返回： 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	protected QueryConditionDlgForBill getConditionDlg() {
		if (ivjQueryConditionDlg == null) {
			ivjQueryConditionDlg = new QueryConditionDlgForBill(this);
			ivjQueryConditionDlg.setTempletID(m_sCorpID, m_sCurrentBillNode, m_sUserID, null);

			// 以下为对公司参照的初始化
			ArrayList alCorpIDs = new ArrayList();
			// try {
			// alCorpIDs = (ArrayList) invokeClient("queryInfo", new Class[] {
			// Integer.class, Object.class }, new Object[] {
			// new Integer(QryInfoConst.USER_CORP), m_sUserID });
			//
			// } catch (Exception e) {
			// nc.vo.scm.pub.SCMEnv.error(e);
			// }
			alCorpIDs.add(m_sCorpID);
			ivjQueryConditionDlg.initCorpRef("head.pk_corp", m_sCorpID, alCorpIDs);
			// 以下为对参照的初始化
			ivjQueryConditionDlg.initQueryDlgRef();

			// 隐藏常用条件
			ivjQueryConditionDlg.hideNormal();
			// 条码是否关闭查询条件body.bbarcodeclose
			ivjQueryConditionDlg.setCombox("body.bbarcodeclose", new String[][] {
					{
							" ", " "
					}, {
							"N", nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000108")
					/*
					 * @res
					 * "否"
					 */}, {
							"Y", nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000244")
					/*
					 * @res
					 * "是"
					 */}
			});

			// 设置下拉框显示
			ivjQueryConditionDlg.setCombox("qbillstatus", new String[][] {
					{
							"2", nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000313")
					/*
					* @res "制单"
					*/}, {
							"3", nc.ui.ml.NCLangRes.getInstance().getStrByID("40080402", "UPT40080402-000013")
					/*
					* @res "签字"
					*/}, {
							"A", nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000217")
					/*
					 * @res
					 * "全部"
					 */}
			});
			// set default logon date into query condiotn dlg
			ivjQueryConditionDlg.setInitDate("head.dbilldate", m_sLogDate);
			ivjQueryConditionDlg.setInOutFlag(m_iBillInOutFlag);

			// 查询对话框显示打印次数页签。
			ivjQueryConditionDlg.setShowPrintStatusPanel(true);

			// 修改自定义项目 add by hanwei 2003-12-09
			DefSetTool.updateQueryConditionClientUserDef(ivjQueryConditionDlg, m_sCorpID, ICConst.BILLTYPE_IC, "head.vuserdef", "body.vuserdef");
			getConDlginitself(ivjQueryConditionDlg);

			// 过滤库存组织，仓库,废品库,客户,供应商的数据权限，部门，业务员
			// zhy2005-06-10 客户和供应商不需要在普通单上过滤，（客户在销售出库单上过滤，供应商在采购入库单上过滤）
			// zhy2007-02-12 V51新需求:3、
			// 客商、地区分类、库存组织、项目受数据权限控制，部门、仓库、存货分类、存货受已定义的库管员匹配纪录的控制；
			/**
			 * 库管员:head.cwhsmanagerid 客商:head.cproviderid 库存组织:head.pk_calbody
			 * 仓库:head.cwarehouseid,head.cwastewarehouseid 项目:body.cprojectid
			 * 部门:head.cdptid 存货分类:invcl.invclasscode 存货:inv.invcode
			 */
			// ivjQueryConditionDlg.setCorpRefs("head.pk_corp", new String[] {
			// "head.cproviderid","head.pk_calbody", "head.cwarehouseid",
			// "head.cwastewarehouseid","body.cprojectid"
			// //, "head.cdptid", "head.cbizid"
			// });
			// ivjQueryConditionDlg.setDataPower(true, m_sCorpID);
			if (BillTypeConst.m_allocationIn.equals(getBillTypeCode()) || BillTypeConst.m_allocationOut.equals(getBillTypeCode())) ivjQueryConditionDlg.setCorpRefs("head.pk_corp", nc.ui.ic.pub.tools.GenMethod.getDataPowerFieldFromDlg(ivjQueryConditionDlg, false, new String[] {
					"head.cothercorpid", "head.coutcorpid", "body.creceieveid", "head.cothercalbodyid", "head.cotherwhid", "head.coutcalbodyid"
			}));
			else ivjQueryConditionDlg.setCorpRefs("head.pk_corp", nc.ui.ic.pub.tools.GenMethod.getDataPowerFieldFromDlgNotByProp(ivjQueryConditionDlg));

			// zhy205-05-19 加可还回数量条件
			// 借出单
			ivjQueryConditionDlg.setCombox("coalesce(body.noutnum,0)-coalesce(body.nretnum,0)-coalesce(body.ntranoutnum,0)", new Integer[][] {
				{
						new Integer(0), new Integer(0)
				}
			});
			// 借入单
			ivjQueryConditionDlg.setCombox("coalesce(body.ninnum,0)-coalesce(body.nretnum,0)-coalesce(body.ntranoutnum,0)", new Integer[][] {
				{
						new Integer(0), new Integer(0)
				}
			});

		}
		return ivjQueryConditionDlg;
	}

	/**
	 * 创建者：余大英 功能：得到当前已录入的存货ID 参数： 返回： 例外： 日期：(2001-5-9 9:23:32)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */

	public ArrayList getCurInvID() {
		// 存货ID
		ArrayList alAllInv = new ArrayList();
		// 行数
		int iRowCount = getBillCardPanel().getRowCount();
		// 按行取，不管是否为空
		nc.ui.pub.bill.BillModel bmBill = getBillCardPanel().getBillModel();
		for (int row = 0; row < iRowCount; row++)
			alAllInv.add(bmBill.getValueAt(row, m_sInvMngIDItemKey));
		return alAllInv;
	}

	/**
	 * 创建者：余大英 功能：得到当前已录入的存货ID 参数： //存货ID 返回：是否有存货 例外： 日期：(2001-5-9 9:23:32)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */

	public boolean getCurInvID(ArrayList alAllInv) {
		if (alAllInv == null) {
			SCMEnv.out("alallinv nvl");
			return false;
		}
		boolean bHaveInv = false;
		// 行数
		int iRowCount = getBillCardPanel().getRowCount();
		// 按行取，不管是否为空
		nc.ui.pub.bill.BillModel bmBill = getBillCardPanel().getBillModel();
		Object oTempInvID = null;
		for (int row = 0; row < iRowCount; row++) {
			oTempInvID = bmBill.getValueAt(row, m_sInvMngIDItemKey);
			alAllInv.add(oTempInvID);
			if (bHaveInv == false && oTempInvID != null && oTempInvID.toString().trim().length() > 0) bHaveInv = true;
		}
		return bHaveInv;
	}

	/**
	 * 创建者：王乃军 功能： 参数： 返回： 例外： 日期：(2001-11-23 18:05:45) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getCurrentBillNode() {
		return m_sCurrentBillNode;
	}

	/**
	 * 创建者：仲瑞庆 功能： 参数： 返回： 例外： 日期：(2001-10-30 15:06:35) 修改日期，修改人，修改原因，注释标志：
	 */
	protected PrintDataInterface getDataSource() {
		if (null == m_dataSource) {
			m_dataSource = new PrintDataInterface();
			BillData bd = getBillCardPanel().getBillData();
			m_dataSource.setBillData(bd);
			m_dataSource.setModuleName(m_sCurrentBillNode);
			m_dataSource.setTotalLinesInOnePage(getPrintEntry().getBreakPos());
		}
		return m_dataSource;
	}

	/**
	 * 创建者：王乃军 功能：构造外设输入控制类 参数： 返回： 例外： 日期：(2001-11-24 12:15:42)
	 * 修改日期，修改人，修改原因，注释标志：
	 */
	public nc.ui.ic.pub.device.DevInputCtrl getDevInputCtrl() throws Exception {

		if (m_dictrl == null) {
			m_dictrl = new nc.ui.ic.pub.device.DevInputCtrl();
			m_dictrl.setPk_corp(m_sCorpID);
			m_dictrl.setBillTypeCode(m_sBillTypeCode);
			m_dictrl.setCard(getBillCardPanel());
			m_dictrl.setTp(this);
		}
		m_dictrl.setup();
		return m_dictrl;
	}

	/**
	 * 创建者：王乃军 功能：得到用户输入的额外查询条件 参数：//查询条件数组 返回： 例外： 日期：(2001-5-9 9:23:32)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	public String getExtendQryCond(nc.vo.pub.query.ConditionVO[] voaCond) {
		// 单据状态条件,缺省无
		String sBillStatusSql = " (1=1) ";
		try {
			// -------- 查询条件字段 itemkey ---------
			String sFieldCode = null;
			// 从条件中查找最大最小日期
			// 单据状态
			String sBillStatus = "A";
			String sFreplenishflag = null;
			if (voaCond != null) {
				for (int i = 0; i < voaCond.length; i++) {
					if (voaCond[i] != null && voaCond[i].getFieldCode() != null) {
						sFieldCode = voaCond[i].getFieldCode().trim();
						if ("qbillstatus".equals(voaCond[i].getFieldCode().trim())) {
							if (voaCond[i].getValue() != null && voaCond[i].getRefResult() != null) sBillStatus = voaCond[i].getRefResult().getRefPK();
						} else if ("boutnumnull".equals(sFieldCode)) {

							voaCond[i].setFieldCode("body.noutnum");
							voaCond[i].setOperaCode(" is ");

							voaCond[i].setDataType(ConditionVO.INTEGER);

							if (voaCond[i].getValue() != null && "Y".equals(voaCond[i].getValue())) {

								voaCond[i].setValue(" not null ");
								m_sBnoutnumnull = "Y";
							} else {

								voaCond[i].setValue("  null ");
								m_sBnoutnumnull = "N";

							}
						}

						if ("freplenishflag".equals(voaCond[i].getFieldCode().trim())) {
							if (voaCond[i].getValue() != null && voaCond[i].getRefResult() != null) sFreplenishflag = voaCond[i].getRefResult().getRefPK();
						}

						if ("like".equals(voaCond[i].getOperaCode().trim()) && voaCond[i].getFieldCode() != null) {
							// String sFeildCode = voaCond[i].getFieldCode()
							// .trim();
							if (sFieldCode.equals("invcl.invclasscode") && voaCond[i].getValue() != null) {
								voaCond[i].setValue(voaCond[i].getValue() + "%");
							} else if (sFieldCode.equals("dept.deptcode") && voaCond[i].getValue() != null) {
								voaCond[i].setValue(voaCond[i].getValue() + "%");
							} else if (voaCond[i].getValue() != null) voaCond[i].setValue("%" + voaCond[i].getValue() + "%");
						}
					}
				}
			}
			// 缺省是A
			if ("2".equals(sBillStatus)) // 自由
			sBillStatusSql = " fbillflag=" + nc.vo.ic.pub.bill.BillStatus.FREE;
			else if ("3".equals(sBillStatus)) // 签字的
			sBillStatusSql = " ( fbillflag=" + nc.vo.ic.pub.bill.BillStatus.SIGNED + " OR fbillflag=" + nc.vo.ic.pub.bill.BillStatus.AUDITED + ") ";

			// 退库查询 add by hanwei 2003-10-10
			if (nc.vo.ic.pub.BillTypeConst.BILLNORMAL.equalsIgnoreCase(sFreplenishflag)) {
				sBillStatusSql += " AND ( freplenishflag is null or freplenishflag='N' )";
			} else if (nc.vo.ic.pub.BillTypeConst.BILLSENDBACK.equalsIgnoreCase(sFreplenishflag)) {
				sBillStatusSql += " AND ( freplenishflag='Y' )";
			} else if (nc.vo.ic.pub.BillTypeConst.BILLALL.equalsIgnoreCase(sFreplenishflag)) {
				sBillStatusSql += "  ";
			}

			// 去掉freplenishflag 是否退库
			String saItemKey[] = new String[] {
					"qbillstatus", "freplenishflag"
			};
			filterCondVO2(voaCond, saItemKey);
			// 其他条件
			String sOtherCond = getConditionDlg().getWhereSQL(voaCond);
			if (sOtherCond != null) sBillStatusSql += " AND ( " + sOtherCond + " )";
		} catch (Exception e) {
			handleException(e);
		}

		return sBillStatusSql;
	}

	/**
	 * 创建日期：(2003-3-4 17:13:59) 作者：韩卫 修改日期： 修改人： 修改原因： 方法说明：
	 * 
	 * @return nc.ui.ic.pub.bill.InvoInfoBYFormula
	 */
	public InvoInfoBYFormula getInvoInfoBYFormula() {
		if (m_InvoInfoBYFormula == null) m_InvoInfoBYFormula = new InvoInfoBYFormula(getCorpPrimaryKey());
		return m_InvoInfoBYFormula;
	}

	/**
	 * 去存货管理档案定义出库是否跟踪入库的参数 功能： 参数：存货VO 返回：boolean 例外： 日期：(2002-05-20 19:55:18)
	 * 修改日期，修改人，修改原因，注释标志：
	 */
	protected boolean getIsInvTrackedBill(InvVO invvo) {
		if (invvo != null && invvo.getOuttrackin() != null) {
			return invvo.getOuttrackin().booleanValue();

		} else {
			return false;
		}
	}

	/**
	 * 返回 FreeItemRefPane1 特性值。
	 * 
	 * @return nc.ui.ic.pub.freeitem.FreeItemRefPane
	 */
	/* 警告：此方法将重新生成。 */
	protected LocatorRefPane getLocatorRefPane() {
		if (ivjLocatorRefPane == null) {
			try {
				ivjLocatorRefPane = new LocatorRefPane(m_iBillInOutFlag);
				ivjLocatorRefPane.setName("LocatorRefPane");
				ivjLocatorRefPane.setLocation(209, 4);
				// user code begin {1}
				// ivjLocatorRefPane.setInOutFlag(InOutFlag.IN);

				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjLocatorRefPane;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-9-19 16:02:07)
	 * 
	 * @return nc.ui.ic.ic101.OrientDialog
	 * @author:余大英
	 */
	public nc.ui.ic.pub.orient.OrientDialog getOrientDlg() {
		if (m_dlgOrient == null) {
			m_dlgOrient = new nc.ui.ic.pub.orient.OrientDialog(this);

			// ivjQueryConditionDlg.setPKCorp(m_sCorpID);

		}
		return m_dlgOrient;
	}

	/**
	 * 创建者：王乃军 功能：得到当前进度条 参数： 返回： 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	protected PrgBar getPrgBar() {
		if (m_pbProgressBar == null) {
			m_pbProgressBar = new PrgBar(this);
		}
		// -----------------
		return m_pbProgressBar;
	}

	/**
	 * 创建者：王乃军 功能：得到进度条 参数：iMode:何种情况下用？ 返回： 例外： 日期：(2001-5-9 9:23:32)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	protected PrgBar getPrgBar(int iMode) {
		if (m_pbProgressBar == null) {
			m_pbProgressBar = new PrgBar(this);
		}
		// 总单位时间
		int iTimeTotal = 1000;
		// 标题
		String sTitle = "";
		// 是否重复显示进度
		boolean bRepeat = false;
		// 时间间隔
		int iTimeInterval = 10;

		switch (iMode) {
		// 保存
		case PrgBar.PB_SAVE:
			iTimeTotal = 10000;
			sTitle = nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000001")/* @res "保存" */;
			break;
		// 查询
		case PrgBar.PB_QRY:
			iTimeTotal = 50000;
			sTitle = nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000006")/* @res "查询" */;
			iTimeInterval = 50;
			bRepeat = true;
			break;
		// 删除
		case PrgBar.PB_DEL:
			iTimeTotal = 1000;
			sTitle = nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000039")/* @res "删除" */;
			break;
		// 签字
		case PrgBar.PB_SIGN:
			iTimeTotal = 1000;
			sTitle = nc.ui.ml.NCLangRes.getInstance().getStrByID("40080402", "UPT40080402-000013")/* @res "签字" */;
			break;
		// 取消签字
		case PrgBar.PB_CANCELSIGN:
			iTimeTotal = 1000;
			sTitle = nc.ui.ml.NCLangRes.getInstance().getStrByID("40080402", "UPT40080402-000014")/* @res "取消签字" */;
			break;
		}
		// 设置总单位时间
		m_pbProgressBar.setTotal(iTimeTotal);
		// 设置标题
		m_pbProgressBar.setTitle(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000314")/* @res "正在" */
				+ sTitle + nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000315")/* @res "，请稍候..." */);
		// 设置时间间隔
		m_pbProgressBar.setSleepTime(iTimeInterval);
		// 设置是否重复显示
		m_pbProgressBar.setRepeat(bRepeat);
		// -----------------
		return m_pbProgressBar;
	}

	/**
	 * 创建者：仲瑞庆 功能： 参数： 返回： 例外： 日期：(2001-10-30 15:06:35) 修改日期，修改人，修改原因，注释标志：
	 */
	protected nc.ui.pub.print.PrintEntry getPrintEntry() {
		if (null == m_print) {
			m_print = new nc.ui.pub.print.PrintEntry(null, null);
			m_print.setTemplateID(m_sCorpID, m_sCurrentBillNode, m_sUserID, null);
		}
		return m_print;
	}

	protected nc.ui.pub.print.PrintEntry getPrintEntryNew() {
		nc.ui.pub.print.PrintEntry pe = new nc.ui.pub.print.PrintEntry(null, null);
		pe.setTemplateID(m_sCorpID, m_sCurrentBillNode, m_sUserID, null);
		return pe;
	}

	/**
	 * 创建者：zhx 功能：返回用户选择的业务类型 参数： 返回： 例外： 日期：(2002-12-10 9:23:32)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	private String getSelBusiType() {
		if (getButtonTree().getButton(ICButtonConst.BTN_BUSINESS_TYPE) != null) {
			ButtonObject[] boaAll = getButtonTree().getButton(ICButtonConst.BTN_BUSINESS_TYPE).getChildButtonGroup();
			if (boaAll != null) {
				for (int i = 0; i < boaAll.length; i++)
					if (boaAll[i].isSelected()) { return boaAll[i].getTag(); }
			}
		}
		return null;
	}

	/**
	 * 类型说明： 创建日期：(2002-11-18 15:49:54) 作者：余大英 修改日期： 修改人： 修改原因： 算法说明：
	 * 
	 * @return java.util.ArrayList
	 */
	public ArrayList getSelectedBills() {

		ArrayList albill = new ArrayList();
		int iSelListHeadRowCount = getBillListPanel().getHeadTable().getSelectedRowCount();
		if (iSelListHeadRowCount <= 0) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH003")/* @res "请选择要处理的数据！" */);
			return null;
		}
		int[] arySelListHeadRows = new int[iSelListHeadRowCount];
		arySelListHeadRows = getBillListPanel().getHeadTable().getSelectedRows();

		GeneralBillVO voaBill[] = new GeneralBillVO[iSelListHeadRowCount];
		Vector vHeadPK = new Vector();
		Vector vIndex = new Vector();
		// 当只有一行并且经过排序时将排序结果取出by zss 2004-02-09
		// if (iSelListHeadRowCount == 1 && m_sLastKey != null) {
		//			
		// albill.add(m_alListData .get(arySelListHeadRows[0]));
		// return albill;
		// // if (voaBill[0].getChildrenVO() == null ||
		// // voaBill[0].getChildrenVO().length == 0) {
		//
		// // vHeadPK.addElement(((GeneralBillHeaderVO)
		// // voaBill[0].getParentVO()).getCgeneralhid());
		// // vIndex.addElement(new Integer(arySelListHeadRows[0]));
		// // }
		// } else
		for (int i = 0; i < iSelListHeadRowCount; i++) {
			if (m_alListData != null && m_alListData.size() > arySelListHeadRows[i]) {

				voaBill[i] = (GeneralBillVO) m_alListData.get(arySelListHeadRows[i]);
				if (voaBill[i].getChildrenVO() == null || voaBill[i].getChildrenVO().length == 0) {

					vHeadPK.addElement(((GeneralBillHeaderVO) voaBill[i].getParentVO()).getCgeneralhid());
					vIndex.addElement(new Integer(arySelListHeadRows[i]));
				}

			}
		}

		// 查询表体数据
		if (vIndex.size() > 0) {
			String[] saPK = new String[vHeadPK.size()];
			int[] indexs = new int[vIndex.size()];
			vHeadPK.copyInto(saPK);

			for (int i = 0; i < vIndex.size(); i++) {
				indexs[i] = ((Integer) vIndex.get(i)).intValue();
			}
			qryItems(indexs, saPK);
		}
		for (int i = 0; i < arySelListHeadRows.length; i++) {
			if (m_alListData != null && m_alListData.size() > arySelListHeadRows[i]) {

				albill.add((GeneralBillVO) m_alListData.get(arySelListHeadRows[i]));
			}
		}

		return albill;
	}

	/**
	 * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2002-10-23 10:31:53) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @param voSNs
	 *            nc.vo.ic.pub.sn.SerialVO[]
	 */
	private String getSNString(SerialVO[] voSNs, String cspaceid) {
		if (voSNs == null || voSNs.length == 0) return null;
		StringBuffer str = new StringBuffer();

		for (int i = 0; i < voSNs.length; i++) {
			if (cspaceid != null) {
				if (cspaceid.equals(voSNs[i].getCspaceid())) str.append(voSNs[i].getVsn() + ";");
			} else str.append(voSNs[i].getVsn() + ";");
		}
		return str.toString();

	}

	/**
	 * 得到m_voBill中的单据类型编码 功能： 参数： 返回： 例外： 日期：(2001-10-12 13:18:06)
	 * 修改日期，修改人，修改原因，注释标志：
	 */
	protected String getSourBillTypeCode() {
		GeneralBillVO voBill = null;
		// 列表形式和表单形式不同
		if (m_iCurPanel == BillMode.List && m_iLastSelListHeadRow >= 0 && m_alListData != null && m_alListData.size() > m_iLastSelListHeadRow && m_alListData.get(m_iLastSelListHeadRow) != null)
		// 先读过来
		voBill = (GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow);
		else
		// 直接用m_vo
		voBill = m_voBill;

		if (voBill != null && voBill.getItemCount() > 0) return (String) voBill.getItemValue(0, "csourcetype");
		else return null;
	}

	/**
	 * 创建者：王乃军 功能：查指定序号单据的货位/序列号数据,用于打印货位序列号名细 参数： 返回： 例外： 日期：(2001-5-9 9:23:32)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	protected GeneralBillVO getWholeBill(int iBillNum) {
		if (m_alListData == null || m_alListData.size() < iBillNum) {
			SCMEnv.out("list ERR.");
			return null;
		}
		qryLocSNSort(iBillNum, QryInfoConst.LOC_SN);
		// Modified by zss for PrintLocSN
		GeneralBillVO voMyBill = null;
		if (m_sLastKey != null && m_voBill != null) voMyBill = m_voBill;
		else voMyBill = (GeneralBillVO) m_alListData.get(iBillNum);

		/*
		 * try { int iMode = QryInfoConst.LOC_SN;
		 * 
		 * ArrayList alLocatorData = null; ArrayList alSerialData = null; if
		 * (voMyBill == null) { SCMEnv.out("this bill null."); return null;
		 * //============================================================= }
		 * //测试此单据是否填了数量，如填了则需要继续执行，否则单据本来就没有这些数据，不用读了 int i = 0, iRowCount =
		 * voMyBill.getItemCount(); //测试是否已经读过这些数据了。 for (i = 0; i < iRowCount;
		 * i++) if (voMyBill.getItemValue(i, "locator") != null ||
		 * voMyBill.getItemValue(i, "serial") != null) break;
		 * 
		 * for (i = 0; i < iRowCount; i++) //测试实出/入数量 if
		 * (voMyBill.getItemValue(i, "ninnum") != null &&
		 * voMyBill.getItemValue(i, "ninnum").toString().length() > 0 ||
		 * voMyBill.getItemValue(i, "noutnum") != null &&
		 * voMyBill.getItemValue(i, "noutnum").toString().length() > 0) break;
		 * 
		 * if (i >= iRowCount) //无数量 return voMyBill;
		 * //=============================================================
		 * 
		 * WhVO voWh = voMyBill.getWh(); //货位管理的仓库需要读货位数据 if (voWh != null &&
		 * voWh.getIsLocatorMgt() != null && voWh.getIsLocatorMgt().intValue()
		 * == 1) iMode = QryInfoConst.LOC_SN; else iMode = QryInfoConst.SN;
		 * 
		 * Integer iSearchMode = new Integer(iMode);
		 * //////////////////////////////iMode); //查货位 & 序列号 3 or 序列号 4
		 * ArrayList alAllData = (ArrayList) invokeClient("queryInfo", new
		 * Class[] { Integer.class, Object.class }, new Object[] { iSearchMode,
		 * voMyBill.getPrimaryKey()});
		 * 
		 * if (iMode == QryInfoConst.LOC_SN) { if (alAllData != null &&
		 * alAllData.size() >= 2) { alLocatorData = (ArrayList)
		 * alAllData.get(0); alSerialData = (ArrayList) alAllData.get(1); }
		 * //else } else { //=== SN only if (alAllData != null &&
		 * alAllData.size() >= 1) alSerialData = (ArrayList) alAllData.get(0);
		 * //else } //修正货位数据 if (alLocatorData == null || alLocatorData.size()
		 * != iRowCount) { alLocatorData = new ArrayList(); for (int j = 0; j <
		 * iRowCount; j++) alLocatorData.add(null); } //修正序列号数据 if (alSerialData
		 * == null || alSerialData.size() != iRowCount) { alSerialData = new
		 * ArrayList(); for (int j = 0; j < iRowCount; j++)
		 * alSerialData.add(null); } //货位序列号保存到alListData for (int j = 0; j <
		 * iRowCount; j++) { if (alLocatorData != null && j <
		 * alLocatorData.size()) voMyBill.setItemValue(j, "locator",
		 * alLocatorData.get(j)); if (alSerialData != null && j <
		 * alSerialData.size()) voMyBill.setItemValue(j, "serial",
		 * alSerialData.get(j)); } } catch (Exception e) {
		 * nc.vo.scm.pub.SCMEnv.error(e); }
		 */
		return voMyBill;
	}

	/**
	 * 隐藏列表形式下的表头列。 创建日期：(2001-5-29 9:13:20)
	 * 
	 * @param strKey
	 *            java.lang.String
	 */
	public void hideListTableHeadCol(nc.ui.pub.bill.BillScrollPane bspTable, int iCol) {
		if (iCol < 0 || bspTable == null || bspTable.getTableModel() == null || iCol >= bspTable.getTableModel().getColumnCount()) {
			SCMEnv.out("hide col <0");
			return;
		}
		// 找到相应的Item
		nc.ui.pub.bill.BillItem item = bspTable.getTableModel().getBodyItems()[iCol];
		javax.swing.table.TableColumn tclCol = null;
		try {
			// 得到列
			tclCol = bspTable.getTable().getColumn(item.getName());
			// 列模型
			javax.swing.table.TableColumnModel cm = bspTable.getTable().getColumnModel();
			// 从model删除
			cm.removeColumn(tclCol);
			// 设为不可视
			item.setShow(false);
		} catch (Throwable e) {
			// SCMEnv.out("列已被隐藏！");
		}
	}

	/**
	 * 
	 * 方法功能描述：获得ButtonTree对象。<br>
	 * ButtonTree可以从“功能注册”中按照按钮的CLASS_NAME获得某个按钮的实例
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * setButtons(getButtonTree().getButtonArray()); m_boAdd =
	 * bt.getButton(“增加”)；
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @return ButtonTree对象
	 *         <p>
	 * @author duy
	 * @time 2007-2-1 下午06:00:49
	 */
	public ButtonTree getButtonTree() {
		if (m_buttonTree == null) {
			try {
				m_buttonTree = new ButtonTree(m_sCurrentBillNode);
			} catch (BusinessException e) {
				handleException(e);
			}
		}
		return m_buttonTree;
	}

	/**
	 * 
	 * 方法功能描述：按钮的初始化。
	 * <p>
	 * <b>参数说明</b>
	 * <p>
	 * 
	 * @deprecated
	 * @author duy
	 * @time 2007-2-5 下午02:56:08
	 */
	protected void initButtons() {
	}

	/**
	 * 
	 /** 创建者：王乃军 功能：初始化按钮。 参数： 返回： 例外： 日期：(2001-11-23 18:11:18)
	 * 修改日期，修改人，修改原因，注释标志： 说明 by hanwei 2003-10-10
	 * m_vTopMenu.insertElementAt(m_vboSendback, 0); 是用来查入指定的菜单位置
	 * 父菜单是在initButtonsData()方法里面初试化 子类里面如果有
	 * nc.ui.ic.ic201.ClientUI.setButtonsStatus(int) super.initButtonsData();
	 * 要去掉super. 由于setButtonsStatus重复调用initButtonsData()，
	 * 所以父菜单对象必须在本方法中声明：m_boSendback = new ButtonObject("退货", "退货", 0);
	 * 否则会导致子菜单重复增加。
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * <b>参数说明</b>
	 * <p>
	 * 
	 * @author duy
	 * @deprecated
	 * @time 2007-2-2 上午11:57:14
	 */
	protected void initButtonsData() {
	}

	/**
	 * 此处插入方法说明。 功能：初始化 外设录入 按钮。 参数： 返回： 例外： 日期：(2002-9-28 10:19:23)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @deprecated
	 */
	protected void initDevInputButtons() {
	}

	/**
	 * 创建者：王乃军 功能：初始化系统参数 参数： 返回： 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	abstract protected void initPanel();

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
			// IC010 是否使用删除方案。
			// IC060 是否保留最初的制单人。
			// IC030 单据号是否允许手工输入

			// 参数编码 含义 缺省值
			// BD501 数量小数位 2
			// BD502 辅计量数量小数位 2
			// BD503 换算率 2
			// BD504 存货成本单价小数位 2
			// BD505 采购/销售单价小数位 2

			String[] saParam = new String[] { // "IC028", "IC010",
					"BD501", "BD502", "BD503", "BD504", "BD301",// "IC060",
					"IC030",
					"IC062",
					"IC0621",
					"IC0641",
					"IC0642",
					"IC050",
					"BD505" // modify by liuzy 2007-04-10
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

			ArrayList alRetData = null;
			alRetData = (ArrayList) ICReportHelper.queryInfo(new Integer(QryInfoConst.INIT_PARAM), alAllParam);

			// 目前读两个。
			if (alRetData == null || alRetData.size() < 2) {
				showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000045")/* @res "初始化参数错误！" */);
				return;
			}
			// 读回的参数值
			String[] saParamValue = (String[]) alRetData.get(0);
			// 追踪到单据参数,默认设置为"N"
			if (saParamValue != null && saParamValue.length >= alAllParam.size()) {
				// if (saParamValue[0] != null)
				// m_sTrackedBillFlag =
				// saParamValue[0].toUpperCase().trim();
				// 是否保存即签字。默认设置为"N"
				// if (saParamValue[0] != null)
				// m_sSaveAndSign = saParamValue[0].toUpperCase().trim();
				// BD501 数量小数位 2
				if (saParamValue[0] != null) m_ScaleValue.setNumScale(Integer.parseInt(saParamValue[0]));
				// BD502 辅计量数量小数位 2
				if (saParamValue[1] != null) m_ScaleValue.setAssistNumScale(Integer.parseInt(saParamValue[1]));
				// BD503 换算率 2
				if (saParamValue[2] != null) m_ScaleValue.setHslScale(Integer.parseInt(saParamValue[2]));
				// BD504 存货成本单价小数位 2
				if (saParamValue[3] != null) m_ScaleValue.setPriceScale(Integer.parseInt(saParamValue[3]));
				// BD301 本币小数位
				if (saParamValue[4] != null) m_ScaleValue.setMnyScale(Integer.parseInt(saParamValue[4]));
				// IC060 是否保留最初制单人 'N' 'Y'
				// if (saParamValue[7] != null)
				// m_sRemainOperator = saParamValue[7].toUpperCase().trim();
				// IC030 是否允许编辑单据号 'N' 'Y'
				if (saParamValue[5] != null && "Y".equalsIgnoreCase(saParamValue[5].trim())) m_bIsEditBillCode = true;

				// IC062 是否保存条码
				if (saParamValue[6] != null && "Y".equalsIgnoreCase(saParamValue[6].trim())) m_bBarcodeSave = true;
				else m_bBarcodeSave = false;

				// m_bBarcodeSave = true;
				// IC063 条码不完整是否保存条码
				if (saParamValue[7] != null && "Y".equalsIgnoreCase(saParamValue[7].trim())) m_bBadBarcodeSave = true;
				else m_bBadBarcodeSave = false;

				// IC0641 条码分组显示的行数
				if (saParamValue[8] != null && saParamValue[8].trim().length() > 0) m_iBarcodeUIColorRow = Integer.parseInt(saParamValue[8].trim());

				// IC0642 条码分组显示的颜色
				if (saParamValue[9] != null && saParamValue[9].trim().length() > 0) m_sColorRow = saParamValue[9].trim();

				// IC050 存货参照是否按照仓库过滤
				if (saParamValue[10] != null && "仓库".equalsIgnoreCase(saParamValue[10].trim())) m_isWhInvRef = true;
				else m_isWhInvRef = false;

				// BD505 采购/销售单价小数位 2
				// add by liuzy 2007-04-10
				if (saParamValue[11] != null) {
					nc.ui.pub.bill.BillItem billItem = null;

					String[] saColumns = {
							"nsaleprice", "ntaxprice", "npprice", "nquoteprice"
					};
					//getBillCardPanel().getBillData().getBodyItem("nsaleprice")
					for (int i = 0, j = saColumns.length; i < j; i++) {
						billItem = getBillCardPanel().getBodyItem(saColumns[i]);
						if (billItem != null) billItem.setDecimalDigits(Integer.parseInt(saParamValue[11]));
						billItem = null;
					}
				}

				SCMEnv.out("-------------- <mny>==========" + m_ScaleValue.getMnyScale());
			}
			// 读回的操作对应的公司
			m_alUserCorpID = (ArrayList) alRetData.get(1);

			// 系统起用日期
			m_sStartDate = m_sLogDate;

			if (alRetData.size() > 2) {
				m_sStartDate = (String) alRetData.get(2);

			}

			//

			m_ScaleKey.setNumKeys(new String[] {
					"nshouldinnum", "nshouldoutnum", "nsignnum", "ntranoutnum", "nretnum", "noutnum", "nleftnum", "ninnum", "neconomicnum", "nsafestocknum", "norderpointnum", "nmaxstocknum", "nminstocknum", "nsettlenum1", "naccountnum2", "nsignnum", "ntoaccountnum", "weight", "volume", "npacknum", "ncorrespondnum", "naccumwastnum", "ningrossnum", "noutgrossnum", "nleftgrsnum", "ntarenum", "nkdnum", "nreplenishednum"
			});

			m_ScaleKey.setAssistNumKeys(new String[] {
					"ninassistnum", "nleftastnum", "nneedinassistnum", "noutassistnum", "nretastnum", "nshouldoutassistnum", "ntranoutastnum", "naccountassistnum2", "naccountassistnum1", "nsignassistnum", "ncorrespondastnum", "nquoteunitnum", "nreplenishedastnum"
			});
			m_ScaleKey.setPriceKeys(new String[] {
					"nprice", "nplannedprice", /*
												 * "nsaleprice",
												 * "ntaxprice"
												 * ,
												 * "npprice"
												 * ,
												 * "nquoteprice"
												 */
			});
			m_ScaleKey.setMnyKeys(new String[] {
					"nmny", "nplannedmny", "nsalemny", "ntaxmny", "ndiscountmny", "nnetmny", "npmoney", "nsettlemny1", "nmaterialmoney", "ntoaccountmny"
			});
			m_ScaleKey.setHslKeys(new String[] {
					"hsl", "nquoteunitrate"
			});

		} catch (Exception e) {
			SCMEnv.out("can not get para" + e.getMessage());
			if (e instanceof nc.vo.pub.BusinessException) showErrorMessage(e.getMessage());
		}
	}

	/**
	 * 创建者：王乃军 功能：如果当前选中的单据不是和本节点相同的单据类型(如借入单上查出的期初单)，不能删除.
	 * 
	 * 参数： 返回： 例外： 日期：(2001-11-23 18:11:18) 修改日期，修改人，修改原因，注释标志：
	 */
	protected boolean isCurrentTypeBill() {
		// 如果不是和本节点相同的单据类型(如借入单上查出的期初单)，不能删除.
		try {
			// 当前选中的单据
			GeneralBillVO voBill = null;
			if (m_iCurPanel == BillMode.List) voBill = (GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow);
			else voBill = m_voBill;
			return m_sBillTypeCode.equals(voBill.getHeaderVO().getCbilltypecode());
		} catch (Exception e) {
			SCMEnv.out(e.getMessage());
		}
		return false;
	}

	/**
	 * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2002-06-03 11:32:25) 修改日期，修改人，修改原因，注释标志：
	 */
	protected boolean isDispensedBill(GeneralBillVO gvo) {
		if (gvo == null) {

			// 列表形式和表单形式不同
			if (m_iCurPanel == BillMode.List && m_iLastSelListHeadRow >= 0 && m_alListData != null && m_alListData.size() > m_iLastSelListHeadRow && m_alListData.get(m_iLastSelListHeadRow) != null)
			// 先读过来
			gvo = (GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow);
			else
			// 直接用m_vo
			gvo = m_voBill;
		}

		if (gvo != null && gvo.getItemCount() > 0) {
			boolean frowflag = false;
			for (int i = 0; i < gvo.getItemCount(); i++) {

				Integer fbillrowflag = gvo.getItemVOs()[i].getFbillrowflag() == null ? null : gvo.getItemVOs()[i].getFbillrowflag();
				if (fbillrowflag != null && fbillrowflag.intValue() == nc.vo.ic.pub.BillRowType.afterConvert) {
					frowflag = true;
					break;
				}

			}
			return frowflag;
		} else return false;

	}

	/**
	 * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2002-06-03 11:32:25) 修改日期，修改人，修改原因，注释标志：
	 */
	protected boolean isDispensedBill(GeneralBillVO gvo, int rownum) {
		if (gvo == null) {

			// 列表形式和表单形式不同
			if (m_iCurPanel == BillMode.List && m_iLastSelListHeadRow >= 0 && m_alListData != null && m_alListData.size() > m_iLastSelListHeadRow && m_alListData.get(m_iLastSelListHeadRow) != null)
			// 先读过来
			gvo = (GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow);
			else
			// 直接用m_vo
			gvo = m_voBill;
		}

		if (gvo != null && gvo.getItemCount() > 0) {
			boolean frowflag = false;

			Integer fbillrowflag = gvo.getItemVOs()[rownum].getFbillrowflag() == null ? null : gvo.getItemVOs()[rownum].getFbillrowflag();
			if (fbillrowflag != null && fbillrowflag.intValue() == nc.vo.ic.pub.BillRowType.afterConvert) {
				frowflag = true;

			}

			return frowflag;
		} else return false;

	}

	/**
	 * 创建者：王乃军 功能： 参数： 返回： 例外： 日期：(2001-11-24 12:14:35) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return boolean
	 */
	public boolean isNeedBillRef() {
		return m_bNeedBillRef;
	}

	/**
	 * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2002-06-11 9:22:38) 修改日期，修改人，修改原因，注释标志：
	 */
	protected boolean isSetInv(GeneralBillVO gvo, int rownum) {
		if (gvo != null && rownum != -1 && (rownum <= gvo.getItemCount() - 1 && rownum >= 0)) if (gvo.getItemInv(rownum).getIsSet() != null && gvo.getItemInv(rownum).getIsSet().intValue() == 1) return true;
		else return false;
		else return false;

	}

	/**
	 * 创建者：张欣 功能：当前选中行是否能序列号分配，要考虑列表/表单下的选中 参数：//当前选中的行 返回： 例外： 日期：(2001-5-9
	 * 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	public boolean isSNmgt(int iCurSelBodyLine) {

		if (iCurSelBodyLine >= 0) {
			InvVO voInv = null;
			// 读表体vo,区分列表下还是表单下
			// 表单形式下
			if (BillMode.Card == m_iCurPanel) {
				if (m_voBill == null) {
					SCMEnv.out("bill null E.");
					return false;
				}
				voInv = m_voBill.getItemInv(iCurSelBodyLine);
			} else // 列表形式下
			if (m_iLastSelListHeadRow >= 0 && m_iLastSelListHeadRow < m_alListData.size()) {
				GeneralBillVO bvo = (GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow);
				if (bvo == null) {
					SCMEnv.out("bill null E.");
					return false;
				}
				voInv = bvo.getItemInv(iCurSelBodyLine);
			}

			if (voInv != null && voInv.getIsSerialMgt() != null && voInv.getIsSerialMgt().intValue() == 1) return true;
			else return false;
		} else return false;
	}

	protected boolean m_bRefBills = false;// 是否参照生成多张单据

	private String m_sBizTypeRef = null;

	public void setBillRefMultiVOs(String sBizType, GeneralBillVO[] vos) throws Exception {
		if (vos == null || vos.length <= 0) return;

		if (vos != null && vos.length == 1) {
			setRefBillsFlag(false);
			setBillRefResultVO(sBizType, vos);// 老的
			return;
		}
		m_sBizTypeRef = sBizType;

		// 处理行号
		nc.ui.scm.pub.report.BillRowNo.setVOsRowNoByRule(vos, m_sBillTypeCode, m_sBillRowNo);
		// 设置数据到列表
		setDataOnList(vos);

	}

	protected void setRefBillsFlag(boolean bRefBills) {
		m_bRefBills = bRefBills;
	}

	/**
	 * 创建者：张欣 功能：选择了业务类型 参数： 返回： 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 */
	protected void onBizType(ButtonObject bo) {
		try {
			// 按钮事件处理
			nc.ui.scm.pub.redun.RedunSourceDlg.childButtonClicked(bo, m_sCorpID, m_sCurrentBillNode, m_sUserID, m_sBillTypeCode, this);

			// 得到用户选择的业务类型的方法. 20021209
			String sBusiTypeID = getSelBusiType();

			if (!nc.ui.scm.pub.redun.RedunSourceDlg.makeFlag()) {
				// 非自制方式
				if (nc.ui.scm.pub.redun.RedunSourceDlg.isCloseOK()) {

					long lTime = System.currentTimeMillis();
					nc.vo.pub.AggregatedValueObject[] vos = null;
					AggregatedValueObject[] newaggvo = null;
					try {
						vos = (GeneralBillVO[]) nc.ui.scm.pub.redun.RedunSourceDlg.getRetsVos();
						newaggvo = nc.ui.scm.pub.redun.RedunSourceDlg.getRetsVos();
					} catch (Exception e) {
						nc.ui.ic.pub.tools.GenMethod.handleException(null, null, e);
						return;
					}
					if (vos == null || vos.length <= 0) return;
					SCMEnv.showTime(lTime, "获得参照VO:");
					for (int i = 0; i < vos.length; i++) {
						if (vos[i] == null) continue;
						GeneralBillItemVO[] itemvos = (GeneralBillItemVO[]) vos[i].getChildrenVO();
						String nodekey = getCurrentBillNode();
						if(nodekey.equals("40080802")){
							for (int j = 0; j < itemvos.length; j++) {
								if (itemvos[j] == null) continue;
								// itemvos[j].getvb
							}
						}else{
							for (int j = 0; j < itemvos.length; j++) {
								if (itemvos[j] == null) continue;
								// itemvos[j].getvb
							}
						}
					}
					if (vos != null && vos.length > 0)
					// 检查单据是否来源于新的参照界面
					if (!ICConst.IsFromNewRef.equals(vos[0].getParentVO().getAttributeValue(ICConst.IsFromNewRef))) {
						// 按库存默认方式分单
						vos = GenMethod.splitGeneralBillVOs((GeneralBillVO[]) vos, getBillTypeCode(), getBillListPanel().getHeadBillModel().getFormulaParse());
						// 将外来单据的单位转换为库存默认单位.
						GenMethod.convertICAssistNumAtUI((GeneralBillVO[]) vos, getBillListPanel().getBodyBillModel().getFormulaParse());
					}

					// v5 lj 支持多张单据参照生成
					if (vos != null && vos.length == 1) {
						setRefBillsFlag(false);
						setBillRefResultVO(sBusiTypeID, vos);
					} else {
						setRefBillsFlag(true);// 是参照生成多张
						setBillRefMultiVOs(sBusiTypeID, (GeneralBillVO[]) vos);
					}
					// end v5

				}
			} else {
				// 自制单据
				setRefBillsFlag(false);
				onAdd(true, null);
				/* 重置单据表体行的存货参照过滤和参照的TextField的可编辑与否 * */
				nc.ui.pub.bill.BillItem bi = bi = getBillCardPanel().getBodyItem("cinventorycode");
				// ((nc.ui.pub.beans.UIRefPane) bi.getComponent())
				// .getUITextField().setEditable(true);
				RefFilter.filtInv(bi, m_sCorpID, null);
				// set user selected 业务类型 20021209
				if (getBillCardPanel().getHeadItem("cbiztype") != null) {
					getBillCardPanel().setHeadItem("cbiztype", sBusiTypeID);
					((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("cbiztype").getComponent()).setPK(sBusiTypeID);
					nc.ui.pub.bill.BillEditEvent event = new nc.ui.pub.bill.BillEditEvent(getBillCardPanel().getHeadItem("cbiztype"), sBusiTypeID, "cbiztype");
					afterEdit(event);

				}
				// 默认情况下，退库状态不可以用 add by hanwei 2003-10-19
				nc.ui.ic.pub.bill.GeneralBillUICtl.setSendBackBillState(this, false);

			}

		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
			showErrorMessage(e.getMessage());
		}
	}

	/**
	 * 
	 * 功能： 核对数据事件响应 参数： 返回： 例外： 日期：(2002-04-18 10:43:46) 修改日期，修改人，修改原因，注释标志：
	 */
	public void onCheckData() {
		try {
			getDevInputCtrl().setBillVO(m_voBill);
			java.util.ArrayList alResult = getDevInputCtrl().onOpenFile(getDevInputCtrl().ACT_CHECK_ITEM);

			alResult.get(1);
		} catch (Exception e) {
			String sErrorMsg = null;
			sErrorMsg = e.getMessage();
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000044")/* @res "错误提示：" */
					+ sErrorMsg);
		}
	}
	
	
	/**
	 * 
	 * 功能： 导入数据事件响应 参数： 返回： 例外： 日期：(2002-04-18 10:43:46) 修改日期，修改人，修改原因，注释标志：
	 */
	
	
	public void onImportData() {
		try{
			// 过滤空行
			filterNullLine();
			// 处理行号
			int iRowCount = getBillCardPanel().getRowCount();
			if (iRowCount > 0 && m_voBill.getChildrenVO() != null) {
				if (getBillCardPanel().getBodyItem(m_sBillRowNo) != null) for (int i = 0; i < iRowCount; i++) {
					m_voBill.setItemValue(i, m_sBillRowNo, getBillCardPanel().getBodyValueAt(i, m_sBillRowNo));
				}
			}

			nc.ui.ic.pub.device.DevInputCtrl devInputCtrl = getDevInputCtrl();
			GeneralBillVO vonew = (GeneralBillVO) (getBillCardPanel().getBillValueVO(GeneralBillVO.class.getName(), GeneralBillHeaderVO.class.getName(), GeneralBillItemVO.class.getName()));
			devInputCtrl.setBillVOUI(vonew);

			devInputCtrl.setBillVO(m_voBill);
			devInputCtrl.setWarehouseidFieldName("cwarehouseid");
			devInputCtrl.setWarehouseNameFieldName("cwarehousename");

			java.util.ArrayList alResult = devInputCtrl.onOpenFile(DevInputCtrl.ACT_ADD_ITEM);
			if (alResult == null || alResult.size() == 0) {
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000046")/* @res "没有导入成功！" */);
				return;
			}

			String sAppendType = (String) alResult.get(0);

			nc.vo.pub.CircularlyAccessibleValueObject[] voaDi = (nc.vo.pub.CircularlyAccessibleValueObject[]) alResult.get(1);

			int iAppendType = Integer.parseInt(sAppendType);
			m_bIsImportData = false;
			if (iAppendType == DevInputCtrl.ACT_ADD_ITEM) {
				if (voaDi != null && voaDi.length > 0) {
					m_bIsImportData = true;
					// 用于是否在保存时校验导入数据的正确性
					String sWarehouseid = null;
					nc.ui.pub.beans.UIRefPane refpane = (UIRefPane) getBillCardPanel().getHeadItem("cwarehouseid").getComponent();

					if (refpane != null) sWarehouseid = refpane.getRefPK();
					// 同步vo.
					synVO(voaDi, sWarehouseid, m_alLocatorData);
					// 必须将货位信息同步到m_alLocatorData,否则单据保存时，getBillVOs方法会清掉m_voBill的货位信息
					GeneralBillItemVO[] voaItemBill = (GeneralBillItemVO[]) m_voBill.getChildrenVO();
					int len = voaItemBill.length;
					for (int i = 0; i < len; i++) {
						LocatorVO[] voLoc = voaItemBill[i].getLocator();
						m_alLocatorData.add(i, voLoc);
					}

				} else m_bIsImportData = false;
			} else {
				// 不用校验存货信息，不更新这些信息，只更新数量
				m_bIsImportData = false;
			}
			

		} catch (Exception e) {
			String sErrorMsg = null;
			sErrorMsg = e.getMessage();
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000044")/* @res "错误提示：" */
					+ sErrorMsg);
			m_bIsImportData = false;
		}
		
	}
	
	
	protected GeneralBillItemVO voCopyLine(int row) {
		// int[] row = getBillCardPanel().getBillTable().getSelectedRows();

		GeneralBillItemVO voaBillItem = new GeneralBillItemVO();
		GeneralBillItemVO uicopyvo = null;

		this.m_voBill.getItemVOs()[row]
				.setLocator((LocatorVO[]) m_alLocatorData.get(row));
		voaBillItem = (GeneralBillItemVO) m_voBill.getItemVOs()[row].clone();
		uicopyvo = (GeneralBillItemVO) getBillCardPanel().getBillModel()
				.getBodyValueRowVO(row, GeneralBillItemVO.class.getName());
		uicopyvo = (GeneralBillItemVO) uicopyvo.clone();

		String firstbillhid = uicopyvo.getCfirstbillhid();
		String firstbillbid = uicopyvo.getCfirstbillbid();
		String firstbillcode = uicopyvo.getVfirstbillcode();

		String[] keys = uicopyvo.getAttributeNames();
		SmartVOUtilExt.copyVOByVO(voaBillItem, keys, uicopyvo, keys);

		if (StringIsNullOrEmpty(voaBillItem.getCfirstbillbid())) {
			voaBillItem.setCfirstbillbid(firstbillbid);
		}
		if (StringIsNullOrEmpty(voaBillItem.getCfirstbillhid())) {
			voaBillItem.setCfirstbillhid(firstbillhid);
		}
		if (StringIsNullOrEmpty(voaBillItem.getVfirstbillcode())) {
			voaBillItem.setVfirstbillcode(firstbillcode);
		}

		// 清除货位、序列号，这些数据是不复制的,和 m_alLoctorData,m_alSerialData保持一致
		// ydy 2004-07-02 货位复制
		// m_voaBillItem[i].setLocator((LocatorVO[])m_alLocatorData.get(row[i]));
		voaBillItem.setSerial(null);
		voaBillItem.setAttributeValue("cparentid", null);
		voaBillItem.setAttributeValue("ncorrespondnum", null);
		voaBillItem.setAttributeValue("ncorrespondastnum", null);

		// 清除条码数据 add by hanwei 2004-04-07
		voaBillItem.setBarCodeVOs(null);
		voaBillItem.setAttributeValue(IItemKey.NBARCODENUM, new UFDouble(0.0));

		voaBillItem.setBarcodeClose(new nc.vo.pub.lang.UFBoolean('N'));

		voaBillItem.setAttributeValue(IItemKey.NKDNUM, null);
		return voaBillItem;
	}
	
	/**
	 * 创建者：张欣 功能：关联录入 参数： 返回： 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	protected void onJointAdd(ButtonObject bo) {
		// 当前是列表形式时，首先切换到表单形式
		if (BillMode.List == m_iCurPanel) onSwitch();
		nc.ui.pub.pf.PfUtilClient.retAddBtn(getButtonTree().getButton(ICButtonConst.BTN_ADD), m_sCorpID, m_sBillTypeCode, bo);
		showSelBizType(bo);
		// updateButtons();

		setButtons();

	}

	/*
	 * 卡片打印/预览
	 * 
	 * @param isPreview boolean true-打印预览；false-直接打印 * @author 邵兵 on Jun 15,
	 * 2005
	 */
	protected void printOnCard(GeneralBillVO voBill, boolean isPreview) {

		// 单据主表
		GeneralBillHeaderVO headerVO = voBill.getHeaderVO();
		String sBillID = headerVO.getPrimaryKey();

		// 构造PrintLogClient及设置PrintInfo.
		ScmPrintlogVO voSpl = new ScmPrintlogVO();
		voSpl.setCbillid(sBillID); // 单据主表的ID
		voSpl.setVbillcode(headerVO.getVbillcode());// 传入单据号，用于显示。
		voSpl.setCbilltypecode(headerVO.getCbilltypecode());
		voSpl.setCoperatorid(headerVO.getCoperatorid());
		voSpl.setIoperatetype(new Integer(PrintConst.PAT_OK));// 固定
		voSpl.setPk_corp(getCorpPrimaryKey());
		voSpl.setTs(headerVO.getTs());// 单据主表的TS

		SCMEnv.out("ts=========tata" + voSpl.getTs());
		nc.ui.scm.print.PrintLogClient plc = new nc.ui.scm.print.PrintLogClient();
		// 设置单据信息
		plc.setPrintInfo(voSpl);
		// 设置TS刷新监听.
		plc.addFreshTsListener(new FreshTsListener());
		// 设置打印监听
		getPrintEntry().setPrintListener(plc);

		plc.setPrintEntry(getPrintEntry());// 用于单打时
		// 设置单据信息
		plc.setPrintInfo(voSpl);

		// 向打印置入数据源，进行打印
		getDataSource().setVO(voBill);
		getPrintEntry().setDataSource(getDataSource());

		// 打印提示信息
		String sPrintMsg = null;
		// 执行打印
		if (isPreview) {
			getPrintEntry().preview();
			sPrintMsg = plc.getPrintResultMsg(true);
		} else {
			getPrintEntry().print();
			sPrintMsg = plc.getPrintResultMsg(false);
		}

		MessageDialog.showHintDlg(this, null, sPrintMsg);

	}

	/*
	 * 列表下打印 @author 邵兵 on Jun 15, 2005
	 */
	protected void printOnList(ArrayList alBill) throws InterruptedException {
		nc.ui.pub.print.PrintEntry pe = getPrintEntryNew();

		if (pe.selectTemplate() < 0) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000048")/*
																											* @res "请先定义打印模板。"
																											*/);
			return;
		}

		pe.beginBatchPrint();

		PrintDataInterface ds = null;
		GeneralBillVO voBill = null;
		// 单据主表
		GeneralBillHeaderVO headerVO = null;
		// nc.vo.scm.print.PrintResultVO printResultVO = null;

		nc.ui.scm.print.PrintLogClient plc = new nc.ui.scm.print.PrintLogClient();
		plc.setBatchPrint(true);// 设置是批打
		// 设置打印监听
		pe.setPrintListener(plc);
		plc.setPrintEntry(pe);
		plc.addFreshTsListener(new FreshTsListener());

		// 打印操作
		for (int i = 0; i < alBill.size(); i++) {
			voBill = (GeneralBillVO) alBill.get(i);
			headerVO = voBill.getHeaderVO();

			ScmPrintlogVO voSpl = new ScmPrintlogVO();
			voSpl = new ScmPrintlogVO();
			voSpl.setCbillid(headerVO.getPrimaryKey()); // 单据主表的ID
			voSpl.setVbillcode(headerVO.getVbillcode());// 传入单据号，用于显示。
			voSpl.setCbilltypecode(headerVO.getCbilltypecode());
			voSpl.setCoperatorid(headerVO.getCoperatorid());
			voSpl.setIoperatetype(new Integer(PrintConst.PAT_OK));// 固定
			voSpl.setPk_corp(getCorpPrimaryKey());
			voSpl.setTs(headerVO.getTs());// 单据主表的TS

			SCMEnv.out("ts=========tata" + voSpl.getTs());
			// 设置单据信息
			plc.setPrintInfo(voSpl);

			if (plc.check()) {// 检查通过才执行打印，有错误的话自动插入打印日志，这里不用处理。
				ds = getDataSourceNew();
				ds.setVO(voBill);
				pe.setDataSource(ds);

				// 常量定义在Setup（很小）中，在现场很容易定制它。
				// while (pe.dsCountInPool() > PrintConst.PL_MAX_TAST_NUM) {
				// Thread.currentThread().sleep(PrintConst.PL_SLEEP_TIME); //
				// 如果有PL_MAX_TAST_NUM个以上任务，就等待PL_SLEEP_TIME秒。
				// }
			}

		}
		pe.endBatchPrint();

		MessageDialog.showHintDlg(this, null, plc.getPrintResultMsg(false));

	}

	/**
	 * 创建者：仲瑞庆 功能：打印 参数： 返回： 例外： 日期：(2001-5-10 下午 4:16) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 修改说明：增加打印次数控制 修改者：邵兵 2005-01-12
	 */
	public void onPrint() {

		try {
			// 调出打印窗口
			// 依当前是列表还是表单而定打印内容
			if (m_iMode == BillMode.Browse && m_iCurPanel == BillMode.Card) { // 浏览

				/* 增加打印次数控制后的打印方案 */
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000047")/*
																												* @res "正在打印，请稍候..."
																												*/);
				// 准备数据：获得要打印的vo.
				GeneralBillVO voBill = null;

				if (m_iLastSelListHeadRow != -1 && null != m_alListData && m_alListData.size() != 0) {
					if (m_sLastKey != null && m_voBill != null) voBill = m_voBill;
					else voBill = (GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow);
					if (getBillCardPanel().getHeadItem("vcustname") != null) voBill.setHeaderValue("vcustname", getBillCardPanel().getHeadItem("vcustname").getValue());
				}

				if (voBill == null) {
					voBill = new GeneralBillVO();
				}
				if (voBill.getParentVO() == null) {
					voBill.setParentVO(new GeneralBillHeaderVO());
				}
				if ((voBill.getChildrenVO() == null) || (voBill.getChildrenVO().length == 0) || (voBill.getChildrenVO()[0] == null)) {
					GeneralBillItemVO[] ivo = new GeneralBillItemVO[1];
					ivo[0] = new GeneralBillItemVO();
					voBill.setChildrenVO(ivo);
				}

				if (getPrintEntry().selectTemplate() < 0) {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000048")/*
																													* @res
																													* "请先定义打印模板。"
																													*/);
					return;
				}

				// 卡片打印
				printOnCard(voBill, false);

				// showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
				// "SCMCOMMON", "UPPSCMCommon-000133")/* @res "就绪" */);

			} else if (m_iCurPanel == BillMode.List) { // 列表
				/* 增加打印次数控制前的打印方案 */
				if (null == m_alListData || m_alListData.size() == 0) {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000049")/*
																														*/);
					return;
				}

				// 得到要打印的列表vo,ArrayList.
				ArrayList alBill = getSelectedBills();
				// 置小数精度
				setScaleOfListData(alBill);
				if (alBill == null || alBill.size() <= 0 || alBill.get(0) == null) {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH003")/*
																									 * @res "请选择要处理的数据！"
																									 */);
					return;
				}

				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000047")/*
																												* @res "正在打印，请稍候..."
																												*/);

				// 列表下打印
				printOnList(alBill);

				// showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
				// "SCMCOMMON", "UPPSCMCommon-000133")/* @res "就绪" */);

			} else showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000052")/*
																												* @res
																												* "请注意：您只能在浏览状态下打印"
																												*/);

		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000061")/* @res "打印出错" */
					+ e.getMessage());
		}
	}

	/**
	 * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2002-10-23 9:07:19) 修改日期，修改人，修改原因，注释标志：
	 */
	private void onPrintLocSN(GeneralBillVO voBill) {
		// 打印数据来自m_voBill
		if (voBill == null || voBill.getParentVO() == null || voBill.getChildrenVO() == null) {
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000053")/* @res "没有数据！" */);
			return;
		}
		// 准备打印标题
		String title = nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000319")/* @res " 货位序列号分配明细表" */;
		GeneralBillHeaderVO voHead = (GeneralBillHeaderVO) voBill.getParentVO();
		// 准备表头字串
		StringBuffer headstr = new StringBuffer(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000320")/*
																																 * @res
																																 * "单据号："
																																 */);
		if (voHead.getVbillcode() != null) headstr.append(voHead.getVbillcode());
		else headstr.append("      ");

		headstr.append(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000321")/* @res "仓库：" */);
		if (voHead.getCwarehousename() != null) headstr.append(voHead.getCwarehousename());
		else headstr.append("       ");
		headstr.append(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000322")/* @res "日期：" */);
		if (voHead.getDbilldate() != null) headstr.append(voHead.getDbilldate().toString());
		else headstr.append("       ");

		// 准备表体列名数组
		String[][] colname = new String[1][11];
		int[] colwidth = new int[11];
		int[] alignflag = new int[11];
		String[] names = new String[] {
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC000-0001480")/* @res "存货编码" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC000-0001453")/* @res "存货名称" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC000-0000745")/* @res "单位" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC000-0003327")/* @res "自由项" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000182")/* @res "批次" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC000-0003971")/* @res "辅数量" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC000-0003938")/* @res "辅单位" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC000-0002161")/* @res "换算率" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC000-0002282")/* @res "数量" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC000-0003830")/* @res "货位" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC000-0001819")
		/* @res "序列号" */};
		for (int i = 0; i < 11; i++) {
			colname[0][i] = names[i];
			colwidth[i] = 60;
			// String
			alignflag[i] = 0;
			// decimal
			if (i == 5 || i == 8 || i == 7) alignflag[i] = 2;

		}

		// 准备表体数据数组

		Vector v = new Vector(); // 缓存要打印的数据
		Object[] data = null;
		// 设置数据精度by ZSS
		ArrayList alVO = new ArrayList();
		alVO.add(voBill);
		setScaleOfListData(alVO);
		voBill = (GeneralBillVO) alVO.get(0);
		//
		GeneralBillItemVO[] voItems = (GeneralBillItemVO[]) voBill.getChildrenVO();
		for (int i = 0; i < voItems.length; i++) {
			// 如果表体行的货位不为空。取得货位分配数组，每增加一个货位即增加一行，数量取自货位VO，序列号与货位pk相关

			if (voItems[i].getLocator() != null) {
				LocatorVO[] locs = voItems[i].getLocator();

				ScaleKey sk = new ScaleKey();
				sk.setNumKeys(new String[] {
						"ninspacenum", "noutspacenum"
				});
				sk.setAssistNumKeys(new String[] {
						"ninspaceassistnum", "noutspaceassistnum"
				});

				GenMethod.setScale(locs, sk, m_ScaleValue);

				for (int j = 0; j < locs.length; j++) {
					data = new Object[11];
					// 存货编码
					data[0] = voItems[i].getCinventorycode();
					data[1] = voItems[i].getInvname();
					data[2] = voItems[i].getMeasdocname();
					data[3] = voItems[i].getVfree0();
					data[4] = voItems[i].getVbatchcode();
					data[6] = voItems[i].getCastunitname();
					data[7] = voItems[i].getHsl();
					// 数量ninspacenum or noutspacenum
					if (locs[j].getNinspacenum() != null) {
						data[8] = locs[j].getNinspacenum();
						data[5] = locs[j].getNinspaceassistnum();
					} else {
						data[8] = locs[j].getNoutspacenum();
						data[5] = locs[j].getNoutspaceassistnum();
					}

					// 货位csname
					data[9] = locs[j].getVspacename();
					// 如果序列号不为空
					data[10] = getSNString(voItems[i].getSerial(), locs[j].getCspaceid());
					v.add(data);
				}
			}
			// 如果货位为空，
			else {
				data = new Object[11];
				// 存货编码
				data[0] = voItems[i].getCinventorycode();
				data[1] = voItems[i].getInvname();
				data[2] = voItems[i].getMeasdocname();
				data[3] = voItems[i].getVfree0();
				data[4] = voItems[i].getVbatchcode();
				data[6] = voItems[i].getCastunitname();
				data[7] = voItems[i].getHsl();
				// 数量取自表体行
				if (voItems[i].getNinnum() != null) {
					data[8] = voItems[i].getNinnum();
					data[5] = voItems[i].getNinassistnum();
				} else {
					data[8] = voItems[i].getNoutnum();
					data[5] = voItems[i].getNoutassistnum();
				}

				// 如果序列号不为空,序列号信息与表体行相关
				data[10] = getSNString(voItems[i].getSerial(), null);
				v.add(data);
			}

		}
		if (v.size() > 0) {
			// 表体数据（去掉隐藏列）
			Object[][] data1 = new Object[v.size()][11];

			for (int i = 0; i < v.size(); i++) {

				data1[i] = (Object[]) v.get(i);

			}

			java.awt.Font font = new java.awt.Font("dialog", java.awt.Font.BOLD, 18);
			java.awt.Font font1 = new java.awt.Font("dialog", java.awt.Font.PLAIN, 12);
			String topstr = nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000323")/* @res "公司：" */
					+ nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getUnitname() + headstr;

			String botstr = nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000325")/* @res "制表人：" */
					+ nc.ui.pub.ClientEnvironment.getInstance().getUser().getUserName() + nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000324")/* @res " 制表日期：" */
					+ nc.ui.pub.ClientEnvironment.getInstance().getDate();
			//
			nc.ui.pub.print.PrintDirectEntry print = new nc.ui.pub.print.PrintDirectEntry();

			print.setTitle(title);
			// 标题 可选
			print.setTitleFont(font);
			// 标题字体 可选
			print.setContentFont(font1);
			// 内容字体（表头、表格、表尾） 可选
			print.setTopStr(topstr);
			// 表头信息 可选
			// 页号
			print.setBottomStr(botstr);
			print.setPageNumDisp(true);
			print.setPageNumFont(font1);
			// 左右0 1 2
			print.setPageNumAlign(2);
			// 上下0 1 2
			print.setPageNumPos(2);
			print.setPageNumTotalDisp(true);
			// 固定表头
			print.setFixedRows(1);
			// 表尾信息 可选
			print.setColNames(colname);
			// 表格列名（二维数组形式）
			print.setData(data1);
			// 表格数据
			print.setColWidth(colwidth);
			// 表格列宽 可选
			print.setAlignFlag(alignflag);
			// 表格每列的对齐方式（0-左, 1-中, 2-右）可选

			print.preview();
			// 预览

		}
		// 提交打印
	}

	/**
	 * 创建者：王乃军 功能：查询单据的表体，并把结果置到arraylist 参数： int iaIndex[],单据在alAlldata中的索引。
	 * String saBillPK[]单据pk数组 返回： 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	protected void qryItems(int iaIndex[], String saBillPK[]) {
		if (iaIndex == null || saBillPK == null || iaIndex.length != saBillPK.length) {
			SCMEnv.out("param value ERR.");
			return;
		}
		try {
			QryConditionVO voCond = new QryConditionVO();

			// 使用公式查询时,仅查出相关列PK
			voCond.setIntParam(0, GeneralBillVO.QRY_ITEM_ONLY_PURE);

			voCond.setParam(0, saBillPK);
			// 启用进度条
			// getPrgBar(PB_QRY).start();
			// long lTime = System.currentTimeMillis();
			ArrayList alRetData = (ArrayList) GeneralBillHelper.queryBills(m_sBillTypeCode, voCond);
			if (alRetData == null || alRetData.size() == 0 || iaIndex.length != alRetData.size()) {
				SCMEnv.out("ret item value ERR.");
				return;
			}

			setAlistDataByFormula(-1, alRetData);
			SCMEnv.out("1存货公式解析成功！");

			// --------------------------------------------
			GeneralBillVO voBill = null;
			for (int i = 0; i < alRetData.size(); i++) {
				// index
				voBill = (GeneralBillVO) m_alListData.get(iaIndex[i]);
				// set value
				if (alRetData.get(i) != null && voBill != null) voBill.setChildrenVO(((GeneralBillVO) alRetData.get(i)).getChildrenVO());

			}
		} catch (Exception e) {
			showErrorMessage(e.getMessage());
		}
	}

	/**
	 * 创建者：王乃军 功能：列表形式下打印前， 需要的化读剩余的单据表体。 参数： 返回： 例外： 日期：(2001-11-23 18:11:18)
	 * 修改日期，修改人，修改原因，注释标志：
	 */
	protected void queryLeftItem(ArrayList alListData) throws Exception {
		// -------------
		if (alListData == null || alListData.size() == 0) return;
		int iIntegralBillNum = 0; // 完整单据的数量
		GeneralBillVO voBill = null;
		GeneralBillItemVO[] voaItem = null;
		for (int bill = 0; bill < alListData.size(); bill++)
			if (alListData.get(bill) != null) {
				voBill = (GeneralBillVO) m_alListData.get(bill);
				voaItem = voBill.getItemVOs();
				if (voaItem != null && voaItem.length > 0) iIntegralBillNum++;
			}
		// 还有没有表体的单据
		if (iIntegralBillNum < alListData.size()) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000054")/*
																											* @res "正在准备打印数据，请稍候..."
																											*/);
			if ((double) iIntegralBillNum / alListData.size() < 0.60) {
				// 如果大于阀值的单据未读出，干脆重新查询
				QryConditionVO voCond = (QryConditionVO) m_voLastQryCond.clone();
				// 查询完整单据
				voCond.setIntParam(0, GeneralBillVO.QRY_FULL_BILL);
				SCMEnv.out("重新查所有数据，准备打印。。。");
				m_alListData = (ArrayList) GeneralBillHelper.queryBills(m_sBillTypeCode, voCond);
			} else { // 否则只读剩下的表体即可。

				// 读单据pk，用于查询，不在前面的循环读。
				// be sure size >0
				int[] iaIndex = new int[alListData.size() - iIntegralBillNum];
				String[] saPk = new String[alListData.size() - iIntegralBillNum];
				int count = 0;
				for (int bill = 0; bill < alListData.size(); bill++)
					if (alListData.get(bill) != null) {
						voBill = (GeneralBillVO) m_alListData.get(bill);
						voaItem = voBill.getItemVOs();
						if (voaItem == null || voaItem.length == 0) {
							saPk[count] = voBill.getHeaderVO().getPrimaryKey();
							iaIndex[count] = bill;
							count++;
						}
					}
				qryItems(iaIndex, saPk);
			}

		}

	}

	/**
	 * 创建者：王乃军 功能：重新设置存货ID,带出存货其它数据，不清批次、自由项、数量等其它数据 参数：行号，存货ID 返回： 例外：
	 * 日期：(2001-5-8 19:08:05) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * (1)获得表体的行billItemVO (2)解析表体的行 (3)给界面赋予表体行 (4)特殊控件处理（是不是可以不做处理？）
	 * 
	 * 
	 */
	public void resetAllInvInfo(GeneralBillVO voBill) {
		try {
			long lTime = System.currentTimeMillis();
			if (voBill == null || voBill.getItemVOs() == null || voBill.getItemVOs().length == 0) {
				SCMEnv.out("---- no item ");
				return;
			}

			getBillCardPanel().getBillModel().setNeedCalculate(false);

			GeneralBillItemVO[] voaItem = voBill.getItemVOs();

			// 重设每一行的存货ID,辅计量
			ArrayList alBillItems = new ArrayList();
			for (int i = 0; i < voBill.getItemVOs().length; i++) {
				alBillItems.add(voaItem[i]);
			}
			SCMEnv.showTime(lTime, "resetAllInvInfo:getItems");
			lTime = System.currentTimeMillis();
			// 读存货数据条件
			getFormulaBillContainer().formulaBodys(getFormulaItemBody(), alBillItems);

			// 计划价格查询 add by hanwei 2004-6-30
			if (alBillItems != null && voaItem.length > 0) {
				int iLen = voaItem.length;
				InvVO[] invVOs = new InvVO[iLen];
				ArrayList<InvVO> astsoilvos = new ArrayList<InvVO>(invVOs.length);
				for (int i = 0; i < voaItem.length; i++) {
					// 获得GeneralBillItemVO的invVO对象
					invVOs[i] = voaItem[i].getInv();
					if (invVOs[i] != null && invVOs[i].getIsAstUOMmgt() != null && invVOs[i].getIsAstUOMmgt().intValue() == 1 && invVOs[i].getIsSolidConvRate() != null && invVOs[i].getIsSolidConvRate().intValue() == 1) astsoilvos.add(invVOs[i]);
				}

				long ITime = System.currentTimeMillis();
				String sCalID = null;
				String sWhID = null;

				if (voBill.getHeaderValue(m_sMainCalbodyItemKey) != null) sCalID = (String) voBill.getHeaderValue(m_sMainCalbodyItemKey);

				if (voBill.getHeaderValue(m_sMainWhItemKey) != null) sWhID = (String) voBill.getHeaderValue(m_sMainWhItemKey);

				// DUYONG 此处需要同时传递库存组织和仓库的ID（为了从成本域库存组织中读取计划价格）
				getInvoInfoBYFormula().getProductPrice(invVOs, sCalID, sWhID);
				if (astsoilvos.size() > 0 && voaItem[0].getCsourcetype() != null && voaItem[0].getCsourcetype().trim().startsWith("2")) getInvoInfoBYFormula().getInVoOfHSLByHashCach(astsoilvos.toArray(new InvVO[astsoilvos.size()]));
				SCMEnv.showTime(ITime, "getProductPrice:" + invVOs.length + "条：");
				for (int i = 0; i < voaItem.length; i++) {
					// 获得GeneralBillItemVO的invVO对象
					voaItem[i].setInv(invVOs[i]);
				}
			}

			SCMEnv.showTime(lTime, "resetAllInvInfo:formulaBodys");

			lTime = System.currentTimeMillis();
			GeneralBillItemVO[] voBillItems = new GeneralBillItemVO[alBillItems.size()];
			alBillItems.toArray(voBillItems);

			// voBill.calConvRate(); //计算换算率

			SCMEnv.showTime(lTime, "resetAllInvInfo:hsl");
			// 设置界面数据
			// 修改 by hanwei 2003-11-18 hw
			lTime = System.currentTimeMillis();
			// 处理换算率
			for (int i = 0; i < voBillItems.length; i++) {
				// Object oBatchcode = voBillItems[i].getVbatchcode();
				InvVO voInv = voBillItems[i].getInv();
				if (voInv != null && voInv.getIsAstUOMmgt().intValue() != 1) {
					voBillItems[i].setHsl(null);
					voBillItems[i].setNshouldoutassistnum(null);
					voBillItems[i].setNneedinassistnum(null);
					voBillItems[i].setNinassistnum(null);
					voBillItems[i].setNoutassistnum(null);

				} else {
					if (voInv != null && voInv.getIsSolidConvRate() != null && voInv.getIsSolidConvRate().intValue() == 1 && voInv.getHsl() != null && (voBillItems[i].getHsl() == null || voInv.getHsl().compareTo(voBillItems[i].getHsl()) != 0)) {
						voBillItems[i].setHsl(voInv.getHsl());
					}
				}

			}

			setBillVO(voBill, false, false);
			SCMEnv.showTime(lTime, "resetAllInvInfo:setBillVO");

			lTime = System.currentTimeMillis();
			for (int i = 0; i < voBillItems.length; i++) {
				// Object oBatchcode = voBillItems[i].getVbatchcode();
				InvVO voInv = voBillItems[i].getInv();
				if (voInv != null && voInv.getIsAstUOMmgt().intValue() != 1) {
					voBillItems[i].setHsl(null);
					voBillItems[i].setNshouldoutassistnum(null);
					voBillItems[i].setNneedinassistnum(null);
					voBillItems[i].setNinassistnum(null);
					voBillItems[i].setNoutassistnum(null);

				}

			}
			SCMEnv.showTime(lTime, "批次号加载数据时间");

			getBillCardPanel().getBillModel().setNeedCalculate(true);
			getBillCardPanel().getBillModel().reCalcurateAll();
		} catch (Exception e2) {
			nc.vo.scm.pub.SCMEnv.error(e2);
		}
	}

	/**
	 * 创建者：王乃军 功能：根据设定的按钮初始化菜单。 参数： 返回： 例外： 日期：(2001-11-23 18:11:18)
	 * 修改日期，修改人，修改原因，注释标志：
	 */
	protected void resetButtons() {
		try {
			initButtonsData();
			initDevInputButtons();
			setButtons();
		} catch (Exception e) {
			handleException(e);
		}

	}

	/**
	 * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2001-11-21 14:47:40) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @param num
	 *            nc.vo.pub.lang.UFDouble
	 * @param assistnum
	 *            nc.vo.pub.lang.UFDouble
	 * @param row
	 *            int
	 */
	private void resetSpace(int row) {
		UFDouble assistnum = null;
		try {
			assistnum = (UFDouble) getBillCardPanel().getBodyValueAt(row, m_sAstItemKey);
		} catch (Exception e) {
		}
		UFDouble num = null;
		try {
			num = (UFDouble) getBillCardPanel().getBodyValueAt(row, m_sNumItemKey);
		} catch (Exception e) {
		}
		UFDouble ngrossnum = null;
		try {
			ngrossnum = (UFDouble) getBillCardPanel().getBodyValueAt(row, m_sNgrossnum);
		} catch (Exception e) {
		}

		LocatorVO[] voLoc = (LocatorVO[]) m_alLocatorData.get(row);

		if (voLoc != null && voLoc.length == 1) {

			// 辅数量
			if (assistnum == null) {
				voLoc[0].setNinspaceassistnum(assistnum);
				voLoc[0].setNoutspaceassistnum(assistnum);
			} else {
				if (m_iBillInOutFlag > 0) {
					voLoc[0].setNinspaceassistnum(assistnum);
					voLoc[0].setNoutspaceassistnum(null);
				} else {
					voLoc[0].setNinspaceassistnum(null);
					voLoc[0].setNoutspaceassistnum(assistnum);
				}
			}
			// 数量
			if (num == null) {
				voLoc[0].setNinspacenum(num);
				voLoc[0].setNoutspacenum(num);
				if (m_alSerialData != null) m_alSerialData.set(row, null);
			} else {
				if (m_iBillInOutFlag > 0) {
					// 入库
					voLoc[0].setNoutspacenum(null);
					voLoc[0].setNinspacenum(num);
				} else {
					// 出库
					voLoc[0].setNinspacenum(null);
					voLoc[0].setNoutspacenum(num);
					if (m_alSerialData != null) m_alSerialData.set(row, null);
				}
			}
			// 毛重
			if (ngrossnum == null) {
				voLoc[0].setNingrossnum(ngrossnum);
				voLoc[0].setNoutgrossnum(ngrossnum);
				if (m_alSerialData != null) m_alSerialData.set(row, null);
			} else {
				if (m_iBillInOutFlag > 0) {
					// 入库
					voLoc[0].setNoutgrossnum(null);
					voLoc[0].setNingrossnum(ngrossnum);
				} else {
					// 出库
					voLoc[0].setNingrossnum(null);
					voLoc[0].setNoutgrossnum(ngrossnum);

				}

			}

		} 
		else if (voLoc != null && voLoc.length == 2&& m_sBillTypeCode.equals("4Q")) {//货位调整不进行货位清空

			UFDouble dTempNum = null;
			UFDouble dTempAstNum = null;
			UFDouble dTempGrossNum = null;
			Object oTempValue = getBillCardPanel().getBodyValueAt(row, "noutnum");
			if ((oTempValue != null) && (oTempValue.toString().trim().length() > 0)) dTempNum = new UFDouble(oTempValue.toString());
			oTempValue = getBillCardPanel().getBodyValueAt(row, "noutassistnum");
			if ((oTempValue != null) && (oTempValue.toString().trim().length() > 0)) dTempAstNum = new UFDouble(oTempValue.toString());
			oTempValue = getBillCardPanel().getBodyValueAt(row, "noutgrossnum");
			if ((oTempValue != null) && (oTempValue.toString().trim().length() > 0)) {
				dTempGrossNum = new UFDouble(oTempValue.toString());
			}

			voLoc[1].setNinspaceassistnum(dTempAstNum);
			voLoc[0].setNoutspaceassistnum(dTempAstNum);
			voLoc[1].setNinspacenum(dTempNum);
			voLoc[0].setNoutspacenum(dTempNum);
			voLoc[1].setNingrossnum(dTempGrossNum);
			voLoc[0].setNoutgrossnum(dTempGrossNum);
			if (m_alSerialData != null) m_alSerialData.set(row, null);
			
		}else{
			m_alLocatorData.set(row, null);
		}

	}

	/**
	 * 创建者：王乃军 功能：读仓库属性数据，放到m_voBill 参数： 返回： 例外： 日期：(2001-5-8 19:08:05)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	public void resetWhInfo(GeneralBillVO voBill) {
		try {
			if (voBill == null || voBill.getHeaderValue(m_sMainWhItemKey) == null) {
				SCMEnv.out("-->W:no wh");
				return;
			} // 查询需要仓库ID
			String sWhID = voBill.getHeaderValue(m_sMainWhItemKey).toString().trim();
			// 读仓库数据
			// 查，返回WhVO
			WhVO voWh = (WhVO) GeneralBillHelper.queryInfo(new Integer(QryInfoConst.WH), sWhID);

			if (m_voBill != null) { // 设置仓库属性
				m_voBill.setWh(voWh);
				voBill.setWh(voWh);// v5
				// 设置货位按钮可用性
				setBtnStatusSpace(false);
			}

		} catch (Exception e2) {
			nc.vo.scm.pub.SCMEnv.error(e2);
			showErrorMessage(e2.getMessage());
		}
	}

	/**
	 * 保存前对单据设置某些固定值。
	 * 
	 * @since v5
	 * @author ljun 只被委外加工入库单重载，设置集采购默认值，便于采购结算
	 */
	// v5
	protected void beforeSave(GeneralBillVO voBill) {

	}

	/**
	 * 创建者：王乃军 功能：保存新增的单据 有错误需要以异常抛出，影响主流程
	 * 
	 * 参数：完整单据 返回： 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 */
	protected void saveNewBill(GeneralBillVO voNewBill) throws Exception {

		try {
			// 得到数据错误，出错 ------------ EXIT -------------------
			nc.vo.ic.pub.bill.Timer timer = new nc.vo.ic.pub.bill.Timer();
			timer.start("@@saveNewBill开始");
			if (voNewBill == null || voNewBill.getParentVO() == null || voNewBill.getChildrenVO() == null) {
				SCMEnv.out("Bill is null !");
				throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000055")/*
																													* @res
																													* "单据为空！"
																													*/);
			}
			// 执行保存...
			// 通过平台实现保存即签字，置入签字人字段，在出入库单保存时清除之。
			// 支持平台，clone一个，以便于以后的处理，同时防止修改了m_voBill
			GeneralBillVO voTempBill = (GeneralBillVO) voNewBill.clone();
			GeneralBillHeaderVO voHead = voTempBill.getHeaderVO();
			voHead.setPk_corp(m_sCorpID);

			// voHead.setDaccountdate(new nc.vo.pub.lang.UFDate(m_sLogDate));
			// 新增的单据清除PK
			voHead.setPrimaryKey(null);
			// 表体PK
			GeneralBillItemVO[] voaItem = voTempBill.getItemVOs();
			for (int row = 0; row < voaItem.length; row++)
				voaItem[row].setPrimaryKey(null);

			// 如果不保存条码清空条码
			nc.vo.ic.pub.barcodeparse.BarcodeparseCtrl.beforSaveBillVOBarcode(m_bBarcodeSave, voNewBill);

			voHead.setCoperatoridnow(m_sUserID); // 当前操作员2002-04-10.wnj
			voHead.setAttributeValue("clogdatenow", m_sLogDate); // 当前登录日期2003-01-05

			voTempBill.setParentVO(voHead);
			voTempBill.setChildrenVO(voaItem);
			timer.showExecuteTime("@@设置表头和表体：");
			// --------- save -------------
			// 加入单据号,如果单据号==sCorpID，将单据号置空，后台会自动获取。
			if (m_sCorpID.equals(voHead.getVbillcode())) {
				voHead.setVbillcode(null);
			}
			ArrayList alPK = null;

			// 保存校验和日志 add by hanwei 2004-04-01
			voTempBill.setAccreditUserID(voNewBill.getAccreditUserID());
			voTempBill.setOperatelogVO(voNewBill.getOperatelogVO());
			// IP地址传入
			// 根据参数是否保存条码，清空条码

			// 是否保存条码
			voTempBill.setSaveBadBarcode(m_bBadBarcodeSave);
			// 是否保存数量不一致的条码
			voTempBill.setSaveBarcode(m_bBarcodeSave);
			// 帐期、信用信息
			voTempBill.m_iActionInt = nc.vo.scm.pub.bill.CreditConst.ICREDIT_ACT_ADD;
			voTempBill.m_sActionCode = "SAVEBASE";

			beforeSave(voTempBill);
			alPK = (ArrayList) nc.ui.pub.pf.PfUtilClient.processAction("SAVE", m_sBillTypeCode, m_sLogDate, voTempBill);
			SCMEnv.out("ret..");
			timer.showExecuteTime("@@走平台保存时间：");

			// [[提示信息][PK_Head,PK_body1,PK_body2,....]]
			// 保存当前单据的OID
			// 返回数据错误 ------------------- EXIT --------------------------
			if (alPK == null || alPK.size() < 3 || alPK.get(1) == null || alPK.get(2) == null) {
				SCMEnv.out("return data error. al pk" + alPK);
				showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000056")/*
																												* @res
																												* "单据已经保存，但返回值错误，请重新查询单据。"
																												*/);
			} else {

				// 显示提示信息
				if (alPK.get(0) != null && alPK.get(0).toString().trim().length() > 0) showWarningMessage((String) alPK.get(0));

				// 行数
				int iRowCount = voNewBill.getItemCount();
				ArrayList alMyPK = (ArrayList) alPK.get(1);
				if (alMyPK == null || alMyPK.size() < (iRowCount + 1) || alMyPK.get(0) == null || alMyPK.get(1) == null) {
					SCMEnv.out("return data error. my pk " + alMyPK);
					showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000057")/*
																													* @res
																													* "保存返回值错误！"
																													*/);
				} else {
					// 表头的OID
					m_sCurBillOID = (String) alMyPK.get(0);
					// 设置界面的单据ID
					if (getBillCardPanel().getHeadItem(m_sBillPkItemKey) != null) getBillCardPanel().setHeadItem(m_sBillPkItemKey, m_sCurBillOID);
					//
					voNewBill.getParentVO().setPrimaryKey(m_sCurBillOID);
					// 表体的OID
					// ###################################################
					// 重新设置单据vo条码VOPK、表头ts,billcode,表体
					// 表头：cgeneralhid,fbillfalg,vbillcode,ts
					// 表体：cgeneralbid,crowno,vbatchcode,vfirstbillcode,ninnum,ninassistnum,noutnum,noutassistnum,ts
					SMGeneralBillVO smbillvo = null;
					smbillvo = (SMGeneralBillVO) alPK.get(2);
					m_sBillStatus = (smbillvo.getHeaderVO().getFbillflag()).toString();

					getGenBillUICtl().refreshSaveData(smbillvo, getBillCardPanel(), voNewBill, m_voBill);
					getGenBillUICtl().refreshLocFromSMVO(smbillvo, getBillCardPanel(), m_alLocatorData, m_voBill);
					getGenBillUICtl().refreshBatchcodeAfterSave(getBillCardPanel(), m_voBill);

					timer.showExecuteTime("@@设置表头和表体PK时间：");
					// 回写到m_alListData
					if (!m_bRefBills) insertBillToList(m_voBill);

					timer.showExecuteTime("@@insertBillToList(m_voBill)：");
				}
			}
			SCMEnv.out("insertok..");

			// v5 lj 支持参照生成多张单据
			if (m_bRefBills == true) {
				// removeBillsOfList(new int[] { m_iLastSelListHeadRow });

				setButtonStatus(false);
				ctrlSourceBillUI(true);
			}

		} catch (Exception e) {
			// 异常必须抛出，由主流程处理。因为它影响主流程。
			// ###################################################
			// 新增保存异常不记录后台日志 add by hanwei 2004-6-8
			// ###################################################

			throw e;

		}
	}

	/**
	 * 创建者：王乃军 功能：保存修改的单据
	 * 
	 * 有错误需要以异常抛出，影响主流程
	 * 
	 * 参数： 返回： 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	protected void saveUpdatedBill(GeneralBillVO voUpdatedBill) throws Exception {
		try {
			// 得到数据错误，出错 ------------ EXIT -------------------
			nc.vo.ic.pub.bill.Timer timer = new nc.vo.ic.pub.bill.Timer();
			timer.start("@@修改保存单据开始：");
			if (voUpdatedBill == null || voUpdatedBill.getParentVO() == null || voUpdatedBill.getChildrenVO() == null) {
				SCMEnv.out("Bill is null !");
				throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000055")/*
																													* @res
																													* "单据为空！"
																													*/);
			}

			// 如果不保存条码清空条码
			nc.vo.ic.pub.barcodeparse.BarcodeparseCtrl.beforSaveBillVOBarcode(m_bBarcodeSave, voUpdatedBill);

			// 设置单据类型
			voUpdatedBill.setHeaderValue("cbilltypecode", m_sBillTypeCode);
			// 05/07设置制单人为当前操作员
			// remark by zhx onSave() set coperatorid into VO
			// voUpdatedBill.setHeaderValue("coperatorid", m_sUserID);
			timer.showExecuteTime("@@设置单据类型：");
			GeneralBillVO voBill = (GeneralBillVO) m_voBill.clone();
			timer.showExecuteTime("@@m_voBill.clone()：");
			voBill.setIDItems(voUpdatedBill);
			int iDelRowCount = voUpdatedBill.getItemCount() - voBill.getItemCount();
			if (iDelRowCount > 0) {
				// 如果有，追加删除行
				GeneralBillItemVO voaItems[] = new GeneralBillItemVO[voUpdatedBill.getItemCount()];
				// 原行
				for (int org = 0; org < voBill.getItemCount(); org++)
					voaItems[org] = voBill.getItemVOs()[org];

				// 附上删除行
				for (int del = 0; del < iDelRowCount; del++)
					voaItems[voBill.getItemCount() + del] = voUpdatedBill.getItemVOs()[voBill.getItemCount() + del];

				voBill.setChildrenVO(voaItems);
				timer.showExecuteTime("@@voBill.setChildrenVO(voaItems)：");
			}
			GeneralBillHeaderVO voHead = voBill.getHeaderVO();
			// --------------------------------------------可以不是当前登录单位的单据，所以不需要修改单位。
			voHead.setPk_corp(m_sCorpID);
			// 因为登录日期和单据日期是可以不同的，所以必须要登录日期。
			// voHead.setDaccountdate(new nc.vo.pub.lang.UFDate(m_sLogDate));
			// vo可能要传给平台，所以要做成和签字后的单据
			voHead.setCoperatoridnow(m_sUserID); // 当前操作员2002-04-10.wnj
			voHead.setAttributeValue("clogdatenow", m_sLogDate); // 当前登录日期2003-01-05

			// update by zhx on 0926 增加根据库存参数IC060 判断是否保留最初的制单人。
			// IC060 ＝‘N’ 不保留，则修改单据时重置制单人。否则，不重置。
			// if (m_sRemainOperator != null
			// && m_sRemainOperator.equalsIgnoreCase("N"))
			// voHead.setCoperatorid(m_sUserID);

			// 修改前的单据
			GeneralBillVO voOriginalBill = null;
			// 来自单据列表
			if (m_iLastSelListHeadRow >= 0 && m_alListData != null && m_alListData.size() > m_iLastSelListHeadRow && m_alListData.get(m_iLastSelListHeadRow) != null) {
				voOriginalBill = (GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow);
				voOriginalBill.getHeaderVO().setCoperatoridnow(m_sUserID);
				voOriginalBill.getHeaderVO().setPk_corp(m_sCorpID);
			} else {
				SCMEnv.out("original null,maybe error.");
				throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000058")/*
																													* @res
																													* "未找到原始单据，请查询后重试。"
																													*/);
			}
			timer.showExecuteTime("@@设置表头数据：");
			// 2003-06-13.02 wnj:设置货位、序列号修改状态。
			voBill.setLocStatus(voOriginalBill);
			timer.showExecuteTime("@@设置货位、序列号修改状态：");
			// ----
			// add by hanwei 2004-04
			voBill.setAccreditUserID(voUpdatedBill.getAccreditUserID());
			voBill.setOperatelogVO(voUpdatedBill.getOperatelogVO());

			// 是否保存条码
			voBill.setSaveBadBarcode(m_bBadBarcodeSave);
			// 是否保存数量不一致的条码
			voBill.setSaveBarcode(m_bBarcodeSave);
			// 帐期、信用信息
			voBill.m_iActionInt = nc.vo.scm.pub.bill.CreditConst.ICREDIT_ACT_MODIFY;
			voBill.m_sActionCode = "SAVEBASE";
			voBill.m_voOld = voOriginalBill;
			ArrayList alRetData = (ArrayList) nc.ui.pub.pf.PfUtilClient.processAction("SAVE", m_sBillTypeCode, m_sLogDate, voBill, voOriginalBill);
			timer.showExecuteTime("@@走平台保存：：");
			// new GeneralBillVO[]{m_voBill});
			// [[提示信息][new_PK_body1,new_PK_body2,....]]
			// 保存当前单据的OID
			// 返回数据错误 ------------------- EXIT --------------------------
			if (alRetData == null || alRetData.size() < 3 || alRetData.get(1) == null || alRetData.get(2) == null) {
				SCMEnv.out("return data error.");
				showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000056")/*
																												* @res
																												* "单据已经保存，但返回值错误，请重新查询单据。"
																												*/);
			} else {
				// 0 ---- 显示提示信息
				if (alRetData.get(0) != null && alRetData.get(0).toString().trim().length() > 0) showWarningMessage((String) alRetData.get(0));
				// 1 ---- 返回的PK
				ArrayList alMyPK = (ArrayList) alRetData.get(1);

				// 设置新增行的PK
				setBodyPkAfterUpdate(alMyPK);
				timer.showExecuteTime("@@设置新增行的PK：：");
				// 加载公式
				// getBillCardPanel().getBillModel().execLoadFormula();
				// 重新获得vo，以刷新列表形式。
				// 上面读出的vo带有删除列，修改列只有id,和界面上的数据不是一一对应的。
				// 所以要重新读一次。

				// necessary！//刷新单据状态

				// 05/07设置界面制单人为当前操作员
				// update by zhx on 0926 增加根据库存参数IC060 判断是否保留最初的制单人。
				// IC060 ＝‘N’ 不保留，则修改单据时重置制单人。否则，不重置。
				// if (m_sRemainOperator != null
				// && m_sRemainOperator.equalsIgnoreCase("N")) {
				// if (getBillCardPanel().getTailItem("coperatorid") != null)
				// getBillCardPanel()
				// .setTailItem("coperatorid", m_sUserID);
				// if (getBillCardPanel().getTailItem("coperatorname") != null)
				// getBillCardPanel().setTailItem("coperatorname",
				// m_sUserName);
				// }

				timer.showExecuteTime("@@getBillCardPanel().updateValue()：：");
				voUpdatedBill = getBillVO();
				// 设置单据类型
				voUpdatedBill.setHeaderValue("cbilltypecode", m_sBillTypeCode);

				// ###################################################
				// 重新设置单据vo条码VOPK、表头ts,billcode,表体
				// 表头：cgeneralhid,fbillfalg,vbillcode,ts
				// 表体：cgeneralbid,crowno,vbatchcode,vfirstbillcode,ninnum,ninassistnum,noutnum,noutassistnum,ts
				SMGeneralBillVO smbillvo = null;
				smbillvo = (SMGeneralBillVO) alRetData.get(2);
				m_sBillStatus = (smbillvo.getHeaderVO().getFbillflag()).toString();
				getGenBillUICtl().refreshSaveData(smbillvo, getBillCardPanel(), voUpdatedBill, m_voBill);
				getGenBillUICtl().refreshLocFromSMVO(smbillvo, getBillCardPanel(), m_alLocatorData, m_voBill);
				getGenBillUICtl().refreshBatchcodeAfterSave(getBillCardPanel(), m_voBill);

				// 添加by hanwei 2004-9-23
				setBillBCVOStatus(m_voBill, nc.vo.pub.VOStatus.UNCHANGED);
				// ###################################################

				getBillCardPanel().updateValue();

				timer.showExecuteTime("@@m_voBill.setIDItems(voUpdatedBill)：");
				// 刷新列表数据
				updateBillToList(m_voBill);
				timer.showExecuteTime("@@刷新列表数据updateBillToList(m_voBill)：");
			}

		} catch (Exception e) {
			// 异常必须抛出，由主流程处理。因为它影响主流程。
			throw e;

		}
	}

	/**
	 * 创建者：王乃军 功能：在列表方式下选择一张单据 参数： 单据在alListData中的索引 返回：无 例外： 日期：(2001-11-23
	 * 18:11:18) 修改日期，修改人，修改原因，注释标志：
	 */
	abstract protected void selectBillOnListPanel(int iBillIndex);

	/**
	 * 创建日期：(2003-3-6 11:33:06) 作者：韩卫 修改日期： 修改人： 修改原因： 方法说明：
	 * 取得单据查询数据，把指定行数范围的单据的表体 用存货公式解析，解析存货有关属性数据
	 * 
	 * @param iTopnum
	 *            int
	 * @param lListData
	 *            java.util.ArrayList
	 */
	public void setAlistDataByFormula(int iTopnum, ArrayList lListData) {
		if (lListData == null || lListData.size() == 0) return;
		// 需要存货公式解析的单据数目
		int iFormulaNum = 0;
		// 如果iTopnum=-1 表示取所有的单据
		// 如果lListData.size
		// 少于 iTopnum 以lListData.size为标准
		if (lListData.size() < iTopnum || iTopnum < 0) iFormulaNum = lListData.size();
		else iFormulaNum = iTopnum;

		GeneralBillVO billVo = null;
		nc.vo.ic.pub.bill.GeneralBillItemVO[] itemVos = null;
		java.util.ArrayList alItemVos = new ArrayList();
		int iItemLen = 0;
		for (int i = 0; i < iFormulaNum; i++) {
			billVo = (GeneralBillVO) lListData.get(i);
			itemVos = (GeneralBillItemVO[]) billVo.getChildrenVO();
			if (itemVos != null && itemVos.length > 0) {
				iItemLen = itemVos.length;
				for (int j = 0; j < iItemLen; j++) {
					// 获得item
					alItemVos.add(itemVos[j]);
				}
			}

		}

		if (alItemVos != null && alItemVos.size() > 0) {
			// 通过单据公式容器类执行有关公式解析的方法
			getFormulaBillContainer().formulaBodys(getFormulaItemBody(), alItemVos);

		}

	}

	/**
	 * 创建者：王乃军 功能：设置单据出入库属性 参数： 返回： 例外： 日期：(2001-5-9 9:23:32)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	public void setBillInOutFlag(int iInOutFlag) {
		// 入库
		if (iInOutFlag == InOutFlag.IN) {
			// 表体数量字段名
			m_sNumItemKey = "ninnum";
			// 表体辅数量字段名
			m_sAstItemKey = "ninassistnum";
			// 表体应辅数量字段名
			m_sShouldAstItemKey = "nneedinassistnum";
			// 表体应数量字段名
			m_sShouldNumItemKey = "nshouldinnum";
			//
			m_sNgrossnum = "ningrossnum";
			m_iBillInOutFlag = iInOutFlag;
		} else // 出库
		if (iInOutFlag == InOutFlag.OUT) {
			// 表体实际出库数量字段名
			m_sNumItemKey = "noutnum";
			// 表体实际出库辅数量字段名
			m_sAstItemKey = "noutassistnum";
			// 表体应发辅数量字段名
			m_sShouldAstItemKey = "nshouldoutassistnum";
			// 表体应发数量字段名
			m_sShouldNumItemKey = "nshouldoutnum";
			m_sNgrossnum = "noutgrossnum";
			m_iBillInOutFlag = iInOutFlag;
		} else showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000059")/* @res "单据类型设置错误！" */);
	}

	/**
	 * 创建者：王乃军 功能： 参数： 返回： 例外： 日期：(2001-11-23 18:05:45) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @param newM_sBillTypeCode
	 *            java.lang.String
	 */
	public void setBillTypeCode(java.lang.String newM_sBillTypeCode) {
		m_sBillTypeCode = newM_sBillTypeCode;
	}

	/**
	 * 类型说明： 创建日期：(2002-12-23 11:59:38) 作者：王乃军 修改日期： 修改人： 修改原因： 算法说明：
	 * 
	 * @param row
	 *            int
	 * 
	 */
	private void setBodySpace(int row) {

		if (getBillCardPanel().getBodyItem("vspacename") == null) return;
		getBillCardPanel().setBodyValueAt(null, row, "vspacename");
		getBillCardPanel().setBodyValueAt(null, row, "cspaceid");

		// 写界面货位
		if (row < 0 || m_alLocatorData == null || m_alLocatorData.size() < row + 1) return;

		LocatorVO[] voaLoc = (LocatorVO[]) m_alLocatorData.get(row);

		if (voaLoc != null && voaLoc.length == 1 && voaLoc[0] != null) {
			getBillCardPanel().setBodyValueAt(voaLoc[0].getVspacename(), row, "vspacename");
			getBillCardPanel().setBodyValueAt(voaLoc[0].getCspaceid(), row, "cspaceid");
		}
	}

	/**
	 * 创建者：王乃军 功能：根据设定的按钮初始化菜单。 参数： 返回： 例外： 日期：(2001-11-23 18:11:18)
	 * 修改日期，修改人，修改原因，注释标志：
	 */
	protected void setButtons() {
		setButtons(getButtonTree().getButtonArray());
		/**
		 * try { Vector vSubMenu = null; if (m_vTopMenu != null &&
		 * m_vTopMenu.size() > 0) { // 没有单据参照的话，新增在单据编辑菜单，否则在业务类型后。 if
		 * (!m_bNeedBillRef) { if (m_vBillMngMenu != null)
		 * m_vBillMngMenu.insertElementAt(m_boAdd, 1); } else {//
		 * 有单据参照的话，新增在业务类型后。 m_vTopMenu.insertElementAt(m_boAdd, 0);
		 * m_vTopMenu.insertElementAt(m_boJointAdd, 0); }
		 * 
		 * // 顶层菜单 m_boaButtonGroup = new ButtonObject[m_vTopMenu.size()]; //
		 * 子菜单 ButtonObject boSub = null; for (int i = 0; i < m_vTopMenu.size();
		 * i++) { if (m_vTopMenu.elementAt(i) instanceof Vector) { // 子菜单
		 * vSubMenu = (Vector) m_vTopMenu.elementAt(i); // 第0个是顶层菜单 boSub =
		 * (ButtonObject) vSubMenu.elementAt(0); for (int j = 1; j <
		 * vSubMenu.size(); j++) boSub.addChildButton((ButtonObject) vSubMenu
		 * .elementAt(j)); m_boaButtonGroup[i] = boSub; } else // 顶层菜单
		 * m_boaButtonGroup[i] = (ButtonObject) m_vTopMenu .elementAt(i); }
		 * setButtons(m_boaButtonGroup); } } catch (Exception e) {
		 * handleException(e); }
		 */
	}

	/**
	 * 创建者：王乃军 功能：抽象方法：设置按钮状态，在setButtonStatus中调用。 参数： 返回： 例外： 日期：(2001-5-9
	 * 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */

	abstract protected void setButtonsStatus(int iBillMode);

	/**
	 * 创建者：王乃军 功能：设置必输项颜色。 参数： 返回： 例外： 日期：(2001-11-23 18:11:18)
	 * 修改日期，修改人，修改原因，注释标志：
	 */
	protected void setColor() {
		try {
			// 更改背景色后的表体中Header着色器
			javax.swing.table.DefaultTableCellRenderer tcrold = (javax.swing.table.DefaultTableCellRenderer) getBillCardPanel().getBillTable().getColumnModel().getColumn(1).getHeaderRenderer();
			HeaderRenderer tcr = new HeaderRenderer(tcrold);

			// 分别得到表头和表体中需着重显示的字段
			ArrayList alHeaderColChangeColorString = GeneralMethod.getHeaderCanotNullString(getBillCardPanel());
			ArrayList alBodyColChangeColorString = GeneralMethod.getBodyCanotNullString(getBillCardPanel());

			// 修改表单中的表头颜色
			SetColor.SetBillCardHeaderColor(getBillCardPanel(), alHeaderColChangeColorString);
			// SetBillCardHeaderColor(alHeaderColChangeColorString);

			// 置入各个着色器于表格的Header中
			nc.ui.scm.ic.exp.SetColor.SetTableHeaderColor(getBillCardPanel().getBillModel(), getBillCardPanel().getBillTable(), alBodyColChangeColorString, tcr);
			SetColor.SetTableHeaderColor(getBillListPanel().getHeadBillModel(), getBillListPanel().getHeadTable(), alHeaderColChangeColorString, tcr);
			SetColor.SetTableHeaderColor(getBillListPanel().getBodyBillModel(), getBillListPanel().getBodyTable(), alBodyColChangeColorString, tcr);
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
		}
	}

	/**
	 * 创建者：王乃军 功能： 参数： 返回： 例外： 日期：(2001-11-23 18:05:45) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @param newM_sCurrentBillNode
	 *            java.lang.String
	 */
	public void setCurrentBillNode(java.lang.String newM_sCurrentBillNode) {
		m_sCurrentBillNode = newM_sCurrentBillNode;
	}

	/**
	 * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2002-07-24 15:31:38) 修改日期，修改人，修改原因，注释标志：
	 */
	void setDeliverAddressByHand() {
		if (getBillCardPanel().getHeadItem("vdiliveraddress") != null) {
			getBillCardPanel().getHeadItem("vdiliveraddress").setDataType(nc.ui.pub.bill.BillItem.USERDEF);
			((UIRefPane) getBillCardPanel().getHeadItem("vdiliveraddress").getComponent()).getUITextField().setText((String) m_voBill.getHeaderValue("vdiliveraddress"));
			// setValue(
			// m_voBill.getHeaderValue("vdiliveraddress"));
			// getBillCardPanel().setHeadItem(
			// "vdiliveraddress",
			// m_voBill.getHeaderValue("vdiliveraddress"));
			// (
			// (nc.ui.pub.beans.UIRefPane)
			// (getBillCardPanel().getHeadItem("vdiliveraddress").getComponent())).setValue(
			// (String) m_voBill.getHeaderValue("vdiliveraddress"));
			// Object o = getBillCardPanel().getHeadItem("vdiliveraddress")
			// .getValue();
		}
	}

	/**
	 * 创建者：王乃军 功能：在表单设置显示VO,不更新界面状态updateValue() 参数： 返回： 例外： 日期：(2001-5-9
	 * 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	public void setImportBillVO(GeneralBillVO bvo) throws Exception {
		setImportBillVO(bvo, true);
	}

	/**
	 * 创建者：王乃军 功能：选中列表形式下的第sn张单据 参数：sn 单据序号
	 * 
	 * 返回： 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	public void setListBodyData(GeneralBillItemVO voi[]) {
		if (voi != null) {
			try {
				getBillListPanel().getBodyTable().getModel().removeTableModelListener(this);
				getBillListPanel().setBodyValueVO(voi);
				// 由于已经在setAlistDataBYFormula中执行了表体公式，
				// 所以不可以重复出现下面这行代码 by hanwei 2003-06-24
				// getBillListPanel().getBodyBillModel().execLoadFormula();
			} catch (Exception e) {
				SCMEnv.out(e.getMessage());
			} finally {
				getBillListPanel().getBodyTable().getModel().addTableModelListener(this);
			}
		}
	}

	/**
	 * 创建者：王乃军 功能：刷新列表形式表头数据为指定的数据 参数： 返回： 例外： 日期：(2001-5-8 19:08:05)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	public void setListHeadData(GeneralBillHeaderVO voh[]) {
		if (voh != null && voh.length > 0) {
			try {
				getBillListPanel().getHeadBillModel().setSortColumn(null);

				if (m_bIsByFormula) {
					// 添加公式处理获取仓库和废品仓库信息 by hw 2003-02-27
					getInvoInfoBYFormula().setBillHeaderWH(voh);
				}

				// 通过单据公式容器类执行有关公式解析的方法
				getFormulaBillContainer().formulaHeaders(getFormulaItemHeader(), voh);
				// 赋予表体数据
				getBillListPanel().setHeaderValueVO(voh);
				// 不可以调用下面代码，已经在上面执行了表头公式
				// getBillListPanel().getHeadBillModel().execLoadFormula();

			} catch (Exception e2) {
				nc.vo.scm.pub.SCMEnv.error(e2);
			}
		}
	}

	/**
	 * 创建者：王乃军 功能： 参数： 返回： 例外： 日期：(2001-11-24 12:14:35) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @param newNeedBillRef
	 *            boolean
	 */
	public void setNeedBillRef(boolean newNeedBillRef) {
		m_bNeedBillRef = newNeedBillRef;
	}

	/**
	 * 创建者：王乃军 功能：列表形式下打印前， 置小数精度 参数： 返回： 例外： 日期：(2001-11-23 18:11:18)
	 * 修改日期，修改人，修改原因，注释标志：
	 */
	protected void setScaleOfListData(ArrayList alListData) {
		if (alListData != null) {
			GeneralBillItemVO[] voaItem = null;
			for (int bill = 0; bill < alListData.size(); bill++)
				if (alListData.get(bill) != null) {
					voaItem = ((GeneralBillVO) alListData.get(bill)).getItemVOs();

					GenMethod.setScale(voaItem, m_ScaleKey, m_ScaleValue);

				}
		} else SCMEnv.out("ld null.");

	}

	/**
	 * 创建者：王乃军 功能：设置表体/表尾的小数位数 参数： 返回： 例外： 日期：(2001-11-23 18:11:18)
	 * 修改日期，修改人，修改原因，注释标志：
	 */
	protected void setScaleOfListPanel() {
		nc.ui.ic.pub.scale.ScaleInit si = new nc.ui.ic.pub.scale.ScaleInit(m_sUserID, m_sCorpID, m_ScaleValue);

		try {
			si.setScale(getBillListPanel(), m_ScaleKey);
		} catch (Exception e) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000060")/* @res "精度设置失败：" */
					+ e.getMessage());
		}

	}

	/**
	 * 创建者：王乃军 功能：在表单设置显示VO,不更新界面状态updateValue() 参数： 返回： 例外： 日期：(2001-5-9
	 * 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	public void setTempBillVO(GeneralBillVO bvo) throws Exception {
		if (bvo == null) throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000061")/* @res "传入的单据为空！" */);
		// 表体行数
		int iRowCount = bvo.getItemCount();

		try {
			getBillCardPanel().getBillModel().removeTableModelListener(this);
			getBillCardPanel().removeBillEditListenerHeadTail();
			// 保存一个clone()
			m_voBill = (GeneralBillVO) bvo.clone();
			// 设置数据
			getBillCardPanel().setBillValueVO(bvo);
			// 执行公式
			getBillCardPanel().getBillModel().execLoadFormula();
			execHeadTailFormulas();
			// 更新状态 ---delete it to support CANCEL
			// getBillCardPanel().updateValue();
			// 清存货现存量数据
			bvo.clearInvQtyInfo();
			// 有表体行，选中第一行
			if (iRowCount > 0) {
				// 选中第一行
				getBillCardPanel().getBillTable().setRowSelectionInterval(0, 0);
				// 重置序列号是否可用
				setBtnStatusSN(0, true);
				// 刷新现存量显示
				// setTailValue(0);
				// 重置其它数据
				nc.ui.pub.bill.BillModel bmTemp = getBillCardPanel().getBillModel();
				m_alLocatorDataBackup = m_alLocatorData;
				m_alSerialDataBackup = m_alSerialData;
				m_alLocatorData = new ArrayList();
				m_alSerialData = new ArrayList();

				for (int row = 0; row < iRowCount; row++) {
					// 设置行状态为新增
					if (bmTemp != null) bmTemp.setRowState(row, nc.ui.pub.bill.BillModel.ADD);
					m_alLocatorData.add(null);
					m_alSerialData.add(null);
				}
			}
		} catch (Exception e) {
			SCMEnv.out(e.getMessage());
		} finally {
			getBillCardPanel().getBillModel().addTableModelListener(this);
			getBillCardPanel().addBillEditListenerHeadTail(this);
		}
	}

	/**
	 * 创建者：王乃军 功能：设置表体的合计列 参数： 返回： 例外： 日期：(2001-11-23 18:11:18)
	 * 修改日期，修改人，修改原因，注释标志：
	 */
	protected void setTotalCol() {
		getBillCardPanel().setTatolRowShow(true);
		// getBillListPanel().set
		// 考虑到单据模版内部的效率，单独拿出来效率更高些。因为表体数量较大，用哈希表实现.
		nc.ui.pub.bill.BillItem[] biaCardBody = getBillCardPanel().getBodyItems();
		// nc.ui.pub.bill.BillItem[] biaListBody =
		// getBillCardPanel().getBodyItems();
		// [itemkey,i]
		Hashtable htCardBody = new Hashtable();
		for (int i = 0; i < biaCardBody.length; i++)
			htCardBody.put(biaCardBody[i].getKey(), new Integer(i));
		// //[itemkey,i]
		// Hashtable htListBody = new Hashtable();
		// for (int i = 0; i < biaCardBody.length; i++)
		// htListBody.put(biaListBody[i].getKey(), new Integer(i));

		// 表体列
		String[] saBodyTotalItemKey = {
				"nmny", "nplannedmny", "nshouldinnum", "nshouldoutnum", "ntranoutnum", "nretnum", "noutnum", "nleftnum", "ninnum", "ninassistnum", "nleftastnum", "nneedinassistnum", "noutassistnum", "nretastnum", "nshouldoutassistnum", "ntranoutastnum", "volume", "weight"
		};
		for (int k = 0; k < saBodyTotalItemKey.length; k++) {
			// 如果是此列
			if (htCardBody.containsKey(saBodyTotalItemKey[k])) biaCardBody[Integer.valueOf(htCardBody.get(saBodyTotalItemKey[k]).toString()).intValue()].setTatol(true);
			// //如果是此列
			// if (htListBody.containsKey(saBodyTotalItemKey[k]))
			// biaListBody[Integer
			// .valueOf(htListBody.get(saBodyTotalItemKey[k]).toString())
			// .intValue()]
			// .setTatol(true);
		}

	}

	/**
	 * 创建者：王乃军 功能：在表单设置显示VO,不更新界面状态updateValue() 参数： 返回： 例外： 日期：(2001-5-9
	 * 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	public void setTs(int iIndex, ArrayList alTs) throws Exception {
		// update bill in list data ------- security -----------
		GeneralBillVO voListBill = null;
		// 设置m_voBill,以读取控制数据。
		if (iIndex >= 0 && m_alListData != null && m_alListData.size() > iIndex && m_alListData.get(iIndex) != null) {
			// 这里不能clone(),改变了m_voBill同时改变m_alListData
			voListBill = (GeneralBillVO) m_alListData.get(iIndex);
		}
		if (voListBill != null) setTs(voListBill, alTs);
	}

	/**
	 * 创建者：王乃军 功能：在表单设置显示VO,不更新界面状态updateValue() 参数： 返回： 例外： 日期：(2001-5-9
	 * 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	public void setTs(GeneralBillVO voThisBill, ArrayList alTs) throws Exception {
		if (alTs == null || alTs.size() < 2 || alTs.get(0) == null || alTs.get(1) == null) throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000027")/* @res "传入的ts为空！" */);
		// put ts to a hashtable,KEy=body item,value=ts
		Hashtable htTs = new Hashtable();
		ArrayList alTemp = null;
		// start from 1 first is for head.
		for (int i = 1; i < alTs.size(); i++) {
			alTemp = (ArrayList) alTs.get(i);
			if (alTemp != null && alTemp.size() >= 2 && alTemp.get(0) != null && alTemp.get(1) != null) htTs.put(alTemp.get(0), alTemp);
		}
		// SCMEnv.out(htTs.toString());
		if (voThisBill != null) {
			voThisBill.setStatus(nc.vo.pub.VOStatus.UNCHANGED);
			voThisBill.getHeaderVO().setTs(alTs.get(0).toString());
			// 表体行数
			int iRowCount = voThisBill.getItemCount();
			GeneralBillItemVO voaItem[] = voThisBill.getItemVOs();
			// Object oTempTs = null;
			// ts,num,assistnum,vfirstbillcode
			ArrayList alrow = null;
			for (int row = 0; row < iRowCount; row++) {
				// oTempTs = htTs.get(voaItem[row].getPrimaryKey());
				// if (oTempTs != null) {
				// voaItem[row].setTs(oTempTs.toString());
				// } else
				// SCMEnv.out(
				// "-------Err-------frh ts -------" + row +
				// voaItem[row].getPrimaryKey());
				alrow = (ArrayList) htTs.get(voaItem[row].getPrimaryKey());
				if (alrow == null) {
					SCMEnv.out("-------Err-------frh ts -------" + row + voaItem[row].getPrimaryKey());
					continue;
				}

				if (alrow.size() > 1) voaItem[row].setTs((String) alrow.get(1));
				if (alrow.size() > 2 && alrow.get(2) != null) {
					voaItem[row].setAttributeValue(m_sNumItemKey, alrow.get(2));
				}
				if (alrow.size() > 3 && alrow.get(3) != null) {
					voaItem[row].setAttributeValue(m_sAstItemKey, alrow.get(3));
				}
				if (alrow.size() > 4 && alrow.get(4) != null) {
					voaItem[row].setVfirstbillcode((String) alrow.get(4));

				}
				if (alrow.size() > 5 && alrow.get(5) != null) {
					voaItem[row].setVbatchcode((String) alrow.get(5));
				}
				if (alrow.size() > 8 && alrow.get(8) != null) {
					voaItem[row].setNprice((UFDouble) alrow.get(8));
				}
				if (alrow.size() > 9 && alrow.get(9) != null) {
					voaItem[row].setNmny((UFDouble) alrow.get(9));
				}

			}
		}
	}

	/**
	 * 创建者：王乃军 功能：在表单设置显示VO,不更新界面状态updateValue() 参数： 返回： 例外： 日期：(2001-5-9
	 * 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	public void setUiTs(ArrayList alTs) throws Exception {
		if (alTs == null || alTs.size() < 2 || alTs.get(0) == null || alTs.get(1) == null) throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000027")/* @res "传入的ts为空！" */);
		// put ts to a hashtable,KEy=body item,value=ts
		Hashtable htTs = new Hashtable();
		ArrayList alTemp = null;
		for (int i = 1; i < alTs.size(); i++) {
			alTemp = (ArrayList) alTs.get(i);
			if (alTemp != null && alTemp.size() >= 2 && alTemp.get(0) != null && alTemp.get(1) != null) htTs.put(alTemp.get(0), alTemp);
		}
		getBillCardPanel().setHeadItem("ts", alTs.get(0).toString());
		// 表体行数
		int iRowCount = getBillCardPanel().getRowCount();
		// Object oTempTs = null;
		// ts,num,astnum,vfirstbillcode
		ArrayList alrow = null;

		getBillCardPanel().getBillModel().setNeedCalculate(false);

		for (int row = 0; row < iRowCount; row++) {
			// oTempTs = htTs.get(getBillCardPanel().getBodyValueAt(row,
			// m_sBillRowItemKey));
			// if (oTempTs != null) {
			// getBillCardPanel().setBodyValueAt(oTempTs.toString(), row, "ts");
			// } else
			// SCMEnv.out("-------Err-------frh ts -------");
			alrow = (ArrayList) htTs.get(getBillCardPanel().getBodyValueAt(row, m_sBillRowItemKey));
			if (alrow == null) {
				SCMEnv.out("-------Err-------frh ts -------");
				continue;
			}

			if (alrow.size() > 1) getBillCardPanel().setBodyValueAt((String) alrow.get(0), row, "ts");
			if (alrow.size() > 2 && alrow.get(2) != null) {
				getBillCardPanel().setBodyValueAt((UFDouble) alrow.get(2), row, m_sNumItemKey);
			}
			if (alrow.size() > 3 && alrow.get(3) != null) {
				getBillCardPanel().setBodyValueAt((UFDouble) alrow.get(3), row, m_sAstItemKey);
			}
			if (alrow.size() > 4 && alrow.get(4) != null) {
				getBillCardPanel().setBodyValueAt((String) alrow.get(4), row, "vfirstbillcode");
			}
			if (alrow.size() > 5 && alrow.get(5) != null) {
				getBillCardPanel().setBodyValueAt((String) alrow.get(5), row, "vbatchcode");
			}
			if (alrow.size() > 8 && alrow.get(8) != null) {
				getBillCardPanel().setBodyValueAt((UFDouble) alrow.get(8), row, "nprice");
			}
			if (alrow.size() > 9 && alrow.get(9) != null) {
				getBillCardPanel().setBodyValueAt((UFDouble) alrow.get(9), row, "nmny");
			}

		}

		getBillCardPanel().getBillModel().setNeedCalculate(true);
		getBillCardPanel().getBillModel().reCalcurateAll();

	}

	/**
	 * 创建者：王乃军 功能：重载的显示提示信息对话框功能 参数： 返回： 例外： 日期：(2001-5-9 9:23:32)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	public void showErrorMessage(String sMsg) {
		if (m_bUserDefaultErrDlg) super.showErrorMessage(sMsg);
		else nc.ui.pub.beans.MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/* @res "提示" */, sMsg);

	}

	/**
	 * 创建者：王乃军 功能：重载的显示提示信息对话框功能 参数： 返回： 例外： 日期：(2001-5-9 9:23:32)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	public void showWarningMessage(String sMsg) {
		if (m_bUserDefaultErrDlg) super.showWarningMessage(sMsg);
		else nc.ui.pub.beans.MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/* @res "提示" */, sMsg);

	}

	/**
	 * 创建者：张欣 功能：设置指定的业务类型为选择方式。 参数： 返回： 例外： 日期：(2001-5-9 9:23:32)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	protected void showSelBizType(ButtonObject bo) {
		ButtonObject[] boaAll = getButtonTree().getButton(ICButtonConst.BTN_BUSINESS_TYPE).getChildButtonGroup();
		if (boaAll != null) {
			for (int i = 0; i < boaAll.length; i++)
				if (bo.equals(boaAll[i])) {
					boaAll[i].setSelected(true);
					break;
				}
		}

	}

	/**
	 * 创建者：王乃军 功能：显示消耗的时间 参数： 返回： 例外： 日期：(2001-11-23 18:11:18)
	 * 修改日期，修改人，修改原因，注释标志：
	 */
	protected void showTime(long lStartTime, String sTaskHint) {
		long lTime = System.currentTimeMillis() - lStartTime;
		SCMEnv.out("执行<" + sTaskHint + ">消耗的时间为：" + (lTime / 60000) + "分" + ((lTime / 1000) % 60) + "秒" + (lTime % 1000) + "毫秒");

	}

	/**
	 * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2002-4-27 13:21:10) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @param time
	 *            long
	 */
	public void writeTimeLog(long timebegin, java.util.Date dbegin, String sHint) {
		// time
		long end = System.currentTimeMillis();
		java.util.Date dend = new java.util.Date(end);
		SCMEnv.out(end - timebegin);
		java.io.FileOutputStream out = null;
		try {
			out = new java.io.FileOutputStream("c://time.log", true);

			out.write(("\n" + sHint + dbegin.toString() + "-" + dend.toString() + new String(new Long(end - timebegin).toString())).getBytes());
			out.close();
		} catch (Exception e) {
			SCMEnv.out(e.getMessage());
		}
		// end time
	}

	// 用户名、密码校验UI
	protected nc.ui.scm.pub.AccreditLoginDialog m_AccreditLoginDialog;

	// 是否添加条码解析 add by hanwei 2004-03-01
	protected boolean m_bAddBarCodeField = true;

	// 条码编辑界面控制类
	public nc.ui.ic.pub.bill.BarcodeCtrl m_BarcodeCtrl = null;

	// 条码不完整是否保存
	protected boolean m_bBadBarcodeSave = false;

	// 条码是否保存
	protected boolean m_bBarcodeSave = false;

	// 条码数量按负数增加，如：采购入库的退库
	protected boolean m_bFixBarcodeNegative = false;

	// 单据公式容器 hanwei 2003-07-23
	BillFormulaContainer m_billFormulaContain;

	// 是否有导入操作 hanwei 2003-12-17
	private boolean m_bIsImportData = false;

	// added by zhx 条码编辑界面
	protected BarCodeDlg m_dlgBarCodeEdit = null;

	// 指定货位
	protected LocSelDlg m_dlgLocSel = null;

	public GeneralBillUICtl m_GenBillUICtl;

	// 条码编辑框的颜色列：每X+1行的颜色需要设置
	protected int m_iBarcodeUIColorRow = 20;

	// 文件打开对话框
	private nc.ui.ic.pub.tools.FileChooserImpBarcode m_InFileDialog = null;

	// 表明定位信息
	private boolean m_isLocated = false;

	// 翻页功能
	protected PageCtrlBtn m_pageBtn;

	// zhx 030626 单据行号
	protected final String m_sBillRowNo = "crowno";

	// 单据状态
	protected String m_sBillStatus = nc.vo.ic.pub.bill.BillStatus.FREE;

	protected String m_sColorRow = null;

	// 成套件对话框
	protected SetPartDlg m_SetpartDlg = null;

	// 排序主键
	String m_sLastKey = null;

	// 时间显示
	protected nc.vo.ic.pub.bill.Timer m_timer = new nc.vo.ic.pub.bill.Timer();

	public nc.ui.ic.pub.barcode.UIBarCodeTextFieldNew m_utfBarCode = null;

	// 辅助功能
	protected Vector m_vAuxiliary = null;

	// 退库菜单
	protected Vector m_vboSendback = null;

	// 导入条码维护菜单
	protected Vector m_vImportBarcodes = null;

	protected static final UFDouble UFDNEGATIVE = new UFDouble(-1.00); // 负数-1.00

	public static final nc.vo.pub.lang.UFDouble UFDZERO = new nc.vo.pub.lang.UFDouble(0.0);

	/**
	 * ClientUI 构造子注解。 nc 2.2 提供的单据联查功能构造子。
	 * 
	 */
	public GeneralBillClientUI(String pk_corp, String billType, String businessType, String operator, String billID) {
		super();
		// 查单据
		GeneralBillVO voBill = qryBill(pk_corp, billType, businessType, operator, billID, null);
		// 初始化界面
		m_alListData = new ArrayList();

		m_alListData.add(voBill);

		if (voBill == null) nc.ui.pub.beans.MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/* @res "提示" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000062")/* @res "没有符合查询条件的单据！" */);
		else {
			pk_corp = voBill.getHeaderVO().getPk_corp();
			initialize(pk_corp, operator, "jc", businessType, "0001", "2003-04-17");
			// 通过单据公式容器类执行有关公式解析的方法
			setListHeadData(new GeneralBillHeaderVO[] {
				voBill.getHeaderVO()
			});

			appendLocator(voBill);

			// 显示单据
			setBillVO(voBill);
		}

	}

	private QueryOnHandInfoPanel m_pnlQueryOnHand;// 现存量Panel

	/**
	 * 创建现存量参照Panel
	 * 
	 * @param iRow
	 */
	public QueryOnHandInfoPanel getPnlQueryOnHandPnl() {

		if (m_pnlQueryOnHand == null) {
			m_pnlQueryOnHand = new QueryOnHandInfoPanel(m_sUserID, m_sCorpID, getOnHandRefDeal(), true, getOnHandRefDeal(), true, true);
		}

		return m_pnlQueryOnHand;
	}

	protected OnHandRefCtrl m_onHandRefDeal = null;// 现存量参照的代码容器

	public OnHandRefCtrl getOnHandRefDeal() {
		if (m_onHandRefDeal == null) {
			m_onHandRefDeal = new OnHandRefCtrl(this);
		}
		return m_onHandRefDeal;
	}

	protected UIPanel m_pnlBarCode;

	/**
	 * 条码扫描Panel,当开关m_bAddBarcodeField为true时显示
	 * 
	 * @return
	 */
	protected UIPanel getPnlBc() {
		if (m_pnlBarCode == null) {
			m_pnlBarCode = new UIPanel();
			try {

				UILabel lbName = new UILabel(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000063")/*
																															* @res
																															* "请输入条形码: "
																															*/);
				lbName.setPreferredSize(new java.awt.Dimension(80, 22));

				m_pnlBarCode.setLayout(new java.awt.FlowLayout());
				((java.awt.FlowLayout) m_pnlBarCode.getLayout()).setHgap(20);
				((java.awt.FlowLayout) m_pnlBarCode.getLayout()).setAlignment(java.awt.FlowLayout.LEFT);

				m_utfBarCode = new nc.ui.ic.pub.barcode.UIBarCodeTextFieldNew();

				m_utfBarCode.addBarCodeInputListener(this, m_sCorpID);
				m_utfBarCode.setPreferredSize(new java.awt.Dimension(300, 22));
				m_utfBarCode.setMaxLength(100);
				m_utfBarCode.setMaxLength(300);

				m_pnlBarCode.add(lbName);
				m_pnlBarCode.add(m_utfBarCode);

			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		if (m_bAddBarCodeField == false) {
			m_pnlBarCode.setVisible(false);
		}
		return m_pnlBarCode;
	}

	/**
	 * 李俊 功能：特殊单为来源单据的其它出入单，数量可编辑；且数量不能大于应收发数量 参数： 返回： /* 1.数量编辑，辅助数量编辑， 11
	 * 得到应收发数量，NULL则为0 12 得到实际收发数量，比较两者数量，如果实际数量》实际辅助数量， 如果实际辅助数量》应收发辅助数量 提示
	 * 
	 * 例外： 日期：(2005-1-28 14:27:22) 修改日期，修改人，修改原因，注释标志：
	 * 
	 */
	public void afterNumEditFromSpe(nc.ui.pub.bill.BillEditEvent e) {

		try {
			int iRow = e.getRow();
			// 来源单据控制：
			String csourcetype = (String) m_voBill.getItemValue(iRow, "csourcetype");

			if (csourcetype != null && (csourcetype.equals(BillTypeConst.m_assembly) || csourcetype.equals(BillTypeConst.m_disassembly) || csourcetype.equals(BillTypeConst.m_transform) || csourcetype.equals(BillTypeConst.m_check))) {

				UFDouble dbNum = null;
				UFDouble dbShouldNum = null;
				if (e.getKey().equals(m_sNumItemKey)) {
					if (e.getValue() == null) return;
					dbNum = new UFDouble(e.getValue().toString());
					dbShouldNum = (UFDouble) m_voBill.getItemValue(iRow, m_sShouldNumItemKey);
					if (dbShouldNum == null) return;
					if (dbNum.toDouble().doubleValue() > dbShouldNum.toDouble().doubleValue()) {
						showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000064")/*
																														* @res
																														* "实际数量不能大于应收发数量！"
																														*/);
						getBillCardPanel().setBodyValueAt(null, iRow, m_sNumItemKey);
						getBillCardPanel().setBodyValueAt(null, iRow, m_sAstItemKey);
						return;
					}

				}

				if (e.getKey().equals(m_sAstItemKey)) {
					if (e.getValue() == null) return;
					dbNum = new UFDouble(e.getValue().toString());
					dbShouldNum = (UFDouble) m_voBill.getItemValue(iRow, m_sShouldAstItemKey);
					if (dbShouldNum == null) return;
					if (dbNum.toDouble().doubleValue() > dbShouldNum.toDouble().doubleValue()) {
						showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000064")/*
																														* @res
																														* "实际数量不能大于应收发数量！"
																														*/);
						getBillCardPanel().setBodyValueAt(null, iRow, m_sAstItemKey);
						getBillCardPanel().setBodyValueAt(null, iRow, m_sAstItemKey);
						return;
					}
				}
			}
		} catch (Exception ex) {
			nc.vo.scm.pub.SCMEnv.error(ex);

		}

	}

	/**
	 * 创建者：王乃军 功能：存货事件处理 参数：e单据编辑事件 返回： 例外： 日期：(2001-5-8 19:08:05)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 */
	public void afterInvMutiEdit(nc.ui.pub.bill.BillEditEvent e) {
		nc.vo.scm.pub.ctrl.GenMsgCtrl.printHint("afterInvMutiEdit:" + e.getKey());
		long ITimeAll = System.currentTimeMillis();

		int row = e.getRow();
		// 字段itemkey
		String sItemKey = e.getKey();
		m_voBill.setItemValue(row, "desainfo", null);
		nc.ui.pub.beans.UIRefPane invRef = (nc.ui.pub.beans.UIRefPane) getBillCardPanel().getBodyItem("cinventorycode").getComponent();
		// 管理档案PK
		String[] refPks = invRef.getRefPKs();
		// 如果返回为空，清空当前环境
		if (refPks == null || refPks.length == 0) {
			nc.vo.scm.pub.ctrl.GenMsgCtrl.printHint("参照返回的存货个数getRefPKs:0");
			clearRow(row);
			return;
		}
		invRef.setPK(null);

		nc.vo.scm.pub.ctrl.GenMsgCtrl.printHint("参照返回的存货个数getRefPKs:" + refPks.length);
		// 仓库和库存组织信息
		String sWhID = null;
		String sCalID = null;
		if (getBillCardPanel().getHeadItem(m_sMainWhItemKey) != null) {
			sWhID = (String) getBillCardPanel().getHeadItem(m_sMainWhItemKey).getValueObject();
			sCalID = (String) getBillCardPanel().getHeadItem(m_sMainCalbodyItemKey).getValueObject();
		}
		long ITime = System.currentTimeMillis();
		if (sCalID == null && sWhID != null) {
			try {
				Object[] ov = (Object[]) nc.ui.scm.pub.CacheTool.getCellValue("bd_stordoc", "pk_stordoc", "pk_calbody", sWhID);
				if (ov != null && ov.length > 0) sCalID = ov[0].toString();
			} catch (Exception e1) {
				SCMEnv.out(e1.toString());
			}
		}

		SCMEnv.showTime(ITime, "存货解析参数设置:");

		ITime = System.currentTimeMillis();
		// 存货解析
		// DUYONG 此处需要验证是否需要进行计划价的处理（经过验证，此处已经进行了处理）
		InvVO[] invVOs = getInvoInfoBYFormula().getInvParseWithPlanPrice(refPks, sWhID, sCalID, true, true);

		SCMEnv.showTime(ITime, "存货解析:");
		InvVO[] invvoBack = new InvVO[invVOs.length];
		for (int i = 0; i < invVOs.length; i++) {
			invVOs[i].setPk_corp(m_sCorpID);
			invvoBack[i] = (InvVO) invVOs[i].clone();
		}

		ITime = System.currentTimeMillis();
		// 处理件数
		try {
			QueryInfo info = new QueryInfo();
			invVOs = info.dealPackType(invVOs);
		} catch (Exception e1) {
			nc.vo.scm.pub.SCMEnv.error(e1);
			invVOs = invvoBack;
		}

		// 清除当前行的条码VO,清除界面的条码相关标志
		BarCodeVO[] bcVOs = m_voBill.getItemVOs()[row].getBarCodeVOs();

		if (bcVOs != null && m_utfBarCode != null) {
			m_utfBarCode.setRemoveBarcode(bcVOs);
			for (int i = 0; i < bcVOs.length; i++) {
				bcVOs[i].setStatus(nc.vo.pub.VOStatus.DELETED);

			}
		}
		m_voBill.getItemVOs()[row].setBarcodeClose(new UFBoolean("N"));
		m_voBill.getItemVOs()[row].setAttributeValue(IItemKey.NBARCODENUM, new UFDouble(0.0));

		getBillCardPanel().getBillModel().setValueAt(UFBoolean.FALSE, row, "bbarcodeclose");
		getBillCardPanel().getBillModel().setValueAt(null, row, IItemKey.NBARCODENUM);

		afterInvMutiEditSetUI(invVOs, row, sItemKey);
		SCMEnv.showTime(ITimeAll, "存货参照多选:");

	}

	/**
	 * 快速录入: v5支持集中采购, 此方法被采购入库覆盖
	 * 
	 * @param istartrow
	 *            增行开始字段
	 * @param count
	 *            增行数量
	 */
	public void setBodyDefaultData(int istartrow, int count) {

	}

	/**
	 * 此处插入方法说明。 存货参照多选后设置界面 用于存货多选和条码扫描两种场合 创建日期：(2004-5-7 12:40:43)
	 * 
	 * @param invVOs
	 *            nc.vo.scm.ic.bill.InvVO[]：存货VO iRow：当前行
	 *            sItemKey：当前行的Key:"cinventorycode"
	 */
	public void afterInvMutiEditSetUI(InvVO[] invVOs, int iRow, String sItemKey) {
		nc.vo.scm.pub.ctrl.GenMsgCtrl.printHint("afterInvMutiEditSetUI:" + sItemKey + "个数：" + invVOs.length);

		// 界面增行
		boolean isLastRow = false;

		int iLen = invVOs.length;
		if (invVOs != null && invVOs.length > 0) {
			if (iRow == getBillCardPanel().getRowCount() - 1) isLastRow = true;

			for (int i = iLen - 1; i >= 0; i--) {
				if (i < iLen - 1) {
					getBillCardPanel().insertLine();
				} else {
					if (getBillCardPanel().getBillModel().getRowState(iRow) == BillModel.NORMAL) getBillCardPanel().getBillModel().setRowState(iRow, BillModel.MODIFICATION);

				}
			}

			if (isLastRow) {
				nc.ui.scm.pub.report.BillRowNo.addLineRowNos(getBillCardPanel(), m_sBillTypeCode, m_sBillRowNo, iLen);

			} else nc.ui.scm.pub.report.BillRowNo.insertLineRowNos(getBillCardPanel(), m_sBillTypeCode, m_sBillRowNo, iRow + iLen - 1, iLen - 1);

		}

		// v5 lj 集中采购快速录入
		setBodyDefaultData(iRow, iLen);

		// 设置界面数据
		boolean bHasSourceBillTypecode = false;
		if (getSourBillTypeCode() == null || getSourBillTypeCode().trim().length() == 0) {
			bHasSourceBillTypecode = false;
		} else bHasSourceBillTypecode = true;

		int iCurRow = 0;

		getBillCardPanel().getBillModel().setNeedCalculate(false);
		// zhy2005-08-24表头供应商应写到表体
		String sHeadProviderName = null;
		String sHeadProviderID = null;
		String sPk_cusbasdoc = null;
		UIRefPane ref = null;
		if (getBillCardPanel().getHeadItem("cproviderid") != null) ref = (nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("cproviderid").getComponent();
		if (ref != null) {
			sHeadProviderName = ref.getRefName();
			sHeadProviderID = ref.getRefPK();
		}

		// 获得单据头界面中的供应商基本档案ID
		BillItem iPk_cubasdoc = getBillCardPanel().getHeadItem("pk_cubasdoc");
		Object oPk_cubasdoc = null;
		if (iPk_cubasdoc != null) oPk_cubasdoc = getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject();

		for (int i = 0; i < iLen; i++) {
			iCurRow = iRow + i;
			// 清批次/自由项等数据
			// 必须放在“m_voBill.setItemInv”代码之前 add by ydy 2003-12-17.01
			clearRowData(0, iCurRow, sItemKey);
			// 表体
			setBodyInvValue(iCurRow, invVOs[i]);

			if (m_voBill != null) {
				m_voBill.setItemInv(iCurRow, invVOs[i]);
				m_voBill.setItemValue(iCurRow, m_sBillPkItemKey, m_voBill.getHeaderValue(m_sBillPkItemKey));
				// zhy2005-08-24
				m_voBill.setItemValue(iCurRow, "cvendorid", sHeadProviderID);
				m_voBill.setItemValue(iCurRow, "vvendorname", sHeadProviderName);
				m_voBill.setItemValue(iCurRow, "pk_cubasdoc", oPk_cubasdoc);
				getBillCardPanel().setBodyValueAt(sHeadProviderID, iCurRow, "cvendorid");
				getBillCardPanel().setBodyValueAt(sHeadProviderName, iCurRow, "vvendorname");
				if (getBillCardPanel().getBodyItem("pk_cubasdoc") != null && oPk_cubasdoc != null) getBillCardPanel().setBodyValueAt(oPk_cubasdoc, iCurRow, "pk_cubasdoc");
			}

			if (bHasSourceBillTypecode) {
				// 清应数量
				// 同步vo
				if (m_voBill != null) {
					m_voBill.setItemValue(iCurRow, m_sShouldNumItemKey, null);
					m_voBill.setItemValue(iCurRow, m_sShouldAstItemKey, null);
				}
				if (getBillCardPanel().getBodyItem(m_sShouldNumItemKey) != null) getBillCardPanel().setBodyValueAt(null, iCurRow, m_sShouldNumItemKey);
				if (getBillCardPanel().getBodyItem(m_sShouldAstItemKey) != null) getBillCardPanel().setBodyValueAt(null, iCurRow, m_sShouldAstItemKey);
			}
		}
		// 设置当前行序列号是否可用
		getBillCardPanel().getBillModel().setNeedCalculate(true);
		getBillCardPanel().getBillModel().reCalcurateAll();

		setTailValue(iRow);
		setBtnStatusSN(iRow, true);

		// 设置当前选中行的现存量参照
		if (m_bOnhandShowHidden) showOnHandPnlInfo(iRow);
		// 动态设置界面
		getOnHandRefDeal().setInvCtrlValue(iRow);
		setCardMode();
		m_layoutManager.show();

	}

	/**
	 *  
	 */
	public void setCardMode() {
		// 如果现存量显示隐藏是显示状态，则当前卡片状态必须为MultiCardMode.CARD_TAB
		if (m_bOnhandShowHidden) {
			m_sMultiMode = MultiCardMode.CARD_TAB;
		} else {
			m_sMultiMode = MultiCardMode.CARD_PURE;
		}
	}

	/**
	 * 显示现存量参照的数据
	 * 
	 * @param iRow
	 */
	protected void showOnHandPnlInfo(int iRow) {
		if (m_pnlQueryOnHand == null || iRow < 0) return;

		m_pnlQueryOnHand.setVORefresh(getOnHandRefDeal().getSelectedItemHandInfo(iRow));

		m_pnlQueryOnHand.showData();

	}

	/**
	 * 创建者：王乃军 功能：删除处理 参数： 返回： 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	public String checkBillsCanDeleted(ArrayList alBills) {
		String sError = null;
		if (alBills != null) {
			{
				GeneralBillVO gvo = null;
				String sSourceBillType = null;
				for (int i = 0; i < alBills.size(); i++) {
					gvo = (GeneralBillVO) alBills.get(i);
					sSourceBillType = (String) gvo.getItemValue(0, "csourcetype");
					/** 判断单据类型是否为外部单据 */
					// 没有来源单据，并且浏览状态下，复制可用
					if (sSourceBillType != null && sSourceBillType.startsWith("4")) {
						// 内部生成单据
						/** 判断单据类型否为转库单 */
						if (sSourceBillType != null && (sSourceBillType.equals(BillTypeConst.m_transfer) || sSourceBillType.equals(BillTypeConst.m_assembly) || sSourceBillType.equals(BillTypeConst.m_disassembly) || sSourceBillType.equals(BillTypeConst.m_transform) || sSourceBillType.equals(BillTypeConst.m_check) || sSourceBillType.equals(nc.vo.ic.pub.BillTypeConst.m_AllocationOrder))) {

							/**
							 * 当单据是浏览状态时，复制按钮不可用 。组装、拆卸、形态转换、盘点特殊单据生成的其他入、
							 * 其他出不能直接删除，在删除时提示用户通过弃审特殊单实现。所以，将删除按钮置灰。
							 */

							String[] args = new String[1];
							args[0] = gvo.getHeaderValue("vbillcode").toString();
							String message = nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000340", null, args);
							/*
							 * @res
							 * "单据号是：{0}的单据是由组装、拆卸、形态转换、盘点特殊单据生成的其他入,其他出不能直接删除，通过弃审特殊单实现!"
							 */

							if (!(sSourceBillType.equals(nc.vo.ic.pub.BillTypeConst.m_transfer) || sSourceBillType.equals(nc.vo.ic.pub.BillTypeConst.m_AllocationOrder))) {
								sError += "(" + message + ")";
							}

						} else if (sSourceBillType != null && (sSourceBillType.equals(BillTypeConst.m_saleOut) || sSourceBillType.equals(BillTypeConst.m_purchaseIn))) {
							// 销售出库单，采购入库单配套生成的其它出，其它入库单据不能删除修改单据，复制
							String[] args1 = new String[1];
							args1[0] = gvo.getHeaderValue("vbillcode").toString();
							String message1 = nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000341", null, args1);
							/*
							 * @res
							 * "单据号是：{0}的单据是由销售出库单，采购入库单配套生成的其它出，其它入库单据不能删除修改单据!"
							 */
							sError += "(" + message1 + ")";
						}

					}
				}
			}

		}
		return sError;
	}

	/**
	 * 作者：李俊 判断选中行(导入Excel条码）是否有数据，是否选中。 创建日期：(2004-5-4 13:12:20)
	 */
	private int checkSelectionRow() {
		if (m_iCurPanel == BillMode.Card) {
			if (m_iLastSelListHeadRow != -1 && null != m_alListData && m_alListData.size() != 0) {
				if (m_voBill == null) m_voBill = (GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow);
			}
		} else if (m_iCurPanel == BillMode.List) { // 列表
			showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000065")/* @res "请切换到卡片界面导入数据!" */);
			return -1;
		}
		int rownow = getBillCardPanel().getBillTable().getSelectedRow();
		if (rownow < 0) { return -1; }
		String invID = (String) getBillCardPanel().getBodyValueAt(rownow, "cinventoryid");
		if (invID == null || invID.trim().equals("")) {
			showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000066")/* @res "没有选中存货!" */);
			return -1;
		}
		if (m_voBill == null || m_voBill.getChildrenVO() == null || m_voBill.getChildrenVO().length < rownow) {
			showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH003")/* @res "请选择要处理的数据！" */);
			return -1;
		}
		return rownow;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2003-10-09 16:34:45)
	 */
	protected void checkVMIWh(GeneralBillVO voBill) throws Exception {

		VOCheck.checkVMIWh(new QueryInfo(), voBill);
		// if (voBill != null) {
		// String sBizType = voBill.getHeaderVO().getCbiztypeid();
		// String verifyrule = null;
		// if (sBizType != null) {
		// verifyrule =
		// execFormular("verifyrule->getColValue(bd_busitype,verifyrule,pk_busitype,pk_busitype)",
		// sBizType);
		// if (verifyrule != null && verifyrule.equals("V")) {
		// if (voBill.getWh().getIsforeignstor() != null
		// && !(voBill.getWh().getIsforeignstor().booleanValue() &&
		// voBill.getWh().getIsgathersettle().booleanValue())) {
		// throw new Exception("核算规则是消耗汇总业务类型的单据必须入外寄仓仓库!");
		// }
		// } else if (verifyrule != null && !verifyrule.equals("V")) {
		// if (voBill.getWh().getIsforeignstor() != null
		// && voBill.getWh().getIsforeignstor().booleanValue()
		// && voBill.getWh().getIsgathersettle().booleanValue()) {
		// throw new Exception("核算规则不是消耗汇总业务类型的单据不能入外寄仓仓库!");
		// }
		// }

		// }

		// }
	}

	/**
	 * 此处插入方法说明。 创建日期：(2003-11-7 15:13:21)
	 * 
	 * @param row
	 *            int
	 */
	public void clearRow(int row) {
		clearRowData(row);
		// 清应数量
		// 同步vo
		if (m_voBill != null) {
			m_voBill.setItemValue(row, m_sShouldNumItemKey, null);
			m_voBill.setItemValue(row, m_sShouldAstItemKey, null);
		}
		if (getBillCardPanel().getBodyItem(m_sShouldNumItemKey) != null) getBillCardPanel().setBodyValueAt(null, row, m_sShouldNumItemKey);
		if (getBillCardPanel().getBodyItem(m_sShouldAstItemKey) != null) getBillCardPanel().setBodyValueAt(null, row, m_sShouldAstItemKey);
		// 表尾
		setTailValue(null);
	}

	/**
	 * 来源单据是转库单时的界面控制方法 功能： 参数： 返回： 例外： 日期：(2001-10-19 09:43:22)
	 * 修改日期，修改人，修改原因，注释标志：
	 */
	protected void ctrlSourceBillButtons(boolean bUpdateButtons) {
		String sSourceBillType = getSourBillTypeCode();
		/** 判断单据类型是否为外部单据 */
		// 没有来源单据，并且浏览状态下，复制可用
		if ((sSourceBillType == null || sSourceBillType.trim().length() == 0)) {
			if (m_iMode == BillMode.Browse && m_iBillQty > 0 && m_iLastSelListHeadRow >= 0) {
				// 浏览状态下可以复制单据
				getButtonTree().getButton(ICButtonConst.BTN_BILL_COPY).setEnabled(true);
			}
		} else if (!sSourceBillType.startsWith("4")) {
			// 外部生成的单据,且已经作乐配套处理

			// 修改、新增状态下。
			if (m_iMode == BillMode.Update || m_iMode == BillMode.New) {

				/** 置单据行，表体右键按钮,增行，插入行为不可用,复制，粘贴可用 */

				getButtonTree().getButton(ICButtonConst.BTN_BILL_COPY).setEnabled(false);
				getButtonTree().getButton(ICButtonConst.BTN_LINE_ADD).setEnabled(false);
				getButtonTree().getButton(ICButtonConst.BTN_LINE_INSERT).setEnabled(false);

				// 表体右键增加行
				getBillCardPanel().getBodyMenuItems()[0].setEnabled(false);
				// 表体右键插入行
				getBillCardPanel().getBodyMenuItems()[2].setEnabled(false);
				// 表体右键删除行，强行置为true; 有Bug，若把插入行置灰，则它前面的删除行也被置灰。
				getBillCardPanel().getBodyMenuItems()[1].setEnabled(true);
			} else
			// 浏览状态下只是不让复制单据
			getButtonTree().getButton(ICButtonConst.BTN_BILL_COPY).setEnabled(false);
			// boolean bisdispensebill = isDispensedBill(null);
			// if (bisdispensebill) {
			// getButtonTree().getButton(ICButtonConst.BTN_BILL_DELETE).setEnabled(false);
			// getButtonTree().getButton(ICButtonConst.BTN_BILL_COPY).setEnabled(false);
			// getButtonTree().getButton(ICButtonConst.BTN_BILL_EDIT).setEnabled(false);

			// }
		} else {
			// 内部生成单据
			/** 判断单据类型否为转库单 */
			if (sSourceBillType != null && (sSourceBillType.equals(BillTypeConst.m_transfer) || sSourceBillType.equals(BillTypeConst.m_assembly) || sSourceBillType.equals(BillTypeConst.m_disassembly) || sSourceBillType.equals(BillTypeConst.m_transform) || sSourceBillType.equals(BillTypeConst.m_check) || sSourceBillType.equals(nc.vo.ic.pub.BillTypeConst.m_AllocationOrder))) {
				if (m_iMode == BillMode.Update || m_iMode == BillMode.New) {
					/** 当单据是新增或修改状态时的界面行为控制。 */
					/** 置单据行，表体右键按钮,删行，增行，插入行为不可用,复制，粘贴可用 */

					/** 置菜单按钮的可用状态 */
					getButtonTree().getButton(ICButtonConst.BTN_LINE).setEnabled(false);
					/** 置表体右键菜单按钮的可用状态 */
					getBillCardPanel().setBodyMenuShow(false);

				}
				/**
				 * 当单据是浏览状态时，复制按钮不可用 。组装、拆卸、形态转换、盘点特殊单据生成的其他入、
				 * 其他出不能直接删除，在删除时提示用户通过弃审特殊单实现。所以，将删除按钮置灰。
				 */
				else {
					if (!(sSourceBillType.equals(nc.vo.ic.pub.BillTypeConst.m_transfer) || sSourceBillType.equals(nc.vo.ic.pub.BillTypeConst.m_AllocationOrder))) {
						getButtonTree().getButton(ICButtonConst.BTN_BILL_DELETE).setEnabled(false);
					} else {
						// 签字的单据不能删除！
						if (CANNOTSIGN == isSigned() || NOTSIGNED == isSigned()) getButtonTree().getButton(ICButtonConst.BTN_BILL_DELETE).setEnabled(true);
					}
					getButtonTree().getButton(ICButtonConst.BTN_BILL_COPY).setEnabled(false);
				}

			} else if (sSourceBillType != null && (sSourceBillType.equals(BillTypeConst.m_saleOut) || sSourceBillType.equals(BillTypeConst.m_purchaseIn))) {
				// 销售出库单，采购入库单配套生成的其它出，其它入库单据不能删除修改单据，复制
				getButtonTree().getButton(ICButtonConst.BTN_BILL_DELETE).setEnabled(false);
				getButtonTree().getButton(ICButtonConst.BTN_BILL_COPY).setEnabled(false);
				getButtonTree().getButton(ICButtonConst.BTN_BILL_EDIT).setEnabled(false);

			}
			// 如果其他入和其他出的拆解类型是“系统产生“的，那么不能复制，删除，修改
			Object oDesatype = null;
			if (BillMode.List == m_iCurPanel) {
				if (m_alListData != null && m_iLastSelListHeadRow >= 0) {
					GeneralBillVO voBill = (GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow);
					oDesatype = voBill.getItemValue(0, "idesatype");
				}
			} else if (BillMode.Card == m_iCurPanel) {
				if (m_voBill != null && m_voBill.getItemVOs() != null && m_voBill.getItemVOs().length > 0) {
					oDesatype = m_voBill.getItemValue(0, "idesatype");
				}

			}
			if (oDesatype != null) {
				Integer idesatype = new Integer(oDesatype.toString().trim());
				if (idesatype.intValue() == nc.vo.ic.pub.DesassemblyVO.TYPE_SYS) {
					getButtonTree().getButton(ICButtonConst.BTN_BILL_DELETE).setEnabled(false);
					getButtonTree().getButton(ICButtonConst.BTN_BILL_COPY).setEnabled(false);
					getButtonTree().getButton(ICButtonConst.BTN_BILL_EDIT).setEnabled(false);
					getButtonTree().getButton(ICButtonConst.BTN_SIGN).setEnabled(false);
				}
			}

		}
		// //已经生成配套的单据不能被删除，修改和复制。
		boolean bisdispensebill = isDispensedBill(null);
		if (bisdispensebill) {
			getButtonTree().getButton(ICButtonConst.BTN_BILL_DELETE).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_BILL_COPY).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_BILL_EDIT).setEnabled(false);

		}

		// ###########################
		// 设置 add by hanwei 2004-05-14
		// 1、库存出入库单如果有直接调转标志，需要在界面控制 修改、删除、复制等按钮不可用；
		// 2、如果单据的仓库是直运仓，应该控制修改、删除、复制等按钮不可用；
		// 参数true:只设置false的属性
		setBtnStatusTranflag(true);
		// #############################

		// 必须调用的刷新界面的菜单按钮
		if (bUpdateButtons) updateButtons();

		// v5 lj
		if (m_bRefBills == true) {
			setRefBtnStatus();
		}
	}

	/**
	 * 来源单据是转库单时的界面控制方法 功能： 参数： 返回： 例外： 日期：(2001-10-19 09:43:22)
	 * 修改日期，修改人，修改原因，注释标志：
	 */
	protected void ctrlSourceBillUI(boolean bUpdateButtons) {
		try {
			String sSourceBillType = getSourBillTypeCode();
			/** 判断单据类型是否为外部单据 */
			// 没有来源单据，并且浏览状态下，复制可用
			if ((sSourceBillType == null || sSourceBillType.trim().length() == 0)) {
				if (m_iMode == BillMode.Browse && m_iBillQty > 0 && m_iLastSelListHeadRow >= 0) {
					// 浏览状态下可以复制单据
					getButtonTree().getButton(ICButtonConst.BTN_BILL_COPY).setEnabled(true);
				}
			} else if (!sSourceBillType.startsWith("4")) {
				// 外部生成的单据

				// 修改、新增状态下。
				if (m_iMode == BillMode.Update || m_iMode == BillMode.New) {

					/** 置表头不可编辑项 */
					String[] saNotEditableHeadKey = {
							"cbiztype", // wnj:2002-10-23.seg01
							// "cdptid",
							// //"cbizid",
							"ccustomerid",
							"cproviderid",
							"alloctypename",
							"freplenishflag"
					};

					for (int i = 0; i < saNotEditableHeadKey.length; i++) {
						if (getBillCardPanel().getBillData().getHeadItem(saNotEditableHeadKey[i]) != null) getBillCardPanel().getBillData().getHeadItem(saNotEditableHeadKey[i]).setEdit(false);

					}

					// if (m_voBill != null && m_voBill.getItemCount() > 0) {
					// String[] sitemkey = { m_sShouldNumItemKey,
					// m_sShouldAstItemKey };
					// for (int j = 0; j < sitemkey.length; j++) {
					// if (getBillCardPanel().getBillData().getBodyItem(
					// sitemkey[j]) != null)
					// getBillCardPanel().getBillData().getBodyItem(
					// sitemkey[j]).setEdit(false);
					// }
					//
					// }

					// SCMEnv.out("From then, you can edit inv with its
					// replacement inv!");
					/** 置单据行，表体右键按钮,增行，插入行为不可用,复制，粘贴可用 */

					getButtonTree().getButton(ICButtonConst.BTN_BILL_COPY).setEnabled(false);
					getButtonTree().getButton(ICButtonConst.BTN_LINE_ADD).setEnabled(false);
					getButtonTree().getButton(ICButtonConst.BTN_LINE_INSERT).setEnabled(false);

					// 表体右键增加行
					getBillCardPanel().getBodyMenuItems()[0].setEnabled(false);
					// 表体右键插入行
					getBillCardPanel().getBodyMenuItems()[2].setEnabled(false);
					// 表体右键删除行，强行置为true; 有Bug，若把插入行置灰，则它前面的删除行也被置灰。
					getBillCardPanel().getBodyMenuItems()[1].setEnabled(true);
				} else
				// 浏览状态下只是不让复制单据
				getButtonTree().getButton(ICButtonConst.BTN_BILL_COPY).setEnabled(false);

			} else {
				// 内部生成单据,特殊单据，销售出库，采购入库的配套生成的其它入，出。
				/** 判断单据类型否为转库单 */
				if (sSourceBillType != null && (sSourceBillType.equals(BillTypeConst.m_transfer) || sSourceBillType.equals(BillTypeConst.m_assembly) || sSourceBillType.equals(BillTypeConst.m_disassembly) || sSourceBillType.equals(BillTypeConst.m_transform) || sSourceBillType.equals(BillTypeConst.m_check) || sSourceBillType.equals(BillTypeConst.m_saleOut) || sSourceBillType.equals(BillTypeConst.m_purchaseIn))) {
					if (m_iMode == BillMode.Update || m_iMode == BillMode.New) {
						/** 当单据是新增或修改状态时的界面行为控制。 */
						/** 置单据行，表体右键按钮,删行，增行，插入行为不可用,复制，粘贴可用 */

						/** 置菜单按钮的可用状态 */
						getButtonTree().getButton(ICButtonConst.BTN_LINE).setEnabled(false);
						/** 置表体右键菜单按钮的可用状态 */
						getBillCardPanel().setBodyMenuShow(false);
						String sHeadItemKey = null;
						// 配套销售出库单生成的其它出库单，表头客户不可编辑
						if (sSourceBillType.equals(BillTypeConst.m_saleOut) && m_sBillTypeCode.equals(BillTypeConst.m_otherOut)) {
							sHeadItemKey = "ccustomerid";

						}
						// 配套采购入库单生成的其它入库单，表头客户不可编辑
						else if (sSourceBillType.equals(BillTypeConst.m_purchaseIn) && m_sBillTypeCode.equals(BillTypeConst.m_otherIn)) {
							sHeadItemKey = "cproviderid";
						}

						/** 置表头不可编辑项 */
						String[] saNotEditableHeadKey2 = null;
						if (sHeadItemKey == null) saNotEditableHeadKey2 = new String[] {
								m_sMainWhItemKey, m_sMainCalbodyItemKey, m_sWasteWhItemKey, m_sMainCalbodywasteItemKey
						};
						else saNotEditableHeadKey2 = new String[] {
								m_sMainWhItemKey, m_sMainCalbodyItemKey, m_sWasteWhItemKey, m_sMainCalbodywasteItemKey, sHeadItemKey
						};

						for (int i = 0; i < saNotEditableHeadKey2.length; i++) {
							if (getBillCardPanel().getBillData().getHeadItem(saNotEditableHeadKey2[i]) != null) getBillCardPanel().getBillData().getHeadItem(saNotEditableHeadKey2[i]).setEnabled(false);

						}

					}
					/**
					 * 当单据是浏览状态时，复制按钮不可用 。组装、拆卸、形态转换、盘点特殊单据生成的其他入、
					 * 其他出不能直接删除，在删除时提示用户通过弃审特殊单实现。所以，将删除按钮置灰。
					 */
					else {
						if (!(sSourceBillType.equals(nc.vo.ic.pub.BillTypeConst.m_transfer) || sSourceBillType.equals(nc.vo.ic.pub.BillTypeConst.m_AllocationOrder))) {
							getButtonTree().getButton(ICButtonConst.BTN_BILL_DELETE).setEnabled(false);
						} else {
							// 签字的单据不能删除！
							int iIsSigned = isSigned();
							if (iIsSigned != SIGNED) getButtonTree().getButton(ICButtonConst.BTN_BILL_DELETE).setEnabled(true);

						}
						getButtonTree().getButton(ICButtonConst.BTN_BILL_COPY).setEnabled(false);
					}
				} else if (sSourceBillType != null && (sSourceBillType.equals(BillTypeConst.m_saleOut) || sSourceBillType.equals(BillTypeConst.m_purchaseIn))) {
					// 销售出库单,采购入库但配套生成的其它出，其它入库单据不能删除修改单据，复制
					getButtonTree().getButton(ICButtonConst.BTN_BILL_DELETE).setEnabled(false);
					getButtonTree().getButton(ICButtonConst.BTN_BILL_COPY).setEnabled(false);
					getButtonTree().getButton(ICButtonConst.BTN_BILL_EDIT).setEnabled(false);

				} else if (sSourceBillType != null && (sSourceBillType.equals(BillTypeConst.m_AllocationOrder))) {
					// 来源单据是调拨订单的界面控制：

					/** 当单据是新增或修改状态时的界面行为控制。 */
					/** 置单据行，表体右键按钮,删行，增行，插入行为不可用,复制，粘贴可用 */

					/** 置菜单按钮的可用状态 */
					getButtonTree().getButton(ICButtonConst.BTN_LINE).setEnabled(false);
					/** 置表体右键菜单按钮的可用状态 */
					getBillCardPanel().setBodyMenuShow(false);

					/** 置表头库存组织不可编辑项 */
					String saNotEditableHeadKey2 = m_sMainCalbodyItemKey;

					if (getBillCardPanel().getBillData().getHeadItem(saNotEditableHeadKey2) != null) getBillCardPanel().getBillData().getHeadItem(saNotEditableHeadKey2).setEnabled(false);

				}

			} // 必须调用的刷新界面的菜单按钮

			// ###########################
			// 设置 add by hanwei 2004-05-14
			// 1、库存出入库单如果有直接调转标志，需要在界面控制 修改、删除、复制等按钮不可用；
			// 2、如果单据的仓库是直运仓，应该控制修改、删除、复制等按钮不可用；
			// 参数true:只设置false的属性
			setBtnStatusTranflag(true);
			// #############################

			if (bUpdateButtons) updateButtons();

			// v5 lj
			if (m_bRefBills == true) {
				setRefBtnStatus();
			}

		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
		}
	}

	/**
	 * 此处插入方法说明。 功能描述:处理查询时没有查到数据的情况下界面的。 作者：程起伍 输入参数: 返回值: 异常处理: 日期:(2003-6-9
	 * 15:57:49)
	 */
	protected void dealNoData() {
		// 设置当前的单据数量/序号，用于按钮控制
		setLastHeadRow(-1);
		// 初始化当前单据序号，切换时用到。
		m_iCurDispBillNum = -1;
		// 当前单据数
		m_iBillQty = 0;
		m_alListData = null;
		clearUi();
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000013")/* @res "未查到符合条件的单据。" */);
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-11-27 10:41:27)
	 * 
	 * @param error
	 *            java.lang.String
	 */
	public void errormessageshow(java.lang.String error) {
		// 提示警告信息
		java.awt.Toolkit.getDefaultToolkit().beep();
		showErrorMessage(error);
		// 光标回编辑框
		m_utfBarCode.requestFocus();
	}

	/**
	 * 调用公式 功能： 参数： 返回： 例外： 日期：(2001-11-12 16:47:04) 修改日期，修改人，修改原因，注释标志：
	 */
	private String execFormular(String formula, String value) {
		nc.ui.pub.formulaparse.FormulaParse f = new nc.ui.pub.formulaparse.FormulaParse();

		if (formula != null && !formula.equals("")) {
			// 设置表达式
			f.setExpress(formula);
			// 获得变量
			nc.vo.pub.formulaset.VarryVO varry = f.getVarry();
			// 给变量付值
			Hashtable h = new Hashtable();
			for (int j = 0; j < varry.getVarry().length; j++) {
				String key = varry.getVarry()[j];

				String[] vs = new String[1];
				vs[0] = value;
				h.put(key, StringUtil.toString(vs));
			}

			f.setDataS(h);
			// 设置结果
			if (varry.getFormulaName() != null && !varry.getFormulaName().trim().equals("")) return f.getValueS()[0];
			else return f.getValueS()[0];

		} else {
			return null;
		}
	}

	/**
	 * 此处插入方法说明。 子类可以重载本方法,实现废品库参照初始化 RefFilter.filtWasteWh(billItem, sCorpID,
	 * null);
	 * 
	 * 创建日期：(2004-3-19 17:41:50)
	 */
	public void filterRefofWareshouse(nc.ui.pub.bill.BillItem billItem, String sCorpID) {

		RefFilter.filtWh(billItem, sCorpID, null);

	}

	/**
	 * 此处插入方法说明。 获得权限认证UI 创建日期：(2004-4-19 14:11:06)
	 * 
	 * @return nc.ui.scm.pub.AccreditLoginDialog
	 */
	public nc.ui.scm.pub.AccreditLoginDialog getAccreditLoginDialog() {
		if (m_AccreditLoginDialog == null) m_AccreditLoginDialog = new AccreditLoginDialog();
		return m_AccreditLoginDialog;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-5-7 14:18:22)
	 * 
	 * @return nc.ui.ic.pub.bill.BarcodeCtrl
	 */
	public BarcodeCtrl getBarcodeCtrl() {
		if (m_BarcodeCtrl == null) {
			m_BarcodeCtrl = new BarcodeCtrl();
			m_BarcodeCtrl.m_sCorpID = m_sCorpID;
		}
		return m_BarcodeCtrl;
	}

	/**
	 * 创建者：仲瑞庆 功能： 参数： 返回： 例外： 日期：(2002-1-24 11:35:23) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return boolean
	 */
	/**
	 * 返回 BillCardPanel1 特性值。
	 * 
	 * @return nc.ui.pub.bill.BillCardPanel
	 */
	/* 警告：此方法将重新生成。 */
	public BarCodeDlg getBarCodeDlg() {
		m_dlgBarCodeEdit = new BarCodeDlg(this, m_sCorpID);
		return m_dlgBarCodeEdit;
	}

	/**
	 * 此处插入方法说明。 用于子类对Condition的个性化修改设置 的重载方法 创建日期：(2003-11-25 20:58:54)
	 */
	protected void getConDlginitself(QueryConditionDlgForBill queryCondition) {
	}

	/**
	 * 此处插入方法说明。 功能：得到标准文件对话框 参数：标准文件对话框 返回：无 例外： 日期：(2002-9-24 15:47:21)
	 * 修改日期，修改人，修改原因，注释标志：
	 */
	public UIFileChooser getFileChooseDlg() {
		try {
			// m_InFileDialog = null;
			if (m_InFileDialog == null) {
				m_InFileDialog = new nc.ui.ic.pub.tools.FileChooserImpBarcode();
				m_InFileDialog.setDialogType(UIFileChooser.OPEN_DIALOG);
				// m_InFileDialog.setFileSelectionMode(UIFileChooser.FILES_ONLY);
				m_InFileDialog.removeChoosableFileFilter(m_InFileDialog.getFileFilter());
				// 移去当前的文件过滤器
				// m_InFileDialog.addChoosableFileFilter(new
				// nc.ui.pf.export.SuffixFilter());
				// 添加文件选择过滤器

				m_InFileDialog.setCurrentDirectory(new java.io.File("D:\\"));
				m_InFileDialog.setFileFilter(new javax.swing.filechooser.FileFilter() {
					public boolean accept(java.io.File f) {
						return f.isDirectory() || f.getName().toLowerCase().endsWith(".xls");
					}

					public String getDescription() {
						return nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000495")/*
																											* @res
																											* "Excel文件"
																											*/;
					}
				});
			}
		} catch (Exception ex) {
			nc.vo.scm.pub.SCMEnv.error(ex);
		}
		return m_InFileDialog;
	}

	/**
	 * 此处插入方法说明。 功能描述:获得 BillFormulaContainer 作者：韩卫 输入参数: 返回值: 异常处理:
	 * 日期:(2003-7-2 9:48:12)
	 * 
	 * @return nc.ui.ic.pub.BillFormulaContainer
	 */
	public BillFormulaContainer getFormulaBillContainer() {
		if (m_billFormulaContain == null) {
			m_billFormulaContain = new BillFormulaContainer(getBillListPanel());
		}
		return m_billFormulaContain;
	}

	/**
	 * 此处插入方法说明。 功能描述: 作者：王乃军 输入参数: 返回值: 异常处理: 日期:(2003-6-25 20:43:17)
	 * 
	 * @return java.util.ArrayList
	 */
	protected ArrayList getFormulaItemBody() {
		String sCusterNameFields = "custname";
		// if (m_bShowCusterShortName)
		// sCusterNameFields="custshortname";
		// else
		// sCusterNameFields="custname";

		ArrayList arylistItemField = new ArrayList();

		// 项目基本档案ID
		String[] aryItemField11 = new String[] {
				"pk_jobbasfil", "pk_jobbasfil", "cprojectid"
		};
		arylistItemField.add(aryItemField11);

		// 项目名称
		String[] aryItemField12 = new String[] {
				"jobname", "cprojectname", "pk_jobbasfil"
		};
		arylistItemField.add(aryItemField12);

		// 项目阶段基本档案ID
		String[] aryItemField13 = new String[] {
				"pk_jobphase", "pk_jobphase", "cprojectphaseid"
		};
		arylistItemField.add(aryItemField13);

		// 项目阶段名称
		String[] aryItemField14 = new String[] {
				"jobphasename", "cprojectphasename", "pk_jobphase"
		};
		arylistItemField.add(aryItemField14);

		// 辅助计量单位
		String[] aryItemField31 = new String[] {
				"measname", "castunitname", "castunitid"
		};
		arylistItemField.add(aryItemField31);

		// 客商基本档案 for 供应商
		String[] aryItemField7 = new String[] {
				"pk_cubasdoc", "pk_cubasdoc", "cvendorid"
		};
		arylistItemField.add(aryItemField7);

		// 客商名称 for 供应商
		String[] aryItemField8 = new String[] {
				sCusterNameFields, "vvendorname", "pk_cubasdoc"
		};
		arylistItemField.add(aryItemField8);

		// zhy2005-09-16客商名称 for 供应商
		String[] aryItemField88 = new String[] {
				sCusterNameFields, "cvendorname", "pk_cubasdoc"
		};
		arylistItemField.add(aryItemField88);

		// 客商基本档案 for 客户||收货单位
		String[] aryItemField17 = new String[] {
				"pk_cubasdoc", "pk_cubasdocrev", "creceieveid"
		};
		arylistItemField.add(aryItemField17);

		// 客商名称 for 客户||收货单位
		String[] aryItemField18 = new String[] {
				sCusterNameFields, "vrevcustname", "pk_cubasdocrev"
		};
		arylistItemField.add(aryItemField18);

		// 来源单据类型
		String[] aryItemField9 = new String[] {
				"billtypename", "csourcetypename", "csourcetype"
		};
		arylistItemField.add(aryItemField9);

		// 成本对象 基本档案
		// ccostobjectbasid
		String[] aryItemField34 = new String[] {
				"pk_invbasdoc", "ccostobjectbasid", "ccostobject"
		};
		arylistItemField.add(aryItemField34);
		// 成本对象
		String[] aryItemField33 = new String[] {
				"invname", "ccostobjectname", "ccostobjectbasid"
		};
		arylistItemField.add(aryItemField33);

		// 源头单据号
		String[] aryItemField10 = new String[] {
				"billtypename", "cfirsttypename", "cfirsttype"
		};
		arylistItemField.add(aryItemField10);
		// //部门
		String[] aryItemField19 = new String[] {
				"deptname", "vdeptname", "cdptid"
		};
		arylistItemField.add(aryItemField19);

		// 货位
		String[] aryItemField20 = new String[] {
				"csname", "vspacename", "cspaceid"
		};
		arylistItemField.add(aryItemField20);

		// 对应入库单类型 ccorrespondtype
		// String[] aryItemField20 =
		// new String[] { "billtypename", "cfirsttypeName", "cfirsttype" };
		// arylistItemField.add(aryItemField20);
		return arylistItemField;
	}

	/**
	 * 此处插入方法说明。 功能描述: 作者：王乃军 输入参数: 返回值: 异常处理: 日期:(2003-6-25 20:43:17)
	 * 
	 * @return java.util.ArrayList
	 */
	protected ArrayList getFormulaItemHeader() {
		ArrayList arylistItemField = new ArrayList();
		// 原有的公式
		// 库存组织 1
		String[] aryItemField40 = new String[] {
				"bodyname", "vcalbodyname", "pk_calbody"
		};
		arylistItemField.add(aryItemField40);

		// 库房管理员 2
		String[] aryItemField3 = new String[] {
				"psnname", "cwhsmanagername", "cwhsmanagerid"
		};
		arylistItemField.add(aryItemField3);

		// 仓库 3
		String[] aryItemField15 = new String[] {
				"storname", "cwarehousename", "cwarehouseid"
		};
		arylistItemField.add(aryItemField15);

		// //仓库是否是否直运仓库
		String[] aryItemField41 = new String[] {
				"isdirectstore", "isdirectstore", "cwarehouseid"
		};
		arylistItemField.add(aryItemField41);

		// 仓库 3
		String[] aryItemField25 = new String[] {
				"storname", "cwastewarehousename", "cwastewarehouseid"
		};
		arylistItemField.add(aryItemField25);

		// 记账人 4
		String[] aryItemField2 = new String[] {
				"user_name", "cregistername", "cregister"
		};
		arylistItemField.add(aryItemField2);

		// //审批人 5
		String[] aryItemField12 = new String[] {
				"user_name", "cauditorname", "cauditorid"
		};
		arylistItemField.add(aryItemField12);

		// //操作员 6
		String[] aryItemField1 = new String[] {
				"user_name", "coperatorname", "coperatorid"
		};
		arylistItemField.add(aryItemField1);

		// 部门 7
		String[] aryItemField19 = new String[] {
				"deptname", "cdptname", "cdptid"
		};
		arylistItemField.add(aryItemField19);

		// 业务员 8
		String[] aryItemField13 = new String[] {
				"psnname", "cbizname", "cbizid"
		};
		arylistItemField.add(aryItemField13);

		// 客商基本档案 for 供应商 9
		String[] aryItemField7 = new String[] {
				"pk_cubasdoc", "pk_cubasdoc", "cproviderid"
		};
		arylistItemField.add(aryItemField7);

		// 客商名称 for 供应商 9
		String[] aryItemField8 = new String[] {
				"custname", "cprovidername", "pk_cubasdoc"
		};
		arylistItemField.add(aryItemField8);
		// 客商名称 for 供应商简称
		String[] aryItemField81 = new String[] {
				"custshortname", "cprovidershortname", "pk_cubasdoc"
		};
		arylistItemField.add(aryItemField81);

		// 客商基本档案 for 客户 10
		String[] aryItemField5 = new String[] {
				"pk_cubasdoc", "pk_cubasdocC", "ccustomerid"
		};
		arylistItemField.add(aryItemField5);

		// 客商名称 for 客户 10
		String[] aryItemField6 = new String[] {
				"custname", "ccustomername", "pk_cubasdocC"
		};
		arylistItemField.add(aryItemField6);
		// 客商名称 for 客户简称
		String[] aryItemField61 = new String[] {
				"custshortname", "ccustomershortname", "pk_cubasdocC"
		};
		arylistItemField.add(aryItemField61);

		// //收发类型 11
		String[] aryItemField18 = new String[] {
				"rdname", "cdispatchername", "cdispatcherid"
		};
		arylistItemField.add(aryItemField18);

		// 业务类型 12
		String[] aryItemField17 = new String[] {
				"businame", "cbiztypename", "cbiztype"
		};
		arylistItemField.add(aryItemField17);

		// 新增加的公式
		// //发运方式 13
		String[] aryItemField42 = new String[] {
				"sendname", "cdilivertypename", "cdilivertypeid"
		};
		arylistItemField.add(aryItemField42);

		// for 来料加工入库单：加工品
		// 基本档案
		// pk_invbasdoc 14
		String[] aryItemField20 = new String[] {
				"pk_invbasdoc", "pk_invbasdoc", "cinventoryid"
		};
		arylistItemField.add(aryItemField20);

		// 名称 14
		String[] aryItemField21 = new String[] {
				"invname", "cinventoryname", "pk_invbasdoc"
		};
		arylistItemField.add(aryItemField21);

		return arylistItemField;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-4-8 11:13:07)
	 * 
	 * @return nc.ui.ic.pub.bill.GeneralBillUICtl
	 */
	public GeneralBillUICtl getGenBillUICtl() {
		if (m_GenBillUICtl == null) m_GenBillUICtl = new GeneralBillUICtl();
		return m_GenBillUICtl;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-7-11 下午 11:19)
	 * 
	 * @return nc.ui.ic.pub.InvOnHandDialog
	 */
	protected LocSelDlg getLocSelDlg() {
		if (null == m_dlgLocSel) {
			m_dlgLocSel = new LocSelDlg(this);
			m_dlgLocSel.setCorpID(m_sCorpID);
		}
		return m_dlgLocSel;
	}

	/**
	 * 此处插入方法说明。 功能描述: 单据查询条件构造; 借入、借出单据可以重新构造该方法,而不必重载onquery方法 输入参数: 返回值:
	 * 异常处理: 日期:
	 * 
	 * @return nc.vo.ic.pub.bill.QryConditionVO
	 */
	protected QryConditionVO getQryConditionVO() {
		// 添加查询
		nc.vo.pub.query.ConditionVO[] voaCond = getConditionDlg().getConditionVO();
		// 处理跨公司部门业务员条件
		voaCond = nc.ui.ic.pub.tools.GenMethod.procMultCorpDeptBizDP(voaCond, getBillTypeCode(), getCorpPrimaryKey());
		// 过滤null为 is null 或 is not null add by hanwei 2004-03-31.01
		nc.ui.ic.pub.report.IcBaseReportComm.fixContionVONullsql(voaCond);
		QryConditionVO voCond = new QryConditionVO(" head.cbilltypecode='" + m_sBillTypeCode + "' AND " + getExtendQryCond(voaCond));

		return voCond;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-3-12 21:25:15)
	 * 
	 * @return nc.ui.ic.pub.setpart.SetPartDlg
	 */
	protected SetPartDlg getSetpartDlg() {
		if (m_SetpartDlg == null) {
			m_SetpartDlg = new SetPartDlg(this);
		}
		return m_SetpartDlg;
	}

	/**
	 * 简单初始化类。按传入参数，不读环境设置的操作员，公司等。
	 */
	/* 警告：此方法将重新生成。 */
	protected void initialize(String pk_corp, String sOperatorid, String sOperatorname, String sBiztypeid, String sGroupid, String sLogDate) {

		m_sUserID = sOperatorid;
		m_sGroupID = sGroupid; // 集团
		m_sUserName = sOperatorname;
		m_sCorpID = pk_corp;
		m_sLogDate = sLogDate;

		try {
			// 界面管理器
			m_layoutManager = new ToftLayoutManager(this);
			// user code begin {1}
			// 抽象方法，初始化参数
			initPanel();
			getButtonTree().getButton(ICButtonConst.BTN_LINE_BARCODE).setEnabled(false);
			// 上下翻页的控制
			m_pageBtn = new PageCtrlBtn(this);
			// 初始化按钮
			// initButtons();
			// 读系统参数
			initSysParam();
			// 初始化缺省菜单。
			// initButtonsData();

			// 支持扩展类和按钮
			addExtendedButtons();

			// user code end
			setName("ClientUI");

			// ------------- 设置精度 -----------
			setScaleOfCardPanel(getBillCardPanel());
			setScaleOfListPanel();
			// 设置单价、换算率等>0
			getGenBillUICtl().setValueRange(getBillCardPanel(), new String[] {
					"nprice", "hsl", "nsaleprice", "ntaxprice", "nquoteunitrate"
			}, 0, nc.vo.scm.pub.bill.SCMDoubleScale.MAXVALUE);
			// 设置菜单
			setButtons();

			// 初始化编辑前控制器
			getBillCardPanel().getBillModel().setCellEditableController(this);

		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}

		// 初始化为不可编辑。
		getBillCardPanel().setEnabled(false);
		m_iMode = BillMode.Browse;
		// 初始化显示表单形式。
		m_iCurPanel = BillMode.Card;

		getButtonTree().getButton(ICButtonConst.BTN_SWITCH).setHint(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000068")/* @res "切换到列表形式" */);

		getBillListPanel().addEditListener(this);
		getBillListPanel().getChildListPanel().addEditListener(this);
		getBillCardPanel().addEditListener(this);

		// 暂时不加，否则表体全可编辑。
		getBillCardPanel().addBodyEditListener2(this);

		getBillCardPanel().setBillBeforeEditListenerHeadTail(this);

		getBillCardPanel().getBillModel().addTableModelListener(this);

		// 排序 监听表头和表头的排序
		m_listHeadSortCtl = new ClientUISortCtl(this, false, BillItem.HEAD);
		getBillListPanel().getHeadBillModel().addBillSortListener2(this);
		m_listBodySortCtl = new ClientUISortCtl(this, false, BillItem.BODY);

		getBillCardPanel().getBillTable().addSortListener();
		m_cardBodySortCtl = new ClientUISortCtl(this, true, BillItem.BODY);

		getBillCardPanel().setAutoExecHeadEditFormula(true);

		// 合计列
		getBillCardPanel().getBillModel().addTotalListener(this);

		// getBillListPanel().getHead().addTableModelListener(this);
		getBillListPanel().getBodyTable().getModel().addTableModelListener(this);

		getBillListPanel().addMouseListener(this);

		getBillCardPanel().addBodyMenuListener(this);
		// 过滤参照显示
		filterRef(m_sCorpID);
		// 设置汇总列
		setTotalCol();
		// 在retBusinessBtn前调用。
		setButtonStatus(true);

		// 如果有单据参照
		if (m_bNeedBillRef) {
			// 读取业务类型
			nc.ui.pub.pf.PfUtilClient.retBusinessBtn(getButtonTree().getButton(ICButtonConst.BTN_BUSINESS_TYPE), m_sCorpID, m_sBillTypeCode);
			getButtonTree().getButton(ICButtonConst.BTN_BUSINESS_TYPE).setCheckboxGroup(true);

		}

		if (getButtonTree().getButton(ICButtonConst.BTN_BUSINESS_TYPE) != null && getButtonTree().getButton(ICButtonConst.BTN_BUSINESS_TYPE).getChildButtonGroup() != null && getButtonTree().getButton(ICButtonConst.BTN_BUSINESS_TYPE).getChildButtonGroup().length > 0 && getButtonTree().getButton(ICButtonConst.BTN_BUSINESS_TYPE).getChildButtonGroup()[0] != null) {
			getButtonTree().getButton(ICButtonConst.BTN_BUSINESS_TYPE).getChildButtonGroup()[0].setSelected(true);
			onJointAdd(getButtonTree().getButton(ICButtonConst.BTN_BUSINESS_TYPE).getChildButtonGroup()[0]);
		}

		showBtnSwitch();
		m_layoutManager.show();
		
		// add by zip:2013/12/18 No 117
		getBillCardPanel().getBodyItem("dbizdate").setDefaultValue(ClientEnvironment.getInstance().getDate().toString());
		// end

		// 修改人：刘家清 修改日期：2007-12-26上午11:05:02 修改原因：右键增加"重排行号"
		UIMenuItem[] oldUIMenuItems = getBillCardPanel().getBodyMenuItems();
		if (oldUIMenuItems.length > 0) {
			ArrayList<UIMenuItem> newMenuList = new ArrayList<UIMenuItem>();
			for (UIMenuItem oldUIMenuItem : oldUIMenuItems)
				newMenuList.add(oldUIMenuItem);
			newMenuList.add(getAddNewRowNoItem());
			getAddNewRowNoItem().removeActionListener(this);
			getAddNewRowNoItem().addActionListener(this);
			UIMenuItem[] newUIMenuItems = new UIMenuItem[newMenuList.size()];
			m_Menu_AddNewRowNO_Index = newMenuList.size() - 1;
			newMenuList.toArray(newUIMenuItems);
			// getBillCardPanel().setBodyMenu(newUIMenuItems);
			getBillCardPanel().getBodyPanel().setMiBody(newUIMenuItems);
			getBillCardPanel().getBodyPanel().setBBodyMenuShow(true);
			getBillCardPanel().getBodyPanel().addTableBodyMenu();
		}

	}

	/**
	 * 
	 * 方法功能描述：增加二次开发的按钮到单据按钮中。从而达到支持二次开发自定义按钮的目录。
	 * <p>
	 * <b>参数说明</b>
	 * <p>
	 * 
	 * @author duy
	 * @throws BusinessException
	 * @time 2007-3-27 下午05:14:43
	 */
	private void addExtendedButtons() throws BusinessException {
		IFuncExtend funcExtend = nc.ui.scm.extend.FuncExtendInfo.getFuncExtendInstance(m_sBillTypeCode);

		// "二次开发"顶级按钮
		ButtonObject boExtTop = null;

		if (funcExtend != null) {
			boExtTop = getButtonTree().getExtTopButton();
			getButtonTree().addMenu(boExtTop);
			ButtonObject[] boExtend = funcExtend.getExtendButton();
			if (boExtend != null) for (int i = 0; i < boExtend.length; i++)
				if (boExtend[i] == null) getButtonTree().addChildMenu(boExtTop, boExtend[i]);
		}

		ButtonObject[] boExtendnew = getExtendBtns();
		if (boExtendnew != null && boExtendnew.length > 0) {
			if (boExtTop == null) {
				boExtTop = getButtonTree().getExtTopButton();
				getButtonTree().addMenu(boExtTop);
			}

			for (int i = 0; i < boExtendnew.length; i++)
				if (boExtendnew[i] != null) getButtonTree().addChildMenu(boExtTop, boExtendnew[i]);
		}
	}

	/**
	 * V5：参照生成多张单据时卡片界面初始化处理
	 * 
	 */
	// public void setRefBillsInit() {
	// getBillCardPanel().addNew();
	// // 设置新增单据的初始数据，如日期，制单人等。
	// setNewBillInitData();
	// getBillCardPanel().setEnabled(true);
	// m_iMode = BillMode.New;
	//		
	// //货位序列号数据处理（暂略）
	//		
	//		
	// //addRowNums(m_iInitRowCount);
	//		
	// // 设置单据号是否可编辑
	// if (getBillCardPanel().getHeadItem("vbillcode") != null)
	// getBillCardPanel().getHeadItem("vbillcode").setEnabled(
	// m_bIsEditBillCode);
	// getBillCardPanel().setTailItem("iprintcount", new Integer(0));
	//
	// // 需要初始化参照过滤, 为点击取消后的新增操作. 20021225
	// //filterRef(m_sCorpID);
	//		
	// //默认情况下，退库状态不可以用 add by hanwei 2003-10-19 //v5 可能需要修改退库控制lj
	// nc.ui.ic.pub.bill.GeneralBillUICtl
	// .setSendBackBillState(this, false);
	//
	// // 默认不是导入数据 add by hanwei 2003-10-30
	// m_bIsImportData = false;
	//
	// // 设置当前的条码框的条码 韩卫 2004-04-05
	// if (m_utfBarCode != null)
	// m_utfBarCode.setCurBillItem(null);
	//
	// }
	/**
	 * 创建者：王乃军 功能：新增处理 参数： 返回： 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 */
	public void onAdd(boolean bUpataBotton, GeneralBillVO voBill) {

		// 当前是列表形式时，首先切换到表单形式,[v5]如果是参照多张生成，不切换，切换在调用onSwitch时执行
		if (BillMode.List == m_iCurPanel && !m_bRefBills) onSwitch();

		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH028")/* @res "正在增加" */);
		// 新增
		try {

			if (voBill == null) {
				m_voBill = new GeneralBillVO();
				getBillCardPanel().updateValue();
				getBillCardPanel().addNew();
				getBillCardPanel().getBillModel().clearBodyData();
			}
			// 设置新增单据的初始数据，如日期，制单人等。
			setNewBillInitData();
			getBillCardPanel().setEnabled(true);
			m_iMode = BillMode.New;

			if (bUpataBotton && voBill == null) setButtonStatus(true);

			// long lTime = System.currentTimeMillis();

			// 保存后清货位数据
			m_alLocatorDataBackup = m_alLocatorData;

			m_alLocatorData = null;
			// 保存后清序列号数据
			m_alSerialDataBackup = m_alSerialData;
			m_alSerialData = null;

			// v5
			if (voBill == null) addRowNums(m_iInitRowCount);

			// 显示表体右键按钮，并可用。
			if (getBillCardPanel().getBodyMenuItems() != null) for (int i = 0; i < getBillCardPanel().getBodyMenuItems().length; i++)
				getBillCardPanel().getBodyMenuItems()[i].setEnabled(true);

			// 20050519 dw 在途单右键的行维护功能应封掉 getBillTypeCode() !="40080620"
			if (getBillTypeCode() != "40080620") {
				getBillCardPanel().setBodyMenuShow(true);
			} else {
				getBillCardPanel().setBodyMenuShow(false);
			}

			// 设置单据号是否可编辑
			if (getBillCardPanel().getHeadItem("vbillcode") != null) getBillCardPanel().getHeadItem("vbillcode").setEnabled(m_bIsEditBillCode);
			getBillCardPanel().setTailItem("iprintcount", new Integer(0));

			// 需要初始化参照过滤, 为点击取消后的新增操作. 20021225
			filterRef(m_sCorpID);

			// zhx add 如果表体没有行则不设置行号.
			if (voBill == null && getBillCardPanel().getBillModel().getRowCount() != 0) nc.ui.scm.pub.report.BillRowNo.addNewRowNo(getBillCardPanel(), m_sBillTypeCode, m_sBillRowNo);

			// 默认情况下，退库状态不可以用 add by hanwei 2003-10-19 //v5 可能需要修改退库控制lj
			nc.ui.ic.pub.bill.GeneralBillUICtl.setSendBackBillState(this, false);

			// 默认不是导入数据 add by hanwei 2003-10-30
			m_bIsImportData = false;

			// 设置当前的条码框的条码 韩卫 2004-04-05
			if (m_utfBarCode != null) m_utfBarCode.setCurBillItem(null);

		} catch (Exception e) {
			handleException(e);
		}
	}

	/**
	 * 创建者：审核中的核心方法 功能：确认（保存）处理 参数：无 例外： 日期：(2004-4-1 9:23:32)
	 * 修改日期，修改人，修改原因，注释标志： 2004-4-1 韩卫
	 */
	public void onAuditKernel(GeneralBillVO voBill) throws Exception {
		nc.ui.pub.pf.PfUtilClient.processBatch("SIGN", m_sBillTypeCode, m_sLogDate, new GeneralBillVO[] {
			voBill
		});
	}

	/**
	 * 条码编辑按钮点击事件相应方法 创建日期：(2003-09-28 9:51:50)
	 */
	public void onBarCodeEdit() {
		// 判断是否能够进行条码编辑
		GeneralBillVO voBill = null;

		int iCurFixLine = 0;
		// 是否可以编辑
		boolean bDirectSave = false;
		if (m_iMode == BillMode.Browse || !m_bAddBarCodeField) {
			bDirectSave = true;
		} else {
			bDirectSave = false;
		}

		if (BillMode.List == m_iCurPanel) {
			bDirectSave = false;
			showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000071")/* @res "请在卡片模式下编辑条码" */);
			return;
		} else {
			voBill = m_voBill;
			iCurFixLine = getBillCardPanel().getBillTable().getSelectedRow();
		}

		// 滤去表单形式下的空行
		filterNullLine();
		if (getBillCardPanel().getRowCount() <= 0) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000072")/* @res "请输入表体行!" */);
			getBillCardPanel().addLine();
			nc.ui.scm.pub.report.BillRowNo.addLineRowNo(getBillCardPanel(), m_sBillTypeCode, m_sBillRowNo);
			return;
		}
		// 检查行号的合法性; 该方法应放在过滤空行的后面。
		// 需要按行号确定唯一行
		if (!nc.ui.scm.pub.report.BillRowNo.verifyRowNosCorrect(getBillCardPanel(), m_sBillRowNo)) { return; }
		boolean bEditable = true;

		m_dlgBarCodeEdit = getBarCodeDlg(bEditable, bDirectSave);

		if (voBill != null && iCurFixLine >= 0 && iCurFixLine < voBill.getItemCount()) {

			GeneralBillItemVO itemvo = voBill.getItemVOs()[iCurFixLine];
			// 是条码管理的存货
			if (itemvo.getBarcodeManagerflag().booleanValue()) {
				// 得到表头的单据号, 表体行号, 存货名称,存货编码
				// ArrayList altemp = new ArrayList();

				// 新增情况下，行号没有在m_voBill中存在,这里设置行号
				getGenBillUICtl().setBillCrowNo(voBill, getBillCardPanel());

				// 获得条码管理的Items

				ArrayList alReuslt = getBarcodeCtrl().getCurBarcodeItems(voBill, iCurFixLine);
				if (alReuslt == null || alReuslt.size() < 2) { return; }
				GeneralBillItemVO[] itemBarcodeVos = (GeneralBillItemVO[]) alReuslt.get(0);
				int iFixLine = ((Integer) alReuslt.get(1)).intValue();
				GeneralBillHeaderVO headervo = voBill.getHeaderVO();
				m_dlgBarCodeEdit.setHeaderItemvo(headervo);
				m_dlgBarCodeEdit.m_sNumItemKey = m_sNumItemKey;
				m_dlgBarCodeEdit.m_sShouldNumItemKey = m_sShouldNumItemKey;
				m_dlgBarCodeEdit.m_iBarcodeUIColorRow = m_iBarcodeUIColorRow;
				m_dlgBarCodeEdit.setColor(m_sColorRow);
				m_dlgBarCodeEdit.setScale(m_ScaleValue.getScaleValueArray());

				// 将条码是否保存参数设置到条码编辑界面，便于在编辑界面保存条码前控制
				m_dlgBarCodeEdit.setSaveBarCode(m_bBarcodeSave);
				m_dlgBarCodeEdit.setSaveBadBarCode(m_bBadBarcodeSave);
				// 修改人:刘家清 修改日期:2007-04-10
				// 修改原因:逻辑不对,对于一个单据,是否保存条码是用bSaveBarcodeFinal
				if (voBill.bSaveBarcodeFinal()) m_dlgBarCodeEdit.setSaveBadBarCode(true);
				// 如果条码关闭，设置条码编辑框不能编辑
				boolean bbarcodeclose = itemvo.getBarcodeClose().booleanValue();
				m_dlgBarCodeEdit.setUIEditableBarcodeClose(!bbarcodeclose);
				m_dlgBarCodeEdit.setUIEditable(m_iMode);
				// 设置到Items中
				m_dlgBarCodeEdit.setCurBarcodeItems(itemBarcodeVos, iFixLine);

				if (m_dlgBarCodeEdit.showModal() == nc.ui.pub.beans.UIDialog.ID_OK) {
					getBillCardPanel().getBillModel().setNeedCalculate(false);
					// 设置条码框数据
					m_utfBarCode.setCurBillItem(itemBarcodeVos);
					// 还有要设置条码框的删除数据？m_utfBarCode.setRemoveBarcode(m_dlgBarCodeEdit.getBarCodeDelofAllVOs());

					// 目的设置m_billvo的条码数据，修改卡片界面状态

					setBarCodeOnUI(voBill, itemBarcodeVos);

					Hashtable htbItemBarcodeVos = getBarcodeCtrl().getHtbItemBarcodeVos(itemBarcodeVos);

					if (htbItemBarcodeVos != null && htbItemBarcodeVos.size() > 0) {

						GeneralBillItemVO billItemTemp = null;
						if (htbItemBarcodeVos != null) {
							GeneralBillItemVO[] billItemsAll = (GeneralBillItemVO[]) voBill.getChildrenVO();
							String sRowNo = null;
							for (int i = 0; i < billItemsAll.length; i++) {
								sRowNo = billItemsAll[i].getCrowno();
								if (sRowNo != null && htbItemBarcodeVos.containsKey(sRowNo)) {
									billItemTemp = (GeneralBillItemVO) htbItemBarcodeVos.get(sRowNo);

									onBarCodeEditUpdateBill(i, billItemTemp);

								}

							}

							if (!m_dlgBarCodeEdit.m_bModifyBillUINum) { // 修改卡片界面状态
								showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000073")/*
																																* @res
																																* "条码编辑框的参数设置是不修改界面数量，单据界面实际数量不被修改！"
																																*/);
							}

							if (!getBarcodeCtrl().isModifyBillUINum()) {
								showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000074")/*
																																* @res
																																* "当前单据界面不允许修改通过条码数量修改实际数量，单据界面实际数量不被修改！"
																																*/);

							}

							if (m_dlgBarCodeEdit.m_bModifyBillUINum && getBarcodeCtrl().isModifyBillUINum()) { // 修改卡片界面状态
								showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000075")/*
																																* @res
																																* "条码编辑框的参数设置是修改界面数量并且界面规则允许修改实际数量，单据界面实际数量已经被修改！"
																																*/);
							}

						}

					}
					// dw
					resetSpace(iCurFixLine);

					getBillCardPanel().getBillModel().setNeedCalculate(true);
					getBillCardPanel().getBillModel().reCalcurateAll();

				}

			} else {// 修改人:刘家清 修改日期:2007-04-05 修改原因:不是存货管理的物品
				nc.ui.ic.pub.tools.GenMethod.handleException(this, null, new BusinessException(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000002")/*
																																											* @res
																																											* "下列存货非主条码管理或次条码管理，请修改存货管理档案的属性："
																																											*/
						+ itemvo.getCinventorycode()));
			}

		} else {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000356")/* @res "请选择表体行！" */);
		}
	}

	/**
	 * ?user> 功能： 参数： 返回： 例外： 日期：(2004-5-24 15:54:15) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @param iCurFixLine
	 *            int
	 * @param billItemvo
	 *            nc.vo.ic.pub.bill.GeneralBillItemVO
	 */
	public void onBarCodeEditUpdateBill(int iCurFixLine, GeneralBillItemVO billItemvo) {

		boolean bNegative = false; // 是否负数
		// 修改实发数量和应发数量
		UFDouble ufdNum = null;
		Object oTemp = getBillCardPanel().getBodyValueAt(iCurFixLine, m_sNumItemKey);
		if (oTemp == null) {
			ufdNum = UFDZERO;
		} else {
			ufdNum = (UFDouble) oTemp;
		}

		UFDouble ufdShouldNum = null;
		Object oShouldTemp = getBillCardPanel().getBodyValueAt(iCurFixLine, m_sNumItemKey);
		if (oShouldTemp == null) {
			ufdShouldNum = UFDZERO;
		} else {
			ufdShouldNum = (UFDouble) oShouldTemp;
		}
		if (ufdNum.doubleValue() < 0 || ufdShouldNum.doubleValue() < 0) {
			bNegative = true;
		}

		// 删除的数据
		// UFDouble ufdZero = UFDZERO;
		UFDouble ufdNumDlg = UFDZERO;
		nc.vo.ic.pub.bc.BarCodeVO[] barcodevosAll = billItemvo.getBarCodeVOs();

		if (barcodevosAll != null) {
			for (int n = 0; n < barcodevosAll.length; n++) {
				if (barcodevosAll[n] != null && barcodevosAll[n].getStatus() != nc.vo.pub.VOStatus.DELETED) {
					ufdNumDlg = ufdNumDlg.add(barcodevosAll[n].getNnumber());
				}
			}
		}
		// 大于应发数量，而又控制不能超应发数量：盘点调整
		if (ufdNumDlg.doubleValue() > ufdShouldNum.doubleValue() && !getBarcodeCtrl().isOverShouldNum()) {
			ufdNumDlg = ufdShouldNum.abs();
		}

		// 转换为负数
		if (m_bFixBarcodeNegative || bNegative) ufdNumDlg = ufdNumDlg.multiply(UFDNEGATIVE);

		if (ufdNumDlg == null) ufdNumDlg = UFDZERO;

		// 设置条码数量字段
		try {
			getBillCardPanel().setBodyValueAt(ufdNumDlg.abs(), iCurFixLine, m_sNumbarcodeItemKey);

		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
		}
		// 是否修改单据数量要根据下面的情况判断
		if (m_dlgBarCodeEdit.m_bModifyBillUINum && getBarcodeCtrl().isModifyBillUINum() && m_iMode != BillMode.Browse) {
			// 修改卡片界面状态

			getBillCardPanel().setBodyValueAt(ufdNumDlg, iCurFixLine, m_sNumItemKey);

			nc.ui.pub.bill.BillEditEvent event1 = new nc.ui.pub.bill.BillEditEvent(getBillCardPanel().getBodyItem(m_sNumItemKey), ufdNumDlg, m_sNumItemKey, iCurFixLine);
			afterNumEdit(event1);
			// 执行模版公式
			getGenBillUICtl().execEditFormula(getBillCardPanel(), iCurFixLine, m_sNumItemKey);
			// 触发单据行状态为修改
			if (getBillCardPanel().getBodyValueAt(iCurFixLine, m_sBillRowItemKey) != null) getBillCardPanel().getBillModel().setRowState(iCurFixLine, BillMode.Update);

		}
	}

	/**
	 * \n创建日期：(2003-3-6 15:13:32) 作者：余大英 修改日期： 修改人： 修改原因： 算法说明：
	 */
	public void onDocument() {
		ArrayList alBill = getSelectedBills();
		if (alBill == null || alBill.size() == 0) {
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000076")/* @res "请先选择单据！" */);
			return;
		}
		String[] spk = new String[alBill.size()];
		String[] scode = new String[alBill.size()];
		GeneralBillVO vo = null;
		GeneralBillHeaderVO voHead = null;
		for (int i = 0; i < alBill.size(); i++) {
			vo = (GeneralBillVO) alBill.get(i);
			if (vo != null) {
				voHead = (GeneralBillHeaderVO) vo.getParentVO();
				if (voHead != null) {
					spk[i] = voHead.getCgeneralhid();
					scode[i] = voHead.getVbillcode();
				}

			}
		}
		DocumentManager.showDM(this, spk, scode);
	}

	public void onMergeShow() {
		if (m_iCurPanel == BillMode.Card) {
			if (m_voBill == null) return;
		} else {
			if (m_alListData == null || m_alListData.size() == 0) return;
		}

		CollectSettingDlg dlg = new CollectSettingDlg(this, ResBase.getBtnMergeShowName()/* @res "合并显示" */);
		dlg.initData(getBillCardPanel(), new String[] {
				"cinventorycode", "invname", "invspec", "invtype"
		}, // 固定分组列
				null,// 缺省分组列
				new String[] {
						"nshouldinnum", "nneedinassistnum", "nshouldoutnum", "nshouldoutassistnum", "ninnum", "ninassistnum", "noutnum", "noutassistnum", "ningrossnum", "noutgrossnum", "nmny", "nplannedmny", "ntarenum"
				},// 求和列
				null,// 求平均列
				null,// 求加权平均列
				null// 数量列
		);
		dlg.show();

	}

	/**
	 * 此处插入方法说明。 创建日期：(2003-8-25 13:53:58) 由应发（收）自动填写实发（收）
	 */
	private void onFillNum() {
		// 辅数量
		// if (getBillCardPanel().getBodyItem(m_sShouldAstItemKey) != null
		// && getBillCardPanel().getBodyItem(m_sAstItemKey) != null
		// && getBillCardPanel().getBodyValueAt(
		// getBillCardPanel().getBillTable().getSelectedRow(),
		// m_sShouldAstItemKey) != null)
		// GeneralBillUICtl.fillValue(getBillCardPanel(), this,
		// m_sShouldAstItemKey, m_sAstItemKey);

		// 数量
		if (getBillCardPanel().getBodyItem(m_sShouldNumItemKey) != null && getBillCardPanel().getBodyItem(m_sNumItemKey) != null) GeneralBillUICtl.fillValue(getBillCardPanel(), this, m_sShouldNumItemKey, m_sNumItemKey);
		return;
	}

	/**
	 * 此处插入方法说明。 功能描述:在卡片模式下转向第一张。 作者：程起伍 输入参数: 返回值: 异常处理: 日期:(2003-5-27
	 * 14:47:24)
	 */
	public void onFirst() {
		if (m_alListData != null && m_alListData.size() > 0) {
			int iAll = m_alListData.size();
			scrollBill(0);
			m_pageBtn.first(iAll);
			updateButtons();
		}
	}

	/**
	 * 此处插入方法说明。 导入来源单据：只在新增加、浏览、已经审核情况下可以直接保存导入 创建日期：(2004-4-20 11:36:04)
	 */
	public void onImportBarcodeSourceBill() {
		try {

			if (m_iMode != BillMode.Browse) {
				// 编辑情况下：
				// 滤去表单形式下的空行
				filterNullLine();
				if (getBillCardPanel().getRowCount() <= 0) {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000072")/*
																													* @res
																													* "请输入表体行!"
																													*/);
					return;
				}
				// 新增情况是以行号为索引的，必须校验
				if (!nc.ui.scm.pub.report.BillRowNo.verifyRowNosCorrect(getBillCardPanel(), m_sBillRowNo)) { return; }
			}

			// 执行后台的导入操作，需要判断是在列表下或卡片下
			ArrayList alBill = new ArrayList();
			if (m_iCurPanel == BillMode.Card) { // 浏览
				GeneralBillVO vo = null;
				if (m_iMode == BillMode.Browse) {
					if (m_iLastSelListHeadRow != -1 && null != m_alListData && m_alListData.size() != 0) {
						vo = (GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow);
					} else {
						showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000077")/*
																														* @res
																														* "卡片下没有单据数据不可以导入！"
																														*/);
						return;
					}
				} else {
					vo = m_voBill;
				}
				alBill.add(vo);
			} else if (m_iCurPanel == BillMode.List) { // 列表
				showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000078")/* @res "列表下不可以导入！" */);
				return;
			}
			String sHID = null;
			String sBillTypecode = null;
			if (alBill != null && alBill.size() > 0) {
				GeneralBillVO billVO = null;
				StringBuffer sbErr = new StringBuffer("");
				ArrayList alSourceHID = new ArrayList();
				for (int n = 0; n < alBill.size(); n++) {
					// 校验数据
					billVO = (GeneralBillVO) alBill.get(n);

					if (billVO == null) continue;

					nc.vo.ic.pub.bill.GeneralBillHeaderVO headvo = billVO.getHeaderVO();
					GeneralBillItemVO[] billItemVOs = (GeneralBillItemVO[]) billVO.getChildrenVO();
					if (billVO == null || billVO.getHeaderVO() == null || billVO.getChildrenVO() == null || billVO.getChildrenVO().length == 0) {
						if (m_iCurPanel == BillMode.Card) sbErr.append(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000325")/*
																																					* @res
																																					* "当前单据表体没有数据不可以导入！"
																																					*/);
						continue;
					}

					// 准备数据
					sHID = headvo.getCgeneralhid();
					sBillTypecode = headvo.getCbilltypecode();

					if (sBillTypecode != null && (sBillTypecode.equalsIgnoreCase("4A") || sBillTypecode.equalsIgnoreCase("4I") || sBillTypecode.equalsIgnoreCase("4Y") || sBillTypecode.equalsIgnoreCase("4E") || sBillTypecode.equalsIgnoreCase("4C"))) { // 符合规则：其他出入库单，调拨出入库单，销售出库可以导入单据条码
					} else {
						sbErr.append(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000326")/*
																													* @res
																													* "当前单据表体，非销售出库、其他出入库单或调拨出入库单，不可以导入来源单据条码"
																													*/);
						showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000079")/*
																														* @res
																														* "没有来源单据，不可导入！"
																														*/);
						return;
					}

					java.util.ArrayList alBIDCrowno = new java.util.ArrayList();
					java.util.ArrayList alBIDSourceid = new java.util.ArrayList();
					java.util.Hashtable htbSourceBIDRepeat = new java.util.Hashtable();
					UFBoolean ufbHasHBID = new UFBoolean(true);
					if (sHID == null) {
						// 没有表体ID，直接新增情况，设置false
						ufbHasHBID = new UFBoolean(false);
					}
					String sSourceHID = null;
					// String sFirstHID = null;
					String sCourceTypecode = null; // 来源单据号
					boolean bTranBill = false; // 是否转库

					String sCsourcebillbid = null;
					java.util.ArrayList alRepeatRow = new java.util.ArrayList(); // 重复的
					for (int i = 0; i < billItemVOs.length; i++) {
						// if (i == 0) {
						sCourceTypecode = billItemVOs[i].getCsourcetype();
						if (sCourceTypecode == null || sCourceTypecode.trim().length() == 0) continue;

						// 转库单用来源单据号
						if (sCourceTypecode.startsWith("4")) {
							sSourceHID = billItemVOs[i].getCsourcebillhid();
							bTranBill = true;
						} else // 调拨单据用源头单据好
						{
							sSourceHID = billItemVOs[i].getCfirstbillhid();
							bTranBill = false;
						}
						// }
						if (!alSourceHID.contains(sSourceHID)) alSourceHID.add(sSourceHID);

						if (billItemVOs[i].getCsourcebillbid() != null) {
							if (bTranBill) {
								sCsourcebillbid = billItemVOs[i].getCsourcebillbid();
							} else {
								sCsourcebillbid = billItemVOs[i].getCfirstbillbid();
							}
							alBIDSourceid.add(sCsourcebillbid);
							// 校验重复的来源单据ID
							if (sCsourcebillbid != null) {
								if (htbSourceBIDRepeat.containsKey(sCsourcebillbid)) {
									alRepeatRow.add(billItemVOs[i].getCrowno());
								} else {
									htbSourceBIDRepeat.put(sCsourcebillbid, sCsourcebillbid);
								}
							}

							if (ufbHasHBID.booleanValue()) {
								alBIDCrowno.add(billItemVOs[i].getCgeneralbid());
							} else alBIDCrowno.add(billItemVOs[i].getCrowno());

						}
					}

					if (alBIDSourceid.size() == 0) {
						showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000079")/*
																														* @res
																														* "没有来源单据，不可导入！"
																														*/);
						return;
					}
					// 不允许有重复的来源单据行：
					if (alRepeatRow != null && alRepeatRow.size() > 0) {
						StringBuffer sbError = new StringBuffer();
						String sRowno = null;
						sbError.append(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000080")/*
																													* @res
																													* "存在下列重复的来源单据行，不能导入来源单据，请合并：\n"
																													*/);
						for (int i = 0; i < alRepeatRow.size(); i++) {
							sRowno = (String) alRepeatRow.get(i);
							if (i > 0) sbError.append(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000000")/*
																																		* @res "、"
																																		*/);
							sbError.append(sRowno);
						}
						showErrorMessage(sbError.toString());
						return;
					}

					java.util.ArrayList alCon = new ArrayList();
					alCon.add(sHID);
					// 把行号或BID作为表体ID传到数据中
					alCon.add(alBIDCrowno);
					alCon.add(alBIDSourceid);
					alCon.add(m_sCorpID);
					alCon.add(sBillTypecode);
					alCon.add(ufbHasHBID);
					// alCon.add(sSourceHID);
					alCon.add(alSourceHID);
					alCon.add(sCourceTypecode);

					// 导入条码
					try {
						java.util.ArrayList alResult = nc.ui.ic.pub.bc.BarCodeImportBO_Client.importSourceBarcode(alCon);

						java.util.ArrayList alresult = nc.vo.ic.pub.barcodeparse.BarcodeparseCtrl.setImportBarcode(billVO, alResult);

						if (alresult.size() > 0) {
							StringBuffer sbMsg = new StringBuffer(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000081")/*
																																				* @res
																																				* "单据导入条码结果：\n"
																																				*/);
							for (int i = 0; i < alresult.size(); i++) {
								sbMsg.append((String) alresult.get(i) + "\n");
							}
							showWarningMessage(sbMsg.toString());
						} else {
							showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000082")/*
																															* @res
																															* "单据行没有条码导入！"
																															*/);
							return;
						}
						// 如果当前在浏览情况下，需要直接保存到后台
						// false:前台不作校验，后台校验
						if (m_iMode == BillMode.Browse) {
							onImportSignedBillBarcode(billVO, false);
							showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000083")/*
																															* @res
																															* "导入来源单据完成！条码已经保存完毕。"
																															*/);
						} else {
							showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000084")/*
																															* @res
																															* "导入来源单据条码到当前单据界面上，请点按纽“保存”，保存单据。"
																															*/);
						}

					} catch (Exception e) {
						String[] args = new String[1];
						args[0] = headvo.getVbillcode().toString();
						String message = nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000342", null, args);
						/* @res "单据号为{0}的单据,不能导入来源单据的条码出现异常:" */
						sbErr.append(message);
					}
				}
				String sErrMsg = sbErr.toString();
				if (sErrMsg != null && sErrMsg.trim().length() > 0) {
					showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000044")/*
																													* @res
																													* "错误提示："
																													*/
							+ sErrMsg);
					showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000085")/*
																													* @res
																													* "导入来源单据失败！"
																													*/);

				}

			} else {
				showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000086")/*
																												* @res "错误提示：没有选择的单据"
																												*/);
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000085")/* @res "导入来源单据失败！" */);
			} // 把导入结果放到前台
		} catch (Exception e) {
			String sErrorMsg = e.getMessage();
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000044")/* @res "错误提示：" */
					+ sErrorMsg);
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000085")/* @res "导入来源单据失败！" */);
		}
	}

	/**
	 * 创建者：李俊 功能：（单据审核后）保存导入条码
	 */
	public void onImportSignedBillBarcode(GeneralBillVO voUpdatedBill) throws Exception {

		// 是否检查条码数量和实际数量：true
		onImportSignedBillBarcode(voUpdatedBill, true);

	}

	/**
	 * 创建者：李俊 功能：单据未签字浏览状态下导入条码
	 */
	public ArrayList onImportSignedBillBarcodeKernel(GeneralBillVO voBill, GeneralBillVO voUpdatedBill) throws Exception {
		voBill.setAccreditUserID(voUpdatedBill.getAccreditUserID());
		voBill.setOperatelogVO(voUpdatedBill.getOperatelogVO());
		// 如果不保存条码清空条码
		nc.vo.ic.pub.barcodeparse.BarcodeparseCtrl.beforSaveBillVOBarcode(m_bBarcodeSave, voBill);
		// 是否保存条码
		voBill.setSaveBadBarcode(m_bBadBarcodeSave);
		// 是否保存数量不一致的条码
		voBill.setSaveBarcode(m_bBarcodeSave);
		ArrayList alRetData = (ArrayList) nc.ui.pub.pf.PfUtilClient.processAction("IMPORTBARCODE", m_sBillTypeCode, m_sLogDate, voBill, null);
		// 检查条码完整，完整则查找指定参数目录下的Excel条码文件并删除
		OffLineCtrl ctrl = new OffLineCtrl(this);
		ctrl.directSaveDelete(voBill);

		return alRetData;
	}

	/**
	 * 用以导入Excel主或次条码文件(整合了所有菜单） 作者:李俊 iMenu =0 ,1，2 分别对应导入主、次条码和主次条码
	 * 创建日期：(2004-4-21 20:41:28)
	 */
	protected void onImptBCExcel(int iMenu) {
		// 是否覆盖旧的存货的条码
		boolean bCover = false;
		// 当前选中行
		int rownow;
		// 导入条码文件于此VO数组中
		BarCodeVO[] voaImport = null;
		// 导入条码数量
		int sizeVOImp = 0;
		// 新旧条码的信息
		ArrayList alOldVO = new ArrayList();
		ArrayList alNewVO = new ArrayList();
		// 整合返回的BarcodeVO数组
		BarCodeVO[] bcVOTotal = null;

		rownow = checkSelectionRow();
		if (rownow == -1) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH004")/* @res "请选择要处理的数据行！" */);
			return;
		}
		// 得到旧的存货条码信息
		GeneralBillItemVO billItemvo = (GeneralBillItemVO) m_voBill.getItemVOs()[rownow];
		alOldVO = BarcodeparseCtrlUI.getVOInfoOld(billItemvo);
		// 导入文件至VO
		try {
			nc.ui.scm.pub.excel.ExcelBarcodeDialog m_eDlg = new nc.ui.scm.pub.excel.ExcelBarcodeDialog(this);
			m_eDlg.setVOName("nc.vo.ic.pub.bc.BarCodeVO");
			m_eDlg.setCHandENnames(BarcodeparseCtrl.getVOStringType(iMenu));
			m_eDlg.showModal();
			bCover = m_eDlg.getRadioSelect();
			voaImport = null;
			if (m_eDlg.isExportOK()) {
				voaImport = (BarCodeVO[]) m_eDlg.getExportVO();
				if (voaImport == null || voaImport.length == 0 || voaImport[0] == null) {
					MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000059")/*
																																	* @res "错误"
																																	*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000088")/*
																																																						* @res "Excel文件条码为空！"
																																																						*/);
					return;
				}
			} else return;
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000089")/* @res "打开Excel条码文件出错！" */
					+ e.getMessage());
		}
		/** 对条码长度进行条码规则校验 */
		String sMsg = BarcodeparseCtrlUI.verifyLenInfo(m_sCorpID, voaImport);
		if (sMsg != null) {
			showErrorMessage(sMsg);
			return;
		}

		// 条码规则判断,只对第一个条码校验
		if (voaImport != null && voaImport.length != 0 && voaImport[0] != null) {
			alNewVO = BarcodeparseCtrlUI.getVOInfoNew(voaImport[0], m_sCorpID);
			if (alNewVO == null) {
				MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000059")/*
																																* @res "错误"
																																*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000090")/*
																																																					* @res "导入条码不复合条码规则！"
																																																					*/);
				return;
			}
			if (iMenu == 0 && alNewVO.get(1) != BarcodeparseCtrl.MAINBARCODE) {
				MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPPSCMCommon-000059")/* @res "错误" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000327")/* @res "导入条码不是主条码！" */);
				return;
			}
			if (iMenu == 1 && alNewVO.get(1) != BarcodeparseCtrl.SUBBARCODE) {
				MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPPSCMCommon-000059")/* @res "错误" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000328")/* @res "导入条码不是次条码！" */);
				return;
			}
			if (iMenu == 2 && alNewVO.get(1) != BarcodeparseCtrl.BOTHCODEHAVE) {
				MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPPSCMCommon-000059")/* @res "错误" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000329")/* @res "导入条码不是主次条码！" */);
				return;
			}
		}
		// 置入数量和单件属性
		if (voaImport != null) sizeVOImp = voaImport.length;
		for (int i = 0; i < sizeVOImp; i++) {
			voaImport[i].setNnumber(new UFDouble("1.00"));

			voaImport[i].setBsingletypeflag((UFBoolean) alNewVO.get(0));
		}

		// 整合VO[]
		BarCodeVO[] voaOld = billItemvo.getBarCodeVOs();
		BarCodeVO[] voaOldCopy = null;
		if (voaOld != null) {
			voaOldCopy = new BarCodeVO[voaOld.length];
			for (int i = 0; i < voaOld.length; i++) {
				voaOldCopy[i] = (nc.vo.ic.pub.bc.BarCodeVO) voaOld[i].clone();
			}
		}

		bcVOTotal = BarcodeparseCtrlUI.barCodeAddWrapper(iMenu, bCover, alOldVO, voaOld, voaImport);
		// *浏览状状态和签字状态下，条码数量应小于实际数量
		if ((m_voBill.getHeaderVO().getPrimaryKey() != null) && (m_iMode == BillMode.Browse)) {
			UFDouble innum = (UFDouble) (billItemvo.getAttributeValue("ninnum"));
			UFDouble outnum = (UFDouble) (billItemvo.getAttributeValue("noutnum"));
			if (innum == null) innum = new UFDouble(0);
			if (outnum == null) outnum = new UFDouble(0);
			double allNum = 0.00;
			int len = bcVOTotal.length;
			for (int i = 0; i < len; i++) {
				if (bcVOTotal[i].getStatus() == nc.vo.pub.VOStatus.DELETED) continue;
				allNum += bcVOTotal[i].getNnumber().doubleValue();
			}
			if (allNum > Math.abs(innum.doubleValue()) && allNum > Math.abs(outnum.doubleValue())) {
				MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000059")/*
																																* @res "错误"
																																*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000091")/*
																																																					* @res "条码数量不应大于实际收发数量！"
																																																					*/);
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000092")/*
																												* @res "条码数量不应大于实际收发数量"
																												*/);
				m_voBill.getItemVOs()[rownow].setBarCodeVOs(voaOldCopy);
				return;
			}
		}
		// 5.将VO放在存货界面VO上
		m_voBill.getItemVOs()[rownow].setBarCodeVOs(bcVOTotal);
		if (bcVOTotal == null || bcVOTotal.equals(voaOld)) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000093")/* @res "没有导入成功，请重新导入!" */);
			return;
		}
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000094")/* @res "已将条码导入到单据,请保存!" */);
		// 6
		try {
			if ((m_voBill.getHeaderVO().getPrimaryKey() != null) && (m_iMode == BillMode.Browse)) {
				if (onImportSignedBillBarcode(m_voBill, true) == 1) showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000095")/*
																																									* @res
																																									* "导入并保存条码成功(请注意库存单据是否允许保存条码）!"
																																									*/);
				else {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000096")/*
																													* @res
																													* "
																													* 没有保存成功
																													* ！"
																													*/);
					m_voBill.getItemVOs()[rownow].setBarCodeVOs(voaOldCopy);
				}
			}
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
			showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000097")/* @res "导入失败：" */
					+ e.getMessage());
		}
	}

	/**
	 * 此处插入方法说明。 功能描述:根据表体行的单据ID和单据类型联查上下游单据。 作者：程起伍 输入参数: 返回值: 异常处理:
	 * 日期:(2003-4-22 16:09:14)
	 */
	public void onJointCheck() {

		if (m_iLastSelListHeadRow >= 0 && m_alListData != null && m_alListData.size() > m_iLastSelListHeadRow && m_alListData.get(m_iLastSelListHeadRow) != null) {

			GeneralBillVO voBill = null;
			GeneralBillHeaderVO voHeader = null;
			// 得到单据VO
			voBill = (GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow);
			// 得到单据表头VO
			voHeader = voBill.getHeaderVO();

			if (voHeader == null) {
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000098")/* @res "没有要联查的单据！" */);
				return;
			}
			String sBillPK = null;
			String sBillTypeCode = null;

			sBillPK = voHeader.getCgeneralhid();
			sBillTypeCode = voHeader.getCbilltypecode();
			// 如果sBillPK和sBillTypeCode为空，联查没有意义。
			if (sBillPK == null || sBillTypeCode == null) {
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000099")/* @res "该行没有对应单据！" */);
				return;
			}
			nc.ui.scm.sourcebill.SourceBillFlowDlg soureDlg = new nc.ui.scm.sourcebill.SourceBillFlowDlg(this, sBillTypeCode, sBillPK, null, m_sUserID, m_sCorpID);
			soureDlg.showModal();
		} else {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000154")/* @res "没有联查的单据！" */);
		}
	}

	/**
	 * 此处插入方法说明。 功能描述:在卡片模式下指向最后一张 作者：程起伍 输入参数: 返回值: 异常处理: 日期:(2003-5-27
	 * 14:48:54)
	 */
	public void onLast() {
		if (m_alListData != null && m_alListData.size() > 0) {
			int iAll = m_alListData.size();
			int iCur = iAll - 1;
			scrollBill(iCur);
			m_pageBtn.last(iAll);
			updateButtons();
		}
	}

	/**
	 * 此处插入方法说明。 功能描述:在卡片模式下指向下一张 作者：程起伍 输入参数: 返回值: 异常处理: 日期:(2003-5-27
	 * 14:48:31)
	 */
	public void onNext() {
		if (m_alListData != null && m_alListData.size() > 0) {
			int iAll = m_alListData.size();
			int iCur = m_iLastSelListHeadRow + 1;
			scrollBill(iCur);
			m_pageBtn.next(iAll, iCur);
			updateButtons();
		}
	}

	/**
	 * 创建者：仲瑞庆 功能：打印预览 参数： 返回： 例外： 日期：(2001-5-10 下午 4:16) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 修改说明：增加打印次数控制 修改者：邵兵 2004-01-12
	 */
	public void onPreview() {

		try {
			// 调出打印窗口
			// 依当前是列表还是表单而定打印内容
			if (m_iMode == BillMode.Browse && m_iCurPanel == BillMode.Card) { // 浏览

				/* 增加打印次数控制后的打印方案 */
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000047")/*
																												* @res "正在打印，请稍候..."
																												*/);
				// 准备数据：获得要打印的vo.
				GeneralBillVO voBill = null;

				if (m_iLastSelListHeadRow != -1 && null != m_alListData && m_alListData.size() != 0) {
					if (m_sLastKey != null && m_voBill != null) voBill = m_voBill;
					else voBill = (GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow);
					if (getBillCardPanel().getHeadItem("vcustname") != null) voBill.setHeaderValue("vcustname", getBillCardPanel().getHeadItem("vcustname").getValueObject());
				}

				if (voBill == null) {
					voBill = new GeneralBillVO();
				}
				if (voBill.getParentVO() == null) {
					voBill.setParentVO(new GeneralBillHeaderVO());
				}
				if ((voBill.getChildrenVO() == null) || (voBill.getChildrenVO().length == 0) || (voBill.getChildrenVO()[0] == null)) {
					GeneralBillItemVO[] ivo = new GeneralBillItemVO[1];
					ivo[0] = new GeneralBillItemVO();
					voBill.setChildrenVO(ivo);
				}

				if (getPrintEntry().selectTemplate() < 0) {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000048")/*
																													* @res
																													* "请先定义打印模板。"
																													*/);
					return;
				}

				// 卡片下预览
				printOnCard(voBill, true);

				// showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
				// "SCMCOMMON", "UPPSCMCommon-000133")/* @res "就绪" */);

			} else if (m_iCurPanel == BillMode.List) { // 列表

				/* 增加打印次数控制后的打印方案 */
				if (null == m_alListData || m_alListData.size() == 0) {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000049")/*
																													* @res
																													* "请先查询或录入单据。"
																													*/);
					return;
				}
				// 需要的化，查询缺少的单据数据
				// queryLeftItem(m_alListData);

				ArrayList alBill = getSelectedBills();
				if (alBill == null || alBill.size() <= 0 || alBill.get(0) == null) {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH003")/*
																									 * @res "请选择要处理的数据！"
																									 */);
					return;
				}
				// 置小数精度
				setScaleOfListData(alBill);

				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000100")/*
																												* @res
																												* "正在生成第一张单据的打印预览数据，请稍候..."
																												*/);

				GeneralBillVO voBill = (GeneralBillVO) alBill.get(0);
				// 单据主表
				GeneralBillHeaderVO headerVO = voBill.getHeaderVO();
				String sBillID = headerVO.getPrimaryKey();

				// 构造PringLogClient以及设置PrintInfo
				ScmPrintlogVO voSpl = new ScmPrintlogVO();
				voSpl = new ScmPrintlogVO();
				voSpl.setCbillid(sBillID); // 单据主表的ID
				voSpl.setVbillcode(headerVO.getVbillcode());// 传入单据号，用于显示。
				voSpl.setCbilltypecode(headerVO.getCbilltypecode());
				voSpl.setCoperatorid(headerVO.getCoperatorid());
				voSpl.setIoperatetype(new Integer(PrintConst.PAT_OK));// 固定
				voSpl.setPk_corp(getCorpPrimaryKey());
				voSpl.setTs(headerVO.getTs());// 单据主表的TS

				SCMEnv.out("ts=========tata" + voSpl.getTs());
				nc.ui.scm.print.PrintLogClient plc = new nc.ui.scm.print.PrintLogClient();

				nc.ui.pub.print.PrintEntry pe = getPrintEntryNew();

				if (pe.selectTemplate() < 0) {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000048")/*
																													* @res
																													* "请先定义打印模板。"
																													*/);
					return;
				}

				plc.setPrintEntry(pe);
				// 设置单据信息
				plc.setPrintInfo(voSpl);
				// 设置ts和printcount刷新监听.
				plc.addFreshTsListener(new FreshTsListener());
				// 设置打印监听
				pe.setPrintListener(plc);

				// 打印预览
				getDataSource().setVO(voBill);
				pe.setDataSource(getDataSource());
				pe.preview();

				// showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
				// "SCMCOMMON", "UPPSCMCommon-000133")/* @res "就绪" */);

			} else showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000101")/*
																												* @res
																												* "请注意：您只能在浏览状态下执行打印预览。"
																												*/);
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000061")/* @res "打印出错" */
					+ e.getMessage());
		}
	}

	/**
	 * 汇总行打印，通过汇总条件对话框获得汇总条件，汇总行VO，打印预览
	 */
	protected void onPrintSumRow() {

		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi", "UPP4008busi-000248")/* @res "正在打印，请稍候..." */);
		SCMEnv.out("打印批次汇总开始!\n");
		try {
			// 调出打印窗口
			// 依当前是列表还是表单而定打印内容
			if (m_iMode == BillMode.Browse && m_iCurPanel == BillMode.Card) { // 浏览
				SCMEnv.out("打印批次汇总开始!表单打印!\n");
				// 准备数据
				GeneralBillVO vo = null;

				if (m_iLastSelListHeadRow != -1 && null != m_alListData && m_alListData.size() != 0) {
					vo = (GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow);
					if (getBillCardPanel().getHeadItem("vcustname") != null) vo.setHeaderValue("vcustname", getBillCardPanel().getHeadItem("vcustname").getValueObject());
				}

				if (null == vo) {
					vo = new GeneralBillVO();
				}
				if (null == vo.getParentVO()) {
					vo.setParentVO(new GeneralBillHeaderVO());
				}
				if ((null == vo.getChildrenVO()) || (vo.getChildrenVO().length == 0) || (vo.getChildrenVO()[0] == null)) {
					GeneralBillItemVO[] ivo = new GeneralBillItemVO[1];
					ivo[0] = new GeneralBillItemVO();
					vo.setChildrenVO(ivo);
				}

				if (getPrintEntry().selectTemplate() < 0) return;
				GeneralBillVO gvobak = (GeneralBillVO) vo.clone();

				// 得到汇总条件,如果不选择汇总辅助计量单位，则不用汇总负数量,默认选择汇总辅数量
				MergeRowDialog dlgMerge = new MergeRowDialog(this);
				if (dlgMerge.showModal() == UIDialog.ID_CANCEL) return;
				ArrayList alGroupBy = dlgMerge.getGroupingAttr();
				if (alGroupBy == null || alGroupBy.size() <= 0 || alGroupBy.size() != 6) return;

				// if ( ((Boolean)alGroupBy.get(2)).booleanValue() == false )
				//				

				// 得到分组字段
				String[] Fields = new String[] {
						"cinventoryid", "vbatchcode", "castunitid", "vfree0", "cspaceid"
				};
				ArrayList alChooseGroup = new ArrayList();
				for (int i = 0; i < alGroupBy.size() - 1; i++) {
					if (((Boolean) alGroupBy.get(i)).booleanValue() == true) alChooseGroup.add(Fields[i]);
				}
				String[] saGroupField = null;
				if (alChooseGroup.size() > 0) saGroupField = new String[alChooseGroup.size()];
				alChooseGroup.toArray(saGroupField);

				nc.vo.scm.merge.DefaultVOMerger dvomerger = new nc.vo.scm.merge.DefaultVOMerger();
				dvomerger.setGroupingAttr(saGroupField);

				// 得到Summing字段
				String[] saSummingField = null;
				if (m_iBillInOutFlag == InOutFlag.IN) {
					if (((Boolean) alGroupBy.get(2)).booleanValue() == true) saSummingField = new String[] {
							"nshouldinnum", "nneedinassistnum", "ninnum", "ninassistnum", "nmny"
					};
					else saSummingField = new String[] {
							"nshouldinnum", "ninnum", "nmny"
					};
				} else if (m_iBillInOutFlag == InOutFlag.OUT) {
					if (((Boolean) alGroupBy.get(2)).booleanValue() == true) saSummingField = new String[] {
							"nshouldoutnum", "nshouldoutassistnum", "noutnum", "noutassistnum", "nmny"
					};
					else saSummingField = new String[] {
							"nshouldoutnum", "noutnum", "nmny"
					};
				}
				dvomerger.setSummingAttr(saSummingField);

				nc.vo.ic.pub.bill.GeneralBillItemVO[] itemvosnew = (nc.vo.ic.pub.bill.GeneralBillItemVO[]) dvomerger.mergeByGroup(gvobak.getItemVOs());

				if (itemvosnew != null) {
					UFDouble udNum = null;
					UFDouble udMny = null;
					for (int k = 0; k < itemvosnew.length; k++) {
						udNum = itemvosnew[k].getNoutnum();
						udMny = itemvosnew[k].getNmny();
						if (udNum != null && udMny != null) {
							itemvosnew[k].setNprice(udMny.div(udNum));
						}
					}

				}

				gvobak.setChildrenVO(itemvosnew);

				// 汇总预览
				printOnCard(gvobak, true);

			} else if (m_iCurPanel == BillMode.List) {
				// 列表

				SCMEnv.out("列表打印开始!\n");
				if (null == m_alListData || m_alListData.size() == 0) { return; }
				if (getPrintEntry().selectTemplate() < 0) return;
				ArrayList alBill = getSelectedBills();
				// 置小数精度
				setScaleOfListData(alBill);
				SCMEnv.out("列表打印:得到选中的单据并设置数量精度!\n");
				if (alBill == null) return;
				nc.vo.scm.merge.DefaultVOMerger dvomerger = null;
				for (int i = 0; i < alBill.size(); i++) {
					SCMEnv.out("列表打印:开始合并表体行!\n");
					GeneralBillVO gvobak = (GeneralBillVO) alBill.get(i);
					// /
					dvomerger = new nc.vo.scm.merge.DefaultVOMerger();
					dvomerger.setGroupingAttr(new String[] {
							"cinventoryid", "castunitid"
					});
					dvomerger.setSummingAttr(new String[] {
							"nshouldoutnum", "nshouldoutassistnum", "noutnum", "noutassistnum", "nmny"
					});
					nc.vo.ic.pub.bill.GeneralBillItemVO[] itemvosnew = (nc.vo.ic.pub.bill.GeneralBillItemVO[]) dvomerger.mergeByGroup(gvobak.getItemVOs());
					SCMEnv.out("列表打印:得到合并后的表体行!\n");
					if (itemvosnew != null) {
						UFDouble udNum = null;
						UFDouble udMny = null;
						for (int k = 0; k < itemvosnew.length; k++) {
							udNum = itemvosnew[k].getNoutnum();
							udMny = itemvosnew[k].getNmny();
							if (udNum != null && udMny != null) {
								itemvosnew[k].setNprice(udMny.div(udNum));
							}

						}
						gvobak.setChildrenVO(itemvosnew);
						alBill.set(i, gvobak);
					}

				}
				//
				SCMEnv.out("列表打印:得到合并后的单据!\n");

				// 列表下打印，而非预览。
				printOnList(alBill);

			} else showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi", "UPP4008busi-000249")/* @res "只能在浏览状态下打印" */);
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi", "UPPSCMCommon-000061")/* @res "打印出错" */
					+ e.getMessage());
		}

	}

	/**
	 * 此处插入方法说明。 功能描述:在卡片模式下指向前一张 作者：程起伍 输入参数: 返回值: 异常处理: 日期:(2003-5-27
	 * 14:48:02)
	 */
	public void onPrevious() {
		if (m_alListData != null && m_alListData.size() > 0) {
			int iAll = m_alListData.size();
			int iCur = m_iLastSelListHeadRow - 1;
			scrollBill(iCur);
			m_pageBtn.previous(iAll, iCur);
			updateButtons();
		}
	}

	private boolean IsNeedCheck(String dh, String primkey, String billtype) throws BusinessException {
		String SQL = "select b.vfree1 ";
		SQL += " from ic_general_h h,ic_general_b b  ";
		SQL += "where h.cgeneralhid=b.cgeneralhid and b.dr=0 and h.dr=0 and b.cgeneralbid='" + primkey + "' and  h.cbilltypecode='" + billtype + "'";
		IUAPQueryBS sessionManager = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		try {
			HashMap resuthm = (HashMap) sessionManager.executeQuery(SQL, new MapProcessor());
			if (resuthm == null || resuthm.isEmpty()) { return true; }
			String vfree1 = String.valueOf(resuthm.get("vfree1"));

			return !dh.equals(vfree1);

		} catch (BusinessException e) {
			// MessageDialog.showErrorDlg(this,"错误Error", e.getMessage());
			// return false;
			throw e;
		}

	}

	/**
	 * 创建者：王乃军 功能：确认（保存）处理 参数：无 返回： true: 成功 false: 失败
	 * 
	 * 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 2001/10/29,wnj,拆分出保存新增/保存修改两个方法，使得更规范。
	 * 
	 * 
	 * 
	 */
	public boolean onSaveBase() {
		try {
			nc.vo.ic.pub.bill.Timer t = new nc.vo.ic.pub.bill.Timer();
			m_timer.start("保存开始");
			t.start();
			// 滤去表单形式下的空行
			filterNullLine();

			GeneralBillItemVO[] oldvo = getBodyVOs();
			HashMap<String, String> hm = new HashMap<String, String>();
			for (int i = 0; i < oldvo.length; i++) {
				if (!StringIsNullOrEmpty(oldvo[i].getVfirstbillcode()) && !hm.containsKey(oldvo[i].getCfirstbillhid())) {
					hm.put(oldvo[i].getCfirstbillhid(), oldvo[i].getVfirstbillcode());
				}
			}

			m_timer.showExecuteTime("filterNullLine");
			// 无表体行 ------------ EXIT -------------------
			if (getBillCardPanel().getRowCount() <= 0) {
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000072")/* @res "请输入表体行!" */);
				// 不添加新行 add by hanwei 2004-06-08 ,调拨出入库单有些情况下不能自制
				return false;
			}
			// added by zhx 030626 检查行号的合法性; 该方法应放在过滤空行的后面。
			if (!nc.ui.scm.pub.report.BillRowNo.verifyRowNosCorrect(getBillCardPanel(), m_sBillRowNo)) { return false; }
			// 当前的表体行数
			int iRowCount = getBillCardPanel().getRowCount();
			// 界面的单据数据
			GeneralBillVO voInputBill = null;
			// 从界面中获得需要的数据
			voInputBill = getBillVO();

			// 得到数据错误，出错 ------------ EXIT -------------------
			if (voInputBill == null || voInputBill.getParentVO() == null || voInputBill.getChildrenVO() == null) {
				SCMEnv.out("Bill is null !");
				return false;
			}
			// 输入的表体行
			GeneralBillItemVO voInputBillItem[] = voInputBill.getItemVOs();
			// 得到数据行
			int iVORowCount = voInputBillItem.length;
			// 得到数据行和界面行数不一致，出错 ------------ EXIT -------------------
			if (iVORowCount != iRowCount) {
				SCMEnv.out("data error." + iVORowCount + "<>" + iRowCount);
				return false;
			}
			m_timer.showExecuteTime("From fliterNullLine Before setIDItems");
			// VO校验准备数据
			m_voBill.setIDItems(voInputBill);
			// 设置单据类型
			m_voBill.setHeaderValue("cbilltypecode", m_sBillTypeCode);

			m_timer.showExecuteTime("setIDItems");

			if (getBillCardPanel().getHeadItem("vdiliveraddress") != null && getBillCardPanel().getHeadItem("vdiliveraddress").getComponent() != null) {
				String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("vdiliveraddress").getComponent()).getUITextField().getText();// getRefName();
				// 保存名称以在列表形式下显示。
				if (m_voBill != null) m_voBill.setHeaderValue("vdiliveraddress", sName);
			}

			// 重置单据行号zhx 0630:
			if (iRowCount > 0 && m_voBill.getChildrenVO() != null) {
				if (getBillCardPanel().getBodyItem(m_sBillRowNo) != null) for (int i = 0; i < iRowCount; i++) {
					m_voBill.setItemValue(i, m_sBillRowNo, getBillCardPanel().getBodyValueAt(i, m_sBillRowNo));

				}
			}

			if (!hm.isEmpty()) {
				for (int i = 0; i < m_voBill.getChildrenVO().length; i++)

				{
					GeneralBillItemVO bo = (GeneralBillItemVO) m_voBill.getChildrenVO()[i];
					if (StringIsNullOrEmpty(bo.getVfirstbillcode()) && !StringIsNullOrEmpty(hm.get(bo.getCfirstbillhid()))) {
						m_voBill.getChildrenVO()[i].setAttributeValue("vfirstbillcode", hm.get(bo.getCfirstbillhid()));
					}
				}
			}

			// VO校验 ------------ EXIT -------------------
			if (!checkVO(m_voBill)) {

			return false; }
			m_timer.showExecuteTime("VO校验");

			// 如果没有单据日期，填写为当前登录日期
			if (getBillCardPanel().getHeadItem("dbilldate") == null || getBillCardPanel().getHeadItem("dbilldate").getValueObject() == null || getBillCardPanel().getHeadItem("dbilldate").getValueObject().toString().trim().length() == 0) {
				SCMEnv.out("-->no bill date.");
				m_voBill.setHeaderValue("dbilldate", m_sLogDate);
			}
			m_timer.showExecuteTime("设置单据类型和单据日期");

			ArrayList dhList = new ArrayList();
			String billtype = ((GeneralBillHeaderVO) m_voBill.getParentVO()).getCbilltypecode();
			if (getParentCorpCode().equals("10395") && (billtype.equals("45") || billtype.equals("46") || billtype.equals("47") || billtype.equals("4A") || billtype.equals("4E"))) {
				for (int i = 0; i < m_voBill.getChildrenVO().length; i++) {

					LocatorVO[] temvo1 = ((GeneralBillItemVO) m_voBill.getChildrenVO()[i]).getLocator();
					if (m_voBill.getChildrenVO()[i].getStatus() != VOStatus.NEW && m_voBill.getChildrenVO()[i].getStatus() != VOStatus.UPDATED) {
						continue;
					}

					String vfree1 = String.valueOf(m_voBill.getChildrenVO()[i].getAttributeValue("vfree1"));
					if (StringIsNullOrEmpty(vfree1)) {
						continue;
					}
					if (dhList.indexOf(vfree1) > 0) {
						continue;
					}
					if (m_voBill.getChildrenVO()[i].getStatus() == VOStatus.UPDATED) {
						try {
							if (!IsNeedCheck(vfree1, m_voBill.getChildrenVO()[i].getPrimaryKey(), billtype)) {
								continue;
							}
						} catch (BusinessException e) {
							showErrorMessage(e.getMessage());
						}
					}
					if (StampIsExist(vfree1)) {
						//edit by shikun 2014-03-20 如果数量为负数即退货时，不进行垛号是否重复入库的判断。
						//取应收数量
						UFDouble nshouldinnum = m_voBill.getChildrenVO()[i].getAttributeValue("nshouldinnum")==null? new UFDouble(0.00)
						:new UFDouble(m_voBill.getChildrenVO()[i].getAttributeValue("nshouldinnum").toString());
						//取实收数量
						UFDouble ninnum = m_voBill.getChildrenVO()[i].getAttributeValue("ninnum")==null? new UFDouble(0.00)
						:new UFDouble(m_voBill.getChildrenVO()[i].getAttributeValue("ninnum").toString());
						//如果实收数量不为0则以实收数量为主
						//退货数量
						UFDouble num = new UFDouble(0.00);
						if (ninnum.doubleValue()!=0) {
							num = ninnum;
						}else {
							num = nshouldinnum;
						}
						
						if (num.doubleValue() >= 0) {
							dhList.add(vfree1);
						}
						//end shikun 
					}
				}
			}

			if (dhList.size() > 0) {
				// if(MessageDialog.ID_YES!=showYesNoMessage(Transformations.getLstrFromMuiStr("垛号@The pile No. &"+dhList.toString()
				// + "&重复入库！@repeat warehousing!")+" \r\n"
				// + "是否继续？ continue?"))
				// {
				// return false ;
				// }'
				showErrorMessage(Transformations.getLstrFromMuiStr("垛号@The pile No. &" + dhList.toString() + "&重复入库！@repeat warehousing!"));
				return false;
			}

			// showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
			// "4008bill", "UPP4008bill-000102")/* @res "正在保存，请稍候..." */);

			// 保存的核心方法入口 add by hanwei 2004-04

			// 默认单据状态 add by hanwei
			m_sBillStatus = nc.vo.ic.pub.bill.BillStatus.FREE;
			// 实际m_sBillStatus的赋值在onSaveBaseKernel中的：saveUpdateBill,saveNewBill

			m_voBill.setIsCheckCredit(true);
			m_voBill.setIsCheckPeriod(true);
			m_voBill.setIsCheckAtp(true);
			m_voBill.setGetPlanPriceAtBs(false);
			m_voBill.setIsRwtPuUserConfirmFlag(false);
			System.out.println("111====" + m_voBill.getStatus());
			System.out.println("2222===" + m_voBill.getHeaderVO().getStatus());

			while (true) {
				try {

					onSaveBaseKernel(m_voBill, m_sUserID);
					break;

				} catch (Exception ee1) {

					BusinessException realbe = nc.ui.ic.pub.tools.GenMethod.handleException(null, null, ee1);
					if (realbe != null && realbe.getClass() == nc.vo.scm.pub.excp.RwtIcToPoException.class) {
						// 错误信息显示，并询问用户“是否继续？”
						int iFlag = showYesNoMessage(realbe.getMessage());
						// 如果用户选择继续
						if (iFlag == nc.ui.pub.beans.MessageDialog.ID_YES) {
							m_voBill.setIsRwtPuUserConfirmFlag(true);
							continue;
						} else return false;
					} else if (realbe != null && realbe.getClass() == CreditNotEnoughException.class) {
						// 错误信息显示，并询问用户“是否继续？”
						int iFlag = showYesNoMessage(realbe.getMessage() + " \r\n" + "是否继续？");
						// 如果用户选择继续
						if (iFlag == nc.ui.pub.beans.MessageDialog.ID_YES) {
							m_voBill.setIsCheckCredit(false);
							continue;
						} else return false;
					} else if (realbe != null && realbe.getClass() == PeriodNotEnoughException.class) {
						// 错误信息显示，并询问用户“是否继续？”
						int iFlag = showYesNoMessage(realbe.getMessage() + " \r\n" + "是否继续？");
						// 如果用户选择继续
						if (iFlag == nc.ui.pub.beans.MessageDialog.ID_YES) {
							m_voBill.setIsCheckPeriod(false);
							continue;
						} else return false;
					} else if (realbe != null && realbe.getClass() == ATPNotEnoughException.class) {
						ATPNotEnoughException atpe = (ATPNotEnoughException) realbe;
						if (atpe.getHint() == null) {
							showErrorMessage(atpe.getMessage());
							return false;
						} else {
							// 错误信息显示，并询问用户“是否继续？”
							int iFlag = showYesNoMessage(atpe.getMessage() + " \r\n" + "是否继续？");
							// 如果用户选择继续
							if (iFlag == nc.ui.pub.beans.MessageDialog.ID_YES) {
								m_voBill.setIsCheckAtp(false);
								continue;
							} else {
								return false;
							}
						}
					} else {
						if (realbe != null) throw realbe;
						else throw ee1;
					}
				}
			}

			// 是普通新增、或修改
			if (BillMode.New == m_iMode || BillMode.Update == m_iMode) {
				// necessary！//刷新单据状态
				getBillCardPanel().updateValue();
				m_timer.showExecuteTime("updateValue");
				// coperatorid
				m_iMode = BillMode.Browse;

				// 不可编辑
				getBillCardPanel().setEnabled(false);
				// 重设按钮状态
				setButtonStatus(false);
				m_timer.showExecuteTime("setButtonStatus");

				// 清空现存量
				// 屏蔽 by hanwei 2003-11-13 避免保存后界面选择出现存量为空
				// m_voBill.clearInvQtyInfo();
				// 选中第一行
				getBillCardPanel().getBillTable().setRowSelectionInterval(0, 0);
				// 重置序列号是否可用
				setBtnStatusSN(0, false);
				// 刷新第一行现存量显示
				setTailValue(0);
				m_timer.showExecuteTime("刷新第一行现存量显示");
			}

			if (m_sBillStatus != null && !m_sBillStatus.equals(BillStatus.FREE) && !m_sBillStatus.equals(BillStatus.DELETED)) {
				SCMEnv.out("**** saved and signed ***");
				freshAfterSignedOK(m_sBillStatus);
				m_timer.showExecuteTime("freshAfterSignedOK");
			}
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH005")/* @res "保存成功" */);

			// 对于有来源的单据进行不同的界面控制；zhx 1130
			ctrlSourceBillUI(true);
			m_timer.showExecuteTime("来源单据界面控制");
			t.stopAndShow("保存合计");

			// save the barcodes to excel file according to param IC***
			m_timer.showExecuteTime("开始执行保存条码文件");
			OffLineCtrl ctrl = new OffLineCtrl(this);
			ctrl.saveExcelFile(m_voBill, getCorpPrimaryKey());
			m_timer.showExecuteTime("执行保存条码文件结束");
			return true;

		} catch (java.net.ConnectException ex1) {
			SCMEnv.out(ex1.getMessage());
			if (showYesNoMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000104")/*
																												* @res
																												* "当前网络中断，是否将单据信息保存到默认目录："
																												*/
			) == MessageDialog.ID_YES) {
				onBillExcel(1);// 保存单据信息到默认目录
			}
		} catch (Exception e) {

			handleException(e);
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000105")/* @res "保存出错。" */);
			String se = e.getMessage();
			if (se != null) {
				int index = se.indexOf("$$ZZZ$$");
				if (index >= 0) se = se.substring(index + 7);
			}
			showErrorMessage(se);

		}
		return false;
	}

	/**
	 * @功能:返回公司的上级公司编码
	 * @return add by cm
	 */
	public String getParentCorpCode() {

		String ParentCorp = new String();
		String key = ClientEnvironment.getInstance().getCorporation().getFathercorp();
		try {
			CorpVO corpVO = CorpBO_Client.findByPrimaryKey(key);
			ParentCorp = corpVO.getUnitcode();
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return ParentCorp;
	}

	/**
	 * 判断垛号是否存在 新增人 ：林桂莹2012/8/29
	 * 
	 * @since v50
	 */
	private boolean StampIsExist(String StampNo) {
		boolean rst = false;
		try {

			String SQL = "select kp.vfree1 ";
			SQL += "from   v_ic_onhandnum6 kp  ";
			SQL += "where kp.vfree1='" + StampNo + "' and nvl(ninspacenum,0.0)-nvl(noutspacenum,0.0)>0 ";
			if(this.m_sCorpID != null){
				SQL += "and kp.pk_corp='"+m_sCorpID+"'";//add by zwx 2015-4-10 
			}

			IUAPQueryBS sessionManager = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			List list = (List) sessionManager.executeQuery(SQL, new ArrayListProcessor());
			ArrayList values = new ArrayList();
			Iterator iterator = list.iterator();
			while (iterator.hasNext()) {
				Object obj = iterator.next();
				if (obj.getClass().isArray()) {
					int len = Array.getLength(obj);
					for (int i = 0; i < len; i++) {
						values.add(Array.get(obj, i));
					}
					if (String.valueOf(values.get(0)) != null && !String.valueOf(values.get(0)).equals("") && !String.valueOf(values.get(0)).equalsIgnoreCase("null")) {
						rst = true;
						break;
					}
				}
			}
		} catch (BusinessException e) {
			MessageDialog.showErrorDlg(this, "错误Error", e.getMessage());
			return false;
		}
		return rst;
	}

	public boolean StringIsNullOrEmpty(Object obj) {
		return obj == null ? true : String.valueOf(obj).equals("") ? true : String.valueOf(obj).equalsIgnoreCase("null") ? true : false;
	}

	/**
	 * 创建者：保存基本方法中的核心方法 功能：确认（保存）处理 参数：无 例外： 日期：(2004-4-1 9:23:32)
	 * 修改日期，修改人，修改原因，注释标志： 2004-4-1 韩卫
	 */
	public void onSaveBaseKernel(GeneralBillVO voBill, String sAccreditUserID) throws Exception {

		try {
			nc.vo.sm.log.OperatelogVO log = getNormalOperateLog();
			BillItem item = getBillCardPanel().getHeadItem("vdiliveraddress");

			if (BillMode.New == m_iMode) {
				if (item != null && item.getComponent() != null) voBill.setHeaderValue("vdiliveraddress", ((UIRefPane) item.getComponent()).getUITextField().getText());
				voBill.setStatus(nc.vo.pub.VOStatus.NEW);
				voBill.setHeaderValue("coperatorid", m_sUserID);
				voBill.setHeaderValue("time", m_dTime);
				voBill.setAccreditUserID(sAccreditUserID);
				voBill.setOperatelogVO(log);
				saveNewBill(voBill);
			} else // 修改
			if (BillMode.Update == m_iMode) {
				// 得到修改后的单据VO
				GeneralBillVO voUpdatedBill = getBillChangedVO();
				voUpdatedBill.setAccreditUserID(sAccreditUserID);
				voUpdatedBill.setTs(voBill);
				voUpdatedBill.setOperatelogVO(log);
				// 执行修改保存...有错误抛出异常
				// 执行修改保存
				if (item != null && item.getComponent() != null) voUpdatedBill.setHeaderValue("vdiliveraddress", ((UIRefPane) item.getComponent()).getUITextField().getText());

				saveUpdatedBill(voUpdatedBill);
			} else {
				SCMEnv.out("status invalid...");
				throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000106")/*
																													* @res
																													* "状态错误。 "
																													*/);
			}
		} catch (RightcheckException e) {
			showErrorMessage(e.getMessage() + nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000069")/* @res ".\n权限校验不通过,保存失败! " */);
			getAccreditLoginDialog().setCorpID(m_sCorpID);
			getAccreditLoginDialog().clearPassWord();
			if (getAccreditLoginDialog().showModal() == nc.ui.pub.beans.UIDialog.ID_OK) {
				String sUserID = getAccreditLoginDialog().getUserID();
				if (sUserID == null) {
					throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000070")/*
																														* @res
																														* "权限校验不通过,保存失败. "
																														*/);
				} else {
					voBill.setAccreditUserID(sUserID);
					onSaveBaseKernel(voBill, sUserID);
				}
			} else {
				throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000070")/*
																													* @res
																													* "权限校验不通过,保存失败. "
																													*/);

			}

		} catch (Exception e) {
			throw e;
		}

	}

	/**
	 * 创建者：王乃军 功能：货位指定
	 * 
	 * 参数： 返回： 例外： 日期：(2003-7-2 19:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	protected void onSelLoc() {
		// warehouse id
		String sNewWhID = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem(m_sMainWhItemKey).getComponent()).getRefPK();
		if (sNewWhID == null || sNewWhID.trim().length() == 0) {
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000107")/* @res "请先选择仓库" */);
		} else {
			getLocSelDlg().setWhID(sNewWhID);
			if (getLocSelDlg().showModal() == LocSelDlg.ID_OK) {

				String cspaceid = getLocSelDlg().getLocID();
				String csname = getLocSelDlg().getLocName();
				// 存货列值暂存
				Object oTempValue = null;
				// 表体model
				nc.ui.pub.bill.BillModel bmBill = getBillCardPanel().getBillModel();
				// 存货列号，效率高一些。
				int iInvCol = bmBill.getBodyColByKey(m_sInvMngIDItemKey);

				// 必须有存货列
				if (bmBill != null && iInvCol >= 0 && iInvCol < bmBill.getColumnCount()) {
					// 行数
					int iRowCount = getBillCardPanel().getRowCount();
					// 从后向前删
					for (int line = 0; line < iRowCount; line++) {
						// 存货填了
						oTempValue = bmBill.getValueAt(line, iInvCol);
						if (oTempValue != null && oTempValue.toString().trim().length() > 0) setRowSpaceData(line, cspaceid, csname);
					}
				}

			}
		}

	}

	/**
	 * 判断选择行是否成套件 创建日期：(2004-3-12 21:14:17)
	 */
	public void onSetpart() {

		int rownow = -1;
		String sInvID = null;
		if (m_iCurPanel == BillMode.Card) {

			rownow = getBillCardPanel().getBillTable().getSelectedRow();

			if ((m_voBill != null) && (rownow >= 0)) {
				sInvID = (String) m_voBill.getItemValue(rownow, "cinventoryid");
			}

		}

		else if (m_iCurPanel == BillMode.List) {
			rownow = getBillListPanel().getBodyTable().getSelectedRow();
			sInvID = (String) ((GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow)).getItemInv(rownow).getAttributeValue("cinventoryid");

		}

		if (rownow < 0) {
			MessageDialog.showErrorDlg(this, null, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH004")/*
																													* @res
																													* "请选择要处理的数据行！"
																													*/);
			return;
		}

		if (m_sCorpID == null) {
			MessageDialog.showErrorDlg(this, null, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000109")/*
																																* @res
																																* "当前登陆公司ID为空！"
																																*/);
			return;
		}
		if (sInvID == null || sInvID.trim().length() == 0) {
			MessageDialog.showErrorDlg(this, null, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000110")/*
																																* @res
																																* "选中行没有存货编码！"
																																*/);
			return;
		}

		getSetpartDlg().setParam(m_sCorpID, sInvID);
		getSetpartDlg().showSetpartDlg();

	}

	/**
	 * 创建者：王乃军 功能：查询指定的单据。 参数： billType, 当前单据类型 billID, 当前单据ID businessType,
	 * 当前业务类型 operator, 当前用户ID pk_corp, 当前公司ID
	 * 
	 * 返回 ：单据vo 例外 ： 日期 ： (2001 - 5 - 9 9 : 23 : 32) 修改日期 ， 修改人 ， 修改原因 ， 注释标志 ：
	 * 
	 * 
	 * 
	 * 
	 */
	protected GeneralBillVO qryBill(String pk_corp, String billType, String businessType, String operator, String billID, ConditionVO[] convos) {

		if (billID == null || billType == null || pk_corp == null) {
			SCMEnv.out("no bill param");
			return null;
		}
		GeneralBillVO voRet = null;
		try {
			String sqrywhere = "  head.cbilltypecode='" + billType + "' AND head.cgeneralhid='" + billID + "' ";
			QryConditionVO voCond = new QryConditionVO(sqrywhere);

			voCond.setIntParam(0, GeneralBillVO.QRY_HEAD_ONLY_PURE);
			if (convos != null && convos.length > 0) {
				voCond.setParam(QryConditionVO.QRY_CONDITIONVO, convos);
				String swhere = convos[0].getWhereSQL(convos);
				if (swhere != null && swhere.trim().length() > 0) voCond.setQryCond(sqrywhere + " and " + swhere);
			}

			// 启用进度条
			// getPrgBar(PB_QRY).start();
			long lTime = System.currentTimeMillis();
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000012")/* @res "正在查询，请稍候..." */);
			ArrayList alListData = (ArrayList) GeneralBillHelper.queryBills(m_sBillTypeCode, voCond);

			showTime(lTime, "查询");
			// 执行扩展公式.目前只被销售出库单UI重载.
			//       
			// execExtendFormula(alListData);
			// //公式情况 第一条表体记录公式查询补充数据 修改 hanwei 2003-03-05
			if (alListData != null && alListData.size() > 0) {
				//
				setAlistDataByFormula(GeneralBillVO.QRY_FIRST_ITEM_NUM, alListData);
				SCMEnv.out("0存货公式解析成功！");
				//
				m_alListData = alListData;
				// //查表体
				// // 屏蔽 by hanwei 2003-06-17 ,多余查询
				// //qryItems(new int[] { 0 }, new String[] { billID });
				// //表头执行公式
				voRet = (GeneralBillVO) alListData.get(0);
				//
				// GeneralBillHeaderVO voh[] = new GeneralBillHeaderVO[1];
				// if (voRet != null) {
				// voh[0] = (GeneralBillHeaderVO) ((GeneralBillVO) voRet)
				// .getParentVO();
				// setListHeadData(voh);
				// } else
				// SCMEnv
				// .out("nc.ui.ic.pub.bill.GeneralBillClientUI.qryBill:表头数据为空，未执行公式！");
				//
			}

		} catch (Exception e) {
			handleException(e);
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000015")/* @res "查询出错：" */
					+ e.getMessage());
		}
		return voRet;
	}

	/**
	 * 创建者：王乃军 功能：查指定序号单据的货位/序列号数据,只用于浏览状态下。 参数：指定查询模式 返回： 例外： 日期：(2001-5-9
	 * 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	protected void qryLocSN(int iBillNum, int iMode) {
		GeneralBillVO voMyBill = null;
		// arraylist 有的话用他的,没有话,就是新的.
		if (m_alListData != null && m_alListData.size() > iBillNum && iBillNum >= 0) voMyBill = (GeneralBillVO) m_alListData.get(iBillNum);
		qryLocSN(voMyBill, iMode);
	}

	private void qryLocSN(GeneralBillVO voMyBill, int iMode) {

		try {

			// ERRRR,needless to read,如新增单据的情况，
			if (voMyBill == null || voMyBill.getPrimaryKey() == null) {
				int iFaceRowCount = getBillCardPanel().getRowCount();
				// 初始化数组 if necessary
				if (m_alLocatorData == null) {
					m_alLocatorData = new ArrayList();
					for (int i = 0; i < iFaceRowCount; i++)
						m_alLocatorData.add(null);
				}
				if (m_alSerialData == null) {
					m_alSerialData = new ArrayList();
					for (int i = 0; i < iFaceRowCount; i++)
						m_alSerialData.add(null);
				}
				SCMEnv.out("null bill,init loc ,sn");
				return;
			}
			// 只在浏览状态下，才会需要赋值和查询，否则会取消已做的修改。
			if (m_iMode == BillMode.Browse) {
				// 测试此单据是否填了数量，如填了则需要继续执行，否则单据本来就没有这些数据，不用读了
				int i = 0, iRowCount = voMyBill.getItemCount();
				// 测试是否已经读过这些数据了。

				boolean hasLoc = true;
				WhVO voWh = voMyBill.getWh();
				if (voWh != null) {
					if (voWh.getIsLocatorMgt() != null && voWh.getIsLocatorMgt().intValue() != 0) {
						for (i = 0; i < iRowCount; i++) {
							if (voMyBill.getItemValue(i, "locator") == null) {
								hasLoc = false;
								break;
							}
						}
						if (hasLoc) {
							try {
								VOCheck.checkSpaceInput(voMyBill, new Integer(m_iBillInOutFlag));
							} catch (Exception e) {
								nc.vo.scm.pub.SCMEnv.error(e);
								hasLoc = false;
							}

						}

					}

				}
				InvVO voInv = null;
				boolean hasSN = true;
				for (i = 0; i < iRowCount; i++) {
					voInv = voMyBill.getItemInv(i);
					// 有序列号管理的行但此行还没有序列号。
					if (voInv != null && voInv.getIsSerialMgt() != null && voInv.getIsSerialMgt().intValue() != 0 && voMyBill.getItemValue(i, "serial") == null) {
						hasSN = false;
						break;
					}
				}
				// 已经读过数据,把数据放到成员变量中，并同步vo(needless now )2003-08-07
				if (hasLoc) m_alLocatorData = voMyBill.getLocators();
				if (hasSN) m_alSerialData = voMyBill.getSNs();
				if (hasLoc && hasSN) return;

				// =============================================================
				// 初始化数组 if necessary
				if (m_alLocatorData == null) {
					m_alLocatorData = new ArrayList();
					for (i = 0; i < iRowCount; i++)
						m_alLocatorData.add(null);
				}
				if (m_alSerialData == null) {
					m_alSerialData = new ArrayList();
					for (i = 0; i < iRowCount; i++)
						m_alSerialData.add(null);
				}

				for (i = 0; i < iRowCount; i++)
					// 测试实出/入数量
					if (voMyBill.getItemValue(i, "ninnum") != null && voMyBill.getItemValue(i, "ninnum").toString().length() > 0 || voMyBill.getItemValue(i, "noutnum") != null && voMyBill.getItemValue(i, "noutnum").toString().length() > 0) break;

				if (i >= iRowCount) // 无数量
				return; // =============================================================

				// 先清空货位、序列号数据
				Integer iSearchMode = null;
				// 需要查货位
				if (!hasLoc && (iMode == QryInfoConst.LOC_SN || iMode == QryInfoConst.LOC)) {
					iSearchMode = new Integer(iMode);
				}
				// 需要查序列号
				if (!hasSN && (iMode == QryInfoConst.LOC_SN || iMode == QryInfoConst.SN)) {
					iSearchMode = new Integer(iMode);
				}
				if (iSearchMode == null) return;
				// WhVO voWh = voMyBill.getWh();
				// 货位管理的仓库，并且还没有货位，需要读货位数据、序列号

				// iMode = 3;
				// Integer iSearchMode = new Integer(iMode);

				// ////////////////////////////iMode); //查货位 & 序列号 3 or 序列号 4
				ArrayList alAllData = (ArrayList) GeneralBillHelper.queryInfo(iSearchMode, voMyBill.getPrimaryKey());
				// 先清空货位、序列号数据
				ArrayList alTempLocatorData = null;
				ArrayList alTempSerialData = null;

				if (iMode == QryInfoConst.LOC_SN) {
					if (alAllData != null && alAllData.size() >= 2) {
						alTempLocatorData = (ArrayList) alAllData.get(0);
						alTempSerialData = (ArrayList) alAllData.get(1);
					} // else
				} else if (iMode == QryInfoConst.SN) { // === SN only
					if (alAllData != null && alAllData.size() >= 1) alTempSerialData = (ArrayList) alAllData.get(0);
					// else
				} else if (iMode == QryInfoConst.LOC) { // === LOC only
					if (alAllData != null && alAllData.size() >= 1) alTempLocatorData = (ArrayList) alAllData.get(0);
				} // else

				// --------------------------------------------------------

				// 如果有的话置货位数据
				if (alTempLocatorData != null) {
					// 放到vo中，根据表体id执行数据匹配。
					voMyBill.setLocators(alTempLocatorData);
					// getLocators处理后的 by hanwei 2004-01-06
					m_alLocatorData = voMyBill.getLocators();
				}
				// 如果有的话置序列号数据
				if (alTempSerialData != null) {
					// 放到vo中，根据表体id执行数据匹配。
					voMyBill.setSNs(alTempSerialData);
					// getSNs处理后的 by hanwei 2004-01-06
					m_alSerialData = voMyBill.getSNs();

				}

			}

		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
		}

	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-2-10 9:11:33)
	 * 
	 * @param iBillNum
	 *            int
	 * @param iMode
	 *            int 专用于列表下表体排序后的数据打印货位的操作
	 */
	public void qryLocSNSort(int iBillNum, int iMode) {
		try {
			GeneralBillVO voMyBill = null;
			// arraylist 有的话用他的,没有话,就是新的.
			// 如果表体排过序就用排序后的数据否则取全局变量中的数据
			if (m_sLastKey != null && m_voBill != null) voMyBill = m_voBill;
			else if (m_alListData != null && m_alListData.size() > iBillNum && iBillNum >= 0) voMyBill = (GeneralBillVO) m_alListData.get(iBillNum);

			// ERRRR,needless to read,如新增单据的情况，
			if (voMyBill == null || voMyBill.getPrimaryKey() == null) {
				int iFaceRowCount = getBillCardPanel().getRowCount();
				// 初始化数组 if necessary
				if (m_alLocatorData == null) {
					m_alLocatorData = new ArrayList();
					for (int i = 0; i < iFaceRowCount; i++)
						m_alLocatorData.add(null);
				}
				if (m_alSerialData == null) {
					m_alSerialData = new ArrayList();
					for (int i = 0; i < iFaceRowCount; i++)
						m_alSerialData.add(null);
				}
				SCMEnv.out("null bill,init loc ,sn");
				return;
			}
			// 只在浏览状态下，才会需要赋值和查询，否则会取消已做的修改。
			if (m_iMode == BillMode.Browse) {
				// 测试此单据是否填了数量，如填了则需要继续执行，否则单据本来就没有这些数据，不用读了
				int i = 0, iRowCount = voMyBill.getItemCount();
				// 测试是否已经读过这些数据了。

				boolean hasLoc = true;
				WhVO voWh = voMyBill.getWh();
				if (voWh != null) {
					for (i = 0; i < iRowCount; i++) {
						if (voWh.getIsLocatorMgt() != null && voWh.getIsLocatorMgt().intValue() != 0 && voMyBill.getItemValue(i, "locator") == null) {
							hasLoc = false;
							break;
						}
					}
				}
				InvVO voInv = null;
				boolean hasSN = true;
				for (i = 0; i < iRowCount; i++) {
					voInv = voMyBill.getItemInv(i);
					// 有序列号管理的行但此行还没有序列号。
					if (voInv != null && voInv.getIsSerialMgt() != null && voInv.getIsSerialMgt().intValue() != 0 && voMyBill.getItemValue(i, "serial") == null) {
						hasSN = false;
						break;
					}
				}
				// 已经读过数据,把数据放到成员变量中，并同步vo(needless now )2003-08-07
				if (hasLoc) m_alLocatorData = voMyBill.getLocators();
				if (hasSN) m_alSerialData = voMyBill.getSNs();
				if (hasLoc && hasSN) return;

				// =============================================================
				// 初始化数组 if necessary
				if (m_alLocatorData == null) {
					m_alLocatorData = new ArrayList();
					for (i = 0; i < iRowCount; i++)
						m_alLocatorData.add(null);
				}
				if (m_alSerialData == null) {
					m_alSerialData = new ArrayList();
					for (i = 0; i < iRowCount; i++)
						m_alSerialData.add(null);
				}

				for (i = 0; i < iRowCount; i++)
					// 测试实出/入数量
					if (voMyBill.getItemValue(i, "ninnum") != null && voMyBill.getItemValue(i, "ninnum").toString().length() > 0 || voMyBill.getItemValue(i, "noutnum") != null && voMyBill.getItemValue(i, "noutnum").toString().length() > 0) break;

				if (i >= iRowCount) // 无数量
				return; // =============================================================

				// 先清空货位、序列号数据
				Integer iSearchMode = null;
				// 需要查货位
				if (!hasLoc && (iMode == QryInfoConst.LOC_SN || iMode == QryInfoConst.LOC)) {
					iSearchMode = new Integer(iMode);
				}
				// 需要查序列号
				if (!hasSN && (iMode == QryInfoConst.LOC_SN || iMode == QryInfoConst.SN)) {
					iSearchMode = new Integer(iMode);
				}
				if (iSearchMode == null) return;
				// WhVO voWh = voMyBill.getWh();
				// 货位管理的仓库，并且还没有货位，需要读货位数据、序列号

				// iMode = 3;
				// Integer iSearchMode = new Integer(iMode);

				// ////////////////////////////iMode); //查货位 & 序列号 3 or 序列号 4
				ArrayList alAllData = (ArrayList) GeneralBillHelper.queryInfo(iSearchMode, voMyBill.getPrimaryKey());
				// 先清空货位、序列号数据
				ArrayList alTempLocatorData = null;
				ArrayList alTempSerialData = null;

				if (iMode == QryInfoConst.LOC_SN) {
					if (alAllData != null && alAllData.size() >= 2) {
						alTempLocatorData = (ArrayList) alAllData.get(0);
						alTempSerialData = (ArrayList) alAllData.get(1);
					} // else
				} else if (iMode == QryInfoConst.SN) { // === SN only
					if (alAllData != null && alAllData.size() >= 1) alTempSerialData = (ArrayList) alAllData.get(0);
					// else
				} else if (iMode == QryInfoConst.LOC) { // === LOC only
					if (alAllData != null && alAllData.size() >= 1) alTempLocatorData = (ArrayList) alAllData.get(0);
				} // else

				// --------------------------------------------------------

				// 如果有的话置货位数据
				if (alTempLocatorData != null) {
					// 放到vo中，根据表体id执行数据匹配。
					voMyBill.setLocators(alTempLocatorData);
					// getLocators处理后的 by hanwei 2004-01-06
					m_alLocatorData = voMyBill.getLocators();
				}
				// 如果有的话置序列号数据
				if (alTempSerialData != null) {
					// 放到vo中，根据表体id执行数据匹配。
					voMyBill.setSNs(alTempSerialData);
					// getSNs处理后的 by hanwei 2004-01-06
					m_alSerialData = voMyBill.getSNs();

				}

			}

		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
		}

	}

	/**
	 * 根据来源单据拉式生成的库存单据需要重置表头Item, 以便将name显示在列表界面和打印中得到item的名称. 功能描述: 输入参数: 返回值:
	 * 异常处理: 日期:
	 */
	public void resetAllHeaderRefItem() {
		if (getBillCardPanel().getHeadItem("cdispatcherid") != null) {
			// 收发类别
			String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("cdispatcherid").getComponent()).getRefName();
			// 保存名称以在列表形式下显示。
			if (m_voBill != null) m_voBill.setHeaderValue("cdispatchername", sName);
		}
		if (getBillCardPanel().getHeadItem("cinventoryid") != null) {
			// 加工品
			String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("cinventoryid").getComponent()).getRefName();
			// 保存名称以在列表形式下显示。
			if (m_voBill != null) m_voBill.setHeaderValue("cinventoryname", sName);
		}
		// 关联录入时, 调用库存组织afterEdit必须放在仓库的前面
		String sNewWhID = null;
		String sNewWhName = null;
		if (m_voBill.getHeaderValue(m_sMainWhItemKey) != null) {

			sNewWhName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem(m_sMainWhItemKey).getComponent()).getRefName();
			sNewWhID = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem(m_sMainWhItemKey).getComponent()).getRefPK();

		}

		if (getBillCardPanel().getHeadItem(m_sMainCalbodyItemKey) != null) {
			// 库存组织
			if (m_voBill.getHeaderValue(m_sMainCalbodyItemKey) != null) {
				nc.ui.pub.bill.BillEditEvent beEvent = new nc.ui.pub.bill.BillEditEvent(getBillCardPanel(), m_voBill.getHeaderValue(m_sMainCalbodyItemKey), m_sMainCalbodyItemKey);
				afterCalbodyEdit(beEvent);
			}
		}

		if (m_voBill.getHeaderValue(m_sMainWhItemKey) != null) {
			if (getBillCardPanel().getHeadItem(m_sMainWhItemKey) != null) {
				nc.ui.pub.bill.BillEditEvent beEvent = new nc.ui.pub.bill.BillEditEvent(getBillCardPanel(), m_voBill.getHeaderValue(m_sMainWhItemKey), m_sMainWhItemKey);
				afterWhEdit(beEvent, sNewWhName, sNewWhID);
			}

		}

		if (getBillCardPanel().getHeadItem("cwhsmanagerid") != null) {
			// 库管员

			String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("cwhsmanagerid").getComponent()).getRefName();
			// 保存名称以在列表形式下显示。
			if (m_voBill != null) m_voBill.setHeaderValue("cwhsmanagername", sName);
		}
		if (getBillCardPanel().getHeadItem("cdptid") != null) {
			// 部门
			String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("cdptid").getComponent()).getRefName();
			// 保存名称以在列表形式下显示。
			if (m_voBill != null) m_voBill.setHeaderValue("cdptname", sName);
		}
		if (getBillCardPanel().getHeadItem("cbizid") != null) {
			// 业务员

			String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("cbizid").getComponent()).getRefName();
			// 保存名称以在列表形式下显示。
			if (m_voBill != null) m_voBill.setHeaderValue("cbizname", sName);
		}
		if (getBillCardPanel().getHeadItem("cproviderid") != null) {
			// 供应商

			String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("cproviderid").getComponent()).getRefName();
			String sRefPK = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("cproviderid").getComponent()).getRefPK();
			// 保存名称以在列表形式下显示。
			if (m_voBill != null) m_voBill.setHeaderValue("cprovidername", sName);
			// 根据客户或供应商过滤发运地址的参照
			if (getBillCardPanel().getHeadItem("vdiliveraddress") != null && sRefPK != null) {

				((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("vdiliveraddress").getComponent()).setWhereString("pk_cubasdoc = (select pk_cubasdoc from bd_cumandoc where pk_cumandoc = '" + sRefPK + "')");
			}
		}
		if (getBillCardPanel().getHeadItem("ccustomerid") != null) {
			// 客户

			String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("ccustomerid").getComponent()).getRefName();
			String sRefPK = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("ccustomerid").getComponent()).getRefPK();
			// 保存名称以在列表形式下显示。
			if (m_voBill != null) m_voBill.setHeaderValue("ccustomername", sName);
			// 根据客户或供应商过滤发运地址的参照

			filterVdiliveraddressRef(true, -1);

			if (getBillCardPanel().getHeadItem("vdiliveraddress") != null && sRefPK != null) {
				((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("vdiliveraddress").getComponent()).setWhereString("pk_cubasdoc = (select pk_cubasdoc from bd_cumandoc where pk_cumandoc = '" + sRefPK + "')");
			}
		}
		if (getBillCardPanel().getHeadItem("cbiztype") != null) {
			// 业务类型

			String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("cbiztype").getComponent()).getRefName();
			// String sPK = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
			// .getHeadItem("cbiztype").getComponent()).getRefPK();
			// 保存名称以在列表形式下显示。
			if (m_voBill != null) m_voBill.setHeaderValue("cbiztypename", sName);
		}
		if (getBillCardPanel().getHeadItem("cdilivertypeid") != null) {
			// 发运方式

			String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("cdilivertypeid").getComponent()).getRefName();
			// 保存名称以在列表形式下显示。
			if (m_voBill != null) m_voBill.setHeaderValue("cdilivertypename", sName);
		}
		if (getBillCardPanel().getHeadItem("vdiliveraddress") != null) {
			// 发运地址

			String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("vdiliveraddress").getComponent()).getUITextField().getText();
			;
			// 保存名称以在列表形式下显示。
			if (m_voBill != null) m_voBill.setHeaderValue("vdiliveraddressname", sName);
		}
	}

	/**
	 * 此处插入方法说明。 更新单行 创建日期：(2002-11-27 10:32:34)
	 * 
	 * @return int
	 * @param vo
	 *            nc.vo.ic.pub.barcodeparse.BarCodeParseVO
	 */
	public void scanAddBarcodeline(nc.vo.ic.pub.barcodeparse.BarCodeParseVO vo) throws Exception {

		if (vo == null) return;

		String sRowPrimaryKey = getBarcodeCtrl().getBarcodeRowPrimaryKey(m_sCorpID, vo);

		// 通过条码规则中定义的关键字列
		String[] sPrimaryKeyItems = vo.getMatchPrimaryKeyItems();

		BarCodeParseVO[] barCodeParseVOs = new BarCodeParseVO[] {
			vo
		};
		boolean bBox = false;
		scanadd(sRowPrimaryKey, barCodeParseVOs, bBox, sPrimaryKeyItems);
		// 光标回条码扫描框
		m_utfBarCode.requestFocus();
		return;
	}

	/**
	 * 此处插入方法说明。 箱条码扫描解析到表体 创建日期：(2004-3-12 15:57:22)
	 * 
	 * @param vo
	 *            nc.vo.ic.pub.barcodeparse.BarCodeGroupVO
	 */
	public void scanAddBoxline(BarCodeGroupVO barCodeGroupVO) throws Exception {

		BarCodeGroupHeadVO barCodeGroupHeadVO = (BarCodeGroupHeadVO) barCodeGroupVO.getParentVO();

		BarCodeParseVO[] barCodeParseVOs = (BarCodeParseVO[]) barCodeGroupVO.getChildrenVO();
		if (barCodeParseVOs == null || barCodeParseVOs.length == 0) { return; }

		// 需要通过barCodeGroupHeadVO获得关键字列
		// 目前条码装箱没有设置存货自由项等属性
		// 所以设置成存货主键
		String[] sPrimaryKeyItems = new String[] {
			nc.vo.ic.pub.barcodeparse.BarcodeparseCtrl.InvManKey
		};

		String sRowPrimaryKey = getBarcodeCtrl().getBarGroupHeadRowPrimaryKey(m_sCorpID, barCodeGroupHeadVO, sPrimaryKeyItems);
		boolean bBox = true;
		scanadd(sRowPrimaryKey, barCodeParseVOs, bBox, sPrimaryKeyItems);
		// 光标回条码扫描框
		m_utfBarCode.requestFocus();
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-3-12 22:26:14)
	 * 
	 * @param sRowPrimaryKey
	 *            java.lang.String
	 * @param barCodeParseVOs
	 *            nc.vo.ic.pub.barcodeparse.BarCodeParseVO[]
	 */
	protected void scanadd(String sRowPrimaryKey, BarCodeParseVO[] barCodeParseVOs, boolean bBox, String[] sPrimaryKeyItems) throws Exception {
		try {

			if (sRowPrimaryKey != null && barCodeParseVOs != null && sRowPrimaryKey.length() > 4 && !sRowPrimaryKey.startsWith("NULL")) // 箱条码中存在存货信息
			{
				// 取当前行
				int iRow = getBillCardPanel().getBillTable().getSelectedRow();

				// 到单据中查找行符合条件的行arraylist ,优先处理选中行
				ArrayList alResultTemp = getBarcodeCtrl().scanBillCardItem(sRowPrimaryKey, m_voBill, iRow, sPrimaryKeyItems);

				ArrayList alResult = new ArrayList();

				// 对箱条码，只适用第一行，处于实际数量填充的校验
				// 否则无法处理这种情况的数据异常回滚
				if (bBox && alResultTemp != null && alResultTemp.size() > 0) {
					alResult.add(alResultTemp.get(0));
				} else alResult = alResultTemp;

				// 如果arraylist为空，或len==0，提示没有对应存货数据
				if ((alResult == null || alResult.size() == 0)) {
					// (1)对调拨出入库单不允许自动增加行的情况下
					// (2)对其他入和其他出界面上，也不允许扫描自动增加行
					if (getButtonTree().getButton(ICButtonConst.BTN_LINE_ADD) != null && getButtonTree().getButton(ICButtonConst.BTN_LINE_ADD).isEnabled() && getBarcodeCtrl().isAddNewInvLine()) {
						// 没有对应的存货，自动增加一行存货
						java.util.ArrayList alInvID = new java.util.ArrayList();
						alInvID.add(sRowPrimaryKey);
						int iCurFixLine = getBarcodeCtrl().fixBlankLineWithInv(this, m_voBill, alInvID, m_sInvMngIDItemKey, m_sMainWhItemKey, m_sMainCalbodyItemKey, m_sBillTypeCode, m_sBillRowNo, sPrimaryKeyItems);
						// 置回光标
						m_utfBarCode.requestFocus();

						// int rowNow = 0;
						boolean bAllforFix = true;
						int iNumUsed = 0;
						scanfixline(barCodeParseVOs, iCurFixLine, iNumUsed, bAllforFix);
						int icurline = getBillCardPanel().getBillTable().getSelectedRow();
						if (icurline >= 0) {
							String vbatchcode = (String) getBillCardPanel().getBodyValueAt(icurline, IItemKey.VBATCHCODE);
							if (vbatchcode != null && vbatchcode.trim().length() > 0) {
								BillEditEvent ev = new BillEditEvent(getBillCardPanel().getBodyItem(IItemKey.VBATCHCODE), null, vbatchcode, IItemKey.VBATCHCODE, icurline, BillItem.BODY);
								afterLotEdit(ev);
							}
						}
					} else {

						errormessageshow(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000127")/*
																														* @res
																														* "扫描识别出新的存货条码，但当前单据界面不允许新增加存货行！"
																														*/);

					}
				} else {
					int icurline = Integer.parseInt(alResult.get(0).toString());
					getBillCardPanel().getBillTable().getSelectionModel().setSelectionInterval(icurline, icurline);
					scanUpdateLine(barCodeParseVOs, alResult);
				}
			} else // 条码中不存在存货信息
			{
				// 需要找当前焦点行，进行扫描处理
				scanUpdateLineSelect(barCodeParseVOs);
			}
		} catch (Exception e) {
			throw nc.ui.ic.pub.tools.GenMethod.handleException(null, null, e);
		}
	}

	/**
	 * 类型说明：发运地址修改后处理 创建日期：(2005-09-15 14:12:13) 作者：yb 修改日期： 修改人： 修改原因： 算法说明：
	 */
	public void afterVdiliveraddress(nc.ui.pub.bill.BillEditEvent e) {
		if (e.getPos() == BillItem.HEAD) {
			String vdiliveraddress = ((UIRefPane) getBillCardPanel().getHeadItem("vdiliveraddress").getComponent()).getUITextField().getText();
			String bodyaddress = null;
			if (vdiliveraddress != null) {
				for (int i = 0, loop = getBillCardPanel().getRowCount(); i < loop; i++) {
					bodyaddress = (String) getBillCardPanel().getBodyValueAt(i, "vdiliveraddress");
					if (bodyaddress == null) {
						getBillCardPanel().setBodyValueAt(vdiliveraddress, i, "vdiliveraddress");
						if (getBillCardPanel().getBillModel().getRowState(i) == BillModel.NORMAL) getBillCardPanel().getBillModel().setRowState(i, BillModel.MODIFICATION);
					}
				}
			}
		} else if (e.getPos() == BillItem.BODY) {

		}
	}

	/**
	 * 过滤发运地址参照 创建者：张欣 功能：初始化参照过滤 参数： 返回： 例外： 日期：(2001-7-17 10:33:20)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 */
	public void filterVdiliveraddressRef(boolean ishead, int row) {
		try {
			// 过滤表头发运地址
			BillItem bicu = getBillCardPanel().getHeadItem("ccustomerid");
			if (bicu == null) return;
			String ccustomerid = (String) bicu.getValueObject();

			if (ishead) {

				BillItem bmaddr = getBillCardPanel().getHeadItem("vdiliveraddress");
				if (bmaddr != null) {
					nc.ui.pub.beans.UIRefPane vdlvr = (nc.ui.pub.beans.UIRefPane) bmaddr.getComponent();
					if (vdlvr != null) {
						if (ccustomerid == null || ccustomerid.trim().length() <= 0) {
							vdlvr.setWhereString(" 11=11 ");
						} else {
							vdlvr.setWhereString(" pk_cubasdoc = (select pk_cubasdoc from bd_cumandoc where pk_cumandoc = '" + ccustomerid + "') ");
						}
					}
				}

			} else {

				BillItem bmaddr = getBillCardPanel().getBodyItem("vdiliveraddress");
				if (bmaddr != null) {
					nc.ui.pub.beans.UIRefPane vdlvr = (nc.ui.pub.beans.UIRefPane) bmaddr.getComponent();
					if (vdlvr != null) {
						if (ccustomerid == null || ccustomerid.trim().length() <= 0) {
							// ((nc.ui.prm.ref.CustAddrRefModel)
							// vdlvr.getRefModel()).setCustId(null);
						} else {
							// ((nc.ui.prm.ref.CustAddrRefModel)
							// vdlvr.getRefModel()).setCustId(ccustomerid);
						}

					}
				}

			}
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
		}
	}

	/**
	 * 此处插入方法说明。 供子类使用，检验条码编辑修改数量和应发数量等数量关系的业务逻辑 创建日期：(2004-4-29 9:08:49)
	 * 
	 * @param event1
	 *            nc.ui.pub.bill.BillEditEvent
	 */
	public void scanCheckNumEdit(nc.ui.pub.bill.BillEditEvent event1) throws Exception {

	}

	/**
	 * 此处插入方法说明。 把条码VO数据填充到表体 创建日期：(2004-3-12 20:15:07)
	 * 
	 * @return int
	 * @param barCodeParseVOs
	 *            nc.vo.ic.pub.barcodeparse.BarCodeParseVO[]：条码数据 VO
	 * 
	 * @param iCurFixLine
	 *            int：当前的行,
	 * 
	 * @param iNumUsed
	 *            int：已经填充使用过的条码数量
	 * 
	 * @param bAllforFix
	 *            java.lang.Boolean ：是否要把剩余的条码都填上
	 * 
	 *            对于只有一行符合条件的存货情况和最后一行情况下，要把剩余的条码都填上
	 * 
	 *            返回本次填充条码的数量
	 */
	public int scanfixline(BarCodeParseVO[] barCodeParseVOs, int iCurFixLine, int iNumUsed, boolean bAllforFix) throws Exception {

		if (barCodeParseVOs == null || barCodeParseVOs.length == 0 || iCurFixLine < 0) { return 0; }
		String sInvID = (String) getBillCardPanel().getBodyValueAt(iCurFixLine, m_sInvMngIDItemKey);
		if (sInvID == null || sInvID.length() == 0) { throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000111")/* @res "请选择有存货数据的行！" */); }
		// 填充数量
		int iNumforUse = scanfixline_fix(barCodeParseVOs, iCurFixLine, iNumUsed, bAllforFix); // 本次填充条码的数量

		// 保存条码
		getBarcodeCtrl().scanfixline_save(barCodeParseVOs, iCurFixLine, iNumUsed, iNumforUse, m_voBill.getItemVOs()); // 本次填充条码的数量

		return iNumforUse;
	}

	/**
	 * 此处插入方法说明。 把条码VO数据填充到表体 创建日期：(2004-3-12 20:15:07)
	 * 
	 * @return int
	 * @param barCodeParseVOs
	 *            nc.vo.ic.pub.barcodeparse.BarCodeParseVO[]：条码数据 VO
	 * 
	 * @param iCurFixLine
	 *            int：当前的行,
	 * 
	 * @param iNumUsed
	 *            int：已经填充使用过的条码数量
	 * 
	 * @param bAllforFix
	 *            java.lang.Boolean ：是否要把剩余的条码都填上
	 * 
	 *            对于只有一行符合条件的存货情况和最后一行情况下，要把剩余的条码都填上
	 * 
	 *            返回本次填充条码的数量
	 */
	protected int scanfixline_fix(BarCodeParseVO[] barCodeParseVOs, int iCurFixLine, int iNumUsed, boolean bAllforFix) throws Exception {

		if (barCodeParseVOs == null || barCodeParseVOs.length == 0 || iCurFixLine < 0) { return 0; }

		int iNumforUse = 0; // 本次填充条码的数量
		if (getBillCardPanel().getBodyItem("cinventorycode") != null) {
			// 实际发数量
			UFDouble nFactNum = null;
			// 应发数量
			UFDouble nShouldNum = null;
			UFDouble nFactBarCodeNum = null; // 实际发，实际入条码数量

			nc.vo.ic.pub.bc.BarCodeVO[] oldBarcodevos = m_voBill.getItemVOs()[iCurFixLine].getBarCodeVOs();

			if (oldBarcodevos == null || oldBarcodevos.length == 0) nFactBarCodeNum = UFDZERO;
			else {
				nFactBarCodeNum = UFDZERO;
				for (int i = 0; i < oldBarcodevos.length; i++) {
					if (oldBarcodevos[i] != null && oldBarcodevos[i].getNnumber() != null) nFactBarCodeNum = nFactBarCodeNum.add(oldBarcodevos[i].getNnumber());
				}
			}

			// 实际发数量
			Object oNum = getBillCardPanel().getBodyValueAt(iCurFixLine, m_sNumItemKey);
			if (oNum == null || oNum.toString().trim().length() == 0) {
				nFactNum = null;
				// 如果没有应发数量，则填充全部数量
			} else nFactNum = (UFDouble) oNum;

			// 应发数量

			try {
				oNum = getBillCardPanel().getBodyValueAt(iCurFixLine, m_sShouldNumItemKey);
			} catch (Exception e) {
				oNum = null;
				nc.vo.scm.pub.SCMEnv.error(e);
			}
			if (oNum == null || oNum.toString().trim().length() == 0) {
				nShouldNum = null;
				// 如果没有应发数量，则填充全部数量
			} else nShouldNum = (UFDouble) oNum;

			boolean bNegative = false; // 是否负数
			if ((nFactNum != null && nFactNum.doubleValue() < 0) || (nShouldNum != null && nShouldNum.doubleValue() < 0)) {
				bNegative = true;
			}

			// 分配条码数据到多个匹配行的算法
			iNumforUse = getBarcodeCtrl().scanfixlinenum(barCodeParseVOs, oldBarcodevos, iCurFixLine, iNumUsed, bAllforFix, nFactNum, nShouldNum);

			nFactBarCodeNum = nFactBarCodeNum.add(iNumforUse);

			// add by hanwei 2004-6-2
			// 条码数量大于应发数量,并且在盘点单生成的其他入出情况霞
			// 不能超过应发数量，这样修改的实发数量等于应发数量
			if (nShouldNum != null && nFactBarCodeNum != null && nFactBarCodeNum.doubleValue() > nShouldNum.doubleValue() && !getBarcodeCtrl().isOverShouldNum()) {
				nFactBarCodeNum = nShouldNum.abs();
			}

			if (nFactNum == null) nFactNum = UFDZERO;

			// 实发数量小于条码数量，才去修改界面上的实发数量
			if (nFactNum.doubleValue() < nFactBarCodeNum.doubleValue()) {

				// 同步实发数量
				if (bNegative || m_bFixBarcodeNegative) nFactBarCodeNum = nFactBarCodeNum.multiply(UFDNEGATIVE);

				getBillCardPanel().setBodyValueAt(nFactBarCodeNum, iCurFixLine, m_sNumItemKey);

				if (getBillCardPanel().getBodyItem("nbarcodenum") != null) getBillCardPanel().setBodyValueAt(nFactBarCodeNum, iCurFixLine, "nbarcodenum");

				if (getBillCardPanel().getBodyItem("m_sNumbarcodeItemKey") != null) getBillCardPanel().setBodyValueAt(nFactBarCodeNum, iCurFixLine, m_sNumbarcodeItemKey);

				// 应发数量不用同步
				nc.ui.pub.bill.BillEditEvent event1 = new nc.ui.pub.bill.BillEditEvent(getBillCardPanel().getBodyItem(m_sNumItemKey), nFactBarCodeNum, m_sNumItemKey, iCurFixLine);
				// 检查数量编辑业务逻辑
				scanCheckNumEdit(event1);
				afterEdit(event1);
				// 执行模版公式
				getGenBillUICtl().execEditFormula(getBillCardPanel(), iCurFixLine, m_sNumItemKey);

				// 触发单据行状态为修改
				if (getBillCardPanel().getBodyValueAt(iCurFixLine, m_sBillRowItemKey) != null) getBillCardPanel().getBillModel().setRowState(iCurFixLine, BillMode.Update);
			}

		}

		return iNumforUse;
	}

	/**
	 * 此处插入方法说明。 逐行更新条码数据， 对超过应发数量的条码数量，移到下一行更新
	 * 
	 * barCodeParseVOs:条码数据VO[] alFixRowNO：ArrayList，每行存有存货行行数据，String 类型 int
	 * 的数据
	 * 
	 * 创建日期：(2004-3-12 19:23:05)
	 * 
	 * @param barCodeParseVOs
	 *            nc.vo.ic.pub.barcodeparse.BarCodeParseVO[]
	 * @param alFixRowNO
	 *            java.util.ArrayList
	 */
	protected void scanUpdateLine(BarCodeParseVO[] barCodeParseVOs, ArrayList alFixRowNO) throws Exception {

		if (barCodeParseVOs == null || barCodeParseVOs.length == 0 || alFixRowNO == null || alFixRowNO.size() == 0) { return; }

		int iNumAll = barCodeParseVOs.length;
		int iNumUsed = 0; // 累计统计行数
		int ifixRows = alFixRowNO.size();
		int iCurFixLine = 0; // 更新当前行
		int ifixSingleLineNum = 0;

		for (int i = 0; i < ifixRows; i++) {
			iCurFixLine = Integer.parseInt((String) alFixRowNO.get(i));

			if (ifixRows == 1) {
				// 只有一行，全部填充当前行
				ifixSingleLineNum = scanfixline(barCodeParseVOs, iCurFixLine, 0, true);
				break;
			} else {
				if (i == ifixRows - 1) // 最后一行，填充所有的数量
				{
					ifixSingleLineNum = scanfixline(barCodeParseVOs, iCurFixLine, iNumUsed, true);
					break;
				} else // 中间行填充应发的数量
				{
					ifixSingleLineNum = scanfixline(barCodeParseVOs, iCurFixLine, iNumUsed, false);
				}
				iNumUsed = iNumUsed + ifixSingleLineNum;
				if (iNumUsed == iNumAll) {
					// 填充完毕了
					break;
				}
			}

		}
	}

	/**
	 * 此处插入方法说明。 把条码VO数据填充到表体的焦点行 创建日期：(2004-3-12 20:15:07)
	 * 
	 * @return int
	 * @param barCodeParseVOs
	 *            nc.vo.ic.pub.barcodeparse.BarCodeParseVO[]：条码数据 VO
	 */
	protected void scanUpdateLineSelect(BarCodeParseVO[] barCodeParseVOs) throws Exception {
		// 取当前行
		int iCurFixLine = 0;
		int rowNow = getBillCardPanel().getBillTable().getSelectedRow();
		if (rowNow < 0) {
			// 提示错误信息
			throw new nc.vo.pub.BusinessException(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000112")/*
																																 * @res
																																 * "请选择对应条码的存货行！"
																																 */);
		} else {
			iCurFixLine = rowNow;
		}
		boolean bAllforFix = true;
		int iNumUsed = 0;

		if (m_voBill != null && m_voBill.getItemVOs() != null && m_voBill.getItemVOs().length > iCurFixLine && m_voBill.getItemVOs()[iCurFixLine] != null) {
			if (m_voBill.getItemVOs()[iCurFixLine].getBarcodeManagerflag().booleanValue() && m_voBill.getItemVOs()[iCurFixLine].getBarcodeClose().booleanValue() == false) {
				scanfixline(barCodeParseVOs, iCurFixLine, iNumUsed, bAllforFix);
			} else {
				throw new nc.vo.pub.BusinessException(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000113")/*
																																	* @res "当前行非条码管理或者行条码已关闭！！"
																																	*/);
			}
		}
	}

	/**
	 * 此处插入方法说明。 功能描述:处理翻页。 作者：程起伍 输入参数:当前页码。 返回值: 异常处理: 日期:(2003-7-5 13:14:52)
	 * 
	 * @param iSelect
	 *            int
	 */
	protected void scrollBill(int iCur) {
		if (m_alListData != null && m_alListData.size() > 0) {
			m_voBill = (GeneralBillVO) m_alListData.get(iCur);
			GeneralBillItemVO voitem[] = m_voBill.getItemVOs();
			if (voitem == null || voitem.length == 0) qryItems(new int[] {
				iCur
			}, new String[] {
				m_voBill.getPrimaryKey()
			});
			// re-get
			m_voBill = (GeneralBillVO) m_alListData.get(iCur);
			setBillVO(m_voBill);
			setLastHeadRow(iCur);
			m_iCurDispBillNum = m_iLastSelListHeadRow;
			selectListBill(iCur);
		}
	}

	/**
	 * 创建者：王乃军 功能：用于修改后设置新增行的PK,并同时刷新传入的VO. 要保证VO中Item的顺序和界面数据一致。 参数： 返回： 例外：
	 * 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	protected void setBarcodePkAfterImp(ArrayList alBodyPK) {
		nc.ui.pub.bill.BillModel bmTemp = getBillCardPanel().getBillModel();
		if (bmTemp == null) {
			SCMEnv.out("bm null ERROR!");
			return;
		}
		if (alBodyPK == null || alBodyPK.size() == 0) {
			SCMEnv.out("no row add.");
			return;
		}
		int rowCount = getBillCardPanel().getRowCount();
		int pk_count = 0;
		for (int row = 0; row < rowCount; row++) {

			// 如果该表体行有条码,则将条码ID置入BarCodeVO中
			if (m_voBill.getItemVOs()[row].getBarCodeVOs() != null) {
				for (int j = 1; j <= m_voBill.getItemVOs()[row].getBarCodeVOs().length; j++) {
					if (m_voBill.getItemVOs()[row].getBarCodeVOs()[j - 1] != null && m_voBill.getItemVOs()[row].getBarCodeVOs()[j - 1].getStatus() == nc.vo.pub.VOStatus.NEW) {
						// 回置条码主键
						m_voBill.getItemVOs()[row].getBarCodeVOs()[j - 1].setPrimaryKey(alBodyPK.get(pk_count + 1).toString().trim());
						// 回置表体主键
						m_voBill.getItemVOs()[row].getBarCodeVOs()[j - 1].setCgeneralbid(m_voBill.getItemVOs()[row].getCgeneralbid());
					}
				}

			}
		}
	}

	/**
	 * 创建者：zhx 功能：在表单设置条码VO的状态 参数： 返回： 例外： 日期：(2003-10-21 9:23:32)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	public void setBillBCVOStatus(GeneralBillVO bvo, int Status) {

		if (bvo != null && bvo.getItemVOs() != null) {
			GeneralBillItemVO itemVO = null;
			BarCodeVO[] bcvos = null;
			BarCodeVO[] bcvosTemp = null;
			java.util.ArrayList alBarcodeVO = null;
			for (int i = 0; i < bvo.getItemVOs().length; i++) {
				itemVO = bvo.getItemVOs()[i];
				bcvos = itemVO.getBarCodeVOs();
				if (bcvos != null) {
					alBarcodeVO = new java.util.ArrayList();
					for (int j = 0; j < bcvos.length; j++) {
						if (bcvos[j].getStatus() != nc.vo.pub.VOStatus.DELETED) {
							bcvos[j].setStatus(Status);
							alBarcodeVO.add(bcvos[j]);
						}
					}
					if (alBarcodeVO.size() > 0) {
						bcvosTemp = new BarCodeVO[alBarcodeVO.size()];
						alBarcodeVO.toArray(bcvosTemp);
						itemVO.setBarCodeVOs(bcvosTemp);
					} else {
						itemVO.setBarCodeVOs(null);
					}
				}

			}

		}

	}

	/**
	 * 此处插入方法说明。 把多张单据VO合并成一张 对采购入库单据，需要重载本方法 创建日期：(2004-3-17 15:35:51)
	 * 
	 * @return nc.vo.ic.pub.bill.GeneralBillVO
	 * @param vos
	 *            nc.vo.pub.AggregatedValueObject[]
	 */
	// protected GeneralBillVO setBillRefResultCombinVo(
	// nc.vo.pub.AggregatedValueObject[] vos) throws Exception {
	//
	// nc.ui.ic.pub.pfconv.VoHandle handle = new nc.ui.ic.pub.pfconv.VoHandle();
	// // 默认检查库存组织按表头的方式来进行
	// GeneralBillVO voRet = handle.combinVo(vos, true);
	//
	// return voRet;
	// }
	/**
	 * 此处插入方法说明。 把拉式订单上的数据加载到库存单据界面上 BusiTypeID：业务类型ID,如果没有为null
	 * vos:单据的AggregatedValueObject[]，实际数据VO是普通单据的VO
	 * 目前该方法被普通单据基本类和采购入库、委外加工入库所引用 创建日期：(2003-10-14 14:29:30)
	 * 
	 * @param BusiTypeID
	 *            java.lang.String
	 * @param vos
	 *            nc.vo.pub.AggregatedValueObject[]
	 */
	protected void setBillRefResultVO(String sBusiTypeID, nc.vo.pub.AggregatedValueObject[] vos) throws Exception {

		if (vos == null || vos.length == 0) throw new BusinessException(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UCH003")/* @res "请选择要处理的数据！" */);

		if (vos != null && !(vos instanceof GeneralBillVO[])) { throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000114")/*
																																									* @res
																																									* "单据类型转换错误！请查看控制台的详细提示信息。"
																																									*/); }

		// v5 不是整合VO，而是验证VO的正确性，清空某些字段值
		GeneralBillVO voRet = VoCombine.combinVo(vos);

		if (voRet != null && voRet.getItemVOs() != null && voRet.getItemVOs().length > 0) {

			// set user selected 业务类型 20021209
			voRet.setHeaderValue("cbiztype", sBusiTypeID);
			// 通过单据公式容器类执行有关公式解析的方法
			getFormulaBillContainer().formulaHeaders(getFormulaItemHeader(), voRet.getHeaderVO());
			BatchCodeDefSetTool.execFormulaForBatchCode(voRet.getItemVOs());

			// 保存传入的单据VO，向替换件参照传入的存货ID始终是该单据VO中存货ID。
			m_voBillRefInput = voRet;
			// lTime = System.currentTimeMillis();
			// 新增单据1
			onAdd(false, null);
			
			// 设置退库状态和只能输入负数 add by hanwei 2004-05-08
			if (voRet.getHeaderVO() != null && voRet.getHeaderVO().getFreplenishflag().booleanValue()) {
				nc.ui.ic.pub.bill.GeneralBillUICtl.setSendBackBillState(this, true);
			} else nc.ui.ic.pub.bill.GeneralBillUICtl.setSendBackBillState(this, false);

			// 清空单据号
			voRet.getHeaderVO().setPrimaryKey(null);
			voRet.getHeaderVO().setVbillcode(null);

			// 增加单据行号：zhx added on 20030630 support for incoming bill
			nc.ui.scm.pub.report.BillRowNo.setVORowNoByRule(voRet, m_sBillTypeCode, m_sBillRowNo);

			// 重设仓库数据
			resetWhInfo(voRet);

			// 重设所有存货数据
			resetAllInvInfo(voRet);

			// 重设新增单据初始数据，因为setTempBillVO把他们清掉了。
			int iOriginalItemCount = voRet.getItemCount();
			// 滤掉多余的行。
			GeneralBillItemVO[] itemvo = m_voBill.filterItem();
			if (itemvo == null || itemvo.length == 0) {
				m_voBill = null;
				clearUi();
				m_voBillRefInput = null;
				throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000115")/*
																													* @res
																													* "参照单据中存货不能为折扣或劳务属性的存货，请修改存货后再关联录入。"
																													*/);

			} else if (iOriginalItemCount > itemvo.length) { // 真的滤掉了行
				m_voBill.setChildrenVO(itemvo);
				setImportBillVO(m_voBill, false);
				m_voBillRefInput.setChildrenVO(itemvo);

			}

			// 重设所有所有条形码状态 修改人:刘家清 修改日期:2007-04-04
			if (m_voBill.getItemVOs() != null) {

				for (int i = 0; i < m_voBill.getItemVOs().length; i++) {

					if (m_voBill.getItemVOs()[i].getBarCodeVOs() != null) {
						nc.vo.ic.pub.SmartVOUtilExt.modifiVOStatus(m_voBill.getItemVOs()[i].getBarCodeVOs(), nc.vo.pub.VOStatus.NEW);
					}
				}
			}

			setNewBillInitData();

			// /手工之发运地址。
			setDeliverAddressByHand();

			setButtonStatus(true);

			ctrlSourceBillUI(true);

			// set user selected 业务类型 20021209
			if (getBillCardPanel().getHeadItem("cbiztype") != null && sBusiTypeID != null) {
				getBillCardPanel().setHeadItem("cbiztype", sBusiTypeID);

				nc.ui.pub.bill.BillEditEvent event = new nc.ui.pub.bill.BillEditEvent(getBillCardPanel().getHeadItem("cbiztype"), sBusiTypeID, "cbiztype");
				afterEdit(event);
			}
			// end set user selected 业务类型

			// showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
			// "SCMCOMMON", "UPPSCMCommon-000133")/* @res "就绪" */);

		} else {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000116")/*
																											* @res
																											* "请双击选择参照录入单据的表头，表体行！"
																											*/);
		}

	}

	/*
	 * 针对单据：调拨出入库单 两个作用： 1 设置调入仓库-4Y /出货仓库-4E 参照过滤 2 对调拨出库单表体的收货单位参照作过滤
	 * 
	 * 邵兵 on Jun 13, 2005
	 */
	protected void setBillRefIn4Eand4Y(GeneralBillVO voBill) {
		nc.ui.pub.bill.BillItem bi = null;

		// 调入仓库-4Y /出货仓库-4E 参照

		bi = getBillCardPanel().getHeadItem("cotherwhid");

		if (bi != null) {
			nc.ui.pub.beans.UIRefPane ref = (nc.ui.pub.beans.UIRefPane) bi.getComponent();
			if (ref != null) {
				ref.getRefModel().setPk_corp((String) voBill.getHeaderValue("cothercorpid"));

				int fallocflag = CONST.IC_ALLOCINSTORE; // 入库调拨
				String isDirectStor = "N";

				if (voBill.getHeaderValue("fallocflag") != null) fallocflag = voBill.getHeaderVO().getFallocflag().intValue();

				if (fallocflag == CONST.IC_ALLOCDIRECT) // 若为直运调拨
				isDirectStor = "Y";

				ref.setWhereString(" pk_calbody ='" + (String) voBill.getHeaderValue("cothercalbodyid") + "' and isdirectstore='" + isDirectStor + "'");
			}
		}
		Object coutcorpid = voBill.getHeaderVO().getAttributeValue("coutcorpid");

		bi = getBillCardPanel().getHeadItem("cproviderid");

		if (bi != null) ((nc.ui.pub.beans.UIRefPane) bi.getComponent()).getRefModel().setPk_corp(coutcorpid.toString());

		bi = getBillCardPanel().getHeadItem("ccustomerid");

		if (bi != null) ((nc.ui.pub.beans.UIRefPane) bi.getComponent()).getRefModel().setPk_corp(coutcorpid.toString());

		// 对调拨出库单表体的收货单位参照作过滤。
		if (!m_sBillTypeCode.equals("4Y")) // 非调拨出库单
		return;
		// 收货单位
		bi = getBillCardPanel().getBodyItem("vrevcustname");
		if (bi != null) {
			nc.ui.pub.beans.UIRefPane ref = (nc.ui.pub.beans.UIRefPane) bi.getComponent();
			if (ref != null) ref.getRefModel().setPk_corp((String) voBill.getHeaderValue("cothercorpid"));
		}
	}

	/**
	 * 创建者：王乃军 功能：在表单设置显示VO 参数： 返回： 例外： 日期：(2001-5-9 9:23:32)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	public void setBillVO(GeneralBillVO bvo, boolean bUpdateBotton, boolean bExeFormule) {
		// 如果是空，清空显示
		if (bvo == null) {
			getBillCardPanel().getBillData().clearViewData();
			getBillCardPanel().updateValue();
			return;
		}

		try {
			long lTime = System.currentTimeMillis();
			getBillCardPanel().getBillModel().removeTableModelListener(this);
			getBillCardPanel().removeBillEditListenerHeadTail();
			// 保存一个clone()
			m_voBill = (GeneralBillVO) bvo.clone();

			// 调拨出入库单的两个参照特殊处理
			// 不应放在基类方法中。
			// shawbing on Jun 13, 2005
			if (m_sBillTypeCode.equals("4Y") || m_sBillTypeCode.equals("4E")) setBillRefIn4Eand4Y(m_voBill);

			// 使用公式查询单据需要手工计算生产日期
			// 需要的话，计算生产日期
			if (m_bIsByFormula) bvo.calPrdDate();
			SCMEnv.showTime(lTime, "setBillVO:bvo.clone()");
			// 设置数据
			lTime = System.currentTimeMillis();
			getBillCardPanel().setBillValueVO(bvo);

			for (int i = 1; i <= 20; i++) {
				String key = "vuserdef" + i;
				BillItem item = getBillCardPanel().getHeadItem(key);
				if (item != null && item.getDataType() == 7) {
					String pk = (String) m_voBill.getHeaderValue("pk_defdoc" + i);
					if (pk != null && pk.length() > 0) ((UIRefPane) item.getComponent()).setPK(bvo.getHeaderValue("pk_defdoc" + i));
				}
			}

			SCMEnv.showTime(lTime, "setBillVO:setBillValueVO");
			// 执行公式
			/** ydy 2005-06-21 */
			if (bExeFormule) {
				getBillCardPanel().getBillModel().execLoadFormula();
				execHeadTailFormulas();
			}
			// 更新状态
			lTime = System.currentTimeMillis();
			getBillCardPanel().updateValue();
			// 清存货现存量数据
			bvo.clearInvQtyInfo();
			// 选中第一行，光标移到表体
			getBillCardPanel().getBillTable().setRowSelectionInterval(0, 0);
			getBillCardPanel().transferFocusTo(1);
			// 重置序列号是否可用
			setBtnStatusSN(0, false);

			SCMEnv.showTime(lTime, "setBillVO:3");
			// 刷新现存量显示
			setTailValue(0);
			// 整理货位数据，序列号。
			lTime = System.currentTimeMillis();
			int iRowCount = bvo.getItemCount();
			m_alLocatorDataBackup = m_alLocatorData;
			m_alSerialDataBackup = m_alSerialData;
			if (iRowCount > 0) {
				m_alLocatorData = new ArrayList();
				m_alSerialData = new ArrayList();
				for (int i = 0; i < iRowCount; i++) {
					m_alLocatorData.add(bvo.getItemValue(i, "locator"));
					m_alSerialData.add(bvo.getItemValue(i, "serial"));
					// m_alLocatorData.add(null);
					// m_alSerialData.add(null);
				}
			} else SCMEnv.out("--->W:row is null");
			SCMEnv.showTime(lTime, "setBillVO:4");
		} catch (Exception e) {
			SCMEnv.out(e.getMessage());
		} finally {
			long lTime = System.currentTimeMillis();
			getBillCardPanel().getBillModel().addTableModelListener(this);
			getBillCardPanel().addBillEditListenerHeadTail(this);
			SCMEnv.showTime(lTime, "setBillVO:addTableModelListener");
		}
		/** 当单据的来源单据为转库单时, 进行界面控制 */
		long lTime = System.currentTimeMillis();
		ctrlSourceBillUI(bUpdateBotton);
		SCMEnv.showTime(lTime, "setBillVO:ctrlSourceBillUI");

	}

	/**
	 * 创建者：张欣 功能：根据当前表体行BarCodeVO[]是否有值,条码编辑按钮是否可用 参数： row行号 返回： 例外：
	 * 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	protected void setBtnStatusBC(int row) {
		GeneralBillVO voBill = null;
		// 设置voBill,以读取控制数据。
		if (BillMode.Browse == m_iMode) {
			if (m_iLastSelListHeadRow >= 0 && m_alListData != null && m_alListData.size() > m_iLastSelListHeadRow && m_alListData.get(m_iLastSelListHeadRow) != null) voBill = (GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow);
		} else
		// 新增、修改方式下用m_voBill
		voBill = m_voBill;

		if (voBill != null && voBill.getItemVOs() != null && voBill.getItemVOs().length > 0 && row < voBill.getItemVOs().length) {
			GeneralBillItemVO voItem = null;
			voItem = voBill.getItemVOs()[row];
			if (voItem != null && voItem.getBarcodeManagerflag().booleanValue()) {
				// 可以条码编辑
				getButtonTree().getButton(ICButtonConst.BTN_LINE_BARCODE).setEnabled(true);
				// if (m_utfBarCode!=null)
				// m_utfBarCode.setEnabled(true);
			} else {
				getButtonTree().getButton(ICButtonConst.BTN_LINE_BARCODE).setEnabled(false);
				// if (m_utfBarCode!=null)
				// m_utfBarCode.setEnabled(false);
			}

		}
		// 焦点问题，ydy 04-06-29
		updateButton(getButtonTree().getButton(ICButtonConst.BTN_LINE_BARCODE));

	}

	/**
	 * 创建者：李俊 功能：根据当前表体行BarCodeVO[],设置导入条码菜单状态 参数： row行号 返回： 例外：
	 * 
	 */
	protected void setBtnStatusImportBarcode(int row) {
		if (row < 0) return;
		GeneralBillVO voBill = null;
		if (BillMode.List == m_iCurPanel) {
			setBarcodeButtonStatus(false);
			return;// 浏览情况，非是审核条码不可以直接导入
		}
		// 设置voBill,以读取控制数据。
		if (BillMode.Browse == m_iMode) {
			if (m_iLastSelListHeadRow >= 0 && m_alListData != null && m_alListData.size() > m_iLastSelListHeadRow && m_alListData.get(m_iLastSelListHeadRow) != null) voBill = (GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow);
		} else
		// 新增、修改方式下用m_voBill
		voBill = m_voBill;

		if (voBill != null && voBill.getItemVOs() != null && voBill.getItemVOs().length > 0 && voBill.getItemVOs().length > row) {
			GeneralBillItemVO voItem = null;
			voItem = voBill.getItemVOs()[row];
			if (voItem != null && voItem.getBarcodeManagerflag().booleanValue()) {
				boolean bPri = voItem.getInv().getIsprimarybarcode().booleanValue();
				boolean bSub = voItem.getInv().getIssecondarybarcode().booleanValue();
				if (bPri == false && bSub == false) setBarcodeButtonStatus(false);
				if (bPri == true && bSub == false) {
					setBarcodeButtonStatus(true);
					getButtonTree().getButton(ICButtonConst.BTN_IMPORT_1ST_BARCODE).setEnabled(true);
					getButtonTree().getButton(ICButtonConst.BTN_IMPORT_2ND_BARCODE).setEnabled(false);
					getButtonTree().getButton(ICButtonConst.BTN_IMPORT_BOTH_BARCODE).setEnabled(false);
				}
				if (bPri == false && bSub == true) {
					setBarcodeButtonStatus(true);
					getButtonTree().getButton(ICButtonConst.BTN_IMPORT_1ST_BARCODE).setEnabled(false);
					getButtonTree().getButton(ICButtonConst.BTN_IMPORT_2ND_BARCODE).setEnabled(true);
					getButtonTree().getButton(ICButtonConst.BTN_IMPORT_BOTH_BARCODE).setEnabled(false);
				}
				if (bPri == true && bSub == true) {
					setBarcodeButtonStatus(true);
					getButtonTree().getButton(ICButtonConst.BTN_IMPORT_1ST_BARCODE).setEnabled(true);
					getButtonTree().getButton(ICButtonConst.BTN_IMPORT_2ND_BARCODE).setEnabled(true);
					getButtonTree().getButton(ICButtonConst.BTN_IMPORT_BOTH_BARCODE).setEnabled(true);
				}
			} else setBarcodeButtonStatus(false);

		} else setBarcodeButtonStatus(false);

	}

	/**
	 * 
	 * 方法功能描述：更新条码导入相关的四个按钮的状态。<br>
	 * V50中，使用了一个顶级按钮“导入条码”，作为这四个按钮的一级按钮，来达到控制这四个按钮状态的目的
	 * V51重构，所有的按钮都使用ButtonTree来进行加载，并充分利用按钮注册的功能，这四个按钮归并到“导出/导入”按钮下，因此需要单独控制其状态
	 * <p>
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @param enabled
	 *            可用状态为true，不可用状态为false
	 *            <p>
	 * @author duy
	 * @time 2007-2-5 下午02:16:46
	 */
	private void setBarcodeButtonStatus(boolean enabled) {
		getButtonTree().getButton(ICButtonConst.BTN_IMPORT_1ST_BARCODE).setEnabled(enabled);
		getButtonTree().getButton(ICButtonConst.BTN_IMPORT_2ND_BARCODE).setEnabled(enabled);
		getButtonTree().getButton(ICButtonConst.BTN_IMPORT_BOTH_BARCODE).setEnabled(enabled);
		getButtonTree().getButton(ICButtonConst.BTN_IMPORT_SOURCE_BARCODE).setEnabled(enabled);
		updateButton(getButtonTree().getButton(ICButtonConst.BTN_IMPORT_1ST_BARCODE));
		updateButton(getButtonTree().getButton(ICButtonConst.BTN_IMPORT_2ND_BARCODE));
		updateButton(getButtonTree().getButton(ICButtonConst.BTN_IMPORT_BOTH_BARCODE));
		updateButton(getButtonTree().getButton(ICButtonConst.BTN_IMPORT_SOURCE_BARCODE));
	}

	/**
	 * 创建者：王乃军 功能：根据当前单据的待审状态决定签字/取消签字那个可用 参数： 返回： 例外： 日期：(2001-5-9 9:23:32)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	protected void setBtnStatusSign(boolean bUpdateButtons) {

		// 只在浏览状态下并且界面上有单据时控制
		if (BillMode.Browse != m_iMode || m_iLastSelListHeadRow < 0 || m_iBillQty <= 0) {
			getButtonTree().getButton(ICButtonConst.BTN_SIGN).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_EXECUTE_SIGN_CANCEL).setEnabled(false);
			return;
		}

		int iSignFlag = isSigned();
		if (SIGNED == iSignFlag) {
			// 已签字，所以设置按钮状态,签字不可用，取消签字可用
			getButtonTree().getButton(ICButtonConst.BTN_SIGN).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_EXECUTE_SIGN_CANCEL).setEnabled(true);
			// 不可删、改
			getButtonTree().getButton(ICButtonConst.BTN_BILL_EDIT).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_BILL_DELETE).setEnabled(false);

		} else if (NOTSIGNED == iSignFlag) {
			// 未签字，所以设置按钮状态,签字可用，取消签字不可用
			// 判断是否已填了数量，因为数量是完整的，所以只要检查第一行就行了。

			getButtonTree().getButton(ICButtonConst.BTN_SIGN).setEnabled(true);
			getButtonTree().getButton(ICButtonConst.BTN_EXECUTE_SIGN_CANCEL).setEnabled(false);
			// 可删、改
			if (isCurrentTypeBill()) {
				getButtonTree().getButton(ICButtonConst.BTN_BILL_EDIT).setEnabled(true);
				getButtonTree().getButton(ICButtonConst.BTN_BILL_DELETE).setEnabled(true);
			} else {
				getButtonTree().getButton(ICButtonConst.BTN_BILL_EDIT).setEnabled(false);
				getButtonTree().getButton(ICButtonConst.BTN_BILL_DELETE).setEnabled(false);
			}
		} else { // 不可签字操作
			getButtonTree().getButton(ICButtonConst.BTN_SIGN).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_EXECUTE_SIGN_CANCEL).setEnabled(false);
			// 可删、改
			if (isCurrentTypeBill()) {
				getButtonTree().getButton(ICButtonConst.BTN_BILL_EDIT).setEnabled(true);
				getButtonTree().getButton(ICButtonConst.BTN_BILL_DELETE).setEnabled(true);
			} else {
				getButtonTree().getButton(ICButtonConst.BTN_BILL_EDIT).setEnabled(false);
				getButtonTree().getButton(ICButtonConst.BTN_BILL_DELETE).setEnabled(false);
			}
		}
		// 使设置生效 屏蔽 by hanwei 2003-11-17 for 效率
		if (bUpdateButtons) updateButtons();

	}

	/**
	 * 创建者：王乃军 功能：根据当前存货的属性决定序列号分配是否可用 参数： row行号 返回： 例外： 日期：(2001-5-9 9:23:32)
	 * 修改日期，修改人，修改原因，注释标志： bUpdateButtons:是否更新按纽,true: 更新, false不更新
	 * 
	 * 
	 * 
	 */
	protected void setBtnStatusSN(int row, boolean bUpdateButtons) {
		GeneralBillVO voBill = null;
		// 设置voBill,以读取控制数据。
		if (BillMode.Browse == m_iMode) {
			if (m_iLastSelListHeadRow >= 0 && m_alListData != null && m_alListData.size() > m_iLastSelListHeadRow && m_alListData.get(m_iLastSelListHeadRow) != null) voBill = (GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow);
		} else
		// 新增、修改方式下用m_voBill
		voBill = m_voBill;

		if (voBill != null) {
			InvVO voInv = null;
			voInv = voBill.getItemInv(row);
			if (voInv != null && voInv.getIsSerialMgt() != null && voInv.getIsSerialMgt().intValue() != 0)
			// 是序列号管理存货，可用
			getButtonTree().getButton(ICButtonConst.BTN_LINE_SERIAL).setEnabled(true);
			else
			// 不是序列号管理存货，不可用
			getButtonTree().getButton(ICButtonConst.BTN_LINE_SERIAL).setEnabled(false);
		} else getButtonTree().getButton(ICButtonConst.BTN_LINE_SERIAL).setEnabled(false);

		// 调用条码按钮状态控制方法by hanwei 2003-11-18
		setBtnStatusBC(row);

		// 调用导入条码按钮状态控制方法 add by ljun
		setBtnStatusImportBarcode(row);

		// 使设置生效 屏蔽 by hanwei 2003-11-17 for 效率
		if (bUpdateButtons)
		// updateButtons();
		// 焦点问题，ydy 04-06-29
		updateButton(getButtonTree().getButton(ICButtonConst.BTN_LINE_SERIAL));

	}

	/**
	 * 创建者：王乃军 功能：根据当前仓库的状态决定货位分配是否可用 参数： 返回： 例外： 日期：(2001-5-9 9:23:32)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	protected void setBtnStatusSpace(boolean bUpdateButtons) {
		GeneralBillVO voBill = null;
		// 设置voBill,以读取控制数据。
		if (BillMode.Browse == m_iMode) {
			if (m_iLastSelListHeadRow >= 0 && m_alListData != null && m_alListData.size() > m_iLastSelListHeadRow && m_alListData.get(m_iLastSelListHeadRow) != null) voBill = (GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow);
		} else
		// 新增、修改方式下用m_voBill
		voBill = m_voBill;

		// 缺省不是货位管理仓库，不可用
		getButtonTree().getButton(ICButtonConst.BTN_LINE_SPACE).setEnabled(false);
		// m_boSelectLocator.setEnabled(false);

		if (voBill != null) {
			WhVO voWh = null;
			voWh = voBill.getWh();
			// 是货位管理仓库，可用
			if (voWh != null && voWh.getIsLocatorMgt() != null && voWh.getIsLocatorMgt().intValue() != 0) {
				getButtonTree().getButton(ICButtonConst.BTN_LINE_SPACE).setEnabled(true);
			}

			// ###########################
			// 设置 add by hanwei 2004-05-14
			// 1、库存出入库单如果有直接调转标志，需要在界面控制 修改、删除、复制等按钮不可用；
			// 2、如果单据的仓库是直运仓，应该控制修改、删除、复制等按钮不可用；
			setBtnStatusTranflag();
			// #############################
		}

		// 使设置生效 by hanwei 2003-11-17 for 效率
		if (bUpdateButtons) updateButton(getButtonTree().getButton(ICButtonConst.BTN_LINE_SPACE));

	}

	/**
	 * 创建者：韩卫 功能： bd_stordoc 是否直运仓库：isdirectstore =Y 或 ic_general_h
	 * 是否直接调拨：bdirecttranflag =Y 1、库存出入库单如果有直接调转标志，需要在界面控制 修改、删除、复制等按钮不可用；
	 * 2、仓库有一个属性，是否直运仓库，需要加在 WHVO中。如果单据的仓库是直运仓，应该控制修改、删除、复制等按钮不可用；
	 * 
	 * 参数： 返回： 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 */
	protected void setBtnStatusTranflag() {
		setBtnStatusTranflag(false);
	}

	/**
	 * 创建者：韩卫 功能： bd_stordoc 是否直运仓库：isdirectstore =Y 或 ic_general_h
	 * 是否直接调拨：bdirecttranflag =Y 1、库存出入库单如果有直接调转标志，需要在界面控制 修改、删除、复制等按钮不可用；
	 * 2、仓库有一个属性，是否直运仓库，需要加在 WHVO中。如果单据的仓库是直运仓，应该控制修改、删除、复制等按钮不可用；
	 * 
	 * 参数： 返回： 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * bOnlyFalse:只设置setEnabled为false的情况的属性
	 * 
	 */
	protected void setBtnStatusTranflag(boolean bOnlyFalse) {

		GeneralBillVO voBill = null;

		// 设置voBill,以读取控制数据。
		if (BillMode.Browse == m_iMode) {
			if (m_iLastSelListHeadRow >= 0 && m_alListData != null && m_alListData.size() > m_iLastSelListHeadRow && m_alListData.get(m_iLastSelListHeadRow) != null) voBill = (GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow);
		} else return;

		boolean bUseable = true;
		// 直运
		if (voBill != null) {
			WhVO voWh = null;
			voWh = voBill.getWh();
			if (voWh != null && voWh.getIsdirectstore() != null && voWh.getIsdirectstore().booleanValue()) {
				bUseable = false;
			}
			if (voBill.getHeaderVO() != null && voBill.getHeaderVO().getBdirecttranflag() != null && voBill.getHeaderVO().getBdirecttranflag().booleanValue()) {
				bUseable = false;
			}

			if (bOnlyFalse) {
				if (!bUseable) {
					getButtonTree().getButton(ICButtonConst.BTN_BILL_COPY).setEnabled(false);
					getButtonTree().getButton(ICButtonConst.BTN_BILL_DELETE).setEnabled(false);
					getButtonTree().getButton(ICButtonConst.BTN_BILL_EDIT).setEnabled(false);
				}
			} else {
				if (m_iMode != BillMode.New) {
					getButtonTree().getButton(ICButtonConst.BTN_BILL_COPY).setEnabled(bUseable);
					getButtonTree().getButton(ICButtonConst.BTN_BILL_DELETE).setEnabled(bUseable);
					getButtonTree().getButton(ICButtonConst.BTN_BILL_EDIT).setEnabled(bUseable);
				}
			}

		}

	}

	/**
	 * 创建者：王乃军 功能：设置按钮状态。 参数： 返回： 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */

	protected void setButtonStatus(boolean bUpdateButtons) {

		long lTime = System.currentTimeMillis();
		switch (m_iMode) {
		case BillMode.New:
			getButtonTree().getButton(ICButtonConst.BTN_ADD).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_ASSIST_FUNC_PICKUP_AUTO).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_BUSINESS_TYPE).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_BILL_COPY).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_BILL_EDIT).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_BILL_DELETE).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_SAVE).setEnabled(true);
			getButtonTree().getButton(ICButtonConst.BTN_BILL_CANCEL).setEnabled(true);
			getButtonTree().getButton(ICButtonConst.BTN_QUERY).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_BROWSE_REFRESH).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_BROWSE_LOCATE).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_SWITCH).setEnabled(false);

			getButtonTree().getButton(ICButtonConst.BTN_ASSIST_FUNC).setEnabled(true);
			getButtonTree().getButton(ICButtonConst.BTN_ASSIST_QUERY).setEnabled(true);
			getButtonTree().getButton(ICButtonConst.BTN_EXPORT_IMPORT).setEnabled(true);
			getButtonTree().getButton(ICButtonConst.BTN_BROWSE).setEnabled(true);

			getButtonTree().getButton(ICButtonConst.BTN_LINE_ADD).setEnabled(true);
			getButtonTree().getButton(ICButtonConst.BTN_LINE_DELETE).setEnabled(true);
			getButtonTree().getButton(ICButtonConst.BTN_LINE_COPY).setEnabled(true);
			getButtonTree().getButton(ICButtonConst.BTN_LINE_PASTE).setEnabled(true);
			getButtonTree().getButton(ICButtonConst.BTN_LINE_INSERT).setEnabled(true);

			// getButtonTree().getButton(ICButtonConst.BTN_LINE_SPACE).setEnabled(true);
			// getButtonTree().getButton(ICButtonConst.BTN_LINE_SERIAL).setEnabled(true);

			getButtonTree().getButton(ICButtonConst.BTN_SIGN).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_EXECUTE_SIGN_CANCEL).setEnabled(false);

			getButtonTree().getButton(ICButtonConst.BTN_LINE).setEnabled(true);

			getButtonTree().getButton(ICButtonConst.BTN_PRINT).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_PRINT_PRINT).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_PRINT_PREVIEW).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_PRINT_SPACE).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_PRINT_SUM).setEnabled(false);

			// 外设输入支持
			getButtonTree().getButton(ICButtonConst.BTN_ASSIST_FUNC_SCAN).setEnabled(true);
			getButtonTree().getButton(ICButtonConst.BTN_IMPORT_BILL).setEnabled(true);
			getButtonTree().getButton(ICButtonConst.BTN_ASSIST_FUNC_INV_CHECK).setEnabled(true);
			getButtonTree().getButton(ICButtonConst.BTN_ASSIST_FUNC_DOCUMENT).setEnabled(false);
			// 控制翻页按钮的状态：
			m_pageBtn.setPageBtnVisible(false);
			getButtonTree().getButton(ICButtonConst.BTN_LINE_AUTO_FILL).setEnabled(true);
			getButtonTree().getButton(ICButtonConst.BTN_ASSIST_FUNC_PICKUP_AUTO).setEnabled(true);

			// 在新增情况下和修改情况条码框可以编辑
			if (m_utfBarCode != null) m_utfBarCode.setEnabled(true);

			// 导入条码
			if (BillMode.List == m_iCurPanel) {
				setBarcodeButtonStatus(false);
			} else {
				setBtnStatusImportBarcode(0);
			}
			if (getButtonTree().getButton(ICButtonConst.BTN_IMPORT_SOURCE_BARCODE) != null) {
				getButtonTree().getButton(ICButtonConst.BTN_IMPORT_SOURCE_BARCODE).setEnabled(true);
			}
			break;
		case BillMode.Update:
			getButtonTree().getButton(ICButtonConst.BTN_ADD).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_ASSIST_FUNC_PICKUP_AUTO).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_BUSINESS_TYPE).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_BILL_COPY).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_BILL_EDIT).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_BILL_DELETE).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_SAVE).setEnabled(true);
			getButtonTree().getButton(ICButtonConst.BTN_BILL_CANCEL).setEnabled(true);
			getButtonTree().getButton(ICButtonConst.BTN_QUERY).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_BROWSE_REFRESH).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_BROWSE_LOCATE).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_SWITCH).setEnabled(false);

			getButtonTree().getButton(ICButtonConst.BTN_ASSIST_FUNC).setEnabled(true);
			getButtonTree().getButton(ICButtonConst.BTN_ASSIST_QUERY).setEnabled(true);
			getButtonTree().getButton(ICButtonConst.BTN_EXPORT_IMPORT).setEnabled(true);
			getButtonTree().getButton(ICButtonConst.BTN_BROWSE).setEnabled(true);

			getButtonTree().getButton(ICButtonConst.BTN_LINE_ADD).setEnabled(true);
			getButtonTree().getButton(ICButtonConst.BTN_LINE_DELETE).setEnabled(true);
			getButtonTree().getButton(ICButtonConst.BTN_LINE_COPY).setEnabled(true);
			getButtonTree().getButton(ICButtonConst.BTN_LINE_PASTE).setEnabled(true);
			getButtonTree().getButton(ICButtonConst.BTN_LINE_INSERT).setEnabled(true);

			// getButtonTree().getButton(ICButtonConst.BTN_LINE_SPACE).setEnabled(true);
			// getButtonTree().getButton(ICButtonConst.BTN_LINE_SERIAL).setEnabled(true);

			getButtonTree().getButton(ICButtonConst.BTN_SIGN).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_EXECUTE_SIGN_CANCEL).setEnabled(false);

			getButtonTree().getButton(ICButtonConst.BTN_LINE).setEnabled(true);

			getButtonTree().getButton(ICButtonConst.BTN_PRINT).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_PRINT_PRINT).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_PRINT_PREVIEW).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_PRINT_SPACE).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_PRINT_SUM).setEnabled(false);

			// 外设输入支持
			getButtonTree().getButton(ICButtonConst.BTN_ASSIST_FUNC_SCAN).setEnabled(true);
			getButtonTree().getButton(ICButtonConst.BTN_IMPORT_BILL).setEnabled(true);
			getButtonTree().getButton(ICButtonConst.BTN_ASSIST_FUNC_INV_CHECK).setEnabled(true);
			getButtonTree().getButton(ICButtonConst.BTN_ASSIST_FUNC_DOCUMENT).setEnabled(false);
			// 控制翻页按钮的状态：
			m_pageBtn.setPageBtnVisible(false);
			getButtonTree().getButton(ICButtonConst.BTN_LINE_AUTO_FILL).setEnabled(true);
			getButtonTree().getButton(ICButtonConst.BTN_ASSIST_FUNC_PICKUP_AUTO).setEnabled(true);

			// 在新增情况下和修改情况条码框可以编辑
			if (m_utfBarCode != null) m_utfBarCode.setEnabled(true);

			if (getButtonTree().getButton(ICButtonConst.BTN_IMPORT_SOURCE_BARCODE) != null && BillMode.Card == m_iCurPanel && m_voBill != null && m_voBill.getHeaderVO() != null) {
				String sBillTypecode = m_voBill.getHeaderVO().getCbilltypecode();
				if (sBillTypecode != null && (sBillTypecode.equalsIgnoreCase("4A") || sBillTypecode.equalsIgnoreCase("4I") || sBillTypecode.equalsIgnoreCase("4Y") || sBillTypecode.equalsIgnoreCase("4E"))) {
					// 符合规则：其他出入库单，调拨出入库单，可以导入单据条码
					// 卡片状态，单据类型，
					getButtonTree().getButton(ICButtonConst.BTN_IMPORT_SOURCE_BARCODE).setEnabled(true);
				}

			}

			if (BillMode.List == m_iCurPanel) {
				setBarcodeButtonStatus(false);
			} else {
				setBtnStatusImportBarcode(0);
			}
			break;
		case BillMode.Browse: // 在浏览情况下条码框不可以编辑
			if (m_utfBarCode != null) m_utfBarCode.setEnabled(false);
			// 在列表情况下，条码按纽
			if (BillMode.List == m_iCurPanel) {
				getButtonTree().getButton(ICButtonConst.BTN_LINE_BARCODE).setEnabled(false);
			} else {
				setBtnStatusBC(0);
				// add by ljun
				setBtnStatusImportBarcode(0);
			}

			getButtonTree().getButton(ICButtonConst.BTN_ADD).setEnabled(true);
			getButtonTree().getButton(ICButtonConst.BTN_ASSIST_FUNC_PICKUP_AUTO).setEnabled(true);
			getButtonTree().getButton(ICButtonConst.BTN_BUSINESS_TYPE).setEnabled(true);
			// 有单据
			if (m_iBillQty > 0 && m_iLastSelListHeadRow >= 0) {
				getButtonTree().getButton(ICButtonConst.BTN_BILL_COPY).setEnabled(true);
				getButtonTree().getButton(ICButtonConst.BTN_BROWSE_LOCATE).setEnabled(true);

				getButtonTree().getButton(ICButtonConst.BTN_ASSIST_FUNC).setEnabled(true);
				getButtonTree().getButton(ICButtonConst.BTN_ASSIST_QUERY).setEnabled(true);
				getButtonTree().getButton(ICButtonConst.BTN_EXPORT_IMPORT).setEnabled(true);
				getButtonTree().getButton(ICButtonConst.BTN_BROWSE).setEnabled(true);
				// 如果不是和本节点相同的单据类型(如借入单上查出的期初单)，不能删除.
				try {
					// 当前选中的单据
					GeneralBillVO voBill = null;
					if (m_iCurPanel == BillMode.List) voBill = (GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow);
					else voBill = m_voBill;
					if (m_sBillTypeCode.equals(voBill.getHeaderVO().getCbilltypecode())) {
						getButtonTree().getButton(ICButtonConst.BTN_BILL_EDIT).setEnabled(true);
						getButtonTree().getButton(ICButtonConst.BTN_BILL_DELETE).setEnabled(true);
					}
				} catch (Exception e) {
					SCMEnv.out(e.getMessage());
				}
			} else {
				getButtonTree().getButton(ICButtonConst.BTN_BILL_COPY).setEnabled(false);
				getButtonTree().getButton(ICButtonConst.BTN_BILL_EDIT).setEnabled(false);
				getButtonTree().getButton(ICButtonConst.BTN_BILL_DELETE).setEnabled(false);
				getButtonTree().getButton(ICButtonConst.BTN_BROWSE_LOCATE).setEnabled(false);
			}

			getButtonTree().getButton(ICButtonConst.BTN_SAVE).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_BILL_CANCEL).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_QUERY).setEnabled(true);

			if (m_bEverQry) getButtonTree().getButton(ICButtonConst.BTN_BROWSE_REFRESH).setEnabled(true);
			else getButtonTree().getButton(ICButtonConst.BTN_BROWSE_REFRESH).setEnabled(false);

			getButtonTree().getButton(ICButtonConst.BTN_SWITCH).setEnabled(true);
			getButtonTree().getButton(ICButtonConst.BTN_LINE_ADD).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_LINE_DELETE).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_LINE_COPY).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_LINE_PASTE).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_LINE_INSERT).setEnabled(false);
			// 有单据可以打印。
			if (m_iBillQty > 0) {
				getButtonTree().getButton(ICButtonConst.BTN_PRINT).setEnabled(true);
				getButtonTree().getButton(ICButtonConst.BTN_PRINT_PRINT).setEnabled(true);
				getButtonTree().getButton(ICButtonConst.BTN_PRINT_PREVIEW).setEnabled(true);
				getButtonTree().getButton(ICButtonConst.BTN_PRINT_SPACE).setEnabled(true);
				getButtonTree().getButton(ICButtonConst.BTN_PRINT_SUM).setEnabled(true);
			} else {
				getButtonTree().getButton(ICButtonConst.BTN_PRINT).setEnabled(false);
				getButtonTree().getButton(ICButtonConst.BTN_PRINT_PRINT).setEnabled(false);
				getButtonTree().getButton(ICButtonConst.BTN_PRINT_PREVIEW).setEnabled(false);
				getButtonTree().getButton(ICButtonConst.BTN_PRINT_SPACE).setEnabled(false);
				getButtonTree().getButton(ICButtonConst.BTN_PRINT_SUM).setEnabled(false);
			} // 还应进一步判断当前的单据是否已签字
				// 同时判断修改、删除是否可用，所以应放在它们的后面。
			getButtonTree().getButton(ICButtonConst.BTN_LINE).setEnabled(false);
			// 外设输入支持
			getButtonTree().getButton(ICButtonConst.BTN_ASSIST_FUNC_SCAN).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_IMPORT_BILL).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_ASSIST_FUNC_INV_CHECK).setEnabled(true);
			getButtonTree().getButton(ICButtonConst.BTN_ASSIST_FUNC_DOCUMENT).setEnabled(true);
			// 导入条码
			if (getButtonTree().getButton(ICButtonConst.BTN_IMPORT_SOURCE_BARCODE) != null) getButtonTree().getButton(ICButtonConst.BTN_IMPORT_SOURCE_BARCODE).setEnabled(true);
			// 控制翻页按钮的状态：
			if (m_iCurPanel == BillMode.List) {
				m_pageBtn.setPageBtnVisible(false);
			} else {
				m_pageBtn.setPageBtnVisible(true);
				m_pageBtn.setPageBtnStatus(m_iBillQty, m_iLastSelListHeadRow);
			}
			getButtonTree().getButton(ICButtonConst.BTN_LINE_AUTO_FILL).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_ASSIST_FUNC_PICKUP_AUTO).setEnabled(false);

			if (BillMode.List == m_iCurPanel) {
				getButtonTree().getButton(ICButtonConst.BTN_ASSIST_FUNC_PICKUP_AUTO).setEnabled(false);
				getButtonTree().getButton(ICButtonConst.BTN_LINE_SPACE).setEnabled(false);
				getButtonTree().getButton(ICButtonConst.BTN_LINE_SERIAL).setEnabled(false);
				getButtonTree().getButton(ICButtonConst.BTN_LINE_BARCODE).setEnabled(false);
			}

			break;
		}
		SCMEnv.showTime(lTime, "setEnable:");
		// 当前选中行
		lTime = System.currentTimeMillis();
		int rownow = getBillCardPanel().getBillTable().getSelectedRow();
		// 判断是否需要序列号分配,设置状态
		if (rownow >= 0) setBtnStatusSN(rownow, false);
		else getButtonTree().getButton(ICButtonConst.BTN_LINE_SERIAL).setEnabled(false);
		// 抽象方法调用
		SCMEnv.showTime(lTime, "setBtnStatusSN)");
		lTime = System.currentTimeMillis();
		setButtonsStatus(m_iMode);
		setExtendBtnsStat(m_iMode);
		SCMEnv.showTime(lTime, "setButtonsStatus(m_iMode)");
		// 重新设置货位分配是否可用
		lTime = System.currentTimeMillis();
		setBtnStatusSpace(false);
		SCMEnv.showTime(lTime, "setBtnStatusSpace();:");
		setBtnStatusSign(false);
		// 根据来源单据控制按钮
		lTime = System.currentTimeMillis();
		ctrlSourceBillButtons(false);
		SCMEnv.showTime(lTime, "根据来源单据控制按钮");

		// 根据单据类型控制按钮
		lTime = System.currentTimeMillis();
		ctrlBillTypeButtons(true);
		SCMEnv.showTime(lTime, "根据单据类型控制按钮");

		// 合并显示
		if (m_iCurPanel == BillMode.List) {
			getButtonTree().getButton(ICButtonConst.BTN_PRINT_DISTINCT).setEnabled(false);
		} else {
			if (m_iMode == BillMode.Browse) getButtonTree().getButton(ICButtonConst.BTN_PRINT_DISTINCT).setEnabled(true);
			else getButtonTree().getButton(ICButtonConst.BTN_PRINT_DISTINCT).setEnabled(false);
		}

		// 列表状态下，存量显示/隐藏的按钮不可用
		if (BillMode.Card == m_iCurPanel) {
			getButtonTree().getButton(ICButtonConst.BTN_ASSIST_FUNC_ONHAND).setEnabled(true);
		} else if (BillMode.List == m_iCurPanel) {
			getButtonTree().getButton(ICButtonConst.BTN_ASSIST_FUNC_ONHAND).setEnabled(false);
		}

		// v5 lj
		if (m_bRefBills == true) {
			setRefBtnStatus();
		}

		// 使设置生效
		lTime = System.currentTimeMillis();
		if (bUpdateButtons) updateButtons();
		SCMEnv.showTime(lTime, "updateButtons();");

	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-4-8 11:13:07)
	 * 
	 * @param newGenBillUICtl
	 *            nc.ui.ic.pub.bill.GeneralBillUICtl
	 */
	public void setGenBillUICtl(GeneralBillUICtl newGenBillUICtl) {
		m_GenBillUICtl = newGenBillUICtl;
	}

	/**
	 * 创建者：王乃军 功能：在表单设置显示VO,不更新界面状态updateValue() 参数： 返回： 例外： 日期：(2001-5-9
	 * 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	public void setImportBillVO(GeneralBillVO bvo, boolean bExeFormula) throws Exception {
		if (bvo == null) throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000061")/* @res "传入的单据为空！" */);
		// 表体行数
		int iRowCount = bvo.getItemCount();

		try {
			getBillCardPanel().getBillModel().removeTableModelListener(this);
			getBillCardPanel().removeBillEditListenerHeadTail();
			// 保存一个clone()
			m_voBill = (GeneralBillVO) bvo.clone();
			// 其他仓库的公司非登陆公司

			// 调拨出入库单的两个参照特殊处理
			// 不应放在基类方法中。
			// shawbing on Jun 13, 2005
			if (m_sBillTypeCode.equals("4Y") || m_sBillTypeCode.equals("4E")) setBillRefIn4Eand4Y(m_voBill);

			// 新增的单据清除PK
			m_voBill.getHeaderVO().setPrimaryKey(null);
			GeneralBillItemVO[] voaItem = m_voBill.getItemVOs();
			for (int row = 0; row < iRowCount; row++) {
				voaItem[row].setPrimaryKey(null);
				voaItem[row].calculateMny();
			}
			// 设置数据
			getBillCardPanel().setBillValueVO(m_voBill);
			// 执行公式
			if (bExeFormula) {
				getBillCardPanel().getBillModel().execLoadFormula();
				execHeadTailFormulas();
			}

			// 更新状态 ---delete it to support CANCEL
			// getBillCardPanel().updateValue();
			// 清存货现存量数据
			bvo.clearInvQtyInfo();
			// 有表体行，选中第一行
			if (iRowCount > 0) {
				// 选中第一行
				getBillCardPanel().getBillTable().setRowSelectionInterval(0, 0);
				// 重置序列号是否可用
				setBtnStatusSN(0, false);

				// 刷新现存量显示
				// setTailValue(0);
				// 重置其它数据
				nc.ui.pub.bill.BillModel bmTemp = getBillCardPanel().getBillModel();
				m_alLocatorDataBackup = m_alLocatorData;
				m_alSerialDataBackup = m_alSerialData;
				m_alLocatorData = new ArrayList();
				m_alSerialData = new ArrayList();

				for (int row = 0; row < iRowCount; row++) {
					// 设置行状态为新增
					if (bmTemp != null) bmTemp.setRowState(row, nc.ui.pub.bill.BillModel.ADD);
					m_alLocatorData.add(null);
					m_alSerialData.add(null);
				}
			}
		} catch (Exception e) {
			SCMEnv.out(e.getMessage());
		} finally {
			getBillCardPanel().getBillModel().addTableModelListener(this);
			getBillCardPanel().addBillEditListenerHeadTail(this);
		}
	}

	/**
	 * ?user> 功能： 参数： 返回： 例外： 日期：(2005-1-11 18:53:03) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @param voLot
	 *            nc.vo.ic.pub.lot.LotNumbRefVO
	 * @param irow
	 *            int
	 */
	// update by zip:2013/11/28:
	public void synlot(nc.vo.ic.pub.lot.LotNumbRefVO voLot, int irow) {

		InvVO voInv = m_voBill.getItemInv(irow);
		if (voLot == null) {
			clearRowData(m_iBillInOutFlag, irow, "vbatchcode");

		}

		// 辅单位
		if (voInv.getIsAstUOMmgt() != null && voInv.getIsAstUOMmgt().intValue() == 1) {

			String oldvalue = (String) getBillCardPanel().getBodyValueAt(irow, "castunitid");// refCastunit.getRefPK();
			m_voBill.setItemValue(irow, "cselastunitid", voLot.getCastunitid());
			nc.vo.ic.pub.DesassemblyVO voDesa = (nc.vo.ic.pub.DesassemblyVO) m_voBill.getItemValue(irow, "desainfo");

			if (voDesa.getMeasInfo() == null) {
				nc.vo.bd.b15.MeasureRateVO[] voMeasInfo = m_voInvMeas.getMeasInfo(m_sCorpID, voInv.getCinventoryid());
				voDesa.setMeasInfo(voMeasInfo);
			}
			m_voBill.setItemValue(irow, "idesatype", new Integer(voDesa.getDesaType()));
			getBillCardPanel().setBodyValueAt(new Integer(voDesa.getDesaType()), irow, "idesatype");
			getBillCardPanel().setBodyValueAt(voLot.getCastunitid(), irow, "cselastunitid");
			// 如果不是拆解，那么执行以前的代码。
			if (voDesa.getDesaType() == nc.vo.ic.pub.DesassemblyVO.TYPE_NO && !voLot.getCastunitid().equals(oldvalue)) {
				nc.ui.pub.beans.UIRefPane refCastunit = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getBodyItem("castunitname").getComponent());

				filterMeas(irow);
				refCastunit.setPK(voLot.getCastunitid());
				refCastunit.setName(voLot.getCastunitname());

				Object source = getBillCardPanel().getBodyItem("castunitname");
				Object newvalue = voLot.getCastunitid();
				nc.ui.pub.bill.BillEditEvent even = new nc.ui.pub.bill.BillEditEvent(source, newvalue, oldvalue, "castunitname", irow, BillItem.BODY);
				getBillCardPanel().setBodyValueAt(voLot.getCastunitname(), irow, "castunitname");
				afterAstUOMEdit(even);
			}
		}
		getBillCardPanel().setBodyValueAt(voLot.getVbatchcode(), irow, "vbatchcode");
		getBillCardPanel().setBodyValueAt(voLot.getDvalidate(), irow, "dvalidate");
		if (voLot.getOnhandnumType() != null && voLot.getOnhandnumType().intValue() == 0) {
			getBillCardPanel().setBodyValueAt(new UFBoolean("N"), irow, "bonroadflag");
			m_voBill.setItemValue(irow, "bonroadflag", new UFBoolean("N"));
		} else {
			getBillCardPanel().setBodyValueAt(new UFBoolean("Y"), irow, "bonroadflag");
			m_voBill.setItemValue(irow, "bonroadflag", new UFBoolean("Y"));
		}

		if (voInv.getIsValidateMgt().intValue() == 1) {
			nc.vo.pub.lang.UFDate dvalidate = voLot.getDvalidate();
			if (dvalidate != null) {
				getBillCardPanel().setBodyValueAt(dvalidate.getDateBefore(voInv.getQualityDay().intValue()).toString(), irow, "scrq");
			}
		}

		// 自由项
		if (voInv.getIsFreeItemMgt() != null && voInv.getIsFreeItemMgt().intValue() == 1) {
			FreeVO freevo = voLot.getFreeVO();
			if (freevo != null && freevo.getVfree0() != null) {
				InvVO invvo = m_voBill.getItemInv(irow);
				if (invvo != null) invvo.setFreeItemVO(freevo);
				getFreeItemRefPane().setFreeItemParam(invvo);
				getBillCardPanel().setBodyValueAt(freevo.getVfree0(), irow, "vfree0");
				for (int i = 1; i <= FreeVO.FREE_ITEM_NUM; i++) {
					if (getBillCardPanel().getBodyItem("vfree" + i) != null){
						getBillCardPanel().setBodyValueAt(freevo.getAttributeValue("vfree" + i), irow, "vfree" + i);
						if (i==1) {
							afterEditVfree1(freevo.getAttributeValue("vfree" + i), irow);//add by shikun 货位调整批次号批选，有垛号处理。
						}
					} else {
						getBillCardPanel().setBodyValueAt(null, irow, "vfree" + i);
					}
				}
			}
			m_voBill.setItemFreeVO(irow, freevo);
		}
		// 同步改变m_voBill
		m_voBill.setItemValue(irow, "vbatchcode", voLot.getVbatchcode());
		m_voBill.setItemValue(irow, "dvalidate", voLot.getDvalidate());

	}

	/**
	 * 货位调整批次号批选，有垛号处理。
	 * @author shikun
	 * @time 2014-12-06 
	 * */
	public void afterEditVfree1(Object attributeValue, int irow) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 创建者：余大英
	 * 
	 * 功能：设置行的货位信息
	 * 
	 * 参数：e单据编辑事件 返回： 例外： 日期：(2001-5-8 19:08:05) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	private void setRowSpaceData(int row, String cspaceid, String csname) {
		int rowcount = getBillCardPanel().getRowCount();
		if (rowcount < 0 || row >= rowcount) {
			SCMEnv.out("row e..");
			return;
		}
		getBillCardPanel().setBodyValueAt(csname, row, "vspacename");
		getBillCardPanel().setBodyValueAt(cspaceid, row, "cspaceid");

		// 如果分配货位，构造LocatorVO
		if (cspaceid != null && cspaceid.trim().length() > 0) {
			// resetSpace(row);

			LocatorVO voSpace = new LocatorVO();
			LocatorVO[] lvos = new LocatorVO[1];
			lvos[0] = voSpace;
			voSpace.setCspaceid(cspaceid);
			voSpace.setVspacename(csname);
			m_alLocatorData.remove(row);
			m_alLocatorData.add(row, lvos);

			resetSpace(row);
			/*
			 * if (getBillCardPanel().getBodyValueAt(row, m_sNumItemKey) != null
			 * && getBillCardPanel() .getBodyValueAt(row, m_sNumItemKey)
			 * .toString() .trim() .length() > 0)
			 * voSpace.setNinspacenum((UFDouble
			 * )getBillCardPanel().getBodyValueAt(row, m_sNumItemKey));
			 * 
			 * if (getBillCardPanel().getBodyValueAt(row, m_sAstItemKey) != null
			 * && getBillCardPanel() .getBodyValueAt(row, m_sAstItemKey)
			 * .toString() .trim() .length() > 0)
			 * voSpace.setNinspaceassistnum((UFDouble)
			 * 
			 * getBillCardPanel().getBodyValueAt(row, m_sAstItemKey));
			 */

			// 修改序列号数据
			if (m_alSerialData != null) {
				SerialVO[] snvos = (SerialVO[]) m_alSerialData.get(row);

				if (snvos != null) {
					for (int i = 0; i < snvos.length; i++) {
						snvos[i].setAttributeValue("cspaceid", cspaceid);
						snvos[i].setAttributeValue("vspacename", csname);
					}
				}
			}

		} else {
			m_alLocatorData.remove(row);
			m_alLocatorData.add(row, null);
			m_alSerialData.set(row, null);
		}

	}

	/**
	 * 创建者：王乃军 功能：设置表体/表尾的小数位数 参数： 返回： 例外： 日期：(2001-11-23 18:11:18)
	 * 修改日期，修改人，修改原因，注释标志：
	 */
	protected void setScaleOfCardPanel(nc.ui.pub.bill.BillCardPanel bill) {

		// 精度

		nc.ui.ic.pub.scale.ScaleInit si = new nc.ui.ic.pub.scale.ScaleInit(m_sUserID, m_sCorpID, m_ScaleValue);

		try {
			si.setScale(bill, m_ScaleKey);
		} catch (Exception e) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000060")/* @res "精度设置失败：" */
					+ e.getMessage());
		}

	}

	/**
	 * 创建者：王乃军 功能：重载的显示提示信息对话框功能 参数： 返回： 例外： 日期：(2001-5-9 9:23:32)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	public void showErrorMessage(String sMsg, boolean bWarnSound) {
		// 提示声音
		if (bWarnSound) java.awt.Toolkit.getDefaultToolkit().beep();

		showErrorMessage(sMsg);

	}

	/**
	 * 
	 * 功能： 导入文件后同步状态vo. 参数： 返回： 例外： 日期：(2002-04-18 10:43:46) 修改日期，修改人，修改原因，注释标志：
	 */
	public void synVO(nc.vo.pub.CircularlyAccessibleValueObject[] voaDi, String sWarehouseid, ArrayList m_alLocatorData) throws Exception {
		// 同步vo.
		if (m_voBill != null) {
			GeneralBillItemVO voaItem[] = m_voBill.getItemVOs();
			int start = getDevInputCtrl().getStartItem();
			if (start >= 0 && voaDi.length > 0 && voaItem.length >= (start + voaDi.length)) {
				for (int line = 0; line < voaDi.length; line++) {
					voaItem[start + line] = (GeneralBillItemVO) voaDi[line];
				}
			} else {
				SCMEnv.out("date error.");
			}
			// 处理货位 add by hanwei 2003-12-17
			nc.ui.ic.pub.bill.GeneralBillUICtl.setLocatorVO(voaItem, sWarehouseid, m_alLocatorData);
		}
	}

	/**
	 * 创建者：仲瑞庆 功能：粘贴行 参数： 返回： 例外： 日期：(2001-6-26 下午 9:32) 修改日期，修改人，修改原因，注释标志：
	 */
	protected void voBillPastLine(int row) {
		// 要求在已经增完行后执行

		for (int i = 0; i < m_voaBillItem.length; i++) {
			m_voBill.getChildrenVO()[row + i] = (GeneralBillItemVO) m_voaBillItem[i].clone();
			m_voBill.getChildrenVO()[row + i].setAttributeValue("crowno", getBillCardPanel().getBodyValueAt(row + i, "crowno"));
			m_voBill.getItemVOs()[row + i].setCgeneralbid(null);
			m_voBill.getItemVOs()[row + i].setCgeneralbb3(null);
			m_voBill.getItemVOs()[row + i].setAttributeValue("ncorrespondnum", null);
			m_voBill.getItemVOs()[row + i].setAttributeValue("ncorrespondastnum", null);
			m_alLocatorData.set(row + i, ((GeneralBillItemVO) (m_voBill.getChildrenVO()[row + i])).getLocator());
			getBillCardPanel().getBillModel().setValueAt(m_voBill.getItemVOs()[row + i].getInv(), row + i, "invvo");
			getBillCardPanel().getBillModel().setValueAt(UFDZERO, row + i, IItemKey.NBARCODENUM);
			getBillCardPanel().getBillModel().setValueAt(UFBoolean.FALSE, row + i, "bbarcodeclose");
			getBillCardPanel().getBillModel().setValueAt(null, row + i, IItemKey.NKDNUM);

			getBillCardPanel().getBillModel().setValueAt(null, row + i, "cgeneralbid");
			getBillCardPanel().getBillModel().setValueAt(null, row + i, "ncorrespondnum");
			getBillCardPanel().getBillModel().setValueAt(null, row + i, "ncorrespondastnum");

			// //粘贴行的赠品可以编辑
			// if (getBillCardPanel().getBodyItem("flargess")!=null)
			// {
			// getBillCardPanel().getBodyItem("flargess").setEnabled(true);
			// getBillCardPanel().getBodyItem("flargess").setEdit(true);
			// }
		}
		// 设置是否可序列号分配
		//
		// voBillPastLineSetAttribe(row,m_voBill);
		setBtnStatusSN(row, true);
	}

	/**
	 * 创建者：仲瑞庆 功能：粘贴行 参数： 返回： 例外： 日期：(2001-6-26 下午 9:32) 修改日期，修改人，修改原因，注释标志：
	 */
	protected void voBillPastTailLine() {
		// 要求在已经增完行后执行
		if (m_voaBillItem != null) {
			int row = getBillCardPanel().getBillTable().getRowCount() - m_voaBillItem.length;
			voBillPastLine(row);
		}

	}

	/**
	 * hw 功能：获得普通的业务日志VO 参数： 返回： 例外： 日期：(2004-6-8 20:42:43) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return nc.vo.sm.log.OperatelogVO
	 */
	public nc.vo.sm.log.OperatelogVO getNormalOperateLog() {
		nc.ui.pub.ClientEnvironment ce = getClientEnvironment();
		nc.vo.sm.log.OperatelogVO log = new nc.vo.sm.log.OperatelogVO();
		log.setCompanyname(ce.getCorporation().getUnitname());
		if (!nc.ui.pub.ClientEnvironment.getInstance().isInDebug()) log.setEnterip(nc.ui.sm.cmenu.Desktop.getApplet().getParameter("USER_IP"));
		log.setPKCorp(m_sCorpID);

		return log;
	}

	/**
	 * 功能：过滤出实发数量非空非零的表体行，打印用。 参数： 返回： 例外： 日期：(2005-2-21 21:31:48)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return nc.vo.ic.pub.bill.GeneralBillVO
	 * @param voBill
	 *            nc.vo.ic.pub.bill.GeneralBillVO
	 */
	private GeneralBillVO getNoZeroNumVO(GeneralBillVO voBill) {
		if (voBill != null) {
			ArrayList alItems = new ArrayList();
			GeneralBillItemVO[] voaItem = voBill.getItemVOs();
			int size = voaItem.length;
			UFDouble ufdnum = null;
			UFDouble ufd0 = new UFDouble(0);
			int inotnull = 0;
			for (int i = 0; i < size; i++) {
				ufdnum = (UFDouble) voaItem[i].getAttributeValue(m_sNumItemKey);
				if (ufdnum != null && ufdnum.compareTo(ufd0) != 0) {
					alItems.add(voaItem[i]);
				}
			}
			inotnull = alItems.size();
			if (size > inotnull && inotnull > 0) {
				GeneralBillItemVO[] voNewItems = new GeneralBillItemVO[inotnull];
				alItems.toArray(voNewItems);
				GeneralBillVO voNewBill = (GeneralBillVO) voBill.clone();
				voNewBill.setChildrenVO(voNewItems);
				return voNewBill;
			} else if (inotnull == 0) {
				return null;
			} else if (size == inotnull) { return voBill; }
		}
		return null;
	}

	/**
	 * 功能：过滤出实发数量非空非零的表体行，打印用。 参数： 返回： 例外： 日期：(2005-2-21 21:33:32)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return java.util.ArrayList
	 * @param alBill
	 *            java.util.ArrayList
	 */
	// private ArrayList getNoZeroNumVOs(ArrayList alBill) {
	// if (alBill != null && alBill.size() > 0) {
	// int size = alBill.size();
	// GeneralBillVO vobill = null;
	// ArrayList alBillRet = new ArrayList();
	// for (int i = 0; i < size; i++) {
	// vobill = getNoZeroNumVO((GeneralBillVO) alBill.get(i));
	// if (vobill != null)
	// alBillRet.add(vobill);
	// }
	// if (alBillRet.size() > 0)
	// return alBillRet;
	// else
	// return null;
	// }
	// return null;
	// }
	/**
	 * 返回 LotNumbRefPane1 特性值。
	 * 
	 * @return nc.ui.ic.pub.lot.LotNumbRefPane
	 */
	/* 警告：此方法将重新生成。 */
	protected nc.ui.ic.pub.tools.VehicleRefPanel getVehicleRefPane() {
		if (ivjVehicleRefPane == null) {
			try {
				ivjVehicleRefPane = new nc.ui.ic.pub.tools.VehicleRefPanel();
				ivjVehicleRefPane.setName("ivjVehicleRefPane");
				ivjVehicleRefPane.setLocation(38, 1);
				ivjVehicleRefPane.setReturnCode(true);
				// ivjVehicleRefPane.setIsMutiSel(true);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjVehicleRefPane;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2003-8-25 13:53:58) 出库单自动拣货, 是否拆行
	 */
	public void onPickAuto() {

		if (m_voBill == null) return;

		if (!nc.ui.scm.pub.report.BillRowNo.verifyRowNosCorrect(getBillCardPanel(), m_sBillRowNo)) { return; }
		// zhy2005-05-17设置单据类型 解决自动捡货空指针
		m_voBill.setHeaderValue("cbilltypecode", m_sBillTypeCode);

		GeneralBillVO voOutBill = (GeneralBillVO) m_voBill.clone();
		// 借出转销售的不允许捡货
		String sBillType = voOutBill.getHeaderVO().getCbiztypeid();
		try {
			String sReturn = (new QueryInfo()).queryBusiTypeVerify(sBillType);
			if (sReturn != null && sReturn.equals("C")) {
				nc.ui.pub.beans.MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPPSCMCommon-000270")/*
																																				* @res "提示"
																																				*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000496")/*
																																																									* @res
																																																									* "借出转销售不能自动捡货！"
																																																									*/);
				return;
			}

		} catch (Exception e) {

			nc.vo.scm.pub.SCMEnv.error(e);
		}

		ArrayList alrow = new ArrayList();

		// ArrayList alNew = new ArrayList();

		GeneralBillItemVO[] voItems = voOutBill.getItemVOs();
		for (int i = 0; i < voItems.length; i++) {
			// zhy如果是辅计量管理的存货,没有输入辅单位,则不能做自动拣货
			InvVO invvo = voItems[i].getInv();
			Integer IsAstUOMmgt = invvo.getIsAstUOMmgt();
			if (IsAstUOMmgt != null && IsAstUOMmgt.intValue() == 1 && (voItems[i].getCastunitid() == null || voItems[i].getCastunitid().trim().length() == 0)) {
				nc.ui.pub.beans.MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/*
																																				 * @res "提示"
																																				 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000511")/*
																																																									* @res
																																																									* "表体行辅计量管理存货应输入辅单位，不能做自动拣货！"
																																																									*/);
				return;
			}

			if (voItems[i].getNshouldoutnum() == null || voItems[i].getNshouldoutnum().doubleValue() < 0) {
				nc.ui.pub.beans.MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/*
																																				 * @res "提示"
																																				 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000117")/*
																																																									* @res
																																																									* "表体行应发数量小于零，不能做自动拣货！"
																																																									*/);
				return;
			}
			// 过滤空行,纪录原始行号
			String crowno = null;
			if (voItems[i].getCinventoryid() != null) {
				if (voItems[i].getCrowno() == null) crowno = (String) getBillCardPanel().getBodyValueAt(i, "crowno");
				else crowno = voItems[i].getCrowno();
				voItems[i].setVbodynote2(crowno);
				alrow.add(voItems[i]);
			}

		}
		if (alrow.size() > 0) {
			voItems = new GeneralBillItemVO[alrow.size()];
			alrow.toArray(voItems);
			voOutBill.setChildrenVO(voItems);

		}

		java.util.Hashtable ht = new java.util.Hashtable();
		// Vector v = new Vector();
		GeneralBillVO voNew = null;
		voOutBill.getHeaderVO().setCoperatoridnow(m_sUserID);
		voOutBill.getHeaderVO().setClogdatenow(m_sLogDate);
		boolean isSN = false;
		boolean isLocator = false;
		try {
			voNew = nc.ui.ic.ic2a1.PickBillHelper.pickAuto(voOutBill, new nc.vo.pub.lang.UFDate(m_sLogDate));
			if (voNew != null && voNew.getItemVOs() != null) {

				calcSpace(voNew);

				voItems = voNew.getItemVOs();
				// 执行公式
				getBillCardPanel().getBillModel().execFormulasWithVO(voItems, getBodyFormula());

				// 纪录货未行

				boolean isVendor = false;
				// 货未档案ID
				String[] aryItemField11 = new String[] {
						"vspacename->getColValue(bd_cargdoc,csname,pk_cargdoc,cspaceid)", "vspacecode->getColValue(bd_cargdoc,cscode,pk_cargdoc,cspaceid)", "crsvbaseid5->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,cvendorid)", "vvendorname->getColValue(bd_cubasdoc,custname,pk_cubasdoc,crsvbaseid5)"
				};

				for (int i = 0; i < voItems.length; i++) {
					if (voItems[i].getCvendorid() != null) isVendor = true;
					if (voItems[i].getLocator() != null && voItems[i].getLocator().length > 0) {

						isLocator = true;

					}
					if (voItems[i].getSerial() != null && voItems[i].getSerial().length > 0) {

						isSN = true;
					}
				}

				// 有货位数据
				if (isLocator || isVendor) {
					nc.vo.pub.SuperVOUtil.execFormulaWithVOs(voItems, aryItemField11, null);

				}

			}
		} catch (Exception e) {

			nc.ui.pub.beans.MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000059")/* @res "错误" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000118")/* @res "自动拣货失败：" */
					+ e.getMessage());
			return;
		}

		if (voItems != null && voItems.length > 0) {
			String key = null;
			UFDouble dkey = new UFDouble(0);
			int iCurRow = 0;
			int icount = 0;// 计数器

			GeneralBillItemVO voRow = null;

			getBillCardPanel().getBillModel().setNeedCalculate(false);

			int irows = getBillCardPanel().getRowCount();
			UFDouble dLastRowNo = new UFDouble(0);
			String sLastRowNo = null;
			if (irows > 0) {
				sLastRowNo = (String) getBillCardPanel().getBodyValueAt(irows - 1, "crowno");// 最后一行的行号
				dLastRowNo = new UFDouble(sLastRowNo);
				// 现将光标置于第一行
				getBillCardPanel().getBillTable().setRowSelectionInterval(0, 0);
			}

			BatchCodeDefSetTool.execFormulaForBatchCode(voItems);

			for (int i = 0; i < voItems.length; i++) {
				key = voItems[i].getVbodynote2();
				dkey = new UFDouble(key);
				if (!ht.containsKey(key)) {// 如果是原行
					ht.put(key, key);
					voRow = voItems[i];
					getBillCardPanel().setBodyValueAt(key, i, "crowno");
					voRow.setCgeneralhid(voOutBill.getItemVOs()[icount].getCgeneralhid());
					voRow.setCgeneralbid(voOutBill.getItemVOs()[icount].getCgeneralbid());
					voRow.setCrowno(voRow.getVbodynote2());
					voRow.setVbodynote2(null);
					getBillCardPanel().getBillModel().setBodyRowVO(voRow, i);
					// 执行辅数量的编辑公式。1201
					execEditFomulas(i, m_sNumItemKey);
					if (getBillCardPanel().getBillModel().getRowState(i) != nc.ui.pub.bill.BillModel.ADD && getBillCardPanel().getBillModel().getRowState(i) != nc.ui.pub.bill.BillModel.MODIFICATION) getBillCardPanel().getBillModel().setRowState(i, nc.ui.pub.bill.BillModel.MODIFICATION);

					// 光标置于下一行（需要考虑临界状态）

					// if(dkey.compareTo(dLastRowNo)<0)
					// getBillCardPanel().getBillTable().setRowSelectionInterval(i+1,
					// i+1);
					// else{
					// //在后面增行
					// }
					// iCurRow=i+1;
					icount++;
				} else {// 如果是拆分的行，则插入行，然后将vo置入
					voRow = voItems[i];
					voRow.setCgeneralbid(null);
					voRow.setCgeneralhid(null);
					voRow.setVbodynote2(null);
					voRow.setNshouldoutnum(null);
					voRow.setNshouldoutassistnum(null);

					// if(key.compareTo(sLastRowNo)<0){
					if (dkey.compareTo(dLastRowNo) < 0) {
						// 插行
						iCurRow = i;
						getBillCardPanel().getBillTable().setRowSelectionInterval(iCurRow, iCurRow);
						getBillCardPanel().insertLine();
						nc.ui.scm.pub.report.BillRowNo.insertLineRowNo(getBillCardPanel(), m_sBillTypeCode, m_sBillRowNo);
						voRow.setCrowno((String) getBillCardPanel().getBodyValueAt(iCurRow, "crowno"));
						getBillCardPanel().getBillModel().setBodyRowVO(voRow, iCurRow);
						execEditFomulas(iCurRow, m_sNumItemKey);
					} else {
						// 增行
						getBillCardPanel().addLine();
						nc.ui.scm.pub.report.BillRowNo.addLineRowNo(getBillCardPanel(), m_sBillTypeCode, m_sBillRowNo);
						voRow.setCrowno((String) getBillCardPanel().getBodyValueAt(i, "crowno"));
						getBillCardPanel().getBillModel().setBodyRowVO(voRow, i);
						execEditFomulas(i, m_sNumItemKey);
					}

				}

				// zhy拣货后,从界面取一遍hsl
				GeneralBillUICtl.synUi2Vo(getBillCardPanel(), voNew, new String[] {
					"hsl"}, i);   //hsl为换算率
			}

			getBillCardPanel().getBillModel().setNeedCalculate(true);
			getBillCardPanel().getBillModel().reCalcurateAll();
			m_voBill = (GeneralBillVO) voNew.clone();
			// 初始化序列号数据
			if (isSN) m_alSerialData = voNew.getSNs();
			// 有货位数据
			if (isLocator)

			m_alLocatorData = voNew.getLocators();

		}

		return;

	}

	/**
	 * ?user> 功能： 参数： 返回： 例外： 日期：(2005-1-11 18:53:03) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @param voLot
	 *            nc.vo.ic.pub.lot.LotNumbRefVO
	 * @param irow
	 *            int
	 * 
	 */
	private void synline(GeneralBillItemVO vo, int iSelrow, boolean isFirstLine) {
		if (vo == null) return;

		if (!isFirstLine || vo.getCinventoryid() != null && !vo.getCinventoryid().equals(getBillCardPanel().getBodyValueAt(iSelrow, "cinventoryid"))) {
			getBillCardPanel().setBodyValueAt(vo.getCinventoryid(), iSelrow, "cinventoryid");
			nc.ui.pub.beans.UIRefPane refCastunit = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getBodyItem("cinventorycode").getComponent());
			refCastunit.setPK(vo.getCinventoryid());

			getBillCardPanel().setBodyValueAt(refCastunit.getRefCode(), iSelrow, "cinventorycode");
			afterEdit(new nc.ui.pub.bill.BillEditEvent(this, null, refCastunit.getRefCode(), "cinventorycode", iSelrow, BillItem.BODY));

		}
		if (getBillCardPanel().getBodyItem("castunitid") != null) {
			String oldvalue = (String) getBillCardPanel().getBodyValueAt(iSelrow, "castunitid");// refCastunit.getRefPK();
			m_voBill.setItemValue(iSelrow, "cselastunitid", vo.getCastunitid());
			nc.vo.ic.pub.DesassemblyVO voDesa = (nc.vo.ic.pub.DesassemblyVO) m_voBill.getItemValue(iSelrow, "desainfo");

			if (voDesa.getMeasInfo() == null) {
				nc.vo.bd.b15.MeasureRateVO[] voMeasInfo = m_voInvMeas.getMeasInfo(m_sCorpID, vo.getCinventoryid());
				voDesa.setMeasInfo(voMeasInfo);
			}
			m_voBill.setItemValue(iSelrow, "idesatype", new Integer(voDesa.getDesaType()));
			getBillCardPanel().setBodyValueAt(new Integer(voDesa.getDesaType()), iSelrow, "idesatype");
			getBillCardPanel().setBodyValueAt(vo.getCastunitid(), iSelrow, "cselastunitid");

			if (!isFirstLine || (voDesa.getDesaType() == nc.vo.ic.pub.DesassemblyVO.TYPE_NO && !GenMethod.isStringEqual(vo.getCastunitid(), oldvalue))) {

				getBillCardPanel().setBodyValueAt(vo.getCastunitid(), iSelrow, "castunitid");
				nc.ui.pub.beans.UIRefPane refCastunit = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getBodyItem("castunitname").getComponent());
				refCastunit.setPK(vo.getCastunitid());
				getBillCardPanel().setBodyValueAt(vo.getCastunitname(), iSelrow, "castunitname");

				nc.vo.bd.b15.MeasureRateVO voMeas = m_voInvMeas.getMeasureRate(m_voBill.getItemInv(iSelrow).getCinventoryid(), vo.getCastunitid());
				if (voMeas != null) {

					UFDouble hsl = voMeas.getMainmeasrate();
					getBillCardPanel().setBodyValueAt(hsl, iSelrow, "hsl");
					hsl = (UFDouble) getBillCardPanel().getBodyValueAt(iSelrow, "hsl");
					m_voBill.setItemValue(iSelrow, "hsl", hsl);
					m_voBill.setItemValue(iSelrow, "isSolidConvRate", voMeas.getFixedflag());

				}

				m_voBill.setItemValue(iSelrow, "castunitid", vo.getCastunitid());

				if (m_voBill.getItemValue(iSelrow, "isSolidConvRate") != null && ((Integer) m_voBill.getItemValue(iSelrow, "isSolidConvRate")).intValue() == 0) {
					getBillCardPanel().setBodyValueAt(vo.getAttributeValue("hsl"), iSelrow, "hsl");
					m_voBill.setItemValue(iSelrow, "hsl", vo.getAttributeValue("hsl"));
				}

				afterEdit(new nc.ui.pub.bill.BillEditEvent(this, null, vo.getCastunitname(), "castunitname", iSelrow, BillItem.BODY));

			}
			// 第一行，且两个辅助单位hsl相同，则替换hsl
			if (m_voBill.getItemValue(iSelrow, "isSolidConvRate") != null && ((Integer) m_voBill.getItemValue(iSelrow, "isSolidConvRate")).intValue() == 0) {
				getBillCardPanel().setBodyValueAt(vo.getAttributeValue("hsl"), iSelrow, "hsl");
				m_voBill.setItemValue(iSelrow, "hsl", vo.getAttributeValue("hsl"));
			}
		}
		//
		if (getBillCardPanel().getBodyItem("vbatchcode") != null) {
			// if
			// (!isFirstLine||!GenMethod.isStringEqual(vo.getVbatchcode(),(String)m_voBill.getItemValue(iSelrow,
			// "vbatchcode"))){
			if (!isFirstLine || !GenMethod.isStringEqual(vo.getVbatchcode(), (String) getBillCardPanel().getBodyValueAt(iSelrow, "vbatchcode"))) {
				getBillCardPanel().setBodyValueAt(vo.getVbatchcode(), iSelrow, "vbatchcode");
				BatchCodeDefSetTool.setBatchCodeInfo(getBillCardPanel(), iSelrow, vo.getCinventoryid(), vo.getVbatchcode(), m_sCorpID);
				// // 该批次的失效日期
				// getBillCardPanel().setBodyValueAt(vo.getDvalidate(), iSelrow,
				// "dvalidate");
				// vo.calPrdDate();
				// v35 lj 带出失效日期生产日期
				// InvVO voInvo = m_voBill.getItemInv(iSelrow);

				// if (voInvo.getQualityDay()!=null) {
				// int ibef = voInvo.getQualityDay().intValue();
				// vo.setScrq( vo.getDvalidate().getDateBefore(ibef));
				// }
				//                                         
				// getBillCardPanel()
				// .setBodyValueAt(vo.getScrq(), iSelrow, "scrq");
				m_voBill.setItemValue(iSelrow, "vbatchcode", vo.getVbatchcode());

				m_voBill.setItemValue(iSelrow, "scrq", vo.getScrq());
				m_voBill.setItemValue(iSelrow, "dvalidate", vo.getDvalidate());
			}
		}
		if (getBillCardPanel().getBodyItem("cvendorid") != null) {
			// if
			// (!isFirstLine||!GenMethod.isStringEqual(vo.getCvendorid(),(String)m_voBill.getItemValue(iSelrow,
			// "cvendorid"))){
			if (!isFirstLine || !GenMethod.isStringEqual(vo.getCvendorid(), (String) getBillCardPanel().getBodyValueAt(iSelrow, "cvendorid"))) {

				getBillCardPanel().setBodyValueAt(vo.getCvendorid(), iSelrow, "cvendorid");
				getBillCardPanel().setBodyValueAt(vo.getVvendorname(), iSelrow, "vvendorname");
				UIRefPane ref = (UIRefPane) getBillCardPanel().getBodyItem("vvendorname").getComponent();
				ref.setPK(vo.getCvendorid());
				m_voBill.setItemValue(iSelrow, "cvendorid", vo.getCvendorid());
				m_voBill.setItemValue(iSelrow, "vvendorname", vo.getVvendorname());
			}
		}
		if (getBillCardPanel().getBodyItem("cprojectid") != null) {
			String cprojectid = (String) vo.getAttributeValue("cprojectid");
			if (!isFirstLine || !GenMethod.isStringEqual(cprojectid, (String) getBillCardPanel().getBodyValueAt(iSelrow, "cprojectid"))) {

				getBillCardPanel().setBodyValueAt(cprojectid, iSelrow, "cprojectid");
				getBillCardPanel().setBodyValueAt(vo.getAttributeValue("cprojectname"), iSelrow, "cprojectname");
				UIRefPane ref = (UIRefPane) getBillCardPanel().getBodyItem("cprojectname").getComponent();
				ref.setPK(cprojectid);
				m_voBill.setItemValue(iSelrow, "cprojectid", cprojectid);
				m_voBill.setItemValue(iSelrow, "cprojectname", vo.getAttributeValue("cprojectname"));
			}
		}

		if (getBillCardPanel().getBodyItem("vtransfercode") != null && vo.getAttributeValue("vtransfercode") != null) {
			if (!isFirstLine || !GenMethod.isStringEqual((String) vo.getAttributeValue("vtransfercode"), (String) getBillCardPanel().getBodyValueAt(iSelrow, "vtransfercode"))) {

				getBillCardPanel().setBodyValueAt(vo.getAttributeValue("vtransfercode"), iSelrow, "vtransfercode");

				m_voBill.setItemValue(iSelrow, "vtransfercode", (String) vo.getAttributeValue("vtransfercode"));
			}
		}

		String userdef = null;
		for (int i = 1; i <= 20; i++) {
			userdef = "vuserdef" + String.valueOf(i);
			if (vo.getAttributeValue(userdef) == null || getBillCardPanel().getBodyValueAt(iSelrow, userdef) != null) continue;

			getBillCardPanel().setBodyValueAt(vo.getAttributeValue(userdef), iSelrow, userdef);
			m_voBill.setItemValue(iSelrow, userdef, vo.getAttributeValue(userdef));
			userdef = "pk_defdoc" + String.valueOf(i);
			getBillCardPanel().setBodyValueAt(vo.getAttributeValue(userdef), iSelrow, userdef);
			m_voBill.setItemValue(iSelrow, userdef, vo.getAttributeValue(userdef));

		}
		if (getBillCardPanel().getBodyItem("vfree0") != null) {
			FreeVO voFree = m_voBill.getItemVOs()[iSelrow].getFreeItemVO();

			for (int i = 1; i <= 5; i++) {
				userdef = "vfree" + String.valueOf(i);
				getBillCardPanel().setBodyValueAt(vo.getAttributeValue(userdef), iSelrow, userdef);
				m_voBill.setItemValue(iSelrow, userdef, vo.getAttributeValue(userdef));
				voFree.setAttributeValue(userdef, vo.getAttributeValue(userdef));

			}
			getBillCardPanel().setBodyValueAt(voFree.getVfree0(), iSelrow, "vfree0");
		}
		if (getBillCardPanel().getBodyItem(m_sNumItemKey) != null) {
			UFDouble num = null;
			if (!isFirstLine || getBillCardPanel().getBodyValueAt(iSelrow, m_sNumItemKey) == null) {
				num = new UFDouble((-1) * (m_iBillInOutFlag)).multiply(vo.getNinnum());
				getBillCardPanel().setBodyValueAt(num, iSelrow, m_sNumItemKey);
				afterEdit(new nc.ui.pub.bill.BillEditEvent(this, null, num, m_sNumItemKey, iSelrow, BillItem.BODY));
			}
			if (!isFirstLine && vo.getCastunitid() != null && getBillCardPanel().getBodyValueAt(iSelrow, m_sAstItemKey) == null) {
				num = new UFDouble((-1) * (m_iBillInOutFlag)).multiply(vo.getNinassistnum());
				getBillCardPanel().setBodyValueAt(num, iSelrow, m_sAstItemKey);
				afterEdit(new nc.ui.pub.bill.BillEditEvent(this, null, num, m_sAstItemKey, iSelrow, BillItem.BODY));

			}

		}

		if (getBillCardPanel().getBodyItem(m_sNgrossnum) != null) {
			InvVO voInvo = m_voBill.getItemInv(iSelrow);
			if (voInvo.getIsmngstockbygrswt() != null && voInvo.getIsmngstockbygrswt().intValue() == 1) {
				UFDouble numgross = null;
				if (!isFirstLine || getBillCardPanel().getBodyValueAt(iSelrow, m_sNgrossnum) == null) {
					numgross = vo.getNingrossnum();
					if (numgross != null) numgross = new UFDouble((-1) * (m_iBillInOutFlag)).multiply(numgross);

					getBillCardPanel().setBodyValueAt(numgross, iSelrow, m_sNgrossnum);
					afterEdit(new nc.ui.pub.bill.BillEditEvent(this, null, numgross, m_sNgrossnum, iSelrow, BillItem.BODY));
				}
			}

		}

		// 执行表体单价计算金额的公式.
		// if (getBillCardPanel().getBodyItem("nprice") != null) {
		// if (!isFirstLine || vo.getNprice() != null) {
		// getBillCardPanel().setBodyValueAt(vo.getNprice(), iSelrow,
		// "nprice");
		// m_voBill.setItemValue(iSelrow, "nprice", vo.getNprice());
		// execEditFomulas(iSelrow, "nprice");
		// }
		// }
		// 对应单据号
		getBillCardPanel().setBodyValueAt(vo.getCcorrespondcode(), iSelrow, m_sCorBillCodeItemKey);
		// 对应单据类型
		getBillCardPanel().setBodyValueAt(vo.getCcorrespondtype(), iSelrow, "ccorrespondtype");
		// 对应单据表头OID
		// 单据模板库中表体位置两个不显示列ccorrespondhid,ccorrespondbid,以保存带出的对应表头，表体OID
		getBillCardPanel().setBodyValueAt(vo.getCcorrespondhid(), iSelrow, "ccorrespondhid");
		// 对应单据表体OID
		getBillCardPanel().setBodyValueAt(vo.getCcorrespondbid(), iSelrow, "ccorrespondbid");
		// 在途标记
		if (getBillCardPanel().getBodyItem("bonroadflag") != null) {
			getBillCardPanel().setBodyValueAt(vo.getAttributeValue("bonroadflag"), iSelrow, "bonroadflag");
		}
		m_voBill.setItemValue(iSelrow, "bonroadflag", vo.getAttributeValue("bonroadflag"));

		m_voBill.setItemValue(iSelrow, "ccorrespondbid", vo.getCcorrespondbid());
		// getICCorBillRef().getCorBillBid());
		m_voBill.setItemValue(iSelrow, "ccorrespondhid", vo.getCcorrespondhid());
		// getICCorBillRef().getCorBillHid());
		m_voBill.setItemValue(iSelrow, m_sCorBillCodeItemKey, vo.getCcorrespondcode());
		// getICCorBillRef().getCorBillCode());
		m_voBill.setItemValue(iSelrow, "ccorrespondtype", vo.getCcorrespondtype());
		// getICCorBillRef().getCorBillType());

	}

	protected javax.swing.JFileChooser m_chooser = null;

	DefVO[] m_defBody = null;

	DefVO[] m_defHead = null;

	String m_sDir = null;

	// 单据导出菜单
	protected Vector m_vBillExcel = null;

	/**
	 * 作者：李俊 功能：客户编辑后事件 参数： 返回： 例外： 日期：(2004-6-21 10:38:55) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @param e
	 *            nc.ui.pub.bill.BillEditEvent
	 */
	public void afterCustomerEdit(nc.ui.pub.bill.BillEditEvent e) {
		// 客户
		String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("ccustomerid").getComponent()).getRefName();
		String sRefPK = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("ccustomerid").getComponent()).getRefPK();
		// 保存名称以在列表形式下显示。
		if (m_voBill != null) m_voBill.setHeaderValue("ccustomername", sName);
		// 根据客户或供应商过滤发运地址的参照
		filterVdiliveraddressRef(true, -1);

		// 根据客户或供应商过滤发运地址的参照
		if (getBillCardPanel().getHeadItem("vdiliveraddress") != null && sRefPK != null) {

			((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("vdiliveraddress").getComponent()).setWhereString("pk_cubasdoc = (select pk_cubasdoc from bd_cumandoc where pk_cumandoc = '" + sRefPK + "')");
		}

		BillItem iProvidername = getBillCardPanel().getHeadItem("ccustomershortname");

		BillItem iPk_cubasdocC = getBillCardPanel().getHeadItem("pk_cubasdocC");

		try {
			// 根据参照带出客商简称
			nc.ui.scm.pub.TwoTableCacheFind twoTable = new nc.ui.scm.pub.TwoTableCacheFind();
			String sCustomerShortName = twoTable.getJoinTableFieldValue("bd_cumandoc", sRefPK, "custshortname");
			if (iProvidername != null) {
				iProvidername.setValue(sCustomerShortName);
			}

			// 获得客商基本档案ID
			String sPk_cubasdoc = getPk_cubasdoc(sRefPK);
			if (iPk_cubasdocC != null) iPk_cubasdocC.setValue(sPk_cubasdoc);

			if (m_voBill != null) {
				m_voBill.setHeaderValue("ccustomershortname", sCustomerShortName);
				m_voBill.setHeaderValue("pk_cubasdocC", sPk_cubasdoc);
			}
		} catch (BusinessException be) {
			Logger.error(be.getMessage(), be);
			MessageDialog.showErrorDlg(this, null, be.getMessage());
		} catch (BusinessRuntimeException bre) {
			Logger.error(bre.getMessage(), bre);
			MessageDialog.showErrorDlg(this, null, bre.getMessage());
		} catch (Exception ee) {
			MessageDialog.showUnknownErrorDlg(this, ee);
		}

	}

	/**
	 * 作者：李俊 功能：供应商编辑后事件：带出简称 参数： 返回： 例外： 日期：(2004-6-21 10:36:30)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @param e
	 *            nc.ui.pub.bill.BillEditEvent
	 */
	public void afterProviderEdit(nc.ui.pub.bill.BillEditEvent e) {

		String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("cproviderid").getComponent()).getRefName();
		String sRefPK = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("cproviderid").getComponent()).getRefPK();

		// 保存名称以在列表形式下显示。
		if (m_voBill != null) m_voBill.setHeaderValue("cprovidername", sName);

		// 根据客户或供应商过滤发运地址的参照
		if (getBillCardPanel().getHeadItem("vdiliveraddress") != null) {
			((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("vdiliveraddress").getComponent()).setWhereString("pk_cubasdoc = (select pk_cubasdoc from bd_cumandoc where pk_cumandoc = '" + sRefPK + "')");
		}

		BillItem iProvidername = getBillCardPanel().getHeadItem("cprovidershortname");
		BillItem iPk_cubasdoc = getBillCardPanel().getHeadItem("pk_cubasdoc");
		try {
			// 根据参照带出供应商简称
			nc.ui.scm.pub.TwoTableCacheFind twoTable = new nc.ui.scm.pub.TwoTableCacheFind();
			String sProviderShortName = twoTable.getJoinTableFieldValue("bd_cumandoc", sRefPK, "custshortname");
			if (iProvidername != null) {
				iProvidername.setValue(sProviderShortName);
			}

			// 获得客商基本档案ID
			String sPk_cubasdoc = getPk_cubasdoc(sRefPK);
			if (iPk_cubasdoc != null) iPk_cubasdoc.setValue(sPk_cubasdoc);

			if (m_voBill != null) {
				m_voBill.setHeaderValue("cprovidershortname", sProviderShortName);
				m_voBill.setHeaderValue("pk_cubasdoc", sPk_cubasdoc);
			}
		} catch (BusinessException be) {
			Logger.error(be.getMessage(), be);
			MessageDialog.showErrorDlg(this, null, be.getMessage());
		} catch (BusinessRuntimeException bre) {
			Logger.error(bre.getMessage(), bre);
			MessageDialog.showErrorDlg(this, null, bre.getMessage());
		} catch (Exception ee) {
			MessageDialog.showUnknownErrorDlg(this, ee);
		}

	}

	/**
	 * 
	 * 方法功能描述：根据客商管理档案获得客商的基本档案。
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @param pk_cumangid
	 *            客商管理档案的ID
	 * @return 客商基本档案ID
	 *         <p>
	 * @author duy
	 * @time 2007-3-20 上午10:50:26
	 */
	private String getPk_cubasdoc(String pk_cumandoc) {
		if (pk_cumandoc == null) return null;
		try {
			Object[] pks = (Object[]) nc.ui.scm.pub.CacheTool.getColumnValue("bd_cumandoc", "pk_cumandoc", "pk_cubasdoc", new String[] {
				pk_cumandoc
			});
			if (pks != null) return (String) pks[0];
		} catch (BusinessException e) {
			nc.ui.ic.pub.tools.GenMethod.handleException(this, null, e);
		}
		return null;
	}

	/**
	 * 创建者：王乃军 功能：仓库改变事件处理 参数：e单据编辑事件 返回： 例外： 日期：(2001-5-8 19:08:05)
	 * 修改日期，修改人，修改原因，注释标志：
	 */
	public void afterWhEdit(nc.ui.pub.bill.BillEditEvent e, String sNewWhName, String sNewWhID) {
		// 仓库
		try {
			getBillCardPanel().rememberFocusComponent();
			if (sNewWhID == null) {
				sNewWhName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem(m_sMainWhItemKey).getComponent()).getRefName();
				sNewWhID = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem(m_sMainWhItemKey).getComponent()).getRefPK();
			} else {
				((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem(m_sMainWhItemKey).getComponent()).setValue(sNewWhName);
				((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem(m_sMainWhItemKey).getComponent()).setPK(sNewWhID);
			}
			// 请空表体的显示数据
			try {
				if (sNewWhID == null || (m_voBill != null && !sNewWhID.equals(m_voBill.getHeaderVO().getCwarehouseid()))) {

					String sIKs[] = new String[] {
							"cspaceid", "vspacename", "ccorrespondbid", "ccorrespondcode", "ccorrespondhid", "ccorrespondtype", "nplannedprice", "nplannedmny"
					};

					int iRowCount = getBillCardPanel().getRowCount();
					for (int row = 0; row < iRowCount; row++)
						clearRowData(row, sIKs);
				}
				// if (iRowCount>0)
				// getBillCardPanel().getBillTable().setRowSelectionInterval(0,
				// 0);
			} catch (Exception e2) {
				SCMEnv.out("可以忽略的错误：" + e2);
			}
			// 清空了仓库
			if (sNewWhID == null) {
				// 清空批次号参照的仓库
				getLotNumbRefPane().setWHParams(null);
				if (m_voBill != null) m_voBill.setWh(null);
			} else {

				// 保存名称以在列表形式下显示。
				// 查询仓库信息
				// 查询方式：如果已经录入了存货，需要同时查计划价。
				int iQryMode = QryInfoConst.WH;
				// 参数
				Object oParam = sNewWhID;
				// 当前已录入的存货数据
				ArrayList alAllInvID = new ArrayList();
				boolean bHaveInv = getCurInvID(alAllInvID);

				// 仓库
				WhVO voWh = null;
				// 清空批次号参照的仓库
				getLotNumbRefPane().setWHParams(null);
				if (m_voBill != null) m_voBill.setWh(null);

				if (bHaveInv) {
					// 参数：仓库ID,原库存组织ID,单位ID,存货ID
					ArrayList alParam = new ArrayList();
					alParam.add(sNewWhID);
					iQryMode = QryInfoConst.WH_PLANPRICE;
					// 当前的库存组织,考虑没有仓库的情况。
					if (m_voBill != null && m_voBill.getWh() != null) alParam.add(m_voBill.getWh().getPk_calbody());
					else alParam.add(null);
					// 公司
					alParam.add(m_sCorpID);
					// 当前的存货
					alParam.add(alAllInvID);
					oParam = alParam;
				}

				Object oRet = GeneralBillHelper.queryInfo(new Integer(iQryMode), oParam);
				// Object oRet = invokeClient("queryInfo", new Class[] {
				// Integer.class, Object.class }, new Object[] {
				// new Integer(iQryMode), oParam });

				// 当前已录入的存货数据,并且修改了库存组织才返回一个ArrayList
				if (oRet instanceof ArrayList) {
					ArrayList alRetValue = (ArrayList) oRet;
					if (alRetValue != null && alRetValue.size() >= 2) {
						voWh = (WhVO) alRetValue.get(0);
						// 刷新计划价
						freshPlanprice((ArrayList) alRetValue.get(1));
					}
				} else
				// 否则返回 WhVO
				voWh = (WhVO) oRet;
				// 库存组织处理
				nc.ui.pub.bill.BillItem biCalBody = getBillCardPanel().getHeadItem("pk_calbody");
				if (biCalBody != null) {
					if (voWh != null) biCalBody.setValue(voWh.getPk_calbody());
					else biCalBody.setValue(null);
				}
				nc.ui.pub.bill.BillItem biCalBodyname = getBillCardPanel().getHeadItem("vcalbodyname");
				if (biCalBodyname != null) {
					if (voWh != null) biCalBodyname.setValue(voWh.getVcalbodyname());
					else biCalBodyname.setValue(null);
				}
				// // 过滤计量器具档案
				// String[] sConstraint1 = new String[1];
				// sConstraint1[0] = " and mm_jldoc.gcbm='" +
				// voWh.getPk_calbody()
				// + "'";
				// nc.ui.pub.bill.BillItem bi1 = getBillCardPanel().getHeadItem(
				// "pk_measware");
				// RefFilter.filtMeasware(bi1, m_sCorpID, sConstraint1);

				if (m_voBill != null) {
					m_voBill.setWh(voWh);
					// 清表尾现存量
					m_voBill.clearInvQtyInfo();
					getLotNumbRefPane().setWHParams(voWh);

					// DUYONG 刷新计划价
					freshPlanprice(getInvoInfoBYFormula().getPlanPrice(alAllInvID, voWh.getPk_calbody(), voWh.getCwarehouseid()));
				}

				// 清货位、序列号数据放在afterEdit()的clearLocSn中
				// int iRowCount = getBillCardPanel().getRowCount();
				// for (int row = 0; row < iRowCount; row++)
				// clearRowData(0, row, m_sMainWhItemKey);
				// 刷新现存量显示
				setTailValue(0);
				// 设置货位分配按钮是否可用。
				setBtnStatusSpace(true);
			}

			getBillCardPanel().restoreFocusComponent();

		} catch (Exception e2) {
			SCMEnv.out(e2);
		}

	}

	/**
	 * ?user> 功能： 参数： 返回： 例外： 日期：(2004-8-27 10:54:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @param voNew
	 *            nc.vo.ic.pub.bill.GeneralBillVO
	 * 
	 *            修改问题：由调拨订单生成调拨出库单时（推式&拉式），货位没有保存上。
	 *            修改原因分析：货位保存时，使用locator中保存的数据，而非表体中显示的space；
	 *            在space修改后，afterEdit会调用afterSpaceEdit方法，自动同步space与locator；
	 *            但在推或拉上游单据时，space不会自动同步locator，以至保存不上。 修改时间：2005-11-24
	 *            修改者：ShawBing
	 * 
	 */
	protected void calcSpace(GeneralBillVO voNew) {
		if (voNew != null && voNew.getItemVOs() != null) {
			GeneralBillItemVO[] voItems = voNew.getItemVOs();
			// 纪录货未行
			boolean isLocator = false;
			// 货未档案ID
			String[] aryItemField11 = new String[] {
					"vspacename->getColValue(bd_cargdoc,csname,pk_cargdoc,cspaceid)", "vspacecode->getColValue(bd_cargdoc,cscode,pk_cargdoc,cspaceid)"
			};

			// boolean isSN = false;

			for (int i = 0; i < voItems.length; i++) {

				if (voItems[i].getCspaceid() != null) {
					isLocator = true;

					/** 1-1 下面部分为2005-11-25 Shaw 修改内容，修改说明见方法注释* */
					// 在推或拉式生成单据时，space有值，而locaor没值，导致货位无法保存
					// 同步locator
					if (voItems[i].getLocator() == null || voItems[i].getLocator().length == 0) {

						LocatorVO[] voLocators = new LocatorVO[1];
						voLocators[0] = new LocatorVO();
						voLocators[0].setCspaceid(voItems[i].getCspaceid());

						voItems[i].setLocator(voLocators);
						m_alLocatorData.remove(i);
						m_alLocatorData.add(i, voLocators);

						resetSpace(i);
					}
					/** 1-1 修改结束* */
				}

				if (voItems[i].getLocator() != null && voItems[i].getLocator().length > 0) {
					nc.vo.pub.SuperVOUtil.execFormulaWithVOs(voItems[i].getLocator(), aryItemField11, null);
					isLocator = true;

				}
				if (voItems[i].getSerial() != null && voItems[i].getSerial().length > 0) {
					nc.vo.pub.SuperVOUtil.execFormulaWithVOs(voItems[i].getSerial(), aryItemField11, null);
					// isSN = true;
				}
			}

			if (isLocator) {
				nc.vo.pub.SuperVOUtil.execFormulaWithVOs(voItems, aryItemField11, null);

			}

		}
		return;
	}

	/**
	 * 创建者：仲瑞庆 功能： 参数： 返回： 例外： 日期：(2001-11-8 19:47:29) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return nc.ui.pub.bill.BillData
	 * @param oldBillData
	 *            nc.ui.pub.bill.BillData
	 */
	protected BillData changeBillDataByUserDef(DefVO[] defHead, DefVO[] defBody, BillData oldBillData) {
		try {
			// 进行自定义项定义用
			// DefVO[] defs= null;
			// 表头
			// 查得对应于公司的该单据的自定义项设置
			// defs= nc.ui.bd.service.BDDef.queryDefVO("供应链/ARAP单据头",
			// m_sCorpID);
			if ((defHead != null)) {
				oldBillData.updateItemByDef(defHead, "vuserdef", true);
				for (int i = 1; i <= 20; i++) {
					nc.ui.pub.bill.BillItem item = oldBillData.getHeadItem("vuserdef" + i);
					if (item != null && item.getDataType() == nc.ui.pub.bill.BillItem.USERDEF) {
						((nc.ui.pub.beans.UIRefPane) item.getComponent()).setAutoCheck(true);
					}
				}
			}

			// 表体
			// 查得对应于公司的该单据的自定义项设置
			// defs= nc.ui.bd.service.BDDef.queryDefVO("供应链/ARAP单据体",
			// m_sCorpID);
			if ((defBody == null)) {
				return oldBillData;
			} else {
				oldBillData.updateItemByDef(defBody, "vuserdef", false);
				for (int i = 1; i <= 20; i++) {
					nc.ui.pub.bill.BillItem item = oldBillData.getBodyItem("vuserdef" + i);
					if (item != null && item.getDataType() == nc.ui.pub.bill.BillItem.USERDEF) {
						((nc.ui.pub.beans.UIRefPane) item.getComponent()).setAutoCheck(true);
					}
				}
			}
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
		}

		return oldBillData;
	}

	/**
	 * 创建者：仲瑞庆 功能： 参数： 返回： 例外： 日期：(2001-11-8 19:47:29) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return nc.ui.pub.bill.BillData
	 * @param oldBillData
	 *            nc.ui.pub.bill.BillData
	 */
	protected BillListData changeBillListDataByUserDef(DefVO[] defHead, DefVO[] defBody, BillListData oldBillData) {
		try {
			if (defHead != null)

			oldBillData.updateItemByDef(defHead, "vuserdef", true);

			if (defBody != null) oldBillData.updateItemByDef(defBody, "vuserdef", false);

			return oldBillData;
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
		}
		return oldBillData;
	}

	/**
	 * 清除定位颜色
	 */
	public void clearOrientColor() {
		if (m_isLocated) {
			if (m_iCurPanel == BillMode.List) nc.ui.scm.pub.report.OrientDialog.clearOrientColor(getBillListPanel().getHeadTable());
			else nc.ui.scm.pub.report.OrientDialog.clearOrientColor(getBillCardPanel().getBillTable());
			m_isLocated = false;
		}

	}

	/**
	 * 程起伍 功能：执行特殊公式. 目前只有销售出库单重载此方法. 参数： 返回： 例外： 日期：(2004-7-20 17:19:12)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 */
	protected void execExtendFormula(ArrayList alListData) {
	}

	/**
	 * 创建者：仲瑞庆 功能： 参数： 返回： 例外： 日期：(2002-1-24 11:35:23) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return boolean
	 */
	/**
	 * 返回 BillCardPanel1 特性值。
	 * 
	 * @return nc.ui.pub.bill.BillCardPanel bEditable:是否可以编辑： bSaveable:是否可以直接保存
	 * 
	 *         条码编辑界面状态： 只读，用来显示 修改：可以修改，但需要通过单据才能保存 保存：可以直接保存，只在浏览状态才可以？
	 * 
	 */
	/* 警告：此方法将重新生成。 */
	public BarCodeDlg getBarCodeDlg(boolean bEditable, boolean bSaveable) {
		m_dlgBarCodeEdit = new BarCodeDlg(this, this, m_sCorpID, bEditable, bSaveable);
		return m_dlgBarCodeEdit;
	}

	/**
	 * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2002-7-26 11:51:17) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return javax.swing.JFileChooser
	 */
	protected javax.swing.JFileChooser getChooser() {

		if (m_chooser == null) {
			m_chooser = new JFileChooser();
			m_chooser.setDialogType(JFileChooser.SAVE_DIALOG);

			// m_chooser.setFileSelectionMode(UIFileChooser.DIRECTORIES_ONLY);
		}
		return m_chooser;

	}

	/**
	 * ?user> 功能： 参数： 返回： 例外： 日期：(2004-8-31 15:56:03) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return java.lang.String
	 */
	public String getDefaultDir() {

		String sDir = null;
		try {
			nc.ui.ic.pub.property.PropertyCtrl ctrl = new nc.ui.ic.pub.property.PropertyCtrl();
			sDir = ctrl.getItemValueByKey(nc.ui.ic.pub.property.IPropertyFile.ExcelBarcode_FileName_Param2);
			if (sDir == null) {
				java.io.File file = new java.io.File(nc.ui.ic.pub.property.IPropertyFile.ExcelBarcode_FileName_Param2_Value);
				if (!file.exists()) file.mkdir();
				sDir = file.getAbsolutePath();
			}
		} catch (Exception ex) {
			nc.vo.scm.pub.SCMEnv.error(ex);
		}
		return sDir;

	}

	/**
	 * ?user> 功能： 参数： 返回： 例外： 日期：(2004-6-30 17:57:26) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return nc.vo.bd.def.DefVO[]
	 * @param pk_corp
	 *            java.lang.String
	 * @param isHead
	 *            boolean
	 */
	public DefVO[] getDefBodyVO() {

		if (m_defBody == null) {
			try {
				m_defBody = DefSetTool.getDefBody(m_sCorpID, ICConst.BILLTYPE_IC);
			} catch (Exception e) {
				nc.vo.scm.pub.SCMEnv.error(e);
			}
		}
		return m_defBody;
	}

	/**
	 * ?user> 功能： 参数： 返回： 例外： 日期：(2004-6-30 17:57:26) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return nc.vo.bd.def.DefVO[]
	 * @param pk_corp
	 *            java.lang.String
	 * @param isHead
	 *            boolean
	 */
	public DefVO[] getDefHeadVO() {
		if (m_defHead == null) {
			// m_defHead = nc.ui.bd.service.BDDef.queryDefVO("供应链/ARAP单据头",
			// m_sCorpID);
			try {
				m_defHead = DefSetTool.getDefHead(m_sCorpID, ICConst.BILLTYPE_IC);
			} catch (Exception e) {
				nc.vo.scm.pub.SCMEnv.error(e);
			}
		}
		return m_defHead;
	}

	/**
	 * 单据导出XML 创建日期：(2003-09-28 9:51:50)
	 */
	public void onExportXML(GeneralBillVO[] billvos, String filename) {
		if (billvos == null || billvos.length <= 0 || filename == null) return;
		try {

			MessageDialog.showInputDlg(this, "请输入外部系统编码", "请输入外部系统编码:", "20", 5);

			IPFxxEJBService export = (IPFxxEJBService) NCLocator.getInstance().lookup(IPFxxEJBService.class.getName());
			Document outdocs = export.exportBills(billvos, getClientEnvironment().getAccount().getAccountCode(), getClientEnvironment().getCorporation().getPrimaryKey(), "IC", "20");

			FileUtils.writeDocToXMLFile(outdocs, filename);
		} catch (Exception e) {
			showErrorMessage(e.getMessage());
		}
	}

	/**
	 * 单据导出xml 创建日期：(2003-09-28 9:51:50)
	 */
	public void onBillExcel(int iFlag) {

		ExcelFileVO[] vos = null;
		GeneralBillItemVO[] tvos = null;
		GeneralBillVO voBill = null;
		String sBillCode = null;
		// String sPKCorp = null;
		// String sFilePath = null;
		String sFilePathDir = null;
		String sBillTypeCode = null;
		String sBillTypeName = null;

		if (iFlag == 1 || iFlag == 3/* 导出为xml */) {
			// 打开文件
			if (getChooser().showSaveDialog(this) == javax.swing.JFileChooser.CANCEL_OPTION) return;
			sFilePathDir = getChooser().getSelectedFile().toString();
		}
		if (sFilePathDir == null) {
			showHintMessage("请输入文件名保存!");
			return;
		}

		try {
			// 依当前是列表还是表单而定导出内容
			if (m_iCurPanel == BillMode.Card) { // 浏览
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000119")/*
																												* @res "正在导出，请稍候..."
																												*/);

				// 准备数据
				voBill = m_voBill;
				if (voBill == null) {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000049")/*
																													* @res
																													* "请先查询或录入单据。"
																													*/);
					return;
				}
				if (voBill.getParentVO() == null) {
					voBill.setParentVO(new GeneralBillHeaderVO());
				}
				if ((voBill.getChildrenVO() == null) || (voBill.getChildrenVO().length == 0) || (voBill.getChildrenVO()[0] == null)) {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH003")/*
																									 * @res "请选择要处理的数据！"
																									 */);
					return;
				}

				if (iFlag == 3) {

					sBillCode = voBill.getHeaderVO().getVbillcode();
					// sFilePath = sFilePathDir + "\\" + "Icbill" + sBillCode
					// + ".xml";
					onExportXML(new GeneralBillVO[] {
						voBill
					}, sFilePathDir);
					showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000120")/*
																													* @res
																													* "导出完成"
																													*/);
					return;
				}

				// 得到单据号，公司
				sBillCode = voBill.getParentVO().getAttributeValue("vbillcode") == null ? "" : voBill.getParentVO().getAttributeValue("vbillcode").toString();
				// sPKCorp = voBill.getParentVO().getAttributeValue("pk_corp")
				// == null ? ""
				// : voBill.getParentVO().getAttributeValue("pk_corp")
				// .toString();
				sBillTypeCode = voBill.getBillTypeCode();
				sBillTypeName = GeneralBillVO.getBillTypeName(sBillTypeCode);

				tvos = (GeneralBillItemVO[]) voBill.getChildrenVO();
				// 需要将条码的信息一起导出
				Vector v = new Vector();
				ExcelFileVO vo = null;

				BarCodeVO[] bvos = null;
				//
				for (int i = 0; i < tvos.length; i++) {
					bvos = tvos[i].getBarCodeVOs();

					// if (tvos[i].getBarcodeManagerflag().booleanValue() ==
					// false)
					// continue;
					//
					// if (tvos[i].getBarcodeClose() == null
					// || tvos[i].getBarcodeClose().booleanValue() == true)
					// continue;

					if (bvos != null && bvos.length > 0) {
						for (int j = 0; j < bvos.length; j++) {
							vo = new ExcelFileVO();
							// 单据号信息
							vo.setAttributeValue("billcode", sBillCode); // 单据号
							// 单据行信息
							vo.setAttributeValue("rowno", tvos[i].getCrowno()); // 行号
							vo.setAttributeValue("inventorycode", tvos[i].getCinventorycode()); // 存货编码
							vo.setAttributeValue("inventoryname", tvos[i].getInvname()); // 存货名称
							vo.setAttributeValue("billtypecode", sBillTypeCode);
							vo.setAttributeValue("billtypename", sBillTypeName);

							UFDouble dshouldin = null; // 应收
							UFDouble dshouldout = null; // 应发
							UFDouble dShouldnum = null;

							UFDouble din = null; // 实收
							UFDouble dout = null; // 实发
							UFDouble dnum = null; // 要导出的数量

							dshouldin = tvos[i].getNshouldinnum();
							dshouldout = tvos[i].getNshouldoutnum();
							if (dshouldin == null && dshouldout == null) {
								dShouldnum = null;
							} else {
								dShouldnum = dshouldin == null ? dshouldout : dshouldin;
							}

							din = tvos[i].getNinnum();
							dout = tvos[i].getNoutnum();
							if (din == null && dout == null) {
								dnum = null;
							} else {
								dnum = din == null ? dout : din;
							}
							if (dShouldnum != null) vo.setAttributeValue("shouldnum", dShouldnum.toString()); // 存货数量（实收数量）
							if (dnum != null) vo.setAttributeValue("nnum", dnum.toString()); // 存货数量（实收数量）
							vo.setAttributeValue("free", tvos[i].getVfree0()); // 自由项目
							vo.setAttributeValue("batchcode", tvos[i].getVbatchcode()); // 批次号
							vo.setAttributeValue("cgeneralbid", tvos[i].getCgeneralbid()); // 表体pk
							vo.setAttributeValue("cgeneralhid", tvos[i].getCgeneralhid()); // 表头pk
							// 条码信息
							vo.setBarcode(bvos[j].getVbarcode()); // 主条码
							vo.setBarcodesub(bvos[j].getVbarcodesub()); // 次条码
							vo.setPackcode(bvos[j].getVpackcode()); // 箱条码

							v.add(vo);

						}
					} else {

						vo = new ExcelFileVO();
						// 单据号信息
						vo.setAttributeValue("billcode", sBillCode); // 单据号
						// 单据行信息
						vo.setAttributeValue("rowno", tvos[i].getCrowno()); // 行号
						vo.setAttributeValue("inventorycode", tvos[i].getCinventorycode()); // 存货编码
						vo.setAttributeValue("inventoryname", tvos[i].getInvname()); // 存货名称
						vo.setAttributeValue("billtypecode", sBillTypeCode);
						vo.setAttributeValue("billtypename", sBillTypeName);

						UFDouble dshouldin = null; // 应收
						UFDouble dshouldout = null; // 应发
						UFDouble dShouldnum = null;

						UFDouble din = null; // 实收
						UFDouble dout = null; // 实发
						UFDouble dnum = null; // 要导出的数量

						dshouldin = tvos[i].getNshouldinnum();
						dshouldout = tvos[i].getNshouldoutnum();
						if (dshouldin == null && dshouldout == null) {
							dShouldnum = null;
						} else {
							dShouldnum = dshouldin == null ? dshouldout : dshouldin;
						}

						din = tvos[i].getNinnum();
						dout = tvos[i].getNoutnum();
						if (din == null && dout == null) {
							dnum = null;
						} else {
							dnum = din == null ? dout : din;
						}
						if (dShouldnum != null) vo.setAttributeValue("shouldnum", dShouldnum.toString()); // 存货数量（实收数量）
						if (dnum != null) vo.setAttributeValue("nnum", dnum.toString()); // 存货数量（实收数量）
						vo.setAttributeValue("free", tvos[i].getVfree0()); // 自由项目
						vo.setAttributeValue("batchcode", tvos[i].getVbatchcode()); // 批次号
						vo.setAttributeValue("cgeneralbid", tvos[i].getCgeneralbid()); // 表体pk
						vo.setAttributeValue("cgeneralhid", tvos[i].getCgeneralhid()); // 表头pk
						v.add(vo);
					}

					// 已经得到vo，然后导出vo
					vos = new ExcelFileVO[v.size()];
					v.copyInto(vos);
					// 调用导出接口
					// 文件名称规则：Icbill+公司PK+单据号+".xls"
					// sFilePath = sFilePathDir;
					ExcelReadCtrl erc = new ExcelReadCtrl();
					erc.setVOToExcel(vos, sFilePathDir);
					// 写状态
					ExcelReadCtrl erc1 = new ExcelReadCtrl(sFilePathDir, true);
					// 写状态
					erc1.setExcelFileFlag(nc.ui.ic.pub.barcodeoffline.IExcelFileFlag.F_NEW);
					showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000120")/*
																													* @res
																													* "导出完成"
																													*/);
				}
			} else if (m_iCurPanel == BillMode.List) { // 列表
				if (null == m_alListData || m_alListData.size() == 0) {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000049")/*
																													* @res
																													* "请先查询或录入单据。"
																													*/);
					return;
				}

				ArrayList alBill = getSelectedBills();

				if (alBill == null || alBill.size() <= 0 || alBill.get(0) == null) {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH003")/*
																									 * @res "请选择要处理的数据！"
																									 */);
					return;
				}

				if (iFlag == 3) {
					sBillCode = ((GeneralBillVO) alBill.get(0)).getHeaderVO().getVbillcode();
					onExportXML((GeneralBillVO[]) alBill.toArray(new GeneralBillVO[alBill.size()]), sFilePathDir);
					showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000120")/*
																													* @res
																													* "导出完成"
																													*/);
					return;
				}

				for (int k = 0; k < alBill.size(); k++) {
					voBill = (GeneralBillVO) alBill.get(k);
					// 得到单据号，公司
					sBillCode = voBill.getParentVO().getAttributeValue("vbillcode") == null ? "" : voBill.getParentVO().getAttributeValue("vbillcode").toString();
					// sPKCorp =
					// voBill.getParentVO().getAttributeValue("pk_corp") == null
					// ? ""
					// : voBill.getParentVO().getAttributeValue("pk_corp")
					// .toString();

					sBillTypeCode = voBill.getBillTypeCode();
					sBillTypeName = GeneralBillVO.getBillTypeName(sBillTypeCode);

					// 表体vo
					tvos = (GeneralBillItemVO[]) voBill.getChildrenVO();
					// 需要将条码的信息一起导出
					Vector v = new Vector();
					ExcelFileVO vo = null;

					BarCodeVO[] bvos = null;

					for (int i = 0; i < tvos.length; i++) {

						// if (tvos[i].getBarcodeManagerflag().booleanValue() ==
						// false)
						// continue;
						// if (tvos[i].getBarcodeClose().booleanValue() == true)
						// continue;

						bvos = tvos[i].getBarCodeVOs();
						if (bvos != null && bvos.length > 0) {
							for (int j = 0; j < bvos.length; j++) {
								vo = new ExcelFileVO();
								vo = new ExcelFileVO();
								// 单据号信息
								vo.setAttributeValue("billcode", sBillCode); // 单据号
								// 单据号信息
								vo.setAttributeValue("billcode", sBillCode); // 单据号
								// 单据行信息
								vo.setAttributeValue("rowno", tvos[i].getCrowno()); // 行号
								vo.setAttributeValue("inventorycode", tvos[i].getCinventorycode()); // 存货编码
								vo.setAttributeValue("inventoryname", tvos[i].getInvname()); // 存货名称
								vo.setAttributeValue("billtypecode", sBillTypeCode);
								vo.setAttributeValue("billtypename", sBillTypeName);

								UFDouble dshouldin = null; // 应收
								UFDouble dshouldout = null; // 应发
								UFDouble dShouldnum = null;

								UFDouble din = null; // 实收
								UFDouble dout = null; // 实发
								UFDouble dnum = null; // 要导出的数量

								dshouldin = tvos[i].getNshouldinnum();
								dshouldout = tvos[i].getNshouldoutnum();
								if (dshouldin == null && dshouldout == null) {
									dShouldnum = null;
								} else {
									dShouldnum = dshouldin == null ? dshouldout : dshouldin;
								}

								din = tvos[i].getNinnum();
								dout = tvos[i].getNoutnum();
								if (din == null && dout == null) {
									dnum = null;
								} else {
									dnum = din == null ? dout : din;
								}
								if (dShouldnum != null) vo.setAttributeValue("shouldnum", dShouldnum.toString()); // 存货数量（实收数量）
								if (dnum != null) vo.setAttributeValue("nnum", dnum.toString()); // 存货数量（实收数量）
								vo.setAttributeValue("free", tvos[i].getVfree0()); // 自由项目
								vo.setAttributeValue("batchcode", tvos[i].getVbatchcode()); // 批次号
								vo.setAttributeValue("cgeneralbid", tvos[i].getCgeneralbid()); // 表体pk
								vo.setAttributeValue("cgeneralhid", tvos[i].getCgeneralhid()); // 表头pk
								// 表头pk //条码信息
								vo.setBarcode(bvos[j].getVbarcode()); // 主条码
								vo.setBarcodesub(bvos[j].getVbarcodesub()); // 次条码
								vo.setPackcode(bvos[j].getVpackcode()); // 箱条码

								v.add(vo);
							}
						} else {
							vo = new ExcelFileVO();
							vo = new ExcelFileVO();
							// 单据号信息
							vo.setAttributeValue("billcode", sBillCode); // 单据号
							// 单据行信息
							vo.setAttributeValue("rowno", tvos[i].getCrowno()); // 行号
							vo.setAttributeValue("inventorycode", tvos[i].getCinventorycode()); // 存货编码
							vo.setAttributeValue("inventoryname", tvos[i].getInvname()); // 存货名称
							vo.setAttributeValue("billtypecode", sBillTypeCode);
							vo.setAttributeValue("billtypename", sBillTypeName);

							UFDouble dshouldin = null; // 应收
							UFDouble dshouldout = null; // 应发
							UFDouble dShouldnum = null;

							UFDouble din = null; // 实收
							UFDouble dout = null; // 实发
							UFDouble dnum = null; // 要导出的数量

							dshouldin = tvos[i].getNshouldinnum();
							dshouldout = tvos[i].getNshouldoutnum();
							if (dshouldin == null && dshouldout == null) {
								dShouldnum = null;
							} else {
								dShouldnum = dshouldin == null ? dshouldout : dshouldin;
							}

							din = tvos[i].getNinnum();
							dout = tvos[i].getNoutnum();
							if (din == null && dout == null) {
								dnum = null;
							} else {
								dnum = din == null ? dout : din;
							}
							if (dShouldnum != null) vo.setAttributeValue("shouldnum", dShouldnum.toString()); // 存货数量（实收数量）
							if (dnum != null) vo.setAttributeValue("nnum", dnum.toString()); // 存货数量（实收数量）
							vo.setAttributeValue("free", tvos[i].getVfree0()); // 自由项目
							vo.setAttributeValue("batchcode", tvos[i].getVbatchcode()); // 批次号
							vo.setAttributeValue("cgeneralbid", tvos[i].getCgeneralbid()); // 表体pk
							vo.setAttributeValue("cgeneralhid", tvos[i].getCgeneralhid()); // 表头pk
							v.add(vo);
						}
					}
					// 每张单据将生成一个文件
					// 已经得到vo，然后导出vo
					vos = new ExcelFileVO[v.size()];
					v.copyInto(vos);
					// 调用导出接口
					// 文件名称规则：Icbill+公司PK+单据号+".xls"
					// sFilePath = sFilePathDir + "\\" + "Icbill" + sBillCode
					// + ".xls";
					ExcelReadCtrl erc = new ExcelReadCtrl();
					erc.setVOToExcel(vos, sFilePathDir);
					// 写状态
					ExcelReadCtrl erc1 = new ExcelReadCtrl(sFilePathDir, true);
					erc1.setExcelFileFlag(nc.ui.ic.pub.barcodeoffline.IExcelFileFlag.F_NEW);
				}
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000120")/* @res "导出完成" */);
				showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000120")/* @res "导出完成" */);
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

	/**
	 * 创建者：李俊 功能：（单据审核后）保存导入条码
	 */
	public int onImportSignedBillBarcode(GeneralBillVO voUpdatedBill, boolean bCheckNum) throws Exception {

		try {
			// 得到数据错误，出错 ------------ EXIT -------------------
			nc.vo.ic.pub.bill.Timer timer = new nc.vo.ic.pub.bill.Timer();
			timer.start("@@修改保存单据开始：");
			if (voUpdatedBill == null || voUpdatedBill.getParentVO() == null || voUpdatedBill.getChildrenVO() == null) {
				SCMEnv.out("Bill is null !");
				throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000055")/*
																													* @res
																													* "单据为空！"
																													*/);
			}
			if (bCheckNum) {
				String sMsg = BarcodeparseCtrl.checkNumWithBarNum(voUpdatedBill.getItemVOs(), true);
				if (sMsg != null) {
					MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPPSCMCommon-000059")/* @res "错误" */, sMsg);
					return 0;
				}

			}

			// 设置单据类型
			voUpdatedBill.setHeaderValue("cbilltypecode", m_sBillTypeCode);
			// 05/07设置制单人为当前操作员
			// remark by zhx onSave() set coperatorid into VO
			// voUpdatedBill.setHeaderValue("coperatorid", m_sUserID);
			timer.showExecuteTime("@@设置单据类型：");
			GeneralBillVO voBill = (GeneralBillVO) m_voBill.clone();
			timer.showExecuteTime("@@m_voBill.clone()：");
			// ydy 0826
			// 把voUpdatedBill上的条码VO放到voBill中
			setBarCodeOnUI(voBill, (GeneralBillItemVO[]) voUpdatedBill.getChildrenVO());
			// voBill.setIDItems(voUpdatedBill);

			GeneralBillHeaderVO voHead = voBill.getHeaderVO();
			// 签字人
			voHead.setCregister(m_sUserID);
			// --------------------------------------------可以不是当前登录单位的单据，所以不需要修改单位。
			voHead.setPk_corp(m_sCorpID);
			// 因为登录日期和单据日期是可以不同的，所以必须要登录日期。
			voHead.setDaccountdate(new nc.vo.pub.lang.UFDate(m_sLogDate));
			// vo可能要传给平台，所以要做成和签字后的单据
			voHead.setFbillflag(new Integer(nc.vo.ic.pub.bill.BillStatus.SIGNED));
			voHead.setCoperatoridnow(m_sUserID); // 当前操作员2002-04-10.wnj
			voHead.setAttributeValue("clogdatenow", m_sLogDate); // 当前登录日期2003-01-05
			// clear audit info
			voHead.setCauditorid(null);
			voHead.setDauditdate(null);

			voUpdatedBill.setAccreditUserID(m_sUserID);

			ArrayList alRetData = new ArrayList();
			try {
				alRetData = onImportSignedBillBarcodeKernel(voBill, voUpdatedBill);
			} catch (RightcheckException e) {
				showErrorMessage(e.getMessage() + nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000069")/*
																																* @res
																																* ".\n权限校验不通过,保存失败! "
																																*/);
				getAccreditLoginDialog().setCorpID(m_sCorpID);
				getAccreditLoginDialog().clearPassWord();
				if (getAccreditLoginDialog().showModal() == nc.ui.pub.beans.UIDialog.ID_OK) {
					String sUserID = getAccreditLoginDialog().getUserID();
					if (sUserID == null) {
						throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000070")/*
																															* @res
																															* "权限校验不通过,保存失败. "
																															*/);
					} else {
						voUpdatedBill.setAccreditUserID(sUserID);
						alRetData = onImportSignedBillBarcodeKernel(voBill, voUpdatedBill);
					}
				} else {
					throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000070")/*
																														* @res
																														* "权限校验不通过,保存失败. "
																														*/);

				}
			}

			if (alRetData == null || alRetData.size() < 0) {
				SCMEnv.out("return data error.");
				throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000056")/*
																													* @res
																													* "单据已经保存，但返回值错误，请重新查询单据。"
																													*/);
			}
			// 0 ---- 显示提示信息
			if (alRetData.get(0) != null && alRetData.get(0).toString().trim().length() > 0) showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000122")/* @res "条码保存成功。" */
					+ (String) alRetData.get(0));
			else showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000122")/* @res "条码保存成功。" */);

			// ###################################################
			// 重新设置单据表头ts
			SMGeneralBillVO smbillvo = null;
			smbillvo = (SMGeneralBillVO) alRetData.get(2);
			String sHeaderTs = smbillvo.getHeaderVO().getTs();
			String sTsKey = "ts";
			nc.ui.pub.bill.BillItem billItem = getBillCardPanel().getHeadItem(sTsKey);
			if (billItem != null) {
				getBillCardPanel().setHeadItem(sTsKey, sHeaderTs);
			}
			m_voBill.getHeaderVO().setTs(sHeaderTs);

			m_voBill.setSmallBillVO(smbillvo);
			voUpdatedBill.setSmallBillVO(smbillvo);
			// ###################################################
			// 更新条码状态
			setBillBCVOStatus(m_voBill, nc.vo.pub.VOStatus.UNCHANGED);
			// hanwei 2004-0916
			setBillBCVOStatus(voUpdatedBill, nc.vo.pub.VOStatus.UNCHANGED);

			// 添加此方法，避免条码VO为空后，没有清空m_voBill对应的条码VO
			m_voBill.setIDClearBarcodeItems(voUpdatedBill);

			// 更新界面时间ts
			getGenBillUICtl().setBillCardPanelData(getBillCardPanel(), smbillvo);

			timer.showExecuteTime(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000123")/*
																												* @res
																												* "@@m_voBill.setIDItems(voUpdatedBill)："
																												*/);

			// 刷新列表数据
			updateBillToList(m_voBill);
			timer.showExecuteTime(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000124")/*
																												* @res
																												* "@@刷新列表数据updateBillToList(m_voBill)："
																												*/);

		} catch (java.net.ConnectException ex1) {
			SCMEnv.out(ex1.getMessage());
			if (showYesNoMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000104")/*
																												* @res
																												* "当前网络中断，是否将单据信息保存到默认目录："
																												*/
			) == MessageDialog.ID_YES) {
				onBillExcel(1); // 保存单据信息到默认目录
			}
		} catch (Exception e) {
			// 异常必须抛出，由主流程处理。因为它影响主流程。
			throw e;

		}
		return 1;
	}

	/**
	 * ?user> 功能：将条码管理的单据行的条码设置到BillVO上 参数： 返回： 例外： 日期：(2004-8-24 14:02:09)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @param billVO
	 *            nc.vo.ic.pub.bill.GeneralBillVO
	 * @param voaBarcodes
	 *            nc.vo.ic.pub.bill.GeneralBillItemVO[]
	 */
	public void setBarCodeOnUI(GeneralBillVO billVO, GeneralBillItemVO[] voaItems) {

		if (voaItems.length <= 0) return;
		Hashtable htbItemBarcodeVos = getBarcodeCtrl().getHtbItemBarcodeVos(voaItems);

		GeneralBillItemVO[] billItemsAll = (GeneralBillItemVO[]) billVO.getChildrenVO();
		if (billItemsAll.length <= 0) return;

		if (htbItemBarcodeVos != null && htbItemBarcodeVos.size() > 0) {

			GeneralBillItemVO billItemTemp = null;
			if (htbItemBarcodeVos != null) {

				String sRowNo = null;
				for (int i = 0; i < billItemsAll.length; i++) {
					sRowNo = billItemsAll[i].getCrowno();
					if (sRowNo != null && htbItemBarcodeVos.containsKey(sRowNo)) {
						billItemTemp = (GeneralBillItemVO) htbItemBarcodeVos.get(sRowNo);
						billItemsAll[i].setBarCodeVOs(billItemTemp.getBarCodeVOs());
					}

				}

			}

		}

	}

	// 条码数量字段
	protected String m_sNumbarcodeItemKey = "nbarcodenum";

	/**
	 * 创建者：仲瑞庆 功能： 参数： 返回： 例外： 日期：(2001-10-30 15:06:35) 修改日期，修改人，修改原因，注释标志：
	 */
	protected PrintDataInterface getDataSourceNew() {
		// if (null == m_dataSource) {
		PrintDataInterface ds = new PrintDataInterface();
		BillData bd = getBillCardPanel().getBillData();
		ds.setBillData(bd);
		ds.setModuleName(m_sCurrentBillNode);
		ds.setTotalLinesInOnePage(getPrintEntry().getBreakPos());
		// }
		return ds;
	}

	/**
	 * 鼠标双击事件 创建日期：(2001-6-20 17:19:03)
	 */
	public void mouse_doubleclick(nc.ui.pub.bill.BillMouseEnent e) {
		if (e.getPos() == BillItem.HEAD) {

			onSwitch();
		}

	}

	/**
	 * 李俊 功能：执行条码关闭 参数： 返回： 例外： 日期：(2004-10-18 10:37:47) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @param iChoose
	 *            int
	 */
	public void onBarcodeOpenClose(int iChoose) {

		int[] iaSel = null;
		// get the selection of card or list
		if (BillMode.List == m_iCurPanel) {
			showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000125")/* @res "请在卡片模式下关闭打开条码" */);
			return;
		} else {
			iaSel = getBillCardPanel().getBillTable().getSelectedRows();
		}
		if (m_iMode == BillMode.New) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000126")/* @res "新增状态不能关闭打开条码！" */);
		}
		BarcodeCloseCtrl ctrl = new BarcodeCloseCtrl(this);

		UFBoolean ufBTrue = new UFBoolean(true);
		UFBoolean ufBFalse = new UFBoolean(false);
		UFBoolean ufBChoose = null;

		ArrayList alParam = null;
		if (iChoose == 0) {
			ufBChoose = ufBTrue;
			alParam = ctrl.openCloseBC(m_voBill, iaSel, ufBTrue);
		} else if (iChoose == 1) {
			ufBChoose = ufBFalse;
			alParam = ctrl.openCloseBC(m_voBill, iaSel, ufBFalse);
		}
		if (alParam == null) return;

		// fresh the voItems to UI

		GeneralBillItemVO[] voaItemBill = (GeneralBillItemVO[]) m_voBill.getChildrenVO();
		int sizeBill = voaItemBill.length;
		if (sizeBill == 0) return;

		ArrayList alBids = (ArrayList) alParam.get(1);
		ArrayList alTss = (ArrayList) alParam.get(2);
		// add to hash
		String sBIdsTemp = null;
		java.util.Hashtable htBids = new java.util.Hashtable();
		for (int i = 0; i < alBids.size(); i++) {
			sBIdsTemp = (String) alBids.get(i);
			if (sBIdsTemp == null) continue;
			htBids.put(sBIdsTemp, alTss.get(i));
		}

		// fresh m_voBill's ts
		// GeneralBillItemVO voItemTemp = null;
		String sBodyPKTemp = null;
		for (int m = 0; m < sizeBill; m++) {
			sBodyPKTemp = voaItemBill[m].getCgeneralbid();
			if (htBids.containsKey(sBodyPKTemp)) {
				voaItemBill[m].setTs((String) htBids.get(sBodyPKTemp));
				voaItemBill[m].setBarcodeClose(ufBChoose);
			}
		}
		// fresh the billModel's ts
		nc.ui.pub.bill.BillModel bm = getBillCardPanel().getBillModel();
		int iCount = bm.getRowCount();
		String strValue = null;
		for (int j = 0; j < iCount; j++) {

			strValue = (String) bm.getValueAt(j, "cgeneralbid");
			if (strValue == null) continue;
			if (htBids.containsKey(strValue)) {
				bm.setValueAt(htBids.get(strValue), j, "ts");
				bm.setValueAt(ufBChoose, j, "bbarcodeclose");
			}
		}

	}

	/*
	 * 增加一个内部类. 继承IFreshTsListener. 实现打印后对ts及printcount的更新. @author 邵兵 创建时间:
	 * 2004-12-23
	 */
	public class FreshTsListener implements IFreshTsListener {

		/*
		 * （非 Javadoc）
		 * 
		 * @see nc.ui.scm.print.IFreshTsListener#freshTs(java.lang.String,
		 * java.lang.String)
		 */
		public void freshTs(String sBillID, String sTS, Integer iPrintCount) {
			// fresh local TS with voPr.getNewTs();

			SCMEnv.out("new Ts = " + sTS);
			SCMEnv.out("new iPrintCount = " + iPrintCount);

			if (m_alListData == null || m_alListData.size() == 0) return;

			// 判断打印的vo是否仍在缓存中．
			// 在打印预览状态打印时，缓存vo可能会有改变，故需要判断．
			int index = 0;
			GeneralBillVO voBill = null;
			GeneralBillHeaderVO headerVO = null;
			for (; index < m_alListData.size(); index++) {
				voBill = (GeneralBillVO) m_alListData.get(index);
				headerVO = voBill.getHeaderVO();

				// 在sBillID传入时，已经判断sBillID不为null.
				if (sBillID.equals(headerVO.getPrimaryKey())) break;
			}

			if (index == m_alListData.size()) // 不在缓存vo中，无需进行更新．
			return;

			// 在缓存vo中
			headerVO.setAttributeValue("ts", sTS);
			headerVO.setAttributeValue("iprintcount", iPrintCount);

			if (m_iCurPanel == BillMode.Card) { // Card
				if (index == m_iCurDispBillNum) { // 如果为当前card显示vo.
					getBillCardPanel().setHeadItem("ts", sTS);
					getBillCardPanel().setTailItem("iprintcount", iPrintCount);
				}
			} else { // List
				int iPrintColumn = getBillListPanel().getHeadBillModel().getBodyColByKey("ts");
				getBillListPanel().getHeadBillModel().setValueAt(sTS, index, iPrintColumn);
				iPrintColumn = getBillListPanel().getHeadBillModel().getBodyColByKey("iprintcount");
				getBillListPanel().getHeadBillModel().setValueAt(iPrintCount, index, iPrintColumn);
			}
		}
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.ui.scm.pub.bill.IBillExtendFun#getExtendBtns()
	 */
	public ButtonObject[] getExtendBtns() {
		return null;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see
	 * nc.ui.scm.pub.bill.IBillExtendFun#onExtendBtnsClick(nc.ui.pub.ButtonObject
	 * )
	 */
	public void onExtendBtnsClick(ButtonObject bo) {

	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.ui.scm.pub.bill.IBillExtendFun#setExtendBtnsStat(int)
	 * BillMode.New 单据新增 BillMode.Browse 浏览 BillMode.Update 修改
	 */
	public void setExtendBtnsStat(int iState) {

	}

	private boolean isExistInBatch(String pk_invmandoc, String vbatchcode) {
		BatchcodeVO vos = getBCVO(pk_invmandoc, vbatchcode);
		if (vos == null) return false;
		else return true;
	}

	private BatchcodeVO getBCVO(String pk_invmandoc, String vbatchcode) {
		ConditionVO[] voCons = new ConditionVO[2];
		voCons[0] = new ConditionVO();
		voCons[0].setFieldCode("pk_invbasdoc");
		voCons[0].setValue(pk_invmandoc);
		voCons[0].setLogic(true);
		voCons[0].setOperaCode("=");

		voCons[1] = new ConditionVO();
		voCons[1].setFieldCode("vbatchcode");
		voCons[1].setValue(vbatchcode);
		voCons[1].setLogic(true);
		voCons[1].setOperaCode("=");
		BatchcodeVO[] vos = null;
		try {
			vos = BatchcodeHelper.queryBatchcode(voCons, m_sCorpID);
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
		}
		return (vos == null || vos.length == 0) ? null : vos[0];
	}

	/**
	 * get 创建日期：(2001-10-26 14:31:14)
	 * 
	 * @param key
	 *            java.lang.String
	 */
	protected ClientUISortCtl getListHeadSortCtl() {
		return m_listHeadSortCtl;
	}

	/**
	 * get 创建日期：(2001-10-26 14:31:14)
	 * 
	 * @param key
	 *            java.lang.String
	 */
	protected ClientUISortCtl getListBodySortCtl() {
		return m_listBodySortCtl;
	}

	/**
	 * get 创建日期：(2001-10-26 14:31:14)
	 * 
	 * @param key
	 *            java.lang.String
	 */
	protected ClientUISortCtl getCardBodySortCtl() {
		return m_cardBodySortCtl;
	}

	/**
	 * 排序后触发。 创建日期：(2001-10-26 14:31:14)
	 * 
	 * @param key
	 *            java.lang.String
	 */
	public void afterSortEvent(boolean iscard, boolean ishead, String key) {
		if (ishead) {
			m_alListData = (ArrayList) getListHeadSortCtl().getRelaSortData(0);
		} else {
			if (iscard) {
				if (m_voBill != null) {
					GeneralBillItemVO[] itemvos = (GeneralBillItemVO[]) getCardBodySortCtl().getRelaSortDataAsArray(0);
					m_voBill.setChildrenVO(itemvos);
					if (m_iMode != BillMode.New && m_iMode != BillMode.QuickNew && m_iMode != BillMode.Update && m_alListData != null && m_alListData.size() > m_iLastSelListHeadRow) {
						if (m_voBill.getHeaderVO().getCgeneralhid() != null && m_voBill.getHeaderVO().getCgeneralhid().equals(((GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow)).getHeaderVO().getCgeneralhid())) try {
							((GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow)).setChildrenVO((GeneralBillItemVO[]) ObjectUtils.serializableClone(itemvos));
						} catch (Exception e) {
							nc.vo.scm.pub.SCMEnv.error(e);
						}
					}
				}
				if (m_alLocatorData != null && m_alLocatorData.size() > 0) m_alLocatorData = (ArrayList) getCardBodySortCtl().getRelaSortData(1);
				if (m_alSerialData != null && m_alSerialData.size() > 0) m_alSerialData = (ArrayList) getCardBodySortCtl().getRelaSortData(2);
				if (m_alLocatorDataBackup != null && m_alLocatorDataBackup.size() > 0) m_alLocatorDataBackup = (ArrayList) getCardBodySortCtl().getRelaSortData(3);
				if (m_alSerialDataBackup != null && m_alSerialDataBackup.size() > 0) m_alSerialDataBackup = (ArrayList) getCardBodySortCtl().getRelaSortData(4);
			} else {
				if (m_alListData != null && m_alListData.size() > 0) {
					GeneralBillItemVO[] itemvos = (GeneralBillItemVO[]) getListBodySortCtl().getRelaSortDataAsArray(0);
					((GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow)).setChildrenVO(itemvos);
					if (m_voBill != null && m_voBill.getHeaderVO().getCgeneralhid() != null && m_voBill.getHeaderVO().getCgeneralhid().equals(((GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow)).getHeaderVO().getCgeneralhid())) {
						try {
							m_voBill.setChildrenVO((GeneralBillItemVO[]) ObjectUtils.serializableClone(itemvos));
						} catch (Exception e) {
							nc.vo.scm.pub.SCMEnv.error(e);
						}
					}
				}

				if (m_alLocatorData != null && m_alLocatorData.size() > 0) m_alLocatorData = (ArrayList) getListBodySortCtl().getRelaSortData(1);
				if (m_alSerialData != null && m_alSerialData.size() > 0) m_alSerialData = (ArrayList) getListBodySortCtl().getRelaSortData(2);
				if (m_alLocatorDataBackup != null && m_alLocatorDataBackup.size() > 0) m_alLocatorDataBackup = (ArrayList) getListBodySortCtl().getRelaSortData(3);
				if (m_alSerialDataBackup != null && m_alSerialDataBackup.size() > 0) m_alSerialDataBackup = (ArrayList) getListBodySortCtl().getRelaSortData(4);
			}
		}
		m_sLastKey = key;
	}

	/**
	 * 排序前触发。 创建日期：(2001-10-26 14:31:14)
	 * 
	 * @param key
	 *            java.lang.String
	 */
	public void beforeSortEvent(boolean iscard, boolean ishead, String key) {

		clearOrientColor();
		// 如果是表头排序
		if (ishead) {
			SCMEnv.out("表头排序");
			if (m_alListData == null || m_alListData.size() <= 0) {
				// 说明没有排序的必要
				return;
			}
			getListHeadSortCtl().addRelaSortData(m_alListData);

		} else {
			SCMEnv.out("表体排序");

			if (iscard) {
				if (m_voBill != null) getCardBodySortCtl().addRelaSortData(m_voBill.getItemVOs());
				if (m_alLocatorData != null && m_alLocatorData.size() > 0) getCardBodySortCtl().addRelaSortData(m_alLocatorData);
				if (m_alSerialData != null && m_alSerialData.size() > 0) getCardBodySortCtl().addRelaSortData(m_alSerialData);
				if (m_alLocatorDataBackup != null && m_alLocatorDataBackup.size() > 0) getCardBodySortCtl().addRelaSortData(m_alLocatorDataBackup);
				if (m_alSerialDataBackup != null && m_alSerialDataBackup.size() > 0) getCardBodySortCtl().addRelaSortData(m_alSerialDataBackup);
			} else {
				if (m_alListData != null && m_alListData.size() > 0) getListBodySortCtl().addRelaSortData(((GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow)).getItemVOs());
				if (m_alLocatorData != null && m_alLocatorData.size() > 0) getListBodySortCtl().addRelaSortData(m_alLocatorData);
				if (m_alSerialData != null && m_alSerialData.size() > 0) getListBodySortCtl().addRelaSortData(m_alSerialData);
				if (m_alLocatorDataBackup != null && m_alLocatorDataBackup.size() > 0) getListBodySortCtl().addRelaSortData(m_alLocatorDataBackup);
				if (m_alSerialDataBackup != null && m_alSerialDataBackup.size() > 0) getListBodySortCtl().addRelaSortData(m_alSerialDataBackup);
			}
		}
	}

	/**
	 * 列表表头排序后触发,当前行变化 创建日期：(2001-10-26 14:31:14)
	 * 
	 * @param key
	 *            java.lang.String
	 */
	public void currentRowChange(int newrow) {

		if (newrow >= 0) {
			if (m_iLastSelListHeadRow != newrow) {
				setLastHeadRow(newrow);
				selectListBill(m_iLastSelListHeadRow); // 表体
				setButtonStatus(true);
			}
		} else {
			if (m_iLastSelListHeadRow < 0 || m_iLastSelListHeadRow >= getBillListPanel().getHeadBillModel().getRowCount()) m_iLastSelListHeadRow = 0;
			selectListBill(m_iLastSelListHeadRow); // 表体
			setButtonStatus(true);
		}
	}

	/**
	 * 功能描述:退出系统
	 */
	public boolean onClosing() {
		// 正在编辑单据时退出提示
		if (m_iMode != BillMode.Browse) {

			int iret = MessageDialog.showYesNoCancelDlg(this, null, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH001")/* @res "是否保存已修改的数据？" */);
			if (iret == MessageDialog.ID_YES) {
				try {
					boolean isok = onSave();
					if (!isok) {

					return false; }
				} catch (Exception e) {

					return false;
				}
				return true;
			} else if (iret == MessageDialog.ID_NO) {

				return true;
			} else return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nc.ui.pub.linkoperate.ILinkQuery#doQueryAction(nc.ui.pub.linkoperate.
	 * ILinkQueryData)
	 */
	public void doQueryAction(ILinkQueryData querydata) {
		queryForLinkOper(querydata.getPkOrg(), querydata.getBillType(), querydata.getBillID());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nc.ui.pub.linkoperate.ILinkQuery#doQueryAction(nc.ui.pub.linkoperate.
	 * ILinkQueryData)
	 */
	public void queryForLinkOper(String PkOrg, String billtype, String billid) {
		// 查单据
		ICDataSet datas = nc.ui.ic.pub.tools.GenMethod.queryData("ic_general_h", "cgeneralhid", new String[] {
			"pk_corp"
		}, new int[] {
			SmartFieldMeta.JAVATYPE_STRING
		}, new String[] {
			billid
		}, " dr=0 ");
		String cbillpkcorp = datas == null ? null : (String) datas.getValueAt(0, 0);

		if (cbillpkcorp == null || cbillpkcorp.trim().length() <= 0) nc.ui.pub.beans.MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/* @res "提示" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000062")/* @res "没有符合查询条件的单据！" */);
		else {
			QueryConditionDlgForBill qrydlg = new QueryConditionDlgForBill(this);
			qrydlg.setTempletID(cbillpkcorp, m_sCurrentBillNode, m_sUserID, null);
			String[] refcodes = null;
			if (BillTypeConst.m_allocationOut.equals(billtype) || BillTypeConst.m_allocationIn.equals(billtype)) refcodes = nc.ui.ic.pub.tools.GenMethod.getDataPowerFieldFromDlg(qrydlg, false, new String[] {
					"head.cothercorpid", "head.coutcorpid", "body.creceieveid", "head.cothercalbodyid", "head.cotherwhid", "head.coutcalbodyid"
			});
			else refcodes = nc.ui.ic.pub.tools.GenMethod.getDataPowerFieldFromDlg(qrydlg, false, null);

			qrydlg.setCorpRefs("head.pk_corp", refcodes);
			ConditionVO[] convos = null;
			if (getClientEnvironment().getCorporation().getPrimaryKey().equals(cbillpkcorp)) {
				convos = ICCommonBusi.getDataPowerConsFromDlg(qrydlg, m_sCurrentBillNode, cbillpkcorp, m_sUserID, refcodes);
				// 处理跨公司部门业务员条件
				convos = nc.ui.ic.pub.tools.GenMethod.procMultCorpDeptBizDP(convos, billtype, cbillpkcorp);
			}
			GeneralBillVO voBill = qryBill(cbillpkcorp, billtype, null, m_sUserID, billid, convos);
			if (voBill == null) {
				nc.ui.pub.beans.MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/*
																																				 * @res "提示"
																																				 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000062")/*
																																																									* @res "没有符合查询条件的单据！"
																																																									*/);
				return;
			}

			// 初始化界面
			m_alListData = new ArrayList();
			m_alListData.add(voBill);

			ArrayList alListData = new ArrayList();
			alListData.add(voBill);
			setDataOnList(alListData, true);
			onSwitch();
			if (!getClientEnvironment().getCorporation().getPrimaryKey().equals(cbillpkcorp)) setButtons(new ButtonObject[] {});
			// setButtons(new
			// ButtonObject[]{getButtonTree().getButton(ICButtonConst.BTN_ASSIST_FUNC_DOCUMENT)});

		}
	}

	/**
	 * UI关联操作-新增
	 * 
	 * @author leijun 2006-5-24
	 */
	public void doAddAction(ILinkAddData adddata) {
		if (adddata == null) return;
		GeneralBillVO[] billvos = getBillVOs(adddata);
		if (billvos == null || billvos.length <= 0) return;

		try {
			// v5 lj 支持多张单据参照生成
			if (billvos.length == 1) {
				setRefBillsFlag(false);
				setBillRefResultVO(billvos[0].getHeaderVO().getCbiztypeid(), billvos);
			} else {
				setRefBillsFlag(true);
				setBillRefMultiVOs(billvos[0].getHeaderVO().getCbiztypeid(), billvos);
			}
			// end v5
		} catch (Exception e) {
			nc.ui.ic.pub.tools.GenMethod.handleException(null, null, e);
		}
	}

	/**
	 * UI关联操作-新增--获取源数据
	 * 
	 * @author leijun 2006-5-24
	 */
	protected GeneralBillVO[] getBillVOs(ILinkAddData adddata) {
		if (adddata == null) return null;
		String billtype = adddata.getSourceBillType();
		AggregatedValueObject[] srcvos = null;

		try {
			if (ScmConst.SO_Order.equals(billtype.trim())) {
				nc.itf.scm.so.so001.ISaleOrderQuery soquery = (nc.itf.scm.so.so001.ISaleOrderQuery) NCLocator.getInstance().lookup(nc.itf.scm.so.so001.ISaleOrderQuery.class.getName());
				AggregatedValueObject srcvo = soquery.querySourceBillVOForLinkAdd(adddata.getSourceBillID(), billtype, ClientEnvironment.getInstance().getCorporation().getPrimaryKey(), null);
				if (srcvo != null) {
					srcvos = (AggregatedValueObject[]) Array.newInstance(srcvo.getClass(), 1);
					srcvos[0] = srcvo;
				}
			} else if (ScmConst.SO_Invoice.equals(billtype.trim())) {
				nc.itf.scm.so.so002.ISaleinvoiceQuery soquery = (nc.itf.scm.so.so002.ISaleinvoiceQuery) NCLocator.getInstance().lookup(nc.itf.scm.so.so002.ISaleinvoiceQuery.class.getName());
				AggregatedValueObject srcvo = soquery.queryData(adddata.getSourceBillID());
				if (srcvo != null) {
					srcvos = (AggregatedValueObject[]) Array.newInstance(srcvo.getClass(), 1);
					srcvos[0] = srcvo;
				}

			} else if (ScmConst.PO_Order.equals(billtype.trim())) {
				nc.itf.po.outer.IQueryForIc qrypo = (nc.itf.po.outer.IQueryForIc) NCLocator.getInstance().lookup(nc.itf.po.outer.IQueryForIc.class.getName());
				srcvos = qrypo.querySourceBillVOForLinkAdd(adddata.getSourcePkOrg(), adddata.getSourceBillID(), billtype, ClientEnvironment.getInstance().getCorporation().getPrimaryKey(), null);

			} else if (BillTypeConst.TO_ORDER3.equals(billtype.trim()) || BillTypeConst.TO_ORDER2.equals(billtype.trim()) || BillTypeConst.TO_ORDER1.equals(billtype.trim()) || BillTypeConst.TO_ORDER4.equals(billtype.trim())) {
				BillVO billvo = (BillVO) LongTimeTask.callMethod(3, "nc.ui.to.outer.QryOrder4ICLinkAdd", null, "querySourceBillVOForLinkAdd", new Class[] {
						String.class, String.class, String.class, String.class, Object.class
				}, new Object[] {
						adddata.getSourceBillID(), billtype, adddata.getSourcePkOrg(), ClientEnvironment.getInstance().getCorporation().getPrimaryKey(), null
				});
				if (billvo == null) return null;
				srcvos = new BillVO[] {
					billvo
				};

			} else {
				return null;
			}
		} catch (Exception e) {
			nc.ui.ic.pub.tools.GenMethod.handleException(this, null, e);
			return null;
		}

		if (srcvos == null || srcvos.length <= 0) {
			showErrorMessage(ResBase.getLinkAddQueryErr());
			return null;
		}

		// 根据源单据分单
		srcvos = GenMethod.splitSourceVOs(srcvos, adddata.getSourceBillType().trim(), getBillTypeCode());

		GeneralBillVO[] retbillvos = null;
		try {
			retbillvos = (GeneralBillVO[]) PfChangeBO_Client.pfChangeBillToBillArray(srcvos, adddata.getSourceBillType().trim(), getBillTypeCode());
			retbillvos = GenMethod.splitGeneralBillVOs(retbillvos, getBillTypeCode(), getBillListPanel().getHeadBillModel().getFormulaParse());
		} catch (Exception e) {
			nc.ui.ic.pub.tools.GenMethod.handleException(null, null, e);
		}

		return retbillvos;
	}

	/**
	 * UI关联操作-审批
	 * 
	 * @author cch 2006-5-9-11:04:16
	 */
	public void doApproveAction(ILinkApproveData approvedata) {
		queryForLinkOper(approvedata.getPkOrg(), approvedata.getBillType(), approvedata.getBillID());
	}

	/**
	 * UI关联操作-维护
	 * 
	 * @author leijun 2006-5-24
	 */
	public void doMaintainAction(ILinkMaintainData maintaindata) {
		queryForLinkOper(getClientEnvironment().getCorporation().getPrimaryKey(), getBillTypeCode(), maintaindata.getBillID());
		onUpdate();
	}

	/**
	 * showBtnSwitch 符合界面规范
	 * 
	 * @author leijun 2006-5-24
	 */
	public void showBtnSwitch() {
		if (m_iCurPanel == BillMode.Card) getButtonTree().getButton(ICButtonConst.BTN_SWITCH).setName(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH022")/* @res "列表显示" */);
		else getButtonTree().getButton(ICButtonConst.BTN_SWITCH).setName(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH021")/* @res "卡片显示" */);
		updateButton(getButtonTree().getButton(ICButtonConst.BTN_SWITCH));

	}

	protected void refreshSelBill(int selrow) {

		GeneralBillVO voBill = (GeneralBillVO) m_alListData.get(selrow);

		QryConditionVO voCond = new QryConditionVO(" head.cgeneralhid='" + voBill.getHeaderVO().getCgeneralhid() + "'");

		try {
			ArrayList alListData = (ArrayList) GeneralBillHelper.queryBills(m_sBillTypeCode, voCond);
			m_alListData.set(selrow, alListData.get(0));
			setDataOnList(m_alListData, true);
			selectListBill(selrow);

		} catch (Exception e) {
			showErrorMessage(e.getMessage());

		}

	}

	/**
	 * 创建者：王乃军 功能：是否借转类型
	 * 
	 * 
	 * 参数： 返回： 例外： 日期：(2001-11-24 12:15:42) 修改日期，修改人，修改原因，注释标志：
	 */
	protected boolean isBrwLendBiztype() {
		return false;

	}

	/**
	 * 创建者：刘家清 功能：根据单据类型控制按钮
	 * 
	 * 
	 * 参数： 返回： 例外： 日期：(2007-04-05 17:00:00) 修改日期，修改人，修改原因，注释标志：
	 */

	protected void ctrlBillTypeButtons(boolean willDo) {

		if (willDo) {

		}

	}

	/**
	 * 创建者：刘家清 功能：根据单据类型控制按钮
	 * 
	 * 
	 * 参数： 返回： 例外： 日期：(2007-04-06 10:26:00) 修改日期，修改人，修改原因，注释标志：
	 */

	public String getBusiTypeItemKey() {

		if (m_voBill != null) {
			if (m_voBill.getBizTypeid() != null) {

			return m_voBill.getBizTypeid();

			}
			return "";
		} else {
			return "";
		}

	}

	protected UIMenuItem miAddNewRowNo = new UIMenuItem("重排行号");

	protected int m_Menu_AddNewRowNO_Index = -1;

	protected UIMenuItem getAddNewRowNoItem() {
		return miAddNewRowNo;
	}

	/**
	 * 创建人：刘家清 创建日期：2007-12-26上午09:27:52 创建原因：自动设置界面上已有的所有行的行号
	 * 
	 */
	protected void onAddNewRowNo() {

		nc.ui.scm.pub.report.BillRowNo.addNewRowNo(getBillCardPanel(), m_sBillTypeCode, m_sBillRowNo);

		for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
			if (getBillCardPanel().getBillModel().getRowState(i) == BillModel.NORMAL) getBillCardPanel().getBillModel().setRowState(i, BillModel.MODIFICATION);
		}
	}

	/**
	 * @功能:返回公司的上级公司编码
	 * @return add by cm
	 */
	public String getParentCorpCode1() {

		String ParentCorp = new String();
		String key = ClientEnvironment.getInstance().getCorporation().getFathercorp();
		try {
			CorpVO corpVO = CorpBO_Client.findByPrimaryKey(key);
			ParentCorp = corpVO.getUnitcode();
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return ParentCorp;
	}
	
	/**
	 * add by shikun 2014-04-08 取得库存无隔离数量
	 * */
	public UFDouble getWGLnum(int row){
		String cwarehouseid = getBillCardPanel().getHeadItem("cwarehouseid").getValueObject()==null?"":getBillCardPanel().getHeadItem("cwarehouseid").getValueObject().toString();
		String cinventoryid = getBillCardPanel().getBodyValueAt(row, "cinventoryid")==null?"":getBillCardPanel().getBodyValueAt(row, "cinventoryid").toString();
		String vbatchnum = getBillCardPanel().getBillModel().getValueAt(row, "vbatchcode") == null ? "" : getBillCardPanel().getBillModel().getValueAt(row, "vbatchcode").toString();
		String cspaceid = getBillCardPanel().getBodyValueAt(row, "cspaceid")==null?"":getBillCardPanel().getBodyValueAt(row, "cspaceid").toString();
		String vfree = getBillCardPanel().getBodyValueAt(row, "vfree0")==null?"":getBillCardPanel().getBodyValueAt(row, "vfree0").toString();
		String vfree0 = getvfree1(vfree);
		if ("".equals(vfree0)) {
			vfree0 = getBillCardPanel().getBodyValueAt(row, "vfree1")==null?"":getBillCardPanel().getBodyValueAt(row, "vfree1").toString();
		}
		if (!cwarehouseid.equals("")&&!cinventoryid.equals("")) {
			StringBuffer sql = new StringBuffer("");
			sql.append(" select SUM((nvl(v1.ninspacenum, 0.0) - nvl(v1.noutspacenum, 0.0)) - ") 
			.append("            (nvl(icf.nfreezenum, 0.0) - nvl(ndefrznum, 0.0))) wglnum ") 
			.append("   from v_ic_onhandnum6 v1 ") 
			.append("   left join ic_freeze icf on v1.cinventoryid = icf.cinventoryid ") 
			.append("                          and icf.ccalbodyid = v1.ccalbodyid ") 
			.append("                          and icf.cwarehouseid = v1.cwarehouseid ") 
			.append("                                  and nvl(icf.cspaceid,'byzgyh') = nvl(v1.cspaceid,'byzgyh') ") 
			.append("                                  and icf.pk_corp = v1.pk_corp ") 
			.append("                                  and nvl(v1.vfree1,'byzgyh') = nvl(icf.vfree1,'byzgyh') ") 
			.append("                                  and nvl(v1.vbatchcode,'byzgyh') = nvl(icf.vbatchcode,'byzgyh') ") 
			.append("                                  and nvl(icf.dr, 0) = 0 and nvl(icf.cthawpersonid,'')='' ") 
			.append("  where 1 = 1 ") 
			.append("    and v1.pk_corp = '"+getCorpPrimaryKey()+"' ") 
			.append("    and v1.cwarehouseid = '"+cwarehouseid+"' ") 
			.append("    and v1.cinventoryid = '"+cinventoryid+"' ") ;
			if (!"".equals(cspaceid)) {
				sql.append("    and v1.cspaceid = '"+cspaceid+"' ") ;
			}
			if (!"".equals(vbatchnum)) {
				sql.append("    and v1.vbatchcode = '"+vbatchnum+"' ") ;
			}
			if (!"".equals(vfree0)) {
				sql.append("    and v1.vfree1 = '"+vfree0+"' ") ;
			}
			IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			MapListProcessor alp = new MapListProcessor();
			Object obj = null;
			try {
				obj = query.executeQuery(sql.toString(), alp);
			} catch (BusinessException e1) {
				e1.printStackTrace();
			}
			ArrayList addrList = (ArrayList) obj;
			//无隔离现存数据
			UFDouble ninspacenum = new UFDouble(0);
			if (addrList != null && addrList.size() > 0) {
				Map addrMap = (Map) addrList.get(0);
				if (addrMap.get("wglnum") == null) {
					ninspacenum = new UFDouble(0.00);
				} else {
					ninspacenum = new UFDouble(addrMap.get("wglnum").toString()) == null ? new UFDouble(0) : new UFDouble(addrMap.get("wglnum").toString());
				}
			}
			return ninspacenum;
		}
		return new UFDouble(0.00);
	}

	public String getvfree1(String vfree) {
		if (!"".equals(vfree)) {
			String[] vfree0s = vfree.split(":");
			if (vfree0s!=null&&vfree0s.length>1) {
				String vfree0 = vfree0s[1];
				vfree0 = vfree0.substring(0,vfree0.length()-1);
				return vfree0;
			}
		}
		return "";
	}

	/**
	 * add by shikun 上下游按钮新增动作去除隔离数量 表体数据刷新 
	 * */
	private void subGLSL() {
		int rows = getBillCardPanel().getBillTable().getRowCount();
		//冻结的数据不要在领用出库、移库、货位调整、发货界面出现 add by shikun
		if (rows>0) {
			for (int i = 0; i < rows; i++) {
				UFDouble num = getWGLnum(i);
				UFDouble noutnum = getBillCardPanel().getBodyValueAt(i, "noutnum")==null? new UFDouble(0.00):new UFDouble(getBillCardPanel().getBodyValueAt(i, "noutnum").toString());
				if (noutnum.doubleValue()!=0&&noutnum.doubleValue()<num.doubleValue()) {//本地数量不为空，并且本地数量小于有效数量，那么本地数量不变；如果本地数量为空或本地数量大于有效数量，那取有效数量
					num = noutnum;
				}
				getBillCardPanel().setBodyValueAt(num, i, "noutnum");
			}
		}
		
	}
	
}