package nc.ui.ps.depose;

import java.awt.BorderLayout;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.ml.NCLangRes;
import nc.ui.ps.settle.SettleHelper;
import nc.ui.pu.pub.PuTool;
import nc.ui.pu.pub.PurDeptRefModel;
import nc.ui.pu.pub.PurPsnRefModel;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListData;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillMouseEnent;
import nc.ui.pub.bill.BillScrollPane;
import nc.ui.pub.bill.BillTableMouseListener;
import nc.ui.pub.bill.IBillRelaSortListener2;
import nc.ui.pub.linkoperate.ILinkAdd;
import nc.ui.pub.linkoperate.ILinkAddData;
import nc.ui.pub.linkoperate.ILinkApprove;
import nc.ui.pub.linkoperate.ILinkApproveData;
import nc.ui.pub.linkoperate.ILinkMaintain;
import nc.ui.pub.linkoperate.ILinkMaintainData;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.pub.print.IDataSource;
import nc.ui.pub.print.PrintEntry;
import nc.ui.scm.file.DocumentManager;
import nc.ui.scm.pub.bill.ButtonTree;
import nc.ui.scm.pub.print.ScmPrintTool;
import nc.ui.scm.pub.query.SCMQueryConditionDlg;
import nc.ui.scm.service.LocalCallService;
import nc.ui.scm.sourcebill.SourceBillFlowDlg;
import nc.vo.bd.CorpVO;
import nc.vo.ps.factor.CostfactorHeaderVO;
import nc.vo.ps.factor.CostfactorVO;
import nc.vo.ps.settle.SettlebillHeaderVO;
import nc.vo.ps.settle.SettlebillItemVO;
import nc.vo.ps.settle.SettlebillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.service.ServcallVO;
import nc.vo.sm.UserVO;

public class DeposeUI extends ToftPanel
  implements BillEditListener, BillTableMouseListener, IDataSource, ListSelectionListener, IBillRelaSortListener2, ILinkMaintain, ILinkAdd, ILinkApprove, ILinkQuery
{
  private ButtonTree m_bt = null;
  private ButtonObject[] m_buttons = null;

  private BillListPanel m_listPanel = null;

  private BillCardPanel m_billPanel = null;

  private String m_sUnitCode = getCorpPrimaryKey();

  private int m_nPresentRecord = 0;

  private SettlebillVO[] m_VOs = null;
  private CostfactorVO[] m_factorVOs = null;

  private SCMQueryConditionDlg m_condClient = null;

  private int m_unitDecimal = 2;

  private int m_priceDecimal = 2;

  private int m_moneyDecimal = 2;

  private ScmPrintTool m_printList = null;

  private int[] measure = null;

  IUAPQueryBS querbas = null;

  public DeposeUI()
  {
    init();
  }

  public void afterEdit(BillEditEvent event)
  {
  }

  public void bodyRowChange(BillEditEvent event)
  {
    if ((UITable)event.getSource() == getBillListPanel().getHeadTable()) {
      this.m_nPresentRecord = event.getRow();

      if (this.m_nPresentRecord >= 0)
      {
        if (isNeedFindBodys(this.m_VOs[this.m_nPresentRecord])) {
          try {
            SettlebillItemVO[] tempItemVO = 
              SettleHelper.querySettleBody(
              this.m_sUnitCode, 
              this.m_VOs[this.m_nPresentRecord].getHeadVO().getCsettlebillid(), 
              this.m_VOs[this.m_nPresentRecord].getHeadVO().getTs());
            if ((tempItemVO == null) || (tempItemVO.length == 0))
              throw new BusinessException(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000058"));
            this.m_VOs[this.m_nPresentRecord].setChildrenVO(tempItemVO);
          } catch (BusinessException e) {
            SCMEnv.out(e);
            MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("40040504", "UPP40040504-000001"), e.getMessage());
            return;
          } catch (Exception e) {
            SCMEnv.out(e);
            return;
          }
        }
        SettlebillItemVO[] bodyVO = this.m_VOs[this.m_nPresentRecord].getBodyVO();
        getBillListPanel().getBodyBillModel().setBodyDataVO(bodyVO);
        getBillListPanel().getBodyBillModel().execLoadFormula();
        getBillListPanel().getBodyBillModel().updateValue();
      }
    }
  }

  private void changeFactorShowName()
  {
    if ((this.m_factorVOs == null) || (this.m_factorVOs.length == 0)) {
      for (int i = 1; i <= 5; i++) {
        String sKey = "nfactor" + i;
        getBillCardPanel().hideBodyTableCol(sKey);
        getBillListPanel().hideBodyTableCol(sKey);
      }
      return;
    }

    Vector vName = new Vector();
    Vector vID = new Vector();
    vID.addElement(this.m_factorVOs[0].getHeadVO().getCcostfactorid().trim());
    for (int i = 1; i < this.m_factorVOs.length; i++) {
      String s1 = this.m_factorVOs[i].getHeadVO().getCcostfactorid().trim();
      if (vID.contains(s1)) continue; vID.addElement(s1);
    }

    Integer[] nCostNO = (Integer[])null;
    String[] sCostPK = (String[])null;
    if (vID.size() > 0) {
      String[] cId = new String[vID.size()];
      vID.copyInto(cId);
      try {
        Vector v = SettleHelper.queryCostfactorNO(this.m_sUnitCode, cId);
        if ((v == null) || (v.size() == 0)) return;
        Vector v1 = (Vector)v.elementAt(0);
        if ((v1 != null) && (v1.size() > 0)) {
          nCostNO = new Integer[v1.size()];
          v1.copyInto(nCostNO);
        }
        vName = (Vector)v.elementAt(1);
        Vector v2 = (Vector)v.elementAt(2);
        if ((v2 != null) && (v2.size() > 0)) {
          sCostPK = new String[v2.size()];
          v2.copyInto(sCostPK);
        }
      } catch (Exception e) {
        SCMEnv.out(e);
        MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040504", "UPP40040504-000002"), e.getMessage());
        return;
      }
    }
    if ((sCostPK == null) || (sCostPK.length == 0)) return;
    String[] sName = new String[sCostPK.length];

    for (int i = 0; i < sCostPK.length; i++) {
      String s1 = sCostPK[i].trim();
      for (int j = 0; j < vID.size(); j++) {
        String s2 = (String)vID.elementAt(j);
        s2 = s2.trim();
        if (s1.equals(s2)) {
          sName[i] = ((String)vName.elementAt(j));
        }
      }

    }

    for (int i = 1; i <= 5; i++) {
      String sKey = "nfactor" + i;
      getBillCardPanel().showBodyTableCol(sKey);
      getBillListPanel().showBodyTableCol(sKey);
    }
    if ((sName != null) && (sName.length > 0)) {
      BillItem[] items1 = getBillCardPanel().getBodyItems();
      BillItem[] items2 = getBillListPanel().getBodyBillModel().getBodyItems();
      for (int i = 0; i < sName.length; i++) {
        String sName0 = sName[i];
        String sKey = "nfactor" + (nCostNO[i].intValue() + 1);
        for (int j = 0; j < items1.length; j++) {
          String s = items1[j].getKey().trim();
          if (!s.equals(sKey)) continue; items1[j].setName(sName0);
        }
        for (int j = 0; j < items2.length; j++) {
          String s = items2[j].getKey().trim();
          if (!s.equals(sKey)) continue; items2[j].setName(sName0);
        }

      }

      BillData data1 = getBillCardPanel().getBillData();
      data1.setBodyItems(items1);

      getBillCardPanel().setBillData(data1);
      getBillCardPanel().setEnabled(false);
      getBillCardPanel().setShowThMark(true);

      BillListData data2 = getBillListPanel().getBillListData();
      data2.setBodyItems(items2);
      getBillListPanel().setListData(data2);
      getBillListPanel().setEnabled(false);
      getBillListPanel().getParentListPanel().setShowThMark(true);
      getBillListPanel().getChildListPanel().setShowThMark(true);

      this.m_listPanel.getHeadTable().setCellSelectionEnabled(false);
      this.m_listPanel.getHeadTable().setSelectionMode(2);
      this.m_listPanel.getHeadTable().getSelectionModel().addListSelectionListener(this);
    }
    else {
      BillData data1 = new BillData(getBillCardPanel().getTempletData("40040504000000000000"));

      getBillCardPanel().setBillData(data1);
      getBillCardPanel().setShowThMark(true);
    }
    getBillCardPanel().setTatolRowShow(true);

    for (int i = 0; i < 5; i++) {
      boolean b = false;
      for (int j = 0; j < nCostNO.length; j++) {
        int k = nCostNO[j].intValue();
        if (i == k) {
          b = true;
          break;
        }
      }
      if (!b) {
        getBillCardPanel().hideBodyTableCol("nfactor" + (i + 1));
        getBillListPanel().hideBodyTableCol("nfactor" + (i + 1));
      }
    }
  }

  private void delVoCard()
  {
    SettlebillVO[] settlevos = (SettlebillVO[])null;
    Vector v = new Vector();
    int delIndex = 0;
    try {
      for (int i = 0; i < this.m_VOs.length; i++) {
        if (i == this.m_nPresentRecord) {
          delIndex = i;
        }
        v.add(i, this.m_VOs[i]);
      }
      v.remove(delIndex);

      if (v.size() > 0) {
        settlevos = new SettlebillVO[v.size()];
        v.copyInto(settlevos);
        this.m_VOs = settlevos;
      } else {
        this.m_VOs = null;
      }
      if (this.m_nPresentRecord > 0)
        this.m_nPresentRecord -= 1;
      else
        this.m_nPresentRecord = 0;
    }
    catch (Exception e) {
      SCMEnv.out(e);
    }
  }

  private void delVoList(Vector v_removed)
  {
    SettlebillVO[] arrives = (SettlebillVO[])null;
    Vector v_all = new Vector();
    try {
      for (int i = 0; i < this.m_VOs.length; i++) {
        v_all.add(i, this.m_VOs[i]);
      }
      for (int i = 0; i < v_removed.size(); i++) {
        v_all.remove(v_removed.elementAt(i));
      }
      if (v_all.size() > 0) {
        arrives = new SettlebillVO[v_all.size()];
        v_all.copyInto(arrives);
        this.m_VOs = arrives;
      } else {
        this.m_VOs = null;
      }
      this.m_nPresentRecord = 0;
    } catch (Exception e) {
      reportException(e);
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000059"), NCLangRes.getInstance().getStrByID("40040504", "UPP40040504-000003"));
    }
  }

  public String[] getAllDataItemExpress()
  {
    BillItem[] headItems = getBillCardPanel().getHeadShowItems();
    BillItem[] bodyItems = getBillCardPanel().getBodyShowItems();
    BillItem[] tailItems = getBillCardPanel().getTailShowItems();

    Vector v = new Vector();
    for (int i = 0; i < headItems.length; i++) v.addElement(headItems[i].getKey());
    for (int i = 0; i < bodyItems.length; i++) v.addElement(bodyItems[i].getKey());
    for (int i = 0; i < tailItems.length; i++) v.addElement(tailItems[i].getKey());

    if (v.size() > 0) {
      String[] sKey = new String[v.size()];
      v.copyInto(sKey);
      return sKey;
    }
    return null;
  }

  public String[] getAllDataItemNames()
  {
    BillItem[] headItems = getBillCardPanel().getHeadShowItems();
    BillItem[] bodyItems = getBillCardPanel().getBodyShowItems();
    BillItem[] tailItems = getBillCardPanel().getTailShowItems();

    Vector v = new Vector();
    for (int i = 0; i < headItems.length; i++) v.addElement(headItems[i].getName());
    for (int i = 0; i < bodyItems.length; i++) v.addElement(bodyItems[i].getName());
    for (int i = 0; i < tailItems.length; i++) v.addElement(tailItems[i].getKey());

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

        BillData bd = new BillData(this.m_billPanel.getTempletData("40040504000000000000"));
        bd = initDecimal(bd);
        this.m_billPanel.setBillData(bd);
        this.m_billPanel.setTatolRowShow(true);

        this.m_billPanel.setShowThMark(true);
      }
      catch (Throwable exception) {
        SCMEnv.out(exception);
        MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040504", "UPP40040504-000004"), NCLangRes.getInstance().getStrByID("40040504", "UPP40040504-000005"));
        return null;
      }
    }
    return this.m_billPanel;
  }

  private BillListPanel getBillListPanel()
  {
    if (this.m_listPanel == null) {
      try {
        this.m_listPanel = new BillListPanel(false);
        this.m_listPanel.setName("ListPanel");
        this.m_listPanel.loadTemplet("40040504000000000000");

        BillListData bd = this.m_listPanel.getBillListData();
        bd = initListDecimal(bd);
        this.m_listPanel.setListData(bd);

        this.m_listPanel.getParentListPanel().setShowThMark(true);
        this.m_listPanel.getChildListPanel().setShowThMark(true);
        this.m_listPanel.getChildListPanel().setTotalRowShow(true);

        this.m_listPanel.addEditListener(this);
        this.m_listPanel.addMouseListener(this);

        this.m_listPanel.getHeadTable().setCellSelectionEnabled(false);
        this.m_listPanel.getHeadTable().setSelectionMode(2);
        this.m_listPanel.getHeadTable().getSelectionModel().addListSelectionListener(this);

        this.m_listPanel.getHeadBillModel().addSortRelaObjectListener2(this);

        this.m_listPanel.updateUI();
      } catch (Throwable e) {
        SCMEnv.out(e);
        MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040504", "UPP40040504-000004"), NCLangRes.getInstance().getStrByID("40040504", "UPP40040504-000005"));
        return null;
      }
    }
    return this.m_listPanel;
  }

  public String[] getDependentItemExpressByExpress(String itemName)
  {
    return null;
  }

  public String[] getItemValuesByExpress(String itemKey)
  {
    BillItem[] headItem = getBillCardPanel().getHeadShowItems();
    BillItem[] bodyItem = getBillCardPanel().getBodyShowItems();
    BillItem[] tailItem = getBillCardPanel().getTailShowItems();

    itemKey = itemKey.trim();
    int nPos = -1;
    for (int i = 0; i < headItem.length; i++) {
      String s = headItem[i].getKey().trim();
      if (itemKey.equals("h_" + s)) {
        nPos = 0;
        break;
      }
    }

    if (nPos == -1) {
      for (int i = 0; i < bodyItem.length; i++) {
        String s = bodyItem[i].getKey().trim();
        if (itemKey.equals(s)) {
          nPos = 1;
          break;
        }
      }
    }

    if (nPos == -1) {
      for (int i = 0; i < tailItem.length; i++) {
        String s = tailItem[i].getKey().trim();
        if (itemKey.equals("t_" + s)) {
          nPos = 2;
          break;
        }
      }

    }

    int nRow = getBillCardPanel().getRowCount();
    if (nPos == 0)
    {
      Object o = null;
      if ((itemKey.equals("h_vsettlebillcode")) || (itemKey.equals("h_dsettledate"))) {
        SettlebillVO VO = new SettlebillVO(nRow);
        getBillCardPanel().getBillValueVO(VO);
        SettlebillHeaderVO headVO = VO.getHeadVO();
        o = headVO.getAttributeValue(itemKey.substring(2));
      } else {
        UIRefPane nRefPanel = (UIRefPane)getBillCardPanel().getHeadItem(itemKey.substring(2)).getComponent();
        o = nRefPanel.getRefName();
      }

      String[] sValues = new String[1];
      if (o != null) sValues[0] = o.toString(); else
        sValues[0] = "";
      return sValues;
    }if (nPos == 1)
    {
      int nTotal = 0;
      if (getBillCardPanel().getBodyPanel().isTatolRow()) nTotal = 1;
      String[] sValues = new String[nRow + nTotal];
      for (int i = 0; i < nRow; i++) {
        Object o = getBillCardPanel().getBodyValueAt(i, itemKey);
        if (o != null) sValues[i] = o.toString(); else
          sValues[i] = "";
      }
      if (nTotal > 0) {
        Object o = getBillCardPanel().getTotalTableModel().getValueAt(0, getBillCardPanel().getBillModel().getBodyColByKey(itemKey));
        if ((o != null) && (o.toString().trim().length() > 0)) sValues[nRow] = o.toString(); else
          sValues[nRow] = "";
      }
      return sValues;
    }

    if (!itemKey.equals("t_coperator")) return null;
    Object o = null;
    UIRefPane nRefPanel = (UIRefPane)getBillCardPanel().getTailItem(itemKey.substring(2)).getComponent();
    o = nRefPanel.getRefName();

    String[] sValues = new String[1];
    if (o != null) sValues[0] = o.toString(); else
      sValues[0] = "";
    return sValues;
  }

  public String getModuleName()
  {
    return "40040504";
  }

  public String getTitle()
  {
    return NCLangRes.getInstance().getStrByID("40040504", "UPP40040504-000020");
  }

  public void init()
  {
    initpara();
    try
    {
      this.m_bt = new ButtonTree("40040504");
      this.m_buttons = this.m_bt.getButtonArray();
    } catch (Exception e) {
      e.printStackTrace();
    }
    setButtons(this.m_buttons);

    setLayout(new BorderLayout());
    add(getBillCardPanel(), "Center");
    getBillCardPanel().setEnabled(false);

    initConfigure();

    for (int i = 0; i < this.m_buttons.length; i++) {
      if ((this.m_buttons[i].getName().equals("查询")) || 
        (this.m_buttons[i].getName().equals("打印管理")) || 
        (this.m_buttons[i].getName().equals("卡片显示/列表显示"))) {
        this.m_buttons[i].setEnabled(true);
        for (int j = 0; j < this.m_buttons[i].getChildCount(); j++) this.m_buttons[i].getChildButtonGroup()[j].setEnabled(true); 
      }
      else {
        this.m_buttons[i].setEnabled(false);
        for (int j = 0; j < this.m_buttons[i].getChildCount(); j++) this.m_buttons[i].getChildButtonGroup()[j].setEnabled(false);
      }
    }
    this.m_bt.getButton("卡片显示/列表显示").setName("列表显示");
    updateButtons();
  }

  private void initConfigure()
  {
    changeFactorShowName();
  }

  private BillData initDecimal(BillData bd)
  {
    if ((this.measure == null) || (this.measure.length == 0)) {
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040504", "UPP40040504-000006"), NCLangRes.getInstance().getStrByID("40040504", "UPP40040504-000007"));
      return null;
    }

    this.m_unitDecimal = this.measure[0];

    this.m_priceDecimal = this.measure[1];

    this.m_moneyDecimal = this.measure[2];

    int nMeasDecimal = this.m_unitDecimal;
    int nPriceDecimal = this.m_priceDecimal;
    int nMoneyDecimal = this.m_moneyDecimal;

    BillItem item1 = bd.getBodyItem("nprice");
    item1.setDecimalDigits(nPriceDecimal);

    item1 = bd.getBodyItem("nreasonalwastprice");
    item1.setDecimalDigits(nPriceDecimal);

    BillItem item2 = bd.getBodyItem("nsettlenum");
    item2.setDecimalDigits(nMeasDecimal);

    BillItem item3 = bd.getBodyItem("nreasonalwastnum");
    item3.setDecimalDigits(nMeasDecimal);

    BillItem item4 = bd.getBodyItem("nmoney");
    item4.setDecimalDigits(nMoneyDecimal);

    item4 = bd.getBodyItem("nreasonalwastmny");
    item4.setDecimalDigits(nMoneyDecimal);

    BillItem item5 = bd.getBodyItem("nsettledisctmny");
    item5.setDecimalDigits(nMoneyDecimal);

    BillItem item6 = bd.getBodyItem("nfactor1");
    item6.setDecimalDigits(nMoneyDecimal);
    BillItem item7 = bd.getBodyItem("nfactor2");
    item7.setDecimalDigits(nMoneyDecimal);
    BillItem item8 = bd.getBodyItem("nfactor3");
    item8.setDecimalDigits(nMoneyDecimal);
    BillItem item9 = bd.getBodyItem("nfactor4");
    item9.setDecimalDigits(nMoneyDecimal);
    BillItem item10 = bd.getBodyItem("nfactor5");
    item10.setDecimalDigits(nMoneyDecimal);
    BillItem item11 = bd.getBodyItem("ngaugemny");
    item11.setDecimalDigits(nMoneyDecimal);

    return bd;
  }

  private BillListData initListDecimal(BillListData bd)
  {
    int nMeasDecimal = this.m_unitDecimal;
    int nPriceDecimal = this.m_priceDecimal;
    int nMoneyDecimal = this.m_moneyDecimal;

    BillItem item1 = bd.getBodyItem("nprice");
    item1.setDecimalDigits(nPriceDecimal);

    item1 = bd.getBodyItem("nreasonalwastprice");
    item1.setDecimalDigits(nPriceDecimal);

    BillItem item2 = bd.getBodyItem("nsettlenum");
    item2.setDecimalDigits(nMeasDecimal);

    BillItem item3 = bd.getBodyItem("nreasonalwastnum");
    item3.setDecimalDigits(nMeasDecimal);

    BillItem item4 = bd.getBodyItem("nmoney");
    item4.setDecimalDigits(nMoneyDecimal);

    item4 = bd.getBodyItem("nreasonalwastmny");
    item4.setDecimalDigits(nMoneyDecimal);

    BillItem item5 = bd.getBodyItem("nsettledisctmny");
    item5.setDecimalDigits(nMoneyDecimal);

    BillItem item6 = bd.getBodyItem("nfactor1");
    item6.setDecimalDigits(nMoneyDecimal);
    BillItem item7 = bd.getBodyItem("nfactor2");
    item7.setDecimalDigits(nMoneyDecimal);
    BillItem item8 = bd.getBodyItem("nfactor3");
    item8.setDecimalDigits(nMoneyDecimal);
    BillItem item9 = bd.getBodyItem("nfactor4");
    item9.setDecimalDigits(nMoneyDecimal);
    BillItem item10 = bd.getBodyItem("nfactor5");
    item10.setDecimalDigits(nMoneyDecimal);
    BillItem item11 = bd.getBodyItem("ngaugemny");
    item11.setDecimalDigits(nMoneyDecimal);

    return bd;
  }

  public void initpara()
  {
    try
    {
      Object[] objs = (Object[])null;
      ServcallVO[] scds = new ServcallVO[2];
      scds[0] = new ServcallVO();
      scds[0].setBeanName("nc.itf.pu.pub.IPub");
      scds[0].setMethodName("getDigitBatch");
      
      //反编译后，报错,修改与20160715  yqq  
    //scds[0].setParameter(new Object[] { this.m_sUnitCode, { "BD501", "BD505", "BD301" } });     
      scds[0].setParameter(new Object[] { this.m_sUnitCode, new String[]{ "BD501", "BD505", "BD301" } });
   
        //反编译后，报错,修改与20160715  yqq
    //scds[0].setParameterTypes(new Class[] { String.class, [Ljava.lang.String.class });
      scds[0].setParameterTypes(new Class[] { String.class, java.lang.String.class });

      scds[1] = new ServcallVO();
      scds[1].setBeanName("nc.itf.ps.factor.ICostfactor");
      scds[1].setMethodName("queryAllFactors");
      scds[1].setParameter(new Object[] { this.m_sUnitCode });
      scds[1].setParameterTypes(new Class[] { String.class });

      objs = LocalCallService.callService(scds);

      this.measure = ((int[])objs[0]);
      this.m_factorVOs = ((CostfactorVO[])objs[1]);
    }
    catch (Exception e) {
      SCMEnv.out(e);
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040504", "UPP40040504-000008"), e.getMessage());
      return;
    }
  }

  public SCMQueryConditionDlg initQueryModel(SCMQueryConditionDlg condClient, String pk_corp, boolean bLinkQuery)
  {
    condClient = new SCMQueryConditionDlg(this, NCLangRes.getInstance().getStrByID("40040504", "UPP40040504-000021"));
    try {
      condClient.setTempletID(pk_corp, "40040504", getClientEnvironment().getUser().getPrimaryKey(), null, "PS05");
    } catch (Exception e) {
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040504", "UPP40040504-000004"), NCLangRes.getInstance().getStrByID("40040504", "UPP40040504-000009"));
      return null;
    }

    condClient.setValueRef("pk_stockcorp", "公司目录");
    condClient.setValueRef("dsettledate", "日历");
    condClient.setValueRef("cinvclassid", "存货分类");
    condClient.setValueRef("coperator", "操作员");

    UIRefPane deptRef = new UIRefPane();
    deptRef.setRefModel(new PurDeptRefModel());
    condClient.setValueRef("cdeptid", deptRef);

    UIRefPane psnRef = new UIRefPane();
    psnRef.setRefModel(new PurPsnRefModel(pk_corp));
    condClient.setValueRef("cemployeeid", psnRef);

    UIRefPane vendorRef = new UIRefPane();
    vendorRef.setRefNodeName("供应商档案");
    condClient.setValueRef("cvendorbaseid", vendorRef);

    UIRefPane inventoryRef = new UIRefPane();
    inventoryRef.setRefNodeName("存货档案");
    condClient.setValueRef("cbaseid", inventoryRef);

    if (!bLinkQuery) {
      condClient.setDefaultValue("dsettledate", "dsettledate", getClientEnvironment().getDate().toString());
      condClient.setIsWarningWithNoInput(true);

      condClient.setSealedDataShow(true);
      condClient.setShowPrintStatusPanel(true);
    }

    condClient.setRefsDataPowerConVOs(ClientEnvironment.getInstance().getUser().getPrimaryKey(), 
      new String[] { ClientEnvironment.getInstance().getCorporation().getPrimaryKey() }, 
      new String[] { "供应商档案", "部门档案", "人员档案", "存货档案", "存货分类" }, 
      new String[] { "cvendorbaseid", "cdeptid", "cemployeeid", "cbaseid", "cinvclassid" }, 
      new int[5]);

    return condClient;
  }

  private boolean isNeedFindBodys(SettlebillVO vo)
  {
    if (vo == null) {
      SCMEnv.out("表头为空，返回false!");
      return false;
    }
    return (vo.getBodyVO() == null) || (vo.getBodyVO().length == 0);
  }

  public boolean isNumber(String itemKey)
  {
    itemKey = itemKey.trim();
    itemKey = itemKey.substring(0, 1);
    return itemKey.equals("n");
  }

  public void mouse_doubleclick(BillMouseEnent event)
  {
    if (event.getPos() == 0) {
      this.m_nPresentRecord = event.getRow();

      this.m_nPresentRecord = PuTool.getIndexBeforeSort(this.m_listPanel, this.m_nPresentRecord);

      this.m_bt.getButton("卡片显示/列表显示").setName("列表显示");

      if (this.m_nPresentRecord >= 0) {
        remove(getBillListPanel());
        getBillCardPanel().setVisible(true);

        for (int i = 0; i < this.m_buttons.length; i++) {
          this.m_buttons[i].setEnabled(true);
          for (int j = 0; j < this.m_buttons[i].getChildCount(); j++) this.m_buttons[i].getChildButtonGroup()[j].setEnabled(true);

          if (this.m_nPresentRecord == 0) {
            if ((this.m_buttons[i].getName().equals("首页")) || (this.m_buttons[i].getName().equals("上页"))) this.m_buttons[i].setEnabled(false); 
          }
          else if ((this.m_nPresentRecord == this.m_VOs.length - 1) && (
            (this.m_buttons[i].getName().equals("末页")) || (this.m_buttons[i].getName().equals("下页")))) this.m_buttons[i].setEnabled(false);

          if ((this.m_VOs.length != 1) || (
            (!this.m_buttons[i].getName().equals("首页")) && 
            (!this.m_buttons[i].getName().equals("上页")) && 
            (!this.m_buttons[i].getName().equals("末页")) && 
            (!this.m_buttons[i].getName().equals("下页")))) 
        	  continue;
              this.m_buttons[i].setEnabled(false);
        }

        updateButtons();

        getBillCardPanel().setBillValueVO(this.m_VOs[this.m_nPresentRecord]);
        getBillCardPanel().getBillModel().execLoadFormula();
        getBillCardPanel().updateValue();
        getBillCardPanel().updateUI();
      }
    }
  }

  private void onBillRelateQuery()
  {
    if ((this.m_VOs == null) || (this.m_VOs.length == 0)) return;

    SettlebillVO VO = this.m_VOs[this.m_nPresentRecord];
    SourceBillFlowDlg soureDlg = new SourceBillFlowDlg(this, 
      "27", 
      VO.getHeadVO().getCsettlebillid(), 
      null, 
      getClientEnvironment().getUser().getPrimaryKey(), 
      getCorpPrimaryKey());

    soureDlg.showModal();
  }

  public void onButtonClicked(ButtonObject bo)
  {
    if (bo.getName().equals("删除")) {
      showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH051"));
      if (getBillCardPanel().isVisible()) onDiscardCard(); else
        onDiscardList();
      showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH006"));
    }
    else if (bo.getName().equals("查询")) {
      if (getBillCardPanel().isVisible()) onCardQuery(); else
        onListQuery();
      showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH009"));
    }
    else if (bo.getName().equals("首页")) {
      onFirst();
      showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000026"));
    }
    else if (bo.getName().equals("上页")) {
      onPrevious();
      showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000027"));
    }
    else if (bo.getName().equals("下页")) {
      onNext();
      showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000028"));
    }
    else if (bo.getName().equals("末页")) {
      onLast();
      showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000029"));
    }
    else if (bo.getName().equals("列表显示")) { onList();
    } else if (bo.getName().equals("卡片显示")) { onSwitch();
    } else if (bo.getName().equals("打印")) {
      if (getBillCardPanel().isVisible()) onPrint(); else
        onListPrint();
      showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH041"));
    }
    else if (bo.getName().equals("预览")) {
      if (getBillCardPanel().isVisible()) onPreview(); else
        onListPreview();
      showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000021"));
    }
    else if (bo.getName().equals("文档管理")) {
      onFileManage();
      showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000025"));
    }
    else if (bo.getName().equals("联查")) {
      onBillRelateQuery();
      showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000019"));
    }
    else if (bo.getName().equals("全选")) {
      onSelectAll();
      showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000033"));
    }
    else if (bo.getName().equals("全消")) {
      onSelectNo();
      showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000034"));
    }
  }

  public void onCardQuery()
  {
    if (this.m_condClient == null) {
      this.m_condClient = initQueryModel(this.m_condClient, this.m_sUnitCode, false);
    }
    showHintMessage("");
    this.m_condClient.hideNormal();
    this.m_condClient.showModal();

    if (this.m_condClient.isCloseOK())
    {
      ConditionVO[] conditionVO = this.m_condClient.getConditionVO();

      long tTime = System.currentTimeMillis();
      try {
        this.m_VOs = SettleHelper.querySettlebill(this.m_sUnitCode, conditionVO);
        if ((this.m_VOs == null) || (this.m_VOs.length == 0))
        {
          MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("40040504", "UPP40040504-000010"), NCLangRes.getInstance().getStrByID("40040504", "UPP40040504-000011"));

          getBillCardPanel().getBillData().clearViewData();
          getBillCardPanel().updateValue();
          getBillCardPanel().updateUI();

          for (int i = 0; i < this.m_buttons.length; i++) {
            if ((this.m_buttons[i].getName().equals("查询")) || 
              (this.m_buttons[i].getName().equals("打印管理")) || 
              (this.m_buttons[i].getName().equals("列表显示"))) {
              this.m_buttons[i].setEnabled(true);
              for (int j = 0; j < this.m_buttons[i].getChildCount(); j++) this.m_buttons[i].getChildButtonGroup()[j].setEnabled(true); 
            }
            else {
              this.m_buttons[i].setEnabled(false);
              for (int j = 0; j < this.m_buttons[i].getChildCount(); j++) this.m_buttons[i].getChildButtonGroup()[j].setEnabled(false);
            }
          }
          updateButtons();
          return;
        }
        tTime = System.currentTimeMillis() - tTime;
        SCMEnv.out("查询结算单时间：" + tTime + " ms!");

        this.m_nPresentRecord = 0;

        getBillCardPanel().setBillValueVO(this.m_VOs[0]);
        getBillCardPanel().getBillModel().execLoadFormula();
        getBillCardPanel().updateUI();

        for (int i = 0; i < this.m_buttons.length; i++) {
          this.m_buttons[i].setEnabled(true);
          for (int j = 0; j < this.m_buttons[i].getChildCount(); j++) this.m_buttons[i].getChildButtonGroup()[j].setEnabled(true);
        }

        if (this.m_nPresentRecord == 0) {
          this.m_bt.getButton("首页").setEnabled(false);
          this.m_bt.getButton("上页").setEnabled(false);
        } else if (this.m_nPresentRecord == this.m_VOs.length - 1) {
          this.m_bt.getButton("末页").setEnabled(false);
          this.m_bt.getButton("下页").setEnabled(false);
        }

        if (this.m_VOs.length == 1) {
          this.m_bt.getButton("首页").setEnabled(false);
          this.m_bt.getButton("上页").setEnabled(false);
          this.m_bt.getButton("末页").setEnabled(false);
          this.m_bt.getButton("下页").setEnabled(false);
        }
        this.m_bt.getButton("全选").setEnabled(false);
        this.m_bt.getButton("全消").setEnabled(false);

        updateButtons();
      } catch (SQLException e) {
        MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040504", "UPP40040504-000010"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000412"));
        SCMEnv.out(e);
      } catch (ArrayIndexOutOfBoundsException e) {
        MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040504", "UPP40040504-000010"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000426"));
        SCMEnv.out(e);
      } catch (NullPointerException e) {
        MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040504", "UPP40040504-000010"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000427"));
        SCMEnv.out(e);
      } catch (Exception e) {
        MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040504", "UPP40040504-000010"), e.getMessage());
        SCMEnv.out(e);
      }
    }
  }

  public IUAPQueryBS getIUAPQueryBS()
  {
    if (this.querbas == null) {
      this.querbas = ((IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()));
    }
    return this.querbas;
  }

  public boolean isYFBill(SettlebillVO billvo)
  {
    SettlebillHeaderVO hvo = (SettlebillHeaderVO)billvo.getParentVO();
    if (hvo != null) {
      String csettlebillid = hvo.getPrimaryKey();
      String sql = "select nvl(isyf,'N') isyf from po_settlebill where csettlebillid='" + csettlebillid + "'";
      try {
        Object obj = getIUAPQueryBS().executeQuery(sql, new ColumnProcessor());
        UFBoolean isyf = obj == null ? new UFBoolean(false) : new UFBoolean(obj.toString());
        return isyf.booleanValue();
      }
      catch (BusinessException e)
      {
        e.printStackTrace();
        return false;
      }
    }

    return false;
  }

  public void onDiscardCard()
  {
    try
    {
      if (MessageDialog.showYesNoDlg(this, NCLangRes.getInstance().getStrByID("40040504", "UPP40040504-000017"), NCLangRes.getInstance().getStrByID("40040504", "UPP40040504-000022")) != 4) {
        return;
      }

      SettlebillVO[] vos = { this.m_VOs[this.m_nPresentRecord] };

      ArrayList listPara = new ArrayList();
      listPara.add(vos);
      listPara.add(getCorpPrimaryKey());
      listPara.add(getClientEnvironment().getUser().getPrimaryKey());
      listPara.add(this.m_factorVOs);

      if ((vos != null) && (vos.length > 0)) {
        if (isYFBill(vos[0]))
          SettleHelper.onDiscard_yf(listPara);
        else {
          SettleHelper.onDiscard(listPara);
        }

      }

      this.m_VOs[this.m_nPresentRecord].getHeadVO().setDr(new Integer(1));
      boolean bFinished = this.m_VOs.length == 1;

      if (bFinished)
      {
        this.m_VOs = null;
        getBillCardPanel().getBillData().clearViewData();
        getBillCardPanel().updateValue();
        getBillCardPanel().updateUI();

        for (int i = 0; i < this.m_buttons.length; i++) {
          if ((this.m_buttons[i].getName().equals("查询")) || 
            (this.m_buttons[i].getName().equals("打印管理")) || 
            (this.m_buttons[i].getName().equals("列表显示"))) {
            this.m_buttons[i].setEnabled(true);
            for (int j = 0; j < this.m_buttons[i].getChildCount(); j++) this.m_buttons[i].getChildButtonGroup()[j].setEnabled(true); 
          }
          else {
            this.m_buttons[i].setEnabled(false);
            for (int j = 0; j < this.m_buttons[i].getChildCount(); j++) this.m_buttons[i].getChildButtonGroup()[j].setEnabled(false);
          }
        }
        updateButtons();
        return;
      }

      delVoCard();
      String strHid = null;
      String strHts = null;
      if (isNeedFindBodys(this.m_VOs[this.m_nPresentRecord])) {
        strHid = this.m_VOs[this.m_nPresentRecord].getHeadVO().getCsettlebillid();
        strHts = this.m_VOs[this.m_nPresentRecord].getHeadVO().getTs();
        SettlebillItemVO[] items = SettleHelper.querySettleBody(this.m_sUnitCode, strHid, strHts);
        this.m_VOs[this.m_nPresentRecord].setChildrenVO(items);
      }

      getBillCardPanel().setBillValueVO(this.m_VOs[this.m_nPresentRecord]);
      getBillCardPanel().getBillModel().execLoadFormula();
      getBillCardPanel().updateUI();

      showHintMessage(NCLangRes.getInstance().getStrByID("40040504", "UPP40040504-000013"));
    } catch (Exception e) {
      showHintMessage(NCLangRes.getInstance().getStrByID("40040504", "UPP40040504-000014"));
      reportException(e);
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040504", "UPP40040504-000015"), e.getMessage());
    }
  }

  public void onDiscardList()
  {
    try
    {
      Integer[] nSelected = (Integer[])null;
      Vector v = new Vector();
      int nRow = getBillListPanel().getHeadBillModel().getRowCount();
      for (int i = 0; i < nRow; i++) {
        int nStatus = getBillListPanel().getHeadBillModel().getRowState(i);
        if (nStatus == 4)
          v.addElement(new Integer(i));
      }
      nSelected = new Integer[v.size()];
      v.copyInto(nSelected);
      if ((nSelected == null) || (nSelected.length == 0)) {
        MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040504", "UPP40040504-000017"), NCLangRes.getInstance().getStrByID("40040504", "UPP40040504-000018"));
        return;
      }
      if (MessageDialog.showYesNoDlg(this, NCLangRes.getInstance().getStrByID("40040504", "UPP40040504-000017"), NCLangRes.getInstance().getStrByID("40040504", "UPP40040504-000022")) != 4) {
        return;
      }
      Vector vAllDelVo = new Vector();
      int iCntSel = nSelected.length;
      for (int i = 0; i < iCntSel; i++) {
        vAllDelVo.addElement(this.m_VOs[nSelected[i].intValue()]);
      }
      SettlebillVO[] vos = new SettlebillVO[iCntSel];
      vAllDelVo.copyInto(vos);

      if ((vos != null) && (vos.length > 0)) {
        ArrayList vos_norm = new ArrayList();
        ArrayList vos_yf = new ArrayList();

        for (int i = 0; i < vos.length; i++) {
          if (isYFBill(vos[i]))
            vos_yf.add(vos[i]);
          else {
            vos_norm.add(vos[i]);
          }
        }
        if ((vos_norm != null) && (vos_norm.size() > 0)) {
          ArrayList listPara = new ArrayList();
          listPara.add((SettlebillVO[])vos_norm.toArray(new SettlebillVO[0]));
          listPara.add(getCorpPrimaryKey());
          listPara.add(getClientEnvironment().getUser().getPrimaryKey());
          listPara.add(this.m_factorVOs);
          SettleHelper.onDiscard(listPara);
        }

        if ((vos_yf != null) && (vos_yf.size() > 0)) {
          ArrayList listPara = new ArrayList();
          listPara.add((SettlebillVO[])vos_yf.toArray(new SettlebillVO[0]));
          listPara.add(getCorpPrimaryKey());
          listPara.add(getClientEnvironment().getUser().getPrimaryKey());
          listPara.add(this.m_factorVOs);
          SettleHelper.onDiscard_yf(listPara);
        }

      }

      for (int i = 0; i < iCntSel; i++) {
        this.m_VOs[nSelected[i].intValue()].getHeadVO().setDr(new Integer(1));
      }
      boolean bFinished = iCntSel == this.m_VOs.length;

      if (bFinished)
      {
        this.m_VOs = null;
        getBillListPanel().getHeadBillModel().clearBodyData();
        getBillListPanel().getBodyBillModel().clearBodyData();
        getBillListPanel().updateUI();
        setBtnsStateList();
        return;
      }

      delVoList(vAllDelVo);
      int jLen = this.m_VOs == null ? 0 : this.m_VOs.length;
      SettlebillHeaderVO[] headVos = new SettlebillHeaderVO[jLen];
      for (int j = 0; j < jLen; j++) {
        headVos[j] = this.m_VOs[j].getHeadVO();
      }
      String strHid = null;
      String strHts = null;
      if (isNeedFindBodys(this.m_VOs[this.m_nPresentRecord])) {
        strHid = this.m_VOs[this.m_nPresentRecord].getHeadVO().getCsettlebillid();
        strHts = this.m_VOs[this.m_nPresentRecord].getHeadVO().getTs();
        SettlebillItemVO[] items = SettleHelper.querySettleBody(this.m_sUnitCode, strHid, strHts);
        this.m_VOs[this.m_nPresentRecord].setChildrenVO(items);
      }

      SettlebillItemVO[] bodyVos = this.m_VOs[this.m_nPresentRecord].getBodyVO();
      getBillListPanel().getHeadBillModel().setBodyDataVO(headVos);
      getBillListPanel().getHeadBillModel().execLoadFormula();
      getBillListPanel().getHeadBillModel().updateValue();
      getBillListPanel().getBodyBillModel().setBodyDataVO(bodyVos);
      getBillListPanel().getBodyBillModel().execLoadFormula();
      getBillListPanel().getBodyBillModel().updateValue();
      getBillListPanel().updateUI();

      onSelectNo();

      setBtnsStateList();
      showHintMessage(NCLangRes.getInstance().getStrByID("40040504", "UPP40040504-000013"));
    } catch (Exception e) {
      showHintMessage(NCLangRes.getInstance().getStrByID("40040504", "UPP40040504-000014"));
      reportException(e);
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040504", "UPP40040504-000015"), e.getMessage());
    }
  }

  private void onFileManage()
  {
    Integer[] nSelected = (Integer[])null;
    boolean b = getBillCardPanel().isVisible();
    if (!b) {
      Vector v = new Vector();
      int nRow = getBillListPanel().getHeadBillModel().getRowCount();
      for (int i = 0; i < nRow; i++) {
        int nStatus = getBillListPanel().getHeadBillModel().getRowState(i);
        if (nStatus != 4) continue; v.addElement(new Integer(i));
      }
      nSelected = new Integer[v.size()];
      v.copyInto(nSelected);
    } else {
      nSelected = new Integer[1];
      nSelected[0] = new Integer(this.m_nPresentRecord);
    }

    if ((nSelected == null) || (nSelected.length == 0)) return;

    String[] sBillID = new String[nSelected.length];
    String[] sBillCode = new String[nSelected.length];

    for (int i = 0; i < sBillID.length; i++) {
      int j = nSelected[i].intValue();
      if ((j >= 0) && (j < this.m_VOs.length)) {
        sBillID[i] = this.m_VOs[j].getHeadVO().getCsettlebillid();
        sBillCode[i] = this.m_VOs[j].getHeadVO().getVsettlebillcode();
      }
    }

    if ((sBillID != null) && (sBillID.length > 0))
      DocumentManager.showDM(this, sBillID, sBillCode);
  }

  public void onFirst()
  {
    showHintMessage("");
    getBillCardPanel().getBillModel().clearBodyData();
    this.m_nPresentRecord = 0;

    this.m_bt.getButton("首页").setEnabled(false);
    this.m_bt.getButton("上页").setEnabled(false);
    this.m_bt.getButton("末页").setEnabled(true);
    this.m_bt.getButton("下页").setEnabled(true);

    updateButtons();

    if (isNeedFindBodys(this.m_VOs[this.m_nPresentRecord])) {
      try {
        SettlebillItemVO[] tempItemVO = 
          SettleHelper.querySettleBody(
          this.m_sUnitCode, 
          this.m_VOs[this.m_nPresentRecord].getHeadVO().getCsettlebillid(), 
          this.m_VOs[this.m_nPresentRecord].getHeadVO().getTs());
        if ((tempItemVO == null) || (tempItemVO.length == 0))
          throw new BusinessException(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000058"));
        this.m_VOs[this.m_nPresentRecord].setChildrenVO(tempItemVO);
      } catch (BusinessException e) {
        SCMEnv.out(e);
        MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("40040504", "UPP40040504-000001"), e.getMessage());
        return;
      } catch (Exception e) {
        SCMEnv.out(e);
      }

    }

    getBillCardPanel().setBillValueVO(this.m_VOs[0]);
    getBillCardPanel().getBillModel().execLoadFormula();
    getBillCardPanel().updateValue();

    getBillCardPanel().updateUI();
  }

  public void onLast()
  {
    showHintMessage("");
    getBillCardPanel().getBillModel().clearBodyData();
    this.m_nPresentRecord = (this.m_VOs.length - 1);

    this.m_bt.getButton("首页").setEnabled(true);
    this.m_bt.getButton("上页").setEnabled(true);
    this.m_bt.getButton("末页").setEnabled(false);
    this.m_bt.getButton("下页").setEnabled(false);

    updateButtons();

    if (isNeedFindBodys(this.m_VOs[this.m_nPresentRecord])) {
      try {
        SettlebillItemVO[] tempItemVO = 
          SettleHelper.querySettleBody(
          this.m_sUnitCode, 
          this.m_VOs[this.m_nPresentRecord].getHeadVO().getCsettlebillid(), 
          this.m_VOs[this.m_nPresentRecord].getHeadVO().getTs());
        if ((tempItemVO == null) || (tempItemVO.length == 0))
          throw new BusinessException(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000058"));
        this.m_VOs[this.m_nPresentRecord].setChildrenVO(tempItemVO);
      } catch (BusinessException e) {
        SCMEnv.out(e);
        MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("40040504", "UPP40040504-000001"), e.getMessage());
        return;
      } catch (Exception e) {
        SCMEnv.out(e);
      }

    }

    getBillCardPanel().setBillValueVO(this.m_VOs[this.m_nPresentRecord]);
    getBillCardPanel().getBillModel().execLoadFormula();
    getBillCardPanel().updateValue();

    getBillCardPanel().updateUI();
  }

  public void onList()
  {
    showHintMessage("");
    getBillCardPanel().setVisible(false);
    add(getBillListPanel(), "Center");
    getBillListPanel().setEnabled(false);

    getBillListPanel().hideHeadTableCol("cbiztype");
    getBillListPanel().hideHeadTableCol("cvendormangid");
    getBillListPanel().hideHeadTableCol("cdeptid");
    getBillListPanel().hideHeadTableCol("cemployeeid");
    getBillListPanel().hideHeadTableCol("coperator");

    this.m_bt.getButton("卡片显示/列表显示").setName("卡片显示");
    if ((this.m_VOs != null) && (this.m_VOs.length > 0))
    {
      Vector v = new Vector();
      for (int i = 0; i < this.m_VOs.length; i++) v.addElement(this.m_VOs[i].getHeadVO());
      SettlebillHeaderVO[] hVO = new SettlebillHeaderVO[v.size()];
      v.copyInto(hVO);

      SettlebillItemVO[] bVO = this.m_VOs[this.m_nPresentRecord].getBodyVO();

      getBillListPanel().getHeadBillModel().setBodyDataVO(hVO);
      getBillListPanel().getHeadBillModel().execLoadFormula();
      getBillListPanel().getBodyBillModel().setBodyDataVO(bVO);
      getBillListPanel().getBodyBillModel().execLoadFormula();
      getBillListPanel().getHeadBillModel().updateValue();
      getBillListPanel().getBodyBillModel().updateValue();
      getBillListPanel().updateUI();

      getBillListPanel().getHeadBillModel().setRowState(this.m_nPresentRecord, 4);

      for (int i = 0; i < this.m_buttons.length; i++) {
        this.m_buttons[i].setEnabled(true);
        for (int j = 0; j < this.m_buttons[i].getChildCount(); j++) this.m_buttons[i].getChildButtonGroup()[j].setEnabled(true);
      }
      this.m_bt.getButton("首页").setEnabled(false);
      this.m_bt.getButton("上页").setEnabled(false);
      this.m_bt.getButton("下页").setEnabled(false);
      this.m_bt.getButton("末页").setEnabled(false);
    } else {
      getBillListPanel().getHeadBillModel().clearBodyData();
      getBillListPanel().getBodyBillModel().clearBodyData();
      getBillListPanel().updateUI();

      for (int i = 0; i < this.m_buttons.length; i++) {
        if ((this.m_buttons[i].getName().equals("查询")) || 
          (this.m_buttons[i].getName().equals("卡片显示"))) {
          this.m_buttons[i].setEnabled(true);
          for (int j = 0; j < this.m_buttons[i].getChildCount(); j++) this.m_buttons[i].getChildButtonGroup()[j].setEnabled(true); 
        }
        else {
          this.m_buttons[i].setEnabled(false);
          for (int j = 0; j < this.m_buttons[i].getChildCount(); j++) this.m_buttons[i].getChildButtonGroup()[j].setEnabled(false);
        }
      }
    }

    updateButtons();
  }

  private void onListPreview()
  {
    showHintMessage("");
    Integer[] nSelected = (Integer[])null;
    Vector v = new Vector();
    int nRow = getBillListPanel().getHeadBillModel().getRowCount();
    for (int i = 0; i < nRow; i++) {
      int nStatus = getBillListPanel().getHeadBillModel().getRowState(i);
      if (nStatus == 4)
        v.addElement(new Integer(i));
    }
    nSelected = new Integer[v.size()];
    v.copyInto(nSelected);

    if ((nSelected == null) || (nSelected.length == 0)) {
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040504", "UPP40040504-000019"), NCLangRes.getInstance().getStrByID("40040504", "UPP40040504-000018"));
      return;
    }

    try
    {
      if ((this.m_VOs != null) && (this.m_VOs.length > 0)) {
        Vector v1 = new Vector();
        Vector v2 = new Vector();
        int iLen = this.m_VOs.length;
        for (int i = 0; i < iLen; i++) {
          int nStatus = getBillListPanel().getHeadBillModel().getRowState(i);
          if ((isNeedFindBodys(this.m_VOs[i])) && (nStatus == 4)) {
            v1.addElement(this.m_VOs[i].getHeadVO().getCsettlebillid());
            v2.addElement(this.m_VOs[i].getHeadVO().getTs());
          }
        }
        if ((v1.size() > 0) && (v2.size() > 0)) {
          String[] headKey = new String[v1.size()];
          v1.copyInto(headKey);
          String[] ts = new String[v2.size()];
          v2.copyInto(ts);
          ArrayList list = SettleHelper.querySettleBodyBatch(this.m_sUnitCode, headKey, ts, new UFBoolean(true));
          if ((list == null) || (list.size() == 0))
            throw new BusinessException(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000058"));
          int j = 0;
          for (int i = 0; i < iLen; i++) {
            int nStatus = getBillListPanel().getHeadBillModel().getRowState(i);
            if ((isNeedFindBodys(this.m_VOs[i])) && (nStatus == 4)) {
              SettlebillItemVO[] tempBodyVO = (SettlebillItemVO[])list.get(j);
              this.m_VOs[i].setChildrenVO(tempBodyVO);

              j++;
            }
          }
        }
      }
    } catch (BusinessException e) {
      SCMEnv.out(e);
      MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("40040504", "UPP40040504-000001"), e.getMessage());
      return;
    } catch (Exception e) {
      SCMEnv.out(e);
      return;
    }

    ArrayList list = new ArrayList();
    for (int i = 0; i < nSelected.length; i++) {
      SettlebillVO vo = this.m_VOs[nSelected[i].intValue()];
      list.add(vo);
    }

    if (list.size() > 0) {
      if (this.m_printList == null) {
        this.m_printList = new ScmPrintTool(getBillCardPanel(), list, getModuleCode());
      }
      this.m_printList.preview();
    }
  }

  private void onListPrint()
  {
    showHintMessage("");
    Integer[] nSelected = (Integer[])null;
    Vector v = new Vector();
    int nRow = getBillListPanel().getHeadBillModel().getRowCount();
    for (int i = 0; i < nRow; i++) {
      int nStatus = getBillListPanel().getHeadBillModel().getRowState(i);
      if (nStatus == 4)
        v.addElement(new Integer(i));
    }
    nSelected = new Integer[v.size()];
    v.copyInto(nSelected);

    if ((nSelected == null) || (nSelected.length == 0)) {
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040504", "UPP40040504-000019"), NCLangRes.getInstance().getStrByID("40040504", "UPP40040504-000018"));
      return;
    }

    try
    {
      if ((this.m_VOs != null) && (this.m_VOs.length > 0)) {
        Vector v1 = new Vector();
        Vector v2 = new Vector();
        int iLen = this.m_VOs.length;
        for (int i = 0; i < iLen; i++) {
          int nStatus = getBillListPanel().getHeadBillModel().getRowState(i);
          if ((isNeedFindBodys(this.m_VOs[i])) && (nStatus == 4)) {
            v1.addElement(this.m_VOs[i].getHeadVO().getCsettlebillid());
            v2.addElement(this.m_VOs[i].getHeadVO().getTs());
          }
        }
        if ((v1.size() > 0) && (v2.size() > 0)) {
          String[] headKey = new String[v1.size()];
          v1.copyInto(headKey);
          String[] ts = new String[v2.size()];
          v2.copyInto(ts);
          ArrayList list = SettleHelper.querySettleBodyBatch(this.m_sUnitCode, headKey, ts, new UFBoolean(true));
          if ((list == null) || (list.size() == 0))
            throw new BusinessException(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000058"));
          int j = 0;
          for (int i = 0; i < this.m_VOs.length; i++) {
            int nStatus = getBillListPanel().getHeadBillModel().getRowState(i);
            if ((isNeedFindBodys(this.m_VOs[i])) && (nStatus == 4)) {
              SettlebillItemVO[] tempBodyVO = (SettlebillItemVO[])list.get(j);
              this.m_VOs[i].setChildrenVO(tempBodyVO);

              j++;
            }
          }
        }
      }
    } catch (BusinessException e) {
      SCMEnv.out(e);
      MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("40040504", "UPP40040504-000001"), e.getMessage());
      return;
    } catch (Exception e) {
      SCMEnv.out(e);
      return;
    }

    ArrayList list = new ArrayList();
    for (int i = 0; i < nSelected.length; i++) {
      SettlebillVO vo = this.m_VOs[nSelected[i].intValue()];
      list.add(vo);
    }

    if (list.size() > 0) {
      if (this.m_printList == null) {
        this.m_printList = new ScmPrintTool(getBillCardPanel(), list, getModuleCode());
      }
      this.m_printList.print();
    }
  }

  public void onListQuery()
  {
    if (this.m_condClient == null) this.m_condClient = initQueryModel(this.m_condClient, this.m_sUnitCode, false);

    this.m_condClient.hideNormal();
    this.m_condClient.showModal();

    if (this.m_condClient.isCloseOK())
    {
      ConditionVO[] conditionVO = this.m_condClient.getConditionVO();

      long tTime = System.currentTimeMillis();
      try {
        this.m_VOs = SettleHelper.querySettlebill(this.m_sUnitCode, conditionVO);
        if ((this.m_VOs == null) || (this.m_VOs.length == 0))
        {
          MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("40040504", "UPP40040504-000010"), NCLangRes.getInstance().getStrByID("40040504", "UPP40040504-000011"));

          getBillListPanel().getHeadBillModel().clearBodyData();
          getBillListPanel().getBodyBillModel().clearBodyData();
          getBillListPanel().updateUI();

          setBtnsStateList();

          return;
        }
        tTime = System.currentTimeMillis() - tTime;
        SCMEnv.out("查询结算单时间：" + tTime + " ms!");

        Vector v = new Vector();
        SettlebillHeaderVO[] headVO = (SettlebillHeaderVO[])null;
        for (int i = 0; i < this.m_VOs.length; i++)
          v.addElement(this.m_VOs[i].getHeadVO());
        if (v.size() > 0) {
          headVO = new SettlebillHeaderVO[v.size()];
          v.copyInto(headVO);
        }

        SettlebillItemVO[] bodyVO = this.m_VOs[0].getBodyVO();
        getBillListPanel().getHeadBillModel().setBodyDataVO(headVO);
        getBillListPanel().getHeadBillModel().execLoadFormula();
        getBillListPanel().getBodyBillModel().setBodyDataVO(bodyVO);
        getBillListPanel().getBodyBillModel().execLoadFormula();
        getBillListPanel().getHeadBillModel().updateValue();
        getBillListPanel().getBodyBillModel().updateValue();
        getBillListPanel().updateUI();

        onSelectNo();

        setBtnsStateList();
      } catch (SQLException e) {
        MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040504", "UPP40040504-000010"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000412"));
        SCMEnv.out(e);
      } catch (ArrayIndexOutOfBoundsException e) {
        MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040504", "UPP40040504-000010"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000426"));
        SCMEnv.out(e);
      } catch (NullPointerException e) {
        MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040504", "UPP40040504-000010"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000427"));
        SCMEnv.out(e);
      } catch (Exception e) {
        MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040504", "UPP40040504-000010"), e.getMessage());
        SCMEnv.out(e);
      }
    }
  }

  public void onNext()
  {
    showHintMessage("");
    getBillCardPanel().getBillModel().clearBodyData();

    this.m_bt.getButton("首页").setEnabled(true);
    this.m_bt.getButton("上页").setEnabled(true);
    if (this.m_nPresentRecord == this.m_VOs.length - 2) {
      this.m_bt.getButton("末页").setEnabled(false);
      this.m_bt.getButton("下页").setEnabled(false);
    }

    updateButtons();

    this.m_nPresentRecord += 1;

    if (isNeedFindBodys(this.m_VOs[this.m_nPresentRecord])) {
      try {
        SettlebillItemVO[] tempItemVO = 
          SettleHelper.querySettleBody(
          this.m_sUnitCode, 
          this.m_VOs[this.m_nPresentRecord].getHeadVO().getCsettlebillid(), 
          this.m_VOs[this.m_nPresentRecord].getHeadVO().getTs());
        if ((tempItemVO == null) || (tempItemVO.length == 0))
          throw new BusinessException(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000058"));
        this.m_VOs[this.m_nPresentRecord].setChildrenVO(tempItemVO);
      } catch (BusinessException e) {
        SCMEnv.out(e);
        MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("40040504", "UPP40040504-000001"), e.getMessage());
        return;
      } catch (Exception e) {
        SCMEnv.out(e);
      }

    }

    getBillCardPanel().setBillValueVO(this.m_VOs[this.m_nPresentRecord]);
    getBillCardPanel().getBillModel().execLoadFormula();
    getBillCardPanel().updateValue();

    getBillCardPanel().updateUI();
  }

  public void onPreview()
  {
    showHintMessage("");

    PrintEntry print = new PrintEntry(null, null);
    print.setTemplateID(this.m_sUnitCode, "40040504", getClientEnvironment().getUser().getPrimaryKey(), null);
    int ret = print.selectTemplate();
    if (ret > 0) {
      print.setDataSource(this);
      print.preview();
    }
  }

  public void onPrevious()
  {
    showHintMessage("");
    getBillCardPanel().getBillModel().clearBodyData();

    if (this.m_nPresentRecord == 1) {
      this.m_bt.getButton("首页").setEnabled(false);
      this.m_bt.getButton("上页").setEnabled(false);
    }
    this.m_bt.getButton("末页").setEnabled(true);
    this.m_bt.getButton("下页").setEnabled(true);

    updateButtons();

    this.m_nPresentRecord -= 1;

    if (isNeedFindBodys(this.m_VOs[this.m_nPresentRecord])) {
      try {
        SettlebillItemVO[] tempItemVO = 
          SettleHelper.querySettleBody(
          this.m_sUnitCode, 
          this.m_VOs[this.m_nPresentRecord].getHeadVO().getCsettlebillid(), 
          this.m_VOs[this.m_nPresentRecord].getHeadVO().getTs());
        if ((tempItemVO == null) || (tempItemVO.length == 0))
          throw new BusinessException(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000058"));
        this.m_VOs[this.m_nPresentRecord].setChildrenVO(tempItemVO);
      } catch (BusinessException e) {
        SCMEnv.out(e);
        MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("40040504", "UPP40040504-000001"), e.getMessage());
        return;
      } catch (Exception e) {
        SCMEnv.out(e);
      }

    }

    getBillCardPanel().setBillValueVO(this.m_VOs[this.m_nPresentRecord]);
    getBillCardPanel().getBillModel().execLoadFormula();
    getBillCardPanel().updateValue();

    getBillCardPanel().updateUI();
  }

  public void onPrint()
  {
    showHintMessage("");

    PrintEntry print = new PrintEntry(null, null);
    print.setTemplateID(this.m_sUnitCode, "40040504", getClientEnvironment().getUser().getPrimaryKey(), null);
    int ret = print.selectTemplate();
    if (ret > 0) {
      print.setDataSource(this);
      print.print();
    }
  }

  public void onSelectAll()
  {
    int iLen = getBillListPanel().getHeadBillModel().getRowCount();
    getBillListPanel().getHeadTable().setRowSelectionInterval(0, iLen - 1);
    for (int i = 0; i < iLen; i++) {
      getBillListPanel().getHeadBillModel().setRowState(i, 4);
      if (!isNeedFindBodys(this.m_VOs[i])) continue;
      try {
        SettlebillItemVO[] tempItemVO = 
          SettleHelper.querySettleBody(
          this.m_sUnitCode, 
          this.m_VOs[i].getHeadVO().getCsettlebillid(), 
          this.m_VOs[i].getHeadVO().getTs());
        if ((tempItemVO == null) || (tempItemVO.length == 0))
          throw new BusinessException(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000058"));
        this.m_VOs[i].setChildrenVO(tempItemVO);
      } catch (BusinessException e) {
        SCMEnv.out(e);
        MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("40040504", "UPP40040504-000001"), e.getMessage());
        return;
      } catch (Exception e) {
        SCMEnv.out(e);
        return;
      }

    }

    setBtnsStateList();
  }

  public void onSelectNo()
  {
    int iLen = getBillListPanel().getHeadBillModel().getRowCount();
    getBillListPanel().getHeadTable().removeRowSelectionInterval(0, iLen - 1);
    for (int i = 0; i < iLen; i++) {
      getBillListPanel().getHeadBillModel().setRowState(i, 0);
    }

    setBtnsStateList();
  }

  public void onSwitch()
  {
    this.m_nPresentRecord = PuTool.getIndexBeforeSort(this.m_listPanel, this.m_nPresentRecord);

    remove(getBillListPanel());
    getBillCardPanel().setVisible(true);

    this.m_bt.getButton("卡片显示/列表显示").setName("列表显示");

    if ((this.m_VOs == null) || (this.m_VOs.length == 0)) {
      getBillCardPanel().getBillData().clearViewData();
      getBillCardPanel().updateUI();

      for (int i = 0; i < this.m_buttons.length; i++) {
        if ((this.m_buttons[i].getName().equals("查询")) || 
          (this.m_buttons[i].getName().equals("打印管理")) || 
          (this.m_buttons[i].getName().equals("列表显示"))) {
          this.m_buttons[i].setEnabled(true);
          for (int j = 0; j < this.m_buttons[i].getChildCount(); j++) this.m_buttons[i].getChildButtonGroup()[j].setEnabled(true); 
        }
        else {
          this.m_buttons[i].setEnabled(false);
          for (int j = 0; j < this.m_buttons[i].getChildCount(); j++) this.m_buttons[i].getChildButtonGroup()[j].setEnabled(false);
        }
      }
      updateButtons();
      return;
    }

    for (int i = 0; i < getBillListPanel().getHeadBillModel().getRowCount(); i++) {
      if (getBillListPanel().getHeadBillModel().getRowState(i) == 4) {
        this.m_nPresentRecord = i;
        break;
      }
    }

    if (this.m_nPresentRecord >= 0)
    {
      for (int i = 0; i < this.m_buttons.length; i++) {
        this.m_buttons[i].setEnabled(true);
        for (int j = 0; j < this.m_buttons[i].getChildCount(); j++) this.m_buttons[i].getChildButtonGroup()[j].setEnabled(true);
      }

      if (this.m_nPresentRecord == 0) {
        this.m_bt.getButton("首页").setEnabled(false);
        this.m_bt.getButton("上页").setEnabled(false);
      } else if (this.m_nPresentRecord == this.m_VOs.length - 1) {
        this.m_bt.getButton("末页").setEnabled(false);
        this.m_bt.getButton("下页").setEnabled(false);
      }

      if (this.m_VOs.length == 1) {
        this.m_bt.getButton("首页").setEnabled(false);
        this.m_bt.getButton("上页").setEnabled(false);
        this.m_bt.getButton("末页").setEnabled(false);
        this.m_bt.getButton("下页").setEnabled(false);
      }
      this.m_bt.getButton("全选").setEnabled(false);
      this.m_bt.getButton("全消").setEnabled(false);

      updateButtons();

      getBillCardPanel().setBillValueVO(this.m_VOs[this.m_nPresentRecord]);
      getBillCardPanel().getBillModel().execLoadFormula();
      getBillCardPanel().updateValue();
      getBillCardPanel().updateUI();
    }
  }

  private void setBtnsStateList()
  {
    int iHCnt = getBillListPanel().getHeadBillModel().getRowCount();
    int iHSelCnt = 0;
    for (int i = 0; i < iHCnt; i++) {
      if (4 == getBillListPanel().getHeadBillModel().getRowState(i)) {
        iHSelCnt++;
      }
    }
    if (iHCnt <= 0) {
      for (int i = 0; i < this.m_buttons.length; i++) {
        if ((this.m_buttons[i].getName().equals("查询")) || 
          (this.m_buttons[i].getName().equals("卡片显示"))) {
          this.m_buttons[i].setEnabled(true);
          for (int j = 0; j < this.m_buttons[i].getChildCount(); j++) this.m_buttons[i].getChildButtonGroup()[j].setEnabled(true); 
        }
        else {
          this.m_buttons[i].setEnabled(false);
          for (int j = 0; j < this.m_buttons[i].getChildCount(); j++) this.m_buttons[i].getChildButtonGroup()[j].setEnabled(false);
        }
      }
      updateButtons();
      return;
    }

    for (int i = 0; i < this.m_buttons.length; i++) {
      this.m_buttons[i].setEnabled(true);
      for (int j = 0; j < this.m_buttons[i].getChildCount(); j++) this.m_buttons[i].getChildButtonGroup()[j].setEnabled(true);
    }
    this.m_bt.getButton("首页").setEnabled(false);
    this.m_bt.getButton("上页").setEnabled(false);
    this.m_bt.getButton("下页").setEnabled(false);
    this.m_bt.getButton("末页").setEnabled(false);

    if (iHCnt == iHSelCnt)
      this.m_bt.getButton("全选").setEnabled(false);
    else {
      this.m_bt.getButton("全选").setEnabled(true);
    }

    if (iHSelCnt > 0)
      this.m_bt.getButton("全消").setEnabled(true);
    else {
      this.m_bt.getButton("全消").setEnabled(false);
    }

    if (iHSelCnt > 0)
      this.m_bt.getButton("删除").setEnabled(true);
    else {
      this.m_bt.getButton("删除").setEnabled(false);
    }

    this.m_bt.getButton("查询").setEnabled(true);

    if (iHSelCnt == 1) {
      this.m_bt.getButton("卡片显示/列表显示").setEnabled(true);
      this.m_bt.getButton("联查").setEnabled(true);
    } else {
      this.m_bt.getButton("卡片显示/列表显示").setEnabled(false);
      this.m_bt.getButton("联查").setEnabled(false);
    }

    if (iHSelCnt > 0) {
      this.m_bt.getButton("文档管理").setEnabled(true);
      this.m_bt.getButton("打印").setEnabled(true);
      this.m_bt.getButton("预览").setEnabled(true);
    } else {
      this.m_bt.getButton("文档管理").setEnabled(false);
      this.m_bt.getButton("打印").setEnabled(false);
      this.m_bt.getButton("预览").setEnabled(false);
    }

    this.m_buttons = this.m_bt.getButtonArray();
    setButtons(this.m_buttons);
    updateButtons();
  }

  public void valueChanged(ListSelectionEvent event)
  {
    if ((ListSelectionModel)event.getSource() == getBillListPanel().getHeadTable().getSelectionModel()) {
      int nRow = getBillListPanel().getHeadBillModel().getRowCount();

      for (int i = 0; i < nRow; i++) {
        getBillListPanel().getHeadBillModel().setRowState(i, 0);
      }

      int[] nSelected = getBillListPanel().getHeadTable().getSelectedRows();
      if ((nSelected != null) && (nSelected.length > 0)) {
        for (int i = 0; i < nSelected.length; i++) {
          int j = nSelected[i];
          if (j < nRow) {
            getBillListPanel().getHeadBillModel().setRowState(j, 4);
            if (!isNeedFindBodys(this.m_VOs[j])) continue;
            try {
              SettlebillItemVO[] tempItemVO = 
                SettleHelper.querySettleBody(
                this.m_sUnitCode, 
                this.m_VOs[j].getHeadVO().getCsettlebillid(), 
                this.m_VOs[j].getHeadVO().getTs());
              if ((tempItemVO == null) || (tempItemVO.length == 0))
                throw new BusinessException(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000058"));
              this.m_VOs[j].setChildrenVO(tempItemVO);
            } catch (BusinessException e) {
              SCMEnv.out(e);
              MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("40040504", "UPP40040504-000001"), e.getMessage());
              return;
            } catch (Exception e) {
              SCMEnv.out(e);
              return;
            }
          }
        }

      }

      setBtnsStateList();
    }
  }

  public Object[] getRelaSortObjectArray() {
    return this.m_VOs;
  }

  public void doAddAction(ILinkAddData adddata)
  {
  }

  public void doApproveAction(ILinkApproveData approvedata)
  {
  }

  public void doQueryAction(ILinkQueryData querydata) {
    init();

    SettlebillVO tempVO = null;
    try {
      String billID = querydata.getBillID();
      tempVO = SettleHelper.querySettlebillByHeadKey(null, billID);
    } catch (Exception e) {
      SCMEnv.out(e);
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040504", "UPP40040504-000000"), e.getMessage());
      return;
    }
    if (tempVO == null) return;

    String pk_corp = tempVO.getHeadVO().getPk_corp();
    SCMQueryConditionDlg dlg = null;
    dlg = initQueryModel(dlg, pk_corp, true);
    ConditionVO[] conditionVO = dlg.getConditionVO();

    Vector vTemp = new Vector();
    ConditionVO condVO = new ConditionVO();
    condVO.setFieldCode("csettlebillid");
    condVO.setOperaCode("=");
    condVO.setValue(tempVO.getHeadVO().getCsettlebillid());
    vTemp.addElement(condVO);
    if ((conditionVO != null) && (conditionVO.length > 0)) {
      for (int i = 0; i < conditionVO.length; i++) vTemp.addElement(conditionVO[i]);
    }
    conditionVO = new ConditionVO[vTemp.size()];
    vTemp.copyInto(conditionVO);
    try {
      SettlebillVO[] VOs = SettleHelper.querySettlebill(pk_corp, conditionVO);
      if ((VOs != null) && (VOs.length > 0)) tempVO = VOs[0]; else
        tempVO = null;
    } catch (Exception e) {
      SCMEnv.out(e);
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040504", "UPP40040504-000000"), e.getMessage());
      return;
    }

    getBillCardPanel().setBillValueVO(tempVO);
    getBillCardPanel().getBillModel().execLoadFormula();
    getBillCardPanel().updateUI();

    if (tempVO != null) this.m_VOs = new SettlebillVO[] { tempVO }; else
      this.m_VOs = null;
  }

  public void doMaintainAction(ILinkMaintainData maintaindata)
  {
  }
}