package nc.ui.so.so002;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
 

import nc.bs.bd.b21.BusinessCurrencyRateUtil;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.com.google.gson.Gson;
import nc.itf.ic.pub.IIcbItf;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.net.sf.json.JSONArray;
import nc.net.sf.json.JSONObject;
import nc.ui.bd.CorpBO_Client;
import nc.ui.bd.b21.CurrtypeQuery;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIMenuItem;
import nc.ui.pub.beans.UIRadioButton;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillBodyMenuListener;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListData;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillMouseEnent;
import nc.ui.pub.bill.BillTableMouseListener;
import nc.ui.pub.bill.BillTotalListener;
import nc.ui.pub.formulaparse.FormulaParse;
import nc.ui.pub.para.SysInitBO_Client;
import nc.ui.pub.pf.PfUtilBO_Client;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.pub.print.PrintEntry;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.pub.tools.BannerDialog;
import nc.ui.pub.workflownote.FlowStateDlg;
import nc.ui.scm.datapower.DpsClient;
import nc.ui.scm.file.DocumentManager;
import nc.ui.scm.freecust.UFRefGridUI;
import nc.ui.scm.goldtax.TransGoldTaxDlg;
import nc.ui.scm.ic.freeitem.FreeItemRefPane;
import nc.ui.scm.print.IFreshTsListener;
import nc.ui.scm.print.PrintLogClient;
import nc.ui.scm.print.SalePubPrintDS;
import nc.ui.scm.pub.CollectSettingDlg;
import nc.ui.scm.pub.FreeVOParse;
import nc.ui.scm.pub.ScmPubHelper;
import nc.ui.scm.pub.bill.ButtonTree;
import nc.ui.scm.pub.bill.IBillExtendFun;
import nc.ui.scm.pub.def.DefSetTool;
import nc.ui.scm.pub.query.ConvertQueryCondition;
import nc.ui.scm.pub.query.SCMQueryConditionDlg;
import nc.ui.scm.pub.report.BillRowNo;
import nc.ui.scm.pub.report.LocateDialog;
import nc.ui.scm.ref.prm.CustBankRefModel;
import nc.ui.scm.so.SaleBillType;
import nc.ui.scm.sourcebill.SourceBillFlowDlg;
import nc.ui.so.so001.panel.card.SOBillCardTools;
import nc.ui.so.so001.panel.list.SOBusiTypeRefPane;
import nc.vo.bd.CorpVO;
import nc.vo.bd.b20.CurrtypeVO;
import nc.vo.bd.b21.CurrinfoVO;
import nc.vo.dm.pub.DMDataVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVOUtil;
import nc.vo.pub.VOStatus;
import nc.vo.pub.ValidationException;
import nc.vo.pub.bill.BillTempletVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.query.QueryConditionVO;
import nc.vo.scm.goldtax.GoldTaxHeadVO;
import nc.vo.scm.goldtax.GoldTaxVO;
import nc.vo.scm.ic.bill.FreeVO;
import nc.vo.scm.ic.bill.InvVO;
import nc.vo.scm.pub.ArrayMethod;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.so.pub.CustCreditVO;
import nc.vo.so.so001.SoVoConst;
import nc.vo.so.so002.SaleVO;
import nc.vo.so.so002.SaleinvoiceBVO;
import nc.vo.so.so002.SaleinvoiceVO;
import nc.vo.so.so006.ProfitHeaderVO;
import nc.vo.so.so006.ProfitItemVO;
import nc.vo.so.so006.ProfitVO;
import nc.vo.so.so015.ARSubUniteVO;
import nc.vo.so.so015.ARSubVO;
import nc.vo.so.so016.SoVoTools;

public class SaleInvoiceAdminUI extends ToftPanel
  implements BillEditListener, BillEditListener2, BillBodyMenuListener, BillTotalListener, BillTableMouseListener, IFreshTsListener, IBillExtendFun
{
  private BillCardPanel ivjBillCardPanel = null;

  private SaleInvoiceListPanel ivjBillListPanel= null;

  private ButtonTree bt = null;

  private FreeItemRefPane ivjFreeItemRefPane = null;
  private ButtonObject m_boBusiType = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000003"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000293"), 1, "业务类型");

  private ButtonObject m_boDocument = new ButtonObject(NCLangRes.getInstance().getStrByID("40060501", "UPT40060501-000057"), NCLangRes.getInstance().getStrByID("40060501", "UPT40060501-000057"), 1, "文档管理");

  private ButtonObject m_boReturn = new ButtonObject(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000464"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000464"), 1, "返回");

  private ButtonObject m_boBrowse = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000021"), NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000020"), 0, "浏览");

  private ButtonObject m_boQuery = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000006"), NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000021"), 0, "查询");

  private ButtonObject m_boLocal = new ButtonObject(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000089"), NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000022"), 0, "定位");

  private ButtonObject m_boFirst = new ButtonObject(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000248"), NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000023"), 0, "首张");

  private ButtonObject m_boPrev = new ButtonObject(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000232"), NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000024"), 0, "上张");

  private ButtonObject m_boNext = new ButtonObject(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000281"), NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000025"), 0, "下张");

  private ButtonObject m_boLast = new ButtonObject(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000177"), NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000026"), 0, "末张");

  private ButtonObject m_boModify = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000045"), NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000027"), 2, "修改");

  private ButtonObject m_boUpdate = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000045"), NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000027"), 2, "修改");

  private ButtonObject m_boSave = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000001"), NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000028"), 3, "保存");

  private ButtonObject m_boCancel = new ButtonObject(NCLangRes.getInstance().getStrByID("40060501", "UPT40060501-000051"), NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000029"), 0, "放弃");

  private ButtonObject m_boPrint = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000007"), NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000030"), 0, "打印");

  private ButtonObject m_boPreview = new ButtonObject(NCLangRes.getInstance().getStrByID("40060501", "UPT40060501-000059"), NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000031"), 0, "预览");

  private ButtonObject m_boHelp = new ButtonObject(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000032"), NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000032"), 0, "帮助");

  private ButtonObject m_boAction = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000026"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000323"), 0, "执行");

  public static ButtonObject m_boBillCombin = new ButtonObject(NCLangRes.getInstance().getStrByID("40060501", "UPT40060501-000058"), NCLangRes.getInstance().getStrByID("40060501", "UPT40060501-000058"), 2);

  private ButtonObject m_boApprove = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000027"), NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000033"), 0, "审批");

  private ButtonObject m_boUnapprove = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000028"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000214"), 0, "弃审");

  private ButtonObject m_boBlankOut = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000005"), NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000034"), 3, "作废");

  private ButtonObject boStockLock = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC000-0001824"), NCLangRes.getInstance().getStrByID("common", "UC000-0001824"), 0, "库存硬锁定");

  private ButtonObject m_boLineOper = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000011"), NCLangRes.getInstance().getStrByID("common", "UC001-0000011"), 0, "行操作");

  private ButtonObject m_boAddLine = new ButtonObject(NCLangRes.getInstance().getStrByID("40060501", "UPT40060501-000052"), NCLangRes.getInstance().getStrByID("40060501", "UPT40060501-000052"), 0, "增加行");

  private ButtonObject m_boDelLine = new ButtonObject(NCLangRes.getInstance().getStrByID("40060501", "UPT40060501-000053"), NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000035"), 0, "删除行");

  private ButtonObject m_boAssistant = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000036"), NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000036"), 0, "辅助");

  private ButtonObject m_boATP = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC000-0001084"), NCLangRes.getInstance().getStrByID("common", "UC000-0001084"), 0, "可用量");

  private ButtonObject m_boCustInfo = new ButtonObject(NCLangRes.getInstance().getStrByID("40060501", "UPT40060501-000054"), NCLangRes.getInstance().getStrByID("40060501", "UPT40060501-000054"), 0, "客户信息");

  private ButtonObject m_boExecRpt = new ButtonObject(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000036"), NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000036"), 0, "发票执行情况");

  private ButtonObject m_boCredit = new ButtonObject(NCLangRes.getInstance().getStrByID("40060501", "UPT40060501-000055"), NCLangRes.getInstance().getStrByID("40060501", "UPT40060501-000055"), 3, "客户信用");

  private ButtonObject m_boPrifit = new ButtonObject(NCLangRes.getInstance().getStrByID("pub_billaction", "D32Prifit"), NCLangRes.getInstance().getStrByID("pub_billaction", "D32Prifit"), 0, "毛利预估");

  private ButtonObject m_boSoTax = new ButtonObject(NCLangRes.getInstance().getStrByID("pub_billaction", "D32SoTax"), NCLangRes.getInstance().getStrByID("pub_billaction", "D32SoTax"), 0, "传金税");

  private ButtonObject m_boCard = new ButtonObject(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000463"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000463"), 2, "卡片");

  private ButtonObject m_boAfterAction = new ButtonObject(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000038"), NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000038"), 0, "后续业务");

  private ButtonObject m_boImpInvoice = new ButtonObject("导入发票号", "导入发票号");

  private ButtonObject boOrderQuery = new ButtonObject(NCLangRes.getInstance().getStrByID("40060501", "UPT40060501-000060"), NCLangRes.getInstance().getStrByID("40060501", "UPT40060501-000060"), 0, "联查");

  protected ButtonObject boSendAudit = new ButtonObject(NCLangRes.getInstance().getStrByID("40060501", "UPT40060501-000061"), NCLangRes.getInstance().getStrByID("40060501", "UPT40060501-000061"), 0, "送审");

  protected ButtonObject boAuditFlowStatus = new ButtonObject(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000240"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000240"), 0, "审批流状态");

  protected ButtonObject boOpposeAct = new ButtonObject(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000155"), NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000155"), 0, "生成对冲发票");

  public String strState = "LIST";
  private Vector vIDs;
  private int id = 0;

  private int selectRow = -1;

  private int m_ordertype = -1;

  private String m_biztype = "";

  public String strShowState = "LIST";
  private double[] orderNumber;
  private double[] packNumber;
  protected UFBoolean SO41 = null;

  private String user = null;

  private String pk_corp = null;

  private ClientEnvironment ce = null;

  private UIMenuItem[] m_bodyMenu = null;

  private UIMenuItem m_boShowFree = new UIMenuItem(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000039"));
  private static final String NO_BUSINESS_TYPE = "KHHH0000000000000001";
  private SaleinvoiceVO saleinvoice = null;

  private SaleVO[] sales = null;

  private UFBoolean SA_02 = null;

  private UFBoolean SA_08 = null;

  private UFBoolean BD302 = null;

  private UFBoolean SA_15 = null;

  private UFDouble SO34 = new UFDouble(0.0D);

  private UFBoolean SO36 = new UFBoolean(true);

  private String SO_06 = null;

  private UFBoolean SO_20 = new UFBoolean("Y");

  protected Integer BD501 = null;

  protected Integer BD502 = null;

  protected Integer BD503 = null;

  protected Integer BD505 = null;
  protected UFBoolean SO59 = null;

  protected BusinessCurrencyRateUtil currtype = null;

  private String m_strWhere = null;

  private int iStatus = 0;

  private String strCurrencyType = null;

  private ArrayList alInvs = new ArrayList();

  private boolean m_isCodeChanged = false;

  private String m_oldreceipt = null;

  protected SaleInvoicePrintDataInterface m_dataSource = null;

  protected PrintEntry m_print = null;

  private boolean bIsPrintDataReady = false;

  int digit = 4;

  SCMQueryConditionDlg dlgQuery = null;

  private ARSubVO[] arsubNew = null;

  protected BillTempletVO billtempletVO = null;

  public boolean bstrikeflag = false;

  QueryConditionClient dlgStrikeQuery = null;

  protected Hashtable hsparas = null;

  public Hashtable hsQueryArsubDataBykey = new Hashtable();

  public Hashtable hsSelectedARSubHVO = new Hashtable();

  public Hashtable hsTotalBykey = new Hashtable();

  private ButtonObject m_boUnite = new ButtonObject(NCLangRes.getInstance().getStrByID("40060501", "UPT40060501-000049"), NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000040"), 0, "合并");

  private ButtonObject m_boUniteCancel = new ButtonObject(NCLangRes.getInstance().getStrByID("40060501", "UPT40060501-000051"), NCLangRes.getInstance().getStrByID("40060501", "UPT40060501-000050"), 0, "放弃");

  private ButtonObject m_boUniteInvoice = new ButtonObject(NCLangRes.getInstance().getStrByID("40060501", "UPT40060501-000048"), NCLangRes.getInstance().getStrByID("40060501", "UPT40060501-000048"), 0, "合并开票");

  UIRadioButton m_rdoAll = new UIRadioButton();

  UIRadioButton m_rdoAudited = new UIRadioButton();

  UIRadioButton m_rdoBlankOut = new UIRadioButton();

  UIRadioButton m_rdoFree = new UIRadioButton();

  public Hashtable oldhsSelectedARSubHVO = new Hashtable();

  public Hashtable oldhsTotalBykey = new Hashtable();

  UFDouble presummoney = null;

  Hashtable presummoneyByProductLine = null;

  private UFDouble SO_22 = new UFDouble(0);

  private UFBoolean SO_27 = null;

  UFDouble strikemoney = null;

  Hashtable strikemoneyByProductLine = null;

  UFDouble summoney = null;

  Hashtable summoneyByProductLine = null;

  SaleInvoiceVOCache vocache = new SaleInvoiceVOCache();

  public UFDouble nUniteInvoiceMnyBeforeChange = null;

  WorkThread work = null;

  ProccDlg proccdlg = null;
  ClientLink m_cl = null;

  private SOBillCardTools soBillCardTools = null;

  protected PrintLogClient printLogClient = null;

  protected HashMap hsBIEnable = new HashMap();

  private void countCardUniteMny()
  {
    for (int i = 0; i < getBillCardPanel().getBillModel().getRowCount(); i++) {
      UFDouble dSubsummny = (UFDouble)getBillCardPanel().getBodyValueAt(i, "nsubsummny");

      UFDouble dOrgcursummny = (UFDouble)getBillCardPanel().getBodyValueAt(i, "noriginalcursummny");

      UFDouble nuniteinvoicemny = (dSubsummny == null ? new UFDouble(0) : dSubsummny).sub(dOrgcursummny == null ? new UFDouble(0) : dOrgcursummny);

      getBillCardPanel().setBodyValueAt(nuniteinvoicemny, i, "nuniteinvoicemny");
    }
  }

  public SaleInvoiceAdminUI()
  {
    initialize();
  }

  public WorkThread getWorkThread() {
    return this.work;
  }

  public void actionPerformed(ActionEvent e)
  {
  }

  protected HashMap getDeleteSaleInvoiceVOs(String tag)
    throws ValidationException
  {
    if ((this.strShowState.equals("LIST")) && (tag != null)) {
      int[] selrow = getBillListPanel().getHeadTable().getSelectedRows();

      if ((selrow == null) || (selrow.length <= 0))
        return null;
      SaleinvoiceVO[] vos = new SaleinvoiceVO[selrow.length];
      HashMap reobj = new HashMap();
      HashMap hs = new HashMap();
      String csaleid = null;
      for (int i = 0; i < selrow.length; i++)
      {
        UFDouble nstrikemny = null;
        nstrikemny = (UFDouble)getBillListPanel().getHeadBillModel().getValueAt(selrow[i], "nstrikemny");

        if ((nstrikemny != null) && (nstrikemny.doubleValue() != 0.0D))
        {
          continue;
        }
        csaleid = (String)getBillListPanel().getHeadBillModel().getValueAt(selrow[i], "csaleid");

        vos[i] = this.vocache.getSaleInvoiceVO(csaleid);

        if (vos[i] == null)
          return null;
        vos[i].setOldSaleOrderVO(vos[i]);
        reobj.put(new Integer(selrow[i]), vos[i]);
        if ((vos[i].getChildrenVO() != null) && (vos[i].getChildrenVO().length > 0))
          continue;
        hs.put(((SaleVO)vos[i].getParentVO()).getCsaleid(), vos[i]);
      }

      if (hs.size() <= 0)
        return reobj;
      String[] hvoids = (String[])hs.keySet().toArray(new String[hs.size()]);
      try
      {
        for (int i = 0; i < hvoids.length; i++) {
          SaleinvoiceBVO[] singlebvos = SaleinvoiceBO_Client.queryBodyData(hvoids[i]);

          ((SaleinvoiceVO)hs.get(hvoids[i])).setChildrenVO(singlebvos);

          ((SaleinvoiceVO)hs.get(hvoids[i])).setOldSaleOrderVO((SaleinvoiceVO)hs.get(hvoids[i]));
        }

      }
      catch (Exception ex)
      {
        SCMEnv.out(ex.getMessage());
        return null;
      }
      return reobj;
    }
    return null;
  }

  protected HashMap getApproveSaleInvoiceVOs(String tag) throws ValidationException
  {
    if ((this.strShowState.equals("LIST")) && (tag != null)) {
      int[] selrow = getBillListPanel().getHeadTable().getSelectedRows();

      if ((selrow == null) || (selrow.length <= 0))
        return null;
      SaleinvoiceVO[] vos = new SaleinvoiceVO[selrow.length];
      HashMap reobj = new HashMap();
      HashMap hs = new HashMap();
      String csaleid = null;
      for (int i = 0; i < selrow.length; i++) {
        csaleid = (String)getBillListPanel().getHeadBillModel().getValueAt(selrow[i], "csaleid");

        vos[i] = this.vocache.getSaleInvoiceVO(csaleid);
        try {
          vos[i].setHsArsubAcct(SaleinvoiceBO_Client.queryStrikeData(csaleid));
        } catch (Exception e) {
          SCMEnv.out(e.getMessage());
        }
        if (vos[i] == null) {
          return null;
        }
        ((SaleVO)vos[i].getParentVO()).setCapproveid(getClientEnvironment().getUser().getPrimaryKey());

        vos[i].setOldSaleOrderVO(vos[i]);
        reobj.put(new Integer(selrow[i]), vos[i]);
        if ((vos[i].getChildrenVO() != null) && (vos[i].getChildrenVO().length > 0))
          continue;
        hs.put(((SaleVO)vos[i].getParentVO()).getCsaleid(), vos[i]);
      }

      if (hs.size() <= 0)
        return reobj;
      String[] hvoids = (String[])hs.keySet().toArray(new String[hs.size()]);
      try
      {
        for (int i = 0; i < hvoids.length; i++) {
          SaleinvoiceBVO[] singlebvos = SaleinvoiceBO_Client.queryBodyData(hvoids[i]);

          ((SaleinvoiceVO)hs.get(hvoids[i])).setChildrenVO(singlebvos);

          ((SaleinvoiceVO)hs.get(hvoids[i])).setOldSaleOrderVO((SaleinvoiceVO)hs.get(hvoids[i]));
        }
      }
      catch (Exception ex) {
        SCMEnv.out(ex.getMessage());
        return null;
      }
      return reobj;
    }
    return null;
  }

  public int onDelete(SaleinvoiceVO vo, String tag) throws Exception {

		if (vo == null)
			return 0;

		SaleinvoiceVO saleinvoice = vo;

		((SaleVO) vo.getParentVO()).setVoldreceiptcode(((SaleVO) vo
				.getParentVO()).getVreceiptcode());

		try {

			PfUtilClient.processActionFlow(this, "SoBlankOut", SaleBillType.SaleInvoice,
					getClientEnvironment().getDate().toString(), vo, null);
			// 更新列表上的原发票的对冲标记
			SaleVO head = (SaleVO) vo.getParentVO();
			SaleinvoiceBVO[] items = (SaleinvoiceBVO[]) vo.getChildrenVO();
			if (head.getFcounteractflag() != null
					&& head.getFcounteractflag().intValue() == 2
					&& items != null && items.length > 0) {
				String coldbillid = items[0].getCupsourcebillid();
				for (int i = 0; i < getBillListPanel().getHeadBillModel()
						.getRowCount(); i++) {
					if (coldbillid.equals(getBillListPanel().getHeadBillModel()
							.getValueAt(i, "csaleid"))) {
						// getBillListPanel().getHeadBillModel().setValueAt(new
						// Integer(1),i,"fcounteractflag");
						//
						String strWhere = "so_saleinvoice.csaleid = '"
								+ coldbillid + "'";
						// 重新加载表头TS
						SaleVO[] salehvo = SaleinvoiceBO_Client
								.queryHeadAllData(strWhere);
						if (salehvo != null && salehvo.length > 0) {
							SaleinvoiceVO oldvo = vocache
									.getSaleInvoiceVO(coldbillid);
							if (oldvo != null) {
								SaleVO oldhead = (SaleVO) oldvo.getParentVO();
								oldhead.setTs(salehvo[0].getTs());
								oldhead.setFcounteractflag(salehvo[0]
										.getFcounteractflag());
								oldvo.setParentVO(oldhead);
								vocache.setSaleinvoiceVO(coldbillid, oldvo);
								// 更改表头数据
								getBillListPanel()
										.getHeadBillModel()
										.setValueAt(salehvo[0].getTs(), i, "ts");
								getBillListPanel()
										.getHeadBillModel()
										.setValueAt(
												salehvo[0].getFcounteractflag(),
												i, "fcounteractflag");
							}
						}
					}
				}
			}

			if (PfUtilClient.isSuccess()) {
				return 1;
			} else {
				return 0;
			}
		} catch (Exception e) {
			throw e;
		}

	}
  public int onApprove(SaleinvoiceVO vo, String tag) throws Exception {

      if (vo == null)
          return 0;

      SaleinvoiceVO saleinvoice = vo;

      if (vo.getChildrenVO() != null) {
          for (int i = 0; i < vo.getChildrenVO().length; i++) {
              vo.getChildrenVO()[i].setStatus(VOStatus.UNCHANGED);
          }
      }
      try {
          onApproveCheckWorkflow(saleinvoice);
          Object otemp = PfUtilClient.processActionFlow(this, tag,
                  SaleBillType.SaleInvoice, getClientEnvironment().getDate()
                          .toString(), saleinvoice, null);
          // onApproveCheck(saleorder);

          // onApproveCheckWorkflow(saleorder);

          // nc.vo.scm.pub.session.ClientLink clientLink = new
          // nc.vo.scm.pub.session.ClientLink(
          //					getClientEnvironment());

          //			saleinvoice.setClientLink(clientLink);

          //			getBillCardTools().getPkarrivecorp(saleorder);

          //			Object otemp = PfUtilClient.processActionFlow(this, tag,
          //					getBillType(), getClientEnvironment().getDate().toString(),
          //					saleorder, null);

          if (otemp != null) {
              String ErrMsg = otemp.toString();
              if (ErrMsg != null && ErrMsg.startsWith("ERR")) {
                  ShowToolsInThread
                          .showMessage(proccdlg, ErrMsg.substring(3));
                  //showWarningMessage(ErrMsg.substring(3));
              }
          }

          if (PfUtilClient.isSuccess()) {
              return 1;
          } else {
              return 0;
          }
      } catch (Exception e) {
          throw e;
      }

  }

  public int onUnApprove(SaleinvoiceVO vo, String tag) throws Exception {
      if (vo == null)
          return 0;
      SaleinvoiceVO saleinvoice = vo;
      if (vo.getChildrenVO() != null) {
          for (int i = 0; i < vo.getChildrenVO().length; i++) {
              vo.getChildrenVO()[i].setStatus(VOStatus.UNCHANGED);
          }
      }
      try {
          PfUtilClient.processActionFlow(this, tag, SaleBillType.SaleInvoice,
                  getClientEnvironment().getDate().toString(), saleinvoice,
                  null);
          if (PfUtilClient.isSuccess()) {
              return 1;
          } else {
              return 0;
          }
      } catch (Exception e) {
          throw e;
      }

  }

  public void doDeleteWork(String tag)
  {

      java.util.HashMap hsvos = null;
      java.util.HashMap hSuccess = new java.util.HashMap();

      ShowToolsInThread.showStatus(proccdlg, new Integer(0));
      try {
          hsvos = getDeleteSaleInvoiceVOs("Del");
          if (hsvos == null || hsvos.size() <= 0) {
              showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
                      "40060501", "UPP40060501-100071")/* @res "请选择待审批的发票！" */);
              return;
          }
      } catch (Exception ev) {
          showErrorMessage(ev.getMessage());
          return;
      }

      int max = proccdlg.getUIProgressBar1().getMaximum();
      int maxcount = hsvos.size();
      
      SaleinvoiceVO saleinvoice = null;
      java.util.Iterator iter = hsvos.keySet().iterator();
      int count = 0;

      while (iter.hasNext()) {
          Object key = iter.next();
          saleinvoice = (SaleinvoiceVO) hsvos.get(key);
          ShowToolsInThread.showStatus(proccdlg, new Integer(
                  (int) (max * (1.0 * count / maxcount))));
          
          saleinvoice.setCuruserid(getClientEnvironment().getUser()
                  .getPrimaryKey());

          try {
              if (work.getRunState() == WorkThread.Interrupted)
                  break;

              if (onDelete(saleinvoice, tag) == 1) {

                  hSuccess.put(key, saleinvoice);
                  String sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID(
                          "40060501",
                          "UPP40060501-100072",
                          null,
                          new String[] { ((SaleVO) saleinvoice.getParentVO())
                                  .getVreceiptcode() });
                  ShowToolsInThread.showMessage(proccdlg, sMsg, sMsg);
              } else {
                  String sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID(
                          "40060501",
                          "UPP40060501-100073",
                          null,
                          new String[] { ((SaleVO) saleinvoice.getParentVO())
                                  .getVreceiptcode() });
                  ShowToolsInThread.showMessage(proccdlg, sMsg, sMsg);
              }

          } catch (BusinessException e) {
              String sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID(
                      "40060501",
                      "UPP40060501-100074",
                      null,
                      new String[] { ((SaleVO) saleinvoice.getParentVO())
                              .getVreceiptcode() });
              sMsg += e.getMessage();
              ShowToolsInThread.showMessage(proccdlg, " ", sMsg);
              if (proccdlg.getckHint().isSelected()) {
                  sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID(
                          "40060501",
                          "UPP40060501-100074",
                          null,
                          new String[] { ((SaleVO) saleinvoice.getParentVO())
                                  .getVreceiptcode() });
                  if (ShowToolsInThread.showYesNoDlg(proccdlg, e.getMessage()
                          + "\n"
                          + sMsg
                          + nc.ui.ml.NCLangRes.getInstance().getStrByID(
                                  "40060501", "UPP40060501-100075")/*
                                                                    * @res
                                                                    * "是否继续审批以下的发票？"
                                                                    */) == MessageDialog.ID_YES) {
                      continue;
                  } else {
                      work.setRunState(WorkThread.Interrupted);
                      break;
                  }
              }

          } catch (Exception e) {
              String sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID(
                      "40060501",
                      "UPP40060501-100074",
                      null,
                      new String[] { ((SaleVO) saleinvoice.getParentVO())
                              .getVreceiptcode() });
              sMsg += e.getMessage();
              ShowToolsInThread.showMessage(proccdlg, " ", sMsg);
              //				ShowToolsInThread.showMessage(proccdlg, " ", "发票["
              //						+ ((SaleVO) saleinvoice.getParentVO())
              //								.getVreceiptcode() + "]审批失败：" + e.getMessage());
              if (proccdlg.getckHint().isSelected()) {
                  sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID(
                          "40060501",
                          "UPP40060501-100074",
                          null,
                          new String[] { ((SaleVO) saleinvoice.getParentVO())
                                  .getVreceiptcode() });
                  if (ShowToolsInThread.showYesNoDlg(proccdlg, e.getMessage()
                          + "\n"
                          + sMsg
                          + nc.ui.ml.NCLangRes.getInstance().getStrByID(
                                  "40060501", "UPP40060501-100075")/*
                                                                    * @res
                                                                    * "是否继续审批以下的发票？"
                                                                    */) == MessageDialog.ID_YES) {
                      continue;
                  } else {
                      work.setRunState(WorkThread.Interrupted);
                      break;
                  }
              }
          } finally {
              count++;
          }
      }

      ShowToolsInThread.showStatus(proccdlg, new Integer(max));

      if (work.getRunState() == work.Interrupted) {
          ShowToolsInThread.showMessage(proccdlg, nc.ui.ml.NCLangRes
                  .getInstance().getStrByID("40060501", "UPP40060501-100069")/*
                                                                              * @res
                                                                              * "审批操作被用户中断！"
                                                                              */);
      } else {
          ShowToolsInThread.showMessage(proccdlg, nc.ui.ml.NCLangRes
                  .getInstance().getStrByID("40060501", "UPP40060501-100070")/*
                                                                              * @res
                                                                              * "审批操作结束！"
                                                                              */);
      }

      try {
          Thread.sleep(500);
      } catch (Exception ex) {

      }

      if (hSuccess.size() > 0) {
          ShowToolsInThread.showMessage(proccdlg, nc.ui.ml.NCLangRes
                  .getInstance().getStrByID("40060501", "UPP40060501-000054")/*
                                                                              * @res
                                                                              * "正在更新界面数据..."
                                                                              */);
          ShowToolsInThread.doLoadAdminData(this);
      }

  
  }
  public void doApproveWork(String tag) {
      java.util.HashMap hsvos = null;
      java.util.HashMap hSuccess = new java.util.HashMap();

      ShowToolsInThread.showStatus(proccdlg, new Integer(0));
      ShowToolsInThread.showMessage(proccdlg, nc.ui.ml.NCLangRes
              .getInstance().getStrByID("40060501", "UPP40060501-000045")/*
                                                                          * @res
                                                                          * "正在进行审批前的准备..."
                                                                          */);
      try {
          hsvos = getApproveSaleInvoiceVOs("APPROVE");
          if (hsvos == null || hsvos.size() <= 0) {
              showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
                      "40060501", "UPP40060501-000046")/* @res "请选择待审批的发票！" */);
              return;
          }
      } catch (Exception ev) {
          showErrorMessage(ev.getMessage());
          return;
      }

      int max = proccdlg.getUIProgressBar1().getMaximum();
      int maxcount = hsvos.size();

//      long s1 = 0;
//      long s = System.currentTimeMillis();

      ShowToolsInThread.showMessage(proccdlg, nc.ui.ml.NCLangRes
              .getInstance().getStrByID("40060501", "UPP40060501-000047")/*
                                                                          * @res
                                                                          * "开始审批..."
                                                                          */);

      SaleinvoiceVO saleinvoice = null;
      java.util.Iterator iter = hsvos.keySet().iterator();
      int count = 0;

      while (iter.hasNext()) {
          Object key = iter.next();
          saleinvoice = (SaleinvoiceVO) hsvos.get(key);
          ShowToolsInThread.showStatus(proccdlg, new Integer(
                  (int) (max * (1.0 * count / maxcount))));
          ShowToolsInThread.showMessage(proccdlg, nc.ui.ml.NCLangRes
                  .getInstance().getStrByID(
                          "40060501",
                          "UPP40060501-000048",
                          null,
                          new String[] { ((SaleVO) saleinvoice.getParentVO())
                                  .getVreceiptcode() }));
          //			ShowToolsInThread.showMessage(proccdlg, "正在审批发票...["
          //					+ ((SaleVO) saleinvoice.getParentVO()).getVreceiptcode()
          //					+ "]");
          saleinvoice.setCuruserid(getClientEnvironment().getUser()
                  .getPrimaryKey());

          try {
              if (work.getRunState() == WorkThread.Interrupted)
                  break;

              if (onApprove(saleinvoice, tag) == 1) {

                  hSuccess.put(key, saleinvoice);
                  String sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID(
                          "40060501",
                          "UPP40060501-000049",
                          null,
                          new String[] { ((SaleVO) saleinvoice.getParentVO())
                                  .getVreceiptcode() });
                  ShowToolsInThread.showMessage(proccdlg, sMsg, sMsg);
                  //					ShowToolsInThread.showMessage(proccdlg, "发票["
                  //							+ ((SaleVO) saleinvoice.getParentVO())
                  //									.getVreceiptcode() + "]审批成功！", "发票["
                  //							+ ((SaleVO) saleinvoice.getParentVO())
                  //									.getVreceiptcode() + "]审批成功！");
                  //}

              } else {
                  String sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID(
                          "40060501",
                          "UPP40060501-000051",
                          null,
                          new String[] { ((SaleVO) saleinvoice.getParentVO())
                                  .getVreceiptcode() });
                  ShowToolsInThread.showMessage(proccdlg, sMsg, sMsg);
                  //					ShowToolsInThread.showMessage(proccdlg, "发票["
                  //							+ ((SaleVO) saleinvoice.getParentVO())
                  //									.getVreceiptcode() + "]" + "审批操作以被用户取消！",
                  //							"发票["
                  //									+ ((SaleVO) saleinvoice.getParentVO())
                  //											.getVreceiptcode() + "]"
                  //									+ "审批操作以被用户取消！");
              }

          } catch (BusinessException e) {
              String sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID(
                      "40060501",
                      "UPP40060501-000052",
                      null,
                      new String[] { ((SaleVO) saleinvoice.getParentVO())
                              .getVreceiptcode() });
              sMsg += e.getMessage();
              ShowToolsInThread.showMessage(proccdlg, " ", sMsg);
              //				ShowToolsInThread.showMessage(proccdlg, " ", "发票["
              //						+ ((SaleVO) saleinvoice.getParentVO())
              //								.getVreceiptcode() + "]审批失败：" + e.getMessage());
              if (proccdlg.getckHint().isSelected()) {
                  sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID(
                          "40060501",
                          "UPP40060501-000052",
                          null,
                          new String[] { ((SaleVO) saleinvoice.getParentVO())
                                  .getVreceiptcode() });
                  if (ShowToolsInThread.showYesNoDlg(proccdlg, e.getMessage()
                          + "\n"
                          + sMsg
                          + nc.ui.ml.NCLangRes.getInstance().getStrByID(
                                  "40060501", "UPP40060501-000053")/*
                                                                    * @res
                                                                    * "是否继续审批以下的发票？"
                                                                    */) == MessageDialog.ID_YES) {
                      continue;
                  } else {
                      work.setRunState(WorkThread.Interrupted);
                      break;
                  }
              }

          } catch (Exception e) {
              String sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID(
                      "40060501",
                      "UPP40060501-000052",
                      null,
                      new String[] { ((SaleVO) saleinvoice.getParentVO())
                              .getVreceiptcode() });
              sMsg += e.getMessage();
              ShowToolsInThread.showMessage(proccdlg, " ", sMsg);
              //				ShowToolsInThread.showMessage(proccdlg, " ", "发票["
              //						+ ((SaleVO) saleinvoice.getParentVO())
              //								.getVreceiptcode() + "]审批失败：" + e.getMessage());
              if (proccdlg.getckHint().isSelected()) {
                  sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID(
                          "40060501",
                          "UPP40060501-000052",
                          null,
                          new String[] { ((SaleVO) saleinvoice.getParentVO())
                                  .getVreceiptcode() });
                  if (ShowToolsInThread.showYesNoDlg(proccdlg, e.getMessage()
                          + "\n"
                          + sMsg
                          + nc.ui.ml.NCLangRes.getInstance().getStrByID(
                                  "40060501", "UPP40060501-000053")/*
                                                                    * @res
                                                                    * "是否继续审批以下的发票？"
                                                                    */) == MessageDialog.ID_YES) {
                      continue;
                  } else {
                      work.setRunState(WorkThread.Interrupted);
                      break;
                  }
              }
          } finally {
              count++;
          }
      }

      ShowToolsInThread.showStatus(proccdlg, new Integer(max));

      if (work.getRunState() == work.Interrupted) {
          ShowToolsInThread.showMessage(proccdlg, nc.ui.ml.NCLangRes
                  .getInstance().getStrByID("40060501", "UPP40060501-000041")/*
                                                                              * @res
                                                                              * "审批操作被用户中断！"
                                                                              */);
      } else {
          ShowToolsInThread.showMessage(proccdlg, nc.ui.ml.NCLangRes
                  .getInstance().getStrByID("40060501", "UPP40060501-000043")/*
                                                                              * @res
                                                                              * "审批操作结束！"
                                                                              */);
      }

      try {
          Thread.sleep(500);
      } catch (Exception ex) {

      }

      if (hSuccess.size() > 0) {
          ShowToolsInThread.showMessage(proccdlg, nc.ui.ml.NCLangRes
                  .getInstance().getStrByID("40060501", "UPP40060501-000054")/*
                                                                              * @res
                                                                              * "正在更新界面数据..."
                                                                              */);
          ShowToolsInThread.doLoadAdminData(this);
      }

  }
  /**
   * 动作执行。 修改，增加净价修改判断的处理，通过捕获异常和设置标志进行 创建日期：(2001-6-1 13:12:36) 修改日期
   * (2003-8-20) 杨波
   */
  public void doUnApproveWork(String tag) {

      java.util.HashMap hsvos = null;
      java.util.HashMap hSuccess = new java.util.HashMap();

      ShowToolsInThread.showStatus(proccdlg, new Integer(0));
      ShowToolsInThread.showMessage(proccdlg, nc.ui.ml.NCLangRes
              .getInstance().getStrByID("40060501", "UPP40060501-000055")/*
                                                                          * @res
                                                                          * "正在进行弃审前的准备..."
                                                                          */);
      try {
          hsvos = getApproveSaleInvoiceVOs("SoUnApprove");
          if (hsvos == null || hsvos.size() <= 0) {
              showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
                      "40060501", "UPP40060501-000056")/* @res "请选择需弃审的发票！" */);
              return;
          }
      } catch (Exception ev) {
          showErrorMessage(ev.getMessage());
          return;
      }

      int max = proccdlg.getUIProgressBar1().getMaximum();
      int maxcount = hsvos.size();

//      long s1 = 0;
//      long s = System.currentTimeMillis();

      ShowToolsInThread.showMessage(proccdlg, nc.ui.ml.NCLangRes
              .getInstance().getStrByID("40060501", "UPP40060501-000057")/*
                                                                          * @res
                                                                          * "开始弃审..."
                                                                          */);

      SaleinvoiceVO saleinvoice = null;
      java.util.Iterator iter = hsvos.keySet().iterator();
      int count = 0;
      boolean isnext = true;

      while (iter.hasNext()) {
          Object key = "";
          if (isnext)
              key = iter.next();
          //Object key = iter.next();
          saleinvoice = (SaleinvoiceVO) hsvos.get(key);
          if (saleinvoice == null) {
              isnext = true;
              continue;
          }

          //            saleinvoice.setCuruserid(getClientEnvironment().getUser()
          //                    .getPrimaryKey());

          ShowToolsInThread.showStatus(proccdlg, new Integer(
                  (int) (max * (1.0 * count / maxcount))));
          ShowToolsInThread.showMessage(proccdlg, nc.ui.ml.NCLangRes
                  .getInstance().getStrByID(
                          "40060501",
                          "UPP40060501-000058",
                          null,
                          new String[] { ((SaleVO) saleinvoice.getParentVO())
                                  .getVreceiptcode() }));
          //			ShowToolsInThread.showMessage(proccdlg, "正在弃审发票...["
          //					+ ((SaleVO) saleinvoice.getParentVO()).getVreceiptcode()
          //					+ "]");
          try {
              if (work.getRunState() == WorkThread.Interrupted)
                  break;
              if (onUnApprove(saleinvoice, tag) == 1) {

                  hSuccess.put(key, saleinvoice);
                  String sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID(
                          "40060501",
                          "UPP40060501-000059",
                          null,
                          new String[] { ((SaleVO) saleinvoice.getParentVO())
                                  .getVreceiptcode() });
                  ShowToolsInThread.showMessage(proccdlg, sMsg, sMsg);
                  //					ShowToolsInThread.showMessage(proccdlg, "发票["
                  //							+ ((SaleVO) saleinvoice.getParentVO())
                  //									.getVreceiptcode() + "]弃审成功！", "发票["
                  //							+ ((SaleVO) saleinvoice.getParentVO())
                  //									.getVreceiptcode() + "]弃审成功！");

              } else {
                  hSuccess.put(key, saleinvoice);
                  String sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID(
                          "40060501",
                          "UPP40060501-000060",
                          null,
                          new String[] { ((SaleVO) saleinvoice.getParentVO())
                                  .getVreceiptcode() });
                  ShowToolsInThread.showMessage(proccdlg, sMsg, sMsg);
                  //					ShowToolsInThread.showMessage(proccdlg, "发票["
                  //							+ ((SaleVO) saleinvoice.getParentVO())
                  //									.getVreceiptcode() + "]" + "弃审操作已被用户取消！",
                  //							"发票["
                  //									+ ((SaleVO) saleinvoice.getParentVO())
                  //											.getVreceiptcode() + "]"
                  //									+ "弃审操作已被用户取消！");
              }
          } catch (BusinessException e) {
              String sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID(
                      "40060501",
                      "UPP40060501-000061",
                      null,
                      new String[] { ((SaleVO) saleinvoice.getParentVO())
                              .getVreceiptcode() });
              sMsg += e.getMessage();
              ShowToolsInThread.showMessage(proccdlg, " ", sMsg);
              //				ShowToolsInThread.showMessage(proccdlg, " ", "发票["
              //						+ ((SaleVO) saleinvoice.getParentVO())
              //								.getVreceiptcode() + "]弃审失败：" + e.getMessage());
              if (proccdlg.getckHint().isSelected()) {
                  sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID(
                          "40060501",
                          "UPP40060501-000061",
                          null,
                          new String[] { ((SaleVO) saleinvoice.getParentVO())
                                  .getVreceiptcode() });
                  if (ShowToolsInThread.showYesNoDlg(proccdlg, sMsg
                          + e.getMessage()
                          + nc.ui.ml.NCLangRes.getInstance().getStrByID(
                                  "40060501", "UPP40060501-000062")/*
                                                                    * @res
                                                                    * "是否继续弃审以下的发票？"
                                                                    */) == MessageDialog.ID_YES) {
                      continue;
                  } else {
                      work.setRunState(WorkThread.Interrupted);
                      break;
                  }
              }

          } catch (Exception e) {
              String sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID(
                      "40060501",
                      "UPP40060501-000061",
                      null,
                      new String[] { ((SaleVO) saleinvoice.getParentVO())
                              .getVreceiptcode() });
              sMsg += e.getMessage();
              ShowToolsInThread.showMessage(proccdlg, " ", sMsg);
              //				ShowToolsInThread.showMessage(proccdlg, " ", "发票["
              //						+ ((SaleVO) saleinvoice.getParentVO())
              //								.getVreceiptcode() + "]弃审失败：" + e.getMessage());
              if (proccdlg.getckHint().isSelected()) {
                  sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID(
                          "40060501",
                          "UPP40060501-000061",
                          null,
                          new String[] { ((SaleVO) saleinvoice.getParentVO())
                                  .getVreceiptcode() });
                  if (ShowToolsInThread.showYesNoDlg(proccdlg, sMsg
                          + e.getMessage()
                          + nc.ui.ml.NCLangRes.getInstance().getStrByID(
                                  "40060501", "UPP40060501-000062")/*
                                                                    * @res
                                                                    * "是否继续弃审以下的发票？"
                                                                    */) == MessageDialog.ID_YES) {
                      continue;
                  } else {
                      work.setRunState(WorkThread.Interrupted);
                      break;
                  }
              }

          } finally {
              count++;
          }

          isnext = true;
      }

      ShowToolsInThread.showStatus(proccdlg, new Integer(max));

      if (work.getRunState() == work.Interrupted) {
          ShowToolsInThread.showMessage(proccdlg, nc.ui.ml.NCLangRes
                  .getInstance().getStrByID("40060501", "UPP40060501-000042")/*
                                                                              * @res
                                                                              * "弃审操作被用户中断！"
                                                                              */);
      } else {
          ShowToolsInThread.showMessage(proccdlg, nc.ui.ml.NCLangRes
                  .getInstance().getStrByID("40060501", "UPP40060501-000044")/*
                                                                              * @res
                                                                              * "弃审操作结束！"
                                                                              */);
      }

      try {
          Thread.sleep(500);
      } catch (Exception ex) {

      }

      if (hSuccess.size() > 0) {
          ShowToolsInThread.showMessage(proccdlg, nc.ui.ml.NCLangRes
                  .getInstance().getStrByID("40060501", "UPP40060501-000054")/*
                                                                              * @res
                                                                              * "正在更新界面数据..."
                                                                              */);
          ShowToolsInThread.doLoadAdminData(this);
      }

  }
  public void afterChangeotoarateEdit(BillEditEvent e)
  {
    for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
      getBillCardPanel().setBodyValueAt(getBillCardPanel().getHeadItem("nexchangeotoarate").getValue(), i, "nexchangeotoarate");

      calculateNumber(i, "noriginalcursummny");

      getBillCardPanel().setBodyValueAt(getBillCardPanel().getBodyValueAt(i, "nquoteprice"), i, "nsubquoteprice");

      getBillCardPanel().setBodyValueAt(getBillCardPanel().getBodyValueAt(i, "nquotetaxprice"), i, "nsubquotetaxprice");

      getBillCardPanel().setBodyValueAt(getBillCardPanel().getBodyValueAt(i, "nquotenetprice"), i, "nsubquotenetprice");

      getBillCardPanel().setBodyValueAt(getBillCardPanel().getBodyValueAt(i, "nquotetaxnetprice"), i, "nsubquotetaxnetprice");

      getBillCardPanel().setBodyValueAt(getBillCardPanel().getBodyValueAt(i, "ntaxnetprice"), i, "nsubtaxnetprice");

      setBodyRowState(i);
    }
  }

  protected SCMQueryConditionDlg getQueryDlg() {
    if (this.dlgQuery == null) {
      this.dlgQuery = new SCMQueryConditionDlg(this);
      this.dlgQuery.setTempletID(getCorpPrimaryKey(), getNodeCode(), getClientEnvironment().getUser().getPrimaryKey(), getBillCardPanel().getBusiType(), getNodeCode());

      this.dlgQuery.hideUnitButton();
      this.dlgQuery.readTempletDate();
      QueryConditionVO[] vos = this.dlgQuery.getConditionDatas();

      if (vos != null) {
        UIRefPane ref = null;
        for (int i = 0; i < vos.length; i++) {
          if (vos[i].getFieldCode().indexOf("dbilldate") >= 0) {
            vos[i].setValue(getClientEnvironment().getDate().toString());
          }

          if ((vos[i].getTableName() == null) || (!vos[i].getTableName().equals("bd_cumandoc")) || (!vos[i].getTableCode().startsWith("def"))) {
            continue;
          }
          vos[i].setFieldCode(vos[i].getTableName() + "." + vos[i].getTableCode());

          vos[i].setReturnType(new Integer(1));
        }

      }

      SOBusiTypeRefPane biztypeRef = new SOBusiTypeRefPane(getCorpPrimaryKey());

      this.dlgQuery.setValueRef("so_saleinvoice.cbiztype", biztypeRef);

      this.m_rdoAll.setText(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000217"));

      this.m_rdoAll.setBackground(getBackground());
      this.m_rdoAll.setForeground(Color.black);
      this.m_rdoAll.setSize(80, this.m_rdoAll.getHeight());

      this.m_rdoFree.setText(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000063"));

      this.m_rdoFree.setBackground(this.m_rdoAll.getBackground());
      this.m_rdoFree.setForeground(this.m_rdoAll.getForeground());
      this.m_rdoFree.setSize(this.m_rdoAll.getSize());

      this.m_rdoAudited.setText(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000301"));

      this.m_rdoAudited.setBackground(this.m_rdoAll.getBackground());
      this.m_rdoAudited.setForeground(this.m_rdoAll.getForeground());
      this.m_rdoAudited.setSize(this.m_rdoAll.getSize());

      this.m_rdoAll.setLocation(50, 30);
      this.m_rdoFree.setLocation(this.m_rdoAll.getX(), this.m_rdoAll.getY() + this.m_rdoAll.getHeight() + 20);

      this.m_rdoAudited.setLocation(this.m_rdoFree.getX(), this.m_rdoFree.getY() + this.m_rdoFree.getHeight() + 20);

      ButtonGroup bg = new ButtonGroup();
      bg.add(this.m_rdoAll);
      bg.add(this.m_rdoFree);
      bg.add(this.m_rdoAudited);

      bg.setSelected(this.m_rdoFree.getModel(), true);
      this.dlgQuery.getUIPanelNormal().setLayout(null);

      this.dlgQuery.getUIPanelNormal().add(this.m_rdoAll);
      this.dlgQuery.getUIPanelNormal().add(this.m_rdoFree);
      this.dlgQuery.getUIPanelNormal().add(this.m_rdoAudited);

      this.dlgQuery.setIsWarningWithNoInput(true);
      DefSetTool.updateQueryConditionClientUserDef(this.dlgQuery, getCorpPrimaryKey(), getBillCode(), "so_saleinvoice.vdef", null, 0, "so_saleexecute.vdef", null, 0);

      DefSetTool.updateQueryConditionForCumandoc(this.dlgQuery, getCorpPrimaryKey(), "bd_cumandoc.def");

      this.dlgQuery.setNormalShow(true);
      this.dlgQuery.setShowPrintStatusPanel(true);
      this.dlgQuery.setDataPower(true, this.ce.getCorporation().getPk_corp());
    }

    return this.dlgQuery;
  }

  public void afterChangeotobrateEdit(BillEditEvent e)
  {
    for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
      getBillCardPanel().setBodyValueAt(getBillCardPanel().getHeadItem("nexchangeotobrate").getValue(), i, "nexchangeotobrate");

      calculateNumber(i, "noriginalcursummny");

      getBillCardPanel().setBodyValueAt(getBillCardPanel().getBodyValueAt(i, "nquoteprice"), i, "nsubquoteprice");

      getBillCardPanel().setBodyValueAt(getBillCardPanel().getBodyValueAt(i, "nquotetaxprice"), i, "nsubquotetaxprice");

      getBillCardPanel().setBodyValueAt(getBillCardPanel().getBodyValueAt(i, "nquotenetprice"), i, "nsubquotenetprice");

      getBillCardPanel().setBodyValueAt(getBillCardPanel().getBodyValueAt(i, "nquotetaxnetprice"), i, "nsubquotetaxnetprice");

      getBillCardPanel().setBodyValueAt(getBillCardPanel().getBodyValueAt(i, "ntaxnetprice"), i, "nsubtaxnetprice");

      setBodyRowState(i);
    }
  }

  public void afterCreceiptcorpEdit(BillEditEvent e)
  {
    UIRefPane vreceiveaddress = (UIRefPane)getBillCardPanel().getHeadItem("vreceiveaddress").getComponent();

    vreceiveaddress.setAutoCheck(false);

    String formula = "getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,ccustomername)";
    String pk_cubasdoc = (String)getBillCardPanel().execHeadFormula(formula);

    String strvreceiveaddress = nc.ui.so.so001.panel.bom.BillTools.getColValue2("bd_custaddr", "pk_custaddr", "defaddrflag", "Y", "pk_cubasdoc", pk_cubasdoc);

    vreceiveaddress.setPK(strvreceiveaddress);
  }

  public void afterCurrencyChange(String billdate)
  {
    UIRefPane ccurrencytypeid = (UIRefPane)getBillCardPanel().getHeadItem("ccurrencyid").getComponent();
    try
    {
      if ((this.BD302 == null) || (!this.BD302.booleanValue()))
      {
        getBillCardPanel().setHeadItem("nexchangeotobrate", this.currtype.getRate(ccurrencytypeid.getRefPK(), null, getBillCardPanel().getHeadItem("dbilldate").getValue()));

        if (this.currtype.isLocalCurrType(ccurrencytypeid.getRefPK())) {
          getBillCardPanel().getHeadItem("nexchangeotobrate").setEnabled(false);
        }
        else {
          getBillCardPanel().getHeadItem("nexchangeotobrate").setEnabled(true);
        }

        getBillCardPanel().getHeadItem("nexchangeotoarate").setEnabled(false);
      }
      else
      {
        UFDouble dCurr0 = this.currtype.getRate(ccurrencytypeid.getRefPK(), this.currtype.getLocalCurrPK(), getBillCardPanel().getHeadItem("dbilldate").getValue());

        UFDouble dCurr1 = this.currtype.getRate(ccurrencytypeid.getRefPK(), this.currtype.getFracCurrPK(), getBillCardPanel().getHeadItem("dbilldate").getValue());

        getBillCardPanel().setHeadItem("nexchangeotobrate", dCurr0);
        getBillCardPanel().setHeadItem("nexchangeotoarate", dCurr1 == null ? new UFDouble(0) : dCurr1);

        SCMEnv.out("折本汇率：<#" + dCurr0 + "#>");

        SCMEnv.out("折辅汇率：<#" + dCurr1 + "#>");

        if (this.currtype.isFracCurrType(ccurrencytypeid.getRefPK())) {
          getBillCardPanel().getHeadItem("nexchangeotoarate").setEnabled(false);

          getBillCardPanel().getHeadItem("nexchangeotobrate").setEnabled(true);
        }
        else if (this.currtype.isLocalCurrType(ccurrencytypeid.getRefPK()))
        {
          getBillCardPanel().getHeadItem("nexchangeotobrate").setEnabled(false);

          getBillCardPanel().getHeadItem("nexchangeotoarate").setEnabled(false);
        }
        else
        {
          getBillCardPanel().getHeadItem("nexchangeotobrate").setEnabled(true);

          getBillCardPanel().getHeadItem("nexchangeotoarate").setEnabled(true);
        }

      }

    }
    catch (Exception e1)
    {
      SCMEnv.out("获得汇率失败！");
      e1.printStackTrace();
    }
  }

  public void afterEdit(BillEditEvent e)
  {
    if (e.getPos() == 0) {
      if (e.getKey().equals("vreceiptcode")) {
        this.m_isCodeChanged = true;
      }

      if (e.getKey().equals("ndiscountrate")) {
        afterDiscountrateEdit(e);
      }
      if (e.getKey().equals("ccustomername")) {
        UIRefPane ref = (UIRefPane)getBillCardPanel().getHeadItem("ccustomername").getComponent();

        if (ref != null)
        {
          String custID = ref.getRefPK();
          getBillCardPanel().setHeadItem("creceiptcorpid", custID);
          try {
            String[][] results = SaleinvoiceBO_Client.getCustomerInfo(custID);

            if (results != null) {
              ((CustBankRefModel)((UIRefPane)getBillCardPanel().getHeadItem("ccustomerbank").getComponent()).getRefModel()).setCondition(results[0][7]);

              getBillCardPanel().setHeadItem("ccustomertel", results[0][0]);

              getBillCardPanel().setHeadItem("ccustomertaxNo", results[0][1]);

              getBillCardPanel().setHeadItem("ccustbankid", results[0][4]);

              getBillCardPanel().setHeadItem("ccustomerbank", results[0][4]);

              getBillCardPanel().setHeadItem("ccustomerbankNo", results[0][3]);

              getBillCardPanel().setHeadItem("vprintcustname", results[0][6]);

              getBillCardPanel().setHeadItem("vreceiveaddress", results[0][7]);
            }
            else {
              getBillCardPanel().setHeadItem("ccustomertel", "");
              getBillCardPanel().setHeadItem("ccustomertaxNo", "");

              getBillCardPanel().setHeadItem("ccustomerbankNo", "");

              getBillCardPanel().setHeadItem("vreceiveaddress", "");
            }
          }
          catch (Exception e1) {
            SCMEnv.out("查询失败！");
            e1.printStackTrace(System.out);
          }

          String formula = "getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,creceiptcorpid)";
          String custID2 = (String)getBillCardPanel().execHeadFormula(formula);
          formula = "bfreecustflag->getColValue(bd_cubasdoc,freecustflag,pk_cubasdoc,\"" + custID2 + "\")";

          getBillCardPanel().getBillData().execHeadFormula(formula);
          if (getBillCardPanel().getHeadItem("bfreecustflag").getValue().equals("false"))
          {
            getBillCardPanel().getHeadItem("cfreecustid").setEnabled(false);
            getBillCardPanel().getHeadItem("cfreecustid").setValue(null);
          } else {
            getBillCardPanel().getHeadItem("cfreecustid").setEnabled(true);
            getBillCardPanel().getHeadItem("cfreecustid").setEdit(true);
          }

          afterCreceiptcorpEdit(e);
        }
      }

      if (e.getKey().equals("ccustomerbank")) {
        UIRefPane ref = (UIRefPane)getBillCardPanel().getHeadItem("ccustomerbank").getComponent();

        if (ref != null)
        {
          String id = ref.getRefPK();
          getBillCardPanel().setHeadItem("ccustbankid", id);

          String code = ref.getRefCode();
          getBillCardPanel().setHeadItem("ccustomerbankNo", code);
        }
      }

      if (e.getKey().equals("nexchangeotobrate")) {
        afterChangeotobrateEdit(e);
      }

      if (e.getKey().equals("nexchangeotoarate")) {
        afterChangeotoarateEdit(e);
      }

      int defindex = 1;
      if (e.getKey().indexOf("vdef") >= 0) {
        for (defindex = 1; defindex <= 20; defindex++) {
          if (("vdef" + defindex).equals(e.getKey())) {
            DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef" + defindex, "pk_defdoc" + defindex);

            break;
          }
        }
      }
    }
    if (e.getPos() == 1)
    {
      if (e.getKey().equals("nuniteinvoicemny")) {
        afterNuniteinvoicemnyEdit(e);
        return;
      }

      if (e.getKey().equals("cinventorycode")) {
        afterInventoryEdit(e);
      }

      if (e.getKey().equals("cpackunitname")) {
        afterUnitEdit(e);
      }

      afterNumberEdit(e);

      if (e.getKey().equals("vfree0")) {
        afterFreeItemEdit(e);
      }

      if (e.getKey().equals("cbodywarehousename")) {
        afterWarehouseEdit(e);
      }

      if (e.getKey().equals(getRowNoItemKey()))
      {
        BillRowNo.afterEditWhenRowNo(getBillCardPanel(), e, getBillCode());
      }

      getBillCardPanel().setBodyValueAt(getBillCardPanel().getBodyValueAt(e.getRow(), "ntaxnetprice"), e.getRow(), "nsubtaxnetprice");

      int defindex = 1;
      if (e.getKey().indexOf("vdef") >= 0)
        for (defindex = 1; defindex <= 20; defindex++)
          if (("vdef" + defindex).equals(e.getKey())) {
            DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), e.getRow(), "vdef" + defindex, "pk_defdoc" + defindex);

            break;
          }
    }
  }

  public void afterFreeItemEdit(BillEditEvent e)
  {
    try
    {
      FreeVO voFree = getFreeItemRefPane().getFreeVO();

      for (int i = 0; i < 5; i++) {
        String fieldname = "vfree" + i;
        Object o = voFree.getAttributeValue(fieldname);
        getBillCardPanel().setBodyValueAt(o, e.getRow(), fieldname);
      }
    } catch (Exception e2) {
      e2.printStackTrace();
    }
  }

  public void afterInventoryEdit(BillEditEvent e)
  {
    String[] clearCol = { "creceipttype", "csourcebillid", "csourcebillbodyid", "npacknumber", "noriginalcurprice", "noriginalcurtaxprice", "noriginalcurnetprice", "noriginalcurtaxnetprice", "noriginalcurtaxmny", "noriginalcurmny", "noriginalcursummny", "noriginalcurdiscountmny" };

    getBillCardPanel().clearRowData(e.getRow(), clearCol);
    getBillCardPanel().execBodyFormulas(e.getRow(), new String[] { "cprolineid->getColValue(bd_invbasdoc,pk_prodline,pk_invbasdoc,cinvbasdocid)", "cprolinename->getColValue(bd_prodline,prodlinename,pk_prodline,cprolineid)" });
    try
    {
      String sTempID1 = (String)getBillCardPanel().getBodyValueAt(e.getRow(), "cinventoryid");

      String sTempID2 = null;

      ArrayList alIDs = new ArrayList();
      alIDs.add(sTempID2);
      alIDs.add(sTempID1);
      alIDs.add(getClientEnvironment().getUser().getPrimaryKey());
      alIDs.add(getCorpPrimaryKey());

      InvVO voInv = nc.ui.so.pub.BillTools.queryInfo(new Integer(0), alIDs);

      this.alInvs.set(e.getRow(), voInv);

      SCMEnv.out("自定义设置成功!");
    } catch (Exception ex) {
      ex.printStackTrace();
      SCMEnv.out("自定义设置失败!");
    }

    if (setAssistUnit(e.getRow())) {
      afterUnitEdit(e);
    }
    getBillCardPanel().setBodyValueAt(this.SO34, getBillCardPanel().getBillModel().getRowCount() - 1, "nnumber");

    getBillCardPanel().setBodyValueAt(this.SO34, getBillCardPanel().getBillModel().getRowCount() - 1, "nquotenumber");

    String cunitid = (String)getBillCardPanel().getBodyValueAt(e.getRow(), "cunitid");
    getBillCardPanel().setBodyValueAt(cunitid, e.getRow(), "cquoteunitid");
    getBillCardPanel().setBodyValueAt(new UFDouble(1), e.getRow(), "nqtscalefactor");
  }

  public void afterNumberEdit(BillEditEvent e)
  {
    String cpackunitid = (String)getBillCardPanel().getBodyValueAt(e.getRow(), "cpackunitid");

    if ((e.getKey().equals("cpackunitname")) && ((cpackunitid == null) || (cpackunitid.length() == 0)))
    {
      getBillCardPanel().setBodyValueAt(null, e.getRow(), "npacknumber");
      return;
    }

    if (e.getKey().equals("nnumber"))
    {
      int row = e.getRow();
      if (setAssistUnit(row)) {
        if ((getBillCardPanel().getBodyValueAt(row, "fixedflag") != null) && (getBillCardPanel().getBodyValueAt(row, "fixedflag").equals(new Boolean(true))))
        {
          if ((getBillCardPanel().getBodyValueAt(row, "nnumber") != null) && (getBillCardPanel().getBodyValueAt(row, "nnumber").toString().length() == 0))
          {
            getBillCardPanel().setBodyValueAt(null, row, "npacknumber");
          }
          else {
            String[] formula = { "npacknumber->iif(nnumber=NULL,0,iif(nnumber=\"\",0,nnumber))/scalefactor" };
            getBillCardPanel().getBillModel().execFormula(row, formula);
          }
        }

        if ((getBillCardPanel().getBodyValueAt(row, "fixedflag") == null) || (getBillCardPanel().getBodyValueAt(row, "fixedflag").equals(new Boolean(false))))
        {
          UFDouble nnumber = null;
          UFDouble npacknumber = null;

          if (getBillCardPanel().getBodyValueAt(row, "nnumber") != null) {
            nnumber = new UFDouble(getBillCardPanel().getBodyValueAt(row, "nnumber").toString(), this.BD501.intValue());
          }

          if (getBillCardPanel().getBodyValueAt(row, "npacknumber") != null) {
            npacknumber = new UFDouble(getBillCardPanel().getBodyValueAt(row, "npacknumber").toString(), this.BD502.intValue());
          }

          if ((npacknumber != null) && (npacknumber.doubleValue() != 0.0D)) {
            getBillCardPanel().setBodyValueAt(new UFDouble(nnumber.div(npacknumber).toString(), this.BD503.intValue()).toString(), e.getRow(), "scalefactor");
          }
        }

      }

    }

    if (e.getKey().equals("npacknumber")) {
      String[] formula = { "nnumber->iif(npacknumber=NULL,0,iif(npacknumber=\"\",0,npacknumber))*scalefactor" };
      getBillCardPanel().getBillModel().execFormula(e.getRow(), formula);
    }

    if (e.getKey().equals("cpackunitname")) {
      UFDouble dPackNum = new UFDouble(getBillCardPanel().getBodyValueAt(e.getRow(), "npacknumber") == null ? "0" : getBillCardPanel().getBodyValueAt(e.getRow(), "npacknumber").toString());

      String sNum = getBillCardPanel().getBodyValueAt(e.getRow(), "nnumber") == null ? "" : getBillCardPanel().getBodyValueAt(e.getRow(), "nnumber").toString().trim();

      if (((dPackNum == null) || (dPackNum.doubleValue() == 0.0D)) && (sNum.length() != 0))
      {
        if (getBillCardPanel().getBodyValueAt(e.getRow(), "scalefactor") != null)
        {
          String[] formula = { "npacknumber->iif(nnumber=NULL,0,iif(nnumber=\"\",0,nnumber))/scalefactor" };
          getBillCardPanel().getBillModel().execFormula(e.getRow(), formula);
        }
      }
      else {
        String[] formula = { "nnumber->iif(npacknumber=NULL,0,iif(npacknumber=\"\",0,npacknumber))*scalefactor" };
        getBillCardPanel().getBillModel().execFormula(e.getRow(), formula);
      }
    }

    calculateNumber(e.getRow(), e.getKey());

    UFDouble dbTemp = computeViaPrice(e.getRow());
    if (dbTemp != null) {
      getBillCardPanel().getBillModel().setValueAt(dbTemp, e.getRow(), "norgviaprice");
    }

    UFDouble dbTempTax = computeViaPriceTax(e.getRow());
    if (dbTempTax != null) {
      getBillCardPanel().getBillModel().setValueAt(dbTempTax, e.getRow(), "norgviapricetax");
    }

    getBillCardPanel().setHeadItem("ntotalsummny", calcurateTotal("noriginalcursummny"));

    getBillCardPanel().execHeadFormula("nnetmny->ntotalsummny-nstrikemny");
    getBillCardPanel().setBodyValueAt(getBillCardPanel().getBodyValueAt(e.getRow(), "noriginalcursummny"), e.getRow(), "nsubsummny");

    getBillCardPanel().setBodyValueAt(getBillCardPanel().getBodyValueAt(e.getRow(), "nsummny"), e.getRow(), "nsubcursummny");
  }

  public void afterUnitChange(int row)
  {
    String[] formulas = new String[3];

    formulas[0] = "cpackunitname->getColValue(bd_measdoc,measname,pk_measdoc,cpackunitid)";

    formulas[1] = "scalefactor->getColValue2(bd_convert,mainmeasrate,pk_invbasdoc,cinvbasdocid,pk_measdoc,cpackunitid)";

    formulas[2] = "fixedflag->getColValue2(bd_convert,fixedflag,pk_invbasdoc,cinvbasdocid,pk_measdoc,cpackunitid)";

    getBillCardPanel().execBodyFormulas(row, formulas);
  }

  public void afterUnitEdit(BillEditEvent e)
  {
    String[] formulas = new String[3];

    formulas[0] = "cpackunitname->getColValue(bd_measdoc,measname,pk_measdoc,cpackunitid)";

    formulas[1] = "scalefactor->getColValue2(bd_convert,mainmeasrate,pk_invbasdoc,cinvbasdocid,pk_measdoc,cpackunitid)";

    formulas[2] = "fixedflag->getColValue2(bd_convert,fixedflag,pk_invbasdoc,cinvbasdocid,pk_measdoc,cpackunitid)";

    getBillCardPanel().execBodyFormulas(e.getRow(), formulas);
  }

  public void afterWarehouseEdit(BillEditEvent e)
  {
    String[] formulas = new String[1];

    formulas[0] = "cbodywarehousename->getColValue(bd_stordoc,storname,pk_stordoc,cbodywarehouseid)";
    getBillCardPanel().execBodyFormulas(e.getRow(), formulas);
  }

  public boolean beforeEdit(BillEditEvent e)
    throws BusinessRuntimeException
  {
    if (e.getPos() == 1) {
      if (e.getKey().equals("nuniteinvoicemny"))
      {
        if (this.bstrikeflag) {
          this.nUniteInvoiceMnyBeforeChange = ((UFDouble)getBillCardPanel().getBodyValueAt(e.getRow(), "nuniteinvoicemny"));

          UFBoolean blargessflag = new UFBoolean(getBillCardPanel().getBodyValueAt(e.getRow(), "blargessflag").toString());

          String sInvbasid = (String)getBillCardPanel().getBodyValueAt(e.getRow(), "cinvbasdocid");

          return (!blargessflag.booleanValue()) && (!isLaborOrDiscount(sInvbasid));
        }

        if (!this.bstrikeflag)
          return false;
      } else if (this.bstrikeflag) {
        return false;
      }
    }

    if (e.getKey().equals("npacknumber")) {
      String cpackunitid = (String)getBillCardPanel().getBodyValueAt(e.getRow(), "cpackunitid");

      if ((cpackunitid == null) || (cpackunitid.length() == 0)) {
        getBillCardPanel().setCellEditable(e.getRow(), "npacknumber", false);
      }
      else {
        getBillCardPanel().setCellEditable(e.getRow(), "npacknumber", true);
      }
    }

    if (e.getKey().equals("cpackunitname")) {
      UIRefPane cpackunitname = (UIRefPane)getBillCardPanel().getBodyItem("cpackunitname").getComponent();

      String cinvbasdocid = (String)getBillCardPanel().getBodyValueAt(e.getRow(), "cinvbasdocid");

      String cunitid = (String)getBillCardPanel().getBodyValueAt(e.getRow(), "cunitid");

      cpackunitname.setWhereString(" ( pk_measdoc in (select pk_measdoc from bd_convert where pk_invbasdoc = '" + cinvbasdocid + "') or pk_measdoc='" + cunitid + "' ) ");
    }

    if (e.getKey().equals("vfree0")) {
      try
      {
        InvVO voInv = (InvVO)this.alInvs.get(e.getRow());

        setBodyFreeValue(e.getRow(), voInv);
        getFreeItemRefPane().setFreeItemParam(voInv);
      } catch (Exception ex) {
        SCMEnv.out("自由项设置失败!");
      }

    }

    if (e.getKey().equals("blargessflag")) {
      getBillCardPanel().setCellEditable(e.getRow(), "blargessflag", false);
    }
    return true;
  }

  public void beforeUnitChange(int row)
  {
    UIRefPane cpackunitname = (UIRefPane)getBillCardPanel().getBodyItem("cpackunitname").getComponent();

    String cinvbasdocid = (String)getBillCardPanel().getBodyValueAt(row, "cinvbasdocid");

    String cunitid = (String)getBillCardPanel().getBodyValueAt(row, "cunitid");

    cpackunitname.setWhereString(" (pk_measdoc in (select pk_measdoc from bd_convert where pk_invbasdoc = '" + cinvbasdocid + "') or pk_measdoc='" + cunitid + "' ) ");
  }

  public void bodyRowChange(BillEditEvent e)
    throws BusinessRuntimeException
  {
    if (this.strShowState.equals("LIST")) {
      this.selectRow = e.getRow();

      String csaleid = (String)getBillListPanel().getHeadBillModel().getValueAt(this.selectRow, "csaleid");

      SaleinvoiceVO vo = this.vocache.getSaleInvoiceVO(csaleid);
      if ((vo == null) || (vo.getParentVO() == null)) {
        return;
      }
      if (vo.getChildrenVO() == null) {
        getBillListPanel().loadBodyData(this.selectRow);
        SaleinvoiceBVO[] bvos = (SaleinvoiceBVO[])getBillListPanel().getBodyBillModel().getBodyValueVOs("nc.vo.so.so002.SaleinvoiceBVO");

        vo.setChildrenVO(bvos);
      } else {
        getBillListPanel().setPanelByCurrency(((SaleinvoiceBVO)vo.getChildrenVO()[0]).getCcurrencytypeid());

        getBillListPanel().setBodyValueVO(vo.getChildrenVO());
        getBillListPanel().getBodyBillModel().execLoadFormula();
      }

      for (int i = 0; i < getBillListPanel().getBodyBillModel().getRowCount(); )
      {
        UFDouble dSubsummny = (UFDouble)getBillListPanel().getBodyBillModel().getValueAt(i, "nsubsummny");

        UFDouble dNoriginalcursummny = (UFDouble)getBillListPanel().getBodyBillModel().getValueAt(i, "noriginalcursummny");

        UFDouble nuniteinvoicemny = (dSubsummny == null ? new UFDouble(0) : dSubsummny).sub(dNoriginalcursummny == null ? new UFDouble(0) : dNoriginalcursummny);

        getBillListPanel().getBodyBillModel().setValueAt(nuniteinvoicemny, i, "nuniteinvoicemny");

        i++;
      }

      Object tStrikemny = getBillListPanel().getHeadBillModel().getValueAt(this.selectRow, "nstrikemny");

      if ((tStrikemny == null) || (new UFDouble(tStrikemny.toString()).doubleValue() == 0.0D))
      {
        this.bstrikeflag = false;
      }
      else this.bstrikeflag = true;

      setButtonsState();
    }
    else {
      int iStatus = Integer.parseInt(getBillCardPanel().getHeadItem("fstatus").getValue());

      if (e.getRow() > -1) {
        Object cfreezeid = getBillCardPanel().getBodyValueAt(e.getRow(), "cfreezeid");

        if ((cfreezeid != null) && (cfreezeid.toString().trim().length() != 0))
        {
          this.boStockLock.setEnabled(false);
          lockRow(e.getRow());
        }
        else if (iStatus == 2) {
          this.boStockLock.setEnabled(true);
        } else {
          this.boStockLock.setEnabled(false);
        }
        updateButton(this.boStockLock);
      }
    }
  }

  private void calculateNumber(int row, String key)
  {
    nc.ui.so.so001.panel.bom.BillTools.calcEditFun(getBillCardPanel().getHeadItem("dbilldate").getValue(), this.BD505, this.SA_02.booleanValue(), key, row, getBillCardPanel());
  }

  public UFDouble calcurateTotal(String key)
  {
    UFDouble total = new UFDouble(0.0D);
    if (this.strShowState.equals("CARD")) {
      for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
        if ((getBillCardPanel().getBodyValueAt(i, "blargessflag") != null) && (!getBillCardPanel().getBodyValueAt(i, "blargessflag").equals(new Boolean(false)))) {
          continue;
        }
        Object value = getBillCardPanel().getBodyValueAt(i, key);
        String v = (value == null) || (value.equals("")) ? "0" : value.toString();

        total = total.add(new UFDouble(v));
      }
    }
    else {
      for (int i = 0; i < getBillListPanel().getBodyBillModel().getRowCount(); )
      {
        if ((getBillListPanel().getBodyBillModel().getValueAt(i, "blargessflag") == null) || (getBillListPanel().getBodyBillModel().getValueAt(i, "blargessflag").equals(new Boolean(false))))
        {
          Object value = getBillListPanel().getBodyBillModel().getValueAt(i, key);

          String v = (value == null) || (value.equals("")) ? "0" : value.toString();

          total = total.add(new UFDouble(v));
        }
        i++;
      }

    }

    return total;
  }

  private UFDouble computeViaPrice(int iRow)
  {
    Object obj1 = getBillCardPanel().getBodyValueAt(iRow, "noriginalcurtaxprice");

    Object obj2 = getBillCardPanel().getBodyValueAt(iRow, "ntaxrate");

    Object obj3 = getBillCardPanel().getBodyValueAt(iRow, "nnumber");

    Object obj4 = getBillCardPanel().getBodyValueAt(iRow, "npacknumber");

    if ((obj1 != null) && (obj2 != null) && (obj3 != null) && (obj4 != null)) {
      UFDouble dTaxRate = ((UFDouble)obj2).div(100.0D).add(1.0D);

      return ((UFDouble)obj1).div(dTaxRate).multiply(((UFDouble)obj3).div((UFDouble)obj4));
    }

    return null;
  }

  public SaleinvoiceVO[] getAllVO()
  {
    SaleinvoiceVO[] hvos = (SaleinvoiceVO[])null;
    SaleinvoiceVO saleinvoice = null;
    try {
      if (this.strShowState.equals("LIST")) {
        String[] ids = new String[getBillListPanel().getHeadBillModel().getRowCount()];

        String saleid = null;
        for (int i = 0; i < getBillListPanel().getHeadBillModel().getRowCount(); )
        {
          saleid = getBillListPanel().getHeadBillModel().getValueAt(i, "csaleid").toString();

          ids[i] = saleid;

          i++;
        }

        hvos = SaleinvoiceBO_Client.queryDataBills(ids);

        for (int j = 0; j < hvos.length; j++) {
          saleinvoice = hvos[j];

          ((SaleVO)saleinvoice.getParentVO()).setCreceipttype("32");

          ((SaleVO)saleinvoice.getParentVO()).setPk_corp(getCorpPrimaryKey());

          if (saleinvoice.getChildrenVO() != null) {
            for (int i = 0; i < saleinvoice.getChildrenVO().length; i++)
            {
              ((SaleinvoiceBVO)saleinvoice.getChildrenVO()[i]).setPk_corp(getCorpPrimaryKey());
            }

          }

        }

      }
      else
      {
        saleinvoice = (SaleinvoiceVO)getBillCardPanel().getBillValueVO(SaleinvoiceVO.class.getName(), SaleVO.class.getName(), SaleinvoiceBVO.class.getName());

        ((SaleVO)saleinvoice.getParentVO()).setPk_corp(getCorpPrimaryKey());

        if (saleinvoice.getChildrenVO() != null) {
          for (int i = 0; i < saleinvoice.getChildrenVO().length; i++)
          {
            ((SaleinvoiceBVO)saleinvoice.getChildrenVO()[i]).setPk_corp(getCorpPrimaryKey());
          }

        }

        ((SaleVO)saleinvoice.getParentVO()).setCreceipttype("32");

        ((SaleVO)saleinvoice.getParentVO()).setPrimaryKey(getID(this.id));

        hvos = new SaleinvoiceVO[1];
        hvos[0] = saleinvoice;
      }
    }
    catch (Exception e) {
      showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000256"));

      SCMEnv.out(e.getMessage());
    }

    return hvos;
  }

  protected Object getAssistantPara(ButtonObject bo)
    throws ValidationException
  {
    int row = getBillListPanel().getHeadTable().getSelectedRow();
    Object o = null;

    if ((bo.getTag().equals("InvoiceExecRpt")) || (bo.getTag().equals("InvoiceExecRpt")))
    {
      if (this.strShowState.equals("LIST")) {
        o = getBillListPanel().getHeadBillModel().getValueAt(row, "csaleid");
      }
      else {
        o = getBillCardPanel().getHeadItem("csaleid").getValue();
      }
    }
    if (bo.getTag().equals("ATP")) {
      o = getVO(true);
    }

    if (bo.getTag().equals("SoTax")) {
      ArrayList arrayObj = new ArrayList();
      arrayObj.add(0, getAllVO());
      arrayObj.add(1, getClientEnvironment().getUser().getPrimaryKey());
      arrayObj.add(2, getCorpPrimaryKey());
      arrayObj.add(3, this.BD505);
      arrayObj.add(4, this.SA_02);
      o = arrayObj;
    }

    if (bo.getTag().equals("CustInfo")) {
      if (this.strShowState.equals("LIST")) {
        o = getBillListPanel().getHeadBillModel().getValueAt(row, "creceiptcorpid");
      }
      else {
        o = getBillCardPanel().getHeadItem("creceiptcorpid").getValue();
      }
    }
    if (bo.getTag().equals("CustCredited")) {
      String sCust = null;
      String sBiztype = null;
      String cproductid = null;
      if (this.strShowState.equals("LIST")) {
        int iselbodyrow = getBillListPanel().getBodyTable().getSelectedRow();
        if (iselbodyrow < 0) iselbodyrow = 0;
        Object oTemp = getBillListPanel().getBodyBillModel().getValueAt(iselbodyrow, "ccustomerid");

        sCust = oTemp == null ? null : oTemp.toString();
        oTemp = getBillListPanel().getHeadBillModel().getValueAt(row, "cbiztype");

        sBiztype = oTemp == null ? null : oTemp.toString();
        if ((this.SO_27 != null) && (this.SO_27.booleanValue()) && (getBillListPanel().getBodyBillModel().getRowCount() > 0))
        {
          cproductid = (String)getBillListPanel().getBodyBillModel().getValueAt(0, "cprolineid");
        }
      } else {
        int iselbodyrow = getBillCardPanel().getBillTable().getSelectedRow();
        if (iselbodyrow < 0) iselbodyrow = 0;
        sCust = (String)getBillCardPanel().getBodyValueAt(iselbodyrow, "ccustomerid");
        sBiztype = getBillCardPanel().getBusiType();

        if ((this.SO_27 != null) && (this.SO_27.booleanValue()) && (getBillCardPanel().getRowCount() > 0))
        {
          cproductid = (String)getBillCardPanel().getBodyValueAt(0, "cprolineid");
        }
      }
      CustCreditVO voCredit = new CustCreditVO();
      voCredit.setPk_cumandoc(sCust);
      voCredit.setCbiztype(sBiztype);
      voCredit.setCproductline(cproductid);
      o = voCredit;
    }
    if (bo.getTag().equals("Prifit")) {
      if (this.strShowState.equals("LIST")) {
        ProfitHeaderVO headVO = new ProfitHeaderVO();

        headVO.setPkcorp(getCorpPrimaryKey());

        headVO.setCcalbodyid((String)getBillListPanel().getHeadBillModel().getValueAt(row, "ccalbodyid"));

        headVO.setCcalbodyname((String)getBillListPanel().getHeadBillModel().getValueAt(row, "ccalbodyname"));

        headVO.setBilltype(getBillListPanel().getBillType());

        if (getBillListPanel().getBodyTable().getRowCount() > 0) {
          String curID = (String)getBillListPanel().getBodyBillModel().getValueAt(0, "ccurrencytypeid");

          headVO.setCurrencyid(curID);
        }
        ProfitItemVO[] bodyVOs = new ProfitItemVO[getBillListPanel().getBodyBillModel().getRowCount()];

        for (int i = 0; i < bodyVOs.length; i++) {
          ProfitItemVO bodyVO = new ProfitItemVO();

          bodyVO.setCinventoryid((String)getBillListPanel().getBodyBillModel().getValueAt(i, "cinventoryid"));

          bodyVO.setCode((String)getBillListPanel().getBodyBillModel().getValueAt(i, "cinventorycode"));

          bodyVO.setName((String)getBillListPanel().getBodyBillModel().getValueAt(i, "cinventoryname"));

          String gg = (String)getBillListPanel().getBodyBillModel().getValueAt(i, "GG");

          gg = gg == null ? "" : gg;
          String xx = (String)getBillListPanel().getBodyBillModel().getValueAt(i, "XX");

          xx = xx == null ? "" : xx;

          bodyVO.setSize(gg + xx);

          bodyVO.setCbatchid((String)getBillListPanel().getBodyBillModel().getValueAt(i, "cbatchid"));

          bodyVO.setNumber((UFDouble)getBillListPanel().getBodyBillModel().getValueAt(i, "nnumber"));

          bodyVO.setNnetprice((UFDouble)getBillListPanel().getBodyBillModel().getValueAt(i, "nnetprice"));

          bodyVO.setCbodycalbodyid((String)getBillListPanel().getBodyBillModel().getValueAt(i, "cadvisecalbodyid"));

          bodyVO.setCbodycalbodyname((String)getBillListPanel().getBodyBillModel().getValueAt(i, "cadvisecalbodyname"));

          bodyVO.setCbodywarehouseid((String)getBillListPanel().getBodyBillModel().getValueAt(i, "cbodywarehouseid"));

          bodyVO.setCbodywarehousename((String)getBillListPanel().getBodyBillModel().getValueAt(i, "cbodywarehousename"));

          if ((getBillListPanel().getBodyBillModel().getValueAt(i, "blargessflag") != null) && (getBillListPanel().getBodyBillModel().getValueAt(i, "blargessflag").toString().equals("false")))
          {
            bodyVO.m_blargessflag = new UFBoolean(false);
          }
          else bodyVO.m_blargessflag = new UFBoolean(true);

          bodyVO.setNmny((UFDouble)getBillListPanel().getBodyBillModel().getValueAt(i, "nmny"));

          bodyVOs[i] = bodyVO;
        }
        ProfitVO profit = new ProfitVO();
        profit.setParentVO(headVO);
        profit.setChildrenVO(bodyVOs);

        o = profit;
      } else {
        ProfitHeaderVO headVO = new ProfitHeaderVO();

        headVO.setPkcorp(getCorpPrimaryKey());

        headVO.setCcalbodyid(getBillCardPanel().getHeadItem("ccalbodyid").getValue());

        UIRefPane ccalbodyid = (UIRefPane)getBillCardPanel().getHeadItem("ccalbodyid").getComponent();

        headVO.setCcalbodyname(ccalbodyid.getRefName());

        headVO.setBilltype(getBillCardPanel().getBillType());

        headVO.setCurrencyid(getBillCardPanel().getHeadItem("ccurrencyid").getValue());

        ProfitItemVO[] bodyVOs = new ProfitItemVO[getBillCardPanel().getRowCount()];

        for (int i = 0; i < bodyVOs.length; i++) {
          ProfitItemVO bodyVO = new ProfitItemVO();

          bodyVO.setCinventoryid((String)getBillCardPanel().getBodyValueAt(i, "cinventoryid"));

          bodyVO.setCode((String)getBillCardPanel().getBodyValueAt(i, "cinventorycode"));

          bodyVO.setName((String)getBillCardPanel().getBodyValueAt(i, "cinventoryname"));

          String gg = (String)getBillCardPanel().getBodyValueAt(i, "GG");

          gg = gg == null ? "" : gg;
          String xx = (String)getBillCardPanel().getBodyValueAt(i, "XX");

          xx = xx == null ? "" : xx;

          bodyVO.setSize(gg + xx);

          bodyVO.setCbatchid((String)getBillCardPanel().getBodyValueAt(i, "cbatchid"));

          bodyVO.setNumber((UFDouble)getBillCardPanel().getBodyValueAt(i, "nnumber"));

          bodyVO.setNnetprice((UFDouble)getBillCardPanel().getBodyValueAt(i, "nnetprice"));

          bodyVO.setCbodycalbodyid((String)getBillCardPanel().getBodyValueAt(i, "cadvisecalbodyid"));

          bodyVO.setCbodycalbodyname((String)getBillCardPanel().getBodyValueAt(i, "cadvisecalbodyname"));

          bodyVO.setCbodywarehouseid((String)getBillCardPanel().getBodyValueAt(i, "cbodywarehouseid"));

          bodyVO.setCbodywarehousename((String)getBillCardPanel().getBodyValueAt(i, "cbodywarehousename"));

          bodyVO.setNmny((UFDouble)getBillCardPanel().getBodyValueAt(i, "nmny"));

          bodyVOs[i] = bodyVO;
        }
        ProfitVO profit = new ProfitVO();
        profit.setParentVO(headVO);
        profit.setChildrenVO(bodyVOs);

        o = profit;
      }
    }
    return o;
  }

  private BillCardPanel getBillCardPanel()
  {
    if (this.ivjBillCardPanel == null) {
      try {
        this.ivjBillCardPanel = new BillCardPanel();
        this.ivjBillCardPanel.setName("BillCardPanel");

        this.ivjBillCardPanel.setBillType("32");
        this.ivjBillCardPanel.setCorp(getCorpPrimaryKey());
        this.ivjBillCardPanel.setOperator(getClientEnvironment().getUser().getPrimaryKey());

        this.ivjBillCardPanel.addEditListener(this);
        this.ivjBillCardPanel.addBodyEditListener2(this);
        this.ivjBillCardPanel.addBodyMenuListener(this);

        this.ivjBillCardPanel.setTatolRowShow(true);

        this.ivjBillCardPanel.setBodyMenuShow(false);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjBillCardPanel;
  }

  public String getBillCode()
  {
    return "32";
  }

  public SaleInvoiceListPanel getBillListPanel()
  {
    if (this.ivjBillListPanel == null) {
      try {
        this.ivjBillListPanel = new SaleInvoiceListPanel(this);
        this.ivjBillListPanel.setName("BillListPanel");

        this.ivjBillListPanel.addHeadEditListener(this);
        this.ivjBillListPanel.setBillType("32");
        this.ivjBillListPanel.setCorp(getCorpPrimaryKey());
        this.ivjBillListPanel.setOperator(getClientEnvironment().getUser().getPrimaryKey());

        this.ivjBillListPanel.updateUI();
        this.ivjBillListPanel.setBD302(this.BD302);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjBillListPanel;
  }

  protected SaleInvoicePrintDataInterface getDataSource()
  {
    this.m_dataSource = new SaleInvoicePrintDataInterface();

    this.m_dataSource.setBillData(getBillCardPanel().getBillData());
    this.m_dataSource.setModuleName(getNodeCode());
    this.m_dataSource.setTotalLinesInOnePage(getPrintEntry().getBreakPos());
    this.bIsPrintDataReady = true;

    return this.m_dataSource;
  }

  private String getID(int id)
  {
    int num = getBillListPanel().getHeadTable().getSelectedRow();
    if (getBillListPanel().getHeadTable().getSelectedRowCount() > 1)
      num = id;
    if ((num < 0) || (num >= getBillListPanel().getHeadBillModel().getRowCount()))
    {
      return null;
    }
    return (String)getBillListPanel().getHeadBillModel().getValueAt(num, "csaleid");
  }

  public String getNodeCode()
  {
    return "40060502";
  }

  public UFBoolean getParaBoolean(Hashtable h, String key)
  {
    if (h.get(key) == null) {
      return new UFBoolean(false);
    }
    String str = (String)h.get(key);
    if (str == null)
      return new UFBoolean(false);
    return new UFBoolean(str);
  }

  public Integer getParaInt(Hashtable h, String key)
  {
    String str = (String)h.get(key);
    if (str == null)
      return new Integer(0);
    return Integer.valueOf(str);
  }

  public String getParaString(Hashtable h, String key)
  {
    String str = (String)h.get(key);
    return str;
  }

  protected PrintEntry getPrintEntry()
  {
    if (this.m_print == null) {
      this.m_print = new PrintEntry(null, null);
      this.m_print.setTemplateID(getClientEnvironment().getCorporation().getPk_corp(), getNodeCode(), getClientEnvironment().getUser().getPrimaryKey(), null);
    }

    return this.m_print;
  }

  public String getRowNoItemKey()
  {
    return "crowno";
  }

  public SourceBillFlowDlg getSourceDlg()
  {
    String sInvoiceid = null;
    if (this.strShowState.equals("LIST"))
      sInvoiceid = getID(this.id);
    else {
      sInvoiceid = getBillCardPanel().getHeadItem("csaleid").getValue();
    }

    SourceBillFlowDlg soureDlg = new SourceBillFlowDlg(this, "32", sInvoiceid, getBillCardPanel().getBusiType(), getBillCardPanel().getOperator(), getBillCardPanel().getCorp());

    return soureDlg;
  }

  public void getSysPara()
  {
    try
    {
      Hashtable h = this.hsparas;

      this.SA_08 = getParaBoolean(h, "SA08");
      this.SO_06 = getParaString(h, "SO06");
      this.BD501 = getParaInt(h, "BD501");
      this.BD502 = getParaInt(h, "BD502");
      this.BD503 = getParaInt(h, "BD503");
      this.BD505 = getParaInt(h, "BD505");
      this.BD302 = getParaBoolean(h, "BD302");
      this.SA_15 = getParaBoolean(h, "SA15");
      this.SO_20 = getParaBoolean(h, "SO20");

      this.SO_27 = getParaBoolean(h, "SO27");
      this.SO_22 = getParaDouble(h, "SO22");
      this.SO36 = getParaBoolean(h, "SO36");

      this.SA_02 = getParaBoolean(h, "SA02");

      String SA13 = (String)h.get("SA13");
      if ("集团定价".equals(SA13)) {
        this.SA_02 = getParaBoolean(h, "SA09");
      }

      String svalue = (String)h.get("SO34");
      if ((svalue == null) || (svalue.trim().length() <= 0))
        this.SO34 = new UFDouble(0.0D);
      else {
        this.SO34 = new UFDouble(svalue.trim());
      }
      this.SO41 = getParaBoolean(h, "SO41");
      if (this.SO41 == null)
        this.SO41 = SoVoConst.buffalse;
      String s59 = (String)h.get("SO59");
      if ((s59 == null) || (s59.trim().length() == 0))
        this.SO59 = new UFBoolean(false);
      else
        this.SO59 = new UFBoolean(s59.trim());
    }
    catch (Exception e) {
      handleException(e);
    }
  }

  public String getTitle()
  {
    return getBillCardPanel().getBillData().getTitle();
  }

  public SaleinvoiceVO getVO()
  {
    SaleinvoiceVO saleinvoice = null;
    try {
      if (this.strShowState.equals("LIST")) {
        String saleid = getBillListPanel().getHeadBillModel().getValueAt(this.selectRow, "csaleid").toString();

        saleinvoice = SaleinvoiceBO_Client.queryData(saleid);

        ((SaleVO)saleinvoice.getParentVO()).setCreceipttype("32");

        ((SaleVO)saleinvoice.getParentVO()).setPk_corp(getCorpPrimaryKey());

        if (saleinvoice.getChildrenVO() != null)
          for (int i = 0; i < saleinvoice.getChildrenVO().length; i++)
          {
            ((SaleinvoiceBVO)saleinvoice.getChildrenVO()[i]).setPk_corp(getCorpPrimaryKey());
          }
      }
      else
      {
        saleinvoice = (SaleinvoiceVO)getBillCardPanel().getBillValueVO(SaleinvoiceVO.class.getName(), SaleVO.class.getName(), SaleinvoiceBVO.class.getName());

        ((SaleVO)saleinvoice.getParentVO()).setPk_corp(getCorpPrimaryKey());

        if (saleinvoice.getChildrenVO() != null) {
          for (int i = 0; i < saleinvoice.getChildrenVO().length; i++)
          {
            ((SaleinvoiceBVO)saleinvoice.getChildrenVO()[i]).setPk_corp(getCorpPrimaryKey());
          }

        }

        ((SaleVO)saleinvoice.getParentVO()).setCreceipttype("32");
      }

      ((SaleVO)saleinvoice.getParentVO()).setCapproveid(getClientEnvironment().getUser().getPrimaryKey());

      saleinvoice.setHsArsubAcct(SaleinvoiceBO_Client.queryStrikeData(saleinvoice.getPrimaryKey()));
    } catch (Exception e) {
      showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000256"));

      SCMEnv.out(e.getMessage());
    }

    return saleinvoice;
  }

  public void deletevoicefromui(String csaleid)
  {
    try
    {
      if (this.strShowState.equals("LIST"))
      {
        getBillListPanel().getHeadBillModel().delLine(new int[] { this.selectRow });
        getBillListPanel().getBodyBillModel().clearBodyData();
      }
      else {
        int rowcount = getBillListPanel().getHeadBillModel().getRowCount();
        for (int i = 0; i < rowcount; i++) {
          String id = (String)getBillListPanel().getHeadBillModel().getValueAt(i, "csaleid");
          if ((id == null) || (!id.equals(csaleid)))
            continue;
          getBillListPanel().getHeadBillModel().delLine(new int[] { i });
          getBillListPanel().getBodyBillModel().clearBodyData();
        }

        getBillCardPanel().addNew();
      }

      int index = this.vocache.findPos(csaleid);
      this.vocache.removeVOAt(index);
    } catch (Exception e) {
      showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000256"));

      SCMEnv.out(e.getMessage());
    }
  }

  public SaleinvoiceVO getVO(boolean needRemove)
  {
    SaleinvoiceVO saleinvoice = null;
    try {
      if (this.strShowState.equals("LIST")) {
        String saleid = getBillListPanel().getHeadBillModel().getValueAt(this.selectRow, "csaleid").toString();

        saleinvoice = SaleinvoiceBO_Client.queryData(saleid);

        ((SaleVO)saleinvoice.getParentVO()).setCreceipttype("32");

        ((SaleVO)saleinvoice.getParentVO()).setPk_corp(getCorpPrimaryKey());

        if (saleinvoice.getChildrenVO() != null)
          for (int i = 0; i < saleinvoice.getChildrenVO().length; i++)
          {
            ((SaleinvoiceBVO)saleinvoice.getChildrenVO()[i]).setPk_corp(getCorpPrimaryKey());
          }
      }
      else
      {
        saleinvoice = (SaleinvoiceVO)getBillCardPanel().getBillValueVO(SaleinvoiceVO.class.getName(), SaleVO.class.getName(), SaleinvoiceBVO.class.getName());

        ((SaleVO)saleinvoice.getParentVO()).setPk_corp(getCorpPrimaryKey());

        if (saleinvoice.getChildrenVO() != null) {
          for (int i = 0; i < saleinvoice.getChildrenVO().length; i++)
          {
            ((SaleinvoiceBVO)saleinvoice.getChildrenVO()[i]).setPk_corp(getCorpPrimaryKey());
          }

        }

        ((SaleVO)saleinvoice.getParentVO()).setCreceipttype("32");

        ((SaleVO)saleinvoice.getParentVO()).setPrimaryKey(getID(this.id));
      }

      if (needRemove) {
        SaleinvoiceBVO[] itemVOs = (SaleinvoiceBVO[])saleinvoice.getChildrenVO();

        int indexSelected = -1;
        if (this.strShowState.equals("LIST")) {
          indexSelected = getBillListPanel().getBodyTable().getSelectedRow();
        }
        else {
          indexSelected = getBillCardPanel().getBillTable().getSelectedRow();
        }

        if ((indexSelected > -1) && (indexSelected < itemVOs.length)) {
          SaleinvoiceBVO[] itemsNew = new SaleinvoiceBVO[1];
          itemsNew[0] = itemVOs[indexSelected];
          saleinvoice.setChildrenVO(itemsNew);
        } else {
          saleinvoice.setChildrenVO(null);
        }
      }

      ((SaleVO)saleinvoice.getParentVO()).setCapproveid(getClientEnvironment().getUser().getPrimaryKey());
    }
    catch (Exception e)
    {
      showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000256"));

      SCMEnv.out(e.getMessage());
    }
    return saleinvoice;
  }

  private void handleException(Throwable exception)
  {
    SCMEnv.out("--------- 未捕捉到的异常 ---------");
    exception.printStackTrace(System.out);
  }

  public void initButtons()
  {
    this.m_boDocument = this.bt.getButton("文档管理");
    this.m_boReturn = this.bt.getButton("卡片显示/列表显示");
    this.m_boBrowse = this.bt.getButton("浏览");
    this.m_boQuery = this.bt.getButton("查询");
    this.m_boLocal = this.bt.getButton("定位");
    this.m_boFirst = this.bt.getButton("首页");
    this.m_boPrev = this.bt.getButton("上页");
    this.m_boNext = this.bt.getButton("下页");
    this.m_boLast = this.bt.getButton("末页");
    this.m_boModify = this.bt.getButton("修改");
    this.m_boSave = this.bt.getButton("保存");
    this.m_boCancel = this.bt.getButton("取消");
    this.m_boPrint = this.bt.getButton("打印");
    this.m_boPreview = this.bt.getButton("预览");
    this.m_boAction = this.bt.getButton("执行");
    this.m_boApprove = this.bt.getButton("审核");
    this.m_boUnapprove = this.bt.getButton("弃审");
    this.m_boBlankOut = this.bt.getButton("删除");
    this.m_boLineOper = this.bt.getButton("行操作");
    this.m_boAddLine = this.bt.getButton("增行");
    this.m_boDelLine = this.bt.getButton("删行");
    this.m_boAssistant = this.bt.getButton("辅助功能");
    this.m_boATP = this.bt.getButton("可用量");
    this.m_boCustInfo = this.bt.getButton("客户信息");
    this.m_boExecRpt = this.bt.getButton("发票执行情况");
    this.m_boCredit = this.bt.getButton("客户信用");
    this.m_boPrifit = this.bt.getButton("毛利预估");
    this.m_boSoTax = this.bt.getButton("传金税");
    this.m_boCard = this.bt.getButton("卡片显示/列表显示");
    this.boOrderQuery = this.bt.getButton("联查");
    this.boSendAudit = this.bt.getButton("送审");
    this.boAuditFlowStatus = this.bt.getButton("审批流状态");
    this.boOpposeAct = this.bt.getButton("生成对冲发票");
    this.m_boUnite = this.bt.getButton("合并开票");
    this.m_boUniteCancel = this.bt.getButton("放弃合并");
    m_boBillCombin = this.bt.getButton("合并显示");
    this.m_boImpInvoice = this.bt.getButton("导入发票号");
    
    this.m_boUnite.setTag("UNITE");
    this.m_boUniteCancel.setTag("UNUNITE");

    this.m_boApprove.setTag("APPROVE");
    this.m_boUnapprove.setTag("SoUnApprove");
    this.m_boBlankOut.setTag("SoBlankOut");
    this.boStockLock.setTag("StockLock");
    this.m_boATP.setTag("ATP");
    this.m_boCustInfo.setTag("CustInfo");
    this.m_boExecRpt.setTag("InvoiceExecRpt");
    this.m_boCredit.setTag("CustCredited");
    this.m_boPrifit.setTag("Prifit");
    this.m_boSoTax.setTag("SoTax");
    this.m_boImpInvoice.setTag("ImpInvoice");

    if (this.strShowState.equals("LIST")) {
      if (getBillListPanel().getHeadTable().getRowCount() > 0) return;

      PfUtilClient.retBusinessBtn(this.m_boBusiType, getCorpPrimaryKey(), "32");

      ButtonObject[] bo = this.m_boBusiType.getChildButtonGroup();
      for (int i = 0; i < bo.length; i++) {
        bo[i].setName(bo[i].getName());
      }
      this.m_boBusiType.setChildButtonGroup(bo);

      if (bo.length > 0) {
        this.m_boBusiType.getChildButtonGroup()[0].setSelected(true);
        this.m_boBusiType.setCheckboxGroup(true);
        this.m_biztype = this.m_boBusiType.getChildButtonGroup()[0].getTag();
        getBillCardPanel().setBusiType(this.m_biztype);
      }

      ButtonObject[] aryListButtonGroup = this.bt.getButtonArray();

      aryListButtonGroup = ArrayMethod.AppendToLast_BO(aryListButtonGroup, getExtendBtns());
      this.m_boReturn.setName(NCLangRes.getInstance().getStrByID("common", "UCH021"));

      setButtons(aryListButtonGroup);

      this.m_boQuery.setVisible(true);
      this.m_boLocal.setVisible(true);

      this.m_boSave.setVisible(false);
      this.m_boCancel.setVisible(false);
      this.m_boLineOper.setVisible(false);
      this.m_boFirst.setVisible(false);
      this.m_boPrev.setVisible(false);
      this.m_boNext.setVisible(false);
      this.m_boLast.setVisible(false);
      m_boBillCombin.setVisible(false);
    }
    else
    {
      ButtonObject[] aryButtonGroup = this.bt.getButtonArray();

      aryButtonGroup = ArrayMethod.AppendToLast_BO(aryButtonGroup, getExtendBtns());
      this.m_boReturn.setName(NCLangRes.getInstance().getStrByID("common", "UCH022"));

      setButtons(aryButtonGroup);

      this.m_boSave.setVisible(true);
      this.m_boCancel.setVisible(true);
      this.m_boLineOper.setVisible(true);
      this.m_boFirst.setVisible(true);
      this.m_boPrev.setVisible(true);
      this.m_boNext.setVisible(true);
      this.m_boLast.setVisible(true);
      m_boBillCombin.setVisible(true);

      this.m_boQuery.setVisible(false);
      this.m_boLocal.setVisible(false);
    }
  }

  protected void initCurrency()
  {
    try
    {
      this.currtype = new BusinessCurrencyRateUtil(getCorpPrimaryKey());
    } catch (Exception exp) {
      exp.printStackTrace();
      throw new BusinessRuntimeException(exp.getMessage());
    }
  }

  public void initFormulaParse()
  {
    nc.ui.so.so001.panel.bom.BillTools.initItemKeys();
  }

  protected void initFreeItem()
  {
    try
    {
      CircularlyAccessibleValueObject[] bodyvos = getBillCardPanel().getBillModel().getBodyValueVOs("nc.vo.so.so002.SaleinvoiceBVO");

      FreeVOParse freeVOParse = new FreeVOParse();
      freeVOParse.setFreeVO(bodyvos, "vfree0", "vfree", "cinvbasdocid", "cinventoryid", false);

      if ((bodyvos != null) && (bodyvos.length > 0)) {
        Object oTemp = null;
        for (int i = 0; i < bodyvos.length; i++) {
          oTemp = bodyvos[i].getAttributeValue("vfree0");
          if ((oTemp != null) && (oTemp.toString().trim().length() > 0))
            getBillCardPanel().getBillModel().setValueAt(oTemp, i, "vfree0");
        }
      }
    }
    catch (Exception ex)
    {
      SCMEnv.out("自由项设置失败!");
    }
  }

  private void initialize()
  {
    try
    {
      setName("SaleInvoice");
      setSize(774, 419);
      add(getBillListPanel(), "Center");
      this.bt = new ButtonTree("40060502");
    }
    catch (Throwable ivjExc)
    {
      handleException(ivjExc);
    }

    this.ce = getClientEnvironment();
    this.m_cl = new ClientLink(this.ce);
    this.user = this.ce.getUser().getPrimaryKey();
    this.pk_corp = getCorpPrimaryKey();

    initButtons();

    initCurrency();

    initVars(this.pk_corp);

    getSysPara();
    initState();
    loadTemplet();
    loadListTemplet();

    getBillListPanel().getHeadBillModel().setRowSort(false);
    getBillListPanel().getBodyBillModel().addTotalListener(this);
    getBillListPanel().addMouseListener(this);
    getInitBillItemEidtState();
    this.getButtonObjectByCode("签呈抵扣").setEnabled(false);
  }

  public void initIDs()
  {
    this.vIDs = new Vector();
    for (int i = 0; i < getBillListPanel().getHeadBillModel().getRowCount(); i++)
      this.vIDs.add(getBillListPanel().getHeadBillModel().getValueAt(i, "csaleid"));
  }

  public void initListFormulaParse()
  {
    nc.ui.so.so001.panel.bom.BillTools.initItemKeys();
  }

  public void initState()
  {
    getBillCardPanel().setEnabled(false);

    setButtonsState();
  }


  public void loadData()
  {
    try
    {
      long s = System.currentTimeMillis();

      this.saleinvoice = SaleinvoiceBO_Client.queryData(getBillListPanel().getHeadBillModel().getValueAt(this.id, "csaleid").toString());

      for (int i = 0; i < this.saleinvoice.getChildrenVO().length; i++) {
        if ((this.saleinvoice.getChildrenVO()[i].getAttributeValue("cupreceipttype") == null) || (!this.saleinvoice.getChildrenVO()[i].getAttributeValue("cupreceipttype").equals("30")))
        {
          continue;
        }
        this.saleinvoice.getChildrenVO()[i].setAttributeValue("csourcebillcode", this.saleinvoice.getChildrenVO()[i].getAttributeValue("coriginalbillcode"));
      }

      String dbilldate = ((SaleVO)this.saleinvoice.getParentVO()).getDbilldate().toString();

      String currencyid = ((SaleinvoiceBVO)this.saleinvoice.getChildrenVO()[0]).getCcurrencytypeid();

      setPanelByCurrency(currencyid);

      UFDouble exchangeotobrate = ((SaleinvoiceBVO)this.saleinvoice.getChildrenVO()[0]).getNexchangeotobrate();

      UFDouble exchangeotoarate = ((SaleinvoiceBVO)this.saleinvoice.getChildrenVO()[0]).getNexchangeotoarate();

      UFBoolean freef = ((SaleVO)this.saleinvoice.getParentVO()).getBfreecustflag();

      ((SaleVO)this.saleinvoice.getParentVO()).setCcurrencyid(currencyid);
      SaleinvoiceBVO[] saleinvoicebodys = (SaleinvoiceBVO[])this.saleinvoice.getChildrenVO();

      saleinvoicebodys = initFreeItemlist(saleinvoicebodys);
      this.saleinvoice.setChildrenVO(saleinvoicebodys);
      getBillCardPanel().setBillValueVO(this.saleinvoice);
      long s1 = System.currentTimeMillis();
      getBillCardPanel().getBillModel().execLoadFormula();

      SCMEnv.out("执行公式[共用时" + (System.currentTimeMillis() - s1) + "]");

      if (exchangeotobrate != null) {
        getBillCardPanel().setHeadItem("nexchangeotobrate", exchangeotobrate.toString());
      }

      if (exchangeotoarate != null) {
        getBillCardPanel().setHeadItem("nexchangeotoarate", exchangeotoarate.toString());
      }

      this.iStatus = ((SaleVO)this.saleinvoice.getParentVO()).getFstatus().intValue();

      String custId = ((SaleVO)this.saleinvoice.getParentVO()).getCreceiptcorpid();

      ((UIRefPane)getBillCardPanel().getHeadItem("ccustomername").getComponent()).setPK(custId);

      String[][] results = (String[][])null;
      try {
        results = SaleinvoiceBO_Client.getCustomerInfo(custId);
        if ((results != null) && (results.length != 0)) {
          UIRefPane bankref = (UIRefPane)getBillCardPanel().getHeadItem("ccustomerbank").getComponent();

          ((CustBankRefModel)bankref.getRefModel()).setCondition(results[0][5]);

          String bankId = ((SaleVO)this.saleinvoice.getParentVO()).getCcustbankid();

          bankref.setPK(bankId);
          String bankNo = bankref.getRefCode();
          getBillCardPanel().setHeadItem("ccustomerbankNo", bankNo);
          getBillCardPanel().setHeadItem("ccustomertel", results[0][0]);

          getBillCardPanel().setHeadItem("ccustomertaxNo", results[0][1]);

          getBillCardPanel().setHeadItem("vreceiveaddress", results[0][7]);
        }
        else {
          getBillCardPanel().setHeadItem("ccustomertel", "");
          getBillCardPanel().setHeadItem("ccustomertaxNo", "");
        }
      } catch (Exception e1) {
        showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000256"));

        e1.printStackTrace(System.out);
      }

      int iStatus = -1;
      if ((getBillCardPanel().getHeadItem("fstatus").getValue() != null) && (!getBillCardPanel().getHeadItem("fstatus").getValue().equals("")))
      {
        iStatus = Integer.parseInt(getBillCardPanel().getHeadItem("fstatus").getValue());
      }
      for (int kk = 0; kk < getBillCardPanel().getRowCount(); kk++) {
        if (iStatus == 1)
          setAssistChange(kk);
        if ((getBillCardPanel().getBodyValueAt(kk, "cpackunitid") == null) || (getBillCardPanel().getBodyValueAt(kk, "cpackunitid").equals("")))
        {
          continue;
        }
        beforeUnitChange(kk);
        afterUnitChange(kk);

        String[] appendFormulaViaPrice = { "norgviaprice->noriginalcurprice*scalefactor", "norgviapricetax->noriginalcurtaxprice*scalefactor" };

        getBillCardPanel().execBodyFormulas(kk, appendFormulaViaPrice);
      }

      countCardUniteMny();

      initFreeItem();
      getBillCardPanel().updateValue();
      getBillCardPanel().getBillModel().reCalcurateAll();

      SCMEnv.out("数据加载成功！[共用时" + (System.currentTimeMillis() - s) + "豪秒]");

      showHintMessage(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000065"));

      setState(iStatus);
    } catch (ValidationException e) {
      showErrorMessage(e.getMessage());
    } catch (Exception e) {
      showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000256"));

      SCMEnv.out(e.getMessage());
    }
  }

  public void loadDataforPrint()
  {
    try
    {
      long s = System.currentTimeMillis();
      this.saleinvoice = SaleinvoiceBO_Client.queryData(getID(this.id));

      String dbilldate = ((SaleVO)this.saleinvoice.getParentVO()).getDbilldate().toString();

      String currencyid = ((SaleinvoiceBVO)this.saleinvoice.getChildrenVO()[0]).getCcurrencytypeid();

      setPanelByCurrency(currencyid);

      UFDouble exchangeotobrate = ((SaleinvoiceBVO)this.saleinvoice.getChildrenVO()[0]).getNexchangeotobrate();

      UFDouble exchangeotoarate = ((SaleinvoiceBVO)this.saleinvoice.getChildrenVO()[0]).getNexchangeotoarate();

      UFBoolean freef = ((SaleVO)this.saleinvoice.getParentVO()).getBfreecustflag();

      ((SaleVO)this.saleinvoice.getParentVO()).setCcurrencyid(currencyid);
      getBillCardPanel().setBillValueVO(this.saleinvoice);
      long s1 = System.currentTimeMillis();
      getBillCardPanel().getBillModel().execLoadFormula();

      setState(this.iStatus);
    } catch (ValidationException e) {
      showErrorMessage(e.getMessage());
    }
    catch (Exception e) {
      showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000256"));

      SCMEnv.out(e.getMessage());
    }
  }

  public void loadIDafterADD(ArrayList listID)
  {
    try
    {
      if ((listID == null) || (listID.size() == 0))
        return;
      getBillCardPanel().setHeadItem("csaleid", (String)listID.get(0));
      getBillCardPanel().setHeadItem("vreceiptcode", (String)listID.get(1));

      for (int i = 2; i < listID.size(); i++) {
        getBillCardPanel().setBodyValueAt((String)listID.get(0), i - 2, "csaleid");

        getBillCardPanel().setBodyValueAt((String)listID.get(i), i - 2, "cinvoice_bid");
      }
    }
    catch (Exception e) {
      showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000256"));

      SCMEnv.out(e.getMessage());
    }
  }

  public void loadIDafterEDIT(ArrayList listID)
  {
    try
    {
      if ((listID == null) || (listID.size() == 0))
        return;
      int i = 0;
      for (int j = 0; j < getBillCardPanel().getRowCount(); j++) {
        String strBodyID = (String)getBillCardPanel().getBodyValueAt(j, "cinvoice_bid");

        if ((strBodyID != null) && (strBodyID.length() != 0))
          continue;
        getBillCardPanel().setBodyValueAt(getBillCardPanel().getHeadItem("csaleid").getValue(), j, "csaleid");

        getBillCardPanel().setBodyValueAt((String)listID.get(i), j, "cinvoice_bid");

        i++;
      }
    }
    catch (Exception e) {
      showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000256"));

      SCMEnv.out(e.getMessage());
    }
  }

  public void loadListTemplet()
  {
    showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000135"));

    BillListData bd = null;
    this.billtempletVO = null;

    if (this.billtempletVO == null) {
      this.billtempletVO = getBillListPanel().getDefaultTemplet(getNodeCode(), null, getClientEnvironment().getUser().getPrimaryKey(), getCorpPrimaryKey());
    }

    bd = new BillListData(this.billtempletVO);

    this.billtempletVO = null;

    if (bd.getHeadItem("cdispatcherid") != null) {
      bd.getHeadItem("cdispatcherid").setShow(false);
    }

    setListPanelByPara(bd);

    getBillListPanel().setListData(bd);
    try
    {
      DefSetTool.updateBillListPanelUserDef(getBillListPanel(), getClientEnvironment().getCorporation().getPk_corp(), "32", "vdef", "vdef");
    }
    catch (Throwable ex)
    {
      ex.printStackTrace(System.out);
    }

    getBillListPanel().getChildListPanel().setTatolRowShow(true);

    initListFormulaParse();

    UIComboBox comCont = (UIComboBox)getBillListPanel().getHeadItem("fcounteractflag").getComponent();

    getBillListPanel().getHeadItem("fcounteractflag").setWithIndex(true);
    comCont.setTranslate(true);
    comCont.addItem(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000156"));

    comCont.addItem(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000157"));

    comCont.addItem(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000158"));

    showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000147"));
  }

  private FreeItemRefPane getFreeItemRefPane()
  {
    if (this.ivjFreeItemRefPane == null) {
      try {
        this.ivjFreeItemRefPane = new FreeItemRefPane();
        this.ivjFreeItemRefPane.setName("FreeItemRefPane");
        this.ivjFreeItemRefPane.setLocation(209, 4);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjFreeItemRefPane;
  }

  public void loadTemplet()
  {
    for (int i = 0; i < this.m_boBusiType.getChildCount(); i++) {
      if (this.m_boBusiType.getChildButtonGroup()[i].isSelected()) {
        getBillCardPanel().setBusiType(this.m_boBusiType.getChildButtonGroup()[i].getTag());

        break;
      }
    }

    showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000136"));

    BillData bd = null;
    this.billtempletVO = null;

    if (this.billtempletVO == null) {
      this.billtempletVO = getBillCardPanel().getTempletData(getNodeCode(), null, getClientEnvironment().getUser().getPrimaryKey(), getCorpPrimaryKey());
    }

    bd = new BillData(this.billtempletVO);

    this.billtempletVO = null;

    getFreeItemRefPane().setMaxLength(1000);

    bd.getBodyItem("vfree0").setComponent(getFreeItemRefPane());

    setCardPanel(bd);

    getBillCardPanel().setBillData(bd);

    ((UIRefPane)getBillCardPanel().getHeadItem("cdispatcherid").getComponent()).getRefModel().addWherePart(" and rdflag = 1 ");

    DefSetTool.updateBillCardPanelUserDef(getBillCardPanel(), getClientEnvironment().getCorporation().getPk_corp(), "32", "vdef", "vdef");

    getBillCardPanel().addBodyTotalListener(this);

    initFormulaParse();

    setInputLimit();

    ((UIRefPane)getBillCardPanel().getHeadItem("csalecorpid").getComponent()).setReturnCode(false);

    ((UIRefPane)getBillCardPanel().getHeadItem("ccustomerbank").getComponent()).setReturnCode(false);

    setBodyComboBox();
    setHeadComboBox();

    BillRowNo.loadRowNoItem(this.ivjBillCardPanel, getRowNoItemKey());

    getBillCardPanel().setTatolRowShow(true);

    showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000176"));
  }

  public void lockRow(int row)
  {
    String[] cols = { "nnumber", "npacknumber" };
    for (int i = 0; i < cols.length; i++)
      getBillCardPanel().setCellEditable(row, cols[i], false);
  }

  public static void main(String[] args)
  {
    try
    {
      JFrame frame = new JFrame();

      SaleInvoiceAdminUI aSaleInvoice = new SaleInvoiceAdminUI();
      frame.setContentPane(aSaleInvoice);
      frame.setSize(aSaleInvoice.getSize());
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
      System.err.println("nc.ui.pub.ToftPanel 的 main() 中发生异常");
      exception.printStackTrace(System.out);
    }
  }
  
//  public class AuditRThread implements Runnable{
//	private ButtonObject boj = null;
//	private SaleinvoiceVO so = null;
//	public void setBtn(ButtonObject bo){
//		boj = bo;
//	}
//	public void setSO(SaleinvoiceVO svo){
//		so = svo;
//	}
//	public void run() {
//		// TODO Auto-generated method stub
//		BannerDialog banner = new BannerDialog(getParent());
//		getParent().setCursor(new Cursor(Cursor.WAIT_CURSOR));
//		banner.start();
//		 long s = System.currentTimeMillis();
//		 UFDateTime begintime =  new UFDateTime(s);
//		try {
//			PfUtilClient.processActionFlow(getParent(), boj.getTag(), "32", getClientEnvironment().getDate().toString(), so, null);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}finally{
//			//Eric  显示本次保存时间 
//			 UFDateTime endtime =  new UFDateTime(System.currentTimeMillis());
//			 String  mill = String.valueOf((endtime.getMillisAfter(begintime)));
//		showHintMessage("审批完成.共耗时："+ mill + "毫秒");
//		banner.end();
//		getParent().setCursor(Cursor.getDefaultCursor());
//		}
//	}
//	
//	
//  }

  public void onAction(ButtonObject bo)
  {
    try
    {
      long s = System.currentTimeMillis();
      SaleinvoiceVO saleinvoicevo = null;
      String cvmStr = "";
      if (bo.getTag().equals("APPROVE"))
      {
    	SaleinvoiceVO temp = getVO();//eric 2015-6-18
        onApproveCheckWorkflow(temp);
        SaleinvoiceVO saleinvoice =temp;
        saleinvoice.setCuruserid(getClientEnvironment().getUser().getPrimaryKey());
        PfUtilClient.processActionFlow(this, bo.getTag(), "32", getClientEnvironment().getDate().toString(), saleinvoice, null);
      }
      else
      {
        saleinvoicevo = new SaleinvoiceVO();
        saleinvoicevo = getVO();

        saleinvoicevo.setAllinvoicevo(saleinvoicevo);
        saleinvoicevo.setHsSelectedARSubHVO(this.hsSelectedARSubHVO);
        saleinvoicevo.setBstrikeflag(new UFBoolean(false));
        saleinvoicevo.setCuruserid(getClientEnvironment().getUser().getPrimaryKey());

        if (bo.getTag().equals("SoBlankOut")) {
          if ((saleinvoicevo.getParentVO().getPrimaryKey() == null) || (saleinvoicevo.getParentVO().getPrimaryKey().trim().length() < 1))
          {
            showWarningMessage(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000071"));

            return;
          }
          checkArsub();
          ((SaleVO)saleinvoicevo.getParentVO()).setVoldreceiptcode(((SaleVO)saleinvoicevo.getParentVO()).getVreceiptcode());
        }

        if (bo.getTag().equals("SoUnApprove")) {
          //add by zwx 2019-3-12 在弃审前获取销售订单对应的应收单
        	Object csaleid = saleinvoicevo.getHeadVO().getCsaleid();//销售发票号
        	String result = "";
            if(csaleid!=null){
            	result= getInfo(csaleid.toString());
            }
          //end by zwx
          PfUtilClient.processActionFlow(this, bo.getTag() + getClientEnvironment().getUser().getPrimaryKey(), "32", getClientEnvironment().getDate().toString(), saleinvoicevo, null);
          //add by zwx 2019-3-12 销售发票反审核后删除应收单同步至cvm
          cvmStr = sendToCVM(result);
          //end by zwx
        }
        else
        {
          PfUtilClient.processActionFlow(this, bo.getTag(), "32", getClientEnvironment().getDate().toString(), saleinvoicevo, null);
        }

        if (bo.getTag().equals("SoBlankOut"))
        {
          SaleVO head = (SaleVO)saleinvoicevo.getParentVO();
          SaleinvoiceBVO[] items = (SaleinvoiceBVO[])saleinvoicevo.getChildrenVO();
          if ((head.getFcounteractflag() != null) && (head.getFcounteractflag().intValue() == 2) && (items != null) && (items.length > 0)) {
            String coldbillid = items[0].getCupsourcebillid();
            for (int i = 0; i < getBillListPanel().getHeadBillModel().getRowCount(); i++) {
              if (!coldbillid.equals(getBillListPanel().getHeadBillModel().getValueAt(i, "csaleid"))) {
                continue;
              }
              String strWhere = "so_saleinvoice.csaleid = '" + coldbillid + "'";

              SaleVO[] salehvo = SaleinvoiceBO_Client.queryHeadAllData(strWhere);

              if ((salehvo != null) && (salehvo.length > 0)) {
                SaleinvoiceVO oldvo = this.vocache.getSaleInvoiceVO(coldbillid);
                if (oldvo != null) {
                  SaleVO oldhead = (SaleVO)oldvo.getParentVO();
                  oldhead.setTs(salehvo[0].getTs());
                  oldhead.setFcounteractflag(salehvo[0].getFcounteractflag());
                  oldvo.setParentVO(oldhead);
                  this.vocache.setSaleinvoiceVO(coldbillid, oldvo);

                  getBillListPanel().getHeadBillModel().setValueAt(salehvo[0].getTs(), i, "ts");

                  getBillListPanel().getHeadBillModel().setValueAt(salehvo[0].getFcounteractflag(), i, "fcounteractflag");  //fcounteractflag 发票对冲标记
                }
              }
            }

          }

        }

        this.strState = "FREE";
      }

      if (bo.getTag().equals("SoUnApprove"))
      {
        this.iStatus = 1;
      } else if (bo.getTag().equals("APPROVE")) {
        this.iStatus = 2;
      }
      else
      {
        this.iStatus = 5;
      }
      boolean bsus = PfUtilClient.isSuccess();
      if ((this.strShowState.equals("CARD")) && (bsus)) {
        getBillCardPanel().setHeadItem("fstatus", new Integer(this.iStatus));
        if (this.iStatus == 2)
        {
          getBillCardPanel().setTailItem("capproveid", ClientEnvironment.getInstance().getUser().getPrimaryKey());
          getBillCardPanel().setTailItem("dapprovedate", ClientEnvironment.getInstance().getBusinessDate().toString());
          getBillCardPanel().setTailItem("daudittime", new UFDateTime(System.currentTimeMillis()));
        }
        else
        {
          getBillCardPanel().setTailItem("capproveid", null);
          getBillCardPanel().setTailItem("dapprovedate", null);
          getBillCardPanel().setTailItem("daudittime", null);
        }
      }
      else {
        setShowDataState(getBillListPanel().getHeadTable().getSelectedRow(), bo.getTag());
      }

      reLoadTS();
      setButtonsState();
      if (bsus) {
        showHintMessage(bo.getName() + NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000067"));
        //add by zwx 2019-3-13 cvm同步返回信息
        if(cvmStr.length()>0){
            showHintMessage("应收单推送CVM:"+cvmStr);
        }
        //end by zwx
      }

      updateCacheVO();
      /**
       * edit 彭佳佳 2018年6月13日14:01:29
       */
      if (bo.getTag().equals("SoBlankOut")) {
    	 if(this.strShowState.equals("LIST")){
    		 int selrow = getBillListPanel().getHeadTable().getSelectedRow();
    		 String csaleid = (String)getBillListPanel().getHeadBillModel().getValueAt(selrow, "csaleid");
    		 SaleinvoiceVO saleinvoice = this.vocache.getSaleInvoiceVO(csaleid);
    		 this.onDeleteQC(saleinvoice);
    	 }else{
    		 SaleinvoiceVO saleinvoice = (SaleinvoiceVO) this.getBillCardPanel().getBillData().getBillValueVO(SaleinvoiceVO.class.getName(), SaleVO.class.getName(), SaleinvoiceBVO.class.getName());
    		 this.onDeleteQC(saleinvoice);
    	 }
        deletevoicefromui(saleinvoicevo.getPrimaryKey());
      }

      getBillListPanel().getHeadBillModel().execLoadFormula();

      getBillCardPanel().updateValue();
    }
    catch (BusinessException e) {
      showErrorMessage(e.getMessage());
    } catch (Exception e) {
      showErrorMessage(e.getMessage());
      SCMEnv.out(e.getMessage());
    }
  }
  

/**
   * 删除发票通知CVM
   * @param vos
   * 彭佳佳 2018-6-13 14:24:22
   */
  public void onDeleteQC(SaleinvoiceVO vos){
	  SaleVO hvo = (SaleVO) vos.getParentVO();
	  String pk_corp = (String) vos.getParentVO().getAttributeValue("pk_corp");
	  String custcode = (String) vos.getParentVO().getAttributeValue("creceiptcorpid");
	  String csaleid = (String) vos.getParentVO().getAttributeValue("csaleid");
	  IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
	  StringBuffer sql =new StringBuffer();
	  //查询客商信息
	  sql.append(" select aa.custcode,corp.unitcode ") 
	     .append("   from bd_cubasdoc aa ") 
	     .append("   left join bd_cumandoc bb ") 
		 .append("     on aa.pk_cubasdoc = bb.pk_cubasdoc ") 
		 .append("    left join bd_corp corp  ") 
		 .append("    on corp.pk_corp = bb.pk_corp ") 
	 	 .append("  where bb.pk_cumandoc = '"+custcode+"' ") 
	 	 .append("    and bb.pk_corp = '"+pk_corp+"' ") 
		 .append("    and nvl(bb.dr,0)= 0 ");
		List custList;
		String dkje = null;
		Map custMap = new HashMap();
		JSONArray jsonarr = new JSONArray();
		JSONObject jsonobj = new JSONObject();
		try {
			custList = (List) bs.executeQuery(sql.toString(),new MapListProcessor());
			
			if(custList.size()>0){
				custMap = (Map) custList.get(0);
				jsonobj.put("username", "baosteel");
				jsonobj.put("password", "123456");
				jsonobj.put("corp", pk_corp);
				jsonobj.put("kscode", (String)custMap.get("custcode"));
				jsonobj.put("ncpk",csaleid);
				jsonarr.add(jsonobj);
				URL url;
				url = new URL("http://10.70.26.23/cvm/CVMService/NC007");
//				url = new URL("http://47.94.84.171/cvm/CVMService/NC007");
				String result = this.httpconnect(url, jsonarr);
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
  }
  
  /**
   * 调用cvm接口
   * @param url
   * @param json
   * @return
   * @throws Exception
   */
			
  private String httpconnect(URL url,Object json) throws Exception {
        //创建HTTP链接
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
    
        //设置请求的方法类型
        httpURLConnection.setRequestMethod("POST");
        
        //设置请求的内容类型
        httpURLConnection.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
        
        //设置发送数据
        httpURLConnection.setDoOutput(true);
        //设置接受数据
        httpURLConnection.setDoInput(true);
        
        //发送数据,使用输出流
        OutputStream outputStream = httpURLConnection.getOutputStream();
        //发送的soap协议的数据
        String content = "jsonData="+URLEncoder.encode(json.toString(), "UTF-8");
        //发送数据
        outputStream.write(content.getBytes());
    
        //接收数据
        InputStream inputStream = httpURLConnection.getInputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));  
        StringBuffer buffer = new StringBuffer();  
        String line = "";  
        while ((line = in.readLine()) != null){  
          buffer.append(line);  
        }  
        String reustle =null;
        reustle = buffer.toString();
        in.close();
        //解析CVM返回JSON数据
        return reustle;
  }
  public void onAddLine()
  {
    getBillCardPanel().addLine();
    int rowCount = getBillCardPanel().getRowCount();

    getBillCardPanel().setCellEditable(rowCount - 1, "ntaxrate", true);

    String currencynowid = getBillCardPanel().getHeadItem("ccurrencyid").getValue();

    getBillCardPanel().setBodyValueAt(currencynowid, rowCount - 1, "ccurrencytypeid");

    String[] formula = { "ccurrencytypename->getColValue(bd_currtype,currtypename,pk_currtype,ccurrencytypeid)" };
    getBillCardPanel().getBillModel().execFormula(rowCount - 1, formula);

    getBillCardPanel().setBodyValueAt(getBillCardPanel().getHeadItem("nexchangeotobrate").getValue(), rowCount - 1, "nexchangeotobrate");

    getBillCardPanel().setBodyValueAt(getBillCardPanel().getHeadItem("nexchangeotoarate").getValue(), rowCount - 1, "nexchangeotoarate");

    getBillCardPanel().setBodyValueAt(getBillCardPanel().getHeadItem("ndiscountrate").getValue(), rowCount - 1, "ndiscountrate");

    getBillCardPanel().setBodyValueAt(new UFDouble(100), rowCount - 1, "nitemdiscountrate");

    getBillCardPanel().setCellEditable(rowCount - 1, "blargessflag", true);
    getBillCardPanel().setCellEditable(rowCount - 1, "cinventorycode", true);

    getBillCardPanel().getBodyItem("cinventorycode").setRefType("<nc.ui.so.so002.DisInvRefModel>");

    getBillCardPanel().setBodyValueAt(this.SO34, rowCount - 1, "nnumber");

    BillRowNo.addLineRowNo(getBillCardPanel(), getBillCode(), getRowNoItemKey());
  }

  public void onAfterAction(ButtonObject bo)
  {
    try
    {
      PfUtilClient.processAction(bo.getTag(), "32", getClientEnvironment().getDate().toString(), getVO(), getID(this.id));

      showHintMessage(bo.getName() + NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000067"));
    }
    catch (Exception e)
    {
      showErrorMessage(bo.getName() + NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000069"));

      SCMEnv.out(e.getMessage());
    }
  }

  public void onApproveCheckWorkflow(SaleinvoiceVO invoicehvo)
    throws ValidationException
  {
    try
    {
      boolean isExist = false;
      isExist = PfUtilBO_Client.isExistWorkFlow("32", invoicehvo.getHeadVO().getCbiztype(), getClientEnvironment().getCorporation().getPk_corp(), getClientEnvironment().getUser().getPrimaryKey());

      String pkOperator = ((SaleVO)invoicehvo.getParentVO()).getCoperatorid().trim();

      if ((isExist) && (pkOperator.equals(getClientEnvironment().getUser().getPrimaryKey().trim())))
      {
        int iWorkflowstate = 0;
        iWorkflowstate = PfUtilClient.queryWorkFlowStatus(invoicehvo.getHeadVO().getCbiztype(), "32", invoicehvo.getParentVO().getPrimaryKey());

        if (iWorkflowstate == 5) {
          throw new ValidationException(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000070"));
        }

      }

    }
    catch (Throwable e)
    {
      SCMEnv.out(e.getMessage());
      throw new ValidationException(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000070"));
    }
  }

  public void onAssistant(ButtonObject bo)
  {
    this.id = getBillListPanel().getHeadTable().getSelectedRow();

    if (this.id == -1)
      return;
    try {
      PfUtilClient.processActionNoSendMessage(this, bo.getTag(), "32", getClientEnvironment().getDate().toString(), getVO(), getAssistantPara(bo), null, null);
    }
    catch (Exception e)
    {
      showErrorMessage(bo.getName() + NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000069"));

      SCMEnv.out(e.getMessage());
    }
  }

  public void onAuditFlowStatus()
  {
    SaleinvoiceVO hvo = null;
    hvo = getVO(false);
    if ((hvo == null) || (hvo.getParentVO() == null)) {
      showErrorMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000199"));
    }
    else {
      SaleVO header = (SaleVO)hvo.getParentVO();
      String pk = header.getCsaleid();
      if (pk == null) {
        showErrorMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000067"));
      }
      else {
        FlowStateDlg approvestatedlg = new FlowStateDlg(this, "32", pk);

        approvestatedlg.showModal();
      }
    }
  }

  public void onBusiType(ButtonObject bo)
  {
    if ((getBillCardPanel().getBusiType() != null) && (getBillCardPanel().getBusiType().equals(bo.getTag())))
    {
      return;
    }
    bo.setSelected(true);

    this.m_boBusiType.setTag(bo.getTag());
    this.m_biztype = bo.getTag();
    getBillCardPanel().setBusiType(bo.getTag());

    setButtons(bo.getTag());
  }

  public void onButtonClicked(ButtonObject bo)
  {
    showHintMessage("");

    if ((this.strShowState.equals("LIST")) && (getBillListPanel().getHeadTable().getSelectedRowCount() <= 0) && (bo.getParent() != this.m_boBusiType) && (bo != this.m_boLocal) && (bo != this.m_boQuery) && (bo != this.m_boDocument))
    {
      showWarningMessage(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000071"));

      return;
    }

    if ((getBillListPanel().getHeadTable().getSelectedRowCount() > 1) && 
      (bo != this.m_boPrint) && (bo != this.m_boPreview) && (bo != this.m_boQuery)) {
      if ((bo == this.m_boApprove) || (bo == this.m_boBlankOut) || (bo == this.m_boUnapprove) || (bo == this.m_boUnite) || (bo == this.m_boUniteCancel)) {
        this.work = new WorkThread(bo.getTag());
        this.proccdlg = new ProccDlg(this, bo.getHint());
        this.proccdlg.showModal();
        return;
      }
      showErrorMessage(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000072"));

      return;
    }

    getBillCardPanel().stopEditing();
    if (bo == this.boOrderQuery)
      onOrderQuery();  //联查
    if ((bo == this.m_boReturn) && (this.strShowState.equals("CARD")))
      onReturn();  //卡片显示/列表显示
    else if (bo == this.m_boCard)
      onCard();  //卡片显示/列表显示
    if (bo == this.m_boFirst)
      onFirst();  //首页
    if (bo == this.m_boPrev)
      onPrev();  //上页
    if (bo == this.m_boNext)
      onNext();  //下页
    if (bo == this.m_boLast)
      onLast();  //末页
    if (bo == this.m_boModify)
      onModify();  //修改
    if (bo == this.m_boSave)
      onSave();  //保存
    if (bo == this.m_boLocal)
      onLocal();  //定位
    if (bo == this.m_boQuery)
      onQuery();  //查询
    if (bo == this.m_boCancel)
      onCancel();  //取消
    if (bo == this.m_boPrint)
      onPrint(false);  //打印
    else if (bo == this.m_boPreview)
      onPrint(true);  //预览
    else if (bo == this.boAuditFlowStatus)
      onAuditFlowStatus();  //审批流状态
    if (bo == this.m_boHelp)
      onHelp();   //帮助
    if (bo == this.m_boDelLine)
      onDelLine();  //删行
    if (bo == this.m_boAddLine)
      onAddLine();  //增行
    if (bo.getParent() == this.m_boBusiType) {
      onBusiType(bo);   //业务类型
    }

    if ((bo == this.m_boUnapprove) || (bo == this.m_boApprove) || (bo == this.m_boBlankOut)) {
      onAction(bo);   //审核/弃审/删除
    }

    if ((bo == this.m_boSoTax) || (bo == this.m_boATP) || (bo == this.m_boCustInfo) || (bo == this.m_boExecRpt) || (bo == this.m_boCredit) || (bo == this.m_boPrifit))
    {
      onAssistant(bo);    //传金税,可用量,客户信息,发票执行情况,客户信用,毛利预估
    }
    if (bo.getParent() == this.m_boAfterAction) {
      onAfterAction(bo);  //后续业务
    }
    if (bo == this.m_boDocument) {
      onDocument();  //文档管理
    }

    if (bo == m_boBillCombin)
      onBillCombin();  //合并显示
    if (bo == this.boSendAudit) {
      onSendAudit();  //送审
    }

    if (bo == this.m_boUnite)
    {
      onUnite(bo);   //合并开票
    }
    if (bo == this.m_boUniteCancel)
      onUniteCancel(bo);   //放弃合并
    if (bo == this.boOpposeAct) {
      onOpposeAct();  //生成对冲发票
    }
    String pk_corp = ClientEnvironment.getInstance().getCorporation().getPk_corp();
    if ((bo == this.m_boImpInvoice) && 
      (getParentCorpCode().equals("10395")))
    {
      onImpInvoice();  //导入发票号
    }

    onExtendBtnsClick(bo);
  }

  public void onCancel()
  {
    getBillCardPanel().resumeValue();

    if ((getBillCardPanel().getHeadItem("csaleid").getValue() == null) || (getBillCardPanel().getHeadItem("csaleid").getValue().trim().length() == 0))
    {
      this.id = getBillListPanel().getHeadTable().getSelectedRow();

      String csaleid = (String)getBillListPanel().getHeadBillModel().getValueAt(this.id, "csaleid");

      SaleinvoiceVO saleinvoice = this.vocache.getSaleInvoiceVO(csaleid);
      SaleinvoiceBVO[] bodys = (SaleinvoiceBVO[])null;
      try {
        bodys = SaleinvoiceBO_Client.queryBodyData(csaleid);
      } catch (Exception e) {
        SCMEnv.out(e.getMessage());
      }
      SoVoTools.execFormulas(new String[] { "discountflag->getColValue(bd_invbasdoc,discountflag,pk_invbasdoc,cinvbasdocid)" }, bodys);

      saleinvoice.setChildrenVO(bodys);
      loadCardData(saleinvoice);
      getBillCardPanel().execHeadFormula("csalecompanyname->getColValue(bd_corp,unitname,pk_corp,pk_corp)");

      for (int i = 0; i < getBillCardPanel().getBillModel().getRowCount(); i++) {
        UFDouble dSubsummny = (UFDouble)getBillCardPanel().getBodyValueAt(i, "nsubsummny");

        UFDouble dNoriginalcursummny = (UFDouble)getBillCardPanel().getBodyValueAt(i, "noriginalcursummny");

        UFDouble nuniteinvoicemny = (dSubsummny == null ? new UFDouble(0) : dSubsummny).sub(dNoriginalcursummny == null ? new UFDouble(0) : dNoriginalcursummny);

        getBillCardPanel().setBodyValueAt(nuniteinvoicemny, i, "nuniteinvoicemny");

        Object orgBillCode = getBillCardPanel().getBodyValueAt(i, "coriginalbillcode");

        getBillCardPanel().setBodyValueAt(orgBillCode, i, "csourcebillcode");
      }

      updateUI();
    }
    this.strState = "FREE";
    setButtonsState();
    showHintMessage("");

    this.hsSelectedARSubHVO = ((Hashtable)this.oldhsSelectedARSubHVO.clone());
    if ((this.hsTotalBykey != null) && (this.hsTotalBykey.size() != 0)) {
      this.hsTotalBykey.clear();
    }
    for (int i = 0; i < getBillCardPanel().getBillModel().getRowCount(); i++) {
      UFDouble nmnytemp = ((UFDouble)getBillCardPanel().getBillModel().getValueAt(i, "nsubsummny")).sub((UFDouble)getBillCardPanel().getBillModel().getValueAt(i, "noriginalcursummny"));

      getBillCardPanel().getBillModel().setValueAt(nmnytemp, i, "nuniteinvoicemny");
    }
  }

  public void onCard()
  {
    onDoubleClick();
    this.strShowState = "CARD";
    initButtons();
    this.strState = "CARD";
    setButtonsState();
  }

  public void onCheck(SaleinvoiceVO saleinvoice)
    throws ValidationException
  {
    if (getBillCardPanel().getRowCount() == 0) {
      throw new ValidationException(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000073"));
    }

    saleinvoice.validate();

    getBillCardPanel().dataNotNullValidate();
    if ((this.BD302 != null) && (this.BD302.booleanValue())) {
      try {
        if ((!this.currtype.isLocalCurrType(getBillCardPanel().getHeadItem("ccurrencyid").getValue())) && (
          (getBillCardPanel().getHeadItem("nexchangeotoarate").getValue() == null) || (new UFDouble(getBillCardPanel().getHeadItem("nexchangeotoarate").getValue()).doubleValue() == 0.0D)))
        {
          throw new ValidationException("主辅币核算时，折辅汇率不能为空或者为零！");
        }
      } catch (ValidationException e) {
        throw e;
      } catch (Exception e) {
        e.printStackTrace();
      }

    }

    for (int i = 0; i < saleinvoice.getChildrenVO().length; i++) {
      SaleinvoiceBVO oldbodyVO = (SaleinvoiceBVO)saleinvoice.getChildrenVO()[i];

      if ((oldbodyVO.getDiscountflag() != null) && (!oldbodyVO.getDiscountflag().booleanValue()))
      {
        if ((oldbodyVO.getBlargessflag() != null) && (!oldbodyVO.getBlargessflag().booleanValue()))
        {
          if ((oldbodyVO.getNoriginalcurmny() != null) && 
            (oldbodyVO.getNnumber().multiply(oldbodyVO.getNoriginalcurmny()).doubleValue() < 0.0D))
          {
            throw new ValidationException(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000073"));
          }

          if ((oldbodyVO.getNoriginalcursummny() != null) && 
            (oldbodyVO.getNnumber().multiply(oldbodyVO.getNoriginalcursummny()).doubleValue() < 0.0D))
          {
            throw new ValidationException(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000074"));
          }

        }

      }

      if ((oldbodyVO.getDiscountflag() != null) && (!oldbodyVO.getDiscountflag().booleanValue()) && (oldbodyVO.getCupsourcebillid() != null))
      {
        if ((oldbodyVO.getNnumber() == null) && (oldbodyVO.getNnumber().doubleValue() == 0.0D))
        {
          throw new ValidationException(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000075"));
        }

      }
      else if ((oldbodyVO.getNnumber() == null) && (oldbodyVO.getNoriginalcurmny() == null) && (oldbodyVO.getNoriginalcursummny() == null))
      {
        throw new ValidationException(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000261"));
      }

      if ((oldbodyVO.getAssistunit() == null) || (!oldbodyVO.getAssistunit().booleanValue()))
        continue;
      if ((oldbodyVO.getDiscountflag() == null) || (oldbodyVO.getDiscountflag().booleanValue()))
        continue;
      if (oldbodyVO.getCpackunitid() == null) {
        throw new ValidationException(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000076"));
      }

      if (oldbodyVO.getNpacknumber() == null) {
        throw new ValidationException(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000077"));
      }

      if (oldbodyVO.getNpacknumber().doubleValue() * oldbodyVO.getNnumber().doubleValue() >= 0.0D)
        continue;
      throw new ValidationException(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000078"));
    }
  }

  public void onDelLine()
  {
    getBillCardPanel().delLine();
    getBillCardPanel().setHeadItem("ntotalsummny", calcurateTotal("noriginalcursummny"));

    getBillCardPanel().execHeadFormula("nnetmny->ntotalsummny-nstrikemny");
  }

  public void onDocument()
  {
    String pk = null;
    String billcode = null;

    if (this.strShowState.equals("LIST")) {
      pk = getBillListPanel().getHeadBillModel().getValueAt(this.selectRow, "csaleid").toString();

      billcode = getBillListPanel().getHeadBillModel().getValueAt(this.selectRow, "vreceiptcode").toString();
    }
    else {
      pk = getBillCardPanel().getHeadItem("csaleid").getValue();
      billcode = getBillCardPanel().getHeadItem("vreceiptcode").getValue();
    }

    DocumentManager.showDM(this, new String[] { pk }, new String[] { billcode });
  }

  public void onDoubleClick()
  {
    this.id = getBillListPanel().getHeadTable().getSelectedRow();
    remove(getBillListPanel());
    add(getBillCardPanel(), "Center");

    String csaleid = (String)getBillListPanel().getHeadBillModel().getValueAt(this.id, "csaleid");

    SaleinvoiceVO saleinvoice = this.vocache.getSaleInvoiceVO(csaleid);
    SaleinvoiceBVO[] bodys = (SaleinvoiceBVO[])null;
    try {
      bodys = SaleinvoiceBO_Client.queryBodyData(csaleid);
    } catch (Exception e) {
      SCMEnv.out(e.getMessage());
    }
    SoVoTools.execFormulas(new String[] { "discountflag->getColValue(bd_invbasdoc,discountflag,pk_invbasdoc,cinvbasdocid)" }, bodys);

    saleinvoice.setChildrenVO(bodys);
    loadCardData(saleinvoice);
    getBillCardPanel().execHeadFormula("csalecompanyname->getColValue(bd_corp,unitname,pk_corp,pk_corp)");

    for (int i = 0; i < getBillCardPanel().getBillModel().getRowCount(); i++) {
      UFDouble dSubsummny = (UFDouble)getBillCardPanel().getBodyValueAt(i, "nsubsummny");

      UFDouble dNoriginalcursummny = (UFDouble)getBillCardPanel().getBodyValueAt(i, "noriginalcursummny");

      UFDouble nuniteinvoicemny = (dSubsummny == null ? new UFDouble(0) : dSubsummny).sub(dNoriginalcursummny == null ? new UFDouble(0) : dNoriginalcursummny);

      getBillCardPanel().setBodyValueAt(nuniteinvoicemny, i, "nuniteinvoicemny");

      Object orgBillCode = getBillCardPanel().getBodyValueAt(i, "coriginalbillcode");

      getBillCardPanel().setBodyValueAt(orgBillCode, i, "csourcebillcode");
    }

    initButtons();
    updateUI();
  }

  public void onFirst()
  {
    this.id = 0;
    String csaleid = (String)getBillListPanel().getHeadBillModel().getValueAt(this.id, "csaleid");

    SaleinvoiceVO vo = this.vocache.getSaleInvoiceVO(csaleid);
    if ((vo != null) && (vo.getChildrenVO() != null)) {
      loadCardData(vo);
    } else {
      loadData();
      updateCacheVO();
    }

    this.strState = "FREE";
    setButtonsState();
  }

  public void onHelp()
  {
  }

  public void onLast()
  {
    this.id = (this.vIDs.size() - 1);
    String csaleid = (String)getBillListPanel().getHeadBillModel().getValueAt(this.id, "csaleid");

    SaleinvoiceVO vo = this.vocache.getSaleInvoiceVO(csaleid);
    if ((vo != null) && (vo.getChildrenVO() != null)) {
      loadCardData(vo);
    } else {
      loadData();
      updateCacheVO();
    }

    this.strState = "FREE";
    setButtonsState();
  }

  public void onLocal()
  {
    LocateDialog dlg = new LocateDialog(this, getBillListPanel().getHeadTable());

    dlg.showModal();
  }

  public void onMenuItemClick(ActionEvent e)
  {
  }

  public void onModify()//调整
  {
    int iOppStats = 0;
    if (this.strShowState.equals("LIST")) {
      int iSelect = getBillListPanel().getHeadTable().getSelectedRow();
      Object tStrikemny = getBillListPanel().getHeadBillModel().getValueAt(iSelect, "nstrikemny");

      if ((tStrikemny == null) || (new UFDouble(tStrikemny.toString()).doubleValue() == 0.0D))
      {
        this.bstrikeflag = false;
      }
      else this.bstrikeflag = true;
    }
    else if ((getBillCardPanel().getHeadItem("nstrikemny") != null) && (new UFDouble(getBillCardPanel().getHeadItem("nstrikemny").getValue()).equals(new UFDouble(0))))
    {
      this.bstrikeflag = false;
    } else {
      this.bstrikeflag = true;
    }

    if (this.bstrikeflag) {
      for (int i = 0; i < getBillCardPanel().getBillModel().getRowCount(); i++) {
        getBillCardPanel().getBillModel().setCellEditable(i, "nuniteinvoicemny", true);
      }
    }
    else {
      for (int i = 0; i < getBillCardPanel().getBillModel().getRowCount(); i++) {
        getBillCardPanel().getBillModel().setCellEditable(i, "nuniteinvoicemny", false);
      }
    }

    if (this.strShowState.equals("LIST")) {
      this.strShowState = "CARD";
      onDoubleClick();

      this.strState = "MODIFY";
      setButtonsState();
    } else {
      this.strState = "MODIFY";
      setButtonsState();
    }
    setNoEditItem();

    iOppStats = Integer.valueOf(getBillCardPanel().getHeadItem("fcounteractflag").getValue()).intValue();

    getBillCardPanel().setEnabled(true);
    if (iOppStats == 2) {
      setOppCellEditable();
    }
    if (this.SO41.booleanValue()) {
      getBillCardPanel().setTailItem("coperatorid", getClientEnvironment().getUser().getPrimaryKey());
    }

    updateUI();

    SaleinvoiceVO hvo = getVO(false);
    SaleVO header = (SaleVO)hvo.getParentVO();
    this.m_oldreceipt = header.getVreceiptcode();
  }

  public void onNext()
  {
    if (this.id < this.vIDs.size()) {
      this.id += 1;
      String csaleid = (String)getBillListPanel().getHeadBillModel().getValueAt(this.id, "csaleid");

      SaleinvoiceVO vo = this.vocache.getSaleInvoiceVO(csaleid);
      if ((vo != null) && (vo.getChildrenVO() != null)) {
        loadCardData(vo);
      } else {
        loadData();
        updateCacheVO();
      }
    }
    this.strState = "FREE";
    setButtonsState();
  }

  public void onOrderQuery()
  {
    this.id = getBillListPanel().getHeadTable().getSelectedRow();
    getSourceDlg().showModal();
  }

  public void onPrev()
  {
    if (this.id > 0) {
      this.id -= 1;
      String csaleid = (String)getBillListPanel().getHeadBillModel().getValueAt(this.id, "csaleid");

      SaleinvoiceVO vo = this.vocache.getSaleInvoiceVO(csaleid);
      if ((vo != null) && (vo.getChildrenVO() != null)) {
        loadCardData(vo);
      } else {
        loadData();
        updateCacheVO();
      }
    }
    this.strState = "FREE";
    setButtonsState();
  }

  public PrintLogClient getPrintLogClient()
  {
    if (this.printLogClient == null) {
      this.printLogClient = new PrintLogClient();
      this.printLogClient.addFreshTsListener(this);
    }
    return this.printLogClient;
  }

  public void freshTs(String sBillID, String sTS, Integer iPrintCount) {
    if ((sTS == null) || (sTS.trim().length() <= 0))
      return;
    if (this.strShowState.equals("LIST")) {
      if ((sBillID == null) || (sBillID.trim().length() <= 0))
        return;
      String csaleid = null;
      int i = 0; int loop = getBillListPanel().getHeadTable().getRowCount();
      for (; i < loop; i++) {
        csaleid = (String)getBillListPanel().getHeadBillModel().getValueAt(i, "csaleid");

        if (sBillID.equals(csaleid)) {
          getBillListPanel().getHeadBillModel().setValueAt(sTS, i, "ts");

          getBillListPanel().getHeadBillModel().setValueAt(iPrintCount, i, "iprintcount");

          break;
        }
      }
    } else {
      getBillCardPanel().setHeadItem("ts", sTS);
      getBillCardPanel().setTailItem("iprintcount", iPrintCount);
      String csaleid = null;
      int i = 0; int loop = getBillListPanel().getHeadTable().getRowCount();
      for (; i < loop; i++) {
        csaleid = (String)getBillListPanel().getHeadBillModel().getValueAt(i, "csaleid");

        if (sBillID.equals(csaleid)) {
          getBillListPanel().getHeadBillModel().setValueAt(sTS, i, "ts");

          getBillListPanel().getHeadBillModel().setValueAt(iPrintCount, i, "iprintcount");

          break;
        }
      }
    }
    SaleinvoiceVO invoicevo = this.vocache.getSaleInvoiceVO(sBillID);
    if (invoicevo != null) {
      invoicevo.getParentVO().setAttributeValue("iprintcount", iPrintCount);

      invoicevo.getParentVO().setAttributeValue("ts", new UFDateTime(sTS.trim()));
    }
  }

  public void checkArsub()
    throws BusinessException
  {
    UFDouble nstrikemny = null;
    if (this.strShowState.equals("LIST")) {
      int iRow = getBillListPanel().getHeadTable().getSelectedRow();
      nstrikemny = (UFDouble)getBillListPanel().getHeadBillModel().getValueAt(iRow, "nstrikemny");
    }
    else {
      if (getBillCardPanel().getHeadItem("nstrikemny") == null)    //nstrikemny 冲减金额
        return;
      nstrikemny = new UFDouble(getBillCardPanel().getHeadItem("nstrikemny").getValue());
    }

    if (nstrikemny == null)
      return;
    if (nstrikemny.doubleValue() != 0.0D)
      throw new BusinessException(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000153"));
  }

  public void onPrint(boolean previewflag)
  {
    if (!this.strShowState.equals("LIST")) {
      boolean total = getBillCardPanel().getBodyPanel().isTatolRow();
      try {
        getBillCardPanel().getBodyPanel().setTotalRowShow(false);

        SalePubPrintDS ds = new SalePubPrintDS(getNodeCode(), getBillCardPanel());

        PrintEntry print = new PrintEntry(null, null);
        print.setTemplateID(getCorpPrimaryKey(), getNodeCode(), this.ce.getUser().getPrimaryKey(), null);
        if (print.selectTemplate() < 0) return;
        getPrintLogClient(); showHintMessage(PrintLogClient.getBeforePrintMsg(previewflag, false));

        SaleinvoiceVO saleinvoice = (SaleinvoiceVO)getBillCardPanel().getBillValueVO(SaleinvoiceVO.class.getName(), SaleVO.class.getName(), SaleinvoiceBVO.class.getName());

        getPrintLogClient().setPrintEntry(print);
        getPrintLogClient().setPrintInfo(((SaleVO)saleinvoice.getParentVO()).getScmPrintlogVO());

        print.setPrintListener(getPrintLogClient());

        ds.setPageRows(print.getBreakPos());
        print.setDataSource(ds);
        if (previewflag)
          print.preview();
        else {
          print.print(true);
        }
        showHintMessage(getPrintLogClient().getPrintResultMsg(previewflag));
      }
      finally
      {
        getBillCardPanel().getBodyPanel().setTotalRowShow(total); } getBillCardPanel().getBodyPanel().setTotalRowShow(total);
    }
    else
    {
      onPrintListVos(previewflag);
    }
  }

  public void onPrintListVos(boolean needPreview)
  {
    ArrayList alvos = new ArrayList();

    int[] selectRows = getBillListPanel().getHeadTable().getSelectedRows();
    SaleVO[] hvos = new SaleVO[selectRows.length];
    for (int k = 0; k < selectRows.length; k++) {
      hvos[k] = ((SaleVO)getBillListPanel().getHeadBillModel().getBodyValueRowVO(selectRows[k], "nc.vo.so.so002.SaleVO"));
    }
    for (int i = 0; i < selectRows.length; i++) {
      this.id = selectRows[i];

      loadData();

      AggregatedValueObject printHvo = getBillCardPanel().getBillValueVO("nc.vo.dm.pub.DMVO", "nc.vo.dm.pub.DMDataVO", "nc.vo.dm.pub.DMDataVO");

      AggregatedValueObject printListHvo = getBillListPanel().getBillValueVO(this.id, "nc.vo.dm.pub.DMVO", "nc.vo.dm.pub.DMDataVO", "nc.vo.dm.pub.DMDataVO");

      DMDataVO header = (DMDataVO)printListHvo.getParentVO();

      header.setAttributeValue("ccustbankid", header.getAttributeValue("ccustomerbank"));

      header.setAttributeValue("cdeptid", header.getAttributeValue("cdeptname"));

      header.setAttributeValue("cwarehouseid", header.getAttributeValue("cwarehousename"));

      header.setAttributeValue("ctransmodeid", header.getAttributeValue("ctransmodename"));

      header.setAttributeValue("creceiptcorpid", header.getAttributeValue("creceiptcorpname"));

      header.setAttributeValue("ccalbodyid", header.getAttributeValue("ccalbodyname"));

      header.setAttributeValue("cfreecustid", header.getAttributeValue("cfreecustname"));

      header.setAttributeValue("creceiptcustomerid", header.getAttributeValue("creceiptcustomername"));

      header.setAttributeValue("ctermprotocolid", header.getAttributeValue("ctermprotocolname"));

      header.setAttributeValue("csalecorpid", header.getAttributeValue("csalecorpname"));

      header.setAttributeValue("cemployeeid", header.getAttributeValue("cemployeename"));

      header.setAttributeValue("capproveid", header.getAttributeValue("capprovename"));

      header.setAttributeValue("coperatorid", header.getAttributeValue("coperatorname"));

      printHvo.setParentVO(header);
      alvos.add(printHvo);
    }
    if ((alvos == null) || (alvos.size() == 0)) {
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000153"));

      return;
    }

    if (getPrintEntry().selectTemplate() < 0) {
      return;
    }
    getPrintLogClient(); showHintMessage(PrintLogClient.getBeforePrintMsg(needPreview, true));

    if (needPreview)
    {
      getPrintLogClient().setPrintEntry(getPrintEntry());
      getPrintLogClient().setPrintInfo(hvos[0].getScmPrintlogVO());

      ArrayList prlistvo = new ArrayList();
      SaleInvoicePrintDataInterface prds = getDataSource();
      prlistvo.add(alvos.get(0));
      prds.setListVOs(prlistvo);
      prds.setTotalLinesInOnePage(getPrintEntry().getBreakPos());

      getPrintEntry().setDataSource(prds);
      getPrintEntry().setPrintListener(getPrintLogClient());

      getPrintEntry().preview();
    } else {
      int count = 0;

      getPrintLogClient().setPrintEntry(getPrintEntry());

      getPrintEntry().beginBatchPrint();
      SaleInvoicePrintDataInterface prds = null;
      ArrayList prlistvo = new ArrayList();
      getPrintLogClient().setBatchPrint(true);
      getPrintEntry().setPrintListener(getPrintLogClient());

      int i = 0; for (int loop = hvos.length; i < loop; i++)
      {
        getPrintLogClient().setPrintInfo(hvos[i].getScmPrintlogVO());

        if (!getPrintLogClient().check())
          continue;
        prds = getDataSource();
        prlistvo.add(alvos.get(i));
        prds.setListVOs(prlistvo);
        prds.setTotalLinesInOnePage(getPrintEntry().getBreakPos());

        getPrintEntry().setDataSource(prds);
      }

      getPrintEntry().endBatchPrint();
    }
    showHintMessage(getPrintLogClient().getPrintResultMsg(needPreview));
  }

  private void onBillCombin()
  {
    CollectSettingDlg dlg = new CollectSettingDlg(this);

    dlg.setBilltype("32");
    dlg.setNodecode(getNodeCode());
    dlg.initData(getBillCardPanel(), new String[] { "cinventorycode", "cinventoryname", "GG", "XX", "ccurrencytypename" }, new String[] { "noriginalcurtaxnetprice" }, new String[] { "noriginalcurtaxmny", "noriginalcurmny", "noriginalcursummny", "nnumber", "noriginalcurdiscountmny", "ntaxmny", "nmny", "nsummny", "ndiscountmny", "nsimulatecostmny", "ncostmny", "nsubsummny", "nsubcursummny" }, null, new String[] { "nsubtaxnetprice", "nsubqutaxnetpri", "nsubqunetpri", "nsubqutaxpri", "nsubqupri", "nqutaxnetprice", "nqunetprice", "nqutaxprice", "nquprice", "nqocurtaxnetpri", "nquoricurnetpri", "nquoricurtaxpri", "nquoricurpri", "ntaxnetprice", "nnetprice", "ntaxprice", "nprice", "noriginalcurtaxnetprice", "noriginalcurnetprice", "noriginalcurtaxprice", "noriginalcurprice" }, "nnumber");

    dlg.showModal();
    showHintMessage(NCLangRes.getInstance().getStrByID("40060501", "UPT40060501-000058"));
  }

  public void onQuery()
  {
    getQueryDlg().setRefsDataPowerConVOs(ClientEnvironment.getInstance().getUser().getPrimaryKey(), new String[] { ClientEnvironment.getInstance().getCorporation().getPk_corp() }, new String[] { "客户档案", "客户档案", "销售组织", "部门档案", "人员档案", "客户档案", "存货档案", "外币档案", "库存组织", "客户档案", "仓库档案" }, new String[] { "so_saleinvoice_b.ccustomerid", "so_saleinvoice.creceiptcorpid", "so_saleinvoice.csalecorpid", "so_saleinvoice.cdeptid", "so_saleinvoice.cemployeeid", "so_saleinvoice.creceiptcustomerid", "so_saleinvoice_b.cinventoryid", "so_saleinvoice_b.ccurrencytypeid", "so_saleinvoice.ccalbodyid", "so_saleinvoice_b.creceiptcorpid", "so_saleinvoice_b.cbodywarehouseid" }, new int[] { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 });

    SCMQueryConditionDlg dlgQuery = getQueryDlg();

    if (dlgQuery.showModal() == 2) {
      return;
    }
    String m_strWhere = " and so_saleinvoice.pk_corp = '" + getCorpPrimaryKey() + "' ";

    if (this.m_rdoAll.isSelected()) {
      m_strWhere = m_strWhere + " and so_saleinvoice.fstatus != " + 5;
    }
    else if (this.m_rdoFree.isSelected()) {
      m_strWhere = m_strWhere + " and so_saleinvoice.fstatus = " + 1;
    }
    else if (this.m_rdoAudited.isSelected()) {
      m_strWhere = m_strWhere + " and so_saleinvoice.fstatus = " + 2;
    }
    else if (this.m_rdoBlankOut.isSelected()) {
      m_strWhere = m_strWhere + " and so_saleinvoice.fstatus = " + 5;
    }

    m_strWhere = m_strWhere + " and so_saleinvoice.creceipttype='32' ";
    //add by zwx 2015-12-16 销售发票过滤删除发票
    m_strWhere = m_strWhere + " and nvl(so_saleinvoice.dr,0) = 0 ";
    //end by zwx
    ConditionVO[] voCon = dlgQuery.getConditionVO();

    if (voCon != null) {
      for (int i = 0; i < voCon.length; i++) {
        if ((voCon[i].getTableNameForMultiTable() == null) || (!voCon[i].getTableNameForMultiTable().equals("bd_cumandoc")) || (voCon[i].getTableCodeForMultiTable().indexOf("def") < 0))
        {
          continue;
        }
        voCon[i].setTableCode(voCon[i].getTableName() + "." + voCon[i].getTableCodeForMultiTable());

        voCon[i].setFieldCode("so_saleinvoice.creceiptcorpid");
      }

    }

    try
    {
      ConditionVO[] newvo = ScmPubHelper.getTotalSubPkVOs(voCon, getCorpPrimaryKey());

      voCon = newvo;
    } catch (Exception e) {
      System.out.print("包含下级部门构件出错! \n");
      SCMEnv.out(e.getMessage());
    }

    voCon = ConvertQueryCondition.getConvertedVO(voCon, getCorpPrimaryKey());

    ArrayList indexlist = new ArrayList();
    ArrayList indexlist2 = new ArrayList();

    for (int i = 0; i < voCon.length; i++)
    {
      if (voCon[i].getFieldCode().equalsIgnoreCase("so_sale.vreceiptcode"))
      {
        indexlist.add(new Integer(i));
      }
      if (!voCon[i].getFieldCode().equalsIgnoreCase("ic_general_h.vbillcode"))
        continue;
      indexlist2.add(new Integer(i));
    }

    ConditionVO[] newVO = new ConditionVO[voCon.length - indexlist.size() - indexlist2.size()];

    int newindex = 0;
    for (int i = 0; i < voCon.length; i++) {
      if ((indexlist.contains(new Integer(i))) || 
        (indexlist2.contains(new Integer(i)))) continue;
      newVO[newindex] = voCon[i];
      newindex++;
    }

    String sdlgWhere = dlgQuery.getWhereSQL(newVO);
    String temWhere = null;
    String temWhere2 = null;
    if (indexlist.size() == 0)
      temWhere = "";
    else
      temWhere = "select csaleid from so_sale where (1=1)";
    for (int i = 0; i < indexlist.size(); i++) {
      int index = ((Integer)indexlist.get(i)).intValue();
      temWhere = temWhere + voCon[index].getSQLStr();
    }

    if (indexlist2.size() == 0)
      temWhere2 = "";
    else
      temWhere2 = "select cgeneralhid from ic_general_h where (1=1)";
    for (int i = 0; i < indexlist2.size(); i++) {
      int index = ((Integer)indexlist2.get(i)).intValue();
      temWhere2 = temWhere2 + voCon[index].getSQLStr();
    }

    if ((temWhere == "") && (temWhere2 == ""))
      temWhere = "";
    else if ((temWhere == "") && (temWhere2 != ""))
      temWhere = temWhere2;
    else if ((temWhere != "") && (temWhere2 != ""))
      temWhere = "(" + temWhere + ") union (" + temWhere2 + ")";
    else {
      temWhere = temWhere;
    }
    if ((sdlgWhere == null) && (temWhere == ""))
      sdlgWhere = "";
    else if ((sdlgWhere == null) && (temWhere != "")) {
      sdlgWhere = "so_saleinvoice_b.cupsourcebillid in (" + temWhere + ")";
    }
    else if ((sdlgWhere != null) && (temWhere == ""))
      sdlgWhere = sdlgWhere;
    else {
      sdlgWhere = sdlgWhere + " and so_saleinvoice_b.cupsourcebillid in (" + temWhere + ")";
    }

    if ((sdlgWhere != null) && (sdlgWhere.trim().length() > 0))
      sdlgWhere = " (" + sdlgWhere + ") ";
    else {
      sdlgWhere = " 1=1 ";
    }
    String sPowersql = DpsClient.getDpSubSqlByBizTable("so_saleinvoice", null, this.ce.getUser().getPrimaryKey(), new String[] { this.ce.getCorporation().getPrimaryKey() });
    if (sPowersql != null) m_strWhere = m_strWhere + " and " + sPowersql;
    getBillListPanel().setWhere(sdlgWhere + m_strWhere);

    if (this.hsSelectedARSubHVO.size() != 0)
      this.hsSelectedARSubHVO.clear();
    if (this.hsTotalBykey.size() != 0) {
      this.hsTotalBykey.clear();
    }

    this.vocache.setCacheData(null);
    setState(-1);
    getBillListPanel().reLoadData();
    fillCacheByListPanel();
    this.selectRow = -1;
    initIDs();

    getBillListPanel().getHeadTable().setSelectionMode(2);

    setButtonsState();
  }

  public void reLoadListData() {
    if (this.strShowState.equals("LIST")) {
      this.vocache.setCacheData(null);
      setState(-1);
      getBillListPanel().reLoadData();
      fillCacheByListPanel();
      this.selectRow = -1;
      initIDs();
    }
  }

  public void onReturn()
  {
    remove(getBillCardPanel());
    add(getBillListPanel(), "Center");
    this.strShowState = "LIST";
    setButtons();

    this.selectRow = -1;

    getBillListPanel().getHeadTable().clearSelection();
    getBillListPanel().getBodyBillModel().clearBodyData();
    getBillListPanel().setHeaderValueVO(this.vocache.getAllSaleInvoiceHVO());
    getBillListPanel().getHeadBillModel().execLoadFormula();
    initIDs();
    setButtonsState();
    updateUI();
  }

  public void onSave()
  {
    long s = System.currentTimeMillis();
    if (!BillRowNo.verifyRowNosCorrect(getBillCardPanel(), getRowNoItemKey()))
    {
      return;
    }
    int invoicetype = Integer.valueOf(getBillCardPanel().getHeadItem("finvoicetype").getValue()).intValue();

    int rowcount = getBillCardPanel().getRowCount();
    for (int b = 0; b < rowcount; b++) {
      Object largessflag = getBillCardPanel().getBodyValueAt(b, "blargessflag");

      if ((largessflag == null) || (!largessflag.toString().equals("Y")) || ((invoicetype != 0) && (invoicetype != 1)))
        continue;
      showErrorMessage(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000079"));

      return;
    }

    this.saleinvoice = ((SaleinvoiceVO)getBillCardPanel().getBillValueChangeVO(SaleinvoiceVO.class.getName(), SaleVO.class.getName(), SaleinvoiceBVO.class.getName()));

    SaleinvoiceVO saleinvoicemdy = (SaleinvoiceVO)getBillCardPanel().getBillValueVO(SaleinvoiceVO.class.getName(), SaleVO.class.getName(), SaleinvoiceBVO.class.getName());
    //eric 将折扣判断处理进行优化 2015-6-18
    FormulaParse fp = new FormulaParse();
    SuperVOUtil.execFormulaWithVOs(this.saleinvoice.getChildrenVO(), new String[]{"discountflag->getColValue(bd_invbasdoc,discountflag,pk_invbasdoc,cinvbasdocid)"},null, fp);
    SuperVOUtil.execFormulaWithVOs(saleinvoicemdy.getChildrenVO(), new String[]{"discountflag->getColValue(bd_invbasdoc,discountflag,pk_invbasdoc,cinvbasdocid)"},null, fp);
  
//    if (this.saleinvoice.getChildrenVO() != null) {
//      for (int i = 0; i < this.saleinvoice.getChildrenVO().length; i++) {
//        ((SaleinvoiceBVO)this.saleinvoice.getChildrenVO()[i]).m_discountflag = new UFBoolean(isLaborOrDiscount(((SaleinvoiceBVO)this.saleinvoice.getChildrenVO()[i]).getCinvbasdocid()));
//      }
//    }
//    if (saleinvoicemdy.getChildrenVO() != null) {
//      for (int i = 0; i < saleinvoicemdy.getChildrenVO().length; i++) {
//        ((SaleinvoiceBVO)saleinvoicemdy.getChildrenVO()[i]).m_discountflag = new UFBoolean(isLaborOrDiscount(((SaleinvoiceBVO)saleinvoicemdy.getChildrenVO()[i]).getCinvbasdocid()));
//      }
//    }

    ((SaleVO)this.saleinvoice.getParentVO()).setPk_corp(this.pk_corp);

    ((SaleVO)this.saleinvoice.getParentVO()).setBcodechanged(this.m_isCodeChanged);
    if (this.m_isCodeChanged) {
      ((SaleVO)this.saleinvoice.getParentVO()).setVoldreceiptcode(this.m_oldreceipt);
    }

    ((SaleVO)this.saleinvoice.getParentVO()).setCreceipttype("32");

    ((SaleVO)this.saleinvoice.getParentVO()).setFstatus(new Integer(1));

    UIRefPane vreceiveaddress = (UIRefPane)getBillCardPanel().getHeadItem("vreceiveaddress").getComponent();

    ((SaleVO)this.saleinvoice.getParentVO()).setVreceiveaddress(vreceiveaddress.getText());
    
 
    
    try
    {
     
      onCheck(saleinvoicemdy);
      ArrayList listBillID = null;

      if ((this.strState.equals("OPP")) && (((SaleVO)this.saleinvoice.getParentVO()).getFcounteractflag() != null) && (((SaleVO)this.saleinvoice.getParentVO()).getFcounteractflag().intValue() == 2))
      {
        this.saleinvoice.setAllinvoicevo(this.saleinvoice);

        this.saleinvoice.setDClientDate(this.ce.getDate());
        showHintMessage(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000080"));

        this.saleinvoice.setStatus(2);
        this.saleinvoice.setCuruserid(getClientEnvironment().getUser().getPrimaryKey());

        SaleinvoiceBVO[] items = (SaleinvoiceBVO[])this.saleinvoice.getChildrenVO();
        if (items == null) {
          showErrorMessage("发票表体不能为空！");
          return;
        }
        for (int it = 0; it < items.length; it++) {
          items[it].setStatus(2);
        }

        this.saleinvoice.setIAction(0);
        
        listBillID = (ArrayList)PfUtilClient.processActionNoSendMessage(this, "PreKeep", "32", getClientEnvironment().getDate().toString(), this.saleinvoice, null, null, null);
       
        loadIDafterADD(listBillID);

        String coldbillid = items[0].getCupsourcebillid();
        for (int i = 0; i < getBillListPanel().getHeadBillModel().getRowCount(); i++) {
          if (!coldbillid.equals(getBillListPanel().getHeadBillModel().getValueAt(i, "csaleid"))) {
            continue;
          }
          String strWhere = "so_saleinvoice.csaleid = '" + coldbillid + "'";

          SaleVO[] salehvo = SaleinvoiceBO_Client.queryHeadAllData(strWhere);

          if ((salehvo == null) || (salehvo.length <= 0))
            continue;
          SaleinvoiceVO oldvo = this.vocache.getSaleInvoiceVO(coldbillid);
          SaleVO oldhead = (SaleVO)oldvo.getParentVO();
          oldhead.setTs(salehvo[0].getTs());
          oldhead.setFcounteractflag(salehvo[0].getFcounteractflag());
          oldvo.setParentVO(oldhead);
          this.vocache.setSaleinvoiceVO(coldbillid, oldvo);

          getBillListPanel().getHeadBillModel().setValueAt(salehvo[0].getTs(), i, "ts");

          getBillListPanel().getHeadBillModel().setValueAt(salehvo[0].getFcounteractflag(), i, "fcounteractflag");
        }

      }
      else
      {
        this.saleinvoice.setAllinvoicevo(saleinvoicemdy);
        this.saleinvoice.setHsSelectedARSubHVO(this.hsSelectedARSubHVO);
        this.saleinvoice.setBstrikeflag(new UFBoolean(this.bstrikeflag));

        this.saleinvoice.setDClientDate(this.ce.getDate());

        this.saleinvoice.setHsArsubAcct(this.hsSelectedARSubHVO);

        showHintMessage(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000080"));

        this.saleinvoice.setStatus(1);

        this.saleinvoice.setCuruserid(getClientEnvironment().getUser().getPrimaryKey());

        this.saleinvoice.setIAction(1);
        this.saleinvoice.setAllSaleOrderVO(saleinvoicemdy);
        ((SaleVO)saleinvoicemdy.getParentVO()).setCbiztype((String)this.saleinvoice.getParentVO().getAttributeValue("cbiztype"));
        SaleinvoiceVO oldvo = this.vocache.getSaleInvoiceVO(getID(this.id));
        if (oldvo != null)
          ((SaleVO)oldvo.getParentVO()).setCbiztype((String)this.saleinvoice.getParentVO().getAttributeValue("cbiztype"));
        this.saleinvoice.setOldSaleOrderVO(oldvo);
        listBillID = (ArrayList)PfUtilClient.processActionNoSendMessage(this, "PreModify", "32", getClientEnvironment().getDate().toString(), this.saleinvoice, null, null, null);
        getBillCardPanel().setHeadItem("vreceiptcode", listBillID.get(listBillID.size() - 1));
        listBillID.remove(listBillID.size() - 1);
        loadIDafterEDIT(listBillID);
      }

      showHintMessage(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000083"));

      this.strState = "FREE";

      this.oldhsSelectedARSubHVO = ((Hashtable)this.hsSelectedARSubHVO.clone());

      this.hsTotalBykey.clear();
      this.oldhsTotalBykey.clear();

      reLoadTS();

      setButtonsState();

      UIComboBox combobox = (UIComboBox)getBillCardPanel().getHeadItem("fstatus").getComponent();

      combobox.setSelectedIndex(1);

      updateCacheVO();

      this.m_isCodeChanged = false;

      getBillListPanel().getHeadBillModel().setValueAt(new Integer(1), getBillListPanel().getHeadTable().getSelectedRow(), "fstatus");

      ((UIRefPane)getBillCardPanel().getHeadItem("cbiztypename").getComponent()).setPK(this.saleinvoice.getHeadVO().getCbiztype());

      getBillCardPanel().updateValue();

      updateUI();
      /**
       * edit 彭佳佳 2018年6月13日18:23:57
       * 反馈实际抵扣金额
       */
      String pk_corp = this.saleinvoice.getPk_corp();
      if(pk_corp.equals("1016") || pk_corp.equals("1071") || pk_corp.equals("1103")||pk_corp.equals("1097")||pk_corp.equals("1017")||pk_corp.equals("1018")||pk_corp.equals("1019")||pk_corp.equals("1107")){
    	  StringBuffer sb = new StringBuffer();
          String cvmjson = new String();
          for (int a = 0 ;a<this.saleinvoice.getChildrenVO().length;a++){
        	  cvmjson = this.saleinvoice.getChildrenVO()[a].getAttributeValue("b_cjje1")==null?"":this.saleinvoice.getChildrenVO()[a].getAttributeValue("b_cjje1").toString();
        	  if(cvmjson.length()<=0){
        		  continue;
        	  }else{
        		  sb.append(cvmjson);
        	  }
          }
          if(sb.length()>0){
        	  BaoyinSaleInvoiceAdminUI ui = new BaoyinSaleInvoiceAdminUI();
              ui.conSaleSerivce(this.saleinvoice);
          } 
      }
    }
    catch (ValidationException e) {
      showErrorMessage(e.getMessage());
    } catch (BusinessException e) {
      showErrorMessage(e.getMessage());
      SCMEnv.out(e.getMessage());
    } catch (Exception e) {
      showErrorMessage(e.getMessage());
      SCMEnv.out(e.getMessage());
    }
  }

  public void onSendAudit()
  {
    SaleinvoiceVO invoicehvo = getVO(false);

    if ((invoicehvo == null) || (invoicehvo.getParentVO() == null))
      showErrorMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000199"));
    else
      try
      {
        boolean isExist = false;
        isExist = PfUtilBO_Client.isExistWorkFlow("32", invoicehvo.getHeadVO().getCbiztype(), getClientEnvironment().getCorporation().getPk_corp(), getClientEnvironment().getUser().getPrimaryKey());

        if (!isExist) {
          showErrorMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000111"));

          return;
        }

        int iWorkflowstate = 0;
        iWorkflowstate = PfUtilClient.queryWorkFlowStatus(invoicehvo.getHeadVO().getCbiztype(), "32", invoicehvo.getParentVO().getPrimaryKey());

        if ((iWorkflowstate != 5) && (iWorkflowstate != 3))
        {
          if (iWorkflowstate == 2) {
            showErrorMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000077"));

            return;
          }if (iWorkflowstate == 0) {
            showErrorMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000078"));

            return;
          }if (iWorkflowstate == 1) {
            showErrorMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000079"));

            return;
          }

          if (iWorkflowstate == 4) {
            showErrorMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000112"));

            return;
          }if (iWorkflowstate == 5) {
            showErrorMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000070"));

            return;
          }

        }

        PfUtilClient.processAction("SoEdit", "32", getClientEnvironment().getDate().toString(), invoicehvo);

        if (PfUtilClient.isSuccess()) {
          setButtonsState();
          showHintMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000291"));
        } else {
          showHintMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000292"));
        }
      } catch (Exception e) {
        showWarningMessage(e.getMessage() + NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000408"));
      }
  }

  public void onShowFree()
  {
  }

  public boolean setAssistChange(int row)
  {
    UFBoolean assistunit = new UFBoolean(false);
    if (getBillCardPanel().getBodyValueAt(row, "assistunit") != null) {
      assistunit = new UFBoolean(getBillCardPanel().getBodyValueAt(row, "assistunit").toString());
    }

    boolean bEdit = true;
    if (!assistunit.booleanValue()) {
      bEdit = false;
    }
    getBillCardPanel().setCellEditable(row, "cpackunitname", bEdit);
    getBillCardPanel().setCellEditable(row, "npacknumber", bEdit);

    return bEdit;
  }

  public boolean setAssistUnit(int row)
  {
    UFBoolean assistunit = new UFBoolean(false);
    if (getBillCardPanel().getBodyValueAt(row, "assistunit") != null) {
      assistunit = new UFBoolean(getBillCardPanel().getBodyValueAt(row, "assistunit").toString());
    }

    boolean bEdit = true;
    if (!assistunit.booleanValue()) {
      bEdit = false;
    }
    getBillCardPanel().setCellEditable(row, "cpackunitname", bEdit);
    getBillCardPanel().setCellEditable(row, "npacknumber", bEdit);

    return bEdit;
  }

  public void setBodyComboBox()
  {
    UIComboBox comItem = (UIComboBox)getBillCardPanel().getBodyItem("frowstatus").getComponent();

    getBillCardPanel().getBodyItem("frowstatus").setWithIndex(true);
    comItem.setTranslate(true);
    comItem.addItem("");
    comItem.addItem(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000340"));

    comItem.addItem(NCLangRes.getInstance().getStrByID("common", "UC001-0000027"));

    comItem.addItem("");
    comItem.addItem("");
    comItem.addItem(NCLangRes.getInstance().getStrByID("common", "UC001-0000005"));

    UIComboBox comBatch = (UIComboBox)getBillCardPanel().getBodyItem("fbatchstatus").getComponent();

    getBillCardPanel().getBodyItem("fbatchstatus").setWithIndex(true);
    comBatch.setTranslate(true);
    comBatch.addItem(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000340"));

    comBatch.addItem(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000086"));

    comBatch.addItem(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000087"));
  }

  protected void setBodyFreeValue(int row, InvVO voInv)
  {
    if (voInv != null) {
      voInv.setFreeItemValue("vfree1", (String)getBillCardPanel().getBodyValueAt(row, "vfree1"));

      voInv.setFreeItemValue("vfree2", (String)getBillCardPanel().getBodyValueAt(row, "vfree2"));

      voInv.setFreeItemValue("vfree3", (String)getBillCardPanel().getBodyValueAt(row, "vfree3"));

      voInv.setFreeItemValue("vfree4", (String)getBillCardPanel().getBodyValueAt(row, "vfree4"));

      voInv.setFreeItemValue("vfree5", (String)getBillCardPanel().getBodyValueAt(row, "vfree5"));
    }
  }

  public void setBodyRowState(int row)
  {
    if (getBillCardPanel().getBillModel().getRowState(row) != 1)
      getBillCardPanel().getBillModel().setRowState(row, 2);
  }

  private void setButtons()
  {
    if (this.strShowState.equals("LIST"))
      setButtons(this.m_biztype);
    else
      setButtons(this.m_biztype);
  }

  public void setButtons(String strBusiType)
  {
    if (this.strShowState.equals("LIST"))
    {
      ButtonObject[] aryListButtonGroup = this.bt.getButtonArray();

      this.m_boCard.setName(NCLangRes.getInstance().getStrByID("common", "UCH021"));

      setButtons(aryListButtonGroup);
      this.m_boQuery.setVisible(true);
      this.m_boLocal.setVisible(true);

      this.m_boSave.setVisible(false);
      this.m_boCancel.setVisible(false);
      this.m_boLineOper.setVisible(false);
      this.m_boFirst.setVisible(false);
      this.m_boPrev.setVisible(false);
      this.m_boNext.setVisible(false);
      this.m_boLast.setVisible(false);
      m_boBillCombin.setVisible(false);
    }
    else
    {
      ButtonObject[] aryButtonGroup = this.bt.getButtonArray();

      this.m_boReturn.setName(NCLangRes.getInstance().getStrByID("common", "UCH022"));

      setButtons(aryButtonGroup);
    }
  }

  public void setButtonsState()
  {
    if (this.strShowState.equals("LIST")) {
      setListButtonsState();
    }
    else
    {
      setCardButtonsState();
    }
  }

  private void setBtnSateByChildBtn(String State)
  {
    if (State.equals("LIST")) {
      if ((!this.m_boModify.isEnabled()) && (!this.m_boCancel.isEnabled()) && (!this.m_boBlankOut.isEnabled()) && (!this.m_boUnite.isEnabled()) && (!this.m_boUniteCancel.isEnabled()))
      {
        this.bt.getButton("维护").setEnabled(false);
      }
      else this.bt.getButton("维护").setEnabled(true);

      if ((!this.m_boAddLine.isEnabled()) && (!this.m_boDelLine.isEnabled()))
        this.m_boLineOper.setEnabled(false);
      else {
        this.m_boLineOper.setEnabled(true);
      }
      if ((!this.boSendAudit.isEnabled()) && (!this.m_boUnapprove.isEnabled()))
        this.m_boAction.setEnabled(false);
      else {
        this.m_boAction.setEnabled(true);
      }
      if ((!this.m_boLocal.isEnabled()) && (!this.m_boFirst.isEnabled()) && (!this.m_boPrev.isEnabled()) && (!this.m_boNext.isEnabled()) && (!this.m_boLast.isEnabled()))
      {
        this.m_boBrowse.setEnabled(false);
      }
      else this.m_boBrowse.setEnabled(true);

      if ((!this.m_boPrint.isEnabled()) && (!this.m_boPreview.isEnabled()) && (!m_boBillCombin.isEnabled()))
      {
        this.bt.getButton("合并显示").setEnabled(false);
      }
      else this.bt.getButton("合并显示").setEnabled(true);

      if ((!this.boOpposeAct.isEnabled()) && (!this.m_boSoTax.isEnabled()) && (!this.m_boDocument.isEnabled()) && (!this.m_boImpInvoice.isEnabled()))
      {
        this.m_boAssistant.setEnabled(false);
      }
      else this.m_boAssistant.setEnabled(true);

      if ((!this.boOrderQuery.isEnabled()) && (!this.m_boATP.isEnabled()) && (!this.boAuditFlowStatus.isEnabled()) && (!this.m_boCustInfo.isEnabled()) && (!this.m_boExecRpt.isEnabled()) && (!this.m_boCredit.isEnabled()) && (!this.m_boPrifit.isEnabled()))
      {
        this.bt.getButton("辅助查询").setEnabled(false);
      }
      else this.bt.getButton("辅助查询").setEnabled(true);
    }
    else
    {
      if ((!this.m_boModify.isEnabled()) && (!this.m_boCancel.isEnabled()) && (!this.m_boBlankOut.isEnabled()) && (!this.m_boUnite.isEnabled()) && (!this.m_boUniteCancel.isEnabled()))
      {
        this.bt.getButton("维护").setEnabled(false);
      }
      else this.bt.getButton("维护").setEnabled(true);

      if ((!this.m_boAddLine.isEnabled()) && (!this.m_boDelLine.isEnabled()))
        this.m_boLineOper.setEnabled(false);
      else {
        this.m_boLineOper.setEnabled(true);
      }
      if ((!this.boSendAudit.isEnabled()) && (!this.m_boUnapprove.isEnabled()))
        this.m_boAction.setEnabled(false);
      else {
        this.m_boAction.setEnabled(true);
      }
      if ((!this.m_boLocal.isEnabled()) && (!this.m_boFirst.isCheckboxGroup()) && (!this.m_boPrev.isEnabled()) && (!this.m_boNext.isEnabled()) && (!this.m_boLast.isEnabled()))
      {
        this.m_boBrowse.setEnabled(false);
      }
      else this.m_boBrowse.setEnabled(true);

      if ((!this.m_boPrint.isEnabled()) && (!this.m_boPreview.isEnabled()) && (!m_boBillCombin.isEnabled()))
      {
        this.bt.getButton("合并显示").setEnabled(false);
      }
      else {
        this.bt.getButton("合并显示").setEnabled(true);
      }

      if ((!this.boOpposeAct.isEnabled()) && (!this.m_boSoTax.isEnabled()) && (!this.m_boDocument.isEnabled()) && (!this.m_boImpInvoice.isEnabled()))
      {
        this.m_boAssistant.setEnabled(false);
      }
      else this.m_boAssistant.setEnabled(true);

      if ((!this.boOrderQuery.isEnabled()) && (!this.m_boATP.isEnabled()) && (!this.boAuditFlowStatus.isEnabled()) && (!this.m_boCustInfo.isEnabled()) && (!this.m_boExecRpt.isEnabled()) && (!this.m_boCredit.isEnabled()) && (!this.m_boPrifit.isEnabled()))
      {
        this.bt.getButton("辅助查询").setEnabled(false);
      }
      else this.bt.getButton("辅助查询").setEnabled(true);
    }
  }

  private void setCardButtonsState()
  {
    this.m_boUniteInvoice.setEnabled(false);
    if (this.strState.equals("FREE")) {
      this.m_boModify.setEnabled(true);
      this.m_boCancel.setEnabled(false);
      this.m_boSave.setEnabled(false);
      this.m_boBlankOut.setEnabled(true);
      this.m_boApprove.setEnabled(false);
      this.m_boReturn.setEnabled(true);
      this.m_boAction.setEnabled(true);
      this.m_boBrowse.setEnabled(true);
      this.m_boLineOper.setEnabled(false);
      this.m_boDocument.setEnabled(true);
      this.boOrderQuery.setEnabled(true);
      this.bt.getButton("打印管理").setEnabled(true);
      this.m_boPreview.setEnabled(true);
      this.m_boPrint.setEnabled(true);
      this.m_boPrev.setEnabled(true);
      this.m_boNext.setEnabled(true);
      this.m_boAfterAction.setEnabled(false);
      m_boBillCombin.setEnabled(true);

      if (this.id == 0)
        this.m_boPrev.setEnabled(false);
      if (this.id == this.vIDs.size() - 1) {
        this.m_boNext.setEnabled(false);
      }
      getBillCardPanel().setEnabled(false);

      setState(this.iStatus);

      int row = getBillCardPanel().getBillTable().getSelectedRow();
      Object cfreezeid = null;
      if (row > -1)
        cfreezeid = getBillCardPanel().getBodyValueAt(row, "cfreezeid");
      if ((cfreezeid != null) && (cfreezeid.toString().trim().length() != 0))
      {
        this.boStockLock.setEnabled(false);
      }
      else if (this.iStatus == 2)
        this.boStockLock.setEnabled(true);
      else {
        this.boStockLock.setEnabled(false);
      }

      this.boSendAudit.setEnabled(true);
      this.boAuditFlowStatus.setEnabled(true);
    }

    if (this.strState.equals("CARD")) {
      this.m_boCancel.setEnabled(false);
      this.m_boSave.setEnabled(false);
      this.m_boReturn.setEnabled(true);
      this.m_boAction.setEnabled(true);
      this.m_boBrowse.setEnabled(true);
      this.m_boLineOper.setEnabled(false);
      this.bt.getButton("打印管理").setEnabled(true);
      this.m_boPrint.setEnabled(true);
      this.m_boPrint.setEnabled(true);
      this.m_boPrev.setEnabled(true);
      this.m_boNext.setEnabled(true);
      this.m_boAfterAction.setEnabled(false);

      if (this.id == 0)
        this.m_boPrev.setEnabled(false);
      if (this.id == this.vIDs.size() - 1) {
        this.m_boNext.setEnabled(false);
      }
      m_boBillCombin.setEnabled(true);
      getBillCardPanel().setEnabled(false);

      setState(this.iStatus);

      this.boSendAudit.setEnabled(true);
      this.boAuditFlowStatus.setEnabled(true);
    }
    if (this.strState.equals("MODIFY")) {
      this.m_boAction.setEnabled(false);
      this.m_boApprove.setEnabled(false);
      this.m_boBrowse.setEnabled(false);
      this.m_boPrint.setEnabled(false);
      this.m_boPreview.setEnabled(false);
      this.m_boModify.setEnabled(false);
      this.m_boCancel.setEnabled(true);
      this.m_boSave.setEnabled(true);
      this.m_boPrev.setEnabled(false);
      this.m_boNext.setEnabled(false);
      this.m_boReturn.setEnabled(false);
      this.m_boAfterAction.setEnabled(false);
      this.m_boLineOper.setEnabled(!this.bstrikeflag);
      this.m_boAddLine.setEnabled(true);
      this.m_boDelLine.setEnabled(true);
      this.m_boDocument.setEnabled(false);
      this.boOrderQuery.setEnabled(false);

      this.boSendAudit.setEnabled(false);
      this.boAuditFlowStatus.setEnabled(false);
      m_boBillCombin.setEnabled(false);

      UFDouble ntotalsummny = new UFDouble(getBillCardPanel().getHeadItem("ntotalsummny").getValue());
      boolean bislgtzero = true;
      if ((ntotalsummny == null) || (ntotalsummny.doubleValue() <= 0.0D))
        bislgtzero = false;
      this.m_boUnite.setEnabled(bislgtzero);
      this.m_boUniteInvoice.setEnabled(bislgtzero);
      this.m_boUniteCancel.setEnabled((this.bstrikeflag) && (bislgtzero));
      getBillCardPanel().setEnabled(true);

      int iOppStats = Integer.valueOf(getBillCardPanel().getHeadItem("fcounteractflag").getValue()).intValue();

      if (iOppStats == 2) {
        this.m_boUnite.setEnabled(false);
        this.m_boUniteInvoice.setEnabled(false);
        this.m_boUniteCancel.setEnabled(false);
        this.m_boLineOper.setEnabled(false);
        this.m_boAddLine.setEnabled(false);
        this.m_boDelLine.setEnabled(false);
      }
    }
    if (this.strState.equals("OPP")) {
      this.m_boAction.setEnabled(false);
      this.m_boBrowse.setEnabled(false);
      this.m_boPrint.setEnabled(false);
      this.m_boPreview.setEnabled(false);
      this.m_boModify.setEnabled(false);
      this.m_boCancel.setEnabled(true);
      this.m_boSave.setEnabled(true);
      this.m_boPrev.setEnabled(false);
      this.m_boNext.setEnabled(false);
      this.m_boReturn.setEnabled(false);
      this.m_boAfterAction.setEnabled(false);
      this.m_boLineOper.setEnabled(false);
      this.m_boAddLine.setEnabled(false);
      this.m_boDelLine.setEnabled(false);
      this.m_boDocument.setEnabled(false);
      this.boOrderQuery.setEnabled(false);

      this.boAuditFlowStatus.setEnabled(false);

      this.m_boBlankOut.setEnabled(false);
      this.m_boModify.setEnabled(false);
      this.m_boApprove.setEnabled(false);
      this.m_boUnapprove.setEnabled(false);
      this.m_boAfterAction.setEnabled(false);
      this.boStockLock.setEnabled(false);
      this.boSendAudit.setEnabled(true);

      this.m_boUnite.setEnabled(false);
      this.m_boUniteInvoice.setEnabled(false);
      this.m_boUniteCancel.setEnabled(false);
      getBillCardPanel().setEnabled(true);
    }

    if (this.strState.equals("LIST")) {
      this.m_boBrowse.setEnabled(true);
      this.m_boModify.setEnabled(true);
      this.m_boApprove.setEnabled(true);
      this.m_boAction.setEnabled(true);
      this.m_boPrint.setEnabled(true);
      this.m_boPreview.setEnabled(true);
      this.m_boAfterAction.setEnabled(false);
      this.m_boLineOper.setEnabled(false);

      this.boSendAudit.setEnabled(true);
      this.boAuditFlowStatus.setEnabled(true);
    }

    setExtendBtnsStat(this.iStatus);

    updateButtons();
  }

  public void setCardPanel(BillData bdData)
  {
    setCardPanelByPara(bdData);

    UIRefPane ref = (UIRefPane)bdData.getHeadItem("cfreecustid").getComponent();
    UFRefGridUI refui = new UFRefGridUI();
    ref.getRef().setRefUI(refui);

    UIRefPane refHeadAddress = (UIRefPane)bdData.getHeadItem("vreceiveaddress").getComponent();

    refHeadAddress.setAutoCheck(false);
    refHeadAddress.setReturnCode(true);
  }
  public void setBodyItemsEdit(BillData billdata, String[] keys, boolean editable) {
    if ((keys == null) || (keys.length <= 0))
      return;
    int i = 0; for (int loop = keys.length; i < loop; i++)
      try {
        if (billdata.getBodyItem(keys[i]) != null)
          billdata.getBodyItem(keys[i]).setEdit(editable);
      } catch (Exception e) {
        SCMEnv.out("key:" + keys[i] + " set editable fail ");
      }
  }

  public void getInitBillItemEidtState()
  {
    this.hsBIEnable.clear();
    if (getBillCardPanel() != null) {
      BillItem[] items = getBillCardPanel().getBillData().getHeadItems();
      if (items != null) {
        int i = 0; for (int loop = items.length; i < loop; i++) {
          this.hsBIEnable.put("H_" + items[i].getKey(), items[i].isEdit() ? new UFBoolean(true) : new UFBoolean(false));
        }
      }

      items = getBillCardPanel().getBillData().getBodyItems();
      if (items != null) {
        int i = 0; for (int loop = items.length; i < loop; i++) {
          this.hsBIEnable.put("B_" + items[i].getKey(), items[i].isEdit() ? new UFBoolean(true) : new UFBoolean(false));
        }
      }

      items = getBillCardPanel().getBillData().getTailItems();
      if (items != null) {
        int i = 0; for (int loop = items.length; i < loop; i++)
          this.hsBIEnable.put("T_" + items[i].getKey(), items[i].isEdit() ? new UFBoolean(true) : new UFBoolean(false));
      }
    }
  }

  public void resumeBillBodyItemEdit(BillItem BillItem)
  {
    if (getBillCardPanel() != null) {
      UFBoolean btemp = null;
      if (BillItem != null) {
        btemp = (UFBoolean)this.hsBIEnable.get("B_" + BillItem.getKey());

        if (btemp != null)
          BillItem.setEdit(btemp.booleanValue());
      }
    }
  }

  protected void setCardPanelByPara(BillData bdData)
  {
    if ((this.SA_15 != null) && (this.SA_15.booleanValue()) && (this.SA_08 != null) && (!this.SA_08.booleanValue()))
    {
      setBodyItemsEdit(bdData, SOBillCardTools.getSaleItems_Price(), false);
    }

    if (this.SA_15.booleanValue())
    {
      if (this.SA_08.booleanValue()) {
        if (this.SA_02.booleanValue()) {
          bdData.getBodyItem("noriginalcurtaxprice").setEdit(false);
          bdData.getBodyItem("nquoteoriginalcurtaxprice").setEdit(false);
        } else {
          bdData.getBodyItem("noriginalcurtaxprice").setEdit(true);
          bdData.getBodyItem("nquoteoriginalcurtaxprice").setEdit(true);
        }

      }

    }

    if ((this.SA_02 != null) && (this.SA_02.booleanValue()))
    {
      if (bdData.getBodyItem("noriginalcurmny") != null)
        bdData.getBodyItem("noriginalcurmny").setEdit(false);
      else {
        System.out.println(" bdData.getBodyItem(noriginalcurmny) is null ");
      }

      if (bdData.getBodyItem("noriginalcursummny") != null) {
        resumeBillBodyItemEdit(bdData.getBodyItem("noriginalcursummny"));
      }
      else
        System.out.println(" bdData.getBodyItem(noriginalcursummny) is null ");
    }
    else
    {
      if (bdData.getBodyItem("noriginalcurmny") != null)
      {
        resumeBillBodyItemEdit(bdData.getBodyItem("noriginalcurmny"));
      }
      else {
        System.out.println(" bdData.getBodyItem(noriginalcurmny) is null ");
      }

      if (bdData.getBodyItem("noriginalcursummny") != null)
        bdData.getBodyItem("noriginalcursummny").setEdit(false);
      else {
        System.out.println(" bdData.getBodyItem(noriginalcursummny) is null ");
      }
    }

    if ((this.BD302 != null) && (!this.BD302.booleanValue())) {
      bdData.getHeadItem("nexchangeotoarate").setShow(false);
    }

    bdData.getHeadItem("nexchangeotobrate").setDecimalDigits(4);
    bdData.getHeadItem("nexchangeotoarate").setDecimalDigits(4);
    bdData.getBodyItem("nexchangeotobrate").setDecimalDigits(4);
    bdData.getBodyItem("nexchangeotoarate").setDecimalDigits(4);

    if (this.BD501 != null) {
      bdData.getBodyItem("nnumber").setDecimalDigits(this.BD501.intValue());
    }
    if (this.BD502 != null) {
      bdData.getBodyItem("npacknumber").setDecimalDigits(this.BD502.intValue());
    }

    if (this.BD505 != null) {
      bdData.getBodyItem("noriginalcurprice").setDecimalDigits(this.BD505.intValue());

      bdData.getBodyItem("noriginalcurtaxprice").setDecimalDigits(this.BD505.intValue());

      bdData.getBodyItem("noriginalcurnetprice").setDecimalDigits(this.BD505.intValue());

      bdData.getBodyItem("noriginalcurtaxnetprice").setDecimalDigits(this.BD505.intValue());

      bdData.getBodyItem("nprice").setDecimalDigits(this.BD505.intValue());
      bdData.getBodyItem("ntaxprice").setDecimalDigits(this.BD505.intValue());
      bdData.getBodyItem("nnetprice").setDecimalDigits(this.BD505.intValue());
      bdData.getBodyItem("ntaxnetprice").setDecimalDigits(this.BD505.intValue());

      bdData.getBodyItem("nassistcurtaxnetprice").setDecimalDigits(this.BD505.intValue());

      bdData.getBodyItem("nassistcurnetprice").setDecimalDigits(this.BD505.intValue());

      bdData.getBodyItem("nassistcurtaxprice").setDecimalDigits(this.BD505.intValue());

      bdData.getBodyItem("nassistcurprice").setDecimalDigits(this.BD505.intValue());

      bdData.getBodyItem("norgviaprice").setDecimalDigits(this.BD505.intValue());

      bdData.getBodyItem("nsubtaxnetprice").setDecimalDigits(this.BD505.intValue());
    }

    if (this.BD503 != null) {
      bdData.getBodyItem("scalefactor").setDecimalDigits(this.BD503.intValue());
    }

    if ((bdData.getBodyItem("nquoteoriginalcurprice").isShow()) || (bdData.getBodyItem("nquoteoriginalcurtaxprice").isShow()) || (bdData.getBodyItem("nquoteoriginalcurnetprice").isShow()) || (bdData.getBodyItem("nquoteoriginalcurtaxnetprice").isShow()))
    {
      bdData.getBodyItem("noriginalcurprice").setEdit(false);
      bdData.getBodyItem("noriginalcurtaxprice").setEdit(false);
      bdData.getBodyItem("noriginalcurtaxnetprice").setEdit(false);
      bdData.getBodyItem("noriginalcurnetprice").setEdit(false);
    }

    if (bdData.getBodyItem("nquotenumber").isShow()) {
      bdData.getBodyItem("nnumber").setEdit(false);
      bdData.getBodyItem("nquotenumber").setEdit(true);
    }
  }

  public void setHeadComboBox()
  {
    try
    {
      UIComboBox comHead = (UIComboBox)getBillCardPanel().getHeadItem("fstatus").getComponent();

      getBillCardPanel().getHeadItem("fstatus").setWithIndex(true);
      comHead.setTranslate(true);
      comHead.addItem("");
      comHead.addItem(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000340"));

      comHead.addItem(NCLangRes.getInstance().getStrByID("common", "UC001-0000027"));

      comHead.addItem("");
      comHead.addItem("");
      comHead.addItem(NCLangRes.getInstance().getStrByID("common", "UC001-0000005"));

      comHead.addItem("");
      comHead.addItem(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000151"));

      comHead.addItem(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000242"));

      String[][] invoiceType = SaleinvoiceBO_Client.getInvoiceType();
      UIComboBox comtype = (UIComboBox)getBillCardPanel().getHeadItem("finvoicetype").getComponent();

      comtype.setTranslate(true);
      getBillCardPanel().getHeadItem("finvoicetype").setWithIndex(true);
      for (int i = 0; i < invoiceType.length; i++) {
        comtype.addItem(invoiceType[i][1]);
      }
      UIComboBox comCont = (UIComboBox)getBillCardPanel().getHeadItem("fcounteractflag").getComponent();

      getBillCardPanel().getHeadItem("fcounteractflag").setWithIndex(true);
      comCont.setTranslate(true);
      comCont.addItem(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000156"));

      comCont.addItem(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000157"));

      comCont.addItem(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000158"));
    }
    catch (Exception e)
    {
      SCMEnv.out("发票类型查询失败！");
      SCMEnv.out(e.getMessage());
    }
  }

  protected void setInputLimit()
  {
    UIRefPane ref = (UIRefPane)getBillCardPanel().getHeadItem("creceiptcorpid").getComponent();

    ref.getRefModel().addWherePart(" and bd_cumandoc.frozenflag = 'N'");
  }

  private void setListButtonsState()
  {
    this.m_boBrowse.setEnabled(true);

    this.m_boUniteInvoice.setEnabled(true);
    this.m_boUnite.setEnabled(true);
    this.m_boUniteCancel.setEnabled(true);

    if (this.selectRow > -1) {
      this.m_boModify.setEnabled(true);
      this.m_boApprove.setEnabled(true);
      this.m_boAction.setEnabled(true);
      this.m_boPrint.setEnabled(true);
      this.m_boPreview.setEnabled(true);
      this.m_boAssistant.setEnabled(true);
      this.m_boATP.setEnabled(true);
      this.m_boCustInfo.setEnabled(true);
      this.m_boCredit.setEnabled(true);
      this.m_boPrifit.setEnabled(true);
      this.m_boExecRpt.setEnabled(true);
      this.m_boSoTax.setEnabled(true);
      this.m_boImpInvoice.setEnabled(true);
      this.bt.getButton("辅助查询").setEnabled(true);
      this.m_boCard.setEnabled(true);
      if (this.m_boAfterAction != null)
        this.m_boAfterAction.setEnabled(false);
      this.m_boDocument.setEnabled(true);
      this.boOrderQuery.setEnabled(true);
      this.boSendAudit.setEnabled(true);
      this.boAuditFlowStatus.setEnabled(true);

      UFDouble ntotalsummny = (UFDouble)getBillListPanel().getHeadBillModel().getValueAt(this.selectRow, "ntotalsummny");

      boolean bislgtzero = true;
      if ((ntotalsummny == null) || (ntotalsummny.doubleValue() <= 0.0D))
        bislgtzero = false;
      this.m_boUnite.setEnabled(bislgtzero);
      this.m_boUniteInvoice.setEnabled(bislgtzero);
      this.m_boUniteCancel.setEnabled((this.bstrikeflag) && (bislgtzero));
    }
    else {
      this.m_boModify.setEnabled(false);
      this.m_boApprove.setEnabled(false);
      this.m_boAction.setEnabled(false);
      this.m_boBlankOut.setEnabled(false);
      this.bt.getButton("打印管理").setEnabled(false);

      this.m_boPrint.setEnabled(false);
      this.m_boPreview.setEnabled(false);

      this.m_boATP.setEnabled(false);
      this.m_boCustInfo.setEnabled(false);
      this.m_boCredit.setEnabled(false);
      this.m_boPrifit.setEnabled(false);
      this.m_boExecRpt.setEnabled(false);
      this.m_boSoTax.setEnabled(false);
      this.m_boImpInvoice.setEnabled(false);
      this.m_boCard.setEnabled(false);

      if (this.m_boAfterAction != null)
        this.m_boAfterAction.setEnabled(false);
      this.m_boDocument.setEnabled(false);
      this.boOrderQuery.setEnabled(false);
      this.bt.getButton("辅助查询").setEnabled(false);
      this.boSendAudit.setEnabled(false);
      this.boAuditFlowStatus.setEnabled(false);
    }

    if (getBillListPanel().getHeadTable().getRowCount() == 0) {
      this.m_boBrowse.setEnabled(false);
      this.m_boLocal.setEnabled(false);
    }
    else {
      this.m_boBrowse.setEnabled(true);
      this.m_boLocal.setEnabled(true);
    }
    if (this.selectRow > -1)
    {
      if ((getBillListPanel().getHeadItem("fstatus") != null) && (getBillListPanel().getHeadBillModel().getValueAt(this.selectRow, "fstatus") != null) && (getBillListPanel().getHeadItem("fstatus").converType(getBillListPanel().getHeadBillModel().getValueAt(this.selectRow, "fstatus")) != null))
      {
        int iRowStatus = Integer.parseInt(getBillListPanel().getHeadItem("fstatus").converType(getBillListPanel().getHeadBillModel().getValueAt(this.selectRow, "fstatus")).toString());

        setState(iRowStatus);
      }

    }

    updateButtons();
  }

  protected void setListPanelByPara(BillListData bdData)
  {
    bdData.getBodyItem("nexchangeotobrate").setDecimalDigits(4);
    bdData.getBodyItem("nexchangeotoarate").setDecimalDigits(4);

    String[] aryNum = { "nnumber" };
    if (this.BD501 != null) {
      for (int i = 0; i < aryNum.length; i++) {
        bdData.getBodyItem(aryNum[i]).setDecimalDigits(this.BD501.intValue());
      }
    }

    if (this.BD502 != null) {
      bdData.getBodyItem("npacknumber").setDecimalDigits(this.BD502.intValue());

      bdData.getBodyItem("nquotenumber").setDecimalDigits(this.BD502.intValue());
    }

    if (this.BD505 != null) {
      bdData.getBodyItem("noriginalcurprice").setDecimalDigits(this.BD505.intValue());

      bdData.getBodyItem("noriginalcurtaxprice").setDecimalDigits(this.BD505.intValue());

      bdData.getBodyItem("noriginalcurnetprice").setDecimalDigits(this.BD505.intValue());

      bdData.getBodyItem("noriginalcurtaxnetprice").setDecimalDigits(this.BD505.intValue());

      bdData.getBodyItem("nquoteoriginalcurtaxprice").setDecimalDigits(this.BD505.intValue());

      bdData.getBodyItem("nquoteoriginalcurprice").setDecimalDigits(this.BD505.intValue());

      bdData.getBodyItem("nquoteoriginalcurtaxnetprice").setDecimalDigits(this.BD505.intValue());

      bdData.getBodyItem("nquoteoriginalcurnetprice").setDecimalDigits(this.BD505.intValue());

      bdData.getBodyItem("nprice").setDecimalDigits(this.BD505.intValue());
      bdData.getBodyItem("ntaxprice").setDecimalDigits(this.BD505.intValue());
      bdData.getBodyItem("nnetprice").setDecimalDigits(this.BD505.intValue());
      bdData.getBodyItem("ntaxnetprice").setDecimalDigits(this.BD505.intValue());

      bdData.getBodyItem("nquotetaxnetprice").setDecimalDigits(this.BD505.intValue());

      bdData.getBodyItem("nquotenetprice").setDecimalDigits(this.BD505.intValue());

      bdData.getBodyItem("nquotetaxprice").setDecimalDigits(this.BD505.intValue());

      bdData.getBodyItem("nquoteprice").setDecimalDigits(this.BD505.intValue());

      bdData.getBodyItem("nsubquoteprice").setDecimalDigits(this.BD505.intValue());

      bdData.getBodyItem("nsubquotetaxprice").setDecimalDigits(this.BD505.intValue());

      bdData.getBodyItem("nsubquotenetprice").setDecimalDigits(this.BD505.intValue());

      bdData.getBodyItem("nsubquotetaxnetprice").setDecimalDigits(this.BD505.intValue());

      bdData.getBodyItem("nassistcurtaxnetprice").setDecimalDigits(this.BD505.intValue());

      bdData.getBodyItem("nassistcurnetprice").setDecimalDigits(this.BD505.intValue());

      bdData.getBodyItem("nassistcurtaxprice").setDecimalDigits(this.BD505.intValue());

      bdData.getBodyItem("nassistcurprice").setDecimalDigits(this.BD505.intValue());

      bdData.getBodyItem("norgviaprice").setDecimalDigits(this.BD505.intValue());

      bdData.getBodyItem("norgviapricetax").setDecimalDigits(this.BD505.intValue());

      bdData.getBodyItem("nsubtaxnetprice").setDecimalDigits(this.BD505.intValue());
    }

    if (this.BD503 != null) {
      bdData.getBodyItem("scalefactor").setDecimalDigits(this.BD503.intValue());

      bdData.getBodyItem("nqtscalefactor").setDecimalDigits(this.BD503.intValue());
    }

    if ((bdData.getBodyItem("nquoteoriginalcurprice").isShow()) || (bdData.getBodyItem("nquoteoriginalcurtaxprice").isShow()) || (bdData.getBodyItem("nquoteoriginalcurnetprice").isShow()) || (bdData.getBodyItem("nquoteoriginalcurtaxnetprice").isShow()))
    {
      bdData.getBodyItem("noriginalcurprice").setEdit(false);
      bdData.getBodyItem("noriginalcurtaxprice").setEdit(false);
      bdData.getBodyItem("noriginalcurtaxnetprice").setEdit(false);
      bdData.getBodyItem("noriginalcurnetprice").setEdit(false);
    }
  }

  public void setNoEditItem()
  {
    if ((getBillCardPanel().getHeadItem("bfreecustflag").getValue() == null) || (getBillCardPanel().getHeadItem("bfreecustflag").getValue().equals("false")))
    {
      getBillCardPanel().getHeadItem("cfreecustid").setEnabled(false);
    } else {
      getBillCardPanel().getHeadItem("cfreecustid").setEnabled(true);
      getBillCardPanel().getHeadItem("cfreecustid").setEdit(true);
    }

    for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
      setAssistUnit(i);
    }

    getBillCardTools().setCardPanelCellEditableByLargess(this.SO59.booleanValue());
  }

  protected void setPanelByCurrency(String ccurrencytypeid)
  {
    try
    {
      if ((ccurrencytypeid == null) || (ccurrencytypeid.length() == 0))
        return;
      CurrinfoVO currVO = null;
      CurrinfoVO localcurrVO = null;
      CurrinfoVO astcurrVO = null;

      CurrtypeVO currtypeVO = null;
      CurrtypeVO localcurrtypeVO = null;
      CurrtypeVO astcurrtypeVO = null;
      CurrtypeQuery currtypeQuery = CurrtypeQuery.getInstance();

      int digitRate = -1;
      try
      {
        currVO = this.currtype.getCurrinfoVO(ccurrencytypeid, null);
        if ((currVO == null) || (currVO.getPk_currtype() == null) || (currVO.getPk_currtype().trim().length() <= 0))
        {
          initCurrency();
          currVO = this.currtype.getCurrinfoVO(ccurrencytypeid, null);
        }
        currtypeVO = currtypeQuery.getCurrtypeVO(ccurrencytypeid);

        localcurrVO = this.currtype.getCurrinfoVO(this.currtype.getLocalCurrPK(), null);
        localcurrtypeVO = currtypeQuery.getCurrtypeVO(this.currtype.getLocalCurrPK());

        if ((this.BD302 != null) && (this.BD302.booleanValue())) {
          astcurrVO = this.currtype.getCurrinfoVO(this.currtype.getFracCurrPK(), null);
          astcurrtypeVO = currtypeQuery.getCurrtypeVO(this.currtype.getFracCurrPK());
        }

        digitRate = currVO.getRatedigit() == null ? 4 : currVO.getRatedigit().intValue();
        SCMEnv.out(digitRate);
      } catch (Exception ex) {
        try {
          initCurrency();

          currVO = this.currtype.getCurrinfoVO(ccurrencytypeid, null);
          currtypeVO = currtypeQuery.getCurrtypeVO(ccurrencytypeid);

          localcurrVO = this.currtype.getCurrinfoVO(this.currtype.getLocalCurrPK(), null);
          localcurrtypeVO = currtypeQuery.getCurrtypeVO(this.currtype.getLocalCurrPK());

          if ((this.BD302 != null) && (this.BD302.booleanValue())) {
            astcurrVO = this.currtype.getCurrinfoVO(this.currtype.getFracCurrPK(), null);
            astcurrtypeVO = currtypeQuery.getCurrtypeVO(this.currtype.getFracCurrPK());
          }

          digitRate = currVO.getRatedigit() == null ? 4 : currVO.getRatedigit().intValue();
        } catch (Exception exx) {
          digitRate = getBillCardPanel().getHeadItem("nexchangeotobrate").getDecimalDigits();

          System.out.println("currVO.getRatedigit().intValue() erro!");
        }
      }

      String[] aryRate = { "nexchangeotobrate", "nexchangeotoarate" };

      if ((this.BD302 == null) || (!this.BD302.booleanValue())) {
        try {
          for (int i = 0; i < aryRate.length; i++)
          {
            getBillCardPanel().getHeadItem(aryRate[i]).setDecimalDigits(digitRate);

            getBillCardPanel().getBodyItem(aryRate[i]).setDecimalDigits(digitRate);

            String name = getBillCardPanel().getBodyItem(aryRate[i]).getName();

            if (getBillCardPanel().getBodyPanel().hasShowCol(name))
              getBillCardPanel().getBodyPanel().resetTableCellRenderer(name);
          }
        }
        catch (Exception ex1) {
          System.out.println("into if (BD302 == null || !BD302.booleanValue()) erro!");
        }
      }
      else {
        try
        {
          CurrinfoVO currVOa = this.currtype.getCurrinfoVO(this.currtype.getFracCurrPK(), null);

          int curRate = currVOa.getRatedigit().intValue();
          SCMEnv.out(curRate);

          getBillCardPanel().getHeadItem("nexchangeotobrate").setDecimalDigits(curRate);

          getBillCardPanel().getHeadItem("nexchangeotoarate").setDecimalDigits(digitRate);

          getBillCardPanel().getBodyItem("nexchangeotobrate").setDecimalDigits(curRate);

          getBillCardPanel().getBodyItem("nexchangeotoarate").setDecimalDigits(digitRate);

          for (int i = 0; i < aryRate.length; i++) {
            String name = getBillCardPanel().getBodyItem(aryRate[i]).getName();

            if (getBillCardPanel().getBodyPanel().hasShowCol(name))
              getBillCardPanel().getBodyPanel().resetTableCellRenderer(name);
          }
        }
        catch (Exception e) {
          System.out.println("into else (BD302 == null || !BD302.booleanValue()) erro!");
        }

      }

      int localdigit = 4;
      int astdigit = 4;
      try
      {
        this.digit = (currtypeVO.getCurrbusidigit() == null ? 4 : currtypeVO.getCurrbusidigit().intValue());
        localdigit = localcurrtypeVO.getCurrbusidigit() == null ? 4 : localcurrtypeVO.getCurrbusidigit().intValue();

        if (astcurrtypeVO != null) {
          astdigit = astcurrtypeVO.getCurrbusidigit() == null ? 4 : astcurrtypeVO.getCurrbusidigit().intValue();
        }
        else {
          astdigit = this.digit;
        }
        SCMEnv.out(this.digit);
      } catch (Exception ex2) {
        this.digit = getBillCardPanel().getBodyItem("noriginalcursummny").getDecimalDigits();

        System.out.println("digit = currVO.getCurrdigit().intValue() erro!");
      }

      String[] fieldnames = { "noriginalcurtaxmny", "noriginalcurmny", "noriginalcursummny", "noriginalcurdiscountmny", "nsubsummny", "nuniteinvoicemny" };

      for (int i = 0; i < fieldnames.length; i++) {
        getBillCardPanel().getBillData().getBodyItem(fieldnames[i]).setDecimalDigits(this.digit);

        String name = getBillCardPanel().getBodyItem(fieldnames[i]).getName();

        if (getBillCardPanel().getBodyPanel().hasShowCol(name)) {
          getBillCardPanel().getBodyPanel().resetTableCellRenderer(name);
        }

      }

      fieldnames = new String[] { "ntaxmny", "nmny", "nsummny", "ndiscountmny", "nsubcursummny" };

      for (int i = 0; i < fieldnames.length; i++) {
        getBillCardPanel().getBillData().getBodyItem(fieldnames[i]).setDecimalDigits(localdigit);

        String name = getBillCardPanel().getBodyItem(fieldnames[i]).getName();

        if (getBillCardPanel().getBodyPanel().hasShowCol(name)) {
          getBillCardPanel().getBodyPanel().resetTableCellRenderer(name);
        }

      }

      fieldnames = new String[] { "nassistcurdiscountmny", "nassistcursummny", "nassistcurmny", "nassistcurtaxmny" };

      for (int i = 0; i < fieldnames.length; i++) {
        getBillCardPanel().getBillData().getBodyItem(fieldnames[i]).setDecimalDigits(astdigit);

        String name = getBillCardPanel().getBodyItem(fieldnames[i]).getName();

        if (getBillCardPanel().getBodyPanel().hasShowCol(name)) {
          getBillCardPanel().getBodyPanel().resetTableCellRenderer(name);
        }
      }

      String[] headfieldnames = { "nstrikemny", "nnetmny", "ntotalsummny" };
      for (int j = 0; j < headfieldnames.length; j++) {
        if (getBillCardPanel().getHeadItem(headfieldnames[j]) != null) {
          getBillCardPanel().getHeadItem(headfieldnames[j]).setDecimalDigits(this.digit);
        }

      }

      String[] bodypricefields = { "nquoteoriginalcurprice", "nquoteoriginalcurtaxprice", "nquoteoriginalcurnetprice", "nquoteoriginalcurtaxnetprice", "nquoteprice", "nquotetaxprice", "nquotenetprice", "nquotetaxnetprice", "nsubquoteprice", "nsubquotetaxprice", "nsubquotenetprice", "nsubquotetaxnetprice", "nsubtaxnetprice" };

      for (int i = 0; i < bodypricefields.length; i++) {
        if (getBillCardPanel().getBillData().getBodyItem(bodypricefields[i]) == null)
          continue;
        getBillCardPanel().getBillData().getBodyItem(bodypricefields[i]).setDecimalDigits(this.BD505.intValue());
      }

    }
    catch (Exception e)
    {
      SCMEnv.out("根据币种设置小数位数失败!");
      SCMEnv.out(e.getMessage());
    }
  }

  public void setShowDataState(int row, String state)
  {
    Integer value = new Integer(0);
    try {
      if (state.equals("APPROVE")) {
        String sBillID = getBillListPanel().getHeadBillModel().getValueAt(this.selectRow, "csaleid").toString();

        String strWhere = "so_saleinvoice.csaleid = '" + sBillID + "'";

        if (sBillID != null) {
          SaleVO[] salehvo = SaleinvoiceBO_Client.queryHeadAllData(strWhere);

          if ((salehvo != null) && (salehvo.length > 0)) {
            value = salehvo[0].getFstatus();
          }

        }

        getBillListPanel().getHeadBillModel().setValueAt(getClientEnvironment().getUser().getUserName().toString(), row, "capprovename");

        getBillListPanel().getHeadBillModel().setValueAt(getClientEnvironment().getUser().getPrimaryKey(), row, "capproveid");

        getBillListPanel().getHeadBillModel().setValueAt(getClientEnvironment().getDate().toString(), row, "dapprovedate");
      }
      else if (state.equals("SoUnApprove")) {
        value = new Integer(1);

        getBillListPanel().getHeadBillModel().setValueAt(null, row, "capprovename");

        getBillListPanel().getHeadBillModel().setValueAt(null, row, "dapprovedate");

        getBillListPanel().getHeadBillModel().setValueAt(null, row, "capproveid");
      }
      else if (state.equals("SoBlankOut")) {
        value = new Integer(5);
      }

      getBillListPanel().getHeadBillModel().setValueAt(value, row, "fstatus");

      if (getBillListPanel().getBodyBillModel() != null) {
        for (int i = 0; i < getBillListPanel().getBodyBillModel().getRowCount(); )
        {
          getBillListPanel().getBodyBillModel().setValueAt(value, i, "frowstatus");

          i++;
        }
      }
    }
    catch (Exception e)
    {
      SCMEnv.out(e.getMessage());
      showWarningMessage(e.getMessage());
    }
  }

  private void setState(int iState)
  {
    switch (iState) {
    case 1:
      this.m_boBlankOut.setEnabled(true);
      this.m_boModify.setEnabled(true);
      this.m_boApprove.setEnabled(true);
      this.m_boUnapprove.setEnabled(false);
      this.m_boAfterAction.setEnabled(false);
      this.boStockLock.setEnabled(false);
      this.m_boDocument.setEnabled(true);
      this.boOrderQuery.setEnabled(true);
      this.boSendAudit.setEnabled(true);

      this.strState = "FREE";
      break;
    case 2:
      this.m_boBlankOut.setEnabled(false);
      this.m_boApprove.setEnabled(false);
      this.m_boUnapprove.setEnabled(true);
      this.m_boModify.setEnabled(false);
      this.m_boAfterAction.setEnabled(true);
      this.boStockLock.setEnabled(false);
      this.m_boDocument.setEnabled(true);
      this.boOrderQuery.setEnabled(true);
      this.boSendAudit.setEnabled(false);

      this.strState = "AUDIT";
      break;
    case 5:
      this.m_boModify.setEnabled(false);
      this.m_boAction.setEnabled(false);
      this.m_boAssistant.setEnabled(false);
      this.m_boAfterAction.setEnabled(false);
      this.m_boPrint.setEnabled(false);
      this.boStockLock.setEnabled(false);
      this.m_boDocument.setEnabled(false);
      this.boOrderQuery.setEnabled(false);
      this.boSendAudit.setEnabled(false);

      this.m_boPreview.setEnabled(false);

      this.boAuditFlowStatus.setEnabled(false);

      this.strState = "BLANKOUT";
      break;
    case 7:
      this.m_boBlankOut.setEnabled(false);
      this.m_boApprove.setEnabled(true);
      this.m_boUnapprove.setEnabled(false);
      this.boStockLock.setEnabled(false);

      this.m_boAfterAction.setEnabled(false);
      this.m_boSave.setEnabled(false);
      this.m_boUpdate.setEnabled(false);
      this.m_boModify.setEnabled(false);

      this.strState = "AUDITING";
      break;
    case 8:
      this.m_boBlankOut.setEnabled(true);
      this.m_boModify.setEnabled(true);
      this.m_boSave.setEnabled(false);
      this.m_boUpdate.setEnabled(false);
      this.m_boCancel.setEnabled(false);
      this.m_boApprove.setEnabled(true);
      this.m_boUnapprove.setEnabled(false);
      this.m_boAfterAction.setEnabled(false);
      this.boStockLock.setEnabled(false);
      this.m_boDocument.setEnabled(true);
      this.boOrderQuery.setEnabled(true);
      this.boSendAudit.setEnabled(true);

      this.strState = "NOPASS";
    case 3:
    case 4:
    case 6:
    }
    int iOppStatus = 0;
    if (this.strShowState.equals("LIST")) {
      if (this.selectRow > -1)
      {
        if ((getBillListPanel().getHeadItem("fcounteractflag") != null) && (getBillListPanel().getHeadBillModel().getValueAt(this.selectRow, "fcounteractflag") != null) && (getBillListPanel().getHeadItem("fcounteractflag").converType(getBillListPanel().getHeadBillModel().getValueAt(this.selectRow, "fcounteractflag")) != null))
        {
          iOppStatus = Integer.parseInt(getBillListPanel().getHeadItem("fcounteractflag").converType(getBillListPanel().getHeadBillModel().getValueAt(this.selectRow, "fcounteractflag")).toString());
        }

      }

    }
    else
    {
      iOppStatus = Integer.valueOf(getBillCardPanel().getHeadItem("fcounteractflag").getValue()).intValue();
    }

    if ((iState == 2) && (iOppStatus == 1)) {
      this.m_boUnapprove.setEnabled(false);
    }

    if ((iState == 2) && (iOppStatus == 0)) {
      this.boOpposeAct.setEnabled(true);
    }
    else {
      this.boOpposeAct.setEnabled(false);
      if ((!this.m_boSoTax.isEnabled()) && (!this.m_boDocument.isEnabled())) {
        this.m_boAssistant.setEnabled(false);
      }
    }

    setExtendBtnsStat(iState);

    updateButtons();
  }

  private UFDouble computeViaPriceTax(int iRow)
  {
    Object obj1 = getBillCardPanel().getBodyValueAt(iRow, "noriginalcurtaxprice");

    Object obj3 = getBillCardPanel().getBodyValueAt(iRow, "nnumber");

    Object obj4 = getBillCardPanel().getBodyValueAt(iRow, "npacknumber");

    if ((obj1 != null) && (obj3 != null) && (obj4 != null))
    {
      return ((UFDouble)obj1).multiply(((UFDouble)obj3).div((UFDouble)obj4));
    }

    return null;
  }

  protected void fillCacheByListPanel()
  {
    SaleVO[] hvos = (SaleVO[])getBillListPanel().getHeadBillModel().getBodyValueVOs("nc.vo.so.so002.SaleVO");

    SaleinvoiceVO[] saleinvoicevos = new SaleinvoiceVO[hvos.length];
    for (int i = 0; i < hvos.length; i++) {
      saleinvoicevos[i] = new SaleinvoiceVO();
      saleinvoicevos[i].setParentVO(hvos[i]);
    }
    this.vocache.setCacheData(saleinvoicevos);
  }

  public UFDouble getParaDouble(Hashtable h, String key)
  {
    String str = (String)h.get(key);
    if (str == null)
      return new UFDouble(0);
    return new UFDouble(str);
  }

  protected QueryConditionClient getStrikeQueryDlg()
  {
    this.dlgStrikeQuery = new QueryConditionClient(this);
    this.dlgStrikeQuery.setTempletID(getCorpPrimaryKey(), "40069907", getClientEnvironment().getUser().getPrimaryKey(), getBillCardPanel().getBusiType());

    this.dlgStrikeQuery.hideNormal();
    this.dlgStrikeQuery.setIsWarningWithNoInput(true);

    this.dlgStrikeQuery.setDefaultValue(null, "csourcebilltype", null, SaleInvoiceUI.sAll);

    this.dlgStrikeQuery.setDefaultValue(null, "pk_corp", ClientEnvironment.getInstance().getCorporation().getPrimaryKey(), null);

    String ccurrencytypeid = getBillCardPanel().getHeadItem("ccurrencyid").getValue();

    this.dlgStrikeQuery.setDefaultValue(null, "ccurrencytypeid", ccurrencytypeid, null);

    String ccustomerid = getCCustomerid(true);
    this.dlgStrikeQuery.setDefaultValue(null, "ccustomerid", ccustomerid, null);

    return this.dlgStrikeQuery;
  }

  private String getCCustomerid(boolean isCard)
  {
    if (!isCard) {
      if (getBillListPanel().getBodyTable().getRowCount() == 0) return null;
      int iselbodyrow = getBillListPanel().getBodyTable().getSelectedRow();
      if (iselbodyrow < 0) iselbodyrow = 0;
      return (String)getBillListPanel().getBodyBillModel().getValueAt(iselbodyrow, "ccustomerid");
    }

    if (getBillCardPanel().getBillTable().getRowCount() == 0) return null;
    int iselbodyrow = getBillCardPanel().getBillTable().getSelectedRow();
    if (iselbodyrow < 0) iselbodyrow = 0;
    return (String)getBillCardPanel().getBodyValueAt(iselbodyrow, "ccustomerid");
  }

  private String getCCustomerid()
  {
    return getCCustomerid(!this.strShowState.equals("LIST"));
  }

  public SaleinvoiceBVO[] initFreeItemlist(SaleinvoiceBVO[] vos)
  {
    try
    {
      FreeVOParse freeVOParse = new FreeVOParse();
      freeVOParse.setFreeVO(vos, "vfree0", "vfree", "cinvbasdocid", "cinventoryid", false);

      return vos;
    } catch (Exception ex) {
      SCMEnv.out("自定义设置失败!");
    }return null;
  }

  public void initVars(String pkcorp)
  {
    String salecorp = null;

    if ((pkcorp == null) || (pkcorp.trim().length() <= 0))
      salecorp = getClientEnvironment().getCorporation().getPrimaryKey();
    else {
      salecorp = pkcorp;
    }

    String[] syParas = { "SA08", "SA15", "SO27", "SO22", "SO06", "BD501", "BD502", "BD503", "BD505", "BD302", "SO30", "SA02", "SA13", "SO34", "SO36", "SO41", "SO59" };

    String[] comnames = { "nc.bs.pub.para.SysInitDMO", "nc.bs.pub.para.SysInitDMO", "nc.bs.bd.def.DefBO", "nc.bs.bd.def.DefBO" };

    String[] funnames = { "queryBatchParaValues", "getParaBoolean", "queryDefVO", "queryDefVO" };

    ArrayList paramObjlist = new ArrayList();

    Object[] paramObjs = (Object[])null;

    paramObjs = new Object[] { salecorp, syParas };

    paramObjlist.add(paramObjs);

    paramObjs = new Object[] { "0001", "SA09" };
    paramObjlist.add(paramObjs);

    paramObjs = new Object[] { "供应链/ARAP单据头", salecorp };
    paramObjlist.add(paramObjs);

    paramObjs = new Object[] { "供应链/ARAP单据体", salecorp };
    paramObjlist.add(paramObjs);
    try
    {
      this.hsparas = SysInitBO_Client.queryBatchParaValues(salecorp, syParas);
      UFBoolean ud = SysInitBO_Client.getParaBoolean("0001", "SA09");

      if ((this.hsparas != null) && (ud != null)) {
        this.hsparas.put("SA09", ud.toString());
      }

    }
    catch (Exception e)
    {
      SCMEnv.out(e.getMessage());
    }
  }

  public boolean isLaborOrDiscount(String cinvbasdocid)
    throws BusinessRuntimeException
  {
	  try{
	        UFBoolean isDiscount = new UFBoolean(
	                ((Object[]) nc.ui.scm.pub.CacheTool.getCellValue(
	                        "bd_invbasdoc", "pk_invbasdoc", "discountflag",
	                        cinvbasdocid))[0].toString());
	        UFBoolean isLabor = new UFBoolean(((Object[]) nc.ui.scm.pub.CacheTool
	                .getCellValue("bd_invbasdoc", "pk_invbasdoc", "laborflag",
	                        cinvbasdocid))[0].toString());
	        if (isDiscount.booleanValue() || isLabor.booleanValue())
	            return true;
	        return false;
	    	}catch(Exception e){
	    		nc.vo.scm.pub.SCMEnv.out(e.getMessage());    		
	    		throw new BusinessRuntimeException(e.getMessage());
	    	}
  }

  private boolean isSameOrderCustomer(SaleinvoiceVO saleinvoice)
  {
    String sCustomerid = null;
    int i = 0; for (int iLen = saleinvoice.getItemVOs().length; i < iLen; i++) {
      if (saleinvoice.getItemVOs()[i].getCcustomerid() != null) {
        if (sCustomerid == null) {
          sCustomerid = saleinvoice.getItemVOs()[i].getCcustomerid();
        } else if (!saleinvoice.getItemVOs()[i].getCcustomerid().equals(sCustomerid)) {
          showErrorMessage(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-100068"));

          return false;
        }
      }

    }

    return true;
  }

  public boolean isStrikeBalance(SaleinvoiceVO saleinvoice, Hashtable hsArSub, boolean byProLine)
  {
    if (!isSameOrderCustomer(saleinvoice)) return false;

    if (byProLine) {
      this.summoneyByProductLine = new Hashtable();
      this.presummoneyByProductLine = new Hashtable();
      this.strikemoneyByProductLine = hsArSub;
      for (int i = 0; i < saleinvoice.getChildrenVO().length; i++) {
        SaleinvoiceBVO saleinvoicebody = (SaleinvoiceBVO)saleinvoice.getChildrenVO()[i];

        if (((saleinvoicebody.getBlargessflag() != null) && (saleinvoicebody.getBlargessflag().booleanValue())) || (isLaborOrDiscount(saleinvoicebody.getCinvbasdocid())) || (saleinvoicebody.getCprolineid() == null))
        {
          continue;
        }
        if (!this.summoneyByProductLine.containsKey(saleinvoicebody.getCprolineid()))
        {
          this.summoneyByProductLine.put(saleinvoicebody.getCprolineid(), saleinvoicebody.getNsubsummny());

          this.presummoneyByProductLine.put(saleinvoicebody.getCprolineid(), saleinvoicebody.getNsummny());
        }
        else {
          UFDouble subsummny = saleinvoicebody.getNsubsummny();
          UFDouble summny = saleinvoicebody.getNsummny();
          subsummny = subsummny.add((UFDouble)this.summoneyByProductLine.get(saleinvoicebody.getCprolineid()));

          summny = summny.add((UFDouble)this.presummoneyByProductLine.get(saleinvoicebody.getCprolineid()));

          this.summoneyByProductLine.put(saleinvoicebody.getCprolineid(), subsummny);

          this.presummoneyByProductLine.put(saleinvoicebody.getCprolineid(), summny);
        }
      }

      Enumeration esummoney = this.summoneyByProductLine.keys();
      while (esummoney.hasMoreElements()) {
        String key = (String)esummoney.nextElement();
        UFDouble money = ((UFDouble)this.summoneyByProductLine.get(key)).multiply(this.SO_22).div(new UFDouble(100)).sub((UFDouble)this.summoneyByProductLine.get(key)).add((UFDouble)this.presummoneyByProductLine.get(key));

        this.summoneyByProductLine.put(key, money);
      }

      Enumeration eStrikeBill = this.strikemoneyByProductLine.keys();
      while (eStrikeBill.hasMoreElements()) {
        String key = (String)eStrikeBill.nextElement();
        if (((UFDouble)this.strikemoneyByProductLine.get(key)).compareTo(this.summoneyByProductLine.get(key) == null ? new UFDouble(0) : (UFDouble)this.summoneyByProductLine.get(key)) <= 0)
          continue;
        showErrorMessage(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000091"));

        this.oldhsTotalBykey = new Hashtable();
        this.hsTotalBykey = new Hashtable();
        return false;
      }

      return true;
    }

    this.summoney = new UFDouble(0);
    this.presummoney = new UFDouble(0);
    this.strikemoney = new UFDouble(0);
    for (int i = 0; i < saleinvoice.getChildrenVO().length; i++) {
      SaleinvoiceBVO saleinvoicebody = (SaleinvoiceBVO)saleinvoice.getChildrenVO()[i];

      if (((saleinvoicebody.getBlargessflag() != null) && (saleinvoicebody.getBlargessflag().booleanValue())) || (isLaborOrDiscount(saleinvoicebody.getCinvbasdocid())))
        continue;
      this.summoney = this.summoney.add(saleinvoicebody.getNsubsummny());

      this.presummoney = this.presummoney.add(saleinvoicebody.getNoriginalcursummny());
    }

    this.summoney = this.summoney.multiply(this.SO_22).div(new UFDouble(100)).sub(this.summoney).add(this.presummoney);

    Enumeration eStirkeMoney = hsArSub.keys();
    while (eStirkeMoney.hasMoreElements()) {
      String key = (String)eStirkeMoney.nextElement();
      this.strikemoney = this.strikemoney.add((UFDouble)hsArSub.get(key));
    }

    if (this.strikemoney.compareTo(this.summoney) > 0) {
      showErrorMessage(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000091"));

      this.oldhsTotalBykey = new Hashtable();
      this.hsTotalBykey = new Hashtable();
      return false;
    }
    return true;
  }

  public boolean isStrikeOverflow(Hashtable hstotal, Hashtable hsquery)
  {
    Enumeration eKeys = hstotal.keys();
    while (eKeys.hasMoreElements()) {
      String key = (String)eKeys.nextElement();
      UFDouble dTotal = (UFDouble)hstotal.get(key);
      UFDouble dQuery = (UFDouble)hsquery.get(key);
      if ((dTotal == null) || (dQuery == null)) {
        continue;
      }
      if (dTotal.compareTo(dQuery) > 0) {
        showErrorMessage(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000092"));

        return false;
      }
    }

    return true;
  }

  public void loadCardData(SaleinvoiceVO saleinvoice)
  {
    long s = System.currentTimeMillis();

    String currencyid = ((SaleinvoiceBVO)saleinvoice.getChildrenVO()[0]).getCcurrencytypeid();

    setPanelByCurrency(currencyid);

    UFDouble exchangeotobrate = ((SaleinvoiceBVO)saleinvoice.getChildrenVO()[0]).getNexchangeotobrate();

    UFDouble exchangeotoarate = ((SaleinvoiceBVO)saleinvoice.getChildrenVO()[0]).getNexchangeotoarate();

    UFBoolean freef = ((SaleVO)saleinvoice.getParentVO()).getBfreecustflag();

    ((SaleVO)saleinvoice.getParentVO()).setCcurrencyid(currencyid);
    getBillCardPanel().setBillValueVO(saleinvoice);
    long s1 = System.currentTimeMillis();
    getBillCardPanel().getBillModel().execLoadFormula();

    System.out.println("执行公式[共用时" + (System.currentTimeMillis() - s1) + "]");

    if (exchangeotobrate != null) {
      getBillCardPanel().setHeadItem("nexchangeotobrate", exchangeotobrate.toString());
    }

    if (exchangeotoarate != null) {
      getBillCardPanel().setHeadItem("nexchangeotoarate", exchangeotoarate.toString());
    }

    this.iStatus = ((SaleVO)saleinvoice.getParentVO()).getFstatus().intValue();

    String custId = ((SaleVO)saleinvoice.getParentVO()).getCreceiptcorpid();

    ((UIRefPane)getBillCardPanel().getHeadItem("ccustomername").getComponent()).setPK(custId);

    String[][] results = (String[][])null;
    try {
      results = SaleinvoiceBO_Client.getCustomerInfo(custId);
      if ((results != null) && (results.length != 0)) {
        UIRefPane bankref = (UIRefPane)getBillCardPanel().getHeadItem("ccustomerbank").getComponent();

        ((CustBankRefModel)bankref.getRefModel()).setCondition(results[0][5]);

        String bankId = ((SaleVO)saleinvoice.getParentVO()).getCcustbankid();

        bankref.setPK(bankId);
        String bankNo = bankref.getRefCode();
        getBillCardPanel().setHeadItem("ccustomerbankNo", bankNo);
        getBillCardPanel().setHeadItem("ccustomertel", results[0][0]);
        getBillCardPanel().setHeadItem("ccustomertaxNo", results[0][1]);
        getBillCardPanel().setHeadItem("vreceiveaddress", results[0][7]);
      }
      else {
        getBillCardPanel().setHeadItem("ccustomertel", "");
        getBillCardPanel().setHeadItem("ccustomertaxNo", "");
      }
    } catch (Exception e1) {
      showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000256"));

      e1.printStackTrace(System.out);
    }

    int iStatus = -1;
    if ((getBillCardPanel().getHeadItem("fstatus").getValue() != null) && (!getBillCardPanel().getHeadItem("fstatus").getValue().equals("")))
    {
      iStatus = Integer.parseInt(getBillCardPanel().getHeadItem("fstatus").getValue());
    }
    for (int kk = 0; kk < getBillCardPanel().getRowCount(); kk++) {
      if (iStatus == 1)
        setAssistChange(kk);
      if ((getBillCardPanel().getBodyValueAt(kk, "cpackunitid") == null) || (getBillCardPanel().getBodyValueAt(kk, "cpackunitid").equals("")))
      {
        continue;
      }
      beforeUnitChange(kk);
      afterUnitChange(kk);

      String[] appendFormulaViaPrice = { "norgviaprice->noriginalcurprice*scalefactor", "norgviapricetax->noriginalcurtaxprice*scalefactor" };

      getBillCardPanel().execBodyFormulas(kk, appendFormulaViaPrice);
    }

    initFreeItem();

    ((UIRefPane)getBillCardPanel().getHeadItem("cbiztypename").getComponent()).setPK(saleinvoice.getHeadVO().getCbiztype());

    getBillCardPanel().updateValue();
    getBillCardPanel().getBillModel().reCalcurateAll();

    for (int i = 0; i < getBillCardPanel().getBillModel().getRowCount(); i++) {
      UFDouble dSubsummny = (UFDouble)getBillCardPanel().getBodyValueAt(i, "nsubsummny");

      UFDouble dNoriginalcursummny = (UFDouble)getBillCardPanel().getBodyValueAt(i, "noriginalcursummny");

      UFDouble nuniteinvoicemny = (dSubsummny == null ? new UFDouble(0) : dSubsummny).sub(dNoriginalcursummny == null ? new UFDouble(0) : dNoriginalcursummny);

      getBillCardPanel().setBodyValueAt(nuniteinvoicemny, i, "nuniteinvoicemny");
    }

    SCMEnv.out("数据加载成功！[共用时" + (System.currentTimeMillis() - s) + "豪秒]");

    showHintMessage(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000065"));

    setState(iStatus);
  }

  public void mouse_doubleclick(BillMouseEnent e)
  {
    if (e.getPos() == 0)
      onCard();
  }

  public void onUnite(ButtonObject bo)
  {
    if (getBillListPanel().isShowing()) {
      autoUnit();
      return;
    }

    QueryConditionClient dlgStrikeQuery = getStrikeQueryDlg();
    if (dlgStrikeQuery.showModal() == 2)
      return;
    if (dlgStrikeQuery.isCloseOK()) {
      ConditionVO[] voCondition = dlgStrikeQuery.getConditionVO();

      for (int i = 0; i < voCondition.length; i++) {
        if (voCondition[i].getFieldCode().equals("pk_corp")) {
          String pk_corp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();

          if (!pk_corp.equals(voCondition[i].getValue())) {
            showErrorMessage(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000093"));

            return;
          }

        }

        if (voCondition[i].getFieldCode().equals("ccurrencytypeid")) {
          String ccurrencytypeid = getBillCardPanel().getHeadItem("ccurrencyid").getValue();

          if (!ccurrencytypeid.equals(voCondition[i].getValue())) {
            showErrorMessage(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000095"));

            return;
          }
        }
      }

      ConditionVO[] newvoCondition = new ConditionVO[voCondition.length - 1];

      String value = null;
      int i = 0; for (int j = 0; i < voCondition.length; i++) {
        if (voCondition[i].getFieldCode().equals("csourcebilltype"))
          value = voCondition[i].getValue();
        else
          newvoCondition[(j++)] = voCondition[i];
      }
      String sqlplus = " and ( csourcebilltype ";
      if (value.trim().equals("0")) {
        sqlplus = sqlplus + " in ('3H','3G') or csourcebilltype ='" + "#&" + "' ) ";
      }
      else if (value.trim().equals("3"))
        sqlplus = sqlplus + " =''" + "#&" + "' ) ";
      else if (value.trim().equals("1"))
        sqlplus = sqlplus + " ='3H' ) ";
      else
        sqlplus = sqlplus + " ='3G' ) ";
      this.m_strWhere = (dlgStrikeQuery.getWhereSQL(newvoCondition) + sqlplus);
    }

    String ccustomerid = getCCustomerid(true);
    this.m_strWhere = (this.m_strWhere + " and (so_arsub.ccustomerid = '" + ccustomerid + "')");

    String cbiztypeid = getBillCardPanel().getHeadItem("cbiztype").getValue();
    if (cbiztypeid == null)
      cbiztypeid = getBillCardPanel().getBusiType();
    if (this.SO36.booleanValue()) {
      this.m_strWhere = (this.m_strWhere + " and (so_arsubacct.cbiztypeid='" + cbiztypeid + "') ");
    }
    else {
      this.m_strWhere = (this.m_strWhere + " and (so_arsubacct.cbiztypeid='" + cbiztypeid + "' or so_arsubacct.cbiztypeid ='" + "#123456789*123456789" + "') ");
    }

    ArSubDLG dlg = new ArSubDLG(this, this.m_strWhere, dlgStrikeQuery, getBillCardPanel().getBusiType(), this.SO36.booleanValue(), this.digit);

    dlg.setCcustomerid(ccustomerid);
    dlg.showModal();

    ARSubUniteVO[] retVOs = dlg.getSeletedVOs();

    this.hsQueryArsubDataBykey = dlg.getQueryArsubData();

    if ((retVOs == null) || (retVOs.length == 0))
      return;
    Hashtable hsArsub = new Hashtable();
    for (int i = 0; i < retVOs.length; i++) {
      String key = retVOs[i].getCproducelineid() == null ? new Integer(i).toString() : retVOs[i].getCproducelineid();

      if (hsArsub.containsKey(key)) {
        hsArsub.put(key, ((UFDouble)hsArsub.get(key)).add(retVOs[i].getNsubmny()));
      }
      else {
        hsArsub.put(key, retVOs[i].getNsubmny());
      }
      String arsubacctkey = retVOs[i].getPrimaryKey();
      if (this.hsTotalBykey.containsKey(arsubacctkey)) {
        this.hsTotalBykey.put(arsubacctkey, ((UFDouble)this.hsTotalBykey.get(arsubacctkey)).add(retVOs[i].getNsubmny()));
      }
      else
        this.hsTotalBykey.put(arsubacctkey, retVOs[i].getNsubmny());
    }
    if (!isStrikeOverflow(this.hsTotalBykey, this.hsQueryArsubDataBykey)) {
      this.hsTotalBykey = ((Hashtable)this.oldhsTotalBykey.clone());
      return;
    }
    this.oldhsTotalBykey = ((Hashtable)this.hsTotalBykey.clone());

    SaleinvoiceVO saleinvoice = (SaleinvoiceVO)getBillCardPanel().getBillValueVO("nc.vo.so.so002.SaleinvoiceVO", "nc.vo.so.so002.SaleVO", "nc.vo.so.so002.SaleinvoiceBVO");

    if (isStrikeBalance(saleinvoice, hsArsub, this.SO_27.booleanValue())) {
      uniteInvoiceProcess(saleinvoice, hsArsub, this.SO_27.booleanValue());
      for (int j = 0; j < retVOs.length; j++) {
        String key = retVOs[j].getPrimaryKey();
        if (this.hsSelectedARSubHVO.containsKey(key)) {
          this.hsSelectedARSubHVO.put(key, ((UFDouble)this.hsSelectedARSubHVO.get(key)).add(retVOs[j].getNsubmny()));
        }
        else
          this.hsSelectedARSubHVO.put(key, retVOs[j].getNsubmny());
      }
    }
    showHintMessage(NCLangRes.getInstance().getStrByID("40060501", "UPT40060501-000048"));
  }

  public void onUniteCancel(ButtonObject bo)
  {
    if (getBillListPanel().isShowing()) {
      cancelUnit();
      return;
    }

    if (!this.bstrikeflag) {
      showErrorMessage(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000096"));

      setButtonsState();
      return;
    }
    int result = showOkCancelMessage(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000097"));

    if (result != 1)
      return;
    String[] strikeKeys = { "nquoteprice", "nquotetaxprice", "nquotenetprice", "nquotetaxnetprice", "noriginalcursummny" };

    String[] prestrikeKeys = { "nsubquoteprice", "nsubquotetaxprice", "nsubquotenetprice", "nsubquotetaxnetprice", "nsubsummny" };

    for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
      for (int j = 0; j < strikeKeys.length; j++) {
        getBillCardPanel().setBodyValueAt(getBillCardPanel().getBodyValueAt(i, prestrikeKeys[j]), i, strikeKeys[j]);

        calculateNumber(i, strikeKeys[j]);
      }
      if ((getBillCardPanel().getBillModel().getRowState(i) != 1) && (getBillCardPanel().getBillModel().getRowState(i) != 3)) {
        getBillCardPanel().getBillModel().setRowState(i, 2);
      }
    }

    getBillCardPanel().setHeadItem("nstrikemny", new UFDouble(0));
    getBillCardPanel().execHeadFormula("nnetmny->ntotalsummny-nstrikemny");

    countCardUniteMny();

    this.bstrikeflag = false;
    setButtonsState();

    getBillCardPanel().setEnabled(true);

    Enumeration e = this.hsSelectedARSubHVO.keys();
    while (e.hasMoreElements()) {
      String key = (String)e.nextElement();
      this.hsSelectedARSubHVO.put(key, new UFDouble(0));
      this.hsTotalBykey.put(key, new UFDouble(0));
      this.oldhsTotalBykey.put(key, new UFDouble(0));
    }
    showHintMessage(NCLangRes.getInstance().getStrByID("40060501", "UPT40060501-000050"));
  }

  public void reLoadTS()
  {
    try
    {
      if (getBillListPanel().isShowing())
        this.id = getBillListPanel().getHeadTable().getSelectedRow();
      int temp = this.id;

      String sBillID = getID(temp);

      String strWhere = "so_saleinvoice.csaleid = '" + sBillID + "'";

      if (sBillID != null)
      {
        if (this.strShowState.equals("LIST")) {
          SaleVO[] salehvo = SaleinvoiceBO_Client.queryHeadAllData(strWhere);

          if ((salehvo != null) && (salehvo.length > 0)) {
            Integer value = salehvo[0].getFstatus();

            getBillListPanel().getHeadBillModel().setValueAt(salehvo[0].getTs(), temp, "ts");

            getBillListPanel().getHeadBillModel().setValueAt(value, temp, "fstatus");

            if (salehvo[0].getFstatus().intValue() == 2) {
              getBillListPanel().getHeadBillModel().setValueAt(getClientEnvironment().getUser().getPrimaryKey().toString(), temp, "capproveid");

              getBillListPanel().getHeadBillModel().setValueAt(getClientEnvironment().getUser().getUserName().toString(), temp, "capprovename");

              getBillListPanel().getHeadBillModel().setValueAt(getClientEnvironment().getDate().toString(), temp, "dapprovedate");

              getBillListPanel().getHeadBillModel().setValueAt(salehvo[0].getDaudittime(), temp, "daudittime");
            }
            else
            {
              getBillListPanel().getHeadBillModel().setValueAt(null, temp, "capproveid");

              getBillListPanel().getHeadBillModel().setValueAt(null, temp, "capprovename");

              getBillListPanel().getHeadBillModel().setValueAt(null, temp, "dapprovedate");

              getBillListPanel().getHeadBillModel().setValueAt(null, temp, "daudittime");
            }

            if ((getBillListPanel().getBodyBillModel() != null) && 
              (getBillListPanel().getBodyTable().getRowCount() > 0)) {
              for (int i = 0; i < getBillListPanel().getBodyBillModel().getRowCount(); )
              {
                getBillListPanel().getBodyBillModel().setValueAt(value, i, "frowstatus");

                i++;
              }
            }
          }

        }
        else
        {
          String[] formula = { "ts->getColValue(so_saleinvoice,ts,csaleid,csaleid)", "dmoditime->getColValue(so_saleinvoice,dmoditime,csaleid,csaleid)", "daudittime->getColValue(so_saleinvoice,daudittime,csaleid,csaleid)", "dbilltime->getColValue(so_saleinvoice,dbilltime,csaleid,csaleid)", "dapprovedate->getColValue(so_saleinvoice,dapprovedate,csaleid,csaleid)" };

          getBillCardPanel().execHeadFormulas(formula);
        }

      }

      updateUI();
    } catch (Exception e) {
      SCMEnv.out("重新加载表头TS失败.");
      SCMEnv.out(e.getMessage());
    }
  }

  public void uniteInvoiceProcess(SaleinvoiceVO saleinvoice, Hashtable hsArSub, boolean byProLine)
  {
    int carddigit = getBillCardPanel().getBodyItem("noriginalcursummny").getDecimalDigits();

    if (byProLine) {
      Enumeration eStrikeBill = this.strikemoneyByProductLine.keys();
      UFDouble totalstrikemny = new UFDouble(0);
      while (eStrikeBill.hasMoreElements()) {
        String key = (String)eStrikeBill.nextElement();
        UFDouble strikeMoney = (UFDouble)this.strikemoneyByProductLine.get(key);
        totalstrikemny = totalstrikemny.add(strikeMoney);
        UFDouble remainMoney = strikeMoney;
        int count = 0;
        for (int i = 0; i < saleinvoice.getChildrenVO().length; i++) {
          SaleinvoiceBVO saleinvoicebody = (SaleinvoiceBVO)saleinvoice.getChildrenVO()[i];

          if (((saleinvoicebody.getBlargessflag() == null) || (!saleinvoicebody.getBlargessflag().booleanValue())) && (!isLaborOrDiscount(saleinvoicebody.getCinvbasdocid())) && (saleinvoicebody.getCprolineid() != null))
            count++;
        }
        int i = 0; for (int j = 1; i < saleinvoice.getChildrenVO().length; i++) {
          SaleinvoiceBVO saleinvoicebody = (SaleinvoiceBVO)saleinvoice.getChildrenVO()[i];

          if (((saleinvoicebody.getBlargessflag() == null) || (!saleinvoicebody.getBlargessflag().booleanValue())) && (!isLaborOrDiscount(saleinvoicebody.getCinvbasdocid())) && (saleinvoicebody.getCprolineid() != null) && (saleinvoicebody.getCprolineid().equals(key)))
          {
            if (j < count) {
              UFDouble money = saleinvoicebody.getNoriginalcursummny();
              UFDouble changemoney = money.multiply(strikeMoney).div((UFDouble)this.presummoneyByProductLine.get(key));
              changemoney = changemoney.setScale(0 - carddigit, 4);
              money = money.sub(changemoney);

              getBillCardPanel().setBodyValueAt((getBillCardPanel().getBodyValueAt(i, "nuniteinvoicemny") == null ? new UFDouble(0.0D) : (UFDouble)getBillCardPanel().getBodyValueAt(i, "nuniteinvoicemny")).add(changemoney), i, "nuniteinvoicemny");

              getBillCardPanel().setBodyValueAt(money, i, "noriginalcursummny");

              if ((getBillCardPanel().getBillModel().getRowState(i) != 1) && (getBillCardPanel().getBillModel().getRowState(i) != 3)) {
                getBillCardPanel().getBillModel().setRowState(i, 2);
              }

              remainMoney = remainMoney.sub(changemoney);
            }
            else
            {
              UFDouble money = saleinvoicebody.getNoriginalcursummny();
              remainMoney = remainMoney.setScale(0 - carddigit, 4);
              money = money.sub(remainMoney);

              getBillCardPanel().setBodyValueAt((getBillCardPanel().getBodyValueAt(i, "nuniteinvoicemny") == null ? new UFDouble(0.0D) : (UFDouble)getBillCardPanel().getBodyValueAt(i, "nuniteinvoicemny")).add(remainMoney), i, "nuniteinvoicemny");

              getBillCardPanel().setBodyValueAt(money, i, "noriginalcursummny");
              if ((getBillCardPanel().getBillModel().getRowState(i) != 1) && (getBillCardPanel().getBillModel().getRowState(i) != 3)) {
                getBillCardPanel().getBillModel().setRowState(i, 2);
              }
            }
            j++;
          }
          calculateNumber(i, "noriginalcursummny");
        }
      }

      getBillCardPanel().setHeadItem("nstrikemny", totalstrikemny.add(new UFDouble(getBillCardPanel().getHeadItem("nstrikemny").getValue())));
      getBillCardPanel().execHeadFormula("nnetmny->ntotalsummny-nstrikemny");
    }
    else
    {
      UFDouble remainMoney = this.strikemoney;
      int count = 0;
      for (int i = 0; i < saleinvoice.getChildrenVO().length; i++) {
        SaleinvoiceBVO saleinvoicebody = (SaleinvoiceBVO)saleinvoice.getChildrenVO()[i];
        if (((saleinvoicebody.getBlargessflag() != null) && (saleinvoicebody.getBlargessflag().booleanValue())) || (isLaborOrDiscount(saleinvoicebody.getCinvbasdocid())))
          continue;
        count++;
      }
      int i = 0; for (int j = 1; i < saleinvoice.getChildrenVO().length; i++) {
        SaleinvoiceBVO saleinvoicebody = (SaleinvoiceBVO)saleinvoice.getChildrenVO()[i];
        if (((saleinvoicebody.getBlargessflag() == null) || (!saleinvoicebody.getBlargessflag().booleanValue())) && (!isLaborOrDiscount(saleinvoicebody.getCinvbasdocid()))) {
          if (j == count) {
            UFDouble money = saleinvoicebody.getNoriginalcursummny();
            remainMoney = remainMoney.setScale(0 - carddigit, 4);
            money = money.sub(remainMoney);

            getBillCardPanel().setBodyValueAt((getBillCardPanel().getBodyValueAt(i, "nuniteinvoicemny") == null ? new UFDouble(0.0D) : (UFDouble)getBillCardPanel().getBodyValueAt(i, "nuniteinvoicemny")).add(remainMoney), i, "nuniteinvoicemny");

            getBillCardPanel().setBodyValueAt(money, i, "noriginalcursummny");
            if ((getBillCardPanel().getBillModel().getRowState(i) != 1) && (getBillCardPanel().getBillModel().getRowState(i) != 3))
              getBillCardPanel().getBillModel().setRowState(i, 2);
          }
          else {
            UFDouble money = saleinvoicebody.getNoriginalcursummny();
            UFDouble changemoney = money.multiply(this.strikemoney).div(this.presummoney);
            changemoney = changemoney.setScale(0 - carddigit, 4);
            money = money.sub(changemoney);

            getBillCardPanel().setBodyValueAt((getBillCardPanel().getBodyValueAt(i, "nuniteinvoicemny") == null ? new UFDouble(0.0D) : (UFDouble)getBillCardPanel().getBodyValueAt(i, "nuniteinvoicemny")).add(changemoney), i, "nuniteinvoicemny");

            getBillCardPanel().setBodyValueAt(money, i, "noriginalcursummny");
            if ((getBillCardPanel().getBillModel().getRowState(i) != 1) && (getBillCardPanel().getBillModel().getRowState(i) != 3)) {
              getBillCardPanel().getBillModel().setRowState(i, 2);
            }

            remainMoney = remainMoney.sub(changemoney);
          }
          j++;
          calculateNumber(i, "noriginalcursummny");
        }
      }

      getBillCardPanel().setHeadItem("nstrikemny", this.strikemoney.add(new UFDouble(getBillCardPanel().getHeadItem("nstrikemny").getValue())));
      getBillCardPanel().execHeadFormula("nnetmny->ntotalsummny-nstrikemny");
    }
    this.bstrikeflag = true;

    setButtonsState();

    getBillCardPanel().setEnabled(false);
    for (int i = 0; i < getBillCardPanel().getBillModel().getRowCount(); i++) {
      getBillCardPanel().getBillModel().setCellEditable(i, "nuniteinvoicemny", true);
    }
    setButtonsState();
  }

  protected void updateCacheVO()
  {
    if (this.strShowState.equals("LIST")) {
      int id = getBillListPanel().getHeadTable().getSelectedRow();
      if (id < 0)
        return;
      SaleinvoiceVO saleinvoice = new SaleinvoiceVO();

      SaleVO hvo = (SaleVO)getBillListPanel().getHeadBillModel().getBodyValueRowVO(id, SaleVO.class.getName());

      SaleinvoiceBVO[] bvos = (SaleinvoiceBVO[])getBillListPanel().getBodyBillModel().getBodyValueVOs(SaleinvoiceBVO.class.getName());
      saleinvoice.setParentVO(hvo);
      saleinvoice.setChildrenVO(bvos);

      this.vocache.setSaleinvoiceVO(((SaleVO)saleinvoice.getParentVO()).getCsaleid(), saleinvoice);

      getBillListPanel().getHeadBillModel().setBodyRowVO(saleinvoice.getParentVO(), id);
    }
    else {
      SaleinvoiceVO saleinvoice = (SaleinvoiceVO)getBillCardPanel().getBillValueVO(SaleinvoiceVO.class.getName(), SaleVO.class.getName(), SaleinvoiceBVO.class.getName());

      SaleVO hvo = new SaleVO();
      hvo = (SaleVO)saleinvoice.getParentVO();
      UIRefPane ufref = (UIRefPane)getBillCardPanel().getHeadItem("ccalbodyid").getComponent();

      hvo.setAttributeValue("ccalbodyname", ufref.getUITextField().getText());

      ufref = (UIRefPane)getBillCardPanel().getHeadItem("cdeptid").getComponent();

      hvo.setAttributeValue("cdeptname", ufref.getUITextField().getText());

      ufref = (UIRefPane)getBillCardPanel().getHeadItem("cemployeeid").getComponent();

      hvo.setAttributeValue("cemployeename", ufref.getUITextField().getText());

      ufref = (UIRefPane)getBillCardPanel().getHeadItem("cfreecustid").getComponent();

      hvo.setAttributeValue("cfreecustname", ufref.getUITextField().getText());

      ufref = (UIRefPane)getBillCardPanel().getHeadItem("creceiptcorpid").getComponent();

      hvo.setAttributeValue("creceiptcorpname", ufref.getUITextField().getText());

      ufref = (UIRefPane)getBillCardPanel().getHeadItem("creceiptcustomerid").getComponent();

      hvo.setAttributeValue("creceiptcustomername", ufref.getUITextField().getText());

      ufref = (UIRefPane)getBillCardPanel().getHeadItem("csalecorpid").getComponent();

      hvo.setAttributeValue("csalecorpname", ufref.getUITextField().getText());

      ufref = (UIRefPane)getBillCardPanel().getHeadItem("ctermprotocolid").getComponent();

      hvo.setAttributeValue("ctermprotocolname", ufref.getUITextField().getText());

      ufref = (UIRefPane)getBillCardPanel().getHeadItem("ctransmodeid").getComponent();

      hvo.setAttributeValue("ctransmodename", ufref.getUITextField().getText());

      ufref = (UIRefPane)getBillCardPanel().getHeadItem("cwarehouseid").getComponent();

      hvo.setAttributeValue("cwarehousename", ufref.getUITextField().getText());

      ufref = (UIRefPane)getBillCardPanel().getHeadItem("vreceiveaddress").getComponent();

      hvo.setAttributeValue("vreceiveaddress", ufref.getUITextField().getText());

      ufref = (UIRefPane)getBillCardPanel().getTailItem("capproveid").getComponent();

      hvo.setAttributeValue("capprovename", ufref.getUITextField().getText());

      ufref = (UIRefPane)getBillCardPanel().getTailItem("coperatorid").getComponent();

      hvo.setAttributeValue("coperatorname", ufref.getUITextField().getText());

      hvo.setAttributeValue("fstatus", saleinvoice.getParentVO().getAttributeValue("fstatus"));

      saleinvoice.setParentVO(hvo);
      this.vocache.setSaleinvoiceVO(hvo.getCsaleid(), saleinvoice);
      if (this.id < getBillListPanel().getHeadBillModel().getRowCount()) {
        getBillListPanel().getHeadBillModel().setBodyRowVO(saleinvoice.getParentVO(), this.id);
      }
      try
      {
        nc.ui.so.so001.panel.bom.BillTools.setMnyToModelByCurrency(getBillListPanel().getHeadBillModel(), saleinvoice.getParentVO(), this.id, getCorpPrimaryKey(), "ccurrencytypeid", new String[] { "ntotalsummny", "nstrikemny", "nnetmny" });
      }
      catch (Exception e)
      {
        handleException(e);
      }
    }
  }

  public void afterNuniteinvoicemnyEdit(BillEditEvent e)
  {
    UFDouble nuniteinvoicemnyNEW = getBillCardPanel().getBodyValueAt(e.getRow(), "nuniteinvoicemny") == null ? new UFDouble(0.0D) : (UFDouble)getBillCardPanel().getBodyValueAt(e.getRow(), "nuniteinvoicemny");

    if (nuniteinvoicemnyNEW.compareTo(new UFDouble(0.0D)) < 0) {
      showErrorMessage(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000098"));

      getBillCardPanel().setBodyValueAt(this.nUniteInvoiceMnyBeforeChange, e.getRow(), "nuniteinvoicemny");

      return;
    }
    UFDouble nsubsummny = getBillCardPanel().getBodyValueAt(e.getRow(), "nsubsummny") == null ? new UFDouble(0.0D) : (UFDouble)getBillCardPanel().getBodyValueAt(e.getRow(), "nsubsummny");

    UFDouble noriginalcursummny = getBillCardPanel().getBodyValueAt(e.getRow(), "noriginalcursummny") == null ? new UFDouble(0.0D) : (UFDouble)getBillCardPanel().getBodyValueAt(e.getRow(), "noriginalcursummny");

    UFDouble nchangemny = nuniteinvoicemnyNEW.sub(nsubsummny.sub(noriginalcursummny));

    UFDouble nchangemnybak = nchangemny;

    Hashtable hsFullData = null;

    Hashtable hsSelectedARSubHVObak = (Hashtable)this.hsSelectedARSubHVO.clone();

    Enumeration efilterkeys = hsSelectedARSubHVObak.keys();
    while (efilterkeys.hasMoreElements()) {
      String cfilterkeys = (String)efilterkeys.nextElement();
      if ((hsSelectedARSubHVObak.get(cfilterkeys) != null) && (((UFDouble)hsSelectedARSubHVObak.get(cfilterkeys)).compareTo(new UFDouble(0.0D)) != 0))
        continue;
      hsSelectedARSubHVObak.remove(cfilterkeys);
    }
    try
    {
      hsFullData = SaleinvoiceBO_Client.fillDataWithARSubAcct(hsSelectedARSubHVObak);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    if (hsFullData == null) {
      showErrorMessage(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000099"));

      return;
    }

    if (nchangemny.compareTo(new UFDouble(0.0D)) > 0)
    {
      if (this.SO_27.booleanValue()) {
        String cchangeAtProlineid = (String)getBillCardPanel().getBodyValueAt(e.getRow(), "cprolineid");

        Enumeration ekeys = hsFullData.keys();
        while (ekeys.hasMoreElements()) {
          String fulldataid = (String)ekeys.nextElement();
          String cprolineid = (String)((ArrayList)hsFullData.get(fulldataid)).get(0);

          if ((cprolineid == null) || (!cprolineid.equals(cchangeAtProlineid)))
            continue;
          UFDouble narsubmny = (UFDouble)((ArrayList)hsFullData.get(fulldataid)).get(1);

          UFDouble narsubinvmny = (UFDouble)((ArrayList)hsFullData.get(fulldataid)).get(2);

          if (narsubmny.sub(narsubinvmny).compareTo(nchangemny) < 0) {
            ((ArrayList)hsFullData.get(fulldataid)).set(2, narsubmny);

            nchangemny = nchangemny.sub(narsubmny.sub(narsubinvmny));
          }
          else {
            ((ArrayList)hsFullData.get(fulldataid)).set(2, narsubinvmny.add(nchangemny));

            nchangemny = new UFDouble(0.0D);
          }
        }
      }
      else {
        Enumeration ekeys = hsFullData.keys();
        while (ekeys.hasMoreElements()) {
          String fulldataid = (String)ekeys.nextElement();
          UFDouble narsubmny = (UFDouble)((ArrayList)hsFullData.get(fulldataid)).get(1);

          UFDouble narsubinvmny = (UFDouble)((ArrayList)hsFullData.get(fulldataid)).get(2);

          if (narsubmny.sub(narsubinvmny).compareTo(nchangemny) < 0) {
            ((ArrayList)hsFullData.get(fulldataid)).set(2, narsubmny);

            nchangemny = nchangemny.sub(narsubmny.sub(narsubinvmny));
          }
          else {
            ((ArrayList)hsFullData.get(fulldataid)).set(2, narsubinvmny.add(nchangemny));

            nchangemny = new UFDouble(0.0D);
          }
        }
      }
      if (nchangemny.compareTo(new UFDouble(0.0D)) > 0) {
        showErrorMessage(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000100"));

        getBillCardPanel().setBodyValueAt(this.nUniteInvoiceMnyBeforeChange, e.getRow(), "nuniteinvoicemny");

        return;
      }
    }
    else
    {
      nchangemny = new UFDouble(0.0D).sub(nchangemny);
      if (this.SO_27.booleanValue()) {
        String cchangeAtProlineid = (String)getBillCardPanel().getBodyValueAt(e.getRow(), "cprolineid");

        Enumeration ekeys = hsFullData.keys();
        while (ekeys.hasMoreElements()) {
          String fulldataid = (String)ekeys.nextElement();
          String cprolineid = (String)((ArrayList)hsFullData.get(fulldataid)).get(0);

          if ((cprolineid == null) || (!cprolineid.equals(cchangeAtProlineid)))
            continue;
          UFDouble narsubinvmny = (UFDouble)((ArrayList)hsFullData.get(fulldataid)).get(2);

          if (narsubinvmny.compareTo(nchangemny) < 0) {
            ((ArrayList)hsFullData.get(fulldataid)).set(2, new UFDouble(0.0D));

            nchangemny = nchangemny.sub(narsubinvmny);
          } else {
            ((ArrayList)hsFullData.get(fulldataid)).set(2, narsubinvmny.sub(nchangemny));

            break;
          }
        }
      }
      else {
        Enumeration ekeys = hsFullData.keys();
        while (ekeys.hasMoreElements()) {
          String fulldataid = (String)ekeys.nextElement();
          UFDouble narsubinvmny = (UFDouble)((ArrayList)hsFullData.get(fulldataid)).get(2);

          if (narsubinvmny.compareTo(nchangemny) < 0) {
            ((ArrayList)hsFullData.get(fulldataid)).set(2, new UFDouble(0.0D));

            nchangemny = nchangemny.sub(narsubinvmny);
          } else {
            ((ArrayList)hsFullData.get(fulldataid)).set(2, narsubinvmny.sub(nchangemny));

            break;
          }
        }
      }

    }

    Hashtable hsArsub = new Hashtable();
    Enumeration eacctkeys = hsFullData.keys();
    int icount = 0;
    while (eacctkeys.hasMoreElements()) {
      String cacctid = (String)eacctkeys.nextElement();

      UFDouble ninvmny = (UFDouble)((ArrayList)hsFullData.get(cacctid)).get(2);

      ArrayList aryData = (ArrayList)hsFullData.get(cacctid);
      String cprolineid = aryData.get(0) == null ? new Integer(icount).toString() : aryData.get(0).toString();

      hsArsub.put(cprolineid, ninvmny);
      icount++;
    }

    SaleinvoiceVO saleinvoice = (SaleinvoiceVO)getBillCardPanel().getBillValueVO("nc.vo.so.so002.SaleinvoiceVO", "nc.vo.so.so002.SaleVO", "nc.vo.so.so002.SaleinvoiceBVO");

    if (isStrikeBalance(saleinvoice, hsArsub, this.SO_27.booleanValue()))
    {
      UFDouble money = (UFDouble)getBillCardPanel().getBodyValueAt(e.getRow(), "noriginalcursummny");

      money = money.sub(nchangemnybak);

      getBillCardPanel().setBodyValueAt(money, e.getRow(), "noriginalcursummny");

      if (this.strState.equals("ADD")) {
        getBillCardPanel().getBillModel().setRowState(e.getRow(), 1);
      }
      else {
        getBillCardPanel().getBillModel().setRowState(e.getRow(), 2);
      }
      calculateNumber(e.getRow(), "noriginalcursummny");
      getBillCardPanel().setHeadItem("nstrikemny", nchangemnybak.add(new UFDouble(getBillCardPanel().getHeadItem("nstrikemny").getValue())));

      getBillCardPanel().execHeadFormula("nnetmny->ntotalsummny-nstrikemny");

      this.bstrikeflag = true;
      setButtonsState();
      for (int i = 0; i < getBillCardPanel().getBillModel().getRowCount(); i++) {
        getBillCardPanel().getBillModel().setCellEditable(i, "nuniteinvoicemny", true);
      }

      setButtonsState();

      Enumeration eselectedkeys = hsFullData.keys();
      while (eselectedkeys.hasMoreElements()) {
        String carsubacctid = (String)eselectedkeys.nextElement();
        UFDouble narsubinvmny = (UFDouble)((ArrayList)hsFullData.get(carsubacctid)).get(2);

        this.hsSelectedARSubHVO.put(carsubacctid, narsubinvmny);

        this.hsTotalBykey.put(carsubacctid, narsubinvmny);
      }
    }
  }

  public void onOpposeAct()
  {
    SaleinvoiceVO oldvo = null;
    if (this.strShowState.equals("LIST")) {
      if (getBillListPanel().getHeadBillModel().getRowCount() == 0)
        return;
      int iSelect = getBillListPanel().getHeadTable().getSelectedRow();

      oldvo = (SaleinvoiceVO)getBillListPanel().getBillValueVO(iSelect, SaleinvoiceVO.class.getName(), SaleVO.class.getName(), SaleinvoiceBVO.class.getName());
    }
    else
    {
      oldvo = (SaleinvoiceVO)getBillCardPanel().getBillValueVO(SaleinvoiceVO.class.getName(), SaleVO.class.getName(), SaleinvoiceBVO.class.getName());
    }

    if (oldvo == null)
      return;
    SaleVO head = (SaleVO)oldvo.getParentVO();
    if ((head != null) && ((oldvo.getChildrenVO() == null) || (oldvo.getChildrenVO().length == 0))) {
      SaleinvoiceBVO[] bodys = (SaleinvoiceBVO[])null;
      try {
        bodys = SaleinvoiceBO_Client.queryBodyData(head.getCsaleid());
        oldvo.setChildrenVO(bodys);
      } catch (Exception e) {
        SCMEnv.out(e.getMessage());
      }
    }

    try
    {
      SaleVO salehvo = SaleinvoiceBO_Client.queryHeadData(head.getCsaleid());
      if ((salehvo != null) && 
        (head.getTs() != null) && (!head.getTs().equals(salehvo.getTs()))) {
        showErrorMessage(NCLangRes.getInstance().getStrByID("sopub", "UPPsopub-000315"));

        return;
      }
    }
    catch (Exception e) {
      SCMEnv.out(e.getMessage());

      if ((head.getNstrikemny() != null) && (head.getNstrikemny().doubleValue() == 0.0D))
      {
        this.bstrikeflag = false;
      } else {
        this.bstrikeflag = true;
        showErrorMessage(NCLangRes.getInstance().getStrByID("sopub", "UPPsopub-000312"));

        return;
      }
    }
    if ((head.getFcounteractflag() != null) && (head.getFcounteractflag().intValue() != 0)) {
      showErrorMessage(NCLangRes.getInstance().getStrByID("sopub", "UPPsopub-000313"));

      return;
    }
    if ((head.getFstatus() != null) && (head.getFstatus().intValue() != 2)) {
      showErrorMessage(NCLangRes.getInstance().getStrByID("sopub", "UPPsopub-000314"));

      return;
    }

    SaleinvoiceVO newvo = convToOppVO(oldvo);

    if (this.strShowState.equals("LIST")) {
      remove(getBillListPanel());
      add(getBillCardPanel(), "Center");

      this.strShowState = "CARD";
      initButtons();
    }
    SaleinvoiceBVO[] bodys = (SaleinvoiceBVO[])newvo.getChildrenVO();

    SoVoTools.execFormulas(new String[] { "discountflag->getColValue(bd_invbasdoc,discountflag,pk_invbasdoc,cinvbasdocid)" }, bodys);

    newvo.setChildrenVO(bodys);
    loadCardData(newvo);
    getBillCardPanel().execHeadFormula("csalecompanyname->getColValue(bd_corp,unitname,pk_corp,pk_corp)");

    for (int i = 0; i < getBillCardPanel().getBillModel().getRowCount(); i++) {
      UFDouble dSubsummny = (UFDouble)getBillCardPanel().getBodyValueAt(i, "nsubsummny");

      UFDouble dNoriginalcursummny = (UFDouble)getBillCardPanel().getBodyValueAt(i, "noriginalcursummny");

      UFDouble nuniteinvoicemny = (dSubsummny == null ? new UFDouble(0) : dSubsummny).sub(dNoriginalcursummny == null ? new UFDouble(0) : dNoriginalcursummny);

      getBillCardPanel().setBodyValueAt(nuniteinvoicemny, i, "nuniteinvoicemny");

      Object orgBillCode = getBillCardPanel().getBodyValueAt(i, "coriginalbillcode");

      getBillCardPanel().setBodyValueAt(orgBillCode, i, "csourcebillcode");

      getBillCardPanel().getBillModel().setRowState(i, 2);
    }

    getBillCardPanel().setEnabled(true);

    this.strState = "OPP";
    setButtonsState();

    setOppCellEditable();

    updateUI();
  }

  private SaleinvoiceVO convToOppVO(SaleinvoiceVO oldvo)
  {
    SaleinvoiceVO lastvo = new SaleinvoiceVO();
    SaleVO headold = (SaleVO)oldvo.getParentVO();
    SaleVO headnew = (SaleVO)headold.clone();
    lastvo.setParentVO(headnew);
    SaleVO head = (SaleVO)lastvo.getParentVO();
    SaleinvoiceBVO[] itemsnew = new SaleinvoiceBVO[oldvo.getChildrenVO().length];

    String[] strFieldnames = { "nnumber", "npacknumber", "nbalancenumber", "noriginalcurtaxmny", "noriginalcurmny", "noriginalcursummny", "noriginalcurdiscountmny", "ntaxmny", "nmny", "nsummny", "ndiscountmny", "nsimulatecostmny", "ncostmny", "ntotalpaymny", "ntotalreceiptnumber", "ntotalinvoicenumber", "ntotalinventorynumber", "nassistcurdiscountmny", "nassistcursummny", "nassistcurmny", "nassistcurtaxmny", "nquotenumber", "nsubsummny", "nsubcursummny","sjtsje","ztsje" };//eric

    if (head.getNnetmny() != null)
      head.setNnetmny(head.getNnetmny().multiply(-1.0D));
    if (head.getNstrikemny() != null)
      head.setNstrikemny(head.getNstrikemny().multiply(-1.0D));
    if (head.getNtotalsummny() != null)
      head.setNtotalsummny(head.getNtotalsummny().multiply(-1.0D));
    head.setFcounteractflag(new Integer(2));
    head.setCsaleid(null);
    head.setVreceiptcode(null);
    head.setDbilldate(getClientEnvironment().getBusinessDate());
    head.setFstatus(new Integer(1));
    head.setCapproveid(null);
    head.setDapprovedate(null);

    SaleinvoiceBVO[] items = (SaleinvoiceBVO[])oldvo.getChildrenVO();
    for (int i = 0; i < items.length; i++) {
      itemsnew[i] = ((SaleinvoiceBVO)items[i].clone());
      for (int j = 0; j < strFieldnames.length; j++) {
        if (items[i].getAttributeValue(strFieldnames[j]) != null) {
          UFDouble ufd = (UFDouble)items[i].getAttributeValue(strFieldnames[j]);
          itemsnew[i].setAttributeValue(strFieldnames[j], ufd.multiply(-1.0D));
        }
      }
      itemsnew[i].setHttssl(0-items[i].getHttssl().intValue());//eric
      itemsnew[i].setCupreceipttype(headold.getCreceipttype());
      itemsnew[i].setCupsourcebillid(items[i].getCsaleid());
      itemsnew[i].setCupsourcebillbodyid(items[i].getPrimaryKey());
      itemsnew[i].setCupsourcebillcode(headold.getVreceiptcode());
      itemsnew[i].setTs(headold.getTs());

      itemsnew[i].setCsaleid(null);
      itemsnew[i].setCsale_bid(null);
    }

    lastvo.setChildrenVO(itemsnew);
    return lastvo;
  }

  public void setOppCellEditable()
  {
    Hashtable htcols = new Hashtable();
    htcols.put("vreceiptcode", "vreceiptcode");
    htcols.put("dbilldate", "dbilldate");
    htcols.put("vnote", "vnote");
    for (int i = 1; i <= 20; i++) {
      htcols.put("vdef" + i, "vdef" + i);
    }

    htcols.put("frownote", "frownote");

    BillItem[] headits = getBillCardPanel().getHeadItems();
    for (int i = 0; i < headits.length; i++) {
      if ((!headits[i].isShow()) || (!headits[i].isEnabled()) || 
        (htcols.containsKey(headits[i].getKey()))) continue;
      getBillCardPanel().getHeadItem(headits[i].getKey()).setEnabled(false);
    }

    BillItem[] bodyits = getBillCardPanel().getBodyItems();
    for (int i = 0; i < bodyits.length; i++) {
      if (htcols.containsKey(bodyits[i].getKey())) {
        getBillCardPanel().getBodyItem(bodyits[i].getKey()).setEnabled(true);
      }
      else {
        getBillCardPanel().getBodyItem(bodyits[i].getKey()).setEnabled(false);
      }
    }

    for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
      getBillCardPanel().setCellEditable(i, "cpackunitname", false);
      getBillCardPanel().setCellEditable(i, "npacknumber", false);
    }

    getBillCardPanel().updateUI();
  }

  public ButtonObject[] getExtendBtns()
  {
    return null;
  }

  public void onExtendBtnsClick(ButtonObject bo)
  {
  }

  public void setExtendBtnsStat(int iState)
  {
  }

  public void checkCanUnApprove(SaleinvoiceVO invvo)
    throws BusinessException
  {
    SaleVO head = (SaleVO)invvo.getParentVO();
    if ((head.getFcounteractflag() != null) && (head.getFcounteractflag().intValue() == 1))
      throw new BusinessException(NCLangRes.getInstance().getStrByID("sopub", "UPPsopub-000311"));
  }

  public void afterDiscountrateEdit(BillEditEvent e)
  {
    Object oDiscountrate = null;
    if (e == null) {
      oDiscountrate = getBillCardPanel().getHeadItem("ndiscountrate").getValue();
    }
    else
      oDiscountrate = e.getValue();
    for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
      getBillCardPanel().setBodyValueAt(oDiscountrate, i, "ndiscountrate");

      calculateNumber(i, "nitemdiscountrate");
      afterNumberEdit(e);
      getBillCardPanel().setHeadItem("ntotalsummny", calcurateTotal("noriginalcursummny"));
      getBillCardPanel().execHeadFormula("nnetmny->ntotalsummny-nstrikemny");
      getBillCardPanel().setBodyValueAt(getBillCardPanel().getBodyValueAt(i, "noriginalcursummny"), i, "nsubsummny");

      getBillCardPanel().setBodyValueAt(getBillCardPanel().getBodyValueAt(i, "nsummny"), i, "nsubcursummny");
      setBodyRowState(i);
    }
  }

  public boolean onClosing()
  {
    if ((this.strState.equals("ADD")) || (this.strState.equals("MODIFY")))
    {
      return MessageDialog.showYesNoDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270"), NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000510")) == 4;
    }

    return true;
  }

  /**
	 * 批量自动合并开票
	 *
	 */
	private void batchAutoUnit(){
      java.util.HashMap hsvos = null;
      java.util.HashMap hSuccess = new java.util.HashMap();

      ShowToolsInThread.showStatus(proccdlg, new Integer(0));
      ShowToolsInThread.showMessage(proccdlg, nc.ui.ml.NCLangRes
              .getInstance().getStrByID("40060501", "UPP40060501-100045")/*
                                                                          * @res
                                                                          * "正在进行合并开票前的准备..."
                                                                          */);
      try {
          hsvos = getApproveSaleInvoiceVOs("UNIT");
          if (hsvos == null || hsvos.size() <= 0) {
              showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
                      "40060501", "UPP40060501-100046")/* @res "请选择待合并开票的发票！" */);
              return;
          }
      } catch (Exception ev) {
          showErrorMessage(ev.getMessage());
          return;
      }

      int max = proccdlg.getUIProgressBar1().getMaximum();
      int maxcount = hsvos.size();


      ShowToolsInThread.showMessage(proccdlg, nc.ui.ml.NCLangRes
              .getInstance().getStrByID("40060501", "UPP40060501-100047")/*
                                                                          * @res
                                                                          * "开始合并开票..."
                                                                          */);

      SaleinvoiceVO saleinvoice = null;
      java.util.Iterator iter = hsvos.keySet().iterator();
      int count = 0;

      while (iter.hasNext()) {
          Object key = iter.next();
          saleinvoice = (SaleinvoiceVO) hsvos.get(key);
          ((SaleVO)saleinvoice.getParentVO()).setCapproveid(null);
          ShowToolsInThread.showStatus(proccdlg, new Integer(
                  (int) (max * (1.0 * count / maxcount))));
          ShowToolsInThread.showMessage(proccdlg, nc.ui.ml.NCLangRes
                  .getInstance().getStrByID(
                          "40060501",
                          "UPP40060501-100048",
                          null,
                          new String[] { ((SaleVO) saleinvoice.getParentVO())
                                  .getVreceiptcode() }));
          //			ShowToolsInThread.showMessage(proccdlg, "正在合并开票...["
          //					+ ((SaleVO) saleinvoice.getParentVO()).getVreceiptcode()
          //					+ "]");
          saleinvoice.setCuruserid(getClientEnvironment().getUser()
                  .getPrimaryKey());

          try {
              if (work.getRunState() == WorkThread.Interrupted)
                  break;
              if (((SaleVO)saleinvoice.getParentVO()).getFstatus().intValue()!=1)
              	throw new BusinessException(nc.ui.ml.NCLangRes
                          .getInstance().getStrByID("40060501", "UPP40060501-100063")/*
                           * @res
                           * "非自由状态，不能合并开票"
                           */);

              if (saleinvoice.getHsArsubAcct()!=null && saleinvoice.getHsArsubAcct().size()>0)
              	throw new BusinessException(nc.ui.ml.NCLangRes
                          .getInstance().getStrByID("40060501", "UPP40060501-100064")/*
                           * @res
                           * "已合并开票，先取消后才会再次合并开票"
                           */);
              saleinvoice = SaleinvoiceBO_Client.autoUniteInvoiceToUI(saleinvoice, m_cl);
              if (saleinvoice.getHsArsubAcct()!=null && saleinvoice.getHsArsubAcct().size()>0) {

                  hSuccess.put(key, saleinvoice);
                  String sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID(
                          "40060501",
                          "UPP40060501-100049",
                          null,
                          new String[] { ((SaleVO) saleinvoice.getParentVO())
                                  .getVreceiptcode() });
                  ShowToolsInThread.showMessage(proccdlg, sMsg, sMsg);
                  //					ShowToolsInThread.showMessage(proccdlg, "发票["
                  //							+ ((SaleVO) saleinvoice.getParentVO())
                  //									.getVreceiptcode() + "]合并开票成功！", "发票["
                  //							+ ((SaleVO) saleinvoice.getParentVO())
                  //									.getVreceiptcode() + "]合并开票成功！");
                  //}

              } else {
                  String sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID(
                          "40060501",
                          "UPP40060501-100051",
                          null,
                          new String[] { ((SaleVO) saleinvoice.getParentVO())
                                  .getVreceiptcode() });
                  ShowToolsInThread.showMessage(proccdlg, sMsg, sMsg);
                  //					ShowToolsInThread.showMessage(proccdlg, "发票["
                  //							+ ((SaleVO) saleinvoice.getParentVO())
                  //									.getVreceiptcode() + "]" + "没有符合条件的冲应收单！",
                  //							"发票["
                  //									+ ((SaleVO) saleinvoice.getParentVO())
                  //											.getVreceiptcode() + "]"
                  //									+ "没有符合条件的冲应收单！");
              }

          } catch (BusinessException e) {
              String sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID(
                      "40060501",
                      "UPP40060501-100052",
                      null,
                      new String[] { ((SaleVO) saleinvoice.getParentVO())
                              .getVreceiptcode() });
              sMsg += e.getMessage();
              ShowToolsInThread.showMessage(proccdlg, " ", sMsg);
              //				ShowToolsInThread.showMessage(proccdlg, " ", "发票["
              //						+ ((SaleVO) saleinvoice.getParentVO())
              //								.getVreceiptcode() + "]合并开票失败：" + e.getMessage());
              if (proccdlg.getckHint().isSelected()) {
                  sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID(
                          "40060501",
                          "UPP40060501-100052",
                          null,
                          new String[] { ((SaleVO) saleinvoice.getParentVO())
                                  .getVreceiptcode() });
                  if (ShowToolsInThread.showYesNoDlg(proccdlg, e.getMessage()
                          + "\n"
                          + sMsg
                          + nc.ui.ml.NCLangRes.getInstance().getStrByID(
                                  "40060501", "UPP40060501-100053")/*
                                                                    * @res
                                                                    * "是否继续合并开票以下的发票？"
                                                                    */) == MessageDialog.ID_YES) {
                      continue;
                  } else {
                      work.setRunState(WorkThread.Interrupted);
                      break;
                  }
              }

          } catch (Exception e) {
              String sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID(
                      "40060501",
                      "UPP40060501-100052",
                      null,
                      new String[] { ((SaleVO) saleinvoice.getParentVO())
                              .getVreceiptcode() });
              sMsg += e.getMessage();
              ShowToolsInThread.showMessage(proccdlg, " ", sMsg);
              //				ShowToolsInThread.showMessage(proccdlg, " ", "发票["
              //						+ ((SaleVO) saleinvoice.getParentVO())
              //								.getVreceiptcode() + "]合并开票失败：" + e.getMessage());
              if (proccdlg.getckHint().isSelected()) {
                  sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID(
                          "40060501",
                          "UPP40060501-100052",
                          null,
                          new String[] { ((SaleVO) saleinvoice.getParentVO())
                                  .getVreceiptcode() });
                  if (ShowToolsInThread.showYesNoDlg(proccdlg, e.getMessage()
                          + "\n"
                          + sMsg
                          + nc.ui.ml.NCLangRes.getInstance().getStrByID(
                                  "40060501", "UPP40060501-100053")/*
                                                                    * @res
                                                                    * "是否继续合并开票以下的发票？"
                                                                    */) == MessageDialog.ID_YES) {
                      continue;
                  } else {
                      work.setRunState(WorkThread.Interrupted);
                      break;
                  }
              }
          } finally {
              count++;
          }
      }

      ShowToolsInThread.showStatus(proccdlg, new Integer(max));

      if (work.getRunState() == work.Interrupted) {
          ShowToolsInThread.showMessage(proccdlg, nc.ui.ml.NCLangRes
                  .getInstance().getStrByID("40060501", "UPP40060501-100041")/*
                                                                              * @res
                                                                              * "合并开票操作被用户中断！"
                                                                              */);
      } else {
          ShowToolsInThread.showMessage(proccdlg, nc.ui.ml.NCLangRes
                  .getInstance().getStrByID("40060501", "UPP40060501-100043")/*
                                                                              * @res
                                                                              * "合并开票操作结束！"
                                                                              */);
      }

      try {
          Thread.sleep(500);
      } catch (Exception ex) {

      }

      if (hSuccess.size() >= 0) {
          ShowToolsInThread.showMessage(proccdlg, nc.ui.ml.NCLangRes
                  .getInstance().getStrByID("40060501", "UPP40060501-000054")/*
                                                                              * @res
                                                                              * "正在更新界面数据..."
                                                                              */);
          ShowToolsInThread.doLoadAdminData(this);
      }
		
	}
  private void autoUnit()
  {
    this.saleinvoice = getVO();
    if (((SaleVO)this.saleinvoice.getParentVO()).getFstatus().intValue() != 1) {
      showErrorMessage(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-100063"));

      return;
    }

    if ((this.saleinvoice.getHsArsubAcct() != null) && (this.saleinvoice.getHsArsubAcct().size() > 0)) {
      showErrorMessage(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-100064"));

      return;
    }
    if (!isSameOrderCustomer(this.saleinvoice)) return; try
    {
      SaleinvoiceVO oldvo = this.vocache.getSaleInvoiceVO(getID(this.id));
      ((SaleVO)oldvo.getParentVO()).setCbiztype((String)this.saleinvoice.getParentVO().getAttributeValue("cbiztype"));
      this.saleinvoice.setOldSaleOrderVO(oldvo);
      ((SaleVO)this.saleinvoice.getParentVO()).setCapproveid(null);
      this.saleinvoice = SaleinvoiceBO_Client.autoUniteInvoiceToUI(this.saleinvoice, this.m_cl);
      reLoadListData();
      if (this.saleinvoice.getHsArsubAcct().size() > 0) {
        showHintMessage(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-100043"));
      }
      else
      {
        String sMsg = NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-100051", null, new String[] { ((SaleVO)this.saleinvoice.getParentVO()).getVreceiptcode() });

        showHintMessage(sMsg);
      }
    } catch (BusinessException e) {
      showErrorMessage(e.getMessage());
    } catch (Exception e) {
      showErrorMessage(e.getMessage());
      SCMEnv.out(e.getMessage());
    }
  }

/**
	 * 批量取消合并开票
	 */
	private void batchCancelUnit(){
        java.util.HashMap hsvos = null;
        java.util.HashMap hSuccess = new java.util.HashMap();

        ShowToolsInThread.showStatus(proccdlg, new Integer(0));
        ShowToolsInThread.showMessage(proccdlg, nc.ui.ml.NCLangRes
                .getInstance().getStrByID("40060501", "UPP40060501-100055")/*
                                                                            * @res
                                                                            * "正在进行取消合并开票前的准备..."
                                                                            */);
        try {
            hsvos = getApproveSaleInvoiceVOs("UNIT");
            if (hsvos == null || hsvos.size() <= 0) {
                showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
                        "40060501", "UPP40060501-100056")/* @res "请选择待合并开票的发票！" */);
                return;
            }
        } catch (Exception ev) {
            showErrorMessage(ev.getMessage());
            return;
        }

        int max = proccdlg.getUIProgressBar1().getMaximum();
        int maxcount = hsvos.size();


        ShowToolsInThread.showMessage(proccdlg, nc.ui.ml.NCLangRes
                .getInstance().getStrByID("40060501", "UPP40060501-100057")/*
                                                                            * @res
                                                                            * "开始合并开票..."
                                                                            */);

        SaleinvoiceVO saleinvoice = null;
        java.util.Iterator iter = hsvos.keySet().iterator();
        int count = 0;

        while (iter.hasNext()) {
            Object key = iter.next();
            id = Integer.parseInt(key.toString());
            saleinvoice = (SaleinvoiceVO) hsvos.get(key);
            ((SaleVO)saleinvoice.getParentVO()).setCapproveid(null);
            ShowToolsInThread.showStatus(proccdlg, new Integer(
                    (int) (max * (1.0 * count / maxcount))));
            ShowToolsInThread.showMessage(proccdlg, nc.ui.ml.NCLangRes
                    .getInstance().getStrByID(
                            "40060501",
                            "UPP40060501-100058",
                            null,
                            new String[] { ((SaleVO) saleinvoice.getParentVO())
                                    .getVreceiptcode() }));
            //			ShowToolsInThread.showMessage(proccdlg, "正在合并开票...["
            //					+ ((SaleVO) saleinvoice.getParentVO()).getVreceiptcode()
            //					+ "]");
            saleinvoice.setCuruserid(getClientEnvironment().getUser()
                    .getPrimaryKey());

            try {
                if (work.getRunState() == WorkThread.Interrupted)
                    break;
                if (((SaleVO)saleinvoice.getParentVO()).getFstatus().intValue()!=1)
                	throw new BusinessException(nc.ui.ml.NCLangRes
                            .getInstance().getStrByID("40060501", "UPP40060501-100065")/*
                             * @res
                             * "非自由状态，不能合并开票"
                             */);

                if (saleinvoice.getHsArsubAcct()==null || saleinvoice.getHsArsubAcct().size()<1)
                	throw new BusinessException(nc.ui.ml.NCLangRes
                            .getInstance().getStrByID("40060501", "UPP40060501-100066")/*
                             * @res
                             * "已合并开票，先取消后才会再次合并开票"
                             */);
                loadCardData(saleinvoice);
                getBillCardPanel().updateValue();
                String[] strikeKeys = new String[] { "nquoteprice",
                        "nquotetaxprice", "nquotenetprice", "nquotetaxnetprice",
                        "noriginalcursummny" };
                String[] prestrikeKeys = new String[] { "nsubquoteprice",
                        "nsubquotetaxprice", "nsubquotenetprice",
                        "nsubquotetaxnetprice", "nsubsummny" };
                for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
                    for (int j = 0; j < strikeKeys.length; j++) {
                        getBillCardPanel().setBodyValueAt(
                                getBillCardPanel().getBodyValueAt(i,
                                        prestrikeKeys[j]), i, strikeKeys[j]);
                        calculateNumber(i, strikeKeys[j]);
                        if(getBillCardPanel().getBillModel().getRowState(i)!=BillModel.ADD &&getBillCardPanel().getBillModel().getRowState(i)!=BillModel.DELETE)
                            getBillCardPanel().getBillModel().setRowState(i,
                                    BillModel.MODIFICATION);
                    }
                }
                getBillCardPanel().setHeadItem("nstrikemny", new UFDouble(0));
                getBillCardPanel().execHeadFormula("nnetmny->ntotalsummny-nstrikemny");
                //计算合并开票金额
                countCardUniteMny();

                //当放弃合并时，一次放弃所有的合并关系(将冲减金额清零，以便回写)
                hsSelectedARSubHVO = saleinvoice.getHsArsubAcct();
                java.util.Enumeration e = hsSelectedARSubHVO.keys();
                while (e.hasMoreElements()) {
                    String key1 = (String) e.nextElement();
                    hsSelectedARSubHVO.put(key1, new UFDouble(0));
                    hsTotalBykey.put(key1, new UFDouble(0));
                    oldhsTotalBykey.put(key1, new UFDouble(0));
                }
                onSave();
                saleinvoice = (SaleinvoiceVO) getBillCardPanel().getBillValueVO(SaleinvoiceVO.class.getName(),
                        SaleVO.class.getName(), SaleinvoiceBVO.class.getName());

                    hSuccess.put(key, saleinvoice);
                    String sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID(
                            "40060501",
                            "UPP40060501-100059",
                            null,
                            new String[] { ((SaleVO) saleinvoice.getParentVO())
                                    .getVreceiptcode() });
                    ShowToolsInThread.showMessage(proccdlg, sMsg, sMsg);
                    //					ShowToolsInThread.showMessage(proccdlg, "发票["
                    //							+ ((SaleVO) saleinvoice.getParentVO())
                    //									.getVreceiptcode() + "]合并开票成功！", "发票["
                    //							+ ((SaleVO) saleinvoice.getParentVO())
                    //									.getVreceiptcode() + "]合并开票成功！");
                    //}

            } catch (BusinessException e) {
                String sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID(
                        "40060501",
                        "UPP40060501-100061",
                        null,
                        new String[] { ((SaleVO) saleinvoice.getParentVO())
                                .getVreceiptcode() });
                sMsg += e.getMessage();
                ShowToolsInThread.showMessage(proccdlg, " ", sMsg);
                //				ShowToolsInThread.showMessage(proccdlg, " ", "发票["
                //						+ ((SaleVO) saleinvoice.getParentVO())
                //								.getVreceiptcode() + "]合并开票失败：" + e.getMessage());
                if (proccdlg.getckHint().isSelected()) {
                    sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID(
                            "40060501",
                            "UPP40060501-100061",
                            null,
                            new String[] { ((SaleVO) saleinvoice.getParentVO())
                                    .getVreceiptcode() });
                    if (ShowToolsInThread.showYesNoDlg(proccdlg, e.getMessage()
                            + "\n"
                            + sMsg
                            + nc.ui.ml.NCLangRes.getInstance().getStrByID(
                                    "40060501", "UPP40060501-100062")/*
                                                                      * @res
                                                                      * "是否继续合并开票以下的发票？"
                                                                      */) == MessageDialog.ID_YES) {
                        continue;
                    } else {
                        work.setRunState(WorkThread.Interrupted);
                        break;
                    }
                }

            } catch (Exception e) {
                String sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID(
                        "40060501",
                        "UPP40060501-100061",
                        null,
                        new String[] { ((SaleVO) saleinvoice.getParentVO())
                                .getVreceiptcode() });
                sMsg += e.getMessage();
                ShowToolsInThread.showMessage(proccdlg, " ", sMsg);
                //				ShowToolsInThread.showMessage(proccdlg, " ", "发票["
                //						+ ((SaleVO) saleinvoice.getParentVO())
                //								.getVreceiptcode() + "]合并开票失败：" + e.getMessage());
                if (proccdlg.getckHint().isSelected()) {
                    sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID(
                            "40060501",
                            "UPP40060501-100061",
                            null,
                            new String[] { ((SaleVO) saleinvoice.getParentVO())
                                    .getVreceiptcode() });
                    if (ShowToolsInThread.showYesNoDlg(proccdlg, e.getMessage()
                            + "\n"
                            + sMsg
                            + nc.ui.ml.NCLangRes.getInstance().getStrByID(
                                    "40060501", "UPP40060501-100062")/*
                                                                      * @res
                                                                      * "是否继续合并开票以下的发票？"
                                                                      */) == MessageDialog.ID_YES) {
                        continue;
                    } else {
                        work.setRunState(WorkThread.Interrupted);
                        break;
                    }
                }
            } finally {
                count++;
            }
        }

        ShowToolsInThread.showStatus(proccdlg, new Integer(max));

        if (work.getRunState() == work.Interrupted) {
            ShowToolsInThread.showMessage(proccdlg, nc.ui.ml.NCLangRes
                    .getInstance().getStrByID("40060501", "UPP40060501-100042")/*
                                                                                * @res
                                                                                * "合并开票操作被用户中断！"
                                                                                */);
        } else {
            ShowToolsInThread.showMessage(proccdlg, nc.ui.ml.NCLangRes
                    .getInstance().getStrByID("40060501", "UPP40060501-100044")/*
                                                                                * @res
                                                                                * "合并开票操作结束！"
                                                                                */);
        }

        try {
            Thread.sleep(500);
        } catch (Exception ex) {

        }

        if (hSuccess.size() >= 0) {
            ShowToolsInThread.showMessage(proccdlg, nc.ui.ml.NCLangRes
                    .getInstance().getStrByID("40060501", "UPP40060501-000054")/*
                                                                                * @res
                                                                                * "正在更新界面数据..."
                                                                                */);
            ShowToolsInThread.doLoadAdminData(this);
        }
		
	}
  private void cancelUnit()
  {
    this.saleinvoice = getVO();
    ((SaleVO)this.saleinvoice.getParentVO()).setCapproveid(null);
    if (((SaleVO)this.saleinvoice.getParentVO()).getFstatus().intValue() != 1)
    {
      showErrorMessage(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-100065"));

      return;
    }

    if ((this.saleinvoice.getHsArsubAcct() == null) || (this.saleinvoice.getHsArsubAcct().size() < 1)) {
      showErrorMessage(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-100066"));

      return;
    }
    int result = showOkCancelMessage(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000097"));

    if (result != 1)
      return;
    try {
      loadCardData(this.saleinvoice);
      getBillCardPanel().updateValue();
      String[] strikeKeys = { "nquoteprice", "nquotetaxprice", "nquotenetprice", "nquotetaxnetprice", "noriginalcursummny" };

      String[] prestrikeKeys = { "nsubquoteprice", "nsubquotetaxprice", "nsubquotenetprice", "nsubquotetaxnetprice", "nsubsummny" };

      for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
        for (int j = 0; j < strikeKeys.length; j++) {
          getBillCardPanel().setBodyValueAt(getBillCardPanel().getBodyValueAt(i, prestrikeKeys[j]), i, strikeKeys[j]);

          calculateNumber(i, strikeKeys[j]);
          if ((getBillCardPanel().getBillModel().getRowState(i) != 1) && (getBillCardPanel().getBillModel().getRowState(i) != 3)) {
            getBillCardPanel().getBillModel().setRowState(i, 2);
          }
        }
      }
      getBillCardPanel().setHeadItem("nstrikemny", new UFDouble(0));
      getBillCardPanel().execHeadFormula("nnetmny->ntotalsummny-nstrikemny");

      countCardUniteMny();

      this.hsSelectedARSubHVO = this.saleinvoice.getHsArsubAcct();
      Enumeration e = this.hsSelectedARSubHVO.keys();
      while (e.hasMoreElements()) {
        String key1 = (String)e.nextElement();
        this.hsSelectedARSubHVO.put(key1, new UFDouble(0));
        this.hsTotalBykey.put(key1, new UFDouble(0));
        this.oldhsTotalBykey.put(key1, new UFDouble(0));
      }
      this.bstrikeflag = false;
      onSave();
      this.hsSelectedARSubHVO.clear();
      this.hsTotalBykey.clear();
      this.oldhsTotalBykey.clear();
      reLoadListData();
    } catch (Exception e) {
      showErrorMessage(e.getMessage());
      SCMEnv.out(e.getMessage());
    }
  }

  public SOBillCardTools getBillCardTools()
  {
    if (this.soBillCardTools == null) {
      this.soBillCardTools = new SOBillCardTools(getBillCardPanel(), getClientEnvironment());
    }

    return this.soBillCardTools;
  }

  public void onImpInvoice()
  {
    try
    {
      GoldTaxVO[] goldTaxVOs = new TransGoldTaxDlg(this).importGoldTax();
      if (goldTaxVOs == null) {
        return;
      }
      SCMEnv.out("导入金税VO数组长度 goldTaxVOs.length = " + goldTaxVOs.length);

      HashMap hmHeadVO = new HashMap();

      for (int i = 0; i < goldTaxVOs.length; i++) {
        GoldTaxHeadVO taxHeadVO = goldTaxVOs[i].getParentVO();

        String billcode = taxHeadVO.getCode();

        if (hmHeadVO.containsKey(billcode)) {
          SaleVO voHead = (SaleVO)hmHeadVO.get(billcode);

          String oldtaxcode = voHead.getVreceiptcode() == null ? "" : 
            voHead.getVreceiptcode();
          String newtaxcode = taxHeadVO.getTaxBillNo() == null ? "" : 
            taxHeadVO.getTaxBillNo();
          voHead.setVreceiptcode(oldtaxcode + "," + newtaxcode);
        } else {
          SaleVO saleVO = new SaleVO();

          saleVO.setVreceiptcode(taxHeadVO.getCode());

          saleVO.setPk_corp(getCorpPrimaryKey());

          saleVO.setVdef20(taxHeadVO.getTaxBillNo());

          hmHeadVO.put(taxHeadVO.getCode(), saleVO);
        }
      }

      SaleVO[] voHeads = (SaleVO[])hmHeadVO.values().toArray(new SaleVO[0]);

      List list = new ArrayList();
      StringBuffer sb = new StringBuffer();
      for (int i = 0; i < voHeads.length; i++) {
        String fph = voHeads[i].getVdef20();
        String djh = voHeads[i].getVreceiptcode();
        String sql = " update so_saleinvoice set  vreceiptcode='" + fph + "' where vreceiptcode='" + djh + "'; ";
        sb.append(sql);
        list.add(sql);
      }
      IIcbItf icop = (IIcbItf)NCLocator.getInstance().lookup(IIcbItf.class.getName());
      int row = icop.updateBYsql(list);
      showHintMessage(NCLangRes.getInstance()
        .getStrByID("40060301", "UPT40060301-000661"));
    }
    catch (Exception e) {
      Logger.error(NCLangRes.getInstance().getStrByID("40060301", "UPT40060301-000662"), e);
      showErrorMessage(NCLangRes.getInstance().getStrByID("40060301", "UPT40060301-000663") + e.getMessage());
    }
  }

  public String getParentCorpCode()
  {
    String ParentCorp = new String();
    String key = ClientEnvironment.getInstance().getCorporation()
      .getFathercorp();
    try {
      CorpVO corpVO = CorpBO_Client.findByPrimaryKey(key);
      ParentCorp = corpVO.getUnitcode();
    }
    catch (BusinessException e) {
      e.printStackTrace();
    }
    return ParentCorp;
  }

  public class WorkThread extends Thread
  {
    public static final int Interrupted = -1;
    public static final int Run = 1;
    public static final int Stop = 0;
    int runstate = 0;

    int iresults = -1;

    Exception ex = null;

    String tag = null;

    public WorkThread(String str) {
      this.tag = str;
    }

    public void run() {
      this.runstate = 1;
      this.ex = null;
      try {
        if (this.tag.equals("APPROVE"))
          SaleInvoiceAdminUI.this.doApproveWork(this.tag);
        else if (this.tag.equals("SoUnApprove"))
          SaleInvoiceAdminUI.this.doUnApproveWork(this.tag);
        else if (this.tag.equals("UNITE"))
          SaleInvoiceAdminUI.this.batchAutoUnit();
        else if (this.tag.equals("UNUNITE")) {
          SaleInvoiceAdminUI.this.batchCancelUnit();
        }
        else if (this.tag.equals("SoBlankOut")) {
          SaleInvoiceAdminUI.this.doDeleteWork(this.tag);
        }
        if (this.runstate == 1)
          this.runstate = 0;
      }
      catch (Exception e) {
        this.iresults = -1;
        this.ex = e;
        this.runstate = -1;
      }
      if (this.runstate == -1) {
        if (this.tag.equals("APPROVE")) {
          ShowToolsInThread.showMessage(SaleInvoiceAdminUI.this.proccdlg, NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000041"));
        }
        else if (this.tag.equals("SoUnApprove")) {
          ShowToolsInThread.showMessage(SaleInvoiceAdminUI.this.proccdlg, NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000042"));
        }
        else if (this.tag.equals("UNITE")) {
          ShowToolsInThread.showMessage(SaleInvoiceAdminUI.this.proccdlg, NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-100041"));
        }
        else if (this.tag.equals("UNUNITE")) {
          ShowToolsInThread.showMessage(SaleInvoiceAdminUI.this.proccdlg, NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-100069"));
        }
        else if (this.tag.equals("SoBlankOut")) {
          ShowToolsInThread.showMessage(SaleInvoiceAdminUI.this.proccdlg, NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-100042"));
        }

      }
      else if (this.tag.equals("APPROVE")) {
        ShowToolsInThread.showMessage(SaleInvoiceAdminUI.this.proccdlg, NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000043"));
      }
      else if (this.tag.equals("SoUnApprove")) {
        ShowToolsInThread.showMessage(SaleInvoiceAdminUI.this.proccdlg, NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000044"));
      }
      else if (this.tag.equals("UNITE")) {
        ShowToolsInThread.showMessage(SaleInvoiceAdminUI.this.proccdlg, NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-100043"));
      }
      else if (this.tag.equals("UNUNITE")) {
        ShowToolsInThread.showMessage(SaleInvoiceAdminUI.this.proccdlg, NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-100044"));
      }
      else if (this.tag.equals("SoBlankOut")) {
        ShowToolsInThread.showMessage(SaleInvoiceAdminUI.this.proccdlg, NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-100070"));
      }

      try
      {
        ShowToolsInThread.doEnableBtn(SaleInvoiceAdminUI.this.proccdlg);
      } catch (Exception localException1) {
      }
      SCMEnv.out("this is a finished!");
    }

    public synchronized void setRunState(int state)
    {
      this.runstate = state;
    }

    public int getRunState() {
      return this.runstate;
    }

    public int getResult() {
      return this.iresults;
    }
  }
  
  	//add by zwx 2019-3-12 根据销售单号查询应收单信息
  	private String getInfo(String csaleid) {

        	StringBuffer sql = new StringBuffer();
    		sql.append(" SELECT DISTINCT  zb.djbh, zb.dwbm,bas.custcode ") 
    		.append("   FROM arap_djzb zb ") 
    		.append("   inner join arap_djfb fb ") 
    		.append("  on zb.vouchid = fb.vouchid ") 
    		.append("  inner join bd_cumandoc man ") 
    		.append("  on man.pk_cumandoc = fb.ksbm_cl ") 
    		.append("  inner join bd_cubasdoc bas ") 
    		.append("  on bas.pk_cubasdoc = man.pk_cubasdoc ") 
    		.append("    where fb.ddlx = '"+csaleid+"' ") 
    		.append("    and rtrim(fb.jsfsbm, ' ') = '32' ") 
    		.append("    and zb.djdl = 'ys' ") 
    		.append("    and nvl(zb.dr,0) = 0 ") 
    		.append("    and nvl(zb.dr,0) = 0 ") ;
    		List list = new ArrayList();
    		Map map = new HashMap();
    		try {
    			IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
//    			System.out.println(sql.toString());
    			list = (List) bs.executeQuery(sql.toString(), new MapListProcessor());
    			for(int a =0;a<list.size();a++){
    				map = (Map) list.get(0);
    			}
    		} catch (BusinessException e2) {
    			e2.printStackTrace();
    		}

    		String result = "";
    		
    		//应收单号
    		String ysdh =map.get("djbh")==null?"":map.get("djbh").toString();
    		//公司编码
//    		String dwbm =map.get("dwbm")==null?"":map.get("dwbm").toString();
    		String dwbm = ClientEnvironment.getInstance().getCorporation().getUnitcode();
    		//客商编码
    		String custcode =map.get("custcode")==null?"":map.get("custcode").toString();
    		Map jsonMap = new HashMap();

    		
    		jsonMap.put("password", "123456");//对接密码
    		jsonMap.put("username", "baosteel");//对接用户名
    		jsonMap.put("unitcode", dwbm);// 公司编码
    		jsonMap.put("vendedorcode", custcode);// 客商代码
    		jsonMap.put("yscode", ysdh);//应收单号
    		jsonMap.put("delflag", "1");// 删除标识
    		
    		Gson gson = new Gson();
    		Object ss = gson.toJson(jsonMap);
    		StringBuffer jsonsb = new StringBuffer();
    		jsonsb.append("[").append(ss).append("]");
        		
    		
    		return jsonsb.toString();
    		
        }

  	//add by zwx 2019-3-13 发送至CVM接口
	private String sendToCVM(String json){
		URL url;
		String result = "";
		try {
			url = new URL("http://10.70.26.23/cvm/CVMService/NC009");
			result  = this.httpconnect(url, json);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return "[{\"status\":\"error\",\"message\":\"连接CVM接口异常(URL协议)'"+e.getMessage()+"'\"}]";
		}catch(Exception e){
			e.printStackTrace();
			return "[{\"status\":\"error\",\"message\":\"连接CVM接口异常(URL协议)'"+e.getMessage()+"'\"}]";
		}
		return result;
	}
   //end by zwx 
}