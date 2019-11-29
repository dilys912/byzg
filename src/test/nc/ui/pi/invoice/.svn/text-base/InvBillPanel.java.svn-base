package nc.ui.pi.invoice;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JComponent;
import nc.bs.framework.common.NCLocator;
import nc.ui.bd.b39.PhaseRefModel;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.bd.ref.UFRefManage;
import nc.ui.ic.service.IICPub_LotNumbRefPane;
import nc.ui.ml.NCLangRes;
import nc.ui.pi.pub.AccountsForVendorRefModel;
import nc.ui.pu.pub.PuTool;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.scm.freecust.UFRefGridUI;
import nc.ui.scm.ic.freeitem.FreeItemRefPane;
import nc.ui.scm.pub.def.DefSetTool;
import nc.ui.scm.service.LocalCallService;
import nc.vo.bd.CorpVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.service.ServcallVO;
import nc.vo.sm.UserVO;

public class InvBillPanel extends BillCardPanel
  implements ItemListener
{
  int nMoneyDecimal = 2;

  int nNumDigit = 2;

  int nPriceDigit = 2;

  public InvBillPanel(ClientEnvironment ce)
    throws Exception
  {
    initi(ce);
  }

  private void initi(ClientEnvironment ce)
  {
    if (!initiTemplet(ce)) {
      return;
    }

    initPara(ce.getCorporation().getPrimaryKey());

    initiRefPane(ce.getCorporation().getPrimaryKey());

    initiComboBox();

    initiDecimalFractionNum();

    initiEnabledFalseItems();

    initiHideItems();

    initiCheckBox();

    initiTotalRow();

    initiUserDef(ce);

    initiInternational();

    initiBillThMark();
  }

  public void itemStateChanged(ItemEvent e)
  {
    if (e.getSource() == getHeadItem("finitflag").getComponent())
    {
      boolean selectedFlag = ((UICheckBox)getHeadItem("finitflag").getComponent()).isSelected();
      if (selectedFlag) {
        showBodyTableCol("noriginalpaymentmny");
      } else {
        hideBodyTableCol("noriginalpaymentmny");

        for (int i = 0; i < getRowCount(); i++)
          setBodyValueAt(null, i, "noriginalpaymentmny");
      }
    }
  }

  public int getSortTypeByBillItemKey(String sItemKey)
  {
    if ("crowno".equals(sItemKey))
      return 2;
    if ("csourcebillrowno".equals(sItemKey))
      return 2;
    if ("cancestorbillrowno".equals(sItemKey)) {
      return 2;
    }

    return getBillModel().getItemByKey(sItemKey).getDataType();
  }

  private void initiBillThMark()
  {
    setBodyShowThMark(true);
  }

  private void initiCheckBox()
  {
    ((UICheckBox)getHeadItem("finitflag").getComponent()).addItemListener(this);
  }

  private void initiComboBox()
  {
    UIComboBox comItem = (UIComboBox)getHeadItem("iinvoicetype").getComponent();
    getHeadItem("iinvoicetype").setWithIndex(true);

    comItem.addItem(NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-001146"));
    comItem.addItem(NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-001147"));
    comItem.addItem(NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-001148"));

    comItem.setTranslate(true);

    comItem = (UIComboBox)getHeadItem("idiscounttaxtype").getComponent();
    getHeadItem("idiscounttaxtype").setWithIndex(true);
    comItem.addItem(NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000105"));
    comItem.addItem(NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000106"));
    comItem.addItem(NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000107"));
    comItem.setSelectedIndex(1);
    comItem.setTranslate(true);

    comItem = (UIComboBox)getBodyItem("idiscounttaxtype").getComponent();
    getBodyItem("idiscounttaxtype").setWithIndex(true);
    comItem.addItem(NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000105"));
    comItem.addItem(NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000106"));
    comItem.addItem(NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000107"));

    comItem.setTranslate(true);
  }

  private void initiDecimalFractionNum()
  {
    UIRefPane nRefPanel = (UIRefPane)getHeadItem("ntaxrate").getComponent();
    UITextField UI = nRefPanel.getUITextField();
    UI.setMinValue(0.0D);
    UI.setDelStr("-");
    UI.setMaxValue(100.0D);

    nRefPanel = (UIRefPane)getHeadItem("nexchangeotobrate").getComponent();
    UI = nRefPanel.getUITextField();
    UI.setMinValue(0.0D);
    UI.setDelStr("-");

    nRefPanel = (UIRefPane)getHeadItem("nexchangeotoarate").getComponent();
    UI = nRefPanel.getUITextField();
    UI.setMinValue(0.0D);
    UI.setDelStr("-");

    nRefPanel = (UIRefPane)getBodyItem("ninvoicenum").getComponent();
    UI = nRefPanel.getUITextField();
    getBodyItem("ninvoicenum").setDecimalDigits(this.nNumDigit);

    nRefPanel = (UIRefPane)getBodyItem("noriginalcurprice").getComponent();
    UI = nRefPanel.getUITextField();
    getBodyItem("noriginalcurprice").setDecimalDigits(this.nPriceDigit);
    UI.setMinValue(0.0D);
    UI.setDelStr("-");

    nRefPanel = (UIRefPane)getBodyItem("norgnettaxprice").getComponent();
    UI = nRefPanel.getUITextField();
    getBodyItem("norgnettaxprice").setDecimalDigits(this.nPriceDigit);
    UI.setMinValue(0.0D);
    UI.setDelStr("-");

    nRefPanel = (UIRefPane)getBodyItem("nplanprice").getComponent();
    UI = nRefPanel.getUITextField();
    getBodyItem("nplanprice").setDecimalDigits(this.nPriceDigit);
    UI.setMinValue(0.0D);
    UI.setDelStr("-");

    nRefPanel = (UIRefPane)getBodyItem("ntaxrate").getComponent();
    UI = nRefPanel.getUITextField();
    UI.setMinValue(0.0D);
    UI.setDelStr("-");

    nRefPanel = (UIRefPane)getBodyItem("nexchangeotobrate").getComponent();
    UI = nRefPanel.getUITextField();
    UI.setMinValue(0.0D);
    UI.setDelStr("-");

    nRefPanel = (UIRefPane)getBodyItem("nexchangeotoarate").getComponent();
    UI = nRefPanel.getUITextField();
    UI.setMinValue(0.0D);
    UI.setDelStr("-");

    if (getBodyItem("nreasonwastenum") != null) {
      nRefPanel = (UIRefPane)getBodyItem("nreasonwastenum").getComponent();
      UI = nRefPanel.getUITextField();
      getBodyItem("nreasonwastenum").setDecimalDigits(this.nNumDigit);
    }
  }

  private void initiEnabledFalseItems()
  {
    getHeadItem("cbiztype").setEnabled(false);

    getHeadItem("cvendorphone").setEnabled(false);

    getHeadItem("cvendoraccount").setEnabled(false);

    getBodyItem("invname").setEnabled(false);
    getBodyItem("invspec").setEnabled(false);
    getBodyItem("invtype").setEnabled(false);
    getBodyItem("cproducearea").setEnabled(false);
    getBodyItem("measname").setEnabled(false);
    getBodyItem("nmoney").setEnabled(false);
    getBodyItem("nexchangeotobrate").setEnabled(false);
    getBodyItem("nexchangeotoarate").setEnabled(false);
    getBodyItem("noriginalpaymentmny").setEnabled(false);
    getBodyItem("npaymentmny").setEnabled(false);

    getTailItem("cauditpsn").setEnabled(false);
    getTailItem("dauditdate").setEnabled(false);
    getTailItem("coperator").setEnabled(false);
  }

  private void initiHideItems()
  {
  }

  private void initiInternational()
  {
    PuTool.setTranslateRender(this);
  }

  private void initiRefPane(String pk_corp)
  {
    FreeItemRefPane pane = new FreeItemRefPane();
    pane.setMaxLength(getBodyItem("vfree0").getLength());
    getBodyItem("vfree0").setComponent(pane);

    BillItem bt = getHeadItem("cvendormangid");
    if (bt != null) {
      UIRefPane panel = (UIRefPane)bt.getComponent();
      panel.getRef().getRefModel().setRefNameField("bd_cubasdoc.custshortname");
    }

    ((UIRefPane)getHeadItem("cvendormangid").getComponent()).getRefModel().addWherePart("AND bd_cumandoc.frozenflag='N'");

    ((UIRefPane)getHeadItem("cfreecustid").getComponent()).getRef().setRefUI(new UFRefGridUI(this));

    ((UIRefPane)getHeadItem("cpayunit").getComponent()).getRefModel().addWherePart("AND bd_cumandoc.frozenflag='N'");

    ((UIRefPane)getHeadItem("caccountbankid").getComponent()).setIsCustomDefined(true);
    ((UIRefPane)getHeadItem("caccountbankid").getComponent()).setRefModel(new AccountsForVendorRefModel());

    ((UIRefPane)getHeadItem("cemployeeid").getComponent()).setRefNodeName("人员档案");

    ((UIRefPane)getHeadItem("vmemo").getComponent()).setAutoCheck(false);

    ((UIRefPane)getHeadItem("cstoreorganization").getComponent()).getRefModel().addWherePart(" AND (bd_calbody.property = 0 or bd_calbody.property = 1) ");

    UIRefPane refPane = (UIRefPane)getBodyItem("invcode").getComponent();
    refPane.getRefModel().addWherePart("AND bd_invmandoc.sealflag='N' AND UPPER(ISNULL(bd_invmandoc.iscanpurchased,'Y')) = 'Y'");
    refPane.setTreeGridNodeMultiSelected(true);
    refPane.setMultiSelectedEnabled(true);

    ((UIRefPane)getBodyItem("cprojectname").getComponent()).getRefModel().addWherePart("AND isnull(bd_jobbasfil.sealflag,'N')='N'");
    ((UIRefPane)getBodyItem("cprojectname").getComponent()).setReturnCode(false);

    ((UIRefPane)getBodyItem("cprojectphasename").getComponent()).setIsCustomDefined(true);

    ((UIRefPane)getBodyItem("cprojectphasename").getComponent()).setRefModel(new PhaseRefModel());
    ((UIRefPane)getBodyItem("cprojectphasename").getComponent()).setReturnCode(false);

    ((UIRefPane)getBodyItem("vmemo").getComponent()).setReturnCode(false);
    ((UIRefPane)getBodyItem("vmemo").getComponent()).setAutoCheck(false);

    ((UIRefPane)getBodyItem("currname").getComponent()).setReturnCode(false);
    ((UIRefPane)getBodyItem("cusedeptname").getComponent()).setReturnCode(false);

    if (getBodyItem("vproducenum").isShow()) {
      IICPub_LotNumbRefPane lotRef = null;
      try
      {
        lotRef = (IICPub_LotNumbRefPane)NCLocator.getInstance().lookup(IICPub_LotNumbRefPane.class.getName());
      } catch (Exception e) {
        SCMEnv.out("获取批次号参照时出现异常，批次号参照不能正常使用！");
      }

      if (lotRef != null) {
        lotRef.setIsCustomDefined(true);
        lotRef.setMaxLength(getBodyItem("vproducenum").getLength());
        getBodyItem("vproducenum").setComponent((JComponent)lotRef);
      }

    }

    ((UIRefPane)getBodyItem("cwarehousename").getComponent()).setReturnCode(false);
  }

  private boolean initiTemplet(ClientEnvironment ce)
  {
    try
    {
      loadTemplet("25", null, ce.getUser().getPrimaryKey(), ce.getCorporation().getPk_corp());
    }
    catch (Exception e)
    {
      SCMEnv.out(e);
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270"), NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000386"));
      return false;
    }

    return true;
  }

  private void initiTotalRow()
  {
    setTatolRowShow(true);
  }

  private void initiUserDef(ClientEnvironment ce)
  {
    DefSetTool.updateBillCardPanelUserDef(this, ce.getCorporation().getPk_corp(), "25", "vdef", "vdef");

    setEnabled(false);
  }

  public void initPara(String pk_corp)
  {
    try
    {
      ServcallVO[] scDisc = new ServcallVO[2];

      scDisc[0] = new ServcallVO();
      scDisc[0].setBeanName("nc.itf.pu.pub.IPub");
      scDisc[0].setMethodName("getDigitBatch");
      
     // scDisc[0].setParameter(new Object[] { pk_corp, { "BD501", "BD505" } });    //yqq  20161221反编译修改
      scDisc[0].setParameter(new Object[] { pk_corp, new String[]{ "BD501", "BD505" } });
    //  scDisc[0].setParameterTypes(new Class[] { String.class, [Ljava.lang.String.class });  //yqq  20161221反编译修改
      scDisc[0].setParameterTypes(new Class[] { String.class, java.lang.String.class });

      scDisc[1] = new ServcallVO();
      scDisc[1].setBeanName("nc.itf.rc.receive.IArriveorder");
      scDisc[1].setMethodName("getCurrDecimal");
      scDisc[1].setParameter(new Object[] { pk_corp });
      scDisc[1].setParameterTypes(new Class[] { String.class });

      Object[] oParaValue = LocalCallService.callService(scDisc);
      if ((oParaValue != null) && (oParaValue.length == 2))
      {
        int[] iDigits = (int[])(int[])oParaValue[0];
        if ((iDigits != null) && (iDigits.length == 2)) {
          this.nNumDigit = iDigits[0];
          this.nPriceDigit = iDigits[1];
        }

        this.nMoneyDecimal = ((Integer)oParaValue[1]).intValue();
      }

    }
    catch (Exception e)
    {
      SCMEnv.out(e);
    }
  }
}