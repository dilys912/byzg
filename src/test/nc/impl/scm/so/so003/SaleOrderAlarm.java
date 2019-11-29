package nc.impl.scm.so.so003;

import nc.bs.pub.pa.IBusinessPlugin;
import nc.bs.pub.pa.html.IAlertMessage;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.pa.Key;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.so.so003.AlarmVO;

public class SaleOrderAlarm
  implements IBusinessPlugin
{
  public int getImplmentsType()
  {
    return 3;
  }

  public Key[] getKeys()
  {
    return null;
  }

  public String getTypeDescription()
  {
    return null;
  }

  public String getTypeName()
  {
    return null;
  }

  public IAlertMessage implementReturnFormatMsg(Key[] keys, String corpPK, UFDate clientLoginDate)
    throws BusinessException
  {
    SaleOrderAlarmFormatMsg msg = null;
    SCMEnv.out("……………………………………………………");
    SCMEnv.out("订单报警扫描开始……");
    SCMEnv.out("……………………………………………………");
    try {
      AlarmImpl bo = new AlarmImpl();
      AlarmVO[] alarms = bo.query(corpPK);
      if (alarms != null) {
        msg = new SaleOrderAlarmFormatMsg();
        msg.setAlarmVO(alarms);
      }
    }
    catch (Exception e) {
      SCMEnv.out(e.getMessage());
    }
    SCMEnv.out("……………………………………………………");
    SCMEnv.out("订单报警结束。");
    SCMEnv.out("……………………………………………………");
    return msg;
  }

  public String implementReturnMessage(Key[] keys, String corpPK, UFDate clientLoginDate)
    throws BusinessException
  {
    return null;
  }

  public Object implementReturnObject(Key[] keys, String corpPK, UFDate clientLoginDate)
    throws BusinessException
  {
    return null;
  }

  public boolean implementWriteFile(Key[] keys, String fileName, String corpPK, UFDate clientLoginDate)
    throws BusinessException
  {
    return false;
  }
}