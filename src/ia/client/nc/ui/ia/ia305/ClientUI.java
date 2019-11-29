package nc.ui.ia.ia305;

import java.awt.CardLayout;
import java.util.Vector;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import nc.ui.ia.ia301.AuditBillVOTranslator;
import nc.ui.ia.ia301.AuditPanel;
import nc.ui.ia.pub.ExceptionUITools;
import nc.ui.ia.pub.IAEnvironment;
import nc.ui.ia.pub.IATableCellRender;
import nc.ui.ia.pub.IATableModel;
import nc.ui.ia.pub.IAUITable;
import nc.ui.ia.pub.RemoteCall;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UITablePane;
import nc.vo.ia.ia301.AuditBillVO;
import nc.vo.ia.ia301.AuditResultVO;
import nc.vo.ia.ia305.CalcTime;
import nc.vo.ia.ia305.ParamVO;
import nc.vo.ia.ia305.ResultVO;
import nc.vo.ia.pub.Log;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pub.smart.ObjectUtils;
import nc.vo.sm.UserVO;

public class ClientUI extends ToftPanel
{
  private static final long serialVersionUID = 8353303820293821204L;
  private UIPanel ivjPnlBott = null;

  private UITablePane ivjTablePnl = null;

  private ButtonObject m_BtnQuery = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000006"), 
    NCLangRes.getInstance().getStrByID("common", "UC001-0000006"), 
    2, "查询");

  private ButtonObject m_BtnUnAudit = new ButtonObject(NCLangRes.getInstance().getStrByID("20146050", 
    "UPT20146050-000001"), 
    NCLangRes.getInstance().getStrByID("20146050", "UPT20146050-000001"), 
    2, "取消成本计算");

  private ButtonObject[] m_aBtnGroup = { 
    this.m_BtnQuery, this.m_BtnUnAudit };

  private IAUITable m_Table = null;

  private IATableModel m_TM = null;

  private Vector m_vColName = null;

  private IAEnvironment ce = null;

  private ParamVO mp = null;

  private ResultVO mvoR = null;

  private int m_iState = 1;

  private int[] m_iaPrec = null;

  private QueryDialog m_dlg = null;

  public ClientUI()
  {
    initialize();
  }

  private UIPanel getPnlBott()
  {
    if (this.ivjPnlBott == null) {
      try {
        this.ivjPnlBott = new UIPanel();
        this.ivjPnlBott.setName("PnlBott");
        this.ivjPnlBott.setLayout(new CardLayout());
        getPnlBott().add(getTablePnl(), getTablePnl().getName());
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjPnlBott;
  }

  private UITablePane getTablePnl()
  {
    if (this.ivjTablePnl == null) {
      try {
        this.ivjTablePnl = new UITablePane();
        this.ivjTablePnl.setName("TablePnl");

        this.m_Table = new IAUITable();
        this.ivjTablePnl.setTable(this.m_Table);
        this.m_Table.setAutoResizeMode(0);
        this.m_Table
          .setSelectionMode(2);
        this.m_TM = new IATableModel();
        this.m_Table.setModel(this.m_TM);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjTablePnl;
  }

  public String getTitle()
  {
    return NCLangRes.getInstance().getStrByID("20146050", 
      "UPT20146050-000001");
  }

  private void handleException(Throwable exception)
  {
    Log.error(exception);
  }

  private void initialize()
  {
    try
    {
      setButtons(this.m_aBtnGroup);

      setName("ClientUI");
      setSize(774, 419);
      add(getPnlBott(), "Center");
    }
    catch (Throwable ivjExc) {
      handleException(ivjExc);
    }

    initSysInfo();
    setData(new Vector());
    this.m_BtnUnAudit.setEnabled(false);
  }

  private QueryDialog getQueryDialog() {
    String s;
    try {
      s = getModuleCode();
    }
    catch (Exception e) {
      s = "20146050";
    }
    QueryDialog dialog = new QueryDialog(this, "305", s);
    dialog.setParent(this);
    return dialog;
  }

  private void initSysInfo()
  {
    try
    {
      this.mp = new ParamVO();
      this.ce = new IAEnvironment();
      this.mp.setCorpID(this.ce.getCorporationID());
      this.mp.setLoginYear(this.ce.getAccountYear());
      this.mp.setLoginMonth(this.ce.getAccountMonth());
      this.mp.setUserID(this.ce.getUser().getPrimaryKey());

      this.m_iaPrec = this.ce.getDataPrecision(this.mp.getCorpID());
      this.mp.setPrec(this.m_iaPrec);
    }
    catch (Exception e) {
      e.printStackTrace();
      return;
    }
  }

  public void onButtonClicked(ButtonObject bo)
  {
    if (bo == this.m_BtnQuery) {
      onQuery();
    }
    else if (bo == this.m_BtnUnAudit)
      onUnAudit();
  }

  private void onQuery()
  {
    if (this.m_dlg == null) {
      this.m_dlg = getQueryDialog();
    }
    this.m_dlg.setLocationRelativeTo(this);
    this.m_dlg.showModal();
    if (!(this.m_dlg.isCloseOK())) {
      return;
    }
    RemoteCall call = new RemoteCall();
    call.execute(this, this, "query", new Object[] { 
      call });
  }

  public void query(RemoteCall call)
  {
    this.mp.unite(this.m_dlg.getCondPnl().getConditionVO());
    try {
      this.mp.setTable(this.m_dlg.getTables());
      this.mp.setConnection(this.m_dlg.getConnections());
      this.mp.setConditions(this.m_dlg.getConditions());
    }
    catch (Exception e) {
      e.printStackTrace();
      showHintMessage(NCLangRes.getInstance()
        .getStrByID("20146050", 
        "UPP20146050-000014"));
      return;
    }
    this.mp.setEndDate(this.ce.getBusinessDate().toString());
    showHintMessage(NCLangRes.getInstance()
      .getStrByID("20146050", 
      "UPP20146050-000015"));
    this.mp.setAuditSeq(null);
    try
    {
      this.mvoR = null;
      this.mvoR = UnAuditBO_Client.queryBill(this.mp);
    }
    catch (Exception ex) {
      ExceptionUITools.showMessage(ex, this, call);
      return;
    }
    AuditBillVO[] voaBill = this.mvoR.getBill();
    this.m_iState = 1;
    if ((voaBill == null) || (voaBill.length == 0)) {
      showHintMessage(NCLangRes.getInstance()
        .getStrByID("20146050", 
        "UPP20146050-000016"));
      setData(new Vector());
      this.mvoR = null;
      return;
    }
    Vector vGrid = new Vector();
    AuditBillVOTranslator.translate(voaBill);
    try
    {
      ObjectUtils.objectReference(voaBill);
    }
    catch (Exception ex) {
      ExceptionUITools.showMessage(ex, this, call);
      return;
    }
    for (int i = 0; i < voaBill.length; ++i) {
      Vector vtmp = new Vector();

      vtmp.addElement(i + 1);
      vtmp.addElement(voaBill[i].getDbilldate().toString());
      vtmp.addElement(voaBill[i].getVbillcode());
      vtmp.addElement(voaBill[i].getBilltypename());
      vtmp.addElement(voaBill[i].getVsourcebillcode());
      vtmp.addElement(voaBill[i].getIrownumber());
      vtmp.addElement(voaBill[i].getBodyname());
      vtmp.addElement(voaBill[i].getInvCode());
      vtmp.addElement(voaBill[i].getInvname());
      vtmp.addElement(voaBill[i].getBatch());
      vtmp.addElement(voaBill[i].getInvspec());
      vtmp.addElement(voaBill[i].getInvtype());
      vtmp.addElement(voaBill[i].getMeasname());
      vtmp.addElement(voaBill[i].getNnumber());
      vtmp.addElement(voaBill[i].getNprice());
      vtmp.addElement(voaBill[i].getNmoney());
      vtmp.addElement(voaBill[i].getVoucher());
      vtmp.addElement(voaBill[i].getCastunitname());
      vtmp.addElement(voaBill[i].getNassistnum());
      vtmp.addElement(voaBill[i].getOperatorname());

      vtmp.addElement(voaBill[i].getCbilltypecode());
      vGrid.addElement(vtmp.clone());
    }
    setData(vGrid);
    this.m_BtnUnAudit.setEnabled(true);
    updateButtons();
    showHintMessage("");
  }

  private void onUnAudit()
  {
    this.mp.unite(this.m_dlg.getCondPnl().getConditionVO());
    int iRow = this.m_Table.getSelectedRow();
    if ((this.mvoR == null) || (this.mvoR.getBill().length == 0)) {
      return;
    }

    Vector v = new Vector();
    int[] ia = this.m_Table.getSelectedRows();
    if (ia.length == 0) {
      String message = NCLangRes.getInstance().getStrByID("20146050", 
        "UPP20146050-000017");

      showWarningMessage(message);
      showHintMessage(message);
      return;
    }
    AuditBillVO vo = null;
    for (int i = 0; i < ia.length; ++i) {
      vo = new AuditBillVO();
      vo.setBodyID(this.mvoR.getBill()[ia[i]].getBodyID());
      vo.setPkInv(this.mvoR.getBill()[ia[i]].getPkInv());
      vo.setBatch(this.mvoR.getBill()[ia[i]].getBatch());
      vo.setCbill_bid(this.mvoR.getBill()[ia[i]].getCbill_bid());
      vo.setDateGetMode(this.mvoR.getBill()[ia[i]].getDateGetMode());
      vo.setPriceMode(this.mvoR.getBill()[ia[i]].getPriceMode());
      vo.setDispatchFlag(this.mvoR.getBill()[ia[i]].getDispatchFlag());
      String message = this.mvoR.getBill()[ia[i]].getVoucher();
      if ((message == null) || (message.equals(""))) {
        v.addElement(vo);
      }
      else {
        String vbillcode = this.mvoR.getBill()[ia[i]].getVbillcode();

        String[] args = new String[2];
        args[0] = vbillcode;
        args[1] = message;
        String tip = NCLangRes.getInstance().getStrByID("20146050", 
          "UPP20146050-000033", null, args);

        showErrorMessage(tip);
        return;
      }
    }
    String sMsg = NCLangRes.getInstance().getStrByID("20146050", 
      "UPP20146050-000018");
    if (showYesNoMessage(sMsg) != 4) {
      return;
    }
    AuditBillVO[] voa = new AuditBillVO[v.size()];
    v.copyInto(voa);
    this.mp.setSelectedBillArray(voa);
    this.mp.setAuditSeq(this.mvoR.getBill()[(this.mvoR.getBill().length - 1)].getAuditSeq());
    this.mp.setInvGroup(this.mvoR.getInvGroup());
    this.mp.setEndDate(this.ce.getBusinessDate().toString());
    this.mp.setLoginDate(this.ce.getBusinessDate().toString());
    showHintMessage(NCLangRes.getInstance()
      .getStrByID("20146050", 
      "UPP20146050-000015"));
    this.m_BtnUnAudit.setEnabled(false);
    updateButtons();

    RemoteCall call = new RemoteCall();
    call.execute(this, this, "unAudit", new Object[] { 
      call });
  }

  public void unAudit(RemoteCall call)
  {
    long t1 = System.currentTimeMillis();

    ResultVO voR = null;
    try
    {
      this.mvoR = null;
      this.m_BtnUnAudit.setEnabled(false);
      voR = UnAuditBO_Client.unAudit(this.mp);
    }
    catch (Exception ex) {
      ExceptionUITools.showMessage(ex, this, call);
      return;
    }
    if (voR == null) {
      call.stopProgress();
      showErrorMessage(NCLangRes.getInstance()
        .getStrByID("20146050", 
        "UPP20146050-000019"));
      showHintMessage(NCLangRes.getInstance()
        .getStrByID("20146050", 
        "UPP20146050-000019"));
      return;
    }

    AuditResultVO[] voaItem = voR.getItem();
    AuditBillVOTranslator.translate(voaItem);

    Vector vResult = new Vector();
    int iCount = voaItem.length;
    Vector vTmp = null;
    for (int i = 0; i < iCount; ++i) {
      vTmp = new Vector();
      vTmp.addElement(voaItem[i].getwhname());
      vTmp.addElement(voaItem[i].getInvCode());
      vTmp.addElement(voaItem[i].getinvname());
      vTmp.addElement(voaItem[i].getsbatch());
      if (voaItem[i].getisOkcompute()) {
        vTmp.addElement(NCLangRes.getInstance()
          .getStrByID("20146050", 
          "UPP20146050-000020"));
        vTmp.addElement(null);
        vTmp.addElement(null);
        vTmp.addElement(null);
        vTmp.addElement(null);
        vTmp.addElement(null);
      }
      else {
        vTmp.addElement(NCLangRes.getInstance()
          .getStrByID("20146050", 
          "UPP20146050-000021"));
        vTmp.addElement(voaItem[i].getReturnMess());
        vTmp.addElement(voaItem[i].getBillTypeName());
        vTmp.addElement(voaItem[i].getBillCode());
        vTmp.addElement(voaItem[i].getRownumber());
        vTmp.addElement(voaItem[i].getSourcebillcode());
        Throwable cause = voaItem[i].getCause();
        if (cause != null) {
          cause.printStackTrace();
        }
      }
      vResult.addElement(vTmp.clone());
    }

    this.m_iState = 2;
    setData(vResult);
    updateButtons();
    CalcTime c = new CalcTime();
    long t2 = System.currentTimeMillis() - t1;

    int size = voaItem.length;
    int countedItem = 0;
    for (int i = 0; i < size; ++i) {
      countedItem += voaItem[i].getCountedItem();
    }
    StringBuffer buffer = new StringBuffer();
    buffer.append(c.calc(t2));

    String[] args = new String[2];
    args[0] = String.valueOf(size);
    args[1] = String.valueOf(countedItem);
    String message = NCLangRes.getInstance().getStrByID("20146050", 
      "UPP20146050-000034", null, args);

    buffer.append(message);

    showHintMessage(buffer.toString());
  }

  private void setColName()
  {
    if (this.m_vColName == null) {
      this.m_vColName = new Vector();
    }
    else {
      this.m_vColName.clear();
    }

    if (this.m_iState == 1) {
      this.m_vColName.addElement(NCLangRes.getInstance()
        .getStrByID("common", "UC000-0001821"));
      this.m_vColName.addElement(NCLangRes.getInstance()
        .getStrByID("common", "UC000-0000799"));
      this.m_vColName.addElement(NCLangRes.getInstance()
        .getStrByID("common", "UC000-0000794"));
      this.m_vColName.addElement(NCLangRes.getInstance()
        .getStrByID("common", "UC000-0000807"));
      this.m_vColName.addElement(NCLangRes.getInstance()
        .getStrByID("common", "UC000-0002750"));
      this.m_vColName.addElement(NCLangRes.getInstance()
        .getStrByID("20146050", "UPP20146050-000022"));
      this.m_vColName.addElement(NCLangRes.getInstance()
        .getStrByID("common", "UC000-0001825"));
      this.m_vColName.addElement(NCLangRes.getInstance()
        .getStrByID("common", "UC000-0001480"));
      this.m_vColName.addElement(NCLangRes.getInstance()
        .getStrByID("common", "UC000-0001453"));
      this.m_vColName.addElement(NCLangRes.getInstance()
        .getStrByID("SCMCOMMON", "UPPSCMCommon-000182"));
      this.m_vColName.addElement(NCLangRes.getInstance()
        .getStrByID("common", "UC000-0003448"));
      this.m_vColName.addElement(NCLangRes.getInstance()
        .getStrByID("common", "UC000-0001240"));
      this.m_vColName.addElement(NCLangRes.getInstance()
        .getStrByID("common", "UC000-0000080"));
      this.m_vColName.addElement(NCLangRes.getInstance()
        .getStrByID("2014", 
        "UPP2014-000649"));
      this.m_vColName.addElement(NCLangRes.getInstance()
        .getStrByID("common", "UC000-0000741"));
      this.m_vColName.addElement(NCLangRes.getInstance()
        .getStrByID("common", "UC000-0004112"));
      this.m_vColName.addElement(NCLangRes.getInstance()
        .getStrByID("20146050", "UPP20146050-000023"));
      this.m_vColName.addElement(NCLangRes.getInstance()
        .getStrByID("common", "UC000-0003975"));
      this.m_vColName.addElement(NCLangRes.getInstance()
        .getStrByID("20146050", "UPP20146050-000024"));
      this.m_vColName.addElement(NCLangRes.getInstance()
        .getStrByID("common", "UC000-0002188"));

      this.m_vColName.addElement("来源单据类型(开发)");
    }
    else if (this.m_iState == 2) {
      this.m_vColName.addElement(NCLangRes.getInstance()
        .getStrByID("common", "UC000-0001825"));
      this.m_vColName.addElement(NCLangRes.getInstance()
        .getStrByID("common", "UC000-0001480"));
      this.m_vColName.addElement(NCLangRes.getInstance()
        .getStrByID("common", "UC000-0001453"));
      this.m_vColName.addElement(NCLangRes.getInstance()
        .getStrByID("SCMCOMMON", "UPPSCMCommon-000182"));
      this.m_vColName.addElement(NCLangRes.getInstance()
        .getStrByID("20146050", "UPP20146050-000025"));
      this.m_vColName.addElement(NCLangRes.getInstance()
        .getStrByID("20146050", "UPP20146050-000026"));
      this.m_vColName.addElement(NCLangRes.getInstance()
        .getStrByID("20146050", "UPP20146050-000027"));
      this.m_vColName.addElement(NCLangRes.getInstance()
        .getStrByID("20146050", "UPP20146050-000028"));
      this.m_vColName.addElement(NCLangRes.getInstance()
        .getStrByID("20146050", "UPP20146050-000029"));
      this.m_vColName.addElement(NCLangRes.getInstance()
        .getStrByID("20146050", "UPP20146050-000030"));
    }
  }

  private void setColPrec(String sColName, int iPrec)
  {
    for (int i = 0; i < this.m_TM.getRowCount(); ++i) {
      int iColumnIndex = this.m_Table.getColumnModel().getColumnIndex(sColName);
      Object oTemp = this.m_TM.getValueAt(i, iColumnIndex);
      String sTemp = "";
      if ((oTemp != null) && (oTemp.toString().length() != 0)) {
        UFDouble dTemp = new UFDouble(oTemp.toString());
        UFDouble d = dTemp.setScale(iPrec, 4);
        sTemp = d.toString();
      }
      this.m_TM.setValueAt(sTemp, i, iColumnIndex);
    }
  }

  private void setData(Vector value)
  {
    setColName();
    this.m_TM.setDataVector(value, this.m_vColName);
    int[] iaWidth;
    if (this.m_iState == 1) {
      iaWidth = new int[] { 
        30, 75, 115, 90, 100, 75, 95, 85, 85, 50, 75, 75, 75, 80, 80, 80, 
        110, 75, 85, 70, 
        100, 110 };

      this.m_Table.setColumnWidth(iaWidth);

      String[] sa = { 
        NCLangRes.getInstance().getStrByID("2014", "UPP2014-000649"), 
        NCLangRes.getInstance()
        .getStrByID("common", "UC000-0000741"), 
        NCLangRes.getInstance()
        .getStrByID("common", "UC000-0004112"), 
        NCLangRes.getInstance().getStrByID("20146050", 
        "UPP20146050-000024") };

      int[] iaPrec = { 
        this.m_iaPrec[0], this.m_iaPrec[1], this.m_iaPrec[2], this.m_iaPrec[4] };

      for (int i = 0; i < 4; ++i) {
        int iColindex = this.m_Table.getColumnModel().getColumnIndex(sa[i]);
        IATableCellRender tcr = new IATableCellRender();
        tcr.setAlignMode(IATableCellRender.ALIGN_RIGHT);
        this.m_Table.getColumnModel().getColumn(iColindex).setCellRenderer(tcr);
        setColPrec(sa[i], -iaPrec[i]);
      }
    }
    else if (this.m_iState == 2) {
      iaWidth = new int[] { 
        85, 85, 85, 50, 85, 110, 90, 115, 120, 120 };

      this.m_Table.setColumnWidth(iaWidth);
    }
  }
}