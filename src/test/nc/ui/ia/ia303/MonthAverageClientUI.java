package nc.ui.ia.ia303;

import java.awt.CardLayout;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.event.ChangeEvent;
import nc.ui.ia.ia401.EndhandleBO_Client;
import nc.ui.ia.ia401.FormulaBO_Client;
import nc.ui.ia.pub.CommonDataBO_Client;
import nc.ui.ia.pub.ExceptionUITools;
import nc.ui.ia.pub.IAEnvironment;
import nc.ui.ia.pub.IAUITable;
import nc.ui.ia.pub.RemoteCall;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.layout.TableLayout;
import nc.ui.pub.print.PrintDirectEntry;
import nc.ui.scm.pub.billutil.ClientCacheHelper;
import nc.vo.ia.ia303.MonthAverVO;
import nc.vo.ia.ia401.FormulaVO;
import nc.vo.ia.pub.ExceptionUtils;
import nc.vo.ia.pub.Log;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pub.smart.ObjectUtils;
import nc.vo.sm.UserVO;

public class MonthAverageClientUI extends ToftPanel
{
  private static final long serialVersionUID = -8535770470982137828L;
  private IAEnvironment my_environment = null;

  private String m_sCorp = null;

  private String m_sCorpName = null;

  private String m_sUser = null;

  private String m_sUserName = null;

  private UFDate m_dLogindata = null;

  private String m_sAccountMonth = null;

  private String m_sAccountYear = null;

  private String m_sUnClosePeriod = null;

  private String m_sStartPeriod = null;

  private boolean m_bIsDispNegaPrice = true;

  private String[] m_sConditions = null;

  private MonthAverVO[] dispdata = null;

  private ButtonObject m_boOkButton = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000044"), NCLangRes.getInstance().getStrByID("20146030", "UPP20146030-000035"), 2, "确定");

  private ButtonObject m_boReturnButton = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000006"), NCLangRes.getInstance().getStrByID("20146030", "UPP20146030-000036"), 2, "查询");

  private ButtonObject m_boSaveButton = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000001"), NCLangRes.getInstance().getStrByID("20146030", "UPP20146030-000037"), 2, "保存");

  private ButtonObject m_boPrintButton = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000007"), NCLangRes.getInstance().getStrByID("20146030", "UPP20146030-000038"), 2, "打印");

  private ButtonObject m_boFilterButton = new ButtonObject(NCLangRes.getInstance().getStrByID("20146030", "UPT20146030-000001"), NCLangRes.getInstance().getStrByID("20146030", "UPP20146030-000039"), 2, "过滤");

  private ButtonObject[] m_aryButtonGroup = { this.m_boReturnButton, this.m_boOkButton, this.m_boSaveButton, this.m_boFilterButton, this.m_boPrintButton };

  private MonthAverPanel ivjMonthAverPanel1 = null;

  private MonthResuPanel ivjMonthResuPanel1 = null;

  private MonAverResDlg m_monAverResDlg = null;

  public MonthAverageClientUI()
  {
    initialize();
  }

  public boolean conditionsCheck()
  {
    if ((this.m_sConditions[0] == null) || (this.m_sConditions[0].length() == 0)) {
      showErrorMessage(NCLangRes.getInstance().getStrByID("20146030", "UPP20146030-000040"));

      return false;
    }
    if (this.m_sUnClosePeriod.equals("00")) {
      String message = NCLangRes.getInstance().getStrByID("20146010", "UPP20146010-000051");

      showErrorMessage(message);
      return false;
    }
    if (this.m_sConditions[0].compareTo(this.m_sUnClosePeriod) > 0) {
      String[] args = new String[1];
      args[0] = this.m_sUnClosePeriod;
      String message = NCLangRes.getInstance().getStrByID("20146030", "UPP20146030-000054", null, args);

      showErrorMessage(message);
      return false;
    }
    if (this.m_sConditions[0].compareTo(this.m_sAccountYear + "-" + this.m_sAccountMonth) > 0) {
      String[] args = new String[1];
      args[0] = (this.m_sAccountYear + "-" + this.m_sAccountMonth);
      String message = NCLangRes.getInstance().getStrByID("20146030", "UPP20146030-000055", null, args);

      showErrorMessage(message);
      return false;
    }
    if (this.m_sConditions[0].compareTo(this.m_sStartPeriod) < 0) {
      String[] args = new String[1];
      args[0] = this.m_sStartPeriod;
      String message = NCLangRes.getInstance().getStrByID("20146030", "UPP20146030-000056", null, args);

      showErrorMessage(message);
      return false;
    }

    return true;
  }

  private boolean datacheck()
  {
    Vector data = getMonthResuPanel1().data;
    String temp = null;

    int count = data.size();
    for (int i = 0; i < count; i++) {
      if (((Vector)data.elementAt(i)).elementAt(6) == null) {
        String[] args = new String[1];
        args[0] = String.valueOf(i + 1);
        String message = NCLangRes.getInstance().getStrByID("20146030", "UPP20146030-000057", null, args);

        showHintMessage(message);
        getMonthResuPanel1().scrollToRow(i);
        return false;
      }

      temp = ((Vector)data.elementAt(i)).elementAt(6).toString();
      if (temp.length() == 0) {
        String[] args = new String[1];
        args[0] = String.valueOf(i + 1);
        String message = NCLangRes.getInstance().getStrByID("20146030", "UPP20146030-000057", null, args);

        showHintMessage(message);
        getMonthResuPanel1().scrollToRow(i);
        return false;
      }

      UFDouble dValue = new UFDouble(temp);
      if (dValue.doubleValue() > 1000000000000.0D) {
        String[] args = new String[1];
        args[0] = String.valueOf(i + 1);
        String message = NCLangRes.getInstance().getStrByID("20146030", "UPP20146030-000058", null, args);

        showHintMessage(message);
        getMonthResuPanel1().scrollToRow(i);
        return false;
      }
      if (dValue.doubleValue() < 0.0D) {
        String[] args = new String[1];
        args[0] = String.valueOf(i + 1);
        String message = NCLangRes.getInstance().getStrByID("20146030", "UPP20146030-000059", null, args);

        showHintMessage(message);
        getMonthResuPanel1().scrollToRow(i);
        return false;
      }
    }

    return true;
  }

  private String getFormula()
  {
    FormulaVO[] m_voFormula = null;
    try
    {
      m_voFormula = FormulaBO_Client.queryFomulaNames(this.m_sCorp, "N");
    }
    catch (Exception e)
    {
      Log.error(e);
      showHintMessage(NCLangRes.getInstance().getStrByID("20146030", "UPP20146030-000041"));

      return NCLangRes.getInstance().getStrByID("20146030", "UPP20146030-000041");
    }

    Hashtable ht = new Hashtable();
    String sFormulaName = NCLangRes.getInstance().getStrByID("20146030", "UPP20146030-000042");

    String m_sFormulaCode = new String();

    for (int i = 0; i < m_voFormula.length; i++) {
      Integer iCode = m_voFormula[i].getIformulafieldflag();
      String sName = m_voFormula[i].getVformulafieldname();
      ht.put(iCode, sName);

      if (iCode.intValue() == -1) {
        m_sFormulaCode = sName;
      }

    }

    for (int i = 0; i < m_sFormulaCode.length(); i++) {
      String sChar = m_sFormulaCode.substring(i, i + 1);
      try
      {
        Integer iIndex = new Integer(sChar);
        String sName = ht.get(iIndex).toString();
        sFormulaName = sFormulaName + sName;
      }
      catch (Exception e)
      {
        sFormulaName = sFormulaName + sChar;
      }
    }

    return sFormulaName;
  }

  private MonthAverPanel getMonthAverPanel1()
  {
    if (this.ivjMonthAverPanel1 == null) {
      try {
        this.ivjMonthAverPanel1 = new MonthAverPanel();
        this.ivjMonthAverPanel1.setName("MonthAverPanel1");
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjMonthAverPanel1;
  }

  private MonthResuPanel getMonthResuPanel1()
  {
    if (this.ivjMonthResuPanel1 == null) {
      try {
        this.ivjMonthResuPanel1 = new MonthResuPanel();
        this.ivjMonthResuPanel1.setName("MonthResuPanel1");
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjMonthResuPanel1;
  }

  public String getTitle()
  {
    return NCLangRes.getInstance().getStrByID("20146030", "UPP20146030-000020");
  }

  private void handleException(Throwable exception)
  {
    Log.error(exception);
  }

  public void init()
  {
    getMonthResuPanel1().setVisible(false);

    this.my_environment = new IAEnvironment();
    this.m_sCorp = this.my_environment.getCorporationID();
    this.m_sCorpName = this.my_environment.getCorporationName();
    this.m_sUser = this.my_environment.getUser().getPrimaryKey();
    this.m_sUserName = this.my_environment.getUser().getUserName();
    this.m_dLogindata = this.my_environment.getBusinessDate();
    this.m_sAccountMonth = this.my_environment.getAccountMonth();
    this.m_sAccountYear = this.my_environment.getAccountYear();
    try {
      this.m_sStartPeriod = CommonDataBO_Client.getStartPeriod(this.m_sCorp);
    }
    catch (Exception e) {
      Log.error(e);
      showErrorMessage(NCLangRes.getInstance().getStrByID("20146030", "UPP20146030-000043"));

      return;
    }
    try
    {
      this.m_sUnClosePeriod = CommonDataBO_Client.getUnClosePeriod(this.m_sCorp);
    }
    catch (Exception e) {
      Log.error(e);
      showErrorMessage(NCLangRes.getInstance().getStrByID("20146030", "UPP20146030-000044"));

      return;
    }
    try
    {
      this.m_bIsDispNegaPrice = CommonDataBO_Client.isDispNegaPrice(this.m_sCorp).booleanValue();
    }
    catch (Exception e)
    {
      Log.error(e);
      ((MonthAverageClientUI)getParent()).showHintMessage(NCLangRes.getInstance().getStrByID("20146030", "UPP20146030-000045"));

      return;
    }

    getMonthResuPanel1().setcorp(this.m_sCorp, this.m_bIsDispNegaPrice);
    getMonthAverPanel1().setaccount(this.m_sAccountYear + "-" + this.m_sAccountMonth);
  }

  private void initialize()
  {
    try
    {
      setName("MonthAverageClientUI");

      setSize(774, 419);

      double[][] size = { { -1.0D }, { -1.0D } };

      TableLayout layout = new TableLayout(size);
      setLayout(layout);
      add(getMonthAverPanel1(), "0,0");
      add(getMonthResuPanel1(), "0,0");
    }
    catch (Throwable ivjExc)
    {
      handleException(ivjExc);
    }

    this.m_boReturnButton.setEnabled(false);
    this.m_boSaveButton.setEnabled(false);
    this.m_boPrintButton.setEnabled(false);
    this.m_boFilterButton.setEnabled(false);
    setButtons(this.m_aryButtonGroup);

    init();
    setLayout(new CardLayout());
  }

  public void onButtonClicked(ButtonObject bo)
  {
    RemoteCall call = new RemoteCall();
    if (getMonthResuPanel1().getUItable().isEditing()) {
      getMonthResuPanel1().getUItable().editingStopped(new ChangeEvent(getMonthResuPanel1()));
    }

    if (bo == this.m_boOkButton)
    {
      onOkButtonClicked();
    }
    else if (bo == this.m_boReturnButton) {
      onReturnButtonClicked();
    }
    else if (bo == this.m_boSaveButton) {
      call.execute(this, this, "onSaveButtonClicked", new Object[] { call });
    }
    else if (bo == this.m_boPrintButton) {
      onPrintButtonClicked();
    }
    else if (bo == this.m_boFilterButton)
      onFilterButtonClicked();
  }

  public void onFilterButtonClicked()
  {
    if (this.m_monAverResDlg == null) {
      this.m_monAverResDlg = new MonAverResDlg(this);
      this.m_monAverResDlg.setModal(false);
      this.m_monAverResDlg.setLocationRelativeTo(this);
    }
    if (this.m_monAverResDlg.isVisible()) {
      this.m_monAverResDlg.setVisible(false);
    }

    MonAverFilConDlg monAverFilConDlg = new MonAverFilConDlg(this);
    MonthAverVO[] datas = getMonthResuPanel1().getData();

    monAverFilConDlg.setDispData(datas);
    int ret = monAverFilConDlg.showModal();
    if (ret == 1)
      try
      {
        datas = monAverFilConDlg.getDispData();
        UFBoolean isEdit = new UFBoolean(false);
        this.m_monAverResDlg.getUIMonthResuPanel().setcorp(this.m_sCorp, this.m_bIsDispNegaPrice);

        this.m_monAverResDlg.getUIMonthResuPanel().setdata(datas, isEdit);

        this.m_monAverResDlg.show();
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
  }

  public void onOkButtonClicked()
  {
    showHintMessage("");

    this.m_sConditions = getMonthAverPanel1().getcondition();

    if (!conditionsCheck()) {
      return;
    }

    String[] systemp = new String[15];
    systemp[0] = this.m_sCorp;
    systemp[1] = this.m_dLogindata.toString();
    systemp[2] = this.m_sUser;
    systemp[3] = this.m_sAccountYear;
    systemp[4] = this.m_sAccountMonth;
    try
    {
      this.dispdata = MonthAverageBO_Client.avCost(systemp, this.m_sConditions);
    }
    catch (Exception e) {
      ExceptionUITools.showMessage(e, this);
      return;
    }

    if ((this.dispdata == null) || (this.dispdata.length == 0)) {
      showHintMessage(NCLangRes.getInstance().getStrByID("20146030", "UPP20146030-000047"));

      return;
    }

    translate(this.dispdata);
    String[] args = new String[1];
    args[0] = String.valueOf(this.dispdata.length);
    String message = NCLangRes.getInstance().getStrByID("20146030", "UPP20146030-000060", null, args);

    showHintMessage(message);

    getMonthAverPanel1().setVisible(false);
    getMonthResuPanel1().setVisible(true);
    getMonthResuPanel1().getUILabel4().setText(this.m_sConditions[0]);

    getMonthResuPanel1().getUILabel6().setText(getFormula());
    getMonthResuPanel1().getUILabel6().setToolTipText(getFormula());

    UFBoolean is_Edit = null;
    if ((this.m_sConditions[0].compareTo(this.m_sUnClosePeriod) < 0) || (!this.m_sConditions[0].equals(this.m_sAccountYear + "-" + this.m_sAccountMonth)))
    {
      is_Edit = new UFBoolean(false);
    }
    else {
      is_Edit = new UFBoolean(true);
    }

    getMonthResuPanel1().setdata(this.dispdata, is_Edit);

    this.m_boOkButton.setEnabled(false);
    this.m_boReturnButton.setEnabled(true);
    this.m_boFilterButton.setEnabled(true);
    if (is_Edit.booleanValue()) {
      this.m_boSaveButton.setEnabled(true);
    }
    this.m_boPrintButton.setEnabled(true);
    setButtons(this.m_aryButtonGroup);

    for (int i = 0; i < this.dispdata.length; i++)
      if (this.dispdata[i].getNmonthprice() == null) {
        getMonthResuPanel1().getUItable().setRowSelectionInterval(i, i);
        getMonthResuPanel1().getUItable().scrollRectToVisible(getMonthResuPanel1().getUItable().getCellRect(i, 0, false));

        showErrorMessage(NCLangRes.getInstance().getStrByID("20146030", "UPP20146030-000048"));

        break;
      }
  }

  public void onPrintButtonClicked()
  {
    String[][] colname = { { NCLangRes.getInstance().getStrByID("common", "UC000-0001831"), NCLangRes.getInstance().getStrByID("common", "UC000-0001828"), NCLangRes.getInstance().getStrByID("common", "UC000-0001480"), NCLangRes.getInstance().getStrByID("common", "UC000-0001453"), NCLangRes.getInstance().getStrByID("common", "UC000-0003448"), NCLangRes.getInstance().getStrByID("common", "UC000-0001240"), NCLangRes.getInstance().getStrByID("20146030", "UPP20146030-000013"), NCLangRes.getInstance().getStrByID("common", "UC000-0000958"), NCLangRes.getInstance().getStrByID("common", "UC000-0001704"), NCLangRes.getInstance().getStrByID("20146030", "UPP20146030-000014"), NCLangRes.getInstance().getStrByID("20146030", "UPP20146030-000015"), NCLangRes.getInstance().getStrByID("20146030", "UPP20146030-000016"), NCLangRes.getInstance().getStrByID("20146030", "UPP20146030-000017"), NCLangRes.getInstance().getStrByID("20146030", "UPP20146030-000018"), NCLangRes.getInstance().getStrByID("20146030", "UPP20146030-000019") } };

    Object[][] printdata = new Object[this.dispdata.length][15];
    Vector v = null;
    int iLen = this.dispdata.length;
    for (int i = 0; i < iLen; i++) {
      v = (Vector)getMonthResuPanel1().data.elementAt(i);
      printdata[i][0] = v.elementAt(0);
      printdata[i][1] = v.elementAt(1);
      printdata[i][2] = v.elementAt(2);
      printdata[i][3] = v.elementAt(3);
      printdata[i][4] = v.elementAt(4);
      printdata[i][5] = v.elementAt(5);
      printdata[i][6] = v.elementAt(6);
      printdata[i][7] = v.elementAt(7);
      printdata[i][8] = v.elementAt(8);
      printdata[i][9] = v.elementAt(9);
      printdata[i][10] = v.elementAt(10);
      printdata[i][11] = v.elementAt(11);
      printdata[i][12] = v.elementAt(12);
      printdata[i][13] = v.elementAt(13);
      printdata[i][14] = v.elementAt(14);
    }
    String title = NCLangRes.getInstance().getStrByID("20146030", "UPP20146030-000020");

    String period = getMonthAverPanel1().getcondition()[0];
    String[] args = new String[3];
    args[0] = this.m_sCorpName;
    args[1] = this.m_sAccountYear;
    args[2] = this.m_sAccountMonth;
    if (period != null) {
      args[1] = period.substring(0, 4);
      args[2] = period.substring(5, 7);
    }

    String topstr = NCLangRes.getInstance().getStrByID("20146030", "UPP20146030-000052", null, args);

    args = new String[2];
    args[0] = this.m_sUserName;
    args[1] = this.m_dLogindata.toString();
    String botstr = NCLangRes.getInstance().getStrByID("20146030", "UPP20146030-000053", null, args);

    int[] colwidths = { 90, 80, 80, 90, 90, 90, 90, 85, 85, 85, 85, 85, 85, 85, 85 };

    int[] flag = { 0, 0, 0, 0, 0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2 };

    PrintDirectEntry print = new PrintDirectEntry();
    print.setFixedRows(1);
    print.setTitle(title);
    print.setTopStr(topstr);
    print.setBottomStr(botstr);
    print.setColWidth(colwidths);
    print.setAlignFlag(flag);
    print.setColNames(colname);
    print.setData(printdata);
    print.preview();
  }

  public void onReturnButtonClicked()
  {
    showHintMessage("");
    getMonthResuPanel1().setVisible(false);
    getMonthAverPanel1().setVisible(true);

    this.m_boOkButton.setEnabled(true);
    this.m_boReturnButton.setEnabled(false);
    this.m_boSaveButton.setEnabled(false);
    this.m_boPrintButton.setEnabled(false);
    this.m_boFilterButton.setEnabled(false);
    setButtons(this.m_aryButtonGroup);

    getMonthResuPanel1().stopEdit();
  }

  public void onSaveButtonClicked(RemoteCall call)
  {
    if (getMonthResuPanel1().iserror()) {
      return;
    }

    if (!datacheck()) {
      return;
    }

    showHintMessage(NCLangRes.getInstance().getStrByID("20146030", "UPP20146030-000049"));

    String[] sysinfo = new String[10];
    sysinfo[0] = this.m_sCorp;
    sysinfo[1] = this.m_sAccountYear;
    sysinfo[2] = this.m_sAccountMonth;
    sysinfo[3] = this.m_sUser;
    sysinfo[4] = this.m_dLogindata.toString();
    sysinfo[5] = "IA";
    sysinfo[6] = "IE";

    long t1 = System.currentTimeMillis();

    Vector data = getMonthResuPanel1().data;
    for (int i = 0; i < this.dispdata.length; i++) {
      this.dispdata[i].setNmonthprice(new UFDouble(((Vector)data.elementAt(i)).elementAt(6).toString().trim()));

      if ((((Vector)data.elementAt(i)).elementAt(8) != null) && (((Vector)data.elementAt(i)).elementAt(8).toString().length() != 0))
      {
        this.dispdata[i].setNdifprice(new UFDouble(((Vector)data.elementAt(i)).elementAt(8).toString().trim()));
      }
      else
      {
        this.dispdata[i].setNdifprice(null);
      }
    }

    try
    {
      ObjectUtils.objectReference(this.dispdata);

      if ((this.dispdata != null) && (this.dispdata.length != 0))
      {
        String message = EndhandleBO_Client.endproc(sysinfo, null, this.dispdata);
        if (message.length() > 0)
          showWarningMessage(message);
      }
    }
    catch (Exception ex)
    {
      ExceptionUtils.wrappException(ex);
    }

    long t2 = System.currentTimeMillis() - t1;
    int iHour = (int)(t2 / 3600000L);
    int iMinutes = (int)((t2 - iHour * 3600000) / 60000L);
    int iSecond = (int)((t2 - iHour * 3600000 - iMinutes * 60000) / 1000L);
    int iOther = (int)(t2 - iHour * 3600000 - iMinutes * 60000 - iSecond * 1000);
    String[] args = new String[4];
    args[0] = String.valueOf(iHour);
    args[1] = String.valueOf(iMinutes);
    args[2] = String.valueOf(iSecond);
    args[3] = String.valueOf(iOther);
    String message = NCLangRes.getInstance().getStrByID("20146030", "UPP20146030-000061", null, args);

    showHintMessage(message);

    this.m_boSaveButton.setEnabled(false);
    setButtons(this.m_aryButtonGroup);
    getMonthResuPanel1().stopEdit();
  }

  private void translate(MonthAverVO[] vos)
  {
    String[] targetitemkey = new String[2];
    String[] field = new String[2];

    targetitemkey[0] = "vrdcentername";
    targetitemkey[1] = "vrdcentercode";
    field[0] = "bodyname";
    field[1] = "bodycode";
    ClientCacheHelper.getColValue(vos, targetitemkey, "bd_calbody", "pk_calbody", field, "crdcenterid");

    targetitemkey = new String[1];
    field = new String[1];
    targetitemkey[0] = "vinventorycode";
    field[0] = "pk_invbasdoc";
    ClientCacheHelper.getColValue(vos, targetitemkey, "bd_invmandoc", "pk_invmandoc", field, "cinventoryid");

    targetitemkey = new String[4];
    field = new String[4];

    targetitemkey[0] = "vinventorycode";
    targetitemkey[1] = "vinventoryname";
    targetitemkey[2] = "vinventoryspec";
    targetitemkey[3] = "vinventorytype";

    field[0] = "invcode";
    field[1] = "invname";
    field[2] = "invspec";
    field[3] = "invtype";
    ClientCacheHelper.getColValue(vos, targetitemkey, "bd_invbasdoc", "pk_invbasdoc", field, "vinventorycode");
  }
}