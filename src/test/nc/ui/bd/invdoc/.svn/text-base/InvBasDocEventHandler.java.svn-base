package nc.ui.bd.invdoc;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.itf.uap.bd.inv.IInvclQry;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.ui.bd.b15.BillTempletDlg;
import nc.ui.bd.b15.IPageDataCtl;
import nc.ui.bd.b15.InventoryBO_Client;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.bill.depend.PageInfo;
import nc.ui.bill.depend.PageNavigationBar;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITree;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListData;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillScrollPane;
import nc.ui.pub.general.PrintDataUtil;
import nc.ui.pub.general.PrintVODataSource;
import nc.ui.pub.print.IDataSource;
import nc.ui.pub.print.PrintEntry;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.trade.base.AbstractBillUI;
import nc.ui.trade.bill.BillCardPanelWrapper;
import nc.ui.trade.buffer.BillUIBuffer;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.ButtonManager;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.pub.DefaultCheckIsDef;
import nc.ui.trade.pub.DefaultGetDefCodeOrName;
import nc.ui.trade.pub.IGetDefCodeOrName;
import nc.ui.trade.pub.PrtDefDealedDecorator;
import nc.ui.trade.pub.VOTreeNode;
import nc.ui.trade.treemanage.BillTreeManageUI;
import nc.ui.trade.treemanage.TreeManageEventHandler;
import nc.vo.bd.BDMsg;
import nc.vo.bd.CorpVO;
import nc.vo.bd.access.AccessorManager;
import nc.vo.bd.access.BddataVO;
import nc.vo.bd.access.IBDAccessor;
import nc.vo.bd.b14.InvclVO;
import nc.vo.bd.b15.PageVO;
import nc.vo.bd.invdoc.BasManUnionVO;
import nc.vo.bd.invdoc.ISetGetPkWhenCopyInsert;
import nc.vo.bd.invdoc.InvbasdocVO;
import nc.vo.bd.log.BDLogUtil;
import nc.vo.bd.log.OperateType;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.general.GeneralExVO;
import nc.vo.pub.general.GeneralSuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.sm.UserVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.trade.pub.IExAggVO;

public class InvBasDocEventHandler extends TreeManageEventHandler
  implements IPageDataCtl
{
  private int m_rowCountPerPage = 100;
  private Hashtable m_nodeInfo;
  private BillTempletDlg billTempletDlg;
  private String queryCondition;
  private int curAdd;
  private HYBillVO m_newBillvo;
  private String m_pk_billtemplet;
  private int _currentPageIndex = 1;
  private String _corpedPK;

  protected void checkCanAdd()
    throws BusinessException
  {
    if ((getSelectedNode() == null) || 
      (getSelectedNode().getData() == null) || 
      (isNotLeaf()))
      throw new BusinessException(NCLangRes.getInstance().getStrByID("10081206", "UPP10081206-000034"));
  }

  private boolean isNotLeaf()
  {
    if (getSelectedNode().isRoot())
      return true;
    try {
      String pk_corp = ClientEnvironment.getInstance().getCorporation().getPk_corp();
      BddataVO datavo = AccessorManager.getAccessor("00010000000000000006", pk_corp).getDocByPk(getSelectedNode().getData().getPrimaryKey());
      IInvclQry impl = (IInvclQry)NCLocator.getInstance().lookup(IInvclQry.class.getName());
      return impl.isHaveChildren(datavo.getCode(), pk_corp);
    } catch (BusinessException e) {
      Logger.error(e.getMessage(), e);
    }
    return true;
  }
  public void onTreeSelected(VOTreeNode selectnode) {
    if (!selectnode.isLeaf())
      return;
    if (getNodeInfo() == null) {
      pageChanged(1);
      return;
    }
    if (getSelectedNode() == null)
      return;
    Hashtable htpage = (Hashtable)(Hashtable)getNodeInfo().get(getNodeDatePK(getSelectedNode()));
    PageVO vo = null;
    if (htpage != null)
    {
      vo = (PageVO)htpage.get(new Integer(0));
    }
    if (vo == null)
    {
      pageChanged(1);
    }
    else
    {
      pageChanged(vo.getPageInfo().getM_iCurrentPage());
    }
  }

  public InvBasDocEventHandler(BillManageUI billUI, IControllerBase control)
  {
    super(billUI, control);
  }

  protected void onBoCard() throws Exception {
    super.onBoCard();
    getBillCardPanelWrapper().getBillCardPanel().execHeadLoadFormulas();
  }

  private CircularlyAccessibleValueObject[] getPageInvs(VOTreeNode selected, int pageIndex) {
    CircularlyAccessibleValueObject[] vosCur = (CircularlyAccessibleValueObject[])null;
    String classid = getNodeDatePK(selected);

    Hashtable htpage = getHtPage(selected);
    initPageVo(classid, htpage);

    if (htpage.get(new Integer(pageIndex)) == null) {
      PageVO pageVO = (PageVO)htpage.get(new Integer(0));
      vosCur = getCurPageVos(selected, pageIndex, pageVO);
      if (vosCur == null)
        return null;
      htpage.put(new Integer(pageIndex), vosCur);
      if (selected != null)
        getNodeInfo().put(getNodeDatePK(selected), htpage);
      else
        getNodeInfo().put("null", htpage);
    } else {
      if ((pageIndex == 0) || (htpage.get(new Integer(pageIndex)) == null))
        return null;
      vosCur = (SuperVO[])htpage.get(new Integer(pageIndex));
    }
    return vosCur;
  }

  private Hashtable getHtPage(VOTreeNode selected)
  {
    if ((selected != null) && (getNodeInfo().get(getNodeDatePK(selected)) == null)) {
      getNodeInfo().put(getNodeDatePK(selected), new Hashtable());
    }

    if ((selected == null) && (getNodeInfo().get("null") == null)) {
      getNodeInfo().put("null", new Hashtable());
    }
    Hashtable htpage = new Hashtable();
    if (selected != null)
      htpage = (Hashtable)(Hashtable)getNodeInfo().get(getNodeDatePK(selected));
    else
      htpage = (Hashtable)(Hashtable)getNodeInfo().get("null");
    return htpage;
  }

  private Hashtable getNodeInfo()
  {
    if (this.m_nodeInfo == null)
      this.m_nodeInfo = new Hashtable();
    return this.m_nodeInfo;
  }

  protected CircularlyAccessibleValueObject[] getCurPageVos(VOTreeNode selected, int pageIndex, PageVO pagevo)
  {
    CircularlyAccessibleValueObject[] vosCur = (CircularlyAccessibleValueObject[])null;
    String pkBegin = (String)pagevo.getPages().get(new Integer(pageIndex - 1));
    String pkEnd = (String)pagevo.getPages().get(new Integer(pageIndex));

    int index = pagevo.getPageInfo().getM_iCurrentPage();
    int totalPage = pagevo.getPageInfo().getM_iTotalPage();

    String strWhere = "(pk_corp='0001' or pk_corp='" + ClientEnvironment.getInstance().getCorporation().getPk_corp() + "') ";
    if (selected != null)
      strWhere = strWhere + " and pk_invcl='" + getNodeDatePK(selected) + "'";
    if (getQueryCondition() != null)
      strWhere = strWhere + " and " + getQueryCondition();
    if (index != totalPage) {
      strWhere = strWhere + " and invcode>='" + pkBegin + "' and invcode<'" + pkEnd + "'";
    }
    else {
      strWhere = strWhere + " and invcode>='" + pkBegin + "' and invcode<='" + pkEnd + "'";
    }
    strWhere = strWhere + " order by invcode";
    try {
      vosCur = HYPubBO_Client.queryByCondition(
        InvbasdocVO.class, strWhere);
    } catch (Exception e1) {
      e1.printStackTrace();
    }
    if (vosCur == null)
      return null;
    return vosCur;
  }

  private void initPageVo(String classid, Hashtable htpage)
  {
    if (htpage.get(new Integer(0)) == null)
    {
      PageVO pageVO = getPageInfoVo(classid, this.m_rowCountPerPage);
      getListPnlPageCtl().setDataCtl(this);
      getListPnlPageCtl().setBeginEndNum(pageVO.getPageInfo());
      ((BillTreeManageUI)getBillUI()).getBillListPanel().getParentListPanel()
        .getPageNavigationBar().setPageInfo(pageVO.getPageInfo());
      htpage.put(new Integer(0), pageVO);
    }
  }

  protected PageVO getPageInfoVo(String classid, int rowCountPerPage)
  {
    PageVO pageVO = new PageVO();

    pageVO.getPageInfo().setM_iRowCountPerPage(rowCountPerPage);
    String sql = "";
    if ((getUITree().getSelectionRows()[0] == 0) && (getQueryCondition() != null)) {
      sql = "select invcode from bd_invbasdoc where pk_corp='0001' and " + getQueryCondition() + 
        " order by invcode";
    }
    else
      sql = "select invcode from bd_invbasdoc where pk_corp='0001' and pk_invcl ='" + 
        classid + "' order by invcode";
    try {
      String[] pks = InventoryBO_Client.queryPKsForPageVO(sql);
      new PageInfoUtil().setPageInfo(pageVO, pks);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return pageVO;
  }

  protected String getNodeDatePK(VOTreeNode selected)
  {
    String classid = null;
    if (selected == null)
      return null;
    if (!selected.isRoot()) {
      try {
        classid = selected.getData().getPrimaryKey();
      } catch (BusinessException e) {
        e.printStackTrace();
      }
    }
    return classid;
  }

  private VOTreeNode getSelectedNode()
  {
    return ((BillTreeManageUI)getBillUI()).getBillTreeSelectNode();
  }

  private IListPnlPageCtrl getListPnlPageCtl()
  {
    if ((getBillUI() instanceof INeedListPageCtl))
      return ((INeedListPageCtl)getBillUI()).getListPnlPageCtl();
    return null;
  }

  public void pageChanged(int pageIndex)
  {
    if (pageIndex == 0)
      pageIndex = 1;
    this._currentPageIndex = pageIndex;
    CircularlyAccessibleValueObject[] queryVos = getPageInvs(getSelectedNode(), pageIndex);

    getBufferData().clear();
    try {
      if ((queryVos != null) && (queryVos.length != 0))
      {
        for (int i = 0; i < queryVos.length; i++)
        {
          AggregatedValueObject aVo = 
            (AggregatedValueObject)Class.forName(getUIController().getBillVoName()[0])
            .newInstance();
          aVo.setParentVO(queryVos[i]);
          getBufferData().addVOToBuffer(aVo);
        }
        getBillUI().setListHeadData(queryVos);

        getBillUI().setBillOperate(2);
      }
      else
      {
        getBillUI().setListHeadData(queryVos);
        getBufferData().setCurrentRow(-1);

        getBillUI().setBillOperate(4);
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void pageRefreshed(int pageIndex)
  {
    clearHtPage();
    pageChanged(1);
    this._currentPageIndex = 1;
  }

  public void pageRowCountChanged(int newRowCount)
  {
    this.m_rowCountPerPage = newRowCount;
    clearHtPage();

    pageChanged(1);
    this._currentPageIndex = 1;
  }

  private void clearHtPage()
  {
    if (getNodeInfo() == null)
      return;
    if (getSelectedNode() != null) {
      Hashtable htpage = (Hashtable)(Hashtable)getNodeInfo().get(getNodeDatePK(getSelectedNode()));
      if (htpage != null)
        htpage.clear();
    }
    else {
      Hashtable htpage = (Hashtable)(Hashtable)getNodeInfo().get("null");
      if (htpage != null)
        htpage.clear(); 
    }
  }

  protected void onBoElse(int intBtn) throws Exception {
    this.curAdd = intBtn;
    switch (intBtn) {
    case 109:
      onDefaultTempAdd();
      break;
    case 106:
      onCopyInsert(true);
      break;
    case 107:
      onTempAdd(true);
      break;
    case 104:
      onAssign();
      break;
    case 108:
      onDocManage();
      break;
    case 112:
      onBoCalute();
      break;
    case 113:
      onupdateData();
      break;
    case 105:
    case 110:
    case 111:
    }super.onBoElse(intBtn);
  }
  private void onupdateData() throws BusinessException {

      String sql = " select * from bd_invbasdoc where assistunit = 'Y'";
      IUAPQueryBS query = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
      ArrayList list = (ArrayList)query.executeQuery(sql, new BeanListProcessor(InvbasdocVO.class));
      for(Iterator iterator = list.iterator(); iterator.hasNext();)
      {
          InvbasdocVO vo = (InvbasdocVO)iterator.next();
          String invspec = vo.getInvspec();
          if(invspec != null && invspec.length() != 0)
          {
              String invspecs[] = invspec.split("\\*");
              if(invspecs.length >= 3)
              {
                  int length = invspecs.length;
                  vo.setLength(invspecs[length - 1]);
                  vo.setWidth(invspecs[length - 2]);
                  vo.setHeight(invspecs[length - 3]);
              }
          }
      }

      IVOPersistence ivoitf = (IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName());
      ivoitf.updateVOArray((SuperVO[])list.toArray(new InvbasdocVO[0]));
      getBillUI().showHintMessage("修改历史数据成功!");
      
      
  }

  private void onBoCalute()
    throws BusinessException
  {
    Object guige = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("invspec").getValueObject();
    if ((guige == null) || (guige.toString().length() == 0)) throw new BusinessException("规格为空，不可以进行计算");
    String[] value = guige.toString().trim().split("\\*");
    int lenth = value.length;
    if (lenth < 3) throw new BusinessException("规格无法转换为长,宽,厚");
    String length = value[(lenth - 1)];
    String width = value[(lenth - 2)];
    String height = value[(lenth - 3)];
    Object lengthObj = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("length").getValueObject();
    if ((lengthObj == null) || (lengthObj.toString().length() == 0))
      getBillCardPanelWrapper().getBillCardPanel().setHeadItem("length", length);
    else
      length = lengthObj.toString();
    Object widthObj = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("width").getValueObject();
    if ((widthObj == null) || (widthObj.toString().length() == 0))
      getBillCardPanelWrapper().getBillCardPanel().setHeadItem("width", width);
    else
      width = widthObj.toString();
    Object heightObj = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("height").getValueObject();
    if ((heightObj == null) || (heightObj.toString().length() == 0))
      getBillCardPanelWrapper().getBillCardPanel().setHeadItem("height", height);
    else
      height = heightObj.toString();
    if (!isDouble(length)) {
      throw new BusinessException("长度不可以转化为数字型!");
    }
    if (!isDouble(width)) {
      throw new BusinessException("宽度可以转化为数字型!");
    }
    if (!isDouble(height)) {
      throw new BusinessException("厚度不可以转化为数字型!");
    }
    Object invpinpaiObj = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("invpinpai").getValueObject();
    if ((invpinpaiObj == null) || (invpinpaiObj.toString().length() == 0)) throw new BusinessException("铁的比重不可以为空!");
    int rowCount = getBillCardPanelWrapper().getBillCardPanel().getBillModel().getRowCount();
    if (rowCount == 0) throw new BusinessException("表体行为空！无法填写换算率值!");
    UFDouble mainmeasrate = new UFDouble(Math.pow(1000.0D, 3.0D)).div(new UFDouble(length)).div(new UFDouble(width))
      .div(new UFDouble(height)).div(new UFDouble(invpinpaiObj.toString().trim()));
    getBillCardPanelWrapper().getBillCardPanel().getBillModel().setValueAt(mainmeasrate, rowCount - 1, "mainmeasrate");
    getBillCardPanelWrapper().getBillCardPanel().getBillModel().setValueAt("Y", rowCount - 1, "fixedflag");
    Object pk_convertObj = getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(rowCount - 1, "pk_convert");
    if ((pk_convertObj != null) && (pk_convertObj.toString().length() != 0))
      getBillCardPanelWrapper().getBillCardPanel().getBillModel().setRowState(rowCount - 1, 2);
    getBillUI().showHintMessage("换算操作结束!");
  }

  private boolean isDouble(String doubleData) {
    boolean flag = false;
    try {
      new UFDouble(doubleData);
      flag = true;
    } catch (NumberFormatException e) {
      flag = false;
    }
    return flag;
  }

  private void onDefaultTempAdd()
    throws Exception
  {
    checkCanAdd();
    super.onBoAdd(getBillUI().getButtonManager().getButton(1));
  }
  private void onCopyInsert(boolean isBtnClick) throws Exception {
    if (getBufferData().getCurrentVO() == null) {
      throw new BusinessException(BDMsg.MSG_CHOOSE_DATA());
    }
    checkCanAdd();
    if (isBtnClick) {
      HYBillVO newBillvo = getCopyedBillVo();
      this.m_newBillvo = newBillvo;
    }
    else if ((getBillUI().getUserObject() instanceof ISetGetPkWhenCopyInsert)) {
      ((ISetGetPkWhenCopyInsert)getBillUI().getUserObject()).setInvPk(this._corpedPK);
    }

    onAddwithDefaultData(this.m_newBillvo);
  }

  protected HYBillVO getCopyedBillVo()
    throws BusinessException
  {
    HYBillVO billvo = (HYBillVO)getBufferData().getCurrentVO();
    if (billvo == null)
      throw new BusinessException(NCLangRes.getInstance().getStrByID("10081206", "UPP10081206-000034"));
    HYBillVO newBillvo = new HYBillVO();
    SuperVO newhead = (SuperVO)billvo.getParentVO().clone();
    if ((getBillUI().getUserObject() instanceof ISetGetPkWhenCopyInsert)) {
      ((ISetGetPkWhenCopyInsert)getBillUI().getUserObject()).setInvPk(newhead.getPrimaryKey());
    }
    newhead.setPrimaryKey(null);
    CircularlyAccessibleValueObject[] oldchildren = billvo.getChildrenVO();
    if ((oldchildren != null) && (oldchildren.length > 0)) {
      SuperVO[] newChildren = getNewChildrenVos(oldchildren);
      newBillvo.setChildrenVO(newChildren);
    }

    newBillvo.setParentVO(newhead);
    return newBillvo;
  }

  protected SuperVO[] getNewChildrenVos(CircularlyAccessibleValueObject[] oldchildren)
  {
    if (oldchildren == null)
      return null;
    SuperVO[] newChildren = new SuperVO[oldchildren.length];
    for (int i = 0; i < newChildren.length; i++) {
      newChildren[i] = ((SuperVO)oldchildren[i].clone());
      newChildren[i].setPrimaryKey(null);
    }
    return newChildren;
  }

  private void onAddwithDefaultData(HYBillVO newBillvo)
    throws Exception
  {
    onDefaultTempAdd();
    String psnCode = (String) getBillCardPanelWrapper().getBillCardPanel().getHeadItem("invcode").getValueObject();
    getBillCardPanelWrapper().setCardData(newBillvo);

    getBillCardPanelWrapper().getBillCardPanel().setHeadItem("invcode", psnCode);
    getBillCardPanelWrapper().getBillCardPanel().setHeadItem("invcode1", psnCode);
    getBillCardPanelWrapper().getBillCardPanel().setHeadItem("invcode2", psnCode);
    getBillCardPanelWrapper().getBillCardPanel().setHeadItem("invcode3", psnCode);
    getBillCardPanelWrapper().getBillCardPanel().setHeadItem("invcode4", psnCode);
    getBillCardPanelWrapper().getBillCardPanel().setHeadItem("invcode5", psnCode);
    setUIEnabled();
  }
  private void onAssign() {//分配事件
    Assign assingDlg = new Assign(getBillUI(), new InvClTreeData().getInvClTreeVO());
    assingDlg.setFlag(getAssignFlag());
    assingDlg.onDivide();
  }

  private void onDocManage()
  {
    throw new Error("Unresolved compilation problem: \n\tThe method getFileDir(Object, String) from the type AssociateFileUtil is not visible\n");
  }

  private InvbasdocVO getInvbasDocVo(HYBillVO billvo)
  {
    InvbasdocVO grpVO = null;
    if ((billvo instanceof IExAggVO))
      grpVO = ((BasManUnionVO)billvo.getParentVO()).getInvbasVo();
    else
      grpVO = (InvbasdocVO)billvo.getParentVO();
    return grpVO;
  }

  private void onTempAdd(boolean isBtnClick)
    throws Exception
  {
    checkCanAdd();
    if (isBtnClick) {
      String pk_billTemplet = getPk_billTemplate();
      this.m_pk_billtemplet = pk_billTemplet;
    }

    if (this.m_pk_billtemplet == null)
    {
      return;
    }

    BillData carddata = new BillData(getBillCardPanelWrapper().getBillCardPanel().getTempletData(this.m_pk_billtemplet));
    BillListData listdata = new BillListData(getBillTreeManageUI().getBillListPanel().getTempletData(this.m_pk_billtemplet));

    setbillData(carddata, listdata);
    getBillTreeManageUI().setListHeadData(getBufferData().getAllHeadVOsFromBuffer());
    onDefaultTempAdd();
    initBillItems();
    getBillCardPanelWrapper().getBillCardPanel().setHeadItem("pk_corp", getBillUI().getEnvironment().getCorporation().getPk_corp());

    InvclVO vo = (InvclVO)getSelectedNode().getData();
    getBillCardPanelWrapper().getBillCardPanel().setHeadItem("pk_invcl", vo.getPrimaryKey());
    setUIEnabled();
  }

  protected void initBillItems()
  {
    if ((getBillUI() instanceof InvBasDocUI))
      ((InvBasDocUI)getBillUI()).initSelfData();
  }

  private void setbillData(BillData carddata, BillListData listdata)
  {
    getBillCardPanelWrapper().getBillCardPanel().setBillData(carddata);
    getBillTreeManageUI().getBillListPanel().setListData(listdata);
    dealUIDef();
  }

  protected void dealUIDef()
  {
    ((InvBasDocUI)getBillTreeManageUI()).dealDefShow();
  }

  private String getPk_billTemplate()
  {
    if (this.billTempletDlg == null)
    {
      this.billTempletDlg = new BillTempletDlg(getBillUI(), getFunCode(), "@@@@");
    }
    int retCode = this.billTempletDlg.showModal();
    String pk_billTemplet = this.billTempletDlg.getSelectedBillTemplet();
    if (retCode == 2)
    {
      pk_billTemplet = null;
    }

    return pk_billTemplet;
  }

  protected String getFunCode()
  {
    return "10081206";
  }

  protected void setUIEnabled()
  {
    ((InvBasDocUI)getBillUI()).setAssistunitEnabled();
  }
  protected int getAssignFlag() {
    return 15;
  }
  protected void onBoRefresh() throws Exception {
    if (((BillManageUI)getBillUI()).isListPanelSelected()) {
      String selectedPk = null;
      boolean isleaf = false;
      if ((getSelectedNode() != null) && (!getSelectedNode().isRoot())) {
        selectedPk = getSelectedNode().getData().getPrimaryKey();
        isleaf = getSelectedNode().isLeaf();
      }

      getUITree().setModel(((BillTreeManageUI)getBillUI()).getBillTreeModel(null));
      getBufferData().clear();
      getBillUI().setListHeadData(null);
      getBufferData().setCurrentRow(-1);
      getNodeInfo().clear();
      setQueryCondition(null);
      if (isleaf)
        ((IResetSelectedTreeNode)getBillUI()).resetSelectedTreeNode(selectedPk);
    }
    else {
      refleshOnCard();
    }
  }

  protected UIDialog getQueryUI()
    throws Exception
  {
    QueryConditionClient querydlg = (QueryConditionClient)super.getQueryUI();
    UIRefPane ref = new UIRefPane();
    ref.setRefNodeName("存货分类");
    ref.setIncludeSubShow(true);
    ref.getRefModel().setIncludeSub(true);
    querydlg.setValueRef("bd_invbasdoc.pk_invcl", ref);
    return querydlg;
  }

  protected void refleshOnCard()
    throws Exception
  {
    super.onBoRefresh();
  }

  protected void onBoQuery() throws Exception {
    StringBuffer strWhere = new StringBuffer();

    if (!askForQueryCondition(strWhere)) {
      return;
    }
    setQueryCondition(strWhere.toString());
    getUITree().setSelectionRow(0);
    clearHtPage();
    getNodeInfo().clear();
    pageChanged(1);
  }

  protected UITree getUITree()
  {
    return ((InvBasDocUI)getBillUI()).getBillTree();
  }
  public String getQueryCondition() {
    return this.queryCondition;
  }
  public void setQueryCondition(String queryCondition) {
    this.queryCondition = queryCondition;
  }
//保存方法
  protected void onBoSave()
    throws Exception
  {
    getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
    super.onBoSave();
    InvbasdocVO basvo = getInvbasDocVo((HYBillVO)getBufferData().getCurrentVO());
    writeOperateLog(basvo, OperateType.SAVE);
    addNewData2Buf();

    if ((getBillUI().getUserObject() instanceof ISetGetPkWhenCopyInsert)) {
      this._corpedPK = ((ISetGetPkWhenCopyInsert)getBillUI().getUserObject()).getInvPk();
      ((ISetGetPkWhenCopyInsert)getBillUI().getUserObject()).setInvPk(null);
    }
    onContinueAdd();
  }

  private void writeOperateLog(InvbasdocVO invVO, OperateType oper) throws BusinessException {
    String nodeCode = "10081208";
    if ("0001".equals(invVO.getPk_corp()))
      nodeCode = "10081206";
    BDLogUtil.writeLog4BD(nodeCode, invVO.getPrimaryKey(), invVO.getInvcode(), oper, null);
  }

  private void addNewData2Buf()
  {
    Hashtable page = getHtPage(getSelectedNode());
    SuperVO header = (SuperVO)getBufferData().getCurrentVO().getParentVO();
    SuperVO[] currentvo = (SuperVO[])page.get(new Integer(1));
    if ((currentvo != null) && (currentvo.length > 0)) {
      ArrayList all = new ArrayList();
      ArrayList pks = new ArrayList();
      for (int i = 0; i < currentvo.length; i++) {
        all.add(currentvo[i]);
        pks.add(currentvo[i].getPrimaryKey());
      }

      if (!pks.contains(header.getPrimaryKey())) {
        all.add(header);
      }
      page.put(new Integer(this._currentPageIndex), (SuperVO[])all.toArray(new SuperVO[0]));
    }
    else {
      page.put(new Integer(this._currentPageIndex), new SuperVO[] { header });
    }
  }

  private void onContinueAdd()
    throws Exception
  {
    switch (this.curAdd) {
    case 109:
      onDefaultTempAdd();
      break;
    case 106:
      onCopyInsert(false);
      break;
    case 107:
      onTempAdd(false);
      break;
    case 108:
    default:
      onBoRefresh();
    }
  }

  protected void onBoLineAdd()
    throws Exception
  {
    checkCanAddLine();
    super.onBoLineAdd();
  }

  protected void onBoLineIns()
    throws Exception
  {
    checkCanAddLine();
    super.onBoLineIns();
  }

  protected void onBoLinePaste()
    throws Exception
  {
    checkCanAddLine();
    super.onBoLinePaste();
  }
  private void checkCanAddLine() throws BusinessException {
    boolean isAssistUnitSelected = new UFBoolean(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("assistunit").getValueObject().toString()).booleanValue();
    if ((!isAssistUnitSelected) && 
      (getBillCardPanelWrapper().getBillCardPanel().getCurrentBodyTableCode().equals("bodymeas")))
      throw new BusinessException(NCLangRes.getInstance().getStrByID("10081206", "UPT10081206-000029"));
  }

  protected void onBoEdit()
    throws Exception
  {
    this.curAdd = 0;
    super.onBoEdit();
    setMeasDocEnabled();
  }

  protected void setMeasDocEnabled()
  {
    boolean isselected = new UFBoolean(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("assistunit").getValueObject().toString()).booleanValue();

    String[] assistUnits = { "pk_measdoc1", "pk_measdoc2", "pk_measdoc3", "pk_measdoc5" };
    for (int i = 0; i < assistUnits.length; i++)
      getBillCardPanelWrapper().getBillCardPanel().getHeadItem(assistUnits[i]).setEnabled(isselected);
  }

  protected void onBoPrint()
    throws Exception
  {
    try
    {
      PrintEntry print = new PrintEntry(getBillUI());
      print.setTemplateID(ClientEnvironment.getInstance().getCorporation().getPk_corp(), getFunCode(), ClientEnvironment.getInstance().getUser().getPrimaryKey(), null);

      if (print.selectTemplate() < 0) {
        return;
      }

      GeneralExVO printData = new GeneralExVO();

      CircularlyAccessibleValueObject vo = null;
      if (getBillTreeManageUI().getBillTreeSelectNode() != null)
        vo = getBillTreeManageUI().getBillTreeSelectNode().getData();
      if (vo != null) {
        GeneralSuperVO headVO = new GeneralSuperVO();
        String[] attrNames = vo.getAttributeNames();
        for (int i = 0; i < attrNames.length; i++)
        {
          headVO.setAttributeValue("h_" + attrNames[i], vo.getAttributeValue(attrNames[i]));
        }
        printData.setParentVO(headVO);
      }

      CircularlyAccessibleValueObject[] vos = PrintDataUtil.getVOsfromBillModel(getBillTreeManageUI().getBillListPanel().getHeadBillModel());

      for (int i = 0; i < (vos == null ? 0 : vos.length); i++) {
        if (vos[i].getAttributeValue("length") == null) {
          vos[i].setAttributeValue("length", " ");
        }
      }
      printData.addTableVO("body", "body", vos);

      IDataSource dataSource = new PrintVODataSource(printData, print, "h_", null);
      IGetDefCodeOrName getdefcodeOrName = new DefaultGetDefCodeOrName(dataSource);
      dataSource = new PrtDefDealedDecorator(new DefaultCheckIsDef(), getdefcodeOrName, dataSource);
      print.setDataSource(dataSource);

      print.preview();
    }
    catch (Throwable ex)
    {
      ex.printStackTrace();
    }
  }

  protected void onBoCancel()
    throws Exception
  {
    super.onBoCancel();

    if ((getBillUI().getUserObject() instanceof ISetGetPkWhenCopyInsert))
      ((ISetGetPkWhenCopyInsert)getBillUI().getUserObject()).setInvPk(null);
  }

  protected void onBoDelete()
    throws Exception
  {
    InvbasdocVO invVo = getInvbasDocVo((HYBillVO)getBufferData().getCurrentVO());
    String currentpk = invVo.getPrimaryKey();
    super.onBoDelete();
    writeOperateLog(invVo, OperateType.DELETE);
    SuperVO[] currentvos = (SuperVO[])getHtPage(getSelectedNode()).get(new Integer(this._currentPageIndex));
    ArrayList al = new ArrayList();
    for (int i = 0; i < currentvos.length; i++) {
      if (!currentpk.equals(currentvos[i].getPrimaryKey()))
        al.add(currentvos[i]);
    }
    getHtPage(getSelectedNode()).put(new Integer(this._currentPageIndex), (SuperVO[])al.toArray(new SuperVO[0]));
  }
}