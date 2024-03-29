package nc.ui.dm.dm108;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.itf.bgzg.pub.BGZGProxy;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.MapProcessor;
import nc.ui.bd.ref.busi.CustmandocDefaultRefModel;
import nc.ui.dm.dm102.GeneralMethod;
import nc.ui.dm.pub.DMBillStatus;
import nc.ui.dm.pub.DMQueryConditionDlg;
import nc.ui.dm.pub.ExceptionUITools;
import nc.ui.dm.pub.cardpanel.DMBillCardPanel;
import nc.ui.dm.pub.ref.TrancustRefModel;
import nc.ui.ml.NCLangRes;
import nc.ui.pf.query.ICheckRetVO;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.FramePanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListData;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.pub.linkoperate.ILinkType;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.scm.print.BillPrintTool;
import nc.ui.scm.pub.bill.IBillExtendFun;
import nc.ui.scm.pub.print.SCMPrintDirect;
import nc.vo.dm.dm104.FreightType;
import nc.vo.dm.dm108.DMInvoiceBodyDataVO;
import nc.vo.dm.dm108.DMInvoiceHeadDataVO;
import nc.vo.dm.dm108.ValueRangeHashtableInvoiceBody;
import nc.vo.dm.pub.DMBillNodeCodeConst;
import nc.vo.dm.pub.DMBillTypeConst;
import nc.vo.dm.pub.DMDataVO;
import nc.vo.dm.pub.DMVO;
import nc.vo.dm.pub.VOCheck;
import nc.vo.dm.pub.tools.FormulaTools;
import nc.vo.ep.dj.DJZBHeaderVO;
import nc.vo.ep.dj.DJZBItemVO;
import nc.vo.ep.dj.DJZBVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.constant.CConstant;
import nc.vo.scm.pub.session.ClientLink;

/**
 * 运费发票界面类 作者：毕晖
 * 
 * @version 最后修改日期(2002-8-14 10:48:50)
 * @see 需要参见的其它类
 * @since 从产品的那一个版本，此类被添加进来。（可选） 修改人 + 修改日期 修改说明
 */
public class ClientUI extends nc.ui.dm.pub.ClientUIforBill implements
    ICheckRetVO, IBillExtendFun, ILinkQuery {

  private boolean bFromDelivbill = false; // 是否来源于发运单

  private ButtonObject boDelivBill;// 查询发运单
  //eric 2012-8-10
  private ButtonObject boBatchEditPrice;

  // private ButtonObject boAction; //执行

  private ButtonObject boVerify; // 核销

  private ButtonObject boVerifyBack; // 反核销--zhy

  // private nc.ui.pub.formulaset.FormulaSetParUI ivjFormulaSetParUI = null;
  // //公式

  // private String m_CheckFormula = null;

  private ChooseDeliveBillDLG m_ChooseDeliveBillDLG; // 选择发运单DLG

  private ClientLink m_Cl = null;// zhy

  // private int CARDVIEW = 0;
  // private int CARDEDIT = 1;
  // private int CARDAUDIT = 2;
  // private int CARDNEW = 3;
  // private int ListView = 4;

  // 发运单查询
  private QueryConditionClient m_condClient = null;

  private DMVO m_dvosave = new DMVO();

  // private FormuSetVO m_FormSetVO = new FormuSetVO();

  // private int m_iRowCounts = 0; //行数

  private DMQueryConditionDlg m_QueryVerifyBackDlg; // 反核销查询DLG--zhy

  // 记录所选发运单表体IDs，用于和运费发票自动核销
  private String[] m_sDelivbillbs = null;

  //
  private String m_sPkCorp = null;

  private VerifyBackDLG m_VerifyBackDLG; // 反核销DLG--zhy

  private VerifyDLG m_VerifyDLG; // 核销DLG

  private DMVO[] m_VOs;

  private String m_conds;
  
  private String billType=new String();

  // 节点由何处触发打开，默认为从节点树正常打开
  private int opentype = ILinkType.NONLINK_TYPE;

  public ClientUI(
      FramePanel fp) {
    this.opentype = fp.getLinkType();
  }

  protected String checkPrerequisite() {
    // 非联查打开节点
    if (this.opentype != ILinkType.LINK_TYPE_QUERY) {
      try {
        initializeNew();
      }
      catch (Error ex) {
        ex.printStackTrace();
        return ex.getMessage();
      }
    }
    return null;
  }

  /*
   * hxb modified at 2006/08/17 9:06 实现审核按钮的可否点击切换
   */
  public void SwitchOnAudit() {
    if (getShowState().equals(DMBillStatus.List)) {
      return;
    }
    else if (getShowState().equals(DMBillStatus.Card)) {
      DMVO dvo = (DMVO) getBillCardPanel().getBillValueVO("nc.vo.dm.pub.DMVO",
          "nc.vo.dm.pub.DMDataVO", "nc.vo.dm.pub.DMDataVO");
      if (dvo.getParentVO().getAttributeValue("pkapprperson") != null
          && ((String) dvo.getParentVO().getAttributeValue("pkapprperson"))
              .trim().length() != 0) {
        UpdateButtonOnAudit(false);
      }
      else {
        UpdateButtonOnAudit(true);
      }
    }
  }

  public void UpdateButtonOnAudit(boolean b) {
    if (b) {
      setButton(boAudit, true);
      setButton(boCancelAudit, false);
    }
    else if (!b) {
      setButton(boCancelAudit, true);
      setButton(boAudit, false);
    }
  }

  /**
   * @return
   */
  /**
   * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2002-11-13 18:51:36) 修改日期，修改人，修改原因，注释标志：
   */
  public void afterDept(nc.ui.pub.bill.BillEditEvent e) {
    UIRefPane pkoperator = (UIRefPane) getBillCardPanel().getHeadItem(
        "pkoperator").getComponent();
    UIRefPane pkoprdepart = (UIRefPane) getBillCardPanel().getHeadItem(
        "pkoprdepart").getComponent();
    if (e.getValue() != null && e.getValue().toString().length() != 0) {
      pkoperator.getRefModel().setWherePart(
          "bd_psndoc.pk_deptdoc ='" + pkoprdepart.getRefPK() + "'");
    }
    else {
      pkoperator.setWhereString("1=1");
    }
  }

  /**
   * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2002-11-13 18:51:36) 修改日期，修改人，修改原因，注释标志：
   */
  public void afterTrancust(nc.ui.pub.bill.BillEditEvent e) {
    UIRefPane refTrancust = (UIRefPane) getBillCardPanel().getHeadItem(
        "pk_transcust").getComponent();
    UIRefPane refCustInvoice = (UIRefPane) getBillCardPanel().getHeadItem(
        "pkcustinvoice").getComponent();
    UIComboBox cbSendtype = (UIComboBox) getBillCardPanel().getHeadItem(
        "isendtype").getComponent();
    if (cbSendtype.getSelectedIndex() == 0) {
      refCustInvoice.setPK(refTrancust.getRefModel().getValue(
          "bd_cumandoc.pk_cumandoc"));
    }
    else {
      refCustInvoice.setPK(null);
    }

  }

  /**
   * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2002-11-13 18:51:36) 修改日期，修改人，修改原因，注释标志：
   */
  public void afterTransfeetype(nc.ui.pub.bill.BillEditEvent e) {
    if (e.getValue().toString().equals(FreightType.sNone)) {
      getBillCardPanel().getHeadItem("pk_transcust").setValue(null);
      getBillCardPanel().getHeadItem("pk_transcust").setEnabled(false);

      getBillCardPanel().getHeadItem("pkcustinvoice").setValue(null);
      getBillCardPanel().getHeadItem("pkcustinvoice").setEnabled(false);
      getBillCardPanel().getHeadItem("vcustinvoicename").setValue(null);
      getBillCardPanel().getHeadItem("pkcustinvoicebas").setValue(null);
    }
    else if (e.getValue().toString().equals(FreightType.sAr)) {
      getBillCardPanel().getHeadItem("pk_transcust").setValue(null);
      getBillCardPanel().getHeadItem("pk_transcust").setEnabled(false);

      getBillCardPanel().getHeadItem("pkcustinvoice").setEnabled(true);
    }
    else if (e.getValue().toString().equals(FreightType.sAp)) {
      getBillCardPanel().getHeadItem("pk_transcust").setEnabled(true);

      getBillCardPanel().getHeadItem("pkcustinvoice").setValue(null);
      getBillCardPanel().getHeadItem("pkcustinvoice").setEnabled(false);
      getBillCardPanel().getHeadItem("vcustinvoicename").setValue(null);
      getBillCardPanel().getHeadItem("pkcustinvoicebas").setValue(null);
    }
  }

  /**
   * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2002-11-13 18:51:36) 修改日期，修改人，修改原因，注释标志：
   */
  public void afterDept() {

    UIRefPane pkoperator = (UIRefPane) getBillCardPanel().getHeadItem(
        "pkoperator").getComponent();
    UIRefPane pkoprdepart = (UIRefPane) getBillCardPanel().getHeadItem(
        "pkoprdepart").getComponent();

    pkoperator.setWhereString("bd_psndoc.pk_deptdoc ='"
        + pkoprdepart.getRefPK() + "'");
  }

  /**
   * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2002-11-13 18:51:36) 修改日期，修改人，修改原因，注释标志：
   */
  public void afterOperator(nc.ui.pub.bill.BillEditEvent e) {
    if (e.getValue() != null && e.getValue().toString().trim().length() != 0) {
      UIRefPane pkoperator = (UIRefPane) getBillCardPanel().getHeadItem(
          "pkoperator").getComponent();
      getBillCardPanel().setHeadItem("pkoprdepart",
          pkoperator.getRefValue("bd_psndoc.pk_deptdoc"));
    }
    // UIRefPane pkoprdepart = (UIRefPane)
    // getBillCardPanel().getHeadItem("pkoprdepart").getComponent();
    // if (e.getValue() != null && e.getValue().toString().length() != 0) {
    // pkoperator.setWhereString("bd_psndoc.pk_deptdoc ='" +
    // pkoprdepart.getRefPK() + "'");
    // }
    // else {
    // pkoperator.setWhereString(null);
    // }
  }

  /**
   * 编辑后事件。 创建日期：(2001-3-23 2:02:27)
   * 
   * @param e
   *          ufbill.BillEditEvent
   */
  public void afterEdit(nc.ui.pub.bill.BillEditEvent e) {
    if (e.getPos() == BillItem.HEAD) {
      if (e.getKey().equals("pkoprdepart"))
        afterDept(e);
      else if (e.getKey().equals("pkoperator"))
        afterOperator(e);
      else if (e.getKey().equals("pk_transcust"))
        afterTrancust(e);
      else if (e.getKey().equals("isendtype"))
        afterTransfeetype(e);
     
    }
  }

  /**
   * 此处插入方法说明。 创建日期：(2002-5-16 20:33:00)
   */
  public void afterQuery(nc.vo.pub.query.ConditionVO[] voCons) {

    String sWhereSql = getQueryConditionDlg().getWhereSQL(voCons);
    sWhereSql = "dm_delivinvoice_h.pk_delivinvoice_h in (select dm_delivinvoice_h.pk_delivinvoice_h from dm_delivinvoice_h inner join dm_delivinvoice_b on dm_delivinvoice_h.pk_delivinvoice_h=dm_delivinvoice_b.pk_delivinvoice_h "
        + (sWhereSql == null || sWhereSql.trim().length() == 0 ? "" : " where "
            + sWhereSql) + ")";

    // 保存查询条件
    m_conds = sWhereSql;

    // 将数据载入列表

    m_alAllVOs = new ArrayList();

    try {
      loadBillData(sWhereSql);
    }
    catch (Exception ex) {
      ExceptionUITools tool = new ExceptionUITools();
      tool.showMessage(ex, this);
      return;
    }

    if (m_strShowState.equals(DMBillStatus.Card)) {
      onSwith();
    }
    getBillCardPanel().getBillModel().execLoadFormula();
    getBillListPanel().getHeadBillModel().execLoadFormula();

  }

  /**
   * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2002-8-31 10:04:54) 修改日期，修改人，修改原因，注释标志：
   */
  private boolean autoVerify(DMVO dvo) {
    // 自动核销

    DMDataVO[] bodyvos = (DMDataVO[]) dvo.getBodyVOs();

    UFDouble dTotalDelivFee = (UFDouble) dvo.getParentVO().getAttributeValue(
        "totalfee"); // 未核销运费
    UFDouble dTaxmoneySum = new UFDouble(0);
    UFDouble dTaxmoney = new UFDouble(0);

    for (int i = 0; i < bodyvos.length; i++) {
      dTaxmoney = (UFDouble) bodyvos[i].getAttributeValue("dtaxmoney");
      dTaxmoneySum = dTaxmoneySum.add(dTaxmoney);
    }

    if (dTotalDelivFee.doubleValue() > dTaxmoneySum.doubleValue()) {
      showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40140418",
          "UPP40140418-000009")/* @res "发运单未核销运费总和应不大于发票含税金额总和！" */);
      // sDelivBillPK= null;
      return false;
    }
    else {

      for (int i = 0; i < bodyvos.length; i++) {

        dTaxmoney = (UFDouble) bodyvos[i].getAttributeValue("dtaxmoney");

        if (dTotalDelivFee.doubleValue() >= dTaxmoney.doubleValue()) {
          bodyvos[i].setAttributeValue("dtranmoney", dTaxmoney);
          dTotalDelivFee = dTotalDelivFee.sub(dTaxmoney);
        }
        else {
          bodyvos[i].setAttributeValue("dtranmoney", dTotalDelivFee);
          dTotalDelivFee = new UFDouble(0);
        }
      }

      // 重置未核销运费

      dvo.getParentVO().setAttributeValue("totalfee", dTotalDelivFee);
    }

    return true;
  }

  /**
   * 行改变事件。 创建日期：(2001-3-23 2:02:27)
   * 
   * @param e
   *          ufbill.BillEditEvent
   */
  public void bodyRowChange(nc.ui.pub.bill.BillEditEvent e) {
    if (getShowState() == DMBillStatus.List) {
      m_selectRow = e.getRow();
      loadListBodyData();
    }
    // if (getShowState() == DMBillStatus.Card) {
    // m_selectRow= e.getRow();
    // }
  }

  /**
   * 创建者：zxping 功能：由于公共代码的自定义项序号为 0~19，而该单据的自定义项序号是 1~20，故需要特殊处理。 参数： 返回： 例外：
   * 日期：(2001-11-8 19:47:29) 修改日期，修改人，修改原因，注释标志：
   * 
   * @return nc.ui.pub.bill.BillData
   * @param oldBillData
   *          nc.ui.pub.bill.BillData
   */
  protected BillData changeBillDataByUserDef(BillData oldBillData) {
    if (getDefVOsH() != null) {
      updateItemByDef(oldBillData, getDefVOsH(), "vuserdef", true, 1);
    }
    if (getDefVOsB() != null) {
      updateItemByDef(oldBillData, getDefVOsB(), "vuserdef", false, 1);
    }
    return oldBillData;
  }

  /**
   * 创建者：zxping 功能：由于公共代码的自定义项序号为 0~19，而该单据的自定义项序号是 1~20，故需要特殊处理。 参数： 返回： 例外：
   * 日期：(2001-11-8 19:47:29) 修改日期，修改人，修改原因，注释标志：
   * 
   * @return nc.ui.pub.bill.BillData
   * @param oldBillData
   *          nc.ui.pub.bill.BillData
   */
  protected BillListData changeBillListDataByUserDef(BillListData oldBillData) {
    if (getDefVOsH() != null) {
      updateItemByDef(oldBillData, getDefVOsH(), "vuserdef", true, 1);
    }
    if (getDefVOsB() != null) {
      updateItemByDef(oldBillData, getDefVOsB(), "vuserdef", false, 1);
    }
    return oldBillData;
  }

  /**
   * 修改检查。 功能： 参数： 返回： 例外： 日期：(2002-9-1 18:21:30) 修改日期，修改人，修改原因，注释标志：
   */
  public boolean checkEdit(DMVO dvo) {

    DMDataVO[] bodyvos = (DMDataVO[]) dvo.getChildrenVO();

    UFDouble dTranmoney = new UFDouble(0);
    UFDouble dTaxmoney = new UFDouble(0);
    Object oTranmoney = null;
    Object oTaxmoney = null;

    for (int i = 0; i < bodyvos.length; i++) {

      oTranmoney = bodyvos[i].getAttributeValue("dtranmoney"); // 核销金额
      oTaxmoney = bodyvos[i].getAttributeValue("dtaxmoney"); // 含税金额
      dTranmoney = (oTranmoney == null ? new UFDouble(0)
          : (UFDouble) oTranmoney);
      dTaxmoney = (oTaxmoney == null ? new UFDouble(0) : (UFDouble) oTaxmoney);

      if (dTaxmoney.doubleValue() < dTranmoney.doubleValue()) {
        showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "40140418", "UPP40140418-000010")/* @res "含税金额不应小于核销金额！" */);
        return false;
      }
    }

    return true;
  }

  /**
   * 此处插入方法说明。 创建日期：(2002-6-27 16:16:11)
   */
  public boolean checkOther(AggregatedValueObject dvo) throws Exception {

    // 校验不可小于零表体项

    String[] itemkeys = {
        "dinvnum", "dunitprice", "dmoney" /* "dtaxfee" */
    };
    String[] itemnames = {
        nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC000-0001464")/*
                                                                               * @res
                                                                               * "存货数量"
                                                                               */,
        nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC000-0002306")/*
                                                                               * @res
                                                                               * "无税单价"
                                                                               */,
        nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC000-0002307")
    /* @res "无税金额" */
    /*
     * nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC000-0003084")
     * /*@res "税额"
     */};

    for (int i = 0; i < itemkeys.length; i++) {
      VOCheck.checkMustGreaterThanZeroInput(dvo.getChildrenVO(), itemkeys[i],
          itemnames[i]);
    }

    // 校验含税金额大于零

    VOCheck
        .checkMustGreaterThanZeroInput(dvo.getChildrenVO(), "dtaxmoney",
            nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
                "UC000-0001162")/* @res "含税金额" */);

    return true;
  }

  /**
   * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2002-8-21 17:23:09) 修改日期，修改人，修改原因，注释标志：
   */
  public boolean checkVO(DMVO vo) {
    String sTransfeeType = getBillCardPanel().getHeadItem("isendtype")
        .getValue();
    if (sTransfeeType == null || sTransfeeType.trim().length() == 0
        || sTransfeeType.equals("0")) {
      showErrorMessage("运费类别不可为空！");
      return false;
    }
    int isendtype = Integer.valueOf(sTransfeeType);
    switch (isendtype) {
      case FreightType.ap:
        String pk_transcust = getBillCardPanel().getHeadItem("pk_transcust")
            .getValue();
        if (pk_transcust == null || pk_transcust.length() == 0) {
          showErrorMessage("承运商不可为空");
          return false;
        }
        break;
      case FreightType.ar:
        String pkcustinvoice = getBillCardPanel().getHeadItem("pkcustinvoice")
            .getValue();
        if (pkcustinvoice == null || pkcustinvoice.length() == 0) {
          showErrorMessage("开票客户不可为空");
          return false;
        }
        break;
    }
    return super.checkVO(vo, (DMBillCardPanel) getBillCardPanel());
  }

  /**
   * 生成应付单。 功能： 参数： 返回： 例外： 日期：(2002-9-4 20:16:54) 修改日期，修改人，修改原因，注释标志：
   */
  public DJZBVO genArapBill(DMVO vo) {
    DJZBVO aparvo = null;
    try {
      // VO转换
      aparvo = (DJZBVO) nc.ui.pub.change.PfChangeBO_Client.pfChangeBillToBill(
          vo, "7V", "D1");

    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return aparvo;
  }

  /**
   * 生成应付单VO。 功能： 参数： 返回： 例外： 日期：(2002-9-4 20:16:54) 修改日期，修改人，修改原因，注释标志：
   */
  public DJZBVO genArapVO(DMVO vo) {

    // 客商基础档案ID
    String pk_trancust = (String) vo.getParentVO().getAttributeValue(
        "pk_transcust"); // 承运商
    String formula = "getColValue(dm_trancust,pkcusmandoc,pk_trancust," + "\""
        + pk_trancust + "\")";
    String pk_cubasdoc = (String) getBillCardPanel().execHeadFormula(formula);

    DJZBVO aparvo = new DJZBVO();

    // 表头
    DJZBHeaderVO headvo = new DJZBHeaderVO();
    headvo.setAttributeValue("qcbz", new UFBoolean(false));
    headvo.setAttributeValue("lybz", new Integer(4));
    headvo.setAttributeValue("djzt", new Integer(1));
    headvo.setAttributeValue("prepay", new UFBoolean(false));
    // headvo.setAttributeValue("ywbm", "dm"); //????单据类型oid
    headvo.setAttributeValue("djlxbm", "D1"); //
    headvo.setAttributeValue("pzglh", new Integer(1));
    headvo.setAttributeValue("djdl", "yf"); // 单据大类
    headvo.setAttributeValue("djrq", vo.getParentVO().getAttributeValue(
        "dinvoicedate")); // 发票日期
    headvo.setAttributeValue("dwbm", vo.getParentVO().getAttributeValue(
        "pk_corp"));
    // headvo.setAttributeValue("hbbm", pk_cubasdoc); //客商基本档案
    // headvo.setAttributeValue("ksbm_cl", pk_cubasdoc); //客商管理档案
    headvo.setAttributeValue("lrr", getOperator()); // 录入人
    // headvo.setAttributeValue("bzbm", "00010000000000000001"); //???币种编码

    // 表体
    DMDataVO[] bvos = (DMDataVO[]) vo.getChildrenVO();
    DJZBItemVO[] bodyvos = new DJZBItemVO[bvos.length];

    for (int i = 0; i < bvos.length; i++) {

      // 计算含税单价
      Object otax = bvos[i].getAttributeValue("dtax");
      Object oprice = bvos[i].getAttributeValue("dunitprice");
      UFDouble dtax = (otax == null ? new UFDouble(0) : new UFDouble(otax
          .toString()));
      UFDouble dprice = (oprice == null ? new UFDouble(0) : new UFDouble(oprice
          .toString()));
      UFDouble ddaxprice = dprice.multiply((dtax.add(new UFDouble(1))));

      bodyvos[i] = new DJZBItemVO();
      bodyvos[i].setAttributeValue("ddlx", bvos[i]
          .getAttributeValue("pk_delivinvoice_h")); // 来源单据id
      bodyvos[i].setAttributeValue("ddhh", bvos[i]
          .getAttributeValue("pk_delivinvoice_b")); // 来源单据行id
      bodyvos[i].setAttributeValue("old_sys_flag", new UFBoolean(false));
      bodyvos[i].setAttributeValue("fx", new Integer(-1));
      bodyvos[i].setAttributeValue("wldx", new Integer(1));
      bodyvos[i].setAttributeValue("kslb", new Integer(1)); // ??扣税类别
      bodyvos[i].setAttributeValue("dfybje", bvos[i]
          .getAttributeValue("dtaxmoney")); // ???贷方原币金额
      bodyvos[i].setAttributeValue("dffbje", new UFDouble(0)); // ??贷方辅币金额
      bodyvos[i].setAttributeValue("dfbbje", bvos[i]
          .getAttributeValue("dtaxmoney")); // ??贷方本币金额
      bodyvos[i].setAttributeValue("ybye", bvos[i]
          .getAttributeValue("dtaxmoney")); // ??原币余额
      bodyvos[i].setAttributeValue("fbye", new UFDouble(0)); // ??辅币余额
      bodyvos[i].setAttributeValue("bbye", bvos[i]
          .getAttributeValue("dtaxmoney")); // ??本币余额
      bodyvos[i].setAttributeValue("dj", bvos[i]
          .getAttributeValue("dunitprice")); // 无税单价
      bodyvos[i].setAttributeValue("sl", bvos[i].getAttributeValue("dtax")); // 税率
      bodyvos[i].setAttributeValue("dfshl", bvos[i]
          .getAttributeValue("dinvnum")); // 贷方数量
      bodyvos[i].setAttributeValue("deptid", vo.getParentVO()
          .getAttributeValue("pkoprdepart")); // 部门
      bodyvos[i].setAttributeValue("ywybm", vo.getParentVO().getAttributeValue(
          "pkoperator")); // 业务员
      bodyvos[i].setAttributeValue("sfkxyh", vo.getParentVO()
          .getAttributeValue("pkarap")); // 收付款协议
      System.out.println("--------------get inv="
          + bvos[i].getAttributeValue("pkinv"));
      bodyvos[i].setAttributeValue("chbm_cl", bvos[i]
          .getAttributeValue("pkinv")); // 存货管理档案
      bodyvos[i].setAttributeValue("cinventoryid", bvos[i]
          .getAttributeValue("pkbasinv")); // 存货基本档案

      bodyvos[i].setAttributeValue("hsdj", ddaxprice); // 含税税单价
      bodyvos[i].setAttributeValue("dfybsj", bvos[i]
          .getAttributeValue("dtaxfee")); // 贷方原币税金
      bodyvos[i].setAttributeValue("dfbbsj", bvos[i]
          .getAttributeValue("dtaxfee")); // 贷方本币税金
      bodyvos[i].setAttributeValue("dffbsj", new UFDouble(0)); // 贷方辅币税金
      bodyvos[i].setAttributeValue("dfybwsje", bvos[i]
          .getAttributeValue("dmoney")); // 贷方原币无税金额
      bodyvos[i].setAttributeValue("dfbbwsje", bvos[i]
          .getAttributeValue("dmoney")); // 贷方本币无税金额
      bodyvos[i].setAttributeValue("wbffbje", new UFDouble(0)); // 贷方辅币无税金额
      bodyvos[i].setAttributeValue("jsfsbm", "7V"); // 上层来源单据类型

      bodyvos[i].setAttributeValue("bbhl", new UFDouble(1)); // 本币汇率

      bodyvos[i].setAttributeValue("hbbm", pk_cubasdoc); // 客商基本档案
      // bodyvos[i].setAttributeValue("ksbm_cl", pk_cumandoc); //客商管理档案
      bodyvos[i].setAttributeValue("ksbm_cl", pk_cubasdoc); // 客商管理档案
    }
    aparvo.setParentVO(headvo);
    aparvo.setChildrenVO(bodyvos);
    return aparvo;
  }

  /**
   * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2002-8-9 16:27:02) 修改日期，修改人，修改原因，注释标志：
   */
  public DMVO getChooseDeliveBillDLG(String sWhere) {

    if (m_ChooseDeliveBillDLG == null) {
      m_ChooseDeliveBillDLG = new ChooseDeliveBillDLG(sWhere, this);
    }
    else {
      m_ChooseDeliveBillDLG.setWhere(sWhere);
    }

    if (m_ChooseDeliveBillDLG.showModal() == UIDialog.ID_OK) {
      DMVO dmvo = m_ChooseDeliveBillDLG.getReturnOrderData();
      return dmvo;
    }

    return null;
  }

  /**
   * @author 毕晖 函数的功能、用途、对属性的更改，以及函数执行前后对象的状态。
   * @param 参数说明
   * @return 返回值
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来。（可选） //
   * @wdeprecated 该方法从类的那一个版本后，已经被其它方法替换。（可选） (2002-12-23 9:43:53)
   * @return java.lang.String
   */
  public String getDelivCorp() {
    // 需要修改//
    /***************************************************************************
     * 原来的****** String formula=
     * "pk_corp->getColValue(bd_delivorg,pkcorp,pk_delivorg,pkdelivorg)"; String
     * pkcorp= (String) getBillCardPanel().execHeadFormula(formula); return
     * pkcorp; 原来的
     **************************************************************************/

    // 修改为：定义全局变量m_sPkCorp
    if (m_sPkCorp == null) {
      String formula = "pk_corp->getColValue(bd_delivorg,pkcorp,pk_delivorg,pkdelivorg)";
      Object o = getBillCardPanel().execHeadFormula(formula);
      m_sPkCorp = o == null ? null : o.toString();
    }

    return m_sPkCorp;
  }

  /**
   * 得到环境变量 创建日期：(2004-11-08 10:22:32)
   */
  private ClientLink getEnvironment() {
    if (m_Cl == null)
      try {
        m_Cl = new ClientLink(ClientEnvironment.getInstance());
      }
      catch (Exception e) {
        reportException(e);
        if (e instanceof BusinessException)
          MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
              .getStrByID("SCMCOMMON", "UPPSCMCommon-000059")/* @res "错误" */,
              e.getMessage());
      }
    return m_Cl;
  }

  /**
   * 此处插入方法说明。 功能：反核销dlg 参数： 返回： 例外： 日期：(2002-8-9 16:27:02) 修改日期，修改人，修改原因，注释标志：
   */
  public DMQueryConditionDlg getQueryDlg() {
    if (m_QueryVerifyBackDlg == null) {
      m_QueryVerifyBackDlg = new DMQueryConditionDlg(this);
      m_QueryVerifyBackDlg.setTempletID(getEnvironment().getCorp(), "40140488",
          getEnvironment().getUser(), null);
      m_QueryVerifyBackDlg.setAutoClear(false);
      m_QueryVerifyBackDlg.setIsWarningWithNoInput(true);
      m_QueryVerifyBackDlg.hideNormal();

      // 核销日期到：设置默认值
      m_QueryVerifyBackDlg.setInitDate("dm_verifyrelation_h.dverifydate1",
          getClientEnvironment().getDate().toString());

      // 设置承运商参照的默认发运组织
      UIRefPane rfpTranCust = new UIRefPane();
      nc.ui.dm.pub.ref.TrancustRefModel rfm = new nc.ui.dm.pub.ref.TrancustRefModel();
      rfm.setDelivOrgPK(getDelivOrgPK());
      rfpTranCust.setRefModel(rfm);
      m_QueryVerifyBackDlg.setValueRef("dm_verifyrelation_h.pktrancust",
          rfpTranCust);
    }
    return m_QueryVerifyBackDlg;
  }

  /**
   * ?user> 功能： 参数： 返回： 例外： 日期：(2004-8-31 16:33:44) 修改日期，修改人，修改原因，注释标志：
   */
  private String getReplacedSQL(String sSource, String sOld, String sReplace) {
    if (sReplace == null || sReplace.trim().length() == 0)
      return sSource;

    int nStart = sSource.indexOf(sOld);
    if (nStart < 0)
      return sSource;
    int nMiddle = sSource.indexOf("'", nStart + 1);
    if (nMiddle < 0)
      return sSource;
    int nEnd = sSource.indexOf("'", nMiddle + 1);

    String s1 = sSource.substring(0, nStart);
    String s2 = sSource.substring(nEnd + 1);

    String s = s1 + sReplace + s2;

    return s;
  }

  /**
   * ?user> 功能： 参数： 返回： 例外： 日期：(2004-8-31 16:33:44) 修改日期，修改人，修改原因，注释标志：
   */
  private String getSQL(ConditionVO conditionVO[]) {
    String sCondition = "";
    String pk_corp = null;
    for (int i = 0; i < conditionVO.length; i++) {
      String sName = conditionVO[i].getFieldCode().trim();
      String sValue = conditionVO[i].getValue();
      if (sName.equals("bd_corp.pk_corp") && sValue != null
          && sValue.trim().length() > 0) {
        // 公司
        pk_corp = sValue;
      }
    }

    for (int i = 0; i < conditionVO.length; i++) {
      String sName = conditionVO[i].getFieldCode().trim();
      String sOpera = conditionVO[i].getOperaCode().trim();
      String sValue = conditionVO[i].getValue();
      String sSQL = conditionVO[i].getSQLStr();
      String sReplace = null;

      if (sName.equals("dm_delivbill_h.pkdelivorg") && sValue != null
          && sValue.trim().length() > 0) {
        // 发运组织
        sReplace = "pkdelivorg " + sOpera + " '" + sValue + "'";
        String s = getReplacedSQL(sSQL, sName, sReplace);
        sCondition += s;

      }
      else if ((sName.equals("dm_delivbill_h.senddate") || sName
          .equals("dm_delivbill_h.senddate1"))
          && sValue != null && sValue.trim().length() > 0) {
        // 日期区间
        sReplace = "senddate " + sOpera + " '" + sValue + "'";
        String s = getReplacedSQL(sSQL, sName, sReplace);
        sCondition += s;

      }
      else if (sName.equals("dm_delivbill_h.pkdelivmode") && sValue != null
          && sValue.trim().length() > 0) {
        // 发运方式
        sReplace = "pk_delivbill_h in (select distinct pk_delivbill_h from dm_delivbill_h where dr = 0 and pkdelivmode "
            + sOpera + " '" + sValue + "')";
        String s = getReplacedSQL(sSQL, sName, sReplace);
        sCondition += s;

      }
      else if (sName.equals("dm_delivbill_h.pktrancust") && sValue != null
          && sValue.trim().length() > 0) {
        // 承运商
        sReplace = "pktrancust " + sOpera + " '" + sValue + "'";
        String s = getReplacedSQL(sSQL, sName, sReplace);
        sCondition += s;

      }
      else if (sName.equals("bd_invbasdoc.pk_invbasdoc") && sValue != null
          && sValue.trim().length() > 0) {
        // 存货
        if (pk_corp != null && pk_corp.trim().length() > 0) {
          sReplace = "pk_delivbill_b in (select pk_delivbill_b from dm_delivbill_b A, bd_invmandoc B, bd_invbasdoc C where A.dr = 0 and B.pk_corp = '"
              + pk_corp + "' ";
          sReplace += "and B.pk_invbasdoc = C.pk_invbasdoc and A.pkinv = B.pk_invmandoc and C.pk_invbasdoc "
              + sOpera + " '" + sValue + "')";
        }
        else {
          sReplace = "pk_delivbill_b in (select pk_delivbill_b from dm_delivbill_b A, bd_invmandoc B, bd_invbasdoc C where A.dr = 0 ";
          sReplace += "and B.pk_invbasdoc = C.pk_invbasdoc and A.pkinv = B.pk_invmandoc and C.pk_invbasdoc "
              + sOpera + " '" + sValue + "')";
        }
        String s = getReplacedSQL(sSQL, sName, sReplace);
        sCondition += s;

      }
      else if (sName.equals("bd_invbasdoc.invpinpai") && sValue != null
          && sValue.trim().length() > 0) {
        // 品牌
        if (pk_corp != null && pk_corp.trim().length() > 0) {
          sReplace = "pk_delivbill_b in (select pk_delivbill_b from dm_delivbill_b A, bd_invmandoc B, bd_invbasdoc C where A.dr = 0 and B.pk_corp = '"
              + pk_corp + "' ";
          sReplace += "and B.pk_invbasdoc = C.pk_invbasdoc and A.pkinv = B.pk_invmandoc and C.invpinpai "
              + sOpera + " '" + sValue + "')";
        }
        else {
          sReplace = "pk_delivbill_b in (select pk_delivbill_b from dm_delivbill_b A, bd_invmandoc B, bd_invbasdoc C where A.dr = 0 ";
          sReplace += "and B.pk_invbasdoc = C.pk_invbasdoc and A.pkinv = B.pk_invmandoc and C.invpinpai "
              + sOpera + " '" + sValue + "')";
        }
        String s = getReplacedSQL(sSQL, sName, sReplace);
        sCondition += s;

      }
      else if (sName.equals("bd_cubasdoc.pk_cubasdoc") && sValue != null
          && sValue.trim().length() > 0) {
        // 客户
        if (pk_corp != null && pk_corp.trim().length() > 0) {
          sReplace = "pk_delivbill_b in (select pk_delivbill_b from dm_delivfeebill_b A, bd_cumandoc B, bd_cubasdoc C where A.dr = 0 and B.pk_corp = '"
              + pk_corp + "' ";
          sReplace += "and B.pk_cubasdoc = C.pk_cubasdoc and A.pkcustinvoice = B.pk_cumandoc and C.pk_cubasdoc "
              + sOpera + " '" + sValue + "')";
        }
        else {
          sReplace = "pk_delivbill_b in (select pk_delivbill_b from dm_delivfeebill_b A, bd_cumandoc B, bd_cubasdoc C where A.dr = 0 ";
          sReplace += "and B.pk_cubasdoc = C.pk_cubasdoc and A.pkcustinvoice = B.pk_cumandoc and C.pk_cubasdoc "
              + sOpera + " '" + sValue + "')";
        }
        String s = getReplacedSQL(sSQL, sName, sReplace);
        sCondition += s;

      }
      else if (sName.equals("dm_delivbill_b.pkarrivearea") && sValue != null
          && sValue.trim().length() > 0) {
        // 地区
        sReplace = "pk_delivbill_b in (select pk_delivbill_b from dm_delivbill_b where dr = 0 and pkarrivearea "
            + sOpera + " '" + sValue + "')";
        String s = getReplacedSQL(sSQL, sName, sReplace);
        sCondition += s;
      }
    }
    sCondition = processFirst(sCondition);

    return "1=1" + sCondition;
  }

  /**
   * 子类实现该方法，返回业务界面的标题。
   * 
   * @version (00-6-6 13:33:25)
   * @return java.lang.String
   */
  public String getTitle() {
    if (getBillCardPanel().getTitle() != null
        && getBillCardPanel().getTitle().trim().length() != 0)
      return getBillCardPanel().getTitle();
    else
      return nc.ui.ml.NCLangRes.getInstance().getStrByID("40140418",
          "UPT40140418-000016")/* @res "运费发票" */;
  }

  /**
   * 此处插入方法说明。 功能：反核销dlg 参数： 返回： 例外： 日期：(2002-8-9 16:27:02) 修改日期，修改人，修改原因，注释标志：
   */
  public DMVO getVerifyBackDLG() {

    // 弹出查询对话框

    // 输入查询条件后，关闭查询对话框，弹出反核销对话框

    m_VerifyBackDLG = new VerifyBackDLG();

    if (m_VerifyBackDLG.showModal() == UIDialog.ID_OK) {

      return null;
    }

    return null;
  }

  /**
   * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2002-8-9 16:27:02) 修改日期，修改人，修改原因，注释标志：
   */
  public DMVO getVerifyDLG() {

    String sTrancustPK = (String) getBillCardPanel()
        .getHeadItem("pk_transcust").getValue();
    String sPkcustinvoice = (String) getBillCardPanel().getHeadItem(
        "pkcustinvoice").getValue();
    String sIsendtype = ((UIComboBox) getBillCardPanel().getHeadItem(
        "isendtype").getComponent()).getSelectedIndex()
        + "";
    String pk_delivinvoice_h = (String) getBillCardPanel().getHeadItem(
        "pk_delivinvoice_h").getValue();

    DMDataVO paramvo = new DMDataVO();
    paramvo.setAttributeValue("pktrancust", sTrancustPK);
    paramvo.setAttributeValue("isendtype", sIsendtype);
    paramvo.setAttributeValue("pkcustinvoice", sPkcustinvoice);
    paramvo.setAttributeValue("pkdelivorg", getDelivOrgPK());
    paramvo.setAttributeValue("pk_delivinvoice_h", pk_delivinvoice_h);
    m_VerifyDLG = new VerifyDLG(this, DM006, paramvo);

    if (m_VerifyDLG.showModal() == UIDialog.ID_OK) {
      // 当核销后，应当刷新当前单据，否则核销标志未更新到当前数据中，再进行审批时出错
      try {
        reLoadData();
      }
      catch (Exception ex) {
        ExceptionUITools tool = new ExceptionUITools();
        tool.showMessage(ex, this);
        return null;
      }

    }
    return null;
  }

  /**
   * 此处插入方法说明。 创建日期：(2001-12-18 16:59:11)
   * 
   * @return nc.vo.pub.AggregatedValueObject
   */
  public nc.vo.pub.AggregatedValueObject getVo() throws java.lang.Exception {
    return null;
  }

  /**
   * 此处插入方法说明。 创建日期：(2002-9-12 22:57:35)
   */
  public void initBodyComboBox() {
    String[] comboxkeys = {
      "isendtype"
    };
    for (int i = 0; i < comboxkeys.length; i++) {
      getBillCardPanel().getHeadItem(comboxkeys[i]).setWithIndex(true);
    }
    UIComboBox betimes = (UIComboBox) getBillCardPanel().getHeadItem(
        "isendtype").getComponent();
    betimes.addItem(FreightType.sNone);
    betimes.addItem(FreightType.sAp);
    betimes.addItem(FreightType.sAr);

  }

  /**
   * 此处插入方法说明。 创建日期：(2002-9-12 22:57:35)
   */
  public void initListHeaderComboBox() {
    String[] comboxkeys = {
      "isendtype"
    };
    for (int i = 0; i < comboxkeys.length; i++) {
      getBillListPanel().getHeadItem(comboxkeys[i]).setWithIndex(true);
    }
    UIComboBox betimes = (UIComboBox) getBillListPanel().getHeadItem(
        "isendtype").getComponent();
    betimes.addItem(FreightType.sNone);
    betimes.addItem(FreightType.sAp);
    betimes.addItem(FreightType.sAr);
  }

  /**
   * 此处插入方法说明。 创建日期：(2002-5-10 11:16:28)
   */
  protected void initFixSubMenuButton() {
	  boBatchEditPrice = new ButtonObject("单价批修改","单价批修改","boBatchEditPrice");
    boDelivBill = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID(
        "40140418", "UPT40140418-000020")/* @res "查询发运单" */,
        nc.ui.ml.NCLangRes.getInstance().getStrByID("40140418",
            "UPT40140418-000020")/* @res "查询发运单" */, 1, "查询发运单"); /*-=notranslate=-*/
    boVerify = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID(
        "40140418", "UPT40140418-000022")/* @res "核销" */,
        nc.ui.ml.NCLangRes.getInstance().getStrByID("40140418",
            "UPT40140418-000022")/* @res "核销" */, 1, "核销"); /*-=notranslate=-*/
    boVerifyBack = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("40140418", "UPT40140418-000024")/* @res "反核销" */,
        nc.ui.ml.NCLangRes.getInstance().getStrByID("40140418",
            "UPT40140418-000024")/* @res "反核销" */, 1, "反核销");// 反核销--zhy
    // /*-=notranslate=-*/
    boAction = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID(
        "common", "UC001-0000026")/* @res "执行" */, nc.ui.ml.NCLangRes
        .getInstance().getStrByID("common", "UC001-0000026")/* @res "执行" */);

    boAction.addChildButton(boAudit);
    boAction.addChildButton(boCancelAudit);

    boLine.addChildButton(boAddLine);
    boLine.addChildButton(boDelLine);
    boLine.addChildButton(boCopyLine);
    boLine.addChildButton(boPasteLine);

    // 维护
    boMaintain.removeAllChildren();
    boMaintain.addChildButton(boEdit);
    boMaintain.addChildButton(boSave);
    boMaintain.addChildButton(boCancel);
    boMaintain.addChildButton(boDel);

    // 浏览
    boBrowse.removeAllChildren();
    boBrowse.addChildButton(boQuery);
    boBrowse.addChildButton(boRefresh);
    boBrowse.addChildButton(boFind);
    boBrowse.addChildButton(boFirst);
    boBrowse.addChildButton(boPre);
    boBrowse.addChildButton(boNext);
    boBrowse.addChildButton(boLast);

    boBrowseList.removeAllChildren();
    boBrowseList.addChildButton(boQuery);
    boBrowseList.addChildButton(boRefresh);
    boBrowseList.addChildButton(boFind);

    // 辅助
    boAssistant.removeAllChildren();
    boAssistant.addChildButton(boVerify);
    boAssistant.addChildButton(boVerifyBack);
    boAssistant.addChildButton(boDocument);
    boAssistant.addChildButton(boPrintPreview);
    boAssistant.addChildButton(boPrint);

    boAssistantList.removeAllChildren();
    boAssistantList.addChildButton(boDocument);
    boAssistantList.addChildButton(boPrintPreview);
    boAssistantList.addChildButton(boPrint);
    //eric 2012-8-8
    PfUtilClient.retBusinessBtn(boBusiType, getCorpPrimaryKey(), getBillType());
    if(boBusiType.getChildButtonGroup() != null && boBusiType.getChildButtonGroup().length > 0)
    {
        ButtonObject bo[] = boBusiType.getChildButtonGroup();
        for(int i = 0; i < bo.length; i++)
            bo[i].setName((new StringBuilder()).append("<").append(bo[i].getName()).append(">").toString());

        boBusiType.setChildButtonGroup(bo);
        boBusiType.setTag(boBusiType.getChildButtonGroup()[0].getTag());
        boBusiType.getChildButtonGroup()[0].setSelected(true);
        boBusiType.setCheckboxGroup(true);
        PfUtilClient.retAddBtn(boAdd, getCorpPrimaryKey(), getBillType(), boBusiType.getChildButtonGroup()[0]);
        bo = boAdd.getChildButtonGroup();
        for(int i = 0; i < bo.length; i++)
            bo[i].setName((new StringBuilder()).append("<").append(bo[i].getName()).append(">").toString());

        boAdd.setChildButtonGroup(bo);
    } else
    {
        System.out.println("\u6CA1\u6709\u521D\u59CB\u5316\u4E1A\u52A1\u7C7B\u578B!");
    }
    
    aryButtonGroup = new ButtonObject[] {
       boBusiType, boAdd, boDelivBill, boMaintain, boLine, boAction, boBrowse, boSwith,
        boAssistant,boBatchEditPrice
    };

    aryListButtonGroup = new ButtonObject[] {
        boBrowseList, boSwith, boAssistantList
    };
  }

  public void initialize() {
  }

  /**
   * 初始化。 功能： 参数： 返回： 例外： 日期：(2002-5-9 16:00:39) 修改日期，修改人，修改原因，注释标志：
   */
  public void initializeNew() {
    // 加载模板

    setBillTypeCode(DMBillTypeConst.m_delivInvoice);

    // it may be redundant
    setNodeCode(DMBillNodeCodeConst.m_delivInvoice);

    setVuserdefCode(DMBillTypeConst.m_delivInvoice);
    // 设置精度
    setNumItemKeys(new String[] {
      "dinvnum"
    }); // 数量
    setMoneyItemKey(new String[] {
        "dmoney", "dtaxmoney", "dtotalpaymoney", "dtranmoney", "dtaxfee"
    });
    // 金额
    setPriceItemKeys(new String[] {
      "dunitprice"
    });
    setVuserdefCode(DMBillTypeConst.m_delivInvoice);

    super.initialize();

    // 初始化公式

    // initFormulaParse();

    switchButtonStatus(DMBillStatus.CardView);
    setButton(boAudit, false);
    setButton(boCancelAudit, false);
    // 隐藏列表部分列

    getBillListPanel().hideHeadTableCol("pk_corp");
    getBillListPanel().hideHeadTableCol("pkoprdepart");
    getBillListPanel().hideHeadTableCol("pkoperator");
    getBillListPanel().hideHeadTableCol("pk_transcust");
    getBillListPanel().hideHeadTableCol("pk_payment");
    getBillListPanel().hideHeadTableCol("pkarap");
    getBillListPanel().hideHeadTableCol("pkbillperson");
    getBillListPanel().hideHeadTableCol("pkapprperson");

    /*-----初始化参照------*/

    // 加载发运组织
    getBillCardPanel().getHeadItem("pkdelivorg").setValue(getDelivOrgPK());

    // 部门
    ((UIRefPane) getBillCardPanel().getHeadItem("pkoprdepart").getComponent())
        .setWhereString("bd_deptdoc.pk_corp = '" + getDelivCorp()
            + "' and  canceled  <>  'Y' ");

    // 付款方式
    ((UIRefPane) getBillCardPanel().getHeadItem("pk_payment").getComponent())
        .setWhereString("bd_balatype.pk_corp = '" + getDelivCorp()
            + "' or  bd_balatype.pk_corp  =  '" + getGroupID() + "'");

    // 收付款协议
    ((UIRefPane) getBillCardPanel().getHeadItem("pkarap").getComponent())
        .setWhereString("bd_payterm.pk_corp = '" + getDelivCorp()
            + "' or bd_payterm.pk_corp = '" + getGroupID() + "'");

    // 存货,单位编码过滤存货且为非封存存货、劳务存货
    ((UIRefPane) getBillCardPanel().getBodyItem("invcode").getComponent())
        .setWhereString("bd_invbasdoc.laborflag='Y' "
            + " and bd_invmandoc.sealflag ='N' and bd_invmandoc.pk_corp='"
            + getDelivCorp() + "'");

    // 设置承运商参照的默认发运组织
    TrancustRefModel model = (TrancustRefModel) ((UIRefPane) getBillCardPanel()
        .getHeadItem("pk_transcust").getComponent()).getRefModel();
    model.setDelivOrgPK(getDelivOrgPK());
    model.setMatchPkWithWherePart(true);

    ((DMBillCardPanel) getBillCardPanel())
        .setBodyValueRangeHashtable(new ValueRangeHashtableInvoiceBody());

    setPrimaryKeyName("pk_delivinvoice_h");
    setBillCodeKeyName("vinvoicenumber");
    ((DMBillCardPanel) getBillCardPanel()).setRowNumKey("crowno");
  
    UIRefPane refpane = (UIRefPane) getBillCardPanel().getHeadItem("pkcustinvoice").getComponent();
    String sql = " bd_cumandoc.pk_corp = '"
      + getBelongCorpIDofDelivOrg() + "' and custflag in( '2', '0')";
    refpane.getRefModel().setWherePart( sql );
    
    getBillCardPanel().setBodyMenuShow(false);

  }

  /**
   * 初始化类。
   */
  protected void initialize(String pk_corp, String billtype, String busitype,
      String operator, String id) {
    // 设置界面
    try {
      setName("SaleOrder");
      setSize(774, 419);
      add(getBillCardPanel(), "Center");
    }
    catch (java.lang.Throwable ivjExc) {
      handleException(ivjExc);
    }

    // 加载参数
    getSystemPara();
    setVuserdefCode(DMBillTypeConst.m_delivInvoice);
    // 初试化币种
    initCurrency();

    // 加载模板
    loadCardTemplet(billtype, operator, pk_corp);

    // 加载数据
    loadCardData(id);

    getBillCardPanel().setEnabled(false);

  }

  /**
   * ?user> 功能： 参数： 返回： 例外： 日期：(2004-8-31 16:33:44) 修改日期，修改人，修改原因，注释标志：
   */
  private void initQuery() {
    // 初始化查询模板
    m_condClient = new QueryConditionClient(this, nc.ui.ml.NCLangRes
        .getInstance().getStrByID("40140418", "UPP40140418-000066")/*
                                                                     * @res
                                                                     * "运费发票中查询发运单"
                                                                     */);
    m_condClient.setTempletID("DM_BILL_QUERTMP_0200");

    m_condClient.setConditionEditable("dm_delivbill_h.pkdelivorg", false);
    m_condClient.setDefaultValue("dm_delivbill_h.pkdelivorg", getDelivOrgPK(),
        null);

    /* 封存的基础数据能被参照 */
    m_condClient.setSealedDataShow(true);

    m_condClient.hideNormal();

    // 承运商
    UIRefPane rfpTranCust = new UIRefPane();
    TrancustRefModel rfm = new TrancustRefModel();
    rfm.setDelivOrgPK(getDelivOrgPK());
    rfpTranCust.setRefModel(rfm);
    m_condClient.setValueRef("dm_delivbill_h.pktrancust", rfpTranCust);

    // 客户
    UIRefPane customerRef = new UIRefPane();
    customerRef.setRefType(2); // 树表结构
    customerRef.setIsCustomDefined(true);

    nc.ui.bd.ref.AbstractRefModel customerModel = new nc.ui.dm.pub.ref.CustbaseRefModel();
    customerRef.setRefModel(customerModel);
    customerRef.setWhereString("bd_cubasdoc.pk_corp in ("
        + getStrCorpIDsOfDelivOrg() + ", '"
        + getClientEnvironment().getGroupId() + "')");
    m_condClient.setValueRef("bd_cubasdoc.pk_cubasdoc", customerRef);
  }

  /**
   * 初试化变量。
   * =========================================================================================
   * 说明： 主要用于设置单据上的顶级菜单按钮，默认按钮定义在类变量定义 代码结构： //卡片按钮 ButtonObject[]
   * aryButtonGroup = {boBrowse,boEdit,boSave,
   * boCancel,boAction,boLine,boPrint,boAssistant}; this.aryButtonGroup =
   * aryButtonGroup; //列表按钮 ButtonObject[] aryListButtonGroup =
   * {boBusiType,boBrowse,boCard,boEdit,boAction,boPrint,boAssistant};
   * this.aryListButtonGroup = aryListButtonGroup;
   * =========================================================================================
   */
  public void initVariable() {
    super.initVariable();

    setNodeCode("40140418");

    setBillTypeCode("7V");

    // 单据显示状态
    setShowState(nc.ui.dm.pub.DMBillStatus.Card);

  }

  /**
   * 载入列表数据 创建日期：(2002-6-10 19:17:44)
   * 
   * @param sWhereSql
   *          java.lang.String
   */
  private DMVO loadBillData(String sWhereSql) throws Exception {
    long lStartTime = System.currentTimeMillis();
    long lTime = 0;

    m_VOs = InvoiceBO_Client.queryInvoice(sWhereSql);

    m_alAllVOs = new ArrayList();

    for (int i = 0; i < m_VOs.length; i++) {
      m_alAllVOs.add(i, m_VOs[i]);
    }
    reLoadListData();

    return null;
  }

  /**
   * 放弃输入。 创建日期：(2001-4-21 10:36:57)
   */
  public void loadCardData(String id) {
    try {
      DMVO[] vos = null;
      String sWhere = " pk_delivinvoice_h= '" + id + "'";

      vos = InvoiceBO_Client.queryInvoice(sWhere);

      if (vos != null && vos.length != 0 && vos[0] != null) {
        getBillCardPanel().setBillValueVO(vos[0]);
        getBillCardPanel().getBillModel().execLoadFormula();
        getBillCardPanel().updateValue();
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 加载卡片模板。 创建日期：(2001-11-15 9:03:35)
   */
  public void loadCardTemplet(String billtype, String operator, String pk_corp) {
    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
        "UPPSCMCommon-000136")/* @res "开始加载模板...." */);

    BillData bd = new BillData(ivjBillCardPanel.getTempletData(billtype, null,
        operator, pk_corp));

    // 改变界面
    setCardPanel(bd);
    changeBillDataByUserDef(bd);
    // 设置界面，置入数据源
    getBillCardPanel().setBillData(bd);

    // 限制输入长度
    setInputLimit();

    // 设置下拉框
    initBodyComboBox();

    // 初试化状态
    // initState();

    getBillCardPanel().getPasteLineToTailMenuItem().setEnabled(false);
    // 设置合计监听
    // getBillCardPanel().addBodyTotalListener(this);
    // getBillCardPanel().getBillTable().getSelectionModel().addListSelectionListener(this);
    // getBillCardPanel().getBillTable().getColumnModel().addColumnModelListener(this);
    // getBillCardPanel().getBillModel().addTableModelListener(this);

    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
        "UPPSCMCommon-000176")/* @res "模板加载成功！" */);
  }

  /**
   * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2002-8-23 14:13:15) 修改日期，修改人，修改原因，注释标志：
   */
  public void loadListBodyData() {

    if (m_selectRow != -1) {
      DMDataVO[] bodyvos = (DMDataVO[]) ((DMVO) getAllVOs().get(m_selectRow))
          .getChildrenVO();
      //2015-05-05 排序后主子表关联有误--wkf--
      String pk_delval = (String) getBillListPanel().getHeadBillModel().getValueAt(m_selectRow, "pk_delivinvoice_h");
      DMDataVO[] tmpBvos = null;
      for(int i = 0; i < getAllVOs().size(); i++) {
    	  DMDataVO[] tmbvo =  (DMDataVO[]) ((DMVO) getAllVOs().get(i)).getChildrenVO();
    	  String tmpdel = (String) tmbvo[0].getAttributeValue("pk_delivinvoice_h");
    	  if(tmpdel.equals(pk_delval)){
    		  tmpBvos= tmbvo;
    		  break;
    	  }
      }
      // end ---wkf--
      getBillListPanel().setBodyValueVO(tmpBvos);
      getBillListPanel().getBodyBillModel().execLoadFormula();
      getBillListPanel().updateUI();
      // DMDataVO[] vos =(DMDataVO[])
      // getBillListPanel().getBodyBillModel().getBodyValueVOs("nc.vo.dm.pub.DMDataVO");
    }
  }

  /**
   * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2002-11-13 14:58:40) 修改日期，修改人，修改原因，注释标志：
   */
  public void loadListHeadData() {
    DMDataVO[] headvos = new DMDataVO[getAllVOs().size()];

    for (int i = 0; i < headvos.length; i++) {
      headvos[i] = (DMDataVO) ((DMVO) getAllVOs().get(i)).getParentVO();
    }

    getBillListPanel().setHeaderValueVO(headvos);
    getBillListPanel().getHeadBillModel().execLoadFormula();
  }

  /**
   * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2002-8-15 10:24:40) 修改日期，修改人，修改原因，注释标志：
   */
  public void onAdd() {

    m_sDelivbillbs = null;
    getBillCardPanel().addNew();
    getBillCardPanel().setEnabled(true);

    switchButtonStatus(DMBillStatus.CardNew);
    setEditFlag(DMBillStatus.CardNew);

    // 设置自动加载数据

    getBillCardPanel().getHeadItem("pkdelivorg").setValue(getDelivOrgPK());
    getBillCardPanel().getTailItem("pkbillperson").setValue(getUserID()); // 制单人
    getBillCardPanel().getTailItem("billdate").setValue(
        getClientEnvironment().getDate()); // 制单日期

    // 公司

    getBillCardPanel().getHeadItem("pk_corp").setValue(getCorpID());
    ((UIRefPane) getBillCardPanel().getHeadItem("pk_corp").getComponent())
        .setPK(getCorpID());
    String text = ((UIRefPane) getBillCardPanel().getHeadItem("pk_corp")
        .getComponent()).getUITextField().getText();
    getBillCardPanel().getHeadItem("pk_corp").setEnabled(false);



    bFromDelivbill = false;

    // 新增时表体自动增行
    getBillCardPanel().addLine();

    String message = NCLangRes.getInstance().getStrByID("common", "UCH028");
    /* @res正在增加 */
    this.showHintMessage(message);
  }

  /**
   * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2002-8-31 15:49:21) 修改日期，修改人，修改原因，注释标志：
   */
  public void onAudit() {
    try {
      if (getBillCardPanel().getHeadItem("vinvoicenumber").getValue() == null
          || getBillCardPanel().getHeadItem("vinvoicenumber").getValue()
              .length() == 0) {
        showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "40140418", "UPP40140418-000012")/* @res "没有单据，不能审批!" */);
        return;
      }
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40140418",
          "UPP40140418-000013")/* @res "开始审核………………！！！" */);
      DMVO dvo = (DMVO) getBillCardPanel().getBillValueVO("nc.vo.dm.pub.DMVO",
          "nc.vo.dm.pub.DMDataVO", "nc.vo.dm.pub.DMDataVO");

      // 操作人
      dvo.getParentVO().setAttributeValue("userid", getOperator());

      //
      DMDataVO[] bodyvos = (DMDataVO[]) dvo.getChildrenVO();
      UFDouble dTranmoneySum = new UFDouble(0);
      UFDouble dTranmoney = new UFDouble(0);
      Object oTranmoney = null;

      // 是否核销才可审批
      if (DM006.booleanValue()) { // DM006.booleanValue()
        for (int i = 0; i < bodyvos.length; i++) {
          oTranmoney = bodyvos[i].getAttributeValue("dtranmoney");
          dTranmoneySum = dTranmoneySum
              .add(oTranmoney == null ? new UFDouble(0) : (UFDouble) oTranmoney);
        }

        if (getBillCardPanel().getHeadItem("bisverify").getValue() != null
            && new UFBoolean(getBillCardPanel().getHeadItem("bisverify")
                .getValue().toString()).booleanValue()) {
          if (dTranmoneySum.doubleValue() <= 0) {
            showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
                "40140418", "UPP40140418-000014")/* @res "发票还未核销，不可审核！" */);
            return;
          }
        }
      }

      if (dvo.getParentVO().getAttributeValue("pkapprperson") != null
          && ((String) dvo.getParentVO().getAttributeValue("pkapprperson"))
              .trim().length() != 0) {
        showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "40140418", "UPP40140418-000015")/* @res "发票已审核！！！" */);
        return;
      }

      String hID = (String) dvo.getParentVO().getAttributeValue(
          "pk_delivinvoice_h");

      // 审核
      dvo.setStatusTo(VOStatus.UPDATED);
      dvo.getParentVO().setAttributeValue("apprdate",
          getClientEnvironment().getDate());
      dvo.getParentVO().setAttributeValue("pkapprperson", getUserID());

      //
      dvo.getParentVO().setAttributeValue("taudittime",
          getClientEnvironment().getServerTime());

      DMVO retvo = null;
      retvo = InvoiceBO_Client.auditInvoice(dvo, new ClientLink(
          getClientEnvironment()));

      dvo.combineOtherVO(retvo);

      m_alAllVOs.set(m_selectRow, dvo);

      // 置界面
      reLoadListData();

      getBillCardPanel().setBillValueVO(dvo);
      getBillCardPanel().updateValue();

      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
          "UCH010")/* @res "审核成功！" */);

    }
    catch (Exception e) {
      ExceptionUITools tool = new ExceptionUITools();
      tool.showMessage(e, this);
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
    // getBillCardPanel().stopEditing();huxiaobo modified at 20060902 16:22
    // 此句话使得点击任何按钮时，billcard失去焦点，所以复制粘贴等功能都失效
    this.showHintMessage("");

    if (isListHeaderMultiSelected(bo, null))
      return;
    
    if (bo == boAdd) {
      onAdd(); // 新增
      this.SwitchOnAudit(); // 更新审核，取消审核按钮
    }
    
    else if (bo == boDelivBill)
      onDelivBill(); // 查询发运单
    
    else if (bo == boVerify) {
      onVerify();  // 核销
    }
    else if (bo == boSave) {
      onSave();
    }
    
    else if (bo == boVerifyBack) {// zhy
      onVerifyBack();  // 反核销
    }
    
    else if (bo == boAudit) {
      onAudit();  // 审核
      this.SwitchOnAudit(); // 更新审核，取消审核按钮
    }
    
    else if (bo == boCancelAudit) {
      onCancelAudit(); // 弃审
      this.SwitchOnAudit(); // 更新审核，取消审核按钮
    }
    else if (bo == boRefresh) {// 刷新
      onRefresh();
      this.SwitchOnAudit(); // 更新审核，取消审核按钮
    }
    else if (bo == boSwith) {
      onSwith();
      this.SwitchOnAudit();// 更新审核，取消审核按钮
    }
    else if (bo == boDel) {
      onDel();
      this.SwitchOnAudit();
    }else if (bo.getParent() == boAdd) { // 业务类型
    	onNew(bo);
	}else if(bo ==boBatchEditPrice){//eric
		onBatchEdit();
	}
    // 二次开发扩展按钮
    else {
      boolean extraButtonFired = false;
      ButtonObject[] buttons = getExtendBtns();
      if (buttons != null && buttons.length > 0) {
        int length = buttons.length;
        for (int i = 0; i < length; i++) {
          if (buttons[i] == bo) {
            extraButtonFired = true;
            break;
          }
        }
      }
      // 二次开发按钮出发的点击事件
      if (extraButtonFired) {
        onExtendBtnsClick(bo);
      }
      else {
        super.onButtonClicked(bo);
      }
    }
  }
  
  public PriceEditDialog dlg = null;
  public PriceEditDialog getDlg(){
	  if(dlg==null){
		  dlg = new PriceEditDialog();
	  }
	  return dlg;
  }
  
  public void onBatchEdit(){
	 int[] selectrows =  getBillCardPanel().getBillTable().getSelectedRows();
	 if(selectrows.length<1){
		 MessageDialog.showErrorDlg(this, "", "请选择表体数据行，再修改");
		 return;
	 }
	  int ret = getDlg().showModal();
	  if(ret==1){
//		  String value = getDlg().getCscode();
		  UFDouble value = getDlg().getCscode()[0]==null?new UFDouble(0.00):new UFDouble(getDlg().getCscode()[0]);
		  UFDouble value1 = getDlg().getCscode()[1]==null?new UFDouble(0.00):new UFDouble(getDlg().getCscode()[1]);
		  UFDouble value2 = getDlg().getCscode()[2]==null?new UFDouble(0.00):new UFDouble(getDlg().getCscode()[2]);
		  for(int i : selectrows){
			  //含税单价
			  if(value1.doubleValue()>0){
				  getBillCardPanel().setBodyValueAt(value1, i, "vuserdef19");
				  getBillCardPanel().execBodyFormula(i, "vuserdef19");
			  }
			  //税率
			  if(value2.doubleValue()>0){
				  getBillCardPanel().setBodyValueAt(value2, i, "dtax");
				  getBillCardPanel().execBodyFormula(i, "dtax");
			  }
			  //无税单价
			  if(value.doubleValue()>0){
				  getBillCardPanel().setBodyValueAt(value, i, "dunitprice");
				  getBillCardPanel().execBodyFormula(i, "dunitprice");
			  }
		  }
//		  for(int i : selectrows){
//			  Object oldvalue = getBillCardPanel().getBodyValueAt(i, "dunitprice");
//			  getBillCardPanel().setBodyValueAt(value, i, "dunitprice");
////				BillEditEvent be = new BillEditEvent(getBillCardPanel().getBodyItem("dunitprice"), oldvalue,value, "dunitprice", i, 1);
//				getBillCardPanel().execBodyFormula(i, "dunitprice");
//		  }
	  }
  }

  //eric
  public void onNew(ButtonObject bo)
  {
	  if (getShowState() == DMBillStatus.List) {
		  super.onSwith();
	  }
	  
	  String tmpString = bo.getTag();
	    int findIndex = tmpString.indexOf(":");

	     billType = tmpString.substring(0, findIndex);
	  PfUtilClient.childButtonClicked(bo, getCorpPrimaryKey(), getNodeCode(),
				getClientEnvironment().getUser().getPrimaryKey(),
				getBillType(), this);
	
	  if (nc.ui.pub.pf.PfUtilClient.makeFlag) {
		  onAdd();
		} else
	  if (PfUtilClient.isCloseOK()) {
		  try {
		  AggregatedValueObject[] aggs = PfUtilClient.getRetVos();
		  DMVO[] ss = (DMVO[]) aggs; 
		  DMInvoiceBodyDataVO[] vos = (DMInvoiceBodyDataVO[]) ss[0].getChildrenVO();
		  
		  onAdd();
		  int rowno = 0 ;
		  for(DMInvoiceBodyDataVO vo : vos){
			  rowno =  rowno+10;
			  vo.setAttributeValue("crowno",rowno);
			  vo = ChangeInvInfo(vo);
		  }
		
		    //查询运费价格表中数据
		  String pk_corp = getClientEnvironment().getCorporation().getPrimaryKey();
		  if(pk_corp.equals("1078") || pk_corp.equals("1100")|| pk_corp.equals("1108")){
			  UpdateInvcode(ss[0]);//更改存货为"产成品运费(9909)"--wkf--2015-02-06
			  UpdatePrice1(ss[0]);
		  }else{
			  UpdatePrice(ss[0]);
		  }
		    getBillCardPanel().setBillValueVO(ss[0]);
		    UIComboBox betimes = (UIComboBox) getBillCardPanel().getHeadItem(
	        "isendtype").getComponent();

		    int selindex=billType==null||billType.equals("")?0:billType.equals("4C")||billType.equals("4Y")?1:2;
		    switch(selindex)
		    {
		    	case 0:
		    	{ betimes.setSelectedIndex(0);
  		      getBillCardPanel().getHeadItem("pk_transcust").setEnabled(false);
		      getBillCardPanel().getHeadItem("pkcustinvoice").setEnabled(false);}
		    	break;
		    	case 1:
		    	{ 
		    		betimes.setSelectedIndex(1);
		    		getBillCardPanel().getHeadItem("pk_transcust").setEnabled(true);
				    getBillCardPanel().getHeadItem("pkcustinvoice").setEnabled(false);		    
	
		    	}break;	
		    	case 2:
		   		 {betimes.setSelectedIndex(2);
   		      getBillCardPanel().getHeadItem("pk_transcust").setEnabled(false);
		      getBillCardPanel().getHeadItem("pkcustinvoice").setEnabled(true);
   		      }break;
		    }
	/*	    nc.ui.pub.bill.BillEditEvent e=new BillEditEvent(getBillCardPanel().getHeadItem(
	        "isendtype").getComponent(),(selindex==0?FreightType.sNone:(selindex==1?FreightType.sAr:FreightType.sAp)),"isendtype",-1,BillItem.HEAD);
		    afterEdit(e);*/
	
			   
		    getBillCardPanel().getTailItem("pkbillperson").setValue(getUserID()); // 制单人
		    getBillCardPanel().getTailItem("billdate").setValue(getClientEnvironment().getDate()); // 制单日期
		    getBillCardPanel().getBillModel().execLoadFormula(); 
		    
		  } catch (Exception e) {
			  // TODO Auto-generated catch block
				e.printStackTrace();
			}
	  }
  }
  
  public void UpdatePrice(DMVO vo) throws BusinessException{
	  DMInvoiceHeadDataVO hvo = (DMInvoiceHeadDataVO) vo.getParentVO();
	  String pk_transcust = hvo.getAttributeValue("pk_transcust")==null?"":hvo.getAttributeValue("pk_transcust").toString();
	  String pk_fytype = hvo.getAttributeValue("pk_defdoc18")==null?"": hvo.getAttributeValue("pk_defdoc18").toString();
	  String billdate =getClientEnvironment().getDate().toString();
	
	  String sql = "select dbaseprice,taxprice,rate from dm_baseprice where nvl(dr,0)=0 and ? >= effectdate and ? < expirationdate  and pk_transcust=? and  pk_sendtype=? ";
	  SQLParameter param = new SQLParameter();
	  param.addParam(billdate);
	  param.addParam(billdate);
	  param.addParam(pk_transcust);
	  param.addParam(pk_fytype);
//	  String sql2 = "select dbaseprice,taxprice,rate from dm_baseprice where nvl(dr,0)=0 and '"+billdate+"' >= effectdate and '"+billdate+"' < expirationdate  and pk_transcust='"+pk_transcust+"' and  pk_sendtype='"+pk_fytype+"' ";
//	  HashMap hm = (HashMap)BGZGProxy.getIUAPQueryBS().executeQuery(sql, param, new MapProcessor());
//	  HashMap hm = (HashMap) BGZGProxy.getIUAPQueryBS().executeQuery(sql2, new MapProcessor());
//	  if(hm!=null&&hm.size()>0){
		  UFDouble total = new UFDouble(0);
		  DMInvoiceBodyDataVO[] vos = (DMInvoiceBodyDataVO[]) vo.getChildrenVO();
		  UFDouble taxprice = new UFDouble(0);
		  for(DMInvoiceBodyDataVO bvo : vos){
			  String sql_getFromTO = "select pksendaddress,pkarriveaddress,h.senddate,pkinv from dm_delivbill_b b,dm_delivbill_h h where h.pk_delivbill_h = b.pk_delivbill_h and b.pk_delivbill_b = '";
			  String pk_delivbill_b = bvo.getAttributeValue("vuserdef15")==null?"":bvo.getAttributeValue("vuserdef15").toString();
			  sql_getFromTO = sql_getFromTO.concat(pk_delivbill_b).concat("' ");
			  HashMap hms = (HashMap)BGZGProxy.getIUAPQueryBS().executeQuery(sql_getFromTO, new MapProcessor());
			  String pksendaddress = hms.get("pksendaddress")==null?"":hms.get("pksendaddress").toString();
			  String pkarriveaddress = hms.get("pkarriveaddress")==null?"":hms.get("pkarriveaddress").toString();
			  billdate = hms.get("senddate")==null?"":hms.get("senddate").toString();
			  String sql2 = "select dbaseprice,taxprice,rate from dm_baseprice where nvl(dr,0)=0 and '"+billdate+"' >= effectdate and '"+billdate+"' < expirationdate  and pk_transcust='"+pk_transcust+"' and  pk_sendtype='"+pk_fytype+"' ";
			  sql2 = sql2.concat(" and pkfromaddress ='").concat(pksendaddress).concat("' and pktoaddress ='").concat(pkarriveaddress).concat("' ");
			  HashMap hm = (HashMap) BGZGProxy.getIUAPQueryBS().executeQuery(sql2, new MapProcessor());
			  if(hm!=null&&hm.size()>0){
			  bvo.setAttributeValue("dunitprice", hm.get("dbaseprice"));
			  bvo.setAttributeValue("dtax", hm.get("rate"));
			  bvo.setAttributeValue("vuserdef19", hm.get("taxprice"));//add Lin 2012/09/18
			  UFDouble num = bvo.getAttributeValue("dinvnum")==null?new UFDouble(0):new UFDouble( bvo.getAttributeValue("dinvnum").toString());
			  UFDouble price = bvo.getAttributeValue("dunitprice")==null?new UFDouble(0):new UFDouble( bvo.getAttributeValue("dunitprice").toString());
			   taxprice = hm.get("taxprice")==null?new UFDouble(0):new UFDouble( hm.get("taxprice").toString());
			  bvo.setAttributeValue("dmoney", num.multiply(price));
			  bvo.setAttributeValue("dtaxmoney", num.multiply(taxprice));
			  bvo.setAttributeValue("dtaxfee", num.multiply(taxprice).sub(num.multiply(price)));
			  total = total.add(taxprice.multiply(num));
			  }
		/*	  String invSql="select invcode from bd_invbasdoc where pk_invbasdoc='"+String.valueOf(bvo.getAttributeValue("pkbasinv"))+"'";
			  HashMap invhm = (HashMap) BGZGProxy.getIUAPQueryBS().executeQuery(invSql, new MapProcessor());*/
			  /*if(hm!=null&&hm.size()>0)
			  {
				  bvo.setAttributeValue("vuserdef14", bvo.getAttributeValue("vuserdef18"));
				  bvo.setAttributeValue("vuserdef18",invhm.get("invcode")) ;
			  }*/
		  }
		  hvo.setAttributeValue("vuserdef20", total.setScale(UFDouble.ROUND_CEILING, 2));
		  hvo.setAttributeValue("vuserdef14", taxprice);
		  hvo.setAttributeValue("vuserdef13", hvo.getAttributeValue("vuserdef18"));
		  hvo.setAttributeValue("vuserdef13", hvo.getAttributeValue("vuserdef18"));
		  hvo.setAttributeValue("dinvoicedate", getClientEnvironment().getDate());//add Lin 2012/09/18
//		  HashMap hm = (HashMap) BGZGProxy.getIUAPQueryBS().executeQuery("select sendcode,sendname from bd_sendtype where pk_sendtype='"+hvo.getAttributeValue("pk_defdoc18")+"'", new MapProcessor());
//		  hvo.setAttributeValue("vuserdef18",hm.get("sendcode"));
//	  }
	  
  }
  
  //获取单价（制盖）,只针对制盖.wkf---2014-12-11
  public void UpdatePrice1(DMVO vo) throws BusinessException{
	  DMInvoiceHeadDataVO hvo = (DMInvoiceHeadDataVO) vo.getParentVO();
	  String pk_transcust = hvo.getAttributeValue("pk_transcust")==null?"":hvo.getAttributeValue("pk_transcust").toString();
	  String pk_fytype = hvo.getAttributeValue("pk_defdoc18")==null?"": hvo.getAttributeValue("pk_defdoc18").toString();
	  String billdate =getClientEnvironment().getDate().toString();
	
		  UFDouble total = new UFDouble(0);
		  DMInvoiceBodyDataVO[] vos = (DMInvoiceBodyDataVO[]) vo.getChildrenVO();
		  UFDouble taxprice = new UFDouble(0);
		  for(DMInvoiceBodyDataVO bvo : vos){
			  String sql_getFromTO = "select h.pk_defdoc17,h.pk_defdoc19 from so_saleorder_b b, so_sale h where nvl(h.dr,0)=0 and h.csaleid = b.csaleid   and b.corder_bid = '";
			  String pk_delivbill_b = bvo.getAttributeValue("vuserdef15")==null?"":bvo.getAttributeValue("vuserdef15").toString();
			  sql_getFromTO = sql_getFromTO.concat(pk_delivbill_b).concat("' ");
			  HashMap hms = (HashMap)BGZGProxy.getIUAPQueryBS().executeQuery(sql_getFromTO, new MapProcessor());
			  String pksendaddress = hms.get("pk_defdoc17")==null?"":hms.get("pk_defdoc17").toString();
			  String pkarriveaddress = hms.get("pk_defdoc19")==null?"":hms.get("pk_defdoc19").toString();
			  
			  String sql2 = "select dbaseprice,taxprice,rate from dm_baseprice where nvl(dr,0)=0  and pk_transcust='"+pk_transcust+"' and  pk_sendtype='"+pk_fytype+"' ";
			  sql2 = sql2.concat(" and pkfromaddress ='").concat(pksendaddress).concat("' and pktoaddress ='").concat(pkarriveaddress).concat("' ");
			  HashMap hm = (HashMap) BGZGProxy.getIUAPQueryBS().executeQuery(sql2, new MapProcessor());
			  if(hm!=null&&hm.size()>0){
			  bvo.setAttributeValue("dunitprice", hm.get("dbaseprice"));
			  bvo.setAttributeValue("dtax", hm.get("rate"));
			  bvo.setAttributeValue("vuserdef19", hm.get("taxprice"));
			  UFDouble num = bvo.getAttributeValue("dinvnum")==null?new UFDouble(0):new UFDouble( bvo.getAttributeValue("dinvnum").toString());
			  UFDouble price = bvo.getAttributeValue("dunitprice")==null?new UFDouble(0):new UFDouble( bvo.getAttributeValue("dunitprice").toString());
			   taxprice = hm.get("taxprice")==null?new UFDouble(0):new UFDouble( hm.get("taxprice").toString());
			  bvo.setAttributeValue("dmoney", num.multiply(price));
			  bvo.setAttributeValue("dtaxmoney", num.multiply(taxprice));
			  bvo.setAttributeValue("dtaxfee", num.multiply(taxprice).sub(num.multiply(price)));
			  total = total.add(taxprice.multiply(num));
			  }
		/*	  String invSql="select invcode from bd_invbasdoc where pk_invbasdoc='"+String.valueOf(bvo.getAttributeValue("pkbasinv"))+"'";
			  HashMap invhm = (HashMap) BGZGProxy.getIUAPQueryBS().executeQuery(invSql, new MapProcessor());*/
			  /*if(hm!=null&&hm.size()>0)
			  {
				  bvo.setAttributeValue("vuserdef14", bvo.getAttributeValue("vuserdef18"));
				  bvo.setAttributeValue("vuserdef18",invhm.get("invcode")) ;
			  }*/
		  }
		  hvo.setAttributeValue("vuserdef20", total.setScale(UFDouble.ROUND_CEILING, 2));
		  hvo.setAttributeValue("vuserdef14", taxprice);
		  hvo.setAttributeValue("vuserdef13", hvo.getAttributeValue("vuserdef18"));
		  hvo.setAttributeValue("vuserdef13", hvo.getAttributeValue("vuserdef18"));
		  hvo.setAttributeValue("dinvoicedate", getClientEnvironment().getDate());//add Lin 2012/09/18
//		  HashMap hm = (HashMap) BGZGProxy.getIUAPQueryBS().executeQuery("select sendcode,sendname from bd_sendtype where pk_sendtype='"+hvo.getAttributeValue("pk_defdoc18")+"'", new MapProcessor());
//		  hvo.setAttributeValue("vuserdef18",hm.get("sendcode"));
//	  }
	  
  }
  //获取设入存货编码（制盖）,只针对制盖.wkf---2014-12-11
  public void UpdateInvcode(DMVO vo) {
	  String pk_corp = getClientEnvironment().getCorporation().getPrimaryKey().toString();
	  StringBuffer sbsql = new StringBuffer();
	  sbsql.append(" select invb.invcode,invb.invname,invb.pk_invbasdoc,invm.pk_invmandoc from bd_invbasdoc invb ") 
	  .append("        left join bd_invmandoc invm ") 
	  .append("        on invb.pk_invbasdoc = invm.pk_invbasdoc ") 
	  .append("        where nvl(invb.dr,0)=0 ") 
	  .append("        and nvl(invm.dr,0)=0 ") 
	  .append("        and invm.pk_corp = '"+pk_corp+"' ") 
	  .append("        and invb.invcode = '9909' ") ;

	  HashMap invbas = null;
	  try {
		  invbas = (HashMap) BGZGProxy.getIUAPQueryBS().executeQuery(sbsql.toString(), new MapProcessor());
	  } catch (BusinessException e) {
		  e.printStackTrace();
	  }
	  String invcode = invbas.get("invcode")==null?"":invbas.get("invcode").toString();
	  String invname = invbas.get("invname")==null?"":invbas.get("invname").toString();
	  String pk_invbasdoc = invbas.get("pk_invbasdoc")==null?"":invbas.get("pk_invbasdoc").toString();
	  String pk_invmandoc = invbas.get("pk_invmandoc")==null?"":invbas.get("pk_invmandoc").toString();
	  //给vo中设值
	  DMInvoiceBodyDataVO[] vos = (DMInvoiceBodyDataVO[]) vo.getChildrenVO();
	  for(DMInvoiceBodyDataVO bvo : vos){
		  //存货基本档案主键	pkbasinv
		  //存货管理档案主键	pkinv
		  //存货编码	invcode
		  //存货名称	invname
		  bvo.setAttributeValue("invcode", invcode);
		  bvo.setAttributeValue("invname", invname);
		  bvo.setAttributeValue("pkbasinv", pk_invbasdoc);
		  bvo.setAttributeValue("pkinv", pk_invmandoc);
	  }
}
  
  /**
 * @param vo
	销售运费发票单据中，系统选择劳务类存货时，选择逻辑如下：
	如果销售运费发票的来源单据中的存货属于“盖子”或者“罐子”分类的存货，则自动带入劳务类存货“销售运费”；
	如果销售运费发票的来源单据中的存货属于“盖子”或者“罐子”分类以外的存货，则自动带入“制造费用”；
 * @return
 * @throws BusinessException 
 */
  
  IUAPQueryBS iquery = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
  
public DMInvoiceBodyDataVO ChangeInvInfo(DMInvoiceBodyDataVO vo) throws BusinessException{
	
	String invcode = vo.getAttributeValue("vuserdef18")==null? "": vo.getAttributeValue("vuserdef18").toString();
//	if(invcode.startsWith("22")){
//		vo  = getInvInfoByCode(vo,"9902");
//	}else {
//		String csourcebillbid = vo.getAttributeValue("vuserdef15")==null?"":vo.getAttributeValue("vuserdef15").toString();
//		String querystoretype = "select stor.storname,stor.def5,info.doccode,info.docname from dm_delivbill_b b left join bd_stordoc stor on b.pkdestrep = stor.pk_stordoc " +
//                                " left join bd_defdoc info on stor.def5 = info.pk_defdoc and b.pk_delivbill_b = '" +csourcebillbid+"'";
//		HashMap hm = (HashMap) iquery.executeQuery(querystoretype, new MapProcessor());
//		if(hm==null||hm.size()==0){
//			vo  = getInvInfoByCode(vo,"9902");
//		}else{
//			int type = Integer.parseInt(hm.get("doccode")==null?"0":hm.get("doccode").toString());
//			if(type==1){
//				vo  = getInvInfoByCode(vo,"9901");
//			}else if(type==2){
//				vo  = getInvInfoByCode(vo,"9902");
//			}
//		}
//		
//	}
	if(invcode.startsWith("23"))//|invcode.startsWith("31"))\
	{
		vo  = getInvInfoByCode(vo,"9902");
	}else{
		vo  = getInvInfoByCode(vo,"9903");
	}
	
	  return vo;
  }

public DMInvoiceBodyDataVO getInvInfoByCode(DMInvoiceBodyDataVO vo , String code) throws BusinessException{
	SQLParameter param = new SQLParameter();
	String sql = "select man.pk_invbasdoc,man.pk_invmandoc,bas.pk_measdoc from bd_invbasdoc bas,bd_invmandoc man where bas.pk_invbasdoc=man.pk_invbasdoc and man.pk_corp='"+getCorpPrimaryKey()+"'"
			    + " and bas.invcode=? ";
	param.addParam(code);
	HashMap hm = (HashMap) iquery.executeQuery(sql,param, new MapProcessor());
	if(hm!=null&&hm.size()>0){
	vo.setAttributeValue("pkinv", hm.get("pk_invmandoc"));
	vo.setAttributeValue("pkbasinv", hm.get("pk_invbasdoc"));
	vo.setAttributeValue("pkinvunit", hm.get("pk_measdoc"));
	}
	return vo;
}

  /**
   * 放弃输入。 创建日期：(2001-4-21 10:36:57)
   */
  public void onCancel() {
    String message = NCLangRes.getInstance().getStrByID("common", "UCH067");
    /* @res是否确定取消？" */
    int ret = MessageDialog.showYesNoDlg(this, null, message,
        UIDialog.ID_NO);

    if (ret == UIDialog.ID_NO) {
        return;
     }
    else if (ret == UIDialog.ID_YES) {
      try {
        DMVO billhvo = (DMVO) getBillCardPanel().getBillValueChangeVO(
            "nc.vo.dm.pub.DMVO", "nc.vo.dm.pub.DMDataVO",
            "nc.vo.dm.pub.DMDataVO");
        // 释放单据号
        if (m_bIsGetNewBillCode) {
          billhvo.getParentVO().setAttributeValue("bmustreturnbillcode", "Y");

          InvoiceBO_Client.returnBillCodeForUI(DMBillTypeConst.m_delivInvoice,
              "pk_corp", "vinvoicenumber", new DMVO[] {
                billhvo
              });

          m_bIsGetNewBillCode = false;
        }
        super.onCancel();
      }
      catch (Exception e) {
        ExceptionUITools tool = new ExceptionUITools();
        tool.showMessage(e, this);
        return;
      }
    }
    message = NCLangRes.getInstance().getStrByID("common", "UCH008");
    /* @res取消成功 */
    this.showHintMessage(message);
  }

  /**
   * 取消审核。 功能： 参数： 返回： 例外： 日期：(2002-8-31 20:56:20) 修改日期，修改人，修改原因，注释标志：
   */
  public void onCancelAudit() {
    try {

      // 发票弃审

      DMVO dvo = (DMVO) getBillCardPanel().getBillValueVO("nc.vo.dm.pub.DMVO",
          "nc.vo.dm.pub.DMDataVO", "nc.vo.dm.pub.DMDataVO");

      if (dvo.getParentVO().getAttributeValue("apprdate") == null
          && dvo.getParentVO().getAttributeValue("pkapprperson") == null)
        return;

      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40140418",
          "UPP40140418-000019")/* @res "取消审核………………！！！" */);

      // 操作人

      dvo.getParentVO().setAttributeValue("userid", getOperator());

      String hID = (String) dvo.getParentVO().getAttributeValue(
          "pk_delivinvoice_h");

      // 弃审

      dvo.setStatusTo(VOStatus.UPDATED);
      dvo.getParentVO().setAttributeValue("apprdate", "");
      dvo.getParentVO().setAttributeValue("pkapprperson", "");

      DMVO retvo = null;
      retvo = InvoiceBO_Client.unauditInvoice(dvo);

      dvo.combineOtherVO(retvo);

      // 置界面

      m_alAllVOs.set(m_selectRow, dvo);

      reLoadListData();

      getBillCardPanel().setBillValueVO(dvo);
      getBillCardPanel().updateValue();
      // getBillCardPanel().getTailItem("pkapprperson").setValue("");
      // getBillCardPanel().getTailItem("apprdate").setValue("");

      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
          "UCH011")/* @res "弃审成功！" */);

    }
    catch (Exception e) {
      ExceptionUITools tool = new ExceptionUITools();
      tool.showMessage(e, this);
      return;
    }
  }

  /**
   * 关闭窗口的客户端接口。可在本方法内完成窗口关闭前的工作。
   * 
   * @return boolean 返回值为true表示允许窗口关闭，返回值为false表示不允许窗口关闭。 创建日期：(2001-8-8
   *         13:52:37)
   */
  public boolean onClosing() {
    if (getEditFlag() == DMBillStatus.CardView) {
      return true;
    }
    String message = NCLangRes.getInstance().getStrByID("common", "UCH001");
    /* @res是否保存已修改的数据？" */
    int ret = MessageDialog.showYesNoCancelDlg(this, null, message,
        UIDialog.ID_YES);

    if (ret == UIDialog.ID_YES) {
      boolean flag = onSaveAction();
      return flag;
    }
    else if (ret == UIDialog.ID_NO) {
      return true;
    }
    else {
      return false;
    }
  }

  /**
   * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2002-8-31 16:39:08) 修改日期，修改人，修改原因，注释标志：
   */
  public void onDel() {
    try {

      DMVO dvo = (DMVO) getBillCardPanel().getBillValueVO("nc.vo.dm.pub.DMVO",
          "nc.vo.dm.pub.DMDataVO", "nc.vo.dm.pub.DMDataVO");

      if (dvo == null || dvo.getChildrenVO() == null
          || dvo.getChildrenVO().length == 0)
        return;

      if (MessageDialog.showYesNoDlg(this,null,nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "40140418", "UPP40140418-000021")/* @res "确定删除！！！" */,MessageDialog.ID_NO) == MessageDialog.ID_NO)
        return;

      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40140418",
          "UPP40140418-000022")/* @res "开始删除………………" */);

      // 操作人

      dvo.getParentVO().setAttributeValue("userid", getOperator());

      String pkapprperson = getBillCardPanel().getTailItem("pkapprperson")
          .getValue();

      if (pkapprperson != null && pkapprperson.trim().length() != 0) {
        showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "40140418", "UPP40140418-000023")/* @res "发票已审核，不可删除！" */);
        return;
      }

      DMDataVO[] bodyvos = (DMDataVO[]) dvo.getChildrenVO();

      UFDouble dTranmoneySum = new UFDouble(0);
      Object oTranmoney = null;

      for (int i = 0; i < bodyvos.length; i++) {
        oTranmoney = bodyvos[i].getAttributeValue("dtranmoney");
        dTranmoneySum = dTranmoneySum.add(oTranmoney == null ? new UFDouble(0)
            : (UFDouble) oTranmoney);
      }

      if (dTranmoneySum.doubleValue() > 0) {
        showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "40140418", "UPP40140418-000024")/* @res "发票已核销，不可删除！" */);
        return;
      }

      // 删除
      dvo.setStatusTo(VOStatus.DELETED);

      InvoiceBO_Client.save(dvo);

      m_alAllVOs.remove(m_selectRow);

      reLoadListData();

      getBillCardPanel().getBillData().clearViewData();
      getBillCardPanel().updateValue();

      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
          "UCH006")/* @res "删除成功！" */);

    }
    catch (Exception e) {
      ExceptionUITools tool = new ExceptionUITools();
      tool.showMessage(e, this);
      return;
    }
  }

  /**
   * 查询发运单。
   */
  public void onDelivBill() {
    if (m_condClient == null)
      initQuery();
    m_condClient.showModal();
    if (!m_condClient.isCloseOK())
      return;

    String sWhere = getSQL(m_condClient.getConditionVO());

    // 根据查询条件显示发票要参照的发运单
    DMVO dmvo = getChooseDeliveBillDLG(sWhere);

    if (dmvo == null)
      return;

    // 需要得到所选发运单的vos，用于发票保存时应记录的核销关系

    // 记录所选发运单ids
    m_sDelivbillbs = (String[]) dmvo.getHeaderVO().getAttributeValue(
        "pk_delivbill_bs");
    getBillCardPanel().setBillValueVO(dmvo);

    getBillCardPanel().getTailItem("pkbillperson").setValue(getOperator());
    // getBillCardPanel().getTailItem("billpersonname").setValue(getClientEnvironment().getUser().getUserName());
    getBillCardPanel().getTailItem("billdate").setValue(
        getClientEnvironment().getDate());

    DMDataVO header = (DMDataVO) dmvo.getParentVO();
    Integer isendtype = (Integer) header.getAttributeValue("isendtype");
    UIComboBox betimes = (UIComboBox) getBillCardPanel().getHeadItem(
        "isendtype").getComponent();
    switch (isendtype.intValue()) {
      case FreightType.ap:
        betimes.setSelectedIndex(1);
        break;
      case FreightType.ar:
        betimes.setSelectedIndex(2);
        break;
    }
    getBillCardPanel().getHeadItem("pk_transcust").setEnabled(false);
    getBillCardPanel().getHeadItem("pkcustinvoice").setEnabled(false);
    getBillCardPanel().getHeadItem("isendtype").setEnabled(false);

    // 自动增行,并将总费用带入价税合计
    getBillCardPanel().addLine();
    getBillCardPanel().setBodyValueAt(
        dmvo.getHeaderVO().getAttributeValue("totalfee"), 0, "dtaxmoney");
    //

    bFromDelivbill = true;

    String message = NCLangRes.getInstance().getStrByID("40140418",
        "UPT40140418-000020");
    this.showHintMessage(message);
  }

  /**
   * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2002-9-1 17:23:32) 修改日期，修改人，修改原因，注释标志：
   */
  public void onEdit() {

    DMVO dvo = new DMVO();

    dvo = (DMVO) getBillCardPanel().getBillValueVO("nc.vo.dm.pub.DMVO",
        "nc.vo.dm.pub.DMDataVO", "nc.vo.dm.pub.DMDataVO");

    String pkapprperson = (String) dvo.getParentVO().getAttributeValue(
        "pkapprperson"); // 审核人

    if (pkapprperson != null && pkapprperson.trim().length() != 0) {
      showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40140418",
          "UPP40140418-000026")/* @res "发票已审核，不可修改！" */);
      return;
    }

    DMDataVO[] bodyvos = (DMDataVO[]) dvo.getChildrenVO();

    UFDouble dTranmoneySum = new UFDouble(0);
    UFDouble dTranmoney = new UFDouble(0);
    Object oTranmoney = null;

    for (int i = 0; i < bodyvos.length; i++) {
      oTranmoney = bodyvos[i].getAttributeValue("dtranmoney");
      dTranmoneySum = dTranmoneySum.add(oTranmoney == null ? new UFDouble(0)
          : (UFDouble) oTranmoney);
    }

    if (dTranmoneySum.doubleValue() > 0) {
      showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40140418",
          "UPP40140418-000027")/* @res "发票已核销，不可修改！" */);
      return;
    }

    afterDept();

    super.onEdit();
    getBillCardPanel().getHeadItem("isendtype").setEnabled(false);
    // 承运商控制
    if (getBillCardPanel().getHeadItem("isendtype").getValue().equals(
        FreightType.ap + "")) {
      getBillCardPanel().getHeadItem("pk_transcust").setEnabled(true);
    }
    else {
      getBillCardPanel().getHeadItem("pk_transcust").setEnabled(false);
    }
    String message = NCLangRes.getInstance().getStrByID("common", "UCH027");
    /* @res正在修改 */
    this.showHintMessage(message);
  }

  /**
   * 切换按钮。 创建日期：(2002-5-17 20:26:04)
   */
  public void onFrist() {
    m_selectRow = 0;

    String message = NCLangRes.getInstance().getStrByID("common", "UCH031");
    /* @res首页 */
    this.showHintMessage(message);
  }

  /**
   * 切换按钮。 创建日期：(2002-5-17 20:26:04)
   */
  public void onLast() {
    String state = getShowState();

    String message = NCLangRes.getInstance().getStrByID("common", "UCH032");
    /* @res末页 */
    this.showHintMessage(message);
  }

  /**
   * 切换按钮。 创建日期：(2002-5-17 20:26:04)
   */
  public void onNext() {
    try {
      String state = getShowState();
      if (m_VOs == null)
        return;
      if (m_selectRow >= m_VOs.length - 1) {
        this.showOkCancelMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "40140418", "UPP40140418-000028")/* @res "已经到达最后一行" */);
        return;
      }
      m_selectRow = m_selectRow + 1;
      if (m_selectRow >= 0)
        refreshCardTable();

      String message = NCLangRes.getInstance().getStrByID("common", "UCH034");
      /* @res下一页 */
      this.showHintMessage(message);
    }
    catch (Exception e) {
      ExceptionUITools tool = new ExceptionUITools();
      tool.showMessage(e, this);
    }
  }

  /**
   * 切换按钮。 创建日期：(2002-5-17 20:26:04)
   */
  public void onPre() {
    try {
      if (m_VOs == null)
        return;

      if (m_selectRow <= 0) {
        this.showOkCancelMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "40140418", "UPP40140418-000029")/* @res "已经到达第一行" */);
        return;
      }
      m_selectRow = m_selectRow - 1;
      if (m_selectRow >= 0 && m_selectRow < m_VOs.length)
        refreshCardTable();

      String message = NCLangRes.getInstance().getStrByID("common", "UCH033");
      /* @res上一页 */
      this.showHintMessage(message);
    }
    catch (Exception e) {
      ExceptionUITools tool = new ExceptionUITools();
      tool.showMessage(e, this);
    }
  }

  /**
   * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2002-11-13 10:24:43) 修改日期，修改人，修改原因，注释标志：
   */
  public void onPrint() {
    String sBillPkKey = "pk_delivinvoice_h";
    AggregatedValueObject cardVO;

    try {
      if (m_strShowState.equals(DMBillStatus.Card)) {

        cardVO = (AggregatedValueObject) getAllVOs().get(m_selectRow);

        // getBillCardPanel().getBillModel().execLoadFormula();
        // getBillCardPanel().execHeadTailLoadFormulas();
        //
        // //为了支持打印中所有根据公式计算出来的值被打印出来
        // //故执行如下两条语句
        // cardVO =
        // getBillCardPanel().getBillValueVO(
        // "nc.vo.dm.pub.DMVO",
        // "nc.vo.dm.pub.DMDataVO",
        // "nc.vo.dm.pub.DMDataVO");
        // getAllVOs().set(m_selectRow, cardVO);

        FormulaTools.execFormulas(cardVO, (BillItem[]) FormulaTools
            .combineArray(getBillCardPanel().getHeadItems(), getBillCardPanel()
                .getTailItems()), getBillCardPanel().getBillModel()
            .getBodyItems());

        // ((DMBillCardPanel) getBillCardPanel()).onCardPrint(vo);

        String oldPrimaryKey; // 用于记录原始的 primaryKey 的值，便于打印后恢复

        // 必须调用方法 cardVO.getParentVO().setPrimaryKey( xx )
        // 以设置 vo 的 billpk, 否则打印的次数不能更新
        ArrayList alBill = new ArrayList();

        oldPrimaryKey = cardVO.getParentVO().getPrimaryKey();

        cardVO.getParentVO().setPrimaryKey(
            (String) cardVO.getParentVO().getAttributeValue(sBillPkKey));
        alBill.add(cardVO);

        BillPrintTool bpt = new BillPrintTool(
            DMBillNodeCodeConst.m_delivInvoice, alBill, getBillCardPanel()
                .getBillData(), null, null, null, "vinvoicenumber", sBillPkKey);

        bpt.onCardPrint(getBillCardPanel(), getBillListPanel(),
            DMBillTypeConst.m_delivInvoice);

        // 打印后恢复 primaryKey 的值
        cardVO.getParentVO().setPrimaryKey(oldPrimaryKey);
      }
      else if (m_strShowState.equals(DMBillStatus.List)) {
        // onListPrint(getBillCardPanel(), getAllVOs());

        ArrayList alvo = getAllVOs();
        if (alvo.size() > 0) {
          String[] oldPrimaryKeys = new String[alvo.size()]; // 用于记录原始的
          // primaryKey
          // 的值，便于打印后恢复
          for (int i = 0; i < alvo.size(); i++) {
            cardVO = (AggregatedValueObject) alvo.get(i);
            oldPrimaryKeys[i] = cardVO.getParentVO().getPrimaryKey();

            // 必须调用方法 cardVO.getParentVO().setPrimaryKey( xx )
            // 以设置 vo 的 billpk, 否则打印的次数不能更新
            cardVO.getParentVO().setPrimaryKey(
                (String) cardVO.getParentVO().getAttributeValue(sBillPkKey));
          }

          FormulaTools.execFormulas(alvo, (BillItem[]) FormulaTools
              .combineArray(getBillCardPanel().getHeadItems(),
                  getBillCardPanel().getTailItems()), getBillCardPanel()
              .getBillModel().getBodyItems());

          BillPrintTool bpt = new BillPrintTool(
              DMBillNodeCodeConst.m_delivInvoice, alvo, getBillCardPanel()
                  .getBillData(), null, null, null, "vinvoicenumber",
              sBillPkKey);

          bpt.onBatchPrint(getBillListPanel(), DMBillTypeConst.m_delivInvoice);

          // 打印后恢复 primaryKey 的值
          for (int i = 0; i < alvo.size(); i++) {
            cardVO = (AggregatedValueObject) alvo.get(i);
            cardVO.getParentVO().setPrimaryKey(oldPrimaryKeys[i]);
          }
        }
      }

      String message = NCLangRes.getInstance().getStrByID("common", "UCH061");
      /* @res正在打印 */
      this.showHintMessage(message);
    }
    catch (Exception e) {
      ExceptionUITools tool = new ExceptionUITools();
      tool.showMessage(e, this);
    }
  }

  /**
   * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2002-11-13 10:24:43) 修改日期，修改人，修改原因，注释标志：
   */
  public void onPrintPreview() {
    String sBillPkKey = "pk_delivinvoice_h";
    AggregatedValueObject cardVO;

    try {
      if (m_strShowState.equals(DMBillStatus.Card)) {

//        cardVO = (AggregatedValueObject) getAllVOs().get(m_selectRow);
//
//        // getBillCardPanel().getBillModel().execLoadFormula();
//        // getBillCardPanel().execHeadTailLoadFormulas();
//        //
//        // //为了支持打印中所有根据公式计算出来的值被打印出来
//        // //故执行如下两条语句
//        // cardVO =
//        // getBillCardPanel().getBillValueVO(
//        // "nc.vo.dm.pub.DMVO",
//        // "nc.vo.dm.pub.DMDataVO",
//        // "nc.vo.dm.pub.DMDataVO");
//        // getAllVOs().set(m_selectRow, cardVO);
//
//        FormulaTools.execFormulas(cardVO, (BillItem[]) FormulaTools
//            .combineArray(getBillCardPanel().getHeadItems(), getBillCardPanel()
//                .getTailItems()), getBillCardPanel().getBillModel()
//            .getBodyItems());
//
//        // ((DMBillCardPanel) getBillCardPanel()).onCardPrint(vo);
//
//        String oldPrimaryKey; // 用于记录原始的 primaryKey 的值，便于打印后恢复
//
//        // 必须调用方法 cardVO.getParentVO().setPrimaryKey( xx )
//        // 以设置 vo 的 billpk, 否则打印的次数不能更新
//        ArrayList alBill = new ArrayList();
//
//        oldPrimaryKey = cardVO.getParentVO().getPrimaryKey();
//
//        cardVO.getParentVO().setPrimaryKey(
//            (String) cardVO.getParentVO().getAttributeValue(sBillPkKey));
//        alBill.add(cardVO);
        //alBill.
        SCMPrintDirect.preview(getBillCardPanel(), getTitle());
//        BillPrintTool bpt = new BillPrintTool(
//            DMBillNodeCodeConst.m_delivInvoice, alBill, getBillCardPanel()
//                .getBillData(), null, null, null, "vinvoicenumber", sBillPkKey);
//
//        bpt.onCardPrintPreview(getBillCardPanel(), getBillListPanel(),
//            DMBillTypeConst.m_delivInvoice);
//
//        // 打印后恢复 primaryKey 的值
//        cardVO.getParentVO().setPrimaryKey(oldPrimaryKey);
      }
      else if (m_strShowState.equals(DMBillStatus.List)) {
        // onListPrint(getBillCardPanel(), getAllVOs());
//
//        ArrayList alvo = getAllVOs();
//        if (alvo.size() > 0) {
//          String[] oldPrimaryKeys = new String[alvo.size()]; // 用于记录原始的
//          // primaryKey
//          // 的值，便于打印后恢复
//          for (int i = 0; i < alvo.size(); i++) {
//            cardVO = (AggregatedValueObject) alvo.get(i);
//            oldPrimaryKeys[i] = cardVO.getParentVO().getPrimaryKey();
//
//            // 必须调用方法 cardVO.getParentVO().setPrimaryKey( xx )
//            // 以设置 vo 的 billpk, 否则打印的次数不能更新
//            cardVO.getParentVO().setPrimaryKey(
//                (String) cardVO.getParentVO().getAttributeValue(sBillPkKey));
//          }
//
//          FormulaTools.execFormulas(alvo, (BillItem[]) FormulaTools
//              .combineArray(getBillCardPanel().getHeadItems(),
//                  getBillCardPanel().getTailItems()), getBillCardPanel()
//              .getBillModel().getBodyItems());
//
//          BillPrintTool bpt = new BillPrintTool(
//              DMBillNodeCodeConst.m_delivInvoice, alvo, getBillCardPanel()
//                  .getBillData(), null, null, null, "vinvoicenumber",
//              sBillPkKey);
//
//          bpt.onBatchPrintPreview(getBillListPanel(),
//              DMBillTypeConst.m_delivInvoice);
//
//          // 打印后恢复 primaryKey 的值
//          for (int i = 0; i < alvo.size(); i++) {
//            cardVO = (AggregatedValueObject) alvo.get(i);
//            cardVO.getParentVO().setPrimaryKey(oldPrimaryKeys[i]);
//          }
//        }
          showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
                  "40140417", "UPP40140417-000023")/* @res "请在卡片界面打印！" */);
      }
      String message = NCLangRes.getInstance().getStrByID("common", "UCH061");
      /* @res正在打印 */
      this.showHintMessage(message);
    }
    catch (Exception e) {
      ExceptionUITools tool = new ExceptionUITools();
      tool.showMessage(e, this);
    }
  }

  /**
   *
   *
   */
  public void onRefresh() {
    if (getQueryConditionDlg() == null) {
      return;
    }

    // 获得查询条件
    nc.vo.pub.query.ConditionVO[] voCons = getQueryConditionDlg()
        .getConditionVO();
    voCons = nc.ui.scm.pub.query.ConvertQueryCondition.getConvertedVO(voCons,
        null);

    getQueryConditionDlg().checkCondition(voCons);

    String message = NCLangRes.getInstance().getStrByID("common", "UCH007");
    /* @res刷新成功 */
    this.showHintMessage(message);

    afterQuery(voCons);
  }

  /**
   * 此处插入方法说明。 创建日期：(2002-6-7 15:58:25)
   */
  public void onQuery() {

    // queryConditionDlg = null;
    if (getQueryConditionDlg() == null) {

      setQueryConditionDlg(new QueryConditionDlg(this));
      getQueryConditionDlg().setDefaultCloseOperation(
          javax.swing.WindowConstants.HIDE_ON_CLOSE);
      getQueryConditionDlg().setDefaultValue("dm_delivinvoice_h.pkdelivorg",
          getDelivOrgPK(), null);
      getQueryConditionDlg().setConditionEditable(
          "dm_delivinvoice_h.pkdelivorg", false);

      // 存货
      UIRefPane ref = new UIRefPane();
      ref.setRefNodeName("存货档案");
      ref.setWhereString("bd_invbasdoc.laborflag='Y' "
          + " and bd_invmandoc.sealflag ='N' and bd_invmandoc.pk_corp='"
          + getDelivCorp() + "'");
      getQueryConditionDlg().setValueRef("dm_delivinvoice_b.pkinv", ref);

      getQueryConditionDlg().setInitDate("dm_delivinvoice_h.dinvoicedate",
          getClientEnvironment().getDate().toString());

      nc.ui.scm.pub.def.DefSetTool.updateQueryConditionClientUserDef(
          (nc.ui.pub.query.QueryConditionClient) getQueryConditionDlg(),
          getCorpPrimaryKey(), vUserdefCode,"dm_delivinvoice_h.vuserdef", null, 1,
          "dm_delivinvoice_b.vuserdef", null, 1);

      // 承运商
      UIRefPane rfpTranCust = new UIRefPane();
      TrancustRefModel rfm = new TrancustRefModel();
      rfm.setDelivOrgPK(getDelivOrgPK());
      rfpTranCust.setRefModel(rfm);
      getQueryConditionDlg().setValueRef("dm_delivinvoice_h.pk_transcust",
          rfpTranCust);

      // 运费发票状态
      getQueryConditionDlg().setCombox(
          "billstatus",
          new String[][] {
              {
                  CConstant.ALL, ""
              },
              {
                  "0",
                  nc.ui.ml.NCLangRes.getInstance().getStrByID("40140418",
                      "UPP40140418-000080")
              /* @res "自由状态" */},
              {
                  "1",
                  nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
                      "UC000-0001558")
              /* @res "审批状态" */}
          });

      // 运费发票是否参与核销
      getQueryConditionDlg().setCombox(
          "dm_delivinvoice_h.bisverify",
          new String[][] {
              {
                  CConstant.ALL, ""
              },
              {
                  CConstant.NOT,
                  nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
                      "UPPSCMCommon-000108")
              /* @res "否" */},
              {
                  CConstant.YES,
                  nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
                      "UPPSCMCommon-000244")
              /* @res "是" */}
          });
      // 运费类别
      getQueryConditionDlg().setCombox("dm_delivinvoice_h.isendtype",
          new String[][] {
              {
                  "", ""
              }, {
                  FreightType.ap + "", FreightType.sAp
              }, {
                  FreightType.ar + "", FreightType.sAr
              }
          });
    }
    UIRefPane refpane = new UIRefPane();
    String sql = " bd_cumandoc.pk_corp = '"
      + getBelongCorpIDofDelivOrg() + "' and custflag in( '2', '0')";
    CustmandocDefaultRefModel model = new CustmandocDefaultRefModel( "客商档案" );
    model.setWherePart( sql );
    refpane.setRefModel( model );
    getQueryConditionDlg().setValueRef( "dm_delivinvoice_h.pkcustinvoice" , refpane);;
    

    getQueryConditionDlg().hideNormal();
    getQueryConditionDlg().showModal();

    if (!getQueryConditionDlg().isCloseOK())
      return;

    // 获得查询条件
    nc.vo.pub.query.ConditionVO[] voCons = getQueryConditionDlg()
        .getConditionVO();
    voCons = nc.ui.scm.pub.query.ConvertQueryCondition.getConvertedVO(voCons,
        null);

    getQueryConditionDlg().checkCondition(voCons);

    // 条件转换
    for (int i = 0; i < voCons.length; i++) {
      // 单据状态
      if (voCons[i].getFieldCode().equals("billstatus")) {
        if (voCons[i].getValue().equals("0")) {
          voCons[i].setFieldCode("dm_delivinvoice_h.pkapprperson");
          voCons[i].setOperaCode("is");
          voCons[i].setValue("null");
        }
        else if (voCons[i].getValue().equals("1")) {
          voCons[i].setFieldCode("dm_delivinvoice_h.pkapprperson");
          voCons[i].setOperaCode("is");
          voCons[i].setValue("not null");
        }
      }
      // 是否参与核销
      else if (voCons[i].getFieldCode().equals("dm_delivinvoice_h.bisverify")) {
        if (voCons[i].getValue().equals(CConstant.YES)) {
          voCons[i].setValue("'Y'");
        }
        else {
          voCons[i].setValue("'N' or dm_delivinvoice_h.bisverify is null ");
        }
      }
    }
    String message = NCLangRes.getInstance().getStrByID("common", "UCH009");
    /* @res查询完成 */
    this.showHintMessage(message);

    afterQuery(voCons);
  }

  public void onSave() {
    onSaveAction();
  }

  /**
   * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2002-8-21 19:28:54) 修改日期，修改人，修改原因，注释标志：
   */
  public boolean onSaveAction() {
    getBillCardPanel().stopEditing();

    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40140418",
        "UPP40140418-000030")/* @res "开始保存！！！" */);

    try {

      DMVO dvo = new DMVO();
      dvo = (DMVO) getBillCardPanel().getBillValueChangeVO("nc.vo.dm.pub.DMVO",
          "nc.vo.dm.pub.DMDataVO", "nc.vo.dm.pub.DMDataVO");

      // 操作人
      dvo.getParentVO().setAttributeValue("userid", getOperator());

      // 修改保存校验
      if (getEditFlag() == DMBillStatus.CardEdit) {
        if (!checkEdit(dvo)) {
          return false;
        }
      }

      // 获得未改变表体数据

      DMVO nochangevo = ((DMBillCardPanel) getBillCardPanel())
          .getBillValueNoChangedBodyVOs();

      if (getEditFlag() == DMBillStatus.CardNew) {
        dvo.getParentVO().setAttributeValue("bisverify", new UFBoolean(true));
        dvo.getParentVO().setStatus(nc.vo.pub.VOStatus.NEW);
        dvo.getParentVO().setAttributeValue("pkdelivorg", getDelivOrgPK());

      }
      else if (getEditFlag() == DMBillStatus.CardEdit)
        dvo.getParentVO().setStatus(VOStatus.UPDATED);

      // 增加表体生成OID的字段

      DMDataVO[] bodyvos = (DMDataVO[]) dvo.getChildrenVO();

      for (int i = 0; i < bodyvos.length; i++) {
        bodyvos[i].setAttributeValue("pk_corp", getCorpID());
        // StaticMemoryVariable.BelongCorpIDofDelivOrg);
      }

      // 保存校验

      if (!checkVO(dvo)) {
        return false;
      }

      // 生成单据号

      if (dvo.getParentVO().getAttributeValue("vinvoicenumber") == null
          || dvo.getParentVO().getAttributeValue("vinvoicenumber").toString()
              .trim().length() == 0) {
        if (!GeneralMethod.setBillCode(dvo.getParentVO(), "7V",
            getBillCardPanel(), "vinvoicenumber", "pk_corp")) {
          nc.ui.pub.beans.MessageDialog
              .showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID(
                  "SCMCOMMON", "UPPSCMCommon-000059")/* @res "错误" */,
                  nc.ui.ml.NCLangRes.getInstance().getStrByID("40140418",
                      "UPP40140418-000031")/* @res "获得发票号失败！" */);
          return false;
        }

      }
      m_bIsGetNewBillCode = true;

      // 自动核销
      // String[]

      if (bFromDelivbill && getEditFlag() == DMBillStatus.CardNew) {
        if (showOkCancelMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "40140418", "UPP40140418-000032")/* @res "此保存操作将自动核销运费单，是否继续？" */) == UIDialog.ID_CANCEL) {
          return false;
        }
        else {
          if (!autoVerify(dvo))
            return false;
        }
      }

      // 置入CL信息
      dvo.getParentVO().setAttributeValue("userid",
          getClientEnvironment().getUser().getPrimaryKey());

      // 最后修改信息
      if (getEditFlag() == DMBillStatus.CardNew) {
        dvo.getParentVO().setAttributeValue("tmaketime",
            getClientEnvironment().getServerTime());
      }
      dvo.getParentVO().setAttributeValue("clastmodifierid",
          getClientEnvironment().getUser().getPrimaryKey());
      dvo.getParentVO().setAttributeValue("tlastmodifytime",
          getClientEnvironment().getServerTime());
      dvo.getParentVO().setAttributeValue("bisverify", "N");//eric
      

      // 保存
      m_dvosave = InvoiceBO_Client.autoVerify(dvo, m_sDelivbillbs);
      
      // 组和数据

      dvo.combineOtherVO(m_dvosave);

      dvo.trueDeleteBodyVO((DMDataVO[]) dvo.getChildrenVO(),
          "pk_delivinvoice_b");
      dvo.appendBodyVO(nochangevo);

      if (getEditFlag() == DMBillStatus.CardNew) {
        m_selectRow = m_alAllVOs.size();
        m_alAllVOs.add(m_selectRow, dvo);
      }
      else
        m_alAllVOs.set(m_selectRow, dvo);

      reLoadListData();

      getBillCardPanel().setBillValueVO(dvo);
      getBillCardPanel().updateValue();
      getBillCardPanel().setEnabled(false);
      switchButtonStatus(DMBillStatus.CardView);
      setEditFlag(DMBillStatus.CardView );
//      setEditFlag(DMBillStatus.CardView);
      // 清空所记录的发运单ids
      m_sDelivbillbs = null;
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40140418",
          "UPP40140418-000033")/* @res "保存成功！！！" */);

    }
    catch (Exception ex) {
      ExceptionUITools tool = new ExceptionUITools();
      tool.showMessage(ex, this);
      return false;
    }
    return true;
  }

  /**
   * 函数的功能、用途、对属性的更改，以及函数执行前后对象的状态。
   * 
   * @param 参数说明
   * @return 返回值
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来。（可选）
   * @deprecated 该方法从类的那一个版本后，已经被其它方法替换。（可选）
   * @author zhongyue
   */
  public void onSwith() {
    super.onSwith();
    if (getShowState().equals(DMBillStatus.List)) {
      if (m_VOs != null && m_selectRow >= 0 && m_selectRow < m_VOs.length)
        getBillListPanel().getHeadTable().setRowSelectionInterval(m_selectRow,
            m_selectRow);
    }
    else {
      // getBillCardPanel().getBillModel().execLoadFormula();
    }
  }

  /**
   * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2002-8-29 14:06:39) 修改日期，修改人，修改原因，注释标志：
   */
  public void onVerify() {
    if (getBillCardPanel().getHeadItem("bisverify").getValue() != null
        && new UFBoolean(getBillCardPanel().getHeadItem("bisverify").getValue()
            .toString()).booleanValue()) {
      getVerifyDLG();

      String message = NCLangRes.getInstance().getStrByID("40140418",
          "UPT40140418-000022");
      this.showHintMessage(message);
      /* @res "核销" */
    }
    else {
      showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "40140418", "UPP40140418-000034")/* @res "不需要核销!" */);
    }

  }

  /**
   * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2002-8-29 14:06:39) 修改日期，修改人，修改原因，注释标志：
   */
  public void onVerifyBack() {
    // 显示查询对话框
    getQueryDlg().showModal();
    if (!getQueryDlg().isCloseOK())
      return;

    // 获得查询结果
    ConditionVO[] voCons = getQueryDlg().getConditionVO();

    for (int i = 0; i < voCons.length; i++) {
      if (voCons[i].getFieldCode().equals("dm_verifyrelation_h.dverifydate1"))
        voCons[i].setFieldCode("dm_verifyrelation_h.dverifydate");
      // sb.append(voCons[i].getSQLStr());
    }

    String s = (new ConditionVO()).getWhereSQL(voCons);

    m_VerifyBackDLG = new VerifyBackDLG(this, s);

    String sPK = null;

    if (m_VerifyBackDLG.showModal() == UIDialog.ID_OK) {
      // 反核销后，应将当前单据重新刷新一次，否则删除时有问题
      // 如果当前单据已经审核，当
      try {
        reLoadData();
      }
      catch (Exception ex) {
        ExceptionUITools tool = new ExceptionUITools();
        tool.showMessage(ex, this);
        return;
      }
    }
    String message = NCLangRes.getInstance().getStrByID("40140418",
        "UPT40140418-000024");
    /* @res "反核销" */
    this.showHintMessage(message);
  }

  /**
   * ?user> 功能： 参数： 返回： 例外： 日期：(2004-8-31 16:33:44) 修改日期，修改人，修改原因，注释标志：
   */
  private String processFirst(String s) {
    s = s.trim();

    int nNull = s.indexOf(" ");
    if (nNull > 0) {
      // String s1 = s.substring(0, nNull);
      String s2 = s.substring(nNull);
      s = " and (" + s2 + ")";
      return " " + s + " ";
    }

    return s;
  }

  /**
   * 此处插入方法说明。 创建日期：(2002-5-29 12:54:25)
   */
  public void refreshCardTable() {

	String pk_delval = (String) getBillListPanel().getHeadBillModel().getValueAt(m_selectRow, "pk_delivinvoice_h");
    getBillCardPanel().resumeValue();

    if (m_alAllVOs.size() == 0)
      return;

    else if (m_selectRow < 0)
      m_selectRow = 0;
  //2015-05-05 排序后主子表关联有误--wkf--
    DMVO tmpvos = null;
    for(int i = 0; i < m_alAllVOs.size(); i++) {
    	DMVO tmvo = (DMVO) m_alAllVOs.get(i);
	  	DMDataVO[] tmbvo =  (DMDataVO[]) tmvo.getChildrenVO();
	  	String tmpdel = (String) tmbvo[0].getAttributeValue("pk_delivinvoice_h");
	  	if(tmpdel.equals(pk_delval)){
  		  tmpvos= tmvo;
  		  break;
	  	}
    }
    // end ---wkf--
//    getBillCardPanel().setBillValueVO((DMVO) m_alAllVOs.get(m_selectRow));
    getBillCardPanel().setBillValueVO(tmpvos);
    getBillCardPanel().getBillModel().execLoadFormula();

    getBillCardPanel().updateValue();
  }

  private void reLoadData() throws Exception {
    loadBillData(m_conds);

    DMVO dvo = null;
    String sPK = getBillCardPanel().getHeadItem("pk_delivinvoice_h").getValue()
        .toString();
    for (int i = 0; i < m_VOs.length; i++) {
      if (sPK.equals(m_VOs[i].getParentVO().getAttributeValue(
          "pk_delivinvoice_h").toString())) {
        dvo = m_VOs[i];
        break;
      }
    }

    getBillCardPanel().setBillValueVO(dvo);
    getBillCardPanel().getBillModel().execLoadFormula();
    getBillCardPanel().updateValue();

    updateUI();
  }

  /**
   * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2002-11-12 19:49:28) 修改日期，修改人，修改原因，注释标志：
   */
  public void reLoadListData1() {

    m_VOs = new DMVO[m_alAllVOs.size()];

    for (int i = 0; i < m_alAllVOs.size(); i++) {
      m_VOs[i] = (DMVO) m_alAllVOs.get(i);
    }

    super.reLoadListData();

  }

  /**
   * 在此处插入方法说明。 创建日期：(2000-8-17 17:00:47)
   * 
   * @param status
   *          int
   */
  protected void switchButtonStatus(int status) {
    // boAdd, boEdit, boDel, boSave, boCancel, boLine, boPrint, boQuery,
    // boSwith

    if (status == DMBillStatus.CardView) // 列表初始，“保存”或“取消”后进入表单非编辑状态
    {
      setButton(boAdd, true);
      setButton(boEdit, true);
      setButton(boDel, true);

      setButton(boSave, false);
      setButton(boCancel, false);
      setButton(boLine, false);
      setButton(boAddLine, false);
      setButton(boDelLine, false);
      setButton(boDelivBill, false);
      setButton(boSwith, true);
      setButton(boPrint, true);
      setButton(boPrintPreview, true);
      setButton(boQuery, true);
      setButton(boRefresh, true);
      setButton(boBatchEditPrice,false);
      // 如果卡片没有单据，则不应该显示“执行”按钮
      // if (getBillCardPanel().getHeadItem("vinvoicenumber").getValue()
      // == null
      // || getBillCardPanel().getHeadItem("vinvoicenumber")
      // .getValue().length() == 0) {
      // setButton(boAction, false);
      // } else {
      // setButton(boAction, true);
      // }
      setButton(boAction, true);
      setButton(boVerify, true);
      setButton(boVerifyBack, true);// zhy
      //eric
      SwitchOnAudit();

    }
    else if (status == DMBillStatus.CardEdit) // “新增”后进入表单状态
    {
      setButton(boAdd, false);
      setButton(boEdit, false);
      setButton(boDel, false);
      setButton(boLine, true);
      setButton(boAddLine, true);
      setButton(boDelLine, true);
      setButton(boSave, true);
      setButton(boCancel, true);
      setButton(boDelivBill, false);
      setButton(boSwith, false);
      setButton(boPrint, false);
      setButton(boPrintPreview, false);
      setButton(boQuery, false);
      setButton(boRefresh, false);
      setButton(boAction, false);
      setButton(boVerify, false);
      setButton(boVerifyBack, false);// zhy
      setButton(boBatchEditPrice,true);//eric
    }
    else if (status == DMBillStatus.CardNew) // “新增”后进入表单状态
    {
      setButton(boAdd, false);
      setButton(boEdit, false);
      setButton(boDel, false);
      setButton(boLine, true);
      setButton(boAddLine, true);
      setButton(boDelLine, true);
      setButton(boSave, true);
      setButton(boCancel, true);
      setButton(boDelivBill, true);
      setButton(boSwith, false);
      setButton(boPrint, false);
      setButton(boPrintPreview, false);
      setButton(boQuery, false);
      setButton(boRefresh, false);
      setButton(boAction, false);
      setButton(boVerify, false);
      setButton(boVerifyBack, false);// zhy
      setButton(boBatchEditPrice,true);//eric
    }
    else if (status == DMBillStatus.ListView) //
    {
      setButton(boAdd, true);
      setButton(boEdit, true);
      setButton(boDel, true);
      setButton(boSave, false);
      setButton(boCancel, false);
      setButton(boAddLine, false);
      setButton(boDelLine, false);
      setButton(boLine, false);

      setButton(boSwith, true);
      setButton(boPrint, true);
      setButton(boPrintPreview, true);
      setButton(boQuery, true);
      setButton(boRefresh, true);
      setButton(boBatchEditPrice,false);
    }
    this.setExtendBtnsStat(status);
  }

  /**
   * 为了支持打印时修改 ts 的需求，搜索出列表下当前选中的行所对应的缓存数据
   * 
   * @return ArrayList 列表下当前选中的行所对应的缓存数据
   */
  private ArrayList getSelectedListBill4Print() {
    ArrayList alvo = new ArrayList();

    // 目前途损列表下只支持单选表头行
    // for (int i = 0; i <
    // getBillListPanel().getHeadTable().getSelectedRowCount(); i++) {
    // alvo.add(getModelVOs().getListVOs().get(getBillListPanel().getHeadTable().getSelectedRows()[i]));
    // }

    int iSelectedRow = getBillListPanel().getHeadTable().getSelectedRow();

    // 为了支持打印中所有根据公式计算出来的值被打印出来
    // 故执行如下语句
    DMDataVO head = (DMDataVO) getBillListPanel().getHeadBillModel()
        .getBodyValueRowVO(iSelectedRow, "nc.vo.dm.pub.DMDataVO");
    DMDataVO[] bodys = (DMDataVO[]) getBillListPanel().getBodyBillModel()
        .getBodyValueVOs("nc.vo.dm.pub.DMDataVO");

    DMVO currvo = new DMVO();
    currvo.setParentVO(head);
    currvo.setChildrenVO(bodys);

    getAllVOs().set(iSelectedRow, currvo);

    alvo.add(currvo);

    return alvo;
  }

  public ButtonObject[] getExtendBtns() {
    return null;
  }

  public void onExtendBtnsClick(ButtonObject bo) {
  }

  public void setExtendBtnsStat(int iState) {

  }

  // 由于需要支持二次开发接口nc.ui.scm.pub.bill.IBillExtendFun(，因此做下面的调整
  // 2005-09-23 v31sp1 钟鸣修改
  public ButtonObject[] getBillButtons() {
    ButtonObject[] buttons = super.getBillButtons();
    ButtonObject[] extraButtons = getExtendBtns();
    if (extraButtons != null && extraButtons.length > 0) {
      int buttonSize = buttons.length;
      int extraSize = extraButtons.length;
      int size = buttonSize + extraSize;

      ButtonObject[] allButtons = new ButtonObject[size];
      System.arraycopy(aryButtonGroup, 0, allButtons, 0, buttonSize);
      System.arraycopy(extraButtons, 0, allButtons, buttonSize, extraSize);
      buttons = allButtons;
    }
    return buttons;
  }

  public void doQueryAction(ILinkQueryData querydata) {
    String billid = querydata.getBillID();
    String pk_corp = querydata.getPkOrg();
    String billtype = querydata.getBillType();
    Object[] objs = (Object[]) querydata.getUserObject();
    String userid = objs[2].toString();
    initialize(pk_corp, billtype, null, userid, billid);
  }

}
