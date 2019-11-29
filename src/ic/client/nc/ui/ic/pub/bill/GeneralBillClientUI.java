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
	//���ݵ��붨��--start--
	public static Workbook w   = null;
    public static int rows=0;
    public static SaleorderBVO[] wbvo = null;
    public static String pk_corp="";
    //���ݵ��붨��--end--
    
	protected String m_sBnoutnumnull = null;

	// ָ����һ��λ
	// protected ButtonObject m_boSelectLocator;

	public static final int CANNOTSIGN = -1; // ����ǩ��

	public static final int NOTSIGNED = 0; // δǩ��//����ǩ��״̬

	public static final int SIGNED = 1; // ��ǩ��

	protected String m_sBillTypeCode = nc.vo.ic.pub.BillTypeConst.m_purchaseIn;

	// �������ͱ���
	protected String m_sTitle = null; // ����

	// protected final String m_sTempletID = "System987679310405"; // ģ��id

	protected final int m_iInitRowCount = 1; // ������ʼ״̬�µ�����

	protected String m_sCurBillOID = null; // ��ǰ�ĵ���id.

	protected int m_iMode = BillMode.Browse; // ��ǰ�ĵ��ݱ༭״̬.

	protected boolean m_bOnhandShowHidden = false; // �Ƿ���ʾ��������

	public String m_sMultiMode = MultiCardMode.CARD_PURE;// �࿨Ƭ״̬

	protected ToftLayoutManager m_layoutManager = new ToftLayoutManager(this);// ���ֹ�����

	protected AfterEditCtrl m_afterEditCtrl = new AfterEditCtrl(this);

	protected int m_iCurPanel = BillMode.Card; // ��ǰ��ʾ��panel.

	// �б���ʽ����
	protected int m_iBillQty = 0; // ��ǰ�б���ʽ�µĵ�������

	protected ArrayList m_alListData = null; // �б�����

	protected int m_iLastSelListHeadRow = -1; // ���ѡ�е��б��ͷ�С�

	protected int m_iCurDispBillNum = -1;

	// ����ʽ�µ�ǰ��ʾ�ĵ�����ţ����б�---�����л�ʱ��δ��ѡ�������ݣ���������������ݣ������Ч�ʡ�
	// vo�������ڶ�ȡ�޸�����ʱ

	// ������VO
	protected GeneralBillVO m_voBill = null;

	protected String m_sNumItemKey = "ninnum"; // ����ʵ�������ֶ���

	protected String m_sAstItemKey = "ninassistnum"; // ���帨�����ֶ���

	protected String m_sNgrossnum = "ningrossnum";// ë��

	// ����Ӧ�������ֶ���
	protected String m_sShouldAstItemKey = "nneedinassistnum";

	// ����Ӧ�������ֶ���
	protected String m_sShouldNumItemKey = "nshouldinnum";

	// ���浱ǰ�������޸ĵĵ��ݵĻ�λ��������...Ҫ��Ҫ�����б���ʽ�����е����ݣ�
	protected ArrayList m_alLocatorData = null;

	protected ArrayList m_alLocatorDataBackup = null;

	// ���ݱ��ݣ�������ʱ���� m_alLocatorData��ȡ��ʱ�ָ�m_alLocatorData.
	// ���浱ǰ�������޸ĵĵ��ݵ����кŷ�������...Ҫ��Ҫ�����б���ʽ�����е����ݣ�
	protected ArrayList m_alSerialData = null;

	protected ArrayList m_alSerialDataBackup = null;

	// ���кŶԻ���
	protected nc.ui.ic.pub.sn.SerialAllocationDlg m_dlgSerialAllocation = null;

	// ��λ�Ի���
	protected nc.ui.ic.pub.locator.SpaceAllocationDlg m_dlgSpaceAllocation = null;

	// ��Ƭ
	protected nc.ui.pub.bill.BillCardPanel ivjBillCardPanel = null;

	// �б�
	protected nc.ui.pub.bill.BillListPanel ivjBillListPanel = null;

	// ���������
	protected FreeItemRefPane ivjFreeItemRefPane = null;

	// ���β���
	protected nc.ui.ic.pub.lot.LotNumbRefPane ivjLotNumbRefPane = null;

	// ���β���
	protected nc.ui.ic.pub.tools.VehicleRefPanel ivjVehicleRefPane = null;

	protected int m_iFirstSelectRow = -1;

	protected int m_iFirstSelectCol = -1;

	protected String m_sCorpID = null; // ��˾ID

	protected String m_sUserID = null; // ��ǰʹ����ID

	protected String m_sLogDate = null; // ��ǰ��¼����

	protected String m_sUserName = null; // ��ǰʹ��������

	// �и���VO
	protected GeneralBillItemVO[] m_voaBillItem = null;

	// ״̬��
	protected javax.swing.JTextField m_tfHintBar = null;

	// ��Ӧ��ⵥ�Ĳ���//��Ӧ���ݲ���
	protected nc.ui.ic.pub.corbillref.ICCorBillRefPane m_aICCorBillRef = null;

	protected InvOnHandDialog m_iohdDlg = null;

	// �������������ʡ�
	protected InvMeasRate m_voInvMeas = new InvMeasRate();

	boolean m_isWhInvRef = false;

	// ��λ����
	private LocatorRefPane ivjLocatorRefPane = null;

	// ��ѯ�Ի���
	protected QueryConditionDlgForBill ivjQueryConditionDlg = null;

	// ����Ա��Ӧ�Ĺ�˾PK,��ʼ��ʱ����
	protected ArrayList m_alUserCorpID = null;

	// added by zhx �������Ƿ�ʹ�ù�ʽ�ı�־;
	protected boolean m_bIsByFormula = true;

	// ���ݺ��Ƿ���������
	protected boolean m_bIsEditBillCode = false;

	// �Ƿ��ڳ�����,ȱʡ���ǡ�
	protected boolean m_bIsInitBill = false;

	// �Ƿ���Ҫ���ݲ���¼��˵���
	protected boolean m_bNeedBillRef = true;

	// �Ƿ���ϵͳtoftpanelȱʡ�Ĵ�����ʾ�Ի���
	protected boolean m_bUserDefaultErrDlg = true;

	// ��ʼ����ӡ�ӿ�
	protected PrintDataInterface m_dataSource = null;

	// ������������ࡣ
	protected nc.ui.ic.pub.device.DevInputCtrl m_dictrl = null;

	/** ��λ�Ի��� */
	protected nc.ui.ic.pub.orient.OrientDialog m_dlgOrient = null;

	// ����ʱ��
	protected nc.vo.pub.lang.UFDateTime m_dTime = null;

	public Hashtable m_htBItemEditFlag = null;

	// add by zhx
	// ��ʼ��ʱ,���浥��ģ���ж���ı�ͷ,�����������Ƿ�ɱ༭, ����ҵ���������������Ƿ�ɱ༭���ж�.
	public Hashtable m_htHItemEditFlag = null;

	// С�����ȶ���--->
	// ����С��λ 2
	// ����������С��λ 2
	// ������ 2
	// ����ɱ�����С��λ 2

	// protected int m_iaScale[] = new int[] { 2, 2, 2, 2, 2 };

	protected ScaleValue m_ScaleValue = new ScaleValue();
	protected ScaleKey m_ScaleKey = new ScaleKey();
	// ���ݳ��������
	protected int m_iBillInOutFlag = InOutFlag.IN;

	// /////////////////////////////////////////
	// ��ʽ������Ҫ�����ȫ�ֱ��� by hanwei 2003-06-26

	private InvoInfoBYFormula m_InvoInfoBYFormula;

	// ///////////////////////////////////////
	// ������
	public PrgBar m_pbProgressBar = null;

	protected nc.ui.pub.print.PrintEntry m_print = null;

	// ��Ŀ����
	protected nc.ui.pub.beans.UIRefPane m_refJob = null;

	// protected nc.ui.bd.b39.JobRefTreeModel m_refJobModel = null;

	// ��Ŀ�׶β���
	protected nc.ui.pub.beans.UIRefPane m_refJobPhase = null;

	protected nc.ui.bd.b39.PhaseRefModel m_refJobPhaseModel = null;

	protected String m_sAstWhItemKey = "cwastewarehouseid";

	// ��ͷ�����ֶ���
	protected final String m_sBillPkItemKey = "cgeneralhid";

	// ���������ֶ���
	protected final String m_sBillRowItemKey = "cgeneralbid";

	// ҵ������ID
	protected final String m_sBusiTypeItemKey = "cbiztype";

	// ��Ӧ���ݺ�
	protected String m_sCorBillCodeItemKey = "ccorrespondcode";

	// �ɱ�����
	protected final String m_sCostObjectItemKey = "ccostobjectname";

	// ��ǰ�ڵ����
	public String m_sCurrentBillNode = "40080602";

	// ����id
	protected String m_sGroupID = null;

	// ��������ֶ��������շ�������
	protected final String m_sInvCodeItemKey = "cinventorycode";

	// ����������ֶ���
	protected final String m_sInvMngIDItemKey = "cinventoryid";

	// �����֯
	protected String m_sMainCalbodyItemKey = "pk_calbody";

	protected String m_sMainCalbodywasteItemKey = "pk_calbodywaste";

	// �ֿ��ֶ�1����������cwarehouseid,cwastewarehouseid��
	protected String m_sMainWhItemKey = "cwarehouseid";

	// ����ƻ�����ֶ���
	protected String m_sPlanMnyItemKey = "nplannedmny";

	// ����ƻ������ֶ���
	protected String m_sPlanPriceItemKey = "nplannedprice";

	// �Ƿ���������Ƶ���
	// protected String m_sRemainOperator = null;

	// �Ƿ���ٵ���ⵥ��־
	// protected String m_sTrackedBillFlag = null;
	// �Ƿ񱣴漴ǩ��
	// protected String m_sSaveAndSign = null;

	// ģ����������
	protected String m_sStartDate = null;

	// �ֿ��ֶ�2��һ��Ӧ�Ƿ�Ʒ��
	protected String m_sWasteWhItemKey = "cwastewarehouseid";

	// ��Ʒ��
	/* ��־�õ����Ƿ�Ϊ����¼�뵥�ݣ�Ĭ��Ϊ���Ƶ��ݣ�* */
	// boolean bIsRefBill = false;
	/* ������Դ���ݲ������ɵĵ���VO* */
	protected GeneralBillVO m_voBillRefInput = null;

	// ���һ�εĲ�ѯ������������ˢ�¡��������б���ʽ�µĴ�ӡ��
	protected QryConditionVO m_voLastQryCond = null;

	// ֧�ֶ��ο����Ĺ�����չ
	protected nc.ui.scm.extend.IFuncExtend m_funcExtend = null;

	// �������ʸı�ʱ���ǹ̶������ʣ�Ĭ���Ǹ����������������䡣�����෴��
	protected boolean m_bAstCalMain = false;

	// ����ǻ����ʴ���afterAstNumEdit����afterNumEdit,��ô��afterNumEdit�оͲ���Ҫ��ȥ����afterAstNumEdit
	protected boolean m_isNeedNumEdit = true;

	// �Ƿ�������ѯ���ı�ʶ
	protected boolean m_bEverQry = false;

	// �ǽ��в�ѯ���ǽ���ˢ�£�Ϊ�˼�����ǰ�İ汾�������Ӹñ���������ʶ���������ͣ�
	protected boolean m_bQuery = true;

	private String[] m_sItemField = null;// ���幫ʽ

	private ClientUISortCtl m_listHeadSortCtl;// �б��ͷ�������
	private ClientUISortCtl m_listBodySortCtl;// �б�����������
	private ClientUISortCtl m_cardBodySortCtl;// ��Ƭ�����������

	// ���ڴӡ�����ע�ᡱ�а��հ�ť��CLASS_NAME���ĳ����ť��ʵ��
	private ButtonTree m_buttonTree;

	/**
	 * ClientUI ������ע�⡣
	 */
	public GeneralBillClientUI() {
		super();
	}

	/**
	 * �����ߣ����˾� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
		// ����
		String sName = ((nc.ui.pub.beans.UIRefPane) itDpt.getComponent()).getRefName();
		// 2003-06-12 zhx add ���ű༭���˺�,��Ҫ���ղ��Ź���ҵ��Ա
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

		// �������������б���ʽ����ʾ��
		if (m_voBill != null) m_voBill.setHeaderValue("cdptname", sName);

	}

	protected void afterCBizidEdit(nc.ui.pub.bill.BillEditEvent e) {
		// ҵ��Ա
		String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("cbizid").getComponent()).getRefName();
		String sPK = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("cbizid").getComponent()).getRefPK();
		// ��Ҫ����ҵ��Ա�Զ���������
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
				// ����
				sDeptName = ((nc.ui.pub.beans.UIRefPane) itDpt.getComponent()).getRefName();
				// 2003-06-12 zhx add ���ű༭���˺�,��Ҫ���ղ��Ź���ҵ��Ա
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
		// �������������б���ʽ����ʾ��
		if (m_voBill != null) {
			m_voBill.setHeaderValue("cbizname", sName);
			m_voBill.setHeaderValue("cdptname", sDeptName);
		}
	}

	/**
	 * �����ߣ����˾� ���ܣ����ݱ༭�¼����� ������e���ݱ༭�¼� ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */

	public void afterEdit(nc.ui.pub.bill.BillEditEvent e) {
		nc.vo.scm.pub.ctrl.GenMsgCtrl.printHint("afterEdit:" + e.getKey());

		// getBillCardPanel().rememberFocusComponent();

		// �У�ѡ�б�ͷ�ֶ�ʱΪ-1
		int row = e.getRow();
		// �ֶ�itemkey
		String sItemKey = e.getKey();
		// �ֶΣ�λ�� 0: head 1:table
		int pos = e.getPos();

		if (pos == nc.ui.pub.bill.BillItem.BODY && row < 0 || sItemKey == null || sItemKey.length() == 0) return;

		// ljun
		// ������ë��Ƥ��
		m_afterEditCtrl.afterEdit(e);

		//��������ݲ�Ҫ�����ó��⡢�ƿ⡢��λ����������������� add by shikun //BillMode.New== m_iMode���������Ǳ༭̬
		if (sItemKey.equals("noutnum")&&getParentCorpCode().equals("10395")&&BillMode.New== m_iMode&&!m_sBillTypeCode.equals("4C")){
			UFDouble num = getWGLnum(row);
			UFDouble noutnum = getBillCardPanel().getBodyValueAt(row, "noutnum")==null? new UFDouble(0.00):new UFDouble(getBillCardPanel().getBodyValueAt(row, "noutnum").toString());
			if (noutnum.doubleValue()!=0&&noutnum.doubleValue()<num.doubleValue()) {//����������Ϊ�գ����ұ�������С����Ч��������ô�����������䣻�����������Ϊ�ջ򱾵�����������Ч��������ȡ��Ч����
				num = noutnum;
			}
			getBillCardPanel().setBodyValueAt(num, row, "noutnum");
		}
		//end shikun 
		
		if (sItemKey.equals("vbillcode")) {
			// ���ݺ�

			// �������������б���ʽ����ʾ��
			if (m_voBill != null) m_voBill.setHeaderValue("vbillcode", getBillCardPanel().getHeadItem("vbillcode").getValueObject());
		} else if (sItemKey.equals("cdispatcherid")) {
			// �շ����
			String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("cdispatcherid").getComponent()).getRefName();
			// �������������б���ʽ����ʾ��
			if (m_voBill != null) m_voBill.setHeaderValue("cdispatchername", sName);
		} else if (sItemKey.equals("cinventoryid")) {
			// �ӹ�Ʒ
			String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("cinventoryid").getComponent()).getRefName();
			// �������������б���ʽ����ʾ��
			if (m_voBill != null) m_voBill.setHeaderValue("cinventoryname", sName);
		} else if (sItemKey.equals(m_sMainWhItemKey))
		// �ֿ�
		afterWhEdit(e);
		else if (sItemKey.equals(m_sMainCalbodyItemKey))
		// �����֯
		afterCalbodyEdit(e);

		else if (sItemKey.equals("cwhsmanagerid")) {
			// ���Ա

			String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("cwhsmanagerid").getComponent()).getRefName();
			// �������������б���ʽ����ʾ��
			if (m_voBill != null) m_voBill.setHeaderValue("cwhsmanagername", sName);
		} else if (sItemKey.equals("cdptid")) {  //����ID
			afterCDptIDEdit(e);
		} else if (sItemKey.equals("cbizid")) {
			// ҵ��Ա
			afterCBizidEdit(e);
		} else if (sItemKey.equals("cproviderid")) {
			// ��Ӧ��
			afterProviderEdit(e);
		} else if (sItemKey.equals("ccustomerid")) {   //�ͻ�ID
			afterCustomerEdit(e);

		} else if (sItemKey.equals("cbiztype")) {
			// ҵ������

			String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("cbiztype").getComponent()).getRefName();
			String sPK = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("cbiztype").getComponent()).getRefPK();
			// �������������б���ʽ����ʾ��
			if (m_voBill != null) m_voBill.setHeaderValue("cbiztypename", sName);
			// ����ҵ�����ʹ���Ĭ�ϵ��շ���� updated by cqw after v2.30
			if (sPK != null) {
				String sReceiptID = execFormular("getColValue(bd_busitype,receipttype,pk_busitype,pk_busitype)", sPK);
				if (sReceiptID != null && sReceiptID.trim().length() > 0) {
					BillItem it = getBillCardPanel().getHeadItem("cdispatcherid");
					if (it != null && it.getValueObject() == null) it.setValue(sReceiptID);
				}
			}
		} else if (sItemKey.equals("cdilivertypeid")) {
			// ���˷�ʽ

			String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("cdilivertypeid").getComponent()).getRefName();
			// �������������б���ʽ����ʾ��
			if (m_voBill != null) m_voBill.setHeaderValue("cdilivertypename", sName);
		} else if (sItemKey.equals("vdiliveraddress")) {
			// ���˵�ַ

			String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("vdiliveraddress").getComponent()).getUITextField().getText();// getRefName();
			// �������������б���ʽ����ʾ��
			if (m_voBill != null) m_voBill.setHeaderValue("vdiliveraddress", sName);

			afterVdiliveraddress(e);

		}

		else if (sItemKey.equals("cotherwhid")) {
			// DW 2005-05-31 �ڸı������ֿ�ʱά�����������֯������˾
			try {
				String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("cotherwhid").getComponent()).getRefName();
				String sCode = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("cotherwhid").getComponent()).getRefPK();
				// ���������⴦�� ���幫˾�Ϳ����֯
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
		// �������ı�
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

		} else // ��Ӧ���ݸı�
		if (sItemKey.equals(m_sCorBillCodeItemKey)) afterCorBillEdit(e);
		else if (sItemKey.equals("vspacename")) afterSpaceEdit(e);

		// ��������
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
		// ʧЧ����
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
		// ��Ŀ
		else if (sItemKey.equals("cprojectname")) {

			String sName = m_refJob.getRefName(); // uiref.getRefName();
			String sPK = m_refJob.getRefPK(); // uiref.getRefPK();
			getBillCardPanel().setBodyValueAt(null, e.getRow(), "cprojectphasename");
			getBillCardPanel().setBodyValueAt(null, e.getRow(), "cprojectphaseid");
			getBillCardPanel().setBodyValueAt(sName, e.getRow(), "cprojectname");
			getBillCardPanel().setBodyValueAt(sPK, e.getRow(), "cprojectid");
			// �������������б���ʽ����ʾ��
			if (m_voBill != null) {
				m_voBill.setItemValue(e.getRow(), "cprojectname", sName);
				m_voBill.setItemValue(e.getRow(), "cprojectid", sPK);

				m_voBill.setItemValue(e.getRow(), "cprojectphasename", null);
				m_voBill.setItemValue(e.getRow(), "cprojectphaseid", null);

			}
		}
		// ��Ŀ�׶�
		else if (sItemKey.equals("cprojectphasename")) {
			// nc.ui.pub.beans.UIRefPane uiref =
			// (nc.ui.pub.beans.UIRefPane) getBillCardPanel()
			// .getBodyItem("cprojectname")
			// .getComponent();

			String sName = m_refJobPhase.getRefName(); // uiref.getRefName();
			String sPK = m_refJobPhase.getRefPK(); // uiref.getRefPK();
			// �������������б���ʽ����ʾ��
			if (m_voBill != null) {
				m_voBill.setItemValue(e.getRow(), "cprojectphasename", sName);
				m_voBill.setItemValue(e.getRow(), "cprojectphaseid", sPK);
				getBillCardPanel().setBodyValueAt(sName, e.getRow(), "cprojectphasename");
				getBillCardPanel().setBodyValueAt(sPK, e.getRow(), "cprojectphaseid");

			}
		} // ���幩Ӧ��
		else if (sItemKey.equals("vvendorname")) {

			String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getBodyItem("vvendorname").getComponent()).getRefName();
			String sPK = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getBodyItem("vvendorname").getComponent()).getRefPK();
			String sPk_cubasdoc = getPk_cubasdoc(sPK);

			getBillCardPanel().setBodyValueAt(sName, e.getRow(), "vvendorname");
			getBillCardPanel().setBodyValueAt(sPK, e.getRow(), "cvendorid");
			getBillCardPanel().setBodyValueAt(sPk_cubasdoc, e.getRow(), "pk_cubasdoc");

			// �������������б���ʽ����ʾ��
			if (m_voBill != null) {
				m_voBill.setItemValue(e.getRow(), "vvendorname", sName);
				m_voBill.setItemValue(e.getRow(), "cvendorid", sPK);
				m_voBill.setItemValue(e.getRow(), "pk_cubasdoc", sPk_cubasdoc);
			}
			// //zhy2005-08-24��afterProviderEdit�е��Ƶ��˴�
			// //����Ǳ���Ĺ�Ӧ�̸ı䣬�������ǳ������ԣ���ôҲҪ������кš�
			// if(m_voBill.getItemVOs()[row].getInOutFlag() == InOutFlag.OUT)
			// clearLocSnData(row,"headprovider");//zhy�˴��ı�������ν����ΪbodyvendorҲ�У�����Ҫ�޸�clearLocSnData�е���Ӧ�ж�

		} else // �ɱ�����
		if (sItemKey.equals("ccostobjectname")) {
			String costobjectname = null;
			String costobjectid = null;
			UIRefPane ref = (UIRefPane) getBillCardPanel().getBodyItem("ccostobjectname").getComponent();
			if (ref != null) {
				costobjectname = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getBodyItem("ccostobjectname").getComponent()).getRefName();
				costobjectid = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getBodyItem("ccostobjectname").getComponent()).getRefPK();
				ref.setPK(null);
			}

			// �������������б���ʽ����ʾ��
			if (m_voBill != null) {
				m_voBill.setItemValue(e.getRow(), "ccostobjectname", costobjectname);
				m_voBill.setItemValue(e.getRow(), "ccostobject", costobjectid);
				getBillCardPanel().setBodyValueAt(costobjectname, e.getRow(), "ccostobjectname");
				getBillCardPanel().setBodyValueAt(costobjectid, e.getRow(), "ccostobject");

			}
			// ((UIRefPane)getBillCardPanel().getBodyItem("ccostobjectname").getComponent()).setPK(null);
		} else // �ջ���λ
		if (sItemKey.equals("creceieveid")) {
			String vrevcustname = null;
			String creceieveid = null;
			if (getBillCardPanel().getBodyItem("vrevcustname").getComponent() != null) {

				vrevcustname = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getBodyItem("vrevcustname").getComponent()).getRefName();
				creceieveid = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getBodyItem("vrevcustname").getComponent()).getRefPK();
			}

			// �������������б���ʽ����ʾ��
			if (m_voBill != null) {
				m_voBill.setItemValue(e.getRow(), "vrevcustname", vrevcustname);
				m_voBill.setItemValue(e.getRow(), "creceieveid", creceieveid);
				getBillCardPanel().setBodyValueAt(vrevcustname, e.getRow(), "vrevcustname");
				getBillCardPanel().setBodyValueAt(creceieveid, e.getRow(), "creceieveid");

			}
		}

		/** �����ʱ༭�� */
		else if (sItemKey.equals("hsl")) afterHslEdit(e);

		// ��ע
		else if (sItemKey.equals("vnotebody")) m_voBill.setItemValue(e.getRow(), "vnotebody", e.getValue());
		// ��Ʒ
		else if (sItemKey.equals("flargess")) {
			UFBoolean ufFlargess = new UFBoolean(e.getValue().toString());
			m_voBill.setItemValue(e.getRow(), "flargess", ufFlargess);
			// �������Ʒ��������ϵĵ��ۺͽ�� add by hanwei 2004-6-24
			if (ufFlargess.booleanValue()) {
				if (getBillCardPanel().getBodyItem("nmny") != null) {
					getBillCardPanel().setBodyValueAt(null, e.getRow(), "nmny");
				}
				if (getBillCardPanel().getBodyItem("nprice") != null) {
					getBillCardPanel().setBodyValueAt(null, e.getRow(), "nprice");
				}
			}
		}
		// ��;
		else if (sItemKey.equals("bonroadflag")) {
			afterOnRoadEdit(e);
		} else if (e.getKey().equals(m_sNumItemKey) || e.getKey().equals(m_sAstItemKey) || e.getKey().equals("hsl") || e.getKey().equals("castunitname")) {
			resetSpace(row);
		} else if (e.getKey().equals(m_sBillRowNo)) {// zhx added for bill
			// row
			// no, after edit process.
			nc.ui.scm.pub.report.BillRowNo.afterEditWhenRowNo(getBillCardPanel(), e, m_sBillTypeCode);
			// ͬ����VO
			m_voBill.setItemValue(row, m_sBillRowNo, getBillCardPanel().getBodyValueAt(row, m_sBillRowNo));

		} else if (sItemKey.startsWith("vuserdef")) {// �Զ������zhy
			if (pos == 0) {// ��ͷ
				String sVdefPkKey = "pk_defdoc" + sItemKey.substring("vuserdef".length());

				// ���ݱ�ͷʹ�ã�afterEditHead(BillData bdata, String sVdefValueKey,
				// String sVdefPkKey)
				DefSetTool.afterEditHead(getBillCardPanel().getBillData(), sItemKey, sVdefPkKey);

				// ͬ��m_voBill
				m_voBill.setHeaderValue(sItemKey, getBillCardPanel().getHeadItem(sItemKey).getValueObject());
				m_voBill.setHeaderValue(sVdefPkKey, getBillCardPanel().getHeadItem(sVdefPkKey).getValueObject());
			} else if (pos == 1) {// ����
				String sVdefPkKey = "pk_defdoc" + sItemKey.substring("vuserdef".length());

				// ���ݱ���ʹ�ã�afterEditBody(BillModel billModel, int iRow,String
				// sVdefValueKey, String sVdefPkKey)
				DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), row, sItemKey, sVdefPkKey);

				// ͬ��m_voBill
				m_voBill.setItemValue(row, sItemKey, getBillCardPanel().getBodyValueAt(row, sItemKey));
				m_voBill.setItemValue(row, sVdefPkKey, getBillCardPanel().getBodyValueAt(row, sVdefPkKey));
			}
		} else if (sItemKey.startsWith(IItemKey.VBCUSER)) {// �������κŵ�����ص��Զ�����
			String sVdefPkKey = IItemKey.PK_DEFDOCBC + sItemKey.substring(IItemKey.VBCUSER.length());

			// ���ݱ���ʹ�ã�afterEditBody(BillModel billModel, int iRow,String
			// sVdefValueKey, String sVdefPkKey)
			DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), row, sItemKey, sVdefPkKey);

			// ͬ��m_voBill
			m_voBill.setItemValue(row, sItemKey, getBillCardPanel().getBodyValueAt(row, sItemKey));
			m_voBill.setItemValue(row, sVdefPkKey, getBillCardPanel().getBodyValueAt(row, sVdefPkKey));
		} else if (sItemKey.equals(IItemKey.CQUALITYLEVELNAME)) {
			String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getBodyItem(IItemKey.CQUALITYLEVELNAME).getComponent()).getRefName();
			String sPK = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getBodyItem(IItemKey.CQUALITYLEVELNAME).getComponent()).getRefPK();
			getBillCardPanel().setBodyValueAt(sName, e.getRow(), IItemKey.CQUALITYLEVELNAME);
			getBillCardPanel().setBodyValueAt(sPK, e.getRow(), IItemKey.CQUALITYLEVELID);
			// �������������б���ʽ����ʾ��
			if (m_voBill != null) {
				m_voBill.setItemValue(e.getRow(), IItemKey.CQUALITYLEVELNAME, sName);
				m_voBill.setItemValue(e.getRow(), IItemKey.CQUALITYLEVELID, sPK);
			}
		} else { // default set id col name:ȱʡ�����ã��༭�˲�����id�е�ֵ
			String sIdColName = null;
			nc.ui.pub.beans.UIRefPane ref = null;
			// �ڱ���
			if (getBillCardPanel().getBodyItem(sItemKey) != null) {

				sIdColName = getBillCardPanel().getBodyItem(sItemKey).getIDColName();
				if (sIdColName != null && getBillCardPanel().getBodyItem(sIdColName) != null && (getBillCardPanel().getBodyItem(sItemKey).getComponent()) != null) {
					ref = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getBodyItem(sItemKey).getComponent());
					// ��pk
					getBillCardPanel().setBodyValueAt(ref.getRefPK(), row, sIdColName);
					// ��ʾname
					getBillCardPanel().setBodyValueAt(ref.getRefName(), row, sItemKey);
					// ͬ��m_voBill
					m_voBill.setItemValue(row, sIdColName, ref.getRefPK());
				} else if (getBillCardPanel().getBodyItem(sItemKey) != null) {
					Object ovalue = getBillCardPanel().getBodyValueAt(row, sItemKey);
					m_voBill.setItemValue(row, sItemKey, ovalue);
				}
			} else if (getBillCardPanel().getHeadItem(sItemKey) != null) {
				// �ڱ�ͷ
				sIdColName = getBillCardPanel().getHeadItem(sItemKey).getIDColName();
				if (sIdColName != null && getBillCardPanel().getHeadItem(sIdColName) != null && getBillCardPanel().getHeadItem(sItemKey).getComponent() != null) {
					ref = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem(sItemKey).getComponent());
					// ��pk
					getBillCardPanel().getHeadItem(sIdColName).setValue(ref.getRefPK());

					// ��ʾname
					getBillCardPanel().getHeadItem(sItemKey).setValue(ref.getRefName());
				}
			}
		}

		// �����Ӧ�л�λ�����к�����
		// zhy2005-08-25��������ⵥ����Ҳ�����˹�Ӧ�̣�����ڴ˴������λ���к�ʱ��Ҫ�ж���������
		if (!(sItemKey.equals("vvendorname") && m_voBill.getItemVOs()[row].getInOutFlag() != InOutFlag.OUT)) clearLocSnData(row, sItemKey);

		//
		if (e.getKey().equals(m_sNumItemKey) || e.getKey().equals(m_sAstItemKey) || e.getKey().equals("hsl") || e.getKey().equals("castunitname") || e.getKey().equals("vbatchcode") || e.getKey().equals(m_sNgrossnum) || e.getKey().equals("ntarenum")) {
			resetSpace(row);
		}
		// zhx added for bill row no, after edit process.
		if (e.getKey().equals(m_sBillRowNo)) {
			nc.ui.scm.pub.report.BillRowNo.afterEditWhenRowNo(getBillCardPanel(), e, m_sBillTypeCode);
			// ͬ����VO
			m_voBill.setItemValue(row, m_sBillRowNo, getBillCardPanel().getBodyValueAt(row, m_sBillRowNo));

		}
		// �����෽��
		afterBillEdit(e);
		// getBillCardPanel().restoreFocusComponent();

//			if(sItemKey.equals("cwarehouseid")&&!m_sBillTypeCode.equals("4Q")/*��λ�����Ĳֿ���е�������*/){//�ֿ�
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
//						obj = query.executeQuery(str.toString(), alp);//ִ�ò�ѯ
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
			if (("vfree1".equals(sItemKey) || "vfree0".equals(sItemKey))&&!m_sBillTypeCode.equals("4Q")/*��λ�����ĵ����Ž��е�������*/) {
			//��ò����еĵ�����
			String vfree = getBillCardPanel().getBodyValueAt(row, "vfree0") == null ? "" : getBillCardPanel().getBodyValueAt(row, "vfree0").toString();
			String Pathcoed = getvfree1(vfree);
			if("".equals(Pathcoed)){
				Pathcoed = getBillCardPanel().getBodyValueAt(row, "vfree1") == null ? "" : getBillCardPanel().getBodyValueAt(row, "vfree1").toString();
			}
			//��ȡ��ͷ�ֶβֿ��ֶ�
			String cwarehouseid = getBillCardPanel().getHeadItem("cwarehouseid").getValueObject()==null?"":getBillCardPanel().getHeadItem("cwarehouseid").getValueObject().toString();
			//��ò����еĴ������������
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
//			if (e.getKey().equals("cinventorycode")&&!m_sBillTypeCode.equals("4Q")/*��λ�����Ĵ��������е�������*/) {
//			Object cwarehouseid = getBillCardPanel().getHeadItem("cwarehouseid").getValueObject();//��ȡ��ͷ�ֶβֿ��ֶ�
//			Object cinventoryid = getBillCardPanel().getBodyValueAt(row, "cinventoryid");//��ò����еĴ������������
//			if (cwarehouseid==null||cinventoryid==null) {
//				getBillCardPanel().getBillModel().setValueAt(null, row, "noutnum");
//				getBillCardPanel().getBillModel().setValueAt(null, row, "cspaceid");
//				return;
//			}
//			StringBuffer str = new StringBuffer("");//����һ���ַ������棬����װSQL���
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
//			IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());//ʹ��IUAPQueryBS������������ò�ѯ����
//			MapListProcessor alp = new MapListProcessor();//���ϴ�����
//			Object obj = null;//����һ���������ڴ��query.executeQuery()�Ĳ�ѯ���
//			try {
//				obj = query.executeQuery(str.toString(), alp);//ִ�ò�ѯ
//			} catch (BusinessException e1) {
//				e1.printStackTrace();
//			}
//			ArrayList addrList = (ArrayList) obj;//����ѯ���ת���������б�����addList��
//			if (addrList != null && addrList.size() > 0) {
//				Map addrMap = (Map) addrList.get(0);//ȡaddrList�����еĵ�0�е�ֵ������addrMap��
//				UFDouble addrName = new UFDouble(addrMap.get("wglsl").toString());//��addName���addMap��anum������ֵ
//				String cspaceid = addrMap.get("cspaceid").toString();//��cspaceid���addMap��cspaceid��λ��ֵ
//				getBillCardPanel().getBillModel().setValueAt(addrName, row, "noutnum");//��װ��������ֵ����noutnum�ֶ�
//				getBillCardPanel().getBillModel().setValueAt(cspaceid, row, "cspaceid");//��װ�л�λ��ֵ����cspaceid�ֶ�
//			}
//			getBillCardPanel().getBillModel().execLoadFormula();//ѡ�����ϵ�����е�ģ�ͣ�ִ�й�ʽ
//		} 
		
	}

	/**
	 * ���ݵ����Ż�ȡ��Ӧ�Ļ�λ�����κš�������ȥ���������������Ե�ǰ�����н��и�ֵ 
	 * add by shikun 2014-04-12 
	 * @throws BusinessException 
	 * */
	@SuppressWarnings("unchecked")   //���κ�
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
					showErrorMessage("���������Ѿ�Ϊ0����ע�⣡");
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
				if (noutnum.doubleValue()!=0&&noutnum.doubleValue()<wglsl.doubleValue()) {//����������Ϊ�գ����ұ�������С����Ч��������ô�����������䣻�����������Ϊ�ջ򱾵�����������Ч��������ȡ��Ч����
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
	 * ���ݵ����Ų�ѯ��Ӧ�Ļ�λ�����κš����������ȥ��������������ѯ��䡣
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
	 * �����ߣ����˾� ���ܣ�������ı��¼����� ������e���ݱ༭�¼� ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
			// �����ص�����
			// �����ص�����
			if (!BillTypeConst.m_purchaseIn.equals(this.m_sBillTypeCode)) clearRowData(0, e.getRow(), "vfree0");

			clearRowData(0, e.getRow(), "vfree0");
			execEditFomulas(e.getRow(), e.getKey());

		} catch (Exception e2) {
			nc.vo.scm.pub.SCMEnv.error(e2);
		}

	}

	/**
	 * �����ߣ����˾� ���ܣ��ֿ�ı��¼����� ������e���ݱ༭�¼� ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	public void afterWhEdit(nc.ui.pub.bill.BillEditEvent e) {
		// �ֿ�
		afterWhEdit(e, null, null);

	}

	protected void setLastHeadRow(int row) {
		m_iLastSelListHeadRow = row;
	}

	/**
	 * �����ߣ����˾� ���ܣ������塢�б��ϱ�༭�¼����� ������e ���ݱ༭�¼� ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	@SuppressWarnings("restriction")
	public void bodyRowChange(nc.ui.pub.bill.BillEditEvent e) {
		// �б���ʽ�µı�ͷ��ѡ��
		getBillCardPanel().rememberFocusComponent();
		int row = e.getRow();
		if (e.getSource() == getBillListPanel().getHeadTable()) {

			if (row < 0 || m_alListData == null && row >= m_alListData.size()) {
				SCMEnv.out(" row now ERR ");
				return;
			}
			// ���δ�ı����򷵻�
			if (m_iLastSelListHeadRow == row) return;
			// ���޹�����
			m_alLocatorData = null;
			m_alSerialData = null;
			// �ñ�����������Ϊ��
			m_sLastKey = null;

			// SCMEnv.out(" Line changed to " + row);
			// �ı��Ӧ�ı�����ʾ
			setLastHeadRow(row);
			selectListBill(m_iLastSelListHeadRow);
			// �����λ
			// clearOrientColor();
			// setBtnStatusSN(0);
			if (m_funcExtend != null) {
				// ֧�ֹ�����չ
				m_funcExtend.rowchange(this, getBillCardPanel(), getBillListPanel(), nc.ui.scm.extend.IFuncExtend.LIST, nc.ui.pub.bill.BillItem.HEAD);
			}
		} else if (e.getSource() == getBillListPanel().getBodyTable()) {
			setBtnStatusSN(row);
			if (m_funcExtend != null) {
				// ֧�ֹ�����չ
				m_funcExtend.rowchange(this, getBillCardPanel(), getBillListPanel(), nc.ui.scm.extend.IFuncExtend.LIST, nc.ui.pub.bill.BillItem.BODY);
			}
		}

		// ����ʽ�µ��޸Ļ�ѡ�С�

		else if (e.getSource() == this.getBillCardPanel().getBillTable()) {
			if (row < 0) return;
			SCMEnv.out("line to " + e.getRow());
			setBtnStatusSN(row);
			setTailValue(row);
			if (m_funcExtend != null) {
				// ֧�ֹ�����չ
				m_funcExtend.rowchange(this, getBillCardPanel(), getBillListPanel(), nc.ui.scm.extend.IFuncExtend.CARD, nc.ui.pub.bill.BillItem.BODY);
			}

			// v5 lj ����������Ŀ��ɫ��
			getFreeItemCellRender().setRenderer("vfree0");
			getLotRefCellRender().setRenderer("vbatchcode");

			// ˢ���ִ���Panel��ʾ
			if (m_bOnhandShowHidden) {
				showOnHandPnlInfo(e.getRow());
			}
			// ��ʾ
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
	// ���л�ʱ�ִ�����ʾ��ڲ���

	// /**
	// * ��ʾ�ִ������յ�����
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
	 * �����ߣ����˾� ���ܣ����ָ���е����� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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

		// ɾ����������
		// ɾ����������
		String sColKey = null;
		int iColCount = items.length;

		for (int col = 0; col < iColCount; col++) {

			sColKey = items[col].getKey();
			if (!m_sBillPkItemKey.equals(sColKey) && !m_sBillRowItemKey.equals(sColKey) && !"crowno".equals(sColKey)) bmBill.setValueAt(null, row, col);
		}
		// ͬ��vo
		m_voBill.clearItem(row);

	}

	/**
	 * �����ߣ����˾� ���ܣ����ָ���С�ָ���е����� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
		// ɾ����������
		nc.ui.pub.bill.BillItem biaBody[] = bmBill.getBodyItems();
		Hashtable<String, String> htBodyItem = new Hashtable<String, String>();
		// ���зŵ�hash��
		for (int col = 0; col < biaBody.length; col++)
			htBodyItem.put(biaBody[col].getKey(), "OK");

		for (int col = 0; col < saColKey.length; col++)
			if (saColKey[col] != null && getBillCardPanel().getBodyItem(saColKey[col]) != null) {
				try {
					// SCMEnv.out("clear "+saColKey[col]);
					try {
						// ����У����֮
						if (htBodyItem.containsKey(saColKey[col])) bmBill.setValueAt(null, row, saColKey[col]);
					} catch (Exception e3) {
					}

					// ͬ��vo
					m_voBill.setItemValue(row, saColKey[col], null);
					// �����������Ļ���ͬʱ��vfree1--->vfree10
					if (saColKey[col].equals("vfree0")) {
						for (int i = 1; i <= 10; i++) {
							// ����У����֮
							if (htBodyItem.contains(saColKey[col])) bmBill.setValueAt(null, row, "vfree" + i);
							// ͬ��vo
							m_voBill.setItemValue(row, "vfree" + i, null);
						}
					}
					// ��afterEdit�е���clearLocSN����locator/sn
					// //���λ������
					// if (saColKey[col].trim().equals("locator")) {
					// if (m_alLocatorData != null && m_alLocatorData.size() >
					// row)
					// m_alLocatorData.set(row, null);
					// m_voBill.getItemVOs()[row].setLocator(null);
					// }
					// //�����кŷ�����
					// if (saColKey[col].trim().equals("sn")) {
					// if (m_alSerialData != null && m_alSerialData.size() >
					// row)
					// m_alSerialData.set(row, null);
					// m_voBill.getItemVOs()[row].setSerial(null);
					// }

				} catch (Exception e) {
					// nc.vo.scm.pub.SCMEnv.error(e);
					SCMEnv.out("nc.ui.ic.pub.bill.GeneralBillClientUI.clearRowData(int, String [])��set value ERR.--->" + saColKey[col]);
				} finally {

				}
			}

	}

	/**
	 * �����ߣ����˾� ���ܣ���ȥ����ʽ�µĿ��У���ѯ ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2001/10/29,wnj,���Ч��
	 * 
	 * 
	 * 
	 */
	public void filterNullLine() {

		// �����ֵ�ݴ�
		Object oTempValue = null;
		// ����model
		nc.ui.pub.bill.BillModel bmBill = getBillCardPanel().getBillModel();
		// ����кţ�Ч�ʸ�һЩ��
		int iInvCol = bmBill.getBodyColByKey(m_sInvMngIDItemKey);

		// �����д����
		if (bmBill != null && iInvCol >= 0 && iInvCol < bmBill.getColumnCount()) {
			// ����
			int iRowCount = getBillCardPanel().getRowCount();
			// �Ӻ���ǰɾ
			for (int line = iRowCount - 1; line >= 0; line--) {
				// ���δ��
				oTempValue = bmBill.getValueAt(line, iInvCol);
				if (oTempValue == null || oTempValue.toString().trim().length() == 0)
				// ɾ��
				getBillCardPanel().getBillModel().delLine(new int[] {
					line
				});
			}
		}
	}

	/**
	 * ���� BillCardPanel1 ����ֵ��
	 * 
	 * @return nc.ui.pub.bill.BillCardPanel 1.
	 *         �½�һ��billcardpanel,�õ�templetData�����ݸ�BillData 2.
	 *         ������������κŲ��տؼ�����BillData���滻ԭComponet������Ŀ����Ҳ�� 3. ���˱��幩Ӧ�� 4.
	 *         ���ñ���m_sTitle���Զ����� 5.
	 *         ��BillData������billCardPanel,���������кţ�����billCardPanel
	 */
	/* ���棺�˷������������ɡ� */
	@SuppressWarnings("restriction")
	protected nc.ui.pub.bill.BillCardPanel getBillCardPanel() {
		if (ivjBillCardPanel == null) {
			try {
				ivjBillCardPanel = new nc.ui.pub.bill.BillCardPanel();
				ivjBillCardPanel.setName("BillCardPanel");
				// user code begin {1}
				// nc.vo.pub.bill.BillTempletVO btv =
				// ����ģ������
				// ivjBillCardPanel.loadTemplet(m_sBillTypeCode, null,
				// m_sUserID, m_sCorpID);
				// BillData bd = ivjBillCardPanel.getBillData();
				BillData bd = new BillData(ivjBillCardPanel.getTempletData(m_sBillTypeCode, null, m_sUserID, m_sCorpID));

				if (bd == null) {
					SCMEnv.out("--> billdata null.");
					return ivjBillCardPanel;
				}
				// ���������
				if (bd.getBodyItem("vfree0") != null) getFreeItemRefPane().setMaxLength(bd.getBodyItem("vfree0").getLength());

				// zhy 2005-04-13 ��ͷ���β���
				if (bd.getHeadItem("vbatchcode") != null) {
					bd.getHeadItem("vbatchcode").setComponent(getLotNumbRefPane());
				}
				// zhy 2005-04-13 ��ͷ������
				if (bd.getHeadItem("vfree0") != null) {
					bd.getHeadItem("vfree0").setComponent(getFreeItemRefPane());
				}

				// ���ڳ��������κŲ���
				// zhy2006-04-18 ���ڼ������κŵ���,�ʲ����ڳ��������⴦��
				// if (!m_bIsInitBill) {
				if (bd.getBodyItem("vbatchcode") != null) getLotNumbRefPane().setMaxLength(bd.getBodyItem("vbatchcode").getLength());
				if (bd.getBodyItem("vbatchcode") != null) bd.getBodyItem("vbatchcode").setComponent(getLotNumbRefPane());
				// }
				if (bd.getBodyItem("vvehiclecode") != null) bd.getBodyItem("vvehiclecode").setComponent(getVehicleRefPane());

				// ����������ռ��뵥��ģ�����
				if (bd.getBodyItem("vfree0") != null) bd.getBodyItem("vfree0").setComponent(getFreeItemRefPane()); // ����������ռ��뵥��ģ�����
				// ��Ŀ����
				m_refJob = new nc.ui.pub.beans.UIRefPane();
				m_refJob.setRefNodeName("��Ŀ������");
				m_refJob.getRefModel().setPk_corp(m_sCorpID);
				// m_refJob.setIsCustomDefined(true);
				// m_refJob.setRefType(2);
				// m_refJobModel = new nc.ui.bd.b39.JobRefTreeModel(m_sGroupID,
				// m_sCorpID, null);
				// m_refJob.setRefModel(m_refJobModel);

				if (bd.getBodyItem("cprojectname") != null) bd.getBodyItem("cprojectname").setComponent(m_refJob);

				// ��Ŀ�׶β���
				m_refJobPhase = new nc.ui.pub.beans.UIRefPane();
				m_refJobPhase.setIsCustomDefined(true);
				try {
					m_refJobPhaseModel = new nc.ui.bd.b39.PhaseRefModel();
				} catch (Exception e) {

				}
				m_refJobPhase.setRefModel(m_refJobPhaseModel);
				if (bd.getBodyItem("cprojectphasename") != null) bd.getBodyItem("cprojectphasename").setComponent(m_refJobPhase);

				// ���ⵥ���幩Ӧ��
				if (bd.getBodyItem("vvendorname") != null) RefFilter.filtProvider(bd.getBodyItem("vvendorname"), m_sCorpID, null);
				if (bd.getBodyItem("vspacename") != null) bd.getBodyItem("vspacename").setComponent(getLocatorRefPane()); //

				// �޸��Զ�����

				bd = changeBillDataByUserDef(getDefHeadVO(), getDefBodyVO(), bd);
				ivjBillCardPanel.setBillData(bd);

				bd = BatchCodeDefSetTool.changeBillDataByBCUserDef(m_sCorpID, bd);
				ivjBillCardPanel.setBillData(bd);

				if (bd.getHeadItem("cbilltypecode") != null) m_sTitle = bd.getHeadItem("cbilltypecode").getName();
				if (ivjBillCardPanel.getTitle() != null && ivjBillCardPanel.getTitle().trim().length() > 0) m_sTitle = ivjBillCardPanel.getTitle();
				// zhx new add billrowno
				nc.ui.scm.pub.report.BillRowNo.loadRowNoItem(ivjBillCardPanel, m_sBillRowNo);
				// ��ԭ����ģ��ı���������!
				ivjBillCardPanel.getBodyPanel().setRowNOShow(nc.ui.ic.pub.bill.Setup.bShowBillRowNo);

				// �����ȼ���������
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
						ref.setRefNodeName("�Զ������");
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
	 * ���� BillListPanel1 ����ֵ��
	 * 
	 * @return nc.ui.pub.bill.BillListPanel 1.�½�billListPanel������ģ������
	 *         2.�õ�ģ������BillListData���޸��Զ������������ 3.���ñ�ͷѡ��ģʽ��������listid��ͷ�ı�ͷ��
	 *         4.��ʾ���б����У�����billListPanel
	 */
	/* ���棺�˷������������ɡ� */
	protected nc.ui.pub.bill.BillListPanel getBillListPanel() {
		if (ivjBillListPanel == null) {
			try {
				ivjBillListPanel = new nc.ui.pub.bill.BillListPanel();
				ivjBillListPanel.setName("BillListPanel");
				// user code begin {1}
				// ivjBillListPanel.loadTemplet(m_sTempletID);
				// �����б�ģ��
				ivjBillListPanel.loadTemplet(m_sBillTypeCode, null, m_sUserID, m_sCorpID);

				BillListData bd = ivjBillListPanel.getBillListData();
				// �޸��Զ�����

				bd = changeBillListDataByUserDef(getDefHeadVO(), getDefBodyVO(), bd);

				bd = BatchCodeDefSetTool.changeBillListDataByBCUserDef(m_sCorpID, bd);

				ivjBillListPanel.setListData(bd);

				// ����С������
				// setScaleOfListPanel();

				ivjBillListPanel.getHeadTable().setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				// �˵�listid����
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
	 * ���� FreeItemRefPane1 ����ֵ��
	 * 
	 * @return nc.ui.ic.pub.freeitem.FreeItemRefPane
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� LotNumbRefPane1 ����ֵ��
	 * 
	 * @return nc.ui.ic.pub.lot.LotNumbRefPane
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� SerialAllocationDlg1 ����ֵ��
	 * 
	 * @return nc.ui.ic.pub.sn.SerialAllocationDlg
	 */
	/* ���棺�˷������������ɡ� */
	@SuppressWarnings("restriction")
	protected nc.ui.ic.pub.sn.SerialAllocationDlg getSerialAllocationDlg() {
		if (m_dlgSerialAllocation == null) {
			try {
				m_dlgSerialAllocation = new nc.ui.ic.pub.sn.SerialAllocationDlg(this);
				m_dlgSerialAllocation.setName("SerialAllocationDlg");
				// m_dlgSerialAllocation.setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
				// user code begin {1}
				// m_dlgSerialAllocation.setParent(this);
				// С����������
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
	 * ���� SpaceAllocationDlg1 ����ֵ��
	 * 
	 * @return nc.ui.ic.pub.locator.SpaceAllocationDlg
	 */
	/* ���棺�˷������������ɡ� */
	@SuppressWarnings("restriction")
	protected nc.ui.ic.pub.locator.SpaceAllocationDlg getSpaceAllocationDlg() {
		if (m_dlgSpaceAllocation == null) {
			try {
				m_dlgSpaceAllocation = new nc.ui.ic.pub.locator.SpaceAllocationDlg(this);
				m_dlgSpaceAllocation.setName("SpaceAllocationDlg");
				// m_dlgSpaceAllocation.setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
				// user code begin {1}
				// С����������
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
	 * ����ʵ�ָ÷���������ҵ�����ı��⡣
	 * 
	 * @version (00-6-6 13:33:25)
	 * 
	 * @return java.lang.String
	 */
	public String getTitle() {
		return m_sTitle;
	}

	/**
	 * �����ߣ����˾� ���ܣ����زֿ�����ֻ�����ڳ���ⵥ�����ⵥҪ���ֳ�/��ֿ�itemkey ������ ���أ� ���⣺ ���ڣ�(2001-5-9
	 * 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
	 * ÿ�������׳��쳣ʱ������
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 */
	protected void handleException(java.lang.Throwable exception) {

		/* ��ȥ���и��е�ע�ͣ��Խ�δ��׽�����쳣��ӡ�� stdout�� */
		SCMEnv.out("--------- δ��׽�����쳣 ---------");
		nc.vo.scm.pub.SCMEnv.error(exception);
	}

	/**
	 * ��ʼ���ࡣ
	 */
	/* ���棺�˷������������ɡ� */
	public void initialize() {
		m_timer.start("��ʼ���࿪ʼ��");
		try {
			// �õ����������������浽��Ա������
			m_timer.showExecuteTime("getCEnvInfo()��");
			getCEnvInfo();

			m_timer.showExecuteTime("��ʼ����ʼ��");
			// initButtons();
			m_timer.showExecuteTime("initButtons��");
			// ydy 04-05-12 �򻯴��룬����
			initialize(m_sCorpID, m_sUserID, m_sUserName, null, m_sGroupID, m_sLogDate);

			// getBillCardPanelSum().setVisible(true);

		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}

	}

	/**
	 * ɾ���б��µ�һ�ŵ���
	 * 
	 * @author ljun
	 * @since v5 ����ʱ��һ�� ���ն��ŵ�������ʱ����Ƭ�����±�����Զ�ת���б����ʱ��ɾ������ĵ���
	 * 
	 */
	protected void delBillOfList(int iSel) {
		if (iSel < 0) return;
		if (m_alListData == null) return;
		// ���ɾ����m_alListData.size()==0 , ��m_iLastSelListHeadRowΪ-1
		m_alListData.remove(iSel);

		// m_iCurDispBillNum��ɾ����Ϊ��һ���б���
		if (BillMode.Card == m_iCurPanel) m_iCurDispBillNum = -1;
		else m_iCurDispBillNum = 0;

		if (m_alListData.size() == 0) setLastHeadRow(-1);
		else setLastHeadRow(0);

		m_iBillQty = m_alListData.size();

		// ˢ�½�����ʾ
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
			// ѡ���б��� ����ˢ�±�����ʾ
			selectListBill(m_iLastSelListHeadRow);
		}

	}

	/**
	 * �����ߣ����˾� ���ܣ��ѱ���ʽ�µĵ��ݲ��뵽�б���������������¼�롢���Ʊ����
	 */
	@SuppressWarnings("unchecked")
	protected void insertBillToList(GeneralBillVO voBill) {
		if (voBill == null || voBill.getParentVO() == null || voBill.getChildrenVO() == null) {
			SCMEnv.out("Bill is null !");
			return;
		}

		// ��ǰû�е���
		if (m_alListData == null) m_alListData = new ArrayList();
		// ��һ�������һ����׷�ӵ���
		// if (m_iLastSelListHeadRow < 0 || m_iLastSelListHeadRow == m_iBillQty
		// - 1)
		// ..................ע��clone()...........................
		// �������㻻����
		voBill.setHaveCalConvRate(true);
		// ------------------
		m_alListData.add(voBill.clone());
		// else //����
		// m_alListData.add(m_iLastSelListHeadRow + 1, voBill.clone());

		m_iBillQty = m_alListData.size();

		// ѡ�е��������С�
		setLastHeadRow(m_iBillQty - 1);

		// ����ʽ�µ�ǰ��ʾ�ĵ�����ţ����б�---�����л�ʱ��δ��ѡ�������ݣ���������������ݣ������Ч�ʡ�
		if (BillMode.Card == m_iCurPanel) m_iCurDispBillNum = m_iLastSelListHeadRow;
		else m_iCurDispBillNum = 0;
		// ˢ�½�����ʾ
		GeneralBillHeaderVO voaBillHeader[] = new GeneralBillHeaderVO[m_alListData.size()];
		for (int i = 0; i < m_alListData.size(); i++) {
			if (m_alListData.get(i) != null) voaBillHeader[i] = ((GeneralBillVO) m_alListData.get(i)).getHeaderVO();
			else {
				SCMEnv.out("list data error!-->" + i);
				return;
			}

		}
		setListHeadData(voaBillHeader);
		// ѡ���б��� ����ˢ�±�����ʾ
		selectListBill(m_iLastSelListHeadRow);
	}

	/**
	 * �����ߣ����˾� ���ܣ��������� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	public void onAdd() {
		// ��ǰ���б���ʽʱ�������л�������ʽ
		onAdd(true, null);
	}

	/**
	 * �����ߣ�yb ���ܣ�ǩ�� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32)
	 * 
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
		UFTime ufdPre1 = new UFTime(System.currentTimeMillis());// ϵͳ��ǰʱ��
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
					// LongTimeTask.showHintMsg("����"+voaBills[i].getHeaderVO().getVbillcode()+"��ʼǩ��...");
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
	 * �����ߣ�yb ���ܣ�ǩ�� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32)
	 * 
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
			// LongTimeTask.procclongTime(this,"����ˢ�½���...",0,
			// 3,this.getClass().getName(),this,"onQuery",
			// null,null);
			// }
		} catch (Exception e) {
			nc.ui.ic.pub.tools.GenMethod.handleException(this, null, e);
		}
		this.updateUI();
	}

	/**
	 * �����ߣ�yb ���ܣ�ɾ������ ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	public String checkBillForAudit(GeneralBillVO vobill) {
		if (vobill == null) return null;
		if (vobill.getHeaderVO().getFbillflag() != null && vobill.getHeaderVO().getFbillflag().intValue() != BillStatus.IFREE) return "����[" + vobill.getHeaderVO().getVbillcode() + "]��������״̬������ǩ�֣�";
		return null;
	}

	/**
	 * �����ߣ����˾� ���ܣ�ǩ�� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32)
	 * 
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 */
	@SuppressWarnings("restriction")
	public GeneralBillVO getAuditVO(GeneralBillVO voAudit, UFDateTime sysdatetime) {

		// ��������״̬δû�б��޸�
		setBillBCVOStatus(voAudit, nc.vo.pub.VOStatus.UNCHANGED);
		// ֧��ƽ̨��cloneһ�����Ա����Ժ�Ĵ���ͬʱ��ֹ�޸���m_voBill
		GeneralBillHeaderVO voHead = voAudit.getHeaderVO();

		// ǩ����
		voHead.setCregister(m_sUserID);
		// ���Բ��ǵ�ǰ��¼��λ�ĵ��ݣ����Բ���Ҫ�޸ĵ��ݡ�
		// voHead.setPk_corp(m_sCorpID);
		voHead.setDaccountdate(new nc.vo.pub.lang.UFDate(m_sLogDate));
		voHead.setAttributeValue("taccounttime", sysdatetime.toString()); // ǩ��ʱ��//zhy2005-06-15ǩ��ʱ��=��½����+ϵͳʱ��

		// vo����Ҫ����ƽ̨������Ҫ���ɺ�ǩ�ֺ�ĵ���
		// voHead.setFbillflag(new
		// Integer(nc.vo.ic.pub.bill.BillStatus.SIGNED));
		voHead.setCoperatoridnow(m_sUserID); // ��ǰ����Ա2002-04-10.wnj
		voHead.setAttributeValue("clogdatenow", m_sLogDate); // ��ǰ��¼����

		voAudit.setParentVO(voHead);

		// ���ݲֿ������òֿ��Ƿ����������� add by hanwei 2004-4-30
		getGenBillUICtl().setBillIscalculatedinvcost(voAudit);

		// ƽ̨����Ҫ�������ͷPK
		GeneralBillItemVO voaItem[] = voAudit.getItemVOs();
		int iRowCount = voAudit.getItemCount();
		for (int i = 0; i < iRowCount; i++) {
			// ��ͷPK
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

		// ���ڡ�������Ϣ
		voAudit.m_iActionInt = nc.vo.scm.pub.bill.CreditConst.ICREDIT_ACT_OTHER;
		voAudit.m_sActionCode = "SIGN";

		return voAudit;

	}
	


	/**
	 * �����ߣ����˾� ���ܣ�ǩ�� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32)
	 * 
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 */
	@SuppressWarnings("restriction")
	public void onAudit() {

		m_timer.start("ǩ�ֿ�ʼ��");	
		try {

			if (BillMode.List == m_iCurPanel && getBillListPanel().getHeadTable().getSelectedRowCount() > 1) {
				onBatchAction(ICConst.SIGN);
				return;
			}

			if (m_iLastSelListHeadRow >= 0 && m_alListData != null && m_alListData.size() > m_iLastSelListHeadRow && m_alListData.get(m_iLastSelListHeadRow) != null)
			// ���ﲻ��clone(),�޸�m_voBillͬʱ�޸�list.
			m_voBill = (GeneralBillVO) ((GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow)).clone();
			
			if (m_voBill != null) {

				UFTime ufdPre1 = new UFTime(System.currentTimeMillis());// ϵͳ��ǰʱ��
				UFDateTime ufdPre = new UFDateTime(m_sLogDate + " " + ufdPre1.toString());

				// ��������״̬δû�б��޸�
				setBillBCVOStatus(m_voBill, nc.vo.pub.VOStatus.UNCHANGED);
				// ֧��ƽ̨��cloneһ�����Ա����Ժ�Ĵ���ͬʱ��ֹ�޸���m_voBill
				GeneralBillVO voAudit = (GeneralBillVO) m_voBill.clone();
				voAudit = getAuditVO(voAudit, ufdPre);

				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000003")/*
																												* @res "����ǩ�֣����Ժ�..."
																												*/);
				m_timer.showExecuteTime("before ƽ̨ǩ�֣�");

				
				while (true) {
					try {
						// ��˵ĺ��ķ���
						onAuditKernel(voAudit);//78��85
						
						/****************************************add by yhj 2014-03-14 START*********************/
						/**
						 *�˴������Ѿ�ת�Ƶ�ia->client:nc.ui.ia.bill.BillClientUI.java
						 */
						//�����ж�ҵ������-�����ӹ������̲Ż�ִ�����´���
//						if(voAudit != null && voAudit.getChildrenVO().length > 0){
//							//1016A210000000098ZO3 ���ӹ���Ʒ����ҵ������
//							if(voAudit.getParentVO().getAttributeValue("cbiztype").equals("1016A210000000098ZO3")){
//								IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
//								GeneralBillVO gbvo = voAudit;
//								//ȡ�����۳��ⵥ ��˾���������͡�
//								CircularlyAccessibleValueObject[] cvos = gbvo.getChildrenVO();
//								String temp_pk_corp = (String)voAudit.getParentVO().getAttributeValue("pk_corp");//��˾
//								String temp_billtypecode = (String)voAudit.getParentVO().getAttributeValue("cbilltypecode");//��������
//								String temp_cfirstbillbid = null;
//								String temp_cinventoryid = null;
//								if(cvos != null && cvos.length > 0){
//									List<GeneralBillItemVO> itemvosList = new ArrayList<GeneralBillItemVO>();
//									for (int i = 0; i < cvos.length; i++) {
//										//ȡ�����۳��ⵥ�����š�ʵ�ʳ�������
//										String temp_dh = (String) cvos[i].getAttributeValue("vfree0");//���
//										UFDouble temp_num = (UFDouble)cvos[i].getAttributeValue("noutnum");//ʵ�ʳ�������
//										temp_cinventoryid = (String)cvos[i].getAttributeValue("cinventoryid");//�������
//										temp_cfirstbillbid = (String)cvos[i].getAttributeValue("cfirstbillbid");//Դͷ���ݱ���ID
//										//���ݶ��-��ȡ������Ʒ��ⵥ�ĵ���
//										/**
//										 * �����۳��ⵥ����Ķ�����������ʽ��ʾ�ģ����Ի���Ҫ�ı�һ�¶�ŵĸ�ʽ��ȥ�������ţ����ģ�
//										 * ��ð��-��[���:[���:20130922-01]]
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
//												//ȥ�����[]
//												String res = temp_dh.substring(1, temp_dh.length() - 1);
//												String[] str = res.split(":");
//												//����ð�Ž�ȡ�����
//												String inner_dh  = str[1];
//												//ȥ���ڲ�[]
//												temp_dh = inner_dh.substring(1, inner_dh.length() - 1);
//												//����ð�Ž�ȡ�ڲ���
//												String[] str2 = temp_dh.split(":");
//												temp_dh = str2[1];
//											}
//											String sql = "select nprice from po_arriveorder_b where vfree1='"+temp_dh+"' and cmangid='"+temp_cinventoryid+"' and nvl(dr,0) = 0 and pk_corp='"+temp_pk_corp+"'";
//											temp_nprice = (BigDecimal)iUAPQueryBS.executeQuery(sql, new ColumnProcessor());
//											 
//										}
//										
//										//�������۳ɱ���ת���Ľ��
//										BigDecimal a2 = new BigDecimal(temp_num.toString());
//										BigDecimal rest = a2.subtract(temp_nprice.doubleValue()==0 ? new BigDecimal(0) : temp_nprice );
//										//ȡ�����۳ɱ���ת����Ӧ������
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
																																		* ".\nȨ��У�鲻ͨ��,����ʧ��! "
																																		*/);
						getAccreditLoginDialog().setCorpID(m_sCorpID);
						getAccreditLoginDialog().clearPassWord();
						if (getAccreditLoginDialog().showModal() == nc.ui.pub.beans.UIDialog.ID_OK) {
							String sUserID = getAccreditLoginDialog().getUserID();
							if (sUserID == null) {
								throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000070")/*
																																	* @res
																																	* "Ȩ��У�鲻ͨ��,����ʧ��. "
																																	*/);
							} else {
								voAudit.setAccreditUserID(sUserID);
								continue;
							}
						} else {
							throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000070")/*
																																* @res
																																* "Ȩ��У�鲻ͨ��,����ʧ��. "
																																*/);

						}
					} catch (Exception ee1) {

						BusinessException realbe = nc.ui.ic.pub.tools.GenMethod.handleException(null, null, ee1);
						if (realbe != null && realbe.getClass() == CreditNotEnoughException.class) {
							// ������Ϣ��ʾ����ѯ���û����Ƿ��������
							int iFlag = showYesNoMessage(realbe.getMessage() + " \r\n" + "�Ƿ������");
							// ����û�ѡ�����
							if (iFlag == nc.ui.pub.beans.MessageDialog.ID_YES) {
								voAudit.setIsCheckCredit(false);
								continue;
							} else return;
						} else if (realbe != null && realbe.getClass() == PeriodNotEnoughException.class) {
							// ������Ϣ��ʾ����ѯ���û����Ƿ��������
							int iFlag = showYesNoMessage(realbe.getMessage() + " \r\n" + "�Ƿ������");
							// ����û�ѡ�����
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
								// ������Ϣ��ʾ����ѯ���û����Ƿ��������
								int iFlag = showYesNoMessage(atpe.getMessage() + " \r\n" + "�Ƿ������");
								// ����û�ѡ�����
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
				// showTime(lTime, "ǩ��");
				m_timer.showExecuteTime("ƽ̨ǩ��ʱ�䣺");
				// ���ش���
				String sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000307")/* @res "ǩ�ֳɹ���" */;
				// ---- old ------����Ǳ��漴ǩ����Ҫˢ�½���,��Ȼ��������ʱ�Ǳ�����ڱ���ʱ����ˡ�
				// ---- old ------ֻҪ�ܱ��棬˵�������Ѿ���ȷ¼���ˡ�
				// -- new ---��Ϊ��ƽ̨��������������ִ����󣬻�Ҫ�ض��Ƿ�ǩ���ˡ�
				String sBillStatus = freshStatusTs(voAudit.getHeaderVO().getPrimaryKey());

				// ����ǩ��ʱ�����ǰ����vo
				m_voBill.setHeaderValue("taccounttime", ufdPre.toString());
				((GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow)).setHeaderValue("taccounttime", ufdPre.toString());

				// �ж��Ƿ����ֹ���,�����ֹ���,�����ֹ��ֿ߲��������ⵥ.xiaolong_fan.2012-12-25.
				SMGeneralBillVO vo = null;
				if (getParentCorpCode1().equals("10395")) {
					try {
						if (isNeedCreateBill(voAudit)) {
							while (true) {
								if (nc.ui.pub.beans.MessageDialog.ID_YES != nc.ui.pub.beans.MessageDialog.showYesNoDlg(this, null, "�õ��ݰ����ֹ�������,�Ƿ���Ҫ�Զ����ɶ�Ӧ��������ⵥ?(ѡ���ǽ��Զ�����������ⵥ,ѡ�����ֶ����ɶ�Ӧ��������ⵥ)", MessageDialog.ID_YES)) {
									break;
								}
								if (voAudit == null || voAudit.getHeaderVO() == null || voAudit.getHeaderVO().getCbizid() == null || "".equals(voAudit.getHeaderVO().getCbizid())) {
									showErrorMessage("�Զ�����ʧ��,�ó��ⵥ�����ֹ�������,������д����Ա!");
									break;
								}
								if (cargdocVO == null || cargdocVO.getCsname() == null || "".equals(cargdocVO.getCsname())) {
									showErrorMessage("�Զ�����ʧ��,�ֹ��߲�(����S01)�����к�����Ա���Ӧ�Ļ�λ(��λ����Ϊ����Ա����+����Ա����)!");
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
						// ɾ���Զ����ɵ���ⵥ
						// showErrorMessage("�ó��ⵥ�����ֹ�������,����ʱ�Զ������ֹ��ֵ߲���ⵥ����,����ϵϵͳά����Ա����!");
					}
					if (vo != null && vo.getHeaderVO() != null) {
						showWarningMessage("�ó��ⵥ�����ֹ�������,�Զ�����������ⵥ,����:" + vo.getHeaderVO().getVbillcode());
					}
				}
				// end by xiaolong_fan.

				// // �����µ�Ԥ���ֶ�6(��¼���ɵ�������ⵥ��ID)
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
				} else sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000308")/* @res "ǩ�ֳ���" */;

				showHintMessage(sMsg);
			}
		} catch (Exception be) {
			// ###################################################
			// ǩ���쳣����̨��־ add by hanwei 2004-6-8
			nc.ui.ic.pub.bill.GeneralBillUICtl.insertOperatelogVO(m_voBill.getHeaderVO(), nc.ui.ml.NCLangRes.getInstance().getStrByID("40080402", "UPT40080402-000013")/*
																																										* @res "ǩ��"
																																										*/, nc.vo.scm.funcs.Businesslog.MSGERROR + "" + be.getMessage());
			// ###################################################

			handleException(be);
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000004")/* @res "ǩ�ֳ���" */
					+ be.getMessage());
			showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000005")/* @res "ǩ�ֳ���" */
					+ be.getMessage());
		}

	}

	private Object String(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * �����ɲ�����ⵥ�Ĵ��������map.
	 */
	@SuppressWarnings("unchecked")
	Map itemvomap = null;

	/**
	 * ��λPK
	 */
	CargdocVO cargdocVO = null;

	/**
	 * �жϲ��ϳ��ⵥ�Ĵ���Ƿ�����ֹ���,����true,��֮false.������ֹ���,�����Զ����ɲ�����ⵥ.
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
			// showErrorMessage("�õ���ǩ���漰�ֹ��ֵ߲Ļ�λ,��ȷ�ϸó��ⵥ������Ա������Ա��Ӧ���ֹ��ֵ߲Ļ�λ�Ƿ����!");
			// throw new
			// Exception("�õ���ǩ���漰�ֹ��ֵ߲Ļ�λ,��ȷ�ϸó��ⵥ������Ա������Ա��Ӧ���ֹ��ֵ߲Ļ�λ�Ƿ����!");
		} else if (obj != null && obj instanceof ArrayList) {
			ArrayList list = (ArrayList) obj;
			cargdocVO = ((list != null && list.size() > 0) ? (CargdocVO) list.get(0) : null);
		}
		// �ֿ�ID
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
				if (obj != null && "1".equals(materclass)) {// 1��Ӧ���Ϸ���Ϊ����.
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
	 * �Զ����ɲ�����ⵥ. xiaolong_fan.2012-12-25.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	protected SMGeneralBillVO createBill(String pk) throws Exception {
		// vomap��Ϊ��,��������������ⵥ.
		if (itemvomap == null || itemvomap.size() <= 0) return null;
		GeneralBillVO billVO = new GeneralBillVO();
		billVO.setParentVO(createBillHeaderVO(pk));
		billVO.getHeaderVO().setStatus(2);
		List<GeneralBillItemVO> itemvolist = new ArrayList();
		Iterator it = itemvomap.values().iterator();
		while (it.hasNext()) {
			GeneralBillItemVO itemvo = (GeneralBillItemVO) it.next();
			GeneralBillItemVO tempvo = new GeneralBillItemVO();

			// ��λ
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
			// ������ⵥ��ʵ������Ϊ���ϳ��ⵥ��ʵ������.
			tempvo.setNinnum(itemvo.getNoutnum());
			// ����
			tempvo.setNprice(itemvo.getNprice());
			// ���
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
			showErrorMessage("�Զ������ֹ��ֵ߲���ⵥ����,���ֶ����ɻ���ϵϵͳά����Ա����!");
			throw new Exception("�Զ������ֹ��ֵ߲���ⵥ����,���ֶ����ɻ���ϵϵͳά����Ա����!");
		}
		if (alPK != null && alPK.size() >= 3 && alPK.get(2) != null) {
			SMGeneralBillVO vo = (SMGeneralBillVO) alPK.get(2);
			billVO.getHeaderVO().setTs(vo.getHeaderVO().getTs());
			billVO.getHeaderVO().setPrimaryKey(vo.getHeaderVO().getPrimaryKey());
			for (int i = 0; i < billVO.getChildrenVO().length; i++) {
				billVO.getChildrenVO()[i].setPrimaryKey(vo.getChildrenVO()[i].getPrimaryKey());
			}
			UFTime ufdPre1 = new UFTime(System.currentTimeMillis());// ϵͳ��ǰʱ��
			UFDateTime ufdPre = new UFDateTime(m_sLogDate + " " + ufdPre1.toString());
			billVO = getAuditVO(billVO, ufdPre);
			try {
				nc.ui.pub.pf.PfUtilClient.processBatch("SIGN", "4A", m_sLogDate, new GeneralBillVO[] {
					billVO
				});
			} catch (Exception e) {
				e.printStackTrace();
				showErrorMessage("�ó��ⵥ�����ֹ�������,����ʱ�Զ������ֹ��ֵ߲���ⵥ:" + vo.getHeaderVO().getVbillcode() + "ǩ�ֳ���,���ֶ�ǩ�ֻ���ϵϵͳά����Ա����!");
				throw new Exception("�ó��ⵥ�����ֹ�������,����ʱ�Զ������ֹ��ֵ߲���ⵥ:" + vo.getHeaderVO().getVbillcode() + "ǩ�ֳ���,���ֶ�ǩ�ֻ���ϵϵͳά����Ա����!");
			}
			return vo;
		}
		return null;
	}

	/**
	 * ����������ⵥ�ı�ͷVO.xiaolong_fan.
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
			// �ֿ�ID
			billHeaderVO.setCwarehouseid(vo.getPrimaryKey());
			// �����֯PK
			billHeaderVO.setPk_calbody(vo.getPk_calbody());
		}
		billHeaderVO.setCwarehousename("");
		// ���ԱID
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
	 * @����:���û�λ��ֵ
	 * @author ���ֹ�Ө
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

			// ������
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
			// ����
			if (num == null) {
				voLoc[0].setNinspacenum(num);
				voLoc[0].setNoutspacenum(num);
				if (m_alSerialData != null) m_alSerialData.set(i, null);
			} else {
				if (m_iBillInOutFlag > 0) {
					// ���
					voLoc[0].setNoutspacenum(null);
					voLoc[0].setNinspacenum(num);
				} else {
					// ����
					voLoc[0].setNinspacenum(null);
					voLoc[0].setNoutspacenum(num);
					if (m_alSerialData != null) m_alSerialData.set(i, null);
				}
			}
			// ë��
			if (ngrossnum == null) {
				voLoc[0].setNingrossnum(ngrossnum);
				voLoc[0].setNoutgrossnum(ngrossnum);
				if (m_alSerialData != null) m_alSerialData.set(i, null);
			} else {
				if (m_iBillInOutFlag > 0) {
					// ���
					voLoc[0].setNoutgrossnum(null);
					voLoc[0].setNingrossnum(ngrossnum);
				} else {
					// ����
					voLoc[0].setNingrossnum(null);
					voLoc[0].setNoutgrossnum(ngrossnum);

				}

			}

		} else m_alLocatorData.set(i, null);

	}

	/** ��ѯ���� */
	private IUAPQueryBS uapQueryBS;

	/**
	 * <p>
	 * �����ѯ����
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
	 * ����ʵ�ָ÷�������Ӧ��ť�¼���
	 * 
	 * @version (00-6-1 10:32:59)
	 * 
	 * @param bo
	 *            ButtonObject
	 */
	@SuppressWarnings("restriction")
	public void onButtonClicked(nc.ui.pub.ButtonObject bo) {
		// getBillCardPanel().tableStopCellEditing();
		getBillCardPanel().stopEditing();//ֹͣ�༭

		// �����λ��2003-07-21 ydy
		clearOrientColor();

		// ��״̬����ʾ
		// showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
		// "SCMCOMMON", "UPPSCMCommon-000133")/* @res "����" */);
		//		
		showHintMessage(bo.getName());
		// ���˵���<����>
		// û�е��ݲ��յĻ��������ڵ��ݱ༭�˵���������ҵ�����ͺ�
		if (m_bNeedBillRef) {
			if (bo.getParent() == getButtonTree().getButton(ICButtonConst.BTN_ADD)) {

				onBizType(bo);   //����
				getBillCardPanel().transferFocusTo(nc.ui.pub.bill.BillCardPanel.HEAD);
			} else if (bo.getParent() == getButtonTree().getButton(ICButtonConst.BTN_BUSINESS_TYPE)) {
				onJointAdd(bo);     //ҵ������
				getBillCardPanel().transferFocusTo(nc.ui.pub.bill.BillCardPanel.HEAD);
			}
		} else if (bo == getButtonTree().getButton(ICButtonConst.BTN_ADD) || bo == getButtonTree().getButton(ICButtonConst.BTN_BILL_ADD_MANUAL)) {
			onAdd();    //��������
			getBillCardPanel().transferFocusTo(nc.ui.pub.bill.BillCardPanel.HEAD);
		}

		if (bo == getButtonTree().getButton(ICButtonConst.BTN_LINE_ADD)) {
			onAddLine();   //����

		} else if (bo == getButtonTree().getButton(ICButtonConst.BTN_LINE_DELETE)) onDeleteRow();  //ɾ��
		else if (bo == getButtonTree().getButton(ICButtonConst.BTN_LINE_COPY)) {

			onCopyLine();   //������

		} else if (bo == getButtonTree().getButton(ICButtonConst.BTN_LINE_PASTE)) {
			onPasteLine();  //ճ����

		} else if (bo == getButtonTree().getButton(ICButtonConst.BTN_LINE_INSERT)) {
			onInsertLine();   //������

		} else if (bo == getButtonTree().getButton(ICButtonConst.BTN_SAVE)) {
			setResIdIfNeed();    
			boolean ok = onSave();  //����
			if (ok) saveResId();
		} else if (bo == getButtonTree().getButton(ICButtonConst.BTN_BILL_DELETE)) onDelete();  //ɾ��
		else if (bo == getButtonTree().getButton(ICButtonConst.BTN_QUERY)) onQuery(true);   //��ѯ
		else if (bo == getButtonTree().getButton(ICButtonConst.BTN_BROWSE_REFRESH)) onQuery(false);  //ˢ��
		else if (bo == getButtonTree().getButton(ICButtonConst.BTN_ASSIST_QUERY_RELATED)) onJointCheck();   //����
		else if (bo == getButtonTree().getButton(ICButtonConst.BTN_BILL_EDIT)) {
			onUpdate();   //�޸�
			getBillCardPanel().transferFocusTo(nc.ui.pub.bill.BillCardPanel.HEAD);

		} else if (bo == getButtonTree().getButton(ICButtonConst.BTN_BILL_CANCEL)) onCancel();  //ȡ��
		else if (bo == getButtonTree().getButton(ICButtonConst.BTN_SWITCH)) onSwitch();  //�л���Ƭ��ʾ/�б���ʾ
		else if (bo == getButtonTree().getButton(ICButtonConst.BTN_EXECUTE_SIGN_CANCEL)) onCancelAudit();  //ȡ��ǩ��
		else if (bo == getButtonTree().getButton(ICButtonConst.BTN_SIGN)) onAudit();  //ǩ��
		else if (bo == getButtonTree().getButton(ICButtonConst.BTN_BILL_COPY)) onCopy();  //����
		else if (bo == getButtonTree().getButton(ICButtonConst.BTN_BROWSE_LOCATE)) onLocate();  //��λ
		else if (bo == getButtonTree().getButton(ICButtonConst.BTN_LINE_SPACE)) onSpaceAssign();  //��λ
		else if (bo == getButtonTree().getButton(ICButtonConst.BTN_LINE_SERIAL)) onSNAssign();    //���к�
		else if (bo == getButtonTree().getButton(ICButtonConst.BTN_ASSIST_QUERY_ONHAND)) onRowQuyQty();  //������ѯ
		else if (bo == getButtonTree().getButton(ICButtonConst.BTN_ASSIST_QUERY_SUITE)) onSetpart();   //���׼���Ϣ
		else if (bo == getButtonTree().getButton(ICButtonConst.BTN_ASSIST_FUNC_ONHAND)) onOnHandShowHidden();  //������ʾ/����
		else if (bo == getButtonTree().getButton(ICButtonConst.BTN_PRINT_PRINT)) onPrint();  //��ӡ
		else if (bo == getButtonTree().getButton(ICButtonConst.BTN_PRINT_PREVIEW)) onPreview();  //Ԥ��
		else if (bo == getButtonTree().getButton(ICButtonConst.BTN_PRINT_SUM)) onPrintSumRow();   //���ܴ�ӡ

		else if (bo == getButtonTree().getButton(ICButtonConst.BTN_PRINT_SPACE)) onPrintLocSN(getWholeBill(m_iLastSelListHeadRow));  //��ӡ��λ
		else if (bo == m_pageBtn.getFirst()) onFirst();   //��ҳ
		else if (bo == m_pageBtn.getPre()) onPrevious();  //��ҳ
		else if (bo == m_pageBtn.getNext()) onNext();   //��ҳ
		else if (bo == m_pageBtn.getLast()) onLast();   //ĩҳ
		if (bo == getButtonTree().getButton(ICButtonConst.BTN_ASSIST_FUNC_INV_CHECK)) onCheckData();  //�˶Դ��
		else if (bo == getButtonTree().getButton(ICButtonConst.BTN_IMPORT_BILL)) onImportData();  //���뵥��

		// ������������
		else if (bo == getButtonTree().getButton(ICButtonConst.BTN_IMPORT_BOTH_BARCODE))
		// onImportBarcodeAndSubFileData();
		onImptBCExcel(2);  //������������
		// ������
		else if (bo == getButtonTree().getButton(ICButtonConst.BTN_IMPORT_1ST_BARCODE)) onImptBCExcel(0);  //����������
		// �����
		else if (bo == getButtonTree().getButton(ICButtonConst.BTN_IMPORT_2ND_BARCODE)) onImptBCExcel(1);  //���������
		// ������Դ��������
		else if (bo == getButtonTree().getButton(ICButtonConst.BTN_IMPORT_SOURCE_BARCODE)) onImportBarcodeSourceBill();  //������Դ��������
		// ����ر�
		else if (bo == getButtonTree().getButton(ICButtonConst.BTN_EXECUTE_BARCODE_CLOSE)) {
			onBarcodeOpenClose(0);   //����ر�
		} else if (bo == getButtonTree().getButton(ICButtonConst.BTN_EXECUTE_BARCODE_OPEN)) {
			onBarcodeOpenClose(1);   //�����
		} else if (bo == getButtonTree().getButton(ICButtonConst.BTN_LINE_BARCODE)) {   //������
			onBarCodeEdit();
		} else if (bo == getButtonTree().getButton(ICButtonConst.BTN_ASSIST_FUNC_DOCUMENT)) onDocument();   //�ĵ�����
		// else if (bo == m_boSelectLocator)
		// onSelLoc();
		// ��Ӧ�����գ��Զ���дʵ�����գ�
		else if (bo == getButtonTree().getButton(ICButtonConst.BTN_LINE_AUTO_FILL)) onFillNum();   //�Զ�ȡ��
		else if (bo == getButtonTree().getButton(ICButtonConst.BTN_ASSIST_FUNC_PICKUP_AUTO)) onPickAuto();   //�Զ����
		else if (bo == getButtonTree().getButton(ICButtonConst.BTN_ASSIST_FUNC_REFER_IN)) onRefInICBill();   //������ⵥ
		// ���ݵ���

		else if (bo == getButtonTree().getButton(ICButtonConst.BTN_EXPORT_TO_DIRECTORY)) onBillExcel(1);// ������ָ��Ŀ¼
		else if (bo == getButtonTree().getButton(ICButtonConst.BTN_EXPORT_TO_XML)) // ������XML
		onBillExcel(3);
		else if (bo == getButtonTree().getButton(ICButtonConst.BTN_PRINT_DISTINCT)) {
			onMergeShow();   //�ϲ���ʾ
		} else if (m_funcExtend != null) {
			// ֧�ֹ�����չ
			if (BillMode.Card == m_iCurPanel) m_funcExtend.doAction(bo, this, getBillCardPanel(), getBillListPanel(), nc.ui.scm.extend.IFuncExtend.CARD);
			else if (BillMode.List == m_iCurPanel) m_funcExtend.doAction(bo, this, getBillCardPanel(), getBillListPanel(), nc.ui.scm.extend.IFuncExtend.LIST);
		} else {
			onExtendBtnsClick(bo);
		}
		//add by shikun 2014-04-08 ������ť�µ��Ӱ�ť����������ȥ�������������
		if (bo.getParent() == getButtonTree().getButton(ICButtonConst.BTN_ADD)) {  //����
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
		Object wwddid = billVO.getHeaderVO().getAttributeValue("pk_defdoc6");// ί�ⶩ��id
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
				String resid = items[i].getVuserdef1();// ��Դ��
				Object bcuser1 = items[i].getAttributeValue("vbcuser1");
				@SuppressWarnings("unused")
				String processBat = (String) items[i].getAttributeValue("pk_defdoc3");// �ӹ�����
				@SuppressWarnings("unused")
				String def6 = (String) items[i].getAttributeValue("pk_defdoc6");// �ӹ����δ�������Դ��
				String def7 = (String) items[i].getAttributeValue("pk_defdoc7");// ί�ⷢ�����κŴ�������Դ��
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
	 * �ɹ������Դ�Ÿ��µ����κŵ�����
	 */
	private void saveResId() {
		GeneralBillVO billVO = getBillVO();
		String cbilltypecode = billVO.getHeaderVO().getCbilltypecode();
		Object wwddid = billVO.getHeaderVO().getAttributeValue("pk_defdoc6");// ί�ⶩ��id
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
				if (updated) onQuery(false);// ˢ��
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

			// ���ӵ�����
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
																																		 * "�Ƿ�ȷ��Ҫȡ����"
																																		 */
		, MessageDialog.ID_NO)) {

		case nc.ui.pub.beans.MessageDialog.ID_YES:
			break;
		default:
			return;
		}
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH045")/* @res "����ȡ��" */);

		// v5 lj ֧�ֲ������ɶ��ŵ���
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
			// �ָ���λ����
			m_alLocatorData = m_alLocatorDataBackup;
			// �屸������
			m_alLocatorDataBackup = null;

			// �ָ����к�����
			m_alSerialData = m_alSerialDataBackup;
			// �����кű�������
			m_alSerialDataBackup = null;
			// �ָ�billvo
			if (m_iLastSelListHeadRow >= 0 && m_alListData != null && m_alListData.size() > m_iLastSelListHeadRow && m_alListData.get(m_iLastSelListHeadRow) != null) {
				m_voBill = (GeneralBillVO) ((GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow)).clone();
				setBillVO(m_voBill);
				// resumeValue�ָ�����ˢ�±�β��ʾ
				setTailValue(0);
			} else {
				// ������������棡
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
			// �����Ƿ�Ϊ��Դ���ݿ��Ƶ��ݽ���
			ctrlSourceBillUI(true);

		}
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH008")/*
																						* @res "ȡ���ɹ�"
																						*/);
	}

	/**
	 * � ��ʾ���������ִ������
	 * 
	 */
	protected void onOnHandShowHidden() {

		if (m_iCurPanel == BillMode.List) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000065")/* @res "���л�����Ƭ���棡" */);
			return;
		}

		m_bOnhandShowHidden = !m_bOnhandShowHidden;
		// if (m_bOnhandShowHidden)
		// m_boOnHandShowHidden.setName("���ش�����");
		// else
		// m_boOnHandShowHidden.setName("��ʾ������");
		//        
		// updateButton(m_boOnHandShowHidden);

		if (m_bOnhandShowHidden) {
			m_sMultiMode = MultiCardMode.CARD_TAB;
		} else m_sMultiMode = MultiCardMode.CARD_PURE;

		m_layoutManager.show();

	}

	/**
	 * �����ߣ�yb ���ܣ�ɾ������ ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	public String checkBillForCancelAudit(GeneralBillVO vobill) {
		if (vobill == null) return null;
		if (vobill.getHeaderVO().getFbillflag() != null && vobill.getHeaderVO().getFbillflag().intValue() != BillStatus.ISIGNED) return "����[" + vobill.getHeaderVO().getVbillcode() + "]����ǩ��״̬������ȡ��ǩ�֣�";
		return null;
	}

	/**
	 * �����ߣ����˾� ���ܣ�ǩ�� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32)
	 * 
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 */
	public GeneralBillVO getCancelAuditVO(GeneralBillVO voAudit) {

		GeneralBillHeaderVO voHead = voAudit.getHeaderVO();
		// ǩ����
		// voHead.setCregister(m_sUserID);
		// ���Բ��ǵ�ǰ��¼��λ�ĵ��ݣ����Բ���Ҫ�޸ĵ��ݡ�
		// voHead.setPk_corp(m_sCorpID);
		// ����������֮�����ɿ���½�ȡ��ǩ��δ�ۼ�����
		// voHead.setDaccountdate(new nc.vo.pub.lang.UFDate(m_sLogDate));
		// vo����Ҫ����ƽ̨������Ҫ����ǩ�ֺ�ĵ���
		voHead.setFbillflag(new Integer(nc.vo.ic.pub.bill.BillStatus.SIGNED));
		voHead.setCoperatoridnow(m_sUserID); // ��ǰ����Ա2002-04-10.wnj
		voHead.setAttributeValue("clogdatenow", m_sLogDate); // ��ǰ��¼����2003-01-05
		voAudit.setParentVO(voHead);

		// ƽ̨����Ҫ�������ͷPK
		GeneralBillItemVO voaItem[] = voAudit.getItemVOs();
		// ��������
		int iRowCount = voAudit.getItemCount();

		for (int i = 0; i < iRowCount; i++) {
			// ��ͷPK
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
	 * �����ߣ����˾� ���ܣ�ȡ������ ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	public void onCancelAudit() {
		try {
			switch (nc.ui.pub.beans.MessageDialog.showYesNoDlg(this, null, ResBase.getIsCancleSign()/* @res "�Ƿ�ȷ��Ҫȡ��ǩ�֣�" */
			, MessageDialog.ID_NO)) {

			case nc.ui.pub.beans.MessageDialog.ID_YES:
				break;
			default:
				return;
			}
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000006")/*
																											* @res "����ȡ��ǩ�֣����Ժ�..."
																											*/);

			if (BillMode.List == m_iCurPanel && getBillListPanel().getHeadTable().getSelectedRowCount() > 1) {
				onBatchAction(ICConst.CANCELSIGN);
				return;
			}
			// ����m_voBill,�Զ�ȡ�������ݡ�
			if (m_iLastSelListHeadRow >= 0 && m_alListData != null && m_alListData.size() > m_iLastSelListHeadRow && m_alListData.get(m_iLastSelListHeadRow) != null)
			// ���ﲻ��clone(),�޸�m_voBillͬʱ�޸�list.
			m_voBill = (GeneralBillVO) ((GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow)).clone();

			//ȡ��ǩ��ʱ,�ж��Ƿ�����Զ����ɵ�������ⵥ.xiaolong_fan.
			if (existBill(m_voBill)) { return; }
			//end by xiaolong_fan.
			if (m_voBill != null) {
				// ��������״̬δû�б��޸�
				setBillBCVOStatus(m_voBill, nc.vo.pub.VOStatus.UNCHANGED);
				// ֧��ƽ̨��cloneһ�����Ա����Ժ�Ĵ���ͬʱ��ֹ�޸���m_voBill
				GeneralBillVO voAudit = (GeneralBillVO) m_voBill.clone();
				voAudit = getCancelAuditVO(voAudit);
				long lTime = System.currentTimeMillis();
				// ����ֵ
				ArrayList alRet = null;

				while (true) {
					try {

						alRet = (ArrayList) nc.ui.pub.pf.PfUtilClient.processAction("CANCELSIGN", m_sBillTypeCode, m_sLogDate, voAudit);
						break;

					} catch (Exception ee1) {

						BusinessException realbe = nc.ui.ic.pub.tools.GenMethod.handleException(null, null, ee1);
						if (realbe != null && realbe.getClass() == CreditNotEnoughException.class) {
							// ������Ϣ��ʾ����ѯ���û����Ƿ��������
							int iFlag = showYesNoMessage(realbe.getMessage() + " \r\n" + "�Ƿ������");
							// ����û�ѡ�����
							if (iFlag == nc.ui.pub.beans.MessageDialog.ID_YES) {
								voAudit.setIsCheckCredit(false);
								continue;
							} else return;
						} else if (realbe != null && realbe.getClass() == PeriodNotEnoughException.class) {
							// ������Ϣ��ʾ����ѯ���û����Ƿ��������
							int iFlag = showYesNoMessage(realbe.getMessage() + " \r\n" + "�Ƿ������");
							// ����û�ѡ�����
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
								// ������Ϣ��ʾ����ѯ���û����Ƿ��������
								int iFlag = showYesNoMessage(atpe.getMessage() + " \r\n" + "�Ƿ������");
								// ����û�ѡ�����
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

				showTime(lTime, "ȡ��ǩ��");
				// ���ش���
				String sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000309")/* @res "ȡ��ǩ�ֳ���" */;
				if (alRet != null && alRet.size() > 0 && alRet.get(0) != null) {
					Boolean bOK = (Boolean) alRet.get(0);
					if (bOK != null && bOK.booleanValue()) {
						int curmode = m_iCurPanel;
						freshAfterCancelSignedOK();
						if (curmode == BillMode.Card) onSwitch();
						sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000310")/*
																											* @res
																											* "ȡ��ǩ�ֳɹ���"
																											*/;
					}
				}
				// ---- old ------����Ǳ��漴ǩ����Ҫˢ�½���,��Ȼ��������ʱ�Ǳ�����ڱ���ʱ����ˡ�
				// ---- old ------ֻҪ�ܱ��棬˵�������Ѿ���ȷ¼���ˡ�
				// -- new ---��Ϊ��ƽ̨��������������ִ����󣬻�Ҫ�ض��Ƿ�ǩ���ˡ�
				freshStatusTs(voAudit.getHeaderVO().getPrimaryKey());

				// ȡ��ǩ�ֺ�Ҫ�����з���Դ���ݶԲ˵�ɾ����ť����
				ctrlSourceBillButtons(true);

				showHintMessage(sMsg);
			}
		} catch (Exception e) {
			handleException(e);
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000007")/* @res "ȡ��ǩ�ֳ���" */);
			showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000008")/* @res "ȡ��ǩ�ֳ���:" */
					+ e.getMessage());
		}

	}

	//	/**
	//	 * �ж��Ƿ�����Զ����ɵ�������ⵥ,�����,���ж��Ƿ���Ҫɾ��.
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
	//				// ���ڡ�������Ϣ
	//				vo.m_iActionInt = nc.vo.scm.pub.bill.CreditConst.ICREDIT_ACT_DEL;
	//				vo.m_sActionCode = "DELETE";
	//				// ����Ա��־
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
	//				showErrorMessage("ɾ���Զ����ɵ�������ⵥʧ��,���ֶ�ɾ��������ⵥ.����:"
	//						+ vo.getHeaderVO().getVbillcode());
	//			}
	//		} catch (Exception e) {
	//			e.printStackTrace();
	//			showErrorMessage("ɾ���Զ����ɵ�������ⵥ����!");
	//		}
	//
	//	}

	/**
	 * �ж��Ƿ�����Զ����ɵ�������ⵥ,xiaolong_fan.
	 * 
	 * @param voAudit
	 */
	protected boolean existBill(GeneralBillVO voAudit) {
		try {
			// ִ��ȡ��ǩ��
			GeneralBillHeaderVO headvo = voAudit.getHeaderVO();
			String pk = headvo.getPrimaryKey();
			if (pk != null && !"".equals(pk)) {
				GeneralBillVO vo = new GeneralBillVO();
				GeneralBillHeaderVO hvo = (GeneralBillHeaderVO) getUAPQuery().executeQuery("select * from ic_general_h where nvl(dr,0)=0 and autocreatebillid='" + pk + "'", new BeanProcessor(GeneralBillHeaderVO.class));
				if (hvo != null) {
					showErrorMessage("ȡ��ǩ��ʱ,����ɾ���������������ⵥ:" + hvo.getVbillcode());
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * �����ߣ����˾� ���ܣ����Ƶ�ǰ���� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	public void onCopy() {
		try {
			// ��ǰ���б���ʽʱ�������л�������ʽ
			if (BillMode.List == m_iCurPanel) {
				onSwitch();

			}

			if (m_iLastSelListHeadRow < 0 || m_alListData == null || m_iLastSelListHeadRow >= m_alListData.size() || m_alListData.get(m_iLastSelListHeadRow) == null) {
				SCMEnv.out("sn error,or list null");
				return;
			}
			// ��ʾ���Ƶĵ�������
			m_voBill = (GeneralBillVO) ((GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow)).clone();
			// ��ʼ����λ/���к�����
			if (m_voBill != null) {

				// ����
				getBillCardPanel().addNew();
				// ������(onAdd())ȥ�������ж�����
				// ���ͷID
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
				// ����ʱӦ��daccountdate���
				m_voBill.setHeaderValue("dauditdate", null);// zhy2005-04-08
				// ����ʱӦ��dauditdate���

				m_voBill.setHeaderValue("iprintcount", new Integer(0)); // �ô�ӡ����

				m_voBill.setHeaderValue("taccounttime", null); // ��ǩ��ʱ��

				// ��������������̬
				m_voBill.setHeaderValue("fbillflag", nc.vo.ic.pub.bill.BillStatus.FREE);
				// ���µ��ݺ�
				// ������ʾVO
				int iRowCount = m_voBill.getItemCount();
				UFDate dcurdate = new UFDate(m_sLogDate);
				GeneralBillItemVO voaMyItem[] = m_voBill.getItemVOs();
				for (int row = 0; row < iRowCount; row++) {
					// �����ID
					voaMyItem[row].setPrimaryKey(null);
					voaMyItem[row].setCgeneralhid(null);
					// ��Դ�������ݣ�
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
					// ��Ӧ����������
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
					// ��������
					voaMyItem[row].setCfreezeid(null);
					// ts
					voaMyItem[row].setTs(null);
					// �ݹ���־����
					voaMyItem[row].setIsok(new UFBoolean(false));
					// �������־����
					voaMyItem[row].setBzgflag(new UFBoolean(false));
					// �������־����
					voaMyItem[row].setAttributeValue("btoinzgflag", new UFBoolean(false));
					voaMyItem[row].setAttributeValue("btoouttoiaflag", new UFBoolean(false));
					// ���Ƶ���ʱ��ձ����б�ǣ�Ŀǰֻ������ʹ�ã�
					voaMyItem[row].setFbillrowflag(null);
					// ���Ƶ���ʱ��ձ�������� �޸� by hanwei 2004-4-07
					voaMyItem[row].setBarCodeVOs(null);
					voaMyItem[row].setAttributeValue(IItemKey.NBARCODENUM, new UFDouble(0.0));
					voaMyItem[row].setBarcodeClose(new nc.vo.pub.lang.UFBoolean('N'));
					voaMyItem[row].setAttributeValue(IItemKey.NKDNUM, null);
					// zhy2005-0-27���Ƶ���ʱ��ձ���������Դ���ݺŵ���Ϣ
					voaMyItem[row].setAttributeValue("csrc2billtype", null);// ������Դ��������
					voaMyItem[row].setAttributeValue("csrc2billhid", null);// ������Դ��ID
					voaMyItem[row].setAttributeValue("vsrc2billcode", null);// ������Դ���ݺ�
					voaMyItem[row].setAttributeValue("csrc2billbid", null);// ������Դ����ID
					voaMyItem[row].setAttributeValue("vsrc2billrowno", null);// ������Դ���к�

					voaMyItem[row].setAttributeValue("csumid", null);// VMI����ID
					voaMyItem[row].setSerial(null);
					if (!m_bIsInitBill) voaMyItem[row].setDbizdate(dcurdate);

				}
				setBillVO(m_voBill);
				// ������������

				// ���õ�ǰ������������ ���� 2004-04-05
				if (m_utfBarCode != null) m_utfBarCode.setCurBillItem(null);

				nc.ui.pub.bill.BillModel bmTemp = getBillCardPanel().getBillModel();

				bmTemp.setNeedCalculate(false);
				for (int row = 0; row < iRowCount; row++) {
					// ������״̬Ϊ����
					if (bmTemp != null) {
						bmTemp.setRowState(row, nc.ui.pub.bill.BillModel.ADD);
						bmTemp.setValueAt(null, row, m_sBillRowItemKey);
					}

				}
				bmTemp.setNeedCalculate(true);

				// �����������ݵĳ�ʼ���ݣ������ڣ��Ƶ��˵ȡ�
				setNewBillInitData();
				getBillCardPanel().setEnabled(true);
				m_iMode = BillMode.New;
				// ���õ��ݺ��Ƿ�ɱ༭
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
			// ����Դ��ѯģ��
			// dlgQry.setParent(this);
			dlgQry.setTempletID(m_sCorpID, "40080608", m_sUserID, null, "40089908");

			dlgQry.initData(m_sCorpID, m_sUserID, "40089908", null, "4I", "4A", null);
			if (dlgQry.showModal() == nc.ui.pub.beans.MessageDialog.ID_OK) {

				// ��Ҫע��
				nc.vo.pub.query.ConditionVO[] voCons = dlgQry.getConditionVO();

				// ��ȡ��ѯ����
				StringBuffer sWhere = new StringBuffer(" 1=1 ");
				if (voCons != null && voCons.length > 0 && voCons[0] != null) {
					sWhere.append(" and " + dlgQry.getWhereSQL(voCons));
				}

				// ����Դ���նԻ���
				dlgBill.initVar("cgeneralhid", m_sCorpID, m_sUserID, null, sWhere.toString(), "4A", null, null, "4I", null, this);

				dlgBill.setStrWhere(sWhere.toString());
				dlgBill.getBillVO();
				dlgBill.loadHeadData();
				dlgBill.addBillUI();
				dlgBill.setQueyDlg(dlgQry);

				nc.vo.scm.pub.ctrl.GenMsgCtrl.printHint("will load qrybilldlg");
				if (dlgBill.showModal() == nc.ui.pub.beans.MessageDialog.ID_OK) {
					nc.vo.scm.pub.ctrl.GenMsgCtrl.printHint("qrybilldlg closeok");
					// ��ȡ��ѡVO
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

					// ���ƽ���
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
		m_timer.start("ɾ����ʼ");

		nc.vo.ic.pub.bill.Timer t = new nc.vo.ic.pub.bill.Timer();
		t.start();

		int iSelListHeadRowCount = getBillListPanel().getHeadTable().getSelectedRowCount();
		if (iSelListHeadRowCount <= 0) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UCH003")/* @res "��ѡ��Ҫ��������ݣ�" */);
			return;
		}

		switch (nc.ui.pub.beans.MessageDialog.showYesNoDlg(this, null, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH002")/*
																																		 * @res
																																		 * "�Ƿ�ȷ��Ҫɾ����"
																																		 */
		, MessageDialog.ID_NO)) {

		case nc.ui.pub.beans.MessageDialog.ID_YES:
			break;
		default:
			return;
		}
		// ���б��£����ŵ���ɾ��;�ڱ���ʽ��,ֻ��ÿ��ɾ��һ�ŵ���,��Ϊ�����б���ѡ�еĵ��ݱ���
		// ͬ�������Կ���ͳһ����
		// GeneralBillVO voaDeleteBill[] = new
		// GeneralBillVO[iSelListHeadRowCount];
		// GeneralBillItemVO voaItem[] = null;
		ArrayList alvo = getSelectedBills();
		GeneralBillVO voaDeleteBill[] = new GeneralBillVO[alvo.size()];

		// ����Ա��־
		nc.vo.sm.log.OperatelogVO log = getNormalOperateLog();

		for (int i = 0; i < alvo.size(); i++) {
			voaDeleteBill[i] = (GeneralBillVO) alvo.get(i);
			// ��ǰ����Ա2002-04-10.wnj
			voaDeleteBill[i].getHeaderVO().setCoperatoridnow(m_sUserID);
			voaDeleteBill[i].getHeaderVO().setAttributeValue("clogdatenow", m_sLogDate);
			// ���ڡ�������Ϣ
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
	 * �����ߣ����˾� ���ܣ�ɾ������ ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	private void onDeleteKernel(GeneralBillVO[] voaDeleteBill) {
		m_timer.start("ɾ����ʼ");

		nc.vo.ic.pub.bill.Timer t = new nc.vo.ic.pub.bill.Timer();
		t.start();

		try {
			int iSelListHeadRowCount = getBillListPanel().getHeadTable().getSelectedRowCount();

			int[] arySelListHeadRows = new int[iSelListHeadRowCount];
			arySelListHeadRows = getBillListPanel().getHeadTable().getSelectedRows();

			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH051")/* @res "����ɾ��" */);

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
																																			* ".\nȨ��У�鲻ͨ��,����ʧ��! "
																																			*/);
						getAccreditLoginDialog().setCorpID(m_sCorpID);
						getAccreditLoginDialog().clearPassWord();
						if (getAccreditLoginDialog().showModal() == nc.ui.pub.beans.UIDialog.ID_OK) {
							String sUserID = getAccreditLoginDialog().getUserID();
							if (sUserID == null) {
								throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000070")/*
																																	* @res
																																	* "Ȩ��У�鲻ͨ��,����ʧ��. "
																																	*/);
							} else {
								for (int i = 0; i < voaDeleteBill.length; i++)
									voaDeleteBill[i].setAccreditUserID(sUserID);
								continue;
							}
						} else {
							throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000070")/*
																																* @res
																																* "Ȩ��У�鲻ͨ��,����ʧ��. "
																																*/);

						}
					} else if (realbe != null && realbe.getClass() == CreditNotEnoughException.class) {
						// ������Ϣ��ʾ����ѯ���û����Ƿ��������
						int iFlag = showYesNoMessage(realbe.getMessage() + " \r\n" + "�Ƿ������");
						// ����û�ѡ�����
						if (iFlag == nc.ui.pub.beans.MessageDialog.ID_YES) {
							for (int i = 0; i < voaDeleteBill.length; i++)
								voaDeleteBill[i].setIsCheckCredit(false);
							continue;
						} else return;
					} else if (realbe != null && realbe.getClass() == PeriodNotEnoughException.class) {
						// ������Ϣ��ʾ����ѯ���û����Ƿ��������
						int iFlag = showYesNoMessage(realbe.getMessage() + " \r\n" + "�Ƿ������");
						// ����û�ѡ�����
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
							// ������Ϣ��ʾ����ѯ���û����Ƿ��������
							int iFlag = showYesNoMessage(atpe.getMessage() + " \r\n" + "�Ƿ������");
							// ����û�ѡ�����
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

			m_timer.showExecuteTime("ƽ̨ɾ��");
			// showTime(lTime, "ɾ��");

			// ɾ�����б���洦��,����m_iLastSelListHeadRow��
			removeBillsOfList(arySelListHeadRows);
			m_timer.showExecuteTime("removeBillsOfList");
			// �ڱ���ʽ��ɾ������ʾ���б���Ӧ�ĵ���
			if (BillMode.Card == m_iCurPanel && m_iLastSelListHeadRow >= 0 && m_alListData != null && m_alListData.size() > m_iLastSelListHeadRow) {
				setBillVO((GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow));
				// ִ�й�ʽ
				// getBillCardPanel().getBillModel().execLoadFormula();
				// getBillCardPanel().updateValue();
			} else if (m_alListData == null || m_alListData.size() == 0) {
				getBillCardPanel().getBillModel().clearBodyData();
				getBillCardPanel().updateValue();
			}
			// ��յ�ǰ�Ļ�λ����
			m_alLocatorData = null;
			m_alLocatorDataBackup = null;

			// ��յ�ǰ�����к�����
			m_alSerialData = null;
			m_alSerialDataBackup = null;
			m_timer.showExecuteTime("Before ���谴ť״̬");
			// ���谴ť״̬
			setButtonStatus(false);
			m_timer.showExecuteTime("���谴ť״̬");
			// �����Ƿ�Ϊ��Դ���ݿ��Ƶ��ݽ���
			ctrlSourceBillUI(true);
			m_timer.showExecuteTime("��Դ���ݿ��Ƶ��ݽ���");

			// delete the excel barcode file
			OffLineCtrl ctrl = new OffLineCtrl(this);
			ctrl.deleteExcelFile(voaDeleteBill, getCorpPrimaryKey());

			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "ɾ���ɹ�")/* @res "ɾ���ɹ�" */);
		} catch (Exception e) {
			handleException(e);
			// showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
			// "4008bill", "UPP4008bill-000011")/* @res "ɾ������" */);
			showWarningMessage(e.getMessage());
		}
		t.stopAndShow("ɾ���ϼ�");
	}

	/**
	 * �����ߣ����˾� ���ܣ�����ʽ��ɾ���д��� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	public void onDeleteRow() {
		int[] selrow = getBillCardPanel().getBillTable().getSelectedRows();
		int length = selrow.length;
		SCMEnv.out("count is " + length);
		int iCurLine = 0; // ��ǰѡ����

		// ɾ���������� add by hanwei
		if (m_voBill != null && selrow != null) {
			for (int i = 0; i < selrow.length; i++) {
				if (m_voBill.getItemBarcodeVO(selrow[i]) != null) {
					m_utfBarCode.setRemoveBarcode(m_voBill.getItemBarcodeVO(selrow[i]));
				}
			}
		}

		if (length == 0) { // ûѡ��һ�У�����
			return;
		} else if (length == 1) { // ɾ��һ��
			int allrownums = getBillCardPanel().getRowCount();
			getBillCardPanel().delLine();
			if (selrow[0] + 1 > allrownums) {
				int iRowCount = getBillCardPanel().getRowCount();
				if (iRowCount > 0) {
					getBillCardPanel().getBillTable().setRowSelectionInterval(selrow[0], selrow[0]);
					iCurLine = selrow[0];

				}
			}
		} else { // ɾ������

			getBillCardPanel().getBillModel().delLine(selrow);

			int iRowCount = getBillCardPanel().getRowCount();
			if (iRowCount > 0) {
				getBillCardPanel().getBillTable().setRowSelectionInterval(0, 0);
				iCurLine = 0;
			}
		}
		// �������к��Ƿ����
		setBtnStatusSN(iCurLine, true);

		// ɾ���������ݣ�����
	}

	/**
	 * �����ߣ����˾� ���ܣ���λ���� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
	 * �������ݵ��б�,��ִ��ǰ20�����ݵĹ�ʽ,����Ĭ��ѡ�����õ���һ��������,���ò˵�״̬
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
	 * �������ݵ��б�,��ִ��ǰ20�����ݵĹ�ʽ,����Ĭ��ѡ�����õ���һ��������,���ò˵�״̬
	 * 
	 * @since v5
	 * @author ljun
	 * @param alData
	 *            ���ݼ���
	 * @param bQuery
	 *            �Ƿ��ѯ����, �ǲ�ѯ����Ϊtrue, ����Ϊfalse
	 */
	public void setDataOnList(ArrayList alData, boolean bQuery) {

		m_timer.start();
		setAlistDataByFormula(GeneralBillVO.QRY_FIRST_ITEM_NUM, alData);
		m_timer.showExecuteTime("@@setAlistDataByFormula��ʽ����ʱ�䣺");

		// ִ����չ��ʽ.Ŀǰֻ�����۳��ⵥUI����.
		execExtendFormula(alData);

		if (alData != null && alData.size() > 0) {
			m_alListData = alData;
			setListHeadData();
			// ���õ�ǰ�ĵ�������/��ţ����ڰ�ť����
			setLastHeadRow(0);
			// ��ǰ�Ǳ���ʽʱ�������л����б���ʽ
			if (BillMode.Card == m_iCurPanel) onSwitch();

			// ȱʡ��ͷָ���һ�ŵ���
			selectListBill(0);

			// ��ʼ����ǰ������ţ��л�ʱ�õ������������������ñ������ݡ�
			m_iCurDispBillNum = -1;
			// ��ǰ������
			m_iBillQty = m_alListData.size();

			if (bQuery) {
				String[] args = new String[1];
				args[0] = String.valueOf(m_iBillQty);
				String message = nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000339", null, args);

				/* @res "���鵽{0}�ŵ��ݣ�" */

				if (m_iBillQty > 0) showHintMessage(message);
				else showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000013")/*
																													* @res
																													* "δ�鵽���������ĵ��ݡ�"
																													*/);

			}
			// ��������Դ���ݵİ�ť���˵��ȵ�״̬��
			ctrlSourceBillUI(false);

		} else {
			dealNoData();
		}

		setButtonStatus(true);
	}

	/**
	 * 
	 * ����������������ѯ��ˢ�°�ť�ķ�����
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * ��ѯ��ť��onQuery(true);
	 * <p>
	 * ˢ�°�ť��onQuery(false);
	 * <p>
	 * <b>����˵��</b>
	 * 
	 * @param bQuery
	 *            ����ǽ��в�ѯ��Ϊtrue������ǽ���ˢ�£�Ϊfalse
	 *            <p>
	 * @author duy
	 * @time 2007-3-2 ����03:07:52
	 */
	public void onQuery(boolean bQuery) {
		// Ϊ�˼�����ǰ�İ汾�������˳�Ա�������洢�ǽ��в�ѯ���ǽ���ˢ��
		m_bQuery = bQuery;

		// ִ�в�ѯ��ˢ�£����̣��ڸ÷����н����˲�ѯ��ˢ�µķֱ���
		onQuery();
	}

	/**
	 * �����ߣ����˾� ���ܣ���ѯ���� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� ����
	 * 2003-06-24 ע�ͣ� �����Ѿ���
	 * nc.ui.ic.pub.bill.GeneralBillClientUI.setListHeadData(GeneralBillHeaderVO
	 * nc.ui.ic.pub.bill.GeneralBillClientUI.setAlistDataByFormula(int,
	 * ArrayList) ִ���˱�ͷ�����幫ʽ�������в����ڴ������ظ�ִ�����´��룺
	 * getBillListPanel().getHeadBillModel().execLoadFormula();
	 * getBillListPanel().getBodyBillModel().execLoadFormula();
	 * 
	 * @deprecated Since version 5.1, ʹ��onQuery(boolean bQuery)
	 */
	public void onQuery() {
		// DUYONG ���Ӳ�ѯ��ˢ�µĴ���
		try {
			nc.vo.ic.pub.bill.Timer timer = new nc.vo.ic.pub.bill.Timer();
			timer.start("@@��ѯ��ʼ��");
			m_sBnoutnumnull = null;
			QryConditionVO voCond = null;

			// ��ԭ�������ͣ���Ƭ/�б���¼����������ڿ�Ƭ����ִ����ˢ�£��������л����б����
			int cardOrList = m_iCurPanel;
			// �����[(1)���в�ѯ(2)����û�н��в�ѯ�����ǵ����ˢ�°�ť]������ʾ��ѯģ����в�ѯ
			// ���������ѯ�������ҵ����ˢ�°�ť���������˶δ���
			if (m_bQuery || !m_bEverQry) {
				getConditionDlg().showModal();
				timer.showExecuteTime("@@getConditionDlg().showModal()��");

				if (getConditionDlg().getResult() != nc.ui.pub.beans.UIDialog.ID_OK)
				// ȡ������
				return;

				// ���qrycontionVO�Ĺ���
				voCond = getQryConditionVO();

				// ��¼��ѯ��������
				m_voLastQryCond = voCond;

				// ����ǽ��в�ѯ���򽫡�������ѯ�����ı�ʶ���ó�true�����������ܹ����С�ˢ�¡��Ĳ�����
				m_bEverQry = true;
				// ˢ�°�ť
				setButtonStatus(true);
			} else voCond = m_voLastQryCond;

			// DUYONG �˴�Ӧ����ˢ�°�ť���ܵĿ�ʼִ�д�

			// ���ʹ�ù�ʽ����봫voaCond ����̨. �޸� zhangxing 2003-03-05
			nc.vo.pub.query.ConditionVO[] voaCond = getConditionDlg().getConditionVO();

			voCond.setParam(QryConditionVO.QRY_CONDITIONVO, voaCond);

			voCond.setIntParam(0, GeneralBillVO.QRY_HEAD_ONLY_PURE);

			if (m_sBnoutnumnull != null) {
				// �Ƿ����ʵ������
				voCond.setParam(33, m_sBnoutnumnull);
			}

			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000012")/* @res "���ڲ�ѯ�����Ժ�..." */);
			timer.showExecuteTime("Before ��ѯ����");

			ArrayList alListData = (ArrayList) GeneralBillHelper.queryBills(m_sBillTypeCode, voCond);

			timer.showExecuteTime("��ѯʱ�䣺");

			setDataOnList(alListData, true);

			// DUYONG ��ִ��ˢ�²��������ҵ�ǰ����Ϊ��Ƭ����ʱ����Ӧ���л����б����͵Ľ�����
			if (!m_bQuery && m_iCurPanel != cardOrList) {
				onSwitch();
			}
		} catch (Exception e) {
			handleException(e);
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000014")/* @res "��ѯ����" */);
			showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000015")/* @res "��ѯ����" */
					+ e.getMessage());
		}
	}

	/**
	 * �����ߣ����˾� ���ܣ����кŷ��� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	public void onSNAssign() {
		nc.vo.ic.pub.bill.Timer timer = new nc.vo.ic.pub.bill.Timer();
		timer.start("@@���кŷ��俪ʼ��");
		// �����ʽ���Ȳ�ѯ��λ/���кţ���ȻqryLocSN��ÿ�ŵ���ֻ��һ�ο⡣
		// if (BillMode.Browse == m_iMode) {
		qryLocSN(m_iLastSelListHeadRow, QryInfoConst.LOC_SN);
		// timer.showExecuteTime("@@�����ʽ�²�ѯ��λ/���кŲ�ѯʱ�䣺");
		// }
		// ��ǰѡ�е���
		int iCurSelBodyLine = -1;
		if (BillMode.Card == m_iCurPanel) {
			iCurSelBodyLine = getBillCardPanel().getBillTable().getSelectedRow();
			if (iCurSelBodyLine < 0) {
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000016")/*
																												* @res "��ѡ��Ҫ�������кŷ�����С�"
																												*/);
				return;
			}
		} else {
			iCurSelBodyLine = getBillListPanel().getBodyTable().getSelectedRow();
			if (iCurSelBodyLine < 0) {
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000017")/*
																												* @res "��ѡ��Ҫ�鿴���кŵ��С�"
																												*/);
				return;
			}
		}
		InvVO voInv = null;
		// �ֿ�
		WhVO voWh = null;
		// ����VO,������m_voBill���б��¶���m_alListData.
		GeneralBillVO voBill = null;

		// ���״̬�²쿴���ȶ����ݣ�����һ��Ҫ����setInitVOs֮ǰ

		// ������vo,�����б��»��Ǳ���
		// ����ʽ��
		if (BillMode.Card == m_iCurPanel) {
			if (m_voBill == null || m_voBill.getItemCount() <= 0) {
				SCMEnv.out("bill null E.");
				return;
			}
			voBill = m_voBill;
			// ����VO
			GeneralBillItemVO voItem = getBodyVO(iCurSelBodyLine);
			// ����VO
			GeneralBillItemVO voItemPty = voBill.getItemVOs()[iCurSelBodyLine];
			// �ϲ�
			if (voItemPty != null) voItemPty.setIDItems(voItem);
			// �����Ĵ��������
			if (voItem != null) voInv = voItemPty.getInv();

			if (voBill != null) voWh = voBill.getWh();
			// ����Ƿ����ģʽ�����Ҳֿ��ǻ�λ�������������ʱӦ�߻�λ��
			if (m_iMode != BillMode.Browse) {
				if (voWh != null && voWh.getIsLocatorMgt() != null && voWh.getIsLocatorMgt().intValue() == 1 && voItem.getInOutFlag() == InOutFlag.IN && m_voBill.getItemValue(iCurSelBodyLine, "cspaceid") == null) {
					nc.ui.pub.beans.MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPPSCMCommon-000270")/*
																																					* @res "��ʾ"
																																					*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000018")/*
																																																										* @res
																																																										* "���ʱ���кŷ�������ִ�С���λ���䡱���ڻ�λ������ִ�С����кš�����"
																																																										*/);
					return;

				}
			}
			// //����Ƿ����ģʽ�����Ҳֿ��ǻ�λ�������������ʱӦ�߻�λ��
			// if (m_iMode != BillMode.Browse) {
			// if (voWh != null
			// && voWh.getIsLocatorMgt() != null
			// && voWh.getIsLocatorMgt().intValue() == 1
			// && voItem.getInOutFlag() == InOutFlag.IN) {
			// nc.ui.pub.beans.MessageDialog.showHintDlg(
			// this,
			// "��ʾ",
			// "���ʱ���кŷ�������ִ�С���λ���䡱���ڻ�λ������ִ�С����кš���");
			// return;

			// }
			// }

		} else
		// �б���ʽ�²쿴���ȶ����ݣ�����һ��Ҫ����setInitVOs֮ǰ
		if (m_iLastSelListHeadRow >= 0 && m_iLastSelListHeadRow < m_alListData.size()) {
			// ������еĻ�λ����ȸ�������
			voBill = (GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow);
			if (voBill == null) {
				SCMEnv.out("bill null E.");
				return;
			}
			voInv = voBill.getItemInv(iCurSelBodyLine);
			// warehouse
			voWh = voBill.getWh();
			// ��˾PK
			voWh.setPk_corp(m_sCorpID);
		}

		if (voInv != null && voBill != null) {
			// �����ϵ�ǰ���ݵ�PK.
			voInv.setCgeneralhid(voBill.getHeaderVO().getPrimaryKey());
			String csrctype = voBill.getItemVOs()[iCurSelBodyLine].getCsourcetype();
			if ("A3".equals(csrctype)) voInv.setCfreezeid(voBill.getItemVOs()[iCurSelBodyLine].getCsourcebillbid());
			else if (csrctype != null && (csrctype.startsWith("5") || csrctype.startsWith("30"))) voInv.setCfreezeid(voBill.getItemVOs()[iCurSelBodyLine].getCfirstbillbid());
			// ��Ӧ����ⵥ�����У�����Ϊnull��
			// �����������ⲻ��Ϊnull
			String sCorrespondBodyItemPK = voInv.getCcorrespondbid();
			// �����Ƿ�������
			// zhx
			if (getIsInvTrackedBill(voInv) && voInv.getInOutFlag() == InOutFlag.OUT && (sCorrespondBodyItemPK == null || sCorrespondBodyItemPK.trim().length() == 0)) {
				showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000019")/*
																												* @res
																												* "����ָ����Ӧ��ⵥ��Ȼ�����ԡ�"
																												*/);
				return;
			}

			// �����ģʽ����������Ϊ��
			if (BillMode.Card == m_iCurPanel && (BillMode.Update == m_iMode || BillMode.New == m_iMode)) {
				Object oQty = voInv.getAttributeValue(m_sNumItemKey);
				if (oQty == null || oQty.toString().trim().length() == 0) {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000020")/*
																													* @res
																													* "��������������"
																													*/);
					return;
				}
				// �������벻��С�� by hanwei 2003-07-20
				if (nc.vo.ic.pub.check.VOCheck.checkIsDecimal(oQty.toString())) {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000021")/*
																													* @res
																													* "���кŹ���������������С����"
																													*/);
					return;
				}

			}

			// if (BillMode.Browse == m_iMode)
			// //������еĻ�λ����ȸ�������
			// resetBodyAssistData(m_iLastSelListHeadRow);
			// ydy2002-12-19
			LocatorVO aLoc = null;
			LocatorVO[] voLocs = null;
			if (m_alLocatorData != null && m_alLocatorData.size() > 0) voLocs = (LocatorVO[]) m_alLocatorData.get(iCurSelBodyLine);

			if (voLocs == null || voLocs.length != 1) {
				// showHintMessage("��λ��������");
				aLoc = new LocatorVO();

			} else aLoc = voLocs[0];
			// end ydy
			// -������ǻ�λ�ֿ�����к���⣻�������к�
			getSerialAllocationDlg().setDataVO(m_iBillInOutFlag, voWh, aLoc, voInv, m_iMode, (SerialVO[]) m_alSerialData.get(iCurSelBodyLine), voLocs);

			// ��ǰ�����к���

			int result = getSerialAllocationDlg().showModal();
			if (nc.ui.pub.beans.UIDialog.ID_OK == result && (BillMode.New == m_iMode || BillMode.Update == m_iMode)) {
				SerialVO voaSN[] = getSerialAllocationDlg().getDataSNVO();
				// ���������
				m_alSerialData.set(iCurSelBodyLine, voaSN);
				// if (voaSN != null)
				// for (int i = 0; i < voaSN.length; i++)
				// SCMEnv.out("ret sn[" + i + "] is" +
				// voaSN[i].getVserialcode());
				// ����ǳ���Ļ����������л�λ��������
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
						// �������кŲ�������û��ë��,�˴���������һ��
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

					// ���浽��Ӧ����
					m_alLocatorData.set(iCurSelBodyLine, voaLoc);
					setBodySpace(iCurSelBodyLine);
					// д�����λ
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
	 * �����ߣ����˾� ���ܣ���λ���� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	public void onSpaceAssign() {
		// �����ʽ���Ȳ�ѯ��λ/���кţ���ȻqryLocSN��ÿ�ŵ���ֻ��һ�ο⡣
		// m_dlgSpaceAllocation = null;

		// if (BillMode.Browse == m_iMode)
		qryLocSN(m_iLastSelListHeadRow, QryInfoConst.LOC_SN);

		// ������һ�η��䣬ʵ���Ͽ���֧��ѡ�з���ġ�
		// ����ѡ�������кź�ʣ������λ������о�Ҫѡ�д���
		// ���������������ݶ�Ӧ���кţ��������ݺ�set����Ӧ��λ�ü��ɡ�

		InvVO[] voInv = null;
		// �ֿ�
		WhVO voWh = null;

		// ����ʽ��
		if (BillMode.Card == m_iCurPanel) {
			// ��ȥ����
			if (BillMode.Update == m_iMode || BillMode.New == m_iMode) filterNullLine();
			if (m_voBill != null) {
				// ����VO
				GeneralBillItemVO voItemPty[] = m_voBill.getItemVOs();
				// ID
				GeneralBillItemVO voItemID[] = getBodyVOs();
				voInv = new InvVO[m_voBill.getItemCount()];
				// �ϲ����ԡ�ID
				for (int row = 0; row < m_voBill.getItemCount(); row++) {
					voItemPty[row].setIDItems(voItemID[row]);
					// �����Ե�InvVO
					voInv[row] = voItemPty[row].getInv();
				}
				voWh = m_voBill.getWh();
			}
		} else
		// �б���ʽ�²쿴���ȶ����ݣ�����һ��Ҫ����setInitVOs֮ǰ
		if (m_iLastSelListHeadRow >= 0 && m_iLastSelListHeadRow < m_alListData.size()) {
			// ������еĻ�λ����ȸ�������
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
		// ������еĻ�λ����ȸ�������
		// resetBodyAssistData(m_iLastSelListHeadRow);
		// else
		// ����ĵ�����ʱ����ִ�С����кš��������кŽ����ϻ��ܻ�λ��
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000022")/*
																										* @res
																										* "����ĵ�����ʱ����ִ�С����кš��������кŽ����ϻ��ܻ�λ��"
																										*/);

		getSpaceAllocationDlg().setData(m_iBillInOutFlag, voWh, voInv, m_iMode, m_alLocatorData, m_alSerialData);
		int result = getSpaceAllocationDlg().showModal();

		if (nc.ui.pub.beans.UIDialog.ID_OK == result && (BillMode.New == m_iMode || BillMode.Update == m_iMode)) {
			ArrayList alRes = getSpaceAllocationDlg().getDataSpaceVO();
			// ����
			// ��������к�,������ؽ����ĳ��element��null,��ʾ�ǳ���Ĵ������ͨ����λ�������������кš����Բ����õ����к����ݡ�
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
			// ���û�λ
			if (alRes != null && alRes.size() > 0) {
				// �����Ҫ����ʼ��֮
				if (m_alLocatorData == null) m_alLocatorData = new ArrayList(alRes.size());
				for (int i = 0; i < alRes.size(); i++) {
					LocatorVO voLoc[] = (LocatorVO[]) alRes.get(i);
					// ֻ���������ݵģ���Ϊ���к�Ҳ���޸Ļ�λ����
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
	 * �����ߣ����˾� ���ܣ���/�б���ʽ�л� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * v5: ֧���б��µĶ��ŵ��ݲ�������ʱ���޸İ�ť��˫����ͷ������onSwitch�� ����Ϊ��������
	 * 
	 * 
	 */
	public void onSwitch() {

		if (BillMode.List == m_iCurPanel) {
			// �б�---�����л�
			m_iCurPanel = BillMode.Card;

			// ����������Ϊ��
			m_sLastKey = null;

			getButtonTree().getButton(ICButtonConst.BTN_SWITCH).setHint(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000068")/* @res "�л����б���ʽ" */);
			updateButton(getButtonTree().getButton(ICButtonConst.BTN_SWITCH));

			// ���б�---�����л�ʱ��δ��ѡ�������ݣ���������������ݣ������Ч�ʡ�
			if (m_sLastKey != null || m_iLastSelListHeadRow >= 0
			// && m_iCurDispBillNum != m_iLastSelListHeadRow
			&& m_alListData != null && m_alListData.size() > m_iLastSelListHeadRow && m_alListData.get(m_iLastSelListHeadRow) != null) {

				// zhx new add a varient to store the ini vocher VO
				m_voBillRefInput = (GeneralBillVO) ((GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow)).clone();
				m_voBill = (GeneralBillVO) ((GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow)).clone();

				// v5 lj ֧�ֲ������ɶ൥��
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

				// ��ǰ�������
				m_iCurDispBillNum = m_iLastSelListHeadRow;
				// ��ǰ������
				m_iBillQty = m_alListData.size();

				setCardMode();
			}
		} else {
			// ��--->�б��л�
			m_iCurPanel = BillMode.List;
			// ��ǰ�������
			m_iCurDispBillNum = m_iLastSelListHeadRow;

			selectListBill(m_iLastSelListHeadRow);

			getButtonTree().getButton(ICButtonConst.BTN_SWITCH).setHint(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000312")/* @res "�л�������ʽ" */);
			updateButton(getButtonTree().getButton(ICButtonConst.BTN_SWITCH));

			// v5 lj
			if (m_bRefBills == true) {
				m_iMode = BillMode.Browse;// ����Ϊ���״̬
				// �����ǰ�б��µĵ���Ϊ0�������ɾ���ģ������޸�m_bRefBills=false
				if (m_alListData == null || m_alListData.size() == 0) {
					setRefBillsFlag(false);
				}
			}
		}

		showBtnSwitch();

		// ������ʾ
		m_layoutManager.show();

		setButtonStatus(false);
		// �����Դ���ݲ�Ϊ�գ�����ҵ�����͵Ȳ�����
		ctrlSourceBillUI(false);
		ctrlSourceBillButtons(true);
	}

	/**
	 * �����ߣ����˾� ���ܣ��޸Ĵ��� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH027")/* @res "�����޸�" */);
		// �����кż���λ--�����Ҫ�Ļ���
		qryLocSN(m_iLastSelListHeadRow, QryInfoConst.LOC_SN);
		GeneralBillVO voMyBill = null;
		// arraylist �еĻ�������,û�л�,�����µ�.
		if (m_alListData != null && m_alListData.size() > m_iLastSelListHeadRow) voMyBill = (GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow);
		// ��Ҫ��ԭ�������ݵ�clone�������ڱ༭�����У���ͬ���޸�ԭ�������ݡ�
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

		// ��ǰ���б���ʽʱ�������л�������ʽ
		if (BillMode.List == m_iCurPanel) onSwitch();

		// ��ǰ����ʾ���ݣ�����Ϊ�ɱ༭��ʽ���޵�����ʾʱ�˰�ť�����á�
		if (BillMode.Browse == m_iMode) {
			getBillCardPanel().updateValue();
			getBillCardPanel().setEnabled(true);

			// v5 lj
			if (m_bRefBills == true) m_iMode = BillMode.New;
			else m_iMode = BillMode.Update;

			setButtonStatus(false);
		}
		// ���浱ǰ�Ļ�λ���ݣ��Է�ȡ�������� useless in fact
		m_alLocatorDataBackup = m_alLocatorData;
		// ���浱ǰ�����к����ݣ��Է�ȡ�������� useless in fact
		m_alSerialDataBackup = m_alSerialData;
		// �����Դ���ݲ�Ϊ�գ�����ҵ�����͵Ȳ�����

		ctrlSourceBillUI(false);
		// �޸�״̬ʱ���ֿⲻ���޸ġ�
		if (getBillCardPanel().getHeadItem(m_sMainWhItemKey) != null) getBillCardPanel().getHeadItem(m_sMainWhItemKey).setEnabled(false);
		if (getBillCardPanel().getHeadItem(m_sWasteWhItemKey) != null) getBillCardPanel().getHeadItem(m_sWasteWhItemKey).setEnabled(false);
		// �޸�״̬ʱ�������֯�����޸ġ�
		if (getBillCardPanel().getHeadItem(m_sMainCalbodyItemKey) != null) getBillCardPanel().getHeadItem(m_sMainCalbodyItemKey).setEnabled(false);
		if (getBillCardPanel().getHeadItem(m_sMainCalbodywasteItemKey) != null) getBillCardPanel().getHeadItem(m_sMainCalbodywasteItemKey).setEnabled(false);
		if (getBillCardPanel().getHeadItem("vbillcode") != null) getBillCardPanel().getHeadItem("vbillcode").setEnabled(false);

		// ���°�Ŧ
		updateButtons();

		// ���ݱ�ͷ�˿��־��ȷ���˿�״̬���͵������˿�UI add by hanwei 2003-10-19
		nc.ui.ic.pub.bill.GeneralBillUICtl.setSendBackBillState(this);
		// showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill",
		// "UPP4008bill-000023")/* @res "����@" */);
		// Ĭ�ϲ��ǵ������� add by hanwei 2003-10-30
		m_bIsImportData = false;

		// if current bill is barcode manage, check if flag is "open", then set
		// the file's flag to "close"; when save, first check
		OffLineCtrl ctrl = new OffLineCtrl(this);
		ctrl.onUpdateBill(voMyBill, getCorpPrimaryKey());

		setUpdateBillInitData();
	}

	/**
	 * �����ߣ����˾� ���ܣ�ɾ�������б���洦�� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	protected void removeBillsOfList(int[] iaDelLines) {

		if (iaDelLines != null && iaDelLines.length > 0) {
			// ɾ�������ϵ�����
			getBillListPanel().getHeadBillModel().delLine(iaDelLines);
			for (int i = iaDelLines.length - 1; i >= 0; i--)
				// ��m_alListData��ɾ��
				if (m_alListData != null && m_alListData.size() > iaDelLines[i]) m_alListData.remove(iaDelLines[i]);

			// ��������
			if (m_alListData != null) m_iBillQty = m_alListData.size();
			else {
				m_alListData = new ArrayList();
				m_iBillQty = 0;
			}
			// ���ɾ�������һ�ţ�����ͬʱɾ�����ţ���ָ���һ�ţ�
			// ���ֻɾ��������һ�ţ���ָ����һ��,��Ӧ����m_iLastSelListHeadRow
			if (m_iBillQty > 0) {
				if (m_iLastSelListHeadRow >= m_iBillQty || iaDelLines.length > 1) setLastHeadRow(0);
				// ѡ���б���
				selectListBill(m_iLastSelListHeadRow);

			} else { // ȫɾ���ˣ������
				m_alListData = new ArrayList();
				setLastHeadRow(-1);
				m_iCurDispBillNum = -1;
				m_iBillQty = 0;
				// ����б�
				getBillListPanel().getHeadBillModel().clearBodyData();
				getBillListPanel().getBodyBillModel().clearBodyData();
				// ��ձ�
				getBillCardPanel().getBillData().clearViewData();
			}

		}
	}

	/**
	 * �����ߣ����˾� ���ܣ����ñ��帨��--��λ/���к� ������ iBillNum��������� ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	protected void resetBodyAssistData(int iBillNum) {
		// ������еĻ�λ��������
		// δ��ѡ�������ݣ��������������ݣ������Ч�ʡ�
		if (iBillNum >= 0 && m_alListData != null && m_alListData.size() > iBillNum && m_alListData.get(iBillNum) != null) { // ���õ�ǰ�Ļ�λ����
			m_alLocatorData = new ArrayList();
			// ���õ�ǰ�����к�����
			m_alSerialData = new ArrayList();
			// ����
			GeneralBillVO hvo = (GeneralBillVO) m_alListData.get(iBillNum);
			if (hvo != null) { // ������
				GeneralBillItemVO ivo[] = (GeneralBillItemVO[]) hvo.getChildrenVO();
				// ��λ����
				LocatorVO[] lvo = null;
				// ���к�����
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
	 * �����ߣ����˾� ���ܣ�ѡ���б���ʽ�µĵ�sn�ŵ��� ������sn �������
	 * 
	 * ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 */
	public void selectListBill(int sn) {
		nc.vo.ic.pub.bill.Timer timer = new nc.vo.ic.pub.bill.Timer();
		timer.start("@@����selectListBill��ʼ");
		if (sn < 0 || m_alListData == null || sn >= m_alListData.size() || m_alListData.get(sn) == null || getBillListPanel().getHeadTable().getRowCount() <= 0) {
			SCMEnv.out("sn error,or list null");
			return;
		}
		// ѡ�б�ͷ��
		getBillListPanel().getHeadTable().changeSelection(sn, 0, false, false);
		getBillListPanel().getHeadTable().setRowSelectionInterval(sn, sn);
		// ��Ӧ�ı�������
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
		// zhy �����㻻���ʺ���������
		// // ��Ҫ�Ļ�������ǹ̶�������
		// voBill.calConvRate();
		// // ��Ҫ�Ļ���������������
		// voBill.calPrdDate();

		voi = voBill.getItemVOs();
		// ����Ƿ��б��壬���û����ʾ���ݿ��ܱ�ɾ����,���������ء�
		if (voi == null || voi.length <= 0) {
			showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000024")/*
																											* @res "δ�ҵ�������Ϣ�����ܵ����ѱ�ɾ����"
																											*/);
		}
		// ִ�����ε�����ʽ
		BatchCodeDefSetTool.execFormulaForBatchCode(voi);
		// ------------
		setListBodyData(voi);
		// abstract method.
		// ��ʾ��λ
		dispSpace(sn);
		selectBillOnListPanel(sn);
		// ִ�й�ʽ
		// getBillListPanel().getBodyBillModel().execLoadFormula();
		//
		// ���û�λ��ť
		setBtnStatusSpace(false);

		setLastHeadRow(sn);

		// ���󷽷�����
		setButtonsStatus(m_iMode);
		setExtendBtnsStat(m_iMode);
		// ����ǩ�ְ�ť�Ƿ���á�
		setBtnStatusSign(false);

		// �����Դ���ݲ�Ϊ�գ�����ҵ�����͵Ȳ�����
		ctrlSourceBillButtons(true);

		updateButtons();

		// ���������ĵ�ǰ��������VO add by hanwei ���ڳ�ʼ�������ΨһУ������
		if (m_utfBarCode != null) m_utfBarCode.setCurBillItem(voi);

		timer.showExecuteTime("@@����selectListBillʱ��");

	}

	/**
	 * 
	 * ���������������ݹ��޸�ĳ����ť�������¼���ť��״̬��
	 * <p>
	 * <b>����˵��</b>
	 * 
	 * @param btn
	 *            Ҫ�޸ĵİ�ť
	 * @param enabled
	 *            �޸ĺ��״̬
	 *            <p>
	 * @author duy
	 * @time 2007-3-27 ����03:49:02
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
	 * ֧�ֲ������ɶ��ŵ��ݵİ�Ŧ���� ���۳�,����,�ɹ�����ʹ��,ֻ���޸ĺ�ȡ������
	 * 
	 * @since v5
	 * @author ljun
	 * 
	 */
	public void setRefBtnStatus() {
		// �������ɶ��ţ��ҵ�ǰ���б����ƺܶఴť�����ã�ֻ��ȡ�����޸Ŀ��ã�ͬʱ˫����ͷ���൱���л�����
		if (m_bRefBills == true && m_iCurPanel == BillMode.List) {
			// ���Ʋ��ն��ŵ���,�ҵ�ǰ���б�״̬ʱ�İ�ť״̬
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
		// ��Ƭ�����ǲ������ɣ��л���ť����
		if (m_bRefBills == true && m_iCurPanel == BillMode.Card) {
			// ���ఴť��������ʱ�İ�ť����
			m_iMode = BillMode.New;

			// setButtonStatus(true);
			//
			getButtonTree().getButton(ICButtonConst.BTN_SWITCH).setEnabled(true);
			// updateButton(getButtonTree().getButton(ICButtonConst.BTN_SWITCH));

			// ������setButtonStatus(true)�����Ĳ��ִ���
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

			// ��������֧��
			getButtonTree().getButton(ICButtonConst.BTN_ASSIST_FUNC_REFER_IN).setEnabled(true);
			getButtonTree().getButton(ICButtonConst.BTN_IMPORT_BILL).setEnabled(true);
			getButtonTree().getButton(ICButtonConst.BTN_ASSIST_FUNC_INV_CHECK).setEnabled(true);
			getButtonTree().getButton(ICButtonConst.BTN_ASSIST_FUNC_DOCUMENT).setEnabled(false);
			// ���Ʒ�ҳ��ť��״̬��
			m_pageBtn.setPageBtnVisible(false);
			getButtonTree().getButton(ICButtonConst.BTN_LINE_AUTO_FILL).setEnabled(true);
			getButtonTree().getButton(ICButtonConst.BTN_ASSIST_FUNC_PICKUP_AUTO).setEnabled(true);

			// ����������º��޸�����������Ա༭
			if (m_utfBarCode != null) m_utfBarCode.setEnabled(true);

		}

	}

	/**
	 * �����ߣ����˾� ���ܣ����ñ�����ʾ�� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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

			// ȱʡ����������
			// if (getBillCardPanel().getBodyItem("castunitname") != null)
			getBillCardPanel().setBodyValueAt(voInv.getCastunitname(), row, "castunitname");
			getBillCardPanel().setBodyValueAt(voInv.getCinvmanid(), row, "cinvbasid");

			if (getBillCardPanel().getBodyItem("unitweight") != null) getBillCardPanel().setBodyValueAt(voInv.getNunitweight(), row, "unitweight");

			if (getBillCardPanel().getBodyItem("unitvolume") != null) getBillCardPanel().setBodyValueAt(voInv.getNunitvolume(), row, "unitvolume");

			if (voInv.getHsl() != null) {
				getBillCardPanel().setBodyValueAt(voInv.getHsl(), row, "hsl");
				// ������
				m_voBill.setItemValue(row, "hsl", voInv.getHsl());
			}
			// �Ƿ�̶�����
			m_voBill.setItemValue(row, "isSolidConvRate", voInv.getIsSolidConvRate());
			// �ƻ���
			if (voInv.getNplannedprice() != null) getBillCardPanel().setBodyValueAt(voInv.getNplannedprice(), row, m_sPlanPriceItemKey);
			// �ƻ����
			Object oTempNum = getBillCardPanel().getBodyValueAt(row, m_sNumItemKey);
			UFDouble dNum = null;
			UFDouble dMny = null;

			// ͬʱ�������͵���ʱ���ż���
			if (oTempNum != null && voInv.getNplannedprice() != null) {
				dNum = (UFDouble) oTempNum;
				dMny = dNum.multiply((UFDouble) voInv.getNplannedprice());
			} else dMny = null;

			if (dMny != null) getBillCardPanel().setBodyValueAt(dMny, row, m_sPlanMnyItemKey);

			// �������������ʾ��
			String sVfree0 = voInv.getFreeItemVO().getVfree0();
			if (sVfree0 != null && sVfree0.trim().length() > 0) getBillCardPanel().setBodyValueAt(sVfree0, row, "vfree0");
			else getBillCardPanel().setBodyValueAt("", row, "vfree0");

			if (voInv.getVbatchcode() != null) getBillCardPanel().setBodyValueAt(voInv.getVbatchcode(), row, "vbatchcode");

			execEditFomulas(row, m_sInvCodeItemKey);
			execEditFomulas(row, m_sInvMngIDItemKey);

			// ���ü�����

			nc.ui.pub.beans.UIRefPane refMeasware = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getBodyItem("cmeaswarename").getComponent());
			if (refMeasware != null) {
				if (m_iBillInOutFlag == InOutFlag.IN) refMeasware.setPK(voInv.getCrkjlc());
				else refMeasware.setPK(voInv.getCckjlc());
				/** ǿ��ִ�б����У������еĹ�ʽ */
				if (refMeasware.getRefModel() != null && refMeasware.getRefModel().getClass().getName().equals("nc.ui.mm.pub.pub1010.JlcRefModel")) execEditFomulas(row, "cmeaswarename");
			}

		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
		}
	}

	/**
	 * �����ߣ����˾� ���ܣ������޸ĺ����������е�PK,��ͬʱˢ�´����VO. Ҫ��֤VO��Item��˳��ͽ�������һ�¡� ������ ���أ� ���⣺
	 * ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
				// ��0���Ǳ�ͷPK,
				getBillCardPanel().setBodyValueAt(alBodyPK.get(pk_count + 1), row, m_sBillRowItemKey);
				pk_count++;
			}
		}
		getBillCardPanel().getBillModel().setNeedCalculate(true);
		getBillCardPanel().getBillModel().reCalcurateAll();

	}

	/**
	 * �����ߣ����˾� ���ܣ����ð�ť״̬�� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */

	protected void setButtonStatus() {
		setButtonStatus(true);
	}

	/**
	 * �����ߣ����˾� ���ܣ������������ݵĳ�ʼ���ݣ������ڣ��Ƶ��˵ȡ� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
	 * �����ߣ�cqw ���ܣ������޸ĵ��ݵĳ�ʼ���� ������ ���أ� ���⣺ ���ڣ�(2005-04-04 19:23:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
	 * �����ߣ�yudaying ���ܣ����ñ�β��ʾ����,��m_voBill��ȡ�������״̬��Ҫ�ض��ִ��� ������ ���أ� ���⣺
	 * ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
		// �޸� by hanwei 2003-11-13
		if (m_voBill.getItemInv(row) == null || m_voBill.getItemInv(row).getCinventoryid() == null) {
			setTailValue(null);
			return;
		}

		InvVO voInv = m_voBill.getItemInv(row);
		// ��ͷ�Ŀ����֯�Ͳֿ�
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
		// ȡ��ǰvo�е�����
		// oWhQty = m_voBill.getItemValue(row, "bkxcl");
		// oTotalQty = m_voBill.getItemValue(row, "xczl");
		// nmaxstocknum = m_voBill.getItemValue(row, "nmaxstocknum");
		// nminstocknum = m_voBill.getItemValue(row, "nminstocknum");
		// norderpointnum = m_voBill.getItemValue(row, "norderpointnum");
		// nsafestocknum = m_voBill.getItemValue(row, "nsafestocknum");

		// ���״̬��Ҫˢ���ִ���������Ѿ��������򲻱��ض���
		// �ڱ༭--������л�ʱ���л��������ʱҪ��տ������
		// �޸�״̬�£�ѡ����ԭ�е���ҲҪ���ִ�����
		BillItem biMax = getBillCardPanel().getTailItem("nmaxstocknum");
		BillItem biMin = getBillCardPanel().getTailItem("nminstocknum");
		BillItem biOpt = getBillCardPanel().getTailItem("norderpointnum");
		BillItem biSafe = getBillCardPanel().getTailItem("nsafestocknum");
		BillItem biWhQty = getBillCardPanel().getTailItem("bkxcl");
		BillItem biBdQty = getBillCardPanel().getTailItem("xczl");

		int iFlag = 0;
		if (((biMax != null && biMax.isShow()) || (biMin != null && biMin.isShow()) || (biOpt != null && biOpt.isShow()) || (biSafe != null && biSafe.isShow())) && m_sCorpID != null && sWhID != null && invid != null) {

			// ��ѯ���ƴ���
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
			// ��ѯ�����Ϣ
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

		// ���ý�����ʾ
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
		 * nsafestocknum.toString().length() == 0)) { //��vo�ж����id Object oInvID
		 * = m_voBill.getItemValue(row, "cinventoryid"); if (oInvID == null) {
		 * SCMEnv.out(row + "row data ERR"); return; }
		 * //((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
		 * //.getBodyItem("cinventorycode") //.getComponent()) //.getRefPK();
		 * Object oWhID = null; oWhID =
		 * m_voBill.getHeaderValue(m_sMainWhItemKey); //���� ArrayList alIDs = new
		 * ArrayList(); alIDs.add(oWhID); alIDs.add(oInvID); ArrayList alQty =
		 * null; //���� try { alQty = (ArrayList) invokeClient("queryInfo", new
		 * Class[] { Integer.class, Object.class }, new Object[] { new
		 * Integer(QryInfoConst.QTY), alIDs }); } catch (Exception e) {
		 * nc.vo.scm.pub.SCMEnv.error(e); } //
		 * ���ݸ�ʽ�������ִ������ִ���������߿��������Ϳ��������������������ȫ����� if (alQty != null &&
		 * alQty.size() >= 6) {
		 * 
		 * oWhQty = alQty.get(0); oTotalQty = alQty.get(1); nmaxstocknum =
		 * alQty.get(2); nminstocknum = alQty.get(3); norderpointnum =
		 * alQty.get(4); nsafestocknum = alQty.get(5); //д��vo����
		 * m_voBill.setItemValue(row, "bkxcl", oWhQty);
		 * m_voBill.setItemValue(row, "xczl", oTotalQty);
		 * m_voBill.setItemValue(row, "nmaxstocknum", nmaxstocknum);
		 * m_voBill.setItemValue(row, "nminstocknum", nminstocknum);
		 * m_voBill.setItemValue(row, "norderpointnum", norderpointnum);
		 * m_voBill.setItemValue(row, "nsafestocknum", nsafestocknum); } }
		 * 
		 * //�����ִ��� nc.ui.pub.bill.BillItem biTail =
		 * getBillCardPanel().getTailItem("bkxcl"); if (biTail != null) if
		 * (oWhQty != null) biTail.setValue(new
		 * nc.vo.pub.lang.UFDouble(oWhQty.toString())); else
		 * biTail.setValue(null); //�ִ����� biTail =
		 * getBillCardPanel().getTailItem("xczl"); if (biTail != null) if
		 * (oTotalQty != null) biTail.setValue(new
		 * nc.vo.pub.lang.UFDouble(oTotalQty.toString())); else
		 * biTail.setValue(null); //��߿�� biTail =
		 * getBillCardPanel().getTailItem("nmaxstocknum"); if (biTail != null)
		 * if (nmaxstocknum != null) biTail.setValue(new
		 * nc.vo.pub.lang.UFDouble(nmaxstocknum.toString())); else
		 * biTail.setValue(null); //��Ϳ�� biTail =
		 * getBillCardPanel().getTailItem("nminstocknum"); if (biTail != null)
		 * if (nminstocknum != null) biTail.setValue(new
		 * nc.vo.pub.lang.UFDouble(nminstocknum.toString())); else
		 * biTail.setValue(null); //��ȫ��� biTail =
		 * getBillCardPanel().getTailItem("nsafestocknum"); if (biTail != null)
		 * if (nsafestocknum != null) biTail.setValue(new
		 * nc.vo.pub.lang.UFDouble(nsafestocknum.toString())); else
		 * biTail.setValue(null); //�������� biTail =
		 * getBillCardPanel().getTailItem("norderpointnum"); if (biTail != null)
		 * if (norderpointnum != null) biTail.setValue(new
		 * nc.vo.pub.lang.UFDouble(norderpointnum.toString())); else
		 * biTail.setValue(null);
		 */
	}

	/**
	 * �����ߣ����˾� ���ܣ����ñ�β��ʾ����,����null����ա� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	protected void setTailValue(InvVO voInv) {
		// �����ִ���
		nc.ui.pub.bill.BillItem biTail = null;
		biTail = getBillCardPanel().getTailItem("bkxcl");
		if (biTail != null) if (voInv != null) biTail.setValue(voInv.getBkxcl());
		else biTail.setValue(null);
		// �ִ�����
		biTail = getBillCardPanel().getTailItem("xczl");
		if (biTail != null) if (voInv != null) biTail.setValue(voInv.getBkxcl());
		else biTail.setValue(null);
		// ��߿��
		biTail = getBillCardPanel().getTailItem("nmaxstocknum");
		if (biTail != null) if (voInv != null) biTail.setValue(voInv.getNmaxstocknum());
		else biTail.setValue(null);
		// ��Ϳ��
		biTail = getBillCardPanel().getTailItem("nminstocknum");
		if (biTail != null) if (voInv != null) biTail.setValue(voInv.getNminstocknum());
		else biTail.setValue(null);
		// ��ȫ���
		biTail = getBillCardPanel().getTailItem("nsafestocknum");
		if (biTail != null) if (voInv != null) biTail.setValue(voInv.getNsafestocknum());
		else biTail.setValue(null);
		// ��������
		biTail = getBillCardPanel().getTailItem("norderpointnum");
		if (biTail != null) if (voInv != null) biTail.setValue(voInv.getNorderpointnum());
		else biTail.setValue(null);

	}

	/**
	 * �����ߣ����˾� ���ܣ�ͬ�������ݣ����λ�����к� ������ int iFirstLine,iLastLine �кţ�start from 0 int
	 * iCol �� start from 0 int type 1: add 0: update -1:delete
	 * 
	 * ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2001-06-13. ͬ��VO
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
			// ��ʼ�������ݣ������ڸ��Ƶ���ʱ��m_alLocatorData==null ������������Ϊ0��
			m_alLocatorData = new ArrayList(getBillCardPanel().getBillModel().getRowCount());
		}
		if (m_alSerialData == null) {
			SCMEnv.out("init serial data.");
			// m_alSerialData=new ArrayList();
			// ��ʼ�������ݣ������ڸ��Ƶ���ʱ��m_alSerialData==null ������������Ϊ0��
			m_alSerialData = new ArrayList(getBillCardPanel().getBillModel().getRowCount());
		}
		if (m_voBill == null) m_voBill = new GeneralBillVO();

		switch (iType) {
		case javax.swing.event.TableModelEvent.INSERT:// ���У��塢׷�ӡ�ճ��
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
		// try��Ŀ���Ǳ�֤addListener��ִ�С�
		try {
			getBillCardPanel().getBillModel().removeTableModelListener(this);
			if (e.getType() != javax.swing.event.TableModelEvent.UPDATE && e.getSource() == getBillCardPanel().getBillModel()) synchLineData(e.getFirstRow(), e.getLastRow(), e.getColumn(), e.getType());

			// //�ڳ���ʱ����ջ�λ
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
	 * �����ߣ����˾� ���ܣ��ñ���ʽ�µĵ���ˢ���б����ݣ������޸ı���� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
		// ˢ������
		m_alListData.set(m_iLastSelListHeadRow, bvo.clone());
		// ˢ���б������ʾ
		GeneralBillHeaderVO voh[] = new GeneralBillHeaderVO[m_alListData.size()];
		for (int i = 0; i < m_alListData.size(); i++) {
			if (m_alListData.get(i) != null) voh[i] = (GeneralBillHeaderVO) ((GeneralBillVO) m_alListData.get(i)).getParentVO();
			else SCMEnv.out("list data error!-->" + i);

		}

		setListHeadData(voh);
		// ѡ���б��ݣ���ˢ�±�����ʾ
		selectListBill(m_iLastSelListHeadRow);

	}

	/**
	 * �����ߣ����˾� ���ܣ����ݱ༭�¼����� ������e���ݱ༭�¼� ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
			// ��������λ
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
	 * �����ߣ����˾� ���ܣ�ȡ��ǩ�ֳɹ����� ������ ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05)
	 * 
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 */
	public void freshAfterCancelSignedOK() {
		try {

			refreshSelBill(m_iLastSelListHeadRow);
			// GeneralBillVO voBill = null;
			// // ����m_voBill,�Զ�ȡ�������ݡ�
			// if (m_iLastSelListHeadRow >= 0 && m_alListData != null
			// && m_alListData.size() > m_iLastSelListHeadRow
			// && m_alListData.get(m_iLastSelListHeadRow) != null) {
			// // ���ﲻ��clone(),�ı���m_voBillͬʱ�ı�m_alListData
			// voBill = (GeneralBillVO) m_alListData
			// .get(m_iLastSelListHeadRow);
			// // �ѵ�ǰ��¼�����õ�vo
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
			// // �����б����
			// m_alListData.set(m_iLastSelListHeadRow, voBill);
			// }
			// // �ѵ�ǰ��¼���ÿ�
			// // m_voBillˢ��
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
			// // �ѽ���ǩ���������ÿ�
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
			// // ˢ���б���ʽ
			// setListHeadData();
			// selectListBill(m_iLastSelListHeadRow);
			// ���ð�ť״̬,ǩ�ֿ��ã�ȡ��ǩ�ֲ�����
			// ��Ӧ��һ���жϵ�ǰ�ĵ����Ƿ���ǩ��
			// δǩ�֣��������ð�ť״̬,ǩ�ֿ��ã�ȡ��ǩ�ֲ�����
			setButtonStatus(false);
			getButtonTree().getButton(ICButtonConst.BTN_SIGN).setEnabled(true);
			getButtonTree().getButton(ICButtonConst.BTN_EXECUTE_SIGN_CANCEL).setEnabled(false);
			// ��ɾ����
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
	 * �����ߣ����˾� ���ܣ��õ�������ʼ���ݣ����Ƶ��˵ȡ� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	protected void getCEnvInfo() {
		try {
			// ��ǰʹ����ID
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
			// ��ǰʹ��������
			try {
				m_sUserName = ce.getUser().getUserName();
			} catch (Exception e) {

			}
			// SCMEnv.out("test user name is ����");
			// m_sUserName="����";
			// ��˾ID
			try {
				m_sCorpID = ce.getCorporation().getPrimaryKey();
				SCMEnv.out("---->corp id is " + m_sCorpID);
			} catch (Exception e) {

			}
			// ����
			try {
				if (ce.getDate() != null) m_sLogDate = ce.getDate().toString();
			} catch (Exception e) {

			}
		} catch (Exception e) {

		}
	}

	/**
	 * �����ߣ����˾� ���ܣ����ݵ�ǰ���ݵĴ���״̬����ǩ��/ȡ��ǩ���Ǹ����� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	protected void setBtnStatusSign() {
		// ֻ�����״̬�²��ҽ������е���ʱ����
		setBtnStatusSign(true);
	}

	/**
	 * �����ߣ����˾� ���ܣ����ݵ�ǰ��������Ծ������кŷ����Ƿ���� ������ row�к� ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	protected void setBtnStatusSN(int row) {
		setBtnStatusSN(row, true);
	}

	/**
	 * �����ߣ����˾� ���ܣ����ݵ�ǰ�ֿ��״̬������λ�����Ƿ���� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	protected void setBtnStatusSpace() {
		setBtnStatusSpace(true);
	}

	/**
	 * �����ߣ����˾� ���ܣ�ˢ���б���ʽ��ͷ���� ������ ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05)
	 * 
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 2003-02-27 ���� ��ӹ�ʽ�����ȡ�ֿ�ͷ�Ʒ�ֿ���Ϣ
	 */
	public void setListHeadData() {
		if (m_alListData != null && m_alListData.size() > 0) {
			// ˢ���б���ʽ��ͷ����
			GeneralBillHeaderVO voh[] = new GeneralBillHeaderVO[m_alListData.size()];
			for (int i = 0; i < m_alListData.size(); i++) {
				if (m_alListData.get(i) != null) voh[i] = (GeneralBillHeaderVO) ((GeneralBillVO) m_alListData.get(i)).getParentVO();
				else SCMEnv.out("list data error!-->" + i);

			}
			setListHeadData(voh);
		}

	}

	/**
	 * �����ߣ����Ӣ ���ܣ��õ���ǰ����vo,������λ/���к�,����ɾ�����У�δ�޸ĵ���ֻ��PK. ������ ���أ� ���⣺ ���ڣ�(2001-5-9
	 * 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
	 * �����ߣ����Ӣ ���ܣ��õ���ǰ����vo,������λ/���кźͽ��������е�����,������ɾ������ ������ ���أ� ���⣺ ���ڣ�(2001-5-9
	 * 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
	 * �����ߣ����˾� ���ܣ��õ������ĵ��ݱ���VO������ɾ�����С� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
		// Ϊ�˵õ������
		GeneralBillItemVO[] voaItemForFree = m_voBill.getItemVOs();
		// ɾ������

		// vo����ĳ���==��ǰ��ʾ������+ɾ����������
		int rowCount = vBodyData.size();
		int length = 0;
		Vector vDeleteRow = bmTemp.getDeleteRow();
		if (vDeleteRow != null) length = rowCount + vDeleteRow.size();
		else length = rowCount;
		// ��ʼ�����ص�vo
		GeneralBillItemVO[] voaBody = new GeneralBillItemVO[length];

		int iRowStatus = nc.ui.pub.bill.BillModel.ADD;

		// ����ǰ��������ʾ���У�����ԭ�С��޸ĺ���С��������С�
		// ����������
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
			// ����״̬
			switch (iRowStatus) {
			case nc.ui.pub.bill.BillModel.ADD: // ��������
				voaBody[i].setStatus(nc.vo.pub.VOStatus.NEW);
				break;
			case nc.ui.pub.bill.BillModel.MODIFICATION: // �޸ĺ����
				voaBody[i].setStatus(nc.vo.pub.VOStatus.UPDATED);
				break;
			case nc.ui.pub.bill.BillModel.NORMAL: // ԭ��
				voaBody[i].setStatus(nc.vo.pub.VOStatus.UNCHANGED);
				break;
			}
			// ��λ��������
			if (m_alLocatorData != null && m_alLocatorData.size() > i) voaBody[i].setLocator((LocatorVO[]) m_alLocatorData.get(i));
			// ���к�����
			if (m_alSerialData != null && m_alSerialData.size() > i) voaBody[i].setSerial((SerialVO[]) m_alSerialData.get(i));
			// ������
			voaBody[i].setFreeItemVO(voaItemForFree[i].getFreeItemVO());

		}
		// ɾ�����д���2003-02-09 wnj:�ô�ԭ�еĵ����п����������λ�����кŶ�û���ˡ�
		if (vDeleteRow != null && vDeleteRow.size() > 0) {
			if (m_iLastSelListHeadRow >= 0 && m_alListData != null && m_alListData.size() > m_iLastSelListHeadRow && m_alListData.get(m_iLastSelListHeadRow) != null) {
				// =========
				int col = bmTemp.getBodyColByKey(m_sBillRowItemKey); // ����PK
				Vector rowVector = null;
				String sItemPK = null;
				GeneralBillVO voOriginalBill = (GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow);
				GeneralBillItemVO[] voaOriginalItem = voOriginalBill.getItemVOs();
				// ��ѯɾ�����е�pk,��ԭ�����в���֮��
				// ��������н϶࣬�����Ż�Ϊhastable.
				for (int del = 0; del < vDeleteRow.size(); del++) {
					rowVector = (Vector) vDeleteRow.elementAt(del);
					sItemPK = (String) rowVector.elementAt(col);
					// ��ԭ�����в���֮��
					if (sItemPK != null) for (int item = 0; item < voaOriginalItem.length; item++)
						if (sItemPK.equals(voaOriginalItem[item].getPrimaryKey())) voaBody[del + rowCount] = (GeneralBillItemVO) voaOriginalItem[item].clone();
					// ����״̬
					voaBody[del + rowCount].setStatus(nc.vo.pub.VOStatus.DELETED);
				}
			} else SCMEnv.out("update err,cannot dup del rows.");

		}
		return voaBody;
	}

	/**
	 * �����ߣ����˾� ���ܣ��õ��޸ĺ��vo,�����޸ĺ�ı��� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
		// Ϊ�˵õ������
		GeneralBillItemVO[] voaItemForFree = m_voBill.getItemVOs();

		// vo����ĳ���==��ǰ��ʾ������
		int rowCount = vBodyData.size();
		// ��ʼ�����ص�vo
		GeneralBillItemVO[] voaBody = new GeneralBillItemVO[rowCount];
		int iRowStatus = nc.ui.pub.bill.BillModel.ADD;

		// ����ǰ��������ʾ���У�����ԭ�С��޸ĺ���С��������С�
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
			// ����״̬
			switch (iRowStatus) {
			case nc.ui.pub.bill.BillModel.ADD: // ��������
				voaBody[i].setStatus(nc.vo.pub.VOStatus.NEW);
				break;
			case nc.ui.pub.bill.BillModel.MODIFICATION: // �޸ĺ����
				voaBody[i].setStatus(nc.vo.pub.VOStatus.UPDATED);
				break;
			case nc.ui.pub.bill.BillModel.NORMAL: // ԭ��
				voaBody[i].setStatus(nc.vo.pub.VOStatus.UNCHANGED);
				break;
			}
			// ��λ��������
			if (m_alLocatorData != null && m_alLocatorData.size() > i) voaBody[i].setLocator((LocatorVO[]) m_alLocatorData.get(i));

			// ���к�����
			if (m_alSerialData != null && m_alSerialData.size() > i) {
				SerialVO[] serialVOs = (SerialVO[]) m_alSerialData.get(i);
				// �������к�����
				voaBody[i].updateSerialDate(serialVOs);
				// �������к�
				voaBody[i].setSerial(serialVOs);
			}

			// ������
			voaBody[i].setFreeItemVO(voaItemForFree[i].getFreeItemVO());

			// ɾ�����в���
		}
		return voaBody;
	}

	/**
	 * �����ߣ����˾� ���ܣ��õ���ǰ�����Ƿ���ǩ�� ������ ���أ� ��ǩ�� 1 δǩ�� 0 ���ܲ��� -1 ���⣺ ���ڣ�(2001-5-9
	 * 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	public int isSigned() {
		GeneralBillVO voBill = null;
		// ����voBill,�Զ�ȡ�������ݡ�

		if (m_iCurPanel == BillMode.List && m_iLastSelListHeadRow >= 0 && m_alListData != null && m_alListData.size() > m_iLastSelListHeadRow && m_alListData.get(m_iLastSelListHeadRow) != null) voBill = (GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow);
		else voBill = m_voBill;

		if (voBill != null) {
			int iCount = voBill.getItemCount();
			int i = 0;
			GeneralBillItemVO voaItem[] = voBill.getItemVOs();
			for (i = 0; i < iCount; i++) {
				// ����ʵ��/������
				if ((voaItem[i].getNinnum() == null || voaItem[i].getNinnum().toString().length() == 0) && (voaItem[i].getNoutnum() == null || voaItem[i].getNoutnum().toString().length() == 0)) break;

			}

			if (i < iCount) // ������
			return CANNOTSIGN;

			// �Ƿ���ǩ����
			String sSignerID = ((GeneralBillHeaderVO) voBill.getHeaderVO()).getCregister();
			if (sSignerID != null && sSignerID.trim().length() > 0) return SIGNED;
			else return NOTSIGNED;
		} else return CANNOTSIGN;
	}

	/**
	 * �����ߣ����˾� ���ܣ��ڱ�������ʾVO ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	public void setBillVO(GeneralBillVO bvo) {
		setBillVO(bvo, true, true);
	}

	/**
	 * �����ߣ������� ���ܣ������� ������ ���أ� ���⣺ ���ڣ�(2001-5-24 ���� 5:17) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	public boolean checkVO() {
		try {
			String sAllErrorMessage = "";

			// ִ�����¼�飬�������еļ��ע��------------------------------------------------
			// ------------------------------------------------------------------------------
			// VO���ڼ��
			VOCheck.checkNullVO(m_voBill);
			// ------------------------------------------------------------------------------
			// Ӧ���������,Ҫ����ǰ��
			// ���ڵ�ʹ��=====================
			// ��������ķ����ҵ�����Ƿ�һ��.�˻��˿������ҪΪ��,���˿��˻�����Ϊ��
			boolean isRight = VOCheck.isNumDirectionRight(m_voBill);
			if (!isRight) {
				sAllErrorMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID("4008check", "UPP4008check-000213")/*
																													 * @res
																													 * "�����ķ����ҵ����һ�£�"
																													 */;
				showErrorMessage(sAllErrorMessage);
				return false;
			}
			// ��ֵ����ȫ���Լ�� v5 ֧�ֻ���
			// VOCheck.checkNumInput(m_voBill.getChildrenVO(), m_sNumItemKey);

			// ���ڵ�ʹ��=====================
			// ��ͷ����ǿռ��
			try {
				VOCheck.validate(m_voBill, GeneralMethod.getHeaderCanotNullString(getBillCardPanel()), GeneralMethod.getBodyCanotNullString(getBillCardPanel()));
			} catch (ICNullFieldException e) {
				// ��ʾ��ʾ
				String sErrorMessage = GeneralMethod.getBodyErrorMessage(getBillCardPanel(), m_voBill, e.getErrorRowNums(), e.getHint());
				sAllErrorMessage = sAllErrorMessage + sErrorMessage + "\n";
			} catch (ICHeaderNullFieldException e) {
				// ��ʾ��ʾ
				String sErrorMessage = GeneralMethod.getHeaderErrorMessage(getBillCardPanel(), e.getErrorRowNums(), e.getHint());
				sAllErrorMessage = sAllErrorMessage + sErrorMessage + "\n";
			}

			/*
			 * // �ɹ������˿��ͷVO��ҵ�����͵ķǿ����� try {
			 * VOCheck.validatePO_RETURN(m_voBill);
			 * 
			 * } catch (ICHeaderNullFieldException e) { // ��ʾ��ʾ String
			 * sErrorMessage = GeneralMethod.getHeaderErrorMessage(
			 * getBillCardPanel(), e.getErrorRowNums(), e.getHint());
			 * sAllErrorMessage = sAllErrorMessage + sErrorMessage + "\n"; }
			 */

			// �е�������ĵ�������У��
			if (m_bIsImportData) sAllErrorMessage = sAllErrorMessage + nc.ui.ic.pub.bill.GeneralBillUICtl.checkImportBodyVO(m_voBill.getChildrenVO());
			// ------------------------------------------------------------------------------
			// ҵ������

			// ������
			try {
				VOCheck.checkFreeItemInput(m_voBill, m_sNumItemKey);

			} catch (ICNullFieldException e) {
				// ��ʾ��ʾ
				String sErrorMessage = GeneralMethod.getBodyErrorMessage(getBillCardPanel(), m_voBill, e.getErrorRowNums(), e.getHint());
				sAllErrorMessage = sAllErrorMessage + sErrorMessage + "\n";
			}
			// ������
			try {
				VOCheck.checkAssistUnitInputByID(m_voBill, m_sNumItemKey, m_sAstItemKey);
			} catch (ICNullFieldException e) {
				// ��ʾ��ʾ
				String sErrorMessage = GeneralMethod.getBodyErrorMessage(getBillCardPanel(), m_voBill, e.getErrorRowNums(), e.getHint());
				sAllErrorMessage = sAllErrorMessage + sErrorMessage + "\n";
			}

			try {
				VOCheck.checkAssistUnitInputByID(m_voBill, m_sAstItemKey, m_sNumItemKey);
			} catch (ICNullFieldException e) {
				// ��ʾ��ʾ
				String sErrorMessage = GeneralMethod.getBodyErrorMessage(getBillCardPanel(), m_voBill, e.getErrorRowNums(), e.getHint());
				sAllErrorMessage = sAllErrorMessage + sErrorMessage + "\n";
			}

			// �������
			try {
				if (getBillCardPanel().getBodyItem("dbizdate") != null) VOCheck.checkdbizdate(m_voBill, m_sNumItemKey);
			} catch (ICNullFieldException e) {
				// ��ʾ��ʾ
				String sErrorMessage = GeneralMethod.getBodyErrorMessage(getBillCardPanel(), m_voBill, e.getErrorRowNums(), e.getHint());
				sAllErrorMessage = sAllErrorMessage + sErrorMessage + "\n";
			}
			// ���ڳ������ҵ�����������ϵͳ��������
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
			// �۸�>0���
			try {
				VOCheck.checkGreaterThanZeroInput(m_voBill.getChildrenVO(), "nprice", nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC000-0000741")/* @res "����" */);
			} catch (ICPriceException e) {
				// ��ʾ��ʾ
				String sErrorMessage = GeneralMethod.getBodyErrorMessage(getBillCardPanel(), m_voBill, e.getErrorRowNums(), e.getHint());
				sAllErrorMessage = sAllErrorMessage + sErrorMessage + "\n";
			}
			// ���кż��
			try {
				if (!isBrwLendBiztype()) VOCheck.checkSNInput(m_voBill.getChildrenVO(), m_sNumItemKey);
			} catch (ICSNException e) {
				// ��ʾ��ʾ
				String sErrorMessage = GeneralMethod.getBodyErrorMessage(getBillCardPanel(), m_voBill, e.getErrorRowNums(), e.getHint());
				sAllErrorMessage = sAllErrorMessage + sErrorMessage + "\n";
			} catch (NullFieldException e) {
				String sErrorMessage = e.getHint();
				sAllErrorMessage = sAllErrorMessage + sErrorMessage + "\n";
			}

			// ����Ӧ���ݺ�
			ArrayList alCheckString = new ArrayList();
			for (int i = 0; i < m_voBill.getItemVOs().length; i++) {
				boolean bCanAdded = false;
				if (m_iBillInOutFlag == InOutFlag.IN) {
					// ��ⵥ
					if (getIsInvTrackedBill(m_voBill.getItemInv(i)) && m_voBill.getItemValue(i, m_sNumItemKey) != null && ((UFDouble) m_voBill.getItemValue(i, m_sNumItemKey)).doubleValue() <= 0) {
						// ����<0
						bCanAdded = true;
					}
				} else {
					// ���ⵥ
					if (getIsInvTrackedBill(m_voBill.getItemInv(i)) && (m_voBill.getItemValue(i, m_sNumItemKey) == null || ((UFDouble) m_voBill.getItemValue(i, m_sNumItemKey)).doubleValue() >= 0)) {
						// ����>=0
						bCanAdded = true;
					}
				}
				if (bCanAdded) {
					ArrayList alCheckddd = new ArrayList();
					alCheckddd.add(0, new Integer(i));
					alCheckddd.add(1, m_sNumItemKey);
					if (getBillCardPanel().getBodyItem(m_sCorBillCodeItemKey) != null) {
						alCheckddd.add(2, m_sCorBillCodeItemKey); // ��Ӧ���ݺ��ֶ�1
						alCheckddd.add(3, "ccorrespondbid"); // ��Ӧ���ݺ��ֶ�2
					}
					alCheckString.add(alCheckddd);
				}
			}
			try {
				VOCheck.validateBody(m_voBill.getItemVOs(), alCheckString);
			} catch (ICNullFieldException e) {
				// ��ʾ��ʾ
				String sErrorMessage = GeneralMethod.getBodyErrorMessage(getBillCardPanel(), m_voBill, e.getErrorRowNums(), e.getHint());
				sAllErrorMessage = sAllErrorMessage + sErrorMessage + "\n";
			}
			// }

			// �Զ�У��ǰ���б����˳�
			if (sAllErrorMessage.trim().length() != 0) {
				showErrorMessage(sAllErrorMessage);
				return false;
			}

			// У�������������ܴ���ʵ���շ�����
			GeneralBillItemVO[] voaItemtemp = (GeneralBillItemVO[]) m_voBill.getChildrenVO();
			String sMsg = BarcodeparseCtrl.checkNumWithBarNum(voaItemtemp, true);
			if (sMsg != null) {
				showErrorMessage(sMsg);
				return false;
			}
			// ���ҵ��������W����ֿ��������Ĳ�.

			try {
				checkVMIWh(m_voBill);

			} catch (Exception ex1) {
				showErrorMessage(ex1.getMessage());

				return false;

			}

			// �����Ϊ��ë�ع�����ı�������ë��
			try {
				VOCheck.checkGrossNumInput(m_voBill.getItemVOs(), m_sNgrossnum, m_sNumItemKey);
			} catch (ICNullFieldException e) {
				// ��ʾ��ʾ
				String sErrorMessage = GeneralMethod.getBodyErrorMessage(getBillCardPanel(), m_voBill, e.getErrorRowNums(), e.getHint());
				sAllErrorMessage = sAllErrorMessage + sErrorMessage + "\n";
			}

			// add by xiaolong_fan,2012-11-06.�ֿ⵵����[��Ӧ�̼Ĵ�]�򹴵� ,��Ϊ�ɰ��ջ�λ����,����Ϊ�����.
			// 1.���ֿ��Ƿ�Ĵ�ֿ�.
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
				if (res != null && "Y".equals(res)) {// �ǼĴ�ֿ�,����λ�����.
					// ����ǳ��⻹�����,�����1,������-1,����Ϊ0
					int inOutFlag = getBillInOutFlag();
					// int inOutFlag = m_voBill.getBillInOutFlag();

					StringBuffer errMsg = new StringBuffer();

					// 2.ѭ�����ÿ�����϶�Ӧ�Ŀ�λ,�Ƿ��ڻ�λ��
					for (int i = 0; i < m_voBill.getItemVOs().length; i++) {
						double ninnum = 0d;
						// ����
						if (m_voBill.getItemValue(i, m_sNumItemKey) != null) {
							ninnum = ((UFDouble) m_voBill.getItemValue(i, m_sNumItemKey)).doubleValue();
						}
						// �������
						// m_voaBillItem
						// GeneralBillItemVO vo = new GeneralBillItemVO();
						// vo.getCinventorycode();
						// vo.getVspacename();
						String cinventorycode = String.valueOf(m_voBill.getItemValue(i, "cinventorycode"));
						// ��λ
						String vspacename = String.valueOf(m_voBill.getItemValue(i, "cspaceid"));
						// ��Ӧ�� add by cm
						String cvendorid = String.valueOf(m_voBill.getItemValue(i, "cvendorid"));
						// ������add by cm
						String vfree1 = String.valueOf(m_voBill.getItemValue(i, "vfree1"));
						// �ֿ� add by cm
						// String cwarehouseid =
						// String.valueOf(m_voBill.getItemValue(i,
						// "cwarehouseid"));
						// ����add by cm vlotb
						String vlotb = String.valueOf(m_voBill.getItemValue(i, "vbatchcode"));
						// �к�add by cm
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
								// errMsg.append("��").append(i+1).append("�д��<"+cinventorycode+">�ڸû�λ�Ľ������Ϊ��").append(num).append(".�������!\n");
								errMsg.append("�к�Ϊ").append(crowno).append("�Ĵ��<" + cinventorycode + ">�ڸû�λ�Ľ������Ϊ��").append(num).append(".�������!\n");
							} else if ((inOutFlag == InOutFlag.OUT) && (num.doubleValue() - ninnum < 0)) {
								errMsg.append("�к�Ϊ").append(crowno).append("���д��<" + cinventorycode + ">�ڸû�λ�Ľ������Ϊ��").append(num).append(".���ܳ���!\n");
							}
						} else {
							// UFDouble num = new
							// UFDouble(String.valueOf(ninnum));
				  		     if (inOutFlag == InOutFlag.OUT) {
								errMsg.append("�к�Ϊ").append(crowno).append("���д��<" + cinventorycode + ">�ڸû�λ�Ľ������Ϊ��").append(0).append(".���ܳ���!\n");
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
			// add by xiaolong_fan.���ϳ����ܿؼ�.
			// ����ǳ��⻹�����,�����1,������-1,����Ϊ0
			int inOutFlag = getBillInOutFlag();
			if (getParentCorpCode1().equals("10395")) {
				if (inOutFlag == -1) {
					Object res = null;
					IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
					for (int j = 0; j < voaItemtemp.length; j++) {
						// �������
						String cinventorycode = String.valueOf(m_voBill.getItemValue(j, "cinventorycode"));
						// ��Դ��������
						String csourcetype = String.valueOf(m_voBill.getItemValue(j, "csourcetype"));
						// ��������add by shikun 2014-06-12
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
						if (res != null && "��".equals(String.valueOf(res))) {
							if (csourcetype == null || !"422X".equals(csourcetype)) {
								//add by zwx ������Դ����Ϊ4Q(��λ������)
								String billtype = getBillTypeCode();
								if (noutnum.doubleValue()>=0&&!"4Q".equals(billtype)) {//add by shikun 2014-06-12 ��������Ϊ������������˻����������뵥У��
									int m = j + 1;
									showErrorMessage("��" + m + "�д�����ܿ����ʣ��������������������뵥��������ͨ����ſɳ���");
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

			// �Զ�У��
			// ------------------------------------------------------------------------------
			/*
			 * if (packageID.equals("221")) { //�����ֿⲻ����ͬ //Ӧ���ѽ����˷ǿ�У�� if
			 * (m_voBill .getParentVO() .getAttributeValue("coutwarehouseid")
			 * .toString() .trim() .equals(
			 * m_voBill.getParentVO().getAttributeValue
			 * ("cinwarehouseid").toString().trim())) { //��ʾ��ʾ
			 * showHintMessage("�����ֿⲻӦ��ͬ..."); //showErrorMessage(
			 * "�����ֿⲻӦ��ͬ..."); //������ɫΪ������ɫ SetColor.SetTableColor(
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
			 * //���κ��쳣 //������ɫΪ������ɫ
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
			// ��ʾ��ʾ
			String sErrorMessage = GeneralMethod.getBodyErrorMessage(getBillCardPanel(), m_voBill, e.getErrorRowNums(), e.getHint());
			// String sErrorMessage= getBodyErrorMessage(e.getErrorRowNums(),
			// e.getHint());
			showErrorMessage(sErrorMessage);
			// showHintMessage(e.getHint());
			// ������ɫ
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
			// ��ʾ��ʾ
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
			// ������ɫ
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
			// ��ʾ��ʾ
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
			// ��ʾ��ʾ
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
			// ��ʾ��ʾ
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
			// ��ʾ��ʾ
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
			// ��ʾ��ʾ
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
			// ��ʾ��ʾ
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
			SCMEnv.out("У���쳣������δ֪����...");
			handleException(e);
			return false;
		}
	}

	/**
	 * �����ߣ����˾� ���ܣ��õ��޸ĺ��vo,�����޸ĺ�ı��� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
	 * �����ߣ����˾� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	/**
	 * �����ߣ����˾� ���ܣ��õ���ǰ��ʾ��panel ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	public int getCurPanel() {
		return m_iCurPanel;
	}

	/**
	 * �����ߣ����˾� ���ܣ����ݱ����Ҽ��˵����� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	public void onMenuItemClick(java.awt.event.ActionEvent e) {
		// Դ
		UIMenuItem item = (UIMenuItem) e.getSource();
		// ����
		if (item == getBillCardPanel().getCopyLineMenuItem()) {
			onCopyLine();
		} else // ճ��
		if (item == getBillCardPanel().getPasteLineMenuItem()) {
			onPasteLine();
		}
		// ճ������βʱ,�����к�
		else if (item == getBillCardPanel().getPasteLineToTailMenuItem()) {
			int iRowCount = getBillCardPanel().getBodyPanel().getTableModel().getRowCount();
			getBillCardPanel().pasteLineToTail();
			// ���ӵ�����
			int iRowCount1 = getBillCardPanel().getBodyPanel().getTableModel().getRowCount();
			nc.ui.scm.pub.report.BillRowNo.addLineRowNos(getBillCardPanel(), m_sBillTypeCode, m_sBillRowNo, iRowCount1 - iRowCount);
			voBillPastTailLine();

		} else // ����
		if (item == getBillCardPanel().getAddLineMenuItem()) {
			onAddLine();

		} else // ɾ��
		if (item == getBillCardPanel().getDelLineMenuItem()) onDeleteRow();
		else // ������
		if (item == getBillCardPanel().getInsertLineMenuItem()) {
			onInsertLine();
		}

	}

	/**
	 * �����ߣ������� ���ܣ������� ������ ���أ� ���⣺ ���ڣ�(2001-6-26 ���� 9:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	protected void voBillCopyLine() {
		int[] row = getBillCardPanel().getBillTable().getSelectedRows();
		if (row == null || row.length == 0) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH004")/* @res "��ѡ��Ҫ�����������" */);
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
			// �����λ�����кţ���Щ�����ǲ����Ƶ�,�� m_alLoctorData,m_alSerialData����һ��
			// ydy 2004-07-02 ��λ����
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

			// ����������� add by hanwei 2004-04-07
			m_voaBillItem[i].setBarCodeVOs(null);
			m_voaBillItem[i].setAttributeValue(IItemKey.NBARCODENUM, new UFDouble(0.0));

			m_voaBillItem[i].setBarcodeClose(new nc.vo.pub.lang.UFBoolean('N'));

			m_voaBillItem[i].setAttributeValue(IItemKey.NKDNUM, null);
		}

	}

	/**
	 * �����ߣ������� ���ܣ�ճ���� ������ ���أ� ���⣺ ���ڣ�(2001-6-26 ���� 9:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	protected void voBillPastLine() {
		// Ҫ�����Ѿ������к�ִ��
		if (m_voaBillItem != null) {
			int row = getBillCardPanel().getBillTable().getSelectedRow() - m_voaBillItem.length;
			voBillPastLine(row);
		}

	}

	/**
	 * �����ߣ����˾� ���ܣ��õ��������� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	public int getBillCount() {
		return m_iBillQty;
	}

	/**
	 * �����ߣ����˾� ���ܣ��õ�ָ���е�VO ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
		// Ϊ�˵õ������
		GeneralBillItemVO voItemForFree = m_voBill.getItemVOs()[iLine];

		// vo����ĳ���==��ǰ��ʾ������
		// int rowCount = vBodyData.size();
		// ��ʼ�����ص�vo
		GeneralBillItemVO voBody = new GeneralBillItemVO();

		int iRowStatus = nc.ui.pub.bill.BillModel.ADD;

		// ����ǰ��������ʾ���У�����ԭ�С��޸ĺ���С��������С�
		iRowStatus = bmTemp.getRowState(iLine);
		for (int j = 0; j < bmTemp.getBodyItems().length; j++) {
			nc.ui.pub.bill.BillItem item = bmTemp.getBodyItems()[j];
			Object aValue = bmTemp.getValueAt(iLine, item.getKey());
			aValue = item.converType(aValue);
			voBody.setAttributeValue(item.getKey(), aValue);
		}
		// ����״̬
		switch (iRowStatus) {
		case nc.ui.pub.bill.BillModel.ADD: // ��������
			voBody.setStatus(nc.vo.pub.VOStatus.NEW);
			break;
		case nc.ui.pub.bill.BillModel.MODIFICATION: // �޸ĺ����
			voBody.setStatus(nc.vo.pub.VOStatus.UPDATED);
			break;
		case nc.ui.pub.bill.BillModel.NORMAL: // ԭ��
			voBody.setStatus(nc.vo.pub.VOStatus.UNCHANGED);
			break;
		}
		// ��λ��������
		if (m_alLocatorData != null && m_alLocatorData.size() > iLine) voBody.setLocator((LocatorVO[]) m_alLocatorData.get(iLine));
		// ���к�����
		if (m_alSerialData != null && m_alSerialData.size() > iLine) voBody.setSerial((SerialVO[]) m_alSerialData.get(iLine));
		// ������
		voBody.setFreeItemVO(voItemForFree.getFreeItemVO());

		// ɾ�����в���
		return voBody;
	}

	/**
	 * �����ߣ����˾� ���ܣ��õ���ǰ���ݵı༭״̬ ������ ���أ� ��ǩ�� 1 δǩ�� 0 ���ܲ��� -1 ���⣺ ���ڣ�(2001-5-9
	 * 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	public int getMode() {
		return m_iMode;
	}

	/**
	 * �����ߣ����˾� ���ܣ���ǰѡ�����Ƿ������кŷ��䣬Ҫ�����б�/���µ�ѡ�� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	public boolean isSNmgt() {
		// ��ǰѡ�е���
		int iCurSelBodyLine = -1;
		if (BillMode.Card == m_iCurPanel) {
			iCurSelBodyLine = getBillCardPanel().getBillTable().getSelectedRow();
			if (iCurSelBodyLine < 0) {
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000016")/*
																												* @res "��ѡ��Ҫ�������кŷ�����С�"
																												*/);
				return false;
			}
		} else {
			iCurSelBodyLine = getBillListPanel().getBodyTable().getSelectedRow();
			if (iCurSelBodyLine < 0) {
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000017")/*
																												* @res "��ѡ��Ҫ�鿴���кŵ��С�"
																												*/);
				return false;
			}
		}
		InvVO voInv = null;
		// ������vo,�����б��»��Ǳ���
		// ����ʽ��
		if (BillMode.Card == m_iCurPanel) {
			if (m_voBill == null) {
				SCMEnv.out("bill null E.");
				return false;
			}
			voInv = m_voBill.getItemInv(iCurSelBodyLine);
		} else // �б���ʽ��
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
	 * �����ߣ����˾� ���ܣ�����״̬����ȱʡ����²������á� ��Ҫ�Ļ����԰���ʾ��Ϣ�ض���ָ����TextField ������ ���أ� ���⣺
	 * ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	public void setHintBar(javax.swing.JTextField tfHint) {
		m_tfHintBar = tfHint;
	}

	/**
	 * �����ߣ����˾� ���ܣ����ص���ʾ��ʾ��Ϣ���� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
	 * �����ߣ����˾� ���ܣ��õ���ǰѡ�еĵ��� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	public int getCurSelBill() {
		return m_iLastSelListHeadRow;
	}

	/**
	 * �����ߣ����˾� ���ܣ��Ƿ��ǻ�λ����ֻ�����ڳ���ⵥ�� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	public boolean isLocatorMgt() {
		if (m_voBill != null && BillMode.Card == m_iCurPanel) {
			WhVO voWh = m_voBill.getWh();
			// ��λ����Ĳֿ���Ҫ����λ����
			if (voWh != null && voWh.getIsLocatorMgt() != null && voWh.getIsLocatorMgt().intValue() == 1) return true;
			else return false;

		}
		if (m_alListData == null || m_iLastSelListHeadRow < 0 || m_iLastSelListHeadRow >= m_alListData.size()) return false;
		GeneralBillVO vob = (GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow);
		if (vob != null) {
			WhVO voWh = vob.getWh();
			// ��λ����Ĳֿ���Ҫ����λ����
			if (voWh != null && voWh.getIsLocatorMgt() != null && voWh.getIsLocatorMgt().intValue() == 1) return true;
			else return false;
		} else return false;
	}

	/**
	 * �����ߣ����Ӣ ���ܣ�Ӧ���������༭�¼����� ������e���ݱ༭�¼� ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * ������*������=����
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
	 * �˴����뷽��˵���� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2001-7-18 15:01:19) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 */
	public void afterCorBillEdit(nc.ui.pub.bill.BillEditEvent e) {
		int iSelrow = e.getRow();
		GeneralBillItemVO[] vos = ((nc.ui.ic.pub.corbillref.ICCorBillRefModel) getICCorBillRef().getRefModel()).getSelectedVOs();

		if (vos == null || vos[0] == null) {
			// ��Ӧ���ݺ�
			getBillCardPanel().setBodyValueAt(null, iSelrow, m_sCorBillCodeItemKey);
			// ��Ӧ��������
			getBillCardPanel().setBodyValueAt(null, iSelrow, "ccorrespondtype");
			// ��Ӧ���ݱ�ͷOID
			// ����ģ����б���λ����������ʾ��ccorrespondhid,ccorrespondbid,�Ա�������Ķ�Ӧ��ͷ������OID
			getBillCardPanel().setBodyValueAt(null, iSelrow, "ccorrespondhid");
			// ��Ӧ���ݱ���OID
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
				// ���ӵ�����
				voBillPastLine();

				int irow = getBillCardPanel().getBillTable().getSelectedRow() - 1;
				// getBillCardPanel().setBodyValueAt(null, irow, m_sNumItemKey);
				// getBillCardPanel().setBodyValueAt(null, irow, m_sAstItemKey);
				// getBillCardPanel().setBodyValueAt(null, irow, "cvendorid");
				// getBillCardPanel().setBodyValueAt(null, irow, "vvendorname");

				synline(vos[j], irow, false);
				// ����ǳ��⴦����棬��ô�кž�Ҫ�������ɡ�
				if ("40080822".equals(m_sCurrentBillNode)) {
					String crowno = DataBuffer.getRowno();
					getBillCardPanel().setBodyValueAt(crowno, irow, "crowno");
					m_voBill.setItemValue(irow, "crowno", crowno);
					getBillCardPanel().setBodyValueAt(null, irow, "cgeneralbid");
					m_voBill.setItemValue(irow, "cgeneralbid", null);
				}
			}
			// ����ǳ��⴦����棬��ô�кž�Ҫ�������ɡ�
			if ("40080822".equals(m_sCurrentBillNode)) { return;
			// �����ٴ����к���
			}
			int iRowCount = vos.length;
			if (e.getRow() + iRowCount == getBillCardPanel().getRowCount()) {
				nc.ui.scm.pub.report.BillRowNo.addLineRowNos(getBillCardPanel(), m_sBillTypeCode, m_sBillRowNo, iRowCount);

			} else {
				nc.ui.scm.pub.report.BillRowNo.insertLineRowNos(getBillCardPanel(), m_sBillTypeCode, m_sBillRowNo, e.getRow() + iRowCount - 1, iRowCount - 1);
			}

			// dw ����������кź���� m_voBill
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
				// �ֹ����룬���ܻ����쳣��
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
				// ���ӵ�����
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
	 * �˴����뷽��˵���� �����ߣ����� ���ܣ����κŸı䴦�� ������ ���أ� ���⣺ ���ڣ�(2001-6-20 21:43:07)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
			// �ֹ����룬���ܻ����쳣��
			voLot = lotRef.getLotNumbRefVOs();
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
		}

		m_voBill.setItemValue(rownum, "vbatchcode", sLot);
		BatchCodeDefSetTool.setBatchCodeInfo(getBillCardPanel(), rownum, (String) m_voBill.getItemValue(rownum, "cinventoryid"), sLot, m_sCorpID, m_voBill.getItemVOs()[rownum]);

		// // �ȶ����������Ϊ������Ϊ0����������жϷ��ء�
		// UFDouble dNum = new
		// UFDouble(getBillCardPanel().getBodyValueAt(rownum,
		// m_sNumItemKey) == null ? "0" : getBillCardPanel()
		// .getBodyValueAt(rownum, m_sNumItemKey).toString().trim());
		// if ((m_iBillInOutFlag == InOutFlag.IN && dNum.doubleValue() >= 0)
		// || (m_iBillInOutFlag == InOutFlag.OUT && dNum.doubleValue() < 0 ||
		// m_bIsInitBill)) {
		// /** ��������⣬�򲻵����ֲ����κŵķ��� */
		// return;
		// }
		// if (m_sBillTypeCode.equals("45"))// edited by liubaohua 2011/01/21
		// {
		// // ����ǲɹ���ⵥ��ȡ���κŵ������Զ�������Ϊ��Դ��
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
				// ���ӵ�����
				iRowCount = getBillCardPanel().getBodyPanel().getTableModel().getRowCount() - iRowCount;
				nc.ui.scm.pub.report.BillRowNo.pasteLineRowNo(getBillCardPanel(), m_sBillTypeCode, m_sBillRowNo, iRowCount);
				voBillPastLine();

				int iSelrow = getBillCardPanel().getBillTable().getSelectedRow() - 1;

				synlot(voLot[j], iSelrow);
				// ����ǳ��⴦����棬��ô�кž�Ҫ�������ɡ�
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
	 * �����ߣ����Ӣ ���ܣ������ı��¼����� ������e���ݱ༭�¼� ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
	 * �����ߣ����Ӣ ���ܣ������ı��¼����� ������e���ݱ༭�¼� ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	public void afterNumEdit_1(int row) {

		/** ������գ�if �̶������ʣ���ո������� */
		Object oNumValue = getBillCardPanel().getBodyValueAt(row, m_sNumItemKey);

		if (oNumValue == null || oNumValue.toString().trim().length() < 1) {

			if (!m_bIsInitBill) {
				// ��ձ��е�ҵ������
				getBillCardPanel().setBodyValueAt(null, row, "dbizdate");

			}

			if (getBillCardPanel().getBodyItem("ncountnum") != null) {
				getBillCardPanel().setBodyValueAt(null, row, "ncountnum");

			}
		} else {

			UFDouble nNum = new UFDouble(oNumValue.toString().trim());

			// �����Դ����Ϊ���ⵥ�ݣ����������͸���������Ӧ����Ӧ�շ�����
			// afterNumEditFromSpe(e);

			if (!m_bIsInitBill) {
				if (getBillCardPanel().getBodyValueAt(row, "dbizdate") == null)

				// ���ڳ������Զ�����ҵ������
				getBillCardPanel().setBodyValueAt(m_sLogDate, row, "dbizdate");

			} else {
				if (getBillCardPanel().getBodyValueAt(row, "dbizdate") == null) {

					// �ڳ������Զ�����ϵͳ��������
					nc.vo.pub.lang.UFDate dstart = new nc.vo.pub.lang.UFDate(m_sStartDate);
					nc.vo.pub.lang.UFDate dbiz = dstart.getDateBefore(1);
					getBillCardPanel().setBodyValueAt(dbiz.toString(), row, "dbizdate");
				}
			}
			UFDouble npacknum = (UFDouble) m_voBill.getItemValue(row, "npacknum");  //��װ����
			if (npacknum != null && npacknum.doubleValue() != 0) {
				double ntemp = nNum.div(npacknum).abs().doubleValue();
				getBillCardPanel().setBodyValueAt(new UFDouble(Math.ceil(ntemp)), row, "ncountnum");
			}

		}
		// �������ԭ����ֵ��Ϊ�գ����Ҹı��˷�����ն�Ӧ����
		if (m_voBill.getItemVOs()[row].getInOutFlag() == InOutFlag.IN) clearCorrBillInfo(row);

		GeneralBillUICtl.synUi2Vo(getBillCardPanel(), m_voBill, new String[] {
				"ncountnum", "dbizdate"
		}, row);
		execEditFomulas(row, m_sNumItemKey);
		afterShouldNumEdit(new BillEditEvent(this, null, null, m_sShouldNumItemKey, row, BillItem.BODY));

		resetSpace(row);
	}

	/**
	 * ?user> ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2005-2-1 14:41:41) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @param e
	 *            nc.ui.pub.bill.BillEditEvent
	 */
	protected void afterOnRoadEdit(nc.ui.pub.bill.BillEditEvent e) {
		int iSelrow = e.getRow();

		UFBoolean bonroadflag = new UFBoolean(getBillCardPanel().getBodyValueAt(iSelrow, e.getKey()).toString());

		// ��Ӧ���ݺ�
		getBillCardPanel().setBodyValueAt(null, iSelrow, m_sCorBillCodeItemKey);
		// ��Ӧ��������
		getBillCardPanel().setBodyValueAt(null, iSelrow, "ccorrespondtype");
		// ��Ӧ���ݱ�ͷOID
		// ����ģ����б���λ����������ʾ��ccorrespondhid,ccorrespondbid,�Ա�������Ķ�Ӧ��ͷ������OID
		getBillCardPanel().setBodyValueAt(null, iSelrow, "ccorrespondhid");
		// ��Ӧ���ݱ���OID
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
	 * �����ߣ����˾� ���ܣ����ָ���С�ָ���е����� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	protected void clearRowData(int iBillFlag, int row, String sItemKey) {
		// �õ��������itemkey
		String[] saColKey = getClearIDs(iBillFlag, sItemKey);
		if (saColKey != null && saColKey.length > 0) clearRowData(row, saColKey);

	}

	/**
	 * �����ߣ����Ӣ ���ܣ����ָ���С�ָ���е����� ���������˸����� ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
		 * showHintMessage("δ�鵽�ô���ĸ�����"); }
		 */
	}

	/**
	 * ���˵��ݲ��� �����ߣ����� ���ܣ���ʼ�����չ��� ������ ���أ� ���⣺ ���ڣ�(2001-7-17 10:33:20)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 */
	public void filterRef(String sCorpID) {
		try {
			// ���˲ֿ����
			nc.ui.pub.bill.BillItem bi = getBillCardPanel().getHeadItem(m_sMainWhItemKey);
			// v31sp1����: �۱� 2005-09-13
			// ����ⵥ�Ĳֿ�����У����˵�ֱ�˲֣�����ϵͳ�����ģ�ֱ�˲ֲ���ʾҲû�����⡣
			RefFilter.filtWh(bi, sCorpID, new String[] {
				"and isdirectstore = 'N'"
			});

			if (!(m_sBillTypeCode.equals("4Y") || m_sBillTypeCode.equals("4E"))) {
				bi = getBillCardPanel().getHeadItem("cotherwhid");
				RefFilter.filtWh(bi, sCorpID, null);
			}
			// ���˿����֯ add by hanwei 2004-05-09
			bi = getBillCardPanel().getHeadItem("pk_calbody");
			RefFilter.filtCalbody(bi, sCorpID, null);

			// ���˴������
			bi = getBillCardPanel().getBodyItem("cinventorycode");
			nc.ui.pub.beans.UIRefPane invRef = (nc.ui.pub.beans.UIRefPane) bi.getComponent();
			invRef.setTreeGridNodeMultiSelected(true);
			invRef.setMultiSelectedEnabled(true);
			// invRef.getRefModel().setIsDynamicCol(true);
			// invRef.getRefModel().setDynamicColClassName("nc.ui.scm.pub.RefDynamic");

			RefFilter.filtInv(bi, sCorpID, null);

			// ���˱�ͷ�������
			bi = getBillCardPanel().getHeadItem("cinventoryid");
			RefFilter.filtInv(bi, sCorpID, null);
			// ���˱�ͷ�ڶ����ֿ����
			bi = getBillCardPanel().getHeadItem(m_sAstWhItemKey);
			if (bi != null && bi.getDataType() == BillItem.UFREF) RefFilter.filtWasteWh(bi, sCorpID, null);
			// ���˿ͻ�����
			bi = getBillCardPanel().getHeadItem("ccustomerid");
			RefFilter.filtCust(bi, sCorpID, null);

			// ����ҵ��Ա���� 2003-11-20
			bi = getBillCardPanel().getHeadItem("cbizid");
			RefFilter.filterPsnByDept(bi, sCorpID, new String[] {
				null
			});

			// ���˹�Ӧ�̲���
			bi = getBillCardPanel().getHeadItem("cproviderid");
			RefFilter.filtProvider(bi, sCorpID, null);
			// �����շ����Ͳ��գ�����ⲻһ����
			bi = getBillCardPanel().getHeadItem("cdispatcherid");
			if (m_iBillInOutFlag == InOutFlag.IN) RefFilter.filtDispatch(bi, sCorpID, 0, null);
			else RefFilter.filtDispatch(bi, sCorpID, 1, null);
			// ��ͷ���˵�ַ:�����Զ���飬�������ơ�
			if (getBillCardPanel().getHeadItem("vdiliveraddress") != null && getBillCardPanel().getHeadItem("vdiliveraddress").getComponent() != null) {
				nc.ui.pub.beans.UIRefPane vdlvr = (nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("vdiliveraddress").getComponent();
				vdlvr.setAutoCheck(false);
				vdlvr.setReturnCode(true);

				filterVdiliveraddressRef(true, -1);

				// ȥ����Լ����ӦΪӦ���ñ�����ջ��ͻ�Լ��
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
			// ���˳ɱ��������
			if (getBillCardPanel().getBodyItem(m_sCostObjectItemKey) != null && getBillCardPanel().getBodyItem(m_sCostObjectItemKey).getComponent() != null) {
				if (!m_sBillTypeCode.equals("4F")) {
					bi = getBillCardPanel().getBodyItem(m_sCostObjectItemKey);
					nc.ui.ic.pub.bill.initref.RefFilter.filterCostObject(bi);
					// filterCostObject();
				}
			}
			// // ���˼�������
			// nc.ui.pub.bill.BillItem bi2 = getBillCardPanel().getHeadItem(
			// "pk_measware");
			// // UIRefPane refCalbody =
			// //
			// (UIRefPane)getBillCardPanel().getHeadItem("pk_calbody").getComponent();
			// // String pk_calbody = refCalbody.getRefPK();
			// // String[] s = new String[1];
			// // s[0] = " and mm_jldoc.gcbm='" + pk_calbody + "'";
			// RefFilter.filtMeasware(bi2, sCorpID, null);

			// ���巢�˵�ַ
			bi = getBillCardPanel().getBodyItem("vdiliveraddress");
			if (bi != null) {
				((UIRefPane) bi.getComponent()).setAutoCheck(false);
			}

		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
		}
	}

	/**
	 * �����ߣ������� ���ܣ��ɴ���ĵ������͡��ֶΣ���õ����ֶθı��Ӧ�ı�������ֶ��б� ������iBillFlag
	 * �������ͣ���Ϊ��ͨ���ݣ�����0����Ϊ���ⵥ�ݣ�����1 ���� ��� cinventorycode�� ����ֿ� cwarehousename�� ������
	 * vfree0�� ��ͷ����ֿ� coutwarehouseid�� ��ͷ�ֿ� cwarehouseid ���أ� ���⣺ ���ڣ�(2001-7-18
	 * ���� 9:20) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @return java.lang.String[]
	 * @param sWhatChange
	 *            java.lang.String
	 */
	protected String[] getClearIDs(int iBillFlag, String sWhatChange) {
		if (sWhatChange == null) return null;
		String[] saReturnString = null;
		sWhatChange = sWhatChange.trim();
		// ���ٵ���ⵥʱҪ���⴦��
		// m_sCorBillCodeItemKey,
		// "ccorrespondtype","ccorrespondhid","ccorrespondbid"
		if (sWhatChange.equals("cinventorycode")) {
			// ���
			saReturnString = new String[] {
					"vbatchcode", "vfree0", m_sNumItemKey, m_sAstItemKey, "scrq", "dvalidate", m_sCorBillCodeItemKey, "ccorrespondtype", "ccorrespondhid", "ccorrespondbid", m_sShouldAstItemKey, "castunitid", "hsl", "nprice", "nmny", "cqualitylevelid",// �����ȼ�
					"vvendbatchcode",// ��Ӧ������
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
					"vdef20"// ����ʱ��
			};
		} else if ((sWhatChange.equals("cwarehousename")) && (iBillFlag == 1)) {
			// ���ⵥ�ı������ڲֿ�
			saReturnString = new String[] {
					"vbatchcode", m_sNumItemKey, m_sAstItemKey, "scrq", "dvalidate", "vfree0", "cqualitylevelid",// �����ȼ�
					"vvendbatchcode",// ��Ӧ������
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
					"vdef20"// ����ʱ��
			};
		} else if (sWhatChange.equals("vfree0")) {
			// ������
			saReturnString = new String[] {
					"vbatchcode", "scrq", "dvalidate", "cqualitylevelid",// �����ȼ�
					"vvendbatchcode",// ��Ӧ������
					"tchecktime"// ����ʱ��
			};
		} else if (sWhatChange.equals("coutwarehouseid")) {
			saReturnString = new String[] {
					"vbatchcode", m_sNumItemKey, m_sAstItemKey, "scrq", "dvalidate", "cqualitylevelid",// �����ȼ�
					"vvendbatchcode",// ��Ӧ������
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
					"vdef20"// ����ʱ��
			};
		} else if ((sWhatChange.equals(m_sMainWhItemKey)) && (iBillFlag == 0)) {
			// ��afterEdit�е���clearLocSN����locator/sn
			// saReturnString= new String[2];
			// saReturnString[0]= "locator";
			// saReturnString[1]= "sn";
			// saReturnString[2]= m_sAstItemKey;
			// saReturnString[3]= "scrq";
			// saReturnString[4]= "dvalidate";
		} else if (sWhatChange.equals("vbatchcode")) {
			// ����
			saReturnString = new String[] {
					"scrq", "dvalidate", "cqualitylevelid",// �����ȼ�
					"vvendbatchcode",// ��Ӧ������
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
					"vdef20"// ����ʱ��
			};
		} else if (sWhatChange.equals("castunitid")) {
			// ������
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
	 * �˴����뷽��˵���� ���ܣ��õ���Ӧ������ ������ ���أ� ���⣺ ���ڣ�(2001-7-18 10:54:47)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
	 * �˴����뷽��˵���� �������ڣ�(2001-7-11 ���� 11:19)
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
	 * ����û��ֹ��޸����κţ����⣬��ȷ����ʧЧ���ڼ���Ӧ���ݺţ�����ȷ����ա� �����ߣ����� ���ܣ� ������ ���أ� ���⣺
	 * ���ڣ�(2001-6-14 10:25:33) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
		// V501������ �����ʵ������¼�봦�� ���ע�͵�������
		// InvVO voInv = m_voBill.getItemInv(iSelrow);
		// if (!bOK) {
		// if (voInv.getNegallowed() == null
		// || !voInv.getNegallowed().booleanValue()) {
		// getBillCardPanel().setBodyValueAt(null, iSelrow, "vbatchcode");
		// getBillCardPanel().setBodyValueAt(null, iSelrow, "dvalidate"); //
		// ��ձ���ʧЧ����
		// getBillCardPanel().setBodyValueAt(null, iSelrow, "scrq"); // ��ձ�����������
		// getBillCardPanel().setBodyValueAt(null, iSelrow, "cqualitylevelid");
		// // ��ձ��������ȼ�
		// getBillCardPanel().setBodyValueAt(null, iSelrow, "vvendbatchcode");
		// // ��ձ��幩Ӧ������
		// getBillCardPanel().setBodyValueAt(null, iSelrow, "tchecktime"); //
		// ��ձ������ʱ��
		//				
		// String sKey=null;
		// for(int i=1;i<21;i++){
		// sKey="vdef"+String.valueOf(i).trim();
		// getBillCardPanel().setBodyValueAt(null, iSelrow, sKey); // ����Զ�����
		// }
		//				
		//				
		// }
		// }

	}

	/**
	 * �Ƿ�̶�������
	 */
	protected boolean isFixFlag(int row) {

		boolean isFixFlag = false;
		if (row >= 0 && m_voBill.getItemVOs()[row] != null && m_voBill.getItemVOs()[row].getIsSolidConvRate() != null) isFixFlag = m_voBill.getItemVOs()[row].getIsSolidConvRate().intValue() == 1 ? true : false;

		return isFixFlag;

	}

	/**
	 * ������ѯ �޸� �������ڣ�(2001-4-18 19:45:39)
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
	 * �����ߣ����˾� ���ܣ�ȷ�ϣ����棩���� �������� ���أ� true: �ɹ� false: ʧ��
	 * 
	 * ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 2001/10/29,wnj,��ֳ���������/�����޸�����������ʹ�ø��淶��
	 * 
	 * 
	 * 
	 */
	public boolean onSave() {
		long lStartTime = System.currentTimeMillis();
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH044")/* @res "���ڱ���" */);

		getBillCardPanel().getBillData().execBodyValidateFormulas();

		boolean bSave = false;
		bSave = onSaveBase();
		// �����б����Ķ���
		if (bSave) {
			setBillBCVOStatus(m_voBill, nc.vo.pub.VOStatus.UNCHANGED);
		}
		if (m_bOnhandShowHidden) {
			m_pnlQueryOnHand.clearCache();
			m_pnlQueryOnHand.fresh();
		}
		// v5 ����ǲ����������ŵ��ݣ�����ɹ���Ҫɾ���б��µĶ�Ӧ����
		if (m_bRefBills && bSave) {
			// ɾ���б��µĶ�Ӧ����
			// delBillOfList(m_iLastSelListHeadRow);
			if (m_iLastSelListHeadRow >= 0) removeBillsOfList(new int[] {
				m_iLastSelListHeadRow
			});
			updateHeadTsWhenMutiBillSave(m_voBill);
			// �л�
			onSwitch();
		}
		nc.vo.scm.pub.SCMEnv.showTime(lStartTime, "Bill Save");

		return bSave;
	}

	/**
	 * �����ߣ�yangbo
	 * 
	 * ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * ��ʽ���ɶ൥ʱ������Դ������ͬ�ĵ��ݣ���Ҫˢ����ص��ݵ���Դ���ݱ�ͷts
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
	 * �����ߣ�yangbo
	 * 
	 * ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * ��ʽ���ɶ൥ʱ������Դ������ͬ�ĵ��ݣ���Ҫˢ����ص��ݵ���Դ���ݱ�ͷts
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
	 * ���û�ѡ�����κź��Զ�������ʧЧ�������Ӧ����ţ��������͡� �����ߣ����� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2001-6-13
	 * 17:38:31) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 */
	public void pickupLotRef(String colname) {
		String s = colname;
		// ���κŲ��մ���ʧЧ���ںͶ�Ӧ���ݺż���Ӧ��������

		String sbatchcode = null;
		int iSelrow = getBillCardPanel().getBillTable().getSelectedRow();
		if (s == null) { return; }
		if (s.equals("vbatchcode")) {
			// �жϵ����κŲ���Ϊ��ʱ,Ӧ�÷���;
			// if(arytemp[0]==null||arytemp[3]==null){
			// return;
			// }

			sbatchcode = (String) getBillCardPanel().getBodyValueAt(iSelrow, "vbatchcode");

			if (sbatchcode != null && sbatchcode.trim().length() > 0) {
				nc.vo.scm.ic.bill.InvVO voInv = m_voBill.getItemInv(iSelrow);
				// /����λ
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
					// �����ε�ʧЧ����
					getBillCardPanel().setBodyValueAt(getLotNumbRefPane().getRefInvalideDate() == null ? "" : getLotNumbRefPane().getRefInvalideDate().toString(), iSelrow, "dvalidate");
				} catch (Exception e) {
				}
				if (getLotNumbRefPane().getRefBillCode() != null) {
					try {
						// ��Ӧ���ݺ�
						getBillCardPanel().setBodyValueAt(getLotNumbRefPane().getRefBillCode() == null ? "" : getLotNumbRefPane().getRefBillCode(), iSelrow, m_sCorBillCodeItemKey);
					} catch (Exception e) {
					}
					try {
						// ��Ӧ��������
						getBillCardPanel().setBodyValueAt(getLotNumbRefPane().getRefBillType() == null ? "" : getLotNumbRefPane().getRefBillType(), iSelrow, "ccorrespondtype");
					} catch (Exception e) {
					}
					try {
						// ��Ӧ���ݱ�ͷOID
						// ����ģ����б���λ����������ʾ��ccorrespondhid,ccorrespondbid,�Ա�������Ķ�Ӧ��ͷ������OID
						getBillCardPanel().setBodyValueAt(getLotNumbRefPane().getRefTableHeaderID() == null ? "" : getLotNumbRefPane().getRefTableHeaderID(), iSelrow, "ccorrespondhid");
					} catch (Exception e) {
					}
					try {
						// ��Ӧ���ݱ���OID
						getBillCardPanel().setBodyValueAt(getLotNumbRefPane().getRefTableBodyID() == null ? "" : getLotNumbRefPane().getRefTableBodyID(), iSelrow, "ccorrespondbid");

					} catch (Exception e) {
					}
				}

				try {
					// //��������
					if (voInv.getIsValidateMgt().intValue() == 1) {
						nc.vo.pub.lang.UFDate dvalidate = getLotNumbRefPane().getRefInvalideDate();
						if (dvalidate != null) {
							getBillCardPanel().setBodyValueAt(dvalidate.getDateBefore(voInv.getQualityDay().intValue()).toString(), iSelrow, "scrq");
						}

					}
				} catch (Exception e) {
				}

				// /������
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

				// ͬ���ı�m_voBill
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
				// //��ձ�������ʧЧ����
				// getBillCardPanel().setBodyValueAt("", iSelrow,
				// m_sCorBillCodeItemKey);
				// //��ձ������ж�Ӧ���ݺ�
				// getBillCardPanel().setBodyValueAt("", iSelrow,
				// "ccorrespondtype");
				// //��ձ������ж�Ӧ��������
				// getBillCardPanel().setBodyValueAt("", iSelrow,
				// "ccorrespondhid");
				// //��ձ������ж�Ӧ���ݱ�ͷOID
				// getBillCardPanel().setBodyValueAt("", iSelrow,
				// "ccorrespondbid");
				// //��ձ������ж�Ӧ���ݱ���OID
				// getBillCardPanel().setBodyValueAt("", iSelrow, "scrq");
				// //��ձ�������ʧЧ����
				// //ͬ���ı�m_voBill
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
	 * �����ߣ����Ӣ ���ܣ�Ӧ���������༭�¼����� ������e���ݱ༭�¼� ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * ������*������=����
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
	 * �˴����뷽��˵���� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2001-7-18 15:01:19) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
	 * �����ߣ����˾� ���ܣ����ݱ༭���� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	abstract protected void afterBillEdit(nc.ui.pub.bill.BillEditEvent e);

	/**
	 * �����ߣ����˾� ���ܣ���������ѡ��ı� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	abstract protected void afterBillItemSelChg(int iRow, int iCol);

	/**
	 * �����ߣ����˾� ���ܣ������֯�ı��¼����� ������e���ݱ༭�¼� ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	public void afterCalbodyEdit(nc.ui.pub.bill.BillEditEvent e) {
		try {
			String sNewID = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem(e.getKey()).getComponent()).getRefPK();
			// ����˿����֯
			// �����ǰ�Ĳֿⲻ����
			// ���˲ֿ����
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
			// // ���˼�������
			// nc.ui.pub.bill.BillItem bi1 = getBillCardPanel().getHeadItem(
			// "pk_measware");
			// RefFilter.filtMeasware(bi1, m_sCorpID, sConstraint1);
			// ����:clear warehouse
			nc.ui.pub.bill.BillItem biWh = getBillCardPanel().getHeadItem(m_sMainWhItemKey);
			if (biWh != null) biWh.setValue(null);

			// ���˳ɱ�����
			// filterCostObject();

		} catch (Exception e2) {
			SCMEnv.out(e2);
		}

	}

	/**
	 * �����ߣ������� ���ܣ��������޸� ������ ���أ� ���⣺ ���ڣ�(2001-11-20 14:01:52) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @param row
	 *            int
	 */
	public void afterHslEdit(nc.ui.pub.bill.BillEditEvent e) {
		// �У�ѡ�б�ͷ�ֶ�ʱΪ-1
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
	 * �����ߣ����Ӣ ���ܣ���λ�޸��¼����� ������e���ݱ༭�¼� ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
	 * �����ߣ����˾� ���ܣ����ݱ���༭�¼�ǰ�������� ������e���ݱ༭�¼� ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */

	abstract public boolean beforeBillItemEdit(nc.ui.pub.bill.BillEditEvent e);

	/**
	 * �����ߣ����˾� ���ܣ���������ѡ��ı� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	abstract protected void beforeBillItemSelChg(int iRow, int iCol);

	/**
	 * UAP�ṩ�ı༭ǰ����
	 * 
	 * @param value
	 * @param row
	 * @param itemkey
	 * @return
	 */
	public boolean isCellEditable(boolean value/* BillModel��isCellEditable�ķ���ֵ */, int row/* ��������� */, String itemkey/* ��ǰ�е�itemkey */) {

		// nc.vo.scm.pub.ctrl.GenMsgCtrl.printHint("beforeEdit:" + e.getKey());
		if (m_iMode == BillMode.Browse) return false;
		getBillCardPanel().stopEditing();

		boolean isEditable = true;
		String sItemKey = itemkey;
		nc.ui.pub.bill.BillItem biCol = getBillCardPanel().getBodyItem(sItemKey);
		int iRow = row;
		// int iPos = e.getPos();
		// forHF,��ʱ�á���ͻ�������������ͬһ���θ�������ͬ�����afterEdit�������ˡ�
		Object oBatchcode = getBillCardPanel().getBodyValueAt(iRow, "vbatchcode");
		if (oBatchcode != null) {
			getBillCardPanel().setBodyValueAt(oBatchcode.toString().trim(), iRow, "vbatchcode");
		}

		if (sItemKey == null || biCol == null) { return false; }

		// ģ������
		if (!biCol.isEdit() || !biCol.isEnabled()) {

		return false; }

		if (m_voBill == null) {
			// biCol.setEnabled(false);
			return false;
		}
		nc.vo.scm.ic.bill.WhVO voWh = m_voBill.getWh();
		if (voWh == null) voWh = m_voBill.getWasteWh();

		// ֱ�˲ֿ⡢ֱ�ӵ���,���ɱ༭
		if (voWh != null && (voWh.getIsdirectstore() != null && voWh.getIsdirectstore().booleanValue()) || (m_voBill.getHeaderVO().getBdirecttranflag() != null && m_voBill.getHeaderVO().getBdirecttranflag().booleanValue())) {
			// biCol.setEnabled(false);
			return false;
		}

		// ��Դ���ݿ��ƣ�
		String csourcetype = (String) m_voBill.getItemValue(iRow, "csourcetype");

		// �Ƿ����׵������롢��
		boolean isDispend = false;

		String sthistype = m_sBillTypeCode;
		// ����������Դ�ǲɹ���
		if ((BillTypeConst.m_otherOut.equals(sthistype) && csourcetype != null && csourcetype.equals(BillTypeConst.m_purchaseIn)) || (BillTypeConst.m_otherIn.equals(sthistype) && csourcetype != null && csourcetype.equals(BillTypeConst.m_saleOut))) isDispend = true;

		// ��Դ�ڿ�����ⵥ
		boolean isFromICSp = false;

		if (csourcetype != null && (csourcetype.equals(BillTypeConst.m_assembly) || csourcetype.equals(BillTypeConst.m_disassembly) || csourcetype.equals(BillTypeConst.m_transform) || csourcetype.equals(BillTypeConst.m_check) || isDispend)) isFromICSp = true;

		// �Ƿ���;
		UFBoolean bonroadflag = (UFBoolean) m_voBill.getItemValue(iRow, "bonroadflag");
		if (bonroadflag == null) bonroadflag = new UFBoolean(false);

		// �����
		if (sItemKey.equals("cinventorycode")) {
			((nc.ui.pub.beans.UIRefPane) biCol.getComponent()).getUITextField().setEditable(true);
			// ���˴��
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
				// v5:�������������Դ��������Ʒ, �ұ�������Ʒ, ������޸Ĵ��
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
				// ֱ�Ӹ��ݵ�ǰ���������ˣ����⵱�����޸�ʱ�޷�����ʱ�ǵ�ǰ�Ĵ��
				// �޸� by hanwei 2003-11-09
				String sPk_invman = (String) m_voBill.getItemValue(iRow, "cinventoryid");
				// �����滻��
				RefFilter.filtReplaceInv(biCol, m_sCorpID, new String[] {
					sPk_invman
				});
				((nc.ui.pub.beans.UIRefPane) biCol.getComponent()).getUITextField().setEditable(false);
			}

		}
		// �Ǵ���У�������������
		else {

			// �������
			Object oTempInvCode = getBillCardPanel().getBodyValueAt(iRow, "cinventorycode");
			// �����
			// Object oTempInvName = getBillCardPanel().getBodyValueAt(iRow,
			// "invname");
			// �������δ����������մ�����������в��ɱ༭��
			if (oTempInvCode == null || oTempInvCode.toString().trim().length() == 0) {
				// biCol.setEnabled(false);
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000026")/* @res "����������!" */);
				return false;
			}
		}

		InvVO voInv = m_voBill.getItemInv(iRow);

		// ����Ǹ���������Ĵ��������������(Ӧ/ʵ)������:V31,�����������븨������
		if (sItemKey.equals(m_sAstItemKey) || sItemKey.equals(m_sShouldAstItemKey) || sItemKey.equals(m_sNumItemKey) || sItemKey.equals(m_sShouldNumItemKey) || sItemKey.equals("ngrossastnum") || sItemKey.equals("ntareastnum")) {

			// if (isFromICSp)
			// isEditable = false;
			/*
			 * if ( voInv.getIsAstUOMmgt() != null &&
			 * voInv.getIsAstUOMmgt().intValue() == 1) { Object castunitid =
			 * getBillCardPanel().getBodyValueAt(iRow, "castunitid"); if
			 * (castunitid == null || castunitid.toString().trim().length() ==
			 * 0) { showHintMessage("�������븨����!"); isEditable = false; } }
			 */
			// Ӧ������,����Դ
			if (csourcetype != null && (sItemKey.equals(m_sShouldAstItemKey) || sItemKey.equals(m_sShouldNumItemKey))) isEditable = false;
		}

		// ������
		if (sItemKey.equals("castunitname") || sItemKey.equals(m_sShouldAstItemKey) || sItemKey.equals(m_sAstItemKey) || sItemKey.equals("hsl") || sItemKey.equals("ngrossastnum") || sItemKey.equals("ntareastnum")) {
			if (voInv.getIsAstUOMmgt() == null || voInv.getIsAstUOMmgt().intValue() != 1) {
				isEditable = false;
			}
			// ���˸���λ
			else {
				if (sItemKey.equals("castunitname")) filterMeas(iRow);
				// �̶������ʲ��ɱ༭
				else if (sItemKey.equals("hsl") && m_voBill.getItemVOs()[iRow].getIsSolidConvRate() != null && m_voBill.getItemVOs()[iRow].getIsSolidConvRate().intValue() == 1) {
					isEditable = false;
				}
				// ������ڶ�Ӧ��ⵥ��Ϣ����������Ŀ��Ϣ�����޸�
				if (getBillCardPanel().getBodyItem(m_sCorBillCodeItemKey) != null && getBillCardPanel().getBodyValueAt(row, m_sCorBillCodeItemKey) != null && getBillCardPanel().getBodyValueAt(row, m_sCorBillCodeItemKey).toString().trim().length() > 0) {
					if (sItemKey.equals("castunitname") || (sItemKey.equals("hsl") && voInv.getIsStoreByConvert().intValue() == 1)) isEditable = false;
				}

			}

		}
		// ������
		else if (sItemKey.equals("vfree0")) {
			if (voInv.getIsFreeItemMgt() == null || voInv.getIsFreeItemMgt().intValue() != 1) {
				isEditable = false;
			}
			// �������������
			else {
				// ����������մ�������
				getFreeItemRefPane().setFreeItemParam(voInv);
				// ������ڶ�Ӧ��ⵥ��Ϣ����������Ŀ��Ϣ�����޸�
				if (getBillCardPanel().getBodyItem(m_sCorBillCodeItemKey) != null && getBillCardPanel().getBodyValueAt(row, m_sCorBillCodeItemKey) != null && getBillCardPanel().getBodyValueAt(row, m_sCorBillCodeItemKey).toString().trim().length() > 0) {
					isEditable = false;
				} else isEditable = true;

			}
		}

		// ���˻�λ����
		else if (sItemKey.equals("vspacename")) {

			if (voWh != null && voWh.getIsLocatorMgt() != null && voWh.getIsLocatorMgt().intValue() == 1) {
				filterSpace(iRow);

			} else {
				isEditable = false;
			}
			// // ������ڶ�Ӧ��ⵥ��Ϣ����������Ŀ��Ϣ�����޸�
			// if (getBillCardPanel().getBodyItem(m_sCorBillCodeItemKey) != null
			// && getBillCardPanel().getBodyValueAt(row,
			// m_sCorBillCodeItemKey) != null
			// && getBillCardPanel().getBodyValueAt(row,
			// m_sCorBillCodeItemKey).toString().trim().length() > 0) {
			// isEditable = false;
			// }

		}
		// ����
		else if (sItemKey.equals("vbatchcode")) {
			if (voInv.getIsLotMgt() != null && voInv.getIsLotMgt().intValue() == 1) {
				String ColName = biCol.getName();
				// ��ⵥ���ڳ�����
				// zhy2006-04-16�����������κŵ���,�ʴ˴������ڳ��������⴦��,ע�����¸���
				// if (m_bIsInitBill) {
				// // ||(m_voBill.getItemVOs()[iRow].getInOutFlag()!=
				// // InOutFlag.OUT
				// // &&m_voBill.getBillInOutFlag()!=InOutFlag.OUT))
				// // {
				// /** �õ�ԭ�������ϵ����κ� */
				// String sBatchCode = (String) getBillCardPanel()
				// .getBodyValueAt(iRow, "vbatchcode");
				// /** ��TextField ��CellEditor����ԭ���յ�CellEditor */
				// nc.ui.pub.beans.UITextField tfLot = new
				// nc.ui.pub.beans.UITextField(
				// sBatchCode);
				// tfLot.setMaxLength(biCol.getLength());
				// getBillCardPanel().getBodyPanel().getTable().getColumn(
				// ColName).setCellEditor(
				// new nc.ui.pub.bill.BillCellEditor(tfLot));

				// } else {
				// reset length !!! 06-26
				// forHF,��ʱ�á���ͻ�������������ͬһ���θ�������ͬ�����afterEdit�������ˡ�
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
			// ������ڶ�Ӧ��ⵥ��Ϣ����������Ŀ��Ϣ�����޸�
			if (getBillCardPanel().getBodyItem(m_sCorBillCodeItemKey) != null && getBillCardPanel().getBodyValueAt(row, m_sCorBillCodeItemKey) != null && getBillCardPanel().getBodyValueAt(row, m_sCorBillCodeItemKey).toString().trim().length() > 0) {
				isEditable = false;
			}

		}

		// ����������
		else if (sItemKey.equals(m_sCorBillCodeItemKey)) {
			// ��������³��������ⲻ���Ա༭��
			// 1:��ⵥ�ݲ����˻���2�����ⵥ�����˻���3����ⵥ������Ϊ����4�����ⵥ������Ϊ��
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
				// ���Ա༭
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
				 * //���ڳ����ݲ��Ҳ������,���ܱ༭ if (!m_bIsInitBill &&
				 * m_voBill.getItemVOs()[iRow].getInOutFlag() != InOutFlag.IN) {
				 * isEditable = false; }
				 */
				isEditable = true;
			} else isEditable = false;

			// zhy�����������κ������κŵ����д���,���������ں�ʵЧ���ڲ�����༭
			if (isEditable) {
				String vbatchcode = (String) getBillCardPanel().getBodyValueAt(iRow, "vbatchcode");
				if (vbatchcode != null && isExistInBatch(voInv.getCinventoryid(), vbatchcode)) isEditable = false;
			}
		}
		// ��Ŀ
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
		// �ɱ�����
		else if (sItemKey.equals(m_sCostObjectItemKey)) {
			// ί�ⷢ�ϲ���Ҫ�ӹ�ƷΪ�ɱ�����
			if (!m_sBillTypeCode.equals("4F")) {
				filterCostObject();
			}
			// get id if null then set refpk = null
			String costid = (String) getBillCardPanel().getBodyValueAt(row, "ccostobject");
			if (costid == null) ((UIRefPane) getBillCardPanel().getBodyItem(m_sCostObjectItemKey).getComponent()).setPK(null);
			else ((UIRefPane) getBillCardPanel().getBodyItem(m_sCostObjectItemKey).getComponent()).setPK(costid);
		}
		// ��;���
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
		// ��Ʒflargess
		else if (sItemKey.equals(IItemKey.FLARGESS)) {
			// ���ƿ��Ա༭�������Ʋ��ɱ༭��
			// ���ڷ����Ƶ����:��Դ�ǲɹ�����������bsourcelargessΪ��Ŀ��Ա༭��Ϊ�ǲ��ɱ༭
			if (csourcetype == null) isEditable = true;
			else if ((sthistype.equals("45") && !(isBrwLendBiztype())) || sthistype.equals("47")) {

				// �޸��ˣ������� �޸����ڣ�2007-05-21
				// �޸�ԭ�򣺸�������Ӧ���ж�bsourcelargess��������flargess
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
				// // 2004-12-13 ydy �����������Ʒ���򲻿ɱ༭��Ʒ��־
				// boolean isbsourcelargess = m_voBill.getItemVOs()[iRow]
				// .getBsourcelargess().booleanValue();
				// if (isbsourcelargess)
				// isEditable = false;
				// else
				// isEditable = true;

			} else isEditable = false;

			// �������csourcetype.startsWith("23"):��Դ����Ϊ�ɹ�����������Ʒ�����Ա༭Ϊ
			// if (csourcetype != null && csourcetype.equalsIgnoreCase("23"))
			// isEditable = false;

		} else if (sItemKey.endsWith("prcie") || sItemKey.endsWith("mny")) {
			if (m_voBill.getItemValue(iRow, "flargess") != null && ((UFBoolean) m_voBill.getItemValue(iRow, "flargess")).booleanValue()) isEditable = false;

		} else if (sItemKey.equals("cmeaswarename")) {
			// ���˴��
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

		// ����������ʱ����幩Ӧ�̲��ܱ༭
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
	 * beforeEdit ����ע�⡣[�����ͷ�༭ǰ�¼�]
	 */
	public boolean beforeEdit(nc.ui.pub.bill.BillItemEvent e) {
		getBillCardPanel().stopEditing();
		String sItemKey = e.getItem().getKey();
		BillItem bi = e.getItem();
		if (m_iMode == BillMode.Browse) return false;
		if (!bi.isEdit()) { return false; }
		if (!bi.isEnabled()) return false;

		// �ֿ�༭ǰ��Ҫ���տ����֯����
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
		// // ���˼�������
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
	 * �����ߣ����˾� ���ܣ����ݱ༭�¼����� ������e���ݱ༭�¼� ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
		 * //ģ������ if (!biCol.isEdit()) {
		 * 
		 * return false; }
		 * 
		 * if (m_voBill == null) { biCol.setEnabled(false); return false; }
		 * nc.vo.scm.ic.bill.WhVO voWh = m_voBill.getWh(); if (voWh == null)
		 * voWh = m_voBill.getWasteWh();
		 * 
		 * //ֱ�˲ֿ⡢ֱ�ӵ���,���ɱ༭ if (voWh!=null&&(voWh.getIsdirectstore() != null &&
		 * voWh.getIsdirectstore().booleanValue()) ||
		 * (m_voBill.getHeaderVO().getBdirecttranflag() != null &&
		 * m_voBill.getHeaderVO().getBdirecttranflag().booleanValue())) {
		 * biCol.setEnabled(false); return false; }
		 * 
		 * //��Դ���ݿ��ƣ� String csourcetype = (String) m_voBill.getItemValue(iRow,
		 * "csourcetype"); //�Ƿ����׵������롢�� boolean isDispend=false;
		 * 
		 * String sthistype=m_voBill.getBillTypeCode(); //����������Դ�ǲɹ���
		 * if((BillTypeConst
		 * .m_otherOut.equals(sthistype)&&csourcetype!=null&&csourcetype
		 * .equals(BillTypeConst.m_purchaseIn))
		 * ||(BillTypeConst.m_otherIn.equals
		 * (sthistype)&&csourcetype!=null&&csourcetype
		 * .equals(BillTypeConst.m_saleOut)) ) isDispend=true;
		 * 
		 * //��Դ�ڿ�����ⵥ boolean isFromICSp = false;
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
		 * //����� if (sItemKey.equals("cinventorycode")) { //���˴��
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
		 * //ֱ�Ӹ��ݵ�ǰ���������ˣ����⵱�����޸�ʱ�޷�����ʱ�ǵ�ǰ�Ĵ�� // �޸� by hanwei 2003-11-09 String
		 * sPk_invman = (String) m_voBill.getItemValue(iRow, "cinventoryid");
		 * //�����滻�� RefFilter.filtReplaceInv(biCol, m_sCorpID, new String[] {
		 * sPk_invman }); // ((nc.ui.pub.beans.UIRefPane)
		 * biCol.getComponent()).getUITextField().setEditable(false); } else if
		 * (isFromICSp) { biCol.setEnabled(true); return false; } }
		 * //�Ǵ���У������������� else {
		 * 
		 * //������� Object oTempInvCode = getBillCardPanel().getBodyValueAt(iRow,
		 * "cinventorycode"); //����� Object oTempInvName =
		 * getBillCardPanel().getBodyValueAt(iRow, "invname");
		 * //�������δ����������մ�����������в��ɱ༭�� if (oTempInvCode == null ||
		 * oTempInvCode.toString().trim().length() == 0) {
		 * biCol.setEnabled(false); showHintMessage("����������!"); return false; }
		 * }
		 * 
		 * InvVO voInv = m_voBill.getItemInv(iRow);
		 * 
		 * //����Ǹ���������Ĵ��������������(Ӧ/ʵ)������ if (sItemKey.equals(m_sAstItemKey) ||
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
		 * showHintMessage("�������븨����!"); isEditable = false; } } //Ӧ������,����Դ //if
		 * (csourcetype != null //&& (sItemKey.equals(m_sShouldAstItemKey) ||
		 * sItemKey.equals(m_sShouldNumItemKey))) //isEditable = false; }
		 * 
		 * //������ if (sItemKey.equals("castunitname") ||
		 * sItemKey.equals(m_sShouldAstItemKey) ||
		 * sItemKey.equals(m_sAstItemKey) || sItemKey.equals("hsl")) { if
		 * (isFromICSp || voInv.getIsAstUOMmgt() == null ||
		 * voInv.getIsAstUOMmgt().intValue() != 1) { isEditable = false; }
		 * //���˸���λ else { if (sItemKey.equals("castunitname")) filterMeas(iRow);
		 * //�̶������ʲ��ɱ༭ else if ( sItemKey.equals("hsl") &&
		 * m_voBill.getItemValue(iRow, "isSolidConvRate") != null && ((Integer)
		 * m_voBill.getItemValue(iRow, "isSolidConvRate")).intValue() == 1) {
		 * isEditable = false; } } } //������ else if (sItemKey.equals("vfree0")) {
		 * if (isFromICSp || voInv.getIsFreeItemMgt() == null ||
		 * voInv.getIsFreeItemMgt().intValue() != 1) { isEditable = false; }
		 * //������������� else { //����������մ�������
		 * getFreeItemRefPane().setFreeItemParam(voInv); } }
		 * 
		 * //���˻�λ���� else if (sItemKey.equals("vspacename")) {
		 * 
		 * if (voWh != null && voWh.getIsLocatorMgt() != null &&
		 * voWh.getIsLocatorMgt().intValue() == 1) filterSpace(iRow); else {
		 * isEditable = false; } } //���� else if (sItemKey.equals("vbatchcode"))
		 * { if (voInv.getIsLotMgt() != null && voInv.getIsLotMgt().intValue()
		 * == 1) { String ColName = biCol.getName(); //��ⵥ���ڳ����� if
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
		 * //���������� else if (sItemKey.equals(m_sCorBillCodeItemKey)) { //���Ա༭
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
		 * voInv.getIsValidateMgt().intValue() == 1) { //���ڳ����ݲ��Ҳ������,���ܱ༭ if
		 * (!m_bIsInitBill && m_voBill.getItemVOs()[iRow].getInOutFlag() !=
		 * InOutFlag.IN) { isEditable = false; } } else isEditable = false; }
		 * //��Ŀ else if ( sItemKey.equals("cprojectphasename") && (m_iMode ==
		 * BillMode.New || m_iMode == BillMode.Update)) { String spk = (String)
		 * m_voBill.getItemValue(iRow, "cprojectphaseid"); String sName =
		 * (String) m_voBill.getItemValue(iRow, "cprojectphasename");
		 * m_refJobPhase.setPK(spk); m_refJobPhase.setName(sName); String
		 * cprojectid = (String) getBillCardPanel().getBodyValueAt(iRow,
		 * "cprojectid"); if (cprojectid != null) {
		 * m_refJobPhaseModel.setJobID(cprojectid); } else { isEditable = false;
		 * } } //�ɱ����� else if (sItemKey.equals(m_sCostObjectItemKey)) {
		 * filterCostObject(); }
		 * 
		 * //��Ʒflargess else if (sItemKey.equals("flargess")) {
		 * //���ƿ��Ա༭�������Ʋ��ɱ༭�� //���ڷ����Ƶ����:��Դ�ǲɹ�����������bsourcelargessΪ��Ŀ��Ա༭��Ϊ�ǲ��ɱ༭
		 * if(csourcetype==null) isEditable=true; else
		 * if(sthistype.equals("45")){ //2004-12-13 ydy �����������Ʒ���򲻿ɱ༭��Ʒ��־
		 * boolean isbsourcelargess =
		 * m_voBill.getItemVOs()[iRow].getBsourcelargess().booleanValue();
		 * if(isbsourcelargess) isEditable=false; else isEditable=true; }else
		 * isEditable=false;
		 * 
		 * 
		 * //�������csourcetype.startsWith("23"):��Դ����Ϊ�ɹ�����������Ʒ�����Ա༭Ϊ if
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
	 * ����ϼơ� �������ڣ�(2001-10-24 16:33:58)
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
	 * ���ݻ����ʼ���ë�غ����ء� �������ڣ�(2005-04-05 16:33:58)
	 * 
	 * @return void
	 * @param hsl
	 *            UFDouble
	 */
	public void calOtherNumByHsl(String sMainNum, String sAstNum, int rownum, UFDouble hsl) {
		if (hsl == null) return;

		// ������Ƥ��ë��
		UFDouble nMain = null;
		UFDouble nAst = null;

		Object oMain = getBillCardPanel().getBodyValueAt(rownum, sMainNum);
		Object oAst = getBillCardPanel().getBodyValueAt(rownum, sAstNum);

		if (oMain != null) nMain = new UFDouble(oMain.toString().trim());
		if (oAst != null) nAst = new UFDouble(oAst.toString().trim());

		/* �������������ʣ����۹̶����䶯�����ʣ����������������������¼��������� */
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
	 * �����ߣ����˾� ���ܣ����󷽷�������ǰ��VO��� �����������浥�� ���أ� ���⣺ ���ڣ�(2001-5-24 ���� 5:17)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	abstract protected boolean checkVO(GeneralBillVO voBill);

	/**
	 * �����ߣ����˾� ���ܣ���ն�Ӧ������ ������ ���أ� ���⣺ ���ڣ�(2001-11-23 18:11:18)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	public void clearCorrBillInfo(int rownum) {
		// ��ն�Ӧ����
		// ��Ӧ���ݺ�
		try {
			getBillCardPanel().setBodyValueAt(null, rownum, m_sCorBillCodeItemKey);
		} catch (Exception e3) {
		}
		try {
			// ��Ӧ��������
			getBillCardPanel().setBodyValueAt(null, rownum, "ccorrespondtype");
		} catch (Exception e4) {
		}
		try {
			// ��Ӧ���ݱ�ͷOID
			// ����ģ����б���λ����������ʾ��ccorrespondhid,ccorrespondbid,�Ա�������Ķ�Ӧ��ͷ������OID
			getBillCardPanel().setBodyValueAt(null, rownum, "ccorrespondhid");
		} catch (Exception e5) {
		}
		try {
			// ��Ӧ���ݱ���OID
			getBillCardPanel().setBodyValueAt(null, rownum, "ccorrespondbid");
		} catch (Exception e6) {
		}
		// ͬ���ı�m_voBill
		m_voBill.setItemValue(rownum, "ccorrespondbid", null);
		m_voBill.setItemValue(rownum, "ccorrespondhid", null);
		m_voBill.setItemValue(rownum, m_sCorBillCodeItemKey, null);
		m_voBill.setItemValue(rownum, "ccorrespondtype", null);

	}

	/**
	 * �����ߣ����˾� ���ܣ������Ӧ�л�λ�����к����� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	protected void clearLocSnData(int row, String sItemKey) {
		// ��Ϊ�ֿ��޸ĺ�row==-1,�������ﲻ���row�Ϸ���
		if (sItemKey == null || sItemKey.length() == 0) return;
		// ��ʾ��Ϣ
		String sHintMsg = "";
		// �Ƿ����λ�����к�
		boolean bClearLoc = false, bClearSn = false;

		if (sItemKey.equals("bonroadflag")) {
			bClearLoc = true;
			// bClearSn=true;
		}

		// �ֿ�
		if (sItemKey.equals(m_sMainWhItemKey)) {
			// ��Ҫ��� ---------- ���� ----------- ��λ��!!!
			if (m_alLocatorData != null && m_alSerialData != null) {
				for (int q = 0; q < m_alLocatorData.size(); q++) {
					m_alLocatorData.set(q, null);
					// ���кźͻ�λ��أ�ҲӦ��ȫ�����
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
			// �ֿ��Ƿ��λ����---�ǻ�λ����ʱ�ڲֿ�ı�ʱ��ա�
			boolean bIsLocatorMgt = isLocatorMgt();
			// ��ǰ���Ƿ����кŹ���---������ȥ����
			// boolean bIsSNmgt = isSNmgt(row);

			// ���µ��жϲ���else���
			// ���
			if (sItemKey.equals("cinventorycode") || sItemKey.equals(m_sNumItemKey) || sItemKey.equals(m_sAstItemKey) || sItemKey.equals(m_sCorBillCodeItemKey) || sItemKey.equals("vvendorname") || sItemKey.equals("headprovider")) {
				// //���ڳ���ʱ��Ҫ���λ��
				// if (!m_bIsInitBill)
				// bClearLoc = true;
				bClearSn = true;
			}
			// ���������
			if (!bClearSn && (sItemKey.equals("vfree0") || sItemKey.equals("vbatchcode"))) bClearSn = true;

			// �������ʱ����Ҫ�����кţ�����Ҫͬʱ���λ
			// if(bClearSn&&m_voBill.getItemInv(row)!=null&&m_voBill.getItemInv(row).getInOutFlag()!=InOutFlag.IN)
			// bClearLoc=true;
			// //����ǻ�λ��������Ҫ������
			if (bIsLocatorMgt && bClearLoc) {
				if (m_alLocatorData != null

				&& row >= 0 && row < m_alLocatorData.size() && m_alLocatorData.get(row) != null) {
					m_alLocatorData.set(row, null);
					getBillCardPanel().setBodyValueAt(null, row, "vspacename");
					getBillCardPanel().setBodyValueAt(null, row, "cspaceid");
					sHintMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC000-0003830")/* @res "��λ" */;
				}
				if (m_voBill != null && m_voBill.getItemVOs().length > row && m_voBill.getItemVOs()[row] != null) {

					m_voBill.getItemVOs()[row].setLocator(null);

				}

			}

			// ----------------- ��������кŹ�������Ҫ������
			// bIsSNmgt
			// &&
			if (bClearSn) {
				if (m_alSerialData != null && row >= 0 && row < m_alSerialData.size()

				&& m_alSerialData.get(row) != null) {
					m_alSerialData.set(row, null);
					// ������˻�λ
					if (sHintMsg != null && sHintMsg.length() > 0) sHintMsg = sHintMsg + nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000311")/*
																																										* @res
																																										* "��"
																																										*/;
					sHintMsg = sHintMsg + nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC000-0001819")/* @res "���к�" */;
				}
				if (m_voBill != null && m_voBill.getItemVOs().length > row && m_voBill.getItemVOs()[row] != null) {

					m_voBill.getItemVOs()[row].setSerial(null);

				}

			}
		}
		// if (sHintMsg != null && sHintMsg.length() > 0)
		// showHintMessage(
		// "����˵� " + (row + 1) + " �е�" + sHintMsg + "���ݣ�������ִ��" + sHintMsg +
		// "���䡣");

	}

	/**
	 * �����ߣ����˾� ���ܣ�����б�ͱ����� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
	 * ��Դ������ת�ⵥʱ�Ľ�����Ʒ��� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2001-10-19 09:43:22)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	protected void ctrlSourceBillButtons() {
		ctrlSourceBillButtons(true);
	}

	/**
	 * ��Դ������ת�ⵥʱ�Ľ�����Ʒ��� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2001-10-19 09:43:22)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	protected void ctrlSourceBillUI() {
		ctrlSourceBillUI(true);
	}

	/**
	 * �˴����뷽��˵���� ���ߣ����Ӣ �������ڣ�(2001-6-21 15:11:22)
	 * 
	 * @param int bill
	 */
	protected void dispSpace(int bill) {
		// ��ѯ��ǰ���ı����λ
		// ydy
		if (m_alListData == null) return;

		if (getBillCardPanel().getBodyItem("vspacename") == null || !(getBillCardPanel().getBodyItem("vspacename").isShow())) {
			if (getBillListPanel().getBodyTable().getRowCount() > 0) getBillListPanel().getBodyTable().setRowSelectionInterval(0, 0);
			return;
		}

		GeneralBillVO voBill = (GeneralBillVO) m_alListData.get(bill);
		appendLocator(voBill);

		setListBodyData(voBill.getItemVOs());

		// ѡ�б����һ��
		// ���岻����Ϊ��
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
	 * �˴����뷽��˵���� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2001-12-5 16:27:59) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @param row
	 *            int
	 * @param formulas
	 *            java.lang.String[]
	 */
	protected void execEditFomulas(int row, String itemKey) {
		/** ǿ��ִ�б����У������еĹ�ʽ */
		nc.ui.pub.bill.BillItem bi = getBillCardPanel().getBodyItem(itemKey);
		if (bi != null) {
			String[] formulas = bi.getEditFormulas();
			getBillCardPanel().execBodyFormulas(row, formulas);
		}
	}

	/**
	 * ����˵����
	 * 
	 * ִ�б�ͷ����β�Ĺ�ʽ�ǡ�
	 * 
	 * �������ڣ�(2002-11-6 13:23:02) ���ߣ� �޸����ڣ� �޸��ˣ� �޸�ԭ�� �㷨˵����
	 */
	public void execHeadTailFormulas() {

		getBillCardPanel().execHeadTailLoadFormulas();

	}

	/**
	 * �����ߣ����˾� ���ܣ��˵�����Ҫ�������ֶΣ�Ȼ�󷵻�֮ ������ ���أ� ���⣺ ���ڣ�(2001-8-17 13:13:51)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	public void filterCondVO2(nc.vo.pub.query.ConditionVO[] voaCond, String[] saItemKey) {
		if (voaCond == null || saItemKey == null) return;
		// �ݴ���
		int j = 0;
		// ���鳤
		int len = saItemKey.length;
		for (int i = 0; i < voaCond.length; i++)
			// ��
			if (voaCond[i] != null) for (j = 0; j < len; j++) {
				if (saItemKey[j] != null && voaCond[i].getFieldCode() != null && saItemKey[j].trim().equals(voaCond[i].getFieldCode().trim())) {
					// ���϶������ı���
					voaCond[i].setFieldCode("1");
					voaCond[i].setOperaCode("=");
					voaCond[i].setDataType(1);
					voaCond[i].setValue("1");
				}
			}
	}

	/**
	 * ���˵��ݲ��� �����ߣ����� ���ܣ����˳ɱ����� ������ ���أ� ���⣺ ���ڣ�(2001-7-17 10:33:20)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 */
	public void filterCostObject() {
		try {
			// ���ݿ����֯����
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
	 * ����ǹ̶���λ�Ĵ�������˳�����Ĺ̶���λ ���ߣ����Ӣ �������ڣ�(2001-7-6 16:53:38)
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
	 * �����ߣ����˾� ���ܣ�ǩ�ֳɹ����� ������ ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05)
	 * 
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 */
	public void freshAfterSignedOK(String sBillStatus) {
		try {
			GeneralBillVO voBill = null;
			// ˢ���б���ʽ
			if (m_iLastSelListHeadRow >= 0 && m_alListData != null && m_alListData.size() > m_iLastSelListHeadRow && m_alListData.get(m_iLastSelListHeadRow) != null) {
				// ���ﲻ��clone(),�ı���m_voBillͬʱ�ı�m_alListData???
				voBill = (GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow);

				// �ѵ�ǰ��¼�����õ�vo
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
				// �����б����
				m_alListData.set(m_iLastSelListHeadRow, voBill);
			}
			// m_voBillˢ��
			// �ѵ�ǰ��¼�����õ�vo
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

			// �ѵ�ǰ��¼��������ʾ������
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
			// ˢ���б���ʽ
			setListHeadData();
			selectListBill(m_iLastSelListHeadRow);
			// ���ð�ť״̬,ǩ�ֲ����ã�ȡ��ǩ�ֿ���
			// ��ǩ�֣��������ð�ť״̬,ǩ�ֲ����ã�ȡ��ǩ�ֿ���
			setButtonStatus(false);

			getButtonTree().getButton(ICButtonConst.BTN_SIGN).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_EXECUTE_SIGN_CANCEL).setEnabled(true);
			// ����ɾ����
			getButtonTree().getButton(ICButtonConst.BTN_BILL_EDIT).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_BILL_DELETE).setEnabled(false);

			updateButtons();

		} catch (Exception e2) {
			nc.vo.scm.pub.SCMEnv.error(e2);
		}

	}

	/**
	 * �����ߣ����˾� ���ܣ�ˢ�¼ƻ��� ������ ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	public void freshPlanprice(ArrayList alNewPlanprice) {
		try {
			// ����
			int iRowCount = getBillCardPanel().getRowCount();
			if (alNewPlanprice == null || alNewPlanprice.size() < iRowCount || m_voBill == null) {
				SCMEnv.out("alallinv nvl");
				return;
			}
			// ����ȡ�������Ƿ�Ϊ��
			nc.ui.pub.bill.BillModel bmBill = getBillCardPanel().getBillModel();
			// ���������ڼ�����
			Object oTempNum = null;
			UFDouble dNum = null;
			UFDouble dMny = null;
			GeneralBillItemVO[] voaBillItem = m_voBill.getItemVOs();
			for (int row = 0; row < iRowCount; row++) {
				bmBill.setValueAt(alNewPlanprice.get(row), row, m_sPlanPriceItemKey);
				// ��Ҫͬ��m_voBill
				if (alNewPlanprice.get(row) != null) voaBillItem[row].setNplannedprice((UFDouble) alNewPlanprice.get(row));
				else voaBillItem[row].setNplannedprice(null);
				oTempNum = bmBill.getValueAt(row, m_sNumItemKey);
				// ͬʱ�������͵���ʱ���ż���
				if (oTempNum != null && alNewPlanprice.get(row) != null) {
					dNum = (UFDouble) oTempNum;
					dMny = dNum.multiply((UFDouble) alNewPlanprice.get(row));
				} else dMny = null;

				bmBill.setValueAt(dMny, row, m_sPlanMnyItemKey);
				// ��Ҫͬ��m_voBill
				voaBillItem[row].setNplannedmny(dMny);

			}

		} catch (Exception e2) {
			nc.vo.scm.pub.SCMEnv.error(e2);
		}
	}

	/**
	 * �˴����뷽��˵���� ���ܣ�ˢ��ts,�õ�����״̬ ������ ���أ� ���⣺ ���ڣ�(2002-6-4 19:54:51)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
	 * �����ߣ����˾� ���ܣ��ڱ�������ʾVO,�����½���״̬updateValue() ������ ���أ� ���⣺ ���ڣ�(2001-5-9
	 * 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	public void freshTs(ArrayList alTs) throws Exception {
		if (alTs == null || alTs.size() == 0) throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000027")/* @res "�����tsΪ�գ�" */);
		setTs(m_voBill, alTs);
		setTs(m_iLastSelListHeadRow, alTs);
		setUiTs(alTs);
	}

	/**
	 * �����ߣ����˾� ���ܣ��õ����ݳ�������� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	public int getBillInOutFlag() {
		return m_iBillInOutFlag;
	}

	/**
	 * �����ߣ����˾� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2001-11-23 18:05:45) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getBillTypeCode() {
		return m_sBillTypeCode;
	}

	/**
	 * �����ߣ����˾� ���ܣ��õ���ѯ�Ի��� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	protected QueryConditionDlgForBill getConditionDlg() {
		if (ivjQueryConditionDlg == null) {
			ivjQueryConditionDlg = new QueryConditionDlgForBill(this);
			ivjQueryConditionDlg.setTempletID(m_sCorpID, m_sCurrentBillNode, m_sUserID, null);

			// ����Ϊ�Թ�˾���յĳ�ʼ��
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
			// ����Ϊ�Բ��յĳ�ʼ��
			ivjQueryConditionDlg.initQueryDlgRef();

			// ���س�������
			ivjQueryConditionDlg.hideNormal();
			// �����Ƿ�رղ�ѯ����body.bbarcodeclose
			ivjQueryConditionDlg.setCombox("body.bbarcodeclose", new String[][] {
					{
							" ", " "
					}, {
							"N", nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000108")
					/*
					 * @res
					 * "��"
					 */}, {
							"Y", nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000244")
					/*
					 * @res
					 * "��"
					 */}
			});

			// ������������ʾ
			ivjQueryConditionDlg.setCombox("qbillstatus", new String[][] {
					{
							"2", nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000313")
					/*
					* @res "�Ƶ�"
					*/}, {
							"3", nc.ui.ml.NCLangRes.getInstance().getStrByID("40080402", "UPT40080402-000013")
					/*
					* @res "ǩ��"
					*/}, {
							"A", nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000217")
					/*
					 * @res
					 * "ȫ��"
					 */}
			});
			// set default logon date into query condiotn dlg
			ivjQueryConditionDlg.setInitDate("head.dbilldate", m_sLogDate);
			ivjQueryConditionDlg.setInOutFlag(m_iBillInOutFlag);

			// ��ѯ�Ի�����ʾ��ӡ����ҳǩ��
			ivjQueryConditionDlg.setShowPrintStatusPanel(true);

			// �޸��Զ�����Ŀ add by hanwei 2003-12-09
			DefSetTool.updateQueryConditionClientUserDef(ivjQueryConditionDlg, m_sCorpID, ICConst.BILLTYPE_IC, "head.vuserdef", "body.vuserdef");
			getConDlginitself(ivjQueryConditionDlg);

			// ���˿����֯���ֿ�,��Ʒ��,�ͻ�,��Ӧ�̵�����Ȩ�ޣ����ţ�ҵ��Ա
			// zhy2005-06-10 �ͻ��͹�Ӧ�̲���Ҫ����ͨ���Ϲ��ˣ����ͻ������۳��ⵥ�Ϲ��ˣ���Ӧ���ڲɹ���ⵥ�Ϲ��ˣ�
			// zhy2007-02-12 V51������:3��
			// ���̡��������ࡢ�����֯����Ŀ������Ȩ�޿��ƣ����š��ֿ⡢������ࡢ������Ѷ���Ŀ��Աƥ���¼�Ŀ��ƣ�
			/**
			 * ���Ա:head.cwhsmanagerid ����:head.cproviderid �����֯:head.pk_calbody
			 * �ֿ�:head.cwarehouseid,head.cwastewarehouseid ��Ŀ:body.cprojectid
			 * ����:head.cdptid �������:invcl.invclasscode ���:inv.invcode
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

			// zhy205-05-19 �ӿɻ�����������
			// �����
			ivjQueryConditionDlg.setCombox("coalesce(body.noutnum,0)-coalesce(body.nretnum,0)-coalesce(body.ntranoutnum,0)", new Integer[][] {
				{
						new Integer(0), new Integer(0)
				}
			});
			// ���뵥
			ivjQueryConditionDlg.setCombox("coalesce(body.ninnum,0)-coalesce(body.nretnum,0)-coalesce(body.ntranoutnum,0)", new Integer[][] {
				{
						new Integer(0), new Integer(0)
				}
			});

		}
		return ivjQueryConditionDlg;
	}

	/**
	 * �����ߣ����Ӣ ���ܣ��õ���ǰ��¼��Ĵ��ID ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */

	public ArrayList getCurInvID() {
		// ���ID
		ArrayList alAllInv = new ArrayList();
		// ����
		int iRowCount = getBillCardPanel().getRowCount();
		// ����ȡ�������Ƿ�Ϊ��
		nc.ui.pub.bill.BillModel bmBill = getBillCardPanel().getBillModel();
		for (int row = 0; row < iRowCount; row++)
			alAllInv.add(bmBill.getValueAt(row, m_sInvMngIDItemKey));
		return alAllInv;
	}

	/**
	 * �����ߣ����Ӣ ���ܣ��õ���ǰ��¼��Ĵ��ID ������ //���ID ���أ��Ƿ��д�� ���⣺ ���ڣ�(2001-5-9 9:23:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
		// ����
		int iRowCount = getBillCardPanel().getRowCount();
		// ����ȡ�������Ƿ�Ϊ��
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
	 * �����ߣ����˾� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2001-11-23 18:05:45) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getCurrentBillNode() {
		return m_sCurrentBillNode;
	}

	/**
	 * �����ߣ������� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2001-10-30 15:06:35) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
	 * �����ߣ����˾� ���ܣ������������������ ������ ���أ� ���⣺ ���ڣ�(2001-11-24 12:15:42)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
	 * �����ߣ����˾� ���ܣ��õ��û�����Ķ����ѯ���� ������//��ѯ�������� ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	public String getExtendQryCond(nc.vo.pub.query.ConditionVO[] voaCond) {
		// ����״̬����,ȱʡ��
		String sBillStatusSql = " (1=1) ";
		try {
			// -------- ��ѯ�����ֶ� itemkey ---------
			String sFieldCode = null;
			// �������в��������С����
			// ����״̬
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
			// ȱʡ��A
			if ("2".equals(sBillStatus)) // ����
			sBillStatusSql = " fbillflag=" + nc.vo.ic.pub.bill.BillStatus.FREE;
			else if ("3".equals(sBillStatus)) // ǩ�ֵ�
			sBillStatusSql = " ( fbillflag=" + nc.vo.ic.pub.bill.BillStatus.SIGNED + " OR fbillflag=" + nc.vo.ic.pub.bill.BillStatus.AUDITED + ") ";

			// �˿��ѯ add by hanwei 2003-10-10
			if (nc.vo.ic.pub.BillTypeConst.BILLNORMAL.equalsIgnoreCase(sFreplenishflag)) {
				sBillStatusSql += " AND ( freplenishflag is null or freplenishflag='N' )";
			} else if (nc.vo.ic.pub.BillTypeConst.BILLSENDBACK.equalsIgnoreCase(sFreplenishflag)) {
				sBillStatusSql += " AND ( freplenishflag='Y' )";
			} else if (nc.vo.ic.pub.BillTypeConst.BILLALL.equalsIgnoreCase(sFreplenishflag)) {
				sBillStatusSql += "  ";
			}

			// ȥ��freplenishflag �Ƿ��˿�
			String saItemKey[] = new String[] {
					"qbillstatus", "freplenishflag"
			};
			filterCondVO2(voaCond, saItemKey);
			// ��������
			String sOtherCond = getConditionDlg().getWhereSQL(voaCond);
			if (sOtherCond != null) sBillStatusSql += " AND ( " + sOtherCond + " )";
		} catch (Exception e) {
			handleException(e);
		}

		return sBillStatusSql;
	}

	/**
	 * �������ڣ�(2003-3-4 17:13:59) ���ߣ����� �޸����ڣ� �޸��ˣ� �޸�ԭ�� ����˵����
	 * 
	 * @return nc.ui.ic.pub.bill.InvoInfoBYFormula
	 */
	public InvoInfoBYFormula getInvoInfoBYFormula() {
		if (m_InvoInfoBYFormula == null) m_InvoInfoBYFormula = new InvoInfoBYFormula(getCorpPrimaryKey());
		return m_InvoInfoBYFormula;
	}

	/**
	 * ȥ�����������������Ƿ�������Ĳ��� ���ܣ� ���������VO ���أ�boolean ���⣺ ���ڣ�(2002-05-20 19:55:18)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	protected boolean getIsInvTrackedBill(InvVO invvo) {
		if (invvo != null && invvo.getOuttrackin() != null) {
			return invvo.getOuttrackin().booleanValue();

		} else {
			return false;
		}
	}

	/**
	 * ���� FreeItemRefPane1 ����ֵ��
	 * 
	 * @return nc.ui.ic.pub.freeitem.FreeItemRefPane
	 */
	/* ���棺�˷������������ɡ� */
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
	 * �˴����뷽��˵���� �������ڣ�(2001-9-19 16:02:07)
	 * 
	 * @return nc.ui.ic.ic101.OrientDialog
	 * @author:���Ӣ
	 */
	public nc.ui.ic.pub.orient.OrientDialog getOrientDlg() {
		if (m_dlgOrient == null) {
			m_dlgOrient = new nc.ui.ic.pub.orient.OrientDialog(this);

			// ivjQueryConditionDlg.setPKCorp(m_sCorpID);

		}
		return m_dlgOrient;
	}

	/**
	 * �����ߣ����˾� ���ܣ��õ���ǰ������ ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
	 * �����ߣ����˾� ���ܣ��õ������� ������iMode:����������ã� ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	protected PrgBar getPrgBar(int iMode) {
		if (m_pbProgressBar == null) {
			m_pbProgressBar = new PrgBar(this);
		}
		// �ܵ�λʱ��
		int iTimeTotal = 1000;
		// ����
		String sTitle = "";
		// �Ƿ��ظ���ʾ����
		boolean bRepeat = false;
		// ʱ����
		int iTimeInterval = 10;

		switch (iMode) {
		// ����
		case PrgBar.PB_SAVE:
			iTimeTotal = 10000;
			sTitle = nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000001")/* @res "����" */;
			break;
		// ��ѯ
		case PrgBar.PB_QRY:
			iTimeTotal = 50000;
			sTitle = nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000006")/* @res "��ѯ" */;
			iTimeInterval = 50;
			bRepeat = true;
			break;
		// ɾ��
		case PrgBar.PB_DEL:
			iTimeTotal = 1000;
			sTitle = nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000039")/* @res "ɾ��" */;
			break;
		// ǩ��
		case PrgBar.PB_SIGN:
			iTimeTotal = 1000;
			sTitle = nc.ui.ml.NCLangRes.getInstance().getStrByID("40080402", "UPT40080402-000013")/* @res "ǩ��" */;
			break;
		// ȡ��ǩ��
		case PrgBar.PB_CANCELSIGN:
			iTimeTotal = 1000;
			sTitle = nc.ui.ml.NCLangRes.getInstance().getStrByID("40080402", "UPT40080402-000014")/* @res "ȡ��ǩ��" */;
			break;
		}
		// �����ܵ�λʱ��
		m_pbProgressBar.setTotal(iTimeTotal);
		// ���ñ���
		m_pbProgressBar.setTitle(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000314")/* @res "����" */
				+ sTitle + nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000315")/* @res "�����Ժ�..." */);
		// ����ʱ����
		m_pbProgressBar.setSleepTime(iTimeInterval);
		// �����Ƿ��ظ���ʾ
		m_pbProgressBar.setRepeat(bRepeat);
		// -----------------
		return m_pbProgressBar;
	}

	/**
	 * �����ߣ������� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2001-10-30 15:06:35) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
	 * �����ߣ�zhx ���ܣ������û�ѡ���ҵ������ ������ ���أ� ���⣺ ���ڣ�(2002-12-10 9:23:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
	 * ����˵���� �������ڣ�(2002-11-18 15:49:54) ���ߣ����Ӣ �޸����ڣ� �޸��ˣ� �޸�ԭ�� �㷨˵����
	 * 
	 * @return java.util.ArrayList
	 */
	public ArrayList getSelectedBills() {

		ArrayList albill = new ArrayList();
		int iSelListHeadRowCount = getBillListPanel().getHeadTable().getSelectedRowCount();
		if (iSelListHeadRowCount <= 0) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH003")/* @res "��ѡ��Ҫ��������ݣ�" */);
			return null;
		}
		int[] arySelListHeadRows = new int[iSelListHeadRowCount];
		arySelListHeadRows = getBillListPanel().getHeadTable().getSelectedRows();

		GeneralBillVO voaBill[] = new GeneralBillVO[iSelListHeadRowCount];
		Vector vHeadPK = new Vector();
		Vector vIndex = new Vector();
		// ��ֻ��һ�в��Ҿ�������ʱ��������ȡ��by zss 2004-02-09
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

		// ��ѯ��������
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
	 * �˴����뷽��˵���� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2002-10-23 10:31:53) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
	 * �õ�m_voBill�еĵ������ͱ��� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2001-10-12 13:18:06)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	protected String getSourBillTypeCode() {
		GeneralBillVO voBill = null;
		// �б���ʽ�ͱ���ʽ��ͬ
		if (m_iCurPanel == BillMode.List && m_iLastSelListHeadRow >= 0 && m_alListData != null && m_alListData.size() > m_iLastSelListHeadRow && m_alListData.get(m_iLastSelListHeadRow) != null)
		// �ȶ�����
		voBill = (GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow);
		else
		// ֱ����m_vo
		voBill = m_voBill;

		if (voBill != null && voBill.getItemCount() > 0) return (String) voBill.getItemValue(0, "csourcetype");
		else return null;
	}

	/**
	 * �����ߣ����˾� ���ܣ���ָ����ŵ��ݵĻ�λ/���к�����,���ڴ�ӡ��λ���к���ϸ ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
		 * //���Դ˵����Ƿ���������������������Ҫ����ִ�У����򵥾ݱ�����û����Щ���ݣ����ö��� int i = 0, iRowCount =
		 * voMyBill.getItemCount(); //�����Ƿ��Ѿ�������Щ�����ˡ� for (i = 0; i < iRowCount;
		 * i++) if (voMyBill.getItemValue(i, "locator") != null ||
		 * voMyBill.getItemValue(i, "serial") != null) break;
		 * 
		 * for (i = 0; i < iRowCount; i++) //����ʵ��/������ if
		 * (voMyBill.getItemValue(i, "ninnum") != null &&
		 * voMyBill.getItemValue(i, "ninnum").toString().length() > 0 ||
		 * voMyBill.getItemValue(i, "noutnum") != null &&
		 * voMyBill.getItemValue(i, "noutnum").toString().length() > 0) break;
		 * 
		 * if (i >= iRowCount) //������ return voMyBill;
		 * //=============================================================
		 * 
		 * WhVO voWh = voMyBill.getWh(); //��λ����Ĳֿ���Ҫ����λ���� if (voWh != null &&
		 * voWh.getIsLocatorMgt() != null && voWh.getIsLocatorMgt().intValue()
		 * == 1) iMode = QryInfoConst.LOC_SN; else iMode = QryInfoConst.SN;
		 * 
		 * Integer iSearchMode = new Integer(iMode);
		 * //////////////////////////////iMode); //���λ & ���к� 3 or ���к� 4
		 * ArrayList alAllData = (ArrayList) invokeClient("queryInfo", new
		 * Class[] { Integer.class, Object.class }, new Object[] { iSearchMode,
		 * voMyBill.getPrimaryKey()});
		 * 
		 * if (iMode == QryInfoConst.LOC_SN) { if (alAllData != null &&
		 * alAllData.size() >= 2) { alLocatorData = (ArrayList)
		 * alAllData.get(0); alSerialData = (ArrayList) alAllData.get(1); }
		 * //else } else { //=== SN only if (alAllData != null &&
		 * alAllData.size() >= 1) alSerialData = (ArrayList) alAllData.get(0);
		 * //else } //������λ���� if (alLocatorData == null || alLocatorData.size()
		 * != iRowCount) { alLocatorData = new ArrayList(); for (int j = 0; j <
		 * iRowCount; j++) alLocatorData.add(null); } //�������к����� if (alSerialData
		 * == null || alSerialData.size() != iRowCount) { alSerialData = new
		 * ArrayList(); for (int j = 0; j < iRowCount; j++)
		 * alSerialData.add(null); } //��λ���кű��浽alListData for (int j = 0; j <
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
	 * �����б���ʽ�µı�ͷ�С� �������ڣ�(2001-5-29 9:13:20)
	 * 
	 * @param strKey
	 *            java.lang.String
	 */
	public void hideListTableHeadCol(nc.ui.pub.bill.BillScrollPane bspTable, int iCol) {
		if (iCol < 0 || bspTable == null || bspTable.getTableModel() == null || iCol >= bspTable.getTableModel().getColumnCount()) {
			SCMEnv.out("hide col <0");
			return;
		}
		// �ҵ���Ӧ��Item
		nc.ui.pub.bill.BillItem item = bspTable.getTableModel().getBodyItems()[iCol];
		javax.swing.table.TableColumn tclCol = null;
		try {
			// �õ���
			tclCol = bspTable.getTable().getColumn(item.getName());
			// ��ģ��
			javax.swing.table.TableColumnModel cm = bspTable.getTable().getColumnModel();
			// ��modelɾ��
			cm.removeColumn(tclCol);
			// ��Ϊ������
			item.setShow(false);
		} catch (Throwable e) {
			// SCMEnv.out("���ѱ����أ�");
		}
	}

	/**
	 * 
	 * �����������������ButtonTree����<br>
	 * ButtonTree���Դӡ�����ע�ᡱ�а��հ�ť��CLASS_NAME���ĳ����ť��ʵ��
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * setButtons(getButtonTree().getButtonArray()); m_boAdd =
	 * bt.getButton(�����ӡ�)��
	 * <p>
	 * <b>����˵��</b>
	 * 
	 * @return ButtonTree����
	 *         <p>
	 * @author duy
	 * @time 2007-2-1 ����06:00:49
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
	 * ����������������ť�ĳ�ʼ����
	 * <p>
	 * <b>����˵��</b>
	 * <p>
	 * 
	 * @deprecated
	 * @author duy
	 * @time 2007-2-5 ����02:56:08
	 */
	protected void initButtons() {
	}

	/**
	 * 
	 /** �����ߣ����˾� ���ܣ���ʼ����ť�� ������ ���أ� ���⣺ ���ڣ�(2001-11-23 18:11:18)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� ˵�� by hanwei 2003-10-10
	 * m_vTopMenu.insertElementAt(m_vboSendback, 0); ����������ָ���Ĳ˵�λ��
	 * ���˵�����initButtonsData()����������Ի� �������������
	 * nc.ui.ic.ic201.ClientUI.setButtonsStatus(int) super.initButtonsData();
	 * Ҫȥ��super. ����setButtonsStatus�ظ�����initButtonsData()��
	 * ���Ը��˵���������ڱ�������������m_boSendback = new ButtonObject("�˻�", "�˻�", 0);
	 * ����ᵼ���Ӳ˵��ظ����ӡ�
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * <b>����˵��</b>
	 * <p>
	 * 
	 * @author duy
	 * @deprecated
	 * @time 2007-2-2 ����11:57:14
	 */
	protected void initButtonsData() {
	}

	/**
	 * �˴����뷽��˵���� ���ܣ���ʼ�� ����¼�� ��ť�� ������ ���أ� ���⣺ ���ڣ�(2002-9-28 10:19:23)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @deprecated
	 */
	protected void initDevInputButtons() {
	}

	/**
	 * �����ߣ����˾� ���ܣ���ʼ��ϵͳ���� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	abstract protected void initPanel();

	/**
	 * �����ߣ����˾� ���ܣ���ϵͳ���� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	protected void initSysParam() {
		// m_sTrackedBillFlag = "N";
		// m_sSaveAndSign = "N";
		try {
			// ��������IC028:����ʱ�Ƿ�ָ����ⵥ;���β��ո����Ƿ񵽵��ݺ�.
			// IC010 �Ƿ�ʹ��ɾ��������
			// IC060 �Ƿ���������Ƶ��ˡ�
			// IC030 ���ݺ��Ƿ������ֹ�����

			// �������� ���� ȱʡֵ
			// BD501 ����С��λ 2
			// BD502 ����������С��λ 2
			// BD503 ������ 2
			// BD504 ����ɱ�����С��λ 2
			// BD505 �ɹ�/���۵���С��λ 2

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

			// ����Ĳ���
			ArrayList alAllParam = new ArrayList();
			// ������ı�������
			ArrayList alParam = new ArrayList();
			alParam.add(m_sCorpID); // ��һ���ǹ�˾
			alParam.add(saParam); // ����Ĳ���
			alAllParam.add(alParam);
			// ���û���Ӧ��˾�ı������
			alAllParam.add(m_sUserID);

			ArrayList alRetData = null;
			alRetData = (ArrayList) ICReportHelper.queryInfo(new Integer(QryInfoConst.INIT_PARAM), alAllParam);

			// Ŀǰ��������
			if (alRetData == null || alRetData.size() < 2) {
				showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000045")/* @res "��ʼ����������" */);
				return;
			}
			// ���صĲ���ֵ
			String[] saParamValue = (String[]) alRetData.get(0);
			// ׷�ٵ����ݲ���,Ĭ������Ϊ"N"
			if (saParamValue != null && saParamValue.length >= alAllParam.size()) {
				// if (saParamValue[0] != null)
				// m_sTrackedBillFlag =
				// saParamValue[0].toUpperCase().trim();
				// �Ƿ񱣴漴ǩ�֡�Ĭ������Ϊ"N"
				// if (saParamValue[0] != null)
				// m_sSaveAndSign = saParamValue[0].toUpperCase().trim();
				// BD501 ����С��λ 2
				if (saParamValue[0] != null) m_ScaleValue.setNumScale(Integer.parseInt(saParamValue[0]));
				// BD502 ����������С��λ 2
				if (saParamValue[1] != null) m_ScaleValue.setAssistNumScale(Integer.parseInt(saParamValue[1]));
				// BD503 ������ 2
				if (saParamValue[2] != null) m_ScaleValue.setHslScale(Integer.parseInt(saParamValue[2]));
				// BD504 ����ɱ�����С��λ 2
				if (saParamValue[3] != null) m_ScaleValue.setPriceScale(Integer.parseInt(saParamValue[3]));
				// BD301 ����С��λ
				if (saParamValue[4] != null) m_ScaleValue.setMnyScale(Integer.parseInt(saParamValue[4]));
				// IC060 �Ƿ�������Ƶ��� 'N' 'Y'
				// if (saParamValue[7] != null)
				// m_sRemainOperator = saParamValue[7].toUpperCase().trim();
				// IC030 �Ƿ�����༭���ݺ� 'N' 'Y'
				if (saParamValue[5] != null && "Y".equalsIgnoreCase(saParamValue[5].trim())) m_bIsEditBillCode = true;

				// IC062 �Ƿ񱣴�����
				if (saParamValue[6] != null && "Y".equalsIgnoreCase(saParamValue[6].trim())) m_bBarcodeSave = true;
				else m_bBarcodeSave = false;

				// m_bBarcodeSave = true;
				// IC063 ���벻�����Ƿ񱣴�����
				if (saParamValue[7] != null && "Y".equalsIgnoreCase(saParamValue[7].trim())) m_bBadBarcodeSave = true;
				else m_bBadBarcodeSave = false;

				// IC0641 ���������ʾ������
				if (saParamValue[8] != null && saParamValue[8].trim().length() > 0) m_iBarcodeUIColorRow = Integer.parseInt(saParamValue[8].trim());

				// IC0642 ���������ʾ����ɫ
				if (saParamValue[9] != null && saParamValue[9].trim().length() > 0) m_sColorRow = saParamValue[9].trim();

				// IC050 ��������Ƿ��ղֿ����
				if (saParamValue[10] != null && "�ֿ�".equalsIgnoreCase(saParamValue[10].trim())) m_isWhInvRef = true;
				else m_isWhInvRef = false;

				// BD505 �ɹ�/���۵���С��λ 2
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
			// ���صĲ�����Ӧ�Ĺ�˾
			m_alUserCorpID = (ArrayList) alRetData.get(1);

			// ϵͳ��������
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
	 * �����ߣ����˾� ���ܣ������ǰѡ�еĵ��ݲ��Ǻͱ��ڵ���ͬ�ĵ�������(����뵥�ϲ�����ڳ���)������ɾ��.
	 * 
	 * ������ ���أ� ���⣺ ���ڣ�(2001-11-23 18:11:18) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	protected boolean isCurrentTypeBill() {
		// ������Ǻͱ��ڵ���ͬ�ĵ�������(����뵥�ϲ�����ڳ���)������ɾ��.
		try {
			// ��ǰѡ�еĵ���
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
	 * �˴����뷽��˵���� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2002-06-03 11:32:25) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	protected boolean isDispensedBill(GeneralBillVO gvo) {
		if (gvo == null) {

			// �б���ʽ�ͱ���ʽ��ͬ
			if (m_iCurPanel == BillMode.List && m_iLastSelListHeadRow >= 0 && m_alListData != null && m_alListData.size() > m_iLastSelListHeadRow && m_alListData.get(m_iLastSelListHeadRow) != null)
			// �ȶ�����
			gvo = (GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow);
			else
			// ֱ����m_vo
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
	 * �˴����뷽��˵���� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2002-06-03 11:32:25) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	protected boolean isDispensedBill(GeneralBillVO gvo, int rownum) {
		if (gvo == null) {

			// �б���ʽ�ͱ���ʽ��ͬ
			if (m_iCurPanel == BillMode.List && m_iLastSelListHeadRow >= 0 && m_alListData != null && m_alListData.size() > m_iLastSelListHeadRow && m_alListData.get(m_iLastSelListHeadRow) != null)
			// �ȶ�����
			gvo = (GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow);
			else
			// ֱ����m_vo
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
	 * �����ߣ����˾� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2001-11-24 12:14:35) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @return boolean
	 */
	public boolean isNeedBillRef() {
		return m_bNeedBillRef;
	}

	/**
	 * �˴����뷽��˵���� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2002-06-11 9:22:38) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	protected boolean isSetInv(GeneralBillVO gvo, int rownum) {
		if (gvo != null && rownum != -1 && (rownum <= gvo.getItemCount() - 1 && rownum >= 0)) if (gvo.getItemInv(rownum).getIsSet() != null && gvo.getItemInv(rownum).getIsSet().intValue() == 1) return true;
		else return false;
		else return false;

	}

	/**
	 * �����ߣ����� ���ܣ���ǰѡ�����Ƿ������кŷ��䣬Ҫ�����б�/���µ�ѡ�� ������//��ǰѡ�е��� ���أ� ���⣺ ���ڣ�(2001-5-9
	 * 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	public boolean isSNmgt(int iCurSelBodyLine) {

		if (iCurSelBodyLine >= 0) {
			InvVO voInv = null;
			// ������vo,�����б��»��Ǳ���
			// ����ʽ��
			if (BillMode.Card == m_iCurPanel) {
				if (m_voBill == null) {
					SCMEnv.out("bill null E.");
					return false;
				}
				voInv = m_voBill.getItemInv(iCurSelBodyLine);
			} else // �б���ʽ��
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

	protected boolean m_bRefBills = false;// �Ƿ�������ɶ��ŵ���

	private String m_sBizTypeRef = null;

	public void setBillRefMultiVOs(String sBizType, GeneralBillVO[] vos) throws Exception {
		if (vos == null || vos.length <= 0) return;

		if (vos != null && vos.length == 1) {
			setRefBillsFlag(false);
			setBillRefResultVO(sBizType, vos);// �ϵ�
			return;
		}
		m_sBizTypeRef = sBizType;

		// �����к�
		nc.ui.scm.pub.report.BillRowNo.setVOsRowNoByRule(vos, m_sBillTypeCode, m_sBillRowNo);
		// �������ݵ��б�
		setDataOnList(vos);

	}

	protected void setRefBillsFlag(boolean bRefBills) {
		m_bRefBills = bRefBills;
	}

	/**
	 * �����ߣ����� ���ܣ�ѡ����ҵ������ ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 */
	protected void onBizType(ButtonObject bo) {
		try {
			// ��ť�¼�����
			nc.ui.scm.pub.redun.RedunSourceDlg.childButtonClicked(bo, m_sCorpID, m_sCurrentBillNode, m_sUserID, m_sBillTypeCode, this);

			// �õ��û�ѡ���ҵ�����͵ķ���. 20021209
			String sBusiTypeID = getSelBusiType();

			if (!nc.ui.scm.pub.redun.RedunSourceDlg.makeFlag()) {
				// �����Ʒ�ʽ
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
					SCMEnv.showTime(lTime, "��ò���VO:");
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
					// ��鵥���Ƿ���Դ���µĲ��ս���
					if (!ICConst.IsFromNewRef.equals(vos[0].getParentVO().getAttributeValue(ICConst.IsFromNewRef))) {
						// �����Ĭ�Ϸ�ʽ�ֵ�
						vos = GenMethod.splitGeneralBillVOs((GeneralBillVO[]) vos, getBillTypeCode(), getBillListPanel().getHeadBillModel().getFormulaParse());
						// ���������ݵĵ�λת��Ϊ���Ĭ�ϵ�λ.
						GenMethod.convertICAssistNumAtUI((GeneralBillVO[]) vos, getBillListPanel().getBodyBillModel().getFormulaParse());
					}

					// v5 lj ֧�ֶ��ŵ��ݲ�������
					if (vos != null && vos.length == 1) {
						setRefBillsFlag(false);
						setBillRefResultVO(sBusiTypeID, vos);
					} else {
						setRefBillsFlag(true);// �ǲ������ɶ���
						setBillRefMultiVOs(sBusiTypeID, (GeneralBillVO[]) vos);
					}
					// end v5

				}
			} else {
				// ���Ƶ���
				setRefBillsFlag(false);
				onAdd(true, null);
				/* ���õ��ݱ����еĴ�����չ��˺Ͳ��յ�TextField�Ŀɱ༭��� * */
				nc.ui.pub.bill.BillItem bi = bi = getBillCardPanel().getBodyItem("cinventorycode");
				// ((nc.ui.pub.beans.UIRefPane) bi.getComponent())
				// .getUITextField().setEditable(true);
				RefFilter.filtInv(bi, m_sCorpID, null);
				// set user selected ҵ������ 20021209
				if (getBillCardPanel().getHeadItem("cbiztype") != null) {
					getBillCardPanel().setHeadItem("cbiztype", sBusiTypeID);
					((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("cbiztype").getComponent()).setPK(sBusiTypeID);
					nc.ui.pub.bill.BillEditEvent event = new nc.ui.pub.bill.BillEditEvent(getBillCardPanel().getHeadItem("cbiztype"), sBusiTypeID, "cbiztype");
					afterEdit(event);

				}
				// Ĭ������£��˿�״̬�������� add by hanwei 2003-10-19
				nc.ui.ic.pub.bill.GeneralBillUICtl.setSendBackBillState(this, false);

			}

		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
			showErrorMessage(e.getMessage());
		}
	}

	/**
	 * 
	 * ���ܣ� �˶������¼���Ӧ ������ ���أ� ���⣺ ���ڣ�(2002-04-18 10:43:46) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	public void onCheckData() {
		try {
			getDevInputCtrl().setBillVO(m_voBill);
			java.util.ArrayList alResult = getDevInputCtrl().onOpenFile(getDevInputCtrl().ACT_CHECK_ITEM);

			alResult.get(1);
		} catch (Exception e) {
			String sErrorMsg = null;
			sErrorMsg = e.getMessage();
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000044")/* @res "������ʾ��" */
					+ sErrorMsg);
		}
	}
	
	
	/**
	 * 
	 * ���ܣ� ���������¼���Ӧ ������ ���أ� ���⣺ ���ڣ�(2002-04-18 10:43:46) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	
	
	public void onImportData() {
		try{
			// ���˿���
			filterNullLine();
			// �����к�
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
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000046")/* @res "û�е���ɹ���" */);
				return;
			}

			String sAppendType = (String) alResult.get(0);

			nc.vo.pub.CircularlyAccessibleValueObject[] voaDi = (nc.vo.pub.CircularlyAccessibleValueObject[]) alResult.get(1);

			int iAppendType = Integer.parseInt(sAppendType);
			m_bIsImportData = false;
			if (iAppendType == DevInputCtrl.ACT_ADD_ITEM) {
				if (voaDi != null && voaDi.length > 0) {
					m_bIsImportData = true;
					// �����Ƿ��ڱ���ʱУ�鵼�����ݵ���ȷ��
					String sWarehouseid = null;
					nc.ui.pub.beans.UIRefPane refpane = (UIRefPane) getBillCardPanel().getHeadItem("cwarehouseid").getComponent();

					if (refpane != null) sWarehouseid = refpane.getRefPK();
					// ͬ��vo.
					synVO(voaDi, sWarehouseid, m_alLocatorData);
					// ���뽫��λ��Ϣͬ����m_alLocatorData,���򵥾ݱ���ʱ��getBillVOs���������m_voBill�Ļ�λ��Ϣ
					GeneralBillItemVO[] voaItemBill = (GeneralBillItemVO[]) m_voBill.getChildrenVO();
					int len = voaItemBill.length;
					for (int i = 0; i < len; i++) {
						LocatorVO[] voLoc = voaItemBill[i].getLocator();
						m_alLocatorData.add(i, voLoc);
					}

				} else m_bIsImportData = false;
			} else {
				// ����У������Ϣ����������Щ��Ϣ��ֻ��������
				m_bIsImportData = false;
			}
			

		} catch (Exception e) {
			String sErrorMsg = null;
			sErrorMsg = e.getMessage();
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000044")/* @res "������ʾ��" */
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

		// �����λ�����кţ���Щ�����ǲ����Ƶ�,�� m_alLoctorData,m_alSerialData����һ��
		// ydy 2004-07-02 ��λ����
		// m_voaBillItem[i].setLocator((LocatorVO[])m_alLocatorData.get(row[i]));
		voaBillItem.setSerial(null);
		voaBillItem.setAttributeValue("cparentid", null);
		voaBillItem.setAttributeValue("ncorrespondnum", null);
		voaBillItem.setAttributeValue("ncorrespondastnum", null);

		// ����������� add by hanwei 2004-04-07
		voaBillItem.setBarCodeVOs(null);
		voaBillItem.setAttributeValue(IItemKey.NBARCODENUM, new UFDouble(0.0));

		voaBillItem.setBarcodeClose(new nc.vo.pub.lang.UFBoolean('N'));

		voaBillItem.setAttributeValue(IItemKey.NKDNUM, null);
		return voaBillItem;
	}
	
	/**
	 * �����ߣ����� ���ܣ�����¼�� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	protected void onJointAdd(ButtonObject bo) {
		// ��ǰ���б���ʽʱ�������л�������ʽ
		if (BillMode.List == m_iCurPanel) onSwitch();
		nc.ui.pub.pf.PfUtilClient.retAddBtn(getButtonTree().getButton(ICButtonConst.BTN_ADD), m_sCorpID, m_sBillTypeCode, bo);
		showSelBizType(bo);
		// updateButtons();

		setButtons();

	}

	/*
	 * ��Ƭ��ӡ/Ԥ��
	 * 
	 * @param isPreview boolean true-��ӡԤ����false-ֱ�Ӵ�ӡ * @author �۱� on Jun 15,
	 * 2005
	 */
	protected void printOnCard(GeneralBillVO voBill, boolean isPreview) {

		// ��������
		GeneralBillHeaderVO headerVO = voBill.getHeaderVO();
		String sBillID = headerVO.getPrimaryKey();

		// ����PrintLogClient������PrintInfo.
		ScmPrintlogVO voSpl = new ScmPrintlogVO();
		voSpl.setCbillid(sBillID); // ���������ID
		voSpl.setVbillcode(headerVO.getVbillcode());// ���뵥�ݺţ�������ʾ��
		voSpl.setCbilltypecode(headerVO.getCbilltypecode());
		voSpl.setCoperatorid(headerVO.getCoperatorid());
		voSpl.setIoperatetype(new Integer(PrintConst.PAT_OK));// �̶�
		voSpl.setPk_corp(getCorpPrimaryKey());
		voSpl.setTs(headerVO.getTs());// ���������TS

		SCMEnv.out("ts=========tata" + voSpl.getTs());
		nc.ui.scm.print.PrintLogClient plc = new nc.ui.scm.print.PrintLogClient();
		// ���õ�����Ϣ
		plc.setPrintInfo(voSpl);
		// ����TSˢ�¼���.
		plc.addFreshTsListener(new FreshTsListener());
		// ���ô�ӡ����
		getPrintEntry().setPrintListener(plc);

		plc.setPrintEntry(getPrintEntry());// ���ڵ���ʱ
		// ���õ�����Ϣ
		plc.setPrintInfo(voSpl);

		// ���ӡ��������Դ�����д�ӡ
		getDataSource().setVO(voBill);
		getPrintEntry().setDataSource(getDataSource());

		// ��ӡ��ʾ��Ϣ
		String sPrintMsg = null;
		// ִ�д�ӡ
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
	 * �б��´�ӡ @author �۱� on Jun 15, 2005
	 */
	protected void printOnList(ArrayList alBill) throws InterruptedException {
		nc.ui.pub.print.PrintEntry pe = getPrintEntryNew();

		if (pe.selectTemplate() < 0) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000048")/*
																											* @res "���ȶ����ӡģ�塣"
																											*/);
			return;
		}

		pe.beginBatchPrint();

		PrintDataInterface ds = null;
		GeneralBillVO voBill = null;
		// ��������
		GeneralBillHeaderVO headerVO = null;
		// nc.vo.scm.print.PrintResultVO printResultVO = null;

		nc.ui.scm.print.PrintLogClient plc = new nc.ui.scm.print.PrintLogClient();
		plc.setBatchPrint(true);// ����������
		// ���ô�ӡ����
		pe.setPrintListener(plc);
		plc.setPrintEntry(pe);
		plc.addFreshTsListener(new FreshTsListener());

		// ��ӡ����
		for (int i = 0; i < alBill.size(); i++) {
			voBill = (GeneralBillVO) alBill.get(i);
			headerVO = voBill.getHeaderVO();

			ScmPrintlogVO voSpl = new ScmPrintlogVO();
			voSpl = new ScmPrintlogVO();
			voSpl.setCbillid(headerVO.getPrimaryKey()); // ���������ID
			voSpl.setVbillcode(headerVO.getVbillcode());// ���뵥�ݺţ�������ʾ��
			voSpl.setCbilltypecode(headerVO.getCbilltypecode());
			voSpl.setCoperatorid(headerVO.getCoperatorid());
			voSpl.setIoperatetype(new Integer(PrintConst.PAT_OK));// �̶�
			voSpl.setPk_corp(getCorpPrimaryKey());
			voSpl.setTs(headerVO.getTs());// ���������TS

			SCMEnv.out("ts=========tata" + voSpl.getTs());
			// ���õ�����Ϣ
			plc.setPrintInfo(voSpl);

			if (plc.check()) {// ���ͨ����ִ�д�ӡ���д���Ļ��Զ������ӡ��־�����ﲻ�ô���
				ds = getDataSourceNew();
				ds.setVO(voBill);
				pe.setDataSource(ds);

				// ����������Setup����С���У����ֳ������׶�������
				// while (pe.dsCountInPool() > PrintConst.PL_MAX_TAST_NUM) {
				// Thread.currentThread().sleep(PrintConst.PL_SLEEP_TIME); //
				// �����PL_MAX_TAST_NUM���������񣬾͵ȴ�PL_SLEEP_TIME�롣
				// }
			}

		}
		pe.endBatchPrint();

		MessageDialog.showHintDlg(this, null, plc.getPrintResultMsg(false));

	}

	/**
	 * �����ߣ������� ���ܣ���ӡ ������ ���أ� ���⣺ ���ڣ�(2001-5-10 ���� 4:16) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * �޸�˵�������Ӵ�ӡ�������� �޸��ߣ��۱� 2005-01-12
	 */
	public void onPrint() {

		try {
			// ������ӡ����
			// ����ǰ���б��Ǳ�������ӡ����
			if (m_iMode == BillMode.Browse && m_iCurPanel == BillMode.Card) { // ���

				/* ���Ӵ�ӡ�������ƺ�Ĵ�ӡ���� */
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000047")/*
																												* @res "���ڴ�ӡ�����Ժ�..."
																												*/);
				// ׼�����ݣ����Ҫ��ӡ��vo.
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
																													* "���ȶ����ӡģ�塣"
																													*/);
					return;
				}

				// ��Ƭ��ӡ
				printOnCard(voBill, false);

				// showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
				// "SCMCOMMON", "UPPSCMCommon-000133")/* @res "����" */);

			} else if (m_iCurPanel == BillMode.List) { // �б�
				/* ���Ӵ�ӡ��������ǰ�Ĵ�ӡ���� */
				if (null == m_alListData || m_alListData.size() == 0) {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000049")/*
																														*/);
					return;
				}

				// �õ�Ҫ��ӡ���б�vo,ArrayList.
				ArrayList alBill = getSelectedBills();
				// ��С������
				setScaleOfListData(alBill);
				if (alBill == null || alBill.size() <= 0 || alBill.get(0) == null) {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH003")/*
																									 * @res "��ѡ��Ҫ��������ݣ�"
																									 */);
					return;
				}

				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000047")/*
																												* @res "���ڴ�ӡ�����Ժ�..."
																												*/);

				// �б��´�ӡ
				printOnList(alBill);

				// showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
				// "SCMCOMMON", "UPPSCMCommon-000133")/* @res "����" */);

			} else showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000052")/*
																												* @res
																												* "��ע�⣺��ֻ�������״̬�´�ӡ"
																												*/);

		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000061")/* @res "��ӡ����" */
					+ e.getMessage());
		}
	}

	/**
	 * �˴����뷽��˵���� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2002-10-23 9:07:19) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	private void onPrintLocSN(GeneralBillVO voBill) {
		// ��ӡ��������m_voBill
		if (voBill == null || voBill.getParentVO() == null || voBill.getChildrenVO() == null) {
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000053")/* @res "û�����ݣ�" */);
			return;
		}
		// ׼����ӡ����
		String title = nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000319")/* @res " ��λ���кŷ�����ϸ��" */;
		GeneralBillHeaderVO voHead = (GeneralBillHeaderVO) voBill.getParentVO();
		// ׼����ͷ�ִ�
		StringBuffer headstr = new StringBuffer(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000320")/*
																																 * @res
																																 * "���ݺţ�"
																																 */);
		if (voHead.getVbillcode() != null) headstr.append(voHead.getVbillcode());
		else headstr.append("      ");

		headstr.append(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000321")/* @res "�ֿ⣺" */);
		if (voHead.getCwarehousename() != null) headstr.append(voHead.getCwarehousename());
		else headstr.append("       ");
		headstr.append(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000322")/* @res "���ڣ�" */);
		if (voHead.getDbilldate() != null) headstr.append(voHead.getDbilldate().toString());
		else headstr.append("       ");

		// ׼��������������
		String[][] colname = new String[1][11];
		int[] colwidth = new int[11];
		int[] alignflag = new int[11];
		String[] names = new String[] {
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC000-0001480")/* @res "�������" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC000-0001453")/* @res "�������" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC000-0000745")/* @res "��λ" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC000-0003327")/* @res "������" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000182")/* @res "����" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC000-0003971")/* @res "������" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC000-0003938")/* @res "����λ" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC000-0002161")/* @res "������" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC000-0002282")/* @res "����" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC000-0003830")/* @res "��λ" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC000-0001819")
		/* @res "���к�" */};
		for (int i = 0; i < 11; i++) {
			colname[0][i] = names[i];
			colwidth[i] = 60;
			// String
			alignflag[i] = 0;
			// decimal
			if (i == 5 || i == 8 || i == 7) alignflag[i] = 2;

		}

		// ׼��������������

		Vector v = new Vector(); // ����Ҫ��ӡ������
		Object[] data = null;
		// �������ݾ���by ZSS
		ArrayList alVO = new ArrayList();
		alVO.add(voBill);
		setScaleOfListData(alVO);
		voBill = (GeneralBillVO) alVO.get(0);
		//
		GeneralBillItemVO[] voItems = (GeneralBillItemVO[]) voBill.getChildrenVO();
		for (int i = 0; i < voItems.length; i++) {
			// ��������еĻ�λ��Ϊ�ա�ȡ�û�λ�������飬ÿ����һ����λ������һ�У�����ȡ�Ի�λVO�����к����λpk���

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
					// �������
					data[0] = voItems[i].getCinventorycode();
					data[1] = voItems[i].getInvname();
					data[2] = voItems[i].getMeasdocname();
					data[3] = voItems[i].getVfree0();
					data[4] = voItems[i].getVbatchcode();
					data[6] = voItems[i].getCastunitname();
					data[7] = voItems[i].getHsl();
					// ����ninspacenum or noutspacenum
					if (locs[j].getNinspacenum() != null) {
						data[8] = locs[j].getNinspacenum();
						data[5] = locs[j].getNinspaceassistnum();
					} else {
						data[8] = locs[j].getNoutspacenum();
						data[5] = locs[j].getNoutspaceassistnum();
					}

					// ��λcsname
					data[9] = locs[j].getVspacename();
					// ������кŲ�Ϊ��
					data[10] = getSNString(voItems[i].getSerial(), locs[j].getCspaceid());
					v.add(data);
				}
			}
			// �����λΪ�գ�
			else {
				data = new Object[11];
				// �������
				data[0] = voItems[i].getCinventorycode();
				data[1] = voItems[i].getInvname();
				data[2] = voItems[i].getMeasdocname();
				data[3] = voItems[i].getVfree0();
				data[4] = voItems[i].getVbatchcode();
				data[6] = voItems[i].getCastunitname();
				data[7] = voItems[i].getHsl();
				// ����ȡ�Ա�����
				if (voItems[i].getNinnum() != null) {
					data[8] = voItems[i].getNinnum();
					data[5] = voItems[i].getNinassistnum();
				} else {
					data[8] = voItems[i].getNoutnum();
					data[5] = voItems[i].getNoutassistnum();
				}

				// ������кŲ�Ϊ��,���к���Ϣ����������
				data[10] = getSNString(voItems[i].getSerial(), null);
				v.add(data);
			}

		}
		if (v.size() > 0) {
			// �������ݣ�ȥ�������У�
			Object[][] data1 = new Object[v.size()][11];

			for (int i = 0; i < v.size(); i++) {

				data1[i] = (Object[]) v.get(i);

			}

			java.awt.Font font = new java.awt.Font("dialog", java.awt.Font.BOLD, 18);
			java.awt.Font font1 = new java.awt.Font("dialog", java.awt.Font.PLAIN, 12);
			String topstr = nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000323")/* @res "��˾��" */
					+ nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getUnitname() + headstr;

			String botstr = nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000325")/* @res "�Ʊ��ˣ�" */
					+ nc.ui.pub.ClientEnvironment.getInstance().getUser().getUserName() + nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000324")/* @res " �Ʊ����ڣ�" */
					+ nc.ui.pub.ClientEnvironment.getInstance().getDate();
			//
			nc.ui.pub.print.PrintDirectEntry print = new nc.ui.pub.print.PrintDirectEntry();

			print.setTitle(title);
			// ���� ��ѡ
			print.setTitleFont(font);
			// �������� ��ѡ
			print.setContentFont(font1);
			// �������壨��ͷ����񡢱�β�� ��ѡ
			print.setTopStr(topstr);
			// ��ͷ��Ϣ ��ѡ
			// ҳ��
			print.setBottomStr(botstr);
			print.setPageNumDisp(true);
			print.setPageNumFont(font1);
			// ����0 1 2
			print.setPageNumAlign(2);
			// ����0 1 2
			print.setPageNumPos(2);
			print.setPageNumTotalDisp(true);
			// �̶���ͷ
			print.setFixedRows(1);
			// ��β��Ϣ ��ѡ
			print.setColNames(colname);
			// �����������ά������ʽ��
			print.setData(data1);
			// �������
			print.setColWidth(colwidth);
			// ����п� ��ѡ
			print.setAlignFlag(alignflag);
			// ���ÿ�еĶ��뷽ʽ��0-��, 1-��, 2-�ң���ѡ

			print.preview();
			// Ԥ��

		}
		// �ύ��ӡ
	}

	/**
	 * �����ߣ����˾� ���ܣ���ѯ���ݵı��壬���ѽ���õ�arraylist ������ int iaIndex[],������alAlldata�е�������
	 * String saBillPK[]����pk���� ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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

			// ʹ�ù�ʽ��ѯʱ,����������PK
			voCond.setIntParam(0, GeneralBillVO.QRY_ITEM_ONLY_PURE);

			voCond.setParam(0, saBillPK);
			// ���ý�����
			// getPrgBar(PB_QRY).start();
			// long lTime = System.currentTimeMillis();
			ArrayList alRetData = (ArrayList) GeneralBillHelper.queryBills(m_sBillTypeCode, voCond);
			if (alRetData == null || alRetData.size() == 0 || iaIndex.length != alRetData.size()) {
				SCMEnv.out("ret item value ERR.");
				return;
			}

			setAlistDataByFormula(-1, alRetData);
			SCMEnv.out("1�����ʽ�����ɹ���");

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
	 * �����ߣ����˾� ���ܣ��б���ʽ�´�ӡǰ�� ��Ҫ�Ļ���ʣ��ĵ��ݱ��塣 ������ ���أ� ���⣺ ���ڣ�(2001-11-23 18:11:18)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	protected void queryLeftItem(ArrayList alListData) throws Exception {
		// -------------
		if (alListData == null || alListData.size() == 0) return;
		int iIntegralBillNum = 0; // �������ݵ�����
		GeneralBillVO voBill = null;
		GeneralBillItemVO[] voaItem = null;
		for (int bill = 0; bill < alListData.size(); bill++)
			if (alListData.get(bill) != null) {
				voBill = (GeneralBillVO) m_alListData.get(bill);
				voaItem = voBill.getItemVOs();
				if (voaItem != null && voaItem.length > 0) iIntegralBillNum++;
			}
		// ����û�б���ĵ���
		if (iIntegralBillNum < alListData.size()) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000054")/*
																											* @res "����׼����ӡ���ݣ����Ժ�..."
																											*/);
			if ((double) iIntegralBillNum / alListData.size() < 0.60) {
				// ������ڷ�ֵ�ĵ���δ�������ɴ����²�ѯ
				QryConditionVO voCond = (QryConditionVO) m_voLastQryCond.clone();
				// ��ѯ��������
				voCond.setIntParam(0, GeneralBillVO.QRY_FULL_BILL);
				SCMEnv.out("���²��������ݣ�׼����ӡ������");
				m_alListData = (ArrayList) GeneralBillHelper.queryBills(m_sBillTypeCode, voCond);
			} else { // ����ֻ��ʣ�µı��弴�ɡ�

				// ������pk�����ڲ�ѯ������ǰ���ѭ������
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
	 * �����ߣ����˾� ���ܣ��������ô��ID,��������������ݣ��������Ρ�������������������� �������кţ����ID ���أ� ���⣺
	 * ���ڣ�(2001-5-8 19:08:05) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * (1)��ñ������billItemVO (2)����������� (3)�����渳������� (4)����ؼ������ǲ��ǿ��Բ���������
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

			// ����ÿһ�еĴ��ID,������
			ArrayList alBillItems = new ArrayList();
			for (int i = 0; i < voBill.getItemVOs().length; i++) {
				alBillItems.add(voaItem[i]);
			}
			SCMEnv.showTime(lTime, "resetAllInvInfo:getItems");
			lTime = System.currentTimeMillis();
			// �������������
			getFormulaBillContainer().formulaBodys(getFormulaItemBody(), alBillItems);

			// �ƻ��۸��ѯ add by hanwei 2004-6-30
			if (alBillItems != null && voaItem.length > 0) {
				int iLen = voaItem.length;
				InvVO[] invVOs = new InvVO[iLen];
				ArrayList<InvVO> astsoilvos = new ArrayList<InvVO>(invVOs.length);
				for (int i = 0; i < voaItem.length; i++) {
					// ���GeneralBillItemVO��invVO����
					invVOs[i] = voaItem[i].getInv();
					if (invVOs[i] != null && invVOs[i].getIsAstUOMmgt() != null && invVOs[i].getIsAstUOMmgt().intValue() == 1 && invVOs[i].getIsSolidConvRate() != null && invVOs[i].getIsSolidConvRate().intValue() == 1) astsoilvos.add(invVOs[i]);
				}

				long ITime = System.currentTimeMillis();
				String sCalID = null;
				String sWhID = null;

				if (voBill.getHeaderValue(m_sMainCalbodyItemKey) != null) sCalID = (String) voBill.getHeaderValue(m_sMainCalbodyItemKey);

				if (voBill.getHeaderValue(m_sMainWhItemKey) != null) sWhID = (String) voBill.getHeaderValue(m_sMainWhItemKey);

				// DUYONG �˴���Ҫͬʱ���ݿ����֯�Ͳֿ��ID��Ϊ�˴ӳɱ�������֯�ж�ȡ�ƻ��۸�
				getInvoInfoBYFormula().getProductPrice(invVOs, sCalID, sWhID);
				if (astsoilvos.size() > 0 && voaItem[0].getCsourcetype() != null && voaItem[0].getCsourcetype().trim().startsWith("2")) getInvoInfoBYFormula().getInVoOfHSLByHashCach(astsoilvos.toArray(new InvVO[astsoilvos.size()]));
				SCMEnv.showTime(ITime, "getProductPrice:" + invVOs.length + "����");
				for (int i = 0; i < voaItem.length; i++) {
					// ���GeneralBillItemVO��invVO����
					voaItem[i].setInv(invVOs[i]);
				}
			}

			SCMEnv.showTime(lTime, "resetAllInvInfo:formulaBodys");

			lTime = System.currentTimeMillis();
			GeneralBillItemVO[] voBillItems = new GeneralBillItemVO[alBillItems.size()];
			alBillItems.toArray(voBillItems);

			// voBill.calConvRate(); //���㻻����

			SCMEnv.showTime(lTime, "resetAllInvInfo:hsl");
			// ���ý�������
			// �޸� by hanwei 2003-11-18 hw
			lTime = System.currentTimeMillis();
			// ��������
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
			SCMEnv.showTime(lTime, "���κż�������ʱ��");

			getBillCardPanel().getBillModel().setNeedCalculate(true);
			getBillCardPanel().getBillModel().reCalcurateAll();
		} catch (Exception e2) {
			nc.vo.scm.pub.SCMEnv.error(e2);
		}
	}

	/**
	 * �����ߣ����˾� ���ܣ������趨�İ�ť��ʼ���˵��� ������ ���أ� ���⣺ ���ڣ�(2001-11-23 18:11:18)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
	 * �˴����뷽��˵���� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2001-11-21 14:47:40) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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

			// ������
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
			// ����
			if (num == null) {
				voLoc[0].setNinspacenum(num);
				voLoc[0].setNoutspacenum(num);
				if (m_alSerialData != null) m_alSerialData.set(row, null);
			} else {
				if (m_iBillInOutFlag > 0) {
					// ���
					voLoc[0].setNoutspacenum(null);
					voLoc[0].setNinspacenum(num);
				} else {
					// ����
					voLoc[0].setNinspacenum(null);
					voLoc[0].setNoutspacenum(num);
					if (m_alSerialData != null) m_alSerialData.set(row, null);
				}
			}
			// ë��
			if (ngrossnum == null) {
				voLoc[0].setNingrossnum(ngrossnum);
				voLoc[0].setNoutgrossnum(ngrossnum);
				if (m_alSerialData != null) m_alSerialData.set(row, null);
			} else {
				if (m_iBillInOutFlag > 0) {
					// ���
					voLoc[0].setNoutgrossnum(null);
					voLoc[0].setNingrossnum(ngrossnum);
				} else {
					// ����
					voLoc[0].setNingrossnum(null);
					voLoc[0].setNoutgrossnum(ngrossnum);

				}

			}

		} 
		else if (voLoc != null && voLoc.length == 2&& m_sBillTypeCode.equals("4Q")) {//��λ���������л�λ���

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
	 * �����ߣ����˾� ���ܣ����ֿ��������ݣ��ŵ�m_voBill ������ ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
			} // ��ѯ��Ҫ�ֿ�ID
			String sWhID = voBill.getHeaderValue(m_sMainWhItemKey).toString().trim();
			// ���ֿ�����
			// �飬����WhVO
			WhVO voWh = (WhVO) GeneralBillHelper.queryInfo(new Integer(QryInfoConst.WH), sWhID);

			if (m_voBill != null) { // ���òֿ�����
				m_voBill.setWh(voWh);
				voBill.setWh(voWh);// v5
				// ���û�λ��ť������
				setBtnStatusSpace(false);
			}

		} catch (Exception e2) {
			nc.vo.scm.pub.SCMEnv.error(e2);
			showErrorMessage(e2.getMessage());
		}
	}

	/**
	 * ����ǰ�Ե�������ĳЩ�̶�ֵ��
	 * 
	 * @since v5
	 * @author ljun ֻ��ί��ӹ���ⵥ���أ����ü��ɹ�Ĭ��ֵ�����ڲɹ�����
	 */
	// v5
	protected void beforeSave(GeneralBillVO voBill) {

	}

	/**
	 * �����ߣ����˾� ���ܣ����������ĵ��� �д�����Ҫ���쳣�׳���Ӱ��������
	 * 
	 * �������������� ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	protected void saveNewBill(GeneralBillVO voNewBill) throws Exception {

		try {
			// �õ����ݴ��󣬳��� ------------ EXIT -------------------
			nc.vo.ic.pub.bill.Timer timer = new nc.vo.ic.pub.bill.Timer();
			timer.start("@@saveNewBill��ʼ");
			if (voNewBill == null || voNewBill.getParentVO() == null || voNewBill.getChildrenVO() == null) {
				SCMEnv.out("Bill is null !");
				throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000055")/*
																													* @res
																													* "����Ϊ�գ�"
																													*/);
			}
			// ִ�б���...
			// ͨ��ƽ̨ʵ�ֱ��漴ǩ�֣�����ǩ�����ֶΣ��ڳ���ⵥ����ʱ���֮��
			// ֧��ƽ̨��cloneһ�����Ա����Ժ�Ĵ���ͬʱ��ֹ�޸���m_voBill
			GeneralBillVO voTempBill = (GeneralBillVO) voNewBill.clone();
			GeneralBillHeaderVO voHead = voTempBill.getHeaderVO();
			voHead.setPk_corp(m_sCorpID);

			// voHead.setDaccountdate(new nc.vo.pub.lang.UFDate(m_sLogDate));
			// �����ĵ������PK
			voHead.setPrimaryKey(null);
			// ����PK
			GeneralBillItemVO[] voaItem = voTempBill.getItemVOs();
			for (int row = 0; row < voaItem.length; row++)
				voaItem[row].setPrimaryKey(null);

			// ��������������������
			nc.vo.ic.pub.barcodeparse.BarcodeparseCtrl.beforSaveBillVOBarcode(m_bBarcodeSave, voNewBill);

			voHead.setCoperatoridnow(m_sUserID); // ��ǰ����Ա2002-04-10.wnj
			voHead.setAttributeValue("clogdatenow", m_sLogDate); // ��ǰ��¼����2003-01-05

			voTempBill.setParentVO(voHead);
			voTempBill.setChildrenVO(voaItem);
			timer.showExecuteTime("@@���ñ�ͷ�ͱ��壺");
			// --------- save -------------
			// ���뵥�ݺ�,������ݺ�==sCorpID�������ݺ��ÿգ���̨���Զ���ȡ��
			if (m_sCorpID.equals(voHead.getVbillcode())) {
				voHead.setVbillcode(null);
			}
			ArrayList alPK = null;

			// ����У�����־ add by hanwei 2004-04-01
			voTempBill.setAccreditUserID(voNewBill.getAccreditUserID());
			voTempBill.setOperatelogVO(voNewBill.getOperatelogVO());
			// IP��ַ����
			// ���ݲ����Ƿ񱣴����룬�������

			// �Ƿ񱣴�����
			voTempBill.setSaveBadBarcode(m_bBadBarcodeSave);
			// �Ƿ񱣴�������һ�µ�����
			voTempBill.setSaveBarcode(m_bBarcodeSave);
			// ���ڡ�������Ϣ
			voTempBill.m_iActionInt = nc.vo.scm.pub.bill.CreditConst.ICREDIT_ACT_ADD;
			voTempBill.m_sActionCode = "SAVEBASE";

			beforeSave(voTempBill);
			alPK = (ArrayList) nc.ui.pub.pf.PfUtilClient.processAction("SAVE", m_sBillTypeCode, m_sLogDate, voTempBill);
			SCMEnv.out("ret..");
			timer.showExecuteTime("@@��ƽ̨����ʱ�䣺");

			// [[��ʾ��Ϣ][PK_Head,PK_body1,PK_body2,....]]
			// ���浱ǰ���ݵ�OID
			// �������ݴ��� ------------------- EXIT --------------------------
			if (alPK == null || alPK.size() < 3 || alPK.get(1) == null || alPK.get(2) == null) {
				SCMEnv.out("return data error. al pk" + alPK);
				showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000056")/*
																												* @res
																												* "�����Ѿ����棬������ֵ���������²�ѯ���ݡ�"
																												*/);
			} else {

				// ��ʾ��ʾ��Ϣ
				if (alPK.get(0) != null && alPK.get(0).toString().trim().length() > 0) showWarningMessage((String) alPK.get(0));

				// ����
				int iRowCount = voNewBill.getItemCount();
				ArrayList alMyPK = (ArrayList) alPK.get(1);
				if (alMyPK == null || alMyPK.size() < (iRowCount + 1) || alMyPK.get(0) == null || alMyPK.get(1) == null) {
					SCMEnv.out("return data error. my pk " + alMyPK);
					showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000057")/*
																													* @res
																													* "���淵��ֵ����"
																													*/);
				} else {
					// ��ͷ��OID
					m_sCurBillOID = (String) alMyPK.get(0);
					// ���ý���ĵ���ID
					if (getBillCardPanel().getHeadItem(m_sBillPkItemKey) != null) getBillCardPanel().setHeadItem(m_sBillPkItemKey, m_sCurBillOID);
					//
					voNewBill.getParentVO().setPrimaryKey(m_sCurBillOID);
					// �����OID
					// ###################################################
					// �������õ���vo����VOPK����ͷts,billcode,����
					// ��ͷ��cgeneralhid,fbillfalg,vbillcode,ts
					// ���壺cgeneralbid,crowno,vbatchcode,vfirstbillcode,ninnum,ninassistnum,noutnum,noutassistnum,ts
					SMGeneralBillVO smbillvo = null;
					smbillvo = (SMGeneralBillVO) alPK.get(2);
					m_sBillStatus = (smbillvo.getHeaderVO().getFbillflag()).toString();

					getGenBillUICtl().refreshSaveData(smbillvo, getBillCardPanel(), voNewBill, m_voBill);
					getGenBillUICtl().refreshLocFromSMVO(smbillvo, getBillCardPanel(), m_alLocatorData, m_voBill);
					getGenBillUICtl().refreshBatchcodeAfterSave(getBillCardPanel(), m_voBill);

					timer.showExecuteTime("@@���ñ�ͷ�ͱ���PKʱ�䣺");
					// ��д��m_alListData
					if (!m_bRefBills) insertBillToList(m_voBill);

					timer.showExecuteTime("@@insertBillToList(m_voBill)��");
				}
			}
			SCMEnv.out("insertok..");

			// v5 lj ֧�ֲ������ɶ��ŵ���
			if (m_bRefBills == true) {
				// removeBillsOfList(new int[] { m_iLastSelListHeadRow });

				setButtonStatus(false);
				ctrlSourceBillUI(true);
			}

		} catch (Exception e) {
			// �쳣�����׳����������̴�����Ϊ��Ӱ�������̡�
			// ###################################################
			// ���������쳣����¼��̨��־ add by hanwei 2004-6-8
			// ###################################################

			throw e;

		}
	}

	/**
	 * �����ߣ����˾� ���ܣ������޸ĵĵ���
	 * 
	 * �д�����Ҫ���쳣�׳���Ӱ��������
	 * 
	 * ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	protected void saveUpdatedBill(GeneralBillVO voUpdatedBill) throws Exception {
		try {
			// �õ����ݴ��󣬳��� ------------ EXIT -------------------
			nc.vo.ic.pub.bill.Timer timer = new nc.vo.ic.pub.bill.Timer();
			timer.start("@@�޸ı��浥�ݿ�ʼ��");
			if (voUpdatedBill == null || voUpdatedBill.getParentVO() == null || voUpdatedBill.getChildrenVO() == null) {
				SCMEnv.out("Bill is null !");
				throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000055")/*
																													* @res
																													* "����Ϊ�գ�"
																													*/);
			}

			// ��������������������
			nc.vo.ic.pub.barcodeparse.BarcodeparseCtrl.beforSaveBillVOBarcode(m_bBarcodeSave, voUpdatedBill);

			// ���õ�������
			voUpdatedBill.setHeaderValue("cbilltypecode", m_sBillTypeCode);
			// 05/07�����Ƶ���Ϊ��ǰ����Ա
			// remark by zhx onSave() set coperatorid into VO
			// voUpdatedBill.setHeaderValue("coperatorid", m_sUserID);
			timer.showExecuteTime("@@���õ������ͣ�");
			GeneralBillVO voBill = (GeneralBillVO) m_voBill.clone();
			timer.showExecuteTime("@@m_voBill.clone()��");
			voBill.setIDItems(voUpdatedBill);
			int iDelRowCount = voUpdatedBill.getItemCount() - voBill.getItemCount();
			if (iDelRowCount > 0) {
				// ����У�׷��ɾ����
				GeneralBillItemVO voaItems[] = new GeneralBillItemVO[voUpdatedBill.getItemCount()];
				// ԭ��
				for (int org = 0; org < voBill.getItemCount(); org++)
					voaItems[org] = voBill.getItemVOs()[org];

				// ����ɾ����
				for (int del = 0; del < iDelRowCount; del++)
					voaItems[voBill.getItemCount() + del] = voUpdatedBill.getItemVOs()[voBill.getItemCount() + del];

				voBill.setChildrenVO(voaItems);
				timer.showExecuteTime("@@voBill.setChildrenVO(voaItems)��");
			}
			GeneralBillHeaderVO voHead = voBill.getHeaderVO();
			// --------------------------------------------���Բ��ǵ�ǰ��¼��λ�ĵ��ݣ����Բ���Ҫ�޸ĵ�λ��
			voHead.setPk_corp(m_sCorpID);
			// ��Ϊ��¼���ں͵��������ǿ��Բ�ͬ�ģ����Ա���Ҫ��¼���ڡ�
			// voHead.setDaccountdate(new nc.vo.pub.lang.UFDate(m_sLogDate));
			// vo����Ҫ����ƽ̨������Ҫ���ɺ�ǩ�ֺ�ĵ���
			voHead.setCoperatoridnow(m_sUserID); // ��ǰ����Ա2002-04-10.wnj
			voHead.setAttributeValue("clogdatenow", m_sLogDate); // ��ǰ��¼����2003-01-05

			// update by zhx on 0926 ���Ӹ��ݿ�����IC060 �ж��Ƿ���������Ƶ��ˡ�
			// IC060 ����N�� �����������޸ĵ���ʱ�����Ƶ��ˡ����򣬲����á�
			// if (m_sRemainOperator != null
			// && m_sRemainOperator.equalsIgnoreCase("N"))
			// voHead.setCoperatorid(m_sUserID);

			// �޸�ǰ�ĵ���
			GeneralBillVO voOriginalBill = null;
			// ���Ե����б�
			if (m_iLastSelListHeadRow >= 0 && m_alListData != null && m_alListData.size() > m_iLastSelListHeadRow && m_alListData.get(m_iLastSelListHeadRow) != null) {
				voOriginalBill = (GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow);
				voOriginalBill.getHeaderVO().setCoperatoridnow(m_sUserID);
				voOriginalBill.getHeaderVO().setPk_corp(m_sCorpID);
			} else {
				SCMEnv.out("original null,maybe error.");
				throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000058")/*
																													* @res
																													* "δ�ҵ�ԭʼ���ݣ����ѯ�����ԡ�"
																													*/);
			}
			timer.showExecuteTime("@@���ñ�ͷ���ݣ�");
			// 2003-06-13.02 wnj:���û�λ�����к��޸�״̬��
			voBill.setLocStatus(voOriginalBill);
			timer.showExecuteTime("@@���û�λ�����к��޸�״̬��");
			// ----
			// add by hanwei 2004-04
			voBill.setAccreditUserID(voUpdatedBill.getAccreditUserID());
			voBill.setOperatelogVO(voUpdatedBill.getOperatelogVO());

			// �Ƿ񱣴�����
			voBill.setSaveBadBarcode(m_bBadBarcodeSave);
			// �Ƿ񱣴�������һ�µ�����
			voBill.setSaveBarcode(m_bBarcodeSave);
			// ���ڡ�������Ϣ
			voBill.m_iActionInt = nc.vo.scm.pub.bill.CreditConst.ICREDIT_ACT_MODIFY;
			voBill.m_sActionCode = "SAVEBASE";
			voBill.m_voOld = voOriginalBill;
			ArrayList alRetData = (ArrayList) nc.ui.pub.pf.PfUtilClient.processAction("SAVE", m_sBillTypeCode, m_sLogDate, voBill, voOriginalBill);
			timer.showExecuteTime("@@��ƽ̨���棺��");
			// new GeneralBillVO[]{m_voBill});
			// [[��ʾ��Ϣ][new_PK_body1,new_PK_body2,....]]
			// ���浱ǰ���ݵ�OID
			// �������ݴ��� ------------------- EXIT --------------------------
			if (alRetData == null || alRetData.size() < 3 || alRetData.get(1) == null || alRetData.get(2) == null) {
				SCMEnv.out("return data error.");
				showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000056")/*
																												* @res
																												* "�����Ѿ����棬������ֵ���������²�ѯ���ݡ�"
																												*/);
			} else {
				// 0 ---- ��ʾ��ʾ��Ϣ
				if (alRetData.get(0) != null && alRetData.get(0).toString().trim().length() > 0) showWarningMessage((String) alRetData.get(0));
				// 1 ---- ���ص�PK
				ArrayList alMyPK = (ArrayList) alRetData.get(1);

				// ���������е�PK
				setBodyPkAfterUpdate(alMyPK);
				timer.showExecuteTime("@@���������е�PK����");
				// ���ع�ʽ
				// getBillCardPanel().getBillModel().execLoadFormula();
				// ���»��vo����ˢ���б���ʽ��
				// ���������vo����ɾ���У��޸���ֻ��id,�ͽ����ϵ����ݲ���һһ��Ӧ�ġ�
				// ����Ҫ���¶�һ�Ρ�

				// necessary��//ˢ�µ���״̬

				// 05/07���ý����Ƶ���Ϊ��ǰ����Ա
				// update by zhx on 0926 ���Ӹ��ݿ�����IC060 �ж��Ƿ���������Ƶ��ˡ�
				// IC060 ����N�� �����������޸ĵ���ʱ�����Ƶ��ˡ����򣬲����á�
				// if (m_sRemainOperator != null
				// && m_sRemainOperator.equalsIgnoreCase("N")) {
				// if (getBillCardPanel().getTailItem("coperatorid") != null)
				// getBillCardPanel()
				// .setTailItem("coperatorid", m_sUserID);
				// if (getBillCardPanel().getTailItem("coperatorname") != null)
				// getBillCardPanel().setTailItem("coperatorname",
				// m_sUserName);
				// }

				timer.showExecuteTime("@@getBillCardPanel().updateValue()����");
				voUpdatedBill = getBillVO();
				// ���õ�������
				voUpdatedBill.setHeaderValue("cbilltypecode", m_sBillTypeCode);

				// ###################################################
				// �������õ���vo����VOPK����ͷts,billcode,����
				// ��ͷ��cgeneralhid,fbillfalg,vbillcode,ts
				// ���壺cgeneralbid,crowno,vbatchcode,vfirstbillcode,ninnum,ninassistnum,noutnum,noutassistnum,ts
				SMGeneralBillVO smbillvo = null;
				smbillvo = (SMGeneralBillVO) alRetData.get(2);
				m_sBillStatus = (smbillvo.getHeaderVO().getFbillflag()).toString();
				getGenBillUICtl().refreshSaveData(smbillvo, getBillCardPanel(), voUpdatedBill, m_voBill);
				getGenBillUICtl().refreshLocFromSMVO(smbillvo, getBillCardPanel(), m_alLocatorData, m_voBill);
				getGenBillUICtl().refreshBatchcodeAfterSave(getBillCardPanel(), m_voBill);

				// ���by hanwei 2004-9-23
				setBillBCVOStatus(m_voBill, nc.vo.pub.VOStatus.UNCHANGED);
				// ###################################################

				getBillCardPanel().updateValue();

				timer.showExecuteTime("@@m_voBill.setIDItems(voUpdatedBill)��");
				// ˢ���б�����
				updateBillToList(m_voBill);
				timer.showExecuteTime("@@ˢ���б�����updateBillToList(m_voBill)��");
			}

		} catch (Exception e) {
			// �쳣�����׳����������̴�����Ϊ��Ӱ�������̡�
			throw e;

		}
	}

	/**
	 * �����ߣ����˾� ���ܣ����б�ʽ��ѡ��һ�ŵ��� ������ ������alListData�е����� ���أ��� ���⣺ ���ڣ�(2001-11-23
	 * 18:11:18) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	abstract protected void selectBillOnListPanel(int iBillIndex);

	/**
	 * �������ڣ�(2003-3-6 11:33:06) ���ߣ����� �޸����ڣ� �޸��ˣ� �޸�ԭ�� ����˵����
	 * ȡ�õ��ݲ�ѯ���ݣ���ָ��������Χ�ĵ��ݵı��� �ô����ʽ��������������й���������
	 * 
	 * @param iTopnum
	 *            int
	 * @param lListData
	 *            java.util.ArrayList
	 */
	public void setAlistDataByFormula(int iTopnum, ArrayList lListData) {
		if (lListData == null || lListData.size() == 0) return;
		// ��Ҫ�����ʽ�����ĵ�����Ŀ
		int iFormulaNum = 0;
		// ���iTopnum=-1 ��ʾȡ���еĵ���
		// ���lListData.size
		// ���� iTopnum ��lListData.sizeΪ��׼
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
					// ���item
					alItemVos.add(itemVos[j]);
				}
			}

		}

		if (alItemVos != null && alItemVos.size() > 0) {
			// ͨ�����ݹ�ʽ������ִ���йع�ʽ�����ķ���
			getFormulaBillContainer().formulaBodys(getFormulaItemBody(), alItemVos);

		}

	}

	/**
	 * �����ߣ����˾� ���ܣ����õ��ݳ�������� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	public void setBillInOutFlag(int iInOutFlag) {
		// ���
		if (iInOutFlag == InOutFlag.IN) {
			// ���������ֶ���
			m_sNumItemKey = "ninnum";
			// ���帨�����ֶ���
			m_sAstItemKey = "ninassistnum";
			// ����Ӧ�������ֶ���
			m_sShouldAstItemKey = "nneedinassistnum";
			// ����Ӧ�����ֶ���
			m_sShouldNumItemKey = "nshouldinnum";
			//
			m_sNgrossnum = "ningrossnum";
			m_iBillInOutFlag = iInOutFlag;
		} else // ����
		if (iInOutFlag == InOutFlag.OUT) {
			// ����ʵ�ʳ��������ֶ���
			m_sNumItemKey = "noutnum";
			// ����ʵ�ʳ��⸨�����ֶ���
			m_sAstItemKey = "noutassistnum";
			// ����Ӧ���������ֶ���
			m_sShouldAstItemKey = "nshouldoutassistnum";
			// ����Ӧ�������ֶ���
			m_sShouldNumItemKey = "nshouldoutnum";
			m_sNgrossnum = "noutgrossnum";
			m_iBillInOutFlag = iInOutFlag;
		} else showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000059")/* @res "�����������ô���" */);
	}

	/**
	 * �����ߣ����˾� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2001-11-23 18:05:45) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @param newM_sBillTypeCode
	 *            java.lang.String
	 */
	public void setBillTypeCode(java.lang.String newM_sBillTypeCode) {
		m_sBillTypeCode = newM_sBillTypeCode;
	}

	/**
	 * ����˵���� �������ڣ�(2002-12-23 11:59:38) ���ߣ����˾� �޸����ڣ� �޸��ˣ� �޸�ԭ�� �㷨˵����
	 * 
	 * @param row
	 *            int
	 * 
	 */
	private void setBodySpace(int row) {

		if (getBillCardPanel().getBodyItem("vspacename") == null) return;
		getBillCardPanel().setBodyValueAt(null, row, "vspacename");
		getBillCardPanel().setBodyValueAt(null, row, "cspaceid");

		// д�����λ
		if (row < 0 || m_alLocatorData == null || m_alLocatorData.size() < row + 1) return;

		LocatorVO[] voaLoc = (LocatorVO[]) m_alLocatorData.get(row);

		if (voaLoc != null && voaLoc.length == 1 && voaLoc[0] != null) {
			getBillCardPanel().setBodyValueAt(voaLoc[0].getVspacename(), row, "vspacename");
			getBillCardPanel().setBodyValueAt(voaLoc[0].getCspaceid(), row, "cspaceid");
		}
	}

	/**
	 * �����ߣ����˾� ���ܣ������趨�İ�ť��ʼ���˵��� ������ ���أ� ���⣺ ���ڣ�(2001-11-23 18:11:18)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	protected void setButtons() {
		setButtons(getButtonTree().getButtonArray());
		/**
		 * try { Vector vSubMenu = null; if (m_vTopMenu != null &&
		 * m_vTopMenu.size() > 0) { // û�е��ݲ��յĻ��������ڵ��ݱ༭�˵���������ҵ�����ͺ� if
		 * (!m_bNeedBillRef) { if (m_vBillMngMenu != null)
		 * m_vBillMngMenu.insertElementAt(m_boAdd, 1); } else {//
		 * �е��ݲ��յĻ���������ҵ�����ͺ� m_vTopMenu.insertElementAt(m_boAdd, 0);
		 * m_vTopMenu.insertElementAt(m_boJointAdd, 0); }
		 * 
		 * // ����˵� m_boaButtonGroup = new ButtonObject[m_vTopMenu.size()]; //
		 * �Ӳ˵� ButtonObject boSub = null; for (int i = 0; i < m_vTopMenu.size();
		 * i++) { if (m_vTopMenu.elementAt(i) instanceof Vector) { // �Ӳ˵�
		 * vSubMenu = (Vector) m_vTopMenu.elementAt(i); // ��0���Ƕ���˵� boSub =
		 * (ButtonObject) vSubMenu.elementAt(0); for (int j = 1; j <
		 * vSubMenu.size(); j++) boSub.addChildButton((ButtonObject) vSubMenu
		 * .elementAt(j)); m_boaButtonGroup[i] = boSub; } else // ����˵�
		 * m_boaButtonGroup[i] = (ButtonObject) m_vTopMenu .elementAt(i); }
		 * setButtons(m_boaButtonGroup); } } catch (Exception e) {
		 * handleException(e); }
		 */
	}

	/**
	 * �����ߣ����˾� ���ܣ����󷽷������ð�ť״̬����setButtonStatus�е��á� ������ ���أ� ���⣺ ���ڣ�(2001-5-9
	 * 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */

	abstract protected void setButtonsStatus(int iBillMode);

	/**
	 * �����ߣ����˾� ���ܣ����ñ�������ɫ�� ������ ���أ� ���⣺ ���ڣ�(2001-11-23 18:11:18)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	protected void setColor() {
		try {
			// ���ı���ɫ��ı�����Header��ɫ��
			javax.swing.table.DefaultTableCellRenderer tcrold = (javax.swing.table.DefaultTableCellRenderer) getBillCardPanel().getBillTable().getColumnModel().getColumn(1).getHeaderRenderer();
			HeaderRenderer tcr = new HeaderRenderer(tcrold);

			// �ֱ�õ���ͷ�ͱ�������������ʾ���ֶ�
			ArrayList alHeaderColChangeColorString = GeneralMethod.getHeaderCanotNullString(getBillCardPanel());
			ArrayList alBodyColChangeColorString = GeneralMethod.getBodyCanotNullString(getBillCardPanel());

			// �޸ı��еı�ͷ��ɫ
			SetColor.SetBillCardHeaderColor(getBillCardPanel(), alHeaderColChangeColorString);
			// SetBillCardHeaderColor(alHeaderColChangeColorString);

			// ���������ɫ���ڱ���Header��
			nc.ui.scm.ic.exp.SetColor.SetTableHeaderColor(getBillCardPanel().getBillModel(), getBillCardPanel().getBillTable(), alBodyColChangeColorString, tcr);
			SetColor.SetTableHeaderColor(getBillListPanel().getHeadBillModel(), getBillListPanel().getHeadTable(), alHeaderColChangeColorString, tcr);
			SetColor.SetTableHeaderColor(getBillListPanel().getBodyBillModel(), getBillListPanel().getBodyTable(), alBodyColChangeColorString, tcr);
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
		}
	}

	/**
	 * �����ߣ����˾� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2001-11-23 18:05:45) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @param newM_sCurrentBillNode
	 *            java.lang.String
	 */
	public void setCurrentBillNode(java.lang.String newM_sCurrentBillNode) {
		m_sCurrentBillNode = newM_sCurrentBillNode;
	}

	/**
	 * �˴����뷽��˵���� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2002-07-24 15:31:38) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
	 * �����ߣ����˾� ���ܣ��ڱ�������ʾVO,�����½���״̬updateValue() ������ ���أ� ���⣺ ���ڣ�(2001-5-9
	 * 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	public void setImportBillVO(GeneralBillVO bvo) throws Exception {
		setImportBillVO(bvo, true);
	}

	/**
	 * �����ߣ����˾� ���ܣ�ѡ���б���ʽ�µĵ�sn�ŵ��� ������sn �������
	 * 
	 * ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
				// �����Ѿ���setAlistDataBYFormula��ִ���˱��幫ʽ��
				// ���Բ������ظ������������д��� by hanwei 2003-06-24
				// getBillListPanel().getBodyBillModel().execLoadFormula();
			} catch (Exception e) {
				SCMEnv.out(e.getMessage());
			} finally {
				getBillListPanel().getBodyTable().getModel().addTableModelListener(this);
			}
		}
	}

	/**
	 * �����ߣ����˾� ���ܣ�ˢ���б���ʽ��ͷ����Ϊָ�������� ������ ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
					// ��ӹ�ʽ�����ȡ�ֿ�ͷ�Ʒ�ֿ���Ϣ by hw 2003-02-27
					getInvoInfoBYFormula().setBillHeaderWH(voh);
				}

				// ͨ�����ݹ�ʽ������ִ���йع�ʽ�����ķ���
				getFormulaBillContainer().formulaHeaders(getFormulaItemHeader(), voh);
				// �����������
				getBillListPanel().setHeaderValueVO(voh);
				// �����Ե���������룬�Ѿ�������ִ���˱�ͷ��ʽ
				// getBillListPanel().getHeadBillModel().execLoadFormula();

			} catch (Exception e2) {
				nc.vo.scm.pub.SCMEnv.error(e2);
			}
		}
	}

	/**
	 * �����ߣ����˾� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2001-11-24 12:14:35) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @param newNeedBillRef
	 *            boolean
	 */
	public void setNeedBillRef(boolean newNeedBillRef) {
		m_bNeedBillRef = newNeedBillRef;
	}

	/**
	 * �����ߣ����˾� ���ܣ��б���ʽ�´�ӡǰ�� ��С������ ������ ���أ� ���⣺ ���ڣ�(2001-11-23 18:11:18)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
	 * �����ߣ����˾� ���ܣ����ñ���/��β��С��λ�� ������ ���أ� ���⣺ ���ڣ�(2001-11-23 18:11:18)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	protected void setScaleOfListPanel() {
		nc.ui.ic.pub.scale.ScaleInit si = new nc.ui.ic.pub.scale.ScaleInit(m_sUserID, m_sCorpID, m_ScaleValue);

		try {
			si.setScale(getBillListPanel(), m_ScaleKey);
		} catch (Exception e) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000060")/* @res "��������ʧ�ܣ�" */
					+ e.getMessage());
		}

	}

	/**
	 * �����ߣ����˾� ���ܣ��ڱ�������ʾVO,�����½���״̬updateValue() ������ ���أ� ���⣺ ���ڣ�(2001-5-9
	 * 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	public void setTempBillVO(GeneralBillVO bvo) throws Exception {
		if (bvo == null) throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000061")/* @res "����ĵ���Ϊ�գ�" */);
		// ��������
		int iRowCount = bvo.getItemCount();

		try {
			getBillCardPanel().getBillModel().removeTableModelListener(this);
			getBillCardPanel().removeBillEditListenerHeadTail();
			// ����һ��clone()
			m_voBill = (GeneralBillVO) bvo.clone();
			// ��������
			getBillCardPanel().setBillValueVO(bvo);
			// ִ�й�ʽ
			getBillCardPanel().getBillModel().execLoadFormula();
			execHeadTailFormulas();
			// ����״̬ ---delete it to support CANCEL
			// getBillCardPanel().updateValue();
			// �����ִ�������
			bvo.clearInvQtyInfo();
			// �б����У�ѡ�е�һ��
			if (iRowCount > 0) {
				// ѡ�е�һ��
				getBillCardPanel().getBillTable().setRowSelectionInterval(0, 0);
				// �������к��Ƿ����
				setBtnStatusSN(0, true);
				// ˢ���ִ�����ʾ
				// setTailValue(0);
				// ������������
				nc.ui.pub.bill.BillModel bmTemp = getBillCardPanel().getBillModel();
				m_alLocatorDataBackup = m_alLocatorData;
				m_alSerialDataBackup = m_alSerialData;
				m_alLocatorData = new ArrayList();
				m_alSerialData = new ArrayList();

				for (int row = 0; row < iRowCount; row++) {
					// ������״̬Ϊ����
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
	 * �����ߣ����˾� ���ܣ����ñ���ĺϼ��� ������ ���أ� ���⣺ ���ڣ�(2001-11-23 18:11:18)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	protected void setTotalCol() {
		getBillCardPanel().setTatolRowShow(true);
		// getBillListPanel().set
		// ���ǵ�����ģ���ڲ���Ч�ʣ������ó���Ч�ʸ���Щ����Ϊ���������ϴ��ù�ϣ��ʵ��.
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

		// ������
		String[] saBodyTotalItemKey = {
				"nmny", "nplannedmny", "nshouldinnum", "nshouldoutnum", "ntranoutnum", "nretnum", "noutnum", "nleftnum", "ninnum", "ninassistnum", "nleftastnum", "nneedinassistnum", "noutassistnum", "nretastnum", "nshouldoutassistnum", "ntranoutastnum", "volume", "weight"
		};
		for (int k = 0; k < saBodyTotalItemKey.length; k++) {
			// ����Ǵ���
			if (htCardBody.containsKey(saBodyTotalItemKey[k])) biaCardBody[Integer.valueOf(htCardBody.get(saBodyTotalItemKey[k]).toString()).intValue()].setTatol(true);
			// //����Ǵ���
			// if (htListBody.containsKey(saBodyTotalItemKey[k]))
			// biaListBody[Integer
			// .valueOf(htListBody.get(saBodyTotalItemKey[k]).toString())
			// .intValue()]
			// .setTatol(true);
		}

	}

	/**
	 * �����ߣ����˾� ���ܣ��ڱ�������ʾVO,�����½���״̬updateValue() ������ ���أ� ���⣺ ���ڣ�(2001-5-9
	 * 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	public void setTs(int iIndex, ArrayList alTs) throws Exception {
		// update bill in list data ------- security -----------
		GeneralBillVO voListBill = null;
		// ����m_voBill,�Զ�ȡ�������ݡ�
		if (iIndex >= 0 && m_alListData != null && m_alListData.size() > iIndex && m_alListData.get(iIndex) != null) {
			// ���ﲻ��clone(),�ı���m_voBillͬʱ�ı�m_alListData
			voListBill = (GeneralBillVO) m_alListData.get(iIndex);
		}
		if (voListBill != null) setTs(voListBill, alTs);
	}

	/**
	 * �����ߣ����˾� ���ܣ��ڱ�������ʾVO,�����½���״̬updateValue() ������ ���أ� ���⣺ ���ڣ�(2001-5-9
	 * 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	public void setTs(GeneralBillVO voThisBill, ArrayList alTs) throws Exception {
		if (alTs == null || alTs.size() < 2 || alTs.get(0) == null || alTs.get(1) == null) throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000027")/* @res "�����tsΪ�գ�" */);
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
			// ��������
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
	 * �����ߣ����˾� ���ܣ��ڱ�������ʾVO,�����½���״̬updateValue() ������ ���أ� ���⣺ ���ڣ�(2001-5-9
	 * 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	public void setUiTs(ArrayList alTs) throws Exception {
		if (alTs == null || alTs.size() < 2 || alTs.get(0) == null || alTs.get(1) == null) throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000027")/* @res "�����tsΪ�գ�" */);
		// put ts to a hashtable,KEy=body item,value=ts
		Hashtable htTs = new Hashtable();
		ArrayList alTemp = null;
		for (int i = 1; i < alTs.size(); i++) {
			alTemp = (ArrayList) alTs.get(i);
			if (alTemp != null && alTemp.size() >= 2 && alTemp.get(0) != null && alTemp.get(1) != null) htTs.put(alTemp.get(0), alTemp);
		}
		getBillCardPanel().setHeadItem("ts", alTs.get(0).toString());
		// ��������
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
	 * �����ߣ����˾� ���ܣ����ص���ʾ��ʾ��Ϣ�Ի����� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	public void showErrorMessage(String sMsg) {
		if (m_bUserDefaultErrDlg) super.showErrorMessage(sMsg);
		else nc.ui.pub.beans.MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/* @res "��ʾ" */, sMsg);

	}

	/**
	 * �����ߣ����˾� ���ܣ����ص���ʾ��ʾ��Ϣ�Ի����� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	public void showWarningMessage(String sMsg) {
		if (m_bUserDefaultErrDlg) super.showWarningMessage(sMsg);
		else nc.ui.pub.beans.MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/* @res "��ʾ" */, sMsg);

	}

	/**
	 * �����ߣ����� ���ܣ�����ָ����ҵ������Ϊѡ��ʽ�� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
	 * �����ߣ����˾� ���ܣ���ʾ���ĵ�ʱ�� ������ ���أ� ���⣺ ���ڣ�(2001-11-23 18:11:18)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	protected void showTime(long lStartTime, String sTaskHint) {
		long lTime = System.currentTimeMillis() - lStartTime;
		SCMEnv.out("ִ��<" + sTaskHint + ">���ĵ�ʱ��Ϊ��" + (lTime / 60000) + "��" + ((lTime / 1000) % 60) + "��" + (lTime % 1000) + "����");

	}

	/**
	 * �˴����뷽��˵���� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2002-4-27 13:21:10) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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

	// �û���������У��UI
	protected nc.ui.scm.pub.AccreditLoginDialog m_AccreditLoginDialog;

	// �Ƿ����������� add by hanwei 2004-03-01
	protected boolean m_bAddBarCodeField = true;

	// ����༭���������
	public nc.ui.ic.pub.bill.BarcodeCtrl m_BarcodeCtrl = null;

	// ���벻�����Ƿ񱣴�
	protected boolean m_bBadBarcodeSave = false;

	// �����Ƿ񱣴�
	protected boolean m_bBarcodeSave = false;

	// �����������������ӣ��磺�ɹ������˿�
	protected boolean m_bFixBarcodeNegative = false;

	// ���ݹ�ʽ���� hanwei 2003-07-23
	BillFormulaContainer m_billFormulaContain;

	// �Ƿ��е������ hanwei 2003-12-17
	private boolean m_bIsImportData = false;

	// added by zhx ����༭����
	protected BarCodeDlg m_dlgBarCodeEdit = null;

	// ָ����λ
	protected LocSelDlg m_dlgLocSel = null;

	public GeneralBillUICtl m_GenBillUICtl;

	// ����༭�����ɫ�У�ÿX+1�е���ɫ��Ҫ����
	protected int m_iBarcodeUIColorRow = 20;

	// �ļ��򿪶Ի���
	private nc.ui.ic.pub.tools.FileChooserImpBarcode m_InFileDialog = null;

	// ������λ��Ϣ
	private boolean m_isLocated = false;

	// ��ҳ����
	protected PageCtrlBtn m_pageBtn;

	// zhx 030626 �����к�
	protected final String m_sBillRowNo = "crowno";

	// ����״̬
	protected String m_sBillStatus = nc.vo.ic.pub.bill.BillStatus.FREE;

	protected String m_sColorRow = null;

	// ���׼��Ի���
	protected SetPartDlg m_SetpartDlg = null;

	// ��������
	String m_sLastKey = null;

	// ʱ����ʾ
	protected nc.vo.ic.pub.bill.Timer m_timer = new nc.vo.ic.pub.bill.Timer();

	public nc.ui.ic.pub.barcode.UIBarCodeTextFieldNew m_utfBarCode = null;

	// ��������
	protected Vector m_vAuxiliary = null;

	// �˿�˵�
	protected Vector m_vboSendback = null;

	// ��������ά���˵�
	protected Vector m_vImportBarcodes = null;

	protected static final UFDouble UFDNEGATIVE = new UFDouble(-1.00); // ����-1.00

	public static final nc.vo.pub.lang.UFDouble UFDZERO = new nc.vo.pub.lang.UFDouble(0.0);

	/**
	 * ClientUI ������ע�⡣ nc 2.2 �ṩ�ĵ������鹦�ܹ����ӡ�
	 * 
	 */
	public GeneralBillClientUI(String pk_corp, String billType, String businessType, String operator, String billID) {
		super();
		// �鵥��
		GeneralBillVO voBill = qryBill(pk_corp, billType, businessType, operator, billID, null);
		// ��ʼ������
		m_alListData = new ArrayList();

		m_alListData.add(voBill);

		if (voBill == null) nc.ui.pub.beans.MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/* @res "��ʾ" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000062")/* @res "û�з��ϲ�ѯ�����ĵ��ݣ�" */);
		else {
			pk_corp = voBill.getHeaderVO().getPk_corp();
			initialize(pk_corp, operator, "jc", businessType, "0001", "2003-04-17");
			// ͨ�����ݹ�ʽ������ִ���йع�ʽ�����ķ���
			setListHeadData(new GeneralBillHeaderVO[] {
				voBill.getHeaderVO()
			});

			appendLocator(voBill);

			// ��ʾ����
			setBillVO(voBill);
		}

	}

	private QueryOnHandInfoPanel m_pnlQueryOnHand;// �ִ���Panel

	/**
	 * �����ִ�������Panel
	 * 
	 * @param iRow
	 */
	public QueryOnHandInfoPanel getPnlQueryOnHandPnl() {

		if (m_pnlQueryOnHand == null) {
			m_pnlQueryOnHand = new QueryOnHandInfoPanel(m_sUserID, m_sCorpID, getOnHandRefDeal(), true, getOnHandRefDeal(), true, true);
		}

		return m_pnlQueryOnHand;
	}

	protected OnHandRefCtrl m_onHandRefDeal = null;// �ִ������յĴ�������

	public OnHandRefCtrl getOnHandRefDeal() {
		if (m_onHandRefDeal == null) {
			m_onHandRefDeal = new OnHandRefCtrl(this);
		}
		return m_onHandRefDeal;
	}

	protected UIPanel m_pnlBarCode;

	/**
	 * ����ɨ��Panel,������m_bAddBarcodeFieldΪtrueʱ��ʾ
	 * 
	 * @return
	 */
	protected UIPanel getPnlBc() {
		if (m_pnlBarCode == null) {
			m_pnlBarCode = new UIPanel();
			try {

				UILabel lbName = new UILabel(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000063")/*
																															* @res
																															* "������������: "
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
	 * � ���ܣ����ⵥΪ��Դ���ݵ��������뵥�������ɱ༭�����������ܴ���Ӧ�շ����� ������ ���أ� /* 1.�����༭�����������༭�� 11
	 * �õ�Ӧ�շ�������NULL��Ϊ0 12 �õ�ʵ���շ��������Ƚ��������������ʵ��������ʵ�ʸ��������� ���ʵ�ʸ���������Ӧ�շ��������� ��ʾ
	 * 
	 * ���⣺ ���ڣ�(2005-1-28 14:27:22) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 */
	public void afterNumEditFromSpe(nc.ui.pub.bill.BillEditEvent e) {

		try {
			int iRow = e.getRow();
			// ��Դ���ݿ��ƣ�
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
																														* "ʵ���������ܴ���Ӧ�շ�������"
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
																														* "ʵ���������ܴ���Ӧ�շ�������"
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
	 * �����ߣ����˾� ���ܣ�����¼����� ������e���ݱ༭�¼� ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 */
	public void afterInvMutiEdit(nc.ui.pub.bill.BillEditEvent e) {
		nc.vo.scm.pub.ctrl.GenMsgCtrl.printHint("afterInvMutiEdit:" + e.getKey());
		long ITimeAll = System.currentTimeMillis();

		int row = e.getRow();
		// �ֶ�itemkey
		String sItemKey = e.getKey();
		m_voBill.setItemValue(row, "desainfo", null);
		nc.ui.pub.beans.UIRefPane invRef = (nc.ui.pub.beans.UIRefPane) getBillCardPanel().getBodyItem("cinventorycode").getComponent();
		// ������PK
		String[] refPks = invRef.getRefPKs();
		// �������Ϊ�գ���յ�ǰ����
		if (refPks == null || refPks.length == 0) {
			nc.vo.scm.pub.ctrl.GenMsgCtrl.printHint("���շ��صĴ������getRefPKs:0");
			clearRow(row);
			return;
		}
		invRef.setPK(null);

		nc.vo.scm.pub.ctrl.GenMsgCtrl.printHint("���շ��صĴ������getRefPKs:" + refPks.length);
		// �ֿ�Ϳ����֯��Ϣ
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

		SCMEnv.showTime(ITime, "���������������:");

		ITime = System.currentTimeMillis();
		// �������
		// DUYONG �˴���Ҫ��֤�Ƿ���Ҫ���мƻ��۵Ĵ���������֤���˴��Ѿ������˴���
		InvVO[] invVOs = getInvoInfoBYFormula().getInvParseWithPlanPrice(refPks, sWhID, sCalID, true, true);

		SCMEnv.showTime(ITime, "�������:");
		InvVO[] invvoBack = new InvVO[invVOs.length];
		for (int i = 0; i < invVOs.length; i++) {
			invVOs[i].setPk_corp(m_sCorpID);
			invvoBack[i] = (InvVO) invVOs[i].clone();
		}

		ITime = System.currentTimeMillis();
		// �������
		try {
			QueryInfo info = new QueryInfo();
			invVOs = info.dealPackType(invVOs);
		} catch (Exception e1) {
			nc.vo.scm.pub.SCMEnv.error(e1);
			invVOs = invvoBack;
		}

		// �����ǰ�е�����VO,��������������ر�־
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
		SCMEnv.showTime(ITimeAll, "������ն�ѡ:");

	}

	/**
	 * ����¼��: v5֧�ּ��вɹ�, �˷������ɹ���⸲��
	 * 
	 * @param istartrow
	 *            ���п�ʼ�ֶ�
	 * @param count
	 *            ��������
	 */
	public void setBodyDefaultData(int istartrow, int count) {

	}

	/**
	 * �˴����뷽��˵���� ������ն�ѡ�����ý��� ���ڴ����ѡ������ɨ�����ֳ��� �������ڣ�(2004-5-7 12:40:43)
	 * 
	 * @param invVOs
	 *            nc.vo.scm.ic.bill.InvVO[]�����VO iRow����ǰ��
	 *            sItemKey����ǰ�е�Key:"cinventorycode"
	 */
	public void afterInvMutiEditSetUI(InvVO[] invVOs, int iRow, String sItemKey) {
		nc.vo.scm.pub.ctrl.GenMsgCtrl.printHint("afterInvMutiEditSetUI:" + sItemKey + "������" + invVOs.length);

		// ��������
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

		// v5 lj ���вɹ�����¼��
		setBodyDefaultData(iRow, iLen);

		// ���ý�������
		boolean bHasSourceBillTypecode = false;
		if (getSourBillTypeCode() == null || getSourBillTypeCode().trim().length() == 0) {
			bHasSourceBillTypecode = false;
		} else bHasSourceBillTypecode = true;

		int iCurRow = 0;

		getBillCardPanel().getBillModel().setNeedCalculate(false);
		// zhy2005-08-24��ͷ��Ӧ��Ӧд������
		String sHeadProviderName = null;
		String sHeadProviderID = null;
		String sPk_cusbasdoc = null;
		UIRefPane ref = null;
		if (getBillCardPanel().getHeadItem("cproviderid") != null) ref = (nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("cproviderid").getComponent();
		if (ref != null) {
			sHeadProviderName = ref.getRefName();
			sHeadProviderID = ref.getRefPK();
		}

		// ��õ���ͷ�����еĹ�Ӧ�̻�������ID
		BillItem iPk_cubasdoc = getBillCardPanel().getHeadItem("pk_cubasdoc");
		Object oPk_cubasdoc = null;
		if (iPk_cubasdoc != null) oPk_cubasdoc = getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject();

		for (int i = 0; i < iLen; i++) {
			iCurRow = iRow + i;
			// ������/�����������
			// ������ڡ�m_voBill.setItemInv������֮ǰ add by ydy 2003-12-17.01
			clearRowData(0, iCurRow, sItemKey);
			// ����
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
				// ��Ӧ����
				// ͬ��vo
				if (m_voBill != null) {
					m_voBill.setItemValue(iCurRow, m_sShouldNumItemKey, null);
					m_voBill.setItemValue(iCurRow, m_sShouldAstItemKey, null);
				}
				if (getBillCardPanel().getBodyItem(m_sShouldNumItemKey) != null) getBillCardPanel().setBodyValueAt(null, iCurRow, m_sShouldNumItemKey);
				if (getBillCardPanel().getBodyItem(m_sShouldAstItemKey) != null) getBillCardPanel().setBodyValueAt(null, iCurRow, m_sShouldAstItemKey);
			}
		}
		// ���õ�ǰ�����к��Ƿ����
		getBillCardPanel().getBillModel().setNeedCalculate(true);
		getBillCardPanel().getBillModel().reCalcurateAll();

		setTailValue(iRow);
		setBtnStatusSN(iRow, true);

		// ���õ�ǰѡ���е��ִ�������
		if (m_bOnhandShowHidden) showOnHandPnlInfo(iRow);
		// ��̬���ý���
		getOnHandRefDeal().setInvCtrlValue(iRow);
		setCardMode();
		m_layoutManager.show();

	}

	/**
	 *  
	 */
	public void setCardMode() {
		// ����ִ�����ʾ��������ʾ״̬����ǰ��Ƭ״̬����ΪMultiCardMode.CARD_TAB
		if (m_bOnhandShowHidden) {
			m_sMultiMode = MultiCardMode.CARD_TAB;
		} else {
			m_sMultiMode = MultiCardMode.CARD_PURE;
		}
	}

	/**
	 * ��ʾ�ִ������յ�����
	 * 
	 * @param iRow
	 */
	protected void showOnHandPnlInfo(int iRow) {
		if (m_pnlQueryOnHand == null || iRow < 0) return;

		m_pnlQueryOnHand.setVORefresh(getOnHandRefDeal().getSelectedItemHandInfo(iRow));

		m_pnlQueryOnHand.showData();

	}

	/**
	 * �����ߣ����˾� ���ܣ�ɾ������ ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
					/** �жϵ��������Ƿ�Ϊ�ⲿ���� */
					// û����Դ���ݣ��������״̬�£����ƿ���
					if (sSourceBillType != null && sSourceBillType.startsWith("4")) {
						// �ڲ����ɵ���
						/** �жϵ������ͷ�Ϊת�ⵥ */
						if (sSourceBillType != null && (sSourceBillType.equals(BillTypeConst.m_transfer) || sSourceBillType.equals(BillTypeConst.m_assembly) || sSourceBillType.equals(BillTypeConst.m_disassembly) || sSourceBillType.equals(BillTypeConst.m_transform) || sSourceBillType.equals(BillTypeConst.m_check) || sSourceBillType.equals(nc.vo.ic.pub.BillTypeConst.m_AllocationOrder))) {

							/**
							 * �����������״̬ʱ�����ư�ť������ ����װ����ж����̬ת�����̵����ⵥ�����ɵ������롢
							 * ����������ֱ��ɾ������ɾ��ʱ��ʾ�û�ͨ���������ⵥʵ�֡����ԣ���ɾ����ť�ûҡ�
							 */

							String[] args = new String[1];
							args[0] = gvo.getHeaderValue("vbillcode").toString();
							String message = nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000340", null, args);
							/*
							 * @res
							 * "���ݺ��ǣ�{0}�ĵ���������װ����ж����̬ת�����̵����ⵥ�����ɵ�������,����������ֱ��ɾ����ͨ���������ⵥʵ��!"
							 */

							if (!(sSourceBillType.equals(nc.vo.ic.pub.BillTypeConst.m_transfer) || sSourceBillType.equals(nc.vo.ic.pub.BillTypeConst.m_AllocationOrder))) {
								sError += "(" + message + ")";
							}

						} else if (sSourceBillType != null && (sSourceBillType.equals(BillTypeConst.m_saleOut) || sSourceBillType.equals(BillTypeConst.m_purchaseIn))) {
							// ���۳��ⵥ���ɹ���ⵥ�������ɵ���������������ⵥ�ݲ���ɾ���޸ĵ��ݣ�����
							String[] args1 = new String[1];
							args1[0] = gvo.getHeaderValue("vbillcode").toString();
							String message1 = nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000341", null, args1);
							/*
							 * @res
							 * "���ݺ��ǣ�{0}�ĵ����������۳��ⵥ���ɹ���ⵥ�������ɵ���������������ⵥ�ݲ���ɾ���޸ĵ���!"
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
	 * ���ߣ�� �ж�ѡ����(����Excel���룩�Ƿ������ݣ��Ƿ�ѡ�С� �������ڣ�(2004-5-4 13:12:20)
	 */
	private int checkSelectionRow() {
		if (m_iCurPanel == BillMode.Card) {
			if (m_iLastSelListHeadRow != -1 && null != m_alListData && m_alListData.size() != 0) {
				if (m_voBill == null) m_voBill = (GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow);
			}
		} else if (m_iCurPanel == BillMode.List) { // �б�
			showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000065")/* @res "���л�����Ƭ���浼������!" */);
			return -1;
		}
		int rownow = getBillCardPanel().getBillTable().getSelectedRow();
		if (rownow < 0) { return -1; }
		String invID = (String) getBillCardPanel().getBodyValueAt(rownow, "cinventoryid");
		if (invID == null || invID.trim().equals("")) {
			showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000066")/* @res "û��ѡ�д��!" */);
			return -1;
		}
		if (m_voBill == null || m_voBill.getChildrenVO() == null || m_voBill.getChildrenVO().length < rownow) {
			showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH003")/* @res "��ѡ��Ҫ��������ݣ�" */);
			return -1;
		}
		return rownow;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-10-09 16:34:45)
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
		// throw new Exception("������������Ļ���ҵ�����͵ĵ��ݱ�������Ĳֲֿ�!");
		// }
		// } else if (verifyrule != null && !verifyrule.equals("V")) {
		// if (voBill.getWh().getIsforeignstor() != null
		// && voBill.getWh().getIsforeignstor().booleanValue()
		// && voBill.getWh().getIsgathersettle().booleanValue()) {
		// throw new Exception("������������Ļ���ҵ�����͵ĵ��ݲ�������Ĳֲֿ�!");
		// }
		// }

		// }

		// }
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-11-7 15:13:21)
	 * 
	 * @param row
	 *            int
	 */
	public void clearRow(int row) {
		clearRowData(row);
		// ��Ӧ����
		// ͬ��vo
		if (m_voBill != null) {
			m_voBill.setItemValue(row, m_sShouldNumItemKey, null);
			m_voBill.setItemValue(row, m_sShouldAstItemKey, null);
		}
		if (getBillCardPanel().getBodyItem(m_sShouldNumItemKey) != null) getBillCardPanel().setBodyValueAt(null, row, m_sShouldNumItemKey);
		if (getBillCardPanel().getBodyItem(m_sShouldAstItemKey) != null) getBillCardPanel().setBodyValueAt(null, row, m_sShouldAstItemKey);
		// ��β
		setTailValue(null);
	}

	/**
	 * ��Դ������ת�ⵥʱ�Ľ�����Ʒ��� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2001-10-19 09:43:22)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	protected void ctrlSourceBillButtons(boolean bUpdateButtons) {
		String sSourceBillType = getSourBillTypeCode();
		/** �жϵ��������Ƿ�Ϊ�ⲿ���� */
		// û����Դ���ݣ��������״̬�£����ƿ���
		if ((sSourceBillType == null || sSourceBillType.trim().length() == 0)) {
			if (m_iMode == BillMode.Browse && m_iBillQty > 0 && m_iLastSelListHeadRow >= 0) {
				// ���״̬�¿��Ը��Ƶ���
				getButtonTree().getButton(ICButtonConst.BTN_BILL_COPY).setEnabled(true);
			}
		} else if (!sSourceBillType.startsWith("4")) {
			// �ⲿ���ɵĵ���,���Ѿ��������״���

			// �޸ġ�����״̬�¡�
			if (m_iMode == BillMode.Update || m_iMode == BillMode.New) {

				/** �õ����У������Ҽ���ť,���У�������Ϊ������,���ƣ�ճ������ */

				getButtonTree().getButton(ICButtonConst.BTN_BILL_COPY).setEnabled(false);
				getButtonTree().getButton(ICButtonConst.BTN_LINE_ADD).setEnabled(false);
				getButtonTree().getButton(ICButtonConst.BTN_LINE_INSERT).setEnabled(false);

				// �����Ҽ�������
				getBillCardPanel().getBodyMenuItems()[0].setEnabled(false);
				// �����Ҽ�������
				getBillCardPanel().getBodyMenuItems()[2].setEnabled(false);
				// �����Ҽ�ɾ���У�ǿ����Ϊtrue; ��Bug�����Ѳ������ûң�����ǰ���ɾ����Ҳ���ûҡ�
				getBillCardPanel().getBodyMenuItems()[1].setEnabled(true);
			} else
			// ���״̬��ֻ�ǲ��ø��Ƶ���
			getButtonTree().getButton(ICButtonConst.BTN_BILL_COPY).setEnabled(false);
			// boolean bisdispensebill = isDispensedBill(null);
			// if (bisdispensebill) {
			// getButtonTree().getButton(ICButtonConst.BTN_BILL_DELETE).setEnabled(false);
			// getButtonTree().getButton(ICButtonConst.BTN_BILL_COPY).setEnabled(false);
			// getButtonTree().getButton(ICButtonConst.BTN_BILL_EDIT).setEnabled(false);

			// }
		} else {
			// �ڲ����ɵ���
			/** �жϵ������ͷ�Ϊת�ⵥ */
			if (sSourceBillType != null && (sSourceBillType.equals(BillTypeConst.m_transfer) || sSourceBillType.equals(BillTypeConst.m_assembly) || sSourceBillType.equals(BillTypeConst.m_disassembly) || sSourceBillType.equals(BillTypeConst.m_transform) || sSourceBillType.equals(BillTypeConst.m_check) || sSourceBillType.equals(nc.vo.ic.pub.BillTypeConst.m_AllocationOrder))) {
				if (m_iMode == BillMode.Update || m_iMode == BillMode.New) {
					/** ���������������޸�״̬ʱ�Ľ�����Ϊ���ơ� */
					/** �õ����У������Ҽ���ť,ɾ�У����У�������Ϊ������,���ƣ�ճ������ */

					/** �ò˵���ť�Ŀ���״̬ */
					getButtonTree().getButton(ICButtonConst.BTN_LINE).setEnabled(false);
					/** �ñ����Ҽ��˵���ť�Ŀ���״̬ */
					getBillCardPanel().setBodyMenuShow(false);

				}
				/**
				 * �����������״̬ʱ�����ư�ť������ ����װ����ж����̬ת�����̵����ⵥ�����ɵ������롢
				 * ����������ֱ��ɾ������ɾ��ʱ��ʾ�û�ͨ���������ⵥʵ�֡����ԣ���ɾ����ť�ûҡ�
				 */
				else {
					if (!(sSourceBillType.equals(nc.vo.ic.pub.BillTypeConst.m_transfer) || sSourceBillType.equals(nc.vo.ic.pub.BillTypeConst.m_AllocationOrder))) {
						getButtonTree().getButton(ICButtonConst.BTN_BILL_DELETE).setEnabled(false);
					} else {
						// ǩ�ֵĵ��ݲ���ɾ����
						if (CANNOTSIGN == isSigned() || NOTSIGNED == isSigned()) getButtonTree().getButton(ICButtonConst.BTN_BILL_DELETE).setEnabled(true);
					}
					getButtonTree().getButton(ICButtonConst.BTN_BILL_COPY).setEnabled(false);
				}

			} else if (sSourceBillType != null && (sSourceBillType.equals(BillTypeConst.m_saleOut) || sSourceBillType.equals(BillTypeConst.m_purchaseIn))) {
				// ���۳��ⵥ���ɹ���ⵥ�������ɵ���������������ⵥ�ݲ���ɾ���޸ĵ��ݣ�����
				getButtonTree().getButton(ICButtonConst.BTN_BILL_DELETE).setEnabled(false);
				getButtonTree().getButton(ICButtonConst.BTN_BILL_COPY).setEnabled(false);
				getButtonTree().getButton(ICButtonConst.BTN_BILL_EDIT).setEnabled(false);

			}
			// �����������������Ĳ�������ǡ�ϵͳ�������ģ���ô���ܸ��ƣ�ɾ�����޸�
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
		// //�Ѿ��������׵ĵ��ݲ��ܱ�ɾ�����޸ĺ͸��ơ�
		boolean bisdispensebill = isDispensedBill(null);
		if (bisdispensebill) {
			getButtonTree().getButton(ICButtonConst.BTN_BILL_DELETE).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_BILL_COPY).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_BILL_EDIT).setEnabled(false);

		}

		// ###########################
		// ���� add by hanwei 2004-05-14
		// 1��������ⵥ�����ֱ�ӵ�ת��־����Ҫ�ڽ������ �޸ġ�ɾ�������ƵȰ�ť�����ã�
		// 2��������ݵĲֿ���ֱ�˲֣�Ӧ�ÿ����޸ġ�ɾ�������ƵȰ�ť�����ã�
		// ����true:ֻ����false������
		setBtnStatusTranflag(true);
		// #############################

		// ������õ�ˢ�½���Ĳ˵���ť
		if (bUpdateButtons) updateButtons();

		// v5 lj
		if (m_bRefBills == true) {
			setRefBtnStatus();
		}
	}

	/**
	 * ��Դ������ת�ⵥʱ�Ľ�����Ʒ��� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2001-10-19 09:43:22)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	protected void ctrlSourceBillUI(boolean bUpdateButtons) {
		try {
			String sSourceBillType = getSourBillTypeCode();
			/** �жϵ��������Ƿ�Ϊ�ⲿ���� */
			// û����Դ���ݣ��������״̬�£����ƿ���
			if ((sSourceBillType == null || sSourceBillType.trim().length() == 0)) {
				if (m_iMode == BillMode.Browse && m_iBillQty > 0 && m_iLastSelListHeadRow >= 0) {
					// ���״̬�¿��Ը��Ƶ���
					getButtonTree().getButton(ICButtonConst.BTN_BILL_COPY).setEnabled(true);
				}
			} else if (!sSourceBillType.startsWith("4")) {
				// �ⲿ���ɵĵ���

				// �޸ġ�����״̬�¡�
				if (m_iMode == BillMode.Update || m_iMode == BillMode.New) {

					/** �ñ�ͷ���ɱ༭�� */
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
					/** �õ����У������Ҽ���ť,���У�������Ϊ������,���ƣ�ճ������ */

					getButtonTree().getButton(ICButtonConst.BTN_BILL_COPY).setEnabled(false);
					getButtonTree().getButton(ICButtonConst.BTN_LINE_ADD).setEnabled(false);
					getButtonTree().getButton(ICButtonConst.BTN_LINE_INSERT).setEnabled(false);

					// �����Ҽ�������
					getBillCardPanel().getBodyMenuItems()[0].setEnabled(false);
					// �����Ҽ�������
					getBillCardPanel().getBodyMenuItems()[2].setEnabled(false);
					// �����Ҽ�ɾ���У�ǿ����Ϊtrue; ��Bug�����Ѳ������ûң�����ǰ���ɾ����Ҳ���ûҡ�
					getBillCardPanel().getBodyMenuItems()[1].setEnabled(true);
				} else
				// ���״̬��ֻ�ǲ��ø��Ƶ���
				getButtonTree().getButton(ICButtonConst.BTN_BILL_COPY).setEnabled(false);

			} else {
				// �ڲ����ɵ���,���ⵥ�ݣ����۳��⣬�ɹ������������ɵ������룬����
				/** �жϵ������ͷ�Ϊת�ⵥ */
				if (sSourceBillType != null && (sSourceBillType.equals(BillTypeConst.m_transfer) || sSourceBillType.equals(BillTypeConst.m_assembly) || sSourceBillType.equals(BillTypeConst.m_disassembly) || sSourceBillType.equals(BillTypeConst.m_transform) || sSourceBillType.equals(BillTypeConst.m_check) || sSourceBillType.equals(BillTypeConst.m_saleOut) || sSourceBillType.equals(BillTypeConst.m_purchaseIn))) {
					if (m_iMode == BillMode.Update || m_iMode == BillMode.New) {
						/** ���������������޸�״̬ʱ�Ľ�����Ϊ���ơ� */
						/** �õ����У������Ҽ���ť,ɾ�У����У�������Ϊ������,���ƣ�ճ������ */

						/** �ò˵���ť�Ŀ���״̬ */
						getButtonTree().getButton(ICButtonConst.BTN_LINE).setEnabled(false);
						/** �ñ����Ҽ��˵���ť�Ŀ���״̬ */
						getBillCardPanel().setBodyMenuShow(false);
						String sHeadItemKey = null;
						// �������۳��ⵥ���ɵ��������ⵥ����ͷ�ͻ����ɱ༭
						if (sSourceBillType.equals(BillTypeConst.m_saleOut) && m_sBillTypeCode.equals(BillTypeConst.m_otherOut)) {
							sHeadItemKey = "ccustomerid";

						}
						// ���ײɹ���ⵥ���ɵ�������ⵥ����ͷ�ͻ����ɱ༭
						else if (sSourceBillType.equals(BillTypeConst.m_purchaseIn) && m_sBillTypeCode.equals(BillTypeConst.m_otherIn)) {
							sHeadItemKey = "cproviderid";
						}

						/** �ñ�ͷ���ɱ༭�� */
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
					 * �����������״̬ʱ�����ư�ť������ ����װ����ж����̬ת�����̵����ⵥ�����ɵ������롢
					 * ����������ֱ��ɾ������ɾ��ʱ��ʾ�û�ͨ���������ⵥʵ�֡����ԣ���ɾ����ť�ûҡ�
					 */
					else {
						if (!(sSourceBillType.equals(nc.vo.ic.pub.BillTypeConst.m_transfer) || sSourceBillType.equals(nc.vo.ic.pub.BillTypeConst.m_AllocationOrder))) {
							getButtonTree().getButton(ICButtonConst.BTN_BILL_DELETE).setEnabled(false);
						} else {
							// ǩ�ֵĵ��ݲ���ɾ����
							int iIsSigned = isSigned();
							if (iIsSigned != SIGNED) getButtonTree().getButton(ICButtonConst.BTN_BILL_DELETE).setEnabled(true);

						}
						getButtonTree().getButton(ICButtonConst.BTN_BILL_COPY).setEnabled(false);
					}
				} else if (sSourceBillType != null && (sSourceBillType.equals(BillTypeConst.m_saleOut) || sSourceBillType.equals(BillTypeConst.m_purchaseIn))) {
					// ���۳��ⵥ,�ɹ���⵫�������ɵ���������������ⵥ�ݲ���ɾ���޸ĵ��ݣ�����
					getButtonTree().getButton(ICButtonConst.BTN_BILL_DELETE).setEnabled(false);
					getButtonTree().getButton(ICButtonConst.BTN_BILL_COPY).setEnabled(false);
					getButtonTree().getButton(ICButtonConst.BTN_BILL_EDIT).setEnabled(false);

				} else if (sSourceBillType != null && (sSourceBillType.equals(BillTypeConst.m_AllocationOrder))) {
					// ��Դ�����ǵ��������Ľ�����ƣ�

					/** ���������������޸�״̬ʱ�Ľ�����Ϊ���ơ� */
					/** �õ����У������Ҽ���ť,ɾ�У����У�������Ϊ������,���ƣ�ճ������ */

					/** �ò˵���ť�Ŀ���״̬ */
					getButtonTree().getButton(ICButtonConst.BTN_LINE).setEnabled(false);
					/** �ñ����Ҽ��˵���ť�Ŀ���״̬ */
					getBillCardPanel().setBodyMenuShow(false);

					/** �ñ�ͷ�����֯���ɱ༭�� */
					String saNotEditableHeadKey2 = m_sMainCalbodyItemKey;

					if (getBillCardPanel().getBillData().getHeadItem(saNotEditableHeadKey2) != null) getBillCardPanel().getBillData().getHeadItem(saNotEditableHeadKey2).setEnabled(false);

				}

			} // ������õ�ˢ�½���Ĳ˵���ť

			// ###########################
			// ���� add by hanwei 2004-05-14
			// 1��������ⵥ�����ֱ�ӵ�ת��־����Ҫ�ڽ������ �޸ġ�ɾ�������ƵȰ�ť�����ã�
			// 2��������ݵĲֿ���ֱ�˲֣�Ӧ�ÿ����޸ġ�ɾ�������ƵȰ�ť�����ã�
			// ����true:ֻ����false������
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
	 * �˴����뷽��˵���� ��������:�����ѯʱû�в鵽���ݵ�����½���ġ� ���ߣ������� �������: ����ֵ: �쳣����: ����:(2003-6-9
	 * 15:57:49)
	 */
	protected void dealNoData() {
		// ���õ�ǰ�ĵ�������/��ţ����ڰ�ť����
		setLastHeadRow(-1);
		// ��ʼ����ǰ������ţ��л�ʱ�õ���
		m_iCurDispBillNum = -1;
		// ��ǰ������
		m_iBillQty = 0;
		m_alListData = null;
		clearUi();
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000013")/* @res "δ�鵽���������ĵ��ݡ�" */);
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2002-11-27 10:41:27)
	 * 
	 * @param error
	 *            java.lang.String
	 */
	public void errormessageshow(java.lang.String error) {
		// ��ʾ������Ϣ
		java.awt.Toolkit.getDefaultToolkit().beep();
		showErrorMessage(error);
		// ���ر༭��
		m_utfBarCode.requestFocus();
	}

	/**
	 * ���ù�ʽ ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2001-11-12 16:47:04) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	private String execFormular(String formula, String value) {
		nc.ui.pub.formulaparse.FormulaParse f = new nc.ui.pub.formulaparse.FormulaParse();

		if (formula != null && !formula.equals("")) {
			// ���ñ��ʽ
			f.setExpress(formula);
			// ��ñ���
			nc.vo.pub.formulaset.VarryVO varry = f.getVarry();
			// ��������ֵ
			Hashtable h = new Hashtable();
			for (int j = 0; j < varry.getVarry().length; j++) {
				String key = varry.getVarry()[j];

				String[] vs = new String[1];
				vs[0] = value;
				h.put(key, StringUtil.toString(vs));
			}

			f.setDataS(h);
			// ���ý��
			if (varry.getFormulaName() != null && !varry.getFormulaName().trim().equals("")) return f.getValueS()[0];
			else return f.getValueS()[0];

		} else {
			return null;
		}
	}

	/**
	 * �˴����뷽��˵���� ����������ر�����,ʵ�ַ�Ʒ����ճ�ʼ�� RefFilter.filtWasteWh(billItem, sCorpID,
	 * null);
	 * 
	 * �������ڣ�(2004-3-19 17:41:50)
	 */
	public void filterRefofWareshouse(nc.ui.pub.bill.BillItem billItem, String sCorpID) {

		RefFilter.filtWh(billItem, sCorpID, null);

	}

	/**
	 * �˴����뷽��˵���� ���Ȩ����֤UI �������ڣ�(2004-4-19 14:11:06)
	 * 
	 * @return nc.ui.scm.pub.AccreditLoginDialog
	 */
	public nc.ui.scm.pub.AccreditLoginDialog getAccreditLoginDialog() {
		if (m_AccreditLoginDialog == null) m_AccreditLoginDialog = new AccreditLoginDialog();
		return m_AccreditLoginDialog;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-5-7 14:18:22)
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
	 * �����ߣ������� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2002-1-24 11:35:23) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @return boolean
	 */
	/**
	 * ���� BillCardPanel1 ����ֵ��
	 * 
	 * @return nc.ui.pub.bill.BillCardPanel
	 */
	/* ���棺�˷������������ɡ� */
	public BarCodeDlg getBarCodeDlg() {
		m_dlgBarCodeEdit = new BarCodeDlg(this, m_sCorpID);
		return m_dlgBarCodeEdit;
	}

	/**
	 * �˴����뷽��˵���� ���������Condition�ĸ��Ի��޸����� �����ط��� �������ڣ�(2003-11-25 20:58:54)
	 */
	protected void getConDlginitself(QueryConditionDlgForBill queryCondition) {
	}

	/**
	 * �˴����뷽��˵���� ���ܣ��õ���׼�ļ��Ի��� ��������׼�ļ��Ի��� ���أ��� ���⣺ ���ڣ�(2002-9-24 15:47:21)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	public UIFileChooser getFileChooseDlg() {
		try {
			// m_InFileDialog = null;
			if (m_InFileDialog == null) {
				m_InFileDialog = new nc.ui.ic.pub.tools.FileChooserImpBarcode();
				m_InFileDialog.setDialogType(UIFileChooser.OPEN_DIALOG);
				// m_InFileDialog.setFileSelectionMode(UIFileChooser.FILES_ONLY);
				m_InFileDialog.removeChoosableFileFilter(m_InFileDialog.getFileFilter());
				// ��ȥ��ǰ���ļ�������
				// m_InFileDialog.addChoosableFileFilter(new
				// nc.ui.pf.export.SuffixFilter());
				// ����ļ�ѡ�������

				m_InFileDialog.setCurrentDirectory(new java.io.File("D:\\"));
				m_InFileDialog.setFileFilter(new javax.swing.filechooser.FileFilter() {
					public boolean accept(java.io.File f) {
						return f.isDirectory() || f.getName().toLowerCase().endsWith(".xls");
					}

					public String getDescription() {
						return nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000495")/*
																											* @res
																											* "Excel�ļ�"
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
	 * �˴����뷽��˵���� ��������:��� BillFormulaContainer ���ߣ����� �������: ����ֵ: �쳣����:
	 * ����:(2003-7-2 9:48:12)
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
	 * �˴����뷽��˵���� ��������: ���ߣ����˾� �������: ����ֵ: �쳣����: ����:(2003-6-25 20:43:17)
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

		// ��Ŀ��������ID
		String[] aryItemField11 = new String[] {
				"pk_jobbasfil", "pk_jobbasfil", "cprojectid"
		};
		arylistItemField.add(aryItemField11);

		// ��Ŀ����
		String[] aryItemField12 = new String[] {
				"jobname", "cprojectname", "pk_jobbasfil"
		};
		arylistItemField.add(aryItemField12);

		// ��Ŀ�׶λ�������ID
		String[] aryItemField13 = new String[] {
				"pk_jobphase", "pk_jobphase", "cprojectphaseid"
		};
		arylistItemField.add(aryItemField13);

		// ��Ŀ�׶�����
		String[] aryItemField14 = new String[] {
				"jobphasename", "cprojectphasename", "pk_jobphase"
		};
		arylistItemField.add(aryItemField14);

		// ����������λ
		String[] aryItemField31 = new String[] {
				"measname", "castunitname", "castunitid"
		};
		arylistItemField.add(aryItemField31);

		// ���̻������� for ��Ӧ��
		String[] aryItemField7 = new String[] {
				"pk_cubasdoc", "pk_cubasdoc", "cvendorid"
		};
		arylistItemField.add(aryItemField7);

		// �������� for ��Ӧ��
		String[] aryItemField8 = new String[] {
				sCusterNameFields, "vvendorname", "pk_cubasdoc"
		};
		arylistItemField.add(aryItemField8);

		// zhy2005-09-16�������� for ��Ӧ��
		String[] aryItemField88 = new String[] {
				sCusterNameFields, "cvendorname", "pk_cubasdoc"
		};
		arylistItemField.add(aryItemField88);

		// ���̻������� for �ͻ�||�ջ���λ
		String[] aryItemField17 = new String[] {
				"pk_cubasdoc", "pk_cubasdocrev", "creceieveid"
		};
		arylistItemField.add(aryItemField17);

		// �������� for �ͻ�||�ջ���λ
		String[] aryItemField18 = new String[] {
				sCusterNameFields, "vrevcustname", "pk_cubasdocrev"
		};
		arylistItemField.add(aryItemField18);

		// ��Դ��������
		String[] aryItemField9 = new String[] {
				"billtypename", "csourcetypename", "csourcetype"
		};
		arylistItemField.add(aryItemField9);

		// �ɱ����� ��������
		// ccostobjectbasid
		String[] aryItemField34 = new String[] {
				"pk_invbasdoc", "ccostobjectbasid", "ccostobject"
		};
		arylistItemField.add(aryItemField34);
		// �ɱ�����
		String[] aryItemField33 = new String[] {
				"invname", "ccostobjectname", "ccostobjectbasid"
		};
		arylistItemField.add(aryItemField33);

		// Դͷ���ݺ�
		String[] aryItemField10 = new String[] {
				"billtypename", "cfirsttypename", "cfirsttype"
		};
		arylistItemField.add(aryItemField10);
		// //����
		String[] aryItemField19 = new String[] {
				"deptname", "vdeptname", "cdptid"
		};
		arylistItemField.add(aryItemField19);

		// ��λ
		String[] aryItemField20 = new String[] {
				"csname", "vspacename", "cspaceid"
		};
		arylistItemField.add(aryItemField20);

		// ��Ӧ��ⵥ���� ccorrespondtype
		// String[] aryItemField20 =
		// new String[] { "billtypename", "cfirsttypeName", "cfirsttype" };
		// arylistItemField.add(aryItemField20);
		return arylistItemField;
	}

	/**
	 * �˴����뷽��˵���� ��������: ���ߣ����˾� �������: ����ֵ: �쳣����: ����:(2003-6-25 20:43:17)
	 * 
	 * @return java.util.ArrayList
	 */
	protected ArrayList getFormulaItemHeader() {
		ArrayList arylistItemField = new ArrayList();
		// ԭ�еĹ�ʽ
		// �����֯ 1
		String[] aryItemField40 = new String[] {
				"bodyname", "vcalbodyname", "pk_calbody"
		};
		arylistItemField.add(aryItemField40);

		// �ⷿ����Ա 2
		String[] aryItemField3 = new String[] {
				"psnname", "cwhsmanagername", "cwhsmanagerid"
		};
		arylistItemField.add(aryItemField3);

		// �ֿ� 3
		String[] aryItemField15 = new String[] {
				"storname", "cwarehousename", "cwarehouseid"
		};
		arylistItemField.add(aryItemField15);

		// //�ֿ��Ƿ��Ƿ�ֱ�˲ֿ�
		String[] aryItemField41 = new String[] {
				"isdirectstore", "isdirectstore", "cwarehouseid"
		};
		arylistItemField.add(aryItemField41);

		// �ֿ� 3
		String[] aryItemField25 = new String[] {
				"storname", "cwastewarehousename", "cwastewarehouseid"
		};
		arylistItemField.add(aryItemField25);

		// ������ 4
		String[] aryItemField2 = new String[] {
				"user_name", "cregistername", "cregister"
		};
		arylistItemField.add(aryItemField2);

		// //������ 5
		String[] aryItemField12 = new String[] {
				"user_name", "cauditorname", "cauditorid"
		};
		arylistItemField.add(aryItemField12);

		// //����Ա 6
		String[] aryItemField1 = new String[] {
				"user_name", "coperatorname", "coperatorid"
		};
		arylistItemField.add(aryItemField1);

		// ���� 7
		String[] aryItemField19 = new String[] {
				"deptname", "cdptname", "cdptid"
		};
		arylistItemField.add(aryItemField19);

		// ҵ��Ա 8
		String[] aryItemField13 = new String[] {
				"psnname", "cbizname", "cbizid"
		};
		arylistItemField.add(aryItemField13);

		// ���̻������� for ��Ӧ�� 9
		String[] aryItemField7 = new String[] {
				"pk_cubasdoc", "pk_cubasdoc", "cproviderid"
		};
		arylistItemField.add(aryItemField7);

		// �������� for ��Ӧ�� 9
		String[] aryItemField8 = new String[] {
				"custname", "cprovidername", "pk_cubasdoc"
		};
		arylistItemField.add(aryItemField8);
		// �������� for ��Ӧ�̼��
		String[] aryItemField81 = new String[] {
				"custshortname", "cprovidershortname", "pk_cubasdoc"
		};
		arylistItemField.add(aryItemField81);

		// ���̻������� for �ͻ� 10
		String[] aryItemField5 = new String[] {
				"pk_cubasdoc", "pk_cubasdocC", "ccustomerid"
		};
		arylistItemField.add(aryItemField5);

		// �������� for �ͻ� 10
		String[] aryItemField6 = new String[] {
				"custname", "ccustomername", "pk_cubasdocC"
		};
		arylistItemField.add(aryItemField6);
		// �������� for �ͻ����
		String[] aryItemField61 = new String[] {
				"custshortname", "ccustomershortname", "pk_cubasdocC"
		};
		arylistItemField.add(aryItemField61);

		// //�շ����� 11
		String[] aryItemField18 = new String[] {
				"rdname", "cdispatchername", "cdispatcherid"
		};
		arylistItemField.add(aryItemField18);

		// ҵ������ 12
		String[] aryItemField17 = new String[] {
				"businame", "cbiztypename", "cbiztype"
		};
		arylistItemField.add(aryItemField17);

		// �����ӵĹ�ʽ
		// //���˷�ʽ 13
		String[] aryItemField42 = new String[] {
				"sendname", "cdilivertypename", "cdilivertypeid"
		};
		arylistItemField.add(aryItemField42);

		// for ���ϼӹ���ⵥ���ӹ�Ʒ
		// ��������
		// pk_invbasdoc 14
		String[] aryItemField20 = new String[] {
				"pk_invbasdoc", "pk_invbasdoc", "cinventoryid"
		};
		arylistItemField.add(aryItemField20);

		// ���� 14
		String[] aryItemField21 = new String[] {
				"invname", "cinventoryname", "pk_invbasdoc"
		};
		arylistItemField.add(aryItemField21);

		return arylistItemField;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-4-8 11:13:07)
	 * 
	 * @return nc.ui.ic.pub.bill.GeneralBillUICtl
	 */
	public GeneralBillUICtl getGenBillUICtl() {
		if (m_GenBillUICtl == null) m_GenBillUICtl = new GeneralBillUICtl();
		return m_GenBillUICtl;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-7-11 ���� 11:19)
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
	 * �˴����뷽��˵���� ��������: ���ݲ�ѯ��������; ���롢������ݿ������¹���÷���,����������onquery���� �������: ����ֵ:
	 * �쳣����: ����:
	 * 
	 * @return nc.vo.ic.pub.bill.QryConditionVO
	 */
	protected QryConditionVO getQryConditionVO() {
		// ��Ӳ�ѯ
		nc.vo.pub.query.ConditionVO[] voaCond = getConditionDlg().getConditionVO();
		// ����繫˾����ҵ��Ա����
		voaCond = nc.ui.ic.pub.tools.GenMethod.procMultCorpDeptBizDP(voaCond, getBillTypeCode(), getCorpPrimaryKey());
		// ����nullΪ is null �� is not null add by hanwei 2004-03-31.01
		nc.ui.ic.pub.report.IcBaseReportComm.fixContionVONullsql(voaCond);
		QryConditionVO voCond = new QryConditionVO(" head.cbilltypecode='" + m_sBillTypeCode + "' AND " + getExtendQryCond(voaCond));

		return voCond;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-3-12 21:25:15)
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
	 * �򵥳�ʼ���ࡣ����������������������õĲ���Ա����˾�ȡ�
	 */
	/* ���棺�˷������������ɡ� */
	protected void initialize(String pk_corp, String sOperatorid, String sOperatorname, String sBiztypeid, String sGroupid, String sLogDate) {

		m_sUserID = sOperatorid;
		m_sGroupID = sGroupid; // ����
		m_sUserName = sOperatorname;
		m_sCorpID = pk_corp;
		m_sLogDate = sLogDate;

		try {
			// ���������
			m_layoutManager = new ToftLayoutManager(this);
			// user code begin {1}
			// ���󷽷�����ʼ������
			initPanel();
			getButtonTree().getButton(ICButtonConst.BTN_LINE_BARCODE).setEnabled(false);
			// ���·�ҳ�Ŀ���
			m_pageBtn = new PageCtrlBtn(this);
			// ��ʼ����ť
			// initButtons();
			// ��ϵͳ����
			initSysParam();
			// ��ʼ��ȱʡ�˵���
			// initButtonsData();

			// ֧����չ��Ͱ�ť
			addExtendedButtons();

			// user code end
			setName("ClientUI");

			// ------------- ���þ��� -----------
			setScaleOfCardPanel(getBillCardPanel());
			setScaleOfListPanel();
			// ���õ��ۡ������ʵ�>0
			getGenBillUICtl().setValueRange(getBillCardPanel(), new String[] {
					"nprice", "hsl", "nsaleprice", "ntaxprice", "nquoteunitrate"
			}, 0, nc.vo.scm.pub.bill.SCMDoubleScale.MAXVALUE);
			// ���ò˵�
			setButtons();

			// ��ʼ���༭ǰ������
			getBillCardPanel().getBillModel().setCellEditableController(this);

		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}

		// ��ʼ��Ϊ���ɱ༭��
		getBillCardPanel().setEnabled(false);
		m_iMode = BillMode.Browse;
		// ��ʼ����ʾ����ʽ��
		m_iCurPanel = BillMode.Card;

		getButtonTree().getButton(ICButtonConst.BTN_SWITCH).setHint(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000068")/* @res "�л����б���ʽ" */);

		getBillListPanel().addEditListener(this);
		getBillListPanel().getChildListPanel().addEditListener(this);
		getBillCardPanel().addEditListener(this);

		// ��ʱ���ӣ��������ȫ�ɱ༭��
		getBillCardPanel().addBodyEditListener2(this);

		getBillCardPanel().setBillBeforeEditListenerHeadTail(this);

		getBillCardPanel().getBillModel().addTableModelListener(this);

		// ���� ������ͷ�ͱ�ͷ������
		m_listHeadSortCtl = new ClientUISortCtl(this, false, BillItem.HEAD);
		getBillListPanel().getHeadBillModel().addBillSortListener2(this);
		m_listBodySortCtl = new ClientUISortCtl(this, false, BillItem.BODY);

		getBillCardPanel().getBillTable().addSortListener();
		m_cardBodySortCtl = new ClientUISortCtl(this, true, BillItem.BODY);

		getBillCardPanel().setAutoExecHeadEditFormula(true);

		// �ϼ���
		getBillCardPanel().getBillModel().addTotalListener(this);

		// getBillListPanel().getHead().addTableModelListener(this);
		getBillListPanel().getBodyTable().getModel().addTableModelListener(this);

		getBillListPanel().addMouseListener(this);

		getBillCardPanel().addBodyMenuListener(this);
		// ���˲�����ʾ
		filterRef(m_sCorpID);
		// ���û�����
		setTotalCol();
		// ��retBusinessBtnǰ���á�
		setButtonStatus(true);

		// ����е��ݲ���
		if (m_bNeedBillRef) {
			// ��ȡҵ������
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

		// �޸��ˣ������� �޸����ڣ�2007-12-26����11:05:02 �޸�ԭ���Ҽ�����"�����к�"
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
	 * �����������������Ӷ��ο����İ�ť�����ݰ�ť�С��Ӷ��ﵽ֧�ֶ��ο����Զ��尴ť��Ŀ¼��
	 * <p>
	 * <b>����˵��</b>
	 * <p>
	 * 
	 * @author duy
	 * @throws BusinessException
	 * @time 2007-3-27 ����05:14:43
	 */
	private void addExtendedButtons() throws BusinessException {
		IFuncExtend funcExtend = nc.ui.scm.extend.FuncExtendInfo.getFuncExtendInstance(m_sBillTypeCode);

		// "���ο���"������ť
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
	 * V5���������ɶ��ŵ���ʱ��Ƭ�����ʼ������
	 * 
	 */
	// public void setRefBillsInit() {
	// getBillCardPanel().addNew();
	// // �����������ݵĳ�ʼ���ݣ������ڣ��Ƶ��˵ȡ�
	// setNewBillInitData();
	// getBillCardPanel().setEnabled(true);
	// m_iMode = BillMode.New;
	//		
	// //��λ���к����ݴ������ԣ�
	//		
	//		
	// //addRowNums(m_iInitRowCount);
	//		
	// // ���õ��ݺ��Ƿ�ɱ༭
	// if (getBillCardPanel().getHeadItem("vbillcode") != null)
	// getBillCardPanel().getHeadItem("vbillcode").setEnabled(
	// m_bIsEditBillCode);
	// getBillCardPanel().setTailItem("iprintcount", new Integer(0));
	//
	// // ��Ҫ��ʼ�����չ���, Ϊ���ȡ�������������. 20021225
	// //filterRef(m_sCorpID);
	//		
	// //Ĭ������£��˿�״̬�������� add by hanwei 2003-10-19 //v5 ������Ҫ�޸��˿����lj
	// nc.ui.ic.pub.bill.GeneralBillUICtl
	// .setSendBackBillState(this, false);
	//
	// // Ĭ�ϲ��ǵ������� add by hanwei 2003-10-30
	// m_bIsImportData = false;
	//
	// // ���õ�ǰ������������ ���� 2004-04-05
	// if (m_utfBarCode != null)
	// m_utfBarCode.setCurBillItem(null);
	//
	// }
	/**
	 * �����ߣ����˾� ���ܣ��������� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	public void onAdd(boolean bUpataBotton, GeneralBillVO voBill) {

		// ��ǰ���б���ʽʱ�������л�������ʽ,[v5]����ǲ��ն������ɣ����л����л��ڵ���onSwitchʱִ��
		if (BillMode.List == m_iCurPanel && !m_bRefBills) onSwitch();

		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH028")/* @res "��������" */);
		// ����
		try {

			if (voBill == null) {
				m_voBill = new GeneralBillVO();
				getBillCardPanel().updateValue();
				getBillCardPanel().addNew();
				getBillCardPanel().getBillModel().clearBodyData();
			}
			// �����������ݵĳ�ʼ���ݣ������ڣ��Ƶ��˵ȡ�
			setNewBillInitData();
			getBillCardPanel().setEnabled(true);
			m_iMode = BillMode.New;

			if (bUpataBotton && voBill == null) setButtonStatus(true);

			// long lTime = System.currentTimeMillis();

			// ��������λ����
			m_alLocatorDataBackup = m_alLocatorData;

			m_alLocatorData = null;
			// ����������к�����
			m_alSerialDataBackup = m_alSerialData;
			m_alSerialData = null;

			// v5
			if (voBill == null) addRowNums(m_iInitRowCount);

			// ��ʾ�����Ҽ���ť�������á�
			if (getBillCardPanel().getBodyMenuItems() != null) for (int i = 0; i < getBillCardPanel().getBodyMenuItems().length; i++)
				getBillCardPanel().getBodyMenuItems()[i].setEnabled(true);

			// 20050519 dw ��;���Ҽ�����ά������Ӧ��� getBillTypeCode() !="40080620"
			if (getBillTypeCode() != "40080620") {
				getBillCardPanel().setBodyMenuShow(true);
			} else {
				getBillCardPanel().setBodyMenuShow(false);
			}

			// ���õ��ݺ��Ƿ�ɱ༭
			if (getBillCardPanel().getHeadItem("vbillcode") != null) getBillCardPanel().getHeadItem("vbillcode").setEnabled(m_bIsEditBillCode);
			getBillCardPanel().setTailItem("iprintcount", new Integer(0));

			// ��Ҫ��ʼ�����չ���, Ϊ���ȡ�������������. 20021225
			filterRef(m_sCorpID);

			// zhx add �������û�����������к�.
			if (voBill == null && getBillCardPanel().getBillModel().getRowCount() != 0) nc.ui.scm.pub.report.BillRowNo.addNewRowNo(getBillCardPanel(), m_sBillTypeCode, m_sBillRowNo);

			// Ĭ������£��˿�״̬�������� add by hanwei 2003-10-19 //v5 ������Ҫ�޸��˿����lj
			nc.ui.ic.pub.bill.GeneralBillUICtl.setSendBackBillState(this, false);

			// Ĭ�ϲ��ǵ������� add by hanwei 2003-10-30
			m_bIsImportData = false;

			// ���õ�ǰ������������ ���� 2004-04-05
			if (m_utfBarCode != null) m_utfBarCode.setCurBillItem(null);

		} catch (Exception e) {
			handleException(e);
		}
	}

	/**
	 * �����ߣ�����еĺ��ķ��� ���ܣ�ȷ�ϣ����棩���� �������� ���⣺ ���ڣ�(2004-4-1 9:23:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2004-4-1 ����
	 */
	public void onAuditKernel(GeneralBillVO voBill) throws Exception {
		nc.ui.pub.pf.PfUtilClient.processBatch("SIGN", m_sBillTypeCode, m_sLogDate, new GeneralBillVO[] {
			voBill
		});
	}

	/**
	 * ����༭��ť����¼���Ӧ���� �������ڣ�(2003-09-28 9:51:50)
	 */
	public void onBarCodeEdit() {
		// �ж��Ƿ��ܹ���������༭
		GeneralBillVO voBill = null;

		int iCurFixLine = 0;
		// �Ƿ���Ա༭
		boolean bDirectSave = false;
		if (m_iMode == BillMode.Browse || !m_bAddBarCodeField) {
			bDirectSave = true;
		} else {
			bDirectSave = false;
		}

		if (BillMode.List == m_iCurPanel) {
			bDirectSave = false;
			showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000071")/* @res "���ڿ�Ƭģʽ�±༭����" */);
			return;
		} else {
			voBill = m_voBill;
			iCurFixLine = getBillCardPanel().getBillTable().getSelectedRow();
		}

		// ��ȥ����ʽ�µĿ���
		filterNullLine();
		if (getBillCardPanel().getRowCount() <= 0) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000072")/* @res "�����������!" */);
			getBillCardPanel().addLine();
			nc.ui.scm.pub.report.BillRowNo.addLineRowNo(getBillCardPanel(), m_sBillTypeCode, m_sBillRowNo);
			return;
		}
		// ����кŵĺϷ���; �÷���Ӧ���ڹ��˿��еĺ��档
		// ��Ҫ���к�ȷ��Ψһ��
		if (!nc.ui.scm.pub.report.BillRowNo.verifyRowNosCorrect(getBillCardPanel(), m_sBillRowNo)) { return; }
		boolean bEditable = true;

		m_dlgBarCodeEdit = getBarCodeDlg(bEditable, bDirectSave);

		if (voBill != null && iCurFixLine >= 0 && iCurFixLine < voBill.getItemCount()) {

			GeneralBillItemVO itemvo = voBill.getItemVOs()[iCurFixLine];
			// ���������Ĵ��
			if (itemvo.getBarcodeManagerflag().booleanValue()) {
				// �õ���ͷ�ĵ��ݺ�, �����к�, �������,�������
				// ArrayList altemp = new ArrayList();

				// ��������£��к�û����m_voBill�д���,���������к�
				getGenBillUICtl().setBillCrowNo(voBill, getBillCardPanel());

				// �����������Items

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

				// �������Ƿ񱣴�������õ�����༭���棬�����ڱ༭���汣������ǰ����
				m_dlgBarCodeEdit.setSaveBarCode(m_bBarcodeSave);
				m_dlgBarCodeEdit.setSaveBadBarCode(m_bBadBarcodeSave);
				// �޸���:������ �޸�����:2007-04-10
				// �޸�ԭ��:�߼�����,����һ������,�Ƿ񱣴���������bSaveBarcodeFinal
				if (voBill.bSaveBarcodeFinal()) m_dlgBarCodeEdit.setSaveBadBarCode(true);
				// �������رգ���������༭���ܱ༭
				boolean bbarcodeclose = itemvo.getBarcodeClose().booleanValue();
				m_dlgBarCodeEdit.setUIEditableBarcodeClose(!bbarcodeclose);
				m_dlgBarCodeEdit.setUIEditable(m_iMode);
				// ���õ�Items��
				m_dlgBarCodeEdit.setCurBarcodeItems(itemBarcodeVos, iFixLine);

				if (m_dlgBarCodeEdit.showModal() == nc.ui.pub.beans.UIDialog.ID_OK) {
					getBillCardPanel().getBillModel().setNeedCalculate(false);
					// �������������
					m_utfBarCode.setCurBillItem(itemBarcodeVos);
					// ����Ҫ����������ɾ�����ݣ�m_utfBarCode.setRemoveBarcode(m_dlgBarCodeEdit.getBarCodeDelofAllVOs());

					// Ŀ������m_billvo���������ݣ��޸Ŀ�Ƭ����״̬

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

							if (!m_dlgBarCodeEdit.m_bModifyBillUINum) { // �޸Ŀ�Ƭ����״̬
								showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000073")/*
																																* @res
																																* "����༭��Ĳ��������ǲ��޸Ľ������������ݽ���ʵ�����������޸ģ�"
																																*/);
							}

							if (!getBarcodeCtrl().isModifyBillUINum()) {
								showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000074")/*
																																* @res
																																* "��ǰ���ݽ��治�����޸�ͨ�����������޸�ʵ�����������ݽ���ʵ�����������޸ģ�"
																																*/);

							}

							if (m_dlgBarCodeEdit.m_bModifyBillUINum && getBarcodeCtrl().isModifyBillUINum()) { // �޸Ŀ�Ƭ����״̬
								showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000075")/*
																																* @res
																																* "����༭��Ĳ����������޸Ľ����������ҽ�����������޸�ʵ�����������ݽ���ʵ�������Ѿ����޸ģ�"
																																*/);
							}

						}

					}
					// dw
					resetSpace(iCurFixLine);

					getBillCardPanel().getBillModel().setNeedCalculate(true);
					getBillCardPanel().getBillModel().reCalcurateAll();

				}

			} else {// �޸���:������ �޸�����:2007-04-05 �޸�ԭ��:���Ǵ���������Ʒ
				nc.ui.ic.pub.tools.GenMethod.handleException(this, null, new BusinessException(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000002")/*
																																											* @res
																																											* "���д�������������������������޸Ĵ�������������ԣ�"
																																											*/
						+ itemvo.getCinventorycode()));
			}

		} else {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000356")/* @res "��ѡ������У�" */);
		}
	}

	/**
	 * ?user> ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2004-5-24 15:54:15) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @param iCurFixLine
	 *            int
	 * @param billItemvo
	 *            nc.vo.ic.pub.bill.GeneralBillItemVO
	 */
	public void onBarCodeEditUpdateBill(int iCurFixLine, GeneralBillItemVO billItemvo) {

		boolean bNegative = false; // �Ƿ���
		// �޸�ʵ��������Ӧ������
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

		// ɾ��������
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
		// ����Ӧ�����������ֿ��Ʋ��ܳ�Ӧ���������̵����
		if (ufdNumDlg.doubleValue() > ufdShouldNum.doubleValue() && !getBarcodeCtrl().isOverShouldNum()) {
			ufdNumDlg = ufdShouldNum.abs();
		}

		// ת��Ϊ����
		if (m_bFixBarcodeNegative || bNegative) ufdNumDlg = ufdNumDlg.multiply(UFDNEGATIVE);

		if (ufdNumDlg == null) ufdNumDlg = UFDZERO;

		// �������������ֶ�
		try {
			getBillCardPanel().setBodyValueAt(ufdNumDlg.abs(), iCurFixLine, m_sNumbarcodeItemKey);

		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
		}
		// �Ƿ��޸ĵ�������Ҫ�������������ж�
		if (m_dlgBarCodeEdit.m_bModifyBillUINum && getBarcodeCtrl().isModifyBillUINum() && m_iMode != BillMode.Browse) {
			// �޸Ŀ�Ƭ����״̬

			getBillCardPanel().setBodyValueAt(ufdNumDlg, iCurFixLine, m_sNumItemKey);

			nc.ui.pub.bill.BillEditEvent event1 = new nc.ui.pub.bill.BillEditEvent(getBillCardPanel().getBodyItem(m_sNumItemKey), ufdNumDlg, m_sNumItemKey, iCurFixLine);
			afterNumEdit(event1);
			// ִ��ģ�湫ʽ
			getGenBillUICtl().execEditFormula(getBillCardPanel(), iCurFixLine, m_sNumItemKey);
			// ����������״̬Ϊ�޸�
			if (getBillCardPanel().getBodyValueAt(iCurFixLine, m_sBillRowItemKey) != null) getBillCardPanel().getBillModel().setRowState(iCurFixLine, BillMode.Update);

		}
	}

	/**
	 * \n�������ڣ�(2003-3-6 15:13:32) ���ߣ����Ӣ �޸����ڣ� �޸��ˣ� �޸�ԭ�� �㷨˵����
	 */
	public void onDocument() {
		ArrayList alBill = getSelectedBills();
		if (alBill == null || alBill.size() == 0) {
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000076")/* @res "����ѡ�񵥾ݣ�" */);
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

		CollectSettingDlg dlg = new CollectSettingDlg(this, ResBase.getBtnMergeShowName()/* @res "�ϲ���ʾ" */);
		dlg.initData(getBillCardPanel(), new String[] {
				"cinventorycode", "invname", "invspec", "invtype"
		}, // �̶�������
				null,// ȱʡ������
				new String[] {
						"nshouldinnum", "nneedinassistnum", "nshouldoutnum", "nshouldoutassistnum", "ninnum", "ninassistnum", "noutnum", "noutassistnum", "ningrossnum", "noutgrossnum", "nmny", "nplannedmny", "ntarenum"
				},// �����
				null,// ��ƽ����
				null,// ���Ȩƽ����
				null// ������
		);
		dlg.show();

	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-8-25 13:53:58) ��Ӧ�����գ��Զ���дʵ�����գ�
	 */
	private void onFillNum() {
		// ������
		// if (getBillCardPanel().getBodyItem(m_sShouldAstItemKey) != null
		// && getBillCardPanel().getBodyItem(m_sAstItemKey) != null
		// && getBillCardPanel().getBodyValueAt(
		// getBillCardPanel().getBillTable().getSelectedRow(),
		// m_sShouldAstItemKey) != null)
		// GeneralBillUICtl.fillValue(getBillCardPanel(), this,
		// m_sShouldAstItemKey, m_sAstItemKey);

		// ����
		if (getBillCardPanel().getBodyItem(m_sShouldNumItemKey) != null && getBillCardPanel().getBodyItem(m_sNumItemKey) != null) GeneralBillUICtl.fillValue(getBillCardPanel(), this, m_sShouldNumItemKey, m_sNumItemKey);
		return;
	}

	/**
	 * �˴����뷽��˵���� ��������:�ڿ�Ƭģʽ��ת���һ�š� ���ߣ������� �������: ����ֵ: �쳣����: ����:(2003-5-27
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
	 * �˴����뷽��˵���� ������Դ���ݣ�ֻ�������ӡ�������Ѿ��������¿���ֱ�ӱ��浼�� �������ڣ�(2004-4-20 11:36:04)
	 */
	public void onImportBarcodeSourceBill() {
		try {

			if (m_iMode != BillMode.Browse) {
				// �༭����£�
				// ��ȥ����ʽ�µĿ���
				filterNullLine();
				if (getBillCardPanel().getRowCount() <= 0) {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000072")/*
																													* @res
																													* "�����������!"
																													*/);
					return;
				}
				// ������������к�Ϊ�����ģ�����У��
				if (!nc.ui.scm.pub.report.BillRowNo.verifyRowNosCorrect(getBillCardPanel(), m_sBillRowNo)) { return; }
			}

			// ִ�к�̨�ĵ����������Ҫ�ж������б��»�Ƭ��
			ArrayList alBill = new ArrayList();
			if (m_iCurPanel == BillMode.Card) { // ���
				GeneralBillVO vo = null;
				if (m_iMode == BillMode.Browse) {
					if (m_iLastSelListHeadRow != -1 && null != m_alListData && m_alListData.size() != 0) {
						vo = (GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow);
					} else {
						showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000077")/*
																														* @res
																														* "��Ƭ��û�е������ݲ����Ե��룡"
																														*/);
						return;
					}
				} else {
					vo = m_voBill;
				}
				alBill.add(vo);
			} else if (m_iCurPanel == BillMode.List) { // �б�
				showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000078")/* @res "�б��²����Ե��룡" */);
				return;
			}
			String sHID = null;
			String sBillTypecode = null;
			if (alBill != null && alBill.size() > 0) {
				GeneralBillVO billVO = null;
				StringBuffer sbErr = new StringBuffer("");
				ArrayList alSourceHID = new ArrayList();
				for (int n = 0; n < alBill.size(); n++) {
					// У������
					billVO = (GeneralBillVO) alBill.get(n);

					if (billVO == null) continue;

					nc.vo.ic.pub.bill.GeneralBillHeaderVO headvo = billVO.getHeaderVO();
					GeneralBillItemVO[] billItemVOs = (GeneralBillItemVO[]) billVO.getChildrenVO();
					if (billVO == null || billVO.getHeaderVO() == null || billVO.getChildrenVO() == null || billVO.getChildrenVO().length == 0) {
						if (m_iCurPanel == BillMode.Card) sbErr.append(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000325")/*
																																					* @res
																																					* "��ǰ���ݱ���û�����ݲ����Ե��룡"
																																					*/);
						continue;
					}

					// ׼������
					sHID = headvo.getCgeneralhid();
					sBillTypecode = headvo.getCbilltypecode();

					if (sBillTypecode != null && (sBillTypecode.equalsIgnoreCase("4A") || sBillTypecode.equalsIgnoreCase("4I") || sBillTypecode.equalsIgnoreCase("4Y") || sBillTypecode.equalsIgnoreCase("4E") || sBillTypecode.equalsIgnoreCase("4C"))) { // ���Ϲ�����������ⵥ����������ⵥ�����۳�����Ե��뵥������
					} else {
						sbErr.append(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000326")/*
																													* @res
																													* "��ǰ���ݱ��壬�����۳��⡢��������ⵥ���������ⵥ�������Ե�����Դ��������"
																													*/);
						showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000079")/*
																														* @res
																														* "û����Դ���ݣ����ɵ��룡"
																														*/);
						return;
					}

					java.util.ArrayList alBIDCrowno = new java.util.ArrayList();
					java.util.ArrayList alBIDSourceid = new java.util.ArrayList();
					java.util.Hashtable htbSourceBIDRepeat = new java.util.Hashtable();
					UFBoolean ufbHasHBID = new UFBoolean(true);
					if (sHID == null) {
						// û�б���ID��ֱ���������������false
						ufbHasHBID = new UFBoolean(false);
					}
					String sSourceHID = null;
					// String sFirstHID = null;
					String sCourceTypecode = null; // ��Դ���ݺ�
					boolean bTranBill = false; // �Ƿ�ת��

					String sCsourcebillbid = null;
					java.util.ArrayList alRepeatRow = new java.util.ArrayList(); // �ظ���
					for (int i = 0; i < billItemVOs.length; i++) {
						// if (i == 0) {
						sCourceTypecode = billItemVOs[i].getCsourcetype();
						if (sCourceTypecode == null || sCourceTypecode.trim().length() == 0) continue;

						// ת�ⵥ����Դ���ݺ�
						if (sCourceTypecode.startsWith("4")) {
							sSourceHID = billItemVOs[i].getCsourcebillhid();
							bTranBill = true;
						} else // ����������Դͷ���ݺ�
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
							// У���ظ�����Դ����ID
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
																														* "û����Դ���ݣ����ɵ��룡"
																														*/);
						return;
					}
					// ���������ظ�����Դ�����У�
					if (alRepeatRow != null && alRepeatRow.size() > 0) {
						StringBuffer sbError = new StringBuffer();
						String sRowno = null;
						sbError.append(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000080")/*
																													* @res
																													* "���������ظ�����Դ�����У����ܵ�����Դ���ݣ���ϲ���\n"
																													*/);
						for (int i = 0; i < alRepeatRow.size(); i++) {
							sRowno = (String) alRepeatRow.get(i);
							if (i > 0) sbError.append(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000000")/*
																																		* @res "��"
																																		*/);
							sbError.append(sRowno);
						}
						showErrorMessage(sbError.toString());
						return;
					}

					java.util.ArrayList alCon = new ArrayList();
					alCon.add(sHID);
					// ���кŻ�BID��Ϊ����ID����������
					alCon.add(alBIDCrowno);
					alCon.add(alBIDSourceid);
					alCon.add(m_sCorpID);
					alCon.add(sBillTypecode);
					alCon.add(ufbHasHBID);
					// alCon.add(sSourceHID);
					alCon.add(alSourceHID);
					alCon.add(sCourceTypecode);

					// ��������
					try {
						java.util.ArrayList alResult = nc.ui.ic.pub.bc.BarCodeImportBO_Client.importSourceBarcode(alCon);

						java.util.ArrayList alresult = nc.vo.ic.pub.barcodeparse.BarcodeparseCtrl.setImportBarcode(billVO, alResult);

						if (alresult.size() > 0) {
							StringBuffer sbMsg = new StringBuffer(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000081")/*
																																				* @res
																																				* "���ݵ�����������\n"
																																				*/);
							for (int i = 0; i < alresult.size(); i++) {
								sbMsg.append((String) alresult.get(i) + "\n");
							}
							showWarningMessage(sbMsg.toString());
						} else {
							showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000082")/*
																															* @res
																															* "������û�����뵼�룡"
																															*/);
							return;
						}
						// �����ǰ���������£���Ҫֱ�ӱ��浽��̨
						// false:ǰ̨����У�飬��̨У��
						if (m_iMode == BillMode.Browse) {
							onImportSignedBillBarcode(billVO, false);
							showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000083")/*
																															* @res
																															* "������Դ������ɣ������Ѿ�������ϡ�"
																															*/);
						} else {
							showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000084")/*
																															* @res
																															* "������Դ�������뵽��ǰ���ݽ����ϣ���㰴Ŧ�����桱�����浥�ݡ�"
																															*/);
						}

					} catch (Exception e) {
						String[] args = new String[1];
						args[0] = headvo.getVbillcode().toString();
						String message = nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000342", null, args);
						/* @res "���ݺ�Ϊ{0}�ĵ���,���ܵ�����Դ���ݵ���������쳣:" */
						sbErr.append(message);
					}
				}
				String sErrMsg = sbErr.toString();
				if (sErrMsg != null && sErrMsg.trim().length() > 0) {
					showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000044")/*
																													* @res
																													* "������ʾ��"
																													*/
							+ sErrMsg);
					showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000085")/*
																													* @res
																													* "������Դ����ʧ�ܣ�"
																													*/);

				}

			} else {
				showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000086")/*
																												* @res "������ʾ��û��ѡ��ĵ���"
																												*/);
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000085")/* @res "������Դ����ʧ�ܣ�" */);
			} // �ѵ������ŵ�ǰ̨
		} catch (Exception e) {
			String sErrorMsg = e.getMessage();
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000044")/* @res "������ʾ��" */
					+ sErrorMsg);
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000085")/* @res "������Դ����ʧ�ܣ�" */);
		}
	}

	/**
	 * �����ߣ�� ���ܣ���������˺󣩱��浼������
	 */
	public void onImportSignedBillBarcode(GeneralBillVO voUpdatedBill) throws Exception {

		// �Ƿ�������������ʵ��������true
		onImportSignedBillBarcode(voUpdatedBill, true);

	}

	/**
	 * �����ߣ�� ���ܣ�����δǩ�����״̬�µ�������
	 */
	public ArrayList onImportSignedBillBarcodeKernel(GeneralBillVO voBill, GeneralBillVO voUpdatedBill) throws Exception {
		voBill.setAccreditUserID(voUpdatedBill.getAccreditUserID());
		voBill.setOperatelogVO(voUpdatedBill.getOperatelogVO());
		// ��������������������
		nc.vo.ic.pub.barcodeparse.BarcodeparseCtrl.beforSaveBillVOBarcode(m_bBarcodeSave, voBill);
		// �Ƿ񱣴�����
		voBill.setSaveBadBarcode(m_bBadBarcodeSave);
		// �Ƿ񱣴�������һ�µ�����
		voBill.setSaveBarcode(m_bBarcodeSave);
		ArrayList alRetData = (ArrayList) nc.ui.pub.pf.PfUtilClient.processAction("IMPORTBARCODE", m_sBillTypeCode, m_sLogDate, voBill, null);
		// ����������������������ָ������Ŀ¼�µ�Excel�����ļ���ɾ��
		OffLineCtrl ctrl = new OffLineCtrl(this);
		ctrl.directSaveDelete(voBill);

		return alRetData;
	}

	/**
	 * ���Ե���Excel����������ļ�(���������в˵��� ����:� iMenu =0 ,1��2 �ֱ��Ӧ�����������������������
	 * �������ڣ�(2004-4-21 20:41:28)
	 */
	protected void onImptBCExcel(int iMenu) {
		// �Ƿ񸲸ǾɵĴ��������
		boolean bCover = false;
		// ��ǰѡ����
		int rownow;
		// ���������ļ��ڴ�VO������
		BarCodeVO[] voaImport = null;
		// ������������
		int sizeVOImp = 0;
		// �¾��������Ϣ
		ArrayList alOldVO = new ArrayList();
		ArrayList alNewVO = new ArrayList();
		// ���Ϸ��ص�BarcodeVO����
		BarCodeVO[] bcVOTotal = null;

		rownow = checkSelectionRow();
		if (rownow == -1) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH004")/* @res "��ѡ��Ҫ����������У�" */);
			return;
		}
		// �õ��ɵĴ��������Ϣ
		GeneralBillItemVO billItemvo = (GeneralBillItemVO) m_voBill.getItemVOs()[rownow];
		alOldVO = BarcodeparseCtrlUI.getVOInfoOld(billItemvo);
		// �����ļ���VO
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
																																	* @res "����"
																																	*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000088")/*
																																																						* @res "Excel�ļ�����Ϊ�գ�"
																																																						*/);
					return;
				}
			} else return;
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000089")/* @res "��Excel�����ļ�����" */
					+ e.getMessage());
		}
		/** �����볤�Ƚ����������У�� */
		String sMsg = BarcodeparseCtrlUI.verifyLenInfo(m_sCorpID, voaImport);
		if (sMsg != null) {
			showErrorMessage(sMsg);
			return;
		}

		// ��������ж�,ֻ�Ե�һ������У��
		if (voaImport != null && voaImport.length != 0 && voaImport[0] != null) {
			alNewVO = BarcodeparseCtrlUI.getVOInfoNew(voaImport[0], m_sCorpID);
			if (alNewVO == null) {
				MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000059")/*
																																* @res "����"
																																*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000090")/*
																																																					* @res "�������벻�����������"
																																																					*/);
				return;
			}
			if (iMenu == 0 && alNewVO.get(1) != BarcodeparseCtrl.MAINBARCODE) {
				MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPPSCMCommon-000059")/* @res "����" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000327")/* @res "�������벻�������룡" */);
				return;
			}
			if (iMenu == 1 && alNewVO.get(1) != BarcodeparseCtrl.SUBBARCODE) {
				MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPPSCMCommon-000059")/* @res "����" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000328")/* @res "�������벻�Ǵ����룡" */);
				return;
			}
			if (iMenu == 2 && alNewVO.get(1) != BarcodeparseCtrl.BOTHCODEHAVE) {
				MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPPSCMCommon-000059")/* @res "����" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000329")/* @res "�������벻���������룡" */);
				return;
			}
		}
		// ���������͵�������
		if (voaImport != null) sizeVOImp = voaImport.length;
		for (int i = 0; i < sizeVOImp; i++) {
			voaImport[i].setNnumber(new UFDouble("1.00"));

			voaImport[i].setBsingletypeflag((UFBoolean) alNewVO.get(0));
		}

		// ����VO[]
		BarCodeVO[] voaOld = billItemvo.getBarCodeVOs();
		BarCodeVO[] voaOldCopy = null;
		if (voaOld != null) {
			voaOldCopy = new BarCodeVO[voaOld.length];
			for (int i = 0; i < voaOld.length; i++) {
				voaOldCopy[i] = (nc.vo.ic.pub.bc.BarCodeVO) voaOld[i].clone();
			}
		}

		bcVOTotal = BarcodeparseCtrlUI.barCodeAddWrapper(iMenu, bCover, alOldVO, voaOld, voaImport);
		// *���״״̬��ǩ��״̬�£���������ӦС��ʵ������
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
																																* @res "����"
																																*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000091")/*
																																																					* @res "����������Ӧ����ʵ���շ�������"
																																																					*/);
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000092")/*
																												* @res "����������Ӧ����ʵ���շ�����"
																												*/);
				m_voBill.getItemVOs()[rownow].setBarCodeVOs(voaOldCopy);
				return;
			}
		}
		// 5.��VO���ڴ������VO��
		m_voBill.getItemVOs()[rownow].setBarCodeVOs(bcVOTotal);
		if (bcVOTotal == null || bcVOTotal.equals(voaOld)) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000093")/* @res "û�е���ɹ��������µ���!" */);
			return;
		}
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000094")/* @res "�ѽ����뵼�뵽����,�뱣��!" */);
		// 6
		try {
			if ((m_voBill.getHeaderVO().getPrimaryKey() != null) && (m_iMode == BillMode.Browse)) {
				if (onImportSignedBillBarcode(m_voBill, true) == 1) showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000095")/*
																																									* @res
																																									* "���벢��������ɹ�(��ע���浥���Ƿ����������룩!"
																																									*/);
				else {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000096")/*
																													* @res
																													* "
																													* û�б���ɹ�
																													* ��"
																													*/);
					m_voBill.getItemVOs()[rownow].setBarCodeVOs(voaOldCopy);
				}
			}
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
			showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000097")/* @res "����ʧ�ܣ�" */
					+ e.getMessage());
		}
	}

	/**
	 * �˴����뷽��˵���� ��������:���ݱ����еĵ���ID�͵����������������ε��ݡ� ���ߣ������� �������: ����ֵ: �쳣����:
	 * ����:(2003-4-22 16:09:14)
	 */
	public void onJointCheck() {

		if (m_iLastSelListHeadRow >= 0 && m_alListData != null && m_alListData.size() > m_iLastSelListHeadRow && m_alListData.get(m_iLastSelListHeadRow) != null) {

			GeneralBillVO voBill = null;
			GeneralBillHeaderVO voHeader = null;
			// �õ�����VO
			voBill = (GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow);
			// �õ����ݱ�ͷVO
			voHeader = voBill.getHeaderVO();

			if (voHeader == null) {
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000098")/* @res "û��Ҫ����ĵ��ݣ�" */);
				return;
			}
			String sBillPK = null;
			String sBillTypeCode = null;

			sBillPK = voHeader.getCgeneralhid();
			sBillTypeCode = voHeader.getCbilltypecode();
			// ���sBillPK��sBillTypeCodeΪ�գ�����û�����塣
			if (sBillPK == null || sBillTypeCode == null) {
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000099")/* @res "����û�ж�Ӧ���ݣ�" */);
				return;
			}
			nc.ui.scm.sourcebill.SourceBillFlowDlg soureDlg = new nc.ui.scm.sourcebill.SourceBillFlowDlg(this, sBillTypeCode, sBillPK, null, m_sUserID, m_sCorpID);
			soureDlg.showModal();
		} else {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000154")/* @res "û������ĵ��ݣ�" */);
		}
	}

	/**
	 * �˴����뷽��˵���� ��������:�ڿ�Ƭģʽ��ָ�����һ�� ���ߣ������� �������: ����ֵ: �쳣����: ����:(2003-5-27
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
	 * �˴����뷽��˵���� ��������:�ڿ�Ƭģʽ��ָ����һ�� ���ߣ������� �������: ����ֵ: �쳣����: ����:(2003-5-27
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
	 * �����ߣ������� ���ܣ���ӡԤ�� ������ ���أ� ���⣺ ���ڣ�(2001-5-10 ���� 4:16) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * �޸�˵�������Ӵ�ӡ�������� �޸��ߣ��۱� 2004-01-12
	 */
	public void onPreview() {

		try {
			// ������ӡ����
			// ����ǰ���б��Ǳ�������ӡ����
			if (m_iMode == BillMode.Browse && m_iCurPanel == BillMode.Card) { // ���

				/* ���Ӵ�ӡ�������ƺ�Ĵ�ӡ���� */
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000047")/*
																												* @res "���ڴ�ӡ�����Ժ�..."
																												*/);
				// ׼�����ݣ����Ҫ��ӡ��vo.
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
																													* "���ȶ����ӡģ�塣"
																													*/);
					return;
				}

				// ��Ƭ��Ԥ��
				printOnCard(voBill, true);

				// showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
				// "SCMCOMMON", "UPPSCMCommon-000133")/* @res "����" */);

			} else if (m_iCurPanel == BillMode.List) { // �б�

				/* ���Ӵ�ӡ�������ƺ�Ĵ�ӡ���� */
				if (null == m_alListData || m_alListData.size() == 0) {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000049")/*
																													* @res
																													* "���Ȳ�ѯ��¼�뵥�ݡ�"
																													*/);
					return;
				}
				// ��Ҫ�Ļ�����ѯȱ�ٵĵ�������
				// queryLeftItem(m_alListData);

				ArrayList alBill = getSelectedBills();
				if (alBill == null || alBill.size() <= 0 || alBill.get(0) == null) {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH003")/*
																									 * @res "��ѡ��Ҫ��������ݣ�"
																									 */);
					return;
				}
				// ��С������
				setScaleOfListData(alBill);

				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000100")/*
																												* @res
																												* "�������ɵ�һ�ŵ��ݵĴ�ӡԤ�����ݣ����Ժ�..."
																												*/);

				GeneralBillVO voBill = (GeneralBillVO) alBill.get(0);
				// ��������
				GeneralBillHeaderVO headerVO = voBill.getHeaderVO();
				String sBillID = headerVO.getPrimaryKey();

				// ����PringLogClient�Լ�����PrintInfo
				ScmPrintlogVO voSpl = new ScmPrintlogVO();
				voSpl = new ScmPrintlogVO();
				voSpl.setCbillid(sBillID); // ���������ID
				voSpl.setVbillcode(headerVO.getVbillcode());// ���뵥�ݺţ�������ʾ��
				voSpl.setCbilltypecode(headerVO.getCbilltypecode());
				voSpl.setCoperatorid(headerVO.getCoperatorid());
				voSpl.setIoperatetype(new Integer(PrintConst.PAT_OK));// �̶�
				voSpl.setPk_corp(getCorpPrimaryKey());
				voSpl.setTs(headerVO.getTs());// ���������TS

				SCMEnv.out("ts=========tata" + voSpl.getTs());
				nc.ui.scm.print.PrintLogClient plc = new nc.ui.scm.print.PrintLogClient();

				nc.ui.pub.print.PrintEntry pe = getPrintEntryNew();

				if (pe.selectTemplate() < 0) {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000048")/*
																													* @res
																													* "���ȶ����ӡģ�塣"
																													*/);
					return;
				}

				plc.setPrintEntry(pe);
				// ���õ�����Ϣ
				plc.setPrintInfo(voSpl);
				// ����ts��printcountˢ�¼���.
				plc.addFreshTsListener(new FreshTsListener());
				// ���ô�ӡ����
				pe.setPrintListener(plc);

				// ��ӡԤ��
				getDataSource().setVO(voBill);
				pe.setDataSource(getDataSource());
				pe.preview();

				// showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
				// "SCMCOMMON", "UPPSCMCommon-000133")/* @res "����" */);

			} else showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000101")/*
																												* @res
																												* "��ע�⣺��ֻ�������״̬��ִ�д�ӡԤ����"
																												*/);
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000061")/* @res "��ӡ����" */
					+ e.getMessage());
		}
	}

	/**
	 * �����д�ӡ��ͨ�����������Ի����û���������������VO����ӡԤ��
	 */
	protected void onPrintSumRow() {

		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi", "UPP4008busi-000248")/* @res "���ڴ�ӡ�����Ժ�..." */);
		SCMEnv.out("��ӡ���λ��ܿ�ʼ!\n");
		try {
			// ������ӡ����
			// ����ǰ���б��Ǳ�������ӡ����
			if (m_iMode == BillMode.Browse && m_iCurPanel == BillMode.Card) { // ���
				SCMEnv.out("��ӡ���λ��ܿ�ʼ!����ӡ!\n");
				// ׼������
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

				// �õ���������,�����ѡ����ܸ���������λ�����û��ܸ�����,Ĭ��ѡ����ܸ�����
				MergeRowDialog dlgMerge = new MergeRowDialog(this);
				if (dlgMerge.showModal() == UIDialog.ID_CANCEL) return;
				ArrayList alGroupBy = dlgMerge.getGroupingAttr();
				if (alGroupBy == null || alGroupBy.size() <= 0 || alGroupBy.size() != 6) return;

				// if ( ((Boolean)alGroupBy.get(2)).booleanValue() == false )
				//				

				// �õ������ֶ�
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

				// �õ�Summing�ֶ�
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

				// ����Ԥ��
				printOnCard(gvobak, true);

			} else if (m_iCurPanel == BillMode.List) {
				// �б�

				SCMEnv.out("�б��ӡ��ʼ!\n");
				if (null == m_alListData || m_alListData.size() == 0) { return; }
				if (getPrintEntry().selectTemplate() < 0) return;
				ArrayList alBill = getSelectedBills();
				// ��С������
				setScaleOfListData(alBill);
				SCMEnv.out("�б��ӡ:�õ�ѡ�еĵ��ݲ�������������!\n");
				if (alBill == null) return;
				nc.vo.scm.merge.DefaultVOMerger dvomerger = null;
				for (int i = 0; i < alBill.size(); i++) {
					SCMEnv.out("�б��ӡ:��ʼ�ϲ�������!\n");
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
					SCMEnv.out("�б��ӡ:�õ��ϲ���ı�����!\n");
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
				SCMEnv.out("�б��ӡ:�õ��ϲ���ĵ���!\n");

				// �б��´�ӡ������Ԥ����
				printOnList(alBill);

			} else showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi", "UPP4008busi-000249")/* @res "ֻ�������״̬�´�ӡ" */);
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi", "UPPSCMCommon-000061")/* @res "��ӡ����" */
					+ e.getMessage());
		}

	}

	/**
	 * �˴����뷽��˵���� ��������:�ڿ�Ƭģʽ��ָ��ǰһ�� ���ߣ������� �������: ����ֵ: �쳣����: ����:(2003-5-27
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
			// MessageDialog.showErrorDlg(this,"����Error", e.getMessage());
			// return false;
			throw e;
		}

	}

	/**
	 * �����ߣ����˾� ���ܣ�ȷ�ϣ����棩���� �������� ���أ� true: �ɹ� false: ʧ��
	 * 
	 * ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 2001/10/29,wnj,��ֳ���������/�����޸�����������ʹ�ø��淶��
	 * 
	 * 
	 * 
	 */
	public boolean onSaveBase() {
		try {
			nc.vo.ic.pub.bill.Timer t = new nc.vo.ic.pub.bill.Timer();
			m_timer.start("���濪ʼ");
			t.start();
			// ��ȥ����ʽ�µĿ���
			filterNullLine();

			GeneralBillItemVO[] oldvo = getBodyVOs();
			HashMap<String, String> hm = new HashMap<String, String>();
			for (int i = 0; i < oldvo.length; i++) {
				if (!StringIsNullOrEmpty(oldvo[i].getVfirstbillcode()) && !hm.containsKey(oldvo[i].getCfirstbillhid())) {
					hm.put(oldvo[i].getCfirstbillhid(), oldvo[i].getVfirstbillcode());
				}
			}

			m_timer.showExecuteTime("filterNullLine");
			// �ޱ����� ------------ EXIT -------------------
			if (getBillCardPanel().getRowCount() <= 0) {
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000072")/* @res "�����������!" */);
				// ��������� add by hanwei 2004-06-08 ,��������ⵥ��Щ����²�������
				return false;
			}
			// added by zhx 030626 ����кŵĺϷ���; �÷���Ӧ���ڹ��˿��еĺ��档
			if (!nc.ui.scm.pub.report.BillRowNo.verifyRowNosCorrect(getBillCardPanel(), m_sBillRowNo)) { return false; }
			// ��ǰ�ı�������
			int iRowCount = getBillCardPanel().getRowCount();
			// ����ĵ�������
			GeneralBillVO voInputBill = null;
			// �ӽ����л����Ҫ������
			voInputBill = getBillVO();

			// �õ����ݴ��󣬳��� ------------ EXIT -------------------
			if (voInputBill == null || voInputBill.getParentVO() == null || voInputBill.getChildrenVO() == null) {
				SCMEnv.out("Bill is null !");
				return false;
			}
			// ����ı�����
			GeneralBillItemVO voInputBillItem[] = voInputBill.getItemVOs();
			// �õ�������
			int iVORowCount = voInputBillItem.length;
			// �õ������кͽ���������һ�£����� ------------ EXIT -------------------
			if (iVORowCount != iRowCount) {
				SCMEnv.out("data error." + iVORowCount + "<>" + iRowCount);
				return false;
			}
			m_timer.showExecuteTime("From fliterNullLine Before setIDItems");
			// VOУ��׼������
			m_voBill.setIDItems(voInputBill);
			// ���õ�������
			m_voBill.setHeaderValue("cbilltypecode", m_sBillTypeCode);

			m_timer.showExecuteTime("setIDItems");

			if (getBillCardPanel().getHeadItem("vdiliveraddress") != null && getBillCardPanel().getHeadItem("vdiliveraddress").getComponent() != null) {
				String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("vdiliveraddress").getComponent()).getUITextField().getText();// getRefName();
				// �������������б���ʽ����ʾ��
				if (m_voBill != null) m_voBill.setHeaderValue("vdiliveraddress", sName);
			}

			// ���õ����к�zhx 0630:
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

			// VOУ�� ------------ EXIT -------------------
			if (!checkVO(m_voBill)) {

			return false; }
			m_timer.showExecuteTime("VOУ��");

			// ���û�е������ڣ���дΪ��ǰ��¼����
			if (getBillCardPanel().getHeadItem("dbilldate") == null || getBillCardPanel().getHeadItem("dbilldate").getValueObject() == null || getBillCardPanel().getHeadItem("dbilldate").getValueObject().toString().trim().length() == 0) {
				SCMEnv.out("-->no bill date.");
				m_voBill.setHeaderValue("dbilldate", m_sLogDate);
			}
			m_timer.showExecuteTime("���õ������ͺ͵�������");

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
						//edit by shikun 2014-03-20 �������Ϊ�������˻�ʱ�������ж���Ƿ��ظ������жϡ�
						//ȡӦ������
						UFDouble nshouldinnum = m_voBill.getChildrenVO()[i].getAttributeValue("nshouldinnum")==null? new UFDouble(0.00)
						:new UFDouble(m_voBill.getChildrenVO()[i].getAttributeValue("nshouldinnum").toString());
						//ȡʵ������
						UFDouble ninnum = m_voBill.getChildrenVO()[i].getAttributeValue("ninnum")==null? new UFDouble(0.00)
						:new UFDouble(m_voBill.getChildrenVO()[i].getAttributeValue("ninnum").toString());
						//���ʵ��������Ϊ0����ʵ������Ϊ��
						//�˻�����
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
				// if(MessageDialog.ID_YES!=showYesNoMessage(Transformations.getLstrFromMuiStr("���@The pile No. &"+dhList.toString()
				// + "&�ظ���⣡@repeat warehousing!")+" \r\n"
				// + "�Ƿ������ continue?"))
				// {
				// return false ;
				// }'
				showErrorMessage(Transformations.getLstrFromMuiStr("���@The pile No. &" + dhList.toString() + "&�ظ���⣡@repeat warehousing!"));
				return false;
			}

			// showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
			// "4008bill", "UPP4008bill-000102")/* @res "���ڱ��棬���Ժ�..." */);

			// ����ĺ��ķ������ add by hanwei 2004-04

			// Ĭ�ϵ���״̬ add by hanwei
			m_sBillStatus = nc.vo.ic.pub.bill.BillStatus.FREE;
			// ʵ��m_sBillStatus�ĸ�ֵ��onSaveBaseKernel�еģ�saveUpdateBill,saveNewBill

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
						// ������Ϣ��ʾ����ѯ���û����Ƿ��������
						int iFlag = showYesNoMessage(realbe.getMessage());
						// ����û�ѡ�����
						if (iFlag == nc.ui.pub.beans.MessageDialog.ID_YES) {
							m_voBill.setIsRwtPuUserConfirmFlag(true);
							continue;
						} else return false;
					} else if (realbe != null && realbe.getClass() == CreditNotEnoughException.class) {
						// ������Ϣ��ʾ����ѯ���û����Ƿ��������
						int iFlag = showYesNoMessage(realbe.getMessage() + " \r\n" + "�Ƿ������");
						// ����û�ѡ�����
						if (iFlag == nc.ui.pub.beans.MessageDialog.ID_YES) {
							m_voBill.setIsCheckCredit(false);
							continue;
						} else return false;
					} else if (realbe != null && realbe.getClass() == PeriodNotEnoughException.class) {
						// ������Ϣ��ʾ����ѯ���û����Ƿ��������
						int iFlag = showYesNoMessage(realbe.getMessage() + " \r\n" + "�Ƿ������");
						// ����û�ѡ�����
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
							// ������Ϣ��ʾ����ѯ���û����Ƿ��������
							int iFlag = showYesNoMessage(atpe.getMessage() + " \r\n" + "�Ƿ������");
							// ����û�ѡ�����
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

			// ����ͨ���������޸�
			if (BillMode.New == m_iMode || BillMode.Update == m_iMode) {
				// necessary��//ˢ�µ���״̬
				getBillCardPanel().updateValue();
				m_timer.showExecuteTime("updateValue");
				// coperatorid
				m_iMode = BillMode.Browse;

				// ���ɱ༭
				getBillCardPanel().setEnabled(false);
				// ���谴ť״̬
				setButtonStatus(false);
				m_timer.showExecuteTime("setButtonStatus");

				// ����ִ���
				// ���� by hanwei 2003-11-13 ���Ᵽ������ѡ����ִ���Ϊ��
				// m_voBill.clearInvQtyInfo();
				// ѡ�е�һ��
				getBillCardPanel().getBillTable().setRowSelectionInterval(0, 0);
				// �������к��Ƿ����
				setBtnStatusSN(0, false);
				// ˢ�µ�һ���ִ�����ʾ
				setTailValue(0);
				m_timer.showExecuteTime("ˢ�µ�һ���ִ�����ʾ");
			}

			if (m_sBillStatus != null && !m_sBillStatus.equals(BillStatus.FREE) && !m_sBillStatus.equals(BillStatus.DELETED)) {
				SCMEnv.out("**** saved and signed ***");
				freshAfterSignedOK(m_sBillStatus);
				m_timer.showExecuteTime("freshAfterSignedOK");
			}
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH005")/* @res "����ɹ�" */);

			// ��������Դ�ĵ��ݽ��в�ͬ�Ľ�����ƣ�zhx 1130
			ctrlSourceBillUI(true);
			m_timer.showExecuteTime("��Դ���ݽ������");
			t.stopAndShow("����ϼ�");

			// save the barcodes to excel file according to param IC***
			m_timer.showExecuteTime("��ʼִ�б��������ļ�");
			OffLineCtrl ctrl = new OffLineCtrl(this);
			ctrl.saveExcelFile(m_voBill, getCorpPrimaryKey());
			m_timer.showExecuteTime("ִ�б��������ļ�����");
			return true;

		} catch (java.net.ConnectException ex1) {
			SCMEnv.out(ex1.getMessage());
			if (showYesNoMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000104")/*
																												* @res
																												* "��ǰ�����жϣ��Ƿ񽫵�����Ϣ���浽Ĭ��Ŀ¼��"
																												*/
			) == MessageDialog.ID_YES) {
				onBillExcel(1);// ���浥����Ϣ��Ĭ��Ŀ¼
			}
		} catch (Exception e) {

			handleException(e);
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000105")/* @res "�������" */);
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
	 * @����:���ع�˾���ϼ���˾����
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
	 * �ж϶���Ƿ���� ������ ���ֹ�Ө2012/8/29
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
			MessageDialog.showErrorDlg(this, "����Error", e.getMessage());
			return false;
		}
		return rst;
	}

	public boolean StringIsNullOrEmpty(Object obj) {
		return obj == null ? true : String.valueOf(obj).equals("") ? true : String.valueOf(obj).equalsIgnoreCase("null") ? true : false;
	}

	/**
	 * �����ߣ�������������еĺ��ķ��� ���ܣ�ȷ�ϣ����棩���� �������� ���⣺ ���ڣ�(2004-4-1 9:23:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2004-4-1 ����
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
			} else // �޸�
			if (BillMode.Update == m_iMode) {
				// �õ��޸ĺ�ĵ���VO
				GeneralBillVO voUpdatedBill = getBillChangedVO();
				voUpdatedBill.setAccreditUserID(sAccreditUserID);
				voUpdatedBill.setTs(voBill);
				voUpdatedBill.setOperatelogVO(log);
				// ִ���޸ı���...�д����׳��쳣
				// ִ���޸ı���
				if (item != null && item.getComponent() != null) voUpdatedBill.setHeaderValue("vdiliveraddress", ((UIRefPane) item.getComponent()).getUITextField().getText());

				saveUpdatedBill(voUpdatedBill);
			} else {
				SCMEnv.out("status invalid...");
				throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000106")/*
																													* @res
																													* "״̬���� "
																													*/);
			}
		} catch (RightcheckException e) {
			showErrorMessage(e.getMessage() + nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000069")/* @res ".\nȨ��У�鲻ͨ��,����ʧ��! " */);
			getAccreditLoginDialog().setCorpID(m_sCorpID);
			getAccreditLoginDialog().clearPassWord();
			if (getAccreditLoginDialog().showModal() == nc.ui.pub.beans.UIDialog.ID_OK) {
				String sUserID = getAccreditLoginDialog().getUserID();
				if (sUserID == null) {
					throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000070")/*
																														* @res
																														* "Ȩ��У�鲻ͨ��,����ʧ��. "
																														*/);
				} else {
					voBill.setAccreditUserID(sUserID);
					onSaveBaseKernel(voBill, sUserID);
				}
			} else {
				throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000070")/*
																													* @res
																													* "Ȩ��У�鲻ͨ��,����ʧ��. "
																													*/);

			}

		} catch (Exception e) {
			throw e;
		}

	}

	/**
	 * �����ߣ����˾� ���ܣ���λָ��
	 * 
	 * ������ ���أ� ���⣺ ���ڣ�(2003-7-2 19:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	protected void onSelLoc() {
		// warehouse id
		String sNewWhID = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem(m_sMainWhItemKey).getComponent()).getRefPK();
		if (sNewWhID == null || sNewWhID.trim().length() == 0) {
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000107")/* @res "����ѡ��ֿ�" */);
		} else {
			getLocSelDlg().setWhID(sNewWhID);
			if (getLocSelDlg().showModal() == LocSelDlg.ID_OK) {

				String cspaceid = getLocSelDlg().getLocID();
				String csname = getLocSelDlg().getLocName();
				// �����ֵ�ݴ�
				Object oTempValue = null;
				// ����model
				nc.ui.pub.bill.BillModel bmBill = getBillCardPanel().getBillModel();
				// ����кţ�Ч�ʸ�һЩ��
				int iInvCol = bmBill.getBodyColByKey(m_sInvMngIDItemKey);

				// �����д����
				if (bmBill != null && iInvCol >= 0 && iInvCol < bmBill.getColumnCount()) {
					// ����
					int iRowCount = getBillCardPanel().getRowCount();
					// �Ӻ���ǰɾ
					for (int line = 0; line < iRowCount; line++) {
						// �������
						oTempValue = bmBill.getValueAt(line, iInvCol);
						if (oTempValue != null && oTempValue.toString().trim().length() > 0) setRowSpaceData(line, cspaceid, csname);
					}
				}

			}
		}

	}

	/**
	 * �ж�ѡ�����Ƿ���׼� �������ڣ�(2004-3-12 21:14:17)
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
																													* "��ѡ��Ҫ����������У�"
																													*/);
			return;
		}

		if (m_sCorpID == null) {
			MessageDialog.showErrorDlg(this, null, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000109")/*
																																* @res
																																* "��ǰ��½��˾IDΪ�գ�"
																																*/);
			return;
		}
		if (sInvID == null || sInvID.trim().length() == 0) {
			MessageDialog.showErrorDlg(this, null, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000110")/*
																																* @res
																																* "ѡ����û�д�����룡"
																																*/);
			return;
		}

		getSetpartDlg().setParam(m_sCorpID, sInvID);
		getSetpartDlg().showSetpartDlg();

	}

	/**
	 * �����ߣ����˾� ���ܣ���ѯָ���ĵ��ݡ� ������ billType, ��ǰ�������� billID, ��ǰ����ID businessType,
	 * ��ǰҵ������ operator, ��ǰ�û�ID pk_corp, ��ǰ��˾ID
	 * 
	 * ���� ������vo ���� �� ���� �� (2001 - 5 - 9 9 : 23 : 32) �޸����� �� �޸��� �� �޸�ԭ�� �� ע�ͱ�־ ��
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

			// ���ý�����
			// getPrgBar(PB_QRY).start();
			long lTime = System.currentTimeMillis();
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000012")/* @res "���ڲ�ѯ�����Ժ�..." */);
			ArrayList alListData = (ArrayList) GeneralBillHelper.queryBills(m_sBillTypeCode, voCond);

			showTime(lTime, "��ѯ");
			// ִ����չ��ʽ.Ŀǰֻ�����۳��ⵥUI����.
			//       
			// execExtendFormula(alListData);
			// //��ʽ��� ��һ�������¼��ʽ��ѯ�������� �޸� hanwei 2003-03-05
			if (alListData != null && alListData.size() > 0) {
				//
				setAlistDataByFormula(GeneralBillVO.QRY_FIRST_ITEM_NUM, alListData);
				SCMEnv.out("0�����ʽ�����ɹ���");
				//
				m_alListData = alListData;
				// //�����
				// // ���� by hanwei 2003-06-17 ,�����ѯ
				// //qryItems(new int[] { 0 }, new String[] { billID });
				// //��ͷִ�й�ʽ
				voRet = (GeneralBillVO) alListData.get(0);
				//
				// GeneralBillHeaderVO voh[] = new GeneralBillHeaderVO[1];
				// if (voRet != null) {
				// voh[0] = (GeneralBillHeaderVO) ((GeneralBillVO) voRet)
				// .getParentVO();
				// setListHeadData(voh);
				// } else
				// SCMEnv
				// .out("nc.ui.ic.pub.bill.GeneralBillClientUI.qryBill:��ͷ����Ϊ�գ�δִ�й�ʽ��");
				//
			}

		} catch (Exception e) {
			handleException(e);
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000015")/* @res "��ѯ����" */
					+ e.getMessage());
		}
		return voRet;
	}

	/**
	 * �����ߣ����˾� ���ܣ���ָ����ŵ��ݵĻ�λ/���к�����,ֻ�������״̬�¡� ������ָ����ѯģʽ ���أ� ���⣺ ���ڣ�(2001-5-9
	 * 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	protected void qryLocSN(int iBillNum, int iMode) {
		GeneralBillVO voMyBill = null;
		// arraylist �еĻ�������,û�л�,�����µ�.
		if (m_alListData != null && m_alListData.size() > iBillNum && iBillNum >= 0) voMyBill = (GeneralBillVO) m_alListData.get(iBillNum);
		qryLocSN(voMyBill, iMode);
	}

	private void qryLocSN(GeneralBillVO voMyBill, int iMode) {

		try {

			// ERRRR,needless to read,���������ݵ������
			if (voMyBill == null || voMyBill.getPrimaryKey() == null) {
				int iFaceRowCount = getBillCardPanel().getRowCount();
				// ��ʼ������ if necessary
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
			// ֻ�����״̬�£��Ż���Ҫ��ֵ�Ͳ�ѯ�������ȡ���������޸ġ�
			if (m_iMode == BillMode.Browse) {
				// ���Դ˵����Ƿ���������������������Ҫ����ִ�У����򵥾ݱ�����û����Щ���ݣ����ö���
				int i = 0, iRowCount = voMyBill.getItemCount();
				// �����Ƿ��Ѿ�������Щ�����ˡ�

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
					// �����кŹ�����е����л�û�����кš�
					if (voInv != null && voInv.getIsSerialMgt() != null && voInv.getIsSerialMgt().intValue() != 0 && voMyBill.getItemValue(i, "serial") == null) {
						hasSN = false;
						break;
					}
				}
				// �Ѿ���������,�����ݷŵ���Ա�����У���ͬ��vo(needless now )2003-08-07
				if (hasLoc) m_alLocatorData = voMyBill.getLocators();
				if (hasSN) m_alSerialData = voMyBill.getSNs();
				if (hasLoc && hasSN) return;

				// =============================================================
				// ��ʼ������ if necessary
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
					// ����ʵ��/������
					if (voMyBill.getItemValue(i, "ninnum") != null && voMyBill.getItemValue(i, "ninnum").toString().length() > 0 || voMyBill.getItemValue(i, "noutnum") != null && voMyBill.getItemValue(i, "noutnum").toString().length() > 0) break;

				if (i >= iRowCount) // ������
				return; // =============================================================

				// ����ջ�λ�����к�����
				Integer iSearchMode = null;
				// ��Ҫ���λ
				if (!hasLoc && (iMode == QryInfoConst.LOC_SN || iMode == QryInfoConst.LOC)) {
					iSearchMode = new Integer(iMode);
				}
				// ��Ҫ�����к�
				if (!hasSN && (iMode == QryInfoConst.LOC_SN || iMode == QryInfoConst.SN)) {
					iSearchMode = new Integer(iMode);
				}
				if (iSearchMode == null) return;
				// WhVO voWh = voMyBill.getWh();
				// ��λ����Ĳֿ⣬���һ�û�л�λ����Ҫ����λ���ݡ����к�

				// iMode = 3;
				// Integer iSearchMode = new Integer(iMode);

				// ////////////////////////////iMode); //���λ & ���к� 3 or ���к� 4
				ArrayList alAllData = (ArrayList) GeneralBillHelper.queryInfo(iSearchMode, voMyBill.getPrimaryKey());
				// ����ջ�λ�����к�����
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

				// ����еĻ��û�λ����
				if (alTempLocatorData != null) {
					// �ŵ�vo�У����ݱ���idִ������ƥ�䡣
					voMyBill.setLocators(alTempLocatorData);
					// getLocators������ by hanwei 2004-01-06
					m_alLocatorData = voMyBill.getLocators();
				}
				// ����еĻ������к�����
				if (alTempSerialData != null) {
					// �ŵ�vo�У����ݱ���idִ������ƥ�䡣
					voMyBill.setSNs(alTempSerialData);
					// getSNs������ by hanwei 2004-01-06
					m_alSerialData = voMyBill.getSNs();

				}

			}

		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
		}

	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-2-10 9:11:33)
	 * 
	 * @param iBillNum
	 *            int
	 * @param iMode
	 *            int ר�����б��±������������ݴ�ӡ��λ�Ĳ���
	 */
	public void qryLocSNSort(int iBillNum, int iMode) {
		try {
			GeneralBillVO voMyBill = null;
			// arraylist �еĻ�������,û�л�,�����µ�.
			// ��������Ź���������������ݷ���ȡȫ�ֱ����е�����
			if (m_sLastKey != null && m_voBill != null) voMyBill = m_voBill;
			else if (m_alListData != null && m_alListData.size() > iBillNum && iBillNum >= 0) voMyBill = (GeneralBillVO) m_alListData.get(iBillNum);

			// ERRRR,needless to read,���������ݵ������
			if (voMyBill == null || voMyBill.getPrimaryKey() == null) {
				int iFaceRowCount = getBillCardPanel().getRowCount();
				// ��ʼ������ if necessary
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
			// ֻ�����״̬�£��Ż���Ҫ��ֵ�Ͳ�ѯ�������ȡ���������޸ġ�
			if (m_iMode == BillMode.Browse) {
				// ���Դ˵����Ƿ���������������������Ҫ����ִ�У����򵥾ݱ�����û����Щ���ݣ����ö���
				int i = 0, iRowCount = voMyBill.getItemCount();
				// �����Ƿ��Ѿ�������Щ�����ˡ�

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
					// �����кŹ�����е����л�û�����кš�
					if (voInv != null && voInv.getIsSerialMgt() != null && voInv.getIsSerialMgt().intValue() != 0 && voMyBill.getItemValue(i, "serial") == null) {
						hasSN = false;
						break;
					}
				}
				// �Ѿ���������,�����ݷŵ���Ա�����У���ͬ��vo(needless now )2003-08-07
				if (hasLoc) m_alLocatorData = voMyBill.getLocators();
				if (hasSN) m_alSerialData = voMyBill.getSNs();
				if (hasLoc && hasSN) return;

				// =============================================================
				// ��ʼ������ if necessary
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
					// ����ʵ��/������
					if (voMyBill.getItemValue(i, "ninnum") != null && voMyBill.getItemValue(i, "ninnum").toString().length() > 0 || voMyBill.getItemValue(i, "noutnum") != null && voMyBill.getItemValue(i, "noutnum").toString().length() > 0) break;

				if (i >= iRowCount) // ������
				return; // =============================================================

				// ����ջ�λ�����к�����
				Integer iSearchMode = null;
				// ��Ҫ���λ
				if (!hasLoc && (iMode == QryInfoConst.LOC_SN || iMode == QryInfoConst.LOC)) {
					iSearchMode = new Integer(iMode);
				}
				// ��Ҫ�����к�
				if (!hasSN && (iMode == QryInfoConst.LOC_SN || iMode == QryInfoConst.SN)) {
					iSearchMode = new Integer(iMode);
				}
				if (iSearchMode == null) return;
				// WhVO voWh = voMyBill.getWh();
				// ��λ����Ĳֿ⣬���һ�û�л�λ����Ҫ����λ���ݡ����к�

				// iMode = 3;
				// Integer iSearchMode = new Integer(iMode);

				// ////////////////////////////iMode); //���λ & ���к� 3 or ���к� 4
				ArrayList alAllData = (ArrayList) GeneralBillHelper.queryInfo(iSearchMode, voMyBill.getPrimaryKey());
				// ����ջ�λ�����к�����
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

				// ����еĻ��û�λ����
				if (alTempLocatorData != null) {
					// �ŵ�vo�У����ݱ���idִ������ƥ�䡣
					voMyBill.setLocators(alTempLocatorData);
					// getLocators������ by hanwei 2004-01-06
					m_alLocatorData = voMyBill.getLocators();
				}
				// ����еĻ������к�����
				if (alTempSerialData != null) {
					// �ŵ�vo�У����ݱ���idִ������ƥ�䡣
					voMyBill.setSNs(alTempSerialData);
					// getSNs������ by hanwei 2004-01-06
					m_alSerialData = voMyBill.getSNs();

				}

			}

		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
		}

	}

	/**
	 * ������Դ������ʽ���ɵĿ�浥����Ҫ���ñ�ͷItem, �Ա㽫name��ʾ���б����ʹ�ӡ�еõ�item������. ��������: �������: ����ֵ:
	 * �쳣����: ����:
	 */
	public void resetAllHeaderRefItem() {
		if (getBillCardPanel().getHeadItem("cdispatcherid") != null) {
			// �շ����
			String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("cdispatcherid").getComponent()).getRefName();
			// �������������б���ʽ����ʾ��
			if (m_voBill != null) m_voBill.setHeaderValue("cdispatchername", sName);
		}
		if (getBillCardPanel().getHeadItem("cinventoryid") != null) {
			// �ӹ�Ʒ
			String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("cinventoryid").getComponent()).getRefName();
			// �������������б���ʽ����ʾ��
			if (m_voBill != null) m_voBill.setHeaderValue("cinventoryname", sName);
		}
		// ����¼��ʱ, ���ÿ����֯afterEdit������ڲֿ��ǰ��
		String sNewWhID = null;
		String sNewWhName = null;
		if (m_voBill.getHeaderValue(m_sMainWhItemKey) != null) {

			sNewWhName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem(m_sMainWhItemKey).getComponent()).getRefName();
			sNewWhID = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem(m_sMainWhItemKey).getComponent()).getRefPK();

		}

		if (getBillCardPanel().getHeadItem(m_sMainCalbodyItemKey) != null) {
			// �����֯
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
			// ���Ա

			String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("cwhsmanagerid").getComponent()).getRefName();
			// �������������б���ʽ����ʾ��
			if (m_voBill != null) m_voBill.setHeaderValue("cwhsmanagername", sName);
		}
		if (getBillCardPanel().getHeadItem("cdptid") != null) {
			// ����
			String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("cdptid").getComponent()).getRefName();
			// �������������б���ʽ����ʾ��
			if (m_voBill != null) m_voBill.setHeaderValue("cdptname", sName);
		}
		if (getBillCardPanel().getHeadItem("cbizid") != null) {
			// ҵ��Ա

			String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("cbizid").getComponent()).getRefName();
			// �������������б���ʽ����ʾ��
			if (m_voBill != null) m_voBill.setHeaderValue("cbizname", sName);
		}
		if (getBillCardPanel().getHeadItem("cproviderid") != null) {
			// ��Ӧ��

			String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("cproviderid").getComponent()).getRefName();
			String sRefPK = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("cproviderid").getComponent()).getRefPK();
			// �������������б���ʽ����ʾ��
			if (m_voBill != null) m_voBill.setHeaderValue("cprovidername", sName);
			// ���ݿͻ���Ӧ�̹��˷��˵�ַ�Ĳ���
			if (getBillCardPanel().getHeadItem("vdiliveraddress") != null && sRefPK != null) {

				((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("vdiliveraddress").getComponent()).setWhereString("pk_cubasdoc = (select pk_cubasdoc from bd_cumandoc where pk_cumandoc = '" + sRefPK + "')");
			}
		}
		if (getBillCardPanel().getHeadItem("ccustomerid") != null) {
			// �ͻ�

			String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("ccustomerid").getComponent()).getRefName();
			String sRefPK = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("ccustomerid").getComponent()).getRefPK();
			// �������������б���ʽ����ʾ��
			if (m_voBill != null) m_voBill.setHeaderValue("ccustomername", sName);
			// ���ݿͻ���Ӧ�̹��˷��˵�ַ�Ĳ���

			filterVdiliveraddressRef(true, -1);

			if (getBillCardPanel().getHeadItem("vdiliveraddress") != null && sRefPK != null) {
				((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("vdiliveraddress").getComponent()).setWhereString("pk_cubasdoc = (select pk_cubasdoc from bd_cumandoc where pk_cumandoc = '" + sRefPK + "')");
			}
		}
		if (getBillCardPanel().getHeadItem("cbiztype") != null) {
			// ҵ������

			String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("cbiztype").getComponent()).getRefName();
			// String sPK = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
			// .getHeadItem("cbiztype").getComponent()).getRefPK();
			// �������������б���ʽ����ʾ��
			if (m_voBill != null) m_voBill.setHeaderValue("cbiztypename", sName);
		}
		if (getBillCardPanel().getHeadItem("cdilivertypeid") != null) {
			// ���˷�ʽ

			String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("cdilivertypeid").getComponent()).getRefName();
			// �������������б���ʽ����ʾ��
			if (m_voBill != null) m_voBill.setHeaderValue("cdilivertypename", sName);
		}
		if (getBillCardPanel().getHeadItem("vdiliveraddress") != null) {
			// ���˵�ַ

			String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("vdiliveraddress").getComponent()).getUITextField().getText();
			;
			// �������������б���ʽ����ʾ��
			if (m_voBill != null) m_voBill.setHeaderValue("vdiliveraddressname", sName);
		}
	}

	/**
	 * �˴����뷽��˵���� ���µ��� �������ڣ�(2002-11-27 10:32:34)
	 * 
	 * @return int
	 * @param vo
	 *            nc.vo.ic.pub.barcodeparse.BarCodeParseVO
	 */
	public void scanAddBarcodeline(nc.vo.ic.pub.barcodeparse.BarCodeParseVO vo) throws Exception {

		if (vo == null) return;

		String sRowPrimaryKey = getBarcodeCtrl().getBarcodeRowPrimaryKey(m_sCorpID, vo);

		// ͨ����������ж���Ĺؼ�����
		String[] sPrimaryKeyItems = vo.getMatchPrimaryKeyItems();

		BarCodeParseVO[] barCodeParseVOs = new BarCodeParseVO[] {
			vo
		};
		boolean bBox = false;
		scanadd(sRowPrimaryKey, barCodeParseVOs, bBox, sPrimaryKeyItems);
		// ��������ɨ���
		m_utfBarCode.requestFocus();
		return;
	}

	/**
	 * �˴����뷽��˵���� ������ɨ����������� �������ڣ�(2004-3-12 15:57:22)
	 * 
	 * @param vo
	 *            nc.vo.ic.pub.barcodeparse.BarCodeGroupVO
	 */
	public void scanAddBoxline(BarCodeGroupVO barCodeGroupVO) throws Exception {

		BarCodeGroupHeadVO barCodeGroupHeadVO = (BarCodeGroupHeadVO) barCodeGroupVO.getParentVO();

		BarCodeParseVO[] barCodeParseVOs = (BarCodeParseVO[]) barCodeGroupVO.getChildrenVO();
		if (barCodeParseVOs == null || barCodeParseVOs.length == 0) { return; }

		// ��Ҫͨ��barCodeGroupHeadVO��ùؼ�����
		// Ŀǰ����װ��û�����ô�������������
		// �������óɴ������
		String[] sPrimaryKeyItems = new String[] {
			nc.vo.ic.pub.barcodeparse.BarcodeparseCtrl.InvManKey
		};

		String sRowPrimaryKey = getBarcodeCtrl().getBarGroupHeadRowPrimaryKey(m_sCorpID, barCodeGroupHeadVO, sPrimaryKeyItems);
		boolean bBox = true;
		scanadd(sRowPrimaryKey, barCodeParseVOs, bBox, sPrimaryKeyItems);
		// ��������ɨ���
		m_utfBarCode.requestFocus();
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-3-12 22:26:14)
	 * 
	 * @param sRowPrimaryKey
	 *            java.lang.String
	 * @param barCodeParseVOs
	 *            nc.vo.ic.pub.barcodeparse.BarCodeParseVO[]
	 */
	protected void scanadd(String sRowPrimaryKey, BarCodeParseVO[] barCodeParseVOs, boolean bBox, String[] sPrimaryKeyItems) throws Exception {
		try {

			if (sRowPrimaryKey != null && barCodeParseVOs != null && sRowPrimaryKey.length() > 4 && !sRowPrimaryKey.startsWith("NULL")) // �������д��ڴ����Ϣ
			{
				// ȡ��ǰ��
				int iRow = getBillCardPanel().getBillTable().getSelectedRow();

				// �������в����з�����������arraylist ,���ȴ���ѡ����
				ArrayList alResultTemp = getBarcodeCtrl().scanBillCardItem(sRowPrimaryKey, m_voBill, iRow, sPrimaryKeyItems);

				ArrayList alResult = new ArrayList();

				// �������룬ֻ���õ�һ�У�����ʵ����������У��
				// �����޷�������������������쳣�ع�
				if (bBox && alResultTemp != null && alResultTemp.size() > 0) {
					alResult.add(alResultTemp.get(0));
				} else alResult = alResultTemp;

				// ���arraylistΪ�գ���len==0����ʾû�ж�Ӧ�������
				if ((alResult == null || alResult.size() == 0)) {
					// (1)�Ե�������ⵥ�������Զ������е������
					// (2)��������������������ϣ�Ҳ������ɨ���Զ�������
					if (getButtonTree().getButton(ICButtonConst.BTN_LINE_ADD) != null && getButtonTree().getButton(ICButtonConst.BTN_LINE_ADD).isEnabled() && getBarcodeCtrl().isAddNewInvLine()) {
						// û�ж�Ӧ�Ĵ�����Զ�����һ�д��
						java.util.ArrayList alInvID = new java.util.ArrayList();
						alInvID.add(sRowPrimaryKey);
						int iCurFixLine = getBarcodeCtrl().fixBlankLineWithInv(this, m_voBill, alInvID, m_sInvMngIDItemKey, m_sMainWhItemKey, m_sMainCalbodyItemKey, m_sBillTypeCode, m_sBillRowNo, sPrimaryKeyItems);
						// �ûع��
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
																														* "ɨ��ʶ����µĴ�����룬����ǰ���ݽ��治���������Ӵ���У�"
																														*/);

					}
				} else {
					int icurline = Integer.parseInt(alResult.get(0).toString());
					getBillCardPanel().getBillTable().getSelectionModel().setSelectionInterval(icurline, icurline);
					scanUpdateLine(barCodeParseVOs, alResult);
				}
			} else // �����в����ڴ����Ϣ
			{
				// ��Ҫ�ҵ�ǰ�����У�����ɨ�账��
				scanUpdateLineSelect(barCodeParseVOs);
			}
		} catch (Exception e) {
			throw nc.ui.ic.pub.tools.GenMethod.handleException(null, null, e);
		}
	}

	/**
	 * ����˵�������˵�ַ�޸ĺ��� �������ڣ�(2005-09-15 14:12:13) ���ߣ�yb �޸����ڣ� �޸��ˣ� �޸�ԭ�� �㷨˵����
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
	 * ���˷��˵�ַ���� �����ߣ����� ���ܣ���ʼ�����չ��� ������ ���أ� ���⣺ ���ڣ�(2001-7-17 10:33:20)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 */
	public void filterVdiliveraddressRef(boolean ishead, int row) {
		try {
			// ���˱�ͷ���˵�ַ
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
	 * �˴����뷽��˵���� ������ʹ�ã���������༭�޸�������Ӧ��������������ϵ��ҵ���߼� �������ڣ�(2004-4-29 9:08:49)
	 * 
	 * @param event1
	 *            nc.ui.pub.bill.BillEditEvent
	 */
	public void scanCheckNumEdit(nc.ui.pub.bill.BillEditEvent event1) throws Exception {

	}

	/**
	 * �˴����뷽��˵���� ������VO������䵽���� �������ڣ�(2004-3-12 20:15:07)
	 * 
	 * @return int
	 * @param barCodeParseVOs
	 *            nc.vo.ic.pub.barcodeparse.BarCodeParseVO[]���������� VO
	 * 
	 * @param iCurFixLine
	 *            int����ǰ����,
	 * 
	 * @param iNumUsed
	 *            int���Ѿ����ʹ�ù�����������
	 * 
	 * @param bAllforFix
	 *            java.lang.Boolean ���Ƿ�Ҫ��ʣ������붼����
	 * 
	 *            ����ֻ��һ�з��������Ĵ����������һ������£�Ҫ��ʣ������붼����
	 * 
	 *            ���ر���������������
	 */
	public int scanfixline(BarCodeParseVO[] barCodeParseVOs, int iCurFixLine, int iNumUsed, boolean bAllforFix) throws Exception {

		if (barCodeParseVOs == null || barCodeParseVOs.length == 0 || iCurFixLine < 0) { return 0; }
		String sInvID = (String) getBillCardPanel().getBodyValueAt(iCurFixLine, m_sInvMngIDItemKey);
		if (sInvID == null || sInvID.length() == 0) { throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000111")/* @res "��ѡ���д�����ݵ��У�" */); }
		// �������
		int iNumforUse = scanfixline_fix(barCodeParseVOs, iCurFixLine, iNumUsed, bAllforFix); // ����������������

		// ��������
		getBarcodeCtrl().scanfixline_save(barCodeParseVOs, iCurFixLine, iNumUsed, iNumforUse, m_voBill.getItemVOs()); // ����������������

		return iNumforUse;
	}

	/**
	 * �˴����뷽��˵���� ������VO������䵽���� �������ڣ�(2004-3-12 20:15:07)
	 * 
	 * @return int
	 * @param barCodeParseVOs
	 *            nc.vo.ic.pub.barcodeparse.BarCodeParseVO[]���������� VO
	 * 
	 * @param iCurFixLine
	 *            int����ǰ����,
	 * 
	 * @param iNumUsed
	 *            int���Ѿ����ʹ�ù�����������
	 * 
	 * @param bAllforFix
	 *            java.lang.Boolean ���Ƿ�Ҫ��ʣ������붼����
	 * 
	 *            ����ֻ��һ�з��������Ĵ����������һ������£�Ҫ��ʣ������붼����
	 * 
	 *            ���ر���������������
	 */
	protected int scanfixline_fix(BarCodeParseVO[] barCodeParseVOs, int iCurFixLine, int iNumUsed, boolean bAllforFix) throws Exception {

		if (barCodeParseVOs == null || barCodeParseVOs.length == 0 || iCurFixLine < 0) { return 0; }

		int iNumforUse = 0; // ����������������
		if (getBillCardPanel().getBodyItem("cinventorycode") != null) {
			// ʵ�ʷ�����
			UFDouble nFactNum = null;
			// Ӧ������
			UFDouble nShouldNum = null;
			UFDouble nFactBarCodeNum = null; // ʵ�ʷ���ʵ������������

			nc.vo.ic.pub.bc.BarCodeVO[] oldBarcodevos = m_voBill.getItemVOs()[iCurFixLine].getBarCodeVOs();

			if (oldBarcodevos == null || oldBarcodevos.length == 0) nFactBarCodeNum = UFDZERO;
			else {
				nFactBarCodeNum = UFDZERO;
				for (int i = 0; i < oldBarcodevos.length; i++) {
					if (oldBarcodevos[i] != null && oldBarcodevos[i].getNnumber() != null) nFactBarCodeNum = nFactBarCodeNum.add(oldBarcodevos[i].getNnumber());
				}
			}

			// ʵ�ʷ�����
			Object oNum = getBillCardPanel().getBodyValueAt(iCurFixLine, m_sNumItemKey);
			if (oNum == null || oNum.toString().trim().length() == 0) {
				nFactNum = null;
				// ���û��Ӧ�������������ȫ������
			} else nFactNum = (UFDouble) oNum;

			// Ӧ������

			try {
				oNum = getBillCardPanel().getBodyValueAt(iCurFixLine, m_sShouldNumItemKey);
			} catch (Exception e) {
				oNum = null;
				nc.vo.scm.pub.SCMEnv.error(e);
			}
			if (oNum == null || oNum.toString().trim().length() == 0) {
				nShouldNum = null;
				// ���û��Ӧ�������������ȫ������
			} else nShouldNum = (UFDouble) oNum;

			boolean bNegative = false; // �Ƿ���
			if ((nFactNum != null && nFactNum.doubleValue() < 0) || (nShouldNum != null && nShouldNum.doubleValue() < 0)) {
				bNegative = true;
			}

			// �����������ݵ����ƥ���е��㷨
			iNumforUse = getBarcodeCtrl().scanfixlinenum(barCodeParseVOs, oldBarcodevos, iCurFixLine, iNumUsed, bAllforFix, nFactNum, nShouldNum);

			nFactBarCodeNum = nFactBarCodeNum.add(iNumforUse);

			// add by hanwei 2004-6-2
			// ������������Ӧ������,�������̵㵥���ɵ�����������ϼ
			// ���ܳ���Ӧ�������������޸ĵ�ʵ����������Ӧ������
			if (nShouldNum != null && nFactBarCodeNum != null && nFactBarCodeNum.doubleValue() > nShouldNum.doubleValue() && !getBarcodeCtrl().isOverShouldNum()) {
				nFactBarCodeNum = nShouldNum.abs();
			}

			if (nFactNum == null) nFactNum = UFDZERO;

			// ʵ������С��������������ȥ�޸Ľ����ϵ�ʵ������
			if (nFactNum.doubleValue() < nFactBarCodeNum.doubleValue()) {

				// ͬ��ʵ������
				if (bNegative || m_bFixBarcodeNegative) nFactBarCodeNum = nFactBarCodeNum.multiply(UFDNEGATIVE);

				getBillCardPanel().setBodyValueAt(nFactBarCodeNum, iCurFixLine, m_sNumItemKey);

				if (getBillCardPanel().getBodyItem("nbarcodenum") != null) getBillCardPanel().setBodyValueAt(nFactBarCodeNum, iCurFixLine, "nbarcodenum");

				if (getBillCardPanel().getBodyItem("m_sNumbarcodeItemKey") != null) getBillCardPanel().setBodyValueAt(nFactBarCodeNum, iCurFixLine, m_sNumbarcodeItemKey);

				// Ӧ����������ͬ��
				nc.ui.pub.bill.BillEditEvent event1 = new nc.ui.pub.bill.BillEditEvent(getBillCardPanel().getBodyItem(m_sNumItemKey), nFactBarCodeNum, m_sNumItemKey, iCurFixLine);
				// ��������༭ҵ���߼�
				scanCheckNumEdit(event1);
				afterEdit(event1);
				// ִ��ģ�湫ʽ
				getGenBillUICtl().execEditFormula(getBillCardPanel(), iCurFixLine, m_sNumItemKey);

				// ����������״̬Ϊ�޸�
				if (getBillCardPanel().getBodyValueAt(iCurFixLine, m_sBillRowItemKey) != null) getBillCardPanel().getBillModel().setRowState(iCurFixLine, BillMode.Update);
			}

		}

		return iNumforUse;
	}

	/**
	 * �˴����뷽��˵���� ���и����������ݣ� �Գ���Ӧ�������������������Ƶ���һ�и���
	 * 
	 * barCodeParseVOs:��������VO[] alFixRowNO��ArrayList��ÿ�д��д���������ݣ�String ���� int
	 * ������
	 * 
	 * �������ڣ�(2004-3-12 19:23:05)
	 * 
	 * @param barCodeParseVOs
	 *            nc.vo.ic.pub.barcodeparse.BarCodeParseVO[]
	 * @param alFixRowNO
	 *            java.util.ArrayList
	 */
	protected void scanUpdateLine(BarCodeParseVO[] barCodeParseVOs, ArrayList alFixRowNO) throws Exception {

		if (barCodeParseVOs == null || barCodeParseVOs.length == 0 || alFixRowNO == null || alFixRowNO.size() == 0) { return; }

		int iNumAll = barCodeParseVOs.length;
		int iNumUsed = 0; // �ۼ�ͳ������
		int ifixRows = alFixRowNO.size();
		int iCurFixLine = 0; // ���µ�ǰ��
		int ifixSingleLineNum = 0;

		for (int i = 0; i < ifixRows; i++) {
			iCurFixLine = Integer.parseInt((String) alFixRowNO.get(i));

			if (ifixRows == 1) {
				// ֻ��һ�У�ȫ����䵱ǰ��
				ifixSingleLineNum = scanfixline(barCodeParseVOs, iCurFixLine, 0, true);
				break;
			} else {
				if (i == ifixRows - 1) // ���һ�У�������е�����
				{
					ifixSingleLineNum = scanfixline(barCodeParseVOs, iCurFixLine, iNumUsed, true);
					break;
				} else // �м������Ӧ��������
				{
					ifixSingleLineNum = scanfixline(barCodeParseVOs, iCurFixLine, iNumUsed, false);
				}
				iNumUsed = iNumUsed + ifixSingleLineNum;
				if (iNumUsed == iNumAll) {
					// ��������
					break;
				}
			}

		}
	}

	/**
	 * �˴����뷽��˵���� ������VO������䵽����Ľ����� �������ڣ�(2004-3-12 20:15:07)
	 * 
	 * @return int
	 * @param barCodeParseVOs
	 *            nc.vo.ic.pub.barcodeparse.BarCodeParseVO[]���������� VO
	 */
	protected void scanUpdateLineSelect(BarCodeParseVO[] barCodeParseVOs) throws Exception {
		// ȡ��ǰ��
		int iCurFixLine = 0;
		int rowNow = getBillCardPanel().getBillTable().getSelectedRow();
		if (rowNow < 0) {
			// ��ʾ������Ϣ
			throw new nc.vo.pub.BusinessException(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000112")/*
																																 * @res
																																 * "��ѡ���Ӧ����Ĵ���У�"
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
																																	* @res "��ǰ�з������������������ѹرգ���"
																																	*/);
			}
		}
	}

	/**
	 * �˴����뷽��˵���� ��������:����ҳ�� ���ߣ������� �������:��ǰҳ�롣 ����ֵ: �쳣����: ����:(2003-7-5 13:14:52)
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
	 * �����ߣ����˾� ���ܣ������޸ĺ����������е�PK,��ͬʱˢ�´����VO. Ҫ��֤VO��Item��˳��ͽ�������һ�¡� ������ ���أ� ���⣺
	 * ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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

			// ����ñ�����������,������ID����BarCodeVO��
			if (m_voBill.getItemVOs()[row].getBarCodeVOs() != null) {
				for (int j = 1; j <= m_voBill.getItemVOs()[row].getBarCodeVOs().length; j++) {
					if (m_voBill.getItemVOs()[row].getBarCodeVOs()[j - 1] != null && m_voBill.getItemVOs()[row].getBarCodeVOs()[j - 1].getStatus() == nc.vo.pub.VOStatus.NEW) {
						// ������������
						m_voBill.getItemVOs()[row].getBarCodeVOs()[j - 1].setPrimaryKey(alBodyPK.get(pk_count + 1).toString().trim());
						// ���ñ�������
						m_voBill.getItemVOs()[row].getBarCodeVOs()[j - 1].setCgeneralbid(m_voBill.getItemVOs()[row].getCgeneralbid());
					}
				}

			}
		}
	}

	/**
	 * �����ߣ�zhx ���ܣ��ڱ���������VO��״̬ ������ ���أ� ���⣺ ���ڣ�(2003-10-21 9:23:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
	 * �˴����뷽��˵���� �Ѷ��ŵ���VO�ϲ���һ�� �Բɹ���ⵥ�ݣ���Ҫ���ر����� �������ڣ�(2004-3-17 15:35:51)
	 * 
	 * @return nc.vo.ic.pub.bill.GeneralBillVO
	 * @param vos
	 *            nc.vo.pub.AggregatedValueObject[]
	 */
	// protected GeneralBillVO setBillRefResultCombinVo(
	// nc.vo.pub.AggregatedValueObject[] vos) throws Exception {
	//
	// nc.ui.ic.pub.pfconv.VoHandle handle = new nc.ui.ic.pub.pfconv.VoHandle();
	// // Ĭ�ϼ������֯����ͷ�ķ�ʽ������
	// GeneralBillVO voRet = handle.combinVo(vos, true);
	//
	// return voRet;
	// }
	/**
	 * �˴����뷽��˵���� ����ʽ�����ϵ����ݼ��ص���浥�ݽ����� BusiTypeID��ҵ������ID,���û��Ϊnull
	 * vos:���ݵ�AggregatedValueObject[]��ʵ������VO����ͨ���ݵ�VO
	 * Ŀǰ�÷�������ͨ���ݻ�����Ͳɹ���⡢ί��ӹ���������� �������ڣ�(2003-10-14 14:29:30)
	 * 
	 * @param BusiTypeID
	 *            java.lang.String
	 * @param vos
	 *            nc.vo.pub.AggregatedValueObject[]
	 */
	protected void setBillRefResultVO(String sBusiTypeID, nc.vo.pub.AggregatedValueObject[] vos) throws Exception {

		if (vos == null || vos.length == 0) throw new BusinessException(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UCH003")/* @res "��ѡ��Ҫ��������ݣ�" */);

		if (vos != null && !(vos instanceof GeneralBillVO[])) { throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000114")/*
																																									* @res
																																									* "��������ת��������鿴����̨����ϸ��ʾ��Ϣ��"
																																									*/); }

		// v5 ��������VO��������֤VO����ȷ�ԣ����ĳЩ�ֶ�ֵ
		GeneralBillVO voRet = VoCombine.combinVo(vos);

		if (voRet != null && voRet.getItemVOs() != null && voRet.getItemVOs().length > 0) {

			// set user selected ҵ������ 20021209
			voRet.setHeaderValue("cbiztype", sBusiTypeID);
			// ͨ�����ݹ�ʽ������ִ���йع�ʽ�����ķ���
			getFormulaBillContainer().formulaHeaders(getFormulaItemHeader(), voRet.getHeaderVO());
			BatchCodeDefSetTool.execFormulaForBatchCode(voRet.getItemVOs());

			// ���洫��ĵ���VO�����滻�����մ���Ĵ��IDʼ���Ǹõ���VO�д��ID��
			m_voBillRefInput = voRet;
			// lTime = System.currentTimeMillis();
			// ��������1
			onAdd(false, null);
			
			// �����˿�״̬��ֻ�����븺�� add by hanwei 2004-05-08
			if (voRet.getHeaderVO() != null && voRet.getHeaderVO().getFreplenishflag().booleanValue()) {
				nc.ui.ic.pub.bill.GeneralBillUICtl.setSendBackBillState(this, true);
			} else nc.ui.ic.pub.bill.GeneralBillUICtl.setSendBackBillState(this, false);

			// ��յ��ݺ�
			voRet.getHeaderVO().setPrimaryKey(null);
			voRet.getHeaderVO().setVbillcode(null);

			// ���ӵ����кţ�zhx added on 20030630 support for incoming bill
			nc.ui.scm.pub.report.BillRowNo.setVORowNoByRule(voRet, m_sBillTypeCode, m_sBillRowNo);

			// ����ֿ�����
			resetWhInfo(voRet);

			// �������д������
			resetAllInvInfo(voRet);

			// �����������ݳ�ʼ���ݣ���ΪsetTempBillVO����������ˡ�
			int iOriginalItemCount = voRet.getItemCount();
			// �˵�������С�
			GeneralBillItemVO[] itemvo = m_voBill.filterItem();
			if (itemvo == null || itemvo.length == 0) {
				m_voBill = null;
				clearUi();
				m_voBillRefInput = null;
				throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000115")/*
																													* @res
																													* "���յ����д������Ϊ�ۿۻ��������ԵĴ�������޸Ĵ�����ٹ���¼�롣"
																													*/);

			} else if (iOriginalItemCount > itemvo.length) { // ����˵�����
				m_voBill.setChildrenVO(itemvo);
				setImportBillVO(m_voBill, false);
				m_voBillRefInput.setChildrenVO(itemvo);

			}

			// ������������������״̬ �޸���:������ �޸�����:2007-04-04
			if (m_voBill.getItemVOs() != null) {

				for (int i = 0; i < m_voBill.getItemVOs().length; i++) {

					if (m_voBill.getItemVOs()[i].getBarCodeVOs() != null) {
						nc.vo.ic.pub.SmartVOUtilExt.modifiVOStatus(m_voBill.getItemVOs()[i].getBarCodeVOs(), nc.vo.pub.VOStatus.NEW);
					}
				}
			}

			setNewBillInitData();

			// /�ֹ�֮���˵�ַ��
			setDeliverAddressByHand();

			setButtonStatus(true);

			ctrlSourceBillUI(true);

			// set user selected ҵ������ 20021209
			if (getBillCardPanel().getHeadItem("cbiztype") != null && sBusiTypeID != null) {
				getBillCardPanel().setHeadItem("cbiztype", sBusiTypeID);

				nc.ui.pub.bill.BillEditEvent event = new nc.ui.pub.bill.BillEditEvent(getBillCardPanel().getHeadItem("cbiztype"), sBusiTypeID, "cbiztype");
				afterEdit(event);
			}
			// end set user selected ҵ������

			// showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
			// "SCMCOMMON", "UPPSCMCommon-000133")/* @res "����" */);

		} else {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000116")/*
																											* @res
																											* "��˫��ѡ�����¼�뵥�ݵı�ͷ�������У�"
																											*/);
		}

	}

	/*
	 * ��Ե��ݣ���������ⵥ �������ã� 1 ���õ���ֿ�-4Y /�����ֿ�-4E ���չ��� 2 �Ե������ⵥ������ջ���λ����������
	 * 
	 * �۱� on Jun 13, 2005
	 */
	protected void setBillRefIn4Eand4Y(GeneralBillVO voBill) {
		nc.ui.pub.bill.BillItem bi = null;

		// ����ֿ�-4Y /�����ֿ�-4E ����

		bi = getBillCardPanel().getHeadItem("cotherwhid");

		if (bi != null) {
			nc.ui.pub.beans.UIRefPane ref = (nc.ui.pub.beans.UIRefPane) bi.getComponent();
			if (ref != null) {
				ref.getRefModel().setPk_corp((String) voBill.getHeaderValue("cothercorpid"));

				int fallocflag = CONST.IC_ALLOCINSTORE; // ������
				String isDirectStor = "N";

				if (voBill.getHeaderValue("fallocflag") != null) fallocflag = voBill.getHeaderVO().getFallocflag().intValue();

				if (fallocflag == CONST.IC_ALLOCDIRECT) // ��Ϊֱ�˵���
				isDirectStor = "Y";

				ref.setWhereString(" pk_calbody ='" + (String) voBill.getHeaderValue("cothercalbodyid") + "' and isdirectstore='" + isDirectStor + "'");
			}
		}
		Object coutcorpid = voBill.getHeaderVO().getAttributeValue("coutcorpid");

		bi = getBillCardPanel().getHeadItem("cproviderid");

		if (bi != null) ((nc.ui.pub.beans.UIRefPane) bi.getComponent()).getRefModel().setPk_corp(coutcorpid.toString());

		bi = getBillCardPanel().getHeadItem("ccustomerid");

		if (bi != null) ((nc.ui.pub.beans.UIRefPane) bi.getComponent()).getRefModel().setPk_corp(coutcorpid.toString());

		// �Ե������ⵥ������ջ���λ���������ˡ�
		if (!m_sBillTypeCode.equals("4Y")) // �ǵ������ⵥ
		return;
		// �ջ���λ
		bi = getBillCardPanel().getBodyItem("vrevcustname");
		if (bi != null) {
			nc.ui.pub.beans.UIRefPane ref = (nc.ui.pub.beans.UIRefPane) bi.getComponent();
			if (ref != null) ref.getRefModel().setPk_corp((String) voBill.getHeaderValue("cothercorpid"));
		}
	}

	/**
	 * �����ߣ����˾� ���ܣ��ڱ�������ʾVO ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	public void setBillVO(GeneralBillVO bvo, boolean bUpdateBotton, boolean bExeFormule) {
		// ����ǿգ������ʾ
		if (bvo == null) {
			getBillCardPanel().getBillData().clearViewData();
			getBillCardPanel().updateValue();
			return;
		}

		try {
			long lTime = System.currentTimeMillis();
			getBillCardPanel().getBillModel().removeTableModelListener(this);
			getBillCardPanel().removeBillEditListenerHeadTail();
			// ����һ��clone()
			m_voBill = (GeneralBillVO) bvo.clone();

			// ��������ⵥ�������������⴦��
			// ��Ӧ���ڻ��෽���С�
			// shawbing on Jun 13, 2005
			if (m_sBillTypeCode.equals("4Y") || m_sBillTypeCode.equals("4E")) setBillRefIn4Eand4Y(m_voBill);

			// ʹ�ù�ʽ��ѯ������Ҫ�ֹ�������������
			// ��Ҫ�Ļ���������������
			if (m_bIsByFormula) bvo.calPrdDate();
			SCMEnv.showTime(lTime, "setBillVO:bvo.clone()");
			// ��������
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
			// ִ�й�ʽ
			/** ydy 2005-06-21 */
			if (bExeFormule) {
				getBillCardPanel().getBillModel().execLoadFormula();
				execHeadTailFormulas();
			}
			// ����״̬
			lTime = System.currentTimeMillis();
			getBillCardPanel().updateValue();
			// �����ִ�������
			bvo.clearInvQtyInfo();
			// ѡ�е�һ�У�����Ƶ�����
			getBillCardPanel().getBillTable().setRowSelectionInterval(0, 0);
			getBillCardPanel().transferFocusTo(1);
			// �������к��Ƿ����
			setBtnStatusSN(0, false);

			SCMEnv.showTime(lTime, "setBillVO:3");
			// ˢ���ִ�����ʾ
			setTailValue(0);
			// �����λ���ݣ����кš�
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
		/** �����ݵ���Դ����Ϊת�ⵥʱ, ���н������ */
		long lTime = System.currentTimeMillis();
		ctrlSourceBillUI(bUpdateBotton);
		SCMEnv.showTime(lTime, "setBillVO:ctrlSourceBillUI");

	}

	/**
	 * �����ߣ����� ���ܣ����ݵ�ǰ������BarCodeVO[]�Ƿ���ֵ,����༭��ť�Ƿ���� ������ row�к� ���أ� ���⣺
	 * ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	protected void setBtnStatusBC(int row) {
		GeneralBillVO voBill = null;
		// ����voBill,�Զ�ȡ�������ݡ�
		if (BillMode.Browse == m_iMode) {
			if (m_iLastSelListHeadRow >= 0 && m_alListData != null && m_alListData.size() > m_iLastSelListHeadRow && m_alListData.get(m_iLastSelListHeadRow) != null) voBill = (GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow);
		} else
		// �������޸ķ�ʽ����m_voBill
		voBill = m_voBill;

		if (voBill != null && voBill.getItemVOs() != null && voBill.getItemVOs().length > 0 && row < voBill.getItemVOs().length) {
			GeneralBillItemVO voItem = null;
			voItem = voBill.getItemVOs()[row];
			if (voItem != null && voItem.getBarcodeManagerflag().booleanValue()) {
				// ��������༭
				getButtonTree().getButton(ICButtonConst.BTN_LINE_BARCODE).setEnabled(true);
				// if (m_utfBarCode!=null)
				// m_utfBarCode.setEnabled(true);
			} else {
				getButtonTree().getButton(ICButtonConst.BTN_LINE_BARCODE).setEnabled(false);
				// if (m_utfBarCode!=null)
				// m_utfBarCode.setEnabled(false);
			}

		}
		// �������⣬ydy 04-06-29
		updateButton(getButtonTree().getButton(ICButtonConst.BTN_LINE_BARCODE));

	}

	/**
	 * �����ߣ�� ���ܣ����ݵ�ǰ������BarCodeVO[],���õ�������˵�״̬ ������ row�к� ���أ� ���⣺
	 * 
	 */
	protected void setBtnStatusImportBarcode(int row) {
		if (row < 0) return;
		GeneralBillVO voBill = null;
		if (BillMode.List == m_iCurPanel) {
			setBarcodeButtonStatus(false);
			return;// ������������������벻����ֱ�ӵ���
		}
		// ����voBill,�Զ�ȡ�������ݡ�
		if (BillMode.Browse == m_iMode) {
			if (m_iLastSelListHeadRow >= 0 && m_alListData != null && m_alListData.size() > m_iLastSelListHeadRow && m_alListData.get(m_iLastSelListHeadRow) != null) voBill = (GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow);
		} else
		// �������޸ķ�ʽ����m_voBill
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
	 * ���������������������뵼����ص��ĸ���ť��״̬��<br>
	 * V50�У�ʹ����һ��������ť���������롱����Ϊ���ĸ���ť��һ����ť�����ﵽ�������ĸ���ť״̬��Ŀ��
	 * V51�ع������еİ�ť��ʹ��ButtonTree�����м��أ���������ð�ťע��Ĺ��ܣ����ĸ���ť�鲢��������/���롱��ť�£������Ҫ����������״̬
	 * <p>
	 * <p>
	 * <b>����˵��</b>
	 * 
	 * @param enabled
	 *            ����״̬Ϊtrue��������״̬Ϊfalse
	 *            <p>
	 * @author duy
	 * @time 2007-2-5 ����02:16:46
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
	 * �����ߣ����˾� ���ܣ����ݵ�ǰ���ݵĴ���״̬����ǩ��/ȡ��ǩ���Ǹ����� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	protected void setBtnStatusSign(boolean bUpdateButtons) {

		// ֻ�����״̬�²��ҽ������е���ʱ����
		if (BillMode.Browse != m_iMode || m_iLastSelListHeadRow < 0 || m_iBillQty <= 0) {
			getButtonTree().getButton(ICButtonConst.BTN_SIGN).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_EXECUTE_SIGN_CANCEL).setEnabled(false);
			return;
		}

		int iSignFlag = isSigned();
		if (SIGNED == iSignFlag) {
			// ��ǩ�֣��������ð�ť״̬,ǩ�ֲ����ã�ȡ��ǩ�ֿ���
			getButtonTree().getButton(ICButtonConst.BTN_SIGN).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_EXECUTE_SIGN_CANCEL).setEnabled(true);
			// ����ɾ����
			getButtonTree().getButton(ICButtonConst.BTN_BILL_EDIT).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_BILL_DELETE).setEnabled(false);

		} else if (NOTSIGNED == iSignFlag) {
			// δǩ�֣��������ð�ť״̬,ǩ�ֿ��ã�ȡ��ǩ�ֲ�����
			// �ж��Ƿ���������������Ϊ�����������ģ�����ֻҪ����һ�о����ˡ�

			getButtonTree().getButton(ICButtonConst.BTN_SIGN).setEnabled(true);
			getButtonTree().getButton(ICButtonConst.BTN_EXECUTE_SIGN_CANCEL).setEnabled(false);
			// ��ɾ����
			if (isCurrentTypeBill()) {
				getButtonTree().getButton(ICButtonConst.BTN_BILL_EDIT).setEnabled(true);
				getButtonTree().getButton(ICButtonConst.BTN_BILL_DELETE).setEnabled(true);
			} else {
				getButtonTree().getButton(ICButtonConst.BTN_BILL_EDIT).setEnabled(false);
				getButtonTree().getButton(ICButtonConst.BTN_BILL_DELETE).setEnabled(false);
			}
		} else { // ����ǩ�ֲ���
			getButtonTree().getButton(ICButtonConst.BTN_SIGN).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_EXECUTE_SIGN_CANCEL).setEnabled(false);
			// ��ɾ����
			if (isCurrentTypeBill()) {
				getButtonTree().getButton(ICButtonConst.BTN_BILL_EDIT).setEnabled(true);
				getButtonTree().getButton(ICButtonConst.BTN_BILL_DELETE).setEnabled(true);
			} else {
				getButtonTree().getButton(ICButtonConst.BTN_BILL_EDIT).setEnabled(false);
				getButtonTree().getButton(ICButtonConst.BTN_BILL_DELETE).setEnabled(false);
			}
		}
		// ʹ������Ч ���� by hanwei 2003-11-17 for Ч��
		if (bUpdateButtons) updateButtons();

	}

	/**
	 * �����ߣ����˾� ���ܣ����ݵ�ǰ��������Ծ������кŷ����Ƿ���� ������ row�к� ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� bUpdateButtons:�Ƿ���°�Ŧ,true: ����, false������
	 * 
	 * 
	 * 
	 */
	protected void setBtnStatusSN(int row, boolean bUpdateButtons) {
		GeneralBillVO voBill = null;
		// ����voBill,�Զ�ȡ�������ݡ�
		if (BillMode.Browse == m_iMode) {
			if (m_iLastSelListHeadRow >= 0 && m_alListData != null && m_alListData.size() > m_iLastSelListHeadRow && m_alListData.get(m_iLastSelListHeadRow) != null) voBill = (GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow);
		} else
		// �������޸ķ�ʽ����m_voBill
		voBill = m_voBill;

		if (voBill != null) {
			InvVO voInv = null;
			voInv = voBill.getItemInv(row);
			if (voInv != null && voInv.getIsSerialMgt() != null && voInv.getIsSerialMgt().intValue() != 0)
			// �����кŹ�����������
			getButtonTree().getButton(ICButtonConst.BTN_LINE_SERIAL).setEnabled(true);
			else
			// �������кŹ�������������
			getButtonTree().getButton(ICButtonConst.BTN_LINE_SERIAL).setEnabled(false);
		} else getButtonTree().getButton(ICButtonConst.BTN_LINE_SERIAL).setEnabled(false);

		// �������밴ť״̬���Ʒ���by hanwei 2003-11-18
		setBtnStatusBC(row);

		// ���õ������밴ť״̬���Ʒ��� add by ljun
		setBtnStatusImportBarcode(row);

		// ʹ������Ч ���� by hanwei 2003-11-17 for Ч��
		if (bUpdateButtons)
		// updateButtons();
		// �������⣬ydy 04-06-29
		updateButton(getButtonTree().getButton(ICButtonConst.BTN_LINE_SERIAL));

	}

	/**
	 * �����ߣ����˾� ���ܣ����ݵ�ǰ�ֿ��״̬������λ�����Ƿ���� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	protected void setBtnStatusSpace(boolean bUpdateButtons) {
		GeneralBillVO voBill = null;
		// ����voBill,�Զ�ȡ�������ݡ�
		if (BillMode.Browse == m_iMode) {
			if (m_iLastSelListHeadRow >= 0 && m_alListData != null && m_alListData.size() > m_iLastSelListHeadRow && m_alListData.get(m_iLastSelListHeadRow) != null) voBill = (GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow);
		} else
		// �������޸ķ�ʽ����m_voBill
		voBill = m_voBill;

		// ȱʡ���ǻ�λ����ֿ⣬������
		getButtonTree().getButton(ICButtonConst.BTN_LINE_SPACE).setEnabled(false);
		// m_boSelectLocator.setEnabled(false);

		if (voBill != null) {
			WhVO voWh = null;
			voWh = voBill.getWh();
			// �ǻ�λ����ֿ⣬����
			if (voWh != null && voWh.getIsLocatorMgt() != null && voWh.getIsLocatorMgt().intValue() != 0) {
				getButtonTree().getButton(ICButtonConst.BTN_LINE_SPACE).setEnabled(true);
			}

			// ###########################
			// ���� add by hanwei 2004-05-14
			// 1��������ⵥ�����ֱ�ӵ�ת��־����Ҫ�ڽ������ �޸ġ�ɾ�������ƵȰ�ť�����ã�
			// 2��������ݵĲֿ���ֱ�˲֣�Ӧ�ÿ����޸ġ�ɾ�������ƵȰ�ť�����ã�
			setBtnStatusTranflag();
			// #############################
		}

		// ʹ������Ч by hanwei 2003-11-17 for Ч��
		if (bUpdateButtons) updateButton(getButtonTree().getButton(ICButtonConst.BTN_LINE_SPACE));

	}

	/**
	 * �����ߣ����� ���ܣ� bd_stordoc �Ƿ�ֱ�˲ֿ⣺isdirectstore =Y �� ic_general_h
	 * �Ƿ�ֱ�ӵ�����bdirecttranflag =Y 1��������ⵥ�����ֱ�ӵ�ת��־����Ҫ�ڽ������ �޸ġ�ɾ�������ƵȰ�ť�����ã�
	 * 2���ֿ���һ�����ԣ��Ƿ�ֱ�˲ֿ⣬��Ҫ���� WHVO�С�������ݵĲֿ���ֱ�˲֣�Ӧ�ÿ����޸ġ�ɾ�������ƵȰ�ť�����ã�
	 * 
	 * ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 */
	protected void setBtnStatusTranflag() {
		setBtnStatusTranflag(false);
	}

	/**
	 * �����ߣ����� ���ܣ� bd_stordoc �Ƿ�ֱ�˲ֿ⣺isdirectstore =Y �� ic_general_h
	 * �Ƿ�ֱ�ӵ�����bdirecttranflag =Y 1��������ⵥ�����ֱ�ӵ�ת��־����Ҫ�ڽ������ �޸ġ�ɾ�������ƵȰ�ť�����ã�
	 * 2���ֿ���һ�����ԣ��Ƿ�ֱ�˲ֿ⣬��Ҫ���� WHVO�С�������ݵĲֿ���ֱ�˲֣�Ӧ�ÿ����޸ġ�ɾ�������ƵȰ�ť�����ã�
	 * 
	 * ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * bOnlyFalse:ֻ����setEnabledΪfalse�����������
	 * 
	 */
	protected void setBtnStatusTranflag(boolean bOnlyFalse) {

		GeneralBillVO voBill = null;

		// ����voBill,�Զ�ȡ�������ݡ�
		if (BillMode.Browse == m_iMode) {
			if (m_iLastSelListHeadRow >= 0 && m_alListData != null && m_alListData.size() > m_iLastSelListHeadRow && m_alListData.get(m_iLastSelListHeadRow) != null) voBill = (GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow);
		} else return;

		boolean bUseable = true;
		// ֱ��
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
	 * �����ߣ����˾� ���ܣ����ð�ť״̬�� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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

			// ��������֧��
			getButtonTree().getButton(ICButtonConst.BTN_ASSIST_FUNC_SCAN).setEnabled(true);
			getButtonTree().getButton(ICButtonConst.BTN_IMPORT_BILL).setEnabled(true);
			getButtonTree().getButton(ICButtonConst.BTN_ASSIST_FUNC_INV_CHECK).setEnabled(true);
			getButtonTree().getButton(ICButtonConst.BTN_ASSIST_FUNC_DOCUMENT).setEnabled(false);
			// ���Ʒ�ҳ��ť��״̬��
			m_pageBtn.setPageBtnVisible(false);
			getButtonTree().getButton(ICButtonConst.BTN_LINE_AUTO_FILL).setEnabled(true);
			getButtonTree().getButton(ICButtonConst.BTN_ASSIST_FUNC_PICKUP_AUTO).setEnabled(true);

			// ����������º��޸�����������Ա༭
			if (m_utfBarCode != null) m_utfBarCode.setEnabled(true);

			// ��������
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

			// ��������֧��
			getButtonTree().getButton(ICButtonConst.BTN_ASSIST_FUNC_SCAN).setEnabled(true);
			getButtonTree().getButton(ICButtonConst.BTN_IMPORT_BILL).setEnabled(true);
			getButtonTree().getButton(ICButtonConst.BTN_ASSIST_FUNC_INV_CHECK).setEnabled(true);
			getButtonTree().getButton(ICButtonConst.BTN_ASSIST_FUNC_DOCUMENT).setEnabled(false);
			// ���Ʒ�ҳ��ť��״̬��
			m_pageBtn.setPageBtnVisible(false);
			getButtonTree().getButton(ICButtonConst.BTN_LINE_AUTO_FILL).setEnabled(true);
			getButtonTree().getButton(ICButtonConst.BTN_ASSIST_FUNC_PICKUP_AUTO).setEnabled(true);

			// ����������º��޸�����������Ա༭
			if (m_utfBarCode != null) m_utfBarCode.setEnabled(true);

			if (getButtonTree().getButton(ICButtonConst.BTN_IMPORT_SOURCE_BARCODE) != null && BillMode.Card == m_iCurPanel && m_voBill != null && m_voBill.getHeaderVO() != null) {
				String sBillTypecode = m_voBill.getHeaderVO().getCbilltypecode();
				if (sBillTypecode != null && (sBillTypecode.equalsIgnoreCase("4A") || sBillTypecode.equalsIgnoreCase("4I") || sBillTypecode.equalsIgnoreCase("4Y") || sBillTypecode.equalsIgnoreCase("4E"))) {
					// ���Ϲ�����������ⵥ����������ⵥ�����Ե��뵥������
					// ��Ƭ״̬���������ͣ�
					getButtonTree().getButton(ICButtonConst.BTN_IMPORT_SOURCE_BARCODE).setEnabled(true);
				}

			}

			if (BillMode.List == m_iCurPanel) {
				setBarcodeButtonStatus(false);
			} else {
				setBtnStatusImportBarcode(0);
			}
			break;
		case BillMode.Browse: // ��������������򲻿��Ա༭
			if (m_utfBarCode != null) m_utfBarCode.setEnabled(false);
			// ���б�����£����밴Ŧ
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
			// �е���
			if (m_iBillQty > 0 && m_iLastSelListHeadRow >= 0) {
				getButtonTree().getButton(ICButtonConst.BTN_BILL_COPY).setEnabled(true);
				getButtonTree().getButton(ICButtonConst.BTN_BROWSE_LOCATE).setEnabled(true);

				getButtonTree().getButton(ICButtonConst.BTN_ASSIST_FUNC).setEnabled(true);
				getButtonTree().getButton(ICButtonConst.BTN_ASSIST_QUERY).setEnabled(true);
				getButtonTree().getButton(ICButtonConst.BTN_EXPORT_IMPORT).setEnabled(true);
				getButtonTree().getButton(ICButtonConst.BTN_BROWSE).setEnabled(true);
				// ������Ǻͱ��ڵ���ͬ�ĵ�������(����뵥�ϲ�����ڳ���)������ɾ��.
				try {
					// ��ǰѡ�еĵ���
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
			// �е��ݿ��Դ�ӡ��
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
			} // ��Ӧ��һ���жϵ�ǰ�ĵ����Ƿ���ǩ��
				// ͬʱ�ж��޸ġ�ɾ���Ƿ���ã�����Ӧ�������ǵĺ��档
			getButtonTree().getButton(ICButtonConst.BTN_LINE).setEnabled(false);
			// ��������֧��
			getButtonTree().getButton(ICButtonConst.BTN_ASSIST_FUNC_SCAN).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_IMPORT_BILL).setEnabled(false);
			getButtonTree().getButton(ICButtonConst.BTN_ASSIST_FUNC_INV_CHECK).setEnabled(true);
			getButtonTree().getButton(ICButtonConst.BTN_ASSIST_FUNC_DOCUMENT).setEnabled(true);
			// ��������
			if (getButtonTree().getButton(ICButtonConst.BTN_IMPORT_SOURCE_BARCODE) != null) getButtonTree().getButton(ICButtonConst.BTN_IMPORT_SOURCE_BARCODE).setEnabled(true);
			// ���Ʒ�ҳ��ť��״̬��
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
		// ��ǰѡ����
		lTime = System.currentTimeMillis();
		int rownow = getBillCardPanel().getBillTable().getSelectedRow();
		// �ж��Ƿ���Ҫ���кŷ���,����״̬
		if (rownow >= 0) setBtnStatusSN(rownow, false);
		else getButtonTree().getButton(ICButtonConst.BTN_LINE_SERIAL).setEnabled(false);
		// ���󷽷�����
		SCMEnv.showTime(lTime, "setBtnStatusSN)");
		lTime = System.currentTimeMillis();
		setButtonsStatus(m_iMode);
		setExtendBtnsStat(m_iMode);
		SCMEnv.showTime(lTime, "setButtonsStatus(m_iMode)");
		// �������û�λ�����Ƿ����
		lTime = System.currentTimeMillis();
		setBtnStatusSpace(false);
		SCMEnv.showTime(lTime, "setBtnStatusSpace();:");
		setBtnStatusSign(false);
		// ������Դ���ݿ��ư�ť
		lTime = System.currentTimeMillis();
		ctrlSourceBillButtons(false);
		SCMEnv.showTime(lTime, "������Դ���ݿ��ư�ť");

		// ���ݵ������Ϳ��ư�ť
		lTime = System.currentTimeMillis();
		ctrlBillTypeButtons(true);
		SCMEnv.showTime(lTime, "���ݵ������Ϳ��ư�ť");

		// �ϲ���ʾ
		if (m_iCurPanel == BillMode.List) {
			getButtonTree().getButton(ICButtonConst.BTN_PRINT_DISTINCT).setEnabled(false);
		} else {
			if (m_iMode == BillMode.Browse) getButtonTree().getButton(ICButtonConst.BTN_PRINT_DISTINCT).setEnabled(true);
			else getButtonTree().getButton(ICButtonConst.BTN_PRINT_DISTINCT).setEnabled(false);
		}

		// �б�״̬�£�������ʾ/���صİ�ť������
		if (BillMode.Card == m_iCurPanel) {
			getButtonTree().getButton(ICButtonConst.BTN_ASSIST_FUNC_ONHAND).setEnabled(true);
		} else if (BillMode.List == m_iCurPanel) {
			getButtonTree().getButton(ICButtonConst.BTN_ASSIST_FUNC_ONHAND).setEnabled(false);
		}

		// v5 lj
		if (m_bRefBills == true) {
			setRefBtnStatus();
		}

		// ʹ������Ч
		lTime = System.currentTimeMillis();
		if (bUpdateButtons) updateButtons();
		SCMEnv.showTime(lTime, "updateButtons();");

	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-4-8 11:13:07)
	 * 
	 * @param newGenBillUICtl
	 *            nc.ui.ic.pub.bill.GeneralBillUICtl
	 */
	public void setGenBillUICtl(GeneralBillUICtl newGenBillUICtl) {
		m_GenBillUICtl = newGenBillUICtl;
	}

	/**
	 * �����ߣ����˾� ���ܣ��ڱ�������ʾVO,�����½���״̬updateValue() ������ ���أ� ���⣺ ���ڣ�(2001-5-9
	 * 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	public void setImportBillVO(GeneralBillVO bvo, boolean bExeFormula) throws Exception {
		if (bvo == null) throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000061")/* @res "����ĵ���Ϊ�գ�" */);
		// ��������
		int iRowCount = bvo.getItemCount();

		try {
			getBillCardPanel().getBillModel().removeTableModelListener(this);
			getBillCardPanel().removeBillEditListenerHeadTail();
			// ����һ��clone()
			m_voBill = (GeneralBillVO) bvo.clone();
			// �����ֿ�Ĺ�˾�ǵ�½��˾

			// ��������ⵥ�������������⴦��
			// ��Ӧ���ڻ��෽���С�
			// shawbing on Jun 13, 2005
			if (m_sBillTypeCode.equals("4Y") || m_sBillTypeCode.equals("4E")) setBillRefIn4Eand4Y(m_voBill);

			// �����ĵ������PK
			m_voBill.getHeaderVO().setPrimaryKey(null);
			GeneralBillItemVO[] voaItem = m_voBill.getItemVOs();
			for (int row = 0; row < iRowCount; row++) {
				voaItem[row].setPrimaryKey(null);
				voaItem[row].calculateMny();
			}
			// ��������
			getBillCardPanel().setBillValueVO(m_voBill);
			// ִ�й�ʽ
			if (bExeFormula) {
				getBillCardPanel().getBillModel().execLoadFormula();
				execHeadTailFormulas();
			}

			// ����״̬ ---delete it to support CANCEL
			// getBillCardPanel().updateValue();
			// �����ִ�������
			bvo.clearInvQtyInfo();
			// �б����У�ѡ�е�һ��
			if (iRowCount > 0) {
				// ѡ�е�һ��
				getBillCardPanel().getBillTable().setRowSelectionInterval(0, 0);
				// �������к��Ƿ����
				setBtnStatusSN(0, false);

				// ˢ���ִ�����ʾ
				// setTailValue(0);
				// ������������
				nc.ui.pub.bill.BillModel bmTemp = getBillCardPanel().getBillModel();
				m_alLocatorDataBackup = m_alLocatorData;
				m_alSerialDataBackup = m_alSerialData;
				m_alLocatorData = new ArrayList();
				m_alSerialData = new ArrayList();

				for (int row = 0; row < iRowCount; row++) {
					// ������״̬Ϊ����
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
	 * ?user> ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2005-1-11 18:53:03) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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

		// ����λ
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
			// ������ǲ�⣬��ôִ����ǰ�Ĵ��롣
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

		// ������
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
							afterEditVfree1(freevo.getAttributeValue("vfree" + i), irow);//add by shikun ��λ�������κ���ѡ���ж�Ŵ���
						}
					} else {
						getBillCardPanel().setBodyValueAt(null, irow, "vfree" + i);
					}
				}
			}
			m_voBill.setItemFreeVO(irow, freevo);
		}
		// ͬ���ı�m_voBill
		m_voBill.setItemValue(irow, "vbatchcode", voLot.getVbatchcode());
		m_voBill.setItemValue(irow, "dvalidate", voLot.getDvalidate());

	}

	/**
	 * ��λ�������κ���ѡ���ж�Ŵ���
	 * @author shikun
	 * @time 2014-12-06 
	 * */
	public void afterEditVfree1(Object attributeValue, int irow) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * �����ߣ����Ӣ
	 * 
	 * ���ܣ������еĻ�λ��Ϣ
	 * 
	 * ������e���ݱ༭�¼� ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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

		// ��������λ������LocatorVO
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

			// �޸����к�����
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
	 * �����ߣ����˾� ���ܣ����ñ���/��β��С��λ�� ������ ���أ� ���⣺ ���ڣ�(2001-11-23 18:11:18)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	protected void setScaleOfCardPanel(nc.ui.pub.bill.BillCardPanel bill) {

		// ����

		nc.ui.ic.pub.scale.ScaleInit si = new nc.ui.ic.pub.scale.ScaleInit(m_sUserID, m_sCorpID, m_ScaleValue);

		try {
			si.setScale(bill, m_ScaleKey);
		} catch (Exception e) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000060")/* @res "��������ʧ�ܣ�" */
					+ e.getMessage());
		}

	}

	/**
	 * �����ߣ����˾� ���ܣ����ص���ʾ��ʾ��Ϣ�Ի����� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	public void showErrorMessage(String sMsg, boolean bWarnSound) {
		// ��ʾ����
		if (bWarnSound) java.awt.Toolkit.getDefaultToolkit().beep();

		showErrorMessage(sMsg);

	}

	/**
	 * 
	 * ���ܣ� �����ļ���ͬ��״̬vo. ������ ���أ� ���⣺ ���ڣ�(2002-04-18 10:43:46) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	public void synVO(nc.vo.pub.CircularlyAccessibleValueObject[] voaDi, String sWarehouseid, ArrayList m_alLocatorData) throws Exception {
		// ͬ��vo.
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
			// �����λ add by hanwei 2003-12-17
			nc.ui.ic.pub.bill.GeneralBillUICtl.setLocatorVO(voaItem, sWarehouseid, m_alLocatorData);
		}
	}

	/**
	 * �����ߣ������� ���ܣ�ճ���� ������ ���أ� ���⣺ ���ڣ�(2001-6-26 ���� 9:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	protected void voBillPastLine(int row) {
		// Ҫ�����Ѿ������к�ִ��

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

			// //ճ���е���Ʒ���Ա༭
			// if (getBillCardPanel().getBodyItem("flargess")!=null)
			// {
			// getBillCardPanel().getBodyItem("flargess").setEnabled(true);
			// getBillCardPanel().getBodyItem("flargess").setEdit(true);
			// }
		}
		// �����Ƿ�����кŷ���
		//
		// voBillPastLineSetAttribe(row,m_voBill);
		setBtnStatusSN(row, true);
	}

	/**
	 * �����ߣ������� ���ܣ�ճ���� ������ ���أ� ���⣺ ���ڣ�(2001-6-26 ���� 9:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	protected void voBillPastTailLine() {
		// Ҫ�����Ѿ������к�ִ��
		if (m_voaBillItem != null) {
			int row = getBillCardPanel().getBillTable().getRowCount() - m_voaBillItem.length;
			voBillPastLine(row);
		}

	}

	/**
	 * hw ���ܣ������ͨ��ҵ����־VO ������ ���أ� ���⣺ ���ڣ�(2004-6-8 20:42:43) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
	 * ���ܣ����˳�ʵ�������ǿշ���ı����У���ӡ�á� ������ ���أ� ���⣺ ���ڣ�(2005-2-21 21:31:48)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
	 * ���ܣ����˳�ʵ�������ǿշ���ı����У���ӡ�á� ������ ���أ� ���⣺ ���ڣ�(2005-2-21 21:33:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
	 * ���� LotNumbRefPane1 ����ֵ��
	 * 
	 * @return nc.ui.ic.pub.lot.LotNumbRefPane
	 */
	/* ���棺�˷������������ɡ� */
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
	 * �˴����뷽��˵���� �������ڣ�(2003-8-25 13:53:58) ���ⵥ�Զ����, �Ƿ����
	 */
	public void onPickAuto() {

		if (m_voBill == null) return;

		if (!nc.ui.scm.pub.report.BillRowNo.verifyRowNosCorrect(getBillCardPanel(), m_sBillRowNo)) { return; }
		// zhy2005-05-17���õ������� ����Զ������ָ��
		m_voBill.setHeaderValue("cbilltypecode", m_sBillTypeCode);

		GeneralBillVO voOutBill = (GeneralBillVO) m_voBill.clone();
		// ���ת���۵Ĳ�������
		String sBillType = voOutBill.getHeaderVO().getCbiztypeid();
		try {
			String sReturn = (new QueryInfo()).queryBusiTypeVerify(sBillType);
			if (sReturn != null && sReturn.equals("C")) {
				nc.ui.pub.beans.MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPPSCMCommon-000270")/*
																																				* @res "��ʾ"
																																				*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000496")/*
																																																									* @res
																																																									* "���ת���۲����Զ������"
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
			// zhy����Ǹ���������Ĵ��,û�����븨��λ,�������Զ����
			InvVO invvo = voItems[i].getInv();
			Integer IsAstUOMmgt = invvo.getIsAstUOMmgt();
			if (IsAstUOMmgt != null && IsAstUOMmgt.intValue() == 1 && (voItems[i].getCastunitid() == null || voItems[i].getCastunitid().trim().length() == 0)) {
				nc.ui.pub.beans.MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/*
																																				 * @res "��ʾ"
																																				 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000511")/*
																																																									* @res
																																																									* "�����и�����������Ӧ���븨��λ���������Զ������"
																																																									*/);
				return;
			}

			if (voItems[i].getNshouldoutnum() == null || voItems[i].getNshouldoutnum().doubleValue() < 0) {
				nc.ui.pub.beans.MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/*
																																				 * @res "��ʾ"
																																				 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000117")/*
																																																									* @res
																																																									* "������Ӧ������С���㣬�������Զ������"
																																																									*/);
				return;
			}
			// ���˿���,��¼ԭʼ�к�
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
				// ִ�й�ʽ
				getBillCardPanel().getBillModel().execFormulasWithVO(voItems, getBodyFormula());

				// ��¼��δ��

				boolean isVendor = false;
				// ��δ����ID
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

				// �л�λ����
				if (isLocator || isVendor) {
					nc.vo.pub.SuperVOUtil.execFormulaWithVOs(voItems, aryItemField11, null);

				}

			}
		} catch (Exception e) {

			nc.ui.pub.beans.MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000059")/* @res "����" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000118")/* @res "�Զ����ʧ�ܣ�" */
					+ e.getMessage());
			return;
		}

		if (voItems != null && voItems.length > 0) {
			String key = null;
			UFDouble dkey = new UFDouble(0);
			int iCurRow = 0;
			int icount = 0;// ������

			GeneralBillItemVO voRow = null;

			getBillCardPanel().getBillModel().setNeedCalculate(false);

			int irows = getBillCardPanel().getRowCount();
			UFDouble dLastRowNo = new UFDouble(0);
			String sLastRowNo = null;
			if (irows > 0) {
				sLastRowNo = (String) getBillCardPanel().getBodyValueAt(irows - 1, "crowno");// ���һ�е��к�
				dLastRowNo = new UFDouble(sLastRowNo);
				// �ֽ�������ڵ�һ��
				getBillCardPanel().getBillTable().setRowSelectionInterval(0, 0);
			}

			BatchCodeDefSetTool.execFormulaForBatchCode(voItems);

			for (int i = 0; i < voItems.length; i++) {
				key = voItems[i].getVbodynote2();
				dkey = new UFDouble(key);
				if (!ht.containsKey(key)) {// �����ԭ��
					ht.put(key, key);
					voRow = voItems[i];
					getBillCardPanel().setBodyValueAt(key, i, "crowno");
					voRow.setCgeneralhid(voOutBill.getItemVOs()[icount].getCgeneralhid());
					voRow.setCgeneralbid(voOutBill.getItemVOs()[icount].getCgeneralbid());
					voRow.setCrowno(voRow.getVbodynote2());
					voRow.setVbodynote2(null);
					getBillCardPanel().getBillModel().setBodyRowVO(voRow, i);
					// ִ�и������ı༭��ʽ��1201
					execEditFomulas(i, m_sNumItemKey);
					if (getBillCardPanel().getBillModel().getRowState(i) != nc.ui.pub.bill.BillModel.ADD && getBillCardPanel().getBillModel().getRowState(i) != nc.ui.pub.bill.BillModel.MODIFICATION) getBillCardPanel().getBillModel().setRowState(i, nc.ui.pub.bill.BillModel.MODIFICATION);

					// ���������һ�У���Ҫ�����ٽ�״̬��

					// if(dkey.compareTo(dLastRowNo)<0)
					// getBillCardPanel().getBillTable().setRowSelectionInterval(i+1,
					// i+1);
					// else{
					// //�ں�������
					// }
					// iCurRow=i+1;
					icount++;
				} else {// ����ǲ�ֵ��У�������У�Ȼ��vo����
					voRow = voItems[i];
					voRow.setCgeneralbid(null);
					voRow.setCgeneralhid(null);
					voRow.setVbodynote2(null);
					voRow.setNshouldoutnum(null);
					voRow.setNshouldoutassistnum(null);

					// if(key.compareTo(sLastRowNo)<0){
					if (dkey.compareTo(dLastRowNo) < 0) {
						// ����
						iCurRow = i;
						getBillCardPanel().getBillTable().setRowSelectionInterval(iCurRow, iCurRow);
						getBillCardPanel().insertLine();
						nc.ui.scm.pub.report.BillRowNo.insertLineRowNo(getBillCardPanel(), m_sBillTypeCode, m_sBillRowNo);
						voRow.setCrowno((String) getBillCardPanel().getBodyValueAt(iCurRow, "crowno"));
						getBillCardPanel().getBillModel().setBodyRowVO(voRow, iCurRow);
						execEditFomulas(iCurRow, m_sNumItemKey);
					} else {
						// ����
						getBillCardPanel().addLine();
						nc.ui.scm.pub.report.BillRowNo.addLineRowNo(getBillCardPanel(), m_sBillTypeCode, m_sBillRowNo);
						voRow.setCrowno((String) getBillCardPanel().getBodyValueAt(i, "crowno"));
						getBillCardPanel().getBillModel().setBodyRowVO(voRow, i);
						execEditFomulas(i, m_sNumItemKey);
					}

				}

				// zhy�����,�ӽ���ȡһ��hsl
				GeneralBillUICtl.synUi2Vo(getBillCardPanel(), voNew, new String[] {
					"hsl"}, i);   //hslΪ������
			}

			getBillCardPanel().getBillModel().setNeedCalculate(true);
			getBillCardPanel().getBillModel().reCalcurateAll();
			m_voBill = (GeneralBillVO) voNew.clone();
			// ��ʼ�����к�����
			if (isSN) m_alSerialData = voNew.getSNs();
			// �л�λ����
			if (isLocator)

			m_alLocatorData = voNew.getLocators();

		}

		return;

	}

	/**
	 * ?user> ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2005-1-11 18:53:03) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
			// ��һ�У�������������λhsl��ͬ�����滻hsl
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
				// // �����ε�ʧЧ����
				// getBillCardPanel().setBodyValueAt(vo.getDvalidate(), iSelrow,
				// "dvalidate");
				// vo.calPrdDate();
				// v35 lj ����ʧЧ������������
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

		// ִ�б��嵥�ۼ�����Ĺ�ʽ.
		// if (getBillCardPanel().getBodyItem("nprice") != null) {
		// if (!isFirstLine || vo.getNprice() != null) {
		// getBillCardPanel().setBodyValueAt(vo.getNprice(), iSelrow,
		// "nprice");
		// m_voBill.setItemValue(iSelrow, "nprice", vo.getNprice());
		// execEditFomulas(iSelrow, "nprice");
		// }
		// }
		// ��Ӧ���ݺ�
		getBillCardPanel().setBodyValueAt(vo.getCcorrespondcode(), iSelrow, m_sCorBillCodeItemKey);
		// ��Ӧ��������
		getBillCardPanel().setBodyValueAt(vo.getCcorrespondtype(), iSelrow, "ccorrespondtype");
		// ��Ӧ���ݱ�ͷOID
		// ����ģ����б���λ����������ʾ��ccorrespondhid,ccorrespondbid,�Ա�������Ķ�Ӧ��ͷ������OID
		getBillCardPanel().setBodyValueAt(vo.getCcorrespondhid(), iSelrow, "ccorrespondhid");
		// ��Ӧ���ݱ���OID
		getBillCardPanel().setBodyValueAt(vo.getCcorrespondbid(), iSelrow, "ccorrespondbid");
		// ��;���
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

	// ���ݵ����˵�
	protected Vector m_vBillExcel = null;

	/**
	 * ���ߣ�� ���ܣ��ͻ��༭���¼� ������ ���أ� ���⣺ ���ڣ�(2004-6-21 10:38:55) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @param e
	 *            nc.ui.pub.bill.BillEditEvent
	 */
	public void afterCustomerEdit(nc.ui.pub.bill.BillEditEvent e) {
		// �ͻ�
		String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("ccustomerid").getComponent()).getRefName();
		String sRefPK = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("ccustomerid").getComponent()).getRefPK();
		// �������������б���ʽ����ʾ��
		if (m_voBill != null) m_voBill.setHeaderValue("ccustomername", sName);
		// ���ݿͻ���Ӧ�̹��˷��˵�ַ�Ĳ���
		filterVdiliveraddressRef(true, -1);

		// ���ݿͻ���Ӧ�̹��˷��˵�ַ�Ĳ���
		if (getBillCardPanel().getHeadItem("vdiliveraddress") != null && sRefPK != null) {

			((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("vdiliveraddress").getComponent()).setWhereString("pk_cubasdoc = (select pk_cubasdoc from bd_cumandoc where pk_cumandoc = '" + sRefPK + "')");
		}

		BillItem iProvidername = getBillCardPanel().getHeadItem("ccustomershortname");

		BillItem iPk_cubasdocC = getBillCardPanel().getHeadItem("pk_cubasdocC");

		try {
			// ���ݲ��մ������̼��
			nc.ui.scm.pub.TwoTableCacheFind twoTable = new nc.ui.scm.pub.TwoTableCacheFind();
			String sCustomerShortName = twoTable.getJoinTableFieldValue("bd_cumandoc", sRefPK, "custshortname");
			if (iProvidername != null) {
				iProvidername.setValue(sCustomerShortName);
			}

			// ��ÿ��̻�������ID
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
	 * ���ߣ�� ���ܣ���Ӧ�̱༭���¼���������� ������ ���أ� ���⣺ ���ڣ�(2004-6-21 10:36:30)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @param e
	 *            nc.ui.pub.bill.BillEditEvent
	 */
	public void afterProviderEdit(nc.ui.pub.bill.BillEditEvent e) {

		String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("cproviderid").getComponent()).getRefName();
		String sRefPK = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("cproviderid").getComponent()).getRefPK();

		// �������������б���ʽ����ʾ��
		if (m_voBill != null) m_voBill.setHeaderValue("cprovidername", sName);

		// ���ݿͻ���Ӧ�̹��˷��˵�ַ�Ĳ���
		if (getBillCardPanel().getHeadItem("vdiliveraddress") != null) {
			((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem("vdiliveraddress").getComponent()).setWhereString("pk_cubasdoc = (select pk_cubasdoc from bd_cumandoc where pk_cumandoc = '" + sRefPK + "')");
		}

		BillItem iProvidername = getBillCardPanel().getHeadItem("cprovidershortname");
		BillItem iPk_cubasdoc = getBillCardPanel().getHeadItem("pk_cubasdoc");
		try {
			// ���ݲ��մ�����Ӧ�̼��
			nc.ui.scm.pub.TwoTableCacheFind twoTable = new nc.ui.scm.pub.TwoTableCacheFind();
			String sProviderShortName = twoTable.getJoinTableFieldValue("bd_cumandoc", sRefPK, "custshortname");
			if (iProvidername != null) {
				iProvidername.setValue(sProviderShortName);
			}

			// ��ÿ��̻�������ID
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
	 * �����������������ݿ��̹�������ÿ��̵Ļ���������
	 * <p>
	 * <b>����˵��</b>
	 * 
	 * @param pk_cumangid
	 *            ���̹�������ID
	 * @return ���̻�������ID
	 *         <p>
	 * @author duy
	 * @time 2007-3-20 ����10:50:26
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
	 * �����ߣ����˾� ���ܣ��ֿ�ı��¼����� ������e���ݱ༭�¼� ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	public void afterWhEdit(nc.ui.pub.bill.BillEditEvent e, String sNewWhName, String sNewWhID) {
		// �ֿ�
		try {
			getBillCardPanel().rememberFocusComponent();
			if (sNewWhID == null) {
				sNewWhName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem(m_sMainWhItemKey).getComponent()).getRefName();
				sNewWhID = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem(m_sMainWhItemKey).getComponent()).getRefPK();
			} else {
				((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem(m_sMainWhItemKey).getComponent()).setValue(sNewWhName);
				((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem(m_sMainWhItemKey).getComponent()).setPK(sNewWhID);
			}
			// ��ձ������ʾ����
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
				SCMEnv.out("���Ժ��ԵĴ���" + e2);
			}
			// ����˲ֿ�
			if (sNewWhID == null) {
				// ������κŲ��յĲֿ�
				getLotNumbRefPane().setWHParams(null);
				if (m_voBill != null) m_voBill.setWh(null);
			} else {

				// �������������б���ʽ����ʾ��
				// ��ѯ�ֿ���Ϣ
				// ��ѯ��ʽ������Ѿ�¼���˴������Ҫͬʱ��ƻ��ۡ�
				int iQryMode = QryInfoConst.WH;
				// ����
				Object oParam = sNewWhID;
				// ��ǰ��¼��Ĵ������
				ArrayList alAllInvID = new ArrayList();
				boolean bHaveInv = getCurInvID(alAllInvID);

				// �ֿ�
				WhVO voWh = null;
				// ������κŲ��յĲֿ�
				getLotNumbRefPane().setWHParams(null);
				if (m_voBill != null) m_voBill.setWh(null);

				if (bHaveInv) {
					// �������ֿ�ID,ԭ�����֯ID,��λID,���ID
					ArrayList alParam = new ArrayList();
					alParam.add(sNewWhID);
					iQryMode = QryInfoConst.WH_PLANPRICE;
					// ��ǰ�Ŀ����֯,����û�вֿ�������
					if (m_voBill != null && m_voBill.getWh() != null) alParam.add(m_voBill.getWh().getPk_calbody());
					else alParam.add(null);
					// ��˾
					alParam.add(m_sCorpID);
					// ��ǰ�Ĵ��
					alParam.add(alAllInvID);
					oParam = alParam;
				}

				Object oRet = GeneralBillHelper.queryInfo(new Integer(iQryMode), oParam);
				// Object oRet = invokeClient("queryInfo", new Class[] {
				// Integer.class, Object.class }, new Object[] {
				// new Integer(iQryMode), oParam });

				// ��ǰ��¼��Ĵ������,�����޸��˿����֯�ŷ���һ��ArrayList
				if (oRet instanceof ArrayList) {
					ArrayList alRetValue = (ArrayList) oRet;
					if (alRetValue != null && alRetValue.size() >= 2) {
						voWh = (WhVO) alRetValue.get(0);
						// ˢ�¼ƻ���
						freshPlanprice((ArrayList) alRetValue.get(1));
					}
				} else
				// ���򷵻� WhVO
				voWh = (WhVO) oRet;
				// �����֯����
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
				// // ���˼������ߵ���
				// String[] sConstraint1 = new String[1];
				// sConstraint1[0] = " and mm_jldoc.gcbm='" +
				// voWh.getPk_calbody()
				// + "'";
				// nc.ui.pub.bill.BillItem bi1 = getBillCardPanel().getHeadItem(
				// "pk_measware");
				// RefFilter.filtMeasware(bi1, m_sCorpID, sConstraint1);

				if (m_voBill != null) {
					m_voBill.setWh(voWh);
					// ���β�ִ���
					m_voBill.clearInvQtyInfo();
					getLotNumbRefPane().setWHParams(voWh);

					// DUYONG ˢ�¼ƻ���
					freshPlanprice(getInvoInfoBYFormula().getPlanPrice(alAllInvID, voWh.getPk_calbody(), voWh.getCwarehouseid()));
				}

				// ���λ�����к����ݷ���afterEdit()��clearLocSn��
				// int iRowCount = getBillCardPanel().getRowCount();
				// for (int row = 0; row < iRowCount; row++)
				// clearRowData(0, row, m_sMainWhItemKey);
				// ˢ���ִ�����ʾ
				setTailValue(0);
				// ���û�λ���䰴ť�Ƿ���á�
				setBtnStatusSpace(true);
			}

			getBillCardPanel().restoreFocusComponent();

		} catch (Exception e2) {
			SCMEnv.out(e2);
		}

	}

	/**
	 * ?user> ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2004-8-27 10:54:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @param voNew
	 *            nc.vo.ic.pub.bill.GeneralBillVO
	 * 
	 *            �޸����⣺�ɵ����������ɵ������ⵥʱ����ʽ&��ʽ������λû�б����ϡ�
	 *            �޸�ԭ���������λ����ʱ��ʹ��locator�б�������ݣ����Ǳ�������ʾ��space��
	 *            ��space�޸ĺ�afterEdit�����afterSpaceEdit�������Զ�ͬ��space��locator��
	 *            �����ƻ������ε���ʱ��space�����Զ�ͬ��locator���������治�ϡ� �޸�ʱ�䣺2005-11-24
	 *            �޸��ߣ�ShawBing
	 * 
	 */
	protected void calcSpace(GeneralBillVO voNew) {
		if (voNew != null && voNew.getItemVOs() != null) {
			GeneralBillItemVO[] voItems = voNew.getItemVOs();
			// ��¼��δ��
			boolean isLocator = false;
			// ��δ����ID
			String[] aryItemField11 = new String[] {
					"vspacename->getColValue(bd_cargdoc,csname,pk_cargdoc,cspaceid)", "vspacecode->getColValue(bd_cargdoc,cscode,pk_cargdoc,cspaceid)"
			};

			// boolean isSN = false;

			for (int i = 0; i < voItems.length; i++) {

				if (voItems[i].getCspaceid() != null) {
					isLocator = true;

					/** 1-1 ���沿��Ϊ2005-11-25 Shaw �޸����ݣ��޸�˵��������ע��* */
					// ���ƻ���ʽ���ɵ���ʱ��space��ֵ����locaorûֵ�����»�λ�޷�����
					// ͬ��locator
					if (voItems[i].getLocator() == null || voItems[i].getLocator().length == 0) {

						LocatorVO[] voLocators = new LocatorVO[1];
						voLocators[0] = new LocatorVO();
						voLocators[0].setCspaceid(voItems[i].getCspaceid());

						voItems[i].setLocator(voLocators);
						m_alLocatorData.remove(i);
						m_alLocatorData.add(i, voLocators);

						resetSpace(i);
					}
					/** 1-1 �޸Ľ���* */
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
	 * �����ߣ������� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2001-11-8 19:47:29) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @return nc.ui.pub.bill.BillData
	 * @param oldBillData
	 *            nc.ui.pub.bill.BillData
	 */
	protected BillData changeBillDataByUserDef(DefVO[] defHead, DefVO[] defBody, BillData oldBillData) {
		try {
			// �����Զ��������
			// DefVO[] defs= null;
			// ��ͷ
			// ��ö�Ӧ�ڹ�˾�ĸõ��ݵ��Զ���������
			// defs= nc.ui.bd.service.BDDef.queryDefVO("��Ӧ��/ARAP����ͷ",
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

			// ����
			// ��ö�Ӧ�ڹ�˾�ĸõ��ݵ��Զ���������
			// defs= nc.ui.bd.service.BDDef.queryDefVO("��Ӧ��/ARAP������",
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
	 * �����ߣ������� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2001-11-8 19:47:29) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
	 * �����λ��ɫ
	 */
	public void clearOrientColor() {
		if (m_isLocated) {
			if (m_iCurPanel == BillMode.List) nc.ui.scm.pub.report.OrientDialog.clearOrientColor(getBillListPanel().getHeadTable());
			else nc.ui.scm.pub.report.OrientDialog.clearOrientColor(getBillCardPanel().getBillTable());
			m_isLocated = false;
		}

	}

	/**
	 * ������ ���ܣ�ִ�����⹫ʽ. Ŀǰֻ�����۳��ⵥ���ش˷���. ������ ���أ� ���⣺ ���ڣ�(2004-7-20 17:19:12)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 */
	protected void execExtendFormula(ArrayList alListData) {
	}

	/**
	 * �����ߣ������� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2002-1-24 11:35:23) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @return boolean
	 */
	/**
	 * ���� BillCardPanel1 ����ֵ��
	 * 
	 * @return nc.ui.pub.bill.BillCardPanel bEditable:�Ƿ���Ա༭�� bSaveable:�Ƿ����ֱ�ӱ���
	 * 
	 *         ����༭����״̬�� ֻ����������ʾ �޸ģ������޸ģ�����Ҫͨ�����ݲ��ܱ��� ���棺����ֱ�ӱ��棬ֻ�����״̬�ſ��ԣ�
	 * 
	 */
	/* ���棺�˷������������ɡ� */
	public BarCodeDlg getBarCodeDlg(boolean bEditable, boolean bSaveable) {
		m_dlgBarCodeEdit = new BarCodeDlg(this, this, m_sCorpID, bEditable, bSaveable);
		return m_dlgBarCodeEdit;
	}

	/**
	 * �˴����뷽��˵���� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2002-7-26 11:51:17) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
	 * ?user> ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2004-8-31 15:56:03) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
	 * ?user> ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2004-6-30 17:57:26) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
	 * ?user> ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2004-6-30 17:57:26) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @return nc.vo.bd.def.DefVO[]
	 * @param pk_corp
	 *            java.lang.String
	 * @param isHead
	 *            boolean
	 */
	public DefVO[] getDefHeadVO() {
		if (m_defHead == null) {
			// m_defHead = nc.ui.bd.service.BDDef.queryDefVO("��Ӧ��/ARAP����ͷ",
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
	 * ���ݵ���XML �������ڣ�(2003-09-28 9:51:50)
	 */
	public void onExportXML(GeneralBillVO[] billvos, String filename) {
		if (billvos == null || billvos.length <= 0 || filename == null) return;
		try {

			MessageDialog.showInputDlg(this, "�������ⲿϵͳ����", "�������ⲿϵͳ����:", "20", 5);

			IPFxxEJBService export = (IPFxxEJBService) NCLocator.getInstance().lookup(IPFxxEJBService.class.getName());
			Document outdocs = export.exportBills(billvos, getClientEnvironment().getAccount().getAccountCode(), getClientEnvironment().getCorporation().getPrimaryKey(), "IC", "20");

			FileUtils.writeDocToXMLFile(outdocs, filename);
		} catch (Exception e) {
			showErrorMessage(e.getMessage());
		}
	}

	/**
	 * ���ݵ���xml �������ڣ�(2003-09-28 9:51:50)
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

		if (iFlag == 1 || iFlag == 3/* ����Ϊxml */) {
			// ���ļ�
			if (getChooser().showSaveDialog(this) == javax.swing.JFileChooser.CANCEL_OPTION) return;
			sFilePathDir = getChooser().getSelectedFile().toString();
		}
		if (sFilePathDir == null) {
			showHintMessage("�������ļ�������!");
			return;
		}

		try {
			// ����ǰ���б��Ǳ�������������
			if (m_iCurPanel == BillMode.Card) { // ���
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000119")/*
																												* @res "���ڵ��������Ժ�..."
																												*/);

				// ׼������
				voBill = m_voBill;
				if (voBill == null) {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000049")/*
																													* @res
																													* "���Ȳ�ѯ��¼�뵥�ݡ�"
																													*/);
					return;
				}
				if (voBill.getParentVO() == null) {
					voBill.setParentVO(new GeneralBillHeaderVO());
				}
				if ((voBill.getChildrenVO() == null) || (voBill.getChildrenVO().length == 0) || (voBill.getChildrenVO()[0] == null)) {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH003")/*
																									 * @res "��ѡ��Ҫ��������ݣ�"
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
																													* "�������"
																													*/);
					return;
				}

				// �õ����ݺţ���˾
				sBillCode = voBill.getParentVO().getAttributeValue("vbillcode") == null ? "" : voBill.getParentVO().getAttributeValue("vbillcode").toString();
				// sPKCorp = voBill.getParentVO().getAttributeValue("pk_corp")
				// == null ? ""
				// : voBill.getParentVO().getAttributeValue("pk_corp")
				// .toString();
				sBillTypeCode = voBill.getBillTypeCode();
				sBillTypeName = GeneralBillVO.getBillTypeName(sBillTypeCode);

				tvos = (GeneralBillItemVO[]) voBill.getChildrenVO();
				// ��Ҫ���������Ϣһ�𵼳�
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
							// ���ݺ���Ϣ
							vo.setAttributeValue("billcode", sBillCode); // ���ݺ�
							// ��������Ϣ
							vo.setAttributeValue("rowno", tvos[i].getCrowno()); // �к�
							vo.setAttributeValue("inventorycode", tvos[i].getCinventorycode()); // �������
							vo.setAttributeValue("inventoryname", tvos[i].getInvname()); // �������
							vo.setAttributeValue("billtypecode", sBillTypeCode);
							vo.setAttributeValue("billtypename", sBillTypeName);

							UFDouble dshouldin = null; // Ӧ��
							UFDouble dshouldout = null; // Ӧ��
							UFDouble dShouldnum = null;

							UFDouble din = null; // ʵ��
							UFDouble dout = null; // ʵ��
							UFDouble dnum = null; // Ҫ����������

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
							if (dShouldnum != null) vo.setAttributeValue("shouldnum", dShouldnum.toString()); // ���������ʵ��������
							if (dnum != null) vo.setAttributeValue("nnum", dnum.toString()); // ���������ʵ��������
							vo.setAttributeValue("free", tvos[i].getVfree0()); // ������Ŀ
							vo.setAttributeValue("batchcode", tvos[i].getVbatchcode()); // ���κ�
							vo.setAttributeValue("cgeneralbid", tvos[i].getCgeneralbid()); // ����pk
							vo.setAttributeValue("cgeneralhid", tvos[i].getCgeneralhid()); // ��ͷpk
							// ������Ϣ
							vo.setBarcode(bvos[j].getVbarcode()); // ������
							vo.setBarcodesub(bvos[j].getVbarcodesub()); // ������
							vo.setPackcode(bvos[j].getVpackcode()); // ������

							v.add(vo);

						}
					} else {

						vo = new ExcelFileVO();
						// ���ݺ���Ϣ
						vo.setAttributeValue("billcode", sBillCode); // ���ݺ�
						// ��������Ϣ
						vo.setAttributeValue("rowno", tvos[i].getCrowno()); // �к�
						vo.setAttributeValue("inventorycode", tvos[i].getCinventorycode()); // �������
						vo.setAttributeValue("inventoryname", tvos[i].getInvname()); // �������
						vo.setAttributeValue("billtypecode", sBillTypeCode);
						vo.setAttributeValue("billtypename", sBillTypeName);

						UFDouble dshouldin = null; // Ӧ��
						UFDouble dshouldout = null; // Ӧ��
						UFDouble dShouldnum = null;

						UFDouble din = null; // ʵ��
						UFDouble dout = null; // ʵ��
						UFDouble dnum = null; // Ҫ����������

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
						if (dShouldnum != null) vo.setAttributeValue("shouldnum", dShouldnum.toString()); // ���������ʵ��������
						if (dnum != null) vo.setAttributeValue("nnum", dnum.toString()); // ���������ʵ��������
						vo.setAttributeValue("free", tvos[i].getVfree0()); // ������Ŀ
						vo.setAttributeValue("batchcode", tvos[i].getVbatchcode()); // ���κ�
						vo.setAttributeValue("cgeneralbid", tvos[i].getCgeneralbid()); // ����pk
						vo.setAttributeValue("cgeneralhid", tvos[i].getCgeneralhid()); // ��ͷpk
						v.add(vo);
					}

					// �Ѿ��õ�vo��Ȼ�󵼳�vo
					vos = new ExcelFileVO[v.size()];
					v.copyInto(vos);
					// ���õ����ӿ�
					// �ļ����ƹ���Icbill+��˾PK+���ݺ�+".xls"
					// sFilePath = sFilePathDir;
					ExcelReadCtrl erc = new ExcelReadCtrl();
					erc.setVOToExcel(vos, sFilePathDir);
					// д״̬
					ExcelReadCtrl erc1 = new ExcelReadCtrl(sFilePathDir, true);
					// д״̬
					erc1.setExcelFileFlag(nc.ui.ic.pub.barcodeoffline.IExcelFileFlag.F_NEW);
					showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000120")/*
																													* @res
																													* "�������"
																													*/);
				}
			} else if (m_iCurPanel == BillMode.List) { // �б�
				if (null == m_alListData || m_alListData.size() == 0) {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000049")/*
																													* @res
																													* "���Ȳ�ѯ��¼�뵥�ݡ�"
																													*/);
					return;
				}

				ArrayList alBill = getSelectedBills();

				if (alBill == null || alBill.size() <= 0 || alBill.get(0) == null) {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH003")/*
																									 * @res "��ѡ��Ҫ��������ݣ�"
																									 */);
					return;
				}

				if (iFlag == 3) {
					sBillCode = ((GeneralBillVO) alBill.get(0)).getHeaderVO().getVbillcode();
					onExportXML((GeneralBillVO[]) alBill.toArray(new GeneralBillVO[alBill.size()]), sFilePathDir);
					showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000120")/*
																													* @res
																													* "�������"
																													*/);
					return;
				}

				for (int k = 0; k < alBill.size(); k++) {
					voBill = (GeneralBillVO) alBill.get(k);
					// �õ����ݺţ���˾
					sBillCode = voBill.getParentVO().getAttributeValue("vbillcode") == null ? "" : voBill.getParentVO().getAttributeValue("vbillcode").toString();
					// sPKCorp =
					// voBill.getParentVO().getAttributeValue("pk_corp") == null
					// ? ""
					// : voBill.getParentVO().getAttributeValue("pk_corp")
					// .toString();

					sBillTypeCode = voBill.getBillTypeCode();
					sBillTypeName = GeneralBillVO.getBillTypeName(sBillTypeCode);

					// ����vo
					tvos = (GeneralBillItemVO[]) voBill.getChildrenVO();
					// ��Ҫ���������Ϣһ�𵼳�
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
								// ���ݺ���Ϣ
								vo.setAttributeValue("billcode", sBillCode); // ���ݺ�
								// ���ݺ���Ϣ
								vo.setAttributeValue("billcode", sBillCode); // ���ݺ�
								// ��������Ϣ
								vo.setAttributeValue("rowno", tvos[i].getCrowno()); // �к�
								vo.setAttributeValue("inventorycode", tvos[i].getCinventorycode()); // �������
								vo.setAttributeValue("inventoryname", tvos[i].getInvname()); // �������
								vo.setAttributeValue("billtypecode", sBillTypeCode);
								vo.setAttributeValue("billtypename", sBillTypeName);

								UFDouble dshouldin = null; // Ӧ��
								UFDouble dshouldout = null; // Ӧ��
								UFDouble dShouldnum = null;

								UFDouble din = null; // ʵ��
								UFDouble dout = null; // ʵ��
								UFDouble dnum = null; // Ҫ����������

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
								if (dShouldnum != null) vo.setAttributeValue("shouldnum", dShouldnum.toString()); // ���������ʵ��������
								if (dnum != null) vo.setAttributeValue("nnum", dnum.toString()); // ���������ʵ��������
								vo.setAttributeValue("free", tvos[i].getVfree0()); // ������Ŀ
								vo.setAttributeValue("batchcode", tvos[i].getVbatchcode()); // ���κ�
								vo.setAttributeValue("cgeneralbid", tvos[i].getCgeneralbid()); // ����pk
								vo.setAttributeValue("cgeneralhid", tvos[i].getCgeneralhid()); // ��ͷpk
								// ��ͷpk //������Ϣ
								vo.setBarcode(bvos[j].getVbarcode()); // ������
								vo.setBarcodesub(bvos[j].getVbarcodesub()); // ������
								vo.setPackcode(bvos[j].getVpackcode()); // ������

								v.add(vo);
							}
						} else {
							vo = new ExcelFileVO();
							vo = new ExcelFileVO();
							// ���ݺ���Ϣ
							vo.setAttributeValue("billcode", sBillCode); // ���ݺ�
							// ��������Ϣ
							vo.setAttributeValue("rowno", tvos[i].getCrowno()); // �к�
							vo.setAttributeValue("inventorycode", tvos[i].getCinventorycode()); // �������
							vo.setAttributeValue("inventoryname", tvos[i].getInvname()); // �������
							vo.setAttributeValue("billtypecode", sBillTypeCode);
							vo.setAttributeValue("billtypename", sBillTypeName);

							UFDouble dshouldin = null; // Ӧ��
							UFDouble dshouldout = null; // Ӧ��
							UFDouble dShouldnum = null;

							UFDouble din = null; // ʵ��
							UFDouble dout = null; // ʵ��
							UFDouble dnum = null; // Ҫ����������

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
							if (dShouldnum != null) vo.setAttributeValue("shouldnum", dShouldnum.toString()); // ���������ʵ��������
							if (dnum != null) vo.setAttributeValue("nnum", dnum.toString()); // ���������ʵ��������
							vo.setAttributeValue("free", tvos[i].getVfree0()); // ������Ŀ
							vo.setAttributeValue("batchcode", tvos[i].getVbatchcode()); // ���κ�
							vo.setAttributeValue("cgeneralbid", tvos[i].getCgeneralbid()); // ����pk
							vo.setAttributeValue("cgeneralhid", tvos[i].getCgeneralhid()); // ��ͷpk
							v.add(vo);
						}
					}
					// ÿ�ŵ��ݽ�����һ���ļ�
					// �Ѿ��õ�vo��Ȼ�󵼳�vo
					vos = new ExcelFileVO[v.size()];
					v.copyInto(vos);
					// ���õ����ӿ�
					// �ļ����ƹ���Icbill+��˾PK+���ݺ�+".xls"
					// sFilePath = sFilePathDir + "\\" + "Icbill" + sBillCode
					// + ".xls";
					ExcelReadCtrl erc = new ExcelReadCtrl();
					erc.setVOToExcel(vos, sFilePathDir);
					// д״̬
					ExcelReadCtrl erc1 = new ExcelReadCtrl(sFilePathDir, true);
					erc1.setExcelFileFlag(nc.ui.ic.pub.barcodeoffline.IExcelFileFlag.F_NEW);
				}
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000120")/* @res "�������" */);
				showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000120")/* @res "�������" */);
			}
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000121")/* @res "��������" */);
			showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000121")/* @res "��������" */
					+ nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000330")/* @res "��" */
					+ e.getMessage() + "," + nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000331")/* @res "�ļ�·��" */
					+ ":" + sFilePathDir);
		}
	}

	/**
	 * �����ߣ�� ���ܣ���������˺󣩱��浼������
	 */
	public int onImportSignedBillBarcode(GeneralBillVO voUpdatedBill, boolean bCheckNum) throws Exception {

		try {
			// �õ����ݴ��󣬳��� ------------ EXIT -------------------
			nc.vo.ic.pub.bill.Timer timer = new nc.vo.ic.pub.bill.Timer();
			timer.start("@@�޸ı��浥�ݿ�ʼ��");
			if (voUpdatedBill == null || voUpdatedBill.getParentVO() == null || voUpdatedBill.getChildrenVO() == null) {
				SCMEnv.out("Bill is null !");
				throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000055")/*
																													* @res
																													* "����Ϊ�գ�"
																													*/);
			}
			if (bCheckNum) {
				String sMsg = BarcodeparseCtrl.checkNumWithBarNum(voUpdatedBill.getItemVOs(), true);
				if (sMsg != null) {
					MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPPSCMCommon-000059")/* @res "����" */, sMsg);
					return 0;
				}

			}

			// ���õ�������
			voUpdatedBill.setHeaderValue("cbilltypecode", m_sBillTypeCode);
			// 05/07�����Ƶ���Ϊ��ǰ����Ա
			// remark by zhx onSave() set coperatorid into VO
			// voUpdatedBill.setHeaderValue("coperatorid", m_sUserID);
			timer.showExecuteTime("@@���õ������ͣ�");
			GeneralBillVO voBill = (GeneralBillVO) m_voBill.clone();
			timer.showExecuteTime("@@m_voBill.clone()��");
			// ydy 0826
			// ��voUpdatedBill�ϵ�����VO�ŵ�voBill��
			setBarCodeOnUI(voBill, (GeneralBillItemVO[]) voUpdatedBill.getChildrenVO());
			// voBill.setIDItems(voUpdatedBill);

			GeneralBillHeaderVO voHead = voBill.getHeaderVO();
			// ǩ����
			voHead.setCregister(m_sUserID);
			// --------------------------------------------���Բ��ǵ�ǰ��¼��λ�ĵ��ݣ����Բ���Ҫ�޸ĵ�λ��
			voHead.setPk_corp(m_sCorpID);
			// ��Ϊ��¼���ں͵��������ǿ��Բ�ͬ�ģ����Ա���Ҫ��¼���ڡ�
			voHead.setDaccountdate(new nc.vo.pub.lang.UFDate(m_sLogDate));
			// vo����Ҫ����ƽ̨������Ҫ���ɺ�ǩ�ֺ�ĵ���
			voHead.setFbillflag(new Integer(nc.vo.ic.pub.bill.BillStatus.SIGNED));
			voHead.setCoperatoridnow(m_sUserID); // ��ǰ����Ա2002-04-10.wnj
			voHead.setAttributeValue("clogdatenow", m_sLogDate); // ��ǰ��¼����2003-01-05
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
																																* ".\nȨ��У�鲻ͨ��,����ʧ��! "
																																*/);
				getAccreditLoginDialog().setCorpID(m_sCorpID);
				getAccreditLoginDialog().clearPassWord();
				if (getAccreditLoginDialog().showModal() == nc.ui.pub.beans.UIDialog.ID_OK) {
					String sUserID = getAccreditLoginDialog().getUserID();
					if (sUserID == null) {
						throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000070")/*
																															* @res
																															* "Ȩ��У�鲻ͨ��,����ʧ��. "
																															*/);
					} else {
						voUpdatedBill.setAccreditUserID(sUserID);
						alRetData = onImportSignedBillBarcodeKernel(voBill, voUpdatedBill);
					}
				} else {
					throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000070")/*
																														* @res
																														* "Ȩ��У�鲻ͨ��,����ʧ��. "
																														*/);

				}
			}

			if (alRetData == null || alRetData.size() < 0) {
				SCMEnv.out("return data error.");
				throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000056")/*
																													* @res
																													* "�����Ѿ����棬������ֵ���������²�ѯ���ݡ�"
																													*/);
			}
			// 0 ---- ��ʾ��ʾ��Ϣ
			if (alRetData.get(0) != null && alRetData.get(0).toString().trim().length() > 0) showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000122")/* @res "���뱣��ɹ���" */
					+ (String) alRetData.get(0));
			else showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000122")/* @res "���뱣��ɹ���" */);

			// ###################################################
			// �������õ��ݱ�ͷts
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
			// ��������״̬
			setBillBCVOStatus(m_voBill, nc.vo.pub.VOStatus.UNCHANGED);
			// hanwei 2004-0916
			setBillBCVOStatus(voUpdatedBill, nc.vo.pub.VOStatus.UNCHANGED);

			// ��Ӵ˷�������������VOΪ�պ�û�����m_voBill��Ӧ������VO
			m_voBill.setIDClearBarcodeItems(voUpdatedBill);

			// ���½���ʱ��ts
			getGenBillUICtl().setBillCardPanelData(getBillCardPanel(), smbillvo);

			timer.showExecuteTime(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000123")/*
																												* @res
																												* "@@m_voBill.setIDItems(voUpdatedBill)��"
																												*/);

			// ˢ���б�����
			updateBillToList(m_voBill);
			timer.showExecuteTime(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000124")/*
																												* @res
																												* "@@ˢ���б�����updateBillToList(m_voBill)��"
																												*/);

		} catch (java.net.ConnectException ex1) {
			SCMEnv.out(ex1.getMessage());
			if (showYesNoMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000104")/*
																												* @res
																												* "��ǰ�����жϣ��Ƿ񽫵�����Ϣ���浽Ĭ��Ŀ¼��"
																												*/
			) == MessageDialog.ID_YES) {
				onBillExcel(1); // ���浥����Ϣ��Ĭ��Ŀ¼
			}
		} catch (Exception e) {
			// �쳣�����׳����������̴�����Ϊ��Ӱ�������̡�
			throw e;

		}
		return 1;
	}

	/**
	 * ?user> ���ܣ����������ĵ����е��������õ�BillVO�� ������ ���أ� ���⣺ ���ڣ�(2004-8-24 14:02:09)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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

	// ���������ֶ�
	protected String m_sNumbarcodeItemKey = "nbarcodenum";

	/**
	 * �����ߣ������� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2001-10-30 15:06:35) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
	 * ���˫���¼� �������ڣ�(2001-6-20 17:19:03)
	 */
	public void mouse_doubleclick(nc.ui.pub.bill.BillMouseEnent e) {
		if (e.getPos() == BillItem.HEAD) {

			onSwitch();
		}

	}

	/**
	 * � ���ܣ�ִ������ر� ������ ���أ� ���⣺ ���ڣ�(2004-10-18 10:37:47) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @param iChoose
	 *            int
	 */
	public void onBarcodeOpenClose(int iChoose) {

		int[] iaSel = null;
		// get the selection of card or list
		if (BillMode.List == m_iCurPanel) {
			showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000125")/* @res "���ڿ�Ƭģʽ�¹رմ�����" */);
			return;
		} else {
			iaSel = getBillCardPanel().getBillTable().getSelectedRows();
		}
		if (m_iMode == BillMode.New) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000126")/* @res "����״̬���ܹرմ����룡" */);
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
	 * ����һ���ڲ���. �̳�IFreshTsListener. ʵ�ִ�ӡ���ts��printcount�ĸ���. @author �۱� ����ʱ��:
	 * 2004-12-23
	 */
	public class FreshTsListener implements IFreshTsListener {

		/*
		 * ���� Javadoc��
		 * 
		 * @see nc.ui.scm.print.IFreshTsListener#freshTs(java.lang.String,
		 * java.lang.String)
		 */
		public void freshTs(String sBillID, String sTS, Integer iPrintCount) {
			// fresh local TS with voPr.getNewTs();

			SCMEnv.out("new Ts = " + sTS);
			SCMEnv.out("new iPrintCount = " + iPrintCount);

			if (m_alListData == null || m_alListData.size() == 0) return;

			// �жϴ�ӡ��vo�Ƿ����ڻ����У�
			// �ڴ�ӡԤ��״̬��ӡʱ������vo���ܻ��иı䣬����Ҫ�жϣ�
			int index = 0;
			GeneralBillVO voBill = null;
			GeneralBillHeaderVO headerVO = null;
			for (; index < m_alListData.size(); index++) {
				voBill = (GeneralBillVO) m_alListData.get(index);
				headerVO = voBill.getHeaderVO();

				// ��sBillID����ʱ���Ѿ��ж�sBillID��Ϊnull.
				if (sBillID.equals(headerVO.getPrimaryKey())) break;
			}

			if (index == m_alListData.size()) // ���ڻ���vo�У�������и��£�
			return;

			// �ڻ���vo��
			headerVO.setAttributeValue("ts", sTS);
			headerVO.setAttributeValue("iprintcount", iPrintCount);

			if (m_iCurPanel == BillMode.Card) { // Card
				if (index == m_iCurDispBillNum) { // ���Ϊ��ǰcard��ʾvo.
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
	 * ���� Javadoc��
	 * 
	 * @see nc.ui.scm.pub.bill.IBillExtendFun#getExtendBtns()
	 */
	public ButtonObject[] getExtendBtns() {
		return null;
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see
	 * nc.ui.scm.pub.bill.IBillExtendFun#onExtendBtnsClick(nc.ui.pub.ButtonObject
	 * )
	 */
	public void onExtendBtnsClick(ButtonObject bo) {

	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see nc.ui.scm.pub.bill.IBillExtendFun#setExtendBtnsStat(int)
	 * BillMode.New �������� BillMode.Browse ��� BillMode.Update �޸�
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
	 * get �������ڣ�(2001-10-26 14:31:14)
	 * 
	 * @param key
	 *            java.lang.String
	 */
	protected ClientUISortCtl getListHeadSortCtl() {
		return m_listHeadSortCtl;
	}

	/**
	 * get �������ڣ�(2001-10-26 14:31:14)
	 * 
	 * @param key
	 *            java.lang.String
	 */
	protected ClientUISortCtl getListBodySortCtl() {
		return m_listBodySortCtl;
	}

	/**
	 * get �������ڣ�(2001-10-26 14:31:14)
	 * 
	 * @param key
	 *            java.lang.String
	 */
	protected ClientUISortCtl getCardBodySortCtl() {
		return m_cardBodySortCtl;
	}

	/**
	 * ����󴥷��� �������ڣ�(2001-10-26 14:31:14)
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
	 * ����ǰ������ �������ڣ�(2001-10-26 14:31:14)
	 * 
	 * @param key
	 *            java.lang.String
	 */
	public void beforeSortEvent(boolean iscard, boolean ishead, String key) {

		clearOrientColor();
		// ����Ǳ�ͷ����
		if (ishead) {
			SCMEnv.out("��ͷ����");
			if (m_alListData == null || m_alListData.size() <= 0) {
				// ˵��û������ı�Ҫ
				return;
			}
			getListHeadSortCtl().addRelaSortData(m_alListData);

		} else {
			SCMEnv.out("��������");

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
	 * �б��ͷ����󴥷�,��ǰ�б仯 �������ڣ�(2001-10-26 14:31:14)
	 * 
	 * @param key
	 *            java.lang.String
	 */
	public void currentRowChange(int newrow) {

		if (newrow >= 0) {
			if (m_iLastSelListHeadRow != newrow) {
				setLastHeadRow(newrow);
				selectListBill(m_iLastSelListHeadRow); // ����
				setButtonStatus(true);
			}
		} else {
			if (m_iLastSelListHeadRow < 0 || m_iLastSelListHeadRow >= getBillListPanel().getHeadBillModel().getRowCount()) m_iLastSelListHeadRow = 0;
			selectListBill(m_iLastSelListHeadRow); // ����
			setButtonStatus(true);
		}
	}

	/**
	 * ��������:�˳�ϵͳ
	 */
	public boolean onClosing() {
		// ���ڱ༭����ʱ�˳���ʾ
		if (m_iMode != BillMode.Browse) {

			int iret = MessageDialog.showYesNoCancelDlg(this, null, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH001")/* @res "�Ƿ񱣴����޸ĵ����ݣ�" */);
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
		// �鵥��
		ICDataSet datas = nc.ui.ic.pub.tools.GenMethod.queryData("ic_general_h", "cgeneralhid", new String[] {
			"pk_corp"
		}, new int[] {
			SmartFieldMeta.JAVATYPE_STRING
		}, new String[] {
			billid
		}, " dr=0 ");
		String cbillpkcorp = datas == null ? null : (String) datas.getValueAt(0, 0);

		if (cbillpkcorp == null || cbillpkcorp.trim().length() <= 0) nc.ui.pub.beans.MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/* @res "��ʾ" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000062")/* @res "û�з��ϲ�ѯ�����ĵ��ݣ�" */);
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
				// ����繫˾����ҵ��Ա����
				convos = nc.ui.ic.pub.tools.GenMethod.procMultCorpDeptBizDP(convos, billtype, cbillpkcorp);
			}
			GeneralBillVO voBill = qryBill(cbillpkcorp, billtype, null, m_sUserID, billid, convos);
			if (voBill == null) {
				nc.ui.pub.beans.MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/*
																																				 * @res "��ʾ"
																																				 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000062")/*
																																																									* @res "û�з��ϲ�ѯ�����ĵ��ݣ�"
																																																									*/);
				return;
			}

			// ��ʼ������
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
	 * UI��������-����
	 * 
	 * @author leijun 2006-5-24
	 */
	public void doAddAction(ILinkAddData adddata) {
		if (adddata == null) return;
		GeneralBillVO[] billvos = getBillVOs(adddata);
		if (billvos == null || billvos.length <= 0) return;

		try {
			// v5 lj ֧�ֶ��ŵ��ݲ�������
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
	 * UI��������-����--��ȡԴ����
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

		// ����Դ���ݷֵ�
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
	 * UI��������-����
	 * 
	 * @author cch 2006-5-9-11:04:16
	 */
	public void doApproveAction(ILinkApproveData approvedata) {
		queryForLinkOper(approvedata.getPkOrg(), approvedata.getBillType(), approvedata.getBillID());
	}

	/**
	 * UI��������-ά��
	 * 
	 * @author leijun 2006-5-24
	 */
	public void doMaintainAction(ILinkMaintainData maintaindata) {
		queryForLinkOper(getClientEnvironment().getCorporation().getPrimaryKey(), getBillTypeCode(), maintaindata.getBillID());
		onUpdate();
	}

	/**
	 * showBtnSwitch ���Ͻ���淶
	 * 
	 * @author leijun 2006-5-24
	 */
	public void showBtnSwitch() {
		if (m_iCurPanel == BillMode.Card) getButtonTree().getButton(ICButtonConst.BTN_SWITCH).setName(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH022")/* @res "�б���ʾ" */);
		else getButtonTree().getButton(ICButtonConst.BTN_SWITCH).setName(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH021")/* @res "��Ƭ��ʾ" */);
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
	 * �����ߣ����˾� ���ܣ��Ƿ��ת����
	 * 
	 * 
	 * ������ ���أ� ���⣺ ���ڣ�(2001-11-24 12:15:42) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	protected boolean isBrwLendBiztype() {
		return false;

	}

	/**
	 * �����ߣ������� ���ܣ����ݵ������Ϳ��ư�ť
	 * 
	 * 
	 * ������ ���أ� ���⣺ ���ڣ�(2007-04-05 17:00:00) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */

	protected void ctrlBillTypeButtons(boolean willDo) {

		if (willDo) {

		}

	}

	/**
	 * �����ߣ������� ���ܣ����ݵ������Ϳ��ư�ť
	 * 
	 * 
	 * ������ ���أ� ���⣺ ���ڣ�(2007-04-06 10:26:00) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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

	protected UIMenuItem miAddNewRowNo = new UIMenuItem("�����к�");

	protected int m_Menu_AddNewRowNO_Index = -1;

	protected UIMenuItem getAddNewRowNoItem() {
		return miAddNewRowNo;
	}

	/**
	 * �����ˣ������� �������ڣ�2007-12-26����09:27:52 ����ԭ���Զ����ý��������е������е��к�
	 * 
	 */
	protected void onAddNewRowNo() {

		nc.ui.scm.pub.report.BillRowNo.addNewRowNo(getBillCardPanel(), m_sBillTypeCode, m_sBillRowNo);

		for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
			if (getBillCardPanel().getBillModel().getRowState(i) == BillModel.NORMAL) getBillCardPanel().getBillModel().setRowState(i, BillModel.MODIFICATION);
		}
	}

	/**
	 * @����:���ع�˾���ϼ���˾����
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
	 * add by shikun 2014-04-08 ȡ�ÿ���޸�������
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
			//�޸����ִ�����
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
	 * add by shikun �����ΰ�ť��������ȥ���������� ��������ˢ�� 
	 * */
	private void subGLSL() {
		int rows = getBillCardPanel().getBillTable().getRowCount();
		//��������ݲ�Ҫ�����ó��⡢�ƿ⡢��λ����������������� add by shikun
		if (rows>0) {
			for (int i = 0; i < rows; i++) {
				UFDouble num = getWGLnum(i);
				UFDouble noutnum = getBillCardPanel().getBodyValueAt(i, "noutnum")==null? new UFDouble(0.00):new UFDouble(getBillCardPanel().getBodyValueAt(i, "noutnum").toString());
				if (noutnum.doubleValue()!=0&&noutnum.doubleValue()<num.doubleValue()) {//����������Ϊ�գ����ұ�������С����Ч��������ô�����������䣻�����������Ϊ�ջ򱾵�����������Ч��������ȡ��Ч����
					num = noutnum;
				}
				getBillCardPanel().setBodyValueAt(num, i, "noutnum");
			}
		}
		
	}
	
}