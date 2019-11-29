package nc.ui.fa.uifactory.manage;

import java.util.Vector;
import javax.swing.ListSelectionModel;
import nc.pub.fa.application.platform.manager.IFANodeManager;
import nc.pub.fi.framework.common.command.ICommand;
import nc.pub.fi.framework.common.manager.ICommandManager;
import nc.pub.fi.framework.common.manager.ILangManager;
import nc.pub.fi.framework.common.util.MessageUtils;
import nc.pub.fi.framework.platform.manager.INodeManager;
import nc.pub.fi.framework.platform.manager.IPlatformManager;
import nc.ui.fa.application.common.command.CheckWhenConfirmCommand;
import nc.ui.fa.application.common.command.SelectAccBookCommand;
import nc.ui.fa.application.platform.IFAPlatformUI;
import nc.ui.fa.application.platform.manager.internal.BillStatusConvertManager;
import nc.ui.fa.application.platform.manager.internal.FAPlatformManager;
import nc.ui.fa.uifactory.lock.ILockManager;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.pub.workflownote.FlowStateDlg;
import nc.ui.trade.base.AbstractBillUI;
import nc.ui.trade.bill.BillCardPanelWrapper;
import nc.ui.trade.buffer.BillUIBuffer;
import nc.ui.trade.businessaction.IBusinessController;
import nc.ui.trade.button.ButtonManager;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.bd.CorpVO;
import nc.vo.fa.accperiod.AccperiodVO;
import nc.vo.fa.application.common.exception.ExceptionUtils;
import nc.vo.fa.closebook.FaCloseBook;
import nc.vo.fa.pub.bill.VOTool;
import nc.vo.fa.pub.exp.FABusinessException;
import nc.vo.fa.uifactory.check.ISaveFlag;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.field.IBillField;
import nc.vo.trade.pub.IExAggVO;

public class FAManageEventHandler extends ManageEventHandler
{
  private int currentButton = -1;

  public FAManageEventHandler(BillManageUI billUI, IControllerBase control) {
    super(billUI, control);

    initBaseDefaultData();
  }

  protected void initBaseDefaultData()
  {
    initializeLineButtonStatus();
  }

  public void onBoCancel()
    throws Exception
  {
    super.onBoCancel();

    UIRefPane refPane = getBillManageUI().getCardCodeRefPane();
    if (refPane != null)
      refPane.setRefModel(getBillManageUI().getCardCodeRefModelWhenView());
  }

  protected void onBoEdit()
    throws Exception
  {
    AggregatedValueObject modelVo = getBufferData().getCurrentVOClone();

    if (modelVo != null)
    {
      String fk_accbook = (String)modelVo.getParentVO().getAttributeValue("fk_accbook");

      if (fk_accbook != null) {
        fk_accbook = fk_accbook.trim();
      }
      if ((getBillManageUI().getCurrentFk_accbook() != null) && (!getBillManageUI().getCurrentFk_accbook().equals(fk_accbook)))
      {
        if (!getBillManageUI().getFANodeManager().getBillType().equals("HI")) {
          ExceptionUtils.asFABusinessException(MessageUtils.getFav50Message("UPPfav50-400029"));
        }
      }
    }
    else if (getBillManageUI().getFANodeManager().getBillType() != null)
    {
      ExceptionUtils.asFABusinessException(MessageUtils.getFav50Message("UPPfav50-400030"));
    }

    UIRefPane refPane = getBillManageUI().getCardCodeRefPane();
    if (refPane != null) {
      refPane.setRefModel(getBillManageUI().getCardCodeRefModelWhenEdit());
    }

    super.onBoEdit();
  }

  private void initializeLineButtonStatus()
  {
    ButtonObject button = getButtonManager().getButton(10);

    if ((button != null) && (button.getChildren() != null) && (button.getChildren().size() >= 5)) {
      button.removeChildButton(getButtonManager().getButton(13));
      button.removeChildButton(getButtonManager().getButton(14));
      button.removeChildButton(getButtonManager().getButton(15));
    }
  }

  public ICommandManager getCommandManager() {
    return getPlatform().getCommandManager();
  }

  private ButtonObject getSelectedButtonObject(ButtonObject parentbutton, int pos) {
    Vector v = parentbutton.getChildren();
    if ((v != null) && (v.size() > 0)) {
      return (ButtonObject)v.elementAt(pos);
    }

    return null;
  }

  public void onBoActionElse(ButtonObject bo)
    throws Exception
  {
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
      break;
    default:
      super.onBoActionElse(bo);
    }
  }

  public final void onBoElse(int intBtn)
    throws Exception
  {
    try
    {
      buttonActionBefore(getBillUI(), intBtn);

      if ("313".equals((intBtn + "").substring(0, (intBtn + "").length() - 1)))
        onBoAccbook(intBtn);
      else if (intBtn == 315)
        onBoQueryFlowState();
      else if (getCommandManager().getCommand("" + intBtn) != null) {
        getCommandManager().getCommand("" + intBtn).execute();
      }
      else {
        super.onBoElse(intBtn);
      }

      buttonActionAfter(getBillUI(), intBtn);
    }
    catch (BusinessException ex) {
      onBusinessException(ex);
    } catch (Exception ex) {
      onException(ex, intBtn);
    }
  }

  public void onBoQueryFlowState()
  {
    AggregatedValueObject vo = getBufferData().getCurrentVO();

    if (vo == null) {
      getBillUI().showErrorMessage(MessageUtils.getFav50Message("UPPfav50-400034"));
      return;
    }

    String billId = null;
    try {
      billId = vo.getParentVO().getPrimaryKey();
    }
    catch (BusinessException e) {
      e.printStackTrace();
      getBillUI().showErrorMessage(e.getMessage());
      return;
    }

    new FlowStateDlg(getBillUI(), getBillType(), billId).showModal();
  }

  public final void onBusinessException(BusinessException ex)
  {
    try
    {
      buttonActionAfter(getBillUI(), this.currentButton);

      if (this.currentButton == 3)
      {
        getBillManageUI().getLockManger().unLock(_getOperator());
      }

      super.onBusinessException(ex);
    } catch (Exception e) {
      e.printStackTrace();
      super.onBusinessException(ex);
    }
  }

  protected final void onException(Exception ex, int intBtn)
    throws Exception
  {
    buttonActionAfter(getBillUI(), intBtn);
    throw ex;
  }

  protected void onBoAccbook(int intBtn)
    throws Exception
  {
    ButtonObject button = getButtonManager().getButton(313);

    ButtonObject selectedButton = getSelectedButtonObject(button, Integer.parseInt((intBtn + "").substring((intBtn + "").length() - 1)));

    ICommand command = getCommandManager().getCommand("313");

    String fk_accbook = selectedButton == null ? null : selectedButton.getCode();
    if (command != null) {
      ((SelectAccBookCommand)command).setFk_accbook(fk_accbook);
    }

    if (getCommandManager().getCommand("313") != null) {
      getCommandManager().getCommand("313").execute();
      setBillItemEnabled(fk_accbook);
      return;
    }
  }

  protected void setBillItemEnabled(String fk_accbook)
    throws Exception
  {
  }

  public void refreshAfterChangeAccbook()
  {
    try
    {
      if (getBillManageUI().isListPanelSelected()) {
        getBufferData().clear();
        getBillManageUI().getBillListPanel().getHeadBillModel().clearBodyData();
        getBillManageUI().getBillListPanel().getBodyBillModel().clearBodyData();
      }
      else
      {
        AggregatedValueObject billVO = getBufferData().getCurrentVO();
        if (billVO == null) {
          return;
        }
        CircularlyAccessibleValueObject head = billVO.getParentVO();
        CircularlyAccessibleValueObject[] bodys = billVO.getChildrenVO();
        boolean bHasBody = false;
        for (int i = 0; i < bodys.length; i++) {
          if ((bodys[i].getAttributeValue("fk_accbook") != null) && (bodys[i].getAttributeValue("fk_accbook").equals(getBillManageUI().getCurrentFk_accbook())))
          {
            bHasBody = true;
          }
        }

        if (bHasBody) {
          getBufferData().clear();
          getBufferData().addVOsToBuffer(new AggregatedValueObject[] { billVO });
          getBufferData().setCurrentRow(0);
          getBillManageUI().setCardUIData(billVO);
          getBillManageUI().setListHeadData(new CircularlyAccessibleValueObject[] { head });
          getBillManageUI().getBillListPanel().getHeadTable().getSelectionModel().setSelectionInterval(0, 0);
        }
        else
        {
          getBillManageUI().getBillCardPanel().getBillData().clearViewData();

          getBillManageUI().getBillListPanel().getHeadBillModel().clearBodyData();
          getBillManageUI().getBillListPanel().getBodyBillModel().clearBodyData();

          getBufferData().clear();
        }
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      getBillManageUI().showErrorMessage(ex.getMessage());
    }
  }

  public void onBoAudit() throws Exception {
    try {
      AggregatedValueObject modelVo = getBufferData().getCurrentVOClone();
      if (modelVo == null)
      {
        return;
      }
      if (!checkVOStatus(modelVo, new int[] { 2, 8 })) {
        getBillUI().showWarningMessage(MessageUtils.getFav50Message("UPPfav50-400005"));
      }
      else
      {
        super.onBoAudit();
      }
    } finally { buttonActionAfter(getBillUI(), 26); }

  }

  protected void confirmWhenCheckPass()
  {
    String pk_corp = getPlatform().getPlatformManager().getCorpId();
    String fk_accbook = getBillManageUI().getCurrentFk_accbook();
    if (((FAPlatformManager)getPlatform().getPlatformManager()).isConfirmWhenCheckPass(pk_corp, fk_accbook))
      try {
        CheckWhenConfirmCommand command = new CheckWhenConfirmCommand(getBillManageUI());
        command.execute();
        getCommandManager().getCommand("300").execute();
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
  }

  protected void onBoCancelAudit() throws Exception
  {
    try {
      AggregatedValueObject modelVo = getBufferData().getCurrentVOClone();

      if (modelVo == null) {
        return;
      }
      if (!checkVOStatus(modelVo, new int[] { 2, 1 })) {
        getBillUI().showWarningMessage(MessageUtils.getFav50Message("UPPfav50-400006"));
      }
      else
      {
        setCheckManAndDate(modelVo);

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

          if (intState.intValue() == 0) {
            modelVo.getParentVO().setAttributeValue(getBillField().getField_CheckMan(), null);
            modelVo.getParentVO().setAttributeValue(getBillField().getField_CheckDate(), null);
          }

          getBufferData().setVOAt(getBufferData().getCurrentRow(), modelVo);
          getBufferData().setCurrentRow(getBufferData().getCurrentRow());
        }
      }
    } finally { buttonActionAfter(getBillUI(), 27); }
  }

  private void setCheckManAndDate(AggregatedValueObject vo)
    throws Exception
  {
    vo.getParentVO().setAttributeValue(getBillField().getField_CheckDate(), getBillUI()._getDate());
    vo.getParentVO().setAttributeValue(getBillField().getField_CheckMan(), getBillUI()._getOperator());
  }

  public void onBoAdd(ButtonObject bo) throws Exception
  {
    UIRefPane refPane = getBillManageUI().getCardCodeRefPane();
    if (refPane != null) {
      refPane.setRefModel(getBillManageUI().getCardCodeRefModelWhenEdit());
    }

    super.onBoAdd(bo);
  }

  public void onBoSave() throws Exception
  {
    if ((getBillUI().getUserObject() != null) && ((getBillUI().getUserObject() instanceof ISaveFlag))) {
      ISaveFlag userObject = (ISaveFlag)getBillUI().getUserObject();
      if (getBillManageUI().getBillOperate() == 1)
        userObject.setInsert();
      else if (getBillManageUI().getBillOperate() == 0) {
        userObject.setUpdate();
      }

    }

    UIRefPane refPane = getBillManageUI().getCardCodeRefPane();
    if (refPane != null) {
      refPane.setRefModel(getBillManageUI().getCardCodeRefModelWhenView());
    }

    super.onBoSave();
  }

  protected String[] getFk_cardsWhenSave()
  {
    return null;
  }

  public FABillManageUI getBillManageUI()
  {
    return (FABillManageUI)getBillUI();
  }

  public AbstractBillUI getBillUI2() {
    return super.getBillUI();
  }

  public IFAPlatformUI getPlatform() {
    return getBillManageUI().getPlatform();
  }

  public UIDialog getQueryUI()
    throws Exception
  {
    return super.getQueryUI();
  }

  public void updateBuffer2() throws Exception {
    super.updateBuffer();
  }

  public void addDataToBuffer(SuperVO[] queryVos) throws Exception {
    super.addDataToBuffer(queryVos);
  }

  public BillUIBuffer getBufferData() {
    return super.getBufferData();
  }

  public BillUIBuffer doDigit(BillUIBuffer billUIBuffer) {
    if (billUIBuffer == null) {
      return null;
    }
    for (int i = 0; i < billUIBuffer.getVOBufferSize(); i++) {
      AggregatedValueObject vo = billUIBuffer.getVOByRowNo(i);

      doDigit(vo);
    }

    return billUIBuffer;
  }

  public void doDigit(AggregatedValueObject vo) {
    SuperVO headVO = (SuperVO)vo.getParentVO();

    doDigit(headVO, getBillManageUI().getHeadShowNum());

    SuperVO[] bodyVOs = (SuperVO[])vo.getChildrenVO();

    if ((bodyVOs != null) && (bodyVOs.length > 0))
      for (int i = 0; i < bodyVOs.length; i++)
        doDigit(bodyVOs[i], getBillManageUI().getItemShowNum());
  }

  public void doDigit(SuperVO vo, String[][] strShow)
  {
    if ((strShow == null) || (strShow.length < 2)) {
      return;
    }
    if (strShow[0].length != strShow[1].length) {
      return;
    }

    for (int i = 0; i < strShow[0].length; i++) {
      String attrName = strShow[0][i];
      Integer attrDigit = new Integer(strShow[1][i]);
      Object value = vo.getAttributeValue(attrName);

      UFDouble result = getPlatform().getLangManager().getUFDouble(value, null);

      if (result != null) {
        result = result.setScale(attrDigit.intValue(), 4);
        vo.setAttributeValue(attrName, result);
      }
    }
  }

  public boolean checkVOStatus(AggregatedValueObject vo, int[] intStatus)
    throws Exception
  {
    if ((vo == null) || (vo.getParentVO() == null))
      return true;
    Integer intState = (Integer)vo.getParentVO().getAttributeValue(getBillField().getField_BillStatus());
    if (intState == null) {
      throw new BusinessException(NCLangRes.getInstance().getStrByID("uifactory", "UPPuifactory-000082"));
    }

    int intCurrentState = BillStatusConvertManager.getDefault().convert(intState.intValue());

    for (int i = 0; i < intStatus.length; i++) {
      if (intStatus[i] == intCurrentState)
        return true;
    }
    return false;
  }

  public CircularlyAccessibleValueObject[] getChildVO(AggregatedValueObject retVo)
  {
    CircularlyAccessibleValueObject[] childVos = null;
    if ((retVo instanceof IExAggVO))
      childVos = ((IExAggVO)retVo).getAllChildrenVO();
    else
      childVos = retVo.getChildrenVO();
    return childVos;
  }

  protected void buttonActionBefore(AbstractBillUI billUI, int intBtn)
    throws Exception
  {
    this.currentButton = intBtn;

    AggregatedValueObject billVO = getBillManageUI().getBufferData().getCurrentVO();

    if (intBtn == 0) {
      billVO = getBillManageUI().getVOFromUI();
    }

    if ((intBtn == 0) || (intBtn == 32) || (intBtn == 3) || (intBtn == 26) || (intBtn == 27))
    {
      if (!getBillType().equals("HI")) {
        checkCloseBook(new AggregatedValueObject[] { billVO }, false);
      }

    }
    else if ((intBtn == 300) || (intBtn == 301))
    {
      checkCloseBook(new AggregatedValueObject[] { billVO }, true);
    } else if ((intBtn == 1) && 
      (!"N/A".equals(getCurrentFk_accbook()))) {
      FaCloseBook.isValiable(getCorpId(), getAccperiodVO().getAccyear(), getAccperiodVO().getAccmonth(), false, getCurrentFk_accbook());
    }

    if ((intBtn == 3) || (intBtn == 32) || (intBtn == 26) || (intBtn == 27) || (intBtn == 300) || (intBtn == 301) || (intBtn == 311))
    {
      SuperVO[] bodyVOs = (SuperVO[])billVO.getChildrenVO();
      Vector cardIds = getCardIdsFromBodyVOs(bodyVOs);

      getBillManageUI().getLockManger().lockCardIds(cardIds, _getOperator());
    }
  }

  protected void onBoLineDel()
    throws Exception
  {
    Vector lockedCardIds = getSelectedCardIdsFromBillUI(getBillCardPanelWrapper().getBillCardPanel());

    getBillManageUI().getLockManger().unLockCardIds(lockedCardIds, _getOperator());
    super.onBoLineDel();
  }

  public Vector getSelectedCardIdsFromBillUI(BillCardPanel billCardPanel) {
    Vector result = new Vector();
    if (billCardPanel.getBillTable().getSelectedRow() > -1) {
      int[] rows = billCardPanel.getBillTable().getSelectedRows();

      for (int i = 0; i < rows.length; i++) {
        Object billId = billCardPanel.getBodyValueAt(rows[i], getBillManageUI().getBodyKey("cardid"));
        if ((billId != null) && (!billId.toString().trim().equals(""))) {
          result.add(billId.toString());
        }
      }
    }

    return result;
  }

  private Vector getCardIdsFromBodyVOs(SuperVO[] bodyVOs)
  {
    if ((bodyVOs == null) || (bodyVOs.length == 0)) {
      return null;
    }
    Vector result = new Vector();

    for (int i = 0; i < bodyVOs.length; i++) {
      Object cardId = bodyVOs[i].getAttributeValue(getBillManageUI().getBodyKey("cardid"));
      if (cardId != null) {
        result.add(cardId);
      }
    }

    return result;
  }

  protected void buttonActionAfter(AbstractBillUI billUI, int intBtn)
    throws Exception
  {
    if ((intBtn == 0) || (intBtn == 7) || (intBtn == 32) || (intBtn == 26) || (intBtn == 27) || (intBtn == 300) || (intBtn == 301) || (intBtn == 311))
    {
      getBillManageUI().getLockManger().unLock(_getOperator());
    }
  }

  public void beforeOnBoAction(int intBtn, AggregatedValueObject billVo)
    throws Exception
  {
    super.beforeOnBoAction(intBtn, billVo);
  }

  public void afterOnBoAction(int intBtn, AggregatedValueObject billVo)
    throws Exception
  {
    super.afterOnBoAction(intBtn, billVo);
  }

  public IBusinessController getBusinessAction2()
  {
    return super.getBusinessAction();
  }

  public IControllerBase getUIController2() {
    return super.getUIController();
  }

  public void onBoRefresh() throws Exception {
    super.onBoRefresh();
  }

  public void setEnabled(int i, boolean bl)
  {
    super.getButtonManager().getButton(i).setEnabled(bl);
  }

  protected final void checkCloseBook(AggregatedValueObject[] billVOs, boolean isCheckVoucher)
    throws FABusinessException
  {
    if (getBillManageUI().getCurrentFk_accbook() == null) {
      return;
    }

    String[] fk_cards = (String[])VOTool.getAttributeValues(billVOs, getBillManageUI().getBodyKey("cardid"), false, true, true);

    if (getBillManageUI().getCurrentFk_accbook().equals("N/A")) {
      if (getBillType().equals("HD")) {
        String[] fk_accbooks = (String[])VOTool.getAttributeValues(billVOs, "fk_accbook", false, true, true);

        FaCloseBook.isValiable(getCorpId(), getLoginDate().toString(), fk_accbooks);
      } else {
        if (fk_cards == null) {
          throw new FABusinessException("表体未包含任何卡片!");
        }
        FaCloseBook.isValiable(fk_cards, getCorpId(), getLoginDate().toString(), isCheckVoucher);
      }
    }
    else FaCloseBook.isValiable(getCorpId(), getAccperiodVO().getAccyear(), getAccperiodVO().getAccmonth(), isCheckVoucher, getCurrentFk_accbook());
  }

  protected String getLoginDate()
  {
    return _getDate().toString();
  }

  protected String getCorpId() {
    return _getCorp().getPk_corp();
  }

  protected String getUserId() {
    return _getOperator();
  }

  protected AccperiodVO getAccperiodVO() {
    return getBillManageUI().getAccperiodVO();
  }

  protected String getCurrentFk_accbook() {
    return getBillManageUI().getCurrentFk_accbook();
  }

  protected String getBillType() {
    INodeManager nodeManager = getBillManageUI().getFANodeManager();
    if ((nodeManager instanceof IFANodeManager)) {
      return ((IFANodeManager)nodeManager).getBillType();
    }

    throw new IllegalArgumentException("实现UI工厂模式的单据需要提供单据类型");
  }
}