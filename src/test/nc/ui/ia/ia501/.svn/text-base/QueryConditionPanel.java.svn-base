package nc.ui.ia.ia501;

import java.awt.Container;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.bd.ref.UFRefManage;
import nc.ui.ia.pub.CommonDataBO_Client;
import nc.ui.ia.pub.IAEnvironment;
import nc.ui.ia.pub.IAMultiCorpQueryDialog;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITabbedPane;
import nc.ui.scm.pub.def.DefSetTool;
import nc.vo.ia.pub.Log;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.query.ConditionVO;

public class QueryConditionPanel extends IAMultiCorpQueryDialog
{
  private static final long serialVersionUID = 1L;
  public InoutLedgerNormalPanel ivjNormalPanel1 = null;
  private UIRefPane ivjUIRefPaneRdcl = null;
  private UIRefPane ivjUIRefPanebusiness = null;
  private UIRefPane ivjUIRefPaneagent = null;
  private UIRefPane ivjUIRefPaneemployee = null;
  private UIRefPane ivjUIRefPaneproject = null;

  private String[] dates = null;

  protected IAEnvironment ce = new IAEnvironment();
  private UIRefPane ivjUIRefPaneCostObject = null;

  public QueryConditionPanel(Container parent, String title, String operatorID, String pk_corp)
  {
    super(parent, title, operatorID, pk_corp, "20148010");
    initialize();
  }

  protected void handlePeci()
    throws Exception
  {
    int[] iPeci = this.ce.getDataPrecision(this.ce.getCorporationID());
    setValueRef("ninnum", new Integer(iPeci[0]));
    setValueRef("ninprice", new Integer(iPeci[1]));
    setValueRef("ninmny", new Integer(iPeci[2]));

    setValueRef("noutnum", new Integer(iPeci[0]));
    setValueRef("noutprice", new Integer(iPeci[1]));
    setValueRef("noutmny", new Integer(iPeci[2]));

    setValueRef("a.nplanedprice", new Integer(iPeci[1]));
    setValueRef("a.nplanedmny", new Integer(iPeci[2]));
  }

  public String checkCondition(ConditionVO[] conditions)
  {
    int left = 0;
    int right = 0;
    for (int i = 0; i < conditions.length; i++) {
      if ((conditions[i].getValue() == null) || (conditions[i].getValue().toString().trim().length() <= 0)) {
        continue;
      }
      if (!conditions[i].getNoLeft())
        left++;
      if (!conditions[i].getNoRight())
        right++;
      if (left < right)
        return NCLangRes.getInstance().getStrByID("20148010", "UPP20148010-000009");
    }
    if (left != right) {
      return NCLangRes.getInstance().getStrByID("20148010", "UPP20148010-000009");
    }

    String[] sCorpIDs = getSelectedCorpIDs();
    String[] sCorpNames = getSelectedCorpNames();
    if ((sCorpIDs == null) || (sCorpIDs.length == 0)) {
      return NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000021");
    }

    try
    {
      String unStartIACorps = checkifstartIA(sCorpIDs, sCorpNames);

      if (unStartIACorps.length() > 0)
        return "公司 " + unStartIACorps + " 未启用存货核算，不能查询！";
    }
    catch (Exception e) {
      Log.error(e);
      return e.getMessage();
    }

    String[] dates = new String[2];

    dates[0] = getNormalPanel().getUIRefBillDate1().getRefCode();
    dates[1] = getNormalPanel().getUIRefBillDate2().getRefCode();

    if ((dates[0] == null) || (dates[0].length() <= 0)) {
      return NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000023");
    }

    if ((dates[1] == null) || (dates[1].length() <= 0)) {
      return NCLangRes.getInstance().getStrByID("20149040", "UPP20149040-000024");
    }

    setDates(dates);

    return null;
  }

  public void beforeDialogClosed()
  {
    ConditionVO[] conditions = getConditionVO();

    for (int i = 0; i < conditions.length; i++)
    {
      if (conditions[i].getOperaCode().equals("like")) {
        conditions[i].setOperaCode(" like ");
        conditions[i].setValue("%" + conditions[i].getValue() + "%");
      }
    }
  }

  public String[] getMultiCorpIDs()
  {
    String[] Corps = getSelectedCorpIDs();

    return Corps;
  }

  public InoutLedgerNormalPanel getNormalPanel()
  {
    if (this.ivjNormalPanel1 == null)
    {
      this.ivjNormalPanel1 = new InoutLedgerNormalPanel();
    }
    return this.ivjNormalPanel1;
  }

  private UIRefPane getUIRefPaneagent() {
    if (this.ivjUIRefPaneagent == null) {
      try {
        this.ivjUIRefPaneagent = new UIRefPane();
        this.ivjUIRefPaneagent.setName("UIRefPaneAgent");
        this.ivjUIRefPaneagent.setLocation(278, 16);
        this.ivjUIRefPaneagent.setRefNodeName("人员档案");

        this.ivjUIRefPaneagent.getRefModel().setSealedDataShow(true);
        this.ivjUIRefPaneagent.setReturnCode(true);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUIRefPaneagent;
  }

  private UIRefPane getUIRefPaneemployee() {
    if (this.ivjUIRefPaneemployee == null) {
      try {
        this.ivjUIRefPaneemployee = new UIRefPane();
        this.ivjUIRefPaneemployee.setName("UIRefPaneEmployee");
        this.ivjUIRefPaneemployee.setLocation(278, 16);
        this.ivjUIRefPaneemployee.setRefNodeName("人员档案");

        this.ivjUIRefPaneemployee.getRefModel().setSealedDataShow(true);
        this.ivjUIRefPaneemployee.setReturnCode(true);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUIRefPaneemployee;
  }

  private UIRefPane getUIRefPanebusiness() {
    if (this.ivjUIRefPanebusiness == null) {
      try {
        this.ivjUIRefPanebusiness = new UIRefPane();
        this.ivjUIRefPanebusiness.setName("UIRefPanebusiness");
        this.ivjUIRefPanebusiness.setLocation(278, 16);
        this.ivjUIRefPanebusiness.setRefNodeName("业务类型");

        this.ivjUIRefPanebusiness.getRefModel().setSealedDataShow(true);
        this.ivjUIRefPanebusiness.setReturnCode(true);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUIRefPanebusiness;
  }
  private UIRefPane getUIRefPaneproject() {
    if (this.ivjUIRefPaneproject == null) {
      try {
        this.ivjUIRefPaneproject = new UIRefPane();
        this.ivjUIRefPaneproject.setName("UIRefPaneProject");
        this.ivjUIRefPaneproject.setLocation(278, 16);
        this.ivjUIRefPaneproject.setRefNodeName("项目管理档案");

        this.ivjUIRefPaneproject.getRefModel().setSealedDataShow(true);
        this.ivjUIRefPaneproject.setReturnCode(true);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUIRefPaneproject;
  }
  private UIRefPane getUIRefPanerdcl() {
    if (this.ivjUIRefPaneRdcl == null) {
      try {
        this.ivjUIRefPaneRdcl = new UIRefPane();
        this.ivjUIRefPaneRdcl.setName("UIRefPaneRdcl");
        this.ivjUIRefPaneRdcl.setLocation(278, 16);
        this.ivjUIRefPaneRdcl.setRefNodeName("收发类别");

        this.ivjUIRefPaneRdcl.getRefModel().setSealedDataShow(true);
        this.ivjUIRefPaneRdcl.setReturnCode(true);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUIRefPaneRdcl;
  }

  public void handleException(Throwable exception)
  {
    Log.error(exception);
  }

  private void initialize()
  {
    try
    {
      setSealedDataShow(true);

      setName("QueryConditionPanel");
    } catch (Throwable ivjExc) {
      handleException(ivjExc);
    }

    getUIPanelNormal().add(getNormalPanel());

    setValueRef("a.cdispatchid", getUIRefPanerdcl());

    setValueRef("a.cbiztypeid", getUIRefPanebusiness());

    setValueRef("a.cagentid", getUIRefPaneagent());

    setValueRef("a.cemployeeid", getUIRefPaneemployee());

    setValueRef("a.cprojectid", getUIRefPaneproject());

    setValueRef("cb.bomcode", getCostObjectRef());

    getUITabbedPane().addChangeListener(this);
    try
    {
      String period = CommonDataBO_Client.getStartPeriod(this.ce.getCorporationID());
      String sBeginDate = CommonDataBO_Client.getMonthBeginDate(this.ce.getCorporationID(), period).toString();
      String sEndDate = this.ce.getBusinessDate().toString();

      getNormalPanel().getUIRefBillDate1().setText(sBeginDate);
      getNormalPanel().getUIRefBillDate2().setText(sEndDate);
    } catch (BusinessException e1) {
      e1.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }

    DefSetTool.updateQueryConditionClientUserDef(this, this.ce.getCorporationID(), "iabill", "a.vdef", "", 0, "a.bdef", "", 0);
    try
    {
      handlePeci();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private UIRefPane getCostObjectRef()
  {
    if (this.ivjUIRefPaneCostObject == null) {
      try {
        this.ivjUIRefPaneCostObject = new UIRefPane();
        this.ivjUIRefPaneCostObject.setName("UIRefPanecb");
        this.ivjUIRefPaneCostObject.setLocation(278, 16);
        this.ivjUIRefPaneCostObject.setRefNodeName("存货档案");

        this.ivjUIRefPaneCostObject.getRefModel().setSealedDataShow(true);
        this.ivjUIRefPaneCostObject.setReturnCode(true);

        AbstractRefModel refmodel = this.ivjUIRefPaneCostObject.getRefModel();

        refmodel.setStrPatch(" distinct ");

        String sTableName = refmodel.getTableName();
        sTableName = sTableName + ",bd_produce";
        refmodel.setTableName(sTableName);

        String sWhere = refmodel.getWherePart();
        sWhere = sWhere + " and bd_produce.pk_invmandoc = bd_invmandoc.pk_invmandoc ";
        sWhere = sWhere + " and bd_produce.sfcbdx = 'Y' ";

        refmodel.setWherePart(sWhere);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUIRefPaneCostObject;
  }

  public String[] getDates() {
    return this.dates;
  }
  public void setDates(String[] dates) {
    this.dates = dates;
  }

  protected void valueChangedForMultiCorps(String[] sCorps)
  {
    getNormalPanel().getUIRefCrdcenterid().setEnabled(false);
    getNormalPanel().getUIRefCrdcenterid().setText(null);
    getNormalPanel().getUIRefCwarehouseid().setEnabled(false);
    getNormalPanel().getUIRefCwarehouseid().setText(null);
    getNormalPanel().getUIRefDept1().setEnabled(false);
    getNormalPanel().getUIRefDept1().setText(null);
    getNormalPanel().getUIRefDept2().setEnabled(false);
    getNormalPanel().getUIRefDept2().setText(null);

    getUIRefPaneagent().setEnabled(false);
    getUIRefPaneagent().setText(null);
    getUIRefPaneemployee().setEnabled(false);
    getUIRefPaneemployee().setText(null);

    getUIRefPanebusiness().setPk_corp("0001");
    getUIRefPanebusiness().getRef().getRefModel().setWherePart("bd_busitype.pk_corp='0001'");

    getUIRefPanebusiness().getRef().getRefModel().setPk_corp("0001");

    getNormalPanel().changeCustomAndVender(sCorps);

    getNormalPanel().getUIRefInventoryid1().setEnabled(false);
    getNormalPanel().getUIRefInventoryid1().setText(null);
    getNormalPanel().getUIRefInventoryid2().setEnabled(false);
    getNormalPanel().getUIRefInventoryid2().setText(null);
  }

  protected void valueChangedForSingleCorp(String sCorp)
  {
    getNormalPanel().getUIRefCrdcenterid().setEnabled(true);
    getNormalPanel().getUIRefCwarehouseid().setEnabled(true);
    getNormalPanel().getUIRefDept1().setEnabled(true);
    getNormalPanel().getUIRefDept2().setEnabled(true);

    getNormalPanel().getUIRefCrdcenterid().getRef().getRefModel().setWherePart("pk_corp = " + sCorp + " and property in (" + 0 + "," + 2 + ")");

    getNormalPanel().getUIRefCrdcenterid().setPk_corp(sCorp);
    getNormalPanel().getUIRefCrdcenterid().getRef().getRefModel().setPk_corp(sCorp);

    getNormalPanel().getUIRefCwarehouseid().getRef().getRefModel().setWherePart(" bd_stordoc.pk_corp ='" + sCorp + "'");

    getNormalPanel().getUIRefCwarehouseid().setPk_corp(sCorp);
    getNormalPanel().getUIRefCwarehouseid().getRef().getRefModel().setPk_corp(sCorp);

    getNormalPanel().getUIRefDept1().getRef().getRefModel().setWherePart("bd_deptdoc.pk_corp='" + sCorp + "' ");
    getNormalPanel().getUIRefDept1().setPk_corp(sCorp);
    getNormalPanel().getUIRefDept1().getRef().getRefModel().setPk_corp(sCorp);

    getNormalPanel().getUIRefDept2().getRef().getRefModel().setWherePart("bd_deptdoc.pk_corp='" + sCorp + "' ");
    getNormalPanel().getUIRefDept2().setPk_corp(sCorp);
    getNormalPanel().getUIRefDept2().getRef().getRefModel().setPk_corp(sCorp);

    getUIRefPaneagent().setEnabled(true);
    getUIRefPaneemployee().setEnabled(true);

    getUIRefPaneagent().setPk_corp(sCorp);
    getUIRefPaneagent().getRef().getRefModel().setPk_corp(sCorp);

    getUIRefPaneemployee().setPk_corp(sCorp);
    getUIRefPaneemployee().getRef().getRefModel().setPk_corp(sCorp);

    getUIRefPanebusiness().setPk_corp(sCorp);
    getUIRefPanebusiness().getRef().getRefModel().setWherePart("bd_busitype.pk_corp='" + sCorp + "'");

    getUIRefPanebusiness().getRef().getRefModel().setPk_corp(sCorp);

    getNormalPanel().getUIRefInventoryid1().setEnabled(true);
    getNormalPanel().getUIRefInventoryid1().setPk_corp(sCorp);
    getNormalPanel().getUIRefInventoryid1().getRef().getRefModel().setPk_corp(sCorp);
    getNormalPanel().getUIRefInventoryid2().setEnabled(true);
    getNormalPanel().getUIRefInventoryid2().setPk_corp(sCorp);
    getNormalPanel().getUIRefInventoryid2().getRef().getRefModel().setPk_corp(sCorp);

    getNormalPanel().changeCustomAndVender(new String[] { sCorp });
  }

  protected void valueChangedForNoCorp()
  {
  }

  private String checkifstartIA(String[] corpIDs, String[] corpNames)
    throws Exception
  {
    String curPeriod = this.ce.getAccountPeriod();
    StringBuffer buffer = new StringBuffer(0);

    for (int i = 0; i < corpIDs.length; i++)
    {
      if (!CommonDataBO_Client.isModuleStarted(corpIDs[i], "2014", curPeriod).booleanValue()) {
        if (buffer.length() > 0) {
          buffer.append(",");
        }
        buffer.append("\"");
        buffer.append(corpNames[i]);
        buffer.append("\"");
      }
    }

    return buffer.toString();
  }
}