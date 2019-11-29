package nc.ui.po.pub;

import java.awt.Container;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import nc.bs.bd.b21.BusinessCurrencyRateUtil;
import nc.bs.bd.b21.CurrencyRateUtil;
import nc.ui.ml.NCLangRes;
import nc.ui.pu.pub.POPubSetUI2;
import nc.ui.pu.pub.PuTool;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillScrollPane;
import nc.ui.pub.bill.IBillModelSortPrepareListener;
import nc.ui.scm.pub.BillTools;
import nc.ui.scm.pub.def.DefSetTool;
import nc.ui.scm.service.LocalCallService;
import nc.vo.po.OrderHeaderVO;
import nc.vo.po.OrderItemVO;
import nc.vo.po.OrderVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pu.Timer;
import nc.vo.scm.pu.VariableConst;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.service.ServcallVO;
import nc.vo.sm.UserVO;

public class PoListPanel extends BillListPanel
  implements ListSelectionListener, IBillModelSortPrepareListener
{
  private Container m_conInvoker = null;

  private String m_sTemplateId = null;

  private POPubSetUI2 m_listPoPubSetUI2 = null;

  private boolean m_bLoadVendCodeName = false;

  public PoListPanel(Container sInvoker, String sTemplateId)
  {
    this.m_listPoPubSetUI2 = new POPubSetUI2();

    setContainer(sInvoker);
    setCorp(PoPublicUIClass.getLoginPk_corp());
    setTemplateId(sTemplateId);
    setBillType(null);

    initi();
  }

  public PoListPanel(Container sInvoker, String sCorp, String sBillType, POPubSetUI2 setUi)
  {
    if (setUi != null)
      this.m_listPoPubSetUI2 = setUi;
    else {
      this.m_listPoPubSetUI2 = new POPubSetUI2();
    }
    setContainer(sInvoker);
    setCorp(sCorp);
    setBillType(sBillType);
    setTemplateId(null);
    initi();
  }

  public void displayCurVO(int iUIPos, boolean bMarkFirstRow)
  {
    displayCurVO(iUIPos, bMarkFirstRow, false);
  }

  public void displayCurVO(int iUIPos, boolean bMarkFirstRow, boolean bLoadVendCodeName)
  {
    this.m_bLoadVendCodeName = bLoadVendCodeName;

    Timer timeDebug = new Timer();
    timeDebug.start();

    getHeadBillModel().clearBodyData();
    getBodyBillModel().clearBodyData();
    timeDebug.addExecutePhase("清除界面数据");

    OrderHeaderVO[] voaHead = getContainer().getOrderViewHVOs();
    if (voaHead == null) {
      getContainer().setButtonsStateList();
      return;
    }

    timeDebug.addExecutePhase("得到表头数组");

    setHeaderValueVO(voaHead);
    timeDebug.addExecutePhase("设置表头VO");

    getHeadBillModel().execLoadFormula();
    timeDebug.addExecutePhase("表头公式");

    if (getHeadTable().getRowCount() != 0) {
      if (bMarkFirstRow) {
        getHeadBillModel().setRowState(iUIPos, 4);
      }
      getHeadTable().setRowSelectionInterval(iUIPos, iUIPos);
    }
    timeDebug.showAllExecutePhase("采购订单列表加载表头");
  }

  public void displayCurVOBody(int iUIPos)
  {
    Timer timeDebug = new Timer();
    timeDebug.start();
    try
    {
      OrderVO voViewCur = getContainer().getOrderViewVOAt(getVOPos(iUIPos));
      String sCurPk_corp = voViewCur.getHeadVO().getPk_corp();
      timeDebug.addExecutePhase("取得表体");

      OrderItemVO[] voaItem = voViewCur.getBodyVO();
      if ((voaItem == null) || (voaItem.length == 0)) {
        getBodyBillModel().clearBodyData();
        return;
      }

      setBodyDigits_CorpRelated(sCurPk_corp);
      timeDebug.addExecutePhase("设置精度");

      setBodyValueVO(voaItem);
      timeDebug.addExecutePhase("设置VO");

      PoCardPanel.resetBodyValueRelated_Curr(sCurPk_corp, voViewCur.getHeadVO().getCcurrencytypeid(), getBodyBillModel(), new BusinessCurrencyRateUtil(getCorp()), getBodyBillModel().getRowCount(), this.m_listPoPubSetUI2);
      timeDebug.addExecutePhase("重设表体币种及值");

      getBodyBillModel().execLoadFormula();
      timeDebug.addExecutePhase("表体公式");

      PoPublicUIClass.setFreeColValue(getBodyBillModel(), "vfree");
      timeDebug.addExecutePhase("设置自由项");

      PuTool.setBillModelConvertRate(getBodyBillModel(), new String[] { "cbaseid", "cassistunit", "nordernum", "nassistnum", "nconvertrate" });
      timeDebug.addExecutePhase("设置换算率");

      PuTool.loadSourceInfoAll(this, "21");
      timeDebug.addExecutePhase("处理来源单据类型、来源单据号");

      PoPublicUIClass.loadListMaxPrice(this, iUIPos, this.m_listPoPubSetUI2);
      timeDebug.addExecutePhase("最高限价");

      if ("2A".equals(getBillType())) {
        setColor();
      }

      if (this.m_bLoadVendCodeName) {
        String strVendorId = voViewCur.getHeadVO().getCvendormangid();
        String[] saMangId = new String[voViewCur.getBodyVO().length];
        for (int i = 0; i < saMangId.length; i++) {
          saMangId[i] = voViewCur.getBodyVO()[i].getCmangid();
        }
        PuTool.loadVendorInvInfos(strVendorId, saMangId, getBodyBillModel(), 0, saMangId.length - 1);
      }
    } catch (Exception ex) {
      PuTool.outException(this, ex);
    }
    timeDebug.showAllExecutePhase("采购订单列表加载表体");
  }

  private IPoListPanel getContainer()
  {
    return (IPoListPanel)this.m_conInvoker;
  }

  public int getHeadRowCount()
  {
    return getHeadBillModel().getRowCount();
  }

  public int getHeadRowState(int iRow)
  {
    return getHeadBillModel().getRowState(iRow);
  }

  public int getHeadSelectedCount()
  {
    if (getHeadBillModel().getRowCount() == 0) {
      return 0;
    }

    int[] iaSelectedRow = getHeadTable().getSelectedRows();
    if (iaSelectedRow == null) {
      return 0;
    }
    return iaSelectedRow.length;
  }

  public int getHeadSelectedRow()
  {
    return getHeadTable().getSelectedRow();
  }

  public int[] getHeadSelectedRows()
  {
    return getHeadTable().getSelectedRows();
  }

  public Integer[] getHeadSelectedVOPoses()
  {
    HashMap htmapIndex = new HashMap();

    int[] iaSelectedRowCount = getHeadSelectedRows();
    int iLen = iaSelectedRowCount.length;
    for (int i = 0; i < iLen; i++) {
      int iVOPos = getVOPos(iaSelectedRowCount[i]);
      htmapIndex.put(new Integer(iVOPos), "");
    }

    TreeMap trmapIndex = new TreeMap(htmapIndex);
    Integer[] iaIndex = (Integer[])(Integer[])trmapIndex.keySet().toArray(new Integer[iLen]);

    return iaIndex;
  }

  public int getSortTypeByBillItemKey(String sItemKey)
  {
    if (("crowno".equals(sItemKey)) || ("csourcebillrowno".equals(sItemKey)) || ("cancestorbillrowno".equals(sItemKey)))
    {
      return 2;
    }
    return getBodyBillModel().getItemByKey(sItemKey).getDataType();
  }

  private String getTemplateId()
  {
    return this.m_sTemplateId;
  }

  public int getVOPos(int iUIRow)
  {
    return PuTool.getIndexBeforeSort(this, iUIRow);
  }

  private void initi()
  {
    Timer timeDebug = new Timer();
    timeDebug.start();
    try
    {
      if (getTemplateId() == null)
        loadTemplet(getBillType(), null, ClientEnvironment.getInstance().getUser().getPrimaryKey(), getCorp());
      else
        loadTemplet(getTemplateId());
    }
    catch (Exception e) {
      SCMEnv.out(e);
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270"), NCLangRes.getInstance().getStrByID("40040200", "UPP40040200-000047"));
      return;
    }
    timeDebug.addExecutePhase("加载模板");

    initiHideItems();
    timeDebug.addExecutePhase("隐藏项");

    initiComboBox();
    timeDebug.addExecutePhase("下拉框");

    int iDigit = 2;
    Integer iIDigit = null;
    try
    {
      ServcallVO[] scdsAll = new ServcallVO[1];

      scdsAll[0] = new ServcallVO();
      scdsAll[0].setBeanName("nc.itf.pu.pub.IPub");
      scdsAll[0].setMethodName("queryResultsFromAnyTable");
      scdsAll[0].setParameter(new Object[] { "bd_currtype", new String[]{ "currdigit" }, "pk_currtype='" + this.m_listPoPubSetUI2.getCurrArith_Finance(getCorp()).getLocalCurrPK() + "'" });

      scdsAll[0].setParameterTypes(new Class[] { String.class, java.lang.String.class, String.class });

      Object[] objs = LocalCallService.callService(scdsAll);
      if ((objs == null) || (objs.length != 1)) {
        throw new Exception(NCLangRes.getInstance().getStrByID("40040200", "UPP40040200-000048"));
      }

      if (objs[0] != null) {
        iIDigit = new Integer(((Object[][])(Object[][])objs[0])[0][0].toString());
      }
      if (iIDigit != null)
        iDigit = iIDigit.intValue();
    }
    catch (Exception e) {
      PuTool.outException(this, e);
    }

    DefSetTool.updateBillListPanelUserDef(this, getCorp(), "21", "vdef", "vdef");

    setEnabled(false);
    timeDebug.addExecutePhase("自定义项");

    setHeadDigits(iDigit);
    timeDebug.addExecutePhase("设置表头精度");

    PuTool.setTranslateRender(this);

    getParentListPanel().setShowThMark(true);
    getChildListPanel().setShowThMark(true);

    initiListener(2);

    getChildListPanel().setTotalRowShow(true);
    timeDebug.addExecutePhase("其他");

    timeDebug.showAllExecutePhase("列表界面加载");
  }

  private void initiComboBox()
  {
    getBodyItem("idiscounttaxtype").setWithIndex(true);

    UIComboBox cbbBType = (UIComboBox)(UIComboBox)getBodyItem("idiscounttaxtype").getComponent();
    cbbBType.setFont(getFont());
    cbbBType.setBackground(getBackground());

    cbbBType.addItem(VariableConst.IDISCOUNTTAXTYPE_NAME_INNER);
    cbbBType.addItem(VariableConst.IDISCOUNTTAXTYPE_NAME_OUTTER);
    cbbBType.addItem(VariableConst.IDISCOUNTTAXTYPE_NAME_NOCOUNT);
    cbbBType.setSelectedIndex(1);

    cbbBType.setTranslate(true);
  }

  private void initiHideItems()
  {
    hideHeadTableCol("pk_corp");
    hideHeadTableCol("cbiztype");
    hideHeadTableCol("cpurorganization");

    hideHeadTableCol("cvendormangid");
    hideHeadTableCol("cfreecustid");

    hideHeadTableCol("caccountbankid");

    hideHeadTableCol("cemployeeid");
    hideHeadTableCol("cdeptid");
    hideHeadTableCol("creciever");
    hideHeadTableCol("cgiveinvoicevendor");

    hideHeadTableCol("ctransmodeid");
    hideHeadTableCol("ctermprotocolid");
  }

  protected void initiListener(int iSelectionModel)
  {
    getHeadTable().setCellSelectionEnabled(false);

    getHeadTable().setSelectionMode(iSelectionModel);
    getHeadTable().getSelectionModel().addListSelectionListener(this);

    getBodyBillModel().setSortPrepareListener(this);
  }

  public void onActionDeselectAll()
  {
    int iLen = getHeadBillModel().getRowCount();

    getHeadTable().removeRowSelectionInterval(0, iLen - 1);
  }

  public void onActionSelectAll()
  {
    getHeadTable().setRowSelectionInterval(0, getHeadBillModel().getRowCount() - 1);
  }

  private void setBodyDigits_CorpRelated(String pk_corp)
  {
    PoCardPanel.setBodyDigits_CorpRelated(pk_corp, getBodyBillModel());

    int[] iaDigit = PoPublicUIClass.getShowDigits(pk_corp);
    if (getBodyItem("nnotarrvnum") != null) {
      getBodyItem("nnotarrvnum").setDecimalDigits(iaDigit[0]);
    }
    if (getBodyItem("nnotstorenum") != null) {
      getBodyItem("nnotstorenum").setDecimalDigits(iaDigit[0]);
    }

    if (getBodyItem("nprice") != null) {
      getBodyItem("nprice").setDecimalDigits(iaDigit[2]);
    }
    if (getBodyItem("ntaxprice") != null)
      getBodyItem("ntaxprice").setDecimalDigits(iaDigit[2]);
  }

  private void setColor()
  {
    if ((getBodyItem("nmoney") == null) || (getBodyItem("nordernum") == null) || (getBodyItem("nmaxprice") == null))
    {
      return;
    }

    ArrayList listRow = new ArrayList();

    int iBodyCount = getBodyBillModel().getRowCount();

    UFDouble dMoney = null;
    UFDouble dNum = null;
    UFDouble dMaxprice = null;
    UFDouble dPrice = null;
    for (int i = 0; i < iBodyCount; i++)
    {
      dMaxprice = PuPubVO.getUFDouble_ValueAsValue(getBodyBillModel().getValueAt(i, "nmaxprice"));
      if (dMaxprice == null) {
        continue;
      }
      dMoney = PuPubVO.getUFDouble_ValueAsValue(getBodyBillModel().getValueAt(i, "nmoney"));
      dNum = PuPubVO.getUFDouble_ValueAsValue(getBodyBillModel().getValueAt(i, "nordernum"));
      if ((dMoney == null) || (dNum == null)) {
        continue;
      }
      dPrice = dMoney.div(dNum);

      if (dPrice.compareTo(dMaxprice) > 0) {
        listRow.add(new Integer(i));
      }
    }

    if (listRow.size() > 0) {
      int[] k = new int[listRow.size()];
      for (int i = 0; i < listRow.size(); i++)
        k[i] = ((Integer)listRow.get(i)).intValue();
      BillTools.changeRowNOColor(getBodyTable(), k);
    } else {
      BillTools.changeRowNOColor(getBodyTable(), null);
    }
  }

  private void setContainer(Container newCon)
  {
    this.m_conInvoker = newCon;
  }

  private void setHeadDigits(int iDigit)
  {
    int iLen = OrderHeaderVO.getDbMnyFields_Local_Finance().length;
    for (int i = 0; i < iLen; i++) {
      BillItem item = getHeadItem(OrderHeaderVO.getDbMnyFields_Local_Finance()[i]);
      if (item != null) {
        item.setDecimalDigits(iDigit);
      }

    }

    BillItem item = getHeadItem("nversion");
    if (item != null)
      item.setDecimalDigits(1);
  }

  private void setTemplateId(String sValue)
  {
    this.m_sTemplateId = sValue;
  }

  public void valueChanged(ListSelectionEvent e)
  {
    if (e.getValueIsAdjusting() == true) {
      return;
    }

    int iCount = getHeadTable().getRowCount();
    for (int i = 0; i < iCount; i++) {
      getHeadBillModel().setRowState(i, 0);
    }

    int[] iaSelectedRow = getHeadTable().getSelectedRows();
    if (iaSelectedRow != null) {
      iCount = iaSelectedRow.length;

      for (int i = 0; i < iCount; i++) {
        getHeadBillModel().setRowState(iaSelectedRow[i], 4);
      }
    }
    if (getHeadSelectedCount() != 1) {
      setBodyValueVO(null);
    }
    else {
      displayCurVOBody(getHeadSelectedRow());
    }

    getContainer().setButtonsStateList();
  }

  public POPubSetUI2 getPoPubSetUi2()
  {
    if (this.m_listPoPubSetUI2 == null) {
      this.m_listPoPubSetUI2 = new POPubSetUI2();
    }
    return this.m_listPoPubSetUI2;
  }
}