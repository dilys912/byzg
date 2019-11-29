package nc.ui.ct.pub;

import java.awt.Container;
import java.util.Hashtable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;
import nc.ui.bd.b20.CurrtypeBO_Client;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListData;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillTabbedPane;
import nc.ui.pub.para.SysInitBO_Client;
import nc.ui.pub.pf.BillSourceDLG;
import nc.ui.pub.pf.PfUtilBO_Client;
import nc.ui.scm.inv.InvTool;
import nc.ui.scm.pub.PubSetUI;
import nc.ui.scm.pub.def.DefSetTool;
import nc.vo.bd.b20.CurrtypeVO;
import nc.vo.bd.b21.CurrinfoVO;
import nc.vo.bd.def.DefVO;
import nc.vo.ct.pub.ManageHeaderVO;
import nc.vo.ct.pub.ManageItemVO;
import nc.vo.ct.pub.ManageVO;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.scm.ic.bill.FreeVO;
import nc.vo.scm.pub.SCMEnv;

public class CtBillSourceDLg extends BillSourceDLG
  implements ListSelectionListener
{
  protected Hashtable htCurr = null;
  protected String m_sPkCorp = null;
  protected Hashtable htRateDigit = null;

  protected CtBillSourceDLg(String pkField, String pkCorp, String operator, String funNode, String queryWhere, String billType, String businessType, String templateId, String currentBillType, Container parent)
  {
    super(pkField, pkCorp, operator, funNode, queryWhere, billType, businessType, templateId, currentBillType, parent);

    BillListData bld = getbillListPanel().getBillListData();
    changeBillListDataByUserDef(bld, billType);
    setListPanelByPara(bld);
    getbillListPanel().setListData(bld);

    this.m_sPkCorp = pkCorp;
    getCurrDigit();

    setDigit();
    addListener();
    if ("30".equals(currentBillType)) {
      int i = 1; for (int loop = getbillListPanel().getBodyTabbedPane().getTabCount(); i < loop; i++)
        getbillListPanel().getBodyTabbedPane().removeTabAt(1);
    }
  }

  protected void addListener()
  {
    getbillListPanel().getHeadTable().getSelectionModel().addListSelectionListener(this);
  }

  protected BillListData changeBillListDataByUserDef(BillListData oldBillData, String billType)
  {
    DefVO[] defs = null;

    defs = DefSetTool.getDefHead(this.m_sPkCorp, billType);
    if (defs != null)
    {
      oldBillData.updateItemByDef(defs, "def", true);
    }

    defs = DefSetTool.getDefBody(this.m_sPkCorp, billType);
    if (defs == null) {
      return oldBillData;
    }

    oldBillData.updateItemByDef(defs, "def", false);

    return oldBillData;
  }

  protected void getAllRateDigit()
  {
    this.htRateDigit = new Hashtable();
    try {
      CurrinfoVO[] cinfos = ContractQueryHelper.queryAllRateDigit(this.m_sPkCorp);
      if ((cinfos != null) && (cinfos.length > 0))
        for (int i = 0; i < cinfos.length; i++)
          this.htRateDigit.put(cinfos[i].getPk_currtype(), cinfos[i].getRatedigit());
    }
    catch (Exception e)
    {
      SCMEnv.out(e);
    }
  }

  protected void getCurrDigit()
  {
    try
    {
      this.htCurr = new Hashtable();

      CurrtypeVO[] vos = CurrtypeBO_Client.queryAll(null);

      if ((vos != null) || (vos.length > 0))
      {
        for (int i = 0; i < vos.length; i++) {
          this.htCurr.put(vos[i].getPk_currtype(), vos[i].getCurrbusidigit());
        }
      }
      else
      {
        MessageDialog.showErrorDlg(this, null, NCLangRes.getInstance().getStrByID("4020pub", "UPPSCMCommon-000015"));
      }
    }
    catch (Exception e)
    {
      MessageDialog.showErrorDlg(this, null, NCLangRes.getInstance().getStrByID("4020pub", "UPP4020pub-000020") + "\n" + e.getMessage());
    }
  }

  public String getHeadCondition()
  {
    String sPkcorp = getPkCorp();

    String sHeadCondition = "ct.pk_corp ='" + sPkcorp + "' or scope.pk_scopecorp = '" + sPkcorp + "'";

    return sHeadCondition;
  }

  public boolean getIsBusinessType()
  {
    return false;
  }

  public void loadHeadData()
  {
    try
    {
      String tmpWhere = null;
      if (getHeadCondition() != null) {
        if (this.m_whereStr == null)
          tmpWhere = " (" + getHeadCondition() + ")";
        else
          tmpWhere = this.m_whereStr + " and (" + getHeadCondition() + ")";
      }
      else {
        tmpWhere = this.m_whereStr;
      }
      String businessType = null;
      if (getIsBusinessType()) {
        businessType = getBusinessType();
      }
      ManageHeaderVO[] tmpHeadVo = (ManageHeaderVO[])(ManageHeaderVO[])PfUtilBO_Client.queryHeadAllData(getBillType(), businessType, tmpWhere);

      getAllRateDigit();

      getbillListPanel().getHeadBillModel().execLoadFormula();

      if ((tmpHeadVo != null) && (tmpHeadVo.length > 0)) {
        String[] sCurrid = new String[tmpHeadVo.length];
        for (int i = 0; i < tmpHeadVo.length; i++) {
          sCurrid[i] = tmpHeadVo[i].getCurrid();
        }
        setRateDigit(sCurrid);
      }

      getbillListPanel().getHeadItem("currrate").setDecimalDigits(20);
      getbillListPanel().setHeaderValueVO(tmpHeadVo);
      getbillListPanel().getHeadBillModel().execLoadFormula();

      getbillListPanel().getHeadTable().clearSelection();
    }
    catch (Exception e)
    {
      SCMEnv.out("数据加载失败！");
      SCMEnv.out(e);
    }
  }

  public void loadBodyData(int row)
  {
    try
    {
      String id = getbillListPanel().getHeadBillModel().getValueAt(row, getpkField()).toString();
      CircularlyAccessibleValueObject[] tmpBodyVo = PfUtilBO_Client.queryBodyAllData(getBillType(), id, getBodyCondition());

      String sMangId = null;
      FreeVO voFree = null;
      for (int i = 0; i < tmpBodyVo.length; i++) {
        sMangId = (String)tmpBodyVo[i].getAttributeValue("invid");
        String dpzk=String.valueOf(tmpBodyVo[i].getAttributeValue("dpzk"));
        String tsbl=String.valueOf(tmpBodyVo[i].getAttributeValue("tsbl"));
        //lcq add by 2015/6/8  销售订单去掉提示
        //MessageDialog.showErrorDlg(this, "审核检验单",dpzk+","+ tsbl);
        //lcq end by 2915/6/8
        voFree = InvTool.getInvFreeVO(sMangId);

        if (voFree != null) {
          voFree.setVfree1((String)tmpBodyVo[i].getAttributeValue("vfree1"));
          voFree.setVfree2((String)tmpBodyVo[i].getAttributeValue("vfree2"));
          voFree.setVfree3((String)tmpBodyVo[i].getAttributeValue("vfree3"));
          voFree.setVfree4((String)tmpBodyVo[i].getAttributeValue("vfree4"));
          voFree.setVfree5((String)tmpBodyVo[i].getAttributeValue("vfree5"));

          tmpBodyVo[i].setAttributeValue("vfree0", voFree.getWholeFreeItem());
        }
      }

      getbillListPanel().setBodyValueVO(tmpBodyVo);
      getbillListPanel().getBodyBillModel().execLoadFormula();
    } catch (Exception e) {
      SCMEnv.out("数据加载失败!");
      SCMEnv.out(e);
    }
  }

  protected void setDigit()
  {
    try
    {
      String sLocalCurrID = SysInitBO_Client.getPkValue(this.m_sPkCorp, "BD301");
      int mainCurrDigit = 2;
      if (sLocalCurrID != null) {
        mainCurrDigit = ((Integer)this.htCurr.get(sLocalCurrID)).intValue();
      }

      int priceDigitInt = 2;
      Integer priceDigit = SysInitBO_Client.getParaInt(this.m_sPkCorp, "BD505");
      if (priceDigit != null) {
        priceDigitInt = priceDigit.intValue();
      }

      int amountDigitInt = 2;
      Integer amountDigit = SysInitBO_Client.getParaInt(this.m_sPkCorp, "BD501");
      if (amountDigit != null) {
        amountDigitInt = amountDigit.intValue();
      }

      getbillListPanel().getBodyItem("amount").setDecimalDigits(amountDigitInt);
      getbillListPanel().getBodyItem("ordnum").setDecimalDigits(amountDigitInt);
      getbillListPanel().getBodyItem("transrate").setDecimalDigits(amountDigitInt);
      getbillListPanel().getBodyItem("oriprice").setDecimalDigits(priceDigitInt);
      getbillListPanel().getBodyItem("natiprice").setDecimalDigits(priceDigitInt);
      getbillListPanel().getBodyItem("oritaxprice").setDecimalDigits(priceDigitInt);
      getbillListPanel().getBodyItem("natitaxprice").setDecimalDigits(priceDigitInt);
      getbillListPanel().getBodyItem("oritaxmny").setDecimalDigits(priceDigitInt);
      getbillListPanel().getBodyItem("oritaxsummny").setDecimalDigits(priceDigitInt);

      getbillListPanel().getBodyItem("natisum").setDecimalDigits(mainCurrDigit);
      getbillListPanel().getBodyItem("natitaxmny").setDecimalDigits(mainCurrDigit);
      getbillListPanel().getBodyItem("natitaxsummny").setDecimalDigits(mainCurrDigit);

      int fracAmountDigitInt = 2;
      Integer fracAmountDigit = SysInitBO_Client.getParaInt(this.m_sPkCorp, "BD502");
      if (fracAmountDigit != null) {
        fracAmountDigitInt = fracAmountDigit.intValue();
      }
      String sFracCurrID = SysInitBO_Client.getPkValue(this.m_sPkCorp, "BD303");
      int fracCurrDigit = 2;
      if ((sFracCurrID != null) && (sFracCurrID.trim().length() > 0)) {
        fracCurrDigit = ((Integer)this.htCurr.get(sFracCurrID)).intValue();
      }
      getbillListPanel().getBodyItem("astnum").setDecimalDigits(fracAmountDigitInt);
      getbillListPanel().getBodyItem("astprice").setDecimalDigits(priceDigitInt);
      getbillListPanel().getBodyItem("asttaxprice").setDecimalDigits(priceDigitInt);

      getbillListPanel().getBodyItem("astsum").setDecimalDigits(fracCurrDigit);
      getbillListPanel().getBodyItem("asttaxmny").setDecimalDigits(fracCurrDigit);
      getbillListPanel().getBodyItem("asttaxsummny").setDecimalDigits(fracCurrDigit);
    }
    catch (Exception e)
    {
      SCMEnv.out(e);
    }
  }

  protected void setListPanelByPara(BillListData bdData)
  {
    UIComboBox comContractStatusItem = (UIComboBox)bdData.getHeadItem("ctflag").getComponent();

    comContractStatusItem.setTranslate(true);

    comContractStatusItem.addItem(NCLangRes.getInstance().getStrByID("4020const", "UPP4020const-000009"));
    comContractStatusItem.addItem(NCLangRes.getInstance().getStrByID("4020const", "UPP4020const-000010"));
    comContractStatusItem.addItem(NCLangRes.getInstance().getStrByID("4020const", "UPP4020const-000011"));
    comContractStatusItem.addItem(NCLangRes.getInstance().getStrByID("4020const", "UPP4020const-000012"));
    comContractStatusItem.addItem(NCLangRes.getInstance().getStrByID("4020const", "UPP4020const-000013"));
    comContractStatusItem.addItem(NCLangRes.getInstance().getStrByID("4020const", "UPP4020const-000014"));
    comContractStatusItem.addItem(NCLangRes.getInstance().getStrByID("4020const", "UPP4020const-000015"));

    comContractStatusItem.setSelectedIndex(0);
    bdData.getHeadItem("ctflag").setComponent(comContractStatusItem);
    bdData.getHeadItem("ctflag").setWithIndex(true);
  }

  protected void setRateDigit(String[] sCurrid)
  {
    int[] iRateDecimals = new int[sCurrid.length];
    int[] iAstRateDecimals = new int[sCurrid.length];
    for (int i = 0; i < sCurrid.length; i++) {
      int[] iTemp = PubSetUI.getBothExchRateDigit(this.m_sPkCorp, sCurrid[i]);
      iRateDecimals[i] = iTemp[0];
      iAstRateDecimals[i] = iTemp[1];
    }

    BillListData bd = getbillListPanel().getBillListData();
    try
    {
      BillItem itemCur = bd.getHeadItem("currrate");
      MyTableCellRenderer CurcellRenderer = new MyTableCellRenderer(itemCur, true, false);
      CurcellRenderer.setPrecision(iRateDecimals);
      if ((itemCur != null) && (itemCur.isShow())) {
        TableColumn CurColumn = getbillListPanel().getHeadTable().getColumn(itemCur.getName());

        CurColumn.setCellRenderer(CurcellRenderer);
      } else if (itemCur != null) {
        itemCur.setDecimalDigits(20);
      }
    }
    catch (Exception e) {
    }
    try {
      BillItem itemAstCur = bd.getHeadItem("astcurrate");
      MyTableCellRenderer AstCurcellRenderer = new MyTableCellRenderer(itemAstCur, true, false);
      AstCurcellRenderer.setPrecision(iAstRateDecimals);
      if ((itemAstCur != null) && (itemAstCur.isShow())) {
        TableColumn ActCurColumn = getbillListPanel().getHeadTable().getColumn(itemAstCur.getName());

        ActCurColumn.setCellRenderer(AstCurcellRenderer);
      } else if (itemAstCur != null) {
        itemAstCur.setDecimalDigits(20);
      }
    }
    catch (Exception e)
    {
    }
  }

  public void valueChanged(ListSelectionEvent e)
  {
    int row = getbillListPanel().getHeadTable().getSelectedRow();
    if ((row >= 0) && (row < getbillListPanel().getHeadTable().getRowCount()))
    {
      ManageVO manageVO = (ManageVO)getbillListPanel().getBillValueVO(row, ManageVO.class.getName(), ManageHeaderVO.class.getName(), ManageItemVO.class.getName());
      String sOriCurrid = manageVO.getParentVO().getCurrid();

      int iOriCurrDigit = 2;
      if ((this.htCurr != null) && (sOriCurrid != null) && (this.htCurr.get(sOriCurrid) != null)) {
        iOriCurrDigit = ((Integer)this.htCurr.get(sOriCurrid)).intValue();
      }

      if (getbillListPanel().getBodyItem("orisum") != null)
        getbillListPanel().getBodyItem("orisum").setDecimalDigits(iOriCurrDigit);
      if (getbillListPanel().getBodyItem("oritaxmny") != null)
        getbillListPanel().getBodyItem("oritaxmny").setDecimalDigits(iOriCurrDigit);
      if (getbillListPanel().getBodyItem("oritaxsummny") != null)
        getbillListPanel().getBodyItem("oritaxsummny").setDecimalDigits(iOriCurrDigit);
      if (getbillListPanel().getBodyItem("ordsum") != null)
        getbillListPanel().getBodyItem("ordsum").setDecimalDigits(iOriCurrDigit);
    }
  }
}