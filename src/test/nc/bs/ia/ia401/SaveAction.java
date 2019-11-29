package nc.bs.ia.ia401;

import nc.bs.ia.pub.IAContext;
import nc.bs.ia.pub.LockOperator;
import nc.vo.ia.ia303.MonthAverVO;
import nc.vo.ia.ia304.PlanedPriceVO;
import nc.vo.ia.ia501.IaInoutledgerVO;
import nc.vo.ia.pub.AssertUtils;
import nc.vo.ia.pub.TimeLog;

public class SaveAction extends AbstractAction
{
  private StringBuffer messageBuffer = new StringBuffer();

  public String endproc(IAContext context, PlanedPriceVO[] planvos, MonthAverVO[] monthvos)
  {
    LockOperator lock = null;

    AssertUtils.assertValue((planvos != null) || (monthvos != null));
    AssertUtils.assertValue((planvos == null) || (planvos.length > 0));
    AssertUtils.assertValue((monthvos == null) || (monthvos.length > 0));

    boolean isPlanedInventory = false;
    try {
      TimeLog.logStart();

      TimeLog.logStart();
      lock = lock(planvos, monthvos, context);
      TimeLog.info("锁定功能点");

      TimeLog.logStart();
      isAccountOK(context);
      TimeLog.info("检查当前是否可以计算");

      String temptable = null;
      TimeLog.logStart();
      if (monthvos != null) {
        temptable = initMonthData(context, monthvos);
      }
      else if (planvos != null) {
        temptable = initJHJData(context, planvos);
        isPlanedInventory = true;
      }
      TimeLog.info("初始化临时表");

      TimeLog.logStart();
      IProcess operator = new VoucherCheckOperator(context, temptable, isPlanedInventory);
      operator.process();
      TimeLog.info("检查单据分录中是否已经生成会计凭证");

      doHuge(planvos, monthvos, context, temptable);

      TimeLog.info("保存结束");
    }
    finally {
      if (lock != null) {
        lock.unlock();
      }
    }
    return this.messageBuffer.toString();
  }

  protected void doDetail(String temptable, IAContext context, boolean isPlanedInventory)
  {
    TimeLog.logStart();
    IaInoutledgerVO[] ledgerVOs = getReturnBillCodeLedgerVOs(context, temptable, isPlanedInventory);

    TimeLog.info("查询前次计算生成的差异率结转单和异常结存调整单");

    TimeLog.logStart();
    IProcess operator = new UnCalculateOperator(context, temptable, isPlanedInventory);
    operator.process();
    TimeLog.info("取消差异率计算或者全月平均计算");

    TimeLog.logStart();
    operator = new CalculateOperator(context, temptable, isPlanedInventory, this.messageBuffer);

    operator.process();
    TimeLog.info("进行差异率计算或者全月平均计算");

    TimeLog.logStart();
    returnBillCode(ledgerVOs);
    TimeLog.info("处理回退单据号——前次计算生成的差异率结转单和异常结存调整单");
  }
}