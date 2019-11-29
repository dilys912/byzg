package nc.ui.fa.fa20120301;

import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintStream;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import nc.ui.fa.UserDefRefModelCategory;
import nc.ui.fa.UserDefRefModelDeptdoc;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITextField;
import nc.vo.bd.CorpVO;
import nc.vo.fa.ShareBase;
import nc.vo.fa.fa20120301.QueryVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;

public class QryPanel extends UIPanel
{
  private UILabel ivjUILabel1 = null;
  private UILabel ivjUILabel12 = null;
  private UILabel ivjUILabel1UseDept = null;
  private UILabel ivjUILabelManDept = null;
  private ButtonGroup m_bgRadioBtn;
  private UIRefPane ivjUIRefManDept = null;
  private UIRefPane ivjUIRefAssCategory = null;
  private UIRefPane ivjUIRefUseDept = null;

  private String m_sPk_Corp = null;
  private String m_sGroupPK = null;

  private UFDate m_ufDateLogin = null;
  private UIRefPane ivjUIRefBeginPreiod = null;
  private UIRefPane ivjUIRefEndPeriod = null;

  private QueryVO m_voNormalCnd = null;
  private UILabel ivjUILabel1UseDept1 = null;
  private UILabel ivjUILabel2 = null;
  private UITextField ivjTFCard_code = null;
  private JCheckBox ivjJCbxCancelFilter = null;
  private JCheckBox ivjJCbxNextlevel = null;
  private JCheckBox ivjJCbxZJOver = null;

  public QryPanel(String pk_corp)
  {
    this.m_sPk_Corp = pk_corp;
    initialize();
  }

  public QryPanel(LayoutManager p0)
  {
    super(p0);
  }

  public QryPanel(LayoutManager p0, boolean p1)
  {
    super(p0, p1);
  }

  public QryPanel(boolean p0)
  {
    super(p0);
  }

  private JCheckBox getJCbxCancelFilter()
  {
    if (this.ivjJCbxCancelFilter == null) {
      try {
        this.ivjJCbxCancelFilter = new JCheckBox();
        this.ivjJCbxCancelFilter.setName("JCbxCancelFilter");
        this.ivjJCbxCancelFilter.setText(NCLangRes.getInstance().getStrByID("201233", "UPP201233-000163"));
        this.ivjJCbxCancelFilter.setBounds(136, 171, 93, 24);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjJCbxCancelFilter;
  }

  private JCheckBox getJCbxNextlevel()
  {
    if (this.ivjJCbxNextlevel == null) {
      try {
        this.ivjJCbxNextlevel = new JCheckBox();
        this.ivjJCbxNextlevel.setName("JCbxNextlevel");
        this.ivjJCbxNextlevel.setText(NCLangRes.getInstance().getStrByID("201233", "UPP201233-000164"));
        this.ivjJCbxNextlevel.setBounds(22, 171, 101, 24);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjJCbxNextlevel;
  }

  private JCheckBox getJCbxZJOver()
  {
    if (this.ivjJCbxZJOver == null) {
      try {
        this.ivjJCbxZJOver = new JCheckBox();
        this.ivjJCbxZJOver.setName("JCbxZJOver");
        this.ivjJCbxZJOver.setText(NCLangRes.getInstance().getStrByID("201233", "UPP201233-000165"));
        this.ivjJCbxZJOver.setBounds(22, 204, 93, 24);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjJCbxZJOver;
  }

  public QueryVO getNormalConditionVO()
  {
    String pkUseDept = null;
    String sUseDeptName = null;
    String pkManDept = null;
    String sManDept = null;
    String pkAssCategory = "";
    String sCategoryName = null;
    String sCard_code = null;
    boolean isCancel_filter = false;
    boolean isNextlevel = false;
    boolean isZJOver = false;

    sCard_code = getTFCard_code().getText();

    pkUseDept = getUIRefUseDept().getRefPK();
    sUseDeptName = getUIRefUseDept().getRefName();

    pkManDept = getUIRefManDept().getRefPK();
    sManDept = getUIRefManDept().getRefName();

    String[] cates = getUIRefAssCategory().getRefPKs();
    if ((cates != null) && (cates.length > 0))
    {
      for (int i = 0; i < cates.length; i++) {
        pkAssCategory = pkAssCategory + cates[i] + ",";
      }

    }

    if (getJCbxNextlevel().isSelected())
      isNextlevel = true;
    else {
      isNextlevel = false;
    }

    if (getJCbxCancelFilter().isSelected())
      isCancel_filter = true;
    else {
      isCancel_filter = false;
    }

    if (getJCbxZJOver().isSelected())
      isZJOver = true;
    else {
      isZJOver = false;
    }

    this.m_voNormalCnd = new QueryVO();
    this.m_voNormalCnd.setPk_corp(this.m_sPk_Corp);
    this.m_voNormalCnd.setCard_code(sCard_code);
    this.m_voNormalCnd.setPk_Using_dept(pkUseDept);
    this.m_voNormalCnd.setPk_Man_dept(pkManDept);
    this.m_voNormalCnd.setPk_Category(pkAssCategory);
    this.m_voNormalCnd.setCancel_filter(new UFBoolean(isCancel_filter));
    this.m_voNormalCnd.setNextlevel(new Boolean(isNextlevel));
    this.m_voNormalCnd.setZJOver(new UFBoolean(isZJOver));

    return this.m_voNormalCnd;
  }

  private UITextField getTFCard_code()
  {
    if (this.ivjTFCard_code == null) {
      try {
        this.ivjTFCard_code = new UITextField();
        this.ivjTFCard_code.setName("TFCard_code");
        this.ivjTFCard_code.setBounds(104, 20, 124, 22);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjTFCard_code;
  }

  private UILabel getUILabel1()
  {
    if (this.ivjUILabel1 == null) {
      try {
        this.ivjUILabel1 = new UILabel();
        this.ivjUILabel1.setName("UILabel1");
        this.ivjUILabel1.setText("------");
        this.ivjUILabel1.setBounds(266, 201, 25, 25);
        this.ivjUILabel1.setVisible(false);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUILabel1;
  }

  private UILabel getUILabel12()
  {
    if (this.ivjUILabel12 == null) {
      try {
        this.ivjUILabel12 = new UILabel();
        this.ivjUILabel12.setName("UILabel12");
        this.ivjUILabel12.setText(NCLangRes.getInstance().getStrByID("201233", "UPP201233-000166"));
        this.ivjUILabel12.setBounds(22, 203, 83, 22);
        this.ivjUILabel12.setVisible(false);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUILabel12;
  }

  private UILabel getUILabel1UseDept()
  {
    if (this.ivjUILabel1UseDept == null) {
      try {
        this.ivjUILabel1UseDept = new UILabel();
        this.ivjUILabel1UseDept.setName("UILabel1UseDept");
        this.ivjUILabel1UseDept.setText(NCLangRes.getInstance().getStrByID("common", "UC000-0000270"));
        this.ivjUILabel1UseDept.setBounds(22, 94, 67, 22);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUILabel1UseDept;
  }

  private UILabel getUILabel1UseDept1()
  {
    if (this.ivjUILabel1UseDept1 == null) {
      try {
        this.ivjUILabel1UseDept1 = new UILabel();
        this.ivjUILabel1UseDept1.setName("UILabel1UseDept1");
        this.ivjUILabel1UseDept1.setText(NCLangRes.getInstance().getStrByID("common", "UC000-0003882"));
        this.ivjUILabel1UseDept1.setBounds(22, 130, 67, 22);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUILabel1UseDept1;
  }

  private UILabel getUILabel2()
  {
    if (this.ivjUILabel2 == null) {
      try {
        this.ivjUILabel2 = new UILabel();
        this.ivjUILabel2.setName("UILabel2");
        this.ivjUILabel2.setText(NCLangRes.getInstance().getStrByID("201233", "UPT201233-000001"));
        this.ivjUILabel2.setBounds(22, 21, 67, 22);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUILabel2;
  }

  private UILabel getUILabelManDept()
  {
    if (this.ivjUILabelManDept == null) {
      try {
        this.ivjUILabelManDept = new UILabel();
        this.ivjUILabelManDept.setName("UILabelManDept");
        this.ivjUILabelManDept.setText(NCLangRes.getInstance().getStrByID("common", "UC000-0003127"));
        this.ivjUILabelManDept.setBounds(21, 50, 67, 39);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUILabelManDept;
  }

  public UIRefPane getUIRefAssCategory()
  {
    if (this.ivjUIRefAssCategory == null) {
      try {
        this.ivjUIRefAssCategory = new UIRefPane();
        this.ivjUIRefAssCategory.setName("UIRefAssCategory");
        this.ivjUIRefAssCategory.setBounds(104, 129, 125, 22);
        this.ivjUIRefAssCategory.setRefType(1);

        this.ivjUIRefAssCategory.setRefModel(new UserDefRefModelCategory(this.m_sPk_Corp, false));

        this.ivjUIRefAssCategory.setMultiSelectedEnabled(true);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUIRefAssCategory;
  }

  public UIRefPane getUIRefBeginPreiod()
  {
    if (this.ivjUIRefBeginPreiod == null) {
      try {
        this.ivjUIRefBeginPreiod = new UIRefPane();
        this.ivjUIRefBeginPreiod.setName("UIRefBeginPreiod");
        this.ivjUIRefBeginPreiod.setBounds(128, 203, 125, 22);
        this.ivjUIRefBeginPreiod.setRefNodeName("会计期间");
        this.ivjUIRefBeginPreiod.setVisible(false);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUIRefBeginPreiod;
  }

  public UIRefPane getUIRefEndPeriod()
  {
    if (this.ivjUIRefEndPeriod == null) {
      try {
        this.ivjUIRefEndPeriod = new UIRefPane();
        this.ivjUIRefEndPeriod.setName("UIRefEndPeriod");
        this.ivjUIRefEndPeriod.setBounds(302, 203, 125, 22);
        this.ivjUIRefEndPeriod.setRefNodeName("会计期间");
        this.ivjUIRefEndPeriod.setVisible(false);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUIRefEndPeriod;
  }

  public UIRefPane getUIRefManDept()
  {
    if (this.ivjUIRefManDept == null) {
      try {
        this.ivjUIRefManDept = new UIRefPane();
        this.ivjUIRefManDept.setName("UIRefManDept");
        this.ivjUIRefManDept.setBounds(104, 57, 125, 22);

        this.ivjUIRefManDept.setRefModel(new UserDefRefModelDeptdoc(this.m_sPk_Corp, false));
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUIRefManDept;
  }

  public UIRefPane getUIRefUseDept()
  {
    if (this.ivjUIRefUseDept == null) {
      try {
        this.ivjUIRefUseDept = new UIRefPane();
        this.ivjUIRefUseDept.setName("UIRefUseDept");
        this.ivjUIRefUseDept.setBounds(104, 93, 125, 22);

        this.ivjUIRefUseDept.setRefModel(new UserDefRefModelDeptdoc(this.m_sPk_Corp, true));
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUIRefUseDept;
  }

  private void handleException(Throwable exception)
  {
    if (exception != null) exception.printStackTrace();
  }

  public void initData()
  {
    ClientEnvironment env = ClientEnvironment.getInstance();
    if (this.m_sPk_Corp == null) this.m_sPk_Corp = env.getCorporation().getPk_corp();
    this.m_ufDateLogin = env.getDate();
    this.m_sGroupPK = ShareBase.getGroupPkey();
  }

  private void initialize()
  {
    try
    {
      initData();

      setName("EvaluateNormalQryPanel");
      setLayout(null);
      setSize(324, 252);
      add(getUILabelManDept(), getUILabelManDept().getName());
      add(getUILabel1UseDept(), getUILabel1UseDept().getName());
      add(getUILabel12(), getUILabel12().getName());
      add(getUILabel1(), getUILabel1().getName());
      add(getUIRefManDept(), getUIRefManDept().getName());
      add(getUIRefBeginPreiod(), getUIRefBeginPreiod().getName());
      add(getUIRefEndPeriod(), getUIRefEndPeriod().getName());
      add(getUIRefUseDept(), getUIRefUseDept().getName());
      add(getUIRefAssCategory(), getUIRefAssCategory().getName());
      add(getUILabel2(), getUILabel2().getName());
      add(getTFCard_code(), getTFCard_code().getName());
      add(getUILabel1UseDept1(), getUILabel1UseDept1().getName());
      add(getJCbxCancelFilter(), getJCbxCancelFilter().getName());
      add(getJCbxNextlevel(), getJCbxNextlevel().getName());
      add(getJCbxZJOver(), getJCbxZJOver().getName());
    } catch (Throwable ivjExc) {
      handleException(ivjExc);
    }
  }

  public static void main(String[] args)
  {
    try
    {
      JFrame frame = new JFrame();

      QryPanel aQryPanel = new QryPanel("");
      frame.setContentPane(aQryPanel);
      frame.setSize(aQryPanel.getSize());
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
}