package nc.ui.ps.settle;

import java.awt.BorderLayout;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import nc.ui.ml.NCLangRes;
import nc.ui.po.pub.PoQueryCondition;
import nc.ui.pu.pub.POPubSetUI;
import nc.ui.pu.pub.PuTool;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.beans.ValueChangedEvent;
import nc.ui.pub.beans.ValueChangedListener;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListData;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillScrollPane;
import nc.ui.pub.para.SysInitBO_Client;
import nc.ui.scm.pub.FreeVOParse;
import nc.ui.scm.pub.def.DefSetTool;
import nc.ui.scm.service.LocalCallService;
import nc.vo.bd.CorpVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.ps.factor.CostfactorHeaderVO;
import nc.vo.ps.factor.CostfactorVO;
import nc.vo.ps.settle.FeeinvoiceVO;
import nc.vo.ps.settle.IinvoiceVO;
import nc.vo.ps.settle.SettlebillHeaderVO;
import nc.vo.ps.settle.SettlebillItemVO;
import nc.vo.ps.settle.SettlebillVO;
import nc.vo.ps.settle.SettletotalVO;
import nc.vo.ps.settle.StockVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.service.ServcallVO;
import nc.vo.sm.UserVO;

public class DifferYFUI extends ToftPanel
  implements BillEditListener, ListSelectionListener, ValueChangedListener
{
  private ButtonObject[] m_buttons1 = null;
  private ButtonObject[] m_buttons2 = null;

  private int[] m_nButtonState = null;

  private BillListPanel m_listPanel = null;
  private UIComboBox m_comDistribute = null;

  private BillListPanel m_totalListPanel = null;

  private String m_sUnitCode = getCorpPrimaryKey();

  private PoQueryCondition m_condClient1 = null;
  private PoQueryCondition m_condClient2 = null;

  private StockVO[] m_stockVOs = null;
  private IinvoiceVO[] m_invoiceVOs = null;
  private SettletotalVO[] m_totalVOs = null;
  private FeeinvoiceVO[] m_feeVOs = null;
  private FeeinvoiceVO[] m_discountVOs = null;
  private SettlebillItemVO[] m_settleVOs = null;

  private StockVO[] m_mStockVOs = null;
  private IinvoiceVO[] m_mInvoiceVOs = null;
  private FeeinvoiceVO[] m_mFeeVOs = null;
  private FeeinvoiceVO[] m_mDiscountVOs = null;

  private UFBoolean[] m_bIsentercost = null;

  private Vector m_vID = null;

  private boolean m_bStock = false;
  private boolean m_bInvoice = false;
  private boolean m_bDistribute = false;
  private boolean m_bInvoiceDistribute = false;

  private String m_sEstimateMode = null;

  private String m_sDifferenceMode = null;

  private int m_unitDecimal = 2;

  private int m_priceDecimal = 2;

  private int m_moneyDecimal = 2;

  private boolean m_bSettling = false;

  private Integer[] m_nCostNO = null;

  private boolean m_bICStartUp = false;

  private boolean m_bCTStartUp = false;

  private int[] measure = null;

  private CostfactorVO[] CostfactorVOtempVO = null;

  private UFBoolean m_bIc2PiSettle = new UFBoolean(false);

  private UFBoolean m_bZGYF = new UFBoolean(false);

  private UIRefPane m_corpPane = null;

  private UIRefPane m_warePane = null;

  public DifferYFUI()
  {
    init();
  }

  public void afterEdit(BillEditEvent event)
  {
    if (isFocusListHead(event)) {
      computeHeadData(event);
      return;
    }

    if (isFocusListBody(event)) computeBodyData(event);
  }

  public void bodyRowChange(BillEditEvent event)
  {
  }

  private void changeButtonState()
  {
    if (getBillListPanel().isVisible())
      for (int i = 0; i < this.m_nButtonState.length; i++) {
        if (this.m_nButtonState[i] == 0) {
          this.m_buttons1[i].setVisible(true);
          this.m_buttons1[i].setEnabled(true);
        }
        if (this.m_nButtonState[i] == 1) {
          this.m_buttons1[i].setVisible(true);
          this.m_buttons1[i].setEnabled(false);
        }
        if (this.m_nButtonState[i] == 2) {
          this.m_buttons1[i].setVisible(false);
        }

        updateButton(this.m_buttons1[i]);
      }
    else
      for (int i = 0; i < this.m_nButtonState.length; i++) {
        if (this.m_nButtonState[i] == 0) {
          this.m_buttons2[i].setVisible(true);
          this.m_buttons2[i].setEnabled(true);
        }
        if (this.m_nButtonState[i] == 1) {
          this.m_buttons2[i].setVisible(true);
          this.m_buttons2[i].setEnabled(false);
        }
        if (this.m_nButtonState[i] == 2) {
          this.m_buttons2[i].setVisible(false);
        }

        updateButton(this.m_buttons2[i]);
      }
  }

  private void changeFactorShowName()
  {
    if ((this.m_feeVOs == null) || (this.m_feeVOs.length == 0))
    {
      for (int i = 0; i < 5; i++) {
        getTotalBillListPanel().hideHeadTableCol("nfactor" + (i + 1));
      }
      return;
    }

    Vector vName = new Vector();
    this.m_vID = new Vector();
    this.m_vID.addElement(this.m_feeVOs[0].getCcostfactorid().trim());
    for (int i = 1; i < this.m_feeVOs.length; i++) {
      String s1 = this.m_feeVOs[i].getCcostfactorid().trim();
      if (this.m_vID.contains(s1)) continue; this.m_vID.addElement(s1);
    }

    String[] sCostPK = (String[])null;
    if (this.m_vID.size() > 0) {
      String[] cId = new String[this.m_vID.size()];
      this.m_vID.copyInto(cId);
      try {
        Vector v = SettleHelper.queryCostfactorNO(this.m_sUnitCode, cId);
        if ((v == null) || (v.size() == 0)) return;
        Vector v1 = (Vector)v.elementAt(0);
        if ((v1 != null) && (v1.size() > 0)) {
          this.m_nCostNO = new Integer[v1.size()];
          v1.copyInto(this.m_nCostNO);
        }
        vName = (Vector)v.elementAt(1);
        Vector v2 = (Vector)v.elementAt(2);
        if ((v2 != null) && (v2.size() > 0)) {
          sCostPK = new String[v2.size()];
          v2.copyInto(sCostPK);
        }
      } catch (Exception e) {
        SCMEnv.out(e);
        MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000059"), e.getMessage());
        return;
      }
    }

    if ((sCostPK == null) || (sCostPK.length == 0)) return;
    String[] sName = new String[sCostPK.length];

    for (int i = 0; i < sCostPK.length; i++) {
      String s1 = sCostPK[i].trim();
      for (int j = 0; j < this.m_vID.size(); j++) {
        String s2 = (String)this.m_vID.elementAt(j);
        s2 = s2.trim();
        if (s1.equals(s2)) {
          sName[i] = ((String)vName.elementAt(j));
        }
      }

    }

    for (int i = 1; i <= 5; i++) {
      String sKey = "nfactor" + i;
      getTotalBillListPanel().showHeadTableCol(sKey);
    }

    if ((sName != null) && (sName.length > 0)) {
      BillItem[] items = getTotalBillListPanel().getHeadBillModel().getBodyItems();
      for (int i = 0; i < sName.length; i++) {
        String sName0 = sName[i];
        String sKey = "nfactor" + (this.m_nCostNO[i].intValue() + 1);
        for (int j = 0; j < items.length; j++) {
          String s = items[j].getKey().trim();
          if (!s.equals(sKey)) continue; items[j].setName(sName0);
        }
      }
      BillListData data = new BillListData(getTotalBillListPanel().getTempletData("40040502020200000000"));
      data = initTotalDecimal(data);
      data.setHeadItems(items);
      getTotalBillListPanel().setListData(data);

      getTotalBillListPanel().getParentListPanel().setShowThMark(true);
      getTotalBillListPanel().getChildListPanel().setShowThMark(true);

      getTotalBillListPanel().getParentListPanel().setTotalRowShow(true);
      getTotalBillListPanel().getChildListPanel().setTotalRowShow(true);

      getTotalBillListPanel().addEditListener(this);
      getTotalBillListPanel().addBodyEditListener(this);

      getTotalBillListPanel().getHeadTable().setSortEnabled(false);
      getTotalBillListPanel().getBodyTable().setSortEnabled(false);
    }

    for (int i = 0; i < 5; i++) {
      boolean b = false;
      for (int j = 0; j < this.m_nCostNO.length; j++) {
        int k = this.m_nCostNO[j].intValue();
        if (i == k) {
          b = true;
          break;
        }
      }
      if (b) continue; getTotalBillListPanel().hideHeadTableCol("nfactor" + (i + 1));
    }
  }

  private boolean checkQueryCondition(ConditionVO[] VOs)
  {
    boolean b = true;
    if ((VOs == null) || (VOs.length == 0)) return b;

    if (this.m_bCTStartUp) return b;

    for (int i = 0; i < VOs.length; i++) {
      String sName = VOs[i].getFieldCode().trim();
      String sValue = VOs[i].getValue();

      if ((sName.equals("vcontractcode")) && (sValue != null) && (sValue.trim().length() > 0)) {
        b = false;
        break;
      }
    }

    return b;
  }

  private void computeBodyData(BillEditEvent event)
  {
    int nRow = getTotalBillListPanel().getBodyBillModel().getRowCount();
    FeeinvoiceVO[] tempVOs = new FeeinvoiceVO[nRow];
    for (int i = 0; i < nRow; i++) tempVOs[i] = new FeeinvoiceVO();

    getTotalBillListPanel().getBodyBillModel().getBodyValueVOs(tempVOs);
    nRow = event.getRow();

    int nFee = 0;
    if ((this.m_feeVOs != null) && (this.m_feeVOs.length > 0)) nFee = this.m_feeVOs.length;
    if (this.m_discountVOs != null) nFee=this.m_discountVOs.length;

    if ((tempVOs[nRow].getNsettlemny() == null) || (tempVOs[nRow].getNsettlemny().toString().length() == 0)) {
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000060"), NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000061"));
      if (nRow < nFee)
        getTotalBillListPanel().getBodyBillModel().setValueAt(this.m_feeVOs[nRow].getNnosettlemny(), nRow, "nsettlemny");
      else {
        getTotalBillListPanel().getBodyBillModel().setValueAt(this.m_discountVOs[(nRow - nFee)].getNnosettlemny(), nRow, "nsettlemny");
      }
      return;
    }
    if (nRow < nFee) {
      if (Math.abs(tempVOs[nRow].getNsettlemny().doubleValue()) > Math.abs(this.m_feeVOs[nRow].getNnosettlemny().doubleValue())) {
        MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000060"), NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000062"));
        getTotalBillListPanel().getBodyBillModel().setValueAt(this.m_feeVOs[nRow].getNnosettlemny(), nRow, "nsettlemny");
        return;
      }
      if (tempVOs[nRow].getNsettlemny().doubleValue() * this.m_feeVOs[nRow].getNnosettlemny().doubleValue() <= 0.0D) {
        MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000060"), NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000063"));
        getTotalBillListPanel().getBodyBillModel().setValueAt(this.m_feeVOs[nRow].getNnosettlemny(), nRow, "nsettlemny");
        return;
      }
      this.m_feeVOs[nRow].setNsettlemny(tempVOs[nRow].getNsettlemny());
    }
    else {
      if (Math.abs(tempVOs[nRow].getNsettlemny().doubleValue()) > Math.abs(this.m_discountVOs[(nRow - nFee)].getNnosettlemny().doubleValue())) {
        MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000060"), NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000064"));
        getTotalBillListPanel().getBodyBillModel().setValueAt(this.m_discountVOs[(nRow - nFee)].getNnosettlemny(), nRow, "nsettlemny");
        return;
      }
      if (tempVOs[nRow].getNsettlemny().doubleValue() * this.m_discountVOs[(nRow - nFee)].getNnosettlemny().doubleValue() <= 0.0D) {
        MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000060"), NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000065"));
        getTotalBillListPanel().getBodyBillModel().setValueAt(this.m_discountVOs[(nRow - nFee)].getNnosettlemny(), nRow, "nsettlemny");
        return;
      }
      this.m_discountVOs[(nRow - nFee)].setNsettlemny(tempVOs[nRow].getNsettlemny());
    }
  }

  private void computeHeadData(BillEditEvent event)
  {
    int nRow = getTotalBillListPanel().getHeadBillModel().getRowCount();
    SettletotalVO[] tempVOs = new SettletotalVO[nRow];
    for (int i = 0; i < nRow; i++) tempVOs[i] = new SettletotalVO();

    getTotalBillListPanel().getHeadBillModel().getBodyValueVOs(tempVOs);
    nRow = event.getRow();
    String sType = tempVOs[nRow].getCbilltype().trim();
    if (sType.equals(NCLangRes.getInstance().getStrByID("4004050204", "UPT4004050204-000002"))) {
      if (!this.m_bInvoiceDistribute)
      {
        if ((tempVOs[nRow].getNstocknum() == null) || (tempVOs[nRow].getNstocknum().toString().length() == 0)) {
          MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000066"), NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000067"));
          getTotalBillListPanel().getHeadBillModel().setValueAt(this.m_totalVOs[nRow].getNnosettlenum(), nRow, "nstocknum");
          return;
        }

        double d = tempVOs[nRow].getNprice().doubleValue();
        if (Math.abs(tempVOs[nRow].getNstocknum().doubleValue()) > Math.abs(this.m_totalVOs[nRow].getNnosettlenum().doubleValue())) {
          MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000066"), NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000068"));
          getTotalBillListPanel().getHeadBillModel().setValueAt(this.m_totalVOs[nRow].getNnosettlenum(), nRow, "nstocknum");
          return;
        }
        if (tempVOs[nRow].getNstocknum().doubleValue() * this.m_totalVOs[nRow].getNnosettlenum().doubleValue() <= 0.0D) {
          MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000066"), NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000069"));
          getTotalBillListPanel().getHeadBillModel().setValueAt(this.m_totalVOs[nRow].getNnosettlenum(), nRow, "nstocknum");
          return;
        }

        if (tempVOs[nRow].getNstocknum().doubleValue() == this.m_totalVOs[nRow].getNnosettlenum().doubleValue()) {
          tempVOs[nRow].setNgaugemny(this.m_totalVOs[nRow].getNnosettlemny());
        } else {
          d *= tempVOs[nRow].getNstocknum().doubleValue();
          tempVOs[nRow].setNgaugemny(new UFDouble(d));
        }

        this.m_totalVOs[nRow].setNstocknum(tempVOs[nRow].getNstocknum());
        this.m_totalVOs[nRow].setNgaugemny(tempVOs[nRow].getNgaugemny());
      }
      else {
        if ((tempVOs[nRow].getNinvoicemny() == null) || (tempVOs[nRow].getNinvoicemny().toString().length() == 0)) {
          MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000070"), NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000071"));
          getTotalBillListPanel().getHeadBillModel().setValueAt(this.m_totalVOs[nRow].getNsettlemny(), nRow, "ninvoicemny");
          return;
        }

        if (tempVOs[nRow].getNinvoicemny().doubleValue() * this.m_totalVOs[nRow].getNsettlemny().doubleValue() <= 0.0D) {
          MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000070"), NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000072"));
          getTotalBillListPanel().getHeadBillModel().setValueAt(this.m_totalVOs[nRow].getNsettlemny(), nRow, "ninvoicemny");
          return;
        }
        double d = tempVOs[nRow].getNinvoicemny().doubleValue();
        double dd = this.m_totalVOs[nRow].getNinvoicemny().doubleValue();
        double ddd = this.m_totalVOs[nRow].getNsettlemny().doubleValue();
        ddd = ddd - dd + d;
        tempVOs[nRow].setNsettlemny(new UFDouble(ddd));
        this.m_totalVOs[nRow].setNinvoicemny(new UFDouble(d));
        this.m_totalVOs[nRow].setNsettlemny(new UFDouble(ddd));
      }
    } else {
      if ((tempVOs[nRow].getNinvoicenum() == null) || (tempVOs[nRow].getNinvoicenum().toString().length() == 0)) {
        MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000073"), NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000074"));
        getTotalBillListPanel().getHeadBillModel().setValueAt(this.m_totalVOs[nRow].getNnosettlenum(), nRow, "ninvoicenum");
        return;
      }

      double d = tempVOs[nRow].getNnosettlemny().doubleValue() / tempVOs[nRow].getNnosettlenum().doubleValue();
      if (Math.abs(tempVOs[nRow].getNinvoicenum().doubleValue()) > Math.abs(this.m_totalVOs[nRow].getNnosettlenum().doubleValue())) {
        MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000073"), NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000075"));
        getTotalBillListPanel().getHeadBillModel().setValueAt(this.m_totalVOs[nRow].getNnosettlenum(), nRow, "ninvoicenum");
        return;
      }
      if (tempVOs[nRow].getNinvoicenum().doubleValue() * this.m_totalVOs[nRow].getNnosettlenum().doubleValue() <= 0.0D) {
        MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000073"), NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000076"));
        getTotalBillListPanel().getHeadBillModel().setValueAt(this.m_totalVOs[nRow].getNnosettlenum(), nRow, "ninvoicenum");
        return;
      }

      d *= tempVOs[nRow].getNinvoicenum().doubleValue();
      tempVOs[nRow].setNinvoicemny(new UFDouble(d));
      this.m_totalVOs[nRow].setNinvoicenum(tempVOs[nRow].getNinvoicenum());
      this.m_totalVOs[nRow].setNinvoicemny(tempVOs[nRow].getNinvoicemny());
    }
    getTotalBillListPanel().getHeadBillModel().setBodyDataVO(this.m_totalVOs);
    getTotalBillListPanel().getHeadBillModel().execLoadFormula();

    setPartEditable();
  }

  private void distributeDiscount()
  {
    if ((this.m_discountVOs == null) || (this.m_discountVOs.length == 0))
    {
      return;
    }

    for (int i = 0; i < this.m_discountVOs.length; i++) {
      double dd = this.m_discountVOs[i].getNsettlemny().doubleValue();
      double total = 0.0D;
      double sum = 0.0D;
      for (int j = 0; j < this.m_stockVOs.length; j++) {
        total += this.m_totalVOs[j].getNgaugemny().doubleValue();
      }
      if (total != 0.0D) {
        for (int j = 0; j < this.m_stockVOs.length - 1; j++) {
          double d = this.m_totalVOs[j].getNgaugemny().doubleValue();
          double ddd = this.m_totalVOs[j].getNdiscountmny().doubleValue();
          ddd += dd / total * d;
          this.m_totalVOs[j].setNdiscountmny(new UFDouble(PuTool.getRoundDouble(this.m_moneyDecimal, ddd)));
          sum += PuTool.getRoundDouble(this.m_moneyDecimal, dd / total * d);
        }

        double ddd = this.m_totalVOs[(this.m_stockVOs.length - 1)].getNdiscountmny().doubleValue();
        ddd += dd - sum;
        this.m_totalVOs[(this.m_stockVOs.length - 1)].setNdiscountmny(new UFDouble(PuTool.getRoundDouble(this.m_moneyDecimal, ddd)));
      }
    }
  }

  private void distributeFee()
  {
    if ((this.m_feeVOs == null) || (this.m_feeVOs.length == 0))
    {
      return;
    }

    Vector v = new Vector();
    try {
      v = SettleHelper.getSUnitWeightAndVolume(this.m_stockVOs);
    } catch (Exception e) {
      SCMEnv.out(e);
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000077"), e.getMessage());
      return;
    }
    UFDouble[] unitWeight = new UFDouble[v.size()];
    UFDouble[] unitVolume = new UFDouble[v.size()];
    for (int i = 0; i < v.size(); i++) {
      Vector vTemp = new Vector();
      vTemp = (Vector)v.elementAt(i);
      String s = (String)vTemp.elementAt(0);
      unitWeight[i] = new UFDouble(new Double(s));
      s = (String)vTemp.elementAt(1);
      unitVolume[i] = new UFDouble(new Double(s));
    }

    int[] n = new int[this.m_feeVOs.length];
    for (int i = 0; i < this.m_feeVOs.length; i++) {
      String s1 = this.m_feeVOs[i].getCcostfactorid().trim();
      for (int j = 0; j < this.m_vID.size(); j++) {
        String s2 = (String)this.m_vID.elementAt(j);
        s2 = s2.trim();
        if (s1.equals(s2)) {
          n[i] = this.m_nCostNO[j].intValue();
          break;
        }
      }

    }

    for (int i = 0; i < this.m_feeVOs.length; i++)
      distributeFeeAll(i, n[i], unitWeight, unitVolume);
  }

  private void distributeFeeAll(int nFee, int nCost, UFDouble[] unitWeight, UFDouble[] unitVolume)
  {
    if ((this.m_feeVOs == null) || (this.m_feeVOs.length == 0)) return;
    int nDistribute = this.m_feeVOs[nFee].getFapportionmode().intValue();
    double total = this.m_feeVOs[nFee].getNsettlemny().doubleValue();
    double sum = 0.0D;

    if (nDistribute == 0)
    {
      double nNum = 0.0D;
      for (int i = 0; i < this.m_stockVOs.length; i++) nNum += this.m_totalVOs[i].getNstocknum().doubleValue();
      if (nNum == 0.0D) return;
      for (int i = 0; i < this.m_stockVOs.length - 1; i++) {
        if (nCost == 0) {
          double dd = this.m_totalVOs[i].getNfactor1().doubleValue();
          dd += total / nNum * this.m_totalVOs[i].getNstocknum().doubleValue();
          this.m_totalVOs[i].setNfactor1(new UFDouble(PuTool.getRoundDouble(this.m_moneyDecimal, dd)));
          sum += PuTool.getRoundDouble(this.m_moneyDecimal, total / nNum * this.m_totalVOs[i].getNstocknum().doubleValue());
        }
        if (nCost == 1) {
          double dd = this.m_totalVOs[i].getNfactor2().doubleValue();
          dd += total / nNum * this.m_totalVOs[i].getNstocknum().doubleValue();
          this.m_totalVOs[i].setNfactor2(new UFDouble(PuTool.getRoundDouble(this.m_moneyDecimal, dd)));
          sum += PuTool.getRoundDouble(this.m_moneyDecimal, total / nNum * this.m_totalVOs[i].getNstocknum().doubleValue());
        }
        if (nCost == 2) {
          double dd = this.m_totalVOs[i].getNfactor3().doubleValue();
          dd += total / nNum * this.m_totalVOs[i].getNstocknum().doubleValue();
          this.m_totalVOs[i].setNfactor3(new UFDouble(PuTool.getRoundDouble(this.m_moneyDecimal, dd)));
          sum += PuTool.getRoundDouble(this.m_moneyDecimal, total / nNum * this.m_totalVOs[i].getNstocknum().doubleValue());
        }
        if (nCost == 3) {
          double dd = this.m_totalVOs[i].getNfactor4().doubleValue();
          dd += total / nNum * this.m_totalVOs[i].getNstocknum().doubleValue();
          this.m_totalVOs[i].setNfactor4(new UFDouble(PuTool.getRoundDouble(this.m_moneyDecimal, dd)));
          sum += PuTool.getRoundDouble(this.m_moneyDecimal, total / nNum * this.m_totalVOs[i].getNstocknum().doubleValue());
        }
        if (nCost == 4) {
          double dd = this.m_totalVOs[i].getNfactor5().doubleValue();
          dd += total / nNum * this.m_totalVOs[i].getNstocknum().doubleValue();
          this.m_totalVOs[i].setNfactor5(new UFDouble(PuTool.getRoundDouble(this.m_moneyDecimal, dd)));
          sum += PuTool.getRoundDouble(this.m_moneyDecimal, total / nNum * this.m_totalVOs[i].getNstocknum().doubleValue());
        }
      }

    }
    else if (nDistribute == 1)
    {
      double nNum = 0.0D;
      for (int i = 0; i < this.m_stockVOs.length; i++) nNum += this.m_totalVOs[i].getNgaugemny().doubleValue();
      if (nNum == 0.0D) return;
      for (int i = 0; i < this.m_stockVOs.length - 1; i++) {
        if (nCost == 0) {
          double dd = this.m_totalVOs[i].getNfactor1().doubleValue();
          dd += total / nNum * this.m_totalVOs[i].getNgaugemny().doubleValue();
          this.m_totalVOs[i].setNfactor1(new UFDouble(PuTool.getRoundDouble(this.m_moneyDecimal, dd)));
          sum += PuTool.getRoundDouble(this.m_moneyDecimal, total / nNum * this.m_totalVOs[i].getNgaugemny().doubleValue());
        }
        if (nCost == 1) {
          double dd = this.m_totalVOs[i].getNfactor2().doubleValue();
          dd += total / nNum * this.m_totalVOs[i].getNgaugemny().doubleValue();
          this.m_totalVOs[i].setNfactor2(new UFDouble(PuTool.getRoundDouble(this.m_moneyDecimal, dd)));
          sum += PuTool.getRoundDouble(this.m_moneyDecimal, total / nNum * this.m_totalVOs[i].getNgaugemny().doubleValue());
        }
        if (nCost == 2) {
          double dd = this.m_totalVOs[i].getNfactor3().doubleValue();
          dd += total / nNum * this.m_totalVOs[i].getNgaugemny().doubleValue();
          this.m_totalVOs[i].setNfactor3(new UFDouble(PuTool.getRoundDouble(this.m_moneyDecimal, dd)));
          sum += PuTool.getRoundDouble(this.m_moneyDecimal, total / nNum * this.m_totalVOs[i].getNgaugemny().doubleValue());
        }
        if (nCost == 3) {
          double dd = this.m_totalVOs[i].getNfactor4().doubleValue();
          dd += total / nNum * this.m_totalVOs[i].getNgaugemny().doubleValue();
          this.m_totalVOs[i].setNfactor4(new UFDouble(PuTool.getRoundDouble(this.m_moneyDecimal, dd)));
          sum += PuTool.getRoundDouble(this.m_moneyDecimal, total / nNum * this.m_totalVOs[i].getNgaugemny().doubleValue());
        }
        if (nCost == 4) {
          double dd = this.m_totalVOs[i].getNfactor5().doubleValue();
          dd += total / nNum * this.m_totalVOs[i].getNgaugemny().doubleValue();
          this.m_totalVOs[i].setNfactor5(new UFDouble(PuTool.getRoundDouble(this.m_moneyDecimal, dd)));
          sum += PuTool.getRoundDouble(this.m_moneyDecimal, total / nNum * this.m_totalVOs[i].getNgaugemny().doubleValue());
        }
      }

    }
    else if (nDistribute == 2)
    {
      double nNum = 0.0D;
      for (int i = 0; i < this.m_stockVOs.length; i++) nNum += this.m_totalVOs[i].getNstocknum().doubleValue() * unitWeight[i].doubleValue();
      if (nNum == 0.0D) return;
      for (int i = 0; i < this.m_stockVOs.length - 1; i++) {
        if (nCost == 0) {
          double dd = this.m_totalVOs[i].getNfactor1().doubleValue();
          dd += total / nNum * this.m_totalVOs[i].getNstocknum().doubleValue() * unitWeight[i].doubleValue();
          this.m_totalVOs[i].setNfactor1(new UFDouble(PuTool.getRoundDouble(this.m_moneyDecimal, dd)));
          sum += PuTool.getRoundDouble(this.m_moneyDecimal, total / nNum * this.m_totalVOs[i].getNstocknum().doubleValue() * unitWeight[i].doubleValue());
        }
        if (nCost == 1) {
          double dd = this.m_totalVOs[i].getNfactor2().doubleValue();
          dd += total / nNum * this.m_totalVOs[i].getNstocknum().doubleValue() * unitWeight[i].doubleValue();
          this.m_totalVOs[i].setNfactor2(new UFDouble(PuTool.getRoundDouble(this.m_moneyDecimal, dd)));
          sum += PuTool.getRoundDouble(this.m_moneyDecimal, total / nNum * this.m_totalVOs[i].getNstocknum().doubleValue() * unitWeight[i].doubleValue());
        }
        if (nCost == 2) {
          double dd = this.m_totalVOs[i].getNfactor3().doubleValue();
          dd += total / nNum * this.m_totalVOs[i].getNstocknum().doubleValue() * unitWeight[i].doubleValue();
          this.m_totalVOs[i].setNfactor3(new UFDouble(PuTool.getRoundDouble(this.m_moneyDecimal, dd)));
          sum += PuTool.getRoundDouble(this.m_moneyDecimal, total / nNum * this.m_totalVOs[i].getNstocknum().doubleValue() * unitWeight[i].doubleValue());
        }
        if (nCost == 3) {
          double dd = this.m_totalVOs[i].getNfactor4().doubleValue();
          dd += total / nNum * this.m_totalVOs[i].getNstocknum().doubleValue() * unitWeight[i].doubleValue();
          this.m_totalVOs[i].setNfactor4(new UFDouble(PuTool.getRoundDouble(this.m_moneyDecimal, dd)));
          sum += PuTool.getRoundDouble(this.m_moneyDecimal, total / nNum * this.m_totalVOs[i].getNstocknum().doubleValue() * unitWeight[i].doubleValue());
        }
        if (nCost == 4) {
          double dd = this.m_totalVOs[i].getNfactor5().doubleValue();
          dd += total / nNum * this.m_totalVOs[i].getNstocknum().doubleValue() * unitWeight[i].doubleValue();
          this.m_totalVOs[i].setNfactor5(new UFDouble(PuTool.getRoundDouble(this.m_moneyDecimal, dd)));
          sum += PuTool.getRoundDouble(this.m_moneyDecimal, total / nNum * this.m_totalVOs[i].getNstocknum().doubleValue() * unitWeight[i].doubleValue());
        }
      }

    }
    else
    {
      double nNum = 0.0D;
      for (int i = 0; i < this.m_stockVOs.length; i++) nNum += this.m_totalVOs[i].getNstocknum().doubleValue() * unitVolume[i].doubleValue();
      if (nNum == 0.0D) return;
      for (int i = 0; i < this.m_stockVOs.length - 1; i++) {
        if (nCost == 0) {
          double dd = this.m_totalVOs[i].getNfactor1().doubleValue();
          dd += total / nNum * this.m_totalVOs[i].getNstocknum().doubleValue() * unitVolume[i].doubleValue();
          this.m_totalVOs[i].setNfactor1(new UFDouble(PuTool.getRoundDouble(this.m_moneyDecimal, dd)));
          sum += PuTool.getRoundDouble(this.m_moneyDecimal, total / nNum * this.m_totalVOs[i].getNstocknum().doubleValue() * unitVolume[i].doubleValue());
        }
        if (nCost == 1) {
          double dd = this.m_totalVOs[i].getNfactor2().doubleValue();
          dd += total / nNum * this.m_totalVOs[i].getNstocknum().doubleValue() * unitVolume[i].doubleValue();
          this.m_totalVOs[i].setNfactor2(new UFDouble(PuTool.getRoundDouble(this.m_moneyDecimal, dd)));
          sum += PuTool.getRoundDouble(this.m_moneyDecimal, total / nNum * this.m_totalVOs[i].getNstocknum().doubleValue() * unitVolume[i].doubleValue());
        }
        if (nCost == 2) {
          double dd = this.m_totalVOs[i].getNfactor3().doubleValue();
          dd += total / nNum * this.m_totalVOs[i].getNstocknum().doubleValue() * unitVolume[i].doubleValue();
          this.m_totalVOs[i].setNfactor3(new UFDouble(PuTool.getRoundDouble(this.m_moneyDecimal, dd)));
          sum += PuTool.getRoundDouble(this.m_moneyDecimal, total / nNum * this.m_totalVOs[i].getNstocknum().doubleValue() * unitVolume[i].doubleValue());
        }
        if (nCost == 3) {
          double dd = this.m_totalVOs[i].getNfactor4().doubleValue();
          dd += total / nNum * this.m_totalVOs[i].getNstocknum().doubleValue() * unitVolume[i].doubleValue();
          this.m_totalVOs[i].setNfactor4(new UFDouble(PuTool.getRoundDouble(this.m_moneyDecimal, dd)));
          sum += PuTool.getRoundDouble(this.m_moneyDecimal, total / nNum * this.m_totalVOs[i].getNstocknum().doubleValue() * unitVolume[i].doubleValue());
        }
        if (nCost == 4) {
          double dd = this.m_totalVOs[i].getNfactor5().doubleValue();
          dd += total / nNum * this.m_totalVOs[i].getNstocknum().doubleValue() * unitVolume[i].doubleValue();
          this.m_totalVOs[i].setNfactor5(new UFDouble(PuTool.getRoundDouble(this.m_moneyDecimal, dd)));
          sum += PuTool.getRoundDouble(this.m_moneyDecimal, total / nNum * this.m_totalVOs[i].getNstocknum().doubleValue() * unitVolume[i].doubleValue());
        }
      }

    }

    if (nCost == 0) {
      double dd = this.m_totalVOs[(this.m_stockVOs.length - 1)].getNfactor1().doubleValue();
      dd += total - sum;
      this.m_totalVOs[(this.m_stockVOs.length - 1)].setNfactor1(new UFDouble(PuTool.getRoundDouble(this.m_moneyDecimal, dd)));
    }
    if (nCost == 1) {
      double dd = this.m_totalVOs[(this.m_stockVOs.length - 1)].getNfactor2().doubleValue();
      dd += total - sum;
      this.m_totalVOs[(this.m_stockVOs.length - 1)].setNfactor2(new UFDouble(PuTool.getRoundDouble(this.m_moneyDecimal, dd)));
    }
    if (nCost == 2) {
      double dd = this.m_totalVOs[(this.m_stockVOs.length - 1)].getNfactor3().doubleValue();
      dd += total - sum;
      this.m_totalVOs[(this.m_stockVOs.length - 1)].setNfactor3(new UFDouble(PuTool.getRoundDouble(this.m_moneyDecimal, dd)));
    }
    if (nCost == 3) {
      double dd = this.m_totalVOs[(this.m_stockVOs.length - 1)].getNfactor4().doubleValue();
      dd += total - sum;
      this.m_totalVOs[(this.m_stockVOs.length - 1)].setNfactor4(new UFDouble(PuTool.getRoundDouble(this.m_moneyDecimal, dd)));
    }
    if (nCost == 4) {
      double dd = this.m_totalVOs[(this.m_stockVOs.length - 1)].getNfactor5().doubleValue();
      dd += total - sum;
      this.m_totalVOs[(this.m_stockVOs.length - 1)].setNfactor5(new UFDouble(PuTool.getRoundDouble(this.m_moneyDecimal, dd)));
    }
  }

  private boolean doClassification()
  {
    Integer[] nSelected = (Integer[])null;
    Vector v = new Vector();
    int nRow = getBillListPanel().getHeadBillModel().getRowCount();
    for (int i = 0; i < nRow; i++) {
      int nStatus = getBillListPanel().getHeadBillModel().getRowState(i);
      if (nStatus != 4) continue; v.addElement(new Integer(i));
    }
    nSelected = new Integer[v.size()];
    v.copyInto(nSelected);

    if ((nSelected == null) || (nSelected.length == 0)) {
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000078"), NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000079"));
      return false;
    }

    Integer[] nSelected1 = (Integer[])null;

    Integer[] nSelected2 = (Integer[])null;

    Integer[] nSelected3 = (Integer[])null;
    Vector v1 = new Vector();
    Vector v2 = new Vector();
    Vector v3 = new Vector();
    nRow = getBillListPanel().getBodyBillModel().getRowCount();
    int iLen = this.m_invoiceVOs == null ? 0 : this.m_invoiceVOs.length;
    int jLen = this.m_feeVOs == null ? 0 : this.m_feeVOs.length;
    int kLen = this.m_discountVOs == null ? 0 : this.m_discountVOs.length;
    for (int i = 0; i < nRow; i++) {
      int nStatus = getBillListPanel().getBodyBillModel().getRowState(i);
      if (nStatus == 4)
        if (i < iLen) { v1.addElement(new Integer(i));
        } else if (i < iLen + jLen) { v2.addElement(new Integer(i)); } else {
          if (i >= iLen + jLen + kLen) continue; v3.addElement(new Integer(i));
        }
    }
    nSelected1 = new Integer[v1.size()];
    v1.copyInto(nSelected1);
    nSelected2 = new Integer[v2.size()];
    v2.copyInto(nSelected2);
    nSelected3 = new Integer[v3.size()];
    v3.copyInto(nSelected3);

    if ((nSelected1 == null) || (nSelected1.length == 0)) {
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000078"), NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000080"));
      return false;
    }

    Vector v0 = new Vector();
    for (int i = 0; i < this.m_stockVOs.length; i++) v0.addElement(this.m_stockVOs[i]);
    this.m_mStockVOs = new StockVO[v0.size()];
    v0.copyInto(this.m_mStockVOs);

    Vector v00 = new Vector();
    for (int i = 0; i < iLen; i++) v00.addElement(this.m_invoiceVOs[i]);
    this.m_mInvoiceVOs = new IinvoiceVO[v00.size()];
    v00.copyInto(this.m_mInvoiceVOs);

    Vector v000 = new Vector();
    for (int i = 0; i < jLen; i++) v000.addElement(this.m_feeVOs[i]);
    this.m_mFeeVOs = new FeeinvoiceVO[v000.size()];
    v000.copyInto(this.m_mFeeVOs);

    Vector v0000 = new Vector();
    for (int i = 0; i < kLen; i++) v0000.addElement(this.m_discountVOs[i]);
    this.m_mDiscountVOs = new FeeinvoiceVO[v0000.size()];
    v0000.copyInto(this.m_mDiscountVOs);

    Vector vv = new Vector();
    for (int i = 0; i < nSelected.length; i++) {
      int n = nSelected[i].intValue();
      vv.addElement(this.m_stockVOs[n]);
    }

    this.m_stockVOs = new StockVO[vv.size()];
    vv.copyInto(this.m_stockVOs);

    Vector vv1 = new Vector();
    for (int i = 0; i < nSelected1.length; i++) {
      int n = nSelected1[i].intValue();
      vv1.addElement(this.m_invoiceVOs[n]);
    }

    this.m_invoiceVOs = new IinvoiceVO[vv1.size()];
    vv1.copyInto(this.m_invoiceVOs);

    Vector vv2 = new Vector();
    for (int i = 0; i < nSelected2.length; i++) {
      int n = nSelected2[i].intValue();
      vv2.addElement(this.m_feeVOs[(n - this.m_mInvoiceVOs.length)]);
    }

    this.m_feeVOs = new FeeinvoiceVO[vv2.size()];
    vv2.copyInto(this.m_feeVOs);

    Vector vv3 = new Vector();
    for (int i = 0; i < nSelected3.length; i++) {
      int n = nSelected3[i].intValue();
      vv3.addElement(this.m_discountVOs[(n - this.m_mInvoiceVOs.length - this.m_mFeeVOs.length)]);
    }

    this.m_discountVOs = new FeeinvoiceVO[vv3.size()];
    vv3.copyInto(this.m_discountVOs);

    return true;
  }

  private void doModification()
  {
    Vector vStockKey = new Vector();
    for (int i = 0; i < this.m_settleVOs.length - 1; i++) {
      String s = this.m_settleVOs[i].getCstockrow();
      if ((s != null) && (s.length() != 0)) {
        s = s.trim();
        boolean b = false;
        for (int j = i + 1; j < this.m_settleVOs.length; j++) {
          String ss = this.m_settleVOs[j].getCstockrow();
          if ((ss != null) && (ss.length() != 0)) {
            ss = ss.trim();
            if (s.equals(ss)) {
              b = true;
              break;
            }
          }
        }
        if (b) continue; vStockKey.addElement(s);
      }
    }
    String sss = this.m_settleVOs[(this.m_settleVOs.length - 1)].getCstockrow();
    UFDouble ddd = this.m_settleVOs[(this.m_settleVOs.length - 1)].getNsettlenum();
    if ((sss != null) && (sss.length() > 0) && (ddd != null)) vStockKey.addElement(sss);

    Vector vInvoiceKey = new Vector();
    for (int i = 0; i < this.m_settleVOs.length - 1; i++) {
      String s = this.m_settleVOs[i].getCinvoice_bid();
      UFDouble d = this.m_settleVOs[i].getNsettlenum();
      if ((d == null) || 
        (s == null) || (s.length() == 0)) continue;
      s = s.trim();
      boolean b = false;
      for (int j = i + 1; j < this.m_settleVOs.length; j++) {
        String ss = this.m_settleVOs[j].getCinvoice_bid();
        if ((ss != null) && (ss.length() != 0)) {
          ss = ss.trim();
          if (s.equals(ss)) {
            b = true;
            break;
          }
        }
      }
      if (b) continue; vInvoiceKey.addElement(s);
    }
    sss = this.m_settleVOs[(this.m_settleVOs.length - 1)].getCinvoice_bid();
    ddd = this.m_settleVOs[(this.m_settleVOs.length - 1)].getNsettlenum();
    if ((sss != null) && (sss.length() > 0) && (ddd != null)) vInvoiceKey.addElement(sss);

    Vector vFeeKey = new Vector();
    for (int i = 0; i < this.m_settleVOs.length - 1; i++) {
      String s = this.m_settleVOs[i].getCinvoice_bid();
      UFDouble d = this.m_settleVOs[i].getNsettlenum();
      if ((d != null) || 
        (s == null) || (s.length() == 0)) continue;
      s = s.trim();
      boolean b = false;
      for (int j = i + 1; j < this.m_settleVOs.length; j++) {
        String ss = this.m_settleVOs[j].getCinvoice_bid();
        if ((ss != null) && (ss.length() != 0)) {
          ss = ss.trim();
          if (s.equals(ss)) {
            b = true;
            break;
          }
        }
      }
      if (b) continue; vFeeKey.addElement(s);
    }
    sss = this.m_settleVOs[(this.m_settleVOs.length - 1)].getCinvoice_bid();
    ddd = this.m_settleVOs[(this.m_settleVOs.length - 1)].getNsettlenum();
    if ((sss != null) && (sss.length() > 0) && (ddd == null)) vFeeKey.addElement(sss);

    StockVO[] stockVOs = modifyStock(vStockKey);

    IinvoiceVO[] iinvoiceVOs = modifyInvoice(vInvoiceKey);

    FeeinvoiceVO[] feeVOs = (FeeinvoiceVO[])null;
    FeeinvoiceVO[] discountVOs = (FeeinvoiceVO[])null;
    if (this.m_bDistribute)
    {
      Vector v = modifyFee(vFeeKey);
      Vector vTemp1 = (Vector)v.elementAt(0);
      Vector vTemp2 = (Vector)v.elementAt(1);
      feeVOs = new FeeinvoiceVO[vTemp1.size()];
      vTemp1.copyInto(feeVOs);
      discountVOs = new FeeinvoiceVO[vTemp2.size()];
      vTemp2.copyInto(discountVOs);
    }

    SettlebillHeaderVO head = new SettlebillHeaderVO();
    head.setPk_corp(this.m_sUnitCode);
    head.setCaccountyear(getClientEnvironment().getAccountYear());
    head.setDsettledate(getClientEnvironment().getDate());
    head.setIbillstatus(new Integer(0));
    head.setCbilltype("27");
    head.setCoperator(getClientEnvironment().getUser().getPrimaryKey());

    SettlebillVO settlebillVO = new SettlebillVO(this.m_settleVOs.length);
    settlebillVO.setParentVO(head);
    settlebillVO.setChildrenVO(this.m_settleVOs);

    long tTime = System.currentTimeMillis();
    try {
      ArrayList listPara = new ArrayList();
      listPara.add(settlebillVO);
      listPara.add(stockVOs);
      listPara.add(iinvoiceVOs);
      listPara.add(feeVOs);
      listPara.add(discountVOs);
      listPara.add(this.m_sEstimateMode);
      listPara.add(this.m_sDifferenceMode);
      listPara.add(getClientEnvironment().getUser().getPrimaryKey());

      listPara.add(this.m_bIc2PiSettle);
      listPara.add(this.m_bZGYF);
      listPara.add(new Integer(this.m_moneyDecimal));
      listPara.add(new UFBoolean(true));

      listPara.add(getClientEnvironment().getDate());

      SettleHelper.doManualBalance_yf(listPara);

      tTime = System.currentTimeMillis() - tTime;
      SCMEnv.out("异存货结算时间：" + tTime + " ms!");
    }
    catch (SQLException e) {
      this.m_bSettling = false;

      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000078"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000412"));
      SCMEnv.out(e);
      return;
    }
    catch (ArrayIndexOutOfBoundsException e) {
      this.m_bSettling = false;

      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000078"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000426"));
      SCMEnv.out(e);
      return;
    }
    catch (NullPointerException e) {
      this.m_bSettling = false;

      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000078"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000427"));
      SCMEnv.out(e);
      return;
    }
    catch (BusinessException e) {
      this.m_bSettling = false;

      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000078"), e.getMessage());
      SCMEnv.out(e);
      return;
    }
    catch (Exception e) {
      this.m_bSettling = false;

      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000078"), e.getMessage());
      SCMEnv.out(e);
      return;
    }

    showHintMessage(NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000180"));

    this.m_bSettling = false;
  }

  private void generateSettlebill()
  {
    int nStock = this.m_stockVOs.length;

    SettlebillItemVO[] body = new SettlebillItemVO[this.m_totalVOs.length];
    Vector v = new Vector();
    for (int i = 0; i < nStock; i++) {
      body[i] = new SettlebillItemVO();
      body[i].setPk_corp(this.m_sUnitCode);
      body[i].setPk_stockcorp(this.m_stockVOs[i].getPk_stockcorp());
      body[i].setCstockrow(this.m_stockVOs[i].getCgeneralbid());
      body[i].setCstockid(this.m_stockVOs[i].getCgeneralhid());
      body[i].setCmangid(this.m_stockVOs[i].getCmangid());
      body[i].setCbaseid(this.m_stockVOs[i].getCbaseid());
      body[i].setNsettledisctmny(this.m_totalVOs[i].getNdiscountmny());

      body[i].setNfactor1(this.m_totalVOs[i].getNfactor1());
      body[i].setNfactor2(this.m_totalVOs[i].getNfactor2());
      body[i].setNfactor3(this.m_totalVOs[i].getNfactor3());
      body[i].setNfactor4(this.m_totalVOs[i].getNfactor4());
      body[i].setNfactor5(this.m_totalVOs[i].getNfactor5());

      body[i].setNmoney(this.m_totalVOs[i].getNsettlemny());
      body[i].setNsettlenum(this.m_totalVOs[i].getNstocknum());
      body[i].setNgaugemny(this.m_totalVOs[i].getNgaugemny());

      UFDouble d1 = body[i].getNmoney();
      UFDouble d2 = body[i].getNsettlenum();
      if ((d1 != null) && (d2 != null) && 
        (d2.doubleValue() != 0.0D)) {
        double d = d1.doubleValue() / d2.doubleValue();
        body[i].setNprice(new UFDouble(d));
      }

      body[i].setVbillcode(this.m_stockVOs[i].getVbillcode());
      v.addElement(body[i]);
    }
    for (int i = nStock; i < this.m_totalVOs.length; i++) {
      body[i] = new SettlebillItemVO();
      body[i].setPk_corp(this.m_sUnitCode);
      body[i].setCinvoice_bid(this.m_invoiceVOs[(i - nStock)].getCinvoice_bid());
      body[i].setCinvoiceid(this.m_invoiceVOs[(i - nStock)].getCinvoiceid());
      body[i].setCmangid(this.m_invoiceVOs[(i - nStock)].getCmangid());
      body[i].setCbaseid(this.m_invoiceVOs[(i - nStock)].getCbaseid());
      body[i].setNmoney(this.m_totalVOs[i].getNinvoicemny());
      body[i].setNsettlenum(this.m_totalVOs[i].getNinvoicenum());

      UFDouble d1 = body[i].getNmoney();
      UFDouble d2 = body[i].getNsettlenum();
      if ((d1 != null) && (d2 != null) && 
        (d2.doubleValue() != 0.0D)) {
        double d = d1.doubleValue() / d2.doubleValue();
        body[i].setNprice(new UFDouble(d));
      }

      body[i].setVinvoicecode(this.m_invoiceVOs[(i - nStock)].getVinvoicecode());
      v.addElement(body[i]);
    }

    this.m_settleVOs = new SettlebillItemVO[v.size()];
    v.copyInto(this.m_settleVOs);
  }

  private BillListPanel getBillListPanel()
  {
    if (this.m_listPanel == null) {
      try {
        this.m_listPanel = new BillListPanel();
        this.m_listPanel.setName("ListPanel");
        this.m_listPanel.loadTemplet("40040502020100000000");

        BillListData bd = this.m_listPanel.getBillListData();
        bd = initListDecimal(bd);
        this.m_listPanel.setListData(bd);

        this.m_listPanel.getParentListPanel().setShowThMark(true);
        this.m_listPanel.getChildListPanel().setShowThMark(true);
        this.m_listPanel.getChildListPanel().setTotalRowShow(true);
        this.m_listPanel.getParentListPanel().setTotalRowShow(true);

        this.m_listPanel.addEditListener(this);
        this.m_listPanel.addBodyEditListener(this);

        this.m_listPanel.getHeadTable().setCellSelectionEnabled(false);
        this.m_listPanel.getHeadTable().setSelectionMode(2);
        this.m_listPanel.getHeadTable().getSelectionModel().addListSelectionListener(this);

        this.m_listPanel.getBodyTable().setCellSelectionEnabled(false);
        this.m_listPanel.getBodyTable().setSelectionMode(2);
        this.m_listPanel.getBodyTable().getSelectionModel().addListSelectionListener(this);

        this.m_listPanel.getHeadTable().setSortEnabled(false);
        this.m_listPanel.getBodyTable().setSortEnabled(false);

        this.m_listPanel.updateUI();
      } catch (Throwable ivjExc) {
        SCMEnv.out(ivjExc);
        MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000050"), NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000081"));
        return null;
      }
    }
    return this.m_listPanel;
  }

  public String getTitle()
  {
    return NCLangRes.getInstance().getStrByID("4004050204", "UPP4004050204-000001");
  }

  private BillListPanel getTotalBillListPanel()
  {
    if (this.m_totalListPanel == null) {
      try {
        this.m_totalListPanel = new BillListPanel();
        this.m_totalListPanel.setName("TotalListPanel");

        this.m_totalListPanel.loadTemplet("40040502020200000000");
        BillListData bd = this.m_totalListPanel.getBillListData();

        bd = initTotalDecimal(bd);

        this.m_totalListPanel.setListData(bd);

        this.m_totalListPanel.getParentListPanel().setShowThMark(true);
        this.m_totalListPanel.getChildListPanel().setShowThMark(true);
        this.m_totalListPanel.getChildListPanel().setTotalRowShow(true);
        this.m_totalListPanel.getParentListPanel().setTotalRowShow(true);

        this.m_totalListPanel.addEditListener(this);
        this.m_totalListPanel.addBodyEditListener(this);

        this.m_totalListPanel.getHeadTable().setSortEnabled(false);
        this.m_totalListPanel.getBodyTable().setSortEnabled(false);

        this.m_totalListPanel.updateUI();
      } catch (Throwable ivjExc) {
        SCMEnv.out(ivjExc);
        MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000050"), NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000081"));
        return null;
      }
    }
    return this.m_totalListPanel;
  }

  public void init()
  {
    initpara();

    this.m_buttons1 = new ButtonObject[5];
    this.m_buttons1[0] = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000041"), NCLangRes.getInstance().getStrByID("common", "UC001-0000041"), "全选");
    this.m_buttons1[1] = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000042"), NCLangRes.getInstance().getStrByID("common", "UC001-0000042"), "全消");
    this.m_buttons1[2] = new ButtonObject(NCLangRes.getInstance().getStrByID("4004050204", "UPT4004050204-000001"), NCLangRes.getInstance().getStrByID("4004050204", "UPT4004050204-000001"), "发票");
    this.m_buttons1[3] = new ButtonObject(NCLangRes.getInstance().getStrByID("4004050204", "UPT4004050204-000002"), NCLangRes.getInstance().getStrByID("4004050204", "UPT4004050204-000002"), "入库单");
    this.m_buttons1[4] = new ButtonObject(NCLangRes.getInstance().getStrByID("4004050204", "UPT4004050204-000005"), NCLangRes.getInstance().getStrByID("4004050204", "UPT4004050204-000005"), "确认");

    this.m_buttons2 = new ButtonObject[4];
    this.m_buttons2[0] = new ButtonObject(NCLangRes.getInstance().getStrByID("4004050204", "UPT4004050204-000006"), NCLangRes.getInstance().getStrByID("4004050204", "UPT4004050204-000006"), "费用分摊");
    this.m_buttons2[1] = new ButtonObject(NCLangRes.getInstance().getStrByID("4004050204", "UPP4004050204-000002"), NCLangRes.getInstance().getStrByID("4004050204", "UPP4004050204-000002"), "发票分摊 ");
    this.m_buttons2[1].addChildButton(new ButtonObject(NCLangRes.getInstance().getStrByID("4004050204", "UPT4004050204-000007"), NCLangRes.getInstance().getStrByID("4004050204", "UPT4004050204-000007"), "按数量分摊"));
    this.m_buttons2[1].addChildButton(new ButtonObject(NCLangRes.getInstance().getStrByID("4004050204", "UPT4004050204-000008"), NCLangRes.getInstance().getStrByID("4004050204", "UPT4004050204-000008"), "按金额分摊"));
    this.m_buttons2[2] = new ButtonObject(NCLangRes.getInstance().getStrByID("4004050204", "UPT4004050204-000004"), NCLangRes.getInstance().getStrByID("4004050204", "UPT4004050204-000004"), "结算");
    this.m_buttons2[3] = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000038"), NCLangRes.getInstance().getStrByID("common", "UC001-0000038"), "返回");
    setButtons(this.m_buttons1);

    setLayout(new BorderLayout());
    add(getBillListPanel(), "Center");
    getBillListPanel().setEnabled(false);

    initConfigure();

    this.m_nButtonState = new int[this.m_buttons1.length];
    for (int i = 0; i < 5; i++) {
      this.m_nButtonState[i] = 1;
    }
    this.m_nButtonState[2] = 0;
    this.m_nButtonState[3] = 0;
    changeButtonState();
  }

  private void initConfigure()
  {
    try
    {
      if ((this.CostfactorVOtempVO != null) && (this.CostfactorVOtempVO.length > 0))
      {
        Vector vBCost = new Vector();
        Vector vTemp0 = new Vector();
        vBCost.addElement(this.CostfactorVOtempVO[0].getHeadVO().getBisentercost());
        vTemp0.addElement(this.CostfactorVOtempVO[0].getHeadVO().getCcostfactorid().trim());
        for (int i = 1; i < this.CostfactorVOtempVO.length; i++) {
          String s1 = this.CostfactorVOtempVO[i].getHeadVO().getCcostfactorid().trim();
          if (!vTemp0.contains(s1)) {
            vTemp0.addElement(s1);
            vBCost.addElement(this.CostfactorVOtempVO[i].getHeadVO().getBisentercost());
          }

        }

        this.m_bIsentercost = new UFBoolean[vBCost.size()];
        vBCost.copyInto(this.m_bIsentercost);
      }
    } catch (Exception e) {
      SCMEnv.out(e);
    }
  }

  private BillListData initListDecimal(BillListData bd)
  {
    if ((this.measure == null) || (this.measure.length == 0)) {
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000048"), NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000049"));
      return null;
    }

    this.m_unitDecimal = this.measure[0];

    this.m_priceDecimal = this.measure[1];

    this.m_moneyDecimal = this.measure[2];

    int nMeasDecimal = this.m_unitDecimal;
    int nPriceDecimal = this.m_priceDecimal;

    BillItem item1 = bd.getHeadItem("ninnum");
    item1.setDecimalDigits(nMeasDecimal);

    BillItem item2 = bd.getHeadItem("nnosettlenum");
    item2.setDecimalDigits(nMeasDecimal);

    BillItem item3 = bd.getHeadItem("nprice");
    item3.setDecimalDigits(nPriceDecimal);

    BillItem item4 = bd.getHeadItem("nnosettlemny");
    item4.setDecimalDigits(this.m_moneyDecimal);

    BillItem item20 = bd.getBodyItem("ninvoicenum");
    item20.setDecimalDigits(nMeasDecimal);

    BillItem item21 = bd.getBodyItem("nnosettlenum");
    item21.setDecimalDigits(nMeasDecimal);

    BillItem item22 = bd.getBodyItem("nprice");
    item22.setDecimalDigits(nPriceDecimal);

    BillItem item23 = bd.getBodyItem("nnosettlemny");
    item23.setDecimalDigits(this.m_moneyDecimal);

    return bd;
  }

  public void initpara()
  {try
    {
      Object[] objs = (Object[])null;
      ServcallVO[] scds = new ServcallVO[4];

      scds[0] = new ServcallVO();
      scds[0].setBeanName("nc.itf.pu.pub.IPub");
      scds[0].setMethodName("getDigitBatch");
      scds[0].setParameter(new Object[] {
              m_sUnitCode, new String[] {
                      "BD501", "BD505", "BD301"
                  }
              });
      scds[0].setParameterTypes(new Class[] { String.class, java.lang.String.class });

      scds[1] = new ServcallVO();
      scds[1].setBeanName("nc.itf.pu.pub.IPub");
      scds[1].setMethodName("isEnabled");
      scds[1].setParameter(new Object[] { this.m_sUnitCode, "IC" });
      scds[1].setParameterTypes(new Class[] { String.class, String.class });

      scds[2] = new ServcallVO();
      scds[2].setBeanName("nc.itf.pu.pub.IPub");
      scds[2].setMethodName("isEnabled");
      scds[2].setParameter(new Object[] { this.m_sUnitCode, "CT" });
      scds[2].setParameterTypes(new Class[] { String.class, String.class });

      scds[3] = new ServcallVO();
      scds[3].setBeanName("nc.itf.ps.factor.ICostfactor");
      scds[3].setMethodName("queryAllFactors");
      scds[3].setParameter(new Object[] { this.m_sUnitCode });
      scds[3].setParameterTypes(new Class[] { String.class });

      Hashtable t = SysInitBO_Client.queryBatchParaValues(this.m_sUnitCode, new String[] { "PO51", "PO52", "PO12", "PO13" });
      if ((t != null) && (t.size() > 0)) {
        Object temp = null;
        if (t.get("PO51") != null) {
          temp = t.get("PO51");
          this.m_bIc2PiSettle = new UFBoolean(temp.toString());
        }
        if (t.get("PO52") != null) {
          temp = t.get("PO52");
          this.m_bZGYF = new UFBoolean(temp.toString());
        }

        if (t.get("PO12") != null) {
          temp = t.get("PO12");
          this.m_sEstimateMode = temp.toString();
        }
        if (t.get("PO13") != null) {
          temp = t.get("PO13");
          this.m_sDifferenceMode = temp.toString();
        }
      }

      objs = LocalCallService.callService(scds);

      this.measure = ((int[])objs[0]);
      this.m_bICStartUp = ((UFBoolean)objs[1]).booleanValue();
      this.m_bCTStartUp = ((UFBoolean)objs[2]).booleanValue();
      this.CostfactorVOtempVO = ((CostfactorVO[])objs[3]);
    }
    catch (Exception e) {
      SCMEnv.out(e);
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000052"), e.getMessage());
      return;
    }
    }

  public void initQueryModelForInvoice()
  {
    if (this.m_condClient2 == null) {
      this.m_condClient2 = new PoQueryCondition(this);
      this.m_condClient2.setTitle(NCLangRes.getInstance().getStrByID("4004050204", "UPP4004050204-000003"));
      this.m_condClient2.setTempletID(this.m_sUnitCode, "400405020201", getClientEnvironment().getUser().getPrimaryKey(), null, "40040502002");

      this.m_condClient2.setValueRef("dinvoicedate", "日历");
      this.m_condClient2.setValueRef("cinvclassid", "存货分类");
      this.m_condClient2.setValueRef("cwarehouseid", "仓库档案");

      UIRefPane vendorRef = new UIRefPane();

      vendorRef.setRefNodeName("供应商档案");

      UIRefPane inventoryRef = new UIRefPane();

      inventoryRef.setRefNodeName("存货档案");

      this.m_condClient2.setValueRef("cvendorbaseid", vendorRef);

      this.m_condClient2.setValueRef("cbaseid", inventoryRef);
      this.m_condClient2.setValueRef("coperator", "操作员");

      this.m_condClient2.setDefaultValue("dinvoicedate", "dinvoicedate", getClientEnvironment().getDate().toString());

      DefSetTool.updateQueryConditionClientUserDef(this.m_condClient2, this.m_sUnitCode, "25", "po_invoice.vdef", null, 0, "po_invoice_b.vdef", null, 0);
      this.m_condClient2.setIsWarningWithNoInput(true);

      this.m_condClient2.setSealedDataShow(true);

      this.m_condClient2.setRefsDataPowerConVOs(ClientEnvironment.getInstance().getUser().getPrimaryKey(), 
        new String[] { ClientEnvironment.getInstance().getCorporation().getPrimaryKey() }, 
        new String[] { "供应商档案", "仓库档案", "存货档案", "存货分类" }, 
        new String[] { "cvendorbaseid", "cwarehouseid", "cbaseid", "cinvclassid" }, 
        new int[4]);
    }
  }

  public void initQueryModelForStock()
  {
    if (this.m_condClient1 == null)
    {
      this.m_condClient1 = new PoQueryCondition(this);
      this.m_condClient1.setTitle(NCLangRes.getInstance().getStrByID("4004050204", "UPP4004050204-000003"));
      this.m_condClient1.setTempletID(this.m_sUnitCode, "400405020201", getClientEnvironment().getUser().getPrimaryKey(), null, "40040502001");

      this.m_corpPane = new UIRefPane();
      this.m_corpPane.setRefNodeName("公司目录");
      this.m_corpPane.addValueChangedListener(this);
      this.m_condClient1.setValueRef("pk_stockcorp", this.m_corpPane);

      this.m_warePane = new UIRefPane();
      this.m_warePane.setRefNodeName("仓库档案");
      this.m_condClient1.setValueRef("cwarehouseid", this.m_warePane);

      this.m_condClient1.setValueRef("dbilldate", "日历");
      this.m_condClient1.setValueRef("cinvclassid", "存货分类");

      UIRefPane vendorRef = new UIRefPane();

      vendorRef.setRefNodeName("供应商档案");
      this.m_condClient1.setValueRef("cvendorbaseid", vendorRef);

      UIRefPane inventoryRef = new UIRefPane();

      inventoryRef.setRefNodeName("存货档案");
      this.m_condClient1.setValueRef("cbaseid", inventoryRef);
      this.m_condClient1.setValueRef("coperatorid", "操作员");

      this.m_condClient1.setDefaultValue("dbilldate", "dbilldate", getClientEnvironment().getDate().toString());

      DefSetTool.updateQueryConditionClientUserDef(this.m_condClient1, this.m_sUnitCode, "icbill", "vuserdef", null, 0, "ic_general_b.vuserdef", null, 0);

      this.m_condClient1.setIsWarningWithNoInput(true);

      this.m_condClient1.setSealedDataShow(true);

      this.m_condClient1.setRefsDataPowerConVOs(ClientEnvironment.getInstance().getUser().getPrimaryKey(), 
        new String[] { ClientEnvironment.getInstance().getCorporation().getPrimaryKey() }, 
        new String[] { "供应商档案", "仓库档案", "存货档案", "存货分类" }, 
        new String[] { "cvendorbaseid", "cwarehouseid", "cbaseid", "cinvclassid" }, 
        new int[] { 0, 2 });
    }
  }

  private BillListData initTotalDecimal(BillListData bd)
  {
    int nMeasDecimal = this.m_unitDecimal;
    int nPriceDecimal = this.m_priceDecimal;

    BillItem item1 = bd.getHeadItem("nstocknum");
    item1.setDecimalDigits(nMeasDecimal);

    BillItem item111 = bd.getHeadItem("nstockaccumsettlenum");
    item111.setDecimalDigits(nMeasDecimal);

    BillItem item2 = bd.getHeadItem("ninvoicenum");
    item2.setDecimalDigits(nMeasDecimal);

    BillItem item3 = bd.getHeadItem("nnosettlenum");
    item3.setDecimalDigits(nMeasDecimal);

    BillItem item4 = bd.getHeadItem("nreasonwastenum");
    item4.setDecimalDigits(nMeasDecimal);

    BillItem item5 = bd.getHeadItem("nprice");
    item5.setDecimalDigits(nPriceDecimal);

    BillItem item6 = bd.getHeadItem("nnosettlemny");
    item6.setDecimalDigits(this.m_moneyDecimal);

    BillItem item7 = bd.getHeadItem("ninvoicemny");
    item7.setDecimalDigits(this.m_moneyDecimal);

    BillItem item8 = bd.getHeadItem("ndiscountmny");
    item8.setDecimalDigits(this.m_moneyDecimal);

    BillItem item9 = bd.getHeadItem("nfactor1");
    item9.setDecimalDigits(this.m_moneyDecimal);
    BillItem item10 = bd.getHeadItem("nfactor2");
    item10.setDecimalDigits(this.m_moneyDecimal);
    BillItem item11 = bd.getHeadItem("nfactor3");
    item11.setDecimalDigits(this.m_moneyDecimal);
    BillItem item12 = bd.getHeadItem("nfactor4");
    item12.setDecimalDigits(this.m_moneyDecimal);
    BillItem item13 = bd.getHeadItem("nfactor5");
    item13.setDecimalDigits(this.m_moneyDecimal);

    BillItem item14 = bd.getHeadItem("nsettlemny");
    item14.setDecimalDigits(this.m_moneyDecimal);

    BillItem item15 = bd.getHeadItem("ngaugemny");
    item15.setDecimalDigits(this.m_moneyDecimal);

    BillItem item20 = bd.getBodyItem("nnum");
    item20.setDecimalDigits(nMeasDecimal);

    BillItem item21 = bd.getBodyItem("nmny");
    item21.setDecimalDigits(this.m_moneyDecimal);

    BillItem item22 = bd.getBodyItem("nnosettlemny");
    item22.setDecimalDigits(this.m_moneyDecimal);

    BillItem item23 = bd.getBodyItem("nsettlemny");
    item23.setDecimalDigits(this.m_moneyDecimal);

    return bd;
  }

  private boolean isFocusListBody(BillEditEvent event)
  {
    String sKey = event.getKey().trim();

    return sKey.equals("nsettlemny");
  }

  private boolean isFocusListHead(BillEditEvent event)
  {
    String sKey = event.getKey().trim();

    return (sKey.equals("nstocknum")) || (sKey.equals("ninvoicenum")) || (sKey.equals("ninvoicemny"));
  }

  private ArrayList dealCondVosForPower(ConditionVO[] voaCond)
  {
    ArrayList listUserInputVos = new ArrayList();
    ArrayList listPowerVos = new ArrayList();

    int iLen = voaCond.length;

    for (int i = 0; i < iLen; i++) {
      if ((voaCond[i].getOperaCode().trim().equalsIgnoreCase("IS")) && (voaCond[i].getValue().trim().equalsIgnoreCase("NULL"))) {
        listPowerVos.add(voaCond[i]);
        i++;
        listPowerVos.add(voaCond[i]);
      } else {
        listUserInputVos.add(voaCond[i]);
      }

    }

    ArrayList listRet = new ArrayList();

    ConditionVO[] voaCondUserInput = (ConditionVO[])null;
    if (listUserInputVos.size() > 0) {
      voaCondUserInput = new ConditionVO[listUserInputVos.size()];
      listUserInputVos.toArray(voaCondUserInput);
    }
    listRet.add(voaCondUserInput);

    ConditionVO[] voaCondPower = (ConditionVO[])null;
    if (listPowerVos.size() > 0) {
      voaCondPower = new ConditionVO[listPowerVos.size()];
      listPowerVos.toArray(voaCondPower);
      String strPowerWherePart = voaCondPower[0].getWhereSQL(voaCondPower);

      strPowerWherePart = StringUtil.replaceAllString(strPowerWherePart, "custcode", "b.pk_cubasdoc");
      strPowerWherePart = StringUtil.replaceAllString(strPowerWherePart, "invcode", "b.pk_invbasdoc");
      strPowerWherePart = StringUtil.replaceAllString(strPowerWherePart, "cbaseid", "B.cbaseid");

      strPowerWherePart = StringUtil.replaceAllString(strPowerWherePart, "cinvclassid", "B.cbaseid");
      strPowerWherePart = StringUtil.replaceAllString(strPowerWherePart, "invclasscode", "pk_invbasdoc");
      strPowerWherePart = StringUtil.replaceAllString(strPowerWherePart, "bd_invcl where 0=0  and pk_invcl", "bd_invcl, bd_invbasdoc where bd_invcl.pk_invcl = bd_invbasdoc.pk_invcl and bd_invcl.pk_invcl");

      strPowerWherePart = StringUtil.replaceAllString(strPowerWherePart, "storcode", "pk_stordoc");

      listRet.add(strPowerWherePart);
    } else {
      listRet.add(null);
    }

    return listRet;
  }

  private void loadInvoiceData()
  {
    ConditionVO[] conditionVO = this.m_condClient2.getConditionVO();

    if (!checkQueryCondition(conditionVO)) {
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000082"), NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000083"));
      return;
    }

    ArrayList listRet = dealCondVosForPower(conditionVO);
    conditionVO = (ConditionVO[])listRet.get(0);
    String strDataPowerSql = (String)listRet.get(1);
    try
    {
      long tTime = System.currentTimeMillis();

      ArrayList listPara = new ArrayList();
      listPara.add(this.m_sUnitCode);
      listPara.add(conditionVO);
      listPara.add(new UFBoolean(false));
      listPara.add(strDataPowerSql);
      ArrayList list = SettleHelper.queryAllInvoiceForManual_yf(listPara);
      if ((list == null) || (list.size() == 0)) {
        MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000084"), NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000085"));

        getBillListPanel().getBodyBillModel().clearBodyData();
        getBillListPanel().updateUI();

        for (int i = 0; i < 5; i++) {
          this.m_nButtonState[i] = 0;
        }
        this.m_bInvoice = false;
        if ((this.m_bStock) && (this.m_bInvoice))
          this.m_nButtonState[4] = 0;
        else
          this.m_nButtonState[4] = 1;
        changeButtonState();
        return;
      }
      this.m_invoiceVOs = ((IinvoiceVO[])list.get(0));
      this.m_feeVOs = ((FeeinvoiceVO[])list.get(1));
      this.m_discountVOs = ((FeeinvoiceVO[])list.get(2));
      if ((this.m_invoiceVOs == null) || (this.m_invoiceVOs.length == 0)) {
        MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000084"), NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000085"));

        getBillListPanel().getBodyBillModel().clearBodyData();
        getBillListPanel().updateUI();

        for (int i = 0; i < 5; i++) {
          this.m_nButtonState[i] = 0;
        }
        this.m_bInvoice = false;
        if ((this.m_bStock) && (this.m_bInvoice))
          this.m_nButtonState[4] = 0;
        else
          this.m_nButtonState[4] = 1;
        changeButtonState();
        return;
      }

      FreeVOParse freeParse = new FreeVOParse();

      Vector vTemp = new Vector();
      for (int i = 0; i < this.m_invoiceVOs.length; i++) {
        if ((this.m_invoiceVOs[i].getVfree1() == null) && (this.m_invoiceVOs[i].getVfree2() == null) && 
          (this.m_invoiceVOs[i].getVfree3() == null) && (this.m_invoiceVOs[i].getVfree4() == null) && 
          (this.m_invoiceVOs[i].getVfree5() == null)) continue;
        vTemp.addElement(this.m_invoiceVOs[i]);
      }

      if (vTemp.size() > 0) {
        IinvoiceVO[] tempVO = new IinvoiceVO[vTemp.size()];
        vTemp.copyInto(tempVO);
        freeParse.setFreeVO(tempVO, "vfree", "vfree", null, "cmangid", false);
      }

      if ((this.m_feeVOs != null) && (this.m_feeVOs.length > 0)) {
        vTemp = new Vector();
        for (int i = 0; i < this.m_feeVOs.length; i++) {
          if ((this.m_feeVOs[i].getVfree1() == null) && (this.m_feeVOs[i].getVfree2() == null) && 
            (this.m_feeVOs[i].getVfree3() == null) && (this.m_feeVOs[i].getVfree4() == null) && 
            (this.m_feeVOs[i].getVfree5() == null)) continue;
          vTemp.addElement(this.m_feeVOs[i]);
        }

        if (vTemp.size() > 0) {
          IinvoiceVO[] tempVO = new IinvoiceVO[vTemp.size()];
          vTemp.copyInto(tempVO);
          freeParse.setFreeVO(tempVO, "vfree", "vfree", null, "cmangid", false);
        }
      }

      if ((this.m_discountVOs != null) && (this.m_discountVOs.length > 0)) {
        vTemp = new Vector();
        for (int i = 0; i < this.m_discountVOs.length; i++) {
          if ((this.m_discountVOs[i].getVfree1() == null) && (this.m_discountVOs[i].getVfree2() == null) && 
            (this.m_discountVOs[i].getVfree3() == null) && (this.m_discountVOs[i].getVfree4() == null) && 
            (this.m_discountVOs[i].getVfree5() == null)) continue;
          vTemp.addElement(this.m_discountVOs[i]);
        }

        if (vTemp.size() > 0) {
          IinvoiceVO[] tempVO = new IinvoiceVO[vTemp.size()];
          vTemp.copyInto(tempVO);
          freeParse.setFreeVO(tempVO, "vfree", "vfree", null, "cmangid", false);
        }

      }

      Vector v = new Vector();
      for (int i = 0; i < this.m_invoiceVOs.length; i++)
        v.addElement(this.m_invoiceVOs[i]);
      int iLen = this.m_feeVOs == null ? 0 : this.m_feeVOs.length;
      for (int i = 0; i < iLen; i++) {
        IinvoiceVO tempVO = new IinvoiceVO();
        tempVO.setCbaseid(this.m_feeVOs[i].getCbaseid());
        tempVO.setCid(this.m_feeVOs[i].getCcostfactorid());
        tempVO.setCinvoice_bid(this.m_feeVOs[i].getCinvoice_bid());
        tempVO.setCinvoiceid(this.m_feeVOs[i].getCinvoiceid());
        tempVO.setCmangid(this.m_feeVOs[i].getCmangid());
        tempVO.setNaccumsettlemny(this.m_feeVOs[i].getNaccumsettlemny());
        tempVO.setNaccumsettlenum(new UFDouble(0.0D));
        tempVO.setNinvoicenum(new UFDouble(0.0D));
        tempVO.setNmoney(this.m_feeVOs[i].getNmny());
        tempVO.setNnosettlemny(this.m_feeVOs[i].getNnosettlemny());
        tempVO.setNnosettlenum(new UFDouble(0.0D));
        tempVO.setCvendorbaseid(this.m_feeVOs[i].getCvendorbaseid());
        tempVO.setCvendormangid(this.m_feeVOs[i].getCvendormangid());
        tempVO.setVinvoicecode(this.m_feeVOs[i].getVinvoicecode());
        tempVO.setCdeptid(this.m_feeVOs[i].getCdeptid());
        tempVO.setCoperatorid(this.m_feeVOs[i].getCoperator());
        tempVO.setVbatchcode(this.m_feeVOs[i].getVbatchcode());
        tempVO.setVfree(this.m_feeVOs[i].getVfree());

        tempVO.setCupsourcebilltype(this.m_feeVOs[i].getCupsourcebilltype());
        tempVO.setCupsourcebillid(this.m_feeVOs[i].getCupsourcebillid());
        tempVO.setCupsourcebillrowid(this.m_feeVOs[i].getCupsourcebillrowid());

        tempVO.setCsourcebilltype(this.m_feeVOs[i].getCsourcebilltype());
        tempVO.setCsourcebillid(this.m_feeVOs[i].getCsourcebillid());
        tempVO.setCsourcebillbid(this.m_feeVOs[i].getCsourcerowid());

        v.addElement(tempVO);
      }
      iLen = this.m_discountVOs == null ? 0 : this.m_discountVOs.length;
      for (int i = 0; i < iLen; i++) {
        IinvoiceVO tempVO = new IinvoiceVO();
        tempVO.setCbaseid(this.m_discountVOs[i].getCbaseid());
        tempVO.setCid(this.m_discountVOs[i].getCcostfactorid());
        tempVO.setCinvoice_bid(this.m_discountVOs[i].getCinvoice_bid());
        tempVO.setCinvoiceid(this.m_discountVOs[i].getCinvoiceid());
        tempVO.setCmangid(this.m_discountVOs[i].getCmangid());
        tempVO.setNaccumsettlemny(this.m_discountVOs[i].getNaccumsettlemny());
        tempVO.setNaccumsettlenum(new UFDouble(0.0D));
        tempVO.setNinvoicenum(new UFDouble(0.0D));
        tempVO.setNmoney(this.m_discountVOs[i].getNmny());
        tempVO.setNnosettlemny(this.m_discountVOs[i].getNnosettlemny());
        tempVO.setNnosettlenum(new UFDouble(0.0D));
        tempVO.setCvendorbaseid(this.m_discountVOs[i].getCvendorbaseid());
        tempVO.setCvendormangid(this.m_discountVOs[i].getCvendormangid());
        tempVO.setVinvoicecode(this.m_discountVOs[i].getVinvoicecode());
        tempVO.setCdeptid(this.m_discountVOs[i].getCdeptid());
        tempVO.setCoperatorid(this.m_discountVOs[i].getCoperator());
        tempVO.setVbatchcode(this.m_discountVOs[i].getVbatchcode());
        tempVO.setVfree(this.m_discountVOs[i].getVfree());

        tempVO.setCupsourcebilltype(this.m_discountVOs[i].getCupsourcebilltype());
        tempVO.setCupsourcebillid(this.m_discountVOs[i].getCupsourcebillid());
        tempVO.setCupsourcebillrowid(this.m_discountVOs[i].getCupsourcebillrowid());

        tempVO.setCsourcebilltype(this.m_discountVOs[i].getCsourcebilltype());
        tempVO.setCsourcebillid(this.m_discountVOs[i].getCsourcebillid());
        tempVO.setCsourcebillbid(this.m_discountVOs[i].getCsourcerowid());

        v.addElement(tempVO);
      }
      IinvoiceVO[] tempVOs = new IinvoiceVO[v.size()];
      v.copyInto(tempVOs);

      tTime = System.currentTimeMillis() - tTime;
      SCMEnv.out("发票查询时间：" + tTime + " ms!");

      getBillListPanel().getBodyBillModel().setBodyDataVO(tempVOs);
      getBillListPanel().getBodyBillModel().execLoadFormula();

      PuTool.loadSourceInfoAll(getBillListPanel().getBodyBillModel(), "ZP");

      getBillListPanel().getBodyBillModel().updateValue();
      getBillListPanel().updateUI();

      for (int i = 0; i < 5; i++) {
        this.m_nButtonState[i] = 0;
      }

      this.m_bInvoice = true;
      if ((this.m_bStock) && (this.m_bInvoice))
        this.m_nButtonState[4] = 0;
      else {
        this.m_nButtonState[4] = 1;
      }
      changeButtonState();
    } catch (SQLException e) {
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000084"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000412"));
      SCMEnv.out(e);
      return;
    } catch (ArrayIndexOutOfBoundsException e) {
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000084"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000426"));
      SCMEnv.out(e);
      return;
    } catch (NullPointerException e) {
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000084"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000427"));
      SCMEnv.out(e);
      return;
    } catch (Exception e) {
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000084"), e.getMessage());
      SCMEnv.out(e);
      return;
    }
  }

  private void loadStockData()
  {
    ConditionVO[] conditionVO = this.m_condClient1.getConditionVO();

    if (!checkQueryCondition(conditionVO)) {
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000082"), NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000083"));
      return;
    }

    try
    {
      long tTime = System.currentTimeMillis();
      this.m_stockVOs = SettleHelper.queryStock_yf(this.m_sUnitCode, conditionVO);

      if ((this.m_stockVOs == null) || (this.m_stockVOs.length == 0)) {
        MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000086"), NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000087"));

        getBillListPanel().getHeadBillModel().clearBodyData();
        getBillListPanel().updateUI();

        for (int i = 0; i < 5; i++) {
          this.m_nButtonState[i] = 0;
        }
        this.m_bStock = false;
        if ((this.m_bStock) && (this.m_bInvoice))
          this.m_nButtonState[4] = 0;
        else
          this.m_nButtonState[4] = 1;
        changeButtonState();
        return;
      }
      tTime = System.currentTimeMillis() - tTime;
      SCMEnv.out("入库单查询时间：" + tTime + " ms!");

      FreeVOParse freeParse = new FreeVOParse();
      Vector vTemp = new Vector();
      for (int i = 0; i < this.m_stockVOs.length; i++) {
        if ((this.m_stockVOs[i].getVfree1() == null) && (this.m_stockVOs[i].getVfree2() == null) && 
          (this.m_stockVOs[i].getVfree3() == null) && (this.m_stockVOs[i].getVfree4() == null) && 
          (this.m_stockVOs[i].getVfree5() == null)) continue;
        vTemp.addElement(this.m_stockVOs[i]);
      }

      if (vTemp.size() > 0) {
        StockVO[] tempVO = new StockVO[vTemp.size()];
        vTemp.copyInto(tempVO);
        freeParse.setFreeVO(tempVO, "vfree", "vfree", null, "cmangid", false);
      }

      getBillListPanel().getHeadBillModel().setBodyDataVO(this.m_stockVOs);
      getBillListPanel().getHeadBillModel().execLoadFormula();

      PuTool.loadSourceInfoAll(getBillListPanel().getHeadBillModel(), "ZP");

      getBillListPanel().getHeadBillModel().updateValue();
      getBillListPanel().updateUI();

      for (int i = 0; i < 5; i++) {
        this.m_nButtonState[i] = 0;
      }

      this.m_bStock = true;
      if ((this.m_bStock) && (this.m_bInvoice))
        this.m_nButtonState[4] = 0;
      else {
        this.m_nButtonState[4] = 1;
      }
      changeButtonState();
    } catch (SQLException e) {
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000086"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000412"));
      SCMEnv.out(e);
      return;
    } catch (ArrayIndexOutOfBoundsException e) {
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000086"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000426"));
      SCMEnv.out(e);
      return;
    } catch (NullPointerException e) {
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000086"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000427"));
      SCMEnv.out(e);
      return;
    } catch (Exception e) {
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000086"), e.getMessage());
      SCMEnv.out(e);
      return;
    }
  }

  private Vector modifyFee(Vector vFeeKey)
  {
    Vector v = new Vector();
    Vector v1 = new Vector();
    Vector v2 = new Vector();

    for (int i = 0; i < vFeeKey.size(); i++) {
      String s = (String)vFeeKey.elementAt(i);
      double nSettlemny = 0.0D;
      String s0 = "";
      for (int j = 0; j < this.m_settleVOs.length; j++) {
        String ss = this.m_settleVOs[j].getCinvoice_bid();
        if ((ss != null) && (ss.length() != 0)) {
          ss = ss.trim();
          if (s.equals(ss)) {
            nSettlemny += this.m_settleVOs[j].getNmoney().doubleValue();
            s0 = this.m_settleVOs[j].getCinvoiceid();
          }
        }
      }

      int k1 = -1;
      int k2 = -1;
      if ((this.m_feeVOs != null) && (this.m_feeVOs.length > 0)) {
        for (int j = 0; j < this.m_feeVOs.length; j++) {
          String s1 = this.m_feeVOs[j].getCinvoice_bid().trim();
          String s2 = this.m_feeVOs[j].getCinvoiceid().trim();
          if ((s.equals(s1)) && (s0.equals(s2))) {
            k1 = j;
            break;
          }
        }
      }
      if ((this.m_discountVOs != null) && (this.m_discountVOs.length > 0)) {
        for (int j = 0; j < this.m_discountVOs.length; j++) {
          String s1 = this.m_discountVOs[j].getCinvoice_bid().trim();
          String s2 = this.m_discountVOs[j].getCinvoiceid().trim();
          if ((s.equals(s1)) && (s0.equals(s2))) {
            k2 = j;
            break;
          }
        }
      }

      if (k2 >= 0)
      {
        double d = this.m_discountVOs[k2].getNaccumsettlemny().doubleValue();
        d += nSettlemny;
        this.m_discountVOs[k2].setNaccumsettlemny(new UFDouble(d));

        v2.addElement(this.m_discountVOs[k2]);
      } else if (k1 >= 0)
      {
        double d = this.m_feeVOs[k1].getNaccumsettlemny().doubleValue();
        d += nSettlemny;
        this.m_feeVOs[k1].setNaccumsettlemny(new UFDouble(d));

        v1.addElement(this.m_feeVOs[k1]);
      } else {
        MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000088"), NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000089"));
        return null;
      }
    }

    v.addElement(v1);
    v.addElement(v2);
    return v;
  }

  private IinvoiceVO[] modifyInvoice(Vector vInvoiceKey)
  {
    Vector v = new Vector();

    for (int i = 0; i < vInvoiceKey.size(); i++) {
      String s = (String)vInvoiceKey.elementAt(i);
      double nSettlenum = 0.0D;
      double nSettlemny = 0.0D;
      String s0 = "";
      for (int j = 0; j < this.m_settleVOs.length; j++) {
        String ss = this.m_settleVOs[j].getCinvoice_bid();
        if ((ss != null) && (ss.length() != 0)) {
          ss = ss.trim();
          if (s.equals(ss)) {
            nSettlenum += this.m_settleVOs[j].getNsettlenum().doubleValue();
            nSettlemny += this.m_settleVOs[j].getNmoney().doubleValue();
            s0 = this.m_settleVOs[j].getCinvoiceid();
          }
        }
      }

      int k = -1;
      for (int j = 0; j < this.m_invoiceVOs.length; j++) {
        String s1 = this.m_invoiceVOs[j].getCinvoice_bid().trim();
        String s2 = this.m_invoiceVOs[j].getCinvoiceid().trim();
        if ((s.equals(s1)) && (s0.equals(s2))) {
          k = j;
          break;
        }
      }
      if (k == -1) {
        MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000090"), NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000089"));
        return null;
      }

      double d = this.m_invoiceVOs[k].getNaccumsettlenum().doubleValue();
      d += nSettlenum;
      this.m_invoiceVOs[k].setNaccumsettlenum(new UFDouble(d));
      d = this.m_invoiceVOs[k].getNaccumsettlemny().doubleValue();
      d += nSettlemny;
      this.m_invoiceVOs[k].setNaccumsettlemny(new UFDouble(d));

      v.addElement(this.m_invoiceVOs[k]);
    }

    if (v.size() > 0) {
      IinvoiceVO[] vos = new IinvoiceVO[v.size()];
      v.copyInto(vos);
      return vos;
    }

    return null;
  }

  private StockVO[] modifyStock(Vector vStockKey)
  {
    Vector v = new Vector();

    for (int i = 0; i < vStockKey.size(); i++) {
      String s = (String)vStockKey.elementAt(i);
      double nSettlenum = 0.0D;
      double nGaugemny = 0.0D;
      String s0 = "";
      for (int j = 0; j < this.m_settleVOs.length; j++) {
        String ss = this.m_settleVOs[j].getCstockrow();
        if ((ss != null) && (ss.length() != 0)) {
          ss = ss.trim();
          if (s.equals(ss)) {
            nSettlenum += this.m_settleVOs[j].getNsettlenum().doubleValue();
            nGaugemny += this.m_settleVOs[j].getNgaugemny().doubleValue();
            s0 = this.m_settleVOs[j].getCstockid();
          }
        }
      }

      int k = -1;
      for (int j = 0; j < this.m_stockVOs.length; j++) {
        String s1 = this.m_stockVOs[j].getCgeneralbid().trim();
        String s2 = this.m_stockVOs[j].getCgeneralhid().trim();
        if ((s.equals(s1)) && (s0.equals(s2))) {
          k = j;
          break;
        }
      }
      if (k == -1) {
        MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000091"), NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000089"));
        return null;
      }

      double d = this.m_stockVOs[k].getNaccumsettlenum().doubleValue();
      d += nSettlenum;
      this.m_stockVOs[k].setNaccumsettlenum(new UFDouble(d));
      d = this.m_stockVOs[k].getNaccumsettlemny().doubleValue();
      d += nGaugemny;
      this.m_stockVOs[k].setNaccumsettlemny(new UFDouble(d));

      v.addElement(this.m_stockVOs[k]);
    }

    if (v.size() > 0) {
      StockVO[] vos = new StockVO[v.size()];
      v.copyInto(vos);
      return vos;
    }

    return null;
  }

  public void onBalance()
  {
    double d = 0.0D;
    for (int i = 0; i < this.m_stockVOs.length; i++)
      d += this.m_totalVOs[i].getNinvoicemny().doubleValue();
    double dd = 0.0D;
    for (int i = this.m_stockVOs.length; i < this.m_totalVOs.length; i++) {
      dd += this.m_totalVOs[i].getNinvoicemny().doubleValue();
    }
    UFDouble dDigitVal = null;
    try {
      dDigitVal = PuPubVO.getDigitVal(POPubSetUI.getCCurrDecimal(getCorpPrimaryKey()));
    } catch (Exception e) {
      SCMEnv.out("取得本位币精度值时出现异常!");
    }
    if (dDigitVal == null) {
      dDigitVal = new UFDouble(0.01D);
    }

    if (Math.abs(d - dd) >= dDigitVal.doubleValue()) {
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000078"), NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000092"));
      return;
    }

    generateSettlebill();
    if ((this.m_settleVOs == null) || (this.m_settleVOs.length == 0))
    {
      showHintMessage(NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000093"));
      return;
    }

    for (int i = 0; i < this.m_stockVOs.length; i++) {
      double sum = 0.0D;
      if ((this.m_bIsentercost != null) && (this.m_bIsentercost.length > 0)) {
        for (int j = 0; j < this.m_bIsentercost.length; j++) {
          if ((j == 0) && (this.m_bIsentercost[j].booleanValue()))
            sum += this.m_totalVOs[i].getNfactor1().doubleValue();
          if ((j == 1) && (this.m_bIsentercost[j].booleanValue()))
            sum += this.m_totalVOs[i].getNfactor2().doubleValue();
          if ((j == 2) && (this.m_bIsentercost[j].booleanValue()))
            sum += this.m_totalVOs[i].getNfactor3().doubleValue();
          if ((j == 3) && (this.m_bIsentercost[j].booleanValue()))
            sum += this.m_totalVOs[i].getNfactor4().doubleValue();
          if ((j == 4) && (this.m_bIsentercost[j].booleanValue()))
            sum += this.m_totalVOs[i].getNfactor5().doubleValue();
        }
      }
      if (this.m_totalVOs[i].getNdiscountmny() != null)
        sum += this.m_totalVOs[i].getNdiscountmny().doubleValue();
      sum += this.m_totalVOs[i].getNinvoicemny().doubleValue();
      this.m_totalVOs[i].setNsettlemny(new UFDouble(PuTool.getRoundDouble(this.m_moneyDecimal, sum)));
    }

    getTotalBillListPanel().getHeadBillModel().setBodyDataVO(this.m_totalVOs);
    getTotalBillListPanel().getHeadBillModel().execLoadFormula();
    getTotalBillListPanel().getHeadBillModel().updateValue();
    getTotalBillListPanel().updateUI();

    if (this.m_bDistribute)
    {
      Vector v = new Vector();
      for (int i = 0; i < this.m_settleVOs.length; i++)
        v.addElement(this.m_settleVOs[i]);
      if ((this.m_feeVOs != null) && (this.m_feeVOs.length > 0)) {
        SettlebillItemVO[] body = new SettlebillItemVO[this.m_feeVOs.length];
        for (int i = 0; i < body.length; i++) {
          body[i] = new SettlebillItemVO();
          body[i].setPk_corp(this.m_sUnitCode);
          body[i].setCinvoice_bid(this.m_feeVOs[i].getCinvoice_bid());
          body[i].setCinvoiceid(this.m_feeVOs[i].getCinvoiceid());
          body[i].setCmangid(this.m_feeVOs[i].getCmangid());
          body[i].setCbaseid(this.m_feeVOs[i].getCbaseid());
          body[i].setNmoney(this.m_feeVOs[i].getNsettlemny());
          v.addElement(body[i]);
        }
      }
      if ((this.m_discountVOs != null) && (this.m_discountVOs.length > 0)) {
        SettlebillItemVO[] body1 = new SettlebillItemVO[this.m_discountVOs.length];
        for (int i = 0; i < body1.length; i++) {
          body1[i] = new SettlebillItemVO();
          body1[i].setPk_corp(this.m_sUnitCode);
          body1[i].setCinvoice_bid(this.m_discountVOs[i].getCinvoice_bid());
          body1[i].setCinvoiceid(this.m_discountVOs[i].getCinvoiceid());
          body1[i].setCmangid(this.m_discountVOs[i].getCmangid());
          body1[i].setCbaseid(this.m_discountVOs[i].getCbaseid());
          body1[i].setNmoney(this.m_discountVOs[i].getNsettlemny());
          v.addElement(body1[i]);
        }
      }
      this.m_settleVOs = new SettlebillItemVO[v.size()];
      v.copyInto(this.m_settleVOs);
    }

    doModification();

    for (int i = 0; i < 4; i++) {
      this.m_nButtonState[i] = 1;
    }
    changeButtonState();

    refreshSettleUI();
  }

  public void onButtonClicked(ButtonObject bo)
  {
    if (bo == this.m_buttons1[0]) {
      onSelectAll();
      showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000033"));
    }

    if (bo == this.m_buttons1[1]) {
      onSelectNo();
      showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000034"));
    }

    if (bo == this.m_buttons1[2]) {
      showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH046"));
      onInvoiceQuery();
      showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH009"));
    }

    if (bo == this.m_buttons1[3]) {
      showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH046"));
      onStockQuery();
      showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH009"));
    }

    if (bo == this.m_buttons1[4]) onConfirm();

    if (bo == this.m_buttons2[0]) onDistribute();

    if (bo == this.m_buttons2[1].getChildButtonGroup()[0]) onDistribute_Num();

    if (bo == this.m_buttons2[1].getChildButtonGroup()[1]) onDistribute_Fee();

    if (bo == this.m_buttons2[2]) onBalance();

    if (bo == this.m_buttons2[3]) {
      onReturn();
      showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000053"));
    }
  }

  public boolean onClosing()
  {
    if (this.m_bSettling)
    {
      int nReturn = MessageDialog.showYesNoCancelDlg(this, NCLangRes.getInstance().getStrByID("4004050204", "UPP4004050204-000004"), NCLangRes.getInstance().getStrByID("4004050204", "UPP4004050204-000005"));
      if (nReturn != 4) {
        return false;
      }
      this.m_bSettling = false;
    }

    return true;
  }

  public void onConfirm()
  {
    if (!doClassification()) return;

    this.m_bSettling = true;

    int nTotal = this.m_stockVOs.length + this.m_invoiceVOs.length;
    this.m_totalVOs = new SettletotalVO[nTotal];
    for (int i = 0; i < nTotal; i++) this.m_totalVOs[i] = new SettletotalVO();

    for (int i = 0; i < this.m_stockVOs.length; i++) {
      this.m_totalVOs[i].setCbilltype(NCLangRes.getInstance().getStrByID("4004050204", "UPT4004050204-000002"));
      this.m_totalVOs[i].setCbill_bid(this.m_stockVOs[i].getCgeneralbid());
      this.m_totalVOs[i].setCbillid(this.m_stockVOs[i].getCgeneralhid());
      this.m_totalVOs[i].setVbillcode(this.m_stockVOs[i].getVbillcode());
      this.m_totalVOs[i].setCmangid(this.m_stockVOs[i].getCmangid());
      this.m_totalVOs[i].setCbaseid(this.m_stockVOs[i].getCbaseid());
      this.m_totalVOs[i].setVbatchcode(this.m_stockVOs[i].getVbatchcode());
      this.m_totalVOs[i].setVfree(this.m_stockVOs[i].getVfree());

      this.m_totalVOs[i].setNnosettlenum(this.m_stockVOs[i].getNnosettlenum());
      this.m_totalVOs[i].setNnosettlemny(this.m_stockVOs[i].getNnosettlemny());
      this.m_totalVOs[i].setNprice(this.m_stockVOs[i].getNprice());

      this.m_totalVOs[i].setNstocknum(this.m_stockVOs[i].getNnosettlenum());
      this.m_totalVOs[i].setNgaugemny(this.m_stockVOs[i].getNnosettlemny());

      this.m_totalVOs[i].setNsettlemny(this.m_totalVOs[i].getNgaugemny());

      this.m_totalVOs[i].setNfactor1(new UFDouble(0.0D));
      this.m_totalVOs[i].setNfactor2(new UFDouble(0.0D));
      this.m_totalVOs[i].setNfactor3(new UFDouble(0.0D));
      this.m_totalVOs[i].setNfactor4(new UFDouble(0.0D));
      this.m_totalVOs[i].setNfactor5(new UFDouble(0.0D));
      this.m_totalVOs[i].setNdiscountmny(new UFDouble(0.0D));

      this.m_totalVOs[i].setNsettlemny(new UFDouble(0.0D));
      this.m_totalVOs[i].setNinvoicemny(new UFDouble(0.0D));
      this.m_totalVOs[i].setNstockaccumsettlenum(this.m_stockVOs[i].getNaccumsettlenum());

      this.m_totalVOs[i].setPk_stockcorp(this.m_stockVOs[i].getPk_stockcorp());
    }
    int nStock = this.m_stockVOs.length;
    for (int i = nStock; i < nTotal; i++) {
      this.m_totalVOs[i].setCbilltype(NCLangRes.getInstance().getStrByID("4004050204", "UPT4004050204-000001"));
      this.m_totalVOs[i].setCbill_bid(this.m_invoiceVOs[(i - nStock)].getCinvoice_bid());
      this.m_totalVOs[i].setCbillid(this.m_invoiceVOs[(i - nStock)].getCinvoiceid());
      this.m_totalVOs[i].setVbillcode(this.m_invoiceVOs[(i - nStock)].getVinvoicecode());
      this.m_totalVOs[i].setCmangid(this.m_invoiceVOs[(i - nStock)].getCmangid());
      this.m_totalVOs[i].setCbaseid(this.m_invoiceVOs[(i - nStock)].getCbaseid());
      this.m_totalVOs[i].setVbatchcode(this.m_invoiceVOs[(i - nStock)].getVbatchcode());
      this.m_totalVOs[i].setVfree(this.m_invoiceVOs[(i - nStock)].getVfree());

      this.m_totalVOs[i].setNnosettlenum(this.m_invoiceVOs[(i - nStock)].getNnosettlenum());
      this.m_totalVOs[i].setNnosettlemny(this.m_invoiceVOs[(i - nStock)].getNnosettlemny());
      UFDouble d1 = this.m_invoiceVOs[(i - nStock)].getNnosettlenum();
      UFDouble d2 = this.m_invoiceVOs[(i - nStock)].getNnosettlemny();
      if ((d1 != null) && (d2 != null) && (d1.doubleValue() != 0.0D)) {
        double d = d2.doubleValue() / d1.doubleValue();
        this.m_totalVOs[i].setNprice(new UFDouble(d));
      }

      this.m_totalVOs[i].setNinvoicenum(this.m_invoiceVOs[(i - nStock)].getNnosettlenum());
      this.m_totalVOs[i].setNinvoicemny(this.m_invoiceVOs[(i - nStock)].getNnosettlemny());
    }

    getBillListPanel().setVisible(false);
    setButtons(this.m_buttons2);
    this.m_nButtonState = new int[this.m_buttons2.length];

    setLayout(new BorderLayout());
    add(getTotalBillListPanel(), "Center");

    changeFactorShowName();

    getTotalBillListPanel().getBillListData().getBodyItem("fapportionmode").setWithIndex(true);
    this.m_comDistribute = ((UIComboBox)getTotalBillListPanel().getBillListData().getBodyItem("fapportionmode").getComponent());
    this.m_comDistribute.setTranslate(true);
    this.m_comDistribute.addItem(NCLangRes.getInstance().getStrByID("4004050204", "UPP4004050204-000006"));
    this.m_comDistribute.addItem(NCLangRes.getInstance().getStrByID("4004050204", "UPP4004050204-000007"));
    this.m_comDistribute.addItem(NCLangRes.getInstance().getStrByID("4004050204", "UPP4004050204-000008"));
    this.m_comDistribute.addItem(NCLangRes.getInstance().getStrByID("4004050204", "UPP4004050204-000009"));

    Vector v = new Vector();
    if ((this.m_feeVOs != null) && (this.m_feeVOs.length > 0)) {
      for (int i = 0; i < this.m_feeVOs.length; i++) v.addElement(this.m_feeVOs[i]);
    }
    if ((this.m_discountVOs != null) && (this.m_discountVOs.length > 0)) {
      for (int i = 0; i < this.m_discountVOs.length; i++) v.addElement(this.m_discountVOs[i]);
    }
    FeeinvoiceVO[] vo = new FeeinvoiceVO[v.size()];
    v.copyInto(vo);

    getTotalBillListPanel().getHeadBillModel().setBodyDataVO(this.m_totalVOs);
    getTotalBillListPanel().getHeadBillModel().execLoadFormula();
    getTotalBillListPanel().getHeadBillModel().updateValue();
    if ((vo != null) && (vo.length > 0)) {
      getTotalBillListPanel().getBodyBillModel().setBodyDataVO(vo);
      getTotalBillListPanel().getBodyBillModel().execLoadFormula();
      getTotalBillListPanel().getBodyBillModel().updateValue();
    } else {
      getTotalBillListPanel().getBodyBillModel().clearBodyData();
    }
    getTotalBillListPanel().updateUI();

    for (int i = 0; i < 4; i++) {
      this.m_nButtonState[i] = 0;
    }
    this.m_nButtonState[2] = 1;

    if (getTotalBillListPanel().getBodyBillModel().getRowCount() == 0) this.m_nButtonState[0] = 1;
    changeButtonState();

    this.m_buttons2[1].getChildButtonGroup()[0].setEnabled(true);
    this.m_buttons2[1].getChildButtonGroup()[1].setEnabled(true);
    updateButton(this.m_buttons2[1].getChildButtonGroup()[0]);
    updateButton(this.m_buttons2[1].getChildButtonGroup()[1]);

    getTotalBillListPanel().setEnabled(true);
    setPartEditable();
  }

  public void onDistribute()
  {
    this.m_bDistribute = true;

    long tTime = System.currentTimeMillis();
    distributeFee();
    distributeDiscount();
    tTime = System.currentTimeMillis() - tTime;
    SCMEnv.out("费用分摊时间：" + tTime + " ms!");

    for (int i = 0; i < this.m_stockVOs.length; i++) {
      double sum = 0.0D;
      if ((this.m_bIsentercost != null) && (this.m_bIsentercost.length > 0)) {
        for (int j = 0; j < this.m_bIsentercost.length; j++) {
          if ((j == 0) && (this.m_bIsentercost[j].booleanValue())) sum += this.m_totalVOs[i].getNfactor1().doubleValue();
          if ((j == 1) && (this.m_bIsentercost[j].booleanValue())) sum += this.m_totalVOs[i].getNfactor2().doubleValue();
          if ((j == 2) && (this.m_bIsentercost[j].booleanValue())) sum += this.m_totalVOs[i].getNfactor3().doubleValue();
          if ((j == 3) && (this.m_bIsentercost[j].booleanValue())) sum += this.m_totalVOs[i].getNfactor4().doubleValue();
          if ((j != 4) || (!this.m_bIsentercost[j].booleanValue())) continue; sum += this.m_totalVOs[i].getNfactor5().doubleValue();
        }
      }
      if (this.m_totalVOs[i].getNdiscountmny() != null) sum += this.m_totalVOs[i].getNdiscountmny().doubleValue();
      sum += this.m_totalVOs[i].getNinvoicemny().doubleValue();
      this.m_totalVOs[i].setNsettlemny(new UFDouble(PuTool.getRoundDouble(this.m_moneyDecimal, sum)));
    }

    getTotalBillListPanel().getHeadBillModel().setBodyDataVO(this.m_totalVOs);
    getTotalBillListPanel().getHeadBillModel().execLoadFormula();
    getTotalBillListPanel().getHeadBillModel().updateValue();
    getTotalBillListPanel().updateUI();

    for (int i = 0; i < 4; i++) {
      this.m_nButtonState[i] = 0;
    }
    this.m_nButtonState[0] = 1;
    this.m_nButtonState[3] = 1;

    if (!this.m_bInvoiceDistribute) this.m_nButtonState[2] = 1;
    changeButtonState();

    getTotalBillListPanel().setEnabled(false);
  }

  public void onDistribute_Fee()
  {
    if ((this.m_invoiceVOs == null) || (this.m_invoiceVOs.length == 0)) {
      MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000094"), NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000095"));
      return;
    }

    this.m_bInvoiceDistribute = true;
    long tTime = System.currentTimeMillis();

    double d = 0.0D;
    for (int i = this.m_stockVOs.length; i < this.m_totalVOs.length; i++) d += this.m_totalVOs[i].getNinvoicemny().doubleValue();

    double nNum = 0.0D;
    for (int i = 0; i < this.m_stockVOs.length; i++) nNum += this.m_totalVOs[i].getNgaugemny().doubleValue();
    if (nNum == 0.0D) {
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000096"), NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000097"));
      return;
    }

    UFDouble befnmy = new UFDouble(0);
    for (int i = 0; i < this.m_stockVOs.length - 1; i++) {
      double dd = this.m_totalVOs[i].getNinvoicemny().doubleValue();
      dd = d / nNum * this.m_totalVOs[i].getNgaugemny().doubleValue();
      befnmy = befnmy.add(new UFDouble(dd, 2));
      this.m_totalVOs[i].setNinvoicemny(new UFDouble(dd, 2));
    }
    this.m_totalVOs[(this.m_stockVOs.length - 1)].setNinvoicemny(new UFDouble(d, 2).sub(befnmy));

    tTime = System.currentTimeMillis() - tTime;
    SCMEnv.out("发票按金额分摊时间：" + tTime + " ms!");

    for (int i = 0; i < this.m_stockVOs.length; i++) {
      double sum = 0.0D;
      if ((this.m_bIsentercost != null) && (this.m_bIsentercost.length > 0)) {
        for (int j = 0; j < this.m_bIsentercost.length; j++) {
          if ((j == 0) && (this.m_bIsentercost[j].booleanValue())) sum += this.m_totalVOs[i].getNfactor1().doubleValue();
          if ((j == 1) && (this.m_bIsentercost[j].booleanValue())) sum += this.m_totalVOs[i].getNfactor2().doubleValue();
          if ((j == 2) && (this.m_bIsentercost[j].booleanValue())) sum += this.m_totalVOs[i].getNfactor3().doubleValue();
          if ((j == 3) && (this.m_bIsentercost[j].booleanValue())) sum += this.m_totalVOs[i].getNfactor4().doubleValue();
          if ((j != 4) || (!this.m_bIsentercost[j].booleanValue())) continue; sum += this.m_totalVOs[i].getNfactor5().doubleValue();
        }
      }
      if (this.m_totalVOs[i].getNdiscountmny() != null) sum += this.m_totalVOs[i].getNdiscountmny().doubleValue();
      sum += this.m_totalVOs[i].getNinvoicemny().doubleValue();
      this.m_totalVOs[i].setNsettlemny(new UFDouble(sum));
    }

    getTotalBillListPanel().getHeadBillModel().setBodyDataVO(this.m_totalVOs);
    getTotalBillListPanel().getHeadBillModel().execLoadFormula();
    getTotalBillListPanel().getHeadBillModel().updateValue();
    getTotalBillListPanel().updateUI();

    for (int i = 1; i < 4; i++) {
      this.m_nButtonState[i] = 0;
    }
    this.m_nButtonState[1] = 1;
    this.m_nButtonState[3] = 1;

    if (this.m_bDistribute) this.m_nButtonState[0] = 1;
    changeButtonState();

    this.m_buttons2[1].getChildButtonGroup()[0].setEnabled(false);
    this.m_buttons2[1].getChildButtonGroup()[1].setEnabled(false);
    updateButton(this.m_buttons2[1].getChildButtonGroup()[0]);
    updateButton(this.m_buttons2[1].getChildButtonGroup()[1]);

    MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000094"), NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000098"));

    setPartEditable();
  }

  public void onDistribute_Num()
  {
    if ((this.m_invoiceVOs == null) || (this.m_invoiceVOs.length == 0)) {
      MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000094"), NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000095"));
      return;
    }

    this.m_bInvoiceDistribute = true;
    long tTime = System.currentTimeMillis();

    double d = 0.0D;
    for (int i = this.m_stockVOs.length; i < this.m_totalVOs.length; i++) d += this.m_totalVOs[i].getNinvoicemny().doubleValue();

    double nNum = 0.0D;
    for (int i = 0; i < this.m_stockVOs.length; i++) nNum += this.m_totalVOs[i].getNstocknum().doubleValue();
    if (nNum == 0.0D) {
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000099"), NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000100"));
      return;
    }
    for (int i = 0; i < this.m_stockVOs.length; i++) {
      double dd = this.m_totalVOs[i].getNinvoicemny().doubleValue();
      dd += d / nNum * this.m_totalVOs[i].getNstocknum().doubleValue();
      this.m_totalVOs[i].setNinvoicemny(new UFDouble(dd));
    }
    tTime = System.currentTimeMillis() - tTime;
    SCMEnv.out("发票按数量分摊时间：" + tTime + " ms!");

    for (int i = 0; i < this.m_stockVOs.length; i++) {
      double sum = 0.0D;
      if ((this.m_bIsentercost != null) && (this.m_bIsentercost.length > 0)) {
        for (int j = 0; j < this.m_bIsentercost.length; j++) {
          if ((j == 0) && (this.m_bIsentercost[j].booleanValue())) sum += this.m_totalVOs[i].getNfactor1().doubleValue();
          if ((j == 1) && (this.m_bIsentercost[j].booleanValue())) sum += this.m_totalVOs[i].getNfactor2().doubleValue();
          if ((j == 2) && (this.m_bIsentercost[j].booleanValue())) sum += this.m_totalVOs[i].getNfactor3().doubleValue();
          if ((j == 3) && (this.m_bIsentercost[j].booleanValue())) sum += this.m_totalVOs[i].getNfactor4().doubleValue();
          if ((j != 4) || (!this.m_bIsentercost[j].booleanValue())) continue; sum += this.m_totalVOs[i].getNfactor5().doubleValue();
        }
      }
      if (this.m_totalVOs[i].getNdiscountmny() != null) sum += this.m_totalVOs[i].getNdiscountmny().doubleValue();
      sum += this.m_totalVOs[i].getNinvoicemny().doubleValue();
      this.m_totalVOs[i].setNsettlemny(new UFDouble(sum));
    }

    getTotalBillListPanel().getHeadBillModel().setBodyDataVO(this.m_totalVOs);
    getTotalBillListPanel().getHeadBillModel().execLoadFormula();
    getTotalBillListPanel().getHeadBillModel().updateValue();
    getTotalBillListPanel().updateUI();

    for (int i = 1; i < 4; i++) {
      this.m_nButtonState[i] = 0;
    }
    this.m_nButtonState[1] = 1;
    this.m_nButtonState[3] = 1;

    if (this.m_bDistribute) this.m_nButtonState[0] = 1;
    changeButtonState();

    this.m_buttons2[1].getChildButtonGroup()[0].setEnabled(false);
    this.m_buttons2[1].getChildButtonGroup()[1].setEnabled(false);
    updateButton(this.m_buttons2[1].getChildButtonGroup()[0]);
    updateButton(this.m_buttons2[1].getChildButtonGroup()[1]);

    MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000094"), NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000098"));

    setPartEditable();
  }

  public void onInvoiceQuery()
  {
    initQueryModelForInvoice();

    this.m_condClient2.hideNormal();
    this.m_condClient2.hideCorp();
    this.m_condClient2.showModal();

    if (this.m_condClient2.isCloseOK()) {
      loadInvoiceData();
    }
    setBtnsState();
  }

  public void onReturn()
  {
    showHintMessage("");

    this.m_bSettling = false;

    Vector v0 = new Vector();
    for (int i = 0; i < this.m_mStockVOs.length; i++) v0.addElement(this.m_mStockVOs[i]);
    this.m_stockVOs = new StockVO[v0.size()];
    v0.copyInto(this.m_stockVOs);

    Vector v00 = new Vector();
    for (int i = 0; i < this.m_mInvoiceVOs.length; i++) v00.addElement(this.m_mInvoiceVOs[i]);
    this.m_invoiceVOs = new IinvoiceVO[v00.size()];
    v00.copyInto(this.m_invoiceVOs);

    Vector v000 = new Vector();
    for (int i = 0; i < this.m_mFeeVOs.length; i++) v000.addElement(this.m_mFeeVOs[i]);
    this.m_feeVOs = new FeeinvoiceVO[v000.size()];
    v000.copyInto(this.m_feeVOs);

    Vector v0000 = new Vector();
    for (int i = 0; i < this.m_mDiscountVOs.length; i++) v0000.addElement(this.m_mDiscountVOs[i]);
    this.m_discountVOs = new FeeinvoiceVO[v0000.size()];
    v0000.copyInto(this.m_discountVOs);

    setButtons(this.m_buttons1);
    this.m_nButtonState = new int[this.m_buttons1.length];
    remove(getTotalBillListPanel());

    getBillListPanel().setVisible(true);

    this.m_bDistribute = false;
  }

  public void onSelectAll()
  {
    int nRow = getBillListPanel().getHeadBillModel().getRowCount();
    for (int i = 0; i < nRow; i++) getBillListPanel().getHeadBillModel().setRowState(i, 4);

    nRow = getBillListPanel().getBodyBillModel().getRowCount();
    for (int i = 0; i < nRow; i++) getBillListPanel().getBodyBillModel().setRowState(i, 4);

    getBillListPanel().updateUI();

    setBtnsState();
  }

  public void onSelectNo()
  {
    int nRow = getBillListPanel().getHeadBillModel().getRowCount();
    for (int i = 0; i < nRow; i++) getBillListPanel().getHeadBillModel().setRowState(i, 0);

    nRow = getBillListPanel().getBodyBillModel().getRowCount();
    for (int i = 0; i < nRow; i++) getBillListPanel().getBodyBillModel().setRowState(i, 0);

    getBillListPanel().updateUI();

    setBtnsState();
  }

  public void onStockQuery()
  {
    if (!this.m_bICStartUp)
    {
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("common", "UC001-0000006"), NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000101"));
      return;
    }

    initQueryModelForStock();

    this.m_condClient1.hideNormal();
    this.m_condClient1.hideCorp();
    this.m_condClient1.showModal();

    if (this.m_condClient1.isCloseOK()) {
      loadStockData();
    }
    setBtnsState();
  }

  private void refreshInvoice()
  {
    loadInvoiceData();
  }

  private void refreshSettleUI()
  {
    this.m_bSettling = false;

    this.m_stockVOs = null;
    this.m_invoiceVOs = null;
    this.m_totalVOs = null;
    this.m_feeVOs = null;
    this.m_discountVOs = null;
    this.m_settleVOs = null;

    this.m_mStockVOs = null;
    this.m_mInvoiceVOs = null;
    this.m_mFeeVOs = null;
    this.m_mDiscountVOs = null;

    setButtons(this.m_buttons1);
    this.m_nButtonState = new int[this.m_buttons1.length];
    remove(getTotalBillListPanel());

    getBillListPanel().setVisible(true);

    this.m_bStock = false;
    this.m_bInvoice = false;
    this.m_bDistribute = false;
    this.m_bInvoiceDistribute = false;

    refreshStock();

    refreshInvoice();

    setBtnsState();
  }

  private void refreshStock()
  {
    loadStockData();
  }

  private void setBtnsState()
  {
    int iHCnt = getBillListPanel().getHeadBillModel().getRowCount();
    int iBCnt = getBillListPanel().getBodyBillModel().getRowCount();
    int iHSelCnt = 0;
    for (int i = 0; i < iHCnt; i++) {
      if (4 == getBillListPanel().getHeadBillModel().getRowState(i)) {
        iHSelCnt++;
      }
    }
    int iBSelCnt = 0;
    for (int i = 0; i < iBCnt; i++) {
      if (4 == getBillListPanel().getBodyBillModel().getRowState(i)) {
        iBSelCnt++;
      }

    }

    if ((iHCnt <= 0) && (iBCnt <= 0)) {
      this.m_nButtonState[0] = 1;
      this.m_nButtonState[1] = 1;
      this.m_nButtonState[2] = 0;
      this.m_nButtonState[3] = 0;
      this.m_nButtonState[4] = 1;
      changeButtonState();
      return;
    }

    if ((iHCnt == iHSelCnt) && (iBCnt == iBSelCnt))
      this.m_nButtonState[0] = 1;
    else {
      this.m_nButtonState[0] = 0;
    }

    if ((iHSelCnt > 0) || (iBSelCnt > 0))
      this.m_nButtonState[1] = 0;
    else {
      this.m_nButtonState[1] = 1;
    }

    this.m_nButtonState[2] = 0;

    this.m_nButtonState[3] = 0;

    if ((iHSelCnt > 0) && (iBSelCnt > 0))
      this.m_nButtonState[4] = 0;
    else {
      this.m_nButtonState[4] = 1;
    }

    changeButtonState();
  }

  private void setPartEditable()
  {
    BillItem[] items = getTotalBillListPanel().getHeadBillModel().getBodyItems();
    int iLen = 0;
    boolean bVal = false;
    String sKey = null;
    if (items != null) {
      iLen = items.length;
      for (int i = 0; i < iLen; i++) {
        if ((items[i] == null) || (!items[i].isShow()))
          continue;
        sKey = items[i].getKey().trim();
        bVal = ("nstocknum".equalsIgnoreCase(sKey)) || ("ninvoicenum".equalsIgnoreCase(sKey));
        items[i].setEnabled(bVal);
        items[i].setEdit(bVal);
      }
    }

    for (int i = 0; i < this.m_stockVOs.length; i++) {
      getTotalBillListPanel().getHeadBillModel().setCellEditable(i, "nstocknum", true);
      getTotalBillListPanel().getHeadBillModel().setCellEditable(i, "ninvoicenum", false);
    }
    for (int i = this.m_stockVOs.length; i < this.m_totalVOs.length; i++) {
      getTotalBillListPanel().getHeadBillModel().setCellEditable(i, "nstocknum", false);
      getTotalBillListPanel().getHeadBillModel().setCellEditable(i, "ninvoicenum", true);
    }

    items = getTotalBillListPanel().getBodyBillModel().getBodyItems();
    if (items != null) {
      iLen = items.length;
      for (int i = 0; i < iLen; i++) {
        if ((items[i] == null) || (!items[i].isShow()))
          continue;
        sKey = items[i].getKey().trim();
        bVal = "nsettlemny".equalsIgnoreCase(sKey);
        items[i].setEnabled(bVal);
        items[i].setEdit(bVal);
      }
    }

    UIRefPane nRefPanel1 = (UIRefPane)getTotalBillListPanel().getHeadItem("nstocknum").getComponent();
    UITextField nStockNumUI = nRefPanel1.getUITextField();
    nStockNumUI.setMaxLength(16);

    UIRefPane nRefPanel2 = (UIRefPane)getTotalBillListPanel().getHeadItem("ninvoicenum").getComponent();
    UITextField nInvoiceNumUI = nRefPanel2.getUITextField();
    nInvoiceNumUI.setMaxLength(16);

    UIRefPane nRefPanel3 = (UIRefPane)getTotalBillListPanel().getBodyItem("nsettlemny").getComponent();
    UITextField nMoneyUI = nRefPanel3.getUITextField();
    nMoneyUI.setMaxLength(16);

    for (int i = 0; i < this.m_stockVOs.length; i++) {
      getTotalBillListPanel().getHeadBillModel().setCellEditable(i, "ninvoicemny", true);
    }
    for (int i = this.m_stockVOs.length; i < this.m_totalVOs.length; i++) {
      getTotalBillListPanel().getHeadBillModel().setCellEditable(i, "ninvoicemny", false);
    }

    UIRefPane nRefPanel = (UIRefPane)getTotalBillListPanel().getHeadItem("ninvoicemny").getComponent();
    nMoneyUI = nRefPanel.getUITextField();
    nMoneyUI.setMaxLength(9);
  }

  public void valueChanged(ListSelectionEvent event)
  {
    if ((ListSelectionModel)event.getSource() == getBillListPanel().getHeadTable().getSelectionModel())
    {
      int nRow = getBillListPanel().getHeadBillModel().getRowCount();

      for (int i = 0; i < nRow; i++) {
        getBillListPanel().getHeadBillModel().setRowState(i, 0);
      }

      int[] nSelected = getBillListPanel().getHeadTable().getSelectedRows();
      if ((nSelected != null) && (nSelected.length > 0)) {
        for (int i = 0; i < nSelected.length; i++) {
          int j = nSelected[i];
          getBillListPanel().getHeadBillModel().setRowState(j, 4);
        }
      }
    }
    else if ((ListSelectionModel)event.getSource() == getBillListPanel().getBodyTable().getSelectionModel())
    {
      int nRow = getBillListPanel().getBodyBillModel().getRowCount();

      for (int i = 0; i < nRow; i++) {
        getBillListPanel().getBodyBillModel().setRowState(i, 0);
      }

      int[] nSelected = getBillListPanel().getBodyTable().getSelectedRows();
      if ((nSelected != null) && (nSelected.length > 0)) {
        for (int i = 0; i < nSelected.length; i++) {
          int j = nSelected[i];
          getBillListPanel().getBodyBillModel().setRowState(j, 4);
        }
      }
    }
    setBtnsState();
  }

  public void valueChanged(ValueChangedEvent event)
  {
    String pk_corp = this.m_corpPane.getRefPK();
    if (pk_corp != null)
      this.m_warePane.setPk_corp(pk_corp);
    else
      this.m_warePane.setPk_corp(this.m_sUnitCode);
  }
}