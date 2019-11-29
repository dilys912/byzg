package nc.ui.scm.inv;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;
import nc.bs.pub.formulaparse.FormulaParse;
import nc.ui.scm.ic.freeitem.DefHelper;
import nc.ui.scm.pub.ScmPubHelper;
import nc.vo.pub.BusinessException;
import nc.vo.pub.formulaset.util.StringUtil;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.ic.bill.FreeVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pu.VariableConst;

public class InvTool
{
  private static HashMap m_htmapBatchCodeManaged = null;

  private static Hashtable m_hashInvConvRate = null;
  private static Hashtable m_hashInvFreeVO;
  private static HashMap m_htmapMainUnit = null;

  private static HashMap m_htmapAssistManaged = null;

  private static Hashtable m_hashInvTaxRate = null;

  private static HashMap m_htmapLabor = null;

  private static HashMap m_htmapDiscount = null;

  public static void loadBatchLabor(String[] saBaseId)
  {
    if ((saBaseId == null) || (saBaseId.length == 0)) {
      return;
    }
    if (m_htmapLabor == null) {
      m_htmapLabor = new HashMap();
    }
    Vector vecQueryBaseId = new Vector();
    int iLen = saBaseId.length;
    for (int i = 0; i < iLen; i++) {
      if ((PuPubVO.getString_TrimZeroLenAsNull(saBaseId[i]) == null) || (m_htmapLabor.containsKey(saBaseId[i])) || (vecQueryBaseId.contains(saBaseId[i]))) {
        continue;
      }
      vecQueryBaseId.add(saBaseId[i]);
    }

    int iQueryLen = vecQueryBaseId.size();
    if (iQueryLen == 0) {
      return;
    }
    Object[] saRet = null;
    try
    {
      saRet = (Object[])(Object[])nc.ui.scm.pub.cache.CacheTool.getColumnValue("bd_invbasdoc", "pk_invbasdoc", "laborflag", saBaseId);
    }
    catch (BusinessException e) {
      System.out.println(e.getMessage());
    }
    if (saRet != null)
      for (int i = 0; i < iQueryLen; i++)
        if ((saRet[i] == null) || (PuPubVO.getString_TrimZeroLenAsNull(saRet[i]) == null) || (saRet[i].equals("N")))
        {
          m_htmapLabor.put(saBaseId[i], VariableConst.UFBOOLEAN_FALSE);
        }
        else
          m_htmapLabor.put(saBaseId[i], VariableConst.UFBOOLEAN_TRUE);
  }

  public static boolean isLaborNew(String sInvBaseId)
  {
    if (PuPubVO.getString_TrimZeroLenAsNull(sInvBaseId) == null) {
      return false;
    }
    if ((m_htmapLabor == null) || (!m_htmapLabor.containsKey(sInvBaseId)))
    {
      loadBatchLabor(new String[] { sInvBaseId });
    }

    UFBoolean bRet = (UFBoolean)m_htmapLabor.get(sInvBaseId);
    if (bRet == null) {
      return false;
    }

    return bRet.booleanValue();
  }

  public static void loadBatchDiscount(String[] saBaseId)
  {
    if ((saBaseId == null) || (saBaseId.length == 0)) {
      return;
    }
    if (m_htmapDiscount == null) {
      m_htmapDiscount = new HashMap();
    }
    Vector vecQueryBaseId = new Vector();
    int iLen = saBaseId.length;
    for (int i = 0; i < iLen; i++) {
      if ((PuPubVO.getString_TrimZeroLenAsNull(saBaseId[i]) == null) || (m_htmapDiscount.containsKey(saBaseId[i])) || (vecQueryBaseId.contains(saBaseId[i]))) {
        continue;
      }
      vecQueryBaseId.add(saBaseId[i]);
    }

    int iQueryLen = vecQueryBaseId.size();
    if (iQueryLen == 0) {
      return;
    }
    Object[] saRet = null;
    try
    {
      saRet = (Object[])(Object[])nc.ui.scm.pub.cache.CacheTool.getColumnValue("bd_invbasdoc", "pk_invbasdoc", "discountflag", saBaseId);
    }
    catch (BusinessException e) {
      System.out.println(e.getMessage());
    }
    if (saRet != null)
      for (int i = 0; i < iQueryLen; i++)
        if ((saRet[i] == null) || (PuPubVO.getString_TrimZeroLenAsNull(saRet[i]) == null) || (saRet[i].equals("N")))
        {
          m_htmapDiscount.put(saBaseId[i], VariableConst.UFBOOLEAN_FALSE);
        }
        else
          m_htmapDiscount.put(saBaseId[i], VariableConst.UFBOOLEAN_TRUE);
  }

  public static boolean isDiscountNew(String sInvBaseId)
  {
    if (PuPubVO.getString_TrimZeroLenAsNull(sInvBaseId) == null) {
      return false;
    }
    if ((m_htmapDiscount == null) || (!m_htmapDiscount.containsKey(sInvBaseId)))
    {
      loadBatchDiscount(new String[] { sInvBaseId });
    }

    UFBoolean bRet = (UFBoolean)m_htmapDiscount.get(sInvBaseId);
    if (bRet == null) {
      return false;
    }

    return bRet.booleanValue();
  }

  public static void loadBatchAssistManaged(String[] saBaseId)
  {
    if ((saBaseId == null) || (saBaseId.length == 0)) {
      return;
    }
    if (m_htmapAssistManaged == null) {
      m_htmapAssistManaged = new HashMap();
    }
    Vector vecQueryBaseId = new Vector();
    int iLen = saBaseId.length;
    for (int i = 0; i < iLen; i++) {
      if ((PuPubVO.getString_TrimZeroLenAsNull(saBaseId[i]) == null) || (m_htmapAssistManaged.containsKey(saBaseId[i])) || (vecQueryBaseId.contains(saBaseId[i]))) {
        continue;
      }
      vecQueryBaseId.add(saBaseId[i]);
    }

    int iQueryLen = vecQueryBaseId.size();
    if (iQueryLen == 0) {
      return;
    }

    Object[] saRet = null;
    try {
      saRet = (Object[])(Object[])nc.ui.scm.pub.cache.CacheTool.getColumnValue("bd_invbasdoc", "pk_invbasdoc", "assistunit", saBaseId);
    }
    catch (BusinessException e) {
      System.out.println(e.getMessage());
    }
    if (saRet != null)
      for (int i = 0; i < iQueryLen; i++)
        if ((saRet[i] == null) || (PuPubVO.getString_TrimZeroLenAsNull(saRet[i]) == null) || (saRet[i].equals("N")))
        {
          m_htmapAssistManaged.put(saBaseId[i], VariableConst.UFBOOLEAN_FALSE);
        }
        else
          m_htmapAssistManaged.put(saBaseId[i], VariableConst.UFBOOLEAN_TRUE);
  }

  public static void loadBatchFreeVO(String[] saMangId)
  {
    if ((saMangId == null) || (saMangId.length < 1)) {
      return;
    }

    if (m_hashInvFreeVO == null) {
      m_hashInvFreeVO = new Hashtable();
    }

    int iLength = saMangId.length;
    ArrayList alQuery = new ArrayList();
    for (int i = 0; i < iLength; i++) {
      if ((PuPubVO.getString_TrimZeroLenAsNull(saMangId[i]) == null) || (m_hashInvFreeVO.containsKey(saMangId[i])))
        continue;
      alQuery.add(saMangId[i]);
    }

    int iQueryLength = alQuery.size();
    if (iQueryLength == 0) {
      return;
    }

    ArrayList allList = new ArrayList();
    allList.add(alQuery);

    ArrayList retList = null;
    try
    {
      retList = DefHelper.queryFreeVOByInvIDsGroupByBills(allList);
    } catch (Exception e) {
      e.printStackTrace(System.out);
      return;
    }

    if (retList != null)
    {
      ArrayList freeList = (ArrayList)(ArrayList)retList.get(0);
      if ((freeList != null) && (freeList.size() == iQueryLength))
      {
        for (int i = 0; i < iQueryLength; i++) {
          String sMangId = (String)alQuery.get(i);
          FreeVO retFreeVO = (FreeVO)freeList.get(i);

          if ((retFreeVO == null) || (((retFreeVO.getVfreename1() == null) || (retFreeVO.getVfreename1().length() < 1)) && ((retFreeVO.getVfreename2() == null) || (retFreeVO.getVfreename2().length() < 1)) && ((retFreeVO.getVfreename3() == null) || (retFreeVO.getVfreename3().length() < 1)) && ((retFreeVO.getVfreename4() == null) || (retFreeVO.getVfreename4().length() < 1)) && ((retFreeVO.getVfreename5() == null) || (retFreeVO.getVfreename5().length() < 1))))
          {
            m_hashInvFreeVO.put(sMangId, "");
          }
          else
          {
            retFreeVO.setCinventoryid(sMangId);
            m_hashInvFreeVO.put(sMangId, retFreeVO);
          }
        }
      }
    }
  }

  public static UFDouble getInvTaxRate(String sBaseId)
  {
    if ((sBaseId == null) || (sBaseId.trim().length() < 1)) {
      return null;
    }

    if (!m_hashInvTaxRate.containsKey(sBaseId))
    {
      loadBatchTaxrate(new String[] { sBaseId });
    }
    Object oRet = m_hashInvTaxRate.get(sBaseId);
    if ((oRet instanceof String)) {
      return null;
    }
    return (UFDouble)oRet;
  }

  public static void loadBatchMainUnit(String[] saBaseId)
  {
    if (saBaseId == null) {
      return;
    }

    if (m_htmapMainUnit == null) {
      m_htmapMainUnit = new HashMap();
    }

    String[] saQueryId = PuPubVO.getNotExistedKeys(m_htmapMainUnit, saBaseId);

    if (saQueryId == null) {
      return;
    }

    Object[] oaRet = null;
    try {
      oaRet = (Object[])(Object[])nc.ui.scm.pub.CacheTool.getColumnValue("bd_invbasdoc", "pk_invbasdoc", "pk_measdoc", saQueryId);
    }
    catch (BusinessException e) {
      System.out.println(e.getMessage());
    }
    if (oaRet == null) {
      return;
    }

    int iLen = saQueryId.length;
    for (int i = 0; i < iLen; i++)
      m_htmapMainUnit.put(saQueryId[i], PuPubVO.getString_TrimZeroLenAsNull(oaRet[i]));
  }

  public static boolean isAssUnitManaged(String sInvBaseId)
  {
    if (PuPubVO.getString_TrimZeroLenAsNull(sInvBaseId) == null) {
      return false;
    }
    if ((m_htmapAssistManaged == null) || (!m_htmapAssistManaged.containsKey(sInvBaseId)))
    {
      loadBatchAssistManaged(new String[] { sInvBaseId });
    }

    UFBoolean bRet = (UFBoolean)m_htmapAssistManaged.get(sInvBaseId);
    if (bRet == null) {
      return false;
    }

    return bRet.booleanValue();
  }

  public static boolean isBatchManaged(String sInvMangId)
  {
    if (PuPubVO.getString_TrimZeroLenAsNull(sInvMangId) == null) {
      return false;
    }

    if ((m_htmapBatchCodeManaged == null) || (!m_htmapBatchCodeManaged.containsKey(sInvMangId)))
    {
      loadBatchProdNumMngt(new String[] { sInvMangId });
    }

    String sRet = PuPubVO.getString_TrimZeroLenAsNull(m_htmapBatchCodeManaged.get(sInvMangId));

    return (sRet != null) && (!sRet.equalsIgnoreCase("N"));
  }

  /** @deprecated */
  public static boolean isDiscount(String sInvBaseId)
  {
    if (PuPubVO.getString_TrimZeroLenAsNull(sInvBaseId) == null) {
      return false;
    }

    FormulaParse formulaParse = new FormulaParse();
    Hashtable varry = new Hashtable();

    varry.put("cbaseid", StringUtil.toString(sInvBaseId));

    formulaParse.setData(varry);
    formulaParse.setExpress("discountflag->getColValue(bd_invbasdoc,discountflag,pk_invbasdoc,cbaseid)");

    String sRet = formulaParse.getValue();
    varry.remove("cbaseid");
    return (PuPubVO.getString_TrimZeroLenAsNull(sRet) != null) && (!sRet.equalsIgnoreCase("N"));
  }

  public static boolean isFixedConvertRate(String sBaseId, String sAssistUnit)
  {
    if ((PuPubVO.getString_TrimZeroLenAsNull(sBaseId) == null) || (PuPubVO.getString_TrimZeroLenAsNull(sAssistUnit) == null))
    {
      return true;
    }

    Object[] oRet = getInvConvRateInfo(sBaseId, sAssistUnit);
    if (oRet != null) {
      return ((UFBoolean)oRet[1]).booleanValue();
    }

    return true;
  }

  public static boolean isFreeMngt(String sMangId)
  {
    if (PuPubVO.getString_TrimZeroLenAsNull(sMangId) == null) {
      return false;
    }

    FreeVO voFree = getInvFreeVO(sMangId);
    return voFree != null;
  }

  /** @deprecated */
  public static boolean isLabor(String sInvBaseId)
  {
    if (PuPubVO.getString_TrimZeroLenAsNull(sInvBaseId) == null) {
      return false;
    }

    Hashtable varry = new Hashtable();

    varry.put("cbaseid", StringUtil.toString(sInvBaseId));

    FormulaParse formulaParse = new FormulaParse();
    formulaParse.setData(varry);
    formulaParse.setExpress("laborflag->getColValue(bd_invbasdoc,laborflag,pk_invbasdoc,cbaseid)");

    String sRet = formulaParse.getValue();
    varry.remove("cbaseid");

    return (PuPubVO.getString_TrimZeroLenAsNull(sRet) != null) && (!sRet.equalsIgnoreCase("N"));
  }

  public static void loadBatchInvConvRateInfo(String[] saBaseId, String[] saAssistUnit)
  {
    if ((saBaseId == null) || (saAssistUnit == null) || (saBaseId.length == 0)) {
      return;
    }

    if (m_hashInvConvRate == null) {
      m_hashInvConvRate = new Hashtable();
    }

    int iLen = saBaseId.length;
    String sKey = null;
    ArrayList listBaseId = new ArrayList();
    ArrayList listAssistUnit = new ArrayList();
    for (int i = 0; i < iLen; i++) {
      if ((PuPubVO.getString_TrimZeroLenAsNull(saBaseId[i]) == null) || (PuPubVO.getString_TrimZeroLenAsNull(saAssistUnit[i]) == null))
      {
        continue;
      }

      sKey = saBaseId[i] + saAssistUnit[i];
      if (!m_hashInvConvRate.containsKey(sKey)) {
        listBaseId.add(saBaseId[i]);
        listAssistUnit.add(saAssistUnit[i]);
      }
    }

    int iQueryLen = listBaseId.size();
    if (iQueryLen == 0) {
      return;
    }

    String[] saQueryBaseId = (String[])(String[])listBaseId.toArray(new String[iQueryLen]);

    String[] saQueryAssistUnit = (String[])(String[])listAssistUnit.toArray(new String[iQueryLen]);

    HashMap hmapQueryRet = null;
    try {
      hmapQueryRet = ScmPubHelper.loadBatchInvConvRateInfo(saQueryBaseId, saQueryAssistUnit);
    }
    catch (Exception e) {
      e.printStackTrace();
      return;
    }

    if ((hmapQueryRet != null) && (hmapQueryRet.size() > 0)) {
      for (int i = 0; i < iQueryLen; i++) {
        sKey = saQueryBaseId[i] + saQueryAssistUnit[i];
        if (hmapQueryRet.containsKey(sKey)) {
          m_hashInvConvRate.put(sKey, hmapQueryRet.get(sKey));
        }
      }

    }

    loadBatchMainUnit(saBaseId);
    String sMainUnit = null;
    iLen = saBaseId.length;
    for (int i = 0; i < iLen; i++)
    {
      sMainUnit = getMainUnit(saBaseId[i]);
      if (!m_hashInvConvRate.containsKey(saBaseId[i] + sMainUnit))
        m_hashInvConvRate.put(saBaseId[i] + sMainUnit, new Object[] { VariableConst.ONE_UFDOUBLE, new UFBoolean(true) });
    }
  }

  public static void loadBatchProdNumMngt(String[] saMangId)
  {
    if ((saMangId == null) || (saMangId.length == 0)) {
      return;
    }

    if (m_htmapBatchCodeManaged == null) {
      m_htmapBatchCodeManaged = new HashMap();
    }

    int iQueryLen = saMangId.length;
    HashMap mapQueryId = new HashMap();
    for (int i = 0; i < iQueryLen; i++) {
      if ((PuPubVO.getString_TrimZeroLenAsNull(saMangId[i]) == null) || (m_htmapBatchCodeManaged.containsKey(saMangId[i])) || (mapQueryId.containsKey(saMangId[i]))) {
        continue;
      }
      mapQueryId.put(saMangId[i], "");
    }

    iQueryLen = mapQueryId.size();
    if (iQueryLen == 0) {
      return;
    }
    String[] saQueryId = (String[])(String[])mapQueryId.keySet().toArray(new String[iQueryLen]);

    Object[] saRet = null;
    try {
      saRet = (Object[])(Object[])nc.ui.scm.pub.cache.CacheTool.getColumnValue("bd_invmandoc", "pk_invmandoc", "wholemanaflag", saQueryId);
    }
    catch (BusinessException e) {
      System.out.println(e.getMessage());
    }

    if (saRet != null)
      for (int i = 0; i < iQueryLen; i++)
        if (saRet[i] == null)
          m_htmapBatchCodeManaged.put(saQueryId[i], "N");
        else
          m_htmapBatchCodeManaged.put(saQueryId[i], saRet[i]);
  }

  public static String getMainUnit(String sInvBaseId)
  {
    if (sInvBaseId == null) {
      return null;
    }

    loadBatchMainUnit(new String[] { sInvBaseId });

    return (String)(String)m_htmapMainUnit.get(sInvBaseId);
  }

  public static void addBatchProdNumMngt(String sMangId, boolean bMngt)
  {
    if (PuPubVO.getString_TrimZeroLenAsNull(sMangId) == null) {
      return;
    }

    if (m_htmapBatchCodeManaged == null) {
      m_htmapBatchCodeManaged = new HashMap();
    }

    m_htmapBatchCodeManaged.put(sMangId, bMngt ? "Y" : "N");
  }

  public static Object[] getInvConvRateInfo(String sBaseId, String sAssistUnit)
  {
    if ((sBaseId == null) || (sBaseId.trim().length() < 1) || (sAssistUnit == null) || (sAssistUnit.trim().length() < 1))
    {
      return null;
    }
    if ((m_hashInvConvRate == null) || (!m_hashInvConvRate.containsKey(sBaseId + sAssistUnit)))
    {
      loadBatchInvConvRateInfo(new String[] { sBaseId }, new String[] { sAssistUnit });
    }

    Object oValue = m_hashInvConvRate.get(sBaseId + sAssistUnit);
    if ((oValue instanceof String)) {
      return null;
    }
    return (Object[])(Object[])oValue;
  }

  public static UFDouble getInvConvRateValue(String sBaseId, String sAssistUnit)
  {
    if ((sBaseId == null) || (sBaseId.trim().length() < 1) || (sAssistUnit == null) || (sAssistUnit.trim().length() < 1))
    {
      return null;
    }

    Object[] oRet = getInvConvRateInfo(sBaseId, sAssistUnit);
    if (oRet != null) {
      return (UFDouble)oRet[0];
    }

    return null;
  }

  public static void loadBatchTaxrate(String[] saBaseId)
  {
    if ((saBaseId == null) || (saBaseId.length < 1)) {
      return;
    }

    if (m_hashInvTaxRate == null) {
      m_hashInvTaxRate = new Hashtable();
    }

    int iLength = saBaseId.length;
    HashMap hmapQueryBaseId = new HashMap();
    for (int i = 0; i < iLength; i++) {
      if ((saBaseId[i] == null) || (m_hashInvTaxRate.containsKey(saBaseId[i])) || (hmapQueryBaseId.containsKey(saBaseId[i]))) {
        continue;
      }
      hmapQueryBaseId.put(saBaseId[i], "");
    }

    int iQueryLength = hmapQueryBaseId.size();
    if (iQueryLength == 0) {
      return;
    }

    String[] saQueryBaseId = new String[iQueryLength];

    hmapQueryBaseId.keySet().toArray(saQueryBaseId);

    Object[][] oRet = (Object[][])null;
    try {
      oRet = ScmPubHelper.queryArrayValue(" bd_invbasdoc INNER JOIN bd_taxitems ON bd_invbasdoc.pk_taxitems = bd_taxitems.pk_taxitems", "bd_invbasdoc.pk_invbasdoc", new String[] { "bd_taxitems.taxratio" }, saQueryBaseId);
    }
    catch (Exception e)
    {
      e.printStackTrace();
      return;
    }
    if (oRet == null) {
      for (int i = 0; i < iQueryLength; i++)
        m_hashInvTaxRate.put(saQueryBaseId[i], "");
    }
    else
      for (int i = 0; i < iQueryLength; i++)
        if ((oRet[i] == null) || (oRet[i][0] == null))
          m_hashInvTaxRate.put(saQueryBaseId[i], "");
        else
          m_hashInvTaxRate.put(saQueryBaseId[i], new UFDouble(oRet[i][0].toString()));
  }

  public static FreeVO getInvFreeVO(String sMangId)
  {
    if ((sMangId == null) || (sMangId.trim().length() < 1)) {
      return null;
    }

    loadBatchFreeVO(new String[] { sMangId });

    Object oRet = m_hashInvFreeVO.get(sMangId);
    if ((oRet instanceof String)) {
      return null;
    }
    return (FreeVO)oRet;
  }
}