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
 * ��������:�빺��ά������
 */
public class PrayUI extends nc.ui.pub.ToftPanel implements BillEditListener,
		BillTableMouseListener, ListSelectionListener, BillBodyMenuListener,
		ICheckRetVO, BillEditListener2, IBillModelSortPrepareListener,
		ISetBillVO, IBillExtendFun, BillCardBeforeEditListener,
		IBillRelaSortListener2, ILinkMaintain,// �����޸�
		ILinkAdd,// ��������
		ILinkApprove,// ������
		ILinkQuery,// ������
		BillSortListener// �������
{
	// �б��Ƿ���ع�
	private boolean m_bLoaded = false;
	// ��ť��ʵ��,since v51
	private ButtonTree m_btnTree = null;
	/**
	 * ������ư�ť����
	 */
	private ButtonObject m_btnBusiTypes = null;// ҵ������
	private ButtonObject m_btnAdds = null;// ����
	// �в���
	private ButtonObject m_btnLines = null;
	private ButtonObject m_btnAddLine = null;
	private ButtonObject m_btnDelLine = null;
	private ButtonObject m_btnInsLine = null;
	private ButtonObject m_btnCpyLine = null;
	private ButtonObject m_btnPstLine = null;
	// ά��
	private ButtonObject m_btnMaintains = null;
	private ButtonObject m_btnModify = null;
	private ButtonObject m_btnSave = null;
	private ButtonObject m_btnCancel = null;
	private ButtonObject m_btnDiscard = null;
	private ButtonObject m_btnCopy = null;
	private ButtonObject m_btnSendAudit = null;
	// ִ��/
	private ButtonObject m_btnActions = null;
	private ButtonObject m_btnApprove = null;
	private ButtonObject m_btnUnApprove = null;
	private ButtonObject m_btnClose = null;
	private ButtonObject m_btnOpen = null;
	// ���
	private ButtonObject m_btnBrowses = null;
	private ButtonObject m_btnQuery = null;
	// add by zip:2014/4/4 No 24
	private ButtonObject btnCKKCXX = new ButtonObject("�ο������Ϣ", "�ο������Ϣ","�ο������Ϣ");;
	// add end
	private ButtonObject m_btnFirst = null;
	private ButtonObject m_btnPrev = null;
	private ButtonObject m_btnNext = null;
	private ButtonObject m_btnLast = null;
	private ButtonObject m_btnRefresh = null;
	private ButtonObject m_btnRefreshList = null;
	//
	private ButtonObject m_btnList = null;
	/* ��Ƭ����Ϣ���Ĺ��� */
	private ButtonObject m_btnOthersFuncs = null;// "��������"
	private ButtonObject m_btnDocument = null;// "�ĵ�����"
	private ButtonObject m_btnOthersQry = null;// "������ѯ"
	private ButtonObject m_btnPriceInfo = null;// "�۸���֤��"
	private ButtonObject m_btnWorkFlowBrowse = null; // ״̬��ѯ
	private ButtonObject m_btnUsable = null;// "������ѯ"
	public ButtonObject m_btnCombin = null;// "�ϲ���ʾ"
	private ButtonObject m_btnLinkBillsBrowse = null;// "����"
	// ��ӡ
	private ButtonObject m_btnPrints = null;// "��ӡ"
	private ButtonObject m_btnPrint = null;// "��ӡ"
	private ButtonObject m_btnPrintPreview = null;// "Ԥ��"
	private ButtonObject m_btnPrintList = null;// "�б��ӡ"
	private ButtonObject m_btnPrintListPreview = null;// "Ԥ��"
	// �б�ť����
	private ButtonObject m_btnSelectAll = null;// "ȫѡ"
	private ButtonObject m_btnSelectNo = null;// "ȫ��"
	private ButtonObject m_btnModifyList = null;// "�б��޸�"
	private ButtonObject m_btnDiscardList = null;// "�б�����"
	// �б��ѯ
	private ButtonObject m_btnQueryList = null;// "�б��ѯ"
	private ButtonObject m_btnCard = null;// "�л�"
	private ButtonObject m_btnQueryForAuditList = null;// "�б�״̬��ѯ"
	private ButtonObject m_btnUsableList = null;// "�б������ѯ"
	private ButtonObject m_btnDocumentList = null;// "�б��ĵ�����"
	// ��Ϣ���İ�ť��(����,״̬��ѯ,�ĵ�����)
	private ButtonObject m_btnAudit = null;// "����"
	private ButtonObject m_btnUnAudit = null;// "����"
	private ButtonObject m_btnOthersAuditCenter = null;// "����"
	private ButtonObject[] m_btnsAuditCenter = null;// ��Ϣ���İ�ť

	// �ɹ���˾(��¼�༭ǰֵ)---�����ڲ����ϸߵ�����»�������
	private static String m_strPurCorpIdOld = null;

	// ��¼�Ƿ��ѯ������ѯ�������á�ˢ�¡�����
	private boolean m_bQueried = false;

	class MyBillData implements IBillData {
		public void prepareBillData(nc.ui.pub.bill.BillData bd) {
			PrayUI.this.initBillBeforeLoad(bd);
		}
	}

	// �Ƿ�����Զ�����(����Ϣ���ĵ��빺�������Զ�����)
	private boolean isCanAutoAddLine = true;

	/** ����ʱ�����⴦�� */
	private boolean isFrmCopy = false;

	private ATPForOneInvMulCorpUI m_atpDlg = null;

	// �Ƿ������빺��(���������빺��)
	private boolean m_bAdd = false;

	// ȡ����ť�Ƿ��Ѿ�����
	private boolean m_bCancel = false;

	private boolean m_bEdit = false;

	// �Ƿ�������Ƶ���
	private boolean isAllowedModifyByOther = false;

	// ���ݼ�������
	private BillCardPanel m_billPanel = null;

	// �Ƿ��¸����˵�
	private boolean m_bIsSubMenuPressed = false;

	// ��ǰ������ҵ������
	private ButtonObject m_bizButton = null;

	// ���﷭�빤����
	private static NCLangRes m_lanResTool = NCLangRes.getInstance();

	// �����������
	class IBillRelaSortListener2Body implements IBillRelaSortListener2 {
		public Object[] getRelaSortObjectArray() {
			return PrayUI.this.getRelaSortObjectArrayBody();
		}
	}

	/** �������õ�����ID */
	private String m_cauditid = null;

	private UIComboBox m_comPraySource = null;

	private UIComboBox m_comPraySource1 = null;

	private UIComboBox m_comPrayType = null;

	private UIComboBox m_comPrayType1 = null;

	// ��ѯģ��
	private PrayUIQueryDlg m_condClient = null;

	// ������С��λ
	// private nc.vo.pub.para.SysInitVO m_exchangeInitVO = null;
	// ������
	private FreeItemRefPane m_freeItem = null;

	/* �������ݳ�ʼ״̬������ */
	// private int m_iInitRowCount = 20;
	/* ��¼ִ�а�ť���ڿ�Ƭ��ť���е�λ�� */
	private final int m_iPOS_m_btnAction = 5;

	// �Ƿ�Ϊ�۸���֤��
	private BillListPanel m_listPanel = null;

	// ��ǰ���빺����ţ�Ϊ���·�ҳ����
	private int m_nPresentRecord = 0;

	// �����б�/��Ƭ״̬
	private int m_nUIState = 0;

	// �۸���֤���ѯ�Ի���
	private QueryConditionClient m_priceDlg = null;

	private PrintEntry m_print = null;

	// ������ӡ�����б��ͷ�����к�
	protected ArrayList listSelectBillsPos = null;

	// �빺����ID����
	private String m_sDeptID = null;

	// ����������ֶ���
	private final String m_sInvMngIDItemKey = "cinventorycode";

	// ��λ���룬ϵͳӦ�ṩ������ȡ
	private String m_sLoginCorpId = getCorpPrimaryKey();

	// ���棬����빺������
	private PraybillVO[] m_VOs = null;

	private int nAssistUnitDecimal = 2;

	private int nExchangeDecimal = 2;

	/** ���ݾ��� */
	private int nMeasDecimal = 2;

	private int nMoneyDecimal = 2;

	private int nPriceDecimal = 2;

	/* ������ӡ���� */
	private ScmPrintTool printList = null; // �����

	private ScmPrintTool printCard = null;

	/* ��ǰ��¼����Ա��Ȩ�޵Ĺ�˾[] */
	private String[] saPkCorp = null;

	// �Ƿ���Ҫ����
	private PraybillVO m_SaveVOs = null;

	private boolean isAlreadySendToAudit = false;

	// ��ǰ��¼�˺͵�½����
	private String m_sLoginDate = getClientEnvironment().getDate().toString();

	// ������д���ҵ������
	Hashtable m_hashBizType = new Hashtable();

	Hashtable isWorkFlow = new Hashtable();

	Hashtable m_hashInvbasIds = new Hashtable();

	// ���Ĭ�ϲ���
	UIRefPane invrefpane = null;

	// add by zip:2013/12/3
	// no 54
	@SuppressWarnings("rawtypes")
	public void setFixedData() throws Exception {
		IUAPQueryBS jdbc = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		String pk_corp = ClientEnvironment.getInstance().getCorporation().getPk_corp();
		String sql = "select pk_calbody,bodycode,bodyname from bd_calbody where pk_corp='"+pk_corp+"' and bodycode='01'";
		Object obj = jdbc.executeQuery(sql, new MapProcessor());
		// ��������֯
		getBillCardPanel().getBodyItem("pk_reqstoorg").setDefaultValue((String)((Map)obj).get("pk_calbody"));
		getBillCardPanel().getBodyItem("reqstoorgname").setDefaultValue((String)((Map)obj).get("bodyname"));
		// ����ֿ�
		sql = "select pk_stordoc,storcode,storname from bd_stordoc where pk_corp='"+pk_corp+"' and pk_calbody='"+(String)((Map)obj).get("pk_calbody")+"' and storcode='B01'";
		obj = jdbc.executeQuery(sql, new MapProcessor());
		getBillCardPanel().getBodyItem("cwarehouseid").setDefaultValue((String)((Map)obj).get("pk_stordoc"));
		getBillCardPanel().getBodyItem("cwarehousename").setDefaultValue((String)((Map)obj).get("storname"));
	}
	// end
	
	/**
	 * PraybillClient ������ע�⡣
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
	 * PrayUI ������ע�⡣
	 */
	public PrayUI(String pk_corp, String billType, String businessType,
			String operator, String billID) {

		super();

		initi();

		PraybillVO vo = null;

		try {
			vo = PraybillHelper.queryPrayVoByHid(billID);
			if (vo != null) {
				// Logger.debug("��ѯ������");
				m_VOs = new PraybillVO[] { vo };
				m_nPresentRecord = 0;
				setVoToBillCard(m_nPresentRecord, "");
				Logger.debug("�ɹ���ʾ����");
			}
		} catch (Exception e) {
			SCMEnv.out(e);
		}
	}

	/**
	 * ��������:��Ӧ�¼�����
	 * 
	 * @param event
	 *            nc.ui.pub.bill.BillEditEvent
	 */
	public void actionPerformed(java.awt.event.ActionEvent event) {
	}

	/**
	 * ��������:�Զ������PK(��ͷ)
	 */
	public static void afterEditWhenHeadDefPK(BillCardPanel bcp, BillEditEvent e) {
		DefSetTool.afterEditHead(bcp.getBillData(), e.getKey(), "pk_defdoc"
				+ e.getKey().substring("vdef".length(), e.getKey().length()));
	}

	/**
	 * ��������:�Զ������PK(����)
	 */
	public static void afterEditWhenBodyDefPK(BillCardPanel bcp, BillEditEvent e) {

		DefSetTool.afterEditBody(bcp.getBillModel(), e.getRow(), e.getKey(),
				"pk_defdoc"
						+ e.getKey().substring("vdef".length(),
								e.getKey().length()));
	}

	/**
	 * ��������:�༭���¼�
	 * 
	 * @param e
	 *            nc.ui.pub.bill.BillEditEvent
	 */
	public void afterEdit(BillEditEvent e) {

		showHintMessage(m_lanResTool
				.getStrByID("common", "4004COMMON000000075")/* @res "���ڱ༭" */);

		if (m_nUIState == 0 && m_bEdit) {
			if (e.getPos() == BillItem.HEAD) {
				// �Զ������PK
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
				// �Զ������PK
				afterEditWhenBodyDefPK(getBillCardPanel(), e);
				// �ɹ�Ա
				if ("cemployeename".equals(e.getKey())) {
					afterEditWhenBodyEmployee(this, getBillCardPanel(),
							m_sLoginCorpId, e);
				}
				// ��Ŀ
				else if ("cprojectname".equals(e.getKey())) {
					afterEditWhenBodyProj(getBillCardPanel(), e);
				}
				// ������
				else if (e.getKey().trim().equals("vfree")) {
					afterEditWhenBodyFree(getBillCardPanel(), e);
				}
				// �������
				else if (e.getKey().trim().equals("cinventorycode")) {
					afterEditWhenBodyInventory(this, getBillCardPanel(),
							m_sLoginCorpId, getClientEnvironment()
									.getCorporation().getUnitname(), e);
				}
				// ��ע
				else if (e.getKey().trim().equals("vmemo")) {
					afterEditWhenBodyMemo(getBillCardPanel(), e);
				}
				// �к�
				else if (e.getKey().equals("crowno")) {
					afterEditWhenBodyRowNo(getBillCardPanel(), e, "crowno");
				}
				// �ɹ���֯
				else if (e.getKey().equals("cpurorganizationname")) {
					afterEditWhenBodyPurOrg(this, getBillCardPanel(),
							m_sLoginCorpId, e);
				}
				// �ɹ���˾
				else if (e.getKey().equals("pk_purcorp")) {
					afterEditWhenBodyPurCorp(getBillCardPanel(), e);
				}
				// ��������֯
				else if (e.getKey().equals("reqstoorgname")) {
					afterEditWhenBodyReqStoOrg(getBillCardPanel(), e);
				}
				// ����ֿ�
				else if (e.getKey().equals("cwarehousename")) {
					afterEditWhenBodyReqWareHouse(getBillCardPanel(), e);
				}
				// ���鹩Ӧ��
				else if (e.getKey().equals("cvendorname")) {
					afterEditWhenBodyVendor(getBillCardPanel(), e);
				}
				// ��������
				else if (e.getKey().trim().equals("ddemanddate")) {
					afterEditWhenBodyDemandDate(this, getBillCardPanel(), e);
				}
				// ������
				else if (e.getKey().trim().equals("npraynum")) {
					afterEditWhenBodyNum(this, getBillCardPanel(), e);
				}
				// ������
				else if (e.getKey().trim().equals("nassistnum")) {
					afterEditWhenBodyAssNum(this, getBillCardPanel(), e);
				}
				// ������
				else if (e.getKey().trim().equals("nexchangerate")) {
					afterEditWhenBodyRate(this, getBillCardPanel(), e);
				}
				// ������
				else if (e.getKey().trim().equals("cassistunitname")) {
					afterEditWhenBodyAssist(this, getBillCardPanel(), e);
				}
				// ���鵥��
				else if (e.getKey().trim().equals("nsuggestprice")) {
					afterEditWhenBodySuggPrice(this, getBillCardPanel(), e);
				}
				// ���
				else if (e.getKey().trim().equals("nmoney")) {
					afterEditWhenBodyMoney(this, getBillCardPanel(), e);
				}
			}
		}
		// �ޱ�����
		if (nc.ui.pu.pub.PuTool.isLastCom(getBillCardPanel(), e)
				&& getBillCardPanel().getBillModel().getRowCount() <= 0) {
			onAppendLine(getBillCardPanel(), this);
		}
		PuTool.setFocusOnLastCom(getBillCardPanel(), e);
		// ��������������ٴ�ѡ����ִ���ԭ���������
		UIRefPane refpane = (UIRefPane) getBillCardPanel().getBodyItem(
				"cinventorycode").getComponent();
		refpane.setPK(null);
		UIRefPane refpaneM = (UIRefPane) getBillCardPanel().getBodyItem(
				"cvendorname").getComponent();
		refpaneM.setPK(null);
	}

	/**
	 * �༭���¼�--��������ֿ�
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
			SCMEnv.out("�޸�����ֿ�ʱ��������������֯������˾Ĭ��ֵ,�ֿ�Ϊ�գ�ֱ�ӷ��ء�");
			return;
		}
		Object[] oaRet = null;
		try {
			// ������������֯
			if (strPkCalBody == null) {
				oaRet = (Object[]) CacheTool.getCellValue("bd_stordoc",
						"pk_stordoc", "pk_calbody", strPkWare);
				if (oaRet == null || oaRet.length == 0 || oaRet[0] == null
						|| oaRet[0].toString().trim().length() == 0) {
					SCMEnv
							.out("���ݲֿ⵵��ID[IDֵ����" + strPkWare
									+ "��]���ܻ�ȡ���������֯ID!");
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
						SCMEnv.out("�������������֯ID[IDֵ����" + strPkCalBody
								+ "��]���ܻ�ȡ���������֯����!");
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
	 * �༭���¼�--������������֯
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
	 * �༭���¼�--���屸ע
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
	 * �༭���¼�--�к�
	 * 
	 * @param bcp
	 * @param e
	 */
	public static void afterEditWhenBodyRowNo(BillCardPanel bcp,
			BillEditEvent e, String strRowNoKey) {
		BillRowNo.afterEditWhenRowNo(bcp, e, strRowNoKey);
	}

	/**
	 * �༭���¼�--�ɹ���˾
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
	 * �༭���¼�--������
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
		// �޸ļ���������,�빺�����仯
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
				 * @res "��������"
				 */, m_lanResTool.getStrByID("40040101", "UPP40040101-000045")
				/*
				 * @res "���������ʲ���Ϊ����"
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
			// �빺�����仯,����Զ��仯
			nPrayNum = PuPubVO.getUFDouble_NullAsZero(bcp.getBodyValueAt(e
					.getRow(), "npraynum"));
			if (nSuggestPrice != null) {
				final double d = nPrayNum.doubleValue()
						* nSuggestPrice.doubleValue();
				bcp.setBodyValueAt(new UFDouble(d), e.getRow(), "nmoney");
			}
			// �����빺�����Ͳɹ���ǰ�ڣ��Զ��޸��������ںͽ��鶩������
			// ������������Ѿ�����,���������ڲ��޸�.
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
	 * �༭���¼�--��������
	 * 
	 * @param bcp
	 * @param e
	 */
	public static void afterEditWhenBodyDemandDate(ToftPanel ui,
			BillCardPanel bcp, BillEditEvent e) {
		// �޸���������,���鶩�������Զ��仯
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
	 * �༭���¼�--������
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
		// �޸ĸ�����,�빺�����仯
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
						 * @res "��������"
						 */, m_lanResTool.getStrByID("40040101",
								"UPP40040101-000044")/* @res "����������Ϊ����" */);
				return;
			}
			// �����Ƿǹ̶������ʣ����Բ����� m_nExchangeRate, Ҫ��ģ����ȡ
			Object exc = bcp.getBillModel().getValueAt(e.getRow(),
					"nexchangerate");
			if (exc != null && !exc.toString().trim().equals(""))
				nExchangeRate = new UFDouble(exc.toString().trim());
			if (nExchangeRate != null) {
				final double d = nAssistNum.doubleValue()
						* nExchangeRate.doubleValue();
				bcp.setBodyValueAt(new UFDouble(d), e.getRow(), "npraynum");
			}
			// �빺�����仯,����Զ��仯
			nPrayNum = (UFDouble) bcp.getBodyValueAt(e.getRow(), "npraynum");
			if (nSuggestPrice != null && nPrayNum != null) {
				final double d = nPrayNum.doubleValue()
						* nSuggestPrice.doubleValue();
				bcp.setBodyValueAt(new UFDouble(d), e.getRow(), "nmoney");
			}
			// �����빺�����Ͳɹ���ǰ�ڣ��Զ��޸��������ںͽ��鶩������
			// ������������Ѿ�����,���������ڲ��޸�.
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
	 * �༭���¼�--���
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
		// ���仯�����鵥���Զ��仯
		if (nMoney != null) {
			if (nMoney.doubleValue() < 0) {
				MessageDialog.showErrorDlg(ui, m_lanResTool.getStrByID(
						"40040101", "UPP40040101-000040")/* "��������" */,
						m_lanResTool.getStrByID("40040101",
								"UPP40040101-000043")/* "����Ϊ����" */);
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

				// �̶�������,���������빺�����仯;����,���������빺�����仯
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
	 * �༭���¼�--���鵥��
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
		// ���鵥�۱仯������Զ��仯
		if (nSuggestPrice != null) {
			if (nSuggestPrice.doubleValue() < 0) {
				MessageDialog.showErrorDlg(ui, m_lanResTool.getStrByID(
						"40040101", "UPP40040101-000040")/* "��������" */,
						m_lanResTool.getStrByID("40040101",
								"UPP40040101-000042")/* "���鵥�۲���Ϊ����" */);
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
				// �̶�������,���������빺�����仯;����,���������빺�����仯
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
	 * �༭���¼�--������
	 * 
	 * @param bcp
	 * @param e
	 */
	public static void afterEditWhenBodyAssist(ToftPanel uiPanel,
			BillCardPanel bcp, BillEditEvent e) {
		int iRow = e.getRow();
		// �����������ID
		String sBaseID = (String) bcp.getBillModel()
				.getValueAt(iRow, "cbaseid");
		// ����������
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
		// ��ȡ������
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

		// �����ʸı䣬���¼���
		BillEditEvent tempE = new BillEditEvent(bcp
				.getBodyItem("nexchangerate"), bcp.getBodyValueAt(iRow,
				"nexchangerate"), "nexchangerate", iRow);
		afterEditWhenBodyRate(uiPanel, bcp, tempE);

		// �˴��ſ����ڿɱ༭���ڱ༭ǰ���������
		bcp.setCellEditable(iRow, "npraynum", true);
		bcp.setCellEditable(iRow, "nmoney", true);
		bcp.setCellEditable(iRow, "nexchangerate", true);
		bcp.setCellEditable(iRow, "nassistnum", true);
		bcp.setCellEditable(iRow, "cassistunitname", true);
	}

	/**
	 * �༭���¼�--����
	 * 
	 * @param bcp
	 * @param e
	 */
	public static void afterEditWhenBodyNum(ToftPanel ui, BillCardPanel bcp,
			BillEditEvent e) {
		// ��Ҫ����ı�����
		UFDouble nSuggestPrice = PuPubVO.getUFDouble_NullAsZero(bcp
				.getBodyValueAt(e.getRow(), "nsuggestprice"));
		UFDouble nPrayNum = PuPubVO.getUFDouble_NullAsZero(bcp.getBodyValueAt(e
				.getRow(), "npraynum"));
		// �����������ID
		String sBaseID = (String) bcp.getBillModel().getValueAt(e.getRow(),
				"cbaseid");
		// ����������
		String sCassId = (String) bcp.getBillModel().getValueAt(e.getRow(),
				"cassistunit");
		// ��ȡ������
		UFDouble nExchangeRate = PuTool.getInvConvRateValue(sBaseID, sCassId);
		// �Ƿ�̶�������
		boolean bFixedFlag = PuTool.isFixedConvertRate(sBaseID, sCassId);
		// �޸ı������������Ӧ�仯
		int iRow = e.getRow();
		if (PuTool.isAssUnitManaged(sBaseID)) {
			Object cassistunit = bcp.getBillModel().getValueAt(iRow,
					"cassistunit");
			if ((cassistunit == null)) {
				// MessageDialog.showWarningDlg(ui,
				// m_lanResTool.getStrByID("SCMCOMMON",
				// "UPPSCMCommon-000270")/*"��ʾ"*/,
				// m_lanResTool.getStrByID("40040101", "UPP40040101-000481",
				// null, new String[] { (iRow + 1) + ""
				// })/*"{0}�У��Ǹ������������������븨��λ����������"*/);
				// bcp.getBillModel().setValueAt(null, iRow, e.getKey());
				bcp.getBillModel().setValueAt(null, iRow, "nassistnum");
				calculateAdvDays(ui, bcp, e);
				return;
			}
		}
		// �빺�����仯������Զ��仯
		if (nPrayNum != null) {
		
			if (nPrayNum.doubleValue() < 0) {
				MessageDialog.showErrorDlg(ui, m_lanResTool.getStrByID(
						"40040101", "UPP40040101-000040")/* "��������" */,
						m_lanResTool.getStrByID("40040101",
								"UPP40040101-000041")/* "�빺��������Ϊ����" */);
				bcp.getBillModel().setValueAt(null, iRow, e.getKey());
				return;
			}
			if (nSuggestPrice != null) {
				bcp.setBodyValueAt(nPrayNum.multiply(nSuggestPrice),
						e.getRow(), "nmoney");
			}

			// �̶�������,���������빺�����仯;����,���������빺�����仯
			if (bFixedFlag) {
				if (nExchangeRate != null && nExchangeRate.doubleValue() != 0.0) {
					bcp.setBodyValueAt(nPrayNum.div(nExchangeRate), e.getRow(),
							"nassistnum");
				}
			} else {
				// �ǹ̶�������
				UFDouble nAssistNum = null;
				Object oTemp = bcp.getBodyValueAt(e.getRow(), "nassistnum");
				if (oTemp != null && oTemp.toString().length() > 0)
					nAssistNum = (UFDouble) oTemp;
				if (nAssistNum != null) {
					if (nAssistNum.doubleValue() != 0.0) {
						bcp.setBodyValueAt(nPrayNum.div(nAssistNum),
								e.getRow(), "nexchangerate");
					} else {
						// ������Ϊ0,����޸Ļ�����,�����������/������!=0,��������Ϊ0��ì��
						// Ϊ��,�޸ĸ�����,���ı任����
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
	 * �����빺�����Ͳɹ���ǰ�ڣ��Զ��޸��������ںͽ��鶩������
	 */
	private static void calculateAdvDays(ToftPanel ui, BillCardPanel bcp,
			BillEditEvent e) {
		// 
		// ������������Ѿ�����,���������ڲ��޸�.
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
	 * �༭���¼�-������Ŀ
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
	 * �༭���¼�--������
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
		// ���⵱ǰֵ��������һ�����������
		InvVO invVO = new InvVO();
		((FreeItemRefPane) bcp.getBodyItem("vfree").getComponent())
				.setFreeItemParam(invVO);
	}

	/**
	 * ��ͷ�༭���¼�--�빺��
	 * 
	 * @param e
	 */
	public static void afterEditWhenHeadPsn(BillCardPanel bcp,
			String sDeptIdOld, BillEditEvent e) {
		String sPsnID = bcp.getHeadItem("cpraypsn").getValue();
		if (sPsnID != null && sPsnID.length() > 0) {
			// ��ò���ID����������
			UIRefPane nRefPanel = (UIRefPane) bcp.getHeadItem("cpraypsn")
					.getComponent();
			sDeptIdOld = (String) nRefPanel.getRefValue("bd_psndoc.pk_deptdoc");
			nRefPanel = (UIRefPane) bcp.getHeadItem("cdeptid").getComponent();
			nRefPanel.setPK(sDeptIdOld);
		}
	}

	/**
	 * ��ͷ�༭���¼�
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
				// ����빺�˲������빺����,����빺��
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
	 * �빺���ͱ���ǰ���
	 */
	private boolean checkPraytype(PraybillItemVO bodyVO[]) {
		// ���������������㡰����˾=�빺��������˾=�ɹ���˾��ʱ���빺���Ͳ���ѡ��ǣ��ɹ������_�����ϣ��е�����һ��
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
					"SCMCOMMON", "UPPSCMCommon-000270")/* @res"��ʾ" */,
					m_lanResTool.getStrByID("40040101", "UPP40040101-000528",
							null, null)/*
										 * @res"ֻ�б�������������:����˾=�빺��������˾=�ɹ���˾ʱ,��������ί�ⶩ����"
										 */);
		}
		return b;
	}

	/**
	 * ��������:��Ŀ�༭���¼����������Ŀ
	 */
	public static void afterEditHeadCproject(BillCardPanel bcp, BillEditEvent e) {
		UIRefPane ref = (UIRefPane) bcp.getHeadItem("cprojectidhead")
				.getComponent();
		String sPkCalBody = ref.getRefPK();
		// �����֯������������еļƻ���
		final int size = bcp.getRowCount();
		for (int i = 0; i < size; i++) {
			bcp.getBillModel().setValueAt(sPkCalBody, i, "cprojectid");
		}
	}

	// ���鹩Ӧ�̸ı�, ����ɹ�Ա������, ȡ��Ӧ��ר��ҵ��Ա
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
	 * �ɹ�Ա �༭��
	 */
	public static void afterEditWhenBodyEmployee(ToftPanel uiPanel,
			BillCardPanel bcp, String sLoginCorpId, BillEditEvent event) {

		String sPsnId = PuPubVO.getString_TrimZeroLenAsNull(bcp.getBodyValueAt(
				event.getRow(), "cemployeeid"));
		if (sPsnId == null) {
			return;
		}
		// ����ҵ��ԱĬ�ϲɹ���֯
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
	 * �ɹ���֯ �༭��
	 */
	public static void afterEditWhenBodyPurOrg(ToftPanel uiPanel,
			BillCardPanel bcp, String sLoginCorpId, BillEditEvent event) {
		int currow = event.getRow();

		BillModel bm = bcp.getBillModel();

		String strPurOrgId = PuPubVO.getString_TrimZeroLenAsNull(bm.getValueAt(
				currow, "cpurorganization"));
		// ����ɹ���֯����գ���ȡ��¼��˾Ϊ�ɹ���˾
		if (strPurOrgId == null) {
			bcp.setBodyValueAt(sLoginCorpId, currow, "pk_purcorp");
			bcp.setBodyValueAt(ClientEnvironment.getInstance().getCorporation()
					.getUnitname(), currow, "purcorpname");
		}
		String strPurCorpCurr = (String) bm.getValueAt(event.getRow(),
				"pk_purcorp");
		// �ɹ���˾�仯����ս��鹩Ӧ�̣��ɹ�Ա
		if (!strPurCorpCurr.equals(m_strPurCorpIdOld)) {
			bm.setValueAt(null, currow, "cvendormangid");
			bm.setValueAt(null, currow, "cvendorbaseid");
			bm.setValueAt(null, currow, "cvendorname");
			bm.setValueAt(null, currow, "cemployeeid");
			bm.setValueAt(null, currow, "cemployeename");
		}
		// �빺��˾
		String strCurrCorpId = PuPubVO.getString_TrimZeroLenAsNull(bm
				.getValueAt(event.getRow(), "pk_corp"));
		if (strCurrCorpId == null) {
			strCurrCorpId = ClientEnvironment.getInstance().getCorporation()
					.getPrimaryKey();
		}
		// ѯ��
		setRulePrice(uiPanel, bcp, new String[] { (String) bm.getValueAt(event
				.getRow(), "pk_purcorp") }, new String[] { (String) bm
				.getValueAt(event.getRow(), "cbaseid") }, PuPubVO
				.getString_TrimZeroLenAsNull(bm.getValueAt(event.getRow(),
						"pk_reqstoorg")), strCurrCorpId, event.getRow(), event
				.getRow() + 1);
	}

	/**
	 * ������룺���յ�ǰ��¼��˾������������ǿգ��ɱ༭��Ĭ����ʾ a) �༭�� i. �������գ������Ŀ�԰���V31SP1���� ii.
	 * ����ı�ֵ������ɹ���֯��ֵ�������ɹ���֯Ĭ��ֵ�� 1.
	 * ������Բɣ�ȡ��¼��˾����ǰ����ɹ��Ĳɹ�Ա�����ݲɹ�Ա�����ϵ������Ӧ�Ĳɹ���֯�����ݲɹ���֯����Ĳɹ�Ա��,����ȡһ�� 2.
	 * ����Ǽ��ɣ�ȡ���ɹ����ж���Ĳɹ���֯������˾���������ID�����ɹ���֯�� iii. ����ı�ֵ������ɹ�Ա��ֵ�������ɹ�ԱĬ��ֵ�� 1.
	 * ���������Բɣ���ȡ�ɹ���˾����ǰ����ɹ��Ĳɹ�Ա�����ݲɹ�Ա�����ϵ�� 2.
	 * �������Ǽ��ɣ�ȡ���ɹ����ж���Ĳɹ���֯������˾���������ID�����ɹ���֯�������ҵ��Ա������ȡһ�� iv.
	 * ����ı�ֵ��������鹩Ӧ����ֵ���������鹩Ӧ��Ĭ��ֵ�� 1.
	 * �����������������䵽�ɹ���֯������˾��ȡ���������ID+�ɹ���֯������˾����Ӧ������Ӧ��
	 * 
	 * !!!!!!! ע�⣺����������Ч�����⣬��Ҫ��һ��Ч���Ż�
	 */
	public static void afterEditWhenBodyInventory(ToftPanel uiPanel,
			BillCardPanel bcp, String strReqCorpId, String strCorpName,
			BillEditEvent event) {

		BillModel bm = bcp.getBillModel();
		
		// �رպϼƿ���
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

		// ѡ�е����������һ�������з������
		int iBeginRow = event.getRow();
		int iEndRow = iBeginRow + iLen;
		int selectedCount = iBeginRow;
		if (iBeginRow == nRow - 1) {
			for (int i = 0; i < iLen; i++) {
				bcp.addLine();
				/* ������Ŀ�Զ�Э�� */
				PuTool.setBodyProjectByHeadProject(bcp, "cprojectidhead",
						"cprojectid", "cprojectname", PuBillLineOprType.ADD);
				/* �����к� */
				BillRowNo.addLineRowNo(bcp, BillTypeConst.PO_PRAY, "crowno");
				// ���в������Զ����뵱ǰ��¼��˾
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
						"UPPSCMCommon-000354")/* @res"����ǰ����ѡ������У�" */);
				return;
			}

			for (int i = iBeginRow + 1; i < iBeginRow + iLen; i++) {
				bcp.getBillModel().insertRow(i);
			}
			for (int i = iBeginRow; i < iBeginRow + iLen; i++) {
				// ���в������Զ����뵱ǰ��¼��˾
				bcp.setBodyValueAt(strReqCorpId, i, "pk_reqcorp");
				bcp.setBodyValueAt(strCorpName, i, "reqcorpname");
				bcp.setBodyValueAt(strReqCorpId, i, "pk_purcorp");
				bcp.setBodyValueAt(strCorpName, i, "purcorpname");
			}
			int iFinalEndRow = iBeginRow + iLen;

			// �����к�
			BillRowNo.insertLineRowNos(bcp, BillTypeConst.PO_PRAY, "crowno",
					iFinalEndRow, iLen - 1);

		}
		// ִ�б��幫ʽ��������
		setInvEditFormulaInfo(bcp, refpane, iBeginRow, iEndRow, strReqCorpId,
				strCorpName);

		// ����ִ�д���༭��ʽ
		BillItem it = bcp.getBodyItem(event.getKey());
		if (it.getEditFormulas() != null && it.getEditFormulas().length > 0) {
			bcp.getBillModel().execFormulas(it.getEditFormulas(), iBeginRow,
					iEndRow);
		}
		// �����������Զ������
		setEnabled_BodyFree(bcp, refpane, iBeginRow, iEndRow);

		ArrayList listPurOrgVendor = null;
		/*
		 * <p>Arraylist(�������������IDΪ��) <p> |--�ɹ���֯ <p> |--�ɹ���˾ <p> |--�ɹ�Ա <p>
		 * |--���鹩Ӧ�̼������Ϣ(ArrayList)
		 */
		try {
			// ���Ĭ�ϲɹ���֯�͹�Ӧ������
			listPurOrgVendor = PraybillHelper.queryPurOrgAndVendor(saMangId,
					saBaseId, pk_reqstoorg, strReqCorpId);
			// ��ȡ���ݹ������ļ۸�
			// uPrice = PraybillHelper.getRulePrice(saMangId, saBaseId,
			// pk_reqstoorg, strPk_corp);
		} catch (Exception e) {
			MessageDialog.showErrorDlg(uiPanel, m_lanResTool.getStrByID(
					"40040101", "UPP40040101-000442")/* "Ĭ�ϲɹ���֯�͹�Ӧ��" */, e
					.getMessage());
			return;
		}
		ArrayList listValAll = null;
		ArrayList listValVendor = null;
		String[] saPurCorpId = new String[saMangId.length];
		for (int i = iBeginRow; i < iEndRow; i++) {
			// �ı���ʱ,���������,������,����,����,���,��������,���鶩������
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
			// ��˾����
			bcp.setBodyValueAt(strReqCorpId, i, "pk_corp");
			// ������Ŀ�Զ�Э��
			PuTool.setBodyProjectByHeadPro(bcp, "cprojectidhead", "cprojectid",
					"cprojectname", i);
			// ���òɹ���֯�͹�Ӧ��
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
			// ���κ����
			bm.setValueAt(null, i, "vproducenum");

			// ��������˾
			bm.setValueAt(pk_reqcorp, i, "pk_reqcorp");
			// ������������֯
			bm.setValueAt(pk_reqstoorg, i, "pk_reqstoorg");
			//
			saPurCorpId[i - iBeginRow] = (String) bcp.getBodyValueAt(i,
					"pk_purcorp");
		}
		// ִ�б���༭��ʽ
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
////���깺δ��������
//String sql1 = new StringBuilder().append("select sum(A.npraynum) as rst from po_praybill_b A,po_praybill B").append(" where A.cpraybillid=B.cpraybillid and nvl(A.dr,0)=0 and nvl(B.dr,0)=0").append(" and B.pk_corp='" + pk_corp + "' and B.ibillstatus=0").append(" and B.dpraydate<='" + dateto + "' and B.dpraydate>='" + startDate + "'").append(" and A.cbaseid='" + invbasid + "'").toString();
////���깺����������
//String sql2 = new StringBuilder().append("select sum(A.npraynum) as rst from po_praybill_b A,po_praybill B").append(" where A.cpraybillid=B.cpraybillid and nvl(A.dr,0)=0 and nvl(B.dr,0)=0").append(" and B.pk_corp='" + pk_corp + "' and B.ibillstatus=3").append(" and B.dpraydate<='" + dateto + "' and B.dpraydate>='" + startDate + "'").append(" and A.cbaseid='" + invbasid + "'").toString();
////�ɹ���������
//String sql3 = new StringBuilder().append("select sum(A.nordernum) as rst from po_order_b A,po_order B").append(" where A.corderid=B.corderid").append(" and nvl(A.dr,0)=0 and nvl(B.dr,0)=0").append(" and B.pk_corp='" + pk_corp + "'").append(" and B.dorderdate<='" + dateto + "' and B.dorderdate>='" + startDate + "'").append(" and A.cbaseid='" + invbasid + "'").toString();
////����������
//String sql4 = new StringBuilder().append("select sum(A.narrvnum) as rst from po_arriveorder_b A,po_arriveorder B").append(" where A.carriveorderid=B.carriveorderid").append(" and nvl(A.dr,0)=0 and nvl(B.dr,0)=0").append(" and B.pk_corp='" + pk_corp + "'").append(" and B.dreceivedate<='" + dateto + "' and B.dreceivedate>='" + startDate + "'").append(" and A.cbaseid='" + invbasid + "'").toString();
////����ϸ�����
//String sql5 = new StringBuilder().append("select sum(A.nelignum) as rst from po_arriveorder_b A,po_arriveorder B").append(" where A.carriveorderid=B.carriveorderid").append(" and nvl(A.dr,0)=0 and nvl(B.dr,0)=0").append(" and B.pk_corp='" + pk_corp + "'").append(" and B.dreceivedate<='" + dateto + "' and B.dreceivedate>='" + startDate + "'").append(" and A.cbaseid='" + invbasid + "'").toString();
////�Ѷ�������
//String sql6 = new StringBuilder().append("select sum(nvl(nfreezenum,0)) as rst from ic_freeze where nvl(dr, 0) = 0 and pk_corp = '" + pk_corp + "' and cinvbasid = '" + invbasid + "' and cthawpersonid is null and cspaceid is not null").toString();
////�ִ���
//String sql7 = new StringBuilder().append("select sum(nonhandnum) from ic_onhandnum where nvl(dr,0)=0 and cinvbasid='"+invbasid+"' and pk_corp='"+pk_corp+"'").toString();
////�ٶ�����
//String sql8 = new StringBuilder().append("select zdhd from bd_produce where nvl(dr,0)=0 and pk_invbasdoc='"+invbasid+"' and pk_corp='"+pk_corp+"'").toString();
////��߿��
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
//bm.setValueAt(d1+d2, i, "pk_defdoc10"); // ���깺(��δ����)
//bm.setValueAt(d3 - d4, i, "pk_defdoc11"); // �Ѷ���δ��������
//bm.setValueAt(d7-d6, i, "pk_defdoc12"); // �ִ��������(�ִ���-�Ѷ���)
//bm.setValueAt(d8, i, "pk_defdoc13"); // �ٶ�������
//bm.setValueAt(d9, i, "pk_defdoc14"); // ��߿����
//}
//} catch (Exception e2) {
//e2.printStackTrace();
//}
//
///////////////////////////////////////////////////////		
		// �����빺��
		setRulePrice(uiPanel, bcp, saPurCorpId, saBaseId, pk_reqstoorg,
				strReqCorpId, iBeginRow, iEndRow);
		// �Ƿ񸨼���������������
		PuTool.loadBatchAssistManaged(saBaseId);
		// �Ƿ����κŹ�����������
		PuTool.loadBatchProdNumMngt(saMangId);
		// ������ʾ czp 20050303 ����
		PraybillVO voCurr = (PraybillVO) bcp.getBillValueVO(PraybillVO.class
				.getName(), PraybillHeaderVO.class.getName(),
				PraybillItemVO.class.getName());
		InvAttrCellRenderer ficr = new InvAttrCellRenderer();
		ficr.setFreeItemRenderer(bcp, voCurr);
		// �򿪺ϼƿ���
		bm.setNeedCalculate(bOldNeedCalc);
		refpane.setPK(null);
		// �˴��ſ����ڿɱ༭���ڱ༭ǰ���������

		for (int iRow = iBeginRow; iRow < iEndRow; iRow++) {
			bcp.setCellEditable(iRow, "npraynum", true);
			bcp.setCellEditable(iRow, "nmoney", true);
			bcp.setCellEditable(iRow, "nexchangerate", true);
			bcp.setCellEditable(iRow, "nassistnum", true);
			bcp.setCellEditable(iRow, "cassistunitname", true);
		}

		// ���ø������������������Ϣ
		setRelated_AssistUnit(bcp, uiPanel, iBeginRow, iEndRow);
	}

	/**
	 * ���ߣ���ӡ�� ���ܣ�������������ڡ������޸ĺ���Ӧ������޼۱仯 �ú�����afterEdit������������ڡ����ָı����á� ������ int
	 * iBeginRow ������ͬ�����Ϣ�ı��忪ʼ�� int iEndRow ������ͬ�����Ϣ�ı�������� ���أ��� ���⣺��
	 * ���ڣ�(2002-3-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2003-11-14 wyf ��������
	 */
	private static void setRelated_AssistUnit(BillCardPanel bcp,
			ToftPanel uiPanel, int iBeginRow, int iEndRow) {
		BillModel bm = bcp.getBillModel();
		// ��������
		String[] saBaseId = (String[]) PuGetUIValueTool.getArrayNotNull(bm,
				"cbaseid", String.class, iBeginRow, iEndRow);
		PuTool.loadBatchAssistManaged(saBaseId);

		// ����������
		Vector vecAssistUnitIndex = new Vector();
		Vector vecBaseId = new Vector();
		Vector vecAssistId = new Vector();

		// ����ֵ

		// ����Ĭ�ϸ�����
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
				// Ϊ����������׼��
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

		// �������ø���������
		int iAssistUnitLen = vecAssistUnitIndex.size();
		if (iAssistUnitLen > 0) {

			// ��������
			PuTool.loadBatchInvConvRateInfo((String[]) vecBaseId
					.toArray(new String[iAssistUnitLen]),
					(String[]) vecAssistId.toArray(new String[iAssistUnitLen]));

			// String[] saCurrId =
			// getAllCurrIdFromCard(getRowCount(),getCorp(),getHeadItem("ccurrencytypeid").getValue(),bm);
			// HashMap mapRateMny =
			// m_cardPoPubSetUI2.getMoneyDigitByCurr_Busi(saCurrId);
			// BusinessCurrencyRateUtil bca =
			// m_cardPoPubSetUI2.getCurrArith_Busi(getCorp());

			// ѭ��ִ��
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

				// �����ʸı䣬���¼���
				BillEditEvent tempE = new BillEditEvent(bcp
						.getBodyItem("nexchangerate"), bcp.getBodyValueAt(iRow,
						"nexchangerate"), "nexchangerate", iRow);
				afterEditWhenBodyRate(uiPanel, bcp, tempE);
			}
		}

		// ���ÿɱ༭��
		// setEnabled_BodyAssistUnitRelated(iBeginRow,iEndRow) ;

	}

	/**
	 * ��������PO29�����ý��鵥��
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

		// ��ȡ���ݹ������ļ۸�
		UFDouble[] uaPrice = null;
		int iLen = iEndRow - iBeginRow;
		try {
			uaPrice = PraybillHelper.getRulePrice(saPurCorpId, saBaseId,
					pk_reqstoorg, strPk_corp);
		} catch (Exception e) {
			MessageDialog.showErrorDlg(uiPanel, m_lanResTool.getStrByID(
					"40040101", "UPP40040101-000442")/* "Ĭ�ϲɹ���֯�͹�Ӧ��" */, e
					.getMessage());
			return;
		}
		if (uaPrice == null || uaPrice.length != iLen) {
			return;
		}
		BillEditEvent e = null;
		for (int i = iBeginRow; i < iEndRow; i++) {
			// ԭ����ֵ������ԭ����ֵ
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
	 * �༭ǰ����:������ �������ڣ�(2001-3-23 2:02:27)
	 * 
	 * @param e
	 *            ufbill.BillEditEvent
	 */
	public boolean beforeEdit(BillEditEvent e) {

		showHintMessage(m_lanResTool
				.getStrByID("common", "4004COMMON000000075")/* @res "���ڱ༭" */);

		// ����û�ģ�嶨�����Ŀ���ɱ༭����ֱ�ӷ���false
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

		// �û�ģ�嶨����޸�����µĴ���
		if (e.getKey().equals("vfree")) {
			return PuTool.beforeEditInvBillBodyFree(getBillCardPanel(), e,
					new String[] { "cmangid", "cinventorycode",
							"cinventoryname", "cinventoryspec",
							"cinventorytype" }, new String[] { "vfree",
							"vfree1", "vfree2", "vfree3", "vfree4", "vfree5" });
		}
		// �ɹ���֯
		else if (e.getKey().equals("cpurorganizationname")) {
			return beforeEditWhenBodyPurOrg(this, getBillCardPanel(),
					m_sLoginCorpId, e);
		}
		// �������
		else if (e.getKey().equals("cinventorycode")) {
			beforeEditWhenBodyInventory(getBillCardPanel(), m_hashBizType,
					m_hashInvbasIds, m_sLoginCorpId, PoPublicUIClass
							.getLoginDate()
							+ "", e);
		}
		// �����Ʒ
		else if (e.getKey().equals("cproductname")) {
			beforeEditWhenBodyProduct(getBillCardPanel(), m_sLoginCorpId,
					m_sLoginDate, e);
		}
		// ����ֿ�
		else if (e.getKey().equals("cwarehousename")) {
			beforeEditWhenBodyReqWare(getBillCardPanel(), e);
		}
		// ��������֯
		else if (e.getKey().equals("reqstoorgname")) {
			beforeEditWhenBodyReqStore(getBillCardPanel(), e);
		}
		// ���鹩Ӧ��
		else if (e.getKey().equals("cvendorname")) {
			return beforeEditWhenBodyVendor(getBillCardPanel(), e);
		}
		// �ɹ�Ա
		else if (e.getKey().equals("cemployeename")) {
			beforeEditWhenBodyEmployer(getBillCardPanel(), e);
		}
		// ��Ŀ
		else if (e.getKey().equals("cprojectname")) {
			beforeEditWhenBodyProject(getBillCardPanel(), e);
		}
		// ��Ŀ�׶�
		else if (e.getKey().equals("cprojectphasename")) {
			beforeEditWhenBodyProjectPhase(getBillCardPanel(), e);
		}
		// ���κ�
		else if (e.getKey().equals("vproducenum")) {
			return beforeEditWhenBodyProduceNum(this, getBillCardPanel(),
					m_sLoginCorpId, e);
		}
		// ����˾
		else if (e.getKey().equals("reqcorpname")) {
			beforeEditWhenBodyReqcorp(getBillCardPanel(), e);
		}
		// �ɹ���˾
		else if (e.getKey().equals("purcorpname")) {
			beforeEditWhenBodyPurcorp(getBillCardPanel(), e);
		}
		// �������������ʡ���������������
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
	 * �ɹ���֯���༭�ɱ༭�Լ�������������
	 * <p>
	 * 1)��ҵ�������ǹ�˾ҵ�����ͣ��ɹ���֯��������ȡ�ɹ���֯������˾Ϊ�빺��������˾�Ĳɹ���֯�������ɱ༭
	 * <p>
	 * 2)��ҵ�������Ǽ���ҵ�����ͣ�
	 * <p>
	 * i.����˾δ¼�룬��������ȡȫ�������вɹ���֯�������ɱ༭
	 * <p>
	 * ii.����˾=�빺��������˾�����δ¼�룬���ɱ༭
	 * <p>
	 * iii.����˾=�빺��������˾���Բɹ������������ȡȫ�������вɹ���֯�������ɱ༭�����ɲ��ɱ༭(XY\WYF\CZP:
	 * �ݲ����ֹ�˾���ƻ���������������Ż����������)
	 * <p>
	 * iv.����˾���빺��������˾�����۴���Ƿ�¼�룬�ɹ���֯��������Ϊ���빺��������˾������˾�����ɱ༭
	 * 
	 * 
	 * @��ϸ���������� <p>
	 *          ���������Բɣ���ȡ����˾����ǰ����ɹ��Ĳɹ�Ա�����ݲɹ�Ա�����ϵ������Ӧ�Ĳɹ���֯�����ݲɹ���֯����Ĳɹ�Ա����
	 *          ���Բ���ѡ��ɹ���֯�����޸ģ�
	 *          <p>
	 *          
	 *          �������Ǽ��ɣ�ȡ���ɹ����ж���Ĳɹ���֯����ǰ��˾����ǲɹ���֯��������˾������Բ���ѡ��������˾��Ϊ��ǰ��˾������˾�Ĳɹ���֯�޸�
	 *          ���������޸ġ�
	 */
	public static boolean beforeEditWhenBodyPurOrg(ToftPanel uiPanel,
			BillCardPanel bcp, String strPk_corp, BillEditEvent e) {
		int currow = e.getRow();
		String pk_reqcorp = null;
		UIRefPane refpane = null;

		m_strPurCorpIdOld = PuPubVO.getString_TrimZeroLenAsNull(bcp
				.getBodyValueAt(e.getRow(), "pk_purcorp"));

		/* ��˾ҵ������ */
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

		/* ����ҵ������ */

		// ����˾Ϊ��
		pk_reqcorp = PuPubVO.getString_TrimZeroLenAsNull(bcp.getBillModel()
				.getValueAt(currow, "pk_reqcorp"));
		if (pk_reqcorp == null) {
			Logger.debug("pk_reqcorp = null, �ɹ���˾����ȡֵ");/* -=notranslate=- */
			refpane = (UIRefPane) bcp.getBillData().getBodyItem(
					"cpurorganizationname").getComponent();
			AbstractRefModel refModel = refpane.getRefModel();
			refModel.addWherePart(" and 1>0 ");
			return true;
		}
		// ����˾���빺��������˾
		if (!pk_reqcorp.equals(strPk_corp)) {
			// �빺��������˾������˾
			// ���۴���Ƿ�¼�룬�ɹ���֯��������Ϊ���빺��������˾������˾���ɱ༭
			refpane = (UIRefPane) bcp.getBillData().getBodyItem(
					"cpurorganizationname").getComponent();
			AbstractRefModel refModel = refpane.getRefModel();
			refModel.addWherePart(" and bd_purorg.ownercorp in ('" + strPk_corp
					+ "','" + pk_reqcorp + "') ");

			return true;
		}
		// ����˾=�빺��������˾
		if (pk_reqcorp.equals(strPk_corp)) {
			// ���δ¼��
			if (PuPubVO.getString_TrimZeroLenAsNull(bcp.getBillModel()
					.getValueAt(currow, "cmangid")) == null) {
				// ���δ¼�롢���ɱ༭
				SCMEnv.out("���δ¼�롢���ɱ༭");/* -=notranslate=- */
				return false;
			}
			// ���¼��
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
					// ���ɣ��ɹ���֯Ĭ��ȡ���ɹ����ж���Ĳɹ���֯���빺��������˾��ɹ���˾��ͬ����ɱ༭�����򲻿ɱ༭
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
				// ������Բ�
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
	 * ����ֿ⣺�ɿգ��ɱ༭��Ĭ�ϲ���ʾ a) �༭ǰ��飺�������˾Ϊ�ջ��ϲ���Դ����ID�ǿգ��򲻿ɱ༭ b)
	 * �༭ǰ�������ã�������������֯�µĲֿ⵵��
	 */
	public static void beforeEditWhenBodyReqWare(BillCardPanel bcp,
			BillEditEvent e) {
		String strReqCorp = PuPubVO.getString_TrimZeroLenAsNull(bcp
				.getBodyValueAt(e.getRow(), "pk_reqcorp"));
		if (strReqCorp == null) {
			Logger.debug("����˾δ¼��,���塰����ֿ⡱��Ŀ���ɱ༭");
			bcp.getBillData().getBillModel().setCellEditable(e.getRow(),
					"reqstoorgname", false);
			return;
		}
		String strUpSrcBillId = PuPubVO.getString_TrimZeroLenAsNull(bcp
				.getBodyValueAt(e.getRow(), "cupsourcebillid"));
		if (strUpSrcBillId != null) {
			Logger.debug("����Դ�ĵ�����,���塰����ֿ⡱��Ŀ���ɱ༭");
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
	 * ����˾�����ɱ༭
	 */
	public static void beforeEditWhenBodyReqcorp(BillCardPanel bcp,
			BillEditEvent e) {
		bcp.getBillData().getBillModel().setCellEditable(e.getRow(),
				"reqcorpname", false);
	}

	/*
	 * �ɹ���˾�����ɱ༭
	 */
	public static void beforeEditWhenBodyPurcorp(BillCardPanel bcp,
			BillEditEvent e) {
		bcp.getBillData().getBillModel().setCellEditable(e.getRow(),
				"purcorpname", false);
	}

	/**
	 * ��������֯����������˾�µĿ����֯�������ɿ�,�ɱ༭��Ĭ�ϲ���ʾ a) �༭ǰ��飺����ϲ���Դ����ID�ǿգ����ɱ༭ b)
	 * �༭ǰ�������ã���������˾�����֯
	 */
	public static void beforeEditWhenBodyReqStore(BillCardPanel bcp,
			BillEditEvent e) {
		// �༭ǰ��飺����ϲ���Դ����ID�ǿգ����ɱ༭
		String strUpSrcBillId = PuPubVO.getString_TrimZeroLenAsNull(bcp
				.getBodyValueAt(e.getRow(), "cupsourcebillid"));
		if (strUpSrcBillId != null) {
			Logger.debug("����Դ�ĵ�����,���塰��������֯����Ŀ���ɱ༭");
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

		// �༭ǰ�������ã���������˾�����֯
		UIRefPane paneReqStoOrg = ((UIRefPane) bcp.getBodyItem("reqstoorgname")
				.getComponent());
		paneReqStoOrg.getRefModel().setSealedDataShow(false);
		Object oTemp = bcp.getBodyValueAt(e.getRow(), "pk_reqcorp");
		if (oTemp != null)
			paneReqStoOrg.getRefModel().addWherePart(
					" and bd_calbody.pk_corp = '" + oTemp.toString() + "' ");
	}

	/**
	 * ���鹩Ӧ�� i. ����ɹ���֯δ¼�룬���ɱ༭ ii. ��������ȡ�ɹ���֯�ġ�������˾���µĿ��̵���
	 */
	public static boolean beforeEditWhenBodyVendor(BillCardPanel bcp,
			BillEditEvent e) {
		String strUpSrcBillId = PuPubVO.getString_TrimZeroLenAsNull(bcp
				.getBodyValueAt(e.getRow(), "pk_purcorp"));
		if (strUpSrcBillId == null) {
			Logger.debug("�ɹ���˾δ¼�룬���ɱ༭");
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
	 * ��Ŀ�׶� �༭ǰ���ò��գ����յ�ǰ��˾��Ŀ�׶ε���
	 */
	public static void beforeEditWhenBodyProjectPhase(BillCardPanel bcp,
			BillEditEvent e) {
		// �༭ǰ��������
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
	 * ��Ŀ �༭ǰ���ò��գ����յ�ǰ��˾��Ŀ����
	 */
	public static void beforeEditWhenBodyProject(BillCardPanel bcp,
			BillEditEvent e) {
		// �༭ǰ�������ã���������˾�����֯
		UIRefPane paneReqStoOrg = ((UIRefPane) bcp.getBodyItem("cprojectname")
				.getComponent());
		// JobmngfilDefaultRefModel rmArrStoOrg = new
		// JobmngfilDefaultRefModel("��Ŀ������");
		AbstractRefModel rmArrStoOrg = paneReqStoOrg.getRefModel();
		rmArrStoOrg.setSealedDataShow(false);
		// paneReqStoOrg.setRefModel(rmArrStoOrg);

	}

	/**
	 * <p>
	 * ���κţ��༭ǰ��飺�������˾Ϊ�ջ��ϲ���Դ����ID�ǿգ��򲻿ɱ༭(+V31SP1�߼�)
	 * 
	 * <p>
	 * 
	 * @�޸ģ� V5
	 */
	public static boolean beforeEditWhenBodyProduceNum(ToftPanel ui,
			BillCardPanel bcp, String sLoginCorpId, BillEditEvent e) {

		int iRow = e.getRow();

		// ��������ι���ֱ�ӷ���
		if (!PuTool
				.isBatchManaged((String) bcp.getBodyValueAt(iRow, "cmangid"))) {
			ui.showHintMessage(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000531")/* @res "����������ι������ɱ༭" */);
			return false;
		}
		//
		ParaVOForBatch vo = new ParaVOForBatch();

		// ����FieldName
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

		// ���ÿ�Ƭģ��,��˾��
		vo.setCardPanel(bcp);
		vo.setPk_corp(sLoginCorpId);
		vo.setEvent(e);

		try {
			PuTool.beforeEditWhenBodyBatch(vo);
		} catch (Exception ex) {
			Logger.debug("�������κų���" + ex.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * a) �ɹ�Ա �༭ǰ: ��������ȡ���ɹ���˾���µ���Ա����
	 */
	public static void beforeEditWhenBodyEmployer(BillCardPanel bcp,
			BillEditEvent e) {

		UIRefPane paneEmployee = ((UIRefPane) bcp.getBodyItem("cemployeename")
				.getComponent());
		AbstractRefModel rmArrStoOrg = paneEmployee.getRefModel();
		rmArrStoOrg.setSealedDataShow(false);// ����ʾ�������
		Object oTemp = bcp.getBodyValueAt(e.getRow(), "pk_purcorp");
		if (oTemp != null) {
			rmArrStoOrg.setPk_corp(oTemp.toString());
			rmArrStoOrg.reloadData();
		}
		paneEmployee.setRefModel(rmArrStoOrg);
	}

	/**
	 * ���ߣ����� ���ܣ��޸Ĵ��ǰ���� ������BillEditEvent e ��׽����BillEditEvent�¼� ���أ��� ���⣺��
	 * ���ڣ�(2005-6-23 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @�޸ģ�czp,V5,�ع�Ϊ֧���޶�����
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
		// ���˴������,��֤��ҵ�����������д���ҵ������ʱ�滻�����������д�������
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
				// ��û�������ID
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
		// ��Ʒ��Ϊ�գ�����ù����������ѯ�Ӽ���
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
		InvmandocDefaultRefModel refModel = new InvmandocDefaultRefModel("�������");
		refModel.setWherePart(wherePart);
		refModel.setPk_corp(sLoginCorpId);
		refpane.setTreeGridNodeMultiSelected(true);
		refpane.setMultiSelectedEnabled(true);
		refpane.setCacheEnabled(false);
		refpane.setRefModel(refModel);
	}

	/**
	 * ���ߣ����� ���ܣ��޸Ĳ�Ʒǰ���� ������BillEditEvent e ��׽����BillEditEvent�¼� ���أ��� ���⣺��
	 * ���ڣ�(2005-6-23 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
		// ���ù����������ѯ�������-��Ʒ

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
		refModel.setRefNodeName("�������");
		refModel.addWherePart(wherePart);
		refpane.setRefModel(refModel);
		// refModel.setPk_corp(getCorpId());

		// ��治��ʾ
		// refModel.addWherePart(wherePart);
		refpane.setRefModel(refModel);
	}

	/**
	 * ��������:�б任�¼���Ӧ
	 * 
	 * @param event
	 *            nc.ui.pub.bill.BillEditEvent
	 */
	public void bodyRowChange(BillEditEvent event) {
		if ((UITable) event.getSource() == getBillCardPanel().getBillTable()) {
			// ������Ŀ�׶οɱ༭��
			setProjPhaseEditable(event);
		} else if ((UITable) event.getSource() == getBillListPanel()
				.getBodyTable()) {
			// ���ø��������ռ�����Ϣ�ɱ༭��
			// setAssisUnitEditState2(event);
			// ��ͬ����������������Ӧ����Ŀ�׶β���
			Object oTemp = getBillCardPanel().getBodyValueAt(event.getRow(),
					"cprojectid");
			if (oTemp != null) {
				UIRefPane nRefPanel = (UIRefPane) getBillCardPanel()
						.getBodyItem("cprojectphasename").getComponent();
				nRefPanel.setIsCustomDefined(true);
				nRefPanel.setRefModel(new ProjectPhase((String) oTemp));
			}
			// ��ͬ����������������Ӧ���������Զ������
			InvVO invVO = getVOForFreeItem(event);
			invVO.setIsFreeItemMgt(new Integer(1));
			m_freeItem.setFreeItemParam(invVO);
		}
	}

	/**
	 * ��������:�ı���水ť״̬
	 */
	private void updateButtonsAll() {
		int iLen = getBtnTree().getButtonArray().length;
		for (int i = 0; i < iLen; i++) {
			update(getBtnTree().getButtonArray()[i]);
		}
	}

	/**
	 * �������ڣ� 2005-9-20 ���������� ���°�ť״̬���ݹ鷽ʽ��
	 */
	private void update(ButtonObject bo) {
		updateButton(bo);
		if (bo.getChildCount() > 0) {
			for (int i = 0, len = bo.getChildCount(); i < len; i++)
				update(bo.getChildButtonGroup()[i]);
		}
	}

	/**
	 * ��������:���Ϊ����������ʱ,��鸨�����Ƿ����
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
													 * @res "��"
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
	 * �˴����뷽��˵���� �������ڣ�(2003-11-20 17:09:22)
	 */
	private boolean checkBeforeSave(PraybillVO VO) {

		/* ��鵥���к� */
		if (!BillRowNo.verifyRowNosCorrect(getBillCardPanel(), "crowno")) {
			showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
					"UPPSCMCommon-000010")/*
										 * @res "����ʧ��"
										 */);
			return false;
		}
		/* ��鵥��ģ��ǿ��� */
		try {
			nc.ui.pu.pub.PuTool.validateNotNullField(getBillCardPanel());
		} catch (Exception e) {
			MessageDialog.showWarningDlg(this, m_lanResTool.getStrByID(
					"40040101", "UPP40040101-000035")/* "����ģ��ǿ�����" */, e
					.getMessage());
			showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
					"UPPSCMCommon-000010")/* @res"����ʧ��" */);
			return false;
		}

		// �Ϸ��Լ��
		try {
			if (getBillCardPanel().getRowCount() < 1) {
				MessageDialog
						.showErrorDlg(this, m_lanResTool.getStrByID("common",
								"UC001-0000001")/* "����" */, m_lanResTool
								.getStrByID("40040101", "UPP40040101-000036")/* "�빺������Ϊ�գ����ܱ��棡" */);
				showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
						"UPPSCMCommon-000010")/* "����ʧ��" */);
				return false;
			}
			VO.validate();
		} catch (ValidationException e) {
			MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
					"40040101", "UPP40040101-000037")/* "�Ϸ��Լ��" */, e
					.getMessage());
			showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
					"UPPSCMCommon-000010")/* "����ʧ��" */);
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
									"common", "UC001-0000001")/* "����" */,
									m_lanResTool.getStrByID("common",
											"4004COMMON000000015")/* @res*�ɹ���˾" */
											+ nc.vo.ml.NCLangRes4VoTransl
													.getNCLangRes().getStrByID(
															"smcomm",
															"UPP1005-000239")/*
																			 * @res
																			 * "����Ϊ�ա�"
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
								.getStrByID("common", "UC001-0000001")/* "����" */,
								m_lanResTool.getStrByID("40040101",
										"UPP40040101-000524")/*
															 * �������ڲ���С���빺����! "
															 */);
						showHintMessage(m_lanResTool.getStrByID("40040101",
								"UPP40040101-000524")/* �빺���ڱ���С�ڽ��鶩�����ں���������!" */);
						return false;
					}
					if (dsuggestdate != null
							&& ddemanddate != null
							&& !(ddemanddate.after(dsuggestdate) || dsuggestdate
									.equals(ddemanddate))) {
						MessageDialog.showErrorDlg(this, m_lanResTool
								.getStrByID("common", "UC001-0000001")/* "����" */,
								m_lanResTool.getStrByID("40040101",
										"UPP40040101-000525")/*
															 * ����ɹ����ڲ���С���빺����! "
															 */);
						showHintMessage(m_lanResTool.getStrByID("40040101",
								"UPP40040101-000525")/* ����ɹ����ڱ���С����������!" */);
						return false;
					}
				} else if (dpraydate != null && ddemanddate != null
						&& dsuggestdate == null) {
					if (!(dpraydate.before(ddemanddate) || dpraydate
							.equals(ddemanddate))) {
						MessageDialog.showErrorDlg(this, m_lanResTool
								.getStrByID("common", "UC001-0000001")/* "����" */,
								m_lanResTool.getStrByID("40040101",
										"UPP40040101-000526")/*
															 * �빺���ڱ���С����������! "
															 */);
						showHintMessage(m_lanResTool.getStrByID("40040101",
								"UPP40040101-000526")/* �빺���ڱ���С����������!" */);
						return false;
					}
				}
			}
		}

		String sMessage = checkAssistUnit();
		if (sMessage.length() > 0) {
			MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
					"40040101", "UPP40040101-000037")/* "�Ϸ��Լ��" */, sMessage);
			showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
					"UPPSCMCommon-000010")/* "����ʧ��" */);
			return false;
		}
		return true;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-11-20 18:05:17)
	 */
	private void dispAfterSave(PraybillVO VO, ArrayList keys) {
		try {
			// ����������
			new nc.ui.scm.pub.FreeVOParse().setFreeVO(VO.getBodyVO(), "vfree",
					"vfree", "cbaseid", "cmangid", true);

		} catch (Exception e) {
			reportException(e);
		}
		Vector vTemp = new Vector();
		if (m_VOs != null) {
			// �Ѿ������빺��(����)
			if (m_bAdd) {
				for (int i = 0; i < m_VOs.length; i++) {
					vTemp.addElement(m_VOs[i]);
				}
				vTemp.addElement(VO);
				m_VOs = new PraybillVO[vTemp.size()];
				vTemp.copyInto(m_VOs);
				m_nPresentRecord = m_VOs.length - 1;
				// �Ѿ������빺��(�޸�)
			} else {
				m_VOs[m_nPresentRecord] = VO;
			}
		} else {
			/* �������빺�� */
			m_VOs = new PraybillVO[1];
			m_VOs[0] = VO;
			m_nPresentRecord = 0;
		}
		/* ���ñ��棬�в���������Ϊ�ң��������� */
		getBillCardPanel().setEnabled(false);
		/* ��������װ�����ݣ������½��� */
		for (int i = 0; i < 1; i++) {
			try {
				getBillCardPanel().setBillValueVO(m_VOs[m_nPresentRecord]);
			} catch (Exception e) {
				continue;
			}
		}

		nc.ui.pu.pub.PuTool.loadInvMaxPrice(getBillCardPanel());
		/* ��ʾ״̬ͼƬ */
		// V5 Del : setImageTypeMy(m_nPresentRecord);
		getBillCardPanel().getBillModel().execLoadFormula();
		getBillCardPanel().updateValue();
		getBillCardPanel().setEnabled(false);
		m_bEdit = false;
		/* ��ʾ��ע */
		UIRefPane nRefPanel3 = (UIRefPane) getBillCardPanel().getHeadItem(
				"vmemo").getComponent();
		nRefPanel3.setValue(m_VOs[m_nPresentRecord].getHeadVO().getVmemo());
		// ��ʾ������Դ��Ϣ
		nc.ui.pu.pub.PuTool.loadSourceInfoAll(getBillCardPanel(),
				nc.vo.scm.pu.BillTypeConst.PO_PRAY);
		// ����������־
		m_bAdd = false;
		setButtonsCard();
		m_nUIState = 0;
	}

	/**
	 * ���ߣ���ά�� ���ܣ�����ͷ��ʽ���ݣ����ⲻ�ɲ��ճ������� ������ ���أ� ���⣺ ���ڣ�(2004-5-20 14:18:41)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @param vo
	 *            nc.vo.pr.pray.PraybillVO
	 */
	public void execHeadTailFormula(PraybillVO vo) {
		if (vo == null) {
			return;
		}
		// ������ʽ
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
	 * �˴����뷽��˵���� ��������:���ǵ����� ���ߣ���ά�� �������: ����ֵ: �쳣����: ����:
	 */
	private void filterNullLine() {
		// �����ֵ�ݴ�
		Object oTempValue = null;
		// ����model
		nc.ui.pub.bill.BillModel bmBill = getBillCardPanel().getBillModel();
		// ����кţ�Ч�ʸ�һЩ��
		final int iInvCol = bmBill.getBodyColByKey(m_sInvMngIDItemKey);

		// �����д����
		if (bmBill != null && iInvCol >= 0 && iInvCol < bmBill.getColumnCount()) {
			// ����
			final int iRowCount = getBillCardPanel().getRowCount();
			// �Ӻ���ǰɾ
			for (int line = iRowCount - 1; line >= 0; line--) {
				// ���δ��
				oTempValue = bmBill.getValueAt(line, iInvCol);
				if (oTempValue == null
						|| oTempValue.toString().trim().length() == 0)
					// ɾ��
					getBillCardPanel().getBillModel().delLine(
							new int[] { line });
			}
		}
		if (bmBill.getRowCount() <= 0)
			onAppendLine(getBillCardPanel(), this);
	}

	/**
	 * ��������:��ô���Ĳɹ���ǰ��
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
					"UPP40040101-000047")/* "�ɹ���ǰ��" */, m_lanResTool.getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000412")/* "SQL������" */);
			return -1;
		} catch (ArrayIndexOutOfBoundsException e) {
			MessageDialog.showErrorDlg(ui, m_lanResTool.getStrByID("40040101",
					"UPP40040101-000047")/* "�ɹ���ǰ��" */, m_lanResTool.getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000426")/* "����Խ�����" */);
			return -1;
		} catch (NullPointerException e) {
			MessageDialog.showErrorDlg(ui, m_lanResTool.getStrByID("40040101",
					"UPP40040101-000047")/* "�ɹ���ǰ��" */, m_lanResTool.getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000427")/* "��ָ�����" */);
			return -1;
		} catch (Exception e) {
			MessageDialog.showErrorDlg(ui, m_lanResTool.getStrByID("40040101",
					"UPP40040101-000047")/* "�ɹ���ǰ��" */, e.getMessage());
			return -1;
		}
		if (v.size() == 0)
			return 0;
		UFDouble dFixedahead = (UFDouble) v.elementAt(0);
		UFDouble dAheadcoff = (UFDouble) v.elementAt(1);
		UFDouble dAheadbatch = (UFDouble) v.elementAt(2);
		UFDouble d = bodyVO[n].getNpraynum();

		// V5,Czp,�ع�����֤��ǰ���㷨ֻ��һ��ʵ��
		return AdDayVO.getAdDaysArith(d, dFixedahead, dAheadcoff, dAheadbatch);
	}

	/**
	 * ���ܣ���ȡ������ѯ�Ի���
	 * 
	 */
	private ATPForOneInvMulCorpUI getAtpDlg() {
		if (null == m_atpDlg) {
			m_atpDlg = new ATPForOneInvMulCorpUI(this);
		}
		return m_atpDlg;
	}

	/**
	 * ��������:��ÿ�Ƭ���ݿ���
	 */
	private BillCardPanel getBillCardPanel() {
		if (m_billPanel == null) {
			try {
				m_billPanel = new BillCardPanel();

				m_billPanel.loadTemplet("20", null, getClientEnvironment()
						.getUser().getPrimaryKey(), m_sLoginCorpId,
						new MyBillData());
				// ����ǧ��λ
				m_billPanel.setBodyShowThMark(true);
				// �ϼ�����ʾ
				m_billPanel.setTatolRowShow(true);
				// �����к�
				BillRowNo.loadRowNoItem(m_billPanel, "crowno");

				// ���ʻ�
				nc.ui.pu.pub.PuTool.setTranslateRender(m_billPanel);

				// �����Զ�����
				nc.ui.scm.pub.def.DefSetTool.updateBillCardPanelUserDef(
						m_billPanel, getClientEnvironment().getCorporation()
								.getPk_corp(), ScmConst.PO_Pray, // ��������
						"vdef", "vdef");

				// �汾��:С��λ��ʾһλ
				m_billPanel.getHeadItem("nversion").setDecimalDigits(1);

			} catch (Exception e) {
				SCMEnv.out(e);
				MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
						"40040101", "UPP40040101-000048")/* "����ģ��" */,
						m_lanResTool.getStrByID("40040101",
								"UPP40040101-000049")/* "ģ�岻���ڣ�" */);
				return null;
			}
		}
		return m_billPanel;
	}

	/**
	 * ��������:����б��ݿ���
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
				// ���ع�˾
				if (m_listPanel.getHeadItem("pk_corpname") != null)
					m_listPanel.hideHeadTableCol("pk_corpname");

				// ����ǧ��λ
				m_listPanel.getParentListPanel().setShowThMark(true);
				m_listPanel.getChildListPanel().setShowThMark(true);

				BillListData bd = m_listPanel.getBillListData();
				// ��ʼ������
				bd = initListDecimal(bd);

				// �����б�����
				m_listPanel.setListData(bd);

				m_listPanel.getHeadTable().setSelectionMode(
						ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

				m_listPanel.addEditListener(this);
				m_listPanel.addMouseListener(this);

				// �����Զ�����
				nc.ui.scm.pub.def.DefSetTool.updateBillListPanelUserDef(
						m_listPanel, getClientEnvironment().getCorporation()
								.getPk_corp(), ScmConst.PO_Pray, // ��������
						"vdef", "vdef");

				m_listPanel.updateUI();

				m_listPanel.getHeadTable().setCellSelectionEnabled(false);
				m_listPanel
						.getHeadTable()
						.setSelectionMode(
								javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				m_listPanel.getHeadTable().getSelectionModel()
						.addListSelectionListener(this);
				// ���Ӻϼ���
				m_listPanel.getChildListPanel().setTotalRowShow(true);

				// �����к��������
				m_listPanel.getBodyBillModel().setSortPrepareListener(this);

				// �������
				m_listPanel.getHeadBillModel().addSortRelaObjectListener2(this);
			} catch (Exception e) {
				SCMEnv.out(e);
				MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
						"40040101", "UPP40040101-000048")/* "����ģ��" */,
						m_lanResTool.getStrByID("40040101",
								"UPP40040101-000049")/* "ģ�岻���ڣ�" */);
				return null;
			}
		}

		return m_listPanel;
	}

	/**
	 * ����ʵ�ָ÷���������ҵ�����ı���
	 */
	private String getCauditid() {

		return m_cauditid;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-10-28 14:53:42)
	 */
	private int getCurVoPos() {
		return m_nPresentRecord;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-10-28 15:02:52)
	 */
	private PraybillVO[] getPraybillVOs() {
		return m_VOs;
	}

	/**
	 * ���ܣ����ز�ѯ�Ի��� ������ ���أ� ���⣺ ���ڣ�(2002-9-13 9:47:11) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @return nc.ui.pub.query.QueryConditionClient
	 */
	private PrayUIQueryDlg getQueryDlg() {

		if (m_condClient == null) {
			m_condClient = new PrayUIQueryDlg(this, m_sLoginCorpId);
			DefSetTool.updateQueryConditionClientUserDef(m_condClient,
					m_sLoginCorpId, ScmConst.PO_Pray, // ��������
					"po_praybill.def", "po_praybill_b.def");
		}

		return m_condClient;
	}

	/**
	 * �޸��ˣ������ �� �ܣ�ȥ�����ռ������ݣ����Ч�� �� �ڣ�2002-05-30
	 */

	private UIRefPane getRefWherePart(UIRefPane refpane, String pk_corp,
			String key) {

		String wherepart = refpane.getRefModel().getWherePart();
		if ((wherepart != null) && (!(wherepart.trim().equals("")))
				&& (wherepart.indexOf("pk_corp") >= 0)) {
			// ��λ����
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
	 * �˴����뷽��˵���� �������ڣ�(2003-11-4 19:59:42)
	 */
	private ArrayList getSelectedBills() {
		Vector vAll = new Vector();
		PraybillVO[] allvos = null;
		// ȫ��ѡ��ѯ�۵�
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

		// ��ѯδ��������ĵ�����
		try {
			allvos = PrTool.getRefreshedVOs(allvos);
		} catch (BusinessException b) {
			showHintMessage(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000444")/* "���ִ���:" */
					+ b.getMessage());
		} catch (Exception e) {
			SCMEnv.out(e);
			showHintMessage(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000445")/* "����δ֪����" */);
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
	 * ���ߣ���ά�� ���ܣ��ӿ�IBillModelSortPrepareListener ��ʵ�ַ��� ������String sItemKey
	 * ITEMKEY ���أ��� ���⣺�� ���ڣ�(2004-03-24 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
	 * ����ʵ�ָ÷���������ҵ�����ı��⡣
	 * 
	 * @version (00-6-6 13:33:25)
	 * 
	 * @return java.lang.String
	 */
	public String getTitle() {
		// "ά���빺��"*/;
		String title = m_lanResTool
				.getStrByID("40040101", "UPP40040101-000453")/* "ά���빺��" */;
		if (m_billPanel != null
				&& !getClientEnvironment().getCorporation().getPk_corp()
						.equals("@@@@")) {
			title = m_billPanel.getTitle();
		}
		return title;
	}

	/**
	 * ���ܣ��÷�����ʵ�ֽӿ�ICheckRetVO�ķ��� �ýӿ�Ϊ��������ƣ���ʵ���������˵������ʱ���������ŷ�Ʊ
	 * �벻Ҫ����ɾ�����޸ĸ÷������Ա������ �������� ���أ�nc.vo.pub.AggregatedValueObject Ϊ�빺��VO ���⣺��
	 * ���ڣ�(2001-5-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	public nc.vo.pub.AggregatedValueObject getVo() throws Exception {
		PraybillVO vo = null;
		try {
			vo = PraybillHelper.queryPrayVoByHid(getCauditid());
		} catch (java.lang.Exception e) {
			SCMEnv.out(e);
			throw e;
		}
		// û�з��������ĵ���
		if (vo == null) {
			return null;
		}
		return vo;
	}

	/**
	 * ���ߣ��ܺ��� ���ܣ���ô�����룬������ƣ������񣬴���ͺţ�10�����������ƣ�10��������ֵ Ϊ��������� ������BillEditEvent
	 * e ��׽����BillEditEvent�¼� ���أ������������VO ���⣺�� ���ڣ�(2002-3-13 11:39:21)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-08-08 wyf �޸Ķ��ڿմ������ID�Ĵ���
	 */
	private InvVO getVOForFreeItem(BillEditEvent event) {
		final int iRow = event.getRow();

		// ���ڷ��ص�VO
		InvVO tempVO = new InvVO();
		String sBaseId = (String) getBillCardPanel().getBodyValueAt(iRow,
				"cbaseid");
		if (sBaseId == null) {
			return tempVO;
		}

		// ��ô�����룬������ƣ������񣬴���ͺż��������ID
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
	 * ��������:��ʼ��
	 */
	private void init() {

		// ��ʼ������
		initPara();

		// ��ʾ����
		setLayout(new java.awt.BorderLayout());
		add(getBillCardPanel(), java.awt.BorderLayout.CENTER);

		// ---------����ģ����غ�ĳ�ʼ����������������

		// ��ʼ����ť
		initButton();

		// ���Ӽ���
		initListener();

		getBillCardPanel().setEnabled(false);
	}

	/**
	 * ��������:���ص���ģ��֮ǰ�ĳ�ʼ��
	 */

	private void initBillBeforeLoad(BillData bd) {

		// ---------����ģ�����ǰ�ĳ�ʼ����������������

		// ��ʼ������
		initRefpane(bd);
		// ��ʼ��ComboBox
		initComboBox(bd);
		// ��ʼ������
		initDecimal(bd);
		// ��ʼ����������
		initiEnabledFalseItems(bd);

	}

	/**
	 * ���ߣ���ӡ�� ���ܣ��Բ��ɱ༭��������� �������� ���أ��� ���⣺�� ���ڣ�(2002-8-26 11:39:21)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-10-23 WYF ����Կ��������������
	 */
	private void initiEnabledFalseItems(BillData bd) {
		// ��ͷ
		// ҵ������
		bd.getHeadItem("cbiztype").setEnabled(false);
		//
		// // ����
		// //����˾ ���ɱ༭ Ĭ�ϲ���ʾ
		if (bd.getBodyItem("pk_reqcorp") != null) {
			bd.getBodyItem("pk_reqcorp").setEnabled(false);
		}

		return;
	}

	/**
	 * V51�ع���Ҫ��ƥ��,��ťʵ����������
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * ʹ��ʾ��
	 * <p>
	 * <b>����˵��</b>
	 * <p>
	 * 
	 * @author czp
	 * @time 2007-3-13 ����01:15:06
	 */
	private void createBtnInstances() {
		// ҵ������
		m_btnBusiTypes = getBtnTree().getButton(
				IButtonConstPr.BTN_BUSINESS_TYPE);
		// ����
		m_btnAdds = getBtnTree().getButton(IButtonConstPr.BTN_ADD);
		// ����
		m_btnSave = getBtnTree().getButton(IButtonConstPr.BTN_SAVE);
		// ����
		m_btnMaintains = getBtnTree().getButton(IButtonConstPr.BTN_BILL);
		m_btnModify = getBtnTree().getButton(IButtonConstPr.BTN_BILL_EDIT);
		m_btnModifyList = m_btnModify;
		m_btnCancel = getBtnTree().getButton(IButtonConstPr.BTN_BILL_CANCEL);
		m_btnDiscard = getBtnTree().getButton(IButtonConstPr.BTN_BILL_DELETE);
		m_btnDiscardList = m_btnDiscard;
		m_btnCopy = getBtnTree().getButton(IButtonConstPr.BTN_BILL_COPY);
		// �в���
		m_btnLines = getBtnTree().getButton(IButtonConstPr.BTN_LINE);
		m_btnAddLine = getBtnTree().getButton(IButtonConstPr.BTN_LINE_ADD);
		m_btnDelLine = getBtnTree().getButton(IButtonConstPr.BTN_LINE_DELETE);
		m_btnInsLine = getBtnTree().getButton(IButtonConstPr.BTN_LINE_INSERT);
		m_btnCpyLine = getBtnTree().getButton(IButtonConstPr.BTN_LINE_COPY);
		m_btnPstLine = getBtnTree().getButton(IButtonConstPr.BTN_LINE_PASTE);
		// ����
		m_btnAudit = getBtnTree().getButton(IButtonConstPr.BTN_AUDIT);
		m_btnApprove = m_btnAudit;
		m_btnApprove.setTag("APPROVE");
		// ִ��
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
		// ��ѯ
		m_btnQuery = getBtnTree().getButton(IButtonConstPr.BTN_QUERY);
		m_btnQueryList = m_btnQuery;
		// ���
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
		// ��Ƭ��ʾ/�б���ʾ
		m_btnCard = getBtnTree().getButton(IButtonConstPr.BTN_SWITCH);
		m_btnList = m_btnCard;
		// ��ӡ
		m_btnPrints = getBtnTree().getButton(IButtonConstPr.BTN_PRINT);
		m_btnPrint = getBtnTree().getButton(IButtonConstPr.BTN_PRINT_PRINT);
		m_btnPrintList = m_btnPrint;
		m_btnPrintPreview = getBtnTree().getButton(
				IButtonConstPr.BTN_PRINT_PREVIEW);
		m_btnPrintListPreview = m_btnPrintPreview;
		m_btnCombin = getBtnTree().getButton(IButtonConstPr.BTN_PRINT_DISTINCT);
		// ������ѯ
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
		// ��������
		m_btnOthersFuncs = getBtnTree().getButton(
				IButtonConstPr.BTN_ASSIST_FUNC);
		m_btnDocument = getBtnTree().getButton(
				IButtonConstPr.BTN_ASSIST_FUNC_DOCUMENT);
		m_btnDocumentList = m_btnDocument;
		// ��Ϣ���ĸ�����ť��
		m_btnOthersAuditCenter = getBtnTree().getButton(
				IButtonConstPr.BTN_ASSIST_FUNC);
		m_btnOthersAuditCenter.removeAllChildren();
		m_btnOthersAuditCenter.addChildButton(m_btnWorkFlowBrowse);
		m_btnOthersAuditCenter.addChildButton(m_btnDocument);
		m_btnOthersAuditCenter.addChildButton(m_btnLinkBillsBrowse);
		// ��Ϣ���Ľ���˵�
		m_btnsAuditCenter = new ButtonObject[] { m_btnAudit, m_btnUnAudit,
				m_btnOthersAuditCenter };

	}

	/**
	 * ��ȡ��ť������Ψһʵ����
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * ʹ��ʾ��
	 * <p>
	 * <b>����˵��</b>
	 * 
	 * @return <p>
	 * @author czp
	 * @time 2007-3-13 ����01:16:48
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
	 * ��Ƭ��ť��ʾǰ�����⴦��
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * ʹ��ʾ��
	 * <p>
	 * <b>����˵��</b>
	 * <p>
	 * 
	 * @author czp
	 * @time 2007-3-13 ����04:17:42
	 */
	private void dealBtnsBeforeCardShowing() {
		// ���⹦��
		m_btnList.setName(m_lanResTool.getStrByID("SCMCOMMON",
				"UPPSCMCommon-000464")/*
									 * @res "�б���ʾ"
									 */);
		// �б����û�
		m_btnSelectAll.setEnabled(false);
		m_btnSelectNo.setEnabled(false);
	}

	/**
	 * ���ߣ���ά�� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2004-9-8 10:18:55) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 */
	private void initButton() {
		// V51�ع���Ҫ��ƥ��,��ťʵ��������
		createBtnInstances();

		// ����ҵ�����Ͱ�ť
		PfUtilClient.retBusinessBtn(m_btnTree
				.getButton(IButtonConstPr.BTN_BUSINESS_TYPE), m_sLoginCorpId,
				"20");

		// ҵ�����Ͱ�ť�򹳴���
		PuTool.initBusiAddBtns(m_btnBusiTypes, m_btnAdds, "20", m_sLoginCorpId);
		if (m_btnBusiTypes != null && m_btnBusiTypes.getChildCount() > 0) {
			m_bizButton = m_btnBusiTypes.getChildButtonGroup()[0];
		}

		// ������չ��ť
		addExtendBtns();

		// ���ؿ�Ƭ��ť
		setButtons(m_btnTree.getButtonArray());

		// ��ť״̬�߼�
		setButtonsCard();

		// ��չ��ť��ʼ��
		setExtendBtnsStat(0);
		//
		m_nUIState = 0;
	}

	/**
	 * ���ߣ���ά�� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2004-9-8 10:18:55) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 */
	private void initComboBox(BillData bd) {
		// �빺����
		bd.getHeadItem("ipraytype").setWithIndex(true);
		m_comPrayType = (UIComboBox) bd.getHeadItem("ipraytype").getComponent();
		m_comPrayType.setTranslate(true);
		m_comPrayType.addItem(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000454")/*
									 * @res "����ߴ���"
									 */);
		m_comPrayType.addItem(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000455")/*
									 * @res "����߲�����"
									 */);
		m_comPrayType.addItem(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000456")/*
									 * @res "�ɹ�"
									 */);
		m_comPrayType.addItem(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000457")/*
									 * @res "��Э"
									 */);

		// �빺��Դ
		bd.getHeadItem("ipraysource").setWithIndex(true);
		m_comPraySource = (UIComboBox) bd.getHeadItem("ipraysource")
				.getComponent();
		m_comPraySource.setTranslate(true);
		m_comPraySource.addItem("MRP");
		m_comPraySource.addItem("MO");
		m_comPraySource.addItem("SCF");
		m_comPraySource.addItem(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000458")/*
									 * @res "���۶���"
									 */);
		m_comPraySource.addItem(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000459")/*
									 * @res "��涩����"
									 */);
		m_comPraySource.addItem(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000460")/*
									 * @res "�ֹ�¼��"
									 */);
		m_comPraySource.addItem("DRP");
		m_comPraySource.addItem(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000461")/*
									 * @res "��������"
									 */);
		m_comPraySource.addItem(m_lanResTool.getStrByID("4004pub",
				"UPP4004pub-000204") /*
									 * @res "������������"
									 */);
	}

	/**
	 * ��������:��ʼ��С��λ
	 */
	private void initDecimal(BillData bd) {

		// �汾��:С��λ��ʾһλ
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
	 * �����Ĺ��ܡ���;�������Եĸ��ģ��Լ�����ִ��ǰ������״̬��
	 * 
	 * @param ����˵��
	 * @return ����ֵ
	 * @exception �쳣����
	 * @see ��Ҫ�μ�����������
	 * @since �������һ���汾���˷�������ӽ���������ѡ��
	 * 
	 */
	private void initi() {

		// ��ʼ������
		initPara();

		// ��ʾ����
		setLayout(new java.awt.BorderLayout());
		add(getBillCardPanel(), java.awt.BorderLayout.CENTER);
		getBillCardPanel().setEnabled(false);

		// ---------����ģ����غ�ĳ�ʼ����������������

		// ����ǧ��λ
		getBillCardPanel().setBodyShowThMark(true);

		// �ϼ�����ʾ
		getBillCardPanel().getBodyPanel().setTotalRowShow(true);

		// �����к�
		BillRowNo.loadRowNoItem(getBillCardPanel(), "crowno");

		// ���ʻ�
		nc.ui.pu.pub.PuTool.setTranslateRender(getBillCardPanel());

		// �����Զ�����
		nc.ui.scm.pub.def.DefSetTool.updateBillCardPanelUserDef(
				getBillCardPanel(), getClientEnvironment().getCorporation()
						.getPk_corp(), ScmConst.PO_Pray, // ��������
				"vdef", "vdef");

	}

	/**
	 * ���ߣ���ά�� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2004-9-8 10:18:55) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 */
	private void initListComboBox() {

		// �빺����
		getBillListPanel().getBillListData().getHeadItem("ipraytype")
				.setWithIndex(true);
		m_comPrayType1 = (UIComboBox) getBillListPanel().getBillListData()
				.getHeadItem("ipraytype").getComponent();
		m_comPrayType1.setTranslate(true);
		m_comPrayType1.addItem(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000454")/*
									 * @res "����ߴ���"
									 */);
		m_comPrayType1.addItem(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000455")/*
									 * @res "����߲�����"
									 */);
		m_comPrayType1.addItem(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000456")/*
									 * @res "�ɹ�"
									 */);
		m_comPrayType1.addItem(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000457")/*
									 * @res "��Э"
									 */);

		// �빺��Դ
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
									 * @res "���۶���"
									 */);
		m_comPraySource1.addItem(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000459")/*
									 * @res "��涩����"
									 */);
		m_comPraySource1.addItem(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000460")/*
									 * @res "�ֹ�¼��"
									 */);
		m_comPraySource1.addItem("DRP");
		m_comPraySource1.addItem(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000461")/*
									 * @res "��������"
									 */);
		m_comPraySource1.addItem(m_lanResTool.getStrByID("4004pub",
				"UPP4004pub-000204") /*
									 * @res "������������"
									 */);
	}

	/**
	 * ��������:��ʼ��С��λ
	 */
	private BillListData initListDecimal(BillListData bd) {

		// �汾��:С��λ��ʾһλ
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

		// �汾��:С��λ��ʾһλ
		bd.getHeadItem("nversion").setDecimalDigits(1);

		return bd;
	}

	/**
	 * ��������:��ʼ��
	 */
	public void initListener() {

		// �����������
		getBillCardPanel().getBodyPanel().addTableSortListener();
		getBillCardPanel().getBillModel().setRowSort(true);
		// �����к��������
		getBillCardPanel().getBillModel().setSortPrepareListener(this);
		// ���Ӳֿ����
		((UIRefPane) getBillCardPanel().getBodyItem("cwarehousename")
				.getComponent()).getUIButton().addActionListener(this);

		// ���ӵ��ݱ༭����
		getBillCardPanel().addEditListener(this);
		getBillCardPanel().addBodyMenuListener(this);

		// ���������
		getBillCardPanel().addBodyEditListener2(this);
		// ������
		((UIRefPane) getBillCardPanel().getBodyItem("vfree").getComponent())
				.getUIButton().addActionListener(this);
		// ��ͷ�༭ǰ�¼�����
		getBillCardPanel().setBillBeforeEditListenerHeadTail(this);
		// �������
		getBillCardPanel().getBillModel().addSortListener(this);
		// ��Ƭ�����������
		getBillCardPanel().getBillModel().addSortRelaObjectListener2(
				new IBillRelaSortListener2Body());
		
		//add by zip 2013/11/27 No 5
		String pk_corp = ClientEnvironment.getInstance().getCorporation().getPk_corp();
		String usercode = ClientEnvironment.getInstance().getUser().getUserCode();
		/**
		 * edit by yhj 2014-03-20
		 * ����yhj�˺Ųɹ��빺���ı��水ťȨ��
		 */
		//�Ϻ�����1016
		if ("1016".equals(pk_corp) && (!"zip".equals(usercode) && !"yhj".equals(usercode) && !"db".equals(usercode)&& !"dzy".equals(usercode))) {
			m_btnSave.setVisible(false);
		}
		//����1097
		if ("1097".equals(pk_corp) && (!"zip".equals(usercode) && !"yhj".equals(usercode) && !"db".equals(usercode)&& !"dzy".equals(usercode))) {
			m_btnSave.setVisible(false);
		}
		//�人1071
		if ("1071".equals(pk_corp) && (!"zip".equals(usercode) && !"yhj".equals(usercode) && !"db".equals(usercode)&& !"dzy".equals(usercode))) {
			m_btnSave.setVisible(false);
		}
		//��ɽ1019
		if ("1019".equals(pk_corp) && (!"zip".equals(usercode) && !"yhj".equals(usercode) && !"db".equals(usercode)&& !"dzy".equals(usercode))) {
			m_btnSave.setVisible(false);
		}
		//�ɶ�1018
		if ("1018".equals(pk_corp) && (!"zip".equals(usercode) && !"yhj".equals(usercode) && !"db".equals(usercode)&& !"dzy".equals(usercode))) {
			m_btnSave.setVisible(false);
		}
		//�ӱ� 1017
		if ("1017".equals(pk_corp) && (!"zip".equals(usercode) && !"yhj".equals(usercode) && !"db".equals(usercode)&& !"dzy".equals(usercode))) {
			m_btnSave.setVisible(false);
		}
		//����1097 �人1071 ��ɽ1019 �ɶ�1018 �ӱ� 1017
		// end
		// ˢ������
		readCard();
	}

	/**
	 * ��������:��ʼ������
	 */
	private void initPara() {
		try {
			// ��ʼ�����ȣ����������ۣ�
			// int[] iDigits = nc.ui.pu.pub.PuTool.getDigitBatch(m_sUnitCode,
			// new String[] { "BD502", "BD503", "BD501", "BD505" });
			// if (iDigits != null && iDigits.length == 4) {
			// nAssistUnitDecimal = iDigits[0];
			// nExchangeDecimal = iDigits[1];
			// nMeasDecimal = iDigits[2];
			// nPriceDecimal = iDigits[3];
			// }
			// ���ҽ���
			// nMoneyDecimal =
			// nc.ui.rc.pub.CPurchseMethods.getCurrDecimal(getCorpId())[0];

			// ����Ƿ�����
			// m_bICStartUp = nc.ui.sm.user.UserPowerUI.isEnabled(m_sUnitCode,
			// "IC");

			ServcallVO[] scDisc = new ServcallVO[2];
			// ��ʼ�����ȣ����������ۣ�
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

			// //�Զ�����Զ�̵���������
			// ServcallVO[] scdsDef =
			// nc.ui.scm.pub.def.DefSetTool.getTwoSCDs(m_sUnitCode);
			// scDisc[3] = scdsDef[0];
			// scDisc[4] = scdsDef[1];

			// ��̨һ�ε���
			Object[] oParaValue = nc.ui.scm.service.LocalCallService
					.callService(scDisc);
			if (oParaValue != null && oParaValue.length == scDisc.length) {
				// ���������ݾ���
				int[] iDigits = (int[]) oParaValue[0];
				if (iDigits != null && iDigits.length == 4) {
					nAssistUnitDecimal = iDigits[0];
					nExchangeDecimal = iDigits[1];
					nMeasDecimal = iDigits[2];
					nPriceDecimal = iDigits[3];
				}
				// ���ҽ���
				nMoneyDecimal = ((Integer) oParaValue[1]).intValue();

				// ����Ƿ�����
				// m_bICStartUp = ((Boolean) oParaValue[2]).booleanValue();

				// �Զ�����Ԥ����
				// nc.ui.scm.pub.def.DefSetTool.setTwoOBJs(new Object[] {
				// oParaValue[3], oParaValue[4] });

				// �����Ƿ������޸�ɾ�����˵ĵ��ݵĲ���
				String sRet = SysInitBO_Client.getParaString(m_sLoginCorpId,
						"PO060");

				isAllowedModifyByOther = (sRet == null || sRet.equals("N")) ? false
						: true;
			}
		} catch (Exception e) {
			reportException(e);
		}

		/** ICģ�������жϲ�������Ľӿڷ�ʽ */
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
	 * ���ߣ���ά�� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2004-9-8 10:18:55) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 */
	private void initRefpane(BillData bd) {
		UIRefPane refpane = null;

		// ��������������������ͷ������������������

		// ҵ��Ա(�ɹ����ŵ�)
		refpane = (UIRefPane) bd.getBodyItem("cemployeename").getComponent();
		refpane.setRefModel(new PurPsnRefModel(m_sLoginCorpId, bd.getHeadItem(
				"cdeptid").getValue()));

		// ��Ŀ
		refpane = (UIRefPane) bd.getBodyItem("cprojectname").getComponent();
		refpane
				.setWhereString(" bd_jobmngfil.pk_corp = '"
						+ m_sLoginCorpId
						+ "' and bd_jobmngfil.pk_jobbasfil = bd_jobbasfil.pk_jobbasfil and upper(isnull(bd_jobmngfil.sealflag,'N')) = 'N'");
		refpane.setCacheEnabled(false);

		// ��ע
		refpane = (UIRefPane) bd.getHeadItem("vmemo").getComponent();
		refpane.setReturnCode(false);
		refpane.setAutoCheck(false);

		// ���������������������壭����������������

		// �������
		refpane = (UIRefPane) bd.getBodyItem("cinventorycode").getComponent();
		String sWhere = " upper(ISNULL(discountflag,'N')) = 'N' and upper(ISNULL(bd_invmandoc.sealflag,'N')) = 'N' and bd_invmandoc.pk_corp = '"
				+ m_sLoginCorpId
				+ "' and bd_invmandoc.pk_invbasdoc = bd_invbasdoc.pk_invbasdoc";

		// �Ƿ�ɲɹ�
		sWhere += " AND UPPER(ISNULL(bd_invmandoc.iscanpurchased,'Y')) = 'Y'";
		refpane.setWhereString(sWhere);
		refpane.setTreeGridNodeMultiSelected(true);
		refpane.setMultiSelectedEnabled(true);
		refpane.setCacheEnabled(false);
		invrefpane = refpane;

		// �������
		refpane = (UIRefPane) bd.getBodyItem("cvendorname").getComponent();
		sWhere = " bd_cumandoc.pk_corp='"
				+ m_sLoginCorpId
				+ "' and (bd_cumandoc.custflag='1' or bd_cumandoc.custflag='3') and bd_cumandoc.pk_cubasdoc = bd_cubasdoc.pk_cubasdoc and upper(frozenflag) = 'N'";
		refpane.setWhereString(sWhere);
		refpane.setCacheEnabled(false);

		// ����������
		refpane = (UIRefPane) bd.getBodyItem("nassistnum").getComponent();
		UITextField nAssistNumUI = refpane.getUITextField();
		nAssistNumUI.setDelStr("-");

		// ����������
		refpane = ((UIRefPane) (bd.getBodyItem("cassistunitname")
				.getComponent()));
		refpane.setIsCustomDefined(true);
		refpane.setRefModel(new OtherRefModel("��������λ"));
		refpane.setReturnCode(false);
		refpane.setRefInputType(1);
		refpane.setCacheEnabled(false);

		// ������
		refpane = (UIRefPane) bd.getBodyItem("nexchangerate").getComponent();
		UITextField nExchangeRateUI = refpane.getUITextField();
		nExchangeRateUI.setDelStr("-");

		// ���κŲ���
		if (bd.getBodyItem("vproducenum").isShow()) {
			LotNumbRefPane lotRef = new LotNumbRefPane();
			lotRef.setMaxLength(bd.getBodyItem("vproducenum").getLength());
			lotRef.setIsCustomDefined(true);
			lotRef.setMaxLength(bd.getBodyItem("vproducenum").getLength());
			bd.getBodyItem("vproducenum").setComponent((JComponent) lotRef);
		}
		// ���������
		m_freeItem = new FreeItemRefPane();
		m_freeItem.setMaxLength(bd.getBodyItem("vfree").getLength());
		bd.getBodyItem("vfree").setComponent(m_freeItem);

		// ����۸�
		refpane = (UIRefPane) bd.getBodyItem("nsuggestprice").getComponent();
		UITextField nSuggestPriceUI = refpane.getUITextField();
		nSuggestPriceUI.setMaxLength(16);
		nSuggestPriceUI.setDelStr("-");

		// �빺����
		refpane = (UIRefPane) bd.getBodyItem("npraynum").getComponent();
		UITextField nPrayNumUI = refpane.getUITextField();
		nPrayNumUI.setMaxLength(20);// eric
		nPrayNumUI.setDelStr("-");

		// ���
		refpane = (UIRefPane) bd.getBodyItem("nmoney").getComponent();
		UITextField nMoneyUI = refpane.getUITextField();
		nMoneyUI.setMaxLength(16);
		nMoneyUI.setDelStr("-");

		// �ֿ���
		refpane = (UIRefPane) bd.getBodyItem("cwarehousename").getComponent();
		refpane.getRefModel().addWherePart(" and upper(gubflag) = 'N' ");
		refpane.setCacheEnabled(false);

		// ���屸ע��������ͬ�ڱ�ͷ
		refpane = (UIRefPane) bd.getBodyItem("vmemo").getComponent();
		// refpane.setTable(bd.getBillTable());
		refpane.getRefModel().setRefCodeField(
				refpane.getRefModel().getRefNameField());
		refpane.getRefModel().setBlurFields(
				new String[] { refpane.getRefModel().getRefNameField() });
		refpane.setAutoCheck(false);

	}

	/**
	 * �Ƿ������
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
	 * �Ƿ�ɹر�
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
	 * �Ƿ�ɴ�
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
	 * �Ƿ������
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
		/* �ж�������,�������� */
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
	 * ��������:���˫���¼���Ӧ
	 */
	public void mouse_doubleclick(BillMouseEnent event) {
		if (event.getPos() == BillItem.HEAD) {
			/* ���û�е����壬����Ϊ������ֱ�ӷ��� */
			PraybillItemVO[] items = (PraybillItemVO[]) getBillListPanel()
					.getBodyBillModel().getBodyValueVOs(
							PraybillItemVO.class.getName());
			if (items == null || items.length <= 0)
				return;
			/* ֧������ */
			m_nPresentRecord = event.getRow();
			m_nPresentRecord = nc.ui.pu.pub.PuTool.getIndexBeforeSort(
					getBillListPanel(), m_nPresentRecord);
			if (m_nPresentRecord >= 0) {
				/* ���ص��ݿ�Ƭ�ؼ� */
				getBillListPanel().setVisible(false);
				getBillCardPanel().setVisible(true);
				m_nUIState = 0;
				/* ���õ���VO����Ƭ */
				setVoToBillCard(m_nPresentRecord, null);
				/* ���ð�ť�߼� */
				setButtonsCard();
			}
		}
	}

	/**
	 * ��������:����
	 */
	private void onAppend() {
		showHintMessage(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000050")/* @res"��������..." */);
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

		// ���ò���Ա
		String strUserId = getClientEnvironment().getUser().getPrimaryKey();
		UIRefPane nRefPanel6 = (UIRefPane) getBillCardPanel().getTailItem(
				"coperator").getComponent();
		nRefPanel6.setPK(strUserId);
		nRefPanel6.setEnabled(false);

		// ȡ����Ա��Ӧҵ��Ա�������빺��(�빺����ֵʱ������)
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
				// ���빺�˴����빺����(����빺������ֵʱ�Ŵ���)
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

		/* �����Ҽ��˵��밴ť�顰�в�����Ȩ����ͬ */
		setPopMenuItemsEnable();

		// �������޸�
		onAppendLine(getBillCardPanel(), this);

		// �ù�굽��ͷ��һ���ɱ༭��Ŀ
		getBillCardPanel().transferFocusTo(BillCardPanel.HEAD);
		//
		showHintMessage(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000533")/* @res "���ӡ��༭����" */);
	}

	/**
	 * ��������:����
	 */
	private static void onAppendLine(BillCardPanel bcp, ToftPanel uiPanel) {
		uiPanel.showHintMessage("");
		bcp.addLine();
		bcp.setEnabled(true);
		/* ������Ŀ�Զ�Э�� */
		nc.ui.pu.pub.PuTool.setBodyProjectByHeadProject(bcp, "cprojectidhead",
				"cprojectid", "cprojectname",
				nc.vo.scm.pu.PuBillLineOprType.ADD);
		/* �����к� */
		BillRowNo.addLineRowNo(bcp, BillTypeConst.PO_PRAY, "crowno");
		// ���в������Զ����뵱ǰ��¼��˾
		bcp.setBodyValueAt(PoPublicUIClass.getLoginPk_corp(),
				bcp.getRowCount() - 1, "pk_reqcorp");
		bcp.execBodyFormula(bcp.getRowCount() - 1, "reqcorpname");
		bcp.setBodyValueAt(PoPublicUIClass.getLoginPk_corp(),
				bcp.getRowCount() - 1, "pk_purcorp");
		bcp.execBodyFormula(bcp.getRowCount() - 1, "purcorpname");

		setPartNoEditable(bcp);
		//
		uiPanel.showHintMessage(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000535")/* @res "��������һ��" */);
	}

	/**
	 * ��ȡ�����������VO
	 * 
	 * @param voAudit
	 *            �����µ�VO
	 * @return
	 * @since v50
	 * @author czp
	 */
	private Integer setLastestInfosToVoAfterAuditted(PraybillVO voAudit)
			throws Exception {

		/* �����ɹ���ˢ�� */
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
		// ֧�������޸�������ͬ������ʱ���
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
	 * �������ڣ� 2005-9-28 ���������� ��ð汾�Ŵ���1�ĵ��ݺ� ����ʱ���� ����
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
	 * ��������:ȡ������
	 */
	protected void onUnAuditList() {
		int iCnt = 0;
		String Cpraybillid = null;
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040102",
				"UPP40040102-000018")/* @res "�������󵥾�..." */);
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
																 * "�빺��ȡ������"
																 */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40040102",
							"UPP40040102-000003")/* @res "δѡ���빺����" */);
			return;
		}
		long tTime = System.currentTimeMillis();
		Vector vTemp = new Vector();
		for (int i = 0; i < nSelected.length; i++) {
			// ������
			PraybillVO vo = m_VOs[nSelected[i].intValue()];
			vo.getHeadVO().setCuserid(
					getClientEnvironment().getUser().getPrimaryKey());
			vo.getHeadVO().setCauditpsn(
					getClientEnvironment().getUser().getPrimaryKey());

			

			vTemp.addElement(vo);
		}
		PraybillVO VOs[] = new PraybillVO[vTemp.size()];
		vTemp.copyInto(VOs);

		// -----------------------v31sp added zhongwei �汾�Ŷ����������
		String versions = getVersionOfCantUnaudit(VOs);
		if (versions != null && versions.trim().length() > 0) {
			showErrorMessage(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
					.getStrByID(
							"40040101",
							"UPP40040101-000527",
							null,
							new String[] { versions.substring(0, versions
									.length() - 2) })/*
													 * @res "�޶����빺����������!\n
													 * ���ݺţ�{0}"
													 */);
			return;
		}
		// -----------------------v31sp added zhongwei �汾�Ŷ����������

		try {
			// �������빺��������Ա
			
			String strOpr = getClientEnvironment().getUser().getPrimaryKey();
			for (int i = 0; i < VOs.length; i++) {
				VOs[i].setCurrOperator(strOpr);
				VOs[i].getHeadVO().setCuserid(strOpr);
				VOs[i].getHeadVO().setTaudittime(null);
				Cpraybillid=VOs[i].getHeadVO().getCpraybillid();
			}
			// ������������ʱ�ż��ر���

			VOs = PrTool.getRefreshedVOs(VOs);
			PfUtilClient.processBatch(this, "UNAPPROVE", "20",
					getClientEnvironment().getDate().toString(), VOs, null);
			if (!PfUtilClient.isSuccess()) {
				// ����ʧ��
				return;
			}
			iCnt = VOs.length;
			
		} catch (java.sql.SQLException e) {
			MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("40040102", "UPP40040102-000020")/*
																 * @res
																 * "ȡ���빺������"
																 */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
							"UPPSCMCommon-000412")/* @res "SQL������" */);
			SCMEnv.out(e);
			return;
		} catch (ArrayIndexOutOfBoundsException e) {
			MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("40040102", "UPP40040102-000020")/*
																 * @res
																 * "ȡ���빺������"
																 */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
							"UPPSCMCommon-000426")/* @res "����Խ�����" */);
			SCMEnv.out(e);
			return;
		} catch (NullPointerException e) {
			MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("40040102", "UPP40040102-000020")/*
																 * @res
																 * "ȡ���빺������"
																 */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
							"UPPSCMCommon-000427")/* @res "��ָ�����" */);
			SCMEnv.out(e);
			return;
		} catch (nc.vo.pub.BusinessException e) {
			MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("40040102", "UPP40040102-000020")/*
																 * @res
																 * "ȡ���빺������"
																 */, e
					.getMessage());
			SCMEnv.out(e);
			return;
		} catch (Exception e) {
			MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("40040102", "UPP40040102-000020")/*
																 * @res
																 * "ȡ���빺������"
																 */, e
					.getMessage());
			SCMEnv.out(e);
			return;
		}
		tTime = System.currentTimeMillis() - tTime;
		SCMEnv.out("ȡ������ʱ�䣺" + tTime + " ms!");
		//lcq add by 2015/6/19  �Ƴ��б�ע
		String sql="UPDATE po_praybill SET Vmemo=' ' WHERE cpraybillid='"+Cpraybillid+"'";
		IPubDMO getdmo = (IPubDMO) NCLocator.getInstance().lookup(IPubDMO.class.getName());
		try {
			getdmo.executeUpdate(sql);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//lcq end by   �Ƴ��б�ע
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
				"UCH011")/* @res "����ɹ�" */);
		//
		onListRefresh();
	}

	/**
	 * ��������:����--�б�
	 */
	public void onAuditList() {
		int iCnt = 0;
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
				"SCMCOMMON", "UPPSCMCommon-000403")/* @res "������������..." */);
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
							"UPP40040102-000002")/* @res "�빺������" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40040102",
							"UPP40040102-000003")/* @res "δѡ���빺����" */);
			return;
		}
		long tTime = System.currentTimeMillis();
		Vector vTemp = new Vector();
		for (int i = 0; i < nSelected.length; i++) {
			// ������
			PraybillVO vo = m_VOs[nSelected[i].intValue()];
			vo.getHeadVO().setCuserid(
					getClientEnvironment().getUser().getPrimaryKey());
			vTemp.addElement(vo);
		}

		PraybillVO VOs[] = new PraybillVO[vTemp.size()];
		vTemp.copyInto(VOs);

		/*
		 * �޸ģ�zhongwei v31-sp1-���� �������ڲ���С���빺����
		 */
		Vector v_ids = new Vector();
		for (int i = 0; i < VOs.length; i++) {
			PraybillHeaderVO headVO = VOs[i].getHeadVO();

			if (headVO.getDpraydate().toString().compareTo(
					getClientEnvironment().getDate().toString()) > 0)
				// ���治����Ҫ����빺�����
				v_ids.add(headVO.getVpraycode());

			headVO.setCauditpsn(getClientEnvironment().getUser()
					.getPrimaryKey());
			headVO.setDauditdate(getClientEnvironment().getDate());
		}

		/*
		 * ��ʾ������Ϣ������
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
																		 * "�����������ڲ���С��{0}!\n���ݺţ�{1}��\n�����µ�¼������(Ҫ�󣺵�¼��ҵ�����ڴ��ڵ���{2}) "
																		 */);
			return;
		}

		try {
			// �������빺��������Ա
			String strOpr = getClientEnvironment().getUser().getPrimaryKey();
			for (int i = 0; i < VOs.length; i++) {
				VOs[i].setCurrOperator(strOpr);
				VOs[i].getHeadVO().setCuserid(strOpr);
				VOs[i].getHeadVO().setTaudittime(
						(new UFDateTime(new Date())).toString());
			}
			// ������������ʱ�ż��ر���
			VOs = PrTool.getRefreshedVOs(VOs);
			PfUtilClient.processBatchFlow(this, "APPROVE", "20",
					getClientEnvironment().getDate().toString(), VOs, null);
			if (!PfUtilClient.isSuccess()) {
				// �������빺��
				return;
			}
			iCnt = VOs.length;
		} catch (java.sql.SQLException e) {
			MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40040102",
							"UPP40040102-000002")/* @res "�빺������" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
							"UPPSCMCommon-000412")/* @res "SQL������" */);
			SCMEnv.out(e);
			return;
		} catch (ArrayIndexOutOfBoundsException e) {
			MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40040102",
							"UPP40040102-000002")/* @res "�빺������" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
							"UPPSCMCommon-000426")/* @res "����Խ�����" */);
			SCMEnv.out(e);
			return;
		} catch (NullPointerException e) {
			MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40040102",
							"UPP40040102-000002")/* @res "�빺������" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
							"UPPSCMCommon-000427")/* @res "��ָ�����" */);
			SCMEnv.out(e);
			return;
		} catch (nc.vo.pub.BusinessException e) {
			MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40040102",
							"UPP40040102-000002")/* @res "�빺������" */, e
							.getMessage());
			SCMEnv.out(e);
			return;
		} catch (Exception e) {
			MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40040102",
							"UPP40040102-000002")/* @res "�빺������" */, e
							.getMessage());
			SCMEnv.out(e);
			return;
		}
		tTime = System.currentTimeMillis() - tTime;
		SCMEnv.out("�빺������ʱ�䣺" + tTime + " ms!");

		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
				"SCMCOMMON", "UPPSCMCommon-000236")/* @res "��˳ɹ�" */);

		onListRefresh();
	}

	/**
	 * ����:ִ������--��Ƭ
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
				 * �޸ģ�zhongwei v31-sp1-���� �������ڲ���С���Ƶ�����(�빺����)
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
															"UC000-0003665") })/* "�����������ڲ���С��{0}!\n���ݺţ�{1}��\n�����µ�¼������(Ҫ�󣺵�¼��ҵ�����ڴ��ڵ���{2}) " */);
				}

				showHintMessage(m_lanResTool.getStrByID("40040101",
						"UPP40040101-000051")/* "��������..." */);
				nc.vo.scm.pu.Timer timer = new nc.vo.scm.pu.Timer();
				timer.start("�빺����������onAudit������ʼ");

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
				timer.addExecutePhase("����ǰ׼������");
				/* ���� */
				PraybillVO[] oaUserObj = new PraybillVO[] { voCloned };
				PfUtilClient.processBatchFlow(null, "APPROVE",
						nc.vo.scm.pu.BillTypeConst.PO_PRAY,
						getClientEnvironment().getDate().toString(),
						new PraybillVO[] { voAudit }, oaUserObj);
				if (!PfUtilClient.isSuccess()) {
					showHintMessage(m_lanResTool.getStrByID("40040101",
							"UPP40040101-000052")/* "����δ�ɹ�" */);
					return;
				}
				timer.addExecutePhase("ִ��APPROVE�ű�����");

				// �����ɹ���ˢ��
				Integer iBillStatus = setLastestInfosToVoAfterAuditted(voAudit);
				timer.addExecutePhase("�����ɹ���ˢ��");
				m_VOs[m_nPresentRecord] = voAudit;

				/* ���ص��� */
				setVoToBillCard(m_nPresentRecord, m_lanResTool.getStrByID(
						"40040101", "UPP40040101-000447")/* "������" */);
				/* ˢ�°�ť״̬ */
				setButtonsCard();
				timer.addExecutePhase("��������ʾ");
				timer.showAllExecutePhase("�빺����������onAudit��������");
				getBillCardPanel().setEnabled(false);
				if (iBillStatus.compareTo(nc.vo.scm.pu.BillStatus.FREE) == 0
						|| iBillStatus
								.compareTo(nc.vo.scm.pu.BillStatus.AUDITFAIL) == 0) {
					showHintMessage(m_lanResTool.getStrByID("40040101",
							"UPP40040101-000052")/* "����δ�ɹ�" */);

				} else if (iBillStatus
						.compareTo(nc.vo.scm.pu.BillStatus.AUDITED) == 0
						|| iBillStatus
								.compareTo(nc.vo.scm.pu.BillStatus.AUDITING) == 0) {
					showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
							"UPPSCMCommon-000236")/* "�����ɹ�" */);
				}
			} catch (Exception e) {
				// ������ʽ���ɲɹ��������ݲ���ʾ���
				if (e instanceof RwtPoToPrException) {
					// �빺���ۼ�����������ʾ
					int iRet = showYesNoMessage(e.getMessage());
					if (iRet == MessageDialog.ID_YES) {
						// ����ѭ��
						isCycle = true;
						// �Ƿ��û�ȷ��
						voAudit.setUserConfirm(true);
					}
				} else {
					PuTool.outException(this, e);
				}
				showHintMessage(m_lanResTool.getStrByID("40040101",
						"UPP40040101-000054")/* "�����쳣,����ʧ��" */);
			}
		}
	}

	/**
	 * getBatchPrintEntry()�� �������ڣ�(2004-12-09) ��ô�ӡ���
	 * 
	 * @author������
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
	 * ҵ������--����¼���Ӧ
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
		// ά���빺��ҳ�棬��ʱ�빺������δ��ˣ�������ά�����¡��޸ġ��������ơ��������ϡ���ť���ã���ʱ�����ҵ�����͡���ĳ�����ͣ���ʱ�ٲ鿴������ά�������޸ġ��������ơ��������ϡ���ťӦ�û�����(��ʱ��δ��������״̬)��
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
																		 * "��ǰ����ҵ�����ͣ�"
																		 */);
	}

	/**
	 * ����
	 * 
	 * @modified by czp v50, �����߼��ع�
	 */
	private void onSendAudit() {

		boolean bSaveFlag = getBillCardPanel().getBillData().getEnabled();
		PraybillVO vo = null;
		try {
			// �༭״̬���󣽡����桱��������
			if (bSaveFlag) {
				boolean bContinue = onSave();
				if (!bContinue) {
					return;
				}
			}
			// ��ȡˢ�º��VO����
			vo = getPraybillVOs()[getCurVoPos()];
			if (vo == null) {
				setButtonsCard();
				SCMEnv.out("�빺��VOΪ�գ�����ɹ��������ܼ�������");/* -=notranslate=- */
				showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
						"UPPSCMCommon-000408")/* @res"����ʧ�ܣ�" */);
				return;
			}
			// ����
			PfUtilClient.processAction("SAVE", BillTypeConst.PO_PRAY,
					ClientEnvironment.getInstance().getDate().toString(), vo);

			// ˢ�µ��ݣ�֧�������������
			setLastestInfosToVoAfterAuditted(vo);

			// ˢ�°�ť״̬
			setButtonsCard();

			/* ���ص��� */
			setVoToBillCard(m_nPresentRecord, m_lanResTool.getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000265")/* "����" */);
			//
			updateButtonsAll();
			// ���������־
			isAlreadySendToAudit = false;
			showHintMessage(m_lanResTool.getStrByID("common", "UCH023")/*
																		 * @res"������"
																		 */);
		} catch (Exception e) {
			SCMEnv.out(e);
			if (e instanceof BusinessException || e instanceof RuntimeException) {
				MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
						"40040101", "UPP40040101-000081")/* @res "�빺������" */, e
						.getMessage());
			} else {
				MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
						"SCMCOMMON", "UPPSCMCommon-000270")/* @res "��ʾ" */,
						m_lanResTool.getStrByID("SCMCOMMON",
								"UPPSCMCommon-000408")/* @res"����ʧ�ܣ�" */);
			}
		}

	}

	/**
	 * ��Ƭ��ť��Ӧ��
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * ʹ��ʾ��
	 * <p>
	 * <b>����˵��</b>
	 * 
	 * @param bo
	 *            <p>
	 * @author czp
	 * @time 2007-3-14 ����10:47:22
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
		/* ����״̬��ѯ������Ϣ���Ĺ��� */
		else if (bo == m_btnWorkFlowBrowse) {
			onQueryForAudit();
		} else if (bo == m_btnUsable) {
			onQueryInvOnHand();
		} else if (bo == m_btnPriceInfo) {
			onPriceInfos();
		}
		/* �ĵ���������Ϣ���Ĺ��� */
		else if (bo == m_btnDocument) {
			onDocument();
		}
		/* ������ */
		else if (bo == m_btnLinkBillsBrowse) {
			onLinkQuery();
		}
		/* ��Ϣ���İ�ť */
		else if (bo == m_btnAudit) {
			onAudit();
		} else if (bo == m_btnUnAudit) {
			onUnAudit();
		} else if (bo == m_btnPrint) {
			onPrint();
		} else if (bo == m_btnPrintPreview) {
			onPrintPreview();
		}
		// �ϲ���ʾ����ӡ
		else if (bo == m_btnCombin) {
			onCombin();
		}
		// ˢ��
		else if (bo == m_btnRefresh) {
			onCardRefresh();
		}
		else if (bo == btnCKKCXX){
			onCKKCXX();
		}
	}

	/**
	 * �б�ť��Ӧ��
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * ʹ��ʾ��
	 * <p>
	 * <b>����˵��</b>
	 * 
	 * @param bo
	 *            <p>
	 * @author czp
	 * @time 2007-3-14 ����10:47:42
	 */
	private void onButtonClickedList(ButtonObject bo) {
		if (bo == m_btnModify) {
			onModify(true);
		} else if (bo == m_btnDiscardList) {
			onDiscard();
		} else if (bo == m_btnCopy) {
			onCopyList();
		}
		// ����
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
		/* �л�����Ƭ */
		else if (bo == m_btnCard) {
			onCard();
		} else if (bo == m_btnWorkFlowBrowse) {
			onQueryForAudit();
		} else if (bo == m_btnUsableList) {
			onQueryInvOnHand();
		} else if (bo == m_btnDocumentList) {
			onDocument();
		}
		/* ������ */
		else if (bo == m_btnLinkBillsBrowse) {
			onLinkQuery();
		}
		/* ȫѡ */
		else if (bo == m_btnSelectAll) {
			onSelectAll();
		}
		/* ȫ�� */
		else if (bo == m_btnSelectNo) {
			onSelectNo();
		}
		/* ��ѯ-�б� */
		else if (bo == m_btnQueryList) {
			onListQuery();
		}
		// �б��ӡ
		else if (bo == m_btnPrintList) {
			onPrintList();
		}
		// �б��ӡԤ��
		else if (bo == m_btnPrintListPreview) {
			onPrintListPreview();
		}
		// ˢ��
		else if (bo == m_btnRefreshList) {
			onListRefresh();
		}
		else if (bo == btnCKKCXX){
			onCKKCXX();
		}

	}

	/**
	 * ����ʵ�ָ÷�������Ӧ��ť�¼���
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
		// ������չ��ť�¼�
		onExtendBtnsClick(bo);

		// ������չ��ť״̬
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
//				MessageDialog.showErrorDlg(this, null, "��ѡ����Ҫ�鿴�ĵ���");
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
					// ���깺δ��������
					String sql1 = new StringBuilder().append("select sum(A.npraynum) as rst from po_praybill_b A,po_praybill B").append(" where A.cpraybillid=B.cpraybillid and nvl(A.dr,0)=0 and nvl(B.dr,0)=0").append(" and B.pk_corp='" + pk_corp + "' and B.ibillstatus=0").append(" and B.dpraydate<='" + dateto + "' and B.dpraydate>='" + startDate + "'").append(" and A.cbaseid='" + invbasid + "'").toString();
					// ���깺����������
					String sql2 = new StringBuilder().append("select sum(A.npraynum) as rst from po_praybill_b A,po_praybill B").append(" where A.cpraybillid=B.cpraybillid and nvl(A.dr,0)=0 and nvl(B.dr,0)=0").append(" and B.pk_corp='" + pk_corp + "' and B.ibillstatus=3").append(" and B.dpraydate<='" + dateto + "' and B.dpraydate>='" + startDate + "'").append(" and A.cbaseid='" + invbasid + "'").toString();
					// �ɹ��������� edit by  yhj 2014-09-10 �������ѹر����� and A.iisactive = 0  and B.breturn = 'N'
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

					//add by yhj 2014-09-10 �ɹ����������������ѹر������� 
 					String closeNumSQL = new StringBuilder().append("select sum(A.nordernum) as rst from po_order_b A,po_order B").append(" where A.corderid=B.corderid").append(" and nvl(A.dr,0)=0 and nvl(B.dr,0)=0").append(" and B.pk_corp='" + pk_corp + "'").append(" and B.dorderdate<='" + dateto + "' and B.dorderdate>='" + startDate + "'").append(" and A.cbaseid='" + invbasid + "'").toString();
				//�ɹ����������������ѹر�������
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
					// ����������
//					String sql4 = new StringBuilder().append("select sum(A.narrvnum) as rst from po_arriveorder_b A,po_arriveorder B").append(" where A.carriveorderid=B.carriveorderid").append(" and nvl(A.dr,0)=0 and nvl(B.dr,0)=0").append(" and B.pk_corp='" + pk_corp + "'").append(" and B.dreceivedate<='" + dateto + "' and B.dreceivedate>='" + startDate + "'").append(" and A.cbaseid='" + invbasid + "'").toString();
					//edit by zwx  2015-9-13 ���˲ɹ�����Ϊ2013���δ�رյ����ɵĵ���������
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
					// ����ϸ�����
					String sql5 = new StringBuilder().append("select sum(A.nelignum) as rst from po_arriveorder_b A,po_arriveorder B").append(" where A.carriveorderid=B.carriveorderid").append(" and nvl(A.dr,0)=0 and nvl(B.dr,0)=0").append(" and B.pk_corp='" + pk_corp + "'").append(" and B.dreceivedate<='" + dateto + "' and B.dreceivedate>='" + startDate + "'").append(" and A.cbaseid='" + invbasid + "'").toString();
					// �Ѷ�������
					String sql6 = new StringBuilder().append("select sum(nvl(nfreezenum,0)) as rst from ic_freeze where nvl(dr, 0) = 0 and pk_corp = '" + pk_corp + "' and cinvbasid = '" + invbasid + "' and cthawpersonid is null and cspaceid is not null").toString();
					// �ִ���
					String sql7 = new StringBuilder().append("select sum(nonhandnum) from ic_onhandnum where nvl(dr,0)=0 and cinvbasid='"+invbasid+"' and pk_corp='"+pk_corp+"'").toString();
					// �ٶ�����
					String sql8 = new StringBuilder().append("select zdhd from bd_produce where nvl(dr,0)=0 and pk_invbasdoc='"+invbasid+"' and pk_corp='"+pk_corp+"'").toString();
					// ��߿��
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
//					bodyBM.setValueAt(d1+d2, i, "pk_defdoc10"); // ���깺(��δ����)
					//edit by yhj 2014-09-10
					obj = queryBS.executeQuery(closeNumSQL.toString(), new ColumnProcessor());
					d10 = obj == null ? 0 : Double.parseDouble(obj.toString());
					//add by yhj 20140910  ���������嵥��ȫѡ��������ť����ʾ��Ӧ���ݣ���ѡҲ�������Ӵ���к��ٵ��û�����ݡ���Ӧ�������嵥24������ı�ע�������
					Double b1 = d1+d2-new2;
 					Double b2 = d3 - d4;
					Double b3 = d7-d6;
					Double b4 = d8;
					Double b5 = d9;
					//end
					bodyBM.setValueAt(b1.toString(), i, "pk_defdoc10"); // ���깺(��δ����)-�ɹ���������(�����ر�)
					bodyBM.setValueAt(b2.toString(), i, "pk_defdoc11"); // �Ѷ���δ��������
					bodyBM.setValueAt(b3.toString(), i, "pk_defdoc12"); // �ִ��������(�ִ���-�Ѷ���)
					bodyBM.setValueAt(b4.toString(), i, "pk_defdoc13"); // �ٶ�������
					bodyBM.setValueAt(b5.toString(), i, "pk_defdoc14"); // ��߿����
					//add by zwx 2015-9-12 
					bodyBM.setValueAt(new UFDouble(d7), i, "vdef8");//�ִ���
					//end by zwx
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	// add end

	/**
	 * ��ӡԤ��-�б�
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
					"SCMCOMMON", "UPPSCMCommon-000270")/* @res"��ʾ" */,
					printList.getPrintMessage());
		} catch (BusinessException e) {
			PuTool.outException(this, e);
		}
	}

	/**
	 * ��ӡ-�б�
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
					"SCMCOMMON", "UPPSCMCommon-000270")/* "��ʾ" */, printList
					.getPrintMessage());
		} catch (BusinessException e) {
			PuTool.outException(this, e);
		}
	}

	/**
	 * ��ӡԤ��--��Ƭ
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
				// Ŀǰ�����Ͼ������벹����
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
					"SCMCOMMON", "UPPSCMCommon-000270")/* "��ʾ" */, printCard
					.getPrintMessage());
		} catch (Exception e1) {
			PuTool.outException(this, e1);
		}
	}

	/**
	 * ��ӡ--��Ƭ
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
				// Ŀǰ�����Ͼ������벹����
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
					"SCMCOMMON", "UPPSCMCommon-000270")/* "��ʾ" */, printCard
					.getPrintMessage());
		} catch (Exception e1) {
			PuTool.outException(this, e1);
		}
	}

	/**
	 * ��������:����
	 */
	public void onCancel() {
		isFrmCopy = false;
		/* ��������ȡ�� */
		if (m_bCancel)
			return;
		else
			m_bCancel = true;
		// ��ֹ�༭
		if (getBillCardPanel().getBillTable().getEditingRow() >= 0) {
			getBillCardPanel().getBillTable().editingStopped(
					new ChangeEvent(getBillCardPanel().getBillTable()));
		}
		/*
		 * ���ӱ�־ΪFALSE,�༭��־ΪFALSE,���ݴ��ڲ��ɱ༭״̬ ������״̬�·������������ǰ���ݴ���,��ʾ��Ӧ�ĵ���;����,��ʾΪ��
		 * ���޸�״̬�·�������ʾ����ǰ�ĵ���
		 */
		if (m_bAdd) {
			if (m_VOs != null && m_VOs.length > 0) {
				/* �������ǰ���ݴ���,����ʾ��Ӧ�ĵ��� */
				if (m_VOs != null
						&& m_VOs[m_nPresentRecord] != null
						&& m_VOs[m_nPresentRecord].getHeadVO().getCpraybillid() != null) {
					getBillCardPanel().setBillValueVO(m_VOs[m_nPresentRecord]);
					nc.ui.pu.pub.PuTool.loadInvMaxPrice(getBillCardPanel());
					/* ��ʾ��Դ��Ϣ */
					PuTool.loadSourceInfoAll(getBillCardPanel(),
							BillTypeConst.PO_PRAY);
					getBillCardPanel().getBillModel().execLoadFormula();
					getBillCardPanel().updateValue();
					getBillCardPanel().updateUI();
					/* ��ʾ��ע */
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
				/* �������ǰ���ݲ�����,��������� */
				getBillCardPanel().getBillData().clearViewData();
				getBillCardPanel().updateUI();
			}
		} else {
			getBillCardPanel().setBillValueVO(m_VOs[m_nPresentRecord]);
			nc.ui.pu.pub.PuTool.loadInvMaxPrice(getBillCardPanel());
			getBillCardPanel().getBillModel().execLoadFormula();
			/* ��ʾ��Դ��Ϣ */
			PuTool.loadSourceInfoAll(getBillCardPanel(), BillTypeConst.PO_PRAY);
			getBillCardPanel().updateValue();
			getBillCardPanel().updateUI();
			/* ��ʾ��ע */
			UIRefPane nRefPanel3 = (UIRefPane) getBillCardPanel().getHeadItem(
					"vmemo").getComponent();
			nRefPanel3.setValue(m_VOs[m_nPresentRecord].getHeadVO().getVmemo());
		}
		setButtonsCard();
		/* �ָ���ť״̬ */
		m_nUIState = 0;
		m_bAdd = false;
		m_bEdit = false;
		getBillCardPanel().setEnabled(false);

		// ��־ƽ ������������������������ʾ����
		nc.ui.po.pub.InvAttrCellRenderer ficr = new nc.ui.po.pub.InvAttrCellRenderer();
		ficr.setFreeItemRenderer(getBillCardPanel(), null);

		showHintMessage(m_lanResTool.getStrByID("common", "UCH008")/* "ȡ���ɹ�" */);
	}

	/**
	 * ��������:��Ƭ״̬��ѯ
	 */
	private void onCardQuery() {

		getQueryDlg().showModal();

		if (getQueryDlg().isCloseOK()) {

			m_bQueried = true;

			// //��������Ȩ��, czp ,since v50
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
																	 * "��ѯ���"
																	 */);
	}

	/**
	 * ��Ƭˢ��
	 * 
	 */
	private void onCardRefresh() {
		showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
				"UPPSCMCommon-000360")/* "��ʼ��ѯ..." */);
		// �Զ�������
		ConditionVO[] conditionVO = getQueryDlg().getConditionVO();
		// ��Դ��������
		String strSubSql = getQueryDlg().getSubSQL();
		// ��ѯ�빺��
		try {
			m_VOs = PraybillHelper.queryAll(getQueryDlg()
					.getSelectedCorpIdString(), conditionVO, getQueryDlg()
					.getStatusCndStr(), null, strSubSql);
		} catch (BusinessException e) {
			showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
					"UPPSCMCommon-000038")/* "��ѯʧ��" */);
			MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
					"40040101", "UPP40040101-000055")/* "�빺����ѯ" */, e
					.getMessage());
			return;
		} catch (Exception e) {
			showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
					"UPPSCMCommon-000038")/* "��ѯʧ��" */);
			MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
					"40040101", "UPP40040101-000055")/* "�빺����ѯ" */, e
					.getMessage());
			return;
		}
		// �����ز�ѯ���
		if (m_VOs != null && m_VOs.length > 0) {
			// �����ͷ�����б�
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

			// ���ص��ݵ���Ƭ
			m_nPresentRecord = 0;
			setVoToBillCard(m_nPresentRecord, null);
		} else {
			m_VOs = null;
			// �޷��ز�ѯ���
			MessageDialog.showHintDlg(this, m_lanResTool.getStrByID("40040101",
					"UPP40040101-000055")/* "�빺����ѯ" */, m_lanResTool.getStrByID(
					"40040101", "UPP40040101-000056")/* "û�з����������빺����" */);
			// �������
			getBillCardPanel().getBillData().clearViewData();
		}
		// ���ð�ť�߼�
		setButtonsCard();
		//
		m_nUIState = 0;
		// �����½���ʾ��Ϣ
		int iCnt = 0;
		if (m_VOs != null && m_VOs.length > 0)
			iCnt = m_VOs.length;
		if (iCnt > 0) {
			String[] value = new String[] { String.valueOf(iCnt) };
			showHintMessage(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000478", null, value)/*
													 * "��ѯ��ϣ����� " +iCnt + "�ŵ���"
													 */);
		} else
			showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
					"UPPSCMCommon-000361")/* "��ѯ��ϣ�û�в鵽����" */);
		m_bAdd = false;
		m_bEdit = false;
		getBillCardPanel().setEnabled(false);
		showHintMessage(m_lanResTool.getStrByID("common", "UCH007")/*
																	 * @res
																	 * "ˢ�³ɹ�"
																	 */);
	}

	/**
	 * ��������:�رյ�ǰ���빺��
	 */
	private void onCloseList() {
		this.showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
				"UPPSCMCommon-000390")/* "���ڹرյ���..." */);
		m_VOs[m_nPresentRecord].getHeadVO().setCuserid(
				getClientEnvironment().getUser().getPrimaryKey());
		long tTime = System.currentTimeMillis();
		try {
			// ������Ա
			String strOpr = getClientEnvironment().getUser().getPrimaryKey();
			m_VOs[m_nPresentRecord].setCurrOperator(strOpr);
			ArrayList keys = (ArrayList) PfUtilClient
					.processActionNoSendMessage(this, "CLOSE",
							nc.vo.scm.pu.BillTypeConst.PO_PRAY,
							getClientEnvironment().getDate().toString(),
							m_VOs[m_nPresentRecord], null, null, null);
			m_VOs[m_nPresentRecord] = (PraybillVO) keys.get(0);

			m_VOs[m_nPresentRecord].getHeadVO().setIbillstatus(new Integer(1));
			// ���ݹرպ�����
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
					"40040101", "UPP40040101-000057")/* "�빺���ر�" */, e
					.getMessage());
			SCMEnv.out(e);
			showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
					"UPPSCMCommon-000391")/* "�ر�ʧ��" */);
			return;
		}
		tTime = System.currentTimeMillis() - tTime;
		Logger.debug("�빺���ر�ʱ�䣺" + tTime + " ms!");
		m_nUIState = 0;
		getBillListPanel().getHeadBillModel().setBodyRowVO(
				m_VOs[m_nPresentRecord].getHeadVO(), m_nPresentRecord);
		getBillListPanel().getBodyBillModel().setBodyDataVO(
				m_VOs[m_nPresentRecord].getBodyVO());
		updateUI();
		/* ˢ�°�ť״̬ */
		setButtonsList();
		//
		showHintMessage(m_lanResTool.getStrByID("common", "UCH013")/*
																	 * @res
																	 * "�ѹر�"
																	 */);
		return;
	}

	/**
	 * ��������:�رյ�ǰ���빺��
	 */
	private void onCloseCard() {
		this.showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
				"UPPSCMCommon-000390")/* "���ڹرյ���..." */);
		m_VOs[m_nPresentRecord].getHeadVO().setCuserid(
				getClientEnvironment().getUser().getPrimaryKey());
		long tTime = System.currentTimeMillis();
		try {
			// ������Ա
			String strOpr = getClientEnvironment().getUser().getPrimaryKey();
			m_VOs[m_nPresentRecord].setCurrOperator(strOpr);
			ArrayList keys = (ArrayList) PfUtilClient
					.processActionNoSendMessage(this, "CLOSE",
							nc.vo.scm.pu.BillTypeConst.PO_PRAY,
							getClientEnvironment().getDate().toString(),
							m_VOs[m_nPresentRecord], null, null, null);
			m_VOs[m_nPresentRecord] = (PraybillVO) keys.get(0);

			m_VOs[m_nPresentRecord].getHeadVO().setIbillstatus(new Integer(1));
			// ���ݹرպ�����
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
					"40040101", "UPP40040101-000057")/* "�빺���ر�" */, e
					.getMessage());
			SCMEnv.out(e);
			showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
					"UPPSCMCommon-000391")/* "�ر�ʧ��" */);
			return;
		}
		tTime = System.currentTimeMillis() - tTime;
		Logger.debug("�빺���ر�ʱ�䣺" + tTime + " ms!");
		m_nUIState = 0;
		/* ���ص��� */
		setVoToBillCard(m_nPresentRecord, m_lanResTool.getStrByID("40040101",
				"UPP40040101-000448")/* "���ر�" */);
		/* ˢ�°�ť״̬ */
		setButtonsCard();
		//
		showHintMessage(m_lanResTool.getStrByID("common", "UCH013")/*
																	 * @res
																	 * "�ѹر�"
																	 */);
		return;
	}

	/**
	 * ��������:�˳�ϵͳ
	 */
	public boolean onClosing() {
		// ���ڱ༭����ʱ�˳���ʾ
		if (m_bEdit) {
			int iRet = MessageDialog.showYesNoCancelDlg(this, m_lanResTool
					.getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/* "��ʾ" */,
					m_lanResTool.getStrByID("common", "UCH001")/*
																 * @res
																 * "�Ƿ񱣴����޸ĵ����ݣ�"
																 */);
			// ����ɹ�����˳�
			if (iRet == MessageDialog.ID_YES) {
				return onSave();
			}
			// �˳�
			else if (iRet == MessageDialog.ID_NO) {
				return true;
			}
			// ȡ���ر�
			else {
				return false;
			}
		}
		isclose = true;
		return true;
	}

	/**
	 * �б������ܡ�
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * ʹ��ʾ��
	 * <p>
	 * <b>����˵��</b>
	 * <p>
	 * 
	 * @author czp
	 * @time 2007-3-14 ����11:02:12
	 */
	private void onCopyList() {
		// ���л�����Ƭ
		onCard();
		// ���߿�Ƭ��������
		onCopy();
	}

	/**
	 * ��������:����
	 */
	private void onCopy() {
		showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
				"UPPSCMCommon-000350")/* "�༭����..." */);

		nc.vo.scm.pu.Timer timer = new nc.vo.scm.pu.Timer();
		timer.start("�빺�����Ʋ�����ʼonCopy");

		isFrmCopy = true;
		// ��������޼�
		nc.ui.pu.pub.PuTool.loadInvMaxPrice(getBillCardPanel());

		timer.addExecutePhase("��������޼�loadInvMaxPrice");

		// �����빺��ԴΪ�ֹ�¼��,��������Ϊ��ǰ����(�������۶����⣬��Ϊ�л�д��ϵ)
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

		// ��������˴���,�����������
		nRefPane = (UIRefPane) getBillCardPanel().getTailItem("cauditpsn")
				.getComponent();
		nRefPane.setValue(null);
		// ����������ڴ���,�����������
		nRefPane = (UIRefPane) getBillCardPanel().getTailItem("dauditdate")
				.getComponent();
		nRefPane.setValue(null);
		nRefPane = (UIRefPane) getBillCardPanel().getTailItem("coperator")
				.getComponent();
		nRefPane.setPK(getClientEnvironment().getUser().getPrimaryKey());
		getBillCardPanel().setEnabled(true);
		setPartNoEditable(getBillCardPanel());

		timer.addExecutePhase("һЩ��ֵ����");

		// ���ñ��帨����״̬
		// setAssisUnitEditState();

		timer.addExecutePhase("setAssisUnitEditState����");

		m_bAdd = true;
		m_bEdit = true;
		m_bCancel = false;
		setButtonsCardCopy();
		m_nUIState = 0;
		updateButtonsAll();
		// ����ʱ,���ݺ�Ϊ��
		UIRefPane nRefPanel3 = (UIRefPane) getBillCardPanel().getHeadItem(
				"vpraycode").getComponent();
		nRefPanel3.setValue(null);
		// ��ձ�ͷ����
		getBillCardPanel().getHeadItem("cpraybillid").setValue(null);
		getBillCardPanel().getHeadItem("ibillstatus").setValue(
				new Integer(BillStatus.FREE));
		getBillCardPanel().getHeadItem("ts").setValue(null);
		// ��ձ�������
		BillModel bm = getBillCardPanel().getBillModel();
		int bmCount = bm.getRowCount();
		String sMangId;
		for (int i = 0; i < bmCount; i++) {
			bm.setRowState(i, BillModel.ADD);
			bm.setValueAt(null, i, "cpraybill_bid");
			bm.setValueAt(null, i, "cpraybillid");
			bm.setValueAt(null, i, "naccumulatenum");

			bm.setValueAt(null, i, "ts");
			// ���κ�
			sMangId = (String) bm.getValueAt(i, "cmangid");
			bm.setCellEditable(i, "vproducenum", PuTool.isBatchManaged(sMangId)
					&& bm.getItemByKey("vproducenum").isEdit());
		}

		// ��־ƽ ������������������������ʾ����
		nc.ui.po.pub.InvAttrCellRenderer ficr = new nc.ui.po.pub.InvAttrCellRenderer();
		ficr.setFreeItemRenderer(getBillCardPanel(), m_VOs[m_nPresentRecord]);

		timer.showAllExecutePhase("�빺�����Ʋ�������onCopy");

		showHintMessage(m_lanResTool.getStrByID("common", "UCH029")/*
																	 * @res
																	 * "�Ѹ���"
																	 */);
	}

	/**
	 * ��������:�п���
	 */
	private void onCopyLine() {
		if (!m_bIsSubMenuPressed) {
			int[] nSelected = getBillCardPanel().getBillTable()
					.getSelectedRows();
			if (nSelected == null || nSelected.length == 0) {
				MessageDialog
						.showHintDlg(this, m_lanResTool.getStrByID("common",
								"UC001-0000015")/* @res "������" */, m_lanResTool
								.getStrByID("40040101", "UPP40040101-000059")/* "û��ѡ���빺�������У�" */);
				return;
			}
		}
		getBillCardPanel().copyLine();
		m_bCancel = false;
		m_nUIState = 0;
		updateButtonsAll();
		showHintMessage(m_lanResTool.getStrByID("common", "UCH039")/*
																	 * @res
																	 * "�����гɹ�"
																	 */);
	}

	/**
	 * ��������:ɾ��
	 */
	private void onDeleteLine() {
		if (!getBillCardPanel().delLine()) {
			MessageDialog.showHintDlg(this, m_lanResTool.getStrByID("common",
					"UC001-0000013")/* "ɾ��" */, m_lanResTool.getStrByID(
					"40040101", "UPP40040101-000059")/* "û��ѡ���빺�������У�" */);
			return;
		}
		m_bCancel = false;
		m_nUIState = 0;
		updateButtonsAll();
		// ������ʾ czp 20050303 ����
		PraybillVO voCurr = (PraybillVO) getBillCardPanel().getBillValueVO(
				PraybillVO.class.getName(), PraybillHeaderVO.class.getName(),
				PraybillItemVO.class.getName());
		nc.ui.po.pub.InvAttrCellRenderer ficr = new nc.ui.po.pub.InvAttrCellRenderer();
		ficr.setFreeItemRenderer(getBillCardPanel(), voCurr);
		showHintMessage(m_lanResTool.getStrByID("common", "UCH037")/*
																	 * @res
																	 * "ɾ�гɹ�"
																	 */);
	}

	/**
	 * ��������:�빺������(�б�Ƭ����)
	 */
	private void onDiscard() {
		showHintMessage(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000060")/* "���ϵ���..." */);
		int ret = MessageDialog.showYesNoDlg(this, m_lanResTool.getStrByID(
				"SCMCOMMON", "UPPSCMCommon-000219")/* @res "ȷ��" */,
				m_lanResTool.getStrByID("common", "4004COMMON000000069")/*
																		 * @res
																		 * "�Ƿ�ȷ��Ҫɾ����"
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
			/* �б� */
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
							"UPP40040101-000061")/* "�빺������" */, m_lanResTool
							.getStrByID("40040101", "UPP40040101-000062")/* "δѡ���빺����" */);
			showHintMessage(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000063")/* "���ϵ���ʧ��" */);
			return;
		}
		long tTime = System.currentTimeMillis();
		Vector v = new Vector();
		/*
		 * ����빺��״̬,���������֮һ����������: �رա���������������ͨ��������
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
								"UPP40040101-000463")/* ���빺���Ѿ��رգ��������ϣ�\n" */);
			} else if (nBillStatus == 2) {
				sMessage.append(vo.getHeadVO().getVpraycode()
						+ m_lanResTool.getStrByID("40040101",
								"UPP40040101-000464")/* ���빺�������������������ϣ�\n" */);
			} else if (nBillStatus == 3) {
				sMessage.append(vo.getHeadVO().getVpraycode()
						+ m_lanResTool.getStrByID("40040101",
								"UPP40040101-000465")/* ���빺���Ѿ��������������ϣ�\n" */);
			}
			nBillStatus = vo.getHeadVO().getDr();
			if (nBillStatus > 0) {
				sMessage.append(vo.getHeadVO().getVpraycode()
						+ m_lanResTool.getStrByID("40040101",
								"UPP40040101-000466")/* ���빺���Ѿ����ϣ�\n" */);
			}
		}
		if (sMessage.length() > 0) {
			MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
					"40040101", "UPP40040101-000061")/* "�빺������" */, sMessage
					.toString());
			showHintMessage(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000063")/* "���ϵ���ʧ��" */);
			return;
		}

		PraybillVO[] tempVOs = new PraybillVO[v.size()];
		v.copyInto(tempVOs);
		try {
			/*
			 * ����ƻ�����ά�����ӡ�ȡ���´���ܣ� Ŀǰ�����빺����ɾ���õ�����ȡ���´� ��������ģ�����òŵ��ô˷���
			 */
			if (PuTool.isProductEnabled(nc.ui.po.pub.PoPublicUIClass
					.getLoginPk_corp(), nc.vo.pub.ProductCode.PROD_MM)) {
				nc.ui.pr.pray.PraybillHelper.onRearOrderDelete(sPraybillIds);
			}

			/* ������Ա */
			String strOpr = getClientEnvironment().getUser().getPrimaryKey();
			for (int i = 0; i < m_VOs.length; i++) {
				m_VOs[i].setCurrOperator(strOpr);
				// Ϊ�ж��Ƿ���޸ġ����������˵���
				m_VOs[i].getHeadVO().setCoperatoridnow(strOpr);
				Logger.debug("Coperator = "
						+ m_VOs[i].getHeadVO().getCoperator());
				Logger.debug("Coperatoridnow = "
						+ m_VOs[i].getHeadVO().getCoperatoridnow());
				m_VOs[i].getHeadVO().setCuserid(strOpr);
			}
			/* ��������������ǰ�ż��ر��� */
			tempVOs = PrTool.getRefreshedVOs(tempVOs);
			/* ƽ̨���ϵ��� */
			PfUtilClient.processBatch("DISCARD", "20", getClientEnvironment()
					.getDate().toString(), tempVOs);
			if (PfUtilClient.isSuccess()) {
				PraybillItemVO[] bodyVO;
				/* ���ϳɹ����޸Ļ���ĵ���״̬ */
				for (int i = 0; i < nSelected.length; i++) {
					m_VOs[nSelected[i].intValue()].getHeadVO().setDr(1);
					bodyVO = m_VOs[nSelected[i].intValue()].getBodyVO();
					for (int j = 0; j < bodyVO.length; j++)
						bodyVO[j].setDr(1);
				}
			} else {
				showHintMessage(m_lanResTool.getStrByID("40040101",
						"UPP40040101-000063")/* "���ϵ���ʧ��" */);
				return;
			}
		} catch (nc.vo.pub.BusinessException e) {
			MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
					"40040101", "UPP40040101-000061")/* "�빺������" */, e
					.getMessage());
			showHintMessage(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000063")/* "���ϵ���ʧ��" */);
			return;
		} catch (Exception e) {
			MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
					"40040101", "UPP40040101-000061")/* "�빺������" */, e
					.getMessage());
			SCMEnv.out(e);
			showHintMessage(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000063")/* "���ϵ���ʧ��" */);
			return;
		}
		tTime = System.currentTimeMillis() - tTime;
		Logger.debug("�빺������ʱ�䣺" + tTime + " ms!");
		/* �빺�����Ϻ�,�����ڽ�����ʾ */
		Vector vTemp = new Vector();
		int vvSize = vv.size();
		int m_VOsLength = m_VOs.length;
		if (bCardShowing) {
			/* ��Ƭ */
			for (int i = 0; i < m_VOsLength; i++) {
				if (m_VOs[i].getHeadVO().getDr() == 0) {
					vTemp.addElement(m_VOs[i]);
				}
			}
		} else {
			/* �б� */
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
			/* ���е��������� */
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
																		 * "ɾ���ɹ�"
																		 */);
			return;
		}
		/* ���ֵ��������� */
		if (m_nUIState == 0) {
			/* ���ص��ݵ���Ƭ */
			setVoToBillCard(m_nPresentRecord, null);
			/* ���ð�ť�߼� */
			setButtonsCard();
		} else {
			/* �������б��ͷ��ʾ����λ�� */
			m_nPresentRecord = 0;
			/* ��ø��ŵ��ݵı��� */
			try {
				m_VOs[m_nPresentRecord] = PrTool
						.getRefreshedVO(m_VOs[m_nPresentRecord]);
			} catch (Exception be) {
				if (be instanceof BusinessException)
					MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
							"SCMCOMMON", "UPPSCMCommon-000422")/* "ҵ���쳣" */,
							be.getMessage());
				showHintMessage(m_lanResTool.getStrByID("40040101",
						"UPP40040101-000065")/* "���ϵ��ݳɹ���ɺ���ص���ʱ���ִ���" */);
				return;
			}
			/* �����б����б�ͷ����һ�ŵ��ݱ��� */
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
				/* ��Ĭ����ʾΪ��һ�� */
				getBillListPanel().getHeadBillModel().setRowState(
						m_nPresentRecord, BillModel.SELECTED);
				getBillListPanel().getHeadTable().setRowSelectionInterval(
						m_nPresentRecord, m_nPresentRecord);
				setButtonsList();
				int headVOLength = headVO.length;
				/* ��ʾ��ע */
				for (int i = 0; i < headVOLength; i++) {
					getBillListPanel().getHeadBillModel().setValueAt(
							headVO[i].getVmemo(), i, "vmemo");
				}
				/* ��ʾ������Դ��Ϣ */
				PuTool.loadSourceInfoAll(getBillListPanel(),
						BillTypeConst.PO_PRAY);
			}
		}
		showHintMessage(m_lanResTool
				.getStrByID("common", "4004COMMON000000068")/* @res "���ϳɹ�" */);
	}

	/**
	 * ���� ���Ĺܹ��� ���õ���״̬����������������������б�
	 */
	private void onDocument() {
		String[] strPks = null;
		String[] strCodes = null;
		HashMap mapBtnPowerVo = new HashMap();
		Integer iBillStatus = null;
		//
		boolean isCard = getBillCardPanel().isDisplayable();
		/* ��Ϣ������ͬ���ݿ�Ƭ */
		if (!(getBillCardPanel().isDisplayable() || getBillListPanel()
				.isDisplayable())) {
			isCard = true;
		}
		// ��Ƭ
		if (isCard) {
			if (m_VOs != null && m_VOs.length > 0
					&& m_VOs[m_nPresentRecord] != null
					&& m_VOs[m_nPresentRecord].getParentVO() != null) {
				strPks = new String[] { (String) m_VOs[m_nPresentRecord]
						.getParentVO().getAttributeValue("cpraybillid") };
				strCodes = new String[] { (String) m_VOs[m_nPresentRecord]
						.getParentVO().getAttributeValue("vpraycode") };
				// �����ĵ������ɾ����ť�Ƿ����
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
		// �б�
		final boolean isList = getBillListPanel().isDisplayable();
		if (isList) {
			if (m_VOs != null && m_VOs.length > 0) {
				PraybillHeaderVO[] headers = null;
				headers = (PraybillHeaderVO[]) getBillListPanel()
						.getHeadBillModel().getBodySelectedVOs(
								PraybillHeaderVO.class.getName());
				if (headers == null || headers.length <= 0) {
					showHintMessage(m_lanResTool.getStrByID("40040101",
							"UPP40040101-000066")/* "û����ȷ��ȡ���ݺ�,���ܽ����ĵ�����" */);
					return;
				}
				strPks = new String[headers.length];
				strCodes = new String[headers.length];
				BtnPowerVO pVo = null;
				for (int i = 0; i < headers.length; i++) {
					strPks[i] = headers[i].getPrimaryKey();
					strCodes[i] = headers[i].getVpraycode();
					// �����ĵ������ɾ����ť�Ƿ����
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
					"UPP40040101-000066")/* "û����ȷ��ȡ���ݺ�,���ܽ����ĵ�����" */);
			return;
		}
		// �����ĵ�����Ի���
		nc.ui.scm.file.DocumentManager.showDM(this, strPks, strCodes,
				mapBtnPowerVo);
		showHintMessage(m_lanResTool
				.getStrByID("common", "4004COMMON000000025")/* @res "�ĵ�����ɹ�" */);
	}

	/**
	 * ��������:��ҳ
	 */
	private void onFirst() {
		/* ���ص��� */
		final int iRollBackPos = m_nPresentRecord;
		m_nPresentRecord = 0;
		setVoToBillCard(iRollBackPos, m_lanResTool.getStrByID("SCMCOMMON",
				"UPPSCMCommon-000248")/* "����" */);
		/* ���ð�ť�߼� */
		setButtonsCard();
		getBillCardPanel().setEnabled(false);
		showHintMessage(m_lanResTool
				.getStrByID("common", "4004COMMON000000026")/* @res "�ɹ���ʾ��ҳ" */);
	}

	/**
	 * ��������:����
	 */
	private void onInsertLine() {
		int nSelectedRow = getBillCardPanel().getBillTable().getSelectedRow();
		if (!getBillCardPanel().insertLine()) {
			MessageDialog.showHintDlg(this, m_lanResTool.getStrByID("40040101",
					"UPP40040101-000068")/* "����" */, m_lanResTool.getStrByID(
					"40040101", "UPP40040101-000059")/* "û��ѡ���빺�������У�" */);
			return;
		}
		/* ������Ŀ�Զ�Э�� */
		nc.ui.pu.pub.PuTool.setBodyProjectByHeadProject(getBillCardPanel(),
				"cprojectidhead", "cprojectid", "cprojectname",
				nc.vo.scm.pu.PuBillLineOprType.INSERT);
		/* �����к� */
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

		// ������ʾ czp 20050303 ����
		PraybillVO voCurr = (PraybillVO) getBillCardPanel().getBillValueVO(
				PraybillVO.class.getName(), PraybillHeaderVO.class.getName(),
				PraybillItemVO.class.getName());
		nc.ui.po.pub.InvAttrCellRenderer ficr = new nc.ui.po.pub.InvAttrCellRenderer();
		ficr.setFreeItemRenderer(getBillCardPanel(), voCurr);
		showHintMessage(m_lanResTool.getStrByID("common", "UCH038")/*
																	 * @res
																	 * "�����гɹ�"
																	 */);

	}

	/**
	 * ��������:ĩҳ
	 */
	private void onLast() {
		/* ���ص��� */
		final int iRollBackPos = m_nPresentRecord;
		m_nPresentRecord = m_VOs.length - 1;
		setVoToBillCard(iRollBackPos, m_lanResTool.getStrByID("SCMCOMMON",
				"UPPSCMCommon-000177")/* "ĩ��" */);
		/* ���ð�ť�߼� */
		setButtonsCard();
		getBillCardPanel().setEnabled(false);
		showHintMessage(m_lanResTool
				.getStrByID("common", "4004COMMON000000029")/* @res "�ɹ���ʾĩҳ" */);
	}

	/**
	 * ��������:��Ƭ���б�
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
		/* ���ز����ֶ� */
		/**
		 * v5 �����֯����ͷ��Ŀ����������ش���ɾ��
		 * getBillListPanel().hideHeadTableCol("cstoreorganization");
		 */
		getBillListPanel().hideHeadTableCol("cbiztype");
		getBillListPanel().hideHeadTableCol("cdeptid");
		getBillListPanel().hideHeadTableCol("cpraypsn");
		getBillListPanel().hideHeadTableCol("coperator");
		getBillListPanel().hideHeadTableCol("cauditpsn");
		getBillListPanel().hideHeadTableCol("ccustomerid");
		if (m_VOs != null && m_VOs.length > 0) {
			/* ��ʾ��Ƭ״̬�ĵ�ǰ���� */
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
					"UPP40040101-000458")/* "���۶���" */);
			m_comPraySource1.addItem(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000459")/* "��涩����" */);
			m_comPraySource1.addItem(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000460")/* "�ֹ�¼��" */);
			m_comPraySource1.addItem("DRP");
			m_comPraySource1.addItem(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000461")/* "��������" */);
			m_comPraySource1.addItem(m_lanResTool.getStrByID("4004pub",
					"UPP4004pub-000204") /* "������������" */);

			getBillListPanel().getBillListData().getHeadItem("ipraytype")
					.setWithIndex(true);
			m_comPrayType1 = (UIComboBox) getBillListPanel().getBillListData()
					.getHeadItem("ipraytype").getComponent();
			m_comPrayType1.removeAllItems();
			m_comPrayType1.setTranslate(true);
			m_comPrayType1.addItem(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000454")/* "����ߴ���" */);
			m_comPrayType1.addItem(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000455")/* "����߲�����" */);
			m_comPrayType1.addItem(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000456")/* "�ɹ�" */);
			m_comPrayType1.addItem(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000457")/* "��Э" */);

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
			/* �б���ʾ���ݴ��� */
			getBillListPanel().getHeadBillModel().setRowState(m_nPresentRecord,
					BillModel.SELECTED);
			getBillListPanel().getHeadTable().setRowSelectionInterval(
					m_nPresentRecord, m_nPresentRecord);
			/* ��ʾ��Դ��Ϣ���б� */
			PuTool.loadSourceInfoAll(getBillListPanel(), BillTypeConst.PO_PRAY);
			/* ��ʾ��ע */
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
		/* �б�ť�߼� */
		setButtonsList();
		m_nUIState = 1;
		getBillListPanel().setEnabled(false);
		showHintMessage(m_lanResTool.getStrByID("common", "UCH022")/*
																	 * @res
																	 * "�б���ʾ"
																	 */);
	}

	/**
	 * ��������:�б�״̬��ѯ
	 */
	private void onListQuery() {
		showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
				"UPPSCMCommon-000360")/* @res"��ʼ��ѯ..." */);
		// ��ʾ�Ի���
		getQueryDlg().showModal();
		if (getQueryDlg().isCloseOK()) {
			m_bQueried = true;
			onListRefresh();
		}

		showHintMessage(m_lanResTool.getStrByID("common", "UCH009")/*
																	 * @res
																	 * "��ѯ���"
																	 */);
	}

	/* �б�ˢ�� */
	private void onListRefresh() {
		// ��ȡ�빺����ѯ����
		ConditionVO[] conditionVO = getQueryDlg().getConditionVO();
		String strSubSql = getQueryDlg().getSubSQL();
		try {
			m_VOs = PraybillHelper.queryAll(getQueryDlg()
					.getSelectedCorpIdString(), conditionVO, getQueryDlg()
					.getStatusCndStr(), null, strSubSql);
		} catch (Exception e) {
			MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
					"40040101", "UPP40040101-000061")/* @res"�빺������" */, e
					.getMessage());
			return;
		}
		if (m_VOs != null && m_VOs.length > 0) {
			// ���û���λ��
			m_nPresentRecord = 0;
			try {
				m_VOs[m_nPresentRecord] = PrTool
						.getRefreshedVO(m_VOs[m_nPresentRecord]);
				// ����������
				new nc.ui.scm.pub.FreeVOParse().setFreeVO(
						m_VOs[m_nPresentRecord].getBodyVO(), "vfree", "vfree",
						"cbaseid", "cmangid", true);
			} catch (Exception be) {
				if (be instanceof BusinessException)
					MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
							"SCMCOMMON", "UPPSCMCommon-000422")/* "ҵ���쳣" */,
							be.getMessage());
				return;
			}
			// ���ز�ѯ���
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

			// ѡ���һ��
			getBillListPanel().getHeadBillModel().setBodyDataVO(hVO);
			getBillListPanel().getHeadBillModel().execLoadFormula();
			getBillListPanel().getHeadBillModel().updateValue();

			// �б���ʾ���ݴ���
			getBillListPanel().getHeadBillModel().setRowState(m_nPresentRecord,
					BillModel.SELECTED);
			getBillListPanel().getHeadTable().setRowSelectionInterval(
					m_nPresentRecord, m_nPresentRecord);
			Logger.debug("$$---$---$$:" + m_nPresentRecord);
			getBillListPanel().updateUI();

			// ��ʾ��ע
			for (int i = 0; i < hVO.length; i++) {
				getBillListPanel().getHeadBillModel().setValueAt(
						hVO[i].getVmemo(), i, "vmemo");
			}
		} else {
			m_VOs = null;
			// �޷��ز�ѯ���
			MessageDialog.showHintDlg(this, m_lanResTool.getStrByID("40040101",
					"UPP40040101-000055")/* "�빺����ѯ" */, m_lanResTool.getStrByID(
					"40040101", "UPP40040101-000056")/* "û�з����������빺����" */);
			// �������
			getBillListPanel().getHeadBillModel().clearBodyData();
			getBillListPanel().getBodyBillModel().clearBodyData();
			getBillListPanel().updateUI();
		}
		// �б�ť�߼�
		setButtonsList();
		m_nUIState = 1;
		updateButtonsAll();
		getBillCardPanel().setEnabled(false);

		showHintMessage(m_lanResTool.getStrByID("common", "UCH007")/*
																	 * @res
																	 * "ˢ�³ɹ�"
																	 */);
	}

	/**
	 * ����������
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
				.getStrByID("common", "4004COMMON000000019")/* @res "����ɹ�" */);
	}

	/**
	 * ��������:�������˵�
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
	 * ��������:����
	 */
	private void onModify(boolean bListShowing) {
		if (bListShowing) {
			// ���б�תΪ��Ƭ
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
						"common", "UC001-0000045")/* @res"�޸�" */, m_lanResTool
						.getStrByID("40040101", "UPP40040101-000446")/*
																	 * @res"û��ѡ���빺����ͷ�У�"
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
			// ��ʾ��Ƭ����
			getBillCardPanel().setBillValueVO(m_VOs[m_nPresentRecord]);
			nc.ui.pu.pub.PuTool.loadInvMaxPrice(getBillCardPanel());
			getBillCardPanel().getBillModel().execLoadFormula();
			// ������Դ������Ϣ
			nc.ui.pu.pub.PuTool.loadSourceInfoAll(getBillCardPanel(),
					nc.vo.scm.pu.BillTypeConst.PO_PRAY);
			getBillCardPanel().updateValue();
			getBillCardPanel().updateUI();
			// ��ʾ��ע
			UIRefPane nRefPanel3 = (UIRefPane) getBillCardPanel().getHeadItem(
					"vmemo").getComponent();
			nRefPanel3.setValue(m_VOs[m_nPresentRecord].getHeadVO().getVmemo());
		}
		// �Ѿ��������빺�������޸�
		int nBillStatus = 0;
		if (m_VOs[m_nPresentRecord].getHeadVO().getIbillstatus() != null) {
			nBillStatus = m_VOs[m_nPresentRecord].getHeadVO().getIbillstatus()
					.intValue();
		}
		if (nBillStatus == 3) {
			MessageDialog
					.showHintDlg(this, m_lanResTool.getStrByID("40040101",
							"UPP40040101-000467")/* "�빺���޸�" */,
							m_VOs[m_nPresentRecord].getHeadVO().getVpraycode()
									+ m_lanResTool.getStrByID("40040101",
											"UPP40040101-000468")/*
																 * @res "
																 * ���빺���Ѿ�����
																 * �������޸ģ�
																 * ��ˢ�½�������"
																 */);
			return;
		}
		if (nBillStatus == 1) {
			MessageDialog
					.showHintDlg(this, m_lanResTool.getStrByID("40040101",
							"UPP40040101-000467")/* "�빺���޸�" */,
							m_VOs[m_nPresentRecord].getHeadVO().getVpraycode()
									+ m_lanResTool.getStrByID("40040101",
											"UPP40040101-000469")/*
																 * @res "
																 * ���빺���Ѿ��ر�
																 * �������޸ģ�
																 * ��ˢ�½�������"
																 */);
			return;
		}
		getBillCardPanel().setEnabled(true);
		showHintMessage(m_lanResTool
				.getStrByID("common", "4004COMMON000000030")/* @res "�����޸�" */);
		// ***************fangy add 2002-12-04 begin****************/
		int rowCount = getBillCardPanel().getRowCount();
		for (int i = 0; i < rowCount; i++) {
			setVProduceNumEditState(i);
		}
		// ***************fangy add 2002-12-04 end******************/
		// �빺���Ų��ɱ༭
		// BillItem item = getBillCardPanel().getHeadItem("vpraycode");
		// item.setEnabled(false);
		// �޸��빺��ʱ����������Ӧ�ÿ����޸ġ�
		BillItem ddemanddate = getBillCardPanel().getBodyItem("ddemanddate");
		ddemanddate.setEnabled(true);

		// yux ��Ŀ�����޸�
		BillItem cprojectname = getBillCardPanel().getBodyItem("cprojectname");
		cprojectname.setEnabled(true);

		setPartNoEditable(getBillCardPanel());
		// setAssisUnitEditState();
		m_bEdit = true;
		m_bCancel = false;
		setButtonsCardModify();
		m_nUIState = 0;
		updateButtonsAll();

		/** �����Ҽ��˵��밴ť�顰�в�����Ȩ����ͬ */
		setPopMenuItemsEnable();
		// ���˴������,��֤��ҵ�����������д���ҵ������ʱ�滻�����������д�������

		// ȡҵ������
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
		// ���˴������
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
		// �ù�굽��ͷ��һ���ɱ༭��Ŀ
		getBillCardPanel().transferFocusTo(BillCardPanel.HEAD);
		showHintMessage(m_lanResTool
				.getStrByID("common", "4004COMMON000000030")/* @res "�����޸�" */);

		// ��־ƽ ������������������������ʾ����
		nc.ui.po.pub.InvAttrCellRenderer ficr = new nc.ui.po.pub.InvAttrCellRenderer();
		// �ӵ�ǰ����ȡֵ����Ϊ������������뻺�治һ�£�ficr.setFreeItemRenderer(getBillCardPanel(),
		// m_VOs[m_nPresentRecord]);
		PraybillVO voCurr = (PraybillVO) getBillCardPanel().getBillValueVO(
				PraybillVO.class.getName(), PraybillHeaderVO.class.getName(),
				PraybillItemVO.class.getName());
		ficr.setFreeItemRenderer(getBillCardPanel(), voCurr);
		getBillCardPanel().updateUI();
		// �ù�굽��ͷ��һ���ɱ༭��Ŀ
		getBillCardPanel().transferFocusTo(BillCardPanel.HEAD);
		//
		showHintMessage(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000534")/* @res "�༭����" */);
	}

	/**
	 * ���ߣ���־ƽ ���ܣ����õ��ݿ�Ƭ�Ҽ��˵��в����밴ť�顰�в�����Ȩ����ͬ �������� ���أ��� ���⣺�� ���ڣ�(2004-11-26
	 * 10:06:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */

	private void setPopMenuItemsEnable() {
		// û�з����в���Ȩ��
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
		// �����в���Ȩ��
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
			// ճ������β��ճ���������߼���ͬ
			getBillCardPanel().getBodyPanel().getMiPasteLineToTail()
					.setEnabled(m_btnPstLine.isPower());

		}
	}

	/**
	 * ��������:��ҳ
	 */
	private void onNext() {
		/* ���ص��� */
		final int iRollBackPos = m_nPresentRecord;
		m_nPresentRecord++;
		setVoToBillCard(iRollBackPos, m_lanResTool.getStrByID("SCMCOMMON",
				"UPPSCMCommon-000281")/* "����" */);
		/* ���ð�ť�߼� */
		setButtonsCard();
		getBillCardPanel().setEnabled(false);
		//
		showHintMessage(m_lanResTool
				.getStrByID("common", "4004COMMON000000028")/* @res "�ɹ���ʾ��һҳ" */);

	}

	/**
	 * ��������:�򿪵�ǰ���빺��--�б�
	 */
	private void onOpenList() {
		this.showHintMessage(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000069")/* "���ڴ򿪵���..." */);
		m_VOs[m_nPresentRecord].getHeadVO().setCuserid(
				getClientEnvironment().getUser().getPrimaryKey());
		long tTime = System.currentTimeMillis();
		try {
			// ������Ա
			String strOpr = getClientEnvironment().getUser().getPrimaryKey();
			m_VOs[m_nPresentRecord].setCurrOperator(strOpr);
			ArrayList keys = (ArrayList) PfUtilClient
					.processActionNoSendMessage(this, "OPEN",
							nc.vo.scm.pu.BillTypeConst.PO_PRAY,
							getClientEnvironment().getDate().toString(),
							m_VOs[m_nPresentRecord], null, null, null);
			m_VOs[m_nPresentRecord] = (PraybillVO) keys.get(0);
			// �򿪵���ʱ�ָ�������״̬
			m_VOs[m_nPresentRecord].getHeadVO().setIbillstatus(new Integer(3));
			// ���ݹرպ�����
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
					"40040101", "UPP40040101-000070")/* "�빺����" */, e
					.getMessage());
			showHintMessage(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000071")/* "��ʧ��" */);
			return;
		}
		tTime = System.currentTimeMillis() - tTime;
		Logger.debug("�빺����ʱ�䣺" + tTime + " ms!");
		m_nUIState = 0;
		//
		getBillListPanel().getHeadBillModel().setBodyRowVO(
				m_VOs[m_nPresentRecord].getHeadVO(), m_nPresentRecord);
		getBillListPanel().getBodyBillModel().setBodyDataVO(
				m_VOs[m_nPresentRecord].getBodyVO());
		updateUI();
		/* ˢ�°�ť״̬ */
		setButtonsList();
		//
		showHintMessage(m_lanResTool.getStrByID("common", "UCH012")/*
																	 * @res
																	 * "�Ѵ�"
																	 */);
		return;
	}

	/**
	 * ��������:�򿪵�ǰ���빺��
	 */
	private void onOpenCard() {
		this.showHintMessage(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000069")/* "���ڴ򿪵���..." */);
		m_VOs[m_nPresentRecord].getHeadVO().setCuserid(
				getClientEnvironment().getUser().getPrimaryKey());
		long tTime = System.currentTimeMillis();
		try {
			// ������Ա
			String strOpr = getClientEnvironment().getUser().getPrimaryKey();
			m_VOs[m_nPresentRecord].setCurrOperator(strOpr);
			ArrayList keys = (ArrayList) PfUtilClient
					.processActionNoSendMessage(this, "OPEN",
							nc.vo.scm.pu.BillTypeConst.PO_PRAY,
							getClientEnvironment().getDate().toString(),
							m_VOs[m_nPresentRecord], null, null, null);
			m_VOs[m_nPresentRecord] = (PraybillVO) keys.get(0);
			// �򿪵���ʱ�ָ�������״̬
			m_VOs[m_nPresentRecord].getHeadVO().setIbillstatus(new Integer(3));
			// ���ݹرպ�����
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
					"40040101", "UPP40040101-000070")/* "�빺����" */, e
					.getMessage());
			showHintMessage(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000071")/* "��ʧ��" */);
			return;
		}
		tTime = System.currentTimeMillis() - tTime;
		Logger.debug("�빺����ʱ�䣺" + tTime + " ms!");
		m_nUIState = 0;
		/* ˢ�°�ť״̬ */
		setButtonsCard();
		/* ���ص��� */
		setVoToBillCard(m_nPresentRecord, m_lanResTool.getStrByID("40040101",
				"UPP40040101-000449")/* "����" */);
		//
		showHintMessage(m_lanResTool.getStrByID("common", "UCH012")/*
																	 * @res
																	 * "�Ѵ�"
																	 */);
		return;
	}

	/**
	 * ��������:ճ����
	 */
	private void onPasteLine() {
		/* ճ��ǰ������ */
		final int iOrgRowCount = getBillCardPanel().getRowCount();
		getBillCardPanel().pasteLine();

		/* ���ӵ����� */
		final int iPastedRowCount = getBillCardPanel().getRowCount()
				- iOrgRowCount;

		/* �����к� */
		BillRowNo.pasteLineRowNo(getBillCardPanel(),
				nc.vo.scm.pu.BillTypeConst.PO_PRAY, "crowno", iPastedRowCount);

		final int iEndRow = getBillCardPanel().getBillTable().getSelectedRow() - 1;
		final int iBeginRow = iEndRow - iPastedRowCount + 1;
		/* �������ʱ����Ҫ����Ϣ */
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
		// ������ʾ czp 20050303 ����
		PraybillVO voCurr = (PraybillVO) getBillCardPanel().getBillValueVO(
				PraybillVO.class.getName(), PraybillHeaderVO.class.getName(),
				PraybillItemVO.class.getName());
		nc.ui.po.pub.InvAttrCellRenderer ficr = new nc.ui.po.pub.InvAttrCellRenderer();
		ficr.setFreeItemRenderer(getBillCardPanel(), voCurr);
		showHintMessage(m_lanResTool.getStrByID("common", "UCH040")/*
																	 * @res
																	 * "ճ���гɹ�"
																	 */);

	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-11-10 11:19:18)
	 */
	private void onPasteLineToTail() {
		final int iOldRowCnt = getBillCardPanel().getRowCount();
		getBillCardPanel().pasteLineToTail();
		final int iNewRowCnt = getBillCardPanel().getRowCount();
		if (iOldRowCnt > 0 && iNewRowCnt > 0 && iOldRowCnt == iNewRowCnt)
			showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
					"UPPSCMCommon-000424")/* "ճ���е���βδ�ɹ�,����ԭ��û�п������ݻ�δȷ��Ҫճ����λ��" */);
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
			// �����к�
			BillRowNo.addLineRowNos(getBillCardPanel(),
					nc.vo.scm.pu.BillTypeConst.PO_PRAY, "crowno", iNewRowCnt
							- iOldRowCnt);
		}
		// ������ʾ czp 20050303 ����
		PraybillVO voCurr = (PraybillVO) getBillCardPanel().getBillValueVO(
				PraybillVO.class.getName(), PraybillHeaderVO.class.getName(),
				PraybillItemVO.class.getName());
		nc.ui.po.pub.InvAttrCellRenderer ficr = new nc.ui.po.pub.InvAttrCellRenderer();
		ficr.setFreeItemRenderer(getBillCardPanel(), voCurr);
		showHintMessage(m_lanResTool.getStrByID("common", "UCH040")/*
																	 * @res
																	 * "ճ���гɹ�"
																	 */);
	}

	/**
	 * ��������:��ҳ
	 */
	private void onPrevious() {
		/* ���ص��� */
		final int iRollBackPos = m_nPresentRecord;
		m_nPresentRecord--;
		setVoToBillCard(iRollBackPos, m_lanResTool.getStrByID("SCMCOMMON",
				"UPPSCMCommon-000232")/* "����" */);
		/* ���ð�ť�߼� */
		setButtonsCard();
		getBillCardPanel().setEnabled(false);
		showHintMessage(m_lanResTool
				.getStrByID("common", "4004COMMON000000027")/* @res "�ɹ���ʾ��һҳ" */);
	}

	/**
	 * ���ܣ��۸���֤�� ������ ���أ� ���⣺ ���ڣ�(2002-9-23 11:49:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	private void onPriceInfos() {
		// ��ʾ��ѯ�Ի���
		if (m_priceDlg == null) {
			m_priceDlg = new QueryConditionClient();
			m_priceDlg.setTempletID("40040103000000000000");
			m_priceDlg.hideNormal();
		}
		m_priceDlg.showModal();
		// ��ѯ
		PriceInfosVO[] vos = null;
		if (m_priceDlg.isCloseOK()) {
			String wherePart = m_priceDlg.getWhereSQL();
			// ȡ�ô����Ϣ
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
						"SCMCOMMON", "UPPSCMCommon-000270")/* "��ʾ" */, e
						.getMessage());
			}
			Hashtable table = new Hashtable();
			if (vos != null && vos.length > 0) {
				// ����۸�Ϊ�ջ�С�ڵ���0���򽫹�Ӧ�����
				int vosLength = vos.length;
				for (int i = 0; i < vosLength; i++) {
					if (vos[i].getQuota1() == null
							|| vos[i].getQuota1().doubleValue() <= 0) {
						vos[i].setVendor1(null);
						vos[i].setQuota1(null);
					}
				}
			}
			// ����ǰ���ݴ��ڶ����ͬ�������۸���֤Ҳ�ֶ�����ʾ��
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
			// �������¼�
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
			/* ��ʾ�۸���֤�� */
			PriceInfoDlg dlgPriceInfo = new PriceInfoDlg(this, vos,
					nPriceDecimal, getClientEnvironment());
			dlgPriceInfo.showModal();
			showHintMessage(m_lanResTool.getStrByID("common",
					"4004COMMON000000031")/* @res "�ɹ���ʾ�۸���֤��" */);
		}
	}

	/**
	 * ��ѯ��ǰ��������״̬
	 */

	private void onQueryForAudit() {
		PraybillVO[] vos = m_VOs;
		// ��ǰ������ڵ���
		if (vos != null && vos.length > 0) {
			// ����õ��ݴ�����������״̬��ִ��������䣺
			nc.ui.pub.workflownote.FlowStateDlg approvestatedlg = new nc.ui.pub.workflownote.FlowStateDlg(
					this, "20", vos[m_nPresentRecord].getHeadVO()
							.getPrimaryKey());
			approvestatedlg.showModal();

			showHintMessage(m_lanResTool.getStrByID("common", "UCH035")/*
																		 * @res
																		 * "����״̬��ѯ�ɹ�"
																		 */);
		} else {
			showHintMessage(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000539")/* @res "����������" */);
		}
	}

	/**
	 * ���ܣ�������ѯ ������(2002-10-31 19:45:39) �޸ģ�2003-04-21/czp/ͳһ�����۶Ի���
	 */
	private void onQueryInvOnHand() {
		PraybillVO voPara = null;
		PraybillItemVO item = null;
		PraybillItemVO[] items = null;
		/* ��Ƭ */
		if (getBillCardPanel().isVisible()) {
			voPara = (PraybillVO) getBillCardPanel().getBillValueVO(
					PraybillVO.class.getName(),
					PraybillHeaderVO.class.getName(),
					PraybillItemVO.class.getName());
			if (voPara == null || voPara.getParentVO() == null) {
				showHintMessage(m_lanResTool.getStrByID("40040101",
						"UPP40040101-000073")/* "δѡȡ����,���ܲ�ѯ����" */);
				return;
			}
			/* ��������ʱ���õ�ǰ��¼��˾ */
			if (voPara.getParentVO().getAttributeValue("pk_corp") == null
					|| voPara.getParentVO().getAttributeValue("pk_corp")
							.toString().trim().equals("")) {
				voPara.getParentVO().setAttributeValue("pk_corp",
						m_sLoginCorpId);
			}
			/* ������Ϣ�����Լ�� */
			int[] iSelRows = getBillCardPanel().getBillTable()
					.getSelectedRows();
			if (iSelRows != null && iSelRows.length > 0) {
				/* �õ��û�ѡȡ�ĵ�һ�� */
				item = (PraybillItemVO) getBillCardPanel().getBillModel()
						.getBodyValueRowVO(iSelRows[0],
								PraybillItemVO.class.getName());
			} else {
				/* �û�δѡ��ʱ��ȡ���ݵ�һ�� */
				items = (PraybillItemVO[]) getBillCardPanel().getBillModel()
						.getBodyValueVOs(PraybillItemVO.class.getName());
				if (items == null || items.length <= 0) {
					showHintMessage(m_lanResTool.getStrByID("40040101",
							"UPP40040101-000074")/* "��˾�����������������Ϣ������,���ܲ�ѯ����" */);
					return;
				}
				item = items[0];
			}
			/* ��Ϣ�����Լ�� */
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
						"UPP40040101-000074")/* "��˾�����������������Ϣ������,���ܲ�ѯ����" */);
				return;
			}
			/* �����VO��ʼ�������ô�����ѯ�Ի��� */
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
						Logger.debug("δ��ѯ����Ȩ�޹�˾��ֱ�ӷ���!");
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
											"UPP40040101-000075")/* "��ȡ��Ȩ�޹�˾�쳣" */,
									m_lanResTool.getStrByID("40040101",
											"UPP40040101-000076")/* "��ȡ��Ȩ�޹�˾ʱ�����쳣(��ϸ��Ϣ�μ�����̨��־)!" */);
					return;
				}
			}
			getAtpDlg().setPkCorps(saPkCorp);
			getAtpDlg().initData(voPara);
			getAtpDlg().showModal();
		}
		/* �б� */
		else if (getBillListPanel().isVisible()) {
			/* ��ͷ��Ϣ�����Լ�� */
			PraybillHeaderVO head = null;
			if (getBillListPanel().getHeadBillModel().getBodySelectedVOs(
					PraybillHeaderVO.class.getName()) == null
					|| getBillListPanel().getHeadBillModel()
							.getBodySelectedVOs(
									PraybillHeaderVO.class.getName()).length <= 0) {
				showHintMessage(m_lanResTool.getStrByID("40040101",
						"UPP40040101-000073")/* "δѡȡ����,���ܲ�ѯ����" */);
				return;
			}
			head = (PraybillHeaderVO) getBillListPanel().getHeadBillModel()
					.getBodySelectedVOs(PraybillHeaderVO.class.getName())[0];
			if (head == null
					|| head.getAttributeValue("pk_corp") == null
					|| head.getAttributeValue("pk_corp").toString().trim()
							.equals("")) {
				showHintMessage(m_lanResTool.getStrByID("40040101",
						"UPP40040101-000077")/* "δ��ȷ��˾,���ܲ�ѯ����" */);
				return;
			}
			/* ������Ϣ�����Լ�� */
			int[] iSelRows = getBillListPanel().getBodyTable()
					.getSelectedRows();
			if (iSelRows != null && iSelRows.length > 0) {
				/* �õ��û�ѡȡ�ĵ�һ�� */
				item = (PraybillItemVO) getBillListPanel().getBodyBillModel()
						.getBodyValueRowVO(iSelRows[0],
								PraybillItemVO.class.getName());
			} else {
				/* �û�δѡ��ʱ��ȡ���ݵ�һ�� */
				items = (PraybillItemVO[]) getBillListPanel()
						.getBodyBillModel().getBodyValueVOs(
								PraybillItemVO.class.getName());
				if (items == null || items.length <= 0) {
					showHintMessage(m_lanResTool.getStrByID("40040101",
							"UPP40040101-000074")/* "��˾�����������������Ϣ������,���ܲ�ѯ����" */);
					return;
				}
				item = items[0];
			}
			/* ��Ϣ�����Լ�� */
			if (item.getAttributeValue("cinventoryid") == null
					|| item.getAttributeValue("cinventoryid").toString().trim()
							.equals("")
					|| item.getAttributeValue("ddemanddate") == null
					|| item.getAttributeValue("ddemanddate").toString().trim()
							.equals("")) {
				showHintMessage(m_lanResTool.getStrByID("40040101",
						"UPP40040101-000074")/* "��˾�����������������Ϣ������,���ܲ�ѯ����" */);
				return;
			}
			/* �����VO��ʼ�������ô�����ѯ�Ի��� */
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
						Logger.debug("δ��ѯ����Ȩ�޹�˾��ֱ�ӷ���!");
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
											"UPP40040101-000075")/* "��ȡ��Ȩ�޹�˾�쳣" */,
									m_lanResTool.getStrByID("40040101",
											"UPP40040101-000076")/* "��ȡ��Ȩ�޹�˾ʱ�����쳣(��ϸ��Ϣ�μ�����̨��־)!" */);
					Logger.debug(e.getMessage());
					return;
				}
			}
			getAtpDlg().setPkCorps(saPkCorp);
			getAtpDlg().initData(voPara);
			getAtpDlg().showModal();
			showHintMessage(m_lanResTool.getStrByID("common",
					"4004COMMON000000032")/* @res "������ѯ���" */);
		}
	}

	/*
	 * ��������:����
	 */
	private boolean onSave() {
		showHintMessage(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000078")/* "���濪ʼ..." */);
		nc.vo.scm.pu.Timer timer = new nc.vo.scm.pu.Timer();
		timer.start("�빺���������onSave������ʼ");

		// ��ֹ�༭
		getBillCardPanel().stopEditing();
		// ���ǿ���
		filterNullLine();

		// ���Ӷ�У�鹫ʽ��֧��,������ʾ��UAP���� since v501
		if (!getBillCardPanel().getBillData().execValidateFormulas()) {
			return false;
		}

		final int nRow = getBillCardPanel().getRowCount();
		PraybillVO VO = new PraybillVO(nRow);
		m_SaveVOs = new PraybillVO(nRow);
		getBillCardPanel().getBillValueVO(VO);
		// Ϊ�������͸�ֵ
		String Cbiztype = VO.getHeadVO().getCbiztype();
		if (Cbiztype == null) {
			if (m_bizButton.getTag() != null
					&& m_bizButton.getTag().trim() != "") {
				VO.getHeadVO().setCbiztype(m_bizButton.getTag());
			}
		}
		// ����ǰ��Ч�Լ��
		if (!checkBeforeSave(VO))
			return false;
		if (!checkPraytype(VO.getBodyVO()))
			return false;

		timer.addExecutePhase("����ǰ��Ч�Լ��");

		if (m_bAdd) {

			// �����빺��(���������빺��)
			PraybillHeaderVO headVO = VO.getHeadVO();
			// ��ͷ����
			headVO.setNversion(new Integer(1));

			headVO.setPk_corp(m_sLoginCorpId);
			headVO.setIpraysource(new Integer(m_comPraySource
					.getSelectedIndex()));
			headVO.setIpraytype(new Integer(m_comPrayType.getSelectedIndex()));
			headVO.setCaccountyear(getClientEnvironment().getAccountYear());
			headVO.setIbillstatus(new Integer(0));

			headVO.setCauditpsn(null);
			headVO.setDauditdate(null);

			// �Ƶ���

			headVO.setCoperator(getClientEnvironment().getUser()
					.getPrimaryKey());
			if (m_bizButton != null)
				headVO.setCbiztype(m_bizButton.getTag());
			// ����ʱ�����⴦��
			if (isFrmCopy) {
				headVO.setCbiztype(((UIRefPane) getBillCardPanel().getHeadItem(
						"cbiztype").getComponent()).getRefModel().getPkValue());
			}
			try {
				// ����ʱ�õ�ǰ����ԱΪ�Ƶ���
				headVO.setCoperator(getClientEnvironment().getUser()
						.getPrimaryKey());
			} catch (Exception e) {
				Logger.debug(e.getMessage());
				MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
						"SCMCOMMON", "UPPSCMCommon-000355")/* "��ͷ����" */, e
						.getMessage());
				showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
						"UPPSCMCommon-000010")/* "����ʧ��" */);
				return false;
			}
			UIRefPane nRefPanel = (UIRefPane) getBillCardPanel().getHeadItem(
					"vmemo").getComponent();
			UITextField vMemoField = nRefPanel.getUITextField();
			headVO.setVmemo(vMemoField.getText());
			// ��������
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
			// ������ʱVO
			m_SaveVOs.setChildrenVO(bodyVO);

		} else {
			// �޸��빺��
			PraybillHeaderVO headVO = VO.getHeadVO();
			Logger.debug("BCoperator=" + headVO.getCoperator());
			// �ӻ����������Ƶ���
			if (m_VOs != null && m_VOs[m_nPresentRecord] != null
					&& m_VOs[m_nPresentRecord].getHeadVO() != null
					&& !isAllowedModifyByOther) {
				headVO.setCoperator(getClientEnvironment().getUser()
						.getPrimaryKey());
			}
			Logger.debug("Coperator=" + headVO.getCoperator());
			Logger.debug("isAllowedModifyByOther=" + isAllowedModifyByOther);
			// ��ͷ����
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
			// ��������
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
			// ���Ʊ���
			Vector vData = new Vector();
			for (int i = 0; i < bodyVO.length; i++) {
				vData.addElement(bodyVO[i]);
			}
			// ����ɾ���ı���
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
				v[i].setBodyEditStatus(3); // ����ɾ��״̬
			}
			// ����ɾ�������б�������Ϊ��ǰ�ı���
			VO.setChildrenVO(v);
			// ������ʱVO
			m_SaveVOs.setChildrenVO(VO.getChildrenVO());
		}

		ArrayList arrReturnFromBs = null;
		try {
			/* �Ƿ���Ҫ���˵��ݺ�:�������ֹ�¼�뵥�ݺ� */
			if (m_bAdd) {
				Object sPraybillCode = VO.getParentVO().getAttributeValue(
						"vpraycode");
				if (VO.getParentVO() != null && sPraybillCode != null
						&& sPraybillCode.toString().trim().length() > 0) {
				}
			}
			/* ���� */
			/* ������Ա(�������������޸�) */
			String strOpr = getClientEnvironment().getUser().getPrimaryKey();
			VO.setCurrOperator(strOpr);
			VO.getHeadVO().setCuserid(strOpr);
			/* ���õ���״̬Ϊ���� */
			VO.getHeadVO().setIbillstatus(new Integer(0));
			m_SaveVOs.setParentVO(VO.getParentVO());
			/* ֧�ֹ�Ӧ�̺�׼��� */
			ArrayList aryUserObj = new ArrayList();
			aryUserObj.add(new Integer(1));
			aryUserObj.add("cvendormangid");
			timer.addExecutePhase("����ǰ��׼������");

			arrReturnFromBs = (ArrayList) PfUtilClient
					.processActionNoSendMessage(this, "SAVEBASE",
							nc.vo.scm.pu.BillTypeConst.PO_PRAY,
							getClientEnvironment().getDate().toString(), VO,
							aryUserObj, null, null);

			timer.addExecutePhase("ִ��SAVEBASE�Ų�����");

			// �õ�����
			if (arrReturnFromBs == null || arrReturnFromBs.size() == 0) {
				showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
						"UPPSCMCommon-000010")/* "����ʧ��" */);
				return false;
			}

			timer.addExecutePhase("�������");

			// ��־ƽ ������������������������ʾ����
			nc.ui.po.pub.InvAttrCellRenderer ficr = new nc.ui.po.pub.InvAttrCellRenderer();
			ficr.setFreeItemRenderer(getBillCardPanel(), null);

			// V50������ɹ������ô˱�־
			isFrmCopy = false;
			//
			showHintMessage(m_lanResTool.getStrByID("common", "UCH005")/*
																		 * @res"����ɹ�"
																		 */);
		} catch (Exception e) {
			SCMEnv.out(e);
			if (e instanceof BusinessException) {
				showHintMessage(m_lanResTool.getStrByID("40040101",
						"UPP40040101-000080")/* @res "����ʧ�ܣ�" */);
				MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
						"40040101", "UPP40040101-000081")/* @res "�빺������" */, e
						.getMessage());
			} else {
				showHintMessage(m_lanResTool.getStrByID("40040101",
						"UPP40040101-000080")/* @res "����ʧ�ܣ�" */);
				MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
						"40040101", "UPP40040101-000081")/* @res "�빺������" */, e
						.getMessage());
			}
			return false;
		}

		// ��̨����ֵ����
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
			// ����ID��TS��Ӧ��
			HashMap<String, String> mapBidBts = new HashMap<String, String>();
			for (int i = 1; i < ts.length; i++) {
				mapBidBts.put(ts[i].substring(0, 20), ts[i].substring(20, 39));
			}
			int iPos = 1;
			// ��BS������Э�飺��֤�빺�������ı����������� arrReturn �������ڶ�������...��Ԫ��
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
			// ��������������������������������������ʱ�Ƶ��˿���ֱ���������ݣ���ʱ��Ҫˢ����ʾ
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
		// // �ֹ�����Ƭ����(�ָ������������������Ϊ������)
		// if (iSortCol >= 0) {
		// getBillCardPanel().getBillModel().sortByColumn(iSortCol, bSortAsc);
		// }
		timer.addExecutePhase("��������ʾ����");
		timer.showAllExecutePhase("�빺�������������");

		showHintMessage(m_lanResTool.getStrByID("common", "UCH005")/*
																	 * @res
																	 * "����ɹ�"
																	 */);

		return true;
	}

	/**
	 * ��������:ȫѡ
	 */
	private void onSelectAll() {
		final int iLen = getBillListPanel().getHeadBillModel().getRowCount();
		if (iLen <= 0) {
			showHintMessage(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000541")/* @res "������ѡ��" */);
			return;
		}
		getBillListPanel().getHeadTable().setRowSelectionInterval(0, iLen - 1);
		for (int i = 0; i < iLen; i++) {
			getBillListPanel().getHeadBillModel().setRowState(i,
					BillModel.SELECTED);
		}
		/* ���ð�ť�߼� */
		setButtonsList();
		showHintMessage(m_lanResTool
				.getStrByID("common", "4004COMMON000000033")/* @res "ȫ��ѡ�гɹ�" */);
	}

	/**
	 * ��������:ȫ��
	 */
	private void onSelectNo() {
		final int iLen = getBillListPanel().getHeadBillModel().getRowCount();
		if (iLen <= 0) {
			showHintMessage(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000541")/* @res "������ѡ��" */);
			return;
		}
		getBillListPanel().getHeadTable().removeRowSelectionInterval(0,
				iLen - 1);
		for (int i = 0; i < iLen; i++) {
			getBillListPanel().getHeadBillModel().setRowState(i,
					BillModel.NORMAL);
		}
		/* ���ð�ť�߼� */
		setButtonsList();
		showHintMessage(m_lanResTool
				.getStrByID("common", "4004COMMON000000034")/* @res "ȫ��ȡ���ɹ�" */);
	}

	/**
	 * ����:�б���Ƭ
	 */
	private void onCard() {

		// ���빺��
		if (m_VOs == null || m_VOs.length == 0) {
			getBillListPanel().setVisible(false);
			getBillCardPanel().setVisible(true);
			getBillCardPanel().getBillData().clearViewData();
			setButtonsCard();
			m_nUIState = 0;
			showHintMessage(m_lanResTool.getStrByID("common", "UCH021")/*
																		 * @res
																		 * "��Ƭ��ʾ"
																		 */);
			return;
		}
		// ���빺��
		if (m_nPresentRecord >= 0) {
			int iSortCol = getBillListPanel().getBodyBillModel()
					.getSortColumn();
			boolean bSortAsc = getBillListPanel().getBodyBillModel()
					.isSortAscending();
			/* ���ص��ݿ�Ƭ�ؼ� */
			getBillListPanel().setVisible(false);
			getBillCardPanel().setVisible(true);
			m_nUIState = 0;
			/* ���ص���VO����Ƭ */
			setVoToBillCard(m_nPresentRecord, null);
			// �ֹ�����Ƭ����(ͬ���б����������)
			if (iSortCol >= 0) {
				getBillCardPanel().getBillModel().sortByColumn(iSortCol,
						bSortAsc);
			}
			/* ���ð�ť�߼� */
			setButtonsCard();
		}
		getBillCardPanel().execHeadTailLoadFormulas();
		getBillCardPanel().setEnabled(false);
		showHintMessage(m_lanResTool.getStrByID("common", "UCH021")/*
																	 * @res
																	 * "��Ƭ��ʾ"
																	 */);
	}

	/**
	 * �������ڣ� 2005-9-20 ���������� ������չ��ť����Ƭ����
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
	 * ����:ִ������
	 */
	private void onUnAudit() {
		try {
			nc.vo.scm.pu.Timer timer = new nc.vo.scm.pu.Timer();
			timer.start("�빺���������onUnAudit������ʼ");
			String[] sPraybillIds = new String[1];
			showHintMessage(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000082")/*
										 * @res "��������..."
										 */);
			PraybillVO vo = m_VOs[m_nPresentRecord];

			/* ---------------- added v31sp1 zhongwei �޶����빺���������� */
			if (vo.getHeadVO().getNversion().doubleValue() > 1)
				throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("40040101", "UPP40040101-000527", null,
								new String[] { vo.getHeadVO().getVpraycode() })/*
																				 * @res
																				 * "�޶����빺����������!\n
																				 * ���ݺ�
																				 * ��
																				 * {
																				 * 0
																				 * }
																				 * "
																				 */);
			/* ---------------- added v31sp1 zhongwei �޶����빺���������� */

			vo.getHeadVO().setCuserid(
					getClientEnvironment().getUser().getPrimaryKey());
			sPraybillIds[0] = vo.getHeadVO().getCpraybillid();
			// ���������
			vo.getHeadVO().setCauditpsn(
					getClientEnvironment().getUser().getPrimaryKey());

			timer.addExecutePhase("����ǰ׼������");
			/* ���� */
			PfUtilClient.processBatchFlow(null, "UNAPPROVE"
					+ getClientEnvironment().getUser().getPrimaryKey(),
					nc.vo.scm.pu.BillTypeConst.PO_PRAY, getClientEnvironment()
							.getDate().toString(), new PraybillVO[] { vo },
					null);
			if (!PfUtilClient.isSuccess()) {
				showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
						"UPPSCMCommon-000409")/*
											 * @res "����δ�ɹ�"
											 */);
				return;
			}
			timer.addExecutePhase("ִ��UNAPPROVE�ű�����");

			/* ����ɹ���ˢ�� */
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
			
			
			//lcq add by 2015/5/29  �Ƴ���ע
			((PraybillHeaderVO) vo.getParentVO()).setVmemo("");
			; 
			String sql="UPDATE po_praybill SET Vmemo=' ' WHERE cpraybillid='"+sPraybillIds[0]+"'";
			IPubDMO getdmo = (IPubDMO) NCLocator.getInstance().lookup(IPubDMO.class.getName());
			getdmo.executeUpdate(sql);
			//lcq end �Ƴ���ע

			timer.addExecutePhase("����ɹ���ˢ��");
			m_VOs[m_nPresentRecord] = vo;
			/* ˢ�°�ť״̬ */
			setButtonsCard();
			/* ���ص��� */
			setVoToBillCard(m_nPresentRecord, m_lanResTool.getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000410")/*
														 * @res "������"
														 */);

			timer.addExecutePhase("�������ʾ");
			timer.showAllExecutePhase("�빺���������onUnAudit��������");
			getBillCardPanel().setEnabled(false);
			showHintMessage(m_lanResTool.getStrByID("common", "UCH011")/*
																		 * @res
																		 * "����ɹ�"
																		 */);
		} catch (Exception e) {
			PuTool.outException(this, e);
			showHintMessage(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000083")/*
										 * @res "�����쳣,����ʧ��"
										 */);
			SCMEnv.out(e);
		}
	}

	/**
	 * ����ִ�ж����鰴ť�߼�
	 */
	private void refreshActionButtons1() {
		int m_btnActionLength = m_btnActions.getChildButtonGroup().length;
		// ִ�а�ť�鴦��
		if (m_btnActions != null && m_btnActions.getChildButtonGroup() != null
				&& m_btnActions.getChildButtonGroup().length > 0) {
			// ��ִ�ж�����
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
			// ��ִ�ж�����
			m_btnActions.setEnabled(false);
			m_btnActions.setChildButtonGroup(null);
		}
		updateButton(m_btnActions);
		for (int i = 0; i < m_btnActionLength; i++) {
			updateButton(m_btnActions.getChildButtonGroup()[i]);
		}
	}

	/**
	 * �������а�ť�Ƿ���á�
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * ʹ��ʾ��
	 * <p>
	 * <b>����˵��</b>
	 * 
	 * @param bEnabled
	 *            <p>
	 * @author czp
	 * @time 2007-3-13 ����01:04:45
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
	 * ���ܣ���Ƭ�����ť�߼�
	 */
	private void setButtonsCard() {
		//
		setButtonsEnabled(true);
		//
		dealBtnsBeforeCardShowing();

		/* ���桢ȡ�����в����� */
		m_btnSave.setEnabled(false);
		m_btnCancel.setEnabled(false);
		m_btnLines.setEnabled(false);
		if (m_btnLines.getChildCount() > 0) {
			for (int i = 0; i < m_btnLines.getChildButtonGroup().length; i++) {
				m_btnLines.getChildButtonGroup()[i].setEnabled(false);
			}
		}
		boolean bHaveRecs = (m_VOs != null && m_VOs.length > 0);
		// ����
		m_btnCopy.setEnabled(bHaveRecs);
		/* ��ҳ */
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
		// ��������
		m_btnDocument.setEnabled(bHaveRecs);
		// ������ѯ
		m_btnWorkFlowBrowse.setEnabled(bHaveRecs);
		m_btnUsable.setEnabled(bHaveRecs);
		m_btnPrint.setEnabled(bHaveRecs);
		m_btnPrintPreview.setEnabled(bHaveRecs);
		m_btnCombin.setEnabled(bHaveRecs);
		m_btnLinkBillsBrowse.setEnabled(bHaveRecs);
		m_btnPriceInfo.setEnabled(bHaveRecs);

		/* ���󡢹رա��򿪡��޸ġ����ϡ����������� */
		int iBillStatus = -1;
		if (bHaveRecs && m_VOs[m_nPresentRecord] != null
				&& m_VOs[m_nPresentRecord].getHeadVO().getIbillstatus() != null) {
			iBillStatus = m_VOs[m_nPresentRecord].getHeadVO().getIbillstatus()
					.intValue();
		}
		// ����
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
		} else if (iBillStatus == 1) {// �Ѿ��ر�
			m_btnSendAudit.setEnabled(false);
			m_btnClose.setEnabled(false);
			m_btnModify.setEnabled(false);
			m_btnDiscard.setEnabled(false);
			m_btnApprove.setEnabled(false);
			m_btnUnApprove.setEnabled(false);
		} else if (iBillStatus == 2) {/* �빺���������� */
			m_btnSendAudit.setEnabled(false);
			m_btnClose.setEnabled(false);
			m_btnOpen.setEnabled(false);
			m_btnModify.setEnabled(false);
			m_btnDiscard.setEnabled(false);
			m_btnUnApprove.setEnabled(false);
		} else if (iBillStatus == 3) {/* �빺��������ͨ�� */
			m_btnSendAudit.setEnabled(false);
			m_btnOpen.setEnabled(false);
			m_btnModify.setEnabled(false);
			m_btnDiscard.setEnabled(false);
			m_btnApprove.setEnabled(false);
		} else if (iBillStatus == 4) {/* �빺��������ͨ�� */
			m_btnSendAudit.setEnabled(false);
			m_btnClose.setEnabled(false);
			m_btnOpen.setEnabled(false);
			m_btnUnApprove.setEnabled(false);
		}
		// ˢ��
		m_btnRefresh.setEnabled(m_bQueried);
		//
		updateButtonsAll();
	}

	/**
	 * ���ܣ�������ʱ��ʼ����Ƭ��ť,���ó����棬�в��������������а�ťΪ���� ���أ� ���⣺ ���ڣ�(2002-5-14 11:24:40)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 */
	private void setButtonsCardModify() {
		//
		setButtonsEnabled(false);
		//
		dealBtnsBeforeCardShowing();
		// �в���
		m_btnLines.setEnabled(true);
		if (m_btnLines.getChildCount() > 0) {
			for (int i = 0; i < m_btnLines.getChildButtonGroup().length; i++) {
				m_btnLines.getChildButtonGroup()[i].setEnabled(true);
			}
		}
		// ��������
		m_btnOthersFuncs.setEnabled(true);
		// ������ѯ
		m_btnOthersQry.setEnabled(true);
		btnCKKCXX.setEnabled(true);
		// ������ѯ
		m_btnUsable.setEnabled(true);
		// ����ά��
		m_btnMaintains.setEnabled(true);
		// ����
		m_btnSave.setEnabled(true);
		// ȡ��
		m_btnCancel.setEnabled(true);
		// ����
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
	 * ����ť�߼�����
	 * 
	 * @author czp
	 * @since v50
	 * @date 2006-09-23
	 * 
	 * @ע�⣺��������Լ������������ʱ���ô˷�����Ҫ����״ֵ̬Ϊ -1
	 * 
	 */
	private boolean isNeedSendAudit(int iBillStatus) {

		// ����δͨ��
		boolean isNeedSendToAuditQ = (iBillStatus == BillStatus.AUDITFAIL);

		// ����
		if (iBillStatus == -1) {
			if (m_bizButton != null && m_bizButton.getTag() != null) {
				isNeedSendToAuditQ = BusiBillManageTool.isNeedSendToAudit("20",
						m_sLoginCorpId, m_bizButton.getTag(), null,
						getClientEnvironment().getUser().getPrimaryKey());
			}
		}

		// ����(�޸����)
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
	 * ���ܣ�������ʱ��ʼ����Ƭ��ť,���ó����棬�в��������������а�ťΪ���� ���أ� ���⣺ ���ڣ�(2002-5-14 11:24:40)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 */
	private void setButtonsCardCopy() {
		//
		setButtonsEnabled(false);

		// ���⹦��
		m_btnList.setName(m_lanResTool.getStrByID("SCMCOMMON",
				"UPPSCMCommon-000464")/*
									 * @res "�б���ʾ"
									 */);
		// �б���
		m_btnSelectAll.setEnabled(false);
		m_btnSelectNo.setEnabled(false);
		//
		m_btnLines.setEnabled(true);
		if (m_btnLines.getChildCount() > 0) {
			for (int i = 0; i < m_btnLines.getChildButtonGroup().length; i++) {
				m_btnLines.getChildButtonGroup()[i].setEnabled(true);
			}
		}
		/* �޸�ʱ����:����{������ѯ} */
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
			// �ò������Ƿ���������
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
		// ˢ��
		m_btnRefresh.setEnabled(false);
	}

	/**
	 * ����ʵ�ָ÷���������ҵ�����ı��⡣
	 * 
	 * @version (00-6-6 13:33:25)
	 * 
	 * @return java.lang.String
	 */
	private void setCauditid(String newId) {

		m_cauditid = newId;
	}

	/**
	 * ���ܣ����þ��� 2002-10-29 czp ��������޼۾�������
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
	 * �����빺����Ƭ�ɱ༭�� ���ñ���ɱ༭�ԣ���Ҫ��������ɱ༭��Ȼ�������ñ���ɱ༭��
	 */
	private void setEdit(boolean b) {
		// ���ŵ��ݲ��ɱ༭
		getBillCardPanel().setEnabled(false);
		// �Ƿ�ɱ༭
		m_bEdit = b;
		// �����Ƿ�ɱ༭
		getBillCardPanel().getBodyItem("npraynum").setEnabled(b);
		getBillCardPanel().getBodyItem("npraynum").setEdit(b);
	}

	/**
	 * ���ߣ���ά�� ���ܣ���������������Ŀɱ༭�� ������iBeginRow��iEndRow �����к� ���أ��� ���⣺�� ���ڣ�(2001-4-22
	 * 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2003-02-26 wyf �޸�Ϊȡ���������ķ����������Ч��
	 */
	private static void setEnabled_BodyFree(BillCardPanel pnlBillCard,
			UIRefPane refpaneInv, int iBeginRow, int iEndRow) {
		if (pnlBillCard == null || refpaneInv == null) {
			SCMEnv.out("�����������ȷ��");
			return;
		}
		Object[] saMangId = ((Object[]) refpaneInv
				.getRefValues("bd_invmandoc.pk_invmandoc"));
		final int size = saMangId.length;
		String[] sMangIds = new String[size];
		for (int i = 0; i < size; i++) {
			sMangIds[i] = saMangId[i].toString();
		}
		// ����װ��
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
	 * ���ߣ���ά�� ���ܣ��������ô���Ĺ��������Ϣ ����: BillCardPanel pnlBillCard ����ģ�� UIRefPane
	 * refpaneInv ������� int iBeginRow ��ʼλ�� int iEndRow ����λ�� ���أ��� ���⣺��
	 * ���ڣ�(2004-02-11 13:45:10) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	private static void setInvEditFormulaInfo(BillCardPanel pnlBillCard,
			UIRefPane refpaneInv, int iBeginRow, int iEndRow,
			String sLoginCorpId, String sLoginCorpName) {
		if (pnlBillCard == null || refpaneInv == null) {
			SCMEnv.out("�����������ȷ��");
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
				Logger.debug("���ݴ��󣺴��������IDΪ�ջ�������IDΪ�ջ���߳��Ȳ��ȣ�ֱ�ӷ���");
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

			// ================����������������������,����������
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
			// ����������
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
			// ================����������:���ء�����޼�
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
			// ����
			String[] saArea = new String[iLen];
			// ����޼�
			UFDouble[] uMaxPrice = new UFDouble[iLen];
			// ��������λ
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
			// ================�Ա����������ֵ
			Object[] saCode = ((Object[]) refpaneInv
					.getRefValues("bd_invbasdoc.invcode"));
			Object[] saName = ((Object[]) refpaneInv
					.getRefValues("bd_invbasdoc.invname"));
			Object[] saSpec = ((Object[]) refpaneInv
					.getRefValues("bd_invbasdoc.invspec"));
			Object[] saType = ((Object[]) refpaneInv
					.getRefValues("bd_invbasdoc.invtype"));

			// ִ�б��幫ʽ
			int iPkIndex = 0;
			for (int i = iBeginRow; i < iEndRow; i++) {
				iPkIndex = i - iBeginRow;
				// ����ID
				pnlBillCard.setBodyValueAt(saMangId[iPkIndex], i, "cmangid");
				// ����ID
				pnlBillCard.setBodyValueAt(saBaseId[iPkIndex], i, "cbaseid");
				// ����
				pnlBillCard.setBodyValueAt(saCode[iPkIndex], i,
						"cinventorycode");
				// ����
				pnlBillCard.setBodyValueAt(saName[iPkIndex], i,
						"cinventoryname");
				// ���
				pnlBillCard.setBodyValueAt(saSpec[iPkIndex], i,
						"cinventoryspec");
				// �ͺ�
				pnlBillCard.setBodyValueAt(saType[iPkIndex], i,
						"cinventorytype");
				// ������λNAME
				pnlBillCard.setBodyValueAt(saUnitName[iPkIndex], i,
						"cinventoryunit");
				// ����
				pnlBillCard.setBodyValueAt(saArea[iPkIndex], i, "cprodarea");
				// ����޼�
				pnlBillCard.setBodyValueAt(uMaxPrice[iPkIndex], i, "nmaxprice");
				// ��������λ
				pnlBillCard.setBodyValueAt(sAssisUnitName[iPkIndex], i,
						"cassistunitname");
				// ������ID
				pnlBillCard.setBodyValueAt(sAssisUnit[iPkIndex], i,
						"cassistunit");
			}
		} catch (Exception e) {
			Logger.debug("¼�����ʱ���ó���");
		}
	}

	/**
	 * �����б�ť�߼�
	 */
	private void setButtonsList() {

		// �б�����
		m_btnCard.setName(m_lanResTool.getStrByID("SCMCOMMON",
				"UPPSCMCommon-000463")/*
									 * @res "��Ƭ��ʾ"
									 */);
		// ��Ƭ����
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

		/* �ݲ��ṩ�б�۸���֤���� */
		m_btnPriceInfo.setEnabled(false);
		/* �ݲ�֧�ֵ��б��ܣ������ؼ�û���б��� */
		m_btnCombin.setEnabled(false);

		// ����Ч��
		m_btnMaintains.setEnabled(true);
		m_btnActions.setEnabled(true);
		m_btnQuery.setEnabled(true);
		m_btnBrowses.setEnabled(true);
		m_btnPrints.setEnabled(true);
		m_btnOthersQry.setEnabled(true);
		m_btnOthersFuncs.setEnabled(true);

		// ��ѯ������Ч
		m_btnRefresh.setEnabled(m_bQueried);

		/*
		 * ����������ذ�ť��������
		 * 
		 * ȫ����Ԥ������ӡ���ĵ�����
		 * 
		 * ȫѡ����Ƭ��ʾ���޸ġ����ϡ����ơ��������������󡢹رա��򿪡��ϲ���ʾ�����顢������ѯ��������״̬
		 */
		int iSelectedCnt = getBillListPanel().getHeadTable()
				.getSelectedRowCount();
		int iCacheDataCnt = getBillListPanel().getHeadBillModel().getRowCount();
		if (m_VOs == null || m_VOs.length <= 0) {
			iCacheDataCnt = 0;
		}
		/* û������ */
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
		/* �����ݵ�û��ѡȡ�κε��� */
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

		/* ������ �� ѡȡ���������ڵ���1 */
		m_btnSelectNo.setEnabled(true);
		m_btnPrintPreview.setEnabled(true);
		m_btnPrint.setEnabled(true);
		m_btnDocument.setEnabled(true);

		// ȫѡ
		if (iSelectedCnt != iCacheDataCnt) {
			m_btnSelectAll.setEnabled(true);
		} else {
			m_btnSelectAll.setEnabled(false);
		}

		/* ��Ƭ��ʾ�����ơ��ϲ���ʾ�����顢������ѯ��������״̬ */
		boolean bOnlyOneSelected = (iSelectedCnt == 1);
		m_btnCard.setEnabled(bOnlyOneSelected);
		m_btnCopy.setEnabled(bOnlyOneSelected);
		// �ݲ�֧�ֵ��б��ܣ������ؼ�û���б���
		// m_btnCombin.setEnabled(bOnlyOneSelected);
		m_btnLinkBillsBrowse.setEnabled(bOnlyOneSelected);
		m_btnUsable.setEnabled(bOnlyOneSelected);
		m_btnWorkFlowBrowse.setEnabled(bOnlyOneSelected);

		/* �޸ġ����ϡ��������������󡢹رա��� */
		if (bOnlyOneSelected) {
			/* ֻ��һ�ŵ���ѡ�е���� */
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
			// �ݲ�֧�������رա���
			m_btnClose.setEnabled(false);
			m_btnOpen.setEnabled(false);
		}
		// ˢ�°�ť״̬
		updateButtonsAll();
	}

	/**
	 * �б���ʱ�����������б�ť�߼����á�
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * ʹ��ʾ��
	 * <p>
	 * <b>����˵��</b>
	 * <p>
	 * 
	 * @author czp
	 * @time 2007-3-14 ����10:21:56
	 */
	private void setButtonsListWhenErr() {
		setButtonsEnabled(false);
		/* ����:��ѯ,ˢ�� */
		m_btnBrowses.setEnabled(true);
		m_btnQuery.setEnabled(true);
		m_btnRefresh.setEnabled(m_bQueried);
		/* ˢ�°�ť״̬ */
		updateButtonsAll();
	}

	/**
	 * ��������:�����빺�����壺������ƣ������񣬴���ͺţ����أ���������������ַ���ɱ༭
	 * �����빺����ͷ��ҵ�����ͣ��빺��Դ�������ˣ��Ƶ��ˣ��������ڲ��ɱ༭
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
	 * ��Ŀ�׶οɱ༭��
	 */
	private void setProjPhaseEditable(BillEditEvent e) {
		final int n = e.getRow();
		if (n < 0)
			return;
		// �ж���Ŀ�Ƿ�Ϊ�ա���Ϊ�գ�����Ŀ�׶β��ɱ༭
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
	 * ���ø���������
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
	 * ���ò��յ���������
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
	 * ����:�������������ʡ��������ɱ༭�ԡ��������������ݼ��ɱ༭��
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
			// �Ƿ���и���������
			strCbaseid = (String) bcp.getBillModel()
					.getValueAt(iRow, "cbaseid");
			// �д��
			if (strCbaseid != null && !strCbaseid.trim().equals("")) {
				if (PuTool.isAssUnitManaged(strCbaseid)) {
					// ���ø���������
					setRefPaneAssistunit(bcp, iRow);
					cassistunit = (String) bcp.getBillModel().getValueAt(iRow,
							"cassistunit");
					// ��������Ϊ��
					oTmp = bcp.getBillModel().getValueAt(iRow, "nexchangerate");
					if (oTmp == null || oTmp.toString().trim().equals("")) {
						// ���û�����(���ԭ�����ڻ����������ã���Ϊ�������Ѿ��ı��˵ķǹ̶�������)
						ufdConv = PuTool.getInvConvRateValue(strCbaseid,
								cassistunit);
						bcp.getBillModel().setValueAt(ufdConv, iRow,
								"nexchangerate");
					}
					// ���ÿɱ༭��
					bcp.setCellEditable(iRow, "cassistunitname", true);
					bcp.setCellEditable(iRow, "npraynum", true);
					bcp.setCellEditable(iRow, "nassistnum", true);
					bcp.setCellEditable(iRow, "nmoney", true);
					bcp.setCellEditable(iRow, "nexchangerate", true);
					// ����������ǹ̶�������
					if (PuTool.isFixedConvertRate(strCbaseid, cassistunit)) {
						bcp.setCellEditable(iRow, "nexchangerate", false);
					} else {
						bcp.setCellEditable(iRow, "nexchangerate", true);
					}
					// ���������������ͬ,�����ʲ��ɱ༭
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
			// �޴��
			else {
				bcp.setCellEditable(iRow, "npraynum", false);
				bcp.setCellEditable(iRow, "nmoney", false);
				bcp.setCellEditable(iRow, "nexchangerate", false);
				bcp.setCellEditable(iRow, "nassistnum", false);
				bcp.setCellEditable(iRow, "cassistunitname", false);
			}
		} catch (Exception e) {
			Logger.debug("¼�����ʱ���ó���");
		}
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-10-28 14:42:54)
	 */
	private void setSendAuditBtnState() {
		if (getPraybillVOs() == null || getPraybillVOs().length <= 0) {
			m_btnSendAudit.setEnabled(false);
			return;
		}
		PraybillVO curVO = getPraybillVOs()[getCurVoPos()];
		if (curVO == null)
			return;
		// ����
		if (PuTool.isNeedSendToAudit(nc.vo.scm.pu.BillTypeConst.PO_PRAY, curVO)
				&& !isAlreadySendToAudit) {
			m_btnSendAudit.setEnabled(true);
		} else {
			m_btnSendAudit.setEnabled(false);
		}
	}

	/**
	 * ���ܣ��򵥾�ģ���м��뻺��VO(�빺��ά�����) ���ߣ���־ƽ ���ڣ�(2003-3-12 10:27:34)
	 */
	private void setVoToBillCard(int iRollBackPos, String strmsg) {
		String strMsg = null;
		if (strmsg == null || strmsg.trim().equals("")) {
			strMsg = "";
		} else {
			strMsg = strmsg.trim();
		}
		if (m_VOs == null || m_VOs.length <= 0) {
			Logger.debug("������������");
			m_nPresentRecord = iRollBackPos;
			String[] value = new String[] { strMsg };
			showHintMessage(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000440", null, value));
			return;
		}
		try {
			m_VOs[m_nPresentRecord] = PrTool
					.getRefreshedVO(m_VOs[m_nPresentRecord]);
			// ����������
			new nc.ui.scm.pub.FreeVOParse().setFreeVO(m_VOs[m_nPresentRecord]
					.getBodyVO(), "vfree", "vfree", "cbaseid", "cmangid", true);
			getBillCardPanel().getBillModel().clearBodyData();
		} catch (Exception be) {
			if (be instanceof BusinessException)
				MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
						"SCMCOMMON", "UPPSCMCommon-000422")/* "ҵ���쳣" */, be
						.getMessage());
			m_nPresentRecord = iRollBackPos;
			String[] value = new String[] { strMsg };
			showHintMessage(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000440", null, value));
			return;
		}

		// ��ʾ���ŵ���
		for (int i = 0; i < 1; i++) {
			try {
				getBillCardPanel().setBillValueVO(m_VOs[m_nPresentRecord]);
			} catch (Exception e) {
				continue;
			}
		}
		nc.ui.pu.pub.PuTool.loadInvMaxPrice(getBillCardPanel());
		getBillCardPanel().getBillModel().execLoadFormula();
		// ��ʾ��Դ��Ϣ
		nc.ui.pu.pub.PuTool.loadSourceInfoAll(getBillCardPanel(),
				nc.vo.scm.pu.BillTypeConst.PO_PRAY);
		getBillCardPanel().updateValue();
		// ��ʾ�����б���Ϣ
		Integer nTemp1 = (Integer) m_VOs[m_nPresentRecord].getParentVO()
				.getAttributeValue("ipraysource");
		Integer nTemp2 = (Integer) m_VOs[m_nPresentRecord].getParentVO()
				.getAttributeValue("ipraytype");
		m_comPraySource.setSelectedIndex(nTemp1.intValue());
		m_comPrayType.setSelectedIndex(nTemp2.intValue());
		getBillCardPanel().updateUI();
		// ��ʾ��ע
		UIRefPane nRefPanel3 = (UIRefPane) getBillCardPanel().getHeadItem(
				"vmemo").getComponent();
		nRefPanel3.setValue(m_VOs[m_nPresentRecord].getHeadVO().getVmemo());
		String[] value = new String[] { strMsg };
		showHintMessage(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000085", null, value));
		// ����ͷ��ʽ���ݣ����ⲻ�ɲ��ճ�������
		execHeadTailFormula(m_VOs[m_nPresentRecord]);
	}

	/**
	 * ���ܣ��򵥾ݽ�����ʾ�빺��(����������) ע�⣺��ʱ�� m_VOs �Ѿ�������������Ϣ ������ ���أ� ���⣺ ���ڣ�(2002-6-27
	 * 14:06:10) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	private void setVoToBillPanel() {
		if (m_VOs != null && m_VOs.length > 0) {
			// Ĭ����ʾ��һ��
			m_nPresentRecord = 0;
			// ���ر���
			try {
				m_VOs[m_nPresentRecord] = PrTool
						.getRefreshedVO(m_VOs[m_nPresentRecord]);
				// ����������
				new nc.ui.scm.pub.FreeVOParse().setFreeVO(
						m_VOs[m_nPresentRecord].getBodyVO(), "vfree", "vfree",
						"cbaseid", "cmangid", true);
			} catch (Exception be) {
				if (be instanceof BusinessException)
					MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
							"SCMCOMMON", "UPPSCMCommon-000422")/* "ҵ���쳣" */,
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
			// ���ز�ѯ���
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
			// ��ʾ��Դ��Ϣ
			nc.ui.pu.pub.PuTool.loadSourceInfoAll(getBillCardPanel(),
					nc.vo.scm.pu.BillTypeConst.PO_PRAY);
			// ����ͷ��ʽ���ݣ����ⲻ�ɲ��ճ�������
			execHeadTailFormula(m_VOs[m_nPresentRecord]);
			getBillCardPanel().updateValue();
			// ��ʾ��ע
			UIRefPane nRefPanel3 = (UIRefPane) getBillCardPanel().getHeadItem(
					"vmemo").getComponent();
			nRefPanel3.setValue(m_VOs[m_nPresentRecord].getHeadVO().getVmemo());
			m_btnWorkFlowBrowse.setEnabled(true);
			m_btnAudit.setEnabled(true);
			updateButtons();
		} else {
			m_VOs = null;
			// �޷��ز�ѯ���
			MessageDialog.showHintDlg(this, m_lanResTool.getStrByID("40040101",
					"UPP40040101-000055")/* "�빺����ѯ" */, m_lanResTool.getStrByID(
					"40040101", "UPP40040101-000056")/* "û�з����������빺����" */);
			// �������
			getBillCardPanel().getBillData().clearViewData();
			getBillCardPanel().updateUI();
			m_btnWorkFlowBrowse.setEnabled(false);
			m_btnAudit.setEnabled(false);
			updateButtons();
		}
	}

	/**
	 * ���ߣ����� ���ܣ��趨ĳ�е����κŵĿɱ༭�ԣ� ������iRow int ���κ����ڵ��У� ���أ��� ���⣺�� ���ڣ�(2002-6-25
	 * 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	private void setVProduceNumEditState(int iRow) {

		String sMangId = (String) getBillCardPanel().getBodyValueAt(iRow,
				"cmangid");
		if (sMangId == null || sMangId.trim().length() < 1) {
			// =====�ɱ༭
			getBillCardPanel().setCellEditable(iRow, "vproducenum", true);

		} else {
			// =====�Ƿ�ɱ༭
			getBillCardPanel().setCellEditable(iRow, "vproducenum",
					nc.ui.pu.pub.PuTool.isBatchManaged(sMangId));
		}
	}

	/**
	 * ���ߣ���־ƽ ���ܣ�ʵ��ListSelectionListener�ļ������� ������ListSelectionEvent e �����¼� ���أ���
	 * ���⣺�� ���ڣ�(2002-5-23 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	public void valueChanged(ListSelectionEvent e) {
		/**/
		boolean isErr = false;
		/* ѡȡ������ */
		int iSelCnt = -1;
		int iSelFirstRow = -1;
		/* �������г�ʼΪδѡ�� */
		final int iCount = getBillListPanel().getHeadTable().getRowCount();
		for (int i = 0; i < iCount; i++) {
			getBillListPanel().getHeadBillModel().setRowState(i,
					BillModel.NORMAL);
		}
		/* �õ���ѡ�е��� */
		int[] selectedRows = getBillListPanel().getHeadTable()
				.getSelectedRows();
		if (selectedRows != null && selectedRows.length > 0) {
			iSelCnt = selectedRows.length;
			iSelFirstRow = selectedRows[0];
		}
		/* ��ѡ�е��б�ʾΪ�򣪺� */
		if (iSelCnt > 0) {
			for (int i = 0; i < iSelCnt; i++) {
				getBillListPanel().getHeadBillModel().setRowState(
						selectedRows[i], BillModel.SELECTED);
			}
		}
		/* ��ʾ��ѡ�е��кͱ��� */
		if (iSelCnt != 1) {
			getBillListPanel().getBodyBillModel().setBodyDataVO(null);
		} else {
			/* ���ҽ�����ͷֻ��һ�б�ѡ��ʱ:���ñ���λ��(֧������) */
			m_nPresentRecord = nc.ui.pu.pub.PuTool.getIndexBeforeSort(
					getBillListPanel(), iSelFirstRow);
			/* �����ʱ�ż��ر��� */
			if (m_VOs != null && m_VOs.length > 0) {
				try {
					m_VOs[m_nPresentRecord] = PrTool
							.getRefreshedVO(m_VOs[m_nPresentRecord]);
					// ����������
					new nc.ui.scm.pub.FreeVOParse().setFreeVO(
							m_VOs[m_nPresentRecord].getBodyVO(), "vfree",
							"vfree", "cbaseid", "cmangid", true);

				} catch (Exception be) {
					getBillListPanel().getBodyBillModel().clearBodyData();
					if (be instanceof BusinessException)
						MessageDialog.showErrorDlg(this,
								m_lanResTool.getStrByID("SCMCOMMON",
										"UPPSCMCommon-000422")/* "ҵ���쳣" */, be
										.getMessage());
					isErr = true;
				}
			}
			/* ������� */
			PraybillItemVO[] bodyVO = m_VOs[m_nPresentRecord].getBodyVO();
			getBillListPanel().getBodyBillModel().setBodyDataVO(bodyVO);
			nc.ui.pu.pub.PuTool.loadInvMaxPrice(getBillListPanel());
			getBillListPanel().getBodyBillModel().execLoadFormula();
			/* ���ص�����Դ��Ϣ */
			nc.ui.pu.pub.PuTool.loadSourceInfoAll(getBillListPanel(),
					nc.vo.scm.pu.BillTypeConst.PO_PRAY);
			getBillListPanel().getBodyBillModel().updateValue();
			getBillListPanel().updateUI();
		}
		/* �����б�ť�߼� */
		setButtonsList();
		/* ���б��־ */
		m_nUIState = 1;
		showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
				"UPPSCMCommon-000393")/* "�������" */);
		if (isErr) {
			setButtonsListWhenErr();
			showHintMessage(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000086")/* "����ʱ���ر�����ֲ���" */);
		}
	}

	/**
	 * ������ӡ�ӿڣ�ʵ����Ƭ�������ݣ�����ɿ�Ƭ��˾���������õ�
	 * 
	 * @see nc.ui.scm.pub.print.ISetBillVO#setBillVO(nc.vo.pub.AggregatedValueObject)
	 */
	public void setBillVO(AggregatedValueObject vo) {
		setVoToBillCard(m_nPresentRecord, "");
	}

	/** Ϊ���ο����ṩ�ӿ� ********************************** */
	/**
	 * ���Ƿ�����nc.ui.pr.pray.PrayUI.java.getExtendBtns �������ڣ�2005-9-22
	 * ������������ȡ��չ��ť���飨ֻ�ṩ��Ƭ���水ť��
	 */
	public ButtonObject[] getExtendBtns() {
		return null;
	}

	/**
	 * ���Ƿ�����nc.ui.pr.pray.PrayUI.java.onExtendBtnsClick �������ڣ�2005-9-22
	 * ����������������չ��ť���¼�
	 */
	public void onExtendBtnsClick(ButtonObject bo) {
		// ����ʵ��
	}

	/**
	 * ���Ƿ�����nc.ui.pr.pray.PrayUI.java.setExtendBtnsStat �������ڣ�2005-9-22
	 * ����������������չ��ť״̬ ״ֵ̬������0---��ʼ�� 1---��Ƭ��� 2---��Ƭ�༭ 3---�б�������ݲ�֧�֣�
	 */
	public void setExtendBtnsStat(int iState) {
		// ����ʵ��
	}

	/**
	 * @���ܣ�����༭�����òɹ�Ա���ɹ���֯���ɹ���˾
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
			// ȡ��ѯ���ɲ���
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
			// ���òɹ�Ա���ɹ���֯ID���ɹ���˾
			if (v.size() > 0) {
				para = new IsCentralVO[v.size()];
				v.copyInto(para);
				result = bo.isCentralPur(para);
				if (result != null && result.length > 0
						&& result.length == iEnd - iBgn) {
					for (int i = 0; i < result.length; i++) {
						resultTemp = result[i];
						if (resultTemp.getIsCentralPur()) {
							// �ɹ�Ա
							bm.setValueAt(boA.getPurchaser(resultTemp
									.getPk_corp(), (String) bm.getValueAt(i
									+ iBgn, "cbaseid")), i + iBgn,
									"cemployeeid");
							// �ɹ���֯ID
							bm.setValueAt(resultTemp.getPk_purorg(), iBgn + i,
									"cpurorganization");
							// �ɹ���˾
							bm.setValueAt(resultTemp.getPk_corp(), iBgn + i,
									"pk_purcorp");
						} else {
							// �ɹ�Ա
							bm.setValueAt(boA.getPurchaser(bcp.getHeadItem(
									"pk_corp").getValue(), (String) bm
									.getValueAt(i + iBgn, "cbaseid")),
									i + iBgn, "cemployeeid");
							// �ɹ���֯ID
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
	 * V5 ,֧���û����岻�ɱ༭��Ŀ�޸�
	 */
	public boolean beforeEdit(BillItemEvent e) {
		if (!e.getItem().isEdit()) {
			return false;
		}
		return true;
	}

	/**
	 * <p>
	 * ���򷽷�������Ҫ����Ļ���VO����
	 * 
	 * @since V50
	 */
	public Object[] getRelaSortObjectArray() {
		return m_VOs;
	}

	/**
	 * <p>
	 * ���򷽷�������Ҫ����ĵ�ǰVO����VO����
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
	 * ��������ӿڷ���ʵ�� -- ά��
	 */
	public void doMaintainAction(ILinkMaintainData maintaindata) {
		SCMEnv.out("����ά���ӿ�...");

		String billID = maintaindata.getBillID();

		initi();

		PraybillVO vo = null;
		m_VOs = null;
		try {
			vo = PraybillHelper.queryPrayVoByHid(billID);
		} catch (Exception e) {
			SCMEnv.out(e);
		}
		// �����ز�ѯ���

		// �����ǰ��¼��˾���ǲ���Ա�Ƶ����ڹ�˾��������޲�����ť�����ṩ������ܣ�by chao , xy , 2006-11-07
		String strLoginCorpId = PoPublicUIClass.getLoginPk_corp();
		String strPrayCorpId = vo == null ? null : vo.getHeadVO().getPk_corp();
		boolean bSameCorpFlag = strLoginCorpId.equals(strPrayCorpId);

		// �����ݴ���
		if (vo == null) {
			MessageDialog.showHintDlg(this, m_lanResTool.getStrByID("40040101",
					"UPP40040101-000055")/* "�빺����ѯ" */, m_lanResTool.getStrByID(
					"40040101", "UPP40040101-000056")/* "û�з����������빺����" */);
			// �������
			getBillCardPanel().getBillData().clearViewData();
			// ���ð�ť�߼�
			if (bSameCorpFlag) {
				setButtonsCard();
			} else {
				setButtonsNull();
			}
			return;
		}

		// �����ݴ���
		m_VOs = new PraybillVO[] { vo };
		m_nPresentRecord = 0;

		// �����ͷ�����б�
		Integer nTemp1 = (Integer) m_VOs[0].getHeadVO().getAttributeValue(
				"ipraysource");
		Integer nTemp2 = (Integer) m_VOs[0].getHeadVO().getAttributeValue(
				"ipraytype");
		m_comPraySource.setSelectedIndex(nTemp1.intValue());
		m_comPrayType.setSelectedIndex(nTemp2.intValue());

		// ���ص��ݵ���Ƭ
		m_nPresentRecord = 0;
		setVoToBillCard(m_nPresentRecord, null);

		// ���ð�ť�߼�
		setButtonsCard();

		//
		m_nUIState = 0;
		// ˢ�°�ť״̬
		updateButtonsAll();
		//
		m_bAdd = false;
		m_bEdit = false;
		getBillCardPanel().setEnabled(false);

		// ��������ڱ���˾�Ƶ�����������а�ť
		if (bSameCorpFlag) {
			onModify(false);
		} else {
			setButtonsNull();
		}
	}

	/**
	 * ��������ӿڷ���ʵ�� -- ����
	 */
	public void doAddAction(ILinkAddData adddata) {
		SCMEnv.out("���������ӿ�...");

	}

	/**
	 * ��������ӿڷ���ʵ�� -- ����
	 */
	public void doApproveAction(ILinkApproveData approvedata) {
		SCMEnv.out("���������ӿ�...");
		if (approvedata == null)
			return;
		String billID = approvedata.getBillID();
		/* ��ʼ�� */
		initi();
		isCanAutoAddLine = false;
		/* ƽ̨��Ҫ */
		setCauditid(billID);
		/* ��ѯ�빺�� */
		PraybillVO vo = null;
		try {
			vo = (PraybillVO) getVo();
		} catch (Exception e) {
			SCMEnv.out(e);
			MessageDialog
					.showHintDlg(this, m_lanResTool.getStrByID("SCMCOMMON",
							"UPPSCMCommon-000270")/* "��ʾ" */, m_lanResTool
							.getStrByID("SCMCOMMON", "UPPSCMCommon-000428")/* "ϵͳ���ϣ�" */);
		}

		/* �޵��ݵĴ��� */
		if (vo == null) {
			m_VOs = null;
			/* ����ť������ */
			for (int i = 0; i < m_btnsAuditCenter.length; i++) {
				m_btnsAuditCenter[i].setEnabled(false);
			}
			for (int i = 0; i < m_btnsAuditCenter.length; i++) {
				updateButton(m_btnsAuditCenter[i]);
			}
			setButtonsNull();
			return;
		}
		/* �е��ݵĴ��� */
		boolean bCorpSameFlag = getCorpPrimaryKey().equals(vo.getPk_corp());
		// ���ð�ť��
		if (bCorpSameFlag) {
			setButtons(getBtnTree().getButtonArray());
			setButtonsCard();
		} else {
			setButtons(m_btnsAuditCenter);
			/* ����ť���� */
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
		/* �����빺��VO���� */
		m_VOs = (new PraybillVO[] { vo });
		m_nPresentRecord = 0;
		/* ���ÿ�Ƭ�������� */
		setVoToBillPanel();
		//
		setEdit(false);
	}

	/**
	 * ��������ӿڷ���ʵ�� -- ������
	 * 
	 * @modified by czp since v51, ��������Ȩ��
	 */
	public void doQueryAction(ILinkQueryData querydata) {
		SCMEnv.out("����������ӿ�...");

		String billID = querydata.getBillID();

		initi();

		PraybillVO vo = null;

		try {
			// �Ȱ��յ���PK��ѯ���������Ĺ�˾corpvalue
			vo = PraybillHelper.queryPrayVoByHid(billID);
			if (vo == null) {
				MessageDialog
						.showHintDlg(this, m_lanResTool.getStrByID("SCMCOMMON",
								"UPPSCMCommon-000270")/* "��ʾ" */, m_lanResTool
								.getStrByID("common", "SCMCOMMON000000161")/* "û�в쿴���ݵ�Ȩ��" */);
				return;
			}
			//
			String strPkCorp = vo.getPk_corp();
			// ���յ���������˾���ز�ѯģ��
			PrayUIQueryDlg queryDlg = new PrayUIQueryDlg(this, strPkCorp);// ��ѯģ����û�й�˾ʱ��Ҫ�������⹫˾
			queryDlg.setDefaultValue("po_praybill.dpraydate",
					"po_praybill.dpraydate", "");
			queryDlg.initCorpRefs();
			// ���ù���������ȡ�ù�˾�п���Ȩ�޵ĵ�������VO����
			QueryConditionVO[] condVOs = queryDlg.getConditionDatas();
			PuTool.switchReturnType(PrayPubVO._Hash_PrayUI,
					PrayPubVO._Hash_PrayUI_Code_Flag, condVOs, true);
			ConditionVO[] voaCond = queryDlg.getDataPowerConVOs(strPkCorp,
					PrayUIQueryDlg.REFKEYS);
			PuTool.switchReturnType(PrayPubVO._Hash_PrayUI,
					PrayPubVO._Hash_PrayUI_Code_Flag, condVOs, false);
			// ��֯�ڶ��β�ѯ���ݣ�����Ȩ�޺͵���PK����
			PraybillVO[] voaRet = PraybillHelper.queryAll(null, voaCond, null,
					null, "po_praybill.cpraybillid = '" + billID + "' ");
			if (voaRet == null || voaRet.length <= 0 || voaRet[0] == null) {
				MessageDialog
						.showHintDlg(this, m_lanResTool.getStrByID("SCMCOMMON",
								"UPPSCMCommon-000270")/* "��ʾ" */, m_lanResTool
								.getStrByID("common", "SCMCOMMON000000161")/* "û�в쿴���ݵ�Ȩ��" */);
				setButtonsNull();
				return;
			}
			m_VOs = new PraybillVO[] { vo };
			m_nPresentRecord = 0;
			setVoToBillCard(m_nPresentRecord, "");
			Logger.debug("�ɹ���ʾ����");
		} catch (Exception e) {
			SCMEnv.out(e);
			MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000270")/* "��ʾ" */, e
					.getMessage());
			setButtonsNull();
			return;
		}
		boolean bCorpSameFlag = getCorpPrimaryKey().equals(vo.getPk_corp());
		// ���ð�ť��
		if (bCorpSameFlag) {
			setButtons(getBtnTree().getButtonArray());
			setButtonsCard();
		} else {
			setButtonsNull();
		}
	}

	/**
	 * ��յ�ǰ���水ť
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
	 * �ϲ���ʾ����ӡ����
	 * 
	 * @since v50
	 */
	private void onCombin() {
		CollectSettingDlg dlg = new CollectSettingDlg(this, m_lanResTool
				.getStrByID("4004020201", "UPT4004020201-000084")/*
																 * @res "�ϲ���ʾ"
																 */);
		dlg.initData(getBillCardPanel(),
		// �̶�������
				new String[] { "cinventorycode", "cinventoryname",
						"cinventoryspec", "cinventorytype", "cprodarea" },
				// ȱʡ������
				null,
				// �����
				new String[] { "nmoney", "npraynum" },
				// ��ƽ����
				null,
				// ���Ȩƽ����
				new String[] { "nsuggestprice" },
				// ������
				"npraynum");
		dlg.showModal();
		showHintMessage(m_lanResTool
				.getStrByID("common", "4004COMMON000000039")/* @res "�ϲ���ʾ���" */);
	}

	/**
	 * ������¼�,��Ҫ��Ϊ�˴����BillModel���ݵ��������Զ���Ļ������ؼ�
	 * 
	 * @author czp
	 * @since v50
	 */
	public void afterSort(String key) {
		// ������ʾ
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
	 * �����߳�
	 */
	Thread readcard = null;
	/**
	 * �̹߳رձ�ʾ
	 */
	volatile boolean isclose = false;
	/**
	 * �����ʾ
	 */
	int icdev = -1;

	/**
	 * �����߳�
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
											"����", "����ˢ��IC�����˶����Ա����Ա���룺"
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
						// ȡ��ˢ���������Զ���˹���
						/*
						if (flag == 1 && !m_btnSave.isEnabled()
								&& m_btnAudit.isEnabled()) {
							onButtonClicked(m_btnAudit);
						} else if (m_btnAudit.isEnabled()
								|| m_btnDiscard.isEnabled()) {
							obj = MessageDialog.showSelectDlg(PrayUI.this,
									"��ʾ", "��ѡ���������",
									new String[] { "���", "ɾ��" }, 2);
							if ("���".equals(obj.toString())) {
								onButtonClicked(m_btnAudit);
							} else if ("ɾ��".equals(obj.toString())) {
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