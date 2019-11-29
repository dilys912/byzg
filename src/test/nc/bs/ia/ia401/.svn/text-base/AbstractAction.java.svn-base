package nc.bs.ia.ia401;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.naming.NamingException;
import nc.bs.ia.ia501.IaInoutledgerDAO;
import nc.bs.ia.pub.DataAccessUtils;
import nc.bs.ia.pub.IAContext;
import nc.bs.ia.pub.IATool;
import nc.bs.ia.pub.LockOperator;
import nc.bs.ia.pub.TempTableDefine;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.pub.SystemException;
import nc.bs.scm.pub.bill.BillCodeDMO;
import nc.vo.ia.bill.BillVO;
import nc.vo.ia.ia303.MonthAverVO;
import nc.vo.ia.ia304.PlanedPriceVO;
import nc.vo.ia.ia501.IaInoutledgerVO;
import nc.vo.ia.pub.ExceptionUtils;
import nc.vo.ia.pub.IRowSet;
import nc.vo.ia.pub.SqlBuilder;
import nc.vo.ia.pub.SystemAccessException;
import nc.vo.ia.pub.TimeLog;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;

public abstract class AbstractAction
{
  protected IRowSet getAuditBatch(String tempTable, IAContext context)
  {
    SqlBuilder sql = new SqlBuilder();
    sql.append(" select count( v.cbill_bid ),v.crdcenterid,v.cinventoryid ");
    sql.append(" from v_ia_inoutledger v , ");
    sql.append(tempTable);
    sql.append(" b where v.crdcenterid = b.crdcenterid ");
    sql.append(" and v.cinventoryid = b.cinventoryid ");
    sql.append(" and v.dauditdate", ">= ", context.getBeginDate());
    sql.append(" and v.dauditdate", "<= ", context.getEndDate());
    sql.append(" and v.caccountyear", context.getAuditYear());
    sql.append(" and v.caccountmonth", context.getAuditMonth());
    sql.append(" and v.pk_corp", context.getCorp());
    int[] getmodel = new int[3];
    getmodel[0] = 5;
    getmodel[1] = 7;
    getmodel[2] = 12;
    sql.append(" and v.fdatagetmodelflag ", getmodel);

    sql.append(" group by v.crdcenterid,v.cinventoryid ");
    sql.append(" order by v.crdcenterid,v.cinventoryid ");

    IRowSet rowset = DataAccessUtils.query(sql.toString());
    return rowset;
  }

  protected void isAccountOK(IAContext context)
  {
    String period = context.getAuditYear() + "-" + context.getAuditMonth();

    if (context.getUnClosePeriod().compareTo(period) > 0)
    {
      String[] args = new String[2];
      args[0] = context.getAuditYear();
      args[1] = context.getAuditMonth();
      String message = NCLangResOnserver.getInstance().getStrByID("20146030", "UPP20146030-000062", null, args);

      ExceptionUtils.wrappBusinessException(message);
    }
    checkDate(context);
  }

  private void checkDate(IAContext context) {
    SqlBuilder sql = new SqlBuilder();
    sql.append(" select dauditdate from ia_bill_b where ");

    sql.append(" iauditsequence", context.getAuditSequence());
    sql.append(" and pk_corp", context.getCorp());
    sql.append(" and dr=0 ");
    sql.append(" and dauditdate", ">=", context.getBeginDate());
    sql.append(" and dauditdate", "<=", context.getEndDate());
    sql.append(" order by dauditdate desc ");
    IRowSet rowset = DataAccessUtils.query(sql.toString());
    if (rowset.next()) {
      String date = rowset.getString(0);
      if (context.getAuditDate().compareTo(date) < 0) {
        String[] args = new String[2];
        args[0] = context.getAuditDate();
        args[1] = date;
        String message = NCLangResOnserver.getInstance().getStrByID("20146010", "UPP20146010-000126", null, args);

        ExceptionUtils.wrappBusinessException(message);
      }
    }
  }

  protected LockOperator lock(PlanedPriceVO[] planvos, MonthAverVO[] monthvos, IAContext context)
  {
    LockOperator lock = new LockOperator(context.getAuditUser());

    String[] generalAccount = null;

    String sMes = context.getAuditUser() + context.getCorp() + "ENDHANDLE";

    if ((planvos != null) && (planvos.length != 0)) {
      sMes = sMes + 5;
      int length = planvos.length;
      generalAccount = new String[length];
      for (int i = 0; i < length; i++) {
        generalAccount[i] = (planvos[i].getCrdcenterid() + planvos[i].getCinventoryid());
      }

    }
    else if ((monthvos != null) && (monthvos.length != 0)) {
      sMes = sMes + 4;
      int length = monthvos.length;
      generalAccount = new String[length];
      for (int i = 0; i < length; i++) {
        generalAccount[i] = (monthvos[i].getCrdcenterid() + monthvos[i].getCinventoryid());
      }

    }

    IATool.getInstance().lockGeneralAccount(generalAccount, lock);

    String[] lockIDs = { sMes };

    String message = NCLangResOnserver.getInstance().getStrByID("20146030", "UPP20146030-000072");

    lock.lock(lockIDs, message);

    lockIDs = new String[] { context.getCorp() + "AUDIT" };

    message = NCLangResOnserver.getInstance().getStrByID("20146030", "UPP20146030-000072");

    lock.lock(lockIDs, message);

    lockIDs = new String[] { context.getCorp() + "ACCOUNT" };

    boolean sucess = lock.lock(lockIDs);

    if (!sucess) {
      lockIDs = new String[] { context.getCorp() + "ENDHANDLEACCOUNT" };

      message = NCLangResOnserver.getInstance().getStrByID("20146030", "UPP20146030-000072");

      lock.lock(lockIDs, message);

      lock.unlock(lockIDs);
    }
    return lock;
  }

  protected String initJHJData(IAContext context, PlanedPriceVO[] vos) {
    List list = new ArrayList();
    int size = vos.length;
    for (int i = 0; i < size; i++) {
      String[] row = new String[6];
      row[0] = context.getCorp();
      row[1] = context.getAuditUser();
      row[2] = vos[i].getCrdcenterid();
      row[3] = vos[i].getCinventoryid();
      BigDecimal rate = vos[i].getNdifrate();
      if (rate == null) {
        continue;
      }
      rate = context.adjustDiffRate(rate);
      vos[i].setNdifrate(rate);
      row[4] = rate.toString();
      list.add(row);
    }
    size = list.size();
    String[][] data = new String[size][6];
    data = (String[][])(String[][])list.toArray(data);

    TempTableDefine define = new TempTableDefine();
    String tempTable = define.getEndHandleTable(data);
    return tempTable;
  }

  protected String initMonthData(IAContext context, MonthAverVO[] vos) {
    List list = new ArrayList();
    int size = vos.length;
    for (int i = 0; i < size; i++) {
      String[] row = new String[6];
      row[0] = context.getCorp();
      row[1] = context.getAuditUser();
      row[2] = vos[i].getCrdcenterid();
      row[3] = vos[i].getCinventoryid();

      UFDouble nmonthprice = vos[i].getNmonthprice();
      if (nmonthprice == null) {
        continue;
      }
      nmonthprice = context.adjustPrice(nmonthprice);
      vos[i].setNmonthprice(nmonthprice);
      row[5] = nmonthprice.toString();
      list.add(row);
    }
    size = list.size();
    String[][] data = new String[size][6];
    data = (String[][])(String[][])list.toArray(data);

    TempTableDefine define = new TempTableDefine();
    String tempTable = define.getEndHandleTable(data);
    return tempTable;
  }

  protected IaInoutledgerVO[] getReturnBillCodeLedgerVOs(IAContext context, String temptable, boolean isPlanedInventory)
  {
    IaInoutledgerVO[] ledgerVOs = new IaInoutledgerVO[0];
    IaInoutledgerDAO dao = new IaInoutledgerDAO();
    SqlBuilder sql = new SqlBuilder();
    sql.append(",");
    sql.append(temptable);
    sql.append(" e  where ");
    sql.append(" a.pk_corp", context.getCorp());
    sql.append(" and a.dauditdate ", ">=", context.getBeginDate());
    sql.append(" and a.dauditdate ", "<=", context.getEndDate());
    sql.append(" and a.caccountyear", context.getAuditYear());
    sql.append(" and a.caccountmonth", context.getAuditMonth());
    sql.append(" and a.crdcenterid = e.crdcenterid ");
    sql.append(" and a.cinventoryid = e.cinventoryid");

    sql.append(" and a.fdispatchflag", 1);
    sql.append(" and a.fdatagetmodelflag", 7);

    if (isPlanedInventory) {
      String[] cbilltypecodes = new String[2];
      cbilltypecodes[0] = "IE";
      cbilltypecodes[1] = "IA";
      sql.append(" and a.cbilltypecode", cbilltypecodes);
    }
    else {
      sql.append(" and a.cbilltypecode", "IA");
    }
    ledgerVOs = dao.queryWithField(sql.toString());
    return ledgerVOs;
  }

  protected void returnBillCode(IaInoutledgerVO[] ledgerVOs) {
    int length = ledgerVOs.length;
    try {
      BillCodeDMO dmo = new BillCodeDMO();
      for (int i = 0; i < length; i++) {
        BillVO bill = ledgerVOs[i].changeViewToBill();
        String billCode = bill.getVBillCode();
        if (billCode != null)
          dmo.returnBillCodeOnDelete(bill);
      }
    }
    catch (SystemException ex)
    {
      throw new SystemAccessException(ex);
    }
    catch (NamingException ex) {
      throw new SystemAccessException(ex);
    }
    catch (BusinessException ex) {
      throw new SystemAccessException(ex);
    }
  }

  protected void doHuge(PlanedPriceVO[] planvos, MonthAverVO[] monthvos, IAContext context, String temptable)
  {
    TimeLog.logStart();
    IRowSet rowset = getAuditBatch(temptable, context);
    TimeLog.info("存货分组");

    boolean isPlanedInventory = false;

    if (planvos != null) {
      isPlanedInventory = true;
    }

    int count = 0;
    List batch = new ArrayList();
    Map map = new HashMap();
    if (isPlanedInventory) {
      for (int i = 0; i < planvos.length; i++) {
        String key = planvos[i].getCrdcenterid() + planvos[i].getCinventoryid();
        map.put(key, planvos[i]);
      }
    }
    else {
      for (int i = 0; i < monthvos.length; i++) {
        String key = monthvos[i].getCrdcenterid() + monthvos[i].getCinventoryid();

        map.put(key, monthvos[i]);
      }
    }

    Set set = new HashSet();
    while (rowset.next()) {
      int size = rowset.getInt(0);
      count += size;
      if (count > 40000)
      {
        if (batch.size() > 0) {
          doHuge(batch, isPlanedInventory, context);
          batch.clear();
          count = 0;
        }
      }
      String crdcenterid = rowset.getString(1);
      String cinventoryid = rowset.getString(2);
      Object obj = map.get(crdcenterid + cinventoryid);
      if (obj != null) {
        batch.add(obj);
        set.add(crdcenterid + cinventoryid);
      }
    }
    if (batch.size() > 0) {
      doHuge(batch, isPlanedInventory, context);
      batch.clear();
    }
    List list = new ArrayList();
    if (isPlanedInventory) {
      for (int i = 0; i < planvos.length; i++) {
        String key = planvos[i].getCrdcenterid() + planvos[i].getCinventoryid();
        if (set.contains(key)) {
          list.add(planvos[i]);
        }
      }
    }
    else {
      for (int i = 0; i < monthvos.length; i++) {
        String key = monthvos[i].getCrdcenterid() + monthvos[i].getCinventoryid();

        if (!set.contains(key)) {
          list.add(monthvos[i]);
        }
      }
    }
    if (list.size() > 0) {
      PlanedPriceVO[] n_planvos = null;
      MonthAverVO[] n_monthvos = null;
      if (isPlanedInventory) {
        n_planvos = new PlanedPriceVO[list.size()];
        n_planvos = (PlanedPriceVO[])(PlanedPriceVO[])list.toArray(n_planvos);
      }
      else {
        n_monthvos = new MonthAverVO[list.size()];
        n_monthvos = (MonthAverVO[])(MonthAverVO[])list.toArray(n_monthvos);
      }

      GeneralAccountEndDataOperator operator = new GeneralAccountEndDataOperator(n_planvos, n_monthvos);

      operator.process();
    }
  }

  private void doHuge(List list, boolean isPlanedInventory, IAContext context) {
    int size = list.size();
    PlanedPriceVO[] planvos = null;
    MonthAverVO[] monthvos = null;
    String temptable = null;
    TimeLog.logStart();
    if (isPlanedInventory) {
      planvos = new PlanedPriceVO[size];
      planvos = (PlanedPriceVO[])(PlanedPriceVO[])list.toArray(planvos);
      temptable = initJHJData(context, planvos);
    }
    else {
      monthvos = new MonthAverVO[size];
      monthvos = (MonthAverVO[])(MonthAverVO[])list.toArray(monthvos);
      temptable = initMonthData(context, monthvos);
    }
    TimeLog.info("初始化临时表");

    doDetail(temptable, context, isPlanedInventory);
  }

  protected abstract void doDetail(String paramString, IAContext paramIAContext, boolean paramBoolean);
}