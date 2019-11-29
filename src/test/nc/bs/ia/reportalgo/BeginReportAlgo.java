package nc.bs.ia.reportalgo;

import nc.bs.ia.ia402.PeriodController;
import nc.impl.ia.pub.CommonDataImpl;
import nc.vo.ia.analyze.InvInOutSumVO;
import nc.vo.ia.analyze.QueryVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;

class BeginReportAlgo extends BaseReportAlgo
{
  public InvInOutSumVO[] exceReportAlgoLineReturnVOS(QueryVO queryVO, String tempTable)
    throws BusinessException
  {
    CommonDataImpl cbo = new CommonDataImpl();

    StringBuffer sSql = new StringBuffer();

    String beginDate = queryVO.getDate()[0];

    if ((beginDate == null) || (beginDate.length() == 0))
    {
      String sGroup = null;

      sSql.append(" insert into " + tempTable);
      sSql.append(" ( ");
      sSql.append(SQLStringUtil.getDimensionSQL(queryVO, 0, true, false));
      sSql.append(", nbeginnum, nbeginmny, nbeginplanedmny, nbeginvarymny");
      if (queryVO.isShowAssistant()) {
        sSql.append(", nbeginassistnum");
      }
      sSql.append(" ) (");

      if (queryVO.isShowSetPart()) {
        sSql.append(" select " + SQLStringUtil.getDimensionSQL(queryVO, 6, true, false));
        sSql.append(", sum(case when setpart.pk_invmandocset is null then coalesce(ab.nabnum, 0) ");
        sSql.append(" else setpart.childsnum * coalesce(ab.nabnum, 0) end) as nbeginnum , ");
        sSql.append(" sum(case when setpart.pk_invmandocset is null then coalesce(ab.nabmny, 0) ");
        sSql.append(" else setpart.childsnum * setpart.partpercent*coalesce(ab.nabmny, 0) end) as nbeginmny , ");
        sSql.append(" sum(case when setpart.pk_invmandocset is null then coalesce(ab.nabplanedmny, 0) ");
        sSql.append(" else setpart.childsnum * setpart.partpercent*coalesce(ab.nabplanedmny,0) end) as nbeginplanedmny ,");
        sSql.append(" sum(case when setpart.pk_invmandocset is null then coalesce(ab.nabvarymny, 0) ");
        sSql.append(" else setpart.childsnum * setpart.partpercent*coalesce(ab.nabvarymny,0) end) as nbeginvarymny  ");

        if (queryVO.isShowAssistant()) {
          sSql.append(", sum(case when setpart.pk_invmandocset is null then coalesce(ab.nabassistnum, 0) ");
          sSql.append(" else setpart.childsnum * coalesce(ab.nabassistnum,0) end)as nbeginassistnum ");
        }

        sGroup = SQLStringUtil.getDimensionSQL(queryVO, 6, true, true);
      }
      else {
        sSql.append(" select " + SQLStringUtil.getDimensionSQL(queryVO, 5, true, false));
        sSql.append(", sum(ab.nabnum) as nbeginnum, sum(ab.nabmny) as nbeginmny, ");
        sSql.append(" sum(ab.nabplanedmny) as nbeginplanedmny, sum(ab.nabvarymny) as nbeginvarymny ");

        if (queryVO.isShowAssistant()) {
          sSql.append(", sum(ab.nabassistnum) as nbeginassistnum ");
        }

        sGroup = SQLStringUtil.getDimensionSQL(queryVO, 5, true, true);
      }

      sSql.append(" from ia_periodaccount ab ");

      String[] sJoin = SQLStringUtil.getJoinSQL(queryVO, "ab");
      sSql.append(sJoin[0]);
      sSql.append(sJoin[1]);

      String[] corps = queryVO.getPk_Corps();

      String strCorp = null;

      for (int i = 0; 9 < corps.length; ++i) {
        if (strCorp.length() > 0) {
          strCorp = strCorp + ", ";
        }
        strCorp = strCorp + corps[i];
      }

      sSql.append("where caccountmonth ='00' and pk_corp in (" + strCorp + ") ");
      String temp = queryVO.getWhere();
      temp = removePeriod(temp);

      if ((temp.length() > 0) && (queryVO.getDataPowerSql() != null) && (queryVO.getDataPowerSql().trim().length() > 0))
      {
        temp = temp + " and " + queryVO.getDataPowerSql();
      } else if ((queryVO.getDataPowerSql() != null) && (queryVO.getDataPowerSql().trim().length() > 0))
      {
        temp = temp + queryVO.getDataPowerSql();
      }

      if (temp.length() > 0) {
        sSql.append(" and " + SQLStringUtil.replaceQueryStr(temp, "ab"));
      }
      sSql.append(" group by " + sGroup);

      if (queryVO.isShowSetPart()) {
        sSql.append(" having sum(case when setpart.pk_invmandocset is null then coalesce(ab.nabnum, 0) ");
        sSql.append(" else setpart.childsnum * coalesce(ab.nabnum, 0) end)!=0 ");
        sSql.append(" or sum(case when setpart.pk_invmandocset is null then coalesce(ab.nabmny, 0) ");
        sSql.append(" else setpart.childsnum * setpart.partpercent*coalesce(ab.nabmny, 0) end)!=0 ");
        sSql.append(" or sum(case when setpart.pk_invmandocset is null then coalesce(ab.nabplanedmny, 0) ");
        sSql.append(" else setpart.childsnum * setpart.partpercent*coalesce(ab.nabplanedmny,0) end)!=0 ");
      }
      else {
        sSql.append(" having sum(ab.nabnum)!=0 or sum(ab.nabmny)!=0 or sum(ab.nabplanedmny)!=0 ");
      }

      sSql.append(") ");

      cbo.execData(sSql.toString());
    }
    else
    {
      String[] sWherePeriod = parseCorpAndPeriod(queryVO);
      String sGroup;
      String[] sJoin;
      String temp;
      if (sWherePeriod[0].length() > 0)
      {
        sGroup = null;

        sSql.append(" insert into " + tempTable);
        sSql.append(" ( ");
        System.out.println(SQLStringUtil.getDimensionSQL(queryVO, 0, true, false));
        sSql.append(SQLStringUtil.getDimensionSQL(queryVO, 0, true, false));
        sSql.append(", nbeginnum, nbeginmny, nbeginplanedmny, nbeginvarymny");
        if (queryVO.isShowAssistant()) {
          sSql.append(", nbeginassistnum");
        }
        sSql.append(" ) (");

        if (queryVO.isShowSetPart()) {
          sSql.append(" select " + SQLStringUtil.getDimensionSQL(queryVO, 6, true, false));
          sSql.append(", sum(case when setpart.pk_invmandocset is null then coalesce(ab.nabnum, 0) else setpart.childsnum * coalesce(ab.nabnum, 0) end),");
          sSql.append(" sum(case when setpart.pk_invmandocset is null then coalesce(ab.nabmny, 0) else setpart.childsnum * setpart.partpercent*coalesce(ab.nabmny, 0) end), ");
          sSql.append(" sum(case when setpart.pk_invmandocset is null then coalesce(ab.nabplanedmny, 0) else setpart.childsnum * setpart.partpercent*coalesce(ab.nabplanedmny,0) end),");
          sSql.append(" sum(case when setpart.pk_invmandocset is null then coalesce(ab.nabvarymny, 0) else setpart.childsnum * setpart.partpercent*coalesce(ab.nabvarymny,0) end) ");

          if (queryVO.isShowAssistant()) {
            sSql.append(", sum(case when setpart.pk_invmandocset is null then coalesce(ab.nabassistnum, 0) else setpart.childsnum * coalesce(ab.nabassistnum,0) end) ");
          }

          sGroup = SQLStringUtil.getDimensionSQL(queryVO, 6, true, true);
        }
        else {
          sSql.append(" select " + SQLStringUtil.getDimensionSQL(queryVO, 5, true, false));
          sSql.append(", sum(ab.nabnum), sum(ab.nabmny), sum(ab.nabplanedmny), sum(ab.nabvarymny)");
          if (queryVO.isShowAssistant()) {
            sSql.append(", sum(ab.nabassistnum)");
          }

          sGroup = SQLStringUtil.getDimensionSQL(queryVO, 5, true, true);
        }

        sSql.append(" from ia_periodaccount ab ");

        sJoin = SQLStringUtil.getJoinSQL(queryVO, "ab");
        sSql.append(sJoin[0]);
        sSql.append(sJoin[1]);

        sSql.append(" where (");
        sSql.append(sWherePeriod[0] + ") ");
        temp = queryVO.getWhere();
        temp = removePeriod(temp);

        if ((temp.length() > 0) && (queryVO.getDataPowerSql() != null) && (queryVO.getDataPowerSql().trim().length() > 0))
        {
          temp = temp + " and " + queryVO.getDataPowerSql();
        } else if ((queryVO.getDataPowerSql() != null) && (queryVO.getDataPowerSql().trim().length() > 0))
        {
          temp = temp + queryVO.getDataPowerSql();
        }

        if (temp.length() > 0) {
          sSql.append(" and " + SQLStringUtil.replaceQueryStr(temp, "ab"));
        }
        sSql.append(" group by " + sGroup);

        if (queryVO.isShowSetPart()) {
          sSql.append(" having sum(case when setpart.pk_invmandocset is null then coalesce(ab.nabnum, 0) else setpart.childsnum * coalesce(ab.nabnum, 0) end)!=0 ");
          sSql.append(" or sum(case when setpart.pk_invmandocset is null then coalesce(ab.nabmny, 0) else setpart.childsnum * setpart.partpercent*coalesce(ab.nabmny, 0) end)!=0 ");
          sSql.append(" or sum(case when setpart.pk_invmandocset is null then coalesce(ab.nabplanedmny, 0) else setpart.childsnum * setpart.partpercent *coalesce(ab.nabplanedmny,0) end)!=0 ");
        } else {
          sSql.append(" having sum(ab.nabnum)!=0 or sum(ab.nabmny)!=0 or sum(ab.nabplanedmny)!=0 ");
        }

        sSql.append(") ");

        cbo.execData(sSql.toString());
      }

      sSql.setLength(0);

      if (sWherePeriod[1].length() > 0)
      {
        sGroup = null;

        sSql.append(" insert into " + tempTable);
        sSql.append(" ( ");
        sSql.append(SQLStringUtil.getDimensionSQL(queryVO, 0, false, false));
        sSql.append(", nbeginnum, nbeginmny, nbeginplanedmny, nbeginvarymny");
        if (queryVO.isShowAssistant()) {
          sSql.append(", nbeginassistnum");
        }
        sSql.append(" ) (");

        if (queryVO.isShowSetPart()) {
          sSql.append(" select " + SQLStringUtil.getDimensionSQL(queryVO, 3, false, false));
          sSql.append(", sum(case when m.fdispatchflag = 0 ");
          sSql.append(" then (case when setpart.pk_invmandocset is null then coalesce(m.ninnum, 0) else setpart.childsnum * coalesce(m.ninnum, 0) end) ");
          sSql.append(" else -(case when setpart.pk_invmandocset is null then coalesce(m.noutnum, 0) else setpart.childsnum * coalesce(m.noutnum, 0) end) end),");
          sSql.append(" sum(case when m.fdispatchflag=0 ");
          sSql.append(" then (case when setpart.pk_invmandocset is null then coalesce(m.ninmny, 0) else setpart.childsnum * setpart.partpercent*coalesce(m.ninmny, 0) end) ");
          sSql.append(" else -(case when setpart.pk_invmandocset is null then coalesce(m.noutmny, 0) else setpart.childsnum * setpart.partpercent*coalesce(m.noutmny, 0) end) end),");
          sSql.append(" sum(case when m.fdispatchflag=0 ");
          sSql.append(" then (case when setpart.pk_invmandocset is null then coalesce(m.ninplanedmny, 0) else setpart.childsnum * setpart.partpercent*coalesce(m.ninplanedmny, 0) end) ");
          sSql.append(" else -(case when setpart.pk_invmandocset is null then coalesce(m.noutplanedmny, 0) else setpart.childsnum * setpart.partpercent*coalesce(m.noutplanedmny, 0) end) end),");
          sSql.append(" sum(case when m.fdispatchflag=0 ");
          sSql.append(" then (case when setpart.pk_invmandocset is null then coalesce(m.ninvarymny, 0) else setpart.childsnum * setpart.partpercent*coalesce(m.ninvarymny, 0) end) ");
          sSql.append(" else -(case when setpart.pk_invmandocset is null then coalesce(m.noutvarymny, 0) else setpart.childsnum * setpart.partpercent*coalesce(m.noutvarymny, 0) end) end) ");

          if (queryVO.isShowAssistant()) {
            sSql.append(", sum(case when m.fdispatchflag=0  ");
            sSql.append(" then (case when setpart.pk_invmandocset is null then coalesce(m.ninassistnum, 0) else setpart.childsnum * coalesce(m.ninassistnum, 0) end) ");
            sSql.append(" else -(case when setpart.pk_invmandocset is null then coalesce(m.noutassistnum, 0) else setpart.childsnum * coalesce(m.noutassistnum, 0) end) end) ");
          }

          sGroup = SQLStringUtil.getDimensionSQL(queryVO, 3, false, true);
        }
        else {
          sSql.append(" select " + SQLStringUtil.getDimensionSQL(queryVO, 1, false, false));
          sSql.append(", sum(case when m.fdispatchflag=0 then m.ninnum else -m.noutnum end), ");
          sSql.append(" sum(case when m.fdispatchflag=0 then m.ninmny else -m.noutmny end), ");
          sSql.append(" sum(case when m.fdispatchflag=0 then m.ninplanedmny else -m.noutplanedmny end), ");
          sSql.append(" sum(case when m.fdispatchflag=0 then m.ninvarymny else -m.noutvarymny end) ");

          if (queryVO.isShowAssistant()) {
            sSql.append(", sum(case when m.fdispatchflag=0 then m.ninassistnum else -m.noutassistnum end)");
          }

          sGroup = SQLStringUtil.getDimensionSQL(queryVO, 1, false, true);
        }

        sSql.append(" from ia_monthinout m ");

        sJoin = SQLStringUtil.getJoinSQL(queryVO, "m");
        sSql.append(sJoin[0]);
        sSql.append(sJoin[1]);

        sSql.append(" where (");
        sSql.append(sWherePeriod[1] + ") ");
        temp = queryVO.getWhere();
        temp = removePeriod(temp);

        if ((temp.length() > 0) && (queryVO.getDataPowerSql() != null) && (queryVO.getDataPowerSql().trim().length() > 0))
        {
          temp = temp + " and " + queryVO.getDataPowerSql();
        } else if ((queryVO.getDataPowerSql() != null) && (queryVO.getDataPowerSql().trim().length() > 0))
        {
          temp = temp + queryVO.getDataPowerSql();
        }

        if (temp.length() > 0) {
          sSql.append(" and " + SQLStringUtil.replaceQueryStr(temp, "m"));
        }
        sSql.append(" group by " + sGroup);

        if (queryVO.isShowSetPart()) {
          sSql.append(" having sum(case when m.fdispatchflag = 0 ");
          sSql.append(" then (case when setpart.pk_invmandocset is null then coalesce(m.ninnum, 0) else setpart.childsnum * coalesce(m.ninnum, 0) end) ");
          sSql.append(" else -(case when setpart.pk_invmandocset is null then coalesce(m.noutnum, 0) else setpart.childsnum * coalesce(m.noutnum, 0) end) end)!=0 ");
          sSql.append(" or sum(case when m.fdispatchflag=0 ");
          sSql.append(" then (case when setpart.pk_invmandocset is null then coalesce(m.ninmny, 0) else setpart.childsnum * setpart.partpercent*coalesce(m.ninmny, 0) end) ");
          sSql.append(" else -(case when setpart.pk_invmandocset is null then coalesce(m.noutmny, 0) else setpart.childsnum * setpart.partpercent*coalesce(m.noutmny, 0) end) end)!=0 ");
          sSql.append(" or sum(case when m.fdispatchflag=0 ");
          sSql.append(" then (case when setpart.pk_invmandocset is null then coalesce(m.ninplanedmny, 0) else setpart.childsnum * setpart.partpercent*coalesce(m.ninplanedmny, 0) end) ");
          sSql.append(" else -(case when setpart.pk_invmandocset is null then coalesce(m.noutplanedmny, 0) else setpart.childsnum * setpart.partpercent*coalesce(m.noutplanedmny, 0) end) end)!=0 ");
        }
        else
        {
          sSql.append(" having sum(case when m.fdispatchflag=0 then m.ninnum else -m.noutnum end)!=0 ");
          sSql.append(" or sum(case when m.fdispatchflag=0 then m.ninmny else -m.noutmny end)!=0 ");
          sSql.append(" or sum(case when m.fdispatchflag=0 then m.ninplanedmny else -m.noutplanedmny end)!=0 ");
        }

        sSql.append(") ");

        cbo.execData(sSql.toString());
      }

      sSql.setLength(0);

      if (sWherePeriod[2].length() > 0)
      {
        sGroup = null;

        sSql.append(" insert into " + tempTable);
        sSql.append(" ( ");
        sSql.append(SQLStringUtil.getDimensionSQL(queryVO, 0, false, false));
        sSql.append(", nbeginnum, nbeginmny, nbeginplanedmny, nbeginvarymny");
        if (queryVO.isShowAssistant()) {
          sSql.append(", nbeginassistnum");
        }
        sSql.append(" ) (");

        if (queryVO.isShowSetPart()) {
          sSql.append(" select " + SQLStringUtil.getDimensionSQL(queryVO, 4, false, false));
          sSql.append(", sum( case when h.fdispatchflag=0 then (case when setpart.pk_invmandocset is null then coalesce(b.nnumber, 0) else setpart.childsnum * coalesce(b.nnumber, 0) end)");
          sSql.append(" else -(case when setpart.pk_invmandocset is null then coalesce(b.nnumber, 0) else setpart.childsnum * coalesce(b.nnumber, 0) end) end ),");
          sSql.append(" sum( case when h.fdispatchflag=0 then (case when setpart.pk_invmandocset is null then coalesce(b.nmoney, 0) else setpart.childsnum * setpart.partpercent*coalesce(b.nmoney, 0) end)");
          sSql.append(" else -(case when setpart.pk_invmandocset is null then coalesce(b.nmoney, 0) else setpart.childsnum * setpart.partpercent*coalesce(b.nmoney, 0) end) end ),");
          sSql.append(" sum( case when h.fdispatchflag=0 then(case when setpart.pk_invmandocset is null then coalesce(b.nplanedmny, 0) else setpart.childsnum * setpart.partpercent*coalesce(b.nplanedmny, 0) end)");
          sSql.append(" else -(case when setpart.pk_invmandocset is null then coalesce(b.nplanedmny, 0) else setpart.childsnum * setpart.partpercent*coalesce(b.nplanedmny, 0) end) end ),");
          sSql.append(" sum( case when h.fdispatchflag=0 then(case when setpart.pk_invmandocset is null then coalesce(b.ninvarymny, 0) else setpart.childsnum * setpart.partpercent*coalesce(b.ninvarymny, 0) end)");
          sSql.append(" else -(case when setpart.pk_invmandocset is null then coalesce(b.noutvarymny, 0) else setpart.childsnum * setpart.partpercent*coalesce(b.noutvarymny, 0) end) end ) ");

          if (queryVO.isShowAssistant()) {
            sSql.append(", sum( case when h.fdispatchflag=0 then(case when setpart.pk_invmandocset is null then coalesce(b.nassistnum, 0) else setpart.childsnum * coalesce(b.nassistnum, 0) end)");
            sSql.append(" else -(case when setpart.pk_invmandocset is null then coalesce(b.nassistnum, 0) else setpart.childsnum * coalesce(b.nassistnum, 0) end) end ) ");
          }

          sGroup = SQLStringUtil.getDimensionSQL(queryVO, 4, false, true);
        } else {
          sSql.append(" select " + SQLStringUtil.getDimensionSQL(queryVO, 2, false, false));
          sSql.append(", sum(case when h.fdispatchflag=0 then coalesce(b.nnumber,0) else -coalesce(b.nnumber,0) end), ");
          sSql.append(" sum(case when h.fdispatchflag=0 then coalesce(b.nmoney,0) else -coalesce(b.nmoney,0) end), ");
          sSql.append(" sum(case when h.fdispatchflag=0 then coalesce(b.nplanedmny,0) else -coalesce(b.nplanedmny,0) end), ");
          sSql.append(" sum(case when h.fdispatchflag=0 then coalesce(b.ninvarymny,0) else -coalesce(b.noutvarymny,0) end) ");

          if (queryVO.isShowAssistant()) {
            sSql.append(", sum(case when h.fdispatchflag=0 then coalesce(b.nassistnum,0) else -coalesce(b.nassistnum,0) end) ");
          }

          sGroup = SQLStringUtil.getDimensionSQL(queryVO, 2, false, true);
        }

        sJoin = SQLStringUtil.getJoinSQL(queryVO, "v");
        sSql.append(" from ia_bill h " + sJoin[0]);
        sSql.append(" , ia_bill_b b " + sJoin[1]);

        sSql.append(" where h.cbillid=b.cbillid and h.dr=0 and b.dr=0 and h.bdisableflag = 'N' ");
        sSql.append(" and (h.cbiztypeid is null or h.cbiztypeid not in (" + queryVO.sFQSK + "," + queryVO.sWTDX + ") ");
        sSql.append(" or b.csourcebilltypecode is null or b.csourcebilltypecode <> '32') ");
        sSql.append(" and (" + sWherePeriod[2] + ") ");
        temp = queryVO.getWhere();
        temp = removePeriod(temp);

        if ((temp.length() > 0) && (queryVO.getDataPowerSql() != null) && (queryVO.getDataPowerSql().trim().length() > 0))
        {
          temp = temp + " and " + queryVO.getDataPowerSql();
        } else if ((queryVO.getDataPowerSql() != null) && (queryVO.getDataPowerSql().trim().length() > 0))
        {
          temp = temp + queryVO.getDataPowerSql();
        }

        if (temp.length() > 0) {
          sSql.append(" and " + SQLStringUtil.replaceQueryStr(temp, "h"));
        }
        sSql.append(" group by " + sGroup);

        if (queryVO.isShowSetPart()) {
          sSql.append(" having sum( case when h.fdispatchflag=0 then (case when setpart.pk_invmandocset is null then coalesce(b.nnumber, 0) else setpart.childsnum * coalesce(b.nnumber, 0) end)");
          sSql.append(" else -(case when setpart.pk_invmandocset is null then coalesce(b.nnumber, 0) else setpart.childsnum * coalesce(b.nnumber, 0) end) end )!=0 ");
          sSql.append(" or sum( case when h.fdispatchflag=0 then (case when setpart.pk_invmandocset is null then coalesce(b.nmoney, 0) else setpart.childsnum * setpart.partpercent*coalesce(b.nmoney, 0) end)");
          sSql.append(" else -(case when setpart.pk_invmandocset is null then coalesce(b.nmoney, 0) else setpart.childsnum * setpart.partpercent*coalesce(b.nmoney, 0) end) end )!=0 ");
          sSql.append(" or sum( case when h.fdispatchflag=0 then(case when setpart.pk_invmandocset is null then coalesce(b.nplanedmny, 0) else setpart.childsnum * setpart.partpercent*coalesce(b.nplanedmny, 0) end)");
          sSql.append(" else -(case when setpart.pk_invmandocset is null then coalesce(b.nplanedmny, 0) else setpart.childsnum * setpart.partpercent*coalesce(b.nplanedmny, 0) end) end )!=0 ");
        }
        else {
          sSql.append(" having sum(case when h.fdispatchflag=0 then coalesce(b.nnumber,0) else -coalesce(b.nnumber,0) end)!=0 ");
          sSql.append(" or sum(case when h.fdispatchflag=0 then coalesce(b.nmoney,0) else -coalesce(b.nmoney,0) end)!=0 ");
          sSql.append(" or sum(case when h.fdispatchflag=0 then coalesce(b.nplanedmny,0) else -coalesce(b.nplanedmny,0) end)!=0 ");
        }

        sSql.append(") ");

        cbo.execData(sSql.toString());
      }
      sSql.setLength(0);

      if (sWherePeriod[3].length() > 0)
      {
        sGroup = null;

        sSql.append(" insert into " + tempTable);
        sSql.append(" ( ");
        sSql.append(SQLStringUtil.getDimensionSQL(queryVO, 0, false, false));
        sSql.append(", nbeginnum, nbeginmny, nbeginplanedmny, nbeginvarymny");
        if (queryVO.isShowAssistant()) {
          sSql.append(", nbeginassistnum");
        }
        sSql.append(" ) (");

        if (queryVO.isShowSetPart()) {
          sSql.append(" select " + SQLStringUtil.getDimensionSQL(queryVO, 4, false, false));
          sSql.append(", sum( case when h.fdispatchflag=0 then(case when setpart.pk_invmandocset is null then coalesce(b.nnumber, 0) else setpart.childsnum * coalesce(b.nnumber, 0) end)");
          sSql.append(" else -(case when setpart.pk_invmandocset is null then coalesce(b.nnumber, 0) else setpart.childsnum * coalesce(b.nnumber, 0) end) end ),");
          sSql.append(" sum( case when h.fdispatchflag=0 then(case when setpart.pk_invmandocset is null then coalesce(b.nmoney, 0) else setpart.childsnum * setpart.partpercent*coalesce(b.nmoney, 0) end)");
          sSql.append(" else -(case when setpart.pk_invmandocset is null then coalesce(b.nmoney, 0) else setpart.childsnum * setpart.partpercent*coalesce(b.nmoney, 0) end) end ),");
          sSql.append(" sum( case when h.fdispatchflag=0 then(case when setpart.pk_invmandocset is null then coalesce(b.nplanedmny, 0) else setpart.childsnum * setpart.partpercent*coalesce(b.nplanedmny, 0) end)");
          sSql.append(" else -(case when setpart.pk_invmandocset is null then coalesce(b.nplanedmny, 0) else setpart.childsnum * setpart.partpercent*coalesce(b.nplanedmny, 0) end) end ),");
          sSql.append(" sum( case when h.fdispatchflag=0 then(case when setpart.pk_invmandocset is null then coalesce(b.ninvarymny, 0) else setpart.childsnum * setpart.partpercent*coalesce(b.ninvarymny, 0) end)");
          sSql.append(" else -(case when setpart.pk_invmandocset is null then coalesce(b.noutvarymny, 0) else setpart.childsnum * setpart.partpercent*coalesce(b.noutvarymny, 0) end) end ) ");

          if (queryVO.isShowAssistant()) {
            sSql.append(", sum( case when h.fdispatchflag=0 then(case when setpart.pk_invmandocset is null then coalesce(b.nassistnum, 0) else setpart.childsnum * coalesce(b.nassistnum, 0) end)");
            sSql.append(" else -(case when h.fdispatchflag=0 then(case when setpart.pk_invmandocset is null then coalesce(b.nassistnum, 0) else setpart.childsnum * coalesce(b.nassistnum, 0) end) end ) ");
          }

          sGroup = SQLStringUtil.getDimensionSQL(queryVO, 4, false, true);
        }
        else {
          sSql.append(" select " + SQLStringUtil.getDimensionSQL(queryVO, 2, false, false));
          sSql.append(", sum(case when h.fdispatchflag=0 then coalesce(b.nnumber,0) else -coalesce(b.nnumber,0) end), ");
          sSql.append(" sum(case when h.fdispatchflag=0 then coalesce(b.nmoney,0) else -coalesce(b.nmoney,0) end), ");
          sSql.append(" sum(case when h.fdispatchflag=0 then coalesce(b.nplanedmny,0) else -coalesce(b.nplanedmny,0) end), ");
          sSql.append(" sum(case when h.fdispatchflag=0 then coalesce(b.ninvarymny,0) else -coalesce(b.noutvarymny,0) end) ");

          if (queryVO.isShowAssistant()) {
            sSql.append(", sum(case when h.fdispatchflag=0 then coalesce(b.nassistnum,0) else -coalesce(b.nassistnum,0) end) ");
          }

          sGroup = SQLStringUtil.getDimensionSQL(queryVO, 2, false, true);
        }

        sJoin = SQLStringUtil.getJoinSQL(queryVO, "v");
        sSql.append(" from ia_bill h " + sJoin[0]);
        sSql.append(" , ia_bill_b b " + sJoin[1]);

        sSql.append(" where h.cbillid=b.cbillid and h.dr=0 and b.dr=0 and h.bdisableflag = 'N' ");
        sSql.append(" and (h.cbiztypeid is null or h.cbiztypeid not in (" + queryVO.sFQSK + "," + queryVO.sWTDX + ") ");
        sSql.append(" or b.csourcebilltypecode is null or b.csourcebilltypecode <> '32') ");
        sSql.append(" and (" + sWherePeriod[3] + ") ");
        temp = queryVO.getWhere();
        temp = removePeriod(temp);

        if ((temp.length() > 0) && (queryVO.getDataPowerSql() != null) && (queryVO.getDataPowerSql().trim().length() > 0))
        {
          temp = temp + " and " + queryVO.getDataPowerSql();
        } else if ((queryVO.getDataPowerSql() != null) && (queryVO.getDataPowerSql().trim().length() > 0))
        {
          temp = temp + queryVO.getDataPowerSql();
        }

        if (temp.length() > 0) {
          sSql.append("and " + SQLStringUtil.replaceQueryStr(temp, "h"));
        }
        sSql.append(" group by " + sGroup);

        if (queryVO.isShowSetPart()) {
          sSql.append(" having sum( case when h.fdispatchflag=0 then(case when setpart.pk_invmandocset is null then coalesce(b.nnumber, 0) else setpart.childsnum * coalesce(b.nnumber, 0) end)");
          sSql.append(" else -(case when setpart.pk_invmandocset is null then coalesce(b.nnumber, 0) else setpart.childsnum * coalesce(b.nnumber, 0) end) end )!=0 ");
          sSql.append(" or sum( case when h.fdispatchflag=0 then(case when setpart.pk_invmandocset is null then coalesce(b.nmoney, 0) else setpart.childsnum * setpart.partpercent*coalesce(b.nmoney, 0) end)");
          sSql.append(" else -(case when setpart.pk_invmandocset is null then coalesce(b.nmoney, 0) else setpart.childsnum * setpart.partpercent*coalesce(b.nmoney, 0) end) end )!=0 ");
          sSql.append(" or sum( case when h.fdispatchflag=0 then(case when setpart.pk_invmandocset is null then coalesce(b.nplanedmny, 0) else setpart.childsnum * setpart.partpercent*coalesce(b.nplanedmny, 0) end)");
          sSql.append(" else -(case when setpart.pk_invmandocset is null then coalesce(b.nplanedmny, 0) else setpart.childsnum * setpart.partpercent*coalesce(b.nplanedmny, 0) end) end )!=0 ");
        } else {
          sSql.append(" having sum(case when h.fdispatchflag=0 then coalesce(b.nnumber,0) else -coalesce(b.nnumber,0) end)!=0 ");
          sSql.append(" or sum(case when h.fdispatchflag=0 then coalesce(b.nmoney,0) else -coalesce(b.nmoney,0) end)!=0 ");
          sSql.append(" or sum(case when h.fdispatchflag=0 then coalesce(b.nplanedmny,0) else -coalesce(b.nplanedmny,0) end)!=0 ");
        }

        sSql.append(") ");
        cbo.execData(sSql.toString());
      }
    }

    return this.nextAlgo.exceReportAlgoLineReturnVOS(queryVO, tempTable);
  }

  private String[] parseCorpAndPeriod(QueryVO query)
    throws BusinessException
  {
    CommonDataImpl cbo = new CommonDataImpl();

    StringBuffer strForAB = new StringBuffer();
    StringBuffer strForPeriod = new StringBuffer();
    StringBuffer strForBill = new StringBuffer();

    String[] corps = query.getPk_Corps();
    String corp;
    for (int i = 0; i < corps.length; ++i)
    {
      corp = corps[i];

      String nowPeriod = cbo.getPeriod(corp, query.getDate()[0]);

      String unAccPeriod = cbo.getUnClosePeriod(corp);

      if (unAccPeriod.equalsIgnoreCase("00")) {
        unAccPeriod = cbo.getStartPeriod(corp);
      }

      String Period = null;
      String perPeriod = null;

      if (unAccPeriod.compareTo(nowPeriod) > 0)
        Period = nowPeriod;
      else {
        Period = unAccPeriod;
      }

      perPeriod = cbo.getPerviousPeriod(corp, Period);

      String lastABPeriod = PeriodController.getLastABPeriod(corp, perPeriod);

      if (strForAB.length() > 0) {
        strForAB.append(" or ");
      }
      strForAB.append("(ab.pk_corp='" + corp + "' ");
      strForAB.append(" and ab.caccountyear='" + lastABPeriod.substring(0, lastABPeriod.indexOf("-")) + "' ");
      strForAB.append(" and ab.caccountmonth='" + lastABPeriod.substring(lastABPeriod.indexOf("-") + 1) + "') ");

      String nextPeriod = cbo.getNextPeriod(corp, lastABPeriod);

      if (!(perPeriod.equalsIgnoreCase(lastABPeriod)))
      {
        if (strForPeriod.length() > 0) {
          strForPeriod.append(" or ");
        }

        strForPeriod.append(" (m.pk_corp='" + corp + "' ");
        strForPeriod.append(" and m.caccountyear='" + perPeriod.substring(0, perPeriod.indexOf(45)) + "' ");

        if (perPeriod.equalsIgnoreCase(nextPeriod)) {
          strForPeriod.append(" and m.caccountmonth='" + nextPeriod.substring(nextPeriod.indexOf("-") + 1).trim() + "' )");
        } else {
          strForPeriod.append(" and m.caccountmonth>='" + nextPeriod.substring(nextPeriod.indexOf("-") + 1).trim() + "' ");
          strForPeriod.append(" and m.caccountmonth<='" + perPeriod.substring(perPeriod.indexOf(45) + 1).trim() + "') ");
        }

      }

      if ((nowPeriod.substring(5, 7).equalsIgnoreCase("00")) || ((Period.equalsIgnoreCase(nowPeriod)) && (cbo.getMonthBeginDate(corp, nowPeriod).toString().equalsIgnoreCase(query.getDate()[0])))) {
        continue;
      }
      if (strForBill.length() > 0) {
        strForBill.append(" or ");
      }

      if (Period.equalsIgnoreCase(nowPeriod)) {
        strForBill.append(" (h.pk_corp='" + corp + "' ");
        strForBill.append(" and h.caccountyear='" + nowPeriod.substring(0, nowPeriod.indexOf(45)) + "' ");
        strForBill.append(" and h.caccountmonth='" + nowPeriod.substring(nowPeriod.indexOf(45) + 1) + "' ");
        strForBill.append(" and b.iauditsequence>0 ");
        strForBill.append(" and b.dauditdate>='" + cbo.getMonthBeginDate(corp, nowPeriod).toString() + "' and b.dauditdate<'" + query.getDate()[0] + "' )");
      } else {
        strForBill.append(" (h.pk_corp='" + corp + "' ");
        strForBill.append(" and b.iauditsequence>0  and ");
        strForBill.append(SQLStringUtil.handlePeriod(Period, nowPeriod, "h"));
        strForBill.append(" and b.dauditdate>='" + cbo.getMonthBeginDate(corp, Period).toString() + "' and b.dauditdate<'" + query.getDate()[0] + "' )");
      }

    }

    String[] result = new String[4];
    result[0] = "";
    result[1] = "";
    result[2] = "";
    result[3] = "";

    if (strForAB.length() > 0) {
      result[0] = strForAB.toString();
    }

    if (strForPeriod.length() > 0) {
      result[1] = strForPeriod.toString();
    }

    if (strForBill.length() > 0) {
      result[2] = strForBill.toString();
    }

    if (query.QueryType.equalsIgnoreCase(QueryVO.ALL)) {
      corp = "";

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
      result[3] = corp + " and iauditsequence=-1 and dbilldate<'" + query.getDate()[0] + "' ";
    }

    return result;
  }

  String removePeriod(String str)
  {
    StringBuffer buf = new StringBuffer(str);
    while (buf.indexOf("v.caccountmonth") > 0)
    {
      int gate = buf.indexOf("v.caccountmonth");

      if ((buf.indexOf(">=", gate) > 0) && (gate + 20 > buf.indexOf(">=", gate))) {
        buf.replace(gate, buf.indexOf(">=", gate) + 12, "1=1");
      }
      if ((buf.indexOf("<=", gate) > 0) && (gate + 20 > buf.indexOf("<=", gate))) {
        buf.replace(gate, buf.indexOf("<=", gate) + 12, "1=1");
      }
      if ((buf.indexOf(">", gate) > 0) && (gate + 20 > buf.indexOf(">", gate))) {
        buf.replace(gate, buf.indexOf(">", gate) + 11, "1=1");
      }
      if ((buf.indexOf("<", gate) > 0) && (gate + 20 > buf.indexOf("<", gate))) {
        buf.replace(gate, buf.indexOf("<", gate) + 11, "1=1");
      }
      if ((buf.indexOf("=", gate) > 0) && (gate + 20 > buf.indexOf("=", gate))) {
        buf.replace(gate, buf.indexOf("=", gate) + 11, "1=1");
      }

    }

    return buf.toString();
  }
}