package nc.ui.ic.ic201;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.pps.IPricStl;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.ui.bd.CorpBO_Client;
import nc.ui.ic.auditdlg.ClientUIInAndOut;
import nc.ui.ic.ic211.GeneralHHelper;
import nc.ui.ic.pub.bill.GeneralBillClientUI;
import nc.ui.ic.pub.bill.GeneralBillHelper;
import nc.ui.ic.pub.bill.GeneralBillUICtl;
import nc.ui.ic.pub.bill.initref.RefFilter;
import nc.ui.ic.pub.bill.query.QueryConditionDlgForBill;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.scm.pub.report.BillRowNo;
import nc.vo.bd.CorpVO;
import nc.vo.ic.pub.BillTypeConst;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.lang.ResBase;
import nc.vo.ic.pub.locator.LocatorVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.ic.bill.InvVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.session.ClientLink;

public class ClientUI extends GeneralBillClientUI
{
  private ClientUIInAndOut m_dlgInOut = null;

  private ArrayList m_alBrwLendBusitype = null;

  private final String m_sBusiTypeItemKey = "cbiztype";

  public ClientUI() {
    initialize();
  }

  public ClientUI(String pk_corp, String billType, String businessType, String operator, String billID)
  {
    super(pk_corp, billType, businessType, operator, billID);
  }

  //设置表体默认数据
  public void setBodyDefaultData(int istartrow, int count) {
    if (getBillCardPanel().getRowCount() > 0) {
      Object obj = getBillCardPanel().getBodyValueAt(0, "csourcebillbid");
      if ((obj != null) && (obj.toString().trim().length() > 0)) {
        return;
      }
    }
    String pk_corp = getBillCardPanel().getHeadItem("pk_corp").getValue();

    String calid = getBillCardPanel().getHeadItem("pk_calbody").getValue();
    String whid = getBillCardPanel().getHeadItem("cwarehouseid").getValue();

    for (int i = istartrow; i < count; i++) {
      String reqCorp = (String)getBillCardPanel().getBodyValueAt(i, 
        "pk_reqcorp");
      if ((reqCorp == null) || (reqCorp.trim().length() == 0)) {
        getBillCardPanel().setBodyValueAt(pk_corp, i, "pk_reqcorp");
        getBillCardPanel().getBillModel().execEditFormulaByKey(i, 
          "creqcorpname");
      }

      String reqCal = (String)getBillCardPanel().getBodyValueAt(i, 
        "pk_reqstoorg");
      if (reqCal == null) {
        getBillCardPanel().setBodyValueAt(calid, i, "pk_reqstoorg");
        getBillCardPanel().getBillModel().execEditFormulaByKey(i, 
          "creqstoorgname");
      }

      String reqWh = (String)getBillCardPanel().getBodyValueAt(i, 
        "pk_creqwareid");
      if (reqWh == null) {
        getBillCardPanel().setBodyValueAt(whid, i, "pk_creqwareid");
        getBillCardPanel().getBillModel().execEditFormulaByKey(i, 
          "creqwarename");
      }

      String invCorp = (String)getBillCardPanel().getBodyValueAt(i, 
        "pk_invoicecorp");
      if (invCorp == null) {
        getBillCardPanel().setBodyValueAt(pk_corp, i, "pk_invoicecorp");
        getBillCardPanel().getBillModel().execEditFormulaByKey(i, 
          "cinvoicecorpname");
      }
    }
  }

  //单据编辑之后调用此方法
  protected void afterBillEdit(BillEditEvent e) {
    String sItemKey = e.getKey();
    int row = e.getRow();
    if ("vbodynote2".equalsIgnoreCase(sItemKey)) {
      UIRefPane refPaneReasonBody = (UIRefPane)getBillCardPanel()
        .getBodyItem("vbodynote2").getComponent();

      String sBodyNoteCode = refPaneReasonBody.getText();
      String sBodyNoteCodeContent = (String)refPaneReasonBody
        .getRefValue("cbackreasonname");

      String sReturnResult = null;

      if (sBodyNoteCodeContent != null)
        sReturnResult = sBodyNoteCodeContent;
      else if (sBodyNoteCode != null) {
        sReturnResult = sBodyNoteCode;
      }
      if (sReturnResult != null)
        getBillCardPanel().setBodyValueAt(sReturnResult, row, 
          "vbodynote2");
    }
    if ((getParentCorpCode().equals("10395")) && (
      (sItemKey.equals("cwarehouseid")) || 
      (sItemKey.equals("cinventorycode")))) {
      String cwarehouseid = ((UIRefPane)getBillCardPanel()
        .getHeadItem("cwarehouseid").getComponent()).getRefPK();

      if ((cwarehouseid == null) || (cwarehouseid.equals(""))) {
        return;
      }
      String sCode = ((UIRefPane)getBillCardPanel().getHeadItem(
        "cwarehouseid").getComponent()).getRefCode();

      if (Iscsflag(cwarehouseid))
      {
        int i = 0;
        while (i < getBillCardPanel().getBillTable()
          .getRowCount())
        {
          String cinventoryid = (String)getBillCardPanel()
            .getBodyValueAt(i, "cinventoryid");
          if ((cinventoryid == null) || (cinventoryid.equals("")))
          {
            return;
          }

          String Sql = "select * from (select d.pk_cargdoc,d.csname,d.cscode from ic_general_h a  ";
          Sql = Sql + "left join ic_general_b b on a.cgeneralhid=b.cgeneralhid  ";
          Sql = Sql + "left join ic_general_bb1 c on c.cgeneralbid=b.cgeneralbid  ";
          Sql = Sql + "left join bd_cargdoc d on c.cspaceid=d.pk_cargdoc  ";
          Sql = Sql + "where a.cbilltypecode ='45' and a.cwarehouseid='" + 
            cwarehouseid + 
            "' and  b.cinventoryid='" + 
            cinventoryid + "'  ";
          Sql = Sql + "and d.pk_cargdoc is not null  and a.taccounttime is not null and nvl(b.dr,0)=0 order by a.taccounttime desc  ";
          Sql = Sql + ") where rownum=1  ";
          IUAPQueryBS sessionManager = (IUAPQueryBS)
            NCLocator.getInstance().lookup(
            IUAPQueryBS.class.getName());
          List list = null;
          try {
            list = (List)sessionManager.executeQuery(Sql, 
              new ArrayListProcessor());

            if (list.isEmpty())
            {
              Sql = "select * from (select kp.cspaceid ,car.csname,car.cscode   ";
              Sql = Sql + "from   v_ic_onhandnum6 kp  ";
              Sql = Sql + "left join bd_cargdoc   car on car.pk_cargdoc=kp.cspaceid   ";
              Sql = Sql + "  where   kp.cspaceid <>'_________N/A________' and kp.cspaceid is not null and kp.cwarehouseid='" + 
                cwarehouseid + 
                "'  and  kp.cinventoryid='" + 
                cinventoryid + "')  ";
              Sql = Sql + "where rownum=1  ";
              list = (List)sessionManager.executeQuery(Sql, 
                new ArrayListProcessor());
              if (list.isEmpty()) {
                return;
              }
            }
          }
          catch (BusinessException e1)
          {
            e1.printStackTrace();

            Iterator iterator = list.iterator();
            while (iterator.hasNext()) {
              ArrayList values = new ArrayList();
              Object obj = iterator.next();
              if (obj == null) {
                continue;
              }
              if (obj.getClass().isArray()) {
                int len = Array.getLength(obj);
                for (int j = 0; j < len; j++) {
                  values.add(Array.get(obj, j));
                }

              }

              setSpace(String.valueOf(values.get(0)), 
                String.valueOf(values.get(1)), i);
            }
            i++;
          }
        }
      }
    }
  }

  public void bodyRowChange(BillEditEvent e)
  {
    super.bodyRowChange(e);
  }

  protected boolean checkVO(GeneralBillVO voBill) {
    return checkVO();
  }

  protected QueryConditionDlgForBill getConditionDlg() {
    if (this.ivjQueryConditionDlg == null) {
      this.ivjQueryConditionDlg = super.getConditionDlg();
      this.ivjQueryConditionDlg
        .setCombox(
        "freplenishflag", 
        new String[][] { 
        { 
        "1", 
        NCLangRes.getInstance().getStrByID(
        "4008busi", 
        "UPP4008busi-000367") }, 
        { 
        "2", 
        NCLangRes.getInstance().getStrByID(
        "4008busi", 
        "UPT40080602-000014") }, 
        { 
        "3", 
        NCLangRes.getInstance().getStrByID(
        "4008busi", 
        "UPPSCMCommon-000217") } });

      this.ivjQueryConditionDlg.setCorpRefs("head.pk_corp", 
        new String[] { "head.cproviderid" });
    }

    return this.ivjQueryConditionDlg;
  }

  protected ClientUIInAndOut getDispenseDlg(String sTitle, ArrayList alInVO, ArrayList alOutVO)
  {
    if (this.m_dlgInOut == null) {
      try {
        this.m_dlgInOut = new ClientUIInAndOut(this, sTitle);
      } catch (Throwable ivjExc) {
        handleException(ivjExc);
      }
    }

    this.m_voBill = 
      ((GeneralBillVO)((GeneralBillVO)this.m_alListData
      .get(this.m_iLastSelListHeadRow)).clone());

    this.m_dlgInOut.setVO(this.m_voBill, alInVO, alOutVO, 
      this.m_sBillTypeCode, this.m_voBill.getPrimaryKey().trim(), 
      this.m_sCorpID, this.m_sUserID);

    this.m_dlgInOut.setName("BillDlg");

    return this.m_dlgInOut;
  }

  public String getTitle() {
    return super.getTitle();
  }

  public void initialize() {
    super.initialize();
    long lTime = System.currentTimeMillis();

    getBillCardPanel().addBodyEditListener2(this);

    SCMEnv.showTime(lTime, "initialize:addBodyEditListener2:");

    lTime = System.currentTimeMillis();
    UIRefPane refPaneReason = (UIRefPane)getBillCardPanel().getHeadItem(
      "vheadnote2").getComponent();

    refPaneReason.setAutoCheck(false);
    refPaneReason.setReturnCode(false);
    UIRefPane refPaneReasonBody = (UIRefPane)getBillCardPanel()
      .getBodyItem("vbodynote2").getComponent();

    refPaneReasonBody.setAutoCheck(false);
    refPaneReasonBody.setReturnCode(true);
    SCMEnv.showTime(lTime, "initialize:init退货理由:");
  }

  protected void initPanel() {
    long lTime = System.currentTimeMillis();

    super.setBillInOutFlag(1);

    super.setNeedBillRef(true);

    this.m_sBillTypeCode = BillTypeConst.m_purchaseIn;

    this.m_sCurrentBillNode = "40080602";

    SCMEnv.showTime(lTime, "initPanel_1:");
  }

  protected boolean isBrwLendBiztype() {
    try {
      GeneralBillVO voMyBill = null;

      String sBusitypeid = null;
      if (this.m_iCurPanel == 4) {
        if ((this.m_alListData != null) && 
          (this.m_iLastSelListHeadRow >= 0) && 
          (this.m_alListData.size() > this.m_iLastSelListHeadRow) && 
          (this.m_alListData.get(this.m_iLastSelListHeadRow) != null)) {
          voMyBill = 
            (GeneralBillVO)this.m_alListData
            .get(this.m_iLastSelListHeadRow);

          sBusitypeid = (String)voMyBill.getHeaderValue("cbiztype");
        }
      }
      else if ((getBillCardPanel().getHeadItem("cbiztype") != null) && 
        (getBillCardPanel().getHeadItem("cbiztype")
        .getComponent() != null)) {
        UIRefPane ref = (UIRefPane)getBillCardPanel().getHeadItem(
          "cbiztype").getComponent();

        sBusitypeid = ref.getRefPK();
      }

      if ((sBusitypeid != null) && (this.m_alBrwLendBusitype == null)) {
        ArrayList alParam = new ArrayList();
        alParam.add(this.m_sCorpID);
        this.m_alBrwLendBusitype = ((ArrayList)
          GeneralBillHelper.queryInfo(new Integer(17), alParam));

        if (this.m_alBrwLendBusitype == null) {
          this.m_alBrwLendBusitype = new ArrayList();
        }
      }
      if ((sBusitypeid != null) && (this.m_alBrwLendBusitype != null) && 
        (this.m_alBrwLendBusitype.contains(sBusitypeid)))
        return true;
    }
    catch (Exception e) {
      SCMEnv.error(e);
    }
    return false;
  }

  public void onButtonClicked(ButtonObject bo) {
    showHintMessage(bo.getName());
    if (bo == getButtonTree().getButton("配套"))
      onDispense();
    else if (bo == getButtonTree().getButton("自制退库"))
      onNewReplenishInvBill();
    else if (bo == getButtonTree().getButton("采购订单退库"))
      onNewReplenishInvBillByOrder();
    else if (bo == getButtonTree().getButton("扣吨计算"))
      onKDJS();
    else
      super.onButtonClicked(bo);
  }

  public static void clearBillBodyItem(BillCardPanel bcp, String itemkey) {
    int rows = bcp.getBillTable().getRowCount();
    for (int i = 0; i < rows; i++)
      bcp.setBodyValueAt(null, i, itemkey);
  }

  private void onKDJS() {
    if (getMode() == 3) {
      showWarningMessage(ResBase.get201KD01());
      return;
    }
    int rows = getBillCardPanel().getBillTable().getRowCount();
    if (rows <= 0) {
      return;
    }

    String purcorp = getBillCardPanel().getHeadItem("pk_purcorp") == null ? null : 
      getBillCardPanel().getHeadItem("pk_purcorp").getValue();

    if (purcorp == null) {
      return;
    }
    if (!purcorp.equals(this.m_sCorpID)) {
      showWarningMessage(ResBase.get201KD03());
      return;
    }

    if ((getBillCardPanel().getBodyValueAt(0, "csourcebillbid") == null) || 
      (getBillCardPanel().getBodyValueAt(0, "csourcetype") == null) || 
      (!getBillCardPanel().getBodyValueAt(0, "csourcetype")
      .toString().equalsIgnoreCase("23"))) {
      showWarningMessage(ResBase.get201KD02());
      return;
    }

    clearBillBodyItem(getBillCardPanel(), "nkdnum");

    GeneralBillItemVO[] voaItem = this.m_voBill.getItemVOs();
    if (voaItem == null) {
      return;
    }
    UFDouble[] ufdArray = (UFDouble[])null;
    try {
      IPricStl obj = (IPricStl)NCLocator.getInstance().lookup(
        IPricStl.class.getName());

      ufdArray = obj.servForQnty(this.m_voBill.getItemVOs(), 
        new ClientLink(getClientEnvironment()));
    } catch (BusinessException exx) {
      SCMEnv.error(exx);
      showHintMessage(exx.getMessage());
    }

    HashMap map = new HashMap();
    if (ufdArray == null)
      return;
    for (int i = 0; i < ufdArray.length; i++) {
      Integer iI = new Integer(i);

      UFDouble ufdGrossNum = (UFDouble)voaItem[i]
        .getAttributeValue("ningrossnum");

      if ((ufdGrossNum == null) || (ufdArray[i] == null))
        map.put(iI, null);
      else {
        map.put(iI, ufdArray[i]);
      }

    }

    if (getBillCardPanel().getBodyItem("nkdnum") == null)
      return;
    for (int i = 0; i < rows; i++) {
      if (getBillCardPanel().getBodyValueAt(i, "cinventoryid") == null)
        continue;
      Integer iX = new Integer(i);

      UFDouble ufd = (UFDouble)map.get(iX);
      getBillCardPanel().setBodyValueAt(ufd, i, "nkdnum");

      getBillCardPanel().execBodyFormula(i, "ninnum");

      Object vl = getBillCardPanel().getBodyValueAt(i, "ninnum");

      getBillCardPanel().execBodyFormula(i, "nkdnum");

      afterNumEdit(new BillEditEvent(this, null, vl, "ninnum", i, 1));

      getBillCardPanel().getBillModel().setRowState(i, 2);
    }
  }

  public void addRowNums(int rownums) {
    super.addRowNums(rownums);

    setBodyDefaultData(0, rownums);

    String pk_corp = getClientEnvironment().getCorporation().getPk_corp();
    getBillCardPanel().setHeadItem("pk_purcorp", pk_corp);
    BillItem it = getBillCardPanel().getHeadItem("pk_purcorp");

    getBillCardPanel().execHeadTailEditFormulas(it);
  }

  private void onDispense() {
    if ((3 != this.m_iMode) || (isSigned() == 1) || 
      (getSourBillTypeCode() == null) || 
      (getSourBillTypeCode().startsWith("4"))) {
      return;
    }
    if (getBillCardPanel().getBillTable().getSelectedRows().length < 1)
      return;
    if (2 == MessageDialog.showOkCancelDlg(this, null, 
      NCLangRes.getInstance().getStrByID("4008busi", "UPP4008busi-000268"))) {
      return;
    }

    GeneralBillVO voBill = (GeneralBillVO)
      ((GeneralBillVO)this.m_alListData
      .get(this.m_iLastSelListHeadRow)).clone();

    GeneralBillVO voBillclone = (GeneralBillVO)voBill.clone();

    ArrayList alOutGeneralVO = new ArrayList();
    ArrayList alInGeneralVO = new ArrayList();

    ArrayList aloutitem = new ArrayList();
    ArrayList alinitem = new ArrayList();
    int[] rownums = getBillCardPanel().getBillTable().getSelectedRows();

    for (int i = 0; i < rownums.length; i++) {
      if (!isSetInv(voBill, rownums[i]))
        continue;
      if (isDispensedBill(voBill, rownums[i]))
      {
        continue;
      }
      GeneralBillItemVO voParts = voBill.getItemVOs()[rownums[i]];
      UFDouble ufSetNum = null;

      ufSetNum = voParts.getNinnum();
      voParts.setAttributeValue("nshouldoutnum", voParts.getNinnum());
      voParts.setAttributeValue("nshouldoutassistnum", 
        voParts.getNinassistnum());

      voParts.setAttributeValue("noutnum", voParts.getNinnum());
      voParts.setAttributeValue("noutassistnum", 
        voParts.getNinassistnum());

      voParts.setAttributeValue("ninnum", null);
      voParts.setAttributeValue("ninassistnum", null);
      voParts.setAttributeValue("nshouldinnum", null);
      voParts.setAttributeValue("nneedinassistnum", null);
      voParts.setDbizdate(new UFDate(this.m_sLogDate));

      voParts.setAttributeValue("csourcetype", voBill.getHeaderVO()
        .getCbilltypecode());

      voParts.setAttributeValue("csourcebillhid", voBill.getHeaderVO()
        .getPrimaryKey());

      voParts.setAttributeValue("csourcebillbid", 
        voBill.getItemVOs()[rownums[i]].getPrimaryKey());

      voParts.setAttributeValue("vsourcebillcode", voBill.getHeaderVO()
        .getVbillcode());

      voParts.setAttributeValue("ccorrespondcode", voBill.getHeaderVO()
        .getVbillcode());

      voParts.setAttributeValue("ccorrespondbid", 
        voBill.getItemVOs()[rownums[i]].getCgeneralbid());

      voParts.setCgeneralbid(null);
      voParts.setCgeneralbb3(null);
      voParts.setCsourceheadts(null);
      voParts.setCsourcebodyts(null);

      aloutitem.add(voParts);

      voParts.setLocator(null);
      GeneralBillItemVO[] tempItemVO = splitInvKit(voParts, 
        voBillclone.getHeaderVO(), ufSetNum);

      if ((tempItemVO != null) && (tempItemVO.length > 0)) {
        for (int j = 0; j < tempItemVO.length; j++)
          alinitem.add(tempItemVO[j]);
      }
      else {
        showErrorMessage(NCLangRes.getInstance().getStrByID("4008busi", 
          "UPP4008busi-000270"));

        return;
      }
    }

    if ((aloutitem.size() == 0) || (alinitem.size() == 0)) {
      return;
    }
    GeneralBillVO gbvoOUT = new GeneralBillVO();
    voBill.getHeaderVO().setCoperatorid(this.m_sUserID);
    voBill.getHeaderVO().setDbilldate(new UFDate(this.m_sLogDate));

    gbvoOUT.setParentVO(voBill.getParentVO());
    gbvoOUT.getHeaderVO().setPrimaryKey(null);
    gbvoOUT.getHeaderVO().setVbillcode(null);
    gbvoOUT.getHeaderVO().setCbilltypecode(BillTypeConst.m_otherOut);

    gbvoOUT.getHeaderVO().setStatus(2);
    gbvoOUT.getHeaderVO().setAttributeValue("bdispenseflag", "Y");

    GeneralBillItemVO[] outbodys = new GeneralBillItemVO[aloutitem.size()];

    aloutitem.toArray(outbodys);

    gbvoOUT.setChildrenVO(outbodys);

    BillRowNo.setVORowNoByRule(gbvoOUT, BillTypeConst.m_otherOut, "crowno");

    alOutGeneralVO.add(gbvoOUT);

    GeneralBillVO gbvoIn = new GeneralBillVO();
    gbvoIn.setParentVO(voBillclone.getParentVO());
    gbvoIn.getHeaderVO().setPrimaryKey(null);
    gbvoIn.getHeaderVO().setVbillcode(null);
    gbvoIn.getHeaderVO().setCbilltypecode(BillTypeConst.m_otherIn);

    gbvoIn.getHeaderVO().setStatus(2);
    gbvoIn.getHeaderVO().setAttributeValue("bdispenseflag", "Y");

    GeneralBillItemVO[] inbodys = new GeneralBillItemVO[alinitem.size()];
    alinitem.toArray(inbodys);

    gbvoIn.setChildrenVO(inbodys);

    BillRowNo.setVORowNoByRule(gbvoIn, BillTypeConst.m_otherIn, "crowno");

    alInGeneralVO.add(gbvoIn);

    getDispenseDlg(
      NCLangRes.getInstance().getStrByID("4008busi", 
      "UPP4008busi-000269"), alInGeneralVO, alOutGeneralVO)
      .showModal();

    if (!this.m_dlgInOut.isOK())
      return;
    try {
      filterNullLine();

      setDispenseFlag(
        (GeneralBillVO)this.m_alListData
        .get(this.m_iLastSelListHeadRow), 
        rownums);

      this.m_voBill = 
        ((GeneralBillVO)((GeneralBillVO)this.m_alListData
        .get(this.m_iLastSelListHeadRow)).clone());

      super.setButtonStatus(false);

      ctrlSourceBillButtons(true);

      String billCardPrimaryKey = getBillCardPanel()
        .getHeadItem("cgeneralhid").getValue().toString().trim();
      ArrayList alFreshRet = (ArrayList)GeneralBillHelper.queryInfo(
        new Integer(16), billCardPrimaryKey);

      if ((alFreshRet == null) || (alFreshRet.get(0) == null)) {
        SCMEnv.out("Err,ret");
      }

      if ((alFreshRet != null) && (alFreshRet.size() >= 2) && 
        (alFreshRet.get(1) != null)) {
        ArrayList alTs = (ArrayList)alFreshRet.get(1);
        freshTs(alTs);
      }
    } catch (Exception e) {
      handleException(e);
      MessageDialog.showErrorDlg(this, null, e.getMessage());
    }
  }

  public void onNewReplenishInvBill() {
    super.onAdd();

    GeneralBillUICtl.setSendBackBillState(this, true);
    this.m_voBill.getHeaderVO().setFreplenishflag(new UFBoolean(true));

    this.m_bFixBarcodeNegative = true;

    getBillCardPanel().getHeadItem("cproviderid").setEdit(true);
    getBillCardPanel().getHeadItem("cproviderid").setEnabled(true);
  }

  public void onNewReplenishInvBillByOrder() {
    IFromPoUI ui = null;
    try {
      ui = 
        (IFromPoUI)Class.forName("nc.ui.ic.ic201.FromPoUI")
        .newInstance();
    } catch (Exception e) {
      showErrorMessage(NCLangRes.getInstance().getStrByID("4008busi", 
        "UPP4008busi-000272"));

      return;
    }
    ui.onNewReplenishInvBillByOrder(this, this.m_sCorpID);
  }

  public boolean onSave() {
    if (isBrwLendBiztype()) {
      this.m_alLocatorDataBackup = this.m_alLocatorData;
      this.m_alLocatorData = null;

      this.m_alSerialDataBackup = this.m_alSerialData;
      this.m_alSerialData = null;
    }

    return super.onSave();
  }

  public void onSNAssign() {
    if ((3 != this.m_iMode) && (isBrwLendBiztype())) {
      showErrorMessage(NCLangRes.getInstance().getStrByID("4008busi", 
        "UPP4008busi-000273"));

      return;
    }
    if (isBrwLendBiztype()) {
      GeneralBillVO voMyBill = null;
      if ((this.m_alListData != null) && 
        (this.m_iLastSelListHeadRow >= 0) && 
        (this.m_alListData.size() > this.m_iLastSelListHeadRow) && 
        (this.m_alListData.get(this.m_iLastSelListHeadRow) != null)) {
        voMyBill = 
          (GeneralBillVO)this.m_alListData
          .get(this.m_iLastSelListHeadRow);

        String sBillPK = (String)voMyBill.getItemValue(0, 
          "cfirstbillhid");

        if ((sBillPK == null) || (sBillPK.trim().length() == 0)) {
          showErrorMessage(NCLangRes.getInstance().getStrByID(
            "4008busi", "UPP4008busi-000274"));

          return;
        }
        showErrorMessage(NCLangRes.getInstance().getStrByID("4008busi", 
          "UPP4008busi-000275"));
      }

      return;
    }

    super.onSNAssign();
  }

  public void onSpaceAssign() {
    if ((3 != this.m_iMode) && (isBrwLendBiztype())) {
      showErrorMessage(NCLangRes.getInstance().getStrByID("4008busi", 
        "UPP4008busi-000273"));

      return;
    }
    if (isBrwLendBiztype()) {
      GeneralBillVO voMyBill = null;
      if ((this.m_alListData != null) && 
        (this.m_iLastSelListHeadRow >= 0) && 
        (this.m_alListData.size() > this.m_iLastSelListHeadRow) && 
        (this.m_alListData.get(this.m_iLastSelListHeadRow) != null)) {
        voMyBill = 
          (GeneralBillVO)this.m_alListData
          .get(this.m_iLastSelListHeadRow);

        String sBillPK = (String)voMyBill.getItemValue(0, 
          "cfirstbillhid");

        if ((sBillPK == null) || (sBillPK.trim().length() == 0)) {
          showErrorMessage(NCLangRes.getInstance().getStrByID(
            "4008busi", "UPP4008busi-000274"));

          return;
        }
        showErrorMessage(NCLangRes.getInstance().getStrByID("4008busi", 
          "UPP4008busi-000275"));
      }

      return;
    }

    super.onSpaceAssign();
  }

  GeneralBillItemVO[] searchInvKit(GeneralBillItemVO[] cvos) {
    ArrayList alInvKit = null;
    GeneralBillItemVO[] resultvos = (GeneralBillItemVO[])null;
    if (cvos != null) {
      alInvKit = new ArrayList();
      for (int i = 0; i < cvos.length; i++) {
        if ((cvos[i].getIsSet() == null) || 
          (cvos[i].getIsSet().intValue() != 1))
          continue;
        alInvKit.add(cvos[i]);
      }
      if (alInvKit.size() > 0) {
        resultvos = new GeneralBillItemVO[alInvKit.size()];
        alInvKit.toArray(resultvos);
      }
      return resultvos;
    }
    return null;
  }

  GeneralBillItemVO searchInvKit(GeneralBillItemVO cvos) {
    if ((cvos != null) && (cvos.getIsSet() != null) && 
      (cvos.getIsSet().intValue() == 1)) {
      return cvos;
    }
    return null;
  }

  protected void selectBillOnListPanel(int iBillIndex)
  {
  }

  protected void setBillRefResultVO(String sBusiTypeID, AggregatedValueObject[] vos) throws Exception {
    nc.vo.ic.pub.GenMethod.setFlargessEdit(vos, false);

    super.setBillRefResultVO(sBusiTypeID, vos);
  }

  protected void setBtnStatusSign(boolean bUpdateButtons) {
    if ((3 != this.m_iMode) || (this.m_iLastSelListHeadRow < 0) || 
      (this.m_iBillQty <= 0)) {
      getButtonTree().getButton("签字").setEnabled(false);
      getButtonTree().getButton("取消签字").setEnabled(false);
      return;
    }
    int iSignFlag = isSigned();
    if (1 == iSignFlag) {
      getButtonTree().getButton("签字").setEnabled(false);
      getButtonTree().getButton("取消签字").setEnabled(true);

      getButtonTree().getButton("修改").setEnabled(false);
      getButtonTree().getButton("删除").setEnabled(false);
      getButtonTree().getButton("配套").setEnabled(false);
    } else if (iSignFlag == 0) {
      getButtonTree().getButton("签字").setEnabled(true);
      getButtonTree().getButton("取消签字").setEnabled(false);

      if (isCurrentTypeBill()) {
        getButtonTree().getButton("修改").setEnabled(true);
        getButtonTree().getButton("删除").setEnabled(true);
      } else {
        getButtonTree().getButton("修改").setEnabled(false);
        getButtonTree().getButton("删除").setEnabled(false);
      }

      if (5 == this.m_iCurPanel)
        getButtonTree().getButton("配套").setEnabled(true);
    }
    else
    {
      getButtonTree().getButton("签字").setEnabled(false);
      getButtonTree().getButton("取消签字").setEnabled(false);

      if (isCurrentTypeBill()) {
        getButtonTree().getButton("修改").setEnabled(true);
        getButtonTree().getButton("删除").setEnabled(true);
      } else {
        getButtonTree().getButton("修改").setEnabled(false);
        getButtonTree().getButton("删除").setEnabled(false);
      }
    }

    if (bUpdateButtons)
      updateButtons();
  }

  public void setBillVO(GeneralBillVO bvo, boolean bUpdateBotton, boolean bExeFormule)
  {
    String purcorp = (String)bvo.getHeaderValue("pk_purcorp");

    if (!purcorp.equals(this.m_sCorpID)) {
      BillItem it = getBillCardPanel().getHeadItem("cbizid");
      if (it != null) {
        RefFilter.filterPsnByDept(it, purcorp, null);
        ((UIRefPane)it.getComponent()).setPk_corp(purcorp);
      }

      BillItem it1 = getBillCardPanel().getHeadItem("cdptid");
      if (it1 != null) {
        ((UIRefPane)it1.getComponent()).getRefModel().setWherePart(
          null);
        ((UIRefPane)it1.getComponent()).setPk_corp(purcorp);
      }

      BillItem it2 = getBillCardPanel().getHeadItem("cproviderid");
      if (it2 != null) {
        ((UIRefPane)it2.getComponent()).getRefModel().setWherePart(
          null);
        ((UIRefPane)it2.getComponent()).setPk_corp(purcorp);
      }

    }

    if ((!purcorp.equals(this.m_sCorpID)) && (bvo != null)) {
      GeneralBillItemVO[] voa = bvo.getItemVOs();
      if (voa != null) {
        for (int i = 0; i < voa.length; i++) {
          if (voa[i] != null) {
            Integer isBatch = voa[i].getInv().getIsLotMgt();
            if (isBatch == null)
              continue;
            if (isBatch.intValue() == 1)
              continue;
            if (isBatch.intValue() == 0) {
              voa[i].setVbatchcode(null);
            }
          }
        }
      }
    }

    super.setBillVO(bvo, bUpdateBotton, bExeFormule);
  }

  protected void setButtonsStatus(int iBillMode) {
    long lTime = System.currentTimeMillis();

    if (getButtonTree().getButton("配套") != null) {
      if ((this.m_iCurPanel == 5) && (iBillMode == 3) && 
        (this.m_iBillQty > 0) && (isSigned() != 1))
        getButtonTree().getButton("配套").setEnabled(true);
      else {
        getButtonTree().getButton("配套").setEnabled(false);
      }

    }

    initButtonsData();

    lTime = System.currentTimeMillis();
    if (nc.ui.ic.pub.tools.GenMethod.isProductEnabled(getCorpPrimaryKey(), 
      "PO")) {
      getButtonTree().getButton("自制退库").setEnabled(true);

      getButtonTree().getButton("采购订单退库").setEnabled(true);

      if (this.m_iMode == 3)
        getButtonTree().getButton("扣吨计算").setEnabled(false);
      else
        getButtonTree().getButton("扣吨计算").setEnabled(true);
    }
    else {
      getButtonTree().getButton("自制退库").setEnabled(false);

      getButtonTree().getButton("采购订单退库").setEnabled(false);

      getButtonTree().getButton("扣吨计算").setEnabled(false);
    }

    lTime = System.currentTimeMillis();
    super.setButtons();
    SCMEnv.showTime(lTime, "setButtonsStatus(int)_6:");
  }

  void setDispenseFlag(GeneralBillVO gvo) {
    if ((gvo == null) || (gvo.getItemCount() == 0))
      return;
    ArrayList alBid = null;
    GeneralBillItemVO[] resultvos = gvo.getItemVOs();
    if (resultvos != null) {
      alBid = new ArrayList();
      for (int i = 0; i < resultvos.length; i++) {
        if ((resultvos[i].getIsSet() == null) || 
          (resultvos[i].getIsSet().intValue() != 1))
          continue;
        resultvos[i].setFbillrowflag(new Integer(3));

        alBid.add(resultvos[i].getPrimaryKey());
      }

      if (alBid.size() <= 0)
        return;
      try {
        GeneralHHelper.setDispense(alBid);
      } catch (Exception e) {
        SCMEnv.error(e);
      }
    }
  }

  void setDispenseFlag(GeneralBillVO gvo, int[] rownums) {
    if ((gvo == null) || (gvo.getItemCount() == 0))
      return;
    ArrayList alBid = null;
    GeneralBillItemVO[] resultvos = gvo.getItemVOs();
    if (resultvos != null) {
      alBid = new ArrayList();

      for (int i = 0; i < rownums.length; i++) {
        if (!isSetInv(gvo, rownums[i]))
          continue;
        resultvos[rownums[i]].setFbillrowflag(new Integer(3));

        alBid.add(resultvos[rownums[i]].getPrimaryKey());
      }
    }
  }

  public GeneralBillItemVO[] splitInvKit(GeneralBillItemVO itemvo, GeneralBillHeaderVO headervo, UFDouble nsetnum)
  {
    if (itemvo == null)
      return null;
    String sInvSetID = itemvo.getCinventoryid();

    if (sInvSetID != null) {
      ArrayList alInvvo = new ArrayList();
      try {
        alInvvo = GeneralBillHelper.queryPartbySetInfo(sInvSetID);
      } catch (Exception e2) {
        SCMEnv.error(e2);
      }
      if (alInvvo == null) {
        SCMEnv.out("该成套件没有配件，请检查数据库...");
        return null;
      }
      int rowcount = alInvvo.size();

      GeneralBillItemVO[] voParts = new GeneralBillItemVO[rowcount];
      UFDate db = new UFDate(this.m_sLogDate);
      for (int i = 0; i < rowcount; i++) {
        voParts[i] = new GeneralBillItemVO();
        voParts[i].setInv((InvVO)alInvvo.get(i));
        voParts[i].setDbizdate(db);
        UFDouble nchildnum = ((InvVO)alInvvo.get(i))
          .getAttributeValue("childsnum") == null ? 
          new UFDouble(
          0) : 
          new UFDouble(((InvVO)alInvvo.get(i))
          .getAttributeValue("childsnum").toString());

        UFDouble ntotalnum = null;
        if (nsetnum != null)
          ntotalnum = nchildnum.multiply(nsetnum);
        else
          ntotalnum = nchildnum;
        UFDouble hsl = ((InvVO)alInvvo.get(i)).getHsl() == null ? null : 
          new UFDouble(((InvVO)alInvvo.get(i)).getHsl()
          .toString());

        UFDouble ntotalastnum = null;
        if ((hsl != null) && (hsl.doubleValue() != 0.0D)) {
          ntotalastnum = ntotalnum.div(hsl);
        }

        voParts[i].setNinnum(ntotalnum);
        voParts[i].setNinassistnum(ntotalastnum);
        voParts[i].setNshouldinnum(ntotalnum);
        voParts[i].setNneedinassistnum(ntotalastnum);
        voParts[i].setDbizdate(new UFDate(this.m_sLogDate));
        voParts[i].setCsourceheadts(null);
        voParts[i].setCsourcebodyts(null);
        voParts[i].setCsourcetype(headervo.getCbilltypecode());
        voParts[i].setCsourcebillhid(headervo.getPrimaryKey());
        voParts[i].setCsourcebillbid(itemvo.getPrimaryKey());
        voParts[i].setVsourcebillcode(headervo.getVbillcode());
        voParts[i].setAttributeValue("creceieveid", 
          itemvo.getCreceieveid());

        voParts[i].setAttributeValue("cprojectid", 
          itemvo.getCprojectid());

        String s = "vuserdef";
        String ss = "pk_defdoc";
        for (int j = 0; j < 20; j++) {
          voParts[i]
            .setAttributeValue(
            s + String.valueOf(j + 1), 
            itemvo.getAttributeValue(s + 
            String.valueOf(j + 1)));

          voParts[i]
            .setAttributeValue(
            ss + String.valueOf(j + 1), 
            itemvo.getAttributeValue(ss + 
            String.valueOf(j + 1)));
        }

        voParts[i].setCgeneralhid(null);
        voParts[i].setCgeneralbid(null);
        voParts[i].setStatus(2);
      }
      return voParts;
    }
    return null;
  }

  public void voBillPastLineSetAttribe(int iRow, GeneralBillVO voBill) {
    Object oTemp = voBill.getItemVOs()[iRow].getAttributeValue("flargess");
    boolean bFlarg = false;
    if (oTemp != null) {
      UFBoolean ufbflargess = (UFBoolean)oTemp;
      bFlarg = ufbflargess.booleanValue();
    }
    if (!bFlarg)
      voBill.getItemVOs()[iRow].setAttributeValue("isflargessedit", "Y");
    else
      voBill.getItemVOs()[iRow].setAttributeValue("isflargessedit", "N");
  }

  protected void onAddLine() {
    super.onAddLine();
    int sel = getBillCardPanel().getBillTable().getSelectedRow();
    if (sel < 0) {
      return;
    }
    String pk_corp = getBillCardPanel().getHeadItem("pk_corp").getValue();

    String calid = getBillCardPanel().getHeadItem("pk_calbody").getValue();
    String whid = getBillCardPanel().getHeadItem("cwarehouseid").getValue();

    getBillCardPanel().setBodyValueAt(pk_corp, sel, "pk_reqcorp");
    getBillCardPanel().getBillModel().execEditFormulaByKey(sel, 
      "creqcorpname");

    getBillCardPanel().setBodyValueAt(calid, sel, "pk_reqstoorg");
    getBillCardPanel().getBillModel().execEditFormulaByKey(sel, 
      "creqstoorgname");

    getBillCardPanel().setBodyValueAt(whid, sel, "pk_creqwareid");
    getBillCardPanel().getBillModel().execEditFormulaByKey(sel, 
      "creqwarename");

    getBillCardPanel().setBodyValueAt(pk_corp, sel, "pk_invoicecorp");
    getBillCardPanel().getBillModel().execEditFormulaByKey(sel, 
      "cinvoicecorpname");
  }

  protected void onCopyLine() {
    super.onCopyLine();
  }

  protected void onInsertLine() {
    super.onInsertLine();
  }

  protected void onPasteLine() {
    super.onPasteLine();
  }

  protected void afterBillItemSelChg(int iRow, int iCol) {
  }

  public void afterCalbodyEdit(BillEditEvent e) {
    super.afterCalbodyEdit(e);
    setDefaultDataByHead();
  }

  public void afterWhEdit(BillEditEvent e) {
    super.afterWhEdit(e);
    setDefaultDataByHead();
  }

  private void setBodyData(int irow, String key, String pk, String keyName) {
    if ((getBillCardPanel().getBodyValueAt(irow, key) != null) && 
      (getBillCardPanel().getBodyValueAt(irow, key).toString()
      .trim().length() != 0)) {
      return;
    }

    getBillCardPanel().setBodyValueAt(pk, irow, key);
    getBillCardPanel().getBillModel().execEditFormulaByKey(irow, keyName);
  }

  public void setDefaultDataByHead() {
    int row = getBillCardPanel().getRowCount();
    if (row <= 0) {
      return;
    }

    String pk_corp = getBillCardPanel().getHeadItem("pk_corp").getValue();

    String calid = getBillCardPanel().getHeadItem("pk_calbody").getValue();
    String whid = getBillCardPanel().getHeadItem("cwarehouseid").getValue();

    for (int i = 0; i < row; i++) {
      Object obj = getBillCardPanel().getBodyValueAt(i, "csourcebillbid");
      if ((obj != null) && (obj.toString().trim().length() > 0)) {
        continue;
      }
      setBodyData(i, "pk_reqcorp", pk_corp, "creqcorpname");
      setBodyData(i, "pk_reqstoorg", calid, "creqstoorgname");
      setBodyData(i, "pk_creqwareid", whid, "creqwarename");
    }
  }

  public boolean beforeBillItemEdit(BillEditEvent e) {
    return false;
  }

  public boolean beforeEdit(BillItemEvent e) {
    boolean bret = super.beforeEdit(e);
    if (!bret) {
      return false;
    }
    String sItemKey = e.getItem().getKey();

    if (sItemKey.equals("cbizid")) {
      BillItem bi2 = getBillCardPanel().getHeadItem("cbizid");

      UIRefPane purcorp = (UIRefPane)getBillCardPanel().getHeadItem(
        "pk_purcorp").getComponent();

      String pkcorpValue = purcorp.getRefPK();
      if ((pkcorpValue != null) && (pkcorpValue.trim().length() != 0)) {
        RefFilter.filterPsnByDept(bi2, pkcorpValue, null);
      }
    }
    return true;
  }

  protected void beforeBillItemSelChg(int iRow, int iCol)
  {
  }

  public void setSpace(String cspaceid, String vspacename, int i)
  {
    getBillCardPanel().setBodyValueAt(cspaceid, i, "cspaceid");
    getBillCardPanel().setBodyValueAt(vspacename, i, "vspacename");
    LocatorVO voSpace = new LocatorVO();
    LocatorVO[] lvos = new LocatorVO[1];
    lvos[0] = voSpace;
    voSpace.setCspaceid(cspaceid);
    voSpace.setVspacename(vspacename);

    this.m_alLocatorData.remove(i);
    this.m_alLocatorData.add(i, lvos);
    UFDouble assistnum = null;
    try {
      assistnum = (UFDouble)getBillCardPanel().getBodyValueAt(i, 
        this.m_sAstItemKey);
    } catch (Exception localException) {
    }
    UFDouble num = null;
    try {
      num = (UFDouble)getBillCardPanel()
        .getBodyValueAt(i, this.m_sNumItemKey);
    } catch (Exception localException1) {
    }
    UFDouble ngrossnum = null;
    try {
      ngrossnum = (UFDouble)getBillCardPanel().getBodyValueAt(i, 
        this.m_sNgrossnum);
    }
    catch (Exception localException2) {
    }
    LocatorVO[] voLoc = (LocatorVO[])this.m_alLocatorData.get(i);

    if ((voLoc != null) && (voLoc.length == 1))
    {
      if (assistnum == null) {
        voLoc[0].setNinspaceassistnum(assistnum);
        voLoc[0].setNoutspaceassistnum(assistnum);
      }
      else if (this.m_iBillInOutFlag > 0) {
        voLoc[0].setNinspaceassistnum(assistnum);
        voLoc[0].setNoutspaceassistnum(null);
      } else {
        voLoc[0].setNinspaceassistnum(null);
        voLoc[0].setNoutspaceassistnum(assistnum);
      }

      if (num == null) {
        voLoc[0].setNinspacenum(num);
        voLoc[0].setNoutspacenum(num);
        if (this.m_alSerialData != null)
          this.m_alSerialData.set(i, null);
      }
      else if (this.m_iBillInOutFlag > 0)
      {
        voLoc[0].setNoutspacenum(null);
        voLoc[0].setNinspacenum(num);
      }
      else {
        voLoc[0].setNinspacenum(null);
        voLoc[0].setNoutspacenum(num);
        if (this.m_alSerialData != null) {
          this.m_alSerialData.set(i, null);
        }
      }

      if (ngrossnum == null) {
        voLoc[0].setNingrossnum(ngrossnum);
        voLoc[0].setNoutgrossnum(ngrossnum);
        if (this.m_alSerialData != null)
          this.m_alSerialData.set(i, null);
      }
      else if (this.m_iBillInOutFlag > 0)
      {
        voLoc[0].setNoutgrossnum(null);
        voLoc[0].setNingrossnum(ngrossnum);
      }
      else {
        voLoc[0].setNingrossnum(null);
        voLoc[0].setNoutgrossnum(ngrossnum);
      }

    }
    else
    {
      this.m_alLocatorData.set(i, null);
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

  public boolean Iscsflag(String primkey)
  {
    boolean rst = false;
    String SQL = "select csflag from bd_stordoc  where pk_stordoc ='" + primkey + "'";
    IUAPQueryBS sessionManager = (IUAPQueryBS)
      NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
    List list = null;
    try {
      list = (List)sessionManager.executeQuery(SQL, 
        new ArrayListProcessor());

      if (list.isEmpty()) {
        return rst;
      }

      Iterator iterator = list.iterator();
      while (iterator.hasNext()) {
        ArrayList values = new ArrayList();
        Object obj = iterator.next();
        if (obj == null) {
          continue;
        }
        if (obj.getClass().isArray()) {
          int len = Array.getLength(obj);
          for (int j = 0; j < len; j++) {
            values.add(Array.get(obj, j));
          }
          return rst = new UFBoolean(String.valueOf(values.get(0))).booleanValue();
        }

      }

    }
    catch (BusinessException e)
    {
      e.printStackTrace();
    }

    return rst;
  }
}