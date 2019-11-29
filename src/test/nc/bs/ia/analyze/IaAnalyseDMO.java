package nc.bs.ia.analyze;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;
import javax.naming.NamingException;
import nc.bs.ia.ia402.PeriodController;
import nc.bs.ia.pub.CommonDataDMO;
import nc.bs.ia.pub.TempTableDefine;
import nc.bs.ia.reportalgo.IReportAlgo;
import nc.bs.ia.reportalgo.ReportAlgoController;
import nc.bs.ia.reportalgo.SQLStringUtil;
import nc.bs.pub.DataManageObject;
import nc.bs.pub.SystemException;
import nc.impl.ia.pub.CommonDataImpl;
import nc.vo.ia.analyze.InvInOutSumVO;
import nc.vo.ia.analyze.QueryVO;
import nc.vo.ia.analyze.StatisticsVO;
import nc.vo.ia.analyze.VelocityVO;
import nc.vo.ia.pub.Log;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

public class IaAnalyseDMO extends DataManageObject
{
  public IaAnalyseDMO()
    throws NamingException, SystemException
  {
  }

  public IaAnalyseDMO(String dbName)
    throws NamingException, SystemException
  {
    super(dbName);
  }

  public Vector getAbc(String sqlWhere, String codes, String flag, String[] params, String xsfp)
    throws SQLException
  {
    beforeCallMethod("nc.bs.ia.analyze.IaAnalyseDMO", "getAbc", new Object[] { sqlWhere });

    Vector result = new Vector();
    Object[][] abc = new Object[3][5];
    UFDouble money = new UFDouble(0);
    StringBuffer sbSql = new StringBuffer();
    String pk_corp = params[3];
    String ABPeriod = "";
    try {
      ABPeriod = PeriodController.getLastABPeriod(pk_corp);
    } catch (BusinessException e1) {
      Log.error(e1);
      return null;
    }

    String year = ABPeriod.substring(0, 4);
    String month = ABPeriod.substring(5);

    StringBuffer sbSqlJoinInv = new StringBuffer();
    sbSqlJoinInv.append(" inner join (select bd_invmandoc.pk_invmandoc invid,bd_invbasdoc.invcode invcode,bd_invcl.invclasscode invclasscode,bd_invbasdoc.invname invname,bd_invbasdoc.pk_invcl pk_invcl,bd_invcl.invclassname invtype,bd_invcl.invclasslev invclasslev, \n");

    sbSqlJoinInv.append(" bd_measdoc.measname measname,bd_invbasdoc.invspec invspec, bd_invbasdoc.invtype invtypes, bd_invmandoc.abctype abctype,bd_invbasdoc.pk_invbasdoc invbasdocs ,bd_invmandoc.planprice invplanprice\n");

    sbSqlJoinInv.append(" from bd_invmandoc,bd_invbasdoc inner join bd_invcl on bd_invcl.pk_invcl = bd_invbasdoc.pk_invcl inner join bd_measdoc on bd_measdoc.pk_measdoc=bd_invbasdoc.pk_measdoc where bd_invbasdoc.pk_invbasdoc = bd_invmandoc.pk_invbasdoc) inv  \n");

    sbSqlJoinInv.append(" on v.cinventoryid = inv.invid  \n");
    StringBuffer sbSqlJoinCalbody = new StringBuffer();

    sbSqlJoinCalbody.append(" left outer join bd_calbody on v.crdcenterid = bd_calbody.pk_calbody \n");

    StringBuffer sbSqlJoinRdcl = new StringBuffer();

    sbSqlJoinRdcl.append(" left outer join bd_rdcl on v.cdispatchid = bd_rdcl.pk_rdcl \n");

    StringBuffer sbSqlJoinDept = new StringBuffer();

    sbSqlJoinDept.append(" left outer join bd_deptdoc on v.cdeptid = bd_deptdoc.pk_deptdoc \n");

    StringBuffer sbSqlJoinStor = new StringBuffer();

    sbSqlJoinStor.append(" left outer join bd_stordoc on v.cwarehouseid = bd_stordoc.pk_stordoc \n");

    StringBuffer sbSqlJoinBusi = new StringBuffer();

    sbSqlJoinBusi.append(" left outer join bd_busitype on v.cbiztypeid = bd_busitype.pk_busitype \n");

    StringBuffer sbSqlJoinProduce = new StringBuffer();

    sbSqlJoinProduce.append(" left outer join bd_produce on v.crdcenterid = bd_produce.pk_calbody and v.cinventoryid = bd_produce.pk_invmandoc \n");

    StringBuffer sbDefaultSqlWhere = new StringBuffer();
    sbDefaultSqlWhere.append("   and (v.badjustedItemflag<>'Y' or  v.cbilltypecode<>'I9')  and v.iauditsequence > 0 \n");
    sbDefaultSqlWhere.append("   and (bd_busitype.verifyrule is null or v.csourcebilltypecode is null \n");

    sbDefaultSqlWhere.append("   or ((bd_busitype.verifyrule <> 'F'  or v.csourcebilltypecode<> '32') \n");

    sbDefaultSqlWhere.append("   and (bd_busitype.verifyrule <> 'W' or v.csourcebilltypecode<> '32'))) \n");

    String sqlabc = "select vcategoryname,COALESCE(nstartamountrate,0),COALESCE(nendamountrate,0),COALESCE(nstartqntrate,0),COALESCE(nendqntrate,0) from ia_abcdefinition where pk_corp ='" + codes + "' order by  vcategoryname";

    if (flag.equals("0")) {
      sbSql.append(" select invcode, invname ,invspec,invtypes ,measname , sum(vnumber), sum(vmoney), abcdoc, nplanedprice \n");
      sbSql.append(" from (");
      sbSql.append(" (select inv.invcode, inv.invname ,inv.invspec,inv.invtypes ,inv.measname , \n");
      sbSql.append(" sum(nabnum) as vnumber, sum(nabmny) as vmoney, ");
      sbSql.append(" (case when count(distinct (v.crdcenterid))>=2 then inv.abctype  else bd_produce.abcfundeg  end) as abcdoc,");
      sbSql.append(" case when  COALESCE(bd_produce.jhj,0)=0 then COALESCE(inv.invplanprice,0) else bd_produce.jhj end as nplanedprice ");
      sbSql.append(" from ia_periodaccount v ");
      sbSql.append(sbSqlJoinInv);

      if (sqlWhere.indexOf("bd_calbody.bodycode") >= 0) {
        sbSql.append(sbSqlJoinCalbody);
      }
      if (sqlWhere.indexOf("bd_rdcl") >= 0) {
        sbSql.append(sbSqlJoinRdcl);
      }
      if (sqlWhere.indexOf("bd_deptdoc.deptcode") >= 0) {
        sbSql.append(sbSqlJoinDept);
      }
      if (sqlWhere.indexOf("bd_stordoc.storcode") >= 0)
        sbSql.append(sbSqlJoinStor);
      sbSql.append(sbSqlJoinProduce);
      sbSql.append(" where v.caccountyear='" + year + "' and v.caccountmonth='" + month + "' and " + sqlWhere + " \n");
      sbSql.append(" group by inv.invcode,inv.invname,inv.invspec,inv.invtypes,inv.measname,inv.abctype,bd_produce.abcfundeg ,inv.invplanprice , bd_produce.jhj \n");
      sbSql.append(" ) union all (");

      sbSql.append(" select inv.invcode, inv.invname ,inv.invspec,inv.invtypes ,inv.measname , \n");
      sbSql.append(" sum( case when  v.fdispatchflag = 0 then  COALESCE(v.nnumber,0) else 0-COALESCE(v.nnumber,0) end) as vnumber, \n");
      sbSql.append(" sum( case when  v.fdispatchflag = 0 then COALESCE(v.nmoney,0) else 0-COALESCE(v.nmoney,0) end) as vmoney ,(case when count(distinct (v.crdcenterid))>=2 then inv.abctype  else bd_produce.abcfundeg  end) as abcdoc, case when  COALESCE(bd_produce.jhj,0)=0 then COALESCE(inv.invplanprice,0) else bd_produce.jhj end as nplanedprice  from  v_ia_inoutledger v \n");

      sbSql.append(sbSqlJoinInv);

      if (sqlWhere.indexOf("bd_calbody.bodycode") >= 0) {
        sbSql.append(sbSqlJoinCalbody);
      }
      if (sqlWhere.indexOf("bd_rdcl") >= 0) {
        sbSql.append(sbSqlJoinRdcl);
      }
      if (sqlWhere.indexOf("bd_deptdoc.deptcode") >= 0) {
        sbSql.append(sbSqlJoinDept);
      }
      if (sqlWhere.indexOf("bd_stordoc.storcode") >= 0)
        sbSql.append(sbSqlJoinStor);
      sbSql.append(sbSqlJoinBusi);
      sbSql.append(sbSqlJoinProduce);
      sbSql.append(" where v.caccountyear||'-'||v.caccountmonth>'" + ABPeriod + "' ");
      sbSql.append(" and v.cbilltypecode <> 'I1' \n");
      sbDefaultSqlWhere.append("  and  v.cbilltypecode in " + params[2] + " and " + sqlWhere + " \n");

      sbSql.append(sbDefaultSqlWhere);
      sbSql.append(" group by inv.invcode,inv.invname,inv.invspec,inv.invtypes,inv.measname,inv.abctype,bd_produce.abcfundeg ,inv.invplanprice , bd_produce.jhj \n");
      sbSql.append(" ) ) as datasource ");

      sbSql.append(" group by invcode,invname,invspec,invtypes,measname,abcdoc ,nplanedprice  \n");
      sbSql.append("  order by sum(vmoney) desc ");
    }
    if (flag.equals("1"))
    {
      sbSql.append(" select invcode, invname ,invspec,invtypes ,measname , sum(vnumber), sum(vmoney), abcdoc, nplanedprice \n");
      sbSql.append(" from (");
      sbSql.append(" (select inv.invcode, inv.invname ,inv.invspec,inv.invtypes ,inv.measname , \n");
      sbSql.append(" sum(ninnum) as vnumber, sum(ninmny) as vmoney, ");
      sbSql.append(" (case when count(distinct (v.crdcenterid))>=2 then inv.abctype  else bd_produce.abcfundeg  end) as abcdoc,");
      sbSql.append(" case when  COALESCE(bd_produce.jhj,0)=0 then COALESCE(inv.invplanprice,0) else bd_produce.jhj end as nplanedprice ");
      sbSql.append(" from ia_periodaccount v ");
      sbSql.append(sbSqlJoinInv);

      if (sqlWhere.indexOf("bd_calbody.bodycode") >= 0) {
        sbSql.append(sbSqlJoinCalbody);
      }
      if (sqlWhere.indexOf("bd_rdcl") >= 0) {
        sbSql.append(sbSqlJoinRdcl);
      }
      if (sqlWhere.indexOf("bd_deptdoc.deptcode") >= 0) {
        sbSql.append(sbSqlJoinDept);
      }
      if (sqlWhere.indexOf("bd_stordoc.storcode") >= 0)
        sbSql.append(sbSqlJoinStor);
      sbSql.append(sbSqlJoinProduce);
      sbSql.append(" where v.caccountyear='" + year + "' and v.caccountmonth='" + month + "' and " + sqlWhere + " \n");
      sbSql.append(" group by inv.invcode,inv.invname,inv.invspec,inv.invtypes,inv.measname,inv.abctype,bd_produce.abcfundeg ,inv.invplanprice , bd_produce.jhj \n");
      sbSql.append(" ) union all (");

      sbSql.append("select inv.invcode, inv.invname ,inv.invspec,inv.invtypes ,inv.measname , \n");

      sbSql.append(" SUM( COALESCE(v.nnumber,0)) as vnumber,sum(COALESCE(v.nmoney,0)) as vmoney,(case when count(distinct (v.crdcenterid))>=2 then inv.abctype  else bd_produce.abcfundeg  end) as abcdoc ,case when  COALESCE(bd_produce.jhj,0)=0 then COALESCE(inv.invplanprice,0) else bd_produce.jhj end as nplanedprice from  v_ia_inoutledger v \n");

      sbSql.append(sbSqlJoinInv);

      if (sqlWhere.indexOf("bd_calbody.bodycode") >= 0) {
        sbSql.append(sbSqlJoinCalbody);
      }
      if (sqlWhere.indexOf("bd_rdcl") >= 0) {
        sbSql.append(sbSqlJoinRdcl);
      }
      if (sqlWhere.indexOf("bd_deptdoc.deptcode") >= 0) {
        sbSql.append(sbSqlJoinDept);
      }
      if (sqlWhere.indexOf("bd_stordoc.storcode") >= 0)
        sbSql.append(sbSqlJoinStor);
      sbSql.append(sbSqlJoinBusi);
      sbSql.append(sbSqlJoinProduce);
      sbSql.append(" where v.caccountyear||'-'||v.caccountmonth>'" + ABPeriod + "' ");
      sbSql.append("  and v.cbilltypecode <> 'I0' and  v.cbilltypecode <> 'I1' \n");

      sbDefaultSqlWhere.append("   and fdispatchflag = 0 and  v.cbilltypecode in " + params[0] + " and " + sqlWhere + " \n");

      sbSql.append(sbDefaultSqlWhere);
      sbSql.append("  group by inv.invcode,inv.invname,inv.invspec,inv.invtypes,inv.measname,inv.abctype,bd_produce.abcfundeg ,inv.invplanprice , bd_produce.jhj  \n");

      sbSql.append(" ) ) as datasource ");

      sbSql.append(" group by invcode,invname,invspec,invtypes,measname,abcdoc ,nplanedprice  \n");
      sbSql.append("  order by sum(vmoney) desc ");
    }
    if (flag.equals("2"))
    {
      sbSql.append(" select invcode, invname ,invspec,invtypes ,measname , sum(vnumber), sum(vmoney), abcdoc, nplanedprice \n");
      sbSql.append(" from (");
      sbSql.append(" (select inv.invcode, inv.invname ,inv.invspec,inv.invtypes ,inv.measname , \n");
      sbSql.append(" sum(noutnum) as vnumber, sum(noutmny) as vmoney, ");
      sbSql.append(" (case when count(distinct (v.crdcenterid))>=2 then inv.abctype  else bd_produce.abcfundeg  end) as abcdoc,");
      sbSql.append(" case when  COALESCE(bd_produce.jhj,0)=0 then COALESCE(inv.invplanprice,0) else bd_produce.jhj end as nplanedprice ");
      sbSql.append(" from ia_periodaccount v ");
      sbSql.append(sbSqlJoinInv);

      if (sqlWhere.indexOf("bd_calbody.bodycode") >= 0) {
        sbSql.append(sbSqlJoinCalbody);
      }
      if (sqlWhere.indexOf("bd_rdcl") >= 0) {
        sbSql.append(sbSqlJoinRdcl);
      }
      if (sqlWhere.indexOf("bd_deptdoc.deptcode") >= 0) {
        sbSql.append(sbSqlJoinDept);
      }
      if (sqlWhere.indexOf("bd_stordoc.storcode") >= 0)
        sbSql.append(sbSqlJoinStor);
      sbSql.append(sbSqlJoinProduce);
      sbSql.append(" where v.caccountyear='" + year + "' and v.caccountmonth='" + month + "' and " + sqlWhere + " \n");
      sbSql.append(" group by inv.invcode,inv.invname,inv.invspec,inv.invtypes,inv.measname,inv.abctype,bd_produce.abcfundeg ,inv.invplanprice , bd_produce.jhj \n");
      sbSql.append(" ) union all (");
      sbSql.append("select inv.invcode, inv.invname ,inv.invspec,inv.invtypes ,inv.measname , \n");

      sbSql.append(" SUM( COALESCE(v.nnumber,0)) as vnumber,sum(COALESCE(v.nmoney,0)) as vmoney,(case when count(distinct (v.crdcenterid))>=2 then inv.abctype  else bd_produce.abcfundeg  end) as abcdoc , case when  COALESCE(bd_produce.jhj,0)=0 then COALESCE(inv.invplanprice,0) else bd_produce.jhj end as nplanedprice from  v_ia_inoutledger v \n");

      sbSql.append(sbSqlJoinInv);

      if (sqlWhere.indexOf("bd_calbody.bodycode") >= 0) {
        sbSql.append(sbSqlJoinCalbody);
      }
      if (sqlWhere.indexOf("bd_rdcl") >= 0) {
        sbSql.append(sbSqlJoinRdcl);
      }
      if (sqlWhere.indexOf("bd_deptdoc.deptcode") >= 0) {
        sbSql.append(sbSqlJoinDept);
      }
      if (sqlWhere.indexOf("bd_stordoc.storcode") >= 0)
        sbSql.append(sbSqlJoinStor);
      sbSql.append(sbSqlJoinBusi);
      sbSql.append(sbSqlJoinProduce);
      sbSql.append(" where v.caccountyear||'-'||v.caccountmonth>'" + ABPeriod + "' ");
      sbSql.append(" and v.cbilltypecode <> 'I1' and  v.cbilltypecode<>'I9' \n");
      sbDefaultSqlWhere.append("  and fdispatchflag = 1 and  v.cbilltypecode in " + params[1] + " and " + sqlWhere + " \n");

      sbSql.append(sbDefaultSqlWhere);
      sbSql.append(" and (bd_busitype.verifyrule is null or v.csourcebilltypecode is null or ((bd_busitype.verifyrule <> 'F'  or v.csourcebilltypecode<> '32') and (bd_busitype.verifyrule <> 'W' or v.csourcebilltypecode<> '32'))) \n");

      sbSql.append(" group by inv.invcode,inv.invname,inv.invspec,inv.invtypes,inv.measname,inv.abctype,bd_produce.abcfundeg, inv.invplanprice , bd_produce.jhj  \n");
      sbSql.append(" ) ) as datasource ");

      sbSql.append(" group by invcode,invname,invspec,invtypes,measname,abcdoc ,nplanedprice  \n");
      sbSql.append("  order by sum(vmoney) desc ");
    }

    StringBuffer sbSqlTotal = new StringBuffer();
    if (flag.equals("0"))
    {
      sbSqlTotal.append(" select sum(moneytotal) from (");
      sbSqlTotal.append(" (select sum(nabmny) as moneytotal from ia_periodaccount v ");
      sbSqlTotal.append(sbSqlJoinInv);
      if (sqlWhere.indexOf("bd_calbody.bodycode") >= 0) {
        sbSqlTotal.append(sbSqlJoinCalbody);
      }
      if (sqlWhere.indexOf("bd_rdcl") >= 0) {
        sbSqlTotal.append(sbSqlJoinRdcl);
      }
      if (sqlWhere.indexOf("bd_deptdoc.deptcode") >= 0) {
        sbSqlTotal.append(sbSqlJoinDept);
      }
      if (sqlWhere.indexOf("bd_stordoc.storcode") >= 0) {
        sbSqlTotal.append(sbSqlJoinStor);
      }
      sbSqlTotal.append(" where v.caccountyear='" + year + "' and v.caccountmonth='" + month + "' and " + sqlWhere + " \n");

      sbSqlTotal.append(" ) union all (");

      sbSqlTotal.append(" select sum( case when  v.fdispatchflag = 0 then COALESCE(v.nmoney,0) else 0-COALESCE(v.nmoney,0) end)  as moneytotal from  v_ia_inoutledger v \n");
      sbSqlTotal.append(sbSqlJoinInv);

      if (sqlWhere.indexOf("bd_calbody.bodycode") >= 0) {
        sbSqlTotal.append(sbSqlJoinCalbody);
      }
      if (sqlWhere.indexOf("bd_rdcl") >= 0) {
        sbSqlTotal.append(sbSqlJoinRdcl);
      }
      if (sqlWhere.indexOf("bd_deptdoc.deptcode") >= 0) {
        sbSqlTotal.append(sbSqlJoinDept);
      }
      if (sqlWhere.indexOf("bd_stordoc.storcode") >= 0)
        sbSqlTotal.append(sbSqlJoinStor);
      sbSqlTotal.append(sbSqlJoinBusi);
      sbSqlTotal.append("  where v.cbilltypecode <> 'I1' \n");
      sbSqlTotal.append(" and v.caccountyear||'-'||v.caccountmonth>'" + ABPeriod + "' ");
      sbSqlTotal.append(sbDefaultSqlWhere);

      sbSqlTotal.append(" ))");
    }
    if (flag.equals("1"))
    {
      sbSqlTotal.append(" select sum(moneytotal) from (");
      sbSqlTotal.append(" (select sum(ninmny) as moneytotal from ia_periodaccount v ");
      sbSqlTotal.append(sbSqlJoinInv);
      if (sqlWhere.indexOf("bd_calbody.bodycode") >= 0) {
        sbSqlTotal.append(sbSqlJoinCalbody);
      }
      if (sqlWhere.indexOf("bd_rdcl") >= 0) {
        sbSqlTotal.append(sbSqlJoinRdcl);
      }
      if (sqlWhere.indexOf("bd_deptdoc.deptcode") >= 0) {
        sbSqlTotal.append(sbSqlJoinDept);
      }
      if (sqlWhere.indexOf("bd_stordoc.storcode") >= 0) {
        sbSqlTotal.append(sbSqlJoinStor);
      }
      sbSqlTotal.append(" where v.caccountyear='" + year + "' and v.caccountmonth='" + month + "' and " + sqlWhere + " \n");

      sbSqlTotal.append(" ) union all (");

      sbSqlTotal.append("  select sum(COALESCE(nmoney,0)) as moneytotal from v_ia_inoutledger v \n");

      sbSqlTotal.append(sbSqlJoinInv);

      if (sqlWhere.indexOf("bd_calbody.bodycode") >= 0) {
        sbSqlTotal.append(sbSqlJoinCalbody);
      }
      if (sqlWhere.indexOf("bd_rdcl") >= 0) {
        sbSqlTotal.append(sbSqlJoinRdcl);
      }
      if (sqlWhere.indexOf("bd_deptdoc.deptcode") >= 0) {
        sbSqlTotal.append(sbSqlJoinDept);
      }
      if (sqlWhere.indexOf("bd_stordoc.storcode") >= 0)
        sbSqlTotal.append(sbSqlJoinStor);
      sbSqlTotal.append(sbSqlJoinBusi);
      sbSqlTotal.append(" where  v.cbilltypecode <> 'I1' and v.cbilltypecode <> 'I0' \n");

      sbSqlTotal.append(" and v.caccountyear||'-'||v.caccountmonth>'" + ABPeriod + "' ");
      sbSqlTotal.append(sbDefaultSqlWhere);
      sbSqlTotal.append(" ))");
    }
    if (flag.equals("2")) {
      sbSqlTotal.append(" select sum(moneytotal) from (");
      sbSqlTotal.append(" (select sum(noutmny) as moneytotal from ia_periodaccount v ");
      sbSqlTotal.append(sbSqlJoinInv);
      if (sqlWhere.indexOf("bd_calbody.bodycode") >= 0) {
        sbSqlTotal.append(sbSqlJoinCalbody);
      }
      if (sqlWhere.indexOf("bd_rdcl") >= 0) {
        sbSqlTotal.append(sbSqlJoinRdcl);
      }
      if (sqlWhere.indexOf("bd_deptdoc.deptcode") >= 0) {
        sbSqlTotal.append(sbSqlJoinDept);
      }
      if (sqlWhere.indexOf("bd_stordoc.storcode") >= 0) {
        sbSqlTotal.append(sbSqlJoinStor);
      }
      sbSqlTotal.append(" where v.caccountyear='" + year + "' and v.caccountmonth='" + month + "' and " + sqlWhere + " \n");

      sbSqlTotal.append(" ) union all (");

      sbSqlTotal.append("  select sum(COALESCE(nmoney,0)) as moneytotal from v_ia_inoutledger v \n");

      sbSqlTotal.append(sbSqlJoinInv);

      if (sqlWhere.indexOf("bd_calbody.bodycode") >= 0) {
        sbSqlTotal.append(sbSqlJoinCalbody);
      }
      if (sqlWhere.indexOf("bd_rdcl") >= 0) {
        sbSqlTotal.append(sbSqlJoinRdcl);
      }
      if (sqlWhere.indexOf("bd_deptdoc.deptcode") >= 0) {
        sbSqlTotal.append(sbSqlJoinDept);
      }
      if (sqlWhere.indexOf("bd_stordoc.storcode") >= 0)
        sbSqlTotal.append(sbSqlJoinStor);
      sbSqlTotal.append(sbSqlJoinBusi);
      sbSqlTotal.append(" where  v.cbilltypecode <> 'I1' and v.cbilltypecode <> 'I0' \n");
      sbSqlTotal.append(" and v.caccountyear||'-'||v.caccountmonth>'" + ABPeriod + "' ");
      sbSqlTotal.append(sbDefaultSqlWhere);
      sbSqlTotal.append(" ))");
    }

    sbSqlTotal.append(" as datasource ");

    Connection con = null;
    PreparedStatement stmtabc = null;
    PreparedStatement stmt = null;
    PreparedStatement stmtTotal = null;
    ResultSet rsabc = null;
    ResultSet rs = null;
    Log.info("-------------------------------------------------------------");

    Log.info("得到abc定义  :" + sqlabc);
    Log.info("得到存货信息  :" + sbSql.toString());
    Log.info("得到存货全部  :" + sbSqlTotal.toString());
    Log.info("-------------------------------------------------------------");
    try
    {
      con = getConnection();

      stmtabc = con.prepareStatement(sqlabc);
      rsabc = stmtabc.executeQuery();
      int i = 0;
      int flags = 0;
      while (rsabc.next()) {
        flags = 1;
        abc[i][0] = rsabc.getString(1).trim();
        abc[i][1] = rsabc.getBigDecimal(2);
        abc[i][2] = rsabc.getBigDecimal(3);
        abc[i][3] = rsabc.getBigDecimal(4);
        abc[i][4] = rsabc.getBigDecimal(5);
        ++i;
      }

      if (flags == 0) {
        return result;
      }
      rsabc.close();

      stmtTotal = con.prepareStatement(sbSqlTotal.toString());
      ResultSet rsTotal = stmtTotal.executeQuery();
      Object t2;
      while (rsTotal.next()) {
        t2 = rsTotal.getObject(1);
        money = new UFDouble(Double.parseDouble((t2 == null) ? "0" : t2.toString()));
      }

      rsTotal.close();

      if (money.toString().equals("0")) {
        return null;
      }
      stmt = con.prepareStatement(sbSql.toString());
      rs = stmt.executeQuery();

      Object abcs = "A";

      UFDouble identity = new UFDouble(0);
      UFDouble vp = new UFDouble(0);
      UFDouble monacc = new UFDouble(0);

      while (rs.next()) {
        Object ids = rs.getString(1);
        Object names = rs.getObject(2);
        Object gg = rs.getObject(3);
        Object xh = rs.getObject(4);
        Object jldw = rs.getObject(5);
        UFDouble num = new UFDouble(rs.getDouble(6));
        UFDouble mon = new UFDouble(rs.getDouble(7));
        Object abcdoc = rs.getObject(8);
        UFDouble planprice = new UFDouble(rs.getDouble(9));
        identity = identity.add(1.0D);
        monacc = monacc.add(mon);
        UFDouble monp = monacc.div(money);
        UFDouble planmny = planprice.multiply(num);
        UFDouble mnydiff;
        UFDouble diffrate;
        if (planprice.doubleValue() != 0.0D) {
          mnydiff = planmny.sub(mon);
          diffrate = mnydiff.div(planmny).multiply(100.0D);
        }
        else {
          mnydiff = new UFDouble();
          diffrate = new UFDouble();
        }

        if (monp.toDouble().doubleValue() < 0.0D) {
          monp = monp.div(-1.0D);
        }

        Vector reRow = new Vector();
        reRow.addElement((ids == null) ? "" : ids.toString().trim());
        reRow.addElement(names);
        reRow.addElement(gg);
        reRow.addElement(xh);
        reRow.addElement(jldw);
        reRow.addElement(num);
        reRow.addElement(identity);
        reRow.addElement(vp);
        reRow.addElement(mon);
        reRow.addElement(monacc);
        reRow.addElement(monp);
        reRow.addElement(abcs);
        reRow.addElement((abcdoc == null) ? "" : abcdoc);
        reRow.addElement(planprice);
        reRow.addElement(planmny);
        reRow.addElement(mnydiff);
        reRow.addElement(diffrate);

        result.addElement(reRow.clone());
      }
      for (int k = 0; k < result.size(); ++k) {
        String sTemp = null;
        Vector temp = (Vector)result.elementAt(k);

        UFDouble tempScale1 = ((UFDouble)temp.elementAt(6)).div(identity);

        UFDouble tempScale2 = (UFDouble)temp.elementAt(10);

        int j = 0; if (j < abc.length) {
          if ((tempScale1.toDouble().compareTo(Double.valueOf(abc[j][1].toString())) >= 0) && (tempScale1.toDouble().compareTo(Double.valueOf(abc[j][2].toString())) <= 0) && (tempScale2.toDouble().compareTo(Double.valueOf(abc[j][3].toString())) >= 0) && (tempScale2.toDouble().compareTo(Double.valueOf(abc[j][4].toString())) <= 0))
          {
            sTemp = abc[j][0].toString();
          }
          else
          {
            sTemp = "B1";
          }
        }

        ((Vector)result.elementAt(k)).setElementAt(tempScale1.multiply(100.0D).toString(), 7);

        ((Vector)result.elementAt(k)).setElementAt(tempScale2.multiply(100.0D).toString(), 10);

        ((Vector)result.elementAt(k)).setElementAt(sTemp, 11);
      }
    }
    finally {
      try {
        try {
          if (rsabc != null)
            rsabc.close();
        }
        catch (Exception e)
        {
        }
        if (stmtabc != null) {
          stmtabc.close();
        }
        if (stmtTotal != null) {
          stmtTotal.close();
        }
        if (stmt != null)
          stmt.close();
      }
      catch (Exception e)
      {
      }
      try {
        if (con != null) {
          con.close();
        }
      }
      catch (Exception e)
      {
      }
    }

    afterCallMethod("nc.bs.ia.analyze.IaAnalyseDMO", "getAbc", new Object[] { sqlWhere });

    return result;
  }

  public String getAllsName(String names)
  {
    if (names.equals("inv.invtypecode,inv.invtype")) {
      return "alls.invtypecode,alls.invtype";
    }

    if (names.equals(" case when v_ia_inoutledger.bauditbatchflag = UPPER('y') then v_ia_inoutledger.vbatch else '' end vbatch ")) {
      return "alls.vbatch";
    }

    if (names.equals(" case when v_ia_inoutledger.bauditbatchflag = UPPER('y') then v_ia_inoutledger.vbatch else '' end ")) {
      return "alls.vbatch ";
    }

    if (names.equals("inv.invcode,inv.invname")) {
      return "alls.invcode,alls.invname";
    }

    if (names.equals("bd_stordoc.storcode,bd_stordoc.storname")) {
      return "alls.storcode,alls.storname";
    }

    if (names.equals("bd_deptdoc.deptcode,bd_deptdoc.deptname")) {
      return "alls.deptcode,alls.deptname";
    }

    if (names.equals("bd_calbody.bodycode,bd_calbody.bodyname")) {
      return "alls.bodycode,alls.bodyname";
    }

    if (names.equals("v_ia_inoutledger.caccountyear,v_ia_inoutledger.caccountmonth"))
    {
      return "alls.caccount";
    }

    String begin = "";
    String end = "";

    if (names.indexOf(",") > 1) {
      begin = names.substring(0, names.indexOf(",") + 1);
      end = names.substring(names.indexOf(",") + 1);
      begin = "alls" + begin.substring(begin.indexOf("."));
      end = "alls" + end.substring(end.indexOf("."));
      return begin + end;
    }
    if (names.indexOf("||") > 1) {
      begin = names.substring(0, names.indexOf("||") + 2);
      end = names.substring(names.indexOf("||") + 2);
      begin = "alls" + begin.substring(begin.indexOf("."));
      end = "'  '||alls" + end.substring(end.indexOf("."));
      return begin + end;
    }
    return "alls" + names.substring(names.indexOf("."));
  }

  public Vector getInCost(QueryVO query)
    throws SQLException
  {
    beforeCallMethod("nc.bs.ia.analyze.IaAnalyseDMO", "getInCost", new Object[] { query });

    Vector result = new Vector();
    Vector rsRow = new Vector();
    StatisticsVO[] stat = query.getStatistics();
    int columns = stat.length;
    String sql = "";

    CommonDataImpl cbo = new CommonDataImpl();
    String corp = query.getPk_Corps()[0];
    String[] periods = query.getPeriod();
    String unClosedPeriod = "";
    String lastClosedPeriod = "";
    try
    {
      unClosedPeriod = cbo.getUnClosePeriod(corp);
      lastClosedPeriod = cbo.getPerviousPeriod(corp, unClosedPeriod);
    } catch (BusinessException e1) {
      Log.error(e1);
    }

    if (periods[1].compareToIgnoreCase(unClosedPeriod) < 0)
    {
      sql = sql + getSqlForInCost(query, "ia_monthinout", periods);
    } else if (periods[0].compareToIgnoreCase(unClosedPeriod) >= 0)
    {
      sql = sql + getSqlForInCost(query, "v_ia_inoutledger", periods);
    } else {
      sql = sql + " select ";

      for (int i = 0; i < columns; ++i) {
        String column = stat[i].getFieldCode().trim();
        if (column.equals("caccountyear,caccountmonth".trim())) {
          sql = sql + " caccount ,";
        }
        else {
          sql = sql + " " + getName(column, "getInBill", 2) + ",";
        }
      }
      sql = sql + " sum(ninmny) from ((";
      sql = sql + getSqlForInCost(query, "ia_monthinout", new String[] { periods[0], lastClosedPeriod });
      sql = sql + ") union all (";
      sql = sql + getSqlForInCost(query, "v_ia_inoutledger", new String[] { unClosedPeriod, periods[1] });
      sql = sql + " )) a ";

      String groupSql = "";
      for (int i = 0; i < stat.length; ++i) {
        String group = getName(stat[i].getFieldCode().trim(), "getInBill", 2);
        if (i > 0) {
          groupSql = groupSql + " , " + group;
        }
        else {
          groupSql = groupSql + group;
        }
      }
      sql = sql + " group by " + groupSql;
    }

    sql = sql.replaceAll("invtypeid", "pk_invcl");

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      rs = stmt.executeQuery();
      while (rs.next()) {
        for (int i = 0; i < columns + 1; ++i) {
          Object oTemp = rs.getObject(i + 1);
          if (oTemp != null) {
            rsRow.addElement(oTemp);
          }
          else {
            rsRow.addElement("");
          }
        }
        result.addElement(rsRow.clone());
        rsRow.removeAllElements();
      }
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception e)
      {
        Log.error(e);
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception e)
      {
        Log.error(e);
      }

    }

    afterCallMethod("nc.bs.ia.analyze.IaAnalyseDMO", "getInCost", new Object[] { query });

    return result;
  }

  private String getSqlForInCost(QueryVO query, String tablename, String[] periods) {
    StringBuffer buffer = new StringBuffer();

    StatisticsVO[] stat = query.getStatistics();
    String sWhereSql = query.getWhere();
    String sDataPower = query.getDataPowerSql();

    int columns = stat.length;

    buffer.append("select ");

    for (int i = 0; i < columns; ++i) {
      String column = stat[i].getFieldCode().trim();
      if (column.equals("caccountyear,caccountmonth".trim())) {
        buffer.append(" (" + tablename + ".caccountyear||" + tablename + ".caccountmonth) as caccount ,");
      }
      else {
        buffer.append(getName(column, "getInBill", 0) + ",");
      }
    }

    if (tablename.equalsIgnoreCase("v_ia_inoutledger"))
      buffer.append(" sum(coalesce(nmoney,0)) ninmny from v_ia_inoutledger  ");
    else {
      buffer.append(" sum(coalesce(ninmny,0)) ninmny from ia_monthinout ");
    }

    buffer.append(getJoinSqlForInCost(query, tablename));

    if (tablename.equalsIgnoreCase("v_ia_inoutledger")) {
      buffer.append(" left outer join bd_busitype on v_ia_inoutledger.cbiztypeid = bd_busitype .pk_busitype ");
    }

    buffer.append(" where ");
    buffer.append(tablename + ".caccountyear||'-'||" + tablename + ".caccountmonth>='" + periods[0] + "' ");
    buffer.append(" and " + tablename + ".caccountyear||'-'||" + tablename + ".caccountmonth<='" + periods[1] + "' ");

    if (sWhereSql.trim().length() > 0) {
      buffer.append(" and " + sWhereSql.replaceAll("pk_corp", new StringBuilder().append(tablename).append(".pk_corp").toString()));
    }
    if (sDataPower.trim().length() > 0)
    {
      if (tablename.equalsIgnoreCase("ia_monthinout")) {
        sDataPower = sDataPower.replaceAll("v_ia_inoutledger", "ia_monthinout");
      }

      buffer.append(" and " + sDataPower);
    }

    if (tablename.equalsIgnoreCase("v_ia_inoutledger")) {
      buffer.append(" and v_ia_inoutledger.cbilltypecode <> 'I1' and (v_ia_inoutledger.badjustedItemflag<>'Y' or  v_ia_inoutledger.cbilltypecode<>'I9')");
      buffer.append(" and v_ia_inoutledger.iauditsequence > 0  and v_ia_inoutledger.fdispatchflag = 0 ");
      buffer.append(" and v_ia_inoutledger.cbilltypecode in ('I2','I9','IB','I3','I4','ID','II')");
      buffer.append(" and (bd_busitype.verifyrule is null or v_ia_inoutledger.csourcebilltypecode is null or ((bd_busitype.verifyrule <> 'F'  or v_ia_inoutledger.csourcebilltypecode<> '32') and (bd_busitype.verifyrule <> 'W' or v_ia_inoutledger.csourcebilltypecode<> '32')))");
    }
    else
    {
      buffer.append(" and ia_monthinout.cbilltypecode in ('I2','I9','IB','I3','I4','ID','II')");
    }

    String groupSql = "";
    for (int i = 0; i < stat.length; ++i) {
      String group = getName(stat[i].getFieldCode().trim(), "getInBill", 1);
      if ((tablename.equalsIgnoreCase("ia_monthinout")) && (group.indexOf("v_ia_inoutledger") >= 0))
      {
        group = group.replaceAll("v_ia_inoutledger", "ia_monthinout");
      }
      if (i > 0) {
        groupSql = groupSql + " , " + group;
      }
      else {
        groupSql = groupSql + group;
      }
    }
    buffer.append(" group by " + groupSql);

    return buffer.toString();
  }

  private String getJoinSqlForInCost(QueryVO query, String tablename)
  {
    StringBuffer buffer = new StringBuffer();

    String sqlWhere = query.getWhere();
    String DataPowerSql = query.getDataPowerSql();

    String format = "";
    StatisticsVO[] stat = query.getStatistics();
    for (int i = 0; i < stat.length; ++i) {
      format = format + stat[i].getFieldCode();
    }

    if ((sqlWhere.indexOf("inv.invcode") >= 0) || (sqlWhere.indexOf("inv.invclasscode") >= 0) || (sqlWhere.indexOf("inv.invclasslev") >= 0) || (DataPowerSql.indexOf("inv.pk_invcl") >= 0) || (format.contains("cinventoryid")) || (format.contains("inv.pk_invcl")))
    {
      buffer.append(" inner join (select bd_invmandoc.pk_invmandoc invid,bd_invbasdoc.invcode invcode,bd_invcl.invclasscode invclasscode,bd_invbasdoc.invname invname,bd_invbasdoc.pk_invcl invtypeid,bd_invcl.invclassname invtype,bd_invcl.invclasslev invclasslev,");
      buffer.append(" bd_measdoc.measname measname,bd_invbasdoc.invspec invspec, bd_invbasdoc.invtype invtypes ");
      buffer.append(" from bd_measdoc,bd_invmandoc,bd_invbasdoc inner join bd_invcl on bd_invcl.pk_invcl = bd_invbasdoc.pk_invcl where bd_measdoc.pk_measdoc=bd_invbasdoc.pk_measdoc and bd_invbasdoc.pk_invbasdoc = bd_invmandoc.pk_invbasdoc) inv ");
      buffer.append(" on " + tablename + ".cinventoryid = inv.invid ");
    }
    if ((sqlWhere.indexOf("job.jobcode") >= 0) || (format.contains("cprojectid")))
    {
      buffer.append(" left outer join (select bd_jobmngfil.pk_jobmngfil jobid,bd_jobbasfil.jobname jobname ,bd_jobbasfil.jobcode jobcode");
      buffer.append(" from bd_jobmngfil,bd_jobbasfil where bd_jobbasfil.pk_jobbasfil = bd_jobmngfil.pk_jobbasfil) job ");
      buffer.append(" on " + tablename + ".cprojectid =job.jobid ");
    }
    if ((sqlWhere.indexOf("bd_deptdoc.deptcode") >= 0) || (format.contains("cdeptid")))
    {
      buffer.append(" left outer join bd_deptdoc on " + tablename + ".cdeptid = bd_deptdoc.pk_deptdoc ");
    }
    if ((sqlWhere.indexOf("bd_rdcl") >= 0) || (sqlWhere.indexOf("cdispatchid") >= 0) || (format.contains("cdispatchid")))
    {
      buffer.append(" left outer join bd_rdcl on " + tablename + ".cdispatchid = bd_rdcl.pk_rdcl ");
    }
    if ((sqlWhere.indexOf("bd_calbody.bodycode") >= 0) || (format.contains("crdcenterid")))
    {
      buffer.append(" left outer join bd_calbody on " + tablename + ".crdcenterid = bd_calbody.pk_calbody ");
    }
    if ((sqlWhere.indexOf("bd_stordoc.storcode") >= 0) || (format.contains("cwarehouseid")))
    {
      buffer.append(" left outer join bd_stordoc on " + tablename + ".cwarehouseid = bd_stordoc.pk_stordoc  ");
    }
    if ((sqlWhere.indexOf("bd_psndoc.psncode") >= 0) || (format.contains("cemployeeid")))
    {
      buffer.append(" left outer join bd_psndoc on " + tablename + ".cemployeeid = bd_psndoc.pk_psndoc ");
    }
    if ((sqlWhere.indexOf("cu.custcode ") >= 0) || (format.contains("ccustomvendorid")))
    {
      buffer.append(" left outer join (select bd_cubasdoc.custcode custcode,bd_cubasdoc.custname custname,bd_cumandoc.pk_cumandoc pk_cumandoc ");
      buffer.append(" from bd_cumandoc,bd_cubasdoc where bd_cubasdoc.pk_cubasdoc = bd_cumandoc.pk_cubasdoc) cu ");
      buffer.append(" on " + tablename + ".ccustomvendorid = cu.pk_cumandoc ");
    }

    return buffer.toString();
  }

  private String getName(String code, String flag, int tag)
  {
    if (flag.equals("getInBill"))
    {
      if (code.equals("pk_corp")) {
        if (tag == 0) {
          return "bd_corp.unitcode||'  '||bd_corp.unitname corpid";
        }
        if (tag == 1) {
          return "bd_corp.unitcode,bd_corp.unitname";
        }
        if (tag == 2) {
          return "corpid";
        }

        return "inv.corpid";
      }

      if (code.equals("cdispatchid")) {
        if (tag == 0) {
          return "bd_rdcl.rdcode||'  '||bd_rdcl.rdname rdid";
        }
        if (tag == 1) {
          return "bd_rdcl.rdcode,bd_rdcl.rdname";
        }
        if (tag == 2) {
          return "rdid";
        }

        return "inv.rdid";
      }

      if (code.equals("v_ia_inoutledger.vbatch")) {
        if (tag == 0) {
          return " case when v_ia_inoutledger.bauditbatchflag = UPPER('y') then v_ia_inoutledger.vbatch else '' end vbatch ";
        }
        if (tag == 1) {
          return " case when v_ia_inoutledger.bauditbatchflag = UPPER('y') then v_ia_inoutledger.vbatch else '' end ";
        }
        if (tag == 2) {
          return "vbatch";
        }

        return "inv.vbatch";
      }

      if (code.equals("caccountyear,caccountmonth"))
      {
        if (tag == 0) {
          return "v_ia_inoutledger.caccountyear||v_ia_inoutledger.caccountmonth caccount";
        }

        if (tag == 1) {
          return "v_ia_inoutledger.caccountyear,v_ia_inoutledger.caccountmonth";
        }
        if (tag == 2) {
          return "caccount";
        }

        return "inv.caccount";
      }

      if (code.equals("inv.pk_invcl")) {
        if (tag == 0) {
          return "inv.invclasscode||'  '||inv.invtype claid";
        }
        if (tag == 1) {
          return "inv.invclasscode,inv.invtype";
        }
        if (tag == 2) {
          return "claid";
        }

        return "inv.claid";
      }

      if (code.equals("cinventoryid")) {
        if (tag == 0) {
          return "inv.invcode||'  '||inv.invname invid";
        }
        if (tag == 1) {
          return "inv.invcode,inv.invname";
        }
        if (tag == 2) {
          return "invid";
        }

        return "inv.invid";
      }

      if (code.equals("cwarehouseid")) {
        if (tag == 0) {
          return "bd_stordoc.storname storid";
        }
        if (tag == 1) {
          return "bd_stordoc.storname";
        }
        if (tag == 2) {
          return "storid";
        }

        return "inv.storid";
      }

      if (code.equals("cdeptid")) {
        if (tag == 0) {
          return "bd_deptdoc.deptcode||'  '||bd_deptdoc.deptname deptid";
        }
        if (tag == 1) {
          return "bd_deptdoc.deptcode,bd_deptdoc.deptname";
        }
        if (tag == 2) {
          return "deptid";
        }

        return "inv.deptid";
      }

      if (code.equals("cemployeeid")) {
        if (tag == 0) {
          return "bd_psndoc.psncode||'  '||bd_psndoc.psnname psnid";
        }
        if (tag == 1) {
          return "bd_psndoc.psncode,bd_psndoc.psnname";
        }
        if (tag == 2) {
          return "psnid";
        }

        return "inv.psnid";
      }

      if (code.equals("cagentid")) {
        if (tag == 0) {
          return "bd_psndoc.psncode||'  '||bd_psndoc.psnname psnid";
        }
        if (tag == 1) {
          return "bd_psndoc.psncode,bd_psndoc.psnname";
        }
        if (tag == 2) {
          return "psnid";
        }

        return "inv.psnid";
      }

      if (code.equals("ccustomvendorid")) {
        if (tag == 0) {
          return "cu.custcode||'  '||cu.custname custid";
        }
        if (tag == 1) {
          return "cu.custcode,cu.custname";
        }
        if (tag == 2) {
          return "custid";
        }

        return "inv.custid";
      }

      if (code.equals("crdcenterid")) {
        if (tag == 0) {
          return "bd_calbody.bodycode||'  '||bd_calbody.bodyname bodyid";
        }
        if (tag == 1) {
          return "bd_calbody.bodycode,bd_calbody.bodyname";
        }
        if (tag == 2) {
          return "bodyid";
        }

        return "inv.bodyid";
      }

      if (code.equals("cprojectid")) {
        if (tag == 0) {
          return "job.jobcode||'  '||job.jobname jobid";
        }
        if (tag == 1) {
          return "job.jobcode,job.jobname";
        }
        if (tag == 2) {
          return "jobid";
        }

        return "inv.jobid";
      }

      if (code.equals("dbilldate")) {
        if (tag == 0) {
          return "v_ia_inoutledger.dbilldate dateid";
        }
        if (tag == 1) {
          return "v_ia_inoutledger.dbilldate";
        }

        return "inv.dateid";
      }

      if (code.equals("v_ia_inoutledger.vbillcode")) {
        if (tag == 0) {
          return "v_ia_inoutledger.vbillcode codeid";
        }
        if (tag == 1) {
          return "v_ia_inoutledger.vbillcode";
        }

        return "inv.codeid";
      }

      if (code.equals("vbomcode")) {
        if (tag == 0) {
          return "cb.bomcode||'  '||cb.bomname bomid";
        }
        if (tag == 1) {
          return "cb.bomcode,cb.bomname";
        }

        return "inv.bomid";
      }

    }
    else if (flag.equals("getInOutBill"))
    {
      if (code.equals("inv.invtype")) {
        if (tag == 0) {
          return "v.cinventoryid,case when v.bauditbatchflag = 'Y' then v.vbatch else null end vbatch";
        }
        if (tag == 1) {
          return "t.cinventoryid,t.vbatch";
        }
        if (tag == 2) {
          return "alls.pk_invcl,alls.cinventoryid,alls.vbatch";
        }

      }
      else if (code.equals("v.cinventoryid")) {
        if (tag == 0) {
          return "v.cinventoryid,case when v.bauditbatchflag = 'Y' then v.vbatch else null end vbatch";
        }
        if (tag == 1) {
          return "t.cinventoryid,t.vbatch";
        }
        if (tag == 2) {
          return "alls.cinventoryid,alls.vbatch";
        }

      }
      else if (code.equals("v.cwarehouseid")) {
        if (tag == 0) {
          return "v.cwarehouseid";
        }
        if (tag == 1) {
          return "t.cwarehouseid";
        }
        if (tag == 2) {
          return "alls.cwarehouseid";
        }

      }
      else if (code.equals("v.pk_corp")) {
        if (tag == 0) {
          return "v.pk_corp";
        }
        if (tag == 1) {
          return "t.pk_corp";
        }
        if (tag == 2) {
          return "v.pk_corp";
        }

      }
      else if (code.equals("v.cdeptid")) {
        if (tag == 0) {
          return "v.cdeptid";
        }
        if (tag == 1) {
          return "t.cdeptid";
        }
        if (tag == 2) {
          return "v.cdeptid";
        }

      }
      else if (code.equals("v.cemployeeid")) {
        if (tag == 0) {
          return "v.cemployeeid";
        }
        if (tag == 1) {
          return "t.cemployeeid";
        }
        if (tag == 2) {
          return "v.cemployeeid";
        }

      }
      else if (code.equals("v.crdcenterid")) {
        if (tag == 0) {
          return "v.crdcenterid";
        }
        if (tag == 1) {
          return "t.crdcenterid";
        }
        if (tag == 2) {
          return "alls.crdcenterid";
        }
      }
    }

    return code;
  }

  public Vector getRdcl(String sql)
    throws SQLException
  {
    beforeCallMethod("nc.bs.ia.analyze.IaAnalyseDMO", "getRdcl", new Object[] { sql });

    Vector result = new Vector();
    String sqls = "select pk_rdcl,pk_corp,rdflag,rdcode,rdname,pk_frdcl from bd_rdcl  where pk_corp = '0001' or pk_corp = '" + sql + "' order by pk_frdcl, pk_rdcl ";

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sqls);
      rs = stmt.executeQuery();
      while (rs.next()) {
        Object[] temp = new Object[6];
        for (int i = 0; i < 6; ++i) {
          Object oTemp = rs.getObject(i + 1);
          temp[i] = ((oTemp == null) ? "" : oTemp);
        }
        result.addElement(temp);
      }
    }
    finally {
      try {
        if (rs != null)
          rs.close();
      }
      catch (Exception e)
      {
      }
      try
      {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception e)
      {
      }
      try {
        if (con != null) {
          con.close();
        }
      }
      catch (Exception e)
      {
      }
    }

    afterCallMethod("nc.bs.ia.analyze.IaAnalyseDMO", "getRdcl", new Object[] { sql });

    return result;
  }

  public Vector getRdclLevel(Vector oTemp)
  {
    Vector temp = (Vector)oTemp.clone();
    Vector result = new Vector();
    Vector rsLevel = new Vector();
    Vector levelStr = new Vector();
    Vector levelStr2 = new Vector();
    levelStr.addElement("");
    while (rsLevel.size() > 0) {
      levelStr2.removeAllElements();
      rsLevel.removeAllElements();
      for (int i = 0; i < temp.size(); ++i) {
        Object[] te = (Object[])(Object[])temp.elementAt(i);
        for (int j = 0; j < levelStr.size(); ++j) {
          if (!(te[5].toString().trim().equals(((String)levelStr.elementAt(j)).toString().trim())))
            continue;
          rsLevel.addElement(temp.elementAt(i));
          levelStr2.addElement(te[0]);
          temp.remove(i);
          break;
        }
      }

      levelStr = (Vector)levelStr2.clone();
      result.addElement(rsLevel);
    }
    return result;
  }

  public Vector getStordoc(String sql)
    throws SQLException
  {
    beforeCallMethod("nc.bs.ia.analyze.IaAnalyseDMO", "getStordocl", new Object[] { sql });

    Vector result = new Vector();
    Vector rsRow = new Vector();
    String sqls = "select storcode,storname,pk_stordoc from bd_stordoc";
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(sqls);
      rs = stmt.executeQuery();
      Object temp = null;
      while (rs.next()) {
        rsRow.addElement(Boolean.FALSE);
        temp = rs.getObject(1);
        if (temp != null) {
          rsRow.addElement(temp);
        }
        else {
          rsRow.addElement(null);
        }
        temp = rs.getObject(2);
        if (temp != null) {
          rsRow.addElement(temp);
        }
        else {
          rsRow.addElement(null);
        }
        temp = rs.getObject(3);
        if (temp != null) {
          rsRow.addElement(temp);
        }
        else {
          rsRow.addElement(null);
        }
        result.addElement(rsRow.clone());
        rsRow.removeAllElements();
      }
    }
    finally {
      try {
        if (rs != null)
          rs.close();
      }
      catch (Exception e)
      {
      }
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception e) {
      }
      try {
        if (con != null) {
          con.close();
        }
      }
      catch (Exception e)
      {
      }
    }

    afterCallMethod("nc.bs.ia.analyze.IaAnalyseDMO", "ggetStordocl", new Object[] { sql });

    return result;
  }

  public InvInOutSumVO[] getInOutSum(QueryVO voQueryCnd)
    throws SQLException, BusinessException
  {
    beforeCallMethod("nc.bs.ia.analyze.IaAnalyseDMO", "getInOutSum", new Object[] { voQueryCnd });

    TempTableDefine def = new TempTableDefine();

    InvInOutSumVO[] vosResult = null;

    ReportAlgoController con = new ReportAlgoController();

    IReportAlgo algo = con.getReportAlgo(QueryVO.INOUTSUM);

    String temptable = def.getPeriodAccountTable(new String[0][0]);

    vosResult = algo.exceReportAlgoLineReturnVOS(voQueryCnd, temptable);

    afterCallMethod("nc.bs.ia.analyze.IaAnalyseDMO", "getInOutSum", new Object[] { voQueryCnd });

    return vosResult;
  }

  public InvInOutSumVO[] getDiffAlloc(QueryVO qvo)
    throws Exception
  {
    beforeCallMethod("nc.bs.ia.analyze.IaAnalyseDMO", "getDiffAlloc", new Object[] { qvo });

    String sqlWhere = qvo.getWhere();
    String sql = "select ";

    sql = sql + " v.caccountyear,v.caccountmonth,v.crdcenterid,v.cwarehouseid,v.cbiztypeid,v.ccustomvendorid, v.cdeptid,v.cinventoryid,v.cprojectid, v.cdispatchid, coalesce(nplanedmny,0),coalesce(noutvarymny,0)  from v_ia_inoutledger v ";

    if ((sqlWhere.indexOf("bd_invbasdoc.") >= 0) && (sqlWhere.indexOf("bd_invcl.") >= 0))
    {
      sql = sql + " inner join bd_invmandoc on bd_invmandoc.pk_invmandoc = v.cinventoryid ";

      sql = sql + " inner join bd_invbasdoc on bd_invmandoc.pk_invbasdoc = bd_invbasdoc.pk_invbasdoc ";
      sql = sql + " inner join bd_invcl on bd_invcl.pk_invcl = bd_invbasdoc.pk_invcl ";
    }
    else if (sqlWhere.indexOf("bd_invbasdoc.") >= 0) {
      sql = sql + " inner join bd_invmandoc on bd_invmandoc.pk_invmandoc = v.cinventoryid ";

      sql = sql + " inner join bd_invbasdoc on bd_invmandoc.pk_invbasdoc = bd_invbasdoc.pk_invbasdoc ";
    }
    else if (sqlWhere.indexOf("bd_invcl.") >= 0) {
      sql = sql + " inner join bd_invmandoc on bd_invmandoc.pk_invmandoc = v.cinventoryid ";

      sql = sql + " inner join bd_invbasdoc on bd_invmandoc.pk_invbasdoc = bd_invbasdoc.pk_invbasdoc ";
      sql = sql + " inner join bd_invcl on bd_invcl.pk_invcl = bd_invbasdoc.pk_invcl ";
    }

    if (sqlWhere.indexOf("bd_jobbasfil.jobcode") >= 0) {
      sql = sql + " left outer join bd_jobmngfil on bd_jobmngfil.pk_jobmngfil = v.cprojectid ";

      sql = sql + " left outer join bd_jobbasfil on bd_jobmngfil.pk_jobbasfil = bd_jobbasfil.pk_jobbasfil ";
    }

    if (sqlWhere.indexOf("bd_deptdoc.deptcode") >= 0) {
      sql = sql + " left outer join bd_deptdoc on v.cdeptid = bd_deptdoc.pk_deptdoc ";
    }

    if ((sqlWhere.indexOf("bd_rdcl") >= 0) || (sqlWhere.indexOf("v.cdispatchid") >= 0))
    {
      sql = sql + " left outer join bd_rdcl on v.cdispatchid = bd_rdcl.pk_rdcl ";
    }

    if (sqlWhere.indexOf("bd_calbody.bodycode") >= 0) {
      sql = sql + " left outer join bd_calbody on v.crdcenterid = bd_calbody.pk_calbody ";
    }

    if (sqlWhere.indexOf("bd_stordoc.storcode") >= 0) {
      sql = sql + " left outer join bd_stordoc on v.cwarehouseid = bd_stordoc.pk_stordoc  ";
    }

    if (sqlWhere.indexOf("cu.custcode ") >= 0) {
      sql = sql + " left outer join bd_cumandoc on bd_cumandoc.pk_cumandoc = v.ccustomvendorid ";

      sql = sql + " left outer join bd_cubasdoc cu on cu.pk_cubasdoc = bd_cumandoc.pk_cubasdoc ";
    }

    sql = sql + " left outer join bd_busitype on v.cbiztypeid = bd_busitype.pk_busitype ";

    sql = sql + " left outer join bd_produce on bd_produce.pk_calbody = v.crdcenterid and bd_produce.pk_invmandoc = v.cinventoryid ";

    sql = sql + " where  v.cbilltypecode <> 'I1' and (v.badjustedItemflag<>'Y' or  v.cbilltypecode<>'I9')  and iauditsequence > 0 and v.fdispatchflag = 1 and bd_produce.pricemethod = 5 and  v.cbilltypecode in (" + QueryVO.getOutbillType() + ") and (bd_busitype.verifyrule is null or v.csourcebilltypecode is null or ((bd_busitype.verifyrule <> 'F'  or v.csourcebilltypecode<> '" + "32" + "') and (bd_busitype.verifyrule <> 'W' or v.csourcebilltypecode<> '" + "32" + "')))";

    if ((sqlWhere != null) && (sqlWhere.trim().length() > 0)) {
      sql = sql + " and " + sqlWhere;
    }

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    ArrayList vResult = new ArrayList();
    InvInOutSumVO[] vosResult = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      rs = stmt.executeQuery();
      while (rs.next()) {
        InvInOutSumVO invinoutsumvo = new InvInOutSumVO();
        int index = 0;

        String caccountmonth = rs.getString(++index);
        invinoutsumvo.setCaccountmonth(caccountmonth);

        String caccountyear = rs.getString(++index);
        invinoutsumvo.setCaccountyear(caccountyear);

        String rdcenterid = rs.getString(++index);
        invinoutsumvo.setCrdcenterid((rdcenterid == null) ? null : rdcenterid.trim());

        String warehouseid = rs.getString(++index);
        invinoutsumvo.setCwarehouseid((warehouseid == null) ? null : warehouseid.trim());

        String biztypeid = rs.getString(++index);
        invinoutsumvo.setCbiztypeid((biztypeid == null) ? null : biztypeid.trim());

        String customvendorid = rs.getString(++index);
        invinoutsumvo.setCcustomvendorid((customvendorid == null) ? null : customvendorid.trim());

        String deptid = rs.getString(++index);
        invinoutsumvo.setCdeptid((deptid == null) ? null : deptid.trim());

        String inventoryid = rs.getString(++index);
        invinoutsumvo.setCinventoryid((inventoryid == null) ? null : inventoryid.trim());

        String projectid = rs.getString(++index);
        invinoutsumvo.setCprojectid((projectid == null) ? null : projectid.trim());

        String dispatchid = rs.getString(++index);
        invinoutsumvo.setCdispatchid((dispatchid == null) ? null : dispatchid.trim());

        BigDecimal nplanedmny = rs.getBigDecimal(++index);
        invinoutsumvo.setNoutplanedmny(new UFDouble(nplanedmny));

        BigDecimal noutvarymny = rs.getBigDecimal(++index);
        invinoutsumvo.setNoutvarymny(new UFDouble(noutvarymny));

        vResult.add(invinoutsumvo);
      }
      vosResult = new InvInOutSumVO[vResult.size()];
      for (int i = 0; i < vResult.size(); ++i) {
        vosResult[i] = new InvInOutSumVO();
        vosResult[i] = ((InvInOutSumVO)vResult.get(i));
      }

      CommonDataDMO.objectReference(vosResult);
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception e)
      {
      }
      try {
        if (con != null) {
          con.close();
        }
      }
      catch (Exception e)
      {
      }
    }

    afterCallMethod("nc.bs.ia.analyze.IaAnalyseDMO", "getInBill", new Object[] { sqlWhere });

    return vosResult;
  }

  public InvInOutSumVO[] getInBill(QueryVO conVO)
    throws Exception
  {
    beforeCallMethod("nc.bs.ia.analyze.IaAnalyseDMO", "getInBill", new Object[] { conVO });

    TempTableDefine def = new TempTableDefine();

    InvInOutSumVO[] vosResult = null;

    ReportAlgoController con = new ReportAlgoController();

    IReportAlgo algo = con.getReportAlgo(QueryVO.INBILL);

    String temptable = def.getPeriodAccountTable(new String[0][0]);

    vosResult = algo.exceReportAlgoLineReturnVOS(conVO, temptable);

    afterCallMethod("nc.bs.ia.analyze.IaAnalyseDMO", "getInBill", new Object[] { conVO });

    return vosResult;
  }

  public InvInOutSumVO[] getOutBill(QueryVO conVO)
    throws Exception
  {
    beforeCallMethod("nc.bs.iv.analyze.IaAnalyseDMO", "getInBill", new Object[] { conVO });

    TempTableDefine def = new TempTableDefine();

    InvInOutSumVO[] vosResult = null;

    ReportAlgoController con = new ReportAlgoController();

    IReportAlgo algo = con.getReportAlgo(QueryVO.OUTBILL);

    String temptable = def.getPeriodAccountTable(new String[0][0]);

    vosResult = algo.exceReportAlgoLineReturnVOS(conVO, temptable);

    afterCallMethod("nc.bs.ia.analyze.IaAnalyseDMO", "getInBill", new Object[] { conVO });

    return vosResult;
  }

  public Vector getStockCapital(String sqlWhere, Integer iflag, String xsfp)
    throws SQLException
  {
    beforeCallMethod("nc.bs.ia.analyze.IaAnalyseDMO", "getStockCapital", new Object[] { sqlWhere });

    Vector result = new Vector();
    Vector rsRow = new Vector();
    int flag = iflag.intValue();
    int col = 0;

    String sql = "";
    String sqlGroup = "";
    String sqlJoin = "";
    if (flag == 0) {
      col = 3;
      sql = " select bd_calbody.bodycode,bd_calbody.bodyname,sum(coalesce(ia_generalledger.nabmny,0)) as num ";
      sqlGroup = " group by  bd_calbody.bodycode,bd_calbody.bodyname  ";
      sql = sql + " from ia_generalledger left outer join (select bd_invmandoc.pk_invmandoc invid,bd_invbasdoc.invcode invcode,bd_invcl.invclasscode invclasscode,bd_invbasdoc.invname invname,bd_invbasdoc.pk_invcl invtypeid,bd_invcl.invclassname invtype,bd_invcl.invclasscode invtypecode,bd_invcl.invclasslev invclasslev ";
      sql = sql + " from bd_invmandoc,bd_invbasdoc left outer join bd_invcl on bd_invcl.pk_invcl = bd_invbasdoc.pk_invcl where bd_invbasdoc.pk_invbasdoc = bd_invmandoc.pk_invbasdoc) inv ";
      sql = sql + " on ia_generalledger.cinventoryid = inv.invid ";
      sql = sql + " left outer join bd_calbody on ia_generalledger.crdcenterid = bd_calbody.pk_calbody ";
      sql = sql + " where   ia_generalledger.frecordtypeflag=4 and " + sqlWhere + sqlGroup;
    }

    if (flag == 1) {
      col = 5;
      sql = "select bd_calbody.bodycode,bd_calbody.bodyname,inv.invcode,inv.invname ";
      sqlGroup = " group by   bd_calbody.bodycode,bd_calbody.bodyname,inv.invcode,inv.invname ";

      sql = sql + " ,sum(coalesce(ia_generalledger.nabmny,0))  ";
      sql = sql + " from ia_generalledger ";
      sql = sql + " left outer join (select bd_invmandoc.pk_invmandoc invid,bd_invbasdoc.invcode invcode,bd_invcl.invclasscode invclasscode,bd_invbasdoc.invname invname,bd_invbasdoc.pk_invcl invtypeid,bd_invcl.invclassname invtype,bd_invcl.invclasscode invtypecode,bd_invcl.invclasslev invclasslev ";
      sql = sql + " from bd_invmandoc,bd_invbasdoc left outer join bd_invcl on bd_invcl.pk_invcl = bd_invbasdoc.pk_invcl where bd_invbasdoc.pk_invbasdoc = bd_invmandoc.pk_invbasdoc) inv ";
      sql = sql + " on ia_generalledger.cinventoryid = inv.invid ";
      sql = sql + " left outer join bd_calbody on ia_generalledger.crdcenterid = bd_calbody.pk_calbody ";
      sql = sql + sqlJoin + " where   ia_generalledger.frecordtypeflag=4 and " + sqlWhere + sqlGroup;
    }

    if (flag == 2) {
      col = 5;
      sql = "select bd_calbody.bodycode,bd_calbody.bodyname ,inv.invtypecode,inv.invtype,sum(coalesce(ia_generalledger.nabmny,0)) as num  ";
      sqlGroup = " group by bd_calbody.bodycode,bd_calbody.bodyname ,inv.invtypecode,inv.invtype ";
      sql = sql + " from ia_generalledger left outer join (select bd_invmandoc.pk_invmandoc invid,bd_invbasdoc.invcode invcode,bd_invcl.invclasscode invclasscode,bd_invbasdoc.invname invname,bd_invbasdoc.pk_invcl invtypeid,bd_invcl.invclassname invtype,bd_invcl.invclasscode invtypecode,bd_invcl.invclasslev invclasslev ";
      sql = sql + " from bd_invmandoc,bd_invbasdoc left outer join bd_invcl on bd_invcl.pk_invcl = bd_invbasdoc.pk_invcl where bd_invbasdoc.pk_invbasdoc = bd_invmandoc.pk_invbasdoc) inv ";
      sql = sql + " on ia_generalledger.cinventoryid = inv.invid ";
      sql = sql + " left outer join bd_calbody on ia_generalledger.crdcenterid = bd_calbody.pk_calbody ";
      sql = sql + " where   ia_generalledger.frecordtypeflag=4 and " + sqlWhere + sqlGroup;
    }
    if (flag == 3) {
      col = 3;
      sql = "select inv.invcode,inv.invname, ";
      sqlGroup = " group by inv.invcode,inv.invname ";
      sql = sql + " sum(coalesce(ia_generalledger.nabmny,0))  ";
      sql = sql + " from ia_generalledger ";
      sql = sql + " left outer join (select bd_invmandoc.pk_invmandoc invid,bd_invbasdoc.invcode invcode,bd_invcl.invclasscode invclasscode,bd_invbasdoc.invname invname,bd_invbasdoc.pk_invcl invtypeid,bd_invcl.invclassname invtype,bd_invcl.invclasscode invtypecode,bd_invcl.invclasslev invclasslev ";
      sql = sql + " from bd_invmandoc,bd_invbasdoc left outer join bd_invcl on bd_invcl.pk_invcl = bd_invbasdoc.pk_invcl where bd_invbasdoc.pk_invbasdoc = bd_invmandoc.pk_invbasdoc) inv ";
      sql = sql + " on ia_generalledger.cinventoryid = inv.invid ";
      sql = sql + " left outer join bd_calbody on ia_generalledger.crdcenterid = bd_calbody.pk_calbody ";
      sql = sql + " where   ia_generalledger.frecordtypeflag=4 and " + sqlWhere + sqlGroup;
    }

    if (flag == 4) {
      col = 3;
      sql = " select inv.invtypecode,inv.invtype,sum(coalesce(ia_generalledger.nabmny,0)) as num ";
      sqlGroup = " group by inv.invtypecode,inv.invtype  ";
      sql = sql + " from ia_generalledger left outer join (select bd_invmandoc.pk_invmandoc invid,bd_invbasdoc.invcode invcode,bd_invcl.invclasscode invclasscode,bd_invbasdoc.invname invname,bd_invbasdoc.pk_invcl invtypeid,bd_invcl.invclassname invtype,bd_invcl.invclasscode invtypecode,bd_invcl.invclasslev invclasslev ";
      sql = sql + " from bd_invmandoc,bd_invbasdoc left outer join bd_invcl on bd_invcl.pk_invcl = bd_invbasdoc.pk_invcl where bd_invbasdoc.pk_invbasdoc = bd_invmandoc.pk_invbasdoc) inv ";
      sql = sql + " on ia_generalledger.cinventoryid = inv.invid ";
      sql = sql + " left outer join bd_calbody on ia_generalledger.crdcenterid = bd_calbody.pk_calbody ";
      sql = sql + " where   ia_generalledger.frecordtypeflag=4 and " + sqlWhere + sqlGroup;
    }

    if (sql.indexOf("caccountmonth") > 0) {
      sql = sql.replaceAll("ia_generalledger", "ia_monthledger");
      sql = sql.replaceAll("frecordtypeflag=4", "frecordtypeflag=3");
    }

    sql = sql.replaceAll("inv.pk_invcl", "inv.invtypeid");
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      rs = stmt.executeQuery();
      while (rs.next()) {
        for (int i = 0; i < col; ++i) {
          Object oTemp = rs.getObject(i + 1);
          if (oTemp != null)
            rsRow.addElement(oTemp);
          else
            rsRow.addElement(null);
        }
        result.addElement(rsRow.clone());
        rsRow.removeAllElements();
      }
    }
    finally {
      try {
        if (rs != null)
          rs.close();
      }
      catch (Exception e)
      {
      }
      try
      {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception e)
      {
      }
      try {
        if (con != null) {
          con.close();
        }
      }
      catch (Exception e)
      {
      }
    }
    afterCallMethod("nc.bs.ia.analyze.IaAnalyseDMO", "getStockCapital", new Object[] { sqlWhere });

    return result;
  }

  public VelocityVO[] getVelocity(String sqlWhere, String[] param, String xsfp, StatisticsVO[] voStatistics)
    throws SQLException
  {
    beforeCallMethod("nc.bs.ia.analyze.IaAnalyseDMO", "getVelocity", new Object[] { sqlWhere });

    UFDate beginDate = new UFDate(param[3], false);
    UFDate endDate = new UFDate(param[4], false);
    String pk_corp = param[6];
    VelocityVO[] result = null;
    ArrayList resultList = null;

    String sField = "";
    String sAllString = "";
    if (voStatistics != null) {
      for (int i = 0; i < voStatistics.length; ++i) {
        sAllString = sAllString + "," + voStatistics[i].getFieldCode();
      }
    }

    ArrayList al = new ArrayList();

    if (sAllString.indexOf("invname") != -1) {
      sField = sField + "cinventoryid,";
      al.add("cinventoryid");
    }
    if ((sAllString.indexOf("invclassname") != -1) && (sField.indexOf("cinventoryid") == -1))
    {
      sField = sField + "cinventoryid,";
      al.add("cinventoryid");
    }
    if (sAllString.indexOf("bodyname") != -1) {
      sField = sField + "crdcenterid,";
      al.add("crdcenterid");
    }

    String[] forBegin = null;
    String[] forInOut = null;

    boolean ifdef = false;

    if (sqlWhere.indexOf("def") > 0) {
      ifdef = true;
    }
    try
    {
      forBegin = paraBeginPeriod(pk_corp, beginDate, ifdef);
      forInOut = paraInOutPeriod(pk_corp, beginDate, endDate, ifdef);
    } catch (BusinessException e1) {
      e1.printStackTrace();
    }

    StringBuffer innerSql = new StringBuffer(0);
    String strField;
    String joinSql;
    if (forBegin != null) {
      if (forBegin[0] != null) {
        strField = getFieldStr(sAllString, "ab");
        innerSql.append(" (select " + strField);
        innerSql.append(" sum(coalesce(ninmny,0)) as beginninmny, sum(coalesce(noutmny,0)) as beginnoutmny, ");
        innerSql.append(" 0 as ninmny, 0 as noutmny ");
        innerSql.append(" from ia_periodaccount ab ");
        joinSql = getJoinSqlForVelocity(sqlWhere, "ab");
        if (joinSql.trim().length() > 0) {
          innerSql.append(joinSql);
        }
        innerSql.append(" where " + forBegin[0] + " and ab.pk_corp='" + pk_corp + "' ");
        if (sqlWhere.trim().length() > 0) {
          innerSql.append(" and " + sqlWhere.replaceAll("crdcenterid", "ab.crdcenterid").replaceAll("cinventoryid", "ab.cinventoryid"));
        }

        innerSql.append(" group by " + strField.substring(0, strField.length() - 1) + ")");
      }

      if (forBegin[1] != null) {
        if (innerSql.length() > 0) {
          innerSql.append(" union all ");
        }
        strField = getFieldStr(sAllString, "m");
        innerSql.append(" (select " + strField);
        innerSql.append(" sum(coalesce(ninmny,0)) as beginninmny, sum(coalesce(noutmny,0)) as beginnoutmny, ");
        innerSql.append(" 0 as ninmny, 0 as noutmny ");
        innerSql.append(" from ia_monthinout m ");
        joinSql = getJoinSqlForVelocity(sqlWhere, "m");
        if (joinSql.trim().length() > 0) {
          innerSql.append(joinSql);
        }
        innerSql.append(" where " + forBegin[1] + " and m.pk_corp='" + pk_corp + "' ");
        if (sqlWhere.trim().length() > 0) {
          innerSql.append(" and " + sqlWhere.replaceAll("crdcenterid", "m.crdcenterid").replaceAll("cinventoryid", "m.cinventoryid"));
        }

        innerSql.append(" group by " + strField.substring(0, strField.length() - 1) + ")");
      }

      if (forBegin[2] != null) {
        if (innerSql.length() > 0)
          innerSql.append(" union all ");
        strField = getFieldStr(sAllString, "v");
        innerSql.append(" (select " + strField);
        innerSql.append(" sum(case when v.fdispatchflag=0 then coalesce(v.nmoney, 0) else 0 end) as beginninmny, ");
        innerSql.append(" sum(case when v.fdispatchflag=1 then coalesce(v.nmoney, 0) else 0 end) as beginnoutmny, ");
        innerSql.append(" 0 as ninmny, 0 as noutmny ");
        innerSql.append(" from v_ia_inoutledger v ");
        innerSql.append(" left outer join bd_busitype on v.cbiztypeid = bd_busitype.pk_busitype ");
        joinSql = getJoinSqlForVelocity(sqlWhere, "v");
        if (joinSql.trim().length() > 0) {
          innerSql.append(joinSql);
        }
        innerSql.append(" where v.cbilltypecode not in ('I1','IE','IF','IG','IH') ");
        innerSql.append(" and (v.badjustedItemflag<>'Y' or v.cbilltypecode<>'I9') ");
        innerSql.append(" and " + forBegin[2] + " and v.pk_corp='" + pk_corp + "' and v.iauditsequence>0 ");
        if (sqlWhere.trim().length() > 0) {
          innerSql.append(" and " + sqlWhere.replaceAll("crdcenterid", "v.crdcenterid").replaceAll("cinventoryid", "v.cinventoryid"));
        }

        innerSql.append(" and (bd_busitype.verifyrule is null or v.csourcebilltypecode is null ");
        innerSql.append(" or ((bd_busitype.verifyrule <> 'F'  or v.csourcebilltypecode<> '32");
        innerSql.append("') and (bd_busitype.verifyrule <> 'W' or v.csourcebilltypecode<> '32')))");
        innerSql.append(" group by " + strField.substring(0, strField.length() - 1) + ")");
      }
    }

    if (forInOut != null)
    {
      if (forInOut[0] != null) {
        innerSql.append(" union all ");
        strField = getFieldStr(sAllString, "m");
        innerSql.append(" (select " + strField);
        innerSql.append(" 0 as beginninmny, 0 as beginnoutmny, ");
        innerSql.append(" sum(coalesce(ninmny,0)) as ninmny, sum(coalesce(noutmny,0)) as noutmny ");
        innerSql.append(" from ia_monthinout m ");
        joinSql = getJoinSqlForVelocity(sqlWhere, "m");
        if (joinSql.trim().length() > 0) {
          innerSql.append(joinSql);
        }
        innerSql.append(" where " + forInOut[0] + " and m.pk_corp='" + pk_corp + "' ");
        if (sqlWhere.trim().length() > 0) {
          innerSql.append(" and " + sqlWhere.replaceAll("crdcenterid", "m.crdcenterid").replaceAll("cinventoryid", "m.cinventoryid"));
        }

        innerSql.append(" group by " + strField.substring(0, strField.length() - 1) + ")");
      }

      if (forInOut[1] != null) {
        if (innerSql.length() > 0)
          innerSql.append(" union all ");
        strField = getFieldStr(sAllString, "v");
        innerSql.append(" (select " + strField);
        innerSql.append(" 0 as beginninmny, 0 as beginnoutmny,");
        innerSql.append(" sum(case when v.fdispatchflag=0 then coalesce(v.nmoney, 0) else 0 end ) as ninmny, ");
        innerSql.append(" sum(case when v.fdispatchflag=1 then coalesce(v.nmoney, 0) else 0 end ) as noutmny ");
        innerSql.append(" from v_ia_inoutledger v ");
        innerSql.append(" left outer join bd_busitype on v.cbiztypeid = bd_busitype.pk_busitype ");
        joinSql = getJoinSqlForVelocity(sqlWhere, "v");
        if (joinSql.trim().length() > 0) {
          innerSql.append(joinSql);
        }
        innerSql.append(" where v.cbilltypecode not in ('I0','I1','IE','IF','IG','IH') ");
        innerSql.append(" and (v.badjustedItemflag<>'Y' or v.cbilltypecode<>'I9') ");
        innerSql.append(" and " + forInOut[1] + " and v.pk_corp='" + pk_corp + "' and v.iauditsequence>0 ");
        if (sqlWhere.trim().length() > 0) {
          innerSql.append(" and " + sqlWhere.replaceAll("crdcenterid", "v.crdcenterid").replaceAll("cinventoryid", "v.cinventoryid"));
        }

        innerSql.append(" and (bd_busitype.verifyrule is null or v.csourcebilltypecode is null ");
        innerSql.append(" or ((bd_busitype.verifyrule <> 'F'  or v.csourcebilltypecode<> '32");
        innerSql.append("') and (bd_busitype.verifyrule <> 'W' or v.csourcebilltypecode<> '32')))");
        innerSql.append(" group by " + strField.substring(0, strField.length() - 1) + ")");
      }
    }

    String sqls = " select " + sField;
    sqls = sqls + " sum(beginninmny), sum(beginnoutmny), sum(ninmny), sum(noutmny) ";
    sqls = sqls + " from (" + innerSql.toString() + ")  as datasource ";
    sqls = sqls + "group by " + sField.substring(0, sField.length() - 1);

    Connection con = null;
    PreparedStatement stmt = null;
    PreparedStatement stmtnum = null;
    ResultSet rs = null;
    resultList = new ArrayList();
    try {
      con = getConnection();
      stmt = con.prepareStatement(sqls);
      rs = stmt.executeQuery();
      while (rs.next())
      {
        VelocityVO temp = new VelocityVO();
        for (int i = 0; i < al.size(); ++i)
        {
          String sKey = al.get(i).toString();
          Object oValue = rs.getObject(sKey);

          temp.setAttributeValue(sKey, oValue);
        }

        int index = al.size();

        Object temp3 = rs.getObject(++index);
        Object temp4 = rs.getObject(++index);
        Object temp5 = rs.getObject(++index);
        Object temp6 = rs.getObject(++index);
        UFDouble qcr = new UFDouble(temp3.toString());
        UFDouble qcc = new UFDouble(temp4.toString());
        UFDouble fsr = new UFDouble(temp5.toString());
        UFDouble fsc = new UFDouble(temp6.toString());
        UFDouble qc = qcr.sub(qcc);
        UFDouble fs = fsr.sub(fsc);

        temp.setNbeginmny(qc);
        temp.setNabmny(qc.add(fs));
        temp.setNoutmny(fsc);
        resultList.add(temp);
      }
      result = new VelocityVO[resultList.size()];
      for (int i = 0; i < result.length; ++i)
        result[i] = ((VelocityVO)resultList.get(i));
    }
    finally
    {
      try {
        if (rs != null)
          rs.close();
      }
      catch (Exception e)
      {
      }
      try {
        if (stmtnum != null) {
          stmtnum.close();
        }
        if (stmt != null)
          stmt.close();
      }
      catch (Exception e)
      {
      }
      try {
        if (con != null) {
          con.close();
        }
      }
      catch (Exception e)
      {
      }
    }

    afterCallMethod("nc.bs.ia.analyze.IaAnalyseDMO", "getVelocity", new Object[] { sqlWhere });

    return result;
  }

  String getJoinSqlForVelocity(String sWhere, String tablename) {
    String joinStr = "";

    if (sWhere.indexOf("bd_calbody.bodycode") >= 0) {
      joinStr = joinStr + " left outer join bd_calbody on " + tablename + ".crdcenterid = bd_calbody.pk_calbody ";
    }

    if ((sWhere.indexOf("inv.") >= 0) || (sWhere.indexOf("pk_invcl") > 0)) {
      joinStr = joinStr + " inner join(select bd_invmandoc.pk_invmandoc invid,bd_invbasdoc.invcode invcode,bd_invcl.invclasscode invclasscode,bd_invbasdoc.invname invname,bd_invbasdoc.pk_invcl pk_invcl, bd_invcl.invclassname invtype,bd_invcl.invclasslev invclasslev,";
      joinStr = joinStr + " bd_measdoc.measname measname,bd_invbasdoc.invspec invspec, bd_invbasdoc.invtype invtypes ";
      joinStr = joinStr + " from bd_measdoc,bd_invmandoc,bd_invbasdoc,bd_invcl where bd_measdoc.pk_measdoc=bd_invbasdoc.pk_measdoc and bd_invbasdoc.pk_invbasdoc = bd_invmandoc.pk_invbasdoc and bd_invcl.pk_invcl = bd_invbasdoc.pk_invcl ) inv ";
      joinStr = joinStr + " on " + tablename + ".cinventoryid = inv.invid ";
    }

    return joinStr;
  }

  String getFieldStr(String sAllString, String tablename)
  {
    String sField = "";
    if (sAllString.indexOf("invname") != -1) {
      sField = sField + "cinventoryid,";
    }
    if ((sAllString.indexOf("invclassname") != -1) && (sField.indexOf("cinventoryid") == -1))
    {
      sField = sField + "cinventoryid,";
    }
    if (sAllString.indexOf("bodyname") != -1) {
      sField = sField + "crdcenterid,";
    }

    return sField;
  }

  String[] paraBeginPeriod(String pk_corp, UFDate beginDate, boolean ifdef) throws BusinessException {
    String[] result = new String[3];

    if (ifdef) {
      String str = " v.dauditdate<'" + beginDate.toString() + "' ";
      result[2] = str;
      return result;
    }

    CommonDataImpl cbo = new CommonDataImpl();

    String Period = cbo.getPeriod(pk_corp, beginDate.toString());
    String perPeriod = cbo.getPerviousPeriod(pk_corp, Period);

    String lastABPeriod = PeriodController.getLastABPeriod(pk_corp, perPeriod);
    String str;
    if ((lastABPeriod != null) && (lastABPeriod.trim().length() > 0)) {
      str = "ab.caccountyear='" + lastABPeriod.substring(0, 4) + "' ";
      str = str + " and ab.caccountmonth='" + lastABPeriod.substring(5) + "' ";
      result[0] = str;
    }

    if (lastABPeriod.compareToIgnoreCase(perPeriod) < 0) {
      str = "m.caccountyear='" + perPeriod.substring(0, 4) + "' ";
      str = str + " and m.caccountmonth>'" + lastABPeriod.substring(5) + "' ";
      str = str + " and m.caccountmonth<='" + perPeriod.substring(5) + "' ";

      result[1] = str;
    }

    UFDate monthbeginDate = cbo.getMonthBeginDate(pk_corp, Period);

    if (monthbeginDate.compareTo(beginDate) != 0) {
      String str1 = " v.dauditdate>='" + monthbeginDate.toString() + "' ";
      str1 = str1 + " and v.dauditdate<'" + beginDate.toString() + "' ";
      result[2] = str1;
    }

    return result;
  }

  String[] paraInOutPeriod(String pk_corp, UFDate beginDate, UFDate endDate, boolean ifdef) throws BusinessException {
    String[] result = new String[2];

    StringBuffer strForPeriod = new StringBuffer(0);
    StringBuffer strForBill = new StringBuffer(0);

    if (ifdef) {
      strForBill.append(" v.dauditdate>='" + beginDate.toString() + "' and ");
      strForBill.append(" v.dauditdate<='" + endDate.toString() + "' ");

      result[1] = strForBill.toString();

      return result;
    }

    CommonDataImpl cbo = new CommonDataImpl();
    String sPeriod = cbo.getPeriod(pk_corp, beginDate.toString());
    String ePeriod = cbo.getPeriod(pk_corp, endDate.toString());
    String usedPeriod = cbo.getStartPeriod(pk_corp);

    String startPeriod = null;
    String endPeriod = null;

    if (cbo.getMonthBeginDate(pk_corp, sPeriod).toString().equalsIgnoreCase(beginDate.toString())) {
      startPeriod = sPeriod;
    }
    else {
      startPeriod = cbo.getNextPeriod(pk_corp, sPeriod);
    }

    if (startPeriod.compareToIgnoreCase(usedPeriod) < 0) {
      startPeriod = usedPeriod;
    }

    if (cbo.getMonthEndDate(pk_corp, ePeriod).toString().equalsIgnoreCase(endDate.toString()))
    {
      endPeriod = ePeriod;
    }
    else {
      endPeriod = cbo.getPerviousPeriod(pk_corp, ePeriod);
    }

    String unClosedPeriod = cbo.getUnClosePeriod(pk_corp);
    String lastAccPeriod = cbo.getPerviousPeriod(pk_corp, unClosedPeriod);

    if (startPeriod.compareTo(endPeriod) > 0) {
      strForBill.append(" (v.pk_corp='" + pk_corp + "' and ");
      strForBill.append(" v.dauditdate>='" + beginDate + "' and v.dauditdate<='" + endDate + "') ");
    }
    else if (endPeriod.compareToIgnoreCase(lastAccPeriod) <= 0)
    {
      strForPeriod.append("( m.pk_corp = '" + pk_corp + "' and ");
      strForPeriod.append(SQLStringUtil.handlePeriod(startPeriod, endPeriod, "m") + ") ");

      if (!(sPeriod.equalsIgnoreCase(startPeriod)))
      {
        strForBill.append(" (v.pk_corp='" + pk_corp + "' and ");
        strForBill.append(" v.dauditdate>='" + beginDate + "' and v.dauditdate<='" + cbo.getMonthEndDate(pk_corp, sPeriod) + "') ");
      }

      if (!(ePeriod.equalsIgnoreCase(endPeriod)))
      {
        strForBill.append(" (v.pk_corp='" + pk_corp + "' and ");
        strForBill.append(" v.dauditdate>='" + cbo.getMonthBeginDate(pk_corp, ePeriod) + "' and v.dauditdate<='" + endDate + "') ");
      }
    }
    else if ((endPeriod.compareToIgnoreCase(lastAccPeriod) > 0) && (startPeriod.compareTo(lastAccPeriod) <= 0))
    {
      strForPeriod.append("( m.pk_corp = '" + pk_corp + "' and ");
      strForPeriod.append(SQLStringUtil.handlePeriod(startPeriod, lastAccPeriod, "m") + ") ");

      if (!(sPeriod.equalsIgnoreCase(startPeriod))) {
        strForBill.append(" (v.pk_corp='" + pk_corp + "' and ");
        strForBill.append(" v.dauditdate>='" + beginDate + "' and v.dauditdate<='" + cbo.getMonthEndDate(pk_corp, sPeriod) + "') ");
      }
      strForBill.append(" (v.pk_corp='" + pk_corp + "' and ");
      strForBill.append(" v.dauditdate>='" + cbo.getMonthBeginDate(pk_corp, unClosedPeriod) + "' and v.dauditdate<='" + endDate + "') ");
    }
    else if (startPeriod.compareTo(lastAccPeriod) > 0)
    {
      strForBill.append(" (v.pk_corp='" + pk_corp + "' and ");
      strForBill.append(" v.dauditdate>='" + beginDate + "' and v.dauditdate<='" + endDate + "') ");
    }

    if (strForPeriod.length() > 0) {
      result[0] = strForPeriod.toString();
    }

    if (strForBill.length() > 0) {
      result[1] = strForBill.toString();
    }

    return result;
  }
}