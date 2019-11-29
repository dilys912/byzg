package nc.bs.ia.ia301.command;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import nc.bs.ia.ia301.AuditSession;
import nc.bs.ia.ia301.AuditUtils;
import nc.bs.ia.ia301.Status;
import nc.bs.ia.ia301.account.AccountOperator;
import nc.bs.ia.ia301.check.AuditChecker;
import nc.bs.ia.ia301.cost.Cost;
import nc.bs.ia.ia301.persistent.Persistent;
import nc.bs.ia.pub.DataAccessUtils;
import nc.bs.ia.pub.IAContext;
import nc.bs.ia.pub.IATool;
import nc.bs.ia.pub.IaInoutledgerVOQuery;
import nc.bs.ia.pub.LockOperator;
import nc.bs.ia.pub.TempTableDefine;
import nc.bs.ml.NCLangResOnserver;
import nc.vo.ia.ia301.AuditResultVO;
import nc.vo.ia.ia501.IaInoutledgerVO;
import nc.vo.ia.pub.ExceptionUtils;
import nc.vo.ia.pub.IRowSet;
import nc.vo.ia.pub.NeedHandInputException;
import nc.vo.ia.pub.SqlBuilder;
import nc.vo.ia.pub.SystemAccessException;
import nc.vo.ia.pub.TimeLog;
import nc.vo.pub.BusinessException;

public class OtherTransactionBillAccount
{
  private IAContext context = null;

  private VoucherOperator voucherOperator = null;

  public OtherTransactionBillAccount(IAContext context)
  {
    this.context = context;
    this.voucherOperator = new VoucherOperator(context);
  }

  public AuditResultVO[] calculate(String[] cbill_bids) {
    AuditUtils.isOKAccount(this.context);
    String sign = Status.createKey(this.context.getCorp(), this.context.getAuditUser());

    String message = NCLangResOnserver.getInstance().getStrByID("20146010", "UPP20146010-000058");

    Status.getHero().setState(sign, message, 1);

    LockOperator lock = new LockOperator(this.context.getAuditUser());
    AuditResultVO[] vos = null;
    try
    {
      IATool.getInstance().lockBillItems(cbill_bids, lock);

      AuditUtils.lockNode(this.context, lock);

      if (cbill_bids.length > 20000) {
        vos = doHuge(lock, cbill_bids);
      }
      else {
        vos = doNormal(lock, cbill_bids);
      }
    }
    finally
    {
      lock.unlock();
    }
    if (this.context.isCanGenerateSSPZ()) {
      message = NCLangResOnserver.getInstance().getStrByID("20146010", "UPP20146010-000063");

      Status.getHero().setState(sign, message, 79);
    }

    this.voucherOperator.generateSSPZ(true);
    message = NCLangResOnserver.getInstance().getStrByID("20146010", "UPP20146010-000060");

    Status.getHero().setState(sign, message, 100);
    return vos;
  }

  private AuditResultVO[] doHuge(LockOperator lock, String[] cbill_bids) {
    List resultList = new ArrayList();
    List batchList = new ArrayList();

    TempTableDefine tempDefine = new TempTableDefine();
    String billBodyTempTable = tempDefine.getBillQueryTable(cbill_bids);

    IRowSet rowset = getAuditBatch(billBodyTempTable);
    int length = rowset.size();
    int count = 0;
    int cursor = 0;
    int start = 0;

    Set hasLockedBil = new HashSet();

    while (rowset.next()) {
      int size = rowset.getInt(0);
      count += size;
      if (count > 20000)
      {
        if (batchList.size() > 20000) {
          String[][] auditBatchs = new String[batchList.size()][3];
          auditBatchs = (String[][])(String[][])batchList.toArray(auditBatchs);
          IaInoutledgerVO[] ledgerVOs = getLeadgerVOs(auditBatchs, billBodyTempTable);

          lockBillHeader(ledgerVOs, hasLockedBil, lock);

          AuditResultVO[] ret = compute(lock, ledgerVOs, start, length);
          for (int i = 0; i < ret.length; i++) {
            resultList.add(ret[i]);
          }

          batchList.clear();
          count = 0;
          start = cursor;
        }
      }
      String[] row = new String[3];
      row[0] = rowset.getString(1);
      row[1] = rowset.getString(2);
      batchList.add(row);
      cursor++;
    }
    if (batchList.size() > 0) {
      String[][] auditBatchs = new String[batchList.size()][3];
      auditBatchs = (String[][])(String[][])batchList.toArray(auditBatchs);
      IaInoutledgerVO[] ledgerVOs = getLeadgerVOs(auditBatchs, billBodyTempTable);

      lockBillHeader(ledgerVOs, hasLockedBil, lock);
      AuditResultVO[] ret = compute(lock, ledgerVOs, start, length);
      for (int i = 0; i < ret.length; i++) {
        resultList.add(ret[i]);
      }

      batchList.clear();
    }

    AuditResultVO[] vos = new AuditResultVO[resultList.size()];
    vos = (AuditResultVO[])(AuditResultVO[])resultList.toArray(vos);

    return vos;
  }

  private AuditResultVO[] doNormal(LockOperator lock, String[] cbill_bids) {
    String sign = Status.createKey(this.context.getCorp(), this.context.getAuditUser());

    String message = NCLangResOnserver.getInstance().getStrByID("20146010", "UPP20146010-000061");

    Status.getHero().setState(sign, message, 7);

    TimeLog.logStart();
    TempTableDefine tempDefine = new TempTableDefine();
    String tableName = tempDefine.getBillQueryTable(cbill_bids);
    StringBuffer sql = new StringBuffer();
    sql.append(" , ");
    sql.append(tableName);
    sql.append(" temptable where  ");
    sql.append(" b.cbill_bid=temptable.cbill_bid ");
    IaInoutledgerVOQuery query = new IaInoutledgerVOQuery();
    IaInoutledgerVO[] ledgerVOs = query.query(sql.toString(), " order by temptable.sequence ");

    TimeLog.info("获取单据");
    if ((ledgerVOs == null) || (ledgerVOs.length == 0)) {
      message = NCLangResOnserver.getInstance().getStrByID("20146010", "UPP20146010-000062");

      ExceptionUtils.wrappBusinessException(message);
    }
    AuditResultVO[] vos = compute(lock, ledgerVOs, 0, cbill_bids.length);
    return vos;
  }

  private IaInoutledgerVO[] getLeadgerVOs(String[][] auditBatchs, String billBodyTempTable)
  {
    TimeLog.logStart();
    TempTableDefine tempDefine = new TempTableDefine();
    String tableName = tempDefine.getAuditBatchTable(auditBatchs);

    StringBuffer sql = new StringBuffer();
    sql.append(" , ");
    sql.append(billBodyTempTable);
    sql.append(" temptable1, ");
    sql.append(tableName);
    sql.append(" temptable2 where  ");
    sql.append(" b.cbill_bid=temptable1.cbill_bid ");
    sql.append(" and h.crdcenterid= temptable2.crdcenterid ");
    sql.append(" and b.cinventoryid = temptable2.cinventoryid ");
    IaInoutledgerVOQuery query = new IaInoutledgerVOQuery();
    IaInoutledgerVO[] ledgerVOs = query.query(sql.toString(), " order by temptable1.sequence ");

    TimeLog.info("获取单据");
    return ledgerVOs;
  }

  private IRowSet getAuditBatch(String tempTable) {
    SqlBuilder sql = new SqlBuilder();
    sql.append(" select count( v.cbill_bid ),crdcenterid,cinventoryid ");
    sql.append(" from v_ia_inoutledger v , ");
    sql.append(tempTable);
    sql.append(" b where v.cbill_bid = b.cbill_bid ");
    sql.append(" group by v.crdcenterid,v.cinventoryid ");
    sql.append(" order by v.crdcenterid,v.cinventoryid ");

    IRowSet rowset = DataAccessUtils.query(sql.toString());
    return rowset;
  }

  private AuditResultVO[] compute(LockOperator lock, IaInoutledgerVO[] ledgerVOs, int start, int all)
  {
    TimeLog.logStart();

    AuditSession[] sessions = AuditUtils.constructSessions(this.context, ledgerVOs);

    AuditUtils.initJHJ(sessions);

    AuditUtils.initAssistantLedgerVO(sessions);

    TimeLog.info("构造成本计算批次数组");

    IATool.getInstance().lockGeneralAccount(sessions, lock);

    AuditResultVO[] vos = compute(sessions, start, all);

    this.voucherOperator.collectVoucherBill_BIDs(sessions);
    return vos;
  }

  private AuditResultVO[] compute(AuditSession[] sessions, int start, int all) {
    int length = sessions.length;
    String sign = Status.createKey(this.context.getCorp(), this.context.getAuditUser());

    TimeLog.logStart();
    List list = new ArrayList();
    for (int i = 0; i < length; i++) {
      String[] args = new String[2];
      args[0] = String.valueOf(i + 1 + start);
      args[1] = String.valueOf(all);
      String message = NCLangResOnserver.getInstance().getStrByID("20146010", "UPP20146010-000127", null, args);

      int rate = (int)((start + i + 1.0D) / all * 60.0D + 8.0D);

      Status.getHero().setState(sign, message, rate);

      TimeLog.logStart();

      AuditResultVO vo = calculateSessionInOtherTransaction(sessions[i]);
      TimeLog.info("成本计算分批次" + (start + i));

      list.add(vo);
    }
    TimeLog.info("成本计算总耗时" + start);
    Persistent persistent = new Persistent();
    persistent.persistent(sessions);

    int size = list.size();
    AuditResultVO[] vos = new AuditResultVO[size];
    vos = (AuditResultVO[])(AuditResultVO[])list.toArray(vos);
    return vos;
  }

  private void lockBillHeader(IaInoutledgerVO[] ledgerVOs, Set hasLockedBill, LockOperator lock)
  {
    TimeLog.logStart();
    int length = ledgerVOs.length;
    HashSet set = new HashSet();
    for (int i = 0; i < length; i++) {
      String cbillid = ledgerVOs[i].getCbillid();
      if (!hasLockedBill.contains(cbillid)) {
        set.add(cbillid);
      }
    }
    length = set.size();
    String[] billIDs = new String[length];
    billIDs = (String[])(String[])set.toArray(billIDs);

    IATool.getInstance().lockBillHeaders(billIDs, lock);
    TimeLog.info("锁定单据主表ID");
    hasLockedBill.addAll(set);
  }

  private AuditResultVO calculateSessionInOtherTransaction(AuditSession session)
  {
    AuditChecker checker = new AuditChecker(session);
    Cost cost = new Cost(session);
    AccountOperator accountOperator = new AccountOperator(session);
    List list = session.getInoutledgerVOs();
    Iterator iterator = list.iterator();
    int index = 0;
    String message = null;
    Throwable cause = null;
    boolean isNeedHanInput = false;
    while (iterator.hasNext()) {
      IaInoutledgerVO vo = (IaInoutledgerVO)iterator.next();
      try {
        checker.check(vo);
        cost.calculateCost(vo);
        accountOperator.account(vo);
      }
      catch (SystemAccessException ex) {
        Throwable th = ExceptionUtils.unmarsh(ex);

        if ((th instanceof NeedHandInputException))
        {
          th.printStackTrace();
          message = th.getMessage();
          isNeedHanInput = true;
          cause = th;
        }
        else if ((th instanceof BusinessException))
        {
          th.printStackTrace();
          BusinessException exception = (BusinessException)th;
          message = exception.getMessage();
          cause = th;
        }
        else {
          th.printStackTrace();
          message = NCLangResOnserver.getInstance().getStrByID("20146010", "UPP20146010-000056");

          cause = th;
        }
        break;
      }
      catch (Exception ex)
      {
        ex.printStackTrace();
        message = NCLangResOnserver.getInstance().getStrByID("20146010", "UPP20146010-000056");

        cause = ex;
        break;
      }
      session.setCurrentIndex(index);
      index++;
    }
    AuditResultVO vo = AuditUtils.constructResult(session, message, isNeedHanInput, cause);

    return vo;
  }
}