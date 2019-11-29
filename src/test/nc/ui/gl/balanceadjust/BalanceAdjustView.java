package nc.ui.gl.balanceadjust;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintStream;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import nc.ui.gl.contrastpub.AccountComBox;
import nc.ui.gl.contrastpub.LinkComBox;
import nc.ui.glpub.IParent;
import nc.ui.glpub.IUiPanel;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UISplitPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.UITablePane;
import nc.vo.bd.CorpVO;
import nc.vo.gl.balanceadjust.ColumnmodeVO;
import nc.vo.gl.contrast.AccountlinkVO;
import nc.vo.gl.contrast.GLQueryVO;
import nc.vo.gl.contrast.GlContrastVO;
import nc.vo.gl.contrastpub.CTransferVO;
import nc.vo.glpub.GlBusinessException;
import nc.vo.pub.lang.UFDate;

public class BalanceAdjustView extends ToftPanel
  implements MouseListener, IView, IUiPanel
{
  private ButtonObject m_bnbill = new ButtonObject(NCLangRes.getInstance().getStrByID("2004365802", "UPP2004365802-000001"), NCLangRes.getInstance().getStrByID("2004365802", "UPP2004365802-000001"), 2, "单据");
  private ButtonObject m_bnprint = new ButtonObject(NCLangRes.getInstance().getStrByID("2004365802", "UPP2004365802-000002"), NCLangRes.getInstance().getStrByID("2004365802", "UPP2004365802-000002"), 2, "打印");
  private ButtonObject m_bnrefresh = new ButtonObject(NCLangRes.getInstance().getStrByID("2004365802", "UPP2004365802-000003"), NCLangRes.getInstance().getStrByID("2004365802", "UPP2004365802-000003"), 2, "刷新");
  private ButtonObject m_bnreturn = new ButtonObject(NCLangRes.getInstance().getStrByID("2004365802", "UPP2004365802-000004"), NCLangRes.getInstance().getStrByID("2004365802", "UPP2004365802-000004"), 2, "返回");
  private ButtonObject[] m_currentbuttons = null;
  private IParent m_parent;
  private BalanceModel m_model;
  private AccountComBox ivjCmbAccount = null;
  private UILabel ivjLAccount = null;
  private UILabel ivjLEndDate = null;
  private UILabel ivjLEndDateTag = null;
  private UILabel ivjLTemp1 = null;
  private UILabel ivjLTemp2 = null;
  private UIPanel ivjPTemp1 = null;
  private UIPanel ivjPTemp2 = null;
  private UIPanel ivjPTemp3 = null;
  private UIPanel ivjPTemp4 = null;
  private UIPanel ivjPTemp5 = null;
  private UIPanel ivjPTop = null;
  private UISplitPane ivjSPCenter = null;
  private UITablePane ivjTabContrast = null;
  private BalanceadjustTableModel ivjBookTableModel = null;
  private BalanceadjustTableModel ivjContrastTableModel = null;
  private UITablePane ivjTabAccountBook = null;
  IvjEventHandler ivjEventHandler = new IvjEventHandler();
  private UILabel ivjLAccount1 = null;
  private LinkComBox ivjLinkComBox1 = null;
  private UILabel ivjLTemp11 = null;
  private UIPanel ivjPTemp41 = null;

  public BalanceAdjustView()
  {
    initialize();
  }

  public void addListener(Object objListener, Object objUserdata)
  {
  }

  public void cmbAccount_ItemStateChanged(ItemEvent itemEvent)
  {
    try
    {
      getBalanceModel().setCurrentaccount(getCmbAccount().getAccount().getPk_contrastaccount());
      refreshUI();
    }
    catch (Exception e)
    {
    }
  }

  private void connEtoC1(ItemEvent arg1)
  {
    try
    {
      cmbAccount_ItemStateChanged(arg1);
    }
    catch (Throwable ivjExc)
    {
      handleException(ivjExc);
    }
  }

  private void connEtoC2()
  {
    try
    {
      linkComBox1_ActionEvents();
    }
    catch (Throwable ivjExc)
    {
      handleException(ivjExc);
    }
  }

  private BalanceModel getBalanceModel()
  {
    if (this.m_model == null) {
      try {
        this.m_model = new BalanceModel();

        this.m_model.setUI(this);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.m_model;
  }

  private BalanceadjustTableModel getBookTableModel()
  {
    if (this.ivjBookTableModel == null) {
      try {
        this.ivjBookTableModel = new BalanceadjustTableModel();
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjBookTableModel;
  }
  public ButtonObject[] getButtons() {
    if (this.m_currentbuttons == null) {
      this.m_bnbill.setVisible(false);
      this.m_currentbuttons = new ButtonObject[4];
      this.m_currentbuttons[0] = this.m_bnbill;
      this.m_currentbuttons[1] = this.m_bnprint;
      this.m_currentbuttons[2] = this.m_bnrefresh;
      this.m_currentbuttons[3] = this.m_bnreturn;
      for (int i = 0; i < this.m_currentbuttons.length; i++) {
        this.m_currentbuttons[i].setTag(this.m_currentbuttons[i].getName());
      }
    }
    return this.m_currentbuttons;
  }

  public ClientEnvironment getClient()
  {
    return getClientEnvironment();
  }

  private AccountComBox getCmbAccount()
  {
    if (this.ivjCmbAccount == null) {
      try {
        this.ivjCmbAccount = new AccountComBox();
        this.ivjCmbAccount.setName("CmbAccount");
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjCmbAccount;
  }

  private ColumnmodeVO[] getColumnsMode()
  {
    ColumnmodeVO[] columns = new ColumnmodeVO[2];
    ColumnmodeVO column1 = new ColumnmodeVO();
    column1.setColumnindex(new Integer(1));
    column1.setColumnkey(new Integer(100));
    column1.setColumnname(NCLangRes.getInstance().getStrByID("2004365802", "UPP2004365802-000005"));
    column1.setColumnwidth(new Integer(100));
    ColumnmodeVO column2 = new ColumnmodeVO();
    column2.setColumnindex(new Integer(2));
    column2.setColumnkey(new Integer(51));
    column2.setColumnname(NCLangRes.getInstance().getStrByID("2004365802", "UPP2004365802-000006"));
    column2.setColumnwidth(new Integer(100));
    columns[0] = column1;
    columns[1] = column2;

    return columns;
  }

  private BalanceadjustTableModel getContrastTableModel()
  {
    if (this.ivjContrastTableModel == null) {
      try {
        this.ivjContrastTableModel = new BalanceadjustTableModel();
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjContrastTableModel;
  }

  private UILabel getLAccount()
  {
    if (this.ivjLAccount == null) {
      try {
        this.ivjLAccount = new UILabel();
        this.ivjLAccount.setName("LAccount");
        this.ivjLAccount.setText(NCLangRes.getInstance().getStrByID("2004365802", "UPP2004365802-000007"));
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjLAccount;
  }

  private UILabel getLAccount1()
  {
    if (this.ivjLAccount1 == null) {
      try {
        this.ivjLAccount1 = new UILabel();
        this.ivjLAccount1.setName("LAccount1");
        this.ivjLAccount1.setText(NCLangRes.getInstance().getStrByID("2004365802", "UPP2004365802-000008"));
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjLAccount1;
  }

  private UILabel getLEndDate()
  {
    if (this.ivjLEndDate == null) {
      try {
        this.ivjLEndDate = new UILabel();
        this.ivjLEndDate.setName("LEndDate");
        this.ivjLEndDate.setText("");
        this.ivjLEndDate.setMaximumSize(new Dimension(100, 22));
        this.ivjLEndDate.setForeground(Color.black);
        this.ivjLEndDate.setILabelType(0);
        this.ivjLEndDate.setPreferredSize(new Dimension(100, 22));
        this.ivjLEndDate.setAlignmentX(0.0F);
        this.ivjLEndDate.setFont(new Font("dialog", 0, 12));
        this.ivjLEndDate.setMinimumSize(new Dimension(100, 22));
        this.ivjLEndDate.setHorizontalAlignment(4);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjLEndDate;
  }

  private UILabel getLEndDateTag()
  {
    if (this.ivjLEndDateTag == null) {
      try {
        this.ivjLEndDateTag = new UILabel();
        this.ivjLEndDateTag.setName("LEndDateTag");
        this.ivjLEndDateTag.setPreferredSize(new Dimension(60, 22));
        this.ivjLEndDateTag.setText(NCLangRes.getInstance().getStrByID("2004365802", "UPP2004365802-000009"));
        this.ivjLEndDateTag.setMaximumSize(new Dimension(60, 22));
        this.ivjLEndDateTag.setMinimumSize(new Dimension(60, 22));
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjLEndDateTag;
  }

  private LinkComBox getLinkComBox1()
  {
    if (this.ivjLinkComBox1 == null) {
      try {
        this.ivjLinkComBox1 = new LinkComBox();
        this.ivjLinkComBox1.setName("LinkComBox1");

        Dimension dim = new Dimension();
        dim.setSize(200, 22);
        this.ivjLinkComBox1.setMaximumSize(dim);
        this.ivjLinkComBox1.setMinimumSize(dim);
        this.ivjLinkComBox1.setPreferredSize(dim);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjLinkComBox1;
  }

  private UILabel getLTemp1()
  {
    if (this.ivjLTemp1 == null) {
      try {
        this.ivjLTemp1 = new UILabel();
        this.ivjLTemp1.setName("LTemp1");
        this.ivjLTemp1.setPreferredSize(new Dimension(15, 22));
        this.ivjLTemp1.setText("");
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjLTemp1;
  }

  private UILabel getLTemp11()
  {
    if (this.ivjLTemp11 == null) {
      try {
        this.ivjLTemp11 = new UILabel();
        this.ivjLTemp11.setName("LTemp11");
        this.ivjLTemp11.setPreferredSize(new Dimension(15, 22));
        this.ivjLTemp11.setText("");
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjLTemp11;
  }

  private UILabel getLTemp2()
  {
    if (this.ivjLTemp2 == null) {
      try {
        this.ivjLTemp2 = new UILabel();
        this.ivjLTemp2.setName("LTemp2");
        this.ivjLTemp2.setPreferredSize(new Dimension(15, 22));
        this.ivjLTemp2.setText("");
        this.ivjLTemp2.setMaximumSize(new Dimension(15, 22));
        this.ivjLTemp2.setMinimumSize(new Dimension(15, 22));
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjLTemp2;
  }

  private UIPanel getPTemp1()
  {
    if (this.ivjPTemp1 == null) {
      try {
        this.ivjPTemp1 = new UIPanel();
        this.ivjPTemp1.setName("PTemp1");
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjPTemp1;
  }

  private UIPanel getPTemp2()
  {
    if (this.ivjPTemp2 == null) {
      try {
        this.ivjPTemp2 = new UIPanel();
        this.ivjPTemp2.setName("PTemp2");
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjPTemp2;
  }

  private UIPanel getPTemp3()
  {
    if (this.ivjPTemp3 == null) {
      try {
        this.ivjPTemp3 = new UIPanel();
        this.ivjPTemp3.setName("PTemp3");
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjPTemp3;
  }

  private UIPanel getPTemp4()
  {
    if (this.ivjPTemp4 == null) {
      try {
        this.ivjPTemp4 = new UIPanel();
        this.ivjPTemp4.setName("PTemp4");
        this.ivjPTemp4.setLayout(new BoxLayout(getPTemp4(), 0));
        this.ivjPTemp4.add(getLTemp1());
        getPTemp4().add(getLAccount(), getLAccount().getName());
        getPTemp4().add(getCmbAccount(), getCmbAccount().getName());
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjPTemp4;
  }

  private UIPanel getPTemp41()
  {
    if (this.ivjPTemp41 == null) {
      try {
        this.ivjPTemp41 = new UIPanel();
        this.ivjPTemp41.setName("PTemp41");
        this.ivjPTemp41.setLayout(new BoxLayout(getPTemp41(), 0));
        this.ivjPTemp41.add(getLTemp11());
        getPTemp41().add(getLAccount1(), getLAccount1().getName());
        getPTemp41().add(getLinkComBox1(), getLinkComBox1().getName());
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjPTemp41;
  }

  private UIPanel getPTemp5()
  {
    if (this.ivjPTemp5 == null) {
      try {
        this.ivjPTemp5 = new UIPanel();
        this.ivjPTemp5.setName("PTemp5");
        this.ivjPTemp5.setAlignmentX(0.5F);
        this.ivjPTemp5.setLayout(new BoxLayout(getPTemp5(), 0));
        getPTemp5().add(getLEndDateTag(), getLEndDateTag().getName());
        this.ivjPTemp5.add(getLEndDate());
        this.ivjPTemp5.add(getLTemp2());
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjPTemp5;
  }

  private UIPanel getPTop()
  {
    if (this.ivjPTop == null) {
      try {
        this.ivjPTop = new UIPanel();
        this.ivjPTop.setName("PTop");
        this.ivjPTop.setPreferredSize(new Dimension(10, 30));
        this.ivjPTop.setLayout(new BorderLayout());
        getPTop().add(getPTemp4(), "West");
        getPTop().add(getPTemp5(), "East");
        getPTop().add(getPTemp41(), "Center");
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjPTop;
  }

  private UISplitPane getSPCenter()
  {
    if (this.ivjSPCenter == null) {
      try {
        this.ivjSPCenter = new UISplitPane(1);
        this.ivjSPCenter.setName("SPCenter");
        getSPCenter().add(getTabAccountBook(), "left");
        getSPCenter().add(getTabContrast(), "right");
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjSPCenter;
  }

  private UITablePane getTabAccountBook()
  {
    if (this.ivjTabAccountBook == null) {
      try {
        this.ivjTabAccountBook = new UITablePane();
        this.ivjTabAccountBook.setName("TabAccountBook");
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjTabAccountBook;
  }

  private UITablePane getTabContrast()
  {
    if (this.ivjTabContrast == null) {
      try {
        this.ivjTabContrast = new UITablePane();
        this.ivjTabContrast.setName("TabContrast");
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjTabContrast;
  }

  public String getTitle()
  {
    return NCLangRes.getInstance().getStrByID("2004365802", "UPP2004365802-000010");
  }

  private void handleException(Throwable exception)
  {
    System.out.println("--------- 未捕捉到的异常 ---------");
    exception.printStackTrace(System.out);
  }

  private void initConnections()
    throws Exception
  {
    getTabAccountBook().getTable().addMouseListener(this);
    getTabContrast().getTable().addMouseListener(this);

    getCmbAccount().addItemListener(this.ivjEventHandler);
    getLinkComBox1().addActionListener(this.ivjEventHandler);
  }

  private void initialize()
  {
    try
    {
      setName("BalanceAdjustView");
      setSize(774, 419);
      add(getPTop(), "North");
      add(getPTemp3(), "South");
      add(getPTemp1(), "West");
      add(getPTemp2(), "East");
      add(getSPCenter(), "Center");
      initConnections();
    } catch (Throwable ivjExc) {
      handleException(ivjExc);
    }

    GLQueryVO q = new GLQueryVO();
    q.setPK_corp(getClientEnvironment().getCorporation().getPk_corp());
    getCmbAccount().setData(q);
    initTable();
  }

  private void initTable() {
    getSPCenter().setDividerLocation(getSize().width / 2 - 10);
    getBookTableModel().setTableModeVO(getColumnsMode());
    getContrastTableModel().setTableModeVO(getColumnsMode());
    getTabAccountBook().getTable().setModel(getBookTableModel());
    getTabContrast().getTable().setModel(getContrastTableModel());
    for (int i = 0; i < getColumnsMode().length; i++) {
      getTabAccountBook().getTable().getColumnModel().getColumn(i).setWidth(getColumnsMode()[i].getColumnwidth().intValue());

      getTabContrast().getTable().getColumnModel().getColumn(i).setWidth(getColumnsMode()[i].getColumnwidth().intValue());

      switch (getColumnsMode()[i].getColumnkey().intValue())
      {
      case 5:
      case 6:
      case 51:
        getTabAccountBook().getTable().getColumnModel().getColumn(i).setCellRenderer(new DefaultTableCellRenderer());

        ((DefaultTableCellRenderer)getTabAccountBook().getTable().getColumnModel().getColumn(i).getCellRenderer()).setHorizontalAlignment(4);

        getTabContrast().getTable().getColumnModel().getColumn(i).setCellRenderer(new DefaultTableCellRenderer());

        ((DefaultTableCellRenderer)getTabContrast().getTable().getColumnModel().getColumn(i).getCellRenderer()).setHorizontalAlignment(4);
      }
    }
  }

  public Object invoke(Object objData, Object objUserData)
  {
    if (objUserData.toString().trim().equals("balancemodel"))
      this.m_model = ((BalanceModel)objData);
    try {
      getCmbAccount().setEnabled(true);
      getCmbAccount().setSelectedAccount(getBalanceModel().getCurrentaccount());
      if (getBalanceModel().isIsSpread()) {
        getLinkComBox1().setEnabled(false);
        getLinkComBox1().setData(getBalanceModel().getCurrentaccount(), false);
        getLinkComBox1().setSelectLink(getBalanceModel().getCurrentlink());
        getLinkComBox1().setEnabled(true);
      } else {
        getLinkComBox1().removeAllItems();
        getLinkComBox1().setEnabled(false);
      }
      getCmbAccount().setEnabled(false);
      refreshUI();
    } catch (Exception e) {
      e.printStackTrace();
    }
    getTabAccountBook().getTable().updateUI();
    return null;
  }

  public void linkComBox1_ActionEvents()
  {
    try
    {
      if (getBalanceModel().isIsSpread()) {
        getBalanceModel().setCurrentaccount(getCmbAccount().getAccount().getPk_contrastaccount());
        if ((getLinkComBox1().getSelectedIndex() >= 0) && (getLinkComBox1().isEnabled())) {
          getBalanceModel().setCurrentlink(getLinkComBox1().getSelectLinks().getPk_accountlink());
          getBalanceModel().setLinkName(getLinkComBox1().getSelectLinks().getM_ass());
          refreshUI();
        }
      }
    }
    catch (Exception e)
    {
    }
  }

  public static void main(String[] args)
  {
    try
    {
      JFrame frame = new JFrame();

      BalanceAdjustView aBalanceAdjustView = new BalanceAdjustView();
      frame.setContentPane(aBalanceAdjustView);
      frame.setSize(aBalanceAdjustView.getSize());
      frame.addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent e) {
          System.exit(0);
        }
      });
      frame.show();
      Insets insets = frame.getInsets();
      frame.setSize(frame.getWidth() + insets.left + insets.right, frame.getHeight() + insets.top + insets.bottom);
      frame.setVisible(true);
    } catch (Throwable exception) {
      System.err.println("nc.ui.pub.ToftPanel 的 main() 中发生异常");
      exception.printStackTrace(System.out);
    }
  }

  public void mouseClicked(MouseEvent e)
  {
    if (e.getClickCount() == 2)
      mouseDoubleClicked(e);
  }

  public void mouseDoubleClicked(MouseEvent e)
  {
    try
    {
      if (e.getSource() == getTabAccountBook().getTable()) {
        int selectedrow = getTabAccountBook().getTable().getSelectedRow();
        CTransferVO selectedvo = (CTransferVO)getBookTableModel().getVOs()[selectedrow];

        if (selectedvo.getUserData() == null) {
          return;
        }
        if (selectedvo.getUserData().toString().equals("+")) {
          if (selectedvo.getPk_account().equals("DEBIT")) {
            getBookTableModel().insertVOs(selectedrow + 1, getBalanceModel().getBankDebit(getBalanceModel().getCurrentaccountLink()));
          }
          else
          {
            getBookTableModel().insertVOs(selectedrow + 1, getBalanceModel().getBankCredit(getBalanceModel().getCurrentaccountLink()));
          }

          ((CTransferVO)getBookTableModel().getVOs()[selectedrow]).setUserData("-");
        }
        else if (selectedvo.getUserData().toString().equals("-")) {
          if (selectedvo.getPk_account().equals("DEBIT")) {
            getBookTableModel().removeVOs(selectedrow + 1, getBalanceModel().getBankDebit(getBalanceModel().getCurrentaccountLink()) == null ? 0 : getBalanceModel().getBankDebit(getBalanceModel().getCurrentaccountLink()).length);
          }
          else
          {
            getBookTableModel().removeVOs(selectedrow + 1, getBalanceModel().getBankCredit(getBalanceModel().getCurrentaccountLink()) == null ? 0 : getBalanceModel().getBankCredit(getBalanceModel().getCurrentaccountLink()).length);
          }

          ((CTransferVO)getBookTableModel().getVOs()[selectedrow]).setUserData("+");
        }

      }
      else if (e.getSource() == getTabContrast().getTable()) {
        int selectedrow = getTabContrast().getTable().getSelectedRow();
        CTransferVO selectedvo = (CTransferVO)getContrastTableModel().getVOs()[selectedrow];

        if (selectedvo.getUserData() == null) {
          return;
        }
        if (selectedvo.getUserData().toString().equals("+")) {
          if (selectedvo.getPk_account().equals("DEBIT")) {
            getContrastTableModel().insertVOs(selectedrow + 1, getBalanceModel().getBillDebit(getBalanceModel().getCurrentaccountLink()));
          }
          else
          {
            getContrastTableModel().insertVOs(selectedrow + 1, getBalanceModel().getBillCredit(getBalanceModel().getCurrentaccountLink()));
          }

          ((CTransferVO)getContrastTableModel().getVOs()[selectedrow]).setUserData("-");
        }
        else if (selectedvo.getUserData().toString().equals("-")) {
          if (selectedvo.getPk_account().equals("DEBIT")) {
            getContrastTableModel().removeVOs(selectedrow + 1, getBalanceModel().getBillDebit(getBalanceModel().getCurrentaccountLink()) == null ? 0 : getBalanceModel().getBillDebit(getBalanceModel().getCurrentaccountLink()).length);
          }
          else
          {
            getContrastTableModel().removeVOs(selectedrow + 1, getBalanceModel().getBillCredit(getBalanceModel().getCurrentaccountLink()) == null ? 0 : getBalanceModel().getBillCredit(getBalanceModel().getCurrentaccountLink()).length);
          }

          ((CTransferVO)getContrastTableModel().getVOs()[selectedrow]).setUserData("+");
        }

      }

    }
    catch (Exception exc)
    {
      exc.printStackTrace();
      throw new GlBusinessException(exc.getMessage());
    }
    getTabAccountBook().getTable().updateUI();
    getTabContrast().getTable().updateUI();
  }

  public void mouseEntered(MouseEvent e)
  {
  }

  public void mouseExited(MouseEvent e)
  {
  }

  public void mousePressed(MouseEvent e)
  {
  }

  public void mouseReleased(MouseEvent e)
  {
  }

  public void nextClosed()
  {
  }

  public void onButtonClicked(ButtonObject bo)
  {
    try
    {
      if (bo == this.m_bnreturn) {
        this.m_parent.closeMe();
      }
      else if (bo == this.m_bnrefresh) {
        refreshUI();
      }
      else if (bo == this.m_bnprint)
      {
        getBalanceModel().setBankPrintDate(getContrastTableModel().getVOs());
        getBalanceModel().setUnitPrintDate(getBookTableModel().getVOs());
        getBalanceModel().print();
      }
    } catch (Exception e) {
      e.printStackTrace();
      showErrorMessage(e.getMessage());
    }
  }

  private void refreshUI() throws Exception {
    if ((getBalanceModel().getCurrentaccount() == null) || (getBalanceModel().getCurrentaccount().equals("")))
      try {
        getBalanceModel().setCurrentaccount(getCmbAccount().getAccount().getPk_contrastaccount());
      }
      catch (Exception e)
      {
      }
    if (getBalanceModel().isIsSpread()) {
      getBookTableModel().setPk_curr(getBalanceModel().getAccount(getBalanceModel().getCurrentaccountLink()).getPk_currtype());
      getContrastTableModel().setPk_curr(getBalanceModel().getAccount(getBalanceModel().getCurrentaccountLink()).getPk_currtype());
      getBookTableModel().setVOs(getBalanceModel().getBillBalance(getBalanceModel().getCurrentaccountLink()));
      getContrastTableModel().setVOs(getBalanceModel().getBankBalance(getBalanceModel().getCurrentaccountLink()));
    } else {
      getBookTableModel().setPk_curr(getBalanceModel().getAccount(getBalanceModel().getCurrentaccountLink()).getPk_currtype());
      getContrastTableModel().setPk_curr(getBalanceModel().getAccount(getBalanceModel().getCurrentaccountLink()).getPk_currtype());
      getBookTableModel().setVOs(getBalanceModel().getBillBalance(getBalanceModel().getCurrentaccountLink()));
      getContrastTableModel().setVOs(getBalanceModel().getBankBalance(getBalanceModel().getCurrentaccountLink()));
    }
    getTabAccountBook().getTable().updateUI();
    getTabContrast().getTable().updateUI();
    getLEndDate().setText(getBalanceModel().getDate() == null ? "" : getBalanceModel().getDate().toString());
  }

  public void removeListener(Object objListener, Object objUserdata)
  {
  }

  public void showMe(IParent parent)
  {
    parent.getUiManager().removeAll();
    parent.getUiManager().add(this, getName());
    this.m_parent = parent;
    setFrame(parent.getFrame());
  }

  class IvjEventHandler
    implements ActionListener, ItemListener
  {
    IvjEventHandler()
    {
    }

    public void actionPerformed(ActionEvent e)
    {
      if (e.getSource() == BalanceAdjustView.this.getLinkComBox1())
        BalanceAdjustView.this.connEtoC2(); 
    }

    public void itemStateChanged(ItemEvent e) {
      if (e.getSource() == BalanceAdjustView.this.getCmbAccount())
        BalanceAdjustView.this.connEtoC1(e);
    }
  }
}