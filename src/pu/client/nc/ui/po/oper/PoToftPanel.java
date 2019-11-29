package nc.ui.po.oper;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JFileChooser;

import nc.bs.framework.common.NCLocator;
import nc.bs.monitor.utils.FileUtils;
import nc.itf.uap.pfxx.IPFxxEJBService;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.itf.uap.sf.ICreateCorpQueryService;
import nc.ui.bd.b09.CustInfoUI;
import nc.ui.bd.b16.InvManCardPanel;
import nc.ui.ml.NCLangRes;
import nc.ui.pi.InvoiceHelper;
import nc.ui.pi.invoice.InvoiceUIQueDlg;
import nc.ui.po.OrderHelper;
import nc.ui.po.prepay.PoToPrePayUI;
import nc.ui.po.pub.ContractClassParse;
import nc.ui.po.pub.GrossProfitUI;
import nc.ui.po.pub.IPoCardPanel;
import nc.ui.po.pub.IPoListPanel;
import nc.ui.po.pub.InvAttrCellRenderer;
import nc.ui.po.pub.OrderPrintData;
import nc.ui.po.pub.PoCardPanel;
import nc.ui.po.pub.PoChangeUI;
import nc.ui.po.pub.PoListPanel;
import nc.ui.po.pub.PoLoadDataTool;
import nc.ui.po.pub.PoPublicUIClass;
import nc.ui.po.pub.PoQueryCondition;
import nc.ui.po.pub.PoVOBufferManager;
import nc.ui.po.rp.PoReceivePlanUI;
import nc.ui.pu.pub.ATPForOneInvMulCorpUI;
import nc.ui.pu.pub.POPubSetUI2;
import nc.ui.pu.pub.PuTool;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.FramePanel;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIMenuItem;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillMouseEnent;
import nc.ui.pub.bill.BillScrollPane;
import nc.ui.pub.bill.BillTableMouseListener;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.pub.workflownote.FlowStateDlg;
import nc.ui.scm.file.DocumentManager;
import nc.ui.scm.ic.setpart.SetPartDlg;
import nc.ui.scm.pub.BusiBillManageTool;
import nc.ui.scm.pub.CollectSettingDlg;
import nc.ui.scm.pub.bill.ButtonTree;
import nc.ui.scm.pub.bill.IBillExtendFun;
import nc.ui.scm.pub.def.DefSetTool;
import nc.ui.scm.pub.print.ISetBillVO;
import nc.ui.scm.pub.print.ScmPrintTool;
import nc.ui.scm.pub.report.AvgSaleQueryUI;
import nc.ui.scm.sourcebill.SourceBillFlowDlg;
import nc.vo.bd.CorpVO;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.pi.InvoiceHeaderVO;
import nc.vo.pi.InvoiceItemVO;
import nc.vo.pi.InvoiceVO;
import nc.vo.pi.NormalCondVO;
import nc.vo.po.OrderHeaderVO;
import nc.vo.po.OrderItemVO;
import nc.vo.po.OrderVO;
import nc.vo.po.pub.OrderPubVO;
import nc.vo.pu.exception.MaxPriceException;
import nc.vo.pu.exception.MaxStockException;
import nc.vo.pu.exception.RwtPoToPrException;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.datapower.BtnPowerVO;
import nc.vo.scm.pu.BillStatus;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pu.Timer;
import nc.vo.scm.pub.CustomerConfigVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.SaveHintException;
import nc.vo.scm.pub.StockPresentException;
import nc.vo.scm.pub.report.AvgSaleQueryVO;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.sm.UserVO;
import org.w3c.dom.Document;

public abstract class PoToftPanel extends ToftPanel
  implements BillTableMouseListener, IPoCardPanel, IPoListPanel, ISetBillVO
{

  protected static final int PoCardPanel = 100;//yqq
  protected static final int PoListPanel = 101;//yqq
  protected int m_iCurPanel = PoCardPanel;  //yqq  当前显示的panel
  protected int m_iCurPanel1 = PoListPanel;  //yqq  当前显示的panel
  private PoCardPanel m_cpBill = null;

  private UFBoolean m_ufbIsDmEnabled = null;

  private POPubSetUI2 m_objSetUI2 = null;

  private ATPForOneInvMulCorpUI m_atpDlg = null;

  private boolean m_bEverQueryed = false;

  private boolean m_bFromOtherBill = false;

  private PoVOBufferManager m_bufPoVO = null;

  private PoVOBufferManager m_bufPoVOFrmBill = null;

  private String m_cbiztype = null;

  private String m_cupsourcebilltype = null;

  private CustInfoUI m_dlgCustInfo = null;

  private SetPartDlg m_dlgInvSetQuery = null;

  private PoLocateDlg m_dlgLocate = null;

  private AvgSaleQueryUI m_dlgSaleNum = null;

  private HashMap m_hmapUpSourcePrayTs = null;

  private int m_iOrgBufferLen = 0;

  private PoListPanel m_listBill = null;

  private int m_nCurOperState = 0;

  private int m_nflagSave = 0;

  private String m_pk_corp = null;

  private GrossProfitUI m_pnlGross = null;

  private InvManCardPanel m_pnlInvInfo = null;

  private ScmPrintTool m_printList = null;

  protected PoToftPanelButton m_btnManager = null;

  private String[] saPkCorp = null;
  protected static final int STATE_BILL_BROWSE = 0;
  protected static final int STATE_BILL_EDIT = 1;
  protected static final int STATE_BILL_GROSS_EVALUATE = 3;
  protected static final int STATE_LIST_BROWSE = 2;


  public PoToftPanel()
  {
    init();
  }

  public abstract String getTitle();

  private void init()
  {
    this.m_objSetUI2 = new POPubSetUI2();

    setCurPk_corp(PoPublicUIClass.getLoginPk_corp());

    setLayout(new BorderLayout());
    if (isInitStateBill()) {
      add(getPoCardPanel(), "Center");
      setButtons(getBtnManager().getBtnaBill(this));

      setCurOperState(0);
      setButtonsStateInit();
    } else {
      add(getPoListPanel(), "Center");
      setButtons(getBtnManager().getBtnaList(this));

      setCurOperState(2);
      setButtonsStateList();
    }
  }

  public void onButtonClicked(ButtonObject bo)
  {
    if (getCurOperState() == 2)
      onButtonClickedList(bo);
    else
      onButtonClickedBill(bo);
  }

  public PoToftPanel(FramePanel panel)
  {
    setFrame(panel);
    init();
  }

  protected void displayCurVOEntirelyReset(boolean bEntirelyReset)
  {
    if (getBufferLength() == 0)
    {
      getPoCardPanel().getBillModel().clearBodyData();

      getPoCardPanel().addNew();
      setButtonsStateBrowse();
      return;
    }

    OrderVO voCur = getOrderDataVOAt(getBufferVOManager().getVOPos());
    if (voCur.getBodyVO() == null)
    {
      getBufferVOManager().setVOPos(getBufferVOManager().getPreviousVOPos());
      return;
    }

    if (bEntirelyReset)
      getPoCardPanel().displayCurVO(voCur, isFromOtherBill());
    else
      getPoCardPanel().displayCurVOAfterJustSave(voCur);
  }

  protected int getBufferLength()
  {
    return getBufferVOManager().getLength();
  }

  private PoVOBufferManager getBufferVOFrmBill()
  {
    if (this.m_bufPoVOFrmBill == null) {
      this.m_bufPoVOFrmBill = new PoVOBufferManager();
    }

    return this.m_bufPoVOFrmBill;
  }

  private ButtonObject[] getButtonsGrossProfit()
  {
    return getGrossProfitPanel().getButtonObjectArray();
  }

  private String getCupsourcebilltype()
  {
    return this.m_cupsourcebilltype;
  }

  private String getCurBizeType()
  {
    return this.m_cbiztype;
  }

  protected int getCurOperState()
  {
    return this.m_nCurOperState;
  }

  protected String getCurPk_corp()
  {
    return this.m_pk_corp;
  }

  private PoLocateDlg getDlgLocate()
  {
    if (this.m_dlgLocate == null) {
      this.m_dlgLocate = new PoLocateDlg(this);
    }
    return this.m_dlgLocate;
  }

  private AvgSaleQueryUI getDlgSaleNum()
  {
    if (this.m_dlgSaleNum == null) {
      this.m_dlgSaleNum = new AvgSaleQueryUI(this);
      this.m_dlgSaleNum.setTxtDayText(new Integer(7));
    }
    return this.m_dlgSaleNum;
  }

  private GrossProfitUI getGrossProfitPanel()
  {
    if (this.m_pnlGross == null) {
      this.m_pnlGross = new GrossProfitUI(getCurPk_corp(), getPoCardPanel().getPoPubSetUi2());
    }
    return this.m_pnlGross;
  }

  private HashMap getHtUpSourcePrayTs()
  {
    if (this.m_hmapUpSourcePrayTs == null) {
      this.m_hmapUpSourcePrayTs = new HashMap();
    }
    return this.m_hmapUpSourcePrayTs;
  }

  protected abstract PoCardPanel getInitPoCardPanel(POPubSetUI2 paramPOPubSetUI2);

  protected abstract PoListPanel getInitPoListPanel(POPubSetUI2 paramPOPubSetUI2);

  public int getOperState()
  {
    if (getCurOperState() == 1) {
      return 0;
    }
    return 1;
  }

  protected OrderVO getOrderDataVOAt(int iPos)
  {
    if (getBufferLength() == 0) {
      return null;
    }
    OrderVO voCur = getBufferVOManager().getVOAt_LoadItemYes(this, iPos);

    if (isFromOtherBill()) {
      PoChangeUI.loadVOBodyWhenConvert(this, voCur, getCupsourcebilltype());
    }

    return voCur;
  }

  public OrderHeaderVO[] getOrderViewHVOs()
  {
    if (getBufferLength() == 0) {
      return null;
    }

    OrderHeaderVO[] hvo = new OrderHeaderVO[getBufferLength()];
    for (int i = 0; i < getBufferLength(); i++) {
      hvo[i] = getBufferVOManager().getHeadVOAt(i);
    }
    return hvo;
  }

  public OrderVO getOrderViewVOAt(int iPos)
  {
    return getOrderDataVOAt(iPos);
  }

  private int getOrgBufferLen()
  {
    return this.m_iOrgBufferLen;
  }

  protected PoCardPanel getPoCardPanel()
  {
    if (this.m_cpBill == null)
    {
      this.m_cpBill = getInitPoCardPanel(this.m_objSetUI2);
    }
    return this.m_cpBill;
  }

  protected PoListPanel getPoListPanel()
  {
    if (this.m_listBill == null)
    {
      this.m_listBill = getInitPoListPanel(this.m_objSetUI2);

      this.m_listBill.addMouseListener(this);

      this.m_listBill.getHeadBillModel().addSortRelaObjectListener(getBufferVOManager());
    }
    return this.m_listBill;
  }

  protected abstract PoQueryCondition getPoQueryCondition();

  private ArrayList getPrintAryList()
  {
    OrderVO[] voaSelected = getSelectedVOs();
    if (voaSelected == null) {
      return null;
    }

    int iLen = voaSelected.length;
    ArrayList arylistPrint = new ArrayList();
    for (int i = 0; i < iLen; i++) {
      arylistPrint.add(voaSelected[i]);
    }

    return arylistPrint;
  }

  private ScmPrintTool getPrintTool()
  {
    if (this.m_printList == null) {
      this.m_printList = new ScmPrintTool(this, getPoCardPanel(), getModuleCode());
    }

    return this.m_printList;
  }

  private OrderVO getSaveVO()
  {
    if (!getPoCardPanel().getBillData().execValidateFormulas()) {
      return null;
    }

    OrderVO voSaved = getPoCardPanel().getSaveVO(getOrderDataVOAt(getBufferVOManager().getVOPos()));

    if (voSaved == null) {
      showHintMessage(NCLangRes.getInstance().getStrByID("4004020601", "UPP4004020601-000073"));
      return null;
    }

    if ((isFromOtherBill()) && (getCupsourcebilltype().equals("20"))) {
      OrderItemVO[] voSavedItem = voSaved.getBodyVO();
      int iBodyLen = voSavedItem.length;
      for (int i = 0; i < iBodyLen; i++) {
        String sType = voSavedItem[i].getCupsourcebilltype();
        if (sType != null) {
          String sHId = voSavedItem[i].getCupsourcebillid();
          if ((sHId != null) && (sHId.trim().length() > 0) && (getHtUpSourcePrayTs().containsKey(sHId)))
          {
            voSavedItem[i].setCupsourcehts((String)getHtUpSourcePrayTs().get(sHId));
          }
          String sBId = voSavedItem[i].getCupsourcebillrowid();
          if ((sBId != null) && (sBId.trim().length() > 0) && (getHtUpSourcePrayTs().containsKey(sBId)))
          {
            voSavedItem[i].setCupsourcebts((String)getHtUpSourcePrayTs().get(sBId));
          }
        }
      }
    }

    return voSaved;
  }

  private OrderVO[] getSelectedVOs()
  {
    Vector vecSelectedVO = new Vector();

    int[] iaSelectedRowCount = getPoListPanel().getHeadSelectedRows();
    int iLen = iaSelectedRowCount.length;
    int iVOPos = 0;
    for (int i = 0; i < iLen; i++) {
      iVOPos = getPoListPanel().getVOPos(iaSelectedRowCount[i]);
      vecSelectedVO.add(getBufferVOManager().getVOAt_LoadItemNo(iVOPos));
    }

    OrderVO[] voaRet = new OrderVO[iLen];
    vecSelectedVO.toArray(voaRet);
    boolean bSuccess = PoLoadDataTool.loadItemsForOrderVOs(this, voaRet);
    if (!bSuccess) {
      return null;
    }

    return voaRet;
  }

  private boolean isEverQueryed()
  {
    return this.m_bEverQueryed;
  }

  private boolean isFromOtherBill()
  {
    return this.m_bFromOtherBill;
  }

  public void mouse_doubleclick(BillMouseEnent e)
  {
    if (e.getPos() == 0) {
      onDoubleClick(e.getRow());
      if (isFromOtherBill()) {
        onBillModify();
        showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000041"));
      } else {
        setButtonsStateBrowse();
        showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000042"));
      }
    }
  }

  private void onBillAppendLine()
  {
    showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000043"));
    getPoCardPanel().onActionAppendLine();
    setButtonsStateEdit();
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH036"));
  }

  private void onBillApQuery()
  {
    String sVendMangId = getPoCardPanel().getHeadItem("cvendormangid").getValue();
    if ((sVendMangId == null) || (sVendMangId.trim().length() == 0)) {
      return;
    }

    ApQueryDlg apDlg = new ApQueryDlg(this, getCurPk_corp(), sVendMangId, getPoCardPanel().getPoPubSetUi2());

    apDlg.showModal();
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000038"));
  }

  private void onBillCancel()
  {
    setCurOperState(0);

    if (isFromOtherBill()) {
      getBufferVOManager().removeAt(getBufferVOManager().getVOPos());

      if (getBufferLength() > 0) {
        onBillList();
      }
      else {
        getBufferVOManager().resetVOs(getBufferVOFrmBill());
        getBufferVOManager().setVOPos(getBufferVOManager().getLength() - 1);

        getPoCardPanel().setEnabled(false);

        displayCurVOEntirelyReset(true);

        setIsFromOtherBill(false);

        setButtonsStateBrowse();
      }
    }
    else {
      getPoCardPanel().setEnabled(false);

      displayCurVOEntirelyReset(true);

      setButtonsStateBrowse();
    }

    if (!(this instanceof RevisionUI)) {
      InvAttrCellRenderer ficr = new InvAttrCellRenderer();
      ficr.setFreeItemRenderer(getPoCardPanel(), null);
    }

    showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH008"));
  }

  private void onBillCombin()
  {
    CollectSettingDlg dlg = new CollectSettingDlg(this, NCLangRes.getInstance().getStrByID("4004020201", "UPT4004020201-000084"));

    dlg.initData(getPoCardPanel(), new String[] { "cinventorycode", "cinventoryname", "cspecification", "ctype", "prodarea", "ccurrencytype" }, null, new String[] { "noriginalcurmny", "noriginaltaxmny", "noriginaltaxpricemny", "nordernum" }, null, new String[] { "noriginalcurprice", "noriginalnetprice" }, "nordernum");

    dlg.showModal();
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000039"));
  }

  private void onBillContractClass()
  {
    ContractClassParse.showDlg(this, getParameter(ContractClassParse.getParaName()), getOrderDataVOAt(getBufferVOManager().getVOPos()));

    showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000040"));
  }

  private void onBillCopyBill()
  {
    setCurOperState(1);

    getPoCardPanel().onActionCopyBill(getOrderDataVOAt(getBufferVOManager().getVOPos()));

    setButtonsStateEdit();

    getPoCardPanel().transferFocusTo(0);

    if (!(this instanceof RevisionUI)) {
      InvAttrCellRenderer ficr = new InvAttrCellRenderer();
      ficr.setFreeItemRenderer(getPoCardPanel(), getOrderDataVOAt(getBufferVOManager().getVOPos()));
    }
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH029"));
  }

  private void onBillCopyLine()
  {
    showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000044"));
    if (getPoCardPanel().getRowCount() > 0) {
      if (getPoCardPanel().getBillTable().getSelectedRowCount() <= 0) {
        showWarningMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000045"));
        return;
      }

      getPoCardPanel().copyLine();
    }

    setButtonsStateEdit();

    showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH039"));
  }

  private void onBillDeleteLine()
  {
    showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000046"));

    getPoCardPanel().onActionDeleteLine();
    setButtonsStateEdit();
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH037"));
  }

  private void onBillDiscard()
  {
    OrderVO voCanceled = getOrderDataVOAt(getBufferVOManager().getVOPos());

    boolean bSuccessed = onDiscard(this, new OrderVO[] { voCanceled });
    if (!bSuccessed) {
      return;
    }

    getBufferVOManager().removeAt(getBufferVOManager().getVOPos());
    displayCurVOEntirelyReset(true);

    setButtonsStateBrowse();

    showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH006"));
  }

  private void onBillDocManage()
  {
    OrderVO voCur = getOrderDataVOAt(getBufferVOManager().getVOPos());

    HashMap mapBtnPowerVo = new HashMap();
    Integer iBillStatus = null;
    BtnPowerVO pVo = new BtnPowerVO(voCur.getHeadVO().getVordercode());
    iBillStatus = PuPubVO.getInteger_NullAs(voCur.getHeadVO().getForderstatus(), new Integer(0));
    if ((iBillStatus.intValue() == 2) || (iBillStatus.intValue() == 3) || (iBillStatus.intValue() == 5))
    {
      pVo.setFileDelEnable("false");
    }
    mapBtnPowerVo.put(voCur.getHeadVO().getVordercode(), pVo);
    DocumentManager.showDM(this, new String[] { voCur.getHeadVO().getCorderid() }, new String[] { voCur.getHeadVO().getVordercode() }, mapBtnPowerVo);

    showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000025"));
  }

  private void onBillExecAudit()
  {
    Timer timeDebug = new Timer();
    timeDebug.start();

    timeDebug.addExecutePhase("设置VO");

    OrderHeaderVO headVO = getOrderDataVOAt(getBufferVOManager().getVOPos()).getHeadVO();
    UFDate dateAuditOld = headVO.getDauditdate();
    String strPsnOld = headVO.getCauditpsn();

    PoPublicUIClass.setCuserId(new OrderVO[] { getOrderDataVOAt(getBufferVOManager().getVOPos()) });
    PoPublicUIClass.setCauditpsn(new OrderVO[] { getOrderDataVOAt(getBufferVOManager().getVOPos()) });

    OrderVO voNewOrder = null;
    try {
      Object[] objs = new Object[1];
      objs[0] = new ClientLink(getClientEnvironment());
      String strErr = PuTool.getAuditLessThanMakeMsg(new OrderVO[] { getOrderDataVOAt(getBufferVOManager().getVOPos()) }, "dorderdate", "vordercode", ((ClientLink)objs[0]).getLogonDate(), "21");
      if (strErr != null) {
        throw new BusinessException(strErr);
      }
      PfUtilClient.processBatchFlow(this, "APPROVE", "21", getClientEnvironment().getDate().toString().trim(), new OrderVO[] { getOrderDataVOAt(getBufferVOManager().getVOPos()) }, objs);

      if (!PfUtilClient.isSuccess())
      {
        headVO.setDauditdate(dateAuditOld);
        headVO.setCauditpsn(strPsnOld);
        return;
      }
      timeDebug.addExecutePhase("审批");

      voNewOrder = OrderHelper.queryOrderVOLight(getOrderDataVOAt(getBufferVOManager().getVOPos()).getHeadVO().getCorderid(), "AUDIT");
      timeDebug.addExecutePhase("重新查询(轻量VO)");
    }
    catch (Exception e)
    {
      headVO.setDauditdate(dateAuditOld);
      headVO.setCauditpsn(strPsnOld);
      PuTool.outException(this, e);
      return;
    }

    if (voNewOrder == null) {
      showHintMessage(PuPubVO.MESSAGE_CONCURRENT);
      return;
    }

    getBufferVOManager().getVOAt_LoadItemNo(getBufferVOManager().getVOPos()).getHeadVO().refreshByHeaderVo(voNewOrder.getHeadVO());

    getPoCardPanel().reloadBillCardAfterAudit(voNewOrder, getBufferVOManager().getVOAt_LoadItemNo(getBufferVOManager().getVOPos()));

    setButtonsStateBrowse();

    showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000236"));

    timeDebug.addExecutePhase("显示");
    timeDebug.showAllExecutePhase("订单UI审批");
  }

  private void onBillExecClose()
  {
    OrderVO voOrderClone = (OrderVO)getOrderDataVOAt(getBufferVOManager().getVOPos()).clone();
    OrderItemVO[] voaItem = voOrderClone.getBodyVO();
    int iBodyLen = voaItem.length;
    for (int i = 0; i < iBodyLen; i++)
    {
      if (voaItem[i].getIisactive().compareTo(OrderItemVO.IISACTIVE_ACTIVE) == 0)
      {
        voaItem[i].setDcorrectdate(getClientEnvironment().getDate());

        voaItem[i].setDCloseDate(PoPublicUIClass.getLoginDate());

        voaItem[i].setCCloseUserId(PoPublicUIClass.getLoginUser());
      }
    }
    PoPublicUIClass.setCuserId(new OrderVO[] { voOrderClone });

    OrderVO voRet = null;
    try {
      voRet = (OrderVO)PfUtilClient.processActionNoSendMessage(this, "CLOSE", "21", getClientEnvironment().getDate().toString().trim(), voOrderClone, null, null, null);

      if (!PfUtilClient.isSuccess())
        return;
    }
    catch (Exception e) {
      PuTool.outException(this, e);
      return;
    }

    getBufferVOManager().setVOAt(getBufferVOManager().getVOPos(), voRet);

    for (int i = 0; i < iBodyLen; i++) {
      getPoCardPanel().setBodyValueAt(OrderItemVO.IISACTIVE_CLOSE_ABNORMAL, i, "iisactive");
      getPoCardPanel().setBodyValueAt(getClientEnvironment().getDate(), i, "dcorrectdate");
      getPoCardPanel().setBodyValueAt(PoPublicUIClass.getLoginDate(), i, "dclosedate");
      getPoCardPanel().setBodyValueAt(PoPublicUIClass.getLoginUser(), i, "ccloseuserid");
      getPoCardPanel().execBodyFormulas(i, getPoCardPanel().getBodyItem("ccloseusername").getLoadFormula());
    }
    setButtonsStateBrowse();

    showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH013"));
  }

  private void onBillExecOpen()
  {
    OrderVO voOrderClone = (OrderVO)getOrderDataVOAt(getBufferVOManager().getVOPos()).clone();
    OrderItemVO[] voaItem = voOrderClone.getBodyVO();
    int iBodyLen = voaItem.length;
    for (int i = 0; i < iBodyLen; i++)
    {
      if (voaItem[i].getIisactive().compareTo(OrderItemVO.IISACTIVE_ACTIVE) != 0)
      {
        voaItem[i].setDcorrectdate(null);

        voaItem[i].setDCloseDate(null);

        voaItem[i].setCCloseUserId(null);
      }
    }
    PoPublicUIClass.setCuserId(new OrderVO[] { voOrderClone });

    OrderVO voRet = null;
    try {
      voRet = (OrderVO)PfUtilClient.processActionNoSendMessage(this, "OPEN", "21", getClientEnvironment().getDate().toString().trim(), voOrderClone, null, null, null);

      if (!PfUtilClient.isSuccess())
        return;
    }
    catch (Exception e) {
      PuTool.outException(this, e);
      return;
    }

    getBufferVOManager().setVOAt(getBufferVOManager().getVOPos(), voRet);

    for (int i = 0; i < iBodyLen; i++) {
      getPoCardPanel().setBodyValueAt(OrderItemVO.IISACTIVE_ACTIVE, i, "iisactive");
      getPoCardPanel().setBodyValueAt(null, i, "dcorrectdate");
      getPoCardPanel().setBodyValueAt(null, i, "dclosedate");
      getPoCardPanel().setBodyValueAt(null, i, "ccloseuserid");
      getPoCardPanel().setBodyValueAt(null, i, "ccloseusername");
    }
    setButtonsStateBrowse();

    showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH012"));
  }

  private void onBillExecUnAudit()
  {
    PoPublicUIClass.setCuserId(new OrderVO[] { getOrderDataVOAt(getBufferVOManager().getVOPos()) });

    OrderHeaderVO headVO = getOrderDataVOAt(getBufferVOManager().getVOPos()).getHeadVO();
    UFDate dateAuditOld = headVO.getDauditdate();
    String strPsnOld = headVO.getCauditpsn();

    Timer timeDebug = new Timer();
    timeDebug.start();

    OrderVO voNewOrder = null;
    try {
      PfUtilClient.processActionNoSendMessage(this, "UNAPPROVE_OPER" + PoPublicUIClass.getLoginUser(), "21", getClientEnvironment().getDate().toString().trim(), getOrderDataVOAt(getBufferVOManager().getVOPos()), new ClientLink(getClientEnvironment()), null, null);

      if (!PfUtilClient.isSuccess())
      {
        headVO.setDauditdate(dateAuditOld);
        headVO.setCauditpsn(strPsnOld);
        return;
      }
      timeDebug.addExecutePhase("弃审");

      voNewOrder = OrderHelper.queryOrderVOLight(getOrderDataVOAt(getBufferVOManager().getVOPos()).getHeadVO().getCorderid(), "AUDIT");
      timeDebug.addExecutePhase("重新查询(轻量VO)");
    }
    catch (Exception e) {
      headVO.setDauditdate(dateAuditOld);
      headVO.setCauditpsn(strPsnOld);

      PuTool.outException(this, e);
      return;
    }

    if (voNewOrder == null) {
      showHintMessage(PuPubVO.MESSAGE_CONCURRENT);
      return;
    }

    getBufferVOManager().getVOAt_LoadItemNo(getBufferVOManager().getVOPos()).getHeadVO().refreshByHeaderVo(voNewOrder.getHeadVO());

    getPoCardPanel().reloadBillCardAfterAudit(voNewOrder, getBufferVOManager().getVOAt_LoadItemNo(getBufferVOManager().getVOPos()));

    setButtonsStateBrowse();

    timeDebug.addExecutePhase("显示");
    timeDebug.showAllExecutePhase("订单UI弃审");

    showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH011"));
  }

  private void onBillFirst()
  {
    getBufferVOManager().setPreviousVOPos(getBufferVOManager().getVOPos());
    getBufferVOManager().setVOPos(0);

    displayCurVOEntirelyReset(true);
    setButtonsStateBrowse();
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000026"));
  }

  private void onBillGrossProfit()
  {
    OrderVO voOrder = (OrderVO)getPoCardPanel().getBillValueVO(OrderVO.class.getName(), OrderHeaderVO.class.getName(), OrderItemVO.class.getName());

    if ((voOrder == null) || (voOrder.getBodyVO().length == 0)) {
      return;
    }

    ArrayList listGrossItem = new ArrayList();
    ArrayList listHintRow = new ArrayList();
    int iLen = voOrder.getBodyVO().length;
    for (int i = 0; i < iLen; i++) {
      if (voOrder.getBodyVO()[i].getCmangid() != null)
      {
        String sBaseId = (String)getPoCardPanel().getBodyValueAt(i, "cbaseid");
        if (sBaseId != null)
        {
          if ((PuTool.isLabor(sBaseId)) || (PuTool.isDiscount(sBaseId)) || (PuPubVO.getString_TrimZeroLenAsNull(voOrder.getBodyVO()[i].getCcurrencytypeid()) == null))
          {
            listHintRow.add(new Integer(i + 1));
          }
          else if ((voOrder.getBodyVO()[i] != null) && (!voOrder.getBodyVO()[i].isLargess()))
            listGrossItem.add(voOrder.getBodyVO()[i]);
        }
      }
    }
    iLen = listHintRow.size();
    if (iLen > 0) {
      String strErrMsg = PuPubVO.getSelectStringByFields((String[])listHintRow.toArray(new String[listHintRow.size()]));
      showWarningMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000047", null, new String[] { strErrMsg }));
    }
    iLen = listGrossItem.size();
    if (iLen == 0)
      return;
    try
    {
      this.m_nflagSave = getCurOperState();
      setCurOperState(3);

      remove(getPoCardPanel());
      add(getGrossProfitPanel(), "Center");

      setButtons(getButtonsGrossProfit());

      getButtonsGrossProfit()[0].setVisible(true);

      voOrder.setChildrenVO((OrderItemVO[])listGrossItem.toArray(new OrderItemVO[iLen]));
      getGrossProfitPanel().display(voOrder);

      repaint();

      showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000041"));
    } catch (Exception e) {
      PuTool.outException(this, e);
    }
  }

  private void onBillInsertLine()
  {
    showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000048"));

    getPoCardPanel().onActionInsertLine();

    setButtonsStateEdit();

    showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH038"));
  }

  private void onBillInvReplace()
  {
    if (getPoCardPanel().getRowCount() == 0) {
      return;
    }

    int iRow = getPoCardPanel().getBillTable().getSelectedRow();
    if (iRow == -1) {
      MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270"), NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000049"));
      return;
    }

    String sMangId = (String)getPoCardPanel().getBodyValueAt(iRow, "cmangid");
    if (PuPubVO.getString_TrimZeroLenAsNull(sMangId) == null) {
      MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270"), NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000049"));
      return;
    }

    if (this.m_pnlInvInfo == null) {
      this.m_pnlInvInfo = new InvManCardPanel();
    }
    this.m_pnlInvInfo.setInvManPk(sMangId);

    UIDialog dlg = new UIDialog(this);
    dlg.setTitle(NCLangRes.getInstance().getStrByID("4004020201", "UPT4004020201-000071"));
    dlg.setSize(700, 500);
    dlg.setContentPane(this.m_pnlInvInfo);
    dlg.showModal();
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000042"));
  }

  private void onBillInvSetQuery()
  {
    if (getPoCardPanel().getRowCount() == 0) {
      return;
    }

    int iRow = getPoCardPanel().getBillTable().getSelectedRow();
    if (iRow == -1) {
      MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270"), NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000049"));
      return;
    }

    String sMangId = (String)getPoCardPanel().getBodyValueAt(iRow, "cmangid");
    if (PuPubVO.getString_TrimZeroLenAsNull(sMangId) == null) {
      MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270"), NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000049"));
      return;
    }

    if (this.m_dlgInvSetQuery == null) {
      this.m_dlgInvSetQuery = new SetPartDlg(this, NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000109"));
    }
    this.m_dlgInvSetQuery.setParam(getCurPk_corp(), sMangId);
    this.m_dlgInvSetQuery.showSetpartDlg();
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000043"));
  }

  private void onBillLast()
  {
    getBufferVOManager().setPreviousVOPos(getBufferVOManager().getVOPos());
    getBufferVOManager().setVOPos(getBufferLength() - 1);

    displayCurVOEntirelyReset(true);
    setButtonsStateBrowse();

    showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000029"));
  }

  private void onBillList()
  {
    setCurOperState(2);

    int iSortCol = getPoCardPanel().getBillModel().getSortColumn();
    boolean bSortAsc = getPoCardPanel().getBillModel().isSortAscending();
    remove(getPoCardPanel());
    add(getPoListPanel(), "Center");

    getPoListPanel().displayCurVO(getBufferVOManager().getVOPos(), true, isFromOtherBill());

    if (iSortCol >= 0) {
      getPoListPanel().getBodyBillModel().sortByColumn(iSortCol, bSortAsc);
    }

    setButtons(getBtnManager().getBtnaList(this));

    if (isFromOtherBill())
      setButtonsStateListFromBills();
    else {
      setButtonsStateListNormal();
    }

    updateUI();

    showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH022"));
  }

  /** @deprecated */
  private void onBillLnkQuery()
  {
    OrderVO voOrder = getOrderDataVOAt(getBufferVOManager().getVOPos());

    SourceBillFlowDlg soureDlg = new SourceBillFlowDlg(this, "21", voOrder.getHeadVO().getCorderid(), null, PoPublicUIClass.getLoginUser(), PoPublicUIClass.getLoginPk_corp());

    soureDlg.showModal();

    showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000019"));
  }

  private void onBillLocate()
  {
    int iRet = 2;

    if (getBufferLength() > 0)
    {
      iRet = getDlgLocate().showModal();

      if (getDlgLocate().isCloseOk())
      {
        getBufferVOManager().setPreviousVOPos(getBufferVOManager().getVOPos());
        getBufferVOManager().setVOPos(getDlgLocate().getBillLocation() - 1);

        displayCurVOEntirelyReset(true);
      }

    }

    setButtonsStateBrowse();
    if (iRet == 1)
      showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000035"));
    else
      showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000076"));
  }

  protected void onBillModify()
  {
    showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000041"));

    setCurOperState(1);

    setButtonsStateEdit();

    getPoCardPanel().onActionModify();

    getPoCardPanel().transferFocusTo(0);

    showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000030"));
  }

  private void setPopMenuBtnsEnable()
  {
    if ((getBtnManager().btnBillRowOperation == null) || (getBtnManager().btnBillRowOperation.getChildCount() == 0)) {
      getPoCardPanel().getBodyPanel().getMiAddLine().setEnabled(false);
      getPoCardPanel().getBodyPanel().getMiCopyLine().setEnabled(false);
      getPoCardPanel().getBodyPanel().getMiDelLine().setEnabled(false);
      getPoCardPanel().getBodyPanel().getMiInsertLine().setEnabled(false);
      getPoCardPanel().getBodyPanel().getMiPasteLine().setEnabled(false);
      getPoCardPanel().getBodyPanel().getMiPasteLineToTail().setEnabled(false);
    }
    else
    {
      getPoCardPanel().getBodyPanel().getMiAddLine().setEnabled(getBtnManager().btnBillAddRow.isPower());
      getPoCardPanel().getBodyPanel().getMiCopyLine().setEnabled(getBtnManager().btnBillCopyRow.isPower());
      getPoCardPanel().getBodyPanel().getMiDelLine().setEnabled(getBtnManager().btnBillDelRow.isPower());
      getPoCardPanel().getBodyPanel().getMiInsertLine().setEnabled(getBtnManager().btnBillInsertRow.isPower());
      getPoCardPanel().getBodyPanel().getMiPasteLine().setEnabled(getBtnManager().btnBillPasteRow.isPower());

      getPoCardPanel().getBodyPanel().getMiPasteLineToTail().setEnabled(getBtnManager().btnBillPasteRow.isPower());
    }
  }

  private void onBillNew()
  {
    setCurOperState(1);

    getPoCardPanel().onActionNew(getCurBizeType());

    setButtonsStateEdit();

    getPoCardPanel().transferFocusTo(0);
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000030"));
  }

  private void onBillNext()
  {
    getBufferVOManager().setPreviousVOPos(getBufferVOManager().getVOPos());
    getBufferVOManager().setVOPos(getBufferVOManager().getVOPos() + 1);

    displayCurVOEntirelyReset(true);
    setButtonsStateBrowse();
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000028"));
  }

  private void onBillPasteLine()
  {
    showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000050"));

    getPoCardPanel().onActionPasteLine();

    setButtonsStateEdit();
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH040"));
  }

  private void onBillPayExecStat()
  {
    PayExecStatDlg dlgPay = new PayExecStatDlg(this, getOrderViewVOAt(getBufferVOManager().getVOPos()).getHeadVO().getPk_corp(), getOrderViewVOAt(getBufferVOManager().getVOPos()).getHeadVO().getCorderid());

    showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000044"));
  }

  private void onBillPrePay()
  {
    OrderVO vo = getOrderDataVOAt(getBufferVOManager().getVOPos());
    boolean bIsAllRowsClosed = vo.isAllRowsClosed();
    if (bIsAllRowsClosed) {
      MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270"), NCLangRes.getInstance().getStrByID("4004ap", "UPP4004ap-000009"));

      return;
    }
    PoToPrePayUI pnlPrePay = new PoToPrePayUI(vo, getPoCardPanel().getPoPubSetUi2());
    pnlPrePay.display(this);
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000045"));
  }

  private void onBillPrevious()
  {
    getBufferVOManager().setPreviousVOPos(getBufferVOManager().getVOPos());
    getBufferVOManager().setVOPos(getBufferVOManager().getVOPos() - 1);

    displayCurVOEntirelyReset(true);
    setButtonsStateBrowse();
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000027"));
  }

  private void onBillPrint()
  {
    ArrayList listVo = new ArrayList();
    listVo.add(getOrderDataVOAt(getBufferVOManager().getVOPos()));
    try
    {
      if ((CustomerConfigVO.getCustomerName().equalsIgnoreCase("TIANYIN")) || (CustomerConfigVO.getCustomerName().equalsIgnoreCase("WUXITAIHU")) || (CustomerConfigVO.getCustomerName().equalsIgnoreCase("NANJINGPUZHEN")) || (CustomerConfigVO.getCustomerName().equalsIgnoreCase("DEMEIHUAGONG")) || (CustomerConfigVO.getCustomerName().equalsIgnoreCase("BEIJINGJINLIUFU")))
      {
        OrderPrintData printData = new OrderPrintData();
        printData.setBillCardPanel(getPoCardPanel());
        this.m_printList = new ScmPrintTool(getPoCardPanel(), printData, listVo, getModuleCode());
      } else {
        this.m_printList = new ScmPrintTool(this, getPoCardPanel(), listVo, getModuleCode());
      }
    } catch (Exception e) {
      SCMEnv.out(e);
    }
    try {
      this.m_printList.onCardPrint(getPoCardPanel(), getPoListPanel(), "21");
      MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270"), this.m_printList.getPrintMessage());
    } catch (Exception e) {
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000051"), e.getMessage());
    }
  }

  private void onBillPrintPreview()
  {
    ArrayList listVo = new ArrayList();
    listVo.add(getOrderDataVOAt(getBufferVOManager().getVOPos()));
    try
    {
      if ((CustomerConfigVO.getCustomerName().equalsIgnoreCase("TIANYIN")) || (CustomerConfigVO.getCustomerName().equalsIgnoreCase("WUXITAIHU")) || (CustomerConfigVO.getCustomerName().equalsIgnoreCase("NANJINGPUZHEN")) || (CustomerConfigVO.getCustomerName().equalsIgnoreCase("DEMEIHUAGONG")) || (CustomerConfigVO.getCustomerName().equalsIgnoreCase("BEIJINGJINLIUFU")))
      {
        OrderPrintData printData = new OrderPrintData();
        printData.setBillCardPanel(getPoCardPanel());
        this.m_printList = new ScmPrintTool(getPoCardPanel(), printData, listVo, getModuleCode());
      } else {
        this.m_printList = new ScmPrintTool(this, getPoCardPanel(), listVo, getModuleCode());
      }
    } catch (Exception e) {
      SCMEnv.out(e);
    }
    try {
      this.m_printList.onCardPrintPreview(getPoCardPanel(), getPoListPanel(), "21");

      showHintMessage(this.m_printList.getPrintMessage());
    } catch (Exception e) {
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000051"), e.getMessage());
    }
  }

  private void onBillQuery()
  {
    getPoQueryCondition().showModal();

    if (getPoQueryCondition().isCloseOK())
    {
      onBillRefresh();
    }

    showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH009"));
  }

  private void onBillRefresh()
  {
    queryOrderVOsFromDB();
    setIsFromOtherBill(false);

    getBufferVOManager().setVOPos(0);
    displayCurVOEntirelyReset(true);
    setButtonsStateBrowse();

    showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH007"));
  }

  private void onBillReveivePlan()
  {
    OrderVO vo = getOrderDataVOAt(getBufferVOManager().getVOPos());
    try {
      UFBoolean ufbBusiRp = OrderHelper.isBusiRp(vo.getHeadVO().getCbiztype(), vo.getHeadVO().getPk_corp());
      if (!ufbBusiRp.booleanValue()) {
        MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000052"), NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000053"));
        return;
      }
    } catch (Exception e) {
      SCMEnv.out("判断当前业务类型“是否进行到货计划安排”时出错!直接返回");
      return;
    }
    if (this.m_ufbIsDmEnabled == null) {
      try {
        ICreateCorpQueryService myService = (ICreateCorpQueryService)NCLocator.getInstance().lookup(ICreateCorpQueryService.class.getName());
        boolean isEnabled = myService.isEnabled(getCorpPrimaryKey(), "DM");
        this.m_ufbIsDmEnabled = new UFBoolean(isEnabled);
      } catch (Exception e) {
        SCMEnv.out("判断供应链发运产品是否启用时出现异常，生成发运日计划功能不能使用");
      }
    }
    PoReceivePlanUI pnlRP = new PoReceivePlanUI(vo);
    pnlRP.display(this, this.m_ufbIsDmEnabled);

    if (pnlRP.isOrderVOChanged()) {
      if (pnlRP.getOrderVO() == null)
        getBufferVOManager().removeAt(getBufferVOManager().getVOPos());
      else {
        getBufferVOManager().setVOAt(getBufferVOManager().getVOPos(), pnlRP.getOrderVO());
      }
      displayCurVOEntirelyReset(true);
      setButtonsStateBrowse();
    }
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000046"));
  }

  private void onBillSaleNum()
  {
    if (getPoCardPanel().getRowCount() == 0) {
      return;
    }

    int iRow = getPoCardPanel().getBillTable().getSelectedRow();
    if (iRow == -1) {
      MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270"), NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000049"));
      return;
    }
    iRow = PuTool.getIndexBeforeSort(getPoCardPanel(), iRow);

    String sMangId = (String)getPoCardPanel().getBodyValueAt(iRow, "cmangid");
    if (PuPubVO.getString_TrimZeroLenAsNull(sMangId) == null) {
      MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270"), NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000049"));
      return;
    }

    AvgSaleQueryVO voSaleQuery = new AvgSaleQueryVO();
    voSaleQuery.setDqueryDate(PoPublicUIClass.getLoginDate());
    voSaleQuery.setCinvmandocid(sMangId);
    voSaleQuery.setCinvbasdocid((String)getPoCardPanel().getBodyValueAt(iRow, "cbaseid"));
    voSaleQuery.setCunitid((String)getPoCardPanel().getBodyValueAt(iRow, "cmessureunit"));
    voSaleQuery.setCunitname((String)getPoCardPanel().getBodyValueAt(iRow, "cmessureunitname"));
    voSaleQuery.setCinvcode((String)getPoCardPanel().getBodyValueAt(iRow, "cinventorycode"));
    voSaleQuery.setCinvname((String)getPoCardPanel().getBodyValueAt(iRow, "cinventoryname"));
    voSaleQuery.setCinvspec((String)getPoCardPanel().getBodyValueAt(iRow, "cspecification"));
    voSaleQuery.setCinvtype((String)getPoCardPanel().getBodyValueAt(iRow, "ctype"));

    getDlgSaleNum().initData(new AvgSaleQueryVO[] { voSaleQuery });
    getDlgSaleNum().showModal();
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000047"));
  }

  protected boolean onBillSave()
  {
    Timer timeDebug = new Timer();
    timeDebug.start();

    OrderVO voSavedOrder = getSaveVO();
    if (voSavedOrder == null) {
      return false;
    }
    timeDebug.addExecutePhase("得到待保存VO");

    voSavedOrder = onSave(this, getPoCardPanel(), voSavedOrder);

    if (voSavedOrder == null) {
      return false;
    }
    timeDebug.addExecutePhase("保存");

    String sOrderId = voSavedOrder.getHeadVO().getCorderid();

    if (isFromOtherBill())
    {
      if ((sOrderId != null) && (getCupsourcebilltype().equals("20"))) {
        ArrayList listPrTs = null;
        try {
          listPrTs = OrderHelper.queryPrayHBTSInAOrder(sOrderId);
        } catch (Exception e) {
          PuTool.outException(this, e);
        }
        if (listPrTs != null) {
          String[][] saHTs = (String[][])listPrTs.get(0);
          String[][] saBTs = (String[][])listPrTs.get(1);
          int iHLen = saHTs.length;
          for (int i = 0; i < iHLen; i++) {
            getHtUpSourcePrayTs().put(saHTs[i][0], saHTs[i][1]);
          }
          int iBLen = saBTs.length;
          for (int i = 0; i < iBLen; i++) {
            getHtUpSourcePrayTs().put(saBTs[i][0], saBTs[i][1]);
          }
        }
      }
      timeDebug.addExecutePhase("请购单并发查询");

      getBufferVOManager().removeAt(getBufferVOManager().getVOPos());
      if (voSavedOrder != null) {
        getBufferVOFrmBill().addVO(voSavedOrder);
      }
      timeDebug.showAllExecutePhase("转单");

      if (getBufferLength() > 0) {
        getBufferVOManager().setVOPos(0);
        onBillList();
        setButtonsStateList();
        return true;
      }
      setIsFromOtherBill(false);

      getBufferVOManager().resetVOs(getBufferVOFrmBill());

      getBufferVOManager().setVOPos(getBufferLength() - 1);

      setHmapUpSourcePrayTs(null);

      getBufferVOFrmBill().clear();
    }
    else {
      String sOrgId = null;
      if (getBufferLength() != 0) {
        sOrgId = getOrderDataVOAt(getBufferVOManager().getVOPos()).getHeadVO().getCorderid();
      }
      if ((voSavedOrder == null) && (sOrderId.equals(sOrgId)))
      {
        getBufferVOManager().removeAt(getBufferVOManager().getVOPos());
        showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000054"));
      }
      else if (sOrderId.equals(sOrgId))
      {
        getBufferVOManager().setVOAt(getBufferVOManager().getVOPos(), voSavedOrder);
      }
      else {
        getBufferVOManager().addVO(voSavedOrder);
        getBufferVOManager().setVOPos(getBufferLength() - 1);
      }

      timeDebug.addExecutePhase("重新设置VO");
    }

    displayCurVOEntirelyReset(false);
    timeDebug.addExecutePhase("重新显示");

    getPoCardPanel().setEnabled(false);

    setCurOperState(0);

    setButtonsStateBrowse();

    repaint();

    if (!(this instanceof RevisionUI)) {
      InvAttrCellRenderer ficr = new InvAttrCellRenderer();
      ficr.setFreeItemRenderer(getPoCardPanel(), null);
    }

    timeDebug.showAllExecutePhase("订单UI保存");

    showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH005"));
    return true;
  }

  private void onBillSendToAudit()
  {
    showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000055"));
    try
    {
      if (getCurOperState() == 1) {
        boolean bContinue = onBillSave();
        if (!bContinue) {
          return;
        }
      }
      OrderVO voOrder = getOrderDataVOAt(getBufferVOManager().getVOPos());

      ArrayList userObj = new ArrayList();
      userObj.add(PoPublicUIClass.getLoginUser());
      userObj.add(PoPublicUIClass.getLoginDate());
      try {
        PfUtilClient.processAction("SAVE", "21", ClientEnvironment.getInstance().getDate().toString(), voOrder, userObj);
      } catch (Exception ex) {
        PuTool.outException(this, ex);
      }

      if (CustomerConfigVO.getCustomerName().equalsIgnoreCase("TIANYIN"))
      {
        voOrder = OrderHelper.queryOrderVOByKey(voOrder.getHeadVO().getCorderid());

        getBufferVOManager().setVOAt(getBufferVOManager().getVOPos(), voOrder);

        displayCurVOEntirelyReset(false);
        repaint();
      }
      else
      {
        OrderVO voNewOrder = OrderHelper.queryOrderVOLight(getOrderDataVOAt(getBufferVOManager().getVOPos()).getHeadVO().getCorderid(), "AUDIT");

        getBufferVOManager().getVOAt_LoadItemNo(getBufferVOManager().getVOPos()).getHeadVO().refreshByHeaderVo(voNewOrder.getHeadVO());

        getPoCardPanel().reloadBillCardAfterAudit(voNewOrder, getBufferVOManager().getVOAt_LoadItemNo(getBufferVOManager().getVOPos()));
      }
    } catch (Exception e) {
      PuTool.outException(this, e);
    }

    setButtonsStateBrowse();
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH023"));
  }

  private void onBillUsableNum()
  {
    if (getPoCardPanel().getRowCount() == 0) {
      return;
    }

    int iRow = getPoCardPanel().getBillTable().getSelectedRow();
    if (iRow == -1) {
      iRow = 0;
    }

    OrderItemVO voItem = (OrderItemVO)getPoCardPanel().getBillModel().getBodyValueRowVO(iRow, OrderItemVO.class.getName());

    if ((PuPubVO.getString_TrimZeroLenAsNull(voItem.getCmangid()) == null) || (PuPubVO.getString_TrimZeroLenAsNull(voItem.getAttributeValue("dconsigndate")) == null))
    {
      MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270"), NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000056"));
      return;
    }
    OrderHeaderVO voHead = (OrderHeaderVO)getPoCardPanel().getBillData().getHeaderValueVO(OrderHeaderVO.class.getName());

    OrderVO voPara = new OrderVO(1);
    voPara.setParentVO(voHead);
    voPara.setChildrenVO(new OrderItemVO[] { voItem });

    if (this.saPkCorp == null) {
      try {
        IUserManageQuery myService = (IUserManageQuery)NCLocator.getInstance().lookup(IUserManageQuery.class.getName());
        CorpVO[] vos = myService.queryAllCorpsByUserPK(getClientEnvironment().getUser().getPrimaryKey());

        if ((vos == null) || (vos.length == 0)) {
          SCMEnv.out("未查询到有权限公司，直接返回!");
          return;
        }
        int iLen = vos.length;
        this.saPkCorp = new String[iLen];
        for (int i = 0; i < iLen; i++)
          this.saPkCorp[i] = vos[i].getPrimaryKey();
      }
      catch (Exception e) {
        MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270"), NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000057"));
        SCMEnv.out(e);
        return;
      }
    }
    getDlgAtp().setPkCorps(this.saPkCorp);
    getDlgAtp().initData(voPara);
    getDlgAtp().showModal();
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000032"));
  }

  private void onBillVendorInfo()
  {
    String sVendMangId = getPoCardPanel().getHeadItem("cvendormangid").getValue();
    if (PuPubVO.getString_TrimZeroLenAsNull(sVendMangId) == null) {
      return;
    }

    if (this.m_dlgCustInfo == null) {
      this.m_dlgCustInfo = new CustInfoUI(this, NCLangRes.getInstance().getStrByID("4004020201", "UPT4004020201-000085"));
    }

    this.m_dlgCustInfo.initData(sVendMangId);
    this.m_dlgCustInfo.showModal();
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000048"));
  }

  protected void onButtonClickedBill(ButtonObject bo)
  {
    if (getCurOperState() == 3) {
      shiftGrossProfitToPoCard();
    }

    if (bo.getParent() == getBtnManager().btnBillBusitype)  //业务类型
    {
      showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000293"));
      this.m_cbiztype = bo.getTag();

      setCurPk_corp(PoPublicUIClass.getLoginPk_corp());
      bo.setSelected(true);
      PfUtilClient.retAddBtn(getBtnManager().btnBillAdd, getCurPk_corp(), "21", bo);
      setButtons(getBtnManager().getBtnaBill(this));
      getBtnManager().btnBillAdd.setEnabled(true);
      updateButton(getBtnManager().btnBillAdd);
      OrderUI oui = new OrderUI();
      oui.updateButtonsAll();

      setButtonsStateBrowse();
    } else if (bo.getParent() == getBtnManager().btnBillAdd)
    {
      showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000058"));

      setCurPk_corp(PoPublicUIClass.getLoginPk_corp());
      setCurOperState(1);

      getBufferVOManager().setPreviousVOPos(getBufferVOManager().getVOPos());
      if (bo.getName().equals(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000341"))) {
        onBillNew();
      }
      else {
        String tag = bo.getTag();
        int index = tag.indexOf(":");
        setCupsourcebilltype(tag.substring(0, index));
        setCurBizeType(tag.substring(index + 1, tag.length()));

        ClientEnvironment.getInstance().putValue("env_name_pu_cbiztype", tag.substring(index + 1, tag.length()));

        PfUtilClient.childButtonClicked(bo, getCurPk_corp(), getModuleCode(), getClientEnvironment().getUser().getPrimaryKey(), "21", this);

        if (PfUtilClient.isCloseOK())
        {
          setOrgBufferLen(getBufferLength());
          AggregatedValueObject[] arySourceVOs = PfUtilClient.getRetVos();
          processAfterChange(getCupsourcebilltype(), arySourceVOs);
          SCMEnv.out("out");
        } else {
          setCurOperState(0);
        }
      }
    } else if ((bo == getBtnManager().btnBillSave) || (bo.getParent() == getBtnManager().btnBillMaintain))
    {                                //保存    维护
      onButtonClickedBillMaintain(bo);  
    } else if ((bo == getBtnManager().btnBillQuery) || (bo.getParent() == getBtnManager().btnBillBrowse))
    {                            //查询                浏览
      onButtonClickedBillBrowse(bo);
    } else if (bo.getParent() == getBtnManager().btnBillRowOperation)   //行操作
    {
      onButtonClickedRowOperation(bo);
    } else if ((bo == getBtnManager().btnBillExecute) || (bo.getParent() == getBtnManager().btnBillExecute))
    {                                  //执行
      onButtonClickedBillExec(bo);
    } else if (bo == getBtnManager().btnBillShift)   //卡片显示/列表显示
    {
      showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000059"));
      onBillList();
      showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH022"));
    }
    else if (bo == getBtnManager().btnBillReceivePlan) {
      onBillReveivePlan();   //到货计划
    }
    else if (bo == getBtnManager().btnBillPrePay) {
      onBillPrePay();   //预付款
    }
    else if (bo == getBtnManager().btnBillPrint) {
      showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000060"));
      onBillPrint();  //打印
    }
    else if (bo == getBtnManager().btnBillPrintPreview) {
      showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPT4004020201-000083"));
      onBillPrintPreview();  //打印预览
    }
    else if (bo == getBtnManager().btnBillGrossProfit) {
      showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPT4004020201-000080"));
      onBillGrossProfit();   //毛利预估
    }
    else if (bo == getBtnManager().btnBillUsenum) {
      showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000061"));
      onBillUsableNum();   //存量查询
    }
    else if (bo == getBtnManager().btnBillSaleNum) {
      showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPT4004020201-000076"));
      onBillSaleNum();    //销量查询
    }
    else if (bo == getBtnManager().btnBillAp) {
      showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000062"));
      onBillApQuery();  //应付款
    }
    else if (bo == getBtnManager().btnBillPayExecStat) {
      showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000063"));
      onBillPayExecStat();   //付款执行情况
    }
    else if (bo == getBtnManager().btnBillCombin) {
      onBillCombin();  //合并显示
    }
    else if (bo == getBtnManager().btnBillLnkQry) {
      onBillLnkQuery();   //联查
    }
    else if (bo == getBtnManager().btnBillContractClass) {
      onBillContractClass();  //扩展
    }
    else if (bo == getBtnManager().btnBillStatusQry) {
      onBillAuditStatusQuery();  //审批流状态
    }
    else if (bo == getBtnManager().btnBillVendor) {
      onBillVendorInfo();  //供应商信息
    }
    else if (bo == getBtnManager().btnBillInvReplace) {
      onBillInvReplace();  //存货信息
    }
    else if (bo == getBtnManager().btnBillInvSetQuery) {
      onBillInvSetQuery();  //成套件信息
    }
    else if (bo == getBtnManager().btnBillDocManage) {
      onBillDocManage();   //文档管理
    }
    else if (bo == getBtnManager().btnBillExcel)
    {
      new OrderUI().onExcel();  //采购订单导入
    }
    
    else if (bo == getBtnManager().btnBillXml)
    {
      new OrderUI().onXml();    //yqq 增加XML导入按钮 调用接口2016-10-26
    }
      
    
    else if (bo == getBtnManager().btnBillXmlch){
    	onXmlch();    //yqq 增加XML导出按钮 2016-11-2
    }
  }

  private void onButtonClickedBillAssistQry(ButtonObject bo)
  {
    if (bo == getBtnManager().btnBillReceivePlan) {
      onBillReveivePlan();  //到货计划
    }
    else if (bo == getBtnManager().btnBillPrePay) {
      onBillPrePay();   //预付款
    }
    else if (bo == getBtnManager().btnBillPrint) {
      showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000060"));
      onBillPrint();    //打印
    }
    else if (bo == getBtnManager().btnBillPrintPreview) {
      showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPT4004020201-000083"));
      onBillPrintPreview();   //打印预览
    }
    else if (bo == getBtnManager().btnBillGrossProfit) {
      showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPT4004020201-000080"));
      onBillGrossProfit();   //毛利预估
    }
    else if (bo == getBtnManager().btnBillUsenum) {
      showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000061"));
      onBillUsableNum();   //存量查询
    }
    else if (bo == getBtnManager().btnBillSaleNum) {
      showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPT4004020201-000076"));
      onBillSaleNum();   //销量查询
    }
    else if (bo == getBtnManager().btnBillAp) {
      showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000062"));
      onBillApQuery();   //应付款
    }
    else if (bo == getBtnManager().btnBillPayExecStat) {
      showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000063"));
      onBillPayExecStat();   //付款执行情况
    }
    else if (bo == getBtnManager().btnBillCombin) {
      onBillCombin();   //合并显示
    }
    else if (bo == getBtnManager().btnBillLnkQry) {
      onBillLnkQuery();    //联查
    }
    else if (bo == getBtnManager().btnBillContractClass) {
      onBillContractClass();  //扩展
    }
    else if (bo == getBtnManager().btnBillStatusQry) {
      onBillAuditStatusQuery();   //审批流状态
    }
    else if (bo == getBtnManager().btnBillVendor) {
      onBillVendorInfo();   //供应商信息
    }
    else if (bo == getBtnManager().btnBillInvReplace) {
      onBillInvReplace();   //存货信息
    }
    else if (bo == getBtnManager().btnBillInvSetQuery) {
      onBillInvSetQuery();   //成套件信息
    }
    else if (bo == getBtnManager().btnBillDocManage) {
      onBillDocManage();   //文档管理
    }
  }

  private void onButtonClickedBillAssistFun(ButtonObject bo)
  {
    if (bo == getBtnManager().btnBillReceivePlan) {
      onBillReveivePlan();  //到货计划
    }
    else if (bo == getBtnManager().btnBillPrePay) {
      onBillPrePay();  //预付款
    }
    else if (bo == getBtnManager().btnBillPrint) {
      showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000060"));
      onBillPrint();  //打印
    } 
    else if (bo == getBtnManager().btnBillPrintPreview) {
      showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPT4004020201-000083"));
      onBillPrintPreview();  //打印预览
    } 
    else if (bo == getBtnManager().btnBillGrossProfit) {
      showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPT4004020201-000080"));
      onBillGrossProfit();  //毛利预估
    }
    else if (bo == getBtnManager().btnBillUsenum) {
      showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000061"));
      onBillUsableNum();  //存量查询
    }
    else if (bo == getBtnManager().btnBillSaleNum) {
      showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPT4004020201-000076"));
      onBillSaleNum();  //销量查询
    }
    else if (bo == getBtnManager().btnBillAp) {
      showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000062"));
      onBillApQuery(); //应付款
    }
    else if (bo == getBtnManager().btnBillPayExecStat) {
      showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000063"));
      onBillPayExecStat();   //付款执行情况
    }
    else if (bo == getBtnManager().btnBillCombin) {
      onBillCombin();  //合并显示
    }
    else if (bo == getBtnManager().btnBillLnkQry) {
      onBillLnkQuery();  //联查
    }
    else if (bo == getBtnManager().btnBillContractClass) {
      onBillContractClass();  //扩展
    }
    else if (bo == getBtnManager().btnBillStatusQry) {
      onBillAuditStatusQuery();  //审批流状态
    }
    else if (bo == getBtnManager().btnBillVendor) {
      onBillVendorInfo();  //供应商信息
    }
    else if (bo == getBtnManager().btnBillInvReplace) {
      onBillInvReplace();  //存货信息
    }
    else if (bo == getBtnManager().btnBillInvSetQuery) {
      onBillInvSetQuery();  //成套件信息
    }
    else if (bo == getBtnManager().btnBillDocManage) {
      onBillDocManage();  //文档管理
    }
  }

  private void onButtonClickedBillBrowse(ButtonObject bo)
  {
    if (bo == getBtnManager().btnBillQuery)
    {
      setCurPk_corp(PoPublicUIClass.getLoginPk_corp());
      onBillQuery();   //查询
    } else if (bo == getBtnManager().btnBillRefresh)
    {
      setCurPk_corp(PoPublicUIClass.getLoginPk_corp());
      onBillRefresh();  //刷新下
    } else if (bo == getBtnManager().btnBillFirst)
    {
      showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000064"));
      onBillFirst();  //首页
    } else if (bo == getBtnManager().btnBillPrevious)
    {
      showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000065"));
      onBillPrevious();   //上页
    } else if (bo == getBtnManager().btnBillNext)
    {
      showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000066"));
      onBillNext();   //下页
    } else if (bo == getBtnManager().btnBillLast)
    {
      showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000067"));
      onBillLast();   //末页
    } else if (bo == getBtnManager().btnBillLocate)
    {
      showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000068"));
      onBillLocate();   //定位
    }
  }

  private boolean onButtonClickedBillExec(ButtonObject bo)
  {
    if (getBtnManager().btnBillExecute != bo.getParent()) {
      return false;  //执行
    }

    if (bo == getBtnManager().btnBillExecAudit) {
      showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000069"));
      onBillExecAudit();  //审核
    } else if (bo == getBtnManager().btnBillExecUnAudit) {
      showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000070"));
      onBillExecUnAudit();  //弃审
    } else if (bo == getBtnManager().btnBillExecClose) {
      showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000071"));
      onBillExecClose();  //关闭
    } else if (bo == getBtnManager().btnBillExecOpen) {
      showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000367"));
      onBillExecOpen();  //打开
    } else if (bo == getBtnManager().btnBillSendToAudit) {
      onBillSendToAudit();  //送审
    }
    else {
      try {
        PfUtilClient.processActionNoSendMessage(this, bo.getTag(), "21", getClientEnvironment().getDate().toString(), getOrderDataVOAt(getBufferVOManager().getVOPos()), null, null, null);
      }
      catch (Exception e)
      {
        PuTool.outException(this, e);
      }
    }

    return true;
  }

  private void onButtonClickedBillMaintain(ButtonObject bo)
  {
    if (bo == getBtnManager().btnBillModify)
    {
      showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000041"));
      onBillModify();  //修改
    } else if (bo == getBtnManager().btnBillSave)
    {
      showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000072"));
      onBillSave();   //保存
    } else if (bo == getBtnManager().btnBillCancel)
    {
      showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000349"));
      onBillCancel();   //取消
    } else if (bo == getBtnManager().btnBillAnnul)
    {
      showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000073"));
      onBillDiscard();  //删除
    } else if (bo == getBtnManager().btnBillCopyBill)
    {
      showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000041"));
      setCurPk_corp(PoPublicUIClass.getLoginPk_corp());
      onBillCopyBill();   //复制
      showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH029"));
    }
    else if (bo == getBtnManager().btnBillSendToAudit) {
      onBillSendToAudit();  //送审
    }
  }

  protected void onButtonClickedList(ButtonObject bo)
  {
    if (bo == getBtnManager().btnListSelectAll) {
      showHintMessage(NCLangRes.getInstance().getStrByID("4004070101", "UPT4004070101-000025"));
      onListSelectAll();   //全选
    } else if (bo == getBtnManager().btnListDeselectAll) {
      showHintMessage(NCLangRes.getInstance().getStrByID("4004070101", "UPT4004070101-000026"));
      onListDeselectAll();  //全消
    } else if (bo == getBtnManager().btnListQuery)
    {
      setCurPk_corp(PoPublicUIClass.getLoginPk_corp());

      onListQuery();   //查询
    } else if (bo == getBtnManager().btnListModify) {
      showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000041"));
      onListModify();  //修改
    } else if (bo == getBtnManager().btnListAnnul) {
      onListDiscard();  //删除
    } else if (bo == getBtnManager().btnListRefresh)
    {
      setCurPk_corp(PoPublicUIClass.getLoginPk_corp());

      onListRefresh();   //刷新
    } else if (bo == getBtnManager().btnListShift) {
      showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000074"));
      onListCard();   //卡片显示/列表显示
    } else if (bo == getBtnManager().btnListCancelTransform) {
      showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000382"));
      onListCancelTransform();  //放弃转单
    } else if (bo == getBtnManager().btnListDocManage) {
      showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPT4004020201-000097"));
      onListDocManage();   //文档管理
    } else if (bo == getBtnManager().btnListUsenum) {
      showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPT4004020201-000081"));
      onListUsableNum();   //存量查询
    } else if (bo == getBtnManager().btnListPrint) {
      showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000368"));
      onListPrint();   //打印
    } else if (bo == getBtnManager().btnListPreview) {
      showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000369"));
      onListPreview();  //打印预览
    } else if (bo == getBtnManager().btnBillExecAudit) {
      onListExecAudit();   //审核
    } else if (bo == getBtnManager().btnBillExecUnAudit) {
      onListExecUnAudit();   //弃审
    } else if (bo == getBtnManager().btnBillCopyBill) {
      onListCard();   //复制
      onBillCopyBill();
    } else if (bo == getBtnManager().btnBillXmlch)    
      onXmlchl();    //yqq 增加XML导出按钮 2016-11-4    
  }

  private void onListExecUnAudit()
  {
    Timer timeDebug = new Timer();
    timeDebug.start();

    OrderVO[] voaSelected = getSelectedVOs();
    if ((voaSelected == null) || (voaSelected.length == 0)) {
      return;
    }
    timeDebug.addExecutePhase("获得VO数组");

    HashMap mapAuditInfo = new HashMap();
    ArrayList listAuditInfo = null;
    OrderHeaderVO headVO = null;
    for (int i = 0; i < voaSelected.length; i++) {
      headVO = voaSelected[i].getHeadVO();

      if (PuPubVO.getString_TrimZeroLenAsNull(headVO.getCauditpsn()) != null) {
        listAuditInfo = new ArrayList();
        listAuditInfo.add(headVO.getCauditpsn());
        listAuditInfo.add(headVO.getDauditdate());
        mapAuditInfo.put(headVO.getPrimaryKey(), listAuditInfo);
      }
    }
    try
    {
      Object[] objs = new Object[voaSelected.length];
      ClientLink cl = new ClientLink(getClientEnvironment());
      int iLen = objs.length;
      for (int i = 0; i < iLen; i++) {
        objs[i] = cl;
      }
      PfUtilClient.processBatch("UNAPPROVE", "21", PoPublicUIClass.getLoginDate().toString(), voaSelected, objs);
    }
    catch (Exception e)
    {
      if (mapAuditInfo.size() > 0)
        for (int i = 0; i < voaSelected.length; i++) {
          headVO = voaSelected[i].getHeadVO();
          if (!mapAuditInfo.containsKey(headVO.getPrimaryKey())) {
            headVO.setCauditpsn(null);
            headVO.setDauditdate(null);
          }
          else {
            listAuditInfo = (ArrayList)mapAuditInfo.get(headVO.getPrimaryKey());
            headVO.setCauditpsn((String)listAuditInfo.get(0));
            headVO.setDauditdate((UFDate)listAuditInfo.get(1));
          }
        }
      PuTool.outException(this, e);
      return;
    }
    timeDebug.addExecutePhase("弃审");

    if (PfUtilClient.isSuccess()) {
      showHintMessage(NCLangRes.getInstance().getStrByID("40040203", "UPP40040203-000012"));

      onListRefresh();
      timeDebug.addExecutePhase("重新显示");
    }
    else
    {
      if (mapAuditInfo.size() > 0)
        for (int i = 0; i < voaSelected.length; i++) {
          headVO = voaSelected[i].getHeadVO();
          if (!mapAuditInfo.containsKey(headVO.getPrimaryKey())) {
            headVO.setCauditpsn(null);
            headVO.setDauditdate(null);
          }
          else {
            listAuditInfo = (ArrayList)mapAuditInfo.get(headVO.getPrimaryKey());
            headVO.setCauditpsn((String)listAuditInfo.get(0));
            headVO.setDauditdate((UFDate)listAuditInfo.get(1));
          }
        }
      showHintMessage(NCLangRes.getInstance().getStrByID("40040203", "UPP40040203-000013"));
    }
    timeDebug.showAllExecutePhase("订单UI弃审");

    showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH011"));
  }

  private void onListExecAudit()
  {
    Timer timeDebug = new Timer();
    timeDebug.start();

    OrderVO[] voaSelected = getSelectedVOs();
    if (voaSelected == null) {
      return;
    }
    timeDebug.addExecutePhase("获得VO数组");

    HashMap mapAuditInfo = new HashMap();
    ArrayList listAuditInfo = null;
    OrderHeaderVO headVO = null;
    for (int i = 0; i < voaSelected.length; i++) {
      headVO = voaSelected[i].getHeadVO();

      if (PuPubVO.getString_TrimZeroLenAsNull(headVO.getCauditpsn()) != null) {
        listAuditInfo = new ArrayList();
        listAuditInfo.add(headVO.getCauditpsn());
        listAuditInfo.add(headVO.getDauditdate());
        mapAuditInfo.put(headVO.getPrimaryKey(), listAuditInfo);
      }

      headVO.setCauditpsn(PoPublicUIClass.getLoginUser());
    }

    try
    {
      Object[] objs = new Object[voaSelected.length];
      ClientLink cl = new ClientLink(getClientEnvironment());
      String strErr = PuTool.getAuditLessThanMakeMsg(voaSelected, "dorderdate", "vordercode", cl.getLogonDate(), "21");
      if (strErr != null) {
        throw new BusinessException(strErr);
      }
      int iLen = objs.length;
      for (int i = 0; i < iLen; i++) {
        objs[i] = cl;
      }
      PfUtilClient.processBatchFlow(this, "APPROVE", "21", PoPublicUIClass.getLoginDate().toString(), voaSelected, objs);
    }
    catch (Exception e)
    {
      if (mapAuditInfo.size() > 0)
        for (int i = 0; i < voaSelected.length; i++) {
          headVO = voaSelected[i].getHeadVO();
          if (!mapAuditInfo.containsKey(headVO.getPrimaryKey())) {
            headVO.setCauditpsn(null);
            headVO.setDauditdate(null);
          }
          else {
            listAuditInfo = (ArrayList)mapAuditInfo.get(headVO.getPrimaryKey());
            headVO.setCauditpsn((String)listAuditInfo.get(0));
            headVO.setDauditdate((UFDate)listAuditInfo.get(1));
          }
        }
      PuTool.outException(this, e);
      return;
    }
    timeDebug.addExecutePhase("审批");

    if (PfUtilClient.isSuccess()) {
      showHintMessage(NCLangRes.getInstance().getStrByID("40040203", "UPP40040203-000006"));

      onListRefresh();
      timeDebug.addExecutePhase("重新显示");
    }
    else
    {
      if (mapAuditInfo.size() > 0)
        for (int i = 0; i < voaSelected.length; i++) {
          headVO = voaSelected[i].getHeadVO();
          if (!mapAuditInfo.containsKey(headVO.getPrimaryKey())) {
            headVO.setCauditpsn(null);
            headVO.setDauditdate(null);
          }
          else {
            listAuditInfo = (ArrayList)mapAuditInfo.get(headVO.getPrimaryKey());
            headVO.setCauditpsn((String)listAuditInfo.get(0));
            headVO.setDauditdate((UFDate)listAuditInfo.get(1));
          }
        }
      showHintMessage(NCLangRes.getInstance().getStrByID("40040203", "UPP40040203-000007"));
    }

    timeDebug.addExecutePhase("订单UI审批");

    showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000236"));
  }

  private void onButtonClickedRowOperation(ButtonObject bo)
  {
    if (bo == getBtnManager().btnBillAddRow) {
      showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000043"));
      onBillAppendLine();
    } else if (bo == getBtnManager().btnBillDelRow) {
      showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000046"));
      onBillDeleteLine();
    } else if (bo == getBtnManager().btnBillInsertRow) {
      showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000048"));
      onBillInsertLine();
    } else if (bo == getBtnManager().btnBillCopyRow) {
      showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000044"));
      onBillCopyLine();
    } else if (bo == getBtnManager().btnBillPasteRow) {
      showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000050"));
      onBillPasteLine();
    }
  }

  public void onDoubleClick(int iRow)
  {
    getBufferVOManager().setVOPos(getPoListPanel().getVOPos(iRow));

    OrderVO voCur = getOrderDataVOAt(getBufferVOManager().getVOPos());
    if ((voCur.getBodyVO() == null) || (voCur.getBodyVO().length == 0))
    {
      return;
    }

    showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000074"));

    setCurOperState(0);

    remove(getPoListPanel());
    add(getPoCardPanel(), "Center");

    setButtons(getBtnManager().getBtnaBill(this));

    displayCurVOEntirelyReset(true);
    updateUI();
  }

  private void onListCancelTransform()
  {
    getBufferVOManager().resetVOs(getBufferVOFrmBill());

    getBufferVOFrmBill().clear();

    int iCurBufferLen = getBufferLength();

    if (getOrgBufferLen() == iCurBufferLen)
      getBufferVOManager().setVOPos(getBufferVOManager().getPreviousVOPos());
    else {
      getBufferVOManager().setVOPos(getBufferLength() - 1);
    }

    getBufferVOManager().setPreviousVOPos(getBufferVOManager().getVOPos());

    setIsFromOtherBill(false);
    setCurOperState(0);

    remove(getPoListPanel());
    add(getPoCardPanel(), "Center");
    getPoCardPanel().setEnabled(false);

    setButtons(getBtnManager().getBtnaBill(this));

    displayCurVOEntirelyReset(true);
    setButtonsStateBrowse();

    setHmapUpSourcePrayTs(null);

    showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH008"));
  }

  private void onListCard()
  {
    setCurOperState(0);

    int iSortCol = getPoListPanel().getBodyBillModel().getSortColumn();
    boolean bSortAsc = getPoListPanel().getBodyBillModel().isSortAscending();

    remove(getPoListPanel());
    add(getPoCardPanel(), "Center");

    setButtons(getBtnManager().getBtnaBill(this));

    if (getBufferLength() > 0)
    {
      getBufferVOManager().setVOPos(0);
      for (int i = 0; i < getPoListPanel().getHeadRowCount(); i++) {
        if (getPoListPanel().getHeadRowState(i) == 4) {
          getBufferVOManager().setVOPos(getPoListPanel().getVOPos(i));
          break;
        }

      }

      displayCurVOEntirelyReset(true);

      if ((getBufferLength() > 0) && 
        (isFromOtherBill()))
        onBillModify();
    }
    else
    {
      getPoCardPanel().addNew();
    }

    if (iSortCol >= 0) {
      getPoCardPanel().getBillModel().sortByColumn(iSortCol, bSortAsc);
    }
    setButtonsStateBrowse();
    updateUI();
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH021"));
  }

  private void onListDeselectAll()
  {
    getPoListPanel().onActionDeselectAll();
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000034"));
  }

  private void onListDiscard()
  {
    int[] iaSelectedRowCount = getPoListPanel().getHeadSelectedRows();
    int iDeletedLen = iaSelectedRowCount.length;
    for (int i = 0; i < iDeletedLen; i++) {
      int iVOPos = getPoListPanel().getVOPos(iaSelectedRowCount[i]);
      OrderVO voCur = getBufferVOManager().getVOAt_LoadItemNo(iVOPos);

      Integer iStatus = voCur.getHeadVO().getForderstatus();
      if ((iStatus.compareTo(BillStatus.FREE) != 0) && (iStatus.compareTo(BillStatus.AUDITFAIL) != 0))
      {
        showWarningMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000075"));
        return;
      }
    }
    OrderVO[] voaCanceled = getSelectedVOs();
    if (voaCanceled == null) {
      return;
    }

    boolean bSuccessed = onDiscard(this, voaCanceled);
    if (!bSuccessed) {
      return;
    }

    if (getBufferLength() == iDeletedLen) {
      getBufferVOManager().clear();
    }
    else
    {
      Integer[] iaIndex = getPoListPanel().getHeadSelectedVOPoses();
      for (int i = iDeletedLen - 1; i >= 0; i--) {
        getBufferVOManager().removeAt(iaIndex[i].intValue());
      }
    }

    getPoListPanel().displayCurVO(0, true, isFromOtherBill());
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH006"));
  }

  private void onListDocManage()
  {
    int[] iaSelectedRow = getPoListPanel().getHeadSelectedRows();
    int iSelectedLen = getPoListPanel().getHeadSelectedCount();
    String[] saPk_corp = new String[iSelectedLen];
    String[] saCode = new String[iSelectedLen];

    HashMap mapBtnPowerVo = new HashMap();
    Integer iBillStatus = null;
    BtnPowerVO pVo = null;
    for (int i = 0; i < iSelectedLen; i++) {
      OrderVO voCur = getOrderDataVOAt(getPoListPanel().getVOPos(iaSelectedRow[i]));
      saPk_corp[i] = voCur.getHeadVO().getCorderid();
      saCode[i] = voCur.getHeadVO().getVordercode();
      pVo = new BtnPowerVO(saCode[i]);
      iBillStatus = PuPubVO.getInteger_NullAs(voCur.getHeadVO().getForderstatus(), new Integer(0));
      if ((iBillStatus.intValue() == 2) || (iBillStatus.intValue() == 3) || (iBillStatus.intValue() == 5))
      {
        pVo.setFileDelEnable("false");
      }
      mapBtnPowerVo.put(saCode[i], pVo);
    }

    DocumentManager.showDM(this, saPk_corp, saCode, mapBtnPowerVo);

    showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000025"));
  }

  private void onListModify()
  {
    onDoubleClick(getPoListPanel().getHeadSelectedRow());

    onBillModify();

    showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000030"));
  }

  private void onListPreview()
  {
    ArrayList arylistPrint = getPrintAryList();
    if (arylistPrint == null)
      return;
    try
    {
      getPrintTool().setData(arylistPrint);
      getPrintTool().onBatchPrintPreview(getPoListPanel(), getPoCardPanel(), "21");
      MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270"), getPrintTool().getPrintMessage());
    } catch (Exception e) {
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000051"), e.getMessage());
    }
  }

  private void onListPrint()
  {
    ArrayList arylistPrint = getPrintAryList();
    if (arylistPrint == null)
      return;
    try
    {
      getPrintTool().setData(arylistPrint);
      getPrintTool().onBatchPrint(getPoListPanel(), getPoCardPanel(), "21");
      MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270"), getPrintTool().getPrintMessage());
    } catch (Exception e) {
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000051"), e.getMessage());
    }
  }

  private void onListQuery()
  {
    getPoQueryCondition().showModal();

    if (getPoQueryCondition().isCloseOK()) {
      onListRefresh();
    }

    showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH009"));
  }

  private void onListRefresh()
  {
    queryOrderVOsFromDB();

    getPoListPanel().displayCurVO(0, true, isFromOtherBill());

    setButtonsStateListNormal();
    updateUI();
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH007"));
  }

  private void onListSelectAll()
  {
    getPoListPanel().onActionSelectAll();
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000033"));
  }

  private void onListUsableNum()
  {
    int iRow = getPoListPanel().getHeadSelectedRow();
    OrderVO voBuf = getOrderDataVOAt(getPoListPanel().getVOPos(iRow));
    OrderItemVO[] bodyVO = voBuf.getBodyVO();
    if ((bodyVO == null) || (bodyVO.length == 0)) {
      return;
    }

    int iSelectBodyRow = getPoListPanel().getBodyTable().getSelectedRow();
    if ((iSelectBodyRow < 0) || (iSelectBodyRow > bodyVO.length - 1)) {
      iSelectBodyRow = 0;
    }
    if ((PuPubVO.getString_TrimZeroLenAsNull(voBuf.getBodyVO()[iSelectBodyRow].getCmangid()) == null) || (PuPubVO.getString_TrimZeroLenAsNull(voBuf.getBodyVO()[iSelectBodyRow].getAttributeValue("dconsigndate")) == null))
    {
      MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270"), NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000056"));
      return;
    }

    OrderVO voPara = new OrderVO(1);
    voPara.setParentVO(voBuf.getHeadVO());
    voPara.setChildrenVO(new OrderItemVO[] { voBuf.getBodyVO()[iSelectBodyRow] });

    if (this.saPkCorp == null) {
      try {
        IUserManageQuery myService = (IUserManageQuery)NCLocator.getInstance().lookup(IUserManageQuery.class.getName());
        CorpVO[] vos = myService.queryAllCorpsByUserPK(getClientEnvironment().getUser().getPrimaryKey());

        if ((vos == null) || (vos.length == 0)) {
          SCMEnv.out("未查询到有权限公司，直接返回!");
          return;
        }
        int iLen = vos.length;
        this.saPkCorp = new String[iLen];
        for (int i = 0; i < iLen; i++)
          this.saPkCorp[i] = vos[i].getPrimaryKey();
      }
      catch (Exception e) {
        MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270"), NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000057"));
        SCMEnv.out(e);
        return;
      }
    }
    getDlgAtp().setPkCorps(this.saPkCorp);
    getDlgAtp().initData(voPara);
    getDlgAtp().showModal();
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000032"));
  }

  private void queryOrderVOsFromDB()
  {
    showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000076"));
    try
    {
      getBufferVOManager().resetVOs(getQueriedVOHead());

      setEverQueryed(true);
    } catch (Exception e) {
      PuTool.outException(this, e);
      return;
    }

    if (getBufferLength() == 0) {
      showWarningMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000077"));
    }

    showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000042"));
  }

  public void setBillVO(AggregatedValueObject vo)
  {
    getPoCardPanel().displayCurVO((OrderVO)vo, isFromOtherBill());
  }

  protected void setButtonsStateBrowse()
  {
    if (getBtnManager().btnBillRevise != null) {
      getBtnManager().btnBillRevise.setEnabled(true);
    }

    if (getBtnManager().btnListCancelTransform != null) {
      getBtnManager().btnListCancelTransform.setVisible(false);
    }

    getBtnManager().btnBillShift.setName(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000464"));

    getBtnManager().btnBillReplenish.setEnabled(true);

    getBtnManager().btnListSelectAll.setEnabled(false);

    getBtnManager().btnListDeselectAll.setEnabled(false);

    getBtnManager().btnBillBusitype.setEnabled(true);

    if ((!getBtnManager().btnBillBusitype.isVisible()) || (getCurBizeType() == null))
    {
      getBtnManager().btnBillAdd.setEnabled(false);
    }
    else getBtnManager().btnBillAdd.setEnabled(true);

    getBtnManager().btnBillSave.setEnabled(false);

    getBtnManager().btnBillCancel.setEnabled(false);

    getBtnManager().btnBillRowOperation.setEnabled(false);

    getBtnManager().btnBillQuery.setEnabled(true);

    getBtnManager().btnBillShift.setEnabled(true);

    getBtnManager().btnBillBrowse.setEnabled(true);

    OrderVO voCur = null;
    if (getBufferLength() == 0)
    {
      getBtnManager().btnBillAnnul.setEnabled(false);

      getBtnManager().btnBillModify.setEnabled(false);

      getBtnManager().btnBillFirst.setEnabled(false);
      getBtnManager().btnBillPrevious.setEnabled(false);
      getBtnManager().btnBillNext.setEnabled(false);
      getBtnManager().btnBillLast.setEnabled(false);

      getBtnManager().btnBillExecAudit.setEnabled(false);

      getBtnManager().btnBillExecute.setEnabled(false);

      getBtnManager().btnBillDocManage.setEnabled(false);

      getBtnManager().btnBillContractClass.setEnabled(false);
    } else {
      voCur = getOrderDataVOAt(getBufferVOManager().getVOPos());

      getBtnManager().btnBillDocManage.setEnabled(true);

      getBtnManager().btnBillContractClass.setEnabled(true);

      getBtnManager().btnBillModify.setEnabled(voCur.isCanBeModified());
      getBtnManager().btnBillAnnul.setEnabled(voCur.isCanBeAnnuled());

      if ((getBtnManager().btnBillExecute.getChildButtonGroup() == null) || (getBtnManager().btnBillExecute.getChildButtonGroup().length == 0))
      {
        getBtnManager().btnBillExecute.setEnabled(false);
      } else {
        getBtnManager().btnBillExecute.setEnabled(true);
        getBtnManager().btnBillExecAudit.setEnabled(voCur.isCanBeAudited());
        getBtnManager().btnBillExecUnAudit.setEnabled(voCur.isCanBeUnAudited());
        getBtnManager().btnBillExecClose.setEnabled(voCur.isCanBeClosed());
        getBtnManager().btnBillExecOpen.setEnabled(voCur.isCanBeOpened());
      }

      if (getBufferLength() == 1)
      {
        getBtnManager().btnBillFirst.setEnabled(false);
        getBtnManager().btnBillPrevious.setEnabled(false);
        getBtnManager().btnBillNext.setEnabled(false);
        getBtnManager().btnBillLast.setEnabled(false);
      }
      else if (getBufferVOManager().getVOPos() == 0) {
        getBtnManager().btnBillFirst.setEnabled(false);
        getBtnManager().btnBillPrevious.setEnabled(false);
        getBtnManager().btnBillNext.setEnabled(true);
        getBtnManager().btnBillLast.setEnabled(true);
      }
      else if (getBufferVOManager().getVOPos() == getBufferLength() - 1) {
        getBtnManager().btnBillFirst.setEnabled(true);
        getBtnManager().btnBillPrevious.setEnabled(true);
        getBtnManager().btnBillNext.setEnabled(false);
        getBtnManager().btnBillLast.setEnabled(false);
      }
      else
      {
        getBtnManager().btnBillFirst.setEnabled(true);
        getBtnManager().btnBillPrevious.setEnabled(true);
        getBtnManager().btnBillNext.setEnabled(true);
        getBtnManager().btnBillLast.setEnabled(true);
      }

    }

    getBtnManager().btnBillAssist.setEnabled(true);

    if (isEverQueryed())
      getBtnManager().btnBillRefresh.setEnabled(true);
    else {
      getBtnManager().btnBillRefresh.setEnabled(false);
    }
    boolean bEnabled = true;
    if (getBufferLength() == 0) {
      bEnabled = false;
    }
    getBtnManager().btnBillCopyBill.setEnabled(bEnabled);

    boolean bReturn = false;
    if ((voCur != null) && (voCur.getHeadVO() != null) && (voCur.getHeadVO().getBreturn() != null)) {
      bReturn = voCur.getHeadVO().getBreturn().booleanValue();
    }
    getBtnManager().btnBillReceivePlan.setEnabled(bEnabled & !bReturn);
    getBtnManager().btnBillPrePay.setEnabled(bEnabled & !bReturn);

    getBtnManager().btnBillLocate.setEnabled(bEnabled);
    getBtnManager().btnBillGrossProfit.setEnabled(bEnabled);
    getBtnManager().btnBillUsenum.setEnabled(bEnabled);
    getBtnManager().btnBillSaleNum.setEnabled(bEnabled);
    getBtnManager().btnBillInvSetQuery.setEnabled(bEnabled);
    getBtnManager().btnBillAp.setEnabled(bEnabled);
    getBtnManager().btnBillVendor.setEnabled(bEnabled);
    getBtnManager().btnBillInvReplace.setEnabled(bEnabled);
    getBtnManager().btnBillStatusQry.setEnabled(bEnabled);
    getBtnManager().btnBillLnkQry.setEnabled(bEnabled);
    getBtnManager().btnBillCombin.setEnabled(bEnabled);

    if (getBufferLength() == 0) {
      getBtnManager().btnBillPayExecStat.setEnabled(false);
    } else {
      Integer iStatus = getOrderViewVOAt(getBufferVOManager().getVOPos()).getHeadVO().getForderstatus();
      if ((iStatus.compareTo(BillStatus.AUDITED) == 0) || (iStatus.compareTo(BillStatus.OUTPUT) == 0))
      {
        getBtnManager().btnBillPayExecStat.setEnabled(true);
      }
      else getBtnManager().btnBillPayExecStat.setEnabled(false);

    }

    if (getBufferLength() == 0) {
      getBtnManager().btnBillPrint.setEnabled(false);
      getBtnManager().btnBillPrintPreview.setEnabled(false);
    } else {
      getBtnManager().btnBillPrint.setEnabled(true);
      getBtnManager().btnBillPrintPreview.setEnabled(true);
    }

    setButtonsState_SendToAudit();

    getPoCardPanel().setBodyMenuShow(true);

    if ((this instanceof IBillExtendFun)) {
      ((IBillExtendFun)this).setExtendBtnsStat(1);
    }

    updateButtonsAll();
  }

  private void setButtonsState_SendToAudit()
  {
    OrderVO voCur = getOrderDataVOAt(getBufferVOManager().getVOPos());

    if (((voCur == null) || (getPoCardPanel().getHeadItem("corderid").getValue() == null) || (getPoCardPanel().getHeadItem("corderid").getValue().trim().equals(""))) && (getCurOperState() == 1))
    {
      String sBillType = "21";
      String sPk_corp = PoPublicUIClass.getLoginPk_corp();
      String sBizType = getPoCardPanel().getHeadItem("cbiztype").getValue();
      String sOper = PoPublicUIClass.getLoginUser();
      boolean bEnabled = BusiBillManageTool.isNeedSendToAudit(sBillType, sPk_corp, sBizType, null, sOper);
      getBtnManager().btnBillSendToAudit.setEnabled(bEnabled);
      return;
    }

    if (getBufferLength() == 0) {
      getBtnManager().btnBillSendToAudit.setEnabled(false);
    }
    else if ((getCurOperState() == 1) && (voCur.getHeadVO() != null) && (BillStatus.AUDITFAIL.equals(voCur.getHeadVO().getForderstatus())))
      getBtnManager().btnBillSendToAudit.setEnabled(true);
    else
      getBtnManager().btnBillSendToAudit.setEnabled(PuTool.isNeedSendToAudit("21", voCur));
  }

  protected void setButtonsStateEdit()
  {
    if (getBtnManager().btnBillExecAudit != null) {
      getBtnManager().btnBillExecAudit.setEnabled(false);
    }

    if (getBtnManager().btnBillRevise != null) {
      getBtnManager().btnBillRevise.setEnabled(false);
    }

    if (getBtnManager().btnListCancelTransform != null) {
      getBtnManager().btnListCancelTransform.setVisible(false);
    }

    getBtnManager().btnBillReplenish.setEnabled(false);

    getBtnManager().btnBillBusitype.setEnabled(false);

    getBtnManager().btnBillAdd.setEnabled(false);

    getBtnManager().btnBillModify.setEnabled(false);

    getBtnManager().btnBillExecute.setEnabled(true);
    getBtnManager().btnBillExecUnAudit.setEnabled(false);
    getBtnManager().btnBillExecClose.setEnabled(false);
    getBtnManager().btnBillExecOpen.setEnabled(false);

    getBtnManager().btnBillSave.setEnabled(true);

    getBtnManager().btnBillSendToAudit.setEnabled(true);

    getBtnManager().btnBillRowOperation.setEnabled(true);

    int iRow = getPoCardPanel().getRowCount();
    if (iRow <= 0)
      getBtnManager().btnBillDelRow.setEnabled(false);
    else {
      getBtnManager().btnBillDelRow.setEnabled(true);
    }

    getBtnManager().btnBillAddRow.setEnabled(true);
    getBtnManager().btnBillCopyRow.setEnabled(true);
    getBtnManager().btnBillInsertRow.setEnabled(true);
    getBtnManager().btnBillPasteRow.setEnabled(true);

    getPoCardPanel().setBodyMenuShow(true);
    getPoCardPanel().getAddLineMenuItem().setEnabled(true);
    getPoCardPanel().getInsertLineMenuItem().setEnabled(true);
    getPoCardPanel().getCopyLineMenuItem().setEnabled(true);
    getPoCardPanel().getPasteLineMenuItem().setEnabled(true);
    getPoCardPanel().getPasteLineToTailMenuItem().setEnabled(true);
    getPoCardPanel().getDelLineMenuItem().setEnabled(true);

    getBtnManager().btnBillCancel.setEnabled(true);

    getBtnManager().btnBillAnnul.setEnabled(false);

    getBtnManager().btnBillBrowse.setEnabled(false);

    getBtnManager().btnBillQuery.setEnabled(false);

    getBtnManager().btnBillShift.setEnabled(false);

    getBtnManager().btnBillFirst.setEnabled(false);
    getBtnManager().btnBillPrevious.setEnabled(false);
    getBtnManager().btnBillNext.setEnabled(false);
    getBtnManager().btnBillLast.setEnabled(false);

    getBtnManager().btnBillAssist.setEnabled(true);

    getBtnManager().btnBillCopyBill.setEnabled(false);
    getBtnManager().btnBillReceivePlan.setEnabled(false);
    getBtnManager().btnBillPrePay.setEnabled(false);
    getBtnManager().btnBillRefresh.setEnabled(false);
    getBtnManager().btnBillLocate.setEnabled(false);
    getBtnManager().btnBillGrossProfit.setEnabled(true);
    getBtnManager().btnBillUsenum.setEnabled(true);
    getBtnManager().btnBillSaleNum.setEnabled(true);
    getBtnManager().btnBillInvSetQuery.setEnabled(true);
    getBtnManager().btnBillAp.setEnabled(true);
    getBtnManager().btnBillPayExecStat.setEnabled(false);
    getBtnManager().btnBillPrint.setEnabled(false);
    getBtnManager().btnBillPrintPreview.setEnabled(false);
    getBtnManager().btnBillCombin.setEnabled(false);
    getBtnManager().btnBillVendor.setEnabled(true);
    getBtnManager().btnBillInvReplace.setEnabled(true);
    getBtnManager().btnBillStatusQry.setEnabled(false);

    getBtnManager().btnBillContractClass.setEnabled(false);

    if ((getPoCardPanel().getHeadItem("corderid").getValue() == null) || (getPoCardPanel().getHeadItem("corderid").getValue().toString().equals("")))
    {
      getBtnManager().btnBillDocManage.setEnabled(false);
    }
    else getBtnManager().btnBillDocManage.setEnabled(true);

    setPopMenuBtnsEnable();

    setButtonsState_SendToAudit();

    if ((this instanceof IBillExtendFun)) {
      ((IBillExtendFun)this).setExtendBtnsStat(2);
    }
    updateButtonsAll();
  }

  private void setButtonsStateInit()
  {
    getBtnManager().btnbillReturn.setVisible(false);

    getBtnManager().btnBillShift.setName(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000464"));

    getBtnManager().btnBillReplenish.setEnabled(true);
    getBtnManager().btnBillRc.setEnabled(true);
    getBtnManager().btnBillIc.setEnabled(true);

    getBtnManager().btnListSelectAll.setEnabled(false);

    getBtnManager().btnListDeselectAll.setEnabled(false);

    getBtnManager().btnListCancelTransform.setVisible(false);

    getBtnManager().btnBillBusitype.setEnabled(true);

    getBtnManager().btnBillAdd.setEnabled(true);

    getBtnManager().btnBillModify.setEnabled(false);

    getBtnManager().btnBillSave.setEnabled(false);

    getBtnManager().btnBillRowOperation.setEnabled(false);

    getBtnManager().btnBillCancel.setEnabled(false);

    getBtnManager().btnBillExecAudit.setEnabled(false);

    getBtnManager().btnBillExecute.setEnabled(false);

    getBtnManager().btnBillAnnul.setEnabled(false);

    getBtnManager().btnBillBrowse.setEnabled(true);

    getBtnManager().btnBillQuery.setEnabled(true);

    getBtnManager().btnBillFirst.setEnabled(false);
    getBtnManager().btnBillPrevious.setEnabled(false);
    getBtnManager().btnBillNext.setEnabled(false);
    getBtnManager().btnBillLast.setEnabled(false);

    getBtnManager().btnBillShift.setEnabled(true);

    getBtnManager().btnBillDocManage.setEnabled(false);

    getBtnManager().btnBillAssist.setEnabled(true);

    getBtnManager().btnBillCopyBill.setEnabled(false);
    getBtnManager().btnBillReceivePlan.setEnabled(false);
    getBtnManager().btnBillPrePay.setEnabled(false);
    getBtnManager().btnBillRefresh.setEnabled(false);
    getBtnManager().btnBillSendToAudit.setEnabled(false);
    getBtnManager().btnBillLocate.setEnabled(false);
    getBtnManager().btnBillGrossProfit.setEnabled(false);
    getBtnManager().btnBillUsenum.setEnabled(false);
    getBtnManager().btnBillSaleNum.setEnabled(false);
    getBtnManager().btnBillInvSetQuery.setEnabled(false);
    getBtnManager().btnBillAp.setEnabled(false);
    getBtnManager().btnBillPrint.setEnabled(false);
    getBtnManager().btnBillPrintPreview.setEnabled(false);
    getBtnManager().btnBillCombin.setEnabled(false);
    getBtnManager().btnBillVendor.setEnabled(false);
    getBtnManager().btnBillInvReplace.setEnabled(false);
    getBtnManager().btnBillStatusQry.setEnabled(false);
    getBtnManager().btnBillLnkQry.setEnabled(false);
    getBtnManager().btnBillPayExecStat.setEnabled(false);

    getBtnManager().btnBillContractClass.setEnabled(false);

    PuTool.initBusiAddBtns(getBtnManager().btnBillBusitype, getBtnManager().btnBillAdd, getPoCardPanel().getBillType(), getCorpPrimaryKey());
    if ((getBtnManager().btnBillBusitype != null) && (getBtnManager().btnBillBusitype.getChildCount() > 0))
    {
      setCurBizeType(getBtnManager().btnBillBusitype.getChildButtonGroup()[0].getTag());
    }

    if ((this instanceof IBillExtendFun)) {
      ((IBillExtendFun)this).setExtendBtnsStat(0);
    }
    updateButtonsAll();
  }

  public void setButtonsStateList()
  {
    if (isFromOtherBill())
      setButtonsStateListFromBills();
    else {
      setButtonsStateListNormal();
    }

    updateButtonsAll();
  }

  private void setButtonsStateListFromBills()
  {
    getBtnManager().btnBillBusitype.setEnabled(false);
    getBtnManager().btnBillAdd.setEnabled(false);
    getBtnManager().btnBillSave.setEnabled(false);
    getBtnManager().btnBillSendToAudit.setEnabled(false);
    getBtnManager().btnBillCancel.setEnabled(false);
    getBtnManager().btnBillRowOperation.setEnabled(false);
    getBtnManager().btnBillLocate.setEnabled(false);
    getBtnManager().btnBillFirst.setEnabled(false);
    getBtnManager().btnBillPrevious.setEnabled(false);
    getBtnManager().btnBillNext.setEnabled(false);
    getBtnManager().btnBillLast.setEnabled(false);

    getBtnManager().btnBillCombin.setEnabled(false);

    getBtnManager().btnBillAp.setEnabled(false);
    getBtnManager().btnBillGrossProfit.setEnabled(false);
    getBtnManager().btnBillInvReplace.setEnabled(false);
    getBtnManager().btnBillInvSetQuery.setEnabled(false);
    getBtnManager().btnBillLnkQry.setEnabled(false);
    getBtnManager().btnBillPayExecStat.setEnabled(false);
    getBtnManager().btnBillPrePay.setEnabled(false);
    getBtnManager().btnBillReceivePlan.setEnabled(false);
    getBtnManager().btnBillSaleNum.setEnabled(false);
    getBtnManager().btnBillStatusQry.setEnabled(false);
    getBtnManager().btnBillVendor.setEnabled(false);

    getBtnManager().btnBillCopyBill.setEnabled(false);
    getBtnManager().btnBillExecAudit.setEnabled(false);
    getBtnManager().btnBillPrint.setEnabled(false);
    getBtnManager().btnBillPrintPreview.setEnabled(false);

    int iSelectedCount = getPoListPanel().getHeadSelectedCount();

    if ((iSelectedCount == 0) || (getBufferLength() < 1)) {
      getBtnManager().btnListModify.setEnabled(false);
      getBtnManager().btnListDeselectAll.setEnabled(false);

      if (getBufferLength() == 1)
        getBtnManager().btnListSelectAll.setEnabled(true);
      else
        getBtnManager().btnListSelectAll.setEnabled(false);
    }
    else {
      if (iSelectedCount == 1) {
        getBtnManager().btnListModify.setEnabled(true);
        getBtnManager().btnListUsenum.setEnabled(true);
      } else {
        getBtnManager().btnListModify.setEnabled(false);
        getBtnManager().btnListUsenum.setEnabled(false);
      }

      getBtnManager().btnListDeselectAll.setEnabled(true);
      getBtnManager().btnListSelectAll.setEnabled(false);
    }

    getBtnManager().btnListQuery.setEnabled(false);

    getBtnManager().btnListAnnul.setEnabled(false);

    getBtnManager().btnListRefresh.setEnabled(false);

    getBtnManager().btnListShift.setEnabled(false);

    getBtnManager().btnListDocManage.setEnabled(false);

    getBtnManager().btnListCancelTransform.setVisible(true);
    getBtnManager().btnListCancelTransform.setEnabled(true);

    getBtnManager().btnListAssist.setEnabled(false);

    if ((this instanceof IBillExtendFun)) {
      ((IBillExtendFun)this).setExtendBtnsStat(4);
    }

    updateButtonsAll();
  }

  private void setButtonsStateListNormal()
  {
    getBtnManager().btnBillShift.setName(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000463"));

    getBtnManager().btnBillReplenish.setEnabled(false);

    getBtnManager().btnBillBusitype.setEnabled(false);
    getBtnManager().btnBillAdd.setEnabled(false);
    getBtnManager().btnBillSave.setEnabled(false);
    getBtnManager().btnBillSendToAudit.setEnabled(false);
    getBtnManager().btnBillCancel.setEnabled(false);
    getBtnManager().btnBillRowOperation.setEnabled(false);
    getBtnManager().btnBillLocate.setEnabled(false);
    getBtnManager().btnBillFirst.setEnabled(false);
    getBtnManager().btnBillPrevious.setEnabled(false);
    getBtnManager().btnBillNext.setEnabled(false);
    getBtnManager().btnBillLast.setEnabled(false);

    getBtnManager().btnBillCombin.setEnabled(false);

    getBtnManager().btnBillAp.setEnabled(false);
    getBtnManager().btnBillGrossProfit.setEnabled(false);
    getBtnManager().btnBillInvReplace.setEnabled(false);
    getBtnManager().btnBillInvSetQuery.setEnabled(false);
    getBtnManager().btnBillLnkQry.setEnabled(false);
    getBtnManager().btnBillPayExecStat.setEnabled(false);
    getBtnManager().btnBillPrePay.setEnabled(false);
    getBtnManager().btnBillReceivePlan.setEnabled(false);
    getBtnManager().btnBillSaleNum.setEnabled(false);
    getBtnManager().btnBillStatusQry.setEnabled(false);
    getBtnManager().btnBillVendor.setEnabled(false);

    getBtnManager().btnBillExecClose.setEnabled(false);
    getBtnManager().btnBillExecOpen.setEnabled(false);

    int iSelectedCount = getPoListPanel().getHeadSelectedCount();

    if (getBufferLength() == 0)
    {
      getBtnManager().btnBillRevise.setEnabled(false);
      getBtnManager().btnListSelectAll.setEnabled(false);
      getBtnManager().btnListDeselectAll.setEnabled(false);
      getBtnManager().btnListAnnul.setEnabled(false);
      getBtnManager().btnListModify.setEnabled(false);
      getBtnManager().btnListShift.setEnabled(true);
      getBtnManager().btnListDocManage.setEnabled(false);
      getBtnManager().btnListUsenum.setEnabled(false);

      getBtnManager().btnBillExecAudit.setEnabled(false);
      getBtnManager().btnBillExecute.setEnabled(false);
      getBtnManager().btnBillCopyBill.setEnabled(false);
    }
    else
    {
      if (iSelectedCount == 0)
      {
        getBtnManager().btnBillRevise.setEnabled(false);
        getBtnManager().btnListSelectAll.setEnabled(true);
        getBtnManager().btnListDeselectAll.setEnabled(false);
        getBtnManager().btnListAnnul.setEnabled(false);
        getBtnManager().btnListModify.setEnabled(false);
        getBtnManager().btnListShift.setEnabled(false);
        getBtnManager().btnListUsenum.setEnabled(false);
        getBtnManager().btnBillExecAudit.setEnabled(false);
        getBtnManager().btnBillExecute.setEnabled(false);
        getBtnManager().btnBillCopyBill.setEnabled(false);

        getBtnManager().btnListPrint.setEnabled(false);
        getBtnManager().btnListPreview.setEnabled(false);
      }
      else {
        getBtnManager().btnListDeselectAll.setEnabled(true);

        if (iSelectedCount == getBufferLength())
          getBtnManager().btnListSelectAll.setEnabled(false);
        else {
          getBtnManager().btnListSelectAll.setEnabled(true);
        }

        getBtnManager().btnBillExecute.setEnabled(true);

        if (iSelectedCount == 1)
        {
          getBtnManager().btnBillRevise.setEnabled(true);

          getBtnManager().btnBillCopyBill.setEnabled(true);

          OrderVO voCur = getBufferVOManager().getVOAt_LoadItemNo(getPoListPanel().getHeadSelectedVOPoses()[0].intValue());
          if ((voCur.getBodyVO() == null) || (voCur.getBodyVO().length == 0))
          {
            getBtnManager().btnListShift.setEnabled(false);
          }
          else getBtnManager().btnListShift.setEnabled(true);

          int iBillStatus = voCur.getHeadVO().getForderstatus().intValue();
          if (((iBillStatus != BillStatus.FREE.intValue()) && (iBillStatus != BillStatus.AUDITFAIL.intValue())) || (voCur.getBodyVO() == null) || (voCur.getBodyVO().length == 0))
          {
            getBtnManager().btnListModify.setEnabled(false);
            getBtnManager().btnListAnnul.setEnabled(false);
          } else {
            getBtnManager().btnListModify.setEnabled(true);
            getBtnManager().btnListAnnul.setEnabled(true);
          }
          getBtnManager().btnListUsenum.setEnabled(true);

          getBtnManager().btnBillExecAudit.setEnabled(voCur.isCanBeAudited());
          getBtnManager().btnBillExecUnAudit.setEnabled(voCur.isCanBeUnAudited());
        }
        else
        {
          getBtnManager().btnBillCopyBill.setEnabled(false);

          getBtnManager().btnBillExecAudit.setEnabled(true);
          getBtnManager().btnBillExecUnAudit.setEnabled(true);
          getBtnManager().btnListShift.setEnabled(false);
          getBtnManager().btnListModify.setEnabled(false);
          getBtnManager().btnListAnnul.setEnabled(true);

          getBtnManager().btnListUsenum.setEnabled(false);
        }

        getBtnManager().btnListAssist.setEnabled(true);
        getBtnManager().btnListPrint.setEnabled(true);
        getBtnManager().btnListPreview.setEnabled(true);
      }

      if (iSelectedCount == 0)
        getBtnManager().btnListDocManage.setEnabled(false);
      else {
        getBtnManager().btnListDocManage.setEnabled(true);
      }
    }

    getBtnManager().btnListQuery.setEnabled(true);

    if (isEverQueryed())
      getBtnManager().btnListRefresh.setEnabled(true);
    else {
      getBtnManager().btnListRefresh.setEnabled(false);
    }

    getBtnManager().btnListCancelTransform.setVisible(false);

    if ((this instanceof IBillExtendFun)) {
      ((IBillExtendFun)this).setExtendBtnsStat(3);
    }
    updateButtonsAll();
  }

  protected void updateButtonsAll()
  {
    int iLen = getBtnManager().getBtnTree(this).getButtonArray().length;
    for (int i = 0; i < iLen; i++)
    {
      update(getBtnManager().getBtnTree(this).getButtonArray()[i]);
    }
    super.updateButtons();
  }

  private void update(ButtonObject bo)
  {
    updateButton(bo);
    if (bo.getChildCount() > 0) {
      int i = 0; for (int len = bo.getChildCount(); i < len; i++)
        update(bo.getChildButtonGroup()[i]);
    }
  }

  private void setCupsourcebilltype(String sNewBillType)
  {
    this.m_cupsourcebilltype = sNewBillType;
  }

  private void setCurBizeType(String newCurBizeType)
  {
    this.m_cbiztype = newCurBizeType;
  }

  protected void setCurOperState(int newCurOperState)
  {
    this.m_nCurOperState = newCurOperState;
  }

  protected void setCurPk_corp(String sPk_corp)
  {
    this.m_pk_corp = sPk_corp;
  }

  private void setEverQueryed(boolean newQueryed)
  {
    this.m_bEverQueryed = newQueryed;
  }

  private void setHmapUpSourcePrayTs(HashMap hmapNew)
  {
    this.m_hmapUpSourcePrayTs = hmapNew;
  }

  private void setIsFromOtherBill(boolean bValue)
  {
    this.m_bFromOtherBill = bValue;
  }

  private void setOrgBufferLen(int iNewLen)
  {
    this.m_iOrgBufferLen = iNewLen;
  }

  private void shiftGrossProfitToPoCard()
  {
    remove(getGrossProfitPanel());
    add(getPoCardPanel(), "Center");

    setCurOperState(this.m_nflagSave);
    setButtons(getBtnManager().getBtnaBill(this));

    if (getCurOperState() == 1)
      showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000041"));
    else {
      showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000042"));
    }
    updateUI();
  }

  protected abstract PoToftPanelButton getBtnManager();

  protected PoVOBufferManager getBufferVOManager()
  {
    if (this.m_bufPoVO == null) {
      this.m_bufPoVO = new PoVOBufferManager();
    }

    return this.m_bufPoVO;
  }

  private ATPForOneInvMulCorpUI getDlgAtp()
  {
    if (this.m_atpDlg == null) {
      this.m_atpDlg = new ATPForOneInvMulCorpUI(this);
    }
    return this.m_atpDlg;
  }

  protected abstract OrderVO[] getQueriedVOHead()
    throws Exception;

  protected boolean isInitStateBill()
  {
    return true;
  }

  protected void onBillAuditStatusQuery()
  {
    if (getBufferLength() > 0)
    {
      FlowStateDlg approvestatedlg = new FlowStateDlg(this, "21", getOrderDataVOAt(getBufferVOManager().getVOPos()).getHeadVO().getPrimaryKey());

      approvestatedlg.showModal();
    }
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH035"));
  }

  public static boolean onDiscard(ToftPanel pnlToft, OrderVO[] voaDelete)
  {
    if (voaDelete == null) {
      return false;
    }

    int iDeletedLen = voaDelete.length;

    for (int i = 0; i < iDeletedLen; i++) {
      Integer iStatus = voaDelete[i].getHeadVO().getForderstatus();
      if ((iStatus.compareTo(BillStatus.FREE) != 0) && (iStatus.compareTo(BillStatus.AUDITFAIL) != 0))
      {
        if (iDeletedLen == 1)
          MessageDialog.showHintDlg(pnlToft, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270"), NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000078"));
        else {
          MessageDialog.showHintDlg(pnlToft, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270"), NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000079"));
        }
        return false;
      }

      voaDelete[i].setStatus(3);
      try
      {
        voaDelete[i].validateDiscard();
      } catch (ValidationException e) {
        PuTool.outException(pnlToft, e);
        return false;
      }

    }

    PoPublicUIClass.setCuserId(voaDelete);

    int iRet = MessageDialog.showYesNoDlg(pnlToft, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000219"), NCLangRes.getInstance().getStrByID("common", "4004COMMON000000069"), 8);
    if (iRet != 4) {
      return false;
    }

    pnlToft.showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000081"));

    boolean bContinue = true;
    boolean bException = false;

    while (bContinue) {
      pnlToft.showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000082"));
      try {
        PfUtilClient.processBatch("DISCARD", "21", PoPublicUIClass.getLoginDate().toString(), voaDelete);

        bContinue = false;
      } catch (Exception e) {
        if ((e instanceof SaveHintException))
        {
          int iMesRet = pnlToft.showYesNoMessage(e.getMessage());
          if (iMesRet == 4)
          {
            bContinue = true;

            for (int i = 0; i < iDeletedLen; i++)
              voaDelete[i].setFirstTime(false);
          }
          else {
            bException = true;
            break;
          }
        } else {
          if (((e instanceof BusinessException)) || ((e instanceof ValidationException)))
          {
            pnlToft.showWarningMessage(e.getMessage());
            bException = true;
            break;
          }
          pnlToft.showErrorMessage(OrderPubVO.returnHintMsgWhenNull(e.getMessage()));
          bException = true;
          break;
        }
      }

    }

    if (bException) {
      for (int i = 0; i < iDeletedLen; i++) {
        voaDelete[i].setFirstTime(true);
        voaDelete[i].setStatus(0);
      }
      return false;
    }
    pnlToft.showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000068"));
    return true;
  }

  public static OrderVO onSave(ToftPanel pnlToft, PoCardPanel pnlBill, OrderVO voSaved)
  {
    Timer timeDebug = new Timer();
    timeDebug.start();

    if ((pnlToft == null) || (pnlBill == null) || (voSaved == null)) {
      return null;
    }

    boolean bContinue = true;

    int iCurOperType = PuPubVO.getString_TrimZeroLenAsNull(voSaved.getHeadVO().getPrimaryKey()) == null ? 0 : 1;

    ArrayList userObj = new ArrayList();
    userObj.add(PoPublicUIClass.getLoginUser());
    userObj.add(PoPublicUIClass.getLoginDate());
    while (bContinue) {
      pnlToft.showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000082"));
      try {
        Object oRet = PfUtilClient.processActionNoSendMessage(pnlToft, "SAVEBASE", "21", PoPublicUIClass.getLoginDate().toString(), voSaved, userObj, null, null);

        if (oRet == null) {
          return null;
        }

        ArrayList arraylist = (ArrayList)oRet;

        voSaved = (OrderVO)arraylist.get(1);

        bContinue = false;
      } catch (Exception e) {
        PuTool.printException(e);

        if ((e instanceof SaveHintException))
        {
          int iRet = pnlToft.showYesNoMessage(e.getMessage());
          if (iRet == 4)
          {
            bContinue = true;

            voSaved.setFirstTime(false);
          } else {
            pnlToft.showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000010"));
            return null;
          }
        } else if ((e instanceof StockPresentException))
        {
          int iRet = pnlToft.showYesNoMessage(e.getMessage());
          if (iRet == 4)
          {
            bContinue = true;

            voSaved.setFirstTimeSP(false);
          } else {
            pnlToft.showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000010"));
            return null;
          }
        } else if ((e instanceof RwtPoToPrException))
        {
          int iRet = pnlToft.showYesNoMessage(e.getMessage());
          if (iRet == 4)
          {
            bContinue = true;

            voSaved.setFirstTime(false);
          } else {
            pnlToft.showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000010"));
            return null;
          }
        } else if ((e instanceof MaxPriceException))
        {
          int iRet = pnlToft.showYesNoMessage(e.getMessage());
          if (iRet == 4)
          {
            bContinue = true;

            voSaved.setFirstTimePrice(false);
          } else {
            pnlToft.showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000010"));
            return null;
          }
        } else if ((e instanceof MaxStockException))
        {
          int iRet = pnlToft.showYesNoMessage(e.getMessage());
          if (iRet == 4)
          {
            bContinue = true;

            voSaved.setFirstTimeStock(false);
          } else {
            pnlToft.showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000010"));
            return null;
          }
        }
        else {
          if (((e instanceof BusinessException)) || ((e instanceof ValidationException)) || ((e.getMessage() != null) && ((e.getMessage().indexOf(NCLangRes.getInstance().getStrByID("common", "UC000-0000275")) >= 0) || (e.getMessage().indexOf(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000083")) >= 0) || (e.getMessage().indexOf(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000084")) >= 0) || (e.getMessage().indexOf(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000085")) >= 0))))
          {
            pnlToft.showWarningMessage(e.getMessage());
          }
          else pnlToft.showErrorMessage(OrderPubVO.returnHintMsgWhenNull(e.getMessage()));

          pnlToft.showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000010"));
          return null;
        }
      }
    }
    pnlToft.showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000086"));
    timeDebug.addExecutePhase("订单保存");

    voSaved.setStatus(0);

    OrderVO voLightRefreshed = null;
    try
    {
      voLightRefreshed = OrderHelper.queryOrderVOLight(voSaved.getHeadVO().getCorderid(), "SAVE");
      timeDebug.addExecutePhase("重新查询订单");
    } catch (Exception e) {
      PuTool.printException(e);
      pnlToft.showWarningMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000087"));
      voSaved = null;
    }
    timeDebug.showAllExecutePhase("订单单纯UI保存");

    boolean bRefreshSucc = voSaved.setLastestVo(voLightRefreshed, iCurOperType);

    if (!bRefreshSucc) {
      try {
        voSaved = OrderHelper.queryOrderVOByKey(voSaved.getHeadVO().getCorderid());
        timeDebug.addExecutePhase("重新查询订单");
      } catch (Exception e) {
        PuTool.printException(e);
        pnlToft.showWarningMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000087"));
        voSaved = null;
      }
    }

    return voSaved;
  }

  public static boolean onSendToAudit(ToftPanel pnlToft, OrderVO voOrder)
  {
    pnlToft.showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000055"));
    Timer timeDebug = new Timer();
    timeDebug.start();

    timeDebug.addExecutePhase("送审");
    timeDebug.addExecutePhase("采购订单送审");

    pnlToft.showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000042"));
    return true;
  }

  protected void processAfterChange(String sUpBillType, AggregatedValueObject[] voaSourceVO)
  {
    OrderVO[] voaRet = PoChangeUI.getChangedVOs(sUpBillType, voaSourceVO, this, getCurBizeType());

    if (voaRet == null) {
      setCurPk_corp(PoPublicUIClass.getLoginPk_corp());
      return;
    }

    setIsFromOtherBill(true);
    setCupsourcebilltype(sUpBillType);

    setCurPk_corp(voaRet[0].getHeadVO().getPk_corp());

    getBufferVOFrmBill().resetVOs(getBufferVOManager());

    getBufferVOManager().resetVOs(voaRet);
    getBufferVOManager().setVOPos(0);
    setCurOperState(1);
    onBillList();

    setButtonsStateList();

    showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000042"));
  }

  public boolean onClosing()
  {
    if (getCurOperState() == 1) {
      int iRet = MessageDialog.showYesNoCancelDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270"), NCLangRes.getInstance().getStrByID("common", "UCH001"));

      if (iRet == 4) {
        return onBillSave();
      }

      return iRet == 8;
    }

    return true;
  }
  
  
	/*
	 * edit by yqq 卡片界面导出xml  2016-11-02
	 */
	 
	public void onXmlch() {

		String sVorderCode = null;
	    String sFilePathDir = null;
	
			// 打开文件
			if (getChooser().showSaveDialog(this) == javax.swing.JFileChooser.CANCEL_OPTION) return;
			sFilePathDir = getChooser().getSelectedFile().toString();
		
		if (sFilePathDir == null) {
			showHintMessage("请输入文件名保存!");
			return;
		}

		try {

			// 依当前是列表还是卡片界面而定导出内容
			if (m_iCurPanel == PoCardPanel) { // 浏览
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000119")/** @res "正在导出，请稍候..."*/);
				
				// 准备数据
			    OrderVO voBill = (OrderVO)getPoCardPanel().getBillValueVO(OrderVO.class.getName(), OrderHeaderVO.class.getName(), OrderItemVO.class.getName());

				if (voBill.getParentVO() == null) {
					voBill.setParentVO(new InvoiceHeaderVO());
				}
				if ((voBill.getChildrenVO() == null) || (voBill.getChildrenVO().length == 0) || (voBill.getChildrenVO()[0] == null)) {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000049")/** @res* "请先查询或录入单据。"*/);
		//			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH003")/** @res "请选择要处理的数据！"*/);
					return;
				}


					sVorderCode = voBill.getHeadVO().getVordercode();
					onExportXML(new OrderVO[] {voBill}, sFilePathDir);//调用外部平台接口方法
					showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000120")/** @res* "导出完成"*/);
					return;				
			}			
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000121")/* @res "导出出错" */);
			showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000121")/* @res "导出出错" */
					+ nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000330")/* @res "：" */
					+ e.getMessage() + "," + nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000331")/* @res "文件路径" */
					+ ":" + sFilePathDir);
		}
	}  

// end by yqq 卡片界面导出xml  2016-11-02
	
	protected javax.swing.JFileChooser m_chooser = null;	
	protected javax.swing.JFileChooser getChooser() {
		if (m_chooser == null) {
			m_chooser = new JFileChooser();
			m_chooser.setDialogType(JFileChooser.SAVE_DIALOG);
		}
		return m_chooser;
	} 

	public void onExportXML(OrderVO[] orderVOs, String filename) {
		if (orderVOs == null || orderVOs.length <= 0 || filename == null) return;
		try {

			MessageDialog.showInputDlg(this, "请输入外部系统编码", "请输入外部系统编码:", "21", 5);

			IPFxxEJBService export = (IPFxxEJBService) NCLocator.getInstance().lookup(IPFxxEJBService.class.getName());
			Document outdocs = export.exportBills(orderVOs, getClientEnvironment().getAccount().getAccountCode(), getClientEnvironment().getCorporation().getPrimaryKey(), "21", "21");

			nc.vo.pfxx.util.FileUtils.writeDocToXMLFile(outdocs, filename);
		} catch (Exception e) {
			showErrorMessage(e.getMessage());
		}
	}
	
	/*
	 * Edit 列表导出xml 2016-11-16 yqq 
	 */
	public void onXmlchl() {

		String sVorderCode = null;
	    String sFilePathDir = null;
	
			// 打开文件
			if (getChooser().showSaveDialog(this) == javax.swing.JFileChooser.CANCEL_OPTION) return;
			sFilePathDir = getChooser().getSelectedFile().toString();
		
		if (sFilePathDir == null) {
			showHintMessage("请输入文件名保存!");
			return;
		}

		try {

			// 依当前是列表还是卡片界面而定导出内容
			if(m_iCurPanel1 == PoListPanel) {
				int iSelListHeadRowCount = getPoListPanel().getHeadTable().getSelectedRowCount();
				
				ArrayList alBill = getPrintAryList();//取得列表界面值
				
				if (alBill == null || alBill.size() <= 0 || alBill.get(0) == null) {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000049")/** @res* "请先查询或录入单据。"*/);
					return;
				}
				
				    sVorderCode = ((OrderVO) alBill.get(0)).getHeadVO().getVordercode();
					onExportXML((OrderVO[]) alBill.toArray(new OrderVO[alBill.size()]), sFilePathDir); //调用外部平台接口方法
					showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000120")/** @res* "导出完成"*/);
					return;
			}

			
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000121")/* @res "导出出错" */);
			showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000121")/* @res "导出出错" */
					+ nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000330")/* @res "：" */
					+ e.getMessage() + "," + nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000331")/* @res "文件路径" */
					+ ":" + sFilePathDir);
		}
	} 
  //end  列表导出xml 2016-11-16 yqq 

}