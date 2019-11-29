package nc.ui.dm.pub;

import java.util.ArrayList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.TableColumnModel;
import nc.ui.dm.pub.cardpanel.DMBillCardPanel;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.vo.bd.def.DefVO;

public class ClientUIforPlan extends DMToftPanel
{
  protected DMBillCardPanel ivjSecBillCardPanel = null;
  protected DMBillCardPanel ivjThdBillCardPanel = null;
  private String m_SecBillTypeCode;
  private String m_ThdBillTypeCode;
  protected ButtonObject[] arySourceButtonGroup;
  protected ButtonObject boSwitchForm;
  protected ButtonObject boSwitchList;
  protected ButtonObject boSwitchSource;
  protected String strTitle1;
  protected String strTitle2;
  protected String strTitle3;
  protected String[] m_itemSourceANumKeys;
  protected String m_itemSourceFactorKey;
  protected String[] m_itemSourceMoneyKey;
  protected String[] m_itemSourceNumKeys;
  protected String[] m_itemSourcePriceKeys;

  protected BillData changeSingleTalbeBillDataByUserDef(BillData oldBillData, boolean isHead)
  {
    DefVO[] defs = new DefVO[20];
    DefVO[] defsHead = getDefVOsH();
    DefVO[] defsBody = getDefVOsB();

    if (defsHead != null) {
      for (int i = 0; i < defsHead.length; i++) {
        defs[i] = defsHead[i];
      }

    }

    if (defsBody != null) {
      for (int i = 0; i < defsBody.length; i++) {
        defs[(i + 10)] = defsBody[i];
      }
    }

    updateItemByDef(oldBillData, defs, "vuserdef", isHead, 0);

    return oldBillData;
  }

  public void columnSelectionChanged(ListSelectionEvent e)
  {
    super.columnSelectionChanged(e);

    if (getShowState().equals(DMBillStatus.Card)) {
      int colnow = getSecBillCardPanel().getBillTable().getSelectedColumn();
      int rownow = getSecBillCardPanel().getBillTable().getSelectedRow();
      if ((colnow >= 0) && (rownow >= 0)) {
        initRef(rownow, colnow, getSecBillCardPanel());
        String key = getSecBillCardPanel().getBodyShowItems()[colnow].getKey();
        Object value = getSecBillCardPanel().getBodyValueAt(rownow, key);
        whenEntered(rownow, colnow, key, value, getSecBillCardPanel());
      }
    } else if (getShowState().equals(DMBillStatus.List)) {
      int colnow = getBillCardPanel().getBillTable().getSelectedColumn();
      int rownow = getBillCardPanel().getBillTable().getSelectedRow();
      if ((colnow >= 0) && (rownow >= 0)) {
        String key = getBillCardPanel().getBodyShowItems()[colnow].getKey();
        Object value = getBillCardPanel().getBodyValueAt(rownow, key);
        whenEntered(rownow, colnow, key, value, getBillCardPanel());
      }
    }
  }

  public ButtonObject[] getBillButtons()
  {
    if (this.m_strShowState.equals(DMBillStatus.Source)) {
      return this.arySourceButtonGroup;
    }
    return super.getBillButtons();
  }

  public DMBillCardPanel getSecBillCardPanel()
  {
    if (this.ivjSecBillCardPanel == null) {
      try {
        this.ivjSecBillCardPanel = new DMBillCardPanel();
        this.ivjSecBillCardPanel.setName("BillSecCardPanel");

        this.ivjSecBillCardPanel.setBillType(getSecBillType());

        this.ivjSecBillCardPanel.setCorp(getCorpPrimaryKey());

        this.ivjSecBillCardPanel.setOperator(getOperator());

        this.ivjSecBillCardPanel.setNodeCode(getNodeCode());

        this.ivjSecBillCardPanel.addEditListener(this);
        this.ivjSecBillCardPanel.addBodyEditListener2(this);
        this.ivjSecBillCardPanel.addBodyMenuListener(this);
        this.ivjSecBillCardPanel.addAfterEditListener(this);

        this.ivjSecBillCardPanel.setTatolRowShow(true);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjSecBillCardPanel;
  }

  public String getSecBillType()
  {
    return this.m_SecBillTypeCode;
  }

  public DMBillCardPanel getThdBillCardPanel()
  {
    if (this.ivjThdBillCardPanel == null) {
      try {
        this.ivjThdBillCardPanel = new DMBillCardPanel();
        this.ivjThdBillCardPanel.setName("BillThdCardPanel");

        this.ivjThdBillCardPanel.setBillType(getThdBillType());

        this.ivjThdBillCardPanel.setCorp(getCorpPrimaryKey());

        this.ivjThdBillCardPanel.setOperator(getOperator());

        this.ivjThdBillCardPanel.addEditListener(this);
        this.ivjThdBillCardPanel.addBodyEditListener2(this);
        this.ivjThdBillCardPanel.addBodyMenuListener(this);
        this.ivjThdBillCardPanel.addAfterEditListener(this);
        this.ivjThdBillCardPanel.setAutoAddLimitLine(false);
        this.ivjThdBillCardPanel.setAutoAddEditLine(false);

        this.ivjThdBillCardPanel.setTatolRowShow(true);

        this.ivjThdBillCardPanel.getBillTable().getSelectionModel().setSelectionMode(2);
        this.ivjThdBillCardPanel.getBillTable().setRowSelectionAllowed(true);
        this.ivjThdBillCardPanel.getBillTable().setColumnSelectionAllowed(false);
        this.ivjThdBillCardPanel.getBillTable().setCellSelectionEnabled(false);

        this.ivjThdBillCardPanel.getBillTable().setSelectionMode(1);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjThdBillCardPanel;
  }

  public String getThdBillType()
  {
    return this.m_ThdBillTypeCode;
  }

  protected void initBodyComboBox()
  {
  }

  protected void initBodyComboBox(BillCardPanel bcp)
  {
    if (null != bcp.getBodyItem("iplanstatus")) {
      UIComboBox iplanstatus = (UIComboBox)bcp.getBodyItem("iplanstatus").getComponent();
      bcp.getBodyItem("iplanstatus").setWithIndex(true);
      iplanstatus.addItem(NCLangRes.getInstance().getStrByID("4014", "UPP4014-000056"));
      iplanstatus.addItem(NCLangRes.getInstance().getStrByID("common", "UC000-0001558"));
      iplanstatus.addItem(NCLangRes.getInstance().getStrByID("4014", "UPP4014-000057"));
      iplanstatus.addItem(NCLangRes.getInstance().getStrByID("4014", "UPP4014-000058"));
      iplanstatus.addItem(NCLangRes.getInstance().getStrByID("4014", "UPP4014-000059"));
      iplanstatus.addItem(NCLangRes.getInstance().getStrByID("4014", "UPP4014-000060"));
    }
  }

  protected void initFixSubMenuButton()
  {
    this.boSwith.setName(NCLangRes.getInstance().getStrByID("40140408", "UPT40140408-000062"));

    this.boSwitchForm = new ButtonObject(NCLangRes.getInstance().getStrByID("40140408", "UPT40140408-000059"), NCLangRes.getInstance().getStrByID("40140408", "UPT40140408-000059"), 1, "切换到表单");

    this.boSwitchList = new ButtonObject(NCLangRes.getInstance().getStrByID("40140408", "UPT40140408-000058"), NCLangRes.getInstance().getStrByID("40140408", "UPT40140408-000058"), 1, "切换到列表");

    this.boSwitchSource = new ButtonObject(NCLangRes.getInstance().getStrByID("40140408", "UPT40140408-000060"), NCLangRes.getInstance().getStrByID("40140408", "UPT40140408-000060"), 1, "切换到源表");

    this.boSwith.addChildButton(this.boSwitchForm);
    this.boSwith.addChildButton(this.boSwitchList);
    this.boSwith.addChildButton(this.boSwitchSource);
    super.initFixSubMenuButton();
  }

  protected void initSecBodyComboBox()
  {
  }

  public void initSecState()
  {
    getSecBillCardPanel().setEnabled(true);

    switchButtonStatus(DMBillStatus.CardNew);
  }

  public void initState()
  {
    getBillCardPanel().setEnabled(true);

    switchButtonStatus(DMBillStatus.ListView);
  }

  protected void initThdBodyComboBox()
  {
  }

  public void initThdState()
  {
    getThdBillCardPanel().setEnabled(true);

    switchButtonStatus(DMBillStatus.SourceView);
  }

  public void loadListTemplet()
  {
  }

  public void loadPanel()
  {
    setButtons(getBillButtons());
    if ((null == this.ivjSecBillCardPanel) || (this.ivjSecBillCardPanel.getBillData() == null) || (this.ivjSecBillCardPanel.getBillData().getHeadItems() == null))
    {
      loadSecCardTemplet();
      getSecBillCardPanel().addAfterEditListener(this);
      getSecBillCardPanel().getBillTable().getSelectionModel().addListSelectionListener(this);
      getSecBillCardPanel().getBillTable().getColumnModel().addColumnModelListener(this);
      getSecBillCardPanel().getBillModel().addTableModelListener(this);
      getSecBillCardPanel().addCheckBoxChangedListener(this);
    }
    if ((null == this.ivjThdBillCardPanel) || (this.ivjThdBillCardPanel.getBillData() == null)) {
      loadThdCardTemplet();
      getThdBillCardPanel().addCheckBoxChangedListener(this);
    }
    switchInterface();
  }

  public void loadSecCardTemplet()
  {
    showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000136"));

    BillData bd = new BillData(getSecBillCardPanel().getTempletData(getSecBillType(), null, getUserID(), getCorpID()));

    setSecCardPanel(bd);

    getSecBillCardPanel().setBillData(bd);

    setSecInputLimit();

    initSecBodyComboBox();

    initSecState();

    getSecBillCardPanel().addBodyTotalListener(this);

    showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000176"));
  }

  public void loadThdCardTemplet()
  {
    showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000136"));

    BillData bd = new BillData(getSecBillCardPanel().getTempletData(getThdBillType(), null, getUserID(), getCorpID()));

    setThdCardPanel(bd);
    changeBillListDataByUserDef(bd);

    getThdBillCardPanel().setBillData(bd);

    this.strTitle3 = bd.getTitle();

    setThdInputLimit();

    initThdBodyComboBox();

    initThdState();

    getThdBillCardPanel().addBodyTotalListener(this);

    showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000176"));
  }

  protected BillData changeBillListDataByUserDef(BillData billData)
  {
    DefVO[] defsHead = getDefVOsH();
    DefVO[] defsBody = getDefVOsB();

    updateItemByDef(billData, defsHead, "vuserdef", true, 0);

    updateItemByDef(billData, defsHead, "vuserdef", false, 0);
    updateItemByDef(billData, defsBody, "vuserdef_b_", false, 0);

    return billData;
  }

  public void onButtonClicked(ButtonObject bo)
  {
    super.onButtonClicked(bo);

    if (bo == this.boSwitchForm) {
      onSwitchForm();
    }
    if (bo == this.boSwitchList) {
      onSwitchList();
    }

    if (bo == this.boSwitchSource)
      onSwitchSource();
  }

  public void onCancel()
  {
    super.onCancel();
    getBillCardPanel().setEnabled(false);
    switchButtonStatus(DMBillStatus.ListView);
  }

  public void onEdit()
  {
    super.onEdit();
    switchButtonStatus(DMBillStatus.ListEdit);
    setEditFlag(DMBillStatus.ListEdit);
  }

  public void onSwitchForm()
  {
    setShowState(DMBillStatus.Card);

    loadPanel();
    setTitleText(this.strTitle2);
  }

  public void onSwitchList()
  {
    setShowState(DMBillStatus.List);

    loadPanel();
    setTitleText(this.strTitle1);
  }

  public void onSwitchSource()
  {
    setShowState(DMBillStatus.Source);

    loadPanel();
    setTitleText(this.strTitle3);
  }

  public void onSwith()
  {
    if (this.m_strShowState.equals(DMBillStatus.Card)) {
      this.m_strShowState = DMBillStatus.Source;
      this.boSwith.setName(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000188"));
    }
    else if (this.m_strShowState.equals(DMBillStatus.List)) {
      this.m_strShowState = DMBillStatus.Card;
      this.boSwith.setName(NCLangRes.getInstance().getStrByID("4014", "UPT40140408-000060"));
    }
    else {
      this.m_strShowState = DMBillStatus.List;
      this.boSwith.setName(NCLangRes.getInstance().getStrByID("4014", "UPT40140408-000059"));
    }
    loadPanel();
  }

  public void setCardPanel(BillData bdData)
  {
    super.setCardPanel(bdData);
    if ((null != bdData.getTitle()) && (bdData.getTitle().trim().length() != 0))
      this.strTitle1 = bdData.getTitle();
  }

  protected void setInputLimit()
  {
  }

  public void setSecBillTypeCode(String newSecBillTypeCode)
  {
    this.m_SecBillTypeCode = newSecBillTypeCode;
  }

  public void setSecCardPanel(BillData bdData)
  {
    setSecCardPanelByPara(bdData);
    setSecCardPanelByOther(bdData);
    if ((null != bdData.getTitle()) && (bdData.getTitle().trim().length() != 0))
    {
      this.strTitle2 = bdData.getTitle();
    }
  }

  protected void setSecCardPanelByOther(BillData bdData)
  {
  }

  protected void setSecCardPanelByPara(BillData bdData)
  {
    ArrayList alDecimalKey = new ArrayList();
    alDecimalKey.add(this.m_itemNumKeys);
    alDecimalKey.add(this.m_itemANumKeys);
    alDecimalKey.add(this.m_itemPriceKeys);
    alDecimalKey.add(this.m_itemFactorKey);
    alDecimalKey.add(this.m_itemMoneyKey);
    ArrayList alPrecision = new ArrayList();
    alPrecision.add(this.BD501);
    alPrecision.add(this.BD502);
    alPrecision.add(this.BD505);
    alPrecision.add(this.BD503);
    alPrecision.add(this.BD301);
    new DMBillCardPanel().changeDecimalItemsPrecision(bdData, alDecimalKey, alPrecision);
  }

  protected void setSecInputLimit()
  {
  }

  public void setSourceANumItemKeys(String[] newANumItemKeys)
  {
    this.m_itemSourceANumKeys = new String[newANumItemKeys.length];
    for (int i = 0; i < newANumItemKeys.length; i++)
      this.m_itemSourceANumKeys[i] = newANumItemKeys[i];
  }

  public void setSourceFactorItemKey(String newFactorItemKey)
  {
    this.m_itemSourceFactorKey = new String("");
    this.m_itemSourceFactorKey = newFactorItemKey;
  }

  public void setSourceMoneyItemKey(String[] newMoneyItemKey)
  {
    this.m_itemSourceMoneyKey = newMoneyItemKey;
  }

  public void setSourceNumItemKeys(String[] newNumItemKeys)
  {
    this.m_itemSourceNumKeys = new String[newNumItemKeys.length];
    for (int i = 0; i < newNumItemKeys.length; i++)
      this.m_itemSourceNumKeys[i] = newNumItemKeys[i];
  }

  public void setSourcePriceItemKeys(String[] newPriceItemKeys)
  {
    this.m_itemSourcePriceKeys = new String[newPriceItemKeys.length];
    for (int i = 0; i < newPriceItemKeys.length; i++)
      this.m_itemSourcePriceKeys[i] = newPriceItemKeys[i];
  }

  public void setThdBillTypeCode(String newThdBillTypeCode)
  {
    this.m_ThdBillTypeCode = newThdBillTypeCode;
  }

  public void setThdCardPanel(BillData bdData)
  {
    setThdCardPanelByPara(bdData);
    setThdCardPanelByOther(bdData);
  }

  protected void setThdCardPanelByOther(BillData bdData)
  {
  }

  protected void setThdCardPanelByPara(BillData bdData)
  {
    ArrayList alDecimalKey = new ArrayList();
    alDecimalKey.add(this.m_itemSourceNumKeys);
    alDecimalKey.add(this.m_itemSourceANumKeys);
    alDecimalKey.add(this.m_itemSourcePriceKeys);
    alDecimalKey.add(this.m_itemSourceFactorKey);
    alDecimalKey.add(this.m_itemSourceMoneyKey);
    ArrayList alPrecision = new ArrayList();
    alPrecision.add(this.BD501);
    alPrecision.add(this.BD502);
    alPrecision.add(this.BD505);
    alPrecision.add(this.BD503);
    alPrecision.add(this.BD301);
    new DMBillCardPanel().changeDecimalItemsPrecision(bdData, alDecimalKey, alPrecision);
    try
    {
      bdData = changeSingleTalbeBillDataByUserDef(bdData, false);
    }
    catch (Exception e)
    {
    }
  }

  protected void setThdInputLimit()
  {
  }

  public void switchButtonStatus(int status)
  {
    if (status == DMBillStatus.CardView)
    {
      setButton(this.boEdit, true);
      setButton(this.boAddLine, false);
      setButton(this.boDelLine, false);
      setButton(this.boCopyLine, false);
      setButton(this.boPasteLine, false);
      setButton(this.boSave, false);
      setButton(this.boCancel, false);
      setButton(this.boQuery, true);
      setButton(this.boPrint, true);
    } else if (status == DMBillStatus.CardEdit)
    {
      setButton(this.boEdit, false);
      setButton(this.boAddLine, true);
      setButton(this.boDelLine, true);
      setButton(this.boCopyLine, true);
      setButton(this.boPasteLine, true);
      setButton(this.boSave, true);
      setButton(this.boCancel, true);
      setButton(this.boQuery, false);
      setButton(this.boPrint, false);
    } else if (status == DMBillStatus.ListNew)
    {
      setButton(this.boEdit, false);
      setButton(this.boAddLine, false);
      setButton(this.boDelLine, false);
      setButton(this.boCopyLine, false);
      setButton(this.boPasteLine, false);
      setButton(this.boSave, true);
      setButton(this.boCancel, false);
      setButton(this.boQuery, true);
      setButton(this.boPrint, false);
      setButton(this.boAudit, true);
      setButton(this.boCancelAudit, true);
      setButton(this.boEnd, true);
    } else if (status == DMBillStatus.ListView)
    {
      setButton(this.boEdit, true);
      setButton(this.boAddLine, false);
      setButton(this.boDelLine, false);
      setButton(this.boCopyLine, false);
      setButton(this.boPasteLine, false);
      setButton(this.boSave, false);
      setButton(this.boCancel, false);
      setButton(this.boQuery, true);
      setButton(this.boPrint, true);
      setButton(this.boAudit, true);
      setButton(this.boCancelAudit, true);
      setButton(this.boEnd, true);
    } else if (status == DMBillStatus.ListEdit)
    {
      setButton(this.boEdit, false);
      setButton(this.boAddLine, true);
      setButton(this.boDelLine, true);
      setButton(this.boCopyLine, true);
      setButton(this.boPasteLine, true);
      setButton(this.boSave, true);
      setButton(this.boCancel, true);
      setButton(this.boQuery, false);
      setButton(this.boPrint, false);
      setButton(this.boAudit, false);
      setButton(this.boCancelAudit, false);
      setButton(this.boEnd, false);
    } else if (status != DMBillStatus.SourceView);
  }

  protected void switchInterface()
  {
    if (this.m_strShowState.equals(DMBillStatus.List)) {
      if (null != this.ivjSecBillCardPanel)
        remove(getSecBillCardPanel());
      if (null != this.ivjThdBillCardPanel)
        remove(getThdBillCardPanel());
      add(getBillCardPanel(), "Center");
    }
    else if (this.m_strShowState.equals(DMBillStatus.Card)) {
      remove(getBillCardPanel());
      remove(getThdBillCardPanel());
      add(getSecBillCardPanel(), "Center");
    }
    else if (this.m_strShowState.equals(DMBillStatus.Source)) {
      remove(getBillCardPanel());
      remove(getSecBillCardPanel());
      add(getThdBillCardPanel(), "Center");
    }

    updateUI();
  }

  public void valueChanged(ListSelectionEvent e)
  {
    super.valueChanged(e);

    if (getShowState().equals(DMBillStatus.Card)) {
      int colnow = getSecBillCardPanel().getBillTable().getSelectedColumn();
      int rownow = getSecBillCardPanel().getBillTable().getSelectedRow();
      if ((colnow >= 0) && (rownow >= 0)) {
        initRef(rownow, colnow, getSecBillCardPanel());
        String key = getSecBillCardPanel().getBodyShowItems()[colnow].getKey();
        Object value = getSecBillCardPanel().getBodyValueAt(rownow, key);
        whenEntered(rownow, colnow, key, value, getSecBillCardPanel());
      }
    } else if (getShowState().equals(DMBillStatus.List)) {
      int colnow = getBillCardPanel().getBillTable().getSelectedColumn();
      int rownow = getBillCardPanel().getBillTable().getSelectedRow();
      if ((colnow >= 0) && (rownow >= 0)) {
        String key = getBillCardPanel().getBodyShowItems()[colnow].getKey();
        Object value = getBillCardPanel().getBodyValueAt(rownow, key);
        whenEntered(rownow, colnow, key, value, getBillCardPanel());
      }
    }
  }

  public void whenEntered(int row, int col, String key, Object value, BillCardPanel bcp)
  {
    super.whenEntered(row, col, key, value, bcp);
  }
}