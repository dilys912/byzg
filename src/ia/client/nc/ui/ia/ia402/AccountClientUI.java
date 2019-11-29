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
 * ������������ĩ���� ����:���� ��������:(2001-5-17 13:10:07) �޸ļ�¼������: �޸���:
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
          "UPT20147020-000006")/* @res "��ĩ����" */,
      nc.ui.ml.NCLangRes.getInstance().getStrByID("20147020",
          "UPT20147020-000006")/* @res "��ĩ����" */, 2, "��ĩ����"); /*-=notranslate=-*/

  private ButtonObject m_bButtonReaudit = new ButtonObject(
      nc.ui.ml.NCLangRes.getInstance().getStrByID("20147020",
          "UPT20147020-000007")/* @res "ȡ������" */,
      nc.ui.ml.NCLangRes.getInstance().getStrByID("20147020",
          "UPT20147020-000007")/* @res "ȡ������" */, 2, "ȡ������"); /*-=notranslate=-*/

  private ButtonObject m_bButtonCheck = new ButtonObject(
      nc.ui.ml.NCLangRes.getInstance().getStrByID("20147020",
          "UPP20147020-000051")/* @res "�½���" */,
      nc.ui.ml.NCLangRes.getInstance().getStrByID("20147020",
          "UPP20147020-000051")/* @res "�½���" */, 2, "�½���"); /*-=notranslate=-*/
  
  
  // start: add by zip 2013/11/7: No 9
  private ButtonObject m_bButtonYLCheck = new ButtonObject("ԭ�����ݺ˶�","ԭ�����ݺ˶�", 2, "ԭ�����ݺ˶�"); /*-=notranslate=-*/
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
      "20147020", "UPT20147020-000006")/* @res "��ĩ����" */;

  private IAEnvironment ce = new IAEnvironment();

  private String m_sColumnName[] = {
      nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC000-0001821")/*
                                                                             * @res
                                                                             * "���"
                                                                             */,
      nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC000-0000240")/*
                                                                             * @res
                                                                             * "����ڼ�"
                                                                             */,
      nc.ui.ml.NCLangRes.getInstance().getStrByID("20147020",
          "UPP20147020-000017")/* @res "����״̬" */,
      nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000308")
  /* @"����" */
  };

  private int[] m_iPeci;

  // ��ť�Ƿ����
  private boolean m_bAuditEnabled = false;

  private boolean m_bReauditEnabled = false;

  //
  //
  private IAUITable m_tUITable = null; // ���

  private IATableModel m_mTableModel = null;

  //
  //
  private String m_sAccountPeriod = ""; // Ҫ��ĩ���˵��ڼ�

  private String m_sReaccountPeriod = ""; // Ҫȡ����ĩ���˵��ڼ�

  private String m_sStartPeriod = ""; // ���û���ڼ�

  private String[] m_sAllPeriods; // ���л���ڼ�

  //
  private String m_sDispMessage = ""; // ��ʾ��Ϣ

  private boolean m_bProcessFlag = false; // ��ʾ���ȱ�־

//  private boolean m_bDispDlg = false; // �Ƿ���ʾ�˶Ի���

  private AccountcheckDlg ivjAccountcheckDlg1 = null;

  //
  String m_sInitMessage = "";

  /**
   * AccountClientUI ������ע�⡣
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
   * ��������:��ĩ���� ����: String sPeriod ----- Ҫ��ĩ���˵Ļ���ڼ� ����ֵ:�Ƿ�ɹ� �쳣:
   */
  private boolean account(String sPeriod) throws Exception {
    String sAccountYear = sPeriod.substring(0, 4);
    String sAccountMonth = sPeriod.substring(5, 7);

    // ��ʼ��ĩ����

    // ׼������
    /*
     * sSysInfo[0]:��¼��λ sSysInfo[1]:��¼����� sSysInfo[2]:��¼����� sSysInfo[3]:Ҫ���˵Ļ����
     * sSysInfo[4]:Ҫ���˵Ļ���� sSysInfo[5]:��¼�û� sSysInfo[6]:��¼����
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
   * �Ի���ر��¼������߱���ʵ�ֵĽӿڷ���
   * 
   * @param event
   *          UIDialogEvent �Ի���ر��¼�
   */
  public void dialogClosed(UIDialogEvent event) {
    // if (event.m_Operation == UIDialogEvent.WINDOW_CANCEL)
    // {
    // freePKLocks();

    // �ָ���ť״̬
    this.m_bButtonAudit.setEnabled(this.m_bAuditEnabled);
    this.m_bButtonReaudit.setEnabled(this.m_bReauditEnabled);

    this.updateButtons();

    return;
    // }

    // �������
    // handleAccount();

    // m_bDispDlg = false;
  }

  /**
   * �����ʱ�����п������л� �������ڣ�(2001-3-23 2:02:27)
   * 
   * @param e
   *          ufbill.BillEditEvent
   */
  public void afterEdit(nc.ui.ia.pub.IATableSelectionEvent e) {

    int row = e.getRow();
    String sPeriod = null;
    String year = null;
    String month = null;
    // �õ�����ڼ�
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
      // ִ�й��˲���
      this.closeAccount(this.m_sCorpID, year, month);
    }
    else {
      // ִ�п��˲���
      this.openAccount(this.m_sCorpID, year, month);

    }
  }

  /**
   * ִ�й��˲��� �������ڣ�(2005-8-31 16:52:28)
   * 
   * @param pk_corp
   *          java.lang.String
   * @param year
   *          java.lang.String
   * @param month
   *          java.lang.String
   */
  private void closeAccount(String pk_corp, String year, String month) {

    // ��ѯ�Ƿ��ڳ�����
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
          "20143010", "UPP20143010-000313")/*@"����˾δ�ڳ����ˣ����Ƚ����ڳ�����"*/);
      // ȡ���������Ѿ����ı��״̬
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
          /*@"����ڼ�: {0}  ���˳ɹ�"*/);
    }
    catch (Exception ex) {
      ExceptionUITools.showMessage(ex, this);
      // ȡ���������Ѿ����ı��״̬
      for (int i = 0; i < this.m_sAllPeriods.length; i++) {
        if (this.m_sAllPeriods[i].equals(year + "-" + month)) {
          this.m_mTableModel.setValueAt(Boolean.FALSE, i, 3);
          break;
        }
      }
    }
  }

  /**
   * ִ�п��˲��� �������ڣ�(2005-8-31 18:23:25)
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
          /*@"����ڼ�: {0}  ȡ�����˳ɹ�"*/);
    }
    catch (Exception ex) {
      ExceptionUITools.showMessage(ex, this);
      // ȡ���������Ѿ����ı��״̬
      for (int i = 0; i < this.m_sAllPeriods.length; i++) {
        if (this.m_sAllPeriods[i].equals(year + "-" + month)) {
          this.m_mTableModel.setValueAt(Boolean.TRUE, i, 3);
          break;
        }
      }
    }
  }

  /**
   * ��ѯ�����ѹ����ڼ� �������ڣ�(2005-8-31 19:59:48)
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
          // ����ڼ���Ϊkey����hashmap�С�2005-06��
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
   * ���� AccountcheckDlg1 ����ֵ��
   * 
   * @return nc.ui.ia.ia402.AccountcheckDlg
   */
  /* ���棺�˷������������ɡ� */
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
   * ��������:���ҵ�����ı��� ����: ����ֵ:ҵ�����ı��� �쳣:
   */
  public String getTitle() {
    return this.m_sTitle;
  }

  /**
   * ���� UILabelCanAudit ����ֵ��
   * 
   * @return nc.ui.pub.beans.UILabel
   */
  /* ���棺�˷������������ɡ� */
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
   * ���� UILabelCanReaudit ����ֵ��
   * 
   * @return nc.ui.pub.beans.UILabel
   */
  /* ���棺�˷������������ɡ� */
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
   * ���� UIPanel1 ����ֵ��
   * 
   * @return nc.ui.pub.beans.UIPanel
   */
  /* ���棺�˷������������ɡ� */
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
   * ���� UITablePane1 ����ֵ��
   * 
   * @return nc.ui.pub.beans.UITablePane
   */
  /* ���棺�˷������������ɡ� */
  private nc.ui.pub.beans.UITablePane getUITablePane1() {
    if (this.ivjUITablePane1 == null) {
      try {
        this.ivjUITablePane1 = new nc.ui.pub.beans.UITablePane();
        this.ivjUITablePane1.setName("UITablePane1");
        // user code begin {1}
        // �����������ؼ�
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
   * �˴����뷽��˵���� �������ڣ�(2003-8-22 13:09:29)
   */
  private void handleAccount() throws Exception {
    long t1 = System.currentTimeMillis();
    this.account(this.m_sAccountPeriod);
    // �����·�״̬
    for (int i = 0; i < this.m_mTableModel.getRowCount(); i++) {
      Object oTemp = this.m_mTableModel.getValueAt(i, 1);
      if (oTemp.equals(this.m_sAccountPeriod)) {
        this.m_mTableModel.setValueAt(nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "20147020", "UPP20147020-000018")/* @res "�ѽ���" */, i, 2);
        break;
      }
    }

    // �����ĩ���˵��·ݵ���һ����
    String sTemp = CommonDataBO_Client.getNextPeriod(this.m_sCorpID,
        this.m_sAccountPeriod);

    // Ҫȡ����ĩ���˵��ڼ伴�ղ���ĩ���˵��ڼ�
    this.m_sReaccountPeriod = this.m_sAccountPeriod;

    // Ҫ���˵��ڼ伴�ղ���ĩ���˵��ڼ����һ���ڼ�
    if (sTemp.substring(sTemp.length() - 2).equals(ConstVO.m_sLastMonth)) {
      sTemp = nc.ui.ml.NCLangRes.getInstance().getStrByID("20147020",
          "UPP20147020-000019")/* @res "��" */;
    }

    this.m_sAccountPeriod = sTemp;

    this.getUILabelCanAudit().setText(
        nc.ui.ml.NCLangRes.getInstance().getStrByID("20147020",
            "UPP20147020-000020")/* @res "�ɽ��˵Ļ���ڼ䣺" */
            + this.m_sAccountPeriod);
    this.getUILabelCanReaudit().setText(
        nc.ui.ml.NCLangRes.getInstance().getStrByID("20147020",
            "UPP20147020-000021")/* @res "��ȡ�����˵Ļ���ڼ䣺" */
            + this.m_sReaccountPeriod);

    // ���ð�ť״̬
    if (this.m_sAccountPeriod.equals(nc.ui.ml.NCLangRes.getInstance().getStrByID(
        "20147020", "UPP20147020-000019")/* @res "��" */)) {
      this.m_bButtonAudit.setEnabled(false);
      this.m_bAuditEnabled = false;
    }
    else {
      this.m_bButtonAudit.setEnabled(true);
      this.m_bAuditEnabled = true;
    }

    if (this.m_sReaccountPeriod.equals(nc.ui.ml.NCLangRes.getInstance().getStrByID(
        "20147020", "UPP20147020-000019")/* @res "��" */)) {
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
        "UPP20147020-000032", null, value)/* @res "����ʱ: {0}Сʱ{1}����{2}��{3}����" */;

    this.m_sDispMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID("20147020",
        "UPP20147020-000022")/* @res "��ĩ���˳ɹ�!" */
        + s;
    this.m_bProcessFlag = false;

    MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/* @res "��ʾ" */,
        this.m_sDispMessage);

  }

  /**
   * ÿ�������׳��쳣ʱ������
   * 
   * @param exception
   *          java.lang.Throwable
   */
  private void handleException(java.lang.Throwable exception) {
    Log.error(exception);
  }

  /**
   * ��������:��ʼ������ ����: ����ֵ: �쳣:
   */
  private void init() {
    try {
      Vector<String> vTableHead = new Vector<String>();
      Vector<Vector> vTableData = new Vector<Vector>();

      for (int i = 0; i < this.m_sColumnName.length; i++){
        vTableHead.addElement(this.m_sColumnName[i]);
      }

      // ����û���������л���ڼ�
      String[] sPeriods = CommonDataBO_Client.getAllPeriods(this.m_sCorpID);
      this.m_sAllPeriods = sPeriods;

      // �����С��δ������
      String sUnClosePeriod = CommonDataBO_Client.getUnClosePeriod(this.m_sCorpID);

      // ������õĻ���ڼ�
      this.m_sStartPeriod = CommonDataBO_Client.getStartPeriod(this.m_sCorpID);

      // ��ù��˵Ļ���ڼ�
      HashMap hmClosedPeriods = this.queryClosedAccount();

      String sStatus = nc.ui.ml.NCLangRes.getInstance().getStrByID("20147020",
          "UPP20147020-000018")/* @res "�ѽ���" */;
      Boolean bClose = Boolean.FALSE;
      for (int i = 0; i < sPeriods.length; i++) {
        if (sUnClosePeriod.equals(sPeriods[i]) || sUnClosePeriod.equals("00")) {
          // ����û������
          sStatus = nc.ui.ml.NCLangRes.getInstance().getStrByID("20147020",
              "UPP20147020-000024")/* @res "δ����" */;
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

      // ���ù������Ƿ�ɱ༭
      this.m_mTableModel.setEditableRowColumn(-99, 3);// ////////////////
      for (int i = 0; i < sPeriods.length; i++) {// /////////////////
        if (sUnClosePeriod.equals(sPeriods[i]) || sUnClosePeriod.equals("00"))// //////////
        {// ////
          break;// /////
        }// /////
        this.m_mTableModel.setEditDisableRowColumn(i, 3);// /////////
      }// //////

      // ��ʾ�ɽ��˵Ļ���·�
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
            "20147020", "UPP20147020-000019")/* @res "��" */;
      }

      this.getUILabelCanAudit().setText(
          nc.ui.ml.NCLangRes.getInstance().getStrByID("20147020",
              "UPP20147020-000020")/* @res "�ɽ��˵Ļ���ڼ䣺" */
              +"  " +  this.m_sAccountPeriod);

      // ��ʾ��ȡ�����˵Ļ���·�
      this.m_sReaccountPeriod = CommonDataBO_Client.getPerviousPeriod(this.m_sCorpID,
          sUnClosePeriod);
      if (this.m_sReaccountPeriod.compareTo(this.m_sStartPeriod) < 0) {
        // ��ȡ����ĩ���˵��·��������ڼ�ǰ
        this.m_sReaccountPeriod = nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "20147020", "UPP20147020-000019")/* @res "��" */;
      }
      else {
        sTemp = this.m_sReaccountPeriod.substring(this.m_sReaccountPeriod.length() - 2,
            this.m_sReaccountPeriod.length());
        if (sTemp.equals(ConstVO.m_sLastMonth) || sTemp.equals("00")) {
          this.m_sReaccountPeriod = nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "20147020", "UPP20147020-000019")/* @res "��" */;
        }
      }

      this.getUILabelCanReaudit().setText(
          nc.ui.ml.NCLangRes.getInstance().getStrByID("20147020",
              "UPP20147020-000021")/* @res "��ȡ�����˵Ļ���ڼ䣺" */
              + "  " + this.m_sReaccountPeriod);

      // ���ð�ť״̬
      if (this.m_sAccountPeriod.equals(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "20147020", "UPP20147020-000019")/* @res "��" */)) {
        this.m_bButtonAudit.setEnabled(false);
        this.m_bAuditEnabled = false;
      }
      else {
        this.m_bButtonAudit.setEnabled(true);
        this.m_bAuditEnabled = true;
      }

      if (this.m_sReaccountPeriod.equals(nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("20147020", "UPP20147020-000019")/* @res "��" */)) {
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
   * ��ʼ���ࡣ
   */
  /* ���棺�˷������������ɡ� */
  private void initialize() throws Exception {
    // user code begin {1}
    // ���ð�ť״̬
    this.m_bButtonAudit.setEnabled(true);
    this.m_bButtonReaudit.setEnabled(true);
    this.setButtons(this.m_aryButtonGroup);

    // ��õ�λ����
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
   * ��������:����ĩ���˰�ť���� ����: ����ֵ: �쳣:
   */
  private void onButtonAccountClicked() {

    int check = this.showYesNoMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
        "20147020", "UPP20147020-000054"));
    /* @res "��ĩ����ǰ��ȷ�����Ѿ������½��飬����Ѿ�������������ǡ�������ĩ���ʣ�����������ȡ�������½����" */

    if (check != 4) {
      return;
    }

    try {
      // ���Ŀǰ��״̬
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
   * ����ʵ�ָ÷�������Ӧ��ť�¼���
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

    // Ϊ��֤��ӡ���水ť���ã���Ϊ��ģʽ�Ի���
    this.getAccountcheckDlg1().setModal(false);
    this.getAccountcheckDlg1().setLocationRelativeTo(this);
    this.getAccountcheckDlg1().showModal();

  }
  
  // start: add by zip 2013/11/7 No 9
  private void onButtonYLCheckClicked() {
	  YLCheckDlg dlg = new YLCheckDlg(this);
	  dlg.setTitle("ԭ�����ݺ˶�");
	  dlg.showModal();
  }
  // end

  /**
   * ��������:��ȡ����ť���� ����: ����ֵ: �쳣:
   */
  private void onButtonReaccountClicked() {
    try {
      String str = CommonDataBO_Client.checkUnload(this.ce.getCorporationID(),
          this.m_sReaccountPeriod);

      if (!str.equalsIgnoreCase("load")) {
        this.showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "20143010", "UPP20143010-000314")/*@"��ǰ����ڼ�������Ѿ���ж��������ȡ������"*/);
        return;
      }

    }
    catch (BusinessException e) {
      Log.error(e);
    }

    try {

      long t1 = System.currentTimeMillis();

      // �����߳�
      String[] value = new String[] {
        this.m_sReaccountPeriod
      };
      this.m_sDispMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID("20147020",
          "UPP20147020-000033", null, value)/* @res "���ڶ�{0}�ڼ����ȡ����ĩ���ʣ����Ժ�" */;
      this.m_bProcessFlag = true;
      Thread td = new Thread(this);
      td.start();
      this.reaccount(this.m_sReaccountPeriod);
      // �����·�״̬
      for (int i = 0; i < this.m_mTableModel.getRowCount(); i++) {
        Object oTemp = this.m_mTableModel.getValueAt(i, 1);
        if (oTemp.equals(this.m_sReaccountPeriod)) {
          this.m_mTableModel.setValueAt(nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "20147020", "UPP20147020-000024")/* @res "δ����" */, i, 2);
          break;
        }
      }

      // ���ȡ����ĩ���˵��·ݵ�ǰһ����
      String sTemp = CommonDataBO_Client.getPerviousPeriod(this.m_sCorpID,
          this.m_sReaccountPeriod);

      // Ҫ���˵��ڼ伴�ղ�ȡ����ĩ���˵��ڼ�
      this.m_sAccountPeriod = this.m_sReaccountPeriod;

      // Ҫȡ����ĩ���˵��ڼ伴ǰһ���ڼ�
      if (this.m_sReaccountPeriod.compareTo(this.m_sStartPeriod) <= 0) {
        // ��ȡ����ĩ���˵��·��������ڼ�ǰ
        sTemp = nc.ui.ml.NCLangRes.getInstance().getStrByID("20147020",
            "UPP20147020-000019")/* @res "��" */;
      }

      this.m_sReaccountPeriod = sTemp;
      this.getUILabelCanAudit().setText(
          nc.ui.ml.NCLangRes.getInstance().getStrByID("20147020",
              "UPP20147020-000020")/* @res "�ɽ��˵Ļ���ڼ䣺" */
              + this.m_sAccountPeriod);
      this.getUILabelCanReaudit().setText(
          nc.ui.ml.NCLangRes.getInstance().getStrByID("20147020",
              "UPP20147020-000021")/* @res "��ȡ�����˵Ļ���ڼ䣺" */
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
          "UPP20147020-000032", null, value)/* @res "����ʱ: {0}Сʱ{1}����{2}��{3}����" */;

      this.m_sDispMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID("20147020",
          "UPP20147020-000028")/* @res "ȡ����ĩ���˳ɹ�!" */
          + s;
      this.m_bProcessFlag = false;

      MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/* @res "��ʾ" */,
          this.m_sDispMessage);

      // ���ð�ť״̬
      if (this.m_sAccountPeriod.equals(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "20147020", "UPP20147020-000019")/* @res "��" */)) {
        this.m_bButtonAudit.setEnabled(false);
        this.m_bAuditEnabled = false;
      }
      else {
        this.m_bButtonAudit.setEnabled(true);
        this.m_bAuditEnabled = true;
      }

      if (this.m_sReaccountPeriod.equals(nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("20147020", "UPP20147020-000019")/* @res "��" */)) {
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
   * �������ڽ��������ɳ�ʼ����������á�����Ա��������� �÷�������ɽ����ʼ������������״̬����Ϣ�ȣ��� �������ڣ�(2001-11-20
   * 17:03:18)
   */
  public void postInit() {
    if (this.m_sInitMessage.length() != 0) {
      MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/* @res "��ʾ" */,
          this.m_sInitMessage);
    }
  }

  /**
   * ��������:ȡ����ĩ���� ����: String sPeriod ----- Ҫȡ����ĩ���˵Ļ���ڼ� ����ֵ:�Ƿ�ɹ� �쳣:
   */
  private void reaccount(String sPeriod) throws Exception {
    String sAccountYear = sPeriod.substring(0, 4);
    String sAccountMonth = sPeriod.substring(5, 7);

    String[] sSysInfo = new String[7];
    /*
     * sSysInfo[0]:��¼��λ sSysInfo[1]:��¼����� sSysInfo[2]:��¼����� sSysInfo[3]:Ҫ���˵Ļ����
     * sSysInfo[4]:Ҫ���˵Ļ���� sSysInfo[5]:��¼�û� sSysInfo[6]:��¼����
     */
    sSysInfo[0] = this.m_sCorpID;
    sSysInfo[1] = this.ce.getAccountYear();
    sSysInfo[2] = this.ce.getAccountMonth();
    sSysInfo[3] = sAccountYear;
    sSysInfo[4] = sAccountMonth;
    sSysInfo[5] = this.ce.getUser().getPrimaryKey();
    sSysInfo[6] = this.ce.getBusinessDate().toString();

    // ��ʼȡ����ĩ����
    AccountBO_Client.reAccount(sSysInfo, this.ce.getClientLink());
    for (int i = 0; i < this.m_sAllPeriods.length; i++) {
      if (this.m_sAllPeriods[i].equals(sPeriod)) {
        this.m_mTableModel.setEditableRowColumn(i, 3);
        break;
      }
    }
  }

  /**
   * ��������: ����: ����ֵ: �쳣:
   */
  public void run() {
    int nCount = 1, nCount1 = 0;
    String sMessage = this.m_sDispMessage;
    while (this.m_bProcessFlag) {
      // ����
      try {
        Thread.sleep(100);
      }
      catch (InterruptedException e) {
        e.printStackTrace();
      }

      // ִ����ʾ��Ϣ
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
    // TODO �Զ����ɷ������

  }

}