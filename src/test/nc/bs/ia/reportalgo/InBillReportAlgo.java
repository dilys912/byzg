package nc.bs.ia.reportalgo;

import javax.naming.NamingException;
import nc.impl.ia.pub.CommonDataImpl;
import nc.vo.ia.analyze.InvInOutSumVO;
import nc.vo.ia.analyze.QueryVO;
import nc.vo.ia.analyze.StatisticsVO;
import nc.vo.ia.pub.IRowSet;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;

public class InBillReportAlgo extends BaseReportAlgo
{
  public InvInOutSumVO[] exceReportAlgoLineReturnVOS(QueryVO queryVO, String tempTable)
    throws BusinessException
  {
    String sDime = SQLStringUtil.getDimensionSQL(queryVO, 0, false, false);

    String sWhere = queryVO.getWhere();

    if ((((sDime.indexOf("dbilldate") >= 0) || (sDime.indexOf("vbillcode") >= 0) || (sDime.indexOf("vbomcode") >= 0) || (sWhere.indexOf("dbilldate") >= 0) || (sWhere.indexOf("dauditdate") >= 0) || (sWhere.indexOf("vbillcode") >= 0) || (sWhere.indexOf("vbomcode") >= 0) || (sWhere.indexOf("bdef") >= 0) || (sWhere.indexOf("vdef") >= 0))) && (queryVO.SourceTable.equalsIgnoreCase(QueryVO.INBILL)))
    {
      return exceReportAlgoDirectlyReturnVOS(queryVO, 2);
    }

    String[] sCorpAndPeriod = parseCorpAndPeriod(queryVO);

    if ((((sCorpAndPeriod[1] == null) || (sCorpAndPeriod[1].length() == 0))) && (((sCorpAndPeriod[2] == null) || (sCorpAndPeriod[2].length() == 0))) && (queryVO.SourceTable.equalsIgnoreCase(QueryVO.INBILL)))
    {
      return exceReportAlgoDirectlyReturnVOS(queryVO, 1);
    }

    exceLine(queryVO, tempTable, sCorpAndPeriod);

    return this.nextAlgo.exceReportAlgoLineReturnVOS(queryVO, tempTable);
  }

  private String[] parseCorpAndPeriod(QueryVO query)
    throws BusinessException
  {
    CommonDataImpl cbo = new CommonDataImpl();

    StringBuffer strForPeriod = new StringBuffer();
    StringBuffer strForBill = new StringBuffer();

    String[] corps = query.getPk_Corps();

    String beginDate = query.getDate()[0];
    String endDate = query.getDate()[1];

    StatisticsVO[] stat = query.getStatistics();

    for (int i = 0; i < stat.length; ++i)
    {
      if (stat[i].getFieldCode().equalsIgnoreCase("busicode")) {
        String[] res = new String[3];
        String strCorp = "";

        for (int j = 0; j < corps.length; ++j) {
          if (strCorp.length() > 0) {
            strCorp = strCorp + ",";
          }
          strCorp = strCorp + "'" + corps[j] + "'";
        }

        String temp = " (h.pk_corp in (" + strCorp + ") and ";
        temp = temp + " b.dauditdate>='" + beginDate + "' and b.dauditdate<='" + endDate + "') ";

        res[1] = temp;

        return res;
      }
    }

    for (int i = 0; i < corps.length; ++i)
    {
      String sPeriod = cbo.getPeriod(corps[i], beginDate).trim();
      String ePeriod = cbo.getPeriod(corps[i], endDate).trim();
      String usedPeriod = cbo.getStartPeriod(corps[i]);

      String startPeriod = null;
      String endPeriod = null;

      if (cbo.getMonthBeginDate(corps[i], sPeriod).toString().equalsIgnoreCase(beginDate))
      {
        startPeriod = sPeriod;
      }
      else {
        startPeriod = cbo.getNextPeriod(corps[i], sPeriod);
      }

      if (startPeriod.compareToIgnoreCase(usedPeriod) < 0) {
        startPeriod = usedPeriod;
      }

      if (cbo.getMonthEndDate(corps[i], ePeriod).toString().equalsIgnoreCase(endDate))
      {
        endPeriod = ePeriod;
      }
      else {
        endPeriod = cbo.getPerviousPeriod(corps[i], ePeriod);
      }

      String unClosedPeriod = cbo.getUnClosePeriod(corps[i]);

      String lastAccPeriod = cbo.getPerviousPeriod(corps[i], unClosedPeriod);

      if ((!(query.QueryType.equalsIgnoreCase(QueryVO.AUDIT))) && (!(query.QueryType.equalsIgnoreCase(QueryVO.ALL))))
        continue;
      if (startPeriod.compareTo(endPeriod) > 0) {
        if (strForBill.length() > 0) {
          strForBill.append(" or ");
        }
        strForBill.append(" (h.pk_corp='" + corps[i] + "' and ");
        strForBill.append(" b.dauditdate>='" + beginDate + "' and b.dauditdate<='" + endDate + "') ");
      }
      else if (endPeriod.compareToIgnoreCase(lastAccPeriod) <= 0)
      {
        if (strForPeriod.length() > 0) {
          strForPeriod.append(" or ");
        }
        strForPeriod.append("( m.pk_corp = '" + corps[i] + "' and ");
        strForPeriod.append(SQLStringUtil.handlePeriod(startPeriod, endPeriod, "m") + ") ");

        if (!(sPeriod.equalsIgnoreCase(startPeriod))) {
          if (strForBill.length() > 0) {
            strForBill.append(" or ");
          }
          strForBill.append(" (h.pk_corp='" + corps[i] + "' and ");
          strForBill.append(" b.dauditdate>='" + beginDate + "' and b.dauditdate<='" + cbo.getMonthEndDate(corps[i], sPeriod) + "') ");
        }

        if (!(ePeriod.equalsIgnoreCase(endPeriod))) {
          if (strForBill.length() > 0) {
            strForBill.append(" or ");
          }
          strForBill.append(" (h.pk_corp='" + corps[i] + "' and ");
          strForBill.append(" b.dauditdate>='" + cbo.getMonthBeginDate(corps[i], ePeriod) + "' and b.dauditdate<='" + endDate + "' )");
        }
      }
      else if ((endPeriod.compareToIgnoreCase(lastAccPeriod) > 0) && (startPeriod.compareTo(lastAccPeriod) <= 0))
      {
        if (strForPeriod.length() > 0) {
          strForPeriod.append(" or ");
        }
        strForPeriod.append("( m.pk_corp = '" + corps[i] + "' and ");
        strForPeriod.append(SQLStringUtil.handlePeriod(startPeriod, lastAccPeriod, "m") + ") ");

        if (!(sPeriod.equalsIgnoreCase(startPeriod))) {
          if (strForBill.length() > 0) {
            strForBill.append(" or ");
          }
          strForBill.append(" (h.pk_corp='" + corps[i] + "' and ");
          strForBill.append(" b.dauditdate>='" + beginDate + "' and b.dauditdate<='" + cbo.getMonthEndDate(corps[i], sPeriod) + "') ");
        }

        if (strForBill.length() > 0) {
          strForBill.append(" or ");
        }
        strForBill.append(" (h.pk_corp='" + corps[i] + "' and ");
        strForBill.append(" b.dauditdate>='" + cbo.getMonthBeginDate(corps[i], unClosedPeriod) + "' and b.dauditdate<='" + endDate + "') ");
      }
      else if (startPeriod.compareTo(lastAccPeriod) > 0) {
        if (strForBill.length() > 0) {
          strForBill.append(" or ");
        }
        strForBill.append(" (h.pk_corp='" + corps[i] + "' and ");
        strForBill.append(" b.dauditdate>='" + beginDate + "' and b.dauditdate<='" + endDate + "') ");
      }

    }

    String[] result = new String[3];

    if (strForPeriod.length() > 0) {
      result[0] = strForPeriod.toString();
    }
    if (strForBill.length() > 0) {
      result[1] = strForBill.toString();
    }
    if ((query.QueryType.equalsIgnoreCase(QueryVO.ALL)) || (query.QueryType.equalsIgnoreCase(QueryVO.UNAUDIT)))
    {
      String corp = "";

      if (corps.length == 1) {
        corp = corp + " h.pk_corp='" + corps[0] + "' ";
      }
      else {
        corp = corp + " h.pk_corp in (";

        for (int i = 0; i < corps.length; ++i) {
          if (i != 0) {
            corp = corp + ",";
          }
          corp = corp + "'" + corps[i] + "'";
        }

        corp = corp + ")";
      }

      result[2] = corp + " and b.iauditsequence=-1 and h.dbilldate>='" + beginDate + "' and h.dbilldate<='" + endDate + "' ";
    }

    return result;
  }

  public InvInOutSumVO[] exceReportAlgoDirectlyReturnVOS(QueryVO query, int flag)
    throws BusinessException
  {
    String strSql = null;

    if (flag == 1)
    {
      strSql = getSQLForPeriod(query, parseCorpAndPeriod(query)[0]);
    }
    else if (flag == 2)
    {
      StringBuffer sWhere = new StringBuffer();

      sWhere.append(" h.pk_corp in (");

      String[] corps = query.getPk_Corps();

      for (int i = 0; i < corps.length; ++i) {
        if (i != 0) {
          sWhere.append(",");
        }
        sWhere.append("'" + corps[i] + "'");
      }

      sWhere.append(") ");

      if (query.QueryType.equalsIgnoreCase(QueryVO.ALL)) {
        sWhere.append(" and (case when b.iauditsequence<0 then h.dbilldate else b.dauditdate end)>='" + query.getDate()[0] + "' ");
        sWhere.append(" and (case when b.iauditsequence<0 then h.dbilldate else b.dauditdate end)<='" + query.getDate()[1] + "' ");
      } else if (query.QueryType.equalsIgnoreCase(QueryVO.AUDIT)) {
        sWhere.append(" and b.iauditsequence>0 ");
        sWhere.append(" and b.dauditdate>='" + query.getDate()[0] + "' ");
        sWhere.append(" and b.dauditdate<='" + query.getDate()[1] + "' ");
      } else if (query.QueryType.equalsIgnoreCase(QueryVO.UNAUDIT)) {
        sWhere.append(" and b.iauditsequence<0 ");
        sWhere.append(" and h.dbilldate>='" + query.getDate()[0] + "' ");
        sWhere.append(" and h.dbilldate<='" + query.getDate()[1] + "' ");
      }
      strSql = getSQLForBill(query, sWhere.toString());
    }

    InvInOutSumVO[] vosResult = null;
    try
    {
      ReportDataAccessUtil util = new ReportDataAccessUtil();

      vosResult = util.getInOutSumVO(strSql.toString());
    }
    catch (NamingException e) {
      e.printStackTrace();
      throw new BusinessException(e);
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new BusinessException(e);
    }

    return vosResult;
  }

  private String getSQLForPeriod(QueryVO query, String sCorpAndPeriod)
  {
    StringBuffer strSql = new StringBuffer();

    String sGroup = "";
    String sHaving = "";

    strSql.append(" select ");

    if (query.isShowSetPart()) {
      strSql.append(SQLStringUtil.getDimensionSQL(query, 3, false, false));

      strSql.append(", sum(case when setpart.pk_invmandocset is null then coalesce(m.ninnum, 0) ");
      strSql.append(" else setpart.childsnum * coalesce(m.ninnum, 0) end) as ninnum,");
      strSql.append(" sum(case when setpart.pk_invmandocset is null then coalesce(m.ninmny, 0) ");
      strSql.append(" else setpart.childsnum * setpart.partpercent *coalesce(m.ninmny, 0) end) as ninmny, ");
      strSql.append(" sum(case when setpart.pk_invmandocset is null then coalesce(m.ninplanedmny, 0) ");
      strSql.append(" else setpart.childsnum * setpart.partpercent * coalesce(m.ninplanedmny,0) end) as ninplanedmny,");
      strSql.append(" sum(case when setpart.pk_invmandocset is null then coalesce(m.ninvarymny, 0) ");
      strSql.append(" else setpart.childsnum * setpart.partpercent *coalesce(m.ninvarymny,0) end) as ninvarymny ");
      if (query.isShowAssistant()) {
        strSql.append(", sum(case when setpart.pk_invmandocset is null then coalesce(m.ninassistnum, 0) ");
        strSql.append(" else setpart.childsnum * coalesce(m.ninassistnum,0) end) as ninassistnum ");
      }

      if (query.isShowAdjustMoney()) {
        strSql.append(", sum(case when m.cbilltypecode='I9' then (case when setpart.pk_invmandocset is null then coalesce(m.ninmny, 0) ");
        strSql.append(" else setpart.childsnum * setpart.partpercent * coalesce(m.ninmny, 0) end) else 0 end) as ninadjustmny ");
      }

      if (query.SourceTable.equalsIgnoreCase(QueryVO.INOUTSUM)) {
        strSql.append(", sum(case when setpart.pk_invmandocset is null then coalesce(m.nbacktaxmny, 0) ");
        strSql.append(" else setpart.childsnum * setpart.partpercent * coalesce(m.nbacktaxmny, 0) end) as nbacktaxmny ");
      }

      sGroup = SQLStringUtil.getDimensionSQL(query, 3, false, true);
      sHaving = sHaving + " sum(case when setpart.pk_invmandocset is null then coalesce(m.ninnum, 0) ";
      sHaving = sHaving + " else setpart.childsnum * coalesce(m.ninnum, 0) end)!=0 ";
      sHaving = sHaving + " or sum(case when setpart.pk_invmandocset is null then coalesce(m.ninmny, 0) ";
      sHaving = sHaving + " else setpart.childsnum * setpart.partpercent*coalesce(m.ninmny, 0) end)!=0 ";
      sHaving = sHaving + " or sum(case when setpart.pk_invmandocset is null then coalesce(m.ninplanedmny, 0) ";
      sHaving = sHaving + " else setpart.childsnum * setpart.partpercent*coalesce(m.ninplanedmny,0) end)!=0 ";
    }
    else
    {
      strSql.append(SQLStringUtil.getDimensionSQL(query, 1, false, false));

      strSql.append(", sum(coalesce(m.ninnum, 0)) as ninnum, sum(coalesce(m.ninmny, 0)) as ninmny, ");
      strSql.append(" sum(coalesce(m.ninplanedmny, 0)) as ninplanedmny, sum(coalesce(m.ninvarymny, 0)) as ninvarymny ");

      if (query.isShowAssistant()) {
        strSql.append(", sum(coalesce(m.ninassistnum, 0)) as ninassistnum ");
      }

      if (query.isShowAdjustMoney()) {
        strSql.append(", sum(case when m.cbilltypecode='I9' then coalesce(m.ninmny, 0) else 0 end) as ninadjustmny ");
      }

      if (query.SourceTable.equalsIgnoreCase(QueryVO.INOUTSUM)) {
        strSql.append(", sum(m.nbacktaxmny) as nbacktaxmny ");
      }

      sGroup = SQLStringUtil.getDimensionSQL(query, 1, false, true);
      sHaving = sHaving + " sum(coalesce(m.ninnum, 0))!=0 or sum(coalesce(m.ninmny, 0))!=0 or sum(coalesce(m.ninplanedmny, 0))!=0 ";
    }

    strSql.append(" from ia_monthinout m ");

    String[] sJoin = SQLStringUtil.getJoinSQL(query, "m");
    strSql.append(sJoin[0]);
    strSql.append(sJoin[1]);

    strSql.append(" where (" + sCorpAndPeriod + ")");
    strSql.append(" and m.cbilltypecode in ('I2','I9','I4','ID','I3','IB','II')");

    if (((query.getWhere() != null) && (query.getWhere().length() > 0)) || ((query.getDataPowerSql() != null) && (query.getDataPowerSql().length() > 0)))
    {
      String sWhere = query.getWhere();

      if ((sWhere.length() > 0) && (query.getDataPowerSql() != null) && (query.getDataPowerSql().trim().length() > 0))
      {
        sWhere = sWhere + " and " + query.getDataPowerSql();
      } else if ((query.getDataPowerSql() != null) && (query.getDataPowerSql().trim().length() > 0))
      {
        sWhere = sWhere + query.getDataPowerSql();
      }

      strSql.append(" and " + SQLStringUtil.replaceQueryStr(sWhere, "m"));
    }
    strSql.append(" group by " + sGroup);
    strSql.append(" having " + sHaving);

    return strSql.toString();
  }

  private String getSQLForBill(QueryVO query, String sCorpAndPeriod)
  {
    StringBuffer strSql = new StringBuffer();
    String sGroup = "";
    String sHaving = "";

    strSql.append(" select ");

    if (query.isShowSetPart())
    {
      strSql.append(SQLStringUtil.getDimensionSQL(query, 4, false, false));

      strSql.append(", sum(case when setpart.pk_invmandocset is null then coalesce(b.nnumber, 0) ");
      strSql.append(" else setpart.childsnum * coalesce(b.nnumber, 0) end) as ninnum,");

      strSql.append(" sum(case when setpart.pk_invmandocset is null then coalesce(b.nmoney, 0) ");
      strSql.append(" else setpart.childsnum * setpart.partpercent*coalesce(b.nmoney, 0) end) as ninmny, ");

      strSql.append(" sum(case when setpart.pk_invmandocset is null then coalesce(b.nplanedmny, 0) ");
      strSql.append(" else setpart.childsnum * setpart.partpercent*coalesce(b.nplanedmny,0) end) as ninplanedmny,");

      strSql.append(" sum(case when setpart.pk_invmandocset is null then coalesce(b.ninvarymny, 0) ");
      strSql.append(" else setpart.childsnum * setpart.partpercent*coalesce(b.ninvarymny,0) end) as ninvarymny ");

      if (query.isShowAssistant()) {
        strSql.append(", sum(case when setpart.pk_invmandocset is null then coalesce(b.nassistnum, 0) ");
        strSql.append(" else setpart.childsnum * coalesce(b.nassistnum,0) end) as ninassistnum ");
      }

      if (query.isShowAdjustMoney()) {
        strSql.append(", sum(case when h.cbilltypecode='I9' then (case when setpart.pk_invmandocset is null then coalesce(b.nmoney, 0) ");
        strSql.append(" else setpart.childsnum * setpart.partpercent*coalesce(b.nmoney, 0) end) else 0 end) as ninadjustmny ");
      }

      if (query.SourceTable.equalsIgnoreCase(QueryVO.INOUTSUM)) {
        strSql.append(", sum(case when b.ndrawsummny is not null then (case when setpart.pk_invmandocset is null then coalesce(b.ndrawsummny, 0)+coalesce(b.nmoney,0) ");
        strSql.append(" else setpart.childsnum * setpart.partpercent*(coalesce(b.ndrawsummny, 0)+coalesce(b.nmoney,0)) end) else 0 end) as nbacktaxmny ");
      }

      sGroup = SQLStringUtil.getDimensionSQL(query, 4, false, true);
      sHaving = sHaving + " sum(case when setpart.pk_invmandocset is null then coalesce(b.nnumber, 0) ";
      sHaving = sHaving + " else setpart.childsnum * coalesce(b.nnumber, 0) end)!=0 ";
      sHaving = sHaving + " or sum(case when setpart.pk_invmandocset is null then coalesce(b.nmoney, 0) ";
      sHaving = sHaving + " else setpart.childsnum * setpart.partpercent*coalesce(b.nmoney, 0) end)!=0 ";
      sHaving = sHaving + " or sum(case when setpart.pk_invmandocset is null then coalesce(b.nplanedmny, 0) ";
      sHaving = sHaving + " else setpart.childsnum * setpart.partpercent*coalesce(b.nplanedmny,0) end)!=0 ";
    }
    else {
      strSql.append(SQLStringUtil.getDimensionSQL(query, 2, false, false));

      strSql.append(", sum(coalesce(b.nnumber, 0)) as ninnum, sum(coalesce(b.nmoney, 0)) as ninmny ,");
      strSql.append(" sum(coalesce(b.nplanedmny, 0)) as ninplanedmny, sum(coalesce(b.ninvarymny, 0)) as ninvarymny ");

      if (query.isShowAssistant()) {
        strSql.append(", sum(coalesce(b.nassistnum, 0)) as ninassistnum ");
      }

      if (query.isShowAdjustMoney()) {
        strSql.append(", sum(case when h.cbilltypecode='I9' then coalesce(b.nmoney, 0) else 0 end) as ninadjustmny ");
      }

      if (query.SourceTable.equalsIgnoreCase(QueryVO.INOUTSUM)) {
        strSql.append(", sum(case when b.ndrawsummny is not null then coalesce(b.ndrawsummny, 0)+coalesce(b.nmoney,0) else 0 end ) as nbacktaxmny ");
      }

      sGroup = SQLStringUtil.getDimensionSQL(query, 2, false, true);
      sHaving = sHaving + " sum(coalesce(b.nnumber, 0))!=0 or sum(coalesce(b.nmoney, 0))!=0 or sum(coalesce(b.nplanedmny, 0))!=0 ";
    }

    String[] sJoin = SQLStringUtil.getJoinSQL(query, "v");
    strSql.append(" from ia_bill h " + sJoin[0]);
    strSql.append(" , ia_bill_b b " + sJoin[1]);

    strSql.append(" where h.cbillid=b.cbillid and h.dr=0 and b.dr=0 and h.bdisableflag = 'N' ");
    strSql.append(" and h.cbilltypecode in ('I2','I9','I4','ID','I3','IB','II') ");
    strSql.append(" and (h.cbiztypeid is null or h.cbiztypeid not in (" + query.sFQSK + "," + query.sWTDX + ") ");
    strSql.append(" or b.csourcebilltypecode is null or b.csourcebilltypecode <> '32') ");
    strSql.append(" and (" + sCorpAndPeriod + ") ");
    if (((query.getWhere() != null) && (query.getWhere().length() > 0)) || ((query.getDataPowerSql() != null) && (query.getDataPowerSql().length() > 0)))
    {
      String sWhere = query.getWhere();

      if ((sWhere.length() > 0) && (query.getDataPowerSql() != null) && (query.getDataPowerSql().trim().length() > 0))
      {
        sWhere = sWhere + " and " + query.getDataPowerSql();
      } else if ((query.getDataPowerSql() != null) && (query.getDataPowerSql().trim().length() > 0))
      {
        sWhere = sWhere + query.getDataPowerSql();
      }

      strSql.append(" and " + SQLStringUtil.replaceQueryStr(sWhere, "h"));
    }
    strSql.append(" group by " + sGroup);
    strSql.append(" having " + sHaving);

    return strSql.toString();
  }

  private void exceLine(QueryVO queryVO, String tempTable, String[] sCorpAndPeriod) throws BusinessException
  {
    CommonDataImpl cbo = new CommonDataImpl();
    StringBuffer strSql = new StringBuffer();

    if ((sCorpAndPeriod[0] != null) && (sCorpAndPeriod[0].length() != 0))
    {
      strSql.append(" insert into " + tempTable);
      strSql.append("(" + SQLStringUtil.getDimensionSQL(queryVO, 0, false, false));
      strSql.append(", ninnum, ninmny, ninplanedmny, ninvarymny ");
      if (queryVO.isShowAssistant()) {
        strSql.append(", ninassistnum ");
      }
      if (queryVO.isShowAdjustMoney()) {
        strSql.append(", ninadjustmny ");
      }
      if (queryVO.SourceTable.equalsIgnoreCase(QueryVO.INOUTSUM)) {
        strSql.append(", nbacktaxmny ");
      }
      strSql.append(") (");
      strSql.append(getSQLForPeriod(queryVO, sCorpAndPeriod[0]));
      strSql.append(")");

      cbo.execData(strSql.toString());

      strSql.setLength(0);
    }

    if ((sCorpAndPeriod[1] != null) && (sCorpAndPeriod[1].length() != 0))
    {
      strSql.append(" insert into " + tempTable);
      strSql.append("(" + SQLStringUtil.getDimensionSQL(queryVO, 0, false, false));
      strSql.append(", ninnum, ninmny, ninplanedmny, ninvarymny ");
      if (queryVO.isShowAssistant()) {
        strSql.append(", ninassistnum ");
      }
      if (queryVO.isShowAdjustMoney()) {
        strSql.append(", ninadjustmny ");
      }
      if (queryVO.SourceTable.equalsIgnoreCase(QueryVO.INOUTSUM)) {
        strSql.append(", nbacktaxmny ");
      }
      strSql.append(") (");
      strSql.append(getSQLForBill(queryVO, sCorpAndPeriod[1]));
      strSql.append(")");

      cbo.execData(strSql.toString());

      strSql.setLength(0);
    }

    if ((sCorpAndPeriod[2] == null) || (sCorpAndPeriod[2].length() == 0))
      return;
    strSql.append(" insert into " + tempTable);
    strSql.append("(" + SQLStringUtil.getDimensionSQL(queryVO, 0, false, false));
    strSql.append(", ninnum, ninmny, ninplanedmny,ninvarymny ");
    if (queryVO.isShowAssistant()) {
      strSql.append(", ninassistnum ");
    }
    if (queryVO.isShowAdjustMoney()) {
      strSql.append(", ninadjustmny ");
    }
    if (queryVO.SourceTable.equalsIgnoreCase(QueryVO.INOUTSUM)) {
      strSql.append(", nbacktaxmny ");
    }
    strSql.append(") (");
    strSql.append(getSQLForBill(queryVO, sCorpAndPeriod[2]));
    strSql.append(")");

    cbo.execData(strSql.toString());

    strSql.setLength(0);
  }

  public IRowSet exceReportAlgoLineReturnIRowSet(QueryVO queryVO, String tempTable)
    throws BusinessException
  {
    String[] sCorpAndPeriod = parseCorpAndPeriod(queryVO);

    exceLine(queryVO, tempTable, sCorpAndPeriod);

    return this.nextAlgo.exceReportAlgoLineReturnIRowSet(queryVO, tempTable);
  }
}