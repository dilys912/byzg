package nc.bs.ic.pub.monthsum;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.naming.NamingException;
import nc.bd.accperiod.AccountCalendar;
import nc.bs.ic.pub.GenMethod;
import nc.bs.pub.DataManageObject;
import nc.bs.pub.SystemException;
import nc.bs.scm.pub.bill.SQLUtil;
import nc.impl.ic.ic008.FixOnhandnumDMO;
import nc.vo.bd.period2.AccperiodmonthVO;
import nc.vo.ic.pub.bill.QryConditionVO;
import nc.vo.ic.pub.monthsum.MonthMode;
import nc.vo.ic.pub.monthsum.MonthVO;
import nc.vo.ic.pub.monthsum.SqlHelper;
import nc.vo.ic.pub.monthsum.SqlMonthSum;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.pub.SCMEnv;

public class MonthQuery extends DataManageObject
{
  private QryConditionCtrl m_ctrl = null;

  private UFDate m_endDate = null;

  private UFDate m_startDate = null;

  private boolean m_bBizDateAccoutDate = true;

  private int m_iQueryType = -1;
  public static final int Borrow_Ref_Book = 1;
  public static final int Len_Ref_Book = 2;
  public static final int Waste_Ref_Book = 3;
  public static final int Machine_Ref_Book = 4;
  public static final int Account_Detail = 5;
  private static final int Xcl_Qichu = -1;

  public MonthQuery()
    throws NamingException, SystemException
  {
  }

  public void test_invman(String[] cinvmanids, UFDate date)
    throws Exception
  {
    ConditionVO vo = new ConditionVO();
    vo.setFieldCode("cinventoryid");
    vo.setOperaCode("in");
    vo.setValue(SQLUtil.getStr(cinvmanids));

    QryConditionCtrl cont = new QryConditionCtrl(new ConditionVO[] { vo });
    cont.setPureSelectGroupField(new String[] { "pk_corp", "pk_calbody", "cwarehouseid", "cinventoryid", "cvendorid", "castunitid" });

    setQueryType(1);

    qryQiChu_Kernel(cont, date, null);
  }

  public void testAdjustXCL(ConditionVO[] voaCond)
    throws Exception
  {
    QryConditionCtrl ctrl = new QryConditionCtrl(voaCond);

    ctrl.setPureSelectGroupField(new String[] { "pk_corp", "pk_calbody", "cwarehouseid", "cspaceid", "cinventoryid", "cinvbasid", "vbatchcode", "castunitid", "cvendorid", "hsl", "vfree1", "vfree2", "vfree3", "vfree4", "vfree5", "vfree6", "vfree7", "vfree8", "vfree9", "vfree10" });

    ctrl.setWhereField(new String[] { "ccalbodyid", "astunitid" }, new String[] { "pk_calbody", "castunitid" });

    ctrl.setVOAndSumField(new String[] { "nnum", "nastnum", "ngrossnum" }, new String[] { "nonhandnum", "nonhandassistnum", "ngrossnum" });

    qryQiChu_Kernel(ctrl, new UFDate("2005-02-01"), "nc.vo.ic.pub.bill.OnhandnumItemVO");
  }

  public void test641Query(QryConditionVO voCond)
    throws BusinessException
  {
    QryConditionCtrl ctrl = new QryConditionCtrl((ConditionVO[])(ConditionVO[])voCond.getParam(10));

    ctrl.setPureSelectGroupField(new String[] { "pk_corp", "pk_calbody", "cwarehouseid", "cinventoryid" });

    ctrl.setVOAndSumField(null, new String[] { "nonhandnum" });
    qryQiChu_Kernel(ctrl, new UFDate(System.currentTimeMillis()), null);
  }

  public void setQueryType(int iQueryType)
  {
    this.m_iQueryType = iQueryType;
  }

  private String qryQiChuFromIC(String dateWhere)
  {
    boolean isIncludeInit = true;

    if (dateWhere == null) {
      String dateFld = this.m_bBizDateAccoutDate == true ? "dbizdate" : "daccountdate";

      if (this.m_endDate == null) {
        throw new IllegalArgumentException(" call qryQiChuFromIC : param is not illegal!");
      }

      dateWhere = "(" + dateFld + "<'" + this.m_endDate.toString() + "' OR (" + dateFld + ">='" + this.m_endDate.toString() + "' and h.cbilltypecode in ('40','41','42','43','44')))";
    }
    else {
      isIncludeInit = false;
    }

    String sql_skeleton = "SELECT pure_fld, sum_fld FROM ic_general_h h inner join ic_general_b b  on h.cgeneralhid=b.cgeneralhid  join_sql WHERE  dateWhere and cond_where and  fixed_where and h.dr=0 and b.dr=0 GROUP BY cond_group";
    HashMap m = new HashMap();

    String s1 = this.m_ctrl.getPureSelectField("ic");
    if (s1.indexOf("cspaceid") != -1) {
      sql_skeleton = "SELECT pure_fld, sum_fld FROM ic_general_h h inner join ic_general_b b on h.cgeneralhid=b.cgeneralhid left outer join ic_general_bb1 bb1 on b.cgeneralbid=bb1.cgeneralbid and bb1.dr=0 join_sql WHERE dateWhere and cond_where and h.dr=0 and b.dr=0 GROUP BY cond_group";
    }
    m.put("pure_fld", s1);

    String s2 = null;
    if (s1.indexOf("cspaceid") != -1) {
      s2 = "sum(COALESCE (bb1.ninspacenum, b.ninnum, 0.0) -COALESCE (bb1.noutspacenum, b.noutnum, 0.0) )as nonhandnum, sum(COALESCE (bb1.ninspaceassistnum, b.ninassistnum, 0.0)-COALESCE (bb1.noutspaceassistnum, b.noutassistnum, 0.0)) as nonhandassistnum,  sum(COALESCE (bb1.ningrossnum, b.ningrossnum, 0.0)-COALESCE (bb1.noutgrossnum, b.noutgrossnum, 0.0)) as ngrossnum  ";
    }
    else
    {
      s2 = "sum(isnull(ninnum,0.0) ";
      if (this.m_iQueryType == 3)
        s2 = s2 + "- case when cbilltypecode='4O' then (-1)*isnull(noutnum,0.0) else isnull(noutnum,0.0) end ";
      else {
        s2 = s2 + "-isnull(noutnum,0.0)";
      }
      if (this.m_iQueryType == 5)
      {
        s2 = s2 + "+case when cbilltypecode in ('41','49') then isnull(ntranoutnum,0.0)-isnull(ninnum,0.0) when cbilltypecode in ('42','4H') then (-1)*isnull(ntranoutnum,0.0)+isnull(noutnum,0.0) else 0.0 end ";
      }

      s2 = s2 + " ) nonhandnum, sum(isnull(ninassistnum,0.0)-isnull(noutassistnum,0.0)";

      if (this.m_iQueryType == 5)
      {
        s2 = s2 + "+case when cbilltypecode in ('41','49') then isnull(ntranoutastnum,0.0)-isnull(ninassistnum,0.0) when cbilltypecode in ('42','4H') then (-1)*isnull(ntranoutastnum,0.0)+isnull(noutassistnum,0.0) else 0.0 end ";
      }

      s2 = s2 + ") nonhandassistnum," + "sum(isnull(ningrossnum,0.0)-isnull(noutgrossnum,0.0)) ngrossnum";
    }

    m.put("sum_fld", s2);

    String s3 = this.m_ctrl.getJoinSql("ic");
    m.put("join_sql", s3);

    String s4 = this.m_ctrl.getWhereSql("ic");
    m.put("cond_where", s4);

    m.put("cond_group", s1);

    m.put("dateWhere", dateWhere);

    String fixedWhere = getFixedWhere("h");
    if (this.m_iQueryType != 5) {
      fixedWhere = fixedWhere + " and (h.cbiztype IN (SELECT pk_busitype FROM bd_busitype WHERE verifyrule <> 'J' and verifyrule <> 'C') OR h.cbiztype IS NULL) ";
    }

    if (!isIncludeInit) {
      fixedWhere = fixedWhere + " and h.cbilltypecode not in ('40','41','42','43','44') ";
    }
    m.put("fixed_where", fixedWhere);

    sql_skeleton = SqlHelper.replace(sql_skeleton, m);
    sql_skeleton = SqlHelper.deal_and(SqlHelper.deal_quote(sql_skeleton));
    return sql_skeleton;
  }

  private String qryBenQiFromIC(String dateWhere)
  {
    if (dateWhere == null) {
      String dateFld = this.m_bBizDateAccoutDate == true ? "dbizdate" : "daccountdate";

      if (this.m_endDate == null) {
        throw new IllegalArgumentException(" call qryQiChu_IC_Pure : param is not illegal!");
      }

      dateWhere = dateFld + "<='" + this.m_endDate.toString() + "' ";
      if (this.m_startDate != null) {
        dateWhere = dateWhere + " and " + dateFld + ">='" + this.m_startDate.toString() + "' ";
      }

    }

    String sql_skeleton = "SELECT pure_fld, sum_fld FROM ic_general_h h inner join ic_general_b b  on h.cgeneralhid=b.cgeneralhid  join_sql WHERE  dateWhere and cond_where and  fixed_where and h.dr=0 and b.dr=0 GROUP BY cond_group";
    HashMap m = new HashMap();

    String s1 = this.m_ctrl.getPureSelectField("ic");
    if (s1.indexOf("cspaceid") != -1) {
      sql_skeleton = "SELECT pure_fld, sum_fld FROM ic_general_h h inner join ic_general_b b on h.cgeneralhid=b.cgeneralhid left outer join ic_general_bb1 bb1 on b.cgeneralbid=bb1.cgeneralbid and bb1.dr=0 join_sql WHERE dateWhere and cond_where and h.dr=0 and b.dr=0  GROUP BY cond_group";
    }
    m.put("pure_fld", s1);

    String s2 = null;

    if (s1.indexOf("cspaceid") != -1) {
      s2 = " sum(COALESCE (bb1.ninspacenum, b.ninnum, 0.0)) as ninnum ,sum(COALESCE (bb1.noutspacenum, b.noutnum, 0.0)) as noutnum, sum(COALESCE (bb1.ninspaceassistnum, b.ninassistnum, 0.0)) as ninassistnum,sum(COALESCE (bb1.noutspaceassistnum, b.noutassistnum, 0.0)) as noutassistnum,  sum(COALESCE (bb1.ningrossnum, b.ningrossnum, 0.0)) as ningrossnum,sum(COALESCE (bb1.noutgrossnum, b.noutgrossnum, 0.0)) as noutgrossnum ,  0.0 as ntranoutnum,0.0 as nretnum ";
    }
    else
    {
      s2 = "sum(isnull(ninnum,0.0)";

      if (this.m_iQueryType == 5)
      {
        s2 = s2 + "+case when cbilltypecode in ('41','49') then isnull(ntranoutnum,0.0)-isnull(ninnum,0.0) else 0.0 end ";
      }

      s2 = s2 + ") as ninnum ,sum(isnull(noutnum,0.0)";
      if (this.m_iQueryType == 5)
      {
        s2 = s2 + "+case when cbilltypecode in ('42','4H') then (-1)*isnull(ntranoutnum,0.0)-isnull(noutnum,0.0) else 0.0  end ";
      }

      s2 = s2 + ") as noutnum, " + "sum(isnull(ninassistnum,0.0)) as ninassistnum,sum(isnull(noutassistnum,0.0)) as noutassistnum," + "sum(isnull(ningrossnum,0.0)) as ningrossnum,sum(isnull(noutgrossnum,0.0)) as  noutgrossnum," + "sum(isnull(ntranoutnum,0.0)) as ntranoutnum,sum(isnull(nretnum,0.0)) as nretnum ";
    }

    m.put("sum_fld", s2);

    String s3 = this.m_ctrl.getJoinSql("ic");
    m.put("join_sql", s3);

    String s4 = this.m_ctrl.getWhereSql("ic");
    m.put("cond_where", s4);

    m.put("cond_group", s1);

    m.put("dateWhere", dateWhere);

    String fixedWhere = getFixedWhere("h");
    fixedWhere = fixedWhere + " and (h.cbiztype IN (SELECT pk_busitype FROM bd_busitype WHERE verifyrule <> 'J' and verifyrule <> 'C') OR h.cbiztype IS NULL) ";

    fixedWhere = fixedWhere + " and h.cbilltypecode not in ('40','41','42','43','44')";

    m.put("fixed_where", fixedWhere);
    sql_skeleton = SqlHelper.replace(sql_skeleton, m);
    sql_skeleton = SqlHelper.deal_and(SqlHelper.deal_quote(sql_skeleton));
    return sql_skeleton;
  }

  private String qryBenQiFromRecordIC(String recordDateWhere, String icDateWhere)
  {
    String sql_record = qryBenQiFromRecord(recordDateWhere);

    String sql_ic = qryBenQiFromIC(icDateWhere);

    if (sql_record == null)
      return sql_ic;
    if (sql_ic == null) {
      return sql_record;
    }

    String sql_union = "SELECT pure_fld_sum , sum_fld_sum FROM ( ";
    sql_union = sql_union + " (发生) UNION ALL  ";
    sql_union = sql_union + " (业务)  ) qichu GROUP BY cond_group_1";

    HashMap m = new HashMap();
    m.put("pure_fld_sum", this.m_ctrl.getPureSelectField(null));
    String sum_all = "sum(ninnum) ninnum, sum(noutnum) noutnum,sum(ninassistnum) ninassistnum,sum(noutassistnum) noutassistnum, sum(ningrossnum) ningrossnum, sum(noutgrossnum) noutgrossnum,sum(ntranoutnum) as ntranoutnum,sum(nretnum) as nretnum ";
    m.put("sum_fld_sum", sum_all);
    m.put("发生", sql_record);
    m.put("业务", sql_ic);
    m.put("cond_group_1", this.m_ctrl.getPureSelectField(null));

    sql_union = SqlHelper.replace(sql_union, m);
    sql_union = SqlHelper.deal_and(SqlHelper.deal_quote(sql_union));

    return sql_union;
  }

  private String qryBenQiFromRecord(String recordDateWhere)
  {
    if (recordDateWhere == null)
      return null;
    String table = this.m_bBizDateAccoutDate == true ? "ic_month_record" : "ic_month_recordsign";

    String sql_record = "SELECT pure_fld, sum_fld FROM " + table + " rec join_sql WHERE  dateWhere and cond_where and  fixed_where GROUP BY cond_group";

    HashMap m = new HashMap();
    String s1 = this.m_ctrl.getPureSelectField("rec");
    m.put("pure_fld", s1);
    m.put("cond_group", s1);

    String s2 = "sum(isnull(ninnum,0.0)";

    if (this.m_iQueryType == 5)
    {
      s2 = s2 + "+case when cbilltypecode in ('41','49') then isnull(ntranoutnum,0.0)-isnull(ninnum,0.0) else 0.0 end ";
    }

    s2 = s2 + ") as ninnum ,sum(isnull(noutnum,0.0)";
    if (this.m_iQueryType == 5)
    {
      s2 = s2 + "+case when cbilltypecode in ('42','4H') then (-1)*isnull(ntranoutnum,0.0)-isnull(noutnum,0.0) else 0.0  end ";
    }

    s2 = s2 + ") as noutnum, " + "sum(isnull(ninassistnum,0.0)) as ninassistnum,sum(isnull(noutassistnum,0.0)) as noutassistnum," + "sum(isnull(ningrossnum,0.0)) as ningrossnum,sum(isnull(noutgrossnum,0.0)) as  noutgrossnum," + "sum(isnull(ntranoutnum,0.0)) as ntranoutnum,sum(isnull(nretnum,0.0)) as nretnum ";

    m.put("sum_fld", s2);

    String s3 = this.m_ctrl.getJoinSql("rec");
    m.put("join_sql", s3);

    String s4 = this.m_ctrl.getWhereSql("rec");
    m.put("cond_where", s4);

    String s5 = recordDateWhere;
    m.put("dateWhere", s5);

    String fixedWhere = getFixedWhere("rec");
    m.put("fixed_where", fixedWhere);

    sql_record = SqlHelper.replace(sql_record, m);
    sql_record = SqlHelper.deal_and(SqlHelper.deal_quote(sql_record));
    return sql_record;
  }

  private String getFixedWhere(String alias) {
    if (alias == null)
      return "";
    if ((!alias.equals("h")) && (!alias.equals("rec"))) {
      return "";
    }

    switch (this.m_iQueryType) {
    case 1:
      return alias + ".cbilltypecode in ('49','41','4J')";
    case 2:
      return alias + ".cbilltypecode in ('4H','42','4B') ";
    case 3:
      return alias + ".cbilltypecode in ('43','4P','4X','4O') ";
    case 4:
      return alias + ".cbilltypecode in ('44','48','4G') ";
    case 5:
      return alias + ".cbilltypecode in ('40','45','46','47','4A','4E','4C','4D','4Y','4F','4I','4O','41','42','49','4H') ";
    case -1:
      String x = SqlHelper.replaceFirst(SqlMonthSum.where_xcl_billtype_rec, "and", " ");
      return SqlHelper.replace(x, "cbilltypecode", alias + ".cbilltypecode");
    case 0:
    }

    return "";
  }

  private String qryQiChuFromRecord(String recordDateWhere)
  {
    if (recordDateWhere == null)
      return null;
    String table = this.m_bBizDateAccoutDate == true ? "ic_month_record" : "ic_month_recordsign";

    String sql_record = "SELECT pure_fld, sum_fld FROM " + table + " rec join_sql WHERE  dateWhere and cond_where and  fixed_where GROUP BY cond_group";

    HashMap m = new HashMap();
    String s1 = this.m_ctrl.getPureSelectField("rec");
    m.put("pure_fld", s1);
    m.put("cond_group", s1);

    String s2 = "sum(isnull(ninnum,0.0) ";
    if (this.m_iQueryType == 3)
      s2 = s2 + "- case when cbilltypecode='4O' then (-1)*isnull(noutnum,0.0) else isnull(noutnum,0.0) end ";
    else {
      s2 = s2 + "-isnull(noutnum,0.0)";
    }
    if (this.m_iQueryType == 5)
    {
      s2 = s2 + "+case when cbilltypecode in ('41','49') then isnull(ntranoutnum,0.0)-isnull(ninnum,0.0) when cbilltypecode in ('42','4H') then (-1)*isnull(ntranoutnum,0.0)+isnull(noutnum,0.0) else 0.0 end ";
    }

    s2 = s2 + " ) nonhandnum, sum(isnull(ninassistnum,0.0)-isnull(noutassistnum,0.0)";
    if (this.m_iQueryType == 5)
    {
      s2 = s2 + "+case when cbilltypecode in ('41','49') then isnull(ntranoutastnum,0.0)-isnull(ninassistnum,0.0) when cbilltypecode in ('42','4H') then (-1)*isnull(ntranoutastnum,0.0)+isnull(noutassistnum,0.0) else 0.0 end ";
    }

    s2 = s2 + ") nonhandassistnum," + "sum(isnull(ningrossnum,0.0)-isnull(noutgrossnum,0.0)) ngrossnum";

    m.put("sum_fld", s2);

    String s3 = this.m_ctrl.getJoinSql("rec");
    m.put("join_sql", s3);

    String s4 = this.m_ctrl.getWhereSql("rec");
    m.put("cond_where", s4);

    String s5 = recordDateWhere;
    m.put("dateWhere", s5);

    String fixedWhere = getFixedWhere("rec");
    m.put("fixed_where", fixedWhere);

    sql_record = SqlHelper.replace(sql_record, m);
    sql_record = SqlHelper.deal_and(SqlHelper.deal_quote(sql_record));
    return sql_record;
  }

  private String qryQiChuFromHand(String handDateWhere)
  {
    if (handDateWhere == null)
      return null;
    String table = this.m_bBizDateAccoutDate == true ? "ic_month_hand" : "ic_month_handsign";

    String sql_hand = "SELECT pure_fld, sum_fld FROM " + table + " hand join_sql WHERE  dateWhere and cond_where GROUP BY cond_group";

    HashMap m = new HashMap();
    String s1 = this.m_ctrl.getPureSelectField("hand");
    m.put("pure_fld", s1);
    m.put("cond_group", s1);

    String s2 = "isnull(sum(nonhandnum),0.0) nonhandnum, isnull(sum(nonhandassistnum),0.0) nonhandassistnum, isnull(sum(ngrossnum),0.0) ngrossnum";
    m.put("sum_fld", s2);

    String s3 = this.m_ctrl.getJoinSql("hand");
    m.put("join_sql", s3);

    String s4 = this.m_ctrl.getWhereSql("hand");
    m.put("cond_where", s4);

    String s5 = handDateWhere;
    m.put("dateWhere", s5);

    sql_hand = SqlHelper.replace(sql_hand, m);
    sql_hand = SqlHelper.deal_and(SqlHelper.deal_quote(sql_hand));
    return sql_hand;
  }

  private String qryQiChuFromRecordIC(String recordDateWhere, String icDateWhere)
  {
    String sql_record = qryQiChuFromRecord(recordDateWhere);

    String sql_ic = qryQiChuFromIC(icDateWhere);

    if (sql_record == null)
      return sql_ic;
    if (sql_ic == null) {
      return sql_record;
    }

    String sql_union = "SELECT pure_fld_sum , sum_fld_sum FROM ( ";
    sql_union = sql_union + " (发生) UNION ALL  ";
    sql_union = sql_union + " (业务)  ) qichu GROUP BY cond_group_1";

    HashMap m = new HashMap();
    m.put("pure_fld_sum", this.m_ctrl.getPureSelectField(null));
    String sum_all = "sum(nonhandnum) nonhandnum, sum(nonhandassistnum) nonhandassistnum, sum(ngrossnum) ngrossnum";
    m.put("sum_fld_sum", sum_all);
    m.put("发生", sql_record);
    m.put("业务", sql_ic);
    m.put("cond_group_1", this.m_ctrl.getPureSelectField(null));

    sql_union = SqlHelper.replace(sql_union, m);
    sql_union = SqlHelper.deal_and(SqlHelper.deal_quote(sql_union));

    return sql_union;
  }

  public String qryQiChu_String(QryConditionCtrl ctrl, UFDate date, boolean bBizDate)
    throws Exception
  {
    this.m_ctrl = ctrl;
    this.m_bBizDateAccoutDate = bBizDate;
    this.m_endDate = date;

    isCorrectBeginDate(date);

    int datetype = bBizDate ? 0 : 1;

    MonthMode serv = new MonthMode();
    serv.alterTableName(datetype);

    MonthExecImp imp = new MonthExecImp(serv);
    if (!imp.isExistRecord()) {
      return qryQiChuFromIC(null);
    }

    int table = ctrl.checkQueryParam();

    if ((this.m_iQueryType != -1) && 
      (table == 2)) {
      table = 1;
    }

    if (table == 3)
    {
      return qryQiChuFromIC(null);
    }

    if (table == 1)
    {
      String dateRecord = null;
      try {
        dateRecord = imp.queryMaxRecordYearMonth(this.m_endDate);
      }
      catch (Exception er) {
        GenMethod.throwBusiException(er);
      }
      if (dateRecord == null) {
        return qryQiChuFromIC(null);
      }

      String ymRecord = " dyearmonth <= '" + dateRecord + "'";
      String datefld = this.m_bBizDateAccoutDate ? "dbizdate" : "daccountdate";

      String dateWhereIC = datefld + ">'" + dateRecord + "-31' and " + datefld + "<'" + this.m_endDate.toString() + "' ";

      return qryQiChuFromRecordIC(ymRecord, dateWhereIC);
    }

    if (table == 2)
    {
      QryZone zone = null;
      try {
        zone = imp.getUFDateZoneQiChuSplit(this.m_endDate);
      }
      catch (Exception e) {
        throw new BusinessException("分析期初日期时出现错误");
      }

      return qryQiChuFromHandRecordIC(zone);
    }

    return null;
  }

  private String qryQiChuFromHandRecordIC(QryZone zone)
  {
    String sql_hand = null; String sql_record = null; String sql_ic = null;

    if (zone.getDate_hand() != null) {
      String dateWhere = " dyearmonth='" + zone.getDate_hand() + "' ";
      sql_hand = qryQiChuFromHand(dateWhere);
    }

    if (zone.getDate_record() != null) {
      String[] saRecDate = zone.getDate_record();
      String sDateWhere = null;
      if (saRecDate[0] != null) {
        sDateWhere = "  dyearmonth>='" + saRecDate[0] + "' and dyearmonth<='" + saRecDate[1] + "'";
      }
      else {
        sDateWhere = "  dyearmonth<='" + saRecDate[1] + "'";
      }
      sql_record = qryQiChuFromRecord(sDateWhere);
    }

    if (zone.getDate_ic() != null)
    {
      String[] saICDate = zone.getDate_ic();
      String sDateWhere = null;
      String dateFld = this.m_bBizDateAccoutDate ? "dbizdate" : "daccountdate";

      if (saICDate[0] != null) {
        sDateWhere = "  " + dateFld + ">'" + saICDate[0] + "' and " + dateFld + "<'" + saICDate[1] + "'";
      }
      else {
        sDateWhere = " " + dateFld + "<'" + saICDate[1] + "'";
      }
      sql_ic = qryQiChuFromIC(sDateWhere);
    }

    String sql_union = "SELECT pure_fld_sum , sum_fld_sum FROM ( ";
    if (zone.getDate_hand() != null)
      sql_union = sql_union + " (结存) UNION ALL  ";
    if (zone.getDate_record() != null)
      sql_union = sql_union + " (发生) UNION ALL ";
    if (zone.getDate_ic() != null) {
      sql_union = sql_union + " (业务) ) qichu GROUP BY cond_group_1";
    }

    String sum_all = "sum(nonhandnum) nonhandnum, sum(nonhandassistnum) nonhandassistnum, sum(ngrossnum) ngrossnum";
    Map mx = new HashMap();
    mx.put("pure_fld_sum", this.m_ctrl.getPureSelectField(null));
    mx.put("sum_fld_sum", sum_all);
    mx.put("结存", sql_hand);
    mx.put("发生", sql_record);
    mx.put("业务", sql_ic);
    mx.put("cond_group_1", this.m_ctrl.getPureSelectField(null));

    sql_union = SqlHelper.replace(sql_union, mx);
    sql_union = SqlHelper.deal_and(SqlHelper.deal_quote(sql_union));

    return sql_union;
  }

  public List qryBenQi_Kernel(QryConditionCtrl ctrl, UFDate startdate, UFDate enddate, String voName)
    throws BusinessException
  {
    String sql_union = null;
    if (enddate == null)
      throw new BusinessException("查询本期截止日期不能空！");
    try {
      sql_union = qryBenQi_String(ctrl, startdate, enddate, true);
    } catch (Exception e) {
      SCMEnv.error(e);
      throw new BusinessException("构造本期查询sql出错:" + e.getMessage());
    }
    return execQuery(sql_union, voName);
  }

  public List qryBenQi_Kernel_Sign(QryConditionCtrl ctrl, UFDate startdate, UFDate enddate, String voName)
    throws BusinessException
  {
    String sql_union = null;
    if (enddate == null)
      throw new BusinessException("查询本期截止日期不能空！");
    try
    {
      sql_union = qryBenQi_String(ctrl, startdate, enddate, false);
    } catch (Exception e) {
      SCMEnv.error(e);
      throw new BusinessException("构造本期查询sql出错:" + e.getMessage());
    }
    return execQuery(sql_union, voName);
  }

  private String qryBenQi_String(QryConditionCtrl ctrl, UFDate startdate, UFDate date, boolean bBizDate)
    throws Exception
  {
    this.m_ctrl = ctrl;
    this.m_bBizDateAccoutDate = bBizDate;

    this.m_endDate = date;
    this.m_startDate = startdate;

    if (startdate != null) {
      if (!isCorrectBeginDate(startdate)) {
        return null;
      }

    }
    else if (!isCorrectBeginDate(date)) {
      return null;
    }

    int datetype = bBizDate == true ? 0 : 1;

    MonthMode serv = new MonthMode();
    serv.alterTableName(datetype);

    MonthExecImp imp = new MonthExecImp(serv);
    if (!imp.isExistRecord()) {
      return qryBenQiFromIC(null);
    }

    int table = ctrl.checkQueryParam();

    if (table == 2) {
      table = 1;
    }

    if (table == 3)
    {
      return qryBenQiFromIC(null);
    }

    if (table == 1)
    {
      String enddateRecord = null;
      String startdateRecord = null;
      try
      {
        enddateRecord = imp.queryMaxRecordYearMonth(this.m_endDate);
        if (this.m_startDate != null)
          startdateRecord = imp.queryNextRecordYearMonth(this.m_startDate);
      }
      catch (Exception er) {
        GenMethod.throwBusiException(er);
      }
      if (((this.m_startDate != null) && (startdateRecord == null)) || (enddateRecord == null)) {
        return qryBenQiFromIC(null);
      }

      if ((startdateRecord != null) && (enddateRecord != null) && (startdateRecord.compareTo(enddateRecord) > 0)) {
        return qryBenQiFromIC(null);
      }
      if (this.m_startDate != null)
      {
        startdateRecord = getNextMonRecord(this.m_startDate);
      }
      else
      {
        startdateRecord = null;
      }

      String ymRecord = " dyearmonth <= '" + enddateRecord + "'";
      if (startdateRecord != null)
        ymRecord = ymRecord + " and dyearmonth >= '" + startdateRecord + "'";
      String datefld = this.m_bBizDateAccoutDate == true ? "dbizdate" : "daccountdate";

      String dateWhereIC = "";
      if (this.m_startDate == null)
      {
        dateWhereIC = "((" + datefld + ">'" + enddateRecord + "-31' and " + datefld + "<='" + this.m_endDate.toString() + "' ))";
      }
      else
      {
        dateWhereIC = "((" + datefld + ">'" + enddateRecord + "-31' and " + datefld + "<='" + this.m_endDate.toString() + "' ) or (" + datefld + "<'" + startdateRecord + "-01' and " + datefld + ">='" + this.m_startDate.toString() + "'))";
      }

      return qryBenQiFromRecordIC(ymRecord, dateWhereIC);
    }

    return null;
  }

  private String getNextMonRecord(UFDate date) {
    String startdateRecord = null;
    int year = date.getYear();
    int mon = date.getMonth();
    if (mon == 12) {
      mon = 1;
      year += 1;
    } else {
      mon += 1;
    }if (String.valueOf(mon).length() == 1) {
      startdateRecord = String.valueOf(year) + "-0" + String.valueOf(mon);
    }
    else {
      startdateRecord = String.valueOf(year) + "-" + String.valueOf(mon);
    }
    return startdateRecord;
  }

  private String getPreMonRecord(UFDate date) {
    String startdateRecord = null;
    int year = date.getYear();
    int mon = date.getMonth();
    if (mon == 1) {
      mon = 12;
      year -= 1;
    } else {
      mon -= 1;
    }if (String.valueOf(mon).length() == 1) {
      startdateRecord = String.valueOf(year) + "-0" + String.valueOf(mon);
    }
    else {
      startdateRecord = String.valueOf(year) + "-" + String.valueOf(mon);
    }
    return startdateRecord;
  }

  public List qryQiChu_Kernel(QryConditionCtrl ctrl, UFDate date, String voName)
    throws BusinessException
  {
    String sql_union = null;
    try
    {
      sql_union = qryQiChu_String(ctrl, date, true);
    }
    catch (Exception e) {
      SCMEnv.error(e);
      throw new BusinessException("构造期初查询sql出错:" + e.getMessage());
    }

    return execQuery(sql_union, voName);
  }

  public List qryQiChu_Kernel_Sign(QryConditionCtrl ctrl, UFDate date, String voName)
    throws BusinessException
  {
    String sql_union = null;
    try
    {
      sql_union = qryQiChu_String(ctrl, date, false);
    }
    catch (Exception e) {
      SCMEnv.error(e);
      throw new BusinessException("构造期初查询sql出错:" + e.getMessage());
    }

    return execQuery(sql_union, voName);
  }

  private List execQuery(String sql, String sVOName)
    throws BusinessException
  {
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    ArrayList lst = new ArrayList();
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      rs = stmt.executeQuery();

      ResultSetMetaData meta = rs.getMetaData();

      GenMethod gm = new GenMethod();
      MonthVO vo = null;
      CircularlyAccessibleValueObject vot = null;

      while (rs.next()) {
        if (sVOName == null) {
          vo = new MonthVO();
          gm.setData(rs, vo, meta);
          lst.add(vo);
          continue;
        }
        try
        {
          vot = (CircularlyAccessibleValueObject)Class.forName(sVOName).newInstance();
        }
        catch (Exception ex) {
          SCMEnv.error(ex);
          throw new BusinessException("调用MonthQuery:execQuery失败," + sVOName + "不是CircularlyAccessibleValueObject类型");
        }

        int iCount = meta.getColumnCount();
        for (int i = 1; i <= iCount; i++) {
          Object oValue = rs.getObject(i);
          if (oValue != null) {
            String s1 = meta.getColumnName(i).toLowerCase();
            String s2 = this.m_ctrl.findCorrespondString(s1);
            if (s2 != null) {
              vot.setAttributeValue(s2, oValue);
            }
          }
        }
        lst.add(vot);
      }
    }
    catch (Exception e)
    {
      SCMEnv.error(e);
      throw new BusinessException(e.getMessage());
    }
    finally
    {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception e) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception e) {
      }
      try {
        if (rs != null)
          rs.close();
      } catch (Exception e) {
      }
    }
    return lst;
  }

  public static boolean isCorrectBeginDate(UFDate dDate)
    throws BusinessException
  {
    String maxDownYM = FixOnhandnumDMO.getDowLoadDate();
    if (maxDownYM != null)
    {
      String maxDownYMD = maxDownYM + "-31";
      if (dDate.before(new UFDate(maxDownYMD)))
        throw new BusinessException("查询日期应该在当前最大卸载日期之后!");
    }
    else
    {
      AccountCalendar calendar = AccountCalendar.getInstance();
      AccperiodmonthVO month = calendar.getFirstMonthOfCurrentScheme();
      if ((dDate != null) && (dDate.before(month.getBegindate())))
        throw new BusinessException("查询日期不能早于第一个会计期间!");
    }
    return true;
  }
}