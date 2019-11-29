package nc.ui.fa.fa20120301pattern.command;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.JTable;
import nc.itf.fa.prv.IAlterBill;
import nc.itf.fa.prv.ICard;
import nc.itf.fa.prv.IDepProcess;
import nc.itf.fa.prv.IOption;
import nc.ui.fa.application.common.command.DefaultCommand;
import nc.ui.fa.fa20120301pattern.ChooserCorpUIRefPane;
import nc.ui.fa.fa20120301pattern.DepdetailUI;
import nc.ui.fa.fa20120301pattern.Enum0301;
import nc.ui.fa.uifactory.manage.FABillManageUI;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillScrollPane;
import nc.ui.pub.bill.BillTabbedPane;
import nc.vo.bd.CorpVO;
import nc.vo.fa.FaPublicPara;
import nc.vo.fa.ShareBase;
import nc.vo.fa.accperiod.AccperiodVO;
import nc.vo.fa.accperiod.PeriodManager;
import nc.vo.fa.closebook.FaCloseBook;
import nc.vo.fa.depdetail.DepdetailBodyVO;
import nc.vo.fa.depdetail.DepdetailVO;
import nc.vo.fa.fa201201.OptionVO;
import nc.vo.fa.fa20120201.CardAllVO;
import nc.vo.fa.fa20120201.CardhistoryVO;
import nc.vo.fa.fa20120204.AltersheetVO;
import nc.vo.fa.parse.DepGather;
import nc.vo.fa.pub.proxy.FAProxy;
import nc.vo.fa.sort.FaSortor;
import nc.vo.pub.lang.UFDouble;

public class ReadDataCommand extends DefaultCommand
{
  private String m_sDwbm = null;

  private OptionVO m_voOptionVO = null;

  private String m_sFaStartYear = null;

  private String m_sFaStartMonth = null;

  private Object oYear = null;

  private Object oMonth = null;

  private DepdetailVO depvo = null;
  private int m_iMinPeriod;
  private int m_iLocalCurrencyScale;
  private int m_iQuantityScale;
  private PeriodManager m_cPeriodSrv = null;

  private Hashtable m_hCardCodeAccuWorkloan = null;
  private int m_iRateScale;
  private int m_iUnitDepScale;
  private HashMap m_hmCorpMap = null;

  private HashMap m_hmCard_codeMapData = null;

  public ReadDataCommand(FABillManageUI manageUI) {
    super(manageUI);
  }

  public void execute()
    throws Exception
  {
    this.m_iMinPeriod = ((DepdetailUI)getBillManageUI()).getM_iMinPeriod();
    this.m_cPeriodSrv = ((DepdetailUI)getBillManageUI()).getM_cPeriodSrv();
    this.m_iRateScale = ((DepdetailUI)getBillManageUI()).getM_iRateScale();
    this.m_iUnitDepScale = ((DepdetailUI)getBillManageUI()).getM_iUnitDepScale();

    this.m_hmCard_codeMapData = ((DepdetailUI)getBillManageUI()).getM_hmCard_codeMapData();

    this.m_sDwbm = ((DepdetailUI)getBillManageUI()).getM_sDwbm();
    this.m_voOptionVO = null;
    this.m_sFaStartYear = ((DepdetailUI)getBillManageUI()).getM_sFaStartYear();
    this.m_sFaStartMonth = ((DepdetailUI)getBillManageUI()).getM_sFaStartMonth();

    this.oYear = ((DepdetailUI)getBillManageUI()).getOYear();
    this.oMonth = ((DepdetailUI)getBillManageUI()).getOMonth();
    CorpVO[] corpVOs = null;
    try {
      corpVOs = ((ChooserCorpUIRefPane)(ChooserCorpUIRefPane)getBillManageUI().getBillCardPanel().getBillData().getHeadItem("multicorp").getComponent()).getResultVOs();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }

    if (corpVOs != null) {
      this.m_hmCorpMap = new HashMap();
      for (int i = 0; i < corpVOs.length; i++) {
        this.m_hmCorpMap.put(corpVOs[i].getPk_corp(), corpVOs[i]);
      }
    }

    ((DepdetailUI)getBillManageUI()).setM_hmCorpMap(this.m_hmCorpMap);

    this.depvo = new DepdetailVO();

    this.m_iMinPeriod = ((DepdetailUI)getBillManageUI()).getM_iMinPeriod();

    this.m_iLocalCurrencyScale = ((DepdetailUI)getBillManageUI()).getM_iLocalCurrencyScale();

    this.m_iQuantityScale = ((DepdetailUI)getBillManageUI()).getM_iQuantityScale();

    this.m_cPeriodSrv = ((DepdetailUI)getBillManageUI()).getM_cPeriodSrv();
    this.m_hCardCodeAccuWorkloan = ((DepdetailUI)getBillManageUI()).getM_hCardCodeAccuWorkloan();

    this.m_iRateScale = ((DepdetailUI)getBillManageUI()).getM_iRateScale();
    this.m_iUnitDepScale = ((DepdetailUI)getBillManageUI()).getM_iUnitDepScale();

    Object oYear = ((UIComboBox)(UIComboBox)getBillManageUI().getBillCardPanel().getBillData().getHeadItem("accyear").getComponent()).getSelectedItem();

    Object oMonth = ((UIComboBox)(UIComboBox)getBillManageUI().getBillCardPanel().getBillData().getHeadItem("period").getComponent()).getSelectedItem();

    ((DepdetailUI)getBillManageUI()).setOYear(oYear);
    ((DepdetailUI)getBillManageUI()).setOMonth(oMonth);

    onRead();

    ((DepdetailUI)getBillManageUI()).getBodyTabbedPane().setSelectedIndex(1);
    getFaTableModel1().setEnabled(false);
    getFaTableModel2().setEnabled(false);
    getFaTableModel3().setEnabled(false);
  }

  private JTable getScrollPaneTable1()
  {
    JTable table = getBillListPanel().getBodyScrollPane(this.depvo.getTableCodes()[0]).getTable();

    return table;
  }

  private String getAccBook()
  {
    return ((DepdetailUI)getBillManageUI()).getAccBook();
  }

  public void onRead()
  {
    try {
      getBillCardPanel().stopEditing();
      try {
        getBillManageUI().setBillOperate(2);
      }
      catch (Exception e1) {
        e1.printStackTrace();
      }

      String fk_accbook = getAccBook();

      getBillManageUI().showHintMessage(NCLangRes.getInstance().getStrByID("201233", "UPP201233-000113"));

      String[] sUnits = ((ChooserCorpUIRefPane)(ChooserCorpUIRefPane)getBillManageUI().getBillCardPanel().getBillData().getHeadItem("multicorp").getComponent()).getRefPKs();

      if ((sUnits != null) && (sUnits.length > 1)) {
        ((DepdetailUI)getBillManageUI()).setM_bIsMutiUnit(true);
        onReadMutiUnitData();
        setEnabled(302, false);
        setEnabled(3, false);
        return;
      }

      ((DepdetailUI)getBillManageUI()).setM_bIsMutiUnit(false);

      if ((sUnits != null) && (sUnits.length == 1)) {
        this.m_sDwbm = sUnits[0];
      }

      boolean depFlag = ((DepdetailUI)getBillManageUI()).isM_bIsDep();
      if (!depFlag) {
        getBillManageUI().showHintMessage(NCLangRes.getInstance().getStrByID("201233", "UPP201233-000052"));

        setEnabled(300, false);

        setEnabled(301, false);

        setEnabled(302, false);

        setEnabled(3, false);

        setEnabled(0, false);

        setEnabled(7, false);

        setEnabled(303, false);

        setEnabled(6, false);

        return;
      }

      if ((((DepdetailUI)getBillManageUI()).getOYear() == null) || (((DepdetailUI)getBillManageUI()).getOMonth() == null))
      {
        getBillManageUI().showHintMessage(NCLangRes.getInstance().getStrByID("201233", "UPP201233-000100"));

        return;
      }
      String sYear = ((DepdetailUI)getBillManageUI()).getOYear().toString();

      String sMonth = ((DepdetailUI)getBillManageUI()).getOMonth().toString();

      ((DepdetailUI)getBillManageUI()).setM_sCurYear(sYear);

      ((DepdetailUI)getBillManageUI()).setM_sCurMonth(sMonth);

      FaCloseBook fc = new FaCloseBook();
      boolean isMinUnCloseBook = fc.isMinUnCloseBookMonth(this.m_sDwbm, sYear, sMonth, fk_accbook);

      int iMinClosebookPeriod = 0;
      if (isMinUnCloseBook) {
        iMinClosebookPeriod = 1;

        ((DepdetailUI)getBillManageUI()).setM_bIsCloseBook(false);
      }
      if (!isMinUnCloseBook)
      {
        boolean bCurrency = fc.isCloseBookCurrentMonth(this.m_sDwbm, sYear, sMonth, fk_accbook);

        if (bCurrency) {
          iMinClosebookPeriod = 2;

          ((DepdetailUI)getBillManageUI()).setM_bIsCloseBook(true);
        } else {
          iMinClosebookPeriod = 3;

          ((DepdetailUI)getBillManageUI()).setM_bIsCloseBook(false);
        }
      }
      try
      {
        this.m_voOptionVO = FAProxy.getRemoteOption().queryOptionAll(this.m_sDwbm, sYear, sMonth);

        ((DepdetailUI)getBillManageUI()).setM_voOptionVO(this.m_voOptionVO);
        if (this.m_voOptionVO.getDepall_flag().intValue() == 0)
        {
          ((DepdetailUI)getBillManageUI()).setM_bIsDepAll_flag(false);
        }
        else
        {
          ((DepdetailUI)getBillManageUI()).setM_bIsDepAll_flag(true);
        }if (this.m_voOptionVO.getDepperiod() == null)
        {
          this.m_voOptionVO.setDepperiod(new Integer(1));
        }
      }
      catch (Throwable e) {
        ((DepdetailUI)getBillManageUI()).setM_bIsDepAll_flag(true);
      }
      Vector vDepBill = null; Vector vWorkloanCard = null; Vector vMonthWordloan = null;
      CardhistoryVO[] cVOs = null;
      if (iMinClosebookPeriod == 1)
      {
        int iDep = FaPublicPara.isDep(this.m_sDwbm, sYear, sMonth, fk_accbook).intValue();

        CardAllVO[] cavos = null;
        if (iDep != 0) {
          cavos = getDepDataNew(this.m_sDwbm, sYear, sMonth);
        }
        if (cavos != null) {
          setDepBillData(cavos);
        }
        vWorkloanCard = getWorkamountNew(this.m_sDwbm, sYear, sMonth);

        vMonthWordloan = FAProxy.getRemoteDepProcess().queryMonthWorkloan_Dep(this.m_sDwbm, sYear, sMonth, new Integer(0), fk_accbook);

        setWorkloanData(vWorkloanCard, vMonthWordloan);

        cVOs = readGatherData(this.m_sDwbm, sYear, sMonth);
        actionFillGather(sYear, sMonth, cVOs);

        ((DepdetailUI)getBillManageUI()).setM_voGatherVOs(cVOs);

        ((DepdetailUI)getBillManageUI()).setM_iMinPeriod(iDep);

        String sMessage = "";

        if ((!this.m_sFaStartYear.equals(sYear)) || (!this.m_sFaStartMonth.equals(sMonth)))
        {
          String sAccyear = getAccperiodVO().getAccyear();
          String sPeriod = getAccperiodVO().getAccmonth();
          if ((!sMonth.equals(sPeriod)) || (!sYear.equals(sAccyear)))
          {
            ((DepdetailUI)getBillManageUI()).setM_iMinPeriod(-1);
            sMessage = NCLangRes.getInstance().getStrByID("201233", "UPP201233-000114");
          }

        }

        ((DepdetailUI)getBillManageUI()).setStatus(((DepdetailUI)getBillManageUI()).getM_iMinPeriod());

        if (iDep > 10) {
          getBillManageUI().showHintMessage(NCLangRes.getInstance().getStrByID("201233", "UPP201233-000115"));
        }
        else if (iDep == 1) {
          getBillManageUI().showHintMessage(NCLangRes.getInstance().getStrByID("201233", "UPP201233-000116") + sMessage);
        }
        else if (iDep == 2) {
          getBillManageUI().showHintMessage(NCLangRes.getInstance().getStrByID("201233", "UPP201233-000117") + sMessage);

          setEnabled(3, true);
        }
        else {
          getBillManageUI().showHintMessage(NCLangRes.getInstance().getStrByID("201233", "UPP201233-000118") + sMessage);
        }

      }
      else if (iMinClosebookPeriod == 2)
      {
        CardAllVO[] cavos = getDepDataOld(this.m_sDwbm, sYear, sMonth);
        setDepBillData(cavos);

        vWorkloanCard = getWorkamountOld(this.m_sDwbm, sYear, sMonth);

        vMonthWordloan = FAProxy.getRemoteDepProcess().queryMonthWorkloan_Dep(this.m_sDwbm, sYear, sMonth, new Integer(0), fk_accbook);

        ((DepdetailUI)getBillManageUI()).setM_iMinPeriod(-1);

        setWorkloanData(vWorkloanCard, vMonthWordloan);

        cVOs = readGatherData(this.m_sDwbm, sYear, sMonth);
        actionFillGather(sYear, sMonth, cVOs);

        ((DepdetailUI)getBillManageUI()).setM_voGatherVOs(cVOs);
        ((DepdetailUI)getBillManageUI()).setStatus(((DepdetailUI)getBillManageUI()).getM_iMinPeriod());

        getBillManageUI().showHintMessage(NCLangRes.getInstance().getStrByID("201233", "UPP201233-000119"));
      }
      else
      {
        ((DepdetailUI)getBillManageUI()).setM_iMinPeriod(-1);

        actionFillWorkloan(null);

        actionFillDepBill(null);

        actionFillGather(sYear, sMonth, null);

        ((DepdetailUI)getBillManageUI()).setM_voGatherVOs(null);
        ((DepdetailUI)getBillManageUI()).setStatus(((DepdetailUI)getBillManageUI()).getM_iMinPeriod());

        getBillManageUI().showHintMessage(NCLangRes.getInstance().getStrByID("201233", "UPP201233-000120"));
      }

    }
    catch (Throwable e)
    {
      getBillManageUI().showHintMessage(NCLangRes.getInstance().getStrByID("201233", "UPP201233-000121"));

      e.printStackTrace();
    }
  }

  public void onReadMutiUnitData()
  {
    String[] sUnits = ((ChooserCorpUIRefPane)(ChooserCorpUIRefPane)getBillManageUI().getBillCardPanel().getBillData().getHeadItem("multicorp").getComponent()).getRefPKs();

    String fk_accbook = getAccBook();

    if ((sUnits == null) || (sUnits.length == 0)) {
      getBillManageUI().showHintMessage(NCLangRes.getInstance().getStrByID("201233", "UPP201233-000062"));

      return;
    }

    Arrays.sort(sUnits);
    String dwbm = "";

    if ((this.oYear == null) || (this.oMonth == null)) {
      getBillManageUI().showHintMessage(NCLangRes.getInstance().getStrByID("201233", "UPP201233-000100"));

      return;
    }
    OptionVO voOptionVO = null;
    String sYear = this.oYear.toString();
    String sMonth = this.oMonth.toString();
    Vector vResult = new Vector(0);
    try
    {
      for (int i = 0; i < sUnits.length; i++)
      {
        this.m_voOptionVO = FAProxy.getRemoteOption().queryOptionAll(dwbm, sYear, sMonth);

        Vector vOneUnitData = new Vector(0);
        dwbm = sUnits[i];
        vOneUnitData.addElement(dwbm);

        FaCloseBook fc = new FaCloseBook();
        boolean isMinUnCloseBook = fc.isMinUnCloseBookMonth(dwbm, sYear, sMonth, fk_accbook);

        int iMinClosebookPeriod = 0;
        if (isMinUnCloseBook) {
          iMinClosebookPeriod = 1;
        }
        if (!isMinUnCloseBook)
        {
          boolean bCurrency = fc.isCloseBookCurrentMonth(dwbm, sYear, sMonth, fk_accbook);

          if (bCurrency)
            iMinClosebookPeriod = 2;
          else
            iMinClosebookPeriod = 3;
        }
        try
        {
          if (sUnits.length == 1) {
            voOptionVO = FAProxy.getRemoteOption().queryOptionAll(dwbm, sYear, sMonth);

            if (voOptionVO.getDepperiod() == null)
            {
              voOptionVO.setDepperiod(new Integer(1));
            }
          } else {
            voOptionVO = null;
          }
        } catch (Throwable e) {
          e.printStackTrace();
        }
        Vector vDepBill = null; Vector vWorkloanCard = null; Vector vMonthWordloan = null;
        CardhistoryVO[] cVOs = null;
        if (iMinClosebookPeriod == 1)
        {
          int iDep = FaPublicPara.isDep(dwbm, sYear, sMonth, fk_accbook).intValue();

          CardAllVO[] cavos = null;
          if (iDep != 0) {
            cavos = getDepDataNew(dwbm, sYear, sMonth);
          }

          vOneUnitData.addElement(cavos);
          vWorkloanCard = getWorkamountNew(dwbm, sYear, sMonth);

          vMonthWordloan = FAProxy.getRemoteDepProcess().queryMonthWorkloan_Dep(dwbm, sYear, sMonth, new Integer(0), fk_accbook);

          vOneUnitData.addElement(vWorkloanCard);
          vOneUnitData.addElement(vMonthWordloan);
          if (sUnits.length == 1) {
            cVOs = readGatherData(dwbm, sYear, sMonth);
            vOneUnitData.addElement(cVOs);
          } else {
            vOneUnitData.addElement(null);
          }
        } else if (iMinClosebookPeriod == 2)
        {
          CardAllVO[] cavos = getDepDataOld(dwbm, sYear, sMonth);
          vOneUnitData.addElement(cavos);
          vWorkloanCard = getWorkamountOld(dwbm, sYear, sMonth);

          vMonthWordloan = FAProxy.getRemoteDepProcess().queryMonthWorkloan_Dep(dwbm, sYear, sMonth, new Integer(0), fk_accbook);

          vOneUnitData.addElement(vWorkloanCard);
          vOneUnitData.addElement(vMonthWordloan);

          if (sUnits.length == 1) {
            cVOs = readGatherData(dwbm, sYear, sMonth);
            vOneUnitData.addElement(cVOs);
          } else {
            vOneUnitData.addElement(null);
          }
        }
        else
        {
          ((DepdetailUI)getBillManageUI()).setM_iMinPeriod(-1);

          vOneUnitData.addElement(null);

          vOneUnitData.addElement(null);
          vOneUnitData.addElement(null);
        }

        vResult.addElement(vOneUnitData);
      }
    } catch (Throwable e) {
      e.printStackTrace();
      getBillManageUI().showHintMessage(e.getMessage());
      return;
    }
    actionFillMutiUnitData(sUnits, vResult);
  }

  private CardAllVO[] getDepDataNew(String sDwbm, String sYear, String sMonth)
  {
    return ((DepdetailUI)getBillManageUI()).getDepDataNew(sDwbm, sYear, sMonth);
  }

  private void setDepBillData(CardAllVO[] cavos)
  {
    ((DepdetailUI)getBillManageUI()).setDepBillData(cavos);
  }

  private void actionFillDepBill(Vector vData2)
  {
    ((DepdetailUI)getBillManageUI()).actionFillDepBill(vData2);
  }

  public void actionFillMutiUnitData(String[] sUnits, Vector vData)
  {
    if (vData.size() == 0) {
      return;
    }
    CardhistoryVO[] gatherDatas = null;
    Vector vAllUnitData = new Vector(0);
    Vector vAllUnitWkData = new Vector(0);
    for (int i = 0; i < vData.size(); i++) {
      Vector vOneUnit = (Vector)vData.elementAt(i);
      String dwbm = vOneUnit.elementAt(0).toString();
      CardAllVO[] cavos = (CardAllVO[])(CardAllVO[])vOneUnit.elementAt(1);
      if (sUnits.length == 1)
      {
        ((DepdetailUI)getBillManageUI()).setM_voKeepAllVOs(cavos);
      }

      processJZZB(dwbm, cavos);
      Vector vWorkloan = (Vector)vOneUnit.elementAt(2);
      Vector vLastMonthWorkloan = (Vector)vOneUnit.elementAt(3);
      Vector vDepBillData = getDataFromCardAllVO(cavos);
      if ((vDepBillData != null) && (vDepBillData.size() > 0))
      {
        vAllUnitData.addElement(vDepBillData);
      }
      Vector vMonthData = getWorkLoanData(vWorkloan, vLastMonthWorkloan);
      if ((vMonthData != null) && (vMonthData.size() > 0))
      {
        vAllUnitWkData = procAllUnitWKData(vAllUnitWkData, vMonthData, dwbm);
      }

      if ((sUnits.length != 1) || 
        (vOneUnit.size() <= 4)) continue;
      gatherDatas = (CardhistoryVO[])(CardhistoryVO[])vOneUnit.elementAt(4);
    }

    if (vAllUnitData.size() > 0) {
      Vector vAllData = procAllUnitData(vAllUnitData);
      actionFillDepBill(vAllData);
    } else {
      actionFillDepBill(null);
    }

    if (vAllUnitWkData.size() > 0)
      actionFillWorkloan(vAllUnitWkData);
    else {
      actionFillWorkloan(null);
    }

    actionFillGather(this.oYear.toString(), this.oMonth.toString(), gatherDatas);
  }

  private void actionFillGather(String year, String month, CardhistoryVO[] cVOs)
  {
    ((DepdetailUI)getBillManageUI()).actionFillGather(year, month, cVOs);
  }

  private Vector procAllUnitData(Vector v)
  {
    Vector vAllData = new Vector();

    UFDouble localvalue = new UFDouble(0);
    UFDouble accudep = new UFDouble(0);
    UFDouble depamount = new UFDouble(0);
    UFDouble accuwork = new UFDouble(0);
    UFDouble monthwork = new UFDouble(0);

    for (int i = 0; i < v.size(); i++) {
      Vector vOneUnitData = (Vector)v.elementAt(i);
      for (int j = 0; j < vOneUnitData.size(); j++) {
        DepdetailBodyVO tmp = (DepdetailBodyVO)vOneUnitData.elementAt(j);

        vAllData.addElement(tmp);
        if (j == vOneUnitData.size() - 1) {
          localvalue = localvalue.add(tmp.getLocaloriginvalue() == null ? new UFDouble(0) : tmp.getLocaloriginvalue());

          depamount = depamount.add(tmp.getDepamount() == null ? new UFDouble(0) : tmp.getDepamount());

          accudep = accudep.add(tmp.getAccudep() == null ? new UFDouble(0) : tmp.getAccudep());

          monthwork = monthwork.add(tmp.getMonthworkloan() == null ? new UFDouble(0) : tmp.getMonthworkloan());

          accuwork = accuwork.add(tmp.getAccuworkloan() == null ? new UFDouble(0) : tmp.getAccuworkloan());
        }

      }

    }

    if ((((DepdetailUI)getBillManageUI()).getCorpKeys() != null) && (((DepdetailUI)getBillManageUI()).getCorpKeys().length > 1))
    {
      Vector vTemp2 = new Vector();

      vTemp2.addElement(Enum0301.HJ);

      vTemp2.addElement(null);
      vTemp2.addElement(null);
      vTemp2.addElement(null);
      vTemp2.addElement(getUFDouble(localvalue, this.m_iLocalCurrencyScale));
      vTemp2.addElement(getUFDouble(depamount, this.m_iLocalCurrencyScale));
      vTemp2.addElement(getUFDouble(accudep, this.m_iLocalCurrencyScale));
      vTemp2.addElement(null);
      vTemp2.addElement(null);
      vTemp2.addElement(getUFDouble(monthwork, this.m_iQuantityScale));
      vTemp2.addElement(getUFDouble(accuwork, this.m_iQuantityScale));
      vTemp2.addElement(null);
      vAllData.addElement(vTemp2);
    }

    return vAllData;
  }

  private CardAllVO[] getDepDataOld(String sDwbm, String sYear, String sMonth)
  {
    CardAllVO[] depBillVO = null;
    String fk_accbook = getAccBook();
    String sNextYear = sYear; String sNextMonth = sMonth;
    AccperiodVO ap = getPeriodSrv().getNextPeriod(sYear, sMonth);

    sNextYear = ap.getAccyear();
    sNextMonth = ap.getAccmonth();
    try
    {
      depBillVO = FAProxy.getRemoteDepProcess().getDepDataOld_Dep(sDwbm, sYear, sMonth, fk_accbook);
    }
    catch (Throwable e) {
      e.printStackTrace();
    }
    return depBillVO;
  }

  private PeriodManager getPeriodSrv()
  {
    return ((DepdetailUI)getBillManageUI()).getPeriodSrv();
  }

  private CardhistoryVO[] readGatherData(String sDwbm, String sYear, String sMonth)
  {
    CardhistoryVO[] gather = null;
    try {
      int dapFlag = 0;
      if (((DepdetailUI)getBillManageUI()).getM_iCycle() > 1) {
        if (getPeriodSrv().isNeedDepSum(sYear, sMonth))
          dapFlag = 1;
      }
      else {
        dapFlag = 1;
      }
      if (dapFlag == 1) {
        DepGather gg = new DepGather(sDwbm, sYear, sMonth, getAccBook());

        gather = gg.readGatherData();
      }
    } catch (Throwable e) {
      e.printStackTrace();
      return null;
    }
    return gather;
  }

  private Vector getWorkamountNew(String sDwbm, String sYear, String sMonth)
  {
    Vector vWorkloanCard = new Vector(0);
    Vector vWhere = new Vector();

    String fk_accbook = getAccBook();

    String[][] sWhere = { { "fa_card.pk_corp", "=", "'" + sDwbm + "'" }, { "fa_cardhistory.laststate_flag", "=", "1" }, { "fa_cardhistory.accyear", "=", "'" + sYear + "'" }, { "fa_cardhistory.period", "=", "'" + sMonth + "'" }, { "fa_cardhistory.fk_accbook", "=", "'" + fk_accbook + "'" } };

    vWhere = ShareBase.getVector(sWhere);

    Vector vUsedDepmethod = null;
    try {
      vUsedDepmethod = FAProxy.getRemoteDepProcess().queryCardByDepmethod_Dep(sDwbm, sYear, sMonth, fk_accbook);
    }
    catch (Throwable e) {
      e.printStackTrace();
      vUsedDepmethod = new Vector(0);
    }

    Vector vArrDepmethod = getWorkloanMethod(vUsedDepmethod);
    String sMethod = "";
    for (int i = 0; i < vArrDepmethod.size(); i++) {
      sMethod = sMethod + ",'" + vArrDepmethod.elementAt(i).toString().trim() + "'";
    }

    String[] sElements = { "fa_cardhistory.fk_depmethod", "in", "('00000000000000000004'" + sMethod + ") " };

    vWhere.addElement(ShareBase.getVector(sElements));
    String[] sElements2 = { "fa_cardhistory.allworkloan", ">", "fa_cardhistory.accuworkloan-fa_cardhistory.monthworkloan" };

    vWhere.addElement(ShareBase.getVector(sElements2));

    Vector changeVec = null;
    if (this.m_voOptionVO.getDepmethod_flag().intValue() == 0) {
      try {
        Vector vFk_card = new Vector(0);

        AltersheetVO[] voAltersheetVOs = FAProxy.getRemoteAlterBill().queryAltersheetVOs(sDwbm, sYear, sMonth, fk_accbook);

        int i = 0;
        for (; (voAltersheetVOs != null) && (i < voAltersheetVOs.length); i++) {
          int iType = voAltersheetVOs[i].getAltertype().intValue();
          if (iType != 6)
            continue;
          if (!voAltersheetVOs[i].getOldcontent().equals("00000000000000000004"))
            continue;
          vFk_card.addElement(voAltersheetVOs[i].getFk_card());
        }

        String con = "";
        for (i = 0; i < vFk_card.size(); i++) {
          if (i == 0)
            con = "'" + vFk_card.elementAt(i).toString() + "'";
          else {
            con = con + ",'" + vFk_card.elementAt(i).toString() + "'";
          }
        }

        if (con.length() > 0) {
          String where = "fa_card.pk_corp = '" + sDwbm + "' ";
          where = where + "and fa_cardhistory.laststate_flag = 1 ";
          where = where + "and fa_cardhistory.accyear = '" + sYear + "' ";
          where = where + "and fa_cardhistory.period = '" + sMonth + "' ";

          where = where + "and fa_cardhistory.fk_accbook =  '" + fk_accbook + "' ";

          where = where + "and fa_cardhistory.fk_card in( " + con + ")";
          Vector vv = new Vector(0);
          vv.addElement(where);
          vv.addElement("");
          vv.addElement("");
          changeVec = new Vector(0);
          changeVec.addElement(vv);
        }
      } catch (Throwable e) {
        e.printStackTrace();
      }
    }

    Vector addRec = null;
    try {
      vWorkloanCard = FAProxy.getRemoteCard().findCalValuesByVector(Enum0301.getFields1(), vWhere);

      if (changeVec != null) {
        addRec = FAProxy.getRemoteCard().findCalValuesByVector(Enum0301.getFields1(), changeVec);

        if (addRec != null) {
          for (int i = 0; i < addRec.size(); i++) {
            vWorkloanCard.addElement(addRec.elementAt(i));
          }
        }
        vWorkloanCard = sortData(vWorkloanCard);
      }

    }
    catch (Throwable e)
    {
      e.printStackTrace();
    }
    return vWorkloanCard;
  }

  private void setWorkloanData(Vector vWorkloanCard, Vector vLastMonthWorkloan)
  {
    this.m_hCardCodeAccuWorkloan = new Hashtable();
    if ((vWorkloanCard == null) || (vWorkloanCard.size() <= 0)) {
      actionFillWorkloan(null);
      return;
    }
    Hashtable hLastMonthWorkloan = new Hashtable();
    if ((vLastMonthWorkloan != null) && (vLastMonthWorkloan.size() > 0)) {
      for (int i = 0; i < vLastMonthWorkloan.size(); i++) {
        Vector vT = (Vector)vLastMonthWorkloan.elementAt(i);
        hLastMonthWorkloan.put(vT.elementAt(0), vT.elementAt(1));
      }
    }
    Vector vData1 = new Vector();
    Vector vTemp1 = new Vector();
    UFDouble ufd = null;
    for (int i = 0; i < vWorkloanCard.size(); i++) {
      vTemp1 = (Vector)vWorkloanCard.elementAt(i);

      ufd = new UFDouble(vTemp1.elementAt(3).toString(), -1 * this.m_iQuantityScale);

      vTemp1.setElementAt(ufd, 3);

      UFDouble ufdTemp = new UFDouble(0).setScale(-1 * this.m_iQuantityScale, 4);

      String sCardPkey = vTemp1.elementAt(8).toString();
      if (hLastMonthWorkloan.containsKey(sCardPkey)) {
        ufdTemp = new UFDouble(hLastMonthWorkloan.get(sCardPkey).toString(), -1 * this.m_iQuantityScale);
      }

      vTemp1.setElementAt(ufdTemp, 4);

      ufd = new UFDouble(vTemp1.elementAt(5).toString(), -1 * this.m_iQuantityScale);

      vTemp1.setElementAt(ufd, 5);

      ufd = new UFDouble(vTemp1.elementAt(6).toString(), -1 * this.m_iQuantityScale);

      vTemp1.setElementAt(ufd, 6);

      double dAllworkloan = new Double(vTemp1.elementAt(3).toString()).doubleValue();

      double dAccuworkloan = new Double(vTemp1.elementAt(6).toString()).doubleValue();

      vTemp1.setElementAt(new UFDouble(dAllworkloan - dAccuworkloan, -1 * this.m_iQuantityScale), 7);

      vData1.addElement(vTemp1.clone());
      double dMonthworkloan = new Double(vTemp1.elementAt(5).toString()).doubleValue();

      String sCardCode = vTemp1.elementAt(0).toString();
      if (!this.m_hCardCodeAccuWorkloan.containsKey(sCardCode)) {
        this.m_hCardCodeAccuWorkloan.put(sCardCode, new UFDouble(dAccuworkloan - dMonthworkloan));
      }

      ((DepdetailUI)getBillManageUI()).setM_hCardCodeAccuWorkloan(this.m_hCardCodeAccuWorkloan);
    }

    actionFillWorkloan(vData1);
  }

  private void actionFillWorkloan(Vector vData1)
  {
    ((DepdetailUI)getBillManageUI()).actionFillWorkloan(vData1);
  }

  public Vector sortData(Vector v)
  {
    Integer[] inde = new Integer[1];
    inde[0] = new Integer(0);

    FaSortor sort = new FaSortor();
    sort.setSortfield(inde);
    v = sort.sortVectorData(v);
    return v;
  }

  public Vector getWorkloanMethod(Vector vExpression)
  {
    String delim = " +-*/()";
    if (vExpression == null)
      return null;
    Vector vResult = new Vector(0);
    for (int i = 0; i < vExpression.size(); i++) {
      String sFk_depmethod = ((Vector)vExpression.elementAt(i)).elementAt(0).toString();

      String sAmountExpression = ((Vector)vExpression.elementAt(i)).elementAt(1).toString();

      String sRateExpression = ((Vector)vExpression.elementAt(i)).elementAt(2).toString();

      StringTokenizer st1 = new StringTokenizer(sAmountExpression, delim);

      StringTokenizer st2 = new StringTokenizer(sRateExpression, delim);

      boolean flag = false;
      String sWorkloanCode = "monthworkloan";
      while (st1.hasMoreTokens()) {
        if (st1.nextToken().equalsIgnoreCase(sWorkloanCode)) {
          flag = true;
        }
      }

      while ((!flag) && (st2.hasMoreTokens())) {
        if (st2.nextToken().equalsIgnoreCase(sWorkloanCode)) {
          flag = true;
        }
      }

      if (flag) {
        vResult.addElement(sFk_depmethod);
      }
    }
    vResult.trimToSize();
    return vResult;
  }

  private Vector getWorkamountOld(String sDwbm, String sYear, String sMonth)
  {
    String fk_accbook = getAccBook();

    Vector vWorkloanCard = new Vector(0);

    String sNextYear = sYear; String sNextMonth = sMonth;

    AccperiodVO ap = getPeriodSrv().getNextPeriod(sYear, sMonth);

    sNextYear = ap.getAccyear();
    sNextMonth = ap.getAccmonth();
    Vector vWhere = new Vector();

    String[][] sWhere = { { "fa_card.pk_corp", "=", "'" + sDwbm + "'" }, { "fa_cardhistory.laststate_flag", "=", "0" }, { "fa_cardhistory.accyear", "=", "'" + sNextYear + "'" }, { "fa_cardhistory.period", "=", "'" + sNextMonth + "'" }, { "fa_cardhistory.newasset_flag", ">=", "10" }, { "fa_cardhistory.depamount", "!=", "0" } };

    vWhere = ShareBase.getVector(sWhere);

    Vector vUsedDepmethod = null;
    try {
      vUsedDepmethod = FAProxy.getRemoteDepProcess().queryCardByDepmethod_Dep(sDwbm, sYear, sMonth, fk_accbook);
    }
    catch (Throwable e) {
      e.printStackTrace();
      vUsedDepmethod = new Vector(0);
    }

    Vector vArrDepmethod = getWorkloanMethod(vUsedDepmethod);
    String sMethod = "";
    for (int i = 0; i < vArrDepmethod.size(); i++) {
      sMethod = sMethod + ",'" + vArrDepmethod.elementAt(i).toString().trim() + "'";
    }

    String[] sElements = { "fa_cardhistory.fk_depmethod", "in", "('00000000000000000004'" + sMethod + ") " };

    vWhere.addElement(ShareBase.getVector(sElements));
    try {
      vWorkloanCard = FAProxy.getRemoteCard().findCalValuesByVector(Enum0301.getFields1(), vWhere);
    }
    catch (Throwable e)
    {
      e.printStackTrace();
      vWorkloanCard = new Vector(0);
    }
    return vWorkloanCard;
  }

  public Vector getDataFromCardAllVO(CardAllVO[] cavos)
  {
    return ((DepdetailUI)getBillManageUI()).getDataFromCardAllVO(cavos);
  }

  private UFDouble getUFDouble(Object obj, int iScale)
  {
    return ((DepdetailUI)getBillManageUI()).getUFDouble(obj, iScale);
  }

  public Vector getWorkLoanData(Vector vWorkloanCard, Vector vLastMonthWorkloan)
  {
    if ((vWorkloanCard == null) || (vWorkloanCard.size() <= 0)) {
      return null;
    }
    Hashtable hLastMonthWorkloan = new Hashtable();
    if ((vLastMonthWorkloan != null) && (vLastMonthWorkloan.size() > 0)) {
      for (int i = 0; i < vLastMonthWorkloan.size(); i++) {
        Vector vT = (Vector)vLastMonthWorkloan.elementAt(i);
        hLastMonthWorkloan.put(vT.elementAt(0), vT.elementAt(1));
      }
    }
    Vector vData1 = new Vector();
    Vector vTemp1 = new Vector();
    UFDouble ufd = null;
    for (int i = 0; i < vWorkloanCard.size(); i++) {
      vTemp1 = (Vector)vWorkloanCard.elementAt(i);

      ufd = new UFDouble(vTemp1.elementAt(3).toString(), -1 * this.m_iQuantityScale);

      vTemp1.setElementAt(ufd, 3);

      UFDouble ufdTemp = new UFDouble(0).setScale(-1 * this.m_iQuantityScale, 4);

      String sCardPkey = vTemp1.elementAt(8).toString();
      if (hLastMonthWorkloan.containsKey(sCardPkey)) {
        ufdTemp = new UFDouble(hLastMonthWorkloan.get(sCardPkey).toString(), -1 * this.m_iQuantityScale);
      }

      vTemp1.setElementAt(ufdTemp, 4);

      ufd = new UFDouble(vTemp1.elementAt(5).toString(), -1 * this.m_iQuantityScale);

      vTemp1.setElementAt(ufd, 5);

      ufd = new UFDouble(vTemp1.elementAt(6).toString(), -1 * this.m_iQuantityScale);

      vTemp1.setElementAt(ufd, 6);

      double dAllworkloan = new Double(vTemp1.elementAt(3).toString()).doubleValue();

      double dAccuworkloan = new Double(vTemp1.elementAt(6).toString()).doubleValue();

      vTemp1.setElementAt(new UFDouble(dAllworkloan - dAccuworkloan, -1 * this.m_iQuantityScale), 7);

      vData1.addElement(vTemp1.clone());
    }

    return vData1;
  }

  private Vector procAllUnitWKData(Vector allData, Vector monthData, String pk_corp)
  {
    if ((monthData == null) || (monthData.size() == 0)) {
      return allData;
    }
    CorpVO corp = (CorpVO)this.m_hmCorpMap.get(pk_corp);
    String unitname = corp.getUnitcode() + "  " + corp.getUnitname();

    for (int i = 0; i < monthData.size(); i++) {
      Vector tmp = null;
      tmp = (Vector)monthData.elementAt(i);
      if (tmp != null) {
        tmp.insertElementAt(unitname, 0);
        allData.addElement(tmp);
      }
    }

    return allData;
  }

  private void processJZZB(String dwbm, CardAllVO[] cavos)
  {
    try
    {
      String tmp = FaPublicPara.getParamter(dwbm, FaPublicPara.IS_EFFECT_DEPR, getAccBook());

      boolean flag = false;
      if (tmp.equals("Y")) {
        flag = true;
      }
      if (!flag) {
        UFDouble u0 = new UFDouble(0);
        for (int i = 0; i < cavos.length; i++)
          cavos[i].getCardhistoryVO().setPredevaluate(u0);
      }
    }
    catch (Throwable e) {
      e.printStackTrace();
    }
  }

  private void setTableModel(BillModel bm, String tabname)
  {
    ((DepdetailUI)getBillManageUI()).setTableModel(bm, tabname);
  }

  private void setEnabled(int i, boolean bl) {
    ((DepdetailUI)getBillManageUI()).setEnabled(i, bl);
  }

  public BillTabbedPane getUITabbedPane1()
  {
    return ((DepdetailUI)getBillManageUI()).getBodyTabbedPane();
  }

  public BillModel getFaTableModel2()
  {
    return getBillListPanel().getBodyScrollPane(this.depvo.getTableCodes()[1]).getTableModel();
  }

  public BillModel getFaTableModel1()
  {
    return getBillListPanel().getBodyScrollPane(this.depvo.getTableCodes()[0]).getTableModel();
  }

  public BillModel getFaTableModel3()
  {
    return getBillListPanel().getBodyScrollPane(this.depvo.getTableCodes()[2]).getTableModel();
  }
}