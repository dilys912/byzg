package nc.ui.ic.pub.vmi;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import nc.ui.ic.pub.bill.GeneralBillHelper;
import nc.ui.ic.pub.print.PrintDataInterface;
import nc.ui.ic.pub.print.PrintDirect;
import nc.ui.ic.pub.tools.GenMethod;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.linkoperate.ILinkApprove;
import nc.ui.pub.linkoperate.ILinkApproveData;
import nc.ui.pub.linkoperate.ILinkMaintain;
import nc.ui.pub.linkoperate.ILinkMaintainData;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.pub.print.PrintEntry;
import nc.ui.pub.report.ReportBaseClass;
import nc.ui.pub.report.ReportItem;
import nc.ui.scm.pub.BillTools;
import nc.ui.scm.pub.report.OrientDialog;
import nc.ui.scm.sourcebill.SourceBillFlowDlg;
import nc.vo.bd.CorpVO;
import nc.vo.ic.pub.bill.QryConditionVO;
import nc.vo.ic.pub.vmi.VmiSumHeaderVO;
import nc.vo.ic.pub.vmi.VmiSumUiVO;
import nc.vo.ic.pub.vmi.VmiSumVO;
import nc.vo.ic.pub.vmi.VmiSumpolicyVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.ic.bill.InvVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.sm.UserVO;

public class SumClientUI extends ToftPanel
  implements ILinkMaintain, ILinkApprove, ILinkQuery
{
  private ReportBaseClass ivjReportBaseClass = null;

  private ButtonObject m_boSum = new ButtonObject(NCLangRes.getInstance().getStrByID("4008other", "UPT40080802-000001"), NCLangRes.getInstance().getStrByID("4008other", "UPP4008other-000466"), 0, "汇总");

  private ButtonObject m_boCancelSum = new ButtonObject(NCLangRes.getInstance().getStrByID("4008other", "UPT40083802-000019"), NCLangRes.getInstance().getStrByID("4008other", "UPT40083802-000019"), 0, "取消汇总");

  private ButtonObject m_boQuery = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000006"), NCLangRes.getInstance().getStrByID("common", "UC001-0000006"), 0, "查询");

  private ButtonObject m_boPolicy = new ButtonObject(NCLangRes.getInstance().getStrByID("4008other", "UPT40083802-000002"), NCLangRes.getInstance().getStrByID("4008other", "UPT40083802-000002"), 0, "汇总条件");

  private ButtonObject m_boOutDetail = new ButtonObject(NCLangRes.getInstance().getStrByID("4008other", "UPT40083802-000020"), NCLangRes.getInstance().getStrByID("4008other", "UPP4008other-000467"), 0, "出库明细");

  private ButtonObject m_boLocate = new ButtonObject(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000089"), NCLangRes.getInstance().getStrByID("4008other", "UPP4008other-000465"), 0, "定位");

  private ButtonObject m_boPrint = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000007"), NCLangRes.getInstance().getStrByID("common", "UC001-0000007"), 2, "打印");

  private ButtonObject m_boPrintPrivew = new ButtonObject(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000305"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000305"), 2, "预览");

  protected ButtonObject m_boRelBillQry = new ButtonObject(NCLangRes.getInstance().getStrByID("40083802", "UPT40083802-000050"), NCLangRes.getInstance().getStrByID("40083802", "UPT40083802-000050"), 0, "联查");

  private ButtonObject m_boTestDlg = new ButtonObject("测试Dlg", "测试Dlg", 2, "测试Dlg");

  private ButtonObject[] m_aryButtonGroup = { this.m_boSum, this.m_boCancelSum, this.m_boQuery, this.m_boOutDetail, this.m_boPolicy, this.m_boLocate, this.m_boPrint, this.m_boPrintPrivew, this.m_boRelBillQry };

  private String m_sCorpID = null;

  private String m_sCorpName = null;

  private String m_sUserID = null;

  private String m_sLogDate = null;

  private String m_sUserName = null;

  private SumQryDlg m_QueryConditionDlg = null;

  private SumPolicyDlg m_dlgSum = null;

  private OutDetailDlg m_dlgOutDetail = null;

  private SumCondDlg m_dlgSumCond = null;

  private String m_sPNodeCode = "40083802";

  private AggregatedValueObject m_voReport = null;

  private String m_sHeaderVOName = "nc.vo.ic.pub.vmi.VmiSumHeaderVO";

  private String m_sItemVOName = "nc.vo.ic.pub.vmi.VmiSumHeaderVO";

  private String m_sVOName = "nc.vo.ic.pub.vmi.VmiSumUiVO";

  PrintDataInterface m_dataSource = null;

  PrintEntry m_print = null;

  protected int[] m_iaScale = { 2, 2, 2, 2, 2 };
  public static final int SC_NUM = 0;
  public static final int SC_ASTNUM = 1;
  public static final int SC_CONVRATE = 2;
  public static final int SC_PRICE = 3;
  public static final int SC_MNY = 4;
  protected String sGeneralHBO_Client = "nc.ui.ic.ic201.GeneralHBO_Client";

  protected ArrayList m_alUserCorpID = null;

  protected VmiSumVO[] m_voaVmiSumData = null;

  private String m_sTransIsConsume = "Y";

  private boolean m_isLocated = false;

  public SumClientUI()
  {
    initialize();
  }

  protected void calculateTotal(CircularlyAccessibleValueObject[] voItems, String[] sCols)
  {
    if ((voItems == null) || (sCols == null) || (sCols.length < 1)) {
      return;
    }
    if ((voItems == null) || (voItems.length < 1))
      return;
    UFDouble[] tNums = new UFDouble[sCols.length];
    Integer[] scales = new Integer[sCols.length];

    for (int i = 0; i < voItems.length; i++) {
      for (int j = 0; j < sCols.length; j++) {
        UFDouble cNum = null;
        try {
          cNum = (UFDouble)getReportBaseClass().getBillModel().getValueAt(i, sCols[j]);
        }
        catch (Exception e)
        {
        }
        if ((cNum != null) && (scales[j] == null)) {
          int point = cNum.toString().indexOf(".");
          int len = cNum.toString().length();
          if (point >= 0) {
            scales[j] = new Integer(len - point - 1);
          }
        }
        if (tNums[j] == null) {
          tNums[j] = new UFDouble(0);
        }
        if (cNum != null) {
          tNums[j] = tNums[j].add(cNum);
        }
      }
    }

    getReportBaseClass().getTotalTableModel().setValueAt(NCLangRes.getInstance().getStrByID("4008other", "UPP4008other-000271"), 0, 0);

    for (int i = 0; i < sCols.length; i++) {
      int iCol = getReportBaseClass().getBillModel().getBodyColByKey(sCols[i]);
      if ((iCol == -1) || 
        (new UFDouble(tNums[i].toString()).doubleValue() == 0.0D)) continue;
      if (scales[i] != null) {
        tNums[i] = new UFDouble(tNums[i].toString(), scales[i].intValue());
      }
      else
        tNums[i] = new UFDouble(tNums[i].toString(), 0);
      getReportBaseClass().getTotalTableModel().setValueAt(tNums[i], 0, iCol);
    }
  }

  public ConditionVO[] filterCondVO(ConditionVO[] voaCond, String[] saItemKey)
  {
    if ((voaCond == null) || (saItemKey == null)) {
      return null;
    }
    Vector vTempCond = new Vector();
    int j = 0;

    int len = saItemKey.length;
    for (int i = 0; i < voaCond.length; i++)
    {
      if (voaCond[i] != null) {
        for (j = 0; (j < len) && (
          (saItemKey[j] == null) || (voaCond[i].getFieldCode() == null) || (!saItemKey[j].trim().equals(voaCond[i].getFieldCode().trim()))); j++);
        if (j >= len)
          vTempCond.add(voaCond[i]);
      }
    }
    ConditionVO[] voaRes = null;
    if (vTempCond.size() > 0) {
      voaRes = new ConditionVO[vTempCond.size()];
      vTempCond.copyInto(voaRes);
    }
    return voaRes;
  }

  public void filterCondVO2(ConditionVO[] voaCond, String[] saItemKey)
  {
    if ((voaCond == null) || (saItemKey == null)) {
      return;
    }
    int j = 0;

    int len = saItemKey.length;
    for (int i = 0; i < voaCond.length; i++)
    {
      if (voaCond[i] != null)
        for (j = 0; j < len; j++) {
          if ((saItemKey[j] == null) || (voaCond[i].getFieldCode() == null) || (!saItemKey[j].trim().equals(voaCond[i].getFieldCode().trim()))) {
            continue;
          }
          voaCond[i].setFieldCode("1");
          voaCond[i].setOperaCode("=");
          voaCond[i].setDataType(1);
          voaCond[i].setValue("1");
        }
    }
  }

  protected void getCEnvInfo()
  {
    try
    {
      ClientEnvironment ce = ClientEnvironment.getInstance();
      try
      {
        this.m_sUserID = ce.getUser().getPrimaryKey();
      }
      catch (Exception e)
      {
      }

      try
      {
        this.m_sUserName = ce.getUser().getUserName();
      }
      catch (Exception e)
      {
      }

      try
      {
        this.m_sCorpID = ce.getCorporation().getPrimaryKey();
        SCMEnv.out("---->corp id is " + this.m_sCorpID);
        this.m_sCorpName = ce.getCorporation().getUnitname();
        SCMEnv.out("---->corp id is " + this.m_sCorpName);
      }
      catch (Exception e)
      {
      }
      try
      {
        if (ce.getDate() != null)
          this.m_sLogDate = ce.getDate().toString();
      }
      catch (Exception e)
      {
      }
    }
    catch (Exception e)
    {
    }
  }

  private SumQryDlg getConditionDlg()
  {
    if (this.m_QueryConditionDlg == null) {
      this.m_QueryConditionDlg = new SumQryDlg(this);

      this.m_QueryConditionDlg.setTempletID(this.m_sCorpID, this.m_sPNodeCode, this.m_sUserID, null);

      this.m_QueryConditionDlg.initCorpRef("pk_corp", this.m_sCorpID, this.m_alUserCorpID);

      String[] sThenClear = { "cwarehouseid", "cvendorid", "cinventoryid" };

      this.m_QueryConditionDlg.setAutoClear("pk_corp", sThenClear);

      this.m_QueryConditionDlg.setDefaultValue("dsumdate", this.m_sLogDate, null);

      this.m_QueryConditionDlg.initQueryDlgRef();

      this.m_QueryConditionDlg.hideNormal();
    }

    return this.m_QueryConditionDlg;
  }

  protected PrintDataInterface getDataSource()
  {
    if (null == this.m_dataSource) {
      this.m_dataSource = new PrintDataInterface();
      BillData bd = getReportBaseClass().getBillData();
      this.m_dataSource.setBillData(bd);
      this.m_dataSource.setModuleName(this.m_sPNodeCode);
      this.m_dataSource.setTotalLinesInOnePage(getPrintEntry().getBreakPos());
    }
    return this.m_dataSource;
  }

  private UFDate getDefaultEndDate()
  {
    if ((this.m_sLogDate == null) || (this.m_sLogDate.trim().length() != 10)) {
      return new UFDate("2001-01-01");
    }
    return new UFDate(new String(this.m_sLogDate.substring(0, 8) + new Integer(new UFDate(this.m_sLogDate).getDaysMonth())));
  }

  private UFDate getDefaultStartDate()
  {
    if ((this.m_sLogDate == null) || (this.m_sLogDate.trim().length() != 10)) {
      return new UFDate("2001-01-01");
    }
    return new UFDate(new String(this.m_sLogDate.substring(0, 8) + "01"));
  }

  public OutDetailDlg getOutDetailDlg()
  {
    if (this.m_dlgOutDetail == null) {
      this.m_dlgOutDetail = new OutDetailDlg(this);
      this.m_dlgOutDetail.setScale(this.m_iaScale);
      this.m_dlgOutDetail.initTable();
    }

    return this.m_dlgOutDetail;
  }

  protected PrintEntry getPrintEntry()
  {
    if (null == this.m_print) {
      this.m_print = new PrintEntry(null, null);
      this.m_print.setTemplateID(this.m_sCorpID, this.m_sPNodeCode, this.m_sUserID, null);
    }
    return this.m_print;
  }

  private ReportBaseClass getReportBaseClass()
  {
    if (this.ivjReportBaseClass == null) {
      try {
        this.ivjReportBaseClass = new ReportBaseClass();
        this.ivjReportBaseClass.setName("ReportBaseClass");
        try
        {
          this.ivjReportBaseClass.setTempletID(this.m_sCorpID, this.m_sPNodeCode, this.m_sUserID, null);

          if ("Y".equals(this.m_sTransIsConsume)) {
            if (this.ivjReportBaseClass.getBody_Item("ntransnum") != null) {
              this.ivjReportBaseClass.hideBodyTableCol("ntransnum");
            }

            this.ivjReportBaseClass.getBillModel().setBodyItems(this.ivjReportBaseClass.getBillModel().getBodyItems());
          }
        }
        catch (Exception e)
        {
          showErrorMessage(NCLangRes.getInstance().getStrByID("4008other", "UPP4008other-000350"));

          return this.ivjReportBaseClass;
        }

        this.ivjReportBaseClass.setRowNOShow(true);
        this.ivjReportBaseClass.setTatolRowShow(true);

        BillData bd = this.ivjReportBaseClass.getBillData();
        if (bd == null) {
          SCMEnv.out("--> billdata null.");
          return this.ivjReportBaseClass;
        }

      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjReportBaseClass;
  }

  protected ReportItem getReportItem(String sKey, String sTitle)
  {
    ReportItem biAddItem = new ReportItem();
    biAddItem.setName(sTitle);
    biAddItem.setKey(sKey);
    biAddItem.setDataType(2);
    biAddItem.setDecimalDigits(4);
    biAddItem.setLength(80);
    biAddItem.setWidth(80);
    biAddItem.setEnabled(false);
    biAddItem.setShow(true);

    biAddItem.setPos(1);
    if (sKey != null) {
      if (sKey.indexOf("mny") >= 0) {
        biAddItem.setDecimalDigits(this.m_iaScale[4]);
      }
      else if ((sKey.indexOf("astnum") >= 0) || (sKey.indexOf("assistnum") >= 0)) {
        biAddItem.setDecimalDigits(this.m_iaScale[1]);
      }
      else {
        biAddItem.setDecimalDigits(this.m_iaScale[0]);
      }
    }
    return biAddItem;
  }

  private SumCondDlg getSumCondDlg()
  {
    if (this.m_dlgSumCond == null) {
      this.m_dlgSumCond = new SumCondDlg(this);
      this.m_dlgSumCond.setCorpID(this.m_sCorpID);
      this.m_dlgSumCond.setDate(this.m_sLogDate);
    }
    return this.m_dlgSumCond;
  }

  private SumPolicyDlg getSumDlg()
  {
    if (this.m_dlgSum == null) {
      this.m_dlgSum = new SumPolicyDlg(this);
    }
    return this.m_dlgSum;
  }

  public String getTitle()
  {
    return NCLangRes.getInstance().getStrByID("4008other", "UPP4008other-000367");
  }

  private void handleException(Throwable exception)
  {
    SCMEnv.out("--------- 未捕捉到的异常 ---------");
    SCMEnv.error(exception);
  }

  private void initialize()
  {
    try
    {
      setButtons(this.m_aryButtonGroup);
      getCEnvInfo();

      setName("ClientUI");
      setSize(774, 419);
      add(getReportBaseClass(), "Center");
    }
    catch (Throwable ivjExc) {
      handleException(ivjExc);
    }

    try
    {
      getReportBaseClass().setMaxLenOfHeadItem("vqrycondition", 500);
      getReportBaseClass().setMaxLenOfHeadItem("vunitname", 100);
    }
    catch (Exception e)
    {
    }

    initSysParam();

    setScale();
  }

  protected void initSysParam()
  {
    try
    {
      String[] saParam = { "BD501", "BD502", "BD503", "BD504", "BD301", "IC028" };

      ArrayList alAllParam = new ArrayList();

      ArrayList alParam = new ArrayList();
      alParam.add(this.m_sCorpID);
      alParam.add(saParam);
      alAllParam.add(alParam);

      alAllParam.add(this.m_sUserID);

      ArrayList alRetData = (ArrayList)GeneralBillHelper.queryInfo(new Integer(11), alAllParam);

      if ((alRetData == null) || (alRetData.size() < 2)) {
        SCMEnv.out("初始化参数错误！");
        return;
      }

      String[] saParamValue = (String[])(String[])alRetData.get(0);
      if ((saParamValue != null) && (saParamValue.length > 5))
      {
        this.m_iaScale[0] = Integer.parseInt(saParamValue[0]);

        this.m_iaScale[1] = Integer.parseInt(saParamValue[1]);

        this.m_iaScale[2] = Integer.parseInt(saParamValue[2]);

        this.m_iaScale[3] = Integer.parseInt(saParamValue[3]);

        this.m_iaScale[4] = Integer.parseInt(saParamValue[4]);
      }

      this.m_alUserCorpID = ((ArrayList)alRetData.get(1));
    }
    catch (Exception e)
    {
      SCMEnv.out("can not get para" + e.getMessage());
      if ((e instanceof BusinessException))
        showErrorMessage(e.getMessage());
    }
  }

  public void onButtonClicked(ButtonObject bo)
  {
    if (this.m_isLocated) {
      BillTools.changeRowColor(getReportBaseClass().getBillTable(), null);

      this.m_isLocated = false;
    }
    if (bo == this.m_boQuery)
      onQuery();
    else if (bo == this.m_boSum)
      onSum();  //汇总
    else if (bo == this.m_boCancelSum)
      onCancelSum();
    else if (bo == this.m_boOutDetail)
      onOutDetail();
    else if (bo == this.m_boPolicy)
      onPolicy();
    else if (bo == this.m_boLocate)
      onLocate();
    else if (bo == this.m_boPrint)
      onPrint();
    else if (bo == this.m_boPrintPrivew)
      onPrintPrivew();
    else if (bo == this.m_boRelBillQry) {
      onRelBillQry();
    }
    else if (bo == this.m_boTestDlg)
      onTestDlg();
  }

  public void onCancelSum()
  {
    try
    {
      if (getSumCondDlg().showModal() != 1)
      {
        return;
      }showHintMessage(NCLangRes.getInstance().getStrByID("4008other", "UPP4008other-000368"));

      UFDate dSumDate = null;
      if (getSumCondDlg().getDate() != null)
        dSumDate = new UFDate(getSumCondDlg().getDate());
      else {
        dSumDate = new UFDate(this.m_sLogDate).getDateBefore(1);
      }
      String sCalbodyid = getSumCondDlg().getCalbodyID();
      String sWh = getSumCondDlg().getWh();
      String sVendorid = getSumCondDlg().getVendorID();
      String sInvid = getSumCondDlg().getInvID();

      QryConditionVO voCond = new QryConditionVO();
      voCond.setParam(1, dSumDate);
      voCond.setStrParam(0, this.m_sCorpID);
      if (sCalbodyid != null)
        voCond.setStrParam(2, sCalbodyid);
      if (sWh != null)
        voCond.setStrParam(1, sWh);
      if (sVendorid != null)
        voCond.setStrParam(6, sVendorid);
      if (sInvid != null) {
        voCond.setStrParam(7, sInvid);
      }

      voCond.setParam(3, getSumCondDlg().getInvClassCodes());

      this.m_voaVmiSumData = VmiSumHelper.cancelSum(voCond);
      showData(this.m_voaVmiSumData);

      showCond();

      showHintMessage(NCLangRes.getInstance().getStrByID("4008other", "UPP4008other-000369"));
    }
    catch (Exception e)
    {
      handleException(e);
      showErrorMessage(NCLangRes.getInstance().getStrByID("4008other", "UPP4008other-000370") + e.getMessage());

      showHintMessage(NCLangRes.getInstance().getStrByID("4008other", "UPP4008other-000371"));
    }
  }

  protected void onLocate()
  {
    OrientDialog dlg = new OrientDialog(this, getReportBaseClass());

    dlg.showModal();
    if (dlg.getResult() == 1) {
      ArrayList alRes = dlg.getOrientRows();
      if ((alRes == null) || (alRes.size() < 1)) {
        showHintMessage(NCLangRes.getInstance().getStrByID("4008other", "UPP4008other-000352"));
      }
    }
    else
    {
      this.m_isLocated = true;
    }
  }

  public void onOutDetail()
  {
    try
    {
      if ((this.m_voaVmiSumData != null) && (this.m_voaVmiSumData.length > 0))
      {
        int iCurLine = getReportBaseClass().getBillTable().getSelectedRow();
        if ((iCurLine >= 0) && (iCurLine < this.m_voaVmiSumData.length) && (this.m_voaVmiSumData[iCurLine] != null) && (this.m_voaVmiSumData[iCurLine].getItemVOs() != null) && (this.m_voaVmiSumData[iCurLine].getItemVOs().length > 0))
        {
          InvVO voInv = new InvVO();
          voInv.setInvname(this.m_voaVmiSumData[iCurLine].getHeaderVO().getVinvname());

          getOutDetailDlg().setInv(voInv);
          getOutDetailDlg().setData(this.m_voaVmiSumData[iCurLine].getItemVOs());
          getOutDetailDlg().showModal();
        }
      }
      else {
        showHintMessage(NCLangRes.getInstance().getStrByID("4008other", "UPP4008other-000372"));
      }
    }
    catch (Exception e) {
      SCMEnv.error(e);
      showErrorMessage(e.getMessage());
    }
  }

  public void onPolicy()
  {
    try
    {
      QryConditionVO voCond = new QryConditionVO();
      voCond.setStrParam(0, this.m_sCorpID);
      ArrayList alRet = VmiSumHelper.queryPolicy(voCond);
      VmiSumpolicyVO voP = null;
      boolean bHaveSumData = false;
      if ((alRet != null) && (alRet.size() >= 2)) {
        voP = (VmiSumpolicyVO)alRet.get(0);
        if (alRet.get(1) != null) {
          bHaveSumData = new UFBoolean(alRet.get(1).toString()).booleanValue();
        }
      }

      if ((voP != null) && (bHaveSumData)) {
        getSumDlg().setPolicy(voP);

        getSumDlg().setEnabled(false);
      }
      else
      {
        getSumDlg().setEnabled(true);
      }

      if (1 == getSumDlg().showModal())
      {
        VmiSumpolicyVO voP2 = getSumDlg().getPolicy();
        if (voP != null)
          voP2.setPrimaryKey(voP.getPrimaryKey());
        voP2.setPk_corp(this.m_sCorpID);
        VmiSumHelper.updatePolicy(voP2);
      }
    }
    catch (Exception e)
    {
      SCMEnv.error(e);
      showErrorMessage(e.getMessage());
    }
  }

  public void onPrint()
  {
    try
    {
      PrintDirect.printDirect(getReportBaseClass(), getTitle());
    }
    catch (Exception e)
    {
      showErrorMessage(e.getMessage());
    }
  }

  public void onPrintPrivew()
  {
    try
    {
      PrintDirect.preview(getReportBaseClass(), getTitle());
    }
    catch (Exception e) {
      showErrorMessage(e.getMessage());
    }
  }

  public void onQuery()
  {
    try
    {
      if (getConditionDlg().showModal() != 1)
      {
        return;
      }showHintMessage(NCLangRes.getInstance().getStrByID("4008other", "UPP4008other-000375"));

      QryConditionVO voCond = new QryConditionVO(getConditionDlg().getWhereSQL());

      voCond.setParam(1, new UFDate(this.m_sLogDate));
      voCond.setStrParam(0, this.m_sCorpID);

      VmiSumUiVO voTemp = new VmiSumUiVO();

      this.m_voaVmiSumData = VmiSumHelper.query(voCond);
      showData(this.m_voaVmiSumData);

      if (getReportBaseClass().getHeadItem("vqrycondition") != null) {
        getReportBaseClass().getHeadItem("vqrycondition").setValue(getConditionDlg().getChText());
      }
      if (getReportBaseClass().getHeadItem("vunitname") != null) {
        getReportBaseClass().getHeadItem("vunitname").setValue(this.m_sCorpName);
      }
      showHintMessage(NCLangRes.getInstance().getStrByID("4008other", "UPP4008other-000376"));
    }
    catch (Exception e)
    {
      handleException(e);
      showErrorMessage(NCLangRes.getInstance().getStrByID("4008other", "UPP4008other-000353") + e.getMessage());

      showHintMessage(NCLangRes.getInstance().getStrByID("4008other", "UPP4008other-000377"));
    }
  }

  public void onSum()
  {
    try
    {
      if (getSumCondDlg().showModal() != 1)
      {
        return;
      }showHintMessage(NCLangRes.getInstance().getStrByID("4008other", "UPP4008other-000378"));

      UFDate dSumDate = null;
      if (getSumCondDlg().getDate() != null)
        dSumDate = new UFDate(getSumCondDlg().getDate());
      else {
        dSumDate = new UFDate(this.m_sLogDate).getDateBefore(1);
      }
      String sCalbodyid = getSumCondDlg().getCalbodyID();
      String sWh = getSumCondDlg().getWh();
      String sVendorid = getSumCondDlg().getVendorID();
      String sInvid = getSumCondDlg().getInvID();

      QryConditionVO voCond = new QryConditionVO();
      voCond.setParam(1, dSumDate);
      voCond.setStrParam(0, this.m_sCorpID);
      if (sCalbodyid != null)
        voCond.setStrParam(2, sCalbodyid);
      if (sWh != null)
        voCond.setStrParam(1, sWh);
      if (sVendorid != null)
        voCond.setStrParam(6, sVendorid);
      if (sInvid != null) {
        voCond.setStrParam(7, sInvid);
      }
      voCond.setParam(3, getSumCondDlg().getInvClassCodes());

      voCond.setStrParam(5, this.m_sTransIsConsume);
      this.m_voaVmiSumData = VmiSumHelper.sum(voCond);

      showData(this.m_voaVmiSumData);

      showCond();
      if ((this.m_voaVmiSumData != null) && (this.m_voaVmiSumData.length > 0)) {
        showHintMessage(NCLangRes.getInstance().getStrByID("4008other", "UPP4008other-000379"));
      }
      else {
        showHintMessage(NCLangRes.getInstance().getStrByID("4008other", "UPP4008other-000380"));
      }
    }
    catch (Exception e)
    {
      BusinessException ee = GenMethod.handleException(null, null, e);

      showErrorMessage(NCLangRes.getInstance().getStrByID("4008other", "UPP4008other-000381") + ee != null ? ee.getMessage() : e.getMessage());

      showHintMessage(NCLangRes.getInstance().getStrByID("4008other", "UPP4008other-000382"));
    }
  }

  protected void setScale()
  {
    BillItem[] biaCardBody = getReportBaseClass().getBodyItems();

    Hashtable htCardBody = new Hashtable();
    for (int i = 0; i < biaCardBody.length; i++) {
      htCardBody.put(biaCardBody[i].getKey(), new Integer(i));
    }

    String[] saBodyNumItemKey = { "ninitnum", "ninnum", "ninoutnum", "noutnum", "noutinnum", "ntransnum", "nfinalnum", "naccountnum", "ntotalinvoicenum" };

    for (int k = 0; k < saBodyNumItemKey.length; k++)
    {
      if (htCardBody.containsKey(saBodyNumItemKey[k])) {
        biaCardBody[Integer.valueOf(htCardBody.get(saBodyNumItemKey[k]).toString()).intValue()].setDecimalDigits(this.m_iaScale[0]);
      }

    }

    String[] saBodyAstNumItemKey = { "nterminastnum", "ntermbeginastnum", "ntermoutastnum", "ntermonhandastnum" };

    for (int k = 0; k < saBodyAstNumItemKey.length; k++)
    {
      if (htCardBody.containsKey(saBodyAstNumItemKey[k])) {
        biaCardBody[Integer.valueOf(htCardBody.get(saBodyAstNumItemKey[k]).toString()).intValue()].setDecimalDigits(this.m_iaScale[1]);
      }

    }

    String[] saBodyPrice = { "nprice", "ngaugeprice" };

    for (int k = 0; k < saBodyPrice.length; k++)
    {
      if (htCardBody.containsKey(saBodyPrice[k])) {
        biaCardBody[Integer.valueOf(htCardBody.get(saBodyPrice[k]).toString()).intValue()].setDecimalDigits(this.m_iaScale[3]);
      }
    }

    String[] saBodyMny = { "ninitmny", "ninmny", "ninoutmny", "noutmny", "noutinmny", "ntransmny", "nfinalmny", "ngaugemoney", "naccountmny" };

    for (int k = 0; k < saBodyMny.length; k++)
    {
      if (htCardBody.containsKey(saBodyMny[k])) {
        biaCardBody[Integer.valueOf(htCardBody.get(saBodyMny[k]).toString()).intValue()].setDecimalDigits(this.m_iaScale[4]);
      }
    }

    if (htCardBody.containsKey("hsl")) {
      biaCardBody[Integer.valueOf(htCardBody.get("hsl").toString()).intValue()].setDecimalDigits(this.m_iaScale[2]);
    }
    if (htCardBody.containsKey("convrate"))
      biaCardBody[Integer.valueOf(htCardBody.get("convrate").toString()).intValue()].setDecimalDigits(this.m_iaScale[2]);
  }

  public void showCond()
  {
    if (getReportBaseClass().getHeadItem("vqrycondition") != null) {
      StringBuffer sbMsg = new StringBuffer();
      sbMsg.append(NCLangRes.getInstance().getStrByID("4008other", "UPP4008other-000383"));

      if (getSumCondDlg().getDate() != null)
        sbMsg.append(getSumCondDlg().getDate());
      else {
        sbMsg.append(new UFDate(this.m_sLogDate).getDateBefore(1));
      }
      if (getSumCondDlg().getCalbodyName() != null) {
        sbMsg.append(NCLangRes.getInstance().getStrByID("4008other", "UPP4008other-000384") + getSumCondDlg().getCalbodyName());
      }

      if (getSumCondDlg().getWhName() != null) {
        sbMsg.append(NCLangRes.getInstance().getStrByID("4008other", "UPP4008other-000385") + getSumCondDlg().getWhName());
      }

      if (getSumCondDlg().getVendorName() != null) {
        sbMsg.append(NCLangRes.getInstance().getStrByID("4008other", "UPP4008other-000386") + getSumCondDlg().getVendorName());
      }

      if (getSumCondDlg().getInvName() != null) {
        sbMsg.append(NCLangRes.getInstance().getStrByID("4008other", "UPP4008other-000387") + getSumCondDlg().getInvName());
      }

      getReportBaseClass().getHeadItem("vqrycondition").setValue(sbMsg.toString());
    }

    if (getReportBaseClass().getHeadItem("vunitname") != null)
      getReportBaseClass().getHeadItem("vunitname").setValue(this.m_sCorpName);
  }

  protected void showData(VmiSumVO[] voaSum)
  {
    if ((voaSum != null) && (voaSum.length > 0)) {
      VmiSumHeaderVO[] voaTempHeader = new VmiSumHeaderVO[voaSum.length];
      for (int i = 0; i < voaSum.length; i++)
        voaTempHeader[i] = voaSum[i].getHeaderVO();
      getReportBaseClass().setHeadDataVO(voaTempHeader[0]);
      getReportBaseClass().setBodyDataVO(voaTempHeader);
    }
    else {
      getReportBaseClass().setHeadDataVO(new VmiSumHeaderVO());
      getReportBaseClass().getBillModel().clearBodyData();
    }

    this.m_voReport = new VmiSumUiVO();
    this.m_voReport.setParentVO(getReportBaseClass().getHeadDataVO());
    this.m_voReport.setChildrenVO(getReportBaseClass().getBodyDataVO());
  }

  protected void showTime(long lStartTime, String sTaskHint)
  {
    long lTime = System.currentTimeMillis() - lStartTime;
    SCMEnv.out("执行<" + sTaskHint + ">消耗的时间为：" + lTime / 60000L + "分" + lTime / 1000L % 60L + "秒" + lTime % 1000L + "毫秒");
  }

  public void onTestDlg()
  {
    try
    {
      ButtonObject bo = new ButtonObject("测试Dlg", "测试Dlg", 2, "测试Dlg");
      bo.setName("VMI汇总");
      bo.setCode("VMI汇总");
      bo.setHint("VMI汇总");
      bo.setTag("50:0001AA100000000001ZC");

      PfUtilClient.childButtonClicked(bo, getCorpPrimaryKey(), "400404", getClientEnvironment().getUser().getPrimaryKey(), "25", this);

      AggregatedValueObject[] vos = PfUtilClient.getRetVos();
      bo.setName("VMI汇总");
    }
    catch (Exception e)
    {
      SCMEnv.error(e);
      showErrorMessage(e.getMessage());
    }
  }

  public SumClientUI(String pk_corp, String billtype, String busitype, String operator, String id)
  {
    initialize();
    loadVMIByID(pk_corp, id);
  }

  public void loadVMIByID(String pk_corp, String id)
  {
    if (id == null)
      return;
    try
    {
      QryConditionVO voCond = new QryConditionVO(" head.cvmihid ='" + id + "' ");

      voCond.setParam(1, getClientEnvironment().getDate());
      voCond.setStrParam(0, pk_corp);

      this.m_voaVmiSumData = VmiSumHelper.query(voCond);

      showData(this.m_voaVmiSumData);

      if (getReportBaseClass().getHeadItem("vqrycondition") != null) {
        getReportBaseClass().getHeadItem("vqrycondition").setValue(NCLangRes.getInstance().getStrByID("40083802", "UPT40083802-000046", null, new String[] { id }));
      }

      if (getReportBaseClass().getHeadItem("vunitname") != null) {
        getReportBaseClass().getHeadItem("vunitname").setValue(getClientEnvironment().getCorporation().getUnitname());
      }
    }
    catch (Exception e)
    {
      handleException(e);
      showErrorMessage(NCLangRes.getInstance().getStrByID("4008other", "UPP4008other-000353") + e.getMessage());

      showHintMessage(NCLangRes.getInstance().getStrByID("4008other", "UPP4008other-000377"));
    }
  }

  public void onRelBillQry()
  {
    int selrow = getReportBaseClass().getBillTable().getSelectedRow();
    if (selrow < 0) {
      showWarningMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000199"));

      return;
    }
    try
    {
      String cvmihid = (String)getReportBaseClass().getBillModel().getValueAt(selrow, "cvmihid");

      if ((cvmihid == null) || (cvmihid.trim().length() <= 0))
        return;
      new SourceBillFlowDlg(this, "50", cvmihid, null, getClientEnvironment().getUser().getPrimaryKey(), getClientEnvironment().getCorporation().getPrimaryKey()).showModal();
    }
    catch (Exception e)
    {
      showErrorMessage(e.getMessage());
    }
  }

  public void doMaintainAction(ILinkMaintainData maintaindata)
  {
    loadVMIByID(getClientEnvironment().getCorporation().getPrimaryKey(), maintaindata.getBillID());
  }

  public void doQueryAction(ILinkQueryData querydata)
  {
    loadVMIByID(getClientEnvironment().getCorporation().getPrimaryKey(), querydata.getBillID());
  }

  public void doApproveAction(ILinkApproveData approvedata)
  {
  }
}