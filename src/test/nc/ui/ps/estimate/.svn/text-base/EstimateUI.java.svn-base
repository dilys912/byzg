package nc.ui.ps.estimate;

import java.awt.BorderLayout;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.ButtonGroup;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.ml.NCLangRes;
import nc.ui.po.pub.PoQueryCondition;
import nc.ui.pps.PricStlHelper;
import nc.ui.pu.pub.PuTool;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRadioButton;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.IBillRelaSortListener2;
import nc.ui.pub.para.SysInitBO_Client;
import nc.ui.pub.print.IDataSource;
import nc.ui.pub.print.PrintEntry;
import nc.ui.scm.pub.FreeVOParse;
import nc.ui.scm.pub.cache.CacheTool;
import nc.ui.scm.pub.def.DefSetTool;
import nc.ui.scm.pub.panel.RelationsCal;
import nc.ui.scm.service.LocalCallService;
import nc.vo.bd.CorpVO;
import nc.vo.pps.PricParaVO;
import nc.vo.ps.estimate.EstimateVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.pu.Timer;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.relacal.SCMRelationsCal;
import nc.vo.scm.service.ServcallVO;
import nc.vo.sm.UserVO;

public class EstimateUI extends ToftPanel
  implements BillEditListener, ListSelectionListener, BillEditListener2, IDataSource, IBillRelaSortListener2
{
  private ButtonObject[] m_buttons = null;

  private int[] m_nButtonState = null;

  private BillCardPanel m_billPanel = null;

  private UIRadioButton m_rbEst = null;
  private UIRadioButton m_rbUnEst = null;

  private String m_sUnitCode = getCorpPrimaryKey();

  private PoQueryCondition m_condClient = null;

  private EstimateVO[] m_estimate1VOs = null;
  private EstimateVO[] m_estimate2VOs = null;
  private String m_sZG = "N";

  private String m_sEstimateMode = null;

  private String m_sDiffMode = null;

  private String m_sEstPriceSource = null;

  private boolean m_bICStartUp = false;

  private int[] m_measure = null;

  private UFBoolean m_bZGYF = new UFBoolean(false);

  private String m_sCurrTypeID = null;

  public EstimateUI()
  {
    init();
  }

  public void afterEdit(BillEditEvent event)
  {
    computeBodyData(event);
  }

  public void bodyRowChange(BillEditEvent event)
  {
  }

  private void changeButtonState()
  {
    for (int i = 0; i < this.m_nButtonState.length; i++) {
      if (this.m_nButtonState[i] == 0) {
        this.m_buttons[i].setVisible(true);
        this.m_buttons[i].setEnabled(true);
      } else if (this.m_nButtonState[i] == 1) {
        this.m_buttons[i].setVisible(true);
        this.m_buttons[i].setEnabled(false);
      } else if (this.m_nButtonState[i] == 2) {
        this.m_buttons[i].setVisible(false);
      }
      updateButton(this.m_buttons[i]);
    }
  }

  private void changeQueryModelLayout()
  {
    if ((this.m_rbEst != null) && (this.m_rbUnEst != null)) {
      return;
    }
    UILabel label1 = new UILabel(NCLangRes.getInstance().getStrByID("4004050301", "UPP4004050301-000000"));
    label1.setBounds(30, 65, 100, 25);

    this.m_rbEst = new UIRadioButton();
    this.m_rbEst.setBounds(130, 65, 16, 16);
    this.m_rbEst.setSelected(true);
    UILabel label2 = new UILabel(NCLangRes.getInstance().getStrByID("4004050301", "UPP4004050301-000001"));
    label2.setBounds(146, 65, 60, 25);

    this.m_rbUnEst = new UIRadioButton();
    this.m_rbUnEst.setBounds(130, 95, 16, 16);
    UILabel label3 = new UILabel(NCLangRes.getInstance().getStrByID("4004050301", "UPP4004050301-000002"));
    label3.setBounds(146, 95, 60, 25);

    ButtonGroup buttonGroup = new ButtonGroup();
    buttonGroup.add(this.m_rbEst);
    buttonGroup.add(this.m_rbUnEst);

    this.m_condClient.getUIPanelNormal().add(label1);
    this.m_condClient.getUIPanelNormal().add(label2);
    this.m_condClient.getUIPanelNormal().add(label3);
    this.m_condClient.getUIPanelNormal().add(this.m_rbEst);
    this.m_condClient.getUIPanelNormal().add(this.m_rbUnEst);
  }

  private void computeBodyData(BillEditEvent event)
  {
    int iRowPos = event.getRow();
    if (iRowPos < 0)
      return;
    Object oMoney = getBillCardPanel().getBillModel().getValueAt(iRowPos, "nmoney");
    UFDouble ufdMoney = (oMoney == null) || (oMoney.toString().trim().length() == 0) ? new UFDouble(0.0D) : new UFDouble(oMoney.toString().trim());

    int[] descriptions = { 0, 1, 2, 7, 13, 8, 14, 11, 15, 9, 18, 12 };

    String s = "应税内含";
    if (this.m_estimate1VOs[iRowPos].getIdiscounttaxtype().intValue() == 1) s = "应税外加";
    if (this.m_estimate1VOs[iRowPos].getIdiscounttaxtype().intValue() == 2) s = "不计税";

    String[] keys = { s, "idiscounttaxtype", "ninnum", "nprice", "nmoney", "ntaxprice", "ntotalmoney", "ntaxrate", "ndiscountrate", "nnetprice", "nnettaxprice", "ntaxmoney" };

    if ((event.getKey() != null) && ("nmoney".equalsIgnoreCase(event.getKey().trim())))
    {
      if ((this.m_estimate1VOs[iRowPos].getNinnum() != null) && (ufdMoney.multiply(this.m_estimate1VOs[iRowPos].getNinnum()).doubleValue() < 0.0D))
      {
        MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000022"), NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000023"));
        getBillCardPanel().setBodyValueAt(event.getOldValue(), iRowPos, "nmoney");
        return;
      }
    }

    RelationsCal.calculate(event, getBillCardPanel(), new int[] { PuTool.getPricePriorPolicy(this.m_sUnitCode) }, descriptions, keys, EstimateVO.class.getName());

    Object oPrice = getBillCardPanel().getBillModel().getValueAt(iRowPos, "nprice");
    oMoney = getBillCardPanel().getBillModel().getValueAt(iRowPos, "nmoney");
    Object oTaxPrice = getBillCardPanel().getBillModel().getValueAt(iRowPos, "ntaxprice");
    Object oTaxRate = getBillCardPanel().getBillModel().getValueAt(iRowPos, "ntaxrate");
    Object oTotalMoney = getBillCardPanel().getBillModel().getValueAt(iRowPos, "ntotalmoney");

    if ((oPrice == null) || (oPrice.toString().trim().length() == 0)) this.m_estimate1VOs[iRowPos].setNprice(new UFDouble(0)); else {
      this.m_estimate1VOs[iRowPos].setNprice(new UFDouble(oPrice.toString()));
    }
    if ((oMoney == null) || (oMoney.toString().trim().length() == 0)) this.m_estimate1VOs[iRowPos].setNmoney(new UFDouble(0)); else {
      this.m_estimate1VOs[iRowPos].setNmoney(new UFDouble(oMoney.toString()));
    }
    if ((oTaxPrice == null) || (oTaxPrice.toString().trim().length() == 0)) this.m_estimate1VOs[iRowPos].setNtaxprice(new UFDouble(0)); else {
      this.m_estimate1VOs[iRowPos].setNtaxprice(new UFDouble(oTaxPrice.toString()));
    }
    if ((oTaxRate == null) || (oTaxRate.toString().trim().length() == 0)) this.m_estimate1VOs[iRowPos].setNtaxrate(new UFDouble(0)); else {
      this.m_estimate1VOs[iRowPos].setNtaxrate(new UFDouble(oTaxRate.toString()));
    }
    if ((oTotalMoney == null) || (oTotalMoney.toString().trim().length() == 0)) this.m_estimate1VOs[iRowPos].setNtotalmoney(new UFDouble(0)); else
      this.m_estimate1VOs[iRowPos].setNtotalmoney(new UFDouble(oTotalMoney.toString()));
  }

  public String[] getAllDataItemExpress()
  {
    BillItem[] bodyItems = getBillCardPanel().getBodyShowItems();

    Vector v = new Vector();
    for (int i = 0; i < bodyItems.length; i++) {
      v.addElement(bodyItems[i].getKey());
    }
    if (v.size() > 0) {
      String[] sKey = new String[v.size()];
      v.copyInto(sKey);
      return sKey;
    }
    return null;
  }

  public String[] getAllDataItemNames()
  {
    BillItem[] bodyItems = getBillCardPanel().getBodyShowItems();

    Vector v = new Vector();
    for (int i = 0; i < bodyItems.length; i++) {
      v.addElement(bodyItems[i].getName());
    }
    if (v.size() > 0) {
      String[] sName = new String[v.size()];
      v.copyInto(sName);
      return sName;
    }
    return null;
  }

  private BillCardPanel getBillCardPanel()
  {
    if (this.m_billPanel == null) {
      try {
        this.m_billPanel = new BillCardPanel();

        BillData bd = new BillData(this.m_billPanel.getTempletData("40040503010000000000"));

        bd = initDecimal(bd);

        this.m_billPanel.setBillData(bd);
        this.m_billPanel.setShowThMark(true);
        this.m_billPanel.setTatolRowShow(true);

        this.m_billPanel.addEditListener(this);
        this.m_billPanel.addBodyEditListener2(this);
        this.m_billPanel.setBodyMenuShow(false);

        this.m_billPanel.getBillTable().setCellSelectionEnabled(false);
        this.m_billPanel.getBillTable().setSelectionMode(2);

        this.m_billPanel.getBillTable().getSelectionModel().addListSelectionListener(this);
        this.m_billPanel.getBillModel().addSortRelaObjectListener2(this);
      }
      catch (Throwable exception)
      {
        SCMEnv.out(exception);
        MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000024"), NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000025"));
        return null;
      }
    }

    return this.m_billPanel;
  }

  public String[] getDependentItemExpressByExpress(String itemName)
  {
    return null;
  }

  public String[] getItemValuesByExpress(String itemKey)
  {
    itemKey = itemKey.trim();

    if (this.m_sZG.equals("N")) {
      if ((this.m_estimate1VOs != null) && (this.m_estimate1VOs.length > 0)) {
        String[] sValues = new String[this.m_estimate1VOs.length];
        for (int i = 0; i < this.m_estimate1VOs.length; i++) {
          Object o = getBillCardPanel().getBodyValueAt(i, itemKey);
          if (o != null)
            sValues[i] = o.toString();
          else
            sValues[i] = "";
        }
        return sValues;
      }

    }
    else if ((this.m_estimate2VOs != null) && (this.m_estimate2VOs.length > 0)) {
      String[] sValues = new String[this.m_estimate2VOs.length];
      for (int i = 0; i < this.m_estimate2VOs.length; i++) {
        Object o = getBillCardPanel().getBodyValueAt(i, itemKey);
        if (o != null)
          sValues[i] = o.toString();
        else
          sValues[i] = "";
      }
      return sValues;
    }

    return null;
  }

  public String getModuleName()
  {
    return "4004050301";
  }

  public String getTitle()
  {
    return NCLangRes.getInstance().getStrByID("4004050301", "UPT4004050301-000002");
  }

  public void init()
  {
    initpara();

    this.m_buttons = new ButtonObject[8];
    this.m_buttons[0] = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000041"), NCLangRes.getInstance().getStrByID("common", "UC001-0000041"), 2, "全选");
    this.m_buttons[1] = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000042"), NCLangRes.getInstance().getStrByID("common", "UC001-0000042"), 2, "全消");
    this.m_buttons[2] = new ButtonObject(NCLangRes.getInstance().getStrByID("4004050301", "UPT4004050301-000003"), NCLangRes.getInstance().getStrByID("4004050301", "UPT4004050301-000003"), 2, "暂估");
    this.m_buttons[3] = new ButtonObject(NCLangRes.getInstance().getStrByID("4004050301", "UPT4004050301-000004"), NCLangRes.getInstance().getStrByID("4004050301", "UPT4004050301-000004"), 2, "反暂");
    this.m_buttons[4] = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000006"), NCLangRes.getInstance().getStrByID("common", "UC001-0000006"), 2, "查询");
    this.m_buttons[5] = new ButtonObject(NCLangRes.getInstance().getStrByID("4004050301", "UPT4004050301-000005"), NCLangRes.getInstance().getStrByID("4004050301", "UPT4004050301-000005"), 2, "打印预览");
    this.m_buttons[6] = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000007"), NCLangRes.getInstance().getStrByID("common", "UC001-0000007"), 2, "打印");
    this.m_buttons[7] = new ButtonObject(NCLangRes.getInstance().getStrByID("4004050301", "UPT4004050301-000007"), NCLangRes.getInstance().getStrByID("4004050301", "UPT4004050301-000007"), 2, "优质优价计算");
    setButtons(this.m_buttons);

    setLayout(new BorderLayout());
    add(getBillCardPanel(), "Center");
    getBillCardPanel().setEnabled(false);

    UIComboBox comItem = (UIComboBox)getBillCardPanel().getBodyItem("idiscounttaxtype").getComponent();
    getBillCardPanel().getBodyItem("idiscounttaxtype").setWithIndex(true);
    comItem.addItem(NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000105"));
    comItem.addItem(NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000106"));
    comItem.addItem(NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000107"));
    comItem.setTranslate(true);

    this.m_nButtonState = new int[this.m_buttons.length];

    for (int i = 0; i < 5; i++) {
      this.m_nButtonState[i] = 1;
    }
    this.m_nButtonState[4] = 0;
    this.m_nButtonState[5] = 0;
    this.m_nButtonState[6] = 0;
    this.m_nButtonState[7] = 0;
    changeButtonState();
  }

  private BillData initDecimal(BillData bd)
  {
    if ((this.m_measure == null) || (this.m_measure.length == 0)) {
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000026"), NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000027"));
      return null;
    }

    int nMeasDecimal = this.m_measure[0];

    int nPriceDecimal = this.m_measure[1];

    int nMoneyDecimal = this.m_measure[2];

    BillItem item1 = bd.getBodyItem("nprice");
    item1.setDecimalDigits(nPriceDecimal);

    item1 = bd.getBodyItem("ntaxprice");
    item1.setDecimalDigits(nPriceDecimal);

    BillItem item2 = bd.getBodyItem("ninnum");
    item2.setDecimalDigits(nMeasDecimal);

    BillItem item3 = bd.getBodyItem("nsettlenum");
    item3.setDecimalDigits(nMeasDecimal);

    BillItem item4 = bd.getBodyItem("nmoney");
    item4.setDecimalDigits(nMoneyDecimal);

    BillItem item5 = bd.getBodyItem("nsettlemny");
    item5.setDecimalDigits(nMoneyDecimal);

    item5 = bd.getBodyItem("ntotalmoney");
    item5.setDecimalDigits(nMoneyDecimal);

    item5 = bd.getBodyItem("ntaxmoney");
    item5.setDecimalDigits(nMoneyDecimal);

    return bd;
  }

  public void initpara()
  {
    try
    {
      Object[] objs = null;
      ServcallVO[] scds = new ServcallVO[2];

      scds[0] = new ServcallVO();
      scds[0].setBeanName("nc.itf.pu.pub.IPub");
      scds[0].setMethodName("getDigitBatch");
 
      //yqq 20170113 反编译
  //    scds[0].setParameter(new Object[] { this.m_sUnitCode, { "BD501", "BD505", "BD301" } });
 //     scds[0].setParameterTypes(new Class[] { String.class, [Ljava.lang.String.class });
      scds[0].setParameter(new Object[] { this.m_sUnitCode, new String[]{ "BD501", "BD505", "BD301" } });
      scds[0].setParameterTypes(new Class[] { String.class, java.lang.String.class });

      scds[1] = new ServcallVO();
      scds[1].setBeanName("nc.itf.pu.pub.IPub");
      scds[1].setMethodName("isEnabled");
      scds[1].setParameter(new Object[] { this.m_sUnitCode, "IC" });
      scds[1].setParameterTypes(new Class[] { String.class, String.class });

      Hashtable hTemp = SysInitBO_Client.queryBatchParaValues(this.m_sUnitCode, new String[] { "PO12", "PO13", "PO27", "PO52", "BD301" });
      if ((hTemp == null) || (hTemp.size() == 0)) {
        SCMEnv.out("未获取初始化参数PO12,PO13,PO27,PO52,BD301 请检查...");
        return;
      }
      if (hTemp.get("PO12") == null) {
        SCMEnv.out("未获取初始化参数PO12, 请检查...");
        return;
      }
      Object temp = hTemp.get("PO12");
      this.m_sEstimateMode = temp.toString();

      if (hTemp.get("PO13") == null) {
        SCMEnv.out("未获取初始化参数PO13, 请检查...");
        return;
      }
      
      //yqq 20170113
  //    Object temp = hTemp.get("PO12");
      Object temp1 = hTemp.get("PO12");
      this.m_sDiffMode = temp.toString();

      if (hTemp.get("PO27") == null) {
        SCMEnv.out("未获取初始化参数PO27, 请检查...");
        return;
      }
      
    //yqq 20170113
  //    Object temp = hTemp.get("PO27");
      Object temp2 = hTemp.get("PO27");
      this.m_sEstPriceSource = temp.toString();

      if (hTemp.get("PO52") == null) {
        SCMEnv.out("未获取初始化参数PO52, 请检查...");
        return;
      }
      
      //yqq 20170113
 //     Object temp = hTemp.get("PO52");
      Object temp3 = hTemp.get("PO52");
      this.m_bZGYF = new UFBoolean(temp.toString());

      if (hTemp.get("BD301") == null) {
        SCMEnv.out("未获取初始化参数BD301, 请检查...");
        return;
      }
      
      //yqq 20170113
   //   Object temp = hTemp.get("BD301");
      Object temp4 = hTemp.get("BD301");
      this.m_sCurrTypeID = temp.toString();

      objs = LocalCallService.callService(scds);

      this.m_measure = ((int[])(int[])objs[0]);
      this.m_bICStartUp = ((UFBoolean)objs[1]).booleanValue();
    }
    catch (Exception e) {
      SCMEnv.out(e);
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000028"), e.getMessage());
      return;
    }
  }

  public void initQueryModel()
  {
    if (this.m_condClient == null)
    {
      this.m_condClient = new PoQueryCondition(this, NCLangRes.getInstance().getStrByID("4004050301", "UPP4004050301-000004"));
      this.m_condClient.setTempletID(this.m_sUnitCode, "4004050301", getClientEnvironment().getUser().getPrimaryKey(), null, "PS03");

      changeQueryModelLayout();
      this.m_condClient.setValueRef("dbilldate", "日历");
      this.m_condClient.setValueRef("cbillmaker", "操作员");

      UIRefPane deptRef = new UIRefPane();
      deptRef.setRefNodeName("部门档案");
      this.m_condClient.setValueRef("cdeptid", deptRef);

      UIRefPane psnRef = new UIRefPane();
      psnRef.setRefNodeName("人员档案");
      this.m_condClient.setValueRef("coperator", psnRef);

      UIRefPane vendorRef = new UIRefPane();
      vendorRef.setRefNodeName("供应商档案");
      vendorRef.getRefModel().addWherePart(" and frozenflag = 'N' ");
      this.m_condClient.setValueRef("cvendorbaseid", vendorRef);

      UIRefPane biztypeRef = new UIRefPane();
      biztypeRef.setRefNodeName("业务类型");
      this.m_condClient.setValueRef("cbiztype", biztypeRef);

      this.m_condClient.setDefaultValue("dbilldate", "dbilldate", getClientEnvironment().getDate().toString());

      DefSetTool.updateQueryConditionClientUserDef(this.m_condClient, this.m_sUnitCode, "icbill", "vuserdef", "vuserdef");
      this.m_condClient.setIsWarningWithNoInput(true);

      this.m_condClient.setSealedDataShow(true);

      this.m_condClient.setRefsDataPowerConVOs(ClientEnvironment.getInstance().getUser().getPrimaryKey(), new String[] { ClientEnvironment.getInstance().getCorporation().getPrimaryKey() }, new String[] { "供应商档案", "部门档案", "人员档案", "存货档案", "存货分类", "仓库档案", "项目管理档案" }, new String[] { "cvendorbaseid", "cdeptid", "coperator", "invcode", "cinvclassid", "cwarehouseid", "cprojectid" }, new int[] { 0, 0, 0, 0, 0, 0, 0 });
    }
  }

  public boolean isNumber(String itemKey)
  {
    itemKey = itemKey.trim();
    itemKey = itemKey.substring(0, 1);

    return itemKey.equals("n");
  }

  public void onButtonClicked(ButtonObject bo)
  {
    if (bo == this.m_buttons[0]) {
      onSelectAll();
      showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000033"));
    }

    if (bo == this.m_buttons[1]) {
      onSelectNo();
      showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000034"));
    }

    if (bo == this.m_buttons[2]) onEstimate();

    if (bo == this.m_buttons[3]) onUnEstimate();

    if (bo == this.m_buttons[4]) {
      onQuery();
      showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH009"));
    }

    if (bo == this.m_buttons[5]) {
      onPreview();
      showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000021"));
    }

    if (bo == this.m_buttons[6]) {
      onPrint();
      showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH041"));
    }

    if (bo == this.m_buttons[7]) onHQHP();
  }

  public void onEstimate()
  {
    Timer timer = new Timer();
    timer.start();

    Integer[] nSelected = null;
    Vector v = new Vector();
    Vector vv = new Vector();
    int nRow = getBillCardPanel().getRowCount();
    for (int i = 0; i < nRow; i++) {
      int nStatus = getBillCardPanel().getBillModel().getRowState(i);
      if (nStatus == 4)
        v.addElement(new Integer(i));
      else
        vv.addElement(new Integer(i));
    }
    nSelected = new Integer[v.size()];
    v.copyInto(nSelected);

    if ((nSelected == null) || (nSelected.length == 0)) {
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000030"), NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000031"));
      return;
    }

    Vector vTemp = new Vector();
    StringBuffer sMessage = new StringBuffer();
    for (int i = 0; i < nSelected.length; i++) {
      EstimateVO vo = this.m_estimate1VOs[nSelected[i].intValue()];
      vTemp.addElement(vo);

      UFDouble nInNum = vo.getNinnum();
      if ((nInNum == null) || (nInNum.doubleValue() == 0.0D)) {
        sMessage.append(vo.getVbillcode() + NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000032"));
      }
    }

    if (sMessage.length() > 0) {
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000033"), sMessage.toString());
      return;
    }

    EstimateVO[] VOs = new EstimateVO[vTemp.size()];
    vTemp.copyInto(VOs);
    try
    {
      for (int i = 0; i < VOs.length; i++) {
        VOs[i].validate();
      }
      timer.addExecutePhase("数据库更新$$$$前$$$处理时间#######");

      ArrayList paraList = new ArrayList();
      paraList.add(getClientEnvironment().getUser().getPrimaryKey());
      paraList.add(getClientEnvironment().getDate());
      paraList.add(this.m_sEstimateMode);
      paraList.add(this.m_sDiffMode);
      paraList.add(this.m_bZGYF);
      paraList.add(this.m_sCurrTypeID);

      EstimateHelper.estimate(VOs, paraList);

      timer.addExecutePhase("数据库更新$$$$操作$$$处理时间#######");
    }
    catch (SQLException e) {
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000030"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000412"));
      SCMEnv.out(e);
      return;
    } catch (ArrayIndexOutOfBoundsException e) {
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000030"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000426"));
      SCMEnv.out(e);
      return;
    } catch (NullPointerException e) {
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000030"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000427"));
      SCMEnv.out(e);
      return;
    } catch (BusinessException e) {
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000030"), e.getMessage());
      SCMEnv.out(e);
      return;
    } catch (Exception e) {
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000030"), e.getMessage());
      SCMEnv.out(e);
      return;
    }
    showHintMessage(NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000034"));

    if ((vv == null) || (vv.size() == 0))
    {
      getBillCardPanel().getBillData().clearViewData();
      getBillCardPanel().updateUI();

      for (int i = 0; i < 5; i++)
        this.m_nButtonState[i] = 1;
      this.m_nButtonState[4] = 0;
      changeButtonState();
      return;
    }

    Vector v0 = new Vector();
    for (int i = 0; i < vv.size(); i++) {
      int n = ((Integer)vv.elementAt(i)).intValue();
      v0.addElement(this.m_estimate1VOs[n]);
    }
    this.m_estimate1VOs = new EstimateVO[v0.size()];
    v0.copyInto(this.m_estimate1VOs);

    getBillCardPanel().getBillModel().setBodyDataVO(this.m_estimate1VOs);
    getBillCardPanel().getBillModel().execLoadFormula();
    getBillCardPanel().getBillModel().updateValue();
    getBillCardPanel().updateUI();

    for (int i = 0; i < 5; i++) {
      this.m_nButtonState[i] = 0;
    }
    this.m_nButtonState[1] = 1;
    this.m_nButtonState[2] = 1;
    this.m_nButtonState[3] = 1;
    this.m_nButtonState[7] = 0;
    changeButtonState();
    timer.addExecutePhase("数据库更新$$$$后$$$处理时间#######");

    timer.showAllExecutePhase("暂估处理UI时间分布：");
  }

  private void onPreview()
  {
    PrintEntry print = new PrintEntry(null, null);

    print.setTemplateID(this.m_sUnitCode, "4004050301", getClientEnvironment().getUser().getPrimaryKey(), null);
    int ret = print.selectTemplate();
    if (ret > 0) {
      print.setDataSource(this);
      print.preview();
    }
  }

  private void onPrint()
  {
    PrintEntry print = new PrintEntry(null, null);

    print.setTemplateID(this.m_sUnitCode, "4004050301", getClientEnvironment().getUser().getPrimaryKey(), null);
    int ret = print.selectTemplate();
    if (ret > 0) {
      print.setDataSource(this);
      print.print();
    }
  }

  private void onHQHP()
  {
    if ((this.m_sZG.equals("N")) && (this.m_estimate1VOs != null) && (this.m_estimate1VOs.length > 0)) {
      int[] nSelected = getBillCardPanel().getBillTable().getSelectedRows();
      if ((nSelected != null) && (nSelected.length > 0))
      {
        Vector vTemp = new Vector();
        for (int i = 0; i < nSelected.length; i++) {
          int j = nSelected[i];
          PricParaVO tempVO = new PricParaVO();
          tempVO.setCgeneralbid(this.m_estimate1VOs[j].getCgeneralbid());
          vTemp.addElement(tempVO);
        }
        PricParaVO[] VOs = new PricParaVO[vTemp.size()];
        vTemp.copyInto(VOs);
        try {
          VOs = PricStlHelper.queryPricStlPrices(VOs);
        } catch (Exception e) {
          SCMEnv.out(e);
        }

        if ((VOs != null) && (VOs.length > 0))
          for (int i = 0; i < nSelected.length; i++) {
            int j = nSelected[i];
            if (VOs[i].getNprice() != null) {
              getBillCardPanel().setBodyValueAt(VOs[i].getNprice(), j, "nprice");
              BillEditEvent event = new BillEditEvent(getBillCardPanel().getBillTable(), VOs[i].getNprice(), "nprice", j);

              computeBodyData(event);
            }
          }
      }
    }
  }

  public void onQuery()
  {
    if (!this.m_bICStartUp)
    {
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000035"), NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000036"));
      return;
    }

    initQueryModel();

    this.m_condClient.hideCorp();
    this.m_condClient.hideUnitButton();
    this.m_condClient.showModal();

    if (this.m_condClient.isCloseOK())
    {
      ConditionVO[] conditionVO = this.m_condClient.getConditionVO();

      for (int i = 0; i < 5; i++) {
        this.m_nButtonState[i] = 0;
      }
      this.m_nButtonState[1] = 1;
      this.m_nButtonState[2] = 1;
      this.m_nButtonState[3] = 1;

      if (this.m_rbEst.isSelected())
        this.m_sZG = "N";
      else {
        this.m_sZG = "Y";
      }

      try
      {
        long tTime = System.currentTimeMillis();

        if (this.m_sZG.toUpperCase().equals("N")) {
          this.m_estimate1VOs = EstimateHelper.queryEstimate(this.m_sUnitCode, conditionVO, this.m_sZG, this.m_sEstPriceSource);
          if ((this.m_estimate1VOs == null) || (this.m_estimate1VOs.length == 0)) {
            MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000035"), NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000037"));

            getBillCardPanel().getBillModel().clearBodyData();
            getBillCardPanel().updateUI();

            this.m_nButtonState[0] = 1;
            changeButtonState();
            return;
          }
          tTime = System.currentTimeMillis() - tTime;
          SCMEnv.out("入库单查询时间：" + tTime + " ms!");
        }
        else {
          this.m_estimate2VOs = EstimateHelper.queryEstimate(this.m_sUnitCode, conditionVO, this.m_sZG, this.m_sEstPriceSource);
          if ((this.m_estimate2VOs == null) || (this.m_estimate2VOs.length == 0)) {
            MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000035"), NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000037"));

            getBillCardPanel().getBillModel().clearBodyData();
            getBillCardPanel().updateUI();

            this.m_nButtonState[0] = 1;
            changeButtonState();
            return;
          }
          tTime = System.currentTimeMillis() - tTime;
          SCMEnv.out("入库单查询时间：" + tTime + " ms!");
        }

        FreeVOParse freeParse = new FreeVOParse();
        Vector vTemp = new Vector();
        if (this.m_sZG.toUpperCase().equals("N")) {
          for (int i = 0; i < this.m_estimate1VOs.length; i++) {
            if ((this.m_estimate1VOs[i].getVfree1() == null) && (this.m_estimate1VOs[i].getVfree2() == null) && (this.m_estimate1VOs[i].getVfree3() == null) && (this.m_estimate1VOs[i].getVfree4() == null) && (this.m_estimate1VOs[i].getVfree5() == null)) {
              continue;
            }
            vTemp.addElement(this.m_estimate1VOs[i]);
          }

          if (vTemp.size() > 0) {
            EstimateVO[] tempVO = new EstimateVO[vTemp.size()];
            vTemp.copyInto(tempVO);
            freeParse.setFreeVO(tempVO, "vfree", "vfree", null, "cmangid", false);
          }
        } else {
          for (int i = 0; i < this.m_estimate2VOs.length; i++) {
            if ((this.m_estimate2VOs[i].getVfree1() == null) && (this.m_estimate2VOs[i].getVfree2() == null) && (this.m_estimate2VOs[i].getVfree3() == null) && (this.m_estimate2VOs[i].getVfree4() == null) && (this.m_estimate2VOs[i].getVfree5() == null)) {
              continue;
            }
            vTemp.addElement(this.m_estimate2VOs[i]);
          }

          if (vTemp.size() > 0) {
            EstimateVO[] tempVO = new EstimateVO[vTemp.size()];
            vTemp.copyInto(tempVO);
            freeParse.setFreeVO(tempVO, "vfree", "vfree", null, "cmangid", false);
          }

        }

        vTemp = new Vector();
        if (this.m_sZG.toUpperCase().equals("N")) {
          for (int i = 0; i < this.m_estimate1VOs.length; i++) {
            this.m_estimate1VOs[i].setNdiscountrate(new UFDouble(100));
            if ((this.m_estimate1VOs[i].getCfirsttype() == null) && 
              (!vTemp.contains(this.m_estimate1VOs[i].getCbaseid()))) vTemp.addElement(this.m_estimate1VOs[i].getCbaseid());
          }
        }
        else {
          for (int i = 0; i < this.m_estimate2VOs.length; i++) {
            if ((this.m_estimate2VOs[i].getCfirsttype() == null) && 
              (!vTemp.contains(this.m_estimate2VOs[i].getCbaseid()))) vTemp.addElement(this.m_estimate2VOs[i].getCbaseid());
          }

        }

        if (vTemp.size() > 0) {
          String[] sBaseID = new String[vTemp.size()];
          vTemp.copyInto(sBaseID);
          Object oTemp = CacheTool.getColumnValue("bd_invbasdoc", "pk_invbasdoc", "pk_taxitems", sBaseID);
          if (oTemp != null) {
            Object[] oResult = (Object[])(Object[])oTemp;
            if ((oResult != null) && (oResult.length > 0)) {
              vTemp = new Vector();
              for (int i = 0; i < oResult.length; i++) {
                vTemp.addElement(oResult[i]);
              }
              if (vTemp.size() > 0) {
                String[] s = new String[vTemp.size()];
                vTemp.copyInto(s);
                oTemp = CacheTool.getColumnValue("bd_taxitems", "pk_taxitems", "taxratio", s);
                if (oTemp != null) {
                  oResult = (Object[])(Object[])oTemp;
                  Hashtable hTaxRate = new Hashtable();
                  for (int i = 0; i < sBaseID.length; i++) {
                    if (oResult[i] != null) hTaxRate.put(sBaseID[i], oResult[i]); else
                      hTaxRate.put(sBaseID[i], new UFDouble(0));
                  }
                  if (this.m_sZG.toUpperCase().equals("N")) {
                    for (int i = 0; i < this.m_estimate1VOs.length; i++)
                      if (this.m_estimate1VOs[i].getCfirsttype() == null) {
                        UFDouble nTaxRate = new UFDouble(0);
                        oTemp = hTaxRate.get(this.m_estimate1VOs[i].getCbaseid());
                        if (oTemp != null) nTaxRate = new UFDouble(oTemp.toString());
                        this.m_estimate1VOs[i].setNtaxrate(nTaxRate);
                        this.m_estimate1VOs[i].setIdiscounttaxtype(new Integer(1));
                      }
                  }
                  else {
                    for (int i = 0; i < this.m_estimate2VOs.length; i++) {
                      if (this.m_estimate2VOs[i].getCfirsttype() == null) {
                        UFDouble nTaxRate = new UFDouble(0);
                        oTemp = hTaxRate.get(this.m_estimate2VOs[i].getCbaseid());
                        if (oTemp != null) nTaxRate = new UFDouble(oTemp.toString());
                        this.m_estimate2VOs[i].setNtaxrate(nTaxRate);
                        this.m_estimate2VOs[i].setIdiscounttaxtype(new Integer(1));
                      }
                    }
                  }
                }
              }
            }
          }
        }
        if (this.m_sZG.toUpperCase().equals("N")) {
          this.m_estimate1VOs = calculateTaxPriceForEstimateVO(this.m_estimate1VOs);
        }

        getBillCardPanel().getBillModel().clearBodyData();
        if (this.m_sZG.toUpperCase().equals("N")) getBillCardPanel().getBillData().setBodyValueVO(this.m_estimate1VOs); else {
          getBillCardPanel().getBillData().setBodyValueVO(this.m_estimate2VOs);
        }
        getBillCardPanel().getBillModel().execLoadFormula();
        getBillCardPanel().updateValue();
        getBillCardPanel().updateUI();

        changeButtonState();

        boolean bSetFlag = this.m_sZG.toUpperCase().equals("N");

        getBillCardPanel().setEnabled(bSetFlag);

        if (bSetFlag)
          setPartEditable();
      }
      catch (SQLException e) {
        MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000035"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000412"));
        SCMEnv.out(e);
        return;
      } catch (ArrayIndexOutOfBoundsException e) {
        MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000035"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000426"));
        SCMEnv.out(e);
        return;
      } catch (NullPointerException e) {
        MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000035"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000427"));
        SCMEnv.out(e);
        return;
      } catch (Exception e) {
        MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000035"), e.getMessage());
        SCMEnv.out(e);
        return;
      }
    }
  }

  private EstimateVO[] calculateTaxPriceForEstimateVO(EstimateVO[] VO)
  {
    int[] descriptions = { 0, 1, 2, 7, 13, 8, 14, 11, 15, 9, 18, 12 };

    int nPricePolicy = PuTool.getPricePriorPolicy(this.m_sUnitCode);
    String sChangedKey = "nprice";

    for (int i = 0; i < VO.length; i++) {
      String s = "应税内含";
      if (VO[i].getIdiscounttaxtype().intValue() == 1) s = "应税外加";
      if (VO[i].getIdiscounttaxtype().intValue() == 2) s = "不计税";

      String[] keys = { s, "idiscounttaxtype", "ninnum", "nprice", "nmoney", "ntaxprice", "ntotalmoney", "ntaxrate", "ndiscountrate", "nnetprice", "nnettaxprice", "ntaxmoney" };

      if (VO[i].getNPricePolicy() == 5) {
        if ((VO[i].getBMoney() != null) && (VO[i].getBMoney().booleanValue())) sChangedKey = "ntotalmoney"; else
          sChangedKey = "ntaxprice";
      }
      else if ((VO[i].getBMoney() != null) && (VO[i].getBMoney().booleanValue())) sChangedKey = "nmoney"; else {
        sChangedKey = "nprice";
      }

      SCMRelationsCal.calculate(VO[i], new int[] { nPricePolicy }, sChangedKey, descriptions, keys);
    }

    return VO;
  }

  public void onSelectAll()
  {
    int nRow = getBillCardPanel().getBillModel().getRowCount();
    for (int i = 0; i < nRow; i++) {
      getBillCardPanel().getBillModel().setRowState(i, 4);
    }
    getBillCardPanel().updateUI();

    for (int i = 0; i < 5; i++) {
      this.m_nButtonState[i] = 0;
    }
    this.m_nButtonState[0] = 1;
    if (this.m_sZG.toUpperCase().equals("N")) {
      this.m_nButtonState[3] = 1;
      this.m_nButtonState[7] = 0;
    } else {
      this.m_nButtonState[2] = 1;
      this.m_nButtonState[7] = 1;
    }
    changeButtonState();
  }

  public void onSelectNo()
  {
    int nRow = getBillCardPanel().getBillModel().getRowCount();
    for (int i = 0; i < nRow; i++) {
      getBillCardPanel().getBillModel().setRowState(i, 0);
    }
    getBillCardPanel().updateUI();

    for (int i = 0; i < 5; i++) {
      this.m_nButtonState[i] = 0;
    }
    this.m_nButtonState[1] = 1;
    this.m_nButtonState[3] = 1;
    this.m_nButtonState[2] = 1;
    this.m_nButtonState[7] = 1;
    changeButtonState();
  }

  public void onUnEstimate()
  {
    Timer timerDebug = new Timer();
    timerDebug.start();

    Integer[] nSelected = null;
    Vector v = new Vector();
    Vector vv = new Vector();
    int nRow = getBillCardPanel().getRowCount();
    for (int i = 0; i < nRow; i++) {
      int nStatus = getBillCardPanel().getBillModel().getRowState(i);
      if (nStatus == 4)
        v.addElement(new Integer(i));
      else
        vv.addElement(new Integer(i));
    }
    nSelected = new Integer[v.size()];
    v.copyInto(nSelected);

    if ((nSelected == null) || (nSelected.length == 0)) {
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000038"), NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000031"));
      return;
    }
    Vector vTemp = new Vector();
    for (int i = 0; i < nSelected.length; i++) {
      EstimateVO vo = this.m_estimate2VOs[nSelected[i].intValue()];
      vTemp.addElement(vo);
    }
    EstimateVO[] VOs = new EstimateVO[vTemp.size()];
    vTemp.copyInto(VOs);

    timerDebug.addExecutePhase("组织数据");
    try
    {
      EstimateHelper.antiEstimate(VOs, getClientEnvironment().getUser().getPrimaryKey());
    } catch (SQLException e) {
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000038"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000412"));
      SCMEnv.out(e);
      return;
    } catch (ArrayIndexOutOfBoundsException e) {
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000038"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000426"));
      SCMEnv.out(e);
      return;
    } catch (NullPointerException e) {
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000038"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000427"));
      SCMEnv.out(e);
      return;
    } catch (BusinessException e) {
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000038"), e.getMessage());
      SCMEnv.out(e);
      return;
    } catch (Exception e) {
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000038"), e.getMessage());
      SCMEnv.out(e);
      return;
    }
    timerDebug.addExecutePhase("远程调用 antiEstimate(EstimateVO[],String)");

    showHintMessage(NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000039"));

    if ((vv == null) || (vv.size() == 0))
    {
      getBillCardPanel().getBillData().clearViewData();
      getBillCardPanel().updateUI();

      for (int i = 0; i < 5; i++)
        this.m_nButtonState[i] = 1;
      this.m_nButtonState[4] = 0;
      changeButtonState();
      return;
    }

    Vector v0 = new Vector();
    for (int i = 0; i < vv.size(); i++) {
      int n = ((Integer)vv.elementAt(i)).intValue();
      v0.addElement(this.m_estimate2VOs[n]);
    }
    this.m_estimate2VOs = new EstimateVO[v0.size()];
    v0.copyInto(this.m_estimate2VOs);

    getBillCardPanel().getBillModel().setBodyDataVO(this.m_estimate2VOs);
    getBillCardPanel().getBillModel().execLoadFormula();
    getBillCardPanel().getBillModel().updateValue();
    getBillCardPanel().updateUI();

    for (int i = 0; i < 5; i++) {
      this.m_nButtonState[i] = 0;
    }
    this.m_nButtonState[1] = 1;
    this.m_nButtonState[2] = 1;
    this.m_nButtonState[3] = 1;
    this.m_nButtonState[7] = 1;
    changeButtonState();

    timerDebug.addExecutePhase("取消暂估后处理(不再显示单据及按钮逻辑)");

    timerDebug.showAllExecutePhase("取消暂估UI时间分布：");
  }

  private void setPartEditable()
  {
    BillItem[] items = getBillCardPanel().getBodyShowItems();

    for (int i = 0; i < items.length; i++) {
      items[i].setEnabled(("nprice".equals(items[i].getKey().trim())) || ("nmoney".equals(items[i].getKey().trim())) || ("ntaxprice".equals(items[i].getKey().trim())) || ("ntotalmoney".equals(items[i].getKey().trim())) || ("ntaxrate".equals(items[i].getKey().trim())));

      items[i].setEdit(("nprice".equals(items[i].getKey().trim())) || ("nmoney".equals(items[i].getKey().trim())) || ("ntaxprice".equals(items[i].getKey().trim())) || ("ntotalmoney".equals(items[i].getKey().trim())) || ("ntaxrate".equals(items[i].getKey().trim())));
    }

    UIRefPane nRefPanel = (UIRefPane)getBillCardPanel().getBodyItem("nprice").getComponent();
    UITextField nPriceUI = nRefPanel.getUITextField();
    nPriceUI.setMaxLength(16);
    nPriceUI.setDelStr("-");

    nRefPanel = (UIRefPane)getBillCardPanel().getBodyItem("ntaxprice").getComponent();
    nPriceUI = nRefPanel.getUITextField();
    nPriceUI.setMaxLength(16);
    nPriceUI.setDelStr("-");
  }

  public void valueChanged(ListSelectionEvent event)
  {
    if ((ListSelectionModel)event.getSource() == getBillCardPanel().getBillTable().getSelectionModel()) {
      int nRow = getBillCardPanel().getBillModel().getRowCount();

      int[] nSelected = getBillCardPanel().getBillTable().getSelectedRows();
      for (int i = 0; i < nRow; i++) {
        getBillCardPanel().getBillModel().setRowState(i, 0);
      }

      if ((nSelected != null) && (nSelected.length > 0)) {
        for (int i = 0; i < nSelected.length; i++) {
          int j = nSelected[i];
          getBillCardPanel().getBillModel().setRowState(j, 4);
        }

      }

      for (int i = 0; i < 5; i++) {
        this.m_nButtonState[i] = 0;
      }
      if (this.m_sZG.toUpperCase().equals("N")) {
        this.m_nButtonState[3] = 1;
        this.m_nButtonState[7] = 0;
      } else {
        this.m_nButtonState[2] = 1;
        this.m_nButtonState[7] = 1;
      }

      int m = 0;
      for (int i = 0; i < getBillCardPanel().getBillModel().getRowCount(); i++) {
        if (getBillCardPanel().getBillModel().getRowState(i) == 4) {
          m++;
        }
      }
      if (m > 0) {
        this.m_nButtonState[1] = 0;
        this.m_nButtonState[7] = 0;
        if (m == nRow) this.m_nButtonState[0] = 1; else
          this.m_nButtonState[0] = 0;
      } else {
        this.m_nButtonState[0] = 0;
        this.m_nButtonState[1] = 1;
        this.m_nButtonState[7] = 1;
        if (this.m_sZG.toUpperCase().equals("N"))
          this.m_nButtonState[2] = 1;
        else
          this.m_nButtonState[3] = 1;
      }
      changeButtonState();
    }
  }

  public boolean beforeEdit(BillEditEvent e)
  {
    Object oTaxRate = getBillCardPanel().getBillModel().getValueAt(e.getRow(), "ntaxrate");
    if ((oTaxRate == null) || (oTaxRate.toString().trim().length() == 0)) {
      getBillCardPanel().getBillModel().setValueAt(new UFDouble(0), e.getRow(), "ntaxrate");
    }
    return true;
  }

  public Object[] getRelaSortObjectArray() {
    if (this.m_sZG.equals("N")) return this.m_estimate1VOs;
    return this.m_estimate2VOs;
  }
}