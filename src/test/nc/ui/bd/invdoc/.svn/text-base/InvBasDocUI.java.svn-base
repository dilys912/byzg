package nc.ui.bd.invdoc;

import java.util.Enumeration;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.uap.sfapp.IBillcodeRuleService;
import nc.ui.bd.def.ManageDefShowUtil;
import nc.ui.bd.pub.AssignButnVO;
import nc.ui.bd.pub.CopyAddButnVO;
import nc.ui.bd.pub.DefaultTempAdd;
import nc.ui.bd.pub.DocManageButnVO;
import nc.ui.bd.pub.ParentAddButnVO;
import nc.ui.bd.pub.TempAddButnVO;
import nc.ui.bill.depend.PageNavigationBar;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITree;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillScrollPane;
import nc.ui.pub.para.SysInitBO_Client;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bill.BillCardPanelWrapper;
import nc.ui.trade.bill.BillListPanelWrapper;
import nc.ui.trade.buffer.BillUIBuffer;
import nc.ui.trade.button.ButtonManager;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.trade.pub.IVOTreeData;
import nc.ui.trade.pub.TableTreeNode;
import nc.ui.trade.pub.VOTreeNode;
import nc.ui.trade.treemanage.BillTreeManageUI;
import nc.vo.bd.CorpVO;
import nc.vo.bd.b14.InvclVO;
import nc.vo.bd.invdoc.InvbasGetChkClass;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.trade.button.ButtonVO;

public class InvBasDocUI extends BillTreeManageUI
  implements INeedListPageCtl, IResetSelectedTreeNode
{
  private static final long serialVersionUID = 2461080626069298297L;
  private IListPnlPageCtrl m_listPnlPageCtl = null;
  private String[] assistUnits = { "pk_measdoc1", "pk_measdoc2", "pk_measdoc3", "pk_measdoc5" };
  private Object m_userObject;

  protected AbstractManageController createController()
  {
    return new InvBasDocControl();
  }

  public InvBasDocUI()
  {
    getBillListPanel().getParentListPanel().getPageNavigationBar().addPageNavigationListener(getListPnlPageCtl());
  }

  protected IVOTreeData createTableTreeData()
  {
    return null;
  }

  protected IVOTreeData createTreeData()
  {
    return null;
  }

  protected void setHeadSpecialData(CircularlyAccessibleValueObject vo, int intRow)
    throws Exception
  {
  }

  protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
    throws Exception
  {
  }

  protected void initSelfData()
  {
    getBillListPanel().getParentListPanel().setPageNavigationBarVisible(true);
    dealDefShow();
    try {
      getBillCardWrapper().setCardDecimalDigits(null, new String[][] { { "mainmeasrate" }, { "8" } });
      getBillListWrapper().setListDecimalDigits(null, new String[][] { { "mainmeasrate" }, { "8" } });
    } catch (Exception e) {
      e.printStackTrace();
    }
    BillItem cardItem = getBillCardPanel().getHeadItem("pk_invcl");
    if (cardItem != null) {
      UIRefPane invclRef = (UIRefPane)cardItem.getComponent();
      invclRef.setNotLeafSelectedEnabled(false);
    }
    getBillCardPanel().setAutoExecHeadEditFormula(true);
  }

  protected ManageEventHandler createEventHandler() {
    return new InvBasDocEventHandler(this, getUIControl());
  }

  public IListPnlPageCtrl getListPnlPageCtl()
  {
    if (this.m_listPnlPageCtl == null)
      this.m_listPnlPageCtl = new ListPnlPageCtrl();
    return this.m_listPnlPageCtl;
  }
  public Object getUserObject() {
    if (this.m_userObject == null)
      this.m_userObject = new InvbasGetChkClass();
    return this.m_userObject;
  }

  public void setDefaultData() throws Exception {
    String strCorp = getClientEnvironment().getCorporation().getPk_corp();
    getBillCardPanel().setHeadItem("pk_corp", strCorp);

    InvclVO vo = (InvclVO)getBillTreeSelectNode().getData();
    getBillCardPanel().setHeadItem("pk_invcl", vo.getPrimaryKey());

    setAssistunitEnabled();
    if (isAutoGenCode()) {
      IBillcodeRuleService iService = (IBillcodeRuleService)NCLocator.getInstance().lookup(IBillcodeRuleService.class.getName());
      String psnCode = iService.getBillCode_RequiresNew("inventory", strCorp, null, null);
      getBillCardPanel().setHeadItem("invcode", psnCode);
      getBillCardPanel().setHeadItem("invcode2", psnCode);
      getBillCardPanel().setHeadItem("invcode3", psnCode);
      getBillCardPanel().setHeadItem("invcode1", psnCode);
    }
  }

  public void setAssistunitEnabled()
  {
    UIRefPane[] uiref = getMeasureRef();
    boolean assiunitIsSelected = ((UICheckBox)getBillCardPanel().getHeadItem("assistunit").getComponent()).isSelected();//assistunit辅计量管理
    for (int i = 1; i < uiref.length; i++)
      uiref[i].setEnabled(assiunitIsSelected);
  }

  public void afterEdit(BillEditEvent e) {
    getBasAfterEdit().afterEdit(e);
  }

  private BasAfterEdit getBasAfterEdit()
  {
    return new BasAfterEdit(this);
  }

  private UIRefPane[] getMeasureRef()
  {
    UIRefPane[] refMeasPanes = new UIRefPane[5];
    refMeasPanes[0] = ((UIRefPane)getBillCardPanel().getHeadItem("pk_measdoc").getComponent());//pk_measdoc主计量单位主键
    for (int i = 1; i < refMeasPanes.length; i++) {
      refMeasPanes[i] = ((UIRefPane)getBillCardPanel().getHeadItem(this.assistUnits[(i - 1)]).getComponent());
    }
    return refMeasPanes;
  }

  protected void initPrivateButton()
  {
    TempAddButnVO btnvo = new TempAddButnVO();
    addPrivateButton(btnvo.getButtonVO());

    CopyAddButnVO copybtnvo = new CopyAddButnVO();
    addPrivateButton(copybtnvo.getButtonVO());

    AssignButnVO assignbtnvo = new AssignButnVO();
    addPrivateButton(assignbtnvo.getButtonVO());

    DocManageButnVO docmangbtnvo = new DocManageButnVO();
    addPrivateButton(docmangbtnvo.getButtonVO());

    DefaultTempAdd defaulttempbtnvo = new DefaultTempAdd();
    addPrivateButton(defaulttempbtnvo.getButtonVO());

    ParentAddButnVO addvo = new ParentAddButnVO();
    addPrivateButton(addvo.getButtonVO());

    ButtonVO calute = new ButtonVO();
    calute.setBtnNo(112);
    calute.setBtnName("计算换算率");
    calute.setOperateStatus(new int[] { 1 });
    addPrivateButton(calute);

    ButtonVO updatedata = new ButtonVO();
    updatedata.setBtnNo(113);
    updatedata.setBtnName("修改历史数据");
    updatedata.setOperateStatus(new int[] { 1 });
    addPrivateButton(updatedata);
  }

  public UITree getBillTree() {
    return super.getBillTree();
  }

  public void dealDefShow() {
    String[] strDefObjs = { "存货档案" };
    String[] strPrefix = { "def" };
    try
    {
      new ManageDefShowUtil(getBillCardPanel(), getBillListPanel()).showDefWhenRef(strDefObjs, strPrefix, true);
    } catch (Exception e) {
      Logger.error(e.getMessage(), e);
    }
  }

  public void resetSelectedTreeNode(String selectedKey)
  {
    if ((selectedKey == null) || (selectedKey.length() == 0))
      return;
    DefaultMutableTreeNode root = (DefaultMutableTreeNode)getBillTree()
      .getModel().getRoot();
    if (root != null) {
      Enumeration elements = root.breadthFirstEnumeration();
      elements.nextElement();
      while (elements.hasMoreElements()) {
        VOTreeNode node = (VOTreeNode)elements.nextElement();
        if (selectedKey.equals(node.getNodeID())) {
          TreePath path = new TreePath(node.getPath());
          getBillTree().makeVisible(path);
          getBillTree().setSelectionPath(path);

          getBillCardPanel().updateValue();
          break;
        }
      }
    }
  }

  public DefaultTreeModel getBillTreeModel(IVOTreeData voTreeData)
  {
    return new InvClTreeData().getBillTreeModel();
  }

  protected void onTreeSelectSetButtonState(TableTreeNode selectnode)
  {
    try {
      if ((getBufferData().getVOBufferSize() == 0) || (!selectnode.isLeaf()))
        setBillOperate(4);
      else {
        setBillOperate(2);
      }

      getButtonManager().getButton(8).setEnabled(true);
      updateButtons();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private boolean isAutoGenCode() {
    try {
      return SysInitBO_Client.getParaBoolean(getClientEnvironment().getCorporation().getPk_corp(), "BD036").booleanValue();
    } catch (Exception e) {
      Logger.error(e.getMessage(), e);
    }
    return false;
  }
}