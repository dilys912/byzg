package nc.ui.so.so001.panel;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import javax.swing.JComponent;
import javax.swing.ListSelectionModel;
import nc.bs.bd.b21.BusinessCurrencyRateUtil;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIMenuItem;
import nc.ui.pub.beans.UIRadioButton;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UISplitPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.beans.UITree;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillScrollPane;
import nc.ui.pub.linkoperate.ILinkAdd;
import nc.ui.pub.linkoperate.ILinkAddData;
import nc.ui.pub.linkoperate.ILinkApprove;
import nc.ui.pub.linkoperate.ILinkApproveData;
import nc.ui.pub.linkoperate.ILinkMaintain;
import nc.ui.pub.linkoperate.ILinkMaintainData;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.pub.pf.PfUtilBO_Client;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.pub.print.PrintEntry;
import nc.ui.pub.workflownote.FlowStateDlg;
import nc.ui.scm.extend.IFuncExtend;
import nc.ui.scm.file.DocumentManager;
import nc.ui.scm.message.Message;
import nc.ui.scm.print.IFreshTsListener;
import nc.ui.scm.print.PrintLogClient;
import nc.ui.scm.print.SalePubPrintDS;
import nc.ui.scm.pub.InvoInfoBYFormula;
import nc.ui.scm.pub.ScmPubHelper;
import nc.ui.scm.pub.query.ConvertQueryCondition;
import nc.ui.scm.pub.query.SCMQueryConditionDlg;
import nc.ui.scm.pub.report.BillRowNo;
import nc.ui.scm.pub.report.LocateDialog;
import nc.ui.scm.recordtime.RecordTimeHelper;
import nc.ui.scm.ref.prm.CustAddrRefModel;
import nc.ui.scm.sourcebill.SourceBillFlowDlg;
import nc.ui.so.pub.InvAttrCellRenderer;
import nc.ui.so.pub.SoTaskManager;
import nc.ui.so.so001.SaleOrderBO_Client;
import nc.ui.so.so001.panel.bom.BomorderBO_Client;
import nc.ui.so.so001.panel.card.BillTreeCardPanel;
import nc.ui.so.so001.panel.card.SOBillCardPanel;
import nc.ui.so.so001.panel.card.SOBillCardTools;
import nc.ui.so.so001.panel.list.SOBillListPanel;
import nc.ui.so.so001.panel.list.SaleBillListUI;
import nc.ui.so.so001.panel.list.SaleOrderVOCache;
import nc.ui.so.so016.OrderBalanceCardUI;
import nc.ui.so.so016.SOToolsBO_Client;
import nc.ui.so.so016.SaleReceiveUI;
import nc.utils.modify.is.IdetermineService;
import nc.vo.bd.CorpVO;
import nc.vo.bd.b06.PsndocVO;
import nc.vo.bd.b21.CurrinfoVO;
import nc.vo.dm.pub.DMDataVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.exp.DelDlvPlanException;
import nc.vo.scm.ic.bill.FreeVO;
import nc.vo.scm.ic.bill.InvVO;
import nc.vo.scm.ic.exp.ATPNotEnoughException;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.SaveHintException;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.scm.pub.smart.ObjectUtils;
import nc.vo.sm.UserVO;
import nc.vo.so.credit.CreditUtil;
import nc.vo.so.pub.CustCreditVO;
import nc.vo.so.so001.AtpCheckException;
import nc.vo.so.so001.AtpSetException;
import nc.vo.so.so001.BomorderHeaderVO;
import nc.vo.so.so001.BomorderItemVO;
import nc.vo.so.so001.BomorderVO;
import nc.vo.so.so001.Deal7DException;
import nc.vo.so.so001.SORowData;
import nc.vo.so.so001.SaleOrderVO;
import nc.vo.so.so001.SaleorderBVO;
import nc.vo.so.so001.SaleorderHVO;
import nc.vo.so.so001.SoVoConst;
import nc.vo.so.so006.ProfitHeaderVO;
import nc.vo.so.so006.ProfitItemVO;
import nc.vo.so.so006.ProfitVO;
import nc.vo.so.so016.SoVoTools;
import nc.vo.so.so120.CreditNotEnoughException;
import nc.vo.so.so120.PeriodNotEnoughException;

public abstract class SaleBillUI extends SaleBillListUI
  implements IFreshTsListener, ILinkAdd, ILinkMaintain, ILinkApprove, ILinkQuery
{
  protected ButtonObject boClose = new ButtonObject(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000119"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000120"), 0, "关闭");

  protected ButtonObject boOpen = new ButtonObject(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000060"), NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000157"), 0, "打开");

  protected ButtonObject boATP = new ButtonObject("ATP", "ATP", 0);
  protected ButtonObject boAfterAction;
  protected ButtonObject boConsignment = new ButtonObject(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000161"), NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000161"), 0, "发货");

  protected ButtonObject boOutStore = new ButtonObject(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000162"), NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000162"), 0, "出库");

  protected ButtonObject boMakeInvoice = new ButtonObject(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000163"), NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000163"), 0, "开票");

  protected ButtonObject boGathering = new ButtonObject(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000164"), NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000164"), 0, "收款");

  protected ButtonObject boRequestPurchase = new ButtonObject(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000165"), NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000165"), 0, "请购");
  protected ButtonObject[] bomButtonGroup;
  SourceBillFlowDlg soureDlg = null;

  private boolean isSaveStart = true;

  protected boolean binitOnNewByOther = false;
  private String strUISource;
  protected boolean bInMsgPanel = false;

  protected String headwarehouseRefWhereSql = null;

  protected OrderBalanceCardUI orderBalanceUI = null;

  protected SaleReceiveUI saleReceiveUI = null;

  protected SoTaskManager soTaskManager = null;

  protected PrintLogClient printLogClient = null;

  protected Vector vRowATPStatus_bak = null;

  protected ArrayList alInvs_bak = null;

  protected HashMap hsMapSendAudit = new HashMap();
  protected SaleOrderPrintDataInterface m_dataSource;
  protected PrintEntry m_print;
  private boolean b_query;
  ProccDlg proccdlg;
  WorkThread work;
  protected ATPUIQryDelegate atpQry = null;

  protected SOBillCardTools soBillCardTools = null;

  private ButtonObject boDocument1 = new ButtonObject(NCLangRes.getInstance().getStrByID("40060301", "UPT40060301-000026"), NCLangRes.getInstance().getStrByID("40060301", "UPT40060301-000026"), "文档管理");

  private ButtonObject boAuditFlowStatus1 = new ButtonObject(NCLangRes.getInstance().getStrByID("40060301", "UPT40060301-000025"), NCLangRes.getInstance().getStrByID("40060301", "UPT40060301-000025"), "审批流状态");

  public SaleBillUI()
  {
  }

  public SaleBillUI(String pk_corp, String billtype, String busitype, String operator, String id)
  {
    super(pk_corp, billtype, busitype, operator, id);
  }

  protected void loadAllBtns() {
    super.loadAllBtns();
    getBoAfterAction();
  }

  private boolean calculatePreceive(SaleOrderVO saleorder)
  {
    BillItem bi = getBillCardPanel().getHeadItem("npreceiverate");
    if (bi == null) {
      SCMEnv.out("错误：预收款比例在模版中不存在!");
      return true;
    }
    bi = getBillCardPanel().getHeadItem("npreceivemny");
    if (bi == null) {
      SCMEnv.out("错误：预收款金额在模版中不存在!");
      return true;
    }

    UFDouble npreceiverate = null;
    UFDouble npreceivemny = null;

    UFDouble noriginalcursummny = new UFDouble(0);

    SaleorderHVO ordhvo = saleorder.getHeadVO();
    SaleorderBVO[] ordbvos = saleorder.getBodyVOs();
    int i = 0; for (int loop = ordbvos.length; i < loop; i++)
    {
      UFBoolean boosflag = ordbvos[i].getBoosflag() == null ? new UFBoolean(false) : ordbvos[i].getBoosflag();

      UFBoolean blargessflag = ordbvos[i].getBlargessflag() == null ? new UFBoolean(false) : ordbvos[i].getBlargessflag();

      if ((ordbvos[i].getCinventoryid() != null) && (ordbvos[i].getCinventoryid().trim().length() > 0))
      {
        if ((!boosflag.booleanValue()) && (!blargessflag.booleanValue()))
        {
          UFDouble dobj = ordbvos[i].getNoriginalcursummny();
          if (dobj == null)
            dobj = new UFDouble(0);
          noriginalcursummny = noriginalcursummny.add(dobj);
        }
      }
    }
    npreceiverate = ordhvo.getNpreceiverate();
    npreceivemny = ordhvo.getNpreceivemny();

    if ((npreceiverate == null) && (npreceivemny == null)) {
      return true;
    }

    if ((npreceiverate != null) && (npreceiverate.doubleValue() == 0.0D) && (npreceivemny != null) && (npreceivemny.doubleValue() == 0.0D))
    {
      return true;
    }

    if (noriginalcursummny.doubleValue() < 0.0D) {
      showErrorMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000174"));

      return false;
    }

    if (noriginalcursummny.doubleValue() == 0.0D) {
      ordhvo.setNpreceiverate(null);
      ordhvo.setNpreceivemny(null);
      getBillCardPanel().setHeadItem("npreceiverate", null);
      getBillCardPanel().setHeadItem("npreceivemny", null);
      return true;
    }

    if (npreceiverate == null) {
      if ((npreceivemny.doubleValue() < 0.0D) || (npreceivemny.doubleValue() > noriginalcursummny.doubleValue()))
      {
        showErrorMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000175"));

        return false;
      }
      npreceiverate = npreceivemny.div(noriginalcursummny).multiply(100.0D);
      ordhvo.setNpreceiverate(npreceiverate);
      getBillCardPanel().setHeadItem("npreceiverate", npreceiverate);
      getBillCardPanel().setHeadItem("npreceivemny", npreceivemny);
      return true;
    }

    if (npreceivemny == null) {
      if ((npreceiverate.doubleValue() < 0.0D) || (npreceiverate.doubleValue() > 100.0D))
      {
        showErrorMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000176"));

        return false;
      }
      npreceivemny = noriginalcursummny.multiply(npreceiverate).div(100.0D);
      ordhvo.setNpreceivemny(npreceivemny);
      getBillCardPanel().setHeadItem("npreceiverate", npreceiverate);
      getBillCardPanel().setHeadItem("npreceivemny", npreceivemny);
      return true;
    }

    if ("比例".equals(this.SO_21)) {
      if ((npreceiverate.doubleValue() < 0.0D) || (npreceiverate.doubleValue() > 100.0D))
      {
        showErrorMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000176"));

        return false;
      }
      npreceivemny = noriginalcursummny.multiply(npreceiverate).div(100.0D);
    } else if ("金额".equals(this.SO_21)) {
      if ((npreceivemny.doubleValue() < 0.0D) || (npreceivemny.doubleValue() > noriginalcursummny.doubleValue()))
      {
        showErrorMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000178"));

        return false;
      }
      npreceiverate = npreceivemny.div(noriginalcursummny).multiply(100.0D);
    }

    if (npreceiverate.doubleValue() > 100.0D) {
      showErrorMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000179"));

      return false;
    }

    ordhvo.setNpreceiverate(npreceiverate);
    ordhvo.setNpreceivemny(npreceivemny);
    getBillCardPanel().setHeadItem("npreceiverate", npreceiverate);
    getBillCardPanel().setHeadItem("npreceivemny", npreceivemny);
    return true;
  }

  private boolean checkSourceComb(AggregatedValueObject[] voSource)
  {
    SaleorderBVO[] saleBs = (SaleorderBVO[])voSource[0].getChildrenVO();
    if ((saleBs == null) || (saleBs.length == 0))
      return true;
    String creceipttype = saleBs[0].getCreceipttype();
    if ((creceipttype == null) || (creceipttype.length() == 0)) {
      return true;
    }

    boolean bCustomer = false;
    boolean bcurr = false;
    boolean bsaleorg = false;
    if ((creceipttype.equals("Z4")) || (creceipttype.equals("Z3")))
    {
      bCustomer = true;
      bcurr = true;
    }
    else if (creceipttype.equals("37"))
    {
      bCustomer = true;
      bcurr = true;
      bsaleorg = true;
    } else if (creceipttype.equals("4H"))
    {
      bCustomer = true;
    }

    if (voSource.length > 1)
    {
      if (bCustomer) {
        String ccustomerid0 = ((SaleorderHVO)voSource[0].getParentVO()).getCcustomerid();

        if (ccustomerid0 != null) {
          for (int i = 1; i < voSource.length; i++) {
            String ccustomerid = ((SaleorderHVO)voSource[i].getParentVO()).getCcustomerid();

            if ((ccustomerid != null) && 
              (!ccustomerid.trim().equals(ccustomerid0.trim()))) {
              showErrorMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000182"));

              return false;
            }
          }
        }

      }

      if (bcurr) {
        SaleorderBVO[] salebodyVO = (SaleorderBVO[])voSource[0].getChildrenVO();

        String currencyid0 = salebodyVO[0].getCcurrencytypeid();
        if (currencyid0 != null) {
          for (int i = 1; i < voSource.length; i++) {
            salebodyVO = (SaleorderBVO[])voSource[i].getChildrenVO();

            String currencyid = salebodyVO[0].getCcurrencytypeid();
            if (!currencyid.trim().equals(currencyid0.trim())) {
              showErrorMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000183"));

              return false;
            }
          }
        }
      }

      if (bsaleorg) {
        String ccustomerid0 = ((SaleorderHVO)voSource[0].getParentVO()).getCsalecorpid();

        if (ccustomerid0 != null) {
          for (int i = 1; i < voSource.length; i++) {
            String ccustomerid = ((SaleorderHVO)voSource[i].getParentVO()).getCsalecorpid();

            if ((ccustomerid != null) && 
              (!ccustomerid.trim().equals(ccustomerid0.trim()))) {
              showErrorMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000513"));

              return false;
            }
          }

        }

      }

    }

    return true;
  }

  private Object getAssistantPara(ButtonObject bo)
    throws ValidationException
  {
    int row = getBillListPanel().getHeadTable().getSelectedRow();

    Object o = null;

    if ((bo.getTag().equals("OrderAlterRpt")) || (bo.getTag().equals("OrderExecRpt")))
    {
      if (this.strShowState.equals("列表")) {
        o = getBillListPanel().getHeadBillModel().getValueAt(this.num, "csaleid");
      }
      else {
        o = getBillCardPanel().getHeadItem("csaleid").getValue();
      }
    }

    if (bo.getTag().equals("ATP")) {
      o = getVO(true);
    }

    if (bo.getTag().equals("CustInfo")) {
      if (this.strShowState.equals("列表")) {
        o = getBillListPanel().getHeadBillModel().getValueAt(this.num, "ccustomerid");
      }
      else {
        o = getBillCardPanel().getHeadItem("ccustomerid").getValue();
      }
    }

    if (bo.getTag().equals("CustCredited")) {
      String sCust = null;
      String sBiztype = null;
      String sProductLine = null;
      if (this.strShowState.equals("列表")) {
        Object oTemp = getBillListPanel().getHeadBillModel().getValueAt(this.num, "ccustomerid");

        sCust = oTemp == null ? null : oTemp.toString();

        oTemp = getBillListPanel().getHeadBillModel().getValueAt(this.num, "cbiztype");

        sBiztype = oTemp == null ? null : oTemp.toString();

        oTemp = getBillListPanel().getBodyBillModel().getValueAt(0, "cprolineid");

        sProductLine = oTemp == null ? null : oTemp.toString();
      } else {
        sCust = (String)getBillCardPanel().getHeadItem("ccustomerid").getValueObject();

        sBiztype = getBillCardPanel().getBusiType();
        sProductLine = (String)getBillCardPanel().getBodyValueAt(0, "cprolineid");
      }

      CustCreditVO voCredit = new CustCreditVO();
      voCredit.setPk_cumandoc(sCust);
      voCredit.setCbiztype(sBiztype);

      if ((this.SO27 == null) || (!this.SO27.booleanValue()))
        voCredit.setCproductline(null);
      else {
        voCredit.setCproductline(sProductLine);
      }
      o = voCredit;
    }

    if (bo.getTag().equals("FlowState")) {
      ArrayList alTemp = new ArrayList();
      alTemp.add(getBillType());
      if (this.strShowState.equals("列表")) {
        alTemp.add(getBillListPanel().getHeadBillModel().getValueAt(this.num, "csaleid"));
      }
      else {
        alTemp.add(getBillCardPanel().getHeadItem("csaleid").getValueObject());
      }

      o = alTemp;
    }

    if (bo.getTag().equals("Prifit")) {
      if (this.strShowState.equals("列表")) {
        ProfitHeaderVO headVO = new ProfitHeaderVO();

        headVO.setPkcorp(getCorpPrimaryKey());

        headVO.setCcalbodyid((String)getBillListPanel().getHeadBillModel().getValueAt(row, "ccalbodyid"));

        headVO.setBilltype(getBillType());

        if (getBillListPanel().getBodyTable().getRowCount() > 0) {
          String curID = (String)getBillListPanel().getBodyBillModel().getValueAt(0, "ccurrencytypeid");

          headVO.setCurrencyid(curID);
        }

        headVO.setCcalbodyname((String)getBillListPanel().getHeadBillModel().getValueAt(row, "ccalbodyname"));

        ProfitItemVO[] bodyVOs = new ProfitItemVO[getBillListPanel().getBodyBillModel().getRowCount()];

        for (int i = 0; i < bodyVOs.length; i++) {
          ProfitItemVO bodyVO = new ProfitItemVO();

          String creccalbodyid = (String)getBillListPanel().getBodyBillModel().getValueAt(i, "creccalbodyid");

          if ((creccalbodyid != null) && (creccalbodyid.trim().length() > 0))
          {
            bodyVO.setCbodycalbodyid((String)getBillListPanel().getBodyBillModel().getValueAt(i, "creccalbodyid"));

            bodyVO.setCbodycalbodyname((String)getBillListPanel().getBodyBillModel().getValueAt(i, "creccalbody"));

            bodyVO.setCbodywarehouseid((String)getBillListPanel().getBodyBillModel().getValueAt(i, "crecwareid"));

            bodyVO.setCbodywarehousename((String)getBillListPanel().getBodyBillModel().getValueAt(i, "crecwarehouse"));
          }
          else
          {
            bodyVO.setCbodycalbodyid((String)getBillListPanel().getBodyBillModel().getValueAt(i, "cadvisecalbodyid"));

            bodyVO.setCbodycalbodyname((String)getBillListPanel().getBodyBillModel().getValueAt(i, "cadvisecalbody"));

            bodyVO.setCbodywarehouseid((String)getBillListPanel().getBodyBillModel().getValueAt(i, "cbodywarehouseid"));

            bodyVO.setCbodywarehousename((String)getBillListPanel().getBodyBillModel().getValueAt(i, "cbodywarehousename"));
          }

          bodyVO.setCinventoryid((String)getBillListPanel().getBodyBillModel().getValueAt(i, "cinventoryid"));

          bodyVO.setCode((String)getBillListPanel().getBodyBillModel().getValueAt(i, "cinventorycode"));

          bodyVO.setName((String)getBillListPanel().getBodyBillModel().getValueAt(i, "cinventoryname"));

          bodyVO.setSize((String)getBillListPanel().getBodyBillModel().getValueAt(i, "GGXX"));

          bodyVO.setCbatchid((String)getBillListPanel().getBodyBillModel().getValueAt(i, "cbatchid"));

          bodyVO.setNumber((UFDouble)getBillListPanel().getBodyBillModel().getValueAt(i, "nnumber"));

          bodyVO.setNnetprice((UFDouble)getBillListPanel().getBodyBillModel().getValueAt(i, "nnetprice") == null ? new UFDouble(0) : (UFDouble)getBillListPanel().getBodyBillModel().getValueAt(i, "nnetprice"));

          bodyVO.setNmny((UFDouble)getBillListPanel().getBodyBillModel().getValueAt(i, "nmny") == null ? new UFDouble(0) : (UFDouble)getBillListPanel().getBodyBillModel().getValueAt(i, "nmny"));

          bodyVOs[i] = bodyVO;
          if ((getBillListPanel().getBodyBillModel().getValueAt(i, "blargessflag") != null) && (getBillListPanel().getBodyBillModel().getValueAt(i, "blargessflag").toString().equals("false")))
          {
            bodyVO.m_blargessflag = new UFBoolean(false);
          }
          else bodyVO.m_blargessflag = new UFBoolean(true);
        }
        ProfitVO profit = new ProfitVO();
        profit.setParentVO(headVO);
        profit.setChildrenVO(bodyVOs);
        profit.validate();
        o = profit;
      }
      else {
        o = getProfitVOonCardPanel();
      }
    }
    return o;
  }

  private Object getProfitVOonCardPanel()
    throws ValidationException
  {
    ProfitHeaderVO headVO = new ProfitHeaderVO();

    headVO.setPkcorp(getCorpPrimaryKey());

    headVO.setCcalbodyid(getBillCardPanel().getHeadItem("ccalbodyid").getValue());

    UIRefPane calBodyName = (UIRefPane)getBillCardPanel().getHeadItem("ccalbodyid").getComponent();

    headVO.setCcalbodyname(calBodyName.getRefName());

    headVO.setBilltype(getBillType());

    headVO.setCurrencyid((String)getBillCardPanel().getHeadItem("ccurrencytypeid").getValueObject());

    ProfitItemVO[] bodyVOs = new ProfitItemVO[getBillCardPanel().getRowCount()];

    for (int i = 0; i < bodyVOs.length; i++) {
      ProfitItemVO bodyVO = new ProfitItemVO();

      String creccalbodyid = (String)getBillCardPanel().getBodyValueAt(i, "creccalbodyid");

      if ((creccalbodyid != null) && (creccalbodyid.trim().length() > 0))
      {
        bodyVO.setCbodycalbodyid((String)getBillCardPanel().getBodyValueAt(i, "creccalbodyid"));

        bodyVO.setCbodycalbodyname((String)getBillCardPanel().getBodyValueAt(i, "creccalbody"));

        bodyVO.setCbodywarehouseid((String)getBillCardPanel().getBodyValueAt(i, "crecwareid"));

        bodyVO.setCbodywarehousename((String)getBillCardPanel().getBodyValueAt(i, "crecwarehouse"));
      }
      else
      {
        bodyVO.setCbodycalbodyid((String)getBillCardPanel().getBodyValueAt(i, "cadvisecalbodyid"));

        bodyVO.setCbodycalbodyname((String)getBillCardPanel().getBodyValueAt(i, "cadvisecalbody"));

        bodyVO.setCbodywarehouseid((String)getBillCardPanel().getBodyValueAt(i, "cbodywarehouseid"));

        bodyVO.setCbodywarehousename((String)getBillCardPanel().getBodyValueAt(i, "cbodywarehousename"));
      }

      bodyVO.setCinventoryid((String)getBillCardPanel().getBodyValueAt(i, "cinventoryid"));

      bodyVO.setCode((String)getBillCardPanel().getBodyValueAt(i, "cinventorycode"));

      bodyVO.setName((String)getBillCardPanel().getBodyValueAt(i, "cinventoryname"));

      bodyVO.setSize((String)getBillCardPanel().getBodyValueAt(i, "GGXX"));

      bodyVO.setCbatchid((String)getBillCardPanel().getBodyValueAt(i, "cbatchid"));

      bodyVO.setNumber((UFDouble)getBillCardPanel().getBodyValueAt(i, "nnumber"));

      Object value = getBillCardPanel().getBodyValueAt(i, "nnetprice");
      if ((value == null) || (value.toString().toString().equals("")))
        value = new UFDouble(0);
      bodyVO.setNnetprice((UFDouble)value);
      if ((getBillCardPanel().getBodyValueAt(i, "blargessflag") != null) && (getBillCardPanel().getBodyValueAt(i, "blargessflag").toString().equals("false")))
      {
        bodyVO.m_blargessflag = new UFBoolean(false);
      }
      else bodyVO.m_blargessflag = new UFBoolean(true);
      value = getBillCardPanel().getBodyValueAt(i, "nmny");
      if ((value == null) || (value.toString().toString().equals("")))
        value = new UFDouble(0);
      bodyVO.setNmny((UFDouble)value);
      bodyVOs[i] = bodyVO;
    }
    ProfitVO profit = new ProfitVO();
    profit.setParentVO(headVO);
    profit.setChildrenVO(bodyVOs);
    profit.validate();

    return profit;
  }

  public abstract String getBillButtonAction(ButtonObject paramButtonObject);

  public abstract String getBillButtonState();

  public abstract ButtonObject[] getBillButtons();

  public abstract String getBillID();

  private String getBillIDSource()
  {
    if (getBillListPanel().isShowing()) {
      this.num = getBillListPanel().getHeadTable().getSelectedRow();
    }

    if ((this.num >= 0) && (this.num < getBillListPanel().getHeadBillModel().getRowCount()))
    {
      return (String)getBillListPanel().getHeadBillModel().getValueAt(this.num, "csaleid");
    }

    return null;
  }

  public String getBillType()
  {
    return "30";
  }

  private SaleOrderVO getLineNumber(SaleOrderVO voSource, SaleOrderVO voChange)
  {
    SaleorderBVO[] voBodys = (SaleorderBVO[])voSource.getChildrenVO();
    Vector vec = new Vector();

    for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
      if (getBillCardPanel().getBillModel().getRowState(i) != 0) {
        voBodys[i].setNlinenumber(new Integer(i + 1));
        vec.addElement(voBodys[i]);
      }
    }

    SaleorderBVO[] voChangeBodys = (SaleorderBVO[])voChange.getChildrenVO();

    for (int i = 0; i < voChangeBodys.length; i++) {
      if (voChangeBodys[i].getStatus() == 3) {
        vec.addElement(voChangeBodys[i]);
      }
    }
    SaleorderBVO[] retBodys = new SaleorderBVO[vec.size()];
    if (vec.size() > 0) {
      vec.copyInto(retBodys);
    }

    SaleOrderVO copyvo = new SaleOrderVO();
    copyvo.setParentVO(voSource.getParentVO());
    copyvo.setChildrenVO(retBodys);

    return copyvo;
  }

  private int[] getNotCheckFreeItemLine()
  {
    if ((getBillCardPanel().alInvs == null) || (getBillCardPanel().alInvs.size() <= 0))
    {
      return null;
    }ArrayList indexlist = new ArrayList();
    for (int i = 0; i < getBillCardPanel().alInvs.size(); i++) {
      InvVO vo = (InvVO)getBillCardPanel().alInvs.get(i);
      if (vo != null)
      {
        if (vo.getFreeItemVO() != null) {
          if (((vo.getFreeItemVO().getVfreeid1() == null) || (vo.getFreeItemVO().getVfreeid1().length() <= 0)) && ((vo.getFreeItemVO().getVfreeid2() == null) || (vo.getFreeItemVO().getVfreeid2().length() <= 0)) && ((vo.getFreeItemVO().getVfreeid3() == null) || (vo.getFreeItemVO().getVfreeid3().length() <= 0)) && ((vo.getFreeItemVO().getVfreeid4() == null) || (vo.getFreeItemVO().getVfreeid4().length() <= 0)) && ((vo.getFreeItemVO().getVfreeid5() == null) || (vo.getFreeItemVO().getVfreeid5().length() <= 0)) && ((vo.getFreeItemVO().getVfreeid6() == null) || (vo.getFreeItemVO().getVfreeid6().length() <= 0)) && ((vo.getFreeItemVO().getVfreeid7() == null) || (vo.getFreeItemVO().getVfreeid7().length() <= 0)) && ((vo.getFreeItemVO().getVfreeid8() == null) || (vo.getFreeItemVO().getVfreeid8().length() <= 0)) && ((vo.getFreeItemVO().getVfreeid9() == null) || (vo.getFreeItemVO().getVfreeid9().length() <= 0)) && ((vo.getFreeItemVO().getVfreeid10() == null) || (vo.getFreeItemVO().getVfreeid10().length() <= 0)))
          {
            indexlist.add(new Integer(i));
          }
        }
        else indexlist.add(new Integer(i));
      }
    }

    if (indexlist.size() <= 0) {
      return null;
    }
    int[] ret = new int[indexlist.size()];
    for (int i = 0; i < indexlist.size(); i++) {
      ret[i] = ((Integer)indexlist.get(i)).intValue();
    }
    return ret;
  }

  public SourceBillFlowDlg getSourceDlg()
  {
    this.soureDlg = new SourceBillFlowDlg(this, getBillType(), getBillIDSource(), getBillCardPanel().getBusiType(), getBillCardPanel().getOperator(), getBillCardPanel().getCorp());

    return this.soureDlg;
  }

  public abstract String getTitle();

  protected void handleSaveException(Throwable ex)
  {
    showWarningMessage(ex.getMessage());
    showHintMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000185"));
  }

  protected void retElseBtn(String billbuttonstate, String billbuttonaction)
  {
    if (billbuttonaction.equals("Order001")) {
      this.boAction.removeAllChildren();

      if (!billbuttonstate.equals("Order001"))
      {
        if ((billbuttonstate.equals("Order002")) || (billbuttonstate.equals("Order003")))
        {
          this.boAction.addChildButton(this.boSendAudit);

          this.boAction.addChildButton(this.boCancelAudit);
          this.boAction.addChildButton(this.boFreeze);
          this.boAction.addChildButton(this.boCancelFreeze);
          this.boAction.addChildButton(this.boFinish);
        }

      }

    }
    else if (billbuttonaction.equals("Order003"))
    {
      this.boAssistant.removeAllChildren();

      if (billbuttonstate.equals("Order001"))
      {
        this.boAssistant.addChildButton(this.boCustCredit);
        this.boAssistant.addChildButton(this.boCustInfo);
        this.boAssistant.addChildButton(this.boPrifit);
        this.boAssistant.addChildButton(this.boOnHandShowHidden);
      }
      else if ((billbuttonstate.equals("Order002")) || (billbuttonstate.equals("Order003")))
      {
        this.boAssistant.addChildButton(this.boRefundment);
        this.boAssistant.addChildButton(this.boCachPay);
        this.boAssistant.addChildButton(this.boOrdBalance);
        this.boAssistant.addChildButton(this.boStockLock);
        this.boAssistant.addChildButton(this.boDocument);
        this.boAssistant.addChildButton(this.boBom);
      }

    }
    else if (billbuttonaction.equals("BomOrder001")) {
      this.boAction.removeAllChildren();

      if (billbuttonstate.equals("BomOrder001")) {
        this.boAction.addChildButton(this.boAudit);
      }
      else if (billbuttonstate.equals("BomOrder002")) {
        this.boAction.addChildButton(this.boAudit);
        this.boAction.addChildButton(this.boCancelAudit);
      }
      else if (billbuttonstate.equals("BomOrder003")) {
        this.boAction.addChildButton(this.boAudit);
      }

    }
    else if (!billbuttonaction.equals("BomOrder002"));
  }

  protected void initButtons()
  {
    PfUtilClient.retBusinessBtn(this.boBusiType, getCorpPrimaryKey(), getBillType());

    if (this.strState.equals("BOM")) {
      setButtons(this.bomButtonGroup);
    }
    else {
      initLineButton();

      if ((this.boBusiType.getChildButtonGroup() != null) && (this.boBusiType.getChildButtonGroup().length > 0))
      {
        ButtonObject[] bo = this.boBusiType.getChildButtonGroup();
        for (int i = 0; i < bo.length; i++) {
          bo[i].setName(bo[i].getName());
        }
        this.boBusiType.setChildButtonGroup(bo);

        this.boBusiType.setTag(this.boBusiType.getChildButtonGroup()[0].getTag());
        this.boBusiType.getChildButtonGroup()[0].setSelected(true);
        this.boBusiType.setCheckboxGroup(true);

        PfUtilClient.retAddBtn(this.boAdd, getCorpPrimaryKey(), getBillType(), this.boBusiType.getChildButtonGroup()[0]);

        bo = this.boAdd.getChildButtonGroup();
        for (int i = 0; i < bo.length; i++) {
          bo[i].setName(bo[i].getName());
        }
        this.boAdd.setChildButtonGroup(bo);

        retElseBtn(getBillButtonState(), getBillButtonAction(this.boAction));
        retElseBtn(getBillButtonState(), getBillButtonAction(this.boAssistant));
      }
      else {
        SCMEnv.out("没有初始化业务类型!");
      }

      setButtons(getBillButtons());
    }
  }

  private void initInvList()
  {
    getBillCardPanel().alInvs = new ArrayList();
    if (getBillCardPanel().getRowCount() <= 0) {
      return;
    }
    try
    {
      InvVO[] invvos = new InvVO[getBillCardPanel().getRowCount()];
      for (int i = 0; i < invvos.length; i++) {
        invvos[i] = new InvVO();
        invvos[i].setCinventoryid((String)getBillCardPanel().getBodyValueAt(i, "cinventoryid"));
      }

      InvoInfoBYFormula invvosget = new InvoInfoBYFormula();
      invvos = invvosget.getQuryInvVOs(invvos, false, true, 1);

      for (int i = 0; i < invvos.length; i++) {
        getBillCardPanel().alInvs.add(invvos[i]);
      }
      if (getBillCardPanel().alInvs != null)
        for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
          InvVO voInv = (InvVO)getBillCardPanel().alInvs.get(i);
          getBillCardPanel().setBodyFreeValue(i, voInv);
        }
    }
    catch (Exception ex) {
      SCMEnv.out("自由项设置失败!");
    }
  }

  private void initLineButton()
  {
    this.boLine.removeAllChildren();
    this.boLine.addChildButton(this.boAddLine);
    this.boLine.addChildButton(this.boDelLine);
    this.boLine.addChildButton(this.boCopyLine);
    this.boLine.addChildButton(this.boPasteLine);
  }

  public void loadCardData()
  {
    try
    {
      long s = System.currentTimeMillis();
      boolean bisCalculate = getBillCardPanel().getBillModel().isNeedCalculate();

      getBillCardPanel().getBillModel().setNeedCalculate(false);

      SaleOrderVO saleorderVO = (SaleOrderVO)SaleOrderBO_Client.queryData(getBillID());

      if ((saleorderVO != null) && (saleorderVO.getHeadVO().getCsaleid() != null))
      {
        saleorderVO.getHeadVO().setNreceiptcathmny(SaleOrderBO_Client.queryCachPayByOrdId(saleorderVO.getHeadVO().getCsaleid()));
      }

      SCMEnv.out("数据读取[共用时" + (System.currentTimeMillis() - s) + "]");
      s = System.currentTimeMillis();

      getBillCardPanel().setPanelByCurrency(((SaleorderBVO)saleorderVO.getChildrenVO()[0]).getCcurrencytypeid());

      UIRefPane vreceiveaddress = (UIRefPane)getBillCardPanel().getHeadItem("vreceiveaddress").getComponent();

      vreceiveaddress.setAutoCheck(false);

      getBillCardPanel().setBillValueVO(saleorderVO);

      getBillCardPanel().getBillModel().execLoadFormula();

      copyIstotal();
      SCMEnv.out("数据设置[共用时" + (System.currentTimeMillis() - s) + "]");
      long s1 = System.currentTimeMillis();

      getBillCardPanel().getBillModel().execLoadFormula();

      SCMEnv.out("执行公式[共用时" + (System.currentTimeMillis() - s1) + "]");

      s1 = System.currentTimeMillis();

      if (getBillCardPanel().getHeadItem("creceiptcustomerid") != null) {
        ((CustAddrRefModel)vreceiveaddress.getRefModel()).setCustId((String)getBillCardPanel().getHeadItem("creceiptcustomerid").getValueObject());
      }

      Object temp = saleorderVO.getParentVO().getAttributeValue("vreceiveaddress");

      String formula = "getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,creceiptcustomerid)";
      getBillCardPanel().execHeadFormula(formula);

      vreceiveaddress.getUITextField().setText(temp == null ? "" : temp.toString());

      UFDouble ndiscountrate = getBillCardTools().getHeadUFDoubleValue("ndiscountrate");

      if (ndiscountrate == null) {
        ndiscountrate = getBillCardTools().getBodyUFDoubleValue(0, "ndiscountrate");
      }

      if (ndiscountrate == null) {
        ndiscountrate = new UFDouble(100);
      }
      getBillCardPanel().setHeadItem("ndiscountrate", ndiscountrate);

      getBillCardPanel().initFreeItem();

      setHeadDefaultData();

      getBillCardPanel().initUnit();

      SCMEnv.out("加载表头[共用时" + (System.currentTimeMillis() - s1) + "]");

      s1 = System.currentTimeMillis();

      if ((getBillType().equals("30")) || (getBillType().equals("3A")))
      {
        String[] formulas = { "wholemanaflag->getColValue(bd_invmandoc,wholemanaflag,pk_invmandoc,cinventoryid)", "isconfigable->getColValue(bd_invmandoc,isconfigable,pk_invmandoc,cinventoryid)", "isspecialty->getColValue(bd_invmandoc,isspecialty,pk_invmandoc,cinventoryid)" };

        getBillCardPanel().getBillModel().execFormulas(formulas);
      }

      String[] appendFormula = { "isappendant->getColValue(bd_invmandoc,isappendant,pk_invmandoc,cinventoryid)" };
      getBillCardPanel().getBillModel().execFormulas(appendFormula);

      ArrayList alist = new ArrayList();
      for (int i = 0; i < getBillCardPanel().getRowCount(); i++)
      {
        nc.ui.so.so001.panel.bom.BillTools.calcViaPrice1(i, getBillCardPanel().getBillModel(), getBillType());

        Object oCsourcebillid = getBillCardPanel().getBillModel().getValueAt(i, "csourcebillid");

        if ((oCsourcebillid != null) && (oCsourcebillid.toString().length() != 0))
        {
          alist.add(new Integer(i));
        }
      }

      String[] bodyFormula = new String[2];
      bodyFormula[0] = "ctinvclassid->getColValue(ct_manage_b,invclid,pk_ct_manage_b,csourcebillbodyid)";
      bodyFormula[1] = "ctinvclass->getColValue(bd_invcl,invclassname,pk_invcl,ctinvclassid)";
      getBillCardTools().execBodyFormulas(bodyFormula, alist);

      getBillCardPanel().setHeadItem("salecorp", getClientEnvironment().getCorporation().getUnitname());

      SCMEnv.out("加载表体[共用时" + (System.currentTimeMillis() - s1) + "]");

      getBillCardPanel().updateValue();

      getBillCardPanel().getBillModel().setNeedCalculate(bisCalculate);
      if (getBillCardPanel().getHeadItem("nheadsummny") != null) {
        getBillCardPanel().getHeadItem("nheadsummny").setValue(getBillCardPanel().getTotalValue("noriginalcursummny"));
      }

      setButtonsState();
      showHintMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000186", null, new String[] { (System.currentTimeMillis() - s) / 1000L + "" }));
    }
    catch (ValidationException e)
    {
      showErrorMessage(e.getMessage());
    } catch (Exception e) {
      showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000256"));
    }
  }

  private void copyIstotal()
  {
    int iRow = getBillCardPanel().getRowCount();
    for (int i = 0; i < iRow; i++) {
      Object largess = getBillCardPanel().getBodyValueAt(i, "blargessflag");

      if ((largess != null) && (new UFBoolean(largess.toString()).booleanValue()))
      {
        getBillCardPanel().setBodyValueAt("1", i, "is_total");
      }
    }
  }

  public void loadCardData(SaleOrderVO billvo)
  {
    if (billvo == null)
      return;
    try
    {
      long s = System.currentTimeMillis();

      getBillCardPanel().setPanelByCurrency(((SaleorderBVO)billvo.getChildrenVO()[0]).getCcurrencytypeid());

      UIRefPane vreceiveaddress = (UIRefPane)getBillCardPanel().getHeadItem("vreceiveaddress").getComponent();

      vreceiveaddress.setAutoCheck(false);

      getBillCardPanel().setBillValueVO(billvo);

      getBillCardPanel().getBillModel().execLoadFormula();

      copyIstotal();

      long s1 = System.currentTimeMillis();

      SCMEnv.out("执行公式[共用时" + (System.currentTimeMillis() - s1) + "]");
      if (getBillCardPanel().getHeadItem("creceiptcustomerid") != null) {
        ((CustAddrRefModel)vreceiveaddress.getRefModel()).setCustId((String)getBillCardPanel().getHeadItem("creceiptcustomerid").getValueObject());
      }

      Object temp = billvo.getParentVO().getAttributeValue("vreceiveaddress");

      String formula = "getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,creceiptcustomerid)";

      getBillCardPanel().execHeadFormula(formula);

      vreceiveaddress.getUITextField().setText(temp == null ? "" : temp.toString());

      getBillCardPanel().initFreeItem();

      setHeadDefaultData();

      getBillCardPanel().initUnit();

      SCMEnv.out("加载表头数据bo[共用时" + (System.currentTimeMillis() - s) + "]");
      s = System.currentTimeMillis();

      String[] formulas = { "wholemanaflag->getColValue(bd_invmandoc,wholemanaflag,pk_invmandoc,cinventoryid)", "isconfigable->getColValue(bd_invmandoc,isconfigable,pk_invmandoc,cinventoryid)", "isspecialty->getColValue(bd_invmandoc,isspecialty,pk_invmandoc,cinventoryid)", "isappendant->getColValue(bd_invmandoc,isappendant,pk_invmandoc,cinventoryid)" };

      getBillCardPanel().getBillModel().execFormulas(formulas);

      ArrayList alist = new ArrayList();
      for (int i = 0; i < getBillCardPanel().getRowCount(); i++)
      {
        nc.ui.so.so001.panel.bom.BillTools.calcViaPrice1(i, getBillCardPanel().getBillModel(), getBillCardPanel().getBillType());

        Object oCsourcebillid = getBillCardPanel().getBillModel().getValueAt(i, "csourcebillid");

        if ((oCsourcebillid != null) && (oCsourcebillid.toString().length() != 0))
        {
          alist.add(new Integer(i));
        }

      }

      String[] bodyFormula = new String[2];
      bodyFormula[0] = "ctinvclassid->getColValue(ct_manage_b,invclid,pk_ct_manage_b,csourcebillbodyid)";
      bodyFormula[1] = "ctinvclass->getColValue(bd_invcl,invclassname,pk_invcl,ctinvclassid)";
      getBillCardTools().execBodyFormulas(bodyFormula, alist);

      getBillCardTools().setBodyInventory1(0, getBillCardPanel().getRowCount());

      UFDouble ndiscountrate = getBillCardTools().getHeadUFDoubleValue("ndiscountrate");

      if (ndiscountrate == null) {
        ndiscountrate = getBillCardTools().getBodyUFDoubleValue(0, "ndiscountrate");
      }

      if (ndiscountrate == null) {
        ndiscountrate = new UFDouble(100);
      }
      getBillCardPanel().setHeadItem("ndiscountrate", ndiscountrate);

      getBillCardPanel().setHeadItem("salecorp", getClientEnvironment().getCorporation().getUnitname());

      getBillCardPanel().updateValue();
      getBillCardPanel().getBillModel().reCalcurateAll();
      if (getBillCardPanel().getHeadItem("nheadsummny") != null) {
        getBillCardPanel().getHeadItem("nheadsummny").setValue(getBillCardPanel().getTotalValue("noriginalcursummny"));
      }

      SCMEnv.out("加载表体数据bo[共用时" + (System.currentTimeMillis() - s) + "]");
    }
    catch (Exception e) {
      showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000256"));
    }
  }

  private void loadIDafterADD(ArrayList listID)
  {
    try
    {
      removeOOSLine();

      if (listID == null)
        return;
      getBillCardPanel().setHeadItem("csaleid", (String)listID.get(0));
      getBillCardPanel().setHeadItem("vreceiptcode", (String)listID.get(1));

      Object ots = listID.get(listID.size() - 1);

      for (int i = 2; i < listID.size() - 1; i++) {
        getBillCardPanel().setBodyValueAt((String)listID.get(0), i - 2, "csaleid");

        getBillCardPanel().setBodyValueAt((String)listID.get(i), i - 2, "corder_bid");

        getBillCardPanel().setBodyValueAt(ots, i - 2, "ts");
        getBillCardPanel().setBodyValueAt(ots, i - 2, "exets");
      }

      getBillCardPanel().setHeadItem("ts", ots);
    }
    catch (Exception e) {
      showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000256"));
    }
  }

  private void loadIDafterEDIT(ArrayList listID)
  {
    try
    {
      removeOOSLine();

      String[] formula = { "vreceiptcode->getColValue(so_sale,vreceiptcode,csaleid,csaleid)" };
      getBillCardPanel().execHeadFormulas(formula);

      if (listID == null)
        return;
      int i = 1;
      Object ots = listID.get(listID.size() - 2);
      for (int j = 0; j < getBillCardPanel().getRowCount(); j++) {
        if (getBillCardPanel().getBillModel().getRowState(j) == 1)
        {
          getBillCardPanel().setBodyValueAt(getBillCardPanel().getHeadItem("csaleid").getValueObject(), j, "csaleid");

          getBillCardPanel().setBodyValueAt((String)listID.get(i), j, "corder_bid");

          getBillCardPanel().setBodyValueAt(ots, j, "ts");
          getBillCardPanel().setBodyValueAt(ots, j, "exets");

          i++;
        }
        if (getBillCardPanel().getBillModel().getRowState(j) == 2) {
          getBillCardPanel().setBodyValueAt(ots, j, "ts");
          getBillCardPanel().setBodyValueAt(ots, j, "exets");
        }
      }
      getBillCardPanel().setHeadItem("ts", ots);
    } catch (Exception e) {
      showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000256"));
    }
  }

  private void markRow(String message)
  {
    Vector vecTemp = new Vector();
    int pos0 = 0;
    if (message != null) {
      while (true) {
        int start = message.indexOf("<-", pos0);
        if (start < 0)
          break;
        int end = message.indexOf("->", start);
        String temp = message.substring(start + 2, end);
        vecTemp.add(temp);
        SCMEnv.out(temp);
        pos0 = end;
      }
    }
    this.vRowATPStatus = new Vector();
    for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
      this.vRowATPStatus.addElement(new UFBoolean(false));
    }

    if ((vecTemp != null) && (vecTemp.size() != 0)) {
      for (int i = 0; i < vecTemp.size(); i++) {
        int iRow = new Integer(vecTemp.elementAt(i).toString()).intValue();

        this.vRowATPStatus.setElementAt(new UFBoolean(true), iRow - 1);
      }
    }
    updateUI();
  }

  protected void onAction(ButtonObject bo)
  {
    if ((this.strShowState.equals("列表")) && (getBillListPanel().getHeadTable().getSelectedRowCount() > 1))
    {
      if (bo.getTag().equals("APPROVE"))
        onApprove(bo);
      else if (bo.getTag().equals("SoUnApprove"))
        onUnApprove(bo);
      else if (bo.getTag().equals("SoBlankout")) {
        onBlankout(bo);
      }

      return;
    }

    SaleOrderVO saleorder = (SaleOrderVO)getVO(false);

    Integer fstatus = saleorder.getHeadVO().getFstatus();
    if ((fstatus != null) && (fstatus.intValue() == 5)) {
      showHintMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000500"));

      return;
    }

    long s1 = 0L;
    try {
      long s = System.currentTimeMillis();

      if (saleorder != null) {
        saleorder.validate();
      }
      SaleorderBVO[] ordbvo = saleorder.getBodyVOs();

      if (bo.getTag().equals("SoBlankout")) {
        saleorder.setIAction(2);
        int i = 0; for (int loop = ordbvo.length; i < loop; i++)
        {
          ordbvo[i].setIAction(12);
          ordbvo[i].setStatus(3);
        }
      }
      else if (bo.getTag().equals("SoUnApprove")) {
        saleorder.setIAction(4);
        int i = 0; for (int loop = ordbvo.length; i < loop; i++)
        {
          ordbvo[i].setIAction(4);
        }
      } else if (bo.getTag().equals("APPROVE")) {
        saleorder.setIAction(3);
        int i = 0; for (int loop = ordbvo.length; i < loop; i++)
        {
          ordbvo[i].setIAction(3);
        }
      } else if (bo.getTag().equals("OrderFinish")) {
        saleorder.setIAction(6);
        int i = 0; for (int loop = ordbvo.length; i < loop; i++)
        {
          ordbvo[i].setIAction(12);
        }
      } else if (bo.getTag().equals("OrderFreeze")) {
        saleorder.setIAction(7);
        int i = 0; for (int loop = ordbvo.length; i < loop; i++)
        {
          ordbvo[i].setIAction(-1);
        }
      } else if (bo.getTag().equals("OrderUnFreeze")) {
        saleorder.setIAction(8);
        int i = 0; for (int loop = ordbvo.length; i < loop; i++)
        {
          ordbvo[i].setIAction(-1);
        }
      }

      if (bo.getTag().equals("APPROVE"))
      {
        CreditUtil.isInvoiceFirstNewFrom501(getCorpPrimaryKey(), saleorder.getBizTypeid());

        onApproveCheck(saleorder);
        onApproveCheckWorkflow(saleorder);
       
        SCMEnv.out("审核检测通过");
      }
      if (bo.getTag().equals("SoUnApprove")) {
        onUnApproveCheck(saleorder);
      }

      s1 = System.currentTimeMillis();
      SCMEnv.out("用时：" + (s1 - s) + "ms");

      ClientLink clientLink = new ClientLink(getClientEnvironment());

      saleorder.setClientLink(clientLink);
      saleorder.setCuruserid(getClientEnvironment().getUser().getPrimaryKey());

      saleorder.setDcurdate(getClientEnvironment().getDate());
      saleorder.setCusername(getClientEnvironment().getUser().getUserName());

      saleorder.setCcorpname(getClientEnvironment().getCorporation().getUnitname());

      saleorder.setCnodename(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000504"));

      if (bo.getTag().equals("APPROVE"))
      {
        getBillCardTools().getPkarrivecorp(saleorder);
      
        saleorder.processVOForTrans();
        //价税合计
        Object nheadsummny = getBillCardPanel().getHeadItem("nheadsummny").getValueObject();
        //收款协议
        Object ctermprotocolid = getBillCardPanel().getHeadItem("ctermprotocolid").getValueObject();
        
        //公司
        Object corp = getBillCardPanel().getHeadItem("pk_corp").getValueObject();
        //获取公司是否是国内的结果
        IdetermineService idetermineService =(IdetermineService) NCLocator.getInstance().lookup(IdetermineService.class.getName());
        Boolean resul = idetermineService.check(String.valueOf(corp));
        //查询协议比率
        String receivingSql = "select jhyfk from bd_payterm where pk_payterm='"+ctermprotocolid+"'";//利用协议号查询出预付款比率和收到发票后付款天数
        IUAPQueryBS  uap = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
		List list = (List<Object>) uap.executeQuery(receivingSql.toString(), new MapListProcessor());
		Map map = (Map) list.get(0);
		//取协议中的比率
		String value = map.get("jhyfk").toString();
		Double values = Double.valueOf(value);
		//协议*0.01；
		double result = values* 0.01;
		//合计金额*比率 是需要应收的钱
		double nheadsummnys =Double.valueOf(String.valueOf(nheadsummny)) ;
		//得出需要应收的金额
		double results =nheadsummnys * result;
		//获取销售主表id
		Object csaleid  = getBillCardPanel().getHeadItem("csaleid").getValueObject();
		
		//查询收款金额
		 String receiving = "select sum(dfbbje) as dfbbje from arap_djfb  where  zyx20='"+csaleid+"'";
	     IUAPQueryBS  uaps = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
	     List lists = (List<Object>) uaps.executeQuery(receivingSql.toString(), new MapListProcessor());
	     if(resul==true){
		    	if(null==lists||lists.size()==0){
		    		showWarningMessage("收款金额不存在");
		    		return;
		    	}
	     Map maps = (Map) lists.get(0);
	     String value1 = maps.get("dfbbsj").toString();
	     double receive = Double.valueOf(value1);
	    
	    //判断应付金额是否小于应收单金额
        if(receive<results){
        	showWarningMessage("应付金额小于应收单金额");
    		return;
        	}
	    }
        Object otemp = null;
        boolean bContinue = true;
        while (bContinue)
        {
          try
          {
            ObjectUtils.objectReference(saleorder);

            otemp = PfUtilClient.processActionFlow(this, bo.getTag(), getBillType(), getClientEnvironment().getDate().toString(), saleorder, null);

            bContinue = false;
          } catch (Exception ex) {
            bContinue = doException(saleorder, null, ex);
          }

        }

        if (otemp != null) {
          String ErrMsg = otemp.toString();
          if ((ErrMsg != null) && (ErrMsg.startsWith("ERR")))
            showWarningMessage(ErrMsg.substring(3));
        }
      }
      else if (bo.getTag().equals("SoUnApprove"))
      {
        saleorder.setIsDel7D(false);
        saleorder.setCuruserid(getClientEnvironment().getUser().getPrimaryKey());

        saleorder.processVOForTrans();
        boolean bContinue = true;
        while (bContinue)
        {
          try
          {
            PfUtilClient.processActionFlow(this, bo.getTag() + getClientEnvironment().getUser().getPrimaryKey(), "30", getClientEnvironment().getDate().toString(), saleorder, null);

            bContinue = false;
          } catch (Exception ex) {
            bContinue = doException(saleorder, null, ex);
          }
        }

      }
      else
      {
        if (bo.getTag().equals("SoBlankout")) {
          saleorder.getParentVO().setStatus(0);
          ((SaleorderHVO)saleorder.getParentVO()).setVoldreceiptcode(((SaleorderHVO)saleorder.getParentVO()).getVreceiptcode());
        }

        saleorder.setIsDel7D(false);
        saleorder.setCuruserid(getClientEnvironment().getUser().getPrimaryKey());

        saleorder.processVOForTrans();

        boolean bContinue = true;
        while (bContinue) {
          try
          {
            PfUtilClient.processActionNoSendMessage(this, bo.getTag(), "30", getClientEnvironment().getDate().toString(), saleorder, null, null, null);

            bContinue = false;
          } catch (Exception ex) {
            bContinue = doException(saleorder, null, ex);
          }
        }

      }

      if (PfUtilClient.isSuccess())
      {
        nc.ui.so.so001.panel.bom.BillTools.reLoadBillState(this, getClientEnvironment());
        showCustManArInfo();

        showHintMessage(bo.getName() + NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000132"));

        if ((this.strShowState.equals("列表")) && 
          (bo.getTag().equals("OrderFinish")))
        {
          UFDouble dtempmny = SaleOrderBO_Client.queryCachPayByOrdId(saleorder.getHeadVO().getCsaleid());

          getBillListPanel().getHeadBillModel().setValueAt(dtempmny, getBillListPanel().getHeadTable().getSelectedRow(), "nreceiptcathmny");
        }

        if (bo.getTag().equals("SoBlankout")) {
          int[] irows = { getBillListPanel().getHeadTable().getSelectedRow() };

          if (this.strShowState.equals("列表")) {
            getBillListPanel().getHeadBillModel().delLine(irows);
            getBillListPanel().getHeadBillModel().updateValue();

            getBillListPanel().getBodyBillModel().clearBodyData();
            getBillListPanel().getBodyBillModel().updateValue();
          } else {
            getBillCardPanel().addNew();
            getBillCardPanel().setHeadItem("vreceiptcode", null);
            getBillCardPanel().getBillModel().clearBodyData();
            getBillCardPanel().updateValue();

            if (irows[0] > -1) {
              getBillListPanel().getHeadBillModel().delLine(irows);

              getBillListPanel().getHeadTable().clearSelection();
              getBillListPanel().getHeadBillModel().updateValue();

              getBillListPanel().getBodyBillModel().clearBodyData();

              getBillListPanel().getBodyBillModel().updateValue();
            }
          }

          this.vocache.deleteByID(saleorder.getHeadVO().getCsaleid());
        }
        else {
          updateCacheVO();
        }

        SCMEnv.out("用时：" + (System.currentTimeMillis() - s1) + "ms");

        if (!this.strShowState.equals("列表"))
          getBillCardPanel().updateValue();
      }
      else
      {
        showHintMessage(bo.getName() + NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000339"));
      }

    }
    catch (BusinessException e)
    {
      showWarningMessage(e.getMessage() + bo.getName() + NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000134"));
    }
    catch (Exception e)
    {
      showWarningMessage(e.getMessage() + " " + bo.getName() + NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000134"));

      SCMEnv.out(e.getMessage());
    }
  }

  protected void onApprove(ButtonObject bo)
  {
    try {
      CreditUtil.isInvoiceFirstNewFrom501(getCorpPrimaryKey(), getBillCardPanel().getBusiType());
    }
    catch (BusinessException e) {
      showErrorMessage(e.getMessage());
      showHintMessage(bo.getName() + NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000134"));

      return;
    }

    this.proccdlg = new ProccDlg(this, bo.getName());
    this.work = new WorkThread(bo.getTag(), this);

    this.proccdlg.showModal();
    updateUI();
  }

  public int onApprove(SaleOrderVO vo, String tag)
    throws Exception
  {
    if (vo == null) {
      return 0;
    }
    SaleOrderVO saleorder = vo;

    if (vo.getChildrenVO() != null) {
      for (int i = 0; i < vo.getChildrenVO().length; i++) {
        vo.getChildrenVO()[i].setStatus(0);
      }
    }
    saleorder.setIAction(3);

    boolean bContinue = true;

    while (bContinue)
    {
      try
      {
        onApproveCheck(saleorder);

        onApproveCheckWorkflow(saleorder);

        ClientLink clientLink = new ClientLink(getClientEnvironment());

        saleorder.setClientLink(clientLink);

        getBillCardTools().getPkarrivecorp(saleorder);

        saleorder.setCuruserid(getClientEnvironment().getUser().getPrimaryKey());

        saleorder.setDcurdate(getClientEnvironment().getDate());
        saleorder.setCusername(getClientEnvironment().getUser().getUserName());

        saleorder.setCcorpname(getClientEnvironment().getCorporation().getUnitname());

        saleorder.setCnodename(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000504"));

        vo.getHeadVO().setAttributeValue("daudittime", ClientEnvironment.getServerTime());

        Object otemp = PfUtilClient.processActionFlow(this, tag, getBillType(), getClientEnvironment().getDate().toString(), saleorder, null);

        if (otemp != null) {
          String ErrMsg = otemp.toString();
          if ((ErrMsg != null) && (ErrMsg.startsWith("ERR"))) {
            ShowToolsInThread.showMessage(this.proccdlg, ErrMsg.substring(3));
          }

        }

        if (PfUtilClient.isSuccess()) {
          return 1;
        }
        return 0;
      }
      catch (Exception ex)
      {
        bContinue = doException(saleorder, null, ex);
      }

    }

    return 0;
  }

  protected void onUnApprove(ButtonObject bo)
  {
    this.proccdlg = new ProccDlg(this, bo.getName());
    this.work = new WorkThread(bo.getTag(), this);

    this.proccdlg.showModal();
    updateUI();
  }

  public int onUnApprove(SaleOrderVO vo, String tag)
    throws Exception
  {
    if (vo == null) {
      return 0;
    }
    SaleOrderVO saleorder = vo;
    if (vo.getChildrenVO() != null) {
      for (int i = 0; i < vo.getChildrenVO().length; i++) {
        vo.getChildrenVO()[i].setStatus(0);
      }
    }

    saleorder.setIAction(4);
    try
    {
      onUnApproveCheck(saleorder);

      ClientLink clientLink = new ClientLink(getClientEnvironment());
      saleorder.setClientLink(clientLink);
      saleorder.setCuruserid(getClientEnvironment().getUser().getPrimaryKey());

      saleorder.setDcurdate(getClientEnvironment().getDate());
      saleorder.setCusername(getClientEnvironment().getUser().getUserName());

      saleorder.setCcorpname(getClientEnvironment().getCorporation().getUnitname());

      saleorder.setCnodename(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000504"));

      PfUtilClient.processAction(tag, "30", getClientEnvironment().getDate().toString(), saleorder);

      if (PfUtilClient.isSuccess()) {
        return 1;
      }
      return 0;
    }
    catch (Exception e) {
      throw e;
    }
  }

  protected void onBlankout(ButtonObject bo)
  {
    this.proccdlg = new ProccDlg(this, bo.getName());
    this.work = new WorkThread(bo.getTag(), this);

    this.proccdlg.showModal();
    updateUI();
  }

  public int onBlankout(SaleOrderVO vo, String tag) throws Exception
  {
    if (vo == null) {
      return 0;
    }
    SaleOrderVO saleorder = vo;
    if (vo.getChildrenVO() != null) {
      for (int i = 0; i < vo.getChildrenVO().length; i++) {
        vo.getChildrenVO()[i].setStatus(0);
      }
    }

    saleorder.setIAction(4);
    try
    {
      ((SaleorderHVO)saleorder.getParentVO()).setVoldreceiptcode(((SaleorderHVO)saleorder.getParentVO()).getVreceiptcode());

      saleorder.setIsDel7D(false);
      saleorder.setCuruserid(getClientEnvironment().getUser().getPrimaryKey());

      saleorder.processVOForTrans();

      PfUtilClient.processActionNoSendMessage(this, tag, "30", getClientEnvironment().getDate().toString(), saleorder, null, null, null);

      if (PfUtilClient.isSuccess()) {
        return 1;
      }
      return 0;
    }
    catch (Exception e) {
      throw e;
    }
  }

  private void onUnApproveCheck(SaleOrderVO saleorder)
    throws ValidationException
  {
    SaleorderBVO[] oldbodyVOs = saleorder.getBodyVOs();

    for (int i = 0; i < oldbodyVOs.length; i++)
    {
      if ((oldbodyVOs[i].getNtotalinventorynumber() != null) && (oldbodyVOs[i].getNtotalinventorynumber().doubleValue() != 0.0D))
      {
        throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000294"));
      }

      if ((oldbodyVOs[i].getBifinventoryfinish() != null) && (oldbodyVOs[i].getBifinventoryfinish().booleanValue()))
      {
        throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000418"));
      }

      if ((oldbodyVOs[i].getBifinvoicefinish() != null) && (oldbodyVOs[i].getBifinvoicefinish().booleanValue()))
      {
        throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000419"));
      }

      if ((oldbodyVOs[i].getNarrangescornum() != null) && (oldbodyVOs[i].getNarrangescornum().doubleValue() != 0.0D))
      {
        throw new ValidationException(NCLangRes.getInstance().getStrByID("sopub", "UPPsopub-000303"));
      }

      if ((oldbodyVOs[i].getNarrangemonum() != null) && (oldbodyVOs[i].getNarrangemonum().doubleValue() != 0.0D))
      {
        throw new ValidationException(NCLangRes.getInstance().getStrByID("sopub", "UPPsopub-000304"));
      }

      if (((oldbodyVOs[i].getNarrangepoapplynum() != null) && (oldbodyVOs[i].getNarrangepoapplynum().doubleValue() != 0.0D)) || ((oldbodyVOs[i].getNarrangetoornum() != null) && (oldbodyVOs[i].getNarrangetoornum().doubleValue() != 0.0D)))
      {
        throw new ValidationException(NCLangRes.getInstance().getStrByID("sopub", "UPPsopub-000036"));
      }

    }

    this.boCancelAudit.setEnabled(false);
    updateButton(this.boCancelAudit);
    SCMEnv.out("弃审检测通过1");
  }

  protected boolean doException(SaleOrderVO vo, SaleOrderVO oldvo, Exception e)
    throws Exception
  {
    e = nc.vo.so.so001.BillTools.marshException(e);

    if (((e instanceof ATPNotEnoughException)) && (((ATPNotEnoughException)e).getHint() == null))
    {
      throw e;
    }

    if ((!(e instanceof ATPNotEnoughException)) && (!(e instanceof CreditNotEnoughException)) && (!(e instanceof PeriodNotEnoughException)) && (!(e instanceof Deal7DException)) && (!(e instanceof SaveHintException)) && (!(e instanceof DelDlvPlanException)))
    {
      throw e;
    }
    String sMsg = e.getMessage();

    if (!(e instanceof DelDlvPlanException)) {
      sMsg = sMsg + NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000230");
    }

    if (showYesNoMessage(sMsg) != 4) {
      return false;
    }
    if ((e instanceof ATPNotEnoughException)) {
      vo.setBCheckATP(false);
      if (oldvo != null)
        oldvo.setBCheckATP(false);
    } else if ((e instanceof CreditNotEnoughException)) {
      vo.setCheckCredit(false);
      if (oldvo != null)
        oldvo.setCheckCredit(false);
    } else if ((e instanceof PeriodNotEnoughException)) {
      vo.setCheckPeriod(false);
      if (oldvo != null)
        oldvo.setCheckPeriod(false);
    } else if ((e instanceof Deal7DException)) {
      vo.setIsDel7D(true);
      if (oldvo != null)
        oldvo.setIsDel7D(true);
    } else if ((e instanceof SaveHintException)) {
      vo.setFirstTime(false);
      if (oldvo != null)
        oldvo.setFirstTime(false);
    } else if (!(e instanceof DelDlvPlanException));
    return true;
  }

  protected void onAfterAction(ButtonObject bo)
  {
    try
    {
      PfUtilClient.processAction(bo.getTag(), getBillType(), getClientEnvironment().getDate().toString(), getVO(false));

      showHintMessage(bo.getName() + NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000132"));
    }
    catch (Exception e)
    {
      showWarningMessage(bo.getName() + NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000134"));
    }
  }

  private void onApproveCheckWorkflow(SaleOrderVO saleorder)
    throws ValidationException
  {
    try
    {
      boolean isExist = false;
      isExist = PfUtilBO_Client.isExistWorkFlow(getBillType(), saleorder.getBizTypeid(), getClientEnvironment().getCorporation().getPk_corp(), getClientEnvironment().getUser().getPrimaryKey());

      String pkOperator = ((SaleorderHVO)saleorder.getParentVO()).getCoperatorid().trim();

      if ((isExist == true) && (pkOperator.equals(getClientEnvironment().getUser().getPrimaryKey().trim())))
      {
        int iWorkflowstate = 0;
        iWorkflowstate = PfUtilClient.queryWorkFlowStatus(saleorder.getBizTypeid(), getBillType(), saleorder.getParentVO().getPrimaryKey());

        if (iWorkflowstate == 5) {
          throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000188"));
        }

      }

    }
    catch (Throwable e)
    {
      SCMEnv.out(e);
      e.printStackTrace();
      throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000188"));
    }
  }

  protected void onAssistant(ButtonObject bo)
  {
    if (this.strShowState.equals("列表")) {
      this.num = getBillListPanel().getHeadTable().getSelectedRow();

      if (this.num == -1)
        return;
    }
    try
    {
      PfUtilClient.processActionNoSendMessage(this, bo.getTag(), getBillType(), getClientEnvironment().getDate().toString(), getVO(false), getAssistantPara(bo), null, null);
    }
    catch (Exception e)
    {
      showErrorMessage(e.getMessage());
    }
  }

  protected void onCopyBill()
  {
    PfUtilClient.retAddBtn(this.boAdd, getCorpPrimaryKey(), getBillType(), this.boBusiType);

    int ccount = this.boAdd.getChildCount();
    ButtonObject[] bos = this.boAdd.getChildButtonGroup();
    boolean bCanCopyFlag = false;
    for (int i = 0; i < ccount; i++) {
      if (bos[i].getTag().startsWith("makeflag")) {
        bCanCopyFlag = true;
        break;
      }
    }

    if (!bCanCopyFlag) {
      showWarningMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000289"));

      return;
    }
    if (this.strShowState.equals("列表")) {
      this.selectRow = getBillListPanel().getHeadTable().getSelectedRow();
      this.num = this.selectRow;
      getBillListPanel().getHeadTable().getSelectionModel().setSelectionInterval(this.selectRow, this.selectRow);

      onCard();
    }
    if ((getBillCardPanel().getHeadItem("bretinvflag") != null) && (getBillCardPanel().getHeadItem("bretinvflag").getValueObject() != null) && (new UFBoolean(getBillCardPanel().getHeadItem("bretinvflag").getValue()).booleanValue()))
    {
      showWarningMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000290"));

      return;
    }

    String[] headitems = { "bretinvflag", "binvoicendflag", "boutendflag", "breceiptendflag", "bpayendflag", "btransendflag", "boverdate", "capproveid", "dapprovedate", "vreceiptcode", "csaleid", "veditreason", "nreceiptcathmny", "boverdate", "iprintcount", "dbilltime", "daudittime", "dmoditime" };

    for (int i = 0; i < headitems.length; i++) {
      if (getBillCardPanel().getHeadItem(headitems[i]) != null)
        getBillCardPanel().getHeadItem(headitems[i]).setValue(null);
    }
    String[] tails = { "capproveid", "dapprovedate", "iprintcount", "dbilltime", "daudittime", "dmoditime" };

    for (int i = 0; i < tails.length; i++) {
      if (getBillCardPanel().getTailItem(tails[i]) != null) {
        getBillCardPanel().getTailItem(tails[i]).setValue(null);
      }
    }
    if (getBillCardPanel().getHeadItem("bcodechanged") != null) {
      getBillCardPanel().getHeadItem("bcodechanged").setValue(new UFBoolean(false));
    }
    if (getBillCardPanel().getHeadItem("fstatus") != null) {
      getBillCardPanel().getHeadItem("fstatus").setValue(new Integer(1));
    }
    if (getBillCardPanel().getHeadItem("dbilldate") != null) {
      getBillCardPanel().getHeadItem("dbilldate").setValue(getClientEnvironment().getDate());
    }

    if (getBillCardPanel().getHeadItem("pk_corp") != null) {
      getBillCardPanel().getHeadItem("pk_corp").setValue(getClientEnvironment().getCorporation().getPk_corp());
    }

    String[] keys = getNeedSetNullItemsWhenCopy();

    String[] keys1 = getBillCardPanel().getNeedSetNullSourceItems();

    this.strState = "新增";
    getBillCardTools().setHeadRefLimit(this.strState);

    getBillCardPanel().setEnabled(true);

    this.vRowATPStatus = new Vector();
    if (this.alInvs == null) {
      initInvList();
    }
    UFBoolean wholemanaflag = null; UFBoolean isDiscount = null; UFBoolean isLabor = null;
    for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
      for (int j = 0; j < keys.length; j++) {
        getBillCardPanel().setBodyValueAt(null, i, keys[j]);
      }
      for (int j = 0; j < keys1.length; j++) {
        getBillCardPanel().setBodyValueAt(null, i, keys1[j]);
      }
      getBillCardPanel().setBodyValueAt(null, i, "csaleid");

      this.vRowATPStatus.addElement(new UFBoolean(false));
      getBillCardPanel().getBillModel().setRowState(i, 1);

      ctlUIOnCconsignCorpChg(i);

      getBillCardPanel().setAssistUnit(i);

      wholemanaflag = getBillCardTools().getBodyUFBooleanValue(i, "wholemanaflag");

      getBillCardPanel().setCellEditable(i, "fbatchstatus", wholemanaflag == null ? false : wholemanaflag.booleanValue());

      getBillCardPanel().setCellEditable(i, "cbatchid", wholemanaflag == null ? false : wholemanaflag.booleanValue());

      isDiscount = getBillCardTools().getBodyUFBooleanValue(i, "discountflag");

      isLabor = getBillCardTools().getBodyUFBooleanValue(i, "laborflag");

      if (((isDiscount != null) && (isDiscount.booleanValue())) || ((isLabor != null) && (isLabor.booleanValue())))
      {
        getBillCardTools().setBodyCellsEdit(new String[] { "cconsigncorp", "creccalbody", "crecwarehouse", "bdericttrans", "boosflag", "bsupplyflag" }, i, false);
      }

    }

    setDefaultData(false);
    try
    {
      this.m_ActionTime = RecordTimeHelper.getTimeStamp();
    } catch (Throwable ex) {
      ex.printStackTrace();
    }

    ctlCurrencyEdit();

    getBillCardTools().setEnableCtransmodeid();

    getBillCardTools().resumeBillItemEditToInit();

    if ((getBillCardPanel().getHeadItem("naccountperiod").getValueObject() != null) && (new UFDouble((String)getBillCardPanel().getHeadItem("naccountperiod").getValueObject()).doubleValue() < 0.0D))
    {
      getBillCardPanel().setHeadItem("naccountperiod", null);
    }
    String[] formulas = { "ccustbasid->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,ccustomerid)", "bfreecustflag->getColValue(bd_cubasdoc,freecustflag,pk_cubasdoc,ccustbasid)" };

    getBillCardPanel().execHeadFormulas(formulas);

    UFBoolean bfreecustflag = getBillCardTools().getHeadUFBooleanValue("bfreecustflag");

    if ((bfreecustflag != null) && (bfreecustflag.booleanValue()))
      getBillCardPanel().getHeadItem("cfreecustid").setEnabled(true);
    else {
      getBillCardPanel().getHeadItem("cfreecustid").setEnabled(false);
    }

    setButtonsState();
    showCustManArInfo();
    try
    {
      InvAttrCellRenderer ficr = new InvAttrCellRenderer();
      ficr.setFreeItemRenderer(getBillCardPanel(), this.alInvs);
    }
    catch (Exception e)
    {
    }
    updateUI();
  }

  protected void onAuditFlowStatus()
  {
    SaleOrderVO hvo = null;
    hvo = (SaleOrderVO)getVO(false);
    if ((hvo == null) || (hvo.getParentVO() == null)) {
      showErrorMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000199"));
    }
    else {
      SaleorderHVO header = (SaleorderHVO)hvo.getParentVO();
      String pk = header.getCsaleid();
      if (pk == null) {
        showErrorMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000067"));
      }
      else {
        FlowStateDlg approvestatedlg = new FlowStateDlg(this, "30", pk);

        approvestatedlg.showModal();
      }
    }
  }

  protected void onBatch()
  {
    long s = System.currentTimeMillis();
    SaleOrderVO saleorder = (SaleOrderVO)getBillCardPanel().getBillValueVO(SaleOrderVO.class.getName(), SaleorderHVO.class.getName(), SaleorderBVO.class.getName());
    try
    {
      onCheck(saleorder);
      showHintMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000278"));

      saleorder.setStatus(2);
      saleorder.getParentVO().setStatus(2);

      saleorder.setIAction(0);

      saleorder.setDcurdate(getClientEnvironment().getDate());
      saleorder.setCuruserid(getClientEnvironment().getUser().getPrimaryKey());

      UIRefPane vreceiveaddress = (UIRefPane)getBillCardPanel().getHeadItem("vreceiveaddress").getComponent();

      saleorder.getHeadVO().setVreceiveaddress(vreceiveaddress.getUITextField().getText());

      for (int i = 0; i < saleorder.getChildrenVO().length; i++) {
        saleorder.getChildrenVO()[i].setStatus(2);
      }

      if (!calculatePreceive(saleorder)) {
        return;
      }
      ArrayList alistID = null;

      boolean bContinue = true;
      while (bContinue) {
        try {
          saleorder.processVOForTrans();
          alistID = (ArrayList)PfUtilClient.processActionNoSendMessage(this, "PreKeep", getBillType(), getClientEnvironment().getDate().toString(), saleorder, null, null, null);

          bContinue = false;
        } catch (Exception ex) {
          bContinue = doException(saleorder, null, ex);
        }
      }

      this.vIDs.add((String)alistID.get(0));
      this.id = ((String)alistID.get(0));
      this.num = (this.vIDs.size() - 1);
      showHintMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000279", null, new String[] { (System.currentTimeMillis() - s) / 1000L + "" }));

      loadCardData();

      this.strState = "自由";

      getBillCardTools().setHeadRefLimit(this.strState);

      setCardButtonsState();

      getBillCardPanel().showCustManArInfo();

      InvAttrCellRenderer ficr = new InvAttrCellRenderer();
      ficr.setFreeItemRenderer(getBillCardPanel(), null);
    }
    catch (ValidationException e) {
      showErrorMessage(e.getMessage() + NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000280"));
    }
    catch (Exception e)
    {
      showWarningMessage(e.getMessage());
      showHintMessage(e.getMessage() + NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000280"));
    }
  }

  private void setCardButtonsState()
  {
    this.boFirst.setEnabled(true);
    this.boLast.setEnabled(true);

    PfUtilClient.retAddBtn(this.boAdd, getCorpPrimaryKey(), getBillType(), this.boBusiType);

    String csaleid = (String)getBillCardPanel().getHeadItem("csaleid").getValueObject();

    if (((csaleid == null) || (csaleid.length() == 0)) && (this.strState.equals("自由")))
    {
      this.boBusiType.setEnabled(true);
      this.boAdd.setEnabled(true);
      this.boSave.setEnabled(false);
      this.boMaintain.setEnabled(false);
      this.boLine.setEnabled(false);
      this.boAudit.setEnabled(false);
      this.boAction.setEnabled(false);
      this.boQuery.setEnabled(true);
      this.boBrowse.setEnabled(false);
      this.boReturn.setEnabled(true);
      this.boPrntMgr.setEnabled(false);
      this.boAssistant.setEnabled(false);
      this.boAsstntQry.setEnabled(false);
    } else if (this.strState.equals("自由")) {
      this.boBusiType.setEnabled(true);
      int ccount = this.boAdd.getChildCount();
      ButtonObject[] bos = this.boAdd.getChildButtonGroup();
      boolean bCanCopyFlag = false;
      for (int i = 0; i < ccount; i++) {
        if (bos[i].getTag().startsWith("makeflag")) {
          bCanCopyFlag = true;
          break;
        }
      }
      this.boAdd.setEnabled(true);
      this.boBatch.setEnabled(false);
      this.boEdit.setEnabled(true);
      this.boCancel.setEnabled(false);
      this.boSave.setEnabled(false);
      this.boMaintain.setEnabled(true);
      this.boBlankOut.setEnabled(true);
      setLineButtonStatus(false);
      this.boAudit.setEnabled(false);
      this.boClose.setEnabled(true);
      this.boFreeze.setEnabled(false);
      this.boDocument.setEnabled(true);
      this.boOrderQuery.setEnabled(true);
      this.boReturn.setEnabled(true);
      this.boAction.setEnabled(true);
      this.boPrntMgr.setEnabled(true);
      this.boAssistant.setEnabled(true);
      this.boAsstntQry.setEnabled(true);
      this.boQuery.setEnabled(true);
      this.boBrowse.setEnabled(true);
      this.boRefundment.setEnabled(false);
      this.boAfterAction.setEnabled(true);
      this.boBom.setEnabled(false);
      this.boPrint.setEnabled(true);
      this.boPreview.setEnabled(true);
      this.boPre.setEnabled(true);
      this.boNext.setEnabled(true);

      this.boSendAudit.setEnabled(true);
      this.boAuditFlowStatus.setEnabled(true);

      if (this.num == 0)
        this.boPre.setEnabled(false);
      if (this.num == this.vIDs.size() - 1) {
        this.boNext.setEnabled(false);
      }
      this.boCopyBill.setEnabled(bCanCopyFlag);

      getBillCardPanel().setEnabled(false);

      int iStatus = ((Integer)getBillCardPanel().getHeadItem("fstatus").getValueObject()).intValue();

      setBtnsByBillState(iStatus);
    }
    else if (this.strState.equals("新增")) {
      this.boBusiType.setEnabled(false);
      this.boAdd.setEnabled(false);
      this.boAudit.setEnabled(false);
      this.boQuery.setEnabled(false);
      this.boPrntMgr.setEnabled(false);
      this.boAssistant.setEnabled(true);
      this.boAsstntQry.setEnabled(true);
      this.boAction.setEnabled(true);
      this.boBrowse.setEnabled(false);
      this.boPrint.setEnabled(false);
      this.boPreview.setEnabled(false);
      this.boEdit.setEnabled(false);
      this.boCancel.setEnabled(true);
      this.boSave.setEnabled(true);
      this.boMaintain.setEnabled(true);
      this.boBlankOut.setEnabled(false);
      setLineButtonStatus(true);
      this.boFirst.setEnabled(false);
      this.boLast.setEnabled(false);
      this.boPre.setEnabled(false);
      this.boNext.setEnabled(false);
      this.boClose.setEnabled(false);
      this.boDocument.setEnabled(false);
      this.boOrderQuery.setEnabled(false);
      this.boReturn.setEnabled(false);
      this.boAfterAction.setEnabled(false);
      this.boRefundment.setEnabled(false);
      this.boBom.setEnabled(false);

      this.boCopyBill.setEnabled(false);

      this.boSendAudit.setEnabled(true);
      this.boAuditFlowStatus.setEnabled(false);

      setLineButtonsState();
    } else if (this.strState.equals("修改")) {
      this.boBusiType.setEnabled(false);
      this.boAdd.setEnabled(false);
      this.boAudit.setEnabled(false);
      this.boQuery.setEnabled(false);
      this.boPrntMgr.setEnabled(false);
      this.boAssistant.setEnabled(true);
      this.boAsstntQry.setEnabled(true);
      this.boAction.setEnabled(true);
      this.boBrowse.setEnabled(false);
      this.boPrint.setEnabled(false);
      this.boPreview.setEnabled(false);
      this.boEdit.setEnabled(false);
      this.boCancel.setEnabled(true);
      this.boSave.setEnabled(true);
      this.boBlankOut.setEnabled(false);
      setLineButtonStatus(true);
      this.boFirst.setEnabled(false);
      this.boLast.setEnabled(false);
      this.boPre.setEnabled(false);
      this.boNext.setEnabled(false);
      this.boClose.setEnabled(false);
      this.boDocument.setEnabled(false);
      this.boOrderQuery.setEnabled(false);
      this.boReturn.setEnabled(false);
      this.boAfterAction.setEnabled(false);
      this.boRefundment.setEnabled(false);
      this.boBom.setEnabled(false);

      this.boCopyBill.setEnabled(false);

      this.boSendAudit.setEnabled(true);
      this.boAuditFlowStatus.setEnabled(true);

      setLineButtonsState();
    } else if (this.strState.equals("修订")) {
      this.boBusiType.setEnabled(false);
      this.boAdd.setEnabled(false);
      this.boAudit.setEnabled(false);
      this.boQuery.setEnabled(false);
      this.boPrntMgr.setEnabled(false);
      this.boAssistant.setEnabled(true);
      this.boAsstntQry.setEnabled(true);
      this.boAction.setEnabled(false);
      this.boBrowse.setEnabled(false);
      this.boPrint.setEnabled(false);
      this.boPreview.setEnabled(false);
      this.boEdit.setEnabled(false);
      this.boCancel.setEnabled(true);
      this.boSave.setEnabled(true);
      this.boBlankOut.setEnabled(false);
      setLineButtonStatus(true);
      this.boFirst.setEnabled(false);
      this.boLast.setEnabled(false);
      this.boPre.setEnabled(false);
      this.boNext.setEnabled(false);
      this.boClose.setEnabled(false);
      this.boDocument.setEnabled(false);
      this.boOrderQuery.setEnabled(false);
      this.boReturn.setEnabled(false);
      this.boAfterAction.setEnabled(false);
      this.boRefundment.setEnabled(false);
      this.boBom.setEnabled(false);

      this.boSendAudit.setEnabled(false);
      this.boAuditFlowStatus.setEnabled(true);

      this.boModification.setEnabled(false);
    }

    if (this.strState.equals("退货")) {
      this.boBatch.setEnabled(true);
      this.boDocument.setEnabled(true);
      this.boOrderQuery.setEnabled(true);
      this.boReturn.setEnabled(true);
      this.boRefundment.setEnabled(false);
      this.boBom.setEnabled(false);
      setLineButtonStatus(true);

      this.boCopyBill.setEnabled(false);

      this.boSendAudit.setEnabled(false);
      this.boAuditFlowStatus.setEnabled(false);
    }

    this.boRefundment.setEnabled(false);

    getBillCardPanel().setBodyMenuShow(this.boLine.isEnabled());
    if (this.strState.equals("退货")) {
      getBillCardPanel().getAddLineMenuItem().setEnabled(false);
      getBillCardPanel().getDelLineMenuItem().setEnabled(this.boDelLine.isEnabled());

      getBillCardPanel().getPasteLineMenuItem().setEnabled(false);
      getBillCardPanel().getCopyLineMenuItem().setEnabled(false);
      getBillCardPanel().getInsertLineMenuItem().setEnabled(false);
    }

    int iStatus = -1;
    if ((getBillCardPanel().getHeadItem("fstatus") != null) && (getBillCardPanel().getHeadItem("fstatus").getValueObject() != null))
    {
      iStatus = ((Integer)getBillCardPanel().getHeadItem("fstatus").getValueObject()).intValue();
    }
    setBtnsByBillState(iStatus);

    if (!this.strState.equals("修订")) {
      getBillCardPanel().hideBodyTableCol("veditreason");
      getBillCardPanel().getBodyItem("veditreason").setEnabled(false);
    }

    this.boRefresh.setEnabled(this.b_query);

    this.boFind.setEnabled(false);
    this.boListSelectAll.setEnabled(false);
    this.boListDeselectAll.setEnabled(false);

    updateButtons();
  }

  protected void onBom() {
    int row = getBillCardPanel().getBillTable().getSelectedRow();
    this.orderrow = row;
    Object nnumber = getBillCardPanel().getBodyValueAt(row, "nnumber");
    if ((nnumber != null) && (nnumber.toString().length() != 0)) {
      remove(getSplitPanelBc());
      add(getBillTreeCardPanel(), "Center");
      String saleID = (String)getBillCardPanel().getHeadItem("csaleid").getValueObject();

      String custID = (String)getBillCardPanel().getHeadItem("ccustomerid").getValueObject();

      String currID = (String)getBillCardPanel().getHeadItem("ccurrencytypeid").getValueObject();

      String invID = getBillCardPanel().getBodyValueAt(row, "cinventoryid").toString();

      String invbaseID = getBillCardPanel().getBodyValueAt(row, "cinvbasdocid").toString();

      String invname = getBillCardPanel().getBodyValueAt(row, "cinventoryname").toString();

      Object tempo = getBillCardPanel().getBodyValueAt(row, "isspecialty");

      String isspecialty = tempo.toString().equals("true") ? "Y" : tempo == null ? "N" : "N";

      Object price = null;
      if (this.SA_02.booleanValue()) {
        price = getBillCardPanel().getBodyValueAt(row, "noriginalcurtaxprice");
      }
      else {
        price = getBillCardPanel().getBodyValueAt(row, "noriginalcurprice");
      }

      String nprice = price == null ? null : price.toString();

      setPanelBomByCurrency(currID);

      if ((saleID != null) && (saleID.length() != 0)) {
        getBillTreeCardPanel().setHeadItem("csaleid", saleID);
      }

      Object bomCurrID = getBillCardPanel().getBodyValueAt(row, "cbomorderid");

      if ((bomCurrID == null) && (!this.strState.equals("新增")) && (!this.strState.equals("修改")))
      {
        this.strBomState = "FREE";
        getBillTreeCardPanel().setEnabled(false);
      } else if (bomCurrID == null) {
        getBillTreeCardPanel().addNew();

        getBillTreeCardPanel().setHeadItem("ccustomerid", custID);

        getBillTreeCardPanel().setHeadItem("ccurrencytypeid", currID);

        getBillTreeCardPanel().setHeadItem("cinventoryid", invID);

        getBillTreeCardPanel().setHeadItem("nrequirenumber", nnumber.toString());

        getBillTreeCardPanel().setHeadItem("nsaleprice", nprice);

        getBillTreeCardPanel().setTailItem("dmakedate", getClientEnvironment().getDate().toString());

        getBillTreeCardPanel().setTailItem("coperatorid", getClientEnvironment().getUser().getPrimaryKey());

        this.strBomState = "ADD";
        getBillTreeCardPanel().setEnabled(true);
      } else {
        this.strBomState = "FREE";
        loadBomData(row);
        getBillTreeCardPanel().setHeadItem("ccurrencytypeid", currID);
      }
      initBomTree(invbaseID, invname, invID, isspecialty);
      getBillTreeCardPanel().getBillModel().execLoadFormula();
      getBillTreeCardPanel().getBodyItem("nprice").setEdit(this.SA_15.booleanValue());

      getBillTreeCardPanel().getCustTree().setEnabled(true);
      getBillTreeCardPanel().getCustTree().setEditable(false);
      this.isSaveStart = true;
      this.strOldState = this.strState;
      this.strState = "BOM";

      initButtons();
      setButtonsState();
      setTitleText(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000190"));

      this.boBomEdit.setEnabled(false);
      updateButtons();
      updateUI();
    } else {
      showWarningMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000191"));
    }
  }

  protected void onBomAction(ButtonObject bo)
  {
    try
    {
      PfUtilClient.processAction(bo.getTag(), "3M", getClientEnvironment().getDate().toString(), getBomVO());

      if (PfUtilClient.isSuccess()) {
        loadBomCurrData();

        showHintMessage(bo.getName() + NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000132"));
      }
      else
      {
        showHintMessage(bo.getName() + NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000339"));
      }

    }
    catch (BusinessException e)
    {
      showErrorMessage(e.getMessage());
      showHintMessage(bo.getName() + NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000134"));
    }
    catch (Exception e)
    {
      showErrorMessage(e.getMessage());
      showHintMessage(bo.getName() + NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000134"));
    }
  }

  protected void onBomCancel()
  {
    if (this.strBomState.equals("ADD")) {
      getBillTreeCardPanel().getCustTree().setEnabled(true);

      this.strBomState = "CANCEL";
    }

    if (this.strBomState.equals("EDIT")) {
      getBillTreeCardPanel().resumeValue();
      getBillTreeCardPanel().getCustTree().setEnabled(true);

      this.strBomState = "FREE";
    }

    getBillTreeCardPanel().getCustTree().setEditable(false);
    setBomButtonsState();
  }

  protected void onBomEdit() {
    this.strBomState = "EDIT";
    getBillTreeCardPanel().setEnabled(true);
    setBomButtonsState();
    getBillTreeCardPanel().getCustTree().setEnabled(false);
    getBillTreeCardPanel().getCustTree().setEditable(false);
    setPzdRowSelected();
  }

  protected void onBomPrint()
  {
  }

  protected void onBomReturn()
  {
    try
    {
      String idBom = (String)getBillTreeCardPanel().getHeadItem("cbomorderid").getValueObject();

      if (idBom != null) {
        UFDouble dPrice = null;
        String idRoot = (String)getBillTreeCardPanel().getHeadItem("cinventoryid").getValueObject();

        if (this.SO_14.booleanValue()) {
          if ((idRoot != null) && (idRoot.length() != 0))
            dPrice = BomorderBO_Client.getBomPriceUnify(getCorpPrimaryKey(), idBom, idRoot);
        }
        else
          dPrice = BomorderBO_Client.getBomPrice(idBom);
        String sTmp = (String)getBillTreeCardPanel().getHeadItem("bomorderfee").getValueObject();

        UFDouble dBomorderfee = sTmp == null ? new UFDouble(0) : new UFDouble(sTmp.trim());

        dPrice = (dPrice == null ? new UFDouble(0) : dPrice).add(dBomorderfee);

        if ((dPrice != null) && (dPrice.doubleValue() != 0.0D)) {
          if (this.SA_02.booleanValue()) {
            getBillCardPanel().setBodyValueAt(dPrice, this.orderrow, "noriginalcurtaxprice");
          }
          else {
            getBillCardPanel().setBodyValueAt(dPrice, this.orderrow, "noriginalcurprice");
          }

        }

        getBillCardPanel().setCellEditable(this.orderrow, "cinventorycode", false);

        getBillCardPanel().setBodyRowState(this.orderrow);
      }
      if (this.SA_02.booleanValue()) {
        getBillCardPanel().calculateNumber(this.orderrow, "noriginalcurtaxprice");
      }
      else {
        getBillCardPanel().calculateNumber(this.orderrow, "noriginalcurprice");
      }
      remove(getBillTreeCardPanel());
      add(getSplitPanelBc(), "Center");
      long s1 = System.currentTimeMillis();
      getBillCardPanel().getBillModel().execLoadFormula();
      SCMEnv.out("执行公式[共用时" + (System.currentTimeMillis() - s1) + "]");
      getBillCardPanel().getBillTable().clearSelection();
      this.strState = this.strOldState;
      initButtons();
      setButtonsState();
      setTitleText(getBillCardPanel().getTitle());

      this.bomID = null;
      updateUI();
    } catch (Throwable ex) {
      handleException(ex);
    }
  }

  protected void onBomSave() {
    long s = System.currentTimeMillis();
    BomorderVO bomorder = null;
    try {
      if (this.strBomState.equals("ADD")) {
        bomorder = (BomorderVO)getBillTreeCardPanel().getBillValueVO(BomorderVO.class.getName(), BomorderHeaderVO.class.getName(), BomorderItemVO.class.getName());

        ((BomorderHeaderVO)bomorder.getParentVO()).setPk_corp(getCorpPrimaryKey());

        ((BomorderHeaderVO)bomorder.getParentVO()).setCreceipttype("3M");

        ((BomorderHeaderVO)bomorder.getParentVO()).setFstatus(new Integer(1));

        bomorder.setStatus(2);
      }
      if (this.strBomState.equals("EDIT")) {
        bomorder = (BomorderVO)getBillTreeCardPanel().getBillValueVO(BomorderVO.class.getName(), BomorderHeaderVO.class.getName(), BomorderItemVO.class.getName());

        ((BomorderHeaderVO)bomorder.getParentVO()).setPk_corp(getCorpPrimaryKey());

        ((BomorderHeaderVO)bomorder.getParentVO()).setCreceipttype("3M");

        ((BomorderHeaderVO)bomorder.getParentVO()).setFstatus(new Integer(1));

        for (int i = 0; i < bomorder.getChildrenVO().length; i++) {
          ((BomorderItemVO[])(BomorderItemVO[])bomorder.getChildrenVO())[i].setStatus(1);
        }

        bomorder.setStatus(1);
      }
      bomorder.validate();
      showHintMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000136"));

      SCMEnv.out("开始保存：" + System.currentTimeMillis());
      if (this.strBomState.equals("ADD")) {
        if (this.isSaveStart) {
          this.bomID = BomorderBO_Client.insert(bomorder);
          int row = getBillCardPanel().getBillTable().getSelectedRow();

          getBillCardPanel().setBodyValueAt(this.bomID, row, "cbomorderid");

          getBillCardPanel().getBillModel().execLoadFormula(row);
        }
        else {
          BomorderBO_Client.insertItems((BomorderItemVO[])bomorder.getChildrenVO(), this.bomID, getCorpPrimaryKey());
        }

        loadBomCurrData();
        this.isSaveStart = false;
      }
      if (this.strBomState.equals("EDIT")) {
        if (this.addData) {
          BomorderBO_Client.insertItems((BomorderItemVO[])bomorder.getChildrenVO(), this.bomID, getCorpPrimaryKey());

          this.addData = false;
        } else {
          bomorder.setPrimaryKey(this.bomID);
          BomorderBO_Client.update(bomorder);
        }
      }
      getBillTreeCardPanel().updateValue();
      showHintMessage(Message.getSaveSuccessMessage(s));
      if (this.strBomState.equals("ADD")) {
        getBillTreeCardPanel().getCustTree().setEnabled(true);
        int yesno = showYesNoMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000396"));

        if (yesno != 4) {
          this.strBomState = "SAVE";
          getBillTreeCardPanel().setEnabled(false);
          setPzdSelectedEnabled(false);
          this.addData = false;
        } else {
          this.strBomState = "EDIT";
          this.addData = false;
        }
      } else if (this.strBomState.equals("EDIT")) {
        getBillTreeCardPanel().getCustTree().setEnabled(true);
        int yesno = showYesNoMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000138"));

        if (yesno != 4) {
          this.strBomState = "SAVE";
        }
      }
      setBomButtonsState();

      SCMEnv.out("保存结束" + System.currentTimeMillis());
      showHintMessage(Message.getSaveSuccessMessage(s));
    } catch (ValidationException e) {
      showErrorMessage(e.getMessage());
    } catch (Exception e) {
      showErrorMessage(e.getMessage());
      SCMEnv.out("数据保存失败！");
    }
  }

  protected void onBusiType(ButtonObject bo)
  {
    bo.setSelected(true);

    this.boBusiType.setTag(bo.getTag());
    getBillCardPanel().setBusiType(bo.getTag());
    getBillListPanel().setBusiType(bo.getTag());

    setButtonsState();
    setButtons(getBillButtons());
  }

  public void onButtonClicked(ButtonObject bo)
  {
    try
    {
      if ((this.strShowState.equals("列表")) && (getFuncExtend() != null))
      {
        getFuncExtend().doAction(bo, this, getBillCardPanel(), getBillListPanel(), 1);
      }
      else if ((this.strShowState.equals("卡片")) && (getFuncExtend() != null))
      {
        getFuncExtend().doAction(bo, this, getBillCardPanel(), getBillListPanel(), 0);
      }

      onExtendBtnsClick(bo);
    }
    catch (Throwable exx) {
      exx.printStackTrace();
    }

    if ((this.strShowState.equals("列表")) && (getBillListPanel().getHeadTable().getSelectedRowCount() <= 0) && (bo.getParent() != this.boBusiType) && (bo.getParent() != this.boBrowse) && (bo != this.boDocument) && (bo != this.boListDeselectAll) && (bo != this.boListSelectAll) && (bo != this.boCard) && (bo != this.boQuery) && (bo.getParent() != this.boAdd) && (bo.getParent() != this.boBusiType))
    {
      showWarningMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000281"));

      return;
    }

    if (bo == this.boListSelectAll)
      onSelectAll();
    else if (bo == this.boListDeselectAll) {
      onDeSelectAll();
    }
    else if ((this.strShowState.equals("列表")) && (getBillListPanel().getHeadTable().getSelectedRowCount() > 1) && (bo != this.boSendAudit) && (bo != this.boCard))
    {
      if ((bo == this.boPrint) || (bo == this.boPreview) || (bo == this.boQuery) || (bo.getParent() == this.boBusiType) || (bo.getParent() == this.boBrowse) || ((bo.getTag() != null) && (bo.getTag().equals("APPROVE"))) || ((bo.getTag() != null) && (bo.getTag().equals("SoUnApprove"))) || ((bo.getTag() != null) && (bo.getTag().equals("SoBlankout"))))
      {
        if ((bo.getTag() != null) && (bo.getTag().equals("APPROVE")))
        {
          int[] selrow = getBillListPanel().getHeadTable().getSelectedRows();

          if ((selrow != null) && (selrow.length > 0)) {
            for (int i = 0; i < selrow.length; i++) {
              int iStatus = Integer.parseInt(getBillListPanel().getHeadItem("fstatus").converType(getBillListPanel().getHeadBillModel().getValueAt(selrow[i], "fstatus")).toString());
              try
              {
                if (isExistWorkflow(selrow[i])) {
                  showErrorMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000282", null, new String[] { selrow[i] + 1 + "" }));

                  return;
                }
              } catch (Exception e) {
                showErrorMessage(e.getMessage());
                return;
              }
              if (iStatus != 1) {
                showErrorMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000284", null, new String[] { selrow[i] + 1 + "" }));

                return;
              }
            }
          }
        }
        else if ((bo.getTag() != null) && (bo.getTag().equals("SoUnApprove")))
        {
          int[] selrow = getBillListPanel().getHeadTable().getSelectedRows();

          for (int i = 0; i < selrow.length; i++)
          {
            int iStatus = Integer.parseInt(getBillListPanel().getHeadItem("fstatus").converType(getBillListPanel().getHeadBillModel().getValueAt(selrow[i], "fstatus")).toString());
            try
            {
              if (isExistWorkflow(selrow[i])) {
                showErrorMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000286", null, new String[] { selrow[i] + 1 + "" }));

                return;
              }
            } catch (Exception e) {
              showErrorMessage(e.getMessage());
              return;
            }
            if (iStatus != 2) {
              showErrorMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000287", null, new String[] { selrow[i] + 1 + "" }));

              return;
            }
          }
        }
        else if ((bo.getTag() != null) && (bo.getTag().equals("SoBlankout")))
        {
          int[] selrow = getBillListPanel().getHeadTable().getSelectedRows();

          for (int i = 0; i < selrow.length; i++)
          {
            int iStatus = Integer.parseInt(getBillListPanel().getHeadItem("fstatus").converType(getBillListPanel().getHeadBillModel().getValueAt(selrow[i], "fstatus")).toString());

            if (iStatus != 1) {
              showErrorMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000527", null, new String[] { selrow[i] + 1 + "" }));

              return;
            }
          }
        }
      }
      else
      {
        showErrorMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000288"));

        return;
      }
    }

    getBillCardPanel().stopEditing();

    showHintMessage("");
    try {
      if (bo == this.boAddLine) {
        onAddLine();
      } else if (bo == this.boDelLine) {
        onDelLine();
      } else if (bo == this.boInsertLine) {
        onInsertLine();
      } else if (bo == this.boCopyLine) {
        onCopyLine();
      } else if (bo == this.boPasteLine) {
        onPasteLine();
      } else if (bo == this.boSave) {
        onSave();
      } else if (bo == this.boCancel) {
        onCancel();
      } else if (bo == this.boEdit) {
        onEdit();
      } else if (bo == this.boCard) {
        onCard();
      } else if (bo == this.boPreview) {
        onPrint(true);
      } else if (bo == this.boPrint) {
        onPrint(false);
      } else if (bo == this.boFind) {
        onFind();
      } else if (bo == this.boFirst) {
        onFrist();
      } else if (bo == this.boLast) {
        onLast();
      } else if (bo == this.boPre) {
        onPre();
      } else if (bo == this.boNext) {
        onNext();
      } else if ((bo == this.boDocument) || ("document".equals(bo.getTag()))) {
        onDocument();
      } else if (bo == this.boReturn) {
        onReturn();
      } else if (bo == this.boBack) {
        onBack();
      } else if (bo == this.boQuery) {
        onQuery();
      } else if (bo == this.boRefresh) {
        onRefresh();
      } else if (bo == this.boRefundment) {
        onRefundment();
      } else if (bo == this.boBatch) {
        onBatch();
      } else if (bo == this.boOrderQuery) {
        onOrderQuery(); } else {
        if (bo == this.boBom) {
          onBom();
          return;
        }if (bo == this.boBomSave) {
          onBomSave();
        } else if (bo == this.boBomEdit) {
          onBomEdit();
        } else if (bo == this.boBomCancel) {
          onBomCancel();
        } else if ((bo == this.boAuditFlowStatus) || ("auditflowstatus".equals(bo.getTag())))
        {
          onAuditFlowStatus();
        } else if (bo == this.boSendAudit) {
          onSendAudit();
        } else if (bo == this.boBomPrint) {
          onBomPrint();
        } else if (bo == this.boBomReturn) {
          onBomReturn();
        } else if (bo == this.boModification) {
          onModification();
        } else if (bo.getParent() == this.boBusiType)
        {
          onBusiType(bo);
        } else if (bo.getParent() == this.boAdd)
        {
          onNew(bo);
          try {
            this.m_ActionTime = RecordTimeHelper.getTimeStamp();
          } catch (Throwable ex) {
          }
        }
        else if (bo == this.boCopyBill) {
          onCopyBill();
        } else if ((bo.getParent() == this.boAction) || ("APPROVE".equals(bo.getTag())) || ("SoUnApprove".equals(bo.getTag())) || ("SoBlankout".equals(bo.getTag())))
        {
          onAction(bo); } else {
          if (bo == this.boCachPay)
          {
            onCachPay();
            return;
          }if (bo == this.boOrdBalance)
          {
            onOrderBalance();
            return;
          }if (bo == this.boOnHandShowHidden)
          {
            onOnHandShowHidden();
          } else if (bo == this.boCustCredit)
          {
            onAssistant(bo);
          } else if (bo == this.boOrderExecRpt)
          {
            onAssistant(bo);
          } else if (bo == this.boCustInfo)
          {
            onAssistant(bo);
          } else if (bo == this.boPrifit)
          {
            onAssistant(bo);
          } else if (bo.getParent() == this.boAssistant)
          {
            onAssistant(bo);
          } else if (bo.getParent() == this.boAfterAction)
          {
            onAfterAction(bo);
          } else {
            ButtonObject[] btns = getSaleReceiveUI().getButtons();
            int i = 0; for (int loop = btns.length; i < loop; i++) {
              if ((btns[i] == bo) || (btns[i] == bo.getParent())) {
                getSaleReceiveUI().onButtonClicked(bo);
                updateButtons();
                updateUI();
                return;
              }
            }
            btns = getOrderBalanceUI().getButtons();
            int j = 0; for (int loop = btns.length; j < loop; j++)
              if ((btns[j] == bo) || (btns[j] == bo.getParent())) {
                getOrderBalanceUI().onButtonClicked(bo);
                updateButtons();
                updateUI();
                return;
              }
          }
        }
      }
    } catch (Exception e) {
      handleException(e);
    }

    setButtonsState();
    if ((bo.getParent() == this.boAdd) || (bo == this.boCopyBill) || (bo == this.boRefundment) || (bo == this.boEdit))
    {
      getBillCardPanel().transferFocusTo(0);
    }
  }

  protected void onSelectAll()
  {
    try {
      int maxindex = getBillListPanel().getHeadBillModel().getRowCount();
      if (maxindex > 0)
        getBillListPanel().getHeadTable().setRowSelectionInterval(0, maxindex - 1);
    }
    catch (Exception e) {
      SCMEnv.out(e.getMessage());
    }
  }

  protected void onDeSelectAll()
  {
    try {
      int maxindex = getBillListPanel().getHeadBillModel().getRowCount();
      if (maxindex > 0)
        getBillListPanel().getHeadTable().clearSelection();
    } catch (Exception e) {
      SCMEnv.out(e.getMessage());
    }
  }

  private boolean isExistWorkflow(int row)
    throws ValidationException
  {
    boolean isExist = false;
    try {
      isExist = PfUtilBO_Client.isExistWorkFlow(getBillType(), this.boBusiType.getTag(), getClientEnvironment().getCorporation().getPk_corp(), (String)getBillListPanel().getHeadBillModel().getValueAt(row, "coperatorid"));
    }
    catch (Throwable e)
    {
      throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000276"));
    }

    return isExist;
  }

  public void onCancel()
  {
    getBillCardPanel().setEnabled(false);

    this.strState = "自由";

    getBillCardPanel().resumeValue();

    if ((this.SO41.booleanValue()) && 
      (getBillCardTools().getOldsaleordervo() != null)) {
      getBillCardPanel().setTailItem("coperatorid", getBillCardTools().getOldsaleordervo().getHeadVO().getCoperatorid());
    }

    String csaleid = (String)getBillCardPanel().getHeadItem("csaleid").getValueObject();

    SaleOrderVO saleorder = this.vocache.getSaleOrderVO(csaleid);
    loadCardData(saleorder);
    setButtonsState();
    getBillCardPanel().showCustManArInfo();
    updateUI();

    getBillCardTools().setHeadRefLimit(this.strState);

    showHintMessage("");

    InvAttrCellRenderer ficr = new InvAttrCellRenderer();
    ficr.setFreeItemRenderer(getBillCardPanel(), null);
  }

  protected void onCard()
  {
    this.strShowState = "卡片";
    this.strState = "自由";
    switchInterface();
    this.num = getBillListPanel().getHeadTable().getSelectedRow();

    String csaleid = (String)getBillListPanel().getHeadBillModel().getValueAt(this.num, "csaleid");

    if (csaleid != null) {
      SaleOrderVO saleorder = this.vocache.getSaleOrderVO(csaleid);
      loadCardData(saleorder);
    }

    setButtons(getBillButtons());

    showCustManArInfo();
    updateUI();
  }

  protected void onCheck(SaleOrderVO saleorder)
    throws ValidationException, BusinessException
  {
    long s = System.currentTimeMillis();

    if (getBillCardPanel().getRowCount() == 0) {
      throw new ValidationException(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000073"));
    }

    Object oaccountperiod = getBillCardPanel().getHeadItem("naccountperiod").getValueObject();

    if ((oaccountperiod != null) && (oaccountperiod.toString().trim().length() > 0))
    {
      UFDouble naccountperiod = null;
      try {
        naccountperiod = new UFDouble(oaccountperiod.toString().trim());
      }
      catch (Exception e) {
        throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000397"));
      }

      if (naccountperiod.doubleValue() < 0.0D) {
        throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000397"));
      }

    }

    int iOOSLine = 0;

    int iallMinusMnyrow = 0;
    int iallMinusNumrow = 0;
    SaleorderBVO[] bodyVOs = (SaleorderBVO[])saleorder.getChildrenVO();

    UFBoolean bSend = saleorder.getHeadVO().getBdeliver();

    UFBoolean bretflag = saleorder.getHeadVO().getBretinvflag();

    for (int i = 0; i < saleorder.getChildrenVO().length; i++)
    {
      if ((bodyVOs[i].getCconsigncorpid() != null) && (bodyVOs[i].getCconsigncorpid().trim().length() > 0) && (!getCorpPrimaryKey().equals(bodyVOs[i].getCconsigncorpid())))
      {
        if ((bodyVOs[i].getCinventoryid1() == null) || (bodyVOs[i].getCinventoryid1().trim().length() <= 0))
        {
          throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000193", null, new String[] { i + 1 + "" }));
        }

      }

      if ((bodyVOs[i].getNoriginalcursummny() != null) && (bodyVOs[i].getNoriginalcursummny().doubleValue() < 0.0D))
      {
        iallMinusMnyrow++;
      }

      if (((bodyVOs[i].getNoriginalcurtaxprice() != null) && (bodyVOs[i].getNoriginalcurtaxprice().doubleValue() < 0.0D)) || ((bodyVOs[i].getNoriginalcurtaxnetprice() != null) && (bodyVOs[i].getNoriginalcurtaxnetprice().doubleValue() < 0.0D)) || ((bodyVOs[i].getNoriginalcurprice() != null) && (bodyVOs[i].getNoriginalcurprice().doubleValue() < 0.0D)) || ((bodyVOs[i].getNoriginalcurnetprice() != null) && (bodyVOs[i].getNoriginalcurnetprice().doubleValue() < 0.0D)))
      {
        throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000420", null, new String[] { i + 1 + "" }));
      }

      if ((bodyVOs[i].getNnumber() != null) && (bodyVOs[i].getNnumber().doubleValue() >= 0.0D))
      {
        if ((bretflag != null) && (bretflag.booleanValue())) {
          throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000195", null, new String[] { i + 1 + "" }));
        }

      }

      if ((bodyVOs[i].getNnumber() != null) && (bodyVOs[i].getNnumber().doubleValue() < 0.0D))
      {
        if ((!this.SO45.booleanValue()) && ((bretflag == null) || (!bretflag.booleanValue())))
        {
          throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000194", null, new String[] { i + 1 + "" }));
        }

        iallMinusNumrow++;
      }
      if ((bodyVOs[i].getBoosflag() != null) && (bodyVOs[i].getBoosflag().booleanValue()))
      {
        iOOSLine++;
      } else {
        if (bodyVOs[i].getBoosflag() == null) {
          bodyVOs[i].setBoosflag(new UFBoolean(false));
        }
        if ((bodyVOs[i].getNnumber() != null) && (bodyVOs[i].getNnumber().doubleValue() < 0.0D))
        {
          if ((bSend != null) && (bSend.booleanValue())) {
            throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000198"));
          }

        }

      }

    }

    if (iOOSLine == bodyVOs.length) {
      throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000398"));
    }

    if ((this.SO_01 != null) && 
      (this.SO_01.intValue() != 0) && 
      (this.SO_01.intValue() < getBillCardPanel().getRowCount())) {
      throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000171", null, new String[] { this.SO_01.intValue() + "" }));
    }

    if ((getBillType().equals("30")) || (getBillType().equals("3A")))
    {
      if ((getBillCardPanel().getHeadItem("nexchangeotobrate").getValueObject() == null) || (getBillCardPanel().getHeadItem("nexchangeotobrate").getValueObject().equals("")))
      {
        throw new ValidationException(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000310"));
      }

      String ccurrencytypeid = getBillCardPanel().getHeadItem("ccurrencytypeid").getValue();

      boolean bcheck = false;
      try {
        if (ccurrencytypeid != null)
          bcheck = this.currtype.isLocalCurrType(ccurrencytypeid);
      } catch (Exception e) {
        SCMEnv.out(e);
      }

      if ((this.BD302 != null) && (this.BD302.booleanValue()) && 
        (!bcheck) && (
        (getBillCardPanel().getHeadItem("nexchangeotoarate").getValueObject() == null) || (getBillCardPanel().getHeadItem("nexchangeotoarate").getValueObject().equals(""))))
      {
        throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000200"));
      }

    }

    saleorder.validate();

    boolean bischeckaddr = false;
    BillItem biaddr = getBillCardPanel().getHeadItem("vreceiveaddress");
    try
    {
      if (biaddr != null) {
        bischeckaddr = biaddr.isNull();
        biaddr.setNull(false);
      }

      int[] notcheckline = getNotCheckFreeItemLine();
      if (notcheckline == null) {
        getBillCardPanel().getBillData().dataNotNullValidate();
      } else {
        BillItem item = getBillCardPanel().getBodyItem("vfree0");
        if (item == null) {
          getBillCardPanel().getBillData().dataNotNullValidate();
        } else {
          Hashtable ht = new Hashtable();
          ht.put(item, notcheckline);
          getBillCardPanel().getBillData().dataNotNullValidate(ht);
        }
      }

      if ((biaddr != null) && 
        (bischeckaddr)) {
        String saddr = ((UIRefPane)biaddr.getComponent()).getUITextField().getText();

        if ((saddr == null) || (saddr.trim().length() <= 0)) {
          throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000083") + biaddr.getName());
        }

      }

    }
    finally
    {
      biaddr.setNull(bischeckaddr);
    }

    boolean retflag = false;
    UFBoolean bret = new UFBoolean(getBillCardPanel().getHeadItem("bretinvflag").getValue());

    if ((bret != null) && (bret.toString().equals("Y"))) {
      retflag = true;
    }

    if ((this.SO27 != null) && (this.SO27.booleanValue())) {
      getBillCardTools().checkProdLineForOne();
    }

    boolean isOtherRow = false;

    HashMap hp = new HashMap();
    if (this.strState.equals("修订")) {
      SaleOrderVO svo = getBillCardTools().getOldsaleordervo();
      for (int i = 0; i < svo.getChildrenVO().length; i++) {
        if (svo.getChildrenVO()[i].getPrimaryKey() != null) {
          hp.put(svo.getChildrenVO()[i].getPrimaryKey(), svo.getChildrenVO()[i]);
        }
      }
    }

    for (int i = 0; i < saleorder.getChildrenVO().length; i++)
    {
      SaleorderBVO oldbodyVO = (SaleorderBVO)saleorder.getChildrenVO()[i];

      if ((oldbodyVO.getCconsigncorpid() != null) && (!oldbodyVO.getCconsigncorpid().equals(getCorpPrimaryKey())))
      {
        isOtherRow = true;
      }
      else
      {
        isOtherRow = false;
      }

      if (isOtherRow)
      {
        if ((oldbodyVO.getCreccalbodyid() == null) || (oldbodyVO.getCreccalbodyid().trim().length() <= 0))
        {
          throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000202"));
        }

        if ((oldbodyVO.getBdericttrans() == null) || (!oldbodyVO.getBdericttrans().booleanValue()))
        {
          if ((oldbodyVO.getCrecwareid() == null) || (oldbodyVO.getCrecwareid().trim().length() <= 0))
          {
            throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000203"));
          }

        }

        if ((oldbodyVO.getLaborflag() != null) && (oldbodyVO.getLaborflag().booleanValue()))
        {
          throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000204"));
        }

        if ((oldbodyVO.getDiscountflag() != null) && (oldbodyVO.getDiscountflag().booleanValue()))
        {
          throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000205"));
        }

      }

      if (!isOtherRow)
      {
        if ((oldbodyVO.getCadvisecalbodyid() == null) || (oldbodyVO.getCadvisecalbodyid().equals("")))
        {
          throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000206"));
        }

      }

      if (oldbodyVO.getStatus() == 2) {
        oldbodyVO.setPrimaryKey(null);
        oldbodyVO.setCsaleid(null);
      }
      if ((oldbodyVO.getDiscountflag() == null) || (!oldbodyVO.getDiscountflag().booleanValue()))
      {
        if ((oldbodyVO.getNnumber() == null) || (oldbodyVO.getNnumber().doubleValue() == 0.0D))
        {
          throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000207"));
        }

        if ((oldbodyVO.getNquoteunitnum() == null) || (oldbodyVO.getNquoteunitnum().doubleValue() == 0.0D))
        {
          throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000208"));
        }

      }

      if ((oldbodyVO.getNexchangeotobrate() == null) && 
        (getBillCardPanel().getHeadItem("nexchangeotobrate").getValueObject() != null) && (!getBillCardPanel().getHeadItem("nexchangeotobrate").getValueObject().equals("")))
      {
        oldbodyVO.setNexchangeotobrate(new UFDouble(getBillCardPanel().getHeadItem("nexchangeotobrate").getValue()));
      }

      if ((oldbodyVO.getNexchangeotoarate() == null) && 
        (getBillCardPanel().getHeadItem("nexchangeotoarate").getValueObject() != null) && (!getBillCardPanel().getHeadItem("nexchangeotoarate").getValueObject().equals("")))
      {
        oldbodyVO.setNexchangeotoarate(new UFDouble(getBillCardPanel().getHeadItem("nexchangeotoarate").getValue()));
      }

      if ((oldbodyVO.getDiscountflag() == null) || (!oldbodyVO.getDiscountflag().booleanValue()))
      {
        if ((oldbodyVO.getBlargessflag() != null) && (!oldbodyVO.getBlargessflag().booleanValue()))
        {
          if ((!getBillType().equals("3A")) && (
            (oldbodyVO.getNoriginalcurprice() == null) || (oldbodyVO.getNoriginalcurprice().doubleValue() < 0.0D)))
          {
            throw new ValidationException(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000063"));
          }

          if (((oldbodyVO.getNoriginalcurmny() != null) && (oldbodyVO.getNoriginalcurmny().doubleValue() < 0.0D)) || ((oldbodyVO.getNoriginalcurtaxmny() != null) && (oldbodyVO.getNoriginalcurtaxmny().doubleValue() < 0.0D)) || ((oldbodyVO.getNoriginalcursummny() != null) && (oldbodyVO.getNoriginalcursummny().doubleValue() < 0.0D)))
          {
            if (oldbodyVO.getNnumber().doubleValue() > 0.0D) {
              throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000209"));
            }

          }

          if ((oldbodyVO.getNoriginalcursummny() == null) && (oldbodyVO.getNoriginalcursummny().doubleValue() == 0.0D))
          {
            throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000210"));
          }

        }

      }
      else
      {
        if (oldbodyVO.getNoriginalcursummny() == null) {
          throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000210"));
        }

        if ((oldbodyVO.getNnumber() == null) && (oldbodyVO.getNoriginalcurmny() == null) && (oldbodyVO.getNoriginalcursummny() == null))
        {
          throw new ValidationException(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000261"));
        }

      }

      if ((oldbodyVO.getAssistunit() != null) && (oldbodyVO.getAssistunit().booleanValue()))
      {
        if ((oldbodyVO.getDiscountflag() != null) && (!oldbodyVO.getDiscountflag().booleanValue()))
        {
          if ((oldbodyVO.getCpackunitid() == null) || (oldbodyVO.getCpackunitid().trim().length() == 0))
          {
            throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000211", null, new String[] { i + 1 + "" }));
          }

          if (oldbodyVO.getNpacknumber() == null) {
            throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000212", null, new String[] { i + 1 + "" }));
          }

          if (oldbodyVO.getNpacknumber().doubleValue() * oldbodyVO.getNnumber().doubleValue() < 0.0D)
          {
            throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000213", null, new String[] { i + 1 + "" }));
          }

        }

      }

      UFDate dbilldate = new UFDate(getBillCardPanel().getHeadItem("dbilldate").getValue());

      UFDate dconsigndate = oldbodyVO.getDconsigndate();
      UFDate deliverdate = oldbodyVO.getDdeliverdate();
      if ((dconsigndate == null) || (dconsigndate.toString().length() == 0) || (deliverdate == null) || (deliverdate.toString().length() == 0))
      {
        throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000214"));
      }

      if ((!retflag) && (dconsigndate != null) && (dbilldate != null) && 
        (dbilldate.after(dconsigndate)) && (dbilldate != dconsigndate)) {
        throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000033"));
      }

      if ((dconsigndate != null) && (deliverdate != null) && 
        (dconsigndate.after(deliverdate)) && (deliverdate != dconsigndate))
      {
        throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000034"));
      }

      if ((!isOtherRow) && 
        (oldbodyVO.getFbatchstatus() != null) && (oldbodyVO.getFbatchstatus().intValue() == 1) && (
        (oldbodyVO.getCbatchid() == null) || (oldbodyVO.getCbatchid().trim().length() == 0)))
      {
        throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000215"));
      }

      String oldinventoryid = oldbodyVO.getCinventoryid();

      if ((oldbodyVO.getBlargessflag() != null) && (!oldbodyVO.getBlargessflag().booleanValue()))
      {
        for (int j = i + 1; j < saleorder.getChildrenVO().length; j++) {
          SaleorderBVO newbodyVO = (SaleorderBVO)saleorder.getChildrenVO()[j];

          if (!this.SO_03.booleanValue()) {
            String newinventoryid = newbodyVO.getCinventoryid();
            if ((newbodyVO.getBlargessflag() != null) && (!newbodyVO.getBlargessflag().booleanValue()))
            {
              if (oldinventoryid.equals(newinventoryid)) {
                throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000216", null, new String[] { i + 1 + "", j + 1 + "" }));
              }

            }

          }

        }

      }

      if (this.strState.equals("修订"))
      {
        UFDouble nnumber = oldbodyVO.getNnumber();
        CircularlyAccessibleValueObject coriVO = (CircularlyAccessibleValueObject)hp.get(oldbodyVO.getPrimaryKey());

        if ((coriVO != null) && (coriVO.getAttributeValue("nnumber") != null) && (oldbodyVO.getNnumber() != null) && (((UFDouble)coriVO.getAttributeValue("nnumber")).multiply(oldbodyVO.getNnumber()).doubleValue() < 0.0D))
        {
          throw new ValidationException(NCLangRes.getInstance().getStrByID("sopub", "UPPsopub-000007"));
        }

        checkAllowDiffer(oldbodyVO, i);

        if ((oldbodyVO.getNtotalreceiptnumber() != null) && (oldbodyVO.getNtotalreceiptnumber().abs().doubleValue() > 0.0D))
        {
          if ((bSend == null) || (!bSend.booleanValue())) {
            throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000223"));
          }

        }

        if ((oldbodyVO.getNtotalinventorynumber() != null) && (oldbodyVO.getNtotalinventorynumber().abs().doubleValue() > 0.0D) && ((oldbodyVO.getNtotalreceiptnumber() == null) || (oldbodyVO.getNtotalreceiptnumber().doubleValue() == 0.0D)))
        {
          if ((bSend != null) && (bSend.booleanValue())) {
            throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000224"));
          }

        }

        UFDouble ntotalconvertnum = SoVoTools.getMnyAdd(SoVoConst.duf0, oldbodyVO.getNarrangescornum());

        ntotalconvertnum = SoVoTools.getMnyAdd(ntotalconvertnum, oldbodyVO.getNarrangepoapplynum());

        ntotalconvertnum = SoVoTools.getMnyAdd(ntotalconvertnum, oldbodyVO.getNarrangetoornum());

        ntotalconvertnum = SoVoTools.getMnyAdd(ntotalconvertnum, oldbodyVO.getNorrangetoapplynum());

        ntotalconvertnum = SoVoTools.getMnyAdd(ntotalconvertnum, oldbodyVO.getNarrangemonum());

        nnumber = oldbodyVO.getNnumber();
        if ((nnumber != null) && (nnumber.doubleValue() < 0.0D)) {
          nnumber = nnumber.multiply(-1.0D);
          ntotalconvertnum = ntotalconvertnum == null ? new UFDouble(0) : ntotalconvertnum.multiply(-1.0D);
        }

        if ((ntotalconvertnum != null) && (nnumber != null) && (ntotalconvertnum.abs().compareTo(nnumber.abs().multiply(this.SO43.add(1.0D))) > 0))
        {
          throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000501", null, new String[] { i + 1 + "" }));
        }

      }

      if (this.strState.equals("退货")) {
        UFDouble nnumber = oldbodyVO.getNnumber();
        UFDouble ntotalreceiptnumber = oldbodyVO.getNtotalreceiptnumber();

        if ((nnumber != null) && 
          (nnumber.compareTo(new UFDouble(0)) >= 0)) {
          throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000225", null, new String[] { i + 1 + "" }));
        }

        if ((nnumber != null) && (ntotalreceiptnumber != null) && 
          (nnumber.compareTo(ntotalreceiptnumber) == 1)) {
          throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000226", null, new String[] { i + 1 + "" }));
        }

        UFDouble ntotalinvoicenumber = oldbodyVO.getNtotalinvoicenumber();

        if ((nnumber != null) && (ntotalinvoicenumber != null) && 
          (nnumber.compareTo(ntotalinvoicenumber) == 1)) {
          throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000228", null, new String[] { i + 1 + "" }));
        }

        UFDouble ntotalinventorynumber = oldbodyVO.getNtotalinventorynumber();

        if ((nnumber != null) && (ntotalinventorynumber != null) && 
          (nnumber.compareTo(ntotalinventorynumber) == 1)) {
          throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000229", null, new String[] { i + 1 + "" }));
        }

      }

    }

    System.out.println("合法性检查用时：" + (System.currentTimeMillis() - s) + "毫秒");
  }

  private void checkAllowDiffer(SaleorderBVO oldbodyVO, int i)
    throws ValidationException
  {
    SaleOrderVO vo = this.vocache.getSaleOrderVO(oldbodyVO.getCsaleid());
    SaleorderBVO bvo = null;
    if ((vo != null) && (vo.getBodyVOs() != null)) {
      for (int k = 0; k < vo.getBodyVOs().length; k++) {
        if (vo.getBodyVOs()[k].getCorder_bid().equals(oldbodyVO.getCorder_bid()))
        {
          bvo = vo.getBodyVOs()[k];
        }
      }
    }
    if (bvo == null) {
      bvo = oldbodyVO;
    }
    UFDouble nnumber = oldbodyVO.getNnumber() == null ? new UFDouble(0) : oldbodyVO.getNnumber();

    boolean bisNeg = false;
    if (nnumber.doubleValue() < 0.0D) {
      bisNeg = true;
      nnumber = nnumber.multiply(-1.0D);
    }

    UFDouble ntotalreceiptnumber = bvo.getNtotalreceiptnumber() == null ? new UFDouble(0) : bvo.getNtotalreceiptnumber();

    UFDouble ntotalinvoicenumber = bvo.getNtotalinvoicenumber() == null ? new UFDouble(0) : bvo.getNtotalinvoicenumber();

    UFDouble ntotalinventorynumber = (bvo.getNtotalinventorynumber() == null ? new UFDouble(0) : bvo.getNtotalinventorynumber()).add(bvo.getNtotalshouldoutnum() == null ? new UFDouble(0) : bvo.getNtotalshouldoutnum());

    UFDouble ntranslossnum = bvo.getNtranslossnum() == null ? new UFDouble(0) : bvo.getNtranslossnum();

    ntotalinventorynumber = ntotalinventorynumber.sub(ntranslossnum);

    if (bisNeg) {
      ntotalreceiptnumber = ntotalreceiptnumber.multiply(-1.0D);
      ntotalinvoicenumber = ntotalinvoicenumber.multiply(-1.0D);
      ntotalinventorynumber = ntotalinventorynumber.multiply(-1.0D);
    }

    UFDouble postiveNum = null;

    String FHMSG = NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000218", null, new String[] { i + 1 + "" });

    String CKMSG = NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000222", null, new String[] { i + 1 + "" });

    String KPMSG = NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000221", null, new String[] { i + 1 + "" });

    ArrayList al = new ArrayList();

    UFDouble nouttoplimit = (UFDouble)bvo.getAttributeValue("nouttoplimit");

    if (nouttoplimit == null)
      nouttoplimit = this.SO24;
    if (this.SO23.booleanValue()) {
      postiveNum = nnumber.multiply(1.0D + nouttoplimit.doubleValue() / 100.0D);
    }
    else {
      postiveNum = nnumber;
    }
    if (postiveNum.compareTo(ntotalreceiptnumber) < 0) {
      al.add(FHMSG);
    }

    if (this.SO25.booleanValue())
      postiveNum = nnumber.multiply(1.0D + this.SO26.doubleValue() / 100.0D);
    else {
      postiveNum = nnumber;
    }
    if (postiveNum.compareTo(ntotalinvoicenumber) < 0) {
      al.add(KPMSG);
    }

    if (this.IC003.booleanValue()) {
      postiveNum = nnumber.multiply(1.0D + nouttoplimit.doubleValue() / 100.0D);
    }
    else {
      postiveNum = nnumber;
    }
    if (postiveNum.compareTo(ntotalinventorynumber) < 0) {
      al.add(CKMSG);
    }

    if (al.size() > 0) {
      String sScoremsg = "";
      for (int j = 0; j < al.size(); j++) {
        sScoremsg = sScoremsg + (String)al.get(j) + "\n";
      }
      throw new ValidationException(sScoremsg);
    }
  }

  protected void onDocument()
  {
    String billcode = null;
    if (this.strShowState.equals("列表")) {
      this.num = getBillListPanel().getHeadTable().getSelectedRow();
      this.id = getBillListPanel().getHeadBillModel().getValueAt(this.num, "csaleid").toString();

      billcode = getBillListPanel().getHeadBillModel().getValueAt(this.num, "vreceiptcode").toString();
    }
    else {
      this.id = ((String)getBillCardPanel().getHeadItem("csaleid").getValueObject());

      billcode = getBillCardPanel().getHeadItem("vreceiptcode").getValue();
    }

    DocumentManager.showDM(this, new String[] { this.id }, new String[] { billcode });
  }

  protected void onEdit()
  {
    if (this.strShowState.equals("列表"))
    {
      this.strShowState = "卡片";
      switchInterface();
      this.num = getBillListPanel().getHeadTable().getSelectedRow();

      String csaleid = (String)getBillListPanel().getHeadBillModel().getValueAt(this.num, "csaleid");

      SaleOrderVO saleorder = this.vocache.getSaleOrderVO(csaleid);
      loadCardData(saleorder);

      setButtons(getBillButtons());
    }

    this.strState = "修改";
    setButtonsState();
    getBillCardPanel().setEnabled(true);

    getBillCardTools().resumeBillItemEditToInit();

    this.vRowATPStatus = new Vector();
    for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
      this.vRowATPStatus.addElement(new UFBoolean(false));

      ctlUIOnCconsignCorpChg(i);
    }

    if (this.alInvs == null) {
      initInvList();
    }

    setNoEditItem();

    UIRefPane vreceiveaddress = (UIRefPane)getBillCardPanel().getHeadItem("vreceiveaddress").getComponent();

    if ((vreceiveaddress != null) && 
      (getBillCardPanel().getHeadItem("creceiptcustomerid") != null))
    {
      ((CustAddrRefModel)vreceiveaddress.getRefModel()).setCustId(getBillCardPanel().getHeadItem("creceiptcustomerid").getValue());
    }

    int iStatus = Integer.parseInt(getBillCardPanel().getHeadItem("fstatus").getValue());

    if (iStatus == 8) {
      for (int i = 0; i < getBillCardPanel().getBillModel().getRowCount(); i++) {
        getBillCardPanel().getBillModel().setRowState(i, 2);
      }

    }

    SaleOrderVO hvo = (SaleOrderVO)getVO(false);
    SaleorderHVO header = (SaleorderHVO)hvo.getParentVO();
    this.m_oldreceipt = header.getVreceiptcode();

    if ((hvo.getHeadVO().getNaccountperiod() != null) && (hvo.getHeadVO().getNaccountperiod().doubleValue() < 0.0D))
    {
      getBillCardPanel().setHeadItem("naccountperiod", null);
    }
    if (this.SO41.booleanValue()) {
      getBillCardPanel().setTailItem("coperatorid", getClientEnvironment().getUser().getPrimaryKey());
    }

    getBillCardTools().setOldsaleordervo(hvo);
    try
    {
      this.alInvs_bak = ((ArrayList)ObjectUtils.serializableClone(this.alInvs));

      this.vRowATPStatus_bak = ((Vector)ObjectUtils.serializableClone(this.vRowATPStatus));
    }
    catch (Exception e)
    {
      SCMEnv.out(e.getMessage());
      this.vRowATPStatus_bak = this.vRowATPStatus;
      this.alInvs_bak = this.alInvs;
      initInvList();
    }

    showCustManArInfo();

    getBillCardTools().clearCatheData();
    try
    {
      InvAttrCellRenderer ficr = new InvAttrCellRenderer();
      ficr.setFreeItemRenderer(getBillCardPanel(), this.alInvs);
    }
    catch (Exception e) {
    }
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH027"));
  }

  protected void onFind()
  {
    this.num = getBillListPanel().getHeadTable().getSelectedRow();
    LocateDialog dlg = new LocateDialog(this, getBillListPanel().getHeadTable());

    dlg.showModal();
    int newnum = getBillListPanel().getHeadTable().getSelectedRow();
    if ((newnum >= 0) && (newnum < getBillListPanel().getHeadBillModel().getRowCount()))
    {
      BillEditEvent e = new BillEditEvent(this, this.num, newnum);
      bodyRowChange(e);
      this.num = getBillListPanel().getHeadTable().getSelectedRow();
    } else {
      getBillListPanel().getHeadTable().getSelectionModel().setSelectionInterval(getBillListPanel().getHeadTable().getSelectedRow(), getBillListPanel().getHeadTable().getSelectedRow());
    }
  }

  protected void onModification()
  {
    if (this.strShowState.equals("列表")) {
      this.strShowState = "卡片";
      this.strState = "自由";
      switchInterface();
      this.num = getBillListPanel().getHeadTable().getSelectedRow();

      String csaleid = (String)getBillListPanel().getHeadBillModel().getValueAt(this.num, "csaleid");

      SaleOrderVO saleorder = this.vocache.getSaleOrderVO(csaleid);
      loadCardData(saleorder);

      updateCacheVO();

      setButtons(getBillButtons());
    }
    else {
      updateCacheVO();
    }

    getBillCardPanel().setEnabled(true);

    getBillCardTools().resumeBillItemEditToInit();

    this.strState = "修订";

    getBillCardTools().setHeadRefLimit(this.strState);

    this.vRowATPStatus = new Vector();
    for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
      this.vRowATPStatus.addElement(new UFBoolean(false));

      ctlUIOnCconsignCorpChg(i);
    }

    if (this.alInvs == null) {
      initInvList();
    }

    setNoEditItem();

    setEditWhenModification();
    getBillCardTools().setCardPanelCellEditableByLargess(this.SO59.booleanValue());

    setNotReviseItems();

    getBillCardPanel().showBodyTableCol("veditreason");
    getBillCardPanel().getBodyItem("veditreason").setEnabled(true);

    SaleOrderVO ordoldvo = (SaleOrderVO)getVO(false);

    if ((ordoldvo.getHeadVO().getNaccountperiod() != null) && (ordoldvo.getHeadVO().getNaccountperiod().doubleValue() < 0.0D))
    {
      getBillCardPanel().setHeadItem("naccountperiod", null);
    }

    getBillCardTools().setOldsaleordervo(ordoldvo);
    try
    {
      this.alInvs_bak = ((ArrayList)ObjectUtils.serializableClone(this.alInvs));

      this.vRowATPStatus_bak = ((Vector)ObjectUtils.serializableClone(this.vRowATPStatus));
    }
    catch (Exception e)
    {
      SCMEnv.out(e.getMessage());
      this.vRowATPStatus_bak = this.vRowATPStatus;
      this.alInvs_bak = this.alInvs;
      initInvList();
    }

    getBillCardTools().clearCatheData();

    showCustManArInfo();
    try
    {
      InvAttrCellRenderer ficr = new InvAttrCellRenderer();
      ficr.setFreeItemRenderer(getBillCardPanel(), this.alInvs);
    }
    catch (Exception e)
    {
    }
    setButtonsState();

    updateUI();
  }

  private void setEditWhenModification()
  {
    Object otemp = getBillCardPanel().getHeadItem("fstatus").getValue();
    if (otemp == null) {
      otemp = "";
    }
    getBillCardTools().setHeadEnable(false);

    getBillCardTools().resumeHeadItemsEditToInit(new String[] { "vreceiptcode", "dbilldate" });

    UFDouble dcathpay = getBillCardTools().getHeadUFDoubleValue("nreceiptcathmny");

    if (((dcathpay == null) || (dcathpay.doubleValue() == 0.0D)) && (this.strState.equals("修订")) && (isHeadCustCanbeModified()))
    {
      getBillCardTools().resumeHeadItemsEditToInit(new String[] { "ccustomerid" });
    }

    Integer fstatus = (Integer)getBillCardPanel().getHeadItem("fstatus").converType(otemp);

    UFBoolean binvoicendflag = getBillCardTools().getHeadUFBooleanValue("binvoicendflag");

    if (((fstatus != null) && (fstatus.intValue() == 6)) || ((binvoicendflag != null) && (binvoicendflag.booleanValue())))
    {
      for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
        getBillCardTools().setRowEnable(i, false);
      }
    }

    String[] bodyItems = { "cinventorycode", "nitemdiscountrate", "ntaxrate", "noriginalcurtaxprice", "noriginalcurprice", "noriginalcurnetprice", "noriginalcurtaxnetprice", "noriginalcurmny", "noriginalcursummny", "noriginalcurtaxmny", "noriginalcurdiscountmny", "blargessflag", "norgqttaxprc", "norgqtprc", "norgqttaxnetprc", "norgqtnetprc", "cpriceitem", "cpriceitemtablename", "cpricepolicy", "cconsigncorp", "creccalbody", "crecwarehouse", "bdericttrans", "crowno", "cadvisecalbody", "cbodywarehousename", "ctransmodeid", "bdeliver", "cprojectname", "creceiptcorpname", "vreceiveaddress", "crecaddrnodename" };

    String[] bodyNoModify = { "cbatchid", "vfree0" };

    getBillCardTools().resumeBillBodyItemsEdit(bodyItems);

    for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
      if (isHasBackwardDoc(i)) {
        getBillCardTools().setBodyCellsEdit(bodyNoModify, i, false);
      }
      getBillCardTools().setBodyCellsEdit(bodyItems, i, true);
    }

    boolean bedit_ccurrencytypeid_h = false;

    boolean bedit_creceiptcorpid_h = false;

    boolean bedit_creceiptcustomerid_h = false;

    boolean bedit_vreceiveaddress_h = false;

    boolean bedit_vnote_h = false;

    boolean bedit_vdef_h = false;

    boolean bedit_bdeliver_h = false;

    boolean bedit_nexchangeotobrate_h = false;

    boolean bedit_nexchangeotoarate_h = false;

    boolean ispay = false;
    UFDouble nreceiptcathmny = getBillCardTools().getHeadUFDoubleValue("nreceiptcathmny");

    if ((nreceiptcathmny != null) && (nreceiptcathmny.doubleValue() > 0.0D)) {
      ispay = true;
    }
    UFDouble ntotalinvoicenumber = null;
    UFDouble ntotalbalancenumber = null;
    UFDouble totalreceiptnumber = null;
    UFBoolean bifinvoicefinish = null;

    UFDouble ntotalinventorynumber = null;

    UFDouble ntalbalancemny = null;
    UFDouble nnumber = null;

    UFDouble nsummny = null;

    String cpackunitid = null;

    String[] keybodys = { "nnumber", "dconsigndate", "tconsigntime", "ddeliverdate", "tdelivertime", "frownote", "cprojectname", "cprojectphasename", "creceiptcorpname", "creceiptareaname", "vreceiveaddress", "crecaddrnodename", "bsafeprice", "breturnprofit", "vdef1", "vdef2", "vdef3", "vdef4", "vdef5", "vdef6", "vdef7", "vdef8", "vdef9", "vdef10", "vdef11", "vdef12", "vdef13", "vdef14", "vdef15", "vdef16", "vdef17", "vdef18", "vdef19", "vdef20", "noriginalcurmny", "noriginalcursummny", "noriginaldiscountmny", "ntaxrate" };

    String[] sNoEditItem = { "GGXX", "ct_code", "ctinvclass", "cpackunitname", "cunitname", "cquoteunit", "cconsigncorp", "cadvisecalbody", "cbodywarehousename", "cbomordercode", "cprojectphasename", "creceiptareaname", "boosflag", "bsupplyflag", "cbatchid", "ccurrencytypename", "isappendant", "crecwarehouse", "bdericttrans", "creccalbody", "ntotalshouldoutnum", "ntotalreceiptnumber", "ntotalinvoicenumber", "ntotalinventorynumber", "ntotalbalancenumber", "ntotalcostmny", "bifreceiptfinish", "bifinventoryfinish", "frowstatus", "ntalplconsigmny", "ntaltransnum", "ntaltransmny", "ntaloutmny", "ntalsignmny", "ntalbalancemny", "ntaltransretnum", "dlastconsigdate", "dlastoutdate", "dlastinvoicedt", "dlastpaydate", "cinvspec", "cinvtype", "narrangescornum", "narrangepoapplynum", "narrangetoornum", "norrangetoapplynum", "narrangemonum", "ntotlbalcostnum", "carrangepersonid" };

    getBillCardTools().setBodyItemEnable(keybodys, true);
    getBillCardTools().setBodyItemEnable(new String[] { "norgqttaxprc", "noriginalcurtaxprice", "norgqttaxnetprc", "noriginalcurtaxnetprice", "norgqtprc", "norginalcurprice", "norgqtnetprc", "noriginalcurnetprice", "npacknumber", "nquoteunitnum", "veditreason" }, true);

    getBillCardTools().setBodyItemEnable(sNoEditItem, false);

    boolean beditaddr = false;
    String[] keybodyfields = { "dconsigndate", "tconsigntime", "creceiptcorpname", "creceiptareaname", "vreceiveaddress", "crecaddrnodename" };

    for (int i = 0; i < getBillCardPanel().getRowCount(); i++)
    {
      getBillCardPanel().getBillModel().setCellEditable(i, "veditreason", true);

      nnumber = getBillCardTools().getBodyUFDoubleValue(i, "nnumber");
      nnumber = nnumber == null ? SoVoConst.duf0 : nnumber;

      ntotalinvoicenumber = getBillCardTools().getBodyUFDoubleValue(i, "ntotalinvoicenumber");

      ntotalinvoicenumber = ntotalinvoicenumber == null ? SoVoConst.duf0 : ntotalinvoicenumber;

      bifinvoicefinish = getBillCardTools().getBodyUFBooleanValue(i, "bifinvoicefinish");

      bifinvoicefinish = bifinvoicefinish == null ? SoVoConst.buffalse : bifinvoicefinish;

      ntotalbalancenumber = getBillCardTools().getBodyUFDoubleValue(i, "ntotalbalancenumber");

      ntotalbalancenumber = ntotalbalancenumber == null ? SoVoConst.duf0 : ntotalbalancenumber;

      totalreceiptnumber = getBillCardTools().getBodyUFDoubleValue(i, "ntotalreceiptnumber");

      totalreceiptnumber = totalreceiptnumber == null ? SoVoConst.duf0 : totalreceiptnumber;

      if ((nnumber.doubleValue() != 0.0D) && (totalreceiptnumber.compareTo(SoVoConst.duf0) == 0) && (!bedit_bdeliver_h))
      {
        bedit_bdeliver_h = true;
      }

      ntotalinventorynumber = getBillCardTools().getBodyUFDoubleValue(i, "ntotalinventorynumber");

      ntotalinventorynumber = ntotalinventorynumber == null ? SoVoConst.duf0 : ntotalinventorynumber;

      if ((nnumber.doubleValue() != 0.0D) && (totalreceiptnumber.doubleValue() == 0.0D) && (ntotalinventorynumber.doubleValue() == 0.0D) && (!beditaddr))
      {
        beditaddr = true;
      }

      ntalbalancemny = getBillCardTools().getBodyUFDoubleValue(i, "ntalbalancemny");

      ntalbalancemny = ntalbalancemny == null ? SoVoConst.duf0 : ntalbalancemny;

      nsummny = getBillCardTools().getBodyUFDoubleValue(i, "nsummny");
      nsummny = nsummny == null ? SoVoConst.duf0 : nsummny;

      cpackunitid = getBillCardTools().getBodyStringValue(i, "cpackunitid");

      if (((nnumber.doubleValue() != 0.0D) && (ntotalinvoicenumber.abs().compareTo(nnumber.abs()) >= 0)) || (bifinvoicefinish.booleanValue()) || ((nnumber.doubleValue() != 0.0D) && (ntotalbalancenumber.abs().compareTo(nnumber.abs()) >= 0)) || ((ntalbalancemny.doubleValue() != 0.0D) && (ntalbalancemny.abs().compareTo(nsummny.abs()) >= 0)))
      {
        getBillCardTools().setRowEnable(i, false);
      }
      else if ((!ispay) && (ntotalinvoicenumber.compareTo(SoVoConst.duf0) == 0) && (ntotalbalancenumber.compareTo(SoVoConst.duf0) == 0) && (ntalbalancemny.compareTo(SoVoConst.duf0) == 0))
      {
        if (!bedit_ccurrencytypeid_h)
          bedit_ccurrencytypeid_h = true;
        if (!bedit_nexchangeotobrate_h)
          bedit_nexchangeotobrate_h = true;
        if (!bedit_nexchangeotoarate_h) {
          bedit_nexchangeotoarate_h = true;
        }
        if (!bedit_creceiptcorpid_h) {
          bedit_creceiptcorpid_h = true;
        }
        if (!bedit_creceiptcustomerid_h) {
          bedit_creceiptcustomerid_h = true;
        }
        if (!bedit_vreceiveaddress_h) {
          bedit_vreceiveaddress_h = true;
        }
        if (!bedit_vnote_h) {
          bedit_vreceiveaddress_h = true;
        }
        if (!bedit_vdef_h) {
          bedit_vdef_h = true;
        }

        if (this.SA_02.booleanValue())
        {
          if (getBillCardPanel().getBodyItem("norgqttaxprc").isShow()) {
            getBillCardPanel().getBillModel().setCellEditable(i, "norgqttaxprc", true);
          }
          else {
            getBillCardPanel().getBillModel().setCellEditable(i, "noriginalcurtaxprice", true);
          }

          if (getBillCardPanel().getBodyItem("norgqttaxnetprc").isShow())
          {
            getBillCardPanel().getBillModel().setCellEditable(i, "norgqttaxnetprc", true);
          }
          else {
            getBillCardPanel().getBillModel().setCellEditable(i, "noriginalcurtaxnetprice", true);
          }
        }
        else
        {
          if (getBillCardPanel().getBodyItem("norgqtprc").isShow()) {
            getBillCardPanel().getBillModel().setCellEditable(i, "norgqtprc", true);
          }
          else {
            getBillCardPanel().getBillModel().setCellEditable(i, "norginalcurprice", true);
          }

          if (getBillCardPanel().getBodyItem("norgqtnetprc").isShow()) {
            getBillCardPanel().getBillModel().setCellEditable(i, "norgqtnetprc", true);
          }
          else {
            getBillCardPanel().getBillModel().setCellEditable(i, "noriginalcurnetprice", true);
          }
        }

        if (SoVoTools.isEmptyString(cpackunitid)) {
          getBillCardPanel().getBillModel().setCellEditable(i, "npacknumber", false);
        }
        else {
          getBillCardPanel().getBillModel().setCellEditable(i, "npacknumber", true);
        }
        getBillCardTools().setRowEnable(i, keybodys, true);

        if (getBillCardPanel().getBodyItem("nquoteunitnum").isShow()) {
          getBillCardPanel().getBillModel().setCellEditable(i, "nquoteunitnum", true);

          getBillCardPanel().getBillModel().setCellEditable(i, "nnumber", false);
        }
        else {
          getBillCardPanel().getBillModel().setCellEditable(i, "nquoteunitnum", false);

          getBillCardPanel().getBillModel().setCellEditable(i, "nnumber", true);
        }

      }
      else if ((ispay) || (ntotalinvoicenumber.compareTo(SoVoConst.duf0) != 0) || (ntotalbalancenumber.compareTo(SoVoConst.duf0) != 0) || (ntalbalancemny.compareTo(SoVoConst.duf0) != 0))
      {
        if (!bedit_creceiptcorpid_h) {
          bedit_creceiptcorpid_h = true;
        }
        if (!bedit_creceiptcustomerid_h) {
          bedit_creceiptcustomerid_h = true;
        }
        if (!bedit_vreceiveaddress_h) {
          bedit_vreceiveaddress_h = true;
        }
        if (!bedit_vnote_h) {
          bedit_vreceiveaddress_h = true;
        }
        if (!bedit_vdef_h) {
          bedit_vdef_h = true;
        }
        if (this.SA_02.booleanValue())
        {
          if (getBillCardPanel().getBodyItem("norgqttaxprc").isShow()) {
            getBillCardPanel().getBillModel().setCellEditable(i, "norgqttaxprc", true);
          }
          else {
            getBillCardPanel().getBillModel().setCellEditable(i, "noriginalcurtaxprice", true);
          }

          if (getBillCardPanel().getBodyItem("norgqttaxnetprc").isShow())
          {
            getBillCardPanel().getBillModel().setCellEditable(i, "norgqttaxnetprc", true);
          }
          else {
            getBillCardPanel().getBillModel().setCellEditable(i, "noriginalcurtaxnetprice", true);
          }
        }
        else
        {
          if (getBillCardPanel().getBodyItem("norgqtprc").isShow()) {
            getBillCardPanel().getBillModel().setCellEditable(i, "norgqtprc", true);
          }
          else {
            getBillCardPanel().getBillModel().setCellEditable(i, "norginalcurprice", true);
          }

          if (getBillCardPanel().getBodyItem("norgqtnetprc").isShow()) {
            getBillCardPanel().getBillModel().setCellEditable(i, "norgqtnetprc", true);
          }
          else {
            getBillCardPanel().getBillModel().setCellEditable(i, "noriginalcurnetprice", true);
          }

        }

        if (SoVoTools.isEmptyString(cpackunitid)) {
          getBillCardPanel().getBillModel().setCellEditable(i, "npacknumber", false);
        }
        else {
          getBillCardPanel().getBillModel().setCellEditable(i, "npacknumber", true);
        }
        getBillCardTools().setRowEnable(i, keybodys, true);

        if (getBillCardPanel().getBodyItem("nquoteunitnum").isShow()) {
          getBillCardPanel().getBillModel().setCellEditable(i, "nquoteunitnum", true);

          getBillCardPanel().getBillModel().setCellEditable(i, "nnumber", false);
        }
        else {
          getBillCardPanel().getBillModel().setCellEditable(i, "nquoteunitnum", false);

          getBillCardPanel().getBillModel().setCellEditable(i, "nnumber", true);
        }

      }

      getBillCardTools().setRowEnable(i, keybodyfields, beditaddr);
      getBillCardTools().setRowEnable(i, sNoEditItem, false);
    }

    getBillCardPanel().getHeadItem("ccurrencytypeid").setEnabled(bedit_ccurrencytypeid_h);

    getBillCardPanel().getHeadItem("ccurrencytypeid").setEdit(bedit_ccurrencytypeid_h);

    getBillCardPanel().getHeadItem("nexchangeotobrate").setEnabled(bedit_nexchangeotobrate_h);

    getBillCardPanel().getHeadItem("nexchangeotobrate").setEdit(bedit_nexchangeotobrate_h);

    getBillCardPanel().getHeadItem("nexchangeotoarate").setEnabled(bedit_nexchangeotoarate_h);

    getBillCardPanel().getHeadItem("nexchangeotoarate").setEdit(bedit_nexchangeotoarate_h);

    getBillCardPanel().getHeadItem("bdeliver").setEnabled(false);
    getBillCardPanel().getHeadItem("bdeliver").setEdit(false);

    getBillCardPanel().getHeadItem("creceiptcorpid").setEnabled(bedit_creceiptcorpid_h);

    getBillCardPanel().getHeadItem("creceiptcorpid").setEdit(bedit_creceiptcorpid_h);

    getBillCardPanel().getHeadItem("creceiptcustomerid").setEnabled(bedit_creceiptcustomerid_h);

    getBillCardPanel().getHeadItem("creceiptcustomerid").setEdit(bedit_creceiptcustomerid_h);

    getBillCardPanel().getHeadItem("vreceiveaddress").setEnabled(bedit_vreceiveaddress_h);

    getBillCardPanel().getHeadItem("vreceiveaddress").setEdit(bedit_vreceiveaddress_h);

    getBillCardPanel().getHeadItem("vnote").setEnabled(bedit_vnote_h);
    getBillCardPanel().getHeadItem("vnote").setEdit(bedit_vnote_h);

    getBillCardPanel().getHeadItem("creceiptcustomerid").setEnabled(beditaddr);

    getBillCardPanel().getHeadItem("creceiptcustomerid").setEdit(beditaddr);

    getBillCardPanel().getHeadItem("vreceiveaddress").setEnabled(beditaddr);
    getBillCardPanel().getHeadItem("vreceiveaddress").setEdit(beditaddr);

    String[] keysh = { "vdef1", "vdef2", "vdef3", "vdef4", "vdef5", "vdef6", "vdef7", "vdef8", "vdef9", "vdef10", "vdef11", "vdef12", "vdef13", "vdef14", "vdef15", "vdef16", "vdef17", "vdef18", "vdef19", "vdef20" };

    getBillCardTools().setHeadEnable(keysh, bedit_vdef_h);

    getBillCardPanel().showBodyTableCol("veditreason");
    getBillCardPanel().getBodyItem("veditreason").setEnabled(true);
  }

  private void setNotReviseItems()
  {
    BillItem[] bHeaditems = getBillCardPanel().getHeadItems();

    for (int i = 0; i < bHeaditems.length; i++)
    {
      if (!bHeaditems[i].isM_bReviseFlag())
        bHeaditems[i].setEnabled(false);
      else if ((bHeaditems[i].getKey() != null) && (bHeaditems[i].getKey().startsWith("vdef")))
      {
        bHeaditems[i].setEnabled(true);
      }
    }

    BillItem[] bBodyitems = getBillCardPanel().getBodyItems();
    for (int i = 0; i < bBodyitems.length; i++)
      if (!bBodyitems[i].isM_bReviseFlag()) {
        int j = 0; for (int jLen = getBillCardPanel().getRowCount(); j < jLen; j++) {
          getBillCardPanel().setCellEditable(j, bBodyitems[i].getKey(), false);
        }

      }
      else if (bBodyitems[i].getKey().startsWith("vdef")) {
        int j = 0; for (int jLen = getBillCardPanel().getRowCount(); j < jLen; j++)
          getBillCardPanel().setCellEditable(j, bBodyitems[i].getKey(), true);
      }
  }

  protected void onNew(ButtonObject bo)
  {
    if (this.strShowState.equals("列表")) {
      this.strShowState = "卡片";
      this.strState = "自由";
      switchInterface();
      setButtons(getBillButtons());
      showCustManArInfo();
      updateUI();
    }

    PfUtilClient.childButtonClicked(bo, getCorpPrimaryKey(), getNodeCode(), getClientEnvironment().getUser().getPrimaryKey(), getBillType(), this);

    this.vRowATPStatus = new Vector();

    boolean bisCalculate = getBillCardPanel().getBillModel().isNeedCalculate();

    getBillCardPanel().getBillModel().setNeedCalculate(false);
    try
    {
      if (PfUtilClient.makeFlag) {
        onNewBySelf();
      }
      else if (PfUtilClient.isCloseOK()) {
        this.binitOnNewByOther = true;

        onNewByOther(PfUtilClient.getRetVos());

        for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
          this.vRowATPStatus.add(new UFBoolean(false));
        }
      }
      getBillCardPanel().showCustManArInfo();
      try
      {
        InvAttrCellRenderer ficr = new InvAttrCellRenderer();
        ficr.setFreeItemRenderer(getBillCardPanel(), getBillCardPanel().alInvs);
      }
      catch (Exception e) {
      }
    }
    finally {
      getBillCardPanel().getBillModel().setNeedCalculate(bisCalculate);
      if (bisCalculate)
        getBillCardPanel().getBillModel().reCalcurateAll();
      this.binitOnNewByOther = false;
    }

    getBillCardTools().clearCatheData();
  }

  protected void onNewByOther(AggregatedValueObject[] saleVOs)
  {
    if ((saleVOs == null) || (saleVOs.length == 0)) {
      return;
    }

    if (!checkSourceComb(saleVOs)) {
      return;
    }
    long s = System.currentTimeMillis();
    SCMEnv.out("onNewByOther1开始...");

    SaleOrderVO saleVO = new SaleOrderVO();
    saleVO.setParentVO(saleVOs[0].getParentVO());

    saleVO.getHeadVO().setCapproveid(null);

    Vector vecTemp = new Vector();
    for (int i = 0; i < saleVOs.length; i++) {
      for (int j = 0; j < saleVOs[i].getChildrenVO().length; j++) {
        vecTemp.addElement(saleVOs[i].getChildrenVO()[j]);
      }
    }
    SaleorderBVO[] aryChildren = new SaleorderBVO[vecTemp.size()];
    if (vecTemp.size() > 0)
      vecTemp.copyInto(aryChildren);
    saleVO.setChildrenVO(aryChildren);

    this.strState = "新增";
    getBillCardPanel().addNew();
    getBillCardPanel().setEnabled(true);

    getBillCardTools().setHeadRefLimit(this.strState);

    Object oCurrency = saleVO.getChildrenVO()[0].getAttributeValue("ccurrencytypeid");
    try
    {
      if ((oCurrency == null) || (oCurrency.toString().length() == 0)) {
        for (int i = 0; i < saleVO.getChildrenVO().length; i++) {
          saleVO.getChildrenVO()[i].setAttributeValue("ccurrencytypeid", this.currtype.getLocalCurrPK());
        }
      }
    }
    catch (Exception e)
    {
      SCMEnv.out(e);
    }
    oCurrency = saleVO.getChildrenVO()[0].getAttributeValue("ccurrencytypeid");

    if ((oCurrency != null) && (oCurrency.toString().length() != 0)) {
      getBillCardPanel().setPanelByCurrency(oCurrency.toString());
    }

    BillRowNo.setVORowNoByRule(saleVO, "30", getBillCardPanel().getRowNoItemKey());

    getBillCardPanel().setBillValueVO(saleVO);

    BillItem[] bms = getBillCardPanel().getBillModel().getBodyItems();
    for (int i = 0; i < bms.length; i++) {
      if ((bms[i].getLoadFormula() != null) && (bms[i].getLoadFormula().length > 0))
      {
        getBillCardPanel().getBillModel().execLoadFormulasByKey(bms[i].getKey());
      }

    }

    ArrayList formulaslist = new ArrayList();

    String creceiptcorpid = getBillCardPanel().getHeadItem("creceiptcorpid").getValue();

    if ((creceiptcorpid == null) || (creceiptcorpid.trim().length() <= 0)) {
      formulaslist.add("creceiptcorpid->getColValue(bd_cumandoc,pk_cusmandoc2,pk_cumandoc,ccustomerid)");
    }

    String creceiptcustomerid = getBillCardPanel().getHeadItem("creceiptcustomerid") == null ? null : getBillCardPanel().getHeadItem("creceiptcustomerid").getValue();

    if ((creceiptcustomerid == null) || (creceiptcustomerid.trim().length() <= 0))
    {
      formulaslist.add("creceiptcustomerid->getColValue(bd_cumandoc,pk_cusmandoc3,pk_cumandoc,ccustomerid)");
    }

    String cdeptid = getBillCardPanel().getHeadItem("cdeptid").getValue();
    if ((cdeptid == null) || (cdeptid.trim().length() <= 0)) {
      formulaslist.add("cdeptid->getColValue(bd_cumandoc,pk_respdept1,pk_cumandoc,ccustomerid)");
    }

    String cemployeeid = getBillCardPanel().getHeadItem("cemployeeid").getValue();

    if ((cemployeeid == null) || (cemployeeid.trim().length() <= 0)) {
      formulaslist.add("cemployeeid->getColValue(bd_cumandoc,pk_resppsn1,pk_cumandoc,ccustomerid)");
    }

    UFDouble ndiscountrate = getBillCardTools().getHeadUFDoubleValue("ndiscountrate");

    if (ndiscountrate == null) {
      formulaslist.add("ndiscountrate->getColValue(bd_cumandoc,discountrate,pk_cumandoc,ccustomerid)");
    }

    UFBoolean bdeliver = getBillCardTools().getHeadUFBooleanValue("bdeliver");

    if ((bdeliver == null) || ((!"38".equals(getSouceBillType())) && (!"3B".equals(getSouceBillType()))))
    {
      String ctransmodeid = getBillCardPanel().getHeadItem("ctransmodeid").getValue();

      if ((ctransmodeid == null) || (ctransmodeid.trim().length() <= 0)) {
        formulaslist.add("ctransmodeid->getColValue(bd_cumandoc,pk_sendtype,pk_cumandoc,ccustomerid)");
      }
    }

    String ccurrencytypeid = getBillCardPanel().getHeadItem("ccurrencytypeid").getValue();

    if ((ccurrencytypeid == null) || (ccurrencytypeid.trim().length() <= 0)) {
      formulaslist.add("ccurrencytypeid->getColValue(bd_cumandoc,pk_currtype1,pk_cumandoc,ccustomerid)");
    }

    String ctermprotocolid = getBillCardPanel().getHeadItem("ctermprotocolid").getValue();

    if ((ctermprotocolid == null) || (ctermprotocolid.trim().length() <= 0)) {
      formulaslist.add("ctermprotocolid->getColValue(bd_cumandoc,pk_payterm,pk_cumandoc,ccustomerid)");
    }

    String ccalbodyid = getBillCardPanel().getHeadItem("ccalbodyid").getValue();

    if ((ccalbodyid == null) || (ccalbodyid.trim().length() <= 0)) {
      formulaslist.add("ccalbodyid->getColValue(bd_cumandoc,pk_calbody,pk_cumandoc,ccustomerid)");
    }

    String csalecorpid = getBillCardPanel().getHeadItem("csalecorpid").getValue();

    if ((csalecorpid == null) || (csalecorpid.trim().length() <= 0)) {
      formulaslist.add("csalecorpid->getColValue(bd_cumandoc,pk_salestru,pk_cumandoc,ccustomerid)");
    }
    formulaslist.add("naccountperiod->getColValue(bd_cumandoc,acclimit,pk_cumandoc,ccustomerid)");

    String ccustbasid = getBillCardPanel().getHeadItem("ccustbasid").getValue();

    if ((ccustbasid == null) || (ccustbasid.trim().length() <= 0)) {
      formulaslist.add("ccustbasid->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,ccustomerid)");
    }

    UFBoolean bfreecustflag = getBillCardTools().getHeadUFBooleanValue("bfreecustflag");

    if (bfreecustflag == null) {
      formulaslist.add("bfreecustflag->getColValue(bd_cubasdoc,freecustflag,pk_cubasdoc,ccustbasid)");
    }

    if (formulaslist.size() > 0) {
      getBillCardPanel().getBillData().execHeadFormulas((String[])formulaslist.toArray(new String[formulaslist.size()]));
    }

    if ((getBillCardPanel().getHeadItem("creceiptcustomerid") != null) && ((getBillCardPanel().getHeadItem("creceiptcustomerid").getValue() == null) || (getBillCardPanel().getHeadItem("creceiptcustomerid").getValue().length() <= 0)))
    {
      getBillCardPanel().getHeadItem("creceiptcustomerid").setValue(getBillCardPanel().getHeadItem("ccustomerid").getValue());
    }

    if ((getBillCardPanel().getHeadItem("creceiptcorpid").getValue() == null) || (getBillCardPanel().getHeadItem("creceiptcorpid").getValue().length() <= 0))
    {
      getBillCardPanel().getHeadItem("creceiptcorpid").setValue(getBillCardPanel().getHeadItem("ccustomerid").getValue());
    }

    String[] bodyformulas = { "cinvbasdocid->getColValue(bd_invmandoc,pk_invbasdoc,pk_invmandoc,cinventoryid)", "laborflag->getColValue(bd_invbasdoc,laborflag,pk_invbasdoc,cinvbasdocid)", "discountflag->getColValue(bd_invbasdoc,discountflag,pk_invbasdoc,cinvbasdocid)", "cprolineid->getColValue(bd_invbasdoc,pk_prodline,pk_invbasdoc,cinvbasdocid)", "cprolinename->getColValue(bd_prodline,prodlinename,pk_prodline,cprolineid)", "nreturntaxrate->getColValue(bd_invmandoc,expaybacktax,pk_invmandoc,cinventoryid)", "ct_name->getColValue(ct_manage,ct_name,pk_ct_manage,csourcebillid)" };

    getBillCardPanel().getBillModel().execFormulas(bodyformulas);

    getBillCardTools().setDefaultValueByTemplate(0, -1);

    if ((getSouceBillType().equals("Z4")) || (getSouceBillType().equals("Z3")))
    {
      if ((getBillCardPanel().getHeadItem("creceiptcorpid").getValue() == null) || (getBillCardPanel().getHeadItem("creceiptcorpid").getValue().length() <= 0))
      {
        getBillCardPanel().getHeadItem("creceiptcorpid").setValue(getBillCardPanel().getHeadItem("ccustomerid").getValue());
      }

      String[] bodyFormula = new String[1];
      bodyFormula[0] = "ctinvclass->getColValue(bd_invcl,invclassname,pk_invcl,ctinvclassid)";

      getBillCardPanel().getBillModel().execFormulas(bodyFormula);

      getBillCardPanel().afterCcalbodyidEdit(null);
      getBillCardPanel().afterCreceiptcorpEdit(null);
    } else if (getSouceBillType().equals("37"))
    {
      if ((getBillCardPanel().getHeadItem("creceiptcorpid").getValue() == null) || (getBillCardPanel().getHeadItem("creceiptcorpid").getValue().length() <= 0))
      {
        getBillCardPanel().getHeadItem("creceiptcorpid").setValue(getBillCardPanel().getHeadItem("ccustomerid").getValue());
      }

      getBillCardPanel().afterCcalbodyidEdit(null);
      getBillCardPanel().afterCreceiptcorpEdit(null);
    }
    else if ((getSouceBillType().equals("4H")) || (getSouceBillType().equals("42")))
    {
      getBillCardPanel().getHeadItem("ccalbodyid").setEnabled(false);
      getBillCardPanel().getBodyItem("cadvisecalbody").setEnabled(false);
      getBillCardPanel().getBodyItem("cbodywarehousename").setEnabled(false);

      if ((getBillCardPanel().getHeadItem("creceiptcorpid").getValue() == null) || (getBillCardPanel().getHeadItem("creceiptcorpid").getValue().length() <= 0))
      {
        getBillCardPanel().getHeadItem("creceiptcorpid").setValue(getBillCardPanel().getHeadItem("ccustomerid").getValue());
      }

      String[] formulas = { "ctaxitemid->getColValue(bd_invbasdoc,pk_taxitems,pk_invbasdoc,cinvbasdocid)", "ntaxrate->getColValue(bd_taxitems,taxratio,pk_taxitems,ctaxitemid)" };

      getBillCardPanel().getBillModel().execFormulas(formulas);

      getBillCardPanel().setHeadItem("ndiscountrate", new UFDouble(100));
      getBillCardPanel().afterDiscountrateEdit(null);

      for (int i = 0; i < getBillCardPanel().getRowCount(); i++)
      {
        getBillCardPanel().setBodyValueAt(new UFDouble(100), i, "nitemdiscountrate");
      }

    }
    else if (getBillType().equals("30"))
    {
      UIRefPane vreceiveaddress = (UIRefPane)getBillCardPanel().getHeadItem("vreceiveaddress").getComponent();

      if (getBillCardPanel().getHeadItem("creceiptcustomerid") != null) {
        ((CustAddrRefModel)vreceiveaddress.getRefModel()).setCustId(getBillCardPanel().getHeadItem("creceiptcustomerid").getValue());
      }

      vreceiveaddress.setValue((String)saleVO.getParentVO().getAttributeValue("vreceiveaddress"));
    }
    else if (!getSouceBillType().equals("21"))
    {
      if ((getSouceBillType().equals("38")) || (getSouceBillType().equals("3B")))
      {
        SaleorderBVO[] ordbvos = saleVO.getBodyVOs();

        String ccalbodyid_h = null;
        int i = 0; for (int loop = ordbvos.length; i < loop; i++) {
          if ((ccalbodyid_h == null) && (
            (ordbvos[i].getCconsigncorpid() == null) || (ordbvos[i].getCconsigncorpid().equals(ordbvos[i].getPkcorp()))))
          {
            ccalbodyid_h = ordbvos[i].getCadvisecalbodyid();
          }

        }

        String oldcalbodyid = getBillCardPanel().getHeadItem("ccalbodyid").getValue();

        getBillCardPanel().getHeadItem("ccalbodyid").setValue(ccalbodyid_h);
        if (SoVoTools.isEmptyString(getBillCardPanel().getHeadItem("ccalbodyid").getValue()))
        {
          getBillCardPanel().getHeadItem("ccalbodyid").setValue(oldcalbodyid);
        }
      }

    }

    SCMEnv.out("onNewByOther1[共用时" + (System.currentTimeMillis() - s) + "]");

    s = System.currentTimeMillis();
    SCMEnv.out("onNewByOther2开始...");
    setDefaultData(false);
    SCMEnv.out("onNewByOther2[共用时" + (System.currentTimeMillis() - s) + "]");

    s = System.currentTimeMillis();
    SCMEnv.out("onNewByOther3开始...");
    setNoEditItem();
    SCMEnv.out("onNewByOther3[共用时" + (System.currentTimeMillis() - s) + "]");

    if ((bdeliver != null) && (("38".equals(getSouceBillType())) || ("3B".equals(getSouceBillType()))))
    {
      getBillCardTools().setEnableCtransmodeid();
    }
    else getBillCardTools().setBdeliverByCtransmodeid();

    getBillCardPanel().setBodyDefaultDataForAll(0, getBillCardPanel().getRowCount());

    for (int i = 1; i <= 20; i++)
      getBillCardPanel().getHeadItem("pk_defdoc" + i).setValue(saleVO.getHeadVO().getAttributeValue("pk_defdoc" + i));
  }

  protected void onNewBySelf()
  {
    this.strState = "新增";
    getBillCardPanel().addNew();
    getBillCardPanel().setEnabled(true);

    getBillCardTools().setHeadRefLimit(this.strState);

    getBillCardPanel().initFreeItem();
    setDefaultData(true);
    setNoEditItem();

    getBillCardTools().setBdeliverByCtransmodeid();

    addLine();
  }

  protected void onNext()
  {
    if (this.num < this.vIDs.size()) {
      this.num += 1;

      String csaleid = (String)getBillListPanel().getHeadBillModel().getValueAt(this.num, "csaleid");

      SaleOrderVO vo = this.vocache.getSaleOrderVO(csaleid);
      if (vo != null)
      {
        if (vo.getChildrenVO() == null) {
          getBillListPanel().loadBodyData(this.num);
          vo = this.vocache.getSaleOrderVO(csaleid);
        }

        loadCardData(vo);
      } else {
        loadCardData();
        updateCacheVO();
      }
      setButtonsState();
      showCustManArInfo();
    }
  }

  protected void onLast()
  {
    this.num = (this.vIDs.size() - 1);

    String csaleid = (String)getBillListPanel().getHeadBillModel().getValueAt(this.num, "csaleid");

    SaleOrderVO vo = this.vocache.getSaleOrderVO(csaleid);

    if (vo != null)
    {
      if (vo.getChildrenVO() == null) {
        getBillListPanel().loadBodyData(this.num);
        vo = this.vocache.getSaleOrderVO(csaleid);
      }

      loadCardData(vo);
    } else {
      loadCardData();
      updateCacheVO();
    }
    setButtonsState();
    showCustManArInfo();
  }

  protected void onFrist()
  {
    this.num = 0;

    String csaleid = (String)getBillListPanel().getHeadBillModel().getValueAt(this.num, "csaleid");

    SaleOrderVO vo = this.vocache.getSaleOrderVO(csaleid);

    if (vo != null)
    {
      if (vo.getChildrenVO() == null) {
        getBillListPanel().loadBodyData(this.num);
        vo = this.vocache.getSaleOrderVO(csaleid);
      }

      loadCardData(vo);
    } else {
      loadCardData();
      updateCacheVO();
    }
    setButtonsState();
    showCustManArInfo();
  }

  protected void onPre()
  {
    if (this.num > 0) {
      this.num -= 1;

      String csaleid = (String)getBillListPanel().getHeadBillModel().getValueAt(this.num, "csaleid");

      SaleOrderVO vo = this.vocache.getSaleOrderVO(csaleid);

      if (vo != null)
      {
        if (vo.getChildrenVO() == null) {
          getBillListPanel().loadBodyData(this.num);
          vo = this.vocache.getSaleOrderVO(csaleid);
        }

        loadCardData(vo);
      } else {
        loadCardData();
        updateCacheVO();
      }
      setButtonsState();
      showCustManArInfo();
    }
  }

  protected void onOrderQuery()
  {
    getSourceDlg().showModal();
  }

  protected void onPrint(boolean needPreview)
  {
    if (!this.strShowState.equals("列表"))
      onPrintCard(needPreview);
    else
      onPrintListVos(needPreview);
  }

  protected void onPrintCard(boolean needPreview)
  {
    boolean total = getBillCardPanel().getBodyPanel().isTatolRow();
    try {
      getBillCardPanel().getBodyPanel().setTotalRowShow(false);

      SalePubPrintDS ds = new SalePubPrintDS(getNodeCode(), getBillCardPanel());

      PrintEntry print = new PrintEntry(null, null);

      print.setTemplateID(getCorpPrimaryKey(), getNodeCode(), getClientEnvironment().getUser().getPrimaryKey(), null);

      if (print.selectTemplate() < 0) {
        return;
      }
      showHintMessage(PrintLogClient.getBeforePrintMsg(needPreview, false));

      SaleOrderVO saleorder = (SaleOrderVO)getBillCardPanel().getBillValueVO(SaleOrderVO.class.getName(), SaleorderHVO.class.getName(), SaleorderBVO.class.getName());

      print.setPrintListener(getPrintLogClient());
      getPrintLogClient().setPrintEntry(print);
      getPrintLogClient().setPrintInfo(saleorder.getHeadVO().getScmPrintlogVO());

      ds.setPageRows(print.getBreakPos());
      print.setDataSource(ds);
      if (needPreview)
        print.preview();
      else {
        print.print(true);
      }

      showHintMessage(getPrintLogClient().getPrintResultMsg(needPreview));
    } finally {
      getBillCardPanel().getBodyPanel().setTotalRowShow(total);
    }
  }

  protected void onPrintListVos(boolean needPreview)
  {
    ArrayList alvos = new ArrayList();

    int[] selectRows = getBillListPanel().getHeadTable().getSelectedRows();

    SaleorderHVO[] hvos = new SaleorderHVO[selectRows.length];
    int i = 0; for (int loop = selectRows.length; i < loop; i++) {
      hvos[i] = ((SaleorderHVO)getBillListPanel().getHeadBillModel().getBodyValueRowVO(selectRows[i], "nc.vo.so.so001.SaleorderHVO"));
    }

    for (int j = 0; j < selectRows.length; j++) {
      this.num = selectRows[j];

      loadCardData();

      AggregatedValueObject printHvo = getBillCardPanel().getBillValueVO("nc.vo.dm.pub.DMVO", "nc.vo.dm.pub.DMDataVO", "nc.vo.dm.pub.DMDataVO");

      AggregatedValueObject printListHvo = getBillListPanel().getBillValueVO(this.num, "nc.vo.dm.pub.DMVO", "nc.vo.dm.pub.DMDataVO", "nc.vo.dm.pub.DMDataVO");

      DMDataVO header = (DMDataVO)printListHvo.getParentVO();

      header.setAttributeValue("ccustomerid", header.getAttributeValue("ccustomername"));

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

      if (needPreview)
        break;
    }
    if ((null == alvos) || (alvos.size() == 0)) {
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000153"));

      return;
    }

    if (getPrintEntry().selectTemplate() < 0) {
      return;
    }
    showHintMessage(PrintLogClient.getBeforePrintMsg(needPreview, true));

    if (needPreview)
    {
      getPrintLogClient().setPrintEntry(getPrintEntry());
      getPrintLogClient().setPrintInfo(hvos[0].getScmPrintlogVO());
      getPrintEntry().setPrintListener(getPrintLogClient());

      ArrayList prlistvo = new ArrayList();
      SaleOrderPrintDataInterface prds = getDataSource();
      prlistvo.add(alvos.get(0));
      prds.setListVOs(prlistvo);
      prds.setTotalLinesInOnePage(getPrintEntry().getBreakPos());

      getPrintEntry().setDataSource(prds);

      getPrintEntry().preview();
    }
    else {
      getPrintLogClient().setPrintEntry(getPrintEntry());

      getPrintEntry().beginBatchPrint();
      SaleOrderPrintDataInterface prds = null;
      ArrayList prlistvo = null;
      getPrintLogClient().setBatchPrint(true);
      getPrintEntry().setPrintListener(getPrintLogClient());
      int k = 0; for (int loop = hvos.length; k < loop; k++)
      {
        getPrintLogClient().setPrintInfo(hvos[k].getScmPrintlogVO());

        if (getPrintLogClient().check())
        {
          prds = getDataSource();
          prlistvo = new ArrayList();
          prlistvo.add(alvos.get(k));
          prds.setListVOs(prlistvo);
          prds.setTotalLinesInOnePage(getPrintEntry().getBreakPos());

          getPrintEntry().setDataSource(prds);
        }

      }

      getPrintEntry().endBatchPrint();
    }

    showHintMessage(getPrintLogClient().getPrintResultMsg(needPreview));
  }

  protected PrintEntry getPrintEntry() {
    if (null == this.m_print) {
      this.m_print = new PrintEntry(null, null);
      this.m_print.setTemplateID(getClientEnvironment().getCorporation().getPk_corp(), getNodeCode(), getClientEnvironment().getUser().getPrimaryKey(), null);
    }

    return this.m_print;
  }

  protected SaleOrderPrintDataInterface getDataSource()
  {
    if (this.m_dataSource == null) {
      this.m_dataSource = new SaleOrderPrintDataInterface();
    }

    this.m_dataSource.setBillData(getBillCardPanel().getBillData());
    this.m_dataSource.setModuleName(getNodeCode());
    this.m_dataSource.setTotalLinesInOnePage(getPrintEntry().getBreakPos());

    return this.m_dataSource;
  }

  protected void onQuery()
  {
    if (getQueryDlg().showModal() == 2) {
      return;
    }
    onRefresh();
  }

  protected void onRefresh()
  {
    ConditionVO[] voCondition = getQueryDlg().getConditionVO();

    if (voCondition != null) {
      for (int i = 0; i < voCondition.length; i++) {
        if ((voCondition[i].getTableNameForMultiTable() != null) && (voCondition[i].getTableNameForMultiTable().equals("bd_cumandoc")) && (voCondition[i].getTableCodeForMultiTable().indexOf("def") >= 0))
        {
          voCondition[i].setTableCode(voCondition[i].getTableName() + "." + voCondition[i].getTableCodeForMultiTable());

          voCondition[i].setFieldCode("ccustomerid");
        }

      }

    }

    try
    {
      ConditionVO[] newvo = ScmPubHelper.getTotalSubPkVOs(voCondition, getCorpPrimaryKey());

      voCondition = newvo;
    } catch (Exception e) {
      System.out.print("包含下级部门构件出错! \n");
      SCMEnv.out(e.getMessage());
    }

    ArrayList al = new ArrayList();
    ArrayList qualified = new ArrayList();
    int i = 0; for (int len = voCondition.length; i < len; i++)
    {
      if (((!voCondition[i].getOperaCode().trim().equalsIgnoreCase("IS")) || (!voCondition[i].getValue().trim().equalsIgnoreCase("NULL"))) && (voCondition[i].getValue().trim().indexOf("(select distinct power.resource_data_id") < 0))
      {
        al.add(voCondition[i]);
      }
      else qualified.add(voCondition[i]);
    }

    ConditionVO[] normals = new ConditionVO[al.size()];
    al.toArray(normals);

    normals = ConvertQueryCondition.getConvertedVO(normals, getCorpPrimaryKey());

    int m = 0; for (int len = normals.length; m < len; m++) {
      voCondition[m] = normals[m];
    }
    int x = 0; for (int len = qualified.size(); x < len; x++) {
      voCondition[(x + normals.length)] = ((ConditionVO)qualified.get(x));
    }

    for (int a = 0; a < voCondition.length; a++) {
      if ((voCondition[a].getFieldCode().equals("so_sale.vreceiptcode")) && (voCondition[a].getOperaCode().equals("like")) && (!voCondition[a].getValue().endsWith("%")))
      {
        voCondition[a].setValue("%" + voCondition[a].getValue() + "%");
        break;
      }
    }

    String tablename = null;
    if (getBillType().equals("30"))
      tablename = "so_sale";
    else {
      tablename = "so_salereceipt";
    }
    StringBuffer strWhere = new StringBuffer();
    strWhere.append(tablename + ".pk_corp = '" + getCorpPrimaryKey() + "' ");

    if (!getBillType().equals("30")) {
      strWhere.append(" AND " + tablename + ".cbiztype = '" + this.boBusiType.getTag() + "' ");
    }

    strWhere.append(" AND " + tablename + ".dr=0 ");

    if (getBillType().equals("30"))
      strWhere.append(" AND " + tablename + ".creceipttype = '30' ");
    else {
      strWhere.append(" AND " + tablename + ".creceipttype = '31' ");
    }

    if (this.m_rdoAll.isSelected()) {
      strWhere.append(" and " + tablename + ".fstatus != " + 5);
    }
    else if (this.m_rdoFree.isSelected()) {
      strWhere.append(" and " + tablename + ".fstatus = " + 1);
    }
    else if (this.m_rdoAudited.isSelected()) {
      strWhere.append(" and " + tablename + ".fstatus = " + 2);
    }
    else if (this.m_rdoBlankOut.isSelected()) {
      strWhere.append(" and " + tablename + ".fstatus = " + 5);
    }
    else if (this.m_rdoFreeze.isSelected()) {
      strWhere.append(" and " + tablename + ".fstatus = " + 3);
    }
    else if (this.m_rdoAuditing.isSelected()) {
      strWhere.append(" and " + tablename + ".fstatus = " + 7);
    }

    if (getBillType().equals("30")) {
      if (this.m_rdoBatch.isSelected())
        strWhere.append(" and " + tablename + ".bretinvflag = 'Y'");
      else {
        strWhere.append(" and " + tablename + ".bretinvflag = 'N'");
      }
    }
    if ((getQueryDlg().getWhereSQL(voCondition) == null) || (getQueryDlg().getWhereSQL(voCondition).length() == 0))
    {
      getBillListPanel().setWhere(strWhere.toString());
    }
    else getBillListPanel().setWhere(strWhere + " AND (" + getQueryDlg().getWhereSQL(voCondition) + ") ");

    setBtnsByBillState(-1);

    getBillListPanel().reLoadData();

    fillCacheByListPanel();
    this.selectRow = -1;

    initIDs();

    if (this.strShowState.equals("卡片"))
    {
      this.selectRow = 0;
      String csaleid = (String)getBillListPanel().getHeadBillModel().getValueAt(this.selectRow, "csaleid");

      SaleOrderVO vo = this.vocache.getSaleOrderVO(csaleid);

      if ((vo == null) || (vo.getParentVO() == null)) {
        this.num = -1;
      } else {
        getBillListPanel().loadBodyData(this.selectRow);

        this.strState = "自由";
        this.num = 0;

        loadCardData(this.vocache.getSaleOrderVO(csaleid));
      }

      showCustManArInfo();
      updateUI();
    }

    this.b_query = true;
    setButtonsState();
  }

  private void fillCacheByListPanel()
  {
    if (getBillListPanel().getHeadBillModel().getRowCount() <= 0) {
      return;
    }
    SaleorderHVO[] hvos = (SaleorderHVO[])getBillListPanel().getHeadBillModel().getBodyValueVOs("nc.vo.so.so001.SaleorderHVO");
    try
    {
      nc.ui.so.so001.panel.bom.BillTools.getMnyByCurrencyFromModel(getBillListPanel().getHeadBillModel(), hvos, getCorpPrimaryKey(), "ccurrencytypeid", new String[] { "npreceivemny", "nreceiptcathmny" });
    }
    catch (Exception e)
    {
      handleException(e);
    }
  }

  protected void onRefundment()
  {
    SaleOrderVO saleorderVO = (SaleOrderVO)getVO(false);

    this.strUISource = this.strShowState;

    this.strShowState = "退货";
    this.strState = "退货";

    getBillCardTools().setHeadRefLimit(this.strState);

    switchInterface();
    try
    {
      this.num = getBillListPanel().getHeadTable().getSelectedRow();
      long s = System.currentTimeMillis();

      getBillCardTools().setOldsaleordervo(saleorderVO);
      String csaleid = saleorderVO.getHeadVO().getCsaleid();

      saleorderVO.getHeadVO().setBretinvflag(new UFBoolean(true));
      saleorderVO.getHeadVO().setDbilldate(getClientEnvironment().getDate());

      saleorderVO.getHeadVO().setFstatus(new Integer(1));
      saleorderVO.getHeadVO().setVreceiptcode(null);
      saleorderVO.getHeadVO().setCapproveid(null);
      saleorderVO.getHeadVO().setDapprovedate(null);
      saleorderVO.getHeadVO().setBinvoicendflag(null);
      saleorderVO.getHeadVO().setBoutendflag(null);
      saleorderVO.getHeadVO().setBreceiptendflag(null);
      saleorderVO.getHeadVO().setBpayendflag(null);
      saleorderVO.getHeadVO().setPrimaryKey(null);
      saleorderVO.getHeadVO().setTs(null);

      saleorderVO.getHeadVO().setNpreceivemny(null);
      saleorderVO.getHeadVO().setNpreceiverate(null);
      saleorderVO.getHeadVO().setNreceiptcathmny(null);
      saleorderVO.getHeadVO().setBoverdate(null);
      saleorderVO.getHeadVO().setBtransendflag(null);

      String[] keys = { "tconsigntime", "tdelivertime", "ntaldcnum", "nasttaldcnum", "ntaldcmny", "nretprofnum", "nastretprofnum", "nretprofmny", "natp", "ntalplconsigmny", "ntaltransnum", "ntaltransmny", "ntaloutmny", "ntalsignmny", "ntalbalancemny", "ntaltransretnum", "ntranslossnum", "biftransfinish", "dlastconsigdate", "dlasttransdate", "dlastoutdate", "dlastinvoicedt", "dlastpaydate", "narrangescornum", "narrangepoapplynum", "narrangetoornum", "norrangetoapplynum", "barrangedflag", "carrangepersonid", "tlastarrangetime", "narrangemonum", "ntotalshouldoutnum" };

      for (int i = 0; i < saleorderVO.getBodyVOs().length; i++)
      {
        saleorderVO.getBodyVOs()[i].setCsourcebillid(csaleid);
        saleorderVO.getBodyVOs()[i].setCsourcebillbodyid(saleorderVO.getBodyVOs()[i].getCorder_bid());

        saleorderVO.getBodyVOs()[i].setCreceipttype("30");

        saleorderVO.getBodyVOs()[i].setPrimaryKey(null);
        saleorderVO.getBodyVOs()[i].setFrowstatus(new Integer(1));

        saleorderVO.getBodyVOs()[i].setBifinventoryfinish(null);
        saleorderVO.getBodyVOs()[i].setBifinvoicefinish(null);
        saleorderVO.getBodyVOs()[i].setBifpayfinish(null);
        saleorderVO.getBodyVOs()[i].setBifpaybalance(null);
        saleorderVO.getBodyVOs()[i].setBifpaysign(null);
        saleorderVO.getBodyVOs()[i].setBifreceiptfinish(null);

        saleorderVO.getBodyVOs()[i].setNtotalbalancenumber(null);
        saleorderVO.getBodyVOs()[i].setNtotalcostmny(null);
        saleorderVO.getBodyVOs()[i].setNtotalinventorynumber(null);
        saleorderVO.getBodyVOs()[i].setNtotalinvoicenumber(null);
        saleorderVO.getBodyVOs()[i].setNtotalinvoicemny(null);
        saleorderVO.getBodyVOs()[i].setNtotalpaymny(null);
        saleorderVO.getBodyVOs()[i].setNtotalreceiptnumber(null);
        saleorderVO.getBodyVOs()[i].setNtotalsignnumber(null);
        saleorderVO.getBodyVOs()[i].setCfreezeid(null);
        saleorderVO.getBodyVOs()[i].setTs(null);
        saleorderVO.getBodyVOs()[i].setBdericttrans(null);
        saleorderVO.getBodyVOs()[i].setCconsigncorp(null);
        saleorderVO.getBodyVOs()[i].setCconsigncorpid(null);
        saleorderVO.getBodyVOs()[i].setCadvisecalbodyid(null);
        saleorderVO.getBodyVOs()[i].setCbodywarehouseid(null);
        saleorderVO.getBodyVOs()[i].setCreccalbody(null);
        saleorderVO.getBodyVOs()[i].setCreccalbodyid(null);
        saleorderVO.getBodyVOs()[i].setCrecwarehouse(null);
        saleorderVO.getBodyVOs()[i].setCrecwareid(null);
        saleorderVO.getBodyVOs()[i].setNtotlbalcostnum(null);

        SoVoTools.setVOValueForOne(saleorderVO.getBodyVOs()[i], keys, null);
      }

      s = System.currentTimeMillis();
      getBillCardPanel().setBillValueVO(saleorderVO);
      SCMEnv.out("数据设置[共用时" + (System.currentTimeMillis() - s) + "]");

      long s1 = System.currentTimeMillis();

      BillItem[] bms = getBillCardPanel().getBillModel().getBodyItems();
      for (int i = 0; i < bms.length; i++) {
        if ((bms[i].getLoadFormula() != null) && (bms[i].getLoadFormula().length > 0))
        {
          getBillCardPanel().getBillModel().execLoadFormulasByKey(bms[i].getKey());
        }
      }

      SCMEnv.out("执行公式[共用时" + (System.currentTimeMillis() - s1) + "]");

      UIRefPane vreceiveaddress = (UIRefPane)getBillCardPanel().getHeadItem("vreceiveaddress").getComponent();

      ((CustAddrRefModel)vreceiveaddress.getRefModel()).setCustId(getBillCardPanel().getHeadItem("creceiptcustomerid").getValue());

      String formula = "getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,creceiptcustomerid)";
      String pk_cubasdoc = (String)getBillCardPanel().execHeadFormula(formula);

      String strvreceiveaddress = nc.ui.so.so001.panel.bom.BillTools.getColValue2("bd_custaddr", "pk_custaddr", "defaddrflag", "Y", "pk_cubasdoc", pk_cubasdoc);

      vreceiveaddress.setPK(strvreceiveaddress);
      getBillCardPanel().initFreeItem();
      setHeadDefaultData();

      if ((getBillCardPanel().getHeadItem("bfreecustflag").getValue() == null) || (getBillCardPanel().getHeadItem("bfreecustflag").getValue().equals("false")))
      {
        getBillCardPanel().getHeadItem("cfreecustid").setEnabled(false);
      }
      else getBillCardPanel().getHeadItem("cfreecustid").setEnabled(true);

      getBillCardPanel().initUnit();

      for (int i = 0; i < getBillCardPanel().getRowCount(); i++)
      {
        getBillCardPanel().setAssistUnit(i);

        if (this.SA_02.booleanValue()) {
          if (getBillCardPanel().getBodyItem("norgqttaxprc").isShow()) {
            getBillCardPanel().getBillModel().setCellEditable(i, "norgqttaxprc", true);
          }
          else {
            getBillCardPanel().getBillModel().setCellEditable(i, "noriginalcurtaxprice", true);
          }
          if (getBillCardPanel().getBodyItem("norgqttaxnetprc").isShow())
          {
            getBillCardPanel().getBillModel().setCellEditable(i, "norgqttaxnetprc", true);
          }
          else {
            getBillCardPanel().getBillModel().setCellEditable(i, "noriginalcurtaxnetprice", true);
          }

          getBillCardPanel().getBillModel().setCellEditable(i, "noriginalcurtaxmny", true);

          getBillCardPanel().getBillModel().setCellEditable(i, "noriginalcursummny", true);

          getBillCardPanel().getBillModel().setCellEditable(i, "noriginaldiscountmny", true);
        }
        else {
          if (getBillCardPanel().getBodyItem("norgqtprc").isShow()) {
            getBillCardPanel().getBillModel().setCellEditable(i, "norgqtprc", true);
          }
          else {
            getBillCardPanel().getBillModel().setCellEditable(i, "norginalcurprice", true);
          }
          if (getBillCardPanel().getBodyItem("norgqtnetprc").isShow()) {
            getBillCardPanel().getBillModel().setCellEditable(i, "norgqtnetprc", true);
          }
          else {
            getBillCardPanel().getBillModel().setCellEditable(i, "noriginalcurnetprice", true);
          }

          getBillCardPanel().getBillModel().setCellEditable(i, "noriginalcurmny", true);

          getBillCardPanel().getBillModel().setCellEditable(i, "noriginalcursummny", true);

          getBillCardPanel().getBillModel().setCellEditable(i, "noriginaldiscountmny", true);
        }

        getBillCardPanel().setCellEditable(i, "ct_name", false);
      }

      UIRefPane cadvisecalbody = (UIRefPane)getBillCardPanel().getBodyItem("cadvisecalbody").getComponent();

      cadvisecalbody.setPK(null);

      getBillCardPanel().updateValue();
      getBillCardPanel().getBillModel().reCalcurateAll();
      getBillCardPanel().setEnabled(true);

      int rowcount = getBillCardPanel().getRowCount();
      for (int i = 0; i < rowcount; i++) {
        getBillCardPanel().setCellEditable(i, "cinventorycode", false);
      }

      getBillCardTools().clearCatheData();

      if ((saleorderVO.getHeadVO().getNaccountperiod() != null) && (saleorderVO.getHeadVO().getNaccountperiod().doubleValue() < 0.0D))
      {
        getBillCardPanel().setHeadItem("naccountperiod", null);
      }
    } catch (Exception e) {
      SCMEnv.out("数据加载失败！");
      SCMEnv.out(e.getMessage());
    }

    getBillCardTools().resumeBillItemEditToInit();

    setButtons();
    setButtonsState();
    setNoEditItem();
    showCustManArInfo();
    try
    {
      InvAttrCellRenderer ficr = new InvAttrCellRenderer();
      ficr.setFreeItemRenderer(getBillCardPanel(), this.alInvs);
    }
    catch (Exception e) {
    }
    updateUI();
  }

  protected void onReturn()
  {
    this.strShowState = "列表";
    switchInterface();

    this.selectRow = -1;
    this.num = -1;

    getBillListPanel().getHeadTable().clearSelection();
    BillModel bmHead = getBillListPanel().getHeadBillModel();
    int i = 0; for (int iRowCount = bmHead.getRowCount(); i < iRowCount; i++) {
      bmHead.setRowState(i, 0);
    }
    getBillListPanel().getBodyBillModel().clearBodyData();

    getBillCardPanel().addNew();
    getBillCardPanel().getHeadItem("vreceiptcode").setValue(null);

    initIDs();

    setButtons(getBillButtons());

    updateUI();
  }

  protected void onBack()
  {
    getBillCardPanel().addNew();
    getBillCardPanel().setEnabled(false);

    if (this.strUISource.equals("列表"))
      onReturn();
    else
      onCard();
  }

  protected boolean onSave()
  {
    if ("新增".equals(this.strState)) {
      onSaveCopyBill();
      return false;
    }
    try
    {
      getBillCardPanel().cleanNullLine();

      if (!BillRowNo.verifyRowNosCorrect(getBillCardPanel(), getRowNoItemKey()))
      {
        showWarningMessage(NCLangRes.getInstance().getStrByID("common", "MC1", null, new String[] { NCLangRes.getInstance().getStrByID("common", "UC000-0003389") }));

        return false;
      }

      SaleOrderVO saleorder = (SaleOrderVO)getBillCardPanel().getBillValueChangeVO(SaleOrderVO.class.getName(), SaleorderHVO.class.getName(), SaleorderBVO.class.getName());

      SaleOrderVO oldsaleorder = (SaleOrderVO)getBillCardPanel().getBillValueVO(SaleOrderVO.class.getName(), SaleorderHVO.class.getName(), SaleorderBVO.class.getName());

      ((SaleorderHVO)saleorder.getParentVO()).setCreceipttype("30");

      if (!calculatePreceive(oldsaleorder)) {
        return false;
      }
      SaleorderBVO[] bvos = saleorder.getBodyVOs();
      if (bvos != null) {
        int i = 0; for (int iLen = bvos.length; i < iLen; i++) {
          if (bvos[i].getBoosflag() == null) {
            bvos[i].setBoosflag(new UFBoolean(false));
          }
        }
      }

      onCheck(oldsaleorder);

      saleorder = getLineNumber(oldsaleorder, saleorder);

      if (!BillRowNo.verifyRowNosCorrect(getBillCardPanel(), getRowNoItemKey()))
      {
        return false;
      }

      ((SaleorderHVO)saleorder.getParentVO()).setPk_corp(getCorpPrimaryKey());

      ((SaleorderHVO)saleorder.getParentVO()).setBcodechanged(this.m_isCodeChanged);

      saleorder.getHeadVO().setVoldreceiptcode(getBillCardTools().getOldsaleordervo() == null ? null : getBillCardTools().getOldsaleordervo().getHeadVO().getVreceiptcode());

      UIRefPane vreceiveaddress = (UIRefPane)getBillCardPanel().getHeadItem("vreceiveaddress").getComponent();

      saleorder.getHeadVO().setVreceiveaddress(vreceiveaddress.getUITextField().getText());

      oldsaleorder.getHeadVO().setVreceiveaddress(vreceiveaddress.getUITextField().getText());

      if (saleorder.getChildrenVO() != null) {
        for (int i = 0; i < saleorder.getChildrenVO().length; i++)
        {
          ((SaleorderBVO)saleorder.getChildrenVO()[i]).setPkcorp(getCorpPrimaryKey());

          if ((this.strState.equals("修订")) && (saleorder.getChildrenVO()[i].getStatus() == 1))
          {
            saleorder.getChildrenVO()[i].setStatus(4);
          }
        }
      }
      if ((this.strState.equals("修改")) || (this.strState.equals("修订"))) {
        saleorder.setStatus(1);
      }

      saleorder.setCuruserid(getClientEnvironment().getUser().getPrimaryKey());

      saleorder.setDcurdate(getClientEnvironment().getDate());
      saleorder.setCusername(getClientEnvironment().getUser().getUserName());

      saleorder.setCcorpname(getClientEnvironment().getCorporation().getUnitname());

      oldsaleorder.setCuruserid(getClientEnvironment().getUser().getPrimaryKey());

      oldsaleorder.setDcurdate(getClientEnvironment().getDate());
      saleorder.setCnodename(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000504"));

      showHintMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000136"));

      SCMEnv.out("开始保存：" + System.currentTimeMillis());

      saleorder.setFirstTime(true);
      boolean bContinue = true;
      boolean bRight = true;
      ArrayList listID = null;
      HashMap reths = null;

      fillDataBeforeSave(saleorder, oldsaleorder);

      ClientLink clientLink = new ClientLink(getClientEnvironment());

      saleorder.setClientLink(clientLink);

      while (bContinue) {
        try {
          if (this.strState.equals("修改"))
          {
            int iStatus = Integer.parseInt(getBillCardPanel().getHeadItem("fstatus").getValue());

            UFDateTime ud = ClientEnvironment.getServerTime();
            saleorder.getHeadVO().setAttributeValue("dmoditime", ud);

            if (iStatus == 8)
            {
              listID = (ArrayList)PfUtilClient.processActionNoSendMessage(this, "SpecialSave", "30", getClientEnvironment().getDate().toString(), oldsaleorder, null, null, null);

              Integer istatus = new Integer(1);
              getBillCardPanel().setHeadItem("fstatus", istatus);
              int i = 0; int loop = getBillCardPanel().getRowCount();
              for (; i < loop; i++) {
                getBillCardPanel().setBodyValueAt(istatus, i, "frowstatus");
              }

            }
            else
            {
              listID = (ArrayList)PfUtilClient.processActionNoSendMessage(this, "PreModify", "30", getClientEnvironment().getDate().toString(), saleorder, null, null, null);
            }

            getBillCardPanel().setTailItem("dmoditime", ud);

            loadIDafterEDIT(listID);
            if (listID != null) {
              reths = (HashMap)listID.get(listID.size() - 1);
            }
          }
          if (this.strState.equals("修订")) {
            UFDateTime ud = ClientEnvironment.getServerTime();
            saleorder.getHeadVO().setAttributeValue("dmoditime", ud);

            listID = (ArrayList)PfUtilClient.processActionNoSendMessage(this, "OrderAlter", "30", getClientEnvironment().getDate().toString(), saleorder, null, null, null);

            if (listID != null) {
              reths = (HashMap)listID.get(listID.size() - 1);
            }

            getBillCardPanel().setTailItem("dmoditime", ud);

            loadCardData();
          }

          bContinue = false;
          bRight = true;
        } catch (Exception ex) {
          bRight = false;
          bContinue = doException(saleorder, oldsaleorder, ex);
        }
      }
      if (!bRight) {
        showHintMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000185"));

        return false;
      }

      SCMEnv.out("保存结束：" + System.currentTimeMillis());
      showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH005"));

      if (!this.strState.equals("退货")) {
        this.strState = "自由";
      }
      getBillCardTools().setHeadRefLimit(this.strState);

      getBillCardTools().reLoadConsignCorpAndCalbody(reths);

      this.vRowATPStatus = new Vector();
      for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
        this.vRowATPStatus.addElement(new UFBoolean(false));
      }

      if (reths != null) {
        getBillCardPanel().setHeadItem("nreceiptcathmny", reths.get("queryCachPayByOrdId"));
      }

      updateCacheVO();

      this.m_isCodeChanged = false;

      showCustManArInfo(reths);

      getBillCardPanel().updateValue();
      try
      {
        InvAttrCellRenderer ficr = new InvAttrCellRenderer();
        ficr.setFreeItemRenderer(getBillCardPanel(), null);
      }
      catch (Exception einv)
      {
      }
      return true;
    }
    catch (AtpSetException atpsetex) {
      showWarningMessage(atpsetex.getMessage());
      getBillCardTools().processAtpSetException(atpsetex.getMessage());
      return false;
    }
    catch (Exception e)
    {
      if ((e.getClass() != AtpCheckException.class) || ((e.getClass() == AtpCheckException.class) && (!"警告".equals(this.SO28))))
      {
        showWarningMessage(e.getMessage());
      }showHintMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000185"));

      markRow(e.getMessage());
      SCMEnv.out(e.getMessage());
    }return false;
  }

  protected void onSaveCopyBill()
  {
    if (!onSaveExt()) {
      return;
    }
    SaleOrderVO oldsaleorder = (SaleOrderVO)getBillCardPanel().getBillValueVO(SaleOrderVO.class.getName(), SaleorderHVO.class.getName(), SaleorderBVO.class.getName());

    if (oldsaleorder != null)
    {
      this.vIDs.add(((SaleorderHVO)oldsaleorder.getParentVO()).getCsaleid());
      this.num = (this.vIDs.size() - 1);
      addCacheVO();
    }

    getBillCardPanel().updateValue();
  }

  private void fillDataBeforeSave(SaleOrderVO saleorder, SaleOrderVO oldsaleorder)
    throws Exception
  {
    if (this.strState.equals("修改")) {
      saleorder.setPrimaryKey(getBillID());

      int iStatus = Integer.parseInt(getBillCardPanel().getHeadItem("fstatus").getValue());

      if (iStatus == 8)
      {
        if ((saleorder != null) && (saleorder.getBodyVOs() != null)) {
          ArrayList dellinelist = new ArrayList();
          int i = 0; for (int loop = saleorder.getBodyVOs().length; i < loop; i++) {
            if (saleorder.getBodyVOs()[i].getStatus() == 3) {
              dellinelist.add(saleorder.getBodyVOs()[i]);
            }
          }
          if (dellinelist.size() > 0) {
            SaleorderBVO[] newbvos = new SaleorderBVO[oldsaleorder.getBodyVOs().length + dellinelist.size()];

            System.arraycopy(oldsaleorder.getBodyVOs(), 0, newbvos, 0, oldsaleorder.getBodyVOs().length);

            int pos = 0;
            int j = oldsaleorder.getBodyVOs().length; 
            for (int loop = newbvos.length; j < loop; j++) {
              newbvos[j] = ((SaleorderBVO)dellinelist.get(pos));
              pos++;
            }
            oldsaleorder.setChildrenVO(newbvos);
          }
        }
        oldsaleorder.setStatus(1);
        oldsaleorder.getParentVO().setStatus(1);
        ((SaleorderHVO)oldsaleorder.getParentVO()).setFstatus(new Integer(1));

        SaleorderBVO[] voBodys = (SaleorderBVO[])oldsaleorder.getChildrenVO();

        for (int i = 0; i < voBodys.length; i++) {
          if (voBodys[i].getStatus() == 0)
            voBodys[i].setStatus(1);
          voBodys[i].setFrowstatus(new Integer(1));

          voBodys[i].setIAction(-1);
        }

        SaleOrderVO oldvo = getBillCardTools().getOldsaleordervo();
        int i = 0; for (int loop = oldvo.getBodyVOs().length; i < loop; i++) {
          oldvo.getBodyVOs()[i].setIAction(-1);
        }

        oldsaleorder.setOldSaleOrderVO(oldvo);

        oldsaleorder.setIAction(10);

        saleorder.setAllSaleOrderVO((SaleOrderVO)getBillCardPanel().getBillValueChangeVO(SaleOrderVO.class.getName(), SaleorderHVO.class.getName(), SaleorderBVO.class.getName()));

        oldsaleorder.processVOForTrans();
      }
      else
      {
        saleorder = remarkOOSLine(saleorder);
        oldsaleorder = remarkOOSLine(oldsaleorder);

        oldsaleorder.setStatus(1);

        saleorder.setOldSaleOrderVO(getBillCardTools().getOldsaleordervo());

        saleorder.setIAction(1);

        saleorder.setAllSaleOrderVO(oldsaleorder);

        saleorder.processVOForTrans();
      }

      saleorder.getHeadVO().setAttributeValue("dmoditime", ClientEnvironment.getServerTime());
    }
    else if (this.strState.equals("修订"))
    {
      saleorder.setPrimaryKey(getBillID());

      oldsaleorder = remarkOOSLine(oldsaleorder);
      oldsaleorder.setStatus(1);

      saleorder.setOldSaleOrderVO(getBillCardTools().getOldsaleordervo());

      saleorder.setIAction(5);

      saleorder.setAllSaleOrderVO(oldsaleorder);

      ClientLink clientLink = new ClientLink(getClientEnvironment());

      saleorder.setClientLink(clientLink);

      SaleorderBVO[] ordbvos = saleorder.getBodyVOs();
      if ((ordbvos != null) && (ordbvos.length > 0)) {
        UFDouble ntotalconvertnum = null;
        int i = 0; for (int loop = ordbvos.length; i < loop; i++) {
          ntotalconvertnum = SoVoConst.duf0;

          ntotalconvertnum = SoVoTools.getMnyAdd(ntotalconvertnum, ordbvos[i].getNarrangescornum());

          ntotalconvertnum = SoVoTools.getMnyAdd(ntotalconvertnum, ordbvos[i].getNarrangepoapplynum());

          ntotalconvertnum = SoVoTools.getMnyAdd(ntotalconvertnum, ordbvos[i].getNarrangetoornum());

          ntotalconvertnum = SoVoTools.getMnyAdd(ntotalconvertnum, ordbvos[i].getNorrangetoapplynum());

          ntotalconvertnum = SoVoTools.getMnyAdd(ntotalconvertnum, ordbvos[i].getNarrangemonum());

          if ((ntotalconvertnum != null) && (ordbvos[i].getNnumber() != null) && (ntotalconvertnum.abs().compareTo(ordbvos[i].getNnumber().abs()) >= 0))
          {
            ordbvos[i].setBarrangedflag(SoVoConst.buftrue);
          }
        }
      }

      saleorder.processVOForTrans();
    }
  }

  protected boolean onSaveExt()
  {
    boolean bret = false;
    String actionname = null;
    if (this.strState.equals("新增"))
      actionname = "PreKeep";
    else if (this.strState.equals("修改")) {
      actionname = "PreModify";
    }
    SaleOrderVO saleorder = null;
    try
    {
      long s = System.currentTimeMillis();
      getBillCardPanel().cleanNullLine();
      saleorder = (SaleOrderVO)getBillCardPanel().getBillValueChangeVO(SaleOrderVO.class.getName(), SaleorderHVO.class.getName(), SaleorderBVO.class.getName());

      SaleOrderVO saleoldorder = (SaleOrderVO)getBillCardPanel().getBillValueVO(SaleOrderVO.class.getName(), SaleorderHVO.class.getName(), SaleorderBVO.class.getName());

      ((SaleorderHVO)saleorder.getParentVO()).setCreceipttype(getBillType());

      ((SaleorderHVO)saleoldorder.getParentVO()).setCreceipttype(getBillType());

      getBillCardPanel().setHeadItem("creceipttype", getBillType());

      ((SaleorderHVO)saleorder.getParentVO()).setCbiztype(getBillCardPanel().getBusiType());

      getBillCardPanel().setHeadItem("cbiztype", getBillCardPanel().getBusiType());

      if (!calculatePreceive(saleoldorder)) {
        return false;
      }
      if (this.strState.equals("新增"))
        onCheck(saleorder);
      else {
        onCheck(saleoldorder);
      }
      saleorder = getLineNumber(saleoldorder, saleorder);

      if (!BillRowNo.verifyRowNosCorrect(getBillCardPanel(), getBillCardPanel().getRowNoItemKey()))
      {
        return bret;
      }
      ((SaleorderHVO)saleorder.getParentVO()).setFstatus(new Integer(1));

      ((SaleorderHVO)saleorder.getParentVO()).setPk_corp(getCorpPrimaryKey());

      UIRefPane vreceiveaddress = (UIRefPane)getBillCardPanel().getHeadItem("vreceiveaddress").getComponent();

      saleorder.getHeadVO().setVreceiveaddress(vreceiveaddress.getUITextField().getText());

      ((SaleorderHVO)saleorder.getParentVO()).setTime(this.m_ActionTime);
      if (saleorder.getChildrenVO() != null) {
        for (int i = 0; i < saleorder.getChildrenVO().length; i++) {
          SaleorderBVO bodyVO = (SaleorderBVO)saleorder.getChildrenVO()[i];

          bodyVO.setPkcorp(getCorpPrimaryKey());
          if (bodyVO.getStatus() == 2) {
            bodyVO.setPrimaryKey(null);
          }
        }
      }
      saleorder.setCuruserid(getClientEnvironment().getUser().getPrimaryKey());

      saleorder.setDcurdate(getClientEnvironment().getDate());
      saleorder.setCusername(getClientEnvironment().getUser().getUserName());

      saleorder.setCcorpname(getClientEnvironment().getCorporation().getUnitname());

      saleoldorder.setCuruserid(getClientEnvironment().getUser().getPrimaryKey());

      saleoldorder.setDcurdate(getClientEnvironment().getDate());

      showHintMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000136"));

      SCMEnv.out("开始保存：" + System.currentTimeMillis());

      saleorder.setFirstTime(true);
      boolean bContinue = true;

      saleorder.setIAction(0);

      saleorder.setStatus(2);
      saleorder.processVOForTrans();
      ClientLink clientLink = new ClientLink(getClientEnvironment());

      saleorder.setClientLink(clientLink);

      while (bContinue) {
        try {
          if (this.strState.equals("新增")) {
            UFDateTime ud = ClientEnvironment.getServerTime();
            saleorder.getHeadVO().setAttributeValue("dbilltime", ud);

            ArrayList alistID = (ArrayList)PfUtilClient.processActionNoSendMessage(this, "PreKeep", getBillType(), getClientEnvironment().getDate().toString(), saleorder, null, null, null);

            getBillCardPanel().setTailItem("dbilltime", ud);
            this.id = ((String)alistID.get(0));
            loadIDafterADD(alistID);
          }

          bContinue = false;
        } catch (Exception ex) {
          bContinue = doException(saleorder, null, ex);
          if (!bContinue)
            return false;
        }
      }
      SCMEnv.out("保存结束：" + System.currentTimeMillis());

      showHintMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000232", null, new String[] { (System.currentTimeMillis() - s) / 1000L + "" }));

      this.strState = "自由";
      bret = true;

      getBillCardTools().setHeadRefLimit(this.strState);

      this.vRowATPStatus = new Vector();
      for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
        this.vRowATPStatus.addElement(new UFBoolean(false));
      }
      getBillCardPanel().updateValue();
      try
      {
        InvAttrCellRenderer ficr = new InvAttrCellRenderer();
        ficr.setFreeItemRenderer(getBillCardPanel(), null);
      }
      catch (Exception einv)
      {
      }
    }
    catch (AtpSetException atpsetex) {
      showWarningMessage(atpsetex.getMessage());
      getBillCardTools().processAtpSetException(atpsetex.getMessage());
    }
    catch (Exception ex)
    {
      handleSaveException(getBillType(), getBillCardPanel().getBusiType(), actionname, getBillCardPanel().getCorp(), getBillCardPanel().getOperator(), saleorder, ex);

      markRow(ex.getMessage());

      return bret;
    }
    return bret;
  }

  protected void onSendAudit()
  {
    if (!"自由".equals(this.strState)) {
      onSave();
    }
    if (!"自由".equals(this.strState)) {
      return;
    }
    SaleOrderVO saleorder = (SaleOrderVO)getVO(false);
    if ((saleorder == null) || (saleorder.getParentVO() == null)) {
      showErrorMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000199"));
    }
    else {
      if (!getClientEnvironment().getUser().getPrimaryKey().equals(saleorder.getHeadVO().getCoperatorid()))
      {
        showErrorMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000505"));

        return;
      }
      try {
        boolean isExist = false;
        isExist = PfUtilBO_Client.isExistWorkFlow(getBillType(), saleorder.getBizTypeid(), getClientEnvironment().getCorporation().getPk_corp(), getClientEnvironment().getUser().getPrimaryKey());

        if (!isExist) {
          showErrorMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000111"));

          return;
        }

        saleorder.validate();

        int iWorkflowstate = 0;
        iWorkflowstate = PfUtilClient.queryWorkFlowStatus(saleorder.getBizTypeid(), getBillType(), saleorder.getParentVO().getPrimaryKey());

        if ((iWorkflowstate != 2) && (iWorkflowstate != 5) && (iWorkflowstate != 3))
        {
          if (iWorkflowstate == 0) {
            showErrorMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000078"));

            return;
          }if (iWorkflowstate == 1) {
            showErrorMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000079"));

            return;
          }if (iWorkflowstate == 3) {
            showErrorMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000071"));

            return;
          }if (iWorkflowstate == 4) {
            showErrorMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000112"));

            return;
          }if (iWorkflowstate == 5) {
            showErrorMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000070"));

            return;
          }
        }
        ClientLink clientLink = new ClientLink(getClientEnvironment());

        saleorder.setClientLink(clientLink);

        saleorder.setCuruserid(getClientEnvironment().getUser().getPrimaryKey());

        saleorder.setDcurdate(getClientEnvironment().getDate());
        saleorder.setCusername(getClientEnvironment().getUser().getUserName());

        saleorder.setCcorpname(getClientEnvironment().getCorporation().getUnitname());

        saleorder.setCnodename(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000504"));

        saleorder.setIAction(15);
        String ErrMsg = null;

        ArrayList al = (ArrayList)PfUtilClient.processAction(this, "SAVE", getBillType(), getClientEnvironment().getDate().toString(), saleorder, null);

        ErrMsg = (String)al.get(1);
        if ((ErrMsg != null) && (ErrMsg.startsWith("ERR"))) {
          ErrMsg = ErrMsg.substring(3);
          if ((ErrMsg != null) && (ErrMsg.trim().length() > 0)) {
            showWarningMessage(ErrMsg);
            if (ErrMsg.indexOf("账期") > 0) {
              getBillCardPanel().setHeadItem("boverdate", new UFBoolean(true));
            }
          }
        }

        if (PfUtilClient.isSuccess()) {
          nc.ui.so.so001.panel.bom.BillTools.reLoadBillState(this, getClientEnvironment());

          setButtonsState();
          showHintMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000291"));
        }
        else
        {
          showHintMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000292"));
        }

        reLoadTS();
        updateCacheVO();
      } catch (BusinessException e) {
        showWarningMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000293") + e.getMessage());
      }
      catch (Exception e)
      {
        showWarningMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000293") + e.getMessage());
      }
    }
  }

  private void reLoadTS()
  {
    try
    {
      if (getBillListPanel().isShowing())
        this.num = getBillListPanel().getHeadTable().getSelectedRow();
      int temp = this.num;
      String sBillID = getBillID();

      if (sBillID != null) {
        if (this.strShowState.equals("列表"))
        {
          String sql = "select so_sale.csaleid,so_sale.vreceiptcode,so_sale.fstatus,so_sale.ts,so_sale.capproveid from so_sale where so_sale.csaleid='" + sBillID.trim() + "'";

          SORowData[] rows = null;

          rows = SOToolsBO_Client.getSORows(sql);

          if ((rows != null) && (rows.length > 0))
          {
            Integer fstatus = rows[0].getInteger(2);

            String value = SaleOrderVO.getBillState(fstatus);

            getBillListPanel().getHeadBillModel().setValueAt(rows[0].getUFDateTime(3), temp, "ts");

            getBillListPanel().getHeadBillModel().setValueAt(value, temp, "fstatus");

            if ((fstatus.intValue() == 2) || (fstatus.intValue() == 6) || (fstatus.intValue() == 3))
            {
              getBillListPanel().getHeadBillModel().setValueAt(getClientEnvironment().getUser().getPrimaryKey().toString(), temp, "capproveid");

              getBillListPanel().getHeadBillModel().setValueAt(getClientEnvironment().getUser().getUserName().toString(), temp, "capprovename");

              getBillListPanel().getHeadBillModel().setValueAt(getClientEnvironment().getDate().toString(), temp, "dapprovedate");
            }
            else
            {
              getBillListPanel().getHeadBillModel().setValueAt(null, temp, "capproveid");

              getBillListPanel().getHeadBillModel().setValueAt(null, temp, "capprovename");

              getBillListPanel().getHeadBillModel().setValueAt(null, temp, "dapprovedate");
            }

            if ((getBillListPanel().getBodyBillModel() != null) && 
              (getBillListPanel().getBodyTable().getRowCount() > 0)) {
              for (int i = 0; i < getBillListPanel().getBodyBillModel().getRowCount(); 
                i++) {
                getBillListPanel().getBodyBillModel().setValueAt(value, i, "frowstatus");
              }
            }
          }
        }
        else
        {
          String[] formula = { "ts->getColValue(so_sale,ts,csaleid,csaleid)", "vreceiptcode->getColValue(so_sale,vreceiptcode,csaleid,csaleid)" };

          getBillCardPanel().execHeadFormulas(formula);
        }
      }

      updateUI();
    } catch (Exception e) {
      SCMEnv.out("重新加载表头TS失败.");
      SCMEnv.out(e.getMessage());
    }
  }

  private SaleOrderVO remarkOOSLine(SaleOrderVO voSource)
  {
    SaleorderBVO[] voBodys = (SaleorderBVO[])voSource.getChildrenVO();
    for (int i = 0; i < voBodys.length; i++) {
      if ((voBodys[i].getBoosflag() != null) && (voBodys[i].getBoosflag().booleanValue()))
      {
        if (voSource.getChildrenVO()[i].getStatus() == 1) {
          voSource.getChildrenVO()[i].setStatus(3);

          ((SaleorderBVO[])(SaleorderBVO[])voSource.getChildrenVO())[i].setBsupplyflag(new UFBoolean(true));
        }
      }
    }

    return voSource;
  }

  private void removeOOSLine()
  {
    int iRowCount = getBillCardPanel().getBillTable().getRowCount();
    Vector vecOOSLine = new Vector();
    for (int i = iRowCount - 1; i >= 0; i--) {
      UFBoolean boosflag = getBillCardPanel().getBodyValueAt(i, "boosflag") == null ? new UFBoolean(false) : new UFBoolean(getBillCardPanel().getBodyValueAt(i, "boosflag").toString());

      if (boosflag.booleanValue()) {
        if ((getBillCardPanel().alInvs != null) && (i < getBillCardPanel().alInvs.size()))
        {
          getBillCardPanel().alInvs.remove(i);
        }
        if ((this.vRowATPStatus != null) && (this.vRowATPStatus.size() > 0) && (i < this.vRowATPStatus.size()))
        {
          this.vRowATPStatus.remove(i);
        }
        vecOOSLine.addElement(i + "");
      }
    }
    int[] aryRow = new int[vecOOSLine.size()];
    for (int i = 0; i < vecOOSLine.size(); i++) {
      aryRow[i] = new Integer(vecOOSLine.elementAt(i).toString()).intValue();
    }

    getBillCardPanel().getBillModel().delLine(aryRow);
  }

  private void setBodyDate(int row)
  {
    UFDate dbilldate = new UFDate(getBillCardPanel().getHeadItem("dbilldate").getValue());

    getBillCardPanel().setBodyValueAt(dbilldate.toString(), row, "dconsigndate");

    getBillCardPanel().afterBodyDateEdit(row, true);
    String sDateDeliver = getBillCardPanel().getBodyValueAt(row, "ddeliverdate") == null ? null : getBillCardPanel().getBodyValueAt(row, "ddeliverdate").toString().trim();

    if ((sDateDeliver == null) || (sDateDeliver.length() == 0))
      getBillCardPanel().setBodyValueAt(dbilldate.toString(), row, "ddeliverdate");
  }

  private void setBomButtonsState()
  {
    if ((this.strOldState.equals("新增")) || (this.strOldState.equals("修改"))) {
      if (this.strBomState.equals("ADD")) {
        this.boBomSave.setEnabled(true);
        this.boBomEdit.setEnabled(false);
        this.boBomCancel.setEnabled(true);
        this.boBomPrint.setEnabled(false);
        this.boBomReturn.setEnabled(false);
      }
      if (this.strBomState.equals("FREE")) {
        this.boBomSave.setEnabled(false);
        this.boBomEdit.setEnabled(true);
        this.boBomCancel.setEnabled(false);
        this.boBomPrint.setEnabled(true);
        this.boBomReturn.setEnabled(true);
        getBillTreeCardPanel().setEnabled(false);
      }
      if (this.strBomState.equals("SAVE")) {
        this.boBomSave.setEnabled(false);
        this.boBomEdit.setEnabled(true);
        this.boBomCancel.setEnabled(false);
        this.boBomPrint.setEnabled(true);
        this.boBomReturn.setEnabled(true);
        getBillTreeCardPanel().setEnabled(false);
      }
      if (this.strBomState.equals("EDIT")) {
        this.boBomSave.setEnabled(true);
        this.boBomEdit.setEnabled(false);
        this.boBomCancel.setEnabled(true);
        this.boBomPrint.setEnabled(false);
        this.boBomReturn.setEnabled(false);
      }
      if (this.strBomState.equals("CANCEL")) {
        this.boBomSave.setEnabled(true);
        this.boBomEdit.setEnabled(false);
        this.boBomCancel.setEnabled(true);
        this.boBomPrint.setEnabled(false);
        this.boBomReturn.setEnabled(true);
      }
      setBomPrice();
    } else {
      this.boBomSave.setEnabled(false);
      this.boBomEdit.setEnabled(false);
      this.boBomCancel.setEnabled(false);
      this.boBomPrint.setEnabled(true);
      this.boBomReturn.setEnabled(true);
      getBillTreeCardPanel().setEnabled(false);
    }
    updateButtons();
  }

  private void setButtons()
  {
    if (this.strShowState.equals("列表")) {
      setButtons(this.aryListButtonGroup);
    } else if (this.strShowState.equals("卡片")) {
      setButtons(this.aryButtonGroup);
    } else {
      this.boLine.removeAllChildren();
      this.boLine.addChildButton(this.boDelLine);
      setButtons(this.aryBatchButtonGroup);
    }
  }

  public void setButtonsState()
  {
    if (this.bInMsgPanel) {
      return;
    }
    if (this.strState.equals("BOM"))
      setBomButtonsState();
    else if (this.strShowState.equals("列表"))
      setListButtonsState();
    else
      setCardButtonsState();
  }

  private void setListButtonsState()
  {
    PfUtilClient.retAddBtn(this.boAdd, getCorpPrimaryKey(), getBillType(), this.boBusiType);

    this.boBusiType.setEnabled(true);
    this.boAdd.setEnabled(true);

    this.boBrowse.setEnabled(true);

    int selectRowCount = getBillListPanel().getHeadTable().getSelectedRowCount();

    if (selectRowCount > 1) {
      this.boMaintain.setEnabled(true);
      this.boBlankOut.setEnabled(true);
      this.boAudit.setEnabled(true);
      this.boAction.setEnabled(true);
      this.boSendAudit.setEnabled(true);
      this.boCancelAudit.setEnabled(true);
      this.boSendAudit.setEnabled(false);
      this.boFreeze.setEnabled(false);
      this.boCancelFreeze.setEnabled(false);
      this.boClose.setEnabled(false);
      this.boRefundment.setEnabled(false);
      this.boQuery.setEnabled(true);
      this.boCard.setEnabled(true);
      this.boPrntMgr.setEnabled(true);
      this.boPreview.setEnabled(true);
      this.boPrint.setEnabled(true);
      this.boAssistant.setEnabled(false);
      this.boAsstntQry.setEnabled(false);
      this.boAuditFlowStatus.setEnabled(true);
    } else if (selectRowCount == 1) {
      this.boMaintain.setEnabled(true);
      this.boEdit.setEnabled(true);
      this.boAction.setEnabled(true);
      this.boPrntMgr.setEnabled(true);
      this.boPrint.setEnabled(true);
      this.boPreview.setEnabled(true);
      this.boAssistant.setEnabled(true);
      this.boAsstntQry.setEnabled(true);
      this.boAfterAction.setEnabled(true);
      this.boBatch.setEnabled(false);
      this.boRefundment.setEnabled(true);
      this.boQuery.setEnabled(true);
      this.boCard.setEnabled(true);
      this.boDocument.setEnabled(true);
      this.boOrderQuery.setEnabled(true);
      this.boCopyBill.setEnabled(true);
      this.boSendAudit.setEnabled(true);
      this.boAuditFlowStatus.setEnabled(true);
    } else {
      this.boMaintain.setEnabled(false);
      this.boEdit.setEnabled(false);
      this.boAudit.setEnabled(false);
      this.boAction.setEnabled(false);
      this.boPrntMgr.setEnabled(false);
      this.boPrint.setEnabled(false);
      this.boPreview.setEnabled(false);
      this.boAssistant.setEnabled(false);
      this.boAsstntQry.setEnabled(false);
      this.boBatch.setEnabled(false);
      this.boQuery.setEnabled(true);
      this.boCard.setEnabled(true);
      this.boRefundment.setEnabled(false);
      this.boAfterAction.setEnabled(false);
      this.boDocument.setEnabled(false);
      this.boOrderQuery.setEnabled(false);
      this.boCopyBill.setEnabled(false);
      this.boSendAudit.setEnabled(false);
      this.boAuditFlowStatus.setEnabled(false);
    }

    if ((this.selectRow > -1) && (getBillListPanel().getHeadBillModel().getValueAt(this.selectRow, "fstatus") != null))
    {
      int iStatus = Integer.parseInt(getBillListPanel().getHeadItem("fstatus").converType(getBillListPanel().getHeadBillModel().getValueAt(this.selectRow, "fstatus")).toString());

      setBtnsByBillState(iStatus);

      Object retinvflag = getBillListPanel().getHeadBillModel().getValueAt(this.selectRow, "bretinvflag");

      if ((retinvflag != null) && (retinvflag.toString().equals("true")))
      {
        this.boRefundment.setEnabled(false);
      }
    }

    if ((getBillListPanel() != null) && (getBillListPanel().getHeadTable() != null))
    {
      if (getBillListPanel().getHeadTable().getRowCount() > 0) {
        this.boFind.setEnabled(true);
        getBoListDeselectAll().setEnabled(true);
        getBoListSelectAll().setEnabled(true);
      } else {
        this.boFind.setEnabled(false);
        getBoListDeselectAll().setEnabled(false);
        getBoListSelectAll().setEnabled(false);
      }
    }
    else this.boFind.setEnabled(false);

    this.boRefresh.setEnabled(this.b_query);

    this.boSave.setEnabled(false);
    this.boCancel.setEnabled(false);
    this.boLine.setEnabled(false);
    this.boFirst.setEnabled(false);
    this.boPre.setEnabled(false);
    this.boNext.setEnabled(false);
    this.boLast.setEnabled(false);

    updateButtons();
  }

  protected final ButtonObject getBoAfterAction() {
    if (this.boAfterAction == null) {
      this.boAfterAction = new ButtonObject(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000128"), NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000128"), 0, "后续业务");
    }

    return this.boAfterAction;
  }

  private void setDefaultData(boolean isfree)
  {
    if (isfree) {
      getBillCardPanel().setHeadItem("naccountperiod", null);
    }

    getBillCardPanel().setHeadItem("salecorp", getClientEnvironment().getCorporation().getUnitname());

    getBillCardPanel().setHeadItem("pk_corp", getCorpPrimaryKey());

    getBillCardPanel().setHeadItem("cbiztype", getBillCardPanel().getBusiType());

    getBillCardPanel().getHeadItem("cfreecustid").setEnabled(false);

    getBillCardPanel().setHeadItem("vreceiptcode", null);

    getBillCardPanel().setHeadItem("dbilldate", getClientEnvironment().getDate().toString());

    getBillCardPanel().setHeadItem("fstatus", "1");

    if (getBillCardPanel().getHeadItem("cemployeeid").getValueObject() == null) {
      String cemployeeid = getCemployeeId();
      getBillCardPanel().setHeadItem("cemployeeid", cemployeeid);
      ((UIRefPane)getBillCardPanel().getHeadItem("cemployeeid").getComponent()).setPK(cemployeeid);

      getBillCardPanel().afterEmployeeEdit(null);
    }

    getBillCardPanel().setTailItem("dmakedate", getClientEnvironment().getDate().toString());

    getBillCardPanel().setTailItem("coperatorid", getClientEnvironment().getUser().getPrimaryKey());

    getBillCardPanel().setTailItem("capproveid", null);

    getBillCardPanel().setTailItem("dapprovedate", null);

    if (isfree)
    {
      getBillCardPanel().setHeadItem("ndiscountrate", "100.0");

      getBillCardPanel().setHeadItem("nevaluatecarriage", "0.0");

      ((UIRefPane)getBillCardPanel().getHeadItem("vreceiptcode").getComponent()).getUITextField().setDelStr("+");
    }
    else
    {
      getBillCardPanel().initFreeItem();
      getBillCardPanel().initUnit();
      initCTTypeVO();

      getBillCardTools().setBodyInventory1(0, getBillCardPanel().getRowCount());

      getBillCardTools().setBodyCchantypeid(0, getBillCardPanel().getRowCount());

      UFBoolean bfreecustflag = getBillCardTools().getHeadUFBooleanValue("bfreecustflag");

      if ((bfreecustflag == null) || (!bfreecustflag.booleanValue()))
        getBillCardPanel().getHeadItem("cfreecustid").setEnabled(false);
      else {
        getBillCardPanel().getHeadItem("cfreecustid").setEnabled(true);
      }

      UIRefPane ccurrencytypeid = (UIRefPane)getBillCardPanel().getHeadItem("ccurrencytypeid").getComponent();

      if (getBillCardPanel().getRowCount() > 0) {
        Object oTemp = getBillCardPanel().getBodyValueAt(0, "ccurrencytypeid");

        if ((oTemp != null) && (((String)oTemp).trim().length() > 0))
        {
          ccurrencytypeid.setPK(getBillCardPanel().getBodyValueAt(0, "ccurrencytypeid"));

          getBillCardPanel().setHeadItem("nexchangeotobrate", getBillCardPanel().getBodyValueAt(0, "nexchangeotobrate"));

          getBillCardPanel().setHeadItem("nexchangeotoarate", getBillCardPanel().getBodyValueAt(0, "nexchangeotoarate"));

          if ((getBillCardTools().getBodyUFDoubleValue(0, "nexchangeotobrate") == null) || (getBillCardTools().getBodyUFDoubleValue(0, "nexchangeotoarate") == null))
          {
            getBillCardPanel().afterCurrencyChange();
          }
          else ctlCurrencyEdit();
        }
        else
        {
          String[] formulas = { "ccurrencytypeid->getColValue(bd_cumandoc,pk_currtype1,pk_cumandoc,ccustomerid)" };
          getBillCardPanel().getBillData().execHeadFormulas(formulas);
          try
          {
            if (ccurrencytypeid.getRefPK() == null)
              ccurrencytypeid.setPK(this.currtype.getLocalCurrPK());
            getBillCardPanel().afterCurrencyChange();
          }
          catch (Exception e1) {
          }
        }
        getBillCardPanel().setHeadItem("ndiscountrate", getBillCardTools().getBodyUFDoubleValue(0, "ndiscountrate"));
      }

      String headccurrencytypeid = getBillCardPanel().getHeadItem("ccurrencytypeid").getValue();

      String headccurrencytypename = ccurrencytypeid.getRefName();

      String headBrate = getBillCardPanel().getHeadItem("nexchangeotobrate").getValue();

      String headArate = getBillCardPanel().getHeadItem("nexchangeotoarate").getValue();

      if ((getBillType().equals("30")) || (getBillType().equals("3A")))
      {
        String[] formulas = { "wholemanaflag->getColValue(bd_invmandoc,wholemanaflag,pk_invmandoc,cinventoryid)", "isconfigable->getColValue(bd_invmandoc,isconfigable,pk_invmandoc,cinventoryid)", "isspecialty->getColValue(bd_invmandoc,isspecialty,pk_invmandoc,cinventoryid)" };

        getBillCardPanel().getBillModel().execFormulas(formulas);
      }

      getBillCardPanel().afterCreceiptcorpEdit();

      String[] keys = { "cconsigncorpid", "cconsigncorp", "creccalbody", "creccalbodyid", "crecwarehouse", "crecwareid", "bdericttrans" };

      for (int i = 0; i < getBillCardPanel().getRowCount(); i++)
      {
        getBillCardPanel().ctlUIOnCconsignCorpChg(i);

        String sDateBody = getBillCardPanel().getBodyValueAt(i, "dconsigndate") == null ? null : getBillCardPanel().getBodyValueAt(i, "dconsigndate").toString().trim();

        if ((sDateBody == null) || (sDateBody.length() == 0)) {
          setBodyDate(i);
        }

        getBillCardPanel().setBodyValueAt(new Integer(1), i, "frowstatus");

        if (getBillCardPanel().getBodyValueAt(i, "blargessflag") == null) {
          getBillCardPanel().setBodyValueAt(new Boolean(false), i, "blargessflag");
        }

        getBillCardPanel().setBodyValueAt(headccurrencytypeid, i, "ccurrencytypeid");

        getBillCardPanel().setBodyValueAt(headccurrencytypename, i, "ccurrencytypename");

        getBillCardPanel().setBodyValueAt(headBrate, i, "nexchangeotobrate");

        getBillCardPanel().setBodyValueAt(headArate, i, "nexchangeotoarate");

        if ((getSouceBillType().equals("4H")) || (getSouceBillType().equals("42")))
        {
          getBillCardTools().setBodyCellsEdit(keys, i, false);
        }

        if (getBillType().equals("30"))
        {
          UFBoolean discountflag = getBillCardTools().getBodyUFBooleanValue(i, "discountflag");

          if ((discountflag == null) || (!discountflag.booleanValue()))
          {
            if ((getSouceBillType().equals("37")) || (getSouceBillType().equals("21")) || (getSouceBillType().equals("38")))
            {
              getBillCardPanel().setBodyValueAt(null, i, "nquoteunitnum");

              nc.ui.so.so001.panel.bom.BillTools.calcQPrice(i, getBillCardPanel().getBillModel(), "30");

              getBillCardPanel().calculateNumber(i, "nnumber");
            }
          }
        }

      }

      if (getBillType().equals("30"))
      {
        if ((getSouceBillType().equals("Z4")) || (getSouceBillType().equals("Z3")) || (getSouceBillType().equals("4H")) || (getSouceBillType().equals("42")) || (getSouceBillType().equals("3B")))
        {
          boolean is3B = getSouceBillType().equals("3B");
          boolean isContract = false;
          if ((getSouceBillType().equals("Z4")) || (getSouceBillType().equals("Z3")))
          {
            isContract = true;
          }int[] rows = new int[getBillCardPanel().getBillModel().getRowCount()];

          int i = 0; int loop = getBillCardPanel().getBillModel().getRowCount();
          for (; i < loop; i++)
          {
            rows[i] = i;
            if (!is3B) {
              getBillCardPanel().setBodyValueAt(null, i, "nquoteunitnum");

              nc.ui.so.so001.panel.bom.BillTools.calcQPrice(i, getBillCardPanel().getBillModel(), "30");
            }

            if (isContract) {
              if (this.SA_02.booleanValue()) {
                getBillCardPanel().calculateNumber(i, "norgqttaxprc");
              }
              else {
                getBillCardPanel().calculateNumber(i, "norgqtprc");
              }
            }

          }

          getBillCardPanel().afterNumberEdit(rows, "nnumber", null, false, true);
        }
      }
    }
  }

  private String getCemployeeId()
  {
    try
    {
      IUserManageQuery query = (IUserManageQuery)NCLocator.getInstance().lookup("nc.itf.uap.rbac.IUserManageQuery");

      PsndocVO psn = query.getPsndocByUserid(getCorpPrimaryKey(), getClientEnvironment().getUser().getPrimaryKey());

      if (psn != null) {
        return psn.getPk_psndoc();
      }
      return null;
    } catch (BusinessException e) {
      SCMEnv.out(e);
    }return null;
  }

  private void setLineButtonsState()
  {
    if (getBillCardPanel().getRowCount() == 0) {
      this.boDelLine.setEnabled(false);
      this.boCopyLine.setEnabled(false);
      this.boPasteLine.setEnabled(false);
    } else {
      this.boDelLine.setEnabled(true);
      this.boCopyLine.setEnabled(true);
      this.boPasteLine.setEnabled(true);
    }
  }

  public void setNoEditItem()
  {
    try
    {
      UFDouble dcathpay = getBillCardTools().getHeadUFDoubleValue("nreceiptcathmny");

      if (((dcathpay != null) && (dcathpay.doubleValue() != 0.0D)) || (this.strState.equals("修订")))
      {
        if (!isHeadCustCanbeModified()) {
          getBillCardPanel().getHeadItem("ccustomerid").setEnabled(false);
        }
      }

      if (!this.strState.equals("修订")) {
        String[] formulas = { "ccustbasid->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,ccustomerid)", "bfreecustflag->getColValue(bd_cubasdoc,freecustflag,pk_cubasdoc,ccustbasid)" };

        getBillCardPanel().execHeadFormulas(formulas);

        UFBoolean bfreecustflag = getBillCardTools().getHeadUFBooleanValue("bfreecustflag");

        if ((bfreecustflag != null) && (bfreecustflag.booleanValue())) {
          getBillCardPanel().getHeadItem("cfreecustid").setEnabled(true);
        }
        else {
          getBillCardPanel().getHeadItem("cfreecustid").setEnabled(false);
        }

      }

      if ((getBillType().equals("30")) || (getBillType().equals("3A")))
      {
        String curID = getBillCardPanel().getHeadItem("ccurrencytypeid").getValue();

        if ((curID != null) && (curID.length() != 0)) {
          if (this.currtype.isLocalCurrType(curID)) {
            getBillCardPanel().getHeadItem("nexchangeotobrate").setEnabled(false);
          }
          else {
            getBillCardPanel().getHeadItem("nexchangeotobrate").setEnabled(true);
          }
        }
        if (this.strState.equals("修订")) {
          getBillCardPanel().getHeadItem("ccalbodyid").setEnabled(false);

          getBillCardPanel().getHeadItem("cwarehouseid").setEnabled(false);
        }

      }

      ((UIRefPane)getBillCardPanel().getHeadItem("ndiscountrate").getComponent()).setMinValue(0.0D);

      String[] bodyItems = { "nqttaxnetprc", "nqtnetprc", "nqttaxprc", "nqtprc" };

      for (int k = 0; k < bodyItems.length; k++) {
        if (getBillCardPanel().getBodyItem(bodyItems[k]) != null) {
          getBillCardPanel().getBodyItem(bodyItems[k]).setEnabled(false);
        }

      }

      if ((this.DRP04 != null) && (this.DRP04.booleanValue()) && 
        (getSouceBillType().equals("51"))) {
        getBillCardPanel().getBodyItem("nnumber").setEnabled(false);
        getBillCardPanel().getBodyItem("nquoteunitnum").setEnabled(false);

        getBillCardPanel().getBodyItem("npacknumber").setEnabled(false);
      }

      for (int i = 0; i < getBillCardPanel().getRowCount(); i++)
      {
        getBillCardPanel().setAssistUnit(i);
        getBillCardPanel().setCtItemEditable(i);
        getBillCardPanel().setScaleEditableByRow(i);
      }

      if (getSouceBillType().equals("51")) {
        for (int row = 0; row < getBillCardPanel().getRowCount(); row++) {
          if (getBillCardPanel().getBodyValueAt(row, "nnumber") != null) {
            getBillCardPanel().afterNumberEdit(new int[] { row }, "nnumber", null, false, true);
          }
        }

      }

      if (getSouceBillType().equals("30")) {
        for (int row = 0; row < getBillCardPanel().getRowCount(); row++) {
          if ((getBillCardPanel().getBodyValueAt(row, "fixedflag") != null) && (getBillCardPanel().getBodyValueAt(row, "fixedflag").equals(new Boolean(true))))
          {
            BillModel bm = getBillCardPanel().getBillModel();
            getBillCardPanel().setBodyValueAt(nc.ui.so.so001.panel.bom.BillTools.calc(row, nc.ui.so.so001.panel.bom.BillTools.value(row, "nnumber", new UFDouble(0), bm), "scalefactor", 3, bm), row, "npacknumber");
          }

        }

      }

      if ((getBillCardPanel().getHeadItem("bfreecustflag").getValue() == null) || (getBillCardPanel().getHeadItem("bfreecustflag").getValue().equals("false")))
      {
        getBillCardPanel().getHeadItem("cfreecustid").setEnabled(false);
      }
      else getBillCardPanel().getHeadItem("cfreecustid").setEnabled(true);

      UFBoolean isDiscount = null;
      UFBoolean isLabor = null;
      int i = 0; for (int iLen = getBillCardPanel().getRowCount(); i < iLen; i++)
      {
        if (getBillType().equals("30"))
        {
          Object temp = getBillCardPanel().getBodyValueAt(i, "wholemanaflag");

          boolean wholemanaflag = temp == null ? false : new UFBoolean(temp.toString()).booleanValue();

          getBillCardPanel().setCellEditable(i, "fbatchstatus", wholemanaflag);

          getBillCardPanel().setCellEditable(i, "cbatchid", wholemanaflag);
        }

        isDiscount = getBillCardTools().getBodyUFBooleanValue(i, "discountflag");

        isLabor = getBillCardTools().getBodyUFBooleanValue(i, "laborflag");

        if (((isDiscount != null) && (isDiscount.booleanValue())) || ((isLabor != null) && (isLabor.booleanValue())))
        {
          getBillCardTools().setBodyCellsEdit(new String[] { "cconsigncorp", "creccalbody", "crecwarehouse", "bdericttrans", "boosflag", "bsupplyflag" }, i, false);
        }

        if (!this.SO23.booleanValue()) {
          getBillCardPanel().setCellEditable(i, "nouttoplimit", false);
        }

        if ((this.SA_15 != null) && (this.SA_15.booleanValue()) && (this.SA_07 != null) && (!this.SA_07.booleanValue()))
        {
          getBillCardTools().setBodyCellsEdit(SOBillCardTools.getSaleItems_Price(), i, false);
        }

      }

      getBillCardTools().setCardPanelCellEditableByLargess(this.SO59.booleanValue());

      UFDouble nreceiptcathmny = getBillCardTools().getHeadUFDoubleValue("nreceiptcathmny");

      if ((nreceiptcathmny != null) && (nreceiptcathmny.doubleValue() > 0.0D)) {
        getBillCardPanel().getHeadItem("ccurrencytypeid").setEnabled(false);

        getBillCardPanel().getHeadItem("nexchangeotobrate").setEnabled(false);

        getBillCardPanel().getHeadItem("nexchangeotoarate").setEnabled(false);
      }

      if (getBillCardPanel().getBodyItem("nquoteunitnum").isShow()) {
        getBillCardPanel().getBodyItem("nnumber").setEnabled(false);
      }

      if ("退货".equals(this.strState)) {
        getBillCardPanel().getHeadItem("npreceiverate").setEnabled(false);

        getBillCardPanel().getHeadItem("npreceivemny").setEnabled(false);

        getBillCardPanel().getHeadItem("naccountperiod").setEnabled(false);
      }

      if (!this.strState.equals("修订"))
        getBillCardTools().setEnableCtransmodeid();
    }
    catch (Throwable ex) {
      handleException(ex);
    }
  }

  private void setPanelBomByCurrency(String ccurrencytypeid)
  {
    try
    {
      if ((ccurrencytypeid == null) || (ccurrencytypeid.length() == 0))
        return;
      CurrinfoVO currVO = this.currtype.getCurrinfoVO(ccurrencytypeid, null);

      int digit = currVO.getCurrdigit() == null ? 4 : currVO.getCurrdigit().intValue();

      getBillTreeCardPanel().getBillData().getHeadItem("nsaleprice").setDecimalDigits(digit);

      getBillTreeCardPanel().getBillData().getHeadItem("bomorderfee").setDecimalDigits(digit);

      getBillTreeCardPanel().getBillData().getBodyItem("nprice").setDecimalDigits(digit);

      String name = getBillTreeCardPanel().getBodyItem("nprice").getName();

      if (getBillTreeCardPanel().getBodyPanel().hasShowCol(name))
        getBillTreeCardPanel().getBodyPanel().resetTableCellRenderer(name);
    }
    catch (Exception e) {
      SCMEnv.out("根据币种设置小数位数失败!");
    }
  }

  private void setPzdSelectedEnabled(boolean isEnable)
  {
    for (int i = 0; i < getBillTreeCardPanel().getBillModel().getRowCount(); i++)
      getBillTreeCardPanel().setCellEditable(i, "bselect", isEnable);
  }

  private void setBtnsByBillState(int iState)
  {
    if ((this.strState.equals("修订")) || (this.strState.equals("修改")) || (this.strState.equals("新增")) || (this.strState.equals("退货")))
    {
      return;
    }
    switch (iState)
    {
    case -1:
      this.boBlankOut.setEnabled(false);
      this.boEdit.setEnabled(false);
      this.boAudit.setEnabled(false);
      this.boCancelAudit.setEnabled(false);
      this.boFreeze.setEnabled(false);
      this.boCancelFreeze.setEnabled(false);
      this.boClose.setEnabled(false);
      this.boOpen.setEnabled(false);
      this.boFinish.setEnabled(false);
      this.boModification.setEnabled(false);
      this.boBatch.setEnabled(false);
      this.boRefundment.setEnabled(false);
      this.boAfterAction.setEnabled(false);
      this.boStockLock.setEnabled(false);

      this.boSendAudit.setEnabled(false);
      this.boAuditFlowStatus.setEnabled(false);

      this.boCachPay.setEnabled(false);
      this.boOrdBalance.setEnabled(false);

      break;
    case 1:
      this.boBlankOut.setEnabled(true);
      this.boEdit.setEnabled(true);
      this.boAudit.setEnabled(true);
      this.boCancelAudit.setEnabled(false);
      this.boFreeze.setEnabled(false);
      this.boCancelFreeze.setEnabled(false);
      this.boClose.setEnabled(false);
      this.boOpen.setEnabled(false);
      this.boFinish.setEnabled(false);
      this.boBatch.setEnabled(false);
      this.boModification.setEnabled(false);
      this.boRefundment.setEnabled(false);
      this.boAfterAction.setEnabled(false);
      this.boStockLock.setEnabled(false);

      this.boSendAudit.setEnabled(true);
      this.boAuditFlowStatus.setEnabled(true);

      this.boCachPay.setEnabled(true);
      this.boOrdBalance.setEnabled(true);

      break;
    case 2:
      this.boBlankOut.setEnabled(false);
      this.boAudit.setEnabled(false);
      this.boCancelAudit.setEnabled(true);
      this.boFreeze.setEnabled(true);
      this.boCancelFreeze.setEnabled(false);
      this.boClose.setEnabled(true);
      this.boOpen.setEnabled(false);
      this.boFinish.setEnabled(true);
      this.boModification.setEnabled(true);
      this.boEdit.setEnabled(false);
      this.boBatch.setEnabled(true);
      this.boRefundment.setEnabled(true);
      this.boAfterAction.setEnabled(true);
      this.boStockLock.setEnabled(true);

      this.boSendAudit.setEnabled(false);
      this.boAuditFlowStatus.setEnabled(true);

      this.boCachPay.setEnabled(true);
      this.boOrdBalance.setEnabled(true);

      setImageType(5);

      break;
    case 7:
      this.boBlankOut.setEnabled(false);
      this.boAudit.setEnabled(true);
      this.boCancelAudit.setEnabled(false);
      this.boFreeze.setEnabled(false);
      this.boCancelFreeze.setEnabled(false);
      this.boClose.setEnabled(false);
      this.boOpen.setEnabled(false);
      this.boFinish.setEnabled(false);
      this.boBatch.setEnabled(false);
      this.boModification.setEnabled(false);
      this.boEdit.setEnabled(false);
      this.boRefundment.setEnabled(false);
      this.boAfterAction.setEnabled(false);
      this.boStockLock.setEnabled(false);

      this.boSendAudit.setEnabled(false);
      this.boAuditFlowStatus.setEnabled(true);

      this.boCachPay.setEnabled(true);
      this.boOrdBalance.setEnabled(true);

      break;
    case 8:
      this.boBlankOut.setEnabled(false);
      this.boAudit.setEnabled(false);
      this.boCancelAudit.setEnabled(false);
      this.boFreeze.setEnabled(false);
      this.boCancelFreeze.setEnabled(false);
      this.boClose.setEnabled(false);
      this.boOpen.setEnabled(false);
      this.boFinish.setEnabled(false);
      this.boModification.setEnabled(false);
      this.boEdit.setEnabled(true);
      this.boBatch.setEnabled(false);
      this.boRefundment.setEnabled(false);
      this.boAfterAction.setEnabled(false);
      this.boStockLock.setEnabled(false);

      this.boSendAudit.setEnabled(false);
      this.boAuditFlowStatus.setEnabled(true);

      this.boCachPay.setEnabled(false);
      this.boOrdBalance.setEnabled(false);

      break;
    case 3:
      this.boEdit.setEnabled(false);
      this.boCancel.setEnabled(false);
      this.boBlankOut.setEnabled(false);
      this.boModification.setEnabled(false);
      this.boAudit.setEnabled(false);
      this.boCancelAudit.setEnabled(false);
      this.boFreeze.setEnabled(false);
      this.boCancelFreeze.setEnabled(true);
      this.boClose.setEnabled(false);
      this.boOpen.setEnabled(false);
      this.boFinish.setEnabled(false);
      this.boBatch.setEnabled(false);
      this.boRefundment.setEnabled(false);
      this.boAfterAction.setEnabled(false);
      this.boStockLock.setEnabled(false);

      this.boSendAudit.setEnabled(false);
      this.boAuditFlowStatus.setEnabled(true);

      this.boCachPay.setEnabled(false);
      this.boOrdBalance.setEnabled(false);

      break;
    case 4:
      this.boAudit.setEnabled(true);
      this.boCancelAudit.setEnabled(false);
      this.boFreeze.setEnabled(true);
      this.boCancelFreeze.setEnabled(false);
      this.boClose.setEnabled(true);
      this.boOpen.setEnabled(false);
      this.boFinish.setEnabled(false);
      this.boBatch.setEnabled(false);
      this.boRefundment.setEnabled(false);
      this.boAfterAction.setEnabled(false);
      this.boEdit.setEnabled(false);
      this.boStockLock.setEnabled(false);

      this.boSendAudit.setEnabled(false);
      this.boAuditFlowStatus.setEnabled(true);

      this.boCachPay.setEnabled(false);
      this.boOrdBalance.setEnabled(false);

      break;
    case 6:
      this.boAudit.setEnabled(false);
      this.boCancelAudit.setEnabled(false);
      this.boFreeze.setEnabled(false);
      this.boCancelFreeze.setEnabled(false);
      this.boClose.setEnabled(false);
      this.boOpen.setEnabled(false);
      this.boFinish.setEnabled(false);
      this.boModification.setEnabled(false);
      this.boBatch.setEnabled(true);
      this.boRefundment.setEnabled(true);
      this.boAfterAction.setEnabled(false);
      this.boBlankOut.setEnabled(false);
      this.boEdit.setEnabled(false);
      this.boStockLock.setEnabled(false);

      this.boSendAudit.setEnabled(false);
      this.boAuditFlowStatus.setEnabled(true);

      this.boCachPay.setEnabled(false);
      this.boOrdBalance.setEnabled(false);

      break;
    case 5:
      this.boBlankOut.setEnabled(false);
      this.boEdit.setEnabled(false);
      this.boAudit.setEnabled(false);
      this.boCancelAudit.setEnabled(false);
      this.boFreeze.setEnabled(false);
      this.boCancelFreeze.setEnabled(false);
      this.boClose.setEnabled(false);
      this.boOpen.setEnabled(false);
      this.boFinish.setEnabled(false);
      this.boModification.setEnabled(false);
      this.boBatch.setEnabled(false);
      this.boRefundment.setEnabled(false);
      this.boAfterAction.setEnabled(false);
      this.boAssistant.setEnabled(false);
      this.boStockLock.setEnabled(false);

      this.boSendAudit.setEnabled(false);
      this.boAuditFlowStatus.setEnabled(true);

      this.boCachPay.setEnabled(false);
      this.boOrdBalance.setEnabled(false);
    case 0:
    }

    if (getBillListPanel().getHeadTable().getRowCount() <= 0)
      setExtendBtnsStat(iState);
    updateUI();
  }

  protected void handleSaveException(String billType, String businessType, String actionName, String corpId, String operator, SaleOrderVO vo, Exception ex)
  {
    String err = ex.getMessage();
    if ((ex.getClass() != AtpCheckException.class) || ((ex.getClass() == AtpCheckException.class) && (!"警告".equals(this.SO28))))
    {
      showWarningMessage(err);
    }
    showHintMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000185"));
  }

  private void initCTTypeVO()
  {
    HashMap ht = new HashMap();

    if ((getBillCardPanel().getBillModel().getRowCount() > 0) && (
      (getSouceBillType().equals("Z4")) || (getSouceBillType().equals("Z3"))))
    {
      for (int i = 0; i < getBillCardPanel().getBillModel().getRowCount(); 
        i++) {
        String ct_manageid = (String)getBillCardPanel().getBodyValueAt(i, "ct_manageid");

        if ((ct_manageid != null) && (ct_manageid.length() != 0)) {
          ht.put(ct_manageid, ct_manageid);
        }
      }
    }

    if (ht.size() <= 0)
      return;
    String[] ids = (String[])ht.keySet().toArray(new String[0]);
    try
    {
      getBillCardPanel().hCTTypeVO = SaleOrderBO_Client.getAllContractType(ids);
    }
    catch (Exception ex)
    {
      SCMEnv.out("获取合同信息失败!");
    }
  }

  private void ctlCurrencyEdit()
  {
    UIRefPane ccurrencytypeid = (UIRefPane)getBillCardPanel().getHeadItem("ccurrencytypeid").getComponent();

    getBillCardPanel().getHeadItem("nexchangeotoarate").setEnabled(true);
    try {
      if ((this.BD302 == null) || (!this.BD302.booleanValue()))
      {
        if (this.currtype.isLocalCurrType(ccurrencytypeid.getRefPK())) {
          getBillCardPanel().getHeadItem("nexchangeotobrate").setEnabled(false);
        }
        else {
          getBillCardPanel().getHeadItem("nexchangeotobrate").setEnabled(true);
        }

        getBillCardPanel().getHeadItem("nexchangeotoarate").setEnabled(false);
      }
      else if (this.currtype.isFracCurrType(ccurrencytypeid.getRefPK())) {
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
    catch (Exception e1)
    {
      SCMEnv.out("获得汇率失败！");
    }
  }

  public OrderBalanceCardUI getOrderBalanceUI()
  {
    if (this.orderBalanceUI == null)
      this.orderBalanceUI = new OrderBalanceCardUI(this);
    return this.orderBalanceUI;
  }

  public SaleReceiveUI getSaleReceiveUI() {
    if (this.saleReceiveUI == null)
      this.saleReceiveUI = new SaleReceiveUI(this);
    return this.saleReceiveUI;
  }

  private void onApproveCheck(SaleOrderVO saleorder)
    throws ValidationException
  {
    String salecorp = null;

    SaleorderBVO[] bodyVOs = saleorder.getBodyVOs();

    salecorp = saleorder.getHeadVO().getPk_corp();

    if ((salecorp == null) || (salecorp.trim().length() <= 0)) {
      salecorp = getCorpPrimaryKey();
    }
    boolean isBomOrder = false;

    int i = 0; for (int loop = bodyVOs.length; i < loop; i++) {
      SaleorderBVO oldbodyVO = bodyVOs[i];

      if ((!SoVoTools.isEmptyString(oldbodyVO.getCbomorderid())) && (!isBomOrder))
      {
        isBomOrder = true;
      }

      if ((oldbodyVO.getCconsigncorpid() != null) && (!oldbodyVO.getCconsigncorpid().equals(salecorp)))
      {
        if ((oldbodyVO.getCreccalbodyid() == null) || (oldbodyVO.getCreccalbodyid().trim().length() <= 0))
        {
          throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000202"));
        }

        if ((oldbodyVO.getBdericttrans() == null) || (!oldbodyVO.getBdericttrans().booleanValue()))
        {
          if ((oldbodyVO.getCrecwareid() == null) || (oldbodyVO.getCrecwareid().trim().length() <= 0))
          {
            throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000203"));
          }

        }

        if ((oldbodyVO.getLaborflag() != null) && (oldbodyVO.getLaborflag().booleanValue()))
        {
          throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000204"));
        }

        if ((oldbodyVO.getDiscountflag() != null) && (oldbodyVO.getDiscountflag().booleanValue()))
        {
          throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000205"));
        }

      }

    }

    SCMEnv.out("审核：" + getSouceBillType());
    SCMEnv.out("审核：51");

    if (!getSouceBillType().equals("51"))
      return;
    try
    {
      if (getBillType().equals("30"))
      {
        if ((isBomOrder) && (!SaleOrderBO_Client.isBomApproved(saleorder.getPrimaryKey())))
        {
          throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000240"));
        }

        if ((getBillCardPanel().getHeadItem("nexchangeotobrate").getValue() == null) || (getBillCardPanel().getHeadItem("nexchangeotobrate").getValue().equals("")))
        {
          throw new ValidationException(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000310"));
        }

        if ((this.BD302 != null) && (this.BD302.booleanValue()) && (
          (getBillCardPanel().getHeadItem("nexchangeotoarate").getValue() == null) || (getBillCardPanel().getHeadItem("nexchangeotoarate").getValue().equals(""))))
        {
          throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000200"));
        }

      }

      saleorder.validate();

      for (int j = 0; j < saleorder.getChildrenVO().length; j++) {
        SaleorderBVO oldbodyVO = (SaleorderBVO)saleorder.getChildrenVO()[j];

        if ((oldbodyVO.getCconsigncorpid() != null) && (!oldbodyVO.getCconsigncorpid().equals(salecorp)))
        {
          if ((oldbodyVO.getCreccalbodyid() == null) || (oldbodyVO.getCreccalbodyid().trim().length() <= 0))
          {
            throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000202"));
          }

          if ((oldbodyVO.getBdericttrans() == null) || (!oldbodyVO.getBdericttrans().booleanValue()))
          {
            if ((oldbodyVO.getCrecwareid() == null) || (oldbodyVO.getCrecwareid().trim().length() <= 0))
            {
              throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000203"));
            }

          }

          if ((oldbodyVO.getDiscountflag() != null) && (oldbodyVO.getDiscountflag().booleanValue()))
          {
            throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000205"));
          }

        }

        if ((oldbodyVO.getDiscountflag() != null) && (!oldbodyVO.getDiscountflag().booleanValue()))
        {
          if ((oldbodyVO.getNoriginalcurmny().doubleValue() < 0.0D) || (oldbodyVO.getNoriginalcurtaxmny().doubleValue() < 0.0D) || (oldbodyVO.getNoriginalcursummny().doubleValue() < 0.0D))
          {
            if (oldbodyVO.getNnumber().doubleValue() > 0.0D) {
              throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000209"));
            }

          }

          if (oldbodyVO.getNoriginalcurprice().doubleValue() < 0.0D) {
            throw new ValidationException(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000063"));
          }

        }
        else if ((oldbodyVO.getNnumber() == null) && (oldbodyVO.getNoriginalcurmny() == null) && (oldbodyVO.getNoriginalcursummny() == null))
        {
          throw new ValidationException(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000261"));
        }

        if ((oldbodyVO.getAssistunit() != null) && (oldbodyVO.getAssistunit().booleanValue()))
        {
          if ((oldbodyVO.getDiscountflag() != null) && (!oldbodyVO.getDiscountflag().booleanValue()))
          {
            if (oldbodyVO.getCpackunitid() == null) {
              throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000241"));
            }

            if (oldbodyVO.getNpacknumber() == null) {
              throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000242"));
            }

            if (oldbodyVO.getNpacknumber().doubleValue() * oldbodyVO.getNnumber().doubleValue() < 0.0D)
            {
              throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000243"));
            }

          }

        }

        UFDate dbilldate = new UFDate(getBillCardPanel().getHeadItem("dbilldate").getValue());

        UFDate dconsigndate = oldbodyVO.getDconsigndate();
        if ((dconsigndate != null) && (dbilldate != null) && 
          (dbilldate.after(dconsigndate)) && (dbilldate != dconsigndate))
        {
          throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000244"));
        }

        String oldinventoryid = oldbodyVO.getCinventoryid();

        if ((oldbodyVO.getBlargessflag() != null) && (!oldbodyVO.getBlargessflag().booleanValue()))
        {
          for (int w = i + 1; w < saleorder.getChildrenVO().length; w++) {
            SaleorderBVO newbodyVO = (SaleorderBVO)saleorder.getChildrenVO()[w];

            if (!this.SO_03.booleanValue()) {
              String newinventoryid = newbodyVO.getCinventoryid();
              if ((newbodyVO.getBlargessflag() != null) && (!newbodyVO.getBlargessflag().booleanValue()))
              {
                if (oldinventoryid.equals(newinventoryid)) {
                  throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000216", null, new String[] { i + 1 + "", w + 1 + "" }));
                }

              }

            }

          }

        }

      }

      SaleorderHVO header = (SaleorderHVO)saleorder.getParentVO();
      for (int q = 0; q < saleorder.getChildrenVO().length; q++) {
        SaleorderBVO saleorderitem = (SaleorderBVO)saleorder.getChildrenVO()[q];

        if ((saleorderitem != null) && (saleorderitem.getNtotalpaymny() != null))
        {
          if ((saleorderitem.getNsummny() != null) && (saleorderitem.getNtotalpaymny().doubleValue() < saleorderitem.getNsummny().doubleValue()))
          {
            throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000245"));
          }

          if ((header.getNsubscription() != null) && (saleorderitem.getNtotalpaymny().doubleValue() < header.getNsubscription().doubleValue()))
          {
            throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000246"));
          }

        }

      }

      SCMEnv.out("审核检测通过1");
    } catch (Throwable e) {
      throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000247"));
    }
  }

  protected void onCachPay()
  {
    try
    {
      SaleOrderVO ordvo = (SaleOrderVO)getVO(false);
      if (ordvo == null) {
        return;
      }
      SaleorderBVO[] bodyVOs = ordvo.getBodysNoInludeOOSLine();

      if (bodyVOs.length <= 0) {
        return;
      }

      UFDouble noriginalcursummny = SOBillCardTools.getCurSumMny(ordvo);
      if ((noriginalcursummny == null) || (noriginalcursummny.doubleValue() <= 0.0D))
      {
        showErrorMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000250"));

        return;
      }

      UFDouble nreceiptcathmny = ((SaleorderHVO)ordvo.getParentVO()).getNreceiptcathmny();

      UFDouble npreceivemny = ordvo.getHeadVO().getNpreceivemny();
      if ((npreceivemny != null) && (npreceivemny.doubleValue() > 0.0D) && 
        (nreceiptcathmny != null) && (nreceiptcathmny.doubleValue() >= npreceivemny.doubleValue()))
      {
        showErrorMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000251"));

        return;
      }

      if ((nreceiptcathmny != null) && 
        (nreceiptcathmny.doubleValue() >= noriginalcursummny.doubleValue()))
      {
        showErrorMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000252"));

        return;
      }

      getSaleReceiveUI().setOrdVO(ordvo);
      if (getCurShowPanel() == null)
        remove(getBillListPanel());
      else
        remove(getCurShowPanel());
      add(getSaleReceiveUI(), "Center");
      getSaleReceiveUI().initUI();
      setTitleText(getSaleReceiveUI().getTitle());

      showHintMessage("");

      setButtons(getSaleReceiveUI().getButtons());

      updateUI();
    } catch (Exception e) {
      returnFormSaleReceiveUI();
      this.saleReceiveUI = null;
      e.printStackTrace();
      SCMEnv.out(e.getMessage());
    }
  }

  protected void onOrderBalance() {
    try {
      SaleOrderVO ordvo = (SaleOrderVO)getVO(false);
      if (ordvo == null) {
        return;
      }

      int iallMinusMnyrow = 0;
      int iallMinusNumrow = 0;
      SaleorderBVO[] bodyVOs = ordvo.getBodysNoInludeOOSLine();

      if (bodyVOs.length <= 0) {
        return;
      }

      int i = 0; for (int loop = bodyVOs.length; i < loop; i++)
      {
        if ((bodyVOs[i].getNoriginalcursummny() != null) && (bodyVOs[i].getNoriginalcursummny().doubleValue() < 0.0D))
        {
          iallMinusMnyrow++;
        }
        if ((bodyVOs[i].getNnumber() != null) && (bodyVOs[i].getNnumber().doubleValue() < 0.0D))
        {
          iallMinusNumrow++;
        }
      }

      ordvo.setChildrenVO(bodyVOs);

      UFDouble noriginalcursummny = SOBillCardTools.getCurSumMny(ordvo);
      if ((noriginalcursummny == null) || (noriginalcursummny.doubleValue() <= 0.0D))
      {
        showErrorMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000250"));

        return;
      }
      getOrderBalanceUI().setOrdVO(ordvo);
      if (getCurShowPanel() == null)
        remove(getBillListPanel());
      else
        remove(getCurShowPanel());
      add(getOrderBalanceUI(), "Center");
      getOrderBalanceUI().initUI();
      setTitleText(getOrderBalanceUI().getTitle());

      showHintMessage("");

      ButtonObject[] btns = getOrderBalanceUI().getButtons();

      int e = 0; for (int len = btns.length; e < len; e++) {
        if ((btns[e].getCode().equals("列表显示")) || (btns[e].getCode().equals("浏览")) || (btns[e].getCode().equals("打印管理")))
        {
          btns[e].setVisible(false);
        }
      }

      setButtons(btns);

      updateUI();
    }
    catch (Exception e) {
      returnFormOrderBalanceUI();
      this.orderBalanceUI = null;
      SCMEnv.out(e.getMessage());
    }
  }

  public void returnFormOrderBalanceUI() {
    remove(getOrderBalanceUI());
    if (getCurShowPanel() == null)
      add(getBillListPanel(), "Center");
    else
      add(getCurShowPanel(), "Center");
    setTitleText(getTitle());
    setButtons();
    setButtonsState();
    getBillCardPanel().setHeadItem("nreceiptcathmny", getOrderBalanceUI().getNbalmny());

    int selrow = getBillListPanel().getHeadTable().getSelectedRow();
    if (selrow >= 0) {
      getBillListPanel().getHeadBillModel().setValueAt(getOrderBalanceUI().getNbalmny(), selrow, "nreceiptcathmny");

      SaleOrderVO ordvo = this.vocache.getSaleOrderVO((String)getBillListPanel().getHeadBillModel().getValueAt(selrow, "csaleid"));

      if (ordvo != null) {
        ordvo.getHeadVO().setNreceiptcathmny(getOrderBalanceUI().getNbalmny());
      }
    }

    updateUI();
  }

  public JComponent getCurShowPanel() {
    return this.curShowPanel;
  }

  public void returnFormSaleReceiveUI() {
    remove(getSaleReceiveUI());
    if (getCurShowPanel() == null)
      add(getBillListPanel(), "Center");
    else {
      add(getCurShowPanel(), "Center");
    }

    setTitleText(getTitle());
    setButtons();
    setButtonsState();

    if (getSaleReceiveUI().getNpaymny() != null) {
      Object dtemp = null;
      int selrow = getBillListPanel().getHeadTable().getSelectedRow();
      if ((selrow >= 0) && (selrow < getBillListPanel().getHeadBillModel().getRowCount()))
      {
        dtemp = getBillListPanel().getHeadBillModel().getValueAt(selrow, "nreceiptcathmny");

        if ((dtemp != null) && (dtemp.toString().trim().length() <= 0))
          dtemp = null;
        dtemp = SoVoTools.getMnyAdd((UFDouble)dtemp, getSaleReceiveUI().getNpaymny());

        getBillCardPanel().setHeadItem("nreceiptcathmny", dtemp);
        getBillListPanel().getHeadBillModel().setValueAt(dtemp, selrow, "nreceiptcathmny");

        SaleOrderVO ordvo = this.vocache.getSaleOrderVO((String)getBillListPanel().getHeadBillModel().getValueAt(selrow, "csaleid"));

        if (ordvo != null) {
          ordvo.getHeadVO().setNreceiptcathmny((UFDouble)dtemp);
        }
      }
    }

    updateUI();
  }

  public void freshTs(String sBillID, String sTS, Integer iPrintCount)
  {
    if ((sTS == null) || (sTS.trim().length() <= 0))
      return;
    if (this.strShowState.equals("列表")) {
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
    SaleOrderVO ordvo = this.vocache.getSaleOrderVO(sBillID);
    if (ordvo != null) {
      ordvo.getHeadVO().setIprintcount(iPrintCount);
      ordvo.getHeadVO().setTs(new UFDateTime(sTS.trim()));
    }
  }

  protected PrintLogClient getPrintLogClient()
  {
    if (this.printLogClient == null) {
      this.printLogClient = new PrintLogClient();
      this.printLogClient.addFreshTsListener(this);
    }
    return this.printLogClient;
  }

  private void showCustManArInfo(HashMap reths)
  {
    if (reths == null) {
      return;
    }
    UFDouble[] dvalues = (UFDouble[])reths.get("showCustManArInfo");
    if ((dvalues == null) || (dvalues.length < 5)) {
      return;
    }
    BillItem[] bis = new BillItem[5];

    bis[0] = getBillCardPanel().getTailItem("accawmny");

    bis[1] = getBillCardPanel().getTailItem("busawmny");

    bis[2] = getBillCardPanel().getTailItem("ordawmny");

    bis[3] = getBillCardPanel().getTailItem("creditmny");

    bis[4] = getBillCardPanel().getTailItem("creditmoney");

    int digit = getBillCardPanel().getBillData().getBodyItem("noriginalcursummny").getDecimalDigits();

    for (int i = 0; i < bis.length; i++) {
      if (bis[i] != null) {
        bis[i].setDecimalDigits(digit);
      }
    }

    for (int i = 0; i < bis.length; i++)
      if (bis[i] != null)
        bis[i].setValue(dvalues[i]);
  }

  private boolean isHeadCustCanbeModified()
  {
    int irowcount = getBillCardPanel().getRowCount();
    if (irowcount <= 0)
      return true;
    for (int irow = 0; irow < irowcount; irow++) {
      String sPk = (String)getBillCardPanel().getBodyValueAt(irow, "csaleid");

      String sBodyPk = (String)getBillCardPanel().getBodyValueAt(irow, "corder_bid");

      if ((sPk != null) && (sPk.trim().length() != 0) && (sBodyPk != null) && (sBodyPk.trim().length() != 0))
      {
        SaleOrderVO svo = this.vocache.getSaleOrderVO(sPk);

        if (svo != null)
        {
          SaleorderBVO[] bvos = svo.getBodyVOs();
          if ((bvos != null) && (bvos.length != 0))
          {
            for (int i = 0; i < bvos.length; i++)
              if (bvos[i].getCorder_bid().equals(sBodyPk)) {
                String[] sNames = { "ntotalreceiptnumber", "ntotalinvoicenumber", "ntotalinventorynumber", "ntotalshouldoutnum", "ntotalbalancenumber", "ntotalreturnnumber" };

                for (int j = 0; j < sNames.length; j++)
                  if ((bvos[i].getAttributeValue(sNames[j]) != null) && (((UFDouble)bvos[i].getAttributeValue(sNames[j])).doubleValue() != 0.0D))
                  {
                    return false;
                  }
                break;
              }
          }
        }
      }
    }
    return true;
  }

  protected void onOnHandShowHidden()
  {
    if (this.strShowState.equals("列表")) {
      onCard();
    }

    this.m_bOnhandShowHidden = (!this.m_bOnhandShowHidden);

    show(true, this.m_bOnhandShowHidden);
    if (this.m_bOnhandShowHidden) {
      freshOnhandnum(getBillCardPanel().getBillTable().getSelectedRow());
    }
    updateUI();
  }

  private void show(boolean bCardShow, boolean bSouthPanelShow)
  {
    getBillCardPanel().setVisible(bCardShow);
    getSplitPanelBc().setVisible(bCardShow);
    if (bCardShow)
      if (bSouthPanelShow) {
        if (getSplitPanelBc().getBottomComponent() == null) {
          getSplitPanelBc().add(getPnlSouth(this), "bottom");
        }

        if (getSplitPanelBc().getTopComponent() == null) {
          getSplitPanelBc().add(getBillCardPanel(), "top");
        }

        getSplitPanelBc().setDividerLocation((int)(getSplitPanelBc().getHeight() * 0.6800000000000001D));
      }
      else
      {
        if (getSplitPanelBc().getTopComponent() == null) {
          getSplitPanelBc().add(getBillCardPanel(), "top");
        }
        if (getSplitPanelBc().getBottomComponent() != null)
          getSplitPanelBc().remove(getPnlSouth(this));
        getSplitPanelBc().setDividerLocation((int)(getSplitPanelBc().getHeight() * 0.95D));
      }
  }

  public boolean onClosing()
  {
    if ((this.strState.equals("修改")) || (this.strState.equals("修订")) || (this.strState.equals("新增")))
    {
      int ireturn = MessageDialog.showYesNoCancelDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270"), NCLangRes.getInstance().getStrByID("common", "UCH001"));

      if (ireturn == 4) {
        if (onSave()) {
          return true;
        }
        return false;
      }if (ireturn == 2)
        return false;
    }
    return true;
  }

  private void setLineButtonStatus(boolean bstatus) {
    this.boLine.setEnabled(bstatus);
    this.boAddLine.setEnabled(bstatus);
    this.boDelLine.setEnabled(bstatus);
    this.boCopyLine.setEnabled(bstatus);
    this.boPasteLine.setEnabled(bstatus);
    getBillCardPanel().setBodyMenuShow(this.boLine.isEnabled());
    getBillCardPanel().getAddLineMenuItem().setEnabled(this.boAddLine.isEnabled());

    getBillCardPanel().getDelLineMenuItem().setEnabled(this.boDelLine.isEnabled());

    getBillCardPanel().getPasteLineMenuItem().setEnabled(this.boPasteLine.isEnabled());

    getBillCardPanel().getCopyLineMenuItem().setEnabled(this.boCopyLine.isEnabled());

    getBillCardPanel().getInsertLineMenuItem().setEnabled(this.boAddLine.isEnabled());

    getBillCardPanel().getPasteLineToTailMenuItem().setEnabled(this.boPasteLine.isEnabled());
  }

  public void doAddAction(ILinkAddData adddata)
  {
    try
    {
      if (PfUtilClient.isCloseOK()) {
        this.binitOnNewByOther = true;

        onNewByOther(PfUtilClient.getRetVos());

        for (int i = 0; i < getBillCardPanel().getRowCount(); i++)
          this.vRowATPStatus.add(new UFBoolean(false));
      }
      getBillCardPanel().showCustManArInfo();
      try
      {
        InvAttrCellRenderer ficr = new InvAttrCellRenderer();
        ficr.setFreeItemRenderer(getBillCardPanel(), getBillCardPanel().alInvs);
      }
      catch (Exception e)
      {
      }
      setBusitype();
    } catch (Exception e) {
      handleException(e);
    } finally {
      this.binitOnNewByOther = false;
    }
  }

  public void doApproveAction(ILinkApproveData approvedata)
  {
    if (this.strShowState == "列表") {
      onCard();
    }

    this.id = approvedata.getBillID();
    loadCardDataByID(this.id);
    setCardButtonsState();

    this.boDocument1.setTag("document");
    this.boAuditFlowStatus1.setTag("auditflowstatus");
    ButtonObject[] btns = { this.boDocument1, this.boAuditFlowStatus1, this.boAudit };
    this.boAuditFlowStatus1.setEnabled(true);
    this.boDocument1.setEnabled(true);
    this.boAudit.setEnabled(true);

    setButtons(btns);
  }

  public void doMaintainAction(ILinkMaintainData maintaindata)
  {
    if (this.strShowState == "列表") {
      onCard();
    }

    this.id = maintaindata.getBillID();
    loadCardDataByID(this.id);
    setBusitype();
  }

  public void doQueryAction(ILinkQueryData querydata)
  {
    if (this.strShowState == "列表") {
      onCard();
    }

    this.id = querydata.getBillID();
    loadCardDataByID(this.id, true);
    setCardButtonsState();

    Object pk_corp = getBillCardPanel().getHeadItem("pk_corp").getValueObject();

    if (getCorpPrimaryKey().equals(pk_corp))
      setButtons(getBillButtons());
    else
      setButtons(new ButtonObject[0]);
  }

  private void setBusitype()
  {
    String sBusitype = getBillCardPanel().getHeadItem("cbiztype").getValue();

    this.boBusiType.setTag(sBusitype);
    getBillCardPanel().setBusiType(sBusitype);
  }

  ClientEnvironment getClient()
  {
    return getClientEnvironment();
  }

  public AggregatedValueObject getVO(boolean needRemove)
  {
    SaleOrderVO saleorder = null;
    if (this.strShowState.equals("列表")) {
      int row = getBillListPanel().getHeadTable().getSelectedRow();

      if (row < 0) {
        return null;
      }
      saleorder = (SaleOrderVO)getBillListPanel().getBillValueVO(row, SaleOrderVO.class.getName(), SaleorderHVO.class.getName(), SaleorderBVO.class.getName());
    }
    else
    {
      saleorder = (SaleOrderVO)getBillCardPanel().getBillValueVO(SaleOrderVO.class.getName(), SaleorderHVO.class.getName(), SaleorderBVO.class.getName());

      ((SaleorderHVO)saleorder.getParentVO()).setCreceipttype("30");

      ((SaleorderHVO)saleorder.getParentVO()).setPrimaryKey(getBillID());

      UIRefPane vreceiveaddress = (UIRefPane)getBillCardPanel().getHeadItem("vreceiveaddress").getComponent();

      ((SaleorderHVO)saleorder.getParentVO()).setVreceiveaddress(vreceiveaddress.getUITextField().getText());
    }

    if (needRemove) {
      Vector vTemp = new Vector();
      SaleorderBVO[] itemVOs = (SaleorderBVO[])saleorder.getChildrenVO();
      int indexSelected = -1;
      if (this.strShowState.equals("列表")) {
        indexSelected = getBillListPanel().getBodyTable().getSelectedRow();
      }
      else {
        indexSelected = getBillCardPanel().getBillTable().getSelectedRow();
      }

      for (int i = 0; i < itemVOs.length; i++)
      {
        boolean notLabor = (itemVOs[i].getLaborflag() == null) || (!itemVOs[i].getLaborflag().booleanValue());

        boolean notDiscount = (itemVOs[i].getDiscountflag() == null) || (!itemVOs[i].getDiscountflag().booleanValue());

        if ((notLabor) && (notDiscount) && 
          (indexSelected > -1) && (i == indexSelected)) {
          vTemp.addElement(itemVOs[i]);
        }
      }

      SaleorderBVO[] itemsNew = new SaleorderBVO[vTemp.size()];
      vTemp.copyInto(itemsNew);
      saleorder.setChildrenVO(itemsNew);
    }

    SaleorderBVO[] itemVOs = (SaleorderBVO[])saleorder.getChildrenVO();
    int i = 0; for (int len = itemVOs.length; i < len; i++) {
      if (itemVOs[i].getPkcorp() == null) {
        itemVOs[i].setPkcorp(getCorpPrimaryKey());
      }

    }

    ((SaleorderHVO)saleorder.getParentVO()).setCapproveid(getClientEnvironment().getUser().getPrimaryKey());

    return saleorder;
  }

  public void bodyRowChange(BillEditEvent e)
  {
    if (e.getRow() == -1)
      return;
    super.bodyRowChange(e);
    if (getFuncExtend() != null) {
      try
      {
        getFuncExtend().rowchange(this, getBillCardPanel(), getBillListPanel(), 1, 0);
      }
      catch (Throwable ee)
      {
        SCMEnv.out(ee.getMessage());
      }
    }

    if (this.strState.equals("修订"))
    {
      if ((getBillCardPanel().getBillModel().getRowState(e.getRow()) == 1) || (!isHasBackwardDoc(e.getRow())))
      {
        this.boDelLine.setEnabled(true);
        getBillCardPanel().getDelLineMenuItem().setEnabled(true);
      }
      else {
        this.boDelLine.setEnabled(false);
        getBillCardPanel().getDelLineMenuItem().setEnabled(false);
      }

      updateButton(this.boDelLine);
    }
    if (this.strShowState.equals("列表")) {
      this.selectRow = e.getRow();
      String csaleid = (String)getBillListPanel().getHeadBillModel().getValueAt(this.selectRow, "csaleid");

      SaleOrderVO vo = this.vocache.getSaleOrderVO(csaleid);

      if ((vo == null) || (vo.getParentVO() == null)) {
        return;
      }
      if ((vo.getChildrenVO() == null) || (vo.getChildrenVO().length <= 0)) {
        getBillListPanel().loadBodyData(this.selectRow);
        getBillListPanel().getBodyBillModel().updateValue();
      } else {
        getBillListPanel().setListBodyByCurrency(vo.getBodyVOs()[0].getCcurrencytypeid());

        getBillListPanel().setBodyValueVO(vo.getChildrenVO());
        getBillListPanel().getBodyBillModel().updateValue();
      }
      setButtonsState();
    }
    else if (this.strShowState.equals("卡片"))
    {
      freshOnhandnum(e.getRow());
    }
  }

  public void reLoadListData()
  {
    if (this.strShowState.equals("列表")) {
      setBtnsByBillState(-1);
      getBillListPanel().reLoadData();
      fillCacheByListPanel();
      this.selectRow = -1;
      initIDs();
    }
  }

  protected String getSouceBillType()
  {
    String creceipttype = null;
    if (this.strShowState.equals("卡片")) {
      if (getBillCardPanel().getRowCount() > 0) {
        creceipttype = (String)getBillCardPanel().getBodyValueAt(0, "creceipttype");
      }

    }
    else if (getBillListPanel().getBodyBillModel().getRowCount() > 0) {
      creceipttype = (String)getBillListPanel().getBodyBillModel().getValueAt(0, "creceipttype");
    }

    if ((creceipttype == null) || (creceipttype.trim().equals("")))
      creceipttype = "NO";
    return creceipttype;
  }
}