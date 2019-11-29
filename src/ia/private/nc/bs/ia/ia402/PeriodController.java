package nc.bs.ia.ia402;

import java.util.Hashtable;
import nc.bs.ia.pub.TempTableDefine;
import nc.bs.ml.NCLangResOnserver;
import nc.impl.ia.pub.CommonDataImpl;
import nc.jdbc.framework.crossdb.CrossDBObject;
import nc.vo.ia.pub.IAMonthConfig;
import nc.vo.pub.BusinessException;

public class PeriodController
{
  public static boolean IsPeriodAB(String pk_corp, String period)
    throws BusinessException
  {
    CommonDataImpl cbo = new CommonDataImpl();
    int index = period.indexOf(45);

    String year = period.substring(0, index);
    String month = period.substring(index + 1);

    String lastmonth = cbo.getLastMonthInYear(pk_corp, year);

    if (month.equalsIgnoreCase(lastmonth)) {
      return true;
    }
    String sSql = "select periodnum from bd_accperiod  where periodyear='" + year + "' ";

    String[][] result = (String[][])null;
    int numMonth = 0;

    result = cbo.queryData(sSql);

    if ((result != null) && (result.length > 0))
    {
      if (result[0].length > 0) {
        numMonth = Integer.parseInt(result[0][0]);
      }else{
    	  throw new BusinessException("得不到会计年" + year + "的会计月的个数");
      }
    }else{
        throw new BusinessException("得不到会计年" + year + "的会计月的个数");
    }
    int number = -1;
    number = IAMonthConfig.getABTimes();

    if (number <= 0) {
      number = 4;
    }

    int n = numMonth / number;

    for (int i = 1; i < number; ++i)
    {
      Integer mon = new Integer(month);

      if (mon.intValue() == i * n) {
        return true;
      }
    }

    return false;
  }

  public static String getLastABPeriod(String pk_corp, String period)
    throws BusinessException
  {
    String sSql = "select max(caccountyear||'-'||caccountmonth) from ia_period where pk_corp='" + pk_corp + "' and caccountyear||'-'||caccountmonth<='" + period + "' ";

    CommonDataImpl cbo = new CommonDataImpl();

    String[][] result = cbo.queryData(sSql);

    if ((result != null) && (result.length > 0) && (result[0] != null) && (result[0].length > 0) && (result[0][0] != null) && (result[0][0].trim().length() > 0))
    {
      return result[0][0];
    }

    String sCorp = " select unitcode from bd_corp where pk_corp = '" + pk_corp + "' ";
    String[][] rs = cbo.queryData(sCorp);
    String corpcode = null;
    if ((rs != null) && (rs.length > 0) && (rs[0] != null) && (rs[0].length > 0) && (rs[0][0] != null) && (rs[0][0].length() > 0))
    {
      corpcode = rs[0][0];
    }
    else throw new BusinessException("读取公司编码出错");

    String message = NCLangResOnserver.getInstance().getStrByID("2014", "UPP2014-000662") + corpcode + NCLangResOnserver.getInstance().getStrByID("2014", "UPP2014-000664");

    throw new BusinessException(message);
  }

  public static String getLastABPeriod(String pk_corp)
    throws BusinessException
  {
    String sSql = "select max(caccountyear||'-'||caccountmonth) from ia_period where pk_corp='" + pk_corp + "'";

    CommonDataImpl cbo = new CommonDataImpl();

    String[][] result = cbo.queryData(sSql);

    if ((result != null) && (result.length > 0) && (result[0] != null) && (result[0].length > 0) && (result[0][0] != null) && (result[0][0].trim().length() > 0))
    {
      return result[0][0];
    }

    String sCorp = " select unitcode from bd_corp where pk_corp = '" + pk_corp + "' ";
    String[][] rs = cbo.queryData(sCorp);
    String corpcode = null;
    if ((rs != null) && (rs.length > 0) && (rs[0] != null) && (rs[0].length > 0) && (rs[0][0] != null) && (rs[0][0].length() > 0))
    {
      corpcode = rs[0][0];
    }
    else throw new BusinessException("读取公司编码出错");

    String message = NCLangResOnserver.getInstance().getStrByID("2014", "UPP2014-000662") + corpcode + NCLangResOnserver.getInstance().getStrByID("2014", "UPP2014-000664");

    throw new BusinessException(message);
  }

  public static void doPeriodAccount(String pk_corp, String period)
    throws BusinessException
  {
    int index = period.indexOf(45);

    String year = period.substring(0, index);
    String month = period.substring(index + 1);

    String[] sBizs = new String[2];
    sBizs[0] = "FQSK";
    sBizs[1] = "WTDX";

    CommonDataImpl cbo = new CommonDataImpl();
    Hashtable ht = cbo.getBizTypeIDs(pk_corp, sBizs);
    String sFQSK = (String)ht.get("FQSK");
    String sWTDX = (String)ht.get("WTDX");

    StringBuffer sSql = new StringBuffer();

    sSql.append(" insert into ia_monthinout");
    sSql.append(" ( ");
    sSql.append(" vproducebatch, cbilltypecode, cinventoryid, crdcenterid, cstockrdcenterid,");
    sSql.append(" cwarehouseid, cwarehousemanagerid, cdeptid, cemployeeid, ccustomvendorid,");
    sSql.append(" cvendorid, cagentid,fdispatchflag, cdispatchid, cprojectid, castunitid, coperatorid,");
    sSql.append(" caccountyear, caccountmonth, pk_corp,");
    sSql.append(" cprojectphase, bwithdrawalflag, fpricemodeflag, vbatch, cwp,");
    sSql.append(" vfree1, vfree2, vfree3, vfree4, vfree5,");
    sSql.append(" ninnum, ninassistnum, ninmny, ninplanedmny, ninvarymny,");
    sSql.append(" noutnum, noutassistnum, noutmny, noutplanedmny, noutvarymny, nbacktaxmny, ts");
    sSql.append(" ) ");
    sSql.append(" (");
    sSql.append(" select ");
    sSql.append(" b.vproducebatch, b.cbilltypecode, b.cinventoryid, h.crdcenterid, h.cstockrdcenterid,");
    sSql.append(" h.cwarehouseid, h.cwarehousemanagerid, h.cdeptid, h.cemployeeid, h.ccustomvendorid,");
    sSql.append(" b.cvendorid, h.cagentid, h.fdispatchflag, h.cdispatchid, b.cprojectid, b.castunitid, h.coperatorid,");
    sSql.append(" h.caccountyear, h.caccountmonth, h.pk_corp,");
    sSql.append(" b.cprojectphase, h.bwithdrawalflag, b.fpricemodeflag, case when b.bauditbatchflag = 'Y' then b.vbatch else null end as vbatch, b.cwp,");
    sSql.append(" b.vfree1, b.vfree2, b.vfree3, b.vfree4, b.vfree5,");
    sSql.append(" sum(case when h.fdispatchflag=0 then coalesce(b.nnumber,0) else 0 end) as ninnumber,");
    sSql.append(" sum(case when h.fdispatchflag=0 then coalesce(b.nassistnum,0) else 0 end) as ninassistnum,");
    sSql.append(" sum(case when h.fdispatchflag=0 then coalesce(b.nmoney,0) else 0 end) as ninmny,");
    sSql.append(" sum(case when h.fdispatchflag=0 then coalesce(b.nplanedmny,0) else 0 end) as ninplanedmny,");
    sSql.append(" sum(case when h.fdispatchflag=0 then coalesce(b.ninvarymny,0) else 0 end) as ninvarymny,");
    sSql.append(" sum(case when h.fdispatchflag=1 then coalesce(b.nnumber,0) else 0 end) as noutnumber,");
    sSql.append(" sum(case when h.fdispatchflag=1 then coalesce(b.nassistnum,0) else 0 end) as noutassistnum,");
    sSql.append(" sum(case when h.fdispatchflag=1 then coalesce(b.nmoney,0) else 0 end) as noutmny,");
    sSql.append(" sum(case when h.fdispatchflag=1 then coalesce(b.nplanedmny,0) else 0 end) as noutplanedmny,");
    sSql.append(" sum(case when h.fdispatchflag=1 then coalesce(b.noutvarymny,0) else 0 end) as noutvarymny,");
    sSql.append(" sum(case when b.ndrawsummny is not null or b.cbilltypecode='IH' then coalesce(b.nmoney,0)+coalesce(b.ndrawsummny,0) else 0 end) as nbacktaxmny, ");
    sSql.append(" '" + CrossDBObject.getCurrentTimeStampString() + "' ");
    sSql.append(" from ia_bill h, ia_bill_b b ");
    sSql.append(" where h.cbillid = b.cbillid and h.bdisableflag = 'N' and h.dr = 0 and b.dr = 0 and b.iauditsequence>0 ");
    sSql.append(" and b.cbilltypecode in ('I2', 'I3', 'I4', 'I5', 'I6', 'I7', 'I8', 'I9', 'IA', 'IB', 'IC', 'ID', 'II', 'IJ','IH') ");
    sSql.append(" and caccountyear='" + year + "' and caccountmonth='" + month + "' and h.pk_corp='" + pk_corp + "' ");
    sSql.append(" and (cbiztypeid is null or cbiztypeid not in (" + sFQSK + "," + sWTDX + ") or csourcebilltypecode is null or csourcebilltypecode<>'32')");
    sSql.append(" group by ");
    sSql.append(" b.vproducebatch, b.cbilltypecode, b.cinventoryid, h.crdcenterid, h.cstockrdcenterid,");
    sSql.append(" h.cwarehouseid, h.cwarehousemanagerid, h.cdeptid, h.cemployeeid, h.ccustomvendorid,");
    sSql.append(" b.cvendorid, h.cagentid, h.fdispatchflag, h.cdispatchid, b.cprojectid, b.castunitid, h.coperatorid,");
    sSql.append(" h.caccountyear, h.caccountmonth, h.pk_corp,");
    sSql.append(" b.cprojectphase, h.bwithdrawalflag, b.fpricemodeflag, case when b.bauditbatchflag = 'Y' then b.vbatch else null end, b.cwp,");
    sSql.append(" b.vfree1, b.vfree2, b.vfree3, b.vfree4, b.vfree5 ");
    sSql.append(" )");

    cbo.execData(sSql.toString());
  }

  public static void doABAccount(String pk_corp, String period)
    throws BusinessException
  {
    CommonDataImpl cbo = new CommonDataImpl();

    TempTableDefine define = new TempTableDefine();
    String temp = define.getPeriodAccountTable(new String[0][0]);

    String lastPeriod = getLastABPeriod(pk_corp, period);
    String nextPeriod = cbo.getNextPeriod(pk_corp, lastPeriod);

    String lastyear = lastPeriod.substring(0, lastPeriod.indexOf(45));
    String lastmonth = lastPeriod.substring(lastPeriod.indexOf(45) + 1);

    String nextmonth = nextPeriod.substring(lastPeriod.indexOf(45) + 1);

    String year = period.substring(0, period.indexOf(45));
    String month = period.substring(period.indexOf(45) + 1);

    StringBuffer sSql = new StringBuffer();

    sSql.append(" insert into " + temp);
    sSql.append(" (");
    sSql.append(" vproducebatch, cinventoryid, crdcenterid, cstockrdcenterid,");
    sSql.append(" cwarehouseid, cwarehousemanagerid, cdeptid, cemployeeid, ccustomvendorid,");
    sSql.append(" cvendorid, cagentid, cprojectid, castunitid, coperatorid,");
    sSql.append(" caccountyear, caccountmonth, pk_corp,");
    sSql.append(" cprojectphase, fpricemodeflag, vbatch, cwp,");
    sSql.append(" vfree1, vfree2, vfree3, vfree4, vfree5,");
    sSql.append(" ninnum, ninassistnum, ninmny, ninplanedmny, ninvarymny,");
    sSql.append(" noutnum, noutassistnum, noutmny, noutplanedmny, noutvarymny,");
    sSql.append(" nabnum, nabassistnum, nabmny, nabplanedmny, nabvarymny");
    sSql.append(" )");
    sSql.append(" (");
    sSql.append(" select vproducebatch, cinventoryid, crdcenterid, cstockrdcenterid,");
    sSql.append(" cwarehouseid, cwarehousemanagerid, cdeptid, cemployeeid, ccustomvendorid,");
    sSql.append(" cvendorid, cagentid, cprojectid, castunitid, coperatorid,");
    sSql.append("'" + year + "' as caccountyear, '" + month + "' as caccountmonth, pk_corp,");
    sSql.append(" cprojectphase, fpricemodeflag, vbatch, cwp,");
    sSql.append(" vfree1, vfree2, vfree3, vfree4, vfree5,");
    sSql.append(" ninnum, ninassistnum, ninmny, ninplanedmny, ninvarymny,");
    sSql.append(" noutnum, noutassistnum, noutmny, noutplanedmny, noutvarymny,");
    sSql.append(" nabnum, nabassistnum, nabmny, nabplanedmny, nabvarymny ");
    sSql.append(" from ia_periodaccount ");
    sSql.append(" where pk_corp='" + pk_corp + "' and caccountyear='" + lastyear + "' and caccountmonth='" + lastmonth + "' ");
    sSql.append(" )");

    cbo.execData(sSql.toString());

    sSql.setLength(0);

    sSql.append(" insert into " + temp);
    sSql.append(" (");
    sSql.append(" vproducebatch, cinventoryid, crdcenterid, cstockrdcenterid,");
    sSql.append(" cwarehouseid, cwarehousemanagerid, cdeptid, cemployeeid, ccustomvendorid,");
    sSql.append(" cvendorid, cagentid, cprojectid, castunitid, coperatorid,");
    sSql.append(" caccountyear, caccountmonth, pk_corp,");
    sSql.append(" cprojectphase, fpricemodeflag, vbatch, cwp,");
    sSql.append(" vfree1, vfree2, vfree3, vfree4, vfree5,");
    sSql.append(" ninnum, ninassistnum, ninmny, ninplanedmny, ninvarymny,");
    sSql.append(" noutnum, noutassistnum, noutmny, noutplanedmny, noutvarymny,");
    sSql.append(" nabnum, nabassistnum, nabmny, nabplanedmny, nabvarymny");
    sSql.append(" )");
    sSql.append(" (");
    sSql.append(" select vproducebatch, cinventoryid, crdcenterid, cstockrdcenterid,");
    sSql.append(" cwarehouseid, cwarehousemanagerid, cdeptid, cemployeeid, ccustomvendorid,");
    sSql.append(" cvendorid, cagentid, cprojectid, castunitid, coperatorid, ");
    sSql.append("'" + year + "' as caccountyear, '" + month + "' as caccountmonth, pk_corp,");
    sSql.append(" cprojectphase, fpricemodeflag, vbatch, cwp,");
    sSql.append(" vfree1, vfree2, vfree3, vfree4, vfree5,");
    sSql.append(" ninnum, ninassistnum, ninmny, ninplanedmny, ninvarymny,");
    sSql.append(" noutnum, noutassistnum, noutmny, noutplanedmny, noutvarymny,");
    sSql.append(" (ninnum-noutnum) as nabnum, (ninassistnum-noutassistnum) as nabassistnum,");
    sSql.append(" (ninmny-noutmny) as nabmny, (ninplanedmny-noutplanedmny) as nabplanedmny, (ninvarymny-noutvarymny) as nabvarymny ");
    sSql.append(" from ia_monthinout ");
    sSql.append(" where pk_corp='" + pk_corp + "' and caccountyear='" + year + "' ");
    sSql.append(" and caccountmonth>='" + nextmonth + "' and caccountmonth<='" + month + "' ");
    sSql.append(" and cbilltypecode in ('I2','I9','I4','ID','I3','IB','II','I5','I6','I7','I8','IA','IC','IJ') ");
    sSql.append(" )");

    cbo.execData(sSql.toString());

    sSql.setLength(0);

    sSql.append(" insert into ia_periodaccount");
    sSql.append(" (");
    sSql.append(" vproducebatch, cinventoryid, crdcenterid, cstockrdcenterid,");
    sSql.append(" cwarehouseid, cwarehousemanagerid, cdeptid, cemployeeid, ccustomvendorid,");
    sSql.append(" cvendorid, cagentid, cprojectid, castunitid, coperatorid,");
    sSql.append(" caccountyear, caccountmonth, pk_corp,");
    sSql.append(" cprojectphase, fpricemodeflag, vbatch, cwp,");
    sSql.append(" vfree1, vfree2, vfree3, vfree4, vfree5,");
    sSql.append(" ninnum, ninassistnum, ninmny, ninplanedmny, ninvarymny,");
    sSql.append(" noutnum, noutassistnum, noutmny, noutplanedmny, noutvarymny,");
    sSql.append(" nabnum, nabassistnum, nabmny, nabplanedmny, nabvarymny, ts");
    sSql.append(" )");
    sSql.append(" (");
    sSql.append(" select vproducebatch, cinventoryid, crdcenterid, cstockrdcenterid,");
    sSql.append(" cwarehouseid, cwarehousemanagerid, cdeptid, cemployeeid, ccustomvendorid,");
    sSql.append(" cvendorid, cagentid, cprojectid, castunitid, coperatorid, ");
    sSql.append(" caccountyear, caccountmonth, pk_corp,");
    sSql.append(" cprojectphase, fpricemodeflag, vbatch, cwp,");
    sSql.append(" vfree1, vfree2, vfree3, vfree4, vfree5,");
    sSql.append(" sum(ninnum), sum(ninassistnum), sum(ninmny), sum(ninplanedmny), sum(ninvarymny),");
    sSql.append(" sum(noutnum), sum(noutassistnum), sum(noutmny), sum(noutplanedmny), sum(noutvarymny),");
    sSql.append(" sum(nabnum), sum(nabassistnum), sum(nabmny), sum(nabplanedmny), sum(nabvarymny), ");
    sSql.append(" '" + CrossDBObject.getCurrentTimeStampString() + "' ");
    sSql.append(" from " + temp);
    sSql.append(" group by ");
    sSql.append(" vproducebatch, cbilltypecode, cinventoryid, crdcenterid, cstockrdcenterid,");
    sSql.append(" cwarehouseid, cwarehousemanagerid, cdeptid, cemployeeid, ccustomvendorid,");
    sSql.append(" cvendorid, cagentid, cprojectid, castunitid, coperatorid, ");
    sSql.append(" caccountyear, caccountmonth, pk_corp,");
    sSql.append(" cprojectphase, fpricemodeflag, vbatch, cwp,");
    sSql.append(" vfree1, vfree2, vfree3, vfree4, vfree5 ");
    sSql.append(" )");

    cbo.execData(sSql.toString());

    sSql.setLength(0);

    sSql.append(" insert into ia_period(pk_corp, caccountyear, caccountmonth) values('" + pk_corp + "','" + year + "','" + month + "')");

    cbo.execData(sSql.toString());

    sSql.setLength(0);
  }

  public static void redoPeriodAccount(String pk_corp, String period)
    throws BusinessException
  {
    String year = period.substring(0, period.indexOf(45));
    String month = period.substring(period.indexOf(45) + 1);

    String sSql = " delete from ia_monthinout where  pk_corp='" + pk_corp + "' " + " and caccountyear='" + year + "' " + " and caccountmonth='" + month + "' ";

    CommonDataImpl cbo = new CommonDataImpl();
    cbo.execData(sSql);
  }

  public static void redoABAccount(String pk_corp, String period)
    throws BusinessException
  {
    String year = period.substring(0, period.indexOf(45));
    String month = period.substring(period.indexOf(45) + 1);

    String sSql = " delete from ia_periodaccount where  pk_corp='" + pk_corp + "' " + " and caccountyear='" + year + "' " + " and caccountmonth='" + month + "' ";

    CommonDataImpl cbo = new CommonDataImpl();
    cbo.execData(sSql);

    sSql = "";
    sSql = " delete from ia_period where  pk_corp='" + pk_corp + "' " + " and caccountyear='" + year + "' " + " and caccountmonth='" + month + "' ";

    cbo.execData(sSql);
  }

  public static void doBeginAccount(String pk_corp, String year)
    throws BusinessException
  {
    StringBuffer strSql = new StringBuffer();

    CommonDataImpl cbo = new CommonDataImpl();

    strSql.append(" insert into ia_periodaccount");
    strSql.append(" (");
    strSql.append(" vproducebatch, cinventoryid, crdcenterid, cstockrdcenterid,");
    strSql.append(" cwarehouseid, cwarehousemanagerid, cdeptid, cemployeeid, ccustomvendorid,");
    strSql.append(" cvendorid, cagentid, cprojectid, castunitid, coperatorid,");
    strSql.append(" caccountyear, caccountmonth, pk_corp,");
    strSql.append(" cprojectphase, fpricemodeflag, vbatch, cwp,");
    strSql.append(" vfree1, vfree2, vfree3, vfree4, vfree5,");
    strSql.append(" ninnum, ninassistnum, ninmny, ninplanedmny, ninvarymny,");
    strSql.append(" noutnum, noutassistnum, noutmny, noutplanedmny, noutvarymny,");
    strSql.append(" nabnum, nabassistnum, nabmny, nabplanedmny, nabvarymny, ts ");
    strSql.append(" )");
    strSql.append(" (select ");
    strSql.append(" b.vproducebatch, b.cinventoryid, h.crdcenterid, h.cstockrdcenterid,");
    strSql.append(" h.cwarehouseid, h.cwarehousemanagerid, h.cdeptid, h.cemployeeid, h.ccustomvendorid,");
    strSql.append(" b.cvendorid, h.cagentid, b.cprojectid, b.castunitid, h.coperatorid,");
    strSql.append(" h.caccountyear, h.caccountmonth, h.pk_corp,");
    strSql.append(" b.cprojectphase, b.fpricemodeflag, case when b.bauditbatchflag = 'Y' then b.vbatch else null end as vbatch, b.cwp,");
    strSql.append(" b.vfree1, b.vfree2, b.vfree3, b.vfree4, b.vfree5,");
    strSql.append(" sum(case when h.fdispatchflag=0 then coalesce(b.nnumber,0) else 0 end) as ninnumber,");
    strSql.append(" sum(case when h.fdispatchflag=0 then coalesce(b.nassistnum,0) else 0 end) as ninassistnum,");
    strSql.append(" sum(case when h.fdispatchflag=0 then coalesce(b.nmoney,0) else 0 end) as ninmny,");
    strSql.append(" sum(case when h.fdispatchflag=0 then coalesce(b.nplanedmny,0) else 0 end) as ninplanedmny,");
    strSql.append(" sum(case when h.fdispatchflag=0 then coalesce(b.ninvarymny,0) else 0 end) as ninvarymny,");
    strSql.append(" sum(case when h.fdispatchflag=1 then coalesce(b.nnumber,0) else 0 end) as noutnumber,");
    strSql.append(" sum(case when h.fdispatchflag=1 then coalesce(b.nassistnum,0) else 0 end) as noutassistnum,");
    strSql.append(" sum(case when h.fdispatchflag=1 then coalesce(b.nmoney,0) else 0 end) as noutmny,");
    strSql.append(" sum(case when h.fdispatchflag=1 then coalesce(b.nplanedmny,0) else 0 end) as noutplanedmny,");
    strSql.append(" sum(case when h.fdispatchflag=1 then coalesce(b.ninvarymny,0) else 0 end) as noutvarymny,");
    strSql.append(" sum(case when h.fdispatchflag=0 then coalesce(b.nnumber,0) else -coalesce(b.nnumber,0) end) as nabnumber,");
    strSql.append(" sum(case when h.fdispatchflag=0 then coalesce(b.nassistnum,0) else -coalesce(b.nassistnum,0) end) as nabassistnum,");
    strSql.append(" sum(case when h.fdispatchflag=0 then coalesce(b.nmoney,0) else -coalesce(b.nmoney,0) end) as nabmny,");
    strSql.append(" sum(case when h.fdispatchflag=0 then coalesce(b.nplanedmny,0) else -coalesce(b.nplanedmny,0) end) as nabplanedmny,");
    strSql.append(" sum(case when h.fdispatchflag=0 then coalesce(b.ninvarymny,0) else -coalesce(b.noutvarymny,0) end) as nabvarymny,");
    strSql.append(" '" + CrossDBObject.getCurrentTimeStampString() + "' ");
    strSql.append(" from ia_bill h, ia_bill_b b ");
    strSql.append(" where h.cbillid = b.cbillid and h.bdisableflag = 'N' and h.dr = 0 and b.dr = 0 and b.iauditsequence>0 ");
    strSql.append(" and b.cbilltypecode in ('I0','I1') ");
    strSql.append(" and caccountyear='" + year + "' and caccountmonth='00' and h.pk_corp='" + pk_corp + "' ");
    strSql.append(" group by ");
    strSql.append(" b.vproducebatch, b.cbilltypecode, b.cinventoryid, h.crdcenterid, h.cstockrdcenterid,");
    strSql.append(" h.cwarehouseid, h.cwarehousemanagerid, h.cdeptid, h.cemployeeid, h.ccustomvendorid,");
    strSql.append(" b.cvendorid, h.cagentid, b.cprojectid, b.castunitid, h.coperatorid,");
    strSql.append(" h.caccountyear, h.caccountmonth, h.pk_corp,");
    strSql.append(" b.cprojectphase, h.bwithdrawalflag, b.fpricemodeflag, case when b.bauditbatchflag = 'Y' then b.vbatch else null end, b.cwp,");
    strSql.append(" b.vfree1, b.vfree2, b.vfree3, b.vfree4, b.vfree5 ");
    strSql.append(" having sum(case when h.fdispatchflag=0 then coalesce(b.nnumber,0) else 0 end)!=0 ");
    strSql.append(" or sum(case when h.fdispatchflag=0 then coalesce(b.nassistnum,0) else 0 end)!=0 ");
    strSql.append(" or sum(case when h.fdispatchflag=0 then coalesce(b.nmoney,0) else 0 end)!=0 ");
    strSql.append(" or sum(case when h.fdispatchflag=0 then coalesce(b.nplanedmny,0) else 0 end)!=0 ");
    strSql.append(" or sum(case when h.fdispatchflag=1 then coalesce(b.nnumber,0) else 0 end)!=0 ");
    strSql.append(" or sum(case when h.fdispatchflag=1 then coalesce(b.nassistnum,0) else 0 end)!=0 ");
    strSql.append(" or sum(case when h.fdispatchflag=1 then coalesce(b.nmoney,0) else 0 end)!=0 ");
    strSql.append(" or sum(case when h.fdispatchflag=1 then coalesce(b.nplanedmny,0) else 0 end)!=0 ");
    strSql.append(" )");

    cbo.execData(strSql.toString());

    strSql.setLength(0);
    strSql.append(" insert into ia_period ");
    strSql.append(" (caccountyear, caccountmonth, pk_corp) ");
    strSql.append(" values('" + year + "', '00', '" + pk_corp + "' ) ");

    cbo.execData(strSql.toString());
  }

  public static void redoBeginAccount(String pk_corp, String year)
    throws BusinessException
  {
    redoABAccount(pk_corp, year + "-00");
  }
}