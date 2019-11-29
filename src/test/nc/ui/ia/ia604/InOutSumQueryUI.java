/*
 * 2016-11-11反编译的
 */

package nc.ui.ia.ia604;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ItemEvent;
import java.util.Vector;
import nc.ui.bd.b39.PhaseRefModel;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.bd.ref.UFRefManage;
import nc.ui.ia.analyze.IaAnalyseBO_Client;
import nc.ui.ia.pub.CommonDataBO_Client;
import nc.ui.ia.pub.IAEnvironment;
import nc.ui.ia.pub.IAMultiCorpQueryDialog;
import nc.ui.ia.pub.IAQueryTemplateDefSetTool;
import nc.ui.ia.pub.JobPhaseRef;
import nc.ui.ia.pub.JobRef;
import nc.ui.ia.pub.sqlparse.PerviewManager;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.ValueChangedEvent;
import nc.ui.pub.beans.layout.TableLayout;
import nc.ui.scm.pub.def.DefSetTool;
import nc.vo.ia.analyze.QueryVO;
import nc.vo.ia.pub.Log;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.query.ConditionVO;
import nc.vo.sm.UserVO;

public class InOutSumQueryUI extends IAMultiCorpQueryDialog
{
  private static final long serialVersionUID = 1L;
  private UIPanel ivjUIPanelCustom = null;
  private JobPhaseRef ivjUIRefPaneJobParse = null;
  private JobRef ivjUIRefPaneJob = null;

  private UICheckBox ivjUICheckBox1 = null;
  private UICheckBox ivjUICheckBox2 = null;
  private UILabel ivjUILabel5 = null;

  private UICheckBox ivjUICheckBox3 = null;
  private UICheckBox ivjUICheckBox4 = null;
  private UIComboBox ivjUIComboBox2 = null;
  private UILabel ivjUILabel1 = null;
  private UILabel ivjUILabel3 = null;
  private UILabel ivjUILabel6 = null;

  private UIRefPane ivjUIRefPanech1 = null;
  private UIRefPane ivjUIRefPanech2 = null;
  private UIRefPane ivjUIRefPanefl1 = null;
  private UIRefPane ivjUIRefPanefl2 = null;
  private UIRefPane ivjUIRefPanerr = null;
  private UIRefPane ivjUIRefDept = null;

  protected IAEnvironment ce = new IAEnvironment();
  private UILabel ivjUILabel61 = null;
  private UIRefPane ivjUIRefPaneck = null;
  private UIRefPane ivjUIRefPanekc1 = null;
  private UIRefPane ivjUIRefPanekc2 = null;
  private UIRefPane ivjUIRefPaneUser = null;

  private String sSqlShow = "";

  private UICheckBox ivjUICheckBoxRdcl = null;
  private UIComboBox ivjUIComboBoxJJFS = null;
  private UIRefPane ivjUIRefDate1 = null;
  private UIRefPane ivjUIRefDate2 = null;
  private UFDate m_dtLast = null;
  private UFDate m_dtStart = null;
  private int[] m_iaClassLevel;
  private String m_sCorpID = null;
  private QueryVO m_voCondition = null;

  public InOutSumQueryUI(Container parent, String title, String operatorID, String pk_corp)
  {
    super(parent, title, operatorID, pk_corp, "20149040");

    initialize();
  }

  public void handleException(Throwable exception)
  {
    exception.printStackTrace(System.out);
  }

  private void initialize()
  {
    try
    {
      setSealedDataShow(true);
      this.m_sCorpID = this.ce.getCorporationID();
      getUIPanelNormal().setLayout(new BorderLayout());
      getUIPanelNormal().add(getUIPanelCustom(), "Center");

      setTempletID(this.m_sCorpID, "20149040", this.ce.getUser().getPrimaryKey(), null);
      setName("InOutSumQueryUI");

      this.m_iaClassLevel = CommonDataBO_Client.getCodeSchemdule(this.m_sCorpID);
      getUIComboBox2().addItem(NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000019"));

      if (this.m_iaClassLevel != null) {
        for (int i = 1; i < this.m_iaClassLevel.length; i++) {
          getUIComboBox2().addItem(new Integer(i));
        }
      }
      getUIComboBox2().setSelectedItem(NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000019"));

      String sStartPeriod = CommonDataBO_Client.getStartPeriod(this.m_sCorpID);

      this.m_dtStart = CommonDataBO_Client.getMonthBeginDate(this.m_sCorpID, sStartPeriod);

      getUIRefDate1().setText(this.m_dtStart.toString());
      getUIRefDate2().setText(this.ce.getBusinessDate().toString());

      String sPeriod = this.ce.getAccountPeriod();
      this.m_dtLast = CommonDataBO_Client.getMonthEndDate(this.m_sCorpID, sPeriod);

      this.m_voCondition = new QueryVO();
      this.m_voCondition.setPk_Corps(new String[] { this.m_sCorpID });
    }
    catch (Throwable ivjExc)
    {
      handleException(ivjExc);
    }

    getUICheckBoxMeas2().addItemListener(this);
    getUICheckBoxAdjustMny().addItemListener(this);
    getUICheckBoxSetPart().addItemListener(this);
    getUICheckBoxNotAudit().addItemListener(this);
    getUICheckBoxRdcl().addItemListener(this);

    setValueRef("bd_stordoc.storcode", getUIRefPaneck());
    setValueRef("bd_psndoc.psncode", getUIRefPanerr());
    setValueRef("bd_deptdoc.deptcode", getUIRefDept());
    getUIRefPaneUser();
    setValueRef("v.fpricemodeflag", getUIComboBoxJJFS());
    setValueRef("v.cprojectphase", getUIRefPaneJobParse());
    setValueRef("v.cprojectid", getUIRefPaneJob());
    init();
    IAQueryTemplateDefSetTool.updateQueryCondition(this, "inv.def", "存货档案", this.ce.getCorporationID());

    hideUnitButton();
  }

  public static void main(String[] args)
  {
  }

  public void beforeDialogClosed()
  {
    ConditionVO[] conditions = getConditionVO();

    for (int i = 0; i < conditions.length; i++) {
      if ((conditions[i].getValue() == null) || (conditions[i].getValue().toString().trim().length() <= 0)) {
        continue;
      }
      if ((conditions[i].getFieldCode().equals("v.fpricemodeflag")) && (getUIComboBoxJJFS().getSelectedIndex() > 0))
      {
        conditions[i].setValue("" + getUIComboBoxJJFS().getSelectedIndex());
      }
      else if (conditions[i].getOperaCode().equals("like")) {
        conditions[i].setOperaCode(" like ");
        conditions[i].setValue("%" + conditions[i].getValue() + "%");
      }

    }

    String defineSql = getDefineWhereSQL();
    String Sql = this.m_voCondition.getWhere();
    if ((defineSql != null) && (defineSql.length() > 0)) {
      if (Sql.length() > 0) {
        Sql = Sql + "and " + defineSql;
      }
      else {
        Sql = defineSql;
      }
    }
    this.m_voCondition.setWhere(Sql);

    String[] ControlObject = { "仓库", "库存组织", "部门", "业务员", "存货分类", "存货" };
    String[] TableNames = { "仓库档案", "库存组织", "部门档案", "人员档案", "存货分类", "存货档案" };

    String[] QueryCode = { "storcode", "bodycode", "bd_deptdoc.deptcode", "bd_psndoc.psncode", "inv.invclasscode", "inv.invcode" };
    try
    {
      PerviewManager pm = new PerviewManager(ControlObject, TableNames, QueryCode);
      String DataPowerSql = pm.getWhereSqlWithDataPower(Sql, this.ce.getUser().getPrimaryKey(), this.m_voCondition.getPk_Corps());
      if (DataPowerSql.trim().length() > 0)
        this.m_voCondition.setDataPowerSql(DataPowerSql);
    }
    catch (BusinessException e) {
      Log.error(e);
    } catch (Exception e) {
      Log.error(e);
    }
  }

  public String checkCondition(ConditionVO[] conditions)
  {
    int left = 0;
    int right = 0;
    for (int i = 0; i < conditions.length; i++) {
      if ((conditions[i].getValue() == null) || (conditions[i].getValue().toString().trim().length() <= 0))
        continue;
      if (!conditions[i].getNoLeft()) {
        left++;
      }
      if (!conditions[i].getNoRight()) {
        right++;
      }
      if (left < right) {
        return NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000020");
      }

      if ((!conditions[i].getFieldCode().equals("v.fpricemodeflag")) || (getUIComboBoxJJFS().getSelectedIndex() <= 0))
        continue;
      conditions[i].setValue("" + getUIComboBoxJJFS().getSelectedIndex());
    }

    if (left != right) {
      return NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000020");
    }

    this.sSqlShow = "";

    String strSql = "";

    String[] sCorpIDs = getSelectedCorpIDs();
    String[] sCorpName = getSelectedCorpNames();

    if ((sCorpIDs == null) || (sCorpIDs.length == 0)) {
      return NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000021");
    }

    StringBuffer sDispName = new StringBuffer();
    for (int i = 0; i < sCorpName.length; i++) {
      sDispName.append(sCorpName[i]);
      if (i != sCorpName.length - 1) {
        sDispName.append(",");
      }
    }

    String[] sValue = { sDispName.toString() };

    this.sSqlShow += NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000022", null, sValue);

    if (sCorpIDs.length == 1) {
      this.m_sCorpID = sCorpIDs[0];
    }

    this.m_voCondition.setPk_Corps(sCorpIDs);
    this.m_voCondition.setCorpNames(sCorpName);

    String[] saDate = new String[2];
    saDate[0] = getUIRefDate1().getRefCode();
    saDate[1] = getUIRefDate2().getRefCode();
    if ((saDate[0] == null) || (saDate[0].length() <= 0)) {
      return NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000023");
    }

    if ((saDate[1] == null) || (saDate[1].length() <= 0)) {
      return NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000024");
    }

    String sMsg = checkDate(saDate, sCorpIDs);
    if (sMsg.length() > 0) {
      return sMsg;
    }

    String sValue1[] = { saDate[0], saDate[1] };

    this.sSqlShow += NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000025", null, sValue1);

    this.m_voCondition.setDate(saDate);

    String[] saInvcl = new String[2];
    saInvcl[0] = getUIRefPanefl1().getRefCode();
    saInvcl[1] = getUIRefPanefl2().getRefCode();
    if ((saInvcl[0] != null) && (saInvcl[1] != null)) {
      sValue1 = new String[] { saInvcl[0] + " " + getUIRefPanefl1().getRefName(), saInvcl[1] + " " + getUIRefPanefl2().getRefName() };

      this.sSqlShow += NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000027", null, sValue1);
    }
    else if (saInvcl[0] != null) {
      sValue1 = new String[] { saInvcl[0] + " " + getUIRefPanefl1().getRefName() };

      this.sSqlShow += NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000030", null, sValue1);
    }
    else if (saInvcl[1] != null) {
      sValue1 = new String[] { saInvcl[1] + " " + getUIRefPanefl2().getRefName() };

      this.sSqlShow += NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000031", null, sValue1);
    }

    if ((saInvcl[0] != null) && (saInvcl[1] != null) && (saInvcl[0].equalsIgnoreCase(saInvcl[1])))
    {
      if (strSql.length() > 0) {
        strSql = strSql + " and ";
      }
      strSql = strSql + " (inv.invclasscode like '" + saInvcl[0] + "%') ";
    }
    else
    {
      if (saInvcl[0] != null) {
        if (strSql.length() > 0)
          strSql = strSql + " and ";
        strSql = strSql + " (inv.invclasscode >='" + saInvcl[0] + "') ";
      }
      if (saInvcl[1] != null) {
        if (strSql.length() > 0)
          strSql = strSql + " and ";
        strSql = strSql + " (inv.invclasscode <='" + saInvcl[1] + "') ";
      }
    }

    String[] saInv = new String[2];
    saInv[0] = getUIRefPanech1().getRefCode();
    saInv[1] = getUIRefPanech2().getRefCode();

    if ((saInv[0] != null) && (saInv[1] != null)) {
      sValue1 = new String[] { saInv[0] + " " + getUIRefPanech1().getRefName(), saInv[1] + " " + getUIRefPanech2().getRefName() };

      this.sSqlShow += NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000033", null, sValue1);
    }
    else if (saInv[0] != null) {
      sValue1 = new String[] { saInv[0] + " " + getUIRefPanech1().getRefName() };

      this.sSqlShow += NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000034", null, sValue1);
    }
    else if (saInv[1] != null) {
      sValue1 = new String[] { saInv[1] + " " + getUIRefPanech2().getRefName() };

      this.sSqlShow += NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000036", null, sValue1);
    }

    if (saInv[0] != null) {
      if (strSql.length() > 0)
        strSql = strSql + " and ";
      strSql = strSql + " (inv.invcode >='" + saInv[0] + "') ";
    }
    if (saInv[1] != null) {
      if (strSql.length() > 0)
        strSql = strSql + " and ";
      strSql = strSql + " (inv.invcode <='" + saInv[1] + "') ";
    }

    String[] saCalbody = new String[2];
    saCalbody[0] = getUIRefPanekc1().getRefCode();
    saCalbody[1] = getUIRefPanekc2().getRefCode();

    if ((saCalbody[0] != null) && (saCalbody[1] != null)) {
      sValue1 = new String[] { saCalbody[0] + " " + getUIRefPanekc1().getRefName(), saCalbody[1] + " " + getUIRefPanekc2().getRefName() };

      this.sSqlShow += NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000038", null, sValue1);
    }
    else if (saCalbody[0] != null) {
      sValue1 = new String[] { saCalbody[0] + " " + getUIRefPanekc1().getRefName() };

      this.sSqlShow += NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000039", null, sValue1);
    }
    else if (saCalbody[1] != null) {
      sValue1 = new String[] { saCalbody[1] + " " + getUIRefPanekc2().getRefName() };

      this.sSqlShow += NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000040", null, sValue1);
    }

    if (saCalbody[0] != null) {
      if (strSql.length() > 0)
        strSql = strSql + " and ";
      strSql = strSql + " (bd_calbody.bodycode>='" + saCalbody[0] + "') ";
    }
    if (saCalbody[1] != null) {
      if (strSql.length() > 0)
        strSql = strSql + " and ";
      strSql = strSql + " (bd_calbody.bodycode<='" + saCalbody[1] + "') ";
    }

    this.m_voCondition.setWhere(strSql);

    String sInvclLevel = getUIComboBox2().getSelectedItem().toString();
    if (getUIComboBox2().getSelectedIndex() == 0) {
      this.m_voCondition.setInvclLevel(0);
    }
    else {
      this.m_voCondition.setInvclLevel(Integer.parseInt(sInvclLevel.trim()));
    }

    if (this.m_voCondition.isSplitRdcl()) {
      if (this.m_voCondition.isShowAdjustMoney()) {
        return NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000041");
      }

      if (this.m_voCondition.isShowAssistant()) {
        return NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000042");
      }
    }

    if ((this.m_voCondition.isShowSetPart()) && (this.m_voCondition.isShowAssistant())) {
      return NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000043");
    }

    if (getUICheckBoxNotAudit().isSelected()) {
      this.m_voCondition.QueryType = QueryVO.ALL;
    }
    else {
      this.m_voCondition.QueryType = QueryVO.AUDIT;
    }

    this.m_voCondition.SourceTable = QueryVO.INOUTSUM;

    if (getUIComboBoxJJFS().getSelectedIndex() > 0) {
      sValue1 = new String[] { getUIComboBoxJJFS().getSelectedItem().toString() };

      this.sSqlShow += NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000044", null, sValue1);
    }

    try
    {
      String checkresult = IaAnalyseBO_Client.checkCondition(this.m_voCondition);

      if ((checkresult != null) && (checkresult.trim().length() > 0))
        return checkresult;
    }
    catch (Exception e) {
      Log.error(e);
    }

    return null;
  }

  private UIComboBox getUIComboBox2()
  {
    if (this.ivjUIComboBox2 == null) {
      try {
        this.ivjUIComboBox2 = new UIComboBox();
        this.ivjUIComboBox2.setName("UIComboBox2");
        this.ivjUIComboBox2.setLocation(343, 207);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUIComboBox2;
  }

  private UILabel getUILabel1()
  {
    if (this.ivjUILabel1 == null) {
      try {
        this.ivjUILabel1 = new UILabel();
        this.ivjUILabel1.setName("UILabel1");
        this.ivjUILabel1.setText(NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000045"));

        this.ivjUILabel1.setBounds(15, 143, 68, 22);

        this.ivjUILabel1.setILabelType(6);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUILabel1;
  }

  private UILabel getUILabel3()
  {
    if (this.ivjUILabel3 == null) {
      try {
        this.ivjUILabel3 = new UILabel();
        this.ivjUILabel3.setName("UILabel3");
        this.ivjUILabel3.setText(NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000046"));

        this.ivjUILabel3.setLocation(14, 15);

        this.ivjUILabel3.setSize(80, 22);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUILabel3;
  }

  private UILabel getUILabel5()
  {
    if (this.ivjUILabel5 == null) {
      try {
        this.ivjUILabel5 = new UILabel();
        this.ivjUILabel5.setName("UILabel5");
        this.ivjUILabel5.setText(NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000047"));

        this.ivjUILabel5.setBounds(213, 207, 80, 22);

        this.ivjUILabel5.setILabelType(6);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUILabel5;
  }

  private UILabel getUILabel6()
  {
    if (this.ivjUILabel6 == null) {
      try {
        this.ivjUILabel6 = new UILabel();
        this.ivjUILabel6.setName("UILabel6");
        this.ivjUILabel6.setText(NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000048"));

        this.ivjUILabel6.setBounds(14, 79, 62, 22);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUILabel6;
  }

  private UIPanel getUIPanelCustom()
  {
    if (this.ivjUIPanelCustom == null) {
      try {
        this.ivjUIPanelCustom = new UIPanel();
        this.ivjUIPanelCustom.setName("UIPanelCustom");
        UIPanel panel = getMainPanel();
        double[][] size = { { 10.0D, -1.0D, 10.0D }, { 10.0D, -2.0D, 10.0D } };

        TableLayout layout = new TableLayout(size);
        this.ivjUIPanelCustom.setLayout(layout);
        this.ivjUIPanelCustom.add(panel, "1,1");
      }
      catch (Throwable ivjExc) {
        handleException(ivjExc);
      }
    }
    return this.ivjUIPanelCustom;
  }

  private UIPanel getMainPanel()
  {
    double[][] size = { { -2.0D, 10.0D, -1.0D, 10.0D, -2.0D, 10.0D, -1.0D }, { -2.0D, 10.0D, -2.0D, 10.0D, -2.0D, 10.0D, -2.0D, 10.0D, -2.0D, 10.0D, -2.0D, 10.0D, -2.0D } };

    TableLayout layout = new TableLayout(size);
    UIPanel panel = new UIPanel();

    panel.setLayout(layout);

    panel.add(getUILabel61(), "0,0");
    panel.add(getUIRefPanekc1(), "2,0");
    panel.add(createLabel(), "4,0");
    panel.add(getUIRefPanekc2(), "6,0");

    panel.add(getUILabel6(), "0,2");
    panel.add(getUIRefPanefl1(), "2,2");
    panel.add(createLabel(), "4,2");
    panel.add(getUIRefPanefl2(), "6,2");

    panel.add(getUILabel3(), "0,4");
    panel.add(getUIRefPanech1(), "2,4");
    panel.add(createLabel(), "4,4");
    panel.add(getUIRefPanech2(), "6,4");

    panel.add(getUILabel1(), "0,6");
    panel.add(getUIRefDate1(), "2,6");
    panel.add(createLabel(), "4,6");
    panel.add(getUIRefDate2(), "6,6");

    panel.add(getUILabel5(), "0,8");
    panel.add(getUIComboBox2(), "2,8");
    panel.add(getUICheckBoxAdjustMny(), "4,8,6,8");

    panel.add(getUICheckBoxSetPart(), "0,10,2,10");
    panel.add(getUICheckBoxMeas2(), "4,10,6,10");

    panel.add(getUICheckBoxRdcl(), "0,12,2,12");
    panel.add(getUICheckBoxNotAudit(), "4,12,6,12");

    return panel;
  }

  private UIRefPane getUIRefPanech1()
  {
    if (this.ivjUIRefPanech1 == null) {
      try {
        this.ivjUIRefPanech1 = new UIRefPane();
        this.ivjUIRefPanech1.setName("UIRefPanech1");
        this.ivjUIRefPanech1.setBounds(97, 15, 99, 22);
        this.ivjUIRefPanech1.setRefNodeName("存货档案");

        this.ivjUIRefPanech1.getRefModel().setSealedDataShow(true);
        this.ivjUIRefPanech1.setReturnCode(false);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUIRefPanech1;
  }

  private UIRefPane getUIRefPanech2()
  {
    if (this.ivjUIRefPanech2 == null) {
      try {
        this.ivjUIRefPanech2 = new UIRefPane();
        this.ivjUIRefPanech2.setName("UIRefPanech2");
        this.ivjUIRefPanech2.setBounds(97, 47, 99, 22);
        this.ivjUIRefPanech2.setRefNodeName("存货档案");

        this.ivjUIRefPanech2.getRefModel().setSealedDataShow(true);
        this.ivjUIRefPanech2.setReturnCode(false);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUIRefPanech2;
  }

  private UIRefPane getUIRefPanefl1()
  {
    if (this.ivjUIRefPanefl1 == null) {
      try {
        this.ivjUIRefPanefl1 = new UIRefPane();
        this.ivjUIRefPanefl1.setName("UIRefPanefl1");
        this.ivjUIRefPanefl1.setBounds(97, 79, 99, 22);
        this.ivjUIRefPanefl1.setRefNodeName("存货分类");

        this.ivjUIRefPanefl1.setReturnCode(false);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUIRefPanefl1;
  }

  private UIRefPane getUIRefPanefl2()
  {
    if (this.ivjUIRefPanefl2 == null) {
      try {
        this.ivjUIRefPanefl2 = new UIRefPane();
        this.ivjUIRefPanefl2.setName("UIRefPanefl2");
        this.ivjUIRefPanefl2.setBounds(97, 111, 99, 22);
        this.ivjUIRefPanefl2.setRefNodeName("存货分类");

        this.ivjUIRefPanefl2.setReturnCode(false);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUIRefPanefl2;
  }

  public void itemStateChanged(ItemEvent e)
  {
    if (e.getSource() == getUICheckBoxAdjustMny()) {
      if (getUICheckBoxAdjustMny().isSelected()) {
        this.m_voCondition.setShowAdjustMoney(true);
      }
      else {
        this.m_voCondition.setShowAdjustMoney(false);
      }
    }
    else if (e.getSource() == getUICheckBoxSetPart()) {
      if (getUICheckBoxSetPart().isSelected()) {
        this.m_voCondition.setShowSetPart(true);
      }
      else {
        this.m_voCondition.setShowSetPart(false);
      }
    }
    else if (e.getSource() == getUICheckBoxMeas2())
    {
      if (getUICheckBoxMeas2().isSelected()) {
        this.m_voCondition.setShowAssistant(true);
      }
      else {
        this.m_voCondition.setShowAssistant(false);
      }
    }
    else if (e.getSource() == getUICheckBoxNotAudit())
    {
      if (getUICheckBoxNotAudit().isSelected()) {
        this.m_voCondition.QueryType = QueryVO.ALL;
        this.m_voCondition.setAll(true);
      }
      else
      {
        this.m_voCondition.QueryType = QueryVO.AUDIT;
        this.m_voCondition.setAll(false);
      }
    }
    else if (e.getSource() == getUICheckBoxRdcl())
    {
      if (getUICheckBoxRdcl().isSelected()) {
        this.m_voCondition.setSplitRdcl(true);
      }
      else
        this.m_voCondition.setSplitRdcl(false);
    }
  }

  public Vector getRdclLevel(Vector oTemp)
  {
    if (oTemp == null) {
      return null;
    }
    Vector temp = (Vector)oTemp.clone();
    Vector result = new Vector();
    Vector rsLevel = new Vector();

    Vector levelStr = new Vector();
    Vector levelStr2 = new Vector();
    levelStr.addElement("");
    while (true) {
      levelStr2.removeAllElements();
      rsLevel.removeAllElements();
      for (int i = 0; i < temp.size(); i++) {
        Object[] te = (Object[])(Object[])temp.elementAt(i);
        for (int j = 0; j < levelStr.size(); j++) {
          if (!te[5].toString().trim().equals(((String)levelStr.elementAt(j)).toString().trim()))
            continue;
          rsLevel.addElement(temp.elementAt(i));
          levelStr2.addElement(te[0]);
          break;
        }
      }

      levelStr = (Vector)levelStr2.clone();
      if (rsLevel.size() <= 0) break;
      result.addElement((Vector)rsLevel.clone());
    }

    return result;
  }

  private UILabel getUILabel61()
  {
    if (this.ivjUILabel61 == null) {
      try {
        this.ivjUILabel61 = new UILabel();
        this.ivjUILabel61.setName("UILabel61");
        this.ivjUILabel61.setText(NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000049"));

        this.ivjUILabel61.setBounds(220, 15, 80, 22);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUILabel61;
  }

  private UIRefPane getUIRefPaneck()
  {
    if (this.ivjUIRefPaneck == null) {
      try {
        this.ivjUIRefPaneck = new UIRefPane();
        this.ivjUIRefPaneck.setName("UIRefPaneck");

        this.ivjUIRefPaneck.setRefNodeName("仓库档案");

        this.ivjUIRefPaneck.getRefModel().setSealedDataShow(true);
        String sWhere = this.ivjUIRefPaneck.getRefModel().getWherePart();
        if ((sWhere != null) && (sWhere.length() > 0)) {
          sWhere = "";
        }
        else {
          sWhere = sWhere + " and ";
        }

        sWhere = sWhere + " bd_stordoc.gubflag<>'Y' and sealflag<>'Y' and iscalculatedinvcost = 'Y' ";

        this.ivjUIRefPaneck.getRefModel().setWherePart(sWhere);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUIRefPaneck;
  }

  private UIRefPane getUIRefPanekc1()
  {
    if (this.ivjUIRefPanekc1 == null) {
      try {
        this.ivjUIRefPanekc1 = new UIRefPane();
        this.ivjUIRefPanekc1.setName("UIRefPanekc1");
        this.ivjUIRefPanekc1.setBounds(343, 15, 99, 22);
        this.ivjUIRefPanekc1.setRefNodeName("库存组织");

        this.ivjUIRefPanekc1.getRefModel().setSealedDataShow(true);
        this.ivjUIRefPanekc1.setReturnCode(false);

        this.ivjUIRefPanekc1.getRefModel().addWherePart(" and property in (0,2)");
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUIRefPanekc1;
  }

  private UIRefPane getUIRefPanekc2()
  {
    if (this.ivjUIRefPanekc2 == null) {
      try {
        this.ivjUIRefPanekc2 = new UIRefPane();
        this.ivjUIRefPanekc2.setName("UIRefPanekc2");
        this.ivjUIRefPanekc2.setBounds(343, 47, 99, 22);
        this.ivjUIRefPanekc2.setRefNodeName("库存组织");

        this.ivjUIRefPanekc2.getRefModel().setSealedDataShow(true);
        this.ivjUIRefPanekc2.setReturnCode(false);

        this.ivjUIRefPanekc2.getRefModel().addWherePart(" and property in (0,2)");
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUIRefPanekc2;
  }

  private UIRefPane getUIRefPaneUser()
  {
    if (this.ivjUIRefPaneUser == null) {
      try {
        this.ivjUIRefPaneUser = new UIRefPane();
        this.ivjUIRefPaneUser.setName("UIRefPaneUser");

        this.ivjUIRefPaneUser.setRefNodeName("操作员");

        this.ivjUIRefPaneUser.getRefModel().setSealedDataShow(true);
        this.ivjUIRefPaneUser.setReturnCode(false);

        AbstractRefModel refmodel = this.ivjUIRefPaneUser.getRefModel();

        String sTableName = refmodel.getTableName();
        sTableName = sTableName + ",sm_userandcorp";
        refmodel.setTableName(sTableName);

        String sWhere = refmodel.getWherePart();

        if (sWhere != null) {
          sWhere = sWhere + " and ";
        }
        else {
          sWhere = "";
        }
        sWhere = sWhere + " sm_userandcorp.userid = sm_user.cuserid ";
        sWhere = sWhere + " and sm_userandcorp.pk_corp = '" + this.ce.getCorporationID() + "'";

        refmodel.setWherePart(sWhere);

        setValueRef("i.user_code", getUIRefPaneUser());
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUIRefPaneUser;
  }

  private void init()
  {
    DefSetTool.updateQueryConditionClientUserDef(this, this.ce.getCorporationID(), "iabill", "v.vdef", "", 0, "v.bdef", "", 0);
  }

  public void setRefWherePart(String sPk_corp)
  {
    if (sPk_corp == null) {
      return;
    }

    String sWherePart = " pk_corp = '" + sPk_corp + "'";
    getUIRefPaneck().getRef().getRefModel().setWherePart(" bd_stordoc.pk_corp ='" + sPk_corp + "'");

    getUIRefPaneck().setPk_corp(sPk_corp);
    getUIRefPaneck().getRef().getRefModel().setPk_corp(sPk_corp);

    getUIRefPanerr().setPk_corp(sPk_corp);
    getUIRefPanerr().getRef().getRefModel().setPk_corp(sPk_corp);

    getUIRefDept().setPk_corp(sPk_corp);
    getUIRefDept().getRef().getRefModel().setPk_corp(sPk_corp);

    getUIRefPanekc1().getRef().getRefModel().setWherePart(sWherePart + " and property in (" + 0 + "," + 2 + ")");

    getUIRefPanekc2().getRef().getRefModel().setWherePart(sWherePart + " and property in (" + 0 + "," + 2 + ")");

    getUIRefPanekc1().setPk_corp(sPk_corp);
    getUIRefPanekc1().getRef().getRefModel().setPk_corp(sPk_corp);

    getUIRefPanekc2().setPk_corp(sPk_corp);
    getUIRefPanekc2().getRef().getRefModel().setPk_corp(sPk_corp);

    getUIRefPanech1().setPk_corp(sPk_corp);
    getUIRefPanech1().getRef().getRefModel().setPk_corp(sPk_corp);

    getUIRefPanech2().setPk_corp(sPk_corp);
    getUIRefPanech2().getRef().getRefModel().setPk_corp(sPk_corp);
  }

  public void setInvRefWherePart(String[] sPk_corps)
  {
    if ((sPk_corps == null) || (sPk_corps.length == 0)) {
      return;
    }

    String sCorpWhereSQL = getCorpWhereStr("bd_invmandoc.pk_corp");

    if (sCorpWhereSQL.indexOf("null") >= 0) {
      sCorpWhereSQL = sCorpWhereSQL.substring(sCorpWhereSQL.indexOf("null") + 4);
    }

    getUIRefPanech1().getRef().getRefModel().setWherePart(" pk_invbasdoc in (select pk_invbasdoc from bd_invmandoc where " + sCorpWhereSQL + ")");

    getUIRefPanech2().getRef().getRefModel().setWherePart(" pk_invbasdoc in (select pk_invbasdoc from bd_invmandoc where " + sCorpWhereSQL + ")");
  }

  private UICheckBox getUICheckBoxAdjustMny()
  {
    if (this.ivjUICheckBox2 == null) {
      try {
        this.ivjUICheckBox2 = new UICheckBox();
        this.ivjUICheckBox2.setName("UICheckBox2");
        this.ivjUICheckBox2.setText(NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000050"));

        this.ivjUICheckBox2.setBounds(218, 123, 107, 22);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUICheckBox2;
  }

  private UICheckBox getUICheckBoxMeas2()
  {
    if (this.ivjUICheckBox1 == null) {
      try {
        this.ivjUICheckBox1 = new UICheckBox();
        this.ivjUICheckBox1.setName("UICheckBox1");
        this.ivjUICheckBox1.setText(NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000051"));

        this.ivjUICheckBox1.setBounds(218, 179, 121, 22);

        this.ivjUICheckBox1.setVisible(true);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUICheckBox1;
  }

  private UICheckBox getUICheckBoxNotAudit()
  {
    if (this.ivjUICheckBox4 == null) {
      try {
        this.ivjUICheckBox4 = new UICheckBox();
        this.ivjUICheckBox4.setName("UICheckBox4");
        this.ivjUICheckBox4.setText(NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000052"));

        this.ivjUICheckBox4.setBounds(13, 240, 151, 22);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUICheckBox4;
  }

  private UICheckBox getUICheckBoxSetPart()
  {
    if (this.ivjUICheckBox3 == null) {
      try {
        this.ivjUICheckBox3 = new UICheckBox();
        this.ivjUICheckBox3.setName("UICheckBox3");
        this.ivjUICheckBox3.setText(NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000053"));

        this.ivjUICheckBox3.setBounds(218, 152, 105, 22);
        this.ivjUICheckBox3.setActionCommand(NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000053"));
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUICheckBox3;
  }

  public QueryVO getQueryVO()
  {
    return this.m_voCondition;
  }

  public String getSqlShow()
  {
    return this.sSqlShow;
  }

  private UICheckBox getUICheckBoxRdcl()
  {
    if (this.ivjUICheckBoxRdcl == null) {
      try {
        this.ivjUICheckBoxRdcl = new UICheckBox();
        this.ivjUICheckBoxRdcl.setName("UICheckBoxRdcl");
        this.ivjUICheckBoxRdcl.setText(NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000057"));

        this.ivjUICheckBoxRdcl.setBounds(13, 208, 151, 22);

        this.ivjUICheckBoxRdcl.setVisible(true);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUICheckBoxRdcl;
  }

  private UIComboBox getUIComboBoxJJFS()
  {
    if (this.ivjUIComboBoxJJFS == null) {
      try {
        this.ivjUIComboBoxJJFS = new UIComboBox();
        this.ivjUIComboBoxJJFS.setName("UIComboBox2");
        this.ivjUIComboBoxJJFS.setLocation(343, 207);

        this.ivjUIComboBoxJJFS.addItem("");
        this.ivjUIComboBoxJJFS.addItem(NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000058"));

        this.ivjUIComboBoxJJFS.addItem(NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000059"));

        this.ivjUIComboBoxJJFS.addItem(NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000060"));

        this.ivjUIComboBoxJJFS.addItem(NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000061"));

        this.ivjUIComboBoxJJFS.addItem(NCLangRes.getInstance().getStrByID("common", "UC000-0003465"));

        this.ivjUIComboBoxJJFS.addItem(NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000062"));
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUIComboBoxJJFS;
  }

  private UIRefPane getUIRefDate1()
  {
    if (this.ivjUIRefDate1 == null) {
      try {
        this.ivjUIRefDate1 = new UIRefPane();
        this.ivjUIRefDate1.setName("UIRefDate1");
        this.ivjUIRefDate1.setBounds(97, 143, 99, 22);
        this.ivjUIRefDate1.setTextType("TextDate");
        this.ivjUIRefDate1.setRefNodeName("日历");
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUIRefDate1;
  }

  private UIRefPane getUIRefDate2()
  {
    if (this.ivjUIRefDate2 == null) {
      try {
        this.ivjUIRefDate2 = new UIRefPane();
        this.ivjUIRefDate2.setName("UIRefDate2");
        this.ivjUIRefDate2.setBounds(97, 175, 99, 22);
        this.ivjUIRefDate2.setTextType("TextDate");
        this.ivjUIRefDate2.setRefNodeName("日历");
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUIRefDate2;
  }

  private UILabel createLabel()
  {
    UILabel label = new UILabel();
    label.setText(NCLangRes.getInstance().getStrByID("common", "UC000-0003365"));

    return label;
  }

  private JobPhaseRef getUIRefPaneJobParse()
  {
    if (this.ivjUIRefPaneJobParse == null) {
      try {
        this.ivjUIRefPaneJobParse = new JobPhaseRef();
        this.ivjUIRefPaneJobParse.setName("UIRefPaneJobParse");
        this.ivjUIRefPaneJobParse.setBounds(814, 20, 10, 10);

        this.ivjUIRefPaneJobParse.setReturnCode(true);
        this.ivjUIRefPaneJobParse.setLocation(10000, 10000);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUIRefPaneJobParse;
  }

  private JobRef getUIRefPaneJob()
  {
    if (this.ivjUIRefPaneJob == null) {
      try {
        this.ivjUIRefPaneJob = new JobRef();
        this.ivjUIRefPaneJob.setName("UIRefPaneJob");
        this.ivjUIRefPaneJob.setLocation(162, 14);

        this.ivjUIRefPaneJob.setReturnCode(true);
        this.ivjUIRefPaneJob.setLocation(10000, 10000);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUIRefPaneJob;
  }

  public void valueChanged(ValueChangedEvent event)
  {
    Object oProjectID = getUIRefPaneJob().getRefPK();
    if ((oProjectID == null) || (oProjectID.toString().trim().length() == 0)) {
      ((PhaseRefModel)getUIRefPaneJobParse().getRefModel()).setJobID(null);
      return;
    }
    ((PhaseRefModel)getUIRefPaneJobParse().getRefModel()).setJobID(oProjectID.toString().trim());
  }

  private String getDefineWhereSQL()
  {
    StringBuffer sWhereSql = new StringBuffer();

    ConditionVO[] conditions = getConditionVO();

    for (int i = 0; i < conditions.length; i++) {
      if (conditions[i].getOperaCode().equals("like")) {
        conditions[i].setOperaCode(" like ");
        conditions[i].setValue("%" + conditions[i].getValue() + "%");
      }

      if ((conditions[i].getOperaCode().equals("<>")) || (conditions[i].getOperaCode().equals("!=")))
      {
        sWhereSql.append(conditions[i].getLogic() ? " and " : " or ");
        sWhereSql.append(conditions[i].getNoLeft() ? "" : "(");
        sWhereSql.append("(");
        sWhereSql.append(conditions[i].getFieldCode());
        sWhereSql.append("<>");
        sWhereSql.append((conditions[i].getDataType() == 1) || (conditions[i].getDataType() == 2) ? "" : "'");
        sWhereSql.append(conditions[i].getValue());
        sWhereSql.append((conditions[i].getDataType() == 1) || (conditions[i].getDataType() == 2) ? "" : "'");
        sWhereSql.append(" or ");
        sWhereSql.append(conditions[i].getFieldCode());
        sWhereSql.append(" is null ");
        sWhereSql.append(")");
        sWhereSql.append(conditions[i].getNoRight() ? "" : ")");
      }
      else {
        sWhereSql.append(conditions[i].getSQLStr());
      }
    }

    String result = "";

    if ((sWhereSql.indexOf("and") >= 0) && (sWhereSql.indexOf("and") < 3)) {
      result = sWhereSql.substring(sWhereSql.indexOf("and") + 3);
    }
    else {
      result = sWhereSql.toString();
    }

    return result;
  }

  private UIRefPane getUIRefPanerr()
  {
    if (this.ivjUIRefPanerr == null) {
      try {
        this.ivjUIRefPanerr = new UIRefPane();
        this.ivjUIRefPanerr.setName("UIRefPanerr");
        this.ivjUIRefPanerr.setBounds(219, 165, 99, 22);
        this.ivjUIRefPanerr.setRefNodeName("人员档案");

        this.ivjUIRefPanerr.getRefModel().setSealedDataShow(true);
        setValueRef("bd_psndoc.psncode", this.ivjUIRefPanerr);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUIRefPanerr;
  }

  private UIRefPane getUIRefDept()
  {
    if (this.ivjUIRefDept == null) {
      try {
        this.ivjUIRefDept = new UIRefPane();
        this.ivjUIRefDept.setName("UIRefDept");
        this.ivjUIRefDept.setToolTipText(NCLangRes.getInstance().getStrByID("20149020", "UPP20149020-000077"));

        this.ivjUIRefDept.setButtonFireEvent(true);
        this.ivjUIRefDept.setBounds(338, 77, 99, 22);
        this.ivjUIRefDept.setRefNodeName("部门档案");

        this.ivjUIRefDept.getRefModel().setSealedDataShow(true);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUIRefDept;
  }

  private String checkDate(String[] saDate, String[] corps)
  {
    UFDate[] dtaCheck = new UFDate[2];
    dtaCheck[0] = new UFDate(saDate[0], false);
    dtaCheck[1] = new UFDate(saDate[1], false);
    if (dtaCheck[1].before(dtaCheck[0])) {
      UFDate dtTemp = dtaCheck[0];
      dtaCheck[0] = dtaCheck[1];
      dtaCheck[1] = dtTemp;
    }

    if (dtaCheck[0].before(this.m_dtStart)) {
      return NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000054");
    }

    if (dtaCheck[1].after(this.m_dtLast)) {
      return NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000055");
    }

    return "";
  }

  protected void valueChangedForMultiCorps(String[] sCorps)
  {
    getUIRefPanekc1().setEnabled(false);
    getUIRefPanekc2().setEnabled(false);
    getUIRefPaneck().setEnabled(false);
    getUIRefPanerr().setEnabled(false);
    getUIRefDept().setEnabled(false);
    getUIRefPanech1().setEnabled(false);
    getUIRefPanech2().setEnabled(false);

    getUIRefPanekc1().setPK(null);
    getUIRefPanekc2().setPK(null);
    getUIRefPaneck().setPK(null);
    getUIRefPanerr().setPK(null);
    getUIRefDept().setPK(null);
    getUIRefPanech1().setPK(null);
    getUIRefPanech2().setPK(null);
  }

  protected void valueChangedForSingleCorp(String sCorp)
  {
    getUIRefPanekc1().setEnabled(true);
    getUIRefPanekc2().setEnabled(true);
    getUIRefPaneck().setEnabled(true);
    getUIRefPanerr().setEnabled(true);
    getUIRefDept().setEnabled(true);
    getUIRefPanech1().setEnabled(true);
    getUIRefPanech2().setEnabled(true);

    setRefWherePart(sCorp);
  }

  protected void valueChangedForNoCorp()
  {
    getUIRefPanekc1().setEnabled(true);
    getUIRefPanekc2().setEnabled(true);
    getUIRefPaneck().setEnabled(true);
    getUIRefPanerr().setEnabled(true);
    getUIRefDept().setEnabled(true);
    getUIRefPanech1().setEnabled(true);
    getUIRefPanech2().setEnabled(true);

    setRefWherePart(this.m_sCorpID);
  }
}