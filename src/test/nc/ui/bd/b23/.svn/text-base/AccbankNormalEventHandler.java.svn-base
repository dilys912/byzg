package nc.ui.bd.b23;

import java.util.HashMap;
import javax.swing.ListSelectionModel;
import nc.bs.logging.Logger;
import nc.bs.uap.bd.BDException;
import nc.ui.bd.b23.netbank.INetBank;
import nc.ui.bd.b23.netbank.NetbankBuilder;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.para.SysInitBO_Client;
import nc.ui.pub.print.IDataSource;
import nc.ui.pub.print.PrintEntry;
import nc.ui.trade.base.AbstractBillUI;
import nc.ui.trade.bill.BillCardPanelWrapper;
import nc.ui.trade.bill.BillListPanelWrapper;
import nc.ui.trade.buffer.BillUIBuffer;
import nc.ui.trade.buffer.RecordNotFoundExcetption;
import nc.ui.trade.businessaction.IBusinessController;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.trade.pub.SingleListHeadPRTS;
import nc.vo.bd.BDMsg;
import nc.vo.bd.CorpVO;
import nc.vo.bd.b23.AccbankVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.trade.checkrule.VOChecker;

public class AccbankNormalEventHandler extends ManageEventHandler
  implements IAccbankBeforeEventHandler
{
  UFBoolean m_isCorpCanAdd = null;
  private NetbankBuilder m_netbankBuilder;
  HashMap netbankHash = new HashMap();

  public AccbankNormalEventHandler(BillManageUI billUI, IControllerBase control)
  {
    super(billUI, control);
  }

  protected void afterEditOperation(AccbankVO currVO) throws BusinessException
  {
  }

  private AccbankVO checkNoDataOperate() throws BusinessException {
    AggregatedValueObject aggVO = getBufferData().getCurrentVO();

    if ((aggVO == null) || (aggVO.getParentVO() == null) || (aggVO.getParentVO().getPrimaryKey() == null)) {
      throw new BDException(BDMsg.MSG_CHOOSE_DATA());
    }
    return (AccbankVO)aggVO.getParentVO();
  }

  protected void checkOtherCorpEdit(AccbankVO currVO) throws BusinessException {
    if (!_getCorp().getPrimaryKey().equals(currVO.getPk_corp()))
      throw new BDException(NCLangRes.getInstance().getStrByID("10081602", "UPP10081602-000065"));
  }

  private void clearNetBankFlag()
  {
    setNetBankItemNullable("unitname", false);
    setNetBankItemNullable("areacode", false);
    setNetBankItemNullable("combineaccnum", false);
    setNetBankItemNullable("orgnumber", false);
    setNetBankItemNullable("bankarea", false);
    setNetBankItemNullable("province", false);
    setNetBankItemNullable("city", false);
    setNetBankItemNullable("abcarea", false);
    setNetBankItemNullable("groupid", false);
  }

  protected IBusinessController createBusinessAction() {
    return new AccbankBusinessAction(getBillUI(), false);
  }

  protected INetBank getCurrentNetbank()
  {
    String banktype = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("banktype").getValue();
    if ((banktype != null) && (banktype.trim().length() > 0)) {
      int banktypeflag = new Integer(banktype).intValue();
      return getNetbankFromHash(banktypeflag);
    }
    return null;
  }

  protected String getHeadCondition() {
    return null;
  }

  private NetbankBuilder getNetbankBuilder() {
    if (this.m_netbankBuilder == null) {
      this.m_netbankBuilder = new NetbankBuilder();
    }
    return this.m_netbankBuilder;
  }

  private INetBank getNetbankFromHash(int flag)
  {
    Integer IntFlag = new Integer(flag);
    INetBank netbank = null;
    if (this.netbankHash.containsKey(IntFlag)) {
      netbank = (INetBank)this.netbankHash.get(IntFlag);
    } else {
      netbank = getNetbankBuilder().createNetbank(flag);
      this.netbankHash.put(IntFlag, netbank);
    }
    return netbank;
  }

  protected UIDialog getQueryUI()
    throws Exception
  {
    return new AccbankQueryDlg(getBillUI(), null);
  }

  public void onBoAdd(ButtonObject bo) throws Exception {
    if (!isCorpCanAdd()) {
      throw new BDException(NCLangRes.getInstance().getStrByID("10081602", "UPP10081602-000077"));
    }
    super.onBoAdd(bo);
    getBillCardPanelWrapper().getBillCardPanel().getHeadItem("sealflag").setEnabled(false);
    UIChanged(null, false);
  }

  protected void onBoCancel() throws Exception {
    super.onBoCancel();
    clearNetBankFlag();
    getBillCardPanelWrapper().getBillCardPanel().execHeadLoadFormulas();
  }

  protected void onBoCard() throws Exception {
    ((BillManageUI)getBillUI()).setCurrentPanel("CARDPANEL");
    getBufferData().updateView();
    getBillCardPanelWrapper().getBillCardPanel().execHeadLoadFormulas();
  }

  protected void onBoDelete()
    throws Exception
  {
    AggregatedValueObject aggVO = getBufferData().getCurrentVO();
    AccbankVO currVO = aggVO == null ? null : (AccbankVO)aggVO.getParentVO();
    if (!_getCorp().getPrimaryKey().equals(currVO.getPk_corp())) {
      throw new Exception(NCLangRes.getInstance().getStrByID("10081602", "UPP10081602-000065"));
    }
    super.onBoDelete();
  }

  protected void onBoEdit() throws Exception
  {
    AccbankVO currVO = checkNoDataOperate();

    checkOtherCorpEdit(currVO);

    super.onBoEdit();

    afterEditOperation(currVO);

    UIChanged(null, false);
  }

  protected void onBoElse(int intBtn) throws Exception
  {
    switch (intBtn) {
    case 111:
      onBoFilterAll();
      break;
    case 112:
      onBoFilterOwn();
      break;
    case 113:
      onBoFilterTrade();
    }
  }

  protected void onBoFilterAll() throws BusinessException
  {
    String sql = "(bd_accbank.pk_corp ='" + _getCorp().getPrimaryKey() + "' or bd_accbank.pk_corp = '0001') ";
    sql = sql + " and bd_accbank.netbankflag <> 3 ";
    refreshDateAndUIByCondition(sql);
  }

  protected void onBoFilterOwn() throws BusinessException {
    String sql = "(bd_accbank.pk_corp ='" + _getCorp().getPrimaryKey() + "' or bd_accbank.pk_corp = '0001') ";
    sql = sql + " and bd_accbank.netbankflag <> 3 ";
    sql = sql + "and netbankflag = 1 ";
    refreshDateAndUIByCondition(sql);
  }

  protected void onBoFilterTrade() throws BusinessException {
    String sql = "(bd_accbank.pk_corp ='" + _getCorp().getPrimaryKey() + "' or bd_accbank.pk_corp = '0001') ";

    sql = sql + "and netbankflag = 2 ";
    refreshDateAndUIByCondition(sql);
  }

  protected void onBoPrint() throws Exception {
    IDataSource dataSource = new SingleListHeadPRTS(getBillUI()._getModuleCode(), ((BillManageUI)getBillUI()).getBillListPanel());

    PrintEntry print = new PrintEntry(null, dataSource);

    print.setTemplateID(getBillUI()._getCorp().getPrimaryKey(), getUIController().getBillType(), getBillUI()._getOperator(), getBillUI().getBusinessType(), getBillUI().getNodeKey());

    if (print.selectTemplate() == 1)
      print.preview();
  }

  protected void onBoRefresh()
    throws Exception
  {
    int count_before_refresh = getBufferData().getVOBufferSize();
    if (!getBufferData().isVOBufferEmpty()) {
      AggregatedValueObject aVo = getBufferData().getCurrentVO();
      if (aVo == null)
        return;
      String id = null;
      if (aVo.getParentVO() != null)
        id = aVo.getParentVO().getPrimaryKey();
      if (id != null)
      {
        SuperVO parentVo = ((AccbankBusinessAction)getBusinessAction()).findAccbankByPk(id, _getCorp().getPrimaryKey());

        if ((parentVo == null) || ((parentVo.getAttributeValue("dr") != null) && (!new Integer(0).equals(parentVo.getAttributeValue("dr")))))
          throw new RecordNotFoundExcetption();
        aVo.setParentVO(parentVo);
      }
      aVo.setChildrenVO(null);

      getBufferData().setVOAt(getBufferData().getCurrentRow(), aVo);

      getBufferData().setCurrentRow(getBufferData().getCurrentRow());
    }
    int count_after_refresh = getBufferData().getVOBufferSize();
    if (count_before_refresh != count_after_refresh)
    {
      if (count_after_refresh == 0)
      {
        getBillUI().setListHeadData(null);
      }
      else
      {
        getBillUI().setListHeadData(getBufferData().getAllHeadVOsFromBuffer());
      }

    }
    else if (getBufferData().getCurrentVO() != null)
      ((BillManageUI)getBillUI()).getBillListWrapper().updateListVo(getBufferData().getCurrentVO().getParentVO(), getBufferData().getCurrentRow());
  }

  protected void onBoSave()
    throws Exception
  {
    BillItem item = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("netbankflag");
    int netbankFlag = Integer.parseInt(item.getValue());
    if (netbankFlag == 0) {
      throw new BusinessException(NCLangRes.getInstance().getStrByID("10081602", "UPP10081602-000066"));
    }
    AggregatedValueObject checkVO = getBillUI().getVOFromUI();
    String fixedErrMsg = null;
    String dynamicErrMsg = null;
    if (!VOChecker.check(checkVO, new AccbankNormalUICheck(checkVO))) {
      fixedErrMsg = VOChecker.getErrorMessage();
    }
    if (getCurrentNetbank() != null) {
      dynamicErrMsg = getCurrentNetbank().check((BillManageUI)getBillUI());
      if ((fixedErrMsg != null) && (dynamicErrMsg != null)) {
        fixedErrMsg = fixedErrMsg.substring(0, fixedErrMsg.indexOf(System.getProperty("line.separator")));
        fixedErrMsg = fixedErrMsg.replace('.', ',');
        throw new BDException(fixedErrMsg + dynamicErrMsg);
      }if (dynamicErrMsg != null) {
        throw new BDException(NCLangRes.getInstance().getStrByID("uffactory_hyeaa", "UPPuffactory_hyeaa-000117") + " " + dynamicErrMsg);
      }
    }

    super.onBoSave();

    clearNetBankFlag();
    getBillCardPanelWrapper().getBillCardPanel().execHeadLoadFormulas();
  }

  protected SuperVO[] queryHeadVOs(String strWhere) throws Exception, ClassNotFoundException
  {
    if (strWhere.indexOf("and (isnull(dr,0)=0)") >= 0) {
      int index = strWhere.indexOf("and (isnull(dr,0)=0)");
      strWhere = strWhere.substring(0, index).concat(strWhere.substring(index + 20));
    }
    return ((AccbankBusinessAction)getBusinessAction()).queryAllHeadVOs(strWhere, _getCorp().getPrimaryKey());
  }
  protected void refreshDateAndUIByCondition(String condition) throws BusinessException {
    try {
      SuperVO[] queryVos = queryHeadVOs(condition);
      getBufferData().clear();

      addDataToBuffer(queryVos);
      updateBuffer();
    } catch (Exception e) {
      Logger.error(e.getMessage(), e);
      throw new BDException(e.getMessage());
    }
  }

  private void setNetBankItemNullable(String key, boolean isNullable)
  {
    BillItem item = getBillCardPanelWrapper().getBillCardPanel().getHeadItem(key);
    if (isNullable) {
      if (!key.equals("groupid")) item.setNull(true);
    }
    else
      item.setNull(false);
  }

  public void UIChanged(BillEditEvent e, boolean isTriggedByEvent)
    throws Exception
  {
    if (getCurrentNetbank() != null) {
      getCurrentNetbank().afterEdit((BillManageUI)getBillUI(), e);
    }

    if ((isTriggedByEvent) && (e.getKey().equals("banktype")));
  }

  protected void onBoQuery()
    throws Exception
  {
    super.onBoQuery();
    if (getBufferData().getVOBufferSize() > 0)
      ((BillManageUI)getBillUI()).getBillListPanel().getHeadTable().getSelectionModel().setSelectionInterval(0, 0);
  }

  protected boolean isCorpCanAdd()
  {
    if ("0001".equalsIgnoreCase(_getCorp().getPrimaryKey())) {
      return true;
    }
    if (this.m_isCorpCanAdd == null)
    {
      try
      {
        this.m_isCorpCanAdd = SysInitBO_Client.getParaBoolean("0001", "BD006");
      }
      catch (BusinessException e) {
        Logger.error(e.getMessage(), e);
        MessageDialog.showErrorDlg(getBillUI(), null, e.getMessage());
      } catch (Exception e) {
        Logger.error(e.getMessage(), e);
      }
    }

    return this.m_isCorpCanAdd.booleanValue();
  }
}