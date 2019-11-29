package nc.ui.fa.fa20120301;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintStream;
import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;
import nc.ui.bd.ref.AbstractRefGridTreeModel;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.fa.base.CorpChooserDlg;
import nc.ui.fa.base.EnumNodeCode;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.vo.bd.b52.GlbookVO;
import nc.vo.fa.AccBookAccessor;
import nc.vo.fa.accperiod.AccperiodVO;
import nc.vo.fa.accperiod.PeriodManager;
import nc.vo.fa.fa20120301.EnumDepVO;
import nc.vo.pub.lang.UFDate;

public class MutiCorpDlg extends UIDialog
  implements ActionListener
{
  private JLabel ivjJLabel1 = null;
  private JPanel ivjUIDialogContentPane = null;
  private JButton ivjbnCancel = null;
  private JButton ivjbnCorpChooser = null;
  private JLabel ivjJLabel2 = null;
  private JList ivjJLCorpList = null;
  private JScrollPane ivjJScrollPane1 = null;
  private UIRefPane ivjRefPeriod = null;
  private String m_sCommand = "";
  private String[] m_sCorpKeys = null;
  private String m_sClosePeriod = "";
  private CorpChooserDlg corpDlg = null;
  private Object[] m_sCorpList = null;
  private JButton ivjbnZJJT = null;
  private String m_sYear = "";
  private String m_sMonth = "";
  private String m_sNodeCode = EnumNodeCode.NODECODE_DEP;

  private String m_accbook = null;

  public MutiCorpDlg()
  {
    super(null);
    initialize();
  }

  public void actionPerformed(ActionEvent e)
  {
    Object source = e.getSource();
    if (source == getbnCorpChooser()) {
      doCorpChoose();
      return;
    }
    if (source == getbnZJJT()) {
      doZJJT();
      return;
    }
    if (source == getbnCancel()) {
      doCancel();
      return;
    }
  }

  public void doCancel()
  {
    setCommand(NCLangRes.getInstance().getStrByID("common", "UC001-0000008"));
    closeCancel();
  }

  public void doCorpChoose()
  {
    if (getCorpDlg().showModal() == 1)
    {
      getJLCorpList().removeAll();
      Object[] dw = getCorpDlg().getResults();
      if (dw == null)
        this.m_sCorpList = new Object[0];
      else {
        this.m_sCorpList = dw;
      }
      if ((this.m_sCorpList != null) && (this.m_sCorpList.length > 0)) {
        AbstractListModel lm = new AbstractListModel() {
          public int getSize() { return MutiCorpDlg.this.m_sCorpList.length; } 
          public Object getElementAt(int i) { return MutiCorpDlg.this.m_sCorpList[i];
          }
        };
        getJLCorpList().setModel(lm);
      }
      this.m_sCorpKeys = getCorpDlg().getResultKeys();
      if (this.m_sCorpKeys == null) return;
      for (int i = 0; i < this.m_sCorpKeys.length; i++)
        System.out.println(this.m_sCorpKeys[i]);
    }
  }

  public void doZJJT()
  {
    setCommand(EnumDepVO.JTZJ);

    closeOK();
  }

  private JButton getbnCancel()
  {
    if (this.ivjbnCancel == null) {
      try {
        this.ivjbnCancel = new JButton();
        this.ivjbnCancel.setName("bnCancel");
        this.ivjbnCancel.setBorder(new EtchedBorder());
        this.ivjbnCancel.setText(NCLangRes.getInstance().getStrByID("common", "UC001-0000008"));
        this.ivjbnCancel.setBounds(303, 162, 70, 25);
        this.ivjbnCancel.setMargin(new Insets(0, 0, 0, 0));
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjbnCancel;
  }

  private JButton getbnCorpChooser()
  {
    if (this.ivjbnCorpChooser == null) {
      try {
        this.ivjbnCorpChooser = new JButton();
        this.ivjbnCorpChooser.setName("bnCorpChooser");
        this.ivjbnCorpChooser.setBorder(new EtchedBorder());
        this.ivjbnCorpChooser.setText(NCLangRes.getInstance().getStrByID("201233", "UPP201233-000081"));
        this.ivjbnCorpChooser.setBounds(88, 50, 78, 25);
        this.ivjbnCorpChooser.setMargin(new Insets(0, 0, 0, 0));
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjbnCorpChooser;
  }

  private JButton getbnZJJT()
  {
    if (this.ivjbnZJJT == null) {
      try {
        this.ivjbnZJJT = new JButton();
        this.ivjbnZJJT.setName("bnZJJT");
        this.ivjbnZJJT.setBorder(new EtchedBorder());
        this.ivjbnZJJT.setText(NCLangRes.getInstance().getStrByID("201233", "UPT201233-000007"));
        this.ivjbnZJJT.setBounds(303, 121, 70, 25);
        this.ivjbnZJJT.setMargin(new Insets(0, 0, 0, 0));
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjbnZJJT;
  }

  public String getClosePeriod()
  {
    return getRefPeriod().getRefName();
  }

  public String getCommand()
  {
    return this.m_sCommand;
  }

  public CorpChooserDlg getCorpDlg()
  {
    if (this.corpDlg == null) {
      this.corpDlg = new CorpChooserDlg(getParent(), this.m_sNodeCode);
    }
    return this.corpDlg;
  }

  public String[] getCorpKeys()
  {
    return this.m_sCorpKeys;
  }

  private JLabel getJLabel1()
  {
    if (this.ivjJLabel1 == null) {
      try {
        this.ivjJLabel1 = new JLabel();
        this.ivjJLabel1.setName("JLabel1");
        this.ivjJLabel1.setFont(new Font("dialog", 0, 12));
        this.ivjJLabel1.setText(NCLangRes.getInstance().getStrByID("201233", "UPP201233-000161"));
        this.ivjJLabel1.setBounds(15, 18, 68, 24);
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
        this.ivjJLabel2 = new JLabel();
        this.ivjJLabel2.setName("JLabel2");
        this.ivjJLabel2.setText(NCLangRes.getInstance().getStrByID("201233", "UPP201233-000081"));
        this.ivjJLabel2.setBounds(16, 53, 68, 20);
        this.ivjJLabel2.setForeground(new Color(102, 102, 153));
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjJLabel2;
  }

  private JList getJLCorpList()
  {
    if (this.ivjJLCorpList == null) {
      try {
        this.ivjJLCorpList = new JList();
        this.ivjJLCorpList.setName("JLCorpList");
        this.ivjJLCorpList.setBounds(0, 0, 160, 120);
        this.ivjJLCorpList.setSelectionMode(0);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjJLCorpList;
  }

  private JScrollPane getJScrollPane1()
  {
    if (this.ivjJScrollPane1 == null) {
      try {
        this.ivjJScrollPane1 = new JScrollPane();
        this.ivjJScrollPane1.setName("JScrollPane1");
        this.ivjJScrollPane1.setBounds(87, 90, 207, 134);
        getJScrollPane1().setViewportView(getJLCorpList());
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjJScrollPane1;
  }

  private UIRefPane getRefPeriod()
  {
    if (this.ivjRefPeriod == null) {
      try {
        this.ivjRefPeriod = new UIRefPane();
        this.ivjRefPeriod.setName("RefPeriod");
        this.ivjRefPeriod.setBounds(86, 18, 145, 22);
        this.ivjRefPeriod.setRefNodeName("会计期间");
        this.ivjRefPeriod.setRefInputType(0);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjRefPeriod;
  }

  private JPanel getUIDialogContentPane()
  {
    if (this.ivjUIDialogContentPane == null) {
      try {
        this.ivjUIDialogContentPane = new JPanel();
        this.ivjUIDialogContentPane.setName("UIDialogContentPane");
        this.ivjUIDialogContentPane.setLayout(null);
        getUIDialogContentPane().add(getJLabel1(), getJLabel1().getName());
        getUIDialogContentPane().add(getRefPeriod(), getRefPeriod().getName());
        getUIDialogContentPane().add(getJLabel2(), getJLabel2().getName());
        getUIDialogContentPane().add(getbnCorpChooser(), getbnCorpChooser().getName());
        getUIDialogContentPane().add(getbnZJJT(), getbnZJJT().getName());
        getUIDialogContentPane().add(getbnCancel(), getbnCancel().getName());
        getUIDialogContentPane().add(getJScrollPane1(), getJScrollPane1().getName());
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUIDialogContentPane;
  }

  private void handleException(Throwable exception)
  {
    if (exception != null) exception.printStackTrace();
  }

  public void initData()
  {
    ClientEnvironment env = ClientEnvironment.getInstance();
    try
    {
      if (this.m_accbook == null) return;

      String sdate = env.getBusinessDate().toString();

      AccperiodVO pvo = new PeriodManager(this.m_accbook).queryPerriodMonth(sdate);

      String sLoginYear = pvo.getAccyear();
      String sLoginMonth = pvo.getAccmonth();
      this.m_sClosePeriod = (sLoginYear + "-" + sLoginMonth);

      String sPeriodPK = PeriodManager.getPeriodPK(sLoginYear, sLoginMonth, this.m_accbook);

      String pk_accperiodscheme = AccBookAccessor.getGLBookVO(this.m_accbook).getPk_accperiodscheme();

      getRefPeriod().getRefModel().setWherePart("(isadj is null or isadj ='N') and bd_accperiod.pk_accperiodscheme = '" + pk_accperiodscheme + "'");

      ((AbstractRefGridTreeModel)getRefPeriod().getRefModel()).setClassWherePart(" bd_accperiod.pk_accperiodscheme = '" + pk_accperiodscheme + "'");

      getRefPeriod().getRefModel().reloadData();

      if ((sPeriodPK != null) || (sPeriodPK.trim().length() != 0))
        getRefPeriod().setPK(sPeriodPK);
      else
        getRefPeriod().setValue(this.m_sClosePeriod);
    }
    catch (Throwable e) {
      e.printStackTrace();
    }
  }

  private void initialize()
  {
    try
    {
      setName("MutiCorpDlg");
      setDefaultCloseOperation(2);
      setResizable(false);
      setSize(387, 258);
      setVisible(false);
      setTitle(NCLangRes.getInstance().getStrByID("201233", "UPP201233-000162"));
      setContentPane(getUIDialogContentPane());
    } catch (Throwable ivjExc) {
      handleException(ivjExc);
    }

    getbnCancel().addActionListener(this);
    getbnZJJT().addActionListener(this);
    getbnCorpChooser().addActionListener(this);

    initData();
  }

  public static void main(String[] args)
  {
    try
    {
      MutiCorpDlg aMutiCorpDlg = new MutiCorpDlg();
      aMutiCorpDlg.setModal(true);
      aMutiCorpDlg.addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent e) {
          System.exit(0);
        }

      });
    }
    catch (Throwable exception)
    {
      System.err.println("nc.ui.pub.beans.UIDialog 的 main() 中发生异常");
      exception.printStackTrace(System.out);
    }
  }

  public void setCommand(String newM_sCommand)
  {
    this.m_sCommand = newM_sCommand;
  }

  public void setAccBook(String fk_accbook)
  {
    if ((this.m_accbook == null) || (!fk_accbook.equals(this.m_accbook))) {
      this.m_accbook = fk_accbook;
      initData();
    }
    this.m_accbook = fk_accbook;
  }
}