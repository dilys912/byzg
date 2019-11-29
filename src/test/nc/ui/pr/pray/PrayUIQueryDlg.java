package nc.ui.pr.pray;

import java.awt.Color;
import java.awt.Container;
import java.awt.Frame;
import java.util.ArrayList;
import nc.ui.bd.ref.AbstractRefGridTreeModel;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.bd.ref.AbstractRefTreeModel;
import nc.ui.bd.ref.RefUtil;
import nc.ui.bd.ref.busi.CalBodyDefaultRefModel;
import nc.ui.bd.ref.busi.DeptdocDefaultRefModel;
import nc.ui.bd.ref.busi.InvclDefaultRefModel;
import nc.ui.bd.ref.busi.InvmandocDefaultRefModel;
import nc.ui.bd.ref.busi.JobmngfilDefaultRefModel;
import nc.ui.bd.ref.busi.OperatorDefaultRefModel;
import nc.ui.ml.NCLangRes;
import nc.ui.po.mcref.MCEmployeeRefModel;
import nc.ui.po.mcref.MCInvClassRefModel;
import nc.ui.po.mcref.MCInvRefModel;
import nc.ui.po.mcref.MCProjectRefModel;
import nc.ui.po.pub.PoQueryDlgListener;
import nc.ui.pu.pub.BusiTypeRefPane;
import nc.ui.pu.pub.PuProjectPhaseRefModel;
import nc.ui.pu.pub.PuTool;
import nc.ui.pu.pub.PurPsnRefModel;
import nc.ui.pu.pub.UpSrcInfoPanel;
import nc.ui.pu.ref.PuBizTypeRefModel;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITabbedPane;
import nc.ui.scm.pub.def.DefSetTool;
import nc.ui.scm.pub.report.MultiCorpQueryClient;
import nc.ui.scm.ref.WarehouseRefModel;
import nc.vo.bd.CorpVO;
import nc.vo.pu.pr.PrayPubVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.query.QueryConditionVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.sm.UserVO;

public class PrayUIQueryDlg extends MultiCorpQueryClient
{
  public static final String CORPKEY = "po_praybill_corp";
  public static final String[] REFKEYS = { "po_praybill.cpraypsn", "po_praybill.cdeptid", "po_praybill_b.cprojectid", "bd_invcl.invclasscode", "bd_invbasdoc.invcode", "bd_invbasdoc.invname", "po_praybill.ccustomerid", "po_praybill.cbiztype" };

  private String m_strPkCorp = null;

  private UICheckBox m_chkFree = null;

  private UICheckBox m_chkClosed = null;

  private UICheckBox m_chkGoing = null;

  private UICheckBox m_chkPass = null;

  private UICheckBox m_chkNoPass = null;

  private Container m_conParent = null;

  private UpSrcInfoPanel m_pnlUpsrcTab = null;

  private ClientEnvironment m_clientEnv = ClientEnvironment.getInstance();

  private String[] m_saRefFieldCode = null;

  public PrayUIQueryDlg(Container parent)
  {
    super(parent);
    init(null);
  }

  public PrayUIQueryDlg(Container parent, String pk_corp)
  {
    super(parent);
    this.m_strPkCorp = pk_corp;
    this.m_conParent = parent;
    init(null);

    setShowPrintStatusPanel(true);
    setNormalShow(true);

    hideCorp();
  }

  public PrayUIQueryDlg(Container parent, String pk_corp, String templet_id)
  {
    super(parent);
    this.m_strPkCorp = pk_corp;
    this.m_conParent = parent;
    init(templet_id);

    setShowPrintStatusPanel(true);
    setNormalShow(true);
  }

  public PrayUIQueryDlg(Frame parent)
  {
    super(parent);

    setShowPrintStatusPanel(true);
    setNormalShow(true);
    init(null);
  }

  public PrayUIQueryDlg(Container parent, String sTitle, String sFuncode, String sPk_corp, String sOperId)
  {
    super(parent, sTitle, sFuncode, sPk_corp, sOperId);

    setShowPrintStatusPanel(true);
    setNormalShow(true);
    init(null);
  }

  private AbstractRefModel getRefModelPurOrg()
  {
    AbstractRefModel refmodel = RefUtil.getRefModel("采购组织");
    return refmodel;
  }

  public String getSelectedCorpId()
  {
    String sSelectedPkCorp = null;
    if (isCorpsShow()) {
      if (getSelectedCorpIDs() != null)
        sSelectedPkCorp = getSelectedCorpIDs()[0];
      else
        sSelectedPkCorp = "NULLNULLNULL";
    }
    else {
      sSelectedPkCorp = getLoginCorp();
    }
    return sSelectedPkCorp;
  }

  public String getSelectedCorpIdString()
  {
    String sSelectedPkCorp = "";
    if (isCorpsShow()) {
      if (getSelectedCorpIDs() != null) {
        String[] corpID = getSelectedCorpIDs();
        sSelectedPkCorp = "('";
        for (int i = 0; i < corpID.length - 1; i++) {
          sSelectedPkCorp = sSelectedPkCorp + corpID[i] + "','";
        }
        sSelectedPkCorp = sSelectedPkCorp + corpID[(corpID.length - 1)] + "')";
      } else {
        sSelectedPkCorp = sSelectedPkCorp + "('NULL')";
      }
    }
    else sSelectedPkCorp = sSelectedPkCorp + "('" + getLoginCorp() + "')";

    return sSelectedPkCorp;
  }

  public String checkCondition(ConditionVO[] conditions)
  {
    String strErrMsg = null;
    strErrMsg = super.checkCondition(conditions);
    if (strErrMsg != null)
      return strErrMsg;
    if (getStatusCndStr().trim().length() <= 0) {
      strErrMsg = NCLangRes.getInstance().getStrByID("40040101", "UPP40040101-000441");
    }

    return strErrMsg;
  }

  public boolean[] getStatusCndBool()
  {
    boolean[] bStatus = { false, false, false, false, false };
    if (this.m_chkFree.isSelected())
      bStatus[0] = true;
    if (this.m_chkClosed.isSelected())
      bStatus[1] = true;
    if (this.m_chkGoing.isSelected())
      bStatus[2] = true;
    if (this.m_chkPass.isSelected())
      bStatus[3] = true;
    if (this.m_chkNoPass.isSelected())
      bStatus[4] = true;
    return bStatus;
  }

  public String getStatusCndStr()
  {
    StringBuffer strCond = new StringBuffer("");
    boolean[] bStatus = getStatusCndBool();
    for (int i = 0; i < bStatus.length; i++) {
      if (!bStatus[i]) {//编译报错修改 bStatus[i] == 1
        if (strCond.toString().trim().length() > 0) {
          strCond.append("or ");
        }
        strCond.append("ibillstatus = ");
        strCond.append(i);
        strCond.append(" ");
      }
    }
    return strCond.toString();
  }

  public String getSubSQL()
  {
    return this.m_pnlUpsrcTab.getSubSQL();
  }

  private void init(String templet_id)
  {
    try
    {
      if (templet_id == null) {
        templet_id = "40040101";
      }
      setTempletID(this.m_strPkCorp, templet_id, this.m_clientEnv.getUser().getPrimaryKey(), null);
    }
    catch (Exception e) {
      SCMEnv.out(e);
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270"), NCLangRes.getInstance().getStrByID("40040101", "UPP40040101-000087"));
    }

    setLoginCorp(this.m_strPkCorp);

    super.hideUnitButton();
    initCorp();

    initRefpane();

    initComboBox();

    initNormal();

    this.m_pnlUpsrcTab = new UpSrcInfoPanel("20", new String[] { "30", "A1", "A2", "5A", "422X" });

    getUITabbedPane().insertTab(this.m_pnlUpsrcTab.getName(), null, this.m_pnlUpsrcTab, null, 1);

    setIsWarningWithNoInput(true);
  }

  private void initComboBox()
  {
    String[] sPrayType = { "", "MRP", "MO", "SCF", NCLangRes.getInstance().getStrByID("40040101", "UPP40040101-000458"), NCLangRes.getInstance().getStrByID("40040101", "UPP40040101-000459"), NCLangRes.getInstance().getStrByID("40040101", "UPP40040101-000460"), "DRP", NCLangRes.getInstance().getStrByID("40040101", "UPP40040101-000461"), NCLangRes.getInstance().getStrByID("4004pub", "UPP4004pub-000204") };

    UIComboBox prayTypeCombo = new UIComboBox(sPrayType);
    prayTypeCombo.setTranslate(true);
    setValueRef("po_praybill.ipraysource", prayTypeCombo);

    String[] sPraySource = { "", NCLangRes.getInstance().getStrByID("40040101", "UPP40040101-000470"), NCLangRes.getInstance().getStrByID("40040101", "UPP40040101-000471"), NCLangRes.getInstance().getStrByID("40040101", "UPP40040101-000456"), NCLangRes.getInstance().getStrByID("40040101", "UPP40040101-000457") };

    UIComboBox praySourceCombo = new UIComboBox(sPraySource);
    praySourceCombo.setTranslate(true);
    setValueRef("po_praybill.ipraytype", praySourceCombo);
  }

  private void initNormal()
  {
    this.m_chkFree = new UICheckBox(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000340"));

    this.m_chkFree.setBackground(this.m_conParent.getBackground());
    this.m_chkFree.setForeground(Color.black);
    this.m_chkFree.setSize(100, this.m_chkFree.getHeight());
    this.m_chkFree.setSelected(true);

    this.m_chkClosed = new UICheckBox(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000119"));

    this.m_chkClosed.setBackground(this.m_conParent.getBackground());
    this.m_chkClosed.setForeground(Color.black);
    this.m_chkClosed.setSize(100, this.m_chkClosed.getHeight());

    this.m_chkGoing = new UICheckBox(NCLangRes.getInstance().getStrByID("40040101", "UPP40040101-000472"));

    this.m_chkGoing.setBackground(this.m_conParent.getBackground());
    this.m_chkGoing.setForeground(Color.black);
    this.m_chkGoing.setSize(110, this.m_chkGoing.getHeight());

    this.m_chkPass = new UICheckBox(NCLangRes.getInstance().getStrByID("40040101", "UPP40040101-000473"));

    this.m_chkPass.setBackground(this.m_conParent.getBackground());
    this.m_chkPass.setForeground(Color.black);
    this.m_chkPass.setSize(150, this.m_chkPass.getHeight());

    this.m_chkNoPass = new UICheckBox(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000242"));

    this.m_chkNoPass.setBackground(this.m_conParent.getBackground());
    this.m_chkNoPass.setForeground(Color.black);
    this.m_chkNoPass.setSize(170, this.m_chkNoPass.getHeight());

    this.m_chkFree.setLocation(50, 30);
    this.m_chkClosed.setLocation(this.m_chkFree.getX(), this.m_chkFree.getY() + this.m_chkFree.getHeight() + 20);
    this.m_chkGoing.setLocation(this.m_chkFree.getX(), this.m_chkClosed.getY() + this.m_chkClosed.getHeight() + 20);
    this.m_chkPass.setLocation(this.m_chkFree.getX(), this.m_chkGoing.getY() + this.m_chkGoing.getHeight() + 20);
    this.m_chkNoPass.setLocation(this.m_chkFree.getX(), this.m_chkPass.getY() + this.m_chkPass.getHeight() + 20);

    super.getUIPanelNormal().setLayout(null);

    super.getUIPanelNormal().add(this.m_chkFree);
    super.getUIPanelNormal().add(this.m_chkClosed);
    super.getUIPanelNormal().add(this.m_chkGoing);
    super.getUIPanelNormal().add(this.m_chkPass);
    super.getUIPanelNormal().add(this.m_chkNoPass);
  }

  private void initRefpane()
  {
    setValueRef("po_praybill.dpraydate", "日历");
    setDefaultValue("po_praybill.dpraydate", "po_praybill.dpraydate", this.m_clientEnv.getDate().toString());

    BusiTypeRefPane biztypeRef = new BusiTypeRefPane(this.m_clientEnv.getCorporation().getPrimaryKey(), "20");

    setValueRef("po_praybill.cbiztype", biztypeRef);

    String[] saInitRefCode = getInitRefFieldCodes();
    int iLen = saInitRefCode.length;
    for (int i = 0; i < iLen; i++) {
      AbstractRefModel refModel = getRefModel(saInitRefCode[i]);
      if (refModel == null)
        continue;
      UIRefPane refPane = new UIRefPane();

      refPane.setRefModel(refModel);
      setValueRef(saInitRefCode[i], refPane);
    }

    DefSetTool.updateQueryConditionClientUserDef(this, this.m_strPkCorp, "20", "po_praybill.vdef", "po_praybill_b.vdef");
  }

  private String[] getInitRefFieldCodes()
  {
    return new String[] { "po_praybill_b.cemployeeid", "po_praybill_b.cpurorganization", "po_praybill.cdeptid", "po_praybill.coperator", "po_praybill.cbiztype", "po_praybill.cauditpsn", "po_praybill_b.cprojectid", "po_praybill_b.cprojectphaseid" };
  }

  public void calculateDataPowerVOs()
  {
    QueryConditionVO[] condVOs = getConditionDatas();
    PuTool.switchReturnType(PrayPubVO._Hash_PrayUI, PrayPubVO._Hash_PrayUI_Code_Flag, condVOs, true);

    super.calculateDataPowerVOs();
    PuTool.switchReturnType(PrayPubVO._Hash_PrayUI, PrayPubVO._Hash_PrayUI_Code_Flag, condVOs, false);
  }

  public void afterChangeWhenMultiCorp()
  {
    int iLen = getRefFieldCodes().length;
    Object oRef = null;
    UIRefPane refPaneCur = null;
    AbstractRefModel refModel = null;
    for (int i = 0; i < iLen; i++) {
      oRef = getValueRefObjectByFieldCode(getRefFieldCodes()[i]);
      if (oRef == null) {
        continue;
      }
      if ((oRef == null) || (!(oRef instanceof UIRefPane))) {
        continue;
      }
      refPaneCur = (UIRefPane)oRef;
      if (isCorpsShow())
        if ((getSelectedCorpIDs() != null) && (getSelectedCorpIDs().length == 1))
        {
          refPaneCur.setPk_corp(getSelectedCorpId());
          if (refPaneCur.getRefModel() != null) {
            if ((refPaneCur.getRefModel() instanceof WarehouseRefModel)) {
              refPaneCur.setRefModel(new WarehouseRefModel(getSelectedCorpId()));
            }
            else if ("业务类型".equals(refPaneCur.getRefModel().getRefNodeName()))
            {
              refPaneCur.setRefModel(new PuBizTypeRefModel(getSelectedCorpId(), "21"));
            }
            else if ("采购组织".equals(refPaneCur.getRefModel().getRefNodeName()))
            {
              refPaneCur.getRefModel().setWherePart("bd_purorg.ownercorp = '" + getSelectedCorpId() + "' ");
            }
            else
            {
              refPaneCur.getRefModel().setPk_corp(getSelectedCorpId());
            }
          }
        }
        else if (getSelectedCorpIDs() == null)
        {
          refPaneCur.setPk_corp("0001");
          if (refPaneCur.getRefModel() == null)
            continue;
          if ((refPaneCur.getRefModel() instanceof WarehouseRefModel)) {
            refPaneCur.setRefModel(new WarehouseRefModel("0001"));
          }
          else if ("业务类型".equals(refPaneCur.getRefModel().getRefNodeName()))
          {
            refPaneCur.setRefModel(new PuBizTypeRefModel("0001", "21"));
          }
          else if ("采购组织".equals(refPaneCur.getRefModel().getRefNodeName()))
          {
            refPaneCur.getRefModel().setWherePart("bd_purorg.ownercorp = '0001' or bd_purorg.ownercorp is null ");
          }
          else
          {
            refPaneCur.getRefModel().setPk_corp("0001");
          }
        }
        else
        {
          refModel = getRefModel(getRefFieldCodes()[i]);
          if (refModel != null)
            refPaneCur.setRefModel(refModel);
        }
    }
  }

  private PurPsnRefModel getRefModelPuEmployee()
  {
    if ((getSelectedCorpIDs() != null) && (getSelectedCorpIDs().length > 1)) {
      return new MCEmployeeRefModel(getSelectedCorpIDs());
    }
    return new PurPsnRefModel(getSelectedCorpId());
  }

  private AbstractRefTreeModel getRefModelDept()
  {
    DeptdocDefaultRefModel refmodel = new DeptdocDefaultRefModel("部门档案");
    refmodel.setPk_corp(getSelectedCorpId());
    return refmodel;
  }

  private AbstractRefModel getRefModelCalbody()
  {
    CalBodyDefaultRefModel refmodel = new CalBodyDefaultRefModel("库存组织");
    refmodel.setPk_corp(getSelectedCorpId());
    return refmodel;
  }

  private AbstractRefModel getRefModelOperator()
  {
    AbstractRefModel refmodel = new OperatorDefaultRefModel("权限操作员");

    refmodel.setPk_corp(getSelectedCorpId());
    return refmodel;
  }

  private AbstractRefModel getRefModelBusiType()
  {
    return new PuBizTypeRefModel(getSelectedCorpId(), "20");
  }

  private AbstractRefTreeModel getRefModelInvClass()
  {
    InvclDefaultRefModel refmodel = null;
    if ((getSelectedCorpIDs() != null) && (getSelectedCorpIDs().length > 1)) {
      refmodel = new MCInvClassRefModel(getSelectedCorpIDs());
    } else {
      refmodel = new InvclDefaultRefModel("存货分类");
      refmodel.setPk_corp(getSelectedCorpId());
    }
    return refmodel;
  }

  private AbstractRefGridTreeModel getRefModelInventory()
  {
    AbstractRefGridTreeModel refmodel = null;
    if ((getSelectedCorpIDs() != null) && (getSelectedCorpIDs().length > 1)) {
      refmodel = new MCInvRefModel(getSelectedCorpIDs());
    }
    else {
      refmodel = new InvmandocDefaultRefModel("存货档案");

      refmodel.setPk_corp(getSelectedCorpId());
    }

    return refmodel;
  }

  private AbstractRefGridTreeModel getRefModelProject()
  {
    AbstractRefGridTreeModel refmodel = null;
    if ((getSelectedCorpIDs() != null) && (getSelectedCorpIDs().length > 1)) {
      refmodel = new MCProjectRefModel(getSelectedCorpIDs());
    }
    else {
      refmodel = new JobmngfilDefaultRefModel("项目管理档案");

      refmodel.setPk_corp(getSelectedCorpId());
    }
    return refmodel;
  }

  private PuProjectPhaseRefModel getRefModelProjectPhase()
  {
    return new PuProjectPhaseRefModel(getSelectedCorpId(), null);
  }

  public boolean isExistField(String sFieldCode)
  {
    int iDefinedLen = getRefFieldCodes().length;
    for (int i = 0; i < iDefinedLen; i++) {
      if (sFieldCode.equals(getRefFieldCodes()[i])) {
        return true;
      }
    }

    return false;
  }

  private AbstractRefModel getRefModel(String sField)
  {
    if (!isExistField(sField)) {
      return null;
    }

    AbstractRefModel refModel = null;
    if (sField.equals("po_praybill.cdeptid"))
      refModel = getRefModelDept();
    else if ((sField.equals("po_praybill_b.cemployeeid")) || (sField.equals("po_praybill.cpraypsn")))
    {
      refModel = getRefModelPuEmployee();
    } else if (sField.equals("bd_invbasdoc.invcode"))
    {
      refModel = getRefModelInventory();
    } else if (sField.equals("bd_invbasdoc.invname"))
    {
      refModel = getRefModelInventory();
    } else if (sField.equals("bd_invcl.invclasscode"))
    {
      refModel = getRefModelInvClass();
    } else if (sField.equals("po_praybill_b.cprojectid"))
    {
      refModel = getRefModelProject();
    } else if (sField.equals("po_praybill_b.cprojectphaseid"))
    {
      refModel = getRefModelProjectPhase();
    } else if (sField.equals("po_praybill_b.cpurorganization"))
    {
      refModel = getRefModelPurOrg();
    } else if (sField.equals("po_praybill.cbiztype"))
    {
      refModel = getRefModelBusiType();
    } else if ((sField.equals("po_praybill.coperator")) || (sField.equals("po_praybill.cauditpsn")))
    {
      refModel = getRefModelOperator();
    } else if (sField.equals("po_praybill_b.cwarehouseid"))
    {
      refModel = new WarehouseRefModel(getSelectedCorpId());
    } else if (sField.equals("po_praybill_b.pk_reqstoorg"))
    {
      refModel = getRefModelCalbody();
    }

    if (refModel != null) {
      refModel.setSealedDataShow(true);
    }

    return refModel;
  }

  private String[] getRefFieldCodes()
  {
    if (this.m_saRefFieldCode == null) {
      ArrayList listFieldCode = new ArrayList();
      QueryConditionVO[] voaData = getConditionDatas();
      if (voaData != null) {
        int iLen = voaData.length;
        for (int i = 0; i < iLen; i++) {
          listFieldCode.add(voaData[i].getFieldCode());
        }
      }
      int iSize = listFieldCode.size();
      this.m_saRefFieldCode = (iSize == 0 ? null : (String[])(String[])listFieldCode.toArray(new String[iSize]));
    }

    return this.m_saRefFieldCode;
  }

  protected int doInitBeforShowModal()
  {
    int result = super.doInitBeforShowModal();

    initRefListener();

    initRefpane();

    initCorpRefs();

    return result;
  }

  public void initCorpRefs()
  {
    ArrayList alcorp = new ArrayList();
    alcorp.add(ClientEnvironment.getInstance().getCorporation().getPrimaryKey());
    initCorpRef("po_praybill_corp", ClientEnvironment.getInstance().getCorporation().getPrimaryKey(), alcorp);

    setCorpRefs("po_praybill_corp", REFKEYS);
  }

  protected void initRefListener()
  {
    PoQueryDlgListener listener = new PoQueryDlgListener(this, new String[] { "po_praybill_b.pk_reqcorp" }, new String[][] { { "po_praybill_b.cwarehouseid", "po_praybill_b.pk_reqstoorg" } });

    Object oRef = getValueRefObjectByFieldCode("po_praybill_b.pk_reqcorp");
    UIRefPane refPaneCur = null;
    if ((oRef != null) && ((oRef instanceof UIRefPane))) {
      refPaneCur = (UIRefPane)oRef;
      refPaneCur.setName("po_praybill_b.pk_reqcorp");
      refPaneCur.addValueChangedListener(listener);
    }
  }
}