package nc.ui.ic.ic281;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.TableColumnModel;
import nc.ui.ic.ic001.BatchCodeDefSetTool;
import nc.ui.ic.pub.bill.GeneralBillUICtl;
import nc.ui.ic.pub.bill.query.QueryConditionDlgForBill;
import nc.ui.ic.pub.freeze.FreezeHelper;
import nc.ui.ic.pub.report.ICReportHelper;
import nc.ui.ic.pub.scale.ScaleInit;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.print.PrintEntry;
import nc.ui.scm.pub.billutil.ClientCacheHelper;
import nc.vo.bd.CorpVO;
import nc.vo.ic.pub.GenMethod;
import nc.vo.ic.pub.ScaleKey;
import nc.vo.ic.pub.ScaleValue;
import nc.vo.ic.pub.bill.QryConditionVO;
import nc.vo.ic.pub.freeze.FreezeBillVO;
import nc.vo.ic.pub.freeze.FreezeVO;
import nc.vo.ic.pub.lang.ResBase;
import nc.vo.pub.bill.BillTempletBodyVO;
import nc.vo.pub.bill.BillTempletHeadVO;
import nc.vo.pub.bill.BillTempletVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.query.RefResultVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.ctrl.GenMsgCtrl;
import nc.vo.sm.UserVO;

public class ClientUI extends ToftPanel
  implements BillEditListener, ListSelectionListener, TableColumnModelListener
{
  private BillCardPanel ivjBillCardPanel = null;

  private ButtonObject m_boFreezeQry = new ButtonObject(NCLangRes.getInstance().getStrByID("4008spec", "UPT400824-000013"), NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000515"), 0, "可冻结量查询");

  private ButtonObject m_boFreeze = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000030"), NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000516"), 0, "冻结");

  private ButtonObject m_boUnfreezeQry = new ButtonObject(NCLangRes.getInstance().getStrByID("4008spec", "UPT400824-000015"), NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000517"), 0, "可解冻量查询");

  private ButtonObject m_boUnfreeze = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000031"), NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000518"), 0, "解冻");

  private ButtonObject m_boSelectAll = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000041"), NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000519"), 0, "全选");

  private ButtonObject m_boSelectNone = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000042"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000401"), 0, "全消");

  private ButtonObject m_boHistoryQry = new ButtonObject(NCLangRes.getInstance().getStrByID("4008spec", "UPT400824-000014"), NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000520"), 0, "历史记录查询");

  protected ButtonObject m_boPrint = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000007"), NCLangRes.getInstance().getStrByID("common", "UC001-0000007"), 0, "打印");

  protected ButtonObject m_boPreview = new ButtonObject(NCLangRes.getInstance().getStrByID("4008spec", "UPPSCMCommon-000305"), NCLangRes.getInstance().getStrByID("4008spec", "UPPSCMCommon-000305"), 0, "预览");

  private ButtonObject[] m_aryButtonGroup = { this.m_boFreezeQry, this.m_boFreeze, this.m_boUnfreezeQry, this.m_boUnfreeze, this.m_boSelectAll, this.m_boSelectNone, this.m_boHistoryQry, this.m_boPrint, this.m_boPreview };

  private String m_sCorpID = null;

  private String m_sCorpName = null;

  private String m_sUserID = null;

  private String m_sLogDate = null;

  private String m_sWhID = null;

  private final int FREEZE = 0;

  private final int UNFREEZE = 1;

  private final int HISTORY = 2;

  private int m_iCurStatus = 0;

  private int m_iCurRowCount = 0;

  private QueryConditionDlg ivjQueryConditionDlg = null;

  private QueryConditionDlg ivjfreCondDlg = null;

  private QueryConditionDlg ivjunfreCondDlg = null;

  private QueryConditionDlg ivjhisCondDlg = null;

  private final String m_sNumItemKey = "nfreezenum";

  private final String m_sAssNumItemKey = "nfreezeastnum";

  private final String m_sUnfreezeNumKey = "ndefrznum";

  private final String m_sUnfreezeastNumKey = "ndefrzastnum";

  private final String m_sGrossNumItemKey = "ngrossnum";

  private final String m_sBillTypeCode = "4Z";

  String m_sCurrentBillNode = "400824";

  protected ScaleValue m_ScaleValue = new ScaleValue();
  protected ScaleKey m_ScaleKey;
  protected ArrayList m_alUserCorpID = null;

  PrintDataInterface m_dataSource = null;

  PrintEntry m_print = null;

  protected final String m_sBillvoName = "nc.vo.ic.pub.freeze.FreezeBillVO";

  protected final String m_sBillHeadvoName = "nc.vo.ic.pub.freeze.FreezeVO";

  protected final String m_sBillItemvoName = "nc.vo.ic.pub.freeze.FreezeVO";

  public FreezeBillVO m_sBillvo = null;

  public ClientUI()
  {
    initialize();
  }

  public void afterEdit(BillEditEvent e)
  {
    if ("nfreezenum".equals(e.getKey()))
      afterNumEdit(e, "freeze");
    else if ("nfreezeastnum".equals(e.getKey()))
    {
      afterNumEdit(e, "freeze");
    } else if ("ngrossnum".equals(e.getKey()))
      afterGrossNumEdit(e);
    else if ("ndefrznum".equals(e.getKey()))
      afterNumEdit(e, "unfreeze");
    else if ("ndefrzastnum".equals(e.getKey()))
      afterNumEdit(e, "unfreeze");
  }

  public void afterNumEdit(BillEditEvent e, String sOperType)
  {
    int irow = e.getRow();

    Integer[] iaCond = new Integer[2];

    String sTempFieldName = null;

    Object oTemp = null;

    String[] saParam = null;

    for (int i = 0; i < iaCond.length; i++) {
      switch (i) {
      case 0:
        sTempFieldName = "isstorebyconvert";
        break;
      case 1:
        sTempFieldName = "issolidconvrate";
      }

      oTemp = getBillCardPanel().getBodyValueAt(irow, sTempFieldName);

      if ((oTemp != null) && (oTemp.toString().trim().length() > 0)) {
        if ((oTemp instanceof Boolean)) {
          if (((Boolean)oTemp).booleanValue()) {
            iaCond[i] = new Integer(1);
          }
          else {
            iaCond[i] = new Integer(0);
          }
        }
        else {
          iaCond[i] = new Integer(oTemp.toString());
        }
      }

    }

    if (sOperType.equals("freeze")) {
      saParam = new String[] { "castunitname", "hsl", "nfreezeastnum", "nfreezenum" };
    }
    else if (sOperType.equals("unfreeze")) {
      saParam = new String[] { "castunitname", "hsl", "ndefrzastnum", "ndefrznum" };
    }

    if ((saParam != null) && (((iaCond[0] != null) && (iaCond[0].intValue() == 1)) || ((iaCond[1] != null) && (iaCond[1].intValue() == 1))))
    {
      GeneralBillUICtl.changeNum(getBillCardPanel(), e.getKey(), irow, saParam, true);
    }
  }

  public void bodyRowChange(BillEditEvent e)
  {
  }

  protected void clearUi()
  {
    try
    {
      this.m_iCurRowCount = -1;
      this.m_sBillvo = null;
      FreezeBillVO voNullBill = new FreezeBillVO();
      FreezeVO head = new FreezeVO();
      FreezeVO[] item = new FreezeVO[1];
      item[0] = new FreezeVO();
      voNullBill.setParentVO(head);
      voNullBill.setChildrenVO(item);

      getBillCardPanel().setBillValueVO(voNullBill);
      getBillCardPanel().getBillModel().clearBodyData();
    }
    catch (Exception e)
    {
    }
  }

  protected void colEditableSet(String sItemKey, int iRow)
  {
    boolean bRow = false;
    bRow = (iRow >= 0) && (iRow < getBillCardPanel().getRowCount());
    if ((sItemKey.equals("nfreezeastnum")) && (bRow)) {
      if ((getBillCardPanel().getBodyValueAt(iRow, "nonhandastnum") != null) && (getBillCardPanel().getBodyValueAt(iRow, "nonhandastnum").toString().trim().length() > 0) && (this.m_iCurStatus == 0))
      {
        getBillCardPanel().getBillData().getBodyItem(sItemKey).setEnabled(true);
      }
      else getBillCardPanel().getBillData().getBodyItem(sItemKey).setEnabled(false);

    }
    else if ((bRow) && (sItemKey.equals("ngrossnum")))
      if ((getBillCardPanel().getBodyValueAt(iRow, "ngross") != null) && (getBillCardPanel().getBodyValueAt(iRow, "ngross").toString().trim().length() > 0) && (this.m_iCurStatus == 0))
      {
        getBillCardPanel().getBillData().getBodyItem(sItemKey).setEnabled(true);
      }
      else getBillCardPanel().getBillData().getBodyItem(sItemKey).setEnabled(false);
  }

  public void columnAdded(TableColumnModelEvent e)
  {
  }

  public void columnMarginChanged(ChangeEvent e)
  {
  }

  public void columnMoved(TableColumnModelEvent e)
  {
  }

  public void columnRemoved(TableColumnModelEvent e)
  {
  }

  public void columnSelectionChanged(ListSelectionEvent e)
  {
    int colnow = getBillCardPanel().getBillTable().getSelectedColumn();
    int rownow = getBillCardPanel().getBillTable().getSelectedRow();

    if ((colnow >= 0) && (getBillCardPanel().getBillModel().getBodyKeyByCol(colnow) != null))
    {
      colEditableSet(getBillCardPanel().getBillModel().getBodyKeyByCol(colnow).trim(), rownow);
    }
  }

  private BillCardPanel getBillCardPanel()
  {
    if (this.ivjBillCardPanel == null) {
      try {
        this.ivjBillCardPanel = new BillCardPanel();
        this.ivjBillCardPanel.setName("BillCardPanel");
        this.ivjBillCardPanel.setAutoscrolls(true);

        BillData bd = new BillData(this.ivjBillCardPanel.getTempletData("4Z", null, this.m_sUserID, this.m_sCorpID));

        if (bd == null) {
          SCMEnv.out("--> billdata null.");
          return this.ivjBillCardPanel;
        }

        bd = BatchCodeDefSetTool.changeBillDataByBCUserDef(this.m_sCorpID, bd);

        this.ivjBillCardPanel.setBillData(bd);

        setScale(this.ivjBillCardPanel);
        this.ivjBillCardPanel.addEditListener(this);
        this.ivjBillCardPanel.getBillTable().getSelectionModel().addListSelectionListener(this);

        this.ivjBillCardPanel.getBillTable().getColumnModel().addColumnModelListener(this);

        this.ivjBillCardPanel.setEnabled(true);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjBillCardPanel;
  }

  protected void getCEnvInfo()
  {
    try
    {
      ClientEnvironment ce = ClientEnvironment.getInstance();

      this.m_sUserID = ce.getUser().getPrimaryKey();
      this.m_sCorpID = ce.getCorporation().getPrimaryKey();
      SCMEnv.out("---->corp id is " + this.m_sCorpID);
      this.m_sCorpName = ce.getCorporation().getUnitname();
      SCMEnv.out("---->corp id is " + this.m_sCorpName);
      if (ce.getDate() != null)
        this.m_sLogDate = ce.getDate().toString();
    }
    catch (Exception e)
    {
    }
  }

  private QueryConditionDlgForBill getConditionDlg(String qryType)
  {
    if (qryType.equals("freeze")) {
      if (this.ivjfreCondDlg == null) {
        this.ivjfreCondDlg = new QueryConditionDlg(this);
        this.ivjQueryConditionDlg = this.ivjfreCondDlg;
      }
      else {
        return this.ivjfreCondDlg;
      }
    }
    else if (qryType.equals("unfreeze")) {
      if (this.ivjunfreCondDlg == null) {
        this.ivjunfreCondDlg = new QueryConditionDlg(this);
        this.ivjQueryConditionDlg = this.ivjunfreCondDlg;
      }
      else {
        return this.ivjunfreCondDlg;
      }

    }
    else if (this.ivjhisCondDlg == null) {
      this.ivjhisCondDlg = new QueryConditionDlg(this);
      this.ivjQueryConditionDlg = this.ivjhisCondDlg;
    }
    else {
      return this.ivjhisCondDlg;
    }

    this.ivjQueryConditionDlg.setTempletID(this.m_sCorpID, this.m_sCurrentBillNode, this.m_sUserID, null, qryType);

    if (!qryType.equals("freeze")) {
      this.ivjQueryConditionDlg.initCorpRef("f.pk_corp", this.m_sCorpID, this.m_alUserCorpID);
    }
    else {
      this.ivjQueryConditionDlg.initCorpRef("pk_corp", this.m_sCorpID, this.m_alUserCorpID);
    }
    this.ivjQueryConditionDlg.initLocatorRef("cspaceid", "invcode", "storcode");
    this.ivjQueryConditionDlg.setLot("vbatchcode", "invcode");

    String[] sThenClear = { "vfree0", "vbatchcode", "cspaceid" };

    this.ivjQueryConditionDlg.setAutoClear("invcode", sThenClear);

    sThenClear = null;
    sThenClear = new String[] { "vbatchcode", "cspaceid" };

    this.ivjQueryConditionDlg.setAutoClear("storcode", sThenClear);

    this.ivjQueryConditionDlg.setFreeItem("vfree0", "invcode");

    this.ivjQueryConditionDlg.hideNormal();
    this.ivjQueryConditionDlg.setRefInitWhereClause("storcode", "仓库档案", "bd_stordoc.pk_corp=", "pk_corp");

    this.ivjQueryConditionDlg.setRefInitWhereClause("storcode", "仓库档案", "gubflag='N'  and pk_corp=", "pk_corp");

    this.ivjQueryConditionDlg.setRefInitWhereClause("ccustomerid", "客商档案", "custflag ='Y' and bd_cumandoc.pk_corp=", "pk_corp");

    this.ivjQueryConditionDlg.setRefInitWhereClause("cproviderid", "供应商档案", "custflag ='N' and bd_cumandoc.pk_corp=", "pk_corp");

    this.ivjQueryConditionDlg.setRefInitWhereClause("invcode", "存货档案", " bd_invmandoc.pk_corp=", "pk_corp");

    this.ivjQueryConditionDlg.setCorpRefs("pk_corp", new String[] { "storcode" });

    this.ivjQueryConditionDlg.setRefInitWhereClause("cspacecode", "货位档案", "bd_cargdoc.endflag='Y' and bd_cargdoc.pk_stordoc=", "storcode");

    return this.ivjQueryConditionDlg;
  }

  protected PrintDataInterface getDataSource()
  {
    if (null == this.m_dataSource) {
      this.m_dataSource = new PrintDataInterface();
      BillData bd = getBillCardPanel().getBillData();

      this.m_dataSource.setBillData(bd);
      this.m_dataSource.setModuleName(this.m_sCurrentBillNode);
      this.m_dataSource.setTotalLinesInOnePage(getPrintEntry().getBreakPos());
    }

    return this.m_dataSource;
  }

  protected PrintEntry getPrintEntry()
  {
    if (null == this.m_print) {
      this.m_print = new PrintEntry(null, null);
      this.m_print.setTemplateID(this.m_sCorpID, this.m_sCurrentBillNode, this.m_sUserID, null);
    }
    return this.m_print;
  }

  public BillTempletVO getTempletVO()
  {
    String[] sItemKeys = { "pk_corp", "cwarehouseid", "cwarehousename", "fselected", "cwarehousename", "cinventoryid", "invcode", "invname", "measdocname", "castunitid", "castunitname", "vfree0", "vfree1", "vfree2", "vfree3", "vfree4", "vfree5", "vfree6", "vfree7", "vfree8", "vfree9", "vfree10", "vbatchcode", "dvalidate", "cspaceid", "cspacecode", "cspacename", "cfreezerid", "cfreezername", "dtfreezetime", "freezeflag", "dthawdate", "cthawpersonid", "cthawpersonname", "nonhandastnum", "nonhandnum", "nfreezeastnum", "nfreezenum", "ccorrespondtype", "ccorrespondcode", "ccorrespondhid", "ccorrespondbid", "cfreezeid", "issolidconvrate", "hsl" };

    Integer[] iPoss = { new Integer(0), new Integer(0), new Integer(0), new Integer(1), new Integer(1), new Integer(1), new Integer(1), new Integer(1), new Integer(1), new Integer(1), new Integer(1), new Integer(1), new Integer(1), new Integer(1), new Integer(1), new Integer(1), new Integer(1), new Integer(1), new Integer(1), new Integer(1), new Integer(1), new Integer(1), new Integer(1), new Integer(1), new Integer(1), new Integer(1), new Integer(1), new Integer(1), new Integer(1), new Integer(1), new Integer(1), new Integer(1), new Integer(1), new Integer(1), new Integer(1), new Integer(1), new Integer(1), new Integer(1), new Integer(1), new Integer(1), new Integer(1), new Integer(1), new Integer(1), new Integer(1), new Integer(1) };

    Integer[] iWidths = { new Integer(1), new Integer(1), new Integer(1), new Integer(2), new Integer(30), new Integer(30), new Integer(20), new Integer(50), new Integer(20), new Integer(20), new Integer(20), new Integer(55), new Integer(60), new Integer(60), new Integer(60), new Integer(60), new Integer(60), new Integer(60), new Integer(60), new Integer(60), new Integer(60), new Integer(60), new Integer(10), new Integer(10), new Integer(20), new Integer(20), new Integer(20), new Integer(20), new Integer(20), new Integer(10), new Integer(20), new Integer(10), new Integer(20), new Integer(20), new Integer(13), new Integer(13), new Integer(13), new Integer(13), new Integer(2), new Integer(30), new Integer(30), new Integer(30), new Integer(30), new Integer(1), new Integer(30) };

    String[] sRefTypes = { "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1" };

    String[] sCaptions = { NCLangRes.getInstance().getStrByID("common", "UC000-0000404"), NCLangRes.getInstance().getStrByID("common", "UC000-0000161"), NCLangRes.getInstance().getStrByID("common", "UC000-0000153"), NCLangRes.getInstance().getStrByID("common", "UC000-0004044"), NCLangRes.getInstance().getStrByID("common", "UC000-0000153"), NCLangRes.getInstance().getStrByID("common", "UC000-0001442"), NCLangRes.getInstance().getStrByID("common", "UC000-0001480"), NCLangRes.getInstance().getStrByID("common", "UC000-0001155"), NCLangRes.getInstance().getStrByID("common", "UC000-0000745"), NCLangRes.getInstance().getStrByID("common", "UC000-0003976"), NCLangRes.getInstance().getStrByID("common", "UC000-0003938"), NCLangRes.getInstance().getStrByID("common", "UC000-0003327"), NCLangRes.getInstance().getStrByID("common", "UC000-0003330"), NCLangRes.getInstance().getStrByID("common", "UC000-0003333"), NCLangRes.getInstance().getStrByID("common", "UC000-0003335"), NCLangRes.getInstance().getStrByID("common", "UC000-0003337"), NCLangRes.getInstance().getStrByID("common", "UC000-0003339"), NCLangRes.getInstance().getStrByID("common", "UC000-0003341"), NCLangRes.getInstance().getStrByID("common", "UC000-0003342"), NCLangRes.getInstance().getStrByID("common", "UC000-0003343"), NCLangRes.getInstance().getStrByID("common", "UC000-0003344"), NCLangRes.getInstance().getStrByID("common", "UC000-0003332"), NCLangRes.getInstance().getStrByID("common", "UC000-0002060"), NCLangRes.getInstance().getStrByID("common", "UC000-0001402"), NCLangRes.getInstance().getStrByID("common", "UC000-0003831"), NCLangRes.getInstance().getStrByID("common", "UC000-0003832"), NCLangRes.getInstance().getStrByID("common", "UC000-0003830"), NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000241"), NCLangRes.getInstance().getStrByID("common", "UC000-0000443"), NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000242"), NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000243"), NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000244"), NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000245"), NCLangRes.getInstance().getStrByID("common", "UC000-0003454"), NCLangRes.getInstance().getStrByID("common", "UC000-0003971"), NCLangRes.getInstance().getStrByID("common", "UC000-0002282"), NCLangRes.getInstance().getStrByID("4008spec", "UPT400824-000001"), NCLangRes.getInstance().getStrByID("common", "UC000-0000445"), NCLangRes.getInstance().getStrByID("4008spec", "UPT400824-000009"), NCLangRes.getInstance().getStrByID("common", "UC000-0001626"), NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000246"), NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000247"), "FREEZE_PK", NCLangRes.getInstance().getStrByID("common", "UC000-0002368"), NCLangRes.getInstance().getStrByID("common", "UC000-0002161") };

    Boolean[] bEditFlags = { new Boolean(false), new Boolean(false), new Boolean(false), new Boolean(true), new Boolean(false), new Boolean(false), new Boolean(false), new Boolean(false), new Boolean(false), new Boolean(false), new Boolean(false), new Boolean(false), new Boolean(false), new Boolean(false), new Boolean(false), new Boolean(false), new Boolean(false), new Boolean(false), new Boolean(false), new Boolean(false), new Boolean(false), new Boolean(false), new Boolean(false), new Boolean(false), new Boolean(false), new Boolean(false), new Boolean(false), new Boolean(false), new Boolean(false), new Boolean(false), new Boolean(false), new Boolean(false), new Boolean(false), new Boolean(false), new Boolean(false), new Boolean(false), new Boolean(false), new Boolean(false), new Boolean(true), new Boolean(true), new Boolean(false), new Boolean(false), new Boolean(false), new Boolean(false), new Boolean(false), new Boolean(false), new Boolean(false) };

    Integer[] iDataTypes = { new Integer(0), new Integer(0), new Integer(0), new Integer(4), new Integer(0), new Integer(0), new Integer(0), new Integer(0), new Integer(0), new Integer(0), new Integer(0), new Integer(0), new Integer(0), new Integer(0), new Integer(0), new Integer(0), new Integer(0), new Integer(0), new Integer(0), new Integer(0), new Integer(0), new Integer(0), new Integer(0), new Integer(0), new Integer(0), new Integer(0), new Integer(0), new Integer(0), new Integer(0), new Integer(0), new Integer(0), new Integer(0), new Integer(0), new Integer(0), new Integer(2), new Integer(2), new Integer(2), new Integer(2), new Integer(0), new Integer(0), new Integer(0), new Integer(0), new Integer(0), new Integer(1), new Integer(2) };

    Integer[] iShowOrder = { new Integer(1), new Integer(2), new Integer(3), new Integer(1), new Integer(2), new Integer(3), new Integer(4), new Integer(5), new Integer(6), new Integer(7), new Integer(10), new Integer(11), new Integer(12), new Integer(13), new Integer(14), new Integer(15), new Integer(16), new Integer(17), new Integer(18), new Integer(19), new Integer(20), new Integer(21), new Integer(22), new Integer(23), new Integer(24), new Integer(25), new Integer(26), new Integer(50), new Integer(51), new Integer(52), new Integer(53), new Integer(54), new Integer(55), new Integer(56), new Integer(27), new Integer(28), new Integer(29), new Integer(30), new Integer(57), new Integer(58), new Integer(59), new Integer(60), new Integer(61), new Integer(7), new Integer(7) };

    Boolean[] bShowFlags = { new Boolean(false), new Boolean(false), new Boolean(true), new Boolean(true), new Boolean(true), new Boolean(false), new Boolean(true), new Boolean(true), new Boolean(true), new Boolean(false), new Boolean(true), new Boolean(true), new Boolean(false), new Boolean(false), new Boolean(false), new Boolean(false), new Boolean(false), new Boolean(false), new Boolean(false), new Boolean(false), new Boolean(false), new Boolean(false), new Boolean(true), new Boolean(true), new Boolean(false), new Boolean(false), new Boolean(true), new Boolean(false), new Boolean(true), new Boolean(true), new Boolean(true), new Boolean(true), new Boolean(false), new Boolean(true), new Boolean(true), new Boolean(true), new Boolean(true), new Boolean(true), new Boolean(false), new Boolean(false), new Boolean(false), new Boolean(false), new Boolean(false), new Boolean(false), new Boolean(true) };

    BillTempletVO voBillTemplet = null;
    BillTempletHeadVO voBillTempletHead = null;

    Vector vBillTempletBodyVO = new Vector();

    for (int i = 0; i < sItemKeys.length; i++) {
      BillTempletBodyVO voTempletBody = new BillTempletBodyVO();
      voTempletBody.setItemkey(sItemKeys[i]);
      voTempletBody.setPos(iPoss[i]);
      if (iPoss[i].intValue() == 1)
        voTempletBody.setWidth(new Integer(70));
      else {
        voTempletBody.setWidth(new Integer(1));
      }
      voTempletBody.setShoworder(iShowOrder[i]);
      voTempletBody.setReftype(sRefTypes[i]);
      voTempletBody.setLockflag(new Boolean(false));
      voTempletBody.setDefaultshowname(sCaptions[i]);
      voTempletBody.setEditflag(bEditFlags[i]);
      voTempletBody.setDatatype(iDataTypes[i]);
      if (iPoss[i].intValue() == 1) {
        voTempletBody.setInputlength(iWidths[i]);
      }
      else {
        voTempletBody.setInputlength(new Integer(80));
      }
      voTempletBody.setUserdefine3(null);
      voTempletBody.setShowflag(bShowFlags[i]);
      voTempletBody.setUserdefine2(null);
      voTempletBody.setUserdefine1(null);

      voTempletBody.setTotalflag(new Boolean(false));
      voTempletBody.setUsereditflag(new Boolean(false));
      voTempletBody.setNullflag(new Boolean(true));

      vBillTempletBodyVO.add(voTempletBody);
    }

    voBillTemplet = new BillTempletVO(voBillTempletHead, vBillTempletBodyVO);

    return voBillTemplet;
  }

  public String getTitle()
  {
    return NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000248");
  }

  private void handleException(Throwable exception)
  {
    SCMEnv.out("--------- 未捕捉到的异常 ---------");
    SCMEnv.error(exception);
  }

  private void initialize()
  {
    try
    {
      setButtons(this.m_aryButtonGroup);
      getCEnvInfo();

      initSysParam();

      setName("ClientUI");
      setLayout(new BorderLayout());
      setSize(774, 419);
      add(getBillCardPanel(), "Center");
    }
    catch (Throwable ivjExc) {
      handleException(ivjExc);
    }

    this.m_boFreeze.setEnabled(false);
    this.m_boUnfreeze.setEnabled(false);
    this.m_boSelectAll.setEnabled(false);
    this.m_boSelectNone.setEnabled(false);
    try {
      ((UIRefPane)getBillCardPanel().getHeadItem("pk_corp").getComponent()).setMaxLength(100);

      ((UIRefPane)getBillCardPanel().getHeadItem("cwarehousename").getComponent()).setMaxLength(100);

      getBillCardPanel().setHeadItem("pk_corp", this.m_sCorpName);
      String[] keys = { "nfreezenum", "nfreezeastnum", "ngrossnum", "ndefrznum", "ndefrzastnum", "ndefrzgrsnum" };
      BillItem item = null;
      for (int k = 0; k < keys.length; k++) {
        item = getBillCardPanel().getBodyItem("nfreezenum");
        if (item != null)
          ((UIRefPane)item.getComponent()).getUITextField().setMinValue(0.0D);
      }
    }
    catch (Exception e)
    {
    }
  }

  protected void initSysParam()
  {
    try
    {
      String[] saParam = { "BD501", "BD502", "BD503", "BD504", "BD301" };

      ArrayList alAllParam = new ArrayList();

      ArrayList alParam = new ArrayList();
      alParam.add(this.m_sCorpID);
      alParam.add(saParam);
      alAllParam.add(alParam);

      alAllParam.add(this.m_sUserID);

      ArrayList alRetData = (ArrayList)ICReportHelper.queryInfo(new Integer(11), alAllParam);

      if ((alRetData == null) || (alRetData.size() < 2)) {
        SCMEnv.out("初始化参数错误！");
        return;
      }

      String[] saParamValue = (String[])(String[])alRetData.get(0);
      if ((saParamValue != null) && (saParamValue.length > 4))
      {
        if (saParamValue[0] != null) {
          this.m_ScaleValue.setNumScale(Integer.parseInt(saParamValue[0]));
        }
        if (saParamValue[1] != null) {
          this.m_ScaleValue.setAssistNumScale(Integer.parseInt(saParamValue[1]));
        }
        if (saParamValue[2] != null) {
          this.m_ScaleValue.setHslScale(Integer.parseInt(saParamValue[2]));
        }
        if (saParamValue[3] != null) {
          this.m_ScaleValue.setPriceScale(Integer.parseInt(saParamValue[3]));
        }
        if (saParamValue[4] != null) {
          this.m_ScaleValue.setMnyScale(Integer.parseInt(saParamValue[4]));
        }

      }

      this.m_alUserCorpID = ((ArrayList)alRetData.get(1));
    }
    catch (Exception e)
    {
      SCMEnv.out("can not get para" + e.getMessage());
    }
  }

  public static void main(String[] args)
  {
    try
    {
      JFrame frame = new JFrame();

      ClientUI aClientUI = new ClientUI();
      frame.setContentPane(aClientUI);
      frame.setSize(aClientUI.getSize());
      frame.addWindowListener(new WindowAdapter() {

          public void windowClosing(WindowEvent e)
          {
              System.exit(0);
          }

      }
);

      frame.show();
      Insets insets = frame.getInsets();
      frame.setSize(frame.getWidth() + insets.left + insets.right, frame.getHeight() + insets.top + insets.bottom);

      frame.setVisible(true);
    }
    catch (Throwable exception) {
      SCMEnv.out("nc.ui.pub.ToftPanel 的 main() 中发生异常");
      SCMEnv.error(exception);
    }
  }

  void newMethod()
  {
  }

  public void onButtonClicked(ButtonObject bo)
  {
    showHintMessage(bo.getName());
    if (bo == this.m_boFreezeQry)
      onFreezeQry();
    else if (bo == this.m_boUnfreezeQry)
      onUnfreezeQry();
    else if (bo == this.m_boHistoryQry)
      onHistoryQry();
    else if (bo == this.m_boSelectAll)
      onSelectAll();
    else if (bo == this.m_boSelectNone)
      onSelectNone();
    else if (bo == this.m_boFreeze)
      onFreeze();
    else if (bo == this.m_boUnfreeze)
      onUnfreeze();
    else if (bo == this.m_boPrint)
      onPrint();
    else if (bo == this.m_boPreview)
      onPreview();
  }

  protected void onFreeze()
  {
    try
    {
      FreezeBillVO voFb = (FreezeBillVO)getBillCardPanel().getBillValueVO("nc.vo.ic.pub.freeze.FreezeBillVO", "nc.vo.ic.pub.freeze.FreezeVO", "nc.vo.ic.pub.freeze.FreezeVO");

      if ((voFb == null) || (voFb.getParentVO() == null) || (voFb.getChildrenVO() == null) || (voFb.getChildrenVO().length == 0))
      {
        return;
      }

      Vector vSelectedLine = new Vector();
      Vector vFb = new Vector();
      FreezeVO[] voaFreeze = (FreezeVO[])(FreezeVO[])voFb.getChildrenVO();
      int iCount = 0;
      ArrayList voList = new ArrayList();
      for (int i = 0; i < voaFreeze.length; i++) {
        if ((voaFreeze[i].getFselected() != null) && (voaFreeze[i].getFselected().booleanValue()))
        {
          if (iCount == 0) {
            voaFreeze[i].setPk_corp(this.m_sCorpID);
            voaFreeze[i].setCwarehouseid(this.m_sWhID);
            voaFreeze[i].setCfreezerid(this.m_sUserID);
            voaFreeze[i].setDtfreezetime(new UFDate(this.m_sLogDate));
          }

          if (voaFreeze[i].getNfreezenum() == null) {
            voaFreeze[i].setNfreezenum(voaFreeze[i].getNonhandnum());
            voaFreeze[i].setNfreezeastnum(voaFreeze[i].getNonhandastnum());
          }
          else if (voaFreeze[i].getNonhandnum() == null) {
            showErrorMessage(NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000249", null, new String[] { voaFreeze[i].getInvname() }));
          }
          else
          {
            if ((GenMethod.isLEZero(voaFreeze[i].getNfreezenum())) || (GenMethod.isLEZero(voaFreeze[i].getNfreezeastnum())) || (GenMethod.isLEZero(voaFreeze[i].getNgrossnum())))
            {
              showErrorMessage("冻结数量不能小于等于零!");
              return;
            }
            if (voaFreeze[i].getNfreezenum().compareTo(voaFreeze[i].getNonhandnum()) <= 0) { if (voaFreeze[i].getNfreezeastnum() != null) if (voaFreeze[i].getNfreezeastnum().compareTo(voaFreeze[i].getNonhandastnum() == null ? GenMethod.ZERO : voaFreeze[i].getNonhandastnum()) <= 0); 
            } else
            {
              showErrorMessage(NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000250", null, new String[] { voaFreeze[i].getInvname() }));

              return;
            }

          }

          voaFreeze[i].setDtfreezetime(ClientEnvironment.getInstance().getDate());

          voaFreeze[i].setBfrzhandflag("Y");
          vFb.add(voaFreeze[i]);
          vSelectedLine.add("" + i);
          iCount++;
        }
        else {
          voList.add(this.m_sBillvo.getChildrenVO()[i]);
        }
      }
      if (vFb.size() == 0) {
        showHintMessage(NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000251"));

        return;
      }
      FreezeVO[] voaFreeze2 = new FreezeVO[vFb.size()];
      vFb.copyInto(voaFreeze2);

      ClientCacheHelper.getColValue(voaFreeze2, new String[] { "cinvbasid" }, "bd_invmandoc", "pk_invmandoc", new String[] { "pk_invbasdoc" }, "cinventoryid");

      FreezeHelper.freeze(voaFreeze2);

      int[] iaLine = new int[vSelectedLine.size()];
      for (int i = 0; i < iaLine.length; i++) {
        iaLine[i] = Integer.valueOf(vSelectedLine.elementAt(i).toString()).intValue();
      }
      getBillCardPanel().getBillModel().delLine(iaLine);

      FreezeVO[] vlFreeze = new FreezeVO[voList.size()];
      for (int i = 0; i < voList.size(); i++)
        vlFreeze[i] = ((FreezeVO)voList.get(i));
      this.m_sBillvo.setChildrenVO(vlFreeze);

      showHintMessage(NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000252"));
    }
    catch (Exception e)
    {
      handleException(e);
      showErrorMessage(NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000253") + e.getMessage());
    }
  }

  protected void onFreezeQry()
  {
    try
    {
      getConditionDlg("freeze").showModal();
      if (!getConditionDlg("freeze").isCloseOK()) {
        return;
      }
      QryConditionVO voCond = new QryConditionVO();
      ConditionVO[] voaCond = getConditionDlg("freeze").getConditionVO();

      String sWhID = null; String sWhName = null;

      Vector vTempCond = new Vector();

      String sCorpID = null; String sCorpName = null;
      String sFieldCode = null;

      String sConds = "";

      String sInvCond = "";
      if (voaCond != null) {
        for (int i = 0; i < voaCond.length; i++) {
          sFieldCode = voaCond[i].getFieldCode().trim();
          if (("pk_corp".equals(sFieldCode)) && (voaCond[i].getRefResult() != null)) {
            sCorpID = voaCond[i].getRefResult().getRefPK();
            sCorpName = voaCond[i].getRefResult().getRefName();
          }
          else if (("storcode".equals(sFieldCode)) && (voaCond[i].getRefResult() != null) && (voaCond[i].getRefResult().getRefPK() != null))
          {
            sWhID = voaCond[i].getRefResult().getRefPK();
            sWhName = voaCond[i].getRefResult().getRefName();
          }
          else if (("invcode".equals(sFieldCode)) || ("invspec".equals(sFieldCode)) || ("invtype".equals(sFieldCode)) || ("invname".equals(sFieldCode)))
          {
            String sCond = voaCond[i].getSQLStr();
            sConds = sConds + sCond;
          }
          else {
            vTempCond.add(voaCond[i]);
          }

        }

        if (sConds.length() > 0) {
          sInvCond = " and cinvbasid in  (select pk_invbasdoc from bd_invbasdoc where 1=1 " + sConds + " )";
        }

      }

      if ((sWhID == null) || (sWhID.trim().length() == 0)) {
        showErrorMessage(NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000254"));

        return;
      }

      if ((sCorpID == null) || (sCorpID.trim().length() == 0)) {
        sCorpID = this.m_sCorpID;
        sCorpName = this.m_sCorpName;
      }

      this.m_sWhID = sWhID;

      voCond.setParam(0, sWhID);
      voCond.setParam(1, "FREEZE");
      voCond.setStrParam(0, this.m_sCorpID);

      ConditionVO[] voaCond22 = new ConditionVO[vTempCond.size()];

      vTempCond.copyInto(voaCond22);

      voCond.setQryCond(getConditionDlg("freeze").getWhereSQL(voaCond22) + sInvCond);

      GenMsgCtrl.printHint(voCond.getQryCond());

      ArrayList alRet = FreezeHelper.query(voCond);

      if ((alRet == null) || (alRet.size() < 2) || (alRet.get(0) == null) || (alRet.get(1) == null))
      {
        showHintMessage(NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000255"));

        clearUi();
        setButtonStatus();
        return;
      }

      FreezeVO[] voaF = (FreezeVO[])(FreezeVO[])alRet.get(1);

      if ((voaF != null) && (voaF.length != 0) && (voaF[0] != null))
      {
        BatchCodeDefSetTool.execFormulaForBatchCode(voaF);

        this.m_iCurRowCount = voaF.length;
        this.m_sBillvo = new FreezeBillVO();

        voaF[0].setPk_corp(sCorpName);
        this.m_sBillvo.setParentVO(voaF[0]);
        this.m_sBillvo.setChildrenVO(voaF);

        getBillCardPanel().setBillValueVO(this.m_sBillvo);

        if (getBillCardPanel().getHeadItem("pk_corp") != null)
          getBillCardPanel().getHeadItem("pk_corp").setValue(sCorpName);
        if (getBillCardPanel().getHeadItem("cwarehousename") != null) {
          getBillCardPanel().getHeadItem("cwarehousename").setValue(sWhName);
        }
        getBillCardPanel().getBillModel().execLoadFormula();
        this.m_iCurStatus = 0;
        setButtonStatus();
        setColEditable();
        showHintMessage(NCLangRes.getInstance().getStrByID("4008spec", "UPPSCMCommon-000133"));
      }
      else
      {
        showHintMessage(NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000255"));
      }

      getBillCardPanel().showBodyTableCol("nonhandnum");
      getBillCardPanel().showBodyTableCol("nonhandastnum");
      getBillCardPanel().showBodyTableCol("ngross");

      getBillCardPanel().hideBodyTableCol("cthawpersonname");
      getBillCardPanel().hideBodyTableCol("dthawdate");
    }
    catch (Exception e) {
      handleException(e);
      showErrorMessage(NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000256") + e.getMessage());
    }
  }

  protected void onHistoryQry()
  {
    String sWhID = null; String sWhName = null;
    String sCorpID = null; String sCorpName = null;
    String sFieldCode = null;
    StringBuffer sbInvWhereSql = new StringBuffer();
    StringBuffer sbInvWhereSql2 = new StringBuffer();
    Vector vOtherWhereSql = new Vector();
    try
    {
      getConditionDlg("history").showModal();

      if (!getConditionDlg("history").isCloseOK()) {
        return;
      }

      QryConditionVO voCond = new QryConditionVO();
      ConditionVO[] voaCond = getConditionDlg("history").getConditionVO();

      if (voaCond != null) {
        for (int i = 0; i < voaCond.length; i++) {
          sFieldCode = voaCond[i].getFieldCode().trim();

          if ((sFieldCode.equals("pk_corp")) && (voaCond[i].getRefResult() != null))
          {
            sCorpID = voaCond[i].getRefResult().getRefPK();
            sCorpName = voaCond[i].getRefResult().getRefName();
          }
          else if (sFieldCode.equals("storcode"))
          {
            sWhID = voaCond[i].getRefResult().getRefPK();
            sWhName = voaCond[i].getRefResult().getRefName();
          }
          else if (sFieldCode.equals("cspacecode"))
          {
            voaCond[i].setFieldCode("loc.cscode");
            vOtherWhereSql.add(voaCond[i]);
          }
          else if ((sFieldCode.equals("invcode")) || (sFieldCode.equals("invname")) || (sFieldCode.equals("invspec")) || (sFieldCode.equals("invtype")))
          {
            voaCond[i].setFieldCode("bd." + sFieldCode);

            sbInvWhereSql.append(voaCond[i].getSQLStr());
          }
          else
          {
            vOtherWhereSql.add(voaCond[i]);
          }

        }

        if (sbInvWhereSql.length() > 0)
        {
          sbInvWhereSql2 = new StringBuffer(" and cinvbasid in (select pk_invbasdoc from bd_invbasdoc bd where 1=1 ");

          sbInvWhereSql2.append(sbInvWhereSql).append(" )");
        }

      }

      if ((sWhID == null) || (sWhID.trim().length() == 0)) {
        showErrorMessage(NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000254"));

        return;
      }

      this.m_sWhID = sWhID;

      if ((sCorpID == null) || (sCorpID.trim().length() == 0)) {
        sCorpID = this.m_sCorpID;
        sCorpName = this.m_sCorpName;
      }

      voCond.setParam(0, sWhID);
      voCond.setParam(1, "HISTORY");
      voCond.setStrParam(0, this.m_sCorpID);

      ConditionVO[] voaCond22 = new ConditionVO[vOtherWhereSql.size()];

      vOtherWhereSql.copyInto(voaCond22);

      voCond.setQryCond(getConditionDlg("history").getWhereSQL(voaCond22) + sbInvWhereSql2.toString());

      ArrayList alRet = FreezeHelper.query(voCond);

      if ((alRet == null) || (alRet.size() < 2) || (alRet.get(1) == null))
      {
        showHintMessage(NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000255"));

        clearUi();
        setButtonStatus();
        return;
      }

      FreezeVO[] voaF = (FreezeVO[])(FreezeVO[])alRet.get(1);
      if ((voaF != null) && (voaF.length != 0) && (voaF[0] != null)) {
        String sfrzhandflag = "";
        for (int i = 0; i < voaF.length; i++) {
          sfrzhandflag = voaF[i].getBfrzhandflag();
          if (sfrzhandflag.equals("N")) {
            voaF[i].setNfreezeastnum(null);
            voaF[i].setNfreezenum(null);
          }
        }

        BatchCodeDefSetTool.execFormulaForBatchCode(voaF);

        this.m_iCurRowCount = voaF.length;
        this.m_sBillvo = new FreezeBillVO();

        voaF[0].setPk_corp(this.m_sCorpName);

        this.m_sBillvo.setParentVO(voaF[0]);
        this.m_sBillvo.setChildrenVO(voaF);
        getBillCardPanel().setBillValueVO(this.m_sBillvo);

        if (getBillCardPanel().getHeadItem("pk_corp") != null)
          getBillCardPanel().getHeadItem("pk_corp").setValue(sCorpName);
        if (getBillCardPanel().getHeadItem("cwarehousename") != null)
          getBillCardPanel().getHeadItem("cwarehousename").setValue(sWhName);
        this.m_iCurStatus = 2;
        setButtonStatus();
        setColEditable();
        showHintMessage(NCLangRes.getInstance().getStrByID("4008spec", "UPPSCMCommon-000133"));
      }
      else
      {
        showHintMessage(NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000255"));
      }

      try
      {
        getBillCardPanel().hideBodyTableCol("nonhandnum");
        getBillCardPanel().hideBodyTableCol("nonhandastnum");
        getBillCardPanel().hideBodyTableCol("ngross");

        getBillCardPanel().showBodyTableCol("cthawpersonname");
        getBillCardPanel().showBodyTableCol("dthawdate");
      }
      catch (Exception e)
      {
      }
      getBillCardPanel().getBillModel().execLoadFormula();
    }
    catch (Exception e) {
      handleException(e);
      showErrorMessage(NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000256") + e.getMessage());
    }
  }

  public void onPreview()
  {
    FreezeBillVO vo = (FreezeBillVO)getBillCardPanel().getBillValueVO("nc.vo.ic.pub.freeze.FreezeBillVO", "nc.vo.ic.pub.freeze.FreezeVO", "nc.vo.ic.pub.freeze.FreezeVO");

    if (null == vo) {
      vo = new FreezeBillVO();
    }
    if (null == vo.getParentVO()) {
      vo.setParentVO(new FreezeVO());
    }
    if ((null == vo.getChildrenVO()) || (vo.getChildrenVO().length == 0) || (vo.getChildrenVO()[0] == null))
    {
      FreezeVO[] ivo = new FreezeVO[1];
      ivo[0] = new FreezeVO();
      vo.setChildrenVO(ivo);
    }

    if (getPrintEntry().selectTemplate() < 0) {
      return;
    }
    getDataSource().setVO(vo);

    getPrintEntry().setDataSource(getDataSource());
    getPrintEntry().preview();
  }

  public void onPrint()
  {
    FreezeBillVO vo = (FreezeBillVO)getBillCardPanel().getBillValueVO("nc.vo.ic.pub.freeze.FreezeBillVO", "nc.vo.ic.pub.freeze.FreezeVO", "nc.vo.ic.pub.freeze.FreezeVO");

    if (null == vo) {
      vo = new FreezeBillVO();
    }
    if (null == vo.getParentVO()) {
      vo.setParentVO(new FreezeVO());
    }
    if ((null == vo.getChildrenVO()) || (vo.getChildrenVO().length == 0) || (vo.getChildrenVO()[0] == null))
    {
      FreezeVO[] ivo = new FreezeVO[1];
      ivo[0] = new FreezeVO();
      vo.setChildrenVO(ivo);
    }

    if (getPrintEntry().selectTemplate() < 0) {
      return;
    }
    getDataSource().setVO(vo);

    getPrintEntry().setDataSource(getDataSource());
    getPrintEntry().print();
  }

  protected void onSelectAll()
  {
    int iRowCount = getBillCardPanel().getBillTable().getRowCount();
    for (int i = 0; i < iRowCount; i++)
      getBillCardPanel().getBillModel().setValueAt(new UFBoolean(true), i, "fselected");
  }

  protected void onSelectNone()
  {
    int iRowCount = getBillCardPanel().getBillTable().getRowCount();
    for (int i = 0; i < iRowCount; i++)
      getBillCardPanel().getBillModel().setValueAt(new UFBoolean(false), i, "fselected");
  }

  protected void onUnfreeze()
  {
    try
    {
      UFDouble freNum = null;
      UFDouble unFreNum = null;
      UFDouble freastNum = null;
      UFDouble unFreastNum = null;
      UFDouble fregroNum = null;
      UFDouble unFregroNum = null;

      StringBuffer sbMsg = new StringBuffer();

      FreezeBillVO voFb = (FreezeBillVO)getBillCardPanel().getBillValueVO("nc.vo.ic.pub.freeze.FreezeBillVO", "nc.vo.ic.pub.freeze.FreezeVO", "nc.vo.ic.pub.freeze.FreezeVO");

      if ((voFb == null) || (voFb.getParentVO() == null) || (voFb.getChildrenVO() == null) || (voFb.getChildrenVO().length == 0))
      {
        return;
      }

      Vector vSelectedLine = new Vector();
      Vector vFb = new Vector();
      FreezeVO[] voaFreeze = (FreezeVO[])(FreezeVO[])voFb.getChildrenVO();
      int iCount = 0;
      ArrayList voList = new ArrayList();
      for (int i = 0; i < voaFreeze.length; i++)
      {
        if ((voaFreeze[i].getFselected() != null) && (voaFreeze[i].getFselected().booleanValue()))
        {
          if ((null != (freNum = voaFreeze[i].getNfreezenum())) && (null != (unFreNum = voaFreeze[i].getNdefrznum())))
          {
            if (unFreNum.compareTo(freNum) > 0) {
              sbMsg.append(i + 1).append(ResBase.getUnFreNumMsg());
            }

          }

          if ((GenMethod.isLEZero(voaFreeze[i].getNdefrznum())) || (GenMethod.isLEZero(voaFreeze[i].getNdefrzastnum())) || (GenMethod.isLEZero(voaFreeze[i].getNdefrzgrsnum())))
          {
            showErrorMessage("解冻结数量不能小于等于零!");
            return;
          }

          if ((null != (freastNum = voaFreeze[i].getNfreezeastnum())) && (null != (unFreastNum = voaFreeze[i].getNdefrzastnum())))
          {
            if (unFreastNum.compareTo(freastNum) > 0) {
              sbMsg.append(i + 1).append(ResBase.getUnFreastNumMsg());
            }

          }

          if ((null != (fregroNum = voaFreeze[i].getNgrossnum())) && (null != (unFregroNum = voaFreeze[i].getNdefrzgrsnum())))
          {
            if (unFregroNum.compareTo(fregroNum) > 0) {
              sbMsg.append(i + 1).append(ResBase.getUnFregroNumMsg());
            }

          }

          if (iCount == 0) {
            voaFreeze[i].setPk_corp(this.m_sCorpID);
            voaFreeze[i].setCwarehouseid(this.m_sWhID);
            voaFreeze[i].setCthawpersonid(this.m_sUserID);
            voaFreeze[i].setDthawdate(new UFDate(this.m_sLogDate));
          }
          vFb.add(voaFreeze[i]);
          vSelectedLine.add("" + i);
          iCount++;
        }
        else {
          voList.add(this.m_sBillvo.getChildrenVO()[i]);
        }
      }
      if (vFb.size() == 0) {
        showHintMessage(NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000257"));

        return;
      }

      if (sbMsg.length() > 0) {
        showErrorMessage(sbMsg.toString());
        return;
      }
      FreezeVO[] voaFreeze2 = new FreezeVO[vFb.size()];
      vFb.copyInto(voaFreeze2);

      FreezeHelper.unfreeze(voaFreeze2);

      int[] iaLine = new int[vSelectedLine.size()];
      for (int i = 0; i < iaLine.length; i++) {
        iaLine[i] = Integer.valueOf(vSelectedLine.elementAt(i).toString()).intValue();
      }
      getBillCardPanel().getBillModel().delLine(iaLine);

      FreezeVO[] vlFreeze = new FreezeVO[voList.size()];
      for (int i = 0; i < voList.size(); i++)
        vlFreeze[i] = ((FreezeVO)voList.get(i));
      this.m_sBillvo.setChildrenVO(vlFreeze);

      showHintMessage(NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000258"));
    }
    catch (Exception e)
    {
      handleException(e);
      showErrorMessage(NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000259") + e.getMessage());
    }
  }

  protected void onUnfreezeQry()
  {
    String sWhID = null; String sWhName = null;
    String sCorpID = null; String sCorpName = null;
    String sFieldCode = null;
    StringBuffer sbInvWhereSql = new StringBuffer();
    StringBuffer sbInvWhereSql2 = new StringBuffer();
    Vector vOtherWhereSql = new Vector();
    try
    {
      getConditionDlg("unfreeze").showModal();

      if (!getConditionDlg("unfreeze").isCloseOK()) {
        return;
      }

      QryConditionVO voCond = new QryConditionVO();
      ConditionVO[] voaCond = getConditionDlg("unfreeze").getConditionVO();

      if (voaCond != null) {
        for (int i = 0; i < voaCond.length; i++) {
          sFieldCode = voaCond[i].getFieldCode().trim();

          if ((sFieldCode.equals("pk_corp")) && (voaCond[i].getRefResult() != null))
          {
            sCorpID = voaCond[i].getRefResult().getRefPK();
            sCorpName = voaCond[i].getRefResult().getRefName();
          }
          else if ((sFieldCode.equals("storcode")) && (voaCond[i].getRefResult() != null) && (voaCond[i].getRefResult().getRefPK() != null))
          {
            sWhID = voaCond[i].getRefResult().getRefPK();
            sWhName = voaCond[i].getRefResult().getRefName();
          }
          else if (sFieldCode.equals("cspacecode"))
          {
            voaCond[i].setFieldCode("loc.cscode");
            vOtherWhereSql.add(voaCond[i]);
          }
          else if ((sFieldCode.equals("invcode")) || (sFieldCode.equals("invname")) || (sFieldCode.equals("invspec")) || (sFieldCode.equals("invtype")))
          {
            voaCond[i].setFieldCode("bd." + sFieldCode);

            sbInvWhereSql.append(voaCond[i].getSQLStr());
          }
          else
          {
            vOtherWhereSql.add(voaCond[i]);
          }

        }

        if (sbInvWhereSql.length() > 0)
        {
          sbInvWhereSql2 = new StringBuffer(" and cinvbasid in (select pk_invbasdoc from bd_invbasdoc bd where 1=1 ");

          sbInvWhereSql2.append(sbInvWhereSql).append(" )");
        }

      }

      if ((sWhID == null) || (sWhID.trim().length() == 0)) {
        showErrorMessage(NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000254"));

        return;
      }

      this.m_sWhID = sWhID;

      if ((sCorpID == null) || (sCorpID.trim().length() == 0)) {
        sCorpID = this.m_sCorpID;
        sCorpName = this.m_sCorpName;
      }

      voCond.setParam(0, sWhID);
      voCond.setParam(1, "UNFREEZE");
      voCond.setStrParam(0, this.m_sCorpID);

      ConditionVO[] voaCond22 = new ConditionVO[vOtherWhereSql.size()];

      vOtherWhereSql.copyInto(voaCond22);

      voCond.setQryCond(getConditionDlg("unfreeze").getWhereSQL(voaCond22) + sbInvWhereSql2.toString());

      ArrayList alRet = FreezeHelper.query(voCond);

      if ((alRet == null) || (alRet.size() < 2) || (alRet.get(1) == null))
      {
        showHintMessage(NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000255"));

        clearUi();
        setButtonStatus();
        return;
      }

      FreezeVO[] voaF = (FreezeVO[])(FreezeVO[])alRet.get(1);
      if ((voaF != null) && (voaF.length != 0) && (voaF[0] != null))
      {
        BatchCodeDefSetTool.execFormulaForBatchCode(voaF);

        this.m_iCurRowCount = voaF.length;

        this.m_sBillvo = new FreezeBillVO();

        voaF[0].setPk_corp(this.m_sCorpName);
        this.m_sBillvo.setParentVO(voaF[0]);
        this.m_sBillvo.setChildrenVO(voaF);

        getBillCardPanel().setBillValueVO(this.m_sBillvo);

        getBillCardPanel().getBillModel().execLoadFormula();

        if (getBillCardPanel().getHeadItem("pk_corp") != null)
          getBillCardPanel().getHeadItem("pk_corp").setValue(sCorpName);
        if (getBillCardPanel().getHeadItem("cwarehousename") != null) {
          getBillCardPanel().getHeadItem("cwarehousename").setValue(sWhName);
        }
        this.m_iCurStatus = 1;
        setButtonStatus();
        setColEditable();
        showHintMessage(NCLangRes.getInstance().getStrByID("4008spec", "UPPSCMCommon-000133"));
      }
      else
      {
        showHintMessage(NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000255"));
      }

      try
      {
        getBillCardPanel().hideBodyTableCol("nonhandnum");
        getBillCardPanel().hideBodyTableCol("nonhandastnum");
        getBillCardPanel().hideBodyTableCol("ngross");

        getBillCardPanel().hideBodyTableCol("cthawpersonname");
        getBillCardPanel().hideBodyTableCol("dthawdate");
      }
      catch (RuntimeException e)
      {
        SCMEnv.out(e);
        handleException(e);
      }
    }
    catch (Exception e)
    {
      handleException(e);
      showErrorMessage(NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000256") + e.getMessage());
    }
  }

  protected void setButtonStatus()
  {
    switch (this.m_iCurStatus) {
    case 0:
      if (this.m_iCurRowCount > 0) {
        this.m_boFreeze.setEnabled(true);
        this.m_boSelectAll.setEnabled(true);
        this.m_boSelectNone.setEnabled(true);
        this.m_boPrint.setEnabled(true);
        this.m_boPreview.setEnabled(true);
      }
      else {
        this.m_boFreeze.setEnabled(false);
        this.m_boSelectAll.setEnabled(false);
        this.m_boSelectNone.setEnabled(false);
        this.m_boPrint.setEnabled(false);
        this.m_boPreview.setEnabled(false);
      }
      this.m_boUnfreeze.setEnabled(false);

      break;
    case 1:
      this.m_boFreeze.setEnabled(false);
      if (this.m_iCurRowCount > 0) {
        this.m_boUnfreeze.setEnabled(true);
        this.m_boSelectAll.setEnabled(true);
        this.m_boSelectNone.setEnabled(true);
        this.m_boPrint.setEnabled(true);
        this.m_boPreview.setEnabled(true);
      }
      else {
        this.m_boUnfreeze.setEnabled(false);
        this.m_boSelectAll.setEnabled(false);
        this.m_boSelectNone.setEnabled(false);
        this.m_boPrint.setEnabled(false);
        this.m_boPreview.setEnabled(false);
      }
      break;
    case 2:
      this.m_boFreeze.setEnabled(false);
      this.m_boUnfreeze.setEnabled(false);
      this.m_boSelectAll.setEnabled(false);
      this.m_boSelectNone.setEnabled(false);
      if (this.m_iCurRowCount > 0) {
        this.m_boPrint.setEnabled(true);
        this.m_boPreview.setEnabled(true);
      }
      else {
        this.m_boPrint.setEnabled(false);
        this.m_boPreview.setEnabled(false);
      }

    }

    updateButtons();
  }

  protected void setColEditable()
  {
    switch (this.m_iCurStatus) {
    case 0:
      if (getBillCardPanel().getBodyItem("fselected") != null)
        getBillCardPanel().getBodyItem("fselected").setEnabled(true);
      if (getBillCardPanel().getBodyItem("nfreezenum") != null) {
        getBillCardPanel().getBodyItem("nfreezenum").setEnabled(true);
      }

      if (getBillCardPanel().getBodyItem("ndefrznum") != null) {
        getBillCardPanel().getBodyItem("ndefrznum").setEnabled(false);
      }

      if (getBillCardPanel().getBodyItem("ndefrzgrsnum") != null) {
        getBillCardPanel().getBodyItem("ndefrzgrsnum").setEnabled(false);
      }

      if (getBillCardPanel().getBodyItem("ndefrzastnum") == null) break;
      getBillCardPanel().getBodyItem("ndefrzastnum").setEnabled(false);
      break;
    case 1:
      if (getBillCardPanel().getBodyItem("fselected") != null) {
        getBillCardPanel().getBodyItem("fselected").setEnabled(true);
      }
      if (getBillCardPanel().getBodyItem("nfreezenum") != null) {
        getBillCardPanel().getBodyItem("nfreezenum").setEnabled(false);
      }

      if (getBillCardPanel().getBodyItem("ndefrznum") != null) {
        getBillCardPanel().getBodyItem("ndefrznum").setEnabled(true);
      }

      if (getBillCardPanel().getBodyItem("ndefrzastnum") != null) {
        getBillCardPanel().getBodyItem("ndefrzastnum").setEnabled(true);
      }

      if (getBillCardPanel().getBodyItem("ndefrzgrsnum") == null) break;
      getBillCardPanel().getBodyItem("ndefrzgrsnum").setEnabled(true);
      break;
    case 2:
      if (getBillCardPanel().getBodyItem("fselected") != null)
        getBillCardPanel().getBodyItem("fselected").setEnabled(false);
      if (getBillCardPanel().getBodyItem("nfreezenum") == null) break;
      getBillCardPanel().getBodyItem("nfreezenum").setEnabled(false);
    }
  }

  protected void setScale(BillCardPanel card)
  {
    ScaleInit si = new ScaleInit(this.m_sUserID, this.m_sCorpID, this.m_ScaleValue);
    try
    {
      this.m_ScaleKey = new ScaleKey();
      this.m_ScaleKey.setNumKeys(new String[] { "nonhandnum", "nfreezenum", "ngrossnum", "ngross", "ndefrznum", "ndefrzgrsnum" });

      this.m_ScaleKey.setAssistNumKeys(new String[] { "nonhandastnum", "nfreezeastnum", "ndefrzastnum" });

      this.m_ScaleKey.setPriceKeys(new String[] { "nprice", "nplannedprice" });

      this.m_ScaleKey.setMnyKeys(new String[] { "nmny", "nplannedmny" });

      this.m_ScaleKey.setHslKeys(new String[] { "hsl" });

      si.setScale(card, this.m_ScaleKey);
    }
    catch (Exception e) {
      showHintMessage(NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000060") + e.getMessage());
    }
  }

  public void valueChanged(ListSelectionEvent e)
  {
    int colnow = getBillCardPanel().getBillTable().getSelectedColumn();
    int rownow = getBillCardPanel().getBillTable().getSelectedRow();
    if ((colnow >= 0) && (getBillCardPanel().getBillModel().getBodyKeyByCol(colnow) != null))
    {
      colEditableSet(getBillCardPanel().getBillModel().getBodyKeyByCol(colnow).trim(), rownow);
    }
  }

  public void afterGrossNumEdit(BillEditEvent e)
  {
    UFDouble ngross = new UFDouble(0.0D);
    if (getBillCardPanel().getBodyValueAt(e.getRow(), "ngross") != null) {
      ngross = new UFDouble(getBillCardPanel().getBodyValueAt(e.getRow(), "ngross").toString().trim());
    }

    UFDouble ngrossnum = new UFDouble(0.0D);
    ngrossnum = new UFDouble(getBillCardPanel().getBodyValueAt(e.getRow(), "ngrossnum").toString().trim());

    if (ngrossnum.doubleValue() > ngross.doubleValue()) {
      showHintMessage("冻结毛重不能大于可冻结毛重。请修改后重试。");
      getBillCardPanel().setBodyValueAt(null, e.getRow(), "ngrossnum");
      return;
    }

    if (ngrossnum.doubleValue() <= 0.0D) {
      showHintMessage("冻结毛重必须大于零。请修改后重试。");
      getBillCardPanel().setBodyValueAt(null, e.getRow(), "ngrossnum");
      return;
    }
  }
}