package nc.ui.rc.audit;

import java.awt.BorderLayout;
import java.awt.Color;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.ButtonGroup;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import nc.ui.bd.ref.DefaultRefModel;
import nc.ui.ml.NCLangRes;
import nc.ui.pu.pub.BusiTypeRefPane;
import nc.ui.pu.pub.PuProjectPhaseRefModel;
import nc.ui.pu.pub.PuTool;
import nc.ui.pu.pub.PurDeptRefModel;
import nc.ui.pu.pub.PurPsnRefModel;
import nc.ui.pu.pub.UpSrcInfoPanel;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRadioButton;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITabbedPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillScrollPane;
import nc.ui.pub.bill.IBillRelaSortListener2;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.rc.pub.CustRefModelForArr;
import nc.ui.rc.pub.InvCodeRefModelForArr;
import nc.ui.rc.pub.RcTool;
import nc.ui.rc.receive.ArriveorderHelper;
import nc.ui.scm.file.DocumentManager;
import nc.ui.scm.pub.def.DefSetTool;
import nc.ui.scm.pub.report.BillRowNo;
import nc.ui.scm.ref.WarehouseRefModel;
import nc.ui.scm.service.LocalCallService;
import nc.ui.scm.sourcebill.SourceBillFlowDlg;
import nc.vo.bd.CorpVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.query.ConditionVO;
import nc.vo.rc.receive.ArriveorderHeaderVO;
import nc.vo.rc.receive.ArriveorderItemVO;
import nc.vo.rc.receive.ArriveorderVO;
import nc.vo.scm.datapower.BtnPowerVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.service.ServcallVO;
import nc.vo.sm.UserVO;

public class AuditUI extends ToftPanel
  implements BillEditListener, ListSelectionListener, IBillRelaSortListener2
{
  protected ButtonObject m_btnDocument = new ButtonObject(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000278"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000278"), 2, "文档管理");
  private ButtonObject m_btnSelectAllArr = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000041"), NCLangRes.getInstance().getStrByID("40040302", "UPP40040302-000005"), 2, "全选");
  private ButtonObject m_btnCancelAllArr = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000042"), NCLangRes.getInstance().getStrByID("40040302", "UPP40040302-000006"), 2, "全消");
  private ButtonObject m_btnAudit = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000027"), NCLangRes.getInstance().getStrByID("common", "UC001-0000027"), 2, "审核");
  private ButtonObject m_btnDisAudit = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000028"), NCLangRes.getInstance().getStrByID("common", "UC001-0000028"), 2, "弃审");
  private ButtonObject m_btnQuery = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000006"), NCLangRes.getInstance().getStrByID("common", "UC001-0000006"), 2, "查询");
  private ButtonObject m_btnRefresh = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000009"), NCLangRes.getInstance().getStrByID("common", "UC001-0000009"), 2, "刷新");

  private ButtonObject m_btnLookSrcBill = new ButtonObject(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000145"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000145"), 2, "联查");

  private ButtonObject[] m_aryArrListButtons = { this.m_btnSelectAllArr, this.m_btnCancelAllArr, this.m_btnAudit, this.m_btnDisAudit, this.m_btnQuery, this.m_btnRefresh, this.m_btnDocument, this.m_btnLookSrcBill };

  private BillListPanel m_arrListPanel = null;
  private BillCardPanel m_arrBillPanel = null;

  private QueryConditionClient m_dlgArrQueryCondition = null;

  private ArriveorderVO[] m_arriveVOs = null;

  private UIRadioButton m_rdoFree = null;
  private UIRadioButton m_rdoAudited = null;

  int nMeasDecimal = 2;
  int nAssistUnitDecimal = 2;
  int nPriceDecimal = 2;

  int nNmoneyDecimal = 2;

  private UpSrcInfoPanel m_pnlUpsrcTab = null;

  public Object[] getRelaSortObjectArray()
  {
    return this.m_arriveVOs;
  }

  public AuditUI()
  {
    initialize();
  }

  public AuditUI(String pk_corp, String billType, String businessType, String operator, String billID)
  {
    initialize();
    ArriveorderVO vo = null;
    try {
      vo = ArriveorderHelper.findByPrimaryKey(billID);
    } catch (Exception e) {
      SCMEnv.out(e);
      return;
    }

    getArrBillCardPanel().setBillValueVO(vo);
    getArrBillCardPanel().getBillModel().execLoadFormula();
    getArrBillCardPanel().updateValue();
    getArrBillCardPanel().updateUI();
  }

  private BillCardPanel getArrBillCardPanel() {
    if (this.m_arrBillPanel == null) {
      try {
        this.m_arrBillPanel = new BillCardPanel();
        try
        {
          this.m_arrBillPanel.loadTemplet("23", null, getOperatorId(), getCorpId());
        } catch (Exception ex) {
          reportException(ex);
          this.m_arrBillPanel.loadTemplet("40040301010000000000");
        }

        this.m_arrBillPanel.setBodyShowThMark(true);

        this.m_arrBillPanel.hideBodyTableCol("squalitylevelname");

        this.m_arrBillPanel.hideBodyTableCol("cdealname");

        if (this.m_arrBillPanel.getBodyItem("crowno") != null) {
          BillRowNo.loadRowNoItem(this.m_arrBillPanel, "crowno");
        }

        DefSetTool.updateBillCardPanelUserDef(this.m_arrBillPanel, getClientEnvironment().getCorporation().getPk_corp(), "23", "vdef", "vdef");

        this.m_arrBillPanel.setTatolRowShow(true);
      }
      catch (Throwable e) {
        SCMEnv.out("初始化单据模板(卡片)时出现异常：");
        SCMEnv.out(e);
      }
    }
    return this.m_arrBillPanel;
  }

  public void afterEdit(BillEditEvent e)
  {
  }

  public void bodyRowChange(BillEditEvent e)
  {
  }

  private boolean checkSelectedDataAudit(ArriveorderVO[] vos)
  {
    return true;
  }

  private boolean checkSelectedDataUnudit(ArriveorderVO[] vos)
  {
    return true;
  }

  private void displayOthersVOs(Vector subVOs)
  {
    Vector allVOs = new Vector();
    Vector newVOs = new Vector();
    ArriveorderVO[] arrvos = null;
    for (int i = 0; i < getM_arriveVOs().length; i++) {
      allVOs.addElement(getM_arriveVOs()[i]);
    }
    for (int i = 0; i < allVOs.size(); i++) {
      if (!subVOs.contains(allVOs.elementAt(i))) {
        newVOs.addElement(allVOs.elementAt(i));
      }
    }
    if (newVOs.size() > 0) {
      arrvos = new ArriveorderVO[newVOs.size()];
      newVOs.copyInto(arrvos);
      setM_arriveVOs(arrvos);
    } else {
      setM_arriveVOs(null);
    }

    setArriveVOsToArrList();

    if ((getM_arriveVOs() != null) && (getM_arriveVOs().length > 0)) {
      onSelectNo();
      getArrBillListPanel().getHeadTable().setRowSelectionInterval(0, 0);
      getArrBillListPanel().getHeadBillModel().setRowState(0, 4);
      setOperateButtonStateList(1);
    } else {
      setOperateButtonStateList(0);
    }updateButtons();
  }

  private BillListPanel getArrBillListPanel()
  {
    if (this.m_arrListPanel == null) {
      try {
        this.m_arrListPanel = new BillListPanel();
        try
        {
          this.m_arrListPanel.loadTemplet("2K", null, getOperatorId(), getCorpId());
        } catch (Exception ex) {
          reportException(ex);
          this.m_arrListPanel.loadTemplet("40040302010000000000");
        }

        this.m_arrListPanel.getParentListPanel().setShowThMark(true);
        this.m_arrListPanel.getChildListPanel().setShowThMark(true);

        this.m_arrListPanel.getBodyItem("narrvnum").setDecimalDigits(this.nMeasDecimal);
        this.m_arrListPanel.getBodyItem("npresentnum").setDecimalDigits(this.nMeasDecimal);
        this.m_arrListPanel.getBodyItem("nwastnum").setDecimalDigits(this.nMeasDecimal);
        this.m_arrListPanel.getBodyItem("nelignum").setDecimalDigits(this.nMeasDecimal);
        this.m_arrListPanel.getBodyItem("nnotelignum").setDecimalDigits(this.nMeasDecimal);

        this.m_arrListPanel.getBodyItem("nassistnum").setDecimalDigits(this.nAssistUnitDecimal);
        this.m_arrListPanel.getBodyItem("npresentassistnum").setDecimalDigits(this.nAssistUnitDecimal);
        this.m_arrListPanel.getBodyItem("nwastassistnum").setDecimalDigits(this.nAssistUnitDecimal);

        this.m_arrListPanel.getBodyItem("nprice").setDecimalDigits(this.nPriceDecimal);

        this.m_arrListPanel.getBodyItem("nmoney").setDecimalDigits(this.nNmoneyDecimal);

        this.m_arrListPanel.getHeadBillModel().addSortRelaObjectListener2(this);

        DefSetTool.updateBillListPanelUserDef(this.m_arrListPanel, getCorpId(), "23", "vdef", "vdef");
      }
      catch (Exception e)
      {
        reportException(e);
      }
    }
    return this.m_arrListPanel;
  }

  private void getArriveVOsFromDB()
  {
    try
    {
      ConditionVO[] condsUserDef = getQueryConditionDlg().getConditionVO();

      String strUpSrcSQlPart = this.m_pnlUpsrcTab.getSubSQL();

      boolean[] status = { this.m_rdoFree.isSelected(), this.m_rdoAudited.isSelected() };

      setM_arriveVOs(ArriveorderHelper.queryForAuditMy(condsUserDef, null, getCorpId(), status, strUpSrcSQlPart));

      if ((getM_arriveVOs() == null) || (getM_arriveVOs().length <= 0))
        MessageDialog.showWarningDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270"), NCLangRes.getInstance().getStrByID("40040302", "UPP40040302-000000"));
    }
    catch (Exception e) {
      reportException(e);
    }
  }

  private ArriveorderHeaderVO[] getArrvieHeaderVOs(ArriveorderVO[] arrivevos)
  {
    ArriveorderHeaderVO[] headers = null;
    if (arrivevos != null) {
      headers = new ArriveorderHeaderVO[arrivevos.length];
      for (int i = 0; i < arrivevos.length; i++) {
        headers[i] = ((ArriveorderHeaderVO)arrivevos[i].getParentVO());
        if (headers[i].getIsauditted().equals("已审批")) { headers[i].setIsauditted(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000301")); } else {
          if (!headers[i].getIsauditted().equals("未审批")) continue; headers[i].setIsauditted(NCLangRes.getInstance().getStrByID("40040302", "UPP40040302-000007"));
        }
      }
    }
    return headers;
  }

  private String getCorpId()
  {
    String corpid = null;
    corpid = getClientEnvironment().getCorporation().getPrimaryKey();
    if ((corpid == null) || (corpid.trim().equals(""))) {
      corpid = getCorpPrimaryKey();
    }
    return corpid;
  }

  private ArriveorderVO[] getM_arriveVOs()
  {
    return this.m_arriveVOs;
  }

  private QueryConditionClient getQueryConditionDlg()
  {
    if (this.m_dlgArrQueryCondition == null)
    {
      this.m_dlgArrQueryCondition = new QueryConditionClient(this);

      this.m_dlgArrQueryCondition.hideUnitButton();

      this.m_rdoFree = new UIRadioButton();
      this.m_rdoFree.setText(NCLangRes.getInstance().getStrByID("40040302", "UPP40040302-000007"));
      this.m_rdoFree.setBackground(getBackground());
      this.m_rdoFree.setForeground(Color.black);
      this.m_rdoFree.setSize(150, this.m_rdoFree.getHeight());
      this.m_rdoFree.setSelected(true);

      this.m_rdoAudited = new UIRadioButton();
      this.m_rdoAudited.setText(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000301"));
      this.m_rdoAudited.setBackground(this.m_rdoFree.getBackground());
      this.m_rdoAudited.setForeground(this.m_rdoFree.getForeground());
      this.m_rdoAudited.setSize(this.m_rdoFree.getSize());

      this.m_rdoFree.setLocation(50, 30);
      this.m_rdoAudited.setLocation(this.m_rdoFree.getX(), this.m_rdoFree.getY() + this.m_rdoFree.getHeight() + 20);

      ButtonGroup bg = new ButtonGroup();
      bg.add(this.m_rdoFree);
      bg.add(this.m_rdoAudited);
      bg.setSelected(this.m_rdoFree.getModel(), true);

      this.m_dlgArrQueryCondition.getUIPanelNormal().setLayout(null);

      this.m_dlgArrQueryCondition.getUIPanelNormal().add(this.m_rdoFree);
      this.m_dlgArrQueryCondition.getUIPanelNormal().add(this.m_rdoAudited);

      this.m_pnlUpsrcTab = new UpSrcInfoPanel("23", new String[] { "21", "61" });
      this.m_dlgArrQueryCondition.getUITabbedPane().insertTab(this.m_pnlUpsrcTab.getName(), null, this.m_pnlUpsrcTab, null, 1);

      this.m_dlgArrQueryCondition.setTempletID(getCorpId(), "40040302", getOperatorId(), null);

      DefSetTool.updateQueryConditionClientUserDef(this.m_dlgArrQueryCondition, getCorpId(), "23", "po_arriveorder.vdef", "po_arriveorder_b.vdef");

      UIRefPane refInvcode = new UIRefPane();
      refInvcode.setRefModel(new InvCodeRefModelForArr(getCorpId()));
      this.m_dlgArrQueryCondition.setValueRef("bd_invbasdoc.invcode", refInvcode);

      refInvcode = new UIRefPane();
      refInvcode.setRefModel(new InvCodeRefModelForArr(getCorpId()));
      this.m_dlgArrQueryCondition.setValueRef("bd_invbasdoc.invname", refInvcode);

      UIRefPane refVendor = new UIRefPane();
      refVendor.setRefModel(new CustRefModelForArr(getCorpId()));
      this.m_dlgArrQueryCondition.setValueRef("bd_cubasdoc.custcode", refVendor);

      refVendor = new UIRefPane();
      refVendor.setRefModel(new CustRefModelForArr(getCorpId()));
      this.m_dlgArrQueryCondition.setValueRef("bd_cubasdoc.custname", refVendor);

      UIRefPane refProjectPhase = new UIRefPane();
      refProjectPhase.setIsCustomDefined(true);
      PuProjectPhaseRefModel refjobphase = new PuProjectPhaseRefModel(getCorpId(), null);
      refProjectPhase.setRefModel(refjobphase);
      this.m_dlgArrQueryCondition.setValueRef("bd_jobphase.jobphasename", refProjectPhase);

      BusiTypeRefPane refBusitype = new BusiTypeRefPane(getCorpId(), "23");
      this.m_dlgArrQueryCondition.setValueRef("bd_busitype.businame", refBusitype);

      UIRefPane refpane = new UIRefPane();
      DefaultRefModel refModel = new DefaultRefModel();
      refModel.setPk_corp(getCorpId());
      refModel.setSealedDataShow(true);
      refModel.setRefNodeName("库存组织");
      refpane.setRefModel(refModel);
      this.m_dlgArrQueryCondition.setValueRef("bd_calbody.bodyname", refpane);

      UIRefPane refPrayPsn = new UIRefPane();

      PurPsnRefModel refPsnModel = new PurPsnRefModel(getCorpId());
      refPrayPsn.setRefModel(refPsnModel);
      this.m_dlgArrQueryCondition.setValueRef("bd_psndoc.psnname", refPrayPsn);

      UIRefPane refPrayDept = new UIRefPane();

      PurDeptRefModel refDeptModel = new PurDeptRefModel();
      refPrayDept.setRefModel(refDeptModel);
      this.m_dlgArrQueryCondition.setValueRef("bd_deptdoc.deptname", refPrayDept);

      UIRefPane refPane = new UIRefPane();

      WarehouseRefModel refModel1 = new WarehouseRefModel(getCorpId());
      refPane.setRefModel(refModel1);
      this.m_dlgArrQueryCondition.setValueRef("bd_stordoc.storname", refPane);

      this.m_dlgArrQueryCondition.setValueRef("po_arriveorder.dreceivedate", "日历");
      this.m_dlgArrQueryCondition.setDefaultValue("po_arriveorder.dreceivedate", "po_arriveorder.dreceivedate", getClientEnvironment().getDate().toString());

      this.m_dlgArrQueryCondition.setIsWarningWithNoInput(true);
    }
    return this.m_dlgArrQueryCondition;
  }

  private String getOperatorId()
  {
    String operatorid = getClientEnvironment().getUser().getPrimaryKey();
    if ((operatorid == null) || (operatorid.trim().equals("")) || (operatorid.equals("88888888888888888888"))) {
      operatorid = "10013488065564590288";
    }
    return operatorid;
  }

  private UFDate getOperDate()
  {
    UFDate ufd = null;
    ufd = getClientEnvironment().getDate();
    return ufd;
  }

  public String getTitle()
  {
    String title = NCLangRes.getInstance().getStrByID("40040302", "UPP40040302-000008");
    if (this.m_arrBillPanel != null)
      title = this.m_arrBillPanel.getTitle();
    return title;
  }

  private void initButtons()
  {
    setButtons(this.m_aryArrListButtons);
    for (int i = 0; i < this.m_aryArrListButtons.length; i++) {
      this.m_aryArrListButtons[i].setEnabled(false);
    }

    this.m_btnQuery.setEnabled(true);

    updateButtons();
  }

  private void initialize()
  {
    initPara();

    setLayout(new BorderLayout());
    add(getArrBillListPanel(), "Center");
    getArrBillListPanel().setEnabled(false);

    initButtons();

    initListener();
  }

  private void initListener()
  {
    getArrBillListPanel().addEditListener(this);

    getArrBillListPanel().getHeadTable().setCellSelectionEnabled(false);
    getArrBillListPanel().getHeadTable().setSelectionMode(2);
    getArrBillListPanel().getHeadTable().getSelectionModel().addListSelectionListener(this);
  }

  public void initPara()
  {
    try
    {
      ServcallVO[] scDisc = new ServcallVO[2];

      scDisc[0] = new ServcallVO();
      scDisc[0].setBeanName("nc.itf.pu.pub.IPub");
      scDisc[0].setMethodName("getDigitBatch");
      scDisc[0].setParameter(new Object[] { getCorpId(), new String[]{ "BD502", "BD503", "BD501", "BD505" } });
      scDisc[0].setParameterTypes(new Class[] { String.class, java.lang.String.class });

      scDisc[1] = new ServcallVO();
      scDisc[1].setBeanName("nc.itf.rc.receive.IArriveorder");
      scDisc[1].setMethodName("getCurrDecimal");
      scDisc[1].setParameter(new Object[] { getCorpId() });
      scDisc[1].setParameterTypes(new Class[] { String.class });

      Object[] oParaValue = LocalCallService.callService(scDisc);
      if ((oParaValue != null) && (oParaValue.length == scDisc.length))
      {
        int[] iDigits = (int[])(int[])oParaValue[0];
        if ((iDigits != null) && (iDigits.length == 4)) {
          this.nAssistUnitDecimal = iDigits[0];

          this.nMeasDecimal = iDigits[2];
          this.nPriceDecimal = iDigits[3];
        }

        this.nNmoneyDecimal = ((Integer)oParaValue[1]).intValue();
      }

    }
    catch (Exception e)
    {
      reportException(e);
    }
  }

  private void onLnkQuery()
  {
    int nSelected = getArrBillListPanel().getHeadTable().getSelectedRow();
    if (nSelected < 0) return;
    ArriveorderVO vo = getM_arriveVOs()[nSelected];
    if ((vo == null) || (vo.getParentVO() == null))
      return;
    SourceBillFlowDlg soureDlg = new SourceBillFlowDlg(this, "23", ((ArriveorderHeaderVO)vo.getParentVO()).getPrimaryKey(), null, getClientEnvironment().getUser().getPrimaryKey(), getCorpId());

    soureDlg.showModal();
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000019"));
  }

  private void onAudit()
  {
    Vector v = new Vector();
    ArriveorderHeaderVO head = new ArriveorderHeaderVO();

    int iRealPos = 0;
    int i = 0;
    while (i < getArrBillListPanel().getHeadBillModel().getRowCount())
    {
      if (getArrBillListPanel().getHeadBillModel().getRowState(i) == 4)
      {
        iRealPos = i;
        iRealPos = PuTool.getIndexBeforeSort(getArrBillListPanel(), iRealPos);

        head = (ArriveorderHeaderVO)getM_arriveVOs()[iRealPos].getParentVO();

        head.setCauditpsn(getOperatorId());
        head.setDauditdate(getOperDate());
        v.add(getM_arriveVOs()[iRealPos]);
      }
      i++;
    }

    ArriveorderVO[] arrivevos = null;
    if (v.size() > 0) {
      arrivevos = new ArriveorderVO[v.size()];
      v.copyInto(arrivevos);
    }
    if (checkSelectedDataAudit(arrivevos))
    {
      try
      {
        for (i = 0; i < arrivevos.length; i++) {
          arrivevos[i].getParentVO().setAttributeValue("cuserid", getOperatorId());
        }
        boolean isSucc = false;
        try
        {
          String strErr = PuTool.getAuditLessThanMakeMsg(arrivevos, "dreceivedate", "varrordercode", ClientEnvironment.getInstance().getDate(), "23");
          if (strErr != null) {
            throw new BusinessException(strErr);
          }

          ArriveorderHeaderVO[] heads = new ArriveorderHeaderVO[arrivevos.length];
          for (i = 0; i < arrivevos.length; i++) {
            heads[i] = ((ArriveorderHeaderVO)arrivevos[i].getParentVO());
          }

          arrivevos = RcTool.getRefreshedVOs(arrivevos);

          PfUtilClient.processBatchFlow(this, "APPROVE", "23", ClientEnvironment.getInstance().getDate().toString(), arrivevos, null);

          isSucc = PfUtilClient.isSuccess();
          if (isSucc)
          {
            displayOthersVOs(v);
            showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000071"));
          } else {
            showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000072"));
          }
        } catch (BusinessException e) {
          reportException(e);
          showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000072"));
          MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000422"), e.getMessage());
        } catch (Exception e) {
          reportException(e);
          showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000072"));
          if ((e instanceof RemoteException))
            MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000422"), e.getMessage());
        }
      }
      catch (Exception e) {
        SCMEnv.out(e);
        showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000072"));
      }
    }
    else showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000072"));
  }

  public void onButtonClicked(ButtonObject bo)
  {
    if (bo == this.m_btnQuery)
      onQuery();
    else if (bo == this.m_btnSelectAllArr)
      onSelectAll();
    else if (bo == this.m_btnCancelAllArr)
      onSelectNo();
    else if (bo == this.m_btnRefresh)
      onRefresh();
    else if (bo == this.m_btnAudit)
      onAudit();
    else if (bo == this.m_btnDisAudit)
      onUnAudit();
    else if (bo == this.m_btnDocument)
      onDocument();
    else if (bo == this.m_btnLookSrcBill)
      onLnkQuery();
  }

  private void onDocument()
  {
    String[] strPks = null;
    String[] strCodes = null;
    HashMap mapBtnPowerVo = new HashMap();
    Integer iBillStatus = null;

    if ((getM_arriveVOs() != null) && (getM_arriveVOs().length > 0)) {
      ArriveorderHeaderVO[] headers = null;
      headers = (ArriveorderHeaderVO[])(ArriveorderHeaderVO[])getArrBillListPanel().getHeadBillModel().getBodySelectedVOs(ArriveorderHeaderVO.class.getName());

      if ((headers == null) || (headers.length <= 0))
        return;
      strPks = new String[headers.length];
      strCodes = new String[headers.length];
      BtnPowerVO pVo = null;
      for (int i = 0; i < headers.length; i++) {
        strPks[i] = headers[i].getPrimaryKey();
        strCodes[i] = headers[i].getVarrordercode();

        pVo = new BtnPowerVO(strCodes[i]);
        iBillStatus = PuPubVO.getInteger_NullAs(headers[i].getIbillstatus(), new Integer(0));

        if ((iBillStatus.intValue() == 2) || (iBillStatus.intValue() == 3)) {
          pVo.setFileDelEnable("false");
        }
        mapBtnPowerVo.put(strCodes[i], pVo);
      }
    }
    if ((strPks == null) || (strPks.length <= 0)) {
      return;
    }
    DocumentManager.showDM(this, strPks, strCodes, mapBtnPowerVo);
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000025"));
  }

  private void onQuery()
  {
    getQueryConditionDlg().showModal();

    if (getQueryConditionDlg().isCloseOK()) {
      onRefresh();
    }
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH009"));
  }

  private void onRefresh()
  {
    showHintMessage(NCLangRes.getInstance().getStrByID("40040302", "UPP40040302-000001"));

    getArriveVOsFromDB();

    setArriveVOsToArrList();
    showHintMessage(NCLangRes.getInstance().getStrByID("40040302", "UPP40040302-000002"));

    if ((getM_arriveVOs() != null) && (getM_arriveVOs().length > 0)) {
      onSelectNo();
      getArrBillListPanel().getHeadTable().setRowSelectionInterval(0, 0);
      getArrBillListPanel().getHeadBillModel().setRowState(0, 4);
      setOperateButtonStateList(1);
    } else {
      setOperateButtonStateList(0);
    }updateButtons();
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH007"));
  }

  private void onSelectAll()
  {
    int iLen = getArrBillListPanel().getHeadBillModel().getRowCount();
    getArrBillListPanel().getHeadTable().setRowSelectionInterval(0, iLen - 1);
    for (int i = 0; i < iLen; i++) {
      getArrBillListPanel().getHeadBillModel().setRowState(i, 4);
    }

    setOperateButtonStateList(iLen);
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000033"));
  }

  private void onSelectNo()
  {
    int iLen = getArrBillListPanel().getHeadBillModel().getRowCount();
    getArrBillListPanel().getHeadTable().removeRowSelectionInterval(0, iLen - 1);
    for (int i = 0; i < iLen; i++) {
      getArrBillListPanel().getHeadBillModel().setRowState(i, 0);
    }

    setOperateButtonStateList(0);
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000034"));
  }

  private void onUnAudit()
  {
    Vector v = new Vector();
    int rowCount = getArrBillListPanel().getHeadBillModel().getRowCount();
    BillModel bm = getArrBillListPanel().getHeadBillModel();
    ArriveorderVO vo = null;
    ArriveorderVO[] arrivevos = null;

    int iRealPos = 0;
    for (int i = 0; i < rowCount; i++) {
      if (bm.getRowState(i) == 4) {
        iRealPos = i;
        iRealPos = PuTool.getIndexBeforeSort(getArrBillListPanel(), iRealPos);

        vo = getM_arriveVOs()[iRealPos];

        v.add(vo);
      }
    }
    if (v.size() > 0) {
      arrivevos = new ArriveorderVO[v.size()];
      v.copyInto(arrivevos);
      if (checkSelectedDataUnudit(arrivevos))
      {
        try
        {
          for (int i = 0; i < arrivevos.length; i++) {
            arrivevos[i].getParentVO().setAttributeValue("cuserid", getOperatorId());
          }

          ArriveorderHeaderVO[] heads = new ArriveorderHeaderVO[arrivevos.length];
          for (int i = 0; i < arrivevos.length; i++) {
            heads[i] = ((ArriveorderHeaderVO)arrivevos[i].getParentVO());
          }

          arrivevos = RcTool.getRefreshedVOs(arrivevos);

          boolean isSucess = false;
          PfUtilClient.processBatch(this, "UNAPPROVE", "23", ClientEnvironment.getInstance().getDate().toString(), arrivevos, null);

          isSucess = PfUtilClient.isSuccess();
          if (isSucess)
          {
            displayOthersVOs(v);
            showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000184"));
          } else {
            showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000185"));
          }
        } catch (BusinessException e) {
          reportException(e);
          showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000185"));
          MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000422"), e.getMessage());
        } catch (Exception ex) {
          reportException(ex);
          showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000185"));
          if ((ex instanceof RemoteException))
            MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000422"), NCLangRes.getInstance().getStrByID("40040302", "UPP40040302-000003") + ex.getMessage());
        }
      }
      else
        showHintMessage(NCLangRes.getInstance().getStrByID("40040302", "UPP40040302-000004"));
    }
  }

  private void setArriveVOsToArrList()
  {
    if ((getM_arriveVOs() != null) && (getM_arriveVOs().length > 0)) {
      getArrBillListPanel().getBodyBillModel().clearBodyData();
      ArriveorderHeaderVO[] headers = null;
      headers = getArrvieHeaderVOs(getM_arriveVOs());
      getArrBillListPanel().setHeaderValueVO(headers);
      try {
        getArrBillListPanel().getHeadBillModel().execLoadFormula();
      } catch (Exception e) {
        SCMEnv.out(e);
      }

      getArrBillListPanel().getHeadTable().setRowSelectionInterval(0, 0);
      getArrBillListPanel().getHeadBillModel().setRowState(0, 4);
    }
    else
    {
      try {
        getArrBillListPanel().getHeadBillModel().clearBodyData();
        getArrBillListPanel().getBodyBillModel().clearBodyData();
      } catch (Exception e) {
        SCMEnv.out(e);
      }
    }
  }

  private void setListBodyData(int row)
  {
    try
    {
      ArriveorderItemVO[] items = null;
      getM_arriveVOs()[row] = RcTool.getRefreshedVO(getM_arriveVOs()[row]);
      if ((getM_arriveVOs()[row] != null) && (getM_arriveVOs()[row].getChildrenVO() != null) && (getM_arriveVOs()[row].getChildrenVO().length > 0))
        items = (ArriveorderItemVO[])(ArriveorderItemVO[])getM_arriveVOs()[row].getChildrenVO();
      getArrBillListPanel().setBodyValueVO(items);
      getArrBillListPanel().getBodyBillModel().execLoadFormula();

      PuTool.loadSourceInfoAll(getArrBillListPanel(), "23");
    } catch (Exception e) {
      if ((e instanceof BusinessException))
        MessageDialog.showWarningDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000422"), e.getMessage());
      SCMEnv.out(e);
      getArrBillListPanel().getBodyBillModel().clearBodyData();
      setListBtnsWhenErr();
    }
  }

  private void setListBtnsWhenErr()
  {
    for (int i = 0; i < this.m_aryArrListButtons.length; i++) {
      this.m_aryArrListButtons[i].setEnabled(true);
    }

    this.m_btnAudit.setEnabled(false);
    this.m_btnDisAudit.setEnabled(false);
    this.m_btnDocument.setEnabled(false);

    for (int i = 0; i < this.m_aryArrListButtons.length; i++)
      updateButton(this.m_aryArrListButtons[i]);
  }

  private void setM_arriveVOs(ArriveorderVO[] newM_arriveVOs)
  {
    this.m_arriveVOs = newM_arriveVOs;
  }

  private void setOperateButtonStateList(int cnt)
  {
    int iRowCount = getArrBillListPanel().getHeadBillModel().getRowCount();
    if (iRowCount <= 0) {
      for (int i = 0; i < this.m_aryArrListButtons.length; i++) {
        this.m_aryArrListButtons[i].setEnabled(false);
      }
      this.m_btnQuery.setEnabled(true);
    } else if (cnt <= 0) {
      for (int i = 0; i < this.m_aryArrListButtons.length; i++) {
        this.m_aryArrListButtons[i].setEnabled(false);
      }
      this.m_btnSelectAllArr.setEnabled(true);
      this.m_btnQuery.setEnabled(true);
      this.m_btnRefresh.setEnabled(true);
    }
    else {
      if (this.m_rdoFree.isSelected()) {
        this.m_btnAudit.setEnabled(true);
        this.m_btnDisAudit.setEnabled(false);
      }

      if (this.m_rdoAudited.isSelected()) {
        this.m_btnDisAudit.setEnabled(true);
        this.m_btnAudit.setEnabled(false);
      }

      if (cnt != iRowCount)
        this.m_btnSelectAllArr.setEnabled(true);
      else {
        this.m_btnSelectAllArr.setEnabled(false);
      }

      if (cnt > 0)
        this.m_btnCancelAllArr.setEnabled(true);
      else {
        this.m_btnCancelAllArr.setEnabled(false);
      }

      this.m_btnQuery.setEnabled(true);

      this.m_btnRefresh.setEnabled(true);

      this.m_btnDocument.setEnabled(true);

      if (getArrBillListPanel().getHeadTable().getSelectedRowCount() == 1) this.m_btnLookSrcBill.setEnabled(true); else
        this.m_btnLookSrcBill.setEnabled(false);
    }
    for (int i = 0; i < this.m_aryArrListButtons.length; i++)
      updateButton(this.m_aryArrListButtons[i]);
  }

  public void valueChanged(ListSelectionEvent e)
  {
    if (e.getValueIsAdjusting())
      return;
    int m_nFirstSelectedIndex = -1;

    int iSelCnt = 0;

    int iCount = getArrBillListPanel().getHeadTable().getRowCount();
    for (int i = 0; i < iCount; i++) {
      getArrBillListPanel().getHeadBillModel().setRowState(i, 0);
    }

    int[] iaSelectedRow = getArrBillListPanel().getHeadTable().getSelectedRows();
    if ((iaSelectedRow == null) || (iaSelectedRow.length == 0)) {
      m_nFirstSelectedIndex = -1;
      setOperateButtonStateList(0);
    } else {
      iSelCnt = iaSelectedRow.length;

      for (int i = 0; i < iSelCnt; i++) {
        getArrBillListPanel().getHeadBillModel().setRowState(iaSelectedRow[i], 4);
      }

    }

    if ((iSelCnt == 1) && (iaSelectedRow != null) && (iaSelectedRow.length > 0))
      m_nFirstSelectedIndex = iaSelectedRow[0];
    if (m_nFirstSelectedIndex < 0) {
      getArrBillListPanel().setBodyValueVO(null);
    } else {
      int nCurIndex = PuTool.getIndexBeforeSort(getArrBillListPanel(), m_nFirstSelectedIndex);

      setListBodyData(nCurIndex);

      getArrBillListPanel().getBodyTable().updateUI();
    }

    setOperateButtonStateList(iSelCnt);
    updateButtons();
  }
}