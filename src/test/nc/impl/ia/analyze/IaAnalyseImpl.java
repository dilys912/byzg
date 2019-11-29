package nc.impl.ia.analyze;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;
import nc.bs.ia.analyze.IaAnalyseDMO;
import nc.bs.ia.pub.CommonDataDMO;
import nc.bs.ml.NCLangResOnserver;
import nc.impl.ia.pub.CommonDataImpl;
import nc.itf.ia.analyze.IIaAnalyse;
import nc.vo.ia.analyze.InvInOutSumVO;
import nc.vo.ia.analyze.QueryVO;
import nc.vo.ia.analyze.RdtypeSumVO;
import nc.vo.ia.analyze.StatisticsVO;
import nc.vo.ia.analyze.VelocityVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;

public class IaAnalyseImpl
  implements IIaAnalyse
{
  private Object[] getArray(Vector rs)
    throws BusinessException
  {
    try
    {
      if ((rs != null) && (rs.size() > 0)) {
        int rows = rs.size();
        int columns = ((Vector)rs.elementAt(0)).size();
        Object[] result = new Object[rows];
        for (int i = 0; i < rows; ++i) {
          Object[] rsRow = new Object[columns];
          for (int j = 0; j < columns; ++j) {
            rsRow[j] = ((Vector)rs.elementAt(i)).elementAt(j);
          }
          result[i] = rsRow;
        }
        return result;
      }
    }
    catch (Exception e) {
      throw new BusinessException(e);
    }
    return null;
  }

  public Object[] getInCost(QueryVO query)
    throws BusinessException
  {
    Vector rs = null;
    Object[] result = null;
    try {
      IaAnalyseDMO dmo = new IaAnalyseDMO();
      rs = dmo.getInCost(query);
      result = getArray(rs);
    } catch (Exception e) {
      throw new BusinessException(e);
    }
    return result;
  }

  public Object[] getAbc(String sql, String codes, String flag, String[] params, String xsfp)
    throws BusinessException
  {
    Vector rs = null;
    Object[] result = null;
    try {
      IaAnalyseDMO dmo = new IaAnalyseDMO();
      rs = dmo.getAbc(sql, codes, flag, params, xsfp);
      result = getArray(rs);
    } catch (Exception e) {
      throw new BusinessException(e);
    }
    return result;
  }

  private CommonDataImpl getCommonDataBO()
    throws Exception
  {
    CommonDataImpl boCommonData = new CommonDataImpl();
    return boCommonData;
  }

  public InvInOutSumVO[] getDiffAlloc(QueryVO qvo)
    throws BusinessException
  {
    InvInOutSumVO[] rs = null;
    try {
      IaAnalyseDMO dmo = new IaAnalyseDMO();
      rs = dmo.getDiffAlloc(qvo);
    } catch (Exception e) {
      throw new BusinessException(e);
    }
    return rs;
  }

  private String[] getGroupField(QueryVO voQuery)
  {
    Vector vGroupField = new Vector();
    StatisticsVO[] voaStatistics = voQuery.getStatistics();
    for (int i = 0; i < voaStatistics.length; ++i)
    {
      if (voaStatistics[i].getFieldCode().equals("crdcenterid"))
      {
        vGroupField.addElement("crdcenterid");
      }
      else if (voaStatistics[i].getFieldCode().equals("cwarehouseid"))
      {
        vGroupField.addElement("cwarehouseid");
      }
      else if (voaStatistics[i].getFieldCode().equals("pk_invcl"))
      {
        vGroupField.addElement("cinventoryid");

        vGroupField.addElement("pk_invcl");
      }
      else if (voaStatistics[i].getFieldCode().equals("cinventoryid"))
      {
        vGroupField.addElement("cinventoryid");
        vGroupField.addElement("vbatch");
        if (!(voQuery.isShowAssistant()))
          continue;
        vGroupField.add("castunitid");
      }
      else if (voaStatistics[i].getFieldCode().equals("pk_corp"))
      {
        vGroupField.addElement("pk_corp");
      }
      else if (voaStatistics[i].getFieldCode().equals("cdeptid"))
      {
        vGroupField.addElement("cdeptid");
      } else {
        if (!(voaStatistics[i].getFieldCode().equals("cemployeeid")))
          continue;
        vGroupField.addElement("cemployeeid");
      }
    }
    String[] saGroupField = new String[vGroupField.size()];
    vGroupField.copyInto(saGroupField);
    return saGroupField;
  }

  public InvInOutSumVO[] getInBill(QueryVO conVO)
    throws BusinessException
  {
    InvInOutSumVO[] vResult = null;
    try
    {
      String[] sBizs = new String[2];
      sBizs[0] = "FQSK";
      sBizs[1] = "WTDX";
      String sFQSK = ""; String sWTDX = "";
      String[] corps = conVO.getPk_Corps();
      String[] period = conVO.getPeriod();
      for (int i = 0; i < corps.length; ++i)
      {
        Hashtable ht = getCommonDataBO().getBizTypeIDs(corps[i], sBizs);
        if (sFQSK.length() > 0) {
          sFQSK = sFQSK + ",";
        }
        sFQSK = (String)ht.get("FQSK");

        if (sWTDX.length() > 0) {
          sWTDX = sWTDX + ",";
        }
        sWTDX = (String)ht.get("WTDX");
      }
      conVO.sFQSK = sFQSK;
      conVO.sWTDX = sWTDX;

      String[] dates = new String[2];

      dates[0] = getCommonDataBO().getMonthBeginDate(corps[0], period[0]).toString();
      dates[1] = getCommonDataBO().getMonthEndDate(corps[0], period[1]).toString();

      conVO.setDate(dates);

      IaAnalyseDMO dmo = new IaAnalyseDMO();
      vResult = dmo.getInBill(conVO);

      CommonDataDMO.objectReference(vResult);
    }
    catch (Exception e) {
      throw new BusinessException(e);
    }
    return vResult;
  }

  public ArrayList getInOutSum(QueryVO voQuery)
    throws BusinessException
  {
    InvInOutSumVO[] result = null;
    ArrayList alResult = new ArrayList();
    try
    {
//      ConditionVO[] voaCondition = voQuery.getConditionVO();
    //edit by zwx 2019-11-14 Ôö¼Ó¹ýÂËÓ¡ÌúµÄ²Ö¿â£º±¦Ó¡¼ôÇÐ¿â£¨×÷·Ï£©¡¢±¦Ó¡³ÉÆ·¿â(×÷·Ï)¡¢±¦Ó¡°ë³ÉÆ·¿â(·â´æ)
      String yswhere = voQuery.getWhere();
      if(yswhere.length()>0){
    	  yswhere =yswhere+" and  cwarehouseid not in ('1020A110000000001WQG','1020A110000000001WQI','1020A110000000001WQM')";
      }else{
    	  yswhere = " cwarehouseid not in ('1020A110000000001WQG','1020A110000000001WQI','1020A110000000001WQM')";
      }
    
      voQuery.setWhere(yswhere);
      ConditionVO[] oldCondition = voQuery.getConditionVO();
      ConditionVO condition = new ConditionVO();
	  condition.setFieldCode("cwarehouseid");
	  condition.setOperaCode("not in ");
	  condition.setDataType(0);
	  condition.setValue("('1020A110000000001WQG','1020A110000000001WQI','1020A110000000001WQM')");
	  ConditionVO[] voaCondition = new ConditionVO[oldCondition.length+1];
	  System.arraycopy(oldCondition, 0, voaCondition, 0, oldCondition.length);
	  voaCondition[oldCondition.length]=condition;
	  voQuery.setConditionVO(voaCondition);
	  //end by zwx 
      for (int i = 0; i < voaCondition.length; ++i)
      {
        if (voaCondition[i].getFieldCode().indexOf("caccountmonth") < 0)
          continue;
        UFDate[] ufdaDate = getCommonDataBO().getMonthDates(voQuery.getPk_Corps()[0], voaCondition[i].getValue());
        if ((ufdaDate == null) || (ufdaDate.length <= 0))
          continue;
        String[] saDate = new String[2];
        saDate[0] = ufdaDate[0].toString();
        saDate[1] = ufdaDate[1].toString();
        voQuery.setDate(saDate);
        break;
      }

      StatisticsVO[] voStatistics = voQuery.getStatistics();
      Vector vTempKey = new Vector(1, 1);
      Vector vTempVO = new Vector(1, 1);

      if (voStatistics == null)
      {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("201490", "UPP201490-000000"));
      }

      for (int j = 0; j < voStatistics.length; ++j)
      {
        vTempKey.addElement(voStatistics[j].getFieldCode());
        vTempVO.addElement(voStatistics[j]);
      }
      if ((vTempKey.contains("v.cinventoryid")) && (vTempKey.contains("inv.invtype")))
      {
        vTempVO.removeElementAt(vTempKey.indexOf("v.cinventoryid"));
      }
      voStatistics = new StatisticsVO[vTempVO.size()];
      vTempVO.copyInto(voStatistics);
      voQuery.setStatistics(voStatistics);
      String[] sBizs = new String[2];
      sBizs[0] = "FQSK";
      sBizs[1] = "WTDX";
      String sFQSK = "";
      String sWTDX = "";
      String[] sCorpIDs = voQuery.getPk_Corps();
      for (int i = 0; i < sCorpIDs.length; ++i)
      {
        Hashtable ht = getCommonDataBO().getBizTypeIDs(sCorpIDs[i], sBizs);
        if (sFQSK.length() > 0) {
          sFQSK = sFQSK + ",";
        }
        sFQSK = (String)ht.get("FQSK");

        if (sWTDX.length() > 0) {
          sWTDX = sWTDX + ",";
        }
        sWTDX = (String)ht.get("WTDX");
      }
      voQuery.sFQSK = sFQSK;
      voQuery.sWTDX = sWTDX;
      IaAnalyseDMO dmo = new IaAnalyseDMO();
      result = dmo.getInOutSum(voQuery);
      if ((result != null) && (voQuery.isSplitRdcl()))
      {
        alResult = splitRdType(result, voQuery);
      }
      else
      {
        alResult.add(0, result);
        alResult.add(1, null);
        alResult.add(2, null);
      }

      CommonDataDMO.objectReference(alResult);
    }
    catch (Exception e) {
      throw new BusinessException(e);
    }

    return alResult;
  }

  public InvInOutSumVO[] getOutBill(QueryVO conVO)
    throws BusinessException
  {
    InvInOutSumVO[] vResult = null;
    try
    {
      String[] sBizs = new String[2];
      sBizs[0] = "FQSK";
      sBizs[1] = "WTDX";
      String sFQSK = ""; String sWTDX = "";
      String[] corps = conVO.getPk_Corps();
      String[] period = conVO.getPeriod();
      for (int i = 0; i < corps.length; ++i)
      {
        Hashtable ht = getCommonDataBO().getBizTypeIDs(corps[i], sBizs);
        if (sFQSK.length() > 0) {
          sFQSK = sFQSK + ",";
        }
        sFQSK = (String)ht.get("FQSK");

        if (sWTDX.length() > 0) {
          sWTDX = sWTDX + ",";
        }
        sWTDX = (String)ht.get("WTDX");
      }

      conVO.sFQSK = sFQSK;
      conVO.sWTDX = sWTDX;

      String[] dates = new String[2];
      dates[0] = getCommonDataBO().getMonthBeginDate(corps[0], period[0]).toString();
      dates[1] = getCommonDataBO().getMonthEndDate(corps[0], period[1]).toString();

      conVO.setDate(dates);

      IaAnalyseDMO dmo = new IaAnalyseDMO();
      vResult = dmo.getOutBill(conVO);

      CommonDataDMO.objectReference(vResult);
    }
    catch (Exception e)
    {
      throw new BusinessException(e);
    }
    return vResult;
  }

  private ArrayList getRdcl(InvInOutSumVO[] voaItem, QueryVO voQuery)
  {
    Hashtable htIn = new Hashtable();
    Hashtable htOut = new Hashtable();

    StringBuffer sbGroupValue = null;

    String sLastValue = null;

    int iInCount = 0; int iOutCount = 0;
    boolean bHaveNullIn = false; boolean bHaveNullOut = false;

    String[] saGroupField = getGroupField(voQuery);
    String NULL_TYPE = "*_*";
    for (int i = 0; i < voaItem.length; ++i) {
      if (voaItem[i] == null) {
        continue;
      }
      sbGroupValue = new StringBuffer();
      for (int j = 0; j < saGroupField.length; ++j)
      {
        Object oValue = voaItem[i].getAttributeValue(saGroupField[j]);
        if ((oValue == null) || (oValue.toString().length() <= 0))
          continue;
        sbGroupValue.append(oValue.toString());
      }

      String sRdName = voaItem[i].getRdName();
      Integer iRdFlag = voaItem[i].getRdFlag();

      if (i == 0)
      {
        sLastValue = sbGroupValue.toString();
      }
      if (!(sbGroupValue.toString().equals(sLastValue)))
      {
        if ((bHaveNullOut) && (!(htOut.containsValue("*_*"))))
        {
          htOut.put(new Integer(iOutCount), "*_*");
          ++iOutCount;
        } else if ((bHaveNullIn) && (!(htIn.containsValue("*_*"))))
        {
          htIn.put(new Integer(iInCount), "*_*");
          ++iInCount;
        }

        sLastValue = sbGroupValue.toString();
        bHaveNullIn = false;
        bHaveNullOut = false;
      }

      if ((sRdName == null) || (sRdName.trim().length() == 0))
      {
        if (iRdFlag != null) {
          if (iRdFlag.intValue() == 1)
          {
            bHaveNullOut = true;
          } else if (iRdFlag.intValue() == 0)
          {
            bHaveNullIn = true;
          }
        }

      }
      else if ((iRdFlag.intValue() == 1) && (!(htOut.containsValue(sRdName.trim()))))
      {
        htOut.put(new Integer(iOutCount), sRdName.trim());
        ++iOutCount;
      }
      else if ((iRdFlag.intValue() == 0) && (!(htIn.containsValue(sRdName.trim()))))
      {
        htIn.put(new Integer(iInCount), sRdName.trim());
        ++iInCount;
      }

      if ((bHaveNullOut) && (!(htOut.containsValue("*_*")))) {
        htOut.put(new Integer(iOutCount), "*_*");
        ++iOutCount; } else {
        if ((!(bHaveNullIn)) || (htIn.containsValue("*_*")))
          continue;
        htIn.put(new Integer(iInCount), "*_*");
        ++iInCount;
      }
    }

    ArrayList alReturn = new ArrayList();
    alReturn.add(0, htIn);
    alReturn.add(1, htOut);
    return alReturn;
  }

  public Object[] getStockCapital(String sql, Integer iflag, String xsfp)
    throws BusinessException
  {
    Vector rs = null;
    Object[] result = null;
    try {
      IaAnalyseDMO dmo = new IaAnalyseDMO();
      rs = dmo.getStockCapital(sql, iflag, xsfp);
      result = getArray(rs);
    } catch (Exception e) {
      throw new BusinessException(e);
    }
    return result;
  }

  public VelocityVO[] getVelocity(String sql, String[] param, String xsfp, StatisticsVO[] voFormats)
    throws BusinessException
  {
    VelocityVO[] rs = null;
    try {
      IaAnalyseDMO dmo = new IaAnalyseDMO();
      rs = dmo.getVelocity(sql, param, xsfp, voFormats);
    } catch (Exception e) {
      throw new BusinessException(e);
    }
    return rs;
  }

  private ArrayList splitRdType(InvInOutSumVO[] voaItem, QueryVO voQuery)
  {
    String[] saGroupField = getGroupField(voQuery);

    ArrayList alTemp = getRdcl(voaItem, voQuery);
    Hashtable htRdIn = new Hashtable();
    Hashtable htRdOut = new Hashtable();
    htRdIn = (Hashtable)alTemp.get(0);
    htRdOut = (Hashtable)alTemp.get(1);

    voaItem = sumRdType(saGroupField, voaItem, htRdIn, htRdOut);

    ArrayList alRet = new ArrayList();
    alRet.add(voaItem);
    alRet.add(htRdIn);
    alRet.add(htRdOut);
    return alRet;
  }

  private InvInOutSumVO[] sumRdType(String[] saGroupField, InvInOutSumVO[] voaItem, Hashtable htRdIn, Hashtable htRdOut)
  {
	return voaItem;
//    if ((saGroupField == null) || (saGroupField.length == 0) || (voaItem == null) || (voaItem.length == 0) || (htRdOut == null) || (htRdIn == null))
//    {
//      return null;
//    }
//
//    Object oTempValue = null;
//    String sRdName = null;
//    Integer iRdFlag = null;
//    int iRdInCount = htRdIn.size(); int iRdOutCount = htRdOut.size();
//    RdtypeSumVO voRdTypeSum = null;
//    InvInOutSumVO voLastData = null;
//
//    InvInOutSumVO voTemp = new InvInOutSumVO();
//    Vector vTemp = new Vector();
//
//    UFDouble ufdBeginnum = null;
//    UFDouble ufdBeginastnum = null;
//    UFDouble ufdBeginmny = null;
//    UFDouble ufdBeginplanedmny = null;
//    UFDouble ufdBeginvarymny = null;
//
//    UFDouble ufdAbnum = null;
//    UFDouble ufdAbastnum = null;
//    UFDouble ufdAbmny = null;
//    UFDouble ufdAbplanedmny = null;
//    UFDouble ufdAbvarymny = null;
//
//    int iIdIndex = -1;
//    String NULL = "*_*";
//
//    Vector vCode = new Vector();
//    for (int i = 0; i < voaItem.length; ++i) {
//      if (voaItem[i] == null)
//        continue;
//      String sKey = "";
//      for (int j = 0; j < saGroupField.length; ++j) {
//        oTempValue = voaItem[i].getAttributeValue(saGroupField[j]);
//        if ((oTempValue != null) && (oTempValue.toString().trim().length() > 0)) {
//          sKey = sKey + oTempValue.toString().trim();
//        }
//      }
//
//      int iIndex = vCode.indexOf(sKey);
//      voTemp = voaItem[i];
//      voRdTypeSum = new RdtypeSumVO();
//
//      if (iIndex < 0)
//      {
//        vCode.addElement(sKey);
//        vTemp.add(voTemp);
//
//        voTemp.initRdTypeSize(iRdInCount, iRdOutCount);
//        ufdBeginnum = new UFDouble(0.0D);
//        ufdBeginastnum = new UFDouble(0.0D);
//        ufdBeginmny = new UFDouble(0.0D);
//        ufdBeginplanedmny = new UFDouble(0.0D);
//        ufdBeginvarymny = new UFDouble(0.0D);
//
//        ufdAbnum = new UFDouble(0.0D);
//        ufdAbastnum = new UFDouble(0.0D);
//        ufdAbmny = new UFDouble(0.0D);
//        ufdAbplanedmny = new UFDouble(0.0D);
//        ufdAbvarymny = new UFDouble(0.0D);
//      }
//      else {
//        voLastData = (InvInOutSumVO)vTemp.elementAt(iIndex);
//
//        ufdBeginnum = voLastData.getNbeginnum().add(voTemp.getNbeginnum());
//        voLastData.setNbeginnum(ufdBeginnum);
//        UFDouble ufdTemp0 = voTemp.getNbeginastnum();
//        ufdTemp0 = (ufdTemp0 == null) ? new UFDouble(0.0D) : ufdTemp0;
//        ufdBeginastnum = ufdTemp0.add((voLastData.getNbeginastnum() == null) ? new UFDouble(0.0D) : voLastData.getNbeginastnum());
//        voLastData.setNbeginastnum(ufdBeginastnum);
//        ufdBeginmny = voLastData.getNbeginmny().add(voTemp.getNbeginmny());
//        voLastData.setNbeginmny(ufdBeginmny);
//        ufdBeginplanedmny = voLastData.getNbeginplanedmny().add(voTemp.getNbeginplanedmny());
//        voLastData.setNbeginplanedmny(ufdBeginplanedmny);
//        ufdBeginvarymny = voLastData.getNbeginvarydmny().add(voTemp.getNbeginvarydmny());
//        voLastData.setNbeginvarymny(ufdBeginvarymny);
//
//        ufdAbnum = voLastData.getNabnum().add(voTemp.getNabnum());
//        voLastData.setNabnum(ufdAbnum);
//        ufdTemp0 = (voLastData.getNabastnum() == null) ? new UFDouble(0.0D) : voLastData.getNabastnum();
//        ufdAbastnum = ufdTemp0.add((voTemp.getNabastnum() == null) ? new UFDouble(0.0D) : voTemp.getNabastnum());
//        voLastData.setNabastnum(ufdAbastnum);
//        ufdAbmny = voLastData.getNabmny().add(voTemp.getNabmny());
//        voLastData.setNabmny(ufdAbmny);
//        ufdAbplanedmny = voLastData.getNabplanedmny().add(voTemp.getNabplanedmny());
//        voLastData.setNabplanedmny(ufdAbplanedmny);
//        ufdAbvarymny = voLastData.getNabvarydmny().add(voTemp.getNabvarydmny());
//        voLastData.setNabvarymny(ufdAbvarymny);
//      }
//
//      sRdName = voTemp.getRdName();
//      iRdFlag = voTemp.getRdFlag();
//
//      if ((sRdName == null) || (sRdName.trim().length() == 0))
//        sRdName = "*_*";
//      int iFind;
//      UFDouble ufdTemp;
//      UFDouble ufdTempMny;
//      RdtypeSumVO voRdTemp;
//      if ((iRdFlag != null) && (iRdFlag.toString().trim().equals("1")))
//      {
//        iIdIndex = -1;
//        for (iFind = 0; iFind < htRdOut.size(); ++iFind)
//        {
//          if (!(sRdName.trim().equals(((String)htRdOut.get(new Integer(iFind))).trim())))
//            continue;
//          iIdIndex = iFind;
//          break;
//        }
//
//        ufdTemp = voTemp.getNoutnum();
//        voRdTypeSum.setNum(ufdTemp);
//
//        if ((ufdTemp != null) && (ufdTemp.doubleValue() != 0.0D))
//        {
//          ufdTempMny = voTemp.getNoutmny();
//          if (ufdTempMny != null)
//          {
//            voRdTypeSum.setNPrice(ufdTempMny.div(ufdTemp));
//          }
//          ufdTempMny = voTemp.getNoutplanedmny();
//          if (ufdTempMny != null)
//          {
//            voRdTypeSum.setNPPrice(ufdTempMny.div(ufdTemp));
//          }
//        }
//
//        ufdTemp = voTemp.getNoutastnum();
//        ufdTemp = (ufdTemp == null) ? new UFDouble(0.0D) : ufdTemp;
//        voRdTypeSum.setNastNum(ufdTemp);
//
//        voRdTypeSum.setNmny(voTemp.getNoutmny());
//        voRdTypeSum.setNpmny(voTemp.getNoutplanedmny());
//        voRdTypeSum.setNvarymny(voTemp.getNoutvarydmny());
//
//        if (iIndex < 0)
//        {
//          voTemp.setRdType(1, iIdIndex, voRdTypeSum);
//        }
//        else
//        {
//          if (iIdIndex != -1)
//          {
//            voRdTemp = voLastData.getRdType(1, iIdIndex);
//            if (voRdTemp != null)
//            {
//              voRdTypeSum.setNum(voRdTemp.getNnum().add(voRdTypeSum.getNnum()));
//              voRdTypeSum.setNmny(voRdTemp.getNmny().add(voRdTypeSum.getNmny()));
//              if (voRdTypeSum.getNnum().doubleValue() != 0.0D)
//              {
//                voRdTypeSum.setNPrice(voRdTypeSum.getNmny().div(voRdTypeSum.getNnum()));
//                voRdTypeSum.setNPPrice(voRdTypeSum.getNpmny().div(voRdTypeSum.getNnum()));
//              }
//              voRdTypeSum.setNastNum(voRdTemp.getNastNum().add(voRdTypeSum.getNastNum()));
//              voRdTypeSum.setNpmny(voRdTemp.getNpmny().add(voRdTypeSum.getNpmny()));
//              voRdTypeSum.setNvarymny(voRdTemp.getNvarymny().add(voRdTypeSum.getNvarymny()));
//            }
//          }
//          voLastData.setRdType(1, iIdIndex, voRdTypeSum);
//          vTemp.setElementAt(voLastData, iIndex);
//        }
//      } else {
//        if ((iRdFlag == null) || (!(iRdFlag.toString().trim().equals("0"))))
//          continue;
//        iIdIndex = -1;
//        for (ufdTemp = 0; ufdTemp < htRdIn.size(); ++ufdTemp)
//        {
//          if (!(sRdName.trim().equals(((String)htRdIn.get(new Integer(ufdTemp))).trim())))
//            continue;
//          iIdIndex = ufdTemp;
//          break;
//        }
//
//        ufdTemp = voTemp.getNinnum();
//        voRdTypeSum.setNum(ufdTemp);
//
//        if ((ufdTemp != null) && (ufdTemp.doubleValue() != 0.0D))
//        {
//          voRdTemp = voTemp.getNinmny();
//          if (voRdTemp != null)
//          {
//            voRdTypeSum.setNPrice(voRdTemp.div(ufdTemp));
//          }
//          voRdTemp = voTemp.getNinplanedmny();
//          if (voRdTemp != null)
//          {
//            voRdTypeSum.setNPPrice(voRdTemp.div(ufdTemp));
//          }
//        }
//
//        ufdTemp = voTemp.getNinastnum();
//        ufdTemp = (ufdTemp == null) ? new UFDouble(0.0D) : ufdTemp;
//        voRdTypeSum.setNastNum(ufdTemp);
//
//        ufdTemp = voTemp.getNinmny();
//        voRdTypeSum.setNmny(ufdTemp);
//
//        ufdTemp = voTemp.getNinplanedmny();
//        voRdTypeSum.setNpmny(ufdTemp);
//
//        ufdTemp = voTemp.getNinvarydmny();
//        voRdTypeSum.setNvarymny(ufdTemp);
//
//        if (iIndex < 0)
//        {
//          voTemp.setRdType(0, iIdIndex, voRdTypeSum);
//        }
//        else
//        {
//          if (iIdIndex != -1)
//          {
//            voRdTemp = voLastData.getRdType(0, iIdIndex);
//            if (voRdTemp != null)
//            {
//              voRdTypeSum.setNum(voRdTemp.getNnum().add(voRdTypeSum.getNnum()));
//              voRdTypeSum.setNmny(voRdTemp.getNmny().add(voRdTypeSum.getNmny()));
//              if (voRdTypeSum.getNnum().doubleValue() != 0.0D)
//              {
//                voRdTypeSum.setNPrice(voRdTypeSum.getNmny().div(voRdTypeSum.getNnum()));
//                voRdTypeSum.setNPPrice(voRdTypeSum.getNpmny().div(voRdTypeSum.getNnum()));
//              }
//              voRdTypeSum.setNastNum(voRdTemp.getNastNum().add(voRdTypeSum.getNastNum()));
//              voRdTypeSum.setNpmny(voRdTemp.getNpmny().add(voRdTypeSum.getNpmny()));
//              voRdTypeSum.setNvarymny(voRdTemp.getNvarymny().add(voRdTypeSum.getNvarymny()));
//            }
//          }
//          voLastData.setRdType(0, iIdIndex, voRdTypeSum);
//          vTemp.setElementAt(voLastData, iIndex);
//        }
//      }
//
//    }
//
//    InvInOutSumVO[] voaResult = new InvInOutSumVO[vTemp.size()];
//    vTemp.copyInto(voaResult);
//    return voaResult;
  }

  public String checkCondition(QueryVO conVO)
    throws BusinessException
  {
    CommonDataImpl cbo = new CommonDataImpl();
    StringBuffer buffer = new StringBuffer(0);
    String[] pk_corps;
    String[] corpnames;
    if (conVO.SourceTable.equals(QueryVO.INOUTSUM)) {
      pk_corps = conVO.getPk_Corps();
      corpnames = conVO.getCorpNames();
      String[] dates = conVO.getDate();
      String beginPeriod = cbo.getPeriod(pk_corps[0], dates[0]);
      String endPeriod = cbo.getPeriod(pk_corps[0], dates[1]);

      String ifstartIA = checkifstartIA(pk_corps, corpnames, beginPeriod);
      if ((ifstartIA != null) && (ifstartIA.trim().length() > 0)) {
        buffer.append(ifstartIA + "\n");
      }

      String ifbeginaccount = checkifBeginAccount(pk_corps, corpnames);
      if ((ifbeginaccount != null) && (ifbeginaccount.trim().length() > 0)) {
        buffer.append(ifbeginaccount + "\n");
      }

      String result = cbo.checkUnload(pk_corps, dates[0]);
      UFDate date;
      if (!(result.equalsIgnoreCase("load"))) {
        date = cbo.getMonthBeginDate(pk_corps[0], beginPeriod);

        if (!(date.toString().equalsIgnoreCase(dates[0]))) {
          buffer.append(NCLangResOnserver.getInstance().getStrByID("2014", "UPP2014-000660") + "\n");
        }

      }

      result = null;
      result = cbo.checkUnload(pk_corps, dates[1]);
      if (!(result.equalsIgnoreCase("load"))) {
        date = cbo.getMonthEndDate(pk_corps[0], endPeriod);

        if (!(date.toString().equalsIgnoreCase(dates[1]))) {
          buffer.append(NCLangResOnserver.getInstance().getStrByID("2014", "UPP2014-000661") + "\n");
        }

      }

      if (buffer.length() > 0)
        return buffer.toString();
    }
    else if ((conVO.SourceTable.equals(QueryVO.INBILL)) || (conVO.SourceTable.equals(QueryVO.OUTBILL)))
    {
      pk_corps = conVO.getPk_Corps();
      corpnames = conVO.getCorpNames();
      String[] periods = conVO.getPeriod();

      String ifstartIA = checkifstartIA(pk_corps, corpnames, periods[0]);
      if ((ifstartIA != null) && (ifstartIA.trim().length() > 0)) {
        buffer.append(ifstartIA + "\n");
      }

      String ifbeginaccount = checkifBeginAccount(pk_corps, corpnames);
      if ((ifbeginaccount != null) && (ifbeginaccount.trim().length() > 0)) {
        buffer.append(ifbeginaccount + "\n");
      }
    }

    return null;
  }

  private String checkifstartIA(String[] corpIDs, String[] corpNames, String curPeriod) throws BusinessException
  {
    StringBuffer buffer = new StringBuffer(0);

    CommonDataImpl cbo = new CommonDataImpl();

    for (int i = 0; i < corpIDs.length; ++i)
    {
      if (!(cbo.isModuleStarted(corpIDs[i], "2014", curPeriod).booleanValue())) {
        if (buffer.length() > 0) {
          buffer.append(",");
        }
        buffer.append("\"");
        buffer.append(corpNames[i]);
        buffer.append("\"");
      }
    }

    if (buffer.length() > 0) {
      return NCLangResOnserver.getInstance().getStrByID("2014", "UPP2014-000662") + buffer.toString() + NCLangResOnserver.getInstance().getStrByID("2014", "UPP2014-000665");
    }

    return null;
  }

  private String checkifBeginAccount(String[] corpIDs, String[] corpNames) throws BusinessException
  {
    StringBuffer buffer = new StringBuffer(0);
    CommonDataImpl cbo = new CommonDataImpl();

    for (int i = 0; i < corpIDs.length; ++i) {
      if (!(cbo.isBeginAccount(corpIDs[i]).booleanValue())) {
        if (buffer.length() > 0) {
          buffer.append(",");
        }
        buffer.append("\"");
        buffer.append(corpNames[i]);
        buffer.append("\"");
      }
    }

    if (buffer.length() > 0) {
      return NCLangResOnserver.getInstance().getStrByID("2014", "UPP2014-000662") + buffer.toString() + NCLangResOnserver.getInstance().getStrByID("2014", "UPP2014-000666");
    }

    return null;
  }
}