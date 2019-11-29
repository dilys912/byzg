package nc.ui.pi.invoice;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import nc.itf.scm.cenpur.service.ChgDataUtil;
import nc.ui.ml.NCLangRes;
import nc.ui.pi.InvoiceHelper;
import nc.ui.pi.InvoiceHelper1;
import nc.ui.pi.pub.PiPqPublicUIClass;
import nc.ui.pps.PricStlHelper;
import nc.ui.pu.pub.POPubSetUI;
import nc.ui.pu.pub.PuTool;
import nc.ui.pu.pub.PubHelper;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillScrollPane;
import nc.ui.pub.bill.IBillRelaSortListener2;
import nc.ui.pub.change.PfChangeBO_Client;
import nc.ui.pub.hotkey.HotkeyUtil;
import nc.ui.pub.para.SysInitBO_Client;
import nc.ui.pub.pf.AbstractReferQueryUI;
import nc.ui.scm.pub.FreeVOParse;
import nc.ui.scm.pub.cache.CacheTool;
import nc.ui.scm.pub.panel.RelationsCal;
import nc.vo.bd.CorpVO;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.pi.InvoiceHeaderVO;
import nc.vo.pi.InvoiceItemVO;
import nc.vo.pi.InvoiceVO;
import nc.vo.po.OrderItemVO;
import nc.vo.pps.PricParaVO;
import nc.vo.ps.estimate.GeneralBb3VO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.para.SysInitVO;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.cenpur.service.ChgDocPkVO;
import nc.vo.scm.ic.bill.FreeVO;
import nc.vo.scm.pu.VariableConst;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.sm.UserVO;

public class InvFrmStoUI1 extends AbstractReferQueryUI
  implements ListSelectionListener, ActionListener, BillEditListener, IBillRelaSortListener2
{
  private UIButton ivjUIButtonCancel = null;
  private UIButton ivjUIButtonDeselectAll = null;
  private UIButton ivjUIButtonDisType = null;
  private UIButton ivjUIButtonOK = null;
  private UIButton ivjUIButtonQuery = null;
  private UIButton ivjUIButtonSelectAll = null;
  private UIButton ivjUIButtonTold = null;
  private BillCardPanel ivjUIPanelData = null;

  private UIPanel m_pnlButton = null;
  private GeneralBillVO[] m_GeneralHVOs;
  private GeneralBillItemVO[] m_GeneralHItemVOs;
  private int[] m_nShowBeginRows;
  private int[] m_nShowEndRows;
  private InvFrmStoQueDlg m_InvFrmStoQueDlg = null;
  private StoClassifyBillDlg m_StoClassifyBillDlg;
  private String m_sCurrTypeID = null;
  private Hashtable m_hCurrTypeID = null;
  private InvoiceVO[] m_InvRetVOs;
  private String m_strDisType;
  private SysInitVO m_sysInitVO = null;
  private Integer[][] m_DistributedRows;
  private int m_nSelectedRowCount;
  private OrderItemVO[] m_OrderItemVOs;
  private Vector m_defaultNumVEC = new Vector();
  private Vector m_defaultPriceVEC = new Vector();

  private Hashtable m_hasMnyDigit = null;

  private HashMap hInvoiceNum = new HashMap();

  private HashMap hWareHouseId = new HashMap();

  private Hashtable m_hasOrderInfo = null;

  private Hashtable m_hasUpSourceTS = null;
  private boolean showed = false;

  //add by cm 
  private static String str_bid;
  
  public static String getStr_bid() {
	return str_bid;
}

public void setStr_bid(String str_bid) {
	this.str_bid = str_bid;
}

public InvFrmStoUI1(String pkField, String pkCorp, String operator, String funNode, String queryWhere, String billType, String businessType, String templateId, String currentBillType, Container parent)
  {
    super(pkField, pkCorp, operator, funNode, queryWhere, billType, businessType, templateId, currentBillType, parent);

    init();

    String strReplacedWhere = queryWhere;
    strReplacedWhere = strReplacedWhere.substring(0, strReplacedWhere.length() - 1);
    if (strReplacedWhere.equals("null")) {
      strReplacedWhere = null;
    }

    initQuery(strReplacedWhere);
  }

  public Object[] getRelaSortObjectArray()
  {
    return this.m_GeneralHItemVOs;
  }

  public void actionPerformed(ActionEvent e)
  {
    if (e.getSource() == this.ivjUIButtonOK)
//    	if(InvoiceUI().zifalg = 1){
//    		onGenInvoices1();
//    	}else{
    		onGenInvoices();
//    	}
    else if (e.getSource() == this.ivjUIButtonCancel)
    {
      super.closeCancel();
    } else if (e.getSource() == this.ivjUIButtonDeselectAll)
      onDeselectAll();
    else if (e.getSource() == this.ivjUIButtonSelectAll)
      onSelectAll();
    else if (e.getSource() == this.ivjUIButtonDisType)
      onDisType();
    else if (e.getSource() == this.ivjUIButtonQuery)
      onQuery();
  }
 
  public void afterEdit(BillEditEvent e)
  {
    afterEditRelations(e);
  }

  public void bodyRowChange(BillEditEvent e)
  {
  }

  private StoClassifyBillDlg getDisTypeDlg()
  {
    if (this.m_StoClassifyBillDlg == null) {
      this.m_StoClassifyBillDlg = new StoClassifyBillDlg(this, getDisType());
    }
    return this.m_StoClassifyBillDlg;
  }

  /** @deprecated */
  private InvFrmStoQueDlg getInvFrmStoQueDlg()
  {
    if (this.m_InvFrmStoQueDlg == null) {
      this.m_InvFrmStoQueDlg = new InvFrmStoQueDlg(this, getPkCorp(), getOperator(), getFunNode(), getBusinessType(), getCurrentBillType(), getBillType(), null);
    }

    return this.m_InvFrmStoQueDlg;
  }

  private int[] getShowBeginRows()
  {
    return this.m_nShowBeginRows;
  }

  private int[] getShowEndRows()
  {
    return this.m_nShowEndRows;
  }

  private UIButton getUIButtonDeselectAll()
  {
    if (this.ivjUIButtonDeselectAll == null) {
      try {
        this.ivjUIButtonDeselectAll = new UIButton();
        this.ivjUIButtonDeselectAll.setName("UIButtonDeselectAll");
        this.ivjUIButtonDeselectAll.setText(NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000115"));
        HotkeyUtil.setHotkey(this.ivjUIButtonDeselectAll, 'N');
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUIButtonDeselectAll;
  }

  private UIButton getUIButtonDisType()
  {
    if (this.ivjUIButtonDisType == null) {
      try {
        this.ivjUIButtonDisType = new UIButton();
        this.ivjUIButtonDisType.setName("UIButtonDisType");
        this.ivjUIButtonDisType.setText(NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000116"));
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUIButtonDisType;
  }

  private UIButton getUIButtonOK()
  {
    if (this.ivjUIButtonOK == null) {
      try {
        this.ivjUIButtonOK = new UIButton();
        this.ivjUIButtonOK.setName("UIButtonOK");
        this.ivjUIButtonOK.setText(NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000117"));
        HotkeyUtil.setHotkey(this.ivjUIButtonOK, 'O');
        this.ivjUIButtonOK.setBounds(236, 4, 115, 22);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUIButtonOK;
  }

  private UIButton getUIButtonSelectAll()
  {
    if (this.ivjUIButtonSelectAll == null) {
      try {
        this.ivjUIButtonSelectAll = new UIButton();
        this.ivjUIButtonSelectAll.setName("UIButtonSelectAll");
        this.ivjUIButtonSelectAll.setText(NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000119"));
        HotkeyUtil.setHotkey(this.ivjUIButtonSelectAll, 'A');
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUIButtonSelectAll;
  }

  private BillCardPanel getUIPanelData()
  {
    if (this.ivjUIPanelData == null) {
      try {
        this.ivjUIPanelData = new InvFrmStoPanel();
        this.ivjUIPanelData.setName("UIPanelData");
        this.ivjUIPanelData.setBounds(4, 43, 687, 380);
        this.ivjUIPanelData.getBillModel().addSortRelaObjectListener2(this);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUIPanelData;
  }

  private void handleException(Throwable exception)
  {
  }

  /** @deprecated */
  private void onDeselectAll()
  {
    getUIPanelData().getBillTable().removeRowSelectionInterval(0, getUIPanelData().getRowCount() - 1);

    for (int i = 0; i < getUIPanelData().getRowCount(); i++) {
      getUIPanelData().getBillModel().setRowState(i, 0);
    }
    setSelectedRowCount(0);
    getUIButtonDeselectAll().setEnabled(false);
    getUIButtonSelectAll().setEnabled(true);
    getUIButtonDisType().setEnabled(true);
    getUIButtonOK().setEnabled(false);
  }

  /** @deprecated */
  private void onDisType()
  {
    getDisTypeDlg().showModal();

    if (getDisTypeDlg().isCloseOK()) {
      setDisType(getDisTypeDlg().getDisType());
      if (this.m_sysInitVO != null) {
        this.m_sysInitVO.setValue(getDisType());
        this.m_sysInitVO.setPk_corpid(ClientEnvironment.getInstance().getCorporation().getPrimaryKey());
        try {
          SysInitBO_Client.saveSysInitVOs(new SysInitVO[] { this.m_sysInitVO });
        } catch (Exception e) {
          SCMEnv.out(e);
        }
      }
    }
  }

  //add by cm start20121217
  private void onGenInvoices1()
  {
    if (getSelectedRowCount() == 0) {
      MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270"), NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000004"));
      return;
    }

    GeneralBillVO[] distributedVOs = getDistributedStoVOs();

    Hashtable hFreeCust = new Hashtable();
    try
    {
      for (int i = 0; i < distributedVOs.length; i++) {
        GeneralBillItemVO[] itemVOs = (GeneralBillItemVO[])(GeneralBillItemVO[])distributedVOs[i].getChildrenVO();
        if ((itemVOs == null) || (itemVOs.length <= 0) || (itemVOs[0].getCfirsttype() == null) || ((!itemVOs[0].getCfirsttype().equals("21")) && (!itemVOs[0].getCfirsttype().equals("61")))) {
          continue;
        }
        String strOrderHid = itemVOs[0].getCfirstbillhid();
        if ((strOrderHid == null) || (strOrderHid.trim().length() <= 0))
          continue;
      }
    }
	  catch (Exception e)
	  {
	    SCMEnv.out(e);
	
	    return;
	  }
  }
  //add by cm end 
  
  private void onGenInvoices()
  {
    if (getSelectedRowCount() == 0) {
      MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270"), NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000004"));
      return;
    }

    GeneralBillVO[] distributedVOs = getDistributedStoVOs();

    Hashtable hFreeCust = new Hashtable();
    StringBuffer sb_bid = new StringBuffer();
    try
    {
      for (int i = 0; i < distributedVOs.length; i++) {
        GeneralBillItemVO[] itemVOs = (GeneralBillItemVO[])(GeneralBillItemVO[])distributedVOs[i].getChildrenVO();
        if ((itemVOs == null) || (itemVOs.length <= 0) || (itemVOs[0].getCfirsttype() == null) || ((!itemVOs[0].getCfirsttype().equals("21")) && (!itemVOs[0].getCfirsttype().equals("61")))) {
          continue;
        }
        String strCgeneralhid = itemVOs[0].getCgeneralhid();
        for (int j = 0; j < itemVOs.length; j++) {
        	String strCgeneralbid = itemVOs[j].getCgeneralbid();
        	if(j==distributedVOs.length-1){
        		sb_bid.append("'"+strCgeneralbid+"'");
        	}else{
        		sb_bid.append("'"+strCgeneralbid+"',");
        	}
		}
//        String strOrderHid = itemVOs[0].getCfirstbillhid();
//        if ((strOrderHid == null) || (strOrderHid.trim().length() <= 0))
//          continue;
//        Object[][] oa2Ret = (Object[][])null;
//        if (itemVOs[0].getCfirsttype().equals("21")) oa2Ret = (Object[][])PubHelper.queryResultsFromAnyTable("po_order", new String[] { "cgiveinvoicevendor", "cvendormangid", "cfreecustid" }, " corderid = '" + strOrderHid + "' and dr = 0 "); else
//          oa2Ret = (Object[][])PubHelper.queryResultsFromAnyTable("sc_order", new String[] { "cgiveinvoicevendor", "cvendormangid" }, " corderid = '" + strOrderHid + "' and dr = 0 ");
//        if ((oa2Ret == null) || (oa2Ret.length <= 0) || 
//          (oa2Ret[0] == null))
//          continue;
//        if ((itemVOs[0].getCfirsttype().equals("21")) && (oa2Ret[0][2] != null)) hFreeCust.put(strOrderHid, oa2Ret[0][2]);
//
//        if (oa2Ret[0][0] != null) {
//          String strGivInvoice = oa2Ret[0][0].toString().trim();
//          if ((strGivInvoice != null) && (strGivInvoice.trim().length() > 0))
//            ((GeneralBillHeaderVO)distributedVOs[i].getParentVO()).setCproviderid(strGivInvoice);
//        }
//        else if (oa2Ret[0][1] != null) {
//          String strVendormangid = oa2Ret[0][1].toString().trim();
//          if ((strVendormangid != null) && (strVendormangid.trim().length() > 0)) {
//            ((GeneralBillHeaderVO)distributedVOs[i].getParentVO()).setCproviderid(strVendormangid);
//          }
//
//        }
//
      }

    }
    catch (Exception e)
    {
      SCMEnv.out(e);

      return;
    }

//    InvoiceVO[] retInvVOs = new InvoiceVO[distributedVOs.length];
//    try {
//      retInvVOs = (InvoiceVO[])(InvoiceVO[])PfChangeBO_Client.pfChangeBillToBillArray(distributedVOs, getBillType(), getCurrentBillType());
//
//      if (!processAfterChange(distributedVOs, retInvVOs)) {
//        MessageDialog.showWarningDlg(this, NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000132"), NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000005"));
//        return;
//      }
//    }
//    catch (Exception e) {
//      SCMEnv.out(e);
//      MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270"), e.getMessage());
//      return;
//    }
//
//    if ((retInvVOs != null) && (retInvVOs.length > 0)) {
//      int retInvVOsLength = retInvVOs.length;
//      Vector vTemp = new Vector();
//      for (int i = 0; i < retInvVOsLength; i++)
//      {
//        InvoiceItemVO[] bodyVO = retInvVOs[i].getBodyVO();
//        String corderid = null;
//        for (int j = 0; j < bodyVO.length; j++) {
//          if (bodyVO[j].getCorderid() != null) {
//            corderid = bodyVO[j].getCorderid();
//            break;
//          }
//        }
//        if (corderid != null) {
//          Object otemp = hFreeCust.get(corderid);
//          if (otemp != null) retInvVOs[i].getHeadVO().setCfreecustid(otemp.toString());
//        }
//
//        for (int j = 0; j < bodyVO.length; j++) {
//          if ((bodyVO[j].getVfree1() == null) && (bodyVO[j].getVfree2() == null) && (bodyVO[j].getVfree3() == null) && (bodyVO[j].getVfree4() == null) && (bodyVO[j].getVfree5() == null))
//            continue;
//          vTemp.addElement(bodyVO[j]);
//        }
//      }
//      if (vTemp.size() > 0) {
//        InvoiceItemVO[] tempbodyVO = new InvoiceItemVO[vTemp.size()];
//        vTemp.copyInto(tempbodyVO);
//        new FreeVOParse().setFreeVO(tempbodyVO, "vfree0", "vfree", null, "cmangid", false);
//      }
//    }
//
//    setRetVOs(retInvVOs);
    setStr_bid(sb_bid.toString());

    closeOK();
  }

  /** @deprecated */
  private void onQuery()
  {
    getInvFrmStoQueDlg().showModal();
    if (getInvFrmStoQueDlg().isCloseOK())
    {
      String strFromWhere = getInvFrmStoQueDlg().getWhereSQL();

      initQuery(strFromWhere);
    }
  }

  /** @deprecated */
  private void onSelectAll()
  {
    getUIPanelData().getBillTable().setRowSelectionInterval(0, getUIPanelData().getRowCount() - 1);

    for (int i = 0; i < getUIPanelData().getRowCount(); i++) {
      getUIPanelData().getBillModel().setRowState(i, 4);
    }

    setSelectedRowCount(getUIPanelData().getRowCount());
    getUIButtonDeselectAll().setEnabled(true);
    getUIButtonSelectAll().setEnabled(false);
    getUIButtonDisType().setEnabled(true);
    getUIButtonOK().setEnabled(true);
  }

  private void setShowBeginRows(int[] newShowBeginRows)
  {
    this.m_nShowBeginRows = newShowBeginRows;
  }

  private void setShowEndRows(int[] newShowEndRows)
  {
    this.m_nShowEndRows = newShowEndRows;
  }

  public AggregatedValueObject[] getRetVos()
  {
    return this.m_InvRetVOs;
  }

  private void setRetVOs(InvoiceVO[] newRetVO)
  {
    this.m_InvRetVOs = newRetVO;
  }

  private String getDisType()
  {
    return this.m_strDisType;
  }

  private void setDisType(String newMode)
  {
    this.m_strDisType = newMode;
  }

  private Integer[][] getDistributedRows()
  {
    return this.m_DistributedRows;
  }

  private OrderItemVO[] getOrderItemVOs()
  {
    return this.m_OrderItemVOs;
  }

  private int getSelectedRowCount()
  {
    return this.m_nSelectedRowCount;
  }

  private int getStoIndexByRow(int row)
  {
    for (int i = 0; i < getGeneralHVOs().length; i++) {
      if ((row <= getShowEndRows()[i]) && (row >= getShowBeginRows()[i])) {
        return i;
      }
    }
    return -1;
  }

  private void initData()
  {
    String strDis = "供应商";
    try {
      this.m_sysInitVO = SysInitBO_Client.queryByParaCode(ClientEnvironment.getInstance().getCorporation().getPrimaryKey(), "PO19");
      if (this.m_sysInitVO != null) strDis = this.m_sysInitVO.getValue(); 
    }
    catch (Exception e) {
      SCMEnv.out(e);
      MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270"), NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000006"));
      strDis = "供应商";
    }
    setDisType(strDis);

    setOrderItemVOs(null);
    setGeneralBillVOs(null);
    setShowBeginRows(null);
    setShowEndRows(null);
    setRetVOs(null);
    this.m_defaultNumVEC = new Vector();
    this.m_defaultPriceVEC = new Vector();
    setDistributedRows((Integer[][])null);
    setSelectedRowCount(0);
  }

  private void setDistributedRows(Integer[][] newDistributedRows)
  {
    this.m_DistributedRows = newDistributedRows;
  }

  private void setOrderItemVOs(OrderItemVO[] newOrderItemVOs)
  {
    this.m_OrderItemVOs = newOrderItemVOs;
  }

  private void setSelectedRowCount(int newSelectedRow)
  {
    this.m_nSelectedRowCount = newSelectedRow;
  }

  private void afterEditRelations(BillEditEvent e)
  {
    if (e.getPos() == 1)
    {
      if ((e.getKey().equals("idiscounttaxtype")) || (e.getKey().equals("ninvoicenum")) || (e.getKey().equals("noriginalcurprice")) || (e.getKey().equals("ntaxrate")) || (e.getKey().equals("noriginalcurmny")) || (e.getKey().equals("noriginaltaxmny")) || (e.getKey().equals("noriginalsummny")) || (e.getKey().equals("norgnettaxprice")))
      {
        if (getUIPanelData().getBodyValueAt(e.getRow(), "idiscounttaxtype") != null) {
          int[] descriptions = { 0, 1, 2, 7, 11, 13, 12, 14, 8 };

          InvoiceVO tempVO = new InvoiceVO(getUIPanelData().getRowCount());
          getUIPanelData().getBillValueVO(tempVO);

          String s = "应税内含";
          InvoiceItemVO[] bodyVO = tempVO.getBodyVO();
          if ((bodyVO != null) && (bodyVO.length > 0)) {
            if (bodyVO[e.getRow()].getIdiscounttaxtype().intValue() == 1) s = "应税外加";
            else if (bodyVO[e.getRow()].getIdiscounttaxtype().intValue() == 2) s = "不计税";
          }

          String[] keys = { s, "idiscounttaxtype", "ninvoicenum", "noriginalcurprice", "ntaxrate", "noriginalcurmny", "noriginaltaxmny", "noriginalsummny", "norgnettaxprice" };

          RelationsCal.calculate(e, getUIPanelData(), new int[] { PuTool.getPricePriorPolicy(getPkCorp()) }, descriptions, keys, InvoiceItemVO.class.getName());
        }
        else
        {
          int[] descriptions = { 2, 7, 11, 13, 12, 14, 8 };

          String[] keys = { "ninvoicenum", "noriginalcurprice", "ntaxrate", "noriginalcurmny", "noriginaltaxmny", "noriginalsummny", "norgnettaxprice" };

          RelationsCal.calculate(e, getUIPanelData(), new int[] { PuTool.getPricePriorPolicy(getPkCorp()) }, descriptions, keys, InvoiceItemVO.class.getName());
        }
      }
    }
  }

  private GeneralBillVO[] getDistributedStoVOs()
  {
    Hashtable stoTable = new Hashtable();
    Hashtable rowsTable = new Hashtable();
    HashMap hFlagMap = new HashMap();

    for (int row = 0; row < getUIPanelData().getBillModel().getRowCount(); row++) {
      if (getUIPanelData().getBillModel().getRowState(row) == 4) {
        int iPos = PuTool.getIndexBeforeSort(getUIPanelData(), row);

        int stoIndex = getStoIndexByRow(iPos);

        String cgeneralhid = this.m_GeneralHItemVOs[row].getCgeneralhid();
        String cgeneralbid = this.m_GeneralHItemVOs[row].getCgeneralbid();
        for (int i = 0; i < getGeneralHVOs().length; i++) {
          GeneralBillHeaderVO headVO = (GeneralBillHeaderVO)getGeneralHVOs()[i].getParentVO();
          if (cgeneralhid.equals(headVO.getCgeneralhid())) {
            stoIndex = i;
            break;
          }

        }

        GeneralBillHeaderVO headVO = (GeneralBillHeaderVO)getGeneralHVOs()[stoIndex].getParentVO();
        GeneralBillItemVO[] itemVOs = (GeneralBillItemVO[])(GeneralBillItemVO[])getGeneralHVOs()[stoIndex].getChildrenVO();
        Object s = headVO.getAttributeValue("pk_purcorp");
        s = itemVOs[0].getAttributeValue("pk_invoicecorp");

        String pk_cubasdoc = null;
        Object oTemp = null;
        try {
          oTemp = CacheTool.getCellValue("bd_cumandoc", "pk_cumandoc", "pk_cubasdoc", headVO.getCproviderid());
        } catch (Exception e) {
          e.printStackTrace();
          return null;
        }
        if ((oTemp != null) && (((Object[])(Object[])oTemp).length >= 1) && 
          (((Object[])(Object[])oTemp)[0] != null) && (((Object[])(Object[])oTemp)[0].toString().trim().length() > 0)) pk_cubasdoc = ((Object[])(Object[])oTemp)[0].toString();

        String curKey = ((headVO.getCbiztypeid() == null) || (headVO.getCbiztypeid().trim().length() == 0) ? "NULL" : headVO.getCbiztypeid()) + ((pk_cubasdoc == null) || (pk_cubasdoc.trim().length() == 0) ? "NULL" : pk_cubasdoc);

        String deptKey = (headVO.getCdptid() == null) || (headVO.getCdptid().trim().length() == 0) ? "NULL" : headVO.getCdptid();
        String sStoreOrgId = (headVO.getPk_calbody() == null) || (headVO.getPk_calbody().trim().length() == 0) ? "NULL" : headVO.getPk_calbody();

        int itemIndex = 0;
        for (int i = 0; i < itemVOs.length; i++) {
          if (cgeneralbid.equals(itemVOs[i].getCgeneralbid())) {
            itemIndex = i;
            break;
          }

        }

        if (getDisType().trim().equals("存货+供应商"))
          curKey = curKey + itemVOs[itemIndex].getCinvbasid();
        else if (getDisType().trim().equals("订单")) {
          if ((getOrderItemVOs() != null) && (getOrderItemVOs()[iPos] != null))
            curKey = curKey + getOrderItemVOs()[iPos].getCorderid();
          else
            curKey = curKey + "NULL";
        }
        else if (getDisType().trim().equals("入库单")) {
          curKey = curKey + headVO.getPrimaryKey();
        }

        if (!stoTable.containsKey(curKey)) {
          Vector vec = new Vector();
          vec.addElement(headVO);
          vec.addElement(itemVOs[itemIndex]);
          stoTable.put(curKey, vec);

          Vector indexVEC = new Vector();
          indexVEC.addElement(new Integer(row));
          rowsTable.put(curKey, indexVEC);

          Vector vFlag = new Vector();
          vFlag.addElement(deptKey);
          vFlag.addElement(sStoreOrgId);

          hFlagMap.put(curKey, vFlag);
        } else {
          Vector vec = (Vector)stoTable.get(curKey);
          vec.addElement(itemVOs[itemIndex]);

          Vector indexVEC = (Vector)rowsTable.get(curKey);
          indexVEC.addElement(new Integer(row));

          Vector vFlag = (Vector)hFlagMap.get(curKey);
          if (!((String)vFlag.get(0)).equals(deptKey))
            ((GeneralBillHeaderVO)vec.get(0)).setCdptid(null);
          if (!((String)vFlag.get(1)).equals(sStoreOrgId)) {
            ((GeneralBillHeaderVO)vec.get(0)).setPk_calbody(null);
          }
        }
      }
    }
    if (stoTable.size() == 0) {
      MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270"), NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000004"));
      return null;
    }

    GeneralBillVO[] allVOs = null;
    Integer[][] allRows = (Integer[][])null;
    if (stoTable.size() > 0) {
      allVOs = new GeneralBillVO[stoTable.size()];
      allRows = new Integer[stoTable.size()][];
      Enumeration elems = stoTable.keys();
      int i = 0;
      while (elems.hasMoreElements()) {
        Object curKey = elems.nextElement();
        Vector vec = (Vector)stoTable.get(curKey);
        int len = vec.size() - 1;

        GeneralBillHeaderVO headVO = (GeneralBillHeaderVO)vec.elementAt(0);
        GeneralBillItemVO[] itemVOs = new GeneralBillItemVO[len];
        vec.removeElementAt(0);
        vec.copyInto(itemVOs);

        allVOs[i] = new GeneralBillVO();
        allVOs[i].setParentVO(headVO);
        allVOs[i].setChildrenVO(itemVOs);

        vec = (Vector)rowsTable.get(curKey);
        allRows[i] = new Integer[vec.size()];
        vec.copyInto(allRows[i]);

        i++;
      }
    }

    setDistributedRows(allRows);

    setUpSourceTS();
    setOrderInfo();

    return allVOs;
  }

  private GeneralBillVO[] getGeneralHVOs()
  {
    return this.m_GeneralHVOs;
  }

  private Hashtable getHashMnyDigit()
  {
    if (this.m_hasMnyDigit == null) {
      this.m_hasMnyDigit = new Hashtable();
    }
    return this.m_hasMnyDigit;
  }

  private void init()
  {
    setSize(800, 600);

    initCard();
    initData();
    initButtons();
    initListener();
    initTitle();
    initPresion();
    try
    {
      this.m_sCurrTypeID = SysInitBO_Client.getPkValue(ClientEnvironment.getInstance().getCorporation().getPrimaryKey(), "BD301");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void initCard()
  {
    try
    {
      getContentPane().setLayout(new BorderLayout());

      getContentPane().add(getUIPanelData(), "Center");
      getContentPane().add(getUIPanelButton(), "South");
    } catch (Exception e) {
      SCMEnv.out(e);
    }
  }

  private UIPanel getUIPanelButton()
  {
    if (this.m_pnlButton == null) {
      this.ivjUIButtonSelectAll = new UIButton(NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000119"));

      this.ivjUIButtonSelectAll.addActionListener(this);
      this.ivjUIButtonSelectAll.addKeyListener(this);

      this.ivjUIButtonDeselectAll = new UIButton(NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000115"));

      this.ivjUIButtonDeselectAll.addActionListener(this);
      this.ivjUIButtonDeselectAll.addKeyListener(this);

      this.ivjUIButtonQuery = new UIButton(NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000118"));

      this.ivjUIButtonQuery.addActionListener(this);
      this.ivjUIButtonQuery.addKeyListener(this);

      this.ivjUIButtonDisType = new UIButton(NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000116"));

      this.ivjUIButtonDisType.addActionListener(this);
      this.ivjUIButtonDisType.addKeyListener(this);

      this.ivjUIButtonOK = new UIButton(NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000117"));
      this.ivjUIButtonOK.addActionListener(this);
      this.ivjUIButtonOK.addKeyListener(this);

      this.ivjUIButtonCancel = new UIButton(NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000114"));
      this.ivjUIButtonCancel.addActionListener(this);
      this.ivjUIButtonCancel.addKeyListener(this);

      this.m_pnlButton = new UIPanel();
      this.m_pnlButton.setLayout(new GridLayout());

      this.m_pnlButton.add(this.ivjUIButtonSelectAll);
      this.m_pnlButton.add(this.ivjUIButtonDeselectAll);
      this.m_pnlButton.add(this.ivjUIButtonQuery);
      this.m_pnlButton.add(this.ivjUIButtonDisType);
      this.m_pnlButton.add(this.ivjUIButtonOK);
      this.m_pnlButton.add(this.ivjUIButtonCancel);
    }

    return this.m_pnlButton;
  }

  private void initButtons()
  {
    getUIButtonDeselectAll().setEnabled(false);
    getUIButtonSelectAll().setEnabled(false);
    getUIButtonDisType().setEnabled(false);
    getUIButtonOK().setEnabled(false);
  }

  private void initListener()
  {
    getUIPanelData().addEditListener(this);
    getUIPanelData().getBillTable().getSelectionModel().addListSelectionListener(this);

    getUIPanelData().getBodyPanel().addTableSortListener();
    getUIPanelData().getBillModel().setRowSort(true);
  }

  private void initPresion()
  {
    getUIPanelData().getBodyItem("ninnum").setDecimalDigits(PiPqPublicUIClass.getNumDigit());
    getUIPanelData().getBodyItem("nsignnum").setDecimalDigits(PiPqPublicUIClass.getNumDigit());
    getUIPanelData().getBodyItem("ninvoicenum").setDecimalDigits(PiPqPublicUIClass.getNumDigit());
    getUIPanelData().getBodyItem("nreasonwastenum").setDecimalDigits(PiPqPublicUIClass.getNumDigit());

    getUIPanelData().getBodyItem("noriginalcurprice").setDecimalDigits(PiPqPublicUIClass.getPriceDigit());
    getUIPanelData().getBodyItem("norgnettaxprice").setDecimalDigits(PiPqPublicUIClass.getPriceDigit());
  }

  private void initQuery(String strFromWhere)
  {
    ConditionVO[] powerVOs = null;
    Vector v = new Vector();

    Object[] ob = null;
    try
    {
      ConditionVO tempVO = new ConditionVO();
      tempVO.setFieldName("操作员");
      tempVO.setFieldCode("");
      tempVO.setOperaCode("");
      tempVO.setValue(ClientEnvironment.getInstance().getUser().getPrimaryKey());
      v.addElement(tempVO);
      tempVO = new ConditionVO();
      tempVO.setFieldName("公司");
      tempVO.setFieldCode("");
      tempVO.setOperaCode("");
      tempVO.setValue(ClientEnvironment.getInstance().getCorporation().getPrimaryKey());
      v.addElement(tempVO);

      powerVOs = new ConditionVO[v.size()];
      v.copyInto(powerVOs);
      ob = InvoiceHelper1.queryGenenelVOsByFromWhere(getBillType(), strFromWhere, powerVOs);
    } catch (Exception e) {
      SCMEnv.out(e);
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270"), NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000428"));
      getUIPanelData().getBillModel().clearBodyData();
      return;
    }

    if (ob == null) {
      MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270"), NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000007"));
      getUIPanelData().updateUI();
      getUIPanelData().getBillModel().clearBodyData();

      getUIButtonDeselectAll().setEnabled(false);
      getUIButtonSelectAll().setEnabled(false);
      getUIButtonDisType().setEnabled(false);
      getUIButtonOK().setEnabled(false);

      return;
    }

    getUIButtonDeselectAll().setEnabled(true);
    getUIButtonSelectAll().setEnabled(true);
    getUIButtonDisType().setEnabled(true);
    getUIButtonOK().setEnabled(true);

    GeneralBillVO[] hVOs = (GeneralBillVO[])(GeneralBillVO[])ob[0];

    cacheWareHouse(hVOs);
    GeneralBb3VO[] bb3VOs = (GeneralBb3VO[])(GeneralBb3VO[])ob[1];
    String[] strOrderCodes = (String[])(String[])ob[2];

    OrderItemVO[] orderItemVOs = (OrderItemVO[])(OrderItemVO[])ob[4];

    delVfree(hVOs);

    int[] newBeginRows = new int[hVOs.length];
    int[] newEndRows = new int[hVOs.length];
    getUIPanelData().getBillModel().clearBodyData();
    int curGeneralHI = 0;
    int beginPos = 0;

    Vector vTemp = new Vector();

    Hashtable hTaxRate = new Hashtable();
    this.m_hCurrTypeID = new Hashtable();

    for (int i = 0; i < hVOs.length; i++) {
      if ((hVOs[i].getHeaderVO().getCproviderid() != null) && (!vTemp.contains(hVOs[i].getHeaderVO().getCproviderid())))
        vTemp.addElement(hVOs[i].getHeaderVO().getCproviderid());
    }
    if (vTemp.size() > 0) {
      String[] sTemp = new String[vTemp.size()];
      vTemp.copyInto(sTemp);
      try {
        Object oTemp = CacheTool.getColumnValue("bd_cumandoc", "pk_cumandoc", "pk_currtype1", sTemp);
        if (oTemp != null) {
          Object[] o = (Object[])(Object[])oTemp;
          if ((o != null) && (o.length > 0))
            for (int i = 0; i < o.length; i++) {
              if (o[i] == null) continue; this.m_hCurrTypeID.put(sTemp[i], o[i]);
            }
        }
      }
      catch (Exception e) {
        e.printStackTrace();
        return;
      }
    }

    vTemp = new Vector();
    for (int i = 0; i < hVOs.length; i++) {
      GeneralBillItemVO[] bodyVO = hVOs[i].getItemVOs();
      for (int j = 0; j < bodyVO.length; j++) {
        if ((bodyVO[j].getCinventoryid() != null) && (!vTemp.contains(bodyVO[j].getCinventoryid())))
          vTemp.addElement(bodyVO[j].getCinventoryid());
      }
    }
    if (vTemp.size() > 0) {
      String[] sTemp = new String[vTemp.size()];
      vTemp.copyInto(sTemp);
      try
      {
        Object oTemp = CacheTool.getColumnValue("bd_invmandoc", "pk_invmandoc", "pk_invbasdoc", sTemp);
        if (oTemp != null) {
          vTemp = new Vector();
          Object[] o = (Object[])(Object[])oTemp;
          if ((o != null) && (o.length > 0)) {
            for (int i = 0; i < o.length; i++) vTemp.addElement(o[i]);
          }
          String[] s = new String[vTemp.size()];
          vTemp.copyInto(s);

          oTemp = CacheTool.getColumnValue("bd_invbasdoc", "pk_invbasdoc", "pk_taxitems", s);

          if (oTemp != null) {
            o = (Object[])(Object[])oTemp;
            vTemp = new Vector();
            if ((o != null) && (o.length > 0)) {
              for (int i = 0; i < o.length; i++) vTemp.addElement(o[i]);
            }
            s = new String[vTemp.size()];
            vTemp.copyInto(s);
            oTemp = CacheTool.getColumnValue("bd_taxitems", "pk_taxitems", "taxratio", s);
            if (oTemp != null) {
              o = (Object[])(Object[])oTemp;
              if ((o != null) && (o.length > 0))
                for (int i = 0; i < o.length; i++) {
                  if (o[i] == null) continue; hTaxRate.put(sTemp[i], o[i]);
                }
            }
          }
        }
      }
      catch (Exception e) {
        e.printStackTrace();
        return;
      }

    }

    vTemp = new Vector();
    while (curGeneralHI < hVOs.length) {
      int endPos = beginPos + hVOs[curGeneralHI].getChildrenVO().length - 1;

      GeneralBillHeaderVO headVO = (GeneralBillHeaderVO)hVOs[curGeneralHI].getParentVO();
      String strGeneralHCode = headVO.getVbillcode();
      String strVendor = headVO.getCproviderid();

      GeneralBillItemVO[] itemVOs = (GeneralBillItemVO[])(GeneralBillItemVO[])hVOs[curGeneralHI].getChildrenVO();
      for (int j = 0; j < itemVOs.length; j++) {
        itemVOs[j].setAttributeValue("pk_corp", headVO.getPk_corp());
        vTemp.addElement(itemVOs[j]);
      }

      for (int j = beginPos; j <= endPos; j++)
      {
        getUIPanelData().getBillModel().addLine();

        getUIPanelData().setBodyValueAt(null, j, "ccurrencytypeid");
        getUIPanelData().setBodyValueAt(null, j, "noriginalcurprice");
        getUIPanelData().setBodyValueAt(null, j, "noriginalcurmny");
        getUIPanelData().setBodyValueAt(null, j, "ntaxrate");
        getUIPanelData().setBodyValueAt(null, j, "noriginaltaxmny");
        getUIPanelData().setBodyValueAt(null, j, "noriginalsummny");
        getUIPanelData().setBodyValueAt(null, j, "ispresent");
        getUIPanelData().setBodyValueAt(null, j, "norgnettaxprice");
        getUIPanelData().setBodyValueAt(null, j, "idiscounttaxtype");

        if ((orderItemVOs != null) && (orderItemVOs[j] != null))
          setMnyDigit(orderItemVOs[j].getCcurrencytypeid());
        else {
          setMnyDigit(PiPqPublicUIClass.getNativeCurrencyID());
        }

        getUIPanelData().setBodyValueAt(strOrderCodes[j], j, "vordercode");

        if ((orderItemVOs != null) && (orderItemVOs[j] != null)) {
          getUIPanelData().setBodyValueAt(orderItemVOs[j].getCcurrencytypeid(), j, "ccurrencytypeid");

          getUIPanelData().setBodyValueAt(orderItemVOs[j].getNoriginalnetprice(), j, "noriginalcurprice");
          getUIPanelData().setBodyValueAt(orderItemVOs[j].getNoriginalcurmny(), j, "noriginalcurmny");
          getUIPanelData().setBodyValueAt(orderItemVOs[j].getNtaxrate(), j, "ntaxrate");
          getUIPanelData().setBodyValueAt(orderItemVOs[j].getNoriginaltaxmny(), j, "noriginaltaxmny");
          getUIPanelData().setBodyValueAt(orderItemVOs[j].getNoriginaltaxpricemny(), j, "noriginalsummny");
          getUIPanelData().setBodyValueAt(itemVOs[(j - beginPos)].getFlargess(), j, "ispresent");

          getUIPanelData().setBodyValueAt(orderItemVOs[j].getNorgnettaxprice(), j, "norgnettaxprice");

          if (orderItemVOs[j].getIdiscounttaxtype() == null) {
            orderItemVOs[j].setIdiscounttaxtype(new Integer(1));
          }
          getUIPanelData().setBodyValueAt(((UIComboBox)getUIPanelData().getBodyItem("idiscounttaxtype").getComponent()).getItemAt(Integer.parseInt(orderItemVOs[j].getIdiscounttaxtype().toString())), j, "idiscounttaxtype");
        }
        else
        {
          if (headVO.getCproviderid() != null) {
            Object oTemp = this.m_hCurrTypeID.get(headVO.getCproviderid());
            if (oTemp != null)
              getUIPanelData().setBodyValueAt(oTemp, j, "ccurrencytypeid");
            else
              getUIPanelData().setBodyValueAt(this.m_sCurrTypeID, j, "ccurrencytypeid");
          } else {
            getUIPanelData().setBodyValueAt(this.m_sCurrTypeID, j, "ccurrencytypeid");
          }

          getUIPanelData().setBodyValueAt(itemVOs[(j - beginPos)].getNprice(), j, "noriginalcurprice");
          getUIPanelData().setBodyValueAt(itemVOs[(j - beginPos)].getNmny(), j, "noriginalcurmny");

          Object oTemp = null;
          if (itemVOs[(j - beginPos)].getCinventoryid() != null)
            oTemp = hTaxRate.get(itemVOs[(j - beginPos)].getCinventoryid());
          if (oTemp != null) {
            getUIPanelData().setBodyValueAt(oTemp, j, "ntaxrate");
          } else {
            getUIPanelData().setBodyValueAt(new UFDouble(0), j, "ntaxrate");
            getUIPanelData().setBodyValueAt(new UFDouble(0), j, "noriginaltaxmny");
            getUIPanelData().setBodyValueAt(itemVOs[(j - beginPos)].getNmny(), j, "noriginalsummny");
            getUIPanelData().setBodyValueAt(itemVOs[(j - beginPos)].getNprice(), j, "norgnettaxprice");
          }

          getUIPanelData().setBodyValueAt(itemVOs[(j - beginPos)].getFlargess(), j, "ispresent");

          if (itemVOs[(j - beginPos)].getNtaxmny() != null) {
            getUIPanelData().setBodyValueAt(itemVOs[(j - beginPos)].getNtaxmny().sub(itemVOs[(j - beginPos)].getNmny()), j, "noriginaltaxmny");
            getUIPanelData().setBodyValueAt(itemVOs[(j - beginPos)].getNtaxmny(), j, "noriginalsummny");
            getUIPanelData().setBodyValueAt(itemVOs[(j - beginPos)].getNtaxmny().div(itemVOs[(j - beginPos)].getNinnum()), j, "norgnettaxprice");
          }

          getUIPanelData().setBodyValueAt(((UIComboBox)getUIPanelData().getBodyItem("idiscounttaxtype").getComponent()).getItemAt(1), j, "idiscounttaxtype");
        }

        getUIPanelData().setBodyValueAt(strGeneralHCode, j, "vgeneralcode");
        getUIPanelData().setBodyValueAt(strVendor, j, "cvendormangid");

        getUIPanelData().setBodyValueAt(itemVOs[(j - beginPos)].getCinventoryid(), j, "cmangid");
        getUIPanelData().setBodyValueAt(itemVOs[(j - beginPos)].getNinnum(), j, "ninnum");

        getUIPanelData().setBodyValueAt(headVO.getPk_corp(), j, "pk_stockcorp");

        if ((bb3VOs != null) && (bb3VOs[j] != null)) {
          getUIPanelData().setBodyValueAt(bb3VOs[j].getNsignnum(), j, "nsignnum");
        }

        if ((bb3VOs != null) && (bb3VOs[j] != null)) {
          getUIPanelData().setBodyValueAt(bb3VOs[j].getNaccountnum1(), j, "naccountnum1");
        }

        if (itemVOs[(j - beginPos)].getNinnum() != null) {
          double dTemp = 0.0D;
          if ((bb3VOs != null) && (bb3VOs[j] != null)) {
            if (bb3VOs[j].getNsignnum() != null) {
              dTemp = bb3VOs[j].getNsignnum().doubleValue();
            }
            if (bb3VOs[j].getNaccountwastenum() != null) {
              dTemp -= bb3VOs[j].getNaccountwastenum().doubleValue();
            }
          }
          double dTemp1 = 0.0D;
          if ((bb3VOs != null) && (bb3VOs[j] != null) && 
            (bb3VOs[j].getNaccountnum1() != null)) {
            dTemp1 = bb3VOs[j].getNaccountnum1().doubleValue();
          }

          double max = 0.0D;
          if (dTemp > dTemp1)
            max = dTemp;
          else {
            max = dTemp1;
          }
          UFDouble dInvNum = itemVOs[(j - beginPos)].getNinnum().sub(max);
          getUIPanelData().setBodyValueAt(dInvNum, j, "ninvoicenum");

          this.hInvoiceNum.put(bb3VOs[j].getCgeneralbid(), dInvNum);

          this.m_defaultNumVEC.addElement(itemVOs[(j - beginPos)].getNinnum());
          if ((orderItemVOs == null) || (orderItemVOs[j] == null)) {
            this.m_defaultPriceVEC.addElement(null);
          } else {
            UFDouble dDefaultPrice = null;
            if ((orderItemVOs[j].getNordernum() != null) && 
              (orderItemVOs[j].getNordernum().doubleValue() != 0.0D) && (orderItemVOs[j].getNmoney() != null)) {
              dDefaultPrice = orderItemVOs[j].getNmoney().div(orderItemVOs[j].getNordernum());
            }

            this.m_defaultPriceVEC.addElement(dDefaultPrice);
          }
          if ((orderItemVOs != null) && (orderItemVOs[j] != null))
          {
            UFDouble ufInvNum = (UFDouble)getUIPanelData().getBodyValueAt(j, "ninvoicenum");
            UFDouble ufOrderNum = orderItemVOs[j].getNordernum();

            String strDisTaxName = getUIPanelData().getBodyValueAt(j, "idiscounttaxtype").toString();

            UFDouble dPrice = orderItemVOs[j].getNoriginalnetprice();

            UFDouble dTaxrate = orderItemVOs[j].getNtaxrate();

            UFDouble dNorgnettaxprice = orderItemVOs[j].getNorgnettaxprice();
            String pk_corp = orderItemVOs[j].getPk_corp();
            if ((ufInvNum.compareTo(ufOrderNum) != 0) && 
              (orderItemVOs != null) && (getUIPanelData().getBodyValueAt(j, "idiscounttaxtype") != null)) {
              InvFrmOrdUI.calInitRelations(getUIPanelData(), j, dInvNum, strDisTaxName, dPrice, dTaxrate, dNorgnettaxprice, pk_corp);
            }
          }
          else
          {
            UFDouble dPrice = itemVOs[(j - beginPos)].getNprice();
            UFDouble dMoney = dInvNum.multiply(dPrice);
            getUIPanelData().setBodyValueAt(dMoney, j, "noriginalcurmny");

            Object oValue = getUIPanelData().getBodyValueAt(j, "noriginalcurmny");
            if (oValue == null)
              dMoney = VariableConst.ZERO;
            else {
              dMoney = new UFDouble(oValue.toString());
            }

            UFDouble dTaxrate = null;
            oValue = getUIPanelData().getBodyValueAt(j, "ntaxrate");
            if (oValue != null)
              dTaxrate = new UFDouble(oValue.toString());
            if (dTaxrate == null) {
              dTaxrate = VariableConst.ZERO;
            }
            else {
              getUIPanelData().setBodyValueAt(dTaxrate, j, "ntaxrate");
              oValue = getUIPanelData().getBodyValueAt(j, "ntaxrate");
              if (oValue == null)
                dTaxrate = VariableConst.ZERO;
              else {
                dTaxrate = new UFDouble(oValue.toString());
              }
              dTaxrate = dTaxrate.div(100.0D);
            }

            UFDouble dTax = null;
            String strDisTaxName = getUIPanelData().getBodyValueAt(j, "idiscounttaxtype").toString();
            if (strDisTaxName == null) {
              dTax = VariableConst.ZERO;
            }
            else if (strDisTaxName.equals(NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000105"))) {
              if (dTaxrate.doubleValue() == 100.0D) {
                dTax = dMoney;
                dMoney = new UFDouble(0.0D);
                dPrice = dMoney;
              } else {
                dTax = dMoney.multiply(dTaxrate).div(new UFDouble(1.0D).sub(dTaxrate));
              }
            } else if (strDisTaxName.equals(NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000106")))
              dTax = dMoney.multiply(dTaxrate);
            else if (strDisTaxName.equals(NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000107")))
              dTax = VariableConst.ZERO;
            else {
              dTax = VariableConst.ZERO;
            }

            getUIPanelData().setBodyValueAt(dTax, j, "noriginaltaxmny");
            oValue = getUIPanelData().getBodyValueAt(j, "noriginaltaxmny");
            if (oValue == null)
              dTax = VariableConst.ZERO;
            else {
              dTax = new UFDouble(oValue.toString());
            }

            UFDouble dSummny = dMoney.add(dTax);
            UFDouble dTaxPrice = dSummny.div(dInvNum);

            getUIPanelData().setBodyValueAt(dTaxPrice, j, "norgnettaxprice");
            getUIPanelData().setBodyValueAt(dTax, j, "noriginaltaxmny");
            getUIPanelData().setBodyValueAt(dSummny, j, "noriginalsummny");
          }
        }
        else
        {
          this.m_defaultNumVEC.addElement(null);
          this.m_defaultPriceVEC.addElement(null);
        }

        getUIPanelData().setBodyValueAt(itemVOs[(j - beginPos)].getVfree6(), j, "vfree0");

        getUIPanelData().setBodyValueAt(itemVOs[(j - beginPos)].getVbatchcode(), j, "vproducenum");
      }

      newBeginRows[curGeneralHI] = beginPos;
      newEndRows[curGeneralHI] = endPos;

      beginPos = endPos + 1;
      curGeneralHI++;
    }

    getUIPanelData().getBillModel().execLoadFormula();

    setGeneralBillVOs(hVOs);
    setOrderItemVOs(orderItemVOs);

    setSelectedRowCount(0);
    setShowBeginRows(newBeginRows);
    setShowEndRows(newEndRows);
    setRetVOs(null);
    setDistributedRows((Integer[][])null);

    if (vTemp.size() > 0) {
      this.m_GeneralHItemVOs = new GeneralBillItemVO[vTemp.size()];
      vTemp.copyInto(this.m_GeneralHItemVOs);
    } else {
      this.m_GeneralHItemVOs = null;
    }
  }

  private void initTitle()
  {
    if (getBillType().equals("45"))
      setTitle(NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000127"));
    else if (getBillType().equals("47"))
      setTitle(NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000128"));
    else if (getBillType().equals("4T"))
      setTitle(NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000129"));
  }

  private void setGeneralBillVOs(GeneralBillVO[] newGeneralHVOs)
  {
    this.m_GeneralHVOs = newGeneralHVOs;
  }

  private void setMnyDigit(String strCurr)
  {
    if (!getHashMnyDigit().containsKey(strCurr)) {
      getHashMnyDigit().put(strCurr, new Integer(POPubSetUI.getMoneyDigitByCurr_Busi(strCurr)));
    }

    int nDigit = ((Integer)getHashMnyDigit().get(strCurr)).intValue();

    getUIPanelData().getBodyItem("noriginalcurmny").setDecimalDigits(nDigit);
    getUIPanelData().getBodyItem("noriginaltaxmny").setDecimalDigits(nDigit);
    getUIPanelData().getBodyItem("noriginalsummny").setDecimalDigits(nDigit);
  }

  private void cacheWareHouse(GeneralBillVO[] billVOs)
  {
    if ((billVOs == null) || (billVOs.length == 0))
      return;
    int size = billVOs.length;
    for (int i = 0; i < size; i++) {
      if (billVOs[i] == null)
        continue;
      GeneralBillHeaderVO hVO = (GeneralBillHeaderVO)billVOs[i].getParentVO();
      String sGeneralhid = hVO.getCgeneralhid();
      String sWareHouseId = hVO.getCwarehouseid();
      if ((sGeneralhid != null) && (sWareHouseId != null))
        this.hWareHouseId.put(sGeneralhid, sWareHouseId);
    }
  }

  private void delVfree(GeneralBillVO[] vos)
  {
    if ((vos == null) && (vos.length == 0)) {
      return;
    }

    FreeVOParse freeParse = new FreeVOParse();
    Vector vTemp = new Vector();
    for (int i = 0; i < vos.length; i++) {
      GeneralBillItemVO[] items = (GeneralBillItemVO[])(GeneralBillItemVO[])vos[i].getChildrenVO();
      if ((items != null) && (items.length > 0)) {
        for (int j = 0; j < items.length; j++) {
          if ((items[j].getVfree1() == null) && (items[j].getVfree2() == null) && (items[j].getVfree3() == null) && (items[j].getVfree4() == null) && (items[j].getVfree5() == null) && (items[j].getVfree6() == null) && (items[j].getVfree7() == null) && (items[j].getVfree8() == null) && (items[j].getVfree9() == null) && (items[j].getVfree10() == null))
          {
            continue;
          }

          vTemp.addElement(items[j]);
        }
      }
    }

    if (vTemp.size() > 0) {
      GeneralBillItemVO[] items = new GeneralBillItemVO[vTemp.size()];
      vTemp.copyInto(items);

      freeParse.setFreeVO(items, "cinvmanid", "cinventoryid", false);

      String sFreevalueTotal = null;

      for (int j = 0; j < items.length; j++) {
        sFreevalueTotal = null;
        if (items[j].getFreeItemVO() != null)
          sFreevalueTotal = items[j].getFreeItemVO().getWholeFreeItem();
        if ((sFreevalueTotal != null) && (sFreevalueTotal.trim().length() > 0))
          items[j].setVfree6(sFreevalueTotal);
      }
    }
  }

  public void keyPressed(KeyEvent e)
  {
    int keyCode = e.getKeyCode();
    switch (keyCode)
    {
    case 65:
      if ((this.ivjUIButtonSelectAll == null) || (!this.ivjUIButtonSelectAll.isEnabled())) break;
      onSelectAll();
      break;
    case 78:
      if ((this.ivjUIButtonDeselectAll == null) || (!this.ivjUIButtonDeselectAll.isEnabled())) break;
      onDeselectAll();
      break;
    case 70:
      if ((this.ivjUIButtonQuery == null) || (!this.ivjUIButtonQuery.isEnabled())) break;
      onQuery();
      break;
    case 71:
      if ((this.ivjUIButtonDisType == null) || (!this.ivjUIButtonDisType.isEnabled())) break;
      onDisType();
      break;
    case 79:
      if ((this.ivjUIButtonOK == null) || (!this.ivjUIButtonOK.isEnabled())) break;
      onGenInvoices();
      break;
    case 67:
      if ((this.ivjUIButtonCancel == null) || (!this.ivjUIButtonCancel.isEnabled())) break;
      super.closeOK();
      break;
    case 66:
    case 68:
    case 69:
    case 72:
    case 73:
    case 74:
    case 75:
    case 76:
    case 77:
    }
  }
  public void paint(Graphics g) { super.paint(g);
    if (!this.showed) {
      this.showed = true;
      repaint();
    }
  }

  /** @deprecated */
  private boolean processAfterChange(GeneralBillVO[] voaStock, InvoiceVO[] voaInvoice)
    throws Exception
  {
    if (voaInvoice == null)
      return true;
    try
    {
      Vector vTemp = new Vector();
      for (int i = 0; i < voaInvoice.length; i++) {
        InvoiceItemVO[] bodyVO = voaInvoice[i].getBodyVO();
        for (int j = 0; j < bodyVO.length; j++) {
          PricParaVO tempVO = new PricParaVO();
          tempVO.setCgeneralbid(bodyVO[j].getCupsourcebillrowid());

          vTemp.addElement(tempVO);
        }
      }
      PricParaVO[] VOs = new PricParaVO[vTemp.size()];
      vTemp.copyInto(VOs);
      VOs = PricStlHelper.queryPricStlPrices(VOs);
      Hashtable tHQHP = new Hashtable();
      if ((VOs != null) && (VOs.length > 0)) {
        for (int i = 0; i < VOs.length; i++) {
          if (VOs[i].getNtaxprice() == null) continue; tHQHP.put(VOs[i].getCgeneralbid(), VOs[i].getNtaxprice());
        }
      }

      for (int i = 0; i < voaInvoice.length; i++)
      {
        voaInvoice[i].setSource(2);

        voaInvoice[i].getHeadVO().setDr(new Integer(0));

        voaInvoice[i].getHeadVO().setDinvoicedate(ClientEnvironment.getInstance().getDate());
        voaInvoice[i].getHeadVO().setDarrivedate(ClientEnvironment.getInstance().getDate());
        voaInvoice[i].getHeadVO().setIbillstatus(new Integer(0));
        voaInvoice[i].getHeadVO().setFinitflag(new Integer(0));
        voaInvoice[i].getHeadVO().setIinvoicetype(new Integer(0));
        voaInvoice[i].getHeadVO().setIdiscounttaxtype(new Integer(1));

        String strOperPk = ClientEnvironment.getInstance().getUser().getPrimaryKey();
        voaInvoice[i].getHeadVO().setCoperator(strOperPk);

        voaInvoice[i].getHeadVO().setIdiscounttaxtype(new Integer(0));
        voaInvoice[i].getHeadVO().setNtaxrate(null);

        if ((voaInvoice[i].getHeadVO().getCvendormangid() != null) && (voaInvoice[i].getHeadVO().getCvendorbaseid() == null))
        {
          String pk_cubasdoc = null;
          Object oTemp = null;
          try {
            oTemp = CacheTool.getCellValue("bd_cumandoc", "pk_cumandoc", "pk_cubasdoc", voaInvoice[i].getHeadVO().getCvendormangid());
          } catch (Exception e) {
            e.printStackTrace();
          }
          if ((oTemp != null) && (((Object[])(Object[])oTemp).length >= 1) && 
            (((Object[])(Object[])oTemp)[0] != null) && (((Object[])(Object[])oTemp)[0].toString().trim().length() > 0)) pk_cubasdoc = ((Object[])(Object[])oTemp)[0].toString();

          voaInvoice[i].getHeadVO().setCvendorbaseid(pk_cubasdoc);
        }

        if (voaInvoice[i].getHeadVO().getPk_purcorp() == null) voaInvoice[i].getHeadVO().setPk_purcorp(ClientEnvironment.getInstance().getCorporation().getPrimaryKey());

        InvoiceItemVO[] voaInvItem = voaInvoice[i].getBodyVO();

        for (int j = 0; j < voaInvItem.length; j++)
        {
          int index = getDistributedRows()[i][j].intValue();

          UFDouble dOleInvoiceNum = (UFDouble)this.hInvoiceNum.get(((GeneralBillItemVO[])(GeneralBillItemVO[])voaStock[i].getChildrenVO())[j].getCgeneralbid());
          UFDouble dNowInvoiceNum = (UFDouble)getUIPanelData().getBodyValueAt(index, "ninvoicenum");
          if ((dOleInvoiceNum != null) && (dNowInvoiceNum != null) && (dOleInvoiceNum.doubleValue() * dNowInvoiceNum.doubleValue() < 0.0D)) {
            return false;
          }

          if ((voaInvItem[j].getCupsourcebillrowid() != null) && (tHQHP.get(voaInvItem[j].getCupsourcebillrowid()) != null))
          {
            getUIPanelData().setBodyValueAt(tHQHP.get(voaInvItem[j].getCupsourcebillrowid()), j, "norgnettaxprice");
            BillEditEvent event = new BillEditEvent(getUIPanelData().getBillTable(), tHQHP.get(voaInvItem[j].getCupsourcebillrowid()), "norgnettaxprice", j, 1);
            afterEditRelations(event);
          }

          if ((this.m_hasOrderInfo != null) && (this.m_hasOrderInfo.size() > 0) && (voaInvItem[j].getCupsourcebillrowid() != null) && (voaInvItem[j].getCupsourcebillrowid().trim().length() > 0)) {
            Object oTemp = this.m_hasOrderInfo.get(voaInvItem[j].getCupsourcebillrowid());
            if (oTemp != null) {
              String[] sInfo = (String[])(String[])oTemp;
              voaInvItem[j].setCorderid(sInfo[0]);
              voaInvItem[j].setCorder_bid(sInfo[1]);
              voaInvItem[j].setCcurrencytypeid(sInfo[2]);
            }

          }

          if (voaInvItem[j].getCcurrencytypeid() == null) {
            Object lObj_VendorMangid = getUIPanelData().getBodyValueAt(index, "cvendormangid");
            if ((lObj_VendorMangid != null) && (lObj_VendorMangid.toString().trim().length() > 0)) {
              Object oTemp = this.m_hCurrTypeID.get(lObj_VendorMangid);
              if (oTemp != null)
                voaInvItem[j].setCcurrencytypeid(oTemp.toString().trim());
              else
                voaInvItem[j].setCcurrencytypeid(this.m_sCurrTypeID);
            }
            else {
              voaInvItem[j].setCcurrencytypeid(this.m_sCurrTypeID);
            }

          }

          voaInvItem[j].setNinvoicenum(dNowInvoiceNum);
          voaInvItem[j].setNoriginalcurprice((UFDouble)getUIPanelData().getBodyValueAt(index, "noriginalcurprice"));
          voaInvItem[j].setNoriginalcurmny((UFDouble)getUIPanelData().getBodyValueAt(index, "noriginalcurmny"));
          voaInvItem[j].setNtaxrate((UFDouble)getUIPanelData().getBodyValueAt(index, "ntaxrate"));

          voaInvItem[j].setNoriginaltaxmny((UFDouble)getUIPanelData().getBodyValueAt(index, "noriginaltaxmny"));
          voaInvItem[j].setNoriginalsummny((UFDouble)getUIPanelData().getBodyValueAt(index, "noriginalsummny"));

          voaInvItem[j].setNorgnettaxprice((UFDouble)getUIPanelData().getBodyValueAt(index, "norgnettaxprice"));
          voaInvItem[j].setNreasonwastenum((UFDouble)getUIPanelData().getBodyValueAt(index, "nreasonwastenum"));

          Object ob = getUIPanelData().getBodyValueAt(index, "idiscounttaxtype");
          if (ob != null)
            voaInvItem[j].setIdiscounttaxtype((Integer)getUIPanelData().getBodyItem("idiscounttaxtype").converType(ob));
          else {
            voaInvItem[j].setIdiscounttaxtype(new Integer(1));
          }

          voaInvItem[j].setDr(new Integer(0));

          voaInvItem[j].setNexchangeotobrate(null);
          voaInvItem[j].setNexchangeotoarate(null);

          GeneralBillItemVO[] genItemVOs = (GeneralBillItemVO[])(GeneralBillItemVO[])voaStock[i].getChildrenVO();

          if ((this.m_hasUpSourceTS != null) && (this.m_hasUpSourceTS.size() > 0) && (voaInvItem[j].getCupsourcebillrowid() != null) && (voaInvItem[j].getCupsourcebillrowid().trim().length() > 0)) {
            Object oTemp = this.m_hasUpSourceTS.get(voaInvItem[j].getCupsourcebillrowid());
            if (oTemp != null) {
              String[] ts = (String[])(String[])oTemp;
              voaInvItem[j].setCupsourcehts(ts[0]);
              voaInvItem[j].setCupsourcebts(ts[1]);
            }
          }

          voaInvItem[j].setVmemo((String)genItemVOs[j].getAttributeValue("vnotebody"));

          String sCupsourceBillId = voaInvItem[j].getCupsourcebillid();

          if ((sCupsourceBillId != null) && (sCupsourceBillId.length() > 0) && (voaStock[i].getHeaderVO().getPk_corp().equals(voaInvoice[i].getHeadVO().getPk_corp()))) {
            String sWareHouseId = (String)this.hWareHouseId.get(sCupsourceBillId);
            if ((sWareHouseId != null) && (sWareHouseId.trim().length() > 0))
              voaInvItem[j].setCwarehouseid(sWareHouseId);
          } else {
            voaInvItem[j].setCwarehouseid(null);
          }

          if ((sCupsourceBillId == null) || (sCupsourceBillId.length() <= 0) || ((voaInvItem[j].getCsourcebillid() != null) && (voaInvItem[j].getCsourcebillid().length() != 0)))
            continue;
          voaInvItem[j].setCsourcebillid(sCupsourceBillId);
          voaInvItem[j].setCsourcebillrowid(voaInvItem[j].getCupsourcebillrowid());
          voaInvItem[j].setCsourcebilltype(voaInvItem[j].getCupsourcebilltype());
        }

      }

      String pk_invcorp = voaInvoice[0].getHeadVO().getPk_corp();
      for (int i = 0; i < voaInvoice.length; i++) {
        GeneralBillItemVO[] itemVO = voaStock[i].getItemVOs();
        for (int j = 0; j < itemVO.length; j++) {
          if (!itemVO[j].getAttributeValue("pk_corp").equals(pk_invcorp)) {
            voaInvoice[i].getHeadVO().setCstoreorganization(null);
            break;
          }
        }
      }
      Vector v = new Vector(); Vector vv = new Vector();
      for (int i = 0; i < voaStock.length; i++) {
        GeneralBillItemVO[] itemVO = voaStock[i].getItemVOs();
        for (int j = 0; j < itemVO.length; j++) {
          if ((!itemVO[j].getAttributeValue("pk_corp").equals(pk_invcorp)) && (!v.contains(itemVO[j].getCinvbasid()))) {
            v.addElement(itemVO[j].getCinvbasid());
            vv.addElement(itemVO[j].getAttributeValue("pk_corp"));
          }
        }
      }
      ChgDocPkVO[] chgInvVO = null;
      if (v.size() > 0) {
        chgInvVO = new ChgDocPkVO[v.size()];
        for (int i = 0; i < v.size(); i++) {
          chgInvVO[i] = new ChgDocPkVO();
          chgInvVO[i].setSrcBasId((String)v.elementAt(i));
          chgInvVO[i].setSrcCorpId((String)vv.elementAt(i));
          chgInvVO[i].setDstCorpId(pk_invcorp);
        }
        chgInvVO = ChgDataUtil.chgPkInvByCorpBase(chgInvVO);
      }

      for (int i = 0; i < voaInvoice.length; i++) {
        InvoiceItemVO[] itemVO = voaInvoice[i].getBodyVO();
        for (int j = 0; j < itemVO.length; j++) {
          if ((chgInvVO != null) && (chgInvVO.length > 0)) {
            for (int k = 0; k < chgInvVO.length; k++) {
              if (itemVO[j].getCbaseid().equals(chgInvVO[k].getSrcBasId())) {
                itemVO[j].setCmangid(chgInvVO[k].getDstManId());
                break;
              }
            }
          }
        }

      }

      v = new Vector();
      vv = new Vector();
      for (int i = 0; i < voaStock.length; i++) {
        GeneralBillHeaderVO headVO = voaStock[i].getHeaderVO();
        if ((headVO.getAttributeValue("pk_purcorp") == null) || 
          (pk_invcorp.equals(headVO.getAttributeValue("pk_purcorp"))) || (headVO.getCproviderid() == null) || (v.contains(headVO.getCproviderid()))) continue;
        v.addElement(headVO.getCproviderid());
        vv.addElement(headVO.getAttributeValue("pk_purcorp"));
      }

      ChgDocPkVO[] chgCuVO = null;
      if (v.size() > 0) {
        chgCuVO = new ChgDocPkVO[v.size()];
        for (int i = 0; i < v.size(); i++) {
          chgCuVO[i] = new ChgDocPkVO();
          chgCuVO[i].setSrcManId((String)v.elementAt(i));
          chgCuVO[i].setSrcCorpId((String)vv.elementAt(i));
          chgCuVO[i].setDstCorpId(pk_invcorp);
        }
        chgCuVO = ChgDataUtil.chgPkCuByCorp(chgCuVO);
      }

      for (int i = 0; i < voaInvoice.length; i++) {
        InvoiceHeaderVO headVO = voaInvoice[i].getHeadVO();
        if ((chgCuVO != null) && (chgCuVO.length > 0)) {
          for (int k = 0; k < chgCuVO.length; k++) {
            if ((headVO.getCvendormangid() != null) && (chgCuVO[k].getSrcManId() != null) && (headVO.getCvendormangid().equals(chgCuVO[k].getSrcManId()))) {
              headVO.setCvendorbaseid(chgCuVO[k].getSrcBasId());
              headVO.setCvendormangid(chgCuVO[k].getDstManId());
            }
          }
        }
      }

    }
    catch (Exception e)
    {
      throw e;
    }
    return true;
  }

  private void setOrderInfo()
  {
    if ((this.m_OrderItemVOs == null) || (this.m_OrderItemVOs.length == 0)) {
      this.m_hasOrderInfo = null;
      return;
    }

    this.m_hasOrderInfo = new Hashtable();
    for (int i = 0; i < this.m_GeneralHVOs.length; i++) {
      GeneralBillItemVO[] bodyVO = (GeneralBillItemVO[])(GeneralBillItemVO[])this.m_GeneralHVOs[i].getChildrenVO();
      if ((bodyVO != null) && (bodyVO.length != 0))
        for (int j = 0; j < bodyVO.length; j++) {
          String s1 = bodyVO[j].getCfirstbillbid();
          if ((s1 != null) && (s1.trim().length() != 0))
            for (int k = 0; k < this.m_OrderItemVOs.length; k++)
              if (this.m_OrderItemVOs[k] != null) {
                String s2 = this.m_OrderItemVOs[k].getCorder_bid();
                String s3 = this.m_OrderItemVOs[k].getCorderid();
                String s4 = this.m_OrderItemVOs[k].getCcurrencytypeid();
                if (s1.equals(s2)) {
                  this.m_hasOrderInfo.put(bodyVO[j].getCgeneralbid(), new String[] { s3, s2, s4 });
                  break;
                }
              }
        }
    }
    if (this.m_hasOrderInfo.size() == 0) this.m_hasOrderInfo = null;
  }

  private void setUpSourceTS()
  {
    if ((this.m_GeneralHVOs == null) || (this.m_GeneralHVOs.length == 0)) {
      this.m_hasUpSourceTS = null;
      return;
    }

    this.m_hasUpSourceTS = new Hashtable();
    for (int i = 0; i < this.m_GeneralHVOs.length; i++) {
      GeneralBillHeaderVO headVO = (GeneralBillHeaderVO)this.m_GeneralHVOs[i].getParentVO();
      GeneralBillItemVO[] bodyVO = (GeneralBillItemVO[])(GeneralBillItemVO[])this.m_GeneralHVOs[i].getChildrenVO();
      if ((bodyVO != null) && (bodyVO.length != 0))
        for (int j = 0; j < bodyVO.length; j++) this.m_hasUpSourceTS.put(bodyVO[j].getCgeneralbid(), new String[] { headVO.getTs(), bodyVO[j].getTs() });
    }
    if (this.m_hasUpSourceTS.size() == 0) this.m_hasUpSourceTS = null;
  }

  public void valueChanged(ListSelectionEvent e)
  {
    BillCardPanel billPanel = getUIPanelData();

    int iCount = billPanel.getBillTable().getRowCount();

    for (int i = 0; i < iCount; i++) {
      billPanel.getBillModel().setRowState(i, 0);
    }

    int[] iaSelectedRow = billPanel.getBillTable().getSelectedRows();
    if ((iaSelectedRow == null) || (iaSelectedRow.length == 0)) {
      setSelectedRowCount(0);
    }
    else {
      iCount = iaSelectedRow.length;

      for (int i = 0; i < iCount; i++)
        billPanel.getBillModel().setRowState(iaSelectedRow[i], 4);
      setSelectedRowCount(iCount);

      getUIButtonDeselectAll().setEnabled(true);
      getUIButtonSelectAll().setEnabled(true);
      getUIButtonDisType().setEnabled(true);
      getUIButtonOK().setEnabled(true);
    }
  }
}