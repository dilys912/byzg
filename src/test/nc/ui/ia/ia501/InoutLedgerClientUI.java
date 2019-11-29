package nc.ui.ia.ia501;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.util.Vector;
import nc.ui.ia.analyze.IaAnalyzeClientUI;
import nc.ui.ia.bill.IABillCardPanel;
import nc.ui.ia.pub.IADefSetTool;
import nc.ui.ia.pub.IAEnvironment;
import nc.ui.ia.pub.sqlparse.PerviewManager;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.report.ReportBaseClass;
import nc.vo.ia.analyze.QueryVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.query.RefResultVO;
import nc.vo.sm.UserVO;

public class InoutLedgerClientUI extends IaAnalyzeClientUI
  implements BillEditListener
{
  private static final long serialVersionUID = 1L;
  private InoutLedgerPanel ivjthisPanel = null;
  private ButtonObject m_bButtonQuery = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000006"), NCLangRes.getInstance().getStrByID("20148010", "UPP20148010-000000"), 2, "查询");

  private ButtonObject m_bButtonFormat = new ButtonObject(NCLangRes.getInstance().getStrByID("20148010", "UPT20148010-000006"), NCLangRes.getInstance().getStrByID("20148010", "UPP20148010-000001"), 2, "格式");

  private ButtonObject m_bButtonBill = new ButtonObject(NCLangRes.getInstance().getStrByID("20148010", "UPT20148010-000007"), NCLangRes.getInstance().getStrByID("20148010", "UPP20148010-000002"), 2, "单据联查");

  private ButtonObject m_bButtonReturn = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000038"), NCLangRes.getInstance().getStrByID("20148010", "UPP20148010-000003"), 2, "返回");

  private ButtonObject m_bButtonRefresh = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000009"), NCLangRes.getInstance().getStrByID("20148010", "UPP20148010-000004"), 2, "刷新");

  private String m_sTitle = NCLangRes.getInstance().getStrByID("20148010", "UPP20148010-000005");

  private String m_sOldCbillid = null;

  private QueryConditionPanel qdialog = null;

  private IABillCardPanel ivjBillCardPanel = null;

  private String whereStr = "";

  private ButtonObject[] m_aryButtonGroup = { this.m_bButtonQuery, this.m_bButtonFormat, this.m_btnSort, this.m_btnSubTotal, this.m_btnFilter, this.m_btnColumnSet, this.m_bButtonBill, this.m_bButtonReturn, this.m_btnPrint, this.m_bButtonRefresh };

  private int m_nCurrentFormat = 5;

  public InoutLedgerClientUI()
  {
    initialize();
  }

  protected ReportBaseClass getReportPanel()
  {
    return getthisPanel();
  }

  public void afterEdit(BillEditEvent e)
  {
  }

  public void bodyRowChange(BillEditEvent e)
  {
    int nRow = e.getRow();
    Object oTemp = getthisPanel().getTableModel().getValueAt(nRow, 0);
    if ((oTemp == null) || (oTemp.toString().indexOf(NCLangRes.getInstance().getStrByID("20148010", "UPT20148010-000013")) != -1) || (oTemp.toString().indexOf(NCLangRes.getInstance().getStrByID("common", "UC000-0001146")) != -1))
    {
      this.m_bButtonBill.setEnabled(false);
    }
    else {
      this.m_bButtonBill.setEnabled(nRow >= 0);
    }
    updateButton(this.m_bButtonBill);
  }

  public void formatColumn(int nFormat)
  {
    getthisPanel().showHiddenColumn("ninnum");
    getthisPanel().showHiddenColumn("ninprice");
    getthisPanel().showHiddenColumn("ninmny");
    getthisPanel().showHiddenColumn("noutnum");
    getthisPanel().showHiddenColumn("noutprice");
    getthisPanel().showHiddenColumn("noutmny");
    getthisPanel().showHiddenColumn("inassistnum");
    getthisPanel().showHiddenColumn("outassistnum");

    ReportBaseClass report = getthisPanel();

    if ((this.m_nCurrentFormat == 6) || (nFormat == 6)) {
      int priceColumn = getthisPanel().getBodyColByKey("ninprice");
      int numColumn = getthisPanel().getBodyColByKey("ninnum");
      exchangeColumn(report, priceColumn, numColumn);

      priceColumn = getthisPanel().getBodyColByKey("noutprice");
      numColumn = getthisPanel().getBodyColByKey("noutnum");
      exchangeColumn(report, priceColumn, numColumn);
    }

    Vector vKeys = new Vector();
    switch (nFormat) {
    case 1:
      vKeys.addElement("ninprice");
      vKeys.addElement("ninmny");
      vKeys.addElement("noutprice");
      vKeys.addElement("noutmny");
      break;
    case 2:
      vKeys.addElement("ninprice");
      vKeys.addElement("ninnum");
      vKeys.addElement("noutprice");
      vKeys.addElement("noutnum");
      vKeys.addElement("inassistnum");
      vKeys.addElement("outassistnum");
      break;
    case 3:
      vKeys.addElement("ninmny");
      vKeys.addElement("noutmny");
      break;
    case 4:
      vKeys.addElement("ninprice");
      vKeys.addElement("noutprice");
      break;
    case 5:
      break;
    case 6:
    }

    if (vKeys.size() > 0) {
      String[] sKeys = new String[vKeys.size()];
      vKeys.copyInto(sKeys);
      getthisPanel().hideColumn(sKeys);
    }

    this.m_nCurrentFormat = nFormat;
  }

  private IABillCardPanel getBillCardPanel()
  {
    if (this.ivjBillCardPanel == null) {
      try {
        this.ivjBillCardPanel = new IABillCardPanel();
        this.ivjBillCardPanel.setName("BillCardPanel");
        this.ivjBillCardPanel.setVisible(false);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjBillCardPanel;
  }

  private InoutLedgerPanel getthisPanel()
  {
    if (this.ivjthisPanel == null) {
      try {
        this.ivjthisPanel = new InoutLedgerPanel();
        this.ivjthisPanel.setName("thisPanel");
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjthisPanel;
  }

  public String getTitle()
  {
    return this.m_sTitle;
  }

  public void handleException(Throwable exception)
  {
    exception.printStackTrace();
  }

  private void initialize()
  {
    try
    {
      this.m_bButtonQuery.setEnabled(true);
      this.m_btnColumnSet.setEnabled(true);
      this.m_btnFilter.setEnabled(true);
      this.m_bButtonBill.setEnabled(false);
      this.m_bButtonReturn.setVisible(false);
      this.m_btnPrint.setEnabled(false);
      this.m_bButtonRefresh.setEnabled(false);
      this.m_btnSort.setEnabled(false);
      this.m_btnSubTotal.setEnabled(false);
      setButtons(this.m_aryButtonGroup);

      this.m_sCorpID = this.ce.getCorporationID();

      setName("InoutLedgerClientUI");
      setLayout(new BorderLayout());
      setSize(774, 419);
      add(getthisPanel(), "Center");
      add(getBillCardPanel(), "Center");
    }
    catch (Throwable ivjExc) {
      handleException(ivjExc);
    }

    getthisPanel().addEditListener(this);
    setLayout(new CardLayout());
    this.m_sTitle = getthisPanel().getReportTitle();
    this.m_sVOName = "nc.vo.ia.ia501.IaInoutledgerPrintVO";

    showNOAndSum(true);

    IADefSetTool.updateBillUserDefForReportPanel(getthisPanel(), this.m_sCorpID, true, true);
  }

  protected String getNodeCode()
  {
    return "20148010";
  }

  public void onButtonClicked(ButtonObject bo)
  {
    showHintMessage(" ");

    if (bo == this.m_bButtonQuery) {
      onQuery();
    }
    else if (bo == this.m_bButtonBill) {
      onBill();
    }
    else if (bo == this.m_bButtonReturn) {
      onReturn();
    }
    else if (bo == this.m_bButtonRefresh) {
      onRefresh();
    }
    else if (bo == this.m_bButtonFormat) {
      onButtonFormat();
    }
    super.onButtonClicked(bo);
  }

  public void refreshButtons(boolean flag)
  {
    try
    {
      this.m_bButtonQuery.setEnabled(flag);
      this.m_btnColumnSet.setEnabled(flag);
      this.m_bButtonFormat.setEnabled(flag);
      this.m_btnFilter.setEnabled(flag);
      this.m_btnPrint.setEnabled(flag);
      this.m_bButtonRefresh.setEnabled(flag);
      this.m_bButtonBill.setEnabled(flag);
      this.m_btnSort.setEnabled(flag);
      this.m_btnSubTotal.setEnabled(flag);

      if (flag)
      {
        int nRow = getthisPanel().getBillTable().getSelectedRow();
        int nRowCount = getthisPanel().getBillTable().getRowCount();
        if (nRowCount == 0) {
          this.m_bButtonBill.setEnabled(false);
          this.m_btnPrint.setEnabled(false);
          this.m_btnSort.setEnabled(false);
          this.m_btnSubTotal.setEnabled(false);
        }
        else {
          Object oTemp = getthisPanel().getTableModel().getValueAt(nRow, 0);
          if ((oTemp == null) || (oTemp.toString().indexOf(NCLangRes.getInstance().getStrByID("20148010", "UPT20148010-000013")) != -1) || (oTemp.toString().indexOf(NCLangRes.getInstance().getStrByID("common", "UC000-0001146")) != -1))
          {
            this.m_bButtonBill.setEnabled(false);
          }
          else {
            this.m_bButtonBill.setEnabled(nRow >= 0);
          }
        }
      }

      updateButtons();
    }
    catch (Exception ivjExc) {
      ivjExc.printStackTrace();
    }
  }

  public void onBill()
  {
    try
    {
      if (getthisPanel().getBillTable().getSelectedRow() <= -1) {
        showHintMessage(NCLangRes.getInstance().getStrByID("20148010", "UPP20148010-000006"));

        return;
      }

      int iSelect = getthisPanel().getBillTable().getSelectedRow();

      Object oNewBillID = getthisPanel().getBillModel().getValueAt(iSelect, "cbillid");

      if (oNewBillID == null) {
        showHintMessage(NCLangRes.getInstance().getStrByID("20148010", "UPP20148010-000019"));

        return;
      }

      if ((oNewBillID != null) && ((this.m_sOldCbillid == null) || (!this.m_sOldCbillid.equals(oNewBillID.toString()))))
      {
        this.m_sOldCbillid = oNewBillID.toString();
        getBillCardPanel().queryBill((String)getthisPanel().getBillModel().getValueAt(iSelect, "corpid"), (String)getthisPanel().getBillModel().getValueAt(iSelect, "billtypecode"), this.m_sOldCbillid, "ia501");
      }

      String sTitle = getBillCardPanel().getTitle();
      setTitleText(sTitle);
      getBillCardPanel().setSize(getSize());
      getthisPanel().setVisible(false);
      getBillCardPanel().setVisible(true);

      refreshButtons(false);
      this.m_bButtonBill.setVisible(false);
      this.m_bButtonReturn.setVisible(true);
      this.m_bButtonReturn.setEnabled(true);
      this.m_bButtonReturn.setHint(NCLangRes.getInstance().getStrByID("20148010", "UPP20148010-000007"));

      setButtons(this.m_aryButtonGroup);

      showHintMessage(NCLangRes.getInstance().getStrByID("2014", "UPP2014-000644"));
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  public void onButtonFormat()
  {
    SetFormatDialog d = new SetFormatDialog(this);
    d.setSelectItem(this.m_nCurrentFormat);
    d.showModal();
    if (d.isCloseOK) {
      if (d.selectedFormat == this.m_nCurrentFormat) {
        return;
      }
      formatColumn(d.selectedFormat);
      this.m_dlgColSet = null;

      showHintMessage(NCLangRes.getInstance().getStrByID("2014", "UPP2014-000643"));
    }
  }

  public void onQuery()
  {
    try
    {
      if (this.qdialog == null) {
        this.qdialog = new QueryConditionPanel(this, "", this.ce.getUser().getPrimaryKey(), this.ce.getCorporationID());
        this.qdialog.hideUnitButton();
        this.qdialog.showModal();
      }
      else {
        this.qdialog.setVisible(true);
      }
      if (this.qdialog.isCloseOK())
      {
        String normalStr = this.qdialog.getNormalPanel().makeNormalStr();
        ConditionVO[] v = this.qdialog.getConditionVO();
        this.whereStr = "";
        for (int i = 0; i < v.length; i++) {
          if (this.whereStr.length() > 0) {
            this.whereStr += (v[i].getLogic() ? " and " : " or ");
          }
          this.whereStr += (v[i].getNoLeft() ? "" : "(");
          if ((v[i].getFieldCode().equalsIgnoreCase("ninnum")) || (v[i].getFieldCode().equalsIgnoreCase("ninprice")) || (v[i].getFieldCode().equalsIgnoreCase("ninmny")))
          {
            this.whereStr = (this.whereStr + "a.fdispatchflag=0 and " + (v[i].getFieldCode().equalsIgnoreCase("ninprice") ? "nprice" : v[i].getFieldCode().equalsIgnoreCase("ninnum") ? "nnumber" : "nmoney") + v[i].getOperaCode() + v[i].getValue());
          }
          else if ((v[i].getFieldCode().equalsIgnoreCase("noutnum")) || (v[i].getFieldCode().equalsIgnoreCase("noutprice")) || (v[i].getFieldCode().equalsIgnoreCase("noutmny")))
          {
            this.whereStr = (this.whereStr + "a.fdispatchflag=1 and " + (v[i].getFieldCode().equalsIgnoreCase("noutprice") ? "nprice" : v[i].getFieldCode().equalsIgnoreCase("noutnum") ? "nnumber" : "nmoney") + v[i].getOperaCode() + v[i].getValue());
          }
          else if ((v[i].getFieldCode().equalsIgnoreCase("a.cagentid")) || (v[i].getFieldCode().equalsIgnoreCase("a.cdispatchid")) || (v[i].getFieldCode().equalsIgnoreCase("a.cbiztypeid")) || (v[i].getFieldCode().equalsIgnoreCase("a.cprojectid")) || (v[i].getFieldCode().equalsIgnoreCase("a.cemployeeid")) || (v[i].getFieldCode().equalsIgnoreCase("l.pk_measdoc")) || (v[i].getFieldCode().equalsIgnoreCase("gl_voucher.pk_vouchertype")))
          {
            if (v[i].getOperaCode().equals("<>")) {
              this.whereStr += "(";
            }

            this.whereStr = (this.whereStr + v[i].getFieldCode() + v[i].getOperaCode() + "'" + v[i].getRefResult().getRefPK() + "'");

            if (v[i].getOperaCode().equals("<>"))
              this.whereStr = (this.whereStr + " or " + v[i].getFieldCode() + " is null) ");
          }
          else
          {
            if (v[i].getOperaCode().equals("<>")) {
              this.whereStr += "(";
            }

            this.whereStr = (this.whereStr + v[i].getFieldCode() + v[i].getOperaCode() + ((v[i].getDataType() == 1) || (v[i].getDataType() == 2) ? "" : "'") + v[i].getValue() + ((v[i].getDataType() == 1) || (v[i].getDataType() == 2) ? "" : "'"));

            if (v[i].getOperaCode().equals("<>")) {
              this.whereStr = (this.whereStr + " or " + v[i].getFieldCode() + " is null) ");
            }
          }
          this.whereStr += (v[i].getNoRight() ? "" : ")");
        }
        this.whereStr = (this.whereStr.length() > 0 ? "(" + this.whereStr + ")" : this.whereStr);
        if (normalStr.length() > 0) {
          this.whereStr = (this.whereStr + (this.whereStr.length() > 0 ? " and " : "") + "(" + normalStr + ")");
        }

        String[] ControlObject = { "仓库", "库存组织", "部门", "业务员", "存货", "经手人", "客商", "存货分类" };
        String[] TableNames = { "仓库档案", "库存组织", "部门档案", "人员档案", "存货档案", "人员档案", "客商档案", "存货分类" };

        String[] QueryCode = { "a.cwarehouseid", "a.crdcenterid", "h.deptcode", "a.cemployeeid", "k.invcode", "a.cagentid", "a.ccustomvendorid", "cl.invclasscode" };
        try
        {
          PerviewManager pm = new PerviewManager(ControlObject, TableNames, QueryCode);
          String dataPower = pm.getWhereSqlWithDataPower(this.whereStr, this.ce.getUser().getPrimaryKey(), this.qdialog.getMultiCorpIDs());

          dataPower = pm.addTableHead(dataPower, "仓库", "a");
          dataPower = pm.addTableHead(dataPower, "库存组织", "a");
          dataPower = pm.addTableHead(dataPower, "部门", "a");
          dataPower = pm.addTableHead(dataPower, "业务员", "a");
          dataPower = pm.addTableHead(dataPower, "存货", "a");
          dataPower = pm.addTableHead(dataPower, "经手人", "a");
          dataPower = pm.addTableHead(dataPower, "客商", "a");
          dataPower = pm.addTableHead(dataPower, "存货分类", "cl");

          if (dataPower.length() > 0)
          {
            if (this.whereStr.length() > 0) {
              this.whereStr += " and ";
            }
            this.whereStr += dataPower;
          }
        }
        catch (BusinessException e)
        {
          e.printStackTrace();
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }

        QueryVO query = new QueryVO();

        query.setWhere(this.whereStr);
        query.setDate(this.qdialog.getDates());
        query.setPk_Corps(this.qdialog.getMultiCorpIDs());

        getthisPanel().makeData(query);

        refreshButtons(true);
      }

    }
    catch (Exception ivjExc)
    {
      ivjExc.printStackTrace();
    }
  }

  public void onRefresh()
  {
    showHintMessage(NCLangRes.getInstance().getStrByID("2014", "UPP2014-000635"));
    try
    {
      QueryVO query = new QueryVO();
      query.setWhere(this.whereStr);
      query.setDate(this.qdialog.getDates());
      query.setPk_Corps(this.qdialog.getMultiCorpIDs());
      getthisPanel().makeData(query);

      refreshButtons(true);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void onReturn()
  {
    setTitleText(this.m_sTitle);
    getthisPanel().setVisible(true);
    getBillCardPanel().setVisible(false);

    refreshButtons(true);
    this.m_bButtonBill.setVisible(true);
    this.m_bButtonBill.setHint(NCLangRes.getInstance().getStrByID("20148010", "UPP20148010-000002"));

    this.m_bButtonReturn.setVisible(false);
    this.m_bButtonQuery.setEnabled(true);
    setButtons(this.m_aryButtonGroup);
  }
}