package nc.ui.ic.ic601;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;
import nc.ui.ic.ic001.BatchCodeDefSetTool;
import nc.ui.ic.pub.bill.query.ICMultiCorpQryClient;
import nc.ui.ic.pub.bill.query.ICheckCondition;
import nc.ui.ic.pub.report.IcBaseReport;
import nc.ui.ic.pub.scale.ScaleInit;
import nc.ui.ic.pub.thread.IPrint;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.FramePanel;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.report.ReportBaseClass;
import nc.ui.scm.pub.FreeVOParse;
import nc.ui.scm.pub.billutil.ClientCacheHelper;
import nc.vo.bd.CorpVO;
import nc.vo.ic.ic601.InvOnHandHeaderVO;
import nc.vo.ic.ic601.InvOnHandItemVO;
import nc.vo.ic.ic601.InvOnHandVO;
import nc.vo.ic.pub.bill.QryConditionVO;
import nc.vo.ic.pub.lang.ResBase;
import nc.vo.logging.Debug;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.query.RefResultVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.sm.UserVO;

public class ClientUI extends IcBaseReport
  implements IPrint, ICheckCondition
{
  private ReportBaseClass ivjReportBase = null;

  private ICMultiCorpQryClient ivjQueryConditionDlg = null;

  private SNDialog m_dlgSN = null;

  private String m_sRNodeName = "40083002SYS";

  private String m_sPNodeCode = "40083002";

  private String m_sUserID = "";

  private String m_sCorpID = null;

  private UFDate m_sLogDate = null;

  private Hashtable m_htShowFlag = new Hashtable();

  private AggregatedValueObject m_voReport = null;

  private ButtonObject m_boQuery = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000006"), NCLangRes.getInstance().getStrByID("common", "UC001-0000006"), 2, "查询");

  private ButtonObject m_boShowSN = new ButtonObject(NCLangRes.getInstance().getStrByID("40083002", "UPT40083002-000002"), NCLangRes.getInstance().getStrByID("40083002", "UPT40083002-000002"), 2, "显示序列号明细");

  private ButtonObject[] m_MainButtonGroup = { this.m_boQuery, this.m_boShowSN };

  boolean m_bClassflag = false;

  boolean m_bHasAssist = false;

  boolean m_bHasBatchcode = false;

  boolean m_bHasCscode = false;

  boolean m_bHasVfree0 = false;

  boolean m_bHasVendor = false;

  boolean m_bHasHsl = false;

  Hashtable m_htField = null;

  public ClientUI()
  {
    initialize();
  }

  public String checkICCondition(ConditionVO[] voCons)
  {
    boolean isflag = false;
    if (voCons != null) {
      for (int i = 0; i < voCons.length; i++) {
        if ((!voCons[i].getFieldCode().equals("vfree0flag")) && (!voCons[i].getFieldCode().equals("cscodeflag")) && (!voCons[i].getFieldCode().equals("vbatchcodeflag")) && (!voCons[i].getFieldCode().equals("measnameflag")))
        {
          continue;
        }
        if ("true".equals(voCons[i].getValue())) {
          isflag = true;
        }
      }

    }

    try
    {
      getConditionDlg().checkOncetime(voCons, new String[] { "vfree0flag", "cscodeflag", "vbatchcodeflag", "invclasscodeflag", "measnameflag", "invclasslev", "num", "assistnum" });

      ArrayList alKey = new ArrayList();
      getConditionDlg().checkAllHaveOrNot(voCons, new String[] { "invclasslev" });
      if (isflag) {
        String[] sStrs1 = { "invclasslev" };
        String[] sStrs2 = { "vfree0flag", "cscodeflag", "vbatchcodeflag", "measnameflag" };

        alKey.add(sStrs1);
        alKey.add(sStrs2);
        getConditionDlg().checkOneNotOther(voCons, alKey, false);
      }

      getConditionDlg().checkOneTrueAnotherMustFillin(voCons, "classflag", "invclasslev", NCLangRes.getInstance().getStrByID("common", "UC000-0000566"));
    }
    catch (BusinessException be)
    {
      return be.getMessage();
    }

    return null;
  }

  private void adjustColumns(Hashtable htFieldFlag)
  {
    if (this.m_bHasCscode) {
      setShowFlag("cscode", true);
      setShowFlag("csname", true);
    } else {
      setShowFlag("cscode", false);
      setShowFlag("csname", false);
    }

    if (this.m_bClassflag) {
      setShowFlag("invclasscode", true);
      setShowFlag("invclassname", true);
      setShowFlag("invcode", false);
      setShowFlag("invname", false);
      setShowFlag("invspec", false);
      setShowFlag("invtype", false);
      setShowFlag("mainmeasname", false);
      setShowFlag("castunitname", false);
      setShowFlag("hsl", false);
      setShowFlag("vbatchcode", false);
      setShowFlag("dvalidate", false);
      setShowFlag("vfree0", false);
      setShowFlag("assistnum", false);
    }
    else {
      setShowFlag("invclasscode", false);
      setShowFlag("invclassname", false);
      setShowFlag("invcode", true);
      setShowFlag("invname", true);
      setShowFlag("invspec", true);
      setShowFlag("invtype", true);
      setShowFlag("mainmeasname", true);

      if (this.m_bHasAssist) {
        setShowFlag("castunitname", true);
        setShowFlag("hsl", true);
        setShowFlag("assistnum", true);
      } else {
        setShowFlag("castunitname", true);
        setShowFlag("hsl", false);
        setShowFlag("assistnum", false);
      }

      if (this.m_bHasBatchcode) {
        setShowFlag("vbatchcode", true);
        setShowFlag("dvalidate", true);
      } else {
        setShowFlag("vbatchcode", false);
        setShowFlag("dvalidate", false);
      }

      if (this.m_bHasVfree0) {
        setShowFlag("vfree0", true);
      }
      else {
        setShowFlag("vfree0", false);
      }

      setShowFlag("custcode", this.m_bHasVendor);
      setShowFlag("custname", this.m_bHasVendor);
      setShowFlag("hsl", this.m_bHasHsl);
    }
  }

  private ConditionVO[] adjustCondition(ConditionVO[] voCons)
  {
    return getConditionDlg().getExpandVOs(voCons);
  }

  protected void getCEnvInfo()
  {
    try
    {
      ClientEnvironment ce = ClientEnvironment.getInstance();
      try {
        this.m_sUserID = ce.getUser().getPrimaryKey();
      }
      catch (Exception e)
      {
      }
      try {
        this.m_sCorpID = ce.getCorporation().getPrimaryKey();
      }
      catch (Exception e)
      {
      }
      try {
        if (ce.getDate() != null)
          this.m_sLogDate = ce.getDate();
      }
      catch (Exception e)
      {
      }
    }
    catch (Exception e)
    {
    }
  }

  private ICMultiCorpQryClient getConditionDlg()
  {
    if (this.ivjQueryConditionDlg == null) {
      this.ivjQueryConditionDlg = new ICMultiCorpQryClient(this, this.m_sUserID, this.m_sCorpID, "40083002");

      this.ivjQueryConditionDlg.setTempletID(this.m_sCorpID, getPNodeCode(), this.m_sUserID, null);

      this.ivjQueryConditionDlg.setFreeItem("vfree0", "inv.invcode");

      this.ivjQueryConditionDlg.setAutoClear("kp.pk_corp", new String[] { "kp.ccalbodyid", "wh1.storcode", "cargdoc.cscode" });

      this.ivjQueryConditionDlg.setAutoClear("inv.invcode", new String[] { "vfree0", "vbatchcode", "meas2.pk_measdoc" });

      this.ivjQueryConditionDlg.setAstUnit("meas2.pk_measdoc", new String[] { "kp.pk_corp", "inv.invcode" });

      this.ivjQueryConditionDlg.setRefInitWhereClause("cargdoc.cscode", "货位档案", " bd_cargdoc.endflag='Y' and bd_cargdoc.pk_stordoc=", "wh1.storcode");

      this.ivjQueryConditionDlg.initRefWhere("ccustomerid", " and (custflag ='0' or custflag ='2') and (bd_cumandoc.frozenflag='N' OR bd_cumandoc.frozenflag='n'  OR bd_cumandoc.frozenflag IS NULL) ");

      this.ivjQueryConditionDlg.initRefWhere("cproviderid", " and (custflag ='1' or custflag ='3') and (bd_cumandoc.frozenflag='N' OR bd_cumandoc.frozenflag='n'  OR bd_cumandoc.frozenflag IS NULL) ");

      this.ivjQueryConditionDlg.initRefWhere("inv.invcode", " and bd_invbasdoc.discountflag='N' and bd_invbasdoc.laborflag='N'");

      this.ivjQueryConditionDlg.setPowerRefsOfCorp("kp.pk_corp", new String[] { "wh1.storcode", "kp.ccalbodyid", "inv.invcode", "invcl.invclasscode", "kp.cwarehouseid", "kp.cinventoryid", "cstoreadminid" }, null);

      this.ivjQueryConditionDlg.addICheckCondition(this);
    }

    return this.ivjQueryConditionDlg;
  }

  public Hashtable getFieldFlag(ConditionVO[] voCons)
  {
    Hashtable htRes = new Hashtable();

    if ((voCons == null) || (voCons.length < 1)) {
      return htRes;
    }
    for (int i = 0; i < voCons.length; i++) {
      String sItemkey = voCons[i].getFieldCode();

      if (!htRes.containsKey(sItemkey)) {
        htRes.put(sItemkey, voCons[i].getValue());
      }
    }
    return htRes;
  }

  public InvOnHandHeaderVO getHeader(ConditionVO[] voCons)
  {
    InvOnHandHeaderVO voHead = new InvOnHandHeaderVO();

    voHead.setQuerydate(this.m_sLogDate);
    for (int i = 0; i < voCons.length; i++) {
      if (voCons[i].getFieldCode().equals("kp.ccalbodyid")) {
        RefResultVO ref = voCons[i].getRefResult();
        if (ref != null) {
          voHead.setCcalbodyname(ref.getRefName());
        }
      }
      if (voCons[i].getFieldCode().equals("kp.pk_corp")) {
        RefResultVO ref = voCons[i].getRefResult();
        if (ref != null) {
          voHead.setUnitname(ref.getRefName());
        }
      }
    }
    voHead.setQuerycondition(getConditionDlg().getChText());
    return voHead;
  }

  public SNDialog getSNDlg()
  {
    if (this.m_dlgSN == null) {
      this.m_dlgSN = new SNDialog(this);
    }
    return this.m_dlgSN;
  }

  public String getTitle()
  {
    if (getReportBaseClass().getReportTitle() != null) {
      return getReportBaseClass().getReportTitle();
    }
    return NCLangRes.getInstance().getStrByID("4008report", "UPP4008report-000002");
  }

  private void initialize()
  {
    setName("ClientUI");
    setLayout(new BorderLayout());
    setSize(774, 419);
    add(getReportBaseClass(), "Center");
    getCEnvInfo();

    setButtons(getButtonArray(this.m_MainButtonGroup));

    initReportTemplet(this.m_sRNodeName);
    getReportBaseClass().setRowNOShow(true);
    getReportBaseClass().setTatolRowShow(true);
    getReportBaseClass().setMaxLenOfHeadItem("unitname", 100);
    getReportBaseClass().setMaxLenOfHeadItem("querycondition", 500);

    ScaleInit si = new ScaleInit(this.m_sUserID, this.m_sCorpID);

    ArrayList alParam = new ArrayList();
    alParam.add(new String[] { "num", "freezenum", "ngrossnum", "nfreezegrossnum" });
    alParam.add(new String[] { "assistnum" });
    alParam.add(null);
    alParam.add(null);
    alParam.add(new String[] { "hsl" });
    try {
      si.setScale(getReportBaseClass(), alParam);
    } catch (Exception e) {
      showHintMessage(NCLangRes.getInstance().getStrByID("4008report", "UPP4008report-000003") + e.getMessage());
    }
  }

  private void initReportTemplet(String sNodeName)
  {
    try
    {
      getReportBaseClass().setTempletID(this.m_sCorpID, getPNodeCode(), this.m_sUserID, null);
    }
    catch (Exception e) {
      showErrorMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000019"));

      return;
    }
    BillData bd = getReportBaseClass().getBillData();
    bd = BatchCodeDefSetTool.changeBillDataByBCUserDef(this.m_sCorpID, bd);
    if (bd == null) {
      SCMEnv.out("--> billdata null.");
      return;
    }
    getReportBaseClass().setBillData(bd);

    String[] strBodyFields = getReportBaseClass().getBodyFields();
    if (strBodyFields != null)
      for (int i = 0; i < strBodyFields.length; i++)
        this.m_htShowFlag.put(strBodyFields[i], new Boolean(true));
  }

  public void onButtonClicked(ButtonObject bo)
  {
    showHintMessage("");
    if (bo == this.m_boQuery)
      onQuery(true);
    if (bo == this.m_boShowSN)
      onShowSN();
    else
      super.onButtonClicked(bo);
  }

  public void onQuery(boolean bQuery)
  {
    if ((bQuery) || (!this.m_bEverQry)) {
      getConditionDlg().hideNormal();
      getConditionDlg().showModal();
      this.m_bEverQry = true;
    } else {
      getConditionDlg().onButtonConfig();
    }

    if (!getConditionDlg().isCloseOK())
      return;
    String[] corps = getConditionDlg().getSelectedCorpIDs();
    if ((corps == null) || (corps.length < 0)) {
      showWarningMessage(NCLangRes.getInstance().getStrByID("4008report", "UPP4008report-000186"));

      onQuery(true);
      return;
    }

    setDlgSubTotal(null);
    try {
      QryConditionVO voQry = new QryConditionVO();
      voQry.setParam(0, corps);

//      ConditionVO[] voCons = getConditionDlg().getConditionVO();
    //edit by zwx 2019-11-14 增加过滤印铁的仓库：宝印剪切库（作废）、宝印成品库(作废)、宝印半成品库(封存)
      ConditionVO[] oldvoCons = getConditionDlg().getConditionVO();
      ConditionVO condition = new ConditionVO();
	  condition.setFieldCode("wh1.pk_stordoc");
	  condition.setOperaCode("not in ");
	  condition.setDataType(0);
	  condition.setValue("('1020A110000000001WQG','1020A110000000001WQI','1020A110000000001WQM')");
	  ConditionVO[] voCons = new ConditionVO[oldvoCons.length+1];
	  System.arraycopy(oldvoCons, 0, voCons, 0, oldvoCons.length);
	  voCons[oldvoCons.length]=condition;
	  //end by zwx 
      voQry.setParam(1, adjustCondition(voCons));

      if (!checkConds(voCons)) {
        return;
      }
      long lTime = System.currentTimeMillis();
      long lTimes = System.currentTimeMillis();

      InvOnHandVO vo = IC601InvOnHandHelper.queryXcl(voQry);
      if (vo == null) {
        getReportBaseClass().getBillModel().clearBodyData();
        return;
      }

      showTime(lTimes, "查询时间：");

      getField(voCons);

      adjustColumns(getFieldFlag(voCons));

      InvOnHandHeaderVO voHead = getHeader(voCons);
      getReportBaseClass().setHeadDataVO(voHead);
      vo.setParentVO(voHead);
      if ((vo.getChildrenVO() != null) && (vo.getChildrenVO().length > 0))
      {
        setReportData(vo.getChildrenVO(), voCons);
      }
      else {
        getReportBaseClass().getBillModel().clearBodyData();
      }

      this.m_voReport = vo;
      calculateTotal();
      showTime(lTime, "查询总时间" + vo.getChildrenVO().length + "条数");
    } catch (Exception e) {
      SCMEnv.error(e);
      showErrorMessage(NCLangRes.getInstance().getStrByID("4008report", "UPP4008report-000004") + e.getMessage());
    }
  }

  public void onShowSN()
  {
    int iCurRow = getReportBaseClass().getBillTable().getSelectedRow();
    String[] scodes = null;
    StringBuffer sInv = new StringBuffer();
    String sNum = "0";
    String[] keys = { "pk_corp", "cwarehouseid", "cinventoryid", "cspaceid", "castunitid", "hsl", "vbatchcode", "vfree1", "vfree2", "vfree3", "vfree4", "vfree5", "vfree6", "vfree7", "vfree8", "vfree9", "vfree10", "cvendorid" };

    if ((iCurRow >= 0) && (iCurRow < getReportBaseClass().getBillModel().getRowCount()))
    {
      InvOnHandItemVO voItem = new InvOnHandItemVO();
      BillModel bm = getReportBaseClass().getBillModel();

      for (int j = 0; j < keys.length; j++) {
        try {
          voItem.setAttributeValue(keys[j], bm.getValueAt(iCurRow, keys[j]));
        }
        catch (Exception e)
        {
        }
      }
      if (voItem != null) {
        try {
          scodes = IC601InvOnHandHelper.querySN(voItem);
        } catch (Exception e) {
          showErrorMessage(NCLangRes.getInstance().getStrByID("4008report", "UPP4008report-000005"));
        }

      }

      if (bm.getValueAt(iCurRow, "invcode") != null)
        sInv.append((String)bm.getValueAt(iCurRow, "invcode"));
      sInv.append(" ");
      if (bm.getValueAt(iCurRow, "invname") != null) {
        sInv.append((String)bm.getValueAt(iCurRow, "invname"));
      }
      if ((scodes != null) && (scodes.length > 0)) {
        sNum = new Integer(scodes.length).toString();
      }
    }

    if ((scodes == null) || (scodes.length < 1)) {
      showHintMessage(NCLangRes.getInstance().getStrByID("4008report", "UPP4008report-000006"));

      return;
    }

    Vector vdata = new Vector(scodes.length);

    for (int i = 0; i < scodes.length; i++) {
      Vector v = new Vector(2);
      v.addElement(Integer.toString(i + 1));
      v.addElement(scodes[i]);
      vdata.addElement(v);
    }
    this.m_dlgSN = null;
    getSNDlg().setData(sInv.toString(), sNum, vdata);
    getSNDlg().showModal();
  }

  public void setShowFlag(String strKey, boolean flag)
  {
    if (flag) {
      if ((this.m_htShowFlag.containsKey(strKey)) && 
        (!((Boolean)this.m_htShowFlag.get(strKey)).booleanValue()))
      {
        getReportBaseClass().showBodyTableCol(strKey);

        this.m_htShowFlag.put(strKey, new Boolean(true));
      }

    }
    else if ((this.m_htShowFlag.containsKey(strKey)) && 
      (((Boolean)this.m_htShowFlag.get(strKey)).booleanValue()))
    {
      getReportBaseClass().hideBodyTableCol(strKey);

      this.m_htShowFlag.put(strKey, new Boolean(false));
    }
  }

  public ClientUI(FramePanel ff)
  {
    setFrame(ff);
    initialize();
  }

  public String getDefaultPNodeCode()
  {
    return this.m_sPNodeCode;
  }

  public ReportBaseClass getReportBaseClass()
  {
    if (this.ivjReportBase == null) {
      try {
        this.ivjReportBase = new ReportBaseClass();
        this.ivjReportBase.setName("ReportBase");
      }
      catch (Throwable ivjExc)
      {
      }

    }

    return this.ivjReportBase;
  }

  protected void showTime(long lStartTime, String sTaskHint)
  {
    long lTime = System.currentTimeMillis() - lStartTime;
    SCMEnv.out("执行<" + sTaskHint + ">消耗的时间为：" + lTime / 60000L + "分" + lTime / 1000L % 60L + "秒" + lTime % 1000L + "毫秒");
  }

  public String getCorpID()
  {
    return this.m_sCorpID;
  }

  private Hashtable getField(ConditionVO[] voCons)
  {
    this.m_htField = new Hashtable();
    if ((voCons != null) && (voCons.length > 0))
    {
      for (int i = 0; i < voCons.length; i++) {
        String sItemkey = voCons[i].getFieldCode();

        if (!this.m_htField.containsKey(sItemkey))
          this.m_htField.put(sItemkey, voCons[i].clone());
      }
    }
    if ((this.m_htField.containsKey("classflag")) && (((ConditionVO)this.m_htField.get("classflag")).getValue().equalsIgnoreCase("Y")))
    {
      this.m_bClassflag = true;
    }
    else this.m_bClassflag = false;

    if ((this.m_htField.containsKey("meas2.assistnum")) || (this.m_htField.containsKey("bd_measdoc2.measname")) || ((this.m_htField.containsKey("measnameflag")) && (((ConditionVO)this.m_htField.get("measnameflag")).getValue().equalsIgnoreCase("Y"))))
    {
      this.m_bHasAssist = true;
    }
    else this.m_bHasAssist = false;

    if ((this.m_htField.containsKey("cargdoc.cscode")) || ((this.m_htField.containsKey("cscodeflag")) && (((ConditionVO)this.m_htField.get("cscodeflag")).getValue().equalsIgnoreCase("Y"))))
    {
      SCMEnv.out("m_bHasCscode = true;");

      this.m_bHasCscode = true;
    } else {
      this.m_bHasCscode = false;
      SCMEnv.out("m_bHasCscode = false;");
    }

    if ((this.m_htField.containsKey("vbatchcode")) || (this.m_htField.containsKey("dvalidate")) || ((this.m_htField.containsKey("vbatchcodeflag")) && (((ConditionVO)this.m_htField.get("vbatchcodeflag")).getValue().equalsIgnoreCase("Y"))))
    {
      this.m_bHasBatchcode = true;
    }
    else this.m_bHasBatchcode = false;

    if ((this.m_htField.containsKey("vfree1")) || (this.m_htField.containsKey("vfree2")) || (this.m_htField.containsKey("vfree3")) || (this.m_htField.containsKey("vfree4")) || (this.m_htField.containsKey("vfree5")) || (this.m_htField.containsKey("vfree6")) || (this.m_htField.containsKey("vfree7")) || (this.m_htField.containsKey("vfree8")) || (this.m_htField.containsKey("vfree9")) || (this.m_htField.containsKey("vfree10")) || ((this.m_htField.containsKey("vfree0flag")) && (((ConditionVO)this.m_htField.get("vfree0flag")).getValue().equalsIgnoreCase("Y"))))
    {
      this.m_bHasVfree0 = true;
    }
    else this.m_bHasVfree0 = false;

    if ((this.m_htField.containsKey("vendor.custcode")) || ((this.m_htField.containsKey("cvendorflag")) && (((ConditionVO)this.m_htField.get("cvendorflag")).getValue().equalsIgnoreCase("Y"))))
    {
      this.m_bHasVendor = true;
    }
    else this.m_bHasVendor = false;

    if ((this.m_htField.containsKey("hslflag")) && (((ConditionVO)this.m_htField.get("hslflag")).getValue().equalsIgnoreCase("Y")))
      this.m_bHasHsl = true;
    else {
      this.m_bHasHsl = false;
    }
    return this.m_htField;
  }

  public AggregatedValueObject getReportVO()
  {
    InvOnHandVO vo = (InvOnHandVO)this.m_voReport;

    if ((null == vo) || (vo.getChildrenVO() == null) || (vo.getChildrenVO().length == 0) || (vo.getChildrenVO()[0] == null))
    {
      return null;
    }
    if (null == vo.getParentVO()) {
      vo.setParentVO(new InvOnHandHeaderVO());
    }
    return vo;
  }

  public String getUserID()
  {
    return this.m_sUserID;
  }

  private void setReportData(CircularlyAccessibleValueObject[] reportData, ConditionVO[] voCons)
  {
    long lTime = System.currentTimeMillis();

    ClientCacheHelper.getColValue(reportData, new String[] { "unitcode", "unitname" }, "bd_corp", "pk_corp", new String[] { "unitcode", "unitname" }, "pk_corp");

    ClientCacheHelper.getColValue(reportData, new String[] { "bodycode", "bodyname" }, "bd_calbody", "pk_calbody", new String[] { "bodycode", "bodyname" }, "ccalbodyid");

    ClientCacheHelper.getColValue(reportData, new String[] { "storcode", "storname" }, "bd_stordoc", "pk_stordoc", new String[] { "storcode", "storname" }, "cwarehouseid");

    if (this.m_bHasCscode) {
      ClientCacheHelper.getColValue(reportData, new String[] { "cscode", "csname" }, "bd_cargdoc", "pk_cargdoc", new String[] { "cscode", "csname" }, "cspaceid");
    }

    ClientCacheHelper.getColValue(reportData, new String[] { "pk_invbasdoc" }, "bd_invmandoc", "pk_invmandoc", new String[] { "pk_invbasdoc" }, "cinventoryid");

    ClientCacheHelper.getColValue(reportData, new String[] { "invname", "invcode", "invspec", "invtype", "mainmeasname" }, "bd_invbasdoc", "pk_invbasdoc", new String[] { "invname", "invcode", "invspec", "invtype", "pk_measdoc" }, "pk_invbasdoc");

    ClientCacheHelper.getColValue(reportData, new String[] { "mainmeasname" }, "bd_measdoc", "pk_measdoc", new String[] { "measname" }, "mainmeasname");

    if ((this.m_bHasAssist) || (this.m_bHasHsl)) {
      ClientCacheHelper.getColValue(reportData, new String[] { "castunitname" }, "bd_measdoc", "pk_measdoc", new String[] { "measname" }, "castunitid");
    }

    ClientCacheHelper.getColValue(reportData, new String[] { "pk_cubasdoc" }, "bd_cumandoc", "pk_cumandoc", new String[] { "pk_cubasdoc" }, "cvendorid");

    ClientCacheHelper.getColValue(reportData, new String[] { "custcode", "custname" }, "bd_cubasdoc", "pk_cubasdoc", new String[] { "custcode", "custname" }, "pk_cubasdoc");

    ClientCacheHelper.getColValue(reportData, new String[] { "pk_invbasdoc" }, "bd_invmandoc", "pk_invmandoc", new String[] { "pk_invbasdoc" }, "cinventoryid");

    BatchCodeDefSetTool.execFormulaBatchCodeForReport(reportData);

    SCMEnv.showTime(lTime, "普通基本档案公式解析New:");

    if (this.m_bHasVfree0) {
      lTime = System.currentTimeMillis();
      FreeVOParse freeVOParse = new FreeVOParse();
      freeVOParse.setFreeVO(reportData, "pk_invbasdoc", null, true);
      SCMEnv.showTime(lTime, "自由项解析");
    }
    lTime = System.currentTimeMillis();
    getReportBaseClass().setBodyDataVO(reportData, true);

    SCMEnv.showTime(lTime, "前台数据加载界面");
  }

  private boolean checkConds(ConditionVO[] cons)
  {
    String sField = null;
    boolean bcstoreadminid = false;
    boolean binvclass = false;
    for (int i = 0; i < cons.length; i++) {
      sField = cons[i].getFieldCode();
      if (sField.equals("cstoreadminid"))
        bcstoreadminid = true;
      else if ((sField.equals("classflag")) && (cons[i].getValue().equalsIgnoreCase("Y"))) {
        binvclass = true;
      }
    }
    if ((bcstoreadminid) && (binvclass)) {
      showErrorMessage(ResBase.get601AdminAndClass());
      return false;
    }
    return true;
  }
}