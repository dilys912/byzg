package nc.ui.pr.audit;

import java.awt.BorderLayout;
import java.awt.Color;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;
import javax.swing.ButtonGroup;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.exception.ComponentException;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.ic.ic212.TestConn;
import nc.ui.ml.NCLangRes;
import nc.ui.pr.pray.PrayUI;
import nc.ui.pr.pray.PraybillHelper;
import nc.ui.pr.pub.PrTool;
import nc.ui.pu.pub.ATPForOneInvMulCorpUI;
import nc.ui.pu.pub.PuTool;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRadioButton;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListData;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillScrollPane;
import nc.ui.pub.bill.IBillRelaSortListener2;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.pub.workflownote.FlowStateDlg;
import nc.ui.scm.file.DocumentManager;
import nc.ui.scm.pub.FreeVOParse;
import nc.ui.scm.pub.def.DefSetTool;
import nc.ui.scm.service.LocalCallService;
import nc.ui.scm.sourcebill.SourceBillFlowDlg;
import nc.vo.bd.CorpVO;
import nc.vo.fts.pub.exception.FTSBusinessException;
import nc.vo.ml.AbstractNCLangRes;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pr.pray.PraybillHeaderVO;
import nc.vo.pr.pray.PraybillItemVO;
import nc.vo.pr.pray.PraybillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.datapower.BtnPowerVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.service.ServcallVO;
import nc.vo.sm.UserVO;

/**
 * 请购单审核界面控制
 * 
 * @author fans
 * 
 */
public class AuditUI extends ToftPanel implements ListSelectionListener,
		IBillRelaSortListener2 {
	protected ButtonObject[] m_buttons = {
			new ButtonObject(NCLangRes.getInstance().getStrByID("common",
					"UC001-0000041"), NCLangRes.getInstance().getStrByID(
					"common", "UC001-0000041"), 2, "全选"),
			new ButtonObject(NCLangRes.getInstance().getStrByID("common",
					"UC001-0000042"), NCLangRes.getInstance().getStrByID(
					"common", "UC001-0000042"), 2, "全消"),
			new ButtonObject(NCLangRes.getInstance().getStrByID("common",
					"UC001-0000027"), NCLangRes.getInstance().getStrByID(
					"common", "UC001-0000027"), 2, "审核"),
			new ButtonObject(NCLangRes.getInstance().getStrByID("common",
					"UC001-0000028"), NCLangRes.getInstance().getStrByID(
					"common", "UC001-0000028"), 2, "弃审"),
			new ButtonObject(NCLangRes.getInstance().getStrByID("common",
					"UC001-0000006"), NCLangRes.getInstance().getStrByID(
					"common", "UC001-0000006"), 2, "查询"),
			new ButtonObject(NCLangRes.getInstance().getStrByID("40040102",
					"UPT40040102-000001"), NCLangRes.getInstance().getStrByID(
					"40040102", "UPT40040102-000001"), 2, "状态查询"),
			new ButtonObject(NCLangRes.getInstance().getStrByID("SCMCOMMON",
					"UPPSCMCommon-000359"), NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000359"), 2, "存量查询"),
			new ButtonObject(NCLangRes.getInstance().getStrByID("SCMCOMMON",
					"UPPSCMCommon-000278"), NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000278"), 2, "文档管理"),
			new ButtonObject(NCLangRes.getInstance().getStrByID("SCMCOMMON",
					"UPPSCMCommon-000145"), NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000145"), 2, "联查") };

	protected int[] m_nButtonState = null;

	protected BillListPanel m_listPanel = null;

	protected UIComboBox m_comPraySource = null;

	protected UIComboBox m_comPrayType = null;

	protected String m_sUnitCode = getCorpPrimaryKey();

	protected int m_nPresentRecord = 0;

	protected PraybillVO[] m_VOs1 = null;

	protected PraybillVO[] m_VOs2 = null;

	protected AuditUIQueryDlg m_condClient = null;

	protected UIRadioButton m_rdoFree = null;

	protected UIRadioButton m_rdoAudited = null;

	protected String m_sAudit = null;

	protected int nMeasDecimal = 2;

	protected int nAssistUnitDecimal = 2;

	protected int nPriceDecimal = 2;

	protected int nMoneyDecimal = 2;

	protected int nExchangeDecimal = 2;

	private ATPForOneInvMulCorpUI m_atpDlg = null;

	private String[] saPkCorp = null;

	protected Hashtable hHead = new Hashtable();

	protected Hashtable hBody = new Hashtable();

	protected HashMap hashOldItems = null;

	public Object[] getRelaSortObjectArray() {
		if (this.m_rdoFree.isSelected()) {
			return this.m_VOs1;
		}
		return this.m_VOs2;
	}

	public AuditUI() {
		init();
	}

	protected void changeButtonState() {
		for (int i = 0; i < this.m_nButtonState.length; ++i) {
			if (this.m_nButtonState[i] == 0) {
				this.m_buttons[i].setVisible(true);
				this.m_buttons[i].setEnabled(true);
			}
			if (this.m_nButtonState[i] == 1) {
				this.m_buttons[i].setVisible(true);
				this.m_buttons[i].setEnabled(false);
			}
			if (this.m_nButtonState[i] == 2) {
				this.m_buttons[i].setVisible(false);
			}
			updateButton(this.m_buttons[i]);
		}
		for (int i = 0; i < this.m_buttons.length; ++i)
			updateButton(this.m_buttons[i]);
	}

	protected void changeQueryModelLayout() {
		this.m_rdoFree = new UIRadioButton();
		this.m_rdoFree.setText(NCLangRes.getInstance().getStrByID("40040102",
				"UPP40040102-000035"));

		this.m_rdoFree.setBackground(getBackground());
		this.m_rdoFree.setForeground(Color.black);
		this.m_rdoFree.setSize(110, this.m_rdoFree.getHeight());
		this.m_rdoFree.setSelected(true);

		this.m_rdoAudited = new UIRadioButton();
		this.m_rdoAudited.setText(NCLangRes.getInstance().getStrByID(
				"SCMCOMMON", "UPPSCMCommon-000301"));

		this.m_rdoAudited.setBackground(this.m_rdoFree.getBackground());
		this.m_rdoAudited.setForeground(this.m_rdoFree.getForeground());
		this.m_rdoAudited.setSize(this.m_rdoFree.getSize());

		this.m_rdoFree.setLocation(50, 30);
		this.m_rdoAudited.setLocation(this.m_rdoFree.getX(), this.m_rdoFree
				.getY()
				+ this.m_rdoFree.getHeight() + 20);

		ButtonGroup bg = new ButtonGroup();
		bg.add(this.m_rdoFree);
		bg.add(this.m_rdoAudited);
		bg.setSelected(this.m_rdoFree.getModel(), true);

		getQueryDlg().getUIPanelNormal().setLayout(null);

		getQueryDlg().getUIPanelNormal().add(this.m_rdoFree);
		getQueryDlg().getUIPanelNormal().add(this.m_rdoAudited);
	}

	private ATPForOneInvMulCorpUI getAtpDlg() {
		if (null == this.m_atpDlg) {
			this.m_atpDlg = new ATPForOneInvMulCorpUI(this);
		}
		return this.m_atpDlg;
	}

	protected BillListPanel getBillListPanel() {
		if (this.m_listPanel == null) {
			try {
				this.m_listPanel = new BillListPanel();
				this.m_listPanel.setName("ListPanel");
				try {
					this.m_listPanel.loadTemplet("2U", null,
							getClientEnvironment().getUser().getPrimaryKey(),
							this.m_sUnitCode);
				} catch (Exception e) {
					SCMEnv.out(e);
					this.m_listPanel.loadTemplet("40040102000000000001");
				}

				this.m_listPanel.getParentListPanel().setShowThMark(true);
				this.m_listPanel.getChildListPanel().setShowThMark(true);

				BillListData bd = this.m_listPanel.getBillListData();
				bd = initListDecimal(bd);
				this.m_listPanel.setListData(bd);

				this.m_listPanel.getHeadTable().setSelectionMode(2);

				DefSetTool.updateBillListPanelUserDef(this.m_listPanel,
						getClientEnvironment().getCorporation().getPk_corp(),
						"20", "vdef", "vdef");

				this.m_listPanel.getHeadTable().setCellSelectionEnabled(false);
				this.m_listPanel.getHeadTable().setSelectionMode(2);

				this.m_listPanel.getHeadTable().getSelectionModel()
						.addListSelectionListener(this);

				this.m_listPanel.hideHeadTableCol("cprojectidhead");

				this.m_listPanel.getHeadBillModel().addSortRelaObjectListener2(
						this);

				this.m_listPanel.updateUI();
			} catch (Throwable ivjExc) {
				SCMEnv.out(ivjExc);
				MessageDialog.showErrorDlg(this, NCLangRes.getInstance()
						.getStrByID("40040102", "UPP40040102-000000"),
						NCLangRes.getInstance().getStrByID("40040102",
								"UPP40040102-000001"));

				return null;
			}
		}
		return this.m_listPanel;
	}

	protected AuditUIQueryDlg getQueryDlg() {
		if (this.m_condClient == null) {
			this.m_condClient = new AuditUIQueryDlg(this, getCorpId(),
					"40040102");

			changeQueryModelLayout();

			this.m_condClient.hideCorp();
		}
		DefSetTool.updateQueryConditionClientUserDef(this.m_condClient,
				getCorpId(), "20", "po_praybill.def", "po_praybill_b.def");

		return this.m_condClient;
	}

	public String getTitle() {
		if (getBillListPanel().getBillListData().getTitle() != null) {
			return getBillListPanel().getBillListData().getTitle();
		}
		return NCLangRes.getInstance().getStrByID("40040102",
				"UPP40040102-000036");
	}

	public void init() {
		initPara();

		setButtons(this.m_buttons);

		setLayout(new BorderLayout());
		add(getBillListPanel(), "Center");

		getBillListPanel().hideHeadTableCol("cprojectidhead");
		getBillListPanel().hideHeadTableCol("cstoreorganization");
		getBillListPanel().hideHeadTableCol("cbiztype");
		getBillListPanel().hideHeadTableCol("cdeptid");
		getBillListPanel().hideHeadTableCol("cpraypsn");
		getBillListPanel().hideHeadTableCol("coperator");
		getBillListPanel().hideHeadTableCol("cauditpsn");

		DefSetTool.updateQueryConditionClientUserDef(this.m_condClient,
				this.m_sUnitCode, "20", "po_praybill.vdef",
				"po_praybill_b.vdef");

		this.m_nButtonState = new int[this.m_buttons.length];
		for (int i = 0; i < this.m_nButtonState.length; ++i) {
			this.m_nButtonState[i] = 1;
		}
		this.m_nButtonState[4] = 0;
		changeButtonState();

		initListener();
	}

	protected BillListData initListDecimal(BillListData bd) {
		BillItem item1 = bd.getBodyItem("nsuggestprice");
		item1.setDecimalDigits(this.nPriceDecimal);

		BillItem item2 = bd.getBodyItem("npraynum");
		item2.setDecimalDigits(this.nMeasDecimal);

		BillItem item3 = bd.getBodyItem("nassistnum");
		item3.setDecimalDigits(this.nAssistUnitDecimal);

		BillItem item4 = bd.getBodyItem("nexchangerate");
		item4.setDecimalDigits(this.nExchangeDecimal);

		BillItem item5 = bd.getBodyItem("nmoney");
		item5.setDecimalDigits(this.nMoneyDecimal);

		return bd;
	}

	protected void initListener() {
		readCard();
	}

	public void initPara() {
		try {
			ServcallVO[] scDisc = new ServcallVO[2];

			scDisc[0] = new ServcallVO();
			scDisc[0].setBeanName("nc.itf.pu.pub.IPub");
			scDisc[0].setMethodName("getDigitBatch");
			scDisc[0].setParameter(new Object[] { m_sUnitCode,
					new String[] { "BD502", "BD503", "BD501", "BD505" } });

			scDisc[0].setParameterTypes(new Class[] { String.class,
					String.class });

			scDisc[1] = new ServcallVO();
			scDisc[1].setBeanName("nc.itf.rc.receive.IArriveorder");
			scDisc[1].setMethodName("getCurrDecimal");
			scDisc[1].setParameter(new Object[] { this.m_sUnitCode });
			scDisc[1].setParameterTypes(new Class[] { String.class });

			Object[] oParaValue = LocalCallService.callService(scDisc);

			if ((oParaValue != null) && (oParaValue.length == scDisc.length)) {
				int[] iDigits = (int[]) (int[]) oParaValue[0];
				if ((iDigits != null) && (iDigits.length == 4)) {
					this.nAssistUnitDecimal = iDigits[0];
					this.nExchangeDecimal = iDigits[1];
					this.nMeasDecimal = iDigits[2];
					this.nPriceDecimal = iDigits[3];
				}

				this.nMoneyDecimal = ((Integer) oParaValue[1]).intValue();
			}

		} catch (Exception e) {
			reportException(e);
		}
	}

	public void onAudit() {
		int iCnt = 0;
		showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON",
				"UPPSCMCommon-000403"));

		Integer[] nSelected = null;
		Vector v = new Vector();
		Vector vv = new Vector();
		int nRow = getBillListPanel().getHeadBillModel().getRowCount();
		for (int i = 0; i < nRow; ++i) {
			int nStatus = getBillListPanel().getHeadBillModel().getRowState(i);
			if (nStatus == 4)
				v.addElement(new Integer(i));
			else
				vv.addElement(new Integer(i));
		}
		nSelected = new Integer[v.size()];
		v.copyInto(nSelected);

		if ((nSelected == null) || (nSelected.length == 0)) {
			MessageDialog
					.showErrorDlg(this, NCLangRes.getInstance().getStrByID(
							"40040102", "UPP40040102-000002"), NCLangRes
							.getInstance().getStrByID("40040102",
									"UPP40040102-000003"));

			return;
		}
		long tTime = System.currentTimeMillis();
		Vector vTemp = new Vector();
		int iPosReal = 0;
		for (int i = 0; i < nSelected.length; ++i) {
			iPosReal = PuTool.getIndexBeforeSort(getBillListPanel(),
					nSelected[i].intValue());

			PraybillVO vo = this.m_VOs1[iPosReal];
			vo.getHeadVO().setCuserid(
					getClientEnvironment().getUser().getPrimaryKey());

			vTemp.addElement(vo);
		}

		PraybillVO[] VOs = new PraybillVO[vTemp.size()];
		vTemp.copyInto(VOs);

		Vector v_ids = new Vector();
		for (int i = 0; i < VOs.length; ++i) {
			PraybillHeaderVO headVO = VOs[i].getHeadVO();

			if (headVO.getDpraydate().toString().compareTo(
					getClientEnvironment().getDate().toString()) > 0) {
				v_ids.add(headVO.getVpraycode());
			}
			headVO.setCauditpsn(getClientEnvironment().getUser()
					.getPrimaryKey());

			headVO.setDauditdate(getClientEnvironment().getDate());
		}

		if (v_ids.size() > 0) {
			String message = "";
			int i = 0;
			for (int len = v_ids.size(); i < len; ++i) {
				message = message + v_ids.get(i).toString() + ", ";
			}
			showErrorMessage(NCLangRes4VoTransl.getNCLangRes().getStrByID(
					"4004pub",
					"UPP4004pub-000199",
					null,
					new String[] {
							NCLangRes4VoTransl.getNCLangRes().getStrByID(
									"common", "UC000-0003665"),
							message.substring(0, message.length() - 2),
							NCLangRes4VoTransl.getNCLangRes().getStrByID(
									"common", "UC000-0003665") }));

			return;
		}

		try {
			String strOpr = getClientEnvironment().getUser().getPrimaryKey();
			for (int i = 0; i < VOs.length; ++i) {
				VOs[i].setCurrOperator(strOpr);
				VOs[i].getHeadVO().setCuserid(strOpr);
				VOs[i].getHeadVO().setTaudittime(
						new UFDateTime(new Date()).toString());
			}

			VOs = PrTool.getRefreshedVOs(VOs);
			PfUtilClient.processBatchFlow(this, "APPROVE", "20",
					getClientEnvironment().getDate().toString(), VOs, null);

			if (!PfUtilClient.isSuccess()) {
				return;
			}
			iCnt = VOs.length;
		} catch (SQLException e) {
			MessageDialog.showErrorDlg(this, NCLangRes.getInstance()
					.getStrByID("40040102", "UPP40040102-000002"), NCLangRes
					.getInstance().getStrByID("SCMCOMMON",
							"UPPSCMCommon-000412"));

			SCMEnv.out(e);
			return;
		} catch (ArrayIndexOutOfBoundsException e) {
			MessageDialog.showErrorDlg(this, NCLangRes.getInstance()
					.getStrByID("40040102", "UPP40040102-000002"), NCLangRes
					.getInstance().getStrByID("SCMCOMMON",
							"UPPSCMCommon-000426"));

			SCMEnv.out(e);
			return;
		} catch (NullPointerException e) {
			MessageDialog.showErrorDlg(this, NCLangRes.getInstance()
					.getStrByID("40040102", "UPP40040102-000002"), NCLangRes
					.getInstance().getStrByID("SCMCOMMON",
							"UPPSCMCommon-000427"));

			SCMEnv.out(e);
			return;
		} catch (BusinessException e) {
			MessageDialog.showErrorDlg(this, NCLangRes.getInstance()
					.getStrByID("40040102", "UPP40040102-000002"), e
					.getMessage());

			SCMEnv.out(e);
			return;
		} catch (Exception e) {
			MessageDialog.showErrorDlg(this, NCLangRes.getInstance()
					.getStrByID("40040102", "UPP40040102-000002"), e
					.getMessage());

			SCMEnv.out(e);
			return;
		}
		tTime = System.currentTimeMillis() - tTime;
		SCMEnv.out("请购单审批时间：" + tTime + " ms!");
		showHintMessage(NCLangRes.getInstance().getStrByID("40040102",
				"UPP40040102-000004"));

		if ((vv == null) || (vv.size() == 0)) {
			getBillListPanel().getHeadBillModel().clearBodyData();
			getBillListPanel().getBodyBillModel().clearBodyData();
			getBillListPanel().updateUI();

			for (int i = 0; i < this.m_buttons.length; ++i) {
				this.m_nButtonState[i] = 1;
			}
			this.m_nButtonState[4] = 0;
			this.m_nButtonState[6] = 0;
			changeButtonState();
			return;
		}

		Vector v0 = new Vector();
		for (int i = 0; i < vv.size(); ++i) {
			int n = ((Integer) vv.elementAt(i)).intValue();

			n = PuTool.getIndexBeforeSort(getBillListPanel(), n);
			v0.addElement(this.m_VOs1[n]);
		}
		this.m_VOs1 = new PraybillVO[v0.size()];
		v0.copyInto(this.m_VOs1);
		Vector v00 = new Vector();
		for (int i = 0; i < this.m_VOs1.length; ++i)
			v00.addElement(this.m_VOs1[i].getHeadVO());
		PraybillHeaderVO[] hVO = new PraybillHeaderVO[v00.size()];
		v00.copyInto(hVO);
		PraybillItemVO[] bVO = this.m_VOs1[0].getBodyVO();
		getBillListPanel().getHeadBillModel().setBodyDataVO(hVO);
		getBillListPanel().getHeadBillModel().execLoadFormula();
		getBillListPanel().getBodyBillModel().setBodyDataVO(bVO);
		PuTool.loadInvMaxPrice(getBillListPanel());
		getBillListPanel().getBodyBillModel().execLoadFormula();

		PuTool.loadSourceInfoAll(getBillListPanel(), "20");

		getBillListPanel().getHeadBillModel().updateValue();
		getBillListPanel().getBodyBillModel().updateValue();

		getBillListPanel().getHeadTable().setRowSelectionInterval(0, 0);
		getBillListPanel().updateUI();
		if (iCnt == 0) {
			showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON",
					"UPPSCMCommon-000404"));
		} else {
			String[] value = { String.valueOf(iCnt) };

			showHintMessage(NCLangRes.getInstance().getStrByID("40040102",
					"UPPSCMCommon-000405", null, value));
		}

		showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON",
				"UPPSCMCommon-000236"));
	}

	public void onButtonClicked(ButtonObject bo) {
		if (bo == this.m_buttons[0])
			onSelectAll();
		else if (bo == this.m_buttons[1])
			onSelectNo();
		else if (bo == this.m_buttons[2])
			onAudit();
		else if (bo == this.m_buttons[3])
			onUnAudit();
		else if (bo == this.m_buttons[4])
			onQuery();
		else if (bo == this.m_buttons[5])
			onQueryForAudit();
		else if (bo == this.m_buttons[6])
			onQueryInvOnHand();
		else if (bo == this.m_buttons[7])
			onDocument();
		else if (bo == this.m_buttons[8])
			onLnkQuery();
			}

	private void onLnkQuery() {
		PraybillHeaderVO head = null;
		head = (PraybillHeaderVO) getBillListPanel().getHeadBillModel()
				.getBodySelectedVOs(PraybillHeaderVO.class.getName())[0];

		if ((head == null)
				|| (head.getAttributeValue("pk_corp") == null)
				|| (head.getAttributeValue("pk_corp").toString().trim()
						.equals(""))) {
			showHintMessage(NCLangRes.getInstance().getStrByID("40040102",
					"UPP40040102-000006"));

			return;
		}
		SourceBillFlowDlg soureDlg = new SourceBillFlowDlg(this, "20", head
				.getPrimaryKey(), null, getClientEnvironment().getUser()
				.getPrimaryKey(), getCorpId());

		soureDlg.showModal();
		showHintMessage(NCLangRes.getInstance().getStrByID("common",
				"4004COMMON000000019"));
	}

	private String getCorpId() {
		String corpid = null;
		corpid = getClientEnvironment().getCorporation().getPrimaryKey();
		if ((corpid == null) || (corpid.trim().equals(""))) {
			corpid = getCorpPrimaryKey();
		}
		return corpid;
	}

	private void onDocument() {
		String[] strPks = null;
		String[] strCodes = null;
		HashMap mapBtnPowerVo = new HashMap();
		Integer iBillStatus = null;
		PraybillVO[] m_VOs = null;
		if (this.m_rdoFree.isSelected())
			m_VOs = this.m_VOs1;
		else {
			m_VOs = this.m_VOs2;
		}
		if ((m_VOs != null) && (m_VOs.length > 0)) {
			PraybillHeaderVO[] headers = null;
			headers = (PraybillHeaderVO[]) (PraybillHeaderVO[]) getBillListPanel()
					.getHeadBillModel().getBodySelectedVOs(
							PraybillHeaderVO.class.getName());

			if ((headers == null) || (headers.length <= 0)) {
				showHintMessage(NCLangRes.getInstance().getStrByID("40040102",
						"UPP40040102-000007"));

				return;
			}
			strPks = new String[headers.length];
			strCodes = new String[headers.length];
			BtnPowerVO pVo = null;
			for (int i = 0; i < headers.length; ++i) {
				strPks[i] = headers[i].getPrimaryKey();
				strCodes[i] = headers[i].getVpraycode();

				pVo = new BtnPowerVO(strCodes[i]);
				iBillStatus = PuPubVO.getInteger_NullAs(headers[i]
						.getIbillstatus(), new Integer(0));

				if ((iBillStatus.intValue() == 1)
						|| (iBillStatus.intValue() == 2)
						|| (iBillStatus.intValue() == 3)) {
					pVo.setFileDelEnable("false");
				}
				mapBtnPowerVo.put(strCodes[i], pVo);
			}
		}
		if ((strPks == null) || (strPks.length <= 0)) {
			showHintMessage(NCLangRes.getInstance().getStrByID("40040102",
					"UPP40040102-000007"));

			return;
		}

		DocumentManager.showDM(this, strPks, strCodes, mapBtnPowerVo);

		showHintMessage(NCLangRes.getInstance().getStrByID("common",
				"4004COMMON000000025"));
	}

	public void onQuery() {
		showHintMessage(NCLangRes.getInstance().getStrByID("40040102",
				"UPP40040102-000008"));

		int iCnt = 0;
		getQueryDlg().showModal();
		if (getQueryDlg().isCloseOK()) {
			ConditionVO[] conditionVO = getQueryDlg().getConditionVO();

			if ((conditionVO != null) && (conditionVO.length > 0)) {
				Vector v = new Vector();
				for (int i = 0; i < conditionVO.length; ++i)
					v.addElement(conditionVO[i]);
				ConditionVO tempVO = new ConditionVO();
				tempVO.setFieldName("操作员");
				tempVO.setFieldCode("");
				tempVO.setOperaCode("");
				tempVO.setValue(getClientEnvironment().getUser()
						.getPrimaryKey());

				v.addElement(tempVO);

				conditionVO = new ConditionVO[v.size()];
				v.copyInto(conditionVO);
			}
			if (this.m_rdoFree.isSelected())
				this.m_sAudit = "N";
			else
				this.m_sAudit = "Y";
			try {
				if (this.m_sAudit.toUpperCase().equals("N")) {
					
					//edit by yqq 2017-05-17  审批请购单查询未审核的单据时需将送审的单据也一起查询处理
					/*this.m_VOs1 = PraybillHelper.queryAll(getQueryDlg()
							.getSelectedCorpIdString(), conditionVO,
							" ibillstatus = 0  ", getClientEnvironment()
									.getUser().getPrimaryKey(), null);*/
					
					this.m_VOs1 = PraybillHelper.queryAll(getQueryDlg()
							.getSelectedCorpIdString(), conditionVO,
							" ibillstatus = 0 or  ibillstatus = 2", getClientEnvironment()
									.getUser().getPrimaryKey(), null);
					//end by yqq 2017-05-17
					
				} else {
					this.m_VOs2 = PraybillHelper.queryAll(getQueryDlg()
							.getSelectedCorpIdString(), conditionVO,
							" ibillstatus = 3 ", getClientEnvironment()
									.getUser().getPrimaryKey(), null);
				}
			} catch (Exception e) {
				MessageDialog.showErrorDlg(this, NCLangRes.getInstance()
						.getStrByID("40040102", "UPP40040102-000009"), e
						.getMessage());

				SCMEnv.out(e);
				return;
			}

			getBillListPanel().getBillListData().getHeadItem("ipraysource")
					.setWithIndex(true);

			this.m_comPraySource = ((UIComboBox) getBillListPanel()
					.getBillListData().getHeadItem("ipraysource")
					.getComponent());

			this.m_comPraySource.setTranslate(true);
			this.m_comPraySource.addItem("MRP");
			this.m_comPraySource.addItem("MO");
			this.m_comPraySource.addItem("SFC");
			this.m_comPraySource.addItem(NCLangRes.getInstance().getStrByID(
					"40040102", "UPP40040102-000037"));

			this.m_comPraySource.addItem(NCLangRes.getInstance().getStrByID(
					"40040102", "UPP40040102-000038"));

			this.m_comPraySource.addItem(NCLangRes.getInstance().getStrByID(
					"40040102", "UPP40040102-000039"));

			this.m_comPraySource.addItem("DRP");
			this.m_comPraySource.addItem(NCLangRes.getInstance().getStrByID(
					"40040102", "UPP40040102-000040"));

			this.m_comPraySource.addItem(NCLangRes.getInstance().getStrByID(
					"4004pub", "UPP4004pub-000204"));

			getBillListPanel().getBillListData().getHeadItem("ipraytype")
					.setWithIndex(true);

			this.m_comPrayType = ((UIComboBox) getBillListPanel()
					.getBillListData().getHeadItem("ipraytype").getComponent());

			this.m_comPrayType.setTranslate(true);
			this.m_comPrayType.addItem(NCLangRes.getInstance().getStrByID(
					"40040102", "UPP40040102-000045"));

			this.m_comPrayType.addItem(NCLangRes.getInstance().getStrByID(
					"40040102", "UPP40040102-000046"));

			this.m_comPrayType.addItem(NCLangRes.getInstance().getStrByID(
					"40040102", "UPP40040102-000043"));

			this.m_comPrayType.addItem(NCLangRes.getInstance().getStrByID(
					"40040102", "UPP40040102-000044"));

			if (this.m_sAudit.toUpperCase().equals("N")) {
				if ((this.m_VOs1 != null) && (this.m_VOs1.length > 0)) {
					iCnt = this.m_VOs1.length;

					this.m_nPresentRecord = 0;

					Vector v = new Vector();
					for (int i = 0; i < this.m_VOs1.length; ++i)
						v.addElement(this.m_VOs1[i].getHeadVO());
					PraybillHeaderVO[] hVO = new PraybillHeaderVO[v.size()];
					v.copyInto(hVO);

					Integer nTemp1 = (Integer) this.m_VOs1[this.m_nPresentRecord]
							.getParentVO().getAttributeValue("ipraysource");

					Integer nTemp2 = (Integer) this.m_VOs1[this.m_nPresentRecord]
							.getParentVO().getAttributeValue("ipraytype");

					this.m_comPraySource.setSelectedIndex(nTemp1.intValue());
					this.m_comPrayType.setSelectedIndex(nTemp2.intValue());

					getBillListPanel().getHeadBillModel().setBodyDataVO(hVO);
					getBillListPanel().getHeadBillModel().execLoadFormula();

					PuTool.loadSourceInfoAll(getBillListPanel(), "20");

					getBillListPanel().getHeadBillModel().updateValue();
					getBillListPanel().updateUI();
					getBillListPanel().getHeadTable().setRowSelectionInterval(
							0, 0);

					for (int i = 0; i < hVO.length; ++i) {
						getBillListPanel().getHeadBillModel().setValueAt(
								hVO[i].getVmemo(), i, "vmemo");
					}
				} else {
					this.m_VOs1 = null;
					MessageDialog.showHintDlg(this, NCLangRes.getInstance()
							.getStrByID("40040102", "UPP40040102-000009"),
							NCLangRes.getInstance().getStrByID("40040102",
									"UPP40040102-000010"));

					getBillListPanel().getHeadBillModel().clearBodyData();
					getBillListPanel().getBodyBillModel().clearBodyData();
					getBillListPanel().updateUI();

					for (int i = 0; i < this.m_buttons.length; ++i) {
						this.m_nButtonState[i] = 1;
					}
					this.m_nButtonState[4] = 0;
				}
				changeButtonState();
			} else {
				if ((this.m_VOs2 != null) && (this.m_VOs2.length > 0)) {
					iCnt = this.m_VOs2.length;

					this.m_nPresentRecord = 0;

					Vector v = new Vector();
					for (int i = 0; i < this.m_VOs2.length; ++i)
						v.addElement(this.m_VOs2[i].getHeadVO());
					PraybillHeaderVO[] hVO = new PraybillHeaderVO[v.size()];
					v.copyInto(hVO);
					getBillListPanel().getBillListData().getHeadItem(
							"ipraysource").setWithIndex(true);

					this.m_comPraySource = ((UIComboBox) getBillListPanel()
							.getBillListData().getHeadItem("ipraysource")
							.getComponent());

					this.m_comPraySource.setTranslate(true);

					Integer nTemp1 = (Integer) this.m_VOs2[this.m_nPresentRecord]
							.getParentVO().getAttributeValue("ipraysource");

					Integer nTemp2 = (Integer) this.m_VOs2[this.m_nPresentRecord]
							.getParentVO().getAttributeValue("ipraytype");

					this.m_comPraySource.setSelectedIndex(nTemp1.intValue());
					this.m_comPrayType.setSelectedIndex(nTemp2.intValue());

					getBillListPanel().getHeadBillModel().setBodyDataVO(hVO);
					getBillListPanel().getHeadBillModel().execLoadFormula();

					PuTool.loadSourceInfoAll(getBillListPanel(), "20");

					getBillListPanel().getHeadBillModel().updateValue();
					getBillListPanel().updateUI();
					getBillListPanel().getHeadTable().setRowSelectionInterval(
							0, 0);

					for (int i = 0; i < hVO.length; ++i) {
						getBillListPanel().getHeadBillModel().setValueAt(
								hVO[i].getVmemo(), i, "vmemo");
					}
				} else {
					this.m_VOs2 = null;
					MessageDialog.showHintDlg(this, NCLangRes.getInstance()
							.getStrByID("40040102", "UPP40040102-000009"),
							NCLangRes.getInstance().getStrByID("40040102",
									"UPP40040102-000010"));

					getBillListPanel().getHeadBillModel().clearBodyData();
					getBillListPanel().getBodyBillModel().clearBodyData();
					getBillListPanel().updateUI();

					for (int i = 0; i < this.m_buttons.length; ++i) {
						this.m_nButtonState[i] = 1;
					}
					this.m_nButtonState[4] = 0;
				}
				changeButtonState();
			}
		}
		if (iCnt == 0) {
			showHintMessage(NCLangRes.getInstance().getStrByID("40040102",
					"UPP40040102-000011"));
		} else {
			String[] value = { String.valueOf(iCnt) };

			showHintMessage(NCLangRes.getInstance().getStrByID("40040102",
					"UPP40040102-000025", null, value));
		}

		showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH009"));
	}

	protected void onQueryForAudit() {
		PraybillVO[] vos = this.m_VOs1;
		if ("Y".equals(this.m_sAudit)) {
			vos = this.m_VOs2;
		}

		if ((vos != null) && (vos.length > 0)) {
			FlowStateDlg approvestatedlg = new FlowStateDlg(this, "20",
					vos[this.m_nPresentRecord].getHeadVO().getPrimaryKey());

			approvestatedlg.showModal();
		}
		showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH035"));
	}

	protected void onQueryInvOnHand() {
		PraybillVO voPara = null;
		PraybillItemVO item = null;
		PraybillItemVO[] items = null;

		PraybillHeaderVO head = null;
		if ((getBillListPanel().getHeadBillModel().getBodySelectedVOs(
				PraybillHeaderVO.class.getName()) == null)
				|| (getBillListPanel().getHeadBillModel().getBodySelectedVOs(
						PraybillHeaderVO.class.getName()).length <= 0)) {
			showHintMessage(NCLangRes.getInstance().getStrByID("40040102",
					"UPP40040102-000013"));

			return;
		}
		head = (PraybillHeaderVO) getBillListPanel().getHeadBillModel()
				.getBodySelectedVOs(PraybillHeaderVO.class.getName())[0];

		if ((head == null)
				|| (head.getAttributeValue("pk_corp") == null)
				|| (head.getAttributeValue("pk_corp").toString().trim()
						.equals(""))) {
			showHintMessage(NCLangRes.getInstance().getStrByID("40040102",
					"UPP40040102-000014"));

			return;
		}

		int[] iSelRows = getBillListPanel().getBodyTable().getSelectedRows();
		if ((iSelRows != null) && (iSelRows.length > 0)) {
			item = (PraybillItemVO) getBillListPanel().getBodyBillModel()
					.getBodyValueRowVO(iSelRows[0],
							PraybillItemVO.class.getName());
		} else {
			items = (PraybillItemVO[]) (PraybillItemVO[]) getBillListPanel()
					.getBodyBillModel().getBodyValueVOs(
							PraybillItemVO.class.getName());

			if ((items == null) || (items.length <= 0)) {
				showHintMessage(NCLangRes.getInstance().getStrByID("40040102",
						"UPP40040102-000015"));

				return;
			}
			item = items[0];
		}

		if ((item.getAttributeValue("cinventoryid") == null)
				|| (item.getAttributeValue("cinventoryid").toString().trim()
						.equals(""))
				|| (item.getAttributeValue("ddemanddate") == null)
				|| (item.getAttributeValue("ddemanddate").toString().trim()
						.equals(""))) {
			showHintMessage(NCLangRes.getInstance().getStrByID("40040102",
					"UPP40040102-000015"));

			return;
		}

		voPara = new PraybillVO();
		voPara.setParentVO(head);
		voPara.setChildrenVO(new PraybillItemVO[] { item });

		if (this.saPkCorp == null) {
			try {
				IUserManageQuery myService = (IUserManageQuery) NCLocator
						.getInstance().lookup(IUserManageQuery.class.getName());

				CorpVO[] vos = myService
						.queryAllCorpsByUserPK(getClientEnvironment().getUser()
								.getPrimaryKey());

				if ((vos == null) || (vos.length == 0)) {
					SCMEnv.out("未查询到有权限公司，直接返回!");
					return;
				}
				int iLen = vos.length;
				this.saPkCorp = new String[iLen];
				for (int i = 0; i < iLen; ++i)
					this.saPkCorp[i] = vos[i].getPrimaryKey();
			} catch (Exception e) {
				MessageDialog.showErrorDlg(this, NCLangRes.getInstance()
						.getStrByID("40040102", "UPP40040102-000016"),
						NCLangRes.getInstance().getStrByID("40040102",
								"UPP40040102-000017"));

				SCMEnv.out(e);
				return;
			}
		}
		getAtpDlg().setPkCorps(this.saPkCorp);

		getAtpDlg().initData(voPara);
		getAtpDlg().showModal();

		showHintMessage(NCLangRes.getInstance().getStrByID("common",
				"4004COMMON000000032"));
	}

	public void onSelectAll() {
		int iLen = getBillListPanel().getHeadBillModel().getRowCount();
		if (iLen > 0) {
			getBillListPanel().getHeadTable().setRowSelectionInterval(0,
					iLen - 1);

			for (int i = 0; i < iLen; ++i) {
				getBillListPanel().getHeadBillModel().setRowState(i, 4);
			}
		}

		showHintMessage(NCLangRes.getInstance().getStrByID("common",
				"4004COMMON000000033"));
	}

	public void onSelectNo() {
		getBillListPanel().getHeadTable().clearSelection();
		showHintMessage(NCLangRes.getInstance().getStrByID("common",
				"4004COMMON000000034"));
	}

	protected void onUnAudit() {
		int iCnt = 0;
		showHintMessage(NCLangRes.getInstance().getStrByID("40040102",
				"UPP40040102-000018"));

		Integer[] nSelected = null;
		Vector v = new Vector();
		Vector vv = new Vector();
		int nRow = getBillListPanel().getHeadBillModel().getRowCount();
		for (int i = 0; i < nRow; ++i) {
			int nStatus = getBillListPanel().getHeadBillModel().getRowState(i);
			if (nStatus == 4)
				v.addElement(new Integer(i));
			else
				vv.addElement(new Integer(i));
		}
		nSelected = new Integer[v.size()];
		v.copyInto(nSelected);
		if ((nSelected == null) || (nSelected.length == 0)) {
			MessageDialog
					.showErrorDlg(this, NCLangRes.getInstance().getStrByID(
							"40040102", "UPP40040102-000019"), NCLangRes
							.getInstance().getStrByID("40040102",
									"UPP40040102-000003"));

			return;
		}
		long tTime = System.currentTimeMillis();
		Vector vTemp = new Vector();
		int iPosReal = 0;
		for (int i = 0; i < nSelected.length; ++i) {
			iPosReal = PuTool.getIndexBeforeSort(getBillListPanel(),
					nSelected[i].intValue());

			PraybillVO vo = this.m_VOs2[iPosReal];
			vo.getHeadVO().setCuserid(
					getClientEnvironment().getUser().getPrimaryKey());

			vo.getHeadVO().setCauditpsn(
					getClientEnvironment().getUser().getPrimaryKey());

			vTemp.addElement(vo);
		}
		PraybillVO[] VOs = new PraybillVO[vTemp.size()];
		vTemp.copyInto(VOs);

		String versions = getVersionOfCantUnaudit(VOs);
		if ((versions != null) && (versions.trim().length() > 0)) {
			showErrorMessage(NCLangRes4VoTransl.getNCLangRes()
					.getStrByID(
							"40040101",
							"UPP40040101-000527",
							null,
							new String[] { versions.substring(0, versions
									.length() - 2) }));

			return;
		}

		try {
			String strOpr = getClientEnvironment().getUser().getPrimaryKey();
			for (int i = 0; i < VOs.length; ++i) {
				VOs[i].setCurrOperator(strOpr);
				VOs[i].getHeadVO().setCuserid(strOpr);
				VOs[i].getHeadVO().setTaudittime(null);
			}

			VOs = PrTool.getRefreshedVOs(VOs);
			PfUtilClient.processBatch(this, "UNAPPROVE", "20",
					getClientEnvironment().getDate().toString(), VOs, null);

			if (!PfUtilClient.isSuccess()) {
				return;
			}
			iCnt = VOs.length;
		} catch (SQLException e) {
			MessageDialog.showErrorDlg(this, NCLangRes.getInstance()
					.getStrByID("40040102", "UPP40040102-000020"), NCLangRes
					.getInstance().getStrByID("SCMCOMMON",
							"UPPSCMCommon-000412"));

			SCMEnv.out(e);
			return;
		} catch (ArrayIndexOutOfBoundsException e) {
			MessageDialog.showErrorDlg(this, NCLangRes.getInstance()
					.getStrByID("40040102", "UPP40040102-000020"), NCLangRes
					.getInstance().getStrByID("SCMCOMMON",
							"UPPSCMCommon-000426"));

			SCMEnv.out(e);
			return;
		} catch (NullPointerException e) {
			MessageDialog.showErrorDlg(this, NCLangRes.getInstance()
					.getStrByID("40040102", "UPP40040102-000020"), NCLangRes
					.getInstance().getStrByID("SCMCOMMON",
							"UPPSCMCommon-000427"));

			SCMEnv.out(e);
			return;
		} catch (BusinessException e) {
			MessageDialog.showErrorDlg(this, NCLangRes.getInstance()
					.getStrByID("40040102", "UPP40040102-000020"), e
					.getMessage());

			SCMEnv.out(e);
			return;
		} catch (Exception e) {
			MessageDialog.showErrorDlg(this, NCLangRes.getInstance()
					.getStrByID("40040102", "UPP40040102-000020"), e
					.getMessage());

			SCMEnv.out(e);
			return;
		}
		tTime = System.currentTimeMillis() - tTime;
		SCMEnv.out("取消审批时间：" + tTime + " ms!");
		showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON",
				"UPPSCMCommon-000406"));

		if ((vv == null) || (vv.size() == 0)) {
			getBillListPanel().getHeadBillModel().clearBodyData();
			getBillListPanel().getBodyBillModel().clearBodyData();
			getBillListPanel().updateUI();

			for (int i = 0; i < this.m_buttons.length; ++i) {
				this.m_nButtonState[i] = 1;
			}
			this.m_nButtonState[4] = 0;
			changeButtonState();
			return;
		}

		Vector v0 = new Vector();
		for (int i = 0; i < vv.size(); ++i) {
			int n = ((Integer) vv.elementAt(i)).intValue();

			n = PuTool.getIndexBeforeSort(getBillListPanel(), n);
			v0.addElement(this.m_VOs2[n]);
		}
		this.m_VOs2 = new PraybillVO[v0.size()];
		v0.copyInto(this.m_VOs2);
		Vector v00 = new Vector();
		for (int i = 0; i < this.m_VOs2.length; ++i)
			v00.addElement(this.m_VOs2[i].getHeadVO());
		PraybillHeaderVO[] hVO = new PraybillHeaderVO[v00.size()];
		v00.copyInto(hVO);
		PraybillItemVO[] bVO = this.m_VOs2[0].getBodyVO();
		getBillListPanel().getHeadBillModel().setBodyDataVO(hVO);
		getBillListPanel().getHeadBillModel().execLoadFormula();
		getBillListPanel().getBodyBillModel().setBodyDataVO(bVO);
		PuTool.loadInvMaxPrice(getBillListPanel());
		getBillListPanel().getBodyBillModel().execLoadFormula();

		PuTool.loadSourceInfoAll(getBillListPanel(), "20");

		getBillListPanel().getHeadBillModel().updateValue();
		getBillListPanel().getBodyBillModel().updateValue();

		getBillListPanel().getHeadTable().setRowSelectionInterval(0, 0);
		getBillListPanel().updateUI();
		if (iCnt == 0) {
			showHintMessage(NCLangRes.getInstance().getStrByID("40040102",
					"UPP40040102-000021"));
		} else {
			String[] value = { String.valueOf(iCnt) };
			showHintMessage(NCLangRes.getInstance().getStrByID("40040102",
					"UPP40040102-000026", null, value));
		}

		showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH011"));
	}

	private String getVersionOfCantUnaudit(PraybillVO[] vos) {
		String versions = "";

		int i = 0;
		for (int len = vos.length; i < len; ++i) {
			if (vos[i].getHeadVO().getNversion().intValue() > 1) {
				versions = versions + vos[i].getHeadVO().getVpraycode() + ", ";
			}
		}
		return versions;
	}

	protected String refreshPraybillStatus(PraybillVO[] VOs) {
		StringBuffer sMessage = new StringBuffer();
		try {
			if ((VOs != null) && (VOs.length > 0)) {
				Vector vStatus = PraybillHelper.queryPraybillStatus(VOs);
				for (int i = 0; i < vStatus.size(); ++i) {
					Vector vTemp = (Vector) vStatus.elementAt(i);
					int nStatus = ((Integer) vTemp.elementAt(0)).intValue();
					if (nStatus == 1) {
						String[] value = { String.valueOf(i + 1) };
						sMessage.append(NCLangRes.getInstance().getStrByID(
								"40040102", "UPP40040102-000027", null, value));
					}

					if (nStatus == 3) {
						String[] value = { String.valueOf(i + 1) };
						sMessage.append(NCLangRes.getInstance().getStrByID(
								"40040102", "UPP40040102-000028", null, value));
					}

					if (nStatus == 4) {
						String[] value = { String.valueOf(i + 1) };
						sMessage.append(NCLangRes.getInstance().getStrByID(
								"40040102", "UPP40040102-000029", null, value));
					}

					nStatus = ((Integer) vTemp.elementAt(1)).intValue();
					if (nStatus == 1) {
						String[] value = { String.valueOf(i + 1) };
						sMessage.append(NCLangRes.getInstance().getStrByID(
								"40040102", "UPP40040102-000030", null, value));
					}

				}

			}

		} catch (Exception e) {
			SCMEnv.out(e);
			MessageDialog.showErrorDlg(this, NCLangRes.getInstance()
					.getStrByID("40040102", "UPP40040102-000024"), e
					.getMessage());
		}

		return sMessage.toString();
	}

	protected void setListBtnsWhenErr() {
		for (int i = 0; i < this.m_nButtonState.length; ++i) {
			this.m_nButtonState[i] = 0;
		}

		this.m_nButtonState[2] = 1;
		this.m_nButtonState[3] = 1;
		this.m_nButtonState[5] = 1;
		this.m_nButtonState[7] = 1;
		this.m_nButtonState[8] = 1;
	}

	public void valueChanged(ListSelectionEvent e) {
		boolean isErr = false;

		if (e.getValueIsAdjusting()) {
			return;
		}
		int iSelCnt = 0;

		int iCount = getBillListPanel().getHeadTable().getRowCount();
		for (int i = 0; i < iCount; ++i) {
			getBillListPanel().getHeadBillModel().setRowState(i, 0);
		}

		int[] selectedRows = getBillListPanel().getHeadTable()
				.getSelectedRows();

		if (selectedRows != null)
			iSelCnt = selectedRows.length;
		if (iSelCnt > 0) {
			for (int i = 0; i < iSelCnt; ++i) {
				getBillListPanel().getHeadBillModel().setRowState(
						selectedRows[i], 4);
			}

		}

		if (iSelCnt != 1) {
			getBillListPanel().getBodyBillModel().setBodyDataVO(null);
		} else {
			if (iSelCnt == 1)
				this.m_nPresentRecord = selectedRows[0];
			this.m_nPresentRecord = PuTool.getIndexBeforeSort(
					getBillListPanel(), this.m_nPresentRecord);

			String strId = null;
			PraybillItemVO[] bodyVO = null;

			if (this.m_sAudit.toUpperCase().equals("N")) {
				strId = this.m_VOs1[this.m_nPresentRecord].getHeadVO()
						.getPrimaryKey();
				if (this.hBody.containsKey(strId))
					this.m_VOs1[this.m_nPresentRecord]
							.setChildrenVO((PraybillItemVO[]) (PraybillItemVO[]) this.hBody
									.get(strId));
				else {
					try {
						this.m_VOs1[this.m_nPresentRecord] = PrTool
								.getRefreshedVO(this.m_VOs1[this.m_nPresentRecord]);

						if (this.hashOldItems == null) {
							this.hashOldItems = new HashMap();
						}
						this.hashOldItems
								.put(
										strId,
										(PraybillItemVO[]) (PraybillItemVO[]) this.m_VOs1[this.m_nPresentRecord]
												.getChildrenVO());
					} catch (Exception be) {
						if (be instanceof BusinessException) {
							MessageDialog.showErrorDlg(this, NCLangRes
									.getInstance().getStrByID("SCMCOMMON",
											"UPPSCMCommon-000422"), be
									.getMessage());
						}

						isErr = true;
					}
				}

				getBillListPanel().getHeadBillModel().setBodyRowVO(
						this.m_VOs1[this.m_nPresentRecord].getHeadVO(),
						selectedRows[0]);

				getBillListPanel().getHeadBillModel().execLoadFormula();
				bodyVO = this.m_VOs1[this.m_nPresentRecord].getBodyVO();
			} else {
				strId = this.m_VOs2[this.m_nPresentRecord].getHeadVO()
						.getPrimaryKey();
				if (this.hBody.containsKey(strId))
					this.m_VOs2[this.m_nPresentRecord]
							.setChildrenVO((PraybillItemVO[]) (PraybillItemVO[]) this.hBody
									.get(strId));
				else {
					try {
						this.m_VOs2[this.m_nPresentRecord] = PrTool
								.getRefreshedVO(this.m_VOs2[this.m_nPresentRecord]);
					} catch (Exception be) {
						if (be instanceof BusinessException) {
							MessageDialog.showErrorDlg(this, NCLangRes
									.getInstance().getStrByID("SCMCOMMON",
											"UPPSCMCommon-000422"), be
									.getMessage());
						}

						isErr = true;
					}
				}

				getBillListPanel().getHeadBillModel().setBodyRowVO(
						this.m_VOs2[this.m_nPresentRecord].getHeadVO(),
						selectedRows[0]);

				getBillListPanel().getHeadBillModel().execLoadFormula();
				bodyVO = this.m_VOs2[this.m_nPresentRecord].getBodyVO();
			}

			new FreeVOParse().setFreeVO(bodyVO, "vfree", "vfree", "cbaseid",
					"cmangid", true);

			getBillListPanel().getBodyBillModel().setBodyDataVO(bodyVO);

			PuTool.loadInvMaxPrice(getBillListPanel());

			PuTool.loadSourceInfoAll(getBillListPanel(), "20");

			getBillListPanel().getBodyBillModel().execLoadFormula();
		}

		if (iSelCnt == iCount)
			this.m_nButtonState[0] = 1;
		else {
			this.m_nButtonState[0] = 0;
		}

		if (iSelCnt > 0) {
			this.m_nButtonState[1] = 0;
			this.m_nButtonState[7] = 0;
		} else {
			this.m_nButtonState[1] = 1;
			this.m_nButtonState[7] = 1;
		}

		if (iSelCnt > 0) {
			if (this.m_sAudit.toUpperCase().equals("N")) {
				this.m_nButtonState[2] = 0;
				this.m_nButtonState[3] = 1;
			} else {
				this.m_nButtonState[2] = 1;
				this.m_nButtonState[3] = 0;
			}
		} else {
			this.m_nButtonState[2] = 1;
			this.m_nButtonState[3] = 1;
		}

		this.m_nButtonState[4] = 0;

		if (iSelCnt == 1) {
			this.m_nButtonState[5] = 0;
			this.m_nButtonState[6] = 0;
			this.m_nButtonState[8] = 0;
		} else {
			this.m_nButtonState[5] = 1;
			this.m_nButtonState[6] = 1;
			this.m_nButtonState[8] = 1;
		}

		if (isErr)
			setListBtnsWhenErr();
		changeButtonState();
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
									"select pk_psndoc,psncode  from bd_psndoc a where nvl(a.dr,0)=0 and a.def1='"
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
									MessageDialog.showErrorDlg(AuditUI.this,
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
						try {
							if (m_buttons[2].isEnabled()) {// 审核按钮可用
								onButtonClicked(m_buttons[2]);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}

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

	@Override
	public boolean onClosing() {
		boolean close = super.onClosing();
		if (close) {
			isclose = true;
		}
		return close;
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