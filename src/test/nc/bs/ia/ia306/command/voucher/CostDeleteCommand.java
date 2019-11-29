package nc.bs.ia.ia306.command.voucher;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import nc.bs.ia.boundary.fip.FIPVoucher;
import nc.bs.ia.pub.DataAccessUtils;
import nc.bs.ia.pub.IAContext;
import nc.bs.ia.pub.IATool;
import nc.bs.ia.pub.IaInoutledgerVOQuery;
import nc.bs.ia.pub.LockOperator;
import nc.bs.ia.pub.TempTableDefine;
import nc.bs.ml.NCLangResOnserver;
import nc.vo.ia.ia306.IARtvoucherVO;
import nc.vo.ia.ia501.IaInoutledgerVO;
import nc.vo.ia.pub.ExceptionUtils;
import nc.vo.ia.pub.SqlBuilder;
import nc.vo.ia.pub.TimeLog;
import nc.vo.pub.lang.UFBoolean;

public class CostDeleteCommand extends AbstractCommand
{
  private static final long serialVersionUID = 6296439194521059516L;
  private boolean lockBill = false;

  public CostDeleteCommand(boolean lockBill)
  {
    this.lockBill = lockBill;
  }

  public Serializable execute(HashMap datas) {
    IARtvoucherVO para = (IARtvoucherVO)datas.get("para");
    IAContext context = init(para);
    LockOperator lock = new LockOperator(para.getUserID());
    String[] cbill_bids = para.getBillItemIDs();
    try {
      if (this.lockBill)
      {
        lockNode(para.getCorpID(), lock);

        IATool.getInstance().lockBillItems(para.getBillItemIDs(), lock);
      }

      int totalSize = cbill_bids.length;
      if (totalSize <= 20000) {
        doNormal(lock, context, cbill_bids);
      }
      else
        doHuge(lock, context, cbill_bids);
    }
    finally
    {
      lock.unlock();
    }
    return null;
  }

  private void doHuge(LockOperator lock, IAContext context, String[] billItemIDs) {
    int start = 0;
    int length = billItemIDs.length;
    Set billHeaderSet = new HashSet();
    Set csumrtvouchidSet = new HashSet();
    while (length > 0) {
      int size = length >= 20000 ? 20000 : length;

      String[] dealBills = new String[size];
      System.arraycopy(billItemIDs, start, dealBills, 0, size);

      TimeLog.logStart();
      IaInoutledgerVO[] vos = getBillForUnAudit(dealBills);
      TimeLog.info("查询待取消实时凭证的单据，数量为 " + size);

      int num = vos.length;
      if (num > 0) {
        calculate(lock, vos, billHeaderSet, context, csumrtvouchidSet);
      }
      length -= size;
      start += size;
    }
  }

  private void doNormal(LockOperator lock, IAContext context, String[] cbill_bids)
  {
    TimeLog.logStart();
    IaInoutledgerVO[] vos = getBillForUnAudit(cbill_bids);
    TimeLog.info("查询待生成实时凭证的单据，数量为 " + cbill_bids.length);

    int size = vos.length;
    int totalSize = cbill_bids.length;
    if (size != totalSize) {
      String message = NCLangResOnserver.getInstance().getStrByID("20146010", "UPP20146010-000062");

      ExceptionUtils.wrappBusinessException(message);
    }
    Set billHeaderSet = new HashSet();
    Set csumrtvouchidSet = new HashSet();
    calculate(lock, vos, billHeaderSet, context, csumrtvouchidSet);
  }

  private void calculate(LockOperator lock, IaInoutledgerVO[] vos, Set billHeaderSet, IAContext context, Set csumrtvouchidSet)
  {
    Set set = new HashSet();
    Set set_sumvoucher = new HashSet();
    int size = vos.length;
    for (int i = 0; i < size; i++) {
      String cbillid = vos[i].getCbillid();
      if (!billHeaderSet.contains(cbillid)) {
        billHeaderSet.add(cbillid);
        set.add(cbillid);
      }
      String csumrtvouchid = vos[i].getCsumrtvouchid();
      String cbill_bid = vos[i].getCbill_bid();
      if (csumrtvouchid.equals(cbill_bid)) {
        continue;
      }
      set_sumvoucher.add(csumrtvouchid);
    }
    String[] csumrtvouchids = new String[set_sumvoucher.size()];
    csumrtvouchids = (String[])(String[])set_sumvoucher.toArray(csumrtvouchids);
    if (this.lockBill) {
      String[] billIDs = new String[set.size()];
      billIDs = (String[])(String[])set.toArray(billIDs);
      IATool.getInstance().lockBillHeaders(billIDs, lock);

      IATool.getInstance().lockSumVoucherID(csumrtvouchids, lock);
    }

    TimeLog.logStart();
    saveDataToDetailAccount(vos);
    TimeLog.info("保存实时凭证标志完毕，数目为：" + vos.length);

    TimeLog.logStart();
    saveDataToPlatform(vos, context);
    TimeLog.info("保存实时凭证到会计平台，数目为：" + vos.length);

    TimeLog.logStart();
    saveDataToDetailAccountForSumVoucher(csumrtvouchids);
    TimeLog.info("删除合并生成的实时凭证标志完毕，数目为：" + csumrtvouchids.length);
  }

  private IaInoutledgerVO[] getBillForUnAudit(String[] cbill_bids)
  {
    TempTableDefine tempDefine = new TempTableDefine();
    String tableName = tempDefine.getBillQueryTable(cbill_bids);

    StringBuffer sql = new StringBuffer();
    sql.append(" , ");
    sql.append(tableName);
    sql.append(" temptable where  ");
    sql.append(" b.cbill_bid=temptable.cbill_bid ");
    IaInoutledgerVOQuery query = new IaInoutledgerVOQuery();
    IaInoutledgerVO[] ledgerVOs = query.query(sql.toString(), " order by temptable.sequence ");

    List list = new ArrayList();
    int length = ledgerVOs.length;
    for (int i = 0; i < length; i++) {
      Integer iauditsequence = ledgerVOs[i].getIauditsequence();
      UFBoolean brtvouchflag = ledgerVOs[i].getBrtvouchflag();
      String cvoucherid = ledgerVOs[i].getCvoucherid();

      if ((cvoucherid != null) || (iauditsequence == null) || (iauditsequence.intValue() <= 0) || (brtvouchflag == null) || (!brtvouchflag.booleanValue())) {
        continue;
      }
      list.add(ledgerVOs[i]);

      ledgerVOs[i].setCdispatchid(null);
    }

    IaInoutledgerVO[] vos = new IaInoutledgerVO[list.size()];
    vos = (IaInoutledgerVO[])(IaInoutledgerVO[])list.toArray(vos);
    return vos;
  }

  private void saveDataToPlatform(IaInoutledgerVO[] ledgerVOs, IAContext context) {
    FIPVoucher tool = new FIPVoucher();
    tool.deleteRealtimeVoucher(ledgerVOs, context.isModuleGLStarted(), context.isModuleJCStarted());
  }

  private void saveDataToDetailAccount(IaInoutledgerVO[] ledgerVOs)
  {
    int size = ledgerVOs.length;
    SqlBuilder sql = new SqlBuilder();
    sql.append(" UPDATE ia_bill_b SET brtvouchflag = 'N',");
    sql.append(" csumrtvouchid = null");
    sql.append(getTempTableCondition(ledgerVOs));
    int count = DataAccessUtils.update(sql.toString());
    if (count != size) {
      String message = NCLangResOnserver.getInstance().getStrByID("20146060", "UPP20146060-000012");

      ExceptionUtils.wrappBusinessException(message);
    }
  }

  private void saveDataToDetailAccountForSumVoucher(String[] csumrtvouchids) {
    SqlBuilder sql = new SqlBuilder();
    sql.append(" UPDATE ia_bill_b SET brtvouchflag = 'N',csumrtvouchid=null");
    sql.append(" where csumrtvouchid = ? ");
    String[] parameterMetaData = new String[1];
    parameterMetaData[0] = "String";
    int length = csumrtvouchids.length;
    ArrayList data = new ArrayList();
    for (int i = 0; i < length; i++) {
      ArrayList row = new ArrayList();
      row.add(csumrtvouchids[i]);
      data.add(row);
    }

    DataAccessUtils.batchUpdate(sql.toString(), parameterMetaData, data);
  }
}