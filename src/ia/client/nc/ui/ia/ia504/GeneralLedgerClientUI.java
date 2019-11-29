package nc.ui.ia.ia504;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Hashtable;
import nc.ui.ia.analyze.IaAnalyzeClientUI;
import nc.ui.ia.ia502.QueryDetailPanel;
import nc.ui.ia.ia502.QueryDetailQueryDialog;
import nc.ui.ia.pub.CommonDataBO_Client;
import nc.ui.ia.pub.CorpRef;
import nc.ui.ia.pub.IAEnvironment;
import nc.ui.ia.pub.sqlparse.PerviewManager;
import nc.ui.ia.query.IAQueryBO_Client;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.report.ReportBaseClass;
import nc.vo.ia.ia504.GeneralLedgerPrintVO;
import nc.vo.ia.pub.Log;
import nc.vo.ia.query.IaQueryConditionVO;
import nc.vo.sm.UserVO;

public class GeneralLedgerClientUI extends IaAnalyzeClientUI
  implements ItemListener, Runnable, BillEditListener
{
  private static final long serialVersionUID = 1L;
  private ButtonObject m_bButtonQuery = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000006"), NCLangRes.getInstance().getStrByID("common", "UC001-0000006"), 2, "查询");

  private ButtonObject m_bButtonInvDetail = new ButtonObject(NCLangRes.getInstance().getStrByID("20148040", "UPT20148040-000003"), NCLangRes.getInstance().getStrByID("20148040", "UPP20148040-000071"), 2, "明细");

  private ButtonObject m_bButtonReturn = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000038"), NCLangRes.getInstance().getStrByID("20148040", "UPP20148040-000072"), 2, "返回");

  private ButtonObject m_bButtonExport = new ButtonObject(NCLangRes.getInstance().getStrByID("20148020", "UPT20148020-000018"), NCLangRes.getInstance().getStrByID("20148040", "UPP20148040-000073"), 2, "输出");

  private ButtonObject m_bButtonRefresh = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000009"), NCLangRes.getInstance().getStrByID("20148040", "UPP20148040-000074"), 2, "刷新");

  private ButtonObject[] m_aryButtonGroup = { this.m_bButtonQuery, this.m_bButtonInvDetail, this.m_bButtonReturn, this.m_btnColumnSet, this.m_btnPrint, this.m_bButtonExport, this.m_bButtonRefresh };

  private String m_sTitle = NCLangRes.getInstance().getStrByID("20148040", "UPP20148040-000075");

  private GeneralLedgerPanel ivjthisPanel = null;

  private QueryDetailPanel ivjqueryDetailPanel = null;

  private String m_sInvQuery = "";

  private QueryDetailQueryDialog qdialog = null;

  private boolean m_bProcessFlag = false;

  public GeneralLedgerClientUI()
  {
    initialize();
  }

  public void afterEdit(BillEditEvent e)
  {
  }

  public void bodyRowChange(BillEditEvent e)
  {
    if (e.getRow() >= getthisPanel().m_tUITable.getRowCount()) {
      this.m_bButtonInvDetail.setEnabled(false);
    }
    else if ((getthisPanel().m_voTableData[e.getRow()].getCaccountyear() == null) || (getthisPanel().m_voTableData[e.getRow()].getCaccountmonth() == null))
    {
      this.m_bButtonInvDetail.setEnabled(false);
    }
    else {
      this.m_bButtonInvDetail.setEnabled(e.getRow() >= 0);
    }

    setButtons(this.m_aryButtonGroup);
  }

  private QueryDetailPanel getqueryDetailPanel()
  {
    if (this.ivjqueryDetailPanel == null) {
      try {
        this.ivjqueryDetailPanel = new QueryDetailPanel();
        this.ivjqueryDetailPanel.setName("queryDetailPanel");
        this.ivjqueryDetailPanel.setVisible(false);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjqueryDetailPanel;
  }

  private GeneralLedgerPanel getthisPanel()
  {
    if (this.ivjthisPanel == null) {
      try {
        this.ivjthisPanel = new GeneralLedgerPanel();
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

  protected ReportBaseClass getReportPanel()
  {
    return getthisPanel();
  }

  public void handleException(Throwable exception)
  {
    Log.error(exception);
  }

  private void initialize()
  {
    try
    {
      setName("GeneralLedgerClientUI");
      setSize(774, 419);
      add(getqueryDetailPanel(), "Center");
      add(getthisPanel(), "Center");
    }
    catch (Throwable ivjExc) {
      handleException(ivjExc);
    }
    try
    {
      this.m_bButtonQuery.setEnabled(true);
      this.m_bButtonInvDetail.setEnabled(false);
      this.m_bButtonReturn.setVisible(false);
      this.m_btnPrint.setEnabled(false);
      this.m_bButtonExport.setEnabled(false);
      this.m_bButtonRefresh.setEnabled(false);

      this.m_bButtonExport.setVisible(false);

      setButtons(this.m_aryButtonGroup);

      getthisPanel().cb_crdcenterid.addItemListener(this);
      getthisPanel().cb_cinventoryid.addItemListener(this);
      getthisPanel().cb_vbatch.addItemListener(this);

      getthisPanel().addEditListener(this);
    }
    catch (Exception ivjExc) {
      ivjExc.printStackTrace();
    }

    this.m_sTitle = getthisPanel().getReportTitle();

    this.m_sVOName = "nc.vo.ia.ia504.GeneralLedgerPrintVO";
  }

  protected String getNodeCode()
  {
    return "20148040";
  }

  public void itemStateChanged(ItemEvent e)
  {
    boolean bVbatch = false;
    try {
      if (e.getStateChange() == 2) {
        return;
      }
      if (getthisPanel().m_binmakecblist) {
        return;
      }

      String sPk_corp = getthisPanel().m_spk_corp;
      UIComboBox cb = (UIComboBox)e.getSource();

      if (cb.getName().equals("crdcenterid")) {
        getthisPanel().cb_cinventoryid.removeAllItems();
        getthisPanel().cb_vbatch.removeAllItems();

        getthisPanel().m_binmakecblist = true;

        String strSql = "select a.invcode, a.invname, a.invspec, a.invtype, m.measname, p.pricemethod, p.pk_invmandoc,b.invclasscode,b.invclassname from bd_invbasdoc a inner join bd_measdoc m on a.pk_measdoc=m.pk_measdoc, bd_invcl b, bd_produce p where a.pk_invcl=b.pk_invcl" + ((this.m_sInvQuery.length() > 0) ? " and " : "") + this.m_sInvQuery + " and a.pk_invbasdoc = p.pk_invbasdoc" + " and p.pk_corp = '" + sPk_corp + "'" + " and p.pk_calbody = '" + getthisPanel().m_arycrdcenterid[getthisPanel().cb_crdcenterid.getSelectedIndex()] + "'" + " and p.pricemethod is not null ";

        String DataPowerForCinvclass = PerviewManager.getDataPowerForCinvclass(getthisPanel().m_spk_corp, this.ce.getUser().getPrimaryKey());

        String DataPowerForCinventory = PerviewManager.getDataPowerForCinventory(getthisPanel().m_spk_corp, this.ce.getUser().getPrimaryKey());

        if (DataPowerForCinvclass.length() > 0) {
          strSql = strSql + " and b.pk_invcl in (";
          strSql = strSql + DataPowerForCinvclass;
          strSql = strSql + ") ";
        }

        if (DataPowerForCinventory.length() > 0) {
          strSql = strSql + " and p.pk_invmandoc in (";
          strSql = strSql + DataPowerForCinventory;
          strSql = strSql + ") ";
        }

        strSql = strSql + " order by a.invcode";

        String[][] resultS = CommonDataBO_Client.queryData(strSql);
        getthisPanel().m_arycinvcode = new String[resultS.length];
        getthisPanel().m_aryspectype = new String[resultS.length];
        getthisPanel().m_arymeasname = new String[resultS.length];
        getthisPanel().m_arypricemethod = new int[resultS.length];
        getthisPanel().m_arycinventoryid = new String[resultS.length];
        getthisPanel().m_arycinvclid = new String[resultS.length];
        getthisPanel().m_arycinvclname = new String[resultS.length];

        for (int i = 0; i < resultS.length; ++i) {
          getthisPanel().m_arycinvcode[i] = resultS[i][0];
          getthisPanel().m_aryspectype[i] = resultS[i][2].trim() + "," + resultS[i][3].trim();

          getthisPanel().m_arymeasname[i] = resultS[i][4].trim();
          getthisPanel().cb_cinventoryid.addItem(resultS[i][0].trim() + "," + resultS[i][1].trim());

          getthisPanel().m_arypricemethod[i] = Integer.valueOf(resultS[i][5]).intValue();

          getthisPanel().m_arycinventoryid[i] = resultS[i][6];
          getthisPanel().m_arycinvclid[i] = resultS[i][7];
          getthisPanel().m_arycinvclname[i] = resultS[i][8];
        }
        if (resultS.length > 0) {
          getthisPanel().rp_spectype.setText(getthisPanel().m_aryspectype[getthisPanel().cb_cinventoryid.getSelectedIndex()]);

          getthisPanel().rp_measname.setText(getthisPanel().m_arymeasname[getthisPanel().cb_cinventoryid.getSelectedIndex()]);
        }

        if (getthisPanel().cb_cinventoryid.getItemCount() > 0)
        {
          String sCrdcenterid = getthisPanel().m_arycrdcenterid[getthisPanel().cb_crdcenterid.getSelectedIndex()];

          String sInventoryid = getthisPanel().m_arycinventoryid[getthisPanel().cb_cinventoryid.getSelectedIndex()];

          Object oTemp = this.ce.getPricingMode(sPk_corp, sCrdcenterid, new String[] { sInventoryid }).get(sInventoryid);

          if (oTemp != null) {
            String[] saResult = (String[])(String[])oTemp;

            if (saResult[1].equalsIgnoreCase("Y")) {
              bVbatch = true;
              String[] saVbatch = IAQueryBO_Client.getVbatch(sPk_corp, sCrdcenterid, sInventoryid);

              if (saVbatch != null) {
                getthisPanel().m_aryvbatch = new String[saVbatch.length];
                System.arraycopy(saVbatch, 0, getthisPanel().m_aryvbatch, 0, saVbatch.length);

                for (int i = 0; i < saVbatch.length; ++i)
                  getthisPanel().cb_vbatch.addItem(saVbatch[i]);
              }
            }
            else
            {
              bVbatch = false;
            }
          }
          else {
            bVbatch = false;
          }
        }
        else {
          bVbatch = false;
        }

        getthisPanel().m_bvbatch = bVbatch;
        getthisPanel().cb_vbatch.setEnabled(bVbatch);

        getthisPanel().m_binmakecblist = false;
      }
      else if (cb.getName().equals("cinventoryid")) {
        getthisPanel().m_binmakecblist = true;

        getthisPanel().cb_vbatch.removeAllItems();

        getthisPanel().rp_spectype.setText(getthisPanel().m_aryspectype[getthisPanel().cb_cinventoryid.getSelectedIndex()]);

        getthisPanel().rp_measname.setText(getthisPanel().m_arymeasname[getthisPanel().cb_cinventoryid.getSelectedIndex()]);

        String sCrdcenterid = getthisPanel().m_arycrdcenterid[getthisPanel().cb_crdcenterid.getSelectedIndex()];

        String sInventoryid = getthisPanel().m_arycinventoryid[getthisPanel().cb_cinventoryid.getSelectedIndex()];

        Object oTemp = this.ce.getPricingMode(sPk_corp, sCrdcenterid, new String[] { sInventoryid }).get(sInventoryid);

        if (oTemp != null) {
          String[] saResult = (String[])(String[])oTemp;

          if (saResult[1].equalsIgnoreCase("Y")) {
            bVbatch = true;
            String[] saVbatch = IAQueryBO_Client.getVbatch(sPk_corp, sCrdcenterid, sInventoryid);

            if (saVbatch != null) {
              getthisPanel().m_aryvbatch = new String[saVbatch.length];
              System.arraycopy(saVbatch, 0, getthisPanel().m_aryvbatch, 0, saVbatch.length);

              for (int i = 0; i < saVbatch.length; ++i)
                getthisPanel().cb_vbatch.addItem(saVbatch[i]);
            }
          }
          else
          {
            bVbatch = false;
          }
        }
        else {
          bVbatch = false;
        }

        getthisPanel().m_bvbatch = bVbatch;
        getthisPanel().cb_vbatch.setEnabled(bVbatch);

        getthisPanel().m_binmakecblist = false;
      }
      else if (cb.getName().equals("vbatch")) {
        bVbatch = true;
      }
      if ((getthisPanel().cb_crdcenterid.getItemCount() == 0) || (getthisPanel().cb_cinventoryid.getItemCount() == 0) || ((bVbatch) && (getthisPanel().cb_vbatch.getItemCount() == 0)))
      {
        showHintMessage(NCLangRes.getInstance().getStrByID("20148040", "UPP20148040-000076"));

        getthisPanel().rp_spectype.setText("");
        getthisPanel().rp_measname.setText("");
        getthisPanel().m_voTableData = new GeneralLedgerPrintVO[0];
        getthisPanel().setBodyDataVO(getthisPanel().m_voTableData);
        refreshButtons(true);
        return;
      }

      IaQueryConditionVO v = new IaQueryConditionVO();
      v.setpk_corp(getthisPanel().m_spk_corp);
      v.setaccperiod_start(getthisPanel().m_sAccperiod_start);
      v.setaccperiod_stop(getthisPanel().m_sAccperiod_stop);
      v.setcinvcode(getthisPanel().m_arycinvcode[getthisPanel().cb_cinventoryid.getSelectedIndex()]);

      v.setcrdcenterid(getthisPanel().m_arycrdcenterid[getthisPanel().cb_crdcenterid.getSelectedIndex()]);

      v.setincludenotaudit(getthisPanel().m_bIncludeNotAudit);
      v.setjhj(getthisPanel().m_arypricemethod[getthisPanel().cb_cinventoryid.getSelectedIndex()] == 5);

      v.setIPriceMethod(getthisPanel().m_arypricemethod[getthisPanel().cb_cinventoryid.getSelectedIndex()]);

      v.setsimulatemny(getthisPanel().m_bsimulatemny);
      if (bVbatch) {
        v.setvbatch(getthisPanel().m_aryvbatch[getthisPanel().cb_vbatch.getSelectedIndex()]);
      }

      Thread thread = new Thread(this);
      this.m_bProcessFlag = true;
      thread.start();

      getthisPanel().makeData(v);
      showHintMessage(NCLangRes.getInstance().getStrByID("20148020", "UPT20148020-000020"));

      refreshButtons(true);
    }
    catch (Exception ivjExc)
    {
      ivjExc.printStackTrace();
    }
    finally {
      this.m_bProcessFlag = false;
    }
  }

  public void onButtonClicked(ButtonObject bo)
  {
    showHintMessage("");

    if (bo == this.m_bButtonQuery) {
      onQuery();
    }
    else if (bo == this.m_bButtonInvDetail) {
      onButtonInvDetailClicked();
    }
    else if (bo == this.m_bButtonReturn) {
      onButtonReturnClicked();
    }
    else if (bo == this.m_bButtonRefresh) {
      onButtonRefreshClicked();
    }
    super.onButtonClicked(bo);
  }

  public void onButtonInvDetailClicked()
  {
    String sQueryYear = ""; String sQueryMonth = "";

    this.m_bButtonInvDetail.setVisible(false);
    this.m_bButtonReturn.setVisible(true);
    this.m_bButtonReturn.setEnabled(true);
    this.m_bButtonReturn.setHint(NCLangRes.getInstance().getStrByID("20148040", "UPP20148040-000077"));

    refreshButtons(false);
    updateButtons();

    setTitleText(NCLangRes.getInstance().getStrByID("20148040", "UPP20148040-000078"));

    sQueryYear = getthisPanel().m_voTableData[getthisPanel().m_tUITable.getSelectedRow()].getCaccountyear();

    sQueryMonth = getthisPanel().m_voTableData[getthisPanel().m_tUITable.getSelectedRow()].getCaccountmonth();

    IaQueryConditionVO condv = new IaQueryConditionVO();
    condv.setpk_corp(getthisPanel().m_spk_corp);
    condv.setcrdcenterid(getthisPanel().m_arycrdcenterid[getthisPanel().cb_crdcenterid.getSelectedIndex()]);

    condv.setaccperiod_start(sQueryYear + "-" + sQueryMonth);
    condv.setaccperiod_stop(sQueryYear + "-" + sQueryMonth);
    condv.setcinvcode(getthisPanel().m_arycinvcode[getthisPanel().cb_cinventoryid.getSelectedIndex()]);

    condv.setjhj(getthisPanel().m_bJHJ);
    condv.setincludenotaudit(getthisPanel().m_bIncludeNotAudit);
    condv.setsimulatemny(getthisPanel().m_bsimulatemny);
    if (getthisPanel().m_bvbatch) {
      condv.setvbatch(getthisPanel().m_aryvbatch[getthisPanel().cb_vbatch.getSelectedIndex()]);
    }

    String[] params = new String[8];
    params[0] = getthisPanel().rp_pk_corp.getText();
    params[1] = getthisPanel().cb_crdcenterid.getSelectedItem().toString();
    params[2] = getthisPanel().cb_cinventoryid.getSelectedItem().toString();
    params[3] = getthisPanel().rp_spectype.getText();
    params[4] = getthisPanel().rp_measname.getText();

    params[5] = ((getthisPanel().m_bvbatch) ? getthisPanel().m_aryvbatch[getthisPanel().cb_vbatch.getSelectedIndex()] : "");

    params[6] = getthisPanel().m_arycinvclid[getthisPanel().cb_cinventoryid.getSelectedIndex()];

    params[7] = getthisPanel().m_arycinvclname[getthisPanel().cb_cinventoryid.getSelectedIndex()];

    getqueryDetailPanel().m_bshow = true;
    getqueryDetailPanel().setTempletData(condv, params);

    getqueryDetailPanel().setSize(getSize());
    getthisPanel().setVisible(false);
    getqueryDetailPanel().setVisible(true);
  }

  public void onQuery()
  {
    try
    {
      boolean bSimulatemny = false;
      boolean bJHJ = false;
      boolean bIncludeNotAudit = false;
      boolean bVbatch = false;

      if (this.qdialog == null) {
        this.qdialog = new QueryDetailQueryDialog(this);

        this.qdialog.getUIRefPk_corp1_pub().setPK(this.ce.getCorporationID());
        String sQueryPeriod1 = CommonDataBO_Client.getStartPeriod(this.ce.getCorporationID());

        String sQueryPeriod2 = this.ce.getAccountPeriod();
        if (sQueryPeriod1.length() < 7) {
          sQueryPeriod1 = sQueryPeriod2;
        }
        if (sQueryPeriod1.substring(0, 4).compareTo(sQueryPeriod2.substring(0, 4)) < 0)
        {
          sQueryPeriod1 = sQueryPeriod2.substring(0, 4) + "-01";
        }
        this.qdialog.getUIRefAccperiod1_pub().setBlurValue(sQueryPeriod1);
        this.qdialog.getUIRefAccperiod2_pub().setBlurValue(sQueryPeriod2);
      }

      this.qdialog.showModal();
      if (this.qdialog.isCloseOK()) {
        getthisPanel().m_binmakecblist = true;

        getthisPanel().cb_crdcenterid.removeAllItems();
        getthisPanel().cb_cinventoryid.removeAllItems();
        getthisPanel().cb_vbatch.removeAllItems();

        getthisPanel().rp_measname.setText("");
        getthisPanel().rp_spectype.setText("");

        String strSql = "";

        String sPk_corp = this.qdialog.getUIRefPk_corp1_pub().getRefPK();
        if ((getthisPanel().m_spk_corp == null) || (!(getthisPanel().m_spk_corp.equals(this.qdialog.getUIRefPk_corp1_pub().getRefPK()))))
        {
          getthisPanel().m_spk_corp = sPk_corp;
          int[] nDataPrecision = this.ce.getDataPrecision(getthisPanel().m_spk_corp);
          getthisPanel().m_nNumberLength = nDataPrecision[0];
          getthisPanel().m_nPriceLength = nDataPrecision[1];
          getthisPanel().m_nMoneyLength = nDataPrecision[2];
          getthisPanel().setColumnDigit();
        }

        getthisPanel().m_spk_corp = sPk_corp;
        getthisPanel().rp_pk_corp.setText(this.qdialog.getUIRefPk_corp1_pub().getRefCode() + "," + this.qdialog.getUIRefPk_corp1_pub().getRefName());

        if (this.qdialog.getUIRefAccperiod1_pub().getRefName().compareTo(this.qdialog.getUIRefAccperiod2_pub().getRefName()) < 0)
        {
          getthisPanel().m_sAccperiod_start = this.qdialog.getUIRefAccperiod1_pub().getRefName();

          getthisPanel().m_sAccperiod_stop = this.qdialog.getUIRefAccperiod2_pub().getRefName();
        }
        else
        {
          getthisPanel().m_sAccperiod_start = this.qdialog.getUIRefAccperiod2_pub().getRefName();

          getthisPanel().m_sAccperiod_stop = this.qdialog.getUIRefAccperiod1_pub().getRefName();
        }

        getthisPanel().rp_accperiod.setText(getthisPanel().m_sAccperiod_start + "~" + getthisPanel().m_sAccperiod_stop);

        String crdcenterid1 = this.qdialog.getUIRefCrdcenterid1_pub().getRefCode();
        String crdcenterid2 = this.qdialog.getUIRefCrdcenterid2_pub().getRefCode();

        String DataPowerForCrdcenter = PerviewManager.getDataPowerForCrdcenter(sPk_corp, this.ce.getUser().getPrimaryKey());

        if ((crdcenterid1 != null) && (crdcenterid1.length() > 0) && (crdcenterid2 != null) && (crdcenterid2.length() > 0))
        {
          strSql = "select bodycode, bodyname, pk_calbody from bd_calbody a where a.bodycode between '" + this.qdialog.getUIRefCrdcenterid1_pub().getRefCode() + "' and '" + this.qdialog.getUIRefCrdcenterid2_pub().getRefCode() + "' and pk_corp = '" + sPk_corp + "' ";

          if (DataPowerForCrdcenter.length() > 0) {
            strSql = strSql + " and pk_calbody in (";
            strSql = strSql + DataPowerForCrdcenter;
            strSql = strSql + ") ";
          }

          strSql = strSql + " order by bodycode";
        }
        else if ((crdcenterid1 != null) && (crdcenterid1.length() > 0)) {
          strSql = "select bodycode, bodyname, pk_calbody from bd_calbody a where a.bodycode >='" + this.qdialog.getUIRefCrdcenterid1_pub().getRefCode() + "' and pk_corp = '" + sPk_corp + "' ";

          if (DataPowerForCrdcenter.length() > 0) {
            strSql = strSql + " and pk_calbody in (";
            strSql = strSql + DataPowerForCrdcenter;
            strSql = strSql + ") ";
          }

          strSql = strSql + " order by bodycode";
        }
        else if ((crdcenterid2 != null) && (crdcenterid2.length() > 0)) {
          strSql = "select bodycode, bodyname, pk_calbody from bd_calbody a where a.bodycode <='" + this.qdialog.getUIRefCrdcenterid2_pub().getRefCode() + "' and pk_corp = '" + sPk_corp + "' ";

          if (DataPowerForCrdcenter.length() > 0) {
            strSql = strSql + " and pk_calbody in (";
            strSql = strSql + DataPowerForCrdcenter;
            strSql = strSql + ") ";
          }

          strSql = strSql + " order by bodycode";
        }
        else {
          strSql = "select bodycode, bodyname, pk_calbody from bd_calbody where pk_corp = '" + sPk_corp + "' ";

          if (DataPowerForCrdcenter.length() > 0) {
            strSql = strSql + " and pk_calbody in (";
            strSql = strSql + DataPowerForCrdcenter;
            strSql = strSql + ") ";
          }

          strSql = strSql + " order by bodycode";
        }
        String[][] resultS = CommonDataBO_Client.queryData(strSql);
        getthisPanel().m_arycrdcenterid = new String[resultS.length];
        for (int i = 0; i < resultS.length; ++i) {
          getthisPanel().m_arycrdcenterid[i] = resultS[i][2];
          getthisPanel().cb_crdcenterid.addItem(resultS[i][0] + "," + resultS[i][1]);
        }

        this.m_sInvQuery = "";
        String invClass = this.qdialog.getUIRefInvClass1_pub().getRefCode();
        String condition_invclasscode = "";

        if ((invClass != null) && (invClass.length() > 0)) {
          condition_invclasscode = " b.invclasscode like '" + this.qdialog.getUIRefInvClass1_pub().getRefCode() + "%'";
        }

        String sInventoryID1 = this.qdialog.getUIRefInventoryid1_pub().getRefCode();
        String sInventoryID2 = this.qdialog.getUIRefInventoryid2_pub().getRefCode();
        String condition_inventory = "";
        if ((sInventoryID1 != null) && (sInventoryID1.length() > 0) && (sInventoryID2 != null) && (sInventoryID2.length() > 0))
        {
          if (sInventoryID1.compareToIgnoreCase(sInventoryID2) > 0) {
            condition_inventory = "a.invcode between '" + this.qdialog.getUIRefInventoryid2_pub().getRefCode() + "' and '" + this.qdialog.getUIRefInventoryid1_pub().getRefCode() + "'";
          }
          else
          {
            condition_inventory = "a.invcode between '" + this.qdialog.getUIRefInventoryid1_pub().getRefCode() + "' and '" + this.qdialog.getUIRefInventoryid2_pub().getRefCode() + "'";
          }

        }
        else if ((sInventoryID1 != null) && (sInventoryID1.length() > 0)) {
          condition_inventory = "a.invcode>='" + this.qdialog.getUIRefInventoryid1_pub().getRefCode() + "'";
        }
        else if ((sInventoryID2 != null) && (sInventoryID2.length() > 0)) {
          condition_inventory = "a.invcode<='" + this.qdialog.getUIRefInventoryid2_pub().getRefCode() + "'";
        }

        this.m_sInvQuery = condition_invclasscode + (((condition_invclasscode.length() > 0) && (condition_inventory.length() > 0)) ? " and " : "") + condition_inventory;

        if (getthisPanel().cb_crdcenterid.getItemCount() > 0) {
          strSql = "select a.invcode, a.invname, a.invspec, a.invtype, m.measname, p.pricemethod, p.pk_invmandoc,b.invclasscode,b.invclassname from bd_invbasdoc a inner join bd_measdoc m on a.pk_measdoc=m.pk_measdoc, bd_invcl b, bd_produce p where a.pk_invcl=b.pk_invcl" + ((this.m_sInvQuery.length() > 0) ? " and " : "") + this.m_sInvQuery + " and a.pk_invbasdoc = p.pk_invbasdoc" + " and p.pk_corp = '" + getthisPanel().m_spk_corp + "'" + " and p.pk_calbody = '" + getthisPanel().m_arycrdcenterid[getthisPanel().cb_crdcenterid.getSelectedIndex()] + "'" + " and p.pricemethod is not null";

          String DataPowerForCinvclass = PerviewManager.getDataPowerForCinvclass(sPk_corp, this.ce.getUser().getPrimaryKey());

          String DataPowerForCinventory = PerviewManager.getDataPowerForCinventory(sPk_corp, this.ce.getUser().getPrimaryKey());

          if (DataPowerForCinvclass.length() > 0) {
            strSql = strSql + " and b.pk_invcl in (";
            strSql = strSql + DataPowerForCinvclass;
            strSql = strSql + ") ";
          }

          if (DataPowerForCinventory.length() > 0) {
            strSql = strSql + " and p.pk_invmandoc in (";
            strSql = strSql + DataPowerForCinventory;
            strSql = strSql + ") ";
          }

          strSql = strSql + " order by a.invcode";

          resultS = CommonDataBO_Client.queryData(strSql);
          getthisPanel().m_arycinvcode = new String[resultS.length];
          getthisPanel().m_aryspectype = new String[resultS.length];
          getthisPanel().m_arymeasname = new String[resultS.length];
          getthisPanel().m_arypricemethod = new int[resultS.length];
          getthisPanel().m_arycinventoryid = new String[resultS.length];
          getthisPanel().m_arycinvclid = new String[resultS.length];
          getthisPanel().m_arycinvclname = new String[resultS.length];

          for (int i = 0; i < resultS.length; ++i) {
            getthisPanel().m_arycinvcode[i] = resultS[i][0];
            getthisPanel().m_aryspectype[i] = resultS[i][2].trim() + "," + resultS[i][3].trim();

            getthisPanel().m_arymeasname[i] = resultS[i][4].trim();
            getthisPanel().cb_cinventoryid.addItem(resultS[i][0].trim() + "," + resultS[i][1].trim());

            getthisPanel().m_arypricemethod[i] = Integer.parseInt(resultS[i][5]);

            getthisPanel().m_arycinventoryid[i] = resultS[i][6];
            getthisPanel().m_arycinvclid[i] = resultS[i][7];

            getthisPanel().m_arycinvclname[i] = resultS[i][8];
          }
          if (resultS.length > 0) {
            getthisPanel().rp_spectype.setText(getthisPanel().m_aryspectype[getthisPanel().cb_cinventoryid.getSelectedIndex()]);

            getthisPanel().rp_measname.setText(getthisPanel().m_arymeasname[getthisPanel().cb_cinventoryid.getSelectedIndex()]);
          }

        }

        if (getthisPanel().cb_cinventoryid.getItemCount() > 0)
        {
          String sCrdcenterid = getthisPanel().m_arycrdcenterid[getthisPanel().cb_crdcenterid.getSelectedIndex()];

          String sInventoryid = getthisPanel().m_arycinventoryid[getthisPanel().cb_cinventoryid.getSelectedIndex()];

          Object oTemp = this.ce.getPricingMode(sPk_corp, sCrdcenterid, new String[] { sInventoryid }).get(sInventoryid);

          if (oTemp != null) {
            String[] saResult = (String[])(String[])oTemp;

            if (saResult[1].equalsIgnoreCase("Y")) {
              bVbatch = true;
              String[] saVbatch = IAQueryBO_Client.getVbatch(sPk_corp, sCrdcenterid, sInventoryid);

              if (saVbatch != null) {
                getthisPanel().m_aryvbatch = new String[saVbatch.length];
                System.arraycopy(saVbatch, 0, getthisPanel().m_aryvbatch, 0, saVbatch.length);

                for (int i = 0; i < saVbatch.length; ++i)
                  getthisPanel().cb_vbatch.addItem(saVbatch[i]);
              }
            }
            else
            {
              bVbatch = false;
            }
          }
          else {
            bVbatch = false;
          }
        }
        else {
          bVbatch = false;
        }

        getthisPanel().m_bvbatch = bVbatch;
        getthisPanel().cb_vbatch.setEnabled(bVbatch);

        bIncludeNotAudit = this.qdialog.getUICheckBox1_pub().isSelected();

        String sfaxncbcz = CommonDataBO_Client.getParaValue(getthisPanel().m_spk_corp, new Integer(8));

        bSimulatemny = sfaxncbcz.equalsIgnoreCase("Y");

        getthisPanel().m_binmakecblist = false;

        getthisPanel().m_bIncludeNotAudit = bIncludeNotAudit;
        getthisPanel().m_bsimulatemny = bSimulatemny;

        if ((getthisPanel().cb_crdcenterid.getItemCount() == 0) || (getthisPanel().cb_cinventoryid.getItemCount() == 0) || ((bVbatch) && (getthisPanel().cb_vbatch.getItemCount() == 0)))
        {
          showHintMessage(NCLangRes.getInstance().getStrByID("20148040", "UPP20148040-000076"));

          getthisPanel().rp_spectype.setText("");
          getthisPanel().rp_measname.setText("");
          getthisPanel().m_voTableData = new GeneralLedgerPrintVO[0];
          getthisPanel().setBodyDataVO(getthisPanel().m_voTableData);
          refreshButtons(true);
          return;
        }

        bJHJ = getthisPanel().m_arypricemethod[getthisPanel().cb_cinventoryid.getSelectedIndex()] == 5;

        IaQueryConditionVO v1 = new IaQueryConditionVO();
        v1.setpk_corp(sPk_corp);
        v1.setaccperiod_start(getthisPanel().m_sAccperiod_start);
        v1.setaccperiod_stop(getthisPanel().m_sAccperiod_stop);
        v1.setcinvcode(getthisPanel().m_arycinvcode[getthisPanel().cb_cinventoryid.getSelectedIndex()]);

        v1.setcrdcenterid(getthisPanel().m_arycrdcenterid[getthisPanel().cb_crdcenterid.getSelectedIndex()]);

        v1.setincludenotaudit(bIncludeNotAudit);
        v1.setjhj(bJHJ);
        v1.setsimulatemny(bSimulatemny);
        v1.setIPriceMethod(getthisPanel().m_arypricemethod[getthisPanel().cb_cinventoryid.getSelectedIndex()]);

        if (bVbatch) {
          v1.setvbatch(getthisPanel().m_aryvbatch[getthisPanel().cb_vbatch.getSelectedIndex()]);
        }

        Thread thread = new Thread(this);
        this.m_bProcessFlag = true;
        thread.start();

        getthisPanel().makeData(v1);

        showHintMessage(NCLangRes.getInstance().getStrByID("20148020", "UPT20148020-000020"));

        refreshButtons(true);
      }
    }
    catch (Exception ivjExc)
    {
      ivjExc.printStackTrace();
    }
    finally {
      this.m_bProcessFlag = false;
    }
  }

  public void onButtonRefreshClicked()
  {
    try
    {
      Thread thread = new Thread(this);
      this.m_bProcessFlag = true;
      thread.start();
      getthisPanel().makeData(null);
      this.m_bProcessFlag = false;
      refreshButtons(true);

      showHintMessage(NCLangRes.getInstance().getStrByID("20148020", "UPT20148020-000020"));
    }
    catch (Exception e)
    {
      e.printStackTrace();
      this.m_bProcessFlag = false;
    }
  }

  public void onButtonReturnClicked()
  {
    getqueryDetailPanel().setVisible(false);
    getthisPanel().setVisible(true);

    setTitleText(this.m_sTitle);
    this.m_bButtonInvDetail.setVisible(true);
    this.m_bButtonInvDetail.setHint(NCLangRes.getInstance().getStrByID("20148040", "UPP20148040-000071"));

    this.m_bButtonReturn.setVisible(false);
    refreshButtons(true);
  }

  public void refreshButtons(boolean flag)
  {
    this.m_bButtonQuery.setEnabled(flag);
    this.m_bButtonInvDetail.setEnabled(flag);
    this.m_bButtonExport.setEnabled(flag);
    this.m_btnPrint.setEnabled(flag);
    this.m_bButtonRefresh.setEnabled(flag);
    this.m_btnColumnSet.setEnabled(flag);

    if (flag) {
      this.m_bButtonInvDetail.setEnabled(false);
      if ((getthisPanel().m_arycinvcode == null) || (getthisPanel().m_arycinvcode.length == 0) || (getthisPanel().m_voTableData.length == 0))
      {
        this.m_bButtonInvDetail.setEnabled(false);
        this.m_bButtonExport.setEnabled(false);
        this.m_btnPrint.setEnabled(false);
        this.m_bButtonRefresh.setEnabled(false);
      }
      else if ((getthisPanel().m_tUITable.getSelectedRow() >= 0) && (getthisPanel().m_tUITable.getSelectedRow() < getthisPanel().m_tUITable.getRowCount()) && (getthisPanel().m_voTableData[getthisPanel().m_tUITable.getSelectedRow()].getCaccountyear() != null))
      {
        this.m_bButtonInvDetail.setEnabled(true);
      }
      else {
        this.m_bButtonInvDetail.setEnabled(false);
      }
    }

    updateButtons();
  }

  public void run()
  {
    int nCount = 1; int nCount1 = 0;
    while (this.m_bProcessFlag) {
      try {
        Thread.sleep(100L);
      }
      catch (InterruptedException e) {
        e.printStackTrace();
      }
      String sMessage = NCLangRes.getInstance().getStrByID("20148040", "UPP20148040-000079");

      ++nCount1;
      if (nCount1 == 5) {
        nCount1 = 0;
        for (int n = 0; n <= nCount; ++n) {
          sMessage = sMessage + ".";
        }
        showHintMessage(sMessage);
        nCount = (nCount > 10) ? 1 : nCount + 1;
      }
    }
  }
}