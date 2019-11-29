package nc.ui.ia.ia302;

import java.util.Vector;

import javax.swing.Spring;
import javax.swing.SpringLayout;

import nc.ui.ia.pub.CommonDataBO_Client;
import nc.ui.ia.pub.ExceptionUITools;
import nc.ui.ia.pub.IAEnvironment;
import nc.ui.ia.pub.RemoteCall;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.print.PrintDirectEntry;

import nc.vo.ia.ia305.P302;
import nc.vo.ia.pub.ExceptionUtils;
import nc.vo.ia.pub.Log;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pub.smart.ObjectUtils;
import nc.vo.sm.UserVO;

/**
 *
 */
public class PriceallocClientUI extends ToftPanel {

  /**
   * 
   */
  private static final long serialVersionUID = 7093365038150668151L;

  private double m_dMaxValue = new UFDouble(999999999999.99999999)
      .doubleValue();

  private String m_sMessage = "";

  private ButtonObject m_btnAlloc = new ButtonObject(
      nc.ui.ml.NCLangRes.getInstance().getStrByID("20146020",
          "UPT20146020-000001") /* @res "分配" */,
      nc.ui.ml.NCLangRes.getInstance().getStrByID("20146020",
          "UPT20146020-000001") /* @res "分配" */, 2, "分配"); /*-=notranslate=-*/

  private ButtonObject m_btnCancel = new ButtonObject(nc.ui.ml.NCLangRes
      .getInstance().getStrByID("common", "UC001-0000006") /* @res "查询" */,
      nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000006")
      /* @res "返回查询界面" */, 2, "查询"); /*-=notranslate=-*/

  // 按钮定义
  // private ButtonObject m_btnOk = new ButtonObject( nc.ui.ml.NCLangRes.
  // getInstance().getStrByID( "common", "UC001-0000044" ) /*@res "确定"*/,
  // nc.
  // ui.ml.NCLangRes.getInstance().getStrByID( "common",
  // "UC001-0000044" ) /*@res "确定"*/,
  // 2, "确定" ); /*-=notranslate=-*/
  private ButtonObject m_btnPrint = new ButtonObject(nc.ui.ml.NCLangRes
      .getInstance().getStrByID("common", "UC001-0000007") /* @res "打印" */,
      nc.ui.ml.NCLangRes.getInstance().getStrByID("20146020",
          "UPP20146020-000027")
      /* @res "打印输出" */, 2, "打印"); /*-=notranslate=-*/

  private int[] m_iaIncCl = null; // 存货分类级次

  private int[] m_iaPrec = null;

  private PriceAllocQueryPanel m_pnlcondition = null;

  private PnlInv m_pnlTblInv = null;

  private PnlInvCl m_pnlTblInvCl = null;

  private String[] m_sa1 = new String[10];

  // 数据相关选项
  private String[] m_saCond = null;

  private UserVO m_voUser = null;

  // 系统信息
  private IAEnvironment mce = null;

  private P302 mp = null;

  private Vector mvDisp = null; // 显示数据

  // private String[] m_saBill = null; //查询结果中的单据分录ID信息
  private Vector mvQry = null; // 查询结果数据

  private Vector mvUpdate = null; // 用户更新的数据

  // 按钮数组定义
  private ButtonObject[] m_aryButtonGroup = {
      this.m_btnCancel, this.m_btnAlloc, this.m_btnPrint
  };

  private String m_sCorp = null;

  private String m_sCorpName = null;

  private UFDate m_dLogindata = null;

  private String m_sAccountMonth = null;

  private String m_sAccountYear = null;

  private UFDate m_dMonthBeginDate = null;

  private UFDate m_dMonthEndDate = null;

  /**
   * PriceallocClientUI 构造子注解。
   */
  public PriceallocClientUI() {
    super();
    this.initialize();
  }

  /**
   * 子类实现该方法，返回业务界面的标题。
   * 
   * @version (00-6-6 13:33:25)
   * @return java.lang.String
   */
  public String getTitle() {
    return nc.ui.ml.NCLangRes.getInstance().getStrByID("20146020",
        "UPP20146020-000008") /* @res "产成品入库成本分配" */;
  }

  /**
   * 每当部件抛出异常时被调用
   * 
   * @param exception
   *          java.lang.Throwable
   */
  private void handleException(java.lang.Throwable exception) {
    Log.error(exception);
  }

  /**
   * 初始化类。
   */
  /* 警告：此方法将重新生成。 */
  private void initialize() {
    try {
      // user code begin {1}
      // user code end
      this.setName("PriceallocClientUI");

      SpringLayout layout = new SpringLayout();
      this.setLayout(layout);

      this.setSize(774, 419);

      // SpringLayout.Constraints constraint = new SpringLayout.Constraints();
      // constraint.setConstraint( SpringLayout.WEST, Spring.constant( 0 ) );
      // constraint.setConstraint( SpringLayout.NORTH, Spring.constant( 0 ) );
      // add( getPriceAllocPanel1(), constraint );

      // SpringLayout.Constraints constraint = new SpringLayout.Constraints();
      // constraint.setConstraint( SpringLayout.WEST, Spring.constant( 0 ) );
      // constraint.setConstraint( SpringLayout.NORTH, Spring.constant( 0 ) );
      // add( getPriceAllocQueryPanel(), constraint );

      SpringLayout.Constraints constraint = new SpringLayout.Constraints();
      constraint.setConstraint(SpringLayout.WEST, Spring.constant(0));
      constraint.setConstraint(SpringLayout.NORTH, Spring.constant(0));
      this.add(this.getTableInv(), constraint);

      constraint = new SpringLayout.Constraints();
      constraint.setConstraint(SpringLayout.WEST, Spring.constant(0));
      constraint.setConstraint(SpringLayout.NORTH, Spring.constant(0));
      this.add(this.getTableInvCl(), constraint);
    }
    catch (java.lang.Throwable ivjExc) {
      this.handleException(ivjExc);
    }
    // user code begin {2}
    this.m_btnCancel.setEnabled(true);
    this.m_btnAlloc.setEnabled(false);
    this.m_btnPrint.setEnabled(false);
    this.setButtons(this.m_aryButtonGroup);
    // getPriceAllocQueryPanel().setVisible(false);
    this.getTableInvCl().setVisible(false);
    // 获取系统信息
    this.mce = new IAEnvironment();
    this.m_sCorp = this.mce.getCorporationID();
    this.m_sCorpName = this.mce.getCorporationName();
    this.m_voUser = this.mce.getUser();
    this.m_dLogindata = this.mce.getBusinessDate();
    this.m_sAccountMonth = this.mce.getAccountMonth();
    this.m_sAccountYear = this.mce.getAccountYear();
    try {
      this.m_dMonthBeginDate = CommonDataBO_Client.getMonthBeginDate(this.m_sCorp,
          this.m_sAccountYear + "-" + this.m_sAccountMonth);
    }
    catch (Exception e) {
      e.printStackTrace();
      this.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20146020",
          "UPP20146020-000009") /* @res "获取会计月起始日错误" */);
      return;
    }
    try {
      this.m_dMonthEndDate = CommonDataBO_Client.getMonthEndDate(this.m_sCorp,
          this.m_sAccountYear + "-" + this.m_sAccountMonth);
    }
    catch (Exception e) {
      e.printStackTrace();
      this.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20146020",
          "UPP20146020-000010") /* @res "获取会计月中止日错误" */);
      return;
    }
    try {
      this.m_iaIncCl = CommonDataBO_Client.getCodeSchemdule("0001");
    }
    catch (Exception e) {
      e.printStackTrace();
      this.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20146020",
          "UPP20146020-000011") /* @res "获取存货分类错误" */);
      return;
    }
    try {
      this.m_iaPrec = this.mce.getDataPrecision(this.m_sCorp);
    }
    catch (Exception e) {
      e.printStackTrace();
      this.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20146020",
          "UPP20146020-000012") /* @res "获取数据精度错误" */);
      return;
    }
    this.getTableInv().setPrec(this.m_iaPrec);
    this.getTableInvCl().setPrec(this.m_iaPrec);
    this.setLayout(new java.awt.CardLayout());
    // user code end
  }

  /**
   * 此处插入方法说明。 创建日期：(01-4-25 14:41:34)
   */
  public void onAllocButtonClicked() {
    this.m_sMessage = "";
    // 是否按照存货分类进行成本分配
    if (this.m_saCond[12].equals("T")) {
      if (this.getTableInvCl().iserror()) {
        return;
      }
      this.mvUpdate = this.getTableInvCl().getdata();
    }
    else {
      if (this.getTableInv().iserror()) {
        return;
      }
      this.mvUpdate = this.getTableInv().getdata();
    }
    this.getTableInvCl().removeTotal(this.mvUpdate);

    // 检查数据是否已经录入正确
    this.mvUpdate = this.checkdata(this.mvUpdate);
    if ((this.mvUpdate != null) && (this.mvUpdate.size() > 0)) {
      try {
        // 对VO进行引用合并，减少网络传输量
        ObjectUtils.objectReference(this.mvUpdate);

        P302 p = new P302();
        p.setSysInfo(this.m_sa1);
        p.setCondition(this.m_saCond);
        p.setVec(this.mvUpdate);
        p.setUser(this.m_voUser.getPrimaryKey());
        p.setPrec(this.m_iaPrec);
        p.setIncCl(this.m_iaIncCl);
        p.setCount(this.mp.getCount());
        PriceAllocBO_Client.update(p);
      }
      catch (Exception ex) {
        ExceptionUtils.wrappException(ex);
      }
      // 对已有成本的产成品入库单是否重新分配
      if (this.m_saCond[10].equals("T")) {
        if (this.m_saCond[12].equals("T")) {
        }
        else {
        } // if
      }
      else {
        // 是否按照存货分类进行成本分配
        if (this.m_saCond[12].equals("T")) {
          this.getTableInvCl().setEdit(false);
        }
        else {
          this.getTableInv().setEdit(false);
        } // if
      } // if
      this.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20146020",
          "UPP20146020-000014") /* @res "分配成功" */);
    }
    else if (this.m_sMessage.length() != 0) {
      ExceptionUtils.wrappBusinessException(this.m_sMessage);

    }
    else {
      String message = nc.ui.ml.NCLangRes.getInstance().getStrByID("20146020",
          "UPP20146020-000015"); /* @res "请输入数据" */
      ExceptionUtils.wrappBusinessException(message);
    }
  }

  /**
   * 子类实现该方法，响应按钮事件。
   * 
   * @version (00-6-1 10:32:59)
   * @param bo
   *          ButtonObject
   */
  public void onButtonClicked(nc.ui.pub.ButtonObject bo) {
    RemoteCall call = new RemoteCall();
    try {
      if (bo == this.m_btnCancel) {
        this.onCancleButtonClicked();
      }
      else if (bo == this.m_btnAlloc) {
        call.execute(this, this, "onAllocButtonClicked");
      }
      else if (bo == this.m_btnPrint) {
        this.onPrintButtonClicked();
      }
    }
    catch (RuntimeException ex) {
      ExceptionUITools.showMessage(ex, this);
    }
  }

  /**
   * 此处插入方法说明。 创建日期：(01-4-25 9:54:53)
   */
  /*
   * private void onCancleButtonClicked() { getTableInv().setVisible( false );
   * getTableInvCl().setVisible( false ); getPriceAllocPanel1().setVisible( true );
   * //设置按钮变化 // m_btnOk.setEnabled( true ); m_btnCancel.setEnabled( false );
   * m_btnAlloc.setEnabled( false ); m_btnPrint.setEnabled( false ); setButtons(
   * m_aryButtonGroup ); showHintMessage( "" ); }
   */
  private void onCancleButtonClicked() {

    if (this.m_pnlcondition == null) {
      this.m_pnlcondition = new PriceAllocQueryPanel(this);

      this.m_pnlcondition.showModal();
    }
    else {
      this.m_pnlcondition.setVisible(true);
    }

    if (this.m_pnlcondition.isCloseOK()) {

      this.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20146020",
          "UPP20146020-000016") /* @res "请等待" */);
      // 从存货档案、单据中获得数据
      // m_saCond = getPriceAllocPanel1().getcondition();

      try {
        this.m_saCond = this.m_pnlcondition.getConditions();

      }
      catch (Exception ex) {
        ExceptionUtils.wrappException(ex);
      }

      this.m_sa1 = new String[10];
      this.m_sa1[0] = this.m_sCorp; // 登录单位
      this.m_sa1[1] = this.m_dLogindata.toString(); // 登录日期
      this.m_sa1[2] = this.m_sAccountYear; // 登录会计年
      this.m_sa1[3] = this.m_sAccountMonth; // 登录会计月
      this.m_sa1[4] = this.m_dMonthBeginDate.toString(); // 当前会计月开时日
      this.m_sa1[5] = this.m_dMonthEndDate.toString(); // 当前会计月截至日
      this.m_sa1[6] = this.m_voUser.getPrimaryKey();
      try {
        this.mp = new P302();
        this.mp.setSysInfo(this.m_sa1);
        this.mp.setCondition(this.m_saCond);
        this.mp.setPrec(this.m_iaPrec);
        this.mp.setIncCl(this.m_iaIncCl);
        this.mp = PriceAllocBO_Client.getdata(this.mp);
        this.mvQry = this.mp.getVec();
      }
      catch (Exception ex) {
        ExceptionUtils.wrappException(ex);
      }
      if ((this.mvQry != null) && (this.mvQry.size() != 0)) {
        // 设置按钮变化
        // m_btnOk.setEnabled( false );
        this.m_btnCancel.setEnabled(true);
        this.m_btnAlloc.setEnabled(true);
        this.m_btnPrint.setEnabled(true);
        this.setButtons(this.m_aryButtonGroup);

        this.mvDisp = null;
        this.mvDisp = this.mvQry;
        // 根据是否按照存货分类进行成本分配显示数据
        if (this.m_saCond[12].equals("T")) {
          this.getTableInvCl().display(this.mvDisp, this.m_saCond[12]);
          this.getTableInvCl().setVisible(true);
          this.getTableInv().setVisible(false);
          this.getTableInvCl().setEdit(true);
        }
        else {
          this.getTableInv().display(this.mvDisp, this.m_saCond[12]);
          this.getTableInv().setVisible(true);
          this.getTableInvCl().setVisible(false);
          this.getTableInv().setEdit(true);
        } // if

        String[] args = new String[1];
        args[0] = String.valueOf(this.mvDisp.size());
        String message = nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "20146020", "UPP20146020-000028", null, args);
        /* @res "本次查询共获得{0}行数据" */
        ;
        this.showHintMessage(message);
      }
      else { // 结果集为空，则清空界面 by 沈志鹏 2005-11-06
        this.getTableInv().display(null, this.m_saCond[12]);
        this.getTableInvCl().display(null, this.m_saCond[12]);
        this.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20146020",
            "UPP20146020-000017") /* @res "没有符合条件的数据" */);
      }
    }
  }

  /**
   * 此处插入方法说明。 创建日期：(01-4-25 9:48:15)
   */

  public void onOkButtonClicked() {
    this.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20146020",
        "UPP20146020-000016") /* @res "请等待" */);
    try {
      // 从存货档案、单据中获得数据
      // m_saCond = getPriceAllocPanel1().getcondition();
      this.m_saCond = this.m_pnlcondition.getConditions();
    }
    catch (Exception e1) {
      e1.printStackTrace();
    }
    this.m_sa1 = new String[10];
    this.m_sa1[0] = this.m_sCorp; // 登录单位
    this.m_sa1[1] = this.m_dLogindata.toString(); // 登录日期
    this.m_sa1[2] = this.m_sAccountYear; // 登录会计年
    this.m_sa1[3] = this.m_sAccountMonth; // 登录会计月
    this.m_sa1[4] = this.m_dMonthBeginDate.toString(); // 当前会计月开时日
    this.m_sa1[5] = this.m_dMonthEndDate.toString(); // 当前会计月截至日
    this.m_sa1[6] = this.m_voUser.getPrimaryKey();
    try {
      this.mp = new P302();
      this.mp.setSysInfo(this.m_sa1);
      this.mp.setCondition(this.m_saCond);
      this.mp.setPrec(this.m_iaPrec);
      this.mp.setIncCl(this.m_iaIncCl);
      this.mp = PriceAllocBO_Client.getdata(this.mp);
      this.mvQry = this.mp.getVec();
    }
    catch (BusinessException e) {
      e.printStackTrace();
      this.showHintMessage(e.getMessage());
      return;
    }
    catch (Exception e) {
      e.printStackTrace();
      this.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20146020",
          "UPP20146020-000013") /* @res "异常错误" */);
      return;
    }
    if ((this.mvQry != null) && (this.mvQry.size() != 0)) {
      // 设置按钮变化
      // m_btnOk.setEnabled( false );
      this.m_btnCancel.setEnabled(true);
      this.m_btnAlloc.setEnabled(true);
      this.m_btnPrint.setEnabled(true);
      this.setButtons(this.m_aryButtonGroup);

      this.mvDisp = null;
      this.mvDisp = this.mvQry;
      // 根据是否按照存货分类进行成本分配显示数据
      if (this.m_saCond[12].equals("T")) {
        this.getTableInvCl().display(this.mvDisp, this.m_saCond[12]);
        this.getTableInvCl().setVisible(true);
        this.getTableInvCl().setEdit(true);
      }
      else {
        this.getTableInv().display(this.mvDisp, this.m_saCond[12]);
        this.getTableInv().setVisible(true);
        this.getTableInv().setEdit(true);
      } // if
      // getPriceAllocPanel1().setVisible( false );

      String[] args = new String[1];
      args[0] = String.valueOf(this.mvDisp.size());
      String message = nc.ui.ml.NCLangRes.getInstance().getStrByID("20146020",
          "UPP20146020-000028", null, args);
      /* @res "本次查询共获得{0}行数据" */
      ;
      this.showHintMessage(message);
    }
    else {
      this.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20146020",
          "UPP20146020-000017") /* @res "没有符合条件的数据" */);
    }
  }

  /**
   * 此处插入方法说明。 创建日期：01-4-25 9:54:53)
   */
  private void onPrintButtonClicked() {
    String[][] sa2ColName = null;
    Vector v = null;
    Object[][] oa2p = null;
    int[] iaColWidth = null;
    int[] iaFlag = null; //
    if (this.m_saCond[12].equals("T")) {
      sa2ColName = new String[][] {
        {
            nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
                "UC000-0001449") /* @res "存货分类编码" */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
                "UC000-0001446") /* @res "存货分类名称" */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("2014",
                "UPP2014-000649") /* @res "主计量数量" */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
                "UC000-0001945") /* @res "总成本金额" */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
                "UC000-0001775")
        /* @res "平均单价" */}
      };
      v = this.getTableInvCl().getdata();

      oa2p = new Object[v.size()][5];
      int iCount = v.size();
      for (int i = 0; i < iCount; i++) {
        for (int j = 0; j < 5; j++) {
          oa2p[i][j] = ((Vector) v.elementAt(i)).elementAt(j);
        } // for
      }
      iaColWidth = new int[] {
          100, 100, 100, 100, 100
      };
      iaFlag = new int[] {
          0, 0, 2, 2, 2
      }; //
    }
    else {
      sa2ColName = new String[][] {
        {
            nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
                "UC000-0002930") /* @res "生产批号" */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
                "UC000-0001480") /* @res "存货编码" */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
                "UC000-0001453") /* @res "存货名称" */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
                "UC000-0003448") /* @res "规格" */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
                "UC000-0001240") /* @res "型号" */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("2014",
                "UPP2014-000649") /* @res "主计量数量" */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
                "UC000-0001945") /* @res "总成本金额" */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
                "UC000-0001775")
        /* @res "平均单价" */}
      };
      v = this.getTableInv().getdata();
      oa2p = new Object[v.size()][8];
      int iCount = v.size();
      for (int i = 0; i < iCount; i++) {
        for (int j = 0; j < 8; j++) {
          oa2p[i][j] = ((Vector) v.elementAt(i)).elementAt(j);
        } // for
      }
      iaColWidth = new int[] {
          100, 80, 80, 80, 80, 90, 90, 90
      };
      iaFlag = new int[] {
          0, 0, 0, 0, 0, 2, 2, 2
      }; //
    } // if
    String sTitle = nc.ui.ml.NCLangRes.getInstance().getStrByID("20146020",
        "UPP20146020-000008") /* @res "产成品入库成本分配" */;

    String[] args = new String[3];
    args[0] = this.m_sCorpName;
    args[1] = this.m_sAccountYear;
    args[2] = this.m_sAccountMonth;
    String sTopstr = nc.ui.ml.NCLangRes.getInstance().getStrByID("20146020",
        "UPP20146020-000029", null, args);
    /* @res "公司： {0} 会计期间： {1}-{2}" */
    ;

    args = new String[2];
    args[0] = this.m_voUser.getUserName();
    args[1] = this.m_dLogindata.toString();
    String sBotstr = nc.ui.ml.NCLangRes.getInstance().getStrByID("20146020",
        "UPP20146020-000030", null, args);
    PrintDirectEntry print = new PrintDirectEntry();
    print.setFixedRows(1);
    print.setTitle(sTitle);
    print.setTopStr(sTopstr);
    print.setBottomStr(sBotstr);
    print.setColWidth(iaColWidth);
    print.setAlignFlag(iaFlag);
    print.setColNames(sa2ColName);
    print.setData(oa2p);
    print.preview();
    if (this.m_saCond[12].equals("T")) { // 按照存货分类进行分配
    }
    else {
    } // if
  }

  /**
   * 函数功能: 参数: 返回值: 异常:
   * 
   * @return boolean
   * @param v
   *          java.util.Vector
   */
  private Vector checkdata(Vector v) {
    Vector data = new Vector();
    String sTmp = null;
    int iCount = v.size();
    int iPos;

    // 是否按照存货分类进行成本分配
    if (this.m_saCond[12].equals("T")) {
      iPos = 4;
    }
    else {
      iPos = 7;
    } // if
    for (int i = 0; i < iCount; i++) {
      boolean canAlloc = true;
      if (((Vector) v.elementAt(i)).elementAt(iPos) == null) {
        String[] args = new String[1];
        args[0] = String.valueOf(i + 1);
        // m_sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
        // "20146020", "UPP20146020-000024", null, args );
        // /*@res "第{0}行平均单价为空，请输入"*/
        ;
        canAlloc = false;
      }
      sTmp = ((Vector) v.elementAt(i)).elementAt(iPos).toString();
      String sTmpMny = ((Vector) v.elementAt(i)).elementAt(iPos - 1).toString();
      if (sTmp.length() == 0) {
        String[] args = new String[1];
        args[0] = String.valueOf(i + 1);
        // m_sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
        // "20146020", "UPP20146020-000024", null, args );
        /* @res "第{0}行平均单价为空，请输入" */
        canAlloc = false;

        // 为空不提示，继续
      }
      if (new nc.vo.pub.lang.UFDouble(sTmp).doubleValue() < 0) {
        String[] args = new String[1];
        args[0] = String.valueOf(i + 1);
        this.m_sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID("20146020",
            "UPP20146020-000025", null, args);
        /* @res "第{0}行平均单价小于零，请输入" */
        ;
        canAlloc = false;

        return null;
      }
      if (new nc.vo.pub.lang.UFDouble(sTmpMny).doubleValue() > this.m_dMaxValue) {

        String[] value = new String[] {
          String.valueOf(this.m_dMaxValue)
        };
        this.m_sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
            "UPP20143010-000164", null, value);
        canAlloc = false;

        return null;
      }
      if (canAlloc) {
        data.add(v.elementAt(i));
      }
    }
    return data;
  }

  /**
   * 返回 PrAllocTabPanel1 特性值。
   * 
   * @return nc.ui.ia.pricealloc.PrAllocTabPanel
   */
  /* 警告：此方法将重新生成。 */
  private PnlInv getTableInv() {
    if (this.m_pnlTblInv == null) {
      try {
        this.m_pnlTblInv = new PnlInv();
        this.m_pnlTblInv.setName("PnlInv");
      }
      catch (java.lang.Throwable ivjExc) {
        this.handleException(ivjExc);
      }
    }
    return this.m_pnlTblInv;
  }

  /**
   * 返回 PrAllocTabPanel1 特性值。
   * 
   * @return nc.ui.ia.pricealloc.PrAllocTabPanel
   */
  /* 警告：此方法将重新生成。 */
  private PnlInvCl getTableInvCl() {
    if (this.m_pnlTblInvCl == null) {
      try {
        this.m_pnlTblInvCl = new PnlInvCl();
        this.m_pnlTblInvCl.setName("m_pnlTblInvCl");
      }
      catch (java.lang.Throwable ivjExc) {
        this.handleException(ivjExc);
      }
    }
    return this.m_pnlTblInvCl;
  }
}
