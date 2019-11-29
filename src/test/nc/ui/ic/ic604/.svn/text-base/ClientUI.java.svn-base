package nc.ui.ic.ic604;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Vector;
import nc.ui.bd.manage.UIRefCellEditor;
import nc.ui.ic.pub.bill.query.ICheckCondition;
import nc.ui.ic.pub.bill.query.QueryConditionDlg;
import nc.ui.ic.pub.report.ICReportHelper;
import nc.ui.ic.pub.report.IcBaseReport;
import nc.ui.ic.pub.scale.ScaleInit;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.FramePanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.report.ReportBaseClass;
import nc.ui.pub.report.ReportItem;
import nc.ui.scm.sourcebill.SourceBillFlowDlg;
import nc.vo.bd.CorpVO;
import nc.vo.ic.ic604.OnRouteBillVO;
import nc.vo.ic.ic604.OnRouteHeadVO;
import nc.vo.ic.ic604.OnRouteVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.cquery.FldgroupVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.query.RefResultVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.sm.UserVO;

public class ClientUI extends IcBaseReport
  implements ItemListener, ICheckCondition
{
  private ReportBaseClass ivjReportBase = null;

  private QueryConditionDlg ivjQueryConditionDlg = null;

  private String m_sRNodeName = "40083006SYS";

  private String m_sPNodeCode = "40083006";

  private String m_sVOName = "nc.vo.ic.ic604.OnRouteBillVO";

  private String m_sHeaderVOName = "nc.vo.ic.ic604.OnRouteHeadVO";

  private String m_sItemVOName = "nc.vo.ic.ic604.OnRouteVO";

  private ArrayList m_alAllData = null;
  private OnRouteHeadVO m_voHead = null;

  private String m_sCorpID = null;
  private String m_sUserID = null;
  private String m_sLogDate = null;

  private ButtonObject m_boQuery = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000006"), NCLangRes.getInstance().getStrByID("common", "UC001-0000006"), 2, "查询");
  private ButtonObject m_boJointQuery = new ButtonObject(NCLangRes.getInstance().getStrByID("4008report", "UPPSCMCommon-000145"), NCLangRes.getInstance().getStrByID("4008report", "UPPSCMCommon-000145"), 2, "联查");

  private ButtonObject[] m_MainButtonGroup = { this.m_boQuery, this.m_boJointQuery };

  public ClientUI()
  {
    initialize();
  }

  private void adjustReportUI(boolean bisTotal)
  {
    if (bisTotal)
    {
      if (getReportBaseClass().getBodyItem("vsobillcode") != null) {
        getReportBaseClass().hideBodyTableCol("vsobillcode");
      }

      if (getReportBaseClass().getBodyItem("vpobillcode") != null) {
        getReportBaseClass().hideBodyTableCol("vpobillcode");
      }

      if (getReportBaseClass().getBodyItem("vtransbillcode") != null) {
        getReportBaseClass().hideBodyTableCol("vtransbillcode");
      }

      SCMEnv.out("Hide these column:vsobillcode,vpobillcode,vtransbillcode!");
    }
    else
    {
      if (getReportBaseClass().getBodyItem("vtransbillcode") != null) {
        getReportBaseClass().showBodyTableCol("vtransbillcode");
      }
      SCMEnv.out("Show these column:vsobillcode,vpobillcode,vtransbillcode!");
    }

    if (getReportBaseClass().getBodyItem("nprice") != null)
      getReportBaseClass().hideBodyTableCol("nprice");
    SCMEnv.out("Hide Price column!");
  }

  protected void getCEnvInfo()
  {
    try
    {
      ClientEnvironment ce = ClientEnvironment.getInstance();
      try {
        this.m_sUserID = ce.getUser().getPrimaryKey();
      } catch (Exception e) {
      }
      try {
        this.m_sCorpID = ce.getCorporation().getPrimaryKey();
        SCMEnv.out("---->corp id is " + this.m_sCorpID);
      }
      catch (Exception e) {
      }
      try {
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

  private QueryConditionDlg getConditionDlg() {
    if (this.ivjQueryConditionDlg == null) {
      this.ivjQueryConditionDlg = new QueryConditionDlg(this);

      this.ivjQueryConditionDlg.setTempletID(this.m_sCorpID, getPNodeCode(), this.m_sUserID, null);

      this.ivjQueryConditionDlg.setRefInitWhereClause("whname", "仓库档案", "gubflag='N' and pk_corp=", "pk_corp");

      this.ivjQueryConditionDlg.setRefInitWhereClause("instoreorg", "库存组织", " bd_calbody.pk_corp=", "inpk_corp");

      this.ivjQueryConditionDlg.setRefInitWhereClause("outstoreorg", "库存组织", " bd_calbody.pk_corp=", "outpk_corp");
      this.ivjQueryConditionDlg.setCorpRefs("inpk_corp", new String[] { "instoreorg" });
      this.ivjQueryConditionDlg.setCorpRefs("outpk_corp", new String[] { "outstoreorg" });
      this.ivjQueryConditionDlg.setCorpRefs("null", new String[] { "invcl" });

      ArrayList alCorpIDs = new ArrayList();
      try {
        alCorpIDs = ICReportHelper.queryCorpIDs(this.m_sUserID);
      } catch (Exception e) {
        SCMEnv.error(e);
      }
      this.ivjQueryConditionDlg.initCorpRef("outpk_corp", this.m_sCorpID, alCorpIDs);
      this.ivjQueryConditionDlg.hideNormal();
      this.ivjQueryConditionDlg.addICheckCondition(this);
    }
    return this.ivjQueryConditionDlg;
  }

  public String checkICCondition(ConditionVO[] voCons)
  {
    try
    {
      String sInstoreorg = "";
      String sOutstoreorg = "";
      String sChooseType = "";
      String sInCorp = "";
      String sOutCorp = "";
      for (int i = 0; i < voCons.length; i++) {
        if (voCons[i].getFieldCode().equals("instoreorg")) {
          sInstoreorg = voCons[i].getValue().trim();
        }
        else if (voCons[i].getFieldCode().equals("outstoreorg")) {
          sOutstoreorg = voCons[i].getValue().trim();
        }
        else if (voCons[i].getFieldCode().equals("queryoutonway")) {
          sChooseType = voCons[i].getValue().trim();
        }
        else
        {
          if (voCons[i].getFieldCode().equals("outpk_corp")) {
            sOutCorp = voCons[i].getRefResult().getRefPK().trim();
          }
          if (voCons[i].getFieldCode().equals("inpk_corp")) {
            sInCorp = voCons[i].getRefResult().getRefPK().trim();
          }
        }
      }
      if ((sChooseType.equals("F")) && (
        (sOutstoreorg == null) || (sOutstoreorg.trim().length() <= 0) || (sOutCorp == null) || (sOutCorp.trim().length() <= 0))) {
        return NCLangRes.getInstance().getStrByID("4008report", "UPP4008report-000024");
      }
      if ((sChooseType.equals("S")) && (
        (sInstoreorg == null) || (sInstoreorg.trim().length() <= 0) || (sInCorp == null) || (sInCorp.trim().length() <= 0))) {
        return NCLangRes.getInstance().getStrByID("4008report", "UPP4008report-000025");
      }
      return null; 
      } catch (Exception e) {
    	    return e.getMessage();
    }

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
    return biAddItem;
  }

  public String getTitle()
  {
    if (getReportBaseClass().getReportTitle() != null) {
      return getReportBaseClass().getReportTitle();
    }
    return NCLangRes.getInstance().getStrByID("4008report", "UPP4008report-000020");
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

    SCMEnv.out("Begin excute adjustReportUI");
    adjustReportUI(true);
    SCMEnv.out("end  excute adjustReportUI");

    ScaleInit si = new ScaleInit(this.m_sUserID, this.m_sCorpID);
    ArrayList alTemp = new ArrayList();

    alTemp.add(new String[] { "nordernum", "nonwaynum", "ntotalnum" });

    alTemp.add(null);

    alTemp.add(null);

    alTemp.add(new String[] { "nordermny", "nonwaymny", "ntotalmny" });

    alTemp.add(null);
    try {
      si.setScale(getReportBaseClass(), alTemp);
    } catch (Exception e) {
      if ((e instanceof BusinessException))
        showHintMessage(e.getMessage());
      else
        SCMEnv.error(e);
    }
  }

  public void initReportTemplet(String sNodeName)
  {
    try
    {
      getReportBaseClass().setTempletID(this.m_sCorpID, getPNodeCode(), this.m_sUserID, null);
    } catch (Exception e) {
      showErrorMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000019"));
      return;
    }
    BillData bd = getReportBaseClass().getBillData();
    if (bd == null) {
      SCMEnv.out("--> billdata null.");
      return;
    }
    getReportBaseClass().setBodyMenuShow(false);
  }

  public void itemStateChanged(ItemEvent e)
  {
  }

  public void onButtonClicked(ButtonObject bo)
  {
    if (bo == this.m_boQuery) {
      onQuery(true);
    }
    else if (bo == this.m_boJointQuery) {
      onJointQuery();
    }
    else
      super.onButtonClicked(bo);
  }

  void onJointQuery()
  {
    try
    {
      int iSelLine = getReportBaseClass().getBillTable().getSelectedRow();
      if (iSelLine < 0)
        return;
      if ((this.m_alAllData == null) || (this.m_alAllData.size() == 0))
        return;
      if ((iSelLine >= this.m_alAllData.size()) || (this.m_alAllData.get(iSelLine) == null)) {
        showHintMessage(NCLangRes.getInstance().getStrByID("4008report", "UPP4008report-000021"));
        return;
      }

      String sBillPK = (String)getReportBaseClass().getBillModel().getValueAt(iSelLine, "transgeneralhid");

      String sBillTypeCode = (String)getReportBaseClass().getBillModel().getValueAt(iSelLine, "transbilltypecode");

      if ((sBillPK == null) || (sBillPK.trim().length() == 0) || (sBillTypeCode == null) || (sBillTypeCode.trim().length() == 0))
      {
        showHintMessage(NCLangRes.getInstance().getStrByID("4008report", "UPP4008report-000022"));
        return;
      }

      SourceBillFlowDlg soureDlg = new SourceBillFlowDlg(this, sBillTypeCode, sBillPK, null, this.m_sUserID, this.m_sCorpID);

      soureDlg.showModal();
    }
    catch (Exception e)
    {
      SCMEnv.out("--------- 未捕捉到的异常 ---------");
      SCMEnv.error(e);
      showErrorMessage(NCLangRes.getInstance().getStrByID("4008report", "UPP4008report-000023") + e.getMessage());
    }
  }

  public void onQuery(boolean bQuery)
  {
    Object[][] arycombox1 = new Object[2][2];
    arycombox1[0][0] = "F";
    arycombox1[0][1] = NCLangRes.getInstance().getStrByID("40083006", "UPT40083006-000017");
    arycombox1[1][0] = "S";
    arycombox1[1][1] = NCLangRes.getInstance().getStrByID("4008report", "UPP4008report-000151");
    getConditionDlg().setCombox("queryoutonway", arycombox1);
    Object[][] arycombox = new Object[2][2];
    arycombox[0][0] = "N";
    arycombox[0][1] = NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000108");
    arycombox[1][0] = "Y";
    arycombox[1][1] = NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000244");
    getConditionDlg().setCombox("detailqueryflag", arycombox);
    UIRefPane uirefpane = new UIRefPane();
    uirefpane.setRefNodeName("存货档案");
    uirefpane.setWhereString(" bd_invbasdoc.discountflag='N' and bd_invbasdoc.laborflag='N' and bd_invmandoc.sealflag ='N' and bd_invmandoc.pk_corp='" + this.m_sCorpID + "'");

    UIRefCellEditor celleditor2 = new UIRefCellEditor(uirefpane);
    getConditionDlg().setValueRef("invcode", celleditor2);
    if ((bQuery) || (!this.m_bEverQry)) {
      getConditionDlg().showModal();
      this.m_bEverQry = true;
    } else {
      getConditionDlg().onButtonConfig();
    }

    if (!getConditionDlg().isCloseOK())
      return;
    setDlgSubTotal(null);
    getConditionDlg().checkCondition();
    try {
      ConditionVO[] voCons = getConditionDlg().getExpandVOs(getConditionDlg().getConditionVO());

      boolean bisTotalQuery = true;
      setReportHeadItem(voCons);

      if ((voCons != null) && (voCons.length > 0))
      {
        for (int i = 0; i < voCons.length; i++) {
          if ((!voCons[i].getFieldCode().equals("detailqueryflag")) || 
            (!voCons[i].getValue().trim().equals("Y"))) continue;
          bisTotalQuery = false;
        }

      }

      adjustReportUI(bisTotalQuery);

      setButtonStatus(bisTotalQuery);

      ArrayList alTranParam = new ArrayList();
      alTranParam.add(this.m_sCorpID);
      alTranParam.add(voCons);
      alTranParam.add(this.m_sLogDate);
      this.m_alAllData = OnRouteHelper.queryBorrowbill(alTranParam);
      SCMEnv.out(getConditionDlg().getWhereSQL());
      OnRouteVO[] aryonroutevos = null;

      if ((this.m_alAllData != null) && (this.m_alAllData.size() > 0)) {
        aryonroutevos = new OnRouteVO[this.m_alAllData.size()];
        this.m_alAllData.toArray(aryonroutevos);
      }

      setReportHeadItem(voCons);
      if ((aryonroutevos == null) || (aryonroutevos.length == 0)) {
        aryonroutevos = new OnRouteVO[] { new OnRouteVO() };
      }
      getReportBaseClass().setBodyDataVO(aryonroutevos);
      calculateTotal();
    } catch (Exception e) {
      SCMEnv.error(e);
      showErrorMessage(NCLangRes.getInstance().getStrByID("4008report", "UPP4008report-000004") + e.getMessage());
    }
  }

  public void procFieldGroupData(int iKeyIndex, Vector vAddReportItem, Vector vFieldGroup, String sTitle, String sSubTitle, String[] saColKey, String[] saColTitle, boolean bRetTopLevel, ArrayList alOtherParam)
  {
    if ((sTitle == null) || (saColTitle == null) || (saColTitle.length == 0) || (saColKey == null) || (saColKey.length != saColTitle.length) || (vAddReportItem == null) || (vFieldGroup == null))
    {
      SCMEnv.out("param null");
      return;
    }
    ReportItem riTemp = null;
    if (iKeyIndex >= 0)
      riTemp = getReportItem(saColKey[0] + iKeyIndex, saColTitle[0]);
    else
      riTemp = getReportItem(saColKey[0], saColTitle[0]);
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
        riTemp = getReportItem(saColKey[1] + iKeyIndex, saColTitle[1]);
      else
        riTemp = getReportItem(saColKey[1], saColTitle[1]);
      vAddReportItem.addElement(riTemp);

      voFg = new FldgroupVO();
      voFg.setGroupid(new Integer(0));
      voFg.setItem1("" + (vAddReportItem.size() - 1));
      voFg.setItem2("" + (vAddReportItem.size() - 2));
      voFg.setGrouptype("0");
      if ((iColNum == 2) && (bRetTopLevel) && (sTitle.equals(sSubTitle)))
        voFg.setToplevelflag("Y");
      else
        voFg.setToplevelflag("N");
      voFg.setGroupname(sSubTitle);

      vFieldGroup.addElement(voFg);
      if ((iColNum == 2) && (bRetTopLevel) && (sTitle.equals(sSubTitle))) {
        return;
      }
    }

    for (int col = 2; col < iColNum; col++) {
      if (iKeyIndex >= 0)
        riTemp = getReportItem(saColKey[col] + iKeyIndex, saColTitle[col]);
      else
        riTemp = getReportItem(saColKey[col], saColTitle[col]);
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
          vFieldGroup.removeElementAt(iSearch);
        voFg = new FldgroupVO();
        voFg.setGroupid(new Integer(0));
        if (bRetTopLevel)
          voFg.setToplevelflag("Y");
        else
          voFg.setToplevelflag("N");
        voFg.setItem1(sSubTitle);
        if (voTemporaryField.getItem1().equals(voTemporaryField.getItem2()))
          voFg.setItem2(voTemporaryField.getItem1());
        else
          voFg.setItem2(sTitle);
        voFg.setGrouptype("3");
        voFg.setGroupname(sTitle);
        vFieldGroup.addElement(voFg);
      }
    }
  }

  private void setButtonStatus(boolean isTotal)
  {
    if (!isTotal) {
      this.m_boJointQuery.setEnabled(true);
    }
    else {
      this.m_boJointQuery.setEnabled(false);
    }
    updateButton(this.m_boJointQuery);
  }

  void setReportHeadItem(ConditionVO[] voCons)
  {
    String sQueryCondition = getConditionDlg().getChText();
    getReportBaseClass().setHeadDataVO(this.m_voHead);
    getReportBaseClass().setMaxLenOfHeadItem("querycondition", 400);
    getReportBaseClass().setHeadItem("querycondition", sQueryCondition);
  }

  public ClientUI(FramePanel ff)
  {
    setFrame(ff);
    initialize();
  }

  public String getCorpID()
  {
    return this.m_sCorpID;
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

        this.ivjReportBase.setRowNOShow(true);
      }
      catch (Throwable ivjExc)
      {
      }

    }

    return this.ivjReportBase;
  }

  public AggregatedValueObject getReportVO()
  {
    OnRouteBillVO vo = (OnRouteBillVO)getReportBaseClass().getBillValueVO(this.m_sVOName, this.m_sHeaderVOName, this.m_sItemVOName);

    if (null == vo) {
      vo = new OnRouteBillVO();
    }
    if (null == vo.getParentVO()) {
      vo.setParentVO(new OnRouteHeadVO());
    }
    if (vo != null) {
      OnRouteVO[] voaItem = (OnRouteVO[])(OnRouteVO[])vo.getChildrenVO();
      if (voaItem != null) {
        for (int i = 0; i < voaItem.length; i++) {
          voaItem[i].setAttributeValue("outcorpname", voaItem[i].getAttributeValue("outcorpname"));
        }
      }
    }
    return vo;
  }

  public String getUserID()
  {
    return this.m_sUserID;
  }
}