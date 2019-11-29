package nc.ui.dm.pub;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.bd.ref.UFRefManage;
import nc.ui.dm.pub.cardpanel.DMBillCardPanel;
import nc.ui.dm.pub.cardpanel.DMBillListPanel;
import nc.ui.dm.pub.mvc.ShowDelivOrg;
import nc.ui.dm.pub.ref.DelivorgRef;
import nc.ui.ic.pub.bill.InvoInfoBYFormula;
import nc.ui.ic.pub.lot.LotNumbRefPane;
import nc.ui.ic.pub.lot.LotRefModel;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.FramePanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.bill.BillBodyMenuListener;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListData;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillScrollPane;
import nc.ui.pub.bill.BillSortListener;
import nc.ui.pub.para.SysInitBO_Client;
import nc.ui.scm.file.DocumentManager;
import nc.ui.scm.ic.exp.GeneralMethod;
import nc.ui.scm.ic.freeitem.FreeItemRefPane;
import nc.ui.scm.ic.measurerate.InvMeasRate;
import nc.ui.scm.pub.ArryFormula;
import nc.ui.scm.pub.CacheTool;
import nc.ui.scm.pub.data.DMListData;
import nc.ui.scm.pub.def.DefSetTool;
import nc.ui.scm.pub.invmeas.InvMeasInfo;
import nc.ui.scm.pub.panel.AlreadyAfterEditListener;
import nc.ui.scm.pub.panel.CheckBoxChangedListener;
import nc.ui.scm.pub.query.ConvertQueryCondition;
import nc.ui.scm.pub.report.OrientDialog;
import nc.ui.scm.sourcebill.SourceBillFlowDlg;
import nc.vo.bd.CorpVO;
import nc.vo.bd.def.DefVO;
import nc.vo.bd.def.DefdefVO;
import nc.vo.dm.pub.DMDataVO;
import nc.vo.dm.pub.DMFreeVO;
import nc.vo.dm.pub.DMVO;
import nc.vo.dm.pub.tools.FormulaTools;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.NullFieldException;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.ic.bill.FreeVO;
import nc.vo.scm.ic.bill.InvVO;
import nc.vo.scm.ic.bill.WhVO;
import nc.vo.scm.ic.exp.ICDateException;
import nc.vo.scm.ic.exp.ICHeaderNullFieldException;
import nc.vo.scm.ic.exp.ICLocatorException;
import nc.vo.scm.ic.exp.ICNullFieldException;
import nc.vo.scm.ic.exp.ICNumException;
import nc.vo.scm.ic.exp.ICPriceException;
import nc.vo.scm.ic.exp.ICRepeatException;
import nc.vo.scm.ic.exp.ICSNException;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.sm.UserVO;

public class DMToftPanel extends BillUI
  implements MouseListener, ListSelectionListener, TableColumnModelListener, TableModelListener, BillBodyMenuListener, BillEditListener, BillSortListener, CheckBoxChangedListener, AlreadyAfterEditListener
{
  protected ArrayList alAgentCorpIDsofDelivOrg;
  protected Integer BD301;
  protected Integer BD501;
  protected Integer BD502;
  protected Integer BD503;
  protected Integer BD505;
  protected ButtonObject boAudit;
  protected ButtonObject boBill;
  protected ButtonObject boCancelAudit;
  protected ButtonObject boDel;
  protected ButtonObject boEnd;
  protected ButtonObject boPrintPreview;
  protected ButtonObject boSort;
  protected UFBoolean DM001;
  protected UFBoolean DM002;
  protected UFBoolean DM003;
  protected UFBoolean DM004;
  protected UFBoolean DM005;
  protected UFBoolean DM006;
  protected UFBoolean DM007;
  protected UFBoolean DM008;
  protected UFDouble DM009;
  protected UFBoolean DM010;
  protected UFBoolean DM011;
  protected UFBoolean DM012;
  protected UFBoolean DM013;
  protected String DM014;
  protected String DM015;
  protected UFBoolean DM016;
  protected String DM017;
  protected InvoInfoBYFormula iibyfInvoInfoBYFormula = new InvoInfoBYFormula();

  protected ArrayList m_alAllVOs = new ArrayList();
  public String[] m_aryFormulasBody;
  public String[] m_aryFormulasHead;
  private boolean m_bDelivOrgNoShow;
  private String m_BillTypeCode;
  protected boolean m_bIsGetNewBillCode = false;
  protected String m_currentCapacityUnit;
  protected String m_currentWeightUnit;
  protected DefVO[] m_defvosb = null;

  protected DefVO[] m_defvosh = null;
  private String m_delivOrgPK;
  private String m_delivOrgCode;
  private String m_delivOrgName;
  private int m_delivsequence = 0;
  private int m_editFlag;
  protected FreeItemRefPane m_firpFreeItemRefPane;
  protected FreeItemRefPane m_firpFreeItemRefPaneHead;
  public Hashtable m_htbBodyItemkey;
  public Hashtable m_htbHeadItemkey;
  protected int m_iLastSelListHeadRow = -1;

  protected Hashtable m_invht = new Hashtable();
  protected String[] m_itemANumKeys;
  protected String m_itemFactorKey;
  protected String[] m_itemMoneyKey;
  protected String[] m_itemNumKeys;
  protected String[] m_itemPriceKeys;
  protected LotNumbRefPane m_lnrpLotNumbRefPane;
  protected LotNumbRefPane m_lnrpLotNumbRefPaneHead;
  protected UFDate m_LogDate = null;

  protected String m_oldBillCode = null;
  protected String m_sCorpID;
  protected String m_sNodeCode;
  protected String m_sUserID;
  protected String m_sUserName;
  protected String m_sCorpName;
  protected String m_sHeaderVOName;
  protected String m_sItemVOName;
  protected String m_sVOName;
  protected InvMeasRate m_voInvMeas = new InvMeasRate();

  private Hashtable m_whht = new Hashtable();
  protected DMQueryConditionDlg queryConditionDlg;
  protected String sBillCodeKeyName;
  protected String sPrimaryKeyName;
  protected String strTitle;

  public DMToftPanel()
  {
  }

  public DMToftPanel(boolean bDelivOrgNoShow)
  {
  }

  public void actionPerformed(ActionEvent e)
  {
  }

  public boolean onClosing()
  {
    return true;
  }

  protected void afterBodyFreeItemEdit(int row, int col, DMBillCardPanel dmbcp)
  {
    DMFreeVO fvoFreeVO = new DMFreeVO();
    fvoFreeVO.combineValueWithOtherFreeVO(getFreeItemRefPane().getFreeVO());
    String[] sFreeVOKeyName = fvoFreeVO.getAttributeNames();
    if (null != sFreeVOKeyName) {
      for (int i = 0; i < sFreeVOKeyName.length; i++) {
        dmbcp.setBodyValueAt(fvoFreeVO.getAttributeValue(sFreeVOKeyName[i]), row, sFreeVOKeyName[i]);
      }
    }

    dmbcp.setBodyValueAt(null, row, "vbatchcode");
    getLotNumbRefPane().setValue(null);
  }

  protected void afterBodyLotEdit(int row, int col, DMBillCardPanel dmbcp)
  {
    getBodyLotRefbyHand("vbatchcode", row, dmbcp);
  }

  public void afterEdit(BillEditEvent e)
  {
    super.afterEdit(e);
  }

  protected void afterHeaderFreeItemEdit(DMBillCardPanel dmbcp)
  {
    DMFreeVO fvoFreeVO = new DMFreeVO();
    fvoFreeVO.combineValueWithOtherFreeVO(getFreeItemRefPaneHead().getFreeVO());
    String[] sFreeVOKeyName = fvoFreeVO.getAttributeNames();
    if (null != sFreeVOKeyName) {
      for (int i = 0; i < sFreeVOKeyName.length; i++) {
        if (null != dmbcp.getHeadItem(sFreeVOKeyName[i])) {
          dmbcp.getHeadItem(sFreeVOKeyName[i]).setValue(fvoFreeVO.getAttributeValue(sFreeVOKeyName[i]));
        }
      }
    }
    else {
      for (int i = 0; i < 10; i++) {
        if (null != dmbcp.getHeadItem("vfree" + i)) {
          dmbcp.getHeadItem("vfree" + i).setValue(null);
        }
      }
    }

    dmbcp.getHeadItem("vbatchcode").setValue(null);
    getLotNumbRefPaneHead().setValue(null);

    initHeaderLot(dmbcp);
    WhVO wvo = queryWhInfo(dmbcp.getHeadItem("pksendstore").getValue());
    getLotNumbRefPane().setParameter(wvo, getInvInfo((String)dmbcp.getHeadItem("pkinv").getValueObject(), null));
  }

  protected void afterHeaderLotEdit(DMBillCardPanel dmbcp)
  {
    getHeaderLotRefbyHand("vbatchcode", dmbcp);
  }

  protected void afterQuery(ConditionVO[] voCons)
  {
  }

  public void afterRefEdit(Object value, int row, String CodeField, String PKField, String NameField)
  {
    ((DMBillCardPanel)getBillCardPanel()).afterRefEdit(value, row, CodeField, PKField, NameField);
  }

  public void afterSort(String key)
  {
  }

  public void bodyRowChange(BillEditEvent e)
  {
    if (e.getSource() == getBillListPanel().getHeadTable())
      listHeadRowChange(e);
  }

  public UFDouble calcurateTotal(String key)
  {
    UFDouble dTotal = new UFDouble(0.0D);

    for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
      Object oValue = getBillCardPanel().getBodyValueAt(i, key);
      String sValue = (oValue == null) || (oValue.equals("")) ? "0" : oValue.toString();
      dTotal = dTotal.add(new UFDouble(sValue));
    }

    return dTotal;
  }

  protected BillData changeBillDataByUserDef(BillData oldBillData)
  {
    if (getDefVOsH() != null) {
      updateItemByDef(oldBillData, getDefVOsH(), "vuserdef", true, 0);
    }
    if (getDefVOsB() != null) {
      updateItemByDef(oldBillData, getDefVOsB(), "vuserdef", false, 0);
    }
    return oldBillData;
  }

  protected BillListData changeBillListDataByUserDef(BillListData oldBillData)
  {
    if (getDefVOsH() != null) {
      updateItemByDef(oldBillData, getDefVOsH(), "vuserdef", true, 0);
    }
    if (getDefVOsB() != null) {
      updateItemByDef(oldBillData, getDefVOsB(), "vuserdef", false, 0);
    }
    return oldBillData;
  }

  public boolean checkHeaderVO(AggregatedValueObject vo, DMBillCardPanel bcp)
  {
    try
    {
      if (!bcp.checkHeaderVO())
        return false;
      return checkOther(vo);
    }
    catch (ICDateException e)
    {
      String sErrorMessage = GeneralMethod.getBodyErrorMessage(getBillCardPanel(), e.getErrorRowNums(), e.getHint());
      handleException(new Exception(sErrorMessage));
      return false;
    }
    catch (ICNullFieldException e)
    {
      String sErrorMessage = GeneralMethod.getBodyErrorMessage(getBillCardPanel(), e.getErrorRowNums(), e.getHint());
      handleException(new Exception(sErrorMessage));
      return false;
    }
    catch (ICNumException e)
    {
      String sErrorMessage = GeneralMethod.getBodyErrorMessage(getBillCardPanel(), e.getErrorRowNums(), e.getHint());
      handleException(new Exception(sErrorMessage));
      return false;
    }
    catch (ICPriceException e)
    {
      String sErrorMessage = GeneralMethod.getBodyErrorMessage(getBillCardPanel(), e.getErrorRowNums(), e.getHint());
      handleException(new Exception(sErrorMessage));
      return false;
    }
    catch (ICSNException e)
    {
      String sErrorMessage = GeneralMethod.getBodyErrorMessage(getBillCardPanel(), e.getErrorRowNums(), e.getHint());
      handleException(new Exception(sErrorMessage));
      return false;
    }
    catch (ICLocatorException e)
    {
      String sErrorMessage = GeneralMethod.getBodyErrorMessage(getBillCardPanel(), e.getErrorRowNums(), e.getHint());
      handleException(new Exception(sErrorMessage));
      return false;
    }
    catch (ICRepeatException e)
    {
      String sErrorMessage = GeneralMethod.getBodyErrorMessage(getBillCardPanel(), e.getErrorRowNums(), e.getHint());
      handleException(new Exception(sErrorMessage));
      return false;
    }
    catch (ICHeaderNullFieldException e)
    {
      String sErrorMessage = GeneralMethod.getHeaderErrorMessage(getBillCardPanel(), e.getErrorRowNums(), e.getHint());
      handleException(new Exception(sErrorMessage));
      return false;
    }
    catch (NullFieldException e) {
      handleException(e);
      return false;
    }
    catch (ValidationException e) {
      SCMEnv.info("较验异常！其他未知故障...");
      showErrorMessage(NCLangRes.getInstance().getStrByID("4014", "UPP4014-000050"));
      handleException(e);
      return false;
    }
    catch (Exception e) {
      handleException(e);
    }return false;
  }

  public boolean checkOther(AggregatedValueObject vo)
    throws Exception
  {
    return true;
  }

  public boolean checkVO(AggregatedValueObject vo, DMBillCardPanel bcp)
  {
    try
    {
      if (!bcp.checkVO())
        return false;
      return checkOther(vo);
    }
    catch (ICDateException e)
    {
      String sErrorMessage = GeneralMethod.getBodyErrorMessage(getBillCardPanel(), e.getErrorRowNums(), e.getHint());
      handleException(new Exception(sErrorMessage));
      return false;
    }
    catch (ICNullFieldException e)
    {
      String sErrorMessage = GeneralMethod.getBodyErrorMessage(getBillCardPanel(), e.getErrorRowNums(), e.getHint());
      handleException(new Exception(sErrorMessage));
      return false;
    }
    catch (ICNumException e)
    {
      String sErrorMessage = GeneralMethod.getBodyErrorMessage(getBillCardPanel(), e.getErrorRowNums(), e.getHint());
      handleException(new Exception(sErrorMessage));
      return false;
    }
    catch (ICPriceException e)
    {
      String sErrorMessage = GeneralMethod.getBodyErrorMessage(getBillCardPanel(), e.getErrorRowNums(), e.getHint());
      handleException(new Exception(sErrorMessage));
      return false;
    }
    catch (ICSNException e)
    {
      String sErrorMessage = GeneralMethod.getBodyErrorMessage(getBillCardPanel(), e.getErrorRowNums(), e.getHint());
      handleException(new Exception(sErrorMessage));
      return false;
    }
    catch (ICLocatorException e)
    {
      String sErrorMessage = GeneralMethod.getBodyErrorMessage(getBillCardPanel(), e.getErrorRowNums(), e.getHint());
      handleException(new Exception(sErrorMessage));
      return false;
    }
    catch (ICRepeatException e)
    {
      String sErrorMessage = GeneralMethod.getBodyErrorMessage(getBillCardPanel(), e.getErrorRowNums(), e.getHint());
      handleException(new Exception(sErrorMessage));
      return false;
    }
    catch (ICHeaderNullFieldException e)
    {
      String sErrorMessage = GeneralMethod.getHeaderErrorMessage(getBillCardPanel(), e.getErrorRowNums(), e.getHint());
      handleException(new Exception(sErrorMessage));
      return false;
    }
    catch (NullFieldException e) {
      handleException(e);

      return false;
    }
    catch (ValidationException e) {
      SCMEnv.info("较验异常！其他未知故障...");
      showErrorMessage(NCLangRes.getInstance().getStrByID("4014", "UPP4014-000050"));
      handleException(e);
      return false;
    }
    catch (Exception e) {
      handleException(e);
    }return false;
  }

  public void columnAdded(TableColumnModelEvent e)
  {
  }

  public void columnMarginChanged(ChangeEvent e)
  {
  }

  public void columnMoved(TableColumnModelEvent e)
  {
  }

  public void columnRemoved(TableColumnModelEvent e)
  {
  }

  public void columnSelectionChanged(ListSelectionEvent e)
  {
    if (getShowState().equals(DMBillStatus.Card)) {
      int colnow = getBillCardPanel().getBillTable().getSelectedColumn();
      int rownow = getBillCardPanel().getBillTable().getSelectedRow();
      if ((colnow >= 0) && (rownow >= 0)) {
        initRef(rownow, colnow, (DMBillCardPanel)getBillCardPanel());
        String key = getBillCardPanel().getBodyItems()[getBillCardPanel().getBillTable().convertColumnIndexToModel(colnow)].getKey();

        Object value = getBillCardPanel().getBodyValueAt(rownow, key);
        whenEntered(rownow, colnow, key, value, getBillCardPanel());
      }
    }
  }

  protected void execBodyFormulas(CircularlyAccessibleValueObject[] alBodys, boolean isListPanel)
  {
    if ((alBodys == null) || (alBodys.length == 0)) {
      return;
    }

    ArrayList arylistItemField = getItemFormulaBody();

    ArryFormula arryFormula = new ArryFormula();

    ArrayList arylistresult = arryFormula.checkHtbkey(arylistItemField, getItemKeyBody());

    String[] arysFormulaGet = (String[])(String[])arylistresult.get(1);

    String[] arysOldFormula = getOldFormulasBody();

    String[] aryCombineFormula = ArryFormula.combineStringArray(arysOldFormula, arysFormulaGet);

    if ((aryCombineFormula != null) && (aryCombineFormula.length > 0))
      if (isListPanel) {
        getBillListPanel().getBodyBillModel().execFormulasWithVO(alBodys, aryCombineFormula);
      }
      else
        getBillCardPanel().getBillModel().execFormulas(aryCombineFormula);
  }

  public void execFormulaBodys(CircularlyAccessibleValueObject[] alBodys)
  {
    execBodyFormulas(alBodys, true);
  }

  protected void execFormulaHeads(CircularlyAccessibleValueObject[] alHeads)
  {
    execHeadTailFormulas(alHeads, true);
  }

  protected void execHeadTailFormulas(CircularlyAccessibleValueObject[] alHeads, boolean isListPanel)
  {
    if ((alHeads == null) || (alHeads.length == 0)) {
      return;
    }
    ArrayList arylistItemField = getItemFormulaHead();

    ArryFormula arryFormula = new ArryFormula();

    ArrayList arylistresult = arryFormula.checkHtbkey(arylistItemField, getItemKeyHead());

    String[] arysFormulaGet = (String[])(String[])arylistresult.get(1);

    String[] arysOldFormula = getOldFormulasHead();

    String[] aryCombineFormula = ArryFormula.combineStringArray(arysOldFormula, arysFormulaGet);

    if ((aryCombineFormula != null) && (aryCombineFormula.length > 0))
      if (isListPanel) {
        getBillListPanel().getHeadBillModel().execFormulasWithVO(alHeads, aryCombineFormula);
      }
      else
        getBillCardPanel().execHeadFormulas(aryCombineFormula);
  }

  protected void filterMeas(String sInvID, String sCastUnitNameField, String pkcorp)
  {
    UIRefPane refCast = (UIRefPane)getBillCardPanel().getBodyItem(sCastUnitNameField).getComponent();

    refCast.setReturnCode(false);

    this.m_voInvMeas.filterMeas(pkcorp, sInvID, refCast);
  }

  protected void filterNullLine(String[] itemkey, BillCardPanel billCard)
  {
    Vector vTemp = new Vector();
    boolean bEmpty = true;
    Object sValue = null;
    for (int i = 0; i < billCard.getRowCount(); i++) {
      bEmpty = true;
      for (int j = 0; j < itemkey.length; j++) {
        try {
          sValue = billCard.getBodyValueAt(i, itemkey[j]);
        }
        catch (Exception e) {
          SCMEnv.info("参数String[] itemkey里面的" + itemkey[j] + "元素不正确，导致：billCard.getBodyValueAt(i, itemkey[j]) 出错" + e.getMessage());

          handleException(e);
        }

        if ((sValue != null) && (sValue.toString().trim().length() > 0)) {
          bEmpty = false;
          break;
        }
      }
      if (bEmpty) {
        vTemp.addElement(new Integer(i));
      }
    }
    int size = vTemp.size();
    if (size > 0) {
      int[] iaRows = new int[size];
      for (int i = 0; i < size; i++)
        iaRows[i] = new Integer(vTemp.elementAt(i).toString()).intValue();
      billCard.getBillModel().delLine(iaRows);
    }
  }

  protected void fullScreen(BillModel bmBillModel, int FirstAddRows)
  {
    int allRow = bmBillModel.getRowCount();
    for (int i = allRow; i < FirstAddRows; i++)
      bmBillModel.addLine();
  }

  protected String genTempPK(String OldPK, int NumberBegin)
  {
    int iTotal = 20;
    int iTail = iTotal - NumberBegin;
    String sH = OldPK.substring(0, NumberBegin);
    String sT = OldPK.substring(NumberBegin);
    sT = Integer.toString(Integer.parseInt(sT) + 1);
    int Len = sT.length();
    for (int i = 0; i < iTail - Len; i++) {
      sT = "0" + sT;
    }
    return sH + sT;
  }

  public ButtonObject getAddButton()
  {
    return this.boAdd;
  }

  public ButtonObject getAddLineButton()
  {
    return this.boAddLine;
  }

  public ArrayList getAgentCorpIDsofDelivOrg()
  {
    return this.alAgentCorpIDsofDelivOrg;
  }

  public ArrayList getAllVOs()
  {
    return this.m_alAllVOs;
  }

  public ButtonObject getAuditButton()
  {
    return this.boAudit;
  }

  public String getBelongCorpIDofDelivOrg()
  {
    return getCorpPrimaryKey();
  }

  public ButtonObject getBillButton()
  {
    return this.boBill;
  }

  public String getBillButtonAction(ButtonObject bo)
  {
    return null;
  }

  public String getBillButtonState()
  {
    return null;
  }

  public BillCardPanel getBillCardPanel()
  {
    if (this.ivjBillCardPanel == null) {
      try {
        this.ivjBillCardPanel = new DMBillCardPanel(getNodeCode());
        this.ivjBillCardPanel.setName("BillCardPanel");

        this.ivjBillCardPanel.setBillType(getBillType());

        this.ivjBillCardPanel.setCorp(getCorpPrimaryKey());

        this.ivjBillCardPanel.setOperator(getOperator());

        ((DMBillCardPanel)this.ivjBillCardPanel).setNodeCode(getNodeCode());

        this.ivjBillCardPanel.addEditListener(this);
        this.ivjBillCardPanel.addBodyEditListener2(this);
        this.ivjBillCardPanel.addBodyMenuListener(this);

        ((DMBillCardPanel)this.ivjBillCardPanel).addAfterEditListener(this);
        ((DMBillCardPanel)this.ivjBillCardPanel).addCheckBoxChangedListener(this);

        this.ivjBillCardPanel.setTatolRowShow(true);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjBillCardPanel;
  }

  protected String getBillCodeKeyName()
  {
    if (null == this.sBillCodeKeyName)
      this.sBillCodeKeyName = "";
    return this.sBillCodeKeyName;
  }

  public String getBillID()
  {
    return null;
  }

  public BillListPanel getBillListPanel()
  {
    if (this.ivjBillListPane == null) {
      try {
        this.ivjBillListPane = new DMBillListPanel();
        this.ivjBillListPane.setName("BillListPanel");

        this.ivjBillListPane.setBillType(getBillType());

        this.ivjBillListPane.setCorp(getCorpPrimaryKey());

        this.ivjBillListPane.setOperator(getOperator());

 //      this.ivjBillListPane.addHeadEditListener(new BillUI.ListHeadListener(this));
        //edit by yqq 2017-01-12
        this.ivjBillListPane.addHeadEditListener(new BillUI.ListHeadListener());


        this.ivjBillListPane.updateUI();
      }
      catch (Throwable ivjExc) {
        handleException(ivjExc);
      }
    }
    return this.ivjBillListPane;
  }

  public String getBillType()
  {
    return this.m_BillTypeCode;
  }

  protected InvVO getBodyInvVOFromPanel(int row, int col, DMBillCardPanel dmbcp)
  {
    try
    {
      DMDataVO[] bodyVOs = (DMDataVO[])(DMDataVO[])dmbcp.getBillValueVO(DMVO.class.getName(), DMDataVO.class.getName(), DMDataVO.class.getName()).getChildrenVO();

      if ((null == bodyVOs) || (bodyVOs.length == 0)) {
        return null;
      }
      FreeVO fvo = new FreeVO();
      bodyVOs[row].translateToOtherVO(fvo);
      InvVO invvo = getInvInfo((String)bodyVOs[row].getAttributeValue("pkinv"), (String)bodyVOs[row].getAttributeValue("pkassistmeasure"));

      bodyVOs[row].translateToOtherVO(invvo);
      invvo.setCinventoryid((String)bodyVOs[row].getAttributeValue("pkinv"));
      invvo.setCinventorycode((String)bodyVOs[row].getAttributeValue("vinvcode"));
      invvo.setCastunitid((String)bodyVOs[row].getAttributeValue("pkassistmeasure"));
      invvo.setInvname((String)bodyVOs[row].getAttributeValue("vinvname"));
      invvo.setMeasdocname((String)bodyVOs[row].getAttributeValue("vunit"));
      fvo.setCinventoryid(invvo.getCinventoryid());
      invvo.setFreeItemVO(fvo);

      return invvo;
    } catch (Exception e) {
    }
    return null;
  }

  protected void getBodyLotRefbyHand(String ColName, int iSelrow, DMBillCardPanel dmbcp)
  {
    if (ColName == null) {
      return;
    }
    String sbatchcode = (String)dmbcp.getBodyValueAt(iSelrow, ColName);

    if (((sbatchcode == null) || (sbatchcode.trim().length() <= 0)) && (getLotNumbRefPane().isClicked())) {
      return;
    }
    boolean isLotRight = getLotNumbRefPane().checkData();

    if (!isLotRight) {
      dmbcp.setBodyValueAt("", iSelrow, ColName);
      getLotNumbRefPane().setValue("");
    }
  }

  public ButtonObject getCancelAuditButton()
  {
    return this.boCancelAudit;
  }

  public ButtonObject getCancelButton()
  {
    return this.boCancel;
  }

  protected String getCapacityUnit()
  {
    return this.m_currentCapacityUnit;
  }

  public ButtonObject getCopyLineButton()
  {
    return this.boCopyLine;
  }

  public String getCorpID()
  {
    return this.m_sCorpID;
  }

  public String getCorpName()
  {
    return this.m_sCorpName;
  }

  protected String getCorpPrimaryKey()
  {
    if (null != getClientEnvironment().getCorporation()) {
      return getClientEnvironment().getCorporation().getPrimaryKey();
    }
    return null;
  }

  public DefVO[] getDefVOsB()
  {
    if (this.m_defvosb == null);
    return this.m_defvosb;
  }

  public DefVO[] getDefVOsH()
  {
    if (this.m_defvosh == null);
    return this.m_defvosh;
  }

  public ButtonObject getDelButton()
  {
    return this.boDel;
  }

  public String getDelivOrgCode()
  {
    return this.m_delivOrgCode;
  }

  public String getDelivOrgName()
  {
    return this.m_delivOrgName;
  }

  public boolean getDelivOrgNoShow()
  {
    return this.m_bDelivOrgNoShow;
  }

  public String getDelivOrgPK()
  {
    return this.m_delivOrgPK;
  }

  protected int getDelivSequence()
  {
    return this.m_delivsequence;
  }

  public ButtonObject getDelLineButton()
  {
    return this.boDelLine;
  }

  public ButtonObject getDocumentButton()
  {
    return this.boDocument;
  }

  public ButtonObject getEditButton()
  {
    return this.boEdit;
  }

  public int getEditFlag()
  {
    return this.m_editFlag;
  }

  public ButtonObject getEndButton()
  {
    return this.boEnd;
  }

  protected String[] getFormulas(BillItem[] billitems)
  {
    if ((billitems == null) || (billitems.length == 0))
      return null;
    ArrayList aryListFormulas = new ArrayList();
    String[] formulas = null;
    BillItem item = null;
    for (int i = 0; i < billitems.length; i++) {
      item = billitems[i];
      formulas = item.getLoadFormula();
      if (formulas != null) {
        for (int j = 0; j < formulas.length; j++) {
          aryListFormulas.add(formulas[j]);
        }
      }
    }
    String[] formulasAll = null;
    if ((aryListFormulas != null) && (aryListFormulas.size() > 0)) {
      formulasAll = new String[aryListFormulas.size()];

      for (int i = 0; i < aryListFormulas.size(); i++) {
        formulasAll[i] = ((String)aryListFormulas.get(i));
      }
    }
    return formulasAll;
  }

  protected FreeItemRefPane getFreeItemRefPane()
  {
    if (null == this.m_firpFreeItemRefPane)
      this.m_firpFreeItemRefPane = new FreeItemRefPane();
    this.m_firpFreeItemRefPane.setName("vfree0");
    return this.m_firpFreeItemRefPane;
  }

  protected FreeItemRefPane getFreeItemRefPaneHead()
  {
    if (null == this.m_firpFreeItemRefPaneHead)
      this.m_firpFreeItemRefPaneHead = new FreeItemRefPane();
    this.m_firpFreeItemRefPaneHead.setName("vfree0");
    return this.m_firpFreeItemRefPaneHead;
  }

  public String getGroupID()
  {
    return getClientEnvironment().getGroupId();
  }

  protected InvVO getHeaderInvVOFromPanel(DMBillCardPanel dmbcp)
  {
    try
    {
      DMDataVO headerVO = (DMDataVO)dmbcp.getBillValueVO(DMVO.class.getName(), DMDataVO.class.getName(), DMDataVO.class.getName()).getParentVO();

      FreeVO fvo = new FreeVO();
      headerVO.translateToOtherVO(fvo);
      InvVO invvo = getInvInfo((String)headerVO.getAttributeValue("pkinv"), (String)headerVO.getAttributeValue("pkassistmeasure"));

      headerVO.translateToOtherVO(invvo);
      invvo.setCinventoryid((String)headerVO.getAttributeValue("pkinv"));
      invvo.setCinventorycode((String)headerVO.getAttributeValue("vinvcode"));
      invvo.setInvname((String)headerVO.getAttributeValue("vinvname"));
      invvo.setCastunitid((String)headerVO.getAttributeValue("pkassistmeasure"));
      invvo.setMeasdocname((String)headerVO.getAttributeValue("vunit"));
      fvo.setCinventoryid(invvo.getCinventoryid());
      invvo.setFreeItemVO(fvo);

      return invvo;
    } catch (Exception e) {
    }
    return null;
  }

  protected void getHeaderLotRefbyHand(String ColName, DMBillCardPanel dmbcp)
  {
    if (ColName == null) {
      return;
    }
    if (null == dmbcp.getHeadItem(ColName))
      return;
    String sbatchcode = ((LotNumbRefPane)dmbcp.getHeadItem(ColName).getComponent()).getText();

    if (((sbatchcode == null) || (sbatchcode.trim().length() <= 0)) && (getLotNumbRefPaneHead().isClicked())) {
      return;
    }
    boolean isLotRight = getLotNumbRefPaneHead().checkData();

    if (!isLotRight) {
      dmbcp.getHeadItem(ColName).setValue("");
      getLotNumbRefPaneHead().setValue("");
    }
  }

  public String getHeaderVOName()
  {
    return this.m_sHeaderVOName;
  }

  private String getInvAstUomKey(String sInv, String sAstID)
  {
    return "" + sInv + "&" + sAstID;
  }

  public InvVO getInvInfo(String sInv, String sAstID)
  {
    InvVO voInv = null;
    try {
      if ((sInv != null) && (sInv.trim().length() > 0)) {
        if (this.m_invht == null)
          this.m_invht = new Hashtable();
        String sKey = getInvAstUomKey(sInv, sAstID);

        if (this.m_invht.containsKey(sKey)) {
          voInv = (InvVO)this.m_invht.get(sKey);
        } else {
          InvVO[] invs = new InvVO[1];

          invs[0] = InvMeasInfo.getInvInfo(sInv, sAstID);

          if ((invs != null) && (invs.length > 0) && (invs[0] != null)) {
            putInvInfo(invs, new String[] { sInv }, new String[] { sAstID });
            voInv = invs[0];
          }
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace();
      showErrorMessage(e.getMessage());
    }
    return voInv;
  }

  public InvVO[] getInvInfo(String[] saInv, String[] saAstID)
  {
    InvVO[] voaInv = null;
    try {
      if (this.m_invht == null)
        this.m_invht = new Hashtable();
      String sKey = null; String sTempAstID = null;
      Vector vInv = new Vector();
      Vector vAst = new Vector();
      if ((saInv != null) && (saInv.length > 0)) {
        for (int i = 0; i < saInv.length; i++) {
          if (saInv[i] == null)
            continue;
          if ((saAstID != null) && (saAstID.length > i))
            sTempAstID = saAstID[i];
          else
            sTempAstID = null;
          sKey = getInvAstUomKey(saInv[i], sTempAstID);

          if (!this.m_invht.containsKey(sKey)) {
            vInv.addElement(saInv[i]);
            vAst.addElement(sTempAstID);
          }

        }

        if (vInv.size() > 0) {
          String[] saMyInvID = new String[vInv.size()];
          vInv.copyInto(saMyInvID);
          String[] saMyAstID = new String[vInv.size()];
          vAst.copyInto(saMyAstID);

          InvVO[] voaMyInv = new InvVO[saMyInvID.length];

          voaMyInv = InvMeasInfo.getInvInfo(saMyInvID, saMyAstID);

          if ((voaMyInv != null) && (voaMyInv.length > 0) && (voaMyInv[0] != null))
          {
            putInvInfo(voaMyInv, saMyInvID, saMyAstID);
          }
        }

        voaInv = new InvVO[saInv.length];
        for (int i = 0; i < saInv.length; i++) {
          if (saInv[i] == null)
            continue;
          if ((saAstID != null) && (saAstID.length > i))
            sTempAstID = saAstID[i];
          else
            sTempAstID = null;
          sKey = getInvAstUomKey(saInv[i], sTempAstID);

          if (this.m_invht.containsKey(sKey))
            voaInv[i] = ((InvVO)this.m_invht.get(sKey));
          else {
            SCMEnv.info("qry hash err,no data found.");
          }
        }
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      showErrorMessage(e.getMessage());
    }
    return voaInv;
  }

  public ArrayList getItemFormulaBody()
  {
    return null;
  }

  public ArrayList getItemFormulaHead()
  {
    return null;
  }

  protected Hashtable getItemKeyBody()
  {
    if ((this.m_htbBodyItemkey == null) || (this.m_htbBodyItemkey.size() == 0)) {
      this.m_htbBodyItemkey = new Hashtable();
      BillItem[] billItems;
      
      //edit by yqq 2017-01-12
  //    BillItem[] billItems;
      if (getBillCardPanel() != null) {
        billItems = getBillCardPanel().getBodyItems();
      }
      else {
        billItems = getBillListPanel().getBodyBillModel().getBodyItems();
      }

      if ((billItems != null) && (billItems.length > 0)) {
        int iLen = billItems.length;
        BillItem billItem = null;
        String sKey = null;
        for (int i = 0; i < iLen; i++) {
          sKey = billItems[i].getKey();
          if ((sKey != null) && (!this.m_htbBodyItemkey.containsKey(sKey))) {
            this.m_htbBodyItemkey.put(sKey, sKey);
          }
        }
      }
    }
    return this.m_htbBodyItemkey;
  }

  protected Hashtable getItemKeyHead()
  {
    if ((this.m_htbHeadItemkey == null) || (this.m_htbHeadItemkey.size() == 0)) {
      this.m_htbHeadItemkey = new Hashtable();
      BillItem[] billItems;
      //edit by yqq 2017-01-12
   //   BillItem[] billItems;
      if (getBillCardPanel() != null) {
        billItems = (BillItem[])(BillItem[])FormulaTools.combineArray(getBillCardPanel().getHeadItems(), getBillCardPanel().getTailItems());
      }
      else
      {
        billItems = getBillListPanel().getHeadBillModel().getBodyItems();
      }

      if ((billItems != null) && (billItems.length > 0)) {
        int iLen = billItems.length;
        BillItem billItem = null;
        String sKey = null;
        for (int i = 0; i < iLen; i++) {
          sKey = billItems[i].getKey();
          if ((sKey != null) && (!this.m_htbHeadItemkey.containsKey(sKey))) {
            this.m_htbHeadItemkey.put(sKey, sKey);
          }
        }
      }
    }

    return this.m_htbHeadItemkey;
  }

  public String getItemVOName()
  {
    return this.m_sItemVOName;
  }

  public ButtonObject getJoinQueryButton()
  {
    return this.boOrderQuery;
  }

  public ButtonObject getLineButton()
  {
    return this.boLine;
  }

  public UFDate getLogDate()
  {
    return this.m_LogDate;
  }

  protected LotNumbRefPane getLotNumbRefPane()
  {
    if (null == this.m_lnrpLotNumbRefPane) {
      this.m_lnrpLotNumbRefPane = new LotNumbRefPane();
      this.m_lnrpLotNumbRefPane.setName("vbatchcode");
      this.m_lnrpLotNumbRefPane.setRefModel(new LotRefModel());
    }
    return this.m_lnrpLotNumbRefPane;
  }

  protected LotNumbRefPane getLotNumbRefPaneHead()
  {
    if (null == this.m_lnrpLotNumbRefPaneHead) {
      this.m_lnrpLotNumbRefPaneHead = new LotNumbRefPane();
      this.m_lnrpLotNumbRefPaneHead.setName("vbatchcode");
      this.m_lnrpLotNumbRefPaneHead.setRefModel(new LotRefModel());
    }

    return this.m_lnrpLotNumbRefPaneHead;
  }

  protected UFDouble getNewValueByHSL(Object oldDesNum, Object oldDivNum, Object newDivNum)
  {
    if ((null == oldDesNum) || (oldDesNum.toString().trim().length() == 0) || (null == oldDivNum) || (oldDivNum.toString().trim().length() == 0) || (null == newDivNum) || (newDivNum.toString().trim().length() == 0))
    {
      return null;
    }
    if (new UFDouble(oldDesNum.toString()).doubleValue() == 0.0D)
      return null;
    if (new UFDouble(oldDivNum.toString()).doubleValue() == 0.0D)
      return null;
    if (new UFDouble(newDivNum.toString()).doubleValue() == 0.0D) {
      return null;
    }
    UFDouble newDesNum = new UFDouble(newDivNum.toString()).div(new UFDouble(oldDivNum.toString())).multiply(new UFDouble(oldDesNum.toString()));

    return newDesNum;
  }

  protected UFDouble getNewValueByHSL(Object oldDesNum, Object oldDivNum, Object newDivNum, Integer precision)
  {
    UFDouble newDesNum = getNewValueByHSL(oldDesNum, oldDivNum, newDivNum);

    if (null != newDesNum) {
      newDesNum = new UFDouble(newDesNum.doubleValue(), -1 * precision.intValue());
    }

    return newDesNum;
  }

  public String getNodeCode()
  {
    return this.m_sNodeCode;
  }

  protected String[] getOldFormulasBody()
  {
    if (this.m_aryFormulasBody == null)
    {
      BillItem[] billItems;
      //edit by yqq 2017-01-12
    //  BillItem[] billItems;
      if (getBillCardPanel() != null) {
        billItems = getBillCardPanel().getBodyItems();
      }
      else {
        billItems = getBillListPanel().getBodyBillModel().getBodyItems();
      }

      this.m_aryFormulasBody = getFormulas(billItems);
    }
    return this.m_aryFormulasBody;
  }

  protected String[] getOldFormulasHead()
  {
    if (this.m_aryFormulasHead == null)
    {
      BillItem[] billItems;
      //edit by yqq 2017-01-12
  //    BillItem[] billItems;
      if (getBillCardPanel() != null) {
        billItems = (BillItem[])(BillItem[])FormulaTools.combineArray(getBillCardPanel().getHeadItems(), getBillCardPanel().getTailItems());
      }
      else
      {
        billItems = getBillListPanel().getHeadBillModel().getBodyItems();
      }

      this.m_aryFormulasHead = getFormulas(billItems);
    }
    return this.m_aryFormulasHead;
  }

  public String getOperator()
  {
    if (null != getClientEnvironment().getUser()) {
      return getClientEnvironment().getUser().getPrimaryKey();
    }
    return null;
  }

  protected UFDouble getParaDouble(Hashtable h, String key) {
    String str = (String)h.get(key);
    if (str == null)
      return new UFDouble(0.0D);
    return new UFDouble(str);
  }

  protected String getParaString(Hashtable h, String key)
  {
    String str = (String)h.get(key);
    if (str == null)
      return "";
    return str;
  }

  public ButtonObject getPasteLineButton()
  {
    return this.boPasteLine;
  }

  protected String getPrimaryKeyName()
  {
    if (null == this.sPrimaryKeyName)
      this.sPrimaryKeyName = "";
    return this.sPrimaryKeyName;
  }

  public ButtonObject getPrintButton()
  {
    return this.boPrint;
  }

  public ButtonObject getQueryButton()
  {
    return this.boQuery;
  }

  public DMQueryConditionDlg getQueryConditionDlg()
  {
    return this.queryConditionDlg;
  }

  public ButtonObject getSaveButton()
  {
    return this.boSave;
  }

  public String getShowState()
  {
    return this.m_strShowState;
  }

  public ButtonObject getSortButton()
  {
    return this.boSort;
  }

  public SourceBillFlowDlg getSourceDlg()
  {
    String sCurrBillID = getBillID();
    if ((sCurrBillID == null) || (sCurrBillID.trim().length() == 0)) {
      return null;
    }
    this.soureDlg = new SourceBillFlowDlg(this, getBillType(), sCurrBillID, getBillCardPanel().getBusiType(), getUserID(), getCorpID());

    return this.soureDlg;
  }

  protected String getStrCorpIDsOfDelivOrg()
  {
    String[] agentCorpIDs = (String[])(String[])ShowDelivOrg.getAgentCorpIDsofDelivOrg().toArray(new String[0]);
    String strAgentCorpIDs = getStringFromStrings(agentCorpIDs);
    return strAgentCorpIDs;
  }

  protected String getStringFromStrings(String[] strs)
  {
    String sWhere = "";

    if ((strs != null) && (strs.length > 0))
    {
      for (int i = 0; i < strs.length; i++) {
        sWhere = sWhere + " '" + strs[i] + "'";
        if (i != strs.length - 1) {
          sWhere = sWhere + " , ";
        }
      }
    }
    if (sWhere.trim().length() == 0) {
      return "''";
    }
    return sWhere;
  }

  public ButtonObject getSwithButton()
  {
    return this.boSwith;
  }

  public void getSystemPara()
  {
    try
    {
      String[] para = { "BD501", "BD502", "BD503", "BD505", "DM001", "DM002", "DM003", "DM004", "DM005", "DM006", "BD202", "BD203", "DM007", "DM008", "DM009", "DM010", "DM011", "DM012", "DM013", "DM014", "DM015", "DM016", "DM017" };

      Hashtable h = SysInitBO_Client.queryBatchParaValues(getCorpPrimaryKey(), para);

      String[] saParam = { "BD301" };

      ArrayList alAllParam = new ArrayList();

      ArrayList alParam = new ArrayList();
      alParam.add(getCorpPrimaryKey());
      alParam.add(saParam);
      alAllParam.add(alParam);

      alAllParam.add(getClientEnvironment().getUser().getPrimaryKey());

      Object oResult = DmHelper.queryInfo(new Integer(11), alAllParam);

      ArrayList alRetData = null;

      if (oResult != null)
      {
        alRetData = (ArrayList)oResult;
      }

      if (this.m_defvosh == null)
        this.m_defvosh = DefSetTool.getDefHead(getCorpID(), this.vUserdefCode);
      if (this.m_defvosb == null) {
        this.m_defvosb = DefSetTool.getDefBody(getCorpID(), this.vUserdefCode);
      }

      this.BD501 = getParaInt(h, "BD501");
      this.BD502 = getParaInt(h, "BD502");
      this.BD503 = getParaInt(h, "BD503");
      this.BD505 = getParaInt(h, "BD505");
      this.DM001 = getParaBoolean(h, "DM001");
      this.DM002 = getParaBoolean(h, "DM002");
      this.DM003 = getParaBoolean(h, "DM003");
      this.DM004 = getParaBoolean(h, "DM004");
      this.DM005 = getParaBoolean(h, "DM005");
      this.DM006 = getParaBoolean(h, "DM006");
      this.DM007 = getParaBoolean(h, "DM007");
      this.DM008 = getParaBoolean(h, "DM008");
      this.DM009 = getParaDouble(h, "DM009");
      this.DM010 = getParaBoolean(h, "DM010");
      this.DM011 = getParaBoolean(h, "DM011");
      this.DM012 = getParaBoolean(h, "DM012");
      this.DM013 = getParaBoolean(h, "DM013");
      this.DM014 = getParaString(h, "DM014");
      this.DM015 = getParaString(h, "DM015");
      this.DM016 = getParaBoolean(h, "DM016");
      this.DM017 = getParaString(h, "DM017");

      this.m_currentWeightUnit = getParaString(h, "BD203");

      this.m_currentCapacityUnit = getParaString(h, "BD202");

      if ((alRetData == null) || (alRetData.size() < 2)) {
        showErrorMessage(NCLangRes.getInstance().getStrByID("4014", "UPP4014-000034"));
        return;
      }

      String[] saParamValue = (String[])(String[])alRetData.get(0);
      if ((saParamValue != null) && (saParamValue.length >= saParam.length))
      {
        if (saParamValue[0] != null) {
          this.BD301 = new Integer(Integer.parseInt(saParamValue[0]));
        }
      }
      if ((this.BD501 == null) || (this.BD501.toString().trim().length() == 0)) {
        this.BD501 = new Integer(2);
      }
      if ((this.BD502 == null) || (this.BD502.toString().trim().length() == 0)) {
        this.BD502 = new Integer(2);
      }
      if ((this.BD503 == null) || (this.BD503.toString().trim().length() == 0)) {
        this.BD503 = new Integer(2);
      }
      if ((this.BD505 == null) || (this.BD505.toString().trim().length() == 0)) {
        this.BD505 = new Integer(2);
      }
      if ((this.BD301 == null) || (this.BD301.toString().trim().length() == 0))
        this.BD301 = new Integer(2);
    }
    catch (Exception e)
    {
      SCMEnv.info("系统参数获取失败:" + e.getMessage());
      e.printStackTrace();
      showErrorMessage(e.getMessage());
    }
  }

  public String getTitle()
  {
    return getBillCardPanel().getTitle();
  }

  public String getUserID()
  {
    return this.m_sUserID;
  }

  public String getUserName()
  {
    return this.m_sUserName;
  }

  public String getVOName()
  {
    return this.m_sVOName;
  }

  protected String getWeightUnit()
  {
    return this.m_currentWeightUnit;
  }

  protected void getWhInfo(String[] sWh)
  {
    try
    {
      WhVO[] whvos = new WhVO[sWh.length];

      for (int i = 0; i < sWh.length; i++) {
        whvos[i] = this.iibyfInvoInfoBYFormula.getWHVO(sWh[i], false);
        if ((null != whvos[i]) && (whvos[i].getCwarehouseid() != null)) {
          this.m_whht.put(whvos[i].getCwarehouseid(), whvos[i]);
        }
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  public void init()
  {
    onShowDelivOrg();
  }

  protected void initBodyFreeItem(int row, int col, DMBillCardPanel dmbcp)
  {
    try
    {
      InvVO invvo = getBodyInvVOFromPanel(row, col, dmbcp);

      getFreeItemRefPane().setFreeItemParam(invvo);
      getFreeItemRefPane().setEditable(true);
    }
    catch (Exception e)
    {
    }
  }

  protected void initBodyLot(int row, int col, DMBillCardPanel dmbcp)
  {
    try
    {
      InvVO invvo = getBodyInvVOFromPanel(row, col, dmbcp);

      WhVO whvo = queryWhInfo((String)dmbcp.getBodyValueAt(row, "pksendstock"));
      getLotNumbRefPane().setParameter(whvo, invvo);

      getLotNumbRefPane().setValue((String)dmbcp.getBodyValueAt(row, "vbatchcode"));
    }
    catch (Exception e)
    {
    }
  }

  protected void initButtons()
  {
    this.boBill = new ButtonObject(NCLangRes.getInstance().getStrByID("40140408", "UPT40140408-000063"), NCLangRes.getInstance().getStrByID("40140408", "UPT40140408-000063"), 1, "单据控制");

    this.boDel = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000039"), NCLangRes.getInstance().getStrByID("4014", "UPP4014-000024"), 1, "删除");

    this.boSort = new ButtonObject(NCLangRes.getInstance().getStrByID("40140602", "UPT40140602-000016"), NCLangRes.getInstance().getStrByID("4014", "UPP4014-000025"), 1, "排序");

    this.boAudit = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000027"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000238"), 1, "审批");

    this.boCancelAudit = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000028"), NCLangRes.getInstance().getStrByID("4014", "UPP4014-000026"), 1, "弃审");

    this.boEnd = new ButtonObject(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000119"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000120"), 1, "关闭");

    this.boPrintPreview = new ButtonObject(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000305"), NCLangRes.getInstance().getStrByID("4014", "UPP4014-000027"), 1, "预览");

    this.boBill.removeAllChildren();

    initFixSubMenuButton();
    setButtons(getBillButtons());
  }

  protected void initCurrency()
  {
  }

  protected void initHeaderFreeItem(DMBillCardPanel dmbcp)
  {
    try
    {
      InvVO invvo = getHeaderInvVOFromPanel(dmbcp);

      getFreeItemRefPaneHead().setFreeItemParam(invvo);
      getFreeItemRefPaneHead().setEditable(false);
    }
    catch (Exception e)
    {
    }
  }

  protected void initHeaderLot(DMBillCardPanel dmbcp)
  {
    try
    {
      InvVO invvo = getHeaderInvVOFromPanel(dmbcp);

      if (null != dmbcp.getHeadItem("pksendstore")) {
        WhVO whvo = queryWhInfo(dmbcp.getHeadItem("pksendstore").getValue());
        getLotNumbRefPaneHead().setParameter(whvo, invvo);
      }

      if (null != dmbcp.getHeadItem("vbatchcode"))
        getLotNumbRefPaneHead().setValue(dmbcp.getHeadItem("vbatchcode").getValue());
    }
    catch (Exception e)
    {
    }
  }

  public void initialize()
  {
    setCorpID(getCorpPrimaryKey());
    if (null != getClientEnvironment().getCorporation()) {
      setCorpName(getClientEnvironment().getCorporation().getUnitname());
    }
    if (null != getClientEnvironment().getUser()) {
      setUserID(getClientEnvironment().getUser().getPrimaryKey());
      setUserName(getClientEnvironment().getUser().getUserName());
    }

    if (getClientEnvironment().getDate() != null) {
      setLogDate(getClientEnvironment().getDate());
    }
    init();

    getSystemPara();

    super.initialize();
  }

  protected void initListHeaderComboBox()
  {
  }

  public void initRef(int rownow, int colnow, DMBillCardPanel bcp)
  {
    String sItemKey = bcp.getBodyPanel().getBodyKeyByCol(colnow).trim();

    if (!bcp.getBillData().getBodyItem(sItemKey).isEnabled())
      return;
  }

  public boolean isListHeaderMultiSelected(ButtonObject bo, ButtonObject[] noHintBos)
  {
    if ((bo == this.boPrint) || (bo == this.boPreview) || (bo == this.boPrintPreview) || (bo == this.boAdd) || (bo == this.boQuery))
      return false;
    if (noHintBos != null) {
      for (int i = 0; i < noHintBos.length; i++) {
        if (bo == noHintBos[i])
          return false;
      }
    }
    if ((getBillListPanel().getHeadTable().getSelectedRows().length > 1) && (getShowState().equals(DMBillStatus.List))) {
      MessageDialog.showWarningDlg(this, NCLangRes.getInstance().getStrByID("4014", "UPPSCMCommon-000132"), NCLangRes.getInstance().getStrByID("4014", "UPP4014-000055"));

      return true;
    }

    return false;
  }

  protected boolean isOutButtonCanClicked(boolean IsPlanOrDeliv, String SendModeKey, DMDataVO HeadVO)
  {
    Object oSendMode = HeadVO.getAttributeValue(SendModeKey);
    if (null == oSendMode)
      return false;
    if (IsPlanOrDeliv)
    {
      Object[][] oValue = CacheTool.getMultiColValue("bd_sendtype", "pk_sendtype", new String[] { "transporttype" }, new String[] { (String)oSendMode });

      if ((null != oValue) && (oValue.length == 1)) {
        int TransType = Integer.parseInt(oValue[0][0].toString());
        if (TransType == 2) {
          return true;
        }

        return getDelivSequence() != 0;
      }

    }
    else
    {
      return getDelivSequence() == 0;
    }

    return false;
  }

  public void listHeadRowChange(BillEditEvent e)
  {
    int rownow = e.getRow();
    if (rownow < 0) {
      return;
    }
    if (rownow > getAllVOs().size() - 1)
    {
      return;
    }

    this.m_iLastSelListHeadRow = rownow;

    getBillListPanel().getBodyBillModel().clearBodyData();

    DMDataVO[] ddvos = (DMDataVO[])(DMDataVO[])((DMVO)getAllVOs().get(this.m_iLastSelListHeadRow)).getChildrenVO();

    getBillListPanel().getBodyBillModel().setBodyDataVO(ddvos);
    getBillListPanel().getBodyBillModel().execLoadFormula();
  }

  public void loadCardTemplet()
  {
    showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000136"));

    BillData bd = new BillData(this.ivjBillCardPanel.getDefaultTemplet(getBillType(), null, getUserID(), getCorpID()));

    setCardPanel(bd);

    getBillCardPanel().setBillData(bd);

    setInputLimit();

    initBodyComboBox();

    initState();

    if (getBillCardPanel().getBillData().getBodyTableCodes() != null) {
      getBillCardPanel().addBodyTotalListener(this);
    }
    if (null != getBillCardPanel().getBillData().getBodyTableCodes()) {
      getBillCardPanel().getBillTable().getSelectionModel().addListSelectionListener(this);
      ((DMBillCardPanel)getBillCardPanel()).addTableColumnModelListener(this);
      getBillCardPanel().getBillModel().addTableModelListener(this);
    }

    showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000176"));
  }

  public void loadListTemplet()
  {
    showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000135"));

    BillListData bd = new BillListData(getBillListPanel().getDefaultTemplet(getBillType(), getBillListPanel().getBusiType(), getBillListPanel().getOperator(), getBillListPanel().getCorp()));

    setListPanelByPara(bd);

    changeBillListDataByUserDef(bd);

    getBillListPanel().setListData(bd);

    getBillListPanel().getHeadBillModel().setRowSort(false);

    getBillListPanel().getChildListPanel().setTatolRowShow(true);

    initListHeaderComboBox();

    showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000147"));
  }

  public void mouseClicked(MouseEvent e)
  {
    if ((e.getSource() == getBillListPanel().getHeadTable()) && 
      (e.getClickCount() == 2) && (getBillListPanel().getHeadTable().getSelectedRow() != -1)) {
      this.m_iLastSelListHeadRow = getBillListPanel().getHeadTable().getSelectedRow();
      onSwith();
    }
  }

  public void mouseEntered(MouseEvent e)
  {
  }

  public void mouseExited(MouseEvent e)
  {
  }

  public void mousePressed(MouseEvent e)
  {
  }

  public void mouseReleased(MouseEvent e)
  {
  }

  public void onButtonClicked(ButtonObject bo)
  {
    if (bo == this.boDel)
      onDel();
    else if (bo == this.boSort)
      onSort((DMBillCardPanel)getBillCardPanel(), null);
    else if (bo == this.boPrintPreview)
      onPrintPreview();
    else if (bo == this.boAddLine)
      onAddLine();
    else if (bo == this.boDelLine)
      onDelLine();
    else if (bo == this.boCopyLine)
      onCopyLine();
    else if (bo == this.boPasteLine)
      onPasteLine();
    else if (bo == this.boSave)
      onSave();
    else if (bo == this.boCancel)
      onCancel();
    else if (bo == this.boEdit)
      onEdit();
    else if (bo == this.boSwith)
      onSwith();
    else if (bo == this.boPrint)
      onPrint();
    else if (bo == this.boPreview)
      onPreview();
    else if (bo == this.boFind)
      onFind();
    else if (bo == this.boFirst)
      onFrist();
    else if (bo == this.boLast)
      onLast();
    else if (bo == this.boPre)
      onPre();
    else if (bo == this.boNext)
      onNext();
    else if (bo == this.boQuery)
      onQuery();
    else if (bo == this.boOrderQuery)
      onOrderQuery();
    else if (bo == this.boDocument)
      onDocument();
    else if (bo.getParent() == this.boBusiType)
      onBusiType(bo);
    else if (bo.getParent() == this.boAdd)
      onNew(bo);
    else if (bo.getParent() == this.boAction)
      onAction(bo);
    else if (bo.getParent() == this.boAssistant)
      onAssistant(bo);
  }

  public void onCardPrint(BillCardPanel bcp)
  {
    ((DMBillCardPanel)bcp).setBillType(getBillType());

    ((DMBillCardPanel)bcp).setCorp(getCorpPrimaryKey());

    ((DMBillCardPanel)bcp).setOperator(getOperator());
    ((DMBillCardPanel)bcp).setNodeCode(getNodeCode());
    ((DMBillCardPanel)bcp).onCardPrint();
  }

  public void onCardPrintPreview(BillCardPanel bcp)
  {
    ((DMBillCardPanel)bcp).setBillType(getBillType());

    ((DMBillCardPanel)bcp).setCorp(getCorpPrimaryKey());

    ((DMBillCardPanel)bcp).setOperator(getOperator());
    ((DMBillCardPanel)bcp).setNodeCode(getNodeCode());
    ((DMBillCardPanel)bcp).onCardPreview();
  }

  public void onDel()
  {
  }

  public void onDocument()
  {
    String[] sIDs = null;
    String[] sCodes = null;
    if (this.m_strShowState.equals(DMBillStatus.Card)) {
      if (getBillCardPanel().getBillData().getEnabled()) {
        showWarningMessage(NCLangRes.getInstance().getStrByID("4014", "UPPSCMCommon-000016"));
        return;
      }
      sIDs = new String[1];
      sCodes = new String[1];
      sIDs[0] = getBillCardPanel().getHeadItem(getPrimaryKeyName()).getValue();
      sCodes[0] = getBillCardPanel().getHeadItem(getBillCodeKeyName()).getValue();
      if ((null == sIDs[0]) || (sIDs[0].trim().length() == 0) || (null == sCodes[0]) || (sCodes[0].trim().length() == 0)) {
        showWarningMessage(NCLangRes.getInstance().getStrByID("4014", "UPP4014-000018"));
        return;
      }
    }
    else if (this.m_strShowState.equals(DMBillStatus.List)) {
      int[] iRows = getBillListPanel().getHeadTable().getSelectedRows();
      if (iRows.length == 0) {
        showWarningMessage(NCLangRes.getInstance().getStrByID("4014", "UPPSCMCommon-000275"));
        return;
      }
      sIDs = new String[iRows.length];
      sCodes = new String[iRows.length];
      for (int i = 0; i < iRows.length; i++) {
        sIDs[i] = getBillListPanel().getHeadBillModel().getValueAt(iRows[i], getPrimaryKeyName()).toString();
        sCodes[i] = getBillListPanel().getHeadBillModel().getValueAt(iRows[i], getBillCodeKeyName()).toString();
      }
    }
    DocumentManager.showDM(this, sIDs, sCodes);
  }

  public void onFind()
  {
    OrientDialog dlgOrient = new OrientDialog(this, getBillListPanel().getHeadBillModel(), getBillListPanel().getBillListData().getHeadItems(), getBillListPanel().getHeadTable());

    dlgOrient.showModal();
  }

  public void onListPrint(BillCardPanel bcp, ArrayList alvos)
  {
    ((DMBillCardPanel)bcp).setBillType(getBillType());

    ((DMBillCardPanel)bcp).setCorp(getCorpPrimaryKey());

    ((DMBillCardPanel)bcp).setOperator(getOperator());
    ((DMBillCardPanel)bcp).setNodeCode(getNodeCode());
    ((DMBillCardPanel)bcp).onListPrint(alvos);
  }

  public void onListPrintPreview(BillCardPanel bcp, ArrayList alvos)
  {
    ((DMBillCardPanel)bcp).setBillType(getBillType());

    ((DMBillCardPanel)bcp).setCorp(getCorpPrimaryKey());

    ((DMBillCardPanel)bcp).setOperator(getOperator());
    ((DMBillCardPanel)bcp).setNodeCode(getNodeCode());
    ((DMBillCardPanel)bcp).onListPreview(alvos);
  }

  public void onOrderQuery()
  {
    SourceBillFlowDlg sourceDlg = getSourceDlg();
    if (sourceDlg == null) {
      showWarningMessage(NCLangRes.getInstance().getStrByID("4014", "UPP4014-000019"));
      return;
    }
    sourceDlg.showModal();
  }

  public void onPrint()
  {
    if (this.m_strShowState.equals(DMBillStatus.Card))
      onCardPrint(getBillCardPanel());
    if (this.m_strShowState.equals(DMBillStatus.List))
      onListPrint(getBillCardPanel(), getAllVOs());
  }

  public void onPrintPreview()
  {
    if (this.m_strShowState.equals(DMBillStatus.Card))
      onCardPrintPreview(getBillCardPanel());
    if (this.m_strShowState.equals(DMBillStatus.List))
      onListPrintPreview(getBillCardPanel(), getAllVOs());
  }

  public void onQuery()
  {
    getQueryConditionDlg().hideNormal();
    if (getQueryConditionDlg().showModal() == 1)
    {
      ConditionVO[] voCons = getQueryConditionDlg().getConditionVO();
      voCons = ConvertQueryCondition.getConvertedVO(voCons, null);

      getQueryConditionDlg().checkCondition(voCons);

      afterQuery(voCons);
    }
  }

  public void onShowDelivOrg()
  {
    Container parent = ClientEnvironment.getInstance().getDesktopApplet();

    DelivorgRef delivorgRef = new DelivorgRef(getCorpPrimaryKey(), parent);
    delivorgRef.setAgentCorp(getCorpPrimaryKey());

    if (null != ShowDelivOrg.getDelivOrgPK()) {
      delivorgRef.setPK(ShowDelivOrg.getDelivOrgPK());
    }

    if ((ShowDelivOrg.getDelivOrgPK() == null) || (ShowDelivOrg.getDelivOrgPK().trim().length() == 0))
    {
      if (!getDelivOrgNoShow()) {
        int iReturnShowCode = delivorgRef.getRef().showModal();
        if (iReturnShowCode == 1) {
          if (delivorgRef.getRefModel().getPkValue() == null)
          {
            throw new Error(NCLangRes.getInstance().getStrByID("4014", "UPP4014-000036"));
          }

          setDelivOrgCode(delivorgRef.getRefModel().getRefCodeValue());
          setDelivOrgPK(delivorgRef.getRefModel().getPkValue());
          setDelivOrgName(delivorgRef.getRefModel().getRefNameValue());
          setDelivSequence(new Integer(delivorgRef.getRefModel().getValue("idelivsequence").toString()).intValue());
          try
          {
            ArrayList list = DmHelper.queryCorpIDsByDelivOrgID(getDelivOrgPK());
            if (list.size() == 0) {
              String message = NCLangRes.getInstance().getStrByID("4014", "UPP4014-000038");

              throw new Error(message);
            }
            setAgentCorpIDsofDelivOrg(list);

            ShowDelivOrg.setDelivOrgPK(getDelivOrgPK());

            ShowDelivOrg.setDelivOrgCode(getDelivOrgCode());

            ShowDelivOrg.setDelivOrgName(getDelivOrgName());

            ShowDelivOrg.setAgentCorpIDsofDelivOrg(getAgentCorpIDsofDelivOrg());

            ShowDelivOrg.setBelongCorpIDofDelivOrg(getCorpPrimaryKey());

            ShowDelivOrg.setDelivSequence(getDelivSequence());
          }
          catch (Exception e)
          {
            throw new Error(NCLangRes.getInstance().getStrByID("4014", "UPP4014-000038"));
          }

        }
        else
        {
          throw new Error(NCLangRes.getInstance().getStrByID("4014", "UPP4014-000036"));
        }
      }
    }
    else {
      setDelivOrgCode(ShowDelivOrg.getDelivOrgCode());

      setDelivOrgPK(ShowDelivOrg.getDelivOrgPK());

      setDelivOrgName(ShowDelivOrg.getDelivOrgName());

      setAgentCorpIDsofDelivOrg(ShowDelivOrg.getAgentCorpIDsofDelivOrg());

      setDelivSequence(ShowDelivOrg.getDelivSequence());
    }
  }

  public void onSort(DMBillCardPanel dbcp, DMListData[] arylistdataMustSelect)
  {
    dbcp.setArylistdataMustSelect(arylistdataMustSelect);
    dbcp.onSort();
  }

  public void onSwith()
  {
    if (this.m_strShowState.equals(DMBillStatus.Card)) {
      this.m_strShowState = DMBillStatus.List;
    }
    else {
      this.m_strShowState = DMBillStatus.Card;
      refreshCardTable();
    }
    setButtons(getBillButtons());
    switchInterface();
  }

  private void putInvInfo(InvVO[] invs, String[] saInv, String[] saAstID)
  {
    if (this.m_invht == null)
      this.m_invht = new Hashtable();
    String sKey = null; String sTempAstID = null;
    if (invs != null)
      for (int i = 0; i < invs.length; i++) {
        if ((invs[i] == null) || (saInv[i] == null))
          continue;
        if ((saAstID != null) && (saAstID.length > i))
          sTempAstID = saAstID[i];
        else
          sTempAstID = null;
        sKey = getInvAstUomKey(saInv[i], sTempAstID);

        if (this.m_invht.containsKey(sKey))
          this.m_invht.remove(sKey);
        if (sTempAstID == null) {
          invs[i].setHsl(null);
        }
        else
        {
          UFDouble ufdHsl = invs[i].getHsl();
          if ((null != ufdHsl) && (ufdHsl.toString().trim().length() != 0)) {
            ufdHsl.setScale(-this.BD503.intValue(), 4);
            invs[i].setHsl(ufdHsl);
          }
        }
        this.m_invht.put(sKey, invs[i]);
      }
  }

  protected WhVO queryWhInfo(String sWh)
  {
    if (null == sWh)
      return null;
    String[] sWhs = { sWh };
    WhVO whvo = queryWhInfo(sWhs)[0];
    return whvo;
  }

  protected WhVO[] queryWhInfo(String[] sWh)
  {
    WhVO[] whvos = new WhVO[sWh.length];
    try
    {
      Vector v = new Vector();
      for (int i = 0; i < sWh.length; i++) {
        if (null != sWh[i])
        {
          WhVO whvo;
          if (null != this.m_whht.get(sWh[i])) {
            whvo = (WhVO)this.m_whht.get(sWh[i]);
          }
          else {
            v.addElement(sWh[i]);
          }
        }
      }
      String[] sWhs = new String[v.size()];
      v.copyInto(sWhs);

      getWhInfo(sWhs);

      for (int i = 0; i < sWh.length; i++) {
        if ((null != sWh[i]) && (null != this.m_whht.get(sWh[i]))) {
          whvos[i] = ((WhVO)this.m_whht.get(sWh[i]));
        }
        else
          whvos[i] = null;
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return whvos;
  }

  public void refreshCardTable()
  {
    getBillCardPanel().resumeValue();
    if (getAllVOs().size() != 0) {
      if (this.m_iLastSelListHeadRow < 0)
        this.m_iLastSelListHeadRow = 0;
      getBillCardPanel().setBillValueVO((DMVO)getAllVOs().get(this.m_iLastSelListHeadRow));
    }

    getBillCardPanel().updateValue();
  }

  public void refreshListTable()
  {
    if ((null != getAllVOs()) && (getAllVOs().size() != 0)) {
      if (this.m_iLastSelListHeadRow < 0) {
        this.m_iLastSelListHeadRow = 0;
      }
      getBillCardPanel().setBillValueVO((DMVO)getAllVOs().get(this.m_iLastSelListHeadRow));
      DMDataVO[] ddvos = new DMDataVO[getAllVOs().size()];
      for (int i = 0; i < getAllVOs().size(); i++) {
        ddvos[i] = ((DMVO)getAllVOs().get(i)).getHeaderVO();
      }
      getBillListPanel().setHeaderValueVO(ddvos);
      getBillListPanel().setBodyValueVO(((DMVO)getAllVOs().get(this.m_iLastSelListHeadRow)).getBodyVOs());
    }
    else {
      getBillListPanel().getHeadTable().clearSelection();
      this.m_iLastSelListHeadRow = -1;
      getBillListPanel().getHeadBillModel().setBodyDataVO(null);
      getBillListPanel().getBodyBillModel().setBodyDataVO(null);
    }
  }

  public void setAgentCorpIDsofDelivOrg(ArrayList newAgentCorpIDsofDelivOrg)
  {
    this.alAgentCorpIDsofDelivOrg = newAgentCorpIDsofDelivOrg;
  }

  public void setAllVOs(ArrayList newAllVOs)
  {
    this.m_alAllVOs = newAllVOs;
  }

  public void setANumItemKeys(String[] newANumItemKeys)
  {
    this.m_itemANumKeys = new String[newANumItemKeys.length];
    for (int i = 0; i < newANumItemKeys.length; i++)
      this.m_itemANumKeys[i] = newANumItemKeys[i];
  }

  protected void setBillCardPanel(DMBillCardPanel dmBillCardPanel)
  {
    this.ivjBillCardPanel = dmBillCardPanel;
  }

  protected void setBillCodeKeyName(String newBillCodeKeyName)
  {
    this.sBillCodeKeyName = newBillCodeKeyName;
  }

  protected void setBillListPanel(DMBillListPanel dmBillListPanel)
  {
    this.ivjBillListPane = dmBillListPanel;
  }

  public void setBillTypeCode(String newBillTypeCode)
  {
    this.m_BillTypeCode = newBillTypeCode;
  }

  protected void setButton(ButtonObject bo, boolean b)
  {
    bo.setEnabled(b);
    updateButton(bo);
  }

  /** @deprecated */
  protected void setCapacityUnit(String newCapacityUnit)
  {
    this.m_currentCapacityUnit = newCapacityUnit;
  }

  public void setCardPanel(BillData bdData)
  {
    setCardPanelByPara(bdData);
    setCardPanelByOther(bdData);
  }

  protected void setCardPanelByOther(BillData bdData)
  {
  }

  protected void setCardPanelByPara(BillData bdData)
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

    for (int i = 0; (this.m_itemNumKeys != null) && (i < this.m_itemNumKeys.length); i++) {
      if (bdData.getBodyItem(this.m_itemNumKeys[i]) != null)
        bdData.getBodyItem(this.m_itemNumKeys[i]).setTatol(true);
    }
    for (int i = 0; (this.m_itemANumKeys != null) && (i < this.m_itemANumKeys.length); i++) {
      if (bdData.getBodyItem(this.m_itemANumKeys[i]) != null)
        bdData.getBodyItem(this.m_itemANumKeys[i]).setTatol(true);
    }
    for (int i = 0; (this.m_itemMoneyKey != null) && (i < this.m_itemMoneyKey.length); i++) {
      if (bdData.getBodyItem(this.m_itemMoneyKey[i]) != null) {
        bdData.getBodyItem(this.m_itemMoneyKey[i]).setTatol(true);
      }

    }

    try
    {
      if (null != bdData.getBodyItem("vfree0"))
      {
        getFreeItemRefPane().setMaxLength(bdData.getBodyItem("vfree0").getLength());

        bdData.getBodyItem("vfree0").setComponent(getFreeItemRefPane());
      }
    }
    catch (Exception e)
    {
    }
    try
    {
      if (null != bdData.getBodyItem("vbatchcode"))
      {
        getLotNumbRefPane().setMaxLength(bdData.getBodyItem("vbatchcode").getLength());

        bdData.getBodyItem("vbatchcode").setComponent(getLotNumbRefPane());
      }
    }
    catch (Exception e)
    {
    }
    try
    {
      bdData = changeBillDataByUserDef(bdData);
    }
    catch (Exception e)
    {
    }
  }

  public void setCorpID(String newCorpID)
  {
    this.m_sCorpID = newCorpID;
  }

  public void setCorpName(String newCorpID)
  {
    this.m_sCorpName = newCorpID;
  }

  public void setDelivOrgCode(String newOrgCode)
  {
    this.m_delivOrgCode = newOrgCode;
  }

  public void setDelivOrgName(String newOrgName)
  {
    this.m_delivOrgName = newOrgName;
  }

  public void setDelivOrgNoShow(boolean m_bDelivOrgNoShow)
  {
    this.m_bDelivOrgNoShow = m_bDelivOrgNoShow;
  }

  public void setDelivOrgPK(String newOrgPK)
  {
    this.m_delivOrgPK = newOrgPK;
  }

  protected void setDelivSequence(int newM_delivsequence)
  {
    this.m_delivsequence = newM_delivsequence;
  }

  public void setEditFlag(int newFlag)
  {
    this.m_editFlag = newFlag;
  }

  public void setFactorItemKey(String newFactorItemKey)
  {
    this.m_itemFactorKey = new String("");
    this.m_itemFactorKey = newFactorItemKey;
  }

  public void setHeaderVOName(String newHeaderVOName)
  {
    this.m_sHeaderVOName = newHeaderVOName;
  }

  public void setItemVOName(String newItemVOName)
  {
    this.m_sItemVOName = newItemVOName;
  }

  protected void setListPanelByPara(BillListData bdData)
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
    new DMBillListPanel().changeDecimalItemsPrecision(bdData, alDecimalKey, alPrecision);

    for (int i = 0; (this.m_itemNumKeys != null) && (i < this.m_itemNumKeys.length); i++) {
      if (bdData.getHeadItem(this.m_itemNumKeys[i]) != null)
        bdData.getHeadItem(this.m_itemNumKeys[i]).setTatol(true);
      if (bdData.getBodyItem(this.m_itemNumKeys[i]) != null)
        bdData.getBodyItem(this.m_itemNumKeys[i]).setTatol(true);
    }
    for (int i = 0; (this.m_itemANumKeys != null) && (i < this.m_itemANumKeys.length); i++) {
      if (bdData.getHeadItem(this.m_itemANumKeys[i]) != null)
        bdData.getHeadItem(this.m_itemANumKeys[i]).setTatol(true);
      if (bdData.getBodyItem(this.m_itemANumKeys[i]) != null)
        bdData.getBodyItem(this.m_itemANumKeys[i]).setTatol(true);
    }
    for (int i = 0; (this.m_itemMoneyKey != null) && (i < this.m_itemMoneyKey.length); i++) {
      if (bdData.getHeadItem(this.m_itemMoneyKey[i]) != null)
        bdData.getHeadItem(this.m_itemMoneyKey[i]).setTatol(true);
      if (bdData.getBodyItem(this.m_itemMoneyKey[i]) != null)
        bdData.getBodyItem(this.m_itemMoneyKey[i]).setTatol(true);
    }
  }

  protected void setLogDate(UFDate newLogDate)
  {
    this.m_LogDate = newLogDate;
  }

  public void setMoneyItemKey(String[] newMoneyItemKey)
  {
    this.m_itemMoneyKey = newMoneyItemKey;
  }

  public void setNodeCode(String newNodeCode)
  {
    this.m_sNodeCode = newNodeCode;
  }

  public void setNumItemKeys(String[] newNumItemKeys)
  {
    this.m_itemNumKeys = new String[newNumItemKeys.length];
    for (int i = 0; i < newNumItemKeys.length; i++)
      this.m_itemNumKeys[i] = newNumItemKeys[i];
  }

  public void setPriceItemKeys(String[] newPriceItemKeys)
  {
    this.m_itemPriceKeys = new String[newPriceItemKeys.length];
    for (int i = 0; i < newPriceItemKeys.length; i++)
      this.m_itemPriceKeys[i] = newPriceItemKeys[i];
  }

  protected void setPrimaryKeyName(String newPrimaryKeyName)
  {
    this.sPrimaryKeyName = newPrimaryKeyName;
  }

  public void setQueryConditionDlg(DMQueryConditionDlg newConditionDlg)
  {
    this.queryConditionDlg = newConditionDlg;
    this.queryConditionDlg.setTempletID(getCorpID(), getNodeCode(), getUserID(), null);

    this.queryConditionDlg.initQueryDlgRef();
  }

  public void setShowState(String newShowState)
  {
    this.m_strShowState = newShowState;
  }

  public void setTitleText(String title)
  {
    if (null != getFrame())
      getFrame().setTitleText(title);
  }

  public void setUserID(String newUserID)
  {
    this.m_sUserID = newUserID;
  }

  public void setUserName(String newUserID)
  {
    this.m_sUserName = newUserID;
  }

  public void setVOName(String newVOName)
  {
    this.m_sVOName = newVOName;
  }

  /** @deprecated */
  protected void setWeightUnit(String newWeightUnit)
  {
    this.m_currentWeightUnit = newWeightUnit;
  }

  protected void showCurrentTime(String methodName)
  {
    long lTime = System.currentTimeMillis();
    SCMEnv.info("========================执行<" + methodName + ">当前的时间为：" + lTime / 60000L + "分" + lTime / 1000L % 60L + "秒" + lTime % 1000L + "毫秒===============");
  }

  protected void showMethodTime(String methodName, long begintime)
  {
    long lTime = System.currentTimeMillis() - begintime;
    SCMEnv.info("========================执行<" + methodName + ">占用的时间为：" + lTime / 60000L + "分" + lTime / 1000L % 60L + "秒" + lTime % 1000L + "毫秒===============");
  }

  public void stateChanged(ChangeEvent e)
  {
  }

  public void tableChanged(TableModelEvent e)
  {
  }

  public void updateInvInfo(InvVO vo)
  {
    if (this.m_invht == null)
      this.m_invht = new Hashtable();
    String sKey = null;
    if ((vo != null) && 
      (vo.getCinventoryid() != null) && (vo != null)) {
      sKey = getInvAstUomKey(vo.getCinventoryid(), vo.getCastunitid());
      if (this.m_invht.containsKey(sKey))
        this.m_invht.remove(sKey);
      this.m_invht.put(sKey, vo);
    }
  }

  protected void updateItemByDef(BillData oldBillData, DefVO[] defVOs, String fieldPrefix, boolean isHead, int iStartIndex)
  {
    String z1 = NCLangRes.getInstance().getStrByID("_Bill", "UPP_Bill-000502");

    String z2 = NCLangRes.getInstance().getStrByID("_Bill", "UPP_Bill-000503");

    if (defVOs == null) {
      return;
    }
    for (int i = 0; i < defVOs.length; i++) {
      DefVO defVO = defVOs[i];

      String itemkey = fieldPrefix + (i + iStartIndex);
      BillItem item = null;

      if (isHead)
        item = oldBillData.getHeadItem(itemkey);
      else {
        item = oldBillData.getBodyItem(itemkey);
      }

      if ((item == null) || 
        (defVO == null))
        continue;
      String defaultshowname = item.getName();
      if ((defaultshowname == null) || (defaultshowname.startsWith("自定义")) || (defaultshowname.startsWith("自由项")) || (defaultshowname.startsWith("表体自定义")) || (defaultshowname.startsWith(z1)) || (defaultshowname.startsWith(z2)) || (defaultshowname.startsWith("H-UDC")) || (defaultshowname.startsWith("B-UDC")) || (defaultshowname.startsWith(fieldPrefix)))
      {
        defaultshowname = defVO.getDefname();
        item.setName(defaultshowname);
      }

      int inputlength = defVO.getLengthnum().intValue();
      item.setLength(inputlength);

      String type = defVO.getType();
      int datatype = 0;
      if (type.equals("备注")) {
        datatype = 0;
      } else if (type.equals("日期")) {
        datatype = 3;
      } else if (type.equals("数字")) {
        datatype = 1;
        if ((defVO.getDigitnum() != null) && (defVO.getDigitnum().intValue() > 0)) {
          datatype = 2;
          item.setDecimalDigits(defVO.getDigitnum().intValue());
        }
      }
      if (type.equals("统计"))
        datatype = 7;
      item.setDataType(datatype);

      String reftype = defVO.getDefdef().getPk_bdinfo();
      if (type.equals("统计")) {
        item.setRefType(reftype);
      }

      item.reCreateComponent();
      item.setIsDef(true);
    }
  }

  protected void updateItemByDef(BillListData oldBillData, DefVO[] defVOs, String fieldPrefix, boolean isHead, int iStartIndex)
  {
    if (defVOs == null)
      return;
    String z1 = NCLangRes.getInstance().getStrByID("_Bill", "UPP_Bill-000502");

    String z2 = NCLangRes.getInstance().getStrByID("_Bill", "UPP_Bill-000503");

    for (int i = 0; i < defVOs.length; i++) {
      DefVO defVO = defVOs[i];

      String itemkey = fieldPrefix + (i + iStartIndex);
      BillItem item = null;

      if (isHead)
        item = oldBillData.getHeadItem(itemkey);
      else {
        item = oldBillData.getBodyItem(itemkey);
      }
      if ((item == null) || (defVO == null)) {
        continue;
      }
      String defaultshowname = item.getName();
      if ((defaultshowname == null) || (defaultshowname.startsWith("自定义")) || (defaultshowname.startsWith("自由项")) || (defaultshowname.startsWith("表体自定义")) || (defaultshowname.startsWith(z1)) || (defaultshowname.startsWith(z2)) || (defaultshowname.startsWith("H-UDC")) || (defaultshowname.startsWith("B-UDC")) || (defaultshowname.startsWith(fieldPrefix)))
      {
        defaultshowname = defVO.getDefname();
        item.setName(defaultshowname);
      }

      int inputlength = defVO.getLengthnum().intValue();
      item.setLength(inputlength);

      String type = defVO.getType();
      int datatype = 0;
      if (type.equals("备注")) {
        datatype = 0;
      } else if (type.equals("日期")) {
        datatype = 3;
      } else if (type.equals("数字")) {
        datatype = 1;
        if ((defVO.getDigitnum() != null) && (defVO.getDigitnum().intValue() > 0)) {
          datatype = 2;
          item.setDecimalDigits(defVO.getDigitnum().intValue());
        }
      }
      if (type.equals("统计"))
        datatype = 7;
      item.setDataType(datatype);

      String reftype = defVO.getDefdef().getPk_bdinfo();
      if (type.equals("统计")) {
        item.setRefType(reftype);
      }

      item.reCreateComponent();
      item.setIsDef(true);
    }
  }

  public void valueChanged(ListSelectionEvent e)
  {
    if (e.getSource() == getBillCardPanel().getBillTable().getSelectionModel()) {
      int colnow = getBillCardPanel().getBillTable().getSelectedColumn();
      int rownow = getBillCardPanel().getBillTable().getSelectedRow();
      if ((colnow >= 0) && (rownow >= 0)) {
        initRef(rownow, colnow, (DMBillCardPanel)getBillCardPanel());
        String key = getBillCardPanel().getBodyShowItems()[colnow].getKey();
        Object value = getBillCardPanel().getBodyValueAt(rownow, key);
        whenEntered(rownow, colnow, key, value, getBillCardPanel());
      }
    }
  }

  public void whenEntered(int row, int col, String key, Object value, BillCardPanel bcp)
  {
    if ((null != bcp.getBodyItem(key)) && ((bcp.getBodyItem(key).getComponent() instanceof UIRefPane)))
      ((UIRefPane)bcp.getBodyItem(key).getComponent()).setValue(value == null ? null : value.toString());
  }

  public String getVuserdefCode()
  {
    return this.vUserdefCode;
  }

  public void setVuserdefCode(String code)
  {
    this.vUserdefCode = code;
  }
}