package nc.ui.so.so002;

import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import nc.bs.bd.b21.BusinessCurrencyRateUtil;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.ml.NCLangResOnserver;
import nc.itf.scm.so.so002.ISaleinvoiceQuery;
import nc.ui.bd.CorpBO_Client;
import nc.ui.bd.b21.CurrtypeQuery;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.dbcache.gui.MessageBox;
import nc.ui.ml.NCLangRes;
import nc.ui.pf.change.PfUtilUITools;
import nc.ui.pf.query.ICheckRetVO;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIMenuItem;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillBodyMenuListener;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListData;
import nc.ui.pub.bill.BillTotalListener;
import nc.ui.pub.formulaparse.FormulaParse;
import nc.ui.pub.linkoperate.ILinkAdd;
import nc.ui.pub.linkoperate.ILinkAddData;
import nc.ui.pub.linkoperate.ILinkApprove;
import nc.ui.pub.linkoperate.ILinkApproveData;
import nc.ui.pub.linkoperate.ILinkMaintain;
import nc.ui.pub.linkoperate.ILinkMaintainData;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.pub.para.SysInitBO_Client;
import nc.ui.pub.pf.PfUtilBO_Client;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.pub.print.PrintEntry;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.pub.workflownote.FlowStateDlg;
import nc.ui.scm.file.DocumentManager;
import nc.ui.scm.ic.freeitem.FreeItemRefPane;
import nc.ui.scm.print.IFreshTsListener;
import nc.ui.scm.print.PrintLogClient;
import nc.ui.scm.print.SalePubPrintDS;
import nc.ui.scm.pub.BillFormulaContainer;
import nc.ui.scm.pub.CollectSettingDlg;
import nc.ui.scm.pub.FreeVOParse;
import nc.ui.scm.pub.bill.ButtonTree;
import nc.ui.scm.pub.bill.IBillExtendFun;
import nc.ui.scm.pub.def.DefSetTool;
import nc.ui.scm.pub.query.SCMQueryConditionDlg;
import nc.ui.scm.pub.report.BillRowNo;
import nc.ui.scm.ref.prm.CustAddrRefModel;
import nc.ui.scm.ref.prm.CustBankRefModel;
import nc.ui.scm.sourcebill.SourceBillFlowDlg;
import nc.ui.so.so001.SaleOrderBO_Client;
import nc.ui.so.so001.panel.card.SOBillCardTools;
import nc.ui.so.so001.panel.list.SOBusiTypeRefPane;
import nc.ui.trade.business.HYPubBO_Client;
import nc.vo.bd.CorpVO;
import nc.vo.bd.b20.CurrtypeVO;
import nc.vo.bd.b21.CurrinfoVO;
import nc.vo.logging.Debug;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.NullFieldException;
import nc.vo.pub.SuperVOUtil;
import nc.vo.pub.ValidationException;
import nc.vo.pub.bill.BillTempletVO;
import nc.vo.pub.formulaset.VarryVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.msg.MessageVO;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.query.QueryConditionVO;
import nc.vo.scm.ic.bill.FreeVO;
import nc.vo.scm.ic.bill.InvVO;
import nc.vo.scm.pu.Timer;
import nc.vo.scm.pub.ArrayMethod;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.smart.ObjectUtils;
import nc.vo.scm.pub.vosplit.SplitBillVOs;
import nc.vo.so.pub.CustCreditVO;
import nc.vo.so.so001.SaleOrderVO;
import nc.vo.so.so002.SaleVO;
import nc.vo.so.so002.SaleinvoiceBVO;
import nc.vo.so.so002.SaleinvoiceVO;
import nc.vo.so.so006.ProfitHeaderVO;
import nc.vo.so.so006.ProfitItemVO;
import nc.vo.so.so006.ProfitVO;
import nc.vo.so.so015.ARSubUniteVO;
import nc.vo.so.so015.ARSubVO;

public class SaleInvoiceUI extends ToftPanel
  implements BillEditListener, BillEditListener2, BillBodyMenuListener, BillTotalListener, ICheckRetVO, ILinkApprove, ILinkQuery, ILinkAdd, ILinkMaintain, IFreshTsListener, IBillExtendFun
{
  private BillCardPanel ivjBillCardPanel = null;

  private FreeItemRefPane ivjFreeItemRefPane = null;

  private ButtonTree bt = null;

  private ButtonObject m_boAdd = null;

  private ButtonObject m_bobill = null;

  private ButtonObject m_boModify = null;

  private ButtonObject m_boSave = null;
  private ButtonObject m_boCancel = null;

  private ButtonObject m_boLineOper = null;

  private ButtonObject m_boAddLine = null;

  private ButtonObject m_boDelLine = null;
  private ButtonObject m_boCheck = null;

  private ButtonObject m_boUnAprove = null;
  private ButtonObject m_boBlankOut = null;

  private ButtonObject m_boPrint = null;

  private ButtonObject m_boHelp = new ButtonObject(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000032"), NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000032"), 0, "帮助");

  private ButtonObject m_boBusiType = null;

  private ButtonObject m_boAction = null;

  private ButtonObject m_boAssistant = null;

  private ButtonObject m_boAtp = null;

  private ButtonObject m_boCustInfo = null;

  private ButtonObject m_boCustCredit = null;

  private ButtonObject m_boPrifit = null;

  private ButtonObject m_boInvoiceExecRpt = new ButtonObject(NCLangRes.getInstance().getStrByID("pub_billaction", "D32InvoiceExecRpt"), NCLangRes.getInstance().getStrByID("pub_billaction", "D32InvoiceExecRpt"), 0, "发票执行情况");

  private ButtonObject m_boSoTax = null;

  private ButtonObject m_boReturn = new ButtonObject(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000464"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000464"), 1, "返回");

  protected String warehouseRefWhereSql = null;

  private ARSubVO[] arsubNew = null;

  protected boolean bInMsgPanel = false;

  protected ButtonObject boAuditFlowStatus = null;

  private boolean bstrikeflag = false;

  QueryConditionClient dlgStrikeQuery = null;

  protected Hashtable hsparas = null;

  public Hashtable hsQueryArsubDataBykey = new Hashtable();

  public Hashtable hsSelectedARSubHVO = null;

  public Hashtable hsTotalBykey = new Hashtable();

  private ButtonObject m_boUnite = null;

  private ButtonObject m_boUniteCancel = null;

  private ButtonObject m_boUniteInvoice = new ButtonObject(NCLangRes.getInstance().getStrByID("40060501", "UPT40060501-000048"), NCLangRes.getInstance().getStrByID("40060501", "UPT40060501-000048"), 0, "合并开票");
  private String m_strWhere;
  public Hashtable oldhsSelectedARSubHVO = null;

  public Hashtable oldhsTotalBykey = new Hashtable();

  UFDouble presummoney = null;

  Hashtable presummoneyByProductLine = null;

  private UFDouble SO_22 = new UFDouble(0);

  private UFBoolean SO_27 = null;

  private UFBoolean SO_30 = null;

  private UFBoolean SO60 = null;

  UFDouble strikemoney = null;

  SaleInvoiceTools m_tools = null;

  private ButtonObject[] aryButtonGroup = null;

  private ButtonObject[] aryButtonGroupList = null;

  private String strState = "FREE";

  private String custID = null;

  private String m_strID = null;

  private Vector vDelLine = null;

  private int delID = -1;

  private int digit = 4;

  private String user = null;

  private String pk_corp = null;

  private ClientEnvironment ce = null;

  private String strBusitype = null;

  private boolean isEdit = false;
  private double[] orderNumber;
  private double[] packNumber;
  private UIMenuItem[] m_bodyMenu = null;
  private static final String NO_BUSINESS_TYPE = "KHHH0000000000000001";
  private SaleinvoiceVO[] saleinvoiceNew = null;

  private SaleinvoiceBVO[] saleinvoiceBs = null;

  private String m_title = NCLangRes.getInstance().getStrByID("40060501", "UPT40060501-000045");

  private UFBoolean SA_02 = null;

  private UFBoolean SA_08 = null;

  private String SO_06 = null;

  private UFBoolean BD302 = null;

  private UFDouble SO34 = new UFDouble(0.0D);

  private UFBoolean SO36 = new UFBoolean(true);

  private UFBoolean SO49 = new UFBoolean(false);

  protected Integer BD501 = null;

  protected Integer BD502 = null;

  protected Integer BD503 = null;

  protected Integer BD505 = null;

  protected BusinessCurrencyRateUtil currtype = null;

  private String code = null;
  protected UFBoolean SO59 = null;

  private String strCurrencyType = null;

  private ArrayList alInvs = new ArrayList();

  public UFDouble nUniteInvoiceMnyBeforeChange = null;

  protected PrintLogClient printLogClient = null;

  public static final String sAll = NCLangRes.getInstance().getStrByID("40069907", "UPT40069907-000001");

  public static final String sSafePrice = NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000138");

  public static final String sReturnPrif = NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000139");

  public static final String sSelfMake = NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000140");

  private ArrayList alBills = null;

  private SaleInvoiceListPanel blpAdd = null;

  protected BillTempletVO billtempletVO = null;

  private boolean isAddList = true;

  private SaleinvoiceVO oldVO = null;
  private SOBillCardTools soBillCardTools = null;

  protected HashMap hsBIEnable = new HashMap();

  private ButtonObject boOrderQuery = null;

  protected ButtonObject boSendAudit = null;

  public String[] fixed = null;

  boolean isGather = false;
  BillFormulaContainer m_billFormulaContain;
  private ButtonObject m_boAfterAction = new ButtonObject(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000038"), NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000038"), 0, "后续业务");

  public static ButtonObject m_boBillCombin = new ButtonObject(NCLangRes.getInstance().getStrByID("40060501", "UPT40060501-000058"), NCLangRes.getInstance().getStrByID("40060501", "UPT40060501-000058"), 2);

  private ButtonObject m_boDocument = null;

  private ButtonObject m_boGather = null;

  private ButtonObject m_boPreview = null;

  private boolean m_isCodeChanged = false;

  private String m_oldreceipt = null;

  private UFBoolean SA_15 = null;

  public SaleinvoiceVO saleinvoice = null;

  public String[] scaleMap = null;

  public String sEmployeeRefCondition = null;

  private UFBoolean SO_20 = new UFBoolean("Y");

  String sVerifyrule = new String();

  Hashtable strikemoneyByProductLine = null;

  UFDouble summoney = null;

  Hashtable summoneyByProductLine = null;

  public SaleInvoiceUI()
  {
    initialize();
  }

  public void actionPerformed(ActionEvent e)
  {
    onMenuItemClick(e);
  }

  public void afterEdit(BillEditEvent e)
  {
    if (!this.isEdit)
      return;
    if (e.getPos() == 0) {
      if (e.getKey().equals("vreceiptcode")) {
        this.m_isCodeChanged = true;
      }

      if (e.getKey().equals("cdeptid")) {
        afterDeptEdit(e);
      }

      if ((!e.getKey().equals("cemployeeid")) || 
        (e.getKey().equals("ccustomername"))) {
        afterCustomerEdit(e);
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

      if (e.getKey().equals("ccurrencyid")) {
        afterCurrencyEdit(e);
      }

      if (e.getKey().equals("ndiscountrate")) {
        afterDiscountrateEdit(e);
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
      
      if(e.getKey().equals("sjtsje"))
      {
    	  if(e.getValue()!=null&&!e.getValue().toString().trim().equals(""))
    	  {
	    	  UFDouble price = (UFDouble)getBillCardPanel().getBodyValueAt(e.getRow(), "noriginalcurtaxprice");
	    	  UFDouble je = new UFDouble(Double.valueOf((String)e.getValue()));
	    	  Integer sl = new BigDecimal(je.div(price).doubleValue()).setScale(0, BigDecimal.ROUND_HALF_UP).intValue(); 
	    	  getBillCardPanel().setBodyValueAt(sl, e.getRow(), "sjtssl");
	    	  
	    	  // 总途损金额=折扣额+实际途损金额
	    	  UFDouble zke = (UFDouble)getBillCardPanel().getBodyValueAt(e.getRow(), "noriginalcurdiscountmny");
	    	  getBillCardPanel().setBodyValueAt(je.add(zke), e.getRow(), "ztsje");
	    	  
    	  }else
    	  {
    		  getBillCardPanel().setBodyValueAt(null, e.getRow(), "sjtssl");
    		  UFDouble zke = (UFDouble)getBillCardPanel().getBodyValueAt(e.getRow(), "noriginalcurdiscountmny");
    		  getBillCardPanel().setBodyValueAt(zke, e.getRow(), "ztsje");
    	  }
    	  
    	   
      }else if(e.getKey().equals("sjtssl"))
      {
    	  if(e.getValue()!=null&&!e.getValue().toString().trim().equals(""))
    	  {
    		  UFDouble sl = new UFDouble(Double.valueOf((String)e.getValue()));
    		  UFDouble price = (UFDouble)getBillCardPanel().getBodyValueAt(e.getRow(), "noriginalcurtaxprice");
    		  getBillCardPanel().setBodyValueAt(sl.multiply(price), e.getRow(), "sjtsje");
    		  UFDouble zke = (UFDouble)getBillCardPanel().getBodyValueAt(e.getRow(), "noriginalcurdiscountmny");
    		  getBillCardPanel().setBodyValueAt(zke.add(sl.multiply(price)), e.getRow(), "ztsje");
    	  }else
    	  {
    		  getBillCardPanel().setBodyValueAt(null, e.getRow(), "sjtsje");
    		  UFDouble zke = (UFDouble)getBillCardPanel().getBodyValueAt(e.getRow(), "noriginalcurdiscountmny");
    		  getBillCardPanel().setBodyValueAt(zke, e.getRow(), "ztsje");
    	  } 
      }else  if(e.getKey().equals("nnumber"))
      { 
    	  if(e.getValue()!=null&&!e.getValue().toString().trim().equals(""))
    	  {
	          UFDouble value = (UFDouble)getBillCardPanel().getBodyValueAt(e.getRow(), "httsbl");
	          //四舍五入
	          Integer httssl = new BigDecimal(value.div(100).doubleValue()*Double.valueOf((String)e.getValue())).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
	          getBillCardPanel().setBodyValueAt(httssl,e.getRow(),"httssl");
    	  }else
    	  {
    		  getBillCardPanel().setBodyValueAt(null,e.getRow(),"httssl");
    	  }
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
    String[] clearCol = { "scalefactor", "fixedflag", "creceipttype", "csourcebillid", "csourcebillbodyid", "nnumber", "npacknumber", "noriginalcurprice", "noriginalcurtaxprice", "noriginalcurnetprice", "noriginalcurtaxnetprice", "noriginalcurtaxmny", "noriginalcurmny", "noriginalcursummny", "noriginalcurdiscountmny" };

    getBillCardPanel().clearRowData(e.getRow(), clearCol);
    if (this.isGather)
      initUnite();
    getBillCardPanel().execBodyFormulas(e.getRow(), new String[] { "cprolineid->getColValue(bd_invbasdoc,pk_prodline,pk_invbasdoc,cinvbasdocid)", "cprolinename->getColValue(bd_prodline,prodlinename,pk_prodline,cprolineid)" });
    try
    {
      if (this.alInvs != null) {
        String sTempID1 = (String)getBillCardPanel().getBodyValueAt(e.getRow(), "cinventoryid");

        String sTempID2 = null;
        ArrayList alIDs = new ArrayList();
        alIDs.add(sTempID2);
        alIDs.add(sTempID1);
        alIDs.add(getClientEnvironment().getUser().getPrimaryKey());
        alIDs.add(getCorpPrimaryKey());
        InvVO voInv = nc.ui.so.pub.BillTools.queryInfo(new Integer(0), alIDs);

        if (this.alInvs.size() == 0)
          this.alInvs.add(voInv);
        else
          this.alInvs.set(e.getRow(), voInv);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      SCMEnv.out("自由项设置失败!");
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

  public void afterNumberChange(int row)
  {
    if (getBillCardPanel().getBodyValueAt(row, "fixedflag") == null) {
      getBillCardPanel().setBodyValueAt(new UFBoolean(false), row, "fixedflag");
    }

    calculateNumber(row, "nnumber");
   
    getBillCardPanel().setBodyValueAt(getBillCardPanel().getBodyValueAt(row, "nquotetaxnetprice"), row, "nsubquotetaxnetprice");


    getBillCardPanel().setBodyValueAt(getBillCardPanel().getBodyValueAt(row, "nquotetaxprice"), row, "nsubquotetaxprice");

    getBillCardPanel().setBodyValueAt(getBillCardPanel().getBodyValueAt(row, "noriginalcursummny"), row, "nsubsummny");

    getBillCardPanel().setBodyValueAt(getBillCardPanel().getBodyValueAt(row, "nsummny"), row, "nsubcursummny");

    getBillCardPanel().setBodyValueAt(getBillCardPanel().getBodyValueAt(row, "ntaxnetprice"), row, "nsubtaxnetprice");

  }

  public void afterNumberEdit(BillEditEvent e)
  {
    String cunitid = (String)getBillCardPanel().getBodyValueAt(e.getRow(), "cunitid");

    String cpackunitid = (String)getBillCardPanel().getBodyValueAt(e.getRow(), "cpackunitid");

    if ((e.getKey().equals("cpackunitname")) && ((cpackunitid == null) || (cpackunitid.length() == 0)))
    {
      getBillCardPanel().setBodyValueAt(null, e.getRow(), "npacknumber");
      return;
    }

    if (e.getKey().equals("nnumber")) {
      int row = e.getRow();
      getBillCardPanel().setBodyValueAt(getBillCardPanel().getBodyValueAt(row, "nnumber"), row, "nquotenumber");

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

    dbTemp = computeViaPriceTax(e.getRow());
    if (dbTemp != null) {
      getBillCardPanel().getBillModel().setValueAt(dbTemp, e.getRow(), "norgviapricetax");
    }

    getBillCardPanel().setHeadItem("ntotalsummny", calcurateTotal("noriginalcursummny"));

    getBillCardPanel().execHeadFormula("nnetmny->ntotalsummny-nstrikemny");
    getBillCardPanel().setBodyValueAt(getBillCardPanel().getBodyValueAt(e.getRow(), "noriginalcursummny"), e.getRow(), "nsubsummny");

    getBillCardPanel().setBodyValueAt(getBillCardPanel().getBodyValueAt(e.getRow(), "nsummny"), e.getRow(), "nsubcursummny");
  }

  public void setUnits(int istarrow, int count)
  {
    String cunitid = null;
    String cpackunitid = null;
    String cquoteunitid = null;
    UFDouble uf1 = new UFDouble(1);
    ArrayList rowlist = new ArrayList();
    ArrayList rowlist_qt = new ArrayList();

    int i = istarrow; for (int loop = istarrow + count; i < loop; i++) {
      cunitid = (String)getBillCardPanel().getBodyValueAt(i, "cunitid");
      cpackunitid = (String)getBillCardPanel().getBodyValueAt(i, "cpackunitid");

      cquoteunitid = (String)getBillCardPanel().getBodyValueAt(i, "cquoteunitid");

      if ((cunitid != null) && (cpackunitid != null)) {
        if (cpackunitid.equals(cunitid))
        {
          getBillCardPanel().setBodyValueAt(getBillCardPanel().getBodyValueAt(i, "cunitname"), i, "cpackunitname");

          getBillCardPanel().setBodyValueAt(uf1, i, "scalefactor");

          getBillCardPanel().setBodyValueAt("Y", i, "fixedflag");
        }
        else
        {
          rowlist.add(new Integer(i));
        }
      }
      else {
        getBillCardPanel().setBodyValueAt(null, i, "cpackunitid");
        getBillCardPanel().setBodyValueAt(null, i, "cpackunitname");
      }

      if ((cunitid != null) && (cquoteunitid != null)) {
        if (cquoteunitid.equals(cunitid))
        {
          getBillCardPanel().setBodyValueAt(getBillCardPanel().getBodyValueAt(i, "cunitname"), i, "cquoteunitname");

          getBillCardPanel().setBodyValueAt(uf1, i, "nqtscalefactor");

          getBillCardPanel().setBodyValueAt("Y", i, "bqtfixedflag");
        }
        else {
          rowlist_qt.add(new Integer(i));
        }
      } else if ((cunitid != null) && (cquoteunitid == null))
      {
        getBillCardPanel().setBodyValueAt(cunitid, i, "cquoteunitid");
        getBillCardPanel().setBodyValueAt(getBillCardPanel().getBodyValueAt(i, "cunitname"), i, "cquoteunit");

        getBillCardPanel().setBodyValueAt(uf1, i, "nqtscalefactor");
        getBillCardPanel().setBodyValueAt("Y", i, "bqtfixedflag");
      }
      else {
        getBillCardPanel().setBodyValueAt(null, i, "cquoteunitid");
        getBillCardPanel().setBodyValueAt(null, i, "cquoteunit");

        getBillCardPanel().setBodyValueAt(null, i, "nqtscalefactor");
        getBillCardPanel().setBodyValueAt(null, i, "bqtfixedflag");
      }
    }

    if (rowlist.size() > 0) {
      String[] formulas = new String[3];

      formulas[0] = "cpackunitname->getColValue(bd_measdoc,measname,pk_measdoc,cpackunitid)";

      formulas[1] = "scalefactor->getColValue2(bd_convert,mainmeasrate,pk_invbasdoc,cinvbasdocid,pk_measdoc,cpackunitid)";

      formulas[2] = "fixedflag->getColValue2(bd_convert,fixedflag,pk_invbasdoc,cinvbasdocid,pk_measdoc,cpackunitid)";
      execBodyFormulas(formulas, rowlist);
    }

    if (rowlist_qt.size() > 0) {
      String[] formulas = new String[3];

      formulas[0] = "cquoteunitname->getColValue(bd_measdoc,measname,pk_measdoc,cquoteunitid)";

      formulas[1] = "nqtscalefactor->getColValue2(bd_convert,mainmeasrate,pk_invbasdoc,cinvbasdocid,pk_measdoc,cquoteunitid)";

      formulas[2] = "bqtfixedflag->getColValue2(bd_convert,fixedflag,pk_invbasdoc,cinvbasdocid,pk_measdoc,cquoteunitid)";
      execBodyFormulas(formulas, rowlist_qt);
    }
  }

  public void afterUnitChange(int eRow)
  {
    String cunitid = (String)getBillCardPanel().getBodyValueAt(eRow, "cunitid");

    String cpackunitid = (String)getBillCardPanel().getBodyValueAt(eRow, "cpackunitid");

    if ((cunitid != null) && (cpackunitid != null)) {
      if (cpackunitid.equals(cunitid)) {
        String[] formulas = new String[3];

        formulas[0] = "cpackunitname->getColValue(bd_measdoc,measname,pk_measdoc,cpackunitid)";

        formulas[1] = "scalefactor->1";

        formulas[2] = "fixedflag->\"Y\"";
        getBillCardPanel().execBodyFormulas(eRow, formulas);
      }
      else
      {
        String[] formulas = new String[3];

        formulas[0] = "cpackunitname->getColValue(bd_measdoc,measname,pk_measdoc,cpackunitid)";

        formulas[1] = "scalefactor->getColValue2(bd_convert,mainmeasrate,pk_invbasdoc,cinvbasdocid,pk_measdoc,cpackunitid)";

        formulas[2] = "fixedflag->getColValue2(bd_convert,fixedflag,pk_invbasdoc,cinvbasdocid,pk_measdoc,cpackunitid)";
        getBillCardPanel().execBodyFormulas(eRow, formulas);
      }
    } else {
      getBillCardPanel().setBodyValueAt(null, eRow, "cpackunitid");
      getBillCardPanel().setBodyValueAt(null, eRow, "cpackunitname");
    }

    String cquoteunitid = (String)getBillCardPanel().getBodyValueAt(eRow, "cquoteunitid");

    if ((cunitid != null) && (cquoteunitid != null)) {
      if (cquoteunitid.equals(cunitid)) {
        String[] formulas = new String[3];

        formulas[0] = "cquoteunitname->getColValue(bd_measdoc,measname,pk_measdoc,cquoteunitid)";

        formulas[1] = "nqtscalefactor->1";

        formulas[2] = "bqtfixedflag->\"Y\"";
        getBillCardPanel().execBodyFormulas(eRow, formulas);
      } else {
        String[] formulas = new String[3];

        formulas[0] = "cquoteunitname->getColValue(bd_measdoc,measname,pk_measdoc,cquoteunitid)";

        formulas[1] = "nqtscalefactor->getColValue2(bd_convert,mainmeasrate,pk_invbasdoc,cinvbasdocid,pk_measdoc,cquoteunitid)";

        formulas[2] = "bqtfixedflag->getColValue2(bd_convert,fixedflag,pk_invbasdoc,cinvbasdocid,pk_measdoc,cquoteunitid)";
        getBillCardPanel().execBodyFormulas(eRow, formulas);
      }
    } else if ((cunitid != null) && (cquoteunitid == null))
    {
      getBillCardPanel().setBodyValueAt(cunitid, eRow, "cquoteunitid");
      getBillCardPanel().setBodyValueAt(getBillCardPanel().getBodyValueAt(eRow, "cunitname"), eRow, "cquoteunit");

      getBillCardPanel().setBodyValueAt(new UFDouble(1.0D), eRow, "nqtscalefactor");

      getBillCardPanel().setBodyValueAt(new UFBoolean(true), eRow, "bqtfixedflag");
    }
    else
    {
      getBillCardPanel().setBodyValueAt(null, eRow, "cquoteunitid");
      getBillCardPanel().setBodyValueAt(null, eRow, "cquoteunit");

      getBillCardPanel().setBodyValueAt(null, eRow, "nqtscalefactor");
      getBillCardPanel().setBodyValueAt(null, eRow, "bqtfixedflag");
    }
  }

  public void afterUnitEdit(BillEditEvent e)
  {
    String[] formulas = new String[6];

    formulas[0] = "cpackunitname->getColValue(bd_measdoc,measname,pk_measdoc,cpackunitid)";

    formulas[1] = "scalefactor->getColValue2(bd_convert,mainmeasrate,pk_invbasdoc,cinvbasdocid,pk_measdoc,cpackunitid)";

    formulas[2] = "fixedflag->getColValue2(bd_convert,fixedflag,pk_invbasdoc,cinvbasdocid,pk_measdoc,cpackunitid)";

    formulas[3] = "cquoteunitname->getColValue(bd_measdoc,measname,pk_measdoc,cquoteunitid)";

    formulas[4] = "nqtscalefactor->getColValue2(bd_convert,mainmeasrate,pk_invbasdoc,cinvbasdocid,pk_measdoc,cquoteunitid)";

    formulas[5] = "bqtfixedflag->getColValue2(bd_convert,fixedflag,pk_invbasdoc,cinvbasdocid,pk_measdoc,cquoteunitid)";

    getBillCardPanel().execBodyFormulas(e.getRow(), formulas);
  }

  public boolean beforeEdit(BillEditEvent e)
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
      return true;
    }

    if (e.getKey().equals("cpackunitname")) {
      UIRefPane cpackunitname = (UIRefPane)getBillCardPanel().getBodyItem("cpackunitname").getComponent();

      String cinvbasdocid = (String)getBillCardPanel().getBodyValueAt(e.getRow(), "cinvbasdocid");

      String cpackunitid = (String)getBillCardPanel().getBodyValueAt(e.getRow(), "cpackunitid");

      String cunitid = (String)getBillCardPanel().getBodyValueAt(e.getRow(), "cunitid");

      cpackunitname.setWhereString(" (pk_measdoc in (select pk_measdoc from bd_convert where pk_invbasdoc = '" + cinvbasdocid + "') or pk_measdoc='" + cunitid + "' ) ");

      return true;
    }

    if (e.getKey().equals("vfree0")) {
      setInvFree(e);
      try
      {
        InvVO voInv = (InvVO)this.alInvs.get(e.getRow());

        setBodyFreeValue(e.getRow(), voInv);
        getFreeItemRefPane().setFreeItemParam(voInv);
      } catch (Exception ex) {
        SCMEnv.out("自由项设置失败!");
        ex.printStackTrace();
      }
      return true;
    }

    if (e.getKey().equals("cbodywarehousename")) {
      return beforeCbodywarehouseidEdit(e);
    }
    return true;
  }

  public void bodyRowChange(BillEditEvent e)
  {
    int i;
    if (this.strState.equals("LIST")) {
      int selectRow = e.getRow();
      SaleinvoiceVO tempVO = (SaleinvoiceVO)this.alBills.get(selectRow);
      SaleinvoiceBVO tempFirstBVO = (SaleinvoiceBVO)tempVO.getChildrenVO()[0];

      String receipttype = tempFirstBVO.getCupreceipttype();
      if (receipttype.equals("4C")) {
        String cfirstbilltype = tempFirstBVO.getCreceipttype();
        if (tempFirstBVO.getNoriginalcurprice() == null) {
          try {
            tempVO = getDataFrom4CTo32(tempVO);
          } catch (Exception ex) {
            SCMEnv.out("填充销售出库单生成的发票VO数据时出错！");
            handleException(ex);
          }

        }

      }

      getBillListPanel().setBodyValueVO((SaleinvoiceBVO[])(SaleinvoiceBVO[])tempVO.getChildrenVO());

      getBillListPanel().getBodyBillModel().execLoadFormula();

      for (i = 0; i < getBillListPanel().getBodyBillModel().getRowCount(); )
      {
        UFDouble dSubsummny = (UFDouble)getBillListPanel().getBodyBillModel().getValueAt(i, "nsubsummny");

        Object tOrgsummny = getBillListPanel().getBodyBillModel().getValueAt(i, "noriginalcursummny");

        UFDouble nuniteinvoicemny = (dSubsummny == null ? new UFDouble(0) : dSubsummny).sub(tOrgsummny == null ? new UFDouble(0) : (UFDouble)tOrgsummny);

        i++;
      }

    }
    else
    {
      int iStatus = Integer.parseInt(getBillCardPanel().getHeadItem("fstatus").getValue());

      if (e.getRow() > -1) {
        Object cfreezeid = getBillCardPanel().getBodyValueAt(e.getRow(), "cfreezeid");

        if ((cfreezeid != null) && (cfreezeid.toString().trim().length() != 0))
        {
          lockRow(e.getRow());
        }
      }
    }
  }

  private void calculateNumber(int row, String key)
  {
    nc.ui.so.so001.panel.bom.BillTools.calcEditFun(getBillCardPanel().getHeadItem("dbilldate").getValue(), this.BD505, this.SA_02.booleanValue(), key, row, getBillCardPanel());
  }

  protected Object getAssistantPara(ButtonObject bo)
    throws ValidationException
  {
    Object o = null;

    if ((bo.getTag().equals("OrderAlterRpt")) || (bo.getTag().equals("OrderExecRpt")))
    {
      o = getBillCardPanel().getHeadItem("csaleid").getValue();
    }

    if (bo.getTag().equals("ATP")) {
      o = getVO(true);
    }

    if (bo.getTag().equals("CustInfo")) {
      o = getBillCardPanel().getHeadItem("creceiptcorpid").getValue();
    }

    if (bo.getTag().equals("CustCredited")) {
      String sCust = getCCustomerid();
      String sBiztype = getBillCardPanel().getHeadItem("cbiztype").getValue();

      String cproductid = null;
      if ((this.SO_27 != null) && (this.SO_27.booleanValue()) && (getBillCardPanel().getRowCount() > 0))
      {
        cproductid = (String)getBillCardPanel().getBodyValueAt(0, "cprolineid");
      }
      CustCreditVO voCredit = new CustCreditVO();
      voCredit.setPk_cumandoc(sCust);
      voCredit.setCbiztype(sBiztype);
      voCredit.setCproductline(cproductid);
      o = voCredit;
    }

    if (bo.getTag().equals("Prifit")) {
      ProfitHeaderVO headVO = new ProfitHeaderVO();

      headVO.setPkcorp(getCorpPrimaryKey());

      headVO.setCcalbodyid(getBillCardPanel().getHeadItem("ccalbodyid").getValue());

      UIRefPane ccalbodyid = (UIRefPane)getBillCardPanel().getHeadItem("ccalbodyid").getComponent();

      headVO.setCcalbodyname(ccalbodyid.getRefName());

      headVO.setBilltype(getBillCardPanel().getBusiType());

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

        Object oTmp = getBillCardPanel().getBodyValueAt(i, "nnumber");
        if ((oTmp != null) && (oTmp.toString().length() != 0))
        {
          bodyVO.setNumber((UFDouble)getBillCardPanel().getBodyValueAt(i, "nnumber"));
        }

        oTmp = getBillCardPanel().getBodyValueAt(i, "nnetprice");
        if ((oTmp != null) && (oTmp.toString().length() != 0))
        {
          bodyVO.setNnetprice((UFDouble)getBillCardPanel().getBodyValueAt(i, "nnetprice"));
        }

        bodyVO.setCbodycalbodyid((String)getBillCardPanel().getBodyValueAt(i, "cadvisecalbodyid"));

        bodyVO.setCbodycalbodyname((String)getBillCardPanel().getBodyValueAt(i, "cadvisecalbodyname"));

        bodyVO.setCbodywarehouseid((String)getBillCardPanel().getBodyValueAt(i, "cbodywarehouseid"));

        bodyVO.setCbodywarehousename((String)getBillCardPanel().getBodyValueAt(i, "cbodywarehousename"));

        if ((getBillCardPanel().getBodyValueAt(i, "blargessflag") != null) && (getBillCardPanel().getBodyValueAt(i, "blargessflag").toString().equals("false")))
          bodyVO.m_blargessflag = new UFBoolean(false);
        else
          bodyVO.m_blargessflag = new UFBoolean(true);
        bodyVO.setNmny((UFDouble)getBillCardPanel().getBodyValueAt(i, "nmny"));

        bodyVOs[i] = bodyVO;
      }
      ProfitVO profit = new ProfitVO();
      profit.setParentVO(headVO);
      profit.setChildrenVO(bodyVOs);

      o = profit;
    }
    return o;
  }

  public BillCardPanel getBillCardPanel()
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

  private SaleInvoiceListPanel getBillListPanel()
  {
    if (this.blpAdd == null) {
      try {
        this.blpAdd = new SaleInvoiceListPanel(true);
        this.blpAdd.setName("ListPanelNew");
        this.blpAdd.addHeadEditListener(this);
        this.blpAdd.setBillType("32");
        this.blpAdd.setCorp(getCorpPrimaryKey());
        this.blpAdd.setOperator(getClientEnvironment().getUser().getPrimaryKey());

        this.blpAdd.updateUI();
        this.blpAdd.setBD302(this.BD302);
        this.blpAdd.getHeadTable().setSortEnabled(false);
      }
      catch (Throwable ivjExc) {
        handleException(ivjExc);
      }
    }
    return this.blpAdd;
  }

  public void loadListTemplet()
  {
    showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000135"));

    BillListData bd = null;
    this.billtempletVO = null;
    if (this.SO_20.booleanValue())
    {
      if (this.billtempletVO == null) {
        this.billtempletVO = getBillListPanel().getDefaultTemplet(getNodeCode(), null, getClientEnvironment().getUser().getPrimaryKey(), getCorpPrimaryKey());
      }

      bd = new BillListData(this.billtempletVO);
    }
    else {
      if (this.billtempletVO == null) {
        this.billtempletVO = getBillListPanel().getDefaultTemplet(getNodeCode(), this.strBusitype, getClientEnvironment().getUser().getPrimaryKey(), getCorpPrimaryKey());
      }

      bd = new BillListData(this.billtempletVO);
    }

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

  public void getSysPara()
  {
    try
    {
      String[] para = { "SA08", "SA15", "SO27", "SO22", "SO06", "BD501", "BD502", "BD503", "BD505", "BD302", "SO30", "SA02", "SA13", "SO34", "SO36", "SO49", "SO59", "SO60" };

      Hashtable h = this.hsparas;

      String svalue = null;

      svalue = (String)h.get("SA08");
      if ((svalue == null) || (svalue.trim().length() <= 0))
        this.SA_08 = new UFBoolean(false);
      else {
        this.SA_08 = new UFBoolean(svalue.trim());
      }
      svalue = (String)h.get("SA15");
      if ((svalue == null) || (svalue.trim().length() <= 0))
        this.SA_15 = new UFBoolean(false);
      else {
        this.SA_15 = new UFBoolean(svalue.trim());
      }
      svalue = (String)h.get("SO27");
      if ((svalue == null) || (svalue.trim().length() <= 0))
        this.SO_27 = new UFBoolean(false);
      else {
        this.SO_27 = new UFBoolean(svalue.trim());
      }
      svalue = (String)h.get("SO22");
      if ((svalue == null) || (svalue.trim().length() <= 0))
        this.SO_22 = new UFDouble(0.0D);
      else {
        this.SO_22 = new UFDouble(svalue.trim());
      }
      this.SO_06 = ((String)h.get("SO06"));

      svalue = (String)h.get("BD501");
      if ((svalue == null) || (svalue.trim().length() <= 0))
        this.BD501 = new Integer(2);
      else {
        this.BD501 = new Integer(svalue.trim());
      }
      svalue = (String)h.get("BD502");
      if ((svalue == null) || (svalue.trim().length() <= 0))
        this.BD502 = new Integer(2);
      else {
        this.BD502 = new Integer(svalue.trim());
      }
      svalue = (String)h.get("BD503");
      if ((svalue == null) || (svalue.trim().length() <= 0))
        this.BD503 = new Integer(2);
      else {
        this.BD503 = new Integer(svalue.trim());
      }
      svalue = (String)h.get("BD505");
      if ((svalue == null) || (svalue.trim().length() <= 0))
        this.BD505 = new Integer(2);
      else {
        this.BD505 = new Integer(svalue.trim());
      }
      svalue = (String)h.get("BD302");
      if ((svalue == null) || (svalue.trim().length() <= 0))
        this.BD302 = new UFBoolean(false);
      else {
        this.BD302 = new UFBoolean(svalue.trim());
      }
      svalue = (String)h.get("SO20");
      if ((svalue == null) || (svalue.trim().length() <= 0))
        this.SO_20 = new UFBoolean(false);
      else {
        this.SO_20 = new UFBoolean(svalue.trim());
      }
      svalue = (String)h.get("SO30");
      if ((svalue == null) || (svalue.trim().length() <= 0))
        this.SO_30 = new UFBoolean(false);
      else
        this.SO_30 = new UFBoolean(svalue.trim());
      svalue = (String)h.get("SO60");
      if ((svalue == null) || (svalue.trim().length() <= 0))
        this.SO60 = new UFBoolean(true);
      else {
        this.SO60 = new UFBoolean(svalue.trim());
      }

      svalue = (String)h.get("SA02");
      if ((svalue == null) || (svalue.trim().length() <= 0))
        this.SA_02 = new UFBoolean(false);
      else {
        this.SA_02 = new UFBoolean(svalue.trim());
      }
      String SA13 = (String)h.get("SA13");
      if ("集团定价".equals(SA13))
      {
        svalue = (String)h.get("SA09");
        if ((svalue == null) || (svalue.trim().length() <= 0))
          this.SA_02 = new UFBoolean(false);
        else {
          this.SA_02 = new UFBoolean(svalue.trim());
        }

      }

      svalue = (String)h.get("SO34");
      if ((svalue == null) || (svalue.trim().length() <= 0))
        this.SO34 = new UFDouble(0.0D);
      else {
        this.SO34 = new UFDouble(svalue.trim());
      }
      String s36 = (String)h.get("SO36");
      if ((s36 == null) || (s36.trim().length() == 0))
        this.SO36 = new UFBoolean(false);
      else {
        this.SO36 = new UFBoolean(s36.trim());
      }
      String s49 = (String)h.get("SO49");
      if ((s49 == null) || (s49.trim().length() == 0))
        this.SO49 = new UFBoolean(false);
      else
        this.SO49 = new UFBoolean(s49.trim());
      String s59 = (String)h.get("SO59");
      if ((s59 == null) || (s59.trim().length() == 0))
        this.SO59 = new UFBoolean(false);
      else
        this.SO59 = new UFBoolean(s59.trim());
    }
    catch (Exception e) {
      SCMEnv.out(e.getMessage());
    }
  }

  public String getTitle()
  {
    return getBillCardPanel().getBillData().getTitle();
  }

  public AggregatedValueObject getVO()
  {
    SaleinvoiceVO saleinvoice = null;
    saleinvoice = (SaleinvoiceVO)getBillCardPanel().getBillValueVO(SaleinvoiceVO.class.getName(), SaleVO.class.getName(), SaleinvoiceBVO.class.getName());

    ((SaleVO)saleinvoice.getParentVO()).setCreceipttype("32");

    ((SaleVO)saleinvoice.getParentVO()).setCapproveid(getClientEnvironment().getUser().getPrimaryKey());

    return saleinvoice;
  }

  private void handleException(Throwable exception)
  {
    SCMEnv.out("--------- 未捕捉到的异常 ---------");
    exception.printStackTrace(System.out);
  }

  public void initButtons()
  {
    this.m_boAdd = this.bt.getButton("增加");
    this.m_boModify = this.bt.getButton("修改");

    this.m_bobill = this.bt.getButton("维护");

    this.m_boSave = this.bt.getButton("保存");

    this.m_boCancel = this.bt.getButton("取消");

    this.m_boLineOper = this.bt.getButton("行操作");

    this.m_boAddLine = this.bt.getButton("增行");

    this.m_boDelLine = this.bt.getButton("删行");

    this.m_boCheck = this.bt.getButton("审核");

    this.m_boUnAprove = this.bt.getButton("弃审");

    this.m_boBlankOut = this.bt.getButton("删除");

    m_boBillCombin = this.bt.getButton("合并显示");

    this.m_boPrint = this.bt.getButton("打印");

    this.m_boBusiType = this.bt.getButton("业务类型");

    this.m_boAction = this.bt.getButton("执行");

    this.m_boAssistant = this.bt.getButton("辅助功能");

    this.m_boAtp = this.bt.getButton("可用量");

    this.m_boCustInfo = this.bt.getButton("客户信息");

    this.m_boCustCredit = this.bt.getButton("客户信用");

    this.m_boPrifit = this.bt.getButton("毛利预估");

    this.m_boSoTax = this.bt.getButton("传金税");

    this.boAuditFlowStatus = this.bt.getButton("审批流状态");

    this.m_boUniteCancel = this.bt.getButton("放弃合并");

    this.m_boUnite = this.bt.getButton("合并开票");

    this.m_boDocument = this.bt.getButton("文档管理");

    this.m_boGather = this.bt.getButton("出库汇总开票");

    this.m_boPreview = this.bt.getButton("预览");

    this.boOrderQuery = this.bt.getButton("联查");

    this.boSendAudit = this.bt.getButton("送审");

    this.aryButtonGroup = this.bt.getButtonArray();

    this.aryButtonGroupList = new ButtonObject[] { this.m_bobill };

    PfUtilClient.retBusinessBtn(this.m_boBusiType, getCorpPrimaryKey(), "32");

    if ((this.m_boBusiType.getChildButtonGroup() != null) && (this.m_boBusiType.getChildButtonGroup().length > 0))
    {
      ButtonObject[] bo = this.m_boBusiType.getChildButtonGroup();
      for (int i = 0; i < bo.length; i++) {
        bo[i].setName(bo[i].getName());
      }
      this.m_boBusiType.setChildButtonGroup(bo);
      this.m_boBusiType.getChildButtonGroup()[0].setSelected(true);
      this.m_boBusiType.getChildButtonGroup()[0].setSelected(true);
      this.strBusitype = this.m_boBusiType.getChildButtonGroup()[0].getTag();
      getBillCardPanel().setBusiType(this.strBusitype);
      this.m_boBusiType.setCheckboxGroup(true);

      PfUtilClient.retAddBtn(this.m_boAdd, getCorpPrimaryKey(), "32", this.m_boBusiType.getChildButtonGroup()[0]);

      bo = this.m_boAdd.getChildButtonGroup();
      for (int i = 0; i < bo.length; i++) {
        bo[i].setName(bo[i].getName());
      }
      this.m_boAdd.setChildButtonGroup(bo);

      onBusiType(this.m_boBusiType.getChildButtonGroup()[0]);
    } else {
      SCMEnv.out("没有设置业务类型！");
    }

    this.m_boAtp.setTag("ATP");
    this.m_boCustInfo.setTag("CustInfo");
    this.m_boCustCredit.setTag("CustCredited");
    this.m_boPrifit.setTag("Prifit");
    this.m_boSoTax.setTag("SoTax");

    this.aryButtonGroup = ArrayMethod.AppendToLast_BO(this.aryButtonGroup, getExtendBtns());

    setButtons(this.aryButtonGroup);

    this.boAuditFlowStatus.setVisible(false);
    this.strState = "BUSITYPE";
    setButtonsState();
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
          if ((oTemp != null) && (oTemp.toString().trim().length() > 0)) {
            getBillCardPanel().getBillModel().setValueAt(oTemp, i, "vfree0");
          }
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
      add(getBillCardPanel(), "Center");
      this.bt = new ButtonTree("40060501");
    } catch (Throwable ivjExc) {
      handleException(ivjExc);
    }

    this.ce = getClientEnvironment();

    this.user = this.ce.getUser().getPrimaryKey();
    this.pk_corp = getCorpPrimaryKey();
    initButtons();
    this.getButtonObjectByCode("签呈抵扣").setEnabled(false);

    initCurrency();

    initVars(this.pk_corp);

    getSysPara();
    loadTemplet();
    loadListTemplet();
    initState();
    getInitBillItemEidtState();
  }

  public void initState()
  {
    setButtonsState();
  }
  

  public void loadData()
  {
    try
    {
      SaleinvoiceBVO[] saleinvoiceBs = SaleinvoiceBO_Client.queryBodyData(this.m_strID);
      getBillCardPanel().getBillModel().setBodyDataVO(saleinvoiceBs);
      long s1 = System.currentTimeMillis();
      getBillCardPanel().getBillModel().execLoadFormula();
      SCMEnv.out("执行公式[共用时" + (System.currentTimeMillis() - s1) + "]");

      if (getBillCardPanel().getRowCount() != 0) {
        for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
          setAssistChange(i);
          beforeUnitChange(i);
          afterUnitChange(i);

          UFDouble dbTemp = computeViaPrice(i);
          if (dbTemp != null) {
            getBillCardPanel().getBillModel().setValueAt(dbTemp, i, "norgviaprice");
          }

          dbTemp = computeViaPriceTax(i);
          if (dbTemp != null) {
            getBillCardPanel().getBillModel().setValueAt(dbTemp, i, "norgviapricetax");
          }
        }
      }

      countCardUniteMny();
      initFreeItem(saleinvoiceBs);
      getBillCardPanel().updateValue();
      getBillCardPanel().getBillModel().reCalcurateAll();
      SCMEnv.out("数据加载成功！");
    }
    catch (ValidationException e) {
      showErrorMessage(e.getMessage());
    } catch (Exception e) {
      showErrorMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000256"));

      SCMEnv.out(e.getMessage());
    }
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

    bd = new BillData(getBillCardPanel().getTempletData(getNodeCode(), null, getClientEnvironment().getUser().getPrimaryKey(), getCorpPrimaryKey()));

    getFreeItemRefPane().setMaxLength(1000);

    bd.getBodyItem("vfree0").setComponent(getFreeItemRefPane());

    setCardPanel(bd);

    getBillCardPanel().setBillData(bd);

    ((UIRefPane)getBillCardPanel().getHeadItem("cdispatcherid").getComponent()).getRefModel().addWherePart(" and rdflag = 1 ");

    DefSetTool.updateBillCardPanelUserDef(getBillCardPanel(), getClientEnvironment().getCorporation().getPk_corp(), "32", "vdef", "vdef");

    getBillCardPanel().addBodyTotalListener(this);

    initFormulaParse();

    setInputLimit();
    setHeadComboBox();
    setBodyComboBox();
    getBillCardPanel().hideBodyTableCol("ntotalpaymny");

    BillRowNo.loadRowNoItem(getBillCardPanel(), getRowNoItemKey());

    getBillCardPanel().setTatolRowShow(true);

    showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000176"));
  }

  public void onAdd()
  {
    this.m_strID = null;

    getBillCardPanel().addNew();
    getBillCardPanel().setEnabled(true);
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

  public void onAddLine()
  {
    showHintMessage(NCLangRes.getInstance().getStrByID("SCMCommon", "UPPSCMCommon-000465"));

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

    if (this.isGather)
      getBillCardPanel().getBodyItem("cinventorycode").setRefType("存货档案");
    else {
      getBillCardPanel().getBodyItem("cinventorycode").setRefType("<nc.ui.so.so002.DisInvRefModel>");
    }

    getBillCardPanel().setBodyValueAt(this.SO34, rowCount - 1, "nnumber");
    getBillCardPanel().setBodyValueAt(this.SO34, rowCount - 1, "nquotenumber");

    this.alInvs.add(null);

    BillRowNo.addLineRowNo(getBillCardPanel(), getBillCode(), getRowNoItemKey());
  }

  public void onApprove()
  {
    try
    {
      SaleinvoiceVO saleinvoice = (SaleinvoiceVO)getBillCardPanel().getBillValueVO(SaleinvoiceVO.class.getName(), SaleVO.class.getName(), SaleinvoiceBVO.class.getName());

      if (!this.bInMsgPanel) {
        saleinvoice.setPrimaryKey(this.m_strID);

        ((SaleVO)saleinvoice.getParentVO()).setCreceipttype("32");

        ((SaleVO)saleinvoice.getParentVO()).setCbiztype(this.strBusitype);

        ((SaleVO)saleinvoice.getParentVO()).setPk_corp(getCorpPrimaryKey());

        if (saleinvoice.getChildrenVO() != null) {
          for (int i = 0; i < saleinvoice.getChildrenVO().length; i++)
          {
            ((SaleinvoiceBVO)saleinvoice.getChildrenVO()[i]).setPk_corp(getCorpPrimaryKey());
          }

        }

        onApproveCheckWorkflow(saleinvoice);

        PfUtilClient.processActionFlow(this, "APPROVE", "32", getClientEnvironment().getDate().toString(), saleinvoice, null);
      }
      else
      {
        ((SaleVO)saleinvoice.getParentVO()).setCapproveid(getClientEnvironment().getUser().getPrimaryKey());

        onApproveCheckWorkflow(saleinvoice);
        PfUtilClient.processActionFlow(this, "APPROVE", "32", getClientEnvironment().getDate().toString(), saleinvoice, null);
      }

      if (PfUtilClient.isSuccess())
      {
        getBillCardPanel().setTailItem("capproveid", getClientEnvironment().getUser().getPrimaryKey());

        getBillCardPanel().setTailItem("dapprovedate", getClientEnvironment().getDate().toString());
        
        this.m_strID = saleinvoice.getPrimaryKey();
        loadData();

        this.strState = "APPROVE";
        setButtonsState();
        showHintMessage(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000123"));

        reLoadTS();
      }
      else {
        showHintMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000254"));
      }
    } catch (BusinessException e1) {
      showErrorMessage(e1.getMessage());
      e1.printStackTrace(System.out);
    } catch (Exception e) {
      showErrorMessage(e.getMessage());
      SCMEnv.out(e.getMessage());
    }
  }

  public void onUnAprove()
  {
    try
    {
      SaleinvoiceVO saleinvoice = (SaleinvoiceVO)getBillCardPanel().getBillValueVO(SaleinvoiceVO.class.getName(), SaleVO.class.getName(), SaleinvoiceBVO.class.getName());
      PfUtilClient.processActionFlow(this, "SoUnApprove", "32", getClientEnvironment().getDate().toString(), saleinvoice, null);
    } catch (BusinessException e1) {
      showErrorMessage(e1.getMessage());
      e1.printStackTrace(System.out);
    } catch (Exception e) {
      showErrorMessage(e.getMessage());
      SCMEnv.out(e.getMessage());
    }
  }

  public void onBlankOut()
  {
    try
    {
      SaleinvoiceVO saleinvoice = (SaleinvoiceVO)getBillCardPanel().getBillValueVO(SaleinvoiceVO.class.getName(), SaleVO.class.getName(), SaleinvoiceBVO.class.getName());
      ((SaleVO)saleinvoice.getParentVO()).setCbiztype(this.strBusitype);
      PfUtilClient.processActionFlow(this, "SoBlankOut", "32", getClientEnvironment().getDate().toString(), saleinvoice, null);
    } catch (BusinessException e1) {
      showErrorMessage(e1.getMessage());
      e1.printStackTrace(System.out);
    } catch (Exception e) {
      showErrorMessage(e.getMessage());
      SCMEnv.out(e.getMessage());
    }
  }

  public void onATP()
  {
    try
    {
      SaleinvoiceVO saleinvoice = (SaleinvoiceVO)getBillCardPanel().getBillValueVO(SaleinvoiceVO.class.getName(), SaleVO.class.getName(), SaleinvoiceBVO.class.getName());
      PfUtilClient.processActionFlow(this, "ATP", "32", getClientEnvironment().getDate().toString(), saleinvoice, null);
    } catch (BusinessException e1) {
      showErrorMessage(e1.getMessage());
      e1.printStackTrace(System.out);
    } catch (Exception e) {
      showErrorMessage(e.getMessage());
      SCMEnv.out(e.getMessage());
    }
  }

  public void onCustInfo()
  {
    try
    {
      SaleinvoiceVO saleinvoice = (SaleinvoiceVO)getBillCardPanel().getBillValueVO(SaleinvoiceVO.class.getName(), SaleVO.class.getName(), SaleinvoiceBVO.class.getName());
      PfUtilClient.processActionFlow(this, "CustInfo", "32", getClientEnvironment().getDate().toString(), saleinvoice, null);
    } catch (BusinessException e1) {
      showErrorMessage(e1.getMessage());
      e1.printStackTrace(System.out);
    } catch (Exception e) {
      showErrorMessage(e.getMessage());
      SCMEnv.out(e.getMessage());
    }
  }

  public void onCustCredit()
  {
    try
    {
      SaleinvoiceVO saleinvoice = (SaleinvoiceVO)getBillCardPanel().getBillValueVO(SaleinvoiceVO.class.getName(), SaleVO.class.getName(), SaleinvoiceBVO.class.getName());
      PfUtilClient.processActionFlow(this, "CustCredited", "32", getClientEnvironment().getDate().toString(), saleinvoice, null);
    } catch (BusinessException e1) {
      showErrorMessage(e1.getMessage());
      e1.printStackTrace(System.out);
    } catch (Exception e) {
      showErrorMessage(e.getMessage());
      SCMEnv.out(e.getMessage());
    }
  }

  public void onPrifit()
  {
    try
    {
      SaleinvoiceVO saleinvoice = (SaleinvoiceVO)getBillCardPanel().getBillValueVO(SaleinvoiceVO.class.getName(), SaleVO.class.getName(), SaleinvoiceBVO.class.getName());
      PfUtilClient.processActionFlow(this, "Prifit", "32", getClientEnvironment().getDate().toString(), saleinvoice, null);
    } catch (BusinessException e1) {
      showErrorMessage(e1.getMessage());
      e1.printStackTrace(System.out);
    } catch (Exception e) {
      showErrorMessage(e.getMessage());
      SCMEnv.out(e.getMessage());
    }
  }

  public void onInvoiceExecRpt()
  {
    try
    {
      SaleinvoiceVO saleinvoice = (SaleinvoiceVO)getBillCardPanel().getBillValueVO(SaleinvoiceVO.class.getName(), SaleVO.class.getName(), SaleinvoiceBVO.class.getName());
      PfUtilClient.processActionFlow(this, "InvoiceExecRpt", "32", getClientEnvironment().getDate().toString(), saleinvoice, null);
    } catch (BusinessException e1) {
      showErrorMessage(e1.getMessage());
      e1.printStackTrace(System.out);
    } catch (Exception e) {
      showErrorMessage(e.getMessage());
      SCMEnv.out(e.getMessage());
    }
  }

  public void onSoTax()
  {
    try
    {
      SaleinvoiceVO saleinvoice = (SaleinvoiceVO)getBillCardPanel().getBillValueVO(SaleinvoiceVO.class.getName(), SaleVO.class.getName(), SaleinvoiceBVO.class.getName());
      PfUtilClient.processActionFlow(this, "SoTax", "32", getClientEnvironment().getDate().toString(), saleinvoice, null);
    } catch (BusinessException e1) {
      showErrorMessage(e1.getMessage());
      e1.printStackTrace(System.out);
    } catch (Exception e) {
      showErrorMessage(e.getMessage());
      SCMEnv.out(e.getMessage());
    }
  }

  public void onAssistant(ButtonObject bo)
  {
    try
    {
      showHintMessage(bo.getHint());
      PfUtilClient.processAction(this, bo.getTag(), "32", getClientEnvironment().getDate().toString(), getVO(), getAssistantPara(bo));
    }
    catch (ValidationException e)
    {
      showErrorMessage(e.getMessage());
    } catch (Exception e) {
      showErrorMessage(bo.getName() + NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000069"));

      SCMEnv.out(e.getMessage());
    }
  }

  public void onBusiType(ButtonObject bo)
  {
    bo.setSelected(true);
    getBillCardPanel().addNew();

    this.m_boBusiType.setTag(bo.getTag());
    this.strBusitype = bo.getTag();
    try {
      this.sVerifyrule = SaleinvoiceBO_Client.getVerifyrule(this.strBusitype);
    } catch (Exception ex) {
      SCMEnv.out(ex.getMessage());
    }
    getBillCardPanel().setBusiType(bo.getTag());

    setButtons(bo);

    this.strState = "BUSITYPE";
    setButtonsState();
  }

  public void onButtonClicked(ButtonObject bo)
  {
    showHintMessage("");
    getBillCardPanel().stopEditing();
    String m_currentbillnode = null;

    if (bo == this.m_boGather) {
      onGather();
      this.isGather = true;
      getBillCardPanel().showBodyTableCol("cupsourcebillcode");
      getBillCardPanel().hideBodyTableCol("csourcebillcode");
      getBillCardPanel().getHeadItem("cdeptid").setEnabled(true);
      getBillCardPanel().getHeadItem("cemployeeid").setEnabled(true);
      initSalePeopleRef();
      initSaleDeptRef();
      return;
    }

    if (bo.getParent() == this.m_boAdd)
    {
      if ((bo.getTag().substring(0, 2).equals("30")) || (bo.getTag().substring(0, 2).equals("3A")))
      {
        getBillCardPanel().hideBodyTableCol("cupsourcebillcode");
      }
      else if ((bo.getTag().substring(0, 2).equals("4C")) || (bo.getTag().substring(0, 2).equals("3Q")))
      {
        getBillCardPanel().showBodyTableCol("cupsourcebillcode");
        getBillCardPanel().hideBodyTableCol("csourcebillcode");
      }
      this.m_strID = null;
      m_currentbillnode = "40060302";

      PfUtilClient.childButtonClicked(bo, this.pk_corp, m_currentbillnode, this.user, "32", this);

      if ((!PfUtilClient.makeFlag) && 
        (PfUtilClient.isCloseOK())) {
        this.saleinvoiceNew = ((SaleinvoiceVO[])(SaleinvoiceVO[])PfUtilClient.getRetVos());

        onNew();   //新增
      }

    }

    if (bo == this.m_boModify)
      onModify();
    if (bo == this.m_boSave)
      onSave();
    if (bo == this.m_boCancel)
      onCancel();
    if (bo == this.m_boDelLine)
      onDelLine();
    if (bo == this.m_boAddLine)
      onAddLine();
    if (bo == this.m_boCheck)
      onApprove();
    if (bo == this.m_boUnAprove)
      onUnAprove();
    if (bo == this.m_boBlankOut) {
      onBlankOut();
    }

    if (bo == this.m_boPrint)
      onPrint(false);
    else if (bo == this.m_boPreview) {
      onPrint(true);
    }
    if (bo == this.m_boDocument)
      onDocument();
    if (bo == this.m_boHelp)
      onHelp();
    if (bo.getParent() == this.m_boBusiType) {
      onBusiType(bo);
    }
    if (bo == this.boOrderQuery) {
      onOrderQuery();
    }

    if ((bo == this.m_boAtp) || (bo == this.m_boCustInfo) || (bo == this.m_boCustCredit) || (bo == this.m_boPrifit)) {
      onAssistant(bo);
    }
    if (bo.getParent() == this.m_boAfterAction)
      onAfterAction(bo);
    if (bo == this.boAuditFlowStatus) {
      onAuditFlowStatus();
    }
    if (bo == m_boBillCombin)
      onBillCombin();
    if (bo == this.boSendAudit)
      onSendAudit();
    if ((bo.getParent() == this.m_boAdd) || (bo == this.m_boModify))
    {
      getBillCardPanel().transferFocusTo(0);
      return;
    }

    if (bo == this.m_boUnite)
    {
      onUnite(bo);
      return;
    }
    if (bo == this.m_boUniteCancel)
      onUniteCancel(bo);
    if (bo == this.m_boReturn) {
      onReturn();
      return;
    }

    onExtendBtnsClick(bo);
  }

  public void onCancel()
  {
    if (this.strState.equals("LIST")) {
      if (getBillListPanel().getHeadTable().getRowCount() != 0)
        if (showYesNoMessage("将放弃所有待保存的发票，是否继续") == 4) {
          this.alBills.clear();
          getBillListPanel().getHeadBillModel().clearBodyData();
          getBillListPanel().getBodyBillModel().clearBodyData();
        } else {
          return;
        }
      remove(getBillListPanel());
      add(getBillCardPanel(), "Center");
      this.strState = "FREE";
      setButtons(this.aryButtonGroup);
    } else {
      if (this.strState.equals("ADD")) {
        this.m_strID = null;
        getBillCardPanel().addNew();
        getBillCardPanel().setEnabled(false);
        this.strState = "FREE";
        this.isGather = false;
      }

      if (this.strState.equals("MODIFY")) {
        getBillCardPanel().resumeValue();
        getBillCardPanel().setEnabled(false);
        this.strState = "SAVE";
      }

      showHintMessage("");

      for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
        getBillCardPanel().setCellEditable(i, "cpackunitname", false);
        getBillCardPanel().setCellEditable(i, "npacknumber", false);
      }

      initRef();

      if (this.oldhsSelectedARSubHVO != null)
        this.hsSelectedARSubHVO = ((Hashtable)this.oldhsSelectedARSubHVO.clone());
    }
    setButtonsState();
    updateUI();
  }

  public boolean beforeCbodywarehouseidEdit(BillEditEvent e)
  {
    String calid = (String)getBillCardPanel().getBodyValueAt(e.getRow(), "cadvisecalbodyid");

    if ((calid != null) && (calid.trim().length() > 0)) {
      UIRefPane wareRef = (UIRefPane)getBillCardPanel().getBodyItem("cbodywarehousename").getComponent();

      if (wareRef == null)
        return true;
      AbstractRefModel m = wareRef.getRefModel();
      if ((m.getWherePart() == null) || (m.getWherePart().trim().length() <= 0))
      {
        m.setWherePart(" pk_calbody ='" + calid.trim() + "' ");
      } else {
        if ((this.warehouseRefWhereSql == null) || (this.warehouseRefWhereSql.trim().length() <= 0))
        {
          this.warehouseRefWhereSql = m.getWherePart();
        }if ((this.warehouseRefWhereSql != null) && (this.warehouseRefWhereSql.trim().length() > 0))
        {
          m.setWherePart(this.warehouseRefWhereSql + " and pk_calbody ='" + calid.trim() + "' ");
        }
        else
          m.setWherePart(" pk_calbody ='" + calid.trim() + "' ");
      }
    }
    return true;
  }

  public void onDelLine()
  {
    showHintMessage(NCLangRes.getInstance().getStrByID("SCMCommon", "UPPSCMCommon-000466"));

    if (getBillCardPanel().getBillModel().getRowCount() == 0) {
      showErrorMessage(NCLangRes.getInstance().getStrByID("SCMCommon", "UPPSCMCommon-000161"));
      return;
    }

    if (getBillCardPanel().getBillTable().getSelectedRowCount() > 0) {
      if (showOkCancelMessage(NCLangRes.getInstance().getStrByID("SCMCommon", "UPPSCMCommon-000180")) != 1) {
        return;
      }

      getBillCardPanel().delLine();
      getBillCardPanel().setHeadItem("ntotalsummny", calcurateTotal("noriginalcursummny"));

      getBillCardPanel().execHeadFormula("nnetmny->ntotalsummny-nstrikemny");
      showHintMessage(NCLangRes.getInstance().getStrByID("SCMCommon", "UPPSCMCommon-000225"));
    }
    else
    {
      showErrorMessage(NCLangRes.getInstance().getStrByID("SCMCommon", "UPPSCMCommon-000169"));
      return;
    }
  }

  public void onHelp()
  {
  }

  public void onMenuItemClick(ActionEvent e)
  {
    UIMenuItem item = (UIMenuItem)e.getSource();
    if (item == getBillCardPanel().getInsertLineMenuItem()) {
      onInsertLine();
    }
    if (item == getBillCardPanel().getAddLineMenuItem()) {
      onAddLine();
    }
    if (item == getBillCardPanel().getDelLineMenuItem()) {
      onDelLine();
    }
    if (item == getBillCardPanel().getCopyLineMenuItem()) {
      onCopyLine();
    }
    if (item == getBillCardPanel().getPasteLineMenuItem()) {
      onPasteLine();
    }
    setButtonsState();
  }

  public void onNewPrepare()
  {
    remove(getBillListPanel());
    add(getBillCardPanel(), "Center");
    onNewFromList();
    setButtons(this.aryButtonGroup);
  }

  public void onModify()
  {
    if (this.strState.equals("LIST")) {
      int iRow = getBillListPanel().getHeadTable().getSelectedRow();
      if (iRow < 0) {
        showErrorMessage("请选择列表中的一行发票");
        return;
      }
      this.saleinvoice = ((SaleinvoiceVO)this.alBills.get(iRow));
      onNewPrepare();
    } else {
      if ((getBillCardPanel().getHeadItem("nstrikemny") != null) && (new UFDouble(getBillCardPanel().getHeadItem("nstrikemny").getValue()).equals(new UFDouble(0))))
      {
        this.bstrikeflag = false;
      }
      else this.bstrikeflag = true;
      this.strState = "MODIFY";
      setButtons(this.aryButtonGroup);
      setButtonsState();

      if (getBillCardPanel().getRowCount() != 0) {
        for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
          setAssistUnit(i);
        }

      }

    }

    SaleinvoiceVO hvo = (SaleinvoiceVO)getVO(false);
    SaleVO header = (SaleVO)hvo.getParentVO();
    this.m_oldreceipt = header.getVreceiptcode();
    this.oldVO = hvo;

    getBillCardTools().setCardPanelCellEditableByLargess(this.SO59.booleanValue());

    showHintMessage(NCLangRes.getInstance().getStrByID("SCMCommon", "UPPSCMCommon-000350"));
  }

  private void setCardExchgrate(UFDouble nexchangeotobrate, UFDouble nexchangeotoarate)
  {
    getBillCardPanel().setHeadItem("nexchangeotobrate", nexchangeotobrate);
    getBillCardPanel().setHeadItem("nexchangeotoarate", nexchangeotoarate);

    int i = 0; for (int iLen = getBillCardPanel().getRowCount(); i < iLen; i++)
    {
      getBillCardPanel().setBodyValueAt(nexchangeotobrate, i, "nexchangeotobrate");

      getBillCardPanel().setBodyValueAt(nexchangeotoarate, i, "nexchangeotoarate");
    }
  }

  public void onNew()
  {
    if (this.hsSelectedARSubHVO != null)
      this.hsSelectedARSubHVO.clear();
    if (this.oldhsSelectedARSubHVO != null)
      this.oldhsSelectedARSubHVO.clear();
    if (this.hsQueryArsubDataBykey != null)
      this.hsQueryArsubDataBykey.clear();
    if (this.hsTotalBykey != null)
      this.hsTotalBykey.clear();
    if (this.oldhsTotalBykey != null)
      this.oldhsTotalBykey.clear();
    long s = System.currentTimeMillis();
    long s0 = System.currentTimeMillis();
    Timer timer = new Timer();
    timer.start(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000124"));

    if ((this.saleinvoiceNew == null) || (this.saleinvoiceNew.length == 0))
      return;
    SaleinvoiceBVO[] saleBs = (SaleinvoiceBVO[])(SaleinvoiceBVO[])this.saleinvoiceNew[0].getChildrenVO();

    if (saleBs == null) {
      return;
    }
    String receipttype = saleBs[0].getCupreceipttype();

    if (this.isAddList) {
      mergeSourceVOs(this.saleinvoiceNew, receipttype);
      if ((this.alBills != null) && (this.alBills.size() > 1)) {
        onNewList();

        getBillCardTools().setCardPanelCellEditableByLargess(this.SO59.booleanValue());

        return;
      }
    }
    boolean bisCalculate = getBillCardPanel().getBillModel().isNeedCalculate();

    getBillCardPanel().getBillModel().setNeedCalculate(false);
    try
    {
      this.saleinvoice = mergeCheck();
    } catch (BusinessException ex) {
      showErrorMessage(ex.getMessage());
      return;
    }
    this.isEdit = false;
    getBillCardPanel().addNew();
    getBillCardPanel().setEnabled(true);

    timer.addExecutePhase(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000126"));

    for (int i = 0; i < this.saleinvoice.getChildrenVO().length; i++) {
      SaleinvoiceBVO body = (SaleinvoiceBVO)this.saleinvoice.getChildrenVO()[i];
      if ((body.getCbodywarehouseid() != null) && (!body.getCbodywarehouseid().trim().equals("NULL")) && (!body.getCbodywarehouseid().trim().equals("null"))) {
        continue;
      }
      body.setCbodywarehouseid(null);
    }

    String currencyid0 = null;
    if (receipttype.equals("4C"))
    {
      try {
        this.saleinvoice = getDataFrom4CTo32(this.saleinvoice);
        currencyid0 = ((SaleinvoiceBVO)this.saleinvoice.getChildrenVO()[0]).getCcurrencytypeid();
                
      }
      catch (Exception e)
      {
        SCMEnv.out("填充销售出库单生成的发票VO数据时出错！");
        SCMEnv.out(e.getMessage());
      }
      SCMEnv.out("折本汇率为：" + ((SaleinvoiceBVO)this.saleinvoice.getChildrenVO()[0]).getNexchangeotobrate());
    }

    try
    {
	    SaleinvoiceBVO [] childs = (SaleinvoiceBVO[]) saleinvoice.getChildrenVO();
	    for(SaleinvoiceBVO child : childs)
	    {
	    	 String tsbl = (String)HYPubBO_Client.findColValue("so_saleorder_b", "(to_chartsbl,'990.999999')", " corder_bid='"+child.getCsourcebillbodyid()+"'");
	    	 if(tsbl!=null&&!tsbl.trim().equals(""))
	    	 {
	    		 child.setHttsbl(new UFDouble(Double.parseDouble(tsbl)));
	    	 }else
	    	 {
	    		 child.setHttsbl(new UFDouble(0));
	    	 }
	    }
    }catch(Exception ex)
    {
    	ex.printStackTrace();
    	Logger.error(ex.getMessage(), ex);
    }

    timer.addExecutePhase(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000127"));

    UFDouble exchangeotobrate = ((SaleinvoiceBVO)this.saleinvoice.getChildrenVO()[0]).getNexchangeotobrate();

    UFDouble exchangeotoarate = ((SaleinvoiceBVO)this.saleinvoice.getChildrenVO()[0]).getNexchangeotoarate();

    if ((exchangeotobrate == null) || (exchangeotobrate.toString().length() == 0))
    {
      showErrorMessage(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000118"));

      return;
    }

    SCMEnv.out("初始化需要[" + (System.currentTimeMillis() - s) + "]毫秒的时间");
     Hashtable hsOutNum = new Hashtable();
    if ((receipttype != null) && ((receipttype.equals("4C")) || (receipttype.equals("3Q")) || (receipttype.equals("4S"))))
    {
      this.saleinvoice.getParentVO().setAttributeValue("ccurrencyid", currencyid0);

      Hashtable hsbid = new Hashtable();
      for (int i = 0; i < this.saleinvoice.getChildrenVO().length; i++)
      {
        SaleinvoiceBVO body = (SaleinvoiceBVO)this.saleinvoice.getChildrenVO()[i];

        body.setNoriginalcurtaxmny((body.getNoriginalcursummny() == null ? new UFDouble(0) : body.getNoriginalcursummny()).sub(body.getNoriginalcurmny() == null ? new UFDouble(0) : body.getNoriginalcurmny()));

        String out_bid = ((SaleinvoiceBVO)this.saleinvoice.getChildrenVO()[i]).getCupsourcebillbodyid();

        if (!hsbid.containsKey(out_bid))
          hsbid.put(out_bid, new UFDouble(0));
      }
      try {
        hsOutNum = SaleinvoiceBO_Client.getOutNumber(hsbid, this.SO_06);
      } catch (Exception e) {
        SCMEnv.out(e.getMessage());
      }
    }
    timer.addExecutePhase(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000129"));

    setPanelByCurrency(((SaleVO)this.saleinvoice.getParentVO()).getCcurrencyid());
    try
    {
      setExchgEditBycurrency(((SaleVO)this.saleinvoice.getParentVO()).getCcurrencyid());
    }
    catch (Exception e) {
      SCMEnv.out(e.getMessage());
    }

    for (int i = 0; i < this.saleinvoice.getChildrenVO().length; i++) {
      UFDouble nPackNum = ((SaleinvoiceBVO)this.saleinvoice.getChildrenVO()[i]).getNpacknumber();

      if ((nPackNum != null) && (nPackNum.doubleValue() == 0.0D)) {
        ((SaleinvoiceBVO)this.saleinvoice.getChildrenVO()[i]).setNpacknumber(null);
      }

    }
    s = System.currentTimeMillis();
    BillRowNo.setVORowNoByRule(this.saleinvoice, "32", getRowNoItemKey());

    SCMEnv.out("增加行号需要[" + (System.currentTimeMillis() - s) + "]毫秒的时间");

    timer.addExecutePhase(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000130"));

    long s1 = System.currentTimeMillis();

    ArrayList a = new ArrayList();
    for (int i = 0; i < this.saleinvoice.getChildrenVO().length; i++)
      a.add(this.saleinvoice.getChildrenVO()[i]);
    getFormulaBillContainer().formulaBodys(getFormulaItemBody(), a);
    SCMEnv.showTime(s1, NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000131"));

    ((UIRefPane)getBillCardPanel().getHeadItem("ccalbodyid").getComponent()).setAutoCheck(false);

    getBillCardPanel().setBillValueVO(this.saleinvoice);
    getBillCardPanel().execHeadLoadFormulas();

    if ((receipttype != null) && (receipttype.equals("4C")))
    {
      initUnite();
    }
    s = System.currentTimeMillis();

    SCMEnv.out("afterCurrencyChanges方法需要[" + (System.currentTimeMillis() - s) + "]毫秒的时间");

    s = System.currentTimeMillis();
    this.custID = ((SaleVO)this.saleinvoice.getParentVO()).getCreceiptcorpid();
    ((UIRefPane)getBillCardPanel().getHeadItem("ccustomername").getComponent()).setPK(this.custID);
    try
    {
      String[][] results = SaleinvoiceBO_Client.getCustomerInfo(this.custID);
      if (results != null) {
        ((CustBankRefModel)((UIRefPane)getBillCardPanel().getHeadItem("ccustomerbank").getComponent()).getRefModel()).setCondition(results[0][5]);

        getBillCardPanel().setHeadItem("ccustomertel", results[0][0]);
        getBillCardPanel().setHeadItem("ccustomertaxNo", results[0][1]);
        getBillCardPanel().setHeadItem("ccustbankid", results[0][4]);
        getBillCardPanel().setHeadItem("ccustomerbank", results[0][4]);
        getBillCardPanel().setHeadItem("ccustomerbankNo", results[0][3]);

        getBillCardPanel().setHeadItem("vreceiveaddress", results[0][7]);

        getBillCardPanel().getHeadItem("vprintcustname").setValue(results[0][6]);
      }
      else {
        getBillCardPanel().setHeadItem("ccustomertel", "");
        getBillCardPanel().setHeadItem("ccustomertaxNo", "");
        getBillCardPanel().setHeadItem("ccustomerbankNo", "");
        getBillCardPanel().setHeadItem("vreceiveaddress", "");
      }
    } catch (Exception e1) {
      SCMEnv.out("查询失败！");
      e1.printStackTrace(System.out);
    }
  
    SCMEnv.out("设置客户需要[" + (System.currentTimeMillis() - s) + "]毫秒的时间");

    getBillCardPanel().setHeadItem("dbilldate", getClientEnvironment().getDate().toString());

    getBillCardPanel().setTailItem("coperatorid", getClientEnvironment().getUser().getPrimaryKey());

    getBillCardPanel().setTailItem("dmakedate", getClientEnvironment().getDate().toString());

    getBillCardPanel().getHeadItem("fcounteractflag").setValue(new Integer(0));

    s = System.currentTimeMillis();

    if (getBillCardPanel().getRowCount() != 0)
    {
      setUnits(0, getBillCardPanel().getRowCount());

      SaleinvoiceBVO[] items = (SaleinvoiceBVO[])(SaleinvoiceBVO[])this.saleinvoice.getChildrenVO();

      for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
        if ((getBillCardPanel().getBodyValueAt(i, "discountflag") != null) && (getBillCardPanel().getBodyValueAt(i, "discountflag").equals(new Boolean(true))))
        {
          continue;
        }
        if ((receipttype != null) && (receipttype.equals("4C")))
        {
          String bid = getBillCardPanel().getBodyValueAt(i, "cupsourcebillbodyid").toString();

          UFDouble outnum = (UFDouble)getBillCardPanel().getBodyValueAt(i, "nnumber");

          UFDouble hsoutnum = hsOutNum.get(bid) == null ? new UFDouble(0) : (UFDouble)hsOutNum.get(bid);

          if (hsoutnum.compareTo(outnum) != 0)
          {
            double outNum0 = 0.0D;
            double outPackNum0 = 0.0D;
            if (getBillCardPanel().getBodyValueAt(i, "nnumber") != null) {
              outNum0 = ((UFDouble)getBillCardPanel().getBodyValueAt(i, "nnumber")).doubleValue();
            }

            if (getBillCardPanel().getBodyValueAt(i, "npacknumber") != null) {
              outPackNum0 = ((UFDouble)getBillCardPanel().getBodyValueAt(i, "npacknumber")).doubleValue();
            }

            getBillCardPanel().setBodyValueAt(new UFDouble(hsoutnum), i, "nnumber");

            if (getBillCardPanel().getBodyValueAt(i, "npacknumber") != null) {
              getBillCardPanel().setBodyValueAt(new UFDouble(hsoutnum.doubleValue() * outPackNum0 / outNum0), i, "npacknumber");

              getBillCardPanel().setBodyValueAt(new UFDouble(hsoutnum), i, "nquotenumber");
            } 
          }

          setAssistChange(i);
          beforeUnitChange(i);

          UFBoolean largess = items[i].getBlargessflag();
          boolean blargess = largess == null ? false : largess.booleanValue();

          if ((items[i].getM_salequoteorgcurtaxnetprice() != null) && (items[i].getM_outquoteorgcurtaxnetprice() != null) && (items[i].getM_salequoteorgcurtaxnetprice().doubleValue() != items[i].getM_outquoteorgcurtaxnetprice().doubleValue()))
          {
            calculateNumber(i, "nquoteoriginalcurtaxprice");
            getBillCardPanel().setBodyValueAt(items[i].getM_outquoteorgcurtaxnetprice(), i, "nquoteoriginalcurtaxnetprice");

            calculateNumber(i, "nquoteoriginalcurtaxnetprice");
          }
           afterNumberChange(i);
           /**
            * edit 2018年12月4日10:29:34 彭佳佳
            */
           String[] formulas = {
							"noriginalcursummny->noriginalcurtaxprice*nnumber*(nitemdiscountrate/100)",
							"noriginalcurmny->noriginalcurprice*nnumber*(nitemdiscountrate/100)",
							"noriginalcurtaxmny->noriginalcursummny-noriginalcurmny",
							"noriginalcurdiscountmny->nnumber*noriginalcurtaxprice*((100-nitemdiscountrate)/100)",
							"ztsje->noriginalcurdiscountmny+sjtsje",
							"sjtste->ztsje-noriginalcurdiscountmny"};
           getBillCardPanel().getBillModel().execFormulas(i, formulas);
           /**
            * end
            */
           //begin add LGY
           
            String noriginalcurdiscountmny=String.valueOf(getBillCardPanel().getBillModel().getValueAt(i, "noriginalcurdiscountmny"));
            noriginalcurdiscountmny=noriginalcurdiscountmny==null||noriginalcurdiscountmny.equals("")||noriginalcurdiscountmny.equalsIgnoreCase("null")?"0":noriginalcurdiscountmny;
      	  getBillCardPanel().setBodyValueAt(new UFDouble(noriginalcurdiscountmny), i, "ztsje");
      	  // end  LGY
          this.alInvs.add(i, null); } else {
          if ((receipttype == null) || (!receipttype.equals("30"))) {
            continue;
          }
          setAssistChange(i);
          beforeUnitChange(i);
          afterNumberChange(i);
        }
        try
        {
           
            getBillCardPanel().setBodyValueAt(items[i].getHttsbl(),i,"httsbl");
            UFDouble value = (UFDouble)getBillCardPanel().getBodyValueAt(i, "nnumber");
            //四舍五入
            Integer httssl = new BigDecimal(items[i].getHttsbl().div(100).doubleValue()*value.doubleValue()).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
            getBillCardPanel().setBodyValueAt(httssl,i,"httssl");

        }catch(Exception ex)
        {
        	ex.printStackTrace();
        	Debug.error(ex);
        	Logger.error(ex.getMessage(), ex);
        }
      }
    }

    if ((receipttype != null) && ((receipttype.equals("4C")) || (receipttype.equals("3Q")) || (receipttype.equals("4S"))))
    {
      getBillCardPanel().getBodyItem("cbodywarehousename").setEnabled(false);

      getBillCardPanel().getBodyItem("cbatchid").setEnabled(false);
      getBillCardPanel().getBodyItem("cpackunitname").setEnabled(false);
    }

    setCardExchgrate(exchangeotobrate, exchangeotoarate);
    initFreeItem(this.saleinvoice.getChildrenVO());
    this.isEdit = true;

    s = System.currentTimeMillis();

    String str1 = null;
    String str2 = null;

    String str3 = null;
    String str4 = null;

    if ((getBillCardPanel().getBodyItem("coriginalbillcode") != null) && 
      (getBillCardPanel().getRowCount() != 0)) {
      if (("30".equals(this.saleinvoice.getChildrenVO()[0].getAttributeValue("creceipttype").toString())) || ("3A".equals(this.saleinvoice.getChildrenVO()[0].getAttributeValue("creceipttype").toString())))
      {
        str1 = "'" + this.saleinvoice.getChildrenVO()[0].getAttributeValue("csourcebillbodyid").toString() + "'";
      }

      if (("4C".equals(this.saleinvoice.getChildrenVO()[0].getAttributeValue("creceipttype").toString())) || ("3Q".equals(this.saleinvoice.getChildrenVO()[0].getAttributeValue("creceipttype").toString())))
      {
        str2 = "'" + this.saleinvoice.getChildrenVO()[0].getAttributeValue("csourcebillbodyid").toString() + "'";
      }

      if (("30".equals(this.saleinvoice.getChildrenVO()[0].getAttributeValue("cupreceipttype").toString())) || ("3A".equals(this.saleinvoice.getChildrenVO()[0].getAttributeValue("cupreceipttype").toString())))
      {
        str3 = "'" + this.saleinvoice.getChildrenVO()[0].getAttributeValue("cupsourcebillbodyid").toString() + "'";
      }

      if (("4C".equals(this.saleinvoice.getChildrenVO()[0].getAttributeValue("cupreceipttype").toString())) || ("3Q".equals(this.saleinvoice.getChildrenVO()[0].getAttributeValue("cupreceipttype").toString())))
      {
        str4 = "'" + this.saleinvoice.getChildrenVO()[0].getAttributeValue("cupsourcebillbodyid").toString() + "'";
      }

      for (int i = 1; i < getBillCardPanel().getRowCount(); i++)
      {
        if (("30".equals(this.saleinvoice.getChildrenVO()[i].getAttributeValue("creceipttype").toString())) || ("3A".equals(this.saleinvoice.getChildrenVO()[i].getAttributeValue("creceipttype").toString())))
        {
          if (this.saleinvoice.getChildrenVO()[i].getAttributeValue("csourcebillbodyid") != this.saleinvoice.getChildrenVO()[(i - 1)].getAttributeValue("csourcebillbodyid"))
          {
            str1 = str1 + ",'" + this.saleinvoice.getChildrenVO()[i].getAttributeValue("csourcebillbodyid").toString() + "'";
          }

        }

        if (("4C".equals(this.saleinvoice.getChildrenVO()[i].getAttributeValue("creceipttype").toString())) || ("3Q".equals(this.saleinvoice.getChildrenVO()[i].getAttributeValue("creceipttype").toString())))
        {
          if (this.saleinvoice.getChildrenVO()[i].getAttributeValue("csourcebillbodyid") != this.saleinvoice.getChildrenVO()[(i - 1)].getAttributeValue("csourcebillbodyid"))
          {
            str2 = str2 + ",'" + this.saleinvoice.getChildrenVO()[i].getAttributeValue("csourcebillbodyid").toString() + "'";
          }

        }

        if (("30".equals(this.saleinvoice.getChildrenVO()[i].getAttributeValue("cupreceipttype").toString())) || ("3A".equals(this.saleinvoice.getChildrenVO()[i].getAttributeValue("cupreceipttype").toString())))
        {
          if (this.saleinvoice.getChildrenVO()[i].getAttributeValue("cupsourcebillbodyid") != this.saleinvoice.getChildrenVO()[(i - 1)].getAttributeValue("cupsourcebillbodyid"))
          {
            str3 = str3 + ",'" + this.saleinvoice.getChildrenVO()[i].getAttributeValue("cupsourcebillbodyid").toString() + "'";
          }

        }

        if ((!"4C".equals(this.saleinvoice.getChildrenVO()[i].getAttributeValue("cupreceipttype").toString())) && (!"3Q".equals(this.saleinvoice.getChildrenVO()[i].getAttributeValue("cupreceipttype").toString())))
        {
          continue;
        }

        if (this.saleinvoice.getChildrenVO()[i].getAttributeValue("cupsourcebillbodyid") == this.saleinvoice.getChildrenVO()[(i - 1)].getAttributeValue("cupsourcebillbodyid"))
        {
          continue;
        }
        str4 = str4 + ",'" + this.saleinvoice.getChildrenVO()[i].getAttributeValue("cupsourcebillbodyid").toString() + "'";
      }

      str1 = (str1 == null ? "''" : str1) + "," + (str3 == null ? "''" : str3);

      str2 = (str2 == null ? "''" : str2) + "," + (str4 == null ? "''" : str4);

      Hashtable h1 = new Hashtable();
      try {
        int itype = 0;
        String strall = null;
        if (str1 != null) {
          itype++;
          strall = str1;
        }
        if (str2 != null) {
          if (strall == null)
            strall = str2;
          else strall = strall + "@#$%" + str2;
          itype += 2;
        }
        h1 = SaleinvoiceBO_Client.queryBillCodeBySource(itype, strall);
      }
      catch (Exception e)
      {
        SCMEnv.out(e.getMessage());
      }
      for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
        String csourcebillbodyid = null;
        String cupsourcebillbodyid = null;
        String coriginalbillcode = null;
        String cupsourcebillcode = null;
        String csourcebillcode = null;
        csourcebillbodyid = (String)this.saleinvoice.getChildrenVO()[i].getAttributeValue("csourcebillbodyid");

        cupsourcebillbodyid = (String)this.saleinvoice.getChildrenVO()[i].getAttributeValue("cupsourcebillbodyid");

        coriginalbillcode = (String)h1.get(csourcebillbodyid);
        cupsourcebillcode = (String)h1.get(cupsourcebillbodyid);
        csourcebillcode = (String)h1.get(cupsourcebillbodyid);
        getBillCardPanel().setHeadItem("coriginalbillcode", coriginalbillcode);

        getBillCardPanel().setBodyValueAt(coriginalbillcode, i, "coriginalbillcode");

        getBillCardPanel().setHeadItem("cupsourcebillcode", cupsourcebillcode);

        getBillCardPanel().setBodyValueAt(cupsourcebillcode, i, "cupsourcebillcode");

        getBillCardPanel().setHeadItem("csourcebillcode", csourcebillcode);

        getBillCardPanel().setBodyValueAt(csourcebillcode, i, "csourcebillcode");
      }

    }

    SCMEnv.out("计算源头单据号需要[" + (System.currentTimeMillis() - s) + "]毫秒的时间");

    s = System.currentTimeMillis();

    getBillCardPanel().setHeadItem("ntotalsummny", calcurateTotal("noriginalcursummny"));

    getBillCardPanel().setHeadItem("nstrikemny", new UFDouble(0));
    getBillCardPanel().execHeadFormula("nnetmny->ntotalsummny-nstrikemny");
    getBillCardPanel().execHeadFormula("csalecompanyname->getColValue(bd_corp,unitname,pk_corp,pk_corp)");

    String[] formulas = { "cprolinename->getColValue(bd_prodline,prodlinename,pk_prodline,cprolineid)", "creceiptcorpname->getColValue(bd_cubasdoc,custname,pk_cubasdoc,creceiptcorpname)", "ccustomername->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,ccustomerid)", "ccustomername->getColValue(bd_cubasdoc,custname,pk_cubasdoc,ccustomername)" };

    getBillCardPanel().getBillModel().execLoadFormula();
    getBillCardPanel().getBillModel().execFormulas(formulas);

    getBillCardPanel().getBillModel().setNeedCalculate(bisCalculate);

    UFBoolean freef = ((SaleVO)this.saleinvoice.getParentVO()).getBfreecustflag();

    boolean freeflag = freef == null ? false : freef.booleanValue();
    if (freeflag) {
      getBillCardPanel().getHeadItem("cfreecustid").setEnabled(true);
      getBillCardPanel().getHeadItem("cfreecustid").setEdit(true);
    } else {
      getBillCardPanel().getHeadItem("cfreecustid").setEnabled(false);
      getBillCardPanel().getHeadItem("cfreecustid").setEdit(false);
    }
    try
    {
      if (((SaleVO)this.saleinvoice.getParentVO()).getCcurrencyid().equals(this.currtype.getLocalCurrPK()))
      {
        getBillCardPanel().getHeadItem("nexchangeotobrate").setEnabled(false);

        getBillCardPanel().getHeadItem("nexchangeotoarate").setEnabled(false);

        getBillCardPanel().getHeadItem("nexchangeotobrate").setEdit(false);

        getBillCardPanel().getHeadItem("nexchangeotoarate").setEdit(false);
      }
    }
    catch (Exception e) {
      e.printStackTrace(System.out);
    }
    ((UIRefPane)getBillCardPanel().getHeadItem("cbiztypename").getComponent()).setPK(getBillCardPanel().getBusiType());

    getBillCardTools().setCardPanelCellEditableByLargess(this.SO59.booleanValue());
    if (bisCalculate)
      getBillCardPanel().getBillModel().reCalcurateAll();
    this.bstrikeflag = false;
    this.strState = "ADD";
    setButtonsState();

    SCMEnv.out("总共需要[" + (System.currentTimeMillis() - s0) + "]毫秒的时间");

    this.hsSelectedARSubHVO = new Hashtable();
    this.oldhsSelectedARSubHVO = new Hashtable();
    showHintMessage(NCLangRes.getInstance().getStrByID("SCMCommon", "UPPSCMCommon-000423"));
  }

  private void onNewFromList()
  {
    if (this.hsSelectedARSubHVO != null)
      this.hsSelectedARSubHVO.clear();
    if (this.oldhsSelectedARSubHVO != null)
      this.oldhsSelectedARSubHVO.clear();
    if (this.hsQueryArsubDataBykey != null)
      this.hsQueryArsubDataBykey.clear();
    if (this.hsTotalBykey != null)
      this.hsTotalBykey.clear();
    if (this.oldhsTotalBykey != null)
      this.oldhsTotalBykey.clear();
    long s = System.currentTimeMillis();
    long s0 = System.currentTimeMillis();
    boolean bisCalculate = getBillCardPanel().getBillModel().isNeedCalculate();

    getBillCardPanel().getBillModel().setNeedCalculate(false);
    Timer timer = new Timer();
    timer.start(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000124"));

    if (this.saleinvoice == null)
      return;
    SaleinvoiceBVO[] saleBs = (SaleinvoiceBVO[])(SaleinvoiceBVO[])this.saleinvoice.getChildrenVO();

    if (saleBs == null) {
      return;
    }
    String receipttype = saleBs[0].getCupreceipttype();
    this.isEdit = false;
    getBillCardPanel().addNew();
    getBillCardPanel().setEnabled(true);

    timer.addExecutePhase(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000126"));

    for (int i = 0; i < this.saleinvoice.getChildrenVO().length; i++) {
      SaleinvoiceBVO body = (SaleinvoiceBVO)this.saleinvoice.getChildrenVO()[i];
      if ((body.getCbodywarehouseid() != null) && (!body.getCbodywarehouseid().trim().equals("NULL")) && (!body.getCbodywarehouseid().trim().equals("null"))) {
        continue;
      }
      body.setCbodywarehouseid(null);
    }

    String currencyid0 = null;
    if (receipttype.equals("4C")) {
      String cfirstbilltype = ((SaleinvoiceBVO)this.saleinvoice.getChildrenVO()[0]).getCreceipttype();
      try
      {
        this.saleinvoice = getDataFrom4CTo32(this.saleinvoice);
        currencyid0 = ((SaleinvoiceBVO)this.saleinvoice.getChildrenVO()[0]).getCcurrencytypeid();
      }
      catch (Exception e) {
        SCMEnv.out("填充销售出库单生成的发票VO数据时出错！");
        SCMEnv.out(e.getMessage());
      }

    }

    UFDouble exchangeotobrate = ((SaleinvoiceBVO)this.saleinvoice.getChildrenVO()[0]).getNexchangeotobrate();

    UFDouble exchangeotoarate = ((SaleinvoiceBVO)this.saleinvoice.getChildrenVO()[0]).getNexchangeotoarate();

    if ((exchangeotobrate == null) || (exchangeotobrate.toString().length() == 0))
    {
      showErrorMessage(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000118"));

      return;
    }

    Hashtable hsOutNum = new Hashtable();
    if ((receipttype != null) && ((receipttype.equals("4C")) || (receipttype.equals("3Q")) || (receipttype.equals("4S"))))
    {
      this.saleinvoice.getParentVO().setAttributeValue("ccurrencyid", currencyid0);

      Hashtable hsbid = new Hashtable();
      for (int i = 0; i < this.saleinvoice.getChildrenVO().length; i++)
      {
        SaleinvoiceBVO body = (SaleinvoiceBVO)this.saleinvoice.getChildrenVO()[i];

        body.setNoriginalcurtaxmny((body.getNoriginalcursummny() == null ? new UFDouble(0) : body.getNoriginalcursummny()).sub(body.getNoriginalcurmny() == null ? new UFDouble(0) : body.getNoriginalcurmny()));

        String out_bid = ((SaleinvoiceBVO)this.saleinvoice.getChildrenVO()[i]).getCupsourcebillbodyid();

        if (!hsbid.containsKey(out_bid))
          hsbid.put(out_bid, new UFDouble(0));
      }
      try {
        hsOutNum = SaleinvoiceBO_Client.getOutNumber(hsbid, this.SO_06);
      } catch (Exception e) {
        SCMEnv.out(e.getMessage());
      }
    }
    timer.addExecutePhase(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000129"));

    UFDate billdate = ((SaleVO)this.saleinvoice.getParentVO()).getDbilldate();
    String dbilldate = getClientEnvironment().getDate().toString();
    if (billdate != null) {
      dbilldate = billdate.toString();
    }
    setPanelByCurrency(((SaleVO)this.saleinvoice.getParentVO()).getCcurrencyid());

    for (int i = 0; i < this.saleinvoice.getChildrenVO().length; i++) {
      UFDouble nPackNum = ((SaleinvoiceBVO)this.saleinvoice.getChildrenVO()[i]).getNpacknumber();

      if ((nPackNum != null) && (nPackNum.doubleValue() == 0.0D)) {
        ((SaleinvoiceBVO)this.saleinvoice.getChildrenVO()[i]).setNpacknumber(null);
      }

    }

    s = System.currentTimeMillis();
    BillRowNo.setVORowNoByRule(this.saleinvoice, "32", getRowNoItemKey());

    SCMEnv.out("增加行号需要[" + (System.currentTimeMillis() - s) + "]毫秒的时间");

    timer.addExecutePhase(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000130"));

    long s1 = System.currentTimeMillis();

    ArrayList a = new ArrayList();
    for (int i = 0; i < this.saleinvoice.getChildrenVO().length; i++)
      a.add(this.saleinvoice.getChildrenVO()[i]);
    getFormulaBillContainer().formulaBodys(getFormulaItemBody(), a);
    SCMEnv.showTime(s1, NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000131"));

    ((UIRefPane)getBillCardPanel().getHeadItem("ccalbodyid").getComponent()).setAutoCheck(false);

    getBillCardPanel().setBillValueVO(this.saleinvoice);
    getBillCardPanel().execHeadLoadFormulas();

    if ((receipttype != null) && (receipttype.equals("4C")))
    {
      initUnite();
    }this.strState = "ADD";
    setButtonsState();

    s = System.currentTimeMillis();
    afterCurrencyChange(dbilldate);
    SCMEnv.out("afterCurrencyChanges方法需要[" + (System.currentTimeMillis() - s) + "]毫秒的时间");

    UFBoolean freef = ((SaleVO)this.saleinvoice.getParentVO()).getBfreecustflag();

    boolean freeflag = freef == null ? false : freef.booleanValue();
    if (freeflag) {
      ((UIRefPane)getBillCardPanel().getHeadItem("cfreecustid").getComponent()).setEnabled(true);
    }
    else {
      ((UIRefPane)getBillCardPanel().getHeadItem("cfreecustid").getComponent()).setEnabled(false);
    }

    getBillCardPanel().getHeadItem("fcounteractflag").setValue(new Integer(0));

    s = System.currentTimeMillis();
    this.custID = ((SaleVO)this.saleinvoice.getParentVO()).getCreceiptcorpid();
    ((UIRefPane)getBillCardPanel().getHeadItem("ccustomername").getComponent()).setPK(this.custID);
    try
    {
      String[][] results = SaleinvoiceBO_Client.getCustomerInfo(this.custID);
      if (results != null) {
        ((CustBankRefModel)((UIRefPane)getBillCardPanel().getHeadItem("ccustomerbank").getComponent()).getRefModel()).setCondition(results[0][5]);

        getBillCardPanel().setHeadItem("ccustomertel", results[0][0]);
        getBillCardPanel().setHeadItem("ccustomertaxNo", results[0][1]);
        getBillCardPanel().setHeadItem("ccustbankid", results[0][4]);
        getBillCardPanel().setHeadItem("ccustomerbank", results[0][4]);
        getBillCardPanel().setHeadItem("ccustomerbankNo", results[0][3]);

        getBillCardPanel().setHeadItem("vreceiveaddress", results[0][7]);

        getBillCardPanel().getHeadItem("vprintcustname").setValue(results[0][6]);
      }
      else
      {
        getBillCardPanel().setHeadItem("ccustomertel", "");
        getBillCardPanel().setHeadItem("ccustomertaxNo", "");
        getBillCardPanel().setHeadItem("ccustomerbankNo", "");
        getBillCardPanel().setHeadItem("vreceiveaddress", "");
      }
    } catch (Exception e1) {
      SCMEnv.out("查询失败！");
      e1.printStackTrace(System.out);
    }
    SCMEnv.out("设置客户需要[" + (System.currentTimeMillis() - s) + "]毫秒的时间");

    getBillCardPanel().setHeadItem("dbilldate", getClientEnvironment().getDate().toString());

    getBillCardPanel().setTailItem("coperatorid", getClientEnvironment().getUser().getPrimaryKey());

    getBillCardPanel().setTailItem("dmakedate", getClientEnvironment().getDate().toString());

    s = System.currentTimeMillis();

    if (getBillCardPanel().getRowCount() != 0)
    {
      setUnits(0, getBillCardPanel().getRowCount());
      SaleinvoiceBVO[] items = (SaleinvoiceBVO[])(SaleinvoiceBVO[])this.saleinvoice.getChildrenVO();

      for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
        if ((getBillCardPanel().getBodyValueAt(i, "discountflag") != null) && (getBillCardPanel().getBodyValueAt(i, "discountflag").equals(new Boolean(true))))
        {
          continue;
        }
        if ((receipttype != null) && (receipttype.equals("4C")))
        {
          String bid = getBillCardPanel().getBodyValueAt(i, "cupsourcebillbodyid").toString();

          UFDouble outnum = (UFDouble)getBillCardPanel().getBodyValueAt(i, "nnumber");

          UFDouble hsoutnum = hsOutNum.get(bid) == null ? new UFDouble(0) : (UFDouble)hsOutNum.get(bid);

          if (hsoutnum.compareTo(outnum) != 0) {
            double outNum0 = 0.0D;
            double outPackNum0 = 0.0D;
            if (getBillCardPanel().getBodyValueAt(i, "nnumber") != null) {
              outNum0 = ((UFDouble)getBillCardPanel().getBodyValueAt(i, "nnumber")).doubleValue();
            }

            if (getBillCardPanel().getBodyValueAt(i, "npacknumber") != null) {
              outPackNum0 = ((UFDouble)getBillCardPanel().getBodyValueAt(i, "npacknumber")).doubleValue();
            }

            getBillCardPanel().setBodyValueAt(new UFDouble(hsoutnum), i, "nnumber");

            if (getBillCardPanel().getBodyValueAt(i, "npacknumber") != null) {
              getBillCardPanel().setBodyValueAt(new UFDouble(hsoutnum.doubleValue() * outPackNum0 / outNum0), i, "npacknumber");

              getBillCardPanel().setBodyValueAt(new UFDouble(hsoutnum), i, "nquotenumber");
            }
          }

          setAssistChange(i);
          if ((items[i].getM_salequoteorgcurtaxnetprice() != null) && (items[i].getM_outquoteorgcurtaxnetprice() != null) && (items[i].getM_salequoteorgcurtaxnetprice().doubleValue() != items[i].getM_outquoteorgcurtaxnetprice().doubleValue()))
          {
            calculateNumber(i, "nquoteoriginalcurtaxprice");
            getBillCardPanel().setBodyValueAt(items[i].getM_outquoteorgcurtaxnetprice(), i, "nquoteoriginalcurtaxnetprice");

            calculateNumber(i, "nquoteoriginalcurtaxnetprice");
          }

          beforeUnitChange(i);
          afterNumberChange(i);
          calculateNumber(i, "noriginalcurtaxprice");
          this.alInvs.add(i, null); } else {
          if ((receipttype == null) || (!receipttype.equals("30"))) {
            continue;
          }
          setAssistChange(i);
          beforeUnitChange(i);
          afterNumberChange(i);
        }
      }
    }
    if ((receipttype != null) && ((receipttype.equals("4C")) || (receipttype.equals("3Q")) || (receipttype.equals("4S"))))
    {
      getBillCardPanel().getBodyItem("cbodywarehousename").setEnabled(false);

      getBillCardPanel().getBodyItem("cbatchid").setEnabled(false);
      getBillCardPanel().getBodyItem("cpackunitname").setEnabled(false);
    }
    getBillCardPanel().setHeadItem("nexchangeotobrate", exchangeotobrate.toString());

    if (exchangeotoarate != null) {
      getBillCardPanel().setHeadItem("nexchangeotoarate", exchangeotoarate.toString());
    }
    initFreeItem(this.saleinvoice.getChildrenVO());
    this.isEdit = true;

    s = System.currentTimeMillis();

    String str1 = null;
    String str2 = null;

    String str3 = null;
    String str4 = null;
    if ((getBillCardPanel().getBodyItem("coriginalbillcode") != null) && 
      (getBillCardPanel().getRowCount() != 0)) {
      if (("30".equals(this.saleinvoice.getChildrenVO()[0].getAttributeValue("creceipttype").toString())) || ("3A".equals(this.saleinvoice.getChildrenVO()[0].getAttributeValue("creceipttype").toString())))
      {
        str1 = "'" + this.saleinvoice.getChildrenVO()[0].getAttributeValue("csourcebillbodyid").toString() + "'";
      }

      if (("4C".equals(this.saleinvoice.getChildrenVO()[0].getAttributeValue("creceipttype").toString())) || ("3Q".equals(this.saleinvoice.getChildrenVO()[0].getAttributeValue("creceipttype").toString())))
      {
        str2 = "'" + this.saleinvoice.getChildrenVO()[0].getAttributeValue("csourcebillbodyid").toString() + "'";
      }

      if (("30".equals(this.saleinvoice.getChildrenVO()[0].getAttributeValue("cupreceipttype").toString())) || ("3A".equals(this.saleinvoice.getChildrenVO()[0].getAttributeValue("cupreceipttype").toString())))
      {
        str3 = "'" + this.saleinvoice.getChildrenVO()[0].getAttributeValue("cupsourcebillbodyid").toString() + "'";
      }

      if (("4C".equals(this.saleinvoice.getChildrenVO()[0].getAttributeValue("cupreceipttype").toString())) || ("3Q".equals(this.saleinvoice.getChildrenVO()[0].getAttributeValue("cupreceipttype").toString())))
      {
        str4 = "'" + this.saleinvoice.getChildrenVO()[0].getAttributeValue("cupsourcebillbodyid").toString() + "'";
      }

      for (int i = 1; i < getBillCardPanel().getRowCount(); i++)
      {
        if (("30".equals(this.saleinvoice.getChildrenVO()[i].getAttributeValue("creceipttype").toString())) || ("3A".equals(this.saleinvoice.getChildrenVO()[i].getAttributeValue("creceipttype").toString())))
        {
          if (this.saleinvoice.getChildrenVO()[i].getAttributeValue("csourcebillbodyid") != this.saleinvoice.getChildrenVO()[(i - 1)].getAttributeValue("csourcebillbodyid"))
          {
            str1 = str1 + ",'" + this.saleinvoice.getChildrenVO()[i].getAttributeValue("csourcebillbodyid").toString() + "'";
          }

        }

        if (("4C".equals(this.saleinvoice.getChildrenVO()[i].getAttributeValue("creceipttype").toString())) || ("3Q".equals(this.saleinvoice.getChildrenVO()[i].getAttributeValue("creceipttype").toString())))
        {
          if (this.saleinvoice.getChildrenVO()[i].getAttributeValue("csourcebillbodyid") != this.saleinvoice.getChildrenVO()[(i - 1)].getAttributeValue("csourcebillbodyid"))
          {
            str2 = str2 + ",'" + this.saleinvoice.getChildrenVO()[i].getAttributeValue("csourcebillbodyid").toString() + "'";
          }

        }

        if (("30".equals(this.saleinvoice.getChildrenVO()[i].getAttributeValue("cupreceipttype").toString())) || ("3A".equals(this.saleinvoice.getChildrenVO()[i].getAttributeValue("cupreceipttype").toString())))
        {
          if (this.saleinvoice.getChildrenVO()[i].getAttributeValue("cupsourcebillbodyid") != this.saleinvoice.getChildrenVO()[(i - 1)].getAttributeValue("cupsourcebillbodyid"))
          {
            str3 = str3 + ",'" + this.saleinvoice.getChildrenVO()[i].getAttributeValue("cupsourcebillbodyid").toString() + "'";
          }

        }

        if ((!"4C".equals(this.saleinvoice.getChildrenVO()[i].getAttributeValue("cupreceipttype").toString())) && (!"3Q".equals(this.saleinvoice.getChildrenVO()[i].getAttributeValue("cupreceipttype").toString())))
        {
          continue;
        }

        if (this.saleinvoice.getChildrenVO()[i].getAttributeValue("cupsourcebillbodyid") == this.saleinvoice.getChildrenVO()[(i - 1)].getAttributeValue("cupsourcebillbodyid"))
        {
          continue;
        }
        str4 = str4 + ",'" + this.saleinvoice.getChildrenVO()[i].getAttributeValue("cupsourcebillbodyid").toString() + "'";
      }

      str1 = (str1 == null ? "''" : str1) + "," + (str3 == null ? "''" : str3);

      str2 = (str2 == null ? "''" : str2) + "," + (str4 == null ? "''" : str4);

      Hashtable h1 = new Hashtable();
      Hashtable h2 = new Hashtable();
      try {
        if (str1 == null)
          h1 = null;
        else {
          h1 = SaleinvoiceBO_Client.queryBillCodeBySource(1, str1);
        }
        if (str2 == null)
          h2 = null;
        else {
          h2 = SaleinvoiceBO_Client.queryBillCodeBySource(2, str2);
        }
        if ((h1 != null) && (h2 != null))
          h1.putAll(h2);
        else if ((h1 == null) && (h2 != null))
          h1 = h2;
      }
      catch (Exception e) {
        SCMEnv.out(e.getMessage());
      }
      for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
        String csourcebillbodyid = null;
        String cupsourcebillbodyid = null;
        String coriginalbillcode = null;
        String cupsourcebillcode = null;
        String csourcebillcode = null;
        csourcebillbodyid = (String)this.saleinvoice.getChildrenVO()[i].getAttributeValue("csourcebillbodyid");

        cupsourcebillbodyid = (String)this.saleinvoice.getChildrenVO()[i].getAttributeValue("cupsourcebillbodyid");

        coriginalbillcode = (String)h1.get(csourcebillbodyid);
        cupsourcebillcode = (String)h1.get(cupsourcebillbodyid);
        csourcebillcode = (String)h1.get(cupsourcebillbodyid);
        getBillCardPanel().setHeadItem("coriginalbillcode", coriginalbillcode);

        getBillCardPanel().setBodyValueAt(coriginalbillcode, i, "coriginalbillcode");

        getBillCardPanel().setHeadItem("cupsourcebillcode", cupsourcebillcode);

        getBillCardPanel().setBodyValueAt(cupsourcebillcode, i, "cupsourcebillcode");

        getBillCardPanel().setHeadItem("csourcebillcode", csourcebillcode);

        getBillCardPanel().setBodyValueAt(csourcebillcode, i, "csourcebillcode");
      }

    }

    SCMEnv.out("计算源头单据号需要[" + (System.currentTimeMillis() - s) + "]毫秒的时间");

    s = System.currentTimeMillis();

    getBillCardPanel().setHeadItem("ntotalsummny", calcurateTotal("noriginalcursummny"));

    getBillCardPanel().setHeadItem("nstrikemny", new UFDouble(0));
    getBillCardPanel().execHeadFormula("nnetmny->ntotalsummny-nstrikemny");
    getBillCardPanel().execHeadFormula("csalecompanyname->getColValue(bd_corp,unitname,pk_corp,pk_corp)");

    String[] formulas = { "cprolinename->getColValue(bd_prodline,prodlinename,pk_prodline,cprolineid)", "creceiptcorpname->getColValue(bd_cubasdoc,custname,pk_cubasdoc,creceiptcorpname)" };

    getBillCardPanel().getBillModel().execFormulas(formulas);
    getBillCardPanel().getBillModel().execLoadFormula();
    getBillCardPanel().getBillModel().setNeedCalculate(bisCalculate);
    if (bisCalculate)
      getBillCardPanel().getBillModel().reCalcurateAll();
    this.bstrikeflag = false;

    SCMEnv.out("总共需要[" + (System.currentTimeMillis() - s0) + "]毫秒的时间");

    this.hsSelectedARSubHVO = new Hashtable();
    this.oldhsSelectedARSubHVO = new Hashtable();
  }

  private void onNewList()
  {
    remove(getBillCardPanel());
    add(getBillListPanel(), "Center");
    this.strState = "LIST";
    getBillListPanel().setHeaderValueVO(getAddListHeadValues());
    getBillListPanel().getHeadBillModel().execLoadFormula();
    getBillListPanel().getBodyBillModel().execLoadFormula();
    getBillListPanel().getHeadBillModel().execFormulas(new String[] { "vprintcustname->ccustomername" });
    setButtons(this.aryButtonGroupList);
    setButtonsState();
    updateUI();
    showHintMessage(NCLangRes.getInstance().getStrByID("SCMCommon", "UPPSCMCommon-000423"));
  }
	
  /**
	 * @功能:返回公司的上级公司编码
	 * @author ：cm
	 * @2012/9/5
	 * 
	 * @since v50
	 */
	public String getParentCorpCode() {

  		String ParentCorp = new String();
  		String key = ClientEnvironment.getInstance().getCorporation()
  				.getFathercorp();
  		try {
  			CorpVO corpVO = CorpBO_Client.findByPrimaryKey(key);
  			ParentCorp = corpVO.getUnitcode();
  		} catch (BusinessException e) {
  			e.printStackTrace();
  		}
  		return ParentCorp;
  	}
	
  public void onSave()
  {
    try
    {
      long s = System.currentTimeMillis();
      int invoicetype = Integer.valueOf(getBillCardPanel().getHeadItem("finvoicetype").getValue()).intValue();
      
      int rowcount = getBillCardPanel().getBillTable().getRowCount();
      for(int i =0;rowcount<i;i++){
    	  
      }
      //add by zwx 发票金额超过100万   2014-7-24
      UFDouble ntotalsummny = new UFDouble(getBillCardPanel().getHeadItem("ntotalsummny").getValue());
      if(this.getParentCorpCode().equals("10395")){
    	  if(ntotalsummny.doubleValue()>=1170000){
        	  showErrorMessage("发票金额超过100万元");
        	  return;
          }
      }
      //end by zwx
      for (int b = 0; b < getBillCardPanel().getRowCount(); b++) {
        if (getBillCardPanel().getBodyValueAt(b, "blargessflag") != null) {
          String largessflag = getBillCardPanel().getBodyValueAt(b, "blargessflag").toString();

          if ((!largessflag.equals("Y")) || ((invoicetype != 0) && (invoicetype != 1)))
            continue;
          showErrorMessage(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000079"));

          return;
        }

      }

      SaleinvoiceVO saleinvoice = (SaleinvoiceVO)getBillCardPanel().getBillValueChangeVO(SaleinvoiceVO.class.getName(), SaleVO.class.getName(), SaleinvoiceBVO.class.getName());

      SaleinvoiceVO saleinvoicemdy = (SaleinvoiceVO)getBillCardPanel().getBillValueVO(SaleinvoiceVO.class.getName(), SaleVO.class.getName(), SaleinvoiceBVO.class.getName());

      for (int i = 0; i < saleinvoice.getChildrenVO().length; i++) {
        if ((saleinvoice.getChildrenVO()[i].getAttributeValue("cupreceipttype") == null) || (!saleinvoice.getChildrenVO()[i].getAttributeValue("cupreceipttype").equals("30")))
        {
          continue;
        }
        saleinvoice.getChildrenVO()[i].setAttributeValue("cupsourcebillcode", null);
      }

      ((SaleVO)saleinvoice.getParentVO()).setPk_corp(this.pk_corp);

      ((SaleVO)saleinvoice.getParentVO()).setBcodechanged(this.m_isCodeChanged);

      if (this.m_isCodeChanged == true) {
        ((SaleVO)saleinvoice.getParentVO()).setVoldreceiptcode(this.m_oldreceipt);
      }

      ((SaleVO)saleinvoice.getParentVO()).setCreceipttype("32");

      if (this.isGather) {
        ((SaleVO)saleinvoice.getParentVO()).setIsGather(new UFBoolean(true));
      }
      else {
        ((SaleVO)saleinvoice.getParentVO()).setIsGather(new UFBoolean(false));
      }

      ((SaleVO)saleinvoice.getParentVO()).setCbiztype(this.strBusitype);

      ((SaleVO)saleinvoice.getParentVO()).setFstatus(new Integer(1));
      
      //eric 将折扣判断处理进行优化 2015-6-18
      
      FormulaParse fp = new FormulaParse();
      
      if (saleinvoice.getChildrenVO() != null) {
    	  SuperVOUtil.execFormulaWithVOs(saleinvoice.getChildrenVO(), new String[]{"discountflag->getColValue(bd_invbasdoc,discountflag,pk_invbasdoc,cinvbasdocid)"},null, fp);
        for (int i = 0; i < saleinvoice.getChildrenVO().length; i++)
        {
          ((SaleinvoiceBVO)saleinvoice.getChildrenVO()[i]).setPk_corp(getCorpPrimaryKey());

          ((SaleinvoiceBVO)saleinvoice.getChildrenVO()[i]).setFrowstatus(new Integer(1));

//          ((SaleinvoiceBVO)saleinvoice.getChildrenVO()[i]).m_discountflag = new UFBoolean(isLaborOrDiscount(((SaleinvoiceBVO)saleinvoice.getChildrenVO()[i]).getCinvbasdocid()));
        }
        
      }
      if (saleinvoicemdy.getChildrenVO() != null) {
    	  SuperVOUtil.execFormulaWithVOs(saleinvoicemdy.getChildrenVO(), new String[]{"discountflag->getColValue(bd_invbasdoc,discountflag,pk_invbasdoc,cinvbasdocid)"},null, fp);
//        for (int i = 0; i < saleinvoicemdy.getChildrenVO().length; i++) {
//          ((SaleinvoiceBVO)saleinvoicemdy.getChildrenVO()[i]).m_discountflag = new UFBoolean(isLaborOrDiscount(((SaleinvoiceBVO)saleinvoicemdy.getChildrenVO()[i]).getCinvbasdocid()));
//        }
      }

      UIRefPane vreceiveaddress = (UIRefPane)getBillCardPanel().getHeadItem("vreceiveaddress").getComponent();

      ((SaleVO)saleinvoice.getParentVO()).setVreceiveaddress(vreceiveaddress.getText());

      if (this.strState.equals("ADD")) {
        if (this.isGather)
          onGatherCheck(saleinvoice);
        else
          onCheck(saleinvoice);
        saleinvoice.setStatus(2);
      }

      if (this.strState.equals("MODIFY")) {
        onCheck(saleinvoicemdy);

        saleinvoice.setStatus(1);
      }

      if (!BillRowNo.verifyRowNosCorrect(getBillCardPanel(), getRowNoItemKey()))
      {
        return;
      }

      if ((this.hsSelectedARSubHVO != null) && (this.hsSelectedARSubHVO.size() != 0))
      {
        SaleinvoiceBO_Client.checkArsubValidity(this.hsSelectedARSubHVO, this.bstrikeflag);
      }

      saleinvoice.setAllinvoicevo(saleinvoicemdy);
      saleinvoice.setHsSelectedARSubHVO(this.hsSelectedARSubHVO);
      saleinvoice.setBstrikeflag(new UFBoolean(this.bstrikeflag));

      saleinvoice.setDClientDate(this.ce.getDate());

      saleinvoice.setHsArsubAcct(this.hsSelectedARSubHVO);

      saleinvoice.setCuruserid(getClientEnvironment().getUser().getPrimaryKey());

      showHintMessage(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000080"));

      SCMEnv.out("开始保存：" + System.currentTimeMillis());
      int i;
      if (this.strState.equals("ADD")) {
        saleinvoice.setIAction(0);
        ArrayList listBillID = (ArrayList)PfUtilClient.processActionNoSendMessage(this, "PreKeep", "32", getClientEnvironment().getDate().toString(), saleinvoice, null, null, null);

        this.m_strID = listBillID.get(0).toString();
        saleinvoice.setPrimaryKey(this.m_strID);
        if (this.SO49.booleanValue()) {
          loadData(this.m_strID);
        } else {
          if (this.isGather)
            loadData(this.m_strID);
          this.code = listBillID.get(1).toString();
          ((SaleVO)saleinvoice.getParentVO()).setVreceiptcode(this.code);
          getBillCardPanel().setHeadItem("vreceiptcode", this.code);
          loadIDafterADD(listBillID);
        }
        getBillCardPanel().updateValue();
        getBillCardPanel().getBillModel().reCalcurateAll();

        for (i = 0; i < getBillCardPanel().getBillModel().getRowCount(); )
        {
          UFDouble nsubsummny = (UFDouble)getBillCardPanel().getBodyValueAt(i, "nsubsummny");

          UFDouble noriginalcursummny = (UFDouble)getBillCardPanel().getBodyValueAt(i, "noriginalcursummny");

          UFDouble nuniteinvoicemny = (nsubsummny == null ? new UFDouble(0.0D) : nsubsummny).sub(noriginalcursummny == null ? new UFDouble(0.0D) : noriginalcursummny);

          getBillCardPanel().setBodyValueAt(nuniteinvoicemny, i, "nuniteinvoicemny");

          i++;
        }

      }
      /**
       * 反馈实际抵扣金额
       * edit 彭佳佳 2018-6-13 15:06:53
       */
      String pk_corp = saleinvoice.getPk_corp();
      if(pk_corp.equals("1016") || pk_corp.equals("1071") || pk_corp.equals("1103")){
    	  StringBuffer sb = new StringBuffer();
          String cvmjson = new String();
          for (int a = 0 ;a<saleinvoice.getChildrenVO().length;a++){
        	  cvmjson = saleinvoice.getChildrenVO()[a].getAttributeValue("b_cjje1")==null?"":saleinvoice.getChildrenVO()[a].getAttributeValue("b_cjje1").toString();
        	  if(cvmjson.length()<=0){
        		  continue;
        	  }else{
        		  sb.append(cvmjson);
        	  }
          }
          if(sb.length()>0){
        	  BaoyinSaleInvoiceUI ui = new BaoyinSaleInvoiceUI();
              ui.conSaleSerivce(saleinvoice);
          } 
      }
     /**
      * end
      */
      if (this.strState.equals("MODIFY"))
      {
        saleinvoice.setIAction(1);
        saleinvoice.setAllSaleOrderVO(saleinvoicemdy);
        saleinvoice.setPrimaryKey(this.m_strID);
        saleinvoice.setOldSaleOrderVO(this.oldVO);

        ((SaleVO)saleinvoicemdy.getParentVO()).setCbiztype((String)saleinvoice.getParentVO().getAttributeValue("cbiztype"));

        ((SaleVO)this.oldVO.getParentVO()).setCbiztype((String)saleinvoice.getParentVO().getAttributeValue("cbiztype"));

        ArrayList listBillID = (ArrayList)PfUtilClient.processActionNoSendMessage(this, "PreModify", "32", getClientEnvironment().getDate().toString(), saleinvoice, null, null, null);

        getBillCardPanel().setHeadItem("vreceiptcode", listBillID.get(listBillID.size() - 1));

        listBillID.remove(listBillID.size() - 1);
        loadIDafterEDIT(listBillID);
      }

      this.oldhsSelectedARSubHVO = (this.hsSelectedARSubHVO == null ? null : (Hashtable)this.hsSelectedARSubHVO.clone());

      if (this.hsTotalBykey != null)
        this.hsTotalBykey.clear();
      if (this.oldhsTotalBykey != null) {
        this.oldhsTotalBykey.clear();
      }

      getBillCardPanel().execHeadFormulas(new String[] { "ts->getColValue(so_saleinvoice,ts,csaleid,csaleid)", "dmoditime->getColValue(so_saleinvoice,dmoditime,csaleid,csaleid)", "csalecompanyname->getColValue(bd_corp,unitname,pk_corp,pk_corp)" });

      getBillCardPanel().updateValue();
      SCMEnv.out("保存结束：" + System.currentTimeMillis());
      showHintMessage(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000083"));

      this.strState = "SAVE";
      this.isGather = false;
      setButtonsState();

      for (i = 0; i < getBillCardPanel().getRowCount(); i++) {
        getBillCardPanel().setCellEditable(i, "cpackunitname", false);
        getBillCardPanel().setCellEditable(i, "npacknumber", false);
      }

      initRef();

      this.m_isCodeChanged = false;

      ((UIRefPane)getBillCardPanel().getHeadItem("cbiztypename").getComponent()).setPK(getBillCardPanel().getBusiType());

      getBillCardPanel().updateUI();
      getBillCardPanel().getBillModel().reCalcurateAll();

      reloadList();
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

  public void setButtons(ButtonObject bo)
  {
    PfUtilClient.retAddBtn(this.m_boAdd, getCorpPrimaryKey(), "32", bo);

    if (this.sVerifyrule.equals("W")) {
      this.m_boAdd.addChildButton(this.m_boGather);
    }

    setButtons(this.aryButtonGroup);
  }

  public void setButtonsState()
  {
    int iRow = -1;
    if (getBillListPanel() != null) {
      iRow = getBillListPanel().getHeadTable().getRowCount();
    }
    if (this.strState.equals("FREE")) {
      this.m_boBusiType.setEnabled(true);
      this.m_boAdd.setEnabled(true);
      this.m_boModify.setEnabled(false);
      this.m_boCancel.setEnabled(false);
      this.m_boSave.setEnabled(false);
      this.m_boLineOper.setEnabled(false);
      this.m_boCheck.setEnabled(false);
      this.m_boUnAprove.setEnabled(false);
      this.m_boBlankOut.setEnabled(true);
      this.m_boAtp.setEnabled(true);
      this.m_boCustInfo.setEnabled(true);
      this.m_boCustCredit.setEnabled(true);
      this.m_boPrifit.setEnabled(true);
      this.m_boInvoiceExecRpt.setEnabled(false);
      this.m_boAssistant.setEnabled(true);
      this.m_boSoTax.setEnabled(false);
      this.m_boPrint.setEnabled(false);
      this.m_boPreview.setEnabled(false);
      this.m_boAction.setEnabled(false);
      this.m_boDocument.setEnabled(false);
      this.boOrderQuery.setEnabled(false);

      this.m_boUnite.setEnabled(false);
      this.m_boUniteCancel.setEnabled(false);

      this.m_boUniteInvoice.setEnabled(false);
      this.m_boUnite.setEnabled(false);

      this.m_boUniteCancel.setEnabled(false);

      int iStatus = -1;
      if ((getBillCardPanel().getHeadItem("fstatus").getValue() != null) && (!getBillCardPanel().getHeadItem("fstatus").getValue().equals("")))
      {
        iStatus = Integer.parseInt(getBillCardPanel().getHeadItem("fstatus").getValue());
      }

      setState(iStatus);

      int row = getBillCardPanel().getBillTable().getSelectedRow();
      Object cfreezeid = null;
      if ((row >= 0) && (getBillCardPanel().getRowCount() > 0))
        cfreezeid = getBillCardPanel().getBodyValueAt(row, "cfreezeid");
      getBillCardPanel().setEnabled(false);

      this.boSendAudit.setEnabled(true);
      if (iRow > 0)
        this.m_boReturn.setEnabled(true);
      else {
        this.m_boReturn.setEnabled(false);
      }
    }
    if (this.strState.equals("BUSITYPE")) {
      this.m_boAdd.setEnabled(true);
      this.m_boModify.setEnabled(false);
      this.m_boCancel.setEnabled(false);
      this.m_boSave.setEnabled(false);
      this.m_boLineOper.setEnabled(false);
      this.m_boCheck.setEnabled(false);
      this.m_boPrint.setEnabled(false);
      this.m_boPreview.setEnabled(false);
      this.m_boBusiType.setEnabled(true);
      this.m_boAction.setEnabled(false);
      this.m_boDocument.setEnabled(false);
      this.m_boUnAprove.setEnabled(false);
      this.m_boBlankOut.setEnabled(false);
      this.m_boAssistant.setEnabled(false);
      this.m_boAtp.setEnabled(false);
      this.m_boCustInfo.setEnabled(false);
      this.m_boCustCredit.setEnabled(false);
      this.m_boPrifit.setEnabled(false);
      this.m_boInvoiceExecRpt.setEnabled(false);
      this.m_boSoTax.setEnabled(false);
      this.boOrderQuery.setEnabled(false);

      this.m_boUnite.setEnabled(false);
      this.m_boUniteCancel.setEnabled(false);
      getBillCardPanel().setEnabled(false);

      this.boSendAudit.setEnabled(false);

      this.m_boUniteInvoice.setEnabled(false);
      if (iRow > 0)
        this.m_boReturn.setEnabled(true);
      else {
        this.m_boReturn.setEnabled(false);
      }
    }
    if (this.strState.equals("APPROVE")) {
      this.m_boAdd.setEnabled(true);
      this.m_boModify.setEnabled(false);
      this.m_boCancel.setEnabled(false);
      this.m_boSave.setEnabled(false);
      this.m_boLineOper.setEnabled(false);
      this.m_boCheck.setEnabled(false);
      this.m_boPrint.setEnabled(true);
      this.m_boPreview.setEnabled(true);
      this.m_boBusiType.setEnabled(true);
      this.m_boAction.setEnabled(false);
      this.m_boAfterAction.setEnabled(true);
      this.m_boDocument.setEnabled(true);
      this.boOrderQuery.setEnabled(true);

      this.m_boUnite.setEnabled(false);
      this.m_boUniteCancel.setEnabled(false);
      this.m_boUnAprove.setEnabled(true);
      this.m_boBlankOut.setEnabled(false);
      this.m_boAssistant.setEnabled(true);
      this.m_boAtp.setEnabled(true);
      this.m_boCustInfo.setEnabled(true);
      this.m_boCustCredit.setEnabled(true);
      this.m_boPrifit.setEnabled(true);
      this.m_boInvoiceExecRpt.setEnabled(false);
      this.m_boSoTax.setEnabled(true);

      getBillCardPanel().setEnabled(false);

      this.boSendAudit.setEnabled(false);

      this.m_boUniteInvoice.setEnabled(false);
      if (iRow > 0)
        this.m_boReturn.setEnabled(true);
      else {
        this.m_boReturn.setEnabled(false);
      }
    }
    if (this.strState.equals("ADD")) {
      this.m_boAdd.setEnabled(false);
      this.m_boModify.setEnabled(false);
      this.m_boCancel.setEnabled(true);
      this.m_boLineOper.setEnabled(!this.bstrikeflag);
      this.m_boSave.setEnabled(true);
      this.m_boCheck.setEnabled(false);
      this.m_boPrint.setEnabled(false);
      this.m_boPreview.setEnabled(false);
      this.m_boBusiType.setEnabled(false);
      this.m_boAction.setEnabled(false);
      this.m_boAfterAction.setEnabled(false);
      this.m_boDocument.setEnabled(false);
      this.boOrderQuery.setEnabled(false);

      this.m_boUnite.setEnabled(false);
      this.m_boUniteCancel.setEnabled(false);
      this.m_boUnAprove.setEnabled(false);
      this.m_boBlankOut.setEnabled(false);
      this.m_boAssistant.setEnabled(false);
      this.m_boAtp.setEnabled(false);
      this.m_boCustInfo.setEnabled(false);
      this.m_boCustCredit.setEnabled(false);
      this.m_boPrifit.setEnabled(false);
      this.m_boInvoiceExecRpt.setEnabled(false);
      this.m_boSoTax.setEnabled(false);
      getBillCardPanel().setEnabled(true);

      this.boSendAudit.setEnabled(false);

      UFDouble ntotalsummny = new UFDouble(getBillCardPanel().getHeadItem("ntotalsummny").getValue());

      boolean bislgtzero = true;
      if ((ntotalsummny != null) && (ntotalsummny.doubleValue() < 0.0D))
        bislgtzero = false;
      this.m_boUnite.setEnabled(bislgtzero);
      this.m_boUniteInvoice.setEnabled(bislgtzero);
      this.m_boUniteCancel.setEnabled((this.bstrikeflag) && (bislgtzero));

      this.m_boReturn.setEnabled(false);
    }
    if (this.strState.equals("MODIFY")) {
      this.m_boAdd.setEnabled(false);
      this.m_boModify.setEnabled(false);
      this.m_boCancel.setEnabled(true);
      this.m_boSave.setEnabled(true);
      this.m_boLineOper.setEnabled(!this.bstrikeflag);
      this.m_boCheck.setEnabled(false);
      this.m_boPrint.setEnabled(false);
      this.m_boPreview.setEnabled(false);
      this.m_boBusiType.setEnabled(false);
      this.m_boAction.setEnabled(false);
      this.m_boAfterAction.setEnabled(false);
      this.m_boDocument.setEnabled(false);
      this.boOrderQuery.setEnabled(false);

      this.m_boUnite.setEnabled(false);
      this.m_boUniteCancel.setEnabled(false);
      this.m_boUnAprove.setEnabled(false);
      this.m_boBlankOut.setEnabled(false);
      this.m_boAssistant.setEnabled(false);
      this.m_boAtp.setEnabled(false);
      this.m_boCustInfo.setEnabled(false);
      this.m_boCustCredit.setEnabled(false);
      this.m_boPrifit.setEnabled(false);
      this.m_boInvoiceExecRpt.setEnabled(false);
      this.m_boSoTax.setEnabled(false);
      getBillCardPanel().setEnabled(true);

      this.boSendAudit.setEnabled(false);

      UFDouble ntotalsummny = new UFDouble(getBillCardPanel().getHeadItem("ntotalsummny").getValue());

      boolean bislgtzero = true;
      if ((ntotalsummny != null) && (ntotalsummny.doubleValue() < 0.0D))
        bislgtzero = false;
      this.m_boUnite.setEnabled(bislgtzero);
      this.m_boUniteInvoice.setEnabled(bislgtzero);
      this.m_boUniteCancel.setEnabled((this.bstrikeflag) && (bislgtzero));

      this.m_boReturn.setEnabled(false);
    }
    if (this.strState.equals("SAVE")) {
      this.m_boAdd.setEnabled(true);
      this.m_boModify.setEnabled(true);
      this.m_boCancel.setEnabled(false);
      this.m_boSave.setEnabled(false);
      this.m_boLineOper.setEnabled(false);
      this.m_boCheck.setEnabled(true);
      this.m_boPrint.setEnabled(true);
      this.m_boPreview.setEnabled(true);
      this.m_boBusiType.setEnabled(true);
      this.m_boAction.setEnabled(true);
      this.m_boAfterAction.setEnabled(false);
      this.m_boDocument.setEnabled(true);
      this.boOrderQuery.setEnabled(true);

      this.m_boUnite.setEnabled(false);
      this.m_boUniteCancel.setEnabled(false);
      this.m_boUnAprove.setEnabled(false);
      this.m_boBlankOut.setEnabled(true);
      this.m_boAssistant.setEnabled(true);
      this.m_boAtp.setEnabled(true);
      this.m_boCustInfo.setEnabled(true);
      this.m_boCustCredit.setEnabled(true);
      this.m_boPrifit.setEnabled(true);
      this.m_boInvoiceExecRpt.setEnabled(false);
      this.m_boSoTax.setEnabled(false);
      getBillCardPanel().setEnabled(false);

      this.boSendAudit.setEnabled(true);

      this.m_boUniteInvoice.setEnabled(false);
      if (iRow > 0)
        this.m_boReturn.setEnabled(true);
      else {
        this.m_boReturn.setEnabled(false);
      }
    }
    if (this.strState.equals("LIST")) {
      this.m_boModify.setEnabled(true);
      this.m_boCancel.setEnabled(true);
    }

    updateButtons();
  }

  public void setHeadComboBox()
  {
    try
    {
      UIRefPane refCust = (UIRefPane)getBillCardPanel().getHeadItem("ccustomername").getComponent();

      refCust.getRefModel().addWherePart(" AND bd_cumandoc.frozenflag!='Y' ");

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
      for (int i = 0; i < invoiceType.length; i++)
        comtype.addItem(invoiceType[i][1]);
      comtype.setSelectedIndex(0);

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

  private void setState(int iState)
  {
    switch (iState) {
    case 2:
      this.m_boCheck.setEnabled(false);
      this.m_boModify.setEnabled(false);
      this.m_boAfterAction.setEnabled(true);
      this.m_boDocument.setEnabled(true);
      this.boOrderQuery.setEnabled(true);

      this.boSendAudit.setEnabled(false);
      break;
    case 1:
      this.m_boAction.setEnabled(true);
      this.m_boCheck.setEnabled(true);
      this.m_boModify.setEnabled(false);
      this.m_boAfterAction.setEnabled(false);
      this.m_boDocument.setEnabled(false);
      this.boOrderQuery.setEnabled(false);

      this.boSendAudit.setEnabled(true);
      break;
    case 5:
      this.m_boModify.setEnabled(false);
      this.m_boAction.setEnabled(false);
      this.m_boAssistant.setEnabled(false);
      this.m_boAfterAction.setEnabled(false);
      this.m_boPrint.setEnabled(false);
      this.m_boDocument.setEnabled(false);
      this.boOrderQuery.setEnabled(false);
      this.boSendAudit.setEnabled(false);
    case 3:
    case 4:
    }

    setExtendBtnsStat(iState);

    updateButtons();
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

  private void setExchgEditBycurrency(String ccurrencytypeid)
    throws BusinessException
  {
    if ((this.BD302 == null) || (!this.BD302.booleanValue()))
    {
      getBillCardPanel().setHeadItem("nexchangeotobrate", this.currtype.getRate(ccurrencytypeid, null, getBillCardPanel().getHeadItem("dbilldate").getValue()));

      if (this.currtype.isLocalCurrType(ccurrencytypeid)) {
        getBillCardPanel().getHeadItem("nexchangeotobrate").setEnabled(false);
      }
      else {
        getBillCardPanel().getHeadItem("nexchangeotobrate").setEnabled(true);
      }

      getBillCardPanel().getHeadItem("nexchangeotoarate").setEnabled(false);
    }
    else if (this.currtype.isFracCurrType(ccurrencytypeid)) {
      getBillCardPanel().getHeadItem("nexchangeotoarate").setEnabled(false);

      getBillCardPanel().getHeadItem("nexchangeotobrate").setEnabled(true);
    }
    else if (this.currtype.isLocalCurrType(ccurrencytypeid))
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

  public void afterCurrencyChange(String billdate)
  {
    UIRefPane ccurrencytypeid = (UIRefPane)getBillCardPanel().getHeadItem("ccurrencyid").getComponent();

    if (ccurrencytypeid.getRefPK() == null)
      return;
    try
    {
      setExchgEditBycurrency(ccurrencytypeid.getRefPK());

      if ((this.BD302 != null) && (this.BD302.booleanValue()))
      {
        UFDouble[] rates = getExchangerate(ccurrencytypeid.getRefPK(), billdate);
        UFDouble dCurr0 = rates[0];
        UFDouble dCurr1 = rates[1];

        getBillCardPanel().setHeadItem("nexchangeotobrate", dCurr0);
        getBillCardPanel().setHeadItem("nexchangeotoarate", dCurr1 == null ? new UFDouble(0) : dCurr1);

        SCMEnv.out("折本汇率：" + dCurr0);
        SCMEnv.out("折辅汇率：" + dCurr1);
      }

    }
    catch (Exception e1)
    {
      SCMEnv.out("获得汇率失败！");
      e1.printStackTrace();
    }
  }

  public ButtonObject[] getBillButtons()
  {
    return this.aryButtonGroup;
  }

  protected void initCurrency()
  {
    try
    {
      this.currtype = new BusinessCurrencyRateUtil(this.pk_corp);
    } catch (Exception exp) {
      exp.printStackTrace();
      throw new BusinessRuntimeException(exp.getMessage());
    }
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
        if ((oldbodyVO.getNnumber() == null) || (oldbodyVO.getNnumber().doubleValue() == 0.0D))
        {
          throw new ValidationException(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000134"));
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
    UIComboBox comBatch = (UIComboBox)getBillCardPanel().getBodyItem("fbatchstatus").getComponent();

    getBillCardPanel().getBodyItem("fbatchstatus").setWithIndex(true);
    comBatch.setTranslate(true);
    comBatch.addItem(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000340"));

    comBatch.addItem(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000086"));

    comBatch.addItem(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000087"));

    UIComboBox comItem = (UIComboBox)getBillCardPanel().getBodyItem("frowstatus").getComponent();

    getBillCardPanel().getBodyItem("frowstatus").setWithIndex(true);
    comItem.setTranslate(true);
    comItem.addItem("");
    comItem.addItem(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000340"));

    comItem.addItem(NCLangRes.getInstance().getStrByID("common", "UC001-0000027"));

    comItem.addItem("");
    comItem.addItem("");
    comItem.addItem(NCLangRes.getInstance().getStrByID("common", "UC001-0000005"));
  }

  public void setBodyRowState(int row)
  {
    if (getBillCardPanel().getBillModel().getRowState(row) != 1)
      getBillCardPanel().getBillModel().setRowState(row, 2);
  }

  public void setCardPanel(BillData bdData)
  {
    setCardPanelByPara(bdData);

    UIRefPane refHeadAddress = (UIRefPane)bdData.getHeadItem("vreceiveaddress").getComponent();

    refHeadAddress.setAutoCheck(false);
    refHeadAddress.setReturnCode(true);
  }

  protected void setCardPanelByPara(BillData bdData)
  {
    bdData.getBodyItem("blargessflag").setEdit(false);

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
      bdData.getBodyItem("nquotenumber").setDecimalDigits(this.BD501.intValue());
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

      bdData.getBodyItem("norgviapricetax").setDecimalDigits(this.BD505.intValue());

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

  public void afterCreceiptcorpEdit(BillEditEvent e)
  {
    UIRefPane vreceiveaddress = (UIRefPane)getBillCardPanel().getHeadItem("vreceiveaddress").getComponent();

    vreceiveaddress.setAutoCheck(false);

    ((CustAddrRefModel)vreceiveaddress.getRefModel()).setCustId(getBillCardPanel().getHeadItem("ccustomername").getValue());

    String formula = "getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,ccustomername)";
    String pk_cubasdoc = (String)getBillCardPanel().execHeadFormula(formula);

    String strvreceiveaddress = nc.ui.so.so001.panel.bom.BillTools.getColValue2("bd_custaddr", "pk_custaddr", "defaddrflag", "Y", "pk_cubasdoc", pk_cubasdoc);

    vreceiveaddress.setPK(strvreceiveaddress);
  }

  public void afterCurrencyEdit(BillEditEvent event)
  {
    UIRefPane ccurrencytypeid = (UIRefPane)getBillCardPanel().getHeadItem("ccurrencyid").getComponent();

    getBillCardPanel().setHeadItem("nexchangeotobrate", null);
    getBillCardPanel().setHeadItem("nexchangeotoarate", null);
    getBillCardPanel().getHeadItem("nexchangeotoarate").setEnabled(true);
    try
    {
      setPanelByCurrency(ccurrencytypeid.getRefPK());
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
      else {
        UFDouble dCurr0 = this.currtype.getRate(ccurrencytypeid.getRefPK(), this.currtype.getLocalCurrPK(), getBillCardPanel().getHeadItem("dbilldate").getValue());

        UFDouble dCurr1 = this.currtype.getRate(ccurrencytypeid.getRefPK(), this.currtype.getFracCurrPK(), getBillCardPanel().getHeadItem("dbilldate").getValue());

        getBillCardPanel().setHeadItem("nexchangeotobrate", dCurr0);
        getBillCardPanel().setHeadItem("nexchangeotoarate", dCurr1 == null ? new UFDouble(0) : dCurr1);

        SCMEnv.out("折本汇率：" + dCurr0);
        SCMEnv.out("折辅汇率：" + dCurr1);

        if (this.currtype.isFracCurrType(ccurrencytypeid.getRefPK())) {
          getBillCardPanel().getHeadItem("nexchangeotoarate").setEnabled(false);

          getBillCardPanel().getHeadItem("nexchangeotobrate").setEnabled(true);
        }
        else if (this.currtype.isLocalCurrType(ccurrencytypeid.getRefPK()))
        {
          getBillCardPanel().getHeadItem("nexchangeotobrate").setEnabled(false);

          getBillCardPanel().getHeadItem("nexchangeotoarate").setEnabled(false);
        }
        else {
          getBillCardPanel().getHeadItem("nexchangeotobrate").setEnabled(true);

          getBillCardPanel().getHeadItem("nexchangeotoarate").setEnabled(true);
        }

      }

      for (int i = 0; i < getBillCardPanel().getRowCount(); i++)
      {
        getBillCardPanel().setBodyValueAt(ccurrencytypeid.getRefPK(), i, "ccurrencytypeid");

        getBillCardPanel().execBodyFormulas(i, getBillCardPanel().getBodyItem("ccurrencytypeid").getLoadFormula());

        getBillCardPanel().setBodyValueAt(getBillCardPanel().getHeadItem("nexchangeotobrate").getValue(), i, "nexchangeotobrate");

        getBillCardPanel().setBodyValueAt(getBillCardPanel().getHeadItem("nexchangeotoarate").getValue(), i, "nexchangeotoarate");

        getBillCardPanel().setBodyValueAt(null, i, "npacknumber");
        getBillCardPanel().setBodyValueAt(null, i, "nnumber");

        getBillCardPanel().setBodyValueAt(null, i, "noriginalcurtaxprice");

        getBillCardPanel().setBodyValueAt(null, i, "noriginalcurprice");
        getBillCardPanel().setBodyValueAt(null, i, "noriginalcurnetprice");

        getBillCardPanel().setBodyValueAt(null, i, "noriginalcurtaxnetprice");

        getBillCardPanel().setBodyValueAt(null, i, "noriginalcurtaxmny");

        getBillCardPanel().setBodyValueAt(null, i, "noriginalcurmny");
        getBillCardPanel().setBodyValueAt(null, i, "noriginalcursummny");

        getBillCardPanel().setBodyValueAt(null, i, "noriginalcurdiscountmny");

        getBillCardPanel().setBodyValueAt(null, i, "nprice");
        getBillCardPanel().setBodyValueAt(null, i, "ntaxprice");
        getBillCardPanel().setBodyValueAt(null, i, "nnetprice");
        getBillCardPanel().setBodyValueAt(null, i, "ntaxnetprice");
        getBillCardPanel().setBodyValueAt(null, i, "ntaxmny");
        getBillCardPanel().setBodyValueAt(null, i, "nmny");
        getBillCardPanel().setBodyValueAt(null, i, "nsummny");
        getBillCardPanel().setBodyValueAt(null, i, "ndiscountmny");
        getBillCardPanel().setBodyValueAt(null, i, "nassistcurdiscountmny");

        getBillCardPanel().setBodyValueAt(null, i, "nassistcursummny");
        getBillCardPanel().setBodyValueAt(null, i, "nassistcurmny");
        getBillCardPanel().setBodyValueAt(null, i, "nassistcurtaxmny");
        getBillCardPanel().setBodyValueAt(null, i, "nassistcurtaxnetprice");

        getBillCardPanel().setBodyValueAt(null, i, "nassistcurnetprice");

        getBillCardPanel().setBodyValueAt(null, i, "nassistcurtaxprice");

        getBillCardPanel().setBodyValueAt(null, i, "nassistcurprice");

        if (getBillCardPanel().getBillModel().getRowState(i) == 0)
          getBillCardPanel().getBillModel().setRowState(i, 2);
      }
    }
    catch (Exception e1) {
      SCMEnv.out("获得汇率失败！");
      e1.printStackTrace();
    }
  }

  public void afterCustomerEdit(BillEditEvent e)
  {
    UIRefPane ref = (UIRefPane)getBillCardPanel().getHeadItem("ccustomername").getComponent();

    if (ref != null)
    {
      this.custID = ref.getRefPK();
      if (this.custID == null) {
        this.custID = getBillCardPanel().getHeadItem("creceiptcorpid").getValue();
      }
      getBillCardPanel().setHeadItem("creceiptcorpid", this.custID);
      ((UIRefPane)getBillCardPanel().getHeadItem("ccustomername").getComponent()).setPK(this.custID);
      try
      {
        String[][] results = SaleinvoiceBO_Client.getCustomerInfo(this.custID);

        if (results != null) {
          ((CustBankRefModel)((UIRefPane)getBillCardPanel().getHeadItem("ccustomerbank").getComponent()).getRefModel()).setCondition(results[0][5]);

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
          getBillCardPanel().setHeadItem("ccustbankid", null);
          ref.getRefModel().clearData();
          getBillCardPanel().setHeadItem("ccustomerbank", null);
          getBillCardPanel().setHeadItem("ccustomerbankNo", null);
        }
      } catch (Exception e1) {
        SCMEnv.out("查询失败！");
        e1.printStackTrace(System.out);
      }
    }

    afterCreceiptcorpEdit(e);

    UIRefPane cemployeeid = (UIRefPane)getBillCardPanel().getHeadItem("cemployeeid").getComponent();

    if (cemployeeid != null)
      cemployeeid.getRefModel().setWherePart(this.sEmployeeRefCondition);
    Vector vecTemp = new Vector();

    if ((getBillCardPanel().getHeadItem("cdeptid").getValue() == null) || (getBillCardPanel().getHeadItem("cdeptid").getValue().toString().equals("")))
    {
      vecTemp.add(new String("cdeptid->getColValue(bd_cumandoc,pk_respdept1,pk_cumandoc,creceiptcorpid)"));
    }

    vecTemp.add(new String("cemployeeid->getColValue(bd_cumandoc,pk_resppsn1,pk_cumandoc,creceiptcorpid)"));

    vecTemp.add(new String("ndiscountrate->getColValue(bd_cumandoc,discountrate,pk_cumandoc,creceiptcorpid)"));

    String oldccurrencyid = getBillCardPanel().getHeadItem("ccurrencyid").getValue();

    if ((oldccurrencyid == null) || (oldccurrencyid.equals("")))
    {
      vecTemp.add(new String("ccurrencyid->getColValue(bd_cumandoc,pk_currtype1,pk_cumandoc,creceiptcorpid)"));
    }

    if ((getBillCardPanel().getHeadItem("ccalbodyid").getValue() == null) || (getBillCardPanel().getHeadItem("ccalbodyid").getValue().toString().equals("")))
    {
      vecTemp.add(new String("ccalbodyid->getColValue(bd_cumandoc,pk_calbody,pk_cumandoc,creceiptcorpid)"));
    }

    if ((getBillCardPanel().getHeadItem("csalecorpid").getValue() == null) || (getBillCardPanel().getHeadItem("csalecorpid").getValue().toString().equals("")))
    {
      vecTemp.add(new String("csalecorpid->getColValue(bd_cumandoc,pk_salestru,pk_cumandoc,creceiptcorpid)"));
    }

    if (vecTemp.size() > 0) {
      String[] formulas = new String[vecTemp.size()];
      vecTemp.copyInto(formulas);
      getBillCardPanel().execHeadFormulas(formulas);
    }

    String ndiscountrate = getBillCardPanel().getHeadItem("ndiscountrate").getValue();

    if ((ndiscountrate == null) || (ndiscountrate.trim().length() == 0)) {
      getBillCardPanel().setHeadItem("ndiscountrate", new UFDouble(100));
    }

    String formula = "getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,creceiptcorpid)";
    String pk_cubasdoc = (String)getBillCardPanel().execHeadFormula(formula);

    formula = "getColValue(bd_calbody,bodyname,pk_calbody,ccalbodyid)";
    String calbodyname = (String)getBillCardPanel().execHeadFormula(formula);

    getBillCardPanel().setHeadItem("ccalbodyname", calbodyname);

    formula = "getColValue(bd_salestru,vsalestruname,csalestruid,csalecorpid)";
    String vsalestruname = (String)getBillCardPanel().execHeadFormula(formula);

    getBillCardPanel().setHeadItem("csalecorpname", vsalestruname);

    formula = "getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,creceiptcorpid)";
    String custID = (String)getBillCardPanel().execHeadFormula(formula);
    formula = "bfreecustflag->getColValue(bd_cubasdoc,freecustflag,pk_cubasdoc,\"" + custID + "\")";

    getBillCardPanel().getBillData().execHeadFormula(formula);
    if (getBillCardPanel().getHeadItem("bfreecustflag").getValue().equals("false"))
    {
      getBillCardPanel().getHeadItem("cfreecustid").setEnabled(false);
      getBillCardPanel().getHeadItem("cfreecustid").setValue(null);
      getBillCardPanel().getHeadItem("cfreecustid").setEdit(false);
    } else {
      getBillCardPanel().getHeadItem("cfreecustid").setEnabled(true);
      getBillCardPanel().getHeadItem("cfreecustid").setEdit(true);
    }
    try
    {
      UIRefPane ccurrencytypeid = (UIRefPane)getBillCardPanel().getHeadItem("ccurrencyid").getComponent();

      if (ccurrencytypeid.getRefPK() == null)
        ccurrencytypeid.setPK(this.currtype.getLocalCurrPK());
      if (getBillCardPanel().getHeadItem("ndiscountrate").getValue() == null)
        getBillCardPanel().getHeadItem("ndiscountrate").setValue("100");
    } catch (Exception e1) {
      SCMEnv.out(e1.getMessage());
    }

    afterDeptEdit(null);
    String newccurrencyid = getBillCardPanel().getHeadItem("ccurrencyid").getValue();

    if ((oldccurrencyid == null) && (newccurrencyid != null))
      afterCurrencyEdit(null);
  }

  public void afterDeptEdit(BillEditEvent e)
  {
    UIRefPane cemployeeid = (UIRefPane)getBillCardPanel().getHeadItem("cemployeeid").getComponent();

    if (e != null) {
      cemployeeid.setPK(null);
    }
    UIRefPane cdeptid = (UIRefPane)getBillCardPanel().getHeadItem("cdeptid").getComponent();

    if (cdeptid != null)
      if (cdeptid.getRefPK() != null) {
        if ((cdeptid.getRefPK() != null) && (cdeptid.getRefPK().length() != 0))
        {
          cemployeeid.getRefModel().addWherePart(" and bd_psndoc.pk_deptdoc = '" + cdeptid.getRefPK() + "'");
        }
        else
        {
          cemployeeid.getRefModel().setWherePart(this.sEmployeeRefCondition);
        }
      }
      else
        initSalePeopleRef();
  }

  public void afterEmployeeEdit(BillEditEvent e)
  {
    UIRefPane cemployeeid = (UIRefPane)getBillCardPanel().getHeadItem("cemployeeid").getComponent();

    UIRefPane cdeptid = (UIRefPane)getBillCardPanel().getHeadItem("cdeptid").getComponent();

    if ((cemployeeid != null) && (cdeptid != null) && 
      (cemployeeid.getRefPK() != null)) {
      Object pk = cemployeeid.getRefValue("bd_deptdoc.pk_deptdoc");
      if ((pk != null) && (pk.toString().trim().length() > 0))
        cdeptid.setPK(pk);
    }
  }

  private void afterNumberEdit(int row, String key)
  {
    nc.ui.so.so001.panel.bom.BillTools.calcUnitNum(row, getBillCardPanel().getBillModel(), key, getBillCardPanel().getBillType());

    calculateNumber(row, key);

    UFDouble dbTemp = computeViaPrice(row);
    if (dbTemp != null) {
      getBillCardPanel().getBillModel().setValueAt(dbTemp, row, "norgviaprice");
    }

    dbTemp = computeViaPriceTax(row);
    if (dbTemp != null)
      getBillCardPanel().getBillModel().setValueAt(dbTemp, row, "norgviapricetax");
  }

  public void afterWarehouseEdit(BillEditEvent e)
  {
    String[] formulas = new String[1];

    formulas[0] = "cbodywarehousename->getColValue(bd_stordoc,storname,pk_stordoc,cbodywarehouseid)";
    getBillCardPanel().execBodyFormulas(e.getRow(), formulas);
  }

  public void beforeUnitChange(int row)
  {
    UIRefPane cpackunitname = (UIRefPane)getBillCardPanel().getBodyItem("cpackunitname").getComponent();

    String cinvbasdocid = (String)getBillCardPanel().getBodyValueAt(row, "cinvbasdocid");

    String cpackunitid = (String)getBillCardPanel().getBodyValueAt(row, "cpackunitid");

    String cunitid = (String)getBillCardPanel().getBodyValueAt(row, "cunitid");

    cpackunitname.setWhereString(" (pk_measdoc in (select pk_measdoc from bd_convert where pk_invbasdoc = '" + cinvbasdocid + "') or pk_measdoc='" + cunitid + "') ");
  }

  public UFDouble calcurateTotal(String key)
  {
    UFDouble total = new UFDouble(0.0D);
    UFDouble dbTemp = null;
    for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
      if ((getBillCardPanel().getBodyValueAt(i, "blargessflag") == null) || (getBillCardPanel().getBodyValueAt(i, "blargessflag").equals(new Boolean(false))))
      {
        Object value = getBillCardPanel().getBodyValueAt(i, key);
        if (value != null) {
          if (value.getClass() == UFDouble.class) {
            total = total.add((UFDouble)value);
          } else {
            String v = (value == null) || (value.equals("")) ? "0" : value.toString();

            total = total.add(new UFDouble(v));
          }
        }
      }
      if (key.equals("norgviaprice"))
      {
        dbTemp = computeViaPrice(i);
        if (dbTemp != null) {
          getBillCardPanel().getBillModel().setValueAt(dbTemp, i, "norgviaprice");
        }
      }

      if (!key.equals("norgviapricetax"))
        continue;
      dbTemp = computeViaPriceTax(i);
      if (dbTemp != null) {
        getBillCardPanel().getBillModel().setValueAt(dbTemp, i, "norgviapricetax");
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

  private UFDouble computeViaPrice1(int iRow)
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

  private UFDouble computeViaPriceTax1(int iRow)
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

  public String getBillCode()
  {
    return "32";
  }

  private SaleVO[] getAddListHeadValues() {
    SaleVO[] aryAddListHeads = new SaleVO[this.alBills.size()];
    for (int i = 0; i < this.alBills.size(); i++) {
      aryAddListHeads[i] = new SaleVO();
      aryAddListHeads[i] = ((SaleVO)((SaleinvoiceVO)this.alBills.get(i)).getParentVO());
    }

    return aryAddListHeads;
  }

  public BillFormulaContainer getFormulaBillContainer()
  {
    if (this.m_billFormulaContain == null) {
      this.m_billFormulaContain = new BillFormulaContainer(getBillCardPanel(), false, true, 1, "fixedflag", "scalefactor", "nnumber", "npacknumber", null, null);
    }

    return this.m_billFormulaContain;
  }

  protected ArrayList getFormulaItemBody()
  {
    ArrayList arylistItemField = new ArrayList();

    String[] aryItemField1 = { "pk_invbasdoc", "cinvbasdocid", "cinventoryid" };

    arylistItemField.add(aryItemField1);

    String[] aryItemField2 = { "invcode", "cinventorycode", "cinvbasdocid" };

    arylistItemField.add(aryItemField2);

    String[] aryItemField3 = { "invname", "cinventoryname", "cinvbasdocid" };

    arylistItemField.add(aryItemField3);

    String[] aryItemField4 = { "pk_measdoc", "cunitid", "cinvbasdocid" };

    arylistItemField.add(aryItemField4);

    String[] aryItemField5 = { "measname", "cunitname", "cunitid" };

    arylistItemField.add(aryItemField5);

    String[] aryItemField6 = { "discountflag", "discountflag", "cinvbasdocid" };

    arylistItemField.add(aryItemField6);

    String[] aryItemField7 = { "assistunit", "assistunit", "cinvbasdocid" };

    arylistItemField.add(aryItemField7);

    String[] aryItemField8 = { "measname", "cpackunitname", "cpackunitid" };

    arylistItemField.add(aryItemField8);

    String[] aryItemField9 = { "storname", "cbodywarehousename", "cbodywarehouseid" };

    arylistItemField.add(aryItemField9);

    String[] aryItemField10 = { "pk_cubasdoc", "creceiptcorpname", "creceiptcorpid" };

    arylistItemField.add(aryItemField10);

    String[] aryItemField11 = { "custname", "creceiptcorpname", "creceiptcorpname" };

    arylistItemField.add(aryItemField11);

    String[] aryItemField12 = { "currtypename", "ccurrencytypename", "ccurrencytypeid" };

    arylistItemField.add(aryItemField12);

    String[] aryItemField13 = { "pk_jobbasfil", "pk_jobbasfil", "cprojectid" };

    arylistItemField.add(aryItemField13);

    String[] aryItemField14 = { "jobname", "cprojectname", "pk_jobbasfil" };

    arylistItemField.add(aryItemField14);

    String[] aryItemField15 = { "pk_jobphase", "pk_jobphase", "cprojectphaseid" };

    arylistItemField.add(aryItemField15);

    String[] aryItemField16 = { "jobphasename", "cprojectphasename", "pk_jobphase" };

    arylistItemField.add(aryItemField16);

    String[] aryItemField17 = { "bodyname", "cadvisecalbodyname", "cadvisecalbodyid" };

    arylistItemField.add(aryItemField17);

    String[] aryItemField18 = { "measname", "cquoteunitname", "cquoteunitid" };

    arylistItemField.add(aryItemField18);

    String[] aryItemField19 = { "prodlinename", "cprolinename", "cprolineid" };

    return arylistItemField;
  }

  protected ArrayList getFormulaItemHeader()
  {
    ArrayList arylistHeadField = new ArrayList();

    String[] aryItemField1 = { "deptname", "cdeptname", "cdeptid" };

    arylistHeadField.add(aryItemField1);

    String[] aryItemField2 = { "psnname", "cemployeename", "cemployeeid" };

    arylistHeadField.add(aryItemField2);

    return arylistHeadField;
  }

  public String getNodeCode()
  {
    return "40060501";
  }

  public String getRowNoItemKey()
  {
    return "crowno";
  }

  public SourceBillFlowDlg getSourceDlg()
  {
    SourceBillFlowDlg soureDlg = new SourceBillFlowDlg(this, "32", this.m_strID, getBillCardPanel().getBusiType(), getBillCardPanel().getOperator(), getBillCardPanel().getCorp());

    return soureDlg;
  }

  public AggregatedValueObject getVo()
    throws Exception
  {
    return getVO();
  }

  public AggregatedValueObject getVO(boolean needRemove)
  {
    SaleinvoiceVO saleinvoice = null;
    saleinvoice = (SaleinvoiceVO)getBillCardPanel().getBillValueVO(SaleinvoiceVO.class.getName(), SaleVO.class.getName(), SaleinvoiceBVO.class.getName());

    ((SaleVO)saleinvoice.getParentVO()).setCreceipttype("32");

    if (needRemove) {
      SaleinvoiceBVO[] itemVOs = (SaleinvoiceBVO[])(SaleinvoiceBVO[])saleinvoice.getChildrenVO();

      int indexSelected = -1;
      indexSelected = getBillCardPanel().getBillTable().getSelectedRow();
      if ((indexSelected > -1) && (indexSelected < itemVOs.length)) {
        SaleinvoiceBVO[] itemsNew = new SaleinvoiceBVO[1];
        itemsNew[0] = itemVOs[indexSelected];
        saleinvoice.setChildrenVO(itemsNew);
      } else {
        saleinvoice.setChildrenVO(null);
      }

    }

    ((SaleVO)saleinvoice.getParentVO()).setCapproveid(getClientEnvironment().getUser().getPrimaryKey());

    return saleinvoice;
  }

  protected void initFreeItem(CircularlyAccessibleValueObject[] bodyvos)
  {
    if (bodyvos == null) {
      return;
    }

    try
    {
      FreeVOParse freeVOParse = new FreeVOParse();
      freeVOParse.setFreeVO(bodyvos, "vfree0", "vfree", "cinvbasdocid", "cinventoryid", false);

      if ((bodyvos != null) && (bodyvos.length > 0)) {
        Object oTemp = null;
        for (int i = 0; i < bodyvos.length; i++)
        {
          oTemp = bodyvos[i].getAttributeValue("vfree0");
          if ((oTemp != null) && (oTemp.toString().trim().length() > 0)) {
            getBillCardPanel().getBillModel().setValueAt(oTemp, i, "vfree0");
          }
        }
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      SCMEnv.out("自由项设置失败!");
    }
  }

  public void doApproveAction(ILinkApproveData approvedata)
  {
    this.bInMsgPanel = true;

    loadTemplet(approvedata.getBillType(), null, this.user, this.pk_corp);
    getInitBillItemEidtState();

    loadData(approvedata.getBillID());
    this.m_strID = approvedata.getBillID();

    getBillCardPanel().setEnabled(false);
    ButtonObject[] btns = { this.m_boDocument, this.boAuditFlowStatus, this.m_boCheck };

    this.boAuditFlowStatus.setEnabled(true);
    this.m_boDocument.setEnabled(true);

    this.m_boCheck.setTag("APPROVE");
    this.boAuditFlowStatus.setVisible(true);
    this.boAuditFlowStatus.setEnabled(true);
    this.m_boDocument.setEnabled(true);
    this.m_boCheck.setEnabled(true);

    setButtons(btns);
  }

  protected SCMQueryConditionDlg getQueryDlg()
  {
    SCMQueryConditionDlg dlgQuery = null;
    if (dlgQuery == null) {
      dlgQuery = new SCMQueryConditionDlg(this);
      dlgQuery.setTempletID(getCorpPrimaryKey(), "40060502", getClientEnvironment().getUser().getPrimaryKey(), getBillCardPanel().getBusiType(), "40060502");

      dlgQuery.hideUnitButton();
      dlgQuery.readTempletDate();
      QueryConditionVO[] vos = dlgQuery.getConditionDatas();

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

      dlgQuery.setValueRef("so_saleinvoice.cbiztype", biztypeRef);

      dlgQuery.setIsWarningWithNoInput(true);
      DefSetTool.updateQueryConditionClientUserDef(dlgQuery, getCorpPrimaryKey(), getBillCode(), "so_saleinvoice.vdef", null, 0, "so_saleexecute.vdef", null, 0);

      DefSetTool.updateQueryConditionForCumandoc(dlgQuery, getCorpPrimaryKey(), "bd_cumandoc.def");

      dlgQuery.setNormalShow(true);
      dlgQuery.setShowPrintStatusPanel(true);
      dlgQuery.setDataPower(true, this.ce.getCorporation().getPk_corp());
    }

    return dlgQuery;
  }

  public void doQueryAction(ILinkQueryData querydata)
  {
    loadTemplet(querydata.getBillType(), null, this.user, this.pk_corp);
    getInitBillItemEidtState();
    getQueryDlg().setRefsDataPowerConVOs(ClientEnvironment.getInstance().getUser().getPrimaryKey(), new String[] { ClientEnvironment.getInstance().getCorporation().getPk_corp() }, new String[] { "客户档案", "客户档案", "销售组织", "部门档案", "人员档案", "客户档案", "存货档案", "外币档案", "库存组织", "客户档案", "仓库档案" }, new String[] { "so_saleinvoice_b.ccustomerid", "so_saleinvoice.creceiptcorpid", "so_saleinvoice.csalecorpid", "so_saleinvoice.cdeptid", "so_saleinvoice.cemployeeid", "so_saleinvoice.creceiptcustomerid", "so_saleinvoice_b.cinventoryid", "so_saleinvoice_b.ccurrencytypeid", "so_saleinvoice.ccalbodyid", "so_saleinvoice_b.creceiptcorpid", "so_saleinvoice_b.cbodywarehouseid" }, new int[] { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 });

    ConditionVO[] voCon = getQueryDlg().getConditionVO();
    String sdlgWhere = null;
    if (voCon != null)
      sdlgWhere = getQueryDlg().getWhereSQL(voCon);
    if ((sdlgWhere == null) || (sdlgWhere.trim().length() == 0)) {
      sdlgWhere = " so_saleinvoice.csaleid='" + querydata.getBillID() + "' or (so_saleinvoice.csaleid =(select csaleid from so_saleinvoice_b where cinvoice_bid ='" + querydata.getBillID() + "') ) ";
    }
    else {
      sdlgWhere = sdlgWhere + " and  (so_saleinvoice.csaleid='" + querydata.getBillID() + "'  or (so_saleinvoice.csaleid =(select csaleid from so_saleinvoice_b where cinvoice_bid ='" + querydata.getBillID() + "') ))";
    }
    ISaleinvoiceQuery iquery = (ISaleinvoiceQuery)NCLocator.getInstance().lookup(ISaleinvoiceQuery.class.getName());
    SaleinvoiceVO[] voaRet = null;
    try {
      voaRet = (SaleinvoiceVO[])(SaleinvoiceVO[])iquery.queryDataByWhere(sdlgWhere);
      if ((voaRet == null) || (voaRet.length <= 0) || (voaRet[0] == null)) {
        MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270"), NCLangRes.getInstance().getStrByID("common", "SCMCOMMON000000161"));

        return;
      }
    }
    catch (Exception e) {
      showWarningMessage(e.getMessage());
    }
    this.m_strID = querydata.getBillID();

    loadData(voaRet[0]);

    getBillCardPanel().setEnabled(false);
    this.strState = "FREE";
    setButtonsState();

    updateButtons();
  }

  public void doAddAction(ILinkAddData adddata)
  {
    try
    {
      if ("30".equals(adddata.getSourceBillType()))
      {
        SaleOrderVO order = (SaleOrderVO)SaleOrderBO_Client.queryData(adddata.getSourceBillID());

        this.saleinvoiceNew = new SaleinvoiceVO[] { (SaleinvoiceVO)PfUtilUITools.runChangeData("30", "32", order) };

        onNew();
       }
    }
    catch (Exception e) {
      handleException(e);
    }
  }

  public void doMaintainAction(ILinkMaintainData maintaindata)
  {
    this.m_strID = maintaindata.getBillID();
    loadData(maintaindata.getBillID());
    onModify();
  }

  public void initRef()
  {
    UIRefPane cdeptid = (UIRefPane)getBillCardPanel().getHeadItem("cdeptid").getComponent();

    if (cdeptid != null) {
      cdeptid.setWhereString(" bd_deptdoc.pk_corp='" + getCorpPrimaryKey() + "'");
    }
    UIRefPane Refpsn = (UIRefPane)getBillCardPanel().getHeadItem("cemployeeid").getComponent();

    if (Refpsn != null)
      Refpsn.setWhereString(" bd_psndoc.pk_corp='" + getCorpPrimaryKey() + "'");
  }

  public void initSaleDeptRef()
  {
    UIRefPane cdeptid = (UIRefPane)getBillCardPanel().getHeadItem("cdeptid").getComponent();

    if (cdeptid != null)
      cdeptid.setWhereString("(bd_deptdoc.deptattr = '3' or bd_deptdoc.deptattr= '4' ) and bd_deptdoc.pk_corp='" + getCorpPrimaryKey() + "'");
  }

  public void initSalePeopleRef()
  {
    UIRefPane Refpsn = (UIRefPane)getBillCardPanel().getHeadItem("cemployeeid").getComponent();

    if (Refpsn != null)
      Refpsn.setWhereString(" (bd_deptdoc.deptattr = '3' or bd_deptdoc.deptattr= '4') and bd_psndoc.pk_corp='" + getCorpPrimaryKey() + "'");
  }

  public void loadData(String sID)
  {
    try
    {
      SaleinvoiceVO saleinvoice = SaleinvoiceBO_Client.queryData(sID);
      loadData(saleinvoice);
    } catch (Exception e) {
      showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000256"));

      SCMEnv.out(e.getMessage());
    }
  }

  public void loadData(SaleinvoiceVO saleinvoice)
  {
    try
    {
      String dbilldate = ((SaleVO)saleinvoice.getParentVO()).getDbilldate().toString();

      String currencyid = ((SaleinvoiceBVO)saleinvoice.getChildrenVO()[0]).getCcurrencytypeid();

      setPanelByCurrency(currencyid);

      UFDouble exchangeotobrate = ((SaleinvoiceBVO)saleinvoice.getChildrenVO()[0]).getNexchangeotobrate();

      UFDouble exchangeotoarate = ((SaleinvoiceBVO)saleinvoice.getChildrenVO()[0]).getNexchangeotoarate();

      UFBoolean freef = ((SaleVO)saleinvoice.getParentVO()).getBfreecustflag();

      ((SaleVO)saleinvoice.getParentVO()).setCcurrencyid(currencyid);
      getBillCardPanel().setBillValueVO(saleinvoice);
      long s1 = System.currentTimeMillis();
      getBillCardPanel().getBillModel().execLoadFormula();
      getBillCardPanel().execHeadFormula("csalecompanyname->getColValue(bd_corp,unitname,pk_corp,pk_corp)");

      SCMEnv.out("执行公式[共用时" + (System.currentTimeMillis() - s1) + "]");

      if (!this.bInMsgPanel) {
        afterCurrencyChange(dbilldate);
        if (exchangeotobrate != null) {
          getBillCardPanel().setHeadItem("nexchangeotobrate", exchangeotobrate.toString());
        }
        if (exchangeotoarate != null) {
          getBillCardPanel().setHeadItem("nexchangeotoarate", exchangeotoarate.toString());
        }

        boolean freeflag = freef == null ? false : freef.booleanValue();
        if (freeflag) {
          getBillCardPanel().getHeadItem("cfreecustid").setEnabled(true);
          getBillCardPanel().getHeadItem("cfreecustid").setEdit(true);
        } else {
          getBillCardPanel().getHeadItem("cfreecustid").setEnabled(false);
          getBillCardPanel().getHeadItem("cfreecustid").setEdit(false);
        }
      }
      String custId = ((SaleVO)saleinvoice.getParentVO()).getCreceiptcorpid();

      UIRefPane ref = (UIRefPane)getBillCardPanel().getHeadItem("ccustomername").getComponent();

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
        else
        {
          getBillCardPanel().setHeadItem("ccustomertel", "");
          getBillCardPanel().setHeadItem("ccustomertaxNo", "");
        }
      }
      catch (Exception e1) {
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
        beforeUnitChange(kk);
        afterUnitChange(kk);

        String[] appendFormulaViaPrice = { "norgviaprice->noriginalcurprice*scalefactor", "norgviapricetax->noriginalcurtaxprice*scalefactor" };

        getBillCardPanel().execBodyFormulas(kk, appendFormulaViaPrice);
      }
      initFreeItem(saleinvoice.getChildrenVO());

      getBillCardPanel().updateValue();
      getBillCardPanel().getBillModel().reCalcurateAll();

      SCMEnv.out("数据加载成功！");

      if (!this.bInMsgPanel)
        setState(iStatus);
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
        String strBodyID = (String)(String)getBillCardPanel().getBodyValueAt(j, "cinvoice_bid");

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

  public void loadTemplet(String billType, String busiType, String userid, String corpid)
  {
    showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000136"));

    BillData bd = null;

    bd = new BillData(getBillCardPanel().getTempletData(getNodeCode(), null, userid, corpid));

    getFreeItemRefPane().setMaxLength(1000);

    bd.getBodyItem("vfree0").setComponent(getFreeItemRefPane());

    setCardPanel(bd);

    getBillCardPanel().setBillData(bd);

    ((UIRefPane)getBillCardPanel().getHeadItem("cdispatcherid").getComponent()).getRefModel().addWherePart(" and rdflag = 1 ");

    DefSetTool.updateBillCardPanelUserDef(getBillCardPanel(), getClientEnvironment().getCorporation().getPk_corp(), "32", "vdef", "vdef");

    getBillCardPanel().addBodyTotalListener(this);

    initFormulaParse();

    setInputLimit();
    setHeadComboBox();
    setBodyComboBox();
    getBillCardPanel().hideBodyTableCol("ntotalpaymny");

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

  private SaleinvoiceVO mergeCheck()
    throws BusinessException
  {
    String salecorpid0 = ((SaleVO)this.saleinvoiceNew[0].getParentVO()).getCsalecorpid();

    String receiptcorpid0 = ((SaleVO)this.saleinvoiceNew[0].getParentVO()).getCreceiptcorpid();

    SaleinvoiceBVO[] salebodyVO = (SaleinvoiceBVO[])(SaleinvoiceBVO[])this.saleinvoiceNew[0].getChildrenVO();

    String currencyid0 = salebodyVO[0].getCcurrencytypeid();

    String calbodyid0 = ((SaleVO)this.saleinvoiceNew[0].getParentVO()).getCcalbodyid();

    if (this.saleinvoiceNew.length > 1) {
      for (int i = 1; i < salebodyVO.length; i++) {
        String currencyid = salebodyVO[i].getCcurrencytypeid();
        if (!currencyid.trim().equals(currencyid0.trim())) {
          throw new BusinessException(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000125"));
        }

      }

      for (int i = 1; i < this.saleinvoiceNew.length; i++) {
        String salecorpid = ((SaleVO)this.saleinvoiceNew[i].getParentVO()).getCsalecorpid();

        String receiptcorpid = ((SaleVO)this.saleinvoiceNew[i].getParentVO()).getCreceiptcorpid();

        String calbodyid = ((SaleVO)this.saleinvoiceNew[i].getParentVO()).getCcalbodyid();

        if ((((SaleVO)this.saleinvoiceNew[i].getParentVO()).getCbiztype() == null) || (!((SaleVO)this.saleinvoiceNew[i].getParentVO()).getCbiztype().equals(this.strBusitype)))
        {
          throw new BusinessException(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-100067"));
        }

        if ((!this.SO_30.booleanValue()) && 
          (!receiptcorpid.trim().equals(receiptcorpid0.trim()))) {
          throw new BusinessException(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000114"));
        }

        if (!salecorpid.trim().equals(salecorpid0.trim())) {
          throw new BusinessException(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000115"));
        }

        salebodyVO = (SaleinvoiceBVO[])(SaleinvoiceBVO[])this.saleinvoiceNew[i].getChildrenVO();

        for (int j = 0; j < salebodyVO.length; j++) {
          String currencyid = salebodyVO[j].getCcurrencytypeid();
          if (!currencyid.trim().equals(currencyid0.trim())) {
            throw new BusinessException(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000117"));
          }

        }

      }

    }

    SaleinvoiceVO saleinvoice = new SaleinvoiceVO();
    if (this.saleinvoiceNew.length == 1) {
      saleinvoice = this.saleinvoiceNew[0];
    } else {
      saleinvoice.setParentVO(this.saleinvoiceNew[0].getParentVO());

      ((SaleVO)saleinvoice.getParentVO()).setCbiztype(this.strBusitype);
      Vector vBodys = new Vector();

      for (int i = 0; i < this.saleinvoiceNew.length; i++) {
        SaleinvoiceBVO[] items = (SaleinvoiceBVO[])(SaleinvoiceBVO[])this.saleinvoiceNew[i].getChildrenVO();

        for (int j = 0; j < items.length; j++)
          vBodys.addElement(items[j]);
      }
      if (vBodys.size() > 0)
      {
        SaleinvoiceBVO[] bodyItems = new SaleinvoiceBVO[vBodys.size()];
        vBodys.copyInto(bodyItems);
        saleinvoice.setChildrenVO(bodyItems);
        ((SaleVO)saleinvoice.getParentVO()).setCcurrencyid(bodyItems[0].getCcurrencytypeid());
      }
    }

    return saleinvoice;
  }

  public SaleinvoiceVO[] mergeSourceVOs(SaleinvoiceVO[] arySourceVO, String sBilltype)
  {
    SaleinvoiceVO[] aryRetVO = null;
    String[] sHeaditems = null;
    String[] sBodyitems = null;
    if (this.SO_30.booleanValue())
      sHeaditems = new String[] { "csalecorpid" };
    else {
      sHeaditems = new String[] { "creceiptcorpid", "csalecorpid" };
    }
    if ((this.SO60.booleanValue()) && (this.SO_27.booleanValue()))
      sBodyitems = new String[] { "ccurrencytypeid", "ccustomerid", "cprolineid" };
    else if (this.SO60.booleanValue())
      sBodyitems = new String[] { "ccurrencytypeid", "ccustomerid" };
    else if (this.SO_27.booleanValue())
      sBodyitems = new String[] { "ccurrencytypeid", "cprolineid" };
    else {
      sBodyitems = new String[] { "ccurrencytypeid" };
    }

    aryRetVO = (SaleinvoiceVO[])(SaleinvoiceVO[])SplitBillVOs.getSplitVOs("nc.vo.so.so002.SaleinvoiceVO", "nc.vo.so.so002.SaleVO", "nc.vo.so.so002.SaleinvoiceBVO", arySourceVO, sHeaditems, sBodyitems);

    this.alBills = new ArrayList();
    SaleinvoiceVO[] aryCloneVOs = null;
    if (aryRetVO.length > 1) {
      try
      {
        aryCloneVOs = (SaleinvoiceVO[])(SaleinvoiceVO[])ObjectUtils.serializableClone(aryRetVO);

        for (int i = 0; i < aryCloneVOs.length; i++)
          this.alBills.add(aryCloneVOs[i]);
      }
      catch (Exception ex) {
        handleException(ex);
      }
    }

    return aryRetVO;
  }

  public void onAfterAction(ButtonObject bo)
  {
    try
    {
      SaleinvoiceVO saleinvoice = (SaleinvoiceVO)getVO();
      saleinvoice.setCuruserid(getClientEnvironment().getUser().getPrimaryKey());

      PfUtilClient.processAction(bo.getTag(), "32", getClientEnvironment().getDate().toString(), saleinvoice, this.m_strID);

      showHintMessage(bo.getName() + NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000067"));
    }
    catch (Exception e)
    {
      showErrorMessage(bo.getName() + NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000069"));

      SCMEnv.out(e.getMessage());
    }
  }

  public void onApproveCheckWorkflow(SaleinvoiceVO invoicehvo) throws ValidationException
  {
    try {
      boolean isExist = false;
      isExist = PfUtilBO_Client.isExistWorkFlow("32", invoicehvo.getHeadVO().getCbiztype(), getClientEnvironment().getCorporation().getPk_corp(), getClientEnvironment().getUser().getPrimaryKey());

      if (isExist == true) {
        int iWorkflowstate = 0;
        iWorkflowstate = PfUtilClient.queryWorkFlowStatus(invoicehvo.getHeadVO().getCbiztype(), "32", invoicehvo.getParentVO().getPrimaryKey());

        if (iWorkflowstate == 5) {
          throw new ValidationException(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000070"));
        }

      }

    }
    catch (Throwable e)
    {
      throw new ValidationException(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000070"));
    }
  }
 
  private void onBillCombin()
  {
    CollectSettingDlg dlg = new CollectSettingDlg(this);

    dlg.setBilltype("32");
    dlg.setNodecode(getNodeCode());
    dlg.initData(getBillCardPanel(), new String[] { "cinventorycode", "cinventoryname", "GG", "XX", "ccurrencytypename","noriginalcurtaxprice","cupsourcebillcode"}, new String[] { "noriginalcurtaxnetprice" }, new String[] { "noriginalcurtaxmny", "noriginalcurmny", "noriginalcursummny", "nnumber", "noriginalcurdiscountmny", "ntaxmny", "nmny", "nsummny", "ndiscountmny", "nsimulatecostmny", "ncostmny", "nsubsummny", "nsubcursummny" }, null, new String[] { "nsubtaxnetprice", "nsubqutaxnetpri", "nsubqunetpri", "nsubqutaxpri", "nsubqupri", "nqutaxnetprice", "nqunetprice", "nqutaxprice", "nquprice", "nqocurtaxnetpri", "nquoricurnetpri", "nquoricurtaxpri", "nquoricurpri", "ntaxnetprice", "nnetprice", "ntaxprice", "nprice", "noriginalcurtaxnetprice", "noriginalcurnetprice", "noriginalcurtaxprice", "noriginalcurprice" }, "nnumber");

    dlg.showModal();
    showHintMessage(NCLangRes.getInstance().getStrByID("40060501", "UPT40060501-000058"));
  }
  
/*
  private void onBillCombin()
  {
    CollectSettingDlg dlg = new CollectSettingDlg(this);

    dlg.setBilltype("32");
    dlg.setNodecode(getNodeCode());
    dlg.initData(getBillCardPanel(), new String[] { "cinventorycode", "cinventoryname", "GG", "XX", "ccurrencytypename" }, new String[] { "noriginalcurtaxnetprice" }, new String[] { "noriginalcurtaxmny", "noriginalcurmny", "noriginalcursummny", "nnumber", "noriginalcurdiscountmny", "ntaxmny", "nmny", "nsummny", "ndiscountmny", "nsimulatecostmny", "ncostmny", "nsubsummny", "nsubcursummny" }, null, new String[] { "nsubtaxnetprice", "nsubqutaxnetpri", "nsubqunetpri", "nsubqutaxpri", "nsubqupri", "nqutaxnetprice", "nqunetprice", "nqutaxprice", "nquprice", "nqocurtaxnetpri", "nquoricurnetpri", "nquoricurtaxpri", "nquoricurpri", "ntaxnetprice", "nnetprice", "ntaxprice", "nprice", "noriginalcurtaxnetprice", "noriginalcurnetprice", "noriginalcurtaxprice", "noriginalcurprice" }, "nnumber");

    dlg.showModal();
    showHintMessage(NCLangRes.getInstance().getStrByID("40060501", "UPT40060501-000058"));
  }
*/
  public void onCopyLine()
  {
    getBillCardPanel().copyLine();
  }

  public void onDocument()
  {
    showHintMessage(this.m_boDocument.getHint());
    String pk = getBillCardPanel().getHeadItem("csaleid").getValue();
    String billcode = getBillCardPanel().getHeadItem("vreceiptcode").getValue();

    DocumentManager.showDM(this, new String[] { pk }, new String[] { billcode });
  }

  public void onGather()
  {
    this.isEdit = true;
    this.strState = "ADD";
    getBillCardPanel().addNew();
    getBillCardPanel().setEnabled(true);
    setDefaultData(true);

    getBillCardPanel().setHeadItem("dbilldate", getClientEnvironment().getDate().toString());

    getBillCardPanel().setTailItem("coperatorid", getClientEnvironment().getUser().getPrimaryKey());

    getBillCardPanel().setTailItem("dmakedate", getClientEnvironment().getDate().toString());

    getBillCardPanel().getHeadItem("fcounteractflag").setValue(new Integer(0));

    getBillCardPanel().getHeadItem("finvoicetype").setValue(new Integer(0));

    ((UIRefPane)getBillCardPanel().getHeadItem("cbiztypename").getComponent()).setPK(getBillCardPanel().getBusiType());

    setButtonsState();
  }

  public void onGatherCheck(SaleinvoiceVO saleinvoice)
    throws ValidationException
  {
    if (getBillCardPanel().getRowCount() == 0) {
      throw new ValidationException(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000073"));
    }

    SaleVO voHead = (SaleVO)saleinvoice.getParentVO();
    String custID = voHead.getCreceiptcorpid();
    String salecorpID = voHead.getCsalecorpid();
    String ccalbodyID = voHead.getCcalbodyid();
    String ccurrencyID = voHead.getCcurrencyid();
    UFDate dBillDate = voHead.getDbilldate();
    ArrayList errFields = new ArrayList();

    if ((dBillDate == null) || (dBillDate.toString().length() == 0)) {
      errFields.add(NCLangRes.getInstance().getStrByID("common", "UC000-0001902"));
    }

    if ((custID == null) || (custID.length() == 0)) {
      errFields.add(NCLangRes.getInstance().getStrByID("common", "UC000-0001589"));
    }

    if ((salecorpID == null) || (salecorpID.length() == 0)) {
      errFields.add(NCLangRes.getInstance().getStrByID("common", "UC000-0004128"));
    }

    if ((ccalbodyID == null) || (ccalbodyID.length() == 0)) {
      errFields.add(NCLangRes.getInstance().getStrByID("common", "UC000-0001825"));
    }

    if ((ccurrencyID == null) || (ccurrencyID.length() == 0)) {
      errFields.add(NCLangRes.getInstance().getStrByID("common", "UC000-0001755"));
    }

    StringBuffer message = new StringBuffer();
    message.append(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000137"));

    if (errFields.size() > 0) {
      String[] temp = (String[])(String[])errFields.toArray(new String[0]);
      message.append(temp[0].toString());
      for (int i = 1; i < temp.length; i++) {
        message.append(NCLangResOnserver.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000000"));

        message.append(temp[i].toString());
      }

      throw new NullFieldException(message.toString());
    }

    for (int i = 0; i < saleinvoice.getChildrenVO().length; i++) {
      SaleinvoiceBVO oldbodyVO = (SaleinvoiceBVO)saleinvoice.getChildrenVO()[i];

      if ((oldbodyVO.getNnumber() == null) || (oldbodyVO.getNnumber().doubleValue() == 0.0D))
      {
        throw new ValidationException(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000134"));
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

  public void onInsertLine()
  {
    getBillCardPanel().insertLine();

    BillRowNo.insertLineRowNo(getBillCardPanel(), getBillCode(), getRowNoItemKey());
  }

  public void onOrderQuery()
  {
    showHintMessage(this.boOrderQuery.getHint());
    getSourceDlg().showModal();
  }

  public void onPasteLine()
  {
    int iBefore = getBillCardPanel().getRowCount();

    getBillCardPanel().pasteLine();
    for (int i = 0; i < getBillCardPanel().getRowCount(); i++)
    {
      Object temp = getBillCardPanel().getBodyValueAt(i, "wholemanaflag");
      boolean wholemanaflag = temp == null ? false : new UFBoolean(temp.toString()).booleanValue();

      getBillCardPanel().setCellEditable(i, "fbatchstatus", wholemanaflag);

      getBillCardPanel().setCellEditable(i, "cbatchid", wholemanaflag);
    }

    int row = getBillCardPanel().getBillTable().getSelectedRow();
    setAssistUnit(row - 1);

    int iAfter = getBillCardPanel().getRowCount();

    int iRow = iAfter - iBefore;
    if ((iBefore > 0) && (iAfter > 0) && (iRow > 0))
      BillRowNo.pasteLineRowNo(getBillCardPanel(), getBillCode(), getRowNoItemKey(), iRow);
  }

  public PrintLogClient getPrintLogClient()
  {
    if (this.printLogClient == null) {
      this.printLogClient = new PrintLogClient();
      this.printLogClient.addFreshTsListener(this);
    }
    return this.printLogClient;
  }

  public void freshTs(String sBillID, String sTS, Integer iPrintCount)
  {
    if ((sTS == null) || (sTS.trim().length() <= 0))
      return;
    getBillCardPanel().setHeadItem("ts", sTS);
    getBillCardPanel().setTailItem("iprintcount", iPrintCount);
  }

  public void onReturn()
  {
    remove(getBillCardPanel());
    add(getBillListPanel(), "Center");
    this.strState = "LIST";
    setButtons(this.aryButtonGroupList);

    getBillListPanel().getHeadTable().clearSelection();
    getBillListPanel().getBodyBillModel().clearBodyData();
    setButtonsState();
    updateUI();
  }

  public void onPrint(boolean previewflag)
  {
    boolean total = getBillCardPanel().getBodyPanel().isTatolRow();
    try {
      SalePubPrintDS ds = new SalePubPrintDS("40060501", getBillCardPanel());

      PrintEntry print = new PrintEntry(null, null);

      print.setTemplateID(getCorpPrimaryKey(), "40060501", this.ce.getUser().getPrimaryKey(), null);

      if (print.selectTemplate() < 0)
        return;
      getPrintLogClient(); showHintMessage(PrintLogClient.getBeforePrintMsg(previewflag, false));

    
      SaleinvoiceVO saleinvoice = (SaleinvoiceVO)getBillCardPanel().getBillValueVO(SaleinvoiceVO.class.getName(), SaleVO.class.getName(), SaleinvoiceBVO.class.getName());
      print.setPrintListener(getPrintLogClient());
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
    } finally {
      getBillCardPanel().getBodyPanel().setTotalRowShow(total);
    }
    
  }

  public void onSendAudit()
  {
    SaleinvoiceVO invoicehvo = (SaleinvoiceVO)getVO(false);

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

        if (iWorkflowstate != 5) {
          if (iWorkflowstate == 2) {
            showErrorMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000077"));
          }
          else if (iWorkflowstate == 0) {
            showErrorMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000078"));
          }
          else if (iWorkflowstate == 1) {
            showErrorMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000079"));
          }
          else if (iWorkflowstate == 3) {
            showErrorMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000071"));
          }
          else if (iWorkflowstate == 4) {
            showErrorMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000112"));
          }
          else if (iWorkflowstate == 5) {
            showErrorMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000070"));
          }
          else if (iWorkflowstate == -1) {
            showErrorMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000277"));
          }

          return;
        }

        PfUtilClient.processAction("SAVE", "32", getClientEnvironment().getDate().toString(), invoicehvo);

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

  public void onStockLock()
  {
    try
    {
      PfUtilClient.processAction("StockLock", "32", getClientEnvironment().getDate().toString(), getVO());

      if (PfUtilClient.isSuccess()) {
        int row = getBillCardPanel().getBillTable().getSelectedRow();
        loadData();
        if (row > -1) {
          Object orderbid = getBillCardPanel().getBodyValueAt(row, "corder_bid");

          SaleinvoiceBO_Client.updateLock(orderbid.toString(), "0001AAA00000000006F1");

          getBillCardPanel().setBodyValueAt("0001AAA00000000006F1", row, "cfreezeid");
        }
      }
    }
    catch (BusinessException e1) {
      showErrorMessage(e1.getMessage());
      e1.printStackTrace(System.out);
    } catch (Exception e) {
      showErrorMessage(e.getMessage());
      SCMEnv.out(e.getMessage());
    }
  }

  public void reloadData(String invoiceID)
  {
    try
    {
      SaleinvoiceVO saleinvoice = SaleinvoiceBO_Client.queryData(invoiceID);

      String dbilldate = ((SaleVO)saleinvoice.getParentVO()).getDbilldate().toString();

      String currencyid = ((SaleinvoiceBVO)saleinvoice.getChildrenVO()[0]).getCcurrencytypeid();

      setPanelByCurrency(currencyid);

      UFDouble exchangeotobrate = ((SaleinvoiceBVO)saleinvoice.getChildrenVO()[0]).getNexchangeotobrate();

      UFDouble exchangeotoarate = ((SaleinvoiceBVO)saleinvoice.getChildrenVO()[0]).getNexchangeotoarate();

      UFBoolean freef = ((SaleVO)saleinvoice.getParentVO()).getBfreecustflag();

      ((SaleVO)saleinvoice.getParentVO()).setCcurrencyid(currencyid);
      getBillCardPanel().setBillValueVO(saleinvoice);
      long s1 = System.currentTimeMillis();
      String[] formulas = new String[4];

      formulas[0] = "cupsourcebillcode->getColValue(ic_general_h,vbillcode,cgeneralhid,cupsourcebillid)";

      formulas[1] = "coriginalbillcode->getColValue(so_sale,vreceiptcode,csaleid,csourcebillid)";

      formulas[2] = "cadvisecalbodyid->getColValue(so_saleorder_b,cadvisecalbodyid,corder_bid,csourcebillbodyid)";
      formulas[3] = "cadvisecalbodyname->getColValue(bd_calbody,bodyname,pk_calbody,cadvisecalbodyid)";

      getBillCardPanel().getBillModel().execFormulas(formulas);

      getBillCardPanel().getBillModel().execLoadFormula();
      SCMEnv.out("执行公式[共用时" + (System.currentTimeMillis() - s1) + "]");

      afterCurrencyChange(dbilldate);
      getBillCardPanel().setHeadItem("nexchangeotobrate", exchangeotobrate.toString());

      if (exchangeotoarate != null) {
        getBillCardPanel().setHeadItem("nexchangeotoarate", exchangeotoarate.toString());
      }
      boolean freeflag = freef == null ? false : freef.booleanValue();
      if (freeflag) {
        getBillCardPanel().getHeadItem("cfreecustid").setEnabled(true);
        getBillCardPanel().getHeadItem("cfreecustid").setEdit(true);
      } else {
        getBillCardPanel().getHeadItem("cfreecustid").setEnabled(false);
        getBillCardPanel().getHeadItem("cfreecustid").setEdit(false);
      }
      int iStatus = ((SaleVO)saleinvoice.getParentVO()).getFstatus().intValue();

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
        }
        else {
          getBillCardPanel().setHeadItem("ccustomertel", "");
          getBillCardPanel().setHeadItem("ccustomertaxNo", "");
        }
      } catch (Exception e1) {
        showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000256"));

        e1.printStackTrace(System.out);
      }

      UIRefPane vreceiveaddress = (UIRefPane)getBillCardPanel().getHeadItem("vreceiveaddress").getComponent();

      Object temp = saleinvoice.getParentVO().getAttributeValue("vreceiveaddress");

      String formula = "getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,ccustomername)";
      String pk_cubasdoc = (String)getBillCardPanel().execHeadFormula(formula);

      String strvreceiveaddress = nc.ui.so.so001.panel.bom.BillTools.getColValue2("bd_custaddr", "pk_custaddr", "defaddrflag", "Y", "pk_cubasdoc", pk_cubasdoc);

      vreceiveaddress.setPK(strvreceiveaddress);

      iStatus = -1;
      if ((getBillCardPanel().getHeadItem("fstatus").getValue() != null) && (!getBillCardPanel().getHeadItem("fstatus").getValue().equals("")))
      {
        iStatus = Integer.parseInt(getBillCardPanel().getHeadItem("fstatus").getValue());
      }
      for (int kk = 0; kk < getBillCardPanel().getRowCount(); kk++) {
        if (iStatus == 1)
          setAssistChange(kk);
        beforeUnitChange(kk);
        afterUnitChange(kk);

        String[] appendFormulaViaPrice = { "norgviaprice->noriginalcurprice*scalefactor", "norgviapricetax->noriginalcurtaxprice*scalefactor" };

        getBillCardPanel().execBodyFormulas(kk, appendFormulaViaPrice);
      }
      initFreeItem(saleinvoice.getChildrenVO());
      getBillCardPanel().updateValue();
      getBillCardPanel().getBillModel().reCalcurateAll();

      SCMEnv.out("数据加载成功！");

      setState(iStatus);
    } catch (ValidationException e) {
      showErrorMessage(e.getMessage());
    } catch (Exception e) {
      showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000256"));

      SCMEnv.out(e.getMessage());
    }
  }

  private void reloadList()
  {
    int iRowCountOld = getBillListPanel().getHeadTable().getRowCount();
    if (iRowCountOld > 1) {
      int iCustSelect = MessageDialog.showYesNoCancelDlg(this, "还有未保存的发票", "还有未保存的发票，是否保存", 0);

      int iRowSelect = getBillListPanel().getHeadTable().getSelectedRow();

      if (iRowSelect >= 0)
        this.alBills.remove(iRowSelect);
      else if ((iRowSelect == -1) && 
        (this.alBills.size() > 0)) {
        this.alBills.remove(0);
      }
      getBillListPanel().setHeaderValueVO(getAddListHeadValues());
      getBillListPanel().getHeadBillModel().execLoadFormula();

      if (iCustSelect == 4) {
        this.saleinvoice = ((SaleinvoiceVO)this.alBills.get(0));
        onNewPrepare();
        return;
      }
      if (iCustSelect == 8)
      {
        if (showYesNoMessage("选'否'，将放弃所有待保存的发票，是否继续") == 4) {
          this.alBills.clear();
          getBillListPanel().getHeadBillModel().clearBodyData();
          getBillListPanel().getBodyBillModel().clearBodyData();
        }
      }
    }

    if (iRowCountOld == 1) {
      this.alBills.clear();
      getBillListPanel().getHeadBillModel().clearBodyData();
      getBillListPanel().getBodyBillModel().clearBodyData();
    }
    setButtonsState();
  }

  public void reLoadTS()
  {
    try
    {
      String sBillID = this.m_strID;

      String[] formula = { "ts->getColValue(so_saleinvoice,ts,csaleid,csaleid)", "dmoditime->getColValue(so_saleinvoice,dmoditime,csaleid,csaleid)", "daudittime->getColValue(so_saleinvoice,daudittime,csaleid,csaleid)", "dbilltime->getColValue(so_saleinvoice,dbilltime,csaleid,csaleid)" };

      getBillCardPanel().execHeadFormulas(formula);
    } catch (Exception e) {
      SCMEnv.out("重新加载表头TS失败.");
      SCMEnv.out(e.getMessage());
    }
  }

  public void setDefaultData(boolean isfree)
  {
    getBillCardPanel().getHeadItem("csalecorpid").setEdit(true);

    getBillCardPanel().getHeadItem("ccalbodyid").setEdit(true);

    getBillCardPanel().getHeadItem("ccurrencyid").setEdit(true);

    getBillCardPanel().setHeadItem("cbiztype", getBillCardPanel().getBusiType());

    getBillCardPanel().getHeadItem("vprintcustname").setEnabled(true);

    getBillCardPanel().setHeadItem("vreceiptcode", null);

    getBillCardPanel().setHeadItem("dbilldate", getClientEnvironment().getDate().toString());

    getBillCardPanel().setHeadItem("pk_corp", getCorpPrimaryKey());

    getBillCardPanel().setHeadItem("fstatus", "1");
    if (isfree)
    {
      getBillCardPanel().setHeadItem("ndiscountrate", "100.0");

      getBillCardPanel().setHeadItem("nevaluatecarriage", "0.0");

      ((UIRefPane)getBillCardPanel().getHeadItem("vreceiptcode").getComponent()).getUITextField().setDelStr("+");
    }

    getBillCardPanel().getBodyItem("cbodywarehousename").setEdit(false);

    getBillCardPanel().setTailItem("dmakedate", getClientEnvironment().getDate().toString());

    getBillCardPanel().setTailItem("coperatorid", getClientEnvironment().getUser().getPrimaryKey());

    getBillCardPanel().setTailItem("capproveid", null);

    getBillCardPanel().setTailItem("dapprovedate", null);
  }

  protected void setInputLimit()
  {
    UIRefPane ref = (UIRefPane)getBillCardPanel().getHeadItem("creceiptcorpid").getComponent();

    ref.getRefModel().addWherePart(" and bd_cumandoc.frozenflag = 'N'");

    UIRefPane custRef = (UIRefPane)getBillCardPanel().getHeadItem("ccustomername").getComponent();

    custRef.getRefModel().addWherePart(" and bd_cumandoc.frozenflag = 'N'");

    UIRefPane refInv = (UIRefPane)getBillCardPanel().getBodyItem("cinventorycode").getComponent();

    if ((refInv != null) && 
      (refInv.getRefModel().getWherePart().indexOf("and bd_invmandoc.iscansaleinvoice ='Y'") < 0))
    {
      refInv.getRefModel().setWherePart(refInv.getRefModel().getWherePart() + " and bd_invmandoc.iscansaleinvoice ='Y' ");
    }
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
      getBillCardPanel().getBillData().getBodyItem("noriginalcurdiscountmny").getValueObject();
      for (int i = 0; i < fieldnames.length; i++) {
        if (getBillCardPanel().getBillData().getBodyItem(fieldnames[i]) != null) {
          getBillCardPanel().getBillData().getBodyItem(fieldnames[i]).setDecimalDigits(this.digit);

          String name = getBillCardPanel().getBodyItem(fieldnames[i]).getName();

          if (getBillCardPanel().getBodyPanel().hasShowCol(name)) {
            getBillCardPanel().getBodyPanel().resetTableCellRenderer(name);
          }
        }

      }

      fieldnames = new String[] { "ntaxmny", "nmny", "nsummny", "ndiscountmny", "nsubcursummny" };

      for (int i = 0; i < fieldnames.length; i++) {
        if (getBillCardPanel().getBillData().getBodyItem(fieldnames[i]) != null) {
          getBillCardPanel().getBillData().getBodyItem(fieldnames[i]).setDecimalDigits(localdigit);

          String name = getBillCardPanel().getBodyItem(fieldnames[i]).getName();

          if (getBillCardPanel().getBodyPanel().hasShowCol(name)) {
            getBillCardPanel().getBodyPanel().resetTableCellRenderer(name);
          }
        }

      }

      fieldnames = new String[] { "nassistcurdiscountmny", "nassistcursummny", "nassistcurmny", "nassistcurtaxmny" };

      for (int i = 0; i < fieldnames.length; i++) {
        if (getBillCardPanel().getBillData().getBodyItem(fieldnames[i]) != null) {
          getBillCardPanel().getBillData().getBodyItem(fieldnames[i]).setDecimalDigits(astdigit);

          String name = getBillCardPanel().getBodyItem(fieldnames[i]).getName();

          if (getBillCardPanel().getBodyPanel().hasShowCol(name)) {
            getBillCardPanel().getBodyPanel().resetTableCellRenderer(name);
          }
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

  private void countCardUniteMny()
  {
    for (int i = 0; i < getBillCardPanel().getBillModel().getRowCount(); i++) {
      UFDouble dSubsummny = (UFDouble)getBillCardPanel().getBodyValueAt(i, "nsubsummny");

      UFDouble dOrgcursummny = (UFDouble)getBillCardPanel().getBodyValueAt(i, "noriginalcursummny");

      UFDouble nuniteinvoicemny = (dSubsummny == null ? new UFDouble(0) : dSubsummny).sub(dOrgcursummny == null ? new UFDouble(0) : dOrgcursummny);

      getBillCardPanel().setBodyValueAt(nuniteinvoicemny, i, "nuniteinvoicemny");
    }
  }

  protected QueryConditionClient getStrikeQueryDlg()
  {
    this.dlgStrikeQuery = new QueryConditionClient(this);
    this.dlgStrikeQuery.setTempletID(getCorpPrimaryKey(), "40069907", getClientEnvironment().getUser().getPrimaryKey(), getBillCardPanel().getBusiType());

    this.dlgStrikeQuery.hideNormal();

    UIComboBox type = new UIComboBox();

    this.dlgStrikeQuery.setDefaultValue(null, "csourcebilltype", null, sAll);

    this.dlgStrikeQuery.setIsWarningWithNoInput(true);
    this.dlgStrikeQuery.setDefaultValue(null, "pk_corp", this.ce.getCorporation().getPrimaryKey(), null);

    String ccurrencytypeid = getBillCardPanel().getHeadItem("ccurrencyid").getValue();

    this.dlgStrikeQuery.setDefaultValue(null, "ccurrencytypeid", ccurrencytypeid, null);

    String ccustomerid = getCCustomerid();
    this.dlgStrikeQuery.setDefaultValue(null, "ccustomerid", ccustomerid, null);

    return this.dlgStrikeQuery;
  }

  public void initData(MessageVO msgvo)
  {
    if (msgvo == null) {
      this.bInMsgPanel = false;
    }
    else {
      SCMEnv.out("into initData ");
      SCMEnv.out("msgvo.getCorpPK():" + msgvo.getCorpPK());

      System.out.println("msgvo.getBusiTypePK():" + msgvo.getBusiTypePK());

      SCMEnv.out("msgvo.getCheckerCode():" + msgvo.getCheckerCode());

      SCMEnv.out("msgvo.getBillPK():" + msgvo.getBillPK());

      this.m_boCheck.setTag("APPROVE");

      ButtonObject[] btns = { this.m_boDocument, this.boAuditFlowStatus, this.m_boCheck };

      this.boAuditFlowStatus.setVisible(true);
      this.boAuditFlowStatus.setEnabled(true);
      this.m_boDocument.setEnabled(true);
      this.m_boCheck.setEnabled(true);

      setButtons(btns);

      SCMEnv.out("end initData ");
    }
  }

  protected void initUnite()
  {
    if (getBillCardPanel().getRowCount() <= 0)
      return;
    try
    {
      String[] formulas = new String[2];

      formulas[0] = "scalefactor->getColValue2(bd_convert,mainmeasrate,pk_invbasdoc,cinvbasdocid,pk_measdoc,cpackunitid)";

      formulas[1] = "fixedflag->getColValue2(bd_convert,fixedflag,pk_invbasdoc,cinvbasdocid,pk_measdoc,cpackunitid)";

      getBillCardPanel().getBillModel().execFormulas(formulas);

      UFBoolean fixedflag = null;
      UFBoolean bqtfixedflag = null;

      int i = 0; for (int count = getBillCardPanel().getRowCount(); i < count; i++) {
        String cunitid = (String)getBillCardPanel().getBodyValueAt(i, "cunitid");

        String cpackunitid = (String)getBillCardPanel().getBodyValueAt(i, "cpackunitid");

        if ((cunitid != null) && (cpackunitid != null)) {
          if (cpackunitid.equals(cunitid)) {
            getBillCardPanel().setBodyValueAt(new UFDouble(1), i, "scalefactor");

            getBillCardPanel().setBodyValueAt(new UFBoolean("Y"), i, "fixedflag");
          }
        }
        else
        {
          getBillCardPanel().setBodyValueAt(null, i, "cpackunitid");
          getBillCardPanel().setBodyValueAt(null, i, "cpackunitname");
        }

        String cquoteunitid = (String)getBillCardPanel().getBodyValueAt(i, "cquoteunitid");

        if ((cunitid != null) && (cquoteunitid != null)) {
          if (cquoteunitid.equals(cunitid)) {
            getBillCardPanel().setBodyValueAt(new UFDouble(1), i, "nqtscalefactor");

            getBillCardPanel().setBodyValueAt(new UFBoolean("Y"), i, "bqtfixedflag");
          }
        }
        else if ((cunitid != null) && (cquoteunitid == null))
        {
          getBillCardPanel().setBodyValueAt(cunitid, i, "cquoteunitid");

          getBillCardPanel().setBodyValueAt(getBillCardPanel().getBodyValueAt(i, "cunitname"), i, "cquoteunitname");

          getBillCardPanel().setBodyValueAt(new UFDouble(1), i, "nqtscalefactor");

          getBillCardPanel().setBodyValueAt(new UFBoolean("Y"), i, "bqtfixedflag");
        }
        else
        {
          getBillCardPanel().setBodyValueAt(null, i, "cquoteunitid");
          getBillCardPanel().setBodyValueAt(null, i, "cquoteunitname");
        }

        Object otemp = getBillCardPanel().getBodyValueAt(i, "fixedflag");

        fixedflag = (otemp == null) || (otemp.toString().trim().length() <= 0) ? new UFBoolean(false) : new UFBoolean(otemp.toString());

        if ((fixedflag != null) && (!fixedflag.booleanValue())) {
          getBillCardPanel().setBodyValueAt(nc.ui.so.so001.panel.bom.BillTools.calc(nc.ui.so.so001.panel.bom.BillTools.value(i, "nnumber", new UFDouble(1), getBillCardPanel().getBillModel()), nc.ui.so.so001.panel.bom.BillTools.value(i, "npacknumber", new UFDouble(1), getBillCardPanel().getBillModel()), 3), i, "scalefactor");
        }

        otemp = getBillCardPanel().getBodyValueAt(i, "bqtfixedflag");
        bqtfixedflag = (otemp == null) || (otemp.toString().trim().length() <= 0) ? new UFBoolean(false) : new UFBoolean(otemp.toString());

        if ((bqtfixedflag != null) && (!bqtfixedflag.booleanValue())) {
          getBillCardPanel().setBodyValueAt(nc.ui.so.so001.panel.bom.BillTools.calc(nc.ui.so.so001.panel.bom.BillTools.value(i, "nnumber", new UFDouble(1), getBillCardPanel().getBillModel()), nc.ui.so.so001.panel.bom.BillTools.value(i, "nquotenumber", nc.ui.so.so001.panel.bom.BillTools.value(i, "npacknumber", new UFDouble(1), getBillCardPanel().getBillModel()), getBillCardPanel().getBillModel()), 3), i, "bqtfixedflag");
        }

        InvVO voInv = null;
        if ((this.alInvs != null) && (this.alInvs.size() > i))
          voInv = (InvVO)this.alInvs.get(i);
        if (voInv == null)
          continue;
        if ((voInv.getIsAstUOMmgt() != null) && (voInv.getIsAstUOMmgt().intValue() == 1))
        {
          getBillCardPanel().setBodyValueAt("Y", i, "assistunit");
        }
        else getBillCardPanel().setBodyValueAt("N", i, "assistunit");

        voInv.setCastunitid(cpackunitid);
        voInv.setCastunitname((String)getBillCardPanel().getBodyValueAt(i, "cpackunitname"));
      }

    }
    catch (Exception ex)
    {
      SCMEnv.out("初始化换算率失败!");
    }
  }

  public void initVars(String pkcorp)
  {
    String salecorp = null;

    if ((pkcorp == null) || (pkcorp.trim().length() <= 0))
      salecorp = getClientEnvironment().getCorporation().getPrimaryKey();
    else {
      salecorp = pkcorp;
    }

    String[] syParas = { "SA08", "SA15", "SO27", "SO22", "SO06", "BD501", "BD502", "BD503", "BD505", "BD302", "SO30", "SA02", "SA13", "SO34", "SO36", "SO49", "SO59", "SO60" };

    String[] comnames = { "nc.bs.pub.para.SysInitDMO", "nc.bs.pub.para.SysInitDMO", "nc.impl.uap.bd.def.DefImpl", "nc.impl.uap.bd.def.DefImpl" };

    String[] funnames = { "queryBatchParaValues", "getParaBoolean", "queryDefVO", "queryDefVO" };

    ArrayList paramObjlist = new ArrayList();

    Object[] paramObjs = null;

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

  public void initListFormulaParse()
  {
    nc.ui.so.so001.panel.bom.BillTools.initItemKeys();
  }

  public boolean isLaborOrDiscount(String cinvbasdocid)
  {
    try
    {
      UFBoolean isDiscount = new UFBoolean(((Object[])(Object[])nc.ui.scm.pub.CacheTool.getCellValue("bd_invbasdoc", "pk_invbasdoc", "discountflag", cinvbasdocid))[0].toString());

      UFBoolean isLabor = new UFBoolean(((Object[])(Object[])nc.ui.scm.pub.CacheTool.getCellValue("bd_invbasdoc", "pk_invbasdoc", "laborflag", cinvbasdocid))[0].toString());

      return (isDiscount.booleanValue()) || (isLabor.booleanValue());
    }
    catch (Exception e)
    {
      SCMEnv.out(e.getMessage());
    }
    return true;
  }

  public boolean isStrikeBalance(SaleinvoiceVO saleinvoice, Hashtable hsArSub, boolean byProLine)
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

    if (byProLine == true) {
      this.summoneyByProductLine = new Hashtable();
      this.presummoneyByProductLine = new Hashtable();
      this.strikemoneyByProductLine = hsArSub;
      for (i = 0; i < saleinvoice.getChildrenVO().length; i++) {
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
        if (((UFDouble)this.strikemoneyByProductLine.get(key)).compareTo(this.summoneyByProductLine.get(key) == null ? new UFDouble(0) : (UFDouble)this.summoneyByProductLine.get(key)) > 0)
        {
          showErrorMessage(NCLangRes.getInstance().getStrByID("40060501", "UPP40060501-000091"));

          return false;
        }
      }

      return true;
    }

    this.summoney = new UFDouble(0);
    this.presummoney = new UFDouble(0);
    this.strikemoney = new UFDouble(0);
    for ( i = 0; i < saleinvoice.getChildrenVO().length; i++) {
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

  public void onAuditFlowStatus()
  {
    SaleinvoiceVO hvo = null;
    hvo = (SaleinvoiceVO)getVO(false);
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

  public void onUnite(ButtonObject bo)
  {
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
      StringBuffer sqlplus = new StringBuffer(" and ( csourcebilltype ");
      if (value.trim().equals("0")) {
        sqlplus.append(" in ('3H','3G') or csourcebilltype ='#&' ) ");
      }
      else if (value.trim().equals("3"))
        sqlplus.append(" ='#&' ) ");
      else if (value.trim().equals("1"))
        sqlplus.append(" ='3H' ) ");
      else {
        sqlplus.append(" ='3G' ) ");
      }
      this.m_strWhere = (dlgStrikeQuery.getWhereSQL(newvoCondition) + sqlplus);
    }

    String ccustomerid = getCCustomerid();
    this.m_strWhere = (this.m_strWhere + " and (so_arsub.ccustomerid = '" + ccustomerid + "')");

    if (this.SO36.booleanValue()) {
      this.m_strWhere = (this.m_strWhere + " and (so_arsubacct.cbiztypeid='" + getBillCardPanel().getBusiType() + "') ");
    }
    else {
      this.m_strWhere = (this.m_strWhere + " and (so_arsubacct.cbiztypeid='" + getBillCardPanel().getBusiType() + "' or so_arsubacct.cbiztypeid ='" + "#123456789*123456789" + "') ");
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
      else
        hsArsub.put(key, retVOs[i].getNsubmny());
      String arsubacctkey = retVOs[i].getPrimaryKey();

      if (this.hsTotalBykey.containsKey(arsubacctkey)) {
        this.hsTotalBykey.put(arsubacctkey, ((UFDouble)this.hsTotalBykey.get(arsubacctkey)).add(retVOs[i].getNsubmny()));
      }
      else {
        this.hsTotalBykey.put(arsubacctkey, retVOs[i].getNsubmny());
      }
    }
    if (!isStrikeOverflow(this.hsTotalBykey, this.hsQueryArsubDataBykey)) {
      this.hsTotalBykey = ((Hashtable)this.oldhsTotalBykey.clone());
      return;
    }

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
      this.oldhsTotalBykey = ((Hashtable)this.hsTotalBykey.clone());
    }
    else {
      this.hsTotalBykey = ((Hashtable)this.oldhsTotalBykey.clone());
    }
    showHintMessage(NCLangRes.getInstance().getStrByID("40060501", "UPT40060501-000048"));
  }

  public void onUniteCancel(ButtonObject bo)
  {
    String strikemny = getBillCardPanel().getHeadItem("nstrikemny").getValue();
    UFDouble nstrikemny = (strikemny == null) || (strikemny.trim().length() == 0) ? new UFDouble(0) : new UFDouble(strikemny);

    if (nstrikemny.doubleValue() == 0.0D) {
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
      if ((getBillCardPanel().getBillModel().getRowState(i) == 1) || (getBillCardPanel().getBillModel().getRowState(i) == 3))
        continue;
      getBillCardPanel().getBillModel().setRowState(i, 2);
    }

    getBillCardPanel().setHeadItem("nstrikemny", new UFDouble(0));

    for (int i = 0; i < getBillCardPanel().getBillModel().getRowCount(); i++) {
      getBillCardPanel().setBodyValueAt(new UFDouble(0.0D), i, "nuniteinvoicemny");
    }
    getBillCardPanel().execHeadFormula("nnetmny->ntotalsummny-nstrikemny");

    this.bstrikeflag = false;
    setButtonsState();

    Enumeration e = this.hsSelectedARSubHVO.keys();
    while (e.hasMoreElements()) {
      String key = (String)e.nextElement();
      this.hsSelectedARSubHVO.put(key, new UFDouble(0));
      this.hsTotalBykey.put(key, new UFDouble(0));
      this.oldhsTotalBykey.put(key, new UFDouble(0));
    }
    showHintMessage(NCLangRes.getInstance().getStrByID("40060501", "UPT40060501-000050"));
  }

  public void setInvFree(BillEditEvent e)
  {
    try
    {
      if (this.alInvs != null) {
        String sTempID1 = (String)getBillCardPanel().getBodyValueAt(e.getRow(), "cinventoryid");

        String sTempID2 = null;
        ArrayList alIDs = new ArrayList();
        alIDs.add(sTempID2);
        alIDs.add(sTempID1);
        alIDs.add(getClientEnvironment().getUser().getPrimaryKey());
        alIDs.add(getCorpPrimaryKey());
        InvVO voInv = nc.ui.so.pub.BillTools.queryInfo(new Integer(0), alIDs);

        if (this.alInvs.size() == 0)
          this.alInvs.add(voInv);
        else
          this.alInvs.set(e.getRow(), voInv);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      SCMEnv.out("自由项设置失败!");
    }
  }

  public void uniteInvoiceProcess(SaleinvoiceVO saleinvoice, Hashtable hsArSub, boolean byProLine)
  {
    int carddigit = getBillCardPanel().getBodyItem("noriginalcursummny").getDecimalDigits();

    if (byProLine == true) {
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

          if (((saleinvoicebody.getBlargessflag() != null) && (saleinvoicebody.getBlargessflag().booleanValue())) || (isLaborOrDiscount(saleinvoicebody.getCinvbasdocid())) || (saleinvoicebody.getCprolineid() == null))
          {
            continue;
          }

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

              if (this.strState.equals("ADD")) {
                getBillCardPanel().getBillModel().setRowState(i, 1);
              }
              else {
                getBillCardPanel().getBillModel().setRowState(i, 2);
              }

              remainMoney = remainMoney.sub(changemoney);
            } else {
              UFDouble money = saleinvoicebody.getNoriginalcursummny();

              remainMoney = remainMoney.setScale(0 - carddigit, 4);

              money = money.sub(remainMoney);

              getBillCardPanel().setBodyValueAt((getBillCardPanel().getBodyValueAt(i, "nuniteinvoicemny") == null ? new UFDouble(0.0D) : (UFDouble)getBillCardPanel().getBodyValueAt(i, "nuniteinvoicemny")).add(remainMoney), i, "nuniteinvoicemny");

              getBillCardPanel().setBodyValueAt(money, i, "noriginalcursummny");

              if (this.strState.equals("ADD")) {
                getBillCardPanel().getBillModel().setRowState(i, 1);
              }
              else {
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

        if (((saleinvoicebody.getBlargessflag() != null) && (saleinvoicebody.getBlargessflag().booleanValue())) || (isLaborOrDiscount(saleinvoicebody.getCinvbasdocid()))) {
          continue;
        }
        count++;
      }
      int i = 0; for (int j = 1; i < saleinvoice.getChildrenVO().length; i++) {
        SaleinvoiceBVO saleinvoicebody = (SaleinvoiceBVO)saleinvoice.getChildrenVO()[i];

        if (((saleinvoicebody.getBlargessflag() != null) && (saleinvoicebody.getBlargessflag().booleanValue())) || (isLaborOrDiscount(saleinvoicebody.getCinvbasdocid()))) {
          continue;
        }
        if (j == count) {
          UFDouble money = saleinvoicebody.getNoriginalcursummny();

          remainMoney = remainMoney.setScale(0 - carddigit, 4);

          money = money.sub(remainMoney);

          getBillCardPanel().setBodyValueAt((getBillCardPanel().getBodyValueAt(i, "nuniteinvoicemny") == null ? new UFDouble(0.0D) : (UFDouble)getBillCardPanel().getBodyValueAt(i, "nuniteinvoicemny")).add(remainMoney), i, "nuniteinvoicemny");

          getBillCardPanel().setBodyValueAt(money, i, "noriginalcursummny");

          if (this.strState.equals("ADD")) {
            getBillCardPanel().getBillModel().setRowState(i, 1);
          }
          else
            getBillCardPanel().getBillModel().setRowState(i, 2);
        }
        else {
          UFDouble money = saleinvoicebody.getNoriginalcursummny();

          UFDouble changemoney = money.multiply(this.strikemoney).div(this.presummoney);

          changemoney = changemoney.setScale(0 - carddigit, 4);

          money = money.sub(changemoney);

          getBillCardPanel().setBodyValueAt((getBillCardPanel().getBodyValueAt(i, "nuniteinvoicemny") == null ? new UFDouble(0.0D) : (UFDouble)getBillCardPanel().getBodyValueAt(i, "nuniteinvoicemny")).add(changemoney), i, "nuniteinvoicemny");

          getBillCardPanel().setBodyValueAt(money, i, "noriginalcursummny");

          if (this.strState.equals("ADD")) {
            getBillCardPanel().getBillModel().setRowState(i, 1);
          }
          else {
            getBillCardPanel().getBillModel().setRowState(i, 2);
          }

          remainMoney = remainMoney.sub(changemoney);
        }
        j++;
        calculateNumber(i, "noriginalcursummny");
      }

      getBillCardPanel().setHeadItem("nstrikemny", this.strikemoney.add(new UFDouble(getBillCardPanel().getHeadItem("nstrikemny").getValue())));

      getBillCardPanel().execHeadFormula("nnetmny->ntotalsummny-nstrikemny");
    }

    this.bstrikeflag = true;

    setButtonsState();

    for (int i = 0; i < getBillCardPanel().getBillModel().getRowCount(); i++) {
      getBillCardPanel().getBillModel().setCellEditable(i, "nuniteinvoicemny", true);
    }

    setButtonsState();
  }

  public void writeBackToARSub(SaleinvoiceVO saleinvoice, Hashtable hsArsub)
  {
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
    try
    {
      hsFullData = SaleinvoiceBO_Client.fillDataWithARSubAcct(this.hsTotalBykey);
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

          if ((cprolineid != null) && (cprolineid.equals(cchangeAtProlineid)))
          {
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
      } else {
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

          if ((cprolineid != null) && (cprolineid.equals(cchangeAtProlineid)))
          {
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
      } else {
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

  public void execBodyFormulas(String[] formulas, ArrayList lines)
  {
    BillCardPanel billcardpanel = getBillCardPanel();
    if ((formulas == null) || (formulas.length <= 0) || (lines == null) || (lines.size() <= 0) || (billcardpanel == null))
    {
      return;
    }

    FormulaParse f = billcardpanel.getFormulaParse();
    f.setExpressArray(formulas);

    VarryVO[] varrys = f.getVarryArray();

    Hashtable[] hs = new Hashtable[varrys.length];

    for (int i = 0; i < varrys.length; i++) {
      VarryVO varry = varrys[i];
      Hashtable h = new Hashtable();
      if (varry.getVarry() != null) {
        for (int j = 0; j < varry.getVarry().length; j++) {
          String key = varry.getVarry()[j];
          int column = billcardpanel.getBodyColByKey(key);

          BillItem item = billcardpanel.getBodyItems()[column];

          String[] os = new String[lines.size()];
          int row = 0; for (int count = lines.size(); row < count; row++)
          {
            Object o = "";
            int rowid = ((Integer)lines.get(row)).intValue();

            o = billcardpanel.getBillModel().getValueAt(rowid, key);
            String value = null;
            if ((o != null) && (!o.equals(""))) {
              if ((item.getDataType() == 1) || (item.getDataType() == 2))
              {
                value = o.toString();
              }
              else value = "\"" + o.toString() + "\"";
            }

            os[row] = value;
          }

          h.put(key, os);
        }
      }
      hs[i] = h;
    }

    f.setDataSArray(hs);

    String[][] results = f.getValueSArray();
    if (results != null)
      for (int i = 0; i < results.length; i++) {
        String[] result = results[i];
        VarryVO varry = varrys[i];

        int row = 0; for (int count = lines.size(); row < count; row++)
        {
          int rowid = ((Integer)lines.get(row)).intValue();

          String valueResult = result[row];
          String itemkey = varry.getFormulaName();

          if ((itemkey == null) || ((itemkey = itemkey.trim()).equals(""))) {
            continue;
          }
          int itemCol = billcardpanel.getBodyColByKey(itemkey);
          BillItem itemDest = billcardpanel.getBodyItems()[itemCol];

          if ((itemDest != null) && (itemDest.getDataType() == 1))
          {
            if ((valueResult != null) && (valueResult.indexOf(".") >= 0))
            {
              valueResult = valueResult.substring(0, valueResult.indexOf("."));
            }

          }

          billcardpanel.getBillModel().setValueAt(valueResult, rowid, itemkey);
        }
      }
  }

  public SaleinvoiceVO getDataFrom4CTo32(SaleinvoiceVO saleinvoice)
  {
    if (this.m_tools == null) {
      this.m_tools = new SaleInvoiceTools(this.BD302, this.currtype);
    }
    return this.m_tools.getDataFrom4CTo32(saleinvoice);
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

  private UFDouble[] getExchangerate(String currid, String billdate)
  {
    UFDouble[] retdb = new UFDouble[2];
    try {
      if ((this.BD302 == null) || (!this.BD302.booleanValue()))
      {
        try {
          retdb[0] = this.currtype.getRate(currid, null, billdate);
        }
        catch (Exception e) {
          SCMEnv.out(e.getMessage());
        }
      }
      else {
        UFDouble dCurr0 = this.currtype.getRate(this.currtype.getFracCurrPK(), this.currtype.getLocalCurrPK(), billdate);
        UFDouble dCurr1 = this.currtype.getRate(currid, this.currtype.getFracCurrPK(), billdate);

        retdb[0] = dCurr0;
        retdb[1] = (dCurr1 == null ? new UFDouble(0) : dCurr1);

        if (currid.equals(this.currtype.getLocalCurrPK())) {
          retdb[0] = new UFDouble(1);
          retdb[1] = new UFDouble(0);
        }
      }
    }
    catch (Exception exp) {
      exp.printStackTrace();
      throw new BusinessRuntimeException(exp.getMessage());
    }
    return retdb;
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
      afterNumberEdit(i, "nitemdiscountrate");
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

  public SOBillCardTools getBillCardTools()
  {
    if (this.soBillCardTools == null) {
      this.soBillCardTools = new SOBillCardTools(getBillCardPanel(), getClientEnvironment());
    }

    return this.soBillCardTools;
  }

  private String getCCustomerid()
  {
    if (getBillCardPanel().getRowCount() == 0) return null;
    int iselbodyrow = getBillCardPanel().getBillTable().getSelectedRow();
    if (iselbodyrow < 0) iselbodyrow = 0;
    return (String)getBillCardPanel().getBodyValueAt(iselbodyrow, "ccustomerid");
  }
}