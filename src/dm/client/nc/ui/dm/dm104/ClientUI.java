package nc.ui.dm.dm104;

import java.awt.Container;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.ibm.db2.jcc.b.sb;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.bd.ref.busi.CustmandocDefaultRefModel;
import nc.ui.dm.dm001.DelivorgHelper;
import nc.ui.dm.dm102.GeneralMethod;
import nc.ui.dm.pub.DMBillStatus;
import nc.ui.dm.pub.DMQueryConditionDlg;
import nc.ui.dm.pub.DmHelper;
import nc.ui.dm.pub.ExceptionUITools;
import nc.ui.dm.pub.RemoteCall;
import nc.ui.dm.pub.cardpanel.DMBillCardPanel;
import nc.ui.dm.pub.mvc.ShowDelivOrg;
import nc.ui.dm.pub.ref.DMTextDocument;
import nc.ui.dm.pub.ref.DelivorgRef;
import nc.ui.dm.pub.ref.TrancustRefModel;
import nc.ui.dm.severalbills.ClientUISeveralBills;
import nc.ui.ic.pub.lot.LotNumbRefPane;
import nc.ui.ml.NCLangRes;
import nc.ui.pf.query.ICheckRetVO;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.FramePanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.beans.textfield.UITextType;
import nc.ui.pub.bill.BillCardBeforeEditListener;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.pub.bill.BillListData;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.IBillModelSortPrepareListener;
import nc.ui.pub.bill.IBillRelaSortListener2;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.pub.linkoperate.ILinkType;
import nc.ui.scm.ic.freeitem.FreeItemRefPane;
import nc.ui.scm.print.BillPrintTool;
import nc.ui.scm.pub.CollectSettingDlg;
import nc.ui.scm.pub.bill.IBillExtendFun;
import nc.vo.dm.dm001.DelivorgHeaderVO;
import nc.vo.dm.dm001.DelivorgVO;
import nc.vo.dm.dm104.DelivBillVOTool;
import nc.vo.dm.dm104.DelivbillHHeaderVO;
import nc.vo.dm.dm104.DelivbillHItemVO;
import nc.vo.dm.dm104.DelivbillHVO;
import nc.vo.dm.dm104.FreightType;
import nc.vo.dm.dm104.OutbillHVO;
import nc.vo.dm.pub.DMBillNodeCodeConst;
import nc.vo.dm.pub.DMBillTypeConst;
import nc.vo.dm.pub.DMDataVO;
import nc.vo.dm.pub.DMVO;
import nc.vo.dm.pub.DelivBillStatus;
import nc.vo.dm.pub.tools.StringTools;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.VOStatus;
import nc.vo.pub.formulaset.function.GetEnglishCurrency;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.query.QueryConditionVO;
import nc.vo.scm.constant.CConstant;
import nc.vo.scm.ic.bill.InvVO;
import nc.vo.scm.ic.exp.ATPNotEnoughException;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.session.ClientLink;

/**
 * 类的功能、用途、现存BUG，以及其它别人可能感兴趣的介绍。 作者：仲瑞庆
 * 
 * @version 最后修改日期(2002-5-9 13:31:03)
 * @see 需要参见的其它类
 * @since 从产品的那一个版本，此类被添加进来。（可选） 修改人 + 修改日期 修改说明
 */

public class ClientUI extends nc.ui.dm.pub.ClientUIforBill implements
		BillCardBeforeEditListener, MouseListener, ICheckRetVO, IBillExtendFun,
		ILinkQuery, IBillRelaSortListener2, IBillModelSortPrepareListener {

	private static final long serialVersionUID = -6362122994030467898L;

	// 表单编辑，“新增”或“修改”后进入表单编辑状态
	private static final int CardBillEditing = 1;

	// 表单非编辑状态，承运商没有确认
	private static final int BeforeConfirm = 2;

	// 没有审批
	private static final int BeforeApprove = 3;

	// 审批后
	private static final int AfterApproved = 4;

	// 出库之后。（审批之后才能生成任务单、才能出库）
	private static final int AfterOut = 6;

	// 关闭后
	private static final int AfterClosed = 5;

	// 未生成任务单（没有填承运商）
	private static final int BeforeTask = 7;

	// 已经生成任务单（没有填承运商）
	private static final int AfterTasked = 8;

	// 卡片批修改
	public static final int CardBatchEditing = 9;

	// 初始状态
	public static final int Init = 10;

	// 查询后
	public static final int AfterQuery = 11;

	protected ButtonObject boCalculateFee; // 计算运费

	protected ButtonObject boCancelConfirm; // 承运商取消确认

	protected ButtonObject boGenTaskBill; // 生成任务单

	protected ButtonObject boOpenBill; // 打开发运单

	protected ButtonObject boOutBill; // 生成出库单

	protected ButtonObject boPackageList; // 包装明细

	protected ButtonObject boShowListBill; // 发运清单

	protected ButtonObject boTestCalculate; // 运费试算

	protected ButtonObject boTrancust; // 承运商确认和取消

	protected ButtonObject boTransConfirm; // 承运商确认

	protected ButtonObject boRowCloseOut; // 行出库关闭

	protected ButtonObject boRowOpenOut; // 行出库打开

	protected ButtonObject boBillCombin; // 合并显示

	// eric
	protected ButtonObject boOnhandnum; // 存量查询

	private double dTransRate; // 换算率: added by zxping

	// 如果是途损引用该节点，
	// 那么在查询条件中有“发运组织”这个条件,但不是必录项
	private boolean isQueryForWayLoss = false;

	// 查询
	private DMQueryConditionDlg ivjQueryConditionDlg = null;

	private QueryConditionDlg listQueryConditionDlg = null;

	// 批生成发运单当前单据
	public DelivbillHVO[] m_batchbills;

	// 列表界面当前单据
	protected DelivbillHVO[] m_bills;

	// 是否插入到已有的发运单
	private boolean m_bInsertExist = false;

	// 是否显示运费计算界面
	private boolean m_bIsCaculateShow = true;

	// 区分列表新增和表单新增
	boolean m_biscardadd;

	private ConditionVO[] m_ConditionVos = null; // 查询条件

	// 卡片界面当前单据
	public DelivbillHVO m_currentbill;

	// 发运清单
	protected DelivListDlg m_delivLisDlg = null;

	// 计算运费对话框
	// protected CaculateFreight m_caculateFreightDlg= null;
	protected CaculateTransFee m_dlgCaculateTransFee = null;

	protected DMDataVO[] m_dmdvosPackageVOs = null; // 传入和返回的记录包装分类和运输仓的数量分配的VO数组

	// 区分增加和修改:0为增加，1为修改, 2为批生成发运单
	// public int m_editFlag;

	// 依使用的自创参照数量建一组参照
	protected FreeItemRefPane m_firpFreeItemRefPane = new FreeItemRefPane();

	// 列表界面当前表头
	protected DelivbillHHeaderVO[] m_headers = null;

	// 在发运安排节点，引用本节点的相应方法
	// 设定该标记，标志操作是否结束
	// 发运安排据此标志，决定是否进行其余的处理
	protected boolean m_isOperationFinish = false;

	// 列表行号
	int m_listRow;

	protected LotNumbRefPane m_lnrpLotNumbRefPane = new LotNumbRefPane();

	// 生成出库单对话框
	protected OutBillDlg m_outBillDlg = null;

	// protected String m_sBillTypeCode = DMBillTypeConst.m_delivDelivBill;

	private String m_sRNodeName = "40140408";

	UFDateTime m_ufdtAddTime = null; // 新增时的当前时间(指中间件时间)

	private PackageListDlg packageListDlg = null; // 输入包装明细界面

	private final String pkassistmeasure = "pkassistmeasure";

	private final String pkinv = "pkinv";

	private final String sAstItemKey = "dinvassist";

	// 辅计量处理字段
	private final String sNumItemKey = "dinvnum";

	private final String vassistmeaname = "vassistmeaname";

	// 如下变量由途损节点使用， 类 DelivBillClientUI 访问。
	// 意义： 是否为发货方途损
	protected boolean m_isForSendSide = false;

	// 发运单上是否已经修改了包装明细
	private boolean hasChangedPack = false;

	// 包装分类件数体积换算表
	private Map packsort_volumnIndex = new HashMap();

	// 包装分类件数、存货换算表
	private Map packsort_numIndex = new HashMap();

	private boolean bIsForSofee = false;

	// "状态"条件查询面板
	private QueryStatusConditionPane m_panelStatusQuery = null;

	// 节点由何处触发打开，默认为从节点树正常打开
	private int opentype = ILinkType.NONLINK_TYPE;

	/**
	 * ClientUI 构造子注解。
	 */
	public ClientUI() {
		super();
		initializeNew();
	}

	/**
	 * ClientUI 构造子注解。
	 */
	public ClientUI(boolean bDelivOrgNoShow) {
		super();
		setDelivOrgNoShow(bDelivOrgNoShow);
		initializeNew();
	}

	/**
	 * ClientUI 构造子注解。
	 */
	public ClientUI(boolean bDelivOrgNoShow, boolean isForSofee) {
		super();
		setDelivOrgNoShow(bDelivOrgNoShow);
		bIsForSofee = isForSofee;
		initializeNew();
	}

	/**
	 * ClientUI 构造子注解。 为 ic 而设置该 construction
	 */
	public ClientUI(String sDelivOrgPK, String sDelivOrgCode,
			String sDelivOrgName, ArrayList alAgentCorpIDsofDelivOrg) {
		super();
		setDelivOrgNoShow(true);
		setDelivOrgCode(sDelivOrgCode);
		setDelivOrgPK(sDelivOrgPK);
		setDelivOrgName(sDelivOrgName);
		setAgentCorpIDsofDelivOrg(alAgentCorpIDsofDelivOrg);
		initializeNew();
		// getBillListPanel().getHeadTable().removeMouseListener(this);
		// addMouseListener(this);
	}

	public ClientUI(FramePanel fp) {
		super();
		this.opentype = fp.getLinkType();
	}

	protected String checkPrerequisite() {
		// 非联查打开节点
		if (this.opentype != ILinkType.LINK_TYPE_QUERY) {
			try {
				initializeNew();
			} catch (Error ex) {
				ex.printStackTrace();
				return ex.getMessage();
			}
		}
		return null;
	}

	public boolean getIsForSendSide() {
		return m_isForSendSide;
	}

	/**
	 * 设置表体部分的自由项参照和批次参照。
	 * 
	 * @author zxj
	 */
	public void addSpecialRef() {
		BillData bd = getBillCardPanel().getBillData();
		if (bd == null) {
			SCMEnv.info("--> billdata is null.");
			return;
		}

		// 参照的文本输入框默认最大输入长度为20个字符，如果需要的输入长度超过这一数值，则可能出错，
		// 因此需要改变参照的文本输入框的输入长度
		getFreeItemRefPane().setMaxLength(bd.getBodyItem("vfree0").getLength()); // 表体,自由项
		getLotNumbRefPane().setMaxLength(
				bd.getBodyItem("vbatchcode").getLength()); // 表体,批次

		// bd.getHeadItem("pk_corp").setComponent(new LotNumbRefPane());//表头
		bd.getBodyItem("vfree0").setComponent(getFreeItemRefPane()); // 表体,自由项
		bd.getBodyItem("vbatchcode").setComponent(getLotNumbRefPane()); // 表体,批次

		getBillCardPanel().setBillData(bd);
	}

	/**
	 * 应发辅数量编辑后，进行的事件处理。
	 * <p>
	 * 注意： 辅数量*换算率=数量
	 * 
	 * @param e
	 *            单据编辑事件
	 * @author 余大英
	 */
	public void afterAstNumEdit(nc.ui.pub.bill.BillEditEvent e) {
		int row = e.getRow(); // 编辑事件所涉及的行
		Object oTemp = null;

		// 取得存货id
		oTemp = getBillCardPanel().getBodyValueAt(row, pkinv);
		if (oTemp == null) {
			MessageDialog.showErrorDlg(this, null, "缺少存货主键");// TODO 多语言
			return;
		}

		UFDouble hsl = null;

		// 如果辅单位主键为空，则清除“辅单位”和“辅数量”的值
		Object oTemp0 = getBillCardPanel().getBodyValueAt(row,
				"pkassistmeasure");
		// 辅单位主键
		if (oTemp0 == null || oTemp0.toString().trim().length() == 0) {
			String[] name = { "["
					+ NCLangRes.getInstance().getStrByID("common",
							"UC000-0003975") + "]" };
			/* @res "请输入{0}" *+* @res 辅计量单位" */
			MessageDialog
					.showWarningDlg(this, null, NCLangRes.getInstance()
							.getStrByID("scmcommon", "UPPSCMCommon-000400",
									null, name));
			getBillCardPanel().setBodyValueAt(null, row, "vassistmeaname"); // 辅单位
			getBillCardPanel().setBodyValueAt(null, row, "dinvassist"); // 辅数量
		}

		// 根据辅计量id和存货id，查询存货的详细信息（包括换算率）
		String sInvID = oTemp.toString();
		oTemp = getBillCardPanel().getBodyValueAt(row, pkassistmeasure);
		String sAstID = null;
		if (oTemp != null) {
			sAstID = oTemp.toString();
		}
		InvVO voaInv[] = getInvInfo(new String[] { sInvID },
				new String[] { sAstID });
		// 查存货信息，带辅计量单位
		if (voaInv == null || voaInv.length == 0 || voaInv[0] == null) {
			SCMEnv.info("没有对应的存货 vo");
			return;
		}

		// 当辅数量为空时的处理
		if (getBillCardPanel().getBodyValueAt(row, sAstItemKey) == null
				|| getBillCardPanel().getBodyValueAt(row, sAstItemKey)
						.toString().trim().length() == 0) {

			// 辅数量为空清空，则清空数量
			getBillCardPanel().setBodyValueAt(null, row, sNumItemKey);

			// 数量列的值清空后，强制执行对表体行数量改变后的处理
			afterNumEdit(e);
			return;
		}

		UFDouble ninassistnum = new UFDouble(getBillCardPanel().getBodyValueAt(
				row, sAstItemKey).toString().trim());

		// 修改数量
		// hsl = invvo.getHsl();
		hsl = new UFDouble(dTransRate);

		if (hsl != null && hsl.doubleValue() > 0) {
			UFDouble ninnum = ninassistnum.multiply(hsl);
			getBillCardPanel().setBodyValueAt(ninnum, row, sNumItemKey);

			// 数量列的值改变后，强制执行对表体行数量改变后的处理
			afterNumEdit(e);
		}

	}

	/**
	 * 单据编辑事件处理
	 * 
	 * @param e
	 *            单据编辑事件
	 * @author 王乃军
	 */
	public void afterAstUOMEdit(nc.ui.pub.bill.BillEditEvent e) {
		int row = e.getRow();

		UFDouble hsl = null;
		if (!getHsl(row, hsl)) {
			return;
		}

		UFDouble ninnum = null;
		UFDouble ninassistnum = null;
		Object oinnum = getBillCardPanel().getBodyValueAt(row, sNumItemKey);
		Object oinassistnum = getBillCardPanel().getBodyValueAt(row,
				sAstItemKey);
		if (oinnum != null) {
			ninnum = new UFDouble(oinnum.toString().trim());
		}
		if (oinassistnum != null) {
			ninassistnum = new UFDouble(oinassistnum.toString().trim());

		}
		if ((ninnum == null && ninassistnum == null) || hsl == null) {
			return;
		}

		/* 辅计量：换算率；无论固定，变动换算率，按辅数量＊换算率来重新计算主数量 */
		if (ninassistnum != null) {
			ninnum = ninassistnum.multiply(hsl);
			getBillCardPanel().setBodyValueAt(ninnum, row, sNumItemKey);
		} else {
			getBillCardPanel().setBodyValueAt(null, row, sNumItemKey);
		}
		// 强制调用“数量”改变事件处理
		afterNumEdit(e);

		// 清批次号
		getBillCardPanel().setBodyValueAt(null, row, "vbatchcode");
		getLotNumbRefPane().setValue(null);
	}

	/**
	 * 编辑后处理
	 * 
	 * @param e
	 *            单据编辑事件
	 */
	public void afterEdit(nc.ui.pub.bill.BillEditEvent e) {
		try {
			super.afterEdit(e);

			// ((UIRefPane) e.getSource()).getRefPK();
			// ((UIRefPane) e.getSource()).getText();
			// ((UIRefPane) e.getSource()).getUITextField().getText();

			String strColName = e.getKey().trim();
			String pk = "";
			int currentRow = 0;
			currentRow = e.getRow();
			// e.getOldValue();
			UIRefPane currentRef = null;

			if (strColName.equals("vinvcode")) { // 存货
				currentRef = ((UIRefPane) getBillCardPanel().getBodyItem(
						strColName).getComponent());
				pk = currentRef.getRefPK();
				currentRow = getBillCardPanel().getBillTable().getSelectedRow();
				getBillCardPanel().setBodyValueAt(pk, currentRow, "pkinv");
				getBillCardPanel().setBodyValueAt(currentRef.getRefName(),
						currentRow, "vinvname");
				getBillCardPanel().setBodyValueAt(new Integer(currentRow + 1),
						currentRow, "irownumber");
				// invArray= DelivbillHBO_Client.findInfoForInv(pk);
				// 获得存货信息
				String[] invkeys = new String[] { pk };
				InvVO[] invvos = getInvInfo(invkeys, null);
				if (null != invvos && invvos.length == 1 && invvos[0] != null) {
					getBillCardPanel().setBodyValueAt(
							invvos[0].getAttributeValue("vspec"), currentRow,
							"vspec");
					getBillCardPanel().setBodyValueAt(
							invvos[0].getAttributeValue("vtype"), currentRow,
							"vtype");
					getBillCardPanel().setBodyValueAt(
							invvos[0].getAttributeValue("vunit"), currentRow,
							"vunit");
				}

			} else if (strColName.equals("vassistmeaname")) { // 辅单位
				Object oTemp = getBillCardPanel().getBodyValueAt(e.getRow(),
						"vassistmeaname");
				if (oTemp == null || oTemp.toString().trim().length() == 0) {
					getBillCardPanel().setBodyValueAt(null, e.getRow(),
							"pkassistmeasure");
					getBillCardPanel().setBodyValueAt(null, e.getRow(),
							"dinvassist");
				}

				currentRef = ((UIRefPane) getBillCardPanel().getBodyItem(
						strColName).getComponent());
				pk = currentRef.getRefPK();
				currentRow = getBillCardPanel().getBillTable().getSelectedRow();
				getBillCardPanel().setBodyValueAt(pk, currentRow,
						"pkassistmeasure");
				getBillCardPanel().setBodyValueAt(currentRef.getRefName(),
						currentRow, "vassistmeaname");

				// 主辅计量换算
				afterAstUOMEdit(e);

			} else if (strColName.equals("vcontainername")) { // 运输仓
				currentRef = ((UIRefPane) getBillCardPanel().getBodyItem(
						strColName).getComponent());
				pk = currentRef.getRefPK();
				currentRow = getBillCardPanel().getBillTable().getSelectedRow();
				getBillCardPanel().setBodyValueAt(pk, currentRow,
						"pk_transcontainer");
				getBillCardPanel().setBodyValueAt(currentRef.getRefName(),
						currentRow, "vcontainername");

			} else if (strColName.equals("pkdelivmode")) { // 发运方式
				currentRef = (UIRefPane) e.getSource();
				pk = currentRef.getRefPK();
				getBillCardPanel().setHeadItem("pkdelivmode", pk);
				getBillCardPanel().setHeadItem("vsendtypename",
						currentRef.getRefName());
			} else if (strColName.equals("pktranorg")) { // 运输部门
				currentRef = (UIRefPane) e.getSource();
				pk = currentRef.getRefPK();
				getBillCardPanel().setHeadItem("pktranorg", pk);
				getBillCardPanel().setHeadItem("vtranorgname",
						currentRef.getRefName());
			} else if (strColName.equals("pktrancust")) { // 承运商
				currentRef = (UIRefPane) e.getSource();

				String textValue = currentRef.getUITextField().getText(); // 用于判断此时承运商选择是否为空

				// if(textValue!=null && textValue.length()==0)
				// textValue = null;

				pk = currentRef.getRefPK();
				// currentRef.getRefModel().getPkValue();
				getBillCardPanel().setHeadItem("pktrancust", pk);
				getBillCardPanel().setHeadItem("vtranname",
						currentRef.getRefName());

				// 带出联系人电话
				String linkman1 = null, phone1 = null;
				ArrayList trancustLink = DelivbillHBO_Client
						.findTrancustLink(pk);
				if (trancustLink != null && trancustLink.size() > 1) {
					if (trancustLink.get(0) != null) {
						linkman1 = trancustLink.get(0).toString();
					}
					if (trancustLink.get(1) != null) {
						phone1 = trancustLink.get(1).toString();
					}
				}
				getBillCardPanel().setHeadItem("linkman1", linkman1);
				getBillCardPanel().setHeadItem("phone1", phone1);

				currentRef = (UIRefPane) getBillCardPanel().getHeadItem(
						"pkdriver").getComponent();
				UIRefPane vehicleRef = (UIRefPane) getBillCardPanel()
						.getHeadItem("pkvehicle").getComponent();
				UIRefPane vehicleTypeRef = (UIRefPane) getBillCardPanel()
						.getHeadItem("pkvehicletype").getComponent();

				currentRef.setPK(null);
				vehicleRef.setPK(null);
				vehicleTypeRef.setPK(null);

				// “司机”参照时按“承运商”进行过滤
				if (textValue != null && textValue.trim().length() > 0) {
					((nc.ui.dm.pub.ref.DriverRefModel) currentRef.getRefModel())
							.addWherePart(" and dm_driver.pktrancust = '" + pk
									+ "' ");
				} else {
					String s = " dm_driver.pkdelivorg = '"
							+ getDelivOrgPK()
							+ "' and dm_driver.dr = 0 and dm_driver.bfroze = 'N' ";
					currentRef.setWhereString(s);
				}

				// “车辆”参照时按“承运商”进行过滤
				if (textValue != null && textValue.trim().length() > 0) {
					((nc.ui.dm.pub.ref.VehicleRefModel) vehicleRef
							.getRefModel())
							.addWherePart(" and dm_vehicle.pktrancust = '" + pk
									+ "' ");
				} else {
					String s = " dm_vehicle.pkdelivorg = '"
							+ getDelivOrgPK()
							+ "' and dm_vehicle.dr = 0 and dm_vehicle.bisseal = 'N' ";
					vehicleRef.setWhereString(s);
				}

				// "包装“按照承运商过滤
				UIRefPane ref = (UIRefPane) getBillCardPanel().getBodyItem(
						"vcontainername").getComponent();
				if (textValue != null && textValue.trim().length() > 0) {
					((nc.ui.dm.pub.ref.ContainerRefModel) ref.getRefModel())
							.addWherePart(" and dm_transcontainer.pktranscust = '"
									+ pk + "' ");
				} else {
					String s = " dm_transcontainer.pkdelivorg = '"
							+ getDelivOrgPK()
							+ "' and dm_transcontainer.dr = 0 ";
					ref.setWhereString(s);
				}

				// 承运商承诺到货日期由是否有承运商来控制
				if (textValue != null && textValue.trim().length() > 0) {
					getBillCardPanel().getHeadItem("h_confirmarrivedate")
							.setEnabled(true);
				} else {
					getBillCardPanel().getHeadItem("h_confirmarrivedate")
							.setValue(null);
					getBillCardPanel().getHeadItem("h_confirmarrivedate")
							.setEnabled(false);
				}

			} else if (strColName.equals("pkdelivroute")) { // 路线
				currentRef = (UIRefPane) e.getSource();
				pk = currentRef.getRefPK();
				if (pk == null) {
					return;
				}

				getBillCardPanel().setHeadItem("pkdelivroute", pk);
				getBillCardPanel().setHeadItem("vroutename",
						currentRef.getRefName());
				String routedesrb = DelivbillHBO_Client.findRouteDescr(pk);
				getBillCardPanel().setHeadItem("vroutedescr", routedesrb);

				String pktrancust = getBillCardPanel()
						.getHeadItem("pktrancust").getValue();
				String vtranname = getBillCardPanel().getHeadItem("vtranname")
						.getValue();
				String pkvehicle = getBillCardPanel().getHeadItem("pkvehicle")
						.getValue();
				String vvehiclename = getBillCardPanel().getHeadItem(
						"vvehiclename").getValue();
				String[] formulas = new String[6];
				// 发运路线上的默认承运商
				formulas[0] = "pktrancust->getColValue(bd_route,pkdetrancust,pk_route,pkdelivroute)";
				formulas[1] = "vtranname->getColValue(dm_trancust,pkcusmandoc,pk_trancust,pktrancust)";
				// 发运路线上的默认车辆
				formulas[2] = "pkvehicle->getColValue(bd_route,pkdelivvehicle,pk_route,pkdelivroute)";
				formulas[3] = "vvehiclename->getColValue(dm_vehicle,vvehiclename,pk_vehicle,pkvehicle)";
				// 车辆对应的车型
				formulas[4] = "pkvehicletype->getColValue(dm_vehicle,pkvehicletype,pk_vehicle,pkvehicle)";
				formulas[5] = "vvhcltypename->getColValue(dm_vehicletype,vvhcltypename,pk_vehicletype,pkvehicletype)";
				getBillCardPanel().execHeadFormulas(formulas);

				String pktrancust_1 = getBillCardPanel().getHeadItem(
						"pktrancust").getValue();
				String vtranname_1 = getBillCardPanel()
						.getHeadItem("vtranname").getValue();
				String pkvehicle_1 = getBillCardPanel()
						.getHeadItem("pkvehicle").getValue();
				String vvehiclename_1 = getBillCardPanel().getHeadItem(
						"vvehiclename").getValue();
				/**
				 * 确保发运线上没有默认值的时候原有输入的选项不会被清空
				 */
				if (pktrancust != null && pktrancust_1 == null) {
					getBillCardPanel().getHeadItem("pktrancust").setValue(
							pktrancust);
				}
				if (vtranname != null && vtranname_1 == null) {
					getBillCardPanel().getHeadItem("vtranname").setValue(
							vtranname);
				}
				if (pkvehicle != null && pkvehicle_1 == null) {
					getBillCardPanel().getHeadItem("pkvehicle").setValue(
							pkvehicle);
				}
				if (vvehiclename != null && vvehiclename_1 == null) {
					getBillCardPanel().getHeadItem("vvehiclename").setValue(
							vvehiclename);
				}

				// 通过路线带出表体的收货地点和发货地点
				String[] parameter = new String[1];
				parameter[0] = pk;

				RemoteCall remote = new RemoteCall();
				String[][] destinates = (String[][]) remote.callService(
						"nc.bs.dm.pub.DMUtils", "queryAddressByRoute",
						new Class[] { String.class }, parameter);
				int rowcount = getBillCardPanel().getRowCount();
				if (destinates.length == 2) {
					String start_pk_des = destinates[0][0];
					String start_name_des = destinates[0][1];
					String end_pk_des = destinates[1][0];
					String end_name_des = destinates[1][1];
					for (int i = 0; i < rowcount; i++) {
						getBillCardPanel().setBodyValueAt(start_pk_des, i,
								"pksendaddress");
						getBillCardPanel().setBodyValueAt(start_name_des, i,
								"vsendaddress");
						getBillCardPanel().setBodyValueAt(end_pk_des, i,
								"pkarriveaddress");
						getBillCardPanel().setBodyValueAt(end_name_des, i,
								"varriveaddress");
					}
				}
			} else if (strColName.equals("pkdriver")) { // 司机
				currentRef = (UIRefPane) e.getSource();
				pk = currentRef.getRefPK();
				getBillCardPanel().setHeadItem("pkdriver", pk);
				getBillCardPanel().setHeadItem("vdrivername",
						currentRef.getRefName());

				Object oTemp = getBillCardPanel().execHeadFormula(
						"getColValue(dm_driver,pktrancust,pk_driver,pkdriver)");
				if (oTemp != null && oTemp.toString().trim().length() > 0) {
					currentRef = (UIRefPane) getBillCardPanel().getHeadItem(
							"pktrancust").getComponent();
					currentRef.setPK(oTemp);
				}

			} else if (strColName.equals("pkvehicle")) { // 车辆
				currentRef = (UIRefPane) e.getSource();
				pk = currentRef.getRefPK();
				getBillCardPanel().setHeadItem("pkvehicle", pk);
				getBillCardPanel().setHeadItem("vvehiclename",
						currentRef.getRefName());
				getBillCardPanel().setHeadItem("vehiclelicense",
						currentRef.getRefModel().getValue("vehiclelicense"));
				getBillCardPanel().setHeadItem("pkvehicletype",
						currentRef.getRefModel().getValue("pk_vehicletype"));
				getBillCardPanel().setHeadItem("vvhcltypename",
						currentRef.getRefModel().getValue("vvhcltypename"));
			} else if (strColName.equals("pkvehicletype")) { // 车型
				currentRef = (UIRefPane) e.getSource();
				pk = currentRef.getRefPK();
				getBillCardPanel().setHeadItem("pkvehicletype", pk);
				getBillCardPanel().setHeadItem("vvhcltypename",
						currentRef.getRefName());

			} else if (strColName.equals("vsendstoreorgname")) { // 发货库存组织
				currentRef = ((UIRefPane) getBillCardPanel().getBodyItem(
						strColName).getComponent());
				pk = currentRef.getRefPK();
				getBillCardPanel().setBodyValueAt(pk, currentRow,
						"pksendstockorg");
				getBillCardPanel().setBodyValueAt(currentRef.getRefName(),
						currentRow, "vsendstoreorgname");
			} else if (strColName.equals("vsendstorename")) { // 发货仓库
				currentRef = ((UIRefPane) getBillCardPanel().getBodyItem(
						strColName).getComponent());
				pk = currentRef.getRefPK();
				getBillCardPanel()
						.setBodyValueAt(pk, currentRow, "pksendstock");
				getBillCardPanel().setBodyValueAt(currentRef.getRefName(),
						currentRow, "vsendstorename");
				// 清批次号
				getBillCardPanel().setBodyValueAt(null, currentRow,
						"vbatchcode");
				getLotNumbRefPane().setValue("");
			} else if (strColName.equals("creceiptcorp")) { // 收货单位
				currentRef = ((UIRefPane) getBillCardPanel().getBodyItem(
						strColName).getComponent());
				pk = currentRef.getRefPK();
				getBillCardPanel().setBodyValueAt(pk, currentRow,
						"creceiptcorpid");
				getBillCardPanel().setBodyValueAt(currentRef.getRefName(),
						currentRow, "creceiptcorp");

				// 自动获得到货地区和到货地址
				try {
					String s[] = DelivbillHBO_Client.getDestArea(pk);
					if (s != null && s.length > 0) {
						getBillCardPanel().setBodyValueAt(s[0], currentRow,
								"pkarrivearea");
						getBillCardPanel().setBodyValueAt(s[1], currentRow,
								"vdestarea");
						getBillCardPanel().setBodyValueAt(s[2], currentRow,
								"vdestaddress");
					}
				} catch (Exception ex) {
					dispErrorMessage(ex);
					return;
				}

				// 带出联系人电话
				String linkman1 = null, phone1 = null;
				ArrayList recieveCustLink = DelivbillHBO_Client
						.findRecieveCustlink(pk);
				if (recieveCustLink != null && recieveCustLink.size() > 1) {
					if (recieveCustLink.get(0) != null) {
						linkman1 = recieveCustLink.get(0).toString();
					}
					if (recieveCustLink.get(1) != null) {
						phone1 = recieveCustLink.get(1).toString();
					}
				}
				getBillCardPanel().setBodyValueAt(linkman1, currentRow,
						"linkman1");
				getBillCardPanel().setBodyValueAt(phone1, currentRow, "phone1");

			} else if (strColName.equals("vdestarea")) { // 到货地区
				currentRef = ((UIRefPane) getBillCardPanel().getBodyItem(
						strColName).getComponent());
				getBillCardPanel().setBodyValueAt(currentRef.getRefPK(),
						currentRow, "pkarrivearea");
				getBillCardPanel().setBodyValueAt(currentRef.getRefName(),
						currentRow, "vdestarea");

			} else if (strColName.equals("vdestaddress")) { // 收货地址
				UIRefPane vdestaddressRef = ((UIRefPane) getBillCardPanel()
						.getBodyItem(strColName).getComponent());
				getBillCardPanel().setBodyValueAt(vdestaddressRef.getText(),
						currentRow, "vdestaddress");

			} else if (strColName.equals("vfree0")) { // 自由项
				afterBodyFreeItemEdit(currentRow, getBillCardPanel()
						.getBillModel().getBodyColByKey("vfree0"),
						(DMBillCardPanel) getBillCardPanel());

			} else if (strColName.equals("vbatchcode")) { // 批次
				afterBodyLotEdit(currentRow, getBillCardPanel().getBillModel()
						.getBodyColByKey("vbatchcode"),
						(DMBillCardPanel) getBillCardPanel());

			} else if (sNumItemKey.equals(strColName)) {
				// 数量变化带出体积和重量变化
				afterNumEdit(e);
				this.hasChangedPack = true;
				this.calculatePackNum(e.getRow());
				this.calculatePackVolumn(e.getRow());
			} else if (sAstItemKey.equals(strColName)) {
				afterAstNumEdit(e);
			} else if (strColName.equals("dunitprice")) {
				afterPriceEdit(e);
			} else if (strColName.equals("dmoney")) {
				afterMoneyEdit(e);
			} else if (strColName.equals("h_confirmarrivedate")) {
				afterH_confirmarrivedateEdit(e);
			} else if (strColName.equals("vsendaddress")) { // 发货地点
				this.hasChangedPack = true;
				currentRef = ((UIRefPane) getBillCardPanel().getBodyItem(
						strColName).getComponent());
				String strPK = currentRef.getRefPK();
				getBillCardPanel().setBodyValueAt(strPK, currentRow,
						"pksendaddress");
			} else if (strColName.equals("varriveaddress")) { // 收货地点
				this.hasChangedPack = true;
				currentRef = ((UIRefPane) getBillCardPanel().getBodyItem(
						strColName).getComponent());
				String strPK = currentRef.getRefPK();
				getBillCardPanel().setBodyValueAt(strPK, currentRow,
						"pkarriveaddress");
			} else if (strColName.equals("packsortcode")) { // 包装分类
				this.hasChangedPack = true;
				currentRef = ((UIRefPane) getBillCardPanel().getBodyItem(
						strColName).getComponent());
				String strPK = currentRef.getRefPK();
				getBillCardPanel().setBodyValueAt(strPK, currentRow,
						"pk_packsort");
				String name = currentRef.getRefName();
				getBillCardPanel().setBodyValueAt(name, currentRow,
						"packsortname");
				if (strPK == null) {
					getBillCardPanel().setBodyValueAt(null, currentRow,
							"dpackvolumn");
					getBillCardPanel().setBodyValueAt(null, currentRow,
							"dpacknum");
					getBillCardPanel().setBodyValueAt(null, currentRow,
							"dpackweight");
				} else {
					calculatePackVolumn(e.getRow());
				}
			} else if (strColName.equals("dpackvolumn")) { // 包装体积
				this.hasChangedPack = true;
				checkHasPackSort(e.getRow());
			} else if (strColName.equals("dpackweight")) { // 包装重量
				this.hasChangedPack = true;
				checkHasPackSort(e.getRow());
			} else if (strColName.equals("dpacknum")) { // 包装件数
				checkHasPackSort(e.getRow());
				calculatePackVolumn(e.getRow());
				this.hasChangedPack = true;
			}

			// 对发运来讲，不能编辑 报价数量 的值，报价数量的值只有在数量变化的时候进行计算
			// else if (strColName.equals("nquoteunitnum")) {
			// afterQuoteUnitNumEdit(e);
			// }
		} catch (Exception e1) {
			this.showErrorMessage(e1.toString());
			handleException(e1);
		}
	}

	private void checkHasPackSort(int row) {
		Object oTemp = getBillCardPanel().getBodyValueAt(row, "pk_packsort");
		if (oTemp == null || oTemp.toString().trim().length() == 0) {
			getBillCardPanel().setBodyValueAt(null, row, "dpackvolumn");
			getBillCardPanel().setBodyValueAt(null, row, "dpacknum");
			getBillCardPanel().setBodyValueAt(null, row, "dpackweight");
		}
	}

	/**
	 * 根据存货数量计算包装件数
	 * 
	 * @param row
	 *            int
	 */
	private void calculatePackNum(int row) {
		Object oTemp = getBillCardPanel().getBodyValueAt(row, "dinvnum");
		if (oTemp == null || oTemp.toString().trim().length() == 0) {
			return;
		}
		UFDouble dinvnum = new UFDouble(oTemp.toString());
		oTemp = getBillCardPanel().getBodyValueAt(row, "pkinv");
		if (oTemp == null || oTemp.toString().trim().length() == 0) {
			return;
		}
		String pkinv = oTemp.toString();
		oTemp = getBillCardPanel().getBodyValueAt(row, "pk_packsort");
		if (oTemp == null || oTemp.toString().trim().length() == 0) {
			return;
		}
		String pk_packsort = oTemp.toString();
		UFDouble packNum = getPackNum(pk_packsort, pkinv);
		if (packNum == null) {
			return;
		}
		UFDouble dPackNum = dinvnum.div(packNum);
		dPackNum = dPackNum.setScale(-this.BD501.intValue(),
				UFDouble.ROUND_HALF_UP);
		getBillCardPanel().setBodyValueAt(dPackNum, row, "dpacknum");

	}

	/**
	 * 根据存货和包装分类得到包装分类需要的存货数量
	 * 
	 * @param pk_packsort
	 *            String
	 * @return UFDouble
	 */
	private UFDouble getPackNum(String pk_packsort, String cinvnetoryid) {
		String key = pk_packsort + cinvnetoryid;
		UFDouble ratio = null;
		if (this.packsort_numIndex.containsKey(key)) {
			ratio = (UFDouble) this.packsort_numIndex.get(key);
		} else {
			StringBuffer sql = new StringBuffer();
			sql.append(" select ic_wholepack.npacknum from ");
			sql.append(" ic_wholepack inner join ic_packtype on ");
			sql.append(" ic_wholepack.cpacktypeid = ic_packtype.cpacktypeid  ");
			sql.append(" where ic_wholepack. cinventoryid= '");
			sql.append(cinvnetoryid);
			sql.append("' and pk_packsort ='");
			sql.append(pk_packsort);
			sql.append("' ");
			DMDataVO[] result = null;
			try {
				result = DmHelper.queryStringBuffer(sql);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			int size = result.length;
			if (size > 0) {
				Object obj = result[0].getAttributeValue("npacknum");
				if (obj == null) {
					ratio = null;
				} else {
					ratio = new UFDouble(obj.toString());
				}
			}
			this.packsort_numIndex.put(key, ratio);
		}
		return ratio;
	}

	/**
	 * 根据包装件数计算体积
	 * 
	 * @param row
	 *            int
	 */
	private void calculatePackVolumn(int row) {
		Object oTemp = getBillCardPanel().getBodyValueAt(row, "dpacknum");
		if (oTemp == null || oTemp.toString().trim().length() == 0) {
			return;
		}
		UFDouble dpacknum = new UFDouble(oTemp.toString());
		oTemp = getBillCardPanel().getBodyValueAt(row, "pk_packsort");
		if (oTemp == null || oTemp.toString().trim().length() == 0) {
			return;
		}
		UFDouble ratio = getPackRatio(oTemp.toString());
		if (ratio == null) {
			return;
		}
		UFDouble dpackvolumn = dpacknum.multiply(ratio);
		dpackvolumn = dpackvolumn.setScale(-this.BD501.intValue(),
				UFDouble.ROUND_HALF_UP);

		getBillCardPanel().setBodyValueAt(dpackvolumn, row, "dpackvolumn");
	}

	/**
	 * 根据包装分类得到体积
	 * 
	 * @param pk_packsort
	 *            String
	 * @return UFDouble
	 */
	private UFDouble getPackRatio(String pk_packsort) {
		UFDouble ratio = null;
		if (this.packsort_volumnIndex.containsKey(pk_packsort)) {
			ratio = (UFDouble) this.packsort_volumnIndex.get(pk_packsort);
		} else {
			StringBuffer sql = new StringBuffer();
			sql.append(" select packvolumn from dm_packsort where ");
			sql.append(" pk_packsort ='");
			sql.append(pk_packsort);
			sql.append("' ");
			DMDataVO[] result = null;
			try {
				result = DmHelper.queryStringBuffer(sql);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			int size = result.length;
			if (size > 0) {
				Object obj = result[0].getAttributeValue("packvolumn");
				if (obj == null) {
					ratio = null;
				} else {
					ratio = new UFDouble(obj.toString());
				}
			}
			this.packsort_volumnIndex.put(pk_packsort, ratio);
		}
		return ratio;
	}

	/**
	 * 当表头修改 h_confirmarrivedate 字段时，自动将表体该字段填上相等的值。
	 * <p>
	 * 为了输入方便，在表头部分特设置一字段“承运商承诺到货日期”， 在查询出发运单时，取表体部分第一条记录的该字段值赋于之； 当将该字段后的值修改为 d
	 * 后，则将表体部分所有行的该字段值修改为d； 当然，表体部分该字段的值可以单独修改。
	 * 
	 * @param event
	 *            单据编辑事件
	 * @author zxping
	 */
	private void afterH_confirmarrivedateEdit(BillEditEvent event) {
		// 得到表头字段“承运商承诺到货日期”的值
		String str = getBillCardPanel().getHeadItem("h_confirmarrivedate")
				.getValue();

		UFDate ufDate = null;
		if (null != str && str.trim().length() > 0) {
			ufDate = new UFDate(str.trim());
		}

		for (int i = getBillCardPanel().getRowCount() - 1; i >= 0; i--) {
			// 将表体行的“承运商承诺到货日期”字段值修改为跟表头相同字段的值相等
			getBillCardPanel().setBodyValueAt(ufDate, i, "confirmarrivedate");

			// 由于直接给单元格赋值不会自动更改 BillModel 的行状态，所以需要明确对其行状态进行改变
			if (getBillCardPanel().getBillModel().getRowState(i) == BillModel.NORMAL) {
				getBillCardPanel().getBillModel().setRowState(i,
						BillModel.MODIFICATION);
			}
		}

	}

	/**
	 * 金额变化后触发的事件。
	 * 
	 * @param event
	 *            单据编辑事件
	 * @author zxj
	 */
	private void afterMoneyEdit(BillEditEvent event) {
		int row = event.getRow();

		UFDouble nInvNum = null; // 数量
		UFDouble nMoney = null; // 金额

		Object oObject = getBillCardPanel().getBodyValueAt(row, "dmoney");
		if (oObject != null && oObject.toString().trim().length() > 0) {
			// 金额不为空
			nMoney = new UFDouble(oObject.toString());
			oObject = getBillCardPanel().getBodyValueAt(row, "dinvnum");
			if (oObject != null && oObject.toString().trim().length() > 0) {
				// 数量不为空
				nInvNum = new UFDouble(oObject.toString());
				if (nInvNum.doubleValue() == 0) {
					// 数量为0,则单价为空
					getBillCardPanel().setBodyValueAt(null, row, "dunitprice");
				} else {
					// 数量不为0
					double nprice = nMoney.doubleValue()
							/ nInvNum.doubleValue();
					getBillCardPanel().setBodyValueAt(new UFDouble(nprice),
							row, "dunitprice");
				}
			} else {
				// 数量为空，则单价为空
				getBillCardPanel().setBodyValueAt(null, row, "dunitprice");
			}
		} else {
			// 金额为空，则单价为空
			getBillCardPanel().setBodyValueAt(null, row, "dunitprice");
		}
	}

	/**
	 * 关闭发运单数量改变事件处理
	 * 
	 * @param e
	 *            单据编辑事件
	 * @author 余大英
	 */
	public void afterNumChangeByEnd() {
		DelivbillHItemVO[] items = (DelivbillHItemVO[]) m_currentbill
				.getChildrenVO();
		for (int i = 0; i < items.length; i++) {
			if (items[i].getDoutnum() != null
					&& items[i].getDinvnum().compareTo(items[i].getDoutnum()) != 0) {
				items[i].setDinvnum(items[i].getDoutnum());
			} else {
				continue;
			}

			// int row = i;

			// 数量变化带出体积和重量变化
			String s[] = new String[] {
					"pkbasinv->getColValue(bd_invmandoc,pk_invbasdoc,pk_invmandoc,pkinv)",
					"dinvweight->getColValue(bd_invbasdoc,unitweight,pk_invbasdoc,pkbasinv)",
					"dinvweight->dinvnum*dinvweight",
					"dvolumn->getColValue(bd_invbasdoc,unitvolume,pk_invbasdoc,pkbasinv)",
					"dvolumn->dinvnum*dvolumn" };
			nc.vo.pub.SuperVOUtil
					.execFormulaWithVOs(
							new CircularlyAccessibleValueObject[] { items[i] },
							s, null);
			// getBillCardPanel().getBillModel().execFormulas(row, s);

			// Modified by xhq 2002/10/07 begin
			// 数量变化，金额也变化
			UFDouble nInvNum = null; // 数量
			UFDouble nUnitPrice = null; // 单价
			// Object oObject = getBillCardPanel().getBodyValueAt(row,
			// "dinvnum");
			Object oObject = items[i].getAttributeValue("dinvnum");
			if (oObject != null && oObject.toString().trim().length() > 0) {
				// 数量不为空
				nInvNum = new UFDouble(oObject.toString());
				// oObject = getBillCardPanel().getBodyValueAt(row,
				// "dunitprice");
				oObject = items[i].getAttributeValue("dunitprice");
				if (oObject != null && oObject.toString().trim().length() > 0) {
					// 单价不为空
					nUnitPrice = new UFDouble(oObject.toString());
					double nmny = nInvNum.doubleValue()
							* nUnitPrice.doubleValue();
					items[i].setAttributeValue("dmoney", new UFDouble(nmny));
					// getBillCardPanel().setBodyValueAt(new UFDouble(nmny),
					// row,
					// "dmoney");
				} else {
					// 单价为空
					oObject = items[i].getAttributeValue("dmoney");
					// oObject = getBillCardPanel().getBodyValueAt(row,
					// "dmoney");
					if (oObject != null
							&& oObject.toString().trim().length() > 0) {
						// 金额不为空，计算单价
						UFDouble nMoney = new UFDouble(oObject.toString());
						if (nInvNum.doubleValue() == 0) {
							// 数量为0，则金额为空
							items[i].setAttributeValue("dmoney", null);
							// getBillCardPanel().setBodyValueAt(null, row,
							// "dmoney");
						} else {
							// 数量不为0
							double nprice = nMoney.doubleValue()
									/ nInvNum.doubleValue();
							items[i].setAttributeValue("dunitprice",
									new UFDouble(nprice));
							// getBillCardPanel().setBodyValueAt(new
							// UFDouble(nprice), row,
							// "dunitprice");
						}
					} else {
						// 金额为空
					}
				}
			} else {
				// 数量为空，则金额为空
				items[i].setAttributeValue("dmoney", null);
				// getBillCardPanel().setBodyValueAt(null, row, "dmoney");
			}
			// Modified by xhq 2002/10/07 end

			Object oTemp = null;
			oTemp = items[i].getAttributeValue(pkinv);
			// oTemp = getBillCardPanel().getBodyValueAt(row, pkinv);
			if (oTemp == null) {
				SCMEnv.info("no inv id");
				continue;
				// return;
			}
			String sInvID = oTemp.toString();
			oTemp = items[i].getAttributeValue(pkassistmeasure);
			// oTemp = getBillCardPanel().getBodyValueAt(row, pkassistmeasure);
			String sAstID = null;
			if (oTemp != null) {
				sAstID = oTemp.toString();
			}
			InvVO voaInv[] = getInvInfo(new String[] { sInvID },
					new String[] { sAstID });
			if (voaInv == null || voaInv.length == 0 || voaInv[0] == null) {
				SCMEnv.info("no inv vo");
				continue;
				// return;
			}
			InvVO invvo = voaInv[0];
			// -------------------------
			Integer isAstMgt = (Integer) invvo.getIsAstUOMmgt(); // 辅计量管理？
			Integer isFixFlag = (Integer) invvo.getIsSolidConvRate(); // 固定换算率
			UFDouble hsl = invvo.getHsl();
			if (isAstMgt == null || isAstMgt.intValue() == 0) {
				SCMEnv.info("not ast mgt");
				continue;
				// return;
			}

			/** 数量清空：if 固定换算率：清空辅数量。 */
			Object oNumValue = items[i].getAttributeValue(sNumItemKey);
			// Object oNumValue = getBillCardPanel().getBodyValueAt(row,
			// sNumItemKey);
			if (oNumValue == null || oNumValue.toString().trim().length() < 1) {
				items[i].setAttributeValue(sNumItemKey, null);
				// getBillCardPanel().setBodyValueAt(null, row, sNumItemKey);
				// 固定换算率清辅数量返回
				if (isFixFlag != null && isFixFlag.intValue() == 1) {
					items[i].setAttributeValue(sAstItemKey, null);
					// getBillCardPanel().setBodyValueAt(null, row,
					// sAstItemKey);
				}
				/** If 变动换算率，清空换算率 */
				if (isFixFlag != null && isFixFlag.intValue() == 0) {
					// 重算存货的换算率
					invvo.setHsl(null);
					// 更新存货vo
					updateInvInfo(invvo);
				}
				continue;
				// return;
			}
			UFDouble nNum = new UFDouble(oNumValue.toString().trim());

			if (isAstMgt == null || isAstMgt.intValue() == 0
					|| isFixFlag == null) {
				continue;
			}
			// return;
			/** if 为 固定换算率，数量修改，则重算辅数量。 */

			// //如果是固定换算率或辅数量为空，推算辅数量
			if (isFixFlag.intValue() == 1
					|| items[i].getAttributeValue(sAstItemKey) == null) {
				// if (isFixFlag.intValue() == 1 ||
				// getBillCardPanel().getBodyValueAt(row, sAstItemKey) == null)
				// {

				if (hsl != null && hsl.doubleValue() != 0.0) {
					UFDouble ninassistnum = nNum.div(hsl);
					items[i].setAttributeValue(sAstItemKey, ninassistnum);
					// getBillCardPanel().setBodyValueAt(ninassistnum, row,
					// sAstItemKey);
				}

			}
			/* If 为变动换算率，数量修改，不影响辅数量，但要重算换算率 */
			if (isFixFlag.intValue() == 0
					&& items[i].getAttributeValue(sAstItemKey) != null) {
				// if (isFixFlag.intValue() == 0 &&
				// getBillCardPanel().getBodyValueAt(row, sAstItemKey) != null)
				// {
				Object oAstNum = items[i].getAttributeValue(sAstItemKey);
				// Object oAstNum = getBillCardPanel().getBodyValueAt(row,
				// sAstItemKey);
				UFDouble nastnum = new UFDouble(0);
				if (null != oAstNum && oAstNum.toString().trim().length() != 0) {
					nastnum = (UFDouble) oAstNum;
				} else {
				}

				if (nNum != null && nastnum.doubleValue() != 0.0) {
					UFDouble dhsl = nNum.div(nastnum);

					// added by zxping
					// dTransRate = dhsl.doubleValue();

					if (dhsl != null && dhsl.doubleValue() >= 0) {
						// 重算存货的换算率
						invvo.setHsl(dhsl);
						// 更新存货vo
						updateInvInfo(invvo);
					} else {
						items[i].setAttributeValue(sNumItemKey, null);
						// getBillCardPanel().setBodyValueAt(null, row,
						// sNumItemKey);
					}
				}
			}
		}

	}

	/**
	 * 数量改变事件处理
	 * 
	 * @param e
	 *            单据编辑事件
	 * @author 余大英
	 */
	public void afterNumEdit(nc.ui.pub.bill.BillEditEvent e) {

		int row = e.getRow();

		// 数量变化带出体积和重量变化
		String s[] = new String[] {
				"pkbasinv->getColValue(bd_invmandoc,pk_invbasdoc,pk_invmandoc,pkinv)",
				"dinvweight->toNumber(getColValue(bd_invbasdoc,unitweight,pk_invbasdoc,pkbasinv))",
				"dinvweight->dinvnum*dinvweight",
				"dvolumn->toNumber(getColValue(bd_invbasdoc,unitvolume,pk_invbasdoc,pkbasinv))",
				"dvolumn->dinvnum*dvolumn",
				// 对发运来讲，不能编辑 报价数量 的值，报价数量的值只有在数量变化的时候进行计算
				"nquoteunitnum->dinvnum/nquoteunitrate" };

		getBillCardPanel().getBillModel().execFormulas(row, s);

		// Modified by xhq 2002/10/07 begin
		// 数量变化，金额也变化
		UFDouble nInvNum = null; // 数量
		UFDouble nUnitPrice = null; // 单价
		Object oObject = getBillCardPanel().getBodyValueAt(row, "dinvnum");
		if (oObject != null && oObject.toString().trim().length() > 0) {
			// 数量不为空
			nInvNum = new UFDouble(oObject.toString());
			oObject = getBillCardPanel().getBodyValueAt(row, "dunitprice");
			if (oObject != null && oObject.toString().trim().length() > 0) {
				// 单价不为空
				nUnitPrice = new UFDouble(oObject.toString());
				double nmny = nInvNum.doubleValue() * nUnitPrice.doubleValue();
				getBillCardPanel().setBodyValueAt(new UFDouble(nmny), row,
						"dmoney");
			} else {
				// 单价为空
				oObject = getBillCardPanel().getBodyValueAt(row, "dmoney");
				if (oObject != null && oObject.toString().trim().length() > 0) {
					// 金额不为空，计算单价
					UFDouble nMoney = new UFDouble(oObject.toString());
					if (nInvNum.doubleValue() == 0) {
						// 数量为0，则金额为空
						getBillCardPanel().setBodyValueAt(null, row, "dmoney");
					} else {
						// 数量不为0
						double nprice = nMoney.doubleValue()
								/ nInvNum.doubleValue();
						getBillCardPanel().setBodyValueAt(new UFDouble(nprice),
								row, "dunitprice");
					}
				} else {
					// 金额为空
				}
			}
		} else {
			// 数量为空，则金额为空
			getBillCardPanel().setBodyValueAt(null, row, "dmoney");
		}
		// Modified by xhq 2002/10/07 end

		Object oTemp = null;
		oTemp = getBillCardPanel().getBodyValueAt(row, pkinv);
		if (oTemp == null) {
			SCMEnv.info("no inv id");
			return;
		}
		String sInvID = oTemp.toString();
		oTemp = getBillCardPanel().getBodyValueAt(row, pkassistmeasure);
		String sAstID = null;
		if (oTemp != null) {
			sAstID = oTemp.toString();
		}
		InvVO voaInv[] = getInvInfo(new String[] { sInvID },
				new String[] { sAstID });
		if (voaInv == null || voaInv.length == 0 || voaInv[0] == null) {
			SCMEnv.info("no inv vo");
			return;
		}
		InvVO invvo = voaInv[0];
		// -------------------------
		Integer isAstMgt = (Integer) invvo.getIsAstUOMmgt(); // 辅计量管理？
		Integer isFixFlag = (Integer) invvo.getIsSolidConvRate(); // 固定换算率
		UFDouble hsl = invvo.getHsl();
		if (isAstMgt == null || isAstMgt.intValue() == 0) {
			SCMEnv.info("not ast mgt");
			return;
		}

		/** 数量清空：if 固定换算率：清空辅数量。 */
		Object oNumValue = getBillCardPanel().getBodyValueAt(row, sNumItemKey);
		if (oNumValue == null || oNumValue.toString().trim().length() < 1) {
			getBillCardPanel().setBodyValueAt(null, row, sNumItemKey);
			// 固定换算率清辅数量返回
			if (isFixFlag != null && isFixFlag.intValue() == 1) {
				getBillCardPanel().setBodyValueAt(null, row, sAstItemKey);
			}
			/** If 变动换算率，清空换算率 */
			if (isFixFlag != null && isFixFlag.intValue() == 0) {
				// 重算存货的换算率
				invvo.setHsl(null);
				// 更新存货vo
				updateInvInfo(invvo);
			}
			return;
		}
		UFDouble nNum = new UFDouble(oNumValue.toString().trim());

		if (isAstMgt == null || isAstMgt.intValue() == 0 || isFixFlag == null) {
			return;
		}
		/** if 为 固定换算率，数量修改，则重算辅数量。 */

		// //如果是固定换算率或辅数量为空，推算辅数量
		if (isFixFlag.intValue() == 1
				|| getBillCardPanel().getBodyValueAt(row, sAstItemKey) == null) {

			if (hsl != null && hsl.doubleValue() != 0.0) {
				UFDouble ninassistnum = nNum.div(hsl);
				getBillCardPanel().setBodyValueAt(ninassistnum, row,
						sAstItemKey);
			}

		}
		/* If 为变动换算率，数量修改，不影响辅数量，但要重算换算率 */
		if (isFixFlag.intValue() == 0
				&& getBillCardPanel().getBodyValueAt(row, sAstItemKey) != null) {
			Object oAstNum = getBillCardPanel()
					.getBodyValueAt(row, sAstItemKey);
			UFDouble nastnum = new UFDouble(0);
			if (null != oAstNum && oAstNum.toString().trim().length() != 0) {
				nastnum = (UFDouble) oAstNum;
			} else {
			}

			if (nNum != null && nastnum.doubleValue() != 0.0) {
				UFDouble dhsl = nNum.div(nastnum);

				if (dhsl != null && dhsl.doubleValue() >= 0) {
					// 重算存货的换算率
					invvo.setHsl(dhsl);
					// 更新存货vo
					updateInvInfo(invvo);
				} else {
					getBillCardPanel().setBodyValueAt(null, row, sNumItemKey);
				}
			}
		}

	}

	/**
	 * 单价变化，金额变化
	 * 
	 * @param event
	 *            单据编辑事件
	 */
	private void afterPriceEdit(BillEditEvent event) {
		int row = event.getRow();

		UFDouble nInvNum = null; // 数量
		UFDouble nUnitPrice = null; // 单价

		Object oObject = getBillCardPanel().getBodyValueAt(row, "dunitprice");
		if (oObject != null && oObject.toString().trim().length() > 0) {
			// 单价不为空
			nUnitPrice = new UFDouble(oObject.toString());
			oObject = getBillCardPanel().getBodyValueAt(row, "dinvnum");
			if (oObject != null && oObject.toString().trim().length() > 0) {
				// 数量不为空
				nInvNum = new UFDouble(oObject.toString());
				double nmny = nInvNum.doubleValue() * nUnitPrice.doubleValue();
				getBillCardPanel().setBodyValueAt(new UFDouble(nmny), row,
						"dmoney");
			} else {
				// 数量为空，则金额为空
				getBillCardPanel().setBodyValueAt(null, row, "dmoney");
			}
		} else {
			// 单价为空，则金额为空
			getBillCardPanel().setBodyValueAt(null, row, "dmoney");
		}
	}

	/**
	 * 报价计量单位数量
	 * 
	 * @param event
	 *            单据编辑事件
	 */
	// private void afterQuoteUnitNumEdit(BillEditEvent event) {
	//
	// }
	/**
	 * 编辑前事件处理。
	 * 
	 * @param e
	 *            单据编辑事件
	 * @return boolean
	 */
	public boolean beforeEdit(BillEditEvent e) {
		if (e.getPos() == BillItem.BODY) {
			// 收货地址
			if (e.getKey().equals("vdestaddress")) {
				UIRefPane vdestaddress = (UIRefPane) getBillCardPanel()
						.getBodyItem("vdestaddress").getComponent();
				((nc.ui.scm.ref.prm.CustAddrRefModel) vdestaddress
						.getRefModel()).setCustId((String) getBillCardPanel()
						.getBodyValueAt(e.getRow(), "creceiptcorpid"));
			}
		}

		return true;
	}

	/**
	 * //1、检查表体行到货地区是否在路线上 //2、表体所有发货地点一致，即是说：要求表体所有发货地点为选中路线的起点
	 * 
	 * @return boolean
	 */
	public void checkItemsInRoute(DelivbillHItemVO[] items, String pkroute)
			throws BusinessException, Exception {
		if (pkroute == null) {
			throw new BusinessException(NCLangRes.getInstance().getStrByID(
					"40140408", "UPP40140408-000058") /* @res "表头发运路线不能为空" */);
		}
		// 检查是否在路线上
		ArrayList routeArray = DelivbillHBO_Client.findRouteDetail(pkroute);
		if (routeArray == null) {
			throw new BusinessException(NCLangRes.getInstance().getStrByID(
					"40140408", "UPP40140408-000059") /* @res "表头发运路线缺少表体行" */);
			// return false;
		}

		// 哈希表存放路线每一站
		java.util.Hashtable routeHash = new java.util.Hashtable();
		for (int i = 0; i < routeArray.size(); i++) {
			Object[] oneRouteaddr = (Object[]) routeArray.get(i);
			if (oneRouteaddr.length > 0 && oneRouteaddr[0] != null) {
				routeHash.put(oneRouteaddr[0], "Y");
			}
		}

		// 如下的检查，需要由参数控制
		if (null != DM012 && DM012.booleanValue()) {
			// 检查每一行是否在路线上
			for (int i = 0; i < items.length; i++) {
				String pkarrivearea = null;
				if (items[i] != null
						&& items[i].getStatus() != nc.vo.pub.VOStatus.DELETED
						&& items[i].getAttributeValue("pkarriveaddress") != null) {
					pkarrivearea = items[i]
							.getAttributeValue("pkarriveaddress").toString();
					if (routeHash.containsKey(pkarrivearea) == false) {
						throw new BusinessException(NCLangRes.getInstance()
								.getStrByID("40140408", "UPP40140408-000060") /*
																			 * @res
																			 * "表体到货地点不在发运路线上"
																			 */);
						// return false;
					}
				}
			}
		}

		// 检查表体所有发货地点为选中路线的起点
		for (int i = 0; i < items.length; i++) {
			String pksendaddress;
			if (items[i] != null
					&& items[i].getStatus() != nc.vo.pub.VOStatus.DELETED
					&& items[i].getAttributeValue("pksendaddress") != null) {
				pksendaddress = items[i].getAttributeValue("pksendaddress")
						.toString();

				if (!pksendaddress.equals(((Object[]) routeArray.get(0))[0])) {
					throw new BusinessException(NCLangRes.getInstance()
							.getStrByID("40140408", "UPP40140408-000061") /*
																		 * @res
																		 * "表体发货地点不是路线的起点"
																		 */);
					// return false;
				}
			}
		}

	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-6-27 16:16:11)
	 */
	public boolean checkOther(AggregatedValueObject vo) throws Exception {
		String sAllErrorMessage = "";

		// 是否进行超重检查
		if (DM003.booleanValue()) {
			Object oVehiclePK = vo.getParentVO().getAttributeValue("pkvehicle");
			if (oVehiclePK != null
					&& oVehiclePK.toString().trim().length() != 0) {
				UFDouble ufdSumTotalWeight = new UFDouble(0);
				for (int i = 0; i < vo.getChildrenVO().length; i++) {
					CircularlyAccessibleValueObject cavo = vo.getChildrenVO()[i];
					Object oCalculateWeight = cavo
							.getAttributeValue("dinvweight");
					if (null != oCalculateWeight
							&& oCalculateWeight.toString().trim().length() != 0) {
						ufdSumTotalWeight = ufdSumTotalWeight.add(new UFDouble(
								oCalculateWeight.toString()));
					}
				}
				// 取得车辆的载重
				nc.vo.dm.dm006.DmVehicleItemVO vvo = nc.ui.dm.dm006.VehicleHelper
						.findByPrimaryKey(oVehiclePK.toString());
				Object oVehicleWeight = vvo.getAttributeValue("dload");
				// 检查是否超标
				if (null != oVehicleWeight
						&& oVehicleWeight.toString().trim().length() != 0) {
					UFDouble ufdVehicleWeight = new UFDouble(oVehicleWeight
							.toString());
					if (ufdSumTotalWeight.doubleValue() > ufdVehicleWeight
							.doubleValue()) {
						String[] value = new String[] {
								ufdVehicleWeight.toString(),
								ufdSumTotalWeight.toString() };
						String sErrorMessage = NCLangRes.getInstance()
								.getStrByID("40140408", "UPP40140408-000108",
										null, value) /*
													 * @res
													 * "重量超出！承重为:{0}，现重为:{1}."
													 */;
						sAllErrorMessage = sAllErrorMessage + sErrorMessage
								+ "\n";
					}
				} else if (ufdSumTotalWeight.doubleValue() > 0) {
					String[] value = new String[] { ufdSumTotalWeight
							.toString() };
					String sErrorMessage = NCLangRes.getInstance().getStrByID(
							"40140408", "UPP40140408-000109", null, value) /*
																			 * @res
																			 * "重量超出！承重未定义，现重为:{0}."
																			 */;
					sAllErrorMessage = sAllErrorMessage + sErrorMessage + "\n";
				}
			}
		}

		// 是否进行超体积的检查
		if (DM004.booleanValue()) {
			Object oVehiclePK = vo.getParentVO().getAttributeValue("pkvehicle");
			if (oVehiclePK != null
					&& oVehiclePK.toString().trim().length() != 0) {
				UFDouble ufdSumTotalVolumn = new UFDouble(0);
				for (int i = 0; i < vo.getChildrenVO().length; i++) {
					CircularlyAccessibleValueObject cavo = vo.getChildrenVO()[i];
					Object oCalculateVolumn = cavo.getAttributeValue("dvolumn");
					if (null != oCalculateVolumn
							&& oCalculateVolumn.toString().trim().length() != 0) {
						ufdSumTotalVolumn = ufdSumTotalVolumn.add(new UFDouble(
								oCalculateVolumn.toString()));
					}
				}
				// 取得车辆的容积
				nc.vo.dm.dm006.DmVehicleItemVO vvo = nc.ui.dm.dm006.VehicleHelper
						.findByPrimaryKey(oVehiclePK.toString());
				Object oVehicleVolumn = vvo.getAttributeValue("dcubage");
				// 检查是否超标
				if (null != oVehicleVolumn
						&& oVehicleVolumn.toString().trim().length() != 0) {
					UFDouble ufdVehicleVolumn = new UFDouble(oVehicleVolumn
							.toString());
					if (ufdSumTotalVolumn.doubleValue() > ufdVehicleVolumn
							.doubleValue()) {
						String[] value = new String[] {
								ufdVehicleVolumn.toString(),
								ufdSumTotalVolumn.toString() };
						String sErrorMessage = NCLangRes.getInstance()
								.getStrByID("40140408", "UPP40140408-000110",
										null, value) /*
													 * @res
													 * "体积超出！有效容积为:{0}，现体积为:{1}."
													 */;
						sAllErrorMessage = sAllErrorMessage + sErrorMessage
								+ "\n";
					}
				} else if (ufdSumTotalVolumn.doubleValue() > 0) {
					String[] value = new String[] { ufdSumTotalVolumn
							.toString() };
					String sErrorMessage = NCLangRes.getInstance().getStrByID(
							"40140408", "UPP40140408-000111", null, value) /*
																			 * @res
																			 * "体积超出！有效容积未定义，现体积为:{0}."
																			 */;
					sAllErrorMessage = sAllErrorMessage + sErrorMessage + "\n";
				}
			}
		}

		// 报错，退出
		if (sAllErrorMessage.trim().length() != 0) {
			showErrorMessage(sAllErrorMessage);
			return false;
		}

		return true;
	}

	/**
	 * 此处插入方法说明。 <li>----检查：如果“承运商”为空，则表体的所有“承诺到货日期”不能为空；</li> <li>
	 * ----检查：如果表头有“承运商”字段，则表体部分的“承诺到货日期”是否都大于表头的“发运日期”； <li> <li>
	 * ----检查：表体行到货地区是否在路线上，发运路线不能为空；</li> <li>----确信：表体行运输仓和表头输入车型两者并不同时存在；</li>
	 * 创建日期：(2002-6-27 16:16:11)
	 */
	private void checkVOs(DelivbillHVO[] VOsForChek) throws BusinessException,
			Exception {
		String sCheckBill = "";
		DelivbillHVO vo = null;
		DelivbillHHeaderVO head = null;
		nc.vo.dm.dm104.DelivbillHItemVO[] body = null;
		int i = 0;
		try {
			for (i = 0; i < VOsForChek.length; i++) {
				if (VOsForChek.length > 1) {
					String[] value = new String[] { String.valueOf(i + 1) };
					sCheckBill = NCLangRes.getInstance().getStrByID("40140408",
							"UPP40140408-000112", null, value) /*
																 * @res
																 * "第{0}张发运单"
																 */;
				}
				vo = (DelivbillHVO) VOsForChek[i];
				head = (DelivbillHHeaderVO) vo.getParentVO();
				body = (DelivbillHItemVO[]) vo.getChildrenVO();

				if (vo.getChildrenVO() == null
						|| vo.getChildrenVO().length == 0) {
					throw new BusinessException(NCLangRes.getInstance()
							.getStrByID("40140408", "UPP40140408-000062") /*
																		 * @res
																		 * "表体不可为空"
																		 */);
				}

				int iBodyLen = vo.getChildrenVO().length;
				for (int j = 0; j < iBodyLen; j++) {
					UFDouble nNum = new UFDouble(0);
					UFDouble nPrice = new UFDouble(0);

					Object oTemp = body[j].getDinvnum(); // 数量
					if (oTemp != null && oTemp.toString().trim().length() > 0) {
						nNum = new UFDouble(oTemp.toString());
					}
					if (nNum.doubleValue() < 0) {
						throw new BusinessException(NCLangRes.getInstance()
								.getStrByID("40140408", "UPP40140408-000063") /*
																			 * @res
																			 * "表体数量不可为负！"
																			 */);
					}

					oTemp = body[j].getDunitprice(); // 单价
					if (oTemp != null && oTemp.toString().trim().length() > 0) {
						nPrice = new UFDouble(oTemp.toString());

						// 金额 = 数量 *单价
					}
					body[j].setDmoney(new UFDouble(nNum.doubleValue()
							* nPrice.doubleValue()));

					// 发货，到货地点不能为空
					Object vsendaddress = body[j]
							.getAttributeValue("vsendaddress");
					Object varriveaddress = body[j]
							.getAttributeValue("varriveaddress");
					if (vsendaddress == null
							|| vsendaddress.toString().trim().length() == 0) {
						throw new BusinessException(NCLangRes.getInstance()
								.getStrByID("40140408", "UPP40140408-000066") /*
																			 * @res
																			 * "表体发货地点不能为空！"
																			 */);

					}
					if (varriveaddress == null
							|| varriveaddress.toString().trim().length() == 0) {
						throw new BusinessException(NCLangRes.getInstance()
								.getStrByID("40140408", "UPP40140408-000067") /*
																			 * @res
																			 * "表体到货地点不能为空！"
																			 */);
					}

				}
				// 如果表头的“承运商”为空，则表体的所有“承诺到货日期”都应该为空
				String pktrancust = head.getPktrancust();
				if (pktrancust == null || pktrancust.trim().length() == 0) {
					for (int j = 0; j < iBodyLen; j++) {
						Object date = body[j].getConfirmarrivedate();
						if (date != null && date.toString().trim().length() > 0) {
							throw new BusinessException(NCLangRes.getInstance()
									.getStrByID("40140408",
											"UPP40140408-000064") /*
																 * @res
																 * "表头承运商空，表体承诺到货日期应为空！"
																 */);
						}
					}
				}
				// 如果表头有“承运商”字段，则判断表体部分的“承诺到货日期”是否都大于表头的“发运日期”
				UFDate date1 = head.getSenddate();
				if (pktrancust != null && pktrancust.trim().length() > 0
						&& date1 != null
						&& date1.toString().trim().length() > 0) {
					for (int j = 0; j < iBodyLen; j++) {
						Object date = body[j].getConfirmarrivedate();
						if (date != null && date.toString().trim().length() > 0) {
							UFDate date2 = new UFDate(date.toString());
							if (date1.compareTo(date2) > 0) {
								throw new BusinessException(NCLangRes
										.getInstance().getStrByID("40140408",
												"UPP40140408-000065") /*
																	 * @res
																	 * "表头承诺到货日期应大于发运日期！"
																	 */);
							}
						}
					}
				}
				// 来源于采购的单据运费类别不能为“代垫运费”
				if (body[0].getVbilltype() != null
						&& body[0].getVbilltype().equals("21")) {
					if (head.getIsendtype().intValue() == FreightType.pre) {
						throw new BusinessException("来源于采购的发运单运费类别不能为代垫运费");
					}
				}
				if (head.getAttributeValue("startdate") != null
						&& head.getAttributeValue("returndate") != null) {
					if (!head.getStartdate().before(head.getReturndate())) {
						throw new BusinessException(NCLangRes.getInstance()
								.getStrByID("40140408", "UPP40140408-000216") /*
																			 * @res
																			 * "返回日期应晚于发车日期！"
																			 */);
					}
				}

				// 1、检查表体行到货地区是否在路线上
				// 2、表体所有发货地点一致，即是说：要求表体所有发货地点为选中路线的起点
				// 需要注意：发运路线不能为空
				// checkItemsInRoute(body, head.getPkdelivroute()); eric
				// 2012-8-14 取消发运路线校验

				// 如果表体行存在运输仓而表头输入车型，报错
				if (head.getPkvehicle() != null
						&& head.getPkvehicle().toString().trim().length() != 0) {
					for (int j = 0; j < iBodyLen; j++) {
						if (body[j].getPk_transcontainer() != null
								&& body[j].getPk_transcontainer().toString()
										.trim().length() != 0) {
							throw new BusinessException(NCLangRes.getInstance()
									.getStrByID("40140408",
											"UPP40140408-000068") /*
																 * @res
																 * "表头输入车辆时，表体不得输入运输仓!"
																 */);
						}
					}
				}
			}
		} catch (BusinessException e) {
			throw new BusinessException(sCheckBill + e.getMessage());
		}

	}

	/**
	 * 参数： 返回：void 例外： 日期：(2002-10-31 21:11:55) 修改日期，修改人，修改原因，注释标志：
	 */
	public void doForEdit() {
		// 关闭单据号的编辑
		getBillCardPanel().getHeadItem("vdelivbillcode").setEdit(false);

		// 置入制单人
		DelivbillHHeaderVO header = (DelivbillHHeaderVO) m_currentbill
				.getParentVO();
		header.setPkbillperson(getUserID());
		header.setVbillpersonname(getUserName());

		// 置入制单日期
		UFDate ufdate = new UFDate(System.currentTimeMillis());
		header.setBilldate(ufdate);

		// // 置入制单时间
		// UFDateTime currentTime = new UFDateTime();
		// currentTime = ClientEnvironment.getServerTime();
		// header.setTmaketime(currentTime.toString());

		m_isOperationFinish = false;

		if (getShowState().equals(DMBillStatus.List)) {
			super.onSwith();
		}
		m_currentbill.getParentVO().setAttributeValue("vdoname",
				getDelivOrgName());

		if (m_currentbill != null) {
			execBodyFormulas(m_currentbill.getChildrenVO(), true);

			getBillCardPanel().setBillValueVO(m_currentbill);

			DelivbillHItemVO bodyVO[] = (DelivbillHItemVO[]) m_currentbill
					.getChildrenVO();

			if (bodyVO != null && bodyVO.length > 0) {
				// for (int j= 0; j < bodyVO.length; j++) {
				String s[] = new String[] {
						"creceiptcorp->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,creceiptcorpid)",
						"creceiptcorp->getColValue(bd_cubasdoc,custname,pk_cubasdoc,creceiptcorp)" };
				getBillCardPanel().getBillModel().execFormulas(s);
				// }
				getBillCardPanel().getBillModel().execLoadFormula();
			}

			DelivbillHHeaderVO headVO = (DelivbillHHeaderVO) m_currentbill
					.getParentVO();
			if (headVO != null) {
				// UIRefPane refPane= (UIRefPane)
				// getBillCardPanel().getHeadItem("pkdelivroute").getComponent();
				// refPane.setText(headVO.getVroutename());

				// 路线
				String pk = headVO.getPkdelivroute();
				try {
					String routedesrb = DelivbillHBO_Client.findRouteDescr(pk);
					getBillCardPanel().setHeadItem("vroutedescr", routedesrb);
				} catch (Exception e) {
					e.printStackTrace();
					dispWarningMessage(NCLangRes.getInstance().getStrByID(
							"40140408", "UPP40140408-000069") /* @res "发运路线计算错误！" */);
				}
			}
		}

		switchButtonStatus(CardBillEditing);
		getBillCardPanel().updateValue();

		// 重置界面VO行状态
		for (int i = 0; i < m_currentbill.getChildrenVO().length; i++) {
			getBillCardPanel()
					.getBillModel()
					.setRowState(
							i,
							(m_currentbill.getChildrenVO()[i].getStatus() == VOStatus.NEW ? BillModel.ADD
									: BillModel.NORMAL));
		}

		getBillCardPanel().updateUI();
		getBillCardPanel().setEnabled(true);
		setInvItemEditable((DMBillCardPanel) getBillCardPanel());

		setEditFlag(1);
		m_isOperationFinish = true;
	}

	/**
	 * 根据客户过滤单据。 创建日期：(2002-6-27 13:04:59)
	 * 
	 * @param pkcumandoc
	 *            java.lang.String
	 */
	public DelivbillHVO filterBillByCust(String pkcumandoc,
			String pkdeststockorg) {
		DelivbillHHeaderVO headVO = (DelivbillHHeaderVO) m_currentbill
				.getParentVO();
		if (headVO != null) {
			String sID = headVO.getPk_delivbill_h();
			if (sID == null || sID.trim().length() == 0) {
				nc.ui.pub.beans.MessageDialog.showErrorDlg(this, NCLangRes
						.getInstance().getStrByID("40140408",
								"UPT40140408-000068")
				/* @res "发运清单" */
				, NCLangRes.getInstance().getStrByID("40140408",
						"UPP40140408-000070")
				/* @res "发运单不存在！" */);
				return null;
			}
		}

		DelivbillHItemVO[] items;
		Vector itemsVector = new Vector();
		items = (DelivbillHItemVO[]) m_currentbill.getChildrenVO();
		if (items == null || items.length == 0) {
			nc.ui.pub.beans.MessageDialog.showErrorDlg(this,
					NCLangRes.getInstance().getStrByID("40140408",
							"UPT40140408-000068") /* @res "发运清单" */, NCLangRes
							.getInstance().getStrByID("40140408",
									"UPP40140408-000070")
			/* @res "发运单不存在！" */);
			return null;
		}

		boolean b1 = false;
		boolean b2 = false;
		if (pkcumandoc != null && pkcumandoc.trim().length() > 0) {
			b1 = true;
		}
		if (pkdeststockorg != null && pkdeststockorg.trim().length() > 0) {
			b2 = true;
		}
		if (b1 && b2) {
			nc.ui.pub.beans.MessageDialog.showErrorDlg(this, NCLangRes
					.getInstance().getStrByID("common", "UC000-0002782") /*
																		 * @res
																		 * "查询条件"
																		 */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40140408",
							"UPP40140408-000071") /*
												 * @res "客户编码/到货库存组织不可同时输入！"
												 */);
			return null;
		} else if ((!b1) && (!b2)) {
			nc.ui.pub.beans.MessageDialog.showErrorDlg(this, NCLangRes
					.getInstance().getStrByID("common", "UC000-0002782") /*
																		 * @res
																		 * "查询条件"
																		 */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40140408",
							"UPP40140408-000072") /*
												 * @res "客户编码/到货库存组织不可同时为空！"
												 */);
			return null;
		}

		if (pkdeststockorg == null || pkdeststockorg.trim().length() == 0) {
			// 通过供应商管理ID获得供应商基础ID
			nc.ui.pub.formulaparse.FormulaParse f = new nc.ui.pub.formulaparse.FormulaParse();
			String sExpress = "getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,pk_cumandoc)";
			f.setExpress(sExpress);
			nc.vo.pub.formulaset.VarryVO varry = f.getVarry();
			java.util.Hashtable table = new java.util.Hashtable();
			String pk[] = new String[items.length];
			for (int i = 0; i < items.length; i++) {
				pk[i] = items[i].getPkcusmandoc();
			}
			table.put(varry.getVarry()[0], pk);
			f.setDataS(table);
			String s[] = f.getValueS();
			//

			for (int i = 0; i < items.length; i++) {
				if (s != null && s.length > 0 && s[i] != null
						&& s[i].equals(pkcumandoc) == true) {
					itemsVector.add(items[i]);
				}
			}
		} else {
			for (int i = 0; i < items.length; i++) {
				if (items[i].getPkdeststockorg() != null
						&& items[i].getPkdeststockorg().equals(pkdeststockorg)) {
					itemsVector.add(items[i]);
				}
			}
		}

		if (itemsVector.size() > 0) {
			DelivbillHVO bill = new DelivbillHVO();
			bill.setParentVO(m_currentbill.getParentVO());
			items = new DelivbillHItemVO[itemsVector.size()];
			itemsVector.copyInto(items);
			bill.setChildrenVO(items);
			return bill;
		}

		nc.ui.pub.beans.MessageDialog.showErrorDlg(this,
				NCLangRes.getInstance().getStrByID("40140408",
						"UPT40140408-000068") /* @res "发运清单" */, NCLangRes
						.getInstance().getStrByID("40140408",
								"UPP40140408-000073") /*
													 * @res "符合条件的发运清单不存在！"
													 */);
		return null;
	}

	/**
	 * 函数的功能、用途、对属性的更改，以及函数执行前后对象的状态。
	 * 
	 * @param 参数说明
	 * @return 返回值
	 * @exception 异常描述
	 * @see 需要参见的其它内容
	 * @since 从类的那一个版本，此方法被添加进来。（可选）
	 * @deprecated 该方法从类的那一个版本后，已经被其它方法替换。（可选）
	 * @author zhongyue
	 * @return nc.vo.dm.dm104.DelivbillHItemVO[]
	 */
	public DelivbillHItemVO[] getAllItems() {
		DelivbillHItemVO[] allitems = null;
		DelivbillHItemVO[] onebillitems = null;
		java.util.ArrayList allitemsArray = new java.util.ArrayList();
		for (int i = 0; i < m_bills.length; i++) {
			if (m_bills[i] != null) {
				onebillitems = (DelivbillHItemVO[]) m_bills[i].getChildrenVO();
			}
			if (onebillitems != null && onebillitems.length > 0) {
				for (int j = 0; j < onebillitems.length; j++) {
					allitemsArray.add(onebillitems[j]);
				}
			}
		}
		allitems = new DelivbillHItemVO[allitemsArray.size()];
		allitemsArray.toArray(allitems);
		return allitems;
	}

	/**
	 * 函数的功能、用途、对属性的更改，以及函数执行前后对象的状态。
	 * 
	 * @param 参数说明
	 * @return 返回值
	 * @exception 异常描述
	 * @see 需要参见的其它内容
	 * @since 从类的那一个版本，此方法被添加进来。（可选）
	 * @deprecated 该方法从类的那一个版本后，已经被其它方法替换。（可选）
	 * @author zhongyue
	 * @return nc.vo.dm.dm104.DelivbillHItemVO[]
	 * @param hvos
	 *            nc.vo.dm.dm104.DelivbillHVO[]
	 */
	public DelivbillHItemVO[] getAllItemsOnebill(DelivbillHVO[] hvos) {
		DelivbillHItemVO[] allitems = null;
		DelivbillHItemVO[] onebillitems = null;
		java.util.ArrayList allitemsArray = new java.util.ArrayList();
		for (int i = 0; i < hvos.length; i++) {
			if (hvos[i] != null) {
				onebillitems = (DelivbillHItemVO[]) hvos[i].getChildrenVO();
			}
			if (onebillitems != null && onebillitems.length > 0) {
				for (int j = 0; j < onebillitems.length; j++) {
					allitemsArray.add(onebillitems[j]);
				}
			}
		}
		allitems = new DelivbillHItemVO[allitemsArray.size()];
		allitemsArray.toArray(allitems);
		return allitems;
	}

	/**
	 * 将列表下所有的VO的数组转换成一个ArrayList。 创建日期：(2002-5-27 9:17:50)
	 * 
	 * @return java.util.ArrayList
	 */
	public java.util.ArrayList getAllVO() {
		ArrayList al = new ArrayList();
		if (null != m_bills && m_bills.length != 0) {
			for (int i = 0; i < m_bills.length; i++) {
				al.add(m_bills[i]);
			}
		}
		return al;
	}

	/**
	 * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2002-9-16 22:20:16) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return nc.vo.dm.dm104.DelivbillHVO[]
	 */
	public nc.vo.dm.dm104.DelivbillHVO[] getArrayBills() {
		return m_bills;
	}

	/**
	 * 返回当前处理单据ID。 创建日期：(2001-11-15 9:10:05)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getBillID() {
		String curBillID = null;
		try {
			if (getCurrentBill() == null) {
				return null;
			} else {
				curBillID = getCurrentBill().getParentVO().getPrimaryKey();
			}
		} catch (Exception e) {

		}
		return curBillID;
	}

	/**
	 * 返回计算运费对话框。 创建日期：(2002-7-10 14:33:22)
	 * 
	 * @return nc.ui.dm.dm104.CaculateFreight
	 */
	protected CaculateTransFee getCaculateTransFeeDlg() {
		try {
			if (m_dlgCaculateTransFee == null) {
				/** 实例化对话框是必须指定他的父窗口，否则另外的窗口得到焦点时，对话框会隐藏到其它窗口的后面。 */
				DMDataVO ddvo = new DMDataVO();
				ddvo.setAttributeValue("pkdelivorg", getDelivOrgPK());
				ddvo.setAttributeValue("userid", getUserID());
				ddvo.setAttributeValue("username", getUserName());
				ddvo.setAttributeValue("corpid", getCorpID());
				ddvo.setAttributeValue("corpname", getCorpName());
				ddvo.setAttributeValue("BD501", BD501); // 主计量数量小数位数
				ddvo.setAttributeValue("BD502", BD502); // 辅计量数量小数位数
				ddvo.setAttributeValue("BD503", BD503); // 换算率
				ddvo.setAttributeValue("BD505", BD505); // 单价小数位数
				ddvo.setAttributeValue("BD301", BD301); // 金额小数位数
				ddvo.setAttributeValue("DM013", DM013); // 是否显示运费单价
				ddvo.setAttributeValue("DM014", DM014); // 计划运费和实际运费自动分摊依据

				m_dlgCaculateTransFee = new CaculateTransFee(this, NCLangRes
						.getInstance().getStrByID("40140408",
								"UPP40140408-000048")
				/* @res "运费计算" */, ddvo);
			}
		} catch (Throwable e) {
			handleException(e);
		}
		return m_dlgCaculateTransFee;
	}

	/**
	 * 返回计算运费按钮。 功能： 参数： 返回： 例外： 日期：(2002-9-10 20:21:21) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return nc.ui.pub.ButtonObject
	 */
	public ButtonObject getCalculateFeeButton() {
		return boCalculateFee;
	}

	/**
	 * 返回承运商取消确认按钮。 功能： 参数： 返回： 例外： 日期：(2002-9-10 20:21:21) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return nc.ui.pub.ButtonObject
	 */
	public ButtonObject getCancelConfirmButton() {
		return boCancelConfirm;
	}

	/**
	 * 返回发运清单查询对话框特性值。
	 * 
	 * @return nc.ui.pub.query.QueryConditionClient
	 */
	private QueryConditionDlg getCndtnDlgList() {
		if (listQueryConditionDlg == null) {
			listQueryConditionDlg = new QueryConditionDlg(this);
			listQueryConditionDlg
					.setDefaultCloseOperation(QueryConditionDlg.HIDE_ON_CLOSE);

			listQueryConditionDlg.setTempletID(m_sCorpID, "40140409",
					m_sUserID, null);
			listQueryConditionDlg.hideNormal();

			// 设置查询对话框参照
			// 客户
			UIRefPane customerRef = new UIRefPane();
			customerRef.setRefType(2); // 树表结构
			customerRef.setIsCustomDefined(true);

			nc.ui.bd.ref.AbstractRefModel customerModel = new nc.ui.dm.pub.ref.CustbaseRefModel();
			/*
			 * customerModel.setWherePart( "bd_cubasdoc.pk_corp in (" +
			 * getStrCorpIDsOfDelivOrg() + ", '" +
			 * getClientEnvironment().getGroupId() + "')");
			 */

			customerRef.setRefModel(customerModel);
			customerRef.setWhereString("bd_cubasdoc.pk_corp in ("
					+ getStrCorpIDsOfDelivOrg() + ", '"
					+ getClientEnvironment().getGroupId() + "')");
			listQueryConditionDlg.setValueRef("pkcusmandoc", customerRef);
			// 库存组织
			UIRefPane storeorgRef = new UIRefPane();
			storeorgRef.setIsCustomDefined(true);

			nc.ui.bd.ref.AbstractRefModel storeorgRefModel = new nc.ui.dm.pub.ref.StoreOrgRefModel();
			// storeorgRefModel.setWherePart("bd_calbody.pk_corp in (" +
			// getStrCorpIDsOfDelivOrg() + ")");

			storeorgRef.setRefModel(storeorgRefModel);
			storeorgRef.setWhereString("bd_calbody.pk_corp in ("
					+ getStrCorpIDsOfDelivOrg() + ")");
			listQueryConditionDlg.setValueRef("pkdeststockorg", storeorgRef);
			//
			ivjQueryConditionDlg.setRefMultiInit("pk_delivbill_h", "", "",
					new String[][] { { "datefrom", "senddate" },
							{ "dateto", "senddate" } }, false);

		}
		return listQueryConditionDlg;
	}

	/**
	 * 返回 QueryConditionClient1 特性值。
	 * 
	 * @return nc.ui.pub.query.QueryConditionClient
	 */
	private DMQueryConditionDlg getConditionDlg() {

		// ivjQueryConditionDlg = null;

		if (ivjQueryConditionDlg == null) {
			ivjQueryConditionDlg = new QueryConditionDlg(this);
			ivjQueryConditionDlg
					.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);

			ivjQueryConditionDlg.setTempletID(m_sCorpID, m_sRNodeName,
					m_sUserID, null);

			ivjQueryConditionDlg.hideUnitButton();
			ivjQueryConditionDlg.setNormalShow(true);
			ivjQueryConditionDlg.getUIPanelNormal().add(
					getQueryStatusConditionPane());
			// 设置下拉框显示
			ivjQueryConditionDlg.setCombox("dm_delivbill_b.irowstatus",
					new Object[][] {
							{
									new Integer(0),
									nc.ui.ml.NCLangRes.getInstance()
											.getStrByID("40140408",
													"UPP40140408-000205")
							/* @res "自由" */},
							{
									new Integer(2),
									nc.ui.ml.NCLangRes.getInstance()
											.getStrByID("40140408",
													"UPP40140408-000206")
							/* @res "审批" */},
							{
									new Integer(3),
									nc.ui.ml.NCLangRes.getInstance()
											.getStrByID("40140408",
													"UPP40140408-000207")
							/* @res "关闭" */},
							{
									new Integer(5),
									nc.ui.ml.NCLangRes.getInstance()
											.getStrByID("40140408",
													"UPP40140408-000208")
							/* @res "全部" */}, });

			ivjQueryConditionDlg.setInitDate("datefrom", getClientEnvironment()
					.getDate().toString());
			ivjQueryConditionDlg.setInitDate("dateto", getClientEnvironment()
					.getDate().toString());

			// 运费类别
			ivjQueryConditionDlg.setCombox("dm_delivbill_h.isendtype",
					new String[][] { { "0", FreightType.sNone },
							{ "1", FreightType.sAp }, { "2", FreightType.sAr },
							{ "3", FreightType.sPre } });

			// 是否退货
			ivjQueryConditionDlg.setCombox("dm_delivbill_b.borderreturn",
					new String[][] {
							{ CConstant.ALL, "" },
							{
									CConstant.NOT,
									NCLangRes.getInstance().getStrByID(
											"SCMCOMMON", "UPPSCMCommon-000108")
							/* @res "否" */},
							{
									CConstant.YES,
									NCLangRes.getInstance().getStrByID(
											"SCMCOMMON", "UPPSCMCommon-000244")
							/* @res "是" */} });

			ivjQueryConditionDlg.setCombox("dm_delivbill_b.vbilltype",
					new String[][] {
							{ CConstant.ALL, "" },
							{
									"30",
									NCLangRes.getInstance().getStrByID(
											"40140408", "UPP40140408-000020")
							/* @res "销售订单" */},
							{
									"21",
									NCLangRes.getInstance().getStrByID(
											"SCMCOMMON", "UPPSCMCommon-000025")
							/* @res "采购订单" */},
							{
									"5C",
									NCLangRes.getInstance().getStrByID(
											"40140408", "UPP40140408-000021")
							/* @res "总部结算的公司调拨定单" */},
							{
									"5D",
									NCLangRes.getInstance().getStrByID(
											"40140408", "UPP40140408-000022")
							/* @res "公司间调拨订单" */},
							{
									"5E",
									NCLangRes.getInstance().getStrByID(
											"40140408", "UPP40140408-000023")
							/* @res "组织间调拨订单" */},
							{
									"5I",
									NCLangRes.getInstance().getStrByID(
											"40140408", "UPP40140408-000024")
							/* @res "组织内调拨订单" */} });

			ivjQueryConditionDlg.setCombox("dm_delivbill_h.bmissionbill",
					new String[][] {
							{ "", "" },
							{
									"Y",
									NCLangRes.getInstance().getStrByID(
											"SCMCOMMON", "UPPSCMCommon-000244")
							/* @res "是" */},
							{
									"N",
									NCLangRes.getInstance().getStrByID(
											"SCMCOMMON", "UPPSCMCommon-000108")
							/* @res "否" */} });

			// 设置查询对话框参照
			// 客户
			UIRefPane customerRef = new UIRefPane();
			customerRef.setRefType(2); // 树表结构
			customerRef.setIsCustomDefined(true);

			nc.ui.bd.ref.AbstractRefModel customerModel = new nc.ui.dm.pub.ref.CustbaseRefModel();
			/*
			 * customerModel.setWherePart( "bd_cubasdoc.pk_corp in (" +
			 * getStrCorpIDsOfDelivOrg() + ", '" +
			 * getClientEnvironment().getGroupId() + "')");
			 */

			customerRef.setRefModel(customerModel);
			customerRef.setWhereString("bd_cubasdoc.pk_corp in ("
					+ getStrCorpIDsOfDelivOrg() + ", '"
					+ getClientEnvironment().getGroupId() + "')");
			ivjQueryConditionDlg.setValueRef("dm_delivbill_b.pkcusmandoc",
					customerRef);
			ivjQueryConditionDlg.setValueRef("dm_delivbill_b.pkcusmandoc1",
					customerRef);
			// 存货
			UIRefPane invRef = new UIRefPane();
			invRef.setRefType(2); // 树表结构
			invRef.setIsCustomDefined(true);

			nc.ui.bd.ref.AbstractRefModel invRefModel = new nc.ui.dm.pub.ref.InvbaseRefModel();
			/*
			 * invRefModel.setWherePart( "bd_invbasdoc.pk_corp in (" +
			 * getStrCorpIDsOfDelivOrg() + ", '" +
			 * getClientEnvironment().getGroupId() + "')");
			 */
			invRef.setRefModel(invRefModel);
			invRef.setWhereString("bd_invbasdoc.pk_corp in ("
					+ getStrCorpIDsOfDelivOrg() + ", '"
					+ getClientEnvironment().getGroupId() + "')");
			ivjQueryConditionDlg.setValueRef("dm_delivbill_b.pkinv", invRef);
			ivjQueryConditionDlg.setValueRef("dm_delivbill_b.pkinv1", invRef);
			//

			// 制单人
			UIRefPane refpanePlanperson = new UIRefPane();
			refpanePlanperson.setRefNodeName("操作员");
			refpanePlanperson.setWhereString("sm_user.pk_corp in ("
					+ getStrCorpIDsOfDelivOrg() + ", '"
					+ getClientEnvironment().getGroupId() + "')");
			ivjQueryConditionDlg.setValueRef("pkbillperson", refpanePlanperson);

			// 发货库存组织
			UIRefPane refpaneCalbodySend = new UIRefPane();
			nc.ui.dm.pub.ref.QuerySendCalBodyRefModel querySendCalBodyRefModel = new nc.ui.dm.pub.ref.QuerySendCalBodyRefModel();
			try {
				String where = nc.ui.dm.dm102.DeliverydailyplanBO_Client
						.getDataPowerSubSql("bd_calbody", "库存组织",
								"bd_calbody.pk_calbody",
								(String[]) getAgentCorpIDsofDelivOrg().toArray(
										new String[0]), getUserID());
				where = where == null ? "" : where;
				if (where.trim().length() > 0) {
					where += " and ";
				}
				where += " property in ( 0,1 ) ";
				querySendCalBodyRefModel.setWherePart(where);

			} catch (Exception e) {
				e.printStackTrace();
				showErrorMessage(e.getMessage());
			}
			refpaneCalbodySend.setRefModel(querySendCalBodyRefModel);
			ivjQueryConditionDlg.setValueRef("dm_delivbill_b.pksendstockorg",
					refpaneCalbodySend);

			// 日计划单据号
			UIRefPane refpaneDayplcode = new UIRefPane();
			nc.ui.dm.pub.ref.DelivdayplcodeRefModel refModelDayplcode = new nc.ui.dm.pub.ref.DelivdayplcodeRefModel();
			refModelDayplcode.setDelivOrgPK(getDelivOrgPK());
			refpaneDayplcode.setRefModel(refModelDayplcode);
			ivjQueryConditionDlg.setValueRef("vdelivdayplcode",
					refpaneDayplcode);

			// 订单号
			UIRefPane refpaneSaleordercode = new UIRefPane();
			nc.ui.dm.pub.ref.SaleOrderCodeRefModel refModelSaleordercode = new nc.ui.dm.pub.ref.SaleOrderCodeRefModel();
			refModelSaleordercode.setDelivOrgPK(getDelivOrgPK());
			refpaneSaleordercode.setRefModel(refModelSaleordercode);
			ivjQueryConditionDlg.setValueRef("vsrcbillnum",
					refpaneSaleordercode);

			UIRefPane vdelivbillcode = new UIRefPane();
			nc.ui.dm.pub.ref.DelivbillcodeRefModel refVdelivbillcode = new nc.ui.dm.pub.ref.DelivbillcodeRefModel();
			refVdelivbillcode.setDelivOrgPK(getDelivOrgPK());
			vdelivbillcode.setRefModel(refVdelivbillcode);
			ivjQueryConditionDlg.setValueRef("vdelivbillcode", vdelivbillcode);

			// 设置承运商参照的默认发运组织
			UIRefPane pktrancust = new UIRefPane();
			TrancustRefModel refpktrancust = new TrancustRefModel();
			refpktrancust.setDelivOrgPK(getDelivOrgPK());
			pktrancust.setRefModel(refpktrancust);
			ivjQueryConditionDlg.setValueRef("dm_delivbill_h.pktrancust",
					pktrancust);

			// //司机 pkdriver nc.ui.dm.pub.ref.DriverRefModel
			UIRefPane pkdriver = new UIRefPane();
			nc.ui.dm.pub.ref.DriverRefModel refpkdriver = new nc.ui.dm.pub.ref.DriverRefModel();
			refpkdriver.setDelivOrgPK(getDelivOrgPK());
			pkdriver.setRefModel(refpktrancust);
			ivjQueryConditionDlg.setValueRef("dm_delivbill_h.pkdriver",
					pkdriver);

			ivjQueryConditionDlg.setRefMultiInit("vdelivbillcode", "", "",
					new String[][] { { "datefrom", "senddate" },
							{ "dateto", "senddate" } }, false);

			// vdelivbillcode

			ivjQueryConditionDlg.setRefMultiInit(
					"dm_delivdaypl.vdelivdayplcode", "", "",
					new String[][] { { "datefrom", "snddate" },
							{ "dateto", "snddate" } }, false);

			ivjQueryConditionDlg.setRefMultiInit("dm_delivdaypl.vsrcbillnum",
					"", "", new String[][] { { "datefrom", "dbilldate" },
							{ "dateto", "dbilldate" } }, false);

			Vector v = new Vector();
			QueryConditionVO[] qcv = ivjQueryConditionDlg.getAllTempletDatas();
			for (int i = 0; i < qcv.length; i++) {
				if (isQueryForWayLoss) {
					if (!qcv[i].getFieldCode().equals("signertype")) {
						if (qcv[i].getFieldCode().equals(
								"dm_delivbill_h.pkdelivorg")) {
							qcv[i].setDispValue(getDelivOrgPK()); // 设置pk
							qcv[i].setValue(getDelivOrgName()); // 设置显示值
							qcv[i].setIfImmobility(new UFBoolean(true));
						}
						v.add(qcv[i]);
					}
				} else {
					// 以下一段是为了隐藏掉一些项
					// 1、因为 签收方 仅仅对签收节点有用，
					// 2、途损方 仅仅对途损节点有用
					// 3、发运组织 仅仅对途损节点和签收节点有用，
					if (!qcv[i].getFieldCode().equals("signertype")
							&& !qcv[i].getFieldCode().equals(
									"dm_delivbill_h.pkdelivorg")
							&& !qcv[i].getFieldCode().equals("waylosstype")) {
						if (bIsForSofee) {
							if (!qcv[i].getFieldCode().equals(
									"dm_delivbill_h.isendtype")) {
								v.add(qcv[i]);
							}
						} else {
							v.add(qcv[i]);
						}
					}
				}
			}
			// 处理代垫
			if (bIsForSofee) {
				QueryConditionVO qcvSofee = new QueryConditionVO();
				qcvSofee.setDataType(new Integer(5));
				qcvSofee.setFieldCode("fee.pkcustinvoice");
				qcvSofee.setFieldName("开票客户");
				qcvSofee.setIfUsed(new UFBoolean(true));
				qcvSofee.setIfAutoCheck(new UFBoolean(true));
				qcvSofee.setReturnType(new Integer(2));
				qcvSofee.setDispType(new Integer(1));
				qcvSofee
						.setPrimaryKey(ivjQueryConditionDlg.getConditionDatas()[0]
								.getPkTemplet());
				qcvSofee.setPkTemplet("temppkcutinvoice");
				// qcvSofee.setIfImmobility(new UFBoolean(false));
				// qcvSofee.setResid("UPP40140408-000027");
				qcvSofee.setConsultCode("客商档案");
				qcvSofee.setIfMust(new UFBoolean(true));
				v.add(qcvSofee);
			}

			// ivjQueryConditionDlg.setConditionDatas((QueryConditionVO[])v.toArray(new
			// QueryConditionVO[v.size()]));
			ivjQueryConditionDlg.initTempletDatas((QueryConditionVO[]) v
					.toArray(new QueryConditionVO[v.size()]));

			//
			ivjQueryConditionDlg.setCombox("isexistpacknum", new String[][] {
					{
							"0",
							NCLangRes.getInstance().getStrByID("SCMCOMMON",
									"UPPSCMCommon-000217")
					/* @res "全部" */},
					{
							"1",
							NCLangRes.getInstance().getStrByID("40140408",
									"UPP40140408-000025")
					/* @res "含包装明细" */
					},
					{
							"2",
							NCLangRes.getInstance().getStrByID("40140408",
									"UPP40140408-000026")
					/* @res "不含包装明细" */
					} });
			//

			// 途损节点"查询发运单"条件中需要用到"途损方"选项
			if (isQueryForWayLoss) {
				ivjQueryConditionDlg.setCombox("waylosstype", new String[][] {
						{
								"0",
								NCLangRes.getInstance().getStrByID("40140408",
										"UPP40140408-000027")
						/* @res "发货方途损" */},
						{
								"1",
								NCLangRes.getInstance().getStrByID("40140408",
										"UPP40140408-000028")
						/* @res "收货方途损" */} });
			}

			nc.ui.scm.pub.def.DefSetTool
					.updateQueryConditionClientUserDef(
							(nc.ui.pub.query.QueryConditionClient) ivjQueryConditionDlg,
							getCorpPrimaryKey(), vUserdefCode,
							"dm_delivbill_h.vuserdef", null, 1,
							"dm_delivbill_b.vuserdef", null, 1);
			// pkdelivmode
			UIRefPane pkdelivmode = new UIRefPane();
			pkdelivmode.setRefNodeName("发运方式");
			AbstractRefModel model = pkdelivmode.getRefModel();
			model.addWherePart(" and (pk_corp = '"+getCorpPrimaryKey()+"' or pk_corp='0001' or pk_corp is null) and (bd_sendtype.transporttype = 0 OR bd_sendtype.transporttype = 2 OR bd_sendtype.transporttype = 3)");
			pkdelivmode.setRefModel(model);
			ivjQueryConditionDlg.setValueRef("pkdelivmode", pkdelivmode);
		}
		if (bIsForSofee) {
			UIRefPane refpane = new UIRefPane();
			String sql = " bd_cumandoc.pk_corp = '"
					+ getBelongCorpIDofDelivOrg()
					+ "' and custflag in( '2', '0')";
			CustmandocDefaultRefModel model = new CustmandocDefaultRefModel(
					"客商档案");
			model.setWherePart(sql);
			refpane.setRefModel(model);
			ivjQueryConditionDlg.setValueRef("fee.pkcustinvoice", refpane);
			;

			ivjQueryConditionDlg.hideNormal();
		}
		return ivjQueryConditionDlg;
	}

	/**
	 * 功能描述：获取QueryStatusConditionPane 面板 author : zwq 创建日期：(2006-6-1)
	 * 
	 * @return QueryStatusConditionPane
	 * @param null
	 * @exception null
	 */
	private QueryStatusConditionPane getQueryStatusConditionPane() {
		if (m_panelStatusQuery == null) {
			m_panelStatusQuery = new QueryStatusConditionPane();
		}
		return m_panelStatusQuery;
	}

	/**
	 * 返回卡片界面当前正在操作的单据 日期：(2002-9-16 22:19:07) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return nc.vo.dm.dm104.DelivbillHVO
	 */
	public nc.vo.dm.dm104.DelivbillHVO getCurrentBill() {
		return m_currentbill;
	}

	/**
	 * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2002-9-16 22:19:07) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return nc.vo.dm.dm104.DelivbillHVO
	 */
	public nc.vo.dm.dm107.DelivfeebillHVO getDelivFeeBillVO() throws Exception {
		nc.vo.dm.dm107.DelivfeebillHVO delivfeevo = new nc.vo.dm.dm107.DelivfeebillHVO();
		m_bIsCaculateShow = false;
		onCalculateFee();
		getCaculateTransFeeDlg().onCalculate();
		getCaculateTransFeeDlg().getPanelGrid().getBillModel().setValueAt(
				"true", 0, "bchoose");
		getCaculateTransFeeDlg().setIsDlgSaveDelivFee(false);
		getCaculateTransFeeDlg().onOK();
		m_bIsCaculateShow = true;
		delivfeevo = getCaculateTransFeeDlg().getDelivFeeBillvo();
		return delivfeevo;
	}

	/**
	 * 返回发运清单对话框。 创建日期：(2001-5-8 19:16:47)
	 * 
	 * @return nc.ui.ic.lot.LotNumbDlg
	 */
	protected DelivListDlg getDelivLisDlg() {
		try {
			if (m_delivLisDlg == null) {
				/** 实例化对话框是必须指定他的父窗口，否则另外的窗口得到焦点时，对话框会隐藏到其它窗口的后面。 */
				// m_dlg = new StatbDlg(); //this.getParent());
				m_delivLisDlg = new DelivListDlg(this.getParent());
				m_delivLisDlg.setTitle(NCLangRes.getInstance().getStrByID(
						"40140408", "UPT40140408-000068") /* @res "发运清单" */);
				// m_delivLisDlg.setParent(this);

			}
		} catch (Throwable e) {
			handleException(e);
		}
		return m_delivLisDlg;
	}

	/**
	 * 参数： 返回： 例外： 日期：(2002-12-12 10:09:33) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return java.lang.String
	 */
	public String getDelivOrgAgentCorpID() {
		return this.getStrCorpIDsOfDelivOrg();
	}

	/**
	 * getTitle 方法注解。
	 */
	protected FreeItemRefPane getFreeItemRefPane() {
		if (m_firpFreeItemRefPane == null) {
			m_firpFreeItemRefPane = new FreeItemRefPane();
		}
		return m_firpFreeItemRefPane;
	}

	/**
	 * 返回计算运费按钮。 功能： 参数： 返回： 例外： 日期：(2002-9-10 20:21:21) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return nc.ui.pub.ButtonObject
	 */
	public ButtonObject getFristButton() {
		return boFirst;
	}

	/**
	 * 返回生成任务单按钮。 功能： 参数： 返回： 例外： 日期：(2002-9-10 20:21:21) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return nc.ui.pub.ButtonObject
	 */
	public ButtonObject getGenTaskBillButton() {
		return boGenTaskBill;
	}

	/**
	 * 创建日期：(2003-4-15 15:28:26) 作者：左小军 修改日期： 修改人： 修改原因： 算法说明：
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 * @param row
	 *            int
	 */
	protected boolean getHsl(int row, UFDouble hsl) {
		// UFDouble hsl= null;
		Object oTemp = null;
		oTemp = getBillCardPanel().getBodyValueAt(row, pkinv);
		if (oTemp == null) {
			SCMEnv.info("no inv id");
			return false;
		}
		String sInvID = oTemp.toString();
		oTemp = getBillCardPanel().getBodyValueAt(row, pkassistmeasure);
		String sAstName = null;
		String sAstID = null;
		if (oTemp != null
				&& getBillCardPanel().getBodyValueAt(row, vassistmeaname) != null) {
			sAstName = getBillCardPanel().getBodyValueAt(row, vassistmeaname)
					.toString();
			sAstID = oTemp.toString();
		}
		InvVO voaInv[] = getInvInfo(new String[] { sInvID },
				new String[] { sAstID });
		if (voaInv == null || voaInv.length == 0 || voaInv[0] == null) {
			SCMEnv.info("no inv vo");
			return false;
		}
		InvVO invvo = voaInv[0];
		// -------------------------
		Integer isAstMgt = (Integer) invvo.getIsAstUOMmgt(); // 辅计量管理？

		if (isAstMgt == null || isAstMgt.intValue() == 0) {
			return false;
		}

		// 00000000
		if (sAstID == null || sAstID.trim().length() == 0) {
			/**
			 * 若清空辅计量：换算率，if 固定换算率： 清空数量，辅数量，应发数量，应发辅数量，金额，计划金额 if
			 * 变动换算率：清空数量，应发数量，金额，计划金额
			 */
			/** 清空界面和VO中的辅计量名称和ID */
			getBillCardPanel().setBodyValueAt(null, row, vassistmeaname);
			getBillCardPanel().setBodyValueAt(null, row, pkassistmeasure);
			/** 清空换算率 */
			// 重算存货的换算率
			invvo.setHsl(null);
			// 更新存货vo
			updateInvInfo(invvo);
			return false;
		}

		String sPK = sAstID;
		String sName = sAstName;

		getBillCardPanel().setBodyValueAt(sName, row, vassistmeaname);
		getBillCardPanel().setBodyValueAt(sPK, row, pkassistmeasure);
		nc.ui.scm.ic.measurerate.InvMeasRate imr = new nc.ui.scm.ic.measurerate.InvMeasRate();
		nc.ui.pub.beans.UIRefPane refCast = (nc.ui.pub.beans.UIRefPane) getBillCardPanel()
				.getBodyItem(vassistmeaname).getComponent();

		imr.filterMeas(m_sCorpID, sInvID, refCast);

		nc.vo.bd.b15.MeasureRateVO voMeas = imr.getMeasureRate(sInvID, sPK);
		if (voMeas != null) {
			hsl = voMeas.getMainmeasrate();
			// 重算存货的换算率
			invvo.setHsl(hsl);
			// 更新存货vo
			updateInvInfo(invvo);
		}
		return true;
	}

	/**
	 * 返回是否插入到已有发运单的标示标志
	 * 
	 * @return true 如果是插入到已有发运单；否则返回 false
	 * @author zxj
	 */
	private boolean getInsertExist() {
		return m_bInsertExist;
	}

	/**
	 * 此处插入方法说明。 功能描述: 输入参数: 返回值: 异常处理: 日期:
	 * 
	 * @return java.util.ArrayList
	 */
	public ArrayList getItemFormulaBody() {
		return nc.ui.dm.dm104.DelivBillFormula.getItemFormulaBody();
	}

	/**
	 * 此处插入方法说明。 功能描述: 输入参数: 返回值: 异常处理: 日期:
	 * 
	 * @return java.util.ArrayList
	 */
	public ArrayList getItemFormulaHead() {
		return nc.ui.dm.dm104.DelivBillFormula.getItemFormulaHead();
	}

	/**
	 * 返回计算运费按钮。 功能： 参数： 返回： 例外： 日期：(2002-9-10 20:21:21) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return nc.ui.pub.ButtonObject
	 */
	public ButtonObject getLastButton() {
		return boLast;
	}

	/**
	 * 返回当前列表行。 功能： 参数： 返回： 例外： 日期：(2002-9-17 22:01:15) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return int
	 */
	public int getListRow() {
		return m_listRow;
	}

	/**
	 * 函数的功能、用途、对属性的更改，以及函数执行前后对象的状态。
	 * 
	 * @param 参数说明
	 * @return 返回值
	 * @exception 异常描述
	 * @see 需要参见的其它内容
	 * @since 从类的那一个版本，此方法被添加进来。（可选）
	 * @author zxj
	 * @return boolean
	 */
	protected LotNumbRefPane getLotNumbRefPane() {
		if (m_lnrpLotNumbRefPane == null) {
			m_lnrpLotNumbRefPane = new LotNumbRefPane();

		}
		return m_lnrpLotNumbRefPane;
	}

	/**
	 * 返回计算运费按钮。 功能： 参数： 返回： 例外： 日期：(2002-9-10 20:21:21) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return nc.ui.pub.ButtonObject
	 */
	public ButtonObject getNextButton() {
		return boNext;
	}

	/**
	 * 返回承运商取消确认按钮。 功能： 参数： 返回： 例外： 日期：(2002-9-10 20:21:21) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return nc.ui.pub.ButtonObject
	 */
	public ButtonObject getOpenBillButton() {
		return boOpenBill;
	}

	/**
	 * 返回生成出库单按钮。 功能： 参数： 返回： 例外： 日期：(2002-9-10 20:21:21) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return nc.ui.pub.ButtonObject
	 */
	public ButtonObject getOutBillButton() {
		return boOutBill;
	}

	/**
	 * 返回 生成出库单对话框。
	 * 
	 * @return nc.ui.pub.query.QueryConditionClient
	 */
	protected OutBillDlg getOutBillDlg() {
		try {
			if (m_outBillDlg == null) {
				/** 实例化对话框是必须指定他的父窗口，否则另外的窗口得到焦点时，对话框会隐藏到其它窗口的后面。 */
				// m_dlg = new StatbDlg(); //this.getParent());
				// DMDataVO dmdvo= new DMDataVO();
				// dmdvo.setAttributeValue("userid", getUserID());
				if (BD501 == null) {
					BD501 = new Integer(2);
				}
				DMDataVO ddvo = new DMDataVO();
				// ddvo.setAttributeValue("pkcorp", getCorpID());
				// ddvo.setAttributeValue("vcorpname", getCorpName());
				ddvo.setAttributeValue("pkdelivorg", getDelivOrgPK());
				ddvo.setAttributeValue("userid", getUserID());
				ddvo.setAttributeValue("username", getUserName());
				ddvo.setAttributeValue("corpid", getCorpID());
				ddvo.setAttributeValue("corpname", getCorpName());
				ddvo.setAttributeValue("BD501", BD501); // 主计量数量小数位数
				ddvo.setAttributeValue("BD502", BD502); // 辅计量数量小数位数
				ddvo.setAttributeValue("BD503", BD503); // 换算率
				ddvo.setAttributeValue("BD505", BD505); // 单价小数位数
				ddvo.setAttributeValue("BD301", BD301); // 金额小数位数
				ddvo.setAttributeValue("DM010", DM010); // 本次是否超发运单生成出库单
				m_outBillDlg = new OutBillDlg(this, NCLangRes.getInstance()
						.getStrByID("40148868", "UPT40148868-000004") /*
																	 * @res
																	 * "生成出库单"
																	 */, ddvo);

			}
		} catch (Throwable e) {
			handleException(e);
		}
		return m_outBillDlg;
	}

	/**
	 * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2002-8-31 10:49:01) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return nc.ui.dm.dm104.InputNumberOfPackage
	 */
	protected PackageListDlg getPackageListDlg() {
		try {
			if (packageListDlg == null) {
				/** 实例化对话框是必须指定他的父窗口，否则另外的窗口得到焦点时，对话框会隐藏到其它窗口的后面。 */
				DMDataVO ddvo = new DMDataVO();
				ddvo.setAttributeValue("pkdelivorg", getDelivOrgPK());
				ddvo.setAttributeValue("corpid", getCorpID());
				ddvo.setAttributeValue("BD501", BD501);
				String sHasChangePack = hasChangedPack ? "Y" : "N";
				ddvo.setAttributeValue("hasChangedPack", sHasChangePack);
				packageListDlg = new PackageListDlg(this, NCLangRes
						.getInstance().getStrByID("40140408",
								"UPP40140408-000014") /*
													 * @res "请输入包装分类对应的数量"
													 */, ddvo);
			}
		} catch (Throwable e) {
			handleException(e);
		}
		return packageListDlg;
	}

	/**
	 * 返回计算运费按钮。 功能： 参数： 返回： 例外： 日期：(2002-9-10 20:21:21) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return nc.ui.pub.ButtonObject
	 */
	public ButtonObject getPreButton() {
		return boPre;
	}

	/**
	 * 创建日期：(2003-12-15 16:42:29) 作者：仲瑞庆 参数： 返回： 说明：
	 * 
	 * @return nc.ui.pub.ButtonObject
	 */
	public ButtonObject getPreview() {
		return boPreview;
	}

	/**
	 * 创建日期：(2003-12-15 16:35:51) 作者：仲瑞庆 参数： 返回： 说明：
	 * 
	 * @return nc.ui.pub.ButtonObject
	 */
	public ButtonObject getPrintPreview() {
		return boPrintPreview;
	}

	/**
	 * 如果是途损引用该节点， 那么在查询条件中有“发运组织”这个条件,但不是必录项
	 */
	public boolean getQueryForWayLoss() {
		return isQueryForWayLoss;
	}

	/**
	 * 为了支持打印时修改 ts 的需求，搜索出列表下当前选中的行所对应的缓存数据
	 * 
	 * @return ArrayList 列表下当前选中的行所对应的缓存数据
	 */
	private ArrayList getSelectedListCacheBills() {
		ArrayList alvo = new ArrayList();
		for (int i = 0; i < getBillListPanel().getHeadTable()
				.getSelectedRowCount(); i++) {
			alvo.add(getAllVO().get(
					getBillListPanel().getHeadTable().getSelectedRows()[i]));
		}
		return alvo;
	}

	/**
	 * 返回发运清单按钮。 功能： 参数： 返回： 例外： 日期：(2002-9-10 20:21:21) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return nc.ui.pub.ButtonObject
	 */
	public ButtonObject getShowListBillButton() {
		return boShowListBill;
	}

	/**
	 * 创建者：晁志平 功能：获取表头查询条件 参数：由查询窗口得到的条件VO 返回： 例外： 日期：(2001-8-2 下午 3:18)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return String 查询条件
	 */
	private String getStrCndHead(ConditionVO[] vos) {
		String strTmp = null;
		ConditionVO[] newvos = null;
		String[] keys = new String[] { "dm_delivbill_b.pkcusmandoc",
				"dm_delivbill_b.pkcusmandoc1", "bd_cubasdoc.pk_cubasdoc",
				"dm_delivbill_b.pkinv", "dm_delivbill_b.pkinv1",
				"bd_invbasdoc.pk_invbasdoc", "dm_delivbill_b.vbilltype",
				"dm_delivbill_b.pk_transcontainer",
				"dm_delivdaypl.vdelivdayplcode", "dm_delivdaypl.vsrcbillnum",
				"dm_delivbill_b.vuserdef0", "dm_delivbill_b.vuserdef1",
				"dm_delivbill_b.vuserdef2", "dm_delivbill_b.vuserdef3",
				"dm_delivbill_b.vuserdef4", "dm_delivbill_b.vuserdef5",
				"dm_delivbill_b.vuserdef6", "dm_delivbill_b.vuserdef7",
				"dm_delivbill_b.vuserdef8", "dm_delivbill_b.vuserdef9",
				"dm_delivbill_b.vuserdef10", "dm_delivbill_b.vuserdef11",
				"dm_delivbill_b.vuserdef12", "dm_delivbill_b.vuserdef13",
				"dm_delivbill_b.vuserdef14", "dm_delivbill_b.vuserdef15",
				"dm_delivbill_b.vuserdef16", "dm_delivbill_b.vuserdef17",
				"dm_delivbill_b.vuserdef18",
				"dm_delivbill_b.vuserdef19",
				"dm_delivbill_b.pksendstockorg", // 发货库存组织
				"dm_delivbill_b.borderreturn", "isexistpacknum", "waylosstype",
				"fee.pkcustinvoice" };
		newvos = packConditionVO(vos, keys);
		strTmp = getConditionDlg().getWhereSQL(newvos);
		if (strTmp != null) {
			strTmp = nc.vo.jcom.lang.StringUtil.replaceAllString(strTmp,
					"datefrom", "senddate");// nc.vo.pub.util.StringUtil.replaceAllString(strTmp,
			// "datefrom", "senddate");
			strTmp = nc.vo.jcom.lang.StringUtil.replaceAllString(strTmp,
					"dateto", "senddate");// nc.vo.pub.util.StringUtil.replaceAllString(strTmp,
			// "dateto", "senddate");
		}

		return strTmp;
	}

	/**
	 * 创建者：晁志平 功能：获取表体查询条件 参数：由查询窗口得到的条件VO 返回： 例外： 日期：(2001-8-2 下午 3:18)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return String 查询条件
	 */
	protected String getStrCndItem(ConditionVO[] vos) {
		String strTmp = null;
		ConditionVO[] newvos = null;
		String[] keys = new String[] { "vdelivbillcode", "pkdelivmode",
				"dm_delivbill_h.pktrancust", "pktranorg", "datefrom", "dateto",
				"dm_delivbill_h.pkbillperson", "dm_delivbill_h.pkdriver",
				"dm_delivbill_h.bmissionbill", "dm_delivbill_h.vuserdef0",
				"dm_delivbill_h.vuserdef1", "dm_delivbill_h.vuserdef2",
				"dm_delivbill_h.vuserdef3", "dm_delivbill_h.vuserdef4",
				"dm_delivbill_h.vuserdef5", "dm_delivbill_h.vuserdef6",
				"dm_delivbill_h.vuserdef7", "dm_delivbill_h.vuserdef8",
				"dm_delivbill_h.vuserdef9", "dm_delivbill_h.vuserdef10",
				"dm_delivbill_h.vuserdef11", "dm_delivbill_h.vuserdef12",
				"dm_delivbill_h.vuserdef13", "dm_delivbill_h.vuserdef14",
				"dm_delivbill_h.vuserdef15", "dm_delivbill_h.vuserdef16",
				"dm_delivbill_h.vuserdef17", "dm_delivbill_h.vuserdef18",
				"dm_delivbill_h.vuserdef19", "dm_delivbill_h.pkdelivorg", // 该项仅仅对途损节点有用
				"waylosstype" // 该项仅仅对途损节点有用
		};
		newvos = packConditionVO(vos, keys);

		Vector v = new Vector();

		int choice = 100;
		StringBuffer sb = new StringBuffer();
		// 处理“包装明细选择”
		for (int i = 0; i < newvos.length; i++) {
			if (newvos[i].getFieldCode().trim().equals("isexistpacknum")) {
				choice = Integer.parseInt(newvos[i].getValue());

				switch (choice) {
				case 0: // 全部
					break;

				case 1: // 含包装明细
					sb
							.append(
									" EXISTS (select pk_delivpacknum from dm_delivpacknum ")
							.append(
									"where dm_delivpacknum.pk_delivbill_h=dm_delivbill_h.pk_delivbill_h ")
							.append("and dm_delivpacknum.dr=0 ) ");
					break;

				case 2: // 不含包装明细
					sb
							.append(
									" NOT EXISTS (select pk_delivpacknum from dm_delivpacknum ")
							.append(
									"where dm_delivpacknum.pk_delivbill_h=dm_delivbill_h.pk_delivbill_h ")
							.append("and dm_delivpacknum.dr=0 ) ");
					break;

				}
			} else if (newvos[i].getFieldCode().trim().equals(
					"dm_delivbill_b.borderreturn")
					&& !newvos[i].getValue().equals(CConstant.ALL)) { // 是否退货
				newvos[i]
						.setValue(newvos[i].getValue().equals(CConstant.YES) ? "Y"
								: "N");

				// newvos[i].setValue(" '"+newvos[i].getValue()+"' and
				// dm_delivbill_b.vbilltype='21' or
				// dm_delivbill_b.vbilltype<>'21') ");

				newvos[i].setValue(" '" + newvos[i].getValue()
						+ "' and dm_delivbill_b.vbilltype='21' ) ");

				newvos[i].setFieldCode("(dm_delivbill_b.borderreturn");
				newvos[i].setDataType(1); // 整数？
				v.add(newvos[i]);
			} else {
				v.add(newvos[i]);
			}
		}

		newvos = (ConditionVO[]) v.toArray(new ConditionVO[v.size()]);

		strTmp = getConditionDlg().getWhereSQL(newvos);
		if (strTmp != null) {
			// strTmp = nc.vo.pub.util.StringUtil.replaceAllString(strTmp,
			// "dm_delivbill_b.pkcusmandoc1", "dm_delivbill_b.pkcusmandoc");
			// strTmp = nc.vo.pub.util.StringUtil.replaceAllString(strTmp,
			// "dm_delivbill_b.pkinv1", "dm_delivbill_b.pkinv");
			strTmp = nc.vo.jcom.lang.StringUtil
					.replaceAllString(strTmp, "dm_delivbill_b.pkcusmandoc1",
							"dm_delivbill_b.pkcusmandoc");
			strTmp = nc.vo.jcom.lang.StringUtil.replaceAllString(strTmp,
					"dm_delivbill_b.pkinv1", "dm_delivbill_b.pkinv");
		}

		if (choice == 1 || choice == 2) {
			if (strTmp != null) {
				strTmp = strTmp + " and " + sb.toString();
			} else {
				strTmp = sb.toString();
			}
		}

		return strTmp;
	}

	/**
	 * 子类实现该方法，返回业务界面的标题。
	 * 
	 * @version (00-6-6 13:33:25)
	 * @return java.lang.String
	 */
	public String getTitle() {
		String s = StringTools.getSimilarString(getBillCardPanel().getTitle());
		if (s != null) {
			return s;
		} else {
			return NCLangRes.getInstance().getStrByID("40140408",
					"UPT40140408-000046")
			/* @res "发运单" */
			;
		}
	}

	/**
	 * 返回计算运费按钮。 功能： 参数： 返回： 例外： 日期：(2002-9-10 20:21:21) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return nc.ui.pub.ButtonObject
	 */
	public ButtonObject getTransConfirmButton() {
		return boTransConfirm;
	}

	/**
	 * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2002-9-11 10:32:57) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return nc.vo.pub.AggregatedValueObject
	 */
	public nc.vo.pub.AggregatedValueObject getVo() {
		if (null == m_currentbill
				|| m_currentbill.getParentVO() == null
				|| m_currentbill.getParentVO().getAttributeValue(
						"pk_delivbill_h") == null) {
			return null;
		}

		return m_currentbill;
	}

	/**
	 * 设置发运单下拉框备选值
	 */
	protected void initBodyComboBox() {
		initComboBoxOnCard();
		initComboBoxOnList();
	}

	/**
	 * 设置卡片下拉框
	 */
	private void initComboBoxOnCard() {
		// 运费类别
		UIComboBox cbCardItem = (UIComboBox) getBillCardPanel().getHeadItem(
				"isendtype").getComponent();
		cbCardItem.setTranslate(true);
		if (cbCardItem.getItemCount() > 0)
			cbCardItem.removeAllItems();
		getBillCardPanel().getHeadItem("isendtype").setWithIndex(true);
		// 设置数据(运费类别的名称)
		for (int i = 0; i < FreightType.nameoftype.length; i++) {
			cbCardItem.addItem(FreightType.nameoftype[i]);
		}
		cbCardItem.setSelectedItem(FreightType.sAr);
	}

	/**
	 * 设置列表下拉框
	 */
	private void initComboBoxOnList() {
		// 运费类别
		UIComboBox cbListItem = (UIComboBox) getBillListPanel().getHeadItem(
				"isendtype").getComponent();
		cbListItem.setTranslate(true);
		if (cbListItem.getItemCount() > 0)
			cbListItem.removeAllItems();
		getBillListPanel().getHeadItem("isendtype").setWithIndex(true);
		// 设置数据(运费类别的名称)
		for (int i = 0; i < FreightType.nameoftype.length; i++) {
			cbListItem.addItem(FreightType.nameoftype[i]);
		}
		cbListItem.setSelectedItem(FreightType.sNone);
	}

	/**
	 * 初始化按钮组合。 创建日期：(2002-5-10 11:16:28)
	 */
	protected void initFixSubMenuButton() {
		boBill.removeAllChildren();
		// eric
		boOnhandnum = new ButtonObject("可用量查询及打印", "可用量查询及打印", 1, "可用量查询及打印");

		// “合并显示”按钮
		boBillCombin = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("40140408", "UPP40140408-000215")/* @res "合并显示" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("40140408",
						"UPP40140408-000215")/* @res "合并显示" */, 2, "图形");

		boCalculateFee = new ButtonObject(NCLangRes.getInstance().getStrByID(
				"40140408", "UPT40140408-000064")
		/* @res "计算运费" */, nc.ui.ml.NCLangRes.getInstance().getStrByID(
				"40140408", "UPT40140408-000064") /* @res "计算运费" */, 1, "计算运费"); /*
																				 * -=notranslate
																				 * =
																				 * -
																				 */
		boTransConfirm = new ButtonObject(NCLangRes.getInstance().getStrByID(
				"40140408", "UPT40140408-000065")
		/* @res "承运商确认" */, nc.ui.ml.NCLangRes.getInstance().getStrByID(
				"40140408", "UPT40140408-000065") /* @res "承运商确认" */, 1, "承运商确认"); /*
																				 * -=notranslate
																				 * =
																				 * -
																				 */
		boCancelConfirm = new ButtonObject(NCLangRes.getInstance().getStrByID(
				"40140408", "UPT40140408-000057")
		/* @res "承运商取消确认" */, nc.ui.ml.NCLangRes.getInstance().getStrByID(
				"40140408", "UPT40140408-000057") /* @res "承运商取消确认" */, 1,
				"承运商取消确认"); /* -=notranslate=- */
		// boShowListBill = new ButtonObject("发运清单", "显示发运清单", 1);
		boPackageList = new ButtonObject(NCLangRes.getInstance().getStrByID(
				"40140408", "UPT40140408-000070") /*
												 * @res "包装"
												 */, NCLangRes.getInstance()
				.getStrByID("40140408", "UPP40140408-000029") /*
															 * @res "显示包装明细"
															 */, 1, "包装");
		/* -=notranslate=- */
		boGenTaskBill = new ButtonObject(NCLangRes.getInstance().getStrByID(
				"40140408", "UPT40140408-000066")
		/* @res "生成任务单" */, nc.ui.ml.NCLangRes.getInstance().getStrByID(
				"40140408", "UPT40140408-000066") /* @res "生成任务单" */, 1, "生成任务单"); /*
																				 * -=notranslate
																				 * =
																				 * -
																				 */
		boOutBill = new ButtonObject(NCLangRes.getInstance().getStrByID(
				"40140408", "UPT40140408-000052") /* @res "出库" */,
				NCLangRes.getInstance().getStrByID("40140408",
						"UPT40140408-000052") /* @res "出库" */, 1, "出库");
		/* -=notranslate=- */
		boOpenBill = new ButtonObject(NCLangRes.getInstance().getStrByID(
				"40140408", "UPT40140408-000069") /*
												 * @res "打开"
												 */, NCLangRes.getInstance()
				.getStrByID("40140408", "UPT40140408-000069") /*
															 * @res "打开"
															 */, 1, "打开"); // 打开发运单
		// /*-=notranslate=-*/
		boTestCalculate = new ButtonObject(NCLangRes.getInstance().getStrByID(
				"40140408", "UPT40140408-000056")
		/* @res "试算" */, nc.ui.ml.NCLangRes.getInstance().getStrByID(
				"40140408", "UPP40140408-000030") /* @res "运费试算" */, 1, "试算"); // 运费试算
		// /*-=notranslate=-*/
		// 承运商确认和取消
		// boTrancust = new
		// ButtonObject(NCLangRes.getInstance().getStrByID("common",
		// "UC000-0002070")/*@res "承运商"*/,
		// NCLangRes.getInstance().getStrByID("common", "UC000-0002070")/*@res
		// "承运商"*/, 1, "承运商"); /*-=notranslate=-*/
		// boTrancust.removeAllChildren();
		// boTrancust.addChildButton(boTransConfirm);
		// boTrancust.addChildButton(boCancelConfirm);

		boRowCloseOut = new ButtonObject(NCLangRes.getInstance().getStrByID(
				"40140408", "UPT40140408-000091")
		/* @res "行出库关闭" */, nc.ui.ml.NCLangRes.getInstance().getStrByID(
				"40140408", "UPT40140408-000091") /* @res "行出库关闭" */, 1, "行出库关闭"); // 行出库关闭
		// /*-=notranslate=-*/

		boRowOpenOut = new ButtonObject(NCLangRes.getInstance().getStrByID(
				"40140408", "UPT40140408-000092")
		/* @res "行出库打开" */, nc.ui.ml.NCLangRes.getInstance().getStrByID(
				"40140408", "UPT40140408-000092") /* @res "行出库打开" */, 1, "行出库打开"); // 行出库打开
		// /*-=notranslate=-*/

		// 辅助
		boAssistant.removeAllChildren();
		boAssistant.addChildButton(boTestCalculate);
		boAssistant.addChildButton(boCalculateFee);
		boAssistant.addChildButton(boGenTaskBill);
		boAssistant.addChildButton(boTransConfirm);
		boAssistant.addChildButton(boCancelConfirm);
		boAssistant.addChildButton(boOrderQuery);
		boAssistant.addChildButton(boDocument);
		boAssistant.addChildButton(boPrintPreview);
		boAssistant.addChildButton(boPrint);

		// 维护
		boMaintain.removeAllChildren();
		boMaintain.addChildButton(boEdit);
		boMaintain.addChildButton(boSave);
		boMaintain.addChildButton(boCancel);
		boMaintain.addChildButton(boDel);

		boMaintainList.removeAllChildren();
		boMaintainList.addChildButton(boAdd);
		boMaintainList.addChildButton(boEdit);
		boMaintainList.addChildButton(boDel);

		boAssistantList.removeAllChildren();
		boAssistantList.addChildButton(boOrderQuery);
		boAssistantList.addChildButton(boDocument);
		boAssistantList.addChildButton(boPrintPreview);
		boAssistantList.addChildButton(boPrint);

		// 浏览
		boBrowse.removeAllChildren();
		boBrowse.addChildButton(boQuery);
		// boBrowse.addChildButton(boRefresh);
		boBrowse.addChildButton(boFind);
		// boBrowse.addChildButton(boFirst);
		// boBrowse.addChildButton(boPre);
		// boBrowse.addChildButton(boNext);
		// boBrowse.addChildButton(boLast);

		boBrowseList.removeAllChildren();
		boBrowseList.addChildButton(boQuery);
		// boBrowseList.addChildButton(boRefresh);
		boBrowseList.addChildButton(boFind);

		boAction.removeAllChildren();
		boAction.addChildButton(boAudit);
		boAction.addChildButton(boCancelAudit);
		boAction.addChildButton(boEnd);
		boAction.addChildButton(boOpenBill);

		if (getDelivSequence() == 0) {
			boAction.addChildButton(boRowCloseOut);
			boAction.addChildButton(boRowOpenOut);
		}

		if (getDelivSequence() == 0) {
			// 先发运后出库
			aryButtonGroup = new ButtonObject[] { boMaintain, boDelLine,
					boBrowse, boFirst, boPre, boNext, boLast, boPackageList,
					boSwith, boAction, boOutBill,

					boAssistant, boBillCombin, boOnhandnum };
		} else if (getDelivSequence() == 1) {
			// 发运出库并行
			aryButtonGroup = new ButtonObject[] { boMaintain, boDelLine,
					boBrowse, boFirst, boPre, boNext, boLast, boPackageList,
					boSwith, boAction,

					boAssistant, boBillCombin, boOnhandnum };
		} else if (getDelivSequence() == 2) {
			// 先出库后发运
			aryButtonGroup = new ButtonObject[] { boMaintain, boDelLine,
					boBrowse, boFirst, boPre, boNext, boLast, boPackageList,
					boSwith, boAction, boAssistant, boBillCombin, boOnhandnum };
		}
		aryListButtonGroup = new ButtonObject[] { boMaintainList, boBrowseList,
				boSwith, boAssistantList, boBillCombin };

	}

	/**
	 * 单据联查入口
	 */
	public void doQueryAction(ILinkQueryData querydata) {
		String pk_corp = querydata.getPkOrg();
		String billtype = querydata.getBillType();
		String id = querydata.getBillID();
		setCorpID(pk_corp);
		String operator = ClientEnvironment.getInstance().getUser()
				.getPrimaryKey();
		setUserID(operator);
		// 设置界面
		try {
			setName("SaleOrder");
			setSize(774, 419);
			add(getBillCardPanel(), "Center");
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}

		this.setANumItemKeys(new String[] { "dinvassist", "dassistnum" });
		this.setNumItemKeys(new String[] { "dweight", "dvolumn", "ndelivernum",
				"dsendnum", "dsignnum", "dbacknum", "nonwaynum", "dinvnum",
				"dinvweight", "dcancelnum", "dsignnum", "doutnum", "dnum" });
		this.setPriceItemKeys(new String[] { "dunitprice" });
		this.setMoneyItemKey(new String[] { "dmoney" });
		// m_sBillTypeCode= "DM_BILL_TEMPLET_007F";
		setBillTypeCode(DMBillTypeConst.m_delivDelivBill);
		// setTempletID("DM_BILL_TEMPLET_007F");
		setVuserdefCode(DMBillTypeConst.m_delivDelivBill);

		loadPara();

		// 加载参数
		getSystemPara();
		// 初试化币种

		initVariable();
		// 设置节点
		// getFuncRegisterVO().setFunCode(getNodeCode());
		// getClientEnvironment().setModuleCode(getNodeCode());
		// 加载参数
		loadSystemPara();

		initCurrency();
		// 初试化按钮
		initButtons();

		// 设置节点
		String PkCorp = matchpk_Corp(ShowDelivOrg.getDelivOrgPK());

		if (PkCorp.equals(pk_corp)) {
			// 加载模板
			loadCardTemplet(billtype, operator, PkCorp);
			loadListTemplet(billtype, operator, PkCorp);
		} else {
			loadCardTemplet(billtype, operator, pk_corp);
			loadListTemplet(billtype, operator, pk_corp);
		}

		switchInterface();

		setPrimaryKeyName("pk_delivbill_h");
		setBillCodeKeyName("vdelivbillcode");

		// switchButtonStatus(AfterClosed);

		// Modified by xhq 2002/10/07 begin
		UIRefPane nRefPane = (UIRefPane) getBillCardPanel().getBodyItem(
				"dunitprice").getComponent();
		nc.ui.pub.beans.UITextField nUI = (nc.ui.pub.beans.UITextField) nRefPane
				.getUITextField();
		nUI.setDelStr("-");
		nRefPane = (UIRefPane) getBillCardPanel().getBodyItem("dinvweight")
				.getComponent();
		nUI = (nc.ui.pub.beans.UITextField) nRefPane.getUITextField();
		nUI.setDelStr("-");
		nRefPane = (UIRefPane) getBillCardPanel().getBodyItem("dvolumn")
				.getComponent();
		nUI = (nc.ui.pub.beans.UITextField) nRefPane.getUITextField();
		nUI.setDelStr("-");
		nRefPane = (UIRefPane) getBillCardPanel().getBodyItem("dinvnum")
				.getComponent();
		nUI = (nc.ui.pub.beans.UITextField) nRefPane.getUITextField();
		nUI.setDelStr("-");
		nRefPane = (UIRefPane) getBillCardPanel().getBodyItem("dinvassist")
				.getComponent();
		nUI = (nc.ui.pub.beans.UITextField) nRefPane.getUITextField();
		nUI.setDelStr("-");

		getBillCardPanel().getAddLineMenuItem().setEnabled(false);
		getBillCardPanel().getInsertLineMenuItem().setEnabled(false);
		getBillCardPanel().getCopyLineMenuItem().setEnabled(false);
		getBillCardPanel().getPasteLineMenuItem().setEnabled(false);
		// Modified by xhq 2002/10/07 end
		((nc.ui.dm.pub.ref.TrancustRefModel) ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
				.getHeadItem("pktrancust").getComponent()).getRefModel())
				.setDelivOrgPK(getDelivOrgPK());

		nRefPane = (UIRefPane) getBillCardPanel().getHeadItem("pkvehicle")
				.getComponent();
		// pkvehicle
		((nc.ui.dm.pub.ref.VehicleRefModel) nRefPane.getRefModel())
				.setDelivOrgPK(getDelivOrgPK());
		((nc.ui.dm.pub.ref.VehicleRefModel) nRefPane.getRefModel())
				.addWherePart(" and dm_vehicle.bisseal = 'N' ");

		// 修改发运方式参照
		UIRefPane sendType = (UIRefPane) getBillCardPanel().getHeadItem(
				"pkdelivmode").getComponent();
		sendType.setWhereString("(pk_corp = '"+getCorpPrimaryKey()+"' or pk_corp='0001' or pk_corp is null) and issendarranged = 'Y' ");

		// 过滤发货库存组织参照中为成本库存组织的库存组织
		nRefPane = (UIRefPane) getBillCardPanel().getBodyItem(
				"vsendstoreorgname").getComponent();
		if (nRefPane != null) {
			String where = nRefPane.getRefModel().getWherePart();
			where = where == null ? "" : where;
			if (where.trim().length() > 0) {
				where += " and ";
			}
			where += " property in ( 0,1 ) ";
			nRefPane.setWhereString(where);
		}

		// 修改运输部门参照
		UIRefPane deptPane = (UIRefPane) getBillCardPanel().getHeadItem(
				"pktranorg").getComponent();
		deptPane.getRefModel().setWherePart(
				" pk_corp in ('" + getBelongCorpIDofDelivOrg()
						+ "') and canceled <> 'Y' ");
		//

		// 加载数据
		loadCardData(id, PkCorp, operator);
	}

	public void setBtnStatusFalse() {
		setButton(boAdd, false);
		setButton(boEdit, false);
		setButton(boDel, false);
		setButton(boPackageList, false);
		setButton(boSwith, false);
		setButton(boPrint, false);
		setButton(boPrintPreview, false);
		setButton(boQuery, false);
		setButton(boRefresh, false);
		setButton(boCalculateFee, false);
		setButton(boTransConfirm, false);
		setButton(boPre, false);
		setButton(boNext, false);
		setButton(boFirst, false);
		setButton(boLast, false);
		setButton(boBrowse, false);
		setButton(boMaintain, false);
		setButton(boDocument, false);
		setButton(boOrderQuery, false);
		setButton(boAction, false);
		setButton(boCalculateFee, false);
		setButton(boAssistant, false);
		setButton(boBillCombin, false);
		setButton(boOnhandnum, false);
	}

	public String matchpk_Corp(String pkDelivorg) {
		DelivorgVO vo = null;
		String pkcorp = null;
		if (pkDelivorg != null) {
			try {
				vo = DelivorgHelper.findByPrimaryKey(pkDelivorg);
			} catch (Exception e) {
				e.printStackTrace();
			}
			DelivorgHeaderVO vos = (DelivorgHeaderVO) vo.getParentVO();
			pkcorp = vos.getPkcorp();
			return pkcorp;
		}
		pkcorp = getCorpPrimaryKey();
		return pkcorp;
	}

	public void loadPara() {
		// 公司
		setCorpID(getCorpPrimaryKey());
		if (null != getClientEnvironment().getCorporation())
			setCorpName(getClientEnvironment().getCorporation().getUnitname());
		// 操作员
		if (null != getClientEnvironment().getUser()) {
			setUserID(getClientEnvironment().getUser().getPrimaryKey());
			setUserName(getClientEnvironment().getUser().getUserName());
		}
		// 日期
		if (getClientEnvironment().getDate() != null)
			setLogDate(getClientEnvironment().getDate());

		Container parent = ClientEnvironment.getInstance().getDesktopApplet();

		// 检查目前的发运组织是否是登录公司可见的发运组织
		DelivorgRef delivorgRef = new DelivorgRef(getCorpPrimaryKey(), parent);
		delivorgRef.setAgentCorp(getCorpPrimaryKey());
		// if (null != StaticMemoryVariable.DelivOrgPK) {
		// delivorgRef.setPK(StaticMemoryVariable.DelivOrgPK);
		// StaticMemoryVariable.DelivOrgPK = delivorgRef.getRefPK();
		// }
		setDelivOrgCode(ShowDelivOrg.getDelivOrgCode());
		setDelivOrgPK(ShowDelivOrg.getDelivOrgPK());
		setDelivOrgName(ShowDelivOrg.getDelivOrgName());
		if (ClientEnvironment.getInstance().getValue("DM4014DelivSequence") != null)
			setDelivSequence(new Integer(ShowDelivOrg.getDelivSequence())
					.intValue());
		if (null != ShowDelivOrg.getDelivOrgPK()) {
			delivorgRef.setPK(ShowDelivOrg.getDelivOrgPK());
		}
		if (getDelivOrgPK() != null) {
			try {
				ArrayList list = DmHelper
						.queryCorpIDsByDelivOrgID(getDelivOrgPK());
				setAgentCorpIDsofDelivOrg(list);
			} catch (Exception e) {
				throw new Error(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4014", "UPP4014-000038")/* @res "未找到发运组织代理的公司" */);
			}
		}
	}

	public void initialize() {
	}

	/**
	 * 初始化数据及界面。
	 * <p>
	 * 包括：设置数量、单价、金额等字段的显示格式，
	 * <p>
	 * 设置列表界面和卡片界面的各种监听，
	 * <p>
	 * 设置命令按钮的状态，
	 * <p>
	 * 对参照允许接收字符的限定，
	 * <p>
	 * 设置承运商参照的默认发运组织，
	 * <p>
	 * 设置“车辆”、“发运方式”、“运输部门”等参照的附加条件。
	 * 
	 * @author zxj
	 */
	public void initializeNew() {
		try {
			// 设置数量字段
			this.setANumItemKeys(new String[] { "dinvassist", "dassistnum" });
			this.setNumItemKeys(new String[] { "dweight", "dvolumn",
					"ndelivernum", "dsendnum", "dsignnum", "dbacknum",
					"nonwaynum", "dinvnum", "dinvweight", "dcancelnum",
					"dsignnum", "doutnum", "dnum", "dpackweight",
					"dpackvolumn", "dpacknum", "dallpacknum", "dallweight",
					"dallvolumn", "dfactweight" });
			// 设置单价字段
			this.setPriceItemKeys(new String[] { "dunitprice" });
			// 设置金额字段
			this.setMoneyItemKey(new String[] { "dmoney" });

			// m_sBillTypeCode= "DM_BILL_TEMPLET_007F";
			setBillTypeCode(DMBillTypeConst.m_delivDelivBill);
			// setTempletID("DM_BILL_TEMPLET_007F");
			setVuserdefCode(DMBillTypeConst.m_delivDelivBill);
			super.initialize();

			// 表头支持多选
			getBillListPanel().getHeadTable().setSelectionMode(
					javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

			getBillCardPanel().addEditListener(this);
			getBillListPanel().getHeadBillModel().addSortRelaObjectListener2(
					this);
			getBillCardPanel().setBillBeforeEditListenerHeadTail(this);

			// 行号按数值排序
			getBillCardPanel().getBillModel().setSortPrepareListener(this);
			getBillListPanel().getBodyBillModel().setSortPrepareListener(this);

			// addSpecialRef();
			getBillCardPanel().setEnabled(false);
			((DMBillCardPanel) getBillCardPanel()).setAutoAddEditLine(false);
			((DMBillCardPanel) getBillCardPanel()).setAutoAddLimitLine(false);

			m_headers = new DelivbillHHeaderVO[0];
			// 卡片界面当前单据
			m_currentbill = new DelivbillHVO();

			// 列表界面当前单据
			m_bills = new DelivbillHVO[0];
			// 隐藏定位按钮
			// super.boFind.setEnabled(false);
			// super.boFind.setVisible(false);
			// 隐藏新增按钮
			super.boAdd.setEnabled(false);
			super.boAdd.setVisible(false);
			// 初始时刷新不可用
			setButton(boRefresh, false);
			switchButtonStatus(Init);

			// Modified by xhq 2002/10/07 begin
			// 对参照允许接收字符的限定
			UIRefPane nRefPane = (UIRefPane) getBillCardPanel().getBodyItem(
					"dunitprice").getComponent();
			nc.ui.pub.beans.UITextField nUI = (nc.ui.pub.beans.UITextField) nRefPane
					.getUITextField();
			nUI.setDelStr("-");
			nRefPane = (UIRefPane) getBillCardPanel().getBodyItem("dinvweight")
					.getComponent();
			nUI = (nc.ui.pub.beans.UITextField) nRefPane.getUITextField();
			nUI.setDelStr("-");
			nRefPane = (UIRefPane) getBillCardPanel().getBodyItem("dvolumn")
					.getComponent();
			nUI = (nc.ui.pub.beans.UITextField) nRefPane.getUITextField();
			nUI.setDelStr("-");
			nRefPane = (UIRefPane) getBillCardPanel().getBodyItem("dinvnum")
					.getComponent();
			nUI = (nc.ui.pub.beans.UITextField) nRefPane.getUITextField();
			nUI.setDelStr("-");
			nRefPane = (UIRefPane) getBillCardPanel().getBodyItem("dinvassist")
					.getComponent();
			nUI = (nc.ui.pub.beans.UITextField) nRefPane.getUITextField();
			nUI.setDelStr("-");

			getBillCardPanel().getAddLineMenuItem().setEnabled(false);
			getBillCardPanel().getInsertLineMenuItem().setEnabled(false);
			getBillCardPanel().getCopyLineMenuItem().setEnabled(false);
			getBillCardPanel().getPasteLineMenuItem().setEnabled(false);
			// Modified by xhq 2002/10/07 end
			// 设置承运商参照的默认发运组织
			TrancustRefModel model = ((TrancustRefModel) ((UIRefPane) getBillCardPanel()
					.getHeadItem("pktrancust").getComponent()).getRefModel());
			model.setDelivOrgPK(getDelivOrgPK());
			model.setMatchPkWithWherePart(true);

			// 设置“车辆”pkvehicle
			nRefPane = (UIRefPane) getBillCardPanel().getHeadItem("pkvehicle")
					.getComponent();
			((nc.ui.dm.pub.ref.VehicleRefModel) nRefPane.getRefModel())
					.setDelivOrgPK(getDelivOrgPK());
			((nc.ui.dm.pub.ref.VehicleRefModel) nRefPane.getRefModel())
					.addWherePart(" and dm_vehicle.bisseal = 'N' ");

			// 路线 nc.ui.dm.pub.ref.RouteRefModel
			nRefPane = (UIRefPane) getBillCardPanel().getHeadItem(
					"pkdelivroute").getComponent();
			((nc.ui.dm.pub.ref.RouteRefModel) nRefPane.getRefModel())
					.setDelivOrgPK(getDelivOrgPK());

			// 车型 pkvehicletype nc.ui.dm.pub.ref.VehicletypeRefModel
			nRefPane = (UIRefPane) getBillCardPanel().getHeadItem(
					"pkvehicletype").getComponent();
			((nc.ui.dm.pub.ref.VehicletypeRefModel) nRefPane.getRefModel())
					.setDelivOrgPK(getDelivOrgPK());

			// 司机 pkdriver nc.ui.dm.pub.ref.DriverRefModel
			nRefPane = (UIRefPane) getBillCardPanel().getHeadItem("pkdriver")
					.getComponent();
			((nc.ui.dm.pub.ref.DriverRefModel) nRefPane.getRefModel())
					.setDelivOrgPK(getDelivOrgPK());

			// 修改发运方式参照
			UIRefPane sendType = (UIRefPane) getBillCardPanel().getHeadItem(
					"pkdelivmode").getComponent();//edit by shikun 2014-11-27 添加公司过滤
			sendType.setWhereString(" (pk_corp = '"+getCorpPrimaryKey()+"' or pk_corp='0001' or pk_corp is null) and issendarranged = 'Y' ");

			// 修改运输部门参照
			UIRefPane deptPane = (UIRefPane) getBillCardPanel().getHeadItem(
					"pktranorg").getComponent();
			deptPane.getRefModel().setWherePart(
					" pk_corp in ('" + getBelongCorpIDofDelivOrg()
							+ "') and canceled <> 'Y' ");
			//
			setPrimaryKeyName("pk_delivbill_h");
			setBillCodeKeyName("vdelivbillcode");
			((DMBillCardPanel) getBillCardPanel()).setRowNumKey("irownumber");

			// 过滤发货库存组织参照中为成本库存组织的库存组织
			nRefPane = (UIRefPane) getBillCardPanel().getBodyItem(
					"vsendstoreorgname").getComponent();
			if (nRefPane != null) {
				String where = nRefPane.getRefModel().getWherePart();
				where = where == null ? "" : where;
				if (where.trim().length() > 0) {
					where += " and ";
				}
				where += " property in ( 0,1 ) ";
				nRefPane.setWhereString(where);
			}
			if (bIsForSofee) {
				getBillListPanel().setMultiSelect(true);
			}
		} catch (Exception ex) {
			ExceptionUITools tool = new ExceptionUITools();
			tool.showMessage(ex, this);
		}
	}

	/**
	 * 功能：初始化参照 参数： rownow 行号 colnow 列号 bcp 卡片界面 返回：void 例外： 日期：(2002-5-8
	 * 19:04:08) 2002-06-19 韩卫 修改 （1）添加判断和显示发运组织选择提示框，
	 * （2）添加m_OrgNoShowFlag，变量和getOrgnoshowFlag、setOrgnoshowFlag方法
	 */
	public void initRef(int rownow, int colnow, DMBillCardPanel bcp) {
		String sItemKey = bcp.getBodyPanel().getBodyKeyByCol(colnow).trim();
		// bcp.getBillModel().getBodyKeyByCol(colnow);
		if (!bcp.getBillData().getBodyItem(sItemKey).isEnabled()) {
			return;
		}
		/*
		 * String sInvID= null; String sWhID= null; try { if
		 * (sItemKey.equals("vassistmeaname")) { //检查辅计量 try { sInvID= (String)
		 * bcp.getBodyValueAt(rownow, "pkinv"); } catch (Exception e) { } if
		 * (null != sInvID) { filterMeas(sInvID, "vassistmeaname"); } } //if
		 * (sItemKey.equals("castunitname")) { ////检查辅计量 //try { //sInvID=
		 * (String) bcp.getBodyValueAt(rownow, "cinventorycode"); //} catch
		 * (Exception e) { //} //if (null != sInvID) { //filterMeas(sInvID,
		 * "castunitname"); //} //} //为vfree0 if (sItemKey.equals("vfree0")) {
		 * try { sInvID= (String) bcp.getBodyValueAt(rownow, "pkinv"); } catch
		 * (Exception e) { } if (null != sInvID) { //向自由项参照传入数据 //初始化
		 * getFreeItemRefPane().setFreeItemParam(queryInvInfo(sInvID)); } }
		 * //向批次号传递参数 if (sItemKey.equals("vbatchcode")) { try { sInvID=
		 * (String) bcp.getBodyValueAt(rownow, "pkinv"); } catch (Exception e) {
		 * } if (null != sInvID) { WhVO wvo= new WhVO(); try { sWhID= (String)
		 * bcp.getBodyValueAt(rownow, "pksendstore"); } catch (Exception e) { }
		 * if (null == sWhID) { try { sWhID= (String) bcp.getBodyValueAt(rownow,
		 * "pksendstock"); } catch (Exception e) { } } if (null != sWhID) { wvo=
		 * queryWhInfo(sWhID); } getLotNumbRefPane().setParameter(wvo,
		 * queryInvInfo(sInvID)); } } } catch (Exception e) { }
		 */
	}

	/**
	 * 操作是否结束。 功能： 参数： 返回： 例外： 日期：(2002-9-17 20:02:39) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return boolean
	 */
	public boolean isOperationFinish() {
		return m_isOperationFinish;
	}

	/**
	 * 列表表头行列变换后事件处理。 创建日期：(2001-6-23 13:42:53)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public void listHeadRowChange(BillEditEvent e) {
		try {
			int i;

			String state = getShowState();

			if (state.equals(DMBillStatus.List)) {
				i = e.getRow();
				if (i < 0
						|| i >= getBillListPanel().getHeadTable().getRowCount()) {
					return;
				}
				m_currentbill = m_bills[i];
				((DelivbillHHeaderVO) m_currentbill.getParentVO())
						.setVdoname(getDelivOrgName());

				DelivbillHItemVO[] items = (DelivbillHItemVO[]) m_currentbill
						.getChildrenVO();
				if (items != null && items.length != 0 && items[0] != null
						&& items[0].getVinvcode() == null) {
					execFormulaBodys(items);
					// 存货自由项改为公式带出
					nc.ui.scm.pub.FreeVOParse freeVOParse = new nc.ui.scm.pub.FreeVOParse();
					freeVOParse.setFreeVO(items, null, "pkinv", false);
				}

				getBillListPanel()
						.setBodyValueVO(m_currentbill.getChildrenVO());
				getBillListPanel().getBodyBillModel().execLoadFormula();
				switchButtonStatusWithBill();
			}
		} catch (Exception e1) {
			this.showErrorMessage(e1.toString());
			handleException(e1);
		}

	}

	/**
	 * 加载卡片数据 创建日期：(2001-4-21 10:36:57)
	 */
	private void loadCardData(String id, String userId, String pkCorp) {
		try {
			// 用id拼where子句
			if (id == null) {
				id = "";
			}
			String whereClause = " pk_delivbill_h='" + id + "'";

			DMDataVO dmdvoSendStororg = new DMDataVO();
			dmdvoSendStororg.setAttributeValue("userid", getUserID());
			dmdvoSendStororg.setAttributeValue("pkcorp", getCorpID());
			dmdvoSendStororg.setAttributeValue("agentcorpids",
					getAgentCorpIDsofDelivOrg());

			DelivbillHVO[] vo = DelivbillHBO_Client.findDelivBillsForSign(null,
					whereClause, null, dmdvoSendStororg);
			m_bills = vo;
			m_headers = new DelivbillHHeaderVO[vo.length];
			for (int i = 0; i < vo.length; i++)
				m_headers[i] = (DelivbillHHeaderVO) vo[i].getParentVO();
			switchbtnstatus(vo[0]);
			// 按纽制灰
			setBtnStatusFalse();
			// 存货自由项改为公式带出
			nc.ui.scm.pub.FreeVOParse freeVOParse = new nc.ui.scm.pub.FreeVOParse();
			for (int i = 0; i < vo.length; i++) {
				DelivbillHItemVO[] items = (DelivbillHItemVO[]) vo[i]
						.getChildrenVO();
				freeVOParse.setFreeVO(items, null, "pkinv", false);
			}

			if (vo.length > 0) {
				((DelivbillHHeaderVO) (vo[0].getParentVO()))
						.setH_confirmarrivedate((UFDate) ((vo[0]
								.getChildrenVO())[0]
								.getAttributeValue("confirmarrivedate")));
				getBillCardPanel().setBillValueVO(vo[0]);

				// getBillCardPanel().getBillModel().execLoadFormula();//该条语句实际上执行的只是表体部分的加载公式
				// getBillCardPanel().execHeadTailLoadFormulas();
				//
				// 数据由公式带出
				execHeadTailFormulas(
						new CircularlyAccessibleValueObject[] { vo[0]
								.getParentVO() }, false);
				execBodyFormulas(vo[0].getChildrenVO(), false);

				m_currentbill = vo[0];
				DelivbillHHeaderVO headVO = (DelivbillHHeaderVO) vo[0]
						.getParentVO();
				if (headVO != null) {
					UIRefPane currentRef = ((UIRefPane) getBillCardPanel()
							.getHeadItem("pkdelivroute").getComponent());
					String pkdelivroute = (String) m_currentbill.getParentVO()
							.getAttributeValue("pkdelivroute");
					currentRef.setPK(pkdelivroute);
					getBillCardPanel().setHeadItem("vroutename",
							currentRef.getRefName());
					updateRouteDesc();
				}

				getBillCardPanel().updateValue();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void switchbtnstatus(DelivbillHVO vo) {
		setButton(boAdd, true);
		setButton(boEdit, true);
		setButton(boDel, true);

		setButton(boSave, false);
		setButton(boPackageList, true);
		setButton(boCancel, false);
		setButton(boLine, false);
		setButton(boAddLine, false);
		setButton(boDelLine, false);

		setButton(boSwith, true);
		setButton(boPrint, true);
		setButton(boPrintPreview, true);
		setButton(boQuery, true);
		setButton(boRefresh, true);
		// setButton(boCalculateFee, true);
		evaluateButtonCalculateFee(vo);
		String b = (String) getBillCardPanel().getHeadItem("bconfirm")
				.getValueObject();
		boolean flag = Boolean.valueOf(b).booleanValue();
		if (!flag) {
			setButton(boTransConfirm, true);
			setButton(boCancelConfirm, false);
		} else {
			setButton(boTransConfirm, false);
			setButton(boCancelConfirm, true);
		}

		setButton(boOutBill, false);
		setButton(boGenTaskBill, false);
		setButton(boAudit, !DM001.booleanValue());
		setButton(boCancelAudit, false);
		setButton(boRowCloseOut, false);
		setButton(boRowOpenOut, false);

		setButton(boEnd, false);
		setButton(boOpenBill, false);
		setButton(boPre, true);
		setButton(boNext, true);
		setButton(boFirst, true);
		setButton(boLast, true);
	}

	public void evaluateButtonCalculateFee(DelivbillHVO vo) {
		DelivbillHHeaderVO head = (DelivbillHHeaderVO) vo.getParentVO();
		// 如果参数值为不控制：则运费计算按钮在发运单自由、审批状态都可用，发运单审批时不自动计算运费（华孚）
		if (DM015 == null) {
			DM015 = "不控制";
		}

		if (DM015.equals("不控制")) {
			setButton(boCalculateFee, true);
		} else if (DM015.equals("审批后")) {
			// 如果参数值为审批后：则运费计算按钮只有在发运单审批后才可用。
			if (StringTools.getSimilarString(head.getPkapprperson()) == null) {
				setButton(boCalculateFee, false);
			} else {
				setButton(boCalculateFee, true);
			}
		} else if (DM015.equals("保存后")) {
			// 如果参数值为保存后：运费计算按钮显示，用户需手工计算运费.（自由状态有效）
			if (StringTools.getSimilarString(head.getPkapprperson()) == null) {
				setButton(boCalculateFee, true);
			} else {
				setButton(boCalculateFee, false);
			}
		}
	}

	/**
	 * 加载卡片模板。(单据联查调用) 创建日期：(2001-11-15 9:03:35)
	 */
	private void loadCardTemplet(String billtype, String operator,
			String pk_corp) {
		// 设置业务类型
		// for (int i=0;i<boBusiType.getChildCount();i++){
		// if (boBusiType.getChildButtonGroup()[i].isSelected()){
		// getBillCardPanel().setBusiType(boBusiType.getChildButtonGroup()[i].getTag());
		// break;
		// }
		// }

		setShowState(DMBillStatus.Card);

		showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON",
				"UPPSCMCommon-000136") /* @res "开始加载模板...." */);

		BillData bd = new BillData(ivjBillCardPanel.getTempletData(billtype,
				null, operator, pk_corp));
		// BillData bd = new BillData(getBillCardPanel().getTempletData());
		// BillData bd= new
		// BillData(getBillCardPanel().getTempletData(getTempletID()));

		// 改变界面
		setCardPanel(bd);

		// 设置界面，置入数据源
		getBillCardPanel().setBillData(bd);

		// initState();
		//    
		// //初始化按纽
		// initButtons();
		// super.initialize();
		// initFixSubMenuButton();
		// aryButtonGroup = super.getBillButtons();
		//    
		// //加载按纽
		// setButtons(aryButtonGroup);

		// 限制输入长度
		setInputLimit();

		// 设置下拉框
		initComboBoxOnCard();

		// 初试化状态
		initState();

		switchInterface();

		// 设置默认业务类型
		if (boBusiType != null && boBusiType.getTag() != null) {
			getBillCardPanel().setBusiType(boBusiType.getTag());
		}

		// 设置合计监听
		getBillCardPanel().addBodyTotalListener(this);
		getBillCardPanel().getBillTable().getSelectionModel()
				.addListSelectionListener(this);
		getBillCardPanel().getBillTable().getColumnModel()
				.addColumnModelListener(this);
		getBillCardPanel().getBillModel().addTableModelListener(this);

		showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON",
				"UPPSCMCommon-000176") /* @res "模板加载成功！" */);
	}

	/**
	 *加载列表模版
	 * 
	 */
	public void loadListTemplet(String billtype, String operator, String pk_corp) {

		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
				"scmcommon", "UPPSCMCommon-000135")/* @res "开始加载列表模板...." */);

		BillListData bd = new BillListData(getBillListPanel()
				.getDefaultTemplet(billtype, null, operator, pk_corp));

		// 改变界面
		setListPanelByPara(bd);

		// 设置界面，置入数据源
		getBillListPanel().setListData(bd);

		// 设置下拉框
		initBodyComboBox();

		getBillListPanel().addEditListener(this);
		getBillListPanel().getHeadTable().addMouseListener(this);
		// 合计行处理
		getBillListPanel().getChildListPanel().setTatolRowShow(true);

		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
				"scmcommon", "UPPSCMCommon-000147")/* @res "列表模板加载成功！" */);
	}

	/**
	 * 对列表界面表头部分的鼠标双击事件进行处理。 如果双击列表界面的表头行，则将界面切换到卡片界面，显示被双击的那条发运单记录。
	 * 
	 * @param e
	 *            鼠标事件
	 * @author zxj
	 */
	public void mouseClicked(java.awt.event.MouseEvent e) {
		m_isOperationFinish = false;
		int i;
		// 对卡片界面的鼠标双击事件不作处理
		if (e.getSource() == getBillListPanel().getHeadTable()) {
			i = getBillListPanel().getHeadTable().getSelectedRow();
			if (e.getClickCount() == 2) {

				if (i < 0 || i > m_headers.length) { // zxping's comment: This
					// branch
					// can not reach at all
					showOkCancelMessage(NCLangRes.getInstance().getStrByID(
							"SCMCOMMON", "UPPSCMCommon-000203") /* @res "请选择一行" */);
					return;
				}

				if (getShowState().equals(DMBillStatus.Card)) {
					// 参见：java.awt.AWTEventMulticaster.mouseClicked(MouseEvent)
					/**
					 * Handles the mouseClicked event by invoking the
					 * mouseClicked methods on listener-a and listener-b.
					 * 
					 * @param e
					 *            the mouse event
					 */
					// public void mouseClicked(MouseEvent e) {
					// ((MouseListener)a).mouseClicked(e);
					// ((MouseListener)b).mouseClicked(e);
					// }
					return;
				}

				onSwith(); // 将界面切换到卡片界面
			}
			m_listRow = i;
		}
		// else if (e.getSource() == getBillCardPanel().getBodyTable()
		// && getShowState().equals(DMBillStatus.Card) && m_editFlag==1) {
		// 采购走发运，发运组织并行，
		// 编辑发运单时应不允许对来源为采购的发运单中“已出库数量”字段进行修改.
		// (采购走发运不可能有出库数量)
		// i = getBillCardPanel().getBodyUIPanel().getTable().getSelectedRow();
		// String vbilltype = getBillCardPanel().getBodyValueAt()
		// if()

		// }

		m_isOperationFinish = true;
	}

	/**
	 * 鼠标进入。不做任何处理。 创建日期：(2002-5-17 19:54:49)
	 * 
	 * @param e
	 *            java.awt.event.MouseEvent
	 */
	public void mouseEntered(java.awt.event.MouseEvent e) {
	}

	/**
	 * 鼠标离开。不做任何处理。 创建日期：(2002-5-17 19:54:49)
	 * 
	 * @param e
	 *            java.awt.event.MouseEvent
	 */
	public void mouseExited(java.awt.event.MouseEvent e) {
	}

	/**
	 * 鼠标按下。不做任何处理。 创建日期：(2002-5-17 19:54:49)
	 * 
	 * @param e
	 *            java.awt.event.MouseEvent
	 */
	public void mousePressed(java.awt.event.MouseEvent e) {
	}

	/**
	 * 鼠标释放。不做任何处理。 创建日期：(2002-5-17 19:54:49)
	 * 
	 * @param e
	 *            java.awt.event.MouseEvent
	 */
	public void mouseReleased(java.awt.event.MouseEvent e) {
	}

	/**
	 * 新增。 创建日期：(2002-5-16 9:09:44)
	 */
	public void onAdd() {
		this.hasChangedPack = true;
		m_listRow = m_headers.length;
		// 清空界面
		if (getShowState().equals(DMBillStatus.List)) {
			m_biscardadd = false;
			super.onSwith();
		} else {
			m_biscardadd = true;
			// getBillCardPanel().resumeValue();

			// 放开单据号的编辑
		}
		getBillCardPanel().getHeadItem("vdelivbillcode").setEdit(true);

		// DelivbillHVO newVO = new DelivbillHVO();
		// newVO.setParentVO(new DelivbillHHeaderVO());
		// newVO.setChildrenVO(new DelivbillHItemVO[0]);
		// getBillCardPanel().setBillValueVO(newVO);
		getBillCardPanel().addNew();
		getBillCardPanel().setEnabled(true);

		setNoEditItem();
		switchButtonStatus(CardBillEditing);
		setEditFlag(0);

		// 新增时置入发运组织名称
		getBillCardPanel().setHeadItem("vdoname", getDelivOrgName());

		// 记录发生时间
		try {
			m_ufdtAddTime = ((nc.itf.scm.recordtime.IRecordTime) nc.bs.framework.common.NCLocator
					.getInstance().lookup(
							nc.itf.scm.recordtime.IRecordTime.class.getName()))
					.getTimeStamp();
		} catch (Exception e) {
			showErrorMessage(NCLangRes.getInstance().getStrByID("40140408",
					"UPP40140408-000031") /* @res "取得生成时间错误!" */);
			m_ufdtAddTime = null;
		}

	}

	/**
	 * 审批。 创建日期：(2002-6-20 9:29:52)
	 */
	public void onAudit() {
		try {
			showHintMessage(NCLangRes.getInstance().getStrByID("40140408",
					"UPP40140408-000032") /* @res "开始审核！" */);
			boolean bIsSaveDelivFee = true;
			if (!DM015.equals("审批时自动")) {
				bIsSaveDelivFee = false;
			}

			UFDate ufdate = null;
			m_isOperationFinish = false;
			DelivbillHHeaderVO header = (DelivbillHHeaderVO) m_currentbill
					.getParentVO();
			DelivbillHItemVO[] items = (DelivbillHItemVO[]) m_currentbill
					.getChildrenVO();

			// 设置表体状态
			for (int i = 0; i < items.length; i++) {
				items[i].setIrowstatus(new Integer(DelivBillStatus.Audit));
				items[i].setStatus(nc.vo.pub.VOStatus.UPDATED);
			}
			// 设置表头状态
			header.setStatus(nc.vo.pub.VOStatus.UPDATED);
			header.setPkapprperson(getUserID());
			header.setVapprpersonname(getUserName());
			// 设置审核日期
			ufdate = new UFDate(System.currentTimeMillis());
			header.setApprdate(ufdate);
			// 设置审核时间
			UFDateTime currentTime = new UFDateTime();
			currentTime = ClientEnvironment.getServerTime();
			header.setTaudittime(currentTime.toString());
			// 设置用户ID
			header.setAttributeValue("userid", getUserID());
			m_currentbill.setGenOIDPK(getCorpID());

			// modified by czp on 2002-10-06 begin
			// 校验必输项
			if (!checkVO(m_currentbill,
					(nc.ui.dm.pub.cardpanel.DMBillCardPanel) getBillCardPanel())) {
				return;
			}
			java.util.ArrayList ary = null;

			if (bIsSaveDelivFee) {
				// 得到运费单vo
				nc.vo.dm.dm107.DelivfeebillHVO delivfeevo = getDelivFeeBillVO();
				// 发运单审批同时生成运费单
				ary = DelivbillHBO_Client.auditAndGenDelivfee(m_currentbill,
						delivfeevo, new ClientLink(getClientEnvironment()));
			} else {
				// 审批不生成运费单
				ary = DelivbillHBO_Client.audit(m_currentbill, new ClientLink(
						getClientEnvironment()));
			}
			if (null != ary && ary.size() > 0) {
				if (m_currentbill.getParentVO() != null) {
					m_currentbill.getParentVO().setAttributeValue("ts",
							ary.get(ary.size() - 1));
				}
			}
			// modified by czp on 2002-10-06 end

			getBillCardPanel().setBillValueVO(m_currentbill);
			getBillCardPanel().getBillModel().execLoadFormula();
			getBillCardPanel().updateValue();
			switchButtonStatusWithBill();
			m_isOperationFinish = true;
			showHintMessage(NCLangRes.getInstance().getStrByID("common",
					"UCH010") /* @res "审核成功！" */);
		} catch (Exception e) {
			handleException(e);
			showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON",
					"UPPSCMCommon-000235") /* @res "审核失败" */);

			DelivbillHHeaderVO header = (DelivbillHHeaderVO) m_currentbill
					.getParentVO();
			header.setPkapprperson(null);
			header.setVapprpersonname(null);
			header.setApprdate(null);
			header.setTaudittime(null);
		}
	}

	/**
	 * 子类实现该方法，响应按钮事件。
	 * 
	 * @version (00-6-1 10:32:59)
	 * @param bo
	 *            ButtonObject
	 */
	public void onButtonClicked(nc.ui.pub.ButtonObject bo) {

		// getBillCardPanel().stopEditing();

		if (isListHeaderMultiSelected(bo, null)) {
			return;
		}

		if (bo == boAddLine) {
			onAddLine();
		} else if (bo == boDelLine) {
			onDelLine();
		} else if (bo == boCopyLine) {
			onCopyLine();
		} else if (bo == boPasteLine) {
			onPasteLine();
		} else if (bo == boSave) {
			onSave();
		} else if (bo == boCancel) {
			onCancel();
		} else if (bo == boEdit) {
			onEdit();
		} else if (bo == boSwith) {
			onSwith();
		} else if (bo == boPrint) {
			onPrint();
		} else if (bo == boFind) {
			onFind();
		} else if (bo == boFirst) {
			onFirst();
		} else if (bo == boLast) {
			onLast();
		} else if (bo == boPre) {
			onPre();
		} else if (bo == boNext) {
			onNext();
		} else if (bo == boQuery) {
			onQuery();
		} else if (bo.getParent() == boBusiType) { // 业务类型
			onBusiType(bo);
		} else if (bo.getParent() == boAdd) { // 新增
			onNew(bo);
		} else if (bo == boAdd) {
			onAdd();
		} else if (bo == boDel) {
			onDel();
		} else if (bo == boCalculateFee) {
			onCalculateFee();
		} else if (bo == boTransConfirm) {
			onTransConfirm();
		} else if (bo == boAudit) {
			onAudit();
		} else if (bo == boCancelAudit) {
			onCancelAudit();
			// else if (bo == boShowListBill)
			// onShowListBill();
		} else if (bo == boGenTaskBill) {
			onGenTaskBill();
		} else if (bo == boEnd) {
			onEnd();
		} else if (bo == boCancelConfirm) {
			onTransCancelConfirm();
		} else if (bo == boOutBill) {
			onOutBill();     //出库
		} else if (bo == boPackageList) {
			onPackageList();
		} else if (bo == boOpenBill) {
			onOpen();
		} else if (bo == boTestCalculate) {
			onTestCalculateFee();

		} else if (bo == boRowCloseOut) { // 行出库关闭
			onRowCloseOut();
		} else if (bo == boRowOpenOut) { // 行出库打开
			onRowOpenOut();
		} else if (bo == boRefresh) { // 刷新
			onRefresh();
		} else if (bo == boBillCombin) // 显示合并
		{
			onBillCombin();
		} else if (bo == boOnhandnum) {
			onHandnumQuery();
		} else {
			boolean extraButtonFired = false;
			ButtonObject[] buttons = getExtendBtns();
			if (buttons != null && buttons.length > 0) {
				int length = buttons.length;
				for (int i = 0; i < length; i++) {
					if (buttons[i] == bo) {
						extraButtonFired = true;
						break;
					}
				}
			}
			// 二次开发按钮出发的点击事件
			if (extraButtonFired) {
				onExtendBtnsClick(bo);
			} else {
				super.onButtonClicked(bo);
			}
		}

	}

	// 行出库关闭
	public void onRowCloseOut() {
		m_isOperationFinish = false;

		int[] iSelectedRows = getBillCardPanel().getBillTable()
				.getSelectedRows();
		if (iSelectedRows == null || iSelectedRows.length == 0) {
			showOkCancelMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON",
					"UPPSCMCommon-000203") /* @res "请选择一行" */);
			return;
		}

		DelivbillHHeaderVO header = (DelivbillHHeaderVO) m_currentbill
				.getParentVO();
		DelivbillHItemVO[] items = (DelivbillHItemVO[]) m_currentbill
				.getChildrenVO();

		// 只将选中的行传到后台进行 行出库关闭 操作
		// 同时，1、进行是否已经 行出库关闭 进行检查
		// 2、如果发运单行来源单据为采购订单时，不予处理
		Vector v = new Vector();
		for (int i = 0; i < iSelectedRows.length; i++) {
			if (StringTools.getSimilarBoolean(items[iSelectedRows[i]]
					.getBcloseout())) {
				String[] value = new String[] { String
						.valueOf(iSelectedRows[i] + 1) };
				String s = NCLangRes.getInstance().getStrByID("40140408",
						"UPP40140408-000189", null, value) /*
															 * @res
															 * "第 {0} 行已经行出库关闭，请重新选择！"
															 */;
				dispWarningMessage(s);
				return;
			}

			if (items[iSelectedRows[i]].getVbilltype().equals("21")) {
				String[] value = new String[] { String
						.valueOf(iSelectedRows[i] + 1) };
				String s = NCLangRes.getInstance().getStrByID("40140408",
						"UPP40140408-000191", null, value) /*
															 * @res "第 {0}
															 * 行来源单据为采购订单
															 * ，不能实施行出库关闭或者行出库打开操作
															 * ，请重新选择！"
															 */;
				dispWarningMessage(s);
				return;
			}

			v.add(items[iSelectedRows[i]]);
		}
		items = new DelivbillHItemVO[v.size()];
		v.toArray(items);

		// 进行 行出库关闭 操作前，保存前台发运单数据表体状态、部分字段值，以备后台操作失败予以恢复
		DelivbillHVO bakCurrVO = (DelivbillHVO) m_currentbill.clone();

		DelivbillHVO savingVO = new DelivbillHVO();
		savingVO.setParentVO(header);
		savingVO.setChildrenVO(items);

		try {
			// 准备数据
			header.setStatus(nc.vo.pub.VOStatus.UPDATED);
			for (int i = 0; i < items.length; i++) {
				items[i].setBcloseout(new UFBoolean(true));
				items[i].setStatus(nc.vo.pub.VOStatus.UPDATED);
			}

			// 发运单 行出库关闭 时释放掉所占可用量，对下游已生成的出库单没有影响
			ArrayList alRet = DelivbillHBO_Client.closeOrOpenRowOut(savingVO,
					new ClientLink(getClientEnvironment()));

			if (alRet != null && alRet.size() > 0) {
				String sHeadTS = (String) alRet.get(alRet.size() - 1);
				m_currentbill.getParentVO().setAttributeValue("ts", sHeadTS);
				m_bills[m_listRow] = m_currentbill;
				m_headers[m_listRow] = (DelivbillHHeaderVO) m_currentbill
						.getParentVO();
			}
			getBillCardPanel().setBillValueVO(m_currentbill);
			getBillCardPanel().getBillModel().execLoadFormula();
			getBillCardPanel().updateValue();
			switchButtonStatusWithBill();
			m_isOperationFinish = true;
		} catch (Exception e) {
			showErrorMessage(e.getMessage());

			// 恢复vo状态
			m_currentbill = bakCurrVO;
			m_bills[m_listRow] = m_currentbill;

			handleException(e);
		}
	}

	// 行出库打开
	public void onRowOpenOut() {
		m_isOperationFinish = false;

		int[] iSelectedRows = getBillCardPanel().getBillTable()
				.getSelectedRows();
		if (iSelectedRows == null || iSelectedRows.length == 0) {
			showOkCancelMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON",
					"UPPSCMCommon-000203") /* @res "请选择一行" */);
			return;
		}

		DelivbillHHeaderVO header = (DelivbillHHeaderVO) m_currentbill
				.getParentVO();
		DelivbillHItemVO[] items = (DelivbillHItemVO[]) m_currentbill
				.getChildrenVO();

		// 只将需要进行 行出库打开 操作的行传到后台
		// 同时，进行是否已经 行出库打开 进行检查
		Vector v = new Vector();
		for (int i = 0; i < iSelectedRows.length; i++) {
			if (items[iSelectedRows[i]].getBcloseout() != null
					&& !items[iSelectedRows[i]].getBcloseout().booleanValue()) {
				String[] value = new String[] { String
						.valueOf(iSelectedRows[i] + 1) };
				String s = NCLangRes.getInstance().getStrByID("40140408",
						"UPP40140408-000190", null, value) /*
															 * @res
															 * "第 {0} 行已经行出库打开，请重新选择！"
															 */;
				dispWarningMessage(s);
				return;
			}
			v.add(items[iSelectedRows[i]]);
		}
		items = new DelivbillHItemVO[v.size()];
		v.toArray(items);

		// 进行 行出库打开 操作前，保存前台发运单数据表体状态、部分字段值，以备后台操作失败予以恢复
		DelivbillHVO bakCurrVO = (DelivbillHVO) m_currentbill.clone();

		DelivbillHVO savingVO = new DelivbillHVO();
		savingVO.setParentVO(header);
		savingVO.setChildrenVO(items);

		try {
			// 准备数据
			header.setStatus(nc.vo.pub.VOStatus.UPDATED);
			for (int i = 0; i < items.length; i++) {
				items[i].setBcloseout(new UFBoolean(false));
				items[i].setStatus(nc.vo.pub.VOStatus.UPDATED);
			}
			ArrayList alRet = null;

			// try {
			// 发运单 行出库打开 时占可用量，对下游已生成的出库单没有影响
			alRet = DelivbillHBO_Client.closeOrOpenRowOut(savingVO,
					new ClientLink(getClientEnvironment()));
			// }
			// catch (ATPNotEnoughException ane) {
			// if (ane.getHint() == null) {
			// throw ane;
			// }
			//
			// String[] value = new String[] { ane.getMessage() };
			// String sChooseMessage =
			// NCLangRes.getInstance().getStrByID("40140408",
			// "UPP40140408-000192", null,
			// value)/*@res "{0},是否继续保存发运单？"*/;
			//
			// int iFlag = showYesNoMessage(sChooseMessage);
			// if (iFlag == nc.ui.pub.beans.MessageDialog.ID_YES) {
			// alRet = DelivbillHBO_Client.closeOrOpenRowOut(savingVO, new
			// ClientLink(getClientEnvironment()));
			// }
			// else
			// throw ane;
			// }
			//

			if (alRet != null && alRet.size() > 0) {
				String sHeadTS = (String) alRet.get(alRet.size() - 1);
				m_currentbill.getParentVO().setAttributeValue("ts", sHeadTS);
				m_bills[m_listRow] = m_currentbill;
				m_headers[m_listRow] = (DelivbillHHeaderVO) m_currentbill
						.getParentVO();
			}
			getBillCardPanel().setBillValueVO(m_currentbill);
			getBillCardPanel().getBillModel().execLoadFormula();
			getBillCardPanel().updateValue();
			switchButtonStatusWithBill();
			m_isOperationFinish = true;
		} catch (Exception e) {
			showErrorMessage(e.getMessage());

			// 恢复vo状态
			m_currentbill = bakCurrVO;
			m_bills[m_listRow] = m_currentbill;

			handleException(e);
		}
	}

	/**
	 * 计算运费。 创建日期：(2002-6-17 15:03:53)
	 */
	public void onCalculateFee() {
		try {
			m_currentbill.getParentVO()
					.setAttributeValue("userid", getUserID());

			m_currentbill.setGenOIDPK(getCorpID());
			for (int i = 0; i < m_currentbill.getChildrenVO().length; i++) {
				m_currentbill.getChildrenVO()[i]
						.setStatus(nc.vo.pub.VOStatus.UNCHANGED);
			}

			DMVO dmvo = new DMVO();
			dmvo.translateFromOtherVO(m_currentbill);

			// 中脉test
			// for (int i = 0; i < m_currentbill.getChildrenVO().length; i++) {
			// if(i == 0) {
			// m_currentbill.getChildrenVO()[i].setAttributeValue("pk_packsort","1004AA1000000000290W");
			// m_currentbill.getChildrenVO()[i].setAttributeValue("dpacknum",new
			// UFDouble("10"));
			// m_currentbill.getChildrenVO()[i].setAttributeValue("dpackweight",new
			// UFDouble("20"));
			// m_currentbill.getChildrenVO()[i].setAttributeValue("dpackvolumn",new
			// UFDouble("10"));
			// }else {
			// m_currentbill.getChildrenVO()[i].setAttributeValue("pk_packsort","1004AA1000000000290X");
			// m_currentbill.getChildrenVO()[i].setAttributeValue("dpacknum",new
			// UFDouble("10"));
			// m_currentbill.getChildrenVO()[i].setAttributeValue("dpackweight",new
			// UFDouble("20"));
			// m_currentbill.getChildrenVO()[i].setAttributeValue("dpackvolumn",new
			// UFDouble("30"));
			//
			// }
			// }

			setCaculateTransFeeDlg(null); // 临时加上，调试用
			getCaculateTransFeeDlg().setDelivVO(dmvo);
			getCaculateTransFeeDlg().uIRefPaneTranCust_ValueChanged();
			getCaculateTransFeeDlg().uIRefPaneForFYFS_ValueChanged();
			getCaculateTransFeeDlg().setIsDlgSaveDelivFee(true);
			if (m_bIsCaculateShow) {
				getCaculateTransFeeDlg().showModal();

				// 根据用户点击“确定”或者“取消”做相应操作
			}
			if (getCaculateTransFeeDlg().getResult() == nc.ui.pub.beans.UIDialog.ID_OK) {
				// 修改界面的发运方式
				m_currentbill.setParentVO(getCaculateTransFeeDlg()
						.getDelivBillVO().getParentVO());
				m_currentbill.getParentVO().setStatus(
						nc.vo.pub.VOStatus.UNCHANGED);
				getBillCardPanel().setBillValueVO(m_currentbill);
				getBillCardPanel().getBillModel().execLoadFormula();
				getBillCardPanel().updateValue();
				updateUI();
				this.showHintMessage(NCLangRes.getInstance().getStrByID(
						"common", "MT9") /* 处理成功 */);
			}
		} catch (Exception e) {
			this.showErrorMessage(e.getMessage());
			handleException(e);
		}
	}

	/**
	 * 放弃输入。 创建日期：(2001-4-21 10:36:57)
	 */
	public void onCancel() {
		m_isOperationFinish = false;

		super.onCancel();

		if (!m_biscardadd || getInsertExist()) { // 切换到列表界面
			onSwith();
		} else {
			getBillCardPanel().getBillModel().execLoadFormula();
		}

		getBillCardPanel().setEnabled(false);

		switchButtonStatus(BeforeConfirm);
		m_isOperationFinish = true;

		// getBillListPanel().getHeadBillModel().clearBodyData();
		getBillListPanel().getBodyBillModel().clearBodyData();

		getBillListPanel().updateUI();

		// 设置插入标志
		setInsertExist(false);
		this.hasChangedPack = false;

		this.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
				"common", "UCH008")/* @res取消成功 */);

	}

	/**
	 * 弃审。 创建日期：(2002-6-20 9:35:05)
	 */
	public void onCancelAudit() {

		String pkapprperson = null;
		String vapprpersonname = null;
		UFDate apprdate = null;
		String auditTime = null;
		try {
			m_isOperationFinish = false;
			if (getEditFlag() == DMBillStatus.CardNew
					|| getEditFlag() == DMBillStatus.CardEdit) {
				int rst = MessageDialog
						.showYesNoDlg(this, null, NCLangRes.getInstance()
								.getStrByID("common", "UCH068") /* 是否确定要弃审？ */,
								MessageDialog.ID_NO);
				if (rst == MessageDialog.ID_NO) {
					return;
				}
			}
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"common", "UCH049")/* @res "正在弃审" */);
			DelivbillHHeaderVO header = (DelivbillHHeaderVO) m_currentbill
					.getParentVO();
			DelivbillHItemVO[] items = (DelivbillHItemVO[]) m_currentbill
					.getChildrenVO();

			// 设置表体状态
			for (int i = 0; i < items.length; i++) {
				UFBoolean bTested = items[i].getBtestbyinvoice();
				if (bTested != null && bTested.booleanValue()) {
					throw new BusinessException(NCLangRes.getInstance()
							.getStrByID("40140408", "UPP40140408-000035")
					/* @res "发运单已核销！" */);
				}
				// 只能对发运单整单进行弃审，如果存在至少一条发运单行为出库关闭状态，
				// 则提示错误：存在出库关闭状态的发运单行，不能弃审，请将出库状态打开后再弃审。
				if (StringTools.getSimilarBoolean(items[i].getBcloseout())) {
					throw new BusinessException(NCLangRes.getInstance()
							.getStrByID("40140408", "UPP40140408-000187")
					/* @res "存在出库关闭状态的发运单行，不能弃审，请将出库状态打开后再弃审。" */);
				}
				items[i].setIrowstatus(new Integer(DelivBillStatus.Free));
				items[i].setStatus(nc.vo.pub.VOStatus.UPDATED);
			}
			//
			pkapprperson = header.getPkapprperson();
			vapprpersonname = header.getVapprpersonname();
			apprdate = header.getApprdate();
			auditTime = header.getTaudittime();
			//
			// 设置表头状态
			header.setStatus(nc.vo.pub.VOStatus.UPDATED);
			header.setPkapprperson(null);
			header.setVapprpersonname(null);
			header.setApprdate(null);
			header.setTaudittime(null);
			m_currentbill.setGenOIDPK(getCorpID());
			header.setAttributeValue("userid", getUserID());
			// modified by czp on 2002-10-06 begin
			java.util.ArrayList ary = DelivbillHBO_Client.cancelAudit(
					m_currentbill, new ClientLink(getClientEnvironment()));
			if (null != ary && ary.size() > 0) {
				if (m_currentbill.getParentVO() != null) {
					m_currentbill.getParentVO().setAttributeValue("ts",
							ary.get(ary.size() - 1));
				}
			}
			// modified by czp on 2002-10-06 end
			getBillCardPanel().setBillValueVO(m_currentbill);
			getBillCardPanel().getBillModel().execLoadFormula();
			getBillCardPanel().updateValue();
			if (m_bills != null) {
				for (int i = 0; i < m_bills.length; i++) {
					if (m_bills[i].getParentVO().getPrimaryKey().equals(
							m_currentbill.getParentVO().getPrimaryKey())) {
						m_bills[i] = null;
						m_bills[i] = m_currentbill;
						break;
					}
				}
			}
			switchButtonStatusWithBill();
			m_isOperationFinish = true;
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"common", "UCH011")/* @res "弃审成功！" */);
		} catch (Exception e) {
			handleException(e);
			DelivbillHHeaderVO header = (DelivbillHHeaderVO) m_currentbill
					.getParentVO();
			header.setPkapprperson(pkapprperson);
			header.setVapprpersonname(vapprpersonname);
			header.setApprdate(apprdate);
			header.setTaudittime(auditTime);
		}
	}

	/**
	 * 承运商取消确认。 功能： 参数： 返回： 例外： 日期：(2002-8-20 10:02:16) 修改日期，修改人，修改原因，注释标志：
	 */
	public void onTransCancelConfirm() {
		// 承运商确认得逆操作
		try {
			int i;
			m_isOperationFinish = false;
			DelivbillHHeaderVO header = (DelivbillHHeaderVO) m_currentbill
					.getParentVO();
			DelivbillHItemVO[] items = (DelivbillHItemVO[]) m_currentbill
					.getChildrenVO();
			// 设置表体状态
			for (i = 0; i < items.length; i++) {
				items[i].setIrowstatus(new Integer(DelivBillStatus.Free));
				items[i].setStatus(nc.vo.pub.VOStatus.UPDATED);
			}
			// 设置表头状态
			header.setBconfirm(new UFBoolean(false));
			header.setStatus(nc.vo.pub.VOStatus.UPDATED);

			m_currentbill.setGenOIDPK(getCorpID());
			header.setAttributeValue("userid", getUserID());
			// modified by czp on 2002-10-06 begin
			// DelivbillHBO_Client.update(m_currentbill);
			java.util.ArrayList ary = DelivbillHBO_Client.confirm(
					m_currentbill, null, true, new ClientLink(
							getClientEnvironment()));
			if (null != ary && ary.size() > 0) {
				if (m_currentbill.getParentVO() != null) {
					m_currentbill.getParentVO().setAttributeValue("ts",
							ary.get(ary.size() - 1));
				}
			}
			// modified by czp on 2002-10-06 end
			getBillCardPanel().setBillValueVO(m_currentbill);
			getBillCardPanel().getBillModel().execLoadFormula();
			getBillCardPanel().updateValue();
			switchButtonStatusWithBill();
			m_isOperationFinish = true;
		} catch (Exception e) {
			handleException(e);
		}
	}

	/**
	 * 删除。 创建日期：(2002-5-16 13:00:06)
	 */
	public void onDel() {
		try {
			m_isOperationFinish = false;
			super.onDel();
			// switchButtonStatus(0);
			int i = getBillListPanel().getHeadTable().getSelectedRow();
			if (getShowState().equals(DMBillStatus.List)) {
				if (i < 0 || i >= m_bills.length) {
					MessageDialog.showErrorDlg(this, null, NCLangRes
							.getInstance().getStrByID("common", "UCH004") /*
																		 * @res
																		 * "请选择要处理的数据行！"
																		 */);
					return;
				} else if (i > 0 || i < m_bills.length) {
					m_currentbill = m_bills[i];
				}
			}
			if (MessageDialog.showYesNoDlg(this, null, NCLangRes.getInstance()
					.getStrByID("common", "UCH002") /* @res "是否确认要删除" */,
					MessageDialog.ID_NO) == MessageDialog.ID_YES) {
				m_currentbill.setGenOIDPK(getCorpID());
				m_currentbill.getParentVO().setAttributeValue("userid",
						getUserID());
				// 删除回写在后台
				m_currentbill.getParentVO().setStatus(
						nc.vo.pub.VOStatus.DELETED);
				DelivbillHBO_Client.delete(m_currentbill, new ClientLink(
						getClientEnvironment()));
				getBillCardPanel().updateValue();
				showHintMessage(NCLangRes.getInstance().getStrByID("common",
						"UCH006")/* 删除成功 */);
			} else {
				return;
			}
			if (getShowState().equals(DMBillStatus.Card)) {
				onSwith();
			}
			// 删除后刷新列表界面
			int headssize = m_headers.length;
			if (headssize > 0) {
				Vector headersVector = new Vector(headssize - 1);

				for (i = 0; i < headssize; i++) {
					if (m_headers[i].getPk_delivbill_h().equals(
							m_currentbill.getParentVO().getPrimaryKey()) == false) {
						headersVector.add(m_headers[i]);
					}
				}
				m_headers = new DelivbillHHeaderVO[headssize - 1];
				headersVector.copyInto(m_headers);
			} else {
				m_headers = new DelivbillHHeaderVO[0];
			}

			// 刷新缓存中的数据
			Vector vTemp = new Vector();
			for (i = 0; i < m_bills.length; i++) {
				DelivbillHHeaderVO headVO = (DelivbillHHeaderVO) m_bills[i]
						.getParentVO();
				if (!headVO.getPk_delivbill_h().equals(
						m_currentbill.getParentVO().getPrimaryKey())) {
					vTemp.addElement(m_bills[i]);
				}
			}
			m_bills = new DelivbillHVO[vTemp.size()];
			vTemp.copyInto(m_bills);
			//

			getBillListPanel().setHeaderValueVO(m_headers);
			getBillListPanel().getHeadTable().clearSelection();
			getBillListPanel().getBodyTable().clearSelection();
			getBillListPanel().setBodyValueVO(null);
			m_isOperationFinish = true;

			this.hasChangedPack = false;
		} catch (Exception e) {
			SCMEnv.error(e);
			showErrorMessage(e.getMessage());
			showHintMessage(NCLangRes.getInstance().getStrByID("common", "MD3",
					null, new String[] { "" })/* {0}删除失败 */);
		}
	}

	/**
	 * 修改。 创建日期：(2002-5-16 15:12:48)
	 */
	public void onEdit() {
		m_isOperationFinish = false;
		// 关闭单据号的编辑
		getBillCardPanel().getHeadItem("vdelivbillcode").setEdit(false);
		// 放开运费类别的控制
		getBillCardPanel().getHeadItem("isendtype").setEdit(true);
		getBillCardPanel().getHeadItem("isendtype").setEnabled(true);
		showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH027")/*
																			 * @res正在修改
																			 */);
		// 置入制单人

		if (getShowState().equals(DMBillStatus.List)) {
			m_biscardadd = false;
			onSwith();
		} else {
			m_biscardadd = true;
		}
		DelivbillHHeaderVO header = (DelivbillHHeaderVO) m_currentbill
				.getParentVO();
		header.setPkbillperson(getUserID());
		header.setVbillpersonname(getUserName());
		UFDate ufdate = new UFDate(System.currentTimeMillis());
		header.setBilldate(ufdate);
		if (getShowState().equals(DMBillStatus.Card)) {
			if (m_currentbill != null) {
				getBillCardPanel().setBillValueVO(m_currentbill);
				getBillCardPanel().updateValue();
				getBillCardPanel().getBillModel().execLoadFormula();
			}
			super.onEdit();
			switchButtonStatus(CardBillEditing);
			// setEditFlag(1);
			getBillCardPanel().setTailItem("pkbillperson", getUserID());
		}
		setInvItemEditable((DMBillCardPanel) getBillCardPanel());
		m_isOperationFinish = true;
	}

	/**
	 * 关闭。 创建日期：(2002-6-20 9:29:52)
	 */
	public void onEnd() {
		m_isOperationFinish = false;

		// 关闭前，保存前台发运单数据表体状态、部分字段值，以备后台操作失败予以恢复
		DelivbillHVO bakCurrVO = (DelivbillHVO) m_currentbill.clone();
		DelivbillHHeaderVO header = (DelivbillHHeaderVO) m_currentbill
				.getParentVO();
		DelivbillHItemVO[] items = (DelivbillHItemVO[]) m_currentbill
				.getChildrenVO();

		try {
			// 根据基础档案判断该发运方式是否必须签收再根据已签收数量是否零或空判断是否已签收
			// 提示用户该发运单未签收
			// 已挪置后台处理

			// 准备数据
			header.setStatus(nc.vo.pub.VOStatus.UPDATED);
			header.setAttributeValue("userid", getUserID());
			m_currentbill.setGenOIDPK(getCorpID());
			header.setBisopen(new UFBoolean(false));
			for (int i = 0; i < items.length; i++) {
				items[i].setIrowstatus(new Integer(DelivBillStatus.End));

				/*
				 * 是否已出库关闭标志备份值--在发运单整单关闭时使用到： 发运单行出库状态已经关闭的行不会再影响可用量
				 */
				items[i].setBcloseout_old(items[i].getBcloseout()); //

				if (!StringTools.getSimilarBoolean(items[i].getBcloseout())) {
					// 整单关闭已包含了发运单所有行的出库关闭动作
					items[i].setBcloseout(new UFBoolean(true));
				}

				items[i].setStatus(nc.vo.pub.VOStatus.UPDATED);
			}

			// 回写日计划的VO
			// 由修改前的单据和修改后的单据得到回写日计划的单据
			// 当发运参数
			// DM018(发运单手工关闭时按执行数回写数量)为"Y",并且发运出库串行时,手工关闭或者打开发运单,需要作回写数量的特殊处理
			// @see DelivbillHBO#update(DelivbillHVO delivbill, DMDataVO[]
			// writeBackItems, boolean isModifyATP,
			// boolean isEnd, boolean isOpen, ClientLink clientLink)
			DMDataVO[] writeBackItems = DelivBillVOTool.getWriteBackBill(
					m_currentbill, header, (DelivbillHItemVO[]) m_currentbill
							.getChildrenVO(), new ClientLink(
							getClientEnvironment())); // 由修改前的单据和修改后的单据得到回写日计划的单据

			// 对于此前已经 行出库关闭 的行，不能再有 释放可用量 的操作
			ArrayList alRet = DelivbillHBO_Client.update(m_currentbill,
					writeBackItems, true,
					new ClientLink(getClientEnvironment()));

			// 将该字段的值复原。否则，当打开发运单，再进行 行出库关闭 操作的时候，
			// 就会出现将该行过滤掉了的错误，也就是该行不能释放掉 待发货单 量的错误
			for (int i = 0; i < items.length; i++) {
				items[i].setBcloseout_old(null);
			}

			if (alRet != null && alRet.size() > 0) {
				String sHeadTS = (String) alRet.get(alRet.size() - 1);
				m_currentbill.getParentVO().setAttributeValue("ts", sHeadTS);
				m_bills[m_listRow] = m_currentbill;
				m_headers[m_listRow] = (DelivbillHHeaderVO) m_currentbill
						.getParentVO();
			}
			getBillCardPanel().setBillValueVO(m_currentbill);
			getBillCardPanel().getBillModel().execLoadFormula();
			getBillCardPanel().updateValue();
			switchButtonStatusWithBill();
			m_isOperationFinish = true;
		} catch (Exception e) {
			// 恢复vo状态
			m_currentbill = bakCurrVO;
			m_bills[m_listRow] = m_currentbill;
			handleException(e);
		}
	}

	/**
	 * 切换按钮。 创建日期：(2002-5-17 20:26:04)
	 */
	public void onFirst() {
		try {
			m_isOperationFinish = false;
			if (getEditFlag() == 2) {
				onFirstBatch();
				return;
			}
			if (m_listRow <= 0) {
				dispWarningMessage(NCLangRes.getInstance().getStrByID(
						"40140408", "UPP40140408-000074") /* @res "已经到达第一行" */);
				return;
			}

			m_listRow = 0;
			if (m_listRow >= 0 && m_listRow < m_headers.length) {
				m_currentbill = m_bills[m_listRow];
				m_currentbill.getParentVO().setAttributeValue("vdoname",
						getDelivOrgName());

				if (m_currentbill != null) {

					DelivbillHItemVO bodyVO[] = (DelivbillHItemVO[]) m_currentbill
							.getChildrenVO();
					if (bodyVO != null && bodyVO.length != 0
							&& bodyVO[0] != null
							&& bodyVO[0].getVinvcode() == null) {
						execFormulaBodys(bodyVO);
						// 存货自由项改为公式带出
						nc.ui.scm.pub.FreeVOParse freeVOParse = new nc.ui.scm.pub.FreeVOParse();
						freeVOParse.setFreeVO(bodyVO, null, "pkinv", false);
					}
					getBillCardPanel().setBillValueVO(m_currentbill);
					if (bodyVO != null && bodyVO.length > 0) {

						String s[] = new String[] {
								"creceiptcorp->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,creceiptcorpid)",
								"creceiptcorp->getColValue(bd_cubasdoc,custname,pk_cubasdoc,creceiptcorp)" };
						getBillCardPanel().getBillModel().execFormulas(s);
						for (int j = 0; j < bodyVO.length; j++) {
							if (bodyVO[j] != null) {
								bodyVO[j].setAttributeValue("creceiptcorp",
										getBillCardPanel().getBodyValueAt(j,
												"creceiptcorp"));
							}
						}
						getBillCardPanel().getBillModel().execLoadFormula();
					}
					DelivbillHHeaderVO headVO = (DelivbillHHeaderVO) m_currentbill
							.getParentVO();
					if (headVO != null) {
						updateRouteDesc();
						// UIRefPane refPane = (UIRefPane)
						// getBillCardPanel().getHeadItem("pkdelivroute").getComponent();
						// refPane.setText(headVO.getVroutename());
					}
				}

				switchButtonStatusWithBill();
				getBillCardPanel().updateValue();
				getBillCardPanel().updateUI();
				getBillCardPanel().setEnabled(false);
			}
			m_isOperationFinish = true;
		} catch (Exception e) {
			this.showErrorMessage(e.getMessage());
			handleException(e);
		}
	}

	/**
	 * 切换按钮。 创建日期：(2002-5-17 20:26:04)
	 */
	public void onFirstBatch() {
		try {
			m_isOperationFinish = false;

			if (m_listRow <= 0) {
				dispWarningMessage(NCLangRes.getInstance().getStrByID(
						"40140408", "UPP40140408-000074") /* @res "已经到达第一行" */);
				return;
			}
			// 记录当前
			m_batchbills[m_listRow] = (DelivbillHVO) getBillCardPanel()
					.getBillValueVO(DelivbillHVO.class.getName(),
							DelivbillHHeaderVO.class.getName(),
							DelivbillHItemVO.class.getName());

			m_listRow = 0;
			if (m_listRow < m_batchbills.length) {
				m_currentbill = m_batchbills[m_listRow];
				m_currentbill.getParentVO().setAttributeValue("vdoname",
						getDelivOrgName());

				if (m_currentbill != null) {
					execFormulaBodys(m_currentbill.getChildrenVO());
					getBillCardPanel().setBillValueVO(m_currentbill);
				}
			}
			getBillCardPanel().getBillModel().execLoadFormula();
			reSetRowNum();
			m_isOperationFinish = true;
		} catch (Exception e) {
			this.showErrorMessage(e.getMessage());
			handleException(e);
		}
	}

	/**
	 * 生成任务单,目前支持单张发运单生成单张任务单。 创建日期：(2002-6-27 14:37:43)
	 */
	public void onGenTaskBill() {
		try {
			m_isOperationFinish = false;

			DelivbillHVO dvo = (DelivbillHVO) getBillCardPanel()
					.getBillValueVO(DelivbillHVO.class.getName(),
							DelivbillHHeaderVO.class.getName(),
							DelivbillHItemVO.class.getName());

			if (dvo == null) {
				showErrorMessage(NCLangRes.getInstance().getStrByID("40140408",
						"UPP40140408-000037") /* @res "没有选择发运单，请重试！" */);
				return;
			}
			DelivbillHHeaderVO headVO = (DelivbillHHeaderVO) dvo.getParentVO();
			if (headVO.getPktrancust() != null
					&& headVO.getPktrancust().trim().length() > 0) {
				nc.ui.pub.beans.MessageDialog.showErrorDlg(this, NCLangRes
						.getInstance().getStrByID("40140410",
								"UPT40140410-000043")
				/* @res "任务单" */
				, NCLangRes.getInstance().getStrByID("40140408",
						"UPP40140408-000038")
				/* @res "承运商存在，不可生成任务单！" */);
				return;
			}

			ClientUISeveralBills billdlg = new ClientUISeveralBills(this,
					NCLangRes.getInstance().getStrByID("40140408",
							"UPP40140408-000039")
			/* @res "发运单生成任务单" */);

			// 隐藏切换按钮zxj
			billdlg.hideSwitchButton();

			// 置入发运组织相关信息zxj,在billdlg中自动带出表头和表体信息
			billdlg.getChldClientUI().setDelivOrgPK(getDelivOrgPK());
			billdlg.getChldClientUI().setDelivOrgName(getDelivOrgName());
			billdlg.getChldClientUI().getBillCardPanel().setHeadItem(
					"pkdelivcode",
					dvo.getParentVO().getAttributeValue("vdelivbillcode"));
			//
			ArrayList al = new ArrayList();
			dvo.setGenOIDPK(getCorpID());
			dvo.getParentVO().setAttributeValue("userid", getUserID());
			al.add(dvo);
			billdlg.setVO(al, getBillType(), null, getCorpID(), getUserID(),
					getUserName());

			billdlg.showModal();

			//
			if (billdlg.isSave()) {
				// 如果生成任务单界面成功保存，则发运单写入已生成任务单状态。
				// 从后台查询该单据 ts 回来
				DelivbillHHeaderVO tmp = DelivbillHBO_Client
						.findHeaderFieldsValue(" pk_delivbill_h='"
								+ m_currentbill.getParentVO().getPrimaryKey()
								+ "' ");

				String ts = tmp.getAttributeValue("ts").toString();
				m_currentbill.getParentVO().setAttributeValue("ts", ts);
				getBillCardPanel().setHeadItem("ts", ts);

				DelivbillHHeaderVO header = (DelivbillHHeaderVO) m_currentbill
						.getParentVO();
				DelivbillHItemVO[] items = (DelivbillHItemVO[]) m_currentbill
						.getChildrenVO();
				header.setStatus(nc.vo.pub.VOStatus.UPDATED);
				for (int i = 0; i < items.length; i++) {
					items[i].setStatus(nc.vo.pub.VOStatus.UNCHANGED);
				}
				header.setBmissionbill(new UFBoolean(true));

				// 是否生成任务单
				getBillCardPanel().setHeadItem("bmissionbill",
						new UFBoolean(true));

				getBillCardPanel().updateValue();
			}
			switchButtonStatusWithBill();
			m_isOperationFinish = true;
		} catch (Exception e) {
			this.showErrorMessage(e.getMessage());
			handleException(e);
		}
	}

	/**
	 * 切换按钮。 创建日期：(2002-5-17 20:26:04)
	 */
	public void onLast() {
		try {
			m_isOperationFinish = false;

			if (getEditFlag() == 2) {
				onLastBatch();
				return;
			}
			if (m_listRow >= m_headers.length - 1) {
				dispWarningMessage(NCLangRes.getInstance().getStrByID(
						"40140408", "UPP40140408-000075") /* @res "已经到达最后一行" */);
				return;
			}

			m_listRow = m_headers.length - 1;
			if (m_listRow >= 0 && m_listRow < m_headers.length) {
				m_currentbill = m_bills[m_listRow];
				m_currentbill.getParentVO().setAttributeValue("vdoname",
						getDelivOrgName());

				if (m_currentbill != null) {

					DelivbillHItemVO bodyVO[] = (DelivbillHItemVO[]) m_currentbill
							.getChildrenVO();
					if (bodyVO != null && bodyVO.length != 0
							&& bodyVO[0] != null
							&& bodyVO[0].getVinvcode() == null) {
						execFormulaBodys(bodyVO);
						// 存货自由项改为公式带出
						nc.ui.scm.pub.FreeVOParse freeVOParse = new nc.ui.scm.pub.FreeVOParse();
						freeVOParse.setFreeVO(bodyVO, null, "pkinv", false);
					}
					getBillCardPanel().setBillValueVO(m_currentbill);
					if (bodyVO != null && bodyVO.length > 0) {

						String s[] = new String[] {
								"creceiptcorp->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,creceiptcorpid)",
								"creceiptcorp->getColValue(bd_cubasdoc,custname,pk_cubasdoc,creceiptcorp)" };
						getBillCardPanel().getBillModel().execFormulas(s);
						for (int j = 0; j < bodyVO.length; j++) {
							if (bodyVO[j] != null) {
								bodyVO[j].setAttributeValue("creceiptcorp",
										getBillCardPanel().getBodyValueAt(j,
												"creceiptcorp"));
							}
						}
						getBillCardPanel().getBillModel().execLoadFormula();
					}
					DelivbillHHeaderVO headVO = (DelivbillHHeaderVO) m_currentbill
							.getParentVO();
					if (headVO != null) {
						updateRouteDesc();
						// UIRefPane refPane = (UIRefPane)
						// getBillCardPanel().getHeadItem("pkdelivroute").getComponent();
						// refPane.setText(headVO.getVroutename());
					}
				}

				switchButtonStatusWithBill();
				getBillCardPanel().updateValue();
				getBillCardPanel().updateUI();
				getBillCardPanel().setEnabled(false);
			}
			showHintMessage(NCLangRes.getInstance().getStrByID("40140408",
					"UPP40140408-000182") /* @res "数据加载成功！" */);
			m_isOperationFinish = true;
		} catch (Exception e) {
			showErrorMessage(e.getMessage());
			handleException(e);
		}
	}

	/**
	 * 切换按钮。 创建日期：(2002-5-17 20:26:04)
	 */
	public void onLastBatch() {
		try {
			if (m_listRow == m_batchbills.length - 1) {
				dispWarningMessage(NCLangRes.getInstance().getStrByID(
						"40140408", "UPP40140408-000075") /* @res "已经到达最后一行" */);
				return;
			}
			// 记录当前
			m_batchbills[m_listRow] = (DelivbillHVO) getBillCardPanel()
					.getBillValueVO(DelivbillHVO.class.getName(),
							DelivbillHHeaderVO.class.getName(),
							DelivbillHItemVO.class.getName());

			m_listRow = m_batchbills.length - 1;
			if (m_listRow >= 0 && m_listRow < m_batchbills.length) {
				m_currentbill = m_batchbills[m_listRow];
				m_currentbill.getParentVO().setAttributeValue("vdoname",
						getDelivOrgName());

				if (m_currentbill != null) {
					execFormulaBodys(m_currentbill.getChildrenVO());
					getBillCardPanel().setBillValueVO(m_currentbill);
				}
			}
			getBillCardPanel().getBillModel().execLoadFormula();
			reSetRowNum();
			showHintMessage(NCLangRes.getInstance().getStrByID("40140408",
					"UPP40140408-000182") /* @res "数据加载成功！" */);
			m_isOperationFinish = true;
		} catch (Exception e) {
			showErrorMessage(e.getMessage());
			handleException(e);
		}
	}

	/**
	 * 切换按钮。 创建日期：(2002-5-17 20:26:04)
	 */
	public void onNext() {
		try {
			m_isOperationFinish = false;
			if (getEditFlag() == 2) {
				onNextBatch();
				return;
			}
			if (m_listRow >= m_headers.length - 1) {
				dispWarningMessage(NCLangRes.getInstance().getStrByID(
						"40140408", "UPP40140408-000075") /* @res "已经到达最后一行" */);
				return;
			}
			m_listRow = m_listRow + 1;
			if (m_listRow >= 0 && m_listRow < m_headers.length) {
				m_currentbill = m_bills[m_listRow];
				m_currentbill.getParentVO().setAttributeValue("vdoname",
						getDelivOrgName());

				if (m_currentbill != null) {

					DelivbillHItemVO bodyVO[] = (DelivbillHItemVO[]) m_currentbill
							.getChildrenVO();
					if (bodyVO != null && bodyVO.length != 0
							&& bodyVO[0] != null
							&& bodyVO[0].getVinvcode() == null) {
						execFormulaBodys(bodyVO);
						// 存货自由项改为公式带出
						nc.ui.scm.pub.FreeVOParse freeVOParse = new nc.ui.scm.pub.FreeVOParse();
						freeVOParse.setFreeVO(bodyVO, null, "pkinv", false);
					}
					getBillCardPanel().setBillValueVO(m_currentbill);
					if (bodyVO != null && bodyVO.length > 0) {

						String s[] = new String[] {
								"creceiptcorp->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,creceiptcorpid)",
								"creceiptcorp->getColValue(bd_cubasdoc,custname,pk_cubasdoc,creceiptcorp)" };
						getBillCardPanel().getBillModel().execFormulas(s);
						for (int j = 0; j < bodyVO.length; j++) {
							if (bodyVO[j] != null) {
								bodyVO[j].setAttributeValue("creceiptcorp",
										getBillCardPanel().getBodyValueAt(j,
												"creceiptcorp"));
							}
						}
						getBillCardPanel().getBillModel().execLoadFormula();
					}
					DelivbillHHeaderVO headVO = (DelivbillHHeaderVO) m_currentbill
							.getParentVO();
					if (headVO != null) {
						updateRouteDesc();
						// UIRefPane refPane = (UIRefPane)
						// getBillCardPanel().getHeadItem("pkdelivroute").getComponent();
						// refPane.setText(headVO.getVroutename());
					}
				}

				switchButtonStatusWithBill();
				getBillCardPanel().updateValue();
				getBillCardPanel().updateUI();
				getBillCardPanel().setEnabled(false);
			} else if (m_listRow >= m_headers.length) {
				m_listRow = m_listRow - 1;
			} else if (m_listRow < 0) {
			}
			showHintMessage(NCLangRes.getInstance().getStrByID("40140408",
					"UPP40140408-000182") /* @res "数据加载成功！" */);
			m_isOperationFinish = true;
		} catch (Exception e) {
			this.showErrorMessage(e.getMessage());
			handleException(e);
		}
	}

	/**
	 * 切换按钮。 创建日期：(2002-5-17 20:26:04)
	 */
	public void onNextBatch() {
		try {
			// m_listRow =0;
			if (m_listRow >= m_batchbills.length - 1) {
				dispWarningMessage(NCLangRes.getInstance().getStrByID(
						"40140408", "UPP40140408-000075") /* @res "已经到达最后一行" */);
				return;
			}
			// 记录当前
			m_batchbills[m_listRow] = (DelivbillHVO) getBillCardPanel()
					.getBillValueVO(DelivbillHVO.class.getName(),
							DelivbillHHeaderVO.class.getName(),
							DelivbillHItemVO.class.getName());

			m_listRow += 1;
			if (m_listRow >= 0 && m_listRow < m_batchbills.length) {
				m_batchbills[m_listRow].getParentVO().setAttributeValue(
						"vdoname", getDelivOrgName());

				if (m_currentbill != null) {
					execFormulaBodys(m_batchbills[m_listRow].getChildrenVO());
					getBillCardPanel().setBillValueVO(m_batchbills[m_listRow]);
				}
			} else if (m_listRow >= m_batchbills.length) {
				m_listRow = m_listRow - 1;
			} else if (m_listRow < 0) {
			}
			getBillCardPanel().getBillModel().execLoadFormula();
			reSetRowNum();
			showHintMessage(NCLangRes.getInstance().getStrByID("40140408",
					"UPP40140408-000182") /* @res "数据加载成功！" */);
			m_isOperationFinish = true;
		} catch (Exception e) {
			showErrorMessage(e.getMessage());
			handleException(e);
		}
	}

	/**
	 * 打开。 创建日期：(2002-6-20 9:29:52)
	 */
	public void onOpen() {
		// 时间：2014-8-18 原因：对发运单的单开按钮做权限
		// 获取当前登录的用户的
		String code = getClientEnvironment().getUser().getUserCode();
		String name = getClientEnvironment().getUser().getUserName();
		String key = getClientEnvironment().getUser().getPrimaryKey();
		// 获取当前登录与用户的角色
		StringBuffer hql = new StringBuffer();
		hql.append(" select sm_role.pk_role ") 
		.append("   from sm_role sm_role ") 
		.append("  inner join sm_user_role sm_user_managerole on sm_role.pk_role = ") 
		.append("                                                sm_user_managerole.pk_role ") 
		.append("  inner join sm_user sm_user on sm_user.cuserid = sm_user_managerole.cuserid ") 
		.append("                            and sm_user.cuserid = '"+key+"' ") 
		.append("  where sm_role.role_code = 'fyddkjs' "); 
		IUAPQueryBS qurey = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		try {
			List<String> nclist = (List<String>) qurey.executeQuery(hql.toString(),
					new BeanListProcessor(String.class));

			if (nclist == null||nclist.size() <1) {
				showErrorMessage(code+" "+name+ "：对不起，你没有该按钮的打开权限");
				return;
			}
		} catch (BusinessException e1) {

			e1.printStackTrace();
		}
		//end 李江涛
		DelivbillHHeaderVO header = (DelivbillHHeaderVO) m_currentbill
				.getParentVO();
		DelivbillHItemVO[] items = (DelivbillHItemVO[]) m_currentbill
				.getChildrenVO();
		DelivbillHItemVO[] olditems = new DelivbillHItemVO[items.length]; // skg
		try {
			int i;
			m_isOperationFinish = false;

			// 检查是否所有的表体行都为关闭状态，
			// 如果不是所有的表体行都为关闭状态，则不能执行此操作
			for (i = 0; i < items.length; i++) {
				if (items[i].getIrowstatus().intValue() != DelivBillStatus.End) {
					showErrorMessage(NCLangRes.getInstance().getStrByID(
							"40140408", "UPP40140408-000077") /*
															 * @res
															 * "只有所有的表体行都为关闭状态，才能执行此操作！"
															 */);
					return;
				}
			}

			header.setStatus(nc.vo.pub.VOStatus.UPDATED);
			header.setBisopen(new nc.vo.pub.lang.UFBoolean(true));
			for (i = 0; i < items.length; i++) {
				olditems[i] = new DelivbillHItemVO(); // skg
				olditems[i].setIrowstatus(items[i].getIrowstatus()); // skg
				olditems[i].setBcloseout(items[i].getBcloseout()); //

				// 打开所有发运单行
				items[i].setBcloseout(new UFBoolean(false));

				items[i].setIrowstatus(new Integer(DelivBillStatus.Audit));
				items[i].setStatus(nc.vo.pub.VOStatus.UPDATED);
			}
			m_currentbill.setGenOIDPK(getCorpID());
			header.setAttributeValue("userid", getUserID());

			// 回写日计划的VO
			// 由修改前的单据和修改后的单据得到回写日计划的单据
			// 当发运参数
			// DM018(发运单手工关闭时按执行数回写数量)为"Y",并且发运出库串行时,手工关闭或者打开发运单,需要作回写数量的特殊处理
			// @see DelivbillHBO#update(DelivbillHVO delivbill, DMDataVO[]
			// writeBackItems, boolean isModifyATP,
			// boolean isEnd, boolean isOpen, ClientLink clientLink)
			DMDataVO[] writeBackItems = DelivBillVOTool.getWriteBackBill(
					m_currentbill, header, (DelivbillHItemVO[]) m_currentbill
							.getChildrenVO(), new ClientLink(
							getClientEnvironment())); // 由修改前的单据和修改后的单据得到回写日计划的单据

			ArrayList alRet = null;
			// try {
			alRet = DelivbillHBO_Client.update(m_currentbill, writeBackItems,
					true, new ClientLink(getClientEnvironment()));
			// }
			// catch (ATPNotEnoughException ane) {
			// if (ane.getHint() == null) {
			// throw ane;
			// }
			//
			//
			// String[] value = new String[] { ane.getMessage() };
			// String sChooseMessage =
			// NCLangRes.getInstance().getStrByID("40140408",
			// "UPP40140408-000192", null,
			// value)/*@res "{0},是否继续保存发运单？"*/;
			//
			// int iFlag = showYesNoMessage(sChooseMessage);
			// if (iFlag == nc.ui.pub.beans.MessageDialog.ID_YES) {
			// alRet = DelivbillHBO_Client.update(m_currentbill, writeBackItems,
			// true,
			// new ClientLink(getClientEnvironment()));
			// }
			// else
			// throw ane;
			//
			// }

			if (alRet != null && alRet.size() > 0) {
				String sHeadTS = (String) alRet.get(alRet.size() - 1);
				m_currentbill.getParentVO().setAttributeValue("ts", sHeadTS);
				((DelivbillHHeaderVO) m_currentbill.getParentVO())
						.setBisopen(new UFBoolean(false));
				m_bills[m_listRow] = m_currentbill;
				m_headers[m_listRow] = (DelivbillHHeaderVO) m_currentbill
						.getParentVO();
			}

			getBillCardPanel().setBillValueVO(m_currentbill);
			getBillCardPanel().getBillModel().execLoadFormula();
			getBillCardPanel().updateValue();
			switchButtonStatusWithBill();
			m_isOperationFinish = true;
		} catch (Exception e) {
			this.showErrorMessage(e.getMessage());
			// skg begin: 恢复vo状态
			((DelivbillHHeaderVO) m_currentbill.getParentVO())
					.setBisopen(new UFBoolean(false));
			for (int i = 0; i < items.length; i++) {
				items[i].setIrowstatus(olditems[i].getIrowstatus());
				items[i].setBcloseout(olditems[i].getBcloseout()); //
				// items[i].setStatus(nc.vo.pub.VOStatus.UNCHANGED);
			}
			// skg end
			handleException(e);
		}
	}

	/**
	 * 生成出库单。 功能： 参数： 返回： 例外： 日期：(2002-8-20 10:03:01) 修改日期，修改人，修改原因，注释标志：
	 */
	public void onOutBill() {
		try {
			showHintMessage(NCLangRes.getInstance().getStrByID("40140408",
					"UPP40140408-000049") /* @res "开始出库！" */);
			// 发运单生成销售出库单、其他出库单的入口条件：
			// （1） 发运单审核
			// （2） 发运单表体行有未出库数量，此处不再做超额参数的检查。
			// (3)来源为采购单据的发运单不允许出库
			// 按钮状态控制已实现

			DelivbillHHeaderVO header = (DelivbillHHeaderVO) m_currentbill
					.getParentVO();
			DelivbillHItemVO[] items = (DelivbillHItemVO[]) m_currentbill
					.getChildrenVO();

			int i;

			if (items != null && items.length > 0
					&& items[0].getAttributeValue("vbilltype").equals("21")) {
				nc.ui.pub.beans.MessageDialog.showErrorDlg(this, NCLangRes
						.getInstance().getStrByID("40140408",
								"UPT40140408-000052")
				/* @res "出库" */
				, NCLangRes.getInstance().getStrByID("40140408",
						"UPP40140408-000050")
				/* @res "来源为采购单据的发运单不允许出库" */);
				return;
			}

			if (header != null && DM001 != null && DM001.booleanValue()) {
				nc.ui.pub.beans.UICheckBox box = (nc.ui.pub.beans.UICheckBox) getBillCardPanel()
						.getHeadItem("bconfirm").getComponent();
				if (!box.isSelected()) {
					nc.ui.pub.beans.MessageDialog.showErrorDlg(this, NCLangRes
							.getInstance().getStrByID("40140408",
									"UPT40140408-000052")
					/* @res "出库" */
					, NCLangRes.getInstance().getStrByID("40140408",
							"UPP40140408-000051")
					/* @res "承运商未确认！" */);
					return;
				}
			}

			if (items != null && items.length > 0) {
				boolean bOutBill = false; // 是否已出库
				// boolean bSigned = false; //是否已签收
				for (i = 0; i < items.length; i++) {
					items[i].setPk_corp(getCorpID());
					// UFDouble dOutNum = items[i].getDoutnum();
					// String vSignName = items[i].getVsignname();
					/*
					 * if (dOutNum != null && dOutNum.toString().trim().length()
					 * > 0 && dOutNum.doubleValue() != 0) { bOutBill = true; }
					 */
					if (items[i].getIrowstatus() != null
							&& items[i].getIrowstatus().intValue() == DelivBillStatus.Out) {
						bOutBill = true;
					}
					// if (vSignName != null && vSignName.trim().length() > 0) {
					// bSigned = true;
					// }
				}
				if (bOutBill) {
					if (nc.ui.pub.beans.MessageDialog.showYesNoDlg(this,
							NCLangRes.getInstance().getStrByID("40140408",
									"UPT40140408-000052")
							/* @res "出库" */
							, NCLangRes.getInstance().getStrByID("40140408",
									"UPP40140408-000052")
					/* @res "已下达出库指令，是否继续出库?" */) != nc.ui.pub.beans.MessageDialog.ID_YES) {
						return;
					}
					// if (bSigned) {
					// nc.ui.pub.beans.MessageDialog.showHintDlg(this,
					// NCLangRes.getInstance().getStrByID("40140408",
					// "UPT40140408-000052")/*@res "出库"*/,
					// NCLangRes.getInstance().getStrByID("40140408",
					// "UPP40140408-000053")/*@res "已签收，不可继续出库！"*/);
					// return;
					// }
				}
			}

			// 回写发运单和生成出库单放在一个事务中
			m_currentbill.setGenOIDPK(getCorpID());
			header.setAttributeValue("userid", getUserID());
			//

			// 发运单表体行有未出库数量，此处不再做超额参数的检查
			// 代码未实现

			// 组织一批对应发运单表体行的OutbillHVO[],在后台DMO实现
			Vector v = new Vector();
			for (i = 0; i < items.length; i++) {
				// 对发运单行进行出库关闭后，此发运单行不能推式生成新的出库单
				if (!StringTools.getSimilarBoolean(items[i].getBcloseout())) {
					v.add(items[i]);
				}
			}

			// 如果发运单所有行均为出库关闭，则点击出库按钮时报错
			if (v.size() == 0) {
				nc.ui.pub.beans.MessageDialog.showWarningDlg(this, NCLangRes
						.getInstance().getStrByID("40140408",
								"UPT40140408-000052")
				/* @res "出库" */
				, NCLangRes.getInstance().getStrByID("40140408",
						"UPP40140408-000188")
				/* @res "发运单所有行均为出库关闭,不能出库!" */);
				return;
			}

			// 查询当前单据的到货库存组织的所有仓库
			// 根据仓库、存货查询现存量，找出所有现存量不为零的仓库组织OutbillHItemVO
			OutbillHVO[] outbills = DelivbillHBO_Client.getOnhandnum(
					(DelivbillHItemVO[]) v.toArray(new DelivbillHItemVO[v
							.size()]), getUserID(), DM011);

			// 此时得到的OutbillHVO[]中的DelivbillHItemVO中的业务员、业务部门已经是对应日计划中的

			// 置入生成出库单界面
			getOutBillDlg().setOutbillHVOs(outbills);
			getOutBillDlg().setHvo(header);
			getOutBillDlg().setDelivbillHItemVO(items);
			getOutBillDlg().clearListRow();
			getOutBillDlg().getBillListPanel().getHeadTable().clearSelection();
			getOutBillDlg().getBillListPanel().getBodyBillModel()
					.clearBodyData();
			getOutBillDlg().getBillListPanel().setBodyValueVO(null);
			getOutBillDlg().showModal();

			// 根据用户点击“确定”或者“取消”做相应操作
			if (getOutBillDlg().getResult() == nc.ui.pub.beans.UIDialog.ID_OK) {

				// 设置界面当前发运单的表头时间戳
				if (m_currentbill.getParentVO() != null
						&& getOutBillDlg().m_outbillts != null) {
					m_currentbill.getParentVO().setAttributeValue("ts",
							getOutBillDlg().m_outbillts);
				}

				// 设置界面当前发运单的表体已出库状态
				for (i = 0; i < items.length; i++) {

					// 对发运单行进行出库关闭后，此发运单行不能推式生成新的出库单
					if (!StringTools.getSimilarBoolean(items[i].getBcloseout())) {
						items[i]
								.setIrowstatus(new Integer(DelivBillStatus.Out));
					}
				}

				getBillCardPanel().setBillValueVO(m_currentbill);
				getBillCardPanel().getBillModel().execLoadFormula();
				getBillCardPanel().updateValue();

				showHintMessage(NCLangRes.getInstance().getStrByID("40140408",
						"UPP40140408-000054") /* @res "出库成功！" */);
				switchButtonStatusWithBill();
			} else {
				// 用户点击“取消”或者关闭对话框
				showHintMessage(NCLangRes.getInstance().getStrByID("40140408",
						"UPP40140408-000055") /* @res "放弃出库！" */);
			}
		} catch (Exception e) {
			handleException(e);
		}
	}

	/**
	 * 包装明细。 创建日期：(2002-6-17 15:03:53)
	 */
	public void onPackageList() {
		try {
			// 先改发运单，后改包装分类，不重算包装分类。
			if (hasChangedPack) {
				hasChangedPack = false;
			}
			if (boSave.isEnabled()) { // 编辑状态
				if (getBillCardPanel().getRowCount() == 0) {
					showErrorMessage(NCLangRes.getInstance().getStrByID(
							"40140408", "UPP40140408-000078") /* @res "发运单表体不可为空！" */);
					return;
				}
				DMVO dmvo = new DMVO();
				DelivbillHVO editvo = (DelivbillHVO) getBillCardPanel()
						.getBillValueVO(DelivbillHVO.class.getName(),
								DelivbillHHeaderVO.class.getName(),
								DelivbillHItemVO.class.getName());
				if (getEditFlag() == 0) { // 新增
					((DelivbillHHeaderVO) (editvo.getParentVO()))
							.setPk_delivbill_h(null);
				}
				dmvo.translateFromOtherVO(editvo);
				if (m_dmdvosPackageVOs == null) {
					setPackageListDlg(null);
					getPackageListDlg().setDelivbillvo(editvo);
					getPackageListDlg().setDelivVO(dmvo);
					getPackageListDlg().setIsDlgSave(false); // 编辑状态不直接保存
				} else {
					getPackageListDlg().setNeedRecalculate(false);
				}
				if (getPackageListDlg().showModal() == nc.ui.pub.beans.MessageDialog.ID_OK) {
					m_dmdvosPackageVOs = getPackageListDlg().getPackageVOs();
					getBillCardPanel().setHeadItem(
							"dallpacknum",
							((DelivbillHHeaderVO) getPackageListDlg()
									.getDelivbillvo().getParentVO())
									.getDallpacknum());
					getBillCardPanel().setHeadItem(
							"dallweight",
							((DelivbillHHeaderVO) getPackageListDlg()
									.getDelivbillvo().getParentVO())
									.getDallweight());
					getBillCardPanel().setHeadItem(
							"dallvolumn",
							((DelivbillHHeaderVO) getPackageListDlg()
									.getDelivbillvo().getParentVO())
									.getDallvolumn());
				}
			} else {
				DMVO dmvo = new DMVO();
				dmvo.translateFromOtherVO(m_currentbill);

				m_currentbill.getParentVO().setAttributeValue("userid",
						getUserID());
				setPackageListDlg(null);
				getPackageListDlg().setDelivVO(dmvo);
				getPackageListDlg().setDelivbillvo(m_currentbill);
				getPackageListDlg().setIsDlgSave(true);
				// 审批后不许修改包装
				if (((DelivbillHHeaderVO) (m_currentbill.getParentVO()))
						.getPkapprperson() != null
						&& ((DelivbillHHeaderVO) m_currentbill.getParentVO())
								.getPkapprperson().trim().length() != 0) {// 审批状态
					getPackageListDlg().setSaveButtonEnable(false);
				}
				if (getPackageListDlg().showModal() == nc.ui.pub.beans.MessageDialog.ID_OK) {
					m_currentbill = getPackageListDlg().getDelivbillvo();
					updateCurrentBill(m_currentbill);
					setBillVOIntoUI();
					updateBills(m_currentbill);
				}

			}
		} catch (Exception e) {
			// ExceptionUITools tool = new ExceptionUITools();
			// tool.showMessage(e,this);
			handleException(e);
		}
	}

	/**
	 * 切换按钮。 创建日期：(2002-5-17 20:26:04)
	 */
	public void onPre() {
		try {
			m_isOperationFinish = false;
			if (getEditFlag() == 2) {
				onPreBatch();
				return;
			}

			if (m_listRow <= 0) {
				dispWarningMessage(NCLangRes.getInstance().getStrByID(
						"40140408", "UPP40140408-000074") /* @res "已经到达第一行" */);
				return;
			}
			m_listRow = m_listRow - 1;
			if (m_listRow >= 0 && m_listRow < m_headers.length) {
				m_currentbill = m_bills[m_listRow];
				m_currentbill.getParentVO().setAttributeValue("vdoname",
						getDelivOrgName());

				if (m_currentbill != null) {

					DelivbillHItemVO bodyVO[] = (DelivbillHItemVO[]) m_currentbill
							.getChildrenVO();
					if (bodyVO != null && bodyVO.length != 0
							&& bodyVO[0] != null
							&& bodyVO[0].getVinvcode() == null) {
						execFormulaBodys(bodyVO);
						// 存货自由项改为公式带出
						nc.ui.scm.pub.FreeVOParse freeVOParse = new nc.ui.scm.pub.FreeVOParse();
						freeVOParse.setFreeVO(bodyVO, null, "pkinv", false);
					}
					getBillCardPanel().setBillValueVO(m_currentbill);
					if (bodyVO != null && bodyVO.length > 0) {
						String s[] = new String[] {
								"creceiptcorp->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,creceiptcorpid)",
								"creceiptcorp->getColValue(bd_cubasdoc,custname,pk_cubasdoc,creceiptcorp)" };
						getBillCardPanel().getBillModel().execFormulas(s);
						for (int j = 0; j < bodyVO.length; j++) {
							if (bodyVO[j] != null) {
								bodyVO[j].setAttributeValue("creceiptcorp",
										getBillCardPanel().getBodyValueAt(j,
												"creceiptcorp"));
							}
						}
						getBillCardPanel().getBillModel().execLoadFormula();
					}
					DelivbillHHeaderVO headVO = (DelivbillHHeaderVO) m_currentbill
							.getParentVO();
					if (headVO != null) {
						updateRouteDesc();
						// UIRefPane refPane = (UIRefPane)
						// getBillCardPanel().getHeadItem("pkdelivroute").getComponent();
						// refPane.setText(headVO.getVroutename());
					}
				}

				switchButtonStatusWithBill();
				getBillCardPanel().updateValue();
				getBillCardPanel().updateUI();
				getBillCardPanel().setEnabled(false);
			} else if (m_listRow < 0) {
				m_listRow = m_listRow + 1;
			} else if (m_listRow >= m_headers.length) {
			}
			showHintMessage(NCLangRes.getInstance().getStrByID("40140408",
					"UPP40140408-000182") /* @res "数据加载成功！" */);
			m_isOperationFinish = true;
		} catch (Exception e) {
			showErrorMessage(e.getMessage());
			handleException(e);
		}
	}

	/**
	 * 切换按钮。 创建日期：(2002-5-17 20:26:04)
	 */
	public void onPreBatch() {
		try {
			if (m_listRow <= 0) {
				dispWarningMessage(NCLangRes.getInstance().getStrByID(
						"40140408", "UPP40140408-000074") /* @res "已经到达第一行" */);
				return;
			}
			// 记录当前
			m_batchbills[m_listRow] = (DelivbillHVO) getBillCardPanel()
					.getBillValueVO(DelivbillHVO.class.getName(),
							DelivbillHHeaderVO.class.getName(),
							DelivbillHItemVO.class.getName());

			m_listRow -= 1;
			if (m_listRow >= 0 && m_listRow < m_batchbills.length) {
				m_batchbills[m_listRow].getParentVO().setAttributeValue(
						"vdoname", getDelivOrgName());

				if (m_currentbill != null) {
					execFormulaBodys(m_batchbills[m_listRow].getChildrenVO());
					getBillCardPanel().setBillValueVO(m_batchbills[m_listRow]);
				}
			} else if (m_listRow < 0) {
				m_listRow = m_listRow + 1;
			} else if (m_listRow > m_batchbills.length) {
			}
			getBillCardPanel().getBillModel().execLoadFormula();
			reSetRowNum();
			showHintMessage(NCLangRes.getInstance().getStrByID("40140408",
					"UPP40140408-000182") /* @res "数据加载成功！" */);
			m_isOperationFinish = true;
		} catch (Exception e) {
			showErrorMessage(e.getMessage());
			handleException(e);
		}
	}

	/**
	 * 打印。 功能： 参数： 返回： 例外： 日期：(2002-7-4 14:42:22) 修改日期，修改人，修改原因，注释标志：
	 */
	public void onPrint() {
		try {
			if (m_strShowState.equals(DMBillStatus.Card)) {
				// onCardPrint(getBillCardPanel());

				ArrayList alBill = new ArrayList();
				alBill.add(m_currentbill);
				// 最远运输里程和单价
				DelivbillHVO[] printvos = new DelivbillHVO[1];
				printvos[0] = m_currentbill;
				ArrayList alRet = DelivbillHBO_Client
						.getMaxmileAndPriceForPrint(printvos);
				DMDataVO dmdMaxmileAndPrice = (DMDataVO) alRet.get(0);
				m_currentbill.getParentVO().setAttributeValue("dmaxmile",
						dmdMaxmileAndPrice.getAttributeValue("dmaxmile"));
				m_currentbill.getParentVO().setAttributeValue("dmaxmileprice",
						dmdMaxmileAndPrice.getAttributeValue("dmaxmileprice"));

				// BillPrintTool(String sModuleCode, ArrayList alBill, BillData
				// bd,
				// Class specialDataSource,String pk_corp, String sUserID,
				// String sNameofBillCodeItem, String sNameofBillIDItem)
				BillPrintTool bpt = new BillPrintTool(
						DMBillNodeCodeConst.m_delivDelivBill, alBill,
						getBillCardPanel().getBillData(), null, null, null,
						"vdelivbillcode", "pk_delivbill_h");

				// onCardPrint(BillCardPanel cardPanel, BillListPanel listPanel,
				// String
				// billtypecode)
				bpt.onCardPrint(getBillCardPanel(), getBillListPanel(),
						DMBillTypeConst.m_delivDelivBill);
			} else if (m_strShowState.equals(DMBillStatus.List)) {
				ArrayList alvo = getSelectedListCacheBills();
				if (alvo.size() > 0) {
					// 最远运输里程和单价
					DelivbillHVO[] printvos = (DelivbillHVO[]) alvo
							.toArray(new DelivbillHVO[0]);
					ArrayList alRet = DelivbillHBO_Client
							.getMaxmileAndPriceForPrint(printvos);
					for (int i = 0; i < printvos.length; i++) {
						DMDataVO dmdMaxmileAndPrice = (DMDataVO) alRet.get(i);
						printvos[i].getParentVO().setAttributeValue(
								"dmaxmile",
								dmdMaxmileAndPrice
										.getAttributeValue("dmaxmile"));
						printvos[i].getParentVO().setAttributeValue(
								"dmaxmileprice",
								dmdMaxmileAndPrice
										.getAttributeValue("dmaxmileprice"));
					}
					// onListPrint(getBillCardPanel(), alvo);
					// 存货自由项改为公式带出
					nc.ui.scm.pub.FreeVOParse freeVOParse = new nc.ui.scm.pub.FreeVOParse();
					for (int i = 0; i < m_bills.length; i++) {
						DelivbillHItemVO[] items = (DelivbillHItemVO[]) m_bills[i]
								.getChildrenVO();
						if (items != null && items.length != 0
								&& items[0] != null
								&& items[0].getVinvcode() == null) {
							execFormulaBodys(items);
							freeVOParse.setFreeVO(items, null, "pkinv", false);
						}
					}

					BillPrintTool bpt = new BillPrintTool(
							DMBillNodeCodeConst.m_delivDelivBill, alvo,
							getBillCardPanel().getBillData(), null, null, null,
							"vdelivbillcode", "pk_delivbill_h");

					// onBatchPrint(BillListPanel listPanel, String
					// billtypecode)
					bpt.onBatchPrint(getBillListPanel(),
							DMBillTypeConst.m_delivDelivBill);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 打印。 功能： 参数： 返回： 例外： 日期：(2002-7-4 14:42:22) 修改日期，修改人，修改原因，注释标志：
	 */
	public void onPrintPreview() {
		try {
			if (m_strShowState.equals(DMBillStatus.Card)) {
				// onCardPrintPreview(getBillCardPanel());

				ArrayList alBill = new ArrayList();
				alBill.add(m_currentbill);
				// 最远运输里程和单价
				DelivbillHVO[] printvos = new DelivbillHVO[1];
				printvos[0] = m_currentbill;
				ArrayList alRet = DelivbillHBO_Client
						.getMaxmileAndPriceForPrint(printvos);
				DMDataVO dmdMaxmileAndPrice = (DMDataVO) alRet.get(0);
				m_currentbill.getParentVO().setAttributeValue("dmaxmile",
						dmdMaxmileAndPrice.getAttributeValue("dmaxmile"));
				m_currentbill.getParentVO().setAttributeValue("dmaxmileprice",
						dmdMaxmileAndPrice.getAttributeValue("dmaxmileprice"));

				// BillPrintTool(String sModuleCode, ArrayList alBill, BillData
				// bd,
				// Class specialDataSource,String pk_corp, String sUserID,
				// String sNameofBillCodeItem, String sNameofBillIDItem)
				BillPrintTool bpt = new BillPrintTool(
						DMBillNodeCodeConst.m_delivDelivBill, alBill,
						getBillCardPanel().getBillData(), null, null, null,
						"vdelivbillcode", "pk_delivbill_h");

				// onCardPrintPreview(BillCardPanel cardPanel, BillListPanel
				// listPanel,
				// String billtypecode)
				bpt.onCardPrintPreview(getBillCardPanel(), getBillListPanel(),
						DMBillTypeConst.m_delivDelivBill);
			} else if (m_strShowState.equals(DMBillStatus.List)) {
				ArrayList alvo = getSelectedListCacheBills();

				if (alvo.size() > 0) {
					// onListPrintPreview(getBillCardPanel(), alvo);
					// 最远运输里程和单价
					DelivbillHVO[] printvos = (DelivbillHVO[]) alvo
							.toArray(new DelivbillHVO[0]);
					ArrayList alRet = DelivbillHBO_Client
							.getMaxmileAndPriceForPrint(printvos);
					for (int i = 0; i < printvos.length; i++) {
						DMDataVO dmdMaxmileAndPrice = (DMDataVO) alRet.get(i);
						printvos[i].getParentVO().setAttributeValue(
								"dmaxmile",
								dmdMaxmileAndPrice
										.getAttributeValue("dmaxmile"));
						printvos[i].getParentVO().setAttributeValue(
								"dmaxmileprice",
								dmdMaxmileAndPrice
										.getAttributeValue("dmaxmileprice"));
					}

					BillPrintTool bpt = new BillPrintTool(
							DMBillNodeCodeConst.m_delivDelivBill, alvo,
							getBillCardPanel().getBillData(), null, null, null,
							"vdelivbillcode", "pk_delivbill_h");

					// onBatchPrint(BillListPanel listPanel, String
					// billtypecode)
					bpt.onBatchPrintPreview(getBillListPanel(),
							DMBillTypeConst.m_delivDelivBill);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 *
	 */
	public void onRefresh() {
		m_isOperationFinish = false;
		if (ivjQueryConditionDlg == null) {
			return;
		}
		onQueryDetail();
		showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH007") /*
																				 * @res
																				 * 刷新成功
																				 */);
	}

	/**
	 * 查询按钮处理。 创建日期：(2002-6-5 10:51:47)
	 */
	public void onQuery() {
		getConditionDlg().showModal();
		m_isOperationFinish = true;
		if (!getConditionDlg().isCloseOK()) {
			m_isOperationFinish = false; // 确定“发运单查询”是查询返回还是关闭、取消返回
			return;
		}
		showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH046") /*
																				 * @res
																				 * 正在查询
																				 */);
		onQueryDetail();
		setButton(boRefresh, true);
		showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH009") /*
																				 * @res
																				 * 查询完成
																				 */);
		this.hasChangedPack = false;
	}

	/**
	 *
	 *
	 */
	private void onQueryDetail() {
		try {
			String state = getShowState();
			if (state.equals(DMBillStatus.Card)) {
				onSwith();
			}

			m_ConditionVos = getConditionDlg().getConditionVO();
			m_ConditionVos = nc.ui.scm.pub.query.ConvertQueryCondition
					.getConvertedVO(m_ConditionVos, null);

			// 表头查询条件
			String strHead = getStrCndHead(m_ConditionVos);
			if (strHead != null && strHead.trim().length() > 0) {
				strHead = " (" + strHead + ") ";

				// 表体查询条件
			}
			String strItem = getStrCndItem(m_ConditionVos);
			if (strItem != null && strItem.trim().length() > 0) {
				strItem = " (" + strItem + ") ";
			}

			String sStatusSql = getQueryStatusConditionPane().getSQL();
			if (sStatusSql != null && sStatusSql.trim().length() > 0) {
				if (strItem == null || strItem.trim().length() == 0) {
					strItem = " (" + sStatusSql + ") ";
				} else if (strItem != null && strItem.trim().length() > 0) {
					strItem = " and (" + sStatusSql + ") ";
				}
			}

			// 为查询库存组织权限准备数据skg
			DMDataVO dmdvoSendStororg = new DMDataVO();
			dmdvoSendStororg.setAttributeValue("userid", getUserID());
			dmdvoSendStororg.setAttributeValue("pkcorp", getCorpID());
			dmdvoSendStororg.setAttributeValue("agentcorpids",
					getAgentCorpIDsofDelivOrg());
			if (isQueryForWayLoss) {
				m_bills = DelivbillHBO_Client.findDelivBillsForWayLoss(
						getDelivOrgPK(), strHead, strItem, m_isForSendSide,
						new ClientLink(getClientEnvironment()));
			} else if (bIsForSofee) {
				m_bills = DelivbillHBO_Client
						.findDelivBillsWithStorOrgPowerForSofee(
								getDelivOrgPK(), strHead, strItem,
								dmdvoSendStororg, new UFBoolean(true));
			} else {
				m_bills = DelivbillHBO_Client.findDelivBillsWithStorOrgPower(
						getDelivOrgPK(), strHead, strItem, dmdvoSendStororg);
			}

			if (m_bills == null) {
				m_bills = new DelivbillHVO[0];
			}

			m_headers = new DelivbillHHeaderVO[m_bills.length];
			for (int i = 0; i < m_bills.length; i++) {
				m_headers[i] = (DelivbillHHeaderVO) m_bills[i].getParentVO();
			}

			// 只有当存在发运单，且不是为途损而查询时，才进行如下操作
			if (m_bills.length > 0 && !isQueryForWayLoss) {
				// 路线描述
				nc.vo.dm.dm001.DelivorgVO delivOrg = nc.ui.dm.dm001.DelivorgHelper
						.findByPrimaryKey(getDelivOrgPK());

				for (int i = 0; i < m_bills.length; i++) {
					/** ******Begin: added by zxping***** */
					DelivbillHItemVO[] items = (DelivbillHItemVO[]) m_bills[i]
							.getChildrenVO();
					if (null != items && items.length > 0) {
						if (delivOrg != null && delivOrg.getParentVO() != null) {
							nc.vo.dm.dm001.DelivorgHeaderVO delivOrgHeader = (nc.vo.dm.dm001.DelivorgHeaderVO) delivOrg
									.getParentVO();
							if (delivOrgHeader.getDelivsequence() != null
									&& delivOrgHeader.getDelivsequence()
											.intValue() == 1) {
								UFDouble zero = new UFDouble(0.0);
								for (i = 0; i < items.length; i++) {
									if (items[i].getDoutnum() == null
											|| items[i].getDoutnum().equals(
													zero)) {
										items[i].setDoutnum(items[i]
												.getDinvnum());

									}
									if (items[i].getDoutassistnum() == null
											|| items[i].getDoutassistnum()
													.equals(zero)) {
										items[i].setDoutassistnum(items[i]
												.getDinvassist());
									}
								}
							}
						}
						m_headers[i].setH_confirmarrivedate((UFDate) items[0]
								.getAttributeValue("confirmarrivedate"));
					} else {
						m_headers[i].setH_confirmarrivedate(null);
					}
					/** ******End: added by zxping***** */
				}
			}

			// 表头数据由公式带出
			execFormulaHeads(m_headers);
			getBillListPanel().setHeaderValueVO(m_headers);

			for (int i = 0; i < m_headers.length; i++) {
				getBillListPanel().getHeadBillModel().setValueAt(
						m_headers[i].getPkdelivmode(), i, "vsendtypename");
				Integer sendType = m_headers[i].getIsendtype();
				int iSendType = (sendType != null ? sendType.intValue()
						: FreightType.ap);
				getBillListPanel().getHeadBillModel().setValueAt(
						FreightType.nameoftype[iSendType], i, "isendtype");
				m_headers[i].setIsendtype(new Integer(iSendType));
			}
			getBillListPanel()
					.getHeadBillModel()
					.execFormulas(
							new String[] { "vsendtypename->getColValue(bd_sendtype,sendname,pk_sendtype,vsendtypename)" });

			// modified by czp on 2002/09/10/13:45 begin
			// 列表查询后清除显示内容
			getBillListPanel().getBodyBillModel().clearBodyData();
			getBillListPanel().getHeadTable().clearSelection();

			getBillListPanel().updateUI();
			switchButtonStatus(AfterQuery);
		} catch (Exception e) {
			handleException(e);
		}
	}

	/**
	 * @param writeBackItems
	 * @param isAfterATPHint
	 * @throws Exception
	 */
	private void onSaveBatch(DMDataVO[] writeBackItems, boolean isAfterATPHint)
			throws Exception {

		DelivbillHVO delivBillVO;
		DelivbillHHeaderVO header;
		DelivbillHItemVO[] litems;

		ArrayList keylist;
		ArrayList alKeylists;

		// 是否进行可用量检查,设为“否”
		for (int i = 0; i < m_batchbills.length; i++) {
			litems = (DelivbillHItemVO[]) m_batchbills[i].getChildrenVO();

			for (int k = 0; k < litems.length; k++) {
				litems[k].setischeckatp(new UFBoolean(isAfterATPHint));
			}
		}

		// 保存新增的发运单 或者 发运单表体
		alKeylists = DelivbillHBO_Client.insertBills(m_batchbills,
				writeBackItems, new ClientLink(getClientEnvironment()));
		for (int i = 0; i < m_batchbills.length; i++) {
			delivBillVO = m_batchbills[i];
			header = (DelivbillHHeaderVO) delivBillVO.getParentVO();
			litems = (DelivbillHItemVO[]) delivBillVO.getChildrenVO();

			// * 第一个返回元素 String 为表头pk， 第二个返回元素 String[] 为表体的 pk 数组，
			// * 第三个返回元素 String 为表头 ts, 第四个返回元素 String[] 为表体 ts 数组
			keylist = (ArrayList) alKeylists.get(i);

			header.setPk_delivbill_h(keylist.get(0).toString());
			header.setAttributeValue("ts", keylist.get(2)); // 表头ts

			header.setStatus(nc.vo.pub.VOStatus.UNCHANGED);

			// 给表头、表体部分的发运单pk赋值，这些pk来源于后台操作
			for (int k = 0; k < litems.length; k++) {
				litems[k].setPk_delivbill_b(((String[]) keylist.get(1))[k]);
				litems[k].setAttributeValue("ts",
						((String[]) keylist.get(3))[k]); // 表体ts，冗余
				litems[k].setPk_delivbill_h(keylist.get(0).toString());

				litems[k].setStatus(nc.vo.pub.VOStatus.UNCHANGED);
			}
		}
		m_currentbill = m_batchbills[m_listRow];
		setBillVOIntoUI();
		// 新增后刷新列表界面
		ArrayList alHeaders = new ArrayList();
		ArrayList alBills = new ArrayList();
		if (m_headers == null) {
			m_headers = new DelivbillHHeaderVO[0];
		}
		if (m_bills == null) {
			m_bills = new DelivbillHVO[0];
		}
		for (int i = 0; i < m_headers.length; i++) {
			alHeaders.add(m_headers[i]);
			alBills.add(m_bills[i]);
		}
		for (int i = 0; i < m_batchbills.length; i++) {
			alHeaders.add((DelivbillHHeaderVO) m_batchbills[i].getParentVO());
			alBills.add(m_batchbills[i]);
		}
		m_headers = (DelivbillHHeaderVO[]) alHeaders
				.toArray(new DelivbillHHeaderVO[0]);
		m_bills = (DelivbillHVO[]) alBills.toArray(new DelivbillHVO[0]);
		getBillListPanel().setHeaderValueVO(m_headers);

		//
		getBillCardPanel().setHeadItem("vdelivbillcode",
				m_batchbills[m_listRow].getVBillCode());

		getBillCardPanel().updateUI();
		getBillCardPanel().setEnabled(false);
		switchButtonStatusWithBill();
		m_isOperationFinish = true;
		// m_bIsGetNewBillCode = false;
		m_batchbills = null;
		setEditFlag(-1);

		// 保存后清空包装明细内容
		m_dmdvosPackageVOs = null;

		//
		getBillCardPanel().getBillModel().execLoadFormula(); // 实际上执行的只是表体部分的加载公式

		// 要执行表头、表尾部分的加载公式，需要
		getBillCardPanel().execHeadTailLoadFormulas();

		showHintMessage(NCLangRes.getInstance().getStrByID("40140408",
				"UPP40140408-000045") /* @res "保存成功！" */);

	}

	/**
	 * @param changedVO
	 * @param writeBackItems
	 * @throws Exception
	 */
	private void onSave(DelivbillHVO changedVO, DMDataVO[] writeBackItems,
			boolean isAfterATPHint) throws Exception {

		DelivbillHHeaderVO header = (DelivbillHHeaderVO) changedVO
				.getParentVO();
		DelivbillHItemVO[] changeitems = (DelivbillHItemVO[]) changedVO
				.getChildrenVO();

		// 是否进行可用量检查,设为“否”
		for (int i = 0; i < changeitems.length; i++) {
			changeitems[i].setischeckatp(new UFBoolean(isAfterATPHint));
		}

		ArrayList keylist;

		if (getEditFlag() == 0) { // 新增

			// 保存新增的发运单 或者 发运单表体
			// * 第一个返回元素 String 为表头pk， 第二个返回元素 String[] 为表体的 pk 数组，
			// * 第三个返回元素 String 为表头 ts, 第四个返回元素 String[] 为表体 ts 数组
			keylist = (ArrayList) DelivbillHBO_Client.insertDelivAndPackage(
					changedVO, writeBackItems, m_dmdvosPackageVOs,
					new ClientLink(getClientEnvironment()));

			m_ufdtAddTime = null;
			header.setPk_delivbill_h((String) keylist.get(0));

			// 得到时间戳ts
			header.setAttributeValue("ts", (String) keylist.get(2));
			header.setStatus(nc.vo.pub.VOStatus.UNCHANGED);

			// 给表头、表体部分的发运单pk赋值，这些pk来源于后台操作
			for (int i = 0; i < changeitems.length; i++) {
				changeitems[i]
						.setPk_delivbill_b(((String[]) keylist.get(1))[i]);
				changeitems[i].setAttributeValue("ts", ((String[]) keylist
						.get(3))[i]); // 表体ts，冗余
				changeitems[i].setPk_delivbill_h((String) keylist.get(0));

				changeitems[i].setStatus(nc.vo.pub.VOStatus.UNCHANGED);
			}

			m_currentbill = changedVO;
			setBillVOIntoUI();

			// 新增后刷新列表界面
			int headssize = m_headers.length;
			Vector headersVector = new Vector(headssize + 1);
			Vector billsVector = new Vector(headssize + 1);
			for (int i = 0; i < headssize; i++) {
				headersVector.add(m_headers[i]);
				billsVector.add(m_bills[i]);
			}

			headersVector.add(header);
			billsVector.add(m_currentbill);
			m_headers = new DelivbillHHeaderVO[headssize + 1];
			m_bills = new DelivbillHVO[headssize + 1];
			headersVector.copyInto(m_headers);
			billsVector.copyInto(m_bills);
			getBillListPanel().setHeaderValueVO(m_headers);
			m_listRow = headssize;

			// 发运安排中的发运单新增保存后“制单人”显示
			getBillCardPanel().getTailItem("pkbillperson")
					.setValue(getUserID());
		}

		else if (getEditFlag() == 1) { // 修改
			// 返回结果存有表体新增pk和单据时间戳
			keylist = DelivbillHBO_Client.updateDelivAndPack(changedVO,
					writeBackItems, m_dmdvosPackageVOs, new ClientLink(
							getClientEnvironment()));

			// 得到时间戳ts：返回的 keylist 中最后一个元素是时间戳
			if (keylist.size() > 0 && keylist.get(keylist.size() - 1) != null) {
				header.setAttributeValue("ts", keylist.get(keylist.size() - 1)
						.toString());

				// 设定新增加的表体行的 pk
			}
			for (int i = 0; i < changeitems.length && i < keylist.size(); i++) {
				if (keylist.get(i) != null
						&& changeitems[i].getStatus() == nc.vo.pub.VOStatus.NEW) {
					changeitems[i].setPk_delivbill_b(keylist.get(i).toString());
					changeitems[i]
							.setPk_delivbill_h(header.getPk_delivbill_h());
				}
			}
			// 置入数据
			updateCurrentBill(changedVO);
			setBillVOIntoUI();
			updateBills(m_currentbill);
		}

		// 刷新界面
		getBillCardPanel().getBillModel().execLoadFormula();

		getBillCardPanel().updateUI();
		getBillCardPanel().setEnabled(false);
		switchButtonStatusWithBill();
		m_isOperationFinish = true;
		m_bIsGetNewBillCode = false;
		// 保存后清空包装明细内容
		m_dmdvosPackageVOs = null;
		showHintMessage(NCLangRes.getInstance().getStrByID("40140408",
				"UPP40140408-000045") /* @res "保存成功！" */);
	}

	/**
	 * 功能：重置发运单包装分类
	 * 
	 * @param changedVO
	 * @param writeBackItems
	 * @throws Exception
	 */
	private void resetPackgeVO(DelivbillHVO changedVO) throws Exception {
		// 未启用包装分类运费计算
		if (getBillCardPanel().getBodyItem("packsortcode") == null) {
			return;
		}
		// 新增时，未点包装单按钮，自动同步包装单
		else if (getEditFlag() == 0 && m_dmdvosPackageVOs == null) {
			// 由下面代码同步
		}
		// 新增时，先改发运单，再点包装单按钮，hasChangedPack由onPack置为false,不同步包装单
		else if (getEditFlag() == 0 && m_dmdvosPackageVOs != null
				&& !hasChangedPack) {
			return;
		}
		// 修改时，未点包装单按钮，自动同步包装单
		else if (getEditFlag() == 1 && m_dmdvosPackageVOs == null
				&& hasChangedPack
				&& getBillCardPanel().getBodyItem("packsortcode").isShow()) {
			// 由下面代码同步
		}
		// 修改时，先改发运单，再点包装单按钮，hasChangedPack由onPack置为false,不同步包装单
		else if (getEditFlag() == 1 && m_dmdvosPackageVOs != null
				&& !hasChangedPack
				&& getBillCardPanel().getBodyItem("packsortcode").isShow()) {
			return;
		}
		// 修改时，发运单中修改不影响包装单，不同步包装单
		else if ((!hasChangedPack)
				&& (getBillCardPanel().getBodyItem("packsortcode").isShow())) {
			return;
		}

		// 自动同步包装单
		DMVO dmvo = new DMVO();
		DelivbillHVO editvo = (DelivbillHVO) getBillCardPanel().getBillValueVO(
				DelivbillHVO.class.getName(),
				DelivbillHHeaderVO.class.getName(),
				DelivbillHItemVO.class.getName());
		if (getEditFlag() == 0) { // 新增
			((DelivbillHHeaderVO) (editvo.getParentVO()))
					.setPk_delivbill_h(null);
		}
		dmvo.translateFromOtherVO(editvo);
		setPackageListDlg(null);
		getPackageListDlg().setDelivVO(dmvo);
		getPackageListDlg().setDelivbillvo(editvo);
		getPackageListDlg().setIsDlgSave(false);
		getPackageListDlg().setVOintoUI();
		getPackageListDlg().onOK();
		m_dmdvosPackageVOs = getPackageListDlg().getPackageVOs();
		((DelivbillHHeaderVO) changedVO.getParentVO())
				.setDallpacknum(((DelivbillHHeaderVO) getPackageListDlg()
						.getDelivbillvo().getParentVO()).getDallpacknum());
		((DelivbillHHeaderVO) changedVO.getParentVO())
				.setDallweight(((DelivbillHHeaderVO) getPackageListDlg()
						.getDelivbillvo().getParentVO()).getDallweight());
		((DelivbillHHeaderVO) changedVO.getParentVO())
				.setDallvolumn(((DelivbillHHeaderVO) getPackageListDlg()
						.getDelivbillvo().getParentVO()).getDallvolumn());

		// getBillCardPanel().setHeadItem("dallpacknum",
		// ((DelivbillHHeaderVO)
		// getPackageListDlg().getDelivbillvo().getParentVO()).getDallpacknum());
		// getBillCardPanel().setHeadItem("dallweight",
		// ((DelivbillHHeaderVO)
		// getPackageListDlg().getDelivbillvo().getParentVO()).getDallweight());
		// getBillCardPanel().setHeadItem("dallvolumn",
		// ((DelivbillHHeaderVO)
		// getPackageListDlg().getDelivbillvo().getParentVO()).getDallvolumn());
	}

	public void onBatch() throws BusinessException {
		if (getEditFlag() == 2) {
			onSaveBatch();
		} else {
			onSave();
		}
	}

	/**
	 * 保存新增或者修改过的发运单。
	 * <p>
	 * 需要进行的操作有：
	 * <ul>
	 * <li>----停止界面的新增、编辑状态；</li>
	 * <li>----计算金额；</li>
	 * <li>----获取新增的、修改过的 VO 和所有的 VO；</li>
	 * <li>----插入到已有发运单；</li>
	 * <li>----置入制单人、日期等值；</li>
	 * <li>----生成单据号；</li>
	 * <li>----校验:表头编码是否重复、表体编码是否重复、表头编码是否为空、表体是否有空行；</li>
	 * <li>----回写日计划；</li>
	 * <li>----得到时间戳ts（返回的 keylist 中最后一个元素是时间戳）；</li>
	 * <li>----设定新增加的表体行的 pk；</li>
	 * <li>----置入数据、刷新界面。</li>
	 * </ul>
	 * 
	 * @author zxj
	 */
	public void onSave() {
		if (getEditFlag() == 2) {
			try {
				onSaveBatch();
			} catch (BusinessException e) {
			}
			return;
		}

		m_bIsGetNewBillCode = false;
		m_isOperationFinish = false;

		// 需要回写日计划的vo
		DMDataVO[] writeBackItems = null;
		DelivbillHVO changedVO = null;

		try {
			getBillCardPanel().stopEditing();
			showHintMessage(NCLangRes.getInstance().getStrByID("common",
					"UCH044") /* 正在保存 */);

			// 更改的vo
			changedVO = (DelivbillHVO) getBillCardPanel().getBillValueChangeVO(
					"nc.vo.dm.dm104.DelivbillHVO",
					"nc.vo.dm.dm104.DelivbillHHeaderVO",
					"nc.vo.dm.dm104.DelivbillHItemVO");

			// 生成发运单的id时，需要提供公司pk，
			changedVO.setGenOIDPK(getCorpID());

			// 卡片上的vo
			DelivbillHVO delivBillVO = (DelivbillHVO) getBillCardPanel()
					.getBillValueVO("nc.vo.dm.dm104.DelivbillHVO",
							"nc.vo.dm.dm104.DelivbillHHeaderVO",
							"nc.vo.dm.dm104.DelivbillHItemVO");

			checkVOs(new nc.vo.dm.dm104.DelivbillHVO[] { delivBillVO });
			if (!checkVO(delivBillVO,
					(nc.ui.dm.pub.cardpanel.DMBillCardPanel) getBillCardPanel())) {
				return;
			}

			DelivbillHHeaderVO header = (DelivbillHHeaderVO) changedVO
					.getParentVO();
			DelivbillHItemVO[] changeitems = (DelivbillHItemVO[]) changedVO
					.getChildrenVO();
			DelivbillHItemVO[] items = (DelivbillHItemVO[]) delivBillVO
					.getChildrenVO();

			header.setAttributeValue("userid", getUserID());
			// 置入制单人和日期
			header.setAttributeValue("pkbillperson", getUserID());
			// header.setAttributeValue("vbillpersonname", getUserName());
			// //commented
			// by zxping 2005-01-13
			header.setAttributeValue("billdate", getClientEnvironment()
					.getDate());

			// 插入到已有发运单
			if (getInsertExist()) {
				Vector vTemp = new Vector();
				// 合并表体记录
				for (int j = 0; (changeitems != null)
						&& (j < changeitems.length); j++) {
					String pk = changeitems[j].getPk_delivbill_b();
					if (pk != null && pk.trim().length() > 0) { // 修改过的，还有删除的
						vTemp.addElement(changeitems[j]);
					}
				}
				for (int j = 0; j < items.length; j++) {
					String pk = items[j].getPk_delivbill_b();
					if (pk == null || pk.trim().length() == 0) { // 新增的
						if (items[j].getAttributeValue("pkcorpforgenoid") == null) {
							items[j].setAttributeValue("pkcorpforgenoid",
									getCorpID());
						}
						vTemp.addElement(items[j]);
					}
				}
				DelivbillHItemVO[] bodyVO = new DelivbillHItemVO[vTemp.size()];
				vTemp.copyInto(bodyVO);
				changedVO.setChildrenVO(bodyVO);
			}
			if (getEditFlag() == 0) { // 新增
				m_oldBillCode = (String) header
						.getAttributeValue("vdelivbillcode");
				if (header.getAttributeValue("pkcorpforgenoid") == null) {
					header.setAttributeValue("pkcorpforgenoid",
							getCorpPrimaryKey());
					// 可以手填修改单据号
					// 生成单据号(需要首先注册单据类型)
				}

				// 新增则添加制单时间
				header.setAttributeValue("tmaketime", ClientEnvironment
						.getServerTime().toString());

				GeneralMethod.setBillCode(header,
						DMBillTypeConst.m_delivDelivBill, getBillCardPanel(),
						"vdelivbillcode", "pkcorpforgenoid");

				m_bIsGetNewBillCode = true;
			}

			// 存入旧单据号
			header.setAttributeValue("voldDelivbillcode", m_oldBillCode);

			if (getEditFlag() == 0) { // 新增
				// 增加
				// (代码调试用实际情况本节点没有增加按钮)//commented by zxping 2005-01-13
				// 填入新增点击时间
				if (null == m_ufdtAddTime) {
					showErrorMessage(NCLangRes.getInstance().getStrByID(
							"40140408", "UPP40140408-000044") /*
															 * @res
															 * "未能获得新增单据时的时间!"
															 */);
					return;
				}
				header.setAttributeValue("billnewaddtime", m_ufdtAddTime);

				header.setPkdelivorg(getDelivOrgPK());
				// header.setVdoname(getDelivOrgName()); //commented by zxping
				// 2005-01-13
				header.setStatus(nc.vo.pub.VOStatus.NEW);

				// 回写日计划
				writeBackItems = new DMDataVO[changeitems.length];

				// 组织接口需要传入的DMDataVO数组
				// @see nc.bs.dm.dm102.DeliverydailyplanBO.setSendNum(DMDataVO
				// [])
				for (int i = 0; i < changeitems.length; i++) {
					changeitems[i].setAttributeValue("irowstatus", new Integer(
							DelivBillStatus.Free));

					writeBackItems[i] = new DMDataVO();
					writeBackItems[i] = writeBackItems[i]
							.translateFromOtherVO(changeitems[i]);

					// by zxping:对于日计划而言，永远都是更新 发运数量
					// 因此，如下以下语句的状态，
					// 应该表明该次发运数量的更改方向，或者说是发运单行此时的操作方向，包括 删除、更改、新增三种
					writeBackItems[i].setStatus(nc.vo.pub.VOStatus.NEW); // nc.vo.pub.VOStatus.UPDATED);

					writeBackItems[i].setAttributeValue("pkbillh",
							changeitems[i].getPkorder());
					writeBackItems[i].setAttributeValue("pkbillb",
							changeitems[i].getPkorderrow());
					writeBackItems[i].setAttributeValue("ndelivernum",
							changeitems[i].getDinvnum());
					writeBackItems[i].setAttributeValue("vbilltype",
							changeitems[i].getAttributeValue("vbilltype")); // 来源单据类型
					writeBackItems[i].setAttributeValue("pk_delivdaypl",
							changeitems[i].getPkdayplan());

					writeBackItems[i].setAttributeValue("pkparamcorp",
							getCorpID());
					writeBackItems[i].setAttributeValue("pksendstoreorg",
							changeitems[i].getPksendstockorg()); //
					writeBackItems[i].setAttributeValue("snddate", header
							.getSenddate()); //
					writeBackItems[i].setAttributeValue("userid", getUserID());

					writeBackItems[i].setAttributeValue("borderreturn",
							changeitems[i].getAttributeValue("borderreturn")); // 是否退货
					writeBackItems[i].setAttributeValue("pkorderplanid",
							changeitems[i].getAttributeValue("pkorderplanid")); // 到货计划
					// @see
					// nc.vo.dm.dm102.DMDelivdayplVOTool.getDelivWritePOData(DMDataVO
					// [])
					// 发货公司
					writeBackItems[i].setAttributeValue("pksalecorp",
							changeitems[i].getPksalecorp());
					// 到货公司
					writeBackItems[i].setAttributeValue("pkarrivecorp",
							changeitems[i].getPkarrivecorp());
					// 发运新数量
					writeBackItems[i].setAttributeValue("ddelivernewnum",
							changeitems[i].getDinvnum());
				}
				resetPackgeVO(changedVO);// 根据需要重置包装单vo

				changedVO.getParentVO().setAttributeValue("clastmodiname",
						getUserName());
				changedVO.getParentVO().setAttributeValue("clastmodifierid",
						getUserID());
				changedVO.getParentVO().setAttributeValue("tlastmodifytime",
						ClientEnvironment.getServerTime().toString());
				// 检查日计划状态是否关闭
				// DelivbillHHeaderVO headervo = (DelivbillHHeaderVO)
				// changedVO.getParentVO();
				DelivbillHItemVO[] changeitemsvo = (DelivbillHItemVO[]) changedVO
						.getChildrenVO();

				onSave(changedVO, writeBackItems, false);
			}

			else if (getEditFlag() == 1) { // 修改
				header.setStatus(nc.vo.pub.VOStatus.UPDATED);
				header.setAttributeValue("userid", getUserID());

				// 回写日计划的VO
				writeBackItems = DelivBillVOTool.getWriteBackBill(
						m_currentbill, header, changeitems, new ClientLink(
								getClientEnvironment())); // 由修改前的单据和修改后的单据得到回写日计划的单据

				// 为发运安排插入到已有发运单
				for (int i = 0; i < changeitems.length; i++) {
					changeitems[i].setAttributeValue("irowstatus", new Integer(
							DelivBillStatus.Free));

					if (changeitems[i].getPk_delivbill_b() == null) {
						changeitems[i].setStatus(nc.vo.pub.VOStatus.NEW);
					}
				}

				resetPackgeVO(changedVO);// 根据需要重置包装单vo

				changedVO.getParentVO().setAttributeValue("clastmodiname",
						getUserName());
				changedVO.getParentVO().setAttributeValue("clastmodifierid",
						getUserID());
				changedVO.getParentVO().setAttributeValue("tlastmodifytime",
						ClientEnvironment.getServerTime().toString());

				onSave(changedVO, writeBackItems, false);

			}
			// eric
			setEditFlag(DMBillStatus.CardView);
			hasChangedPack = false;// 包装单不再重算
			showHintMessage(NCLangRes.getInstance().getStrByID("common",
					"UCH005") /* 保存成功 */);
		} catch (ATPNotEnoughException ane) {
			if (ane.getHint() == null) {
				handleException(ane);
			} else {
				String sChooseMessage = NCLangRes.getInstance().getStrByID(
						"40140408", "UPP40140408-000192", null,
						new String[] { ane.getMessage() })/* @res "{0},是否继续保存发运单？" */;
				int iFlag = showYesNoMessage(sChooseMessage);
				if (iFlag == nc.ui.pub.beans.MessageDialog.ID_YES) {
					try {
						onSave(changedVO, writeBackItems, true);
					} catch (Exception e) {
						handleException(e);
					}
				} else {
					return;
				}
			}
		} catch (Exception e) {
			handleException(e);
		} finally {
			if (m_bIsGetNewBillCode) {
				try {
					DelivbillHVO delivBillVO = (DelivbillHVO) getBillCardPanel()
							.getBillValueVO("nc.vo.dm.dm104.DelivbillHVO",
									"nc.vo.dm.dm104.DelivbillHHeaderVO",
									"nc.vo.dm.dm104.DelivbillHItemVO");
					// 释放单据号
					if (delivBillVO != null
							&& delivBillVO.getParentVO() != null) {
						delivBillVO.getParentVO().setAttributeValue(
								"pkcorpforgenoid", getCorpPrimaryKey());
						DelivbillHBO_Client.returnBillCodeForUI(
								"pkcorpforgenoid", "vdelivbillcode",
								new DelivbillHVO[] { delivBillVO });
					}
				} catch (Exception e1) {
					// SCMEnv.error(e1);
					// if (e1 instanceof BusinessException) {
					// showErrorMessage(e1.getMessage());
					// }
					// else {
					// showErrorMessage(NCLangRes.getInstance().getStrByID("40140408",
					// "UPP40140408-000046") /* @res "释放单据号失败，请退出节点！" */);
					// showHintMessage(NCLangRes.getInstance().getStrByID("common",
					// "MD2",null,new String[]{""}) /* 保存失败 */);
					// }
					handleException(e1);
				}

			}
		}
	}

	/**
	 * 保存新增或者修改过的发运单。
	 * <p>
	 * 需要进行的操作有：
	 * <ul>
	 * <li>----停止界面的新增、编辑状态；</li>
	 * <li>----计算金额；</li>
	 * <li>----获取新增的、修改过的 VO 和所有的 VO；</li>
	 * <li>----插入到已有发运单；</li>
	 * <li>----置入制单人、日期等值；</li>
	 * <li>----生成单据号；</li>
	 * <li>----校验:表头编码是否重复、表体编码是否重复、表头编码是否为空、表体是否有空行；</li>
	 * <li>----回写日计划；</li>
	 * <li>----得到时间戳ts（返回的 keylist 中最后一个元素是时间戳）；</li>
	 * <li>----设定新增加的表体行的 pk；</li>
	 * <li>----置入数据、刷新界面。</li>
	 * </ul>
	 * 
	 * @author zxj
	 */
	private void onSaveBatch() throws BusinessException {
		m_isOperationFinish = false;
		// m_bIsGetNewBillCode = false;

		// 生成单据号或者保存可能失败，则需要回退本次保存中所有成功生成的单据号
		Vector vDeleteBillCode = new Vector();

		// 需要回写日计划的vo
		DMDataVO[] writeBackItems = null;

		try {
			getBillCardPanel().stopEditing();

			showHintMessage(NCLangRes.getInstance().getStrByID("common",
					"UCH044") /* 正在保存 */);

			// 卡片上的vo
			DelivbillHVO curbillhvo = (DelivbillHVO) getBillCardPanel()
					.getBillValueVO("nc.vo.dm.dm104.DelivbillHVO",
							"nc.vo.dm.dm104.DelivbillHHeaderVO",
							"nc.vo.dm.dm104.DelivbillHItemVO");
			m_batchbills[m_listRow] = curbillhvo;

			checkVOs(m_batchbills);
			for (int i = 0; i < m_batchbills.length; i++) {
				if (!checkVO(
						m_batchbills[i],
						(nc.ui.dm.pub.cardpanel.DMBillCardPanel) getBillCardPanel())) {
					return;
				}
			}

			//
			DelivbillHVO delivBillVO = null;
			DelivbillHHeaderVO header = null;
			DelivbillHItemVO[] items = null;

			ArrayList alDmitems = new ArrayList();
			int i;

			for (int j = 0; j < m_batchbills.length; j++) {
				delivBillVO = m_batchbills[j];
				header = (DelivbillHHeaderVO) delivBillVO.getParentVO();
				items = (DelivbillHItemVO[]) delivBillVO.getChildrenVO();

				// 生成发运单的id时，需要提供公司pk，
				delivBillVO.setGenOIDPK(getCorpID());
				header.setAttributeValue("userid", getUserID());
				header.setVbillpersonname(getUserName());

				// 置入制单人,制单日期和制单时间
				header.setPkoperator(getUserID());
				header.setAttributeValue("billdate", getClientEnvironment()
						.getDate());

				// 保存旧的单据号，如果此后保存失败，便于恢复
				// 当然，此时，有的单据的单据号为空
				// 存入旧单据号
				header.setAttributeValue("voldDelivbillcode", header
						.getAttributeValue("vdelivbillcode"));

				header.setAttributeValue("pkcorpforgenoid", getCorpID());

				// 可以手填修改单据号
				// 生成单据号(需要首先注册单据类型)
				GeneralMethod.setBillCode(header,
						DMBillTypeConst.m_delivDelivBill, null, // getBillCardPanel(),
						"vdelivbillcode", "pkcorpforgenoid");

				// 生成单据号或者保存可能失败，则需要回退本次保存中所有成功生成的单据号
				vDeleteBillCode.add(delivBillVO);

				if (getEditFlag() == 2) { // 新增

					// 新增则增加“制单时间”
					header.setAttributeValue("tmaketime", ClientEnvironment
							.getServerTime().toString());

					// 填入新增点击时间
					if (null == m_ufdtAddTime) {
						showErrorMessage(NCLangRes.getInstance().getStrByID(
								"40140408", "UPP40140408-000044") /*
																 * @res
																 * "未能获得新增单据时的时间!"
																 */);
						return;
					}
					header.setAttributeValue("billnewaddtime", m_ufdtAddTime);

					header.setPkdelivorg(getDelivOrgPK());
					// header.setVdoname(getDelivOrgName()); //commented by
					// zxping
					// 2005-01-13
					header.setStatus(nc.vo.pub.VOStatus.NEW);

					// 回写日计划
					writeBackItems = new DMDataVO[items.length];
					// 组织接口需要传入的DMDataVO数组
					// @see
					// nc.bs.dm.dm102.DeliverydailyplanBO.setSendNum(DMDataVO
					// [])
					for (i = 0; i < items.length; i++) {
						items[i].setAttributeValue("irowstatus", new Integer(
								DelivBillStatus.Free));

						writeBackItems[i] = new DMDataVO();
						writeBackItems[i] = writeBackItems[i]
								.translateFromOtherVO(items[i]);

						// by zxping:对于日计划而言，永远都是更新 发运数量
						// 因此，如下以下语句的状态，
						// 应该表明该次发运数量的更改方向，或者说是发运单行此时的操作方向，包括 删除、更改、新增三种
						writeBackItems[i].setStatus(nc.vo.pub.VOStatus.NEW); // nc.vo.pub.VOStatus.UPDATED);
						writeBackItems[i].setAttributeValue("pkbillh", items[i]
								.getPkorder());
						writeBackItems[i].setAttributeValue("pkbillb", items[i]
								.getPkorderrow());
						writeBackItems[i].setAttributeValue("ndelivernum",
								items[i].getDinvnum());
						writeBackItems[i].setAttributeValue("vbilltype",
								items[i].getAttributeValue("vbilltype")); // 来源单据类型
						writeBackItems[i].setAttributeValue("pk_delivdaypl",
								items[i].getPkdayplan());
						// the next line is commented by zxping 2005-01-13
						// writeBackItems[i].setAttributeValue("delivstatus",
						// new
						// Integer(changeitems[i].getStatus()));
						writeBackItems[i].setAttributeValue("pkparamcorp",
								getCorpID());
						writeBackItems[i].setAttributeValue("pksendstoreorg",
								items[i].getPksendstockorg()); //
						writeBackItems[i].setAttributeValue("snddate", header
								.getSenddate()); //
						writeBackItems[i].setAttributeValue("userid",
								getUserID());
						// the next line is commented by zxping 2005-01-13
						// writeBackItems[i].setAttributeValue("pk_delivbill_h",
						// header.getPk_delivbill_h());//此项有用？
						writeBackItems[i].setAttributeValue("borderreturn",
								items[i].getAttributeValue("borderreturn"));
						// 是否退货
						writeBackItems[i].setAttributeValue("pkorderplanid",
								items[i].getAttributeValue("pkorderplanid"));
						// 到货计划
						// @see
						// nc.vo.dm.dm102.DMDelivdayplVOTool.getDelivWritePOData(DMDataVO
						// [])
						// 发货公司
						writeBackItems[i].setAttributeValue("pksalecorp",
								items[i].getPksalecorp());
						// 到货公司
						writeBackItems[i].setAttributeValue("pkarrivecorp",
								items[i].getPkarrivecorp());
						// 发运新数量
						writeBackItems[i].setAttributeValue("ddelivernewnum",
								items[i].getDinvnum());
						alDmitems.add(writeBackItems[i]);
						// 发运旧数量 ，新增时不需要传入
						// writeBackItems[i].setAttributeValue("ddeliveroldnum",
						// changeitems[i].getPkarrivecorp());
					}
				}
			}
			writeBackItems = (DMDataVO[]) alDmitems.toArray(new DMDataVO[0]);

			onSaveBatch(writeBackItems, false);
			showHintMessage(NCLangRes.getInstance().getStrByID("common",
					"UCH005") /* 保存成功 */);
		} catch (ATPNotEnoughException ane) {
			if (ane.getHint() == null) {
				showErrorMessage(ane.getMessage());
			} else {
				String sChooseMessage = NCLangRes.getInstance().getStrByID(
						"40140408", "UPP40140408-000192", null,
						new String[] { ane.getMessage() }) /*
															 * @res
															 * "{0},是否继续保存发运单？"
															 */;
				int iFlag = showYesNoMessage(sChooseMessage);
				if (iFlag == nc.ui.pub.beans.MessageDialog.ID_YES) {
					try {
						onSaveBatch(writeBackItems, true);
					} catch (Exception e) {
						showErrorMessage(e.getMessage());
						throw new BusinessException(e);
					}
				} else {
					return;
				}
			}
		} catch (Exception e) {
			// 生成单据号或者保存可能失败，则需要回退本次保存中所有成功生成的单据号
			if (vDeleteBillCode.size() > 0) {
				DelivbillHVO[] deleteBillCodeVOs = (DelivbillHVO[]) vDeleteBillCode
						.toArray(new DelivbillHVO[vDeleteBillCode.size()]);
				for (int i = 0; i < deleteBillCodeVOs.length; i++) {
					m_batchbills[i].getParentVO().setAttributeValue(
							"vdelivbillcode",
							m_batchbills[i].getParentVO().getAttributeValue(
									"voldDelivbillcode"));
				}

				try {
					// 释放单据号
					DelivbillHBO_Client.returnBillCodeForUI("pkcorpforgenoid",
							"vdelivbillcode", deleteBillCodeVOs);
				} catch (Exception e1) {
					e1.printStackTrace();
					if (e1 instanceof BusinessException) {
						showErrorMessage(e1.getMessage());
						throw (BusinessException) e1;
					} else {
						showErrorMessage(NCLangRes.getInstance().getStrByID(
								"40140408", "UPP40140408-000046") /*
																 * @res
																 * "释放单据号失败，请退出节点！"
																 */);
						throw new BusinessException(NCLangRes.getInstance()
								.getStrByID("40140408", "UPP40140408-000046"));
					}
				}
			}
			SCMEnv.error(e);
			showErrorMessage(e.getMessage());
			showHintMessage(NCLangRes.getInstance().getStrByID("common", "MD2",
					null, new String[] { "" }) /* 保存失败 */);
			throw new BusinessException(e.getMessage());

		}

	}

	/**
	 * 发运清单。 创建日期：(2002-6-26 9:04:33)
	 */
	public void onShowListBill() {
		DelivbillHVO billFilterByCust = null;
		String pkcumandoc = "";
		getCndtnDlgList().showModal();
		if (!getCndtnDlgList().isCloseOK()) {
			return;
		}
		m_ConditionVos = getCndtnDlgList().getConditionVO();
		m_ConditionVos = nc.ui.scm.pub.query.ConvertQueryCondition
				.getConvertedVO(m_ConditionVos, null);

		// Modified by xhq 2002/10/08 begin
		String sCustName = null;
		String pkdeststockorg = null;
		// Modified by xhq 2002/10/08 end

		if (null != m_ConditionVos) {
			ConditionVO oneCondtionVO = null;
			for (int i = 0; i < m_ConditionVos.length; i++) {
				oneCondtionVO = m_ConditionVos[i];

				String sFieldCode = oneCondtionVO.getFieldCode().trim();
				String sValue = oneCondtionVO.getValue().trim();
				// 按数量查询
				if (sFieldCode.equalsIgnoreCase("pkcusmandoc") == true) {
					// Modified by xhq 2002/10/08 begin
					// pkcumandoc=
					// oneCondtionVO.getRefResult().getRefPK().toString().trim();
					pkcumandoc = sValue;
					sCustName = oneCondtionVO.getRefResult().getRefName();
					// Modified by xhq 2002/10/08 end
				} else if (sFieldCode.equalsIgnoreCase("pkdeststockorg") == true) {
					pkdeststockorg = sValue;
					sCustName = oneCondtionVO.getRefResult().getRefName();
				}
			}
		}
		billFilterByCust = filterBillByCust(pkcumandoc, pkdeststockorg);
		if (billFilterByCust == null) {
			return;
		}
		// Modified by xhq 2002/10/08 begin
		// getDelivLisDlg().setDelivbillHVO(billFilterByCust);
		getDelivLisDlg().setDelivbillHVO(billFilterByCust, sCustName);
		// Modified by xhq 2002/10/08 end
		getDelivLisDlg().showModal();
	}

	/**
	 * 表单和列表界面间的相互切换
	 */
	public void onSwith() {
		try {
			m_isOperationFinish = false;
			String state = getShowState();
			if (state.equals(DMBillStatus.List)) {
				int i = getBillListPanel().getHeadTable().getSelectedRow();
				if (i < 0 || i >= m_headers.length) {
					MessageDialog.showHintDlg(this, null, NCLangRes
							.getInstance().getStrByID("common", "UCH004") /*
																		 * @res
																		 * 请选择要处理的数据行
																		 * ！
																		 */);
					return;
				}
				super.onSwith();
				// m_currentbill = (DelivbillHVO) m_bills[i].clone();
				m_currentbill = (DelivbillHVO) m_bills[i];
				m_currentbill.getParentVO().setAttributeValue("vdoname",
						getDelivOrgName());

				if (m_currentbill != null) {
					getBillCardPanel().setBillValueVO(m_currentbill);
					setCardComboxName(m_currentbill);

					// Modified by xhq 2002/10/07 begin
					DelivbillHItemVO bodyVO[] = (DelivbillHItemVO[]) m_currentbill
							.getChildrenVO();
					if (bodyVO != null && bodyVO.length > 0) {
						String s[] = new String[] {
								"creceiptcorp->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,creceiptcorpid)",
								"creceiptcorp->getColValue(bd_cubasdoc,custname,pk_cubasdoc,creceiptcorp)" };
						getBillCardPanel().getBillModel().execFormulas(s);
						for (int j = 0; j < bodyVO.length; j++) {
							if (bodyVO[j] != null) {
								bodyVO[j].setAttributeValue("creceiptcorp",
										getBillCardPanel().getBodyValueAt(j,
												"creceiptcorp"));
							}
						}
						getBillCardPanel().getBillModel().execLoadFormula();
						if (m_currentbill.getParentVO() != null
								&& m_currentbill.getParentVO()
										.getAttributeValue("pkdelivroute") != null) {
							UIRefPane currentRef = ((UIRefPane) getBillCardPanel()
									.getHeadItem("pkdelivroute").getComponent());
							String pkdelivroute = (String) m_currentbill
									.getParentVO().getAttributeValue(
											"pkdelivroute");
							SCMEnv
									.info("****pkdelivroute: ****"
											+ pkdelivroute);
							// getBillCardPanel().setHeadItem("pkdelivroute",pkdelivroute);
							currentRef.setPK(pkdelivroute);
							getBillCardPanel().setHeadItem("vroutename",
									currentRef.getRefName());
							updateRouteDesc();
						} else {
							SCMEnv.info("****pkdelivroute:**** null");
						}

					}
				}

				switchButtonStatusWithBill();
				getBillCardPanel().updateValue();
				getBillCardPanel().updateUI();
				m_isOperationFinish = true;

			} else if (state.equals(DMBillStatus.Card)) {
				super.onSwith();
				// m_headers= RouteBO_Client.findAllHeader();
				getBillCardPanel().setEnabled(false);
				if (null == m_headers || m_headers.length == 0) {
					getBillListPanel().getHeadBillModel().resumeValue();
					getBillListPanel().getBodyBillModel().resumeValue();
				} else {
					for (int i = 0; i < m_bills.length; i++) {
						m_headers[i] = (DelivbillHHeaderVO) m_bills[i]
								.getParentVO();
					}
					getBillListPanel().setHeaderValueVO(m_headers);
					for (int i = 0; i < m_headers.length; i++) {
						getBillListPanel().getHeadBillModel().setValueAt(
								m_headers[i].getPkdelivmode(), i,
								"vsendtypename");
						getBillListPanel()
								.getHeadBillModel()
								.execFormulas(
										i,
										new String[] { "vsendtypename->getColValue(bd_sendtype,sendname,pk_sendtype,vsendtypename)" });
					}
					if (m_currentbill != null) {
						getBillCardPanel().setBillValueVO(m_currentbill);
						getBillListPanel().setBodyValueVO(
								m_currentbill.getChildrenVO());
					}
				}
				getBillListPanel().setBodyValueVO(null);
				// 新增
				// 修改
				// if (m_listRow >= 0 && m_listRow < m_headers.length)
				// getBillListPanel().getHeadTable().setRowSelectionInterval(m_listRow,
				// m_listRow);

				getBillListPanel().getHeadTable().clearSelection();
				getBillListPanel().getBodyTable().clearSelection();

				m_isOperationFinish = true;

			}

		} catch (Exception e) {
			dispErrorMessage(e);
		}
	}

	/**
	 * 计算运费。 创建日期：(2002-6-17 15:03:53)
	 */
	public void onTestCalculateFee() {
		try {
			DMDataVO ddvo = new DMDataVO();
			ddvo.setAttributeValue("pkdelivorg", getDelivOrgPK());
			ddvo.setAttributeValue("userid", getUserID());
			ddvo.setAttributeValue("username", getUserName());
			ddvo.setAttributeValue("corpid", getCorpID());
			ddvo.setAttributeValue("corpname", getCorpName());
			ddvo.setAttributeValue("BD501", BD501); // 主计量数量小数位数
			ddvo.setAttributeValue("BD502", BD502); // 辅计量数量小数位数
			ddvo.setAttributeValue("BD503", BD503); // 换算率
			ddvo.setAttributeValue("BD505", BD505); // 单价小数位数
			ddvo.setAttributeValue("BD301", BD301); // 金额小数位数
			TestCaculateSendTypeFee dlg = new TestCaculateSendTypeFee(this,
					NCLangRes.getInstance().getStrByID("40140408",
							"UPP40140408-000030")
					/* @res "运费试算" */, ddvo);
			dlg.showModal();
		} catch (Exception e) {
			this.showErrorMessage(e.getMessage());
			handleException(e);
		}
	}

	/**
	 * 承运商确认。 创建日期：(2002-6-17 16:09:05)
	 */
	public void onTransConfirm() {
		try {
			int i;
			m_isOperationFinish = false;
			DelivbillHHeaderVO header = (DelivbillHHeaderVO) m_currentbill
					.getParentVO();
			DelivbillHItemVO[] items = (DelivbillHItemVO[]) m_currentbill
					.getChildrenVO();
			// 设置表体状态
			for (i = 0; i < items.length; i++) {
				items[i].setIrowstatus(new Integer(DelivBillStatus.Confirm));
				items[i].setStatus(nc.vo.pub.VOStatus.UPDATED);
				Object dConfirmArriveDate = items[i]
						.getAttributeValue("confirmarrivedate");
				if (dConfirmArriveDate == null
						|| dConfirmArriveDate.toString().trim().length() == 0) {
					showErrorMessage(NCLangRes.getInstance().getStrByID(
							"40140408", "UPP40140408-000047") /* @res "请输入承运商承诺日期" */);
					return;
				}
			}
			// 设置表头状态
			header.setBconfirm(new UFBoolean(true));
			header.setStatus(nc.vo.pub.VOStatus.UPDATED);

			m_currentbill.setGenOIDPK(getCorpID());
			header.setAttributeValue("userid", getUserID());
			// modified by czp on 2002-10-06 begin
			java.util.ArrayList ary = DelivbillHBO_Client.confirm(
					m_currentbill, null, false, new ClientLink(
							getClientEnvironment()));
			if (null != ary && ary.size() > 0) {
				if (m_currentbill.getParentVO() != null) {
					m_currentbill.getParentVO().setAttributeValue("ts",
							ary.get(ary.size() - 1));
				}
			}
			// modified by czp on 2002-10-06 end
			getBillCardPanel().setBillValueVO(m_currentbill);
			getBillCardPanel().getBillModel().execLoadFormula();
			getBillCardPanel().updateValue();
			switchButtonStatusWithBill();
			m_isOperationFinish = true;
			this.showHintMessage(NCLangRes.getInstance().getStrByID("common",
					"MT9") /* 处理成功 */);
		} catch (Exception e) {
			this.showErrorMessage(e.getMessage());
			handleException(e);
		}
	}

	/**
	 * 创建者：仲瑞庆 功能：压缩条件VO，生成向BS端传入的参数 参数：由查询窗口得到的条件VO 返回：压缩后的条件VO 例外：
	 * 日期：(2001-8-2 下午 3:18) 修改日期，修改人 :晁志平，修改原因 :处理查询条件，注释标志：
	 * 
	 * @return ConditionVO[] 压缩过的查询条件VO数组
	 * @param cvonow
	 *            ConditionVO[],待压缩查询条件数组
	 * @param String
	 *            keys,待压缩查询条件字段名称数组
	 */
	private ConditionVO[] packConditionVO(ConditionVO[] cvonow, String[] keys) {

		boolean bFilterOutDeliOrg = false;
		// 得到途损类型的值
		for (int i = 0; i < cvonow.length; i++) {
			if (cvonow[i].getFieldCode().trim().equals("waylosstype")) {
				if (cvonow[i].getValue().equals("1")) { // "收货方途损"
					bFilterOutDeliOrg = true;
					break;
				}
			}
		}

		// 待压缩字段名称数组串，则“，”分隔
		String strKeys = ",";
		for (int i = 0; i < keys.length; i++) {
			strKeys += keys[i];
			strKeys += ",";
		}
		Vector alcvo = new Vector();
		ConditionVO cvonew = null;
		for (int i = 0; i < cvonow.length; i++) {
			cvonew = (ConditionVO) cvonow[i].clone();
			if (strKeys.indexOf(cvonow[i].getFieldCode().trim()) <= 0) {

				// 如果是收货方途损，则需要过滤掉发运组织
				if (!(bFilterOutDeliOrg && cvonow[i].getFieldCode().trim()
						.equals("dm_delivbill_h.pkdelivorg"))) {
					alcvo.add(cvonew);
				}
			}
		}
		ConditionVO[] cvoFromAlcvo = null;
		cvoFromAlcvo = new nc.vo.pub.query.ConditionVO[alcvo.size()];
		alcvo.copyInto(cvoFromAlcvo);
		return cvoFromAlcvo;
	}

	/**
	 * 切换按钮。 创建日期：(2002-5-17 20:26:04)
	 */
	private void reSetRowNum() {
		boolean bIsNeedReset = true;
		for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
			Object oRowNum = getBillCardPanel().getBillModel().getValueAt(i,
					((DMBillCardPanel) getBillCardPanel()).getRowNumKey());
			if (oRowNum != null && oRowNum.toString().trim().length() != 0) {
				bIsNeedReset = false;
				break;
			}
		}
		if (bIsNeedReset) {
			((DMBillCardPanel) getBillCardPanel()).resetAllRowNo();
		}
	}

	/**
	 * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2002-9-16 22:20:16) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @param newM_bills
	 *            nc.vo.dm.dm104.DelivbillHVO[]
	 */
	public void setArrayBills(nc.vo.dm.dm104.DelivbillHVO[] newM_bills) {
		m_bills = newM_bills;
	}

	/**
	 * 根据参数改变列表界面。 创建日期：(2001-9-27 16:13:57)
	 */
	protected void setListPanelByPara(BillListData bdData) {
		if (bIsForSofee) {
			BillItem[] bodys = bdData.getBodyItems();
			BillItem[] sofeebodys = new BillItem[bodys.length + 2];
			for (int i = 0; i < bodys.length; i++) {
				sofeebodys[i] = bodys[i];
			}

			BillItem billitem = new BillItem();
			billitem.setName("运费");
			billitem.setKey("dfeeitem");
			billitem.setNull(false);
			billitem.setDataType(2);
			billitem.setShow(true);
			billitem.setWidth(150);
			billitem.setEdit(DM005.booleanValue());
			billitem.setEnabled(DM005.booleanValue());
			billitem.setPos(1);
			billitem.setTatol(true);
			sofeebodys[bodys.length] = billitem;

			billitem = new BillItem();
			billitem.setName("开票客户");
			billitem.setKey("pkcustinvoice");
			billitem.setNull(false);
			billitem.setDataType(BillItem.STRING);
			billitem.setShow(false);
			billitem.setWidth(150);
			billitem.setEdit(false);
			billitem.setEnabled(false);
			billitem.setPos(1);
			billitem.setTatol(false);
			sofeebodys[bodys.length + 1] = billitem;

			bdData.setBodyItems(sofeebodys);
		}
		super.setListPanelByPara(bdData);
	}

	/**
	 * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2002-9-16 22:20:16) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @param newM_bills
	 *            nc.vo.dm.dm104.DelivbillHVO[]
	 */
	public void setArrayListBills(ArrayList alImput) {
		if (null != alImput) {
			m_bills = new DelivbillHVO[alImput.size()];
			m_bills = (DelivbillHVO[]) alImput.toArray();
		}
	}

	/**
	 * 将m_currentbill置入界面,并更新
	 */
	protected void setBillVOIntoUI() {
		// Modified by xhq 2002/10/07 begin
		DelivbillHItemVO bodyVO[] = (DelivbillHItemVO[]) m_currentbill
				.getChildrenVO();
		if (bodyVO != null && bodyVO.length > 0) {
			String s[] = new String[] {
					"creceiptcorp->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,creceiptcorpid)",
					"creceiptcorp->getColValue(bd_cubasdoc,custname,pk_cubasdoc,creceiptcorp)",
					"pksrccalbodyar->getColValue(bd_calbody,pk_areacl,pk_calbody,pksendstockorg)" };

			getBillCardPanel().getBillModel().execFormulas(s);
		}
		DelivbillHHeaderVO headVO = (DelivbillHHeaderVO) m_currentbill
				.getParentVO();
		if (headVO != null) {
			UIRefPane refPane = (UIRefPane) getBillCardPanel().getHeadItem(
					"pkdelivroute").getComponent();
			refPane.setText(headVO.getVroutename());
		}
		setInsertExist(false);
		// Modified by xhq 2002/10/07 end

		getBillCardPanel().setBillValueVO(m_currentbill);
		getBillCardPanel().getBillModel().execLoadFormula();
		getBillCardPanel().updateValue();

		getBillCardPanel().getBillValueVO(m_currentbill);
	}

	/**
	 * 设置卡片状态下拉框的显示值
	 */
	private void setCardComboxName(DelivbillHVO bill) {
		// 运费类别
		UIComboBox cbCardItem = (UIComboBox) getBillCardPanel().getHeadItem(
				"isendtype").getComponent();
		Integer sendType = ((DelivbillHHeaderVO) bill.getParentVO())
				.getIsendtype();
		int iSendType = (sendType != null ? sendType.intValue()
				: FreightType.ap);
		cbCardItem.setSelectedIndex(iSendType);
		// cbCardItem.setEnabled(true);
	}

	/**
	 * 返回计算运费对话框。 创建日期：(2002-7-10 14:33:22)
	 * 
	 * @return nc.ui.dm.dm104.CaculateFreight
	 */
	protected void setCaculateTransFeeDlg(CaculateTransFee CaculateTransFeeDlg) {
		m_dlgCaculateTransFee = CaculateTransFeeDlg;
	}

	/**
	 * 由其它原因改变界面。 创建日期：(2001-11-15 9:18:13)
	 * 
	 * @param bdData
	 *            nc.ui.pub.bill.BillData
	 */
	protected void setCardPanelByOther(BillData bdData) {
		try {
			((nc.ui.pub.beans.UIRefPane) bdData.getHeadItem("pkdelivmode")
					.getComponent())
					.setWhereString("where (pk_corp = '"+getCorpPrimaryKey()+"' or pk_corp='0001' or pk_corp is null) and issendarranged = 'Y' ");
			// 到货地址:不用自动检查，返回名称。
			UIRefPane vdestaddress = (UIRefPane) bdData.getBodyItem(
					"vdestaddress").getComponent();
			vdestaddress.setAutoCheck(false);
			vdestaddress.setReturnCode(true);
			bdData.getBodyItem("vdestaddress").setDataType(
					nc.ui.pub.bill.BillItem.USERDEF);

			UIRefPane refPane = (UIRefPane) bdData.getHeadItem("pkdelivroute")
					.getComponent();
			((nc.ui.dm.pub.ref.RouteRefModel) refPane.getRefModel())
					.addWherePart(" and pk_delivorg = '" + getDelivOrgPK()
							+ "' ");
		} catch (Exception e) {
		}

		try {
			// 计划发货时间
			UITextField starttime = (UITextField) ((UIRefPane) bdData
					.getHeadItem("starttime").getComponent()).getUITextField();
			starttime.setTextType(UITextType.TextTime);
			DMTextDocument dmStarttime = new DMTextDocument(starttime);
			starttime.setDocument(dmStarttime);

		} catch (Exception e) {
		}

		try {
			// 计划发货时间
			UITextField returntime = (UITextField) ((UIRefPane) bdData
					.getHeadItem("returntime").getComponent()).getUITextField();
			returntime.setTextType(UITextType.TextTime);
			DMTextDocument dmReturntime = new DMTextDocument(returntime);
			returntime.setDocument(dmReturntime);

		} catch (Exception e) {
		}

		try {
			// 计划发货时间
			UITextField uitfout = (UITextField) ((UIRefPane) bdData
					.getBodyItem("plantime").getComponent()).getUITextField();
			uitfout.setTextType(UITextType.TextTime);
			DMTextDocument dmtdout = new DMTextDocument(uitfout);
			uitfout.setDocument(dmtdout);
		} catch (Exception e) {
		}

		try {
			// 订单要求到货时间
			UITextField uitfout = (UITextField) ((UIRefPane) bdData
					.getBodyItem("orderplantime").getComponent())
					.getUITextField();
			uitfout.setTextType(UITextType.TextTime);
			DMTextDocument dmtdout = new DMTextDocument(uitfout);
			uitfout.setDocument(dmtdout);
		} catch (Exception e) {
		}
		// 置表体出库数量的可编辑性
		if (getDelivSequence() == 0) {
			// 先发运后出库
			bdData.getBodyItem("doutnum").setEdit(false);
		} else if (getDelivSequence() == 1) {
			// 发运出库并行
			bdData.getBodyItem("doutnum").setEdit(true);
		} else if (getDelivSequence() == 2) {
			// 先出库后发运
			bdData.getBodyItem("doutnum").setEdit(false);
		}
	}

	/**
	 * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2002-9-16 22:19:07) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @param newM_currentbill
	 *            nc.vo.dm.dm104.DelivbillHVO
	 */
	public void setCurrentBill(nc.vo.dm.dm104.DelivbillHVO newM_currentbill) {
		m_currentbill = newM_currentbill;
	}

	/**
	 * 返回： 例外： 日期：(2002-11-7 13:35:14) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @param b
	 *            boolean
	 */
	public void setInsertExist(boolean b) {
		m_bInsertExist = b;
	}

	/**
	 * 作者：毕晖 查存货属性 创建日期：(2002-6-11 9:47:30)
	 * 
	 * @param sInv
	 *            java.lang.String[]
	 */
	public void setInvItemEditable(DMBillCardPanel bcp) {

		// 获得界面数据
		DMVO dvo = (DMVO) bcp.getBillValueVO(DMVO.class.getName(),
				DMDataVO.class.getName(), DMDataVO.class.getName());
		DMDataVO[] dmdvos = dvo.getBodyVOs();

		// 获得存货主键、辅计量主键
		String[] invkeys = new String[dmdvos.length];
		String[] astkeys = new String[dmdvos.length];
		for (int i = 0; i < dmdvos.length; i++) {
			invkeys[i] = (String) dmdvos[i].getAttributeValue("pkinv");
			astkeys[i] = (String) dmdvos[i]
					.getAttributeValue("pkassistmeasure");
		}
		// 获得存货信息
		InvVO[] invvos = getInvInfo(invkeys, astkeys);
		// 置存货属性是否可编辑
		for (int i = 0; i < dmdvos.length; i++) {
			// 是否辅计量
			// 辅数量
			Integer isassistunit = (Integer) invvos[i]
					.getAttributeValue("isAstUOMmgt");

			if (isassistunit.intValue() == 1) {
				getHsl(i, null);

				bcp.setCellEditable(i, "vassistmeaname", true);
				bcp.setCellEditable(i, "dinvassist", true);
			} else {
				bcp.setCellEditable(i, "vassistmeaname", false);
				bcp.setCellEditable(i, "dinvassist", false);
			}

			// 修改发运单时批次应为置灰状态不能允许编辑.
			// (需求规定批次只在日计划中可以修改在发运单中只是由日计划带入不可编辑)
			// Integer isbatch= (Integer)
			// invvos[i].getAttributeValue("isLotMgt");
			// bcp.setCellEditable(i, "vbatchcode", (isbatch!=null &&
			// isbatch.intValue() == 1));

			// 自由项
			Integer isfreeitem = (Integer) invvos[i]
					.getAttributeValue("isFreeItemMgt");
			bcp.setCellEditable(i, "vfree0", (isfreeitem != null && isfreeitem
					.intValue() == 1));

			// 上游单据类型
			String sSourceBillType = (String) dmdvos[i]
					.getAttributeValue("vbilltype");
			// if (null == sSourceBillType || sSourceBillType.trim().length() ==
			// 0)
			// sSourceBillType = "Error";
			// sSourceBillType = sSourceBillType.trim();

			// 到货单位
			bcp.setCellEditable(i, "creceiptcorp", sSourceBillType
					.equals(nc.vo.so.pub.SOBillType.SaleOrder));
			// 到货仓库
			bcp.setCellEditable(i, "vdeststorename", sSourceBillType
					.equals(nc.vo.ic.pub.BillTypeConst.m_AllocationOrder));
		}

		// 按承运商设置运输仓和司机参照
		UIRefPane ref = ((UIRefPane) getBillCardPanel().getHeadItem(
				"pktrancust").getComponent());
		String pkinv = ref.getRefPK();

		ref = (UIRefPane) getBillCardPanel().getBodyItem("vcontainername")
				.getComponent();
		if (pkinv != null && pkinv.trim().length() > 0) {
			((nc.ui.dm.pub.ref.ContainerRefModel) ref.getRefModel())
					.addWherePart(" and dm_transcontainer.pktranscust = '"
							+ pkinv + "' ");
		} else {
			String s = " where dm_transcontainer.pkdelivorg = '"
					+ getDelivOrgPK() + "' and dm_transcontainer.dr = 0 ";
			ref.setWhereString(s);
		}

		ref = (UIRefPane) getBillCardPanel().getHeadItem("pkdriver")
				.getComponent();
		if (pkinv != null && pkinv.trim().length() > 0) {
			((nc.ui.dm.pub.ref.DriverRefModel) ref.getRefModel())
					.addWherePart(" and dm_driver.pktrancust = '" + pkinv
							+ "' ");
		} else {
			String s = " where dm_driver.pkdelivorg = '" + getDelivOrgPK()
					+ "' and dm_driver.dr = 0 and dm_driver.bfroze = 'N' ";
			ref.setWhereString(s);
		}
	}

	/**
	 * 置入当前列表行。 功能： 参数： 返回： 例外： 日期：(2002-9-17 22:02:37) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @param i
	 *            int
	 */
	public void setListRow(int i) {
		m_listRow = i;
	}

	/**
	 * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2002-9-4 13:22:20) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @param newTransFeeInputDlg
	 *            nc.ui.dm.dm104.TransFeeInputDlg
	 */
	protected void setPackageListDlg(PackageListDlg newPackageListDlg) {
		packageListDlg = newPackageListDlg;
	}

	/**
	 * 如果是途损引用该节点， 那么在查询条件中有“发运组织”这个条件,但不是必录项
	 */
	public void setQueryForWayLoss(boolean isQueryForWayLoss) {
		this.isQueryForWayLoss = isQueryForWayLoss;
	}

	/**
	 * 显示错误信息
	 */
	public void dispErrorMessage(Throwable ex) {
		SCMEnv.error(ex);
		MessageDialog.showErrorDlg(this, null, NCLangRes.getInstance()
				.getStrByID("4014", "UPP4014-000092"));/* @res程序异常，请查看控制台信息 */
	}

	/**
	 * 显示警告信息
	 */
	public void dispWarningMessage(String msg) {
		SCMEnv.warn(msg);
		MessageDialog.showWarningDlg(this, null, msg);
		showHintMessage(msg);
	}

	// 对于“计算运费”按钮的可用时机，需要根据 参数DM015 和 当前单据状态 进行判断
	public void evaluateBtnCalculateFee() {
		// DelivbillHHeaderVO head
		// =(DelivbillHHeaderVO)getCurrentBill().getParentVO();
		// //如果参数值为不控制：则运费计算按钮在发运单自由、审批状态都可用，发运单审批时不自动计算运费（华孚）
		// if(DM015 == null) {
		// DM015=NCLangRes.getInstance().getStrByID(Constant.DM015_ResPath,
		// Constant.sNoControlSID);
		// }
		//
		// if (DM015.equals(
		// NCLangRes.getInstance().getStrByID(Constant.DM015_ResPath,
		// Constant.sNoControlSID)/*@res "不控制"*/)) {
		// setButton(boCalculateFee, true);
		// }else if(DM015.equals(
		// NCLangRes.getInstance().getStrByID(Constant.DM015_ResPath,
		// Constant.sAfterAuditedSID)/*@res "审批后"*/)) {
		// //如果参数值为审批后：则运费计算按钮只有在发运单审批后才可用。
		// if(StringTools.getSimilarString(head.getPkapprperson()) == null) {
		// setButton(boCalculateFee, false);
		// }else {
		// setButton(boCalculateFee, true);
		// }
		// }else if(DM015.equals(
		// NCLangRes.getInstance().getStrByID(Constant.DM015_ResPath,
		// Constant.sAfterSavedSID)/*@res "保存后"*/)) {
		// //如果参数值为保存后：运费计算按钮显示，用户需手工计算运费.（自由状态有效）
		// if(StringTools.getSimilarString(head.getPkapprperson()) == null) {
		// setButton(boCalculateFee, true);
		// }else {
		// setButton(boCalculateFee, false);
		// }
		// }else { //审批时自动
		// //如果参数值为审批时自动：运费计算按钮不显示
		// }

		DelivbillHHeaderVO head = (DelivbillHHeaderVO) getCurrentBill()
				.getParentVO();
		// 如果参数值为不控制：则运费计算按钮在发运单自由、审批状态都可用，发运单审批时不自动计算运费（华孚）
		if (DM015 == null) {
			DM015 = "不控制";
		}

		if (DM015.equals("不控制")) {
			setButton(boCalculateFee, true);
		} else if (DM015.equals("审批后")) {
			// 如果参数值为审批后：则运费计算按钮只有在发运单审批后才可用。
			if (StringTools.getSimilarString(head.getPkapprperson()) == null) {
				setButton(boCalculateFee, false);
			} else {
				setButton(boCalculateFee, true);
			}
		} else if (DM015.equals("保存后")) {
			// 如果参数值为保存后：运费计算按钮显示，用户需手工计算运费.（自由状态有效）
			if (StringTools.getSimilarString(head.getPkapprperson()) == null) {
				setButton(boCalculateFee, true);
			} else {
				setButton(boCalculateFee, false);
			}
		} else { // 审批时自动
			// 如果参数值为审批时自动：运费计算按钮不显示
		}
	}

	// 对于“行出库打开”按钮的可用时机，需要根据当前单据表头的 状态 进行判断
	public boolean getBtnRowOpenOutState() {
		DelivbillHItemVO[] items = (DelivbillHItemVO[]) m_currentbill
				.getChildrenVO();

		// 如果是整单关闭的情况，则不能进行 行出库打开 操作
		if (items[0] != null && items[0].getIrowstatus() != null
				&& items[0].getIrowstatus().intValue() == DelivBillStatus.End) {
			return false;
		}

		// 如果存在 行出库关闭 的表体行，则 行出库打开 操作可以进行
		for (int i = 0; items != null && i < items.length; i++) {
			if (items[i] != null
					&& StringTools.getSimilarBoolean(items[i].getBcloseout())) {
				return true;
			}
		}

		return false;
	}

	// the next method is not used at present
	// 对于“行出库关闭”按钮的可用时机，需要根据当前单据表头的 状态 进行判断
	public boolean getBtnRowCloseOutState() {
		DelivbillHItemVO[] items = (DelivbillHItemVO[]) m_currentbill
				.getChildrenVO();

		for (int i = 0; items != null && i < items.length; i++) {
			if (items[i] != null
					&& items[i].getIrowstatus() != null
					&& (items[i].getIrowstatus().intValue() == DelivBillStatus.Audit || items[i]
							.getIrowstatus().intValue() == DelivBillStatus.Out)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 切换按钮状态。 创建日期：(2000-8-17 17:00:47)
	 * 
	 * @param status
	 *            int
	 */
	public void switchButtonStatus(int status) {
		if (status == Init) {
			setButton(boFind, false);
			setButton(boSwith, false);
			setButton(boMaintainList, false);
			setButton(boAssistantList, false);
			setButton(boBillCombin, false);
			setButton(boOnhandnum, false);
		} else if (status == AfterQuery) {// 查询后进入列表显示
			if (m_bills != null && m_bills.length != 0) {
				setButton(boFind, true);
				setButton(boSwith, true);
				setButton(boMaintainList, true);
				setButton(boAssistantList, true);
				setButton(boBillCombin, true);
				setButton(boOnhandnum, true);
			} else {
				setButton(boFind, false);
				setButton(boSwith, false);
				setButton(boMaintainList, false);
				setButton(boAssistantList, false);
				setButton(boBillCombin, false);
				setButton(boOnhandnum, false);
			}
		} else if (status == CardBillEditing) { // “新增”后进入表单状态
			setButton(boAdd, false);
			setButton(boEdit, false);
			setButton(boDel, false);
			setButton(boLine, true);
			setButton(boAddLine, true);
			setButton(boDelLine, true);
			setButton(boSave, true);
			setButton(boPackageList, true);
			setButton(boCancel, true);

			setButton(boSwith, false);
			setButton(boPrint, false);
			setButton(boPrintPreview, false);
			setButton(boQuery, false);
			setButton(boRefresh, false);

			setButton(boCalculateFee, false);
			setButton(boTransConfirm, false);
			setButton(boGenTaskBill, false);
			setButton(boCancelConfirm, false);
			setButton(boOutBill, false);
			setButton(boAudit, false);
			setButton(boCancelAudit, false);
			setButton(boRowCloseOut, false);
			setButton(boRowOpenOut, false);
			setButton(boEnd, false);
			setButton(boOpenBill, false);
			setButton(boPre, false);
			setButton(boNext, false);
			setButton(boFirst, false);
			setButton(boLast, false);

		} else if (status == BeforeConfirm) { // 列表初始，“保存”或“取消”后进入表单非编辑状态
			setButton(boAdd, true);
			setButton(boEdit, true);
			setButton(boDel, true);

			setButton(boSave, false);
			setButton(boPackageList, true);
			setButton(boCancel, false);
			setButton(boLine, false);
			setButton(boAddLine, false);
			setButton(boDelLine, false);

			setButton(boSwith, true);
			setButton(boPrint, true);
			setButton(boPrintPreview, true);
			setButton(boQuery, true);
			setButton(boRefresh, true);
			// setButton(boCalculateFee, true);
			evaluateBtnCalculateFee();

			String b = (String) getBillCardPanel().getHeadItem("bconfirm")
					.getValueObject();
			boolean flag = Boolean.valueOf(b).booleanValue();
			if (!flag) {
				setButton(boTransConfirm, true);
				setButton(boCancelConfirm, false);
			} else {
				setButton(boTransConfirm, false);
				setButton(boCancelConfirm, true);
			}

			setButton(boOutBill, false);
			setButton(boGenTaskBill, false);
			setButton(boAudit, !DM001.booleanValue());
			setButton(boCancelAudit, false);
			setButton(boRowCloseOut, false);
			setButton(boRowOpenOut, false);

			setButton(boEnd, false);
			setButton(boOpenBill, false);
			setButton(boPre, true);
			setButton(boNext, true);
			setButton(boFirst, true);
			setButton(boLast, true);
		} else if (status == BeforeApprove) { // 列表初始，审批前
			setButton(boAdd, true);
			setButton(boEdit, true);
			setButton(boDel, true);

			setButton(boSave, false);
			setButton(boPackageList, false);
			setButton(boCancel, false);
			setButton(boLine, false);
			setButton(boAddLine, false);
			setButton(boDelLine, false);

			setButton(boSwith, true);
			setButton(boPrint, true);
			setButton(boPrintPreview, true);
			setButton(boQuery, true);
			setButton(boRefresh, true);
			// setButton(boCalculateFee, true);
			evaluateBtnCalculateFee();

			setButton(boTransConfirm, false);
			setButton(boCancelConfirm, true);
			setButton(boOutBill, false);
			setButton(boGenTaskBill, false);
			setButton(boAudit, true);
			setButton(boCancelAudit, false);
			setButton(boRowCloseOut, false);
			setButton(boRowOpenOut, false);

			setButton(boEnd, false);
			setButton(boOpenBill, false);
			setButton(boPre, true);
			setButton(boNext, true);
			setButton(boFirst, true);
			setButton(boLast, true);
		} else if (status == AfterApproved) { // 列表初始，审批后
			setButton(boAdd, true);
			setButton(boEdit, false);
			setButton(boDel, false);

			setButton(boSave, false);
			setButton(boPackageList, false);
			setButton(boCancel, false);
			setButton(boLine, false);
			setButton(boAddLine, false);
			setButton(boDelLine, false);

			setButton(boSwith, true);
			setButton(boPrint, true);
			setButton(boPrintPreview, true);
			setButton(boQuery, true);
			setButton(boRefresh, true);
			// setButton(boCalculateFee, true);
			evaluateBtnCalculateFee();

			setButton(boTransConfirm, false);
			setButton(boCancelConfirm, false);
			setButton(boOutBill, false);
			setButton(boPre, true);
			setButton(boNext, true);
			setButton(boFirst, true);
			setButton(boLast, true);

			String sTemp = getBillCardPanel().getHeadItem("pktrancust")
					.getValue();
			if (sTemp != null && sTemp.trim().length() > 0) {
				setButton(boGenTaskBill, false);
			} else {
				setButton(boGenTaskBill, true);
			}
			//

			setButton(boAudit, false);
			setButton(boCancelAudit, true);
			setButton(boRowCloseOut, true);

			setButton(boRowOpenOut, getBtnRowOpenOutState());

			setButton(boEnd, true);
			setButton(boOpenBill, false);
		} else if (status == AfterOut) { // 出库后
			setButton(boAdd, true);
			setButton(boEdit, false);
			setButton(boDel, false);

			setButton(boSave, false);
			setButton(boPackageList, true);
			setButton(boCancel, false);
			setButton(boLine, false);
			setButton(boAddLine, false);
			setButton(boDelLine, false);

			setButton(boSwith, true);
			setButton(boPrint, true);
			setButton(boPrintPreview, true);
			setButton(boQuery, true);
			setButton(boRefresh, true);
			// setButton(boCalculateFee, true);
			evaluateBtnCalculateFee();

			setButton(boTransConfirm, false);
			setButton(boCancelConfirm, false);
			setButton(boOutBill, true);
			setButton(boPre, true);
			setButton(boNext, true);
			setButton(boFirst, true);
			setButton(boLast, true);

			String sTemp = getBillCardPanel().getHeadItem("pktrancust")
					.getValue();
			if (sTemp != null && sTemp.trim().length() > 0) {
				setButton(boGenTaskBill, false);
			} else {
				setButton(boGenTaskBill, true);
			}

			setButton(boAudit, false);
			setButton(boCancelAudit, true);
			setButton(boRowCloseOut, true);
			setButton(boRowOpenOut, getBtnRowOpenOutState());

			setButton(boEnd, true);
			setButton(boOpenBill, false);
		} else if (status == BeforeTask) { // 生成任务单前
			setButton(boAdd, true);
			setButton(boEdit, false);
			setButton(boDel, false);

			setButton(boSave, false);
			setButton(boPackageList, true);
			setButton(boCancel, false);
			setButton(boLine, false);
			setButton(boAddLine, false);
			setButton(boDelLine, false);

			setButton(boSwith, true);
			setButton(boPrint, true);
			setButton(boPrintPreview, true);
			setButton(boQuery, true);
			setButton(boRefresh, true);
			// setButton(boCalculateFee, true);
			evaluateBtnCalculateFee();

			setButton(boTransConfirm, false);
			setButton(boCancelConfirm, false);
			setButton(boOutBill, true);
			setButton(boPre, true);
			setButton(boNext, true);
			setButton(boFirst, true);
			setButton(boLast, true);

			String sTemp = getBillCardPanel().getHeadItem("pktrancust")
					.getValue();
			if (sTemp != null && sTemp.trim().length() > 0) {
				setButton(boGenTaskBill, false);
			} else {
				setButton(boGenTaskBill, true);
			}
			//

			setButton(boAudit, false);
			setButton(boCancelAudit, true);
			setButton(boRowCloseOut, true);
			setButton(boRowOpenOut, getBtnRowOpenOutState());

			setButton(boEnd, true);
			setButton(boOpenBill, false);
		} else if (status == AfterTasked) { // 生成任务单后
			setButton(boAdd, true);
			setButton(boEdit, false);
			setButton(boDel, false);

			setButton(boSave, false);
			setButton(boPackageList, true);
			setButton(boCancel, false);
			setButton(boLine, false);
			setButton(boAddLine, false);
			setButton(boDelLine, false);

			setButton(boSwith, true);
			setButton(boPrint, true);
			setButton(boPrintPreview, true);
			setButton(boQuery, true);
			setButton(boRefresh, true);
			// setButton(boCalculateFee, true);
			evaluateBtnCalculateFee();

			setButton(boTransConfirm, false);
			setButton(boCancelConfirm, false);
			setButton(boOutBill, true);
			setButton(boGenTaskBill, false);
			setButton(boAudit, false);
			setButton(boCancelAudit, false);
			setButton(boRowCloseOut, true);
			setButton(boRowOpenOut, getBtnRowOpenOutState());

			setButton(boEnd, true);
			setButton(boOpenBill, false);
			setButton(boPre, true);
			setButton(boNext, true);
			setButton(boFirst, true);
			setButton(boLast, true);
		} else if (status == AfterOut) { // 生成任务单前，出库单后
			setButton(boAdd, true);
			setButton(boEdit, false);
			setButton(boDel, false);

			setButton(boSave, false);
			setButton(boPackageList, true);
			setButton(boCancel, false);
			setButton(boLine, false);
			setButton(boAddLine, false);
			setButton(boDelLine, false);

			setButton(boSwith, true);
			setButton(boPrint, true);
			setButton(boPrintPreview, true);
			setButton(boQuery, true);
			setButton(boRefresh, true);
			// setButton(boCalculateFee, true);
			evaluateBtnCalculateFee();

			setButton(boTransConfirm, false);
			setButton(boCancelConfirm, false);
			setButton(boOutBill, true);
			String sTemp = getBillCardPanel().getHeadItem("pktrancust")
					.getValue();
			if (sTemp != null && sTemp.trim().length() > 0) {
				setButton(boGenTaskBill, false);
			} else {
				setButton(boGenTaskBill, true);
			}
			setButton(boAudit, false);

			setButton(boCancelAudit, false);
			setButton(boRowCloseOut, true);
			setButton(boRowOpenOut, getBtnRowOpenOutState());

			setButton(boEnd, true);
			setButton(boOpenBill, false);
			setButton(boPre, true);
			setButton(boNext, true);
			setButton(boFirst, true);
			setButton(boLast, true);
		} else if (status == AfterClosed) { // 关闭以后
			setButton(boAdd, true);
			setButton(boEdit, false);
			setButton(boDel, false);

			setButton(boSave, false);
			setButton(boPackageList, true);
			setButton(boCancel, false);
			setButton(boLine, false);
			setButton(boAddLine, false);
			setButton(boDelLine, false);

			setButton(boSwith, true);
			setButton(boPrint, true);
			setButton(boPrintPreview, true);
			setButton(boQuery, true);
			setButton(boRefresh, true);
			// setButton(boCalculateFee, true);
			evaluateBtnCalculateFee();

			setButton(boTransConfirm, false);
			setButton(boCancelConfirm, false);
			setButton(boOutBill, false);
			setButton(boGenTaskBill, false);
			setButton(boAudit, false);
			setButton(boCancelAudit, false);
			setButton(boRowCloseOut, false);
			setButton(boRowOpenOut, false);

			setButton(boEnd, false);
			setButton(boOpenBill, true);
			setButton(boPre, true);
			setButton(boNext, true);
			setButton(boFirst, true);
			setButton(boLast, true);
		} else if (status == CardBatchEditing) { // 表单批改状态
			setButton(boAdd, false);
			setButton(boEdit, false);
			setButton(boDel, false);
			setButton(boLine, true);
			setButton(boAddLine, true);
			setButton(boDelLine, true);
			setButton(boSave, true);
			setButton(boPackageList, false);
			setButton(boCancel, true);

			setButton(boSwith, false);
			setButton(boPrint, false);
			setButton(boPrintPreview, false);
			setButton(boQuery, false);
			setButton(boRefresh, false);
			setButton(boCalculateFee, false);
			setButton(boTransConfirm, false);
			setButton(boGenTaskBill, false);
			setButton(boCancelConfirm, false);
			setButton(boOutBill, false);
			setButton(boAudit, false);
			setButton(boCancelAudit, false);
			setButton(boRowCloseOut, false);
			setButton(boRowOpenOut, false);

			setButton(boEnd, false);
			setButton(boOpenBill, false);
			setButton(boPre, true);
			setButton(boNext, true);
			setButton(boFirst, true);
			setButton(boLast, true);
		}
		this.setExtendBtnsStat(status);
	}

	/**
	 * 根据单据状态切换按钮。 创建日期：(2002-6-17 14:40:24)
	 */
	public void switchButtonStatusWithBill() {
		DelivbillHHeaderVO header = (DelivbillHHeaderVO) m_currentbill
				.getParentVO();
		DelivbillHItemVO[] items = (DelivbillHItemVO[]) m_currentbill
				.getChildrenVO();
		int iStatus = 0;

		// 取得行状态
		if (items.length > 0) {
			if (items[0].getIrowstatus() != null) {
				iStatus = items[0].getIrowstatus().intValue();
			}
		}

		if (header.getBconfirm().booleanValue() == false
				&& (StringTools.getSimilarString(header.getPkapprperson()) == null)) {
			// 未确认未审批
			switchButtonStatus(BeforeConfirm);
		} else if (StringTools.getSimilarString(header.getPkapprperson()) == null) {
			// 已确认未审批
			switchButtonStatus(BeforeApprove);
		} else if (iStatus != DelivBillStatus.End
				&& header.getBmissionbill().booleanValue()) {
			// 已审批未关闭可出库
			switchButtonStatus(AfterTasked);
		} else if (iStatus != DelivBillStatus.End
				&& header.getBmissionbill().booleanValue() == false) {
			if (iStatus != DelivBillStatus.Out) {
				// 已审批未关闭未出库
				switchButtonStatus(BeforeTask);
			} else {
				// 已审批已出库
				switchButtonStatus(AfterOut);
			}
		} else {
			// 关闭后
			switchButtonStatus(AfterClosed);
		}
	}

	/**
	 * 更新bills数组全局变量。 创建日期：(2002-5-24 10:02:54)
	 * 
	 * @param currentbill
	 *            nc.vo.dm.dm002.DelivbillHVO
	 */
	private void updateBills(DelivbillHVO currentbill) {
		if (m_bills.length > m_listRow) {
			m_bills[m_listRow] = currentbill;
		}
		if (m_headers.length > m_listRow) {
			m_headers[m_listRow] = (DelivbillHHeaderVO) currentbill
					.getParentVO();

		}
		if (m_bills.length == 0) {
			m_bills = new DelivbillHVO[1];
			m_bills[0] = currentbill;
		}
	}

	/**
	 * 修改单据时合并修改和未修改的行。 创建日期：(2002-5-17 16:10:35)
	 * 
	 * @param hvo
	 *            nc.vo.dm.dm001.DelivorgVO
	 */
	private void updateCurrentBill(DelivbillHVO hvo) {
		DelivbillHHeaderVO header = (DelivbillHHeaderVO) hvo.getParentVO();
		DelivbillHItemVO[] items = (DelivbillHItemVO[]) hvo.getChildrenVO();
		if (header.getStatus() == nc.vo.pub.VOStatus.UPDATED) {
			m_currentbill.setParentVO(header);
		}
		DelivbillHItemVO[] currentitems = (DelivbillHItemVO[]) m_currentbill
				.getChildrenVO();
		int i, j;
		String currentpk;
		int current;
		Vector result = new Vector();
		for (i = 0; i < currentitems.length; i++) {
			current = nc.vo.pub.VOStatus.UNCHANGED;
			currentpk = currentitems[i].getPk_delivbill_b();
			for (j = 0; j < items.length; j++) {
				if (items[j].getPk_delivbill_b() != null
						&& items[j].getPk_delivbill_b().equals(currentpk)) {
					if (items[j].getStatus() == nc.vo.pub.VOStatus.UPDATED) {
						current = nc.vo.pub.VOStatus.UPDATED;
						result.add(items[j]);
					} else if (items[j].getStatus() == nc.vo.pub.VOStatus.DELETED) {
						current = nc.vo.pub.VOStatus.DELETED;
					}
					break;
				}
			}
			if (current == nc.vo.pub.VOStatus.UNCHANGED
					&& currentitems[i].getPk_delivbill_b() != null) {
				result.add(currentitems[i]);
			}

		}
		// 处理新增的item
		for (j = 0; j < items.length; j++) {
			if (items[j].getStatus() == nc.vo.pub.VOStatus.NEW) {
				result.add(items[j]);
			}
		}
		currentitems = new DelivbillHItemVO[result.size()];
		result.copyInto(currentitems);
		m_currentbill.setChildrenVO(currentitems);
	}

	private void updateRouteDesc() {
		try {
			String routedesc = (String) m_currentbill.getParentVO()
					.getAttributeValue("vroutedescr");
			if (routedesc == null || routedesc.trim().length() == 0) {
				String strPKRoute = (String) m_currentbill.getParentVO()
						.getAttributeValue("pkdelivroute");
				routedesc = DelivbillHBO_Client.findRouteDescr(strPKRoute);
				m_currentbill.getParentVO().setAttributeValue("vroutedescr",
						routedesc);
				getBillCardPanel().setHeadItem("vroutedescr", routedesc);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void whenEntered(int row, int col, String key, Object value,
			BillCardPanel bcp) {
		super.whenEntered(row, col, key, value, bcp);

		// Begin: addded by zxping
		// 辅数量
		if (key.equals("dinvassist")) {
			Object oinvassist = bcp.getBodyValueAt(row, "dinvassist"); // 辅数量
			Object odinvnum = bcp.getBodyValueAt(row, "dinvnum"); // 辅数量
			if (oinvassist != null && odinvnum != null
					&& oinvassist.toString().trim().length() > 0
					&& odinvnum.toString().trim().length() > 0) {
				dTransRate = (new Double(odinvnum.toString())).doubleValue()
						/ (new Double(oinvassist.toString())).doubleValue();
			}
			return;
		}
		// End: added by zxping

		else if (key.equals("doutnum")) {
			// 发运组织发运类型为“先发运后出库”，
			// 发运组织发运类型为“先发运后出库”时"已出库数量"应不允许编辑。
			// （只有并行时且不是采购走发运的情况下“已出库数量”才允许编辑）
			String vbilltype = (String) bcp.getBodyValueAt(row, "vbilltype");
			if (getDelivSequence() == 1 && !vbilltype.equals("21")) { // 并行
				// 且不是采购走发运
				bcp.getBodyItem("doutnum").setEnabled(true);
			} else {
				bcp.getBodyItem("doutnum").setEnabled(false);
			}
		}

		// 发运单表体根据销售公司ID重新设置参照
		Object oTemp = bcp.getBodyValueAt(row, "pksalecorp");
		if (oTemp != null && oTemp.toString().trim().length() > 0) {
			String pksalecorp = oTemp.toString();
			UIRefPane refPane = null;
			Object s = null;
			if (key.equals("vdestarea")) {
				// 到货地区
				refPane = (UIRefPane) bcp.getBodyItem("vdestarea")
						.getComponent();

				refPane.getRefModel().setPk_corp(pksalecorp);
				refPane.setPk_corp(pksalecorp);

				refPane.setWhereString(" pk_corp = '" + pksalecorp
						+ "' or pk_corp='0001' or pk_corp is null ");
			}

			else if (key.equals("creceiptcorp")) {
				// 收货单位
				refPane = (UIRefPane) bcp.getBodyItem("creceiptcorp")
						.getComponent();

				refPane.getRefModel().setPk_corp(pksalecorp);
				refPane.setPk_corp(pksalecorp);

				refPane
						.setWhereString(" bd_cumandoc.pk_corp = '"
								+ pksalecorp
								+ "' AND bd_cumandoc.custflag = '0' OR bd_cumandoc.custflag = '2' ");
			}

			else if (key.equals("vdestaddress")) {
				// 到货地址
				refPane = (UIRefPane) bcp.getBodyItem("vdestaddress")
						.getComponent();
				s = bcp.getBodyValueAt(row, "creceiptcorpid");
				if (s != null && s.toString().trim().length() > 0) {
					((nc.ui.scm.ref.prm.CustAddrRefModel) refPane.getRefModel())
							.setCustId(s.toString());
				}
			}

			else if (key.equals("vsendstoreorgname")) {
				// 发货库存组织
				refPane = (UIRefPane) bcp.getBodyItem("vsendstoreorgname")
						.getComponent();

				refPane.getRefModel().setPk_corp(pksalecorp);
				refPane.setPk_corp(pksalecorp);

				String where = " pk_corp = '" + pksalecorp + "' ";
				where += " and property in ( 0,1 ) ";
				refPane.setWhereString(where);
			}

			else if (key.equals("vsendstorename")) {
				// 发货仓库
				refPane = (UIRefPane) bcp.getBodyItem("vsendstorename")
						.getComponent();
				s = bcp.getBodyValueAt(row, "pksendstockorg");
				if (s != null && s.toString().trim().length() > 0) {

					refPane.getRefModel().setPk_corp(pksalecorp);
					refPane.setPk_corp(pksalecorp);
					String where = " pk_calbody = '" + s.toString();
					where += "' and gubflag = 'N' and sealflag ='N' and pk_corp = '";
					where += pksalecorp + "' ";

					refPane.setWhereString(where);
				}
			}
		}

		// 辅单位
		if (key.equals("vassistmeaname")) {
			String sInvID = (String) bcp.getBodyValueAt(row, "pkinv");
			String pksalecorp = (String) bcp.getBodyValueAt(row, "pksalecorp");
			if (null != sInvID) {
				filterMeas(sInvID, key, pksalecorp);
			}
		}

		// 自由项
		else if (key.equals("vfree0")) {
			initBodyFreeItem(row, col, (DMBillCardPanel) bcp);

			// 批次号
		} else if (key.equals("vbatchcode")) {
			initBodyLot(row, col, (DMBillCardPanel) bcp);
		}
	}

	public ButtonObject[] getExtendBtns() {
		return null;
	}

	public void onExtendBtnsClick(ButtonObject bo) {
	}

	public void setExtendBtnsStat(int iState) {

	}

	// 由于需要支持二次开发接口nc.ui.scm.pub.bill.IBillExtendFun(，因此做下面的调整
	// 2005-09-23 v31sp1 钟鸣修改
	public ButtonObject[] getBillButtons() {
		ButtonObject[] buttons = super.getBillButtons();
		ButtonObject[] extraButtons = getExtendBtns();
		if (extraButtons != null && extraButtons.length > 0) {
			int buttonSize = buttons.length;
			int extraSize = extraButtons.length;
			int size = buttonSize + extraSize;

			ButtonObject[] allButtons = new ButtonObject[size];
			System.arraycopy(aryButtonGroup, 0, allButtons, 0, buttonSize);
			System
					.arraycopy(extraButtons, 0, allButtons, buttonSize,
							extraSize);
			buttons = allButtons;
		}
		return buttons;
	}

	public void onDelLine() {
		super.onDelLine();
		this.hasChangedPack = true;
	}

	public void onAddLine() {
		super.onAddLine();
		this.hasChangedPack = true;
	}

	public void onPasteLine() {
		super.onPasteLine();
		this.hasChangedPack = true;
	}

	/**
	 * 关闭窗口的客户端接口。可在本方法内完成窗口关闭前的工作。
	 * 
	 * @return boolean 返回值为true表示允许窗口关闭，返回值为false表示不允许窗口关闭。
	 * 
	 *         创建日期：(2001-8-8 13:52:37)
	 */
	public boolean onClosing() {
		if (getEditFlag() == DMBillStatus.CardNew
				|| getEditFlag() == DMBillStatus.CardEdit) {
			int i = MessageDialog
					.showYesNoCancelDlg(this, null, NCLangRes.getInstance()
							.getStrByID("common", "UCH001") /* 是否保存已修改的数据？ */,
							MessageDialog.ID_CANCEL);
			if (i == MessageDialog.ID_YES) {
				onSave();
				return true;
			} else if (i == MessageDialog.ID_CANCEL) {
				return false;
			} else if (i == MessageDialog.ID_NO) {
				return true;
			}
		}
		return true;
	}

	/**
	 * 对当前单据进行合并显示，并可打印 修改日期，修改人，修改原因，注释标志：
	 */
	private void onBillCombin() {
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40140408",
				"UPP40140408-000215"));// 合并显示
		CollectSettingDlg dlg = new CollectSettingDlg(this, nc.ui.ml.NCLangRes
				.getInstance().getStrByID("40140408", "UPP40140408-000215"));// 合并显示

		BillCardPanel bcp = this.getBillCardPanel();
		dlg.initData(bcp, new String[] { "cinvcode", "cinvname", "cinvspec",
				"cinvtype" }, // 固定分组列
				null, // 缺省分组列
				new String[] { "nnum", "nmny", "nnotaxmny" }, // 求和列
				null,// 求平均列
				new String[] { "nprice", "nnotaxprice" },// 加权平均
				"nnum"// 数量
		);
		dlg.showModal();
	}

	public Object[] getRelaSortObjectArray() {
		return m_bills;
	}

	public int getSortTypeByBillItemKey(String key) {
		if ("irownumber".equals(key)) {
			return nc.ui.pub.bill.BillItem.DECIMAL;
		}
		return getBillCardPanel().getBillModel().getItemByKey(key)
				.getDataType();
	}

	public boolean beforeEdit(BillItemEvent e) { // eric
		// if(getEditFlag() == DMBillStatus.CardEdit){
		if (e.getItem().getKey().equals("pktrancust")) {
			UIRefPane trancust = (UIRefPane) getBillCardPanel().getHeadItem(
					"pktrancust").getComponent();
			Object pkdelivmode = getBillCardPanel().getHeadItem("pkdelivmode")
					.getValueObject();
			Object senddate = getBillCardPanel().getHeadItem("senddate")
					.getValueObject();
			int row = getBillCardPanel().getBillTable().getSelectedRow();
			// if(e.getItem().getValueObject()==null||e.getItem().getValueObject().equals("")){
			if (row < 0) {
				// MessageDialog.showErrorDlg(this, "", "请选择表体行数据才能过滤承运商");
				return false;
			} else {
				Object vsendaddress = getBillCardPanel().getBodyValueAt(row,
						"pksendaddress");
				Object varriveaddress = getBillCardPanel().getBodyValueAt(row,
						"pkarriveaddress");
				trancust
						.setWhereString("  dm_baseprice.pk_sendtype='"
								+ pkdelivmode
								+ "' and pkfromaddress ='"
								+ vsendaddress
								+ "' and pktoaddress='"
								+ varriveaddress
								+ "' and effectdate <= '"
								+ senddate.toString()
								+ "' and expirationdate > '"
								+ senddate.toString()
								+ "' and nvl(dm_baseprice.dr,0)=0 and "
								+ " bd_cumandoc.custflag in ('0','2') and dm_trancust.dr = 0 and bd_cumandoc.pk_corp = '"
								+ ClientEnvironment.getInstance()
										.getCorporation().getPk_corp() + "' ");
			}

		}
		// }
		return false;
	}

	public void onHandnumQuery() {
		DelivbillHHeaderVO header = (DelivbillHHeaderVO) m_currentbill
				.getParentVO();
		DelivbillHItemVO[] items = (DelivbillHItemVO[]) m_currentbill
				.getChildrenVO();
		// ToftPanel toftpanel = SFClientUtil.showNode("40083004",
		// IFuncWindow.WINDOW_TYPE_DLG);
		// nc.ui.ic.ic602.ClientUI ui = (nc.ui.ic.ic602.ClientUI)toftpanel;
		// ui.setGvo(m_currentbill);
		// ui.RefQuery();

		UITable table = this.getBillCardPanel().getBillTable();
		int[] rows = table.getSelectedRows();
		if (rows.length < 1) {
			MessageDialog.showErrorDlg(this, "提示", "请选择表体行数据！");
			return;
		}
		//edit by shikun 2014-11-29  依据表体发货仓库进行数据过滤
//		String[] pkInv = new String[rows.length];
		ArrayList<String> invlist = new ArrayList<String>();
		ArrayList<String> storlist = new ArrayList<String>();
		for (int i = 0; i < rows.length; i++) {
			String pkinv = items[rows[i]].getPkinv()==null?"":items[rows[i]].getPkinv();
			String cwarehouseid = items[rows[i]].getPksendstock()==null?"":items[rows[i]].getPksendstock();
			if (!"".equals(pkinv)&&invlist.indexOf(pkinv)==-1) {
				invlist.add(pkinv);
			}
			if (!"".equals(cwarehouseid)&&storlist.indexOf(cwarehouseid)==-1) {
				storlist.add(cwarehouseid);
			}
//			pkInv[i] = items[rows[i]].getPkinv();
		}
		if (invlist==null||invlist.size()<=0||storlist==null||storlist.size()<=0) {
			showErrorMessage("所选明细行存货或发货仓库为空！");
			return;
		}
//		QueryXcl dialog = new QueryXcl(this, invlist.toArray(new String[0]));
		int result=this.showYesNoMessage("是否只查询选择行发货仓库的可用现存量？");
		if(result==UIDialog.ID_YES){
			QueryXcl dialog = new QueryXcl(this, invlist.toArray(new String[0]), storlist.toArray(new String[0]));
			dialog.showModal();
		}else{
			QueryXcl dialog = new QueryXcl(this, invlist.toArray(new String[0]), null);
			dialog.showModal();
		}
		//end 依据表体发货仓库进行数据过滤
	}
}
