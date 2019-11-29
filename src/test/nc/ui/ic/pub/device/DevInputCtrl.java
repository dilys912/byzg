package nc.ui.ic.pub.device;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import nc.ui.bd.ref.BatchMatchContext;
import nc.ui.ic.pub.bill.InvoInfoBYFormula;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIFileChooser;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.scm.pub.CacheTool;
import nc.ui.scm.pub.report.BillRowNo;
import nc.vo.ic.pub.BillTypeConst;
import nc.vo.ic.pub.GenMethod;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.bill.SpecialBillItemVO;
import nc.vo.ic.pub.device.DevDataFormulaVO;
import nc.vo.ic.pub.device.DevFileFmtVO;
import nc.vo.ic.pub.device.DevInputHeadVO;
import nc.vo.ic.pub.device.DevInputVO;
import nc.vo.ic.pub.locator.LocatorVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVOUtil;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.ic.bill.FreeVO;
import nc.vo.scm.ic.bill.InvVO;
import nc.vo.scm.pub.SCMEnv;

public class DevInputCtrl
  implements DevInputListener
{
  protected BillCardPanel m_pCard = null;
  protected BillListPanel m_pList = null;

  protected ToftPanel m_tp = null;
  protected int m_iMode = 3;

  protected AggregatedValueObject m_voBill = null;

  protected DevFileFmtVO[] m_voaFmt = null;

  protected DevDataFormulaVO[] m_devDataFormulaVO = null;

  protected String m_pk_corp = null;

  protected String m_sBillTypeCode = BillTypeConst.m_purchaseIn;
  public final int BC_GEN = 1000;
  public final int BC_SPEC = 2000;
  public final int BC_ALLOC = 3000;

  protected int m_iBillClass = 1000;

  protected String m_sDevDataFilePath = null;
  protected String m_sDevFileFmtName = null;

  protected String m_sDevDFormulaFmtName = null;
  protected String m_sDevFileYype = null;
  protected String m_sDevFnStart = null;
  protected String m_sDevReadFrq = null;
  protected String m_devfuncdef = null;
  public static final int ACT_ADD_ITEM = 0;
  public static final int ACT_CHECK_ITEM = 1;
  protected int m_iStartItem = -1;
  protected UIFileChooser m_InFileDialog = null;

  InvoInfoBYFormula m_invoInfoBYFormula = null;

  protected Hashtable m_htbGolbal = null;
  protected String m_sWarehouseidFieldName;
  public static final int ACT_UPDATE_ITEM = 2;
  public boolean m_bWhMan = false;
  public String m_sWarehouseNameFieldName;
  public String[] sItemNumKeys = { "ninnum", "noutnum", "dbizdate" };
  private AggregatedValueObject m_voBillUI;

  protected void checkItems(DevInputVO[] voaDevIn)
  {
    if ((voaDevIn == null) || (voaDevIn.length == 0)) {
      SCMEnv.out("no data.");
      return;
    }

    GeneralBillItemVO[] voaFileItem = new GeneralBillItemVO[voaDevIn.length];

    for (int i = 0; i < voaDevIn.length; i++) {
      voaFileItem[i] = new GeneralBillItemVO();
      voaDevIn[i].copy(voaFileItem[i]);
    }

    GeneralBillItemVO[] voaCurItem = getBodyVOs();
    GeneralBillVO vo = new GeneralBillVO();
    GeneralBillHeaderVO voHeader = new GeneralBillHeaderVO();

    voHeader.setCbilltypecode(this.m_sBillTypeCode);
    vo.setParentVO(voHeader);
    GeneralBillItemVO[] voaCompResultItem = vo.compare(voaFileItem, voaCurItem);

    SCMEnv.out("check ok");
    showCheckMessage(voaCompResultItem);
  }

  public void formulaVO(DevInputVO[] devInputVOs)
  {
    ArrayList alFormula = new ArrayList();
    if ((this.m_devDataFormulaVO == null) || (this.m_devDataFormulaVO.length == 0)) {
      return;
    }
    String sTable = null;
    String sKeyfield = null;
    String sKeyfieldValueName = null;
    String sSearchfield = null;

    String keyfieldValue = null;
    String sSearchfieldVName = null;
    String[] arykeyfieldValue = null;
    String[] aryKeyfieldValueName = null;

    String sBDSql = null;
    for (int i = 0; i < this.m_devDataFormulaVO.length; i++)
    {
      sTable = (String)this.m_devDataFormulaVO[i].getAttributeValue("table");
      sKeyfield = (String)this.m_devDataFormulaVO[i].getAttributeValue("keyfield");
      sKeyfieldValueName = (String)this.m_devDataFormulaVO[i].getAttributeValue("keyfieldValueName");

      arykeyfieldValue = getArrayStr(sKeyfield, ",");
      aryKeyfieldValueName = getArrayStr(sKeyfieldValueName, ",");

      sSearchfield = (String)this.m_devDataFormulaVO[i].getAttributeValue("searchfield");
      sSearchfieldVName = (String)this.m_devDataFormulaVO[i].getAttributeValue("searchfieldVName");

      String sKeyfieldValueNameOne = null;
      String sKeyfieldOne = null;

      for (int n = 0; n < devInputVOs.length; n++)
      {
        if ((sSearchfieldVName == null) || (sTable == null) || (sKeyfield == null) || (sKeyfieldValueName == null) || (sSearchfield == null))
        {
          continue;
        }

        sBDSql = "";
        for (int j = 0; j < arykeyfieldValue.length; j++)
        {
          sKeyfieldOne = arykeyfieldValue[j];
          sKeyfieldValueNameOne = aryKeyfieldValueName[j];

          if (this.m_htbGolbal.containsKey(sKeyfieldValueNameOne)) {
            try {
              keyfieldValue = (String)this.m_htbGolbal.get(sKeyfieldValueNameOne);
            }
            catch (Exception e) {
              SCMEnv.out(e.toString());
            }
          }
          else {
            keyfieldValue = (String)devInputVOs[n].getAttributeValue(aryKeyfieldValueName[j]);
          }

          if (keyfieldValue == null)
          {
            sBDSql = null;
            break;
          }
          if (j != 0)
          {
            sBDSql = sBDSql + " and ";
          }
          if ("pk_corp".equalsIgnoreCase(sKeyfieldOne))
          {
            sBDSql = sBDSql + "(" + sKeyfieldOne + " = '" + keyfieldValue + "' or pk_corp='0001')";
          }
          else {
            sBDSql = sBDSql + sKeyfieldOne + " = '" + keyfieldValue + "'";
          }
        }
        if (sBDSql == null)
        {
          continue;
        }

        SCMEnv.out(sBDSql);

        Object[][] objValue = CacheTool.getAnyValue2(sTable, sSearchfield, sBDSql);

        if ((objValue != null) && (objValue.length != 0))
        {
          devInputVOs[n].setAttributeValue(sSearchfieldVName, objValue[0][0]);
          SCMEnv.out(sSearchfieldVName + ":" + (String)objValue[0][0]);
          if (sSearchfieldVName.startsWith("h_"))
            this.m_htbGolbal.put(sSearchfieldVName, objValue[0][0]);
        }
        else
        {
          SCMEnv.out("not result !");
        }

      }

    }

    int xxx = 0;
  }

  public String[] getArrayStr(String sSource, String sSplit)
  {
    String[] sResult = null;
    try {
      ArrayList allist = new ArrayList();
      if ((sSplit == null) || (sSplit.trim().length() == 0))
        sSplit = ",";
      if (sSource == null)
        return null;
      if (sSource.indexOf(sSplit) == 0)
      {
        sResult = new String[1];
        sResult[0] = sSource;
      }
      else
      {
        StringTokenizer tokens = new StringTokenizer(sSource, sSplit);
        while (tokens.hasMoreTokens())
          allist.add(tokens.nextToken());
        if (allist.size() > 0)
        {
          sResult = new String[allist.size()];
          allist.toArray(sResult);
        }
        else
        {
          sResult = new String[1];
          sResult[0] = sSource;
        }
      }
    }
    catch (Exception e)
    {
      SCMEnv.out(e.toString());
    }
    return sResult;
  }

  public String getBillTypeCode()
  {
    return this.m_sBillTypeCode;
  }

  public AggregatedValueObject getBillVO()
  {
    return this.m_voBill;
  }

  protected GeneralBillItemVO[] getBodyVOs()
  {
    if (this.m_voBill == null) {
      SCMEnv.out("============ fatal set vo first ......");
      return null;
    }

    GeneralBillItemVO[] voaItemForFree = ((GeneralBillVO)this.m_voBill).getItemVOs();

    int rowCount = getCard().getRowCount();

    GeneralBillItemVO[] voaBody = new GeneralBillItemVO[rowCount];
    for (int line = 0; line < rowCount; line++)
      voaBody[line] = new GeneralBillItemVO();
    getCard().getBillModel().getBodyValueVOs(voaBody);

    for (int i = 0; i < rowCount; i++)
      voaBody[i].setFreeItemVO(voaItemForFree[i].getFreeItemVO());
    return voaBody;
  }

  public BillCardPanel getCard()
  {
    return this.m_pCard;
  }

  public String getDevDataFilePath()
  {
    return this.m_sDevDataFilePath;
  }

  public String getDevDFormulaFmtName()
  {
    return this.m_sDevDFormulaFmtName;
  }

  public String getDevFileFmtName()
  {
    return this.m_sDevFileFmtName;
  }

  public String getDevFileYype()
  {
    return this.m_sDevFileYype;
  }

  public String getDevFnStart()
  {
    return this.m_sDevFnStart;
  }

  public String getDevfuncdef()
  {
    return this.m_devfuncdef;
  }

  public String getDevReadFrq()
  {
    return this.m_sDevReadFrq;
  }

  public UIFileChooser getFileChooseDlg()
  {
    try
    {
      if (this.m_InFileDialog == null) {
        this.m_InFileDialog = new UIFileChooser();
        this.m_InFileDialog.setDialogType(0);

        this.m_InFileDialog.removeChoosableFileFilter(this.m_InFileDialog.getFileFilter());

        if (this.m_sDevDataFilePath != null) {
          this.m_InFileDialog.setCurrentDirectory(new File(this.m_sDevDataFilePath));
        }

      }

    }
    catch (Exception ex)
    {
      SCMEnv.error(ex);
    }
    return this.m_InFileDialog;
  }

  protected DevInputHeadVO getHeadVO(DevInputVO[] devInputVO)
    throws BusinessException
  {
    DevInputHeadVO devInputHeadVO = new DevInputHeadVO();

    this.m_htbGolbal = new Hashtable();

    UIRefPane refpane = (UIRefPane)getCard().getHeadItem(getWarehouseidFieldName()).getComponent();

    if (refpane != null)
    {
      String sWarehouseid = refpane.getRefPK();
      String sWarehousename = refpane.getRefName();
      if (sWarehouseid != null)
      {
        devInputHeadVO.setAttributeValue(getWarehouseidFieldName(), sWarehouseid);
        devInputHeadVO.setAttributeValue(getWarehouseNameFieldName(), sWarehousename);
        this.m_htbGolbal.put("h_" + getWarehouseidFieldName(), sWarehouseid);
      }

    }

    for (int i = 0; i < devInputVO.length; i++)
    {
      if (devInputVO[i].getStatus() != 0)
        continue;
      devInputHeadVO = getItemToHeadVO(devInputVO[i], devInputHeadVO);
      break;
    }

    devInputHeadVO.setAttributeValue("pk_corp", getPk_corp());
    this.m_htbGolbal.put("h_pk_corp", getPk_corp());

    DevInputHeadVO headOldVO = new DevInputHeadVO();
    getCard().getBillData().getHeaderValueVO(headOldVO);
    if (headOldVO != null)
    {
      String[] aryHeadItemNames = devInputHeadVO.getAttributeNames();
      int iLen = aryHeadItemNames.length;
      for (int i = 0; i < iLen; i++)
      {
        headOldVO.setAttributeValue(aryHeadItemNames[i], devInputHeadVO.getAttributeValue(aryHeadItemNames[i]));
      }
    }
    return headOldVO;
  }

  protected DevInputHeadVO getItemToHeadVO(DevInputVO devInputVO)
  {
    String sItemVoName = null;
    String sHeadVoName = null;
    Object sHeadVoValue = null;
    DevInputHeadVO devInputHeadVO = new DevInputHeadVO();
    String[] sItemVONames = devInputVO.getAttributeNames();
    if ((sItemVONames != null) && (sItemVONames.length > 0))
    {
      for (int i = 0; i < sItemVONames.length; i++) {
        sItemVoName = sItemVONames[i];
        if (sItemVoName.startsWith("h_"))
          sHeadVoName = sItemVoName.substring(2, sItemVoName.length());
        else
          sHeadVoName = sItemVoName;
        sHeadVoValue = devInputVO.getAttributeValue(sItemVoName);
        devInputHeadVO.setAttributeValue(sHeadVoName, sHeadVoValue);
      }
    }
    return devInputHeadVO;
  }

  protected DevInputVO[] getItemVO(DevInputVO[] devInputVO)
  {
    ArrayList aryItemVO = new ArrayList();
    for (int i = 0; i < devInputVO.length; i++)
    {
      if (devInputVO[i].getStatus() != 1)
        continue;
      aryItemVO.add(devInputVO[i]);
    }

    if ((aryItemVO != null) && (aryItemVO.size() > 0))
    {
      DevInputVO[] devInputVOs = new DevInputVO[aryItemVO.size()];
      aryItemVO.toArray(devInputVOs);
      return devInputVOs;
    }
    return null;
  }

  protected int getLastFilledRowNum()
  {
    int iLineCount = getCard().getBillTable().getRowCount();
    int i = iLineCount - 1;
    while ((i >= 0) && 
      (getCard().getBillModel().getValueAt(i, "cinventorycode") == null)) {
      i--;
    }

    return i;
  }

  public BillListPanel getList()
  {
    return this.m_pList;
  }

  public int getMode()
  {
    return this.m_iMode;
  }

  public String getPk_corp()
  {
    return this.m_pk_corp;
  }

  public int getStartItem()
  {
    return this.m_iStartItem;
  }

  public ToftPanel getTp()
  {
    return this.m_tp;
  }

  public void onDevInput(DevInputEvent e)
  {
    if (((this.m_iMode == 0) || (this.m_iMode == 2)) && (getCard() != null))
    {
      int iCurLine = 0;
      iCurLine = getCard().getBillTable().getSelectedRow();
      if (iCurLine >= 0);
      getCard().getBillModel().setValueAt(NCLangRes.getInstance().getStrByID("4008other", "UPP4008other-000437"), iCurLine - 1, "invname");
    }
  }

  public void readFileFmt()
    throws BusinessException, Exception
  {
    DevFileFmtXMLParser xp = new DevFileFmtXMLParser();
    this.m_voaFmt = xp.getDocData(this.m_sDevFileFmtName);

    XMLParser xpDataFormula = new XMLParser();
    DevDataFormulaVO devDataFormulaVO = new DevDataFormulaVO();
    ArrayList al = xpDataFormula.getDocData(this.m_sDevDFormulaFmtName, devDataFormulaVO);

    if ((al != null) && (al.size() > 0))
    {
      this.m_devDataFormulaVO = new DevDataFormulaVO[al.size()];
      al.toArray(this.m_devDataFormulaVO);
    }
  }

  public void readFileFmt(String sFileName)
  {
    DevFileFmtXMLParser xp = new DevFileFmtXMLParser();
    this.m_voaFmt = xp.getDocData(sFileName);
  }

  public DevInputVO[] readTxtFile(String sFileName)
    throws BusinessException, Exception
  {
    if (this.m_voaFmt == null)
      readFileFmt();
    DevInputTxtParser tp = new DevInputTxtParser();
    tp.setRule(this.m_voaFmt);
    return tp.getParsedTxtDoc(sFileName);
  }

  public DevInputVO[] readTxtFile(String sFileName, ArrayList alReuslt)
    throws BusinessException, Exception
  {
    if (this.m_voaFmt == null)
      readFileFmt();
    DevInputTxtParser tp = new DevInputTxtParser();
    tp.setRule(this.m_voaFmt);
    DevInputVO[] devInputVO = tp.getParsedTxtDoc(sFileName);

    alReuslt.add(new Integer(tp.getRuleIndex()));
    return devInputVO;
  }

  public void setBillTypeCode(String newBillTypeCode)
  {
    if (newBillTypeCode != null) {
      this.m_sBillTypeCode = newBillTypeCode;
      if ((this.m_sBillTypeCode.equals(BillTypeConst.m_initIn)) || (this.m_sBillTypeCode.equals(BillTypeConst.m_initBorrow)) || (this.m_sBillTypeCode.equals(BillTypeConst.m_initWaster)) || (this.m_sBillTypeCode.equals(BillTypeConst.m_initEntrustMachining)) || (this.m_sBillTypeCode.equals(BillTypeConst.m_purchaseIn)) || (this.m_sBillTypeCode.equals(BillTypeConst.m_productIn)) || (this.m_sBillTypeCode.equals(BillTypeConst.m_consignMachiningIn)) || (this.m_sBillTypeCode.equals(BillTypeConst.m_entrustMachiningIn)) || (this.m_sBillTypeCode.equals(BillTypeConst.m_borrowIn)) || (this.m_sBillTypeCode.equals(BillTypeConst.m_otherIn)) || (this.m_sBillTypeCode.equals(BillTypeConst.m_lendIn)) || (this.m_sBillTypeCode.equals(BillTypeConst.m_purchaseInit)) || (this.m_sBillTypeCode.equals(BillTypeConst.m_saleOut)) || (this.m_sBillTypeCode.equals(BillTypeConst.m_materialOut)) || (this.m_sBillTypeCode.equals(BillTypeConst.m_consignMachiningOut)) || (this.m_sBillTypeCode.equals(BillTypeConst.m_entrustMachiningOut)) || (this.m_sBillTypeCode.equals(BillTypeConst.m_lendOut)) || (this.m_sBillTypeCode.equals(BillTypeConst.m_otherOut)) || (this.m_sBillTypeCode.equals(BillTypeConst.m_borrowOut)) || (this.m_sBillTypeCode.equals(BillTypeConst.m_discardOut)) || (this.m_sBillTypeCode.equals(BillTypeConst.m_wasterProcess)) || (this.m_sBillTypeCode.equals(BillTypeConst.m_initLend)))
      {
        this.m_iBillClass = 1000;
      }
      else if ((this.m_sBillTypeCode.equals(BillTypeConst.m_check)) || (this.m_sBillTypeCode.equals(BillTypeConst.m_transfer)) || (this.m_sBillTypeCode.equals(BillTypeConst.m_disassembly)) || (this.m_sBillTypeCode.equals(BillTypeConst.m_assembly)) || (this.m_sBillTypeCode.equals(BillTypeConst.m_spaceAdjust)) || (this.m_sBillTypeCode.equals(BillTypeConst.m_transform)))
      {
        this.m_iBillClass = 2000;
      } else if (this.m_sBillTypeCode.equals(BillTypeConst.m_AllocationOrder))
        this.m_iBillClass = 3000;
    }
  }

  public void setBillVO(AggregatedValueObject newM_voBill)
  {
    this.m_voBill = newM_voBill;
  }

  public void setCard(BillCardPanel newM_pCard)
  {
    this.m_pCard = newM_pCard;
  }

  public void setDevDataFilePath(String newDevDataFilePath)
  {
    this.m_sDevDataFilePath = newDevDataFilePath;
  }

  public void setDevDFormulaFmtName(String newDevFileFmtName)
  {
    this.m_sDevDFormulaFmtName = newDevFileFmtName;
  }

  public void setDevFileFmtName(String newDevFileFmtName)
  {
    this.m_sDevFileFmtName = newDevFileFmtName;
  }

  public void setDevFileYype(String newDevFileYype)
  {
    this.m_sDevFileYype = newDevFileYype;
  }

  public void setDevFnStart(String newDevFnStart)
  {
    this.m_sDevFnStart = newDevFnStart;
  }

  public void setDevfuncdef(String newM_devfuncdef)
  {
    this.m_devfuncdef = newM_devfuncdef;
  }

  public void setDevReadFrq(String newDevReadFrq)
  {
    this.m_sDevReadFrq = newDevReadFrq;
  }

  public void setList(BillListPanel newM_pList)
  {
    this.m_pList = newM_pList;
  }

  public void setMode(int newM_iMode)
  {
    this.m_iMode = newM_iMode;
  }

  public void setPk_corp(String newM_pk_corp)
  {
    this.m_pk_corp = newM_pk_corp;
  }

  public void setStartItem(int newStartItem)
  {
    this.m_iStartItem = newStartItem;
  }

  public void setTp(ToftPanel newM_tp)
  {
    this.m_tp = newM_tp;
  }

  public void setup()
    throws BusinessException
  {
    if (getCard() == null) {
      SCMEnv.out("set cardpanel first ....");
      return;
    }

    String[] saTemp = null;
    BillItem hi = null;

    String sdevdatafilepath = null; String sdevfilefmtname = null;
    String sdevfiletype = null; String sdevfnstart = null;
    String sdevreadfrq = null; String sdevfuncdef = null;
    String sdevfilefmtFormulaname = null;
    String[] saIk = { "devdatafilepath", "devfilefmtname", "devfiletype", "devfnstart", "devreadfrq", "devfuncdef" };

    String[] saValue = new String[saIk.length];

    for (int i = 0; i < saIk.length; i++) {
      hi = getCard().getHeadItem(saIk[i]);
      if (hi != null) {
        saTemp = hi.getEditFormulas();
        if ((saTemp != null) && (saTemp.length > 0) && (saTemp[0] != null) && (saTemp[0].trim().length() > 0))
        {
          saValue[i] = saTemp[0].trim();
        }
        else SCMEnv.out(" no definition." + saIk[i]); 
      }
      else {
        SCMEnv.out(" no definition." + saIk[i]);
      }
    }

    sdevdatafilepath = saValue[0];
    sdevfilefmtname = saValue[1];
    sdevfiletype = saValue[2];
    sdevfnstart = saValue[3];
    sdevreadfrq = saValue[4];
    sdevfuncdef = saValue[5];

    if (sdevfilefmtname == null) {
      throw new BusinessException(NCLangRes.getInstance().getStrByID("4008other", "UPP4008other-000169"));
    }
    if (sdevdatafilepath == null) {
      throw new BusinessException(NCLangRes.getInstance().getStrByID("4008other", "UPP4008other-000170"));
    }

    sdevfilefmtFormulaname = sdevfilefmtname.substring(0, sdevfilefmtname.length() - 4) + "formula" + ".xml";

    sdevdatafilepath = retractParam(sdevdatafilepath);
    sdevfilefmtname = retractParam(sdevfilefmtname);
    sdevfilefmtFormulaname = retractParam(sdevfilefmtFormulaname);
    sdevfiletype = retractParam(sdevfiletype);
    sdevfnstart = retractParam(sdevfnstart);
    sdevreadfrq = retractParam(sdevreadfrq);
    sdevfuncdef = retractParam(sdevfuncdef);

    setDevDataFilePath(sdevdatafilepath);
    setDevFileFmtName(sdevfilefmtname);

    setDevDFormulaFmtName(sdevfilefmtFormulaname);
    setDevFileYype(sdevfiletype);
    setDevFnStart(sdevfnstart);
    setDevReadFrq(sdevreadfrq);
    setDevfuncdef(sdevfuncdef);
  }

  private String retractParam(String sParam) {
    if (sParam == null) return null;
    sParam = sParam.trim();
    int iBeg = sParam.indexOf(">");
    String sret = sParam.substring(iBeg + 1);
    return sret;
  }

  protected void showCheckMessage(GeneralBillItemVO[] vos)
  {
    if ((vos != null) && (vos.length > 0)) {
      StringBuffer sbMsg = new StringBuffer();
      boolean bHave = false;

      for (int i = 0; i < vos.length; i++) {
        if ((vos[i] != null) && (vos[i].getStatus() == 2)) {
          if (!bHave) {
            sbMsg.append(NCLangRes.getInstance().getStrByID("4008other", "UPP4008other-000171"));
            bHave = true;
          }
          sbMsg.append(vos[i].getItemInfo());
          sbMsg.append("\n");
        }
      }
      bHave = false;
      for (int i = 0; i < vos.length; i++) {
        if ((vos[i] != null) && (vos[i].getStatus() == 1)) {
          if (!bHave) {
            sbMsg.append(NCLangRes.getInstance().getStrByID("4008other", "UPP4008other-000172"));
            bHave = true;
          }
          sbMsg.append(vos[i].getItemInfo());
          sbMsg.append("\n");
        }
      }
      bHave = false;
      for (int i = 0; i < vos.length; i++) {
        if ((vos[i] != null) && (vos[i].getStatus() == 3)) {
          if (!bHave) {
            sbMsg.append(NCLangRes.getInstance().getStrByID("4008other", "UPP4008other-000173"));
            bHave = true;
          }
          sbMsg.append(vos[i].getItemInfo());
          sbMsg.append("\n");
        }
      }
      if (sbMsg.toString().length() == 0) {
        sbMsg.append(NCLangRes.getInstance().getStrByID("4008other", "UPP4008other-000174"));
      }
      MessageDialog.showHintDlg(this.m_tp, NCLangRes.getInstance().getStrByID("4008other", "UPP4008other-000438"), sbMsg.toString());
    }
  }

  protected InvoInfoBYFormula getInvoFormula()
  {
    if (this.m_invoInfoBYFormula == null) {
      this.m_invoInfoBYFormula = new InvoInfoBYFormula();
    }
    return this.m_invoInfoBYFormula;
  }

  protected void setHead(DevInputHeadVO inputHeadVO)
  {
    setHeaderValueVO(inputHeadVO);
    getCard().getBillData().getHeadItem(getWarehouseidFieldName()).setEnabled(false);
  }

  protected void setItems(CircularlyAccessibleValueObject[] billItemVOs)
  {
    if (billItemVOs == null) {
      SCMEnv.out("no data.");
      return;
    }
    int iLastFilledRowNum = -1;
    int iOrginalLineCount = getCard().getBillTable().getRowCount();
    int iCurLine = 0;

    if (iOrginalLineCount > 0) {
      iLastFilledRowNum = getLastFilledRowNum();
      if ((iLastFilledRowNum >= 0) && (iLastFilledRowNum <= iOrginalLineCount - 1)) {
        iCurLine = iLastFilledRowNum + 1;
      }
    }
    this.m_iStartItem = iCurLine;

    for (int i = 0; i < billItemVOs.length; i++) {
      if (billItemVOs[i] == null)
        continue;
      if (iCurLine + 1 >= iOrginalLineCount) {
        getCard().addLine();
      }

      FreeVO voFree = null;
      try { voFree = parseFreeItemCodeReturn(billItemVOs[i]); if (voFree == null);
      } catch (Exception e) {
        SCMEnv.error(e);
      }
      InvVO invvo = ((GeneralBillItemVO)billItemVOs[i]).getInv();
      int xx = 1;
      invvo.setFreeItemVO(voFree);

      InvVO invvo1 = ((GeneralBillItemVO)billItemVOs[i]).getInv();

      getCard().getBillModel().setBodyRowVO(billItemVOs[i], iCurLine);

      syncVOItem(billItemVOs[i]);
      BillRowNo.addNewRowNo(getCard(), this.m_sBillTypeCode, "crowno");

      iCurLine++;
    }
  }

  protected void checkImportBodyVO(CircularlyAccessibleValueObject[] voBodyItems, String sWareStoreID)
    throws Exception, BusinessException
  {
    if ((voBodyItems == null) || (voBodyItems.length == 0)) {
      return;
    }
    String sCinventoryid = null;
    String sCInvCode = null;
    String sNotExistInvCode = null;

    for (int i = 0; i < voBodyItems.length; i++) {
      Object objTempIvID = voBodyItems[i].getAttributeValue("cinventoryid");
      Object objTempIvCode = voBodyItems[i].getAttributeValue("cinventorycode");
      if ((objTempIvID != null) || 
        (objTempIvCode == null)) continue;
      sCInvCode = (String)objTempIvCode;
      if (sNotExistInvCode == null)
      {
        sNotExistInvCode = "";
      }
      else {
        sNotExistInvCode = sNotExistInvCode + NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000000");
      }
      sNotExistInvCode = sNotExistInvCode + sCInvCode;
    }

    if ((sNotExistInvCode != null) && (sNotExistInvCode.length() > 0))
    {
      throw new BusinessException(NCLangRes.getInstance().getStrByID("4008other", "UPP4008other-000439") + sNotExistInvCode);
    }
  }

  public void dealBatchCode(CircularlyAccessibleValueObject[] aryBodyItems, String sWarehouseid)
    throws Exception
  {
    if ((aryBodyItems == null) || (aryBodyItems.length == 0))
      return;
    try
    {
      int iLen = aryBodyItems.length;
      String sInvID = null; String sBatchcode = null; String sCastunitid = null;

      ArrayList alInvID = new ArrayList();
      ArrayList alItem = new ArrayList();
      for (int i = 0; i < iLen; i++) {
        sInvID = (String)aryBodyItems[i].getAttributeValue("cinventoryid");
        sBatchcode = (String)aryBodyItems[i].getAttributeValue("vbatchcode");
        if ((sInvID != null) && (sBatchcode != null)) {
          alInvID.add(sInvID);
          alItem.add(aryBodyItems[i]);
        }
      }

      if ((alInvID != null) && (alInvID.size() > 0)) {
        int iSize = alInvID.size();
        String sKey = null; String sValidate = null;
        String[] aryInvID = new String[iSize];
        alInvID.toArray(aryInvID);
        ArrayList alBatchCode = DevInputBO_Client.getInvBatchCodeInfo(aryInvID, getPk_corp(), sWarehouseid);

        Hashtable htbBatchCode = null;
        if ((alBatchCode != null) && (alBatchCode.size() > 0)) {
          int ialLen = alBatchCode.size();
          htbBatchCode = new Hashtable();
          String[] aryResult = new String[2];
          String sReKey = null; String sReValidate = null;
          for (int i = 0; i < ialLen; i++) {
            aryResult = (String[])(String[])alBatchCode.get(i);
            if ((aryResult != null) && (aryResult.length == 2)) {
              sReKey = aryResult[0];
              sReValidate = aryResult[1];
              if ((sReKey != null) && (sReValidate != null)) {
                htbBatchCode.put(sReKey, sReValidate);
              }
            }
          }

        }

        Integer iQualityDay = null;
        int qualityDay = 0;
        UFDate udValidate = null; UFDate udScrq = null;
        Object oInvID = null;
        Object oBatchcode = null;
        Object oCastunitid = null;

        for (int n = 0; n < iSize; n++) {
          CircularlyAccessibleValueObject voItem = (CircularlyAccessibleValueObject)alItem.get(n);

          oInvID = voItem.getAttributeValue("cinventoryid");
          oBatchcode = voItem.getAttributeValue("vbatchcode");
          sBatchcode = (String)oBatchcode;
          oCastunitid = voItem.getAttributeValue("castunitid");

          if ((oInvID != null) && (oBatchcode != null)) {
            sInvID = (String)oInvID;
            sKey = sInvID;

            sBatchcode = (String)oBatchcode;
            sKey = sKey + sBatchcode;

            if (oCastunitid != null) {
              sCastunitid = (String)oCastunitid;
              sKey = sKey + sCastunitid;
            }

            if ((htbBatchCode != null) && (htbBatchCode.containsKey(sKey))) {
              try {
                sValidate = (String)htbBatchCode.get(sKey);
                if (!sValidate.equalsIgnoreCase("NULL")) {
                  udValidate = new UFDate(sValidate);
                  iQualityDay = (Integer)voItem.getAttributeValue("qualityDay");
                  voItem.setAttributeValue("dvalidate", udValidate);

                  if ((iQualityDay != null) && (udValidate != null)) {
                    qualityDay = iQualityDay.intValue();
                    udScrq = udValidate.getDateBefore(qualityDay);
                    voItem.setAttributeValue("scrq", udScrq);
                  }
                }
              } catch (Exception e) {
                SCMEnv.out("nc.ui.ic.pub.device.DevInputCtrl.dealBatchCode:");
                SCMEnv.error(e);
                throw new Exception(NCLangRes.getInstance().getStrByID("4008other", "UPP4008other-000175") + e.getMessage());
              }
            }
            else
            {
              voItem.setAttributeValue("vbatchcode", NCLangRes.getInstance().getStrByID("4008other", "UPP4008other-000440") + sBatchcode);
              throw new Exception(NCLangRes.getInstance().getStrByID("4008other", "UPP4008other-000175") + NCLangRes.getInstance().getStrByID("4008other", "UPP4008other-000440") + sBatchcode);
            }
          }
        }
      }

    }
    catch (Exception e)
    {
      SCMEnv.out("nc.ui.ic.pub.device.DevInputCtrl.dealBatchCode:");
      SCMEnv.error(e);
      throw e;
    }
  }

  public CircularlyAccessibleValueObject[] dealWithVO(DevInputVO[] voaDi, DevInputHeadVO inputHeadVO)
    throws Exception
  {
    if ((voaDi == null) || (voaDi.length == 0)) {
      return null;
    }
    int iLen = voaDi.length;
    InvVO[] voInv = new InvVO[iLen];

    InvoInfoBYFormula invoInfoFormula = getInvoFormula();
    for (int i = 0; i < iLen; i++) {
      if (voaDi[i] == null)
        continue;
      if (voaDi[i].getStatus() == 0)
        continue;
      try
      {
        voaDi[i].setFreeValue();

        voaDi[i].setCastunitID();

        voInv[i] = ((InvVO)voaDi[i].getAttributeValue("invvo"));

        if (voInv[i] == null) {
          continue;
        }
        if ((voInv[i].getCastunitid() != null) && (voInv[i].getCastunitid().length() > 0))
        {
          invoInfoFormula.getInVoOfHSL(voInv[i]);

          GenMethod method = new GenMethod();
          voaDi[i].setConvRate(voInv[i], voaDi[i], "isSolidConvRate", "hsl", "ninnum", "ninassistnum", "noutnum", "noutassistnum");
        }
        else
        {
          voaDi[i].setAttributeValue("ninassistnum", null);
          voaDi[i].setAttributeValue("noutassistnum", null);
        }
      }
      catch (Exception e) {
        SCMEnv.out("nc.ui.ic.pub.device.DevInputCtrl.dealWithVO" + e.toString());

        SCMEnv.error(e);
        throw e;
      }
    }

    ArrayList alAddItem = new ArrayList();
    for (int i = 0; i < voaDi.length; i++) {
      CircularlyAccessibleValueObject voItem = null;
      if (voaDi[i].getStatus() == 0)
        continue;
      if (voaDi[i] != null) {
        if (this.m_iBillClass == 1000)
        {
          voItem = new GeneralBillItemVO();
          voaDi[i].copyBillVO(voItem);

          String cspaceid = (String)voaDi[i].getAttributeValue("pk_cargdoc");
          String cscode = (String)voaDi[i].getAttributeValue("cscode");
          if ((this.m_bWhMan) && (cspaceid == null))
          {
            if ((cscode != null) && (cscode.trim().length() > 0)) {
              throw new Exception(NCLangRes.getInstance().getStrByID("4008other", "UPP4008other-000441", null, new String[] { cscode }));
            }
            throw new Exception(NCLangRes.getInstance().getStrByID("4008other", "UPP4008other-000176"));
          }
          setLocVOBillVO(voaDi[i], (GeneralBillItemVO)voItem, cspaceid);
        }
        else
        {
          voItem = new SpecialBillItemVO();
          voaDi[i].copySpecBillVO(voItem);
        }
        alAddItem.add(voItem);
      }
    }
    CircularlyAccessibleValueObject[] aryBodyItems = null;
    if ((alAddItem != null) && (alAddItem.size() > 0)) {
      aryBodyItems = new CircularlyAccessibleValueObject[alAddItem.size()];
      alAddItem.toArray(aryBodyItems);
    }

    String sWarehouseid = (String)inputHeadVO.getAttributeValue(getWarehouseidFieldName());

    dealBatchCode(aryBodyItems, sWarehouseid);
    return aryBodyItems;
  }

  private void setLocVOBillVO(DevInputVO vo, GeneralBillItemVO voItem, String cspaceid)
  {
    if (cspaceid != null) {
      LocatorVO[] newLocator = new LocatorVO[1];
      newLocator[0] = new LocatorVO();

      String[] sSourceField = { "ninnum", "noutnum" };

      String[] sTargetField = { "ninspacenum", "noutspacenum" };

      for (int i = 0; i < sSourceField.length; i++) {
        if (vo.getAttributeValue(sSourceField[i]) != null) {
          newLocator[0].setAttributeValue(sTargetField[i], vo.getAttributeValue(sSourceField[i]));
        }

      }

      newLocator[0].setAttributeValue("cspaceid", cspaceid);

      SuperVOUtil.execFormulaWithVOs(newLocator, new String[] { "vspacecode->getColValue(bd_cargdoc,cscode,pk_cargdoc,cspaceid", "vspacename->getColValue(bd_cargdoc,csname,pk_cargdoc,cspaceid" }, null);

      voItem.addLocator(newLocator);
    }
  }

  protected DevInputHeadVO getItemToHeadVO(DevInputVO devInputVO, DevInputHeadVO devInputHeadVO)
  {
    String sItemVoName = null;
    String sHeadVoName = null;
    Object sHeadVoValue = null;

    String[] sItemVONames = devInputVO.getAttributeNames();
    if ((sItemVONames != null) && (sItemVONames.length > 0))
    {
      for (int i = 0; i < sItemVONames.length; i++) {
        sItemVoName = sItemVONames[i];
        if (sItemVoName.startsWith("h_"))
          sHeadVoName = sItemVoName.substring(2, sItemVoName.length());
        else
          sHeadVoName = sItemVoName;
        sHeadVoValue = devInputVO.getAttributeValue(sItemVoName);
        devInputHeadVO.setAttributeValue(sHeadVoName, sHeadVoValue);
      }
    }
    return devInputHeadVO;
  }

  public String getWarehouseidFieldName()
  {
    return this.m_sWarehouseidFieldName;
  }

  public void setWarehouseidFieldName(String newWarehouseidFieldName)
  {
    this.m_sWarehouseidFieldName = newWarehouseidFieldName;
  }

  protected void checkHead(DevInputHeadVO inputHeadVO)
    throws Exception
  {
    String sVbillcode = null;
    sVbillcode = getCard().getBillData().getHeadItem("vbillcode").getValue();

    if ((sVbillcode == null) || (sVbillcode.trim().length() == 0)) {
      throw new Exception(NCLangRes.getInstance().getStrByID("4008other", "UPP4008other-000177"));
    }
    sVbillcode = sVbillcode.trim();

    String sInputvBillcode = (String)inputHeadVO.getAttributeValue("vbillcode");
    if ((sInputvBillcode == null) || (sInputvBillcode.trim().length() == 0)) {
      throw new Exception(NCLangRes.getInstance().getStrByID("4008other", "UPP4008other-000178"));
    }
    sInputvBillcode = sInputvBillcode.trim();

    if (!sInputvBillcode.equalsIgnoreCase(sVbillcode))
      throw new Exception(NCLangRes.getInstance().getStrByID("4008other", "UPP4008other-000442", null, new String[] { sVbillcode, sInputvBillcode }));
  }

  protected String getFreeKey(FreeVO voFree)
  {
    String sFreeValue = "vfree";
    String sTemp = "";
    String sReturn = "";
    for (int i = 1; i <= 5; i++) {
      sFreeValue = sFreeValue + i;
      sTemp = (String)voFree.getAttributeValue(sFreeValue);
      if ((sTemp != null) && (sTemp.trim().length() != 0))
        sReturn = sReturn + sTemp;
    }
    return sReturn;
  }

  public Hashtable getHtKwFromFile(CircularlyAccessibleValueObject[] voBodyItems)
  {
    if ((voBodyItems == null) || (voBodyItems[0] == null) || (voBodyItems.length <= 0))
      return null;
    int len = voBodyItems.length;
    String sKeyWord = null;
    Hashtable htKeyWords = new Hashtable();

    for (int i = 0; i < len; i++)
    {
      if (voBodyItems[i] == null)
        continue;
      if (this.m_iBillClass == 1000) {
        sKeyWord = getKeyWordsGen((GeneralBillItemVO)voBodyItems[i]);
      }
      if (this.m_iBillClass == 2000) {
        sKeyWord = getKeyWordsSp((SpecialBillItemVO)voBodyItems[i]);
      }
      htKeyWords.put(sKeyWord, voBodyItems[i]);
    }

    return htKeyWords;
  }

  public String getKeyWordsGen(GeneralBillItemVO vo)
  {
    String sCode = vo.getInv().getCinventorycode();
    if (sCode == null) sCode = "INVCODE";
    else {
      sCode = sCode + "I";
    }
    FreeVO voFree = vo.getFreeItemVO();
    String sFree = null;
    if (voFree != null)
      sFree = getFreeKey(voFree);
    if ((voFree == null) || (sFree == null) || (sFree.trim().length() == 0)) sFree = "FREECODE";
    else {
      sFree = sFree + "F";
    }
    String sHW = "";
    sHW = vo.getCspaceid();
    if (sHW == null) sHW = "HWCODE"; else {
      sHW = sHW + "H";
    }
    if ((this.m_bWhMan == true) && (!this.m_sBillTypeCode.equalsIgnoreCase("4C"))) {
      return sCode + sFree + sHW;
    }

    return sCode + sFree;
  }

  public Hashtable getKeyWordsGen_Bill(GeneralBillItemVO[] voItems)
  {
    Hashtable htReturn = new Hashtable();

    if (voItems == null) return htReturn;
    String sKeyWord = null;
    for (int i = 0; i < voItems.length; i++) {
      if (voItems[i] == null)
        continue;
      sKeyWord = getKeyWordsGen(voItems[i]);
      ArrayList alItemOne = null;
      if (!htReturn.containsKey(sKeyWord)) {
        alItemOne = new ArrayList();
        alItemOne.add(voItems[i]);
        htReturn.put(sKeyWord, alItemOne);
      }
      else {
        alItemOne = (ArrayList)htReturn.get(sKeyWord);
        alItemOne.add(voItems[i]);
      }

    }

    return htReturn;
  }

  public String getKeyWordsSp(SpecialBillItemVO vo)
  {
    String sCode = vo.getInv().getCinventorycode();
    if (sCode == null) sCode = "INVCODE";
    else {
      sCode = sCode + "I";
    }
    FreeVO voFree = vo.getFreeItemVO();
    String sFree = null;
    if (voFree != null)
      sFree = getFreeKey(voFree);
    if ((voFree == null) || (sFree == null) || (sFree.trim().length() == 0)) sFree = "FREECODE";
    else {
      sFree = sFree + "F";
    }
    String sHW = vo.getCspaceid();
    if (sHW == null) sHW = "HWCODE"; else {
      sHW = sHW + "H";
    }
    if ((this.m_bWhMan == true) && (!this.m_sBillTypeCode.equalsIgnoreCase("4C"))) {
      return sCode + sFree + sHW;
    }

    return sCode + sFree;
  }

  public Hashtable getKeyWordsSp_Bill(SpecialBillItemVO[] voItems)
  {
    Hashtable htReturn = new Hashtable();

    if (voItems == null) return htReturn;
    String sKeyWord = null;
    for (int i = 0; i < voItems.length; i++) {
      if (voItems[i] == null)
        continue;
      sKeyWord = getKeyWordsSp(voItems[i]);
      ArrayList alItemOne = null;
      if (!htReturn.containsKey(sKeyWord)) {
        alItemOne = new ArrayList();
        alItemOne.add(voItems[i]);
        htReturn.put(sKeyWord, alItemOne);
      }
      else {
        alItemOne = (ArrayList)htReturn.get(sKeyWord);
        alItemOne.add(voItems[i]);
      }

    }

    return htReturn;
  }

  public Hashtable getKwFromBill(AggregatedValueObject voBill)
  {
    Hashtable htKwBill = new Hashtable();

    if (this.m_iBillClass == 1000) {
      GeneralBillItemVO[] voaItemsGen = (GeneralBillItemVO[])(GeneralBillItemVO[])voBill.getChildrenVO();

      htKwBill = getKeyWordsGen_Bill(voaItemsGen);
    }
    else if (this.m_iBillClass == 2000) {
      SpecialBillItemVO[] voaItemsSp = (SpecialBillItemVO[])(SpecialBillItemVO[])voBill.getChildrenVO();

      htKwBill = getKeyWordsSp_Bill(voaItemsSp);
    }

    return htKwBill;
  }

  protected String getWarehouseNameFieldName()
  {
    return this.m_sWarehouseNameFieldName;
  }

  protected void getWhType(String sWhID)
    throws BusinessException
  {
    Object objValue = CacheTool.getCellValue("bd_stordoc", "pk_stordoc", "csflag", sWhID);
    if ((objValue != null) && (((String)objValue).equalsIgnoreCase("Y")))
      this.m_bWhMan = true;
    else
      this.m_bWhMan = false;
  }

  public ArrayList matchKey(String sKey, Hashtable htKwBill)
  {
    if (htKwBill.containsKey(sKey)) {
      return (ArrayList)htKwBill.get(sKey);
    }
    return null;
  }

  public ArrayList onOpenFile()
    throws BusinessException, Exception
  {
    return onOpenFile(0);
  }

  public ArrayList onOpenFile(int action)
    throws BusinessException, Exception
  {
    ArrayList alResults = new ArrayList();

    DevInputVO[] voaDi = null;
    File f = null;
    CircularlyAccessibleValueObject[] voBodyItems = null;
    if (getFileChooseDlg().showOpenDialog(this.m_tp) == 0)
    {
      if ((f = getFileChooseDlg().getSelectedFile()) != null) {
        try
        {
          String sFileName = f.getAbsolutePath();

          ArrayList alResult = new ArrayList();

          voaDi = readTxtFile(sFileName, alResult);

          DevInputHeadVO inputHeadVO = getHeadVO(voaDi);
          String sWarehouseidOld = (String)inputHeadVO.getAttributeValue(getWarehouseidFieldName());

          formulaVO(voaDi);

          inputHeadVO = getHeadVO(voaDi);
          DevInputVO[] inputItemVOs = getItemVO(voaDi);

          String sWarehouseid = (String)inputHeadVO.getAttributeValue(getWarehouseidFieldName());

          if (sWarehouseid == null) {
            throw new BusinessException(NCLangRes.getInstance().getStrByID("4008other", "UPP4008other-000179"));
          }
          if ((sWarehouseidOld != null) && (!sWarehouseidOld.equalsIgnoreCase(sWarehouseid)))
          {
            throw new BusinessException(NCLangRes.getInstance().getStrByID("4008other", "UPP4008other-000180"));
          }

          getWhType(sWarehouseid);
          if (this.m_iBillClass == 1000)
            this.m_voBill.getParentVO().setAttributeValue("islocatormgt", new UFBoolean(this.m_bWhMan));
          else if (this.m_iBillClass == 2000) {
            this.m_voBill.getParentVO().setAttributeValue("islocatormgtout", new UFBoolean(this.m_bWhMan));
          }

          inputItemVOs = DevInputBO_Client.query(this.m_pk_corp, inputItemVOs);

          voBodyItems = dealWithVO(inputItemVOs, inputHeadVO);

          for (int i = 0; i < voBodyItems.length; i++)
          {
            parseFreeItemCode(voBodyItems[i]);
          }

          alResults.add(String.valueOf(action));
          alResults.add(voBodyItems);

          Hashtable htKeywords = getHtKwFromFile(voBodyItems);
          if ((htKeywords == null) || (htKeywords.size() <= 0)) {
            return null;
          }
          Hashtable htKwBill = getKwFromBill(this.m_voBillUI);
          if ((htKwBill == null) || (htKwBill.size() <= 0))
          {
            if ((this.m_iBillClass == 1000) && 
              (((GeneralBillItemVO)voBodyItems[0]).getCsourcebillbid() != null)) {
              return null;
            }

            setItems(voBodyItems);

            setHead(inputHeadVO);
            this.m_tp.showHintMessage(NCLangRes.getInstance().getStrByID("4008ui", "UPP4008ui-000022"));
            return alResults;
          }

          String sKey = null;

          Enumeration enumer = htKeywords.keys();
          ArrayList alMatch = null;
          while (enumer.hasMoreElements())
          {
            sKey = (String)enumer.nextElement();
            alMatch = matchKey(sKey, htKwBill);
            if (alMatch == null)
            {
              if ((this.m_iBillClass == 1000) && 
                (((GeneralBillItemVO[])(GeneralBillItemVO[])this.m_voBill.getChildrenVO())[0].getCsourcebillbid() != null))
              {
                return null;
              }

              setItems((CircularlyAccessibleValueObject)htKeywords.get(sKey));

              setHead(inputHeadVO);
              this.m_tp.showHintMessage(NCLangRes.getInstance().getStrByID("4008ui", "UPP4008ui-000022"));
              continue;
            }
            setMathUpdateItems((CircularlyAccessibleValueObject)htKeywords.get(sKey), alMatch);
          }

        }
        catch (Exception e)
        {
          SCMEnv.error(e);
          throw e;
        }
      }
    }
    this.m_tp.showHintMessage(NCLangRes.getInstance().getStrByID("4008ui", "UPP4008ui-000023"));
    return alResults;
  }

  public void parseFreeItemCode(CircularlyAccessibleValueObject voItem)
    throws Exception
  {
    String sSQL = null;
    FreeVO voFree = null;
    if (this.m_iBillClass == 1000)
      voFree = ((GeneralBillItemVO)voItem).getFreeItemVO();
    if (this.m_iBillClass == 2000) {
      voFree = ((SpecialBillItemVO)voItem).getFreeItemVO();
    }
    if (voFree == null)
      return;
    String sFreeValue = "vfree";
    String sFreeValueCode = "vfreevalue";
    String sFree = null;

    String[] sSearchfield = { "docname", "pk_defdoc" };
    String sValue = null;
    for (int i = 1; i <= 5; i++) {
      sFreeValue = "vfree" + i;
      sFree = (String)voFree.getAttributeValue(sFreeValue);
      if (sFree == null)
      {
        continue;
      }

      Object[][] objValue = CacheTool.getMultiColValue("bd_defdoc", "doccode", sSearchfield, new String[] { sFree });

      if ((objValue != null) && (objValue.length > 0)) {
        sFreeValueCode = "vfreevalue" + i;
        sValue = (String)objValue[0][0];
        String sValue1 = (String)objValue[0][1];
        if (sValue != null) {
          voFree.setAttributeValue(sFreeValueCode, sFree);
          voFree.setAttributeValue(sFreeValue, sValue);
        }
        else
        {
          voFree.setAttributeValue(sFreeValueCode, sFree);
        }
      } else {
        throw new Exception(NCLangRes.getInstance().getStrByID("4008other", "UPP4008other-000443", null, new String[] { sFree }));
      }

    }

    if (this.m_iBillClass == 1000) {
      voFree.getVfree0();
      ((GeneralBillItemVO)voItem).setFreeItemVO((FreeVO)voFree.clone());
      ((GeneralBillItemVO)voItem).getInv().setFreeItemVO((FreeVO)voFree.clone());
    }

    if (this.m_iBillClass == 2000)
      ((SpecialBillItemVO)voItem).setFreeItemVO(voFree);
  }

  public FreeVO parseFreeItemCodeReturn(CircularlyAccessibleValueObject voItem)
    throws Exception
  {
    String sSQL = null;
    FreeVO voFree = null;
    if (this.m_iBillClass == 1000)
      voFree = ((GeneralBillItemVO)voItem).getFreeItemVO();
    if (this.m_iBillClass == 2000) {
      voFree = ((SpecialBillItemVO)voItem).getFreeItemVO();
    }
    if (voFree == null)
      return null;
    String sFreeValue = "vfree";
    String sFreeValueCode = "vfreevalue";
    String sFree = null;
    String[] sSearchfield = { "docname", "pk_defdoc" };
    String sValue = null;
    for (int i = 1; i <= 5; i++) {
      sFreeValue = "vfree" + i;
      sFree = (String)voFree.getAttributeValue(sFreeValue);
      if (sFree == null)
      {
        continue;
      }

      Object[][] objValue = CacheTool.getMultiColValue("bd_defdoc", "doccode", sSearchfield, new String[] { sFree });

      if ((objValue != null) && (objValue.length > 0)) {
        sFreeValueCode = "vfreevalue" + i;
        sValue = (String)objValue[0][0]; String sValue1 = (String)objValue[0][1];
        if (sValue != null)
        {
          voFree.setAttributeValue(sFreeValue, sValue);
          voFree.setAttributeValue(sFreeValueCode, sValue1);
        }
        else {
          voFree.setAttributeValue(sFreeValueCode, sFree);
        }
      } else {
        throw new Exception(NCLangRes.getInstance().getStrByID("4008other", "UPP4008other-000443", null, new String[] { sFree }));
      }

    }

    if (this.m_iBillClass == 1000) {
      voFree.getVfree0();
      return (FreeVO)voFree.clone();
    }
    if (this.m_iBillClass == 2000) {
      voFree.getVfree0();
      return (FreeVO)voFree.clone();
    }
    return null;
  }

  public void setHeaderValueVO(CircularlyAccessibleValueObject headVO)
  {
    if (headVO == null)
      return;
    long t = System.currentTimeMillis();

    BatchMatchContext.getShareInstance().setInBatchMatch(true);
    BatchMatchContext.getShareInstance().clear();

    BillItem[] items = getCard().getBillData().getHeadTailItems();
    if (items != null) {
      for (int i = 0; i < items.length; i++) {
        if (!items[i].getKey().equalsIgnoreCase("vbillcode")) {
          items[i].setValue(headVO.getAttributeValue(items[i].getKey()));
        }
      }
    }

    BatchMatchContext.getShareInstance().executeBatch();
    BatchMatchContext.getShareInstance().setInBatchMatch(false);
    SCMEnv.out("ExecBatchRefSetPk taken time:" + (System.currentTimeMillis() - t) + "ms.");
  }

  protected void setItems(CircularlyAccessibleValueObject billItemVO)
  {
    if (billItemVO == null) {
      SCMEnv.out("no data.");
      return;
    }
    int iLastFilledRowNum = -1;
    int iOrginalLineCount = getCard().getBillTable().getRowCount();
    int iCurLine = 0;

    if (iOrginalLineCount > 0) {
      iLastFilledRowNum = getLastFilledRowNum();
      if ((iLastFilledRowNum >= 0) && (iLastFilledRowNum <= iOrginalLineCount - 1)) {
        iCurLine = iLastFilledRowNum + 1;
      }
    }
    this.m_iStartItem = iCurLine;

    if (billItemVO != null)
    {
      if (iCurLine + 1 >= iOrginalLineCount) {
        getCard().addLine();
      }
      getCard().getBillModel().setBodyRowVO(billItemVO, iCurLine);
      syncVOItem(billItemVO);
      BillRowNo.addNewRowNo(getCard(), this.m_sBillTypeCode, "crowno");

      iCurLine++;
    }
  }

  protected void setMathUpdateItems(CircularlyAccessibleValueObject voLine, ArrayList matchLines)
  {
    if (this.m_voBill == null) {
      return;
    }
    if (this.m_iBillClass == 1000)
    {
      GeneralBillItemVO voLineGen = (GeneralBillItemVO)voLine;
      GeneralBillItemVO voGet = null;

      UFDouble ufNumLine = null;
      UFDouble ufShouldNum = null;
      boolean bInOut = false;

      if (GeneralBillVO.getBillInOutFlag(this.m_sBillTypeCode) == 1)
        bInOut = true;
      if (GeneralBillVO.getBillInOutFlag(this.m_sBillTypeCode) == -1)
        bInOut = false;
      if (bInOut == true)
        ufNumLine = voLineGen.getNinnum();
      else
        ufNumLine = voLineGen.getNoutnum();
      UFDate bizDate = voLineGen.getDbizdate();

      for (int i = 0; i < matchLines.size(); i++) {
        voGet = (GeneralBillItemVO)matchLines.get(i);
        if (voGet == null) {
          continue;
        }
        if (bInOut == true)
          ufShouldNum = voGet.getNshouldinnum();
        else {
          ufShouldNum = voGet.getNshouldoutnum();
        }
        if (ufNumLine == null)
          continue;
        if ((i == matchLines.size() - 1) || (ufShouldNum == null) || (ufShouldNum.doubleValue() >= ufNumLine.doubleValue())) {
          if (bInOut == true) {
            voGet.setNinnum(ufNumLine);
            voGet.setDbizdate(bizDate);
            getCard().setBodyValueAt(ufNumLine, Integer.parseInt(voGet.getCrowno()) - 1, "ninnum");
            break;
          }
          voGet.setNoutnum(ufNumLine);
          voGet.setDbizdate(bizDate);
          getCard().setBodyValueAt(ufNumLine, Integer.parseInt(voGet.getCrowno()) - 1, "noutnum");

          break;
        }

        if (ufShouldNum.doubleValue() < ufNumLine.doubleValue()) {
          if (bInOut == true) {
            voGet.setNinnum(ufShouldNum);
            voGet.setDbizdate(bizDate);
            getCard().setBodyValueAt(ufShouldNum, Integer.parseInt(voGet.getCrowno()) - 1, "ninnum");
          }
          else {
            voGet.setNoutnum(ufShouldNum);
            voGet.setDbizdate(bizDate);
            getCard().setBodyValueAt(ufShouldNum, Integer.parseInt(voGet.getCrowno()) - 1, "noutnum");
          }
        }
        ufNumLine = ufNumLine.sub(ufShouldNum);
      }

    }
    else if (this.m_iBillClass == 2000)
    {
      SpecialBillItemVO voLineGen = (SpecialBillItemVO)voLine;
      SpecialBillItemVO voGet = null;
      UFDouble ufCheckNum = null;
      ufCheckNum = voLineGen.getNchecknum();

      voGet = (SpecialBillItemVO)matchLines.get(0);
      if ((voGet == null) || (ufCheckNum == null)) {
        return;
      }

      Object numUI = getCard().getBodyValueAt(Integer.parseInt(voGet.getCrowno()) - 1, "nchecknum");
      UFDouble dbNumUI = null;
      if ((numUI == null) || (numUI.toString().equals("")))
        dbNumUI = new UFDouble(0.0D);
      else {
        dbNumUI = new UFDouble(numUI.toString());
      }
      UFDouble ufNumAll = dbNumUI.add(ufCheckNum);

      voGet.setNchecknum(ufNumAll);
      getCard().setBodyValueAt(ufNumAll, Integer.parseInt(voGet.getCrowno()) - 1, "nchecknum");
    }
  }

  protected void setUpdateItemsNum(CircularlyAccessibleValueObject[] billItemVOs)
  {
    if (billItemVOs == null) {
      SCMEnv.out("no data.");
      return;
    }
    int iLastFilledRowNum = -1;
    int iOrginalLineCount = getCard().getBillTable().getRowCount();
    int iCurLine = 0;

    String sCrowno = null;
    if ((this.m_voBill != null) && (this.m_voBill.getChildrenVO() != null)) {
      GeneralBillItemVO[] itemsCurrVOs = (GeneralBillItemVO[])(GeneralBillItemVO[])this.m_voBill.getChildrenVO();

      Hashtable htbItems = new Hashtable();

      for (int i = 0; i < billItemVOs.length; i++) {
        if (billItemVOs[i] != null) {
          sCrowno = (String)billItemVOs[i].getAttributeValue("crowno");
          if (sCrowno != null) {
            sCrowno = sCrowno.trim();
            htbItems.put(sCrowno, billItemVOs[i]);
          }
        }
      }

      this.sItemNumKeys = new String[] { "ninnum", "noutnum", "dbizdate" };

      GeneralBillItemVO itemsCurrVO = null;
      Object Otemp = null;
      String sValue = null;
      for (int i = 0; i < itemsCurrVOs.length; i++)
        if (itemsCurrVOs[i] != null) {
          sCrowno = (String)itemsCurrVOs[i].getAttributeValue("crowno");
          if (sCrowno != null) {
            sCrowno = sCrowno.trim();
            if (htbItems.containsKey(sCrowno)) {
              itemsCurrVO = (GeneralBillItemVO)htbItems.get(sCrowno);
              if (itemsCurrVO != null)
                for (int n = 0; n < this.sItemNumKeys.length; n++) {
                  Otemp = itemsCurrVO.getAttributeValue(this.sItemNumKeys[n]);

                  if ((Otemp != null) && (Otemp.toString().trim().length() > 0)) {
                    sValue = Otemp.toString().trim();
                    billItemVOs[i].setAttributeValue(this.sItemNumKeys[n], sValue);
                    getCard().setBodyValueAt(billItemVOs[i].getAttributeValue(this.sItemNumKeys[n]), i, this.sItemNumKeys[n]);
                  }
                }
            }
          }
        }
    }
  }

  public void setWarehouseNameFieldName(String newWarehouseNameFieldName)
  {
    this.m_sWarehouseNameFieldName = newWarehouseNameFieldName;
  }

  public void setBillVOUI(AggregatedValueObject vo)
  {
    this.m_voBillUI = vo;
  }

  public void syncVOItem(CircularlyAccessibleValueObject voItem)
  {
    if (this.m_voBill == null) return;
    int ilen = this.m_voBill.getChildrenVO().length;
    this.m_voBill.getChildrenVO()[(ilen - 1)] = voItem;
  }
}