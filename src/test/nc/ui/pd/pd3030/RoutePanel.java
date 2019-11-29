package nc.ui.pd.pd3030;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.io.PrintStream;
import java.util.Hashtable;
import java.util.Vector;
import nc.bs.pub.pa.Utils;
import nc.itf.pd.inner.IMMLock;
import nc.itf.pd.inner.IRt;
import nc.ui.bd.b15.DocManageDlg;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.ml.NCLangRes;
import nc.ui.mm.pub.MMToftPanel;
import nc.ui.mm.pub.pub1040.DefConfig;
import nc.ui.pd.pd4010.v5.IEditData;
import nc.ui.pd.pd4010.v5.IEditPanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UILabelLayout;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.ValueChangedEvent;
import nc.ui.pub.beans.ValueChangedListener;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.lock.LockBO_Client;
import nc.ui.pub.print.PrintEntry;
import nc.vo.mm.pub.FactoryVO;
import nc.vo.mm.pub.FunctionBase;
import nc.vo.mm.pub.pub1020.RtBAtItemVO;
import nc.vo.mm.pub.pub1020.RtBItemVO;
import nc.vo.mm.pub.pub1020.RtHeaderVO;
import nc.vo.mm.pub.pub1020.RtItemVO;
import nc.vo.mm.pub.pub1020.RtVO;
import nc.vo.mm.pub.pub1020.ZzfsHeaderVO;
import nc.vo.pd.pd3030.InitVO;
import nc.vo.pd.pd3030.RtBAtVO;
import nc.vo.pd.pd3030.RtBVO;
import nc.vo.pd.proxy.PDProxy;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.sm.UserVO;

public class RoutePanel extends UIPanel
  implements IEditPanel, ValueChangedListener
{
  private UILabel ivjLbFactory = null;

  private UILabel ivjLbMaterial = null;

  private UIRefPane ivjRfpFactory = null;

  private UIRefPane ivjRfpMaterial = null;

  private UIPanel ivjWlPanel = null;

  private UILabelLayout ivjWlPanelUILabelLayout = null;

  private UIPanel ivjEditPanel = null;

  private UIPanel ivjViewPanel = null;
  private MMToftPanel m_toft;
  private int m_nState;
  private RouteListPanel m_pnlRouteList;
  private RouteCardPanel m_pnlRouteCard;
  private RtData m_rtData;
  private String m_strLockedPk;
  private boolean m_bUpdateButtons = true;

  private PrintEntry m_printEntry = null;

  private AtDlg m_dlgAt = null;

  private RtZzfsDlg m_dlgZzfs = null;

  private CopyRtDlg m_dlgCopyRt = null;

  private RtHlDlg m_dlgGxHl = null;

  public RoutePanel()
  {
    initialize();
  }

  public RoutePanel(MMToftPanel toft)
  {
    this.m_toft = toft;
    initialize();
  }

  public RouteCardPanel getRouteCard()
  {
    if (this.m_pnlRouteCard == null) {
      this.m_pnlRouteCard = new RouteCardPanel(this.m_toft);
      this.m_pnlRouteCard.setName("RouteCardPanel");
    }
    return this.m_pnlRouteCard;
  }

  public RouteListPanel getRouteList()
  {
    if (this.m_pnlRouteList == null) {
      this.m_pnlRouteList = new RouteListPanel(this.m_toft);
      this.m_pnlRouteList.setName("RouteListPanel");
    }
    return this.m_pnlRouteList;
  }

  public boolean getCbr()
  {
    return this.m_rtData.getInitData().getCbr().equals("Y");
  }

  public IEditData getData()
  {
    return this.m_rtData;
  }

  private UIPanel getEditPanel()
  {
    if (this.ivjEditPanel == null) {
      try {
        this.ivjEditPanel = new UIPanel();
        this.ivjEditPanel.setName("EditPanel");

        this.ivjEditPanel.setLayout(new BorderLayout());
        this.ivjEditPanel.add(getRouteCard(), "Center");
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjEditPanel;
  }

  public UILabel getLbFactory()
  {
    if (this.ivjLbFactory == null) {
      try {
        this.ivjLbFactory = new UILabel();
        this.ivjLbFactory.setName("LbFactory");
        this.ivjLbFactory.setText(getStrByID("UC000-0001685"));
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjLbFactory;
  }

  public UILabel getLbMaterial()
  {
    if (this.ivjLbMaterial == null) {
      try {
        this.ivjLbMaterial = new UILabel();
        this.ivjLbMaterial.setName("LbMaterial");
        this.ivjLbMaterial.setText(getStrByID("UC000-0002911"));

        this.ivjLbMaterial.setILabelType(6);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjLbMaterial;
  }

  public UIRefPane getRfpFactory()
  {
    if (this.ivjRfpFactory == null) {
      try {
        this.ivjRfpFactory = new UIRefPane();
        this.ivjRfpFactory.setName("RfpFactory");

        this.ivjRfpFactory.setRefNodeName("库存组织");
        this.ivjRfpFactory.setEnabled(false);

        this.ivjRfpFactory.setPK(this.m_toft.getFactoryCode());
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjRfpFactory;
  }

  public UIRefPane getRfpMaterial()
  {
    if (this.ivjRfpMaterial == null) {
      try {
        this.ivjRfpMaterial = new UIRefPane();
        this.ivjRfpMaterial.setName("RfpMaterial");

        this.ivjRfpMaterial.setRefNodeName("物料档案");

        this.ivjRfpMaterial.setButtonFireEvent(true);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjRfpMaterial;
  }

  private UIPanel getViewPanel()
  {
    if (this.ivjViewPanel == null) {
      try {
        this.ivjViewPanel = new UIPanel();
        this.ivjViewPanel.setName("ViewPanel");
        this.ivjViewPanel.setLayout(new BorderLayout());
        getViewPanel().add(getWlPanel(), "North");

        getViewPanel().add(getRouteList(), "Center");
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjViewPanel;
  }

  private UIPanel getWlPanel()
  {
    if (this.ivjWlPanel == null) {
      try {
        this.ivjWlPanel = new UIPanel();
        this.ivjWlPanel.setName("WlPanel");
        this.ivjWlPanel.setPreferredSize(new Dimension(10, 35));
        this.ivjWlPanel.setLayout(getWlPanelUILabelLayout());
        this.ivjWlPanel.setMinimumSize(new Dimension(10, 10));
        getWlPanel().add(getLbMaterial(), getLbMaterial().getName());
        getWlPanel().add(getRfpMaterial(), getRfpMaterial().getName());
        getWlPanel().add(getLbFactory(), getLbFactory().getName());
        getWlPanel().add(getRfpFactory(), getRfpFactory().getName());
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjWlPanel;
  }

  private UILabelLayout getWlPanelUILabelLayout()
  {
    UILabelLayout ivjWlPanelUILabelLayout = null;
    try
    {
      ivjWlPanelUILabelLayout = new UILabelLayout();
      ivjWlPanelUILabelLayout.setTop(8);
      ivjWlPanelUILabelLayout.setRows(1);
      ivjWlPanelUILabelLayout.setColumns(5);
    } catch (Throwable ivjExc) {
      handleException(ivjExc);
    }

    return ivjWlPanelUILabelLayout;
  }

  public String getZzfsFlag()
  {
    return this.m_rtData.getInitData().getZzfsFlag();
  }

  private void handleException(Throwable exception)
  {
    System.out.println("--------- 未捕捉到的异常 ---------");
    exception.printStackTrace(System.out);
  }

  public void hideFactory()
  {
    getLbFactory().setVisible(false);
    getRfpFactory().setVisible(false);

    this.m_bUpdateButtons = false;
  }

  public void hideMaterial()
  {
    getWlPanel().setVisible(false);

    this.m_bUpdateButtons = false;
  }

  private void initialize()
  {
    try
    {
      this.m_toft.getFactoryCode();

      setName("RoutePanel");
      setLayout(new CardLayout());
      setSize(774, 419);
      add(getViewPanel(), getViewPanel().getName());
      add(getEditPanel(), getEditPanel().getName());
    } catch (Exception ivjExc) {
      handleException(ivjExc);
    }
  }

  private boolean lockPK(String pk, String tablename)
  {
    Integer flag = null;
    boolean locked = false;
    try {
      flag = PDProxy.getRemoteMMLock().lockPK(pk, this.m_toft.getUser().getPrimaryKey(), tablename, "pk_rtid");
    }
    catch (Exception e) {
      this.m_toft.reportException(e);
      this.m_toft.showErrorMessage(getStrByID("UPP-000024"));

      this.m_toft.showHintMessage(getStrByID("UPP-000025"));
      return false;
    }
    if (flag.intValue() == 0) {
      locked = false;
      this.m_toft.showHintMessage("");
      this.m_toft.showErrorMessage(getStrByID("UPP-000026"));
    }
    else if (flag.intValue() == 1) {
      locked = false;
      this.m_toft.showHintMessage("");
      this.m_toft.showErrorMessage(getStrByID("UPP-000027"));
    }
    else if (flag.intValue() == 2) {
      locked = true;
      this.m_strLockedPk = pk;
    }
    return locked;
  }

  public void materialChanged()
  {
    this.m_rtData.initRtData();

    String pk = getRfpMaterial().getRefPK();
    String wlbmid = (String)getRfpMaterial().getRefValue("bd_produce.pk_invbasdoc");

    if ((wlbmid == null) || (wlbmid.trim().length() == 0)) {
      getRouteList().clearUI();
      setState(-1);
      MessageDialog.showHintDlg(this, null, getStrByID("UPP-000028"));

      return;
    }

    try
    {
      this.m_rtData.getRtHeaders(wlbmid);
    } catch (Exception e) {
      this.m_toft.reportException(e);
      this.m_toft.showHintMessage(getStrByID("UPP-000029"));
      this.m_toft.showErrorMessage(getStrByID("UPP-000029") + ": " + e.getMessage());

      return;
    }

    getRouteList().reloadData();

    getRouteCard().updateVecGdsl(wlbmid);

    getRfpMaterial().setPK(pk);
    setState(0);
  }

  public boolean onAdd()
  {
    if (!getRouteCard().addRoute(getRfpMaterial())) {
      return false;
    }

    if (this.m_rtData.getRtCache().isEmpty())
      setState(6);
    else if (this.m_rtData.getInitData().getEcn().equals("Y"))
      setState(3);
    else {
      setState(2);
    }

    return true;
  }

  public boolean onAddLine()
  {
    return getRouteCard().onAddLine();
  }

  public boolean onCancel()
  {
    setState(0);

    return unLockPK();
  }

  public boolean onCopy()
  {
    if ((getDlgCopyRt().getRoutePanel().getRfpMaterial().getRefPK() == null) || (getDlgCopyRt().getRoutePanel().getRfpMaterial().getRefPK().equals(getRfpMaterial().getRefPK())))
    {
      getDlgCopyRt().getRoutePanel().setData((RtData)this.m_rtData.clone());
      getDlgCopyRt().getRoutePanel().getRfpMaterial().setPK(getRfpMaterial().getRefPK());
    }

    if (getDlgCopyRt().showModal() != 1) {
      return false;
    }

    RtVO rt = getDlgCopyRt().getRoutePanel().getRouteList().getSelectedRoute();

    RtHeaderVO rtHeader = (RtHeaderVO)rt.getParentVO().clone();
    rtHeader.setPk_rtid(null);
    rtHeader.setVersion(this.m_rtData.getNewVersion());
    rtHeader.setCreator(null);
    rtHeader.setCreatorName(null);
    if (this.m_rtData.getInitData().getEcn().equals("Y")) {
      rtHeader.setBblx(new Integer(1));
      rtHeader.setBBLX(FunctionBase.getQzsm(rtHeader.getBblx(), this.m_rtData.getInitData().getBblxVO()));

      rtHeader.setSfqs(new UFBoolean(false));
      rtHeader.setSfmr(new UFBoolean(false));
    } else if (this.m_rtData.getRtCache().isEmpty()) {
      rtHeader.setBblx(new Integer(0));
      rtHeader.setBBLX(FunctionBase.getQzsm(rtHeader.getBblx(), this.m_rtData.getInitData().getBblxVO()));

      rtHeader.setSfqs(new UFBoolean(true));
      rtHeader.setSfmr(new UFBoolean(true));
    }

    RtVO rtDst = new RtVO();
    rtDst.setParentVO(rtHeader);
    rtDst.setChildrenVO(rt.getChildrenVO());
    getRouteCard().setRouteVO(rtDst);
    getRouteCard().getCardPanel().setHeadItem("wlbm", getRfpMaterial().getRefPK());

    if (!getRfpMaterial().getRefPK().equals(rtHeader.getPk_produce())) {
      getRouteCard().wlbmChanged();
    }

    this.m_rtData.setHlCache((Hashtable)getDlgCopyRt().getHlCache().clone());
    this.m_rtData.setAtCache((Hashtable)getDlgCopyRt().getAtCache().clone());

    if (this.m_rtData.getRtCache().isEmpty())
      setState(6);
    else if (this.m_rtData.getInitData().getEcn().equals("Y"))
      setState(3);
    else {
      setState(2);
    }
    return true;
  }

  public boolean onDelete()
  {
    RtVO rt = getRouteList().getSelectedRoute();
    if (rt == null) {
      return false;
    }

    RtHeaderVO rtHeader = (RtHeaderVO)rt.getParentVO();
    if ((this.m_rtData.getInitData().getEcn().equals("Y")) && 
      (rtHeader.getBblx().intValue() == 0)) {
      this.m_toft.showErrorMessage(getStrByID("UPP-000030"));

      return false;
    }

    String question = getStrByID("UPP-000031");
    if ((rtHeader.getSfmr().booleanValue()) && 
      (this.m_rtData.getYxbbCount() > 1)) {
      question = getStrByID("UPP-000032");
    }

    if (this.m_toft.showYesNoMessage(question) != 4) {
      return false;
    }

    String pk_rtid = rtHeader.getPk_rtid();
    if (!lockPK(pk_rtid, "pd_rt")) {
      return false;
    }
    try
    {
      this.m_rtData.removeRoute(pk_rtid);
    } catch (Exception e) {
      this.m_toft.reportException(e);
      this.m_toft.showHintMessage(getStrByID("UPP-000033"));
      this.m_toft.showErrorMessage(getStrByID("UPP-000033") + ": " + e.getMessage());

      int i = 0;
    }
    finally
    {
      unLockPK();
    }

    BillListPanel list = getRouteList().getListPanel();
    list.getHeadBillModel().delLine(list.getHeadTable().getSelectedRows());
    list.getHeadBillModel().updateValue();
    list.getHeadTable().clearSelection();
    getRouteList().setBodyData(null);

    return true;
  }

  public boolean onDelLine()
  {
    return getRouteCard().onDeleteLine();
  }

  public boolean onModify()
  {
    RtVO rt = getRouteList().getSelectedRoute();
    if (rt == null) {
      return false;
    }

    RtHeaderVO rtHeader = (RtHeaderVO)rt.getParentVO();
    int bblx = rtHeader.getBblx().intValue();
    boolean ecn = this.m_rtData.getInitData().getEcn().equals("Y");
    if ((ecn) && (bblx == 0)) {
      setState(1);
    } else {
      String pk_rtid = rtHeader.getPk_rtid();
      if (!lockPK(pk_rtid, "pd_rt"))
        return false;
      if (ecn)
        setState(5);
      else {
        setState(4);
      }

    }

    getRouteCard().setRouteVO(rt);
    return true;
  }

  public boolean onPrint()
  {
    RtVO rt = getRouteList().getSelectedRoute();
    if (rt == null) {
      return false;
    }

    RtHeaderVO rtHeader = (RtHeaderVO)rt.getParentVO();
    rtHeader.setGcbm(getRfpFactory().getRefName());

    String[] selectItems = { getStrByID("UPP-000034"), getStrByID("UPP-000035"), getStrByID("UPP-000036"), getStrByID("UPP-000037") };

    String message = getStrByID("UPP-000038");
    Object value = MessageDialog.showSelectDlg(this, null, message, selectItems, selectItems.length);

    if (value == null)
      return false;
    if (value.equals(selectItems[1]))
      rt = addZxForRt(rt, true, false);
    else if (value.equals(selectItems[2]))
      rt = addZxForRt(rt, false, true);
    else if (value.equals(selectItems[3])) {
      rt = addZxForRt(rt, true, true);
    }

    DataSource dataSource = new DataSource(this.m_toft);
    dataSource.setVO(rt);
    BillData data = getRouteCard().getCardPanel().getBillData();
    dataSource.setBillData(data);
    dataSource.setModuleName("10093030");

    getPrintEntry().setDataSource(dataSource);
    if (getPrintEntry().selectTemplate() < 0)
      return false;
    getPrintEntry().preview();

    return true;
  }

  public void onRefresh()
  {
    if (this.m_nState == 0)
      materialChanged();
  }

  public boolean onSave()
  {
    if ((this.m_nState == -1) || (this.m_nState == 0)) {
      materialChanged();
      return false;
    }

    try
    {
      getRouteCard().getCardPanel().getBillData().dataNotNullValidate();
    } catch (ValidationException e) {
      MessageDialog.showHintDlg(this, null, e.getMessage());
      return false;
    }
    RtVO rt = getRouteCard().getRouteVO();

    if (!verifyData(rt)) {
      return false;
    }

    if (!getRouteList().dealDefaultVersionBeforeSave(rt, this.m_nState)) {
      return false;
    }

    sort((RtItemVO[])(RtItemVO[])rt.getChildrenVO());
    System.out.println();
    try
    {
      switch (this.m_nState) {
      case 2:
      case 3:
      case 6:
        rt = this.m_rtData.addRoute(rt);

        getRouteList().dealDefaultVersionAfterSave(rt, this.m_nState);
        getRouteList().addRoute(rt);
        break;
      case 4:
      case 5:
        rt = this.m_rtData.updateRoute(rt);

        getRouteList().dealDefaultVersionAfterSave(rt, this.m_nState);
        getRouteList().updateRoute(rt);
      }

      unLockPK();
    } catch (Exception e) {
      this.m_toft.reportException(e);
      this.m_toft.showHintMessage(getStrByID("UPP-000039"));
      this.m_toft.showErrorMessage(getStrByID("UPP-000039") + ": " + e.getMessage());

      return false;
    }

    setState(0);
    return true;
  }

  public boolean onZzfs()
  {
    this.m_toft.showHintMessage("");

    String pk_rtid = null;
    RtVO rt = null;
    RtHeaderVO rtHeader = null;
    if (this.m_nState == 0) {
      rt = getRouteList().getSelectedRoute();
      if (rt != null) {
        rtHeader = (RtHeaderVO)rt.getParentVO();
        pk_rtid = rtHeader.getPk_rtid();
      } else {
        return false;
      }
    } else {
      pk_rtid = getRouteCard().getCardPanel().getHeadItem("pk_rtid").getValue();

      if ((pk_rtid != null) && (pk_rtid.trim().length() > 0)) {
        rt = this.m_rtData.getRtVO(pk_rtid);
        rtHeader = (RtHeaderVO)rt.getParentVO();
      } else {
        MessageDialog.showHintDlg(this, null, getStrByID("UPP-000040"));

        return false;
      }
    }
    if (rt == null) {
      MessageDialog.showHintDlg(this, null, getStrByID("UPP-000040"));

      return false;
    }

    ZzfsHeaderVO[] vos = null;
    try {
      vos = this.m_rtData.getZzfs(pk_rtid);
    } catch (Exception e) {
      this.m_toft.reportException(e);
      this.m_toft.showHintMessage(getStrByID("UPP-000023"));
      this.m_toft.showErrorMessage(getStrByID("UPP-000023") + ": " + e.getMessage());

      return false;
    }

    getDlgZzfs().setData(null, vos);
    getDlgZzfs().setRtHeader(rtHeader);

    if (getDlgZzfs().showModal() != 1) {
      return false;
    }

    vos = getDlgZzfs().getZzfsHeaders();
    try {
      this.m_rtData.updateZzfsVO(pk_rtid, vos);
    } catch (Exception e) {
      this.m_toft.reportException(e);
      this.m_toft.showHintMessage(getStrByID("UPP-000041"));
      this.m_toft.showErrorMessage(getStrByID("UPP-000041") + ": " + e.getMessage());

      return false;
    }

    return true;
  }

  public void postInit()
  {
    this.m_rtData = new RtData();
    this.m_rtData.setToftPanel(this.m_toft);
    this.m_rtData.initRtData();
    try {
      this.m_rtData.readInitData();
    } catch (Exception e) {
      this.m_toft.reportException(e);
      this.m_toft.showHintMessage(getStrByID("UPP-000042"));

      this.m_toft.showErrorMessage(getStrByID("UPP-000042") + ": " + e.getMessage());

      return;
    }

    getRouteList().setRtData(this.m_rtData);
    getRouteCard().setRtData(this.m_rtData);

    DefConfig defineConfig = new DefConfig("工艺路线头", "工艺路线体");

    defineConfig.converDefItems(getRouteCard().getCardPanel(), getRouteList().getListPanel());

    getRouteList().postInit();
    getRouteCard().postInit();

    String wlWherePart = " and bd_produce.pk_calbody = '" + this.m_toft.getFactoryCode() + "' and ((matertype = 'MR' and (outtype = 'OB' or outtype = 'OD')) or matertype = 'PR' or virtualflag = 'Y')";

    if (this.m_rtData.getInitData().getSfjhwlkz().equals("Y")) {
      wlWherePart = wlWherePart + " and exists(select jhyid from pd_jhwl where jhyid = '" + this.m_toft.getUser().getPrimaryKey() + "' and jhlx = 5 and (sfwlgly = 'Y' or (pd_jhwl.pk_produce = bd_produce.pk_produce)))";
    }

    getRfpMaterial().setReturnCode(false);
    getRfpMaterial().getRefModel().addWherePart(wlWherePart);

    getRfpMaterial().addValueChangedListener(this);

    setState(-1);
  }

  public void setCbr(boolean isCbr)
  {
    if (isCbr)
      this.m_rtData.getInitData().setCbr("Y");
    else
      this.m_rtData.getInitData().setCbr("N");
  }

  public void setData(IEditData data)
  {
    if (data == null) {
      getRouteList().clearUI();
      getRfpMaterial().setPK(null);
      return;
    }

    this.m_rtData = ((RtData)data);
    getRfpMaterial().setPK(this.m_rtData.getMaterialInfo().getPk_produce());
    getRouteList().setRtData(this.m_rtData);
    getRouteCard().setRtData(this.m_rtData);
  }

  public void setState(int nState)
  {
    if (this.m_nState == nState) {
      return;
    }
    switch (nState) {
    case -1:
      ((CardLayout)getLayout()).show(this, getViewPanel().getName());

      break;
    case 0:
      ((CardLayout)getLayout()).show(this, getViewPanel().getName());

      getDlgAt().setEditable(false);
      getDlgZzfs().setEditable(true);
      break;
    case 1:
      ((CardLayout)getLayout()).show(this, getEditPanel().getName());

      getDlgAt().setEditable(false);
      getDlgZzfs().setEditable(false);
      break;
    case 2:
    case 3:
    case 4:
    case 5:
    case 6:
      ((CardLayout)getLayout()).show(this, getEditPanel().getName());

      getDlgAt().setEditable(true);
      getDlgZzfs().setEditable(false);
    }

    if (this.m_bUpdateButtons)
    {
      this.m_toft.setState(nState, null);
    }

    getRouteCard().setState(nState);

    this.m_nState = nState;
  }

  public void setToftPanel(MMToftPanel toft)
  {
    this.m_toft = toft;
  }

  public void setUpdateButtons(boolean isUpdateButtons)
  {
    this.m_bUpdateButtons = isUpdateButtons;
  }

  public boolean unLockPK() {
    if (this.m_strLockedPk != null) {
      try {
        LockBO_Client.freePK(this.m_strLockedPk, this.m_toft.getUnitCode(), "pd_rt");

        this.m_strLockedPk = null;
      } catch (Exception e) {
        this.m_toft.reportException(e);
        this.m_toft.showErrorMessage(getStrByID("UPP-000043"));

        return false;
      }
    }
    return true;
  }

  public void valueChanged(ValueChangedEvent event)
  {
    if (event.getSource() == getRfpMaterial()) {
      getRfpMaterial().removeValueChangedListener(this);
      materialChanged();
      getRfpMaterial().addValueChangedListener(this);
    }
  }

  private RtItemVO getCurrentItemVO()
  {
    RtItemVO vo = null;
    if (this.m_nState == 0)
      vo = getRouteList().getBodySelectedVO();
    else {
      vo = getRouteCard().getBodySelectedVO();
    }

    if (vo == null) {
      return null;
    }
    if (vo.getGxh() == null) {
      MessageDialog.showHintDlg(this.m_toft, null, getStrByID("UPP-000044"));

      return null;
    }

    return vo;
  }

  private AtDlg getDlgAt()
  {
    if (this.m_dlgAt == null) {
      this.m_dlgAt = new AtDlg(this.m_toft, getStrByID("UPP-000045"));
    }
    return this.m_dlgAt;
  }

  public RtZzfsDlg getDlgZzfs()
  {
    if (this.m_dlgZzfs == null) {
      this.m_dlgZzfs = new RtZzfsDlg(this.m_toft, getStrByID("UPP-000012"));
    }
    return this.m_dlgZzfs;
  }

  public void onAtDlg()
  {
    RtItemVO voHeader = getCurrentItemVO();
    if (voHeader == null) {
      return;
    }
    String key = voHeader.getGxh();
    RtBAtVO vo = (RtBAtVO)this.m_rtData.getAtCache().get(key);
    if (vo == null)
      vo = new RtBAtVO();
    vo.setParentVO(voHeader);
    getDlgAt().setData(vo);

    if (getDlgAt().showModal() != 1) {
      return;
    }
    if (this.m_nState == 0) {
      return;
    }

    boolean isChanged = getDlgAt().isDataChanged();
    if (isChanged) {
      int row = getRouteCard().getCardPanel().getBillTable().getSelectedRow();

      if (getRouteCard().getCardPanel().getBillModel().getRowState(row) == 0) {
        getRouteCard().getCardPanel().getBillModel().setRowState(row, 2);
      }

    }

    vo = (RtBAtVO)getDlgAt().getData();
    if (isChanged)
      vo.setVoChanged(true);
    this.m_rtData.getAtCache().put(key, vo);
  }

  public void onHlDlg()
  {
    RtItemVO rtItem = getCurrentItemVO();
    if (rtItem == null) {
      return;
    }
    String key = rtItem.getGxh();
    RtBVO vo = (RtBVO)this.m_rtData.getHlCache().get(key);
    if (vo == null)
      vo = new RtBVO();
    vo.setParentVO(rtItem);
    RtHeaderVO rtHeader = getCurrentRtHeader();
    getDlgGxHl().setRtHlData(vo);
    getDlgGxHl().setRtHeader(rtHeader);

    if ((this.m_nState == 0) || (this.m_nState == 1))
    {
      getDlgGxHl().setState(-1);
    }
    else getDlgGxHl().setState(0);

    if (getDlgGxHl().showModal() != 1) {
      return;
    }
    if (this.m_nState == 0) {
      return;
    }

    int row = getRouteCard().getCardPanel().getBillTable().getSelectedRow();
    if (getRouteCard().getCardPanel().getBillModel().getRowState(row) == 0) {
      getRouteCard().getCardPanel().getBillModel().setRowState(row, 2);
    }

    vo = getDlgGxHl().getRtHlData();
    if (vo == null) {
      this.m_rtData.getHlCache().remove(key);
    } else {
      vo.setVoChanged(true);
      this.m_rtData.getHlCache().put(key, vo);
    }
  }

  private boolean verifyData(RtVO vo)
  {
    RtHeaderVO header = (RtHeaderVO)vo.getParentVO();
    String wlbmid = header.getWlbmid();
    if (wlbmid == null) {
      MessageDialog.showHintDlg(this.m_toft, null, getStrByID("UPP-000046"));

      return false;
    }

    RtItemVO[] items = (RtItemVO[])(RtItemVO[])vo.getChildrenVO();
    if ((items == null) || (items.length == 0)) {
      MessageDialog.showHintDlg(this.m_toft, null, getStrByID("UPP-000047"));

      return false;
    }

    return (verifyHeader(header)) && (verifyItems(items, header));
  }

  private boolean verifyHeader(RtHeaderVO header)
  {
    String version = header.getVersion();
    if ((version == null) || (version.trim().length() == 0)) {
      MessageDialog.showHintDlg(this.m_toft, null, getStrByID("UPP-000048"));

      return false;
    }

    UFDouble sl = header.getSl();
    if ((sl == null) || (sl.compareTo(new UFDouble(0)) <= 0)) {
      MessageDialog.showHintDlg(this.m_toft, null, getStrByID("UPP-000049"));

      return false;
    }

    UFDouble ssl = header.getSsl();
    UFDouble esl = header.getEsl();
    UFDouble gdsl = header.getGdsl();
    UFDouble zero = new UFDouble(0);
    int gylxlx = header.getGylxlx().intValue();
    if (gylxlx != RtVO.TYPE_STANDARD) {
      if (gylxlx == RtVO.TYPE_VARBATCH) {
        if ((ssl == null) || ((esl != null) && (ssl.compareTo(esl) > 0))) {
          MessageDialog.showHintDlg(this.m_toft, null, getStrByID("UPP-000050"));

          return false;
        }
      } else if ((gylxlx == RtVO.TYPE_FIXBATCH) && (
        (gdsl == null) || (gdsl.compareTo(zero) <= 0))) {
        MessageDialog.showHintDlg(this.m_toft, null, getStrByID("UPP-000051"));

        return false;
      }

    }

    boolean sftpmr = header.getSfqs().booleanValue();
    if ((sftpmr) && (header.getBblx().intValue() != 0)) {
      MessageDialog.showHintDlg(this.m_toft, null, getStrByID("UPP-000052"));

      return false;
    }

    boolean sfmr = header.getSfmr().booleanValue();
    if ((sfmr) && (!sftpmr)) {
      MessageDialog.showHintDlg(this.m_toft, null, getStrByID("UPP-000053"));

      return false;
    }

    RtHeaderVO[] rtHeaders = (RtHeaderVO[])(RtHeaderVO[])getRouteList().getListPanel().getHeadBillModel().getBodyValueVOs(RtHeaderVO.class.getName());

    String pk = header.getPk_rtid();
    for (int i = 0; i < rtHeaders.length; i++) {
      if (rtHeaders[i].getPk_rtid().equals(pk)) {
        continue;
      }
      String tempVer = rtHeaders[i].getVersion();
      if (version.equals(tempVer)) {
        MessageDialog.showHintDlg(this.m_toft, null, getStrByID("UPP-000054"));

        return false;
      }

      if (gylxlx == RtVO.TYPE_VARBATCH) {
        boolean isCross = false;
        UFDouble tempSsl = rtHeaders[i].getSsl();
        UFDouble tempEsl = rtHeaders[i].getEsl();
        if (esl != null) {
          if ((tempSsl != null) && (tempEsl != null)) {
            if ((ssl.compareTo(tempEsl) < 0) && (esl.compareTo(tempSsl) > 0) && ((ssl.compareTo(tempSsl) != 0) || (esl.compareTo(tempEsl) != 0)))
            {
              isCross = true;
            }
          } else if ((tempSsl != null) && (tempEsl == null) && 
            (esl.compareTo(tempSsl) > 0)) {
            isCross = true;
          }

        }
        else if ((tempEsl == null) || (tempEsl.compareTo(ssl) > 0)) {
          isCross = true;
        }

        if (isCross) {
          MessageDialog.showHintDlg(this.m_toft, null, getStrByID("UPP-000055"));

          return false;
        }

      }

      int id = -1;
      boolean isCheckTpmr = true;
      if ((sfmr) && (rtHeaders[i].getSfmr().booleanValue())) {
        id = MessageDialog.showYesNoDlg(this.m_toft, null, getStrByID("UPP-000056"));

        if (id != 4) {
          return false;
        }
        isCheckTpmr = false;
      }

      if ((sftpmr) && (isCheckTpmr)) {
        if (gylxlx == RtVO.TYPE_STANDARD) {
          if (rtHeaders[i].getSfqs().booleanValue()) {
            id = MessageDialog.showYesNoDlg(this.m_toft, null, getStrByID("UPP-000056"));

            if (id != 4)
              return false;
          }
        }
        else if (gylxlx == RtVO.TYPE_VARBATCH) {
          UFDouble tempSsl = rtHeaders[i].getSsl();
          UFDouble tempEsl = rtHeaders[i].getEsl();
          if ((tempSsl != null) && (tempEsl != null) && 
            (ssl.compareTo(tempSsl) == 0) && (esl.compareTo(tempEsl) == 0))
          {
            if (rtHeaders[i].getSfqs().booleanValue()) {
              id = MessageDialog.showYesNoDlg(this.m_toft, null, getStrByID("UPP-000057"));

              if (id != 4) {
                return false;
              }
            }
          }
        }
        else if (gylxlx == RtVO.TYPE_FIXBATCH) {
          UFDouble tempGdsl = rtHeaders[i].getGdsl();
          if ((tempGdsl == null) || 
            (gdsl.compareTo(tempGdsl) != 0) || 
            (!rtHeaders[i].getSfqs().booleanValue())) continue;
          id = MessageDialog.showYesNoDlg(this.m_toft, null, getStrByID("UPP-000058"));

          if (id != 4) {
            return false;
          }

        }

      }

    }

    return true;
  }

  private CopyRtDlg getDlgCopyRt()
  {
    if (this.m_dlgCopyRt == null) {
      this.m_dlgCopyRt = new CopyRtDlg(this.m_toft, getStrByID("UPP-000059"));
    }
    return this.m_dlgCopyRt;
  }

  public int getState()
  {
    return this.m_nState;
  }

  public void setInvAndVersion(String pk_corp, String pk_calbody, String pk_invbasdoc, String version)
  {
    RtVO rt = null;
    try {
      rt = PDProxy.getRemoteRt().getValidateRtByVersion(pk_corp, pk_calbody, pk_invbasdoc, version, null);
    }
    catch (Exception e) {
      this.m_toft.reportException(e);
      this.m_toft.showErrorMessage(getStrByID("UPP-000060") + ": " + e.getMessage());

      return;
    }

    getRouteCard().setRouteVO(rt);
    setState(1);
  }

  public boolean setProducePK(String pk_produce)
  {
    getRfpMaterial().setPK(pk_produce);
    if (getRfpMaterial().getRefPK() == null) {
      getRouteList().clearUI();
      return false;
    }
    materialChanged();
    return true;
  }

  private void sort(RtItemVO[] vos)
  {
    if ((vos == null) || (vos.length < 2)) {
      return;
    }
    for (int i = 0; i < vos.length - 1; i++)
    {
      int minIndex = i;
      String gxhMin = vos[minIndex].getGxh();

      for (int j = i + 1; j < vos.length; j++) {
        if (vos[j].getGxh().compareTo(gxhMin) < 0) {
          minIndex = j;
          gxhMin = vos[j].getGxh();
        }
      }
      if (minIndex > i) {
        RtItemVO voTemp = vos[i];
        vos[i] = vos[minIndex];
        vos[minIndex] = voTemp;
      }
    }
  }

  private PrintEntry getPrintEntry()
  {
    if (this.m_printEntry == null) {
      this.m_printEntry = new PrintEntry(this, null);
      this.m_printEntry.setTemplateID(this.m_toft.getUnitCode(), "10093030", this.m_toft.getUser().getPrimaryKey(), null);
    }

    return this.m_printEntry;
  }

  private RtVO addZxForRt(RtVO vo, boolean isGxhl, boolean isAt)
  {
    RtVO rt = new RtVO();
    rt.setParentVO((CircularlyAccessibleValueObject)vo.getParentVO().clone());

    RtItemVO[] items = (RtItemVO[])(RtItemVO[])vo.getChildrenVO();
    Vector v = new Vector();
    int len = items.length;

    for (int i = 0; i < len; i++)
    {
      Vector vecTemp = new Vector();

      if (isGxhl) {
        RtBItemVO[] hls = items[i].getHls();
        if ((hls == null) || (hls.length == 0))
          vecTemp.addElement(items[i].clone());
        else {
          for (int j = 0; j < hls.length; j++) {
            RtItemVO item = (RtItemVO)items[i].clone();
            item.setZxhlbm(hls[j].getWlbm());
            item.setZxhlmc(hls[j].getWlmc());
            item.setZxhlsl(hls[j].getXhsl());
            item.setZxhljldw(hls[j].getJldwmc());
            item.setZxhlinvspec(hls[j].getInvspec());
            item.setZxhlinvtype(hls[j].getInvtype());
            item.m_freeitemvo = hls[j].m_freeitemvo;
            vecTemp.addElement(item);
          }
        }
      }

      if (isAt) {
        RtBAtItemVO[] ats = items[i].getAts();
        int size = vecTemp.size();
        if ((ats == null) || (ats.length == 0)) {
          if (size == 0)
            vecTemp.addElement(items[i].clone());
        }
        else {
          if (size < ats.length) {
            int appendCount = ats.length - size;
            for (int j = 0; j < appendCount; j++) {
              RtItemVO item = (RtItemVO)items[i].clone();
              vecTemp.addElement(item);
            }
          }
          for (int j = 0; j < ats.length; j++) {
            RtItemVO item = (RtItemVO)vecTemp.elementAt(j);
            item.setAtcode(ats[j].getAtcode());
            item.setAtname(ats[j].getAtname());
            item.setAtjldw(ats[j].getJldwmc());
            item.setAtbzzyl(ats[j].getBzzyl());
            item.setAtmemo(ats[j].getBz());
            vecTemp.setElementAt(item, j);
          }
        }
      }

      for (int j = 0; j < vecTemp.size(); j++) {
        v.addElement(vecTemp.elementAt(j));
      }
    }

    items = new RtItemVO[v.size()];
    if (v.size() > 0) {
      v.copyInto(items);
    }

    rt.setChildrenVO(items);
    return rt;
  }

  private RtHeaderVO getCurrentRtHeader()
  {
    RtHeaderVO header = null;

    if (this.m_nState == 0) {
      header = (RtHeaderVO)getRouteList().getSelectedRoute().getParentVO();
    }
    else {
      header = getRouteCard().getRtHeaderVO();
    }

    return header;
  }

  private RtHlDlg getDlgGxHl()
  {
    if (this.m_dlgGxHl == null) {
      this.m_dlgGxHl = new RtHlDlg(this.m_toft, getStrByID("UPT10093030-000028"), this.m_rtData);
    }

    return this.m_dlgGxHl;
  }

  private boolean verifyItems(RtItemVO[] items, RtHeaderVO header)
  {
    UFDouble zero = new UFDouble(0);
    int nZxCount = 0;

    for (int i = 0; i < items.length; i++)
    {
      String append = getStrByID("UPP-000061", new String[] { i + 1 + "" }) + ": ";

      String gxh = items[i].getGxh();
      if ((gxh == null) || (gxh.trim().length() == 0)) {
        MessageDialog.showHintDlg(this.m_toft, null, append + getStrByID("UPP-000062"));

        return false;
      }

      String gzzxid = items[i].getGzzxbmid();
      if ((gzzxid == null) || (gzzxid.trim().length() == 0)) {
        MessageDialog.showHintDlg(this.m_toft, null, append + getStrByID("UPP-000063"));

        return false;
      }

      UFDouble jgsj = items[i].getJgsj();
      if ((jgsj == null) || (jgsj.compareTo(new UFDouble(0)) < 0)) {
        MessageDialog.showHintDlg(this.m_toft, null, append + "加工时间必须大于或等于0");

        return false;
      }

      UFDate sdate = items[i].getSdate();
      UFDate edate = items[i].getEdate();
      if ((sdate == null) || (edate == null) || (sdate.after(edate))) {
        MessageDialog.showHintDlg(this.m_toft, null, append + getStrByID("UPP-000065"));

        return false;
      }

      for (int j = 0; j < i; j++)
      {
        String tempGxh = items[j].getGxh();
        if (gxh.equals(tempGxh)) {
          MessageDialog.showHintDlg(this.m_toft, null, getStrByID("UPP-000061", new String[] { j + 1 + "," + (i + 1) }) + ": " + getStrByID("UPP-000066"));

          return false;
        }

      }

      RtBItemVO[] rtHls = items[i].getHls();
      if ((rtHls != null) && (rtHls.length > 0)) {
        for (int j = 0; j < rtHls.length; j++) {
          if (!rtHls[j].getWlbmid().equals(header.getWlbmid())) {
            continue;
          }
          MessageDialog.showHintDlg(this.m_toft, null, append + getStrByID("UPP-000067", new String[] { rtHls[j].getHh() + "" }));

          return false;
        }

      }

      if ((items[i].getSfzx() != null) && (items[i].getSfzx().booleanValue())) {
        nZxCount++;
      }
    }
    if (nZxCount != 1) {
      MessageDialog.showHintDlg(this.m_toft, null, getStrByID("UPP-000068"));

      return false;
    }
    return true;
  }

  public boolean verifyModifyAuthority()
  {
    RtVO rt = getRouteList().getSelectedRoute();
    if (rt == null) {
      return false;
    }

    RtHeaderVO rtHeader = (RtHeaderVO)rt.getParentVO();
    if (rtHeader.getCreator().equals(this.m_toft.getUser().getUserCode())) {
      return true;
    }
    MessageDialog.showHintDlg(this, null, getStrByID("UPP-000069"));

    return false;
  }

  private String getStrByID(String id)
  {
    return NCLangRes.getInstance().getStrByID("10093030", id);
  }

  private String getStrByID(String id, String[] args) {
    return NCLangRes.getInstance().getStrByID("10093030", id, null, args);
  }

  public void freeLock() throws Exception
  {
    unLockPK();
  }

  public int getCurrentState() {
    if ((this.m_nState == 2) || (this.m_nState == 3) || (this.m_nState == 6))
    {
      return 2;
    }if ((this.m_nState == 4) || (this.m_nState == 5))
    {
      return 3;
    }
    return 1;
  }

  public boolean isDataChanged() {
    return false;
  }

  public int save() {
    if (onSave()) {
      return 4;
    }
    return 7;
  }

  public void onJudge()
  {
    RtVO rt = getRouteList().getSelectedRoute();
    if (rt == null) return;

    Object value = MessageDialog.showInputDlg(this, "鉴定", "请输入鉴定号", null, 50);
    if ((value == null) || (value.toString().equals(""))) {
      MessageDialog.showHintDlg(this, null, "鉴定号不能为空");
      return;
    }
    try
    {
      rt = PDProxy.getRemoteRt().judgeRoute(rt, value.toString());
    } catch (Exception e) {
      this.m_toft.reportException(e);
      this.m_toft.showErrorMessage(e.getMessage());
      return;
    }

    getRouteList().updateRoute(rt);
    this.m_toft.showHintMessage("鉴定完毕");
  }

  public void onDocManage()
  {
    RtItemVO rtItem = getCurrentItemVO();
    if (rtItem == null) return;

    if ((rtItem.getGraph() == null) || (rtItem.getGraph().equals(""))) {
      MessageDialog.showHintDlg(this, null, "请先为工序定义图纸号");
      return;
    }

    RtHeaderVO rtHeader = getCurrentRtHeader();
    String pk_corp = rtHeader.getPk_corp();
    String factory = this.m_toft.getFactoryVO().getName();
    String showName = rtItem.getGxh() + "_" + rtItem.getGraph();
    String dirName = "工艺路线_" + pk_corp + "_" + factory + Utils.FS;
    dirName = dirName + rtHeader.getWlbm() + "_" + rtHeader.getTh() + "_" + rtHeader.getVersion() + Utils.FS;
    dirName = dirName + showName;

    DocManageDlg docManDlg = new DocManageDlg(this, new String[] { dirName }, new String[] { showName });

    docManDlg.setUpLoad(true);
    docManDlg.showModal();
  }
}