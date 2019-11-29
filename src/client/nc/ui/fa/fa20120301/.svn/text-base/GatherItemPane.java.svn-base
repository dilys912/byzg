package nc.ui.fa.fa20120301;

import java.awt.Font;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import nc.itf.fa.prv.ICardItem;
import nc.itf.fa.prv.IOption;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIList;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITextField;
import nc.vo.bd.CorpVO;
import nc.vo.bd.b52.GlbookVO;
import nc.vo.fa.AccBookAccessor;
import nc.vo.fa.ShareBase;
import nc.vo.fa.accperiod.AccperiodVO;
import nc.vo.fa.accperiod.PeriodManager;
import nc.vo.fa.closebook.FaCloseBook;
import nc.vo.fa.fa20120301.EnumDepVO;
import nc.vo.fa.fa20120301.GatherItemVO;
import nc.vo.fa.fa201206.CarditemVO;
import nc.vo.fa.favouchstyle.VoucherStyleVO;
import nc.vo.fa.pub.proxy.FAProxy;

public class GatherItemPane extends UIPanel
  implements ActionListener, ListSelectionListener
{
  private JButton ivjJBnDeSelectAll = null;
  private JButton ivjJBnDeSelectOne = null;
  private JButton ivjJBnDown = null;
  private JButton ivjJBnSelectAll = null;
  private JButton ivjJBnSelectOne = null;
  private JButton ivjJBnUp = null;
  private JLabel ivjJLabel1 = null;
  private JLabel ivjJLabel2 = null;
  private JList ivjJListLeft = null;
  private JList ivjJListRight = null;
  private JPanel ivjJPanel1 = null;
  private JPanel ivjJPanel11 = null;
  private JScrollPane ivjJScrollPane1 = null;
  private JScrollPane ivjJScrollPane2 = null;
  private JLabel ivjJLabelJcfw = null;
  private JLabel ivjJLabelTo = null;
  private JPanel ivjJPanelJC = null;
  private JTextField ivjJTfLastJc = null;
  private JTextField ivjJTfStartJc = null;
  private GatherItemVO[] m_voGatherItemLeft = null;
  private GatherItemVO[] m_voGatherItemRight = null;
  private Vector m_voItemLeft = null;
  private Vector m_voItemRight = null;
  private String m_sPk_Corp = "";
  private String m_sGroupPk = null;
  private JButton ivjJBnSet = null;
  private HashMap m_hCardItem = null;
  private String m_accyear = null;
  private String m_period = null;
  private PeriodManager m_cPeriodSrv = null;
  private Boolean m_bIsEditable = null;
  private JLabel ivjJLabelTip = null;

  private int m_iMaxCateCodeLevel = 0;
  private JButton ivjJBnSort = null;

  private String m_accbook = null;

  public GatherItemPane()
  {
    initialize();
  }

  public void doBtnSort() {
    int index = getJListRight().getSelectedIndex();
    if (index < 0) {
      return;
    }
    DefaultListModel dlm = (DefaultListModel)getJListRight().getModel();
    Object o = dlm.get(index);
    GatherItemVO vo = (GatherItemVO)o;
    if (vo.getItem_name().startsWith("*"))
      vo.setItem_name(vo.getItem_name().substring(1, vo.getItem_name().length()));
    else {
      vo.setItem_name("*" + vo.getItem_name());
    }

    getJListRight().repaint();
  }

  public GatherItemPane(LayoutManager p0)
  {
    super(p0);
  }

  public GatherItemPane(LayoutManager p0, boolean p1)
  {
    super(p0, p1);
  }

  public GatherItemPane(boolean p0)
  {
    super(p0);
  }

  public void actionPerformed(ActionEvent e)
  {
    if (e.getSource() == getJBnSelectOne()) {
      doSelectOne();
    }
    if (e.getSource() == getJBnSelectAll()) {
      doSelectAll();
    }
    if (e.getSource() == getJBnDeSelectOne()) {
      doDeSelectOne();
    }
    if (e.getSource() == getJBnDeSelectAll()) {
      doDeSelectAll();
    }
    if (e.getSource() == getJBnUp()) {
      doUp();
    }
    if (e.getSource() == getJBnDown()) {
      doDown();
    }
    if (e.getSource() == getJBnSet()) {
      doSetJc();
    }
    if (e.getSource() == getUIButton_sort())
      doBtnSort();
  }

  public void doDeSelectAll()
  {
    if (this.m_voItemRight.size() == 0) return;
    DefaultListModel dlm = (DefaultListModel)getJListLeft().getModel();
    for (int i = 0; i < this.m_voItemRight.size(); i++) {
      dlm.addElement(this.m_voItemRight.elementAt(i));
      this.m_voItemLeft.addElement(this.m_voItemRight.elementAt(i));
    }
    dlm = (DefaultListModel)getJListRight().getModel();
    dlm.removeAllElements();
    this.m_voItemRight.removeAllElements();
    getJTfStartJc().setText("");
    getJTfLastJc().setText("");
  }

  public void doDeSelectOne()
  {
    Object o = getJListRight().getSelectedValue();
    if (o == null) return;
    int index = getJListRight().getSelectedIndex();
    DefaultListModel dlm = (DefaultListModel)getJListRight().getModel();
    dlm.remove(index);
    this.m_voItemRight.remove(index);
    dlm = (DefaultListModel)getJListLeft().getModel();
    dlm.addElement(o);
    this.m_voItemLeft.addElement(o);
    getJTfStartJc().setText("");
    getJTfLastJc().setText("");
  }

  public void doDown()
  {
    int index = getJListRight().getSelectedIndex();
    if ((index < 0) || (index == this.m_voItemRight.size() - 1)) {
      return;
    }
    DefaultListModel dlm = (DefaultListModel)getJListRight().getModel();
    Object o = dlm.get(index);
    dlm.remove(index);
    dlm.insertElementAt(o, index + 1);
    this.m_voItemRight.remove(index);
    this.m_voItemRight.insertElementAt(o, index + 1);
    getJListRight().setSelectedIndices(new int[] { index + 1 });
  }

  public void doListChanged()
  {
    String[] ss = { EnumDepVO.ZCLB, EnumDepVO.SYZK, EnumDepVO.ZJFS };
    Object o = getJListRight().getSelectedValue();
    if (o == null) return;
    GatherItemVO g = (GatherItemVO)o;
    boolean flag = false;
    for (int i = 0; i < ss.length; i++) {
      if (EnumDepVO.getItemName(g.getItem_code()).equals(ss[i])) {
        int level1 = g.getLevel1().intValue();
        int level2 = g.getLevel2().intValue();
        if ((level1 == 0) || (level2 == 0)) {
          getJTfStartJc().setText("");
          getJTfLastJc().setText("");
          flag = true;
          break;
        }
        getJTfStartJc().setText(level1 + "");
        getJTfLastJc().setText(level2 + "");
        flag = true;
        break;
      }
    }
    if (!flag) {
      getJTfStartJc().setText("");
      getJTfLastJc().setText("");
      getJTfStartJc().setEnabled(false);
      getJTfLastJc().setEnabled(false);
      getJTfStartJc().setEditable(false);
      getJTfLastJc().setEditable(false);
      getJBnSet().setEnabled(false);
    } else {
      getJTfStartJc().setEnabled(isEditable());
      getJTfLastJc().setEnabled(isEditable());
      getJTfStartJc().setEditable(isEditable());
      getJTfLastJc().setEditable(isEditable());
      getJBnSet().setEnabled(isEditable());
    }
  }

  public void doSelectAll()
  {
    if (this.m_voItemLeft.size() == 0) return;
    DefaultListModel dlm = (DefaultListModel)getJListRight().getModel();
    for (int i = 0; i < this.m_voItemLeft.size(); i++) {
      dlm.addElement(this.m_voItemLeft.elementAt(i));
      this.m_voItemRight.addElement(this.m_voItemLeft.elementAt(i));
    }
    dlm = (DefaultListModel)getJListLeft().getModel();
    dlm.removeAllElements();
    this.m_voItemLeft.removeAllElements();
  }

  public void doSelectOne()
  {
    Object o = getJListLeft().getSelectedValue();
    if (o == null) return;
    int index = getJListLeft().getSelectedIndex();
    DefaultListModel dlm = (DefaultListModel)getJListLeft().getModel();
    dlm.remove(index);
    this.m_voItemLeft.remove(index);
    dlm = (DefaultListModel)getJListRight().getModel();
    dlm.addElement(o);
    this.m_voItemRight.addElement(o);
  }

  public void doSetJc()
  {
    int index = getJListRight().getSelectedIndex();
    if (index < 0) return;
    GatherItemVO g = (GatherItemVO)getJListRight().getSelectedValue();
    String s1 = getJTfStartJc().getText();
    String s2 = getJTfLastJc().getText();

    s1 = s2;
    if ((s1 == null) || (s1.trim().length() == 0)) s1 = "0";
    if ((s2 == null) || (s2.trim().length() == 0)) s2 = "0";
    int level1 = 0;
    int level2 = 0;
    try {
      level1 = Integer.parseInt(s1);
      level2 = Integer.parseInt(s2);
    } catch (NumberFormatException e) {
      System.out.println("级次的数据格式非法！");
      MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("201233", "UPP201233-000146"), NCLangRes.getInstance().getStrByID("201233", "UPP201233-000147"));
      level1 = 0;
      level2 = 0;
      getJTfLastJc().setText("");
      getJTfStartJc().setText("");
    }
    if (level2 < 0) {
      System.out.println("级次不能小于0！");
      MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("201233", "UPP201233-000146"), NCLangRes.getInstance().getStrByID("201233", "UPP201233-000148"));
      level1 = 0;
      level2 = 0;
      getJTfLastJc().setText("");
      getJTfStartJc().setText("");
    }

    if ((((GatherItemVO)this.m_voItemRight.elementAt(index)).getItem_code().equals(EnumDepVO.m_sItemCode[0])) && 
      (level2 > 0) && (this.m_iMaxCateCodeLevel > 0) && (level2 < this.m_iMaxCateCodeLevel)) {
      MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("201233", "UPP201233-000146"), NCLangRes.getInstance().getStrByID("201233", "UPP201233-000149"));
      level1 = 0;
      level2 = 0;
      getJTfLastJc().setText("");
      getJTfStartJc().setText("");
    }

    g.setLevel1(new Integer(level1));
    g.setLevel2(new Integer(level2));
    ((GatherItemVO)this.m_voItemRight.elementAt(index)).setLevel1(new Integer(level1));
    ((GatherItemVO)this.m_voItemRight.elementAt(index)).setLevel2(new Integer(level2));
  }

  public void doUp()
  {
    int index = getJListRight().getSelectedIndex();
    if (index <= 0) return;
    DefaultListModel dlm = (DefaultListModel)getJListRight().getModel();
    Object o = dlm.get(index);
    dlm.remove(index);
    dlm.insertElementAt(o, index - 1);
    this.m_voItemRight.remove(index);
    this.m_voItemRight.insertElementAt(o, index - 1);
    getJListRight().setSelectedIndices(new int[] { index - 1 });
  }

  private JButton getJBnDeSelectAll()
  {
    if (this.ivjJBnDeSelectAll == null) {
      try {
        this.ivjJBnDeSelectAll = new UIButton();
        this.ivjJBnDeSelectAll.setName("JBnDeSelectAll");
        this.ivjJBnDeSelectAll.setText("<<");
        this.ivjJBnDeSelectAll.setBounds(5, 116, 41, 27);
        this.ivjJBnDeSelectAll.setMargin(new Insets(0, 0, 0, 0));
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjJBnDeSelectAll;
  }

  private JButton getJBnDeSelectOne()
  {
    if (this.ivjJBnDeSelectOne == null) {
      try {
        this.ivjJBnDeSelectOne = new UIButton();
        this.ivjJBnDeSelectOne.setName("JBnDeSelectOne");
        this.ivjJBnDeSelectOne.setText("<");
        this.ivjJBnDeSelectOne.setBounds(5, 81, 41, 27);
        this.ivjJBnDeSelectOne.setMargin(new Insets(0, 0, 0, 0));
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjJBnDeSelectOne;
  }

  private JButton getJBnDown()
  {
    if (this.ivjJBnDown == null) {
      try {
        this.ivjJBnDown = new UIButton();
        this.ivjJBnDown.setName("JBnDown");
        this.ivjJBnDown.setFont(new Font("dialog", 0, 12));
        this.ivjJBnDown.setText(NCLangRes.getInstance().getStrByID("201233", "UPP201233-000150"));
        this.ivjJBnDown.setBounds(4, 101, 62, 21);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjJBnDown;
  }

  private JButton getJBnSelectAll()
  {
    if (this.ivjJBnSelectAll == null) {
      try {
        this.ivjJBnSelectAll = new UIButton();
        this.ivjJBnSelectAll.setName("JBnSelectAll");
        this.ivjJBnSelectAll.setText(">>");
        this.ivjJBnSelectAll.setBounds(5, 43, 41, 27);
        this.ivjJBnSelectAll.setMargin(new Insets(0, 0, 0, 0));
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjJBnSelectAll;
  }

  private JButton getJBnSelectOne()
  {
    if (this.ivjJBnSelectOne == null) {
      try {
        this.ivjJBnSelectOne = new UIButton();
        this.ivjJBnSelectOne.setName("JBnSelectOne");
        this.ivjJBnSelectOne.setText(">");
        this.ivjJBnSelectOne.setBounds(5, 6, 41, 27);
        this.ivjJBnSelectOne.setMargin(new Insets(0, 0, 0, 0));
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjJBnSelectOne;
  }

  private JButton getJBnSet()
  {
    if (this.ivjJBnSet == null) {
      try {
        this.ivjJBnSet = new JButton();
        this.ivjJBnSet.setName("JBnSet");
        this.ivjJBnSet.setFont(new Font("dialog", 0, 12));
        this.ivjJBnSet.setText(NCLangRes.getInstance().getStrByID("201233", "UPP201233-000151"));
        this.ivjJBnSet.setBounds(196, 4, 42, 24);
        this.ivjJBnSet.setMargin(new Insets(0, 0, 0, 0));
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjJBnSet;
  }

  private JButton getJBnUp()
  {
    if (this.ivjJBnUp == null) {
      try {
        this.ivjJBnUp = new UIButton();
        this.ivjJBnUp.setName("JBnUp");
        this.ivjJBnUp.setFont(new Font("dialog", 0, 12));
        this.ivjJBnUp.setText(NCLangRes.getInstance().getStrByID("201233", "UPP201233-000152"));
        this.ivjJBnUp.setBounds(4, 59, 62, 21);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjJBnUp;
  }

  private JLabel getJLabel1()
  {
    if (this.ivjJLabel1 == null) {
      try {
        this.ivjJLabel1 = new UILabel();
        this.ivjJLabel1.setName("JLabel1");
        this.ivjJLabel1.setFont(new Font("dialog", 0, 12));
        this.ivjJLabel1.setText(NCLangRes.getInstance().getStrByID("201233", "UPP201233-000153"));
        this.ivjJLabel1.setBounds(12, 10, 108, 16);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjJLabel1;
  }

  private JLabel getJLabel2()
  {
    if (this.ivjJLabel2 == null) {
      try {
        this.ivjJLabel2 = new UILabel();
        this.ivjJLabel2.setName("JLabel2");
        this.ivjJLabel2.setFont(new Font("dialog", 0, 12));
        this.ivjJLabel2.setText(NCLangRes.getInstance().getStrByID("201233", "UPP201233-000154"));
        this.ivjJLabel2.setBounds(203, 13, 104, 16);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjJLabel2;
  }

  private JLabel getJLabelJcfw()
  {
    if (this.ivjJLabelJcfw == null) {
      try {
        this.ivjJLabelJcfw = new UILabel();
        this.ivjJLabelJcfw.setName("JLabelJcfw");
        this.ivjJLabelJcfw.setFont(new Font("dialog", 0, 12));
        this.ivjJLabelJcfw.setText(NCLangRes.getInstance().getStrByID("201233", "UPP201233-000156"));
        this.ivjJLabelJcfw.setBounds(6, 6, 67, 20);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjJLabelJcfw;
  }

  private JLabel getJLabelTip()
  {
    if (this.ivjJLabelTip == null) {
      try {
        this.ivjJLabelTip = new UILabel();
        this.ivjJLabelTip.setName("JLabelTip");
        this.ivjJLabelTip.setText(NCLangRes.getInstance().getStrByID("201233", "UPP201233-000157"));
        this.ivjJLabelTip.setBounds(6, 30, 232, 19);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjJLabelTip;
  }

  private JLabel getJLabelTo()
  {
    if (this.ivjJLabelTo == null) {
      try {
        this.ivjJLabelTo = new UILabel();
        this.ivjJLabelTo.setName("JLabelTo");
        this.ivjJLabelTo.setText(">>");
        this.ivjJLabelTo.setBounds(117, 5, 28, 20);
        this.ivjJLabelTo.setVisible(false);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjJLabelTo;
  }

  private JList getJListLeft()
  {
    if (this.ivjJListLeft == null) {
      try {
        this.ivjJListLeft = new UIList();
        this.ivjJListLeft.setName("JListLeft");
        this.ivjJListLeft.setBounds(0, 0, 160, 132);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjJListLeft;
  }

  private JList getJListRight()
  {
    if (this.ivjJListRight == null) {
      try {
        this.ivjJListRight = new UIList();
        this.ivjJListRight.setName("JListRight");
        this.ivjJListRight.setBounds(0, 0, 160, 120);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjJListRight;
  }

  private JPanel getJPanel1()
  {
    if (this.ivjJPanel1 == null) {
      try {
        this.ivjJPanel1 = new UIPanel();
        this.ivjJPanel1.setName("JPanel1");
        this.ivjJPanel1.setLayout(null);
        this.ivjJPanel1.setBounds(135, 33, 52, 155);
        getJPanel1().add(getJBnSelectOne(), getJBnSelectOne().getName());
        getJPanel1().add(getJBnSelectAll(), getJBnSelectAll().getName());
        getJPanel1().add(getJBnDeSelectOne(), getJBnDeSelectOne().getName());
        getJPanel1().add(getJBnDeSelectAll(), getJBnDeSelectAll().getName());
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjJPanel1;
  }

  private JPanel getJPanel11()
  {
    if (this.ivjJPanel11 == null) {
      try {
        this.ivjJPanel11 = new UIPanel();
        this.ivjJPanel11.setName("JPanel11");
        this.ivjJPanel11.setLayout(null);
        this.ivjJPanel11.setBounds(330, 33, 83, 150);
        this.ivjJPanel11.add(getUIButton_sort(), getUIButton_sort().getName());
        this.ivjJPanel11.add(getJBnUp(), getJBnUp().getName());
        this.ivjJPanel11.add(getJBnDown(), getJBnDown().getName());
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjJPanel11;
  }

  private JPanel getJPanelJC()
  {
    if (this.ivjJPanelJC == null) {
      try {
        this.ivjJPanelJC = new UIPanel();
        this.ivjJPanelJC.setName("JPanelJC");
        this.ivjJPanelJC.setLayout(null);
        this.ivjJPanelJC.setBounds(136, 189, 241, 51);
        getJPanelJC().add(getJTfLastJc(), getJTfLastJc().getName());
        getJPanelJC().add(getJLabelTo(), getJLabelTo().getName());
        getJPanelJC().add(getJTfStartJc(), getJTfStartJc().getName());
        getJPanelJC().add(getJLabelJcfw(), getJLabelJcfw().getName());
        getJPanelJC().add(getJBnSet(), getJBnSet().getName());
        getJPanelJC().add(getJLabelTip(), getJLabelTip().getName());
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjJPanelJC;
  }

  private JScrollPane getJScrollPane1()
  {
    if (this.ivjJScrollPane1 == null) {
      try {
        this.ivjJScrollPane1 = new UIScrollPane();
        this.ivjJScrollPane1.setName("JScrollPane1");
        this.ivjJScrollPane1.setBounds(8, 32, 125, 209);
        getJScrollPane1().setViewportView(getJListLeft());
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjJScrollPane1;
  }

  private JScrollPane getJScrollPane2()
  {
    if (this.ivjJScrollPane2 == null) {
      try {
        this.ivjJScrollPane2 = new UIScrollPane();
        this.ivjJScrollPane2.setName("JScrollPane2");
        this.ivjJScrollPane2.setBounds(191, 33, 134, 151);
        getJScrollPane2().setViewportView(getJListRight());
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjJScrollPane2;
  }

  private JTextField getJTfLastJc()
  {
    if (this.ivjJTfLastJc == null) {
      try {
        this.ivjJTfLastJc = new UITextField();
        this.ivjJTfLastJc.setName("JTfLastJc");
        this.ivjJTfLastJc.setBounds(85, 6, 103, 20);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjJTfLastJc;
  }

  private JTextField getJTfStartJc()
  {
    if (this.ivjJTfStartJc == null) {
      try {
        this.ivjJTfStartJc = new UITextField();
        this.ivjJTfStartJc.setName("JTfStartJc");
        this.ivjJTfStartJc.setBounds(79, 5, 33, 20);
        this.ivjJTfStartJc.setVisible(false);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjJTfStartJc;
  }

  public String getMapItemCode(String s)
  {
    Object o = this.m_hCardItem.get(s);
    if (o == null) return null;
    return o.toString();
  }

  private PeriodManager getPeriodSrv()
  {
    if (this.m_cPeriodSrv == null) {
      this.m_cPeriodSrv = new PeriodManager(getAccBook());
    }
    return this.m_cPeriodSrv;
  }

  private void handleException(Throwable exception)
  {
    if (exception != null) exception.printStackTrace();
  }

  private void initConnection()
  {
    getJBnDeSelectAll().addActionListener(this);
    getJBnDeSelectOne().addActionListener(this);
    getJBnDown().addActionListener(this);
    getJBnSelectAll().addActionListener(this);
    getJBnSelectOne().addActionListener(this);
    getJBnUp().addActionListener(this);
    getUIButton_sort().addActionListener(this);
    getJBnSet().addActionListener(this);
    getJListRight().addListSelectionListener(this);
  }

  public void initData()
  {
    ClientEnvironment env = ClientEnvironment.getInstance();
    this.m_sPk_Corp = env.getCorporation().getPk_corp();
    this.m_sGroupPk = ShareBase.getGroupPkey();
    this.m_accyear = env.getAccountYear();
    this.m_period = env.getAccountMonth();

    initLeftData();
    initRightData();

    if (this.m_voGatherItemRight != null) {
      Vector vv = new Vector(0);
      String ss = "";
      for (int i = 0; i < this.m_voGatherItemRight.length; i++) {
        ss = this.m_voGatherItemRight[i].getItem_code();
        for (int j = 0; j < this.m_voItemLeft.size(); j++) {
          if (ss.equalsIgnoreCase(((GatherItemVO)this.m_voItemLeft.elementAt(j)).getItem_code())) {
            this.m_voItemLeft.removeElementAt(j);
            break;
          }
        }
      }
    }

    getJListLeft().removeAll();
    getJListRight().removeAll();
    if ((this.m_voItemLeft != null) && (this.m_voItemLeft.size() > 0)) {
      DefaultListModel dlm = new DefaultListModel();
      for (int i = 0; i < this.m_voItemLeft.size(); i++) {
        dlm.addElement(this.m_voItemLeft.elementAt(i));
      }
      getJListLeft().setModel(dlm);
    } else {
      DefaultListModel dlm = new DefaultListModel();
      getJListLeft().setModel(dlm);
      this.m_voItemLeft = new Vector(0);
    }
    if ((this.m_voItemRight != null) && (this.m_voItemRight.size() > 0)) {
      DefaultListModel dlm = new DefaultListModel();
      for (int i = 0; i < this.m_voItemRight.size(); i++) {
        dlm.addElement(this.m_voItemRight.elementAt(i));
      }
      getJListRight().setModel(dlm);
    } else {
      DefaultListModel dlm = new DefaultListModel();
      getJListRight().setModel(dlm);
      this.m_voItemRight = new Vector(0);
    }
    try {
      VoucherStyleVO[] selectedStyleVo = FAProxy.getRemoteOption().queryVoucherStyleAll(this.m_sPk_Corp);
      if ((selectedStyleVo != null) && (selectedStyleVo.length > 0)) {
        int iCatelevel = 0;
        for (int i = 0; i < selectedStyleVo.length; i++) {
          if (selectedStyleVo[i].getCate_level().intValue() <= iCatelevel)
            continue;
          iCatelevel = selectedStyleVo[i].getCate_level().intValue();
        }

        this.m_iMaxCateCodeLevel = iCatelevel;
      }
    }
    catch (Throwable e) {
      e.printStackTrace();
    }

    getJTfStartJc().setText("");
    getJTfLastJc().setText("");
    setEnabledAll(isEditable());
    getJTfStartJc().setEnabled(false);
    getJTfLastJc().setEnabled(false);
    getJBnSet().setEnabled(false);
  }

  private void initialize()
  {
    try
    {
      setName("GatherItemPane");
      setLayout(null);
      setSize(466, 259);
      add(getJLabel1(), getJLabel1().getName());
      add(getJScrollPane1(), getJScrollPane1().getName());
      add(getJPanel1(), getJPanel1().getName());
      add(getJScrollPane2(), getJScrollPane2().getName());
      add(getJPanel11(), getJPanel11().getName());
      add(getJLabel2(), getJLabel2().getName());
      add(getJPanelJC(), getJPanelJC().getName());
    } catch (Throwable ivjExc) {
      handleException(ivjExc);
    }

    initConnection();
    initData();
  }

  public void initLeftData()
  {
    Vector vv = new Vector(0);

    for (int i = 0; i < EnumDepVO.m_sItemCode.length; i++) {
      GatherItemVO g = new GatherItemVO();
      g.setPk_corp(this.m_sPk_Corp);
      g.setAccyear(this.m_accyear);
      g.setPeriod(this.m_period);
      g.setItem_code(EnumDepVO.m_sItemCode[i]);
      g.setItem_name(EnumDepVO.m_sItemName[i]);
      g.setSystem(new Integer(1));
      g.setLevel1(new Integer(0));
      g.setLevel2(new Integer(0));
      g.setdef1(null);
      g.setdef2(null);
      vv.addElement(g);
    }

    CarditemVO[] cardItem = null;
    try {
      cardItem = FAProxy.getRemoteCardItem().queryCarditemSelfDef(this.m_sPk_Corp, this.m_sGroupPk);
      setCardItemVO(cardItem);
    } catch (Throwable e) {
      e.printStackTrace();
    }
    if (cardItem != null) {
      for (int i = 0; i < cardItem.length; i++) {
        GatherItemVO g = new GatherItemVO();
        g.setPk_corp(this.m_sPk_Corp);

        Object oo = getMapItemCode(cardItem[i].getPk_carditem());
        if ((oo == null) || (oo.toString().trim().length() == 0)) {
          continue;
        }
        g.setAccyear(this.m_accyear);
        g.setPeriod(this.m_period);
        g.setItem_code(oo.toString());
        g.setItem_name(cardItem[i].getItem_name());
        g.setSystem(new Integer(0));
        g.setLevel1(new Integer(0));
        g.setLevel2(new Integer(0));
        g.setdef1(cardItem[i].getItem_code());
        g.setdef2(null);
        vv.addElement(g);
      }
    }
    this.m_voGatherItemLeft = new GatherItemVO[vv.size()];
    vv.copyInto(this.m_voGatherItemLeft);
    this.m_voItemLeft = new Vector(this.m_voGatherItemLeft.length);
    for (int i = 0; i < this.m_voGatherItemLeft.length; i++)
      this.m_voItemLeft.addElement(this.m_voGatherItemLeft[i].clone());
  }

  public void initRightData()
  {
    try
    {
      this.m_voGatherItemRight = null;
      this.m_voGatherItemRight = FAProxy.getRemoteOption().findGatherItemByPKcorp(this.m_sPk_Corp, this.m_accyear, this.m_period);
      this.m_voItemRight = new Vector(0);
      for (int i = 0; (this.m_voGatherItemRight != null) && (i < this.m_voGatherItemRight.length); i++) {
        String preStr = "";
        if (this.m_voGatherItemRight[i].getItem_name().startsWith("*")) {
          preStr = "*";
        }
        if (!this.m_voGatherItemRight[i].getItem_code().startsWith("def")) {
          this.m_voGatherItemRight[i].setItem_name(preStr + EnumDepVO.getItemName(this.m_voGatherItemRight[i].getItem_code()));
        }
        this.m_voItemRight.addElement(this.m_voGatherItemRight[i]);
      }
    } catch (Throwable e) {
      e.printStackTrace();
    }
  }

  private String getAccBook()
  {
    if (this.m_accbook != null) return this.m_accbook;
    try
    {
      this.m_accbook = AccBookAccessor.getDefaultGlBookVO(this.m_sPk_Corp).getPrimaryKey();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return this.m_accbook;
  }

  public boolean isEditable()
  {
    if (this.m_bIsEditable == null) {
      try {
        String sLogPeriod = this.m_accyear + this.m_period;

        String sPreYear = "";
        String sPreMonth = "";

        AccperiodVO voPeriod = getPeriodSrv().getStartPeriod_FA(this.m_sPk_Corp);

        String sStartPeriod = voPeriod.getAccyear() + voPeriod.getAccmonth();

        voPeriod = getPeriodSrv().getPrePeriod(this.m_accyear, this.m_period);
        if (voPeriod != null) {
          sPreYear = voPeriod.getAccyear();
          sPreMonth = voPeriod.getAccmonth();
        }

        boolean bClosebookFlag = new FaCloseBook().isMinUnCloseBookMonth(this.m_sPk_Corp, this.m_accyear, this.m_period, getAccBook());

        if ((bClosebookFlag) && ((sLogPeriod.equals(sStartPeriod)) || (getPeriodSrv().isNeedDepSum(sPreYear, sPreMonth))))
        {
          this.m_bIsEditable = new Boolean(true);
        }
        else this.m_bIsEditable = new Boolean(false); 
      }
      catch (Throwable e)
      {
        e.printStackTrace();
        this.m_bIsEditable = new Boolean(false);
      }
    }
    return this.m_bIsEditable.booleanValue();
  }

  public static void main(String[] args)
  {
    try
    {
      JFrame frame = new JFrame();

      GatherItemPane aGatherItemPane = new GatherItemPane();
      frame.setContentPane(aGatherItemPane);
      frame.setSize(aGatherItemPane.getSize());
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
      System.err.println("nc.ui.pub.beans.UIPanel 的 main() 中发生异常");
      exception.printStackTrace(System.out);
    }
  }

  public void newMethod()
  {
  }

  public void resetData()
  {
    initData();
  }

  public String saveData()
  {
    GatherItemVO[] gg = new GatherItemVO[this.m_voItemRight.size()];
    this.m_voItemRight.copyInto(gg);
    if (gg.length == 0) return null; try
    {
      if (isEditable())
      {
        gg[0].setPk_corp(this.m_sPk_Corp);
        gg[0].setAccyear(this.m_accyear);
        gg[0].setPeriod(this.m_period);
        FAProxy.getRemoteOption().insertOption(gg);
        getJTfStartJc().setEnabled(false);
        getJTfLastJc().setEnabled(false);
        getJTfStartJc().setEditable(false);
        getJTfLastJc().setEditable(false);
        getJBnSet().setEnabled(false);
        getJListRight().clearSelection();
      } else {
        MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("201233", "UPP201233-000146"), NCLangRes.getInstance().getStrByID("201233", "UPP201233-000159"));
        getJListRight().clearSelection();
        getJTfLastJc().setText("");
        getJTfStartJc().setText("");
        return NCLangRes.getInstance().getStrByID("201233", "UPP201233-000159");
      }
    } catch (Throwable e) {
      e.printStackTrace();
      return e.getMessage();
    }
    return "";
  }

  public void setCardItemVO(CarditemVO[] newM_cardItemVO)
  {
    this.m_hCardItem = new HashMap();
    if (newM_cardItemVO != null)
      for (int i = 0; i < newM_cardItemVO.length; i++) {
        String pk_defquote = newM_cardItemVO[i].getPk_defquote();
        if ((pk_defquote == null) || (pk_defquote.length() <= 0) || (newM_cardItemVO[i].getItem_code().length() != 8))
          continue;
        int iSequence = new Integer(pk_defquote.substring(17)).intValue();
        String strQuote = "def" + iSequence;
        this.m_hCardItem.put(newM_cardItemVO[i].getPk_carditem(), strQuote);
      }
  }

  public void setEnabledAll(boolean isEnable)
  {
    getJListLeft().setEnabled(isEnable);

    getJBnDeSelectAll().setEnabled(isEnable);
    getJBnDeSelectOne().setEnabled(isEnable);
    getJBnDown().setEnabled(isEnable);
    getJBnSelectAll().setEnabled(isEnable);
    getJBnSelectOne().setEnabled(isEnable);
    getJBnUp().setEnabled(isEnable);
    setEnableJC(isEnable);
  }

  public void setEnableJC(boolean enable)
  {
    getJLabelJcfw().setEnabled(enable);
    getJPanelJC().setEnabled(enable);
    getJTfLastJc().setEnabled(enable);

    getJBnSet().setEnabled(enable);
  }

  public void valueChanged(ListSelectionEvent e)
  {
    if (e.getSource() == getJListRight())
    {
      doListChanged();
    }
  }

  private JButton getUIButton_sort()
  {
    if (this.ivjJBnSort == null) {
      this.ivjJBnSort = new UIButton();
      this.ivjJBnSort.setName("JBnSort");
      this.ivjJBnSort.setFont(new Font("dialog", 0, 12));
      this.ivjJBnSort.setText(NCLangRes.getInstance().getStrByID("201233", "UPP201233-000155"));
      this.ivjJBnSort.setBounds(4, 17, 62, 21);
    }

    return this.ivjJBnSort;
  }
}