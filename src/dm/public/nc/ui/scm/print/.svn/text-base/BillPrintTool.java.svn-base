package nc.ui.scm.print;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.print.PrintEntry;
import nc.ui.scm.pub.print.SCMPrintDataInterface;
import nc.vo.bd.CorpVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.scm.print.ScmPrintlogVO;
import nc.vo.sm.UserVO;

public class BillPrintTool
{
  private PrintEntry m_printEntry = null;

  private Class m_cDataSource = null;

  private BillData m_bd = null;

  private String m_sModuleCode = null;

  private String m_pk_corp = null;

  private String m_sUserID = null;

  private String m_sNameofBillCodeItem = null;

  private String m_sNameofBillIDItem = null;

  private ArrayList m_alBills = null;

  private HashMap m_hmId2VO = null;

  private HashMap m_hmId2Row = null;

  private BillListPanel m_ListPanel = null;

  private BillCardPanel m_CardPanel = null;

  private AggregatedValueObject m_voOperated = null;

  private boolean m_bIsCard = false;

  private String m_strPrintMsg = "";

  public BillPrintTool(String sModuleCode, ArrayList alBill, BillData bd, Class specialDataSource, String pk_corp, String sUserID, String sNameofBillCodeItem, String sNameofBillIDItem)
    throws BusinessException, InstantiationException, IllegalAccessException
  {
    if ((sModuleCode == null) || (sNameofBillIDItem == null) || (alBill == null) || (bd == null) || ((specialDataSource != null) && (!(specialDataSource.newInstance() instanceof SCMPrintDataInterface))))
    {
      throw new BusinessException(NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000391"));
    }
    this.m_bd = bd;

    this.m_cDataSource = specialDataSource;

    this.m_alBills = alBill;

    this.m_sModuleCode = sModuleCode;

    this.m_pk_corp = (pk_corp != null ? pk_corp : ClientEnvironment.getInstance().getCorporation().getPrimaryKey());

    this.m_sUserID = (sUserID != null ? sUserID : ClientEnvironment.getInstance().getUser().getPrimaryKey());

    if ((sNameofBillCodeItem != null) && (sNameofBillCodeItem.length() != 0)) {
      this.m_sNameofBillCodeItem = sNameofBillCodeItem;
    }
    else {
      this.m_sNameofBillCodeItem = "vbillcode";
    }

    this.m_sNameofBillIDItem = sNameofBillIDItem;
  }

  private SCMPrintDataInterface getNewDataSource()
    throws BusinessException, InstantiationException, IllegalAccessException
  {
    SCMPrintDataInterface dataSource = null;

    if (this.m_cDataSource != null) {
      dataSource = (SCMPrintDataInterface)this.m_cDataSource.newInstance();
    }
    else
    {
      dataSource = new SCMPrintDataInterface();
    }
    dataSource.setBillData(this.m_bd);
    dataSource.setModuleName(this.m_sModuleCode);
    dataSource.setTotalLinesInOnePage(getPrintEntry().getBreakPos());

    dataSource.setIsNeedSpaceRowInOneVO(false);

    return dataSource;
  }

  private PrintEntry getPrintEntry()
  {
    if (this.m_printEntry == null) {
      this.m_printEntry = new PrintEntry(null, null);
      this.m_printEntry.setTemplateID(this.m_pk_corp, this.m_sModuleCode, this.m_sUserID, null);
      getPrintEntry().selectTemplate();
    }
    return this.m_printEntry;
  }

  private HashMap mapId2VO(ArrayList alVOs)
    throws BusinessException
  {
    HashMap map = new HashMap();
    int i = 0; for (int j = alVOs.size(); i < j; i++) {
      AggregatedValueObject vo = (AggregatedValueObject)alVOs.get(i);
      String sBillID = vo.getParentVO().getPrimaryKey();
      map.put(sBillID, vo);
    }
    return map;
  }

  private HashMap mapId2Row(ArrayList alVOs)
    throws BusinessException
  {
    HashMap hm = new HashMap();
    if (this.m_ListPanel != null) {
      int iRowCount = this.m_ListPanel.getHeadBillModel().getRowCount();
      for (int rowIndex = 0; rowIndex < iRowCount; rowIndex++) {
        hm.put(this.m_ListPanel.getHeadBillModel().getValueAt(rowIndex, this.m_sNameofBillIDItem), new Integer(rowIndex));
      }

    }

    return hm;
  }

  public void onBatchPrint(BillListPanel listPanel, String billtypecode)
    throws BusinessException, InstantiationException, IllegalAccessException, InterruptedException
  {
    if ((listPanel == null) || (billtypecode == null) || ((this.m_alBills == null) && (this.m_alBills.size() == 0)))
    {
      System.out.println("BillPrintTool-onBatchPrint:传入参数错误！");
      return;
    }
    this.m_bIsCard = false;
    this.m_ListPanel = listPanel;
    this.m_hmId2VO = mapId2VO(this.m_alBills);
    this.m_hmId2Row = mapId2Row(this.m_alBills);

    int billsCount = this.m_alBills.size();

    if (getPrintEntry().dsCountInPool() > 0) {
      this.m_strPrintMsg = PrintLogClient.getBatchPrintingMsg();
      return;
    }
    if (getPrintEntry().selectTemplate() < 0) {
      System.out.println("ERROR:no print templet....");
      return;
    }
    getPrintEntry().beginBatchPrint();
    PrintLogClient plc = new PrintLogClient();
    plc.addFreshTsListener(new FreshTsListener());
    plc.setBatchPrint(true);

    getPrintEntry().setPrintListener(plc);

    SCMPrintDataInterface dataSource = null;
    AggregatedValueObject vo = null;
    for (int i = 0; i < billsCount; i++)
    {
      vo = (AggregatedValueObject)this.m_alBills.get(i);
      if (vo == null)
      {
        continue;
      }
      ScmPrintlogVO voaSpl = getScmPrintlogVO(billtypecode, vo);
      plc.setPrintInfo(voaSpl);
      try {
        if (plc.check()) {
          dataSource = getNewDataSource();
          dataSource.setVO(vo);
          getPrintEntry().setDataSource(dataSource);
        }
      } catch (Exception e) {
        System.out.println("打印出现异常" + e.getMessage());
      }
    }

    getPrintEntry().endBatchPrint();
    this.m_strPrintMsg = plc.getPrintResultMsg(false);
  }

  public void onBatchPrintPreview(BillListPanel listPanel, String billtypecode)
    throws BusinessException, InstantiationException, IllegalAccessException, InterruptedException
  {
    print(null, listPanel, billtypecode, true, false);
  }

  public void onCardPrint(BillCardPanel cardPanel, BillListPanel listPanel, String billtypecode)
    throws BusinessException, InstantiationException, IllegalAccessException
  {
    print(cardPanel, listPanel, billtypecode, false, true);
  }

  public void onCardPrintPreview(BillCardPanel cardPanel, BillListPanel listPanel, String billtypecode)
    throws BusinessException, InstantiationException, IllegalAccessException
  {
    print(cardPanel, listPanel, billtypecode, true, true);
  }

  private ScmPrintlogVO getScmPrintlogVO(String billtypecode, AggregatedValueObject vo)
    throws BusinessException
  {
    String cbillid = vo.getParentVO().getPrimaryKey();
    String vbillcode = (String)vo.getParentVO().getAttributeValue(this.m_sNameofBillCodeItem);
    Object ts = vo.getParentVO().getAttributeValue("ts");

    ScmPrintlogVO voaSpl = new ScmPrintlogVO();
    voaSpl.setCbillid(cbillid);
    voaSpl.setVbillcode(vbillcode);
    voaSpl.setCbilltypecode(billtypecode);
    voaSpl.setCoperatorid(this.m_sUserID);
    voaSpl.setIoperatetype(new Integer(0));
    voaSpl.setPk_corp(this.m_pk_corp);
    voaSpl.setTs(ts != null ? ts.toString() : "");
    return voaSpl;
  }

  private void print(BillCardPanel cardPanel, BillListPanel listPanel, String billtypecode, boolean isPreview, boolean isCard)
    throws BusinessException, InstantiationException, IllegalAccessException
  {
    if (isCard) {
      if ((cardPanel == null) || (billtypecode == null)) {
        System.out.println("BillPrintTool-onCardPrintPreView: 传入参数错误！");
        return;
      }

    }
    else if ((listPanel == null) || (billtypecode == null)) {
      System.out.println("BillPrintTool-onCardPrintPreView: 传入参数错误！");
      return;
    }

    this.m_bIsCard = isCard;

    this.m_CardPanel = cardPanel;
    this.m_ListPanel = listPanel;

    this.m_hmId2VO = mapId2VO(this.m_alBills);
    this.m_hmId2Row = mapId2Row(this.m_alBills);

    this.m_voOperated = ((AggregatedValueObject)this.m_alBills.get(0));

    PrintLogClient plc = new PrintLogClient();

    ScmPrintlogVO voaSpl = getScmPrintlogVO(billtypecode, this.m_voOperated);

    FreshTsListener freshTsListener = new FreshTsListener();

    plc.addFreshTsListener(freshTsListener);

    getPrintEntry().setPrintListener(plc);//弹出选择框

    plc.setPrintInfo(voaSpl);

    plc.setPrintEntry(getPrintEntry());

    SCMPrintDataInterface dataSource = getNewDataSource();
    dataSource.setVO(this.m_voOperated);
    getPrintEntry().setDataSource(dataSource);
    if(getPrintEntry().selectTemplate()>1)
    {
    	getPrintEntry().selectTemplate();
    }
    if (isPreview) {
      getPrintEntry().preview();
      this.m_strPrintMsg = plc.getPrintResultMsg(true);
    }
    else {
      getPrintEntry().print();
      this.m_strPrintMsg = plc.getPrintResultMsg(false);
    }
  }

  public String getPrintMessage()
  {
    return this.m_strPrintMsg;
  }

  public class FreshTsListener implements IFreshTsListener
  {
    public FreshTsListener()
    {
    }

    public void freshTs(String sBillID, String sTS, Integer iPrintCount)
    {
      System.out.println("new Ts = " + sTS);
      System.out.println("new iPrintCount = " + iPrintCount);

      AggregatedValueObject vo = null;
      if (BillPrintTool.this.m_bIsCard) {
        vo = BillPrintTool.this.m_voOperated;

        vo.getParentVO().setAttributeValue("ts", sTS);
        vo.getParentVO().setAttributeValue("iprintcount", iPrintCount);

        BillPrintTool.this.m_CardPanel.setHeadItem("ts", sTS);
        BillPrintTool.this.m_CardPanel.setTailItem("iprintcount", iPrintCount);
        Integer rowNum = (Integer)BillPrintTool.this.m_hmId2Row.get(sBillID);
        if (rowNum != null) {
          BillPrintTool.this.m_ListPanel.getHeadBillModel().setValueAt(iPrintCount, rowNum.intValue(), "iprintcount");
        }

      }
      else
      {
        vo = (AggregatedValueObject)BillPrintTool.this.m_hmId2VO.get(sBillID);

        vo.getParentVO().setAttributeValue("ts", sTS);
        vo.getParentVO().setAttributeValue("iprintcount", iPrintCount);

        Integer rowNum = (Integer)BillPrintTool.this.m_hmId2Row.get(sBillID);
        if (rowNum == null) {
          System.out.println("程序bug:BillPrintTool$FreshTsListener.freshTs中发现要打印的单据数据和界面数据对应错误,打印中断");
          return;
        }
        BillPrintTool.this.m_ListPanel.getHeadBillModel().setValueAt(iPrintCount, rowNum.intValue(), "iprintcount");
      }
    }
  }
}