package nc.impl.ia.query;

import java.util.Vector;
import nc.bs.ia.ia501.IaInoutledgerDMO;
import nc.bs.ia.pub.CommonDataDMO;
import nc.bs.ia.query.IaQueryDetail;
import nc.bs.ia.query.IaQueryDiffDetail;
import nc.bs.ia.query.IaQueryGeneralledger;
import nc.bs.ia.query.IaQuerySendDetail;
import nc.bs.ml.NCLangResOnserver;
import nc.impl.ia.ia501.IaInoutledgerImpl;
import nc.impl.ia.ia504.GeneralledgerImpl;
import nc.impl.ia.ia504.MonthledgerImpl;
import nc.impl.ia.ia508.GoodsledgertotalImpl;
import nc.impl.ia.pub.CommonDataImpl;
import nc.itf.ia.ia501.IIaInoutledger;
import nc.itf.ia.query.IIAQuery;
import nc.vo.ia.analyze.QueryVO;
import nc.vo.ia.ia501.IaInoutledgerPrintVO;
import nc.vo.ia.ia501.IaInoutledgerVO;
import nc.vo.ia.ia502.QueryDetailPrintVO;
import nc.vo.ia.ia503.DiffDetailPrintVO;
import nc.vo.ia.ia504.GeneralLedgerPrintVO;
import nc.vo.ia.ia504.GeneralledgerVO;
import nc.vo.ia.ia504.MonthledgerVO;
import nc.vo.ia.ia508.GoodsLedgerPrintVO;
import nc.vo.ia.ia508.GoodsledgerVO;
import nc.vo.ia.pub.ExceptionUtils;
import nc.vo.ia.query.IaQueryConditionVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

public class IAQueryImpl
  implements IIAQuery
{
  public QueryDetailPrintVO[] queryDetail(IaQueryConditionVO voCondition, Integer[] nDataPrecision)
    throws BusinessException
  {
    QueryDetailPrintVO[] voTableData = null;
    IaQueryDetail querydetail = new IaQueryDetail();
    GeneralledgerImpl gbo = new GeneralledgerImpl();
    MonthledgerImpl mbo = new MonthledgerImpl();
    CommonDataImpl cbo = new CommonDataImpl();
    boolean bJHJ = voCondition.getjhj();
    String sPk_corp = null;
    String sCrdcenterid = null;

    String sCinventoryid = null;
    String sAccperiod_start = null;
    String sAccperiod_stop = null;
    String sUnclosedperiod = null;
    String sStartPeriod = null;
    String sVbatch = " and vbatch='" + voCondition.getvbatch() + "'";

    String[][] sResult = (String[][])null;
    boolean bHaveRecord = false;
    sPk_corp = voCondition.getpk_corp() == null ? "" : voCondition.getpk_corp();

    sCrdcenterid = voCondition.getcrdcenterid() == null ? "" : voCondition.getcrdcenterid();
    try
    {
      sStartPeriod = cbo.getStartPeriod(sPk_corp);
      sUnclosedperiod = cbo.getUnClosePeriod(sPk_corp);
      voCondition.setbeginuseperiod(sStartPeriod);
      voCondition.setunclosedperiod(sUnclosedperiod);
      querydetail.initValue(voCondition, nDataPrecision);
      sResult = cbo.queryData("select pk_invmandoc from bd_invmandoc m, bd_invbasdoc b where m.pk_invbasdoc=b.pk_invbasdoc and m.pk_corp='" + sPk_corp + "' and b.invcode='" + voCondition.getcinvcode() + "'");

      if ((sResult == null) || (sResult.length == 0))
      {
        BusinessException be = new BusinessException(NCLangResOnserver.getInstance().getStrByID("201480", "UPP201480-000000"));

        throw be;
      }
      sCinventoryid = sResult[0][0].trim();
      sAccperiod_start = voCondition.getaccperiod_start();
      sAccperiod_stop = voCondition.getaccperiod_stop();

      if (sUnclosedperiod.equalsIgnoreCase("00")) {
        BusinessException be = new BusinessException(NCLangResOnserver.getInstance().getStrByID("201480", "UPP201480-000001"));

        throw be;
      }
      if ((sAccperiod_stop.compareTo(sStartPeriod) < 0) || (sAccperiod_start.compareTo(sUnclosedperiod) > 0))
      {
        BusinessException be = new BusinessException(NCLangResOnserver.getInstance().getStrByID("201480", "UPP201480-000002"));

        throw be;
      }
      if (sAccperiod_start.compareTo(sStartPeriod) < 0)
        sAccperiod_start = sStartPeriod;
      if ((sCrdcenterid.length() == 0) || (sCinventoryid.length() == 0)) {
        BusinessException be = new BusinessException(NCLangResOnserver.getInstance().getStrByID("201480", "UPP201480-000003"));

        throw be;
      }

      String sWTDX = cbo.getBizTypeID(sPk_corp, "WTDX");
      String sFQSK = cbo.getBizTypeID(sPk_corp, "FQSK");
      querydetail.setWTDX(sWTDX);
      querydetail.setFQSK(sFQSK);

      GeneralledgerVO[] gv = null;
      MonthledgerVO[] mv = null;
      if (sAccperiod_start.equals(sStartPeriod))
      {
        gv = gbo.queryByCondition(" cinventoryid ='" + sCinventoryid + "'" + " and crdcenterid='" + sCrdcenterid + "'" + " and caccountyear='" + sStartPeriod.substring(0, 4) + "' and frecordtypeflag=1" + " and ia_generalledger.pk_corp='" + sPk_corp + "'" + sVbatch);
      }
      else
      {
        String sQueryyear = null; String sQuerymonth = null;
        if (sAccperiod_start.substring(5, 7).equals("01"))
        {
          int accountyear = Integer.parseInt(sAccperiod_start.substring(0, 4));
          sQueryyear = Integer.toString(accountyear - 1);
          sQuerymonth = cbo.getLastMonthInYear(sPk_corp, sQueryyear);
        }
        else {
          sQueryyear = sAccperiod_start.substring(0, 4);
          int accountmonth = Integer.parseInt(sAccperiod_start.substring(5)) - 1;
          sQuerymonth = (accountmonth < 10 ? "0" : "") + accountmonth;
        }
        mv = mbo.queryByCondition_month(" crdcenterid='" + sCrdcenterid + "'" + " and cinventoryid ='" + sCinventoryid + "'" + " and caccountyear='" + sQueryyear + "'" + " and caccountmonth= '" + sQuerymonth + "'" + " and frecordtypeflag=3" + " and pk_corp='" + sPk_corp + "'" + sVbatch);
      }

      if (((gv != null) && (gv.length == 0)) || ((mv != null) && (mv.length == 0)))
      {
        GeneralledgerVO gv1 = new GeneralledgerVO();
        gv1.setFrecordtypeflag(Integer.valueOf("1"));
        gv1.setNabnum(new UFDouble(0));
        gv1.setNabmny(new UFDouble(0));
        gv1.setNabvarymny(new UFDouble(0));
        querydetail.insertLine1(gv1, true);
      }
      else if ((gv != null) && (gv.length > 0)) {
        if (gv[0].getNabmny() == null)
          gv[0].setNabmny(new UFDouble(0));
        if (gv[0].getNabnum() == null)
          gv[0].setNabnum(new UFDouble(0));
        if (gv[0].getNabvarymny() == null)
          gv[0].setNabvarymny(new UFDouble(0));
        if ((gv[0].getFpricemodeflag().intValue() == 5) && (bJHJ))
        {
          gv[0].setNabmny(gv[0].getNabmny().sub(gv[0].getNabvarymny()));
          gv[0].setNabprice(gv[0].getNabnum().doubleValue() == 0.0D ? null : gv[0].getNabmny().div(gv[0].getNabnum()));
        }

        querydetail.insertLine1(gv[0], true);
      }
      else if ((mv != null) && (mv.length > 0)) {
        if (mv[0].getNabmny() == null)
          mv[0].setNabmny(new UFDouble(0));
        if (mv[0].getNabvarymny() == null)
          mv[0].setNabvarymny(new UFDouble(0));
        if (mv[0].getNabnum() == null)
          mv[0].setNabnum(new UFDouble(0));
        if ((mv[0].getFpricemodeflag().intValue() == 5) && (bJHJ))
        {
          mv[0].setNabmny(mv[0].getNabmny().sub(mv[0].getNabvarymny()));
          mv[0].setNabprice(mv[0].getNabnum().doubleValue() == 0.0D ? null : mv[0].getNabmny().div(mv[0].getNabnum()));
        }

        querydetail.insertLine1(mv[0], true);
      }
      MonthledgerVO[] voMonthLedgers = null;
      voMonthLedgers = mbo.queryByCondition_month(" cinventoryid ='" + sCinventoryid + "'" + " and crdcenterid='" + sCrdcenterid + "'" + " and caccountyear || '-' || caccountmonth between '" + sAccperiod_start + "' and '" + sAccperiod_stop + "'" + " and frecordtypeflag in (2, 3)" + " and pk_corp='" + sPk_corp + "'" + sVbatch + " order by caccountyear, caccountmonth, frecordtypeflag");

      IaInoutledgerVO[] voResult = null;

      for (int m = 0; m < voMonthLedgers.length; m++) {
        if (voMonthLedgers[m].getFrecordtypeflag().intValue() == 2)
        {
          if (voMonthLedgers[m].getCaccountmonth().equals("01")) {
            querydetail.resetInOutValue();
          }
          IaInoutledgerVO condV = new IaInoutledgerVO();
          condV.setPk_corp(sPk_corp);
          condV.setCrdcenterid(sCrdcenterid);
          condV.setCinventoryid(sCinventoryid);
          condV.setCaccountyear(voMonthLedgers[m].getCaccountyear());
          condV.setCaccountmonth(voMonthLedgers[m].getCaccountmonth());
          String strWhere = " where a.pk_corp='" + condV.getPk_corp() + "' and a.cbilltypecode not in ('" + "I0" + "', '" + "I1" + "', '" + "IF" + "', '" + "IG" + "', '" + "IH" + "')" + " and a.cinventoryid = '" + condV.getCinventoryid() + "' and a.crdcenterid ='" + condV.getCrdcenterid() + "'and a.iauditsequence > 0" + " and (a.badjusteditemflag = 'N' or a.cbilltypecode <> '" + "I9" + "')" + (voCondition.getvbatch() == null ? "" : new StringBuilder().append(" and a.vbatch='").append(voCondition.getvbatch()).append("'").toString()) + " and a.caccountmonth = '" + condV.getCaccountmonth() + "'and a.caccountyear = '" + condV.getCaccountyear() + "' order by a.iauditsequence";

          voResult = getIaInoutledgerBO().queryByConditionNoInv(strWhere);
          if ((voMonthLedgers[m].getFpricemodeflag().intValue() == 5) && (bJHJ))
          {
            querydetail.insertLine2_JHJ(voResult);
          }
          else {
            querydetail.insertLine2(voResult);
          }
        }

        if ((voMonthLedgers[m].getFpricemodeflag().intValue() == 5) && (bJHJ))
        {
          if (voMonthLedgers[m].getNinnum() == null)
            voMonthLedgers[m].setNinnum(new UFDouble(0));
          if (voMonthLedgers[m].getNoutnum() == null)
            voMonthLedgers[m].setNoutnum(new UFDouble(0));
          if (voMonthLedgers[m].getNinmny() == null)
            voMonthLedgers[m].setNinmny(new UFDouble(0));
          if (voMonthLedgers[m].getNinvarymny() == null)
            voMonthLedgers[m].setNinvarymny(new UFDouble(0));
          if (voMonthLedgers[m].getNoutmny() == null)
            voMonthLedgers[m].setNoutmny(new UFDouble(0));
          if (voMonthLedgers[m].getNoutvarymny() == null)
            voMonthLedgers[m].setNoutvarymny(new UFDouble(0));
          if (voMonthLedgers[m].getNabmny() == null)
            voMonthLedgers[m].setNabmny(new UFDouble(0));
          if (voMonthLedgers[m].getNabvarymny() == null)
            voMonthLedgers[m].setNabvarymny(new UFDouble(0));
          voMonthLedgers[m].setNinmny(voMonthLedgers[m].getNinmny().sub(voMonthLedgers[m].getNinvarymny()));

          voMonthLedgers[m].setNoutmny(voMonthLedgers[m].getNoutmny().sub(voMonthLedgers[m].getNoutvarymny()));

          voMonthLedgers[m].setNabmny(voMonthLedgers[m].getNabmny().sub(voMonthLedgers[m].getNabvarymny()));

          voMonthLedgers[m].setNabprice(voMonthLedgers[m].getNplanedprice());
        }
        else
        {
          voMonthLedgers[m].setNabvarymny(new UFDouble(0));
        }
        if ((voResult != null) && (voResult.length > 0)) {
          querydetail.insertLine1(voMonthLedgers[m], false);
        }
      }

      if (sAccperiod_stop.compareTo(sUnclosedperiod) >= 0) {
        querydetail.saveYearValue();
        querydetail.resetInOutValue();

        IaInoutledgerVO condV = new IaInoutledgerVO();
        String whereStr = "";
        condV.setPk_corp(sPk_corp);
        condV.setCrdcenterid(sCrdcenterid);
        condV.setCinventoryid(sCinventoryid);
        condV.setCaccountyear(sUnclosedperiod.substring(0, 4));
        condV.setCaccountmonth(sUnclosedperiod.substring(5, 7));
        whereStr = " where a.pk_corp='" + condV.getPk_corp() + "' and a.cbilltypecode not in ('" + "I0" + "', '" + "I1" + "', '" + "IF" + "', '" + "IG" + "', '" + "IH" + "')" + " and a.crdcenterid ='" + condV.getCrdcenterid() + "' and a.cinventoryid = '" + condV.getCinventoryid() + "' and a.iauditsequence > 0" + " and (a.badjusteditemflag = 'N' or a.cbilltypecode <> '" + "I9" + "')" + (voCondition.getvbatch() == null ? "" : new StringBuilder().append(" and a.vbatch='").append(voCondition.getvbatch()).append("'").toString());

        whereStr = whereStr + " and a.caccountmonth = '" + condV.getCaccountmonth() + "'and a.caccountyear = '" + condV.getCaccountyear() + "'";

        voResult = getIaInoutledgerBO().queryByConditionNoInv(whereStr + " order by a.iauditsequence");

        if ((voResult != null) && (voResult.length > 0)) {
          if ((voResult[0].getFpricemodeflag().intValue() == 5) && (bJHJ)) {
            querydetail.insertLine2_JHJ(voResult);
          }
          else {
            querydetail.insertLine2(voResult);
          }
          bHaveRecord = true;
        }

        if (voCondition.getincludenotaudit()) {
          IaInoutledgerVO voCond1 = new IaInoutledgerVO();
          voCond1.setPk_corp(sPk_corp);
          voCond1.setCrdcenterid(sCrdcenterid);
          voCond1.setCinventoryid(sCinventoryid);
          whereStr = " where a.pk_corp='" + condV.getPk_corp() + "' and a.cbilltypecode not in ('" + "I0" + "', '" + "I1" + "','" + "IF" + "', '" + "IG" + "', '" + "IH" + "')" + " and (a.badjusteditemflag = 'N' or a.cbilltypecode <> '" + "I9" + "')" + " and a.crdcenterid ='" + condV.getCrdcenterid() + "' and a.cinventoryid = '" + condV.getCinventoryid() + "'" + " and a.dbilldate >='" + cbo.getMonthBeginDate(sPk_corp, sAccperiod_start) + "'" + " and a.dbilldate <='" + cbo.getMonthEndDate(sPk_corp, sAccperiod_stop) + "'" + " and a.iauditsequence = -1" + (voCondition.getvbatch() == null ? "" : new StringBuilder().append(" and a.vbatch='").append(voCondition.getvbatch()).append("'").toString());

          whereStr = whereStr + " order by a.cbill_bid";
          voResult = getIaInoutledgerBO().queryByConditionNoInv(whereStr);
          if ((voResult != null) && (voResult.length > 0)) {
            if ((voResult[0].getFpricemodeflag().intValue() == 5) && (bJHJ))
            {
              querydetail.insertLine2_JHJ(voResult);
            }
            else {
              querydetail.insertLine2(voResult);
            }
            bHaveRecord = true;
          }
        }

        if (bHaveRecord)
        {
          querydetail.insertMonthTotal();

          querydetail.insertYearTotal();
        }
      }
      voTableData = new QueryDetailPrintVO[querydetail.getVtabledata().size()];
      querydetail.getVtabledata().copyInto(voTableData);

      CommonDataDMO.objectReference(voTableData);
    }
    catch (BusinessException be) {
      throw new BusinessException(be);
    }
    catch (Exception e) {
      throw new BusinessException("IAQueryBO::queryDetail(IaQueryConditionVO voCondition, Integer[] nDataPrecision) Exception!", e);
    }

    return voTableData;
  }

  public IaInoutledgerPrintVO[] queryinoutledger(QueryVO query)
    throws BusinessException
  {
    IaInoutledgerPrintVO[] iainoutledgerprints = null;
    try {
      IaInoutledgerDMO dmo = new IaInoutledgerDMO();
      iainoutledgerprints = dmo.queryinoutledger(query);

      CommonDataDMO.objectReference(iainoutledgerprints);
    }
    catch (BusinessException e) {
      throw e;
    }
    catch (Exception e)
    {
      throw new BusinessException("IaqueryinoutBean::queryinoutledger(String sWhereStr, String sOrderStr) Exception!", e);
    }

    return iainoutledgerprints;
  }

  private IIaInoutledger getIaInoutledgerBO()
    throws Exception
  {
    IIaInoutledger iIaInoutledger = new IaInoutledgerImpl();
    return iIaInoutledger;
  }

  public String[] getVbatch(String sPk_corp, String sCrdcenterid, String sInventoryid)
    throws BusinessException
  {
    CommonDataImpl cbo = new CommonDataImpl();
    String[] saReturn = null;
    try
    {
      String strSql = "select b.vbatch from ia_bill_b b inner join ia_bill h on b.cbillid = h.cbillid  where b.pk_corp = '" + sPk_corp + "'" + " and h.crdcenterid = '" + sCrdcenterid + "'" + " and b.cinventoryid = '" + sInventoryid + "'" + " and b.dr = 0" + " group by b.vbatch";

      String[][] sa2Result = cbo.queryData(strSql);
      if ((sa2Result != null) && (sa2Result.length > 0)) {
        int iLength = sa2Result.length;
        saReturn = new String[iLength];
        for (int i = 0; i < iLength; i++)
          saReturn[i] = sa2Result[i][0];
      }
    }
    catch (BusinessException e)
    {
      throw new BusinessException("IAQueryBO::getVbatch(String sPk_corp, String sCrdcenterid, String sInventoryid)", e);
    }
    catch (Exception e)
    {
      throw new BusinessException("IAQueryBO::getVbatch(String sPk_corp, String sCrdcenterid, String sInventoryid)", e);
    }

    return saReturn;
  }

  public DiffDetailPrintVO[] queryDiffDetail(IaQueryConditionVO voCondition, Integer nMoneyLength)
    throws BusinessException
  {
    DiffDetailPrintVO[] voTableData = null;
    IaQueryDiffDetail queryDiff = new IaQueryDiffDetail();
    CommonDataImpl cbo = new CommonDataImpl();
    GeneralledgerImpl gbo = new GeneralledgerImpl();
    MonthledgerImpl mbo = new MonthledgerImpl();
    String sPk_corp = null;
    String sCrdcenterid = null;

    String sCinventoryid = null;
    String sAccperiod_start = null;
    String sAccperiod_stop = null;
    String sUnclosedperiod = null;
    String sStartaccperiod = null;
    String[][] sResult = (String[][])null;
    sPk_corp = voCondition.getpk_corp() == null ? "" : voCondition.getpk_corp();

    sCrdcenterid = voCondition.getcrdcenterid() == null ? "" : voCondition.getcrdcenterid();
    try
    {
      sResult = cbo.queryData("select pk_invmandoc from bd_invmandoc m, bd_invbasdoc b where m.pk_invbasdoc=b.pk_invbasdoc and m.pk_corp='" + sPk_corp + "' and b.invcode='" + voCondition.getcinvcode() + "'");

      if ((sResult == null) || (sResult.length == 0))
      {
        BusinessException be = new BusinessException(NCLangResOnserver.getInstance().getStrByID("201480", "UPP201480-000000"));

        throw be;
      }
      sCinventoryid = sResult[0][0].trim();
      sStartaccperiod = cbo.getStartPeriod(sPk_corp);
      sUnclosedperiod = cbo.getUnClosePeriod(sPk_corp);
      voCondition.setbeginuseperiod(sStartaccperiod);
      voCondition.setunclosedperiod(sUnclosedperiod);
      queryDiff.initValue(voCondition, nMoneyLength);
      sAccperiod_start = voCondition.getaccperiod_start();
      sAccperiod_stop = voCondition.getaccperiod_stop();
      if (sUnclosedperiod.equals("00")) {
        BusinessException be = new BusinessException(NCLangResOnserver.getInstance().getStrByID("201480", "UPP201480-000001"));

        throw be;
      }
      if ((sAccperiod_stop.compareTo(sStartaccperiod) < 0) || (sAccperiod_start.compareTo(sUnclosedperiod) > 0))
      {
        BusinessException be = new BusinessException(NCLangResOnserver.getInstance().getStrByID("201480", "UPP201480-000002"));

        throw be;
      }
      if (sAccperiod_start.compareTo(sStartaccperiod) < 0)
        sAccperiod_start = sStartaccperiod;
      if ((sCrdcenterid.length() == 0) || (sCinventoryid.length() == 0)) {
        BusinessException be = new BusinessException(NCLangResOnserver.getInstance().getStrByID("201480", "UPP201480-000003"));

        throw be;
      }

      String sWTDX = cbo.getBizTypeID(sPk_corp, "WTDX");
      String sFQSK = cbo.getBizTypeID(sPk_corp, "FQSK");
      queryDiff.setWTDX(sWTDX);
      queryDiff.setFQSK(sFQSK);

      GeneralledgerVO[] gv = null;
      MonthledgerVO[] mv = null;
      if (sAccperiod_start.equals(sStartaccperiod))
      {
        gv = gbo.queryByCondition("caccountyear='" + sStartaccperiod.substring(0, 4) + "' and frecordtypeflag=1 and ia_generalledger.pk_corp='" + sPk_corp + "' and crdcenterid='" + sCrdcenterid + "' and cinventoryid ='" + sCinventoryid + "'");
      }
      else
      {
        String sQueryyear = null; String sQuerymonth = null;
        if (sAccperiod_start.substring(5, 7).equals("01"))
        {
          int accountyear = Integer.parseInt(sAccperiod_start.substring(0, 4));
          sQueryyear = Integer.toString(accountyear - 1);
          sQuerymonth = cbo.getLastMonthInYear(sPk_corp, sQueryyear);
        }
        else {
          sQueryyear = sAccperiod_start.substring(0, 4);
          int accountmonth = Integer.parseInt(sAccperiod_start.substring(5)) - 1;
          sQuerymonth = (accountmonth < 10 ? "0" : "") + String.valueOf(accountmonth).trim();
        }

        mv = mbo.queryByCondition_month("caccountyear='" + sQueryyear + "' and caccountmonth= '" + sQuerymonth + "' and frecordtypeflag=3 and pk_corp='" + sPk_corp + "' and crdcenterid='" + sCrdcenterid + "' and cinventoryid ='" + sCinventoryid + "'");
      }

      if (((gv != null) && (gv.length == 0)) || ((mv != null) && (mv.length == 0)))
      {
        GeneralledgerVO gv1 = new GeneralledgerVO();
        gv1.setFrecordtypeflag(Integer.valueOf("1"));
        gv1.setNabnum(new UFDouble(0));
        gv1.setNabmny(new UFDouble(0));
        gv1.setNabvarymny(new UFDouble(0));
        queryDiff.insertLine1(gv1, new Boolean(true));
      }
      else if ((gv != null) && (gv.length > 0)) {
        if (gv[0].getNabmny() == null)
          gv[0].setNabmny(new UFDouble(0));
        if (gv[0].getNabnum() == null)
          gv[0].setNabnum(new UFDouble(0));
        if (gv[0].getNabvarymny() == null)
          gv[0].setNabvarymny(new UFDouble(0));
        if (voCondition.getjhj())
        {
          gv[0].setNabmny(gv[0].getNabmny().sub(gv[0].getNabvarymny()));
          gv[0].setNabprice(gv[0].getNabnum().doubleValue() == 0.0D ? null : gv[0].getNabmny().div(gv[0].getNabnum()));
        }

        queryDiff.insertLine1(gv[0], new Boolean(true));
      }
      else if ((mv != null) && (mv.length > 0)) {
        if (mv[0].getNabmny() == null)
          mv[0].setNabmny(new UFDouble(0));
        if (mv[0].getNabvarymny() == null)
          mv[0].setNabvarymny(new UFDouble(0));
        if (mv[0].getNabnum() == null)
          mv[0].setNabnum(new UFDouble(0));
        if (voCondition.getjhj())
        {
          mv[0].setNabmny(mv[0].getNabmny().sub(mv[0].getNabvarymny()));
          mv[0].setNabprice(mv[0].getNabnum().doubleValue() == 0.0D ? null : mv[0].getNabmny().div(mv[0].getNabnum()));
        }

        queryDiff.insertLine1(mv[0], new Boolean(true));
      }
      MonthledgerVO[] voMonthLedgers = null;
      voMonthLedgers = mbo.queryByCondition_month("caccountyear || '-' || caccountmonth between '" + sAccperiod_start + "' and '" + sAccperiod_stop + "' and frecordtypeflag in (2, 3) and pk_corp='" + sPk_corp + "' and crdcenterid='" + sCrdcenterid + "' and cinventoryid ='" + sCinventoryid + "' order by caccountyear, caccountmonth, frecordtypeflag");

      IaInoutledgerVO[] voResult = null;

      for (int m = 0; m < voMonthLedgers.length; m++) {
        if (voMonthLedgers[m].getFrecordtypeflag().intValue() == 2)
        {
          if (voMonthLedgers[m].getCaccountmonth().equals("01")) {
            queryDiff.resetInOutValue();
          }

          IaInoutledgerVO condv = new IaInoutledgerVO();
          condv.setPk_corp(sPk_corp);
          condv.setCrdcenterid(sCrdcenterid);
          condv.setCinventoryid(sCinventoryid);
          condv.setCaccountyear(voMonthLedgers[m].getCaccountyear());
          condv.setCaccountmonth(voMonthLedgers[m].getCaccountmonth());
          String strWhere = " left outer join bd_busitype e on a.cbiztypeid=e.pk_busitype ";
          strWhere = strWhere + " where a.pk_corp='" + condv.getPk_corp() + "'and a.iauditsequence > 0 and a.cbilltypecode not in ('" + "I0" + "', '" + "I1" + "', '" + "IF" + "', '" + "IG" + "', '" + "IH" + "') and (a.csourcebilltypecode is null or e.verifyrule is null or (e.verifyrule <> 'F'  or e.verifyrule <> 'W') or a.csourcebilltypecode <> '" + "32" + "') and (a.badjusteditemflag = 'N' or a.cbilltypecode <> '" + "I9" + "')" + " and a.crdcenterid ='" + condv.getCrdcenterid() + "' and a.cinventoryid = '" + condv.getCinventoryid() + "' and a.caccountmonth = '" + condv.getCaccountmonth() + "'and a.caccountyear = '" + condv.getCaccountyear() + "'and a.fpricemodeflag = " + 5 + " order by a.iauditsequence";

          voResult = getIaInoutledgerBO().queryByConditionNoInv(strWhere);
          queryDiff.insertLine2(voResult);
        }
        if ((voResult != null) && (voResult.length > 0)) {
          queryDiff.insertLine1(voMonthLedgers[m], new Boolean(false));
        }
      }
      if (sAccperiod_stop.compareTo(sUnclosedperiod) >= 0) {
        queryDiff.saveYearValue();
        queryDiff.resetInOutValue();
        boolean bHaveRecord = false;

        IaInoutledgerVO condv = new IaInoutledgerVO();
        condv.setPk_corp(sPk_corp);
        condv.setCrdcenterid(sCrdcenterid);
        condv.setCinventoryid(sCinventoryid);
        condv.setCaccountyear(sUnclosedperiod.substring(0, 4));
        condv.setCaccountmonth(sUnclosedperiod.substring(5, 7));

        String strWhere = " left outer join bd_busitype e on a.cbiztypeid=e.pk_busitype ";
        strWhere = strWhere + " where a.pk_corp='" + condv.getPk_corp() + "'and a.iauditsequence > 0 and a.cbilltypecode not in ('" + "I0" + "', '" + "I1" + "', '" + "IF" + "', '" + "IG" + "', '" + "IH" + "') and (a.csourcebilltypecode is null or e.verifyrule is null or (e.verifyrule <> 'F'  or e.verifyrule <> 'W') or a.csourcebilltypecode <> '" + "32" + "') and (a.badjusteditemflag = 'N' or a.cbilltypecode <> '" + "I9" + "')";

        strWhere = strWhere + " and a.crdcenterid ='" + condv.getCrdcenterid() + "' and a.cinventoryid = '" + condv.getCinventoryid() + "' and a.caccountmonth = '" + condv.getCaccountmonth() + "'and a.caccountyear = '" + condv.getCaccountyear() + "'and a.fpricemodeflag = " + 5 + " order by a.iauditsequence";

        voResult = getIaInoutledgerBO().queryByConditionNoInv(strWhere);

        queryDiff.insertLine2(voResult);
        if (voResult.length > 0) {
          bHaveRecord = true;
        }
        if (voCondition.getincludenotaudit()) {
          IaInoutledgerVO condV1 = new IaInoutledgerVO();
          condV1.setPk_corp(sPk_corp);
          condV1.setCrdcenterid(sCrdcenterid);
          condV1.setCinventoryid(sCinventoryid);
          String whereStr = " left outer join bd_busitype e on a.cbiztypeid=e.pk_busitype ";
          whereStr = whereStr + " where a.pk_corp='" + condV1.getPk_corp() + "'and a.dbilldate <='" + cbo.getMonthEndDate(sPk_corp, sAccperiod_stop) + "'" + " and a.dauditdate is null and a.cbilltypecode not in ('" + "I0" + "', '" + "I1" + "','" + "IF" + "', '" + "IG" + "', '" + "IH" + "') and (a.csourcebilltypecode is null or e.verifyrule is null or (e.verifyrule <> 'F'  or e.verifyrule <> 'W') or a.csourcebilltypecode <> '" + "32" + "') and (a.badjusteditemflag = 'N' or a.cbilltypecode <> '" + "I9" + "')";

          whereStr = whereStr + " and a.crdcenterid ='" + condV1.getCrdcenterid() + "' and a.cinventoryid = '" + condV1.getCinventoryid() + "' order by a.cbill_bid";

          voResult = getIaInoutledgerBO().queryByConditionNoInv(whereStr);

          queryDiff.insertLine2(voResult);
          if (voResult.length > 0) {
            bHaveRecord = true;
          }
        }
        if (bHaveRecord)
        {
          queryDiff.insertMonthTotal();

          queryDiff.insertYearTotal();
        }
      }
      voTableData = new DiffDetailPrintVO[queryDiff.getVtabledata().size()];
      queryDiff.getVtabledata().copyInto(voTableData);

      CommonDataDMO.objectReference(voTableData);
    }
    catch (BusinessException be)
    {
      throw new BusinessException(be);
    }
    catch (Exception e) {
      throw new BusinessException("IAQueryBO::queryDiffDetail(IaQueryConditionVO voCondition, Integer nMoneyLength) Exception!", e);
    }

    return voTableData;
  }

  public GeneralLedgerPrintVO[] queryGeneralledger(IaQueryConditionVO voCondition, Integer[] nDataPrecision)
    throws BusinessException
  {
    GeneralLedgerPrintVO[] voTableData = null;
    IaQueryGeneralledger querygeneral = new IaQueryGeneralledger();
    GeneralledgerImpl gbo = new GeneralledgerImpl();
    MonthledgerImpl mbo = new MonthledgerImpl();
    CommonDataImpl cbo = new CommonDataImpl();
    String sPk_corp = null;
    String sCrdcenterid = null;
    String sCinventoryid = null;
    String sAccperiod_start = null;
    String sAccperiod_stop = null;
    String sUnclosedperiod = null;
    String sStartaccperiod = null;
    String[][] sa2Result = (String[][])null;
    String sVbatch = " and vbatch='" + voCondition.getvbatch() + "'";

    sPk_corp = voCondition.getpk_corp() == null ? "" : voCondition.getpk_corp();

    sCrdcenterid = voCondition.getcrdcenterid() == null ? "" : voCondition.getcrdcenterid();

    sAccperiod_start = voCondition.getaccperiod_start();
    sAccperiod_stop = voCondition.getaccperiod_stop();
    try {
      sStartaccperiod = cbo.getStartPeriod(sPk_corp);
      sUnclosedperiod = cbo.getUnClosePeriod(sPk_corp);
      voCondition.setbeginuseperiod(sStartaccperiod);
      voCondition.setunclosedperiod(sUnclosedperiod);
      querygeneral.initValue(voCondition, nDataPrecision);
      sa2Result = cbo.queryData("select pk_invmandoc from bd_invmandoc m, bd_invbasdoc b where m.pk_invbasdoc=b.pk_invbasdoc and m.pk_corp='" + sPk_corp + "' and b.invcode='" + voCondition.getcinvcode() + "'");

      if ((sa2Result == null) || (sa2Result.length == 0)) {
        BusinessException be = new BusinessException(NCLangResOnserver.getInstance().getStrByID("201480", "UPP201480-000000"));

        throw be;
      }
      sCinventoryid = sa2Result[0][0].trim();
      if (sUnclosedperiod.equalsIgnoreCase("00")) {
        BusinessException be = new BusinessException(NCLangResOnserver.getInstance().getStrByID("201480", "UPP201480-000001"));

        throw be;
      }
      if ((sAccperiod_stop.compareTo(sStartaccperiod) < 0) || (sAccperiod_start.compareTo(sUnclosedperiod) > 0))
      {
        BusinessException be = new BusinessException(NCLangResOnserver.getInstance().getStrByID("201480", "UPP201480-000002"));

        throw be;
      }
      if (sAccperiod_start.compareTo(sStartaccperiod) < 0)
        sAccperiod_start = sStartaccperiod;
      if ((sCrdcenterid.length() == 0) || (sCinventoryid.length() == 0)) {
        BusinessException be = new BusinessException(NCLangResOnserver.getInstance().getStrByID("201480", "UPP201480-000003"));

        throw be;
      }
      String sWTDX = cbo.getBizTypeID(sPk_corp, "WTDX");

      String sFQSK = cbo.getBizTypeID(sPk_corp, "FQSK");

      querygeneral.setWTDX(sWTDX);
      querygeneral.setFQSK(sFQSK);

      GeneralledgerVO[] gv = null;
      MonthledgerVO[] mv = null;
      if (sAccperiod_start.equals(sStartaccperiod))
      {
        gv = gbo.queryByCondition("caccountyear='" + sStartaccperiod.substring(0, 4) + "'" + " and frecordtypeflag=1" + " and ia_generalledger.pk_corp='" + sPk_corp + "'" + " and crdcenterid='" + sCrdcenterid + "'" + " and cinventoryid ='" + sCinventoryid + "'" + sVbatch);
      }
      else
      {
        String sQueryyear = null; String sQuerymonth = null;
        if (sAccperiod_start.substring(5, 7).equals("01"))
        {
          int accountyear = Integer.parseInt(sAccperiod_start.substring(0, 4));
          sQueryyear = Integer.toString(accountyear - 1);
          sQuerymonth = cbo.getLastMonthInYear(sPk_corp, sQueryyear);
        }
        else {
          sQueryyear = sAccperiod_start.substring(0, 4);
          int accountmonth = Integer.parseInt(sAccperiod_start.substring(5)) - 1;
          sQuerymonth = (accountmonth < 10 ? "0" : "") + Integer.toString(accountmonth).trim();
        }

        mv = mbo.queryByCondition_month("caccountyear='" + sQueryyear + "'" + " and caccountmonth= '" + sQuerymonth + "'" + " and frecordtypeflag=3" + " and pk_corp='" + sPk_corp + "'" + " and crdcenterid='" + sCrdcenterid + "'" + " and cinventoryid ='" + sCinventoryid + "'" + sVbatch);
      }

      if (((gv != null) && (gv.length == 0)) || ((mv != null) && (mv.length == 0)))
      {
        GeneralledgerVO gv1 = new GeneralledgerVO();
        gv1.setFrecordtypeflag(Integer.valueOf("1"));
        gv1.setNabnum(new UFDouble(0));
        gv1.setNabmny(new UFDouble(0));
        querygeneral.insertLine1(gv1, true);
        querygeneral.resetInOutValue();
      }
      else if ((gv != null) && (gv.length > 0)) {
        if (gv[0].getNabmny() == null)
          gv[0].setNabmny(new UFDouble(0));
        if (gv[0].getNabvarymny() == null)
          gv[0].setNabvarymny(new UFDouble(0));
        if (gv[0].getNabnum() == null)
          gv[0].setNabnum(new UFDouble(0));
        if (voCondition.getjhj())
        {
          gv[0].setNabmny(gv[0].getNabmny().sub(gv[0].getNabvarymny()));
          gv[0].setNabprice(gv[0].getNabnum().doubleValue() == 0.0D ? null : gv[0].getNabmny().div(gv[0].getNabnum()));
        }

        querygeneral.insertLine1(gv[0], true);

        if (sAccperiod_start.substring(5, 7).equals("01"))
        {
          querygeneral.resetInOutValue();
        }

      }
      else if ((mv != null) && (mv.length > 0)) {
        if (mv[0].getNabmny() == null)
          mv[0].setNabmny(new UFDouble(0));
        if (mv[0].getNabvarymny() == null)
          mv[0].setNabvarymny(new UFDouble(0));
        if (mv[0].getNabnum() == null)
          mv[0].setNabnum(new UFDouble(0));
        if (voCondition.getjhj())
        {
          mv[0].setNabmny(mv[0].getNabmny().sub(mv[0].getNabvarymny()));
          mv[0].setNabprice(mv[0].getNabnum().doubleValue() == 0.0D ? null : mv[0].getNabmny().div(mv[0].getNabnum()));
        }

        querygeneral.insertLine1(mv[0], true);

        if (sAccperiod_start.substring(5, 7).equals("01"))
        {
          querygeneral.resetInOutValue();
        }
      }

      MonthledgerVO[] v2 = null;
      v2 = mbo.queryByCondition_month("caccountyear || '-' || caccountmonth between '" + sAccperiod_start + "' and '" + sAccperiod_stop + "'" + " and frecordtypeflag in (2, 3)" + " and pk_corp='" + sPk_corp + "'" + " and crdcenterid='" + sCrdcenterid + "'" + " and cinventoryid ='" + sCinventoryid + "'" + sVbatch + " order by caccountyear, caccountmonth, frecordtypeflag");

      for (int m = 0; m < v2.length; m++) {
        if ((m != 25) || 
          (v2[m].getNinnum() == null))
          v2[m].setNinnum(new UFDouble(0));
        if (v2[m].getNoutnum() == null)
          v2[m].setNoutnum(new UFDouble(0));
        if (v2[m].getNinmny() == null)
          v2[m].setNinmny(new UFDouble(0));
        if (v2[m].getNinvarymny() == null)
          v2[m].setNinvarymny(new UFDouble(0));
        if (v2[m].getNoutmny() == null)
          v2[m].setNoutmny(new UFDouble(0));
        if (v2[m].getNoutvarymny() == null)
          v2[m].setNoutvarymny(new UFDouble(0));
        if (v2[m].getNabmny() == null)
          v2[m].setNabmny(new UFDouble(0));
        if (v2[m].getNabvarymny() == null)
          v2[m].setNabvarymny(new UFDouble(0));
        if (v2[m].getNabnum() == null)
          v2[m].setNabnum(new UFDouble(0));
        if (voCondition.getjhj())
        {
          v2[m].setNinmny(v2[m].getNinmny().sub(v2[m].getNinvarymny()));
          v2[m].setNoutmny(v2[m].getNoutmny().sub(v2[m].getNoutvarymny()));
          v2[m].setNabmny(v2[m].getNabmny().sub(v2[m].getNabvarymny()));
          v2[m].setNabprice(v2[m].getNplanedprice());
        }
        if ((v2[m].getFrecordtypeflag().intValue() == 2) && (v2[m].getNinnum().doubleValue() == 0.0D) && (v2[m].getNinmny().doubleValue() == 0.0D) && (v2[m].getNinvarymny().doubleValue() == 0.0D) && (v2[m].getNoutnum().doubleValue() == 0.0D) && (v2[m].getNoutmny().doubleValue() == 0.0D) && (v2[m].getNoutvarymny().doubleValue() == 0.0D))
        {
          m++;
          if (!v2[m].getCaccountmonth().equals("01"))
            continue;
          querygeneral.resetInOutValue();
        }
        else
        {
          querygeneral.insertLine1(v2[m], false);

          if (!v2[m].getCaccountmonth().equals("01"))
          {
            continue;
          }

        }

      }

      IaInoutledgerVO[] resultV = null;
      IaInoutledgerVO[] resultV2 = null;
      if (sAccperiod_stop.compareTo(sUnclosedperiod) >= 0) {
        querygeneral.saveYearValue();
        querygeneral.resetInOutValue();

        IaInoutledgerVO condV = new IaInoutledgerVO();
        String whereStr = "";
        condV.setPk_corp(sPk_corp);
        condV.setCrdcenterid(sCrdcenterid);
        condV.setCinventoryid(sCinventoryid);
        condV.setCaccountyear(sUnclosedperiod.substring(0, 4));
        condV.setCaccountmonth(sUnclosedperiod.substring(5, 7));
        whereStr = " where a.pk_corp='" + condV.getPk_corp() + "'and a.iauditsequence > 0" + " and a.cbilltypecode not in ('" + "I0" + "', '" + "I1" + "', '" + "IF" + "', '" + "IG" + "','" + "IH" + "')" + " and (a.badjusteditemflag = 'N' or a.cbilltypecode <> '" + "I9" + "')" + (voCondition.getvbatch() == null ? "" : new StringBuilder().append(" and a.vbatch='").append(voCondition.getvbatch()).append("'").toString());

        whereStr = whereStr + " and a.crdcenterid ='" + condV.getCrdcenterid() + "' and a.cinventoryid = '" + condV.getCinventoryid() + "' and a.caccountmonth = '" + condV.getCaccountmonth() + "'and a.caccountyear = '" + condV.getCaccountyear() + "' order by a.iauditsequence";

        resultV = getIaInoutledgerBO().queryByConditionNoInv(whereStr);

        if ((resultV != null) && (resultV.length > 0)) {
          if (voCondition.getjhj()) {
            querygeneral.insertLine2_JHJ(resultV);
          }
          else {
            querygeneral.insertLine2(resultV);
          }
        }

        if (voCondition.getincludenotaudit()) {
          IaInoutledgerVO condV1 = new IaInoutledgerVO();
          condV1.setPk_corp(sPk_corp);
          condV1.setCrdcenterid(sCrdcenterid);
          condV1.setCinventoryid(sCinventoryid);
          whereStr = " where a.dbilldate <='" + cbo.getMonthEndDate(sPk_corp, sUnclosedperiod) + "'" + " and a.dauditdate is null" + " and a.pk_corp='" + sPk_corp + "'" + " and a.crdcenterid='" + sCrdcenterid + "'" + " and a.cinventoryid='" + sCinventoryid + "'" + " and a.cbilltypecode not in ('" + "I0" + "', '" + "I1" + "','" + "IE" + "', '" + "IF" + "', '" + "IG" + "','" + "IH" + "')" + " and (a.badjusteditemflag = 'N' or a.cbilltypecode <> '" + "I9" + "')" + (voCondition.getvbatch() == null ? "" : new StringBuilder().append(" and a.vbatch='").append(voCondition.getvbatch()).append("'").toString());

          whereStr = whereStr + " order by a.cbill_bid";
          resultV2 = getIaInoutledgerBO().queryByConditionNoInv(whereStr);

          if ((resultV2 != null) && (resultV2.length > 0)) {
            if (voCondition.getjhj()) {
              querygeneral.insertLine2_JHJ(resultV2);
            }
            else {
              querygeneral.insertLine2(resultV2);
            }
          }
        }
        if (((resultV != null) && (resultV.length > 0)) || ((resultV2 != null) && (resultV2.length > 0)))
        {
          querygeneral.insertMonthTotal();

          querygeneral.insertYearTotal();
        }
      }
      voTableData = new GeneralLedgerPrintVO[querygeneral.getVtabledata().size()];

      querygeneral.getVtabledata().copyInto(voTableData);

      CommonDataDMO.objectReference(voTableData);
    }
    catch (BusinessException be)
    {
      throw be;
    }
    catch (Exception e) {
      throw new BusinessException("IAQueryBO::queryGeneralledger(IaQueryConditionVO voCondition, Integer[] nDataPrecision) Exception!", e);
    }

    return voTableData;
  }

  public GoodsLedgerPrintVO[] querySendDetail(IaQueryConditionVO voCondition, Integer[] nDataPrecision, Boolean bEntrust)
    throws BusinessException
  {
    GoodsLedgerPrintVO[] voTableData = null;
    IaQuerySendDetail querysenddetail = new IaQuerySendDetail();
    GoodsledgertotalImpl goodsBO = new GoodsledgertotalImpl();
    CommonDataImpl cbo = new CommonDataImpl();
    String sPk_corp = null;
    String sCrdcenterid = null;
    String sCinventoryid = null;
    String sAccperiod_start = null;
    String sAccperiod_stop = null;
    String sUnclosedperiod = null;
    String sStartaccperiod = null;
    String[][] sResult = (String[][])null;

    String sVbatch = " and vbatch='" + voCondition.getvbatch() + "'";

    boolean bJHJ = voCondition.getjhj();
    sPk_corp = voCondition.getpk_corp() == null ? "" : voCondition.getpk_corp();

    sCrdcenterid = voCondition.getcrdcenterid() == null ? "" : voCondition.getcrdcenterid();

    sAccperiod_start = voCondition.getaccperiod_start();
    sAccperiod_stop = voCondition.getaccperiod_stop();
    bJHJ = voCondition.getjhj();
    try {
      sStartaccperiod = cbo.getStartPeriod(sPk_corp);
      sUnclosedperiod = cbo.getUnClosePeriod(sPk_corp);
      voCondition.setbeginuseperiod(sStartaccperiod);
      voCondition.setunclosedperiod(sUnclosedperiod);
      querysenddetail.initValue(voCondition, nDataPrecision, bEntrust);
      sResult = cbo.queryData("select pk_invmandoc from bd_invmandoc m, bd_invbasdoc b where m.pk_invbasdoc=b.pk_invbasdoc and m.pk_corp='" + sPk_corp + "' and b.invcode='" + voCondition.getcinvcode() + "'");

      if ((sResult == null) || (sResult.length == 0)) {
        BusinessException be = new BusinessException(NCLangResOnserver.getInstance().getStrByID("201480", "UPP201480-000000"));

        throw be;
      }
      sCinventoryid = sResult[0][0].trim();
      if (sUnclosedperiod.equalsIgnoreCase("00")) {
        BusinessException be = new BusinessException(NCLangResOnserver.getInstance().getStrByID("201480", "UPP201480-000001"));

        throw be;
      }
      if ((sAccperiod_stop.compareTo(sStartaccperiod) < 0) || (sAccperiod_start.compareTo(sUnclosedperiod) > 0))
      {
        BusinessException be = new BusinessException(NCLangResOnserver.getInstance().getStrByID("201480", "UPP201480-000002"));

        throw be;
      }
      if (sAccperiod_start.compareTo(sStartaccperiod) < 0)
        sAccperiod_start = sStartaccperiod;
      if ((sCrdcenterid.length() == 0) || (sCinventoryid.length() == 0)) {
        BusinessException be = new BusinessException(NCLangResOnserver.getInstance().getStrByID("201480", "UPP201480-000003"));

        throw be;
      }

      String sWTDX = cbo.getBizTypeID(sPk_corp, "WTDX");
      String sFQSK = cbo.getBizTypeID(sPk_corp, "FQSK");

      querysenddetail.setWTDX(sWTDX);
      querysenddetail.setFQSK(sFQSK);

      String[][] sa2Result = (String[][])null;
      if (bJHJ) {
        sa2Result = cbo.queryData("select sum(isnull(ndebitnum, 0) - isnull(ncreditnum, 0)) as nabnum, sum(isnull(ndebitmny, 0) - isnull(ninvarymny, 0) - isnull(ncreditmny, 0) + isnull(noutvarymny, 0)) as nabmny, sum(isnull(ninvarymny, 0) - isnull(noutvarymny, 0)) as nabvarymny  from ia_goodsledger  where pk_corp = '" + sPk_corp + "'" + " and crdcenterid = '" + sCrdcenterid + "'" + " and cinventoryid = '" + sCinventoryid + "'" + " and bissend = '" + (bEntrust.booleanValue() ? 'N' : 'Y') + "'" + " and ccustomvendorid = '" + voCondition.getccustomvendorid() + "'" + " and (caccountyear = '" + sAccperiod_start.substring(0, 4) + "'" + " and caccountmonth < '" + sAccperiod_start.substring(5, 7) + "'" + " or caccountyear < '" + sAccperiod_start.substring(0, 4) + "')" + sVbatch);
      }
      else
      {
        sa2Result = cbo.queryData("select sum(isnull(ndebitnum, 0) - isnull(ncreditnum, 0)) as nabnum, sum(isnull(ndebitmny, 0) - isnull(ncreditmny, 0)) as nabmny, NULL as nabvarymny  from ia_goodsledger  where pk_corp = '" + sPk_corp + "'" + " and crdcenterid = '" + sCrdcenterid + "'" + " and cinventoryid = '" + sCinventoryid + "'" + " and bissend = '" + (bEntrust.booleanValue() ? 'N' : 'Y') + "'" + " and ccustomvendorid = '" + voCondition.getccustomvendorid() + "'" + " and (caccountyear = '" + sAccperiod_start.substring(0, 4) + "'" + " and caccountmonth < '" + sAccperiod_start.substring(5, 7) + "'" + " or caccountyear < '" + sAccperiod_start.substring(0, 4) + "')" + sVbatch);
      }

      GoodsLedgerPrintVO gv = new GoodsLedgerPrintVO();
      gv.setVbillcode(NCLangResOnserver.getInstance().getStrByID("201480", "UPP201480-000004"));

      if (sa2Result.length > 0) {
        gv.setNabnum(new UFDouble(sa2Result[0][0]));
        gv.setNabmny(new UFDouble(sa2Result[0][1]));
        if (bJHJ)
          gv.setNabvarymny(new UFDouble(sa2Result[0][2]));
      }
      else {
        gv.setNabnum(new UFDouble(0));
        gv.setNabmny(new UFDouble(0));
        if (bJHJ)
          gv.setNabvarymny(new UFDouble(0));
      }
      querysenddetail.insertLine1(gv, new Boolean(true));
      GoodsledgerVO condv = new GoodsledgerVO();
      condv.setPk_corp(sPk_corp);
      condv.setCrdcenterid(sCrdcenterid);
      condv.setCinventoryid(sCinventoryid);
      condv.setBissend(new UFBoolean(!bEntrust.booleanValue()));

      condv.setCcustomvendorid(voCondition.getccustomvendorid());
      condv.setVbatch(voCondition.getvbatch());
      GoodsledgerVO[] voResult = null;
      String sWhereStr = "(caccountyear || '-' || caccountmonth between '" + sAccperiod_start + "' and '" + sAccperiod_stop + "')";

      voResult = goodsBO.queryByVO(condv, Boolean.TRUE, sWhereStr, " order by a.caccountyear, a.caccountmonth, a.cgoodsledgerid");

      int nResultLength = voResult.length;

      if (nResultLength > 0) {
        querysenddetail.insertLine2(voResult);
      }

      if (sAccperiod_stop.compareTo(sUnclosedperiod) >= 0) {
        boolean bHaveRecord = false;
        if (voResult.length > 0) {
          if (sUnclosedperiod.compareTo(voResult[(nResultLength - 1)].getCaccountyear() + "-" + voResult[(nResultLength - 1)].getCaccountmonth()) != 0)
          {
            querysenddetail.insertMonthTotal(voResult[(nResultLength - 1)].getCaccountyear(), voResult[(nResultLength - 1)].getCaccountmonth());

            querysenddetail.insertYearTotal(voResult[(nResultLength - 1)].getCaccountyear(), voResult[(nResultLength - 1)].getCaccountmonth());

            if (voResult[(nResultLength - 1)].getCaccountyear().compareTo(sUnclosedperiod.substring(0, 4)) != 0)
            {
              querysenddetail.resetValue();
            }
          }
          else {
            bHaveRecord = true;
          }
        }

        if (voCondition.getincludenotaudit())
        {
          IaInoutledgerVO vConditionInoutLedger = new IaInoutledgerVO();
          vConditionInoutLedger.setPk_corp(sPk_corp);
          vConditionInoutLedger.setCrdcenterid(sCrdcenterid);
          vConditionInoutLedger.setCinventoryid(sCinventoryid);
          vConditionInoutLedger.setCcustomvendorid(voCondition.getccustomvendorid());

          IaInoutledgerVO[] voResult1 = null;
          sVbatch = " and a.vbatch='" + voCondition.getvbatch() + "'";

          if (bEntrust.booleanValue())
          {
            sWhereStr = " a.dbilldate <='" + cbo.getMonthEndDate(sPk_corp, sUnclosedperiod) + "'" + " and a.dauditdate is null" + " and a.cbilltypecode = '" + "I5" + "'" + " and a.cbiztypeid in (" + sWTDX + ")" + sVbatch;

            voResult1 = getIaInoutledgerBO().queryByVO(vConditionInoutLedger, Boolean.TRUE, sWhereStr, " order by a.cbill_bid");
          }
          else
          {
            sWhereStr = " a.dbilldate <='" + cbo.getMonthEndDate(sPk_corp, sUnclosedperiod) + "'" + " and a.dauditdate is null" + " and a.cbilltypecode = '" + "I5" + "'" + " and a.cbiztypeid in (" + sFQSK + ")" + sVbatch;

            voResult1 = getIaInoutledgerBO().queryByVO(vConditionInoutLedger, Boolean.TRUE, sWhereStr, " order by a.cbill_bid");
          }

          if (voResult1.length > 0) {
            bHaveRecord = true;
            querysenddetail.insertLine2_notaudit(voResult1);
          }
        }

        if (bHaveRecord)
        {
          querysenddetail.insertMonthTotal(sUnclosedperiod.substring(0, 4), sUnclosedperiod.substring(5, 7));

          querysenddetail.insertYearTotal(sUnclosedperiod.substring(0, 4), sUnclosedperiod.substring(5, 7));
        }

      }
      else if (nResultLength > 0)
      {
        querysenddetail.insertMonthTotal(voResult[(nResultLength - 1)].getCaccountyear(), voResult[(nResultLength - 1)].getCaccountmonth());

        querysenddetail.insertYearTotal(voResult[(nResultLength - 1)].getCaccountyear(), voResult[(nResultLength - 1)].getCaccountmonth());
      }

      voTableData = new GoodsLedgerPrintVO[querysenddetail.getVtabledata().size()];

      querysenddetail.getVtabledata().copyInto(voTableData);

      CommonDataDMO.objectReference(voTableData);
    }
    catch (Exception ex)
    {
      ExceptionUtils.marsh(ex);
    }
    return voTableData;
  }

  public GoodsLedgerPrintVO[] querySendDetailSingle(IaQueryConditionVO voCondition, Integer[] nDataPrecision, String ssaleadviceitemid)
    throws BusinessException
  {
    Vector vDatas = new Vector();
    GoodsledgertotalImpl goodsBO = new GoodsledgertotalImpl();
    GoodsLedgerPrintVO[] voDatas = null;
    try
    {
      UFDouble nabnumber = new UFDouble(0);
      UFDouble nabmoney = new UFDouble(0);
      UFDouble nabvary = new UFDouble(0);

      boolean bSimulateMny = voCondition.getsimulatemny();
      boolean bIncludeNotAudit = voCondition.getincludenotaudit();
      boolean bJHJ = voCondition.getjhj();
      int nNumberLength = nDataPrecision[0].intValue();
      int nPriceLength = nDataPrecision[1].intValue();
      int nMoneyLength = nDataPrecision[2].intValue();
      GoodsledgerVO condv = new GoodsledgerVO();
      condv.setCcsaleadviceitemid(ssaleadviceitemid);

      condv.setVbatch(voCondition.getvbatch());
      GoodsledgerVO[] voResult = null;
      voResult = goodsBO.queryByVO(condv, Boolean.TRUE, null, " order by a.dauditdate, a.cbill_bid");

      for (int i = 0; i < voResult.length; i++)
      {
        if (voResult[i].getCsourcebilltypecode().equalsIgnoreCase("4C"))
        {
          if (bJHJ) {
            voResult[i].setNdebitmny(voResult[i].getNplanedmny());
            voResult[i].setNdebitprice(voResult[i].getNplanedprice());
          }
          if (voResult[i].getNdebitnum() != null)
          {
            nabnumber = nabnumber.add(voResult[i].getNdebitnum());
            if (voResult[i].getNdebitmny() != null)
            {
              nabmoney = nabmoney.add(voResult[i].getNdebitmny());
            }
            else if ((bSimulateMny) && (voResult[i].getNsimulatemny() != null))
            {
              nabmoney = nabmoney.add(voResult[i].getNsimulatemny());
            }
          }

          if (voResult[i].getNinvarymny() != null)
          {
            nabvary = nabvary.add(voResult[i].getNinvarymny());
          }
        }
        else
        {
          if (bJHJ) {
            voResult[i].setNcreditmny(voResult[i].getNplanedmny());
            voResult[i].setNcreditprice(voResult[i].getNplanedprice());
          }
          if (voResult[i].getNcreditnum() != null)
          {
            nabnumber = nabnumber.sub(voResult[i].getNcreditnum());
            if (voResult[i].getNcreditmny() != null)
            {
              nabmoney = nabmoney.sub(voResult[i].getNcreditmny());
            }
            else if ((bSimulateMny) && (voResult[i].getNsimulatemny() != null))
            {
              nabmoney = nabmoney.sub(voResult[i].getNsimulatemny());
            }
          }

          if (voResult[i].getNoutvarymny() != null)
          {
            nabvary = nabvary.sub(voResult[i].getNoutvarymny());
          }
        }

        GoodsLedgerPrintVO voData = new GoodsLedgerPrintVO();
        voData.setVbillcode(voResult[i].getVbillcode());
        voData.setDbilldate(voResult[i].getDbilldate().toString());
        voData.setBilltypename(voResult[i].getBilltypename());
        voData.setBusiname(voResult[i].getBusiname());
        voData.setCdispatchname(voResult[i].getCdispatchname());
        voData.setStorname(voResult[i].getStorname());
        voData.setDeptname(voResult[i].getDeptname());
        voData.setCustname(voResult[i].getCustname());
        voData.setVsourcebillcode(voResult[i].getVsourcebillcode());

        voData.setNdebitnum(voResult[i].getNdebitnum() == null ? null : voResult[i].getNdebitnum().setScale(-nNumberLength, 4));

        if (voResult[i].getNdebitprice() != null) {
          voData.setNdebitprice(voResult[i].getNdebitprice().setScale(-nPriceLength, 4));
        }
        else if ((voResult[i].getNdebitnum() != null) && (voResult[i].getNdebitnum().doubleValue() != 0.0D) && (voResult[i].getNsimulatemny() != null))
        {
          voData.setNdebitprice(voResult[i].getNsimulatemny().div(voResult[i].getNdebitnum()).setScale(-nPriceLength, 4));
        }

        if (voResult[i].getNdebitmny() != null) {
          voData.setNdebitmny(voResult[i].getNdebitmny().setScale(-nMoneyLength, 4));
        }
        else if ((voResult[i].getNdebitnum() != null) && (voResult[i].getNsimulatemny() != null))
        {
          voData.setNdebitmny(voResult[i].getNsimulatemny().setScale(-nMoneyLength, 4));
        }

        if (voResult[i].getNinvarymny() != null) {
          voData.setNinvarymny(voResult[i].getNinvarymny().setScale(-nMoneyLength, 4));
        }

        if (voResult[i].getNcreditnum() != null) {
          voData.setNcreditnum(voResult[i].getNcreditnum().setScale(-nNumberLength, 4));
        }

        if (voResult[i].getNcreditprice() != null) {
          voData.setNcreditprice(voResult[i].getNcreditprice().setScale(-nPriceLength, 4));
        }
        else if ((voResult[i].getNcreditnum() != null) && (voResult[i].getNcreditnum().doubleValue() != 0.0D) && (voResult[i].getNsimulatemny() != null))
        {
          voData.setNcreditprice(voResult[i].getNsimulatemny().div(voResult[i].getNcreditnum()).setScale(-nPriceLength, 4));
        }

        if (voResult[i].getNcreditmny() != null) {
          voData.setNcreditmny(voResult[i].getNcreditmny().setScale(-nMoneyLength, 4));
        }
        else if ((voResult[i].getNcreditnum() != null) && (voResult[i].getNsimulatemny() != null))
        {
          voData.setNcreditmny(voResult[i].getNsimulatemny().setScale(-nMoneyLength, 4));
        }

        if (voResult[i].getNoutvarymny() != null) {
          voData.setNoutvarymny(voResult[i].getNoutvarymny().setScale(-nMoneyLength, 4));
        }

        voData.setNabnum(nabnumber.setScale(-nNumberLength, 4));

        voData.setNabprice(nabnumber.doubleValue() != 0.0D ? nabmoney.div(nabnumber).setScale(-nPriceLength, 4) : null);

        voData.setNabmny(nabmoney.setScale(-nMoneyLength, 4));

        voData.setNabvarymny(nabvary.setScale(-nMoneyLength, 4));

        voData.setVnote(voResult[i].getVnote());
        vDatas.addElement(voData);
      }
      if (bIncludeNotAudit)
      {
        IaInoutledgerVO vConditionInoutLedger = new IaInoutledgerVO();
        vConditionInoutLedger.setCcsaleadviceitemid(ssaleadviceitemid);

        vConditionInoutLedger.setVbatch(voCondition.getvbatch());
        IaInoutledgerVO[] voResult1 = getIaInoutledgerBO().queryByVO(vConditionInoutLedger, Boolean.TRUE, "a.dauditdate is null", " order by a.cbill_bid");

        for (int i = 0; i < voResult1.length; i++)
        {
          if (voResult1[i].getCsourcebilltypecode().equalsIgnoreCase("4C"))
          {
            if (bJHJ) {
              voResult1[i].setNmoney(voResult1[i].getNplanedmny());
              voResult1[i].setNprice(voResult1[i].getNplanedprice());
            }
            if (voResult1[i].getNnumber() != null)
            {
              nabnumber = nabnumber.add(voResult1[i].getNnumber());
              if (voResult1[i].getNmoney() != null)
              {
                nabmoney = nabmoney.add(voResult1[i].getNmoney());
              }
              else if ((bSimulateMny) && (voResult1[i].getNsimulatemny() != null))
              {
                nabmoney = nabmoney.add(voResult1[i].getNsimulatemny());
              }
            }

            if (voResult1[i].getNinvarymny() != null)
            {
              nabvary = nabvary.add(voResult1[i].getNinvarymny());
            }
          }
          else
          {
            if (bJHJ) {
              voResult1[i].setNmoney(voResult1[i].getNplanedmny());
              voResult1[i].setNprice(voResult1[i].getNplanedprice());
            }
            if (voResult1[i].getNnumber() != null)
            {
              nabnumber = nabnumber.sub(voResult1[i].getNnumber());
              if (voResult1[i].getNmoney() != null)
              {
                nabmoney = nabmoney.sub(voResult1[i].getNmoney());
              }
              else if ((bSimulateMny) && (voResult1[i].getNsimulatemny() != null))
              {
                nabmoney = nabmoney.sub(voResult1[i].getNsimulatemny());
              }
            }

            if (voResult1[i].getNoutvarymny() != null)
            {
              nabvary = nabvary.sub(voResult1[i].getNoutvarymny());
            }
          }

          GoodsLedgerPrintVO voData = new GoodsLedgerPrintVO();
          voData.setVbillcode(voResult1[i].getVbillcode());
          voData.setDbilldate(voResult1[i].getDbilldate().toString());
          voData.setBilltypename(voResult1[i].getBilltypename());
          voData.setBusiname(voResult1[i].getBusiname());
          voData.setCdispatchname(voResult1[i].getVdispatchname());
          voData.setStorname(voResult1[i].getStorname());
          voData.setDeptname(voResult1[i].getDeptname());
          voData.setCustname(voResult1[i].getCustname());
          voData.setVsourcebillcode(voResult1[i].getVsourcebillcode());
          if (voResult1[i].getCsourcebilltypecode().equalsIgnoreCase("4C"))
          {
            voData.setNdebitnum(voResult1[i].getNnumber() == null ? null : voResult1[i].getNnumber().setScale(-nNumberLength, 4));

            if (voResult1[i].getNprice() != null) {
              voData.setNdebitprice(voResult1[i].getNprice().setScale(-nPriceLength, 4));
            }
            else if ((voResult1[i].getNnumber() != null) && (voResult1[i].getNnumber().doubleValue() != 0.0D) && (voResult1[i].getNsimulatemny() != null))
            {
              voData.setNdebitprice(voResult1[i].getNsimulatemny().div(voResult1[i].getNnumber()).setScale(-nPriceLength, 4));
            }

            if (voResult1[i].getNmoney() != null) {
              voData.setNdebitmny(voResult1[i].getNmoney().setScale(-nMoneyLength, 4));
            }
            else if ((voResult1[i].getNnumber() != null) && (voResult1[i].getNsimulatemny() != null))
            {
              voData.setNdebitmny(voResult1[i].getNsimulatemny().setScale(-nMoneyLength, 4));
            }

            if (voResult1[i].getNinvarymny() != null) {
              voData.setNinvarymny(voResult1[i].getNinvarymny().setScale(-nMoneyLength, 4));
            }

          }
          else
          {
            if (voResult1[i].getNnumber() != null) {
              voData.setNcreditnum(voResult1[i].getNnumber().setScale(-nNumberLength, 4));
            }

            if (voResult1[i].getNprice() != null) {
              voData.setNcreditprice(voResult1[i].getNprice().setScale(-nPriceLength, 4));
            }
            else if ((voResult1[i].getNnumber() != null) && (voResult1[i].getNnumber().doubleValue() != 0.0D) && (voResult1[i].getNsimulatemny() != null))
            {
              voData.setNcreditprice(voResult1[i].getNsimulatemny().div(voResult1[i].getNnumber()).setScale(-nPriceLength, 4));
            }

            if (voResult1[i].getNmoney() != null) {
              voData.setNcreditmny(voResult1[i].getNmoney().setScale(-nMoneyLength, 4));
            }
            else if ((voResult1[i].getNnumber() != null) && (voResult1[i].getNsimulatemny() != null))
            {
              voData.setNcreditmny(voResult1[i].getNsimulatemny().setScale(-nMoneyLength, 4));
            }

            if (voResult1[i].getNoutvarymny() != null) {
              voData.setNoutvarymny(voResult1[i].getNoutvarymny().setScale(-nMoneyLength, 4));
            }

          }

          voData.setNabnum(nabnumber.setScale(-nNumberLength, 4));

          voData.setNabprice(nabnumber.doubleValue() != 0.0D ? nabmoney.div(nabnumber).setScale(-nPriceLength, 4) : null);

          voData.setNabmny(nabmoney.setScale(-nMoneyLength, 4));

          voData.setNabvarymny(nabvary.setScale(-nMoneyLength, 4));

          voData.setVnote(voResult1[i].getVnote());
          vDatas.addElement(voData);
        }
      }
    }
    catch (Exception e) {
      throw new BusinessException("IAQueryBO::querySendDetailSingle(IaQueryConditionVO voCondition, Integer[] nDataPrecision, String ssaleadviceitemid) Exception!", e);
    }

    voDatas = new GoodsLedgerPrintVO[vDatas.size()];
    vDatas.copyInto(voDatas);
    return voDatas;
  }

  public String[] querySendDetailSingleHead(String ssaleadviceitemid)
    throws BusinessException
  {
    String[] sRetvalue = new String[3];
    try {
      GoodsledgerVO condv = new GoodsledgerVO();
      GoodsledgertotalImpl goodsBO = new GoodsledgertotalImpl();
      condv.setCcsaleadviceitemid(ssaleadviceitemid);
      GoodsledgerVO[] voResult = null;
      voResult = goodsBO.queryByVO_total(condv, Boolean.TRUE);
      if (voResult.length > 0) {
        sRetvalue[0] = voResult[0].getDeptname();
        sRetvalue[1] = voResult[0].getStorname();
        sRetvalue[2] = voResult[0].getVsourcebillcode();
      }
      else {
        IaInoutledgerVO vConditionInoutLedger = new IaInoutledgerVO();
        vConditionInoutLedger.setCcsaleadviceitemid(ssaleadviceitemid);
        IaInoutledgerVO[] voResult1 = getIaInoutledgerBO().queryByVO(vConditionInoutLedger, Boolean.TRUE, "a.dauditdate is null", " order by a.cbill_bid");

        if (voResult1.length > 0) {
          sRetvalue[0] = voResult1[0].getDeptname();
          sRetvalue[1] = voResult1[0].getStorname();
          sRetvalue[2] = voResult1[0].getVsourcebillcode();
        }
      }
    }
    catch (Exception e) {
      throw new BusinessException("IAQueryBO::querySendDetailSingleHead(String ssaleadviceitemid) Exception!", e);
    }

    return sRetvalue;
  }
}