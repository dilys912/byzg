package nc.ui.rc.check;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.lang.reflect.Array;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.swing.ButtonGroup;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import nc.bs.framework.common.NCLocator;
import nc.bs.pub.pf.PfUtilBO;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.sf.ICreateCorpQueryService;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.ui.bd.CorpBO_Client;
import nc.ui.bd.languagetransformations.Transformations;
import nc.ui.common.ListProcessor;
import nc.ui.ic.pub.bill.GeneralBillHelper;
import nc.ui.ml.NCLangRes;
import nc.ui.pf.change.PfUtilUITools;
import nc.ui.pp.pub.CMethods;
import nc.ui.pu.pub.BusiTypeRefPane;
import nc.ui.pu.pub.PuTool;
import nc.ui.pu.pub.PurDeptRefModel;
import nc.ui.pu.pub.PurPsnRefModel;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRadioButton;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.IBillRelaSortListener2;
import nc.ui.pub.print.PrintEntry;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.rc.pub.PurchasePrintDS;
import nc.ui.rc.pub.RcTool;
import nc.ui.rc.receive.ArriveorderHelper;
import nc.ui.scm.pub.CacheTool;
import nc.ui.scm.pub.def.DefSetTool;
import nc.ui.scm.pub.print.ScmPrintTool;
import nc.ui.scm.ref.WarehouseRefModel;
import nc.ui.scm.service.LocalCallService;
import nc.vo.bd.CorpVO;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.bill.QryConditionVO;
import nc.vo.ic.pub.locator.LocatorVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.lang.UFTime;
import nc.vo.pub.query.ConditionVO;
import nc.vo.rc.receive.ArriveorderHeaderVO;
import nc.vo.rc.receive.ArriveorderItemVO;
import nc.vo.rc.receive.ArriveorderVO;
import nc.vo.rc.receive.CheckRsltItemVO;
import nc.vo.rc.receive.CheckRsltVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.service.ServcallVO;
import nc.vo.sm.UserVO;

public class CheckUI extends ToftPanel implements BillEditListener, ListSelectionListener, IBillRelaSortListener2 {
	private ButtonObject m_btnSelectAll = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000041"), NCLangRes.getInstance().getStrByID("40040303", "UPP40040303-000027"), 2, "全选");
	private ButtonObject m_btnCancelAll = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000042"), NCLangRes.getInstance().getStrByID("40040303", "UPP40040303-000028"), 2, "全消");

	private ButtonObject m_btnCheck = new ButtonObject(NCLangRes.getInstance().getStrByID("40040303", "UPT40040303-000009"), NCLangRes.getInstance().getStrByID("40040303", "UPP40040303-000029"), 2, "检验");
	private ButtonObject m_btnCheckCancel = new ButtonObject(NCLangRes.getInstance().getStrByID("40040303", "UPT40040303-000011"), NCLangRes.getInstance().getStrByID("40040303", "UPP40040303-000030"), 2, "反检");
	private ButtonObject m_btnQuery = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000006"), NCLangRes.getInstance().getStrByID("40040303", "UPP40040303-000031"), 2, "查询");
	private ButtonObject m_btnRefresh = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000009"), NCLangRes.getInstance().getStrByID("common", "UC001-0000009"), 2, "刷新");
	private ButtonObject m_btnPrint = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000007"), NCLangRes.getInstance().getStrByID("common", "UC001-0000007"), 2, "打印");
	private ButtonObject m_btnPrintPreview = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000056"), NCLangRes.getInstance().getStrByID("common", "4004COMMON000000056"), 2, "预览");
	private ButtonObject m_btnCheckRpt = new ButtonObject(NCLangRes.getInstance().getStrByID("40040303", "UPT40040303-000010"), NCLangRes.getInstance().getStrByID("40040303", "UPT40040303-000010"), 2, "质检报告");
	private ButtonObject m_btnAudit = new ButtonObject("审核", "审核", 2, "Audit");

	private ButtonObject[] m_aryCheckButtons = {
			this.m_btnSelectAll, this.m_btnCancelAll, this.m_btnCheck, this.m_btnCheckCancel, this.m_btnQuery, this.m_btnRefresh, this.m_btnPrint, this.m_btnPrintPreview, this.m_btnCheckRpt, this.m_btnAudit
	};

	private BillCardPanel m_billCardPanel = null;

	private ArriveorderVO m_arriveVO = null;

	private ArriveorderItemVO[] m_itemVOs = null;

	private QueryConditionClient m_dlgArrQueryCondition = null;

	Hashtable h = null;

	int nMeasDecimal = 2;
	int nAssistUnitDecimal = 2;

	boolean m_isQCEnable = false;

	private boolean _isDebug = false;

	private UIRadioButton m_rdoNoCheck = null;

	private UIRadioButton m_rdoChecked = null;

	private UIRadioButton m_rdoWtRst = null;

	int m_checkStatus = -1;

	BillCardPanel m_checkRpt = null;

	Hashtable hStorByChk = null;

	private PrintEntry m_printEntry = null;

	private GeneralBillVO m_GeneralBillVO = null;
	private ArriveorderVO[] m_arriveVOs = null;
	Hashtable errorht = null;

	public CheckUI() {
		initialize();
	}

	public void afterEdit(BillEditEvent e) {
		if (e.getRow() < 0) return;
		int i = e.getRow();
		Object oarrvnum = null;
		Object oaudittednum = null;
		Object owillauditnum = null;
		Object oelignum = null;
		UFDouble arrvnum = null;
		UFDouble audittednum = null;
		UFDouble willauditnum = null;
		UFDouble elignum = null;

		BillModel bm = getBillCardPanel().getBillModel();
		oarrvnum = bm.getValueAt(i, "arrvnum");
		oaudittednum = bm.getValueAt(i, "audittednum");
		owillauditnum = bm.getValueAt(i, "willauditnum");
		oelignum = bm.getValueAt(i, "elignum");

		willauditnum = (owillauditnum == null) || (owillauditnum.toString().trim().equals("")) ? new UFDouble(0) : new UFDouble(owillauditnum.toString().trim());
		audittednum = (oaudittednum == null) || (oaudittednum.toString().trim().equals("")) ? new UFDouble(0) : new UFDouble(oaudittednum.toString().trim());
		arrvnum = (oarrvnum == null) || (oarrvnum.toString().trim().equals("")) ? new UFDouble(0) : new UFDouble(oarrvnum.toString().trim());
		elignum = (oelignum == null) || (oelignum.toString().trim().equals("")) ? new UFDouble(0) : new UFDouble(oelignum.toString().trim());

		if (willauditnum.add(audittednum).compareTo(arrvnum) > 0) {
			MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000059"), NCLangRes.getInstance().getStrByID("40040303", "UPP40040303-000000"));
			return;
		}
		if (e.getKey().trim().equals("willauditnum")) {
			bm.setValueAt(willauditnum, i, "elignum");
			bm.setValueAt(new UFDouble(0), e.getRow(), "notelignum");
		}
		if (e.getKey().trim().equals("elignum")) if (elignum.compareTo(willauditnum) > 0) {
			bm.setValueAt(willauditnum, i, "elignum");
			bm.setValueAt(new UFDouble(0), e.getRow(), "notelignum");
		} else {
			bm.setValueAt(willauditnum.sub(elignum), i, "notelignum");
		}
	}

	public void bodyRowChange(BillEditEvent e) {
		getBillCardPanel().getBodyItem("glnum").setEnabled(true);
		getBillCardPanel().getBodyItem("jyjg").setEnabled(true);

		int row = e.getRow();

		if (getParentCorpCode().equals("10395")) {
			UFDouble naccumchecknum = null;
			UFDouble naccumwarehousenum = null;
			if (getBillCardPanel().getBillModel().getValueAt(row, "naccumchecknum") != null) {
				naccumchecknum = new UFDouble(getBillCardPanel().getBillModel().getValueAt(row, "naccumchecknum").toString());
			}
			if (getBillCardPanel().getBillModel().getValueAt(row, "naccumwarehousenum") != null) {
				naccumwarehousenum = new UFDouble(getBillCardPanel().getBillModel().getValueAt(row, "naccumwarehousenum").toString());
			}
			if (naccumchecknum == null) {
				naccumchecknum = new UFDouble(0);
			}
			if (naccumwarehousenum == null) {
				naccumwarehousenum = new UFDouble(0);
			}
			if (naccumchecknum.compareTo(naccumwarehousenum) == 0) {
				this.m_btnAudit.setEnabled(false);
				updateButton(this.m_btnAudit);
			} else if (naccumchecknum.compareTo(naccumwarehousenum) > 0) {
				this.m_btnAudit.setEnabled(true);
				updateButton(this.m_btnAudit);
			}
		}
	}

	private boolean checkSelectedData() {
		Object oarrvnum = null;
		Object oaudittednum = null;
		Object owillauditnum = null;
		Object oelignum = null;
		UFDouble arrvnum = null;
		UFDouble audittednum = null;
		UFDouble willauditnum = null;
		BillModel bm = getBillCardPanel().getBillModel();
		UFDouble ling = new UFDouble(0);
		for (int i = 0; i < bm.getRowCount(); i++) {
			if (bm.getRowState(i) == 4) {
				oarrvnum = bm.getValueAt(i, "arrvnum");
				oaudittednum = bm.getValueAt(i, "audittednum");
				owillauditnum = bm.getValueAt(i, "willauditnum");
				oelignum = bm.getValueAt(i, "elignum");

				if ((owillauditnum == null) || (owillauditnum.toString().trim().equals(""))) {
					MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000059"), "<#" + (i + 1) + "#>" + NCLangRes.getInstance().getStrByID("40040303", "UPP40040303-000001"));
					return false;
				}
				if ((oelignum == null) || (oelignum.toString().trim().equals(""))) {
					MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000059"), "<#" + (i + 1) + "#>" + NCLangRes.getInstance().getStrByID("40040303", "UPP40040303-000002"));
					return false;
				}

				willauditnum = new UFDouble(owillauditnum.toString().trim());
				audittednum = oaudittednum == null ? ling : new UFDouble(oaudittednum.toString().trim());
				arrvnum = (oarrvnum == null) || (oarrvnum.toString().trim().equals("")) ? ling : new UFDouble(oarrvnum.toString().trim());
				audittednum = (oaudittednum == null) || (oaudittednum.toString().trim().equals("")) ? ling : new UFDouble(oaudittednum.toString().trim());
				if (willauditnum.add(audittednum).compareTo(arrvnum) > 0) {
					MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000059"), NCLangRes.getInstance().getStrByID("40040303", "UPP40040303-000000"));
					return false;
				}
			}
		}
		return true;
	}

	private void getArriveVOFromDB() {
		try {
			ConditionVO[] conds = getQueryConditionDlg().getConditionVO();

			if (this.m_isQCEnable)
			;
			setM_arriveVO(ArriveorderHelper.queryForCheckMy(conds, getCorpId(), new UFBoolean((this.m_rdoChecked != null) && (this.m_rdoChecked.isSelected())), this.m_checkStatus));
			if ((getM_arriveVO() == null) || (getM_arriveVO().getChildrenVO() == null) || (getM_arriveVO().getChildrenVO().length <= 0)) {
				showHintMessage(NCLangRes.getInstance().getStrByID("40040303", "UPP40040303-000003"));
				getBillCardPanel().getBillData().clearViewData();
				updateUI();
			} else {
				ArriveorderItemVO item = null;
				UFDouble ufdArr = null;
				UFDouble ufdChk = null;
				UFDouble ufdAcc = null;
				Vector vRowId = new Vector();
				if ((getM_arriveVO().getChildrenVO() != null) && (getM_arriveVO().getChildrenVO().length > 0)) {
					this.h = new Hashtable();
					for (int i = 0; i < getM_arriveVO().getChildrenVO().length; i++) {
						item = (ArriveorderItemVO) getM_arriveVO().getChildrenVO()[i];

						if (!vRowId.contains(item.getPrimaryKey())) vRowId.addElement(item.getPrimaryKey());
						ufdChk = (UFDouble) item.getAttributeValue("willauditnum");
						ufdArr = (UFDouble) item.getAttributeValue("arrvnum");
						ufdAcc = (UFDouble) item.getAttributeValue("audittednum");
						if (ufdArr != null) {
							if (ufdAcc == null) ufdChk = ufdArr;
							else ufdChk = ufdArr.sub(ufdAcc);
							item.setAttributeValue("willauditnum", ufdChk);
							item.setAttributeValue("elignum", ufdChk);
						}

						if (item.getAttributeValue("willauditnum") == null) item.setAttributeValue("willauditnum", new UFDouble(0));
						if (item.getAttributeValue("audittednum") == null) {
							item.setAttributeValue("audittednum", new UFDouble(0));
						}
						ArriveorderItemVO vo = new ArriveorderItemVO();
						vo = (ArriveorderItemVO) ((ArriveorderItemVO) getM_arriveVO().getChildrenVO()[i]).clone();
						this.h.put(vo.getCarriveorder_bid(), vo);
					}
				}

				if ((this.m_isQCEnable) && (vRowId != null) && (vRowId.size() > 0)) {
					String[] sa = new String[vRowId.size()];
					vRowId.copyInto(sa);
					ArrayList aryTmp = ArriveorderHelper.getStoreByChkArray(sa);
					if ((aryTmp != null) && (aryTmp.size() > 0)) {
						this.hStorByChk = new Hashtable();
						for (int i = 0; i < sa.length; i++)
							if (aryTmp.get(i) != null) this.hStorByChk.put(sa[i], aryTmp.get(i));
					}
				}
			}
		} catch (Exception e) {
			SCMEnv.out(e);
		}
	}

	private BillCardPanel getBillCardPanel() {
		if (this.m_billCardPanel == null) {
			try {
				this.m_billCardPanel = new BillCardPanel();
				try {
					this.m_billCardPanel.loadTemplet("2L", null, getOperatorId(), getCorpId());
				} catch (Exception ex) {
					reportException(ex);
					this.m_billCardPanel.loadTemplet("40040303010000000000");
				}

				this.m_billCardPanel.setBodyShowThMark(true);

				this.m_billCardPanel.setBodyMenuShow(false);

				this.m_billCardPanel.getBodyItem("arrvnum").setDecimalDigits(this.nMeasDecimal);

				this.m_billCardPanel.getBodyItem("audittednum").setDecimalDigits(this.nMeasDecimal);

				this.m_billCardPanel.getBodyItem("willauditnum").setDecimalDigits(this.nMeasDecimal);

				this.m_billCardPanel.getBodyItem("elignum").setDecimalDigits(this.nMeasDecimal);

				this.m_billCardPanel.getBodyItem("notelignum").setDecimalDigits(this.nMeasDecimal);

				this.m_billCardPanel.getBodyItem("naccumchecknum").setDecimalDigits(this.nMeasDecimal);

				this.m_billCardPanel.getBodyItem("npresentnum").setDecimalDigits(this.nMeasDecimal);

				this.m_billCardPanel.getBodyItem("nwastnum").setDecimalDigits(this.nMeasDecimal);

				this.m_billCardPanel.getBodyItem("nassistnum").setDecimalDigits(this.nAssistUnitDecimal);

				this.m_billCardPanel.setEnabled(true);
				BillItem[] items = this.m_billCardPanel.getBodyItems();
				int iLen = items == null ? 0 : items.length;
				for (int i = 0; i < iLen; i++) {
					if (items[i] == null) {
						continue;
					}
					if (("willauditnum".equalsIgnoreCase(items[i].getKey())) || ("elignum".equalsIgnoreCase(items[i].getKey()))) {
						items[i].setEnabled(true);
					} else {
						items[i].setEnabled(false);
					}
				}

				UIRefPane refWillauditnum = (UIRefPane) this.m_billCardPanel.getBodyItem("willauditnum").getComponent();
				refWillauditnum.getUITextField().setMinValue(0.0D);
				refWillauditnum.getUITextField().setMaxValue(2147483647);

				UIRefPane refElignum = (UIRefPane) this.m_billCardPanel.getBodyItem("elignum").getComponent();
				refElignum.getUITextField().setMinValue(0.0D);
				refWillauditnum.getUITextField().setMaxValue(2147483647);

				this.m_billCardPanel.getBillModel().execLoadFormula();

				DefSetTool.updateBillCardPanelUserDef(this.m_billCardPanel, getCorpId(), "23", "vdef", "vdef");
			} catch (Throwable e) {
				SCMEnv.out(e);
			}
		}
		return this.m_billCardPanel;
	}

	public BillCardPanel getcheckRpt() {
		if (this.m_checkRpt == null) {
			this.m_checkRpt = new BillCardPanel();
			BillData data = new BillData();

			String[] aryitemNames = {
					NCLangRes.getInstance().getStrByID("common", "UC000-0001480"), NCLangRes.getInstance().getStrByID("common", "UC000-0001453"), NCLangRes.getInstance().getStrByID("common", "UC000-0003938"), NCLangRes.getInstance().getStrByID("common", "UC000-0003941"), NCLangRes.getInstance().getStrByID("common", "UC000-0003971"), NCLangRes.getInstance().getStrByID("common", "UC000-0002161"), NCLangRes.getInstance().getStrByID("common", "UC000-0002282"), NCLangRes.getInstance().getStrByID("common", "UC000-0000745"), NCLangRes.getInstance().getStrByID("common", "UC000-0003840"), NCLangRes.getInstance().getStrByID("40040303", "UPP40040303-000032"), NCLangRes.getInstance().getStrByID("common", "UC000-0001886"), NCLangRes.getInstance().getStrByID("40040303", "UPP40040303-000033"), NCLangRes.getInstance().getStrByID("40040303", "UPP40040303-000034"), NCLangRes.getInstance().getStrByID("common", "UC000-0001460"), NCLangRes.getInstance().getStrByID("40040303", "UPP40040303-000035"), NCLangRes.getInstance().getStrByID("40040303", "UPP40040303-000036")
			};

			String[] aryitemKeys = {
					"invcode", "invname", "assisunitname", "assisunit", "assisnum", "convertrate", "num", "mainunit", "chkstatus", "iselg", "dealmethod", "bcheckin", "bchange", "cbaseid", "changedinvcode", "changedinvname"
			};

			BillItem[] items = new BillItem[aryitemNames.length];
			for (int i = 0; i < aryitemNames.length; i++) {
				BillItem item = new BillItem();
				if (aryitemKeys[i].equals("num")) {
					item.setDataType(2);
					item.setDecimalDigits(this.nMeasDecimal);
				} else if (aryitemKeys[i].equals("assisnum")) {
					item.setDataType(2);
					item.setDecimalDigits(this.nMeasDecimal);
				} else if (aryitemKeys[i].equals("convertrate")) {
					item.setDataType(2);
					item.setDecimalDigits(this.nMeasDecimal);
				} else if (aryitemKeys[i].equals("iselg")) {
					item.setDataType(4);
				} else if (aryitemKeys[i].equals("bcheckin")) {
					item.setDataType(4);
				} else if (aryitemKeys[i].equals("bchange")) {
					item.setDataType(4);
				} else if (aryitemKeys[i].equals("assisunit")) {
					item.setShow(false);
				} else if (aryitemKeys[i].equals("assisunitname")) {
					item.setDataType(5);
					item.setIDColName("assisunit");
				} else if (aryitemKeys[i].equals("cbaseid")) {
					item.setShow(false);
				} else if (aryitemKeys[i].equals("changedinvcode")) {
					item.setDataType(5);
					item.setIDColName("cbaseid");
				} else if (aryitemKeys[i].equals("changedinvname")) {
					item.setDataType(5);
					item.setIDColName("cbaseid");
				} else if (aryitemKeys[i].equals("dealmethod")) {
					item.setDataType(5);
				}

				item.setName(aryitemNames[i]);
				item.setKey(aryitemKeys[i]);
				item.setWidth(100);
				item.setLength(100);
				item.setShowOrder(i);
				item.setEnabled(false);
				items[i] = item;
			}

			data.setBodyItems(items);
			this.m_checkRpt.setBillData(data);
		}
		return this.m_checkRpt;
	}

	private String getCorpId() {
		String corpid = null;
		corpid = getClientEnvironment().getCorporation().getPrimaryKey();
		if ((corpid == null) || (corpid.trim().equals(""))) {
			corpid = getCorpPrimaryKey();
		}
		return corpid;
	}

	private String getErrChk() {
		String strErr = "";
		BillModel bm = getBillCardPanel().getBillModel();
		ArriveorderItemVO[] items = (ArriveorderItemVO[]) bm.getBodySelectedVOs(ArriveorderItemVO.class.getName());
		if ((items == null) || (items.length <= 0)) return NCLangRes.getInstance().getStrByID("40040303", "UPP40040303-000009");
		Vector vErr = new Vector();
		ArriveorderItemVO item = null;
		UFDouble ufdArr = null;
		UFDouble ufdChk = null;
		for (int i = 0; i < bm.getRowCount(); i++) {
			if (bm.getRowState(i) == 4) {
				item = (ArriveorderItemVO) bm.getBodyValueRowVO(i, ArriveorderItemVO.class.getName());
				ufdChk = item.getNaccumchecknum();
				ufdArr = item.getNarrvnum();

				if ((ufdChk == null) || (ufdChk.doubleValue() == 0.0D)) {
					continue;
				}
				if ((ufdArr != null) && (ufdArr.doubleValue() == ufdChk.doubleValue())) {
					if (vErr.size() > 0) vErr.addElement(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000000"));
					vErr.addElement(new Integer(i + 1));
				}
			}
		}

		if (vErr.size() > 0) {
			strErr = strErr + NCLangRes.getInstance().getStrByID("40040303", "UPP40040303-000004");
			for (int i = 0; i < vErr.size(); i++) {
				strErr = strErr + vErr.elementAt(i);
			}
			if (strErr.trim().length() > 0) {
				strErr = strErr + NCLangRes.getInstance().getStrByID("40040303", "UPP40040303-000005");
				strErr = strErr + "\r";
			}
		}
		if (strErr.trim().length() <= 0) return null;
		return strErr.trim();
	}

	private String getErrChkCancel() {
		String strErr = "";
		BillModel bm = getBillCardPanel().getBillModel();
		ArriveorderItemVO[] items = (ArriveorderItemVO[]) bm.getBodySelectedVOs(ArriveorderItemVO.class.getName());
		if ((items == null) || (items.length <= 0)) return NCLangRes.getInstance().getStrByID("40040303", "UPP40040303-000006");
		Vector vWar = new Vector();
		Vector vChk = new Vector();
		ArriveorderItemVO item = null;
		for (int i = 0; i < bm.getRowCount(); i++) {
			if (bm.getRowState(i) == 4) {
				item = (ArriveorderItemVO) bm.getBodyValueRowVO(i, ArriveorderItemVO.class.getName());

				if ((item.getNaccumwarehousenum() != null) && (item.getNaccumwarehousenum().doubleValue() != 0.0D)) {
					if (vWar.size() > 0) vWar.addElement(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000000"));
					vWar.addElement(new Integer(i + 1));
				}

				if ((item.getNaccumchecknum() != null) && (item.getNaccumchecknum().doubleValue() != 0.0D)) continue;
				if (vChk.size() > 0) vChk.addElement(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000000"));
				vChk.addElement(new Integer(i + 1));
			}
		}

		if (vWar.size() > 0) {
			strErr = strErr + NCLangRes.getInstance().getStrByID("40040303", "UPP40040303-000007");
			for (int i = 0; i < vWar.size(); i++) {
				strErr = strErr + vWar.elementAt(i);
			}
			if (strErr.trim().length() > 0) {
				strErr = strErr + NCLangRes.getInstance().getStrByID("40040303", "UPP40040303-000005");
				strErr = strErr + "\r";
			}
		}
		if (vChk.size() > 0) {
			strErr = strErr + NCLangRes.getInstance().getStrByID("40040303", "UPP40040303-000008");
			for (int i = 0; i < vChk.size(); i++) {
				strErr = strErr + vChk.elementAt(i);
			}
			if (strErr.trim().length() > 0) {
				strErr = strErr + NCLangRes.getInstance().getStrByID("40040303", "UPP40040303-000005");
				strErr = strErr + "\r";
			}
		}
		if (strErr.trim().length() <= 0) return null;
		return strErr.trim();
	}

	private String getErrChkQcEnable() {
		String strErr = "";
		BillModel bm = getBillCardPanel().getBillModel();
		ArriveorderItemVO[] items = (ArriveorderItemVO[]) bm.getBodySelectedVOs(ArriveorderItemVO.class.getName());
		if ((items == null) || (items.length <= 0)) return NCLangRes.getInstance().getStrByID("40040303", "UPP40040303-000009");
		Vector vErr = new Vector();
		ArriveorderItemVO item = null;
		UFDouble ufdAcc = null;
		String strRowId = null;
		UFBoolean ufbStorByChk = null;
		for (int i = 0; i < bm.getRowCount(); i++) {
			if (bm.getRowState(i) == 4) {
				item = (ArriveorderItemVO) bm.getBodyValueRowVO(i, ArriveorderItemVO.class.getName());
				ufdAcc = item.getNaccumwarehousenum();

				strRowId = item.getPrimaryKey();

				if ((ufdAcc == null) || (ufdAcc.doubleValue() == 0.0D)) {
					continue;
				}
				if ((this.hStorByChk == null) || (this.hStorByChk.size() <= 0)) continue;
				ufbStorByChk = (UFBoolean) this.hStorByChk.get(strRowId);
				if (ufbStorByChk.booleanValue()) {
					if (vErr.size() > 0) vErr.addElement(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000000"));
					vErr.addElement(new Integer(i + 1));
				}
			}
		}

		if (vErr.size() > 0) {
			strErr = strErr + NCLangRes.getInstance().getStrByID("40040303", "UPP40040303-000010");
			for (int i = 0; i < vErr.size(); i++) {
				strErr = strErr + vErr.elementAt(i);
			}
			if (strErr.trim().length() > 0) {
				strErr = strErr + NCLangRes.getInstance().getStrByID("40040303", "UPP40040303-000005");
				strErr = strErr + "\r";
			}
		}
		if (strErr.trim().length() <= 0) return null;
		return strErr.trim();
	}

	private ArriveorderVO getM_arriveVO() {
		if ((this.m_itemVOs != null) && (this.m_arriveVO != null)) {
			this.m_arriveVO.setChildrenVO(this.m_itemVOs);
		}

		return this.m_arriveVO;
	}

	private QueryConditionClient getQueryConditionDlg() {
		if (this.m_dlgArrQueryCondition == null) {
			this.m_dlgArrQueryCondition = new QueryConditionClient(this);
			this.m_dlgArrQueryCondition.hideUnitButton();
			try {
				this.m_dlgArrQueryCondition.setTempletID(getCorpId(), "40040303", getOperatorId(), null);
			} catch (Exception e) {
				SCMEnv.out(e.getMessage());
			}
			if (!this.m_isQCEnable) {
				this.m_dlgArrQueryCondition.hideNormal();
			} else {
				this.m_rdoNoCheck = new UIRadioButton();
				this.m_rdoNoCheck.setText(NCLangRes.getInstance().getStrByID("40040303", "UPP40040303-000037"));
				this.m_rdoNoCheck.setBackground(getBackground());
				this.m_rdoNoCheck.setForeground(Color.black);
				this.m_rdoNoCheck.setSize(350, this.m_rdoNoCheck.getHeight());
				this.m_rdoNoCheck.setSelected(true);

				this.m_rdoWtRst = new UIRadioButton();
				this.m_rdoWtRst.setText(NCLangRes.getInstance().getStrByID("40040303", "UPP40040303-000038"));
				this.m_rdoWtRst.setBackground(this.m_rdoNoCheck.getBackground());
				this.m_rdoWtRst.setForeground(this.m_rdoNoCheck.getForeground());
				this.m_rdoWtRst.setSize(this.m_rdoNoCheck.getSize());

				this.m_rdoChecked = new UIRadioButton();
				this.m_rdoChecked.setText(NCLangRes.getInstance().getStrByID("40040303", "UPP40040303-000039"));
				this.m_rdoChecked.setBackground(this.m_rdoNoCheck.getBackground());
				this.m_rdoChecked.setForeground(this.m_rdoNoCheck.getForeground());
				this.m_rdoChecked.setSize(this.m_rdoNoCheck.getSize());

				this.m_rdoNoCheck.setLocation(50, 30);
				this.m_rdoWtRst.setLocation(this.m_rdoNoCheck.getX(), this.m_rdoNoCheck.getY() + this.m_rdoNoCheck.getHeight() + 20);
				this.m_rdoChecked.setLocation(this.m_rdoWtRst.getX(), this.m_rdoWtRst.getY() + this.m_rdoWtRst.getHeight() + 20);

				ButtonGroup bg = new ButtonGroup();
				bg.add(this.m_rdoNoCheck);
				bg.add(this.m_rdoWtRst);
				bg.add(this.m_rdoChecked);
				bg.setSelected(this.m_rdoNoCheck.getModel(), true);

				this.m_dlgArrQueryCondition.getUIPanelNormal().setLayout(null);

				this.m_dlgArrQueryCondition.getUIPanelNormal().add(this.m_rdoNoCheck);
				this.m_dlgArrQueryCondition.getUIPanelNormal().add(this.m_rdoWtRst);
				this.m_dlgArrQueryCondition.getUIPanelNormal().add(this.m_rdoChecked);
			}

			DefSetTool.updateQueryConditionClientUserDef(this.m_dlgArrQueryCondition, getCorpId(), "23", "po_arriveorder.vdef", "po_arriveorder_b.vdef");

			BusiTypeRefPane refBusitype = new BusiTypeRefPane(getCorpId(), "23");
			this.m_dlgArrQueryCondition.setValueRef("bd_busitype.busicode", refBusitype);

			UIRefPane refPrayPsn = new UIRefPane();

			PurPsnRefModel refPsnModel = new PurPsnRefModel(getCorpId());
			refPrayPsn.setRefModel(refPsnModel);
			this.m_dlgArrQueryCondition.setValueRef("bd_psndoc.psncode", refPrayPsn);

			UIRefPane refPrayDept = new UIRefPane();

			PurDeptRefModel refDeptModel = new PurDeptRefModel();
			refPrayDept.setRefModel(refDeptModel);
			this.m_dlgArrQueryCondition.setValueRef("bd_deptdoc.deptcode", refPrayDept);

			UIRefPane refPane = new UIRefPane();

			WarehouseRefModel refModel1 = new WarehouseRefModel(getCorpId());
			refPane.setRefModel(refModel1);
			this.m_dlgArrQueryCondition.setValueRef("bd_stordoc.storcode", refPane);

			this.m_dlgArrQueryCondition.setValueRef("po_arriveorder.dreceivedate", "日历");
			this.m_dlgArrQueryCondition.setDefaultValue("po_arriveorder.dreceivedate", "po_arriveorder.dreceivedate", getClientEnvironment().getDate().toString());

			this.m_dlgArrQueryCondition.setIsWarningWithNoInput(true);
		}
		return this.m_dlgArrQueryCondition;
	}

	private String getOperatorId() {
		String operatorid = getClientEnvironment().getUser().getPrimaryKey();
		if ((operatorid == null) || (operatorid.trim().equals("")) || (operatorid.equals("88888888888888888888"))) {
			operatorid = "10013488065564590288";
		}
		return operatorid;
	}

	private PrintEntry getPrintEntry() {
		if (this.m_printEntry == null) {
			this.m_printEntry = new PrintEntry(null, null);

			this.m_printEntry.setTemplateID(getCorpId(), getModuleCode(), getOperatorId(), null);
		}
		return this.m_printEntry;
	}

	private int getRowLast() {
		int iPos = -1;
		for (int i = 0; i < getBillCardPanel().getBillModel().getRowCount(); i++) {
			if (getBillCardPanel().getBillModel().getRowState(i) == 4) {
				iPos = i;
			}
		}
		return iPos;
	}

	public String getTitle() {
		String title = NCLangRes.getInstance().getStrByID("40040303", "UPP40040303-000040");
		if (this.m_billCardPanel != null) title = this.m_billCardPanel.getTitle();
		return title;
	}

	private void initialize() {
		initPara();

		setButtonsState();
		setLayout(new BorderLayout());
		getBillCardPanel().getHeadUIPanel().setVisible(false);
		add(getBillCardPanel(), "Center");
		try {
			ICreateCorpQueryService myService = (ICreateCorpQueryService) NCLocator.getInstance().lookup(ICreateCorpQueryService.class.getName());
			this.m_isQCEnable = myService.isEnabled(getCorpId(), "QC");
		} catch (Exception e) {
			SCMEnv.out(e);
		}

		if (this.m_isQCEnable) {
			UFDate ufdStart = CMethods.getSysStartDate(getCorpId(), "QC");
			if (ufdStart != null) this.m_isQCEnable = ((this.m_isQCEnable) && (!ufdStart.after(getClientEnvironment().getDate())));
		}
		if (this._isDebug) {
			this.m_isQCEnable = true;
		}
		if (this.m_isQCEnable) {
			this.m_btnCheckCancel.setVisible(false);

			getBillCardPanel().getBodyItem("willauditnum").setEnabled(false);
			getBillCardPanel().getBodyItem("elignum").setEnabled(false);

			getBillCardPanel().hideBodyTableCol("elignum");
			getBillCardPanel().hideBodyTableCol("notelignum");
		}

		initListener();
		updateUI();
	}

	private void initListener() {
		getBillCardPanel().addEditListener(this);

		getBillCardPanel().getBillTable().setCellSelectionEnabled(false);
		getBillCardPanel().getBillTable().setSelectionMode(2);
		getBillCardPanel().getBillTable().getSelectionModel().addListSelectionListener(this);

		getBillCardPanel().getBillModel().addSortRelaObjectListener2(this);
	}

	public void initPara() {
		try {
			ServcallVO[] scDisc = new ServcallVO[1];

			scDisc[0] = new ServcallVO();
			scDisc[0].setBeanName("nc.itf.pu.pub.IPub");
			scDisc[0].setMethodName("getDigitBatch");
			scDisc[0].setParameter(new Object[] {
					getCorpId(), new String[] {
							"BD502", "BD503", "BD501", "BD505"
					}
			});
			scDisc[0].setParameterTypes(new Class[] {
					String.class, String[].class
			});

			Object[] oParaValue = LocalCallService.callService(scDisc);
			if ((oParaValue != null) && (oParaValue.length == scDisc.length)) {
				int[] iDigits = (int[]) oParaValue[0];
				if ((iDigits != null) && (iDigits.length == 4)) {
					this.nAssistUnitDecimal = iDigits[0];

					this.nMeasDecimal = iDigits[2];
				}

			}

		} catch (Exception e) {
			reportException(e);
		}
	}

	private boolean isCanCheck() {
		BillModel bm = getBillCardPanel().getBillModel();
		ArriveorderItemVO[] items = (ArriveorderItemVO[]) bm.getBodySelectedVOs(ArriveorderItemVO.class.getName());
		if ((items == null) || (items.length <= 0)) { return false; }
		for (int i = 0; i < items.length; i++) {
			UFDouble ufdArr = items[i].getArrvnum();
			if (ufdArr == null) ufdArr = new UFDouble(0);
			UFDouble ufdAccChk = items[i].getNaccumchecknum();
			if (ufdAccChk == null) ufdAccChk = new UFDouble(0);
			UFDouble ufdAccWar = items[i].getNaccumwarehousenum();
			if (ufdAccWar == null) ufdAccWar = new UFDouble(0);
			String strRowId = items[i].getPrimaryKey();

			if (ufdAccChk.doubleValue() == 0.0D) continue;
			if (this.m_isQCEnable) {
				if (this.m_checkStatus == 0) {
					if (ufdArr.doubleValue() == ufdAccChk.doubleValue()) return false;
				} else {
					if ((this.m_checkStatus != 2) || (this.hStorByChk == null) || (this.hStorByChk.size() <= 0)) continue;
					UFBoolean isStorByChk = (UFBoolean) this.hStorByChk.get(strRowId);

					if (!isStorByChk.booleanValue()) continue;
					if (ufdAccWar.doubleValue() != 0.0D) { return false; }

				}

			} else if (ufdArr.doubleValue() == ufdAccChk.doubleValue()) { return false; }
		}

		return true;
	}

	private boolean isCanCheckCancel() {
		BillModel bm = getBillCardPanel().getBillModel();
		ArriveorderItemVO[] items = (ArriveorderItemVO[]) bm.getBodySelectedVOs(ArriveorderItemVO.class.getName());
		if ((items == null) || (items.length <= 0)) { return false; }
		for (int i = 0; i < items.length; i++) {
			UFDouble ufdWar = items[i].getNaccumwarehousenum();
			if (ufdWar == null) ufdWar = new UFDouble(0);
			UFDouble ufdChk = items[i].getNaccumchecknum();
			if ((ufdChk == null) || (ufdChk.toString().trim().length() == 0)) ufdChk = new UFDouble(0);
			if ((ufdChk.doubleValue() == 0.0D) || (ufdWar.doubleValue() != 0.0D)) return false;
		}
		return true;
	}

	public void onButtonClicked(ButtonObject bo) {
		if (bo == this.m_btnQuery) onQuery();
		else if (bo == this.m_btnSelectAll) onSelectAll();
		else if (bo == this.m_btnCancelAll) onSelectNo();
		else if (bo == this.m_btnRefresh) onRefresh();
		else if (bo == this.m_btnCheck) onCheck();
		else if (bo == this.m_btnPrint) {
			ScmPrintTool.onPrint(getPrintEntry(), new PurchasePrintDS(getModuleCode(), getBillCardPanel()));
		} else if (bo == this.m_btnPrintPreview) {
			ScmPrintTool.onPreview(getPrintEntry(), new PurchasePrintDS(getModuleCode(), getBillCardPanel()));
		} else if (bo == this.m_btnCheckRpt) onCheckRpt();
		else if (bo == this.m_btnCheckCancel) {
			onCheckCancle();
		} else if (bo == this.m_btnAudit) try {
			doAudit();
		} catch (Exception e) {
			MessageDialog.showErrorDlg(this, "Error", e.getMessage().toString());

			e.printStackTrace();
		}
	}

	private void onCheck() {
		try {
			String strErr = null;
			if (!this.m_isQCEnable) {
				if (!checkSelectedData()) return;
				strErr = getErrChk();
				if (strErr != null) {
					MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040303", "UPP40040303-000011"), strErr);
					return;
				}
			} else {
				if (this.m_checkStatus == 0) strErr = getErrChk();
				else strErr = getErrChkQcEnable();
				if (strErr != null) {
					MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040303", "UPP40040303-000011"), strErr);
					return;
				}
			}
			Vector vNew = new Vector();
			Vector vDisp = new Vector();
			Vector vQc = new Vector();
			ArriveorderItemVO[] itemsQc = (ArriveorderItemVO[]) null;
			ArriveorderItemVO itemOld = null;
			ArriveorderItemVO itemNew = null;

			UFDouble narrvnum = new UFDouble(0);
			UFDouble nelignum = new UFDouble(0);
			UFDouble nnotelignum = new UFDouble(0);
			UFDouble naccnumold = new UFDouble(0);
			UFDouble elignum = new UFDouble(0);
			UFDouble notelignum = new UFDouble(0);

			String carriveorder_bid = null;
			String carriveorderid = null;
			String carriveorder_bts = null;
			String carriveorderts = null;
			ArrayList aryRewriteNum = new ArrayList();
			Vector vStrLineId = new Vector();
			Vector vStrHeadId = new Vector();
			Vector vStrLineTs = new Vector();
			Vector vStrHeadTs = new Vector();
			for (int i = 0; i < getBillCardPanel().getBillModel().getRowCount(); i++) {
				itemOld = ((ArriveorderItemVO[]) getM_arriveVO().getChildrenVO())[i];
				vDisp.addElement(itemOld);
				if (getBillCardPanel().getBillModel().getRowState(i) != 4) continue;
				carriveorder_bid = itemOld.getCarriveorder_bid();
				carriveorderid = itemOld.getCarriveorderid();
				carriveorder_bts = itemOld.getTs();
				carriveorderts = itemOld.getTsh();

				vDisp.removeElement(itemOld);

				vQc.addElement(itemOld);

				narrvnum = new UFDouble(0.0D);
				if ((itemOld.getNarrvnum() != null) && (itemOld.getNarrvnum().toString().trim().length() > 0)) {
					narrvnum = itemOld.getNarrvnum();
				}

				naccnumold = new UFDouble(0);
				if ((itemOld.getNaccumchecknum() != null) && (itemOld.getNaccumchecknum().toString().trim().length() > 0)) {
					naccnumold = itemOld.getNaccumchecknum();
				}

				nelignum = new UFDouble(0);
				if ((itemOld.getNelignum() != null) && (itemOld.getNelignum().toString().trim().length() > 0)) {
					nelignum = itemOld.getNelignum();
				}

				nnotelignum = new UFDouble(0);
				if ((itemOld.getNnotelignum() != null) && (itemOld.getNnotelignum().toString().trim().length() > 0)) {
					nnotelignum = itemOld.getNnotelignum();
				}

				itemNew = (ArriveorderItemVO) itemOld.clone();

				elignum = new UFDouble(0);
				Object oelignum = getBillCardPanel().getBillModel().getValueAt(i, "elignum");
				if ((oelignum != null) && (oelignum.toString().trim().length() > 0)) {
					elignum = new UFDouble(oelignum.toString().trim());
				}
				notelignum = new UFDouble(0);
				Object onotelignum = getBillCardPanel().getBillModel().getValueAt(i, "notelignum");
				if ((onotelignum != null) && (onotelignum.toString().trim().length() > 0)) {
					notelignum = new UFDouble(onotelignum.toString().trim());
				}

				nelignum = nelignum.add(elignum);
				itemNew.setNelignum(nelignum);

				nnotelignum = nnotelignum.add(notelignum);
				itemNew.setNnotelignum(nnotelignum);

				naccnumold = naccnumold.add(elignum.add(notelignum));
				itemNew.setNaccumchecknum(naccnumold);

				UFDouble[] rewriteNum = new UFDouble[3];
				rewriteNum[0] = nelignum;
				rewriteNum[1] = nnotelignum;
				rewriteNum[2] = naccnumold;

				String jyjg = getBillCardPanel().getBillModel().getValueAt(i, "jyjg") == null ? "" : getBillCardPanel().getBillModel().getValueAt(i, "jyjg").toString();
				UFDouble glnum = getBillCardPanel().getBillModel().getValueAt(i, "glnum") == null ? null : new UFDouble(getBillCardPanel().getBillModel().getValueAt(i, "glnum").toString());
				Object[] obj = new Object[5];
				obj[0] = nelignum;
				obj[1] = nnotelignum;
				obj[2] = naccnumold;
				obj[3] = jyjg;
				obj[4] = glnum;
				aryRewriteNum.add(obj);

				vStrLineId.addElement(carriveorder_bid);
				vStrLineTs.addElement(carriveorder_bts);
				vStrHeadId.addElement(carriveorderid);
				vStrHeadTs.addElement(carriveorderts);

				if (narrvnum.compareTo(nelignum.add(nnotelignum)) > 0) {
					itemNew.setArrvnum(narrvnum);
					itemNew.setAudittednum(naccnumold);
					itemNew.setWillauditnum(narrvnum.sub(naccnumold));
					itemNew.setElignum(narrvnum.sub(naccnumold));
					itemNew.setNotelignum(new UFDouble(0));
					vDisp.addElement(itemNew);
				}
				vNew.addElement(itemNew);
			}

			if (vStrLineId.size() <= 0) { return; }

			boolean isTsChanged = false;
			ArrayList listPara = new ArrayList();

			listPara.add(0, new UFBoolean(this.m_isQCEnable));
			if (this.m_isQCEnable) {
				if (vQc.size() > 0) {
					showHintMessage(NCLangRes.getInstance().getStrByID("40040303", "UPP40040303-000012"));
					itemsQc = new ArriveorderItemVO[vQc.size()];
					vQc.copyInto(itemsQc);

					UFDate dBusinessDate = getClientEnvironment().getDate();

					UFDateTime dtServerDateTime = ClientEnvironment.getServerTime();

					UFDateTime dtDateTime = new UFDateTime(dBusinessDate, new UFTime(dtServerDateTime.getTime()));

					listPara.add(1, null);
					listPara.add(2, itemsQc);
					listPara.add(3, getOperatorId());
					listPara.add(4, dtDateTime);
				} else {
					listPara.add(1, null);
					listPara.add(2, null);
					listPara.add(3, null);
					listPara.add(4, null);
				}
			} else {
				listPara.add(1, null);
				listPara.add(2, null);
				listPara.add(3, null);
				listPara.add(4, null);
			}

			ArriveorderItemVO[] items = (ArriveorderItemVO[]) null;

			listPara.add(5, new UFBoolean(vNew.size() > 0));

			if (vNew.size() > 0) {
				items = new ArriveorderItemVO[vNew.size()];
				vNew.copyInto(items);

				String[] arrStrLineIds = new String[vStrLineId.size()];
				String[] arrStrHeadIds = new String[vStrHeadId.size()];
				String[] arrStrLineTss = new String[vStrLineTs.size()];
				String[] arrStrHeadTss = new String[vStrHeadTs.size()];
				vStrLineId.copyInto(arrStrLineIds);
				vStrHeadId.copyInto(arrStrHeadIds);
				vStrLineTs.copyInto(arrStrLineTss);
				vStrHeadTs.copyInto(arrStrHeadTss);

				listPara.add(6, getCorpId());
				listPara.add(7, new UFBoolean(this.m_isQCEnable));
				listPara.add(8, arrStrLineIds);
				listPara.add(9, arrStrHeadIds);
				listPara.add(10, aryRewriteNum);
				listPara.add(11, getOperatorId());
				listPara.add(12, arrStrLineTss);
				listPara.add(13, arrStrHeadTss);
				listPara.add(14, new UFBoolean((this.m_isQCEnable) && (this.m_rdoChecked.isSelected())));
				listPara.add(15, new UFBoolean(isTsChanged));
			}

			String sRet = ArriveorderHelper.crtQcAndRewriteNum(listPara);

			if (sRet != null) {
				MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000059"), NCLangRes.getInstance().getStrByID("40040303", "UPP40040303-000013") + sRet);
				showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000060"));
			}

			if (vNew.size() > 0) {
				onRefresh();
			}
			if (sRet == null) showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000058"));
		} catch (BusinessException b) {
			MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000059"), b.getMessage());
			showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000060"));
		} catch (Exception b) {
			MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000059"), b.getMessage());
			showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000060"));
		}
	}

	private void onCheckCancle() {
		String strErr = getErrChkCancel();
		if (strErr != null) {
			MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040303", "UPP40040303-000019"), strErr);
			return;
		}

		BillModel bm = getBillCardPanel().getBillModel();
		ArriveorderItemVO[] items = (ArriveorderItemVO[]) bm.getBodySelectedVOs(ArriveorderItemVO.class.getName());

		ArrayList aryPara = new ArrayList();
		Vector vId = new Vector();
		Vector vTs = new Vector();
		for (int i = 0; i < items.length; i++) {
			if ((items[i].getPrimaryKey() == null) || (items[i].getPrimaryKey().trim().equals(""))) {
				SCMEnv.out("出现数据错误：表体行找不到表体ID");
				return;
			}
			vId.addElement(items[i].getPrimaryKey());

			if ((items[i].getTs() == null) || (items[i].getTs().trim().equals(""))) {
				SCMEnv.out("出现数据错误：表体行找不到表体ID");
				return;
			}
			vTs.addElement(items[i].getTs());
		}
		if ((vId.size() <= 0) || (vTs.size() <= 0) || (vId.size() != vTs.size())) return;
		String[] saId = new String[vId.size()];
		String[] saTs = new String[vId.size()];
		vId.copyInto(saId);
		vTs.copyInto(saTs);
		aryPara.add(saId);
		aryPara.add(saTs);
		aryPara.add(getOperatorId());
		try {
			showHintMessage(NCLangRes.getInstance().getStrByID("40040303", "UPP40040303-000020"));
			ArriveorderHelper.doChkCancel(aryPara);
			onRefresh();
			showHintMessage(NCLangRes.getInstance().getStrByID("40040303", "UPP40040303-000021"));
		} catch (Exception e) {
			showHintMessage(NCLangRes.getInstance().getStrByID("40040303", "UPP40040303-000022"));
			SCMEnv.out("反检失败，失败原因：\n" + e.getMessage());
			if (((e instanceof RemoteException)) || ((e instanceof BusinessException))) {
				MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040303", "UPP40040303-000019"), e.getMessage());
			}
			return;
		}
		showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000059"));
	}

	public void onCheckRpt() {
		if (getRowLast() < 0) {
			showWarningMessage(NCLangRes.getInstance().getStrByID("40040303", "UPP40040303-000023"));
		} else {
			int row = getRowLast();

			CheckRsltVO vo = null;

			String carriveorder_bid = ((ArriveorderItemVO[]) this.m_arriveVO.getChildrenVO())[row].getPrimaryKey();
			if ((carriveorder_bid == null) || (carriveorder_bid.trim().length() == 0)) { return; }
			CheckRsltItemVO[] items = (CheckRsltItemVO[]) null;
			try {
				items = ArriveorderHelper.queryAllChkInfo(carriveorder_bid);
			} catch (Exception e) {
				if ((e instanceof BusinessException)) showErrorMessage(e.getMessage());
				return;
			}
			if ((items == null) || (items.length == 0)) { return; }

			for (int i = 0; i < items.length; i++) {
				items[i].setCinvcode((String) getBillCardPanel().getBodyValueAt(row, "cinventorycode"));
				items[i].setCinvname((String) getBillCardPanel().getBodyValueAt(row, "cinventoryname"));

				if (!items[i].getBchange().booleanValue()) {
					items[i].setCmainunit((String) getBillCardPanel().getBodyValueAt(row, "cmainmeasname"));
					items[i].setCassisunitname((String) getBillCardPanel().getBodyValueAt(row, "cassistunitname"));
				} else {
					Object oTemp = null;
					try {
						oTemp = CacheTool.getCellValue("bd_invbasdoc", "pk_invbasdoc", "pk_measdoc", items[i].getCbaseid());
						if (oTemp != null) {
							oTemp = CacheTool.getCellValue("bd_measdoc", "pk_measdoc", "measname", ((Object[]) oTemp)[0].toString());
							if (oTemp != null) items[i].setCmainunit(((Object[]) oTemp)[0].toString());
						}
						oTemp = CacheTool.getCellValue("bd_measdoc", "pk_measdoc", "measname", items[i].getCassisunit());
						if (oTemp == null) continue;
						items[i].setCassisunitname(((Object[]) oTemp)[0].toString());
					} catch (Exception ee) {
						SCMEnv.out(ee);
					}

				}

			}

			vo = new CheckRsltVO();

			vo.setChildrenVO(items);

			UIDialog dialog = new UIDialog(this);
			dialog.setLocation(110, 160);
			dialog.setSize(800, 300);

			getcheckRpt().setBillValueVO(vo);

			for (int i = 0; i < items.length; i++) {
				if (items[i].getBchange().booleanValue()) {
					Object oTemp = null;
					try {
						oTemp = CacheTool.getCellValue("bd_invbasdoc", "pk_invbasdoc", "invcode", items[i].getCbaseid());
						if (oTemp != null) getcheckRpt().setBodyValueAt(((Object[]) oTemp)[0], i, "changedinvcode");
						oTemp = CacheTool.getCellValue("bd_invbasdoc", "pk_invbasdoc", "invname", items[i].getCbaseid());
						if (oTemp == null) continue;
						getcheckRpt().setBodyValueAt(((Object[]) oTemp)[0], i, "changedinvname");
					} catch (Exception ee) {
						SCMEnv.out(ee);
					}
				}
			}

			dialog.getContentPane().setLayout(new BorderLayout(10, 10));
			dialog.getContentPane().add(this.m_checkRpt);
			dialog.showModal();
		}

		showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000055"));
	}

	private void onQuery() {
		getQueryConditionDlg().showModal();
		if (getQueryConditionDlg().isCloseOK()) {
			if ((this.m_rdoChecked != null) && (this.m_rdoChecked.isSelected())) this.m_checkStatus = 2;
			if ((this.m_rdoNoCheck != null) && (this.m_rdoNoCheck.isSelected())) this.m_checkStatus = 0;
			if ((this.m_rdoWtRst != null) && (this.m_rdoWtRst.isSelected())) this.m_checkStatus = 1;
			onRefresh();
			updateUI();
		} else {
			this.m_checkStatus = -1;
		}
		showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH009"));
	}

	private void onRefresh() {
		showHintMessage(NCLangRes.getInstance().getStrByID("40040303", "UPP40040303-000024"));
		getArriveVOFromDB();
		if ((getM_arriveVO() != null) && (getM_arriveVO().getChildrenVO() != null) && (getM_arriveVO().getChildrenVO().length > 0)) {
			setConvertrate(getM_arriveVO());
			getBillCardPanel().setBillValueVO(getM_arriveVO());
			getBillCardPanel().updateValue();

			PuTool.loadSourceInfoAll(getBillCardPanel(), "23");

			getBillCardPanel().getBillModel().execLoadFormula();
			showHintMessage(NCLangRes.getInstance().getStrByID("40040303", "UPP40040303-000025"));
		} else {
			showHintMessage(NCLangRes.getInstance().getStrByID("40040303", "UPP40040303-000026"));
		}
		setButtonsState();
		updateUI();
		showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH007"));
	}

	private void onSelectAll() {
		for (int i = 0; i < getBillCardPanel().getBillModel().getRowCount(); i++) {
			getBillCardPanel().getBillModel().setRowState(i, 4);
		}

		int iCount = getBillCardPanel().getBillTable().getRowCount();
		setDispCheck(iCount);
		this.m_btnCancelAll.setEnabled(true);
		this.m_btnSelectAll.setEnabled(false);

		for (int i = 0; i < this.m_aryCheckButtons.length; i++) {
			updateButton(this.m_aryCheckButtons[i]);
		}
		updateButtons();
		showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000033"));
	}

	private void onSelectNo() {
		for (int i = 0; i < getBillCardPanel().getBillModel().getRowCount(); i++) {
			getBillCardPanel().getBillModel().setRowState(i, 0);
		}

		int iCount = getBillCardPanel().getBillTable().getRowCount();
		setDispCheck(iCount);
		this.m_btnSelectAll.setEnabled(true);
		this.m_btnCancelAll.setEnabled(false);

		for (int i = 0; i < this.m_aryCheckButtons.length; i++) {
			updateButton(this.m_aryCheckButtons[i]);
		}

		updateButtons();
		showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000034"));
	}

	private void setButtonsState() {
		setButtons(this.m_aryCheckButtons);

		if ((getM_arriveVO() != null) && (getM_arriveVO().getChildrenVO() != null) && (getM_arriveVO().getChildrenVO().length > 0)) {
			boolean flag = true;
			if (this.m_checkStatus == 1) flag = !flag;
			for (int i = 0; i < this.m_aryCheckButtons.length; i++) {
				this.m_aryCheckButtons[i].setEnabled(flag);
			}
			this.m_btnCancelAll.setEnabled(false);
			this.m_btnCheck.setEnabled(false);
			this.m_btnCheckCancel.setEnabled(false);
			this.m_btnCheckRpt.setEnabled(false);

			if (this.m_checkStatus == 2) {
				this.m_btnCheckRpt.setEnabled(true);
				this.m_btnCheck.setEnabled(true);
			}
			if (this.m_checkStatus == 1) this.m_btnCheck.setEnabled(false);
		} else {
			for (int i = 0; i < this.m_aryCheckButtons.length; i++) {
				this.m_aryCheckButtons[i].setEnabled(false);
			}
		}
		this.m_btnQuery.setEnabled(true);
		this.m_btnAudit.setEnabled(false);

		if (!this.m_isQCEnable) {
			this.m_btnCheckRpt.setEnabled(false);
		}
		for (int i = 0; i < this.m_aryCheckButtons.length; i++)
			updateButton(this.m_aryCheckButtons[i]);
	}

	private void setConvertrate(ArriveorderVO vo) {
		ArriveorderItemVO[] items = (ArriveorderItemVO[]) vo.getChildrenVO();
		int rowCount = items.length;
		String[] sBaseIds = new String[rowCount];
		String[] sAssisUnit = new String[rowCount];
		for (int i = 0; i < rowCount; i++) {
			sBaseIds[i] = items[i].getCbaseid();
			sAssisUnit[i] = items[i].getCassistunit();
		}

		PuTool.loadBatchAssistManaged(sBaseIds);

		PuTool.loadBatchInvConvRateInfo(sBaseIds, sAssisUnit);
		for (int i = 0; i < rowCount; i++) {
			boolean bIsAssUnitMaga = PuTool.isAssUnitManaged(sBaseIds[i]);

			if (!bIsAssUnitMaga) continue;
			if (!PuTool.isFixedConvertRate(sBaseIds[i], sAssisUnit[i])) {
				UFDouble dArrNum = items[i].getNarrvnum();
				UFDouble dAssisNum = items[i].getNassistnum();
				if ((dArrNum != null) && (dAssisNum != null) && (!dAssisNum.equals(new UFDouble(0.0D)))) items[i].setConvertrate(dArrNum.div(dAssisNum));
				else items[i].setConvertrate(null);
			} else {
				UFDouble dConvertrate = PuTool.getInvConvRateValue(sBaseIds[i], sAssisUnit[i]);
				items[i].setConvertrate(dConvertrate);
			}
		}
	}

	private void setDispCheck(int cnt) {
		int iRowCount = getBillCardPanel().getBillModel().getRowCount();

		if (cnt > 0) {
			if (isCanCheck()) this.m_btnCheck.setEnabled(true);
			else {
				this.m_btnCheck.setEnabled(false);
			}

			if (isCanCheckCancel()) this.m_btnCheckCancel.setEnabled(true);
			else {
				this.m_btnCheckCancel.setEnabled(false);
			}

		} else {
			this.m_btnCheck.setEnabled(false);
			this.m_btnCheckCancel.setEnabled(false);
		}

		if (cnt == iRowCount) this.m_btnSelectAll.setEnabled(false);
		else {
			this.m_btnSelectAll.setEnabled(true);
		}

		if (cnt <= 0) this.m_btnCancelAll.setEnabled(false);
		else {
			this.m_btnCancelAll.setEnabled(true);
		}

		if ((this.m_isQCEnable) && (this.m_checkStatus == 1)) {
			for (int i = 0; i < this.m_aryCheckButtons.length; i++) {
				this.m_aryCheckButtons[i].setEnabled(false);
			}
			this.m_btnQuery.setEnabled(true);
			this.m_btnPrint.setEnabled(true);
			this.m_btnPrintPreview.setEnabled(true);
		}

		for (int i = 0; i < this.m_aryCheckButtons.length; i++) {
			updateButton(this.m_aryCheckButtons[i]);
		}
		updateButtons();
	}

	private void setM_arriveVO(ArriveorderVO newM_arriveVO) {
		this.m_arriveVO = newM_arriveVO;

		if (this.m_arriveVO != null) this.m_itemVOs = ((ArriveorderItemVO[]) this.m_arriveVO.getChildrenVO());
	}

	public void valueChanged(ListSelectionEvent e) {
		int iSelCnt = 0;

		int iCount = getBillCardPanel().getBillTable().getRowCount();
		for (int i = 0; i < iCount; i++) {
			getBillCardPanel().getBillModel().setRowState(i, 0);
		}

		int[] iaSelectedRow = getBillCardPanel().getBillTable().getSelectedRows();
		if ((iaSelectedRow == null) || (iaSelectedRow.length == 0)) {
			setDispCheck(0);
		} else {
			iSelCnt = iaSelectedRow.length;

			for (int i = 0; i < iSelCnt; i++) {
				getBillCardPanel().getBillModel().setRowState(iaSelectedRow[i], 4);
			}
		}

		setDispCheck(iSelCnt);
		updateButtons();
	}

	public Object[] getRelaSortObjectArray() {
		return this.m_itemVOs;
	}

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

	private void refreshVoFieldsByKey(ArriveorderVO vo, String strKey) throws Exception {
		ArrayList arrRet = ArriveorderHelper.queryForSaveAudit(strKey);
		((ArriveorderHeaderVO) vo.getParentVO()).setDauditdate((UFDate) arrRet.get(0));
		((ArriveorderHeaderVO) vo.getParentVO()).setCauditpsn((String) arrRet.get(1));
		((ArriveorderHeaderVO) vo.getParentVO()).setIbillstatus((Integer) arrRet.get(2));
		((ArriveorderHeaderVO) vo.getParentVO()).setTs((String) arrRet.get(3));
		((ArriveorderHeaderVO) vo.getParentVO()).setTmaketime((UFDateTime) arrRet.get(4));
		((ArriveorderHeaderVO) vo.getParentVO()).setTaudittime((UFDateTime) arrRet.get(5));
		((ArriveorderHeaderVO) vo.getParentVO()).setTlastmaketime((UFDateTime) arrRet.get(6));
	}

	private GeneralBillItemVO getAddLocatorVOInItemVO(String cwarehouseid, String cwarehouse, String Pk_corp, String cstoreorganization, String cspaceid, String vspacename, GeneralBillItemVO changeVo, boolean IsCalcInNum, int crowindex) throws BusinessException {
		GeneralBillItemVO bo = new GeneralBillItemVO();
		bo = changeVo;
		bo.setIsok(new UFBoolean("N"));
		bo.setBarcodeClose(new UFBoolean("N"));
		bo.setBreturnprofit(new UFBoolean("N"));
		bo.setAttributeValue("bsafeprice", new UFBoolean("N"));
		bo.setAttributeValue("btoinzgflag", new UFBoolean("N"));
		bo.setAttributeValue("btoouttoiaflag", new UFBoolean("N"));
		bo.setAttributeValue("bzgflag", new UFBoolean("N"));
		bo.setFchecked(Integer.valueOf(0));

		bo.setIsok(new UFBoolean("N"));
		bo.setStatus(0);
		bo.setCrowno(String.valueOf((crowindex + 1) * 10));
		bo.setAttributeValue("cvendorid", cwarehouse);
		bo.setAttributeValue("pk_invoicecorp", Pk_corp);
		bo.setAttributeValue("pk_bodycalbody", cstoreorganization);
		bo.setBarcodeClose(new UFBoolean("N"));
		bo.setAttributeValue("pk_reqcorp", Pk_corp);
		bo.setAttributeValue("bonroadflag", new UFBoolean("N"));
		bo.setAttributeValue("idesatype", Integer.valueOf(0));
		bo.setNbarcodenum(new UFDouble("0"));
		try {
			if (IsCalcInNum) {
				getInNum(bo);
			}

			String batchcode = bo.getVbatchcode();
			if ((batchcode == null) || (batchcode.equals("")) || (batchcode.equalsIgnoreCase("null"))) {
				bo.setVbatchcode(null);
			}
			if (Iscsflag(cwarehouseid)) {
				bo.setCspaceid(cspaceid);
				bo.setVspacename(vspacename);
				bo.setLocStatus(2);
				LocatorVO[] lvos = new LocatorVO[1];
				LocatorVO voSpace = new LocatorVO();
				lvos[0] = voSpace;
				voSpace.setCspaceid(cspaceid);
				voSpace.setVspacename(vspacename);
				voSpace.setCwarehouseid(cwarehouseid);
				voSpace.setNinspacenum(bo.getNinnum());
				if (bo.getHsl() != null) {
					voSpace.setNinspaceassistnum(bo.getNinnum().multiply(bo.getHsl()));
				} else voSpace.setNinspaceassistnum(null);

				voSpace.setNingrossnum(null);
				LocatorVO[] voLoc = lvos;
				bo.setLocator(voLoc);
			}
		} catch (BusinessException e) {
			throw e;
		}

		bo.setStatus(2);

		return bo;
	}

	private UFDate getOperDate() {
		UFDate ufd = null;
		ufd = getClientEnvironment().getDate();
		return ufd;
	}

	private void getInNum(GeneralBillItemVO generalBillItemVO) throws BusinessException {
		try {
			String innum = "0";
			String nprice = "0";
			String SQL = "select narrvnum, nvl(nelignum,0)-nvl(naccumwarehousenum,0) as innum,nprice from  po_arriveorder_b where carriveorder_bid='" + generalBillItemVO.getCsourcebillbid() + "'";

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
				}
			}

			String narrvnum = String.valueOf(values.get(0));
			innum = String.valueOf(values.get(1));
			if ((generalBillItemVO.getFlargess() == null) || (!generalBillItemVO.getFlargess().booleanValue())) {
				nprice = String.valueOf(values.get(2));

				if ((nprice == null) || (nprice.equalsIgnoreCase("null")) || (new UFDouble(nprice).compareTo(new UFDouble("0")) == 0)) { throw new BusinessException("单价为空或者为0!Price is empty or 0!"); }
				if ((innum == null) || (innum.equalsIgnoreCase("null")) || (new UFDouble(innum).compareTo(new UFDouble("0")) == 0)) { throw new BusinessException("数量为空或者为0!Quantity is empty or 0!"); }
			}

			UFDouble nmny = new UFDouble(nprice).multiply(new UFDouble(innum));
			generalBillItemVO.setNinnum(new UFDouble(innum));
			generalBillItemVO.setNshouldinnum(new UFDouble(narrvnum));
			generalBillItemVO.setNprice(new UFDouble(nprice));
			generalBillItemVO.setAttributeValue("nmny", nmny);
		} catch (BusinessException e) {
			throw e;
		}
	}

	private ArriveorderItemVO[] InsertItemVo(ArriveorderVO arrvo, ArriveorderItemVO arritemvo, String carriveorderbid) {
		ArriveorderItemVO[] tempvo = (ArriveorderItemVO[]) null;
		int VoCount = arrvo.getChildrenVO().length;
		tempvo = new ArriveorderItemVO[VoCount + 1];

		Boolean isexit = Boolean.valueOf(false);
		for (int i = 0; i < VoCount; i++) {
			if ((tempvo[i] == null) || (!tempvo[i].getPrimaryKey().equals(carriveorderbid))) continue;
			isexit = Boolean.valueOf(true);
			break;
		}

		if (!isexit.booleanValue()) {
			for (int i = 0; i < VoCount; i++) {
				tempvo[i] = ((ArriveorderItemVO) arrvo.getChildrenVO()[i]);
				if (i + 1 == VoCount) {
					tempvo[(i + 1)] = arritemvo;
				}
			}
		}
		return !isexit.booleanValue() ? tempvo : (ArriveorderItemVO[]) arrvo.getChildrenVO();
	}

	private Hashtable geArriveVOs() throws Exception {
		Hashtable arrvotb = new Hashtable();
		ArriveorderVO[] voObj = (ArriveorderVO[]) null;
		int voCount = 0;
		for (int i = 0; i < getBillCardPanel().getBillModel().getRowCount(); i++) {
			float checknum = 0.0F;
			if (getBillCardPanel().getBillModel().getValueAt(i, "audittednum") != null) checknum = Float.valueOf(getBillCardPanel().getBillModel().getValueAt(i, "audittednum").toString()).floatValue();
			else {
				if (checknum == 0.0F) continue;
			}
			if ((getBillCardPanel().getBillModel().getRowState(i) != 4) || (checknum <= 0.0F)) continue;
			ArriveorderVO headArrvo = null;
			String carriveorderid = this.m_arriveVO.getBodyVo()[i].m_carriveorderid;
			String carriveorderbid = this.m_arriveVO.getBodyVo()[i].m_carriveorder_bid;
			headArrvo = ArriveorderHelper.findByPrimaryKey(carriveorderid);
			for (int j = 0; j < headArrvo.getChildrenVO().length; j++) {
				if (!headArrvo.getChildrenVO()[j].getPrimaryKey().equals(carriveorderbid)) continue;
				ArriveorderVO selectArrvo = new ArriveorderVO();
				ArriveorderItemVO[] arrvoItem = (ArriveorderItemVO[]) null;
				if (arrvotb.containsKey(carriveorderid)) {
					ArriveorderVO tempvp = (ArriveorderVO) arrvotb.get(carriveorderid);
					arrvoItem = InsertItemVo(tempvp, (ArriveorderItemVO) headArrvo.getChildrenVO()[j], carriveorderbid);
					selectArrvo.setParentVO(headArrvo.getHeadVO());
					selectArrvo.setChildrenVO(arrvoItem);
				} else if (!arrvotb.containsKey(carriveorderid)) {
					selectArrvo.setParentVO(headArrvo.getHeadVO());
					selectArrvo.setChildrenVO(new ArriveorderItemVO[] {
						(ArriveorderItemVO) headArrvo.getChildrenVO()[j]
					});
					voCount++;
				}
				arrvotb.put(carriveorderid, selectArrvo);

				break;
			}

		}

		if (arrvotb.isEmpty()) {
			MessageDialog.showErrorDlg(this, "审核检验单Audit inspection documents", "没有选择数据!Do not select the data!");
			return null;
		}
		return arrvotb;
	}

	private void doAudit() throws Exception {
		int iRealPos = 0;
		int i = 0;
		boolean IsDeafultSave = false;
		Hashtable ht_arrvo = geArriveVOs();
		if (ht_arrvo.isEmpty()) { return; }
		Enumeration keys = ht_arrvo.keys();
		while (keys.hasMoreElements()) {
			Object returnobj = null;
			int chkCount = 0;
			String key_arri = (String) keys.nextElement();
			ArriveorderVO arrvos = (ArriveorderVO) ht_arrvo.get(key_arri);
			String calbody = ((ArriveorderHeaderVO) arrvos.getParentVO()).getCstoreorganization();
			this.errorht = new Hashtable();

			if (!IsNotLockAccount(getOperDate().toString(), calbody)) {
				MessageDialog.showErrorDlg(this, "检验到货单Inspection documents", "审核期间处于关帐期间，不能入库!Audit period is during the Closing，Not warehousing!");
				return;
			}

			if (arrvos.getHeadVO().getIbillstatus().intValue() != 3) {
				IsDeafultSave = true;
				Vector v = new Vector();
				ArriveorderHeaderVO head = new ArriveorderHeaderVO();
				head = (ArriveorderHeaderVO) arrvos.getParentVO();

				head.setCauditpsn(getOperatorId());
				head.setDauditdate(getOperDate());
				v.add(arrvos);

				ArriveorderVO[] arrivevos = (ArriveorderVO[]) null;
				if (v.size() > 0) {
					arrivevos = new ArriveorderVO[v.size()];
					v.copyInto(arrivevos);
				}
				try {
					for (int j = 0; j < arrivevos.length; j++) {
						arrivevos[j].getParentVO().setAttributeValue("cuserid", getOperatorId());
					}
					boolean isSucc = false;

					String strErr = PuTool.getAuditLessThanMakeMsg(arrivevos, "dreceivedate", "varrordercode", ClientEnvironment.getInstance().getDate(), "23");
					if (strErr != null) { throw new BusinessException(strErr); }

					ArriveorderHeaderVO[] heads = new ArriveorderHeaderVO[arrivevos.length];
					for (int j = 0; j < arrivevos.length; j++) {
						heads[j] = ((ArriveorderHeaderVO) arrivevos[j].getParentVO());
					}

					arrivevos = RcTool.getRefreshedVOs(arrivevos);

					for (int r = 0; r < arrivevos.length; r++) {
						if (!checkVO(arrivevos[r])) {
							continue;
						}

						chkCount = arrvos.getChildrenVO().length - this.errorht.size() == 0 ? arrvos.getChildrenVO().length : arrvos.getChildrenVO().length - this.errorht.size();
						new PfUtilBO().processAction("APPROVE", "23", ClientEnvironment.getInstance().getDate().toString(), null, arrivevos[r], null);
					}

				} catch (Exception e) {
					SCMEnv.out(e);
					throw new Exception(e.getMessage());
				}

			}

			if (!IsPO(arrvos.getParentVO().getAttributeValue("cbiztype").toString())) {
				continue;
			}
			if (!checkVO(arrvos)) {
				chkCount = arrvos.getChildrenVO().length - this.errorht.size() == 0 ? arrvos.getChildrenVO().length : arrvos.getChildrenVO().length - this.errorht.size();
			}
			if ((chkCount == arrvos.getChildrenVO().length) && (this.errorht.size() > 0)) {
				continue;
			}
			if (chkCount > 0) {
				ArriveorderItemVO[] newChidVO = new ArriveorderItemVO[chkCount];
				int k = 0;
				for (int r = 0; r < arrvos.getChildrenVO().length; r++) {
					if (this.errorht.containsKey(arrvos.getChildrenVO()[r].getPrimaryKey())) continue;
					newChidVO[k] = ((ArriveorderItemVO) arrvos.getChildrenVO()[r]);
					k++;
				}

				arrvos.setChildrenVO(newChidVO);
			}
			try {
				refreshVoFieldsByKey(arrvos, arrvos.getParentVO().getPrimaryKey());
				UFDate auditdate = arrvos.getHeadVO().getDauditdate();
				AggregatedValueObject[] generVO = PfUtilUITools.runChangeDataAry("23", "45", new AggregatedValueObject[] {
					arrvos
				});
				String OperUser = getClientEnvironment().getUser().getPrimaryKey();
				String Pk_corp = getClientEnvironment().getCorporation().getPk_corp();
				GeneralBillVO changeVo = (GeneralBillVO) generVO[0];
				GeneralBillVO saveVo = new GeneralBillVO();
				saveVo.setParentVO(changeVo.getHeaderVO());
				saveVo.setLockOperatorid(OperUser);
				String cwarehouse = (String) saveVo.getHeaderVO().getAttributeValue("cproviderid");
				String cstoreorganization = (String) saveVo.getHeaderVO().getAttributeValue("pk_calbody");
				saveVo.getHeaderVO().setStatus(2);
				int voCount = changeVo.getChildrenVO().length;
				saveVo.setChildrenVO(changeVo.getChildrenVO());
				String cwarehouseid = saveVo.getHeaderVO().getCwarehouseid();
				for (int j = 0; j < voCount; j++) {
					GeneralBillItemVO tempvo = (GeneralBillItemVO) changeVo.getChildrenVO()[j];

					String cspaceid = setSpace(tempvo.getCsourcebillbid(), arrvos.getChildrenVO());
					String vspacename = setSpaceName(tempvo.getCsourcebillbid(), arrvos.getChildrenVO());
					boolean IsCalcInNum = true;

					GeneralBillItemVO bo = getAddLocatorVOInItemVO(cwarehouseid, cwarehouse, Pk_corp, cstoreorganization, cspaceid, vspacename, tempvo, IsCalcInNum, j);

					bo.setDbizdate(auditdate);
					saveVo.setItem(j, bo);
				}
				returnobj = new PfUtilBO().processAction("SAVE", "45", ClientEnvironment.getInstance().getDate().toString(), null, saveVo, null);
			} catch (Exception e) {
				SCMEnv.out(e);
				e.printStackTrace();
				throw new Exception(e.getMessage());
			}

			if (returnobj == null) continue;
			try {
				ArrayList keyList = (ArrayList) ((ArrayList) returnobj).get(1);
				String generVoKey = (String) keyList.get(0);
				GeneralBillVO newVo = new GeneralBillVO();

				QryConditionVO voCond = new QryConditionVO();
				voCond.setQryCond("head.cbilltypecode='45' and head.cgeneralhid='" + generVoKey + "'");
				voCond.setDirty(false);
				ArrayList alListData = GeneralBillHelper.queryBills("45", voCond);
				newVo = (GeneralBillVO) alListData.get(0);
				newVo.setLockOperatorid(getClientEnvironment().getUser().getPrimaryKey());
				String Pk_corp = getClientEnvironment().getCorporation().getPk_corp();
				newVo.getHeaderVO().setStatus(3);
				newVo.getHeaderVO().setFreplenishflag(new UFBoolean("N"));
				new PfUtilBO().processAction("SIGN", "45", newVo.getHeaderVO().getDbilldate().toString(), null, newVo, null);
			} catch (Exception e) {
				SCMEnv.out(e);
				MessageDialog.showErrorDlg(this, "审核检验单Audit inspection documents", e.getMessage());
			}
		}

		this.errorht = null;
		onRefresh();
		this.m_btnAudit.setEnabled(false);
		updateButton(this.m_btnAudit);
	}

	private String setSpace(String key, CircularlyAccessibleValueObject[] childrenVO) {
		String spaceid = new String();
		for (int i = 0; i < childrenVO.length; i++) {
			if (((ArriveorderItemVO) childrenVO[i]).getPrimaryKey().equals(key)) {
				spaceid = (String) ((ArriveorderItemVO) childrenVO[i]).getAttributeValue("cstoreid");
			}
		}

		return spaceid;
	}

	private String setSpaceName(String key, CircularlyAccessibleValueObject[] childrenVO) {
		String spacename = new String();
		for (int i = 0; i < childrenVO.length; i++) {
			if (((ArriveorderItemVO) childrenVO[i]).getPrimaryKey().equals(key)) {
				spacename = (String) ((ArriveorderItemVO) childrenVO[i]).getAttributeValue("vstorename");
			}
		}

		return spacename;
	}

	public boolean checkVO(ArriveorderVO vo) {
		try {
			try {
				checkFreeItemAndBatchAndSpaceInput(vo);
			} catch (ValidationException e) {
				showErrorMessage(e.getMessage());
				return false;
			}

		} catch (Exception e) {
			return false;
		}

		return true;
	}

	public void checkFreeItemAndBatchAndSpaceInput(AggregatedValueObject hvo) throws ValidationException {
		StringBuilder errMessage = new StringBuilder();
		ArriveorderItemVO[] vo = (ArriveorderItemVO[]) hvo.getChildrenVO();
		String vbillCode = ((ArriveorderHeaderVO) hvo.getParentVO()).getVarrordercode();
		for (int i = 0; i < vo.length; i++) {
			HashMap invhm = getInvdocHashtable(vo[i].m_cmangid);
			if ((invhm == null) || (invhm.size() <= 0)) { throw new ValidationException("查询出异常存货信息!Query exception inventory information!"); }

			String invcode = String.valueOf(invhm.get("invcode"));
			String key = vo[i].getPrimaryKey();
			try {
				if (Iscsflag(vo[i].getCwarehouseid())) {
					String storeid = vo[i].getCstoreid();
					if ((storeid == null) || (storeid.equals("")) || (storeid.equals("null"))) {
						if (!this.errorht.containsKey(key)) {
							ArrayList volist = new ArrayList();
							volist.add(vo[i]);
							this.errorht.put(key, volist);
						} else {
							ArrayList volist = (ArrayList) this.errorht.get(key);
							volist.add(vo[i]);
							this.errorht.put(key, volist);
						}
						errMessage.append(Transformations.getLstrFromMuiStr(new StringBuilder("单号@Document number&:").append(vbillCode).append("&行号@Line number&:").append(vo[i].m_crowno).append("& 存货@Inventories&").append(invcode).append("&的货位不能为空!@of cargo space can not be empty!").toString()) + "\n");
					} else {
						String Msg = null;
						try {
							Msg = checkstorectl(vo[i].m_cmangid, vo[i].getCwarehouseid(), storeid);
						} catch (Exception ex) {
							throw new ValidationException(ex.getMessage());
						}
						if (Msg != null) {
							if (!this.errorht.containsKey(key)) {
								ArrayList volist = new ArrayList();
								volist.add(vo[i]);
								this.errorht.put(key, volist);
							} else {
								ArrayList volist = (ArrayList) this.errorht.get(key);
								volist.add(vo[i]);
								this.errorht.put(key, volist);
							}
							Msg = Msg.substring(Msg.indexOf("fs") + 2, Msg.length()) + "@require separate storage!";
							errMessage.append(Transformations.getLstrFromMuiStr(new StringBuilder("单号@Document number&:").append(vbillCode).append("&行号@Line number&:").append(vo[i].m_crowno).append("& 存货@Inventories&").append(invcode).append("&").append(Msg).toString()) + "\n");
						}
					}
				}
			} catch (BusinessException e) {
				throw new ValidationException(e.getMessage());
			}
			String IsBathcMgrt = String.valueOf(invhm.get("isbathcmgrt"));
			IsBathcMgrt = (IsBathcMgrt != null) && (!IsBathcMgrt.equals("")) && (!IsBathcMgrt.equals("null")) && (!IsBathcMgrt.equals("N")) ? "Y" : "N";

			String chkfreeflag = String.valueOf(invhm.get("chkfreeflag"));
			chkfreeflag = (chkfreeflag != null) && (!chkfreeflag.equals("")) && (!chkfreeflag.equals("null")) && (!chkfreeflag.equals("N")) ? "Y" : "N";
			String stockbycheck = String.valueOf(invhm.get("stockbycheck"));
			stockbycheck = (stockbycheck != null) && (!stockbycheck.equals("")) && (!stockbycheck.equals("null")) && (!stockbycheck.equals("N")) ? "Y" : "N";

			if (chkfreeflag.equalsIgnoreCase(stockbycheck)) {
				if (!this.errorht.containsKey(key)) {
					ArrayList volist = new ArrayList();
					volist.add(vo[i]);
					this.errorht.put(key, volist);
				} else {
					ArrayList volist = (ArrayList) this.errorht.get(key);
					volist.add(vo[i]);
					this.errorht.put(key, volist);
				}
				errMessage.append(Transformations.getLstrFromMuiStr(new StringBuilder("单号@Document number&:").append(vbillCode).append("&行号@Line number&:").append(vo[i].m_crowno).append("& 存货@Inventories&").append(invcode).append("&存货的免检、是否根据检验结果入库的属性设置不对!请在[物料生产档案]的<控制信息>中进行设置（二者必勾选其一）!@of the exemption, whether based on the test results storage properties set wrong! Materials production file]> <control information set (two must check one of)!").toString()) + "\n");
			}
			if (new UFBoolean(IsBathcMgrt).booleanValue()) {
				String vbatch = String.valueOf(vo[i].getVproducenum());
				if ((vbatch == null) || (vbatch.equals("")) || (vbatch.equals("null"))) {
					if (!this.errorht.containsKey(key)) {
						ArrayList volist = new ArrayList();
						volist.add(vo[i]);
						this.errorht.put(key, volist);
					} else {
						ArrayList volist = (ArrayList) this.errorht.get(key);
						volist.add(vo[i]);
						this.errorht.put(key, volist);
					}
					errMessage.append(Transformations.getLstrFromMuiStr(new StringBuilder("单号@Document number&:").append(vbillCode).append("&行号@Line number&:").append(vo[i].m_crowno).append("& 存货@Inventories&").append(invcode).append("&启用批次号管理，批次号不能为空!@Enable batch number management, batch number can not be empty!").toString()) + "\n");
				}
			}

			for (int j = 1; j <= 5; j++) {
				String vfreeid = String.valueOf(invhm.get("free" + String.valueOf(j)));
				String vfree = String.valueOf(vo[i].getAttributeValue("vfree" + j));
				if ((vfreeid == null) || (vfreeid.equals("")) || (vfreeid.equals("null"))) {
					continue;
				}
				if ((vfree != null) && (!vfree.equals("")) && (!vfree.equals("null"))) continue;
				if (!this.errorht.containsKey(key)) {
					ArrayList volist = new ArrayList();
					volist.add(vo[i]);
					this.errorht.put(key, volist);
				} else {
					ArrayList volist = (ArrayList) this.errorht.get(key);
					volist.add(vo[i]);
					this.errorht.put(key, volist);
				}
				errMessage.append(Transformations.getLstrFromMuiStr("单号@Document number&:" + vbillCode + "& 存货@Inventories&" + invcode + "&的自由项@free term&" + String.valueOf(j) + "&不能为空!@can not be empty!"));
			}

		}

		if (errMessage.length() > 0) throw new ValidationException(errMessage.toString());
	}

	private HashMap getInvdocHashtable(String key) {
		String sql = "select a.invcode, a.free1,a.free2,a.free3,a.free4,a.free5,wholemanaflag as IsBathcMgrt,chkfreeflag,stockbycheck ";
		sql = sql + " from bd_invbasdoc a ,bd_invmandoc b,bd_produce c  ";
		sql = sql + "where a.pk_invbasdoc=b.pk_invbasdoc  and c.pk_invmandoc=b.pk_invmandoc and b.dr=0 and b.pk_corp='" + ClientEnvironment.getInstance().getCorporation().getPrimaryKey() + "' and b.pk_invmandoc ='" + key + "'";
		IUAPQueryBS sessionManager = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		HashMap inv = null;
		try {
			inv = (HashMap) sessionManager.executeQuery(sql, new MapProcessor());
		} catch (BusinessException e) {
			return null;
		}

		return inv;
	}

	public boolean Iscsflag(String primkey) throws BusinessException {
		boolean rst = false;
		String SQL = "select csflag from bd_stordoc  where pk_stordoc ='" + primkey + "'";
		IUAPQueryBS sessionManager = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		List list = null;
		try {
			list = (List) sessionManager.executeQuery(SQL, new ArrayListProcessor());

			if (list.isEmpty()) { return rst; }

			Iterator iterator = list.iterator();
			while (iterator.hasNext()) {
				ArrayList values = new ArrayList();
				Object obj = iterator.next();
				if (obj == null) {
					continue;
				}
				if (obj.getClass().isArray()) {
					int len = Array.getLength(obj);
					for (int j = 0; j < len; j++) {
						values.add(Array.get(obj, j));
					}
					return rst = new UFBoolean(String.valueOf(values.get(0))).booleanValue();
				}
			}
		} catch (BusinessException e) {
			throw e;
		}
		return rst;
	}

	private boolean IsPO(String cbizitype) {
		String sql = "select busicode from bd_busitype where pk_busitype='" + cbizitype + "'";
		IUAPQueryBS sessionManager = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		HashMap bizitype = null;
		try {
			bizitype = (HashMap) sessionManager.executeQuery(sql, new MapProcessor());
		} catch (BusinessException e) {
			return false;
		}

		if (bizitype.size() <= 0) { return false; }
		String busicode = String.valueOf(bizitype.get("busicode"));

		return (busicode != null) && (!busicode.equalsIgnoreCase("")) && (!busicode.equalsIgnoreCase("null")) && (!busicode.equalsIgnoreCase("C004"));
	}

	private boolean IsNotLockAccount(String dbDate, String calbody) throws Exception {
		boolean rst = false;
		try {
			if (new UFDate(dbDate).compareTo(ClientEnvironment.getServerTime().getDate()) > 0) { return true; }
			StringBuffer Sql = new StringBuffer();
			Sql.append("SELECT  acc.faccountflag  ");
			Sql.append("FROM ic_accountctrl acc, bd_calbody cal  ");
			Sql.append("where acc.pk_calbody = cal.pk_calbody  AND acc.dr = 0  AND acc.pk_calbody = '1016A21000000000WLM3'  ");
			Sql.append("and '" + dbDate + "' between  to_char(to_date(acc.tstarttime,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd') and  to_char(to_date(acc.tendtime,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd') ");
			IUAPQueryBS sessionManager = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			ArrayList al = (ArrayList) sessionManager.executeQuery(Sql.toString(), new ListProcessor());
			if ((al == null) || (al.size() <= 0)) { return true; }
			rst = new UFBoolean(String.valueOf(al.get(0))).booleanValue();
			return rst;
		} catch (Exception e) {
			throw e;
		}
	}

	public String checkstorectl(String inv, String wh, String space) throws Exception {
		StringBuffer Sql = new StringBuffer();
		Sql.append("SELECT  fseparatespace,ffixedspace,cspaceid  ");
		Sql.append("FROM ic_storectl acc, ic_defaultspace cal  ");
		Sql.append("where acc.cstorectlid=cal.cstorectlid  and cwarehouseid='" + wh + "' and cinventoryid='" + inv + "'");
		HashMap invhm = null;
		try {
			IUAPQueryBS sessionManager = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			invhm = (HashMap) sessionManager.executeQuery(Sql.toString(), new MapProcessor());
		} catch (BusinessException e) {
			throw e;
		}

		if ((invhm == null) || (invhm.size() <= 0)) { return null; }

		UFBoolean fseparatespace = new UFBoolean(StringIsNullOrEmpty(invhm.get("fseparatespace")) ? "N" : String.valueOf(invhm.get("fseparatespace")));
		UFBoolean ffixedspace = new UFBoolean(StringIsNullOrEmpty(invhm.get("ffixedspace")) ? "N" : String.valueOf(invhm.get("ffixedspace")));
		String cspaceid = String.valueOf(invhm.get("cspaceid"));
		if ((ffixedspace.booleanValue()) && (!space.equalsIgnoreCase(cspaceid))) { return "ff要求存放固定货位！"; }
		if (fseparatespace.booleanValue()) {
			Sql.setLength(0);
			Sql.append("select nvl(hand.ninspacenum,0.0) - nvl(hand.noutspacenum,0.0) as onhandnum  ");
			Sql.append("from v_ic_onhandnum6 hand  where hand.pk_corp = '" + ClientEnvironment.getInstance().getCorporation().getPrimaryKey() + "' and hand.cspaceid='" + space + "'  and nvl(hand.ninspacenum,0.0) - nvl(hand.noutspacenum,0.0)>0");
			try {
				IUAPQueryBS sessionManager = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
				HashMap handnumhm = (HashMap) sessionManager.executeQuery(Sql.toString(), new MapProcessor());
				if (handnumhm == null) { return null; }
				if (handnumhm.size() > 0) { return "fs要求单独存放！"; }
			} catch (BusinessException e) {
				throw e;
			}

		}

		return null;
	}

	public boolean StringIsNullOrEmpty(Object obj) {
		return obj == null;
	}
}