package nc.ui.ia.analyze;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;
import nc.ui.bd.b14.InvclBO_Client;
import nc.ui.ia.ia501.QueryPrintDataSource;
import nc.ui.ia.pub.CommonDataBO_Client;
import nc.ui.ia.pub.IAEnvironment;
import nc.ui.ia.pub.IATableModel;
import nc.ui.ia.pub.IATableSelectionEvent;
import nc.ui.ia.pub.IAUITable;
import nc.ui.ia.pub.IIATableSelectionListener;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.UITablePane;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.print.IDataSource;
import nc.ui.pub.print.PrintEntry;
import nc.ui.pub.report.ReportBaseClass;
import nc.ui.pub.report.ReportItem;
import nc.ui.scm.pub.ArryFormula;
import nc.ui.scm.pub.print.ScmPrintTool;
import nc.ui.scm.pub.report.FilterRecordDlg;
import nc.ui.scm.pub.report.MultiSortDlg;
import nc.ui.scm.pub.report.OrientDialog;
import nc.ui.scm.pub.report.ShowItemsDlg;
import nc.ui.scm.pub.report.SubTotalRecordDlg;
import nc.vo.bd.b14.InvclVO;
import nc.vo.ia.analyze.InvInOutSumVO;
import nc.vo.ia.analyze.RdtypeSumVO;
import nc.vo.ia.analyze.StatisticsVO;
import nc.vo.ia.pub.ConstVO;
import nc.vo.ia.pub.Log;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.report.ReportModelVO;
import nc.vo.pub.report.SubtotalContext;
import nc.vo.sm.UserVO;

public abstract class IaAnalyzeClientUI extends ToftPanel
  implements IIATableSelectionListener
{
  protected InvclVO[] m_voaInvcl = null;

  protected Vector assData = new Vector();

  protected IAEnvironment ce = new IAEnvironment();

  protected Vector comData = new Vector();

  protected ReportItem[] m_riaDefault = null;

  protected UITablePane ivjUITablePane1 = null;
  private int[] m_iaClassLevel;
  protected int[] m_aryiPrecision;
  public ButtonObject m_btnColumnSet = new ButtonObject(NCLangRes.getInstance().getStrByID("201490", "UPT201490-000001"), NCLangRes.getInstance().getStrByID("201490", "UPT201490-000001"), 2, "栏目");

  protected ButtonObject m_btnDirectPrint = new ButtonObject(NCLangRes.getInstance().getStrByID("201490", "UPT201490-000002"), NCLangRes.getInstance().getStrByID("201490", "UPP201490-000015"), 2, "直接打印");

  public ButtonObject m_btnExcel = new ButtonObject(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000434"), NCLangRes.getInstance().getStrByID("201490", "UPP201490-000020"), 2, "EXCEL输出");

  public ButtonObject m_btnExcelInterFace = new ButtonObject(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000435"), NCLangRes.getInstance().getStrByID("201490", "UPP201490-000021"), 2, "界面格式输出");

  public ButtonObject m_btnExcelPrint = new ButtonObject(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000436"), NCLangRes.getInstance().getStrByID("201490", "UPP201490-000022"), 2, "打印模板输出");

  public ButtonObject m_btnFilter = new ButtonObject(NCLangRes.getInstance().getStrByID("201490", "UPT201490-000003"), NCLangRes.getInstance().getStrByID("201490", "UPP201490-000016"), 2, "过滤");

  protected ButtonObject m_btnFormat = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC000-0003261"), NCLangRes.getInstance().getStrByID("201490", "UPP201490-000017"), 2, "统计依据");

  public ButtonObject m_btnLocate = new ButtonObject(NCLangRes.getInstance().getStrByID("201490", "UPT201490-000004"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000089"), 2, "定位");

  protected ButtonObject m_btnPrint = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000007"), NCLangRes.getInstance().getStrByID("common", "UC001-0000007"), 2, "打印");

  protected ButtonObject m_btnQuery = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000006"), NCLangRes.getInstance().getStrByID("201490", "UPP201490-000018"), 2, "查询");

  public ButtonObject m_btnSort = new ButtonObject(NCLangRes.getInstance().getStrByID("201490", "UPT201490-000005"), NCLangRes.getInstance().getStrByID("201490", "UPP201490-000019"), 2, "排序");

  public ButtonObject m_btnSubTotal = new ButtonObject(NCLangRes.getInstance().getStrByID("201490", "UPT201490-000006"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000285"), 2, "小计");

  protected ButtonObject[] m_arybtnGroup = { this.m_btnQuery, this.m_btnLocate, this.m_btnFormat, this.m_btnColumnSet, this.m_btnFilter, this.m_btnSort, this.m_btnSubTotal, this.m_btnDirectPrint, this.m_btnPrint, this.m_btnExcel };

  protected ShowItemsDlg m_dlgColSet = null;

  protected FilterRecordDlg m_dlgFilter = null;

  protected FormatDialog m_dlgFormat = null;

  protected MultiSortDlg m_dlgMultiSort = null;

  protected OrientDialog m_dlgOrient = null;

  protected SubTotalRecordDlg m_dlgSubTotal = null;

  protected Object[][] m_formatData = (Object[][])null;

  protected Hashtable<String, String> m_htbBodyItemKey = null;

  private boolean m_isLocated = false;

  private boolean m_isTotalAst = false;

  protected IATableModel m_mTableModel = null;

  protected String m_sCorpID = "0001";

  protected String m_sNodeCode = null;

  protected String m_sTitle = null;

  protected String m_SUserID = null;

  protected String m_sVOName = "nc.vo.ia.analyze.InvInOutSumVO";

  protected IAUITable m_tUITable = null;

  protected StatisticsVO[] m_voaFormat = null;

  protected Vector m_vTableData = new Vector();

  protected Vector m_vTableHead = new Vector();
  protected String xsfp;
  protected String[] strColKeys = null;

  protected String[] strRowKeys = null;

  protected String[] strValKeys = null;

  protected boolean m_bDlgSubTotal = false;

  protected boolean m_bSort = false;

  protected boolean m_bOrient = false;

  protected boolean m_bFilter = false;

  protected boolean m_bColumnSet = false;

  public IaAnalyzeClientUI()
  {
    initialize();
  }

  public void afterEdit(IATableSelectionEvent e)
  {
  }

  protected boolean afteResetCol(boolean bDlgSubTotal, boolean bSort, boolean bOrient, boolean bFilter, boolean bColSet)
  {
    if (bDlgSubTotal)
    {
      getDlgSubTotal().initData();

      this.m_bDlgSubTotal = false;
    }

    if (bSort)
    {
      if (this.m_dlgMultiSort == null) {
        this.m_dlgMultiSort = new MultiSortDlg(this, getReportPanel());
      }
      this.m_dlgMultiSort.initData();

      this.m_bSort = false;
    }

    if (bOrient)
    {
      if (this.m_dlgOrient == null) {
        this.m_dlgOrient = new OrientDialog(this, getReportPanel());
      }
      this.m_dlgOrient.initData();

      this.m_bOrient = false;
    }

    if (bFilter)
    {
      if (this.m_dlgFilter == null) {
        this.m_dlgFilter = new FilterRecordDlg(this, getReportPanel());
      }
      this.m_dlgFilter.setItems(getReportPanel().getBodyItems());

      this.m_bFilter = false;
    }

    if (bColSet)
    {
      if (this.m_dlgColSet == null) {
        this.m_dlgColSet = new ShowItemsDlg(this, getReportPanel());
      }
      this.m_dlgColSet.setItems(getReportPanel().getBodyItems());

      this.m_bColumnSet = false;
    }

    return true;
  }

  protected RdtypeSumVO[] combinerdRDType(RdtypeSumVO[] lastrvos, RdtypeSumVO[] newrvos)
  {
    if ((lastrvos == null) && (newrvos == null))
    {
      return null;
    }

    if ((lastrvos == null) && (newrvos != null))
    {
      return newrvos;
    }

    if ((lastrvos != null) && (newrvos == null))
    {
      return lastrvos;
    }

    String[] sNames = new String[0];

    if (lastrvos.length > 0) {
      for (int i = 0; i < lastrvos.length; i++) {
        if (lastrvos[i] != null) {
          sNames = lastrvos[i].getAttributeNames();
          break;
        }
      }

    }

    for (int i = 0; i < lastrvos.length; i++) {
      if ((lastrvos[i] == null) && (newrvos[i] == null))
      {
        continue;
      }
      if ((lastrvos[i] == null) && (newrvos[i] != null)) {
        lastrvos[i] = newrvos[i];
      }
      else
      {
        if ((lastrvos[i] != null) && (newrvos[i] == null))
        {
          continue;
        }

        for (int j = 0; j < sNames.length; j++) {
          Object oValue = newrvos[i].getAttributeValue(sNames[j]);
          Object oLastValue = lastrvos[i].getAttributeValue(sNames[j]);

          if ((!sNames[j].startsWith("n")) || (sNames[j].indexOf("price") >= 0) || (oValue == null))
            continue;
          UFDouble dValue = new UFDouble(oValue.toString());
          UFDouble dLastValue = new UFDouble(oLastValue.toString());

          lastrvos[i].setAttributeValue(sNames[j], dValue.add(dLastValue));
        }
      }
    }

    return lastrvos;
  }

  private Hashtable getBodyItemKey()
  {
    if (this.m_htbBodyItemKey == null) {
      this.m_htbBodyItemKey = new Hashtable();

      ReportModelVO[] aryReportModels = getReportPanel().getAllBodyVOs();

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

    return this.m_htbBodyItemKey;
  }

  protected final SubTotalRecordDlg getDlgSubTotal()
  {
    if (this.m_dlgSubTotal == null) {
      if (!this.m_isTotalAst) {
        this.m_dlgSubTotal = new SubTotalRecordDlg(this, getReportPanel());
      }
      else {
        this.m_dlgSubTotal = new SubTotalRecordDlg(this, getReportPanel(), true);
      }
    }
    return this.m_dlgSubTotal;
  }

  protected FormatDialog getFormatDlg()
  {
    return this.m_dlgFormat;
  }

  protected ReportBaseClass getReportPanel()
  {
    return null;
  }

  protected abstract String getNodeCode();

  protected String[] getSortKeys()
  {
    ArrayList vGrpFormat = new ArrayList();
    for (int i = 0; i < getReportPanel().getBodyShowItems().length; i++) {
      if ((getReportPanel().getBodyShowItems()[i].getDataType() != 0) || 
        (getReportPanel().getBodyShowItems()[i].getKey().indexOf("code") <= 0)) continue;
      vGrpFormat.add(getReportPanel().getBodyShowItems()[i].getKey());
    }

    String[] sortKeys = null;
    if (vGrpFormat.size() >= 2) {
      sortKeys = new String[vGrpFormat.size() - 1];
      for (int i = 0; i < sortKeys.length; i++) {
        sortKeys[i] = vGrpFormat.get(i).toString();
      }
    }
    return sortKeys;
  }

  protected String[] getSumCols()
  {
    Vector vResult = new Vector();
    ReportItem[] items = getReportPanel().getBody_Items();
    for (int i = 0; i < items.length; i++) {
      String sKey = items[i].getKey();
      if ((sKey == null) || ((sKey.indexOf("num") < 0) && (sKey.indexOf("mny") < 0) && (sKey.indexOf("JE") < 0) && (sKey.indexOf("SL") < 0))) {
        continue;
      }
      vResult.addElement(sKey);
    }

    String[] saTemp = new String[vResult.size()];
    vResult.copyInto(saTemp);
    return saTemp;
  }

  public String getTitle()
  {
    return this.m_sTitle;
  }

  protected void getTotal()
  {
    SubtotalContext stctx = new SubtotalContext();
    String[] saGrpKey = getSortKeys();
    stctx.setGrpKeys(saGrpKey);
    boolean bSubtotal = true;
    if ((saGrpKey == null) || (saGrpKey.length < 1)) {
      bSubtotal = false;
    }
    stctx.setIsSubtotal(bSubtotal);
    stctx.setSubtotalCols(getSumCols());
    String sKey = getReportPanel().getBodyShowItems()[0].getKey();
    if (!sKey.startsWith("n")) {
      stctx.setTotalNameColKeys(sKey);
      stctx.setSumtotalName(NCLangRes.getInstance().getStrByID("201490", "UPP201490-000024"));

      stctx.setSubtotalName(NCLangRes.getInstance().getStrByID("201490", "UPP201490-000025"));
    }

    getReportPanel().setSubtotalContext(stctx);
    getReportPanel().subtotal(false);
  }

  protected int getInvClassCodeLength() {
    return -1;
  }

  public UITablePane getUITablePane1()
  {
    if (this.ivjUITablePane1 == null) {
      try {
        this.ivjUITablePane1 = new UITablePane();
        this.ivjUITablePane1.setName("UITablePane1");

        this.m_tUITable = new IAUITable();
        this.ivjUITablePane1.setTable(this.m_tUITable);
        this.m_mTableModel = new IATableModel();
        this.m_tUITable.setModel(this.m_mTableModel);
        this.ivjUITablePane1.getTable().setAutoResizeMode(4);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUITablePane1;
  }

  public void handleException(Throwable exception)
  {
    Log.error(exception);
  }

  public void IATableCellEditorTableSelectionChanged(IATableSelectionEvent e)
  {
  }

  public void IATableSelectionChanged(IATableSelectionEvent e)
  {
  }

  private void initialize()
  {
    this.m_sNodeCode = getNodeCode();

    this.m_btnExcel.addChildButton(this.m_btnExcelInterFace);
    this.m_btnExcel.addChildButton(this.m_btnExcelPrint);

    setButtons(this.m_arybtnGroup);

    this.m_sCorpID = this.ce.getCorporationID();

    this.m_SUserID = this.ce.getUser().getPrimaryKey();
    try
    {
      this.m_aryiPrecision = this.ce.getDataPrecision(this.m_sCorpID);

      this.m_iaClassLevel = CommonDataBO_Client.getCodeSchemdule(this.m_sCorpID);
    }
    catch (Exception e) {
      e.printStackTrace();
      showErrorMessage(NCLangRes.getInstance().getStrByID("201490", "UPP201490-000023"));

      return;
    }
  }

  protected InvInOutSumVO[] makeDataByInvcl(InvInOutSumVO[] inVos)
  {
    Vector vTemp = new Vector();
    Vector vCode = new Vector();
    try
    {
      if (this.m_voaInvcl == null) {
        this.m_voaInvcl = InvclBO_Client.queryAll(this.m_sCorpID);
      }

      int iLevel = getInvClassCodeLength();

      int iInvclLength = 0;
      for (int i = 0; i < iLevel; i++) {
        iInvclLength += this.m_iaClassLevel[i];
      }

      for (int i = 0; i < inVos.length; i++) {
        String sKey = "";
        for (int m = 0; m < this.m_voaFormat.length; m++) {
          String sStatisticsCode = this.m_voaFormat[m].getFieldCode();

          if (sStatisticsCode.equals("cwarehouseid")) {
            Object oCode = inVos[i].getAttributeValue("storcode");
            if (oCode != null) {
              sKey = sKey + oCode.toString() + inVos[i].getAttributeValue("storname").toString();
            }

          }
          else if (sStatisticsCode.equals("crdcenterid")) {
            Object oCode = inVos[i].getAttributeValue("bodycode");
            if (oCode != null) {
              sKey = sKey + oCode.toString() + inVos[i].getAttributeValue("bodyname").toString();
            }

          }
          else if (sStatisticsCode.equals("pk_corp")) {
            Object oCode = inVos[i].getAttributeValue("corpcode");
            if (oCode != null) {
              sKey = sKey + oCode.toString() + inVos[i].getAttributeValue("corpname").toString();
            }

          }
          else if (sStatisticsCode.equals("cdeptid")) {
            Object oCode = inVos[i].getAttributeValue("deptcode");
            if (oCode != null) {
              sKey = sKey + oCode.toString() + inVos[i].getAttributeValue("deptname").toString();
            }

          }
          else if (sStatisticsCode.equals("cemployeeid")) {
            Object oCode = inVos[i].getAttributeValue("employeename");
            if (oCode != null) {
              sKey = sKey + oCode.toString();
            }
          }
          else if (sStatisticsCode.equals("vbillcode")) {
            Object oCode = inVos[i].getAttributeValue("vbillcode");
            if (oCode != null) {
              sKey = sKey + oCode.toString();
            }
          }
          else if (sStatisticsCode.equals("cprojectid")) {
            Object oCode = inVos[i].getAttributeValue("jobname");
            if (oCode != null) {
              sKey = sKey + oCode.toString();
            }
          }
          else if (sStatisticsCode.equals("cbilltypecode")) {
            Object oCode = inVos[i].getAttributeValue("billtypename");
            if (oCode != null) {
              sKey = sKey + oCode.toString();
            }
          }
          else if (sStatisticsCode.equals("cdispatchid")) {
            Object oCode = inVos[i].getAttributeValue("dispatchname");
            if (oCode != null) {
              sKey = sKey + oCode.toString();
            }
          }
          else if (sStatisticsCode.equals("cagentid")) {
            Object oCode = inVos[i].getAttributeValue("agentname");
            if (oCode != null) {
              sKey = sKey + oCode.toString();
            }
          }
          else if (sStatisticsCode.equals("ccustomvendorid")) {
            Object oCode = inVos[i].getAttributeValue("custname");
            if (oCode != null) {
              sKey = sKey + oCode.toString();
            }
          }
        }

        String sInvClassCode = inVos[i].getInvclasscode();

        if ((iInvclLength != 0) && (sInvClassCode.length() >= iInvclLength)) {
          sInvClassCode = sInvClassCode.substring(0, iInvclLength);
        }

        sKey = sKey + sInvClassCode;
        int iIndex = vCode.indexOf(sKey);
        if (iIndex == -1)
        {
          for (int j = 0; j < this.m_voaInvcl.length; j++) {
            if (this.m_voaInvcl[j].getInvclasscode().equals(sInvClassCode)) {
              inVos[i].setInvclasscode(sInvClassCode);
              inVos[i].setInvclassname(this.m_voaInvcl[j].getInvclassname());
              break;
            }

          }

          vTemp.addElement(inVos[i]);
          vCode.addElement(sKey);
        }
        else
        {
          InvInOutSumVO voLastData = (InvInOutSumVO)vTemp.elementAt(iIndex);
          String[] sNames = voLastData.getAttributeNames();
          for (int j = 0; j < sNames.length; j++) {
            Object oValue = inVos[i].getAttributeValue(sNames[j]);
            Object oLastValue = voLastData.getAttributeValue(sNames[j]);
            if ((!sNames[j].startsWith("n")) || (sNames[j].indexOf("price") >= 0) || (oValue == null))
              continue;
            UFDouble dValue = new UFDouble(oValue.toString());
            UFDouble dLastValue = new UFDouble(oLastValue == null ? "0" : oLastValue.toString());

            voLastData.setAttributeValue(sNames[j], dValue.add(dLastValue));
          }

          RdtypeSumVO[] lastrvos = voLastData.getRcl();
          RdtypeSumVO[] newrvos = inVos[i].getRcl();
          voLastData.setRcl(combinerdRDType(lastrvos, newrvos));

          RdtypeSumVO[] lastdvos = voLastData.getDcl();
          RdtypeSumVO[] newdvos = inVos[i].getDcl();
          voLastData.setDcl(combinerdRDType(lastdvos, newdvos));

          vTemp.setElementAt(voLastData, iIndex);
        }
      }

      if (vTemp.size() > 0) {
        inVos = new InvInOutSumVO[vTemp.size()];
        vTemp.copyInto(inVos);
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return inVos;
  }

  public void onButtonClicked(ButtonObject bo)
  {
    if (this.m_isLocated) {
      OrientDialog.clearOrientColor(getReportPanel().getBillTable());

      this.m_isLocated = false;
    }

    if ((getFormatDlg() != null) && (this.m_voaFormat == null)) {
      this.m_voaFormat = getFormatDlg().getStatistics();
    }

    if (bo == this.m_btnQuery) {
      onQuery();
    }
    else if (bo == this.m_btnPrint) {
      onPrint();
    }
    else if (bo == this.m_btnDirectPrint) {
      onDirectPrint();
    }
    else if (bo == this.m_btnFormat) {
      onFormat();
    }
    else if (bo == this.m_btnFilter)
    {
      afteResetCol(false, false, false, this.m_bFilter, false);

      onFilter();
    }
    else if (bo == this.m_btnColumnSet)
    {
      afteResetCol(false, false, false, false, this.m_bColumnSet);
      onColumnSet();

      showHintMessage(NCLangRes.getInstance().getStrByID("2014", "UPP2014-000640"));
    }
    else if (bo == this.m_btnSort)
    {
      afteResetCol(false, this.m_bSort, false, false, false);

      onSort();
    }
    else if (bo == this.m_btnSubTotal)
    {
      afteResetCol(this.m_bDlgSubTotal, false, false, false, false);

      onSubTotal();

      showHintMessage(NCLangRes.getInstance().getStrByID("2014", "UPP2014-000639"));
    }
    else if (bo == this.m_btnLocate)
    {
      afteResetCol(false, false, this.m_bOrient, false, false);
      onLocate();
      updateUI();
    }
    else if (bo == this.m_btnExcelInterFace) {
      onExcelInterFace();
    }
    else if (bo == this.m_btnExcelPrint) {
      onExcelPrint();
    }
  }

  protected void onColumnSet()
  {
    if (this.m_dlgColSet == null) {
      this.m_dlgColSet = new ShowItemsDlg(this, getReportPanel());
    }
    this.m_dlgColSet.showModal();
  }

  public void onDirectPrint()
  {
    getReportPanel().previewData();
  }

  protected void onExcelInterFace()
  {
    try
    {
      if (getReportPanel() != null) {
        ScmPrintTool.exportExcelDirectly(getReportPanel());
      }
      else {
        showErrorMessage(NCLangRes.getInstance().getStrByID("201490", "UPP201490-000027"));
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  protected void onExcelPrint()
  {
    try
    {
      if (getReportPanel() != null)
      {
        IDataSource dataSource = null;

        dataSource = new QueryPrintDataSource(getReportPanel().getBillModel().getBodyValueVOs(this.m_sVOName), getReportPanel().getBillData(), this.m_sNodeCode);

        PrintEntry print = new PrintEntry(null, null);

        print.setTemplateID(this.m_sCorpID, this.m_sNodeCode, this.m_SUserID, null);

        ScmPrintTool.exportExcelByPrintTemplet(print, dataSource);
      }
      else
      {
        showErrorMessage(NCLangRes.getInstance().getStrByID("201490", "UPP201490-000027"));
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  public void onFilter()
  {
    this.m_dlgFilter = new FilterRecordDlg(this, getReportPanel());
    int iReturnFlag = this.m_dlgFilter.showModal();
    if (iReturnFlag == 1) {
      getReportPanel().getBillModel().reCalcurateAll();

      showHintMessage(NCLangRes.getInstance().getStrByID("2014", "UPP2014-000636"));
    }
  }

  protected boolean onFormat()
  {
    if (this.m_dlgFormat.showModal() != 1) {
      return false;
    }

    this.m_voaFormat = this.m_dlgFormat.getStatistics();

    for (int i = 0; i < this.m_voaFormat.length; i++) {
      if ((this.m_voaFormat[i].isCross()) && (getReportPanel() != null))
      {
        for (int j = 0; j < getReportPanel().getBodyDataVO().length; j++) {
          if ((getReportPanel().getBodyDataVO()[j] instanceof InvInOutSumVO))
            ((InvInOutSumVO)getReportPanel().getBodyDataVO()[j]).setCrossCol(this.m_voaFormat[i].getFieldCode().trim());
        }
      }
      else
      {
        if ((getReportPanel() == null) || (getReportPanel().getBodyDataVO() == null)) {
          continue;
        }
        for (int j = 0; j < getReportPanel().getBodyDataVO().length; j++) {
          if ((getReportPanel().getBodyDataVO()[j] instanceof InvInOutSumVO)) {
            String s = ((InvInOutSumVO)getReportPanel().getBodyDataVO()[j]).getCrossCol();

            if (s.equals(this.m_voaFormat[i].getFieldCode())) {
              ((InvInOutSumVO)getReportPanel().getBodyDataVO()[j]).setCrossCol("");
            }
          }
        }
      }

    }

    showHintMessage(NCLangRes.getInstance().getStrByID("2014", "UPP2014-000641"));

    return true;
  }

  protected void onLocate()
  {
    this.m_dlgOrient = new OrientDialog(this, getReportPanel());
    this.m_dlgOrient.showModal();
    if (this.m_dlgOrient.getResult() == 1) {
      this.m_isLocated = true;

      showHintMessage(NCLangRes.getInstance().getStrByID("2014", "UPP2014-000637"));
    }
  }

  protected void onPrint()
  {
    try
    {
      if ((getReportPanel().getBodyDataVO() == null) || (getReportPanel().getBodyDataVO().length <= 0))
      {
        MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000059"), NCLangRes.getInstance().getStrByID("201490", "UPP201490-000028"));

        return;
      }
      if (this.m_voaFormat != null) {
        for (int i = 0; i < this.m_voaFormat.length; i++) {
          if (!this.m_voaFormat[i].isCross())
            continue;
          MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000059"), NCLangRes.getInstance().getStrByID("201490", "UPP201490-000035"));

          return;
        }

      }

      IDataSource dataSource = null;

      dataSource = new QueryPrintDataSource(getReportPanel().getBillModel().getBodyValueVOs(this.m_sVOName), getReportPanel().getBillData(), this.m_sNodeCode);

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
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000059"), NCLangRes.getInstance().getStrByID("201490", "UPP201490-000029"));
    }
  }

  protected void onQuery()
  {
    getDlgSubTotal().initData();

    if (this.m_dlgMultiSort == null) {
      this.m_dlgMultiSort = new MultiSortDlg(this, getReportPanel());
    }
    this.m_dlgMultiSort.initData();

    if (this.m_dlgOrient == null) {
      this.m_dlgOrient = new OrientDialog(this, getReportPanel());
    }
    this.m_dlgOrient.initData();

    if (this.m_dlgFilter == null) {
      this.m_dlgFilter = new FilterRecordDlg(this, getReportPanel());
    }
    this.m_dlgFilter.setItems(getReportPanel().getBodyItems());

    if (this.m_dlgColSet == null) {
      this.m_dlgColSet = new ShowItemsDlg(this, getReportPanel());
    }
    this.m_dlgColSet.setItems(getReportPanel().getBodyItems());
  }

  public void onSort()
  {
    ReportBaseClass report = getReportPanel();

    Vector tempData = report.getBillModel().getDataVector();

    if ((tempData == null) || (tempData.size() == 0)) {
      MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("201490", "UPP201490-000019"), NCLangRes.getInstance().getStrByID("201490", "UPP201490-000030"));

      return;
    }

    Vector filterData = new Vector();
    for (int i = 0; i < tempData.size(); i++) {
      Object object = tempData.elementAt(i);
      if (object != null) {
        Vector v = (Vector)object;
        boolean b = false;
        if ((v != null) && (v.size() > 0)) {
          for (int j = 0; j < v.size(); j++) {
            Object value = v.elementAt(j);
            if ((value != null) && (value.toString().trim().length() > 0)) {
              String s = value.toString();
              if ((s.indexOf(NCLangRes.getInstance().getStrByID("common", "UC000-0001146")) < 0) && (s.indexOf(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000285")) < 0))
              {
                continue;
              }
              b = true;
              break;
            }
          }

        }

        if (b)
          continue;
        filterData.addElement(v);
      }

    }

    if (filterData.size() == 0) {
      MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("201490", "UPP201490-000019"), NCLangRes.getInstance().getStrByID("201490", "UPP201490-000031"));

      return;
    }

    report.getBillModel().setDataVector(filterData);

    if (this.m_dlgMultiSort == null) {
      this.m_dlgMultiSort = new MultiSortDlg(this, report);
    }
    this.m_dlgMultiSort.showModal();

    if (!this.m_dlgMultiSort.isCloseOk())
    {
      report.getBillModel().setDataVector(tempData);
    }
    else
      showHintMessage(NCLangRes.getInstance().getStrByID("2014", "UPP2014-000638"));
  }

  public void onSubTotal()
  {
    getDlgSubTotal().showModal();
  }

  protected void reCalcSum()
  {
    if (getReportPanel() != null)
    {
      setPrecision();

      BillModel bm = getReportPanel().getBillModel();

      boolean bReCalcu = bm.isNeedCalculate();

      bm.setNeedCalculate(false);

      for (int i = 0; i < bm.getRowCount(); i++) {
        for (int j = 0; j < bm.getColumnCount(); j++) {
          Object oValue = bm.getValueAt(i, j);

          if ((oValue != null) && ((oValue instanceof UFDouble))) {
            UFDouble d = new UFDouble(oValue.toString()).setScale(getReportPanel().getBodyItems()[j].getDecimalDigits(), 4);

            bm.setValueAt(d, i, j);
          }
        }

      }

      for (int i = 0; i < getReportPanel().getBody_Items().length; i++) {
        String sKey = getReportPanel().getBody_Items()[i].getKey();

        if ((sKey.indexOf("num") == -1) && (sKey.indexOf("SL_") == -1) && (sKey.indexOf("quantity") == -1) && (sKey.indexOf("mny") == -1) && (sKey.indexOf("money") == -1) && (sKey.indexOf("JE_") == -1) && (sKey.indexOf("amount") == -1))
        {
          continue;
        }
        getReportPanel().getBody_Items()[i].setTatol(true);
      }

      bm.setNeedCalculate(bReCalcu);
      getReportPanel().getBillModel().reCalcurateAll();
    }
  }

  protected final CircularlyAccessibleValueObject[] setBodyDatabyFormulaItem(CircularlyAccessibleValueObject[] sourceVO, ArrayList arlItemField, boolean isSetReportData, boolean bSort)
  {
    if ((sourceVO == null) || (sourceVO.length == 0)) {
      return sourceVO;
    }

    ArryFormula arryFormula = new ArryFormula();
    ArrayList arlResult = arryFormula.checkHtbkey(arlItemField, getBodyItemKey());

    String sError = (String)arlResult.get(0);
    if (sError != null) {
      Log.info(sError);
    }

    String[] arysFormulaGet = (String[])(String[])arlResult.get(1);
    if (((arysFormulaGet == null) || (arysFormulaGet.length == 0)) && (!isSetReportData))
    {
      return sourceVO;
    }

    if ((IAEnvironment.isInDebug()) && 
      (arysFormulaGet != null)) {
      for (int i = 0; i < arysFormulaGet.length; i++) {
        Log.debug(arysFormulaGet[i]);
      }

    }

    if ((arysFormulaGet != null) && (arysFormulaGet.length > 0)) {
      getReportPanel().getBillModel().execFormulasWithVO(sourceVO, arysFormulaGet);
    }

    if (isSetReportData) {
      if (bSort)
      {
        ArrayList al = new ArrayList();
        String sColumn = this.m_voaFormat[0].getFieldCode();

        if ((sColumn.indexOf("invclass") != -1) || (sColumn.indexOf("invtype") != -1))
        {
          sColumn = "invclasscode";
        }
        else if (sColumn.indexOf("inv") != -1) {
          sColumn = "invcode";
        }
        else if (sColumn.indexOf("ware") != -1) {
          sColumn = "storcode";
        }
        else if (sColumn.indexOf("crdcenterid") != -1) {
          sColumn = "bodycode";
        }

        for (int i = 0; i < sourceVO.length; i++) {
          String sKey = (String)sourceVO[i].getAttributeValue(sColumn);
          int j = 0;

          for (j = 0; j < al.size(); j++) {
            CircularlyAccessibleValueObject cvo = (CircularlyAccessibleValueObject)al.get(j);

            String sThisKey = (String)cvo.getAttributeValue(sColumn);

            if ((sThisKey != null) && (sKey != null) && (sThisKey.compareTo(sKey) > 0))
            {
              break;
            }
          }

          al.add(j, sourceVO[i]);
        }

        if ((sourceVO[0] instanceof InvInOutSumVO)) {
          sourceVO = new InvInOutSumVO[al.size()];
          sourceVO = (InvInOutSumVO[])(InvInOutSumVO[])al.toArray(sourceVO);
        }
        else {
          sourceVO = new CircularlyAccessibleValueObject[al.size()];
          sourceVO = (CircularlyAccessibleValueObject[])(CircularlyAccessibleValueObject[])al.toArray(sourceVO);
        }
      }

      getReportPanel().setBodyDataVO(sourceVO, false);

      if (this.m_voaFormat != null)
      {
        for (int i = 0; i < this.m_voaFormat.length; i++) {
          if ((this.m_voaFormat[i].isCross()) && (getReportPanel() != null))
          {
            for (int j = 0; j < getReportPanel().getBodyDataVO().length; j++) {
              if ((getReportPanel().getBodyDataVO()[j] instanceof InvInOutSumVO)) {
                ((InvInOutSumVO)getReportPanel().getBodyDataVO()[j]).setCrossCol(this.m_voaFormat[i].getFieldCode().trim());
              }
            }
          }
          else
          {
            if ((getReportPanel() == null) || (getReportPanel().getBodyDataVO() == null)) {
              continue;
            }
            for (int j = 0; j < getReportPanel().getBodyDataVO().length; j++) {
              if ((getReportPanel().getBodyDataVO()[j] instanceof InvInOutSumVO)) {
                String s = ((InvInOutSumVO)getReportPanel().getBodyDataVO()[j]).getCrossCol();

                if (s.equals(this.m_voaFormat[i].getFieldCode())) {
                  ((InvInOutSumVO)getReportPanel().getBodyDataVO()[j]).setCrossCol("");
                }
              }
            }
          }
        }
      }

    }

    return sourceVO;
  }

  protected void showNOAndSum(boolean bShow)
  {
    if (getReportPanel() != null)
    {
      this.m_riaDefault = getReportPanel().getBody_Items();

      String[] sSumData = getReportPanel().getSums();
      if (sSumData != null) {
        for (int i = 0; i < sSumData.length; i++) {
          for (int j = 0; j < this.m_riaDefault.length; j++) {
            if (this.m_riaDefault[j].getKey().equals(sSumData[i])) {
              this.m_riaDefault[j].setTatol(true);
              break;
            }
          }
        }
      }

      getReportPanel().setRowNOShow(bShow);

      getReportPanel().setTatolRowShow(bShow);

      getReportPanel().setShowThMark(true);
    }
  }

  protected void setPrecision()
  {
    ReportItem[] m_vTableHead = getReportPanel().getBody_Items();

    for (int i = 0; i < m_vTableHead.length; i++)
      if (((m_vTableHead[i].getKey().indexOf("num") >= 0) || (m_vTableHead[i].getKey().indexOf("SL") >= 0) || (m_vTableHead[i].getKey().indexOf("number") >= 0) || (m_vTableHead[i].getKey().indexOf("quantity") > 0)) && (m_vTableHead[i].getKey().indexOf("assistnum") < 0) && (m_vTableHead[i].getKey().indexOf("FSL") < 0) && (m_vTableHead[i].getKey().indexOf("astnum") < 0))
      {
        getReportPanel().getBodyItems()[i].setDecimalDigits(this.m_aryiPrecision[0]);
      }
      else if ((m_vTableHead[i].getKey().indexOf("assistnum") >= 0) || (m_vTableHead[i].getKey().indexOf("FSL") >= 0) || (m_vTableHead[i].getKey().indexOf("astnum") >= 0))
      {
        getReportPanel().getBodyItems()[i].setDecimalDigits(this.m_aryiPrecision[4]);
      }
      else if ((m_vTableHead[i].getKey().indexOf("mny") >= 0) || (m_vTableHead[i].getKey().indexOf("JE") >= 0) || (m_vTableHead[i].getKey().indexOf("money") >= 0) || (m_vTableHead[i].getKey().indexOf("amount") > 0))
      {
        getReportPanel().getBodyItems()[i].setDecimalDigits(this.m_aryiPrecision[2]);
      }
      else
      {
        if ((m_vTableHead[i].getKey().indexOf("price") < 0) && (m_vTableHead[i].getKey().indexOf("DJ") < 0) && (m_vTableHead[i].getKey().indexOf("money") < 0)) {
          continue;
        }
        getReportPanel().getBodyItems()[i].setDecimalDigits(this.m_aryiPrecision[1]);
      }
  }

  protected void setBooleanValue(boolean bValue)
  {
    this.m_bDlgSubTotal = bValue;
    this.m_bSort = bValue;
    this.m_bOrient = bValue;
    this.m_bFilter = bValue;
    this.m_bColumnSet = bValue;
  }

  protected void setReportDef()
  {
    if (getReportPanel() != null)
    {
      String sSQL = " select ";
      sSQL = sSQL + " a.fieldname,c.defname,b.objname,c.type,c.lengthnum,c.digitnum ";

      sSQL = sSQL + " from ";
      sSQL = sSQL + " bd_defquote a,bd_defused b,bd_defdef c ";
      sSQL = sSQL + " where ";
      sSQL = sSQL + " a.pk_defused = b.pk_defused ";
      sSQL = sSQL + " and ";
      sSQL = sSQL + " c.pk_defdef = a.pk_defdef ";
      sSQL = sSQL + " and ";
      sSQL = sSQL + " b.objname in ('" + ConstVO.m_sDefHeadName + "','" + ConstVO.m_sDefBodyName + "')";

      sSQL = sSQL + " order by ";
      sSQL = sSQL + " b.objname,a.fieldname ";
      try {
        String[][] sResult = CommonDataBO_Client.queryData(sSQL);
        if (sResult != null) {
          for (int i = 0; i < sResult.length; i++) {
            String[] sTemp = sResult[i];
            if (sTemp.length <= 1)
              continue;
            String sFieldName = sTemp[0];
            String sDefName = sTemp[1];
            String sObjName = sTemp[2];

            String sFieldCode = "";

            if (sObjName.equals(ConstVO.m_sDefHeadName))
            {
              sFieldCode = "v" + sFieldName;
              getReportPanel().renameCol(sFieldCode, sDefName);
            }
            else
            {
              sFieldCode = "b" + sFieldName;

              getReportPanel().renameCol(sFieldCode, sDefName);
            }
          }
        }
      }
      catch (Exception ex)
      {
        Log.error(ex);
      }
    }
  }

  protected void exchangeColumn(ReportBaseClass reprot, int columnIndex1, int columnIndex2)
  {
    if ((columnIndex1 < 0) || (columnIndex2 < 0)) {
      return;
    }
    int start = columnIndex1;
    int end = columnIndex2;
    if (start > end) {
      start = columnIndex2;
      end = columnIndex1;
    }
    reprot.getBillTable().moveColumn(start, end);
    if (start + 1 < end)
      reprot.getBillTable().moveColumn(end - 1, start);
  }

  protected String getCrossColumnName(String statName)
  {
    if (statName.equalsIgnoreCase("pk_invcl")) {
      return "invclassname";
    }
    if (statName.equalsIgnoreCase("cinventoryid")) {
      return "invname";
    }
    if (statName.equalsIgnoreCase("crdcenterid")) {
      return "bodyname";
    }
    if (statName.equalsIgnoreCase("pk_corp")) {
      return "corpname";
    }
    if (statName.equalsIgnoreCase("cwarehouseid")) {
      return "storname";
    }
    if (statName.equalsIgnoreCase("cdeptid")) {
      return "deptname";
    }
    if (statName.equalsIgnoreCase("cemployeeid")) {
      return "employeename";
    }
    if (statName.equalsIgnoreCase("cagentid")) {
      return "agentname";
    }
    if (statName.equalsIgnoreCase("ccustomvendorid")) {
      return "custname";
    }
    if (statName.equalsIgnoreCase("cdispatchid")) {
      return "dispatchname";
    }
    if (statName.equalsIgnoreCase("cprojectid")) {
      return "jobname";
    }

    return statName;
  }
}