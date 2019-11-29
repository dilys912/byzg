package nc.ui.ia.ia501;

import java.awt.LayoutManager;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.bd.ref.UFRefManage;
import nc.ui.ia.pub.BillTypeRef;
import nc.ui.ia.pub.IAEnvironment;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.beans.layout.TableLayout;
import nc.vo.ia.pub.Log;

public class InoutLedgerNormalPanel extends UIPanel
{
  private static final long serialVersionUID = 1L;
  private UILabel ivjUILabel1 = null;
  private UILabel ivjUILabel111 = null;
  private UILabel ivjUILabel1111 = null;
  private UILabel ivjUILabel12 = null;
  private UILabel ivjUILabel121 = null;
  private UILabel ivjUILabel1211 = null;
  private UILabel ivjUILabel12111 = null;
  private UILabel ivjUILabel121111 = null;
  private UILabel ivjUILabel121112 = null;
  private UILabel ivjUILabel1211121 = null;
  private UILabel ivjUILabel1212 = null;
  private UIRefPane ivjUIRefBillDate1 = null;
  private UIRefPane ivjUIRefBillDate2 = null;
  private UIRefPane ivjUIRefCrdcenterid = null;
  private UIRefPane ivjUIRefCustname = null;
  private UIRefPane ivjUIRefCwarehouseid = null;

  private UIRefPane ivjUIRefInventoryid1 = null;
  private UIRefPane ivjUIRefInventoryid2 = null;
  private UIRefPane ivjUIRefOperatorname = null;
  private UITextField ivjUITextBillCode1 = null;
  private UITextField ivjUITextBillCode2 = null;
  private UICheckBox ivjUICheckBox1 = null;
  private BillTypeRef ivjBillTypeRef1 = null;
  private UILabel ivjUILabel112 = null;
  private IAEnvironment ce = new IAEnvironment();
  private UIPanel panel = null;

  private UILabel ivjUILabel11111 = null;
  private UILabel ivjUILabelinvclass = null;
  private UILabel ivjUILabelVendor = null;
  private UIRefPane ivjUIRefDept1 = null;
  private UIRefPane ivjUIRefDept2 = null;
  private UIRefPane ivjUIRefInvClass1 = null;
  private UIRefPane ivjUIRefVendor = null;

  public InoutLedgerNormalPanel()
  {
    initialize();
  }

  public InoutLedgerNormalPanel(LayoutManager p0)
  {
    super(p0);
  }

  public InoutLedgerNormalPanel(LayoutManager p0, boolean p1)
  {
    super(p0, p1);
  }

  public InoutLedgerNormalPanel(boolean p0)
  {
    super(p0);
  }

  private BillTypeRef getBillTypeRef1()
  {
    if (this.ivjBillTypeRef1 == null) {
      try {
        this.ivjBillTypeRef1 = new BillTypeRef();
        this.ivjBillTypeRef1.setName("BillTypeRef1");
        this.ivjBillTypeRef1.setButtonFireEvent(true);
        this.ivjBillTypeRef1.setBounds(83, 100, 122, 22);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjBillTypeRef1;
  }

  private UICheckBox getUICheckBox1()
  {
    if (this.ivjUICheckBox1 == null) {
      try {
        this.ivjUICheckBox1 = new UICheckBox();
        this.ivjUICheckBox1.setName("UICheckBox1");
        this.ivjUICheckBox1.setText(NCLangRes.getInstance().getStrByID("20148010", "UPP20148010-000008"));

        this.ivjUICheckBox1.setBounds(13, 224, 197, 22);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUICheckBox1;
  }

  private UILabel getUILabel111()
  {
    if (this.ivjUILabel111 == null) {
      try {
        this.ivjUILabel111 = new UILabel();
        this.ivjUILabel111.setName("UILabel111");
        this.ivjUILabel111.setText(NCLangRes.getInstance().getStrByID("common", "UC000-0003365"));

        this.ivjUILabel111.setBounds(263, 56, 19, 22);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUILabel111;
  }

  private UILabel getUILabel1111()
  {
    if (this.ivjUILabel1111 == null) {
      try {
        this.ivjUILabel1111 = new UILabel();
        this.ivjUILabel1111.setName("UILabel1111");
        this.ivjUILabel1111.setText(NCLangRes.getInstance().getStrByID("common", "UC000-0003365"));

        this.ivjUILabel1111.setBounds(210, 77, 23, 22);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUILabel1111;
  }

  private UILabel getUILabel112()
  {
    if (this.ivjUILabel112 == null) {
      try {
        this.ivjUILabel112 = new UILabel();
        this.ivjUILabel112.setName("UILabel112");
        this.ivjUILabel112.setText(NCLangRes.getInstance().getStrByID("common", "UC000-0003365"));

        this.ivjUILabel112.setBounds(210, 30, 23, 22);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUILabel112;
  }

  public UIRefPane getUIRefBillDate1()
  {
    if (this.ivjUIRefBillDate1 == null) {
      try {
        this.ivjUIRefBillDate1 = new UIRefPane();
        this.ivjUIRefBillDate1.setName("UIRefBillDate1");
        this.ivjUIRefBillDate1.setButtonFireEvent(false);
        this.ivjUIRefBillDate1.setLocation(83, 30);
        this.ivjUIRefBillDate1.setRefNodeName("日历");
        this.ivjUIRefBillDate1.setRefInputType(3);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUIRefBillDate1;
  }

  public UIRefPane getUIRefBillDate2()
  {
    if (this.ivjUIRefBillDate2 == null) {
      try {
        this.ivjUIRefBillDate2 = new UIRefPane();
        this.ivjUIRefBillDate2.setName("UIRefBillDate2");
        this.ivjUIRefBillDate2.setButtonFireEvent(false);
        this.ivjUIRefBillDate2.setLocation(238, 30);
        this.ivjUIRefBillDate2.setRefNodeName("日历");
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUIRefBillDate2;
  }

  public UIRefPane getUIRefCrdcenterid()
  {
    if (this.ivjUIRefCrdcenterid == null) {
      try {
        this.ivjUIRefCrdcenterid = new UIRefPane();
        this.ivjUIRefCrdcenterid.setName("UIRefCrdcenterid");
        this.ivjUIRefCrdcenterid.setButtonFireEvent(true);
        this.ivjUIRefCrdcenterid.setLocation(83, 124);
        this.ivjUIRefCrdcenterid.setRefNodeName("库存组织");
        this.ivjUIRefCrdcenterid.setReturnCode(false);

        this.ivjUIRefCrdcenterid.getRefModel().setSealedDataShow(true);

        this.ivjUIRefCrdcenterid.getRefModel().addWherePart(" and property in (0,2)");
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUIRefCrdcenterid;
  }

  public UIRefPane getUIRefCustname()
  {
    if (this.ivjUIRefCustname == null) {
      try {
        this.ivjUIRefCustname = new UIRefPane();
        this.ivjUIRefCustname.setName("UIRefCustname");
        this.ivjUIRefCustname.setButtonFireEvent(true);
        this.ivjUIRefCustname.setLocation(83, 148);
        this.ivjUIRefCustname.setRefNodeName("客户档案");
        this.ivjUIRefCustname.setReturnCode(false);

        this.ivjUIRefCustname.getRefModel().setSealedDataShow(true);
        this.ivjUIRefCustname.getRef().setPk_corp(this.ce.getCorporationID());
        this.ivjUIRefCustname.getRef().getRefModel().setPk_corp(this.ce.getCorporationID());
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUIRefCustname;
  }

  public UIRefPane getUIRefCwarehouseid()
  {
    if (this.ivjUIRefCwarehouseid == null) {
      try {
        this.ivjUIRefCwarehouseid = new UIRefPane();
        this.ivjUIRefCwarehouseid.setName("UIRefCwarehouseid");
        this.ivjUIRefCwarehouseid.setButtonFireEvent(true);
        this.ivjUIRefCwarehouseid.setLocation(298, 125);
        this.ivjUIRefCwarehouseid.setRefNodeName("仓库档案");
        this.ivjUIRefCwarehouseid.setReturnCode(false);

        this.ivjUIRefCwarehouseid.getRefModel().setSealedDataShow(true);

        String sWhere = this.ivjUIRefCwarehouseid.getRefModel().getWherePart();
        if ((sWhere != null) && (sWhere.length() > 0)) {
          sWhere = sWhere + " and iscalculatedinvcost = 'Y'";
        }
        this.ivjUIRefCwarehouseid.getRefModel().setWherePart(sWhere);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUIRefCwarehouseid;
  }

  public UIRefPane getUIRefInventoryid1()
  {
    if (this.ivjUIRefInventoryid1 == null) {
      try {
        this.ivjUIRefInventoryid1 = new UIRefPane();
        this.ivjUIRefInventoryid1.setName("UIRefInventoryid1");
        this.ivjUIRefInventoryid1.setButtonFireEvent(true);
        this.ivjUIRefInventoryid1.setLocation(83, 76);
        this.ivjUIRefInventoryid1.setRefNodeName("存货档案");
        this.ivjUIRefInventoryid1.setReturnCode(false);

        this.ivjUIRefInventoryid1.getRefModel().setSealedDataShow(true);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUIRefInventoryid1;
  }

  public UIRefPane getUIRefInventoryid2()
  {
    if (this.ivjUIRefInventoryid2 == null) {
      try {
        this.ivjUIRefInventoryid2 = new UIRefPane();
        this.ivjUIRefInventoryid2.setName("UIRefInventoryid2");
        this.ivjUIRefInventoryid2.setButtonFireEvent(true);
        this.ivjUIRefInventoryid2.setLocation(238, 76);
        this.ivjUIRefInventoryid2.setRefNodeName("存货档案");
        this.ivjUIRefInventoryid2.setReturnCode(false);

        this.ivjUIRefInventoryid2.getRefModel().setSealedDataShow(true);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUIRefInventoryid2;
  }

  private UIRefPane getUIRefOperatorname()
  {
    if (this.ivjUIRefOperatorname == null) {
      try {
        this.ivjUIRefOperatorname = new UIRefPane();
        this.ivjUIRefOperatorname.setName("UIRefOperatorname");

        this.ivjUIRefOperatorname.setRefNodeName("权限操作员");

        this.ivjUIRefOperatorname.getRefModel().setSealedDataShow(true);
        this.ivjUIRefOperatorname.setLocation(83, 196);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUIRefOperatorname;
  }

  private UITextField getUITextBillCode1()
  {
    if (this.ivjUITextBillCode1 == null) {
      try {
        this.ivjUITextBillCode1 = new UITextField();
        this.ivjUITextBillCode1.setName("UITextBillCode1");
        this.ivjUITextBillCode1.setBounds(83, 54, 174, 20);
        this.ivjUITextBillCode1.setMaxLength(30);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUITextBillCode1;
  }

  private UITextField getUITextBillCode2()
  {
    if (this.ivjUITextBillCode2 == null) {
      try {
        this.ivjUITextBillCode2 = new UITextField();
        this.ivjUITextBillCode2.setName("UITextBillCode2");
        this.ivjUITextBillCode2.setBounds(288, 54, 174, 20);
        this.ivjUITextBillCode2.setMaxLength(30);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUITextBillCode2;
  }

  private void handleException(Throwable exception)
  {
    Log.error(exception);
  }

  private void initialize()
  {
    try
    {
      setName("MyNormalPanel");

      setSize(476, 260);
      double[][] size = { { 10.0D, -1.0D, 0.0D }, { 10.0D, -2.0D, 10.0D } };

      TableLayout layout = new TableLayout(size);
      setLayout(layout);
      this.panel = getMainPanel();
      add(this.panel, "1,1");
    }
    catch (Throwable ivjExc) {
      handleException(ivjExc);
    }
  }

  private UIPanel getMainPanel()
  {
    double[][] size = { { -2.0D, 10.0D, -1.0D, 10.0D, -2.0D, 10.0D, -1.0D }, { -2.0D, 4.0D, -2.0D, 4.0D, -2.0D, 4.0D, -2.0D, 4.0D, -2.0D, 4.0D, -2.0D, 4.0D, -2.0D, 4.0D, -2.0D, 4.0D, -2.0D, 4.0D, -2.0D } };

    TableLayout layout = new TableLayout(size);
    UIPanel m_panel = new UIPanel();

    m_panel.setLayout(layout);

    m_panel.add(getUILabelBillDate(), "0,2");
    m_panel.add(getUIRefBillDate1(), "2,2");
    m_panel.add(getUILabel112(), "4,2");
    m_panel.add(getUIRefBillDate2(), "6,2");

    m_panel.add(getUILabelBillcode(), "0,4");
    m_panel.add(getUITextBillCode1(), "2,4");
    m_panel.add(getUILabel111(), "4,4");
    m_panel.add(getUITextBillCode2(), "6,4");

    m_panel.add(getUILabelInv(), "0,6");
    m_panel.add(getUIRefInventoryid1(), "2,6");
    m_panel.add(getUILabel1111(), "4,6");
    m_panel.add(getUIRefInventoryid2(), "6,6");

    m_panel.add(getUILabelBilltype(), "0,8");
    m_panel.add(getBillTypeRef1(), "2,8");
    m_panel.add(getUILabelInvclass(), "4,8");
    m_panel.add(getUIRefInvClass1(), "6,8");

    m_panel.add(getUILabelCalbody(), "0,10");
    m_panel.add(getUIRefCrdcenterid(), "2,10");
    m_panel.add(getUILabelWarehouse(), "4,10");
    m_panel.add(getUIRefCwarehouseid(), "6,10");

    m_panel.add(getUILabelCustom(), "0,12");
    m_panel.add(getUIRefCustname(), "2,12");
    m_panel.add(getUILabelVendor(), "4,12");
    m_panel.add(getUIRefVendor(), "6,12");

    m_panel.add(getUILabelDept(), "0,14");
    m_panel.add(getUIRefDept1(), "2,14");
    m_panel.add(getUILabel11111(), "4,14");
    m_panel.add(getUIRefDept2(), "6,14");

    m_panel.add(getUILabelOperator(), "0,16");
    m_panel.add(getUIRefOperatorname(), "2,16");

    m_panel.add(getUICheckBox1(), "0,18,4,18");
    return m_panel;
  }

  public String makeNormalStr()
  {
    String normalStr = "";

    if ((getBillTypeRef1().getText() != null) && (getBillTypeRef1().getText().length() > 0))
    {
      normalStr = normalStr + (normalStr.length() > 0 ? " and " : "") + "a.cbilltypecode='" + getBillTypeRef1().getRefCode() + "'";
    }

    String vbillcode1 = getUITextBillCode1().getText();
    String vbillcode2 = getUITextBillCode2().getText();
    if ((vbillcode1 != null) && (vbillcode1.length() > 0) && (vbillcode2 != null) && (vbillcode2.length() > 0))
    {
      normalStr = normalStr + (normalStr.length() > 0 ? " and " : "") + "a.vbillcode>='" + (vbillcode1.compareTo(vbillcode2) > 0 ? vbillcode2 : vbillcode1) + "' and a.vbillcode<='" + (vbillcode1.compareTo(vbillcode2) > 0 ? vbillcode1 : vbillcode2) + "'";
    }
    else if ((vbillcode1 != null) && (vbillcode1.length() > 0)) {
      normalStr = normalStr + (normalStr.length() > 0 ? " and " : "") + " a.vbillcode>='" + vbillcode1 + "'";
    }
    else if ((vbillcode2 != null) && (vbillcode2.length() > 0)) {
      normalStr = normalStr + (normalStr.length() > 0 ? " and " : "") + " a.vbillcode<='" + vbillcode2 + "'";
    }

    if ((getUIRefCrdcenterid().getText() != null) && (getUIRefCrdcenterid().getText().length() > 0))
    {
      normalStr = normalStr + (normalStr.length() > 0 ? " and " : "") + "a.crdcenterid='" + getUIRefCrdcenterid().getRefPK() + "'";
    }

    if ((getUIRefCwarehouseid().getText() != null) && (getUIRefCwarehouseid().getText().length() > 0))
    {
      normalStr = normalStr + (normalStr.length() > 0 ? " and " : "") + "a.cwarehouseid='" + getUIRefCwarehouseid().getRefPK() + "'";
    }

    if ((getUIRefCustname().getText() != null) && (getUIRefCustname().getText().length() > 0) && (getUIRefVendor().getText() != null) && (getUIRefVendor().getText().length() > 0))
    {
      normalStr = normalStr + (normalStr.length() > 0 ? " and " : "") + "a.ccustomvendorid in ('" + getUIRefCustname().getRefPK() + "','" + getUIRefVendor().getRefPK() + "')";

      normalStr = normalStr + " and f.custflag in ('0','1','2','3')";
    }
    else if ((getUIRefCustname().getText() != null) && (getUIRefCustname().getText().length() > 0))
    {
      normalStr = normalStr + (normalStr.length() > 0 ? " and " : "") + "a.ccustomvendorid='" + getUIRefCustname().getRefPK() + "'";

      normalStr = normalStr + " and f.custflag in ('0','2')";
    }
    else if ((getUIRefVendor().getText() != null) && (getUIRefVendor().getText().length() > 0))
    {
      normalStr = normalStr + (normalStr.length() > 0 ? " and " : "") + "a.ccustomvendorid='" + getUIRefVendor().getRefPK() + "'";

      normalStr = normalStr + " and f.custflag in ('1','3')";
    }

    String cdept1 = getUIRefDept1().getRefCode();
    String cdept2 = getUIRefDept2().getRefCode();
    if ((cdept1 != null) && (cdept1.length() > 0) && (cdept2 != null) && (cdept2.length() > 0))
    {
      if (cdept1.compareToIgnoreCase(cdept2) > 0) {
        normalStr = normalStr + (normalStr.length() > 0 ? " and " : "") + "h.deptcode between '" + cdept2 + "' and '" + cdept1 + "'";
      }
      else
      {
        normalStr = normalStr + (normalStr.length() > 0 ? " and " : "") + "h.deptcode between '" + cdept1 + "' and '" + cdept2 + "'";
      }

    }
    else if ((cdept1 != null) && (cdept1.length() > 0)) {
      normalStr = normalStr + (normalStr.length() > 0 ? " and " : "") + "h.deptcode>='" + cdept1 + "'";
    }
    else if ((cdept2 != null) && (cdept2.length() > 0)) {
      normalStr = normalStr + (normalStr.length() > 0 ? " and " : "") + "h.deptcode<='" + cdept2 + "'";
    }

    String invclass = getUIRefInvClass1().getRefCode();
    if ((invclass != null) && (invclass.length() > 0)) {
      normalStr = normalStr + (normalStr.length() > 0 ? " and " : "") + "cl.invclasscode like '" + invclass + "%' ";
    }

    String inventoryid1 = getUIRefInventoryid1().getRefCode();
    String inventoryid2 = getUIRefInventoryid2().getRefCode();
    if ((inventoryid1 != null) && (inventoryid1.length() > 0) && (inventoryid2 != null) && (inventoryid2.length() > 0))
    {
      if (inventoryid1.compareToIgnoreCase(inventoryid2) > 0) {
        normalStr = normalStr + (normalStr.length() > 0 ? " and " : "") + "k.invcode between '" + inventoryid2 + "' and '" + inventoryid1 + "'";
      }
      else
      {
        normalStr = normalStr + (normalStr.length() > 0 ? " and " : "") + "k.invcode between '" + inventoryid1 + "' and '" + inventoryid2 + "'";
      }

    }
    else if ((inventoryid1 != null) && (inventoryid1.length() > 0)) {
      normalStr = normalStr + (normalStr.length() > 0 ? " and " : "") + "k.invcode>='" + inventoryid1 + "'";
    }
    else if ((inventoryid2 != null) && (inventoryid2.length() > 0)) {
      normalStr = normalStr + (normalStr.length() > 0 ? " and " : "") + "k.invcode<='" + inventoryid2 + "'";
    }

    if ((getUIRefOperatorname().getText() != null) && (getUIRefOperatorname().getText().length() > 0))
    {
      normalStr = normalStr + (normalStr.length() > 0 ? " and " : "") + "a.coperatorid='" + getUIRefOperatorname().getRefPK() + "'";
    }

    if (!getUICheckBox1().isSelected()) {
      normalStr = normalStr + (normalStr.length() > 0 ? " and " : "") + "a.iauditsequence > 0";
    }

    return normalStr;
  }

  private UILabel getUILabel11111()
  {
    if (this.ivjUILabel11111 == null) {
      try {
        this.ivjUILabel11111 = new UILabel();
        this.ivjUILabel11111.setName("UILabel1111");
        this.ivjUILabel11111.setText(NCLangRes.getInstance().getStrByID("common", "UC000-0003365"));

        this.ivjUILabel11111.setBounds(210, 172, 23, 22);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUILabel11111;
  }

  private UILabel getUILabelBillcode()
  {
    if (this.ivjUILabel121 == null) {
      try {
        this.ivjUILabel121 = new UILabel();
        this.ivjUILabel121.setName("UILabelBillcode");
        this.ivjUILabel121.setText(NCLangRes.getInstance().getStrByID("common", "UC000-0000794"));

        this.ivjUILabel121.setBounds(10, 55, 60, 22);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUILabel121;
  }

  private UILabel getUILabelBillDate()
  {
    if (this.ivjUILabel1 == null) {
      try {
        this.ivjUILabel1 = new UILabel();
        this.ivjUILabel1.setName("UILabelBillDate");
        this.ivjUILabel1.setText(NCLangRes.getInstance().getStrByID("common", "UC000-0002313"));

        this.ivjUILabel1.setBounds(10, 30, 60, 22);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUILabel1;
  }

  private UILabel getUILabelBilltype()
  {
    if (this.ivjUILabel12 == null) {
      try {
        this.ivjUILabel12 = new UILabel();
        this.ivjUILabel12.setName("UILabelBilltype");
        this.ivjUILabel12.setText(NCLangRes.getInstance().getStrByID("common", "UC000-0000807"));

        this.ivjUILabel12.setBounds(10, 100, 65, 22);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUILabel12;
  }

  private UILabel getUILabelCalbody()
  {
    if (this.ivjUILabel1211 == null) {
      try {
        this.ivjUILabel1211 = new UILabel();
        this.ivjUILabel1211.setName("UILabelCalbody");
        this.ivjUILabel1211.setText(NCLangRes.getInstance().getStrByID("common", "UC000-0001825"));

        this.ivjUILabel1211.setBounds(10, 124, 60, 22);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUILabel1211;
  }

  private UILabel getUILabelCustom()
  {
    if (this.ivjUILabel121111 == null) {
      try {
        this.ivjUILabel121111 = new UILabel();
        this.ivjUILabel121111.setName("UILabelCustom");
        this.ivjUILabel121111.setText(NCLangRes.getInstance().getStrByID("common", "UC000-0001589"));

        this.ivjUILabel121111.setBounds(10, 147, 60, 22);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUILabel121111;
  }

  private UILabel getUILabelDept()
  {
    if (this.ivjUILabel121112 == null) {
      try {
        this.ivjUILabel121112 = new UILabel();
        this.ivjUILabel121112.setName("UILabelDept");
        this.ivjUILabel121112.setText(NCLangRes.getInstance().getStrByID("common", "UC000-0004064"));

        this.ivjUILabel121112.setBounds(10, 170, 60, 22);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUILabel121112;
  }

  private UILabel getUILabelInv()
  {
    if (this.ivjUILabel1212 == null) {
      try {
        this.ivjUILabel1212 = new UILabel();
        this.ivjUILabel1212.setName("UILabelInv");
        this.ivjUILabel1212.setText(NCLangRes.getInstance().getStrByID("common", "UC000-0001439"));

        this.ivjUILabel1212.setBounds(10, 76, 60, 22);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUILabel1212;
  }

  private UILabel getUILabelInvclass()
  {
    if (this.ivjUILabelinvclass == null) {
      try {
        this.ivjUILabelinvclass = new UILabel();
        this.ivjUILabelinvclass.setName("UILabelInvclass");
        this.ivjUILabelinvclass.setText(NCLangRes.getInstance().getStrByID("common", "UC000-0001443"));

        this.ivjUILabelinvclass.setBounds(226, 100, 60, 22);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUILabelinvclass;
  }

  private UILabel getUILabelOperator()
  {
    if (this.ivjUILabel1211121 == null) {
      try {
        this.ivjUILabel1211121 = new UILabel();
        this.ivjUILabel1211121.setName("UILabelOperator");
        this.ivjUILabel1211121.setText(NCLangRes.getInstance().getStrByID("common", "UC000-0000661"));

        this.ivjUILabel1211121.setBounds(10, 193, 60, 22);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUILabel1211121;
  }

  private UILabel getUILabelVendor()
  {
    if (this.ivjUILabelVendor == null) {
      try {
        this.ivjUILabelVendor = new UILabel();
        this.ivjUILabelVendor.setName("UILabelVendor");
        this.ivjUILabelVendor.setText(NCLangRes.getInstance().getStrByID("common", "UC000-0000275"));

        this.ivjUILabelVendor.setBounds(226, 147, 60, 22);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUILabelVendor;
  }

  private UILabel getUILabelWarehouse()
  {
    if (this.ivjUILabel12111 == null) {
      try {
        this.ivjUILabel12111 = new UILabel();
        this.ivjUILabel12111.setName("UILabelWarehouse");
        this.ivjUILabel12111.setText(NCLangRes.getInstance().getStrByID("common", "UC000-0000157"));

        this.ivjUILabel12111.setBounds(226, 124, 60, 22);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUILabel12111;
  }

  public UIRefPane getUIRefDept1()
  {
    if (this.ivjUIRefDept1 == null) {
      try {
        this.ivjUIRefDept1 = new UIRefPane();
        this.ivjUIRefDept1.setName("UIRefDept1");
        this.ivjUIRefDept1.setButtonFireEvent(true);
        this.ivjUIRefDept1.setLocation(83, 172);
        this.ivjUIRefDept1.setRefNodeName("部门档案");
        this.ivjUIRefDept1.setReturnCode(false);

        this.ivjUIRefDept1.getRefModel().setSealedDataShow(true);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUIRefDept1;
  }

  public UIRefPane getUIRefDept2()
  {
    if (this.ivjUIRefDept2 == null) {
      try {
        this.ivjUIRefDept2 = new UIRefPane();
        this.ivjUIRefDept2.setName("UIRefDept1");
        this.ivjUIRefDept2.setButtonFireEvent(true);
        this.ivjUIRefDept2.setLocation(238, 172);
        this.ivjUIRefDept2.setRefNodeName("部门档案");
        this.ivjUIRefDept2.setReturnCode(false);

        this.ivjUIRefDept2.getRefModel().setSealedDataShow(true);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUIRefDept2;
  }

  private UIRefPane getUIRefInvClass1()
  {
    if (this.ivjUIRefInvClass1 == null) {
      try {
        this.ivjUIRefInvClass1 = new UIRefPane();
        this.ivjUIRefInvClass1.setName("UIRefInvClass1");
        this.ivjUIRefInvClass1.setButtonFireEvent(true);
        this.ivjUIRefInvClass1.setBounds(298, 100, 125, 22);
        this.ivjUIRefInvClass1.setRefNodeName("存货分类");

        this.ivjUIRefInvClass1.getRefModel().setSealedDataShow(true);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUIRefInvClass1;
  }

  public UIRefPane getUIRefVendor()
  {
    if (this.ivjUIRefVendor == null) {
      try {
        this.ivjUIRefVendor = new UIRefPane();
        this.ivjUIRefVendor.setName("UIRefVendor");
        this.ivjUIRefVendor.setButtonFireEvent(true);
        this.ivjUIRefVendor.setRefNodeName("供应商档案");

        this.ivjUIRefVendor.getRefModel().setSealedDataShow(true);
        this.ivjUIRefVendor.setLocation(298, 149);
        this.ivjUIRefVendor.getRef().setPk_corp(this.ce.getCorporationID());
        this.ivjUIRefVendor.getRef().getRefModel().setPk_corp(this.ce.getCorporationID());
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUIRefVendor;
  }

  public void changeCustomAndVender(String[] corps)
  {
    if ((corps == null) || (corps.length == 0)) {
      corps = new String[] { getUIRefVendor().getRef().getPk_corp() };
    }

    if (corps.length > 1)
    {
      getUIRefVendor().setEnabled(false);
      getUIRefCustname().setEnabled(false);
    }
    else if (corps.length == 1)
    {
      getUIRefCustname().setEnabled(true);
      getUIRefCustname().getRef().setPk_corp(corps[0]);
      getUIRefCustname().getRef().getRefModel().setPk_corp(corps[0]);

      getUIRefVendor().setEnabled(true);
      getUIRefVendor().getRef().setPk_corp(corps[0]);
      getUIRefVendor().getRef().getRefModel().setPk_corp(corps[0]);
    }
  }
}