package nc.ui.ic.ic641;

import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.JFrame;
import nc.ui.bd.b14.LocalModel;
import nc.ui.ic.pub.LongTimeTask;
import nc.ui.ic.pub.bill.GeneralBillHelper;
import nc.ui.ic.pub.bill.query.ICMultiCorpQryClient;
import nc.ui.ic.pub.report.IcBaseReport;
import nc.ui.ic.pub.report.IcBaseReportComm;
import nc.ui.ic.pub.scale.ScaleInit;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.FramePanel;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIList;
import nc.ui.pub.beans.UIRadioButton;
import nc.ui.pub.beans.UITabbedPane;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.report.ReportBaseClass;
import nc.ui.pub.report.ReportItem;
import nc.ui.scm.pub.FreeVOParse;
import nc.ui.scm.pub.billutil.ClientCacheHelper;
import nc.ui.scm.pub.def.DefSetTool;
import nc.vo.bd.CorpVO;
import nc.vo.ic.ic641.QueryCondCtl;
import nc.vo.ic.ic641.RcvDlvOnhandHeaderVO;
import nc.vo.ic.ic641.RcvDlvOnhandItemVO;
import nc.vo.ic.ic641.RcvDlvOnhandVO;
import nc.vo.ic.pub.GenMethod;
import nc.vo.ic.pub.bill.QryConditionVO;
import nc.vo.ic.pub.lang.ResBase;
import nc.vo.ic.pub.tools.ArrayTools;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.cquery.FldgroupVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.query.RefResultVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.sm.UserVO;

public class ClientUI extends IcBaseReport
{
  private ReportBaseClass ivjReportBaseClass = null;

  private ButtonObject m_boQuery = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000006"), NCLangRes.getInstance().getStrByID("4008report", "UPP4008report-000122"), 0, "查询");

  private ButtonObject[] m_aryButtonGroup = { this.m_boQuery };

  private String m_sCorpID = null;

  private String m_sCorpName = null;

  private String m_sUserID = null;

  private String m_sLogDate = null;

  private ICMultiCorpQryClient m_QueryConditionDlg = null;

  private ReportItem[] m_voaDefaultRI = null;

  private String m_sPNodeCode = "40083602";

  private AggregatedValueObject m_voReport = null;

  private String m_sHeaderVOName = "nc.vo.ic.ic641.RcvDlvOnhandHeaderVO";

  private String m_sItemVOName = "nc.vo.ic.ic641.RcvDlvOnhandItemVO";

  private String m_sVOName = "nc.vo.ic.ic641.RcvDlvOnhandVO";

  protected int[] m_iaScale = { 2, 2, 2, 2, 2 };
  public static final int SC_NUM = 0;
  public static final int SC_ASTNUM = 1;
  public static final int SC_CONVRATE = 2;
  public static final int SC_PRICE = 3;
  public static final int SC_MNY = 4;
  protected ArrayList m_alUserCorpID = null;

  protected String m_sTrackedBillFlag = null;

  QryNormPnl m_sortpanel = null;

  private ReportItem[] m_riDefaultNotGroup = null;
  private ArrayList m_riDefaultNotGroup_Back = null;

  private QueryCondCtl m_queryParam = null;
  private int[] m_AryLevelLen;
  private String[] m_arysCalColumnItemkey = { "ccalbodyname", "ccalbodycode" };

  private String[] m_arysCargdocItemkey = { "cspacename", "cspacecode" };

  private String[] m_arysInvItemkey = { "invclassname", "invclasscode", "cinventorycode", "invname", "invtype", "invspec", "measdocname", "castunitname", "hsl", "vbatchcode", "vfree0" };

  private String[] m_arysWareColumnItemkey = { "storcode", "storname" };
  QuryNormalPanel m_quryNormalPanel;
  private String[] m_sQryLevelIndex = { NCLangRes.getInstance().getStrByID("common", "UC000-0000559"), NCLangRes.getInstance().getStrByID("common", "UC000-0001439"), NCLangRes.getInstance().getStrByID("4008report", "UPP4008report-000155"), NCLangRes.getInstance().getStrByID("4008report", "UPP4008report-000156"), NCLangRes.getInstance().getStrByID("4008report", "UPP4008report-000157"), NCLangRes.getInstance().getStrByID("4008report", "UPP4008report-000158") };

  private Vector m_vBillItemHiddenID = null;

  private String m_warehouselevel = null; private String m_whlevelassistant = null;

  public ClientUI()
  {
    initialize();
  }

  public ClientUI(FramePanel ff)
  {
    setFrame(ff);
    initialize();
  }

  public ConditionVO[] filterCondVO1(ConditionVO[] voaCond, String[] saItemKey)
  {
    if ((voaCond == null) || (saItemKey == null)) return null;
    Vector vTempCond = new Vector();
    int j = 0;
    int len = saItemKey.length;
    for (int i = 0; i < voaCond.length; i++) {
      if (voaCond[i] == null)
        continue;
      for (j = 0; j < len; j++)
      {
        if ((saItemKey[j] != null) && (voaCond[i].getFieldCode() != null) && (saItemKey[j].trim().equals(voaCond[i].getFieldCode().trim())))
        {
          break;
        }
      }

      if (j < len) continue; vTempCond.add(voaCond[i]);
    }

    ConditionVO[] voaRes = null;
    if (vTempCond.size() > 0)
    {
      voaRes = new ConditionVO[vTempCond.size()];
      vTempCond.copyInto(voaRes);
    }
    return voaRes;
  }

  public void filterCondVO2(ConditionVO[] voaCond, String[] saItemKey)
  {
    if ((voaCond == null) || (saItemKey == null)) return;
    int j = 0;
    int len = saItemKey.length;
    for (int i = 0; i < voaCond.length; i++)
      if (voaCond[i] != null)
        for (j = 0; j < len; j++)
        {
          if ((saItemKey[j] == null) || (voaCond[i].getFieldCode() == null) || (!saItemKey[j].trim().equals(voaCond[i].getFieldCode().trim())))
          {
            continue;
          }

          voaCond[i].setFieldCode("1");
          voaCond[i].setOperaCode("=");
          voaCond[i].setDataType(1);
          voaCond[i].setValue("1");
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
      } catch (Exception e) {
      }
      try {
        this.m_sCorpID = ce.getCorporation().getPrimaryKey();
        SCMEnv.out("---->corp id is " + this.m_sCorpID);

        this.m_sCorpName = ce.getCorporation().getUnitname();
        SCMEnv.out("---->corp id is " + this.m_sCorpName);
      }
      catch (Exception e) {
      }
      try {
        if (ce.getDate() != null) this.m_sLogDate = ce.getDate().toString(); 
      }
      catch (Exception e) {
      }
    }
    catch (Exception e) {
    }
  }

  private QryNormPnl getGroupSortPanel() {
    if (this.m_sortpanel == null) this.m_sortpanel = new QryNormPnl();
    return this.m_sortpanel;
  }

  private ICMultiCorpQryClient getConditionDlg()
  {
    if (this.m_QueryConditionDlg == null)
    {
      this.m_QueryConditionDlg = new ICMultiCorpQryClient(this, ResBase.get641Qry(), this.m_sUserID, this.m_sCorpID, "40083602");

      this.m_QueryConditionDlg.setTempletID(this.m_sCorpID, getPNodeCode(), this.m_sUserID, null);

      DefSetTool.updateQueryConditionForInvbasdoc(this.m_QueryConditionDlg, this.m_sCorpID, "inv.def");

      DefSetTool.updateQueryConditionForInvmandoc(this.m_QueryConditionDlg, this.m_sCorpID, "man.def");

      this.m_QueryConditionDlg.initRefWhere("inv.invcode", " and bd_invbasdoc.discountflag='N' and bd_invbasdoc.laborflag='N' ");

      this.m_QueryConditionDlg.setPowerRefsOfCorp("pk_corp", new String[] { "cwarehouseid", "kp.ccalbodyid", "cinventoryid", "invclasscode", "inv.invcode" }, new String[] { "cproviderid", "dept.deptcode", "rdcl.pk_rdcl", "cprojectid" });

      String[] sThenClear = { "cproviderid", "ccustomerid", "cdptid", "cwarehouseid", "cinventoryid", "invclasscode" };

      this.m_QueryConditionDlg.setAutoClear("pk_corp", sThenClear);

      this.m_QueryConditionDlg.setFreeItem("vfree0", "cinventoryid");
      this.m_QueryConditionDlg.setLot("vbatchcode", "cinventoryid");

      sThenClear = new String[] { "vfree0", "vbatchcode", "castunitid" };
      this.m_QueryConditionDlg.setAutoClear("cinventoryid", sThenClear);

      sThenClear = new String[] { "vbatchcode" };
      this.m_QueryConditionDlg.setAutoClear("cwarehouseid", sThenClear);

      this.m_QueryConditionDlg.setAstUnit("castunitid", new String[] { "pk_corp", "cinventoryid" });

      this.m_QueryConditionDlg.setDefaultValue("dbilldatefrom", null, getDefaultStartDate().toString());

      this.m_QueryConditionDlg.setDefaultValue("dbilldateto", null, getDefaultEndDate().toString());

      this.m_QueryConditionDlg.initQueryDlgRef();

      this.m_QueryConditionDlg.getUITabbedPane().insertTab(NCLangRes.getInstance().getStrByID("4008report", "UPP4008report-000185"), null, getQuryNormalPanel(), null, 3);

      this.m_QueryConditionDlg.setCombox("datetype", new String[][] { { "dbizdate", NCLangRes.getInstance().getStrByID("common", "UC000-0000059") }, { "daccountdate", NCLangRes.getInstance().getStrByID("common", "UC000-0003103") } });

      this.m_QueryConditionDlg.hideUnitButton();
      this.m_QueryConditionDlg.hideNormal();
      this.m_QueryConditionDlg.getUITabbedPane().setSelectedIndex(0);
    }

    return this.m_QueryConditionDlg;
  }

  private UFDate getDefaultEndDate()
  {
    if ((this.m_sLogDate == null) || (this.m_sLogDate.trim().length() != 10)) {
      return new UFDate("2001-01-01");
    }
    return new UFDate(new String(this.m_sLogDate.substring(0, 8) + new Integer(new UFDate(this.m_sLogDate).getDaysMonth())));
  }

  public String getDefaultPNodeCode()
  {
    return this.m_sPNodeCode;
  }

  private UFDate getDefaultStartDate()
  {
    if ((this.m_sLogDate == null) || (this.m_sLogDate.trim().length() != 10)) {
      return new UFDate("2001-01-01");
    }
    return new UFDate(new String(this.m_sLogDate.substring(0, 8) + "01"));
  }

  public ReportBaseClass getReportBaseClass()
  {
    if (this.ivjReportBaseClass == null) {
      try {
        this.ivjReportBaseClass = new ReportBaseClass();
        this.ivjReportBaseClass.setName("ReportBaseClass");
        try
        {
          this.ivjReportBaseClass.setTempletID(this.m_sCorpID, getPNodeCode(), this.m_sUserID, null);
        }
        catch (Exception e) {
          showErrorMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000019"));

          SCMEnv.out(e.toString());
          return this.ivjReportBaseClass;
        }

        this.ivjReportBaseClass.setRowNOShow(true);
        this.ivjReportBaseClass.setTatolRowShow(true);

        BillData bd = this.ivjReportBaseClass.getBillData();
        if (bd == null)
        {
          SCMEnv.out("--> billdata null.");
          return this.ivjReportBaseClass;
        }
      }
      catch (Throwable ivjExc) {
        handleException(ivjExc);
      }
    }
    return this.ivjReportBaseClass;
  }

  protected ReportItem getReportItemHidden(String sKey, String sTitle) {
    ReportItem biAddItem = new ReportItem();
    biAddItem.setName(sTitle);
    biAddItem.setKey(sKey);
    biAddItem.setDataType(0);
    biAddItem.setLength(80);
    biAddItem.setWidth(80);
    biAddItem.setEnabled(false);
    biAddItem.setShow(false);
    biAddItem.setShowOrder(1);
    biAddItem.setPos(1);
    return biAddItem;
  }

  public static ReportItem getReportItem(String sKey, String sTitle, int[] iaScale)
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

    if (sKey != null)
    {
      if ((sKey.indexOf("mny") >= 0) || (sKey.indexOf("num") >= 0) || (sKey.indexOf("price") >= 0)) {
        biAddItem.setTatol(true);
      }
    }
    if (sKey != null) {
      if (sKey.indexOf("mny") >= 0) {
        biAddItem.setDecimalDigits(iaScale[4]);
      }
      else if ((sKey.indexOf("astnum") >= 0) || (sKey.indexOf("assistnum") >= 0)) {
        biAddItem.setDecimalDigits(iaScale[1]);
      }
      else {
        biAddItem.setDecimalDigits(iaScale[0]);
      }
    }

    if (sTitle != null)
      if (sTitle.indexOf(NCLangRes.getInstance().getStrByID("common", "UC000-0004112")) >= 0)
      {
        biAddItem.setDecimalDigits(iaScale[4]);
      } else if ((sTitle.indexOf(NCLangRes.getInstance().getStrByID("4008ui", "UPP4008ui-000021")) >= 0) || (sTitle.indexOf(NCLangRes.getInstance().getStrByID("common", "UC000-0003971")) >= 0))
      {
        biAddItem.setDecimalDigits(iaScale[1]);
      } else if (sTitle.indexOf(NCLangRes.getInstance().getStrByID("common", "UC000-0002282")) >= 0)
      {
        biAddItem.setDecimalDigits(iaScale[0]);
      }
    return biAddItem;
  }

  public String[] getSums()
  {
    ReportItem[] ra = getReportBaseClass().getBody_Items();

    ArrayList al = new ArrayList();
    for (int i = 0; i < ra.length; i++)
    {
      if ((ra[i] == null) || (
        (!ra[i].getKey().endsWith("num")) && (!ra[i].getKey().endsWith("mny"))))
        continue;
      al.add(ra[i].getKey());
    }

    if (al.size() > 0) return (String[])(String[])al.toArray(new String[al.size()]);

    return null;
  }

  protected String[] getSumCol()
  {
    ReportItem[] riNow = getReportBaseClass().getBody_Items();

    String[] saSumCol = null;

    String sItemKey = null;
    Vector vSumCol = new Vector();
    if ((riNow != null) && (riNow.length > 0)) {
      for (int i = 0; i < riNow.length; i++) {
        if (riNow[i] != null) {
          sItemKey = riNow[i].getKey();
          if ((sItemKey.indexOf("mny") < 0) && (sItemKey.indexOf("num") < 0))
            continue;
          vSumCol.addElement(sItemKey);
        }
      }
    }
    if (vSumCol.size() > 0) {
      saSumCol = new String[vSumCol.size()];
      vSumCol.copyInto(saSumCol);
    }

    return saSumCol;
  }

  public String getTitle()
  {
    if (getReportBaseClass().getReportTitle() != null) {
      return getReportBaseClass().getReportTitle();
    }
    return NCLangRes.getInstance().getStrByID("4008report", "UPP4008report-000078");
  }

  private void handleException(Throwable exception)
  {
    SCMEnv.out("--------- 未捕捉到的异常 ---------");
    SCMEnv.error(exception);
  }

  private String getLastMultiCol()
  {
    String sLastKey = null;
    if (getQueryParam().isJHJEShow())
      sLastKey = "ntermonhandplanmny";
    else if (getQueryParam().isCanKaoMnyShow())
      sLastKey = "ntermonhandcankaomny";
    else if (getQueryParam().isMnyShow())
      sLastKey = "ntermonhandmny";
    else if (getQueryParam().isMZShow())
      sLastKey = "ntermonhandgrossnum";
    else if (getQueryParam().isAstNumShow())
      sLastKey = "ntermonhandastnum";
    else
      sLastKey = "ntermonhandnum";
    return sLastKey;
  }

  private void splitReportItem()
  {
    if ((this.m_voaDefaultRI == null) || (this.m_voaDefaultRI.length == 0)) return;

    ArrayList alNotJC_Before = new ArrayList();
    ArrayList alNotJC_Back = new ArrayList();
    int iOrder = -1;

    for (int i = 0; i < this.m_voaDefaultRI.length; i++) {
      if (this.m_voaDefaultRI[i].getKey().equals(getLastMultiCol())) {
        iOrder = this.m_voaDefaultRI[i].getShowOrder();
      }
    }

    for (int i = 0; i < this.m_voaDefaultRI.length; i++) {
      if ((this.m_voaDefaultRI[i].getKey().endsWith("num")) || (this.m_voaDefaultRI[i].getKey().endsWith("mny")) || (!this.m_voaDefaultRI[i].isShow()) || (this.m_voaDefaultRI[i].getShowOrder() >= iOrder))
      {
        continue;
      }

      alNotJC_Before.add(this.m_voaDefaultRI[i]);
    }

    for (int i = 0; i < this.m_voaDefaultRI.length; i++) {
      if ((this.m_voaDefaultRI[i].getKey().endsWith("num")) || (this.m_voaDefaultRI[i].getKey().endsWith("mny")) || (!this.m_voaDefaultRI[i].isShow()) || (this.m_voaDefaultRI[i].getShowOrder() <= iOrder))
      {
        continue;
      }

      alNotJC_Back.add(this.m_voaDefaultRI[i]);
    }

    this.m_riDefaultNotGroup = ((ReportItem[])(ReportItem[])alNotJC_Before.toArray(new ReportItem[alNotJC_Before.size()]));
    this.m_riDefaultNotGroup_Back = alNotJC_Back;
  }

  private void initialize()
  {
    try
    {
      this.m_btnPrint.setVisible(true);
      this.m_btnPreview.setVisible(true);
      setButtons(getButtonArray(this.m_aryButtonGroup));
      getCEnvInfo();
      setName("ClientUI");
      setSize(774, 419);
      add(getReportBaseClass(), "Center");
      setTotalAst(false);
    } catch (Throwable ivjExc) {
      handleException(ivjExc);
    }

    try
    {
      if (getReportBaseClass().getHeadItem("dbilldatefrom") != null) {
        getReportBaseClass().getHeadItem("dbilldatefrom").setEnabled(false);
      }
      if (getReportBaseClass().getHeadItem("dbilldateto") != null)
        getReportBaseClass().getHeadItem("dbilldateto").setEnabled(false);
    }
    catch (Exception e)
    {
    }
    try
    {
      getReportBaseClass().setMaxLenOfHeadItem("qrycondition", 500);
      getReportBaseClass().setMaxLenOfHeadItem("unitname", 100);
    }
    catch (Exception e)
    {
    }

    initSysParam();

    setScale();

    this.m_voaDefaultRI = getReportBaseClass().getBody_Items();

    initParam();

    splitReportItem();

    rebuildRdTypeFieldGroup(false, null, null);

    setBtnState();
  }

  public QueryCondCtl getQueryParam()
  {
    if (this.m_queryParam == null) {
      this.m_queryParam = new QueryCondCtl();
    }
    return this.m_queryParam;
  }

  private static boolean isReportItemIn(ReportItem[] ra, String pattern)
  {
    HashSet set = new HashSet();
    for (int i = 0; i < ra.length; i++)
    {
      if (ra[i] != null) {
        set.add(ra[i].getKey());
      }
    }
    Iterator iter = set.iterator();
    while (iter.hasNext())
    {
      if (iter.next().toString().endsWith(pattern)) {
        return true;
      }
    }
    return false;
  }

  private void initParam()
  {
    int len = this.m_voaDefaultRI.length;

    if (!isReportItemIn(this.m_voaDefaultRI, "onhandmny")) {
      getQueryParam().setIsMnyShow(false);
    }

    if (!isReportItemIn(this.m_voaDefaultRI, "astnum")) {
      getQueryParam().setIsAstNumShow(false);
    }

    if (!isReportItemIn(this.m_voaDefaultRI, "grossnum")) {
      getQueryParam().setIsMZShow(false);
    }

    if (!isReportItemIn(this.m_voaDefaultRI, "onhandplanmny")) {
      getQueryParam().setIsJHJEShow(false);
    }

    if (!isReportItemIn(this.m_voaDefaultRI, "cankaomny"))
      getQueryParam().setIsCanKaoMnyShow(false);
  }

  protected void initSysParam()
  {
    try
    {
      this.m_sTrackedBillFlag = "N";

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
        if (saParamValue[5] != null) {
          this.m_sTrackedBillFlag = saParamValue[5].toUpperCase().trim();
        }
      }
      this.m_alUserCorpID = ((ArrayList)alRetData.get(1));
    }
    catch (Exception e) {
      SCMEnv.out("can not get para" + e.getMessage());
      if ((e instanceof BusinessException))
        showErrorMessage(e.getMessage());
    }
  }

  public static void main(String[] args)
  {
    try
    {
      JFrame frame = new JFrame();

      ClientUI aClientUI = new ClientUI();
      frame.setContentPane(aClientUI);
      frame.setSize(aClientUI.getSize());
      frame.addWindowListener(new WindowAdapter() {

          public void windowClosing(WindowEvent e)
          {
              System.exit(0);
          }

      });

      frame.show();
      Insets insets = frame.getInsets();
      frame.setSize(frame.getWidth() + insets.left + insets.right, frame.getHeight() + insets.top + insets.bottom);

      frame.setVisible(true);
    } catch (Throwable exception) {
      SCMEnv.out("nc.ui.pub.ToftPanel 的 main() 中发生异常");
      SCMEnv.error(exception);
    }
  }

  public void onButtonClicked(ButtonObject bo)
  {
    if (bo == this.m_boQuery)
    {
      onQuery(true);
      setBtnState();
    }
    else {
      super.onButtonClicked(bo);
    }
  }

  public void onQuery(boolean bQuery)
  {
    try
    {
      if ((bQuery) || (!this.m_bEverQry))
      {
        getConditionDlg().showModal();

        this.m_bEverQry = true;
      }
      else {
        getConditionDlg().onButtonConfig();
      }

      if (!getConditionDlg().isCloseOK()) return;
      String[] selectCorps = getConditionDlg().getSelectedCorpIDs();
      if ((selectCorps == null) || (selectCorps.length <= 0)) {
        showWarningMessage(NCLangRes.getInstance().getStrByID("4008report", "UPP4008report-000186"));

        onQuery(true);
        return;
      }

      getQueryParam().setParam("公司条件", selectCorps);

      setDlgSubTotal(null);

      ConditionVO[] voaCond = getConditionDlg().getConditionVO();

      UFDate dtStartDate = null; UFDate dtEndDate = null;

      String sQryLevel = "1";

      String sInvClassLevel = "2";

      StringBuffer sbMsg = new StringBuffer();

      String sFieldCode = null;
      String sPk_calbody = null;
      String cwarehouseid = null;
      String sClassCode = null;

      if (voaCond != null) {
        for (int i = 0; i < voaCond.length; i++) {
          if ((voaCond[i] != null) && (voaCond[i].getFieldCode() != null)) {
            sFieldCode = voaCond[i].getFieldCode().trim();
            if ("like".equals(voaCond[i].getOperaCode())) {
              if ("dept.deptcode".equals(sFieldCode)) {
                voaCond[i].setValue(voaCond[i].getValue() + "%");
              }
              else {
                voaCond[i].setValue("%" + voaCond[i].getValue() + "%");
              }

            }
            else if ("datetype".equals(sFieldCode)) {
              getQueryParam().setParam("日期选择", voaCond[i].getValue());

              voaCond[i].setDataType(1);
              voaCond[i].setFieldCode("1");
              voaCond[i].setOperaCode("=");
              voaCond[i].setValue("1");
            }
            else if ("dbilldatefrom".equals(voaCond[i].getFieldCode().trim()))
            {
              if ((voaCond[i].getValue() == null) || (voaCond[i].getValue().trim().length() <= 0)) {
                continue;
              }
              dtStartDate = new UFDate(voaCond[i].getValue().trim());
            }
            else if ("dbilldateto".equals(voaCond[i].getFieldCode().trim()))
            {
              if ((voaCond[i].getValue() == null) || (voaCond[i].getValue().trim().length() <= 0))
                continue;
              dtEndDate = new UFDate(voaCond[i].getValue().trim());
            }
            else if ("invclasslev".equals(sFieldCode)) {
              sInvClassLevel = voaCond[i].getValue();
            }
            else if (("invclasscode".equals(sFieldCode)) && (voaCond[i].getValue() != null))
            {
              if ("=".equals(voaCond[i].getOperaCode().trim())) {
                sClassCode = voaCond[i].getValue();
                voaCond[i].setOperaCode(" LIKE ");
                voaCond[i].setValue(voaCond[i].getValue() + "%");
              }

              voaCond[i].setFieldCode("invcl.invclasscode");
            }
            else if (("rdcl.pk_rdcl".equals(sFieldCode)) && (voaCond[i].getValue() != null))
            {
              getQueryParam().setHasRDValue(true);
            } else if ("kp.ccalbodyid".equals(sFieldCode)) {
              if (voaCond[i].getRefResult() != null)
                sPk_calbody = voaCond[i].getRefResult().getRefPK();
            }
            else {
              if ((!"cwarehouseid".equals(sFieldCode)) || 
                (voaCond[i].getRefResult() == null)) continue;
              cwarehouseid = voaCond[i].getRefResult().getRefPK();
            }
          }

        }

      }

      getQueryParam().setIsProvider(getQuryNormalPanel().getchbProvider().isSelected());

      sQryLevel = String.valueOf(getQuryNormalPanel().getUIlistINVLevel().getSelectedIndex());
      if ((sQryLevel == null) || (sQryLevel.trim().length() == 0)) sQryLevel = "1";
      if ((sQryLevel.trim().charAt(0) > '5') || (sQryLevel.trim().charAt(0) < '0')) {
        sbMsg.append(NCLangRes.getInstance().getStrByID("4008report", "UPP4008report-000079"));
      }

      boolean bQryInvClass = false;

      if ("0".equals(sQryLevel))
      {
        bQryInvClass = true;
        if ((sInvClassLevel == null) || (sInvClassLevel.trim().length() == 0)) {
          sbMsg.append(NCLangRes.getInstance().getStrByID("4008report", "UPP4008report-000187"));
        }

      }

      if (("4".equals(sQryLevel)) || ("5".equals(sQryLevel))) {
        getQueryParam().setIsAstNumShow(true);
      }

      if (getQuryNormalPanel().getradioCalbody().isSelected()) {
        this.m_warehouselevel = "库存组织";
        if (getQuryNormalPanel().getchbWarehouse().isSelected())
          this.m_whlevelassistant = "Y";
        else
          this.m_whlevelassistant = "N";
      } else if (getQuryNormalPanel().getradioCorp().isSelected()) {
        this.m_warehouselevel = "公司";
        if (getQuryNormalPanel().getchbCalbody().isSelected())
          this.m_whlevelassistant = "Y";
        else
          this.m_whlevelassistant = "N";
      } else if (getQuryNormalPanel().getradioWareHouseid().isSelected()) {
        this.m_warehouselevel = "仓库";
        if (getQuryNormalPanel().getchbCargdoc().isSelected())
          this.m_whlevelassistant = "Y";
        else {
          this.m_whlevelassistant = "N";
        }
      }

      getQueryParam().setIsHidWarehouseTransfer(getQuryNormalPanel().getchbHidwarehtransfer().isSelected());

      getQueryParam().setIsSplitRd(getQuryNormalPanel().getchbSplitdispatcher().isSelected());

      String sErrMessage = checkContionVOGroupType(sPk_calbody, cwarehouseid, this.m_warehouselevel);

      if ((sErrMessage != null) && (sErrMessage.length() > 0)) {
        sbMsg.append(sErrMessage);
      }

      if (dtStartDate == null)
        dtStartDate = getDefaultStartDate();
      if (dtEndDate == null)
        dtEndDate = getDefaultEndDate();
      if (dtStartDate.after(dtEndDate)) {
        sbMsg.append(NCLangRes.getInstance().getStrByID("4008report", "UPP4008report-000041"));
      }

      if (sbMsg.toString().length() > 0) {
        showErrorMessage(sbMsg.toString());
        onQuery(true);
        return;
      }

      QryConditionVO voCond = new QryConditionVO();
      voCond.setParam(1, sQryLevel);
      getQueryParam().setSignBillOnly(getQuryNormalPanel().getchbIsSign().isSelected());

      voCond.setParam(4, dtStartDate);

      voCond.setParam(5, dtEndDate);

      String sCurClass = null;
      if ((bQryInvClass) && (sInvClassLevel != null) && (sClassCode != null)) {
        int iCurLevel = Integer.parseInt(sInvClassLevel);
        sCurClass = getInvClassCode(iCurLevel, getAryLevelLen(), sClassCode);

        if (sCurClass == null)
          sCurClass = "";
        sCurClass = sCurClass + "%";
        IcBaseReportComm.setContionValue(voaCond, "invcl.invclasscode", sCurClass);
      }

      voCond.setParam(6, sCurClass);
      voCond.setParam(7, sInvClassLevel);
      voCond.setParam(10, voaCond);
      voCond.setParam(15, this.m_warehouselevel);

      voCond.setParam(16, this.m_whlevelassistant);

      voCond.setParam(20, Integer.toString(this.m_iaScale[3]));

      Vector vDeleteKeys = new Vector();
      vDeleteKeys.add("pk_corp");
      vDeleteKeys.add("qrylevel");
      vDeleteKeys.add("dbilldatefrom");
      vDeleteKeys.add("dbilldateto");
      vDeleteKeys.add("invclasslev");

      if (bQryInvClass) {
        vDeleteKeys.add("cinventoryid");
        vDeleteKeys.add("kp.cinventoryid");
        vDeleteKeys.add("h.cinventoryid");
        vDeleteKeys.add("tb1.cinventoryid");
        vDeleteKeys.add("tb.cinventoryid");
      }
      String[] saItemKey = new String[vDeleteKeys.size()];
      vDeleteKeys.copyInto(saItemKey);
      filterCondVO2(voaCond, saItemKey);
      voCond.setQryCond(getConditionDlg().getWhereSQL(voaCond));

      if (!bQryInvClass) setCinvHeader(voaCond);

      
      //edit by zwx 2019-11-14 增加过滤印铁的仓库：宝印剪切库（作废）、宝印成品库(作废)、宝印半成品库(封存)
      ConditionVO[] voaOld = voaCond;
      ConditionVO condition = new ConditionVO();
	  condition.setFieldCode("cwarehouseid");
	  condition.setOperaCode("not in ");
	  condition.setDataType(0);
	  condition.setValue("('1020A110000000001WQG','1020A110000000001WQI','1020A110000000001WQM')");
	  ConditionVO[] voaNew = new ConditionVO[voaOld.length+1];
	  System.arraycopy(voaOld, 0, voaNew, 0, voaOld.length);
	  voaNew[voaOld.length]=condition;
	  voCond.setCond(1, getConditionDlg().getWhereSQL(voaNew));
	  //end by zwx 
//      voCond.setCond(1, getConditionDlg().getWhereSQL(voaCond));

      long lTimeAll = System.currentTimeMillis();
      long lTime = System.currentTimeMillis();

      voCond.setParam(22, getQueryParam());

      ArrayList alRet = (ArrayList)LongTimeTask.callICService(this, "正在查询数据,请稍候...", "nc.bs.ic.ic641.RcvDlvOnhandBO", "queryRcvDlvOnhand", new Class[] { QryConditionVO.class }, new Object[] { voCond });

      showTime(lTime, "数据库查询时间");

      if ((alRet == null) || (alRet.size() == 0) || (alRet.get(0) == null)) {
        RcvDlvOnhandHeaderVO voHead = new RcvDlvOnhandHeaderVO();
        getReportBaseClass().setHeadDataVO(voHead);
        getReportBaseClass().getBillModel().clearBodyData();
        showHintMessage(NCLangRes.getInstance().getStrByID("4008report", "UPP4008report-000042"));

        return;
      }

      showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000133"));

      RcvDlvOnhandItemVO[] voaF = (RcvDlvOnhandItemVO[])(RcvDlvOnhandItemVO[])alRet.get(0);
      RcvDlvOnhandVO voFA = new RcvDlvOnhandVO();
      RcvDlvOnhandHeaderVO voHead = new RcvDlvOnhandHeaderVO();
      voHead.setDbilldatefrom(dtStartDate);
      voHead.setDbilldateto(dtEndDate);

      voHead.setQrycondition(getConditionDlg().getChText());

      boolean bSplited = false;

      if (getQueryParam().isSplitRd())
      {
        ArrayList aRdTypeNameIn = null;
        ArrayList aRdTypeNameOut = null;
        if (getQueryParam().isSplitRd())
        {
          if ((alRet != null) && (alRet.size() >= 4))
          {
            aRdTypeNameIn = (ArrayList)alRet.get(1);
            aRdTypeNameOut = (ArrayList)alRet.get(2);
            bSplited = ((Boolean)alRet.get(3)).booleanValue();
          }
          if (bSplited)
          {
            rebuildRdTypeFieldGroup(true, aRdTypeNameIn, aRdTypeNameOut);
          }
          else {
            rebuildRdTypeFieldGroup(false, null, null);
            showHintMessage(NCLangRes.getInstance().getStrByID("4008report", "UPP4008report-000080"));
          }

        }

      }
      else
      {
        rebuildRdTypeFieldGroup(false, null, null);
      }

      hiddenColumn(this.m_warehouselevel, this.m_whlevelassistant, sQryLevel);

      getReportBaseClass().setHeadDataVO(voHead);

      setReportData(voaF, sQryLevel);

      this.m_voReport = voFA;
      this.m_voReport.setParentVO(getReportBaseClass().getHeadDataVO());
      this.m_voReport.setChildrenVO(getReportBaseClass().getBodyDataVO());

      getReportBaseClass().updateValue();

      calculateTotal();

      showTime(lTimeAll, "全部时间" + voaF.length + "条记录");
    } catch (Exception e) {
      SCMEnv.error(e);
      showErrorMessage(NCLangRes.getInstance().getStrByID("4008report", "UPP4008report-000004") + e.getMessage());
    }
  }

  public static void procFieldGroupData(int iKeyIndex, Vector vAddReportItem, Vector vFieldGroup, String sTitle, String sSubTitle, String[] saColKey, String[] saColTitle, boolean bRetTopLevel, ArrayList alOtherParam, int[] iaScale)
  {
    if ((sTitle == null) || (saColTitle == null) || (saColTitle.length == 0) || (saColKey == null) || (saColKey.length != saColTitle.length) || (vAddReportItem == null) || (vFieldGroup == null))
    {
      SCMEnv.out("param null");
      return;
    }

    ReportItem riTemp = null;
    if (iKeyIndex >= 0)
      riTemp = getReportItem(saColKey[0] + iKeyIndex, saColTitle[0], iaScale);
    else
      riTemp = getReportItem(saColKey[0], saColTitle[0], iaScale);
    vAddReportItem.addElement(riTemp);

    int iColNum = saColTitle.length;

    FldgroupVO voFg = null;

    if (iColNum == 1) {
      voFg = new FldgroupVO();
      voFg.setGroupid(new Integer(0));
      voFg.setItem1("" + (vAddReportItem.size() - 1));
      voFg.setItem2(sSubTitle);
      voFg.setGrouptype("1");
      if (bRetTopLevel)
        voFg.setToplevelflag("Y");
      else
        voFg.setToplevelflag("N");
      voFg.setGroupname(sTitle);
      vFieldGroup.addElement(voFg);
      return;
    }

    if (iColNum > 1) {
      if (iKeyIndex >= 0)
        riTemp = getReportItem(saColKey[1] + iKeyIndex, saColTitle[1], iaScale);
      else
        riTemp = getReportItem(saColKey[1], saColTitle[1], iaScale);
      vAddReportItem.addElement(riTemp);

      voFg = new FldgroupVO();
      voFg.setGroupid(new Integer(0));
      voFg.setItem1("" + (vAddReportItem.size() - 1));
      voFg.setItem2("" + (vAddReportItem.size() - 2));
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
        riTemp = getReportItem(saColKey[col] + iKeyIndex, saColTitle[col], iaScale);
      }
      else
        riTemp = getReportItem(saColKey[col], saColTitle[col], iaScale);
      vAddReportItem.addElement(riTemp);

      voFg = new FldgroupVO();
      voFg.setGroupid(new Integer(0));
      voFg.setItem1("" + (vAddReportItem.size() - 1));
      voFg.setItem2(sSubTitle);

      voFg.setGrouptype("1");

      if ((col == iColNum - 1) && (sTitle.equals(sSubTitle))) {
        if (bRetTopLevel)
          voFg.setToplevelflag("Y");
        else
          voFg.setToplevelflag("N");
      }
      else voFg.setToplevelflag("N");

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
            break;
        }
      }
      if (iSearch >= vFieldGroup.size()) {
        voFg = new FldgroupVO();
        voFg.setGroupid(new Integer(0));
        voFg.setToplevelflag("N");
        voFg.setItem1(sSubTitle);
        voFg.setItem2(sSubTitle);
        voFg.setGrouptype("3");
        voFg.setGroupname(sTitle);
        vFieldGroup.addElement(voFg);
      }
      else {
        if (voTemporaryField.getItem1().equals(voTemporaryField.getItem2()))
        {
          vFieldGroup.removeElementAt(iSearch);
        }voFg = new FldgroupVO();
        voFg.setGroupid(new Integer(0));
        if (bRetTopLevel)
          voFg.setToplevelflag("Y");
        else
          voFg.setToplevelflag("N");
        voFg.setItem1(sSubTitle);
        if (voTemporaryField.getItem1().equals(voTemporaryField.getItem2()))
        {
          voFg.setItem2(voTemporaryField.getItem1());
        }
        else
          voFg.setItem2(sTitle);
        voFg.setGrouptype("3");
        voFg.setGroupname(sTitle);
        vFieldGroup.addElement(voFg);
      }
    }
  }

  protected void setCinvHeader(ConditionVO[] voaCond)
  {
    if (voaCond != null)
      for (int i = 0; i < voaCond.length; i++) {
        if ((voaCond[i] == null) || (voaCond[i].getFieldCode() == null) || (!"cinventoryid".equals(voaCond[i].getFieldCode().trim())))
        {
          continue;
        }
        if ("Y".equals(this.m_sTrackedBillFlag))
          voaCond[i].setFieldCode("tb1.cinventoryid");
        else
          voaCond[i].setFieldCode("kp.cinventoryid");
      }
  }

  protected void setScale()
  {
    ScaleInit si = new ScaleInit(this.m_sUserID, this.m_sCorpID);

    String[] saBodyNumItemKey = { "ntermbeginnum", "nterminnum", "ntermoutnum", "ntermonhandnum" };

    String[] saBodyAstNumItemKey = { "nterminastnum", "ntermbeginastnum", "ntermoutastnum", "ntermonhandastnum" };

    String[] saBodyPrice = { "nprice", "nplannedprice", "ncankaoprice", "nlastplanprice" };

    String[] saBodyMny = { "ntermbeginmny", "nterminmny", "ntermoutmny", "ntermonhandmny" };

    String[] saBodyHSL = { "hsl" };

    ArrayList alParam = new ArrayList();
    alParam.add(saBodyNumItemKey);
    alParam.add(saBodyAstNumItemKey);
    alParam.add(saBodyPrice);
    alParam.add(saBodyMny);
    alParam.add(saBodyHSL);
    try {
      si.setScale(getReportBaseClass(), alParam);
    } catch (Exception e) {
      showHintMessage(NCLangRes.getInstance().getStrByID("4008report", "UPP4008report-000003") + e.getMessage());
    }
  }

  protected void showTime(long lStartTime, String sTaskHint)
  {
    long lTime = System.currentTimeMillis() - lStartTime;
    SCMEnv.out("执行<" + sTaskHint + ">消耗的时间为：" + lTime / 60000L + "分" + lTime / 1000L % 60L + "秒" + lTime % 1000L + "毫秒");
  }

  private String checkContionVOGroupType(String sPk_calbody, String sCwarehouseid, String sWarehouselevel)
  {
    String sErroMsg = "";
    if (!"库存组织".equalsIgnoreCase(sWarehouselevel))
    {
      if (("货位".equalsIgnoreCase(sWarehouselevel)) && 
        (sCwarehouseid == null)) {
        sErroMsg = NCLangRes.getInstance().getStrByID("4008report", "UPP4008report-000085");
      }

    }

    return sErroMsg;
  }

  private int[] getAryLevelLen()
  {
    try
    {
      if (this.m_AryLevelLen == null)
        this.m_AryLevelLen = LocalModel.getCodeRule();
    }
    catch (Exception e) {
      SCMEnv.out(e.toString());
    }
    return this.m_AryLevelLen;
  }

  public String getCorpID()
  {
    return this.m_sCorpID;
  }

  private Vector getHiddenBillItem()
  {
    if ((this.m_vBillItemHiddenID == null) || (this.m_vBillItemHiddenID.size() == 0)) {
      this.m_vBillItemHiddenID = new Vector();
      if (this.m_voaDefaultRI != null) {
        for (int i = 0; i < this.m_voaDefaultRI.length; i++)
          if (!this.m_voaDefaultRI[i].isShow())
            this.m_vBillItemHiddenID.addElement(this.m_voaDefaultRI[i]);
      }
    }
    return this.m_vBillItemHiddenID;
  }

  public String getInvClassCode(int iInvClassLevel, int[] aryCodeLevel, String sRootClassCode)
  {
    if ((aryCodeLevel == null) || (sRootClassCode == null) || (iInvClassLevel == 0))
    {
      return "";
    }
    if ((iInvClassLevel < 0) || (iInvClassLevel >= aryCodeLevel.length)) {
      return sRootClassCode;
    }
    String sCurClasscode = null;
    int iCurLen = 0;
    for (int i = 0; i < aryCodeLevel.length; i++) {
      iCurLen += aryCodeLevel[i];
      if (iInvClassLevel == i + 1)
        break;
    }
    if (iCurLen > sRootClassCode.length())
      sCurClasscode = sRootClassCode;
    else
      sCurClasscode = sRootClassCode.substring(0, iCurLen);
    return sCurClasscode;
  }

  private QuryNormalPanel getQuryNormalPanel() {
    if (this.m_quryNormalPanel == null)
      this.m_quryNormalPanel = new QuryNormalPanel(this.m_sQryLevelIndex);
    return this.m_quryNormalPanel;
  }

  public AggregatedValueObject getReportVO()
  {
    RcvDlvOnhandVO vo = (RcvDlvOnhandVO)getReportBaseClass().getBillValueVO(this.m_sVOName, this.m_sHeaderVOName, this.m_sItemVOName);

    if (null == vo) {
      vo = new RcvDlvOnhandVO();
    }
    if (null == vo.getParentVO()) {
      vo.setParentVO(new RcvDlvOnhandHeaderVO());
    }
    return vo;
  }

  public String getUserID()
  {
    return this.m_sUserID;
  }

  private void hiddenColumn(String sWarehouselevel, String sWhlevelassistant, String sQryLevel)
  {
    boolean isShowAssistant = false;
    if ("Y".equalsIgnoreCase(sWhlevelassistant))
      isShowAssistant = true;
    if ("公司".equalsIgnoreCase(sWarehouselevel)) {
      hiddenShowColumn(isShowAssistant, this.m_arysCalColumnItemkey);
      hiddenShowColumn(false, this.m_arysWareColumnItemkey);
      hiddenShowColumn(false, this.m_arysCargdocItemkey);
    } else if ("库存组织".equalsIgnoreCase(sWarehouselevel)) {
      hiddenShowColumn(true, this.m_arysCalColumnItemkey);
      hiddenShowColumn(false, this.m_arysCargdocItemkey);
      hiddenShowColumn(isShowAssistant, this.m_arysWareColumnItemkey);
    }
    else if ("仓库".equalsIgnoreCase(sWarehouselevel)) {
      hiddenShowColumn(false, this.m_arysCalColumnItemkey);
      hiddenShowColumn(true, this.m_arysWareColumnItemkey);
      hiddenShowColumn(isShowAssistant, this.m_arysCargdocItemkey);
    }

    if (sQryLevel != null)
    {
      sQryLevel = sQryLevel.trim();

      hiddenShowColumn(false, this.m_arysInvItemkey);
      char cQryLevel = sQryLevel.charAt(0);

      if (cQryLevel == '0') {
        hiddenShowColumn(true, "invclassname");
        hiddenShowColumn(true, "invclasscode");
      }
      else {
        if (getQueryParam().isProvider())
          hiddenShowColumn(true, "cprovidername");
        else {
          hiddenShowColumn(false, "cprovidername");
        }
        hiddenShowColumn(true, "cinventorycode");
        hiddenShowColumn(true, "invname");
        hiddenShowColumn(true, "invtype");
        hiddenShowColumn(true, "invspec");
        hiddenShowColumn(true, "measdocname");
        if (cQryLevel == '2')
        {
          hiddenShowColumn(true, "vbatchcode");
        } else if (cQryLevel == '3')
        {
          hiddenShowColumn(true, "vfree0");
        } else if (cQryLevel == '4')
        {
          hiddenShowColumn(true, "castunitname");
          hiddenShowColumn(true, "hsl");
        } else if (cQryLevel == '5')
        {
          hiddenShowColumn(true, "vfree0");
          hiddenShowColumn(true, "vbatchcode");
          hiddenShowColumn(true, "castunitname");
          hiddenShowColumn(true, "hsl");
        }

      }

      String[] aryAstnumCol = { "ntermbeginastnum", "nterminastnum", "ntermoutastnum", "ntermonhandastnum" };

      if ((cQryLevel == '4') || (cQryLevel == '5')) {
        hiddenShowColumn(true, aryAstnumCol);
      }
      else
      {
        hiddenShowColumn(false, aryAstnumCol);
      }

    }

    if (!getQueryParam().isMnyShow()) {
      hiddenShowColumn(false, "nprice");
    }

    if (!getQueryParam().isProvider())
      hiddenShowColumn(false, new String[] { "cprovidername" });
    else {
      hiddenShowColumn(true, new String[] { "cprovidername" });
    }
    if (!getQueryParam().isJHJEShow())
      hiddenShowColumn(false, new String[] { "nplannedprice", "nlastplanprice" });
    else
      hiddenShowColumn(true, new String[] { "nplannedprice", "nlastplanprice" });
  }

  private int getNumShow() {
    int num = 1;
    if (getQueryParam().isAstNumShow()) {
      num += 1;
    }
    if (getQueryParam().isMnyShow()) {
      num += 1;
    }
    if (getQueryParam().isJHJEShow()) {
      num += 1;
    }
    if (getQueryParam().isMZShow()) {
      num += 1;
    }
    if (getQueryParam().isCanKaoMnyShow()) {
      num += 1;
    }
    return num;
  }

  private String[] getColTitle(boolean bColKeyOrTitle, int type, boolean bHJ)
  {
    int x = getNumShow();
    String[] sa = new String[x];

    int iWhichWrite = 1;
    if (bColKeyOrTitle) {
      sa[0] = NCLangRes.getInstance().getStrByID("common", "UC000-0002282");
    }
    else {
      if (type == 0)
        sa[0] = "ntermbeginnum";
      if ((type == 1) && (!bHJ))
        sa[0] = "rntermnum";
      else if (type == 1) {
        sa[0] = "nterminnum";
      }
      if ((type == 2) && (!bHJ))
        sa[0] = "dntermnum";
      else if (type == 2) {
        sa[0] = "ntermoutnum";
      }
      if (type == 3) {
        sa[0] = "ntermonhandnum";
      }
    }
    if (getQueryParam().isAstNumShow()) {
      if (bColKeyOrTitle) {
        sa[iWhichWrite] = NCLangRes.getInstance().getStrByID("common", "UC000-0003971");
      }
      else {
        if (type == 0)
          sa[iWhichWrite] = "ntermbeginastnum";
        if ((type == 1) && (!bHJ))
          sa[iWhichWrite] = "rntermastnum";
        else if (type == 1)
          sa[iWhichWrite] = "nterminastnum";
        if ((type == 2) && (!bHJ))
          sa[iWhichWrite] = "dntermastnum";
        else if (type == 2) {
          sa[iWhichWrite] = "ntermoutastnum";
        }
        if (type == 3)
          sa[iWhichWrite] = "ntermonhandastnum";
      }
      iWhichWrite++;
    }

    if (getQueryParam().isMZShow()) {
      if (bColKeyOrTitle) {
        sa[iWhichWrite] = ResBase.getMaoZhong();
      } else {
        if (type == 0)
          sa[iWhichWrite] = "ntermbegingrossnum";
        if ((type == 1) && (!bHJ))
          sa[iWhichWrite] = "rntermgrossnum";
        else if (type == 1) {
          sa[iWhichWrite] = "ntermingrossnum";
        }
        if ((type == 2) && (!bHJ))
          sa[iWhichWrite] = "dntermgrossnum";
        else if (type == 2) {
          sa[iWhichWrite] = "ntermoutgrossnum";
        }
        if (type == 3)
          sa[iWhichWrite] = "ntermonhandgrossnum";
      }
      iWhichWrite++;
    }

    if (getQueryParam().isMnyShow()) {
      if (bColKeyOrTitle) {
        sa[iWhichWrite] = NCLangRes.getInstance().getStrByID("common", "UC000-0003223");
      }
      else {
        if (type == 0)
          sa[iWhichWrite] = "ntermbeginmny";
        if ((type == 1) && (!bHJ))
          sa[iWhichWrite] = "rntermmny";
        else if (type == 1) {
          sa[iWhichWrite] = "nterminmny";
        }
        if ((type == 2) && (!bHJ))
          sa[iWhichWrite] = "dntermmny";
        else if (type == 2) {
          sa[iWhichWrite] = "ntermoutmny";
        }
        if (type == 3)
          sa[iWhichWrite] = "ntermonhandmny";
      }
      iWhichWrite++;
    }

    if (getQueryParam().isJHJEShow()) {
      if (bColKeyOrTitle) {
        sa[iWhichWrite] = NCLangRes.getInstance().getStrByID("common", "UC000-0003522");
      }
      else {
        if (type == 0)
          sa[iWhichWrite] = "ntermbeginplanmny";
        if ((type == 1) && (!bHJ))
          sa[iWhichWrite] = "rntermplanmny";
        else if (type == 1) {
          sa[iWhichWrite] = "nterminplanmny";
        }
        if ((type == 2) && (!bHJ))
          sa[iWhichWrite] = "dntermplanmny";
        else if (type == 2) {
          sa[iWhichWrite] = "ntermoutplanmny";
        }
        if (type == 3)
          sa[iWhichWrite] = "ntermonhandplanmny";
      }
      iWhichWrite++;
    }

    if (getQueryParam().isCanKaoMnyShow()) {
      if (bColKeyOrTitle) {
        sa[iWhichWrite] = ResBase.getCanKaoMny();
      } else {
        if (type == 0)
          sa[iWhichWrite] = "ntermbegincankaomny";
        if ((type == 1) && (!bHJ))
          sa[iWhichWrite] = "rntermcankaomny";
        else if (type == 1) {
          sa[iWhichWrite] = "ntermincankaomny";
        }
        if ((type == 2) && (!bHJ))
          sa[iWhichWrite] = "dntermcankaomny";
        else if (type == 2) {
          sa[iWhichWrite] = "ntermoutcankaomny";
        }
        if (type == 3)
          sa[iWhichWrite] = "ntermonhandcankaomny";
      }
      iWhichWrite++;
    }
    return sa;
  }

  public void rebuildRdTypeFieldGroup(boolean bSplit, ArrayList alRdTypeNameIn, ArrayList alRdTypeNameOut)
  {
    try
    {
      if ((alRdTypeNameOut == null) || (alRdTypeNameOut.size() == 0)) {
        alRdTypeNameOut = new ArrayList();
        alRdTypeNameOut.add("无类别");
      }

      if ((alRdTypeNameIn == null) || (alRdTypeNameIn.size() == 0)) {
        alRdTypeNameIn = new ArrayList();
        alRdTypeNameIn.add("无类别");
      }

      ReportItem[] biReport = this.m_riDefaultNotGroup;

      if ((biReport == null) || (biReport.length == 0)) {
        SCMEnv.out("no column.");
        return;
      }

      int iInRdtypeCount = 0;
      int iOutRdtypeCount = 0;

      if (alRdTypeNameIn != null)
        iInRdtypeCount = alRdTypeNameIn.size();
      if (alRdTypeNameOut != null) {
        iOutRdtypeCount = alRdTypeNameOut.size();
      }
      if (!bSplit) {
        iInRdtypeCount = 0;
        iOutRdtypeCount = 0;
      }

      Vector vBillItem = new Vector();
      for (int col = 0; col < biReport.length; col++) {
        if (biReport[col] != null) {
          vBillItem.addElement(biReport[col]);
        }
      }
      int iRdCount = 0;

      Vector vFieldGroup = new Vector();

      String sTopTitle = NCLangRes.getInstance().getStrByID("40083802", "UPT40083802-000003");

      String[] saColTitle = getColTitle(true, 0, false);
      String[] saColKey = getColTitle(false, 0, false);
      int iShowFieldNum = saColTitle.length;

      if (iShowFieldNum == 1) {
        vBillItem.addElement(getReportItem("ntermbeginnum", sTopTitle + saColTitle[0], this.m_iaScale));
      }
      else {
        procFieldGroupData(-1, vBillItem, vFieldGroup, sTopTitle, sTopTitle, saColKey, saColTitle, true, null, this.m_iaScale);
      }

      sTopTitle = NCLangRes.getInstance().getStrByID("4008report", "UPP4008report-000188");

      saColTitle = getColTitle(true, 1, false);
      saColKey = getColTitle(false, 1, false);

      String sColName = null;
      if (getQueryParam().isSplitRd() == true)
      {
        for (int rdtype = 0; rdtype < iInRdtypeCount; rdtype++)
        {
          sColName = null;
          if ((alRdTypeNameIn != null) && (alRdTypeNameIn.size() > iRdCount) && (alRdTypeNameIn.get(rdtype) != null))
          {
            sColName = alRdTypeNameIn.get(rdtype).toString().trim();
          }if ((sColName == null) || (sColName.length() == 0)) {
            sColName = NCLangRes.getInstance().getStrByID("4008report", "UPP4008report-000189");
          }

          procFieldGroupData(rdtype, vBillItem, vFieldGroup, sTopTitle, sColName, saColKey, saColTitle, false, null, this.m_iaScale);
        }

      }

      if ((iInRdtypeCount >= 1) || (!getQueryParam().isSplitRd()))
      {
        saColTitle = getColTitle(true, 1, true);
        saColKey = getColTitle(false, 1, true);
        if (getQueryParam().isSplitRd()) {
          sColName = NCLangRes.getInstance().getStrByID("4008report", "UPP4008report-000190");
        }
        else
        {
          sColName = sTopTitle;
        }

        if ((iInRdtypeCount == 0) && (getNumShow() == 1)) {
          vBillItem.addElement(getReportItem("nterminnum", sTopTitle + saColTitle[0], this.m_iaScale));
        }
        else {
          procFieldGroupData(-1, vBillItem, vFieldGroup, sTopTitle, sColName, saColKey, saColTitle, true, null, this.m_iaScale);
        }
      }

      int curIndex = vBillItem.size();
      vBillItem.addElement(null);

      sTopTitle = NCLangRes.getInstance().getStrByID("4008report", "UPP4008report-000191");

      saColTitle = getColTitle(true, 2, false);
      saColKey = getColTitle(false, 2, false);
      if (getQueryParam().isSplitRd())
      {
        for (int rdtype = 0; rdtype < iOutRdtypeCount; rdtype++)
        {
          sColName = null;
          if ((alRdTypeNameOut != null) && (alRdTypeNameOut.size() > iRdCount) && (alRdTypeNameOut.get(rdtype) != null))
          {
            sColName = alRdTypeNameOut.get(rdtype).toString();
          }
          if ((sColName == null) || (sColName.length() == 0)) {
            sColName = NCLangRes.getInstance().getStrByID("4008report", "UPP4008report-000192");
          }

          procFieldGroupData(rdtype, vBillItem, vFieldGroup, sTopTitle, sColName, saColKey, saColTitle, false, null, this.m_iaScale);
        }

      }

      if ((iOutRdtypeCount >= 1) || (!getQueryParam().isSplitRd())) {
        saColTitle = getColTitle(true, 2, true);
        saColKey = getColTitle(false, 2, true);
        if (getQueryParam().isSplitRd()) {
          sColName = NCLangRes.getInstance().getStrByID("4008report", "UPP4008report-000190");
        }
        else
        {
          sColName = sTopTitle;
        }
        if ((iOutRdtypeCount == 0) && (getNumShow() == 1)) {
          vBillItem.addElement(getReportItem("ntermoutnum", sTopTitle + saColTitle[0], this.m_iaScale));
        }
        else {
          procFieldGroupData(-1, vBillItem, vFieldGroup, sTopTitle, sColName, saColKey, saColTitle, true, null, this.m_iaScale);
        }

      }

      sTopTitle = NCLangRes.getInstance().getStrByID("40083802", "UPT40083802-000009");

      saColTitle = getColTitle(true, 3, false);
      saColKey = getColTitle(false, 3, false);

      if (iShowFieldNum == 1) {
        vBillItem.addElement(getReportItem("ntermonhandnum", sTopTitle + saColTitle[0], this.m_iaScale));
      }
      else {
        procFieldGroupData(-1, vBillItem, vFieldGroup, sTopTitle, sTopTitle, saColKey, saColTitle, true, null, this.m_iaScale);
      }

      vBillItem.addAll(getHiddenBillItem());

      vBillItem.addAll(this.m_riDefaultNotGroup_Back);

      int iLen = vBillItem.size();
      for (int index = 0; index < iLen; index++)
      {
        if ((vBillItem.elementAt(index) == null) || 
          (!((ReportItem)vBillItem.elementAt(index)).getKey().equals("nadjustplanmny")))
          continue;
        ReportItem adjustPlanReportItem = new ReportItem();
        adjustPlanReportItem = (ReportItem)vBillItem.elementAt(index);
        vBillItem.removeElementAt(index);
        vBillItem.setElementAt(adjustPlanReportItem, curIndex);
        break;
      }

      setReportItemShowFalse(vBillItem);

      ReportItem[] riaResult = new ReportItem[vBillItem.size()];
      vBillItem.copyInto(riaResult);
      FldgroupVO[] voaFg = new FldgroupVO[vFieldGroup.size()];
      vFieldGroup.copyInto(voaFg);
      getReportBaseClass().setFieldGroup(voaFg);
      getReportBaseClass().setBody_Items(riaResult);
    } catch (Exception e) {
      handleException(e);
    }
  }

  private void setReportItemShowFalse(Vector vBillItem)
  {
    String[] saJH = { "nplannedprice", "nlastplanprice", "nadjustplanmny" };
    HashMap m = new HashMap();
    for (int i = 0; i < saJH.length; i++) {
      m.put(saJH[i], saJH[i]);
    }
    if (!getQueryParam().isJHJEShow()) {
      for (int i = 0; i < vBillItem.size(); i++) {
        ReportItem ite = (ReportItem)vBillItem.get(i);
        if ((ite == null) || 
          (m.get(ite.getKey().trim().toLowerCase()) == null)) continue;
        ite.setShow(false);
      }
    }
    else
      for (int i = 0; i < vBillItem.size(); i++) {
        ReportItem ite = (ReportItem)vBillItem.get(i);
        if ((ite == null) || 
          (m.get(ite.getKey().trim().toLowerCase()) == null)) continue;
        ite.setShow(true);
      }
  }

  private void setReportData(RcvDlvOnhandItemVO[] reportData, String sQryLevel)
  {
    if (sQryLevel == null)
      return;
    ArrayList arylistItemField = new ArrayList();

    int iLevel = Integer.parseInt(sQryLevel);

    ClientCacheHelper.getColValue(reportData, new String[] { "unitcode", "unitname" }, "bd_corp", "pk_corp", new String[] { "unitcode", "unitname" }, "pk_corp");

    ClientCacheHelper.getColValue(reportData, new String[] { "pk_invbasdoc" }, "bd_invmandoc", "pk_invmandoc", new String[] { "pk_invbasdoc" }, "cinventoryid");

    ClientCacheHelper.getColValue(reportData, new String[] { "invname", "cinventorycode", "invspec", "invtype", "pk_measdoc" }, "bd_invbasdoc", "pk_invbasdoc", new String[] { "invname", "invcode", "invspec", "invtype", "pk_measdoc" }, "pk_invbasdoc");

    ClientCacheHelper.getColValue(reportData, new String[] { "measdocname" }, "bd_measdoc", "pk_measdoc", new String[] { "measname" }, "pk_measdoc");

    ClientCacheHelper.getColValue(reportData, new String[] { "pk_cubasdoc" }, "bd_cumandoc", "pk_cumandoc", new String[] { "pk_cubasdoc" }, "cproviderid");

    ClientCacheHelper.getColValue(reportData, new String[] { "cprovidername", "ccustomercode" }, "bd_cubasdoc", "pk_cubasdoc", new String[] { "custname", "custcode" }, "pk_cubasdoc");

    boolean isShowAssistant = false;
    if ("Y".equalsIgnoreCase(this.m_whlevelassistant))
      isShowAssistant = true;
    if ((("公司".equalsIgnoreCase(this.m_warehouselevel)) && (isShowAssistant)) || ("库存组织".equalsIgnoreCase(this.m_warehouselevel)))
    {
      ClientCacheHelper.getColValue(reportData, new String[] { "ccalbodycode", "ccalbodyname" }, "bd_calbody", "pk_calbody", new String[] { "bodycode", "bodyname" }, "ccalbodyid");
    }

    if ((("库存组织".equalsIgnoreCase(this.m_warehouselevel)) && (isShowAssistant)) || ("仓库".equalsIgnoreCase(this.m_warehouselevel)))
    {
      ClientCacheHelper.getColValue(reportData, new String[] { "storcode", "storname" }, "bd_stordoc", "pk_stordoc", new String[] { "storcode", "storname" }, "cwarehouseid");
    }

    if (("仓库".equalsIgnoreCase(this.m_warehouselevel)) && (isShowAssistant))
    {
      ClientCacheHelper.getColValue(reportData, new String[] { "cspacecode", "cspacename" }, "bd_cargdoc", "pk_cargdoc", new String[] { "cscode", "csname" }, "cspaceid");
    }

    if ((iLevel == 4) || (iLevel == 5))
    {
      ClientCacheHelper.getColValue(reportData, new String[] { "castunitname" }, "bd_measdoc", "pk_measdoc", new String[] { "measname" }, "castunitid");
    }

    long lTime = System.currentTimeMillis();
    if ((iLevel == 4) || (iLevel == 5))
    {
      GenMethod method = new GenMethod();
      method.calAllConvRate(reportData, "fixedflag", "hsl", "nterminnum", "nterminastnum", "ntermoutnum", "ntermoutastnum");
    }

    if ((iLevel == 3) || (iLevel == 5)) {
      FreeVOParse freeVOParse = new FreeVOParse();
      freeVOParse.setFreeVO(reportData, "pk_invbasdoc", null, true);
    }
    showTime(lTime, "公式解析查询");

    ArrayList al = new ArrayList();
    ReportItem[] ris = getReportBaseClass().getBody_Items();
    for (int i = 0; i < ris.length; i++) {
      if ((ris[i] == null) || 
        (!ris[i].getKey().endsWith("num"))) continue;
      al.add(ris[i].getKey());
    }

    String[] sa = new String[al.size()];
    al.toArray(sa);

    reportData = (RcvDlvOnhandItemVO[])(RcvDlvOnhandItemVO[])ArrayTools.filtArrayNullOrZero(reportData, sa);

    if (reportData != null)
    {
      UFDouble ZERO = new UFDouble(0);
      if (getQueryParam().isJHJEShow()) {
        for (int i = 0; i < reportData.length; i++) {
          UFDouble ufdHand = (UFDouble)reportData[i].getAttributeValue("ntermonhandplanmny");
          if (ufdHand == null)
            ufdHand = ZERO;
          UFDouble ufdBegin = (UFDouble)reportData[i].getAttributeValue("ntermbeginplanmny");
          if (ufdBegin == null)
            ufdBegin = ZERO;
          UFDouble ufdIn = (UFDouble)reportData[i].getAttributeValue("nterminplanmny");
          if (ufdIn == null)
            ufdIn = ZERO;
          UFDouble ufdOut = (UFDouble)reportData[i].getAttributeValue("ntermoutplanmny");
          if (ufdOut == null) ufdOut = ZERO;

          UFDouble ufdAdjust = ufdHand.add(ufdOut).sub(ufdBegin).sub(ufdIn);
          reportData[i].setAttributeValue("nadjustplanmny", ufdAdjust);
        }
      }
    }

    getReportBaseClass().setBodyDataVO(reportData, true);
  }
}