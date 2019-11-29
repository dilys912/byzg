package nc.bs.ic.pub.monthsum;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.naming.NamingException;
import nc.bs.ic.pub.GenMethod;
import nc.bs.ic.pub.bill.GeneralSqlString;
import nc.bs.logging.Logger;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.pub.DataManageObject;
import nc.bs.pub.SystemException;
import nc.bs.scm.pub.TempTableDMO;
import nc.bs.scm.pub.bill.SQLUtil;
import nc.bs.scm.pub.smart.SmartDMO;
import nc.bs.uap.lock.PKLock;
import nc.impl.ic.ic008.FixOnhandnumDMO;
import nc.itf.scm.pub.lock.LockTool;
import nc.jdbc.framework.crossdb.CrossDBConnection;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.lang.ResBase;
import nc.vo.ic.pub.locator.LocatorVO;
import nc.vo.ic.pub.monthsum.MonthMode;
import nc.vo.ic.pub.monthsum.MonthVO;
import nc.vo.ic.pub.monthsum.QryCondStr;
import nc.vo.ic.pub.monthsum.SqlHelper;
import nc.vo.ic.pub.monthsum.SqlMonthSum;
import nc.vo.ic.pub.monthsum.XMLMonthConfig;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.Timer;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.smart.ObjectUtils;

public class MonthServ extends DataManageObject
{
  private static boolean DEBUG = true;
  public static final String LOCK_MONTH = "monthquery";
  private MonthMode m_tableMode = new MonthMode();

  private MonthExecImp m_monthExecImp = null;

  private static final UFDouble ZERO = new UFDouble(0);

  public MonthServ()
    throws NamingException, SystemException
  {
  }

  public MonthServ(String dbName)
    throws NamingException, SystemException
  {
    super(dbName);
  }

  private UFDate[] qryMonthPeriod()
    throws Exception
  {
    String sql = "select min(dbizdate),max(dbizdate) from ic_general_b where dr=0 and dbizdate is not null";
    SmartDMO dmo = new SmartDMO();
    Object[] oa = dmo.selectBy2(sql);

    UFDate min = null;
    UFDate max = null;
    if (oa != null) {
      min = (UFDate)((Object[])(Object[])oa[0])[0];
      max = (UFDate)((Object[])(Object[])oa[0])[1];
    }
    if ((min == null) || (max == null))
      return null;
    if (min.getYear() > max.getYear()) {
      return null;
    }
    return YearMonthCtrl.getBetween(min, max);
  }

  private void deleteMonthExec() throws Exception {
    String sql = "delete from " + this.m_tableMode.IC_MONTH_EXEC;

    execUpdate(sql);
  }

  private void clearAllTable() throws Exception {
    String sql1 = "delete from " + this.m_tableMode.IC_MONTH_RECORD;
    String sql2 = "delete from " + this.m_tableMode.IC_MONTH_HAND;
    String sql3 = "delete from " + this.m_tableMode.IC_MONTH_EXEC;

    execUpdate(sql1);
    execUpdate(sql2);
    execUpdate(sql3);
  }

  private void dropCreateMonthHandTable() throws Exception {
    String sql2 = "drop table " + this.m_tableMode.IC_MONTH_HAND;

    execUpdate(sql2);

    String sql3 = null;

    if (this.m_tableMode.m_iMode == 0) {
      sql3 = "create table ic_month_record (pk_corp              char(4)              null,cbilltypecode        varchar(4)           null,cwarehouseid         char(20)             null,cdispatcherid        char(20)             null,fbillflag            smallint             null,pk_calbody           char(20)             null,cinventoryid         char(20)             null,vbatchcode           varchar(30)          null,vfree1               varchar(100)         null,vfree2               varchar(100)         null,vfree3               varchar(100)         null,vfree4               varchar(100)         null,vfree5               varchar(100)         null,vfree6               varchar(100)         null,vfree7               varchar(100)         null,vfree8               varchar(100)         null,vfree9               varchar(100)         null,vfree10              varchar(100)         null,noutnum              decimal(20,8)        null,noutassistnum        decimal(20,8)        null,castunitid           char(20)             null,ninnum               decimal(20,8)        null,ninassistnum         decimal(20,8)        null,cprojectphaseid      char(20)             null,cprojectid           char(20)             null,cvendorid            char(20)             null,hsl                  decimal(20,8)        null,ningrossnum          decimal(20,8)        null,noutgrossnum         decimal(20,8)        null,dyear                char(4)              null,dmonth               char(2)              null,cspaceid             char(20)             null,daccountdate         char(10)             null,ts                   char(19)             null default convert(char(19),getdate(),20),dr                   smallint             null default 0)";
    }
    else
    {
      sql3 = "";
    }
    execUpdate(sql3);
  }

  private void dropAndCreateTable() throws Exception
  {
    dropCreateMonthHandTable();

    String sql1 = "drop table " + this.m_tableMode.IC_MONTH_RECORD;
    execUpdate(sql1);

    if (this.m_tableMode.m_iMode == 0) {
      sql1 = "create table ic_month_hand (pk_corp char(4) null,cwarehouseid         char(20)             null,fbillflag            smallint             null,pk_calbody           char(20)             null,cinventoryid         char(20)             null,vbatchcode           varchar(30)          null,vfree1               varchar(100)         null,vfree2               varchar(100)         null,vfree3               varchar(100)         null,vfree4               varchar(100)         null,vfree5               varchar(100)         null,vfree6               varchar(100)         null,vfree7               varchar(100)         null,vfree8               varchar(100)         null,vfree9               varchar(100)         null,vfree10              varchar(100)         null,nonhandassistnum     decimal(20,8)        null,cprojectphaseid      char(20)             null,cprojectid           char(20)             null,cvendorid            char(20)             null,castunitid           char(20)             null,hsl                  decimal(20,8)        null,ngrossnum            decimal(20,8)        null,nonhandnum           decimal(20,8)        null,dyear                char(4)              null,dmonth               char(2)              null,cspaceid             char(20)             null,cbilltypecode        varchar(4)           null,cdispatcherid        char(20)             null,daccountdate         char(10)             null,ts                   char(19)             null default convert(char(19),getdate(),20),dr                   smallint             null default 0)";
    }
    else
    {
      sql1 = null;
    }
    execUpdate(sql1);
  }

  private static UFDate dealWithSignBegin(GeneralBillVO vo, UFDate ufd)
    throws BusinessException
  {
    HashSet set = new HashSet();
    set.add("40");
    set.add("41");
    set.add("44");
    set.add("42");
    set.add("43");

    String billtype = vo.getBillTypeCode();

    if (set.contains(billtype)) {
      ufd = getAccoutBeginDate();
      vo.getHeaderVO().setDaccountdate(ufd);
    }
    return ufd;
  }

  private GeneralBillVO getBillNeedBD_Sign(GeneralBillVO vo, UFDate ufd)
    throws BusinessException
  {
    if ((vo == null) || (ufd == null))
      return null;
    GeneralBillHeaderVO head = vo.getHeaderVO();
    UFDate date = head.getDaccountdate();
    if (date == null) {
      return null;
    }

    date = dealWithSignBegin(vo, date);

    String ym = ufd.toString().substring(0, 7);

    if (ym.compareTo(date.toString().substring(0, 7)) >= 0) {
      return (GeneralBillVO)vo.clone();
    }
    return null;
  }

  private static boolean isNeedInsertMonth(GeneralBillItemVO voItem, UFDate ufd)
  {
    String[] saNumFld = { "ninnum", "noutnum", "ninassistnum", "noutassistnum" };

    boolean hasNum = false;

    if ((voItem.getLocator() != null) && (voItem.getLocator().length > 0))
      hasNum = true;
    if (!hasNum) {
      for (int i = 0; i < saNumFld.length; i++) {
        if (voItem.getAttributeValue(saNumFld[i]) != null) {
          hasNum = true;
          break;
        }
      }
    }
    UFDate d = voItem.getDbizdate();
    if ((d == null) || (!hasNum)) {
      return false;
    }
    String ym = d.toString().substring(0, 7);
    String ym1 = ufd.toString().substring(0, 7);

    return ym1.compareTo(ym) >= 0;
  }

  private boolean isQiChu(String billtype)
  {
    boolean isqichu = false;
    if (("40".equals(billtype)) || ("41".equals(billtype)) || ("42".equals(billtype)) || ("43".equals(billtype)) || ("44".equals(billtype)))
    {
      isqichu = true;
    }

    return isqichu;
  }

  private boolean isHand(String billtype) {
    boolean isHand = true;
    if (("42".equals(billtype)) || ("43".equals(billtype)) || ("4X".equals(billtype)) || ("4P".equals(billtype)))
    {
      isHand = false;
    }

    return isHand;
  }

  private GeneralBillVO getBillNeedBD(GeneralBillVO vo, UFDate ufd)
  {
    if ((vo == null) || (ufd == null))
      return null;
    GeneralBillHeaderVO head = vo.getHeaderVO();
    GeneralBillItemVO[] voaItem = vo.getItemVOs();
    UFDate date = getAccoutBeginDate();
    ArrayList al = new ArrayList();

    for (int i = 0; i < voaItem.length; i++) {
      if (voaItem[i] == null) {
        continue;
      }
      String billtype = vo.getHeaderVO().getCbilltypecode();

      if ((isNeedInsertMonth(voaItem[i], ufd)) || (isQiChu(billtype))) {
        GeneralBillItemVO voi = (GeneralBillItemVO)voaItem[i].clone();
        billtype = vo.getHeaderVO().getCbilltypecode();

        if (isQiChu(billtype))
          voi.setDbizdate(date);
        al.add(voi);
      }

    }

    if (al.size() > 0) {
      GeneralBillVO voret = new GeneralBillVO();
      voret.setParentVO((GeneralBillHeaderVO)head.clone());
      voret.setChildrenVO((GeneralBillItemVO[])(GeneralBillItemVO[])al.toArray(new GeneralBillItemVO[al.size()]));

      return voret;
    }
    return null;
  }

  private MonthExecImp getMonthExecImp()
  {
    if (this.m_monthExecImp == null) {
      this.m_monthExecImp = new MonthExecImp(this.m_tableMode);
    }
    return this.m_monthExecImp;
  }

  private void insert_month_hand(UFDate[] saPeriod)
    throws Exception
  {
    for (int i = 0; i < saPeriod.length; i++)
    {
      String yearmonthI = YearMonthCtrl.getYearMonth(saPeriod[i]);

      String max_ym = getMonthExecImp().queryMaxRecordOnHand(true);

      if (max_ym == null)
      {
        String sql = SqlMonthSum.getInsertOnHandSql(yearmonthI, this.m_tableMode,"1016");

        execUpdate(sql);

        String sqlUpdate = "update " + this.m_tableMode.IC_MONTH_HAND + " set dyearmonth='" + yearmonthI + "'";

        execUpdate(sqlUpdate);

        getMonthExecImp().updateByYearAndMonth(yearmonthI, true);
      }
      else
      {
        TempTableDMO tmpDMO = new TempTableDMO();
        Connection con = getConnection();

        String[] saCols = SqlMonthSum.getFlds_Month_Hand_All();
        String[] saTypes = SqlMonthSum.saField_Month_Hand_Types;
        String subSql = "select " + SqlMonthSum.getFld_Month_Hand_ALL() + " from " + this.m_tableMode.IC_MONTH_HAND + " where dyearmonth='" + max_ym + "'";

        String sTmpTable = null;
        try {
          sTmpTable = tmpDMO.getTempStringTable(con, "t_ic_month_t1", saCols, saTypes, new String[] { "dyearmonth" }, subSql);
        }
        catch (SQLException ex)
        {
          SCMEnv.error(ex);
          throw ex;
        }

        String subSql2 = "select " + SqlMonthSum.getFld_Month_Hand_NotSum() + " ," + SqlMonthSum.getFld_Month_Hand_SumSql() + " from " + this.m_tableMode.IC_MONTH_RECORD + " where dyearmonth>'" + max_ym + "' and dyearmonth<='" + yearmonthI + "' " + SqlMonthSum.where_xcl_billtype_rec + " group by " + SqlMonthSum.getFld_Month_Hand_NotSum() + " Having isnull(sum(ninnum),0)-isnull(sum(noutnum),0) !=0  " + " or isnull(sum(ninassistnum),0)-isnull(sum(noutassistnum),0) !=0 " + " or isnull(sum(ningrossnum),0)-isnull(sum(noutgrossnum),0) !=0 ";

        String sql2 = "insert into " + sTmpTable + "(" + SqlMonthSum.getFld_Month_Hand_ALL() + ") " + subSql2;

        execUpdate(sql2);

        String sqlUpdate = "update " + sTmpTable + " set dyearmonth='" + yearmonthI + "'";

        execUpdate(sqlUpdate);

        String sqlInsert = " insert into " + this.m_tableMode.IC_MONTH_HAND + "  (" + SqlMonthSum.getFld_Month_Hand_ALL() + ") select " + SqlMonthSum.getFld_Month_Hand_NotSum() + "," + "sum(nonhandnum) as nonhandnum,sum(nonhandassistnum) as nonhandassistnum," + "sum(ngrossnum) as ngrossnum " + " from " + sTmpTable + " group by " + SqlMonthSum.getFld_Month_Hand_NotSum() + " Having  sum(nonhandnum) !=0  " + " or sum(nonhandassistnum) !=0 " + " or sum(ngrossnum) !=0 ";

        execUpdate(sqlInsert);

        getMonthExecImp().updateByYearAndMonth(yearmonthI, true);
      }
    }
  }

  private void insert_month_record(UFDate[] saPeriod)
    throws Exception
  {
    for (int i = 0; i < saPeriod.length; i++) {
      UFDate date = saPeriod[i];
      insertRecordTableByDate(date);

      String yearmonth = date.toString().substring(0, 7);

      getMonthExecImp().insertByYearAndMonth(yearmonth, false);
    }
  }

  private ArrayList qryDateFromMonthRecord()
    throws Exception
  {
    String sql = "SELECT DISTINCT dyearmonth FROM " + this.m_tableMode.IC_MONTH_RECORD + " WHERE dyearmonth is not null ORDER BY dyearmonth  asc";

    SmartDMO dmo = new SmartDMO();
    Object[] oa = dmo.selectBy2(sql);

    if ((oa == null) || (oa.length == 0)) {
      return null;
    }
    ArrayList al2 = new ArrayList();
    for (int i = 0; i < oa.length; i++) {
      Object[] oa1 = (Object[])(Object[])oa[i];
      if ((oa1 == null) || (oa1[0] == null) || (oa1[0].toString().length() != 7)) {
        continue;
      }
      UFDate d = new UFDate(oa1[0].toString() + "-" + "01");
      al2.add(d);
    }
    return al2;
  }

  public static int[] getMonthPattern()
    throws BusinessException
  {
    int[] iaPattern = null;
    if (XMLMonthConfig.getInstance().getInterval() == 6)
      iaPattern = new int[] { 6, 12 };
    else if (XMLMonthConfig.getInstance().getInterval() == 4)
      iaPattern = new int[] { 4, 8, 12 };
    else if (XMLMonthConfig.getInstance().getInterval() == 3)
      iaPattern = new int[] { 3, 6, 9, 12 };
    else if (XMLMonthConfig.getInstance().getInterval() == 2)
      iaPattern = new int[] { 2, 4, 6, 8, 10, 12 };
    else if (XMLMonthConfig.getInstance().getInterval() == 1)
      iaPattern = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
    else if (XMLMonthConfig.getInstance().getInterval() == 12)
      iaPattern = new int[] { 12 };
    else {
      throw new BusinessException("月结的时间间隔必须为12的约数!");
    }
    return iaPattern;
  }

  private static int getNearMonth(int month) throws BusinessException {
    int[] ia = getMonthPattern();
    for (int i = 0; i < ia.length; i++) {
      if (ia[i] >= month)
        return ia[i];
    }
    return -1;
  }

  public static UFDate[] findOutYearMonthHand(UFDate[] ufda)
    throws BusinessException
  {
    if ((ufda == null) || (ufda.length <= 0)) {
      return null;
    }
    int[] iaPattern = getMonthPattern();

    ArrayList al = new ArrayList();
    for (int i = 0; i < ufda.length; i++) {
      int iMonth = ufda[i].getMonth();
      if (isIn(iMonth, iaPattern)) {
        al.add(ufda[i]);
      }
    }
    if (al.size() >= 0) {
      return (UFDate[])(UFDate[])al.toArray(new UFDate[al.size()]);
    }
    return null;
  }

  private static boolean isIn(int x, int[] ia)
  {
    if ((x < 0) || (ia == null) || (ia.length <= 0))
      return false;
    for (int i = 0; i < ia.length; i++) {
      if (ia[i] == x)
        return true;
    }
    return false;
  }

  private static String formGreaterDateCond(String yearmonth)
  {
    return "  (dyearmonth>'" + yearmonth + "') ";
  }

  private static String formLessDateCond(String yearmonth) {
    return "  (dyearmonth<'" + yearmonth + "') ";
  }

  public void insert_upgrade()
    throws BusinessException
  {
    boolean bAddSuccess = PKLock.getInstance().addDynamicLock("monthquery");
    if (!bAddSuccess) {
      throw new BusinessException("有单据进行业务,请稍后...");
    }
    this.m_tableMode.alterTableName(0);
    insert_all_kernel();

    this.m_tableMode.alterTableName(1);
    insert_all_kernel();
  }

  public void insert_all(Integer iMode)
    throws BusinessException
  {
    boolean bAddSuccess = PKLock.getInstance().addDynamicLock("monthquery");
    if (!bAddSuccess) {
      throw new BusinessException("有单据进行业务,请稍后...");
    }

    this.m_tableMode.alterTableName(iMode.intValue());
    insert_all_kernel();
  }

  private void insert_all_kernel()
    throws BusinessException
  {
    String date_download = FixOnhandnumDMO.getDowLoadDate();

    if (date_download == null) {
      insert_kernel();
    }
    else {
      try {
        if (!getMonthExecImp().isExistRecord())
          throw new BusinessException("已经数据卸载但没有月结记录, 数据非法, 请联系系统管理员!");
      }
      catch (Exception ep) {
        SCMEnv.error(ep);
        throw new BusinessException("已经数据卸载但没有月结记录, 数据非法, 请联系系统管理员!");
      }
      try
      {
        deleteMonthExec();

        String sql2 = "delete from " + this.m_tableMode.IC_MONTH_HAND;
        execUpdate(sql2);
      } catch (Exception e) {
        SCMEnv.error(e);
        GenMethod.throwBusiException(e);
      }

      String yearmonth = date_download;
      String y = formGreaterDateCond(yearmonth);

      String sqlDel = "delete from " + this.m_tableMode.IC_MONTH_RECORD + " where " + y;
      try
      {
        execUpdate(sqlDel);
      } catch (Exception e12) {
        SCMEnv.error(e12);
        GenMethod.throwBusiException(e12);
      }

      String dateWhere = null;
      String ymd = yearmonth + "-31";
      if (this.m_tableMode.m_iMode == 0)
        dateWhere = " (dbizdate>'" + ymd + "')";
      else {
        dateWhere = " (daccountdate>'" + ymd + "')";
      }

      try
      {
        insertRecordTableByDate(dateWhere);

        insertRestAll();
      }
      catch (Exception e1)
      {
        SCMEnv.error(e1);
        GenMethod.throwBusiException(e1);
      }
    }
  }

  private static UFDate getAccoutBeginDate()
  {
    UFDate ufd = new UFDate(SqlMonthSum.getAccoutBeginDate() + "-01");

    return ufd;
  }

  private void insertRestAll() throws BusinessException
  {
    ArrayList al = null;
    try {
      al = qryDateFromMonthRecord();
    } catch (Exception e1) {
      SCMEnv.error(e1);
      throw new BusinessException(e1.getMessage());
    }
    if ((al == null) || (al.size() <= 0)) {
      return;
    }

    String lastYM = ((UFDate)al.get(al.size() - 1)).toString();
    lastYM = lastYM.substring(0, 7);
    try {
      deleteLastMonthData(lastYM);
    } catch (Exception e2) {
      SCMEnv.error(e2);
      throw new BusinessException(e2.getMessage());
    }

    UFDate[] ufda = (UFDate[])(UFDate[])al.toArray(new UFDate[al.size()]);

    UFDate ufdBeginMonth = getAccoutBeginDate();

    UFDate[] ufdInsert = YearMonthCtrl.getBetween(ufdBeginMonth, ufda[(ufda.length - 1)]);

    getMonthExecImp().insetBatch(ufdInsert);

    UFDate[] ufdHand = findOutYearMonthHand(ufdInsert);
    if ((ufdHand == null) || (ufdHand.length <= 0))
      return;
    try
    {
      insert_month_hand(ufdHand);
    } catch (Exception e3) {
      SCMEnv.error(e3);
      GenMethod.throwBusiException(e3);
    }
  }

  private void deleteLastMonthData(String ym)
    throws Exception
  {
    String sql = "delete from " + this.m_tableMode.IC_MONTH_RECORD + " where dyearmonth='" + ym.trim() + "'";

    execUpdate(sql);
  }

  private void insert_kernel() throws BusinessException {
    Timer t = new Timer();
    try {
      deleteMonthExec();

      clearAllTable();

      String dbzidateString = null;
      insertRecordTableByDate(dbzidateString);

      insertRestAll();
    }
    catch (Exception ex) {
      SCMEnv.error(ex);

      GenMethod.throwBusiException(ex);
    }

    t.showExecuteTime("执行月结重算消耗的时间:");
  }

  private void insertRecordTableByDate(String dbzidateString)
    throws Exception
  {
    String sql1 = null;
    String sql2 = null;
    String sql3 = null;
    if (this.m_tableMode.m_iMode == 0) {
      sql1 = SqlMonthSum.getInsertMonthSql_LocMgt(dbzidateString, this.m_tableMode,"1016");
      sql2 = SqlMonthSum.getInsertMonthSql_NotLocMgt(dbzidateString, this.m_tableMode,"1016");
      sql3 = SqlMonthSum.getInsertMonthSql_RetTranLocMgt(dbzidateString, this.m_tableMode,"1016");
    }
    if (this.m_tableMode.m_iMode == 1) {
      sql1 = SqlMonthSum.getInsertMonthSql_LocMgt_Sign(dbzidateString, this.m_tableMode,"1016");
      sql2 = SqlMonthSum.getInsertMonthSql_NotLocMgt_Sign(dbzidateString, this.m_tableMode,"1016");

      sql3 = SqlMonthSum.getInsertMonthSql_RetTranLocMgt_sign(dbzidateString, this.m_tableMode,"1016");
    }

    execUpdate(sql1);
    execUpdate(sql2);
    execUpdate(sql3);
  }

  public void insert_month_alert()
    throws BusinessException
  {
    boolean bLock = LockTool.setLockForPks(new String[] { "monthquery" }, "0001");
    if (!bLock) {
      throw new BusinessException("系统有单据操作在进行,库存月结预警暂时不能执行!");
    }

    UFDate ufdTime = new UFDate(System.currentTimeMillis());

    this.m_tableMode.alterTableName(0);
    alert_kernel(ufdTime);

    this.m_tableMode.alterTableName(1);
    alert_kernel(ufdTime);

    LockTool.releaseLockForPks(new String[] { "monthquery" }, "0001");
  }

  private void alert_kernel(UFDate ufdTime)
    throws BusinessException
  {
    boolean bExist = false;
    try {
      bExist = getMonthExecImp().isExistRecord();
    } catch (Exception exx) {
      SCMEnv.error(exx);
      throw new BusinessException(exx.getMessage());
    }
    if (!bExist) {
      insert_all_kernel();
      return;
    }

    UFDate curr_date = ufdTime;
    String maxym = null;
    try {
      maxym = getMonthExecImp().queryMaxRecordYearMonth(null);
    } catch (Exception e) {
      SCMEnv.error(e);
      throw new BusinessException(e.getMessage());
    }
    if (maxym == null)
      return;
    int year = Integer.valueOf(maxym.substring(0, 4).toString()).intValue();
    int month = Integer.valueOf(maxym.substring(5).toString()).intValue();
    UFDate max_date = YearMonthCtrl.getAfterMonth(year, month);

    UFDate[] ufd = YearMonthCtrl.getBetween(max_date, curr_date);

    if ((ufd != null) && (ufd.length > 0)) {
      try {
        insert_month_record(ufd);
      } catch (Exception ex1) {
        SCMEnv.error(ex1);
        GenMethod.throwBusiException(ex1);
      }

    }

    String maxRecord = null;
    try {
      maxRecord = getMonthExecImp().queryMaxRecordOnHand(true);
    } catch (Exception ex2) {
      SCMEnv.error(ex2);
      GenMethod.throwBusiException(ex2);
    }

    UFDate ufdMax_JC = null;

    if (maxRecord != null) {
      int yearx = Integer.valueOf(maxRecord.substring(0, 4)).intValue();
      int monthx = Integer.valueOf(maxRecord.substring(5)).intValue();
      ufdMax_JC = YearMonthCtrl.getAfterMonth(yearx, monthx);
    }
    else {
      String ld = null;
      try {
        ld = getMonthExecImp().queryYearMonthMinNotHand();
      } catch (Exception ex3) {
        GenMethod.throwBusiException(ex3);
      }
      int nearMonth = getNearMonth(Integer.valueOf(ld.substring(5)).intValue());
      int yearx = Integer.valueOf(ld.substring(0, 4)).intValue();
      ufdMax_JC = YearMonthCtrl.getAfterMonth(yearx, nearMonth);
    }

    String lmaxnow = null;
    try {
      lmaxnow = getMonthExecImp().queryMaxRecordYearMonth(null);
    }
    catch (Exception ex4) {
      SCMEnv.error(ex4);
      GenMethod.throwBusiException(ex4);
    }

    if (lmaxnow == null)
      return;
    int year_lmaxnow = Integer.valueOf(lmaxnow.substring(0, 4).toString()).intValue();

    int month_lmaxnow = Integer.valueOf(lmaxnow.substring(5).toString()).intValue();

    UFDate ufd_now = YearMonthCtrl.getAfterMonth(year_lmaxnow, month_lmaxnow);

    UFDate[] ufd1 = YearMonthCtrl.getBetween(ufdMax_JC, ufd_now);
    if ((ufd1 == null) || (ufd1.length <= 0)) {
      return;
    }
    UFDate[] ufdHand = findOutYearMonthHand(ufd1);
    if ((ufdHand == null) || (ufdHand.length <= 0)) {
      return;
    }
    try
    {
      insert_month_hand(ufdHand);
    }
    catch (Exception ex5) {
      SCMEnv.error(ex5);
      GenMethod.throwBusiException(ex5);
    }
  }

  private void setKeyValue(MonthVO voMonth, GeneralBillHeaderVO voHead, GeneralBillItemVO voItem, LocatorVO voLoc, int iMode, boolean bAudit)
  {
    if (voMonth == null)
      voMonth = new MonthVO();
    if ((voHead == null) || (voItem == null)) {
      return;
    }
    String[] sa_h = null;
    String[] sa_b = null;
    if (iMode == 0)
      sa_h = SqlMonthSum.getFlds_General_H_Record();
    else {
      sa_h = SqlMonthSum.getFlds_General_H_Hand();
    }
    if (iMode == 0)
      sa_b = SqlMonthSum.getFlds_General_B_Record();
    else {
      sa_b = SqlMonthSum.getFlds_General_B_Hand();
    }
    for (int i = 0; i < sa_h.length; i++) {
      voMonth.setAttributeValue(sa_h[i], voHead.getAttributeValue(sa_h[i]));
    }

    for (int i = 0; i < sa_b.length; i++) {
      if (sa_b[i].equals("dbizdate")) {
        UFDate ufd = voItem.getDbizdate();
        UFDate ufd2 = voHead.getDaccountdate();
        if ((ufd == null) && (!bAudit)) {
          throw new IllegalArgumentException("setKeyValue Dbizdate is null");
        }

        if ((ufd2 == null) && (bAudit)) {
          throw new IllegalArgumentException("setKeyValue Daccountdate is null");
        }

        if (!bAudit) {
          String value = ufd.toString();
          String ym = value.substring(0, 7);
          voMonth.setAttributeValue("dyearmonth", ym);
        } else if (bAudit) {
          String ym_audit = ufd2.toString().substring(0, 7);
          voMonth.setAttributeValue("dyearmonth", ym_audit);
        }
      } else {
        voMonth.setAttributeValue(sa_b[i], voItem.getAttributeValue(sa_b[i]));
      }

    }

    if (voLoc == null) {
      return;
    }
    voMonth.setAttributeValue("cspaceid", (String)voLoc.getAttributeValue("cspaceid"));
  }

  private String getKey(GeneralBillItemVO voItem, GeneralBillHeaderVO voHead, LocatorVO voLoc, int iMode, boolean bAudit)
  {
    if ((voHead == null) || (voItem == null)) {
      return null;
    }
    String ym_audit = voHead.getDaccountdate() == null ? null : voHead.getDaccountdate().toString();

    StringBuffer key = new StringBuffer();

    String[] h_fld = iMode == 0 ? SqlMonthSum.getFlds_General_H_Record() : SqlMonthSum.getFlds_General_H_Hand();

    String[] b_fld = iMode == 0 ? SqlMonthSum.getFlds_General_B_Record() : SqlMonthSum.getFlds_General_B_Hand();

    for (int i = 0; i < h_fld.length; i++) {
      key.append(voHead.getAttributeValue(h_fld[i]));
    }

    for (int i = 0; i < b_fld.length; i++) {
      if ((b_fld[i].equals("dbizdate")) && (!bAudit)) {
        if (voItem.getDbizdate() != null) {
          UFDate ufd = voItem.getDbizdate();
          String value = ufd.toString();
          String ym = value.substring(0, 7);
          key.append(ym);
        }
      }
      else if ((b_fld[i].equals("dbizdate")) && (bAudit) && 
        (ym_audit != null)) {
        key.append(ym_audit.substring(0, 7));
      }
      else
      {
        key.append(voItem.getAttributeValue(b_fld[i]));
      }
    }
    if (voLoc == null) {
      return key.toString();
    }

    key.append(voLoc.getAttributeValue("cspaceid").toString());

    return key.toString();
  }

  private HashMap getHashMonth(GeneralBillVO voDeal, boolean bNew, int iMode, boolean bAudit)
  {
    if (voDeal == null) {
      return new HashMap();
    }
    HashMap ht = new HashMap();
    GeneralBillHeaderVO voHeadPre = null;
    GeneralBillItemVO[] voItems = null;

    if ((voDeal.getParentVO() != null) && (voDeal.getChildrenVO() != null)) {
      voHeadPre = voDeal.getHeaderVO();
      voItems = (GeneralBillItemVO[])(GeneralBillItemVO[])voDeal.getChildrenVO();

      for (int i = 0; i < voItems.length; i++) {
        if ((bNew) && (voItems[i].getStatus() == 3) && (!bAudit))
        {
          continue;
        }
        if (voItems[i].getLocator() == null) {
          String key = getKey(voItems[i], voHeadPre, null, iMode, bAudit);

          if (!ht.containsKey(key)) {
            MonthVO vomonth = new MonthVO();
            setKeyValue(vomonth, voHeadPre, voItems[i], null, iMode, bAudit);

            ht.put(key, vomonth);
          }
        }
        else
        {
          if ((voItems[i].getLocator() == null) || (voItems[i].getLocator().length <= 0))
            continue;
          LocatorVO[] voaLoc = voItems[i].getLocator();
          for (int x = 0; x < voaLoc.length; x++) {
            String key = getKey(voItems[i], voHeadPre, voaLoc[x], iMode, bAudit);

            if (!ht.containsKey(key)) {
              MonthVO vomonth = new MonthVO();
              setKeyValue(vomonth, voHeadPre, voItems[i], voaLoc[x], iMode, bAudit);

              ht.put(key, vomonth);
            }

          }

        }

      }

      for (int i = 0; i < voItems.length; i++)
      {
        if ((bNew) && (voItems[i].getStatus() == 3)) {
          continue;
        }
        if (voItems[i].getLocator() == null)
        {
          String key = getKey(voItems[i], voHeadPre, null, iMode, bAudit);

          MonthVO vo = (MonthVO)ht.get(key);
          if (vo == null) {
            continue;
          }
          setNumField(vo, voItems[i], null);
        }
        else {
          if ((voItems[i].getLocator() == null) || (voItems[i].getLocator().length <= 0)) {
            continue;
          }
          LocatorVO[] voLoc = voItems[i].getLocator();
          for (int j = 0; j < voLoc.length; j++)
          {
            String keyx = getKey(voItems[i], voHeadPre, voLoc[j], iMode, bAudit);

            MonthVO vo = (MonthVO)ht.get(keyx);
            if (vo == null)
            {
              continue;
            }
            setNumField(vo, null, voLoc[j]);
          }
        }

      }

    }

    return ht;
  }

  public static void assertValid(boolean bTrueFalse)
  {
    if (!bTrueFalse) {
      IllegalArgumentException e = new IllegalArgumentException("assertValide error!");

      SCMEnv.error(e);
      throw e;
    }
  }

  private void setNumField(MonthVO voM, GeneralBillItemVO voItem, LocatorVO voLoc)
  {
    if (voM == null)
      return;
    if ((voItem == null) && (voLoc == null))
      return;
    String[] sa_num = SqlMonthSum.getFlds_Month_Record_Sum();

    if (voItem != null) {
      for (int i = 0; i < sa_num.length; i++) {
        Object obj = voItem.getAttributeValue(sa_num[i]);
        UFDouble ufd1 = obj == null ? ZERO : (UFDouble)obj;

        Object obj2 = voM.getAttributeValue(sa_num[i]);
        UFDouble ufd2 = obj2 == null ? ZERO : (UFDouble)obj2;

        voM.setAttributeValue(sa_num[i], ufd1.add(ufd2));
      }
      return;
    }

    if (voLoc != null)
    {
      String[] sa = { "ninspacenum", "ninspaceassistnum", "noutspacenum", "noutspaceassistnum", "ningrossnum", "noutgrossnum", "nretnum", "nretastnum", "ntranoutnum", "ntranoutastnum" };

      assertValid(sa.length == sa_num.length);

      for (int i = 0; i < sa_num.length; i++) {
        Object obj = voLoc.getAttributeValue(sa[i]);
        UFDouble ufd1 = obj == null ? ZERO : (UFDouble)obj;

        Object obj2 = voM.getAttributeValue(sa_num[i]);
        UFDouble ufd2 = obj2 == null ? ZERO : (UFDouble)obj2;

        voM.setAttributeValue(sa_num[i], ufd1.add(ufd2));
      }

      return;
    }
  }

  private MonthVO[] getMonthVOs_Sign(GeneralBillVO vo, int iMode)
  {
    if (vo == null)
      return null;
    HashMap ht = getHashMonth(vo, true, iMode, true);

    if ((ht == null) || (ht.size() <= 0)) {
      return null;
    }
    Iterator iter = ht.entrySet().iterator();
    ArrayList al = new ArrayList();
    while (iter.hasNext()) {
      Map.Entry en = (Map.Entry)iter.next();
      al.add(en.getValue());
    }
    if (al.size() > 0) {
      return (MonthVO[])(MonthVO[])al.toArray(new MonthVO[al.size()]);
    }

    return null;
  }

  private MonthVO[] getMonthVOs(GeneralBillVO voNew, GeneralBillVO voOld, int iMode)
  {
    if ((voNew == null) && (voOld == null)) {
      return null;
    }
    UFDouble NEG = new UFDouble(-1);
    String key = null;

    HashMap htCur = getHashMonth(voNew, true, iMode, false);
    HashMap htOld = getHashMonth(voOld, false, iMode, false);

    UFDouble ZERO = new UFDouble(0);
    ArrayList alRet = new ArrayList();

    if ((htCur != null) && (htCur.size() > 0)) {
      Set keys = htCur.keySet();
      Iterator iter = keys.iterator();
      while (iter.hasNext()) {
        key = (String)iter.next();
        MonthVO vo = (MonthVO)htCur.get(key);
        MonthVO voOld_month = (MonthVO)htOld.get(key);

        if (voOld_month != null)
        {
          for (int i = 0; i < SqlMonthSum.getFlds_Month_Record_Sum().length; i++) {
            String attr = SqlMonthSum.getFlds_Month_Record_Sum()[i];
            Object obj = vo.getAttributeValue(attr);
            UFDouble ufd1 = obj == null ? ZERO : new UFDouble(obj.toString());

            Object obj2 = voOld_month.getAttributeValue(attr);
            UFDouble ufd2 = obj2 == null ? ZERO : new UFDouble(obj2.toString());

            vo.setAttributeValue(SqlMonthSum.getFlds_Month_Record_Sum()[i], ufd1.sub(ufd2));
          }

          htOld.remove(key);
        }

        alRet.add(vo);
      }

    }

    if ((htOld != null) && (htOld.size() > 0))
    {
      Set keys = htOld.keySet();
      Iterator iter = keys.iterator();
      while (iter.hasNext()) {
        key = (String)iter.next();
        MonthVO vo = (MonthVO)htOld.get(key);

        for (int i = 0; i < SqlMonthSum.getFlds_Month_Record_Sum().length; i++) {
          String attri = SqlMonthSum.getFlds_Month_Record_Sum()[i];
          UFDouble ufd = vo.getAttributeValue(attri) == null ? ZERO : (UFDouble)vo.getAttributeValue(attri);

          vo.setAttributeValue(attri, ufd.multiply(NEG));
        }
        alRet.add(vo);
      }

    }

    if ((alRet != null) && (alRet.size() > 0)) {
      MonthVO[] vos = new MonthVO[alRet.size()];
      alRet.toArray(vos);
      return vos;
    }
    return null;
  }

  public void modifyMonthData_Sign(GeneralBillVO voBill, boolean bAuditCancelAudit)
    throws BusinessException
  {
    GeneralBillVO voBillClone = voBill;

    String date_download = FixOnhandnumDMO.getDowLoadDate();
    String cbilltypecode = null;
    if ((voBill != null) && (voBill.getHeaderVO() != null)) {
      cbilltypecode = voBill.getHeaderVO().getCbilltypecode();
    }

    if (isQiChu(cbilltypecode))
    {
      voBillClone = (GeneralBillVO)voBill.clone();

      if (date_download != null) {
        throw new BusinessException(ResBase.getNotAppend());
      }

    }

    this.m_tableMode.alterTableName(1);

    String lt_record = null;
    try {
      lt_record = getMonthExecImp().queryMaxRecordYearMonth(null);
    } catch (Exception e1) {
      SCMEnv.error(e1);
      GenMethod.throwBusiException(e1);
    }
    if (lt_record == null) {
      SCMEnv.out("执行表无记录，不需要补单数据到月结表");
      return;
    }

    String lt_hand = null;
    try {
      lt_hand = getMonthExecImp().queryMaxRecordOnHand(true);
    } catch (Exception e2) {
      SCMEnv.error(e2);
      GenMethod.throwBusiException(e2);
    }

    UFDate ufdate_record = new UFDate(lt_record + "-01");
    UFDate ufdate_hand = null;
    if (lt_hand != null) {
      ufdate_hand = new UFDate(lt_hand + "-01");
    }

    GeneralBillVO von_need_record = getBillNeedBD_Sign(voBillClone, ufdate_record);

    GeneralBillVO von_need_hand = getBillNeedBD_Sign(voBillClone, ufdate_hand);

    if (von_need_record == null) {
      return;
    }

    boolean bAddSuccess = PKLock.getInstance().addDynamicLock("monthquery$@$SHARED_LOCK$@$");

    if (!bAddSuccess) {
      throw new BusinessException("月结正在进行,请稍后...");
    }

    MonthVO[] voa_record = getMonthVOs_Sign(von_need_record, 0);
    MonthVO[] voa_hand = null;

    if (!isHand(cbilltypecode))
      voa_hand = null;
    else {
      voa_hand = getMonthVOs_Sign(von_need_hand, 1);
    }

    if (!bAuditCancelAudit) {
      reverseNumDirection(voa_record);
      reverseNumDirection(voa_hand);
    }
    modifyRecordHand(voa_record, voa_hand);
  }

  private static void reverseNumDirection(MonthVO[] voa)
  {
    if ((voa == null) || (voa.length == 0))
      return;
    String[] sa = null;

    sa = SqlMonthSum.getFlds_Month_Record_Sum();

    UFDouble NEG = new UFDouble(-1);
    for (int i = 0; i < voa.length; i++)
      for (int j = 0; j < sa.length; j++) {
        Object obj = voa[i].getAttributeValue(sa[j]);
        if (obj == null)
          continue;
        if (!(obj instanceof UFDouble)) {
          continue;
        }
        voa[i].setAttributeValue(sa[j], ((UFDouble)obj).multiply(NEG));
      }
  }

  private void modifyRecordHand(MonthVO[] voa_record, MonthVO[] voa_hand)
    throws BusinessException
  {
    try
    {
      insert_record_hand_bd(voa_record, true);
    }
    catch (Exception e1) {
      SCMEnv.error(e1);
      SCMEnv.error(e1);

      GenMethod.throwBusiException(e1);
    }

    if (voa_hand == null) {
      return;
    }
    for (int y = 0; y < voa_hand.length; y++) {
      if (voa_hand[y] == null) {
        continue;
      }
      String[] sa1 = { "ninnum", "noutnum", "ninassistnum", "noutassistnum", "ningrossnum", "noutgrossnum" };

      for (int j = 0; j < sa1.length; j++) {
        Object obj = voa_hand[y].getAttributeValue(sa1[j]);
        UFDouble ufdx = obj == null ? ZERO : new UFDouble(obj.toString());

        voa_hand[y].setAttributeValue(sa1[j], ufdx);
      }

      voa_hand[y].setNonhandnum(voa_hand[y].getNinnum().sub(voa_hand[y].getNoutnum()));

      voa_hand[y].setNonhandassistnum(voa_hand[y].getNinassistnum().sub(voa_hand[y].getNoutassistnum()));

      voa_hand[y].setNgrossnum(voa_hand[y].getNingrossnum().sub(voa_hand[y].getNoutgrossnum()));
    }

    Hashtable ht = new Hashtable();
    for (int i = 0; i < voa_hand.length; i++) {
      if (voa_hand[i] == null)
        continue;
      String ym = voa_hand[i].getYearMonth();

      String[] sDate = null;
      try {
        sDate = getMonthExecImp().getAfterMonth_Hand(ym);
      } catch (Exception ex) {
        SCMEnv.error(ex);
        throw new BusinessException("MonthServ, insertRecordHand: getAfter_month_hand() error!");
      }

      if (sDate == null)
        continue;
      for (int j = 0; j < sDate.length; j++) {
        if (sDate[j] == null)
          continue;
        MonthVO vox = resetDate(voa_hand[i], sDate[j]);

        if (!ht.containsKey(sDate[j])) {
          ArrayList al = new ArrayList();
          al.add(vox);
          ht.put(sDate[j], al);
        } else {
          ArrayList al1 = (ArrayList)ht.get(sDate[j]);
          al1.add(vox);
        }
      }

    }

    if (ht.size() > 0) {
      Set set = ht.entrySet();
      Iterator iterx = set.iterator();
      while (iterx.hasNext()) {
        Map.Entry entryset = (Map.Entry)iterx.next();

        ArrayList alx = (ArrayList)entryset.getValue();
        MonthVO[] vo_x = (MonthVO[])(MonthVO[])alx.toArray(new MonthVO[alx.size()]);
        try
        {
          insert_record_hand_bd(vo_x, false);
        } catch (Exception et) {
          SCMEnv.error(et);
          GenMethod.throwBusiException(et);
        }
      }
    }
  }

  public void modifyMonthData(GeneralBillVO voNew, GeneralBillVO voOld)
    throws Exception
  {
	 String date_download = FixOnhandnumDMO.getDowLoadDate();
    String cbilltypecode = null;
    if ((voNew != null) && (voNew.getHeaderVO() != null))
      cbilltypecode = voNew.getHeaderVO().getCbilltypecode();
    else if ((voOld != null) && (voOld.getHeaderVO() != null)) {
      cbilltypecode = voOld.getHeaderVO().getCbilltypecode();
    }

    if (isQiChu(cbilltypecode))
    {
      if (date_download != null) {
        throw new BusinessException(ResBase.getNotAppend());
      }

    }

    int BILL_STATUS = -1;
    if ((voOld == null) && (voNew != null))
      BILL_STATUS = 0;
    if ((voOld != null) && (voNew != null))
      BILL_STATUS = 1;
    if ((voOld != null) && (voNew == null))
      BILL_STATUS = 2;
    if (BILL_STATUS == -1)
      return;
    this.m_tableMode.alterTableName(0);

    String lt_record = getMonthExecImp().queryMaxRecordYearMonth(null);
    if (lt_record == null) {
      SCMEnv.out("执行表无记录，不需要补单数据到月结表");
      return;
    }
    String lt_hand = getMonthExecImp().queryMaxRecordOnHand(true);

    UFDate ufdate_record = new UFDate(lt_record + "-01");
    UFDate ufdate_hand = null;
    if (lt_hand != null) {
      ufdate_hand = new UFDate(lt_hand + "-01");
    }

    GeneralBillVO von_need_record = null;
    GeneralBillVO von_need_hand = null;
    GeneralBillVO voo_need_record = null;
    GeneralBillVO voo_need_hand = null;

    boolean isHand = isHand(cbilltypecode);
    von_need_record = getBillNeedBD(voNew, ufdate_record);

    if (!isHand)
      von_need_hand = null;
    else {
      von_need_hand = getBillNeedBD(voNew, ufdate_hand);
    }
    voo_need_record = getBillNeedBD(voOld, ufdate_record);
    if (!isHand)
      voo_need_hand = null;
    else {
      voo_need_hand = getBillNeedBD(voOld, ufdate_hand);
    }

    if ((von_need_record == null) && (voo_need_record == null)) {
      return;
    }

    boolean bAddSuccess = PKLock.getInstance().addDynamicLock("monthquery$@$SHARED_LOCK$@$");

    if (!bAddSuccess) {
      throw new BusinessException("月结正在进行,请稍后...");
    }

    MonthVO[] voa_record = getMonthVOs(von_need_record, voo_need_record, 0);
    MonthVO[] voa_hand = getMonthVOs(von_need_hand, voo_need_hand, 1);
    if (voa_record == null) {
      return;
    }
    modifyRecordHand(voa_record, voa_hand);
  }

  public void modifyRecordRetNum(String[] sBillItemPKs, UFDouble[] dNums, UFDouble[] dAstNums)
    throws BusinessException
  {
    if ((sBillItemPKs == null) || (sBillItemPKs.length == 0) || (dNums == null) || (dNums.length == 0))
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008busi", "UPP4008busi-000077"));
    try {
      HashMap hm = queryMonthVOs(sBillItemPKs);
      ArrayList al = new ArrayList();
      ArrayList alsign = new ArrayList();
      MonthVO vohead = null;

      for (int i = 0; i < sBillItemPKs.length; i++) {
        if (hm.containsKey(sBillItemPKs[i])) {
          vohead = (MonthVO)hm.get(sBillItemPKs[i]);

          if ((dNums != null) && (dNums[i] != null))
            vohead.setNretnum(dNums[i]);
          if ((dAstNums != null) && (dAstNums[i] != null)) {
            vohead.setNretastnum(dAstNums[i]);
          }
          al.add(vohead);
        }
        if (hm.containsKey(sBillItemPKs[i] + "_sign")) {
          vohead = (MonthVO)hm.get(sBillItemPKs[i] + "_sign");

          if ((dNums != null) && (dNums[i] != null))
            vohead.setNretnum(dNums[i]);
          if ((dAstNums != null) && (dAstNums[i] != null)) {
            vohead.setNretastnum(dAstNums[i]);
          }
          alsign.add(vohead);
        }

      }

      if (al.size() > 0) {
        MonthVO[] vos = new MonthVO[al.size()];
        al.toArray(vos);

        boolean bAddSuccess = PKLock.getInstance().addDynamicLock("monthquery$@$SHARED_LOCK$@$");

        if (!bAddSuccess) {
          throw new BusinessException("月结正在进行,请稍后...");
        }

        this.m_tableMode.alterTableName(0);
        modifyRecordHand(vos, null);
      }

      if (alsign.size() > 0) {
        MonthVO[] vos = new MonthVO[alsign.size()];
        alsign.toArray(vos);

        this.m_tableMode.alterTableName(1);
        modifyRecordHand(vos, null);
      }
    }
    catch (Exception e)
    {
      throw new BusinessException(e.getMessage());
    }
  }

  public void modifyRecordTranoutNum(String[] sBillItemPKs, UFDouble[] dNums, UFDouble[] dAstNums)
    throws BusinessException
  {
    if ((sBillItemPKs == null) || (sBillItemPKs.length == 0) || (dNums == null) || (dNums.length == 0))
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008busi", "UPP4008busi-000077"));
    try {
      HashMap hm = queryMonthVOs(sBillItemPKs);
      ArrayList al = new ArrayList();
      ArrayList alsign = new ArrayList();
      MonthVO vohead = null;

      for (int i = 0; i < sBillItemPKs.length; i++) {
        if (hm.containsKey(sBillItemPKs[i])) {
          vohead = (MonthVO)hm.get(sBillItemPKs[i]);

          if ((dNums != null) && (dNums[i] != null))
            vohead.setNtrannum(dNums[i]);
          if ((dAstNums != null) && (dAstNums[i] != null)) {
            vohead.setNtranastnum(dAstNums[i]);
          }
          al.add(vohead);
        }
        if (hm.containsKey(sBillItemPKs[i] + "_sign")) {
          vohead = (MonthVO)hm.get(sBillItemPKs[i] + "_sign");

          if ((dNums != null) && (dNums[i] != null))
            vohead.setNtrannum(dNums[i]);
          if ((dAstNums != null) && (dAstNums[i] != null)) {
            vohead.setNtranastnum(dAstNums[i]);
          }
          alsign.add(vohead);
        }

      }

      if (al.size() > 0) {
        MonthVO[] vos = new MonthVO[al.size()];
        al.toArray(vos);

        boolean bAddSuccess = PKLock.getInstance().addDynamicLock("monthquery$@$SHARED_LOCK$@$");

        if (!bAddSuccess) {
          throw new BusinessException("月结正在进行,请稍后...");
        }

        this.m_tableMode.alterTableName(0);
        modifyRecordHand(vos, null);
      }

      if (alsign.size() > 0) {
        MonthVO[] vos = new MonthVO[alsign.size()];
        alsign.toArray(vos);

        this.m_tableMode.alterTableName(1);
        modifyRecordHand(vos, null);
      }
    }
    catch (Exception e)
    {
      throw new BusinessException(e.getMessage());
    }
  }

  private MonthVO resetDate(MonthVO vo, String date)
    throws BusinessException
  {
    if ((vo == null) || (date == null)) {
      throw new IllegalArgumentException("resetDate args error");
    }
    MonthVO voClone = null;
    try {
      voClone = (MonthVO)ObjectUtils.serializableClone(vo);
    }
    catch (Exception e) {
      Logger.error(e.getMessage() + " JAMLEE ", e);
      SCMEnv.warn(e);
      throw new BusinessException("SmartVO clone出错!:MonthServ:resetDate");
    }
    voClone.setAttributeValue("dyearmonth", date.trim());
    return voClone;
  }

  private Integer execUpdate(String sql) throws SQLException
  {
    CrossDBConnection con = null;
    PreparedStatement stmt = null;
    int iReturnCode = -1;
    try {
      con = (CrossDBConnection)getConnection();
      con.setAddTimeStamp(false);
      stmt = con.prepareStatement(sql);

      iReturnCode = stmt.executeUpdate();
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception e) {
      }
      try {
        if (con != null) {
          con.setAddTimeStamp(true);
          con.close();
        }
      } catch (Exception e) {
      }
    }
    return new Integer(iReturnCode);
  }

  private void insertRecordTableByDate(UFDate date)
    throws Exception
  {
    if (date == null) {
      return;
    }
    String sWhereDate = "";
    if (this.m_tableMode.m_iMode == 0) {
      sWhereDate = "  b.dbizdate >= '" + YearMonthCtrl.getBeginDate(date) + "' and " + " b.dbizdate <= '" + YearMonthCtrl.getEndDate(date) + "'";
    }
    else if (this.m_tableMode.m_iMode == 1) {
      sWhereDate = "  h.daccountdate >= '" + YearMonthCtrl.getBeginDate(date) + "' and " + " h.daccountdate <= '" + YearMonthCtrl.getEndDate(date) + "'";
    }

    String sql1 = SqlMonthSum.getInsertMonthSql_LocMgt(sWhereDate, this.m_tableMode,"1016");

    String sql2 = SqlMonthSum.getInsertMonthSql_NotLocMgt(sWhereDate, this.m_tableMode,"1016");

    String sql3 = SqlMonthSum.getInsertMonthSql_RetTranLocMgt(sWhereDate, this.m_tableMode,"1016");

    execUpdate(sql1);
    execUpdate(sql2);
    execUpdate(sql3);
  }

  private void insert_record_hand_bd(MonthVO[] voa, boolean bRecord)
    throws SQLException
  {
    if ((voa == null) || (voa.length == 0)) {
      return;
    }
    String sql = null;
    if (bRecord) {
      sql = "insert into " + this.m_tableMode.IC_MONTH_RECORD + " (A) values(B)";
    }
    else {
      sql = "insert into " + this.m_tableMode.IC_MONTH_HAND + " (A) values(B)";
    }
    QryCondStr kv = new QryCondStr();

    if (bRecord)
      kv.set("A", SqlMonthSum.getFld_Month_Record_All());
    else {
      kv.set("A", SqlMonthSum.getFld_Month_Hand_ALL());
    }
    int len = -1;
    if (bRecord)
      len = SqlMonthSum.getFlds_Month_Record_All().length;
    else {
      len = SqlMonthSum.getFlds_Month_Hand_All().length;
    }
    String[] sa = new String[len];
    for (int i = 0; i < sa.length; i++) {
      sa[i] = "?";
    }
    String strValues = SQLUtil.getStr(sa);

    kv.set("B", strValues);

    String sql_formal = SqlHelper.replace(sql, kv);

    MonthVO vo = null;

    String[] sa_names = null;
    if (bRecord)
      sa_names = SqlMonthSum.getFlds_Month_Record_All();
    else {
      sa_names = SqlMonthSum.getFlds_Month_Hand_All();
    }
    CrossDBConnection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      con = (CrossDBConnection)getConnection();

      stmt = prepareStatement(con, sql_formal);

      for (int i = 0; i < voa.length; i++) {
        vo = voa[i];

        for (int k = 0; k < sa_names.length; k++) {
          if ((!sa_names[k].endsWith("num")) && (!sa_names[k].equals("hsl")) && (!sa_names[k].equals("fbillflag")))
          {
            if (vo.getAttributeValue(sa_names[k]) == null)
              stmt.setNull(k + 1, 1);
            else {
              stmt.setString(k + 1, (String)vo.getAttributeValue(sa_names[k]));
            }

          }
          else if ((sa_names[k].endsWith("num")) || (sa_names[k].equals("hsl")))
          {
            if (vo.getAttributeValue(sa_names[k]) == null)
              stmt.setNull(k + 1, 2);
            else {
              stmt.setBigDecimal(k + 1, ((UFDouble)vo.getAttributeValue(sa_names[k])).toBigDecimal());
            }

          }
          else if (sa_names[k].equals("fbillflag")) {
            if (vo.getAttributeValue(sa_names[k]) == null)
              stmt.setNull(k + 1, 5);
            else {
              stmt.setInt(k + 1, ((Integer)vo.getAttributeValue(sa_names[k])).intValue());
            }

          }

        }

        executeUpdate(stmt);
      }

      executeBatch(stmt);
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception e) {
      }
      try {
        if (con != null)
        {
          con.close();
        }
      } catch (Exception e) {
      }
      try {
        if (rs != null)
          rs.close();
      } catch (Exception e) {
      }
    }
  }

  private HashMap queryMonthVOs(String[] sBillItemPKs) throws SQLException, BusinessException, SystemException {
    if ((sBillItemPKs == null) || (sBillItemPKs.length == 0)) {
      return new HashMap();
    }

    MonthVO voItem = null;
    HashMap hm = new HashMap();
    StringBuffer sbSql = new StringBuffer(" select body.cgeneralbid,daccountdate,dbizdate , cbilltypecode, ");

    String[] headflds = SqlMonthSum.getFlds_General_H_Record();
    String[] bodyflds = SqlMonthSum.getFlds_General_B_Record();

    if (headflds != null) {
      for (int i = 0; i < headflds.length; i++)
      {
        sbSql.append("head." + headflds[i]);
        if (i < headflds.length - 1) {
          sbSql.append(",");
        }
      }
    }

    if (bodyflds != null) {
      if (headflds != null) {
        sbSql.append(",");
      }
      for (int i = 0; i < bodyflds.length; i++)
      {
        sbSql.append("body." + bodyflds[i]);
        if (i < bodyflds.length - 1) {
          sbSql.append(",");
        }
      }
    }

    sbSql.append(" from ic_general_h head,ic_general_b body where head.cgeneralhid=body.cgeneralhid and head.dr=0 and body.dr=0 ");
    sbSql.append(GeneralSqlString.formInSQL("body.cgeneralbid", sBillItemPKs));

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(sbSql.toString());

      rs = stmt.executeQuery();
      String bid = null;
      GenMethod gm = new GenMethod();
      ResultSetMetaData meta = rs.getMetaData();
      String begindate = getAccoutBeginDate().toString().substring(0, 7);
      while (rs.next())
      {
        bid = rs.getString(1);
        String dsigndate = rs.getString(2);
        String dbizdate = rs.getString(3);
        voItem = new MonthVO();
        gm.setData(rs, voItem, meta, 4);

        if (isQiChu((String)voItem.getAttributeValue("cbilltypecode"))) {
          voItem.setAttributeValue("dyearmonth", begindate);
          hm.put(bid, voItem);
          hm.put(bid + "_sign", voItem);
        }
        else
        {
          if (dbizdate != null) {
            dbizdate = dbizdate.substring(0, 7);
            voItem.setAttributeValue("dyearmonth", dbizdate);
            hm.put(bid, voItem);
          }

          if (dsigndate != null) {
            dsigndate = dsigndate.substring(0, 7);
            if (!dsigndate.equals(dbizdate)) {
              MonthVO voItem1 = (MonthVO)voItem.clone();
              voItem1.setAttributeValue("dyearmonth", dsigndate);
              hm.put(bid + "_sign", voItem1);
            }

          }

        }

        hm.put(bid, voItem);
      }

    }
    finally
    {
      try
      {
        if (rs != null)
          rs.close();
      } catch (Exception e) {
        SCMEnv.error(e);
      }
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception e) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception e) {
      }
    }
    return hm;
  }

  public static abstract interface IBillACT
  {
    public static final int NEW = 0;
    public static final int UPDATE = 1;
    public static final int DELETE = 2;
  }
}