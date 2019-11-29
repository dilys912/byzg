package nc.ui.ia.ia402;

import java.util.HashMap;
import java.util.Vector;

import nc.ui.ia.pub.CommonDataBO_Client;
import nc.ui.ia.pub.ExceptionUITools;
import nc.ui.ia.pub.IAEnvironment;
import nc.ui.ia.pub.IATableModel;
import nc.ui.ia.pub.IATableSelectionEvent;
import nc.ui.ia.pub.IAUITable;
import nc.ui.ia.pub.IIATableSelectionListener;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialogEvent;
import nc.ui.pub.beans.UIDialogListener;

import nc.vo.ia.pub.ConstVO;
import nc.vo.ia.pub.Log;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;

/**
 * 功能描述：月末结帐 作者:崔勇 创建日期:(2001-5-17 13:10:07) 修改记录及日期: 修改人:
 */
public class AccountClientUI extends nc.ui.pub.ToftPanel implements Runnable,
    UIDialogListener, IIATableSelectionListener {
  /**
   * 
   */
  private static final long serialVersionUID = -4299144787271637991L;

  private nc.ui.pub.beans.UITablePane ivjUITablePane1 = null;

  private nc.ui.pub.beans.UILabel ivjUILabelCanAudit = null;

  private nc.ui.pub.beans.UILabel ivjUILabelCanReaudit = null;

  private nc.ui.pub.beans.UIPanel ivjUIPanel1 = null;

  //
  //
  private ButtonObject m_bButtonAudit = new ButtonObject(
      nc.ui.ml.NCLangRes.getInstance().getStrByID("20147020",
          "UPT20147020-000006")/* @res "月末结账" */,
      nc.ui.ml.NCLangRes.getInstance().getStrByID("20147020",
          "UPT20147020-000006")/* @res "月末结账" */, 2, "月末结账"); /*-=notranslate=-*/

  private ButtonObject m_bButtonReaudit = new ButtonObject(
      nc.ui.ml.NCLangRes.getInstance().getStrByID("20147020",
          "UPT20147020-000007")/* @res "取消结账" */,
      nc.ui.ml.NCLangRes.getInstance().getStrByID("20147020",
          "UPT20147020-000007")/* @res "取消结账" */, 2, "取消结账"); /*-=notranslate=-*/

  private ButtonObject m_bButtonCheck = new ButtonObject(
      nc.ui.ml.NCLangRes.getInstance().getStrByID("20147020",
          "UPP20147020-000051")/* @res "月结检查" */,
      nc.ui.ml.NCLangRes.getInstance().getStrByID("20147020",
          "UPP20147020-000051")/* @res "月结检查" */, 2, "月结检查"); /*-=notranslate=-*/
  
  
  // start: add by zip 2013/11/7: No 9
  private ButtonObject m_bButtonYLCheck = new ButtonObject("原料数据核对","原料数据核对", 2, "原料数据核对"); /*-=notranslate=-*/
  // end

  // start: edit by zip 2013/11/7: No 9
  private ButtonObject[] m_aryButtonGroup = {
      this.m_bButtonAudit, this.m_bButtonReaudit, this.m_bButtonCheck,this.m_bButtonYLCheck
  };
  // end

  //
  //
  private String m_sCorpID = "-1";

  private String m_sTitle = nc.ui.ml.NCLangRes.getInstance().getStrByID(
      "20147020", "UPT20147020-000006")/* @res "月末结账" */;

  private IAEnvironment ce = new IAEnvironment();

  private String m_sColumnName[] = {
      nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC000-0001821")/*
                                                                             * @res
                                                                             * "序号"
                                                                             */,
      nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC000-0000240")/*
                                                                             * @res
                                                                             * "会计期间"
                                                                             */,
      nc.ui.ml.NCLangRes.getInstance().getStrByID("20147020",
          "UPP20147020-000017")/* @res "结账状态" */,
      nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000308")
  /* @"关账" */
  };

  private int[] m_iPeci;

  // 按钮是否可用
  private boolean m_bAuditEnabled = false;

  private boolean m_bReauditEnabled = false;

  //
  //
  private IAUITable m_tUITable = null; // 表格

  private IATableModel m_mTableModel = null;

  //
  //
  private String m_sAccountPeriod = ""; // 要月末结账的期间

  private String m_sReaccountPeriod = ""; // 要取消月末结账的期间

  private String m_sStartPeriod = ""; // 启用会计期间

  private String[] m_sAllPeriods; // 所有会计期间

  //
  private String m_sDispMessage = ""; // 提示信息

  private boolean m_bProcessFlag = false; // 显示进度标志

//  private boolean m_bDispDlg = false; // 是否显示了对话框

  private AccountcheckDlg ivjAccountcheckDlg1 = null;

  //
  String m_sInitMessage = "";

  /**
   * AccountClientUI 构造子注解。
   */
  public AccountClientUI() {
    super();
    try {
      this.initialize();
    }
    catch (Exception ex) {
      ExceptionUITools.showMessage(ex, this);
    }
  }

  /**
   * 函数功能:月末结账 参数: String sPeriod ----- 要月末结账的会计期间 返回值:是否成功 异常:
   */
  private boolean account(String sPeriod) throws Exception {
    String sAccountYear = sPeriod.substring(0, 4);
    String sAccountMonth = sPeriod.substring(5, 7);

    // 开始月末结账

    // 准备参数
    /*
     * sSysInfo[0]:登录单位 sSysInfo[1]:登录会计年 sSysInfo[2]:登录会计月 sSysInfo[3]:要结账的会计年
     * sSysInfo[4]:要结账的会计月 sSysInfo[5]:登录用户 sSysInfo[6]:登录日期
     */
    String[] sSysInfo = new String[7];
    sSysInfo[0] = this.m_sCorpID;
    sSysInfo[1] = this.ce.getAccountYear();
    sSysInfo[2] = this.ce.getAccountMonth();
    sSysInfo[3] = sAccountYear;
    sSysInfo[4] = sAccountMonth;
    sSysInfo[5] = this.ce.getUser().getPrimaryKey();
    sSysInfo[6] = this.ce.getBusinessDate().toString();

    AccountBO_Client.account(sSysInfo, this.ce.getClientLink());
    HashMap hmClosedPeriod = this.queryClosedAccount();
    if (!hmClosedPeriod.containsKey(sPeriod)) {
      this.closeAccount(this.m_sCorpID, sAccountYear, sAccountMonth);
    }
    for (int i = 0; i < this.m_sAllPeriods.length; i++) {
      if (this.m_sAllPeriods[i].equals(sPeriod)) {
        this.m_mTableModel.setEditDisableRowColumn(i, 3);
        this.m_mTableModel.setValueAt(Boolean.TRUE, i, 3);
        break;
      }
    }
    return true;
  }

  /**
   * 对话框关闭事件监听者必须实现的接口方法
   * 
   * @param event
   *          UIDialogEvent 对话框关闭事件
   */
  public void dialogClosed(UIDialogEvent event) {
    // if (event.m_Operation == UIDialogEvent.WINDOW_CANCEL)
    // {
    // freePKLocks();

    // 恢复按钮状态
    this.m_bButtonAudit.setEnabled(this.m_bAuditEnabled);
    this.m_bButtonReaudit.setEnabled(this.m_bReauditEnabled);

    this.updateButtons();

    return;
    // }

    // 处理结账
    // handleAccount();

    // m_bDispDlg = false;
  }

  /**
   * 点关账时，进行开关账切换 创建日期：(2001-3-23 2:02:27)
   * 
   * @param e
   *          ufbill.BillEditEvent
   */
  public void afterEdit(nc.ui.ia.pub.IATableSelectionEvent e) {

    int row = e.getRow();
    String sPeriod = null;
    String year = null;
    String month = null;
    // 得到会计期间
    Vector data = (Vector) this.m_mTableModel.getDataVector();
    if (data != null) {
      Vector rowdata = (Vector) data.get(row);
      if (rowdata != null) {
        sPeriod = (String) rowdata.get(1);
      }
    }
    if (sPeriod != null) {
      int i = sPeriod.indexOf("-");
      year = sPeriod.substring(0, i);
      month = sPeriod.substring(i + 1);
    }

    boolean value = ((Boolean) e.getValue()).booleanValue();
    if (value) {
      // 执行关账操作
      this.closeAccount(this.m_sCorpID, year, month);
    }
    else {
      // 执行开账操作
      this.openAccount(this.m_sCorpID, year, month);

    }
  }

  /**
   * 执行关账操作 创建日期：(2005-8-31 16:52:28)
   * 
   * @param pk_corp
   *          java.lang.String
   * @param year
   *          java.lang.String
   * @param month
   *          java.lang.String
   */
  private void closeAccount(String pk_corp, String year, String month) {

    // 查询是否期初记帐
    UFBoolean bIsBeginAccout = null;
    try {
      bIsBeginAccout = CommonDataBO_Client.isBeginAccount(this.m_sCorpID);
    }
    catch (Exception e) {
    	Log.error(e);
      this.showErrorMessage(e.getMessage());
    }
    if (!bIsBeginAccout.booleanValue()) {
      this.showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "20143010", "UPP20143010-000313")/*@"本公司未期初记账，请先进行期初记账"*/);
      // 取消界面上已经被改变的状态
      for (int i = 0; i < this.m_sAllPeriods.length; i++) {
        if (this.m_sAllPeriods[i].equals(year + "-" + month)) {
          this.m_mTableModel.setValueAt(Boolean.FALSE, i, 3);
          break;
        }
      }
      return;
    }

    String id = "acctclosed" + pk_corp + year + month;
    String sql = "insert into ia_accountclosed "
        + " 	(caccountclosedid,pk_corp,caccountyear,caccountmonth) "
        + "values('" + id + "','" + pk_corp + "','" + year + "','" + month
        + "')";
    try {
      nc.ui.ia.pub.CommonDataBO_Client.execData(sql);
      this.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "20143010", "UPP20143010-000311",null,new String[]{year + "-" + month})
          /*@"会计期间: {0}  关账成功"*/);
    }
    catch (Exception ex) {
      ExceptionUITools.showMessage(ex, this);
      // 取消界面上已经被改变的状态
      for (int i = 0; i < this.m_sAllPeriods.length; i++) {
        if (this.m_sAllPeriods[i].equals(year + "-" + month)) {
          this.m_mTableModel.setValueAt(Boolean.FALSE, i, 3);
          break;
        }
      }
    }
  }

  /**
   * 执行开账操作 创建日期：(2005-8-31 18:23:25)
   * 
   * @param pk_corp
   *          java.lang.String
   * @param year
   *          java.lang.String
   * @param month
   *          java.lang.String
   */
  private void openAccount(String pk_corp, String year, String month) {

    StringBuffer sql = new StringBuffer(200);
    sql.append("delete from ia_accountclosed where pk_corp = '")
        .append(pk_corp).append("' and caccountyear = '").append(year).append(
            "' and caccountmonth = '").append(month).append("'");

    try {
      nc.ui.ia.pub.CommonDataBO_Client.execData(sql.toString());
      this.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "20143010", "UPP20143010-000312",null,new String[]{year + "-" + month})
          /*@"会计期间: {0}  取消关账成功"*/);
    }
    catch (Exception ex) {
      ExceptionUITools.showMessage(ex, this);
      // 取消界面上已经被改变的状态
      for (int i = 0; i < this.m_sAllPeriods.length; i++) {
        if (this.m_sAllPeriods[i].equals(year + "-" + month)) {
          this.m_mTableModel.setValueAt(Boolean.TRUE, i, 3);
          break;
        }
      }
    }
  }

  /**
   * 查询所有已关账期间 创建日期：(2005-8-31 19:59:48)
   * 
   * @return java.lang.HashMap
   */
  private HashMap queryClosedAccount() {
    HashMap<String,String> result = new HashMap<String,String>();
    String sql = "select caccountyear,caccountmonth from ia_accountclosed where dr = 0 and pk_corp = '"
        + this.m_sCorpID + "' order by caccountyear,caccountmonth";
    try {
      String[][] rst = CommonDataBO_Client.queryData(sql);
      if ((rst != null) && (rst.length != 0)) {
        for (int i = 0; i < rst.length; i++) {
          // 会计期间作为key放入hashmap中“2005-06”
          result.put(rst[i][0] + "-" + rst[i][1], "");
        }
      }
    }
    catch (Exception e) {
      ExceptionUITools.showMessage(e, this);
    }
    return result;
  }

  /**
   * 返回 AccountcheckDlg1 特性值。
   * 
   * @return nc.ui.ia.ia402.AccountcheckDlg
   */
  /* 警告：此方法将重新生成。 */
  private AccountcheckDlg getAccountcheckDlg1() {
    if (this.ivjAccountcheckDlg1 == null) {
      try {
        this.ivjAccountcheckDlg1 = new nc.ui.ia.ia402.AccountcheckDlg(this);
        // ivjAccountcheckDlg1 = new UnReachIADialog(this);
        this.ivjAccountcheckDlg1.setName("AccountcheckDlg1");
        this.ivjAccountcheckDlg1
            .setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        // user code begin {1}
        this.ivjAccountcheckDlg1.addUIDialogListener(this);

        // user code end
      }
      catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        this.handleException(ivjExc);
      }
    }
    return this.ivjAccountcheckDlg1;
  }

  /**
   * 函数功能:获得业务界面的标题 参数: 返回值:业务界面的标题 异常:
   */
  public String getTitle() {
    return this.m_sTitle;
  }

  /**
   * 返回 UILabelCanAudit 特性值。
   * 
   * @return nc.ui.pub.beans.UILabel
   */
  /* 警告：此方法将重新生成。 */
  private nc.ui.pub.beans.UILabel getUILabelCanAudit() {
    if (this.ivjUILabelCanAudit == null) {
      try {
        this.ivjUILabelCanAudit = new nc.ui.pub.beans.UILabel();
        this.ivjUILabelCanAudit.setName("UILabelCanAudit");
        this.ivjUILabelCanAudit.setText("UILabel");
        // user code begin {1}
        // user code end
      }
      catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        this.handleException(ivjExc);
      }
    }
    return this.ivjUILabelCanAudit;
  }

  /**
   * 返回 UILabelCanReaudit 特性值。
   * 
   * @return nc.ui.pub.beans.UILabel
   */
  /* 警告：此方法将重新生成。 */
  private nc.ui.pub.beans.UILabel getUILabelCanReaudit() {
    if (this.ivjUILabelCanReaudit == null) {
      try {
        this.ivjUILabelCanReaudit = new nc.ui.pub.beans.UILabel();
        this.ivjUILabelCanReaudit.setName("UILabelCanReaudit");
        this.ivjUILabelCanReaudit.setText("UILabel1");
        // user code begin {1}
        // user code end
      }
      catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        this.handleException(ivjExc);
      }
    }
    return this.ivjUILabelCanReaudit;
  }

  /**
   * 返回 UIPanel1 特性值。
   * 
   * @return nc.ui.pub.beans.UIPanel
   */
  /* 警告：此方法将重新生成。 */
  private nc.ui.pub.beans.UIPanel getUIPanel1() {
    if (this.ivjUIPanel1 == null) {
      try {
        this.ivjUIPanel1 = new nc.ui.pub.beans.UIPanel();
        this.ivjUIPanel1.setName("UIPanel1");
        this.ivjUIPanel1.setLayout(new java.awt.GridLayout());
        this.getUIPanel1().add(this.getUILabelCanAudit(), this.getUILabelCanAudit().getName());
        this.getUIPanel1().add(this.getUILabelCanReaudit(),
            this.getUILabelCanReaudit().getName());
        // user code begin {1}
        // user code end
      }
      catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        this.handleException(ivjExc);
      }
    }
    return this.ivjUIPanel1;
  }

  /**
   * 返回 UITablePane1 特性值。
   * 
   * @return nc.ui.pub.beans.UITablePane
   */
  /* 警告：此方法将重新生成。 */
  private nc.ui.pub.beans.UITablePane getUITablePane1() {
    if (this.ivjUITablePane1 == null) {
      try {
        this.ivjUITablePane1 = new nc.ui.pub.beans.UITablePane();
        this.ivjUITablePane1.setName("UITablePane1");
        // user code begin {1}
        // 将表格设置入控件
        this.m_tUITable = new IAUITable();
        this.ivjUITablePane1.setTable(this.m_tUITable);
        this.m_mTableModel = new IATableModel();
        this.m_tUITable.setModel(this.m_mTableModel);
        // user code end
      }
      catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        this.handleException(ivjExc);
      }
    }
    return this.ivjUITablePane1;
  }

  /**
   * 此处插入方法说明。 创建日期：(2003-8-22 13:09:29)
   */
  private void handleAccount() throws Exception {
    long t1 = System.currentTimeMillis();
    this.account(this.m_sAccountPeriod);
    // 设置月份状态
    for (int i = 0; i < this.m_mTableModel.getRowCount(); i++) {
      Object oTemp = this.m_mTableModel.getValueAt(i, 1);
      if (oTemp.equals(this.m_sAccountPeriod)) {
        this.m_mTableModel.setValueAt(nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "20147020", "UPP20147020-000018")/* @res "已结账" */, i, 2);
        break;
      }
    }

    // 获得月末结账的月份的下一个月
    String sTemp = CommonDataBO_Client.getNextPeriod(this.m_sCorpID,
        this.m_sAccountPeriod);

    // 要取消月末结账的期间即刚才月末结账的期间
    this.m_sReaccountPeriod = this.m_sAccountPeriod;

    // 要结账的期间即刚才月末结账的期间的下一个期间
    if (sTemp.substring(sTemp.length() - 2).equals(ConstVO.m_sLastMonth)) {
      sTemp = nc.ui.ml.NCLangRes.getInstance().getStrByID("20147020",
          "UPP20147020-000019")/* @res "无" */;
    }

    this.m_sAccountPeriod = sTemp;

    this.getUILabelCanAudit().setText(
        nc.ui.ml.NCLangRes.getInstance().getStrByID("20147020",
            "UPP20147020-000020")/* @res "可结账的会计期间：" */
            + this.m_sAccountPeriod);
    this.getUILabelCanReaudit().setText(
        nc.ui.ml.NCLangRes.getInstance().getStrByID("20147020",
            "UPP20147020-000021")/* @res "可取消结账的会计期间：" */
            + this.m_sReaccountPeriod);

    // 设置按钮状态
    if (this.m_sAccountPeriod.equals(nc.ui.ml.NCLangRes.getInstance().getStrByID(
        "20147020", "UPP20147020-000019")/* @res "无" */)) {
      this.m_bButtonAudit.setEnabled(false);
      this.m_bAuditEnabled = false;
    }
    else {
      this.m_bButtonAudit.setEnabled(true);
      this.m_bAuditEnabled = true;
    }

    if (this.m_sReaccountPeriod.equals(nc.ui.ml.NCLangRes.getInstance().getStrByID(
        "20147020", "UPP20147020-000019")/* @res "无" */)) {
      this.m_bButtonReaudit.setEnabled(false);
      this.m_bReauditEnabled = false;
    }
    else {
      this.m_bButtonReaudit.setEnabled(true);
      this.m_bReauditEnabled = true;
    }

    this.updateButtons();

    long t2 = System.currentTimeMillis() - t1;
    int iHour = (int) (t2 / 3600000);
    int iMinutes = (int) ((t2 - iHour * 3600000) / 60000);
    int iSecond = (int) ((t2 - iHour * 3600000 - iMinutes * 60000) / 1000);
    int iOther = (int) (t2 - iHour * 3600000 - iMinutes * 60000 - iSecond * 1000);
    String[] value = new String[] {
        String.valueOf(iHour), String.valueOf(iMinutes),
        String.valueOf(iSecond), String.valueOf(iOther)
    };
    String s = nc.ui.ml.NCLangRes.getInstance().getStrByID("20147020",
        "UPP20147020-000032", null, value)/* @res "共用时: {0}小时{1}分钟{2}秒{3}毫秒" */;

    this.m_sDispMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID("20147020",
        "UPP20147020-000022")/* @res "月末结账成功!" */
        + s;
    this.m_bProcessFlag = false;

    MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/* @res "提示" */,
        this.m_sDispMessage);

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
   * 函数功能:初始化数据 参数: 返回值: 异常:
   */
  private void init() {
    try {
      Vector<String> vTableHead = new Vector<String>();
      Vector<Vector> vTableData = new Vector<Vector>();

      for (int i = 0; i < this.m_sColumnName.length; i++){
        vTableHead.addElement(this.m_sColumnName[i]);
      }

      // 获得用户定义的所有会计期间
      String[] sPeriods = CommonDataBO_Client.getAllPeriods(this.m_sCorpID);
      this.m_sAllPeriods = sPeriods;

      // 获得最小的未结账月
      String sUnClosePeriod = CommonDataBO_Client.getUnClosePeriod(this.m_sCorpID);

      // 获得启用的会计期间
      this.m_sStartPeriod = CommonDataBO_Client.getStartPeriod(this.m_sCorpID);

      // 获得关账的会计期间
      HashMap hmClosedPeriods = this.queryClosedAccount();

      String sStatus = nc.ui.ml.NCLangRes.getInstance().getStrByID("20147020",
          "UPP20147020-000018")/* @res "已结账" */;
      Boolean bClose = Boolean.FALSE;
      for (int i = 0; i < sPeriods.length; i++) {
        if (sUnClosePeriod.equals(sPeriods[i]) || sUnClosePeriod.equals("00")) {
          // 账中没有数据
          sStatus = nc.ui.ml.NCLangRes.getInstance().getStrByID("20147020",
              "UPP20147020-000024")/* @res "未结账" */;
        }
        if (hmClosedPeriods.containsKey(sPeriods[i])) {
          bClose = Boolean.TRUE;
        }
        else {
          bClose = Boolean.FALSE;
        }
        Vector<Object> vTemp = new Vector<Object>();
        vTemp.addElement((i + 1) + "");
        vTemp.addElement(sPeriods[i]);
        vTemp.addElement(sStatus);
        vTemp.addElement(bClose);

        vTableData.addElement(vTemp);
      }

      this.m_mTableModel.setDataVector(vTableData, vTableHead);
      this.m_tUITable.addIATableSelectionListener(this);

      // 设置关账列是否可编辑
      this.m_mTableModel.setEditableRowColumn(-99, 3);// ////////////////
      for (int i = 0; i < sPeriods.length; i++) {// /////////////////
        if (sUnClosePeriod.equals(sPeriods[i]) || sUnClosePeriod.equals("00"))// //////////
        {// ////
          break;// /////
        }// /////
        this.m_mTableModel.setEditDisableRowColumn(i, 3);// /////////
      }// //////

      // 显示可结账的会计月份
      if (sUnClosePeriod.equals("00") == false) {
        this.m_sAccountPeriod = sUnClosePeriod;
      }
      else {
        this.m_sAccountPeriod = CommonDataBO_Client.getNextPeriod(this.m_sCorpID,
            sUnClosePeriod);
      }

      String sTemp = this.m_sAccountPeriod.substring(this.m_sAccountPeriod.length() - 2,
          this.m_sAccountPeriod.length());
      if (sTemp.equals(ConstVO.m_sLastMonth) || sTemp.equals("00")) {
        this.m_sAccountPeriod = nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "20147020", "UPP20147020-000019")/* @res "无" */;
      }

      this.getUILabelCanAudit().setText(
          nc.ui.ml.NCLangRes.getInstance().getStrByID("20147020",
              "UPP20147020-000020")/* @res "可结账的会计期间：" */
              +"  " +  this.m_sAccountPeriod);

      // 显示可取消结账的会计月份
      this.m_sReaccountPeriod = CommonDataBO_Client.getPerviousPeriod(this.m_sCorpID,
          sUnClosePeriod);
      if (this.m_sReaccountPeriod.compareTo(this.m_sStartPeriod) < 0) {
        // 可取消月末结账的月份是启用期间前
        this.m_sReaccountPeriod = nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "20147020", "UPP20147020-000019")/* @res "无" */;
      }
      else {
        sTemp = this.m_sReaccountPeriod.substring(this.m_sReaccountPeriod.length() - 2,
            this.m_sReaccountPeriod.length());
        if (sTemp.equals(ConstVO.m_sLastMonth) || sTemp.equals("00")) {
          this.m_sReaccountPeriod = nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "20147020", "UPP20147020-000019")/* @res "无" */;
        }
      }

      this.getUILabelCanReaudit().setText(
          nc.ui.ml.NCLangRes.getInstance().getStrByID("20147020",
              "UPP20147020-000021")/* @res "可取消结账的会计期间：" */
              + "  " + this.m_sReaccountPeriod);

      // 设置按钮状态
      if (this.m_sAccountPeriod.equals(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "20147020", "UPP20147020-000019")/* @res "无" */)) {
        this.m_bButtonAudit.setEnabled(false);
        this.m_bAuditEnabled = false;
      }
      else {
        this.m_bButtonAudit.setEnabled(true);
        this.m_bAuditEnabled = true;
      }

      if (this.m_sReaccountPeriod.equals(nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("20147020", "UPP20147020-000019")/* @res "无" */)) {
        this.m_bButtonReaudit.setEnabled(false);
        this.m_bReauditEnabled = false;
      }
      else {
        this.m_bButtonReaudit.setEnabled(true);
        this.m_bReauditEnabled = true;
      }

      this.setButtons(this.m_aryButtonGroup);
    }
    catch (Exception e) {
      ExceptionUITools.showMessage(e, this);
    }
  }

  /**
   * 初始化类。
   */
  /* 警告：此方法将重新生成。 */
  private void initialize() throws Exception {
    // user code begin {1}
    // 设置按钮状态
    this.m_bButtonAudit.setEnabled(true);
    this.m_bButtonReaudit.setEnabled(true);
    this.setButtons(this.m_aryButtonGroup);

    // 获得单位编码
    this.m_sCorpID = this.ce.getCorporationID();

    this.m_iPeci = this.ce.getDataPrecision(this.m_sCorpID);
    // user code end
    this.setName("AccountClientUI");
    this.setLayout(new java.awt.BorderLayout());
    this.setSize(774, 419);
    this.add(this.getUITablePane1(), "Center");
    this.add(this.getUIPanel1(), "North");
    this.init();
  }

  /**
   * 函数功能:点月末结账按钮触发 参数: 返回值: 异常:
   */
  private void onButtonAccountClicked() {

    int check = this.showYesNoMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
        "20147020", "UPP20147020-000054"));
    /* @res "月末结帐前请确认您已经做过月结检查，如果已经做过，点击“是”继续月末结帐；否则点击“否”取消本次月结操作" */

    if (check != 4) {
      return;
    }

    try {
      // 获得目前的状态
      this.m_bAuditEnabled = this.m_bButtonAudit.isEnabled();
      this.m_bReauditEnabled = this.m_bButtonReaudit.isEnabled();

      this.handleAccount();
    }
    catch (Exception ex) {
      this.m_sDispMessage = ex.getMessage();
      this.m_bProcessFlag = false;
      ExceptionUITools.showMessage(ex, this);
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
    if (bo == this.m_bButtonAudit) {
      this.onButtonAccountClicked();
    }
    else if (bo == this.m_bButtonReaudit) {
      // other button disposing
      this.onButtonReaccountClicked();
    }
    else if (bo == this.m_bButtonCheck) {
      this.onButtonCheckClicked();
    }
    // start: add by zip 2013/11/7: No 9
    else if(bo == this.m_bButtonYLCheck) {
    	this.onButtonYLCheckClicked();
    }
    // end
  }

  private void onButtonCheckClicked() {

    this.getAccountcheckDlg1().setData(this.m_sCorpID, this.m_sAccountPeriod.substring(0, 4),
        this.m_sAccountPeriod.substring(5, 7), this.m_iPeci);

    // 为保证打印界面按钮可用，作为非模式对话框
    this.getAccountcheckDlg1().setModal(false);
    this.getAccountcheckDlg1().setLocationRelativeTo(this);
    this.getAccountcheckDlg1().showModal();

  }
  
  // start: add by zip 2013/11/7 No 9
  private void onButtonYLCheckClicked() {
	  YLCheckDlg dlg = new YLCheckDlg(this);
	  dlg.setTitle("原料数据核对");
	  dlg.showModal();
  }
  // end

  /**
   * 函数功能:点取消按钮触发 参数: 返回值: 异常:
   */
  private void onButtonReaccountClicked() {
    try {
      String str = CommonDataBO_Client.checkUnload(this.ce.getCorporationID(),
          this.m_sReaccountPeriod);

      if (!str.equalsIgnoreCase("load")) {
        this.showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "20143010", "UPP20143010-000314")/*@"当前会计期间的数据已经被卸出，不能取消结帐"*/);
        return;
      }

    }
    catch (BusinessException e) {
      Log.error(e);
    }

    try {

      long t1 = System.currentTimeMillis();

      // 启动线程
      String[] value = new String[] {
        this.m_sReaccountPeriod
      };
      this.m_sDispMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID("20147020",
          "UPP20147020-000033", null, value)/* @res "正在对{0}期间进行取消月末结帐，请稍候" */;
      this.m_bProcessFlag = true;
      Thread td = new Thread(this);
      td.start();
      this.reaccount(this.m_sReaccountPeriod);
      // 设置月份状态
      for (int i = 0; i < this.m_mTableModel.getRowCount(); i++) {
        Object oTemp = this.m_mTableModel.getValueAt(i, 1);
        if (oTemp.equals(this.m_sReaccountPeriod)) {
          this.m_mTableModel.setValueAt(nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "20147020", "UPP20147020-000024")/* @res "未结账" */, i, 2);
          break;
        }
      }

      // 获得取消月末结账的月份的前一个月
      String sTemp = CommonDataBO_Client.getPerviousPeriod(this.m_sCorpID,
          this.m_sReaccountPeriod);

      // 要结账的期间即刚才取消月末结账的期间
      this.m_sAccountPeriod = this.m_sReaccountPeriod;

      // 要取消月末结账的期间即前一个期间
      if (this.m_sReaccountPeriod.compareTo(this.m_sStartPeriod) <= 0) {
        // 可取消月末结账的月份是启用期间前
        sTemp = nc.ui.ml.NCLangRes.getInstance().getStrByID("20147020",
            "UPP20147020-000019")/* @res "无" */;
      }

      this.m_sReaccountPeriod = sTemp;
      this.getUILabelCanAudit().setText(
          nc.ui.ml.NCLangRes.getInstance().getStrByID("20147020",
              "UPP20147020-000020")/* @res "可结账的会计期间：" */
              + this.m_sAccountPeriod);
      this.getUILabelCanReaudit().setText(
          nc.ui.ml.NCLangRes.getInstance().getStrByID("20147020",
              "UPP20147020-000021")/* @res "可取消结账的会计期间：" */
              + this.m_sReaccountPeriod);

      long t2 = System.currentTimeMillis() - t1;
      int iHour = (int) (t2 / 3600000);
      int iMinutes = (int) ((t2 - iHour * 3600000) / 60000);
      int iSecond = (int) ((t2 - iHour * 3600000 - iMinutes * 60000) / 1000);
      int iOther = (int) (t2 - iHour * 3600000 - iMinutes * 60000 - iSecond * 1000);

      value = new String[] {
          String.valueOf(iHour), String.valueOf(iMinutes),
          String.valueOf(iSecond), String.valueOf(iOther)
      };
      String s = nc.ui.ml.NCLangRes.getInstance().getStrByID("20147020",
          "UPP20147020-000032", null, value)/* @res "共用时: {0}小时{1}分钟{2}秒{3}毫秒" */;

      this.m_sDispMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID("20147020",
          "UPP20147020-000028")/* @res "取消月末结账成功!" */
          + s;
      this.m_bProcessFlag = false;

      MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/* @res "提示" */,
          this.m_sDispMessage);

      // 设置按钮状态
      if (this.m_sAccountPeriod.equals(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "20147020", "UPP20147020-000019")/* @res "无" */)) {
        this.m_bButtonAudit.setEnabled(false);
        this.m_bAuditEnabled = false;
      }
      else {
        this.m_bButtonAudit.setEnabled(true);
        this.m_bAuditEnabled = true;
      }

      if (this.m_sReaccountPeriod.equals(nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("20147020", "UPP20147020-000019")/* @res "无" */)) {
        this.m_bButtonReaudit.setEnabled(false);
        this.m_bReauditEnabled = false;
      }
      else {
        this.m_bButtonReaudit.setEnabled(true);
        this.m_bReauditEnabled = true;
      }

      this.setButtons(this.m_aryButtonGroup);

    }
    catch (Exception ex) {
      this.m_bProcessFlag = false;
      ExceptionUITools.showMessage(ex, this);
    }
  }

  /**
   * 本方法在界面基类完成初始化工作后调用。程序员可在子类的 该方法中完成界面初始化工作（设置状态条信息等）。 创建日期：(2001-11-20
   * 17:03:18)
   */
  public void postInit() {
    if (this.m_sInitMessage.length() != 0) {
      MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/* @res "提示" */,
          this.m_sInitMessage);
    }
  }

  /**
   * 函数功能:取消月末结账 参数: String sPeriod ----- 要取消月末结账的会计期间 返回值:是否成功 异常:
   */
  private void reaccount(String sPeriod) throws Exception {
    String sAccountYear = sPeriod.substring(0, 4);
    String sAccountMonth = sPeriod.substring(5, 7);

    String[] sSysInfo = new String[7];
    /*
     * sSysInfo[0]:登录单位 sSysInfo[1]:登录会计年 sSysInfo[2]:登录会计月 sSysInfo[3]:要结账的会计年
     * sSysInfo[4]:要结账的会计月 sSysInfo[5]:登录用户 sSysInfo[6]:登录日期
     */
    sSysInfo[0] = this.m_sCorpID;
    sSysInfo[1] = this.ce.getAccountYear();
    sSysInfo[2] = this.ce.getAccountMonth();
    sSysInfo[3] = sAccountYear;
    sSysInfo[4] = sAccountMonth;
    sSysInfo[5] = this.ce.getUser().getPrimaryKey();
    sSysInfo[6] = this.ce.getBusinessDate().toString();

    // 开始取消月末结账
    AccountBO_Client.reAccount(sSysInfo, this.ce.getClientLink());
    for (int i = 0; i < this.m_sAllPeriods.length; i++) {
      if (this.m_sAllPeriods[i].equals(sPeriod)) {
        this.m_mTableModel.setEditableRowColumn(i, 3);
        break;
      }
    }
  }

  /**
   * 函数功能: 参数: 返回值: 异常:
   */
  public void run() {
    int nCount = 1, nCount1 = 0;
    String sMessage = this.m_sDispMessage;
    while (this.m_bProcessFlag) {
      // 休眠
      try {
        Thread.sleep(100);
      }
      catch (InterruptedException e) {
        e.printStackTrace();
      }

      // 执行显示信息
      nCount1 = nCount1 + 1;
      if (nCount1 == 5) {
        nCount1 = 0;
        sMessage = sMessage + ".";
        this.showHintMessage(sMessage);
        if (nCount > 10) {
          sMessage = this.m_sDispMessage;
        }
        else {
          nCount = nCount + 1;
        }
      }
    }
    this.showHintMessage("");
  }

  public void IATableSelectionChanged(IATableSelectionEvent e) {
    // TODO 自动生成方法存根

  }

}