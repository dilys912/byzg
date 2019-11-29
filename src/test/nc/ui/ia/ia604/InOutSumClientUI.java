package nc.ui.ia.ia604;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;
import nc.ui.ia.analyze.FormatDialog;
import nc.ui.ia.analyze.IaAnalyseBO_Client;
import nc.ui.ia.analyze.IaAnalyzeClientUI;
import nc.ui.ia.ia501.QueryPrintDataSource;
import nc.ui.ia.pub.IAEnvironment;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.print.IDataSource;
import nc.ui.pub.print.PrintEntry;
import nc.ui.pub.report.ReportBaseClass;
import nc.ui.pub.report.ReportItem;
import nc.ui.scm.pub.report.ShowItemsDlg;
import nc.vo.ia.analyze.InvInOutSumVO;
import nc.vo.ia.analyze.QueryVO;
import nc.vo.ia.analyze.RdtypeSumVO;
import nc.vo.ia.analyze.StatisticsVO;
import nc.vo.ia.pub.Log;
import nc.vo.pub.BusinessException;
import nc.vo.pub.cquery.FldgroupVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.query.RefResultVO;
import nc.vo.pub.report.ReportModelVO;
import nc.vo.sm.UserVO;

public class InOutSumClientUI extends IaAnalyzeClientUI
{
  private static final long serialVersionUID = 1L;
  private int BEGNUM = 0; private int BEGPRICE = 1; private int BEGMNY = 2; private int BEGPPRICE = 3; private int BEGPMNY = 4;
  private int BEGVMNY = 5; private int INNUM = 6; private int INPRICE = 7; private int INMNY = 8; private int INPPRICE = 9;
  private int INPMNY = 10; private int INVMNY = 11; private int OUTNUM = 12; private int OUTPRICE = 13; private int OUTMNY = 14;
  private int OUTPPRICE = 15; private int OUTPMNY = 16; private int OUTVMNY = 17; private int ABNUM = 18; private int ABPRICE = 19;
  private int ABMNY = 20; private int ABPPRICE = 21; private int ABPMNY = 22; private int ABVMNY = 23;

  private ShowItemsDlg columnSetDlg = null;

  private ArrayList<UFBoolean> m_ariItemShow = null;

  protected InOutSumQueryUI m_dlgQuery = null;

  private Hashtable<String, String[]> m_htColumnKey = null;

  private ReportBaseClass m_reportPanel = null;

  private ReportItem m_riFormatStr = null;

  private ReportItem m_riQueryStr = null;

  private String[] m_saShow = null;

  private InvInOutSumVO[] m_voaInOutSum = null;

  private ButtonObject m_bButtonRefresh = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000009"), NCLangRes.getInstance().getStrByID("common", "UC001-0000009"), 2, "刷新");

  protected ButtonObject[] m_arybtnGroup = { this.m_btnQuery, this.m_btnLocate, this.m_btnFormat, this.m_btnColumnSet, this.m_btnFilter, this.m_btnSort, this.m_btnSubTotal, this.m_btnDirectPrint, this.m_btnPrint, this.m_btnExcel, this.m_bButtonRefresh };

  private boolean ifhaveData = false;

  private QueryVO m_voQuery = null;

  protected void refreshButtons()
  {
    if (this.ifhaveData) {
      this.m_btnLocate.setEnabled(true);
      this.m_btnSort.setEnabled(true);
      this.m_btnSubTotal.setEnabled(true);
      this.m_btnDirectPrint.setEnabled(true);
      this.m_btnPrint.setEnabled(true);
      this.m_btnExcel.setEnabled(true);
      this.m_bButtonRefresh.setEnabled(true);
    }
    else {
      this.m_btnLocate.setEnabled(false);
      this.m_btnSort.setEnabled(false);
      this.m_btnSubTotal.setEnabled(false);
      this.m_btnDirectPrint.setEnabled(false);
      this.m_btnPrint.setEnabled(false);
      this.m_btnExcel.setEnabled(false);
      this.m_bButtonRefresh.setEnabled(false);
    }

    updateButtons();
  }

  public InOutSumClientUI()
  {
    init();
  }

  protected int getInvClassCodeLength() {
    return this.m_voQuery.getInvclLevel();
  }

  private Vector getFormatColKeys()
  {
    Vector vTempStrCol = new Vector();

    for (int j = 0; j < this.m_voaFormat.length; j++) {
      String[] saTemp = (String[])(String[])this.m_htColumnKey.get(this.m_voaFormat[j].getFieldCode());

      for (int i = 0; i < saTemp.length; i++) {
        if (saTemp[i].equals("invcode")) {
          vTempStrCol.addElement(saTemp[i]);
        }
        else if (saTemp[i].equals("invclasscode")) {
          vTempStrCol.addElement(saTemp[i]);

          if (!vTempStrCol.contains("invcode")) {
            vTempStrCol.addElement("invcode");
          }
        }
        else if (saTemp[i].equals("employeename")) {
          vTempStrCol.addElement(saTemp[i]);
        }
        else {
          if ((saTemp[i].indexOf("name") > 0) || (saTemp[i].startsWith("inv")))
            continue;
        }
        vTempStrCol.addElement(saTemp[i]);
      }
    }
    return vTempStrCol;
  }

  private String getFormatStr()
  {
    String sFormat = "";
    if (this.m_voaFormat != null) {
      int i = 0;
      for (; i < this.m_voaFormat.length - 1; i++) {
        sFormat = sFormat + this.m_voaFormat[i].getFieldName() + " + ";
      }
      sFormat = sFormat + this.m_voaFormat[i].getFieldName();
    }
    return sFormat;
  }

  public FormatDialog getFormatDlg()
  {
    if (this.m_dlgFormat == null)
    {
      Vector vTemp = new Vector();
      if (getReportPanel().getBody_Item("invcode") != null)
      {
        vTemp.addElement(new Object[] { Boolean.TRUE, NCLangRes.getInstance().getStrByID("common", "UC000-0001439"), "cinventoryid", "2", null });
      }

      if (getReportPanel().getBody_Item("invclasscode") != null) {
        vTemp.addElement(new Object[] { Boolean.FALSE, NCLangRes.getInstance().getStrByID("common", "UC000-0001443"), "pk_invcl", "2", null });
      }

      if (getReportPanel().getBody_Item("storcode") != null) {
        vTemp.addElement(new Object[] { Boolean.FALSE, NCLangRes.getInstance().getStrByID("common", "UC000-0000153"), "cwarehouseid", "2", null });
      }

      if (getReportPanel().getBody_Item("bodycode") != null) {
        vTemp.addElement(new Object[] { Boolean.FALSE, NCLangRes.getInstance().getStrByID("common", "UC000-0001825"), "crdcenterid", "2", null });
      }

      if (getReportPanel().getBody_Item("corpname") != null) {
        vTemp.addElement(new Object[] { Boolean.FALSE, NCLangRes.getInstance().getStrByID("common", "UC000-0000404"), "pk_corp", "2", null });
      }

      if (getReportPanel().getBody_Item("deptname") != null) {
        vTemp.addElement(new Object[] { Boolean.FALSE, NCLangRes.getInstance().getStrByID("common", "UC000-0004064"), "cdeptid", "2", null });
      }

      if (getReportPanel().getBody_Item("employeename") != null) {
        vTemp.addElement(new Object[] { Boolean.FALSE, NCLangRes.getInstance().getStrByID("common", "UC000-0000050"), "cemployeeid", "2", null });
      }

      Object[][] formatData = new Object[vTemp.size()][];
      vTemp.copyInto(formatData);

      this.m_dlgFormat = new FormatDialog(this, formatData, 10, 1, 0);
    }
    return this.m_dlgFormat;
  }

  private void getPrice()
  {
    if ((this.m_voaInOutSum == null) || (this.m_voaInOutSum.length <= 0)) {
      return;
    }
    UFDouble ufdNum = null;
    UFDouble ufdMon = null;
    UFDouble ufdPmon = null;

    for (int i = 0; i < this.m_voaInOutSum.length; i++) {
      ufdNum = this.m_voaInOutSum[i].getNbeginnum();
      if ((ufdNum != null) && (ufdNum.doubleValue() != 0.0D)) {
        ufdMon = this.m_voaInOutSum[i].getNbeginmny();
        if (ufdMon != null) {
          this.m_voaInOutSum[i].setNbeginprice(ufdMon.div(ufdNum));
        }
        ufdPmon = this.m_voaInOutSum[i].getNbeginplanedmny();
        if (ufdPmon != null) {
          this.m_voaInOutSum[i].setNbeginplanedprice(ufdPmon.div(ufdNum));
        }
      }
      ufdNum = this.m_voaInOutSum[i].getNinnum();
      if ((ufdNum != null) && (ufdNum.doubleValue() != 0.0D)) {
        ufdMon = this.m_voaInOutSum[i].getNinmny();
        if (ufdMon != null) {
          this.m_voaInOutSum[i].setNinprice(ufdMon.div(ufdNum));
        }
        ufdPmon = this.m_voaInOutSum[i].getNinplanedmny();
        if (ufdPmon != null) {
          this.m_voaInOutSum[i].setNinplanedprice(ufdPmon.div(ufdNum));
        }
      }
      ufdNum = this.m_voaInOutSum[i].getNoutnum();
      if ((ufdNum != null) && (ufdNum.doubleValue() != 0.0D)) {
        ufdMon = this.m_voaInOutSum[i].getNoutmny();
        if (ufdMon != null) {
          this.m_voaInOutSum[i].setNoutprice(ufdMon.div(ufdNum));
        }
        ufdPmon = this.m_voaInOutSum[i].getNoutplanedmny();
        if (ufdPmon != null) {
          this.m_voaInOutSum[i].setNoutplanedprice(ufdPmon.div(ufdNum));
        }
      }
      ufdNum = this.m_voaInOutSum[i].getNabnum();
      if ((ufdNum != null) && (ufdNum.doubleValue() != 0.0D)) {
        ufdMon = this.m_voaInOutSum[i].getNabmny();
        if (ufdMon != null) {
          this.m_voaInOutSum[i].setNabprice(ufdMon.div(ufdNum));
        }
        ufdPmon = this.m_voaInOutSum[i].getNabplanedmny();
        if (ufdPmon != null) {
          this.m_voaInOutSum[i].setNabplanedprice(ufdPmon.div(ufdNum));
        }
      }
      ufdNum = this.m_voaInOutSum[i].getNinadjustnum();
      if ((ufdNum != null) && (ufdNum.doubleValue() != 0.0D)) {
        ufdMon = this.m_voaInOutSum[i].getNinadjustmny();
        if (ufdMon != null) {
          this.m_voaInOutSum[i].setNinadjustprice(ufdMon.div(ufdNum));
        }
        ufdPmon = this.m_voaInOutSum[i].getNinadjustplanedmny();
        if (ufdPmon != null) {
          this.m_voaInOutSum[i].setNinadjustplanedprice(ufdPmon.div(ufdNum));
        }
      }
      ufdNum = this.m_voaInOutSum[i].getNinadjustnum();
      if ((ufdNum != null) && (ufdNum.doubleValue() != 0.0D)) {
        ufdMon = this.m_voaInOutSum[i].getNoutadjustmny();
        if (ufdMon != null) {
          this.m_voaInOutSum[i].setNoutadjustprice(ufdMon.div(ufdNum));
        }
        ufdPmon = this.m_voaInOutSum[i].getNoutadjustplanedmny();
        if (ufdPmon != null)
          this.m_voaInOutSum[i].setNoutadjustplanedprice(ufdPmon.div(ufdNum));
      }
    }
  }

  private String getQueryStr()
  {
    StringBuffer sbQuery = new StringBuffer();
    sbQuery.append(this.m_dlgQuery.getSqlShow());

    ConditionVO[] qvo = this.m_dlgQuery.getConditionVO();

    if ((qvo != null) && (qvo.length > 0)) {
      for (int i = 0; i < qvo.length; i++) {
        String sValue = qvo[i].getValue();

        if (qvo[i].getDataType() == 5) {
          sValue = qvo[i].getRefResult().getRefCode() + " " + qvo[i].getRefResult().getRefName();
        }

        if (!qvo[i].getFieldName().equals(NCLangRes.getInstance().getStrByID("common", "UC000-0000153"))) {
          continue;
        }
        if (qvo[i].getLogic()) {
          sbQuery.append("  " + qvo[i].getFieldName() + qvo[i].getOperaCode() + sValue);
        }
        else
        {
          sbQuery.append(" or " + qvo[i].getFieldName() + qvo[i].getOperaCode() + sValue);
        }
      }

    }

    return sbQuery.toString();
  }

  protected String getNodeCode()
  {
    return "20149040";
  }

  private ReportItem getReportItem(String sKey, String sName)
  {
    ReportItem riItem = new ReportItem();
    riItem.setName(sName);
    riItem.setKey(sKey);
    riItem.setDataType(2);
    riItem.setWidth(80);
    riItem.setEnabled(false);
    riItem.setShow(true);
    riItem.setPos(1);
    return riItem;
  }

  public ReportBaseClass getReportPanel()
  {
    if (this.m_reportPanel == null) {
      this.m_reportPanel = new ReportBaseClass();
      this.m_reportPanel.setName("ReportPanel");
      try {
        this.m_reportPanel.setTempletID(this.m_sCorpID, this.m_sNodeCode, this.m_SUserID, null);
      }
      catch (Exception e) {
        showErrorMessage(NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000001"));
      }

      if (this.m_htbBodyItemKey == null) {
        this.m_htbBodyItemKey = new Hashtable();

        ReportModelVO[] aryReportModels = this.m_reportPanel.getAllBodyVOs();

        String sColumnCode = null;
        int iSelectType = 1;
        for (int i = 0; i < aryReportModels.length; i++) {
          sColumnCode = aryReportModels[i].columnCode;
          iSelectType = aryReportModels[i].selectType.intValue();

          if ((sColumnCode == null) || (sColumnCode.length() <= 0) || (iSelectType == 0))
            continue;
          this.m_htbBodyItemKey.put(sColumnCode, sColumnCode);
        }
      }
    }

    return this.m_reportPanel;
  }

  protected String[] getSortKeys()
  {
    Vector vTempStrCol = new Vector();
    for (int j = 0; j < this.m_voaFormat.length - 1; j++) {
      String[] saTemp = (String[])(String[])this.m_htColumnKey.get(this.m_voaFormat[j].getFieldCode());

      for (int i = 0; i < saTemp.length; i++) {
        if (saTemp[i].indexOf("code") >= 0) {
          vTempStrCol.addElement(saTemp[i]);
        }
      }
    }
    String[] saReturn = new String[vTempStrCol.size()];
    vTempStrCol.copyInto(saReturn);
    return saReturn;
  }

  public String getTitle()
  {
    String sTitle = getReportPanel().getReportTitle();
    if (sTitle != null) {
      return sTitle;
    }

    return NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000002");
  }

  private void init()
  {
    try
    {
      setName("InOutSumClientUI");
      setSize(774, 419);
      setLayout(new BorderLayout());
      add(getReportPanel(), "Center");
    }
    catch (Exception ivjExc) {
      handleException(ivjExc);
    }

    setButtons(this.m_arybtnGroup);

    showNOAndSum(true);

    this.m_saShow = new String[] { "storcode", "storname", "bodycode", "bodyname", "invclasscode", "invclassname", "invcode", "invname", "invspec", "invtype", "graphid", "measname", "astunitname", "vbatch", "corpcode", "corpname", "deptcode", "deptname", "employeename" };

    this.m_htColumnKey = new Hashtable();
    String[] saTemp0 = new String[2];
    saTemp0[0] = this.m_saShow[0];
    saTemp0[1] = this.m_saShow[1];
    this.m_htColumnKey.put("cwarehouseid", saTemp0);

    String[] saTemp1 = new String[2];
    saTemp1[0] = this.m_saShow[2];
    saTemp1[1] = this.m_saShow[3];
    this.m_htColumnKey.put("crdcenterid", saTemp1);

    String[] saTemp2 = new String[2];
    saTemp2[0] = this.m_saShow[4];
    saTemp2[1] = this.m_saShow[5];
    this.m_htColumnKey.put("pk_invcl", saTemp2);

    String[] saTemp3 = new String[2];
    saTemp3[0] = this.m_saShow[14];
    saTemp3[1] = this.m_saShow[15];
    this.m_htColumnKey.put("pk_corp", saTemp3);

    String[] saTemp = new String[8];
    for (int i = 0; i < saTemp.length; i++) {
      saTemp[i] = this.m_saShow[(i + 6)];
    }
    this.m_htColumnKey.put("cinventoryid", saTemp);

    saTemp3 = new String[2];
    saTemp3[0] = this.m_saShow[16];
    saTemp3[1] = this.m_saShow[17];
    this.m_htColumnKey.put("cdeptid", saTemp3);

    this.m_htColumnKey.put("cemployeeid", new String[] { this.m_saShow[18] });

    this.m_riFormatStr = ((ReportItem)getReportPanel().getHeadItem("formatStr"));

    this.m_riQueryStr = ((ReportItem)getReportPanel().getHeadItem("queryStr"));

    setPrecision();
    refreshButtons();

    this.m_ariItemShow = new ArrayList();
    saveTempletSet();
  }

  private void makeDataByInv()
  {
    Vector vTemp = new Vector();
    Vector vCode = new Vector();
    try {
      for (int i = 0; i < this.m_voaInOutSum.length; i++) {
        String sKey = "";
        for (int m = 0; m < this.m_voaFormat.length; m++) {
          String sStatisticsCode = this.m_voaFormat[m].getFieldCode();

          if (sStatisticsCode.equals("cwarehouseid"))
          {
            return;
          }
          if (sStatisticsCode.equals("crdcenterid"))
          {
            return;
          }
          if (sStatisticsCode.equals("pk_corp"))
          {
            return;
          }
          if (sStatisticsCode.equals("cdeptid"))
          {
            return;
          }
          if (sStatisticsCode.equals("cemployeeid"))
          {
            return;
          }
        }

        String sInvCode = this.m_voaInOutSum[i].getInvcode();
        String sBatch = this.m_voaInOutSum[i].getVbatch();

        sKey = sKey + sInvCode + sBatch;
        int iIndex = vCode.indexOf(sKey);
        if (iIndex == -1)
        {
          vTemp.addElement(this.m_voaInOutSum[i]);
          vCode.addElement(sKey);
        }
        else
        {
          InvInOutSumVO voLastData = (InvInOutSumVO)vTemp.elementAt(iIndex);
          String[] sNames = voLastData.getAttributeNames();
          for (int j = 0; j < sNames.length; j++) {
            Object oValue = this.m_voaInOutSum[i].getAttributeValue(sNames[j]);
            Object oLastValue = voLastData.getAttributeValue(sNames[j]);
            if ((!sNames[j].startsWith("n")) || (sNames[j].indexOf("price") >= 0) || (oValue == null))
              continue;
            UFDouble dValue = new UFDouble(oValue.toString());
            UFDouble dLastValue = new UFDouble(oLastValue.toString());

            voLastData.setAttributeValue(sNames[j], dValue.add(dLastValue));
          }

          RdtypeSumVO[] lastrvos = voLastData.getRcl();
          RdtypeSumVO[] newrvos = this.m_voaInOutSum[i].getRcl();
          voLastData.setRcl(combinerdRDType(lastrvos, newrvos));

          RdtypeSumVO[] lastdvos = voLastData.getDcl();
          RdtypeSumVO[] newdvos = this.m_voaInOutSum[i].getDcl();
          voLastData.setDcl(combinerdRDType(lastdvos, newdvos));

          vTemp.setElementAt(voLastData, iIndex);
        }
      }

      if (vTemp.size() > 0) {
        this.m_voaInOutSum = new InvInOutSumVO[vTemp.size()];
        vTemp.copyInto(this.m_voaInOutSum);
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  protected boolean onFormat()
  {
    if (getFormatDlg().showModal() != 1) {
      return false;
    }

    this.m_voaFormat = getFormatDlg().getStatistics();

    if (this.m_dlgQuery != null) {
      ((UIRefPane)this.m_riQueryStr.getComponent()).setText(getQueryStr());
    }

    ((UIRefPane)this.m_riFormatStr.getComponent()).setText(getFormatStr());
    getReportPanel().updateValue();
    if (this.m_voQuery == null) {
      return false;
    }

    String[] sCorpIDs = this.m_voQuery.getPk_Corps();

    if ((sCorpIDs != null) && (sCorpIDs.length > 1) && (!getFormatColKeys().contains("corpcode")));
    this.m_voQuery.setStatistics(this.m_voaFormat);
    queryData();

    return true;
  }

  protected void onRefresh()
  {
    showHintMessage(NCLangRes.getInstance().getStrByID("2014", "UPP2014-000635"));

    queryData();
  }

  public void onButtonClicked(ButtonObject bo)
  {
    showHintMessage("");
    if (bo == this.m_bButtonRefresh) {
      onRefresh();
    }
    else
      super.onButtonClicked(bo);
  }

  protected void onPrint()
  {
    try {
      if ((this.m_voaInOutSum == null) || (this.m_voaInOutSum.length <= 0)) {
        MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000059"), NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000003"));

        return;
      }

      if (this.m_voQuery.isSplitRdcl())
      {
        MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000059"), NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000063"));

        return;
      }

      IDataSource dataSource = null;

      InvInOutSumVO[] voResult = (InvInOutSumVO[])(InvInOutSumVO[])getReportPanel().getBillModel().getBodyValueVOs("nc.vo.ia.analyze.InvInOutSumVO");

      dataSource = new QueryPrintDataSource(voResult, getReportPanel().getBillData(), this.m_sNodeCode);

      PrintEntry print = new PrintEntry(null, null);

      print.setTemplateID(this.m_sCorpID, this.m_sNodeCode, this.m_SUserID, null);

      print.setDataSource(dataSource);
      if (print.selectTemplate() < 0) {
        return;
      }
      print.preview();
    }
    catch (Exception e) {
      e.printStackTrace();
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000059"), NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000004"));
    }
  }

  protected void onQuery()
  {
    if (this.m_dlgQuery == null) {
      this.m_dlgQuery = new InOutSumQueryUI(this, "", this.ce.getUser().getPrimaryKey(), this.ce.getCorporationID());

      this.m_dlgQuery.setRefWherePart(this.m_sCorpID);
    }
    this.m_dlgQuery.showModal();
    if (this.m_dlgQuery.isCloseOK()) {
      try {
        ((UIRefPane)this.m_riQueryStr.getComponent()).setText(getQueryStr());
        ((UIRefPane)this.m_riFormatStr.getComponent()).setText(getFormatStr());
        getReportPanel().updateValue();
        this.m_voQuery = this.m_dlgQuery.getQueryVO();
        this.m_voQuery.setConditionVO(this.m_dlgQuery.getConditionVO());
        String[] sCorps = this.m_voQuery.getPk_Corps();
        if ((sCorps != null) && (sCorps.length == 1) && (!this.m_sCorpID.equals(sCorps[0])))
        {
          this.m_sCorpID = sCorps[0];
          this.m_aryiPrecision = this.ce.getDataPrecision(sCorps[0]);
        }
        else if ((sCorps != null) && (sCorps.length != 0) && (!this.m_sCorpID.equals(sCorps[0])))
        {
          this.m_aryiPrecision = this.ce.getDataPrecision(sCorps[0]);
        }
      }
      catch (Exception e) {
        e.printStackTrace();
      }

      this.m_voQuery.setStatistics(this.m_voaFormat);
      this.m_voQuery.SourceTable = QueryVO.INOUTSUM;
      queryData();
    }
  }

  private void procFieldGroupData(int iKeyIndex, Vector<ReportItem> vAddReportItem, Vector<FldgroupVO> vFieldGroup, String sTitle, String sSubTitle, String[] saColKey, String[] saColTitle, boolean bRetTopLevel)
  {
    if ((sTitle == null) || (saColTitle == null) || (saColTitle.length == 0) || (saColKey == null) || (saColKey.length != saColTitle.length) || (vAddReportItem == null) || (vFieldGroup == null))
    {
      Log.info("param null");
      return;
    }

    ReportItem riTemp = null;
    if (iKeyIndex >= 0) {
      riTemp = getReportItem(saColKey[0] + iKeyIndex, saColTitle[0]);
    }
    else {
      riTemp = getReportItem(saColKey[0], saColTitle[0]);
    }
    vAddReportItem.addElement(riTemp);

    int iColNum = saColTitle.length;

    FldgroupVO voFg = null;

    if (iColNum == 1) {
      voFg = new FldgroupVO();
      voFg.setGroupid(new Integer(0));
      voFg.setItem1(sSubTitle);
      voFg.setItem2("" + (vAddReportItem.size() - 1));
      voFg.setGrouptype("1");
      if (bRetTopLevel) {
        voFg.setToplevelflag("Y");
      }
      else {
        voFg.setToplevelflag("N");
      }
      voFg.setGroupname(sTitle);
      vFieldGroup.addElement(voFg);
      return;
    }

    if (iColNum > 1) {
      if (iKeyIndex >= 0) {
        riTemp = getReportItem(saColKey[1] + iKeyIndex, saColTitle[1]);
      }
      else {
        riTemp = getReportItem(saColKey[1], saColTitle[1]);
      }
      vAddReportItem.addElement(riTemp);

      voFg = new FldgroupVO();
      voFg.setGroupid(new Integer(0));
      voFg.setItem1("" + (vAddReportItem.size() - 2));
      voFg.setItem2("" + (vAddReportItem.size() - 1));
      voFg.setGrouptype("0");
      if ((iColNum == 2) && (bRetTopLevel) && (sTitle.equals(sSubTitle))) {
        voFg.setToplevelflag("Y");
      }
      else {
        voFg.setToplevelflag("N");
      }
      voFg.setGroupname(sSubTitle);

      vFieldGroup.addElement(voFg);
      if ((iColNum == 2) && (bRetTopLevel) && (sTitle.equals(sSubTitle))) {
        return;
      }

    }

    for (int col = 2; col < iColNum; col++) {
      if (iKeyIndex >= 0) {
        riTemp = getReportItem(saColKey[col] + iKeyIndex, saColTitle[col]);
      }
      else {
        riTemp = getReportItem(saColKey[col], saColTitle[col]);
      }
      vAddReportItem.addElement(riTemp);

      voFg = new FldgroupVO();
      voFg.setGroupid(new Integer(0));
      voFg.setItem1(sSubTitle);
      voFg.setItem2("" + (vAddReportItem.size() - 1));
      voFg.setGrouptype("2");
      voFg.setToplevelflag("N");

      if ((col == iColNum - 1) && (sTitle.equals(sSubTitle)) && 
        (bRetTopLevel)) {
        voFg.setToplevelflag("Y");
      }

      voFg.setGroupname(sSubTitle);
      vFieldGroup.addElement(voFg);

      if ((col == iColNum - 1) && (sTitle.equals(sSubTitle))) {
        return;
      }
    }

    if (sTitle != sSubTitle) {
      int iSearch = 0;
      FldgroupVO voTemporaryField = null;
      for (iSearch = 0; iSearch < vFieldGroup.size(); iSearch++) {
        if (vFieldGroup.elementAt(iSearch) != null) {
          voTemporaryField = (FldgroupVO)vFieldGroup.elementAt(iSearch);
          if (sTitle.equals(voTemporaryField.getGroupname()))
          {
            break;
          }
        }
      }
      if (iSearch >= vFieldGroup.size()) {
        voFg = new FldgroupVO();
        voFg.setGroupid(new Integer(0));
        voFg.setToplevelflag("Y");
        voFg.setItem1(sSubTitle);
        voFg.setItem2(sSubTitle);
        voFg.setGrouptype("3");
        voFg.setGroupname(sTitle);
        vFieldGroup.addElement(voFg);
      }
      else
      {
        if (voTemporaryField.getItem1().equals(voTemporaryField.getItem2())) {
          vFieldGroup.removeElementAt(iSearch);
        }
        voFg = new FldgroupVO();
        voFg.setGroupid(new Integer(0));
        if (bRetTopLevel) {
          voFg.setToplevelflag("Y");
        }
        else {
          voFg.setToplevelflag("N");
        }
        if (voTemporaryField.getItem1().equals(voTemporaryField.getItem2())) {
          voFg.setItem1(voTemporaryField.getItem1());
        }
        else {
          voFg.setItem1(sTitle);
        }
        voFg.setItem2(sSubTitle);
        voFg.setGrouptype("3");
        voFg.setGroupname(sTitle);
        vFieldGroup.addElement(voFg);
      }
    }
  }

  private void procFieldGroupDataNew(Vector<ReportItem> vAddReportItem, Vector<FldgroupVO> vFieldGroup, String sTitle, ReportItem[] riaItem)
  {
    if ((sTitle == null) || (riaItem == null) || (vFieldGroup == null)) {
      Log.info("param null");
      return;
    }

    vAddReportItem.addElement(riaItem[0]);

    int iColNum = riaItem.length;

    FldgroupVO voFg = null;

    if (iColNum == 1) {
      voFg = new FldgroupVO();
      voFg.setGroupid(new Integer(0));
      voFg.setItem1("" + (vAddReportItem.size() - 1));
      voFg.setItem2(null);
      voFg.setGrouptype("0");
      voFg.setToplevelflag("Y");
      voFg.setGroupname(sTitle);
      vFieldGroup.addElement(voFg);
      return;
    }

    if (iColNum > 1) {
      vAddReportItem.addElement(riaItem[1]);

      voFg = new FldgroupVO();
      voFg.setGroupid(new Integer(0));
      voFg.setItem1("" + (vAddReportItem.size() - 2));
      voFg.setItem2("" + (vAddReportItem.size() - 1));
      voFg.setGrouptype("0");
      if (iColNum == 2) {
        voFg.setToplevelflag("Y");
      }
      else {
        voFg.setToplevelflag("N");
      }
      voFg.setGroupname(sTitle);

      vFieldGroup.addElement(voFg);
      if (iColNum == 2) {
        return;
      }

    }

    for (int col = 2; col < iColNum; col++) {
      vAddReportItem.addElement(riaItem[col]);

      voFg = new FldgroupVO();
      voFg.setGroupid(new Integer(0));
      voFg.setItem1(sTitle);
      voFg.setItem2("" + (vAddReportItem.size() - 1));
      voFg.setGrouptype("2");
      voFg.setToplevelflag("N");

      if (col == iColNum - 1) {
        voFg.setToplevelflag("Y");
      }
      voFg.setGroupname(sTitle);
      vFieldGroup.addElement(voFg);
    }
  }

  private void queryData()
  {
    try
    {
      boolean bInvSelect = getFormatDlg().isInvSelect();
      if ((this.m_voQuery.isShowAssistant()) && 
        (!bInvSelect)) {
        MessageDialog.showWarningDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270"), NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000005"));

        this.m_riFormatStr.setValue("");
        this.m_riQueryStr.setValue("");
        return;
      }

      if (this.m_voQuery.getInvclLevel() != 0) {
        boolean bFind = false;
        for (int i = 0; i < this.m_voaFormat.length; i++) {
          if (!this.m_voaFormat[i].getFieldCode().equals("pk_invcl"))
            continue;
          bFind = true;
        }

        if (!bFind) {
          MessageDialog.showWarningDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270"), NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000006"));

          return;
        }
        if (bInvSelect) {
          MessageDialog.showWarningDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270"), NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000007"));

          return;
        }
      }
      if ((this.m_voQuery.isShowSetPart()) && 
        (!bInvSelect)) {
        MessageDialog.showWarningDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270"), NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000008"));

        return;
      }

      if (this.m_vTableData != null) {
        this.m_vTableData.removeAllElements();
      }
      ArrayList alResult = IaAnalyseBO_Client.getInOutSum(this.m_voQuery);
      this.m_voaInOutSum = ((InvInOutSumVO[])(InvInOutSumVO[])alResult.get(0));
      Hashtable htRdInName = (Hashtable)alResult.get(1);
      Hashtable htRdOutName = (Hashtable)alResult.get(2);

      if ((this.m_voaInOutSum == null) || (this.m_voaInOutSum.length == 0))
      {
        getReportPanel().setBodyDataVO(null);
        showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000035"));

        this.ifhaveData = false;
        refreshButtons();
        return;
      }

      this.ifhaveData = true;
      refreshButtons();
      if (this.m_voQuery.isSplitRdcl()) {
        splitRdcl(htRdInName, htRdOutName);
      }
      else {
        resetTableColumns();
      }

      setInterfaceData();
      setReportData(this.m_voaInOutSum);

      if ((this.m_voQuery.getPk_Corps() != null) && (this.m_voQuery.getPk_Corps().length > 1)) {
        makeDataByInv();
      }

      if ((getFormatDlg().isInvclSelect()) && (!getFormatDlg().isInvSelect())) {
        this.m_voaInOutSum = makeDataByInvcl(this.m_voaInOutSum);
      }
      getPrice();
      getReportPanel().setBodyDataVO(this.m_voaInOutSum, false);

      reCalcSum();

      if (this.columnSetDlg != null) {
        this.columnSetDlg = null;
      }

      String[] sValue = { String.valueOf(this.m_voaInOutSum.length) };

      setBooleanValue(true);

      showHintMessage(NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000009", null, sValue));
    }
    catch (BusinessException e)
    {
      e.printStackTrace();

      String message = e.getMessage();
      if (message.indexOf(":") > 0) {
        message = message.substring(message.indexOf(":"));
      }
      String[] sValue = { message };

      showHintMessage(NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000010", null, sValue));
    }
    catch (Throwable e)
    {
      e.printStackTrace();
      showHintMessage(NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000011"));
    }
  }

  private void resetTableColumns()
  {
    try
    {
      ReportItem[] biReport = this.m_riaDefault;

      if ((biReport == null) || (biReport.length == 0)) {
        return;
      }

      Vector vBillItem = new Vector();
      Vector vTempStrCol = new Vector();
      boolean bShowAst = this.m_voQuery.isShowAssistant();

      for (int j = 0; j < this.m_voaFormat.length; j++) {
        String[] saTemp = (String[])(String[])this.m_htColumnKey.get(this.m_voaFormat[j].getFieldCode());

        for (int i = 0; i < saTemp.length; i++) {
          vTempStrCol.addElement(saTemp[i]);
        }
        if (!bShowAst) {
          vTempStrCol.removeElement("astunitname");
        }
      }
      int[] iaIndex = new int[vTempStrCol.size()];

      for (int m = 0; m < iaIndex.length; m++) {
        iaIndex[m] = -1;
      }

      for (int i = 0; i < biReport.length; i++)
      {
        int iIndex = vTempStrCol.indexOf(biReport[i].getKey());
        if (iIndex >= 0) {
          biReport[i].setShow(true);
          iaIndex[iIndex] = i;
        }
        else if (biReport[i].getName().indexOf("ID") >= 0) {
          vBillItem.addElement(biReport[i]);
        }
      }
      for (int j = 0; j < iaIndex.length; j++) {
        if (iaIndex[j] >= 0) {
          vBillItem.addElement(biReport[iaIndex[j]]);
        }
      }

      Vector vFieldGroup = new Vector();
      boolean bShowAdjust = this.m_voQuery.isShowAdjustMoney();
      ArrayList arlGroup = new ArrayList(6);

      Vector vItemBegin = new Vector(1, 1);
      arlGroup.add(0, vItemBegin);
      Vector vItemIn = new Vector(1, 1);
      arlGroup.add(1, vItemIn);
      Vector vItemInAdjust = new Vector(1, 1);
      if (bShowAdjust) {
        arlGroup.add(2, vItemInAdjust);
      }
      else {
        arlGroup.add(2, null);
      }
      Vector vItemOut = new Vector(1, 1);
      arlGroup.add(3, vItemOut);
      Vector vItemOutAdjust = new Vector(1, 1);
      if (bShowAdjust) {
        arlGroup.add(4, vItemOutAdjust);
      }
      else {
        arlGroup.add(4, null);
      }
      Vector vItemAb = new Vector(1, 1);
      arlGroup.add(5, vItemAb);
      for (int m = 0; m < this.m_riaDefault.length; m++) {
        String sKey = this.m_riaDefault[m].getKey();
        if (sKey.startsWith("nbegin")) {
          if (sKey.indexOf("astnum") >= 0) {
            if (bShowAst) {
              this.m_riaDefault[m].setShow(true);
              vItemBegin.addElement(this.m_riaDefault[m]);
            }

          }
          else
          {
            this.m_riaDefault[m].setShow(true);
            vItemBegin.addElement(this.m_riaDefault[m]);
          }
        }
        else if (sKey.startsWith("ninadjust")) {
          this.m_riaDefault[m].setShow(true);
          vItemInAdjust.addElement(this.m_riaDefault[m]);
        }
        else if (sKey.startsWith("nin")) {
          if (sKey.indexOf("astnum") >= 0) {
            if (bShowAst) {
              this.m_riaDefault[m].setShow(true);
              vItemIn.addElement(this.m_riaDefault[m]);
            }

          }
          else
          {
            this.m_riaDefault[m].setShow(true);
            vItemIn.addElement(this.m_riaDefault[m]);
          }
        }
        else if (sKey.startsWith("noutadjust")) {
          this.m_riaDefault[m].setShow(true);
          vItemOutAdjust.addElement(this.m_riaDefault[m]);
        }
        else if ((sKey.startsWith("nout")) || (sKey.startsWith("nback"))) {
          if (sKey.indexOf("astnum") >= 0) {
            if (bShowAst) {
              this.m_riaDefault[m].setShow(true);
              vItemOut.addElement(this.m_riaDefault[m]);
            }

          }
          else
          {
            this.m_riaDefault[m].setShow(true);
            vItemOut.addElement(this.m_riaDefault[m]);
          }
        }
        else if (sKey.startsWith("nab")) {
          if (sKey.indexOf("astnum") >= 0) {
            if (bShowAst) {
              this.m_riaDefault[m].setShow(true);
              vItemAb.addElement(this.m_riaDefault[m]);
            }

          }
          else
          {
            this.m_riaDefault[m].setShow(true);
            vItemAb.addElement(this.m_riaDefault[m]);
          }
        }
      }

      Object oTemp = null;
      Vector vTemp = null;
      ReportItem[] riaTemp = null;
      String sTitle = "";
      for (int i = 0; i < arlGroup.size(); i++) {
        oTemp = arlGroup.get(i);
        if (oTemp == null) {
          continue;
        }
        vTemp = (Vector)oTemp;
        if (vTemp.size() <= 0) {
          continue;
        }
        riaTemp = new ReportItem[vTemp.size()];
        vTemp.copyInto(riaTemp);
        switch (i) {
        case 0:
          sTitle = NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000012");

          break;
        case 1:
          sTitle = NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000013");

          break;
        case 2:
          sTitle = NCLangRes.getInstance().getStrByID("common", "UC000-0002204");

          break;
        case 3:
          sTitle = NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000014");

          break;
        case 4:
          sTitle = NCLangRes.getInstance().getStrByID("common", "UC000-0000968");

          break;
        case 5:
          sTitle = NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000015");
        }

        procFieldGroupDataNew(vBillItem, vFieldGroup, sTitle, riaTemp);
      }

      ReportItem[] riaResult = new ReportItem[vBillItem.size()];
      vBillItem.copyInto(riaResult);
      for (int l = 0; l < riaResult.length; l++) {
        riaResult[l].setShowOrder(l);
      }

      FldgroupVO[] voaFg = new FldgroupVO[vFieldGroup.size()];
      vFieldGroup.copyInto(voaFg);
      getReportPanel().setFieldGroup(voaFg);
      getReportPanel().setBody_Items(riaResult);
      getReportPanel().updateValue();
    }
    catch (Exception e) {
      handleException(e);
    }
  }

  private void saveTempletSet()
  {
    if (getReportPanel().getBody_Item("nbeginnum") != null) {
      this.m_ariItemShow.add(this.BEGNUM, new UFBoolean(true));
    }
    else {
      this.m_ariItemShow.add(this.BEGNUM, new UFBoolean(false));
    }
    if (getReportPanel().getBody_Item("nbeginprice") != null) {
      this.m_ariItemShow.add(this.BEGPRICE, new UFBoolean(true));
    }
    else {
      this.m_ariItemShow.add(this.BEGPRICE, new UFBoolean(false));
    }

    if (getReportPanel().getBody_Item("nbeginmny") != null) {
      this.m_ariItemShow.add(this.BEGMNY, new UFBoolean(true));
    }
    else {
      this.m_ariItemShow.add(this.BEGMNY, new UFBoolean(false));
    }

    if (getReportPanel().getBody_Item("nbeginplanedprice") != null) {
      this.m_ariItemShow.add(this.BEGPPRICE, new UFBoolean(true));
    }
    else {
      this.m_ariItemShow.add(this.BEGPPRICE, new UFBoolean(false));
    }
    if (getReportPanel().getBody_Item("nbeginplanedmny") != null) {
      this.m_ariItemShow.add(this.BEGPMNY, new UFBoolean(true));
    }
    else {
      this.m_ariItemShow.add(this.BEGPMNY, new UFBoolean(false));
    }
    if (getReportPanel().getBody_Item("nbeginvarymny") != null) {
      this.m_ariItemShow.add(this.BEGVMNY, new UFBoolean(true));
    }
    else {
      this.m_ariItemShow.add(this.BEGVMNY, new UFBoolean(false));
    }
    if (getReportPanel().getBody_Item("ninnum") != null) {
      this.m_ariItemShow.add(this.INNUM, new UFBoolean(true));
    }
    else {
      this.m_ariItemShow.add(this.INNUM, new UFBoolean(false));
    }
    if (getReportPanel().getBody_Item("ninprice") != null) {
      this.m_ariItemShow.add(this.INPRICE, new UFBoolean(true));
    }
    else {
      this.m_ariItemShow.add(this.INPRICE, new UFBoolean(false));
    }

    if (getReportPanel().getBody_Item("ninmny") != null) {
      this.m_ariItemShow.add(this.INMNY, new UFBoolean(true));
    }
    else {
      this.m_ariItemShow.add(this.INMNY, new UFBoolean(false));
    }

    if (getReportPanel().getBody_Item("ninplanedprice") != null) {
      this.m_ariItemShow.add(this.INPPRICE, new UFBoolean(true));
    }
    else {
      this.m_ariItemShow.add(this.INPPRICE, new UFBoolean(false));
    }
    if (getReportPanel().getBody_Item("ninplanedmny") != null) {
      this.m_ariItemShow.add(this.INPMNY, new UFBoolean(true));
    }
    else {
      this.m_ariItemShow.add(this.INPMNY, new UFBoolean(false));
    }
    if (getReportPanel().getBody_Item("ninvarymny") != null) {
      this.m_ariItemShow.add(this.INVMNY, new UFBoolean(true));
    }
    else {
      this.m_ariItemShow.add(this.INVMNY, new UFBoolean(false));
    }
    if (getReportPanel().getBody_Item("noutnum") != null) {
      this.m_ariItemShow.add(this.OUTNUM, new UFBoolean(true));
    }
    else {
      this.m_ariItemShow.add(this.OUTNUM, new UFBoolean(false));
    }
    if (getReportPanel().getBody_Item("noutprice") != null) {
      this.m_ariItemShow.add(this.OUTPRICE, new UFBoolean(true));
    }
    else {
      this.m_ariItemShow.add(this.OUTPRICE, new UFBoolean(false));
    }

    if (getReportPanel().getBody_Item("noutmny") != null) {
      this.m_ariItemShow.add(this.OUTMNY, new UFBoolean(true));
    }
    else {
      this.m_ariItemShow.add(this.OUTMNY, new UFBoolean(false));
    }

    if (getReportPanel().getBody_Item("noutplanedprice") != null) {
      this.m_ariItemShow.add(this.OUTPPRICE, new UFBoolean(true));
    }
    else {
      this.m_ariItemShow.add(this.OUTPPRICE, new UFBoolean(false));
    }
    if (getReportPanel().getBody_Item("noutplanedmny") != null) {
      this.m_ariItemShow.add(this.OUTPMNY, new UFBoolean(true));
    }
    else {
      this.m_ariItemShow.add(this.OUTPMNY, new UFBoolean(false));
    }
    if (getReportPanel().getBody_Item("noutvarymny") != null) {
      this.m_ariItemShow.add(this.OUTVMNY, new UFBoolean(true));
    }
    else {
      this.m_ariItemShow.add(this.OUTVMNY, new UFBoolean(false));
    }
    if (getReportPanel().getBody_Item("nabnum") != null) {
      this.m_ariItemShow.add(this.ABNUM, new UFBoolean(true));
    }
    else {
      this.m_ariItemShow.add(this.ABNUM, new UFBoolean(false));
    }
    if (getReportPanel().getBody_Item("nabprice") != null) {
      this.m_ariItemShow.add(this.ABPRICE, new UFBoolean(true));
    }
    else {
      this.m_ariItemShow.add(this.ABPRICE, new UFBoolean(false));
    }

    if (getReportPanel().getBody_Item("nabmny") != null) {
      this.m_ariItemShow.add(this.ABMNY, new UFBoolean(true));
    }
    else {
      this.m_ariItemShow.add(this.ABMNY, new UFBoolean(false));
    }

    if (getReportPanel().getBody_Item("nabplanedprice") != null) {
      this.m_ariItemShow.add(this.ABPPRICE, new UFBoolean(true));
    }
    else {
      this.m_ariItemShow.add(this.ABPPRICE, new UFBoolean(false));
    }
    if (getReportPanel().getBody_Item("nabplanedmny") != null) {
      this.m_ariItemShow.add(this.ABPMNY, new UFBoolean(true));
    }
    else {
      this.m_ariItemShow.add(this.ABPMNY, new UFBoolean(false));
    }
    if (getReportPanel().getBody_Item("nabvarymny") != null) {
      this.m_ariItemShow.add(this.ABVMNY, new UFBoolean(true));
    }
    else
      this.m_ariItemShow.add(this.ABVMNY, new UFBoolean(false));
  }

  private void setInterfaceData()
  {
    BillItem[] bi = getReportPanel().getBody_Items();
    for (int i = 0; i < bi.length; i++) {
      String sKey = bi[i].getKey();
      if (sKey != null)
        if (sKey.indexOf("mny") >= 0) {
          bi[i].setDecimalDigits(this.m_aryiPrecision[2]);
        }
        else if (sKey.indexOf("num") >= 0) {
          bi[i].setDecimalDigits(this.m_aryiPrecision[0]);
        }
        else
          bi[i].setDecimalDigits(this.m_aryiPrecision[1]);
    }
  }

  private void setReportData(InvInOutSumVO[] voaData)
  {
    ArrayList arlItemField = new ArrayList();
    Vector vColKeys = getFormatColKeys();

    if (vColKeys.contains("invcode"))
    {
      String[] saItemField20 = { "pk_invbasdoc", "pk_invbasdoc", "pk_invmandoc" };

      arlItemField.add(saItemField20);

      String[] saItemField21 = { "invname", "invname", "pk_invbasdoc" };

      arlItemField.add(saItemField21);

      String[] saItemField27 = { "invcode", "invcode", "pk_invbasdoc" };

      arlItemField.add(saItemField27);

      String[] saItemField22 = { "invspec", "invspec", "pk_invbasdoc" };

      arlItemField.add(saItemField22);

      String[] saItemField23 = { "invtype", "invtype", "pk_invbasdoc" };

      arlItemField.add(saItemField23);

      String[] aryItemField223 = { "graphid", "graphid", "pk_invbasdoc" };

      arlItemField.add(aryItemField223);

      String[] saItemField25 = { "pk_measdoc", "pk_measdoc", "pk_invbasdoc" };

      arlItemField.add(saItemField25);

      String[] saItemField30 = { "measname", "measname", "pk_measdoc" };

      arlItemField.add(saItemField30);
    }

    if (vColKeys.contains("invclasscode"))
    {
      String[] saItemField12 = { "invclasscode", "invclasscode", "pk_invcl" };

      arlItemField.add(saItemField12);

      String[] saItemField13 = { "invclassname", "invclassname", "pk_invcl" };

      arlItemField.add(saItemField13);
    }

    if (vColKeys.contains("storcode"))
    {
      String[] saItemField14 = { "storcode", "storcode", "cwarehouseid" };

      arlItemField.add(saItemField14);

      String[] saItemField15 = { "storname", "storname", "cwarehouseid" };

      arlItemField.add(saItemField15);
    }

    if (vColKeys.contains("corpcode"))
    {
      String[] saItemField15 = { "unitname", "corpname", "corpid" };

      arlItemField.add(saItemField15);

      String[] saItemField16 = { "unitcode", "corpcode", "corpid" };

      arlItemField.add(saItemField16);
    }

    if (vColKeys.contains("bodycode"))
    {
      String[] saItemField16 = { "bodycode", "bodycode", "crdcenterid" };

      arlItemField.add(saItemField16);

      String[] saItemField17 = { "bodyname", "bodyname", "crdcenterid" };

      arlItemField.add(saItemField17);
    }

    if (this.m_voQuery.isShowAssistant())
    {
      String[] aryItemField31 = { "measname", "astunitname", "castunitid" };

      arlItemField.add(aryItemField31);
    }

    if (vColKeys.contains("employeename"))
    {
      String[] saItemField15 = { "psnname", "employeename", "cemployeeid" };

      arlItemField.add(saItemField15);
    }

    if (vColKeys.contains("deptcode"))
    {
      String[] saItemField15 = { "deptcode", "deptcode", "cdeptid" };

      arlItemField.add(saItemField15);

      String[] saItemField16 = { "deptname", "deptname", "cdeptid" };

      arlItemField.add(saItemField16);
    }

    long lTime = System.currentTimeMillis();
    this.m_voaInOutSum = ((InvInOutSumVO[])(InvInOutSumVO[])setBodyDatabyFormulaItem(voaData, arlItemField, true, true));

    showTime(lTime, NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000016"));
  }

  protected void showTime(long lStartTime, String sTaskHint)
  {
    long lTime = System.currentTimeMillis() - lStartTime;
    Log.info("执行<" + sTaskHint + ">消耗的时间为：" + lTime / 60000L + "分" + lTime / 1000L % 60L + "秒" + lTime % 1000L + "毫秒");
  }

  public void splitRdcl(Hashtable htRdTypeNameIn, Hashtable htRdTypeNameOut)
  {
    try
    {
      ReportItem[] biReport = this.m_riaDefault;

      if ((biReport == null) || (biReport.length == 0)) {
        return;
      }

      int iInRdCount = 1;
      int iOutRdCount = 1;

      if ((htRdTypeNameIn != null) && (htRdTypeNameIn.size() > 0)) {
        iInRdCount = htRdTypeNameIn.size();
      }
      if ((htRdTypeNameOut != null) && (htRdTypeNameOut.size() > 0)) {
        iOutRdCount = htRdTypeNameOut.size();
      }

      Vector vBillItem = new Vector();
      Vector vTempStrCol = new Vector();
      boolean bShowAst = this.m_voQuery.isShowAssistant();
      for (int j = 0; j < this.m_voaFormat.length; j++) {
        String[] saTemp = (String[])(String[])this.m_htColumnKey.get(this.m_voaFormat[j].getFieldCode());

        for (int i = 0; i < saTemp.length; i++) {
          vTempStrCol.addElement(saTemp[i]);
        }
        if (!bShowAst) {
          vTempStrCol.removeElement("castunitname");
        }
      }
      int[] iaIndex = new int[vTempStrCol.size()];

      for (int m = 0; m < iaIndex.length; m++) {
        iaIndex[m] = -1;
      }

      for (int i = 0; i < biReport.length; i++)
      {
        int iIndex = vTempStrCol.indexOf(biReport[i].getKey());
        if (iIndex >= 0) {
          biReport[i].setShow(true);
          iaIndex[iIndex] = i;
        }
        else if (biReport[i].getName().indexOf("ID") >= 0) {
          vBillItem.addElement(biReport[i]);
        }
      }
      for (int j = 0; j < iaIndex.length; j++) {
        if (iaIndex[j] >= 0) {
          vBillItem.addElement(biReport[iaIndex[j]]);
        }
      }

      Vector vFieldGroup = new Vector();

      String sTopTitle = NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000012");

      Vector vColTitle = new Vector(1, 1);
      Vector vColKey = new Vector(1, 1);

      if (((UFBoolean)this.m_ariItemShow.get(this.BEGNUM)).booleanValue()) {
        vColTitle.addElement(NCLangRes.getInstance().getStrByID("common", "UC000-0002282"));

        vColKey.addElement("nbeginnum");
      }
      if (bShowAst) {
        vColTitle.addElement(NCLangRes.getInstance().getStrByID("common", "UC000-0003971"));

        vColKey.addElement("nbeginastnum");
      }
      if (((UFBoolean)this.m_ariItemShow.get(this.BEGPRICE)).booleanValue()) {
        vColTitle.addElement(NCLangRes.getInstance().getStrByID("common", "UC000-0000741"));

        vColKey.addElement("nbeginprice");
      }
      if (((UFBoolean)this.m_ariItemShow.get(this.BEGMNY)).booleanValue()) {
        vColTitle.addElement(NCLangRes.getInstance().getStrByID("common", "UC000-0004112"));

        vColKey.addElement("nbeginmny");
      }
      if (((UFBoolean)this.m_ariItemShow.get(this.BEGPPRICE)).booleanValue()) {
        vColTitle.addElement(NCLangRes.getInstance().getStrByID("common", "UC000-0003474"));

        vColKey.addElement("nbeginplanedprice");
      }
      if (((UFBoolean)this.m_ariItemShow.get(this.BEGPMNY)).booleanValue()) {
        vColTitle.addElement(NCLangRes.getInstance().getStrByID("common", "UC000-0003522"));

        vColKey.addElement("nbeginplanedmny");
      }
      if (((UFBoolean)this.m_ariItemShow.get(this.BEGVMNY)).booleanValue()) {
        vColTitle.addElement(NCLangRes.getInstance().getStrByID("common", "UC000-0001697"));

        vColKey.addElement("nbeginvarymny");
      }
      String[] saColTitle = new String[vColTitle.size()];
      String[] saColKey = new String[vColKey.size()];
      vColTitle.copyInto(saColTitle);
      vColKey.copyInto(saColKey);

      procFieldGroupData(-1, vBillItem, vFieldGroup, sTopTitle, sTopTitle, saColKey, saColTitle, true);

      vColKey.clear();
      vColTitle.clear();
      sTopTitle = NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000013");

      if (((UFBoolean)this.m_ariItemShow.get(this.INNUM)).booleanValue()) {
        vColKey.addElement("rnnum");
        vColTitle.addElement(NCLangRes.getInstance().getStrByID("common", "UC000-0002282"));
      }

      if (bShowAst) {
        vColTitle.addElement(NCLangRes.getInstance().getStrByID("common", "UC000-0003971"));

        vColKey.addElement("rnastnum");
      }
      if (((UFBoolean)this.m_ariItemShow.get(this.INPRICE)).booleanValue()) {
        vColTitle.addElement(NCLangRes.getInstance().getStrByID("common", "UC000-0000741"));

        vColKey.addElement("rnprice");
      }
      if (((UFBoolean)this.m_ariItemShow.get(this.INMNY)).booleanValue()) {
        vColTitle.addElement(NCLangRes.getInstance().getStrByID("common", "UC000-0004112"));

        vColKey.addElement("rnmny");
      }
      if (((UFBoolean)this.m_ariItemShow.get(this.INPPRICE)).booleanValue()) {
        vColTitle.addElement(NCLangRes.getInstance().getStrByID("common", "UC000-0003474"));

        vColKey.addElement("rnpprice");
      }
      if (((UFBoolean)this.m_ariItemShow.get(this.INPMNY)).booleanValue()) {
        vColTitle.addElement(NCLangRes.getInstance().getStrByID("common", "UC000-0003522"));

        vColKey.addElement("rnpmny");
      }
      if (((UFBoolean)this.m_ariItemShow.get(this.INVMNY)).booleanValue()) {
        vColTitle.addElement(NCLangRes.getInstance().getStrByID("common", "UC000-0001697"));

        vColKey.addElement("rnvarymny");
      }
      saColKey = new String[vColKey.size()];
      saColTitle = new String[vColTitle.size()];
      vColKey.copyInto(saColKey);
      vColTitle.copyInto(saColTitle);

      String sColName = null;
      for (int j = 0; j < iInRdCount; j++)
      {
        sColName = null;
        if ((htRdTypeNameIn == null) || (htRdTypeNameIn.size() <= 0)) {
          sColName = new String(NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000017"));
        }
        else if (htRdTypeNameIn.get(new Integer(j)) != null) {
          sColName = htRdTypeNameIn.get(new Integer(j)).toString().trim();
          if (sColName.equals("*_*")) {
            sColName = new String(NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000017"));
          }

        }

        procFieldGroupData(j, vBillItem, vFieldGroup, sTopTitle, sColName, saColKey, saColTitle, true);
      }

      FldgroupVO voFg = (FldgroupVO)vFieldGroup.lastElement();
      if (iInRdCount == 1)
      {
        voFg.setToplevelflag("Y");
        voFg.setItem2(null);
        voFg.setGrouptype("2");
      }

      sTopTitle = NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000014");

      vColKey.clear();
      vColTitle.clear();
      if (((UFBoolean)this.m_ariItemShow.get(this.OUTNUM)).booleanValue()) {
        vColKey.addElement("dnnum");
        vColTitle.addElement(NCLangRes.getInstance().getStrByID("common", "UC000-0002282"));
      }

      if (bShowAst) {
        vColTitle.addElement(NCLangRes.getInstance().getStrByID("common", "UC000-0003971"));

        vColKey.addElement("dnastnum");
      }
      if (((UFBoolean)this.m_ariItemShow.get(this.OUTPRICE)).booleanValue()) {
        vColTitle.addElement(NCLangRes.getInstance().getStrByID("common", "UC000-0000741"));

        vColKey.addElement("dnprice");
      }
      if (((UFBoolean)this.m_ariItemShow.get(this.OUTMNY)).booleanValue()) {
        vColTitle.addElement(NCLangRes.getInstance().getStrByID("common", "UC000-0004112"));

        vColKey.addElement("dnmny");
      }
      if (((UFBoolean)this.m_ariItemShow.get(this.OUTPPRICE)).booleanValue()) {
        vColTitle.addElement(NCLangRes.getInstance().getStrByID("common", "UC000-0003474"));

        vColKey.addElement("dnpprice");
      }
      if (((UFBoolean)this.m_ariItemShow.get(this.OUTPMNY)).booleanValue()) {
        vColTitle.addElement(NCLangRes.getInstance().getStrByID("common", "UC000-0003522"));

        vColKey.addElement("dnpmny");
      }
      if (((UFBoolean)this.m_ariItemShow.get(this.OUTVMNY)).booleanValue()) {
        vColTitle.addElement(NCLangRes.getInstance().getStrByID("common", "UC000-0001697"));

        vColKey.addElement("dnvarymny");
      }
      saColKey = new String[vColKey.size()];
      saColTitle = new String[vColTitle.size()];
      vColKey.copyInto(saColKey);
      vColTitle.copyInto(saColTitle);
      for (int j = 0; j < iOutRdCount; j++)
      {
        sColName = null;
        if ((htRdTypeNameOut == null) || (htRdTypeNameOut.size() <= 0)) {
          sColName = new String(NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000018"));
        }
        else if (htRdTypeNameOut.get(new Integer(j)) != null) {
          sColName = htRdTypeNameOut.get(new Integer(j)).toString();
          if (sColName.equals("*_*")) {
            sColName = new String(NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000018"));
          }

        }

        procFieldGroupData(j, vBillItem, vFieldGroup, sTopTitle, sColName, saColKey, saColTitle, true);
      }

      FldgroupVO voFg2 = (FldgroupVO)vFieldGroup.lastElement();

      if (iOutRdCount == 1)
      {
        voFg2.setToplevelflag("Y");
        voFg2.setItem2(null);
        voFg2.setGrouptype("2");
      }

      sTopTitle = NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000015");

      vColKey.clear();
      vColTitle.clear();
      if (((UFBoolean)this.m_ariItemShow.get(this.ABNUM)).booleanValue()) {
        vColTitle.addElement(NCLangRes.getInstance().getStrByID("common", "UC000-0002282"));

        vColKey.addElement("nabnum");
      }
      if (bShowAst) {
        vColTitle.addElement(NCLangRes.getInstance().getStrByID("common", "UC000-0003971"));

        vColKey.addElement("nabastnum");
      }
      if (((UFBoolean)this.m_ariItemShow.get(this.ABPRICE)).booleanValue()) {
        vColTitle.addElement(NCLangRes.getInstance().getStrByID("common", "UC000-0000741"));

        vColKey.addElement("nabprice");
      }
      if (((UFBoolean)this.m_ariItemShow.get(this.ABMNY)).booleanValue()) {
        vColTitle.addElement(NCLangRes.getInstance().getStrByID("common", "UC000-0004112"));

        vColKey.addElement("nabmny");
      }
      if (((UFBoolean)this.m_ariItemShow.get(this.ABPPRICE)).booleanValue()) {
        vColTitle.addElement(NCLangRes.getInstance().getStrByID("common", "UC000-0003474"));

        vColKey.addElement("nabplanedprice");
      }
      if (((UFBoolean)this.m_ariItemShow.get(this.ABPMNY)).booleanValue()) {
        vColTitle.addElement(NCLangRes.getInstance().getStrByID("common", "UC000-0003522"));

        vColKey.addElement("nabplanedmny");
      }
      if (((UFBoolean)this.m_ariItemShow.get(this.ABVMNY)).booleanValue()) {
        vColTitle.addElement(NCLangRes.getInstance().getStrByID("common", "UC000-0001697"));

        vColKey.addElement("nabvarymny");
      }
      saColKey = new String[vColKey.size()];
      saColTitle = new String[vColTitle.size()];
      vColKey.copyInto(saColKey);
      vColTitle.copyInto(saColTitle);

      procFieldGroupData(-1, vBillItem, vFieldGroup, sTopTitle, sTopTitle, saColKey, saColTitle, true);

      ReportItem[] riaResult = new ReportItem[vBillItem.size()];
      vBillItem.copyInto(riaResult);

      FldgroupVO[] voaFg = new FldgroupVO[vFieldGroup.size()];
      vFieldGroup.copyInto(voaFg);

      getReportPanel().setFieldGroup(voaFg);
      getReportPanel().setBody_Items(riaResult);
      getReportPanel().updateValue();
    }
    catch (Exception e) {
      handleException(e);
    }
  }
}