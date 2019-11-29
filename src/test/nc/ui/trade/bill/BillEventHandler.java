package nc.ui.trade.bill;

import java.io.PrintStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import nc.bs.logging.Logger;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.pub.print.IDataSource;
import nc.ui.pub.print.PrintEntry;
import nc.ui.trade.base.AbstractBillUI;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.bsdelegate.DefaultBusinessSplit;
import nc.ui.trade.bsdelegate.IBusinessSplit;
import nc.ui.trade.buffer.BillUIBuffer;
import nc.ui.trade.buffer.RecordNotFoundExcetption;
import nc.ui.trade.businessaction.BdBusinessAction;
import nc.ui.trade.businessaction.BusinessAction;
import nc.ui.trade.businessaction.IBusinessController;
import nc.ui.trade.button.ButtonManager;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.handler.EventHandler;
import nc.ui.trade.pub.BillDirectPrint;
import nc.ui.trade.pub.CardPanelPRTS;
import nc.ui.trade.pub.ReportTreeTableModelAdapter;
import nc.ui.trade.query.INormalQuery;
import nc.vo.bd.CorpVO;
import nc.vo.format.Format;
import nc.vo.pf.pub.BusitypeVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.trade.button.ButtonVO;
import nc.vo.trade.field.IBillField;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.trade.pub.IExAggVO;
import nc.vo.trade.summarize.Hashlize;
import nc.vo.trade.summarize.VOHashPrimaryKeyAdapter;

public abstract class BillEventHandler extends EventHandler
{
  private static final String staticACTION = "BOACTION";
  private static final String staticASS = "BOASS";
  private IBillBusiListener m_bbl = null;

  public BillEventHandler(AbstractBillUI billUI, IControllerBase control)
  {
    super(billUI, control);
  }

  protected void afterOnBoAction(int intBtn, AggregatedValueObject billVo)
    throws Exception
  {
  }

  protected void afterOnBoAss(ButtonObject bo)
    throws Exception
  {
  }

  protected void beforeOnBoAction(int intBtn, AggregatedValueObject billVo)
    throws Exception
  {
    if ((billVo instanceof HYBillVO))
      ((HYBillVO)billVo).setM_billField(getBillField());
  }

  protected void beforeOnBoAss(ButtonObject bo)
    throws Exception
  {
  }

  protected void busiTypeBefore(AbstractBillUI billUI, ButtonObject bo)
    throws Exception
  {
  }

  private void clearChildPk(CircularlyAccessibleValueObject[] vos)
    throws Exception
  {
    if ((vos == null) || (vos.length == 0))
      return;
    for (int i = 0; i < vos.length; i++)
      vos[i].setPrimaryKey(null);
  }

  private void complexOnButton(int intBtn, ButtonObject bo)
    throws Exception
  {
    switch (intBtn) {
    case 2:
      getBillUI().showHintMessage(NCLangRes.getInstance().getStrByID("uifactory", "UPPuifactory-000060"));

      onBoBusiType(bo);
      break;
    case 1:
      getBillUI().showHintMessage(NCLangRes.getInstance().getStrByID("uifactory", "UPPuifactory-000061"));

      onBoBusiTypeAdd(bo, null);
      break;
    case 25:
      onBoAction(bo);
      break;
    case 29:
      getBillUI().showHintMessage(NCLangRes.getInstance().getStrByID("uifactory", "UPPuifactory-000062"));

      onBoAss(bo);
      break;
    case 10:
      onBoLine(bo);
      break;
    case 16:
      onBoFile(bo);
    case 20:
      onBoBrow(bo);
      break;
    case 33:
      onBoNodekey(bo);
      break;
    case 9:
      ButtonVO btnVo = (ButtonVO)bo.getData();
      onBoElse(btnVo.getBtnNo());
    case 3:
    case 4:
    case 5:
    case 6:
    case 7:
    case 8:
    case 11:
    case 12:
    case 13:
    case 14:
    case 15:
    case 17:
    case 18:
    case 19:
    case 21:
    case 22:
    case 23:
    case 24:
    case 26:
    case 27:
    case 28:
    case 30:
    case 31:
    case 32: }  } 
  protected IBusinessController createBusinessAction() { switch (getUIController().getBusinessActionType()) {
    case 0:
      return new BusinessAction(getBillUI());
    case 1:
      return new BdBusinessAction(getBillUI());
    }
    return new BusinessAction(getBillUI());
  }

  protected abstract BillCardPanelWrapper getBillCardPanelWrapper();

  protected final BusinessDelegator getBusiDelegator()
  {
    return getBillUI().getBusiDelegator();
  }

  public final void initActionButton()
  {
    try
    {
      ButtonObject boAction = getButtonManager().getButton(25);

      if (getBillUI().isBusinessType().booleanValue()) {
        ButtonObject boBusitype = getButtonManager().getButton(2);

        ButtonObject boAdd = getButtonManager().getButton(1);

        getBusiDelegator().retBusinessBtn(boBusitype, _getCorp().getPrimaryKey(), getUIController().getBillType());

        if ((boBusitype.getChildButtonGroup() != null) && (boBusitype.getChildButtonGroup().length > 0))
        {
          boBusitype.getChildButtonGroup()[0].setSelected(true);
          boBusitype.setCheckboxGroup(true);

          if (boAdd != null) {
            getBusiDelegator().retAddBtn(boAdd, _getCorp().getPrimaryKey(), getUIController().getBillType(), boBusitype.getChildButtonGroup()[0]);
          }

          ButtonObject bo = boBusitype.getChildButtonGroup()[0];

          BusitypeVO vo = (BusitypeVO)bo.getData();

          getBillUI().setBusinessType(vo.getPrimaryKey());

          getBillUI().setBusicode(vo.getBusicode());

          if (boAction != null) {
            getBusiDelegator().retElseBtn(boAction, getUIController().getBillType(), "BOACTION");
          }
          initAssButton(boBusitype.getChildButtonGroup()[0]);
        } else {
          System.out.println("没有初始化业务类型!");
        }
      }
      else if (boAction != null) {
        getBusiDelegator().retElseBtn(boAction, getUIController().getBillType(), "BOACTION");
      }

      if (boAction != null)
        getButtonManager().setActionButtonVO();
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      getBillUI().showHintMessage(NCLangRes.getInstance().getStrByID("uifactory", "UPPuifactory-000063"));
    }
  }

  private void initAssButton(ButtonObject bo)
    throws Exception
  {
    ButtonObject boAss = getButtonManager().getButton(29);
    if (boAss == null)
      return;
    getBusiDelegator().retElseBtn(boAss, getUIController().getBillType(), "BOASS");
  }

  public boolean isAdding()
  {
    return (getBillUI().getBillOperate() == 1) || (getBillUI().getBillOperate() == 3);
  }

  public boolean isEditing()
  {
    return getBillUI().getBillOperate() == 0;
  }

  public void onBillRef()
    throws Exception
  {
    ButtonObject btn = new ButtonObject(NCLangRes.getInstance().getStrByID("uifactory", "UC001-0000010"));

    btn.setTag(getBillUI().getRefBillType() + ":");
    onBoBusiTypeAdd(btn, null);
  }

  private final void onBoAction(ButtonObject bo)
    throws Exception
  {
    long lngTime = System.currentTimeMillis();
    getBillUI().showHintMessage(NCLangRes.getInstance().getStrByID("uifactory", "UPPuifactory-000179"));

    ButtonVO btnVo = (ButtonVO)bo.getData();
    if (btnVo == null)
      return;
    switch (btnVo.getBtnNo()) {
    case 26:
      onBoAudit();
      break;
    case 27:
      onBoCancelAudit();
      break;
    case 28:
      onBoCommit();
      break;
    case 4:
      onBoDel();
      break;
    default:
      onBoActionElse(bo);
    }

    getBillUI().showHintMessage(NCLangRes.getInstance().getStrByID("uifactory", "UPPuifactory-000180"));
  }

  public void onBoActionElse(ButtonObject bo)
    throws Exception
  {
    AggregatedValueObject modelVo = getBufferData().getCurrentVOClone();
    int intBtn = 0;
    if (bo.getData() != null)
      intBtn = ((ButtonVO)bo.getData()).getBtnNo();
    beforeOnBoAction(intBtn, modelVo);

    Object retObj = getBusinessAction().processAction(bo.getTag(), modelVo, getUIController().getBillType(), getBillUI()._getDate().toString(), getBillUI().getUserObject());

    if (PfUtilClient.isSuccess()) {
      if ((retObj instanceof AggregatedValueObject)) {
        AggregatedValueObject retVo = (AggregatedValueObject)retObj;
        afterOnBoAction(intBtn, retVo);
        CircularlyAccessibleValueObject[] childVos = getChildVO(retVo);
        if (childVos == null)
          modelVo.setParentVO(retVo.getParentVO());
        else {
          modelVo = retVo;
        }
      }
      getBufferData().setVOAt(getBufferData().getCurrentRow(), modelVo);
      getBufferData().setCurrentRow(getBufferData().getCurrentRow());
    }
  }

  public void onBoAdd(ButtonObject bo)
    throws Exception
  {
    getBillUI().setBillOperate(1);
  }

  private final void onBoAss(ButtonObject bo)
    throws Exception
  {
    beforeOnBoAss(bo);
    AggregatedValueObject modelVo = getBufferData().getCurrentVO();
    Object ret = getBusinessAction().processAction(bo.getTag(), modelVo, getUIController().getBillType(), getBillUI()._getDate().toString(), getBillUI().getUserObject());

    if ((ret != null) && ((ret instanceof AggregatedValueObject))) {
      AggregatedValueObject vo = (AggregatedValueObject)ret;

      modelVo.getParentVO().setAttributeValue(getBillField().getField_BillStatus(), vo.getParentVO().getAttributeValue(getBillField().getField_BillStatus()));

      modelVo.getParentVO().setAttributeValue("ts", vo.getParentVO().getAttributeValue("ts"));

      getBufferData().setVOAt(getBufferData().getCurrentRow(), modelVo);
      getBufferData().setCurrentRow(getBufferData().getCurrentRow());
    }
    afterOnBoAss(bo);
  }

  protected void onBoAssign()
    throws Exception
  {
  }

  public void onBoAudit()
    throws Exception
  {
    AggregatedValueObject modelVo = getBufferData().getCurrentVOClone();
    setCheckManAndDate(modelVo);

    if (checkVOStatus(modelVo, new int[] { 1 })) {
      System.out.println("无效的鼠标处理机制");
      return;
    }
    beforeOnBoAction(26, modelVo);

    AggregatedValueObject retVo = getBusinessAction().approve(modelVo, getUIController().getBillType(), getBillUI()._getDate().toString(), getBillUI().getUserObject());

    if (PfUtilClient.isSuccess())
    {
      afterOnBoAction(26, retVo);
      CircularlyAccessibleValueObject[] childVos = getChildVO(retVo);
      if (childVos == null)
        modelVo.setParentVO(retVo.getParentVO());
      else {
        modelVo = retVo;
      }
      getBufferData().setVOAt(getBufferData().getCurrentRow(), modelVo);
      getBufferData().setCurrentRow(getBufferData().getCurrentRow());
    }
  }

  protected void onBoBodyQuery()
    throws Exception
  {
    StringBuffer strWhere = new StringBuffer();

    if (!askForBodyQueryCondition(strWhere))
      return;
    doBodyQuery(strWhere.toString());
  }

  protected void doBodyQuery(String strWhere)
    throws Exception, ClassNotFoundException, InstantiationException, IllegalAccessException
  {
    SuperVO[] queryVos = getBusiDelegator().queryByCondition(Class.forName(getUIController().getBillVoName()[2]), strWhere == null ? "" : strWhere);

    getBufferData().clear();

    AggregatedValueObject vo = (AggregatedValueObject)Class.forName(getUIController().getBillVoName()[0]).newInstance();

    vo.setChildrenVO(queryVos);
    getBufferData().addVOToBuffer(vo);

    updateBuffer();
  }

  protected boolean askForBodyQueryCondition(StringBuffer sqlWhereBuf)
    throws Exception
  {
    if (sqlWhereBuf == null) {
      throw new IllegalArgumentException("askForQueryCondition().sqlWhereBuf cann't be null");
    }
    UIDialog querydialog = getQueryUI();

    if (querydialog.showModal() != 1)
      return false;
    INormalQuery query = (INormalQuery)querydialog;

    String strWhere = query.getWhereSql();
    if (strWhere == null) {
      strWhere = "1=1";
    }
    strWhere = "(" + strWhere + ") and (isnull(dr,0)=0)";

    if (getUIController().getBodyCondition() != null) {
      strWhere = strWhere + " and " + getUIController().getBodyCondition();
    }

    sqlWhereBuf.append(strWhere);
    return true;
  }

  private void onBoBrow(ButtonObject bo)
    throws Exception
  {
    long lngTime = System.currentTimeMillis();
    int intBtn = Integer.parseInt(bo.getTag());

    buttonActionBefore(getBillUI(), intBtn);
    switch (intBtn) {
    case 21:
      getBufferData().first();
      break;
    case 23:
      getBufferData().prev();
      break;
    case 22:
      getBufferData().next();
      break;
    case 24:
      getBufferData().last();
    }

    buttonActionAfter(getBillUI(), intBtn);
    getBillUI().showHintMessage(NCLangRes.getInstance().getStrByID("uifactory", "UPPuifactory-000503", null, new String[] { Format.indexFormat(getBufferData().getCurrentRow() + 1) }));
  }

  private final void onBoBusiType(ButtonObject bo)
    throws Exception
  {
    busiTypeBefore(getBillUI(), bo);
    bo.setSelected(true);

    BusitypeVO vo = (BusitypeVO)bo.getData();

    getBusiDelegator().retAddBtn(getButtonManager().getButton(1), _getCorp().getPrimaryKey(), getUIController().getBillType(), bo);

    getBusiDelegator().retElseBtn(getButtonManager().getButton(25), getUIController().getBillType(), "BOACTION");

    getButtonManager().setActionButtonVO();

    String oldtype = getBillUI().getBusinessType();
    String newtype = vo.getPrimaryKey();
    String oldcode = getBillUI().getBusicode();
    String newcode = vo.getBusicode();

    getBillUI().setBusinessType(newtype);

    getBillUI().setBusicode(newcode);

    getBillUI().initUI();

    getBillUI().getBufferData().clear();
    getBillUI().getBufferData().setCurrentRow(-1);

    getBillUI().updateButtonUI();

    if (this.m_bbl != null) {
      BusiTypeChangeEvent e = new BusiTypeChangeEvent(this, oldtype, newtype, oldcode, newcode);

      this.m_bbl.busiTypeChange(e);
    }
  }

  public final void onBoRefAdd(String strRefBillType, String sourceBillId)
    throws Exception
  {
    onBoBusiTypeAdd(getBusiDelegator().getRefButton(getButtonManager().getButton(1), strRefBillType), sourceBillId);
  }

  private final void onBoBusiTypeAdd(ButtonObject bo, String sourceBillId)
    throws Exception
  {
    getBusiDelegator().childButtonClicked(bo, _getCorp().getPrimaryKey(), getBillUI()._getModuleCode(), _getOperator(), getUIController().getBillType(), getBillUI(), getBillUI().getUserObject(), sourceBillId);

    if (PfUtilClient.makeFlag)
    {
      getBillUI().setCardUIState();

      getBillUI().setBillOperate(1);
    }
    else if (PfUtilClient.isCloseOK()) {
      if (this.m_bbl != null) {
        String tmpString = bo.getTag();
        int findIndex = tmpString.indexOf(":");
        String newtype = tmpString.substring(0, findIndex);
        RefBillTypeChangeEvent e = new RefBillTypeChangeEvent(this, null, newtype);

        this.m_bbl.refBillTypeChange(e);
      }
      if (isDataChange())
        setRefData(PfUtilClient.getRetVos());
      else
        setRefData(PfUtilClient.getRetOldVos());
    }
  }

  protected void onBoCancel()
    throws Exception
  {
    if (getBufferData().isVOBufferEmpty()) {
      getBillUI().setBillOperate(4);
    } else {
      getBillUI().setBillOperate(2);
      getBufferData().setCurrentRow(getBufferData().getCurrentRow());
    }
  }

  protected void onBoCancelAudit()
    throws Exception
  {
    AggregatedValueObject modelVo = getBufferData().getCurrentVOClone();

    setCheckManAndDate(modelVo);

    if (checkVOStatus(modelVo, new int[] { 8 })) {
      System.out.println("无效的鼠标处理机制");
      return;
    }
    beforeOnBoAction(27, modelVo);

    AggregatedValueObject retVo = getBusinessAction().unapprove(modelVo, getUIController().getBillType(), getBillUI()._getDate().toString(), getBillUI().getUserObject());

    if (PfUtilClient.isSuccess()) {
      afterOnBoAction(27, modelVo);
      CircularlyAccessibleValueObject[] childVos = getChildVO(retVo);
      if (childVos == null)
        modelVo.setParentVO(retVo.getParentVO());
      else {
        modelVo = retVo;
      }
      Integer intState = (Integer)modelVo.getParentVO().getAttributeValue(getBillField().getField_BillStatus());

      if (intState.intValue() == 8) {
        modelVo.getParentVO().setAttributeValue(getBillField().getField_CheckMan(), null);

        modelVo.getParentVO().setAttributeValue(getBillField().getField_CheckDate(), null);
      }

      getBufferData().setVOAt(getBufferData().getCurrentRow(), modelVo);
      getBufferData().setCurrentRow(getBufferData().getCurrentRow());
    }
  }

  protected void onBoCard()
    throws Exception
  {
  }

  protected void onBoCommit()
    throws Exception
  {
    AggregatedValueObject modelVo = getBufferData().getCurrentVOClone();

    modelVo.getParentVO().setAttributeValue(getBillField().getField_Operator(), getBillUI()._getOperator());

    beforeOnBoAction(28, modelVo);
    getBillUI().getEnvironment(); String strTime = ClientEnvironment.getServerTime().toString();

    ArrayList retList = getBusinessAction().commit(modelVo, getUIController().getBillType(), getBillUI()._getDate().toString() + strTime.substring(10), getBillUI().getUserObject());

    if (PfUtilClient.isSuccess()) {
      Object o = retList.get(1);
      if ((o instanceof AggregatedValueObject)) {
        AggregatedValueObject retVo = (AggregatedValueObject)o;
        afterOnBoAction(28, retVo);
        CircularlyAccessibleValueObject[] childVos = getChildVO(retVo);
        if (childVos == null)
          modelVo.setParentVO(retVo.getParentVO());
        else
          modelVo = retVo;
      }
      getBufferData().setVOAt(getBufferData().getCurrentRow(), modelVo);
      getBufferData().setCurrentRow(getBufferData().getCurrentRow());
    }
  }

  protected void onBoCopy()
    throws Exception
  {
    AggregatedValueObject copyVo = getBufferData().getCurrentVOClone();

    copyVo.getParentVO().setPrimaryKey(null);
    if ((copyVo instanceof IExAggVO))
      clearChildPk(((IExAggVO)copyVo).getAllChildrenVO());
    else {
      clearChildPk(copyVo.getChildrenVO());
    }

    getBillUI().setBillOperate(1);

    getBillUI().setCardUIData(copyVo);
  }

  protected void onBoDel()
    throws Exception
  {
    AggregatedValueObject modelVo = getBufferData().getCurrentVO();

    if ((modelVo instanceof HYBillVO)) {
      ((HYBillVO)modelVo).setM_billField(getBillField());
    }
    AggregatedValueObject delVo = getBusinessAction().delete(modelVo, getUIController().getBillType(), getBillUI()._getDate().toString(), getBillUI().getUserObject());

    if (PfUtilClient.isSuccess()) {
      getBillUI().setBillOperate(2);

      modelVo.getParentVO().setAttributeValue(getBillField().getField_BillStatus(), new Integer(4));

      modelVo.getParentVO().setAttributeValue("ts", delVo.getParentVO().getAttributeValue("ts"));

      getBufferData().setVOAt(getBufferData().getCurrentRow(), modelVo);
      getBufferData().setCurrentRow(getBufferData().getCurrentRow());
    }
  }

  protected void onBoDelete()
    throws Exception
  {
    if (getBufferData().getCurrentVO() == null) {
      return;
    }
    if (MessageDialog.showOkCancelDlg(getBillUI(), NCLangRes.getInstance().getStrByID("uifactory", "UPPuifactory-000064"), NCLangRes.getInstance().getStrByID("uifactory", "UPPuifactory-000065"), 2) != 1)
    {
      return;
    }
    AggregatedValueObject modelVo = getBufferData().getCurrentVO();
    getBusinessAction().delete(modelVo, getUIController().getBillType(), getBillUI()._getDate().toString(), getBillUI().getUserObject());

    if (PfUtilClient.isSuccess())
    {
      getBillUI().removeListHeadData(getBufferData().getCurrentRow());
      if ((getUIController() instanceof ISingleController)) {
        ISingleController sctl = (ISingleController)getUIController();
        if (!sctl.isSingleDetail())
          getBufferData().removeCurrentRow();
      } else {
        getBufferData().removeCurrentRow();
      }
    }

    if (getBufferData().getVOBufferSize() == 0)
      getBillUI().setBillOperate(4);
    else
      getBillUI().setBillOperate(2);
    getBufferData().setCurrentRow(getBufferData().getCurrentRow());
  }

  protected void onBoEdit()
    throws Exception
  {
    if (getBufferData().getCurrentVO() == null)
      return;
    String strTime = getBillUI()._getServerTime().toString();
    AggregatedValueObject modelVo = getBufferData().getCurrentVO();
    Object o = getBusinessAction().edit(modelVo, getUIController().getBillType(), getBillUI()._getDate().toString() + strTime.substring(10), null);

    if ((o instanceof AggregatedValueObject)) {
      AggregatedValueObject retVo = (AggregatedValueObject)o;
      if (retVo.getChildrenVO() == null)
        modelVo.setParentVO(retVo.getParentVO());
      else
        modelVo = retVo;
      getBufferData().setVOAt(getBufferData().getCurrentRow(), modelVo);
      getBufferData().setCurrentRow(getBufferData().getCurrentRow());
    }

    getBillUI().setBillOperate(0);
  }

  protected void onBoElse(int intBtn)
    throws Exception
  {
  }

  private void onBoLine(ButtonObject bo)
    throws Exception
  {
    int intBtn = Integer.parseInt(bo.getTag());

    buttonActionBefore(getBillUI(), intBtn);
    switch (intBtn) {
    case 11:
      getBillUI().showHintMessage(bo.getName());

      onBoLineAdd();

      buttonActionAfter(getBillUI(), intBtn);
      break;
    case 12:
      getBillUI().showHintMessage(bo.getName());

      onBoLineDel();

      buttonActionAfter(getBillUI(), intBtn);
      break;
    case 13:
      getBillUI().showHintMessage(bo.getName());

      onBoLineCopy();

      buttonActionAfter(getBillUI(), intBtn);
      break;
    case 15:
      getBillUI().showHintMessage(bo.getName());

      onBoLineIns();

      buttonActionAfter(getBillUI(), intBtn);
      break;
    case 14:
      getBillUI().showHintMessage(bo.getName());

      onBoLinePaste();

      buttonActionAfter(getBillUI(), intBtn);
    }
  }

  protected void onBoLineAdd()
    throws Exception
  {
    getBillCardPanelWrapper().addLine();

    postProcessOfAddNewLine();
  }

  protected void postProcessOfAddNewLine()
  {
    try
    {
      CircularlyAccessibleValueObject vo = processNewBodyVO(getBillCardPanelWrapper().getSelectedBodyVOs()[0]);

      int row = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();

      if (row == -1) {
        row = getBillCardPanelWrapper().getBillCardPanel().getBillModel().getRowCount() - 1;
      }
      if (row < 0)
        throw new RuntimeException("cann't get selected row");
      if (vo != null)
        getBillCardPanelWrapper().getBillCardPanel().getBillModel().setBodyRowVO(vo, row);
    }
    catch (NullPointerException e) {
      System.out.println("错误：增行或删行后没有获取到被选择的VO");
      e.printStackTrace();
    } catch (ArrayIndexOutOfBoundsException e) {
      System.out.println("错误：增行或删行后没有获取到被选择的VO");
      e.printStackTrace();
    }
  }

  protected void onBoLineCopy()
    throws Exception
  {
    getBillCardPanelWrapper().copySelectedLines();
  }

  protected void onBoLineDel()
    throws Exception
  {
    getBillCardPanelWrapper().deleteSelectedLines();
  }

  protected void onBoLineIns()
    throws Exception
  {
    getBillCardPanelWrapper().insertLine();
    if (getBillCardPanelWrapper().getBillCardPanel().getRowCount() > 0)
      postProcessOfAddNewLine();
  }

  protected CircularlyAccessibleValueObject processNewBodyVO(CircularlyAccessibleValueObject newBodyVO)
  {
    return newBodyVO;
  }

  protected void onBoLinePaste()
    throws Exception
  {
    processCopyedBodyVOsBeforePaste(getBillCardPanelWrapper().getCopyedBodyVOs());

    getBillCardPanelWrapper().pasteLines();
  }

  protected void onBoPrint()
    throws Exception
  {
    IDataSource dataSource = new CardPanelPRTS(getBillUI()._getModuleCode(), getBillCardPanelWrapper().getBillCardPanel());

    PrintEntry print = new PrintEntry(null, dataSource);

    print.setTemplateID(getBillUI()._getCorp().getPrimaryKey(), getBillUI()._getModuleCode(), getBillUI()._getOperator(), getBillUI().getBusinessType(), getBillUI().getNodeKey());

    if (print.selectTemplate() == 1)
      print.preview();
  }

  protected void onBoDirectPrint()
    throws Exception
  {
    BillModel billmodel = getBillCardPanelWrapper().getBillCardPanel().getBillModel();

    if (billmodel == null)
      return;
    if ((billmodel instanceof ReportTreeTableModelAdapter)) {
      ((ReportTreeTableModelAdapter)billmodel).setPrinting(true);
    }

    BillDirectPrint print = new BillDirectPrint(getBillCardPanelWrapper().getBillCardPanel(), getBillCardPanelWrapper().getBillCardPanel().getTitle());

    print.onPrint();

    if ((billmodel instanceof ReportTreeTableModelAdapter))
      ((ReportTreeTableModelAdapter)billmodel).setPrinting(false);
  }

  protected void onBoQuery()
    throws Exception
  {
    StringBuffer strWhere = new StringBuffer();

    if (!askForQueryCondition(strWhere)) {
      return;
    }
    SuperVO[] queryVos = queryHeadVOs(strWhere.toString());

    getBufferData().clear();

    addDataToBuffer(queryVos);

    updateBuffer();
  }

  protected SuperVO[] queryHeadVOs(String strWhere)
    throws Exception, ClassNotFoundException
  {
    SuperVO[] queryVos = getBusiDelegator().queryHeadAllData(Class.forName(getUIController().getBillVoName()[1]), getUIController().getBillType(), strWhere.toString());

    return queryVos;
  }

  protected boolean askForQueryCondition(StringBuffer sqlWhereBuf)
    throws Exception
  {
    if (sqlWhereBuf == null) {
      throw new IllegalArgumentException("askForQueryCondition().sqlWhereBuf cann't be null");
    }
    UIDialog querydialog = getQueryUI();

    if (querydialog.showModal() != 1)
      return false;
    INormalQuery query = (INormalQuery)querydialog;

    String strWhere = query.getWhereSql();
    if (strWhere == null) {
      strWhere = "1=1";
    }
    if (getButtonManager().getButton(2) != null) {
      if (getBillIsUseBusiCode().booleanValue())
      {
        strWhere = "(" + strWhere + ") and " + getBillField().getField_BusiCode() + "='" + getBillUI().getBusicode() + "'";
      }
      else
      {
        strWhere = "(" + strWhere + ") and " + getBillField().getField_Busitype() + "='" + getBillUI().getBusinessType() + "'";
      }

    }

    strWhere = "(" + strWhere + ") and (isnull(dr,0)=0)";

    if (getHeadCondition() != null) {
      strWhere = strWhere + " and " + getHeadCondition();
    }
    sqlWhereBuf.append(strWhere);
    return true;
  }

  protected final void updateBuffer()
    throws Exception
  {
    if (getBufferData().getVOBufferSize() != 0)
    {
      getBillUI().setListHeadData(getBufferData().getAllHeadVOsFromBuffer());

      getBillUI().setBillOperate(2);
      getBufferData().setCurrentRow(0);
    } else {
      getBillUI().setListHeadData(null);
      getBillUI().setBillOperate(4);
      getBufferData().setCurrentRow(-1);
      getBillUI().showHintMessage(NCLangRes.getInstance().getStrByID("uifactory", "UPPuifactory-000066"));
    }
  }

  protected void addDataToBuffer(SuperVO[] queryVos)
    throws Exception
  {
    if (queryVos == null) {
      getBufferData().clear();
      return;
    }
    for (int i = 0; i < queryVos.length; i++) {
      AggregatedValueObject aVo = (AggregatedValueObject)Class.forName(getUIController().getBillVoName()[0]).newInstance();

      aVo.setParentVO(queryVos[i]);
      getBufferData().addVOToBuffer(aVo);
    }
  }

  protected void onBoRefresh()
    throws Exception
  {
    try
    {
      getBufferData().refresh();
    }
    catch (RecordNotFoundExcetption e) {
      if (getBillUI().showYesNoMessage(NCLangRes.getInstance().getStrByID("uifactory", "UPPuifactory-000185")) != 4)
      {
        getBufferData().removeCurrentRow();
      }
    }
  }

  protected void onBoReturn()
    throws Exception
  {
  }

  protected void onBoSave()
    throws Exception
  {
    AggregatedValueObject billVO = getBillUI().getChangedVOFromUI();
    setTSFormBufferToVO(billVO);
    AggregatedValueObject checkVO = getBillUI().getVOFromUI();
    setTSFormBufferToVO(checkVO);

    Object o = null;
    ISingleController sCtrl = null;
    if ((getUIController() instanceof ISingleController)) {
      sCtrl = (ISingleController)getUIController();
      if (sCtrl.isSingleDetail()) {
        o = billVO.getParentVO();
        billVO.setParentVO(null);
      } else {
        o = billVO.getChildrenVO();
        billVO.setChildrenVO(null);
      }
    }

    boolean isSave = true;

    if ((billVO.getParentVO() == null) && ((billVO.getChildrenVO() == null) || (billVO.getChildrenVO().length == 0)))
    {
      isSave = false;
    }
    else if (getBillUI().isSaveAndCommitTogether()) {
      billVO = getBusinessAction().saveAndCommit(billVO, getUIController().getBillType(), _getDate().toString(), getBillUI().getUserObject(), checkVO);
    }
    else
    {
      billVO = getBusinessAction().save(billVO, getUIController().getBillType(), _getDate().toString(), getBillUI().getUserObject(), checkVO);
    }

    if ((sCtrl != null) && 
      (sCtrl.isSingleDetail())) {
      billVO.setParentVO((CircularlyAccessibleValueObject)o);
    }
    int nCurrentRow = -1;
    if (isSave) {
      if (isEditing()) {
        if (getBufferData().isVOBufferEmpty()) {
          getBufferData().addVOToBuffer(billVO);
          nCurrentRow = 0;
        }
        else {
          getBufferData().setCurrentVO(billVO);
          nCurrentRow = getBufferData().getCurrentRow();
        }
      }

      setAddNewOperate(isAdding(), billVO);
    }

    setSaveOperateState();
    if (nCurrentRow >= 0)
      getBufferData().setCurrentRow(nCurrentRow);
  }

  protected void onBusinessException(BusinessException e)
  {
    MessageDialog.showHintDlg(getBillUI(), null, e.getMessage());
    Logger.error(e.getMessage(), e);
  }

  public void onButton(ButtonObject bo)
  {
    if ((getBillUI().getBillOperate() == 1) || (getBillUI().getBillOperate() == 0))
    {
      if (getBillCardPanelWrapper() != null)
        getBillCardPanelWrapper().getBillCardPanel().stopEditing();
    }
    try
    {
      ButtonObject parentBtn = bo.getParent();

      if ((parentBtn != null) && (Integer.parseInt(parentBtn.getTag()) < 100)) {
        int intParentBtn = Integer.parseInt(parentBtn.getTag());
        complexOnButton(intParentBtn, bo);
      } else {
        if (bo.getTag() == null)
          System.out.println("新增按钮必须设置TAG,TAG>100的整数.....");
        int intBtn = Integer.parseInt(bo.getTag());
        if (intBtn > 100) {
          onBoElse(intBtn);
        }
        else
          simpleOnButton(intBtn, bo);
      }
    } catch (BusinessException ex) {
      onBusinessException(ex);
    } catch (SQLException ex) {
      getBillUI().showErrorMessage(ex.getMessage());
    } catch (Exception e) {
      getBillUI().showErrorMessage(e.getMessage());
      e.printStackTrace();
    }
  }

  protected AggregatedValueObject[] onSplitBillVos(AggregatedValueObject[] refVos)
    throws Exception
  {
    return null;
  }

  protected void processCopyedBodyVOsBeforePaste(CircularlyAccessibleValueObject[] vos)
  {
    if (vos == null) {
      return;
    }
    for (int i = 0; i < vos.length; i++) {
      vos[i].setAttributeValue(getUIController().getPkField(), null);
      vos[i].setAttributeValue(getUIController().getChildPkField(), null);
    }
  }

  protected AggregatedValueObject refVOChange(AggregatedValueObject[] vos)
    throws Exception
  {
    return vos[0];
  }

  private void setCheckManAndDate(AggregatedValueObject vo) throws Exception
  {
    vo.getParentVO().setAttributeValue(getBillField().getField_CheckDate(), getBillUI()._getDate());

    vo.getParentVO().setAttributeValue(getBillField().getField_CheckMan(), getBillUI()._getOperator());
  }

  private void simpleOnButton(int intBtn, ButtonObject bo)
    throws Exception
  {
    long lngTime = System.currentTimeMillis();
    buttonActionBefore(getBillUI(), intBtn);
    switch (intBtn) {
    case 1:
      if (getBillUI().isBusinessType().booleanValue()) break;
      getBillUI().showHintMessage(NCLangRes.getInstance().getStrByID("uifactory", "UPPuifactory-000061"));

      onBoAdd(bo);

      buttonActionAfter(getBillUI(), intBtn);
      break;
    case 3:
      getBillUI().showHintMessage(NCLangRes.getInstance().getStrByID("uifactory", "UPPuifactory-000067"));

      onBoEdit();

      buttonActionAfter(getBillUI(), intBtn);
      break;
    case 4:
      getBillUI().showHintMessage(NCLangRes.getInstance().getStrByID("uifactory", "UPPuifactory-000068"));

      onBoDel();

      buttonActionAfter(getBillUI(), intBtn);
      getBillUI().showHintMessage(NCLangRes.getInstance().getStrByID("uifactory", "UPPuifactory-000069"));

      break;
    case 32:
      getBillUI().showHintMessage(NCLangRes.getInstance().getStrByID("uifactory", "UPPuifactory-000070"));

      onBoDelete();

      buttonActionAfter(getBillUI(), intBtn);
      getBillUI().showHintMessage(NCLangRes.getInstance().getStrByID("uifactory", "UPPuifactory-000071"));

      break;
    case 5:
      getBillUI().showHintMessage(bo.getName());

      if ((super.getUIController() instanceof ISingleController)) {
        ISingleController strl = (ISingleController)super.getUIController();

        if (strl.isSingleDetail())
          onBoBodyQuery();
        else
          onBoQuery();
      } else {
        onBoQuery();
      }
      buttonActionAfter(getBillUI(), intBtn);

      break;
    case 0:
      getBillUI().showHintMessage(NCLangRes.getInstance().getStrByID("uifactory", "UPPuifactory-000072"));

      onBoSave();

      buttonActionAfter(getBillUI(), intBtn);
      getBillUI().showHintMessage(NCLangRes.getInstance().getStrByID("uifactory", "UPPuifactory-000073"));

      break;
    case 7:
      onBoCancel();

      getBillUI().showHintMessage("");

      buttonActionAfter(getBillUI(), intBtn);
      break;
    case 6:
      getBillUI().showHintMessage(NCLangRes.getInstance().getStrByID("uifactory", "UPPuifactory-000074"));

      onBoPrint();

      buttonActionAfter(getBillUI(), intBtn);
      getBillUI().showHintMessage(NCLangRes.getInstance().getStrByID("uifactory", "UPPuifactory-000075"));

      break;
    case 50:
      getBillUI().showHintMessage(NCLangRes.getInstance().getStrByID("uifactory", "UPPuifactory-000074"));

      onBoDirectPrint();

      buttonActionAfter(getBillUI(), intBtn);
      getBillUI().showHintMessage(NCLangRes.getInstance().getStrByID("uifactory", "UPPuifactory-000075"));

      break;
    case 31:
      onBoReturn();

      buttonActionAfter(getBillUI(), intBtn);
      break;
    case 30:
      onBoCard();

      buttonActionAfter(getBillUI(), intBtn);
      break;
    case 8:
      getBillUI().showHintMessage(NCLangRes.getInstance().getStrByID("uifactory", "UPPuifactory-000076"));

      onBoRefresh();

      buttonActionAfter(getBillUI(), intBtn);
      getBillUI().showHintMessage(NCLangRes.getInstance().getStrByID("uifactory", "UPPuifactory-000077"));

      break;
    case 9:
      getBillUI().showHintMessage(NCLangRes.getInstance().getStrByID("uifactory", "UPPuifactory-000078"));

      onBillRef();

      buttonActionAfter(getBillUI(), intBtn);
      getBillUI().showHintMessage(NCLangRes.getInstance().getStrByID("uifactory", "UPPuifactory-000079"));

      break;
    case 41:
      getBillUI().showHintMessage(NCLangRes.getInstance().getStrByID("uifactory", "UPPuifactory-000080"));

      onBoCopy();

      buttonActionAfter(getBillUI(), intBtn);
      getBillUI().showHintMessage(NCLangRes.getInstance().getStrByID("uifactory", "UPPuifactory-000081"));

      break;
    case 26:
      getBillUI().showHintMessage(NCLangRes.getInstance().getStrByID("uifactory", "UPPuifactory-000179"));

      onBoAudit();
      getBillUI().showHintMessage(NCLangRes.getInstance().getStrByID("uifactory", "UPPuifactory-000180"));

      break;
    case 27:
      getBillUI().showHintMessage(NCLangRes.getInstance().getStrByID("uifactory", "UPPuifactory-000179"));

      onBoCancelAudit();
      getBillUI().showHintMessage(NCLangRes.getInstance().getStrByID("uifactory", "UPPuifactory-000180"));

      break;
    case 28:
      getBillUI().showHintMessage(NCLangRes.getInstance().getStrByID("uifactory", "UPPuifactory-000179"));

      onBoCommit();
      getBillUI().showHintMessage(NCLangRes.getInstance().getStrByID("uifactory", "UPPuifactory-000180"));

      break;
    case 35:
      getBillUI().showHintMessage(NCLangRes.getInstance().getStrByID("uifactory", "UPPuifactory-000179"));

      onBoSelAll();

      getBillUI().showHintMessage(NCLangRes.getInstance().getStrByID("uifactory", "UPPuifactory-000180"));

      break;
    case 36:
      getBillUI().showHintMessage(NCLangRes.getInstance().getStrByID("uifactory", "UPPuifactory-000179"));

      onBoSelNone();
      getBillUI().showHintMessage(NCLangRes.getInstance().getStrByID("uifactory", "UPPuifactory-000180"));

      break;
    case 51:
      getBillUI().showHintMessage(NCLangRes.getInstance().getStrByID("uifactory", "UPPuifactory-000179"));

      onBoImport();
      getBillUI().showHintMessage(NCLangRes.getInstance().getStrByID("uifactory", "UPPuifactory-000180"));

      break;
    case 52:
      getBillUI().showHintMessage(NCLangRes.getInstance().getStrByID("uifactory", "UPPuifactory-000179"));

      onBoExport();
      getBillUI().showHintMessage(NCLangRes.getInstance().getStrByID("uifactory", "UPPuifactory-000180"));

      break;
    case 2:
    case 10:
    case 11:
    case 12:
    case 13:
    case 14:
    case 15:
    case 16:
    case 17:
    case 18:
    case 19:
    case 20:
    case 21:
    case 22:
    case 23:
    case 24:
    case 25:
    case 29:
    case 33:
    case 34:
    case 37:
    case 38:
    case 39:
    case 40:
    case 42:
    case 43:
    case 44:
    case 45:
    case 46:
    case 47:
    case 48:
    case 49:
    default:
      getBillUI().showHintMessage(NCLangRes.getInstance().getStrByID("uifactory", "UPPuifactory-000179"));

      onBoActionElse(bo);

      getBillUI().showHintMessage(NCLangRes.getInstance().getStrByID("uifactory", "UPPuifactory-000180"));
    }
  }

  protected void onBoExport()
    throws Exception
  {
  }

  protected void onBoImport()
    throws Exception
  {
  }

  protected void addBillBusiListener(IBillBusiListener bbl)
  {
    this.m_bbl = bbl;
  }

  protected boolean checkVOStatus(AggregatedValueObject vo, int[] intStatus)
    throws Exception
  {
    if ((vo == null) || (vo.getParentVO() == null))
      return true;
    Integer intState = (Integer)vo.getParentVO().getAttributeValue(getBillField().getField_BillStatus());

    if (intState == null) {
      throw new BusinessException(NCLangRes.getInstance().getStrByID("uifactory", "UPPuifactory-000082"));
    }

    int intCurrentState = intState.intValue();
    for (int i = 0; i < intStatus.length; i++) {
      if (intStatus[i] == intCurrentState)
        return true;
    }
    return false;
  }

  protected IBusinessSplit createBusinessSplit()
  {
    return new DefaultBusinessSplit();
  }

  public UFBoolean getBillIsUseBusiCode()
  {
    return new UFBoolean(false);
  }

  private CircularlyAccessibleValueObject[] getChildVO(AggregatedValueObject retVo)
  {
    CircularlyAccessibleValueObject[] childVos = null;
    if ((retVo instanceof IExAggVO))
      childVos = ((IExAggVO)retVo).getAllChildrenVO();
    else
      childVos = retVo.getChildrenVO();
    return childVos;
  }

  protected String getHeadCondition()
  {
    if ((getBillCardPanelWrapper() != null) && 
      (getBillCardPanelWrapper().getBillCardPanel().getHeadTailItem(getBillField().getField_Corp()) != null))
    {
      return getBillField().getField_Corp() + "='" + getBillUI()._getCorp().getPrimaryKey() + "'";
    }
    return null;
  }

  public final void initNodeKeyButton()
  {
    try
    {
      ButtonObject boNodeKey = getButtonManager().getButton(33);

      if (boNodeKey != null)
        if ((boNodeKey.getChildButtonGroup() != null) && (boNodeKey.getChildButtonGroup().length > 0))
        {
          ButtonObject bo = boNodeKey.getChildButtonGroup()[0];
          bo.setSelected(true);

          getBillUI().setNodeKey(bo.getTag());
        } else {
          System.out.println("没有初始化NodeKey类型!");
        }
    } catch (Exception ex) {
      ex.printStackTrace();
      getBillUI().showHintMessage(NCLangRes.getInstance().getStrByID("uifactory", "UPPuifactory-000083"));
    }
  }

  protected boolean isDataChange()
  {
    return true;
  }

  protected void onBillRef(int intBtn, String refBilltype)
    throws Exception
  {
    ButtonObject btn = getButtonManager().getButton(intBtn);
    btn.setTag(refBilltype + ":");
    onBoBusiTypeAdd(btn, null);
  }

  private final void onBoNodekey(ButtonObject bo)
    throws Exception
  {
    bo.setSelected(true);

    getBillUI().setNodeKey(bo.getTag());

    getBillUI().initUI();

    setQueryUI(null);

    getBillUI().getBufferData().clear();
    getBillUI().getBufferData().setCurrentRow(-1);

    getBillUI().updateButtonUI();
  }

  protected void onBoSelAll()
    throws Exception
  {
  }

  protected void onBoSelNone()
    throws Exception
  {
  }

  protected void removeBillBusiListener()
  {
    this.m_bbl = null;
  }

  protected void setAddNewOperate(boolean isAdding, AggregatedValueObject billVO)
    throws Exception
  {
    if (isAdding) {
      getBufferData().addVOsToBuffer(new AggregatedValueObject[] { billVO });

      getBufferData().setCurrentRow(getBufferData().getVOBufferSize() - 1);
    }
  }

  protected void setRefData(AggregatedValueObject[] vos)
    throws Exception
  {
    getBillUI().setCardUIState();

    AggregatedValueObject vo = refVOChange(vos);
    if (vo == null) {
      throw new BusinessException(NCLangRes.getInstance().getStrByID("uifactory", "UPPuifactory-000084"));
    }

    getBillUI().setBillOperate(3);

    getBillCardPanelWrapper().setCardData(vo);
  }

  protected void setSaveOperateState()
    throws Exception
  {
    getBillUI().setBillOperate(2);
  }

  private void setTSFormBufferToVO(AggregatedValueObject setVo)
    throws Exception
  {
    if (setVo == null)
      return;
    AggregatedValueObject vo = getBufferData().getCurrentVO();
    if (vo == null)
      return;
    if (getBillUI().getBillOperate() == 0)
    {
      if ((vo.getParentVO() != null) && (setVo.getParentVO() != null)) {
        setVo.getParentVO().setAttributeValue("ts", vo.getParentVO().getAttributeValue("ts"));
      }

      SuperVO[] changedvos = (SuperVO[])(SuperVO[])getChildVO(setVo);

      if ((changedvos != null) && (changedvos.length != 0))
      {
        HashMap bufferedVOMap = null;

        SuperVO[] bufferedVOs = (SuperVO[])(SuperVO[])getChildVO(vo);
        if ((bufferedVOs != null) && (bufferedVOs.length != 0)) {
          bufferedVOMap = Hashlize.hashlizeObjects(bufferedVOs, new VOHashPrimaryKeyAdapter());

          for (int i = 0; i < changedvos.length; i++)
            if (changedvos[i].getPrimaryKey() != null) {
              ArrayList bufferedAl = (ArrayList)bufferedVOMap.get(changedvos[i].getPrimaryKey());

              if (bufferedAl != null) {
                SuperVO bufferedVO = (SuperVO)bufferedAl.get(0);

                changedvos[i].setAttributeValue("ts", bufferedVO.getAttributeValue("ts"));
              }
            }
        }
      }
    }
  }
}