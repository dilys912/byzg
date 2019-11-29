package nc.ui.ia.ia301;

import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import javax.swing.Spring;
import javax.swing.SpringLayout;
import javax.swing.SpringLayout.Constraints;
import javax.swing.SwingUtilities;
import nc.ui.ia.bill.IABillCardPanel;
import nc.ui.ia.ia305.QueryDialog;
import nc.ui.ia.pub.ExceptionUITools;
import nc.ui.ia.pub.IAEnvironment;
import nc.ui.ia.pub.IAUITable;
import nc.ui.ia.pub.RemoteCall;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.UITablePane;
import nc.ui.pub.print.PrintDirectEntry;
import nc.vo.ia.ia301.AuditBillVO;
import nc.vo.ia.ia301.AuditResultVO;
import nc.vo.ia.ia301.ParamVO;
import nc.vo.ia.ia305.CalcTime;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.sm.UserVO;

public class AuditClientUI extends ToftPanel
  implements Runnable
{
  private static final long serialVersionUID = 7642377175608230459L;
  private ButtonObject m_btnCompute = new ButtonObject(NCLangRes.getInstance().getStrByID("20146010", 
    "UPT20146010-000001"), 
    NCLangRes.getInstance().getStrByID("20146010", 
    "UPP20146010-000058"), 2, "成本计算");

  private ButtonObject m_btnSave = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000001"), 
    NCLangRes.getInstance().getStrByID("20146010", 
    "UPP20146010-000103"), 
    2, "保存");

  private ButtonObject m_btnCancel = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000008"), 
    NCLangRes.getInstance().getStrByID("20146010", 
    "UPP20146010-000104"), 
    2, "取消");

  private ButtonObject m_btnPrint = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000007"), 
    NCLangRes.getInstance().getStrByID("20146010", 
    "UPP20146010-000105"), 
    2, "打印");

  private ButtonObject m_btnQuery = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000006"), 
    NCLangRes.getInstance().getStrByID("common", "UC001-0000006"), 
    2, "查询");

  private ButtonObject m_btnBill = new ButtonObject(NCLangRes.getInstance().getStrByID("20146010", 
    "UPT20146010-000002"), 
    NCLangRes.getInstance().getStrByID("20146010", 
    "UPT20146010-000002"), 2, "单据联查");

  private ButtonObject m_btnReturn = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000038"), 
    NCLangRes.getInstance().getStrByID("20146010", 
    "UPP20146010-000106"), 
    2, "返回");

  private ButtonObject[] m_aryButtonGroup = { 
    this.m_btnQuery, this.m_btnCompute, this.m_btnBill, this.m_btnReturn, 
    this.m_btnPrint, this.m_btnSave, this.m_btnCancel };

  private IAEnvironment my_environment = null;
  private String m_sUser;
  private String m_sUserName;
  private String m_sCorp;
  private String m_sCorpName;
  private String m_sLogindata;
  private String m_sAccountMonth = null;

  private String m_sAccountYear = null;

  private String[] m_sSysinfo = null;
  private String[] m_sConditions;
  private AuditResultVO[] m_result = null;

  private IABillCardPanel ivjIABillCardPanel1 = null;

  private AuditTablePanel ivjAuditTablePanel1 = null;

  private AuditBillTablePanel ivjAuditBillTablePanel1 = null;

  private String[] bill_bids = null;

  private AuditBillVO[] m_vAuditBill = null;

  private QueryDialog m_dlg = null;

  private ParamVO m_voParam = null;
  private UpdateThread progress;
  private Vector m_vData = null;

  public AuditClientUI()
  {
    initialize();
  }

  private AuditBillTablePanel getAuditBillTablePanel1()
  {
    if (this.ivjAuditBillTablePanel1 == null) {
      try {
        this.ivjAuditBillTablePanel1 = 
          new AuditBillTablePanel(this.m_sCorp);
        this.ivjAuditBillTablePanel1.setName("AuditBillTablePanel1");
        this.ivjAuditBillTablePanel1.setSize(453, -403);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjAuditBillTablePanel1;
  }

  private AuditTablePanel getAuditTablePanel1()
  {
    if (this.ivjAuditTablePanel1 == null) {
      try {
        this.ivjAuditTablePanel1 = new AuditTablePanel();
        this.ivjAuditTablePanel1.setName("AuditTablePanel1");
        this.ivjAuditTablePanel1.setSize(774, 403);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjAuditTablePanel1;
  }

  private IABillCardPanel getIABillCardPanel1()
  {
    if (this.ivjIABillCardPanel1 == null) {
      try {
        this.ivjIABillCardPanel1 = new IABillCardPanel();
        this.ivjIABillCardPanel1.setName("IABillCardPanel1");

        this.ivjIABillCardPanel1.setSize(774, 419);
        this.ivjIABillCardPanel1.setSize(774, 419);
      }
      catch (Throwable ivjExc) {
        handleException(ivjExc);
      }
    }
    return this.ivjIABillCardPanel1;
  }

  public String getTitle()
  {
    return NCLangRes.getInstance().getStrByID("20146010", 
      "UPT20146010-000001");
  }

  private void handleException(Throwable exception)
  {
  }

  private void init()
  {
    this.m_sUser = this.my_environment.getUser().getPrimaryKey();
    this.m_sUserName = this.my_environment.getUser().getUserName();
    this.m_sCorpName = this.my_environment.getCorporationName();
    this.m_sLogindata = this.my_environment.getBusinessDate().toString();
    this.m_sAccountYear = this.my_environment.getAccountYear();
    this.m_sAccountMonth = this.my_environment.getAccountMonth();

    this.m_sSysinfo = new String[5];
    this.m_sSysinfo[0] = this.m_sCorp;
    this.m_sSysinfo[1] = this.m_sLogindata;
    this.m_sSysinfo[2] = this.m_sUser;
    this.m_sSysinfo[3] = this.m_sAccountYear;
    this.m_sSysinfo[4] = this.m_sAccountMonth;

    this.m_btnCompute.setEnabled(false);
    this.m_btnSave.setVisible(false);
    this.m_btnCancel.setVisible(false);
    this.m_btnPrint.setEnabled(false);
    this.m_btnBill.setEnabled(false);
    this.m_btnReturn.setVisible(false);
    setButtons(this.m_aryButtonGroup);

    getIABillCardPanel1().setVisible(false);
    getAuditTablePanel1().setVisible(false);
    getAuditBillTablePanel1().setVisible(true);
    getAuditBillTablePanel1().display(new Vector());
  }

  private void initialize()
  {
    try
    {
      this.my_environment = new IAEnvironment();
      this.m_sCorp = this.my_environment.getCorporationID();
      setName("AuditClientUI");
      setSize(774, 419);

      SpringLayout layout = new SpringLayout();
      setLayout(layout);

      SpringLayout.Constraints constraint = new SpringLayout.Constraints();
      constraint.setConstraint("West", Spring.constant(0));
      constraint.setConstraint("North", Spring.constant(0));
      add(getIABillCardPanel1(), constraint);

      constraint = new SpringLayout.Constraints();
      constraint.setConstraint("West", Spring.constant(0));
      constraint.setConstraint("North", Spring.constant(16));
      add(getAuditTablePanel1(), constraint);

      constraint = new SpringLayout.Constraints();
      constraint.setConstraint("West", Spring.constant(0));
      constraint.setConstraint("North", Spring.constant(419));
      add(getAuditBillTablePanel1(), constraint);
    }
    catch (Throwable ivjExc)
    {
      handleException(ivjExc);
    }
    init();
    setLayout(new CardLayout());
  }

  private void onBtnBillClicked()
  {
    try
    {
      int iRow = getAuditBillTablePanel1().getTblPnl().getTable()
        .getSelectedRow();
      if (iRow <= -1) {
        showHintMessage(NCLangRes.getInstance()
          .getStrByID("20146010", "UPP20146010-000107"));
        return;
      }
      getAuditBillTablePanel1().setVisible(false);
      getIABillCardPanel1().setVisible(true);
      getIABillCardPanel1().repaint();
      String sBilltype = this.m_vAuditBill[iRow].getBillType();

      setTitleText(this.m_vAuditBill[iRow].getBilltypename());

      String sBillid = this.m_vAuditBill[iRow].getPkBill();
      this.m_btnQuery.setEnabled(false);
      this.m_btnCompute.setEnabled(false);
      this.m_btnSave.setVisible(false);
      this.m_btnCancel.setVisible(false);
      this.m_btnPrint.setEnabled(false);
      this.m_btnBill.setEnabled(false);
      this.m_btnReturn.setVisible(true);
      this.m_btnReturn.setEnabled(true);
      updateButtons();
      getIABillCardPanel1().queryBill(this.m_sCorp, sBilltype, sBillid, 
        "ia502");
      setTitleText(getIABillCardPanel1().getTitle());
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  private void onBtnQueryClicked()
  {
    if (this.m_dlg == null) {
      this.m_dlg = getQueryDialog();
    }
    this.m_dlg.setLocationRelativeTo(this);
    this.m_dlg.showModal();
    if (!(this.m_dlg.isCloseOK()))
      return;
    try
    {
      this.m_dlg.checkOncetime(this.m_dlg.getConditionVO(), new String[] { 
        "needcalculatecbdx" });

      this.m_dlg.checkOncetime(this.m_dlg.getConditionVO(), new String[] { 
        "autofillJHJForCCPRKD" });
    }
    catch (Exception ex)
    {
      ExceptionUITools.showMessage(ex, this);
      return;
    }

    showHintMessage(NCLangRes.getInstance()
      .getStrByID("20146010", "UPP20146010-000108"));

    this.m_sConditions = this.m_dlg.getCondPnl().getconditions();
    this.m_voParam = new ParamVO();
    this.m_voParam.setCondition(this.m_sConditions);
    this.m_voParam.setSystemInfo(this.m_sSysinfo);
    String[] conditions = (String[])null;
    try {
      this.m_voParam.setTable(this.m_dlg.getTables());
      this.m_voParam.setConnection(this.m_dlg.getConnections());
      conditions = this.m_dlg.getConditions();
    }
    catch (Exception ex) {
      ExceptionUITools.showMessage(ex, this);
      return;
    }
    if (conditions != null) {
      List list = new ArrayList();
      for (int i = 0; i < conditions.length; ++i) {
        String condition = filterCondition(conditions[i]);
        list.add(condition);
      }
      conditions = new String[list.size()];
      conditions = (String[])list.toArray(conditions);
    }
    this.m_voParam.setAuto_inTraceOut(this.m_dlg.isAuto_TraceOut());
    this.m_voParam.setConditions(conditions);

    if ((this.m_sConditions != null) && (this.m_sConditions[4] != null) && 
      (this.m_sLogindata.compareTo(this.m_sConditions[4]) < 0)) {
      String[] args = new String[2];
      args[0] = this.m_sLogindata;
      args[1] = this.m_sConditions[4];
      String message = NCLangRes.getInstance().getStrByID(
        "20146010", "UPP20146010-000130", null, args);

      showWarningMessage(message);
    }

    RemoteCall call = new RemoteCall();
    call.execute(this, this, "query", new Object[] { 
      call });
  }

  private String filterCondition(String condition)
  {
    String str = condition;
    Object[] value;
    boolean flag;
    if (str.indexOf("needcalculatecbdx") >= 0) {
      value = filterCondition("needcalculatecbdx", condition);
      str = (String)value[0];
      flag = ((Boolean)value[1]).booleanValue();
      this.m_voParam.setNeedcalculatecbdx(flag);
    }

    if (str.indexOf("autofillJHJForCCPRKD") >= 0) {
      value = filterCondition("autofillJHJForCCPRKD", str);
      str = (String)value[0];
      flag = ((Boolean)value[1]).booleanValue();
      this.m_voParam.setAutofillJHJForCCPRKD(flag);
    }
    return str;
  }

  private Object[] filterCondition(String name, String condition) {
    boolean flag = false;
    if (condition.indexOf(name) >= 0) {
      int index = condition.indexOf(name);
      int index_end = -1;
      if (index >= 0) {
        if (condition.indexOf("Y", index) >= 0) {
          flag = true;
          index_end = condition.indexOf("Y", index);
        }
        else {
          index_end = condition.indexOf("N", index);
        }
      }

      StringBuffer bf = new StringBuffer(condition);
      bf.replace(index, index_end + 2, "1=1");
      condition = bf.toString();
    }
    Object[] value = new Object[2];
    value[0] = condition;
    value[1] = Boolean.valueOf(flag);
    return value;
  }

  public void query(RemoteCall call) {
    HashMap result = new HashMap();
    try
    {
      this.m_vAuditBill = null;
      HashMap map = AuditBO_Client.getAuditBill(this.m_voParam);
      if (map != null) {
        result.putAll(map);
        this.m_vAuditBill = ((AuditBillVO[])result.get("AuditBill"));
      }
    }
    catch (Exception ex)
    {
      ExceptionUITools.showMessage(ex, this, call);
      return;
    }
//    Runnable end = new runable(this, result, call);

//    SwingUtilities.invokeLater(end);
  }

  public void onButtonClicked(ButtonObject bo)
  {
    showHintMessage("");
    if (bo == this.m_btnSave) {
      onSaveButtonClicked();
    }
    else if (bo == this.m_btnCancel) {
      onCancleButtonClicked();
    }
    else if (bo == this.m_btnCompute) {
      onComputeButtonClicked();
    }
    else if (bo == this.m_btnPrint) {
      onPrintButtonClicked();
    }
    else if (bo == this.m_btnQuery) {
      onBtnQueryClicked();
    }
    else if (bo == this.m_btnBill) {
      onBtnBillClicked();
    }
    else if (bo == this.m_btnReturn)
      onBtnReturnClicked();
  }

  private void onCancleButtonClicked()
  {
    this.m_vData = new Vector();
    Vector v = null;
    int iCount = this.m_result.length;
    AuditBillVOTranslator.translate(this.m_result);
    for (int i = 0; i < iCount; ++i) {
      v = new Vector();
      v.addElement(this.m_result[i].getwhname());
      v.addElement(this.m_result[i].getInvCode());
      v.addElement(this.m_result[i].getinvname());
      v.addElement(this.m_result[i].getsbatch());
      if (this.m_result[i].getisOkcompute()) {
        v.addElement(NCLangRes.getInstance()
          .getStrByID("20146010", 
          "UPP20146010-000109"));
        v.addElement(null);
        v.addElement(null);
        v.addElement(null);
        v.addElement(null);
        v.addElement(null);
      }
      else {
        v.addElement(NCLangRes.getInstance()
          .getStrByID("20146010", 
          "UPP20146010-000110"));
        v.addElement(this.m_result[i].getReturnMess());
        v.addElement(this.m_result[i].getBillTypeName());
        v.addElement(this.m_result[i].getBillCode());
        v.addElement(this.m_result[i].getRownumber());
        v.addElement(this.m_result[i].getSourcebillcode());
        Throwable cause = this.m_result[i].getCause();
        if (cause != null) {
          cause.printStackTrace();
        }
      }
      this.m_vData.addElement(v.clone());
    }

    this.m_btnSave.setVisible(false);
    this.m_btnCancel.setVisible(false);
    this.m_btnPrint.setVisible(true);
    this.m_btnPrint.setEnabled(true);
    this.m_btnQuery.setVisible(true);
    setButtons(this.m_aryButtonGroup);
    getIABillCardPanel1().setVisible(false);
    getAuditTablePanel1().setVisible(true);
    getAuditTablePanel1().display(this.m_vData);
    showHintMessage(NCLangRes.getInstance()
      .getStrByID("20146010", "UPP20146010-000111"));
    setTitleText(getTitle());
  }

  private void onComputeButtonClicked()
  {
    int[] ia = getAuditBillTablePanel1().getTable().getSelectedRows();
    if (ia.length == 0) {
      String message = NCLangRes.getInstance().getStrByID("20146050", 
        "UPP20146050-000017");

      showWarningMessage(message);
      showHintMessage(message);
      return;
    }
    String sMsg = NCLangRes.getInstance().getStrByID("20146010", 
      "UPP20146010-000140");
    if (showYesNoMessage(sMsg) != 4) {
      return;
    }

    int i = 1000;
    if (IAEnvironment.isInDebug()) {
      i = 5000;
    }

    this.progress = 
      new UpdateThread(this.m_voParam, this.m_sCorp, 
      this.m_sUser, this, i);
    this.progress.start();
    Thread th = new Thread(this);
    th.start();
  }

  private void onPrintButtonClicked()
  {
    String[][] sa2ColName;
    Vector vData;
    Object[][] oa2pData;
    int iCount;
    Vector v;
    int i;
    String sTitle;
    String[] args;
    String sTopstr;
    String sBotstr;
    int[] iaColwidth;
    if ((getAuditBillTablePanel1().isVisible()) && 
      (!(getAuditTablePanel1().isVisible()))) {
      sa2ColName = new String[][] { 
        { 
        NCLangRes.getInstance().getStrByID("common", 
        "UC000-0001821"), 
        NCLangRes.getInstance().getStrByID("common", 
        "UC000-0000799"), 
        NCLangRes.getInstance().getStrByID("common", 
        "UC000-0000794"), 
        NCLangRes.getInstance().getStrByID("common", 
        "UC000-0000807"), 
        NCLangRes.getInstance().getStrByID("common", 
        "UC000-0002750"), 
        NCLangRes.getInstance().getStrByID("20146010", 
        "UPP20146010-000091"), 
        NCLangRes.getInstance().getStrByID("common", 
        "UC000-0001825"), 
        NCLangRes.getInstance().getStrByID("common", 
        "UC000-0001480"), 
        NCLangRes.getInstance().getStrByID("common", 
        "UC000-0001453"), 
        NCLangRes.getInstance().getStrByID("SCMCOMMON", 
        "UPPSCMCommon-000182"), 
        NCLangRes.getInstance().getStrByID("common", 
        "UC000-0003448"), 
        NCLangRes.getInstance().getStrByID("common", 
        "UC000-0001240"), 
        NCLangRes.getInstance().getStrByID("common", 
        "UC000-0000080"), 
        NCLangRes.getInstance().getStrByID("common", 
        "UC000-0002282"), 
        NCLangRes.getInstance().getStrByID("common", 
        "UC000-0000741"), 
        NCLangRes.getInstance().getStrByID("common", 
        "UC000-0004112"), 
        NCLangRes.getInstance().getStrByID("common", 
        "UC000-0003975"), 
        NCLangRes.getInstance().getStrByID("20146010", 
        "UPP20146010-000136"), 
        NCLangRes.getInstance().getStrByID("common", 
        "UC000-0002188"), 
        "来源单据号(开发)", "来源单据类型(开发)" } };

      vData = this.m_vData;
      oa2pData = new Object[vData.size()][19];
      iCount = vData.size();
      v = null;
      for (i = 0; i < iCount; ++i) {
        v = (Vector)vData.elementAt(i);
        oa2pData[i][0] = v.elementAt(0);
        oa2pData[i][1] = v.elementAt(1);
        oa2pData[i][2] = v.elementAt(2);
        oa2pData[i][3] = v.elementAt(3);
        oa2pData[i][4] = v.elementAt(4);
        oa2pData[i][5] = v.elementAt(5);
        oa2pData[i][6] = v.elementAt(6);
        oa2pData[i][7] = v.elementAt(7);
        oa2pData[i][8] = v.elementAt(8);
        oa2pData[i][9] = v.elementAt(9);
        oa2pData[i][10] = v.elementAt(10);
        oa2pData[i][11] = v.elementAt(11);
        oa2pData[i][12] = v.elementAt(12);
        oa2pData[i][13] = v.elementAt(13);
        oa2pData[i][14] = v.elementAt(14);
        oa2pData[i][15] = v.elementAt(15);
        oa2pData[i][16] = v.elementAt(16);
        oa2pData[i][17] = v.elementAt(17);
        oa2pData[i][18] = v.elementAt(18);
        oa2pData[i][19] = v.elementAt(19);
        oa2pData[i][20] = v.elementAt(20);
      }
      sTitle = NCLangRes.getInstance().getStrByID("20146010", 
        "UPP20146010-000112");

      args = new String[3];
      args[0] = this.m_sCorpName;
      args[1] = this.m_sAccountYear;
      args[2] = this.m_sAccountMonth;
      sTopstr = NCLangRes.getInstance().getStrByID("20146010", 
        "UPP20146010-000133", null, args);

      args = new String[2];
      args[0] = this.m_sUserName;
      args[1] = this.m_sLogindata;
      sBotstr = NCLangRes.getInstance().getStrByID("20146010", 
        "UPP20146010-000134", null, args);

      iaColwidth = new int[] { 
        30, 70, 110, 70, 110, 70, 70, 70, 70, 50, 100, 100, 70, 90, 90, 100, 
        70, 90, 70, 
        100, 110 };

      int[] iaFlag = { 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 0, 2 };

      PrintDirectEntry print = new PrintDirectEntry();
      print.setFixedRows(1);
      print.setTitle(sTitle);
      print.setTopStr(sTopstr);
      print.setBottomStr(sBotstr);
      print.setColNames(sa2ColName);
      print.setColWidth(iaColwidth);
      print.setAlignFlag(iaFlag);
      print.setData(oa2pData);
      print.preview();
    } else {
      if ((!(getAuditTablePanel1().isVisible())) || 
        (getAuditBillTablePanel1().isVisible())) return;
      sa2ColName = new String[][] { 
        { 
        NCLangRes.getInstance().getStrByID("common", 
        "UC000-0001825"), 
        NCLangRes.getInstance().getStrByID("common", 
        "UC000-0001480"), 
        NCLangRes.getInstance().getStrByID("common", 
        "UC000-0001453"), 
        NCLangRes.getInstance().getStrByID("SCMCOMMON", 
        "UPPSCMCommon-000182"), 
        NCLangRes.getInstance().getStrByID("20146010", 
        "UPP20146010-000095"), 
        NCLangRes.getInstance().getStrByID("20146010", 
        "UPP20146010-000096"), 
        NCLangRes.getInstance().getStrByID("20146010", 
        "UPP20146010-000097"), 
        NCLangRes.getInstance().getStrByID("20146010", 
        "UPP20146010-000098"), 
        NCLangRes.getInstance().getStrByID("20146010", 
        "UPP20146010-000099"), 
        NCLangRes.getInstance().getStrByID("20146010", 
        "UPP20146010-000100") } };

      vData = this.m_vData;
      oa2pData = new Object[vData.size()][10];
      iCount = vData.size();
      v = null;
      for (int a = 0; a < iCount;a++) {
        v = (Vector)vData.elementAt(a);
        oa2pData[a][0] = v.elementAt(0);
        oa2pData[a][1] = v.elementAt(1);
        oa2pData[a][2] = v.elementAt(2);
        oa2pData[a][3] = v.elementAt(3);
        oa2pData[a][4] = v.elementAt(4);
        oa2pData[a][5] = v.elementAt(5);
        oa2pData[a][6] = v.elementAt(6);
        oa2pData[a][7] = v.elementAt(7);
        oa2pData[a][8] = v.elementAt(8);
        oa2pData[a][9] = v.elementAt(9);
      }
      sTitle = NCLangRes.getInstance().getStrByID("20146010", 
        "UPP20146010-000113");

      args = new String[3];
      args[0] = this.m_sCorpName;
      args[1] = this.m_sAccountYear;
      args[2] = this.m_sAccountMonth;
      sTopstr = NCLangRes.getInstance().getStrByID("20146010", 
        "UPP20146010-000133", null, args);

      args = new String[2];
      args[0] = this.m_sUserName;
      args[1] = this.m_sLogindata;
      sBotstr = NCLangRes.getInstance().getStrByID("20146010", 
        "UPP20146010-000134", null, args);

      iaColwidth = new int[] { 
        70, 70, 70, 50, 70, 90, 70, 110, 70, 110 };

      PrintDirectEntry print = new PrintDirectEntry();
      print.setFixedRows(1);
      print.setTitle(sTitle);
      print.setTopStr(sTopstr);
      print.setBottomStr(sBotstr);
      print.setColNames(sa2ColName);
      print.setColWidth(iaColwidth);
      print.setData(oa2pData);
      print.preview();
    }
  }

  private void onSaveButtonClicked()
  {
    showHintMessage(NCLangRes.getInstance()
      .getStrByID("20146010", "UPP20146010-000114"));

    String[][] prices = (String[][])null;
    try {
      prices = getAuditBillTablePanel1().getPrices();
    }
    catch (BusinessException ex)
    {
      ExceptionUITools.showMessage(ex, this);
      return;
    }

    if ((prices == null) || (prices[0] == null) || (prices[0][0].length() == 0)) {
      return;
    }

    String[][] SavePrices = new String[prices[0].length][3];

    for (int i = 0; i < prices[0].length; ++i) {
      SavePrices[i][0] = this.bill_bids[i];
      SavePrices[i][1] = prices[0][i];
      SavePrices[i][2] = prices[1][i];
    }

    try
    {
      AuditBO_Client.SavePrices(SavePrices, this.m_sUser);
    }
    catch (Exception ex)
    {
      ExceptionUITools.showMessage(ex, this);
    }

    setTitleText(getTitle());

    this.m_btnSave.setVisible(false);
    this.m_btnCancel.setVisible(false);
    this.m_btnPrint.setVisible(true);
    this.m_btnQuery.setVisible(true);

    setButtons(this.m_aryButtonGroup);
    getIABillCardPanel1().setVisible(false);
    showHintMessage(NCLangRes.getInstance()
      .getStrByID("20146010", "UPP20146010-000117"));

    int i = 1000;
    if (IAEnvironment.isInDebug()) {
      i = 20000;
    }

    this.m_vAuditBill = null;
    this.progress = 
      new UpdateThread(this.m_voParam, this.m_sCorp, 
      this.m_sUser, this, i);

    this.progress.start();
    Thread th = new Thread(this);
    th.start();
  }

  private void process()
  {
    boolean inputsucc = true;

    int iCount = this.m_result.length;
    for (int i = 0; i < iCount; ++i) {
      if ((this.m_result[i].getisOkcompute()) || 
        (!(this.m_result[i].getisUserImput())))
      {
        continue;
      }

      handleUserInput();
      return;
    }

    AuditBillVOTranslator.translate(this.m_result);

    this.m_vData = new Vector();
    Vector v = null;
    iCount = this.m_result.length;
    for (int i = 0; i < iCount; ++i) {
      v = new Vector();
      v.addElement(this.m_result[i].getwhname());
      v.addElement(this.m_result[i].getInvCode());
      v.addElement(this.m_result[i].getinvname());
      v.addElement(this.m_result[i].getsbatch());
      if (this.m_result[i].getisOkcompute())
      {
        v.addElement(NCLangRes.getInstance()
          .getStrByID("20146010", 
          "UPP20146010-000119"));

        v.addElement(null);
        v.addElement(null);
        v.addElement(null);
        v.addElement(null);
        v.addElement(null);
      }
      else {
        v.addElement(NCLangRes.getInstance()
          .getStrByID("20146010", 
          "UPP20146010-000120"));
        if ((this.m_result[i].getisUserImput()) && (!(inputsucc))) {
          v.addElement(NCLangRes.getInstance()
            .getStrByID("20146010", 
            "UPP20146010-000121"));
        }
        else {
          v.addElement(this.m_result[i].getReturnMess());
        }
        v.addElement(this.m_result[i].getBillTypeName());
        v.addElement(this.m_result[i].getBillCode());
        v.addElement(this.m_result[i].getRownumber());
        v.addElement(this.m_result[i].getSourcebillcode());
        Throwable cause = this.m_result[i].getCause();
        if (cause != null) {
          cause.printStackTrace();
        }
      }
      this.m_vData.addElement(v.clone());
    }

    this.m_btnCompute.setEnabled(false);
    setButtons(this.m_aryButtonGroup);
    getAuditBillTablePanel1().setVisible(false);
    getIABillCardPanel1().setVisible(false);
    getAuditTablePanel1().setVisible(true);
    getAuditTablePanel1().display(this.m_vData);
  }

  private void handleUserInput()
  {
    int iCount = this.m_result.length;

    Vector v = new Vector();

    for (int i = 0; i < iCount; ++i)
    {
      if ((this.m_result[i].getisOkcompute()) || 
        (!(this.m_result[i].getisUserImput())))
        continue;
      v.addElement(this.m_result[i].getNotAuditedBillItems()[0]);
    }

    this.bill_bids = new String[v.size()];

    v.copyInto(this.bill_bids);

    ParamVO para = new ParamVO();
    para.setBill_bids(this.bill_bids);
    para.bIfForHandInput = true;
    HashMap map = null;
    try
    {
      map = AuditBO_Client.getAuditBill(para);
    }
    catch (Exception ex)
    {
      ExceptionUITools.showMessage(ex, this);
      return;
    }

    if (map != null) {
      this.m_vAuditBill = ((AuditBillVO[])map.get("handinput"));
    }
    else {
      this.m_vAuditBill = new AuditBillVO[0];
    }

    AuditBillVOTranslator.translate(this.m_vAuditBill);

    iCount = this.m_vAuditBill.length;

    this.m_vData = new Vector();

    this.bill_bids = new String[iCount];

    for (int i = 0; i < iCount; ++i)
    {
      Vector element = new Vector();

      element.addElement(i + 1);
      element.addElement(this.m_vAuditBill[i].getDbilldate().toString());
      element.addElement(this.m_vAuditBill[i].getVbillcode());
      element.addElement(this.m_vAuditBill[i].getBilltypename());
      element.addElement(this.m_vAuditBill[i].getVsourcebillcode());
      element.addElement(this.m_vAuditBill[i].getIrownumber());
      element.addElement(this.m_vAuditBill[i].getBodyname());
      element.addElement(this.m_vAuditBill[i].getInvCode());
      element.addElement(this.m_vAuditBill[i].getInvname());
      element.addElement(this.m_vAuditBill[i].getBatch());
      element.addElement(this.m_vAuditBill[i].getInvspec());
      element.addElement(this.m_vAuditBill[i].getInvtype());
      element.addElement(this.m_vAuditBill[i].getMeasname());
      element.addElement(this.m_vAuditBill[i].getNnumber());
      element.addElement(this.m_vAuditBill[i].getNprice());
      element.addElement(this.m_vAuditBill[i].getNmoney());
      element.addElement(this.m_vAuditBill[i].getCastunitname());
      element.addElement(this.m_vAuditBill[i].getNassistnum());
      element.addElement(this.m_vAuditBill[i].getOperatorname());

      this.bill_bids[i] = this.m_vAuditBill[i].getCbill_bid();

      this.m_vData.addElement(element.clone());
    }

    getAuditBillTablePanel1().display(this.m_vData);
    getAuditBillTablePanel1().setEditableRowColumn(-99, 14);

    MessageDialog.showHintDlg(this, "", 
      NCLangRes.getInstance().getStrByID("20146010", "UPP20146010-000118"));

    this.m_btnCompute.setEnabled(false);
    this.m_btnPrint.setEnabled(false);
    this.m_btnCompute.setEnabled(false);
    this.m_btnSave.setVisible(true);
    this.m_btnCancel.setVisible(true);
    this.m_btnQuery.setVisible(false);
    setButtons(this.m_aryButtonGroup);
    getIABillCardPanel1().setVisible(false);
    getAuditTablePanel1().setVisible(false);
    getAuditBillTablePanel1().setVisible(true);
    updateButtons();
  }

  public void run()
  {
    long t1 = System.currentTimeMillis();
    showHintMessage(NCLangRes.getInstance()
      .getStrByID("20146010", "UPP20146010-000122"));
    ParamVO vor = null;
    try {
      this.m_btnCompute.setEnabled(false);
      this.m_btnQuery.setEnabled(false);
      this.m_btnBill.setEnabled(false);
      updateButtons();

      this.m_voParam.setMethedId(1);
      if (this.m_vAuditBill != null) {
        int[] ia = getAuditBillTablePanel1().getTable().getSelectedRows();
        String[] bill_bids = new String[ia.length];
        for (int i = 0; i < ia.length; ++i) {
          bill_bids[i] = this.m_vAuditBill[ia[i]].getCbill_bid();
        }
        this.m_voParam.setBill_bids(bill_bids);
      }
      else if (this.m_result != null) {
        this.m_voParam
          .setBill_bids(processUnAuditedBillIDs(this.m_result));
      }

      this.m_vAuditBill = null;
      this.m_result = null;
      vor = AuditBO_Client.audit(this.m_voParam);
      this.m_result = vor.getResult();
    }
    catch (Exception ex) {
      this.progress.setProcessFlag(false);
      ExceptionUITools.showMessage(ex, this);
      return;
    }
    finally {
      this.progress.setProcessFlag(false);
      this.m_btnCompute.setEnabled(false);
      this.m_btnQuery.setEnabled(true);
      updateButtons();
    }
    long t2 = System.currentTimeMillis() - t1;
    process();
    CalcTime c = new CalcTime();

    int size = this.m_result.length;
    int countedItem = 0;
    for (int i = 0; i < size; ++i) {
      countedItem += this.m_result[i].getCountedItem();
    }
    String[] args = new String[2];
    args[0] = String.valueOf(size);
    args[1] = String.valueOf(countedItem);
    String invMessage = NCLangRes.getInstance().getStrByID("20146010", 
      "UPP20146010-000135", null, args);

    StringBuffer buffer = new StringBuffer();
    buffer.append(c.calc(t2));
    buffer.append(invMessage);

    showHintMessage(buffer.toString());

    if (vor != null) {
      String message = (String)vor.getExtraInfo().get("sspzInfo");
      if (message != null)
        showErrorMessage(NCLangRes.getInstance()
          .getStrByID("20146010", "UPP20146010-000123"));
    }
  }

  private void onBtnReturnClicked()
  {
    try
    {
      getAuditBillTablePanel1().setVisible(true);
      getIABillCardPanel1().setVisible(false);

      this.m_btnQuery.setEnabled(true);
      this.m_btnCompute.setEnabled(true);
      this.m_btnPrint.setEnabled(true);
      this.m_btnSave.setVisible(false);
      this.m_btnCancel.setVisible(false);
      this.m_btnBill.setEnabled(true);
      this.m_btnReturn.setVisible(false);
      setTitleText(NCLangRes.getInstance()
        .getStrByID("20146010", 
        "UPT20146010-000001"));
      updateButtons();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private String[] processUnAuditedBillIDs(AuditResultVO[] vos) {
    ArrayList list = new ArrayList();
    int size = vos.length;
    for (int i = 0; i < size; ++i) {
      String[] temp = this.m_result[i].getNotAuditedBillItems();
      if (temp != null) {
        int length = temp.length;
        for (int j = 0; j < length; ++j) {
          if (temp[j] != null) {
            list.add(temp[j]);
          }
        }
      }
    }
    String[] bill_bids = new String[list.size()];
    bill_bids = (String[])list.toArray(bill_bids);
    return bill_bids;
  }

  private QueryDialog getQueryDialog() {
    String s;
    try {
      s = getModuleCode();
    }
    catch (Exception e) {
      s = "20146010";
    }
    QueryDialog dialog = new QueryDialog(this, "301", s);
    dialog.setParent(this);
    return dialog;
  }
}