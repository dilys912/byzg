package nc.impl.scm.so.so003;

import nc.itf.scm.so.so003.IAlarmQuery;
import nc.vo.pub.BusinessException;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.so.so003.AlarmVO;

public class AlarmImpl
  implements IAlarmQuery
{
  public AlarmVO[] query(String pk_corp)
    throws BusinessException
  {
    AlarmVO[] sales = null;
    try {
      AlarmDMO dmo = new AlarmDMO();
      sales = dmo.query(pk_corp);
    }
    catch (Exception e) {
      reportException(e);
      throw new BusinessException("SaleReportBean::query(String pk_corp) Exception!");
    }
    return sales;
  }
  private void reportException(Exception e) {
    SCMEnv.out(e.getMessage());
  }
}