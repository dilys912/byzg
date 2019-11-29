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
      TimeLog.info("�������ܵ�");

      TimeLog.logStart();
      isAccountOK(context);
      TimeLog.info("��鵱ǰ�Ƿ���Լ���");

      String temptable = null;
      TimeLog.logStart();
      if (monthvos != null) {
        temptable = initMonthData(context, monthvos);
      }
      else if (planvos != null) {
        temptable = initJHJData(context, planvos);
        isPlanedInventory = true;
      }
      TimeLog.info("��ʼ����ʱ��");

      TimeLog.logStart();
      IProcess operator = new VoucherCheckOperator(context, temptable, isPlanedInventory);
      operator.process();
      TimeLog.info("��鵥�ݷ�¼���Ƿ��Ѿ����ɻ��ƾ֤");

      doHuge(planvos, monthvos, context, temptable);

      TimeLog.info("�������");
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

    TimeLog.info("��ѯǰ�μ������ɵĲ����ʽ�ת�����쳣��������");

    TimeLog.logStart();
    IProcess operator = new UnCalculateOperator(context, temptable, isPlanedInventory);
    operator.process();
    TimeLog.info("ȡ�������ʼ������ȫ��ƽ������");

    TimeLog.logStart();
    operator = new CalculateOperator(context, temptable, isPlanedInventory, this.messageBuffer);

    operator.process();
    TimeLog.info("���в����ʼ������ȫ��ƽ������");

    TimeLog.logStart();
    returnBillCode(ledgerVOs);
    TimeLog.info("������˵��ݺš���ǰ�μ������ɵĲ����ʽ�ת�����쳣��������");
  }
}