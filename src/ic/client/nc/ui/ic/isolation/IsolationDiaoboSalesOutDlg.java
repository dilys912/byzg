package nc.ui.ic.isolation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import nc.ui.bd.languagetransformations.Transformations;
import nc.ui.ic.ic218.GeneralHHelper;
import nc.ui.ic.ic218.ModifyCorpDlg;
import nc.ui.ic.pub.bill.GeneralBillClientUI;
import nc.ui.ic.pub.bill.GeneralBillHelper;
import nc.ui.ic.pub.bill.GeneralBillUICtl;
import nc.ui.ic.pub.bill.initref.RefFilter;
import nc.ui.ic.pub.bill.query.QueryConditionDlgForBill;
import nc.ui.ic.pub.bill.uicontext.ICBusiCtlTools;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.change.PfChangeBO_Client;
import nc.ui.scm.pub.FactoryLoginDialog;
import nc.ui.scm.pub.bill.ButtonTree;
import nc.ui.scm.pub.redun.RedunSourceDlg;
import nc.ui.to.outer.SettlePathDlgForIC;
import nc.ui.to.service.ITOToIC_QryDLg;
import nc.vo.bd.CorpVO;
import nc.vo.ic.pub.BillTypeConst;
import nc.vo.ic.pub.GenMethod;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.bill.QryConditionVO;
import nc.vo.ic.pub.bill.Timer;
import nc.vo.ic.pub.smallbill.SMGeneralBillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.pub.FactoryVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.sm.UserVO;

public class IsolationDiaoboSalesOutDlg extends GeneralBillClientUI
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private FactoryLoginDialog m_DlgFactory;
  ITOToIC_QryDLg m_dlgTOQry = null;
  private String m_sCalbodyid;
  private String m_sErr = null;
  ModifyCorpDlg m_dlgModifyCorp;
  SettlePathDlgForIC m_dlgModifySettlePath;
  public int length=0;
  public Vector vcs=new Vector();
  public IsolationDiaoboSalesOutDlg()
  {
    initialize();
  }

  public IsolationDiaoboSalesOutDlg(String pk_corp, String billType, String businessType, String operator, String billID)
  {
    super(pk_corp, billType, businessType, operator, billID);
  }

  protected void afterBillEdit(BillEditEvent e)
  {
    if (e.getKey().equals("cotherwhid"))
    {
      String sName = ((UIRefPane)getBillCardPanel().getHeadItem("cotherwhid").getComponent()).getRefName();

      if (this.m_voBill != null)
        this.m_voBill.setHeaderValue("cotherwhname", sName);
    }
  }

  public void onAudit() {
    qryLocSN(this.m_iLastSelListHeadRow, 3);
    super.onAudit();
  }

  protected void afterBillItemSelChg(int iRow, int iCol)
  {
  }

  private void afterVoLoaded()
  {
    String[] keys = { "noutnum", "noutassistnum", "nshouldoutassistnum", "nshouldoutnum", "nmny" };

    if ((this.m_voBill != null) && (this.m_voBill.getHeaderVO() != null) && (this.m_voBill.getHeaderVO().getFreplenishflag().booleanValue()))
    {
      GeneralBillUICtl.setValueRange(getBillCardPanel(), keys, -10000000000000000.0D, 0.0D);
    }
    else { GeneralBillUICtl.setValueRange(getBillCardPanel(), keys, 0.0D, 1.7976931348623157E+308D);
    }

    if (getBillCardPanel().getHeadItem("alloctypename") != null)
      getBillCardPanel().getHeadItem("alloctypename").setEnabled(false);

    if (getBillCardPanel().getHeadItem("freplenishflag") != null) {
      getBillCardPanel().getHeadItem("freplenishflag").setEnabled(false);
    }

    calcSpace(this.m_voBill);
  }

  public boolean beforeBillItemEdit(BillEditEvent e)
  {
    if (e.getKey() == this.m_sMainCalbodyItemKey)
      getBillCardPanel().getHeadItem(this.m_sMainCalbodyItemKey).setEnabled(false);

    return false;
  }

  protected void beforeBillItemSelChg(int iRow, int iCol)
  {
  }

  public String checkPrerequisite()
  {
    return this.m_sErr;
  }

  protected boolean checkVO(GeneralBillVO voBill)
  {
    boolean bCheck;
    try
    {
      bCheck = true;
      bCheck = super.checkVO();

      return bCheck;
    }
    catch (Exception e) {
      showErrorMessage(e.getMessage()); }
    return false;
  }

  private ITOToIC_QryDLg createDlgQuery()
  {
    ITOToIC_QryDLg dlgQry = null;
    try
    {
      dlgQry = (ITOToIC_QryDLg)Class.forName("nc.ui.to.outer.QRYOrderDlg").newInstance();
    }
    catch (Exception e)
    {
    }

    return dlgQry;
  }

  public void filterRef(String sCorpID)
  {
    try
    {
      super.filterRef(sCorpID);

      String[] sConstraint = null;
      if (getCalbodyid() != null) {
        sConstraint = new String[1];
        sConstraint[0] = " AND pk_calbody='" + getCalbodyid() + "'";
      }
      BillItem bi = getBillCardPanel().getHeadItem(this.m_sMainWhItemKey);
      RefFilter.filtWh(bi, this.m_sCorpID, sConstraint);
    }
    catch (Exception e) {
      SCMEnv.error(e);
    }
  }

  public String getCalbodyid()
  {
    if ((this.m_sCalbodyid == null) && (isStartFromTO())) {
      FactoryVO vo = (FactoryVO)ClientEnvironment.getInstance().getValue("TO_CALBODY," + this.m_sCorpID + "," + this.m_sUserID);
      if (vo != null)
        this.m_sCalbodyid = vo.getPrimaryKey();
    }

    return this.m_sCalbodyid;
  }

  protected QueryConditionDlgForBill getConditionDlg()
  {
    String[] otherfieldcodes;
    UIRefPane ref;
    int i;
    if (this.ivjQueryConditionDlg == null)
    {
      this.ivjQueryConditionDlg = super.getConditionDlg();
      this.ivjQueryConditionDlg.setCombox("body.cfirsttype", new String[][] { { "", "" }, { "5C", NCLangRes.getInstance().getStrByID("40080618", "UPT40080618-000006") }, { "5D", NCLangRes.getInstance().getStrByID("40080618", "UPT40080618-000007") }, { "5E", NCLangRes.getInstance().getStrByID("40080618", "UPT40080618-000008") }, { "5I", NCLangRes.getInstance().getStrByID("40080618", "UPT40080618-000009") } });

      this.ivjQueryConditionDlg.setRefInitWhereClause("head.coutcalbodyid",Transformations.getLstrFromMuiStr("库存组织","Inventory Organization"), "pk_corp=", "head.coutcorpid");

      this.ivjQueryConditionDlg.setRefInitWhereClause("head.cothercalbodyid",Transformations.getLstrFromMuiStr("库存组织","Inventory Organization"), "pk_corp=", "head.cothercorpid");

      this.ivjQueryConditionDlg.setRefInitWhereClause("head.cotherwhid", Transformations.getLstrFromMuiStr("仓库档案","Warehouse file"), "bd_stordoc.pk_calbody=", "head.cothercalbodyid");

      this.ivjQueryConditionDlg.setCorpRefs("head.pk_corp", nc.ui.ic.pub.tools.GenMethod.getDataPowerFieldFromDlg(this.ivjQueryConditionDlg, false, new String[] { "head.cothercorpid", "head.coutcorpid", "body.creceieveid", "head.cothercalbodyid", "head.cotherwhid", "head.coutcalbodyid" }));

      this.ivjQueryConditionDlg.setCorpRefs("head.cothercorpid", new String[] { "head.cothercalbodyid", "head.cotherwhid", "body.creceieveid" });
      this.ivjQueryConditionDlg.setCorpRefs("head.coutcorpid", new String[] { "head.coutcalbodyid" });

      otherfieldcodes = new String[] { "body.creceieveid", "head.cothercalbodyid", "head.cotherwhid", "head.coutcalbodyid" };
      nc.ui.ic.pub.tools.GenMethod.setDataPowerFlag(this.ivjQueryConditionDlg, false, otherfieldcodes);
      ref = null;
      for (i = 0; i < otherfieldcodes.length; ++i) {
        ref = this.ivjQueryConditionDlg.getRefPaneByNodeCode(otherfieldcodes[i]);
        if ((ref != null) && (ref.getRefModel() != null))
          ref.getRefModel().setUseDataPower(false);
      }
    }
    return this.ivjQueryConditionDlg;
  }

  protected void getConDlginitself(QueryConditionDlgForBill queryCondition)
  {
    if (this.m_sCalbodyid != null)
      queryCondition.setRefInitWhereClause("head.pk_calbody", ClientEnvironment.getInstance().getLanguage().equalsIgnoreCase("zh-cn")?"库存组织":"Inventory Organization", "pk_calbody='" + this.m_sCalbodyid + "' and pk_corp=", "pk_corp");
  }

  private FactoryLoginDialog getDlgFactory()
  {
    if (this.m_DlgFactory == null)
      this.m_DlgFactory = new FactoryLoginDialog(this, NCLangRes.getInstance().getStrByID("4008busi", "UPP4008busi-000294"));
    return this.m_DlgFactory;
  }

  private ITOToIC_QryDLg getTODlgQry()
  {
    if (this.m_dlgTOQry == null)
      this.m_dlgTOQry = createDlgQuery();

    return this.m_dlgTOQry;
  }

  public String getExtendQryCond(ConditionVO[] voaCond)
  {
    String sFieldCode;
    String sField;
    int i;
    if (voaCond != null) {
      sFieldCode = null;
      sField = null;
      for (i = 0; i < voaCond.length; ++i) {
        if ((voaCond[i] != null) && (voaCond[i].getFieldCode() != null))
        {
          sFieldCode = voaCond[i].getFieldCode().trim();

          if (sFieldCode.startsWith("order.")) {
            sField = sFieldCode.substring(sFieldCode.indexOf(".") + 1);
            GenMethod.setCondIn(voaCond[i], " select cbillid from to_bill where dr=0 ", "body.cfirstbillhid", sField);
          }
          else if (sFieldCode.startsWith("orderbody.")) {
            sField = sFieldCode.substring(sFieldCode.indexOf(".") + 1);
            GenMethod.setCondIn(voaCond[i], " select cbill_bid from to_bill_b where dr=0 ", "body.cfirstbillbid", sField);
          }

        }

      }

    }

    if (this.m_sCalbodyid != null)
      return super.getExtendQryCond(voaCond) + " and pk_calbody ='" + this.m_sCalbodyid + "' ";

    return super.getExtendQryCond(voaCond);
  }

  protected ArrayList getFormulaItemHeader()
  {
    ArrayList arylistItemField = super.getFormulaItemHeader();

    arylistItemField.add(new String[] { "bodyname", "coutbodyname", "coutcalbodyid" });
    arylistItemField.add(new String[] { "bodyname", "cotherbodyname", "cothercalbodyid" });
    arylistItemField.add(new String[] { "unitname", "cotherunitname", "cothercorpid" });
    arylistItemField.add(new String[] { "unitname", "coutunitname", "coutcorpid" });

    return arylistItemField;
  }

  public String getTitle()
  {
    return this.m_sTitle;
  }

  public void initialize()
  {
    this.m_sCorpID = ClientEnvironment.getInstance().getCorporation().getPk_corp();
    this.m_sUserID = ClientEnvironment.getInstance().getUser().getPrimaryKey();

    if ((isStartFromTO()) && (getCalbodyid() == null))
    {
      getDlgFactory().showModal();
      if (getCalbodyid() == null)
      {
        this.m_sErr = NCLangRes.getInstance().getStrByID("4008busi", "UPP4008busi-000295");
        return;
      }
    }

    super.initialize();

    if ((getCalbodyid() != null) && (getBillCardPanel().getHeadItem(this.m_sMainCalbodyItemKey) != null)) {
      getBillCardPanel().getHeadItem(this.m_sMainCalbodyItemKey).setEnabled(false);
      getBillCardPanel().getHeadItem(this.m_sMainCalbodyItemKey).setEdit(false);
    }

    if (getBillCardPanel().getHeadItem("cotherwhid") != null) {
      UIRefPane ref = (UIRefPane)getBillCardPanel().getHeadItem("cotherwhid").getComponent();
      if (ref != null) {
        ref.getRefModel().setUseDataPower(false);
      }

    }

    String[] hidekey = { "nleftnum", "nleftastnum" };
    for (int i = 0; i < hidekey.length; ++i)
      try {
        getBillCardPanel().getBodyPanel().hideTableCol(hidekey[i]);
      } catch (Exception e) {
        SCMEnv.out(e.getMessage());
      }


    for (int i = 0; i < hidekey.length; ++i)
      try {
        getBillListPanel().getChildListPanel().hideTableCol(hidekey[i]);
      } catch (Exception e) {
        SCMEnv.out(e.getMessage());
      }
  }

  protected void initPanel()
  {
    super.setBillInOutFlag(-1);

    super.setNeedBillRef(false);

    this.m_sBillTypeCode = BillTypeConst.m_allocationOut;

    this.m_sCurrentBillNode = "40080820";
  }

  private boolean isStartFromTO()
  {
    return false;
  }

  private void onAdd5C()
  {
    onAddToOrder("40093010", "40099901", "5C", getTODlgQry());
  }

  private void onAdd5D()
  {
    onAddToOrder("40093020", "40099902", "5D", getTODlgQry());
  }

  private void onAdd5E()
  {
    onAddToOrder("40093030", "40099903", "5E", getTODlgQry());
  }

  private void onAdd5I()
  {
    onAddToOrder("40093031", "40099904", "5I", getTODlgQry());
  }

  private void onAddSelf()
  {
    onAdd();
  }

  private void onAddToOrder(String funnode, String qrynodekey, String sourcetype, ITOToIC_QryDLg dlgQry)
  {
    if (dlgQry == null) {
      showErrorMessage(NCLangRes.getInstance().getStrByID("4008busi", "UPP4008busi-000293"));
      return;
    }

    AggregatedValueObject[] vos = dlgQry.getReturnVOs(this.m_sCorpID, this.m_sUserID, sourcetype, this.m_sBillTypeCode, funnode, qrynodekey, this);

    if (vos == null)
      return;

    try
    {
      AggregatedValueObject[] voRetvos = (AggregatedValueObject[])PfChangeBO_Client.pfChangeBillToBillArray(vos, sourcetype, this.m_sBillTypeCode);

      setBillRefResultVO(null, voRetvos);
      afterVoLoaded();
    }
    catch (Exception e) {
      showErrorMessage(NCLangRes.getInstance().getStrByID("4008busi", "UPP4008busi-000297") + e.getMessage());
    }
  }

  public void onButtonClicked(ButtonObject bo)
  {
    if (bo == getButtonTree().getButton("自制")) {
      onAddSelf(); } else {
      AggregatedValueObject[] vos;
      if (bo == getButtonTree().getButton("三方调拨订单"))
      {
        bo.setTag("5C:0001AA100000000001ZC");
        RedunSourceDlg.childButtonClicked(bo, this.m_sCorpID, this.m_sCurrentBillNode, this.m_sUserID, this.m_sBillTypeCode, this);

        if (RedunSourceDlg.isCloseOK())
        {
          vos = RedunSourceDlg.getRetsVos();

          onAddToOrder(vos);
        }

      }
      else if (bo == getButtonTree().getButton("公司间调拨订单"))
      {
        bo.setTag("5D:0001AA100000000001ZC");
        RedunSourceDlg.childButtonClicked(bo, this.m_sCorpID, this.m_sCurrentBillNode, this.m_sUserID, this.m_sBillTypeCode, this);

        if (RedunSourceDlg.isCloseOK())
        {
          vos = RedunSourceDlg.getRetsVos();

          onAddToOrder(vos);
        }

      }
      else if (bo == getButtonTree().getButton("组织间调拨订单"))
      {
        bo.setTag("5E:0001AA100000000001ZC");
        RedunSourceDlg.childButtonClicked(bo, this.m_sCorpID, this.m_sCurrentBillNode, this.m_sUserID, this.m_sBillTypeCode, this);

        if (RedunSourceDlg.isCloseOK())
        {
          vos = RedunSourceDlg.getRetsVos();

          onAddToOrder(vos);
        }

      }
      else if (bo == getButtonTree().getButton("组织内调拨订单"))
      {
        bo.setTag("5I:0001AA100000000001ZC");
        RedunSourceDlg.childButtonClicked(bo, this.m_sCorpID, this.m_sCurrentBillNode, this.m_sUserID, this.m_sBillTypeCode, this);

        if (RedunSourceDlg.isCloseOK())
        {
          vos = RedunSourceDlg.getRetsVos();

          onAddToOrder(vos);
        }

      }
      else if (bo == getButtonTree().getButton("指定结算路径"))
      {
        onModifySettlePath(); } else {
        super.onButtonClicked(bo);
      }
    }
  }

  private void onModifyOutCorp()
  {
    ArrayList alSelected;
    try {
      alSelected = getSelectedBills();
      if ((alSelected == null) || (alSelected.size() == 0))
        return;
      if (this.m_dlgModifyCorp == null)
      {
        this.m_dlgModifyCorp = new ModifyCorpDlg(this.m_sCorpID);
      }

      this.m_dlgModifyCorp.showModal();
      if (this.m_dlgModifyCorp.getResult() != 1)
        return;
      ArrayList alparam = this.m_dlgModifyCorp.getResValue();
      ArrayList alErrorCode = new ArrayList();

      ArrayList alSameCorpBillCode = new ArrayList();

      String sAppointedCorp = alparam.get(0).toString();
      GeneralBillHeaderVO headVO = null;

      for (int i = 0; i < alSelected.size(); ++i)
      {
        GeneralBillVO rowVO = (GeneralBillVO)alSelected.get(i);
        headVO = rowVO.getHeaderVO();

        headVO.setCoperatoridnow(this.m_sUserID);
        headVO.setAttributeValue("clogdatenow", this.m_sLogDate);

        if ((headVO.getFallocflag().intValue() != 0) || ((headVO.getFbillflag().intValue() != 3) && (headVO.getFbillflag().intValue() != 4)))
        {
          alErrorCode.add(headVO.getVbillcode());
          alSelected.remove(i);
          i -= 1;
        }
        else if (sAppointedCorp.equals(headVO.getAttributeValue("cothercorpid"))) {
          alSameCorpBillCode.add(headVO.getVbillcode());
          alSelected.remove(i);
          i -= 1;
        }

      }

      ArrayList alResult = null;
      ArrayList alNOin = null;
      ArrayList alInvNotInOrg = null;

      if ((alSelected != null) && (alSelected.size() > 0))
      {
        GeneralBillVO[] selVOs = new GeneralBillVO[alSelected.size()];
        selVOs = (GeneralBillVO[])(GeneralBillVO[])alSelected.toArray(selVOs);

        alResult = GeneralHHelper.modifyOutCorp(selVOs, alparam);

        alNOin = (ArrayList)alResult.get(0);
        Object oFreshUIInfo = alResult.get(1);
        alInvNotInOrg = (ArrayList)alResult.get(2);

        if (oFreshUIInfo != null) {
          SMGeneralBillVO[] voResult = null;
          voResult = (SMGeneralBillVO[])(SMGeneralBillVO[])oFreshUIInfo;
          String[] keys = { "ts", "coutcorpid", "coutunitname", "coutcalbodyid", "coutbodyname" };
          refreshHeadValue(voResult, keys);
        }

      }

      StringBuffer sbHintMessage = new StringBuffer();
      if (alErrorCode.size() > 0)
      {
        sbHintMessage.append(NCLangRes.getInstance().getStrByID("4008busi", "UPP4008busi-000374")).append("\n").append(alErrorCode.toString());
      }

      if (alSameCorpBillCode.size() > 0)
      {
        sbHintMessage.append(NCLangRes.getInstance().getStrByID("4008busi", "UPP4008busi-000389")).append("\n").append(alSameCorpBillCode.toString());
      }

      if ((alNOin != null) && (alNOin.size() > 0)) {
        if (sbHintMessage.length() > 0)
          sbHintMessage.append("\n");
        sbHintMessage.append(NCLangRes.getInstance().getStrByID("4008busi", "UPP4008busi-000387")).append("\n").append(alNOin.toString());
      }

      if ((alInvNotInOrg != null) && (alInvNotInOrg.size() > 0)) {
        if (sbHintMessage.length() > 0)
          sbHintMessage.append("\n");
        sbHintMessage.append(NCLangRes.getInstance().getStrByID("4008busi", "UPP4008busi-000388")).append("\n").append(alInvNotInOrg.toString());
      }

      if (sbHintMessage.length() != 0)
        MessageDialog.showHintDlg(this, null, sbHintMessage.toString());
      else
        showHintMessage(NCLangRes.getInstance().getStrByID("4008busi", "UPP4008busi-000376"));

    }
    catch (Exception e)
    {
      SCMEnv.error(e);
      showHintMessage(e.getMessage());
    }
  }

  private void onModifySettlePath()
  {
    try
    {
      if (0 == this.m_iMode) {
        showHintMessage("请单据保存之后再指定结算！Then specify the settlement documents saved!");
        return;
      }

      ArrayList alSelected = getSelectedBills();
      if ((alSelected == null) || (alSelected.size() != 1)) {
        showHintMessage("请选择一张单据！Please select a document!");
        return;
      }

      int iselrow = getBillListPanel().getHeadTable().getSelectedRow();
      GeneralBillVO voBill = (GeneralBillVO)alSelected.get(0);

      if (voBill.getHeaderVO().getCgeneralhid() == null) {
        showHintMessage("请单据保存之后再指定结算！Then specify the settlement documents saved!");
        return;
      }

      GeneralBillItemVO[] voItems = voBill.getItemVOs();
      UFDouble ufd = null;

      for (int i = 0; i < voItems.length; ++i)
      {
        ufd = (UFDouble)voItems[i].getAttributeValue("nsettlenum1");

        if ((ufd != null) && (ufd.doubleValue() != 0.0D))
          throw new BusinessException("已经做过调入调出结算，不能再指定结算路径！Done has been transferred to tune out the settlement, can no longer be designated clearing path!");

        if (!("5D".equals(voItems[i].getCfirsttype())))
        {
          throw new BusinessException("只有公司间调拨定单可以指定结算路径！The only company to allocate orders designated clearing path!");
        }

      }

      SettlePathDlgForIC dlgModifySettlePath = new SettlePathDlgForIC((String)voBill.getHeaderVO().getAttributeValue("cothercorpid"), this.m_sCorpID, this, "指定结算路径");

      if (dlgModifySettlePath == null) {
        return;
      }

      dlgModifySettlePath.showModal();
      if (dlgModifySettlePath.getResult() != 1) {
        return;
      }

      String cpathid = dlgModifySettlePath.getSelectedSettlePathID();

      if (cpathid == null) {
        SCMEnv.out("路径空！");
        return;
      }
      voBill.getHeaderVO().setCoperatoridnow(this.m_sUserID);
      GeneralBillVO voRet = GeneralHHelper.modifySettlePath(voBill, cpathid);
      if ((voRet != null) && (voRet.getHeaderVO() != null)) {
        voRet.getHeaderVO().setAttributeValue("csettlepathid", cpathid);
        String[] keys = { "ts", "csettlepathid", "cpathname" };
        refreshHeadValue(new GeneralBillVO[] { voRet }, keys);
        selectListBill(iselrow);
        showHintMessage(NCLangRes.getInstance().getStrByID("4008busi", "UPP4008busi-000376"));
      }

    }
    catch (Exception e)
    {
      SCMEnv.error(e);
      showErrorMessage(e.getMessage());
    }
  }

  private void refreshHeadValue(AggregatedValueObject[] smallvos, String[] keys)
  {
    if (smallvos == null)
      return;
    HashMap htsmall = new HashMap();

    for (int i = 0; i < smallvos.length; ++i) {
      htsmall.put(smallvos[i].getParentVO().getAttributeValue("cgeneralhid"), smallvos[i]);
    }

    if (this.m_alListData != null) {
      for (int i = 0; i < this.m_alListData.size(); ++i) {
        AggregatedValueObject svo;
        int j;
        GeneralBillVO voBill = (GeneralBillVO)this.m_alListData.get(i);
        String hid = voBill.getHeaderVO().getCgeneralhid();
        if (htsmall.containsKey(hid)) {
          svo = (AggregatedValueObject)htsmall.get(hid);

          for (j = 0; j < keys.length; ++j) {
            voBill.getHeaderVO().setAttributeValue(keys[j], svo.getParentVO().getAttributeValue(keys[j]));
          }

          if ((this.m_voBill != null) && (this.m_voBill.getHeaderVO() != null) && (this.m_voBill.getHeaderVO().getCgeneralhid().equals(hid)))
          {
            getBillCardPanel().getHeadItem("ts").setValue(svo.getParentVO().getAttributeValue("ts"));
            for (j = 0; j < keys.length; ++j)
              this.m_voBill.getHeaderVO().setAttributeValue(keys[j], svo.getParentVO().getAttributeValue(keys[j]));

            for (j = 0; j < keys.length; ++j) {
              if (getBillCardPanel().getHeadItem(keys[j]) != null) {
                getBillCardPanel().getHeadItem(keys[j]).setValue(svo.getParentVO().getAttributeValue(keys[j]));
              }

            }

          }

        }

      }

      GeneralBillHeaderVO[] voh = new GeneralBillHeaderVO[this.m_alListData.size()];
      for (int i = 0; i < this.m_alListData.size(); ++i) {
        if (this.m_alListData.get(i) != null) {
          voh[i] = ((GeneralBillHeaderVO)((GeneralBillVO)this.m_alListData.get(i)).getParentVO());
        }

      }

      setListHeadData(voh);
    }
  }

  public void onUpdate()
  {
    super.onUpdate();
    afterVoLoaded();
  }

  protected void selectBillOnListPanel(int iBillIndex)
  {
  }

  protected void setButtonsStatus(int iBillMode)
  {
  }

  public void setCalbodyid(String newCalbodyid)
  {
    this.m_sCalbodyid = newCalbodyid;
  }

  protected void setNewBillInitData()
  {
    super.setNewBillInitData();
    try
    {
      if ((getBillCardPanel().getHeadItem(this.m_sMainCalbodyItemKey) != null) && (getCalbodyid() != null)) {
        getBillCardPanel().setHeadItem(this.m_sMainCalbodyItemKey, getCalbodyid());
      }

    }
    catch (Exception e)
    {
      SCMEnv.error(e);
    }
  }

  protected String freshStatusTs(String sBillPK)
    throws Exception
  {
    String sBillStatus = null;

    String sQryWhere = " head.cgeneralhid ='" + this.m_voBill.getPrimaryKey() + "'  ";
    QryConditionVO voQryCond = new QryConditionVO(sQryWhere);
    voQryCond.setIntParam(0, 500);

    GeneralBillVO voRetBill = (GeneralBillVO)GeneralBillHelper.queryBills(this.m_sBillTypeCode, voQryCond).get(0);

    this.m_alListData.remove(this.m_iLastSelListHeadRow);
    this.m_alListData.add(this.m_iLastSelListHeadRow, voRetBill);
    this.m_voBill = ((GeneralBillVO)this.m_alListData.get(this.m_iLastSelListHeadRow));

    ArrayList altemp = new ArrayList();
    altemp.add(this.m_voBill);
    setAlistDataByFormula(1, altemp);

    setBillVO(this.m_voBill);

    GeneralBillHeaderVO[] voaHeader = new GeneralBillHeaderVO[this.m_alListData.size()];
    for (int i = 0; i < this.m_alListData.size(); ++i)
      if (this.m_alListData.get(i) != null) {
        voaHeader[i] = ((GeneralBillHeaderVO)((GeneralBillVO)this.m_alListData.get(i)).getParentVO());
        if ((voaHeader[i].getCgeneralhid() != null) && (voaHeader[i].getCgeneralhid().equals(sBillPK)) && (voaHeader[i].getFbillflag() != null))
          sBillStatus = voaHeader[i].getFbillflag().toString();
      }
      else {
        SCMEnv.out("list data error!-->" + i);
        sBillStatus = "1";
      }


    setListHeadData(voaHeader);
    selectListBill(this.m_iLastSelListHeadRow);

    return sBillStatus;
  }

  private void onAddToOrder(AggregatedValueObject[] vos)
  {
    if (vos == null)
      return;

    try
    {
      if ((vos != null) && (vos.length == 1)) {
        setRefBillsFlag(false);
        setBillRefResultVO(null, vos);
      } else {
        setRefBillsFlag(true);
        setBillRefMultiVOs(null, (GeneralBillVO[])(GeneralBillVO[])vos);
      }
    }
    catch (Exception e)
    {
      showErrorMessage(NCLangRes.getInstance().getStrByID("4008busi", "UPP4008busi-000297") + e.getMessage());
    }
  }

  public void setBillVO(GeneralBillVO bvo, boolean bUpdateBotton, boolean bExeFormule)
  {
    super.setBillVO(bvo, bUpdateBotton, bExeFormule);
    if (ICBusiCtlTools.isStringNull(bvo.getHeaderVO().getCgeneralhid()))
      afterVoLoaded();
  }
  @Override
	public void onQuery() {
		// TODO Auto-generated method stub
	  Timer timer;
	   
      timer = new Timer();
      this.m_sBnoutnumnull = null;

      timer.start(NCLangRes.getInstance().getStrByID("4008busi", "UPP4008busi-000277"));

      int cardOrList = this.m_iCurPanel;

      if ((this.m_bQuery) || (!(this.m_bEverQry))) {
        getConditionDlg().showModal();
        timer.showExecuteTime("@@getConditionDlg().showModal()：");

        if (getConditionDlg().getResult() != 1)
        {
          return ;
        }

        this.m_bEverQry = true;

        setButtonStatus(true);
      }

      QryConditionVO voCond = getQryConditionVO();

      this.m_voLastQryCond = voCond;

      ConditionVO[] voaCond = getConditionDlg().getConditionVO();

      voCond.setParam(1, voaCond);

      voCond.setIntParam(0, 300);
      if (this.m_sBnoutnumnull != null)
      {
        voCond.setParam(33, this.m_sBnoutnumnull);
      }

      showHintMessage(NCLangRes.getInstance().getStrByID("4008busi", "UPP4008busi-000250"));

      timer.showExecuteTime("Before 查询：：");
      ArrayList alListData = null;
	try {
		alListData = GeneralBillHelper.queryBills(this.m_sBillTypeCode, voCond);
	} catch (Exception e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
      timer.showExecuteTime("查询时间：");
      try
      {
        setAlistDataByFormula(20, alListData);

        timer.showExecuteTime("@@setAlistDataByFormula公式解析时间：");
        SCMEnv.out("0存货公式解析成功！");
      }
      catch (Exception e) {
      }
      execExtendFormula(alListData);
      if ((alListData != null) && (alListData.size() > 0)) {
        this.m_alListData = alListData;
        //弹出展示界面
        ShowSalesOutResult write=new ShowSalesOutResult(this, alListData);
        String result[]=null;
        if (write.showModal()==2) {
			//newaccount=wriui.getUITextField1().getText();//得到新账号
        	result=write.result;
		}
        if(result==null || result.length<1 ){
        	return;
        }
        length=result.length;
        vcs.clear();
        try
        {
        	GeneralBillItemVO item = null;
        	GeneralBillHeaderVO header = null;
        for (int a = 0; a < result.length; a++) {
			for (int i = 0; i < alListData.size(); i++) { 
				GeneralBillVO vo=(GeneralBillVO) alListData.get(i);
				header = vo.getHeaderVO();
				for (int j = 0; j < vo.getChildrenVO().length; j++) {
					item = (GeneralBillItemVO) vo.getChildrenVO()[j];
					Vector vc=new Vector();
//					if(result[a].equals(vo.getChildrenVO()[j].getAttributeValue("vbatchcode").toString())){
					if(result[a].equals(vo.getChildrenVO()[j].getPrimaryKey())){
						vc.addElement(vo.getChildrenVO()[j].getAttributeValue("cinventorycode")==null?"":vo.getChildrenVO()[j].getAttributeValue("cinventorycode").toString());//料号
						vc.addElement(vo.getChildrenVO()[j].getAttributeValue("vbatchcode")==null?"":vo.getChildrenVO()[j].getAttributeValue("vbatchcode").toString());//批次号
						vc.addElement(vo.getChildrenVO()[j].getAttributeValue("invname")==null?"":vo.getChildrenVO()[j].getAttributeValue("invname").toString());//产品
					//	vc.addElement(vo.getChildrenVO()[j].getAttributeValue("pk_batchcode").toString());//货位
						vc.addElement(vo.getHeaderVO().getAttributeValue("cwarehouseid")==null?"":vo.getHeaderVO().getAttributeValue("cwarehouseid").toString());//仓库
						vc.addElement(vo.getChildrenVO()[j].getAttributeValue("cinventoryid")==null?"":vo.getChildrenVO()[j].getAttributeValue("cinventoryid").toString());//产品id
						if(item.getNoutnum()!=null)
						{
							vc.add(item.getNoutnum().intValue()); //数量
						}else
						{
							vc.add(null);
						}
						vc.add(item.getVfree0()); //垛号
						if(item.getLocator()!=null&&item.getLocator().length>0)
						{
							vc.add(item.getLocator()[0].getVspacecode());  //货位编码
						}else
						{
							vc.add(null);
						}
						vcs.add(vc); 
					}
				}
				
			}
        }
        }catch(Exception ex)
        {
        	MessageDialog.showErrorDlg(this, "错误Error", "错误Error:"+ex.getMessage());
        	ex.printStackTrace();
        	vcs.clear();
        }
      }
      
    }
}