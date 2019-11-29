package nc.bs.ia.ia401;

import nc.bs.ia.ia401.uncalculate.BillDeleteOperator;
import nc.bs.ia.ia401.uncalculate.MonthAccountOperator;
import nc.bs.ia.ia401.uncalculate.PlanAccountOperator;
import nc.bs.ia.ia401.uncalculate.RTVoucherOperator;
import nc.bs.ia.ia401.uncalculate.SCAccountOperator;
import nc.bs.ia.ia401.uncalculate.SOAccountOperator;
import nc.bs.ia.pub.IAContext;
import nc.vo.ia.pub.TimeLog;

public class UnCalculateOperator
  implements IProcess
{
  private IAContext context = null;

  private String temptable = null;

  private boolean isPlanedInventory = false;

  public UnCalculateOperator(IAContext context, String temptable, boolean isPlanedInventory)
  {
    this.context = context;
    this.temptable = temptable;
    this.isPlanedInventory = isPlanedInventory;
  }

  public void process() {
    TimeLog.logStart();
    IProcess operator = new SCAccountOperator(this.context, this.temptable);
    operator.process();
    TimeLog.info("取消委外加工明细账");

    TimeLog.logStart();
    operator = new RTVoucherOperator(this.context, this.temptable, this.isPlanedInventory);
    operator.process();
    TimeLog.info("删除以前计算生成的实时凭证");

    TimeLog.logStart();
    operator = new SOAccountOperator(this.context, this.temptable);
    operator.process();
    TimeLog.info("取消回写销售成本的接口");

    if (this.isPlanedInventory) {
      TimeLog.logStart();
      operator = new PlanAccountOperator(this.context, this.temptable);
      operator.process();
      TimeLog.info("取消计算计划价存货帐表");
    }
    else {
      TimeLog.logStart();
      operator = new MonthAccountOperator(this.context, this.temptable);
      operator.process();
      TimeLog.info("取消计算全月平均存货帐表");
    }

    TimeLog.logStart();
    operator = new BillDeleteOperator(this.context, this.temptable, this.isPlanedInventory);
    operator.process();
    TimeLog.info("删除以前计算生成的单据");
  }
}