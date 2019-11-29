package nc.bs.ia.reportalgo;

import javax.naming.NamingException;
import nc.bs.ia.pub.DataAccessUtils;
import nc.vo.ia.analyze.InvInOutSumVO;
import nc.vo.ia.analyze.QueryVO;
import nc.vo.ia.pub.IRowSet;
import nc.vo.pub.BusinessException;

public class SumReportAlgo extends BaseReportAlgo
{
  public InvInOutSumVO[] exceReportAlgoLineReturnVOS(QueryVO queryVO, String tempTable)
    throws BusinessException
  {
    if (queryVO.SourceTable.equalsIgnoreCase(QueryVO.INBILL))
      return getInvInOutSumVos(getSumInBillSql(queryVO, tempTable));
    if (queryVO.SourceTable.equalsIgnoreCase(QueryVO.OUTBILL))
      return getInvInOutSumVos(getSumOutBillSql(queryVO, tempTable));
    if ((queryVO.SourceTable.equalsIgnoreCase(QueryVO.INOUTSUM)) || (queryVO.SourceTable.equalsIgnoreCase(QueryVO.IAFORSO)) || (queryVO.SourceTable.equalsIgnoreCase(QueryVO.REMAIN)))
    {
      return getInvInOutSumVos(getSumInOutSumSql(queryVO, tempTable)); }
    if (queryVO.SourceTable.equalsIgnoreCase(QueryVO.IUFO)) {
      return getInvInOutSumVos(getSumForIUFOSql(queryVO, tempTable));
    }
    return null;
  }

  public IRowSet exceReportAlgoLineReturnIRowSet(QueryVO queryVO, String tempTable) throws BusinessException
  {
    if (queryVO.SourceTable.equalsIgnoreCase(QueryVO.IUFO)) {
      return getIRowSet(getSumForIUFOSql(queryVO, tempTable));
    }
    return null;
  }

  private String getSumInBillSql(QueryVO query, String tempTable)
  {
    StringBuffer strSql = new StringBuffer();

    strSql.append(" select " + SQLStringUtil.getDimensionSQL(query, 0, false, false));
    strSql.append(", sum(ninnum) as ninnum,sum(ninmny) as ninmny, sum(ninplanedmny) as ninplanedmny,sum(ninvarymny) as ninvarymny");
    if (query.isShowAssistant()) {
      strSql.append(", sum(ninassistnum) as ninassistnum ");
    }
    strSql.append(" from " + tempTable);
    strSql.append(" group by " + SQLStringUtil.getDimensionSQL(query, 0, false, true));

    return strSql.toString();
  }

  private String getSumOutBillSql(QueryVO query, String tempTable)
  {
    StringBuffer strSql = new StringBuffer();

    strSql.append(" select " + SQLStringUtil.getDimensionSQL(query, 0, false, false));
    strSql.append(", sum(coalesce(noutnum,0)) as noutnum,sum(coalesce(noutmny,0)) as noutmny, ");
    strSql.append(" sum(coalesce(noutplanedmny,0)) as noutplanedmny,sum(coalesce(noutvarymny,0)) as noutvarymny");
    if (query.isShowAssistant()) {
      strSql.append(", sum(coalesce(noutassistnum,0)) as noutassistnum ");
    }
    strSql.append(", sum(coalesce(njtlnum,0)) as njtlnum ");

    strSql.append(" from " + tempTable);
    strSql.append(" group by " + SQLStringUtil.getDimensionSQL(query, 0, false, true));

    return strSql.toString();
  }

  private String getSumInOutSumSql(QueryVO query, String tempTable)
  {
    StringBuffer strSql = new StringBuffer();

    strSql.append(" select " + SQLStringUtil.getDimensionSQL(query, 0, false, false));

    strSql.append(", sum(coalesce(nbeginnum,0)) as nbeginnum, ");
    if (query.isShowAssistant()) {
      strSql.append(" sum(coalesce(nbeginassistnum,0)) as nbeginassistnum , ");
    }
    strSql.append("sum(coalesce(nbeginmny,0)) as nbeginmny, sum(coalesce(nbeginplanedmny,0)) as nbeginplanedmny, sum(coalesce(nbeginvarymny,0)) as nbeginvarymny, ");

    strSql.append(" sum(coalesce(ninnum,0)) as ninnum,");
    if (query.isShowAssistant()) {
      strSql.append(" sum(coalesce(ninassistnum,0)) as ninassistnum, ");
    }
    strSql.append("sum(coalesce(ninmny,0)) as ninmny, sum(coalesce(ninplanedmny,0)) as ninplanedmny, sum(coalesce(ninvarymny,0)) as ninvarymny, ");
    if (query.isShowAdjustMoney()) {
      strSql.append(" sum(coalesce(ninadjustmny,0)) as ninadjustmny,");
    }

    strSql.append(" sum(coalesce(noutnum,0)) as noutnum,");
    if (query.isShowAssistant()) {
      strSql.append(" sum(coalesce(noutassistnum,0)) as noutassistnum, ");
    }
    strSql.append("sum(coalesce(noutmny,0)) as noutmny, sum(coalesce(noutplanedmny,0)) as noutplanedmny, sum(coalesce(noutvarymny,0)) as noutvarymny, ");
    if (query.isShowAdjustMoney()) {
      strSql.append(" sum(coalesce(noutadjustmny,0)) as noutadjustmny,");
    }
    strSql.append(" sum(coalesce(nbacktaxmny,0)) as nbacktaxmny,");

    strSql.append(" sum(coalesce(nbeginnum,0) + coalesce(ninnum,0) - coalesce(noutnum,0)) as nabnum, ");
    if (query.isShowAssistant()) {
      strSql.append(" sum(coalesce(nbeginassistnum,0) + coalesce(ninassistnum,0) - coalesce(noutassistnum,0)) as nabassistnum, ");
    }
    strSql.append("sum(coalesce(nbeginmny,0)+coalesce(ninmny,0)-coalesce(noutmny,0)) as nabmny,");
    strSql.append("sum(coalesce(nbeginplanedmny,0)+coalesce(ninplanedmny,0)-coalesce(noutplanedmny,0)) as nabplanedmny,");
    strSql.append("sum(coalesce(nbeginvarymny,0)+coalesce(ninvarymny,0)-coalesce(noutvarymny,0)) as nabvarymny ");

    strSql.append(" from " + tempTable);
    strSql.append(" group by " + SQLStringUtil.getDimensionSQL(query, 0, false, true));
    strSql.append(" having sum(coalesce(nbeginnum,0))!=0 or sum(coalesce(nbeginmny,0))!=0 ");
    strSql.append(" or sum(coalesce(nbeginplanedmny,0))!=0 or sum(coalesce(nbeginvarymny,0))!=0 ");
    strSql.append(" or sum(coalesce(ninnum,0))!=0 or sum(coalesce(ninmny,0))!=0 ");
    strSql.append(" or sum(coalesce(ninplanedmny,0))!=0 ");
    strSql.append(" or sum(coalesce(noutnum,0))!=0 or sum(coalesce(noutmny,0))!=0 ");
    strSql.append(" or sum(coalesce(noutplanedmny,0))!=0 ");
    strSql.append(" or sum(coalesce(nbacktaxmny,0))!=0 ");

    if (query.isShowAssistant()) {
      strSql.append(" or sum(coalesce(nbeginassistnum,0))!=0 or sum(coalesce(ninassistnum,0))!=0 ");
      strSql.append(" or sum(coalesce(noutassistnum,0))!=0 ");
    }

    if (query.isShowAdjustMoney()) {
      strSql.append(" or sum(coalesce(ninadjustmny,0))!=0 or sum(coalesce(noutadjustmny,0))!=0 ");
    }

    return strSql.toString();
  }

  private String getSumForIUFOSql(QueryVO query, String tempTable)
  {
    StringBuffer strSql = new StringBuffer();

    strSql.append(" select sum(coalesce(ninnum,0)) as ninnum, sum(coalesce(ninmny,0)) as ninmny, ");
    strSql.append(" sum(coalesce(noutnum,0)) as noutnum, sum(coalesce(noutmny,0)) as noutmny, ");
    strSql.append(SQLStringUtil.getDimensionSQL(query, 7, false, false));
    strSql.append(" from " + tempTable);

    String[] joinSql = SQLStringUtil.getJoinSQL(query, tempTable);

    strSql.append(joinSql[0]);
    strSql.append(joinSql[1]);
    strSql.append(" group by " + SQLStringUtil.getDimensionSQL(query, 7, false, true));
    strSql.append(" having sum(coalesce(ninnum,0))!=0 or sum(coalesce(ninmny,0))!=0 ");
    strSql.append(" or sum(coalesce(noutnum,0))!=0 or sum(coalesce(noutmny,0))!=0 ");

    return strSql.toString();
  }

  private InvInOutSumVO[] getInvInOutSumVos(String strSql) throws BusinessException {
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

  private IRowSet getIRowSet(String strSql) throws BusinessException
  {
    IRowSet rowset = DataAccessUtils.query(strSql);

    return rowset;
  }
}