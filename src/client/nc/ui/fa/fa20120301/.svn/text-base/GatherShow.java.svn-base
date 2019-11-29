package nc.ui.fa.fa20120301;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Vector;
import nc.itf.fa.prv.ICardItem;
import nc.itf.fa.prv.IOption;
import nc.ui.fa.catename.CateNameProvider;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.UIRefPane;
import nc.vo.bd.access.BddataVO;
import nc.vo.fa.FaPublicPara;
import nc.vo.fa.UFDouble8;
import nc.vo.fa.base.BaseDocProc;
import nc.vo.fa.currency.FaCurrency;
import nc.vo.fa.fa20120201.CardhistoryVO;
import nc.vo.fa.fa20120301.EnumDepVO;
import nc.vo.fa.fa20120301.GatherItemVO;
import nc.vo.fa.fa201206.CarditemVO;
import nc.vo.fa.pub.proxy.FAProxy;
import nc.vo.fa.sort.FaSortor;
import nc.vo.pub.lang.UFDouble;

public class GatherShow
{
  private CardhistoryVO[] m_RowData = null;
  private Vector m_vData = null;
  private Vector m_vHead = null;
  private String m_sDwbm = null;
  private String m_sYear = null;
  private String m_sMonth = null;
  private String[] m_sGatherItem = null;
  private HashMap m_hmGatherItem = null;
  private String m_sGroupPk = "0001";
  private int m_iLocalCurrencyScale = 0;
  private HashMap m_hCardItem = null;
  private HashMap m_hAllSelectedItem = null;

  private int gatherItemCount = 0;

  private BaseDocProc m_docProc = null;

  private HashMap m_hmAddCode = null;

  private HashMap m_hSelfItem = null;
  private HashMap m_hmSysItem = null;

  private UIRefPane m_workCenter = null;
  private UIRefPane m_jobmngFile = null;
  private UIRefPane m_provider = null;
  private int[] m_arrUFDColumnIndex = null;
  int[] m_iCateLevel = { 2, 2, 2, 2, 2 };
  int[] m_iUseStatusLevel = { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 };
  int[] m_iAddReduceLevel = { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 };

  public GatherShow()
  {
  }

  public GatherShow(String dwbm, String year, String month, CardhistoryVO[] cvos, BaseDocProc bsp)
  {
    this.m_RowData = cvos;
    this.m_sDwbm = dwbm;
    this.m_sYear = year;
    this.m_sMonth = month;
    this.m_docProc = bsp;
    initData();
    process();
  }
  private void processDefault() {
    CardhistoryVO[] cVOs = this.m_RowData;
    if ((cVOs == null) || (cVOs.length <= 0)) {
      this.m_vData = new Vector(0);
      this.m_vHead = new Vector(0);
      return;
    }

    Vector vHead = new Vector();
    String sTitle = NCLangRes.getInstance().getStrByID("201233", "UPP201233-000160");
    vHead.addElement(sTitle);
    Vector vData = new Vector();
    Vector vCate = new Vector();
    Vector vDept = new Vector();
    Hashtable hData = new Hashtable();
    String sCatePk = ""; String sDeptPk = "";
    UFDouble ufTemp = null;
    UFDouble uTempSum = new UFDouble(0);
    for (int m = 0; m < cVOs.length; m++) {
      sCatePk = cVOs[m].getFk_category();
      sDeptPk = cVOs[m].getFk_dept();
      String key = sCatePk + "_" + sDeptPk;
      ufTemp = getUFDouble(cVOs[m].getDepamount(), this.m_iLocalCurrencyScale);
      if (hData.containsKey(key)) {
        uTempSum = (UFDouble)hData.get(key);
        hData.put(key, getUFDouble(uTempSum.add(ufTemp), this.m_iLocalCurrencyScale));
      } else {
        hData.put(key, ufTemp);
      }
      if (!vCate.contains(sCatePk)) {
        vCate.addElement(sCatePk);
      }
      if (!vDept.contains(sDeptPk)) {
        vDept.addElement(sDeptPk);
        vHead.addElement(this.m_docProc.getDocNameByPK("00010000000000000002", this.m_sDwbm, sDeptPk));
      }
    }
    vHead.addElement(EnumDepVO.HJ);
    Vector vTemp = new Vector();
    double dDeptTotal = 0.0D;
    for (int m = 0; m < vCate.size(); m++) {
      sCatePk = vCate.elementAt(m).toString();
      vTemp = new Vector();
      dDeptTotal = 0.0D;
      String sName = sCatePk;

      sName = CateNameProvider.getCateNameByPK(sCatePk, this.m_sDwbm, null);

      vTemp.addElement(sName);
      for (int n = 0; n < vDept.size(); n++) {
        sDeptPk = vDept.elementAt(n).toString();
        if (hData.containsKey(sCatePk + "_" + sDeptPk)) {
          ufTemp = (UFDouble)hData.get(sCatePk + "_" + sDeptPk);
          vTemp.addElement(ufTemp);

          dDeptTotal += ufTemp.doubleValue();
        } else {
          vTemp.addElement(null);
        }
      }
      vTemp.addElement(getUFDouble(new UFDouble8(dDeptTotal), this.m_iLocalCurrencyScale));
      vData.addElement(vTemp);
    }

    double dCateTotal = 0.0D;
    vTemp = new Vector();
    vTemp.addElement(EnumDepVO.HJ);
    for (int n = 1; n < vHead.size(); n++) {
      dCateTotal = 0.0D;
      for (int m = 0; m < vCate.size(); m++) {
        ufTemp = (UFDouble)((Vector)vData.elementAt(m)).elementAt(n);
        if (ufTemp != null) {
          dCateTotal += ufTemp.doubleValue();
        }
      }
      vTemp.addElement(getUFDouble(new UFDouble8(dCateTotal), this.m_iLocalCurrencyScale));
    }
    Object[] sortField = { "0" };
    vData = sortData(vData, sortField);

    vData.addElement(vTemp);
    this.m_vData = vData;
    this.m_vHead = vHead;

    this.m_arrUFDColumnIndex = new int[vHead.size() - 1];
    for (int i = 0; i < this.m_arrUFDColumnIndex.length; i++)
      this.m_arrUFDColumnIndex[i] = (i + 1);
  }

  public Vector getData()
  {
    return this.m_vData;
  }

  public String getFatherPk(int type, String pk, int level)
  {
    if ((pk == null) || (pk.length() == 0)) return null;
    int preLevel = 0;
    String code = "";
    if (type == 0) {
      for (int i = 1; (i < this.m_iCateLevel.length) && (i < level); i++) {
        preLevel += this.m_iCateLevel[i];
      }
      BddataVO vo = this.m_docProc.getVOByPK("00010000000000000022", this.m_sDwbm, pk);

      String thisCode = vo.getCode();
      code = thisCode.substring(0, preLevel);
      BddataVO cvo = this.m_docProc.getVOByCode("00010000000000000022", this.m_sDwbm, code);
      if (cvo == null)
      {
        return null;
      }
      return cvo.getPk();
    }
    if (type == 1) {
      BddataVO vo = this.m_docProc.getVOByPK("0001fa01000000000061", this.m_sDwbm, pk);
      String thisCode = vo.getCode();
      code = thisCode.substring(0, thisCode.length() - 2);
      BddataVO uvo = this.m_docProc.getVOByCode("0001fa01000000000061", this.m_sDwbm, code);
      if (uvo == null)
      {
        return null;
      }
      return uvo.getPk();
    }
    if (type == 2) {
      BddataVO vo = this.m_docProc.getVOByPK("00010000000000000051", this.m_sDwbm, pk);
      String thisCode = vo.getCode();
      code = thisCode.substring(0, thisCode.length() - 2);
      BddataVO avo = this.m_docProc.getVOByCode("00010000000000000051", this.m_sDwbm, code);
      if (avo == null)
      {
        return null;
      }
      return avo.getPk();
    }

    return null;
  }

  public Vector getHead()
  {
    return this.m_vHead;
  }

  private String getMapItemCode(String s)
  {
    Object o = this.m_hCardItem.get(s);
    if (o == null) return null;
    return o.toString();
  }

  private String getNameFromPk(String gatherItem, CardhistoryVO cvo)
  {
    Object o = null;
    if (gatherItem.equals("fk_category")) {
      o = cvo.getFk_category();
      if (o == null) return null;
      return CateNameProvider.getCateNameByPK(o.toString(), this.m_sDwbm, null);
    }
    if (gatherItem.equals("fk_usedept")) {
      o = cvo.getFk_usedept();
      if (o == null) return null;
      return this.m_docProc.getDocNameByPK("00010000000000000002", this.m_sDwbm, o.toString());
    }
    if (gatherItem.equals("fk_mandept")) {
      o = cvo.getFk_mandept();
      if (o == null) return null;
      return this.m_docProc.getDocNameByPK("00010000000000000002", this.m_sDwbm, o.toString());
    }
    if (gatherItem.equals("fk_dept")) {
      o = cvo.getFk_dept();
      if (o == null) return null;
      return this.m_docProc.getDocNameByPK("00010000000000000002", this.m_sDwbm, o.toString());
    }
    if (gatherItem.equals("fk_usingstatus")) {
      o = cvo.getFk_usingstatus();
      if (o == null) return null;
      return this.m_docProc.getDocNameByPK("0001fa01000000000061", this.m_sDwbm, o.toString());
    }
    if (gatherItem.equals("fk_addreducestyle")) {
      o = cvo.getM_fk_addreducestyle();
      if (o == null) return null;
      return this.m_docProc.getDocNameByPK("00010000000000000051", this.m_sDwbm, o.toString());
    }
    if (gatherItem.equals("fk_depmethod")) {
      o = cvo.getFk_depmethod();
      if (o == null) return null;
      return this.m_docProc.getDocNameByPK("0001fa01000000000062", this.m_sDwbm, o.toString());
    }
    if (gatherItem.equals("assetuser")) {
      o = cvo.getM_assetuser();
      if (o == null) return null;
      return this.m_docProc.getDocNameByPK("00010000000000000001", this.m_sDwbm, o.toString());
    }
    if (gatherItem.equals("fk_jobmngfil")) {
      o = cvo.getFk_jobmngfil();
      if (o == null) return null;
      return this.m_docProc.getDocNameByPK("00010000000000000015", this.m_sDwbm, o.toString());
    }
    if (gatherItem.equals("workcenter")) {
      o = cvo.getM_fk_workcenter();
      if (o == null) return null;
      return this.m_docProc.getDocNameByPK("0001PD01AA0000008Q14", this.m_sDwbm, o.toString());
    }
    if (gatherItem.equals("provider")) {
      o = cvo.getProvider();
      if (o == null) return null;
      return this.m_docProc.getDocNameByPK("00010000000000000048", this.m_sDwbm, o.toString());
    }

    return "";
  }

  private HashMap getSelfItemMap()
  {
    return this.m_hSelfItem;
  }

  private HashMap getSysItemMap()
  {
    return this.m_hmSysItem;
  }

  public int[] getUFDColumnIndex()
  {
    return this.m_arrUFDColumnIndex;
  }

  private UFDouble getUFDouble(Object obj, int iScale)
  {
    if (obj == null) {
      return null;
    }
    UFDouble ufd = null;
    if ((obj instanceof Number)) {
      ufd = new UFDouble(obj.toString(), -1 * iScale);
      return ufd;
    }
    return null;
  }

  public void initCateBMJC()
  {
    try
    {
      this.m_iCateLevel = FaPublicPara.getCategoryLever();
    }
    catch (Throwable e) {
      e.printStackTrace();
      this.m_iCateLevel = new int[] { 2, 2, 2, 2, 2 };
    }
  }

  private void initData()
  {
    try
    {
      if (this.m_docProc == null) {
        this.m_docProc = new BaseDocProc();
      }

      GatherItemVO[] ggs = FAProxy.getRemoteOption().findGatherItemByPKcorp(this.m_sDwbm, this.m_sYear, this.m_sMonth);

      if (ggs != null) {
        this.m_sGatherItem = new String[ggs.length];
        this.m_hmGatherItem = new HashMap();
        for (int i = 0; i < ggs.length; i++) {
          this.m_sGatherItem[i] = ggs[i].getItem_code();
          this.m_hmGatherItem.put(this.m_sGatherItem[i], ggs[i]);
        }

      }

      if ((ggs != null) && (ggs.length > 0)) {
        this.gatherItemCount = ggs.length;
      }

      FaCurrency curr = new FaCurrency(this.m_sDwbm);

      String sLocalCurrencyPkey = curr.getLocalCurrencyPK();

      this.m_iLocalCurrencyScale = curr.getCurrDigit(sLocalCurrencyPkey);
    } catch (Throwable e) {
      e.printStackTrace();
    }

    CarditemVO[] cardItem = null;
    try {
      cardItem = FAProxy.getRemoteCardItem().queryCarditemSelfDef(this.m_sDwbm, this.m_sGroupPk);
      setCardItemVO(cardItem);
    } catch (Throwable e) {
      e.printStackTrace();
    }
    if (cardItem != null) {
      this.m_hSelfItem = new HashMap();
      for (int i = 0; i < cardItem.length; i++) {
        Object oo = getMapItemCode(cardItem[i].getPk_carditem());
        if ((oo == null) || (oo.toString().trim().length() == 0)) {
          continue;
        }
        this.m_hSelfItem.put(oo.toString(), cardItem[i].getItem_name());
      }
    }

    initCateBMJC();
  }

  private void process()
  {
    if ((this.m_sGatherItem == null) || (this.m_sGatherItem.length == 0))
    {
      processDefault();
      return;
    }
    this.m_hmSysItem = new HashMap();
    for (int i = 0; i < EnumDepVO.m_sItemCode.length; i++) {
      this.m_hmSysItem.put(EnumDepVO.m_sItemCode[i], EnumDepVO.m_sItemName[i]);
    }
    this.m_hAllSelectedItem = new HashMap();
    String item = "";
    for (int i = 0; i < this.m_sGatherItem.length; i++) {
      item = this.m_sGatherItem[i];
      if (this.m_hmSysItem.containsKey(item)) {
        this.m_hAllSelectedItem.put(item, this.m_hmSysItem.get(item));
      }
      else if (this.m_hSelfItem.containsKey(item)) {
        this.m_hAllSelectedItem.put(item, this.m_hSelfItem.get(item));
      }

    }

    if (this.m_sGatherItem.length == 2) {
      process2Item();
      return;
    }
    processMutiItem();
  }

  private void process2Item()
  {
    CardhistoryVO[] cVOs = this.m_RowData;
    if ((cVOs == null) || (cVOs.length <= 0)) {
      this.m_vData = new Vector(0);
      this.m_vHead = new Vector(0);
      return;
    }
    String itemNameRow = "";
    String itemNameCol = "";
    itemNameRow = ((GatherItemVO)this.m_hmGatherItem.get(this.m_sGatherItem[0])).getItem_name();
    itemNameCol = ((GatherItemVO)this.m_hmGatherItem.get(this.m_sGatherItem[1])).getItem_name();
    if (itemNameRow.equals(EnumDepVO.ZCLB)) {
      itemNameRow = NCLangRes.getInstance().getStrByID("common", "UC000-0003130");
    }
    if (itemNameCol.equals(EnumDepVO.ZCLB)) {
      itemNameCol = NCLangRes.getInstance().getStrByID("common", "UC000-0003130");
    }
    if (itemNameRow.equals(EnumDepVO.ZJCDBM)) {
      itemNameRow = NCLangRes.getInstance().getStrByID("common", "UC000-0004064");
    }
    if (itemNameCol.equals(EnumDepVO.ZJCDBM)) {
      itemNameCol = NCLangRes.getInstance().getStrByID("common", "UC000-0004064");
    }

    Vector vHead = new Vector();
    String sTitle = itemNameRow + "\\" + itemNameCol;
    vHead.addElement(sTitle);

    Vector vData = new Vector();
    HashSet hDataRow = new HashSet();
    HashSet hDataCol = new HashSet();
    HashMap hmDataCross = new HashMap();
    UFDouble ufTemp = null;
    Vector vRow = new Vector(0);
    String valueRow = "";
    String valueCol = "";
    UFDouble uTempSum = new UFDouble(0);
    for (int m = 0; m < cVOs.length; m++) {
      if (this.m_hmSysItem.containsKey(this.m_sGatherItem[1]))
        valueCol = getNameFromPk(this.m_sGatherItem[1], this.m_RowData[m]);
      else {
        valueCol = (String)cVOs[m].getAttributeValue(this.m_sGatherItem[1]);
      }
      if ((valueCol == null) || (valueCol.trim().length() == 0)) valueCol = "";
      if (!hDataCol.contains(valueCol)) {
        vHead.addElement(valueCol);
        hDataCol.add(valueCol);
      }
      if (this.m_hmSysItem.containsKey(this.m_sGatherItem[0]))
        valueRow = getNameFromPk(this.m_sGatherItem[0], this.m_RowData[m]);
      else {
        valueRow = cVOs[m].getAttributeValue(this.m_sGatherItem[0]).toString();
      }
      if ((valueRow == null) || (valueRow.trim().length() == 0)) valueRow = "";
      if (!hDataRow.contains(valueRow)) {
        vRow.addElement(valueRow);
        hDataRow.add(valueRow);
      }
      String key = valueRow + "_" + valueCol;
      ufTemp = getUFDouble(cVOs[m].getDepamount(), this.m_iLocalCurrencyScale);
      if (hmDataCross.containsKey(key)) {
        uTempSum = (UFDouble)hmDataCross.get(key);
        hmDataCross.put(key, getUFDouble(uTempSum.add(ufTemp), this.m_iLocalCurrencyScale));
      } else {
        hmDataCross.put(key, ufTemp);
      }
    }
    vHead.addElement(EnumDepVO.HJ);
    double dDeptTotal = 0.0D;
    Vector rowdata = null;
    String key = "";
    Vector vSumRow = new Vector(vHead.size());
    for (int i = 0; i < vHead.size(); i++) {
      if (i == 0) {
        vSumRow.addElement(EnumDepVO.HJ);
      }
      vSumRow.addElement(new UFDouble(0));
    }
    UFDouble uu = null;
    UFDouble uTotal = new UFDouble(0);
    for (int m = 0; m < vRow.size(); m++) {
      valueRow = vRow.elementAt(m).toString();
      rowdata = new Vector();
      dDeptTotal = 0.0D;
      rowdata.addElement(valueRow);
      for (int n = 1; n < vHead.size() - 1; n++) {
        valueCol = vHead.elementAt(n).toString();
        key = valueRow + "_" + valueCol;
        if (hmDataCross.containsKey(key)) {
          ufTemp = (UFDouble)hmDataCross.get(key);
          rowdata.addElement(ufTemp);

          dDeptTotal += ufTemp.doubleValue();

          uu = getUFDouble(((UFDouble)vSumRow.elementAt(n)).add(ufTemp), this.m_iLocalCurrencyScale);
          vSumRow.setElementAt(uu, n);
        } else {
          rowdata.addElement(null);
        }
      }
      uTotal = uTotal.add(dDeptTotal);
      rowdata.addElement(getUFDouble(new UFDouble8(dDeptTotal), this.m_iLocalCurrencyScale));
      vData.addElement(rowdata);
    }

    vSumRow.setElementAt(getUFDouble(uTotal, this.m_iLocalCurrencyScale), vHead.size() - 1);
    vData.addElement(vSumRow);
    this.m_vData = vData;
    this.m_vHead = vHead;

    this.m_arrUFDColumnIndex = new int[vHead.size() - 1];
    for (int i = 0; i < this.m_arrUFDColumnIndex.length; i++)
      this.m_arrUFDColumnIndex[i] = (i + 1);
  }

  private void processMutiItem()
  {
    this.m_vHead = new Vector(0);
    this.m_vData = new Vector(0);
    String itemName = "";
    for (int i = 0; i < this.m_sGatherItem.length; i++) {
      itemName = (String)this.m_hAllSelectedItem.get(this.m_sGatherItem[i]);
      this.m_vHead.addElement(itemName);
    }
    this.m_vHead.addElement(EnumDepVO.ZJE);
    UFDouble uSum = new UFDouble(0);
    Vector row = null;
    HashSet hs = new HashSet();
    String value = "";
    UFDouble utemp = new UFDouble(0);
    for (int i = 0; i < this.m_RowData.length; i++) {
      row = new Vector(0);
      for (int j = 0; j < this.m_sGatherItem.length; j++)
      {
        if (this.m_hmSysItem.containsKey(this.m_sGatherItem[j])) {
          value = getNameFromPk(this.m_sGatherItem[j], this.m_RowData[i]);
          if (value == null) value = "";
          row.addElement(value);
        }
        else {
          value = (String)this.m_RowData[i].getDefValue(this.m_sGatherItem[j]);
          if (value == null) value = "";
          row.addElement(value);
        }
      }
      utemp = getUFDouble(this.m_RowData[i].getDepamount(), this.m_iLocalCurrencyScale);
      uSum = uSum.add(utemp);
      row.addElement(utemp);
      this.m_vData.addElement(row);
    }
    row = new Vector(0);
    for (int i = 0; i < this.m_sGatherItem.length - 1; i++) {
      row.addElement("");
    }

    row.addElement(EnumDepVO.HJ);
    uSum = getUFDouble(uSum, this.m_iLocalCurrencyScale);
    row.addElement(uSum);
    this.m_vData.addElement(row);

    this.m_arrUFDColumnIndex = new int[1];

    this.m_arrUFDColumnIndex[0] = (this.m_vHead.size() - 1);
  }

  private void setCardItemVO(CarditemVO[] newM_cardItemVO)
  {
    this.m_hCardItem = new HashMap();
    if (newM_cardItemVO != null)
      for (int i = 0; i < newM_cardItemVO.length; i++) {
        String pk_defquote = newM_cardItemVO[i].getPk_defquote();
        if ((pk_defquote != null) && (pk_defquote.length() > 0) && (newM_cardItemVO[i].getItem_code().length() == 8)) {
          int iSequence = new Integer(pk_defquote.substring(17)).intValue();
          String strQuote = "def" + iSequence;
          this.m_hCardItem.put(newM_cardItemVO[i].getPk_carditem(), strQuote);
        }
      }
  }

  private Vector sortData(Vector v, Object[] sortField)
  {
    FaSortor faSort = new FaSortor();
    faSort.setSortfield(sortField);
    Vector vData = new Vector(0);
    vData = faSort.sortVectorData(v);
    return vData;
  }
  public int getGatherItemCount() {
    return this.gatherItemCount;
  }
  public void setGatherItemCount(int gatherItemCount) {
    this.gatherItemCount = gatherItemCount;
  }
}