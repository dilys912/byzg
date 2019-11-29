package nc.ui.pu.pub;

import java.awt.Component;
import java.awt.Container;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.sf.ICreateCorpQueryService;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.bd.ref.DefaultRefModel;
import nc.ui.ic.pub.lot.LotNumbRefPane;
import nc.ui.ic.service.IICPub_LotNumbRefPane;
import nc.ui.ml.NCLangRes;
import nc.ui.pi.pub.PiPqPublicUIClass;
import nc.ui.po.pub.PoPublicUIClass;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.beans.table.TableCellRendererLabel;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.formulaparse.FormulaParse;
import nc.ui.pub.para.SysInitBO_Client;
import nc.ui.pub.print.IDataSource;
import nc.ui.pub.print.PrintEntry;
import nc.ui.pub.report.ReportBaseClass;
import nc.ui.scm.exp.ExcpTool;
import nc.ui.scm.ic.freeitem.DefHelper;
import nc.ui.scm.ic.freeitem.FreeItemRefPane;
import nc.ui.scm.inv.InvTool;
import nc.ui.scm.pu.ParaVOForBatch;
import nc.ui.scm.pub.BusiBillManageTool;
import nc.ui.scm.pub.panel.BillPanelTool;
import nc.ui.scm.pub.print.ScmPrintTool;
import nc.ui.scm.pub.report.ShowItemsDlg;
import nc.ui.scm.sourcebill.SourceBillTool;
import nc.vo.pi.InvoiceHeaderVO;
import nc.vo.pi.InvoiceVO;
import nc.vo.pp.ask.PriceauditHeaderVO;
import nc.vo.pp.ask.PriceauditMergeVO;
import nc.vo.pr.pray.PraybillHeaderVO;
import nc.vo.pr.pray.PraybillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.QueryConditionVO;
import nc.vo.scm.ic.bill.FreeVO;
import nc.vo.scm.ic.bill.InvVO;
import nc.vo.scm.ic.bill.WhVO;
import nc.vo.scm.pu.BillStatus;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pu.VariableConst;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.VOTool;

public class PuTool
{
  private static final int _PU_BILL_HEAD_ITEM_MAX_LEN_ = 500;
  private static HashMap m_hmapPUDefaultAssUnit = null;

  private static HashMap m_hmapParaPricePolicy = null;

  private static Hashtable m_hashCurrency = null;

  private static Hashtable m_hashAutoSwitchPUAssistUnit = null;

  private static Hashtable m_hashProductEnabled = null;

  private static FormulaParse m_formulaParse = null;

  private static Hashtable m_varry = null;
  public static final int _STYLE_IC = 0;
  public static final int _STYLE_NORMAL = -999;

  /** @deprecated */
  public static void onPreview(PrintEntry printEntry, IDataSource ds)
  {
    ScmPrintTool.onPreview(printEntry, ds);
  }

  public static double getRoundDouble(int decimal, double d)
  {
    double temp = 1.0D;
    for (int i = 1; i <= decimal; i++) temp *= 10.0D;

    long i = Math.round(d * temp);
    double dd = i / temp;

    return dd;
  }

  /** @deprecated */
  public static void onPrint(PrintEntry printEntry, IDataSource ds)
  {
    ScmPrintTool.onPrint(printEntry, ds);
  }

  public static String getDefaultPUAssUnit(String sBaseId)
  {
    loadBatchDefaultPUAssUnit(new String[] { sBaseId });

    Object oValue = m_hmapPUDefaultAssUnit.get(sBaseId);
    if ((oValue == null) || (oValue.equals(""))) {
      return null;
    }
    return (String)oValue;
  }

  public static void loadBatchDefaultPUAssUnit(String[] saBaseId)
  {
    if (saBaseId == null) {
      return;
    }

    String[] saDistinctId = PuPubVO.getDistinctArray(saBaseId);
    if (saDistinctId == null) {
      return;
    }
    if (m_hmapPUDefaultAssUnit == null) {
      m_hmapPUDefaultAssUnit = new HashMap();
    }
    saDistinctId = PuPubVO.getNotExistedKeys(m_hmapPUDefaultAssUnit, saDistinctId);

    if (saDistinctId == null) {
      return;
    }

    Object[] oaAssUnit = null;
    try {
      oaAssUnit = (Object[])(Object[])nc.ui.scm.pub.CacheTool.getColumnValue("bd_invbasdoc", "pk_invbasdoc", "pk_measdoc2", saBaseId);
    }
    catch (Exception ee)
    {
      SCMEnv.out(ee);
    }
    int iLen = saDistinctId.length;
    for (int i = 0; i < iLen; i++)
      if ((oaAssUnit == null) || (PuPubVO.getString_TrimZeroLenAsNull(oaAssUnit[i]) == null))
      {
        m_hmapPUDefaultAssUnit.put(saDistinctId[i], "");
      }
      else m_hmapPUDefaultAssUnit.put(saDistinctId[i], oaAssUnit[i].toString());
  }

  public static String getCurrName(String sCurrId)
  {
    if ((sCurrId == null) || (sCurrId.trim().length() < 1)) {
      return null;
    }

    if (m_hashCurrency == null) {
      m_hashCurrency = new Hashtable();
    }

    if (!m_hashCurrency.containsKey(sCurrId)) {
      m_hashCurrency.put(sCurrId, PiPqPublicUIClass.getAResultFromTable("bd_currtype", "currtypename", "pk_currtype", sCurrId));
    }

    return (String)m_hashCurrency.get(sCurrId);
  }

  public static void afterEditWareValidWithStoreOrg(BillModel billModel, BillEditEvent e, String sCalBodyId, String sWareIdKey, String[] saKeyBodyWarehouse)
  {
    if ((billModel == null) || (PuPubVO.getString_TrimZeroLenAsNull(sCalBodyId) == null) || (sWareIdKey == null))
    {
      SCMEnv.out("方法afterEditWareValidWithStoreOrg(BillModel, BillEditEvent, String, String, String [])传入参数错误！");
      return;
    }

    int iRow = e.getRow();
    String sWareId = PuPubVO.getString_TrimZeroLenAsNull(billModel.getValueAt(iRow, sWareIdKey));

    if (sWareId == null) {
      return;
    }

    Object[] oaRet = null;
    try {
      oaRet = (Object[])(Object[])nc.ui.scm.pub.CacheTool.getCellValue("bd_stordoc", "pk_stordoc", "pk_calbody", sWareId);
    }
    catch (Exception ee)
    {
      SCMEnv.out(ee);
    }
    String sAfterEditCalBodyId = (String)oaRet[0];
    if (!PuPubVO.isEqual(sCalBodyId, sAfterEditCalBodyId))
    {
      if (saKeyBodyWarehouse != null) {
        int iWareColLen = saKeyBodyWarehouse.length;
        for (int i = 0; i < iWareColLen; i++) {
          if ((saKeyBodyWarehouse[i] == null) || (saKeyBodyWarehouse[i].trim().length() <= 0))
            continue;
          billModel.setValueAt(null, iRow, saKeyBodyWarehouse[i]);
        }
      }
    }
  }

  public static int[] getDigitBatch(String corpID, String[] paraCodes)
  {
    int[] iRets = null;
    try {
      iRets = PubHelper.getDigitBatch(corpID, paraCodes);
    } catch (Exception e) {
      SCMEnv.out(e);
    }
    return iRets;
  }

  public static FormulaParse getFormulaParse()
  {
    if (m_formulaParse == null) {
      m_formulaParse = new FormulaParse();
    }
    return m_formulaParse;
  }

  public static int getPricePriorPolicy(String pk_corp)
  {
    if (m_hmapParaPricePolicy == null) {
      m_hmapParaPricePolicy = new HashMap();
    }

    Integer iPara = (Integer)m_hmapParaPricePolicy.get(pk_corp);
    if (iPara == null) {
      String sPara = null;
      try
      {
        sPara = SysInitBO_Client.getParaString(pk_corp, "PO28");
      } catch (Exception e) {
        outException(e);
        SCMEnv.out("获得订单价格优先参数时出错！默认取“含税价格优先”！");
        sPara = "含税价格优先";
      }

      if (sPara == null) {
        sPara = "含税价格优先";
      }
      iPara = sPara.equals("含税价格优先") ? new Integer(5) : new Integer(6);

      m_hmapParaPricePolicy.put(pk_corp, iPara);
    }

    return iPara.intValue();
  }

  public static String getPuDefaultPricePara(String pk_corp)
  {
    String sPara = null;
    try
    {
      sPara = SysInitBO_Client.getParaString(pk_corp, "PO06");
    } catch (Exception e) {
      outException(e);
      SCMEnv.out("获得采购默认价格参数时出错！默认取“参考成本”！");
      sPara = "参考成本";
    }

    return PuPubVO.getString_TrimZeroLenAsNull(sPara) == null ? "参考成本" : sPara;
  }

  public static Hashtable getVarry()
  {
    if (m_varry == null) {
      m_varry = new Hashtable();
    }
    return m_varry;
  }

  public static boolean isAutoSendToAudit(String sPk_corp)
  {
    return false;
  }

  public static boolean isAutoSwitchPUAssistUnit(String pk_corp)
  {
    if (PuPubVO.getString_TrimZeroLenAsNull(pk_corp) == null) {
      SCMEnv.out("nc.ui.po.pub.PoPublicUIClass.isAutoSwitchPUAssistUnit(String)传入参数不正确！");
      return false;
    }

    if (m_hashAutoSwitchPUAssistUnit == null) {
      m_hashAutoSwitchPUAssistUnit = new Hashtable();
    }

    UFBoolean bRet = (UFBoolean)m_hashAutoSwitchPUAssistUnit.get(pk_corp);
    if (bRet != null) {
      return bRet.booleanValue();
    }

    String sRet = null;
    try {
      sRet = SysInitBO_Client.getParaString(pk_corp, "PO08");
    } catch (Exception e) {
      outException(e);
      return false;
    }
    if (sRet == null) {
      SCMEnv.out("查询是否自动转换辅计量单位参数时出现系统错误！");
      return false;
    }
    if (sRet.equals("Y"))
      bRet = new UFBoolean(true);
    else {
      bRet = new UFBoolean(false);
    }
    m_hashAutoSwitchPUAssistUnit.put(pk_corp, bRet);

    return bRet.booleanValue();
  }

  public static boolean isNeedSendToAudit(String sBillType, AggregatedValueObject voBill)
  {
    if (voBill == null) {
      return false;
    }
    String sPk_corp = null;
    if ("20".equals(sBillType))
      sPk_corp = ((PraybillVO)voBill).getHeadVO().getPk_corp();
    else if ("21".equals(sBillType))
      sPk_corp = ((nc.vo.po.OrderVO)voBill).getHeadVO().getPk_corp();
    else if ("25".equals(sBillType))
      sPk_corp = ((InvoiceVO)voBill).getHeadVO().getPk_corp();
    else if ("61".equals(sBillType)) {
      sPk_corp = ((nc.vo.sc.order.OrderHeaderVO)((nc.vo.sc.order.OrderVO)voBill).getParentVO()).getPk_corp();
    }
    else if ("28".equals(sBillType)) {
      sPk_corp = ((PriceauditHeaderVO)((PriceauditMergeVO)voBill).getParentVO()).getPk_corp();
    }

    Integer iBillStatus = null;
    String sOper = null;
    String sBizType = null;
    String sId = null;
    if ("20".equals(sBillType)) {
      iBillStatus = ((PraybillVO)voBill).getHeadVO().getIbillstatus();
      sOper = ((PraybillVO)voBill).getHeadVO().getCoperator();
      sBizType = ((PraybillVO)voBill).getHeadVO().getCbiztype();
      sId = ((PraybillVO)voBill).getHeadVO().getCpraybillid();
    } else if ("21".equals(sBillType)) {
      iBillStatus = ((nc.vo.po.OrderVO)voBill).getHeadVO().getForderstatus();
      sOper = ((nc.vo.po.OrderVO)voBill).getHeadVO().getCoperator();
      sBizType = ((nc.vo.po.OrderVO)voBill).getHeadVO().getCbiztype();
      sId = ((nc.vo.po.OrderVO)voBill).getHeadVO().getCorderid();
    } else if ("25".equals(sBillType)) {
      iBillStatus = ((InvoiceVO)voBill).getHeadVO().getIbillstatus();
      sOper = ((InvoiceVO)voBill).getHeadVO().getCoperator();
      sBizType = ((InvoiceVO)voBill).getHeadVO().getCbiztype();
      sId = ((InvoiceVO)voBill).getHeadVO().getCinvoiceid();
    } else if ("61".equals(sBillType)) {
      iBillStatus = ((nc.vo.sc.order.OrderHeaderVO)((nc.vo.sc.order.OrderVO)voBill).getParentVO()).getIbillstatus();

      sOper = ((nc.vo.sc.order.OrderHeaderVO)((nc.vo.sc.order.OrderVO)voBill).getParentVO()).getCoperator();

      sBizType = ((nc.vo.sc.order.OrderHeaderVO)((nc.vo.sc.order.OrderVO)voBill).getParentVO()).getCbiztype();

      sId = ((nc.vo.sc.order.OrderHeaderVO)((nc.vo.sc.order.OrderVO)voBill).getParentVO()).getCorderid();
    }
    else if ("28".equals(sBillType)) {
      iBillStatus = ((PriceauditHeaderVO)((PriceauditMergeVO)voBill).getParentVO()).getIbillstatus();

      sOper = ((PriceauditHeaderVO)((PriceauditMergeVO)voBill).getParentVO()).getCoperatorid();

      sId = ((PriceauditHeaderVO)((PriceauditMergeVO)voBill).getParentVO()).getPk_corp();
    }

    if ((iBillStatus != null) && (iBillStatus.compareTo(BillStatus.FREE) == 0) && (PoPublicUIClass.getLoginUser().equals(sOper))) {
      try
      {
        return BusiBillManageTool.isNeedSendToAudit(sBillType, sPk_corp, sBizType, sId, sOper);
      } catch (Exception e) {
        printException(e);
        return false;
      }
    }
    return false;
  }

  public static boolean isProductEnabled(String pk_corp, String sProdCode)
  {
    if ((PuPubVO.getString_TrimZeroLenAsNull(pk_corp) == null) || (PuPubVO.getString_TrimZeroLenAsNull(sProdCode) == null))
    {
      SCMEnv.out("nc.ui.pu.pub.PuTool.isProductEnabled(String, String)传入参数不正确！");
      return false;
    }

    if (m_hashProductEnabled == null) {
      m_hashProductEnabled = new Hashtable();
    }

    String sKey = pk_corp + sProdCode;
    if (m_hashProductEnabled.containsKey(sKey)) {
      return ((UFBoolean)m_hashProductEnabled.get(sKey)).booleanValue();
    }

    boolean bEnabled = false;
    try {
      ICreateCorpQueryService myService = (ICreateCorpQueryService)NCLocator.getInstance().lookup(ICreateCorpQueryService.class.getName());
      bEnabled = myService.isEnabled(pk_corp, sProdCode);
    }
    catch (Exception e)
    {
      outException(new Exception(e));
      return false;
    }

    m_hashProductEnabled.put(sKey, new UFBoolean(bEnabled));
    return bEnabled;
  }

  public static void loadBatchTaxrate(String[] saBaseId)
  {
    InvTool.loadBatchTaxrate(saBaseId);
  }

  public static void loadBatchTaxrate(AggregatedValueObject[] voaQuery, String sBaseIdName)
  {
    if ((voaQuery == null) || (voaQuery.length < 1)) {
      return;
    }

    Vector vecQueryBaseId = new Vector();
    int iVOLength = voaQuery.length;
    for (int i = 0; i < iVOLength; i++) {
      int iBodyLen = voaQuery[i].getChildrenVO().length;
      for (int j = 0; j < iBodyLen; j++) {
        vecQueryBaseId.addElement((String)voaQuery[i].getChildrenVO()[j].getAttributeValue(sBaseIdName));
      }

    }

    int iQueryLen = vecQueryBaseId.size();
    if (iQueryLen == 0) {
      return;
    }
    String[] saQueryBaseId = new String[iQueryLen];
    vecQueryBaseId.toArray(saQueryBaseId);

    loadBatchTaxrate(saQueryBaseId);
  }

  public static void loadInvMaxPrice(BillCardPanel pnlBill)
  {
    if (pnlBill == null) {
      return;
    }

    Vector vInvPk = new Vector();
    Vector vPutRow = new Vector();
    int iLen = pnlBill.getRowCount();
    for (int i = 0; i < iLen; i++) {
      String sInvPk = (String)pnlBill.getBodyValueAt(i, "cmangid");
      if ((sInvPk == null) || (sInvPk.trim().length() == 0)) {
        pnlBill.setBodyValueAt(null, i, "nmaxprice");
      } else {
        vInvPk.add(sInvPk);
        vPutRow.add(new Integer(i));
      }
    }

    int iQueryedLen = vInvPk.size();
    if (iQueryedLen != 0) {
      String[] saQueryedPk = new String[iQueryedLen];
      vInvPk.copyInto(saQueryedPk);
      UFDouble[] daMaxPrice = PoPublicUIClass.getInvMaxPriceArray(saQueryedPk);

      for (int i = 0; i < iQueryedLen; i++)
        pnlBill.setBodyValueAt(daMaxPrice[i], ((Integer)vPutRow.get(i)).intValue(), "nmaxprice");
    }
  }

  public static void loadInvMaxPrice(BillListPanel pnlList)
  {
    if (pnlList == null) {
      return;
    }

    BillModel bm = pnlList.getBodyBillModel();
    int iLen = bm.getRowCount();

    Vector vInvPk = new Vector();
    Vector vPutRow = new Vector();
    for (int i = 0; i < iLen; i++) {
      String sInvPk = (String)bm.getValueAt(i, "cmangid");
      if ((sInvPk == null) || (sInvPk.trim().length() == 0)) {
        bm.setValueAt(null, i, "nmaxprice");
      } else {
        vInvPk.add(sInvPk);
        vPutRow.add(new Integer(i));
      }
    }

    int iQueryedLen = vInvPk.size();
    if (iQueryedLen != 0) {
      String[] saQueryedPk = new String[iQueryedLen];
      vInvPk.copyInto(saQueryedPk);
      UFDouble[] daMaxPrice = PoPublicUIClass.getInvMaxPriceArray(saQueryedPk);

      for (int i = 0; i < iQueryedLen; i++)
        bm.setValueAt(daMaxPrice[i], ((Integer)vPutRow.get(i)).intValue(), "nmaxprice");
    }
  }

  public static void main(String[] args)
  {
  }

  public static void onColSet(Container parent, ReportBaseClass rb)
  {
    ShowItemsDlg dlg = null;
    dlg = new ShowItemsDlg(parent, rb);
    dlg.showModal();
  }

  public static void printException(Exception e)
  {
    ExcpTool.printException(e);
  }

  public static void outException(Container cont, Exception e)
  {
    ExcpTool.reportException(cont, e);
  }

  public static void outException(Exception e)
  {
    SCMEnv.out(e);
  }

  public static void setBillModelConvertRate(BillModel billModel, String[] saItem)
  {
    if ((billModel == null) || (saItem == null) || (saItem.length == 0) || (billModel.getRowCount() == 0))
    {
      return;
    }

    loadBatchInvConvRateInfo((String[])(String[])PuGetUIValueTool.getArray(billModel, saItem[0], String.class, 0, billModel.getRowCount() - 1), (String[])(String[])PuGetUIValueTool.getArray(billModel, saItem[1], String.class, 0, billModel.getRowCount() - 1));

    String sBaseIdName = saItem[0];
    String sAssistUnitIdName = saItem[1];
    String sMainNumName = saItem[2];
    String sAssistNumName = saItem[3];
    String sConvertRateName = saItem[4];

    for (int i = 0; i < billModel.getRowCount(); i++)
    {
      String sAssistUnitId = (String)billModel.getValueAt(i, sAssistUnitIdName);

      UFDouble dConvRate = null;

      if (PuPubVO.getString_TrimZeroLenAsNull(sAssistUnitId) != null) {
        String sBaseId = (String)billModel.getValueAt(i, sBaseIdName);
        boolean bFixed = isFixedConvertRate(sBaseId, sAssistUnitId);

        if (bFixed)
        {
          dConvRate = getInvConvRateValue(sBaseId, sAssistUnitId);
        }
        else {
          UFDouble dMainNum = (UFDouble)billModel.getValueAt(i, sMainNumName);

          if ((dMainNum != null) && (dMainNum.compareTo(VariableConst.ZERO) != 0))
          {
            UFDouble dAssistNum = (UFDouble)billModel.getValueAt(i, sAssistNumName);

            if ((dAssistNum != null) && (dAssistNum.compareTo(VariableConst.ZERO) != 0))
            {
              dConvRate = dMainNum.div(dAssistNum);
            }
          } else {
            dConvRate = PuPubVO.getUFDouble_ValueAsValue(billModel.getValueAt(i, sConvertRateName));
          }
        }
      }

      billModel.setValueAt(dConvRate, i, sConvertRateName);
    }
  }

  public static void setBodyProjectByHeadPro(BillCardPanel bcp, String sHeadKey, String sBodyKeyId, String sBodyKeyName, int iRow)
  {
    try
    {
      if (!(bcp.getHeadItem(sHeadKey).getComponent() instanceof UIRefPane)) {
        SCMEnv.out("处理项目自动协带时出错:表头'项目'参数(单据模板中的itemkey)有错或相应单据模板数据设置有误");
        return;
      }
      UIRefPane projectRefPane = (UIRefPane)bcp.getHeadItem(sHeadKey).getComponent();

      String strProjectId = projectRefPane.getRefPK();

      if ((strProjectId == null) || (strProjectId.trim().equals(""))) {
        return;
      }
      if (iRow < 0) {
        SCMEnv.out("处理项目自动协带时出错:未正确得到设置表体项目的表体行");
        return;
      }
      bcp.getBillModel().setValueAt(strProjectId, iRow, sBodyKeyId);
      BillItem bi = bcp.getBodyItem(sBodyKeyName);
      String[] aryformulas = bi.getEditFormulas();
      if ((aryformulas != null) && (aryformulas.length > 0))
        bcp.getBillModel().execFormulas(iRow, aryformulas);
    }
    catch (Exception e) {
      SCMEnv.out("处理项目自动协带时出错,详细错误如下:");
      SCMEnv.out(e);
    }
  }

  public static void setBodyProjectByHeadProject(BillCardPanel bcp, String sHeadKey, String sBodyKeyId, String sBodyKeyName, int iOprType)
  {
    try
    {
      if (!(bcp.getHeadItem(sHeadKey).getComponent() instanceof UIRefPane)) {
        SCMEnv.out("处理项目自动协带时出错:表头'项目'参数(单据模板中的itemkey)有错或相应单据模板数据设置有误");
        return;
      }
      UIRefPane projectRefPane = (UIRefPane)bcp.getHeadItem(sHeadKey).getComponent();

      String strProjectId = projectRefPane.getRefPK();

      if ((strProjectId == null) || (strProjectId.trim().equals("")))
        return;
      int iNewLineRow = 0;
      switch (iOprType) {
      case 100:
        iNewLineRow = bcp.getBillTable().getRowCount() - 1;
        break;
      case 120:
        iNewLineRow = bcp.getBillTable().getSelectedRow();
        break;
      default:
        iNewLineRow = bcp.getBillTable().getRowCount() - 1;
      }

      if (iNewLineRow < 0) {
        SCMEnv.out("处理项目自动协带时出错:未正确得到设置表体项目的表体行");
        return;
      }
      bcp.getBillModel().setValueAt(strProjectId, iNewLineRow, sBodyKeyId);

      BillItem bi = bcp.getBodyItem(sBodyKeyName);
      String[] aryformulas = bi.getEditFormulas();
      if ((aryformulas != null) && (aryformulas.length > 0))
        bcp.getBillModel().execFormulas(iNewLineRow, aryformulas);
    }
    catch (Exception e) {
      SCMEnv.out("处理项目自动协带时出错,详细错误如下:");
      SCMEnv.out(e);
    }
  }

  public static void setBodyProjectByHeadProject(BillCardPanel bcp, String sHeadKey, String sBodyKeyId, String sBodyKeyName, int iBeginRow, int iEndRow)
  {
    try
    {
      if ((bcp.getHeadItem(sHeadKey) == null) || (!(bcp.getHeadItem(sHeadKey).getComponent() instanceof UIRefPane)))
      {
        SCMEnv.out("处理项目自动协带时出错:表头'项目'参数(单据模板中的itemkey)有错或相应单据模板数据设置有误");
        return;
      }
      UIRefPane projectRefPane = (UIRefPane)bcp.getHeadItem(sHeadKey).getComponent();

      String strProjectId = projectRefPane.getRefPK();

      if (PuPubVO.getString_TrimZeroLenAsNull(strProjectId) == null) {
        return;
      }

      int i = iBeginRow;
      while (i <= iEndRow) {
        if (PuPubVO.getString_TrimZeroLenAsNull(bcp.getBodyValueAt(i, sBodyKeyId)) == null)
        {
          bcp.setBodyValueAt(strProjectId, i, sBodyKeyId);
        }
        i++;
      }

      BillItem bi = bcp.getBodyItem(sBodyKeyName);
      String[] aryformulas = bi.getEditFormulas();
      if ((aryformulas != null) && (aryformulas.length > 0))
        bcp.getBillModel().execFormulas(aryformulas, iBeginRow, iEndRow);
    }
    catch (Exception e)
    {
      SCMEnv.out("处理项目自动协带时出错,详细错误如下:");
      SCMEnv.out(e);
    }
  }

  public static void setColorCard(BillCardPanel bcp, ArrayList aryHeadKeys, ArrayList aryBodyKeys, ArrayList aryTailKeys)
  {
    BillPanelTool.setColorCard(bcp, aryHeadKeys, aryBodyKeys, aryTailKeys);
  }

  public static void setColorList(BillCardPanel bcp, BillListPanel blp, ArrayList aryHeadKeys, ArrayList aryBodyKeys)
  {
    BillPanelTool.setColorList(bcp, blp, aryHeadKeys, aryBodyKeys);
  }

  public static void setFocusOnAdd(BillCardPanel bcp, ButtonObject bo)
  {
    try
    {
      if ((bcp == null) || (bo == null)) {
        SCMEnv.out("传入参数不正确，直接返回");
        return;
      }
      String makeBySelf = NCLangRes.getInstance().getStrByID("UPP4004pub", "UPP4004pub-000174");
      if (makeBySelf.equals(bo.getName())) {
        BillItem[] items = bcp.getHeadShowItems();
        if ((items == null) || (items.length <= 0))
          return;
        int iLen = items.length;
        for (int i = 0; i < iLen; i++)
          if (items[i].isEdit()) {
            items[i].setEdit(true);
            break;
          }
      }
    }
    catch (Exception e) {
      SCMEnv.out("处理项目自动协带时出错,详细错误如下:");
      SCMEnv.out(e);
    }
  }

  public static void setFocusOnLastCom(BillCardPanel bcp, BillEditEvent e)
  {
    try
    {
      if ((bcp == null) || (e == null)) {
        SCMEnv.out("传入参数不正确，直接返回");
        return;
      }

      BillItem[] items = null;

      if (e.getPos() == 0)
      {
        items = bcp.getBodyShowItems();
        int iLen = items.length;
        for (int i = 0; i < iLen; i++) {
          if (items[i].isEdit()) {
            items[i].setEdit(true);
            break;
          }
        }

      } else if (e.getPos() != 1);
    }
    catch (Exception ex) {
      SCMEnv.out("处理项目自动协带时出错,详细错误如下:");
      SCMEnv.out(ex);
    }
  }

  public static void setHeadTailItemLen(ReportBaseClass rpt)
  {
    if (rpt == null) {
      return;
    }
    BillItem[] items = rpt.getHead_Items();
    if ((items == null) || (items.length <= 0))
      return;
    for (int i = 0; i < items.length; i++) {
      if (items[i] == null)
        continue;
      if ((items[i].getKey() == null) || (items[i].getKey().trim().equals(""))) {
        continue;
      }
      rpt.setMaxLenOfHeadItem(items[i].getKey(), 500);
    }

    items = rpt.getTail_Items();
    if ((items == null) || (items.length <= 0))
      return;
    for (int i = 0; i < items.length; i++) {
      if (items[i] == null)
        continue;
      if ((items[i].getKey() == null) || (items[i].getKey().trim().equals(""))) {
        continue;
      }
      rpt.setMaxLenOfTailItem(items[i].getKey(), 500);
    }
  }

  public static void setInvRefByBusiType(String pk_corp, String sBusiTypeCode, UIRefPane refPane)
  {
    if ((pk_corp == null) || (sBusiTypeCode == null) || (refPane == null)) {
      return;
    }

    DefaultRefModel f = new DefaultRefModel();
    f.setTableName("bd_busitype");
    f.setFieldCode(new String[] { "verifyrule" });
    f.setWherePart("pk_corp ='" + pk_corp + "' and busicode = '" + sBusiTypeCode + "'");

    f.getData();
    Vector v1 = f.getVecData();

    String sVerifyRule = null;
    if (v1.size() > 0) {
      Vector v2 = (Vector)v1.elementAt(0);
      if (v2.size() > 0)
        sVerifyRule = (String)v2.elementAt(0);
    }
    if (sVerifyRule == null) {
      return;
    }
    AbstractRefModel model = refPane.getRefModel();
    String oldWhere = model.getWherePart();
    String where = " and bd_invmandoc.sellproxyflag =";

    int index = oldWhere.indexOf(where);

    if (sVerifyRule.trim().equals("S")) {
      where = where + " 'Y' and bd_invmandoc.consumesettleflag = 'N'";
    }
    else if (sVerifyRule.trim().equals("V")) {
      where = where + " 'N' and bd_invmandoc.consumesettleflag = 'Y'";
    }
    else
    {
      where = where + " 'N' and bd_invmandoc.consumesettleflag = 'N'";
    }

    if (index >= 0) {
      oldWhere = oldWhere.substring(0, index - 1);
    }
    model.setWherePart(oldWhere + where);
  }

  public static void setInvRefByBusiType(String pk_BusiType, UIRefPane refPane)
  {
    if ((pk_BusiType == null) || (refPane == null)) {
      return;
    }

    DefaultRefModel f = new DefaultRefModel();
    f.setTableName("bd_busitype");
    f.setFieldCode(new String[] { "verifyrule" });
    f.setWherePart("pk_busitype = '" + pk_BusiType + "'");
    f.getData();
    Vector v1 = f.getVecData();

    String sVerifyRule = null;
    if (v1.size() > 0) {
      Vector v2 = (Vector)v1.elementAt(0);
      if (v2.size() > 0)
        sVerifyRule = (String)v2.elementAt(0);
    }
    if (sVerifyRule == null) {
      return;
    }
    AbstractRefModel model = refPane.getRefModel();
    String oldWhere = model.getWherePart();
    String where = " and bd_invmandoc.sellproxyflag =";

    int index = oldWhere.indexOf(where);

    if (sVerifyRule.trim().equals("S")) {
      where = where + " 'Y' and bd_invmandoc.consumesettleflag = 'N'";
    }
    else if (sVerifyRule.trim().equals("V")) {
      where = where + " 'N' and bd_invmandoc.consumesettleflag = 'Y'";
    }
    else
    {
      where = where + " 'N' and bd_invmandoc.consumesettleflag = 'N'";
    }

    if (index >= 0) {
      oldWhere = oldWhere.substring(0, index);
    }
    model.setWherePart(oldWhere + where);
  }

  public static void setTranslateRender(UITable table, BillItem[] items, String[] keys)
  {
    int index = 0;
    ArrayList alIndex = new ArrayList();
    for (int i = 0; i < items.length; i++) {
      for (int j = 0; j < keys.length; j++) {
        if ((!items[i].getKey().equalsIgnoreCase(keys[j])) || (!items[i].isShow()))
          continue;
        alIndex.add(new Integer(index));
      }

      if (items[i].isShow()) {
        index++;
      }
    }
    TableColumnModel model = table.getColumnModel();
    TableColumn column = null;
    for (int i = 0; i < alIndex.size(); i++) {
      column = new TableColumn();
      column = model.getColumn(new Integer(alIndex.get(i).toString()).intValue());

      TableCellRendererLabel label = new TableCellRendererLabel();
      label.setTranslate(true);
      column.setCellRenderer(label);
    }
  }

  public static void setTranslateRender(BillCardPanel cardPanel)
  {
    UITable table = cardPanel.getBillTable();
    BillItem[] items = cardPanel.getBodyItems();

    int index = 0;
    TableColumnModel model = table.getColumnModel();
    TableColumn column = new TableColumn();
    for (int i = 0; i < items.length; i++) {
      if ((items[i].getDataType() == 6) && (items[i].isShow())) {
        column = model.getColumn(index);
        TableCellRendererLabel label = new TableCellRendererLabel();
        label.setTranslate(true);
        column.setCellRenderer(label);
      }

      if (items[i].isShow())
        index++;
    }
  }

  public static void setTranslateRender(BillListPanel listPanel)
  {
    UITable headTable = listPanel.getHeadTable();
    UITable bodyTable = listPanel.getBodyTable();
    BillItem[] headItems = listPanel.getHeadBillModel().getBodyItems();

    BillItem[] bodyItems = listPanel.getBodyBillModel().getBodyItems();

    int index = 0;
    TableColumnModel model = headTable.getColumnModel();
    TableColumn column = new TableColumn();
    for (int i = 0; i < headItems.length; i++) {
      if ((headItems[i].getDataType() == 6) && (headItems[i].isShow())) {
        column = model.getColumn(index);
        TableCellRendererLabel label = new TableCellRendererLabel();
        label.setTranslate(true);
        column.setCellRenderer(label);
      }

      if (headItems[i].isShow()) {
        index++;
      }
    }
    index = 0;
    model = bodyTable.getColumnModel();
    column = new TableColumn();
    int iLen = model.getColumnCount();
    for (int i = 0; (i < bodyItems.length) && 
      (i < iLen); i++)
    {
      if ((bodyItems[i].getDataType() == 6) && (bodyItems[i].isShow())) {
        column = model.getColumn(index);
        TableCellRendererLabel label = new TableCellRendererLabel();
        label.setTranslate(true);
        column.setCellRenderer(label);
      }
      if (bodyItems[i].isShow())
        index++;
    }
  }

  public static void setVOConvertRate(AggregatedValueObject voConvert, String[] saItem)
  {
    if ((voConvert == null) || (saItem == null) || (saItem.length == 0)) {
      return;
    }

    String sBaseIdName = saItem[0];
    String sAssistUnitIdName = saItem[1];
    String sMainNumName = saItem[2];
    String sAssistNumName = saItem[3];
    String sConvertRateName = saItem[4];

    CircularlyAccessibleValueObject[] voaBody = voConvert.getChildrenVO();
    int iLen = voaBody.length;

    for (int i = 0; i < iLen; i++)
    {
      String sAssistUnitId = (String)voaBody[i].getAttributeValue(sAssistUnitIdName);

      UFDouble dConvRate = null;

      if ((sAssistUnitId != null) && (!sAssistUnitId.trim().equals(""))) {
        String sBaseId = (String)voaBody[i].getAttributeValue(sBaseIdName);

        boolean bFixed = isFixedConvertRate(sBaseId, sAssistUnitId);

        if (bFixed)
        {
          dConvRate = getInvConvRateValue(sBaseId, sAssistUnitId);
        }
        else {
          UFDouble dMainNum = (UFDouble)voaBody[i].getAttributeValue(sMainNumName);

          if ((dMainNum != null) && (dMainNum.compareTo(VariableConst.ZERO) != 0))
          {
            UFDouble dAssistNum = (UFDouble)voaBody[i].getAttributeValue(sAssistNumName);

            if ((dAssistNum != null) && (dAssistNum.compareTo(VariableConst.ZERO) != 0))
            {
              dConvRate = dMainNum.div(dAssistNum);
            }
          }
        }
      }
      voaBody[i].setAttributeValue(sConvertRateName, dConvRate);
    }
  }

  public static void loadAncestorBillData(BillCardPanel panel, String billType)
  {
    SourceBillTool.loadAncestorBillData(panel, billType);
  }

  public static void loadAncestorBillData(BillListPanel panel, String billType)
  {
    SourceBillTool.loadAncestorBillData(panel, billType);
  }

  public static void loadAncestorBillData(BillModel model, String billType)
  {
    SourceBillTool.loadAncestorBillData(model, billType);
  }

  public static void loadSourceBillData(BillCardPanel panel, String billType)
  {
    SourceBillTool.loadSourceBillData(panel, billType);
  }

  public static void loadSourceBillData(BillListPanel panel, String billType)
  {
    SourceBillTool.loadSourceBillData(panel, billType);
  }

  public static void loadSourceBillData(BillModel model, String billType)
  {
    SourceBillTool.loadSourceBillData(model, billType);
  }

  public static void loadBatchAssistManaged(String[] saBaseId)
  {
    InvTool.loadBatchAssistManaged(saBaseId);
  }

  public static void loadBatchFreeVO(String[] saMangId)
  {
    InvTool.loadBatchFreeVO(saMangId);
  }

  public static void loadBatchMainUnit(String[] saBaseId)
  {
    InvTool.loadBatchMainUnit(saBaseId);
  }

  public static boolean isAssUnitManaged(String sInvBaseId)
  {
    return InvTool.isAssUnitManaged(sInvBaseId);
  }

  public static boolean isBatchManaged(String sInvMangId)
  {
    return InvTool.isBatchManaged(sInvMangId);
  }

  public static boolean isDiscount(String sInvBaseId)
  {
    return InvTool.isDiscount(sInvBaseId);
  }

  public static boolean isFixedConvertRate(String sBaseId, String sAssistUnit)
  {
    return InvTool.isFixedConvertRate(sBaseId, sAssistUnit);
  }

  public static boolean isFreeMngt(String sMangId)
  {
    return InvTool.isFreeMngt(sMangId);
  }

  public static boolean isLabor(String sInvBaseId)
  {
    return InvTool.isLabor(sInvBaseId);
  }

  public static void loadBatchInvConvRateInfo(String[] saBaseId, String[] saAssistUnit)
  {
    InvTool.loadBatchInvConvRateInfo(saBaseId, saAssistUnit);
  }

  public static void loadBatchProdNumMngt(String[] saMangId)
  {
    InvTool.loadBatchProdNumMngt(saMangId);
  }

  public static String getMainUnit(String sInvBaseId)
  {
    return InvTool.getMainUnit(sInvBaseId);
  }

  public static void addBatchProdNumMngt(String sMangId, boolean bMngt)
  {
    InvTool.addBatchProdNumMngt(sMangId, bMngt);
  }

  public static Object[] getInvConvRateInfo(String sBaseId, String sAssistUnit)
  {
    return InvTool.getInvConvRateInfo(sBaseId, sAssistUnit);
  }

  public static UFDouble getInvConvRateValue(String sBaseId, String sAssistUnit)
  {
    return InvTool.getInvConvRateValue(sBaseId, sAssistUnit);
  }

  public static FreeVO getInvFreeVO(String sMangId)
  {
    return InvTool.getInvFreeVO(sMangId);
  }

  public static UFDouble getInvTaxRate(String sBaseId)
  {
    return InvTool.getInvTaxRate(sBaseId);
  }

  public static int[] getRowsAfterMultiSelect(int iCurRow, int iSelectedLen)
  {
    return BillPanelTool.getRowsAfterMultiSelect(iCurRow, iSelectedLen);
  }

  public static int[] getRowsWhenAttrNull(BillModel billModel, String sItemKey)
  {
    return BillPanelTool.getRowsWhenAttrNull(billModel, sItemKey);
  }

  public static Hashtable getHashBodyByHeadKey(CircularlyAccessibleValueObject[] bodys, String strHeadKey)
  {
    return VOTool.getHashBodyByHeadKey(bodys, strHeadKey);
  }

  /** @deprecated */
  public static int getIndexBeforeSort(BillCardPanel cpPanel, int iShowIndex)
  {
    return iShowIndex;
  }

  /** @deprecated */
  public static int getIndexBeforeSort(BillListPanel lpPanel, int iShowIndex)
  {
    return iShowIndex;
  }

  public static void sortByItem(BillCardPanel billPanel, String key, boolean bAscend, String headClassName, String BodyClassName)
  {
    BillItem item = billPanel.getBodyItem(key);
    CircularlyAccessibleValueObject[] bodys = null;
    bodys = billPanel.getBillModel().getBodyValueVOs(BodyClassName);
    CircularlyAccessibleValueObject head = billPanel.getBillData().getHeaderValueVO(headClassName);

    if ((item == null) || (bodys == null)) {
      return;
    }

    int bottom = bodys.length - 1;
    while (bottom > 0) {
      int temp = 0;
      for (int j = 0; j < bottom; j++)
      {
        if ((item.getDataType() == 1) || (item.getDataType() == 2))
        {
          double value1 = -1.0E+099D;
          double value2 = -1.0E+099D;
          Object ob1 = bodys[j].getAttributeValue(item.getKey());
          Object ob2 = bodys[(j + 1)].getAttributeValue(item.getKey());

          if (ob1 != null)
            value1 = ((Number)ob1).doubleValue();
          if (ob2 != null) {
            value2 = ((Number)ob2).doubleValue();
          }
          if (((value1 > value2) && (bAscend)) || ((value1 < value2) && (!bAscend)))
          {
            CircularlyAccessibleValueObject tempVO = bodys[j];
            bodys[j] = bodys[(j + 1)];
            bodys[(j + 1)] = tempVO;
            temp = j;
          }
        } else {
          String value1 = "";
          String value2 = "";
          Object ob1 = bodys[j].getAttributeValue(item.getKey());
          Object ob2 = bodys[(j + 1)].getAttributeValue(item.getKey());

          if (ob1 != null)
            value1 = ob1.toString();
          if (ob2 != null) {
            value2 = ob2.toString();
          }
          if (((value1.compareTo(value2) <= 0) || (!bAscend)) && ((value1.compareTo(value2) >= 0) || (bAscend)))
            continue;
          CircularlyAccessibleValueObject tempVO = bodys[j];
          bodys[j] = bodys[(j + 1)];
          bodys[(j + 1)] = tempVO;
          temp = j;
        }
      }

      bottom = temp;
    }

    billPanel.getBillData().clearViewData();
    billPanel.getBillData().setHeaderValueVO(head);
    billPanel.getBillData().setBodyValueVO(bodys);
    billPanel.updateValue();
  }

  public static boolean isLastCom(BillCardPanel bcp, BillEditEvent e)
  {
    return BillPanelTool.isLastCom(bcp, e);
  }

  public static final void validateNotNullField(BillCardPanel bc)
    throws BusinessException
  {
    BillPanelTool.validateNotNullField(bc);
  }

  public static void validateNotNullField(BillCardPanel bc, ArrayList listNotCheckedKeys)
    throws BusinessException
  {
    if (bc == null) {
      return;
    }
    BillItem[] headItems = bc.getHeadShowItems();
    BillItem[] bodyItems = bc.getBodyShowItems();

    String[] headNotNullKeys = null;
    Vector v1 = new Vector();
    for (int i = 0; (headItems != null) && (i < headItems.length); i++) {
      if ((!headItems[i].isNull()) || (
        (listNotCheckedKeys != null) && (listNotCheckedKeys.contains(headItems[i].getKey())))) {
        continue;
      }
      v1.add(headItems[i].getKey());
    }

    if (v1.size() > 0) {
      headNotNullKeys = new String[v1.size()];
      v1.copyInto(headNotNullKeys);
    }

    String[] bodyNotNullKeys = null;
    Vector v2 = new Vector();
    for (int i = 0; (bodyItems != null) && (i < bodyItems.length); i++) {
      if ((!bodyItems[i].isNull()) || (
        (listNotCheckedKeys != null) && (listNotCheckedKeys.contains(bodyItems[i].getKey())))) {
        continue;
      }
      v2.add(bodyItems[i].getKey());
    }

    if (v2.size() > 0) {
      bodyNotNullKeys = new String[v2.size()];
      v2.copyInto(bodyNotNullKeys);
    }

    String hErrorMessage = "";
    for (int i = 0; (headNotNullKeys != null) && (i < headNotNullKeys.length); i++) {
      Object ob = bc.getHeadItem(headNotNullKeys[i]).getComponent();
      String value = null;
      if ((ob instanceof UIRefPane))
      {
        value = ((UIRefPane)ob).getUITextField().getText();
      }
      else
      {
        value = bc.getHeadItem(headNotNullKeys[i]).getValue();
      }

      String name = bc.getHeadItem(headNotNullKeys[i]).getName();
      if ((value == null) || (value.trim().equals(""))) {
        hErrorMessage = hErrorMessage + name + ",";
      }
    }
    if (!hErrorMessage.equals("")) {
      hErrorMessage = "\n" + hErrorMessage.substring(0, hErrorMessage.length() - 1);
    }

    String bErrorMessage = "";
    int rows = bc.getRowCount();
    for (int i = 0; i < rows; i++)
    {
      if ((bc.getBillModel().getValueAt(i, "cmangid") == null) || (bc.getBillModel().getValueAt(i, "cmangid").toString().trim().equals("")))
      {
        continue;
      }
      String[] valueLan = { String.valueOf(i + 1) };
      String bRowErrorMessage = NCLangRes.getInstance().getStrByID("4004pub", "UPP4004pub-000163", null, valueLan);
      String old = bRowErrorMessage.toString();

      for (int j = 0; (bodyNotNullKeys != null) && (j < bodyNotNullKeys.length); j++) {
        Object value = bc.getBillModel().getValueAt(i, bodyNotNullKeys[j]);
        String name = bc.getBodyItem(bodyNotNullKeys[j]).getName();
        if ((value == null) || (value.toString().trim().equals(""))) {
          bRowErrorMessage = bRowErrorMessage + name + ",";
        }

      }

      if (bRowErrorMessage.length() > old.length()) {
        bErrorMessage = bErrorMessage + "\n" + bRowErrorMessage.substring(0, bRowErrorMessage.length() - 1);
      }

    }

    if ((!hErrorMessage.equals("")) || (!bErrorMessage.equals(""))) {
      String errorMessage = NCLangRes.getInstance().getStrByID("4004pub", "UPP4004pub-000164");
      if (!hErrorMessage.equals("")) {
        String[] value = { hErrorMessage };
        errorMessage = errorMessage + NCLangRes.getInstance().getStrByID("4004pub", "UPP4004pub-000165", null, value);
      }
      if (!bErrorMessage.equals("")) {
        String[] value = { bErrorMessage };
        errorMessage = errorMessage + NCLangRes.getInstance().getStrByID("4004pub", "UPP4004pub-000166", null, value);
      }
      throw new BusinessException(errorMessage);
    }
  }

  public static void restrictWarehouseRefByStoreOrg(BillCardPanel pnlBill, String pk_corp, String sCalBodyId, String sKeyBodyWareRef)
  {
    BillPanelTool.restrictWarehouseRefByStoreOrg(pnlBill, pk_corp, sCalBodyId, sKeyBodyWareRef);
  }

  public static void afterEditInvToMaxPrice(BillCardPanel pnlBill, BillEditEvent e)
  {
    if ((pnlBill == null) || (e == null)) {
      SCMEnv.out("方法nc.ui.pu.pub.PuTool.afterEditInvToMaxPrice(BillCardPanel)传入参数错误！");
      return;
    }

    int iRow = e.getRow();
    String sInvPk = (String)pnlBill.getBodyValueAt(iRow, "cmangid");
    if ((sInvPk == null) || (sInvPk.trim().length() == 0)) {
      pnlBill.setBodyValueAt(null, iRow, "nmaxprice");
    } else {
      UFDouble dValue = PoPublicUIClass.getInvMaxPrice(sInvPk);
      dValue = (dValue == null) || (dValue.compareTo(VariableConst.ZERO) == 0) ? null : dValue;

      pnlBill.setBodyValueAt(dValue, iRow, "nmaxprice");
    }
  }

  public static void afterEditStoreOrgToWarehouse(BillCardPanel pnlBill, BillEditEvent e, String pk_corp, String sCalBodyId, String sKeyBodyWareRef, String[] saKeyBodyWarehouse)
  {
    BillPanelTool.afterEditStoreOrgToWarehouse(pnlBill, e, pk_corp, sCalBodyId, sKeyBodyWareRef, saKeyBodyWarehouse);
  }

  public static boolean beforeEditBillBodyMemo(BillCardPanel pnlCard, BillEditEvent e)
  {
    pnlCard.stopEditing();

    Object ob = pnlCard.getBodyValueAt(e.getRow(), "vmemo");
    if (ob == null) {
      ((UIRefPane)pnlCard.getBodyItem("vmemo").getComponent()).setText("");
    }
    else {
      ((UIRefPane)pnlCard.getBodyItem("vmemo").getComponent()).setText((String)ob);
    }

    return true;
  }

  public static boolean beforeEditInvBillBodyFree(BillCardPanel pnlBill, BillEditEvent e, String[] saNameInv, String[] saNameFree)
  {
    pnlBill.stopEditing();

    int iRow = e.getRow();
    if (iRow < 0) {
      return false;
    }

    String sMangId = (String)pnlBill.getBodyValueAt(iRow, saNameInv[0]);
    if ((sMangId == null) || (sMangId.length() < 1)) {
      ((UIRefPane)pnlBill.getBodyItem(saNameFree[0]).getComponent()).setButtonVisible(false);

      return false;
    }

    if (isFreeMngt(sMangId))
    {
      InvVO invVO = new InvVO();

      invVO.setIsFreeItemMgt(new Integer(1));

      invVO.setCinventoryid(sMangId);

      Object strTemp = pnlBill.getBodyValueAt(iRow, saNameInv[1]);
      invVO.setCinventorycode(strTemp == null ? null : strTemp.toString());

      strTemp = pnlBill.getBodyValueAt(iRow, saNameInv[2]);
      invVO.setInvname(strTemp == null ? null : strTemp.toString());

      strTemp = pnlBill.getBodyValueAt(iRow, saNameInv[3]);
      invVO.setInvspec(strTemp == null ? null : strTemp.toString());

      strTemp = pnlBill.getBodyValueAt(iRow, saNameInv[4]);
      invVO.setInvtype(strTemp == null ? null : strTemp.toString());

      FreeVO voFree = getInvFreeVO(sMangId);
      if (voFree != null) {
        voFree.setVfree1((String)pnlBill.getBodyValueAt(iRow, saNameFree[1]));

        voFree.setVfree2((String)pnlBill.getBodyValueAt(iRow, saNameFree[2]));

        voFree.setVfree3((String)pnlBill.getBodyValueAt(iRow, saNameFree[3]));

        voFree.setVfree4((String)pnlBill.getBodyValueAt(iRow, saNameFree[4]));

        voFree.setVfree5((String)pnlBill.getBodyValueAt(iRow, saNameFree[5]));
      }

      invVO.setFreeItemVO(voFree);
      ((FreeItemRefPane)pnlBill.getBodyItem(saNameFree[0]).getComponent()).setFreeItemParam(invVO);

      ((FreeItemRefPane)pnlBill.getBodyItem(saNameFree[0]).getComponent()).setButtonVisible(true);

      return true;
    }

    ((FreeItemRefPane)pnlBill.getBodyItem(saNameFree[0]).getComponent()).setButtonVisible(false);

    return false;
  }

  public static void beforeEditWhenBodyBatch(BillCardPanel panel, String pk_corp, BillEditEvent e, String[] invFieldNames, String freePrefix)
  {
    if ((panel == null) || (pk_corp == null) || (pk_corp.length() <= 0) || (invFieldNames == null) || (invFieldNames.length != 9) || (freePrefix == null) || (freePrefix.length() <= 0))
    {
      SCMEnv.out("传入参数不完整！");
    }

    panel.stopEditing();
    int row = e.getRow();
    Object omangid = panel.getBodyValueAt(row, invFieldNames[0]);
    if ((omangid == null) || (omangid.toString().length() <= 0))
      return;
    String cmangid = omangid.toString();

    InvVO invVO = new InvVO();

    Object owhid = panel.getBodyValueAt(row, invFieldNames[8]);
    WhVO whvo = new WhVO();
    if ((owhid != null) && (owhid.toString().length() > 0)) {
      String cwhid = owhid.toString();
      UIRefPane refPane = new UIRefPane();
      refPane.setRefNodeName("仓库档案");
      refPane.setValue(cwhid);
      refPane.setPK(cwhid);

      whvo.setCwarehouseid(cwhid);
      whvo.setCwarehousecode(refPane.getRefCode());
      whvo.setCwarehousename(refPane.getRefName());
      whvo.setIsWasteWh(new Integer(0));
      whvo.setPk_corp(pk_corp);
    }

    ArrayList listI = new ArrayList();
    listI.add(cmangid);
    ArrayList retList = null;
    try {
      ArrayList allList = new ArrayList();
      allList.add(listI);
      retList = DefHelper.queryFreeVOByInvIDsGroupByBills(allList);
    } catch (Exception ee) {
      SCMEnv.out(ee);
    }

    if (retList != null) {
      ArrayList freeList = (ArrayList)(ArrayList)retList.get(0);
      FreeVO freeVO = (FreeVO)freeList.get(0);

      if ((freeVO == null) || (((freeVO.getVfreename1() == null) || (freeVO.getVfreename1().length() < 1)) && ((freeVO.getVfreename2() == null) || (freeVO.getVfreename2().length() < 1)) && ((freeVO.getVfreename3() == null) || (freeVO.getVfreename3().length() < 1)) && ((freeVO.getVfreename4() == null) || (freeVO.getVfreename4().length() < 1)) && ((freeVO.getVfreename5() == null) || (freeVO.getVfreename5().length() < 1))))
      {
        invVO.setIsFreeItemMgt(new Integer(0));
      }
      else {
        invVO.setIsFreeItemMgt(new Integer(1));

        freeVO.setCinventoryid(cmangid);
        for (int i = 1; i < 6; i++) {
          Object oTemp = panel.getBodyValueAt(row, freePrefix + i);
          freeVO.setAttributeValue("vfree" + i, oTemp == null ? null : oTemp.toString());
        }

        invVO.setFreeItemVO(freeVO);
      }
    }
    else
    {
      invVO.setIsFreeItemMgt(new Integer(0));
    }

    invVO.setCinventoryid(cmangid);

    Object strTemp = panel.getBodyValueAt(row, invFieldNames[1]);
    invVO.setCinventorycode(strTemp == null ? null : strTemp.toString());

    strTemp = panel.getBodyValueAt(row, invFieldNames[2]);
    invVO.setInvname(strTemp == null ? null : strTemp.toString());

    strTemp = panel.getBodyValueAt(row, invFieldNames[3]);
    invVO.setInvspec(strTemp == null ? null : strTemp.toString());

    strTemp = panel.getBodyValueAt(row, invFieldNames[4]);
    invVO.setInvtype(strTemp == null ? null : strTemp.toString());

    strTemp = panel.getBodyValueAt(row, invFieldNames[5]);
    invVO.setMeasdocname(strTemp == null ? null : strTemp.toString());

    if ((invFieldNames[7] != null) && (invFieldNames[7].trim().length() > 0)) {
      strTemp = panel.getBodyValueAt(row, invFieldNames[7]);
      if (strTemp != null) {
        strTemp = strTemp.toString().equalsIgnoreCase("Y") ? new Integer(1) : new Integer(0);
      }
      else
      {
        strTemp = new Integer(0);
      }invVO.setIsAstUOMmgt((Integer)strTemp);
    }
    if ((invFieldNames[6] != null) && (invFieldNames[6].trim().length() > 0))
    {
      strTemp = panel.getBodyValueAt(row, invFieldNames[6]);
      invVO.setCastunitid(strTemp == null ? null : strTemp.toString());
    }

    IICPub_LotNumbRefPane refPane = (IICPub_LotNumbRefPane)panel.getBodyItem("vproducenum").getComponent();

    refPane.setParameter(whvo, invVO);
  }

  public static UIRefPane beforeEditWhenBodyBatch(ParaVOForBatch para)
    throws ValidationException
  {
    CircularlyAccessibleValueObject[] voaParaLot = BillPanelTool.get2VOsForLotNumRefPane(para);

    LotNumbRefPane refPane = (LotNumbRefPane)para.getCardPanel().getBodyItem("vproducenum").getComponent();
    refPane.setParameter((WhVO)voaParaLot[0], (InvVO)voaParaLot[1]);

    return refPane;
  }

  public static void restrictStoreOrg(Component com)
  {
    restrictStoreOrg(com, true);
  }

  public static void restrictStoreOrg(Component com, boolean bRefresh)
  {
    if (com == null) {
      SCMEnv.out("Para Error,Reutrn!");
      return;
    }
    if (!(com instanceof UIRefPane)) {
      SCMEnv.out("Para Error,Component is not instanceof UIRefPane , Reutrn!");
      return;
    }
    ((UIRefPane)com).getRefModel().addWherePart(" and bd_calbody.property in (0,1) ");
  }

  public static String getPurIdByPsnId(String strPsnId)
  {
    if ((strPsnId == null) || (strPsnId.trim().length() == 0))
      return null;
    try
    {
      Object[][] objs = PubHelper.queryResultsFromAnyTable("bd_psntopurorg", new String[] { "pk_purorg" }, " pk_psndoc = '" + strPsnId + "' ");
      if ((objs != null) && (objs.length == 1) && (objs[0] != null) && (objs[0][0] != null))
        return objs[0][0] + "";
    }
    catch (Exception e) {
      SCMEnv.out("根据业务员关联查询所性采购组织时出现异常，可手工填写采购组织");
    }
    return null;
  }

  public static void loadSourceInfoAll(BillCardPanel card, String billType)
  {
    if ((card == null) || (card.getBillModel() == null)) {
      return;
    }
    SourceBillTool.loadSourceInfoAll(card.getBillModel(), billType);
  }

  public static void loadSourceInfoAll(BillListPanel list, String billType)
  {
    if ((list == null) || (list.getBodyBillModel() == null)) {
      return;
    }
    SourceBillTool.loadSourceInfoAll(list.getBodyBillModel(), billType);
  }

  public static void loadSourceInfoAll(BillModel model, String billType)
  {
    SourceBillTool.loadSourceInfoAll(model, billType);
  }

  public static void initBusiAddBtns(ButtonObject btnBusi, ButtonObject btnAdd, String strBillType, String strPkCorp)
  {
    BillPanelTool.initBusiAddBtns(btnBusi, btnAdd, strBillType, strPkCorp);
  }

  public static void switchReturnType(HashMap mapCodeName2Pk, HashMap mapCodeNameFlag, QueryConditionVO[] condVOs, boolean fromCodeName2Pk)
  {
    if ((mapCodeName2Pk == null) || (mapCodeName2Pk.size() == 0) || (mapCodeNameFlag == null) || (mapCodeNameFlag.size() == 0) || (condVOs == null) || (condVOs.length == 0))
    {
      return;
    }
    UFBoolean bCode = null;
    for (int i = 0; i < condVOs.length; i++) {
      Integer iType = condVOs[i].getReturnType();
      if ((mapCodeName2Pk.containsKey(condVOs[i].getFieldCode())) && ((new Integer(0).equals(iType)) || (new Integer(1).equals(iType))) && (fromCodeName2Pk))
      {
        condVOs[i].setReturnType(new Integer(2));
      } else {
        if ((!mapCodeName2Pk.containsKey(condVOs[i].getFieldCode())) || (!new Integer(2).equals(iType)) || (fromCodeName2Pk)) {
          continue;
        }
        bCode = (UFBoolean)mapCodeNameFlag.get(condVOs[i].getFieldCode());
        if ((bCode != null) && (bCode.booleanValue()))
          condVOs[i].setReturnType(new Integer(0));
        else
          condVOs[i].setReturnType(new Integer(1));
      }
    }
  }

  public static void switchFieldCode(HashMap mapCodeName2Pk, QueryConditionVO[] condVOs)
  {
    if ((mapCodeName2Pk == null) || (mapCodeName2Pk.size() == 0) || (condVOs == null) || (condVOs.length == 0))
    {
      return;
    }
    for (int i = 0; i < condVOs.length; i++) {
      if (!mapCodeName2Pk.containsKey(condVOs[i].getFieldCode()))
        continue;
      condVOs[i].setFieldCode((String)mapCodeName2Pk.get(condVOs[i].getFieldCode()));
    }
  }

  public static void setBillCardPanelDefaultValue(BillCardPanel cardPanel, BillEditListener listener)
  {
    if (cardPanel == null) {
      return;
    }
    if (!cardPanel.getBillData().getEnabled()) {
      SCMEnv.out("本方法目前只支持单据卡片编辑状态设置固定值");
      return;
    }
    BillData bd = cardPanel.getBillData();

    BillItem[] items = bd.getHeadTailItems();
    int iLen = items == null ? 0 : items.length;
    for (int i = 0; i < iLen; i++) {
      if (PuPubVO.getString_TrimZeroLenAsNull(items[i].getDefaultValue()) == null) {
        continue;
      }
      if (PuPubVO.getString_TrimZeroLenAsNull(items[i].getValue()) != null) {
        continue;
      }
      items[i].clearViewData();
      if (listener != null)
        listener.afterEdit(new BillEditEvent(items[i].getComponent(), null, items[i].getValue(), items[i].getKey(), -1, items[i].getPos()));
    }
  }

  public static String getAuditLessThanMakeMsg(AggregatedValueObject[] vos, String strKeyMake, String strKeyBillCode, UFDate ufdAudit)
  {
    return getAuditLessThanMakeMsg(vos, strKeyMake, strKeyBillCode, ufdAudit, null);
  }

  public static String getAuditLessThanMakeMsg(AggregatedValueObject[] vos, String strKeyMake, String strKeyBillCode, UFDate ufdAudit, String strBillType)
  {
    String strErrBill = "";
    if ((vos == null) || (vos.length <= 0) || (strKeyMake == null) || (strKeyBillCode == null) || (ufdAudit == null)) {
      SCMEnv.out("传入参数存在空，直接返回NULL");
      return null;
    }
    int iLen = vos.length;
    UFDate ufdMake = null;
    for (int i = 0; i < iLen; i++) {
      if ((vos[i] == null) || (vos[i].getParentVO() == null)) {
        continue;
      }
      ufdMake = PuPubVO.getUFDate(vos[i].getParentVO().getAttributeValue(strKeyMake));
      if (ufdMake == null) {
        continue;
      }
      if (ufdAudit.compareTo(ufdMake) >= 0) {
        continue;
      }
      if (strErrBill.length() > 0) {
        strErrBill = strErrBill + NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000000");
      }
      strErrBill = strErrBill + vos[i].getParentVO().getAttributeValue(strKeyBillCode);
    }
    strErrBill = PuPubVO.getString_TrimZeroLenAsNull(strErrBill);
    if (strErrBill == null) {
      return null;
    }
    String strKeyName = "制单日期";
    if (strBillType != null) {
      if (strBillType.trim().equals("20"))
        strKeyName = NCLangRes.getInstance().getStrByID("COMMON", "UC000-0003665");
      else if (strBillType.trim().equals("21"))
        strKeyName = NCLangRes.getInstance().getStrByID("COMMON", "UC000-0003550");
      else if (strBillType.trim().equals("23"))
        strKeyName = NCLangRes.getInstance().getStrByID("COMMON", "UC000-0000650");
      else if (strBillType.trim().equals("25"))
        strKeyName = NCLangRes.getInstance().getStrByID("COMMON", "UC000-0000986");
      else if (strBillType.trim().equals("61")) {
        strKeyName = NCLangRes.getInstance().getStrByID("COMMON", "UC000-0003550");
      }
    }
    strErrBill = NCLangRes.getInstance().getStrByID("4004pub", "UPP4004pub-000199", null, new String[] { strKeyName, strErrBill, strKeyName });

    return strErrBill;
  }

  public static boolean isExist(Object[] oaSrc, Object oDest)
  {
    if ((oDest == null) || (oaSrc == null) || (oaSrc.length == 0)) {
      return false;
    }
    int iLen = oaSrc.length;
    for (int i = 0; i < iLen; i++) {
      if (oDest.equals(oaSrc[i])) {
        return true;
      }
    }
    return false;
  }

  public static Object appendOneToArray(Object[] oaDest, Object oNewOne)
  {
    if (oNewOne == null) {
      return oaDest;
    }
    if (oNewOne.getClass().isArray()) {
      SCMEnv.out("建议调用另外方法:appendArrayToArray(Object[] oaDest, Object[] oaSrc, Class cls)");
      return oaDest;
    }
    return appendArrayToArray(oaDest, new Object[] { oNewOne }, oNewOne.getClass());
  }

  public static Object appendArrayToArray(Object[] oaDest, Object[] oaSrc, Class cls)
  {
    if ((oaSrc == null) || (oaSrc.length == 0)) {
      return oaDest;
    }
    if ((oaDest == null) || (oaDest.length == 0)) {
      return oaSrc;
    }
    int iInitLen = oaDest.length;
    int iAppedLen = oaSrc.length;
    Object oRslt = Array.newInstance(cls, iInitLen + iAppedLen);
    System.arraycopy(oaDest, 0, oRslt, 0, iInitLen);
    System.arraycopy(oaSrc, 0, oRslt, iInitLen, iAppedLen);
    return oRslt;
  }

  public static boolean isExistBtnName(ButtonObject[] oaSrc, ButtonObject oDest)
  {
    if ((oDest == null) || (oaSrc == null) || (oaSrc.length == 0)) {
      return false;
    }
    int iLen = oaSrc.length;
    for (int i = 0; i < iLen; i++) {
      if (oaSrc[i] == null) {
        continue;
      }
      if ((oDest.getName() != null) && (oDest.getName().equals(oaSrc[i].getName()))) {
        return true;
      }
    }
    return false;
  }

  public static String[][] getVendorInvInfos(String strVendorId, String[] saMangId)
  {
    if ((PuPubVO.getString_TrimZeroLenAsNull(strVendorId) == null) || (saMangId == null) || (saMangId.length == 0))
    {
      return (String[][])null;
    }
    int iLen = saMangId.length;
    String[] saVendorId = new String[iLen];
    for (int i = 0; i < iLen; i++) {
      saVendorId[i] = strVendorId;
    }
    Object[][] oaVendorInvInfos = nc.ui.scm.pub.cache.CacheTool.getMultiColValue2("vrm_vendorstock", "cvendormangid", "cmangid", new String[] { "vvendinventorycode", "vvendinventoryname" }, saVendorId, saMangId);

    if ((oaVendorInvInfos == null) || (oaVendorInvInfos.length == 0) || (oaVendorInvInfos.length != iLen)) {
      return (String[][])null;
    }
    String[][] saVendorInvInfos = new String[iLen][2];
    for (int i = 0; i < iLen; i++) {
      saVendorInvInfos[i][0] = (oaVendorInvInfos[i] == null ? null : (String)oaVendorInvInfos[i][0]);
      saVendorInvInfos[i][1] = (oaVendorInvInfos[i] == null ? null : (String)oaVendorInvInfos[i][1]);
    }
    return saVendorInvInfos;
  }

  public static void loadVendorInvInfos(String strVendorId, String[] saMangId, BillModel bm, int iBgnPos, int iEndPos)
  {
    String[][] saCodeNames = getVendorInvInfos(strVendorId, saMangId);
    if ((saCodeNames != null) && (saCodeNames.length == saMangId.length))
      for (int i = iBgnPos; i <= iEndPos; i++) {
        bm.setValueAt(saCodeNames[(i - iBgnPos)][0], i, "vvendinventorycode");
        bm.setValueAt(saCodeNames[(i - iBgnPos)][1], i, "vvendinventoryname");
      }
    else
      for (int i = iBgnPos; i <= iEndPos; i++) {
        bm.setValueAt(null, i, "vvendinventorycode");
        bm.setValueAt(null, i, "vvendinventoryname");
      }
  }
}