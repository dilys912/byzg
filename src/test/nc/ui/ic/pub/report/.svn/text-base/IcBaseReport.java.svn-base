package nc.ui.ic.pub.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import nc.ui.ic.pub.BillModelRelaSortData;
import nc.ui.ic.pub.print.PrintDataInterface;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillSortListener;
import nc.ui.pub.bill.IBillModelSortPrepareListener;
import nc.ui.pub.formulaparse.FormulaParse;
import nc.ui.pub.print.PrintEntry;
import nc.ui.pub.report.ReportBaseClass;
import nc.ui.pub.report.ReportItem;
import nc.ui.scm.pub.print.ScmPrintTool;
import nc.ui.scm.pub.report.FilterRecordDlg;
import nc.ui.scm.pub.report.MultiSortDlg;
import nc.ui.scm.pub.report.OrientDialog;
import nc.ui.scm.pub.report.ShowItemsDlg;
import nc.ui.scm.pub.report.SubTotalRecordDlg;
import nc.ui.scm.sourcebill.SourceBillFlowDlg;
import nc.vo.bd.CorpVO;
import nc.vo.ic.pub.lang.ResBase;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.SuperVOUtil;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.report.QryOrderVO;
import nc.vo.pub.report.ReportModelVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.sm.UserVO;

public abstract class IcBaseReport extends ToftPanel
  implements BillSortListener, IBillModelSortPrepareListener
{
  private ArrayList m_listshowitemkey;
  private ArrayList m_listhideitemkey;
  public boolean m_bEverQry = false;

  public ButtonObject m_btnDirectExport = new ButtonObject(ResBase.getExcelDirect(), ResBase.getExcelDirect(), 6, "直接打印输出");

  public ButtonObject m_btnTempletExport = new ButtonObject(ResBase.getExcelTemplet(), ResBase.getExcelTemplet(), 6, "模板Excel输出");

  public ButtonObject m_ExcelExport = new ButtonObject(ResBase.getExcelExport(), ResBase.getExcelExport(), 6, "Excel输出");

  public ButtonObject m_btnColSet = new ButtonObject(NCLangRes.getInstance().getStrByID("400820", "UPT400820-000005"), NCLangRes.getInstance().getStrByID("400820", "UPT400820-000005"), 2, "栏目");

  public ButtonObject m_btnDirectPreview = new ButtonObject(NCLangRes.getInstance().getStrByID("400820", "UPT400820-000004"), NCLangRes.getInstance().getStrByID("400820", "UPT400820-000004"), 5, "直接打印预览");

  public ButtonObject m_btnDirectPrint = new ButtonObject(NCLangRes.getInstance().getStrByID("400820", "UPT400820-000003"), NCLangRes.getInstance().getStrByID("400820", "UPT400820-000003"), 5, "直接打印");

  public ButtonObject m_btnFilter = new ButtonObject(NCLangRes.getInstance().getStrByID("400820", "UPT400820-000007"), NCLangRes.getInstance().getStrByID("400820", "UPT400820-000007"), 2, "过滤");

  public ButtonObject m_btnLocate = new ButtonObject(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000089"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000089"), 2, "定位");

  public ButtonObject m_btnPreview = new ButtonObject(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000305"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000305"), 5, "预览");

  public ButtonObject m_btnPrint = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000007"), NCLangRes.getInstance().getStrByID("common", "UC001-0000007"), 5, "打印");

  public ButtonObject m_btnSort = new ButtonObject(NCLangRes.getInstance().getStrByID("40080816", "UPT40080816-000040"), NCLangRes.getInstance().getStrByID("40080816", "UPT40080816-000040"), 2, "排序");

  public ButtonObject m_btnSubTotal = new ButtonObject(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000285"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000285"), 2, "小计");

  public ButtonObject m_btnRefresh = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000009"), NCLangRes.getInstance().getStrByID("common", "UC001-0000009"), 2, "刷新");

  ShowItemsDlg m_ColSetdlg = null;

  PrintDataInterface m_dataSource = null;

  SubTotalRecordDlg m_dlgSubTotal = null;
  private FormulaParse m_FormulaParse;
  private Hashtable m_htbBodyItemKey = null;

  private Hashtable m_htbCubasdocField = null;

  private Hashtable m_htbHeadItemKey = null;

  private boolean m_isLocated = false;

  private boolean m_isTotalAst = false;

  PrintEntry m_print = null;

  public ButtonObject m_PrintMng = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000007"), NCLangRes.getInstance().getStrByID("common", "UC001-0000007"), 5, "打印管理");

  public String m_sCorpID = null;

  public String m_sPNodeCode = null;

  public String m_sUserID = null;

  public QryOrderVO[] m_voaGroup = null;

  public QryOrderVO[] m_voaOrder = null;

  public String title = null;

  private ArrayList m_sortRelaDatas = new ArrayList(10);

  protected final UFDouble ZERO = new UFDouble(0.0D);
  public ButtonObject[] m_BtnGroup;

  public IcBaseReport()
  {
    this.m_PrintMng.addChildButton(this.m_btnPrint);
    this.m_PrintMng.addChildButton(this.m_btnPreview);
    this.m_PrintMng.addChildButton(this.m_btnDirectPrint);
    this.m_PrintMng.addChildButton(this.m_btnDirectPreview);

    this.m_ExcelExport.addChildButton(this.m_btnDirectExport);
    this.m_ExcelExport.addChildButton(this.m_btnTempletExport);

    this.m_BtnGroup = new ButtonObject[] { this.m_btnLocate, this.m_btnSort, this.m_PrintMng, this.m_ExcelExport, this.m_btnColSet, this.m_btnSubTotal, this.m_btnFilter, this.m_btnRefresh };
  }

  public IcBaseReport(String title)
  {
    this.m_PrintMng.addChildButton(this.m_btnPrint);
    this.m_PrintMng.addChildButton(this.m_btnPreview);
    this.m_PrintMng.addChildButton(this.m_btnDirectPrint);
    this.m_PrintMng.addChildButton(this.m_btnDirectPreview);

    this.m_ExcelExport.addChildButton(this.m_btnDirectExport);
    this.m_ExcelExport.addChildButton(this.m_btnTempletExport);

    this.m_BtnGroup = new ButtonObject[] { this.m_btnLocate, this.m_btnSort, this.m_PrintMng, this.m_ExcelExport, this.m_btnColSet, this.m_btnSubTotal, this.m_btnFilter, this.m_btnRefresh };

    this.title = title;
  }

  protected void calculateTotal()
  {
    if (getReportBaseClass() == null) {
      return;
    }
    resetTotalAttri();
    getReportBaseClass().getBillModel().reCalcurateAll();
  }

  private void resetTotalAttri()
  {
    ReportItem[] ri = getReportBaseClass().getBody_Items();
    String[] sa = getSums();

    if ((sa == null) || (ri == null)) return;
    HashMap map = new HashMap();
    for (int m = 0; m < sa.length; m++) {
      if (sa[m] != null)
        map.put(sa[m], sa[m]);
    }
    for (int i = 0; i < ri.length; i++) {
      if ((ri == null) || 
        (!map.containsKey(ri[i].getKey()))) continue;
      ri[i].setTatol(true);
    }
  }

  public String[] getSums()
  {
    String[] sa = getReportBaseClass().getSums();
    return sa;
  }

  private Hashtable getBodyItemKey()
  {
    if (this.m_htbBodyItemKey == null) {
      this.m_htbBodyItemKey = new Hashtable();

      ReportModelVO[] aryReportModelVO = getReportBaseClass().getAllBodyVOs();

      String sColumnCode = null;
      int iSelectType = 1;
      for (int i = 0; i < aryReportModelVO.length; i++) {
        sColumnCode = aryReportModelVO[i].columnCode;
        iSelectType = aryReportModelVO[i].selectType.intValue();

        if ((sColumnCode == null) || (sColumnCode.length() <= 0) || (iSelectType == 0))
          continue;
        this.m_htbBodyItemKey.put(sColumnCode, sColumnCode);
      }
    }

    return this.m_htbBodyItemKey;
  }

  public ButtonObject getBtnCloset()
  {
    return this.m_btnColSet;
  }

  public ButtonObject getBtnDrtPrint()
  {
    return this.m_btnDirectPrint;
  }

  public ButtonObject getBtnFilter()
  {
    return this.m_btnFilter;
  }

  public ButtonObject getBtnSort()
  {
    return this.m_btnSort;
  }

  public ButtonObject getBtnSubtotal()
  {
    return this.m_btnSubTotal;
  }

  public ButtonObject[] getButtonArray(ButtonObject[] btns)
  {
    int count = 0;
    if ((this.m_BtnGroup != null) && (this.m_BtnGroup.length > 0))
      count = this.m_BtnGroup.length;
    ButtonObject[] btnAry = new ButtonObject[btns.length + count];

    for (int i = 0; i < btns.length; i++) {
      btnAry[i] = btns[i];
    }
    for (int i = btns.length; i < btns.length + count; i++) {
      btnAry[i] = this.m_BtnGroup[(i - btns.length)];
    }

    return btnAry;
  }

  public abstract String getCorpID();

  protected PrintDataInterface getDataSource()
  {
    if (null == this.m_dataSource) {
      this.m_dataSource = new PrintDataInterface();
      BillData bd = getReportBaseClass().getBillData();
      this.m_dataSource.setBillData(bd);
      this.m_dataSource.setModuleName(getPNodeCode());
      this.m_dataSource.setTotalLinesInOnePage(getPrintEntry().getBreakPos());
    }
    return this.m_dataSource;
  }

  public abstract String getDefaultPNodeCode();

  protected final SubTotalRecordDlg getDlgSubTotal()
  {
    if (this.m_dlgSubTotal == null) {
      if (!this.m_isTotalAst) {
        this.m_dlgSubTotal = new SubTotalRecordDlg(this, getReportBaseClass());
      }
      else {
        this.m_dlgSubTotal = new SubTotalRecordDlg(this, getReportBaseClass(), true);
      }
    }
    return this.m_dlgSubTotal;
  }

  private FormulaParse getFormulaParse()
  {
    if (this.m_FormulaParse == null) {
      this.m_FormulaParse = new FormulaParse();
    }
    return this.m_FormulaParse;
  }

  public QryOrderVO[] getGroupVO()
  {
    this.m_voaGroup = getReportBaseClass().getGroupVOs();
    if (this.m_voaGroup == null) {
      this.m_voaGroup = new QryOrderVO[1];
      this.m_voaGroup[0] = new QryOrderVO();
      this.m_voaGroup[0].setQryfld("cinventoryid");
    }
    return this.m_voaGroup;
  }

  public QryOrderVO[] getGroupVO(String[] sGroupTableName)
  {
    getGroupVO();
    setGroupTableName(sGroupTableName);
    return this.m_voaGroup;
  }

  private Hashtable getHeadItemKey()
  {
    if (this.m_htbHeadItemKey == null) {
      this.m_htbHeadItemKey = new Hashtable();
      ReportModelVO[] aryReportModelVO = getReportBaseClass().getHeadVOs();

      String sColumnCode = null;
      for (int i = 0; i < aryReportModelVO.length; i++) {
        sColumnCode = aryReportModelVO[i].columnCode;
        if ((sColumnCode != null) && (sColumnCode.length() > 0))
          this.m_htbHeadItemKey.put(sColumnCode, sColumnCode);
      }
    }
    return this.m_htbHeadItemKey;
  }

  public QryOrderVO[] getOrderVO()
  {
    this.m_voaOrder = getReportBaseClass().getOrderVOs();
    if (this.m_voaOrder == null) {
      this.m_voaOrder = new QryOrderVO[1];
      this.m_voaOrder[0] = new QryOrderVO();
      this.m_voaOrder[0].setQryfld("cinventoryid");
    }
    return this.m_voaOrder;
  }

  public QryOrderVO[] getOrderVO(String[] sOrderTableName)
  {
    getOrderVO();
    setOrderTableName(sOrderTableName);
    return this.m_voaOrder;
  }

  public String getPNodeCode()
  {
    try
    {
      this.m_sPNodeCode = getModuleCode();
    } catch (Exception e) {
      SCMEnv.out(e.getMessage());
    }

    if ((this.m_sPNodeCode == null) || (this.m_sPNodeCode.trim().length() == 0)) {
      this.m_sPNodeCode = getDefaultPNodeCode();
    }
    return this.m_sPNodeCode;
  }

  protected PrintEntry getPrintEntry()
  {
    if (null == this.m_print) {
      this.m_print = new PrintEntry(null, null);
      this.m_print.setTemplateID(getCorpID(), getPNodeCode(), getUserID(), null);
    }

    return this.m_print;
  }

  public abstract ReportBaseClass getReportBaseClass();

  public abstract AggregatedValueObject getReportVO();

  public String getTitle()
  {
    String t = getReportBaseClass().getTitle();
    if ((t == null) || (t.trim().length() == 0))
      t = this.title;
    return t;
  }

  public abstract String getUserID();

  public void hiddenShowColumn(boolean bShow, String sColumnItemkey)
  {
    if (sColumnItemkey == null)
      return;
    if (!bShow)
      getReportBaseClass().hideColumn(sColumnItemkey);
    else
      getReportBaseClass().showHiddenColumn(sColumnItemkey);
  }

  public void hiddenShowColumn(boolean bShow, String[] sColumnItemkey)
  {
    if (sColumnItemkey == null)
      return;
    if (!bShow)
      getReportBaseClass().hideColumn(sColumnItemkey);
    else
      getReportBaseClass().showHiddenColumn(sColumnItemkey);
  }

  protected void hideButton(ButtonObject btnObject)
  {
    if (btnObject != null) {
      btnObject.setVisible(false);
      updateButtons();
    }
  }

  protected void hideButtons(ButtonObject[] btnObjects)
  {
    if ((btnObjects != null) || (btnObjects.length > 0)) {
      for (int i = 0; i < btnObjects.length; i++) {
        btnObjects[i].setVisible(false);
      }
      updateButtons();
    }
  }

  public void initButtons()
  {
    setButtons(this.m_BtnGroup);
    setBtnState();
  }

  public void onButtonClicked(ButtonObject bo)
  {
    showHintMessage(bo.getName());
    if (this.m_isLocated) {
      OrientDialog.clearOrientColor(getReportBaseClass().getBillTable());

      this.m_isLocated = false;
    }
    if (bo == this.m_btnColSet)
      onColSet();
    else if (bo == this.m_btnDirectPreview)
      onDirectPreview();
    else if (bo == this.m_btnDirectPrint)
      onDirectPrint();
    else if (bo == this.m_btnPreview)
      onPreview();
    else if (bo == this.m_btnPrint)
      onPrint();
    else if (bo == this.m_btnFilter)
      onFilter();
    else if (bo == this.m_btnSubTotal) {
      onSubTotal();
    }
    else if (bo == this.m_btnSort)
      onSort();
    else if (bo == this.m_btnLocate)
      onLocate();
    else if (bo == this.m_btnDirectExport)
      onExportExcelDirect();
    else if (bo == this.m_btnTempletExport)
      onExportExcelTemplet();
    else if (bo == this.m_btnRefresh) {
      onQuery(false);
    }
    setBtnState();
  }

  protected void onExportExcelDirect()
  {
    try
    {
      ScmPrintTool.exportExcelDirectly(getReportBaseClass());
    }
    catch (Exception e)
    {
      SCMEnv.error(e);
    }
  }

  protected void onExportExcelTemplet()
  {
    AggregatedValueObject vo = getReportVO();
    if ((null == vo) || (vo.getChildrenVO() == null) || (vo.getChildrenVO().length == 0) || (vo.getChildrenVO()[0] == null))
    {
      return;
    }
    getDataSource().setVO(vo);

    int ret = getPrintEntry().selectTemplate();
    try
    {
      if (ret > 0)
      {
        ScmPrintTool.exportExcelByPrintTemplet(getPrintEntry(), getDataSource());
      }

    }
    catch (Exception e)
    {
      SCMEnv.error(e);
    }
  }

  public void onColSet()
  {
    if (this.m_ColSetdlg == null) {
      this.m_ColSetdlg = new ShowItemsDlg(this, getReportBaseClass());
    }

    this.m_ColSetdlg.showModal();
  }

  public void onDirectPreview()
  {
    getReportBaseClass().previewData();
  }

  public void onDirectPrint()
  {
    getReportBaseClass().printData();
  }

  public void onFilter()
  {
    FilterRecordDlg dlg = new FilterRecordDlg(this, getReportBaseClass());

    if (dlg.showModal() == 1)
      calculateTotal();
  }

  protected void onJointCheck()
  {
    if (getReportBaseClass().getRowCount() <= 0)
      return;
    int iSelLine = getReportBaseClass().getBillTable().getSelectedRow();
    if (iSelLine < 0) {
      showHintMessage(NCLangRes.getInstance().getStrByID("4008other", "UPP4008other-000272"));

      return;
    }

    String sUserID = null;

    String sCorpID = null;
    ClientEnvironment ce = ClientEnvironment.getInstance();

    sUserID = ce.getUser().getPrimaryKey();
    sCorpID = ce.getCorporation().getPrimaryKey();

    String sBillPK = (String)getReportBaseClass().getBillModel().getValueAt(iSelLine, "cgeneralhid");

    String sBillTypeCode = (String)getReportBaseClass().getBillModel().getValueAt(iSelLine, "cbilltypecode");

    if ((sBillPK == null) || (sBillTypeCode == null)) {
      showHintMessage(NCLangRes.getInstance().getStrByID("4008other", "UPP4008other-000273"));

      return;
    }
    SourceBillFlowDlg soureDlg = new SourceBillFlowDlg(this, sBillTypeCode, sBillPK, null, sUserID, sCorpID);

    soureDlg.showModal();
  }

  protected void onLocate()
  {
    OrientDialog dlg = new OrientDialog(this, getReportBaseClass().getBillModel(), getReportBaseClass().getBody_Items(), getReportBaseClass().getBillTable());

    dlg.setReportBase(getReportBaseClass());
    dlg.showModal();
    if (dlg.getResult() == 1)
      this.m_isLocated = true;
  }

  public void onPreview()
  {
    AggregatedValueObject vo = getReportVO();
    if ((null == vo) || (vo.getChildrenVO() == null) || (vo.getChildrenVO().length == 0) || (vo.getChildrenVO()[0] == null))
    {
      return;
    }
    getDataSource().setVO(vo);

    getPrintEntry().selectTemplate();
    getPrintEntry().setDataSource(getDataSource());
    getPrintEntry().preview();
  }

  public void onPrint()
  {
    AggregatedValueObject vo = getReportVO();
    if ((null == vo) || (vo.getChildrenVO() == null) || (vo.getChildrenVO().length == 0) || (vo.getChildrenVO()[0] == null))
    {
      return;
    }
    getDataSource().setVO(vo);

    getPrintEntry().selectTemplate();
    getPrintEntry().setDataSource(getDataSource());
    getPrintEntry().print();
  }

  public abstract void onQuery(boolean paramBoolean);

  public void onSort()
  {
    ReportBaseClass report = getReportBaseClass();

    Vector tempData = report.getBillModel().getDataVector();

    if ((tempData == null) || (tempData.size() == 0)) {
      MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("4008other", "UPT40080816-000040"), NCLangRes.getInstance().getStrByID("4008other", "UPP4008other-000274"));

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
            if ((value == null) || (value.toString().trim().length() <= 0))
              continue;
            String s = value.toString();
            if ((s.indexOf("合计") >= 0) || (s.indexOf("小计") >= 0)) {
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
      MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("4008other", "UPT40080816-000040"), NCLangRes.getInstance().getStrByID("4008other", "UPP4008other-000275"));

      return;
    }

    report.getBillModel().setDataVector(filterData);

    MultiSortDlg dlg = new MultiSortDlg(this, report);

    dlg.showModal();

    if (!dlg.isCloseOk())
    {
      report.getBillModel().setDataVector(tempData);
    }
  }

  public void onSubTotal()
  {
    saveReportPanelShowState();

    if (!this.m_isTotalAst) {
      this.m_dlgSubTotal = new SubTotalRecordDlg(this, getReportBaseClass());
    }
    else {
      this.m_dlgSubTotal = new SubTotalRecordDlg(this, getReportBaseClass(), true);
    }

    getDlgSubTotal().showModal();
  }

  public String resetQryfld(String sQryfld)
  {
    if (sQryfld != null) {
      int iPosition = sQryfld.lastIndexOf(".");
      if (iPosition == -1)
        iPosition = 0;
      sQryfld = sQryfld.substring(iPosition + 1);
    }
    return sQryfld;
  }

  protected final void setBodyDatabyFormulaItem(CircularlyAccessibleValueObject[] sourceVO, ArrayList arylistItemField, boolean isSetReportData)
  {
    ArryFormula arryFormula = new ArryFormula();
    ArrayList arylistresult = arryFormula.checkHtbkey(arylistItemField, getBodyItemKey());

    String sError = (String)arylistresult.get(0);
    if (sError != null) {
      SCMEnv.out("nc.ui.ic.pub.report.IcBaseReport.getRDataByFormulakey:" + sError);
    }

    String[] arysFormulaGet = (String[])(String[])arylistresult.get(1);

    String[] arysOldFormula = getReportBaseClass().getFormulae();

    String[] aryCombineFormula = nc.vo.ic.pub.GenMethod.combineStringArray(arysFormulaGet, arysOldFormula);

    if ((SCMEnv.DEBUG) && 
      (aryCombineFormula != null)) {
      for (int i = 0; i < aryCombineFormula.length; i++) {
        SCMEnv.out(aryCombineFormula[i]);
      }
    }

    if ((aryCombineFormula != null) && (aryCombineFormula.length > 0)) {
      long lTime = System.currentTimeMillis();
      getReportBaseClass().getBillModel().execFormulasWithVO(sourceVO, aryCombineFormula);

      SCMEnv.showTime(lTime, "execFormulasWithVO:");
    }

    if (isSetReportData)
      getReportBaseClass().setBodyDataVO(sourceVO, false);
  }

  protected final void setBodyDatabyFormulaItem(CircularlyAccessibleValueObject[] sourceVO, ArrayList arylistItemField, String[] aryFormulaAppend, boolean isSetReportData)
  {
    ArryFormula arryFormula = new ArryFormula();
    ArrayList arylistresult = arryFormula.checkHtbkey(arylistItemField, getBodyItemKey());

    String sError = (String)arylistresult.get(0);
    if (sError != null) {
      SCMEnv.out("nc.ui.ic.pub.report.IcBaseReport.getRDataByFormulakey:" + sError);
    }

    String[] arysFormulaGet = (String[])(String[])arylistresult.get(1);

    String[] arysOldFormula = getReportBaseClass().getFormulae();

    String[] aryCombineFormula = null;
    aryCombineFormula = nc.vo.ic.pub.GenMethod.combineStringArray(arysOldFormula, arysFormulaGet);

    aryCombineFormula = nc.vo.ic.pub.GenMethod.combineStringArray(aryCombineFormula, aryFormulaAppend);

    if ((SCMEnv.DEBUG) && 
      (aryCombineFormula != null)) {
      for (int i = 0; i < aryCombineFormula.length; i++) {
        SCMEnv.out(aryCombineFormula[i]);
      }
    }

    if ((aryCombineFormula != null) && (aryCombineFormula.length > 0)) {
      getReportBaseClass().getBillModel().execFormulasWithVO(sourceVO, aryCombineFormula);
    }

    if (isSetReportData)
      getReportBaseClass().setBodyDataVO(sourceVO, false);
  }

  protected final void setBodyDataFormulaNOBillItem(CircularlyAccessibleValueObject[] sourceVO, ArrayList arylistItemField, boolean isSetReportData)
  {
    ArryFormula arryFormula = new ArryFormula();
    ArrayList arylistresult = arryFormula.nocheckHtbkey(arylistItemField);

    String sError = (String)arylistresult.get(0);
    if (sError != null) {
      SCMEnv.out("nc.ui.ic.pub.report.IcBaseReport.getRDataByFormulakey:" + sError);
    }

    String[] arysFormulaGet = (String[])(String[])arylistresult.get(1);
    if (((arysFormulaGet == null) || (arysFormulaGet.length == 0)) && (!isSetReportData))
    {
      return;
    }

    String[] arysOldFormula = getReportBaseClass().getFormulae();

    String[] aryCombineFormula = nc.vo.ic.pub.GenMethod.combineStringArray(arysOldFormula, arysFormulaGet);

    if ((SCMEnv.DEBUG) && 
      (aryCombineFormula != null)) {
      for (int i = 0; i < aryCombineFormula.length; i++) {
        SCMEnv.out(aryCombineFormula[i]);
      }
    }

    if ((aryCombineFormula != null) && (aryCombineFormula.length > 0))
    {
      SuperVOUtil.execFormulaWithVOs((SuperVO[])(SuperVO[])sourceVO, aryCombineFormula);
    }

    if (isSetReportData)
      getReportBaseClass().setBodyDataVO(sourceVO, false);
  }

  public ConditionVO[] setConditionVO(ConditionVO[] voCons)
  {
    getGroupVO();
    getOrderVO();
    if ((this.m_voaGroup != null) && (voCons != null)) {
      int iGroupVOLength = this.m_voaGroup.length;
      int ivoCons = voCons.length;
      int k = 0;
      ConditionVO[] tempvoCons = new ConditionVO[iGroupVOLength + ivoCons];

      for (int i = 0; i < iGroupVOLength + ivoCons; i++) {
        tempvoCons[i] = new ConditionVO();
        String saccount = new Integer(k + 1).toString();
        if (i >= ivoCons) {
          tempvoCons[i].setFieldName(NCLangRes.getInstance().getStrByID("4008other", "UPP4008other-000276") + saccount);

          tempvoCons[i].setFieldCode("GroupField" + saccount);
          tempvoCons[i].setValue(this.m_voaGroup[k].getQryfld());
          k++;
        } else {
          tempvoCons[i] = voCons[i];
        }
      }
      voCons = tempvoCons;
    }

    if ((this.m_voaOrder != null) && (voCons != null)) {
      int iOrderVOLength = this.m_voaOrder.length;
      int ivoCons = voCons.length;
      int k = 0;
      ConditionVO[] tempvoCons = new ConditionVO[iOrderVOLength + ivoCons];

      for (int i = 0; i < iOrderVOLength + ivoCons; i++) {
        tempvoCons[i] = new ConditionVO();
        String saccount = new Integer(k + 1).toString();
        if (i >= ivoCons) {
          tempvoCons[i].setFieldName(NCLangRes.getInstance().getStrByID("4008other", "UPT40080816-000040") + saccount);

          tempvoCons[i].setFieldCode("OrderField" + saccount);
          tempvoCons[i].setValue(this.m_voaOrder[k].getQryfld());
          k++;
        } else {
          tempvoCons[i] = voCons[i];
        }
      }
      voCons = tempvoCons;
    }
    return voCons;
  }

  public ConditionVO[] setConditionVO(ConditionVO[] voCons, String[] sGroupTableName, String[] sOrderTableName)
  {
    getGroupVO();
    getOrderVO();
    setGroupTableName(sGroupTableName);
    setOrderTableName(sOrderTableName);
    if ((this.m_voaGroup != null) && (voCons != null)) {
      int iGroupVOLength = this.m_voaGroup.length;
      int ivoCons = voCons.length;
      int k = 0;
      ConditionVO[] tempvoCons = new ConditionVO[iGroupVOLength + ivoCons];

      for (int i = 0; i < iGroupVOLength + ivoCons; i++) {
        tempvoCons[i] = new ConditionVO();
        String saccount = new Integer(k + 1).toString();
        if (i >= ivoCons) {
          tempvoCons[i].setFieldName(NCLangRes.getInstance().getStrByID("4008other", "UPP4008other-000276") + saccount);

          tempvoCons[i].setFieldCode("GroupField" + saccount);
          tempvoCons[i].setValue(this.m_voaGroup[k].getQryfld());
          k++;
        } else {
          tempvoCons[i] = voCons[i];
        }
      }
      voCons = tempvoCons;
    }

    if ((this.m_voaOrder != null) && (voCons != null)) {
      int iOrderVOLength = this.m_voaOrder.length;
      int ivoCons = voCons.length;
      int k = 0;
      ConditionVO[] tempvoCons = new ConditionVO[iOrderVOLength + ivoCons];

      for (int i = 0; i < iOrderVOLength + ivoCons; i++) {
        tempvoCons[i] = new ConditionVO();
        String saccount = new Integer(k + 1).toString();
        if (i >= ivoCons) {
          tempvoCons[i].setFieldName(NCLangRes.getInstance().getStrByID("4008other", "UPT40080816-000040") + saccount);

          tempvoCons[i].setFieldCode("OrderField" + saccount);
          tempvoCons[i].setValue(this.m_voaOrder[k].getQryfld());
          k++;
        } else {
          tempvoCons[i] = voCons[i];
        }
      }
      voCons = tempvoCons;
    }
    return voCons;
  }

  protected final void setDlgSubTotal(SubTotalRecordDlg newDlg)
  {
    if (this.m_dlgSubTotal != null) {
      String[] saGroupID = this.m_dlgSubTotal.getGroupID();
      String[] saSubID = this.m_dlgSubTotal.getSubTotalID();
      getReportBaseClass().showHiddenColumn(saGroupID);
      getReportBaseClass().showHiddenColumn(saSubID);
    }
    this.m_dlgSubTotal = newDlg;
  }

  public void setGroupTableName(String[] sTableName)
  {
    if ((this.m_voaGroup != null) && (sTableName != null)) {
      int iVOLength = this.m_voaGroup.length;
      int iTableNameLength = sTableName.length;
      if (iVOLength <= iTableNameLength) {
        for (int i = 0; i < iVOLength; i++) {
          this.m_voaGroup[i].setQryfld(resetQryfld(this.m_voaGroup[i].getQryfld()));

          this.m_voaGroup[i].setQryfld(sTableName[i].toString() + "." + this.m_voaGroup[i].getQryfld());
        }
      }
      else
        for (int i = 0; i < iTableNameLength; i++) {
          this.m_voaGroup[i].setQryfld(resetQryfld(this.m_voaGroup[i].getQryfld()));

          this.m_voaGroup[i].setQryfld(sTableName[i].toString() + "." + this.m_voaGroup[i].getQryfld());
        }
    }
  }

  protected final void setHeadDatabyFormulaItem(CircularlyAccessibleValueObject sourceVO, ArrayList arylistItemField, boolean isSetReportData)
  {
    ArryFormula arryFormula = new ArryFormula();
    ArrayList arylistresult = arryFormula.checkHtbkey(arylistItemField, getHeadItemKey());

    String sError = (String)arylistresult.get(0);
    if (sError != null) {
      SCMEnv.out("nc.ui.ic.pub.report.IcBaseReport.getRDataByFormulakey:" + sError);
    }

    String[] arysFormulaGet = (String[])(String[])arylistresult.get(1);
    if (((arysFormulaGet == null) || (arysFormulaGet.length == 0)) && (!isSetReportData))
    {
      return;
    }

    if ((SCMEnv.DEBUG) && 
      (arysFormulaGet != null)) {
      for (int i = 0; i < arysFormulaGet.length; i++) {
        SCMEnv.out(arysFormulaGet[i]);
      }
    }

    if ((arysFormulaGet != null) && (arysFormulaGet.length > 0)) {
      CircularlyAccessibleValueObject[] aryDataVo = new CircularlyAccessibleValueObject[1];
      aryDataVo[0] = sourceVO;
      getReportBaseClass().getBillModel().execFormulasWithVO(aryDataVo, arysFormulaGet);
    }

    if (isSetReportData)
      getReportBaseClass().setHeadDataVO(sourceVO);
  }

  public void setOrderTableName(String[] sTableName)
  {
    if ((this.m_voaOrder != null) && (sTableName != null)) {
      int iVOLength = this.m_voaOrder.length;
      int iTableNameLength = sTableName.length;
      if (iVOLength <= iTableNameLength) {
        for (int i = 0; i < iVOLength; i++) {
          this.m_voaOrder[i].setQryfld(resetQryfld(this.m_voaOrder[i].getQryfld()));

          this.m_voaOrder[i].setQryfld(sTableName[i].toString() + "." + this.m_voaOrder[i].getQryfld());
        }
      }
      else
        for (int i = 0; i < iTableNameLength; i++) {
          this.m_voaOrder[i].setQryfld(resetQryfld(this.m_voaOrder[i].getQryfld()));

          this.m_voaOrder[i].setQryfld(sTableName[i].toString() + "." + this.m_voaOrder[i].getQryfld());
        }
    }
  }

  public void setTotalAst(boolean newM_isTotalAst)
  {
    this.m_isTotalAst = newM_isTotalAst;
  }

  protected void showButton(ButtonObject btnObject)
  {
    if (btnObject != null) {
      btnObject.setVisible(true);
      updateButtons();
    }
  }

  protected void showButtons(ButtonObject[] btnObjects)
  {
    if ((btnObjects != null) || (btnObjects.length > 0)) {
      for (int i = 0; i < btnObjects.length; i++) {
        btnObjects[i].setVisible(true);
      }
      updateButtons();
    }
  }

  protected void addRelaSortData(List sortdata)
  {
    if (sortdata == null)
      return;
    this.m_sortRelaDatas.add(new BillModelRelaSortData(sortdata, getReportBaseClass().getBillModel()));
  }

  protected void addRelaSortData(Object[] sortdata) {
    if (sortdata == null)
      return;
    this.m_sortRelaDatas.add(new BillModelRelaSortData(sortdata, getReportBaseClass().getBillModel()));
  }

  protected List getRelaSortData(int i) {
    if ((i < 0) || (i >= this.m_sortRelaDatas.size()))
      return null;
    return ((BillModelRelaSortData)this.m_sortRelaDatas.get(i)).getRelaSortData();
  }

  protected Object[] getRelaSortDataAsArray(int i) {
    if ((i < 0) || (i >= this.m_sortRelaDatas.size()))
      return null;
    return ((BillModelRelaSortData)this.m_sortRelaDatas.get(i)).getRelaSortDataAsArray();
  }

  protected void addSortListener()
  {
    try
    {
      getReportBaseClass().getBillModel().addSortListener(this);
      getReportBaseClass().getBillModel().setSortPrepareListener(this);
    } catch (Exception e) {
      nc.ui.ic.pub.tools.GenMethod.handleException(null, null, e);
    }
  }

  public void afterSort(String key)
  {
  }

  public void beforeSort(String key)
  {
  }

  private void clearRaleDatas()
  {
    try
    {
      if (this.m_sortRelaDatas.size() > 0) {
        for (int i = 0; i < this.m_sortRelaDatas.size(); i++)
          ((BillModelRelaSortData)this.m_sortRelaDatas.get(i)).destroy();
        this.m_sortRelaDatas.clear();
      }
    }
    catch (Exception e) {
      SCMEnv.error(e);
    }
  }

  public int getSortTypeByBillItemKey(String key)
  {
    try
    {
      clearRaleDatas();
      beforeSort(key);
    } catch (Exception e) {
      nc.ui.ic.pub.tools.GenMethod.handleException(null, null, e);
    }
    return -1;
  }

  public void setBtnState()
  {
    if (getReportBaseClass().getBillModel().getRowCount() > 0) {
      this.m_btnLocate.setEnabled(true);
      this.m_btnSort.setEnabled(true);
      this.m_ExcelExport.setEnabled(true);
      this.m_PrintMng.setEnabled(true);
      this.m_btnSubTotal.setEnabled(true);
      this.m_btnFilter.setEnabled(true);
    }
    else
    {
      this.m_btnLocate.setEnabled(false);
      this.m_btnSort.setEnabled(false);
      this.m_ExcelExport.setEnabled(false);
      this.m_PrintMng.setEnabled(false);
      this.m_btnSubTotal.setEnabled(false);
      this.m_btnFilter.setEnabled(false);
    }

    updateButtons();
  }

  private void saveReportPanelShowState()
  {
    if ((this.m_listshowitemkey != null) || (this.m_listhideitemkey != null)) {
      return;
    }
    this.m_listshowitemkey = new ArrayList();
    this.m_listhideitemkey = new ArrayList();

    ReportItem[] items = getReportBaseClass().getBody_Items();
    if (items == null)
      return;
    for (int i = 0; i < items.length; i++) {
      if (items[i] == null)
        continue;
      if (items[i].isShow())
        this.m_listshowitemkey.add(items[i].getKey());
      else
        this.m_listhideitemkey.add(items[i].getKey());
    }
  }

  protected void resetPanelShowState()
  {
    if ((this.m_listshowitemkey != null) && (this.m_listshowitemkey.size() > 0))
      try {
        getReportBaseClass().showHiddenColumn((String[])(String[])this.m_listshowitemkey.toArray(new String[this.m_listshowitemkey.size()]));
      }
      catch (Exception e)
      {
      }
    if ((this.m_listhideitemkey != null) && (this.m_listhideitemkey.size() > 0))
      try {
        getReportBaseClass().hideColumn((String[])(String[])this.m_listhideitemkey.toArray(new String[this.m_listhideitemkey.size()]));
      }
      catch (Exception e)
      {
      }
  }
}