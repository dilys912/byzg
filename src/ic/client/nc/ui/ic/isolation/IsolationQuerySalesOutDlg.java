package nc.ui.ic.isolation;

import java.util.ArrayList;
import java.util.Vector;

import nc.ui.bd.languagetransformations.Transformations;
import nc.ui.ic.auditdlg.ClientUIInAndOut;
import nc.ui.ic.ic211.FormMemoDlg;
import nc.ui.ic.pub.bill.GeneralBillClientUI;
import nc.ui.ic.pub.bill.GeneralBillHelper;
import nc.ui.ic.pub.bill.ICBcurrArithUI;
import nc.ui.ic.pub.bill.query.QueryConditionDlgForBill;
import nc.ui.ic.pub.pf.ICBillQuery;
import nc.ui.ic.pub.pf.QryInBillDlg;
import nc.ui.ic.pub.tools.GenMethod;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.change.PfChangeBO_Client;
import nc.ui.scm.pub.billutil.ClientCacheHelper;
import nc.ui.scm.pub.report.BillRowNo;
import nc.ui.scm.recordtime.RecordTimeHelper;
import nc.vo.ic.pub.BillTypeConst;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.bill.QryConditionVO;
import nc.vo.ic.pub.bill.Timer;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.ic.bill.InvVO;
import nc.vo.scm.merge.DefaultVOMerger;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.ctrl.GenMsgCtrl;
import nc.vo.scm.pub.session.ClientLink;

public class IsolationQuerySalesOutDlg extends GeneralBillClientUI
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private FormMemoDlg ivjFormMemoDlg1 = null;
  private ClientUIInAndOut m_dlgInOut = null;
  private ArrayList m_alBrwLendBusitype = null;
  private final String m_sBusiTypeItemKey = "cbiztype";
  private final String m_sPNodeCode = "40080802";
  private boolean m_isQCstartup = false;
  private boolean m_isCheckQCstartup = false;
  private ICBcurrArithUI clsCurrArith;
  public Vector vcs=new Vector();
  public int length=0;
  public IsolationQuerySalesOutDlg()
  {
    initialize();
  }

  public IsolationQuerySalesOutDlg(String pk_corp, String billType, String businessType, String operator, String billID)
  {
    super(pk_corp, billType, businessType, operator, billID);
  }

  protected void afterBillEdit(BillEditEvent e)
  {
  }

  protected void afterBillItemSelChg(int iRow, int iCol)
  {
  }

  public void afterInvEdit(BillEditEvent e)
  {
    SCMEnv.out("inv chg");
    try
    {
      String sItemKey = e.getKey();
      int row = e.getRow();

      if (e.getValue().toString().trim().length() == 0) {
        clearRowData(row);

        if (this.m_voBill != null) {
          this.m_voBill.setItemValue(row, this.m_sShouldNumItemKey, null);
          this.m_voBill.setItemValue(row, this.m_sShouldAstItemKey, null);
        }
        if (getBillCardPanel().getBodyItem(this.m_sShouldNumItemKey) != null)
          getBillCardPanel().setBodyValueAt(null, row, this.m_sShouldNumItemKey);

        if (getBillCardPanel().getBodyItem(this.m_sShouldAstItemKey) != null) {
          getBillCardPanel().setBodyValueAt(null, row, this.m_sShouldAstItemKey);
        }

        setTailValue(null);
      }
      else {
        String sTempID1 = ((UIRefPane)getBillCardPanel().getBodyItem(sItemKey).getComponent()).getRefPK();

        if ((sTempID1 != null) && (sTempID1.trim().length() != 0)) {
          String sTempID2 = null;
          if (getBillCardPanel().getHeadItem(this.m_sMainWhItemKey) != null)
            sTempID2 = getBillCardPanel().getHeadItem(this.m_sMainWhItemKey).getValue();

          ArrayList alIDs = new ArrayList();
          alIDs.add(sTempID2);
          alIDs.add(sTempID1);
          alIDs.add(this.m_sUserID);
          alIDs.add(this.m_sCorpID);

          InvVO voInv = (InvVO)GeneralBillHelper.queryInfo(new Integer(0), alIDs);

          if (this.m_voBill != null)
          {
            this.m_voBill.setItemInv(row, voInv);
            this.m_voBill.setItemValue(row, "cgeneralhid", this.m_voBill.getHeaderValue("cgeneralhid"));
          }

          setBodyInvValue(row, voInv);

          clearRowData(0, row, sItemKey);

          execEditFomulas(0, "cinventorycode");
        }

      }

      if ((getSourBillTypeCode() == null) || (getSourBillTypeCode().trim().length() == 0))
      {
        if (this.m_voBill != null) {
          this.m_voBill.setItemValue(row, this.m_sShouldNumItemKey, null);
          this.m_voBill.setItemValue(row, this.m_sShouldAstItemKey, null);
        }
        if (getBillCardPanel().getBodyItem(this.m_sShouldNumItemKey) != null)
          getBillCardPanel().setBodyValueAt(null, row, this.m_sShouldNumItemKey);

        if (getBillCardPanel().getBodyItem(this.m_sShouldAstItemKey) != null) {
          getBillCardPanel().setBodyValueAt(null, row, this.m_sShouldAstItemKey);
        }

      }

      setBtnStatusSN(e.getRow(), true);
    } catch (Exception e2) {
      SCMEnv.error(e2);
    }
  }

  public void afterInvEditforBarCode(String sItemKey, int row)
  {
    SCMEnv.out("inv chg");
    try
    {
      String sTempID1 = ((UIRefPane)getBillCardPanel().getBodyItem(sItemKey).getComponent()).getRefPK();

      if ((sTempID1 != null) && (sTempID1.trim().length() != 0)) {
        String sTempID2 = null;
        if (getBillCardPanel().getHeadItem(this.m_sMainWhItemKey) != null)
          sTempID2 = getBillCardPanel().getHeadItem(this.m_sMainWhItemKey).getValue();

        ArrayList alIDs = new ArrayList();
        alIDs.add(sTempID2);
        alIDs.add(sTempID1);
        alIDs.add(this.m_sUserID);
        alIDs.add(this.m_sCorpID);

        InvVO voInv = (InvVO)GeneralBillHelper.queryInfo(new Integer(0), alIDs); if (this.m_voBill != null)
          this.m_voBill.setItemInv(row, voInv);

        setBodyInvValue(row, voInv);

        clearRowData(0, row, sItemKey);
      }

      if ((getSourBillTypeCode() == null) || (getSourBillTypeCode().trim().length() == 0))
      {
        if (this.m_voBill != null) {
          this.m_voBill.setItemValue(row, this.m_sShouldNumItemKey, null);
          this.m_voBill.setItemValue(row, this.m_sShouldAstItemKey, null);
        }
        if (getBillCardPanel().getBodyItem(this.m_sShouldNumItemKey) != null)
          getBillCardPanel().setBodyValueAt(null, row, this.m_sShouldNumItemKey);

        if (getBillCardPanel().getBodyItem(this.m_sShouldAstItemKey) != null)
          getBillCardPanel().setBodyValueAt(null, row, this.m_sShouldAstItemKey);

      }

    }
    catch (Exception e2)
    {
      SCMEnv.error(e2);
    }
  }

  public void afterInvEditforBarCode(BillEditEvent e)
  {
    SCMEnv.out("inv chg");
    try
    {
      String sItemKey = e.getKey();
      int row = e.getRow();

      if (e.getValue().toString().trim().length() == 0) {
        clearRowData(row);

        if (this.m_voBill != null) {
          this.m_voBill.setItemValue(row, this.m_sShouldNumItemKey, null);
          this.m_voBill.setItemValue(row, this.m_sShouldAstItemKey, null);
        }
        if (getBillCardPanel().getBodyItem(this.m_sShouldNumItemKey) != null)
          getBillCardPanel().setBodyValueAt(null, row, this.m_sShouldNumItemKey);

        if (getBillCardPanel().getBodyItem(this.m_sShouldAstItemKey) != null) {
          getBillCardPanel().setBodyValueAt(null, row, this.m_sShouldAstItemKey);
        }

        setTailValue(null);
      }
      else {
        String sTempID1 = ((UIRefPane)getBillCardPanel().getBodyItem(sItemKey).getComponent()).getRefPK();

        if ((sTempID1 != null) && (sTempID1.trim().length() != 0)) {
          String sTempID2 = null;
          if (getBillCardPanel().getHeadItem(this.m_sMainWhItemKey) != null)
            sTempID2 = getBillCardPanel().getHeadItem(this.m_sMainWhItemKey).getValue();

          ArrayList alIDs = new ArrayList();
          alIDs.add(sTempID2);
          alIDs.add(sTempID1);
          alIDs.add(this.m_sUserID);
          alIDs.add(this.m_sCorpID);

          InvVO voInv = (InvVO)GeneralBillHelper.queryInfo(new Integer(0), alIDs);
          if (this.m_voBill != null)
            this.m_voBill.setItemInv(row, voInv);

          setBodyInvValue(row, voInv);

          clearRowData(0, row, sItemKey);
        }

      }

      if ((getSourBillTypeCode() == null) || (getSourBillTypeCode().trim().length() == 0))
      {
        if (this.m_voBill != null) {
          this.m_voBill.setItemValue(row, this.m_sShouldNumItemKey, null);
          this.m_voBill.setItemValue(row, this.m_sShouldAstItemKey, null);
        }
        if (getBillCardPanel().getBodyItem(this.m_sShouldNumItemKey) != null)
          getBillCardPanel().setBodyValueAt(null, row, this.m_sShouldNumItemKey);

        if (getBillCardPanel().getBodyItem(this.m_sShouldAstItemKey) != null) {
          getBillCardPanel().setBodyValueAt(null, row, this.m_sShouldAstItemKey);
        }

      }

      setBtnStatusSN(e.getRow(), true);
    } catch (Exception e2) {
      SCMEnv.error(e2);
    }
  }

  public boolean beforeBillItemEdit(BillEditEvent e)
  {
    return true;
  }

  protected void beforeBillItemSelChg(int iRow, int iCol)
  {
  }

  public void bodyRowChange(BillEditEvent e)
  {
    super.bodyRowChange(e);
  }

  protected boolean checkVO(GeneralBillVO voBill)
  {
    return checkVO();
  }

  protected void execExtendFormula(ArrayList alListData)
  {
    if ((alListData == null) || (alListData.get(0) == null))
      return;
    int iLen = alListData.size();
    CircularlyAccessibleValueObject[] headVO = new CircularlyAccessibleValueObject[iLen];
    for (int i = 0; i < iLen; ++i) {
      headVO[i] = ((AggregatedValueObject)alListData.get(i)).getParentVO();
    }

    ClientCacheHelper.getColValue(headVO, new String[] { "pk_cubasdoctran" }, "dm_trancust", "pk_trancust", new String[] { "pkcusmandoc" }, "cwastewarehouseid");

    ClientCacheHelper.getColValue(headVO, new String[] { "vcustname" }, "bd_cubasdoc", "pk_cubasdoc", new String[] { "custname" }, "pk_cubasdoctran");
  }

  public GeneralBillVO getBillVO()
  {
    GeneralBillVO billVO = super.getBillVO();
    if (getBillCardPanel().getHeadItem("vdiliveraddress") != null) {
      billVO.setHeaderValue("vdiliveraddress", ((UIRefPane)getBillCardPanel().getHeadItem("vdiliveraddress").getComponent()).getText());
    }

    return billVO;
  }

  protected QueryConditionDlgForBill getConditionDlg()
  {
    if (this.ivjQueryConditionDlg == null) {
      this.ivjQueryConditionDlg = super.getConditionDlg();
      this.ivjQueryConditionDlg.setCombox("freplenishflag", new String[][] { { "1", NCLangRes.getInstance().getStrByID("4008busi", "UPP4008busi-000368") }, { "2", NCLangRes.getInstance().getStrByID("4008busi", "UPT40080602-000014") }, { "3", NCLangRes.getInstance().getStrByID("4008busi", "UPPSCMCommon-000217") } });

      this.ivjQueryConditionDlg.setCorpRefs("head.pk_corp", new String[] { "head.ccustomerid" });
    }

    return this.ivjQueryConditionDlg;
  }

  protected ClientUIInAndOut getDispenseDlg(String sTitle, ArrayList alInVO, ArrayList alOutVO)
  {
    if (this.m_dlgInOut == null)
      try
      {
        this.m_dlgInOut = new ClientUIInAndOut(this, sTitle);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }


    this.m_voBill = ((GeneralBillVO)((GeneralBillVO)this.m_alListData.get(this.m_iLastSelListHeadRow)).clone());

    this.m_dlgInOut.setVO(this.m_voBill, alInVO, alOutVO, this.m_sBillTypeCode, this.m_voBill.getPrimaryKey().trim(), this.m_sCorpID, this.m_sUserID);

    this.m_dlgInOut.setName("BillDlg");

    return this.m_dlgInOut;
  }

  private FormMemoDlg getFormMemoDlg1()
  {
    if (this.ivjFormMemoDlg1 == null)
      try {
        this.ivjFormMemoDlg1 = new FormMemoDlg(this);
        this.ivjFormMemoDlg1.setName("FormMemoDlg1");
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }

    return this.ivjFormMemoDlg1;
  }

  public String getTitle()
  {
    return super.getTitle();
  }

  public void initialize()
  {
    super.initialize();
    try
    {
      BillItem item = getBillCardPanel().getBodyItem("navlinvoicenum");
      if (item != null) {
        item.setShow(false);
        getBillCardPanel().getBodyPanel().hideTableCol("navlinvoicenum");
      }
    } catch (Exception e) {
      SCMEnv.error(e.getMessage());
    }
  }

  protected void initPanel()
  {
    super.setBillInOutFlag(-1);

    super.setNeedBillRef(true);

    this.m_sBillTypeCode = BillTypeConst.m_saleOut;

    this.m_sCurrentBillNode = "40080802";

    getButtonTree().getButton(Transformations.getLstrFromMuiStr("形成代管","TheFormationOfTheEscrow")).setEnabled(false);

    getButtonTree().getButton(Transformations.getLstrFromMuiStr("配套","Supporting")).setEnabled(true);
  }

  private void onPrintCert()
  {
    Object obj = null;
    try {
      if (!(this.m_isCheckQCstartup)) {
        this.m_isQCstartup = GenMethod.isProductEnabled(this.m_sCorpID, "QC");

        this.m_isCheckQCstartup = true;
      }
      if (!(this.m_isQCstartup)) {
        showHintMessage(NCLangRes.getInstance().getStrByID("4008other", "UPP4008other-000492"));

        return;
      }

      Class cl = Class.forName("nc.ui.qc.inter.CertService");
      obj = cl.newInstance();
    }
    catch (Exception e) {
      SCMEnv.error(e);
    }
    if (obj == null)
      return;
    ClientEnvironment ce = ClientEnvironment.getInstance();

    ClientLink client = new ClientLink(ce);
    GeneralBillVO voBill = null;
    if ((this.m_voBill != null) && (this.m_iCurPanel == 5))
    {
      voBill = (GeneralBillVO)this.m_voBill.clone();

   //   ((CertService)obj).printCert(this, voBill, client);
    }
    else if ((this.m_iLastSelListHeadRow != -1) && (null != this.m_alListData) && (this.m_alListData.size() != 0))
    {
      voBill = (GeneralBillVO)this.m_alListData.get(this.m_iLastSelListHeadRow);

    //  ((CertService)obj).printCert(this, voBill, client);
    }
  }

  protected boolean isBrwLendBiztype()
  {
    GeneralBillVO voMyBill;
    try
    {
      voMyBill = null;

      String sBusitypeid = null;
      if (this.m_iCurPanel == 4) {
        if ((this.m_alListData != null) && (this.m_iLastSelListHeadRow >= 0) && (this.m_alListData.size() > this.m_iLastSelListHeadRow) && (this.m_alListData.get(this.m_iLastSelListHeadRow) != null))
        {
          voMyBill = (GeneralBillVO)this.m_alListData.get(this.m_iLastSelListHeadRow);

          sBusitypeid = (String)voMyBill.getHeaderValue("cbiztype");
        }

      }
      else if ((getBillCardPanel().getHeadItem("cbiztype") != null) && (getBillCardPanel().getHeadItem("cbiztype").getComponent() != null))
      {
        UIRefPane ref = (UIRefPane)getBillCardPanel().getHeadItem("cbiztype").getComponent();

        sBusitypeid = ref.getRefPK();
      }

      if ((sBusitypeid != null) && (this.m_alBrwLendBusitype == null)) {
        ArrayList alParam = new ArrayList();
        alParam.add(this.m_sCorpID);
        this.m_alBrwLendBusitype = ((ArrayList)GeneralBillHelper.queryInfo(new Integer(17), alParam));

        if (this.m_alBrwLendBusitype == null)
          this.m_alBrwLendBusitype = new ArrayList();
      }

      if ((sBusitypeid != null) && (this.m_alBrwLendBusitype != null) && (this.m_alBrwLendBusitype.contains(sBusitypeid)))
      {
        return true; }
    } catch (Exception e) {
      SCMEnv.error(e);
    }
    return false;
  }

  public void onAdd()
  {
    super.onAdd();
    try
    {
      this.m_dTime = RecordTimeHelper.getTimeStamp();
    } catch (Exception e) {
      SCMEnv.error(e);
    }
  }

  public void onButtonClicked(ButtonObject bo)
  {
    showHintMessage(bo.getName());
    if (bo == getButtonTree().getButton(Transformations.getLstrFromMuiStr("形成代管","TheFormationOfTheEscrow")))
    {
      onFormMemo();
    } else if (bo == getButtonTree().getButton(Transformations.getLstrFromMuiStr("配套","Supporting"))) {
      onDispense();
    } else if (bo == getButtonTree().getButton(Transformations.getLstrFromMuiStr("汇总打印批次","SummaryPrintBatch"))) {
      onPrintLot();
    } else if (bo == getButtonTree().getButton(Transformations.getLstrFromMuiStr("打印质证书","PrintQualityCertificate"))) {
      onPrintCert();
    } else if (bo == getButtonTree().getButton(Transformations.getLstrFromMuiStr("参照入库单","RefStorage"))) {
      onRefInBill();
    }
    else {
      if ((((bo == getButtonTree().getButton("增加")) || (bo == getButtonTree().getButton("复制")) || (bo == getButtonTree().getButton("修改")))) && 
        (getBillCardPanel().getHeadItem("ccustomerid") != null) && (getBillCardPanel().getHeadItem("vdiliveraddress") != null))
      {
        String sRefPK = ((UIRefPane)getBillCardPanel().getHeadItem("ccustomerid").getComponent()).getRefPK();

        if (sRefPK != null) {
          ((UIRefPane)getBillCardPanel().getHeadItem("vdiliveraddress").getComponent()).setWhereString("pk_cubasdoc = (select pk_cubasdoc from bd_cumandoc where pk_cumandoc = '" + sRefPK + "')");
        }

      }

      super.onButtonClicked(bo);
    }
  }

  private void onDispense()
  {
    if ((3 == this.m_iMode) && (isSigned() != 1))
    {
     // break label20:
    	  }
   // return;

    if (getBillCardPanel().getBillTable().getSelectedRows().length >= 1)
    {
      if (2 == MessageDialog.showOkCancelDlg(this, null, NCLangRes.getInstance().getStrByID("4008busi", "UPP4008busi-000268")))
      {
        label20: return;
      }

      GeneralBillVO voBill = (GeneralBillVO)((GeneralBillVO)this.m_alListData.get(this.m_iLastSelListHeadRow)).clone();

      GeneralBillVO voBillclone = (GeneralBillVO)voBill.clone();

      ArrayList alOutGeneralVO = new ArrayList();
      ArrayList alInGeneralVO = new ArrayList();

      ArrayList aloutitem = new ArrayList();
      ArrayList alinitem = new ArrayList();
      int[] rownums = getBillCardPanel().getBillTable().getSelectedRows();

      for (int i = 0; i < rownums.length; ++i)
      {
        if (isSetInv(voBill, rownums[i])) { int j;
          if (isDispensedBill(voBill, rownums[i])) {
          //  break label460:
          }

          GeneralBillItemVO voParts = voBill.getItemVOs()[rownums[i]];
          UFDouble ufSetNum = null;

          ufSetNum = voParts.getNoutnum();
          voParts.setAttributeValue("nshouldinnum", voParts.getNoutnum());
          voParts.setAttributeValue("nneedinassistnum", voParts.getNoutassistnum());

          voParts.setAttributeValue("ninnum", voParts.getNoutnum());
          voParts.setAttributeValue("ninassistnum", voParts.getNoutassistnum());

          voParts.setAttributeValue("noutnum", null);
          voParts.setAttributeValue("noutassistnum", null);
          voParts.setAttributeValue("nshouldoutnum", null);
          voParts.setAttributeValue("nshouldoutassistnum", null);

          voParts.setAttributeValue("csourcetype", voBill.getHeaderVO().getCbilltypecode());

          voParts.setAttributeValue("csourcebillhid", voBill.getHeaderVO().getPrimaryKey());

          voParts.setAttributeValue("csourcebillbid", voBill.getItemVOs()[rownums[i]].getPrimaryKey());

          voParts.setAttributeValue("vsourcebillcode", voBill.getHeaderVO().getVbillcode());

          voParts.setCgeneralbid(null);
          voParts.setCgeneralbb3(null);
          voParts.setCsourceheadts(null);
          voParts.setCsourcebodyts(null);
          voParts.setDbizdate(new UFDate(this.m_sLogDate));

          alinitem.add(voParts);

          voParts.setLocator(null);
          GeneralBillItemVO[] tempItemVO = splitInvKit(voParts, voBillclone.getHeaderVO(), ufSetNum);

          if ((tempItemVO != null) && (tempItemVO.length > 0)) {
            for (j = 0; j < tempItemVO.length; ++j)
              aloutitem.add(tempItemVO[j]);

          }
          else
          {
            showErrorMessage(NCLangRes.getInstance().getStrByID("4008busi", "UPP4008busi-000270"));

            return;
          }
        }
      }
      if ((aloutitem.size() == 0) || (alinitem.size() == 0))
      {
        label460: return;
      }
      GeneralBillVO gbvoIn = new GeneralBillVO();
      voBill.getHeaderVO().setCoperatorid(this.m_sUserID);
      voBill.getHeaderVO().setDbilldate(new UFDate(this.m_sLogDate));

      gbvoIn.setParentVO(voBill.getParentVO());
      gbvoIn.getHeaderVO().setPrimaryKey(null);
      gbvoIn.getHeaderVO().setVbillcode(null);
      gbvoIn.getHeaderVO().setCbilltypecode(BillTypeConst.m_otherIn);

      gbvoIn.getHeaderVO().setStatus(2);
      gbvoIn.getHeaderVO().setAttributeValue("bdispenseflag", "Y");

      GeneralBillItemVO[] inbodys = new GeneralBillItemVO[alinitem.size()];
      alinitem.toArray(inbodys);
      gbvoIn.setChildrenVO(inbodys);
      alInGeneralVO.add(gbvoIn);

      GeneralBillVO gbvoOut = new GeneralBillVO();
      gbvoOut.setParentVO(voBillclone.getParentVO());
      gbvoOut.getHeaderVO().setPrimaryKey(null);
      gbvoOut.getHeaderVO().setVbillcode(null);
      gbvoOut.getHeaderVO().setCbilltypecode(BillTypeConst.m_otherOut);

      gbvoOut.getHeaderVO().setStatus(2);
      gbvoOut.getHeaderVO().setAttributeValue("bdispenseflag", "Y");

      GeneralBillItemVO[] outbodys = new GeneralBillItemVO[aloutitem.size()];
      aloutitem.toArray(outbodys);

      gbvoOut.setChildrenVO(outbodys);

      BillRowNo.setVORowNoByRule(gbvoOut, BillTypeConst.m_otherOut, "crowno");

      alOutGeneralVO.add(gbvoOut);

      getDispenseDlg(NCLangRes.getInstance().getStrByID("4008busi", "UPP4008busi-000269"), alInGeneralVO, alOutGeneralVO).showModal();

      if (this.m_dlgInOut.isOK())
        try
        {
          filterNullLine();

          setDispenseFlag((GeneralBillVO)this.m_alListData.get(this.m_iLastSelListHeadRow), rownums);

          this.m_voBill = ((GeneralBillVO)((GeneralBillVO)this.m_alListData.get(this.m_iLastSelListHeadRow)).clone());

          super.setButtonStatus(false);

          ctrlSourceBillButtons(true);
        }
        catch (Exception e) {
          handleException(e);
          MessageDialog.showErrorDlg(this, null, e.getMessage());
        }
    }
  }
  

  protected void onRefInBill()
  {
    QryInBillDlg dlgBill;
    try
    {
      dlgBill = new QryInBillDlg("cgeneralhid", this.m_sCorpID, this.m_sUserID, "40089907", "1=1", "45", null, null, "4C", this);

      if (dlgBill == null)
        return;

      ICBillQuery dlgQry = new ICBillQuery(this);

      dlgQry.setTempletID(this.m_sCorpID, "40080608", this.m_sUserID, null, "40089907");
      dlgQry.initData(this.m_sCorpID, this.m_sUserID, "40089907", null, "4C", "45", null);

      if (dlgQry.showModal() == 1)
      {
        ConditionVO[] voCons = dlgQry.getConditionVO();

        StringBuffer sWhere = new StringBuffer(" 1=1 ");
        if ((voCons != null) && (voCons.length > 0) && (voCons[0] != null)) {
          sWhere.append(" and " + dlgQry.getWhereSQL(voCons));
        }

        dlgBill.initVar("cgeneralhid", this.m_sCorpID, this.m_sUserID, null, sWhere.toString(), "45", null, null, "4C", null, this);

        dlgBill.setStrWhere(sWhere.toString());
        dlgBill.getBillVO();
        dlgBill.loadHeadData();
        dlgBill.addBillUI();
        dlgBill.setQueyDlg(dlgQry);

        GenMsgCtrl.printHint("will load qrybilldlg");
        if (dlgBill.showModal() == 1) {
          GenMsgCtrl.printHint("qrybilldlg closeok");

          AggregatedValueObject[] vos = dlgBill.getRetVos();
          GenMsgCtrl.printHint("qrybilldlg getRetVos");

          if (vos == null) {
            GenMsgCtrl.printHint("qrybilldlg getRetVos null");

            return;
          }

          GenMsgCtrl.printHint("qrybilldlg getRetVos is not null");

          AggregatedValueObject[] voRetvos = (AggregatedValueObject[])PfChangeBO_Client.pfChangeBillToBillArray(vos, "45", "4C");

          GenMsgCtrl.printHint("qrybilldlg getRetVos pfChangeBillToBillArray ok");

          String cbiztype = null;
          if ((voRetvos != null) && (voRetvos.length > 0))
            cbiztype = (String)voRetvos[0].getParentVO().getAttributeValue("cbiztype");
          setBillRefResultVO(cbiztype, voRetvos);
          if ((this.m_voBill.getItemVOs().length > 0) && (this.m_voBill.getItemVOs()[0] != null) && (this.m_voBill.getItemVOs()[0].getNoutnum() != null))
          {
            this.m_alSerialData = this.m_voBill.getSNs();
            this.m_alLocatorData = this.m_voBill.getLocators();
          }

          GenMsgCtrl.printHint("qrybilldlg getRetVos pfChangeBillToBillArray ok setBillRefResultVO ok");
        }
      }

    }
    catch (Exception e)
    {
      showErrorMessage(e.getMessage());
    }
  }

  private void onFormMemo()
  {
    if ((this.m_iLastSelListHeadRow >= 0) && (this.m_alListData != null) && (this.m_alListData.size() > this.m_iLastSelListHeadRow) && (this.m_alListData.get(this.m_iLastSelListHeadRow) != null))
    {
      if (((GeneralBillVO)this.m_alListData.get(this.m_iLastSelListHeadRow)).getChildrenVO().length == 0)
      {
        return;
      }

      GeneralBillVO voBill = (GeneralBillVO)((GeneralBillVO)this.m_alListData.get(this.m_iLastSelListHeadRow)).clone();

      voBill.setHeaderValue("coperatorid", this.m_sUserID);
  //    getFormMemoDlg1().setBillVO(voBill);
      getFormMemoDlg1().showModal();
    }
  }

  public void onPrintLot()
  {
    showHintMessage(NCLangRes.getInstance().getStrByID("4008busi", "UPP4008busi-000248"));

    SCMEnv.out("打印批次汇总开始!Print batch summary start!\n");
    try
    {
      if ((this.m_iMode == 3) && (this.m_iCurPanel == 5)) {
        UFDouble udNum;
        UFDouble udMny;
        int k;
        SCMEnv.out("打印批次汇总开始!表单打印!Print batch summary begin! Form Print!\n");

        GeneralBillVO vo = null;

        if ((this.m_iLastSelListHeadRow != -1) && (null != this.m_alListData) && (this.m_alListData.size() != 0))
        {
          vo = (GeneralBillVO)this.m_alListData.get(this.m_iLastSelListHeadRow);

          if (getBillCardPanel().getHeadItem("vcustname") != null)
            vo.setHeaderValue("vcustname", getBillCardPanel().getHeadItem("vcustname").getValue());

        }

        if (null == vo)
          vo = new GeneralBillVO();

        if (null == vo.getParentVO())
          vo.setParentVO(new GeneralBillHeaderVO());

        if ((null == vo.getChildrenVO()) || (vo.getChildrenVO().length == 0) || (vo.getChildrenVO()[0] == null))
        {
          GeneralBillItemVO[] ivo = new GeneralBillItemVO[1];
          ivo[0] = new GeneralBillItemVO();
          vo.setChildrenVO(ivo);
        }

        if (getPrintEntry().selectTemplate() < 0)
          return;
        GeneralBillVO gvobak = (GeneralBillVO)vo.clone();

        DefaultVOMerger dvomerger = new DefaultVOMerger();
        dvomerger.setGroupingAttr(new String[] { "cinventoryid", "castunitid" });

        dvomerger.setSummingAttr(new String[] { "nshouldoutnum", "nshouldoutassistnum", "noutnum", "noutassistnum", "nmny" });

        GeneralBillItemVO[] itemvosnew = (GeneralBillItemVO[])(GeneralBillItemVO[])dvomerger.mergeByGroup(gvobak.getItemVOs());

        if (itemvosnew != null) {
          udNum = null;
          udMny = null;
          for (k = 0; k < itemvosnew.length; ++k) {
            udNum = itemvosnew[k].getNoutnum();
            udMny = itemvosnew[k].getNmny();
            if ((udNum != null) && (udMny != null))
              itemvosnew[k].setNprice(udMny.div(udNum));

            SCMEnv.out("cinventoryid:" + itemvosnew[k].getCinventoryid() + "\n");

            SCMEnv.out("castunitid:" + itemvosnew[k].getCastunitid() + "\n");

            SCMEnv.out("Vbatchcode:" + itemvosnew[k].getVbatchcode() + "\n");

            SCMEnv.out("noutnum:" + udNum + "\n");
          }

        }

        gvobak.setChildrenVO(itemvosnew);

        getDataSource().setVO(gvobak);

        getPrintEntry().setDataSource(getDataSource());
        SCMEnv.out("打印批次汇总开始!表单打印结束!The end of the print batch summary! Form print!\n");
        getPrintEntry().preview();
      }
      else if (this.m_iCurPanel == 4)
      {
        SCMEnv.out("列表打印开始!The beginning of the list Print!\n");
        if ((null == this.m_alListData) || (this.m_alListData.size() == 0))
          return;

        if (getPrintEntry().selectTemplate() < 0)
          return;
        ArrayList alBill = getSelectedBills();

        setScaleOfListData(alBill);
        SCMEnv.out("列表打印:得到选中的单据并设置数量精度!The list Print: selected documents and set the number of precision!\n");
        if (alBill == null)
          return;
        DefaultVOMerger dvomerger = null;
        for (int i = 0; i < alBill.size(); ++i) {
          SCMEnv.out("列表打印:开始合并表体行!The list Print: began to merge table body line!\n");
          GeneralBillVO gvobak = (GeneralBillVO)alBill.get(i);

          dvomerger = new DefaultVOMerger();
          dvomerger.setGroupingAttr(new String[] { "cinventoryid", "castunitid" });

          dvomerger.setSummingAttr(new String[] { "nshouldoutnum", "nshouldoutassistnum", "noutnum", "noutassistnum", "nmny" });

          GeneralBillItemVO[] itemvosnew = (GeneralBillItemVO[])(GeneralBillItemVO[])dvomerger.mergeByGroup(gvobak.getItemVOs());

          SCMEnv.out("列表打印:得到合并后的表体行!The list Print: after the merger of the table body row!\n");
          if (itemvosnew != null) {
            UFDouble udNum = null;
            UFDouble udMny = null;
            for (int k = 0; k < itemvosnew.length; ++k) {
              udNum = itemvosnew[k].getNoutnum();
              udMny = itemvosnew[k].getNmny();
              if ((udNum != null) && (udMny != null))
                itemvosnew[k].setNprice(udMny.div(udNum));

              SCMEnv.out("cinventoryid:" + itemvosnew[k].getCinventoryid() + "\n");

              SCMEnv.out("castunitid:" + itemvosnew[k].getCastunitid() + "\n");

              SCMEnv.out("Vbatchcode:" + itemvosnew[k].getVbatchcode() + "\n");

              SCMEnv.out("noutnum:" + udNum + "\n");
            }

            gvobak.setChildrenVO(itemvosnew);
            alBill.set(i, gvobak);
          }

        }

        SCMEnv.out("列表打印:得到合并后的单据!The list Print: been merged documents!\n");
        getDataSource().setListVOs(alBill);
        getDataSource().setTotalLinesInOnePage(getPrintEntry().getBreakPos());

        getPrintEntry().setDataSource(getDataSource());
        getPrintEntry().preview();
      }
      else {
        showHintMessage(NCLangRes.getInstance().getStrByID("4008busi", "UPP4008busi-000249"));
      }
    } catch (Exception e) {
      SCMEnv.error(e);
      showHintMessage(NCLangRes.getInstance().getStrByID("4008busi", "UPPSCMCommon-000061") + e.getMessage());
    }
  }

  public void onQuery()
  {
    Timer timer;
   
      timer = new Timer();
      this.m_sBnoutnumnull = null;

      timer.start(NCLangRes.getInstance().getStrByID("4008busi", "UPP4008busi-000277"));

      int cardOrList = this.m_iCurPanel;

      if ((this.m_bQuery) || (!(this.m_bEverQry))) {
        getConditionDlg().showModal();
        timer.showExecuteTime("@@getConditionDlg().showModal()：");

        if (getConditionDlg().getResult() != 1)
        {
          return ;
        }

        this.m_bEverQry = true;

        setButtonStatus(true);
      }

      QryConditionVO voCond = getQryConditionVO();

      this.m_voLastQryCond = voCond;

      ConditionVO[] voaCond = getConditionDlg().getConditionVO();

      voCond.setParam(1, voaCond);

      voCond.setIntParam(0, 300);
      if (this.m_sBnoutnumnull != null)
      {
        voCond.setParam(33, this.m_sBnoutnumnull);
      }

      showHintMessage(NCLangRes.getInstance().getStrByID("4008busi", "UPP4008busi-000250"));

      timer.showExecuteTime("Before 查询：：");
      ArrayList alListData = null;
	try {
		alListData = GeneralBillHelper.queryBills(this.m_sBillTypeCode, voCond);
	} catch (Exception e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
      timer.showExecuteTime("查询时间：");
      try
      {
        setAlistDataByFormula(20, alListData);

        timer.showExecuteTime("@@setAlistDataByFormula公式解析时间：");
        SCMEnv.out("0存货公式解析成功！");
      }
      catch (Exception e) {
      }
      execExtendFormula(alListData);
      if ((alListData != null) && (alListData.size() > 0)) {
        this.m_alListData = alListData;
        //弹出展示界面
        ShowSalesOutResult write=new ShowSalesOutResult(this, alListData);
        String result[]=null;
        if (write.showModal()==2) {
			//newaccount=wriui.getUITextField1().getText();//得到新账号
        	result=write.result;
		}
        if(result==null || result.length<1 ){
        	return;
        }
        length=result.length;
        vcs.clear();
        try
        {
        	GeneralBillItemVO item = null;
        	GeneralBillHeaderVO header = null;
	        for (int a = 0; a < result.length; a++) {
				for (int i = 0; i < alListData.size(); i++) { 
					GeneralBillVO vo=(GeneralBillVO) alListData.get(i);
					header = vo.getHeaderVO();
					for (int j = 0; j < vo.getChildrenVO().length; j++) {
						Vector vc=new Vector();
						item = (GeneralBillItemVO) vo.getChildrenVO()[j];
	//					if(result[a].equals(vo.getChildrenVO()[j].getAttributeValue("vbatchcode").toString())){
						if(result[a].equals(item.getPrimaryKey())){//根据主键来识别选中的VO
							vc.addElement(item.getCinventorycode());//料号
							vc.addElement(item.getVbatchcode());//批次号
							vc.addElement(item.getInvname());//产品
						//	vc.addElement(vo.getChildrenVO()[j].getAttributeValue("pk_batchcode").toString());//货位
							vc.addElement(header.getCwarehouseid());//仓库
							vc.addElement(item.getCinventoryid());//产品id
							if(item.getNoutnum()!=null)
							{   
								vc.add(item.getNoutnum().intValue()); //数量
							}else
							{
								vc.add(null);
							}
							vc.add(item.getVfree0()); //垛号
							if(item.getLocator()!=null&&item.getLocator().length>0)
							{
								vc.add(item.getLocator()[0].getVspacecode());  //货位编码
							}else
							{
								vc.add(null);
							}
							vcs.add(vc);
						}
					}
					
				}
	        }
        }catch(Exception ex)
        { 
        	MessageDialog.showErrorDlg(this, "错误", "错误："+ex.getMessage());
        	ex.printStackTrace();
        	vcs.clear();
        }

      
    }
  }
    

  public boolean onSave()
  {
    if (isBrwLendBiztype())
    {
      this.m_alLocatorDataBackup = this.m_alLocatorData;
      this.m_alLocatorData = null;

      this.m_alSerialDataBackup = this.m_alSerialData;
      this.m_alSerialData = null;
    }
    return super.onSave();
  }

  public void onSNAssign()
  {
    if ((3 != this.m_iMode) && (isBrwLendBiztype())) {
      showErrorMessage(NCLangRes.getInstance().getStrByID("4008busi", "UPP4008busi-000273"));

      return;
    }
    if (isBrwLendBiztype()) {
      GeneralBillVO voMyBill = null;
      if ((this.m_alListData != null) && (this.m_iLastSelListHeadRow >= 0) && (this.m_alListData.size() > this.m_iLastSelListHeadRow) && (this.m_alListData.get(this.m_iLastSelListHeadRow) != null))
      {
        voMyBill = (GeneralBillVO)this.m_alListData.get(this.m_iLastSelListHeadRow);

        String sBillPK = (String)voMyBill.getItemValue(0, "cfirstbillhid");

        if ((sBillPK == null) || (sBillPK.trim().length() == 0)) {
          showErrorMessage(NCLangRes.getInstance().getStrByID("4008busi", "UPP4008busi-000274"));

          return;
        }
        showErrorMessage(NCLangRes.getInstance().getStrByID("4008busi", "UPP4008busi-000275"));
      }

      return;
    }

    super.onSNAssign();
  }

  public void onSpaceAssign()
  {
    if ((3 != this.m_iMode) && (isBrwLendBiztype())) {
      showErrorMessage(NCLangRes.getInstance().getStrByID("4008busi", "UPP4008busi-000273"));

      return;
    }
    if (isBrwLendBiztype()) {
      GeneralBillVO voMyBill = null;
      if ((this.m_alListData != null) && (this.m_iLastSelListHeadRow >= 0) && (this.m_alListData.size() > this.m_iLastSelListHeadRow) && (this.m_alListData.get(this.m_iLastSelListHeadRow) != null))
      {
        voMyBill = (GeneralBillVO)this.m_alListData.get(this.m_iLastSelListHeadRow);

        String sBillPK = (String)voMyBill.getItemValue(0, "cfirstbillhid");

        if ((sBillPK == null) || (sBillPK.trim().length() == 0)) {
          showErrorMessage(NCLangRes.getInstance().getStrByID("4008busi", "UPP4008busi-000274"));

          return;
        }
        showErrorMessage(NCLangRes.getInstance().getStrByID("4008busi", "UPP4008busi-000275"));
      }

      return;
    }

    super.onSpaceAssign();
  }

  GeneralBillItemVO searchInvKit(GeneralBillItemVO cvos)
  {
    ArrayList alInvKit = null;

    if ((cvos != null) && 
      (cvos.getIsSet() != null) && (cvos.getIsSet().intValue() == 1))
      return cvos;

    return null;
  }

  protected void selectBillOnListPanel(int iBillIndex)
  {
  }

  protected void setBtnStatusSign()
  {
    setBtnStatusSign(true);
  }

  protected void setBtnStatusSign(boolean bUpdateButtons)
  {
    if ((3 != this.m_iMode) || (this.m_iLastSelListHeadRow < 0) || (this.m_iBillQty <= 0))
    {
      getButtonTree().getButton("签字").setEnabled(false);
      getButtonTree().getButton("取消签字").setEnabled(false);
      return;
    }
    int iSignFlag = isSigned();
    if (1 == iSignFlag)
    {
      getButtonTree().getButton("签字").setEnabled(false);
      getButtonTree().getButton("取消签字").setEnabled(true);

      getButtonTree().getButton("修改").setEnabled(false);
      getButtonTree().getButton("删除").setEnabled(false);
      getButtonTree().getButton(Transformations.getLstrFromMuiStr("配套","Supporting")).setEnabled(false);
    }
    else if (0 == iSignFlag)
    {
      getButtonTree().getButton("签字").setEnabled(true);
      getButtonTree().getButton("取消签字").setEnabled(false);

      if (isCurrentTypeBill())
      {
        getButtonTree().getButton("修改").setEnabled(true);
        getButtonTree().getButton("删除").setEnabled(true);
      }
      else {
        getButtonTree().getButton("修改").setEnabled(false);
        getButtonTree().getButton("删除").setEnabled(false);
      }

      if (5 == this.m_iCurPanel) {
        getButtonTree().getButton(Transformations.getLstrFromMuiStr("配套","Supporting")).setEnabled(true);
      }

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

  protected void setButtonsStatus(int iBillMode)
  {
    if (getButtonTree().getButton(Transformations.getLstrFromMuiStr("形成代管","TheFormationOfTheEscrow")) != null)
      if ((iBillMode == 3) && (this.m_iBillQty > 0) && (isSigned() == 1))
      {
        getButtonTree().getButton(Transformations.getLstrFromMuiStr("形成代管","TheFormationOfTheEscrow")).setEnabled(true);
      }
      else getButtonTree().getButton(Transformations.getLstrFromMuiStr("形成代管","TheFormationOfTheEscrow")).setEnabled(false);


    if (getButtonTree().getButton(Transformations.getLstrFromMuiStr("配套","Supporting")) != null) {
      if ((this.m_iCurPanel == 5) && (iBillMode == 3) && (this.m_iBillQty > 0) && (isSigned() != 1))
      {
        getButtonTree().getButton(Transformations.getLstrFromMuiStr("配套","Supporting")).setEnabled(true);
      }
      else
      {
        getButtonTree().getButton(Transformations.getLstrFromMuiStr("配套","Supporting")).setEnabled(false);
      }

    }

    if (getButtonTree().getButton(Transformations.getLstrFromMuiStr("参照入库单","RefStorage")) != null)
      if (iBillMode == 3)
        getButtonTree().getButton(Transformations.getLstrFromMuiStr("参照入库单","RefStorage")).setEnabled(true);
      else
        getButtonTree().getButton(Transformations.getLstrFromMuiStr("参照入库单","RefStorage")).setEnabled(false);
  }

  void setDispenseFlag(GeneralBillVO gvo, int[] rownums)
  {
    int i;
    if ((gvo == null) || (gvo.getItemCount() == 0))
      return;
    ArrayList alBid = null;
    GeneralBillItemVO[] resultvos = gvo.getItemVOs();
    if (resultvos != null) {
      alBid = new ArrayList();

      for (i = 0; i < rownums.length; ++i) {
        if (!(isSetInv(gvo, rownums[i])))
     //     break label91:
        resultvos[rownums[i]].setFbillrowflag(new Integer(3));

        alBid.add(resultvos[rownums[i]].getPrimaryKey());
      }

    }

    //label91:
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
      }
      catch (Exception e2) {
        SCMEnv.error(e2);
      }

      if (alInvvo == null) {
        SCMEnv.out("该成套件没有配件，请检查数据库...The kit accessories, please check the database...");
        return null;
      }
      int rowcount = alInvvo.size();

      GeneralBillItemVO[] voParts = new GeneralBillItemVO[rowcount];
      UFDate db = new UFDate(this.m_sLogDate);
      for (int i = 0; i < rowcount; ++i) {
        voParts[i] = new GeneralBillItemVO();
        voParts[i].setInv((InvVO)alInvvo.get(i));
        voParts[i].setDbizdate(db);

        UFDouble nchildnum = new UFDouble(((InvVO)alInvvo.get(i)).getAttributeValue("childsnum").toString());

        UFDouble ntotalnum = null;
        if (nsetnum != null)
          ntotalnum = nchildnum.multiply(nsetnum);
        else
          ntotalnum = nchildnum;
        UFDouble hsl = new UFDouble(((InvVO)alInvvo.get(i)).getAttributeValue("hsl").toString());

        UFDouble ntotalastnum = null;
        if ((hsl != null) && (hsl.doubleValue() != 0.0D)) {
          ntotalastnum = ntotalnum.div(hsl);
        }

        voParts[i].setAttributeValue("nshouldoutnum", ntotalnum);
        voParts[i].setAttributeValue("nshouldoutassistnum", ntotalastnum);

        voParts[i].setAttributeValue("noutnum", ntotalnum);
        voParts[i].setAttributeValue("noutassistnum", ntotalastnum);
        voParts[i].setCsourceheadts(null);
        voParts[i].setCsourcebodyts(null);

        voParts[i].setAttributeValue("csourcetype", headervo.getCbilltypecode());

        voParts[i].setAttributeValue("csourcebillhid", headervo.getPrimaryKey());

        voParts[i].setAttributeValue("csourcebillbid", itemvo.getPrimaryKey());

        voParts[i].setAttributeValue("vsourcebillcode", headervo.getVbillcode());

        voParts[i].setAttributeValue("creceieveid", itemvo.getCreceieveid());
        voParts[i].setAttributeValue("cprojectid", itemvo.getCprojectid());
        String s = "vuserdef";
        String ss = "pk_defdoc";
        for (int j = 0; j < 20; ++j)
        {
          voParts[i].setAttributeValue(s + String.valueOf(j + 1), itemvo.getAttributeValue(s + String.valueOf(j + 1)));

          voParts[i].setAttributeValue(ss + String.valueOf(j + 1), itemvo.getAttributeValue(ss + String.valueOf(j + 1)));
        }

        voParts[i].setCgeneralhid(null);
        voParts[i].setCgeneralbid(null);
        voParts[i].setStatus(2);
      }
      return voParts;
    }
    return null;
  }

  public ICBcurrArithUI getCurrArith()
  {
    if (this.clsCurrArith == null)
      try {
        this.clsCurrArith = new ICBcurrArithUI(ClientEnvironment.getInstance().getCorporation().getPrimaryKey());
      } catch (Exception e) {
        GenMethod.handleException(this, null, e);
      }


    return this.clsCurrArith;
  }

  public void setListBodyData(GeneralBillItemVO[] voi)
  {
    if ((voi != null) && (voi.length > 0))
      setCurrDigit(4, (String)voi[0].getAttributeValue("cquotecurrency"));

    super.setListBodyData(voi);
  }

  public void setBillVO(GeneralBillVO bvo, boolean bUpdateBotton, boolean bExeFormule)
  {
    if ((bvo != null) && (bvo.getChildrenVO() != null) && (bvo.getChildrenVO().length > 0))
      setCurrDigit(5, (String)bvo.getChildrenVO()[0].getAttributeValue("cquotecurrency"));

    super.setBillVO(bvo, bUpdateBotton, bExeFormule);
  }

  public void setCurrDigit(int ipanelsatus, String cquotecurrency)
  {
    if ((cquotecurrency == null) || (cquotecurrency.trim().length() <= 0))
      return;

    int iDigit = 2;
    try {
      BillItem item;
      ICBcurrArithUI currtype = getCurrArith();

      Integer Digit = currtype.getBusiCurrDigit(cquotecurrency);

      if (Digit != null)
        iDigit = Digit.intValue();

      if (ipanelsatus == 5) {
        item = getBillCardPanel().getBodyItem("nquotemny");
        if (item != null)
          item.setDecimalDigits(iDigit);
      } else {
        item = getBillListPanel().getBodyBillModel().getItemByKey("nquotemny");
        if (item != null)
          item.setDecimalDigits(iDigit);
      }
    }
    catch (Exception e) {
      SCMEnv.error(e);
    }
  }
}