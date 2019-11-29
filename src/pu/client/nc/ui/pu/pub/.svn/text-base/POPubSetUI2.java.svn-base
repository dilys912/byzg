package nc.ui.pu.pub;

import java.util.ArrayList;
import java.util.HashMap;
import nc.bs.bd.b21.BusinessCurrencyRateUtil;
import nc.bs.bd.b21.CurrencyRateUtil;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapProcessor;
import nc.ui.bd.CorpBO_Client;
import nc.ui.po.pub.PoPublicUIClass;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.para.SysInitBO_Client;
import nc.vo.bd.CorpVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.para.SysInitVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.SCMEnv;

public class POPubSetUI2
{
  private String m_sUnitCode = PoPublicUIClass.getLoginPk_corp();
  private int m_nAssistDecimal = -1;
  private int m_nMeasDecimal = -1;
  private int m_nPriceDecimal = -1;
  private int m_nMnyDecimal = -1;
  private int m_nConvertDecimal = -1;

  private HashMap m_hmapCorpCurrArith_Busi = null;

  private HashMap m_hmapCorpCurrArith_Finance = null;

  private HashMap m_hmapBothExchRateDigit = null;

  private HashMap m_hmapCurrRateDigit = null;

  private HashMap m_hmapMnyDigitBusi = null;

  private HashMap m_hmapMnyDigitFinance = null;
  private HashMap m_hmapCurrAndDate;
  private HashMap m_hasCurrAndEditable = null;

  private HashMap m_hasDigitRMB = null;

  public POPubSetUI2()
  {
  }

  public POPubSetUI2(String unitCode)
  {
    this.m_sUnitCode = unitCode;
  }

  public int getAssistDecimal()
  {
    try
    {
      if (this.m_nAssistDecimal < 0)
      {
        SysInitVO initVO = SysInitBO_Client.queryByParaCode(this.m_sUnitCode, "BD502");
        if (initVO != null)
          this.m_nAssistDecimal = Integer.parseInt(initVO.getValue().trim());
      }
    }
    catch (Exception e)
    {
      SCMEnv.out(e);
    }

    if (this.m_nAssistDecimal < 0) this.m_nAssistDecimal = 2;
    return this.m_nAssistDecimal;
  }

  public int[] getBCurrDecimal(String[] currTypeId)
  {
    int[] n = new int[currTypeId.length];
    for (int i = 0; i < n.length; i++) {
      n[i] = 2;
    }

    try
    {
      String[] s = POPubSetHelper.getBCurrDigit(currTypeId);
      if ((s != null) && (s.length > 0))
        for (int i = 0; i < n.length; i++) {
          if ((s[i] == null) || (s[i].length() <= 0)) continue; n[i] = Integer.parseInt(s[i].trim());
        }
    }
    catch (Exception e)
    {
      SCMEnv.out(e);
    }

    return n;
  }

  public int[] getBothExchRateDigit(String pk_corp, String sCurrId)
  {
    if ((pk_corp == null) || (pk_corp.trim().length() < 1) || (sCurrId == null) || (sCurrId.trim().length() < 1))
    {
      return new int[] { 2, 2 };
    }

    if (this.m_hmapBothExchRateDigit == null) {
      this.m_hmapBothExchRateDigit = new HashMap();
    }
    String sCurKey = pk_corp + sCurrId;
    int[] iaDigit = (int[])(int[])this.m_hmapBothExchRateDigit.get(sCurKey);
    if (iaDigit != null) {
      return iaDigit;
    }

    BusinessCurrencyRateUtil caCurCorp = getCurrArith_Busi(pk_corp);

    iaDigit = new int[] { 2, 2 };

    int nBDigit = 2;
    int nADigit = 2;
    try {
      boolean bZFHS = caCurCorp.isBlnLocalFrac();
      String sBCurrID = caCurCorp.getLocalCurrPK();
      String sACurrID = caCurCorp.getFracCurrPK();

      if (bZFHS)
      {
        if (sCurrId.equals(sBCurrID))
        {
          nBDigit = getExchRateDigit(pk_corp, sBCurrID, null);
        }
        else {
          nBDigit = getExchRateDigit(pk_corp, sACurrID, sBCurrID);

          if (sCurrId.equals(sACurrID))
          {
            nADigit = getExchRateDigit(pk_corp, sACurrID, null);
          }
          else {
            nADigit = getExchRateDigit(pk_corp, sCurrId, sACurrID);
          }
        }

      }
      else if (sCurrId.equals(sBCurrID))
      {
        nBDigit = getExchRateDigit(pk_corp, sBCurrID, null);
      }
      else
        nBDigit = getExchRateDigit(pk_corp, sCurrId, sBCurrID);
    }
    catch (Exception e)
    {
      SCMEnv.out(e);
      return iaDigit;
    }

    iaDigit[0] = nBDigit;
    iaDigit[1] = nADigit;

    this.m_hmapBothExchRateDigit.put(sCurKey, iaDigit);

    return iaDigit;
  }

  public boolean[] getBothExchRateEditable(String pk_corp, String sCurrId)
  {
    boolean[] baEditable = { false, false };

    if ((pk_corp == null) || (pk_corp.trim().length() < 1) || (sCurrId == null) || (sCurrId.trim().length() < 1))
    {
      SCMEnv.out("nc.ui.pu.pub.POPubSetUI.getBothExchRateEditable(String, String)传入参数不正确！");

      return baEditable;
    }

    if (this.m_hasCurrAndEditable == null) {
      this.m_hasCurrAndEditable = new HashMap();
    }
    String sCurKey = pk_corp + sCurrId;
    baEditable = (boolean[])(boolean[])this.m_hasCurrAndEditable.get(sCurKey);
    if (baEditable != null) {
      return baEditable;
    }

    baEditable = new boolean[] { true, true };
    try
    {
      if (getCurrArith_Busi(pk_corp).isBlnLocalFrac())
      {
        if (sCurrId.equals(getCurrArith_Busi(pk_corp).getLocalCurrPK()))
        {
          baEditable[0] = false;
          baEditable[1] = false;
        }
        else if (sCurrId.equals(getCurrArith_Busi(pk_corp).getFracCurrPK()))
        {
          baEditable[1] = false;
        }
      }
      else
      {
        baEditable[1] = false;

        if (sCurrId.equals(getCurrArith_Busi(pk_corp).getLocalCurrPK()))
        {
          baEditable[0] = false;
        }
      }
    } catch (Exception e) {
      SCMEnv.out(e);
      SCMEnv.out("不能判断当前币种的折本及的折辅汇率是否可编辑，暂取可编辑!");
      baEditable[0] = true;
      baEditable[1] = true;
      return baEditable;
    }

    this.m_hasCurrAndEditable.put(sCurKey, baEditable);

    return baEditable;
  }

  public UFDouble[] getBothExchRateValue(String pk_corp, String strCurr, UFDate dRateDate)
  {
    UFDouble[] dValue = { null, null };
    if ((pk_corp == null) || (pk_corp.trim().equals("")) || (dRateDate == null) || (dRateDate.toString().trim().equals("")) || (strCurr == null) || (strCurr.trim().equals("")))
    {
      return dValue;
    }

    BusinessCurrencyRateUtil caCurCorp = getCurrArith_Busi(pk_corp);

    if (this.m_hmapCurrAndDate == null) {
      this.m_hmapCurrAndDate = new HashMap();
    }

    String strRateDate = dRateDate.toString();
    String strKey = pk_corp + strRateDate + strCurr;
    if (this.m_hmapCurrAndDate.containsKey(strKey)) {
      return (UFDouble[])(UFDouble[])this.m_hmapCurrAndDate.get(strKey);
    }

    try
    {
      if (caCurCorp.isBlnLocalFrac())
      {
        if (strCurr.equals(caCurCorp.getLocalCurrPK())) {
          dValue[0] = new UFDouble(1.0D);
          dValue[1] = null;
        } else {
          dValue[0] = caCurCorp.getRate(caCurCorp.getFracCurrPK(), caCurCorp.getLocalCurrPK(), strRateDate);

          if (strCurr.equals(caCurCorp.getFracCurrPK()))
            dValue[1] = new UFDouble(1.0D);
          else 
            dValue[1] = caCurCorp.getRate(strCurr, caCurCorp.getFracCurrPK(), strRateDate);
        }
      }
      else
      {
        dValue[1] = null;

        if (strCurr.equals(caCurCorp.getLocalCurrPK()))
          dValue[0] = new UFDouble(1.0D);
        else if(!getParentCorpCode().equalsIgnoreCase("10395"))
          dValue[0] = caCurCorp.getRate(strCurr, caCurCorp.getLocalCurrPK(), strRateDate);
        else 
        {
        	dValue[0]=getMonthRate(strCurr,caCurCorp.getLocalCurrPK());
        }
      }
    }
    catch (Exception e) {
      SCMEnv.out(e);
    }

    this.m_hmapCurrAndDate.put(strKey, dValue);

    return dValue;
  }

  public int getCCurrDecimal()
  {
    try
    {
      if (this.m_nMnyDecimal < 0)
      {
        SysInitVO initVO = SysInitBO_Client.queryByParaCode(this.m_sUnitCode, "BD301");
        if (initVO != null) {
          String s = POPubSetHelper.getCurrDigit(initVO.getPkvalue());
          if ((s != null) && (s.length() > 0))
            this.m_nMnyDecimal = Integer.parseInt(s.trim());
        }
      }
    }
    catch (Exception e)
    {
      SCMEnv.out(e);
    }

    if (this.m_nMnyDecimal < 0) this.m_nMnyDecimal = 2;
    return this.m_nMnyDecimal;
  }

  public int getCCurrDecimal(String strPkCorp)
  {
    Integer iTmp = null;

    if ((this.m_hasDigitRMB == null) || (!this.m_hasDigitRMB.containsKey(strPkCorp))) {
      try {
        SysInitVO initVO = SysInitBO_Client.queryByParaCode(strPkCorp, "BD301");
        if (initVO != null) {
          String s = POPubSetHelper.getCurrDigit(initVO.getPkvalue());
          if ((s != null) && (s.length() > 0))
            iTmp = new Integer(s.trim());
        }
      }
      catch (Exception e) {
        SCMEnv.out("取公司主键为：" + strPkCorp + " 的币种精度时出错！");
        SCMEnv.out(e);
        iTmp = new Integer(2);
      }
      if (this.m_hasDigitRMB == null) {
        this.m_hasDigitRMB = new HashMap();
      }
      if (iTmp == null) {
        iTmp = new Integer(2);
      }
      this.m_hasDigitRMB.put(strPkCorp, iTmp);
    }

    iTmp = (Integer)this.m_hasDigitRMB.get(strPkCorp);

    if (iTmp == null) {
      return 2;
    }
    return iTmp.intValue();
  }

  public int getConvertDecimal()
  {
    try
    {
      if (this.m_nConvertDecimal < 0)
      {
        SysInitVO initVO = SysInitBO_Client.queryByParaCode(this.m_sUnitCode, "BD503");
        if (initVO != null)
          this.m_nConvertDecimal = Integer.parseInt(initVO.getValue().trim());
      }
    }
    catch (Exception e)
    {
      SCMEnv.out(e);
    }

    if (this.m_nConvertDecimal < 0) this.m_nConvertDecimal = 2;
    return this.m_nConvertDecimal;
  }

  public BusinessCurrencyRateUtil getCurrArith_Busi(String pk_corp)
  {
    if ((pk_corp == null) || (pk_corp.trim().length() < 1)) {
      SCMEnv.out("nc.ui.pu.pub.POPubSetUI.getCurrArith(String)传入参数不正确！");
      return null;
    }

    if (this.m_hmapCorpCurrArith_Busi == null) {
      this.m_hmapCorpCurrArith_Busi = new HashMap();
    }

    BusinessCurrencyRateUtil caCurCorp = (BusinessCurrencyRateUtil)this.m_hmapCorpCurrArith_Busi.get(pk_corp);
    if (caCurCorp == null) {
      try {
        caCurCorp = new BusinessCurrencyRateUtil(pk_corp);
      } catch (Exception e) {
        SCMEnv.out("调用总账接口方法报错：" + e.getMessage());
      }

      if (caCurCorp == null) {
        SCMEnv.out("nc.ui.pi.pub.getBothExchangeRateDigit(String, String)传入参数不正确！");
        return null;
      }

      this.m_hmapCorpCurrArith_Busi.put(pk_corp, caCurCorp);
    }

    return caCurCorp;
  }

  public CurrencyRateUtil getCurrArith_Finance(String pk_corp)
  {
    if (PuPubVO.getString_TrimZeroLenAsNull(pk_corp) == null) {
      SCMEnv.out("nc.ui.pu.pub.POPubSetUI.getCurrArith(String)传入参数不正确！");
      return null;
    }

    if (this.m_hmapCorpCurrArith_Finance == null) {
      this.m_hmapCorpCurrArith_Finance = new HashMap();
    }

    CurrencyRateUtil caCurCorp = (CurrencyRateUtil)this.m_hmapCorpCurrArith_Finance.get(pk_corp);
    if (caCurCorp == null) {
      caCurCorp = new CurrencyRateUtil(pk_corp);

      if (caCurCorp == null) {
        SCMEnv.out("nc.ui.pi.pub.getBothExchangeRateDigit(String, String)传入参数不正确！");
        return null;
      }

      this.m_hmapCorpCurrArith_Finance.put(pk_corp, caCurCorp);
    }

    return caCurCorp;
  }

  public UFDouble[] getExchangeRate(String currTypeId, String sDate)
  {
    CurrencyRateUtil currArith = new CurrencyRateUtil(this.m_sUnitCode);
    UFDouble dCurrRate = null;
    UFDouble dAuxiRate = null;
    try
    {
      if (currArith.isBlnLocalFrac())
      {
        if (currArith.isLocalCurrType(currTypeId))
        {
          dCurrRate = new UFDouble(1.0D);
          dAuxiRate = null;
        } else {
          dCurrRate = currArith.getRate(currArith.getFracCurrPK(), currArith.getLocalCurrPK(), sDate);
          if (currArith.isFracCurrType(currTypeId))
          {
            dAuxiRate = new UFDouble(1.0D);
          }
          else dAuxiRate = currArith.getRate(currTypeId, currArith.getFracCurrPK(), sDate);
        }
      }
      else
      {
        if (currArith.isLocalCurrType(currTypeId))
        {
          dCurrRate = new UFDouble(1.0D);
        }
        else dCurrRate = currArith.getRate(currTypeId, currArith.getLocalCurrPK(), sDate);

        dAuxiRate = null;
      }
    }
    catch (Exception e) {
      SCMEnv.out(e);
    }

    UFDouble[] d = new UFDouble[2];
    d[0] = dCurrRate;
    d[1] = dAuxiRate;

    return d;
  }

  public int getExchRateDigit(String pk_corp, String sCurrId, String sCurrIdDest)
  {
    if ((pk_corp == null) || (pk_corp.trim().length() < 1) || (sCurrId == null) || (sCurrId.trim().length() < 1))
    {
      return 2;
    }

    if (this.m_hmapCurrRateDigit == null) {
      this.m_hmapCurrRateDigit = new HashMap();
    }

    if (this.m_hmapCurrRateDigit.containsKey(sCurrId)) {
      return ((Integer)this.m_hmapCurrRateDigit.get(sCurrId)).intValue();
    }

    Integer iDigit = new Integer(2);
    try {
      iDigit = PubHelper.getExchangeDigit(pk_corp, sCurrId, sCurrIdDest);
    } catch (Exception e) {
      SCMEnv.out("获取币种汇率时出现异常：");
      SCMEnv.out(e);
    }
    this.m_hmapCurrRateDigit.put(sCurrId, iDigit);

    return iDigit.intValue();
  }

  public int getMeasDecimal()
  {
    try
    {
      if (this.m_nMeasDecimal < 0)
      {
        SysInitVO initVO = SysInitBO_Client.queryByParaCode(this.m_sUnitCode, "BD501");
        if (initVO != null)
          this.m_nMeasDecimal = Integer.parseInt(initVO.getValue().trim());
      }
    }
    catch (Exception e)
    {
      SCMEnv.out(e);
    }

    if (this.m_nMeasDecimal < 0) this.m_nMeasDecimal = 2;
    return this.m_nMeasDecimal;
  }

  public int getMoneyDigitByCurr_Busi(String strCurr)
  {
    int[] iRet = getMoneyDigitByCurr_Busi_Batch(new String[] { strCurr });
    if ((iRet != null) && (iRet.length >= 1)) {
      return iRet[0];
    }
    return 2;
  }

  public int[] getMoneyDigitByCurr_Busi_Batch(String[] saCurrId)
  {
    if ((saCurrId == null) || (saCurrId.length < 1)) {
      return null;
    }
    if (this.m_hmapMnyDigitBusi == null) {
      this.m_hmapMnyDigitBusi = new HashMap();
    }

    ArrayList listNotCurrId = new ArrayList();
    ArrayList listNotCurrPos = new ArrayList();
    int iLen = saCurrId.length;
    int[] iRet = new int[iLen];
    for (int i = 0; i < iLen; i++) {
      iRet[i] = 2;
      if (saCurrId[i] == null) {
        continue;
      }
      if (this.m_hmapMnyDigitBusi.containsKey(saCurrId[i])) {
        iRet[i] = ((Integer)this.m_hmapMnyDigitBusi.get(saCurrId[i])).intValue();
      } else {
        listNotCurrId.add(saCurrId[i]);
        listNotCurrPos.add(new Integer(i));
      }
    }
    if (listNotCurrId.size() == 0) {
      return iRet;
    }
    iLen = listNotCurrId.size();

    String strSqlPart = "pk_currtype='" + listNotCurrId.get(0) + "' ";
    for (int i = 1; i < iLen; i++) {
      strSqlPart = strSqlPart + "or pk_currtype='" + listNotCurrId.get(i) + "' ";
    }
    Integer nDigit = null;

    Object[][] ob = (Object[][])null;
    try {
      ob = PubHelper.queryResultsFromAnyTable("bd_currtype", new String[] { "pk_currtype", "currbusidigit" }, strSqlPart);
    }
    catch (Exception e)
    {
      SCMEnv.out(e);
    }
    HashMap mapIdDigit = new HashMap();
    if ((ob != null) && (ob.length > 0)) {
      for (int i = 0; i < ob.length; i++) {
        if (ob[i] == null) {
          continue;
        }
        if ((ob[i][0] != null) && (ob[i][1] != null)) {
          mapIdDigit.put(ob[i][0], new Integer(ob[i][1].toString()));
        }
      }
    }

    int iPos = -1;
    for (int i = 0; i < iLen; i++) {
      nDigit = (Integer)mapIdDigit.get(listNotCurrId.get(i));
      if (nDigit == null) {
        nDigit = new Integer(2);
      }
      iPos = ((Integer)listNotCurrPos.get(i)).intValue();
      iRet[iPos] = nDigit.intValue();
      this.m_hmapMnyDigitBusi.put(listNotCurrId.get(i), nDigit);
    }

    return iRet;
  }

  public int getMoneyDigitByCurr_Finance(String strCurr)
  {
    int[] iRet = getMoneyDigitByCurr_Finance_Batch(new String[] { strCurr });
    if ((iRet != null) && (iRet.length >= 1)) {
      return iRet[0];
    }
    return 2;
  }

  public int[] getMoneyDigitByCurr_Finance_Batch(String[] saCurrId)
  {
    if ((saCurrId == null) || (saCurrId.length < 1)) {
      return null;
    }
    if (this.m_hmapMnyDigitFinance == null) {
      this.m_hmapMnyDigitFinance = new HashMap();
    }

    ArrayList listNotCurrId = new ArrayList();
    ArrayList listNotCurrPos = new ArrayList();
    int iLen = saCurrId.length;
    int[] iRet = new int[iLen];
    for (int i = 0; i < iLen; i++) {
      iRet[i] = 2;
      if (saCurrId[i] == null) {
        continue;
      }
      if (this.m_hmapMnyDigitFinance.containsKey(saCurrId[i])) {
        iRet[i] = ((Integer)this.m_hmapMnyDigitFinance.get(saCurrId[i])).intValue();
      } else {
        listNotCurrId.add(saCurrId[i]);
        listNotCurrPos.add(new Integer(i));
      }
    }
    if (listNotCurrId.size() == 0) {
      return iRet;
    }
    iLen = listNotCurrId.size();

    String strSqlPart = "pk_currtype='" + listNotCurrId.get(0) + "' ";
    for (int i = 1; i < iLen; i++) {
      strSqlPart = strSqlPart + "or pk_currtype='" + listNotCurrId.get(i) + "' ";
    }
    Integer nDigit = null;

    Object[][] ob = (Object[][])null;
    try {
      ob = PubHelper.queryResultsFromAnyTable("bd_currtype", new String[] { "pk_currtype", "currdigit" }, strSqlPart);
    }
    catch (Exception e)
    {
      SCMEnv.out(e);
    }
    HashMap mapIdDigit = new HashMap();
    if ((ob != null) && (ob.length > 0)) {
      for (int i = 0; i < ob.length; i++) {
        if (ob[i] == null) {
          continue;
        }
        if ((ob[i][0] != null) && (ob[i][1] != null)) {
          mapIdDigit.put(ob[i][0], new Integer(ob[i][1].toString()));
        }
      }
    }

    int iPos = -1;
    for (int i = 0; i < iLen; i++) {
      nDigit = (Integer)mapIdDigit.get(listNotCurrId.get(i));
      if (nDigit == null) {
        nDigit = new Integer(2);
      }
      iPos = ((Integer)listNotCurrPos.get(i)).intValue();
      iRet[iPos] = nDigit.intValue();
      this.m_hmapMnyDigitFinance.put(listNotCurrId.get(i), nDigit);
    }

    return iRet;
  }

  public int getPriceDecimal()
  {
    try
    {
      if (this.m_nPriceDecimal < 0)
      {
        SysInitVO initVO = SysInitBO_Client.queryByParaCode(this.m_sUnitCode, "BD505");
        if (initVO != null)
          this.m_nPriceDecimal = Integer.parseInt(initVO.getValue().trim());
      }
    }
    catch (Exception e)
    {
      SCMEnv.out(e);
    }

    if (this.m_nPriceDecimal < 0) this.m_nPriceDecimal = 2;
    return this.m_nPriceDecimal;
  }

  public int getSCurrDecimal(String currTypeId)
  {
    int n = 2;
    try
    {
      String s = POPubSetHelper.getCurrDigit(currTypeId);
      if ((s != null) && (s.length() > 0)) n = Integer.parseInt(s.trim()); 
    }
    catch (Exception e)
    {
      SCMEnv.out(e);
      return 2;
    }

    return n;
  }
  
  /**
   * @功能:取月汇率
   * @author ：林桂莹
   * @2013/1/21
   * 
   * @since v50
   */
  private UFDouble getMonthRate(String curr,String oppcurr)
  {
	 UFDate serverdate= ClientEnvironment.getInstance().getDate();
	 int month= serverdate.getMonth();
	 int year=serverdate.getYear();
	 if(month==1)
	 {
		 year--;
		 month=12;
	 }
	 else 
		 month--;
     StringBuffer getRateSql=new StringBuffer();
     getRateSql.append(" SELECT rate.adjustrate ") 
	 .append("   FROM bd_adjustrate rate ") 
	 .append("  WHERE (nvl(dr, 0) = 0) ") 
	 .append("   and pk_currinfo =(select pk_currinfo from bd_currinfo where pk_corp='").append(ClientEnvironment.getInstance().getCorporation().getPk_corp()).append("' and pk_currtype='").append(curr).append("' and oppcurrtype='").append(oppcurr).append("')") 
	 .append("   and pk_accperiod = ( select pk_accperiod from bd_accperiod acc where acc.periodnum='12' and acc.periodyear='").append(year).append("') ")
     .append("   and ratemonth ='").append(month>=10?month:"0"+month).append("' "); 
     IUAPQueryBS sessionManager = (IUAPQueryBS) NCLocator
 	.getInstance().lookup(IUAPQueryBS.class.getName());
     HashMap rate = null;
     try {
    	 
    	  rate = (HashMap)sessionManager.executeQuery(getRateSql.toString(),new MapProcessor());
    	  if(rate==null||rate.size()==0)
    	  {
    			return new UFDouble(0.0);
    	  }
    	  String strrate=String.valueOf(rate.get("adjustrate"));
    	  if(strrate==null||strrate.equalsIgnoreCase("null"))
    	  {
    		  return new UFDouble(0.0); 
    	  }
    	  else 
    	  {
    		  return new UFDouble(strrate);
    	  }
         }
         catch (BusinessException e)
         {
        	 return new UFDouble(0.0);
         	
         }
	//return new UFDouble(0.0);
	  
  }
  
  /**
   * @功能:返回公司的上级公司编码
   * @author ：林桂莹
   * @2012/9/5
   * 
   * @since v50
   */
  public String getParentCorpCode() {

  	String ParentCorp = new String();
  	String key = ClientEnvironment.getInstance().getCorporation()
  			.getFathercorp();
  	try {
  		CorpVO corpVO = CorpBO_Client.findByPrimaryKey(key);
  		ParentCorp = corpVO.getUnitcode();
  	} catch (BusinessException e) {
  		// TODO Auto-generated catch block
  		e.printStackTrace();
  	}
  	return ParentCorp;
  }
}