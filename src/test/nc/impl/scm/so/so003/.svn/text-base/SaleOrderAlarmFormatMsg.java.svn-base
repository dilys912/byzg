package nc.impl.scm.so.so003;

import nc.bs.ml.NCLangResOnserver;
import nc.bs.pub.pa.html.IAlertMessage;
import nc.vo.so.so003.AlarmVO;

public class SaleOrderAlarmFormatMsg
  implements IAlertMessage
{
  AlarmVO[] alarms = null;

  public String[] getBodyFields()
  {
    return new String[] { NCLangResOnserver.getInstance().getStrByID("common", "UC000-0003534"), NCLangResOnserver.getInstance().getStrByID("common", "UC000-0001589"), NCLangResOnserver.getInstance().getStrByID("common", "UC000-0001480"), NCLangResOnserver.getInstance().getStrByID("common", "UC000-0001453"), NCLangResOnserver.getInstance().getStrByID("common", "UC000-0001011") };
  }

  public Object[][] getBodyValue()
  {
    Object[][] obj = new Object[this.alarms.length][5];
    for (int i = 0; i < this.alarms.length; i++) {
      obj[i][0] = this.alarms[i].getAttributeValue("vreceiptcode");
      obj[i][1] = this.alarms[i].getAttributeValue("customer");
      obj[i][2] = this.alarms[i].getAttributeValue("invcode");
      obj[i][3] = this.alarms[i].getAttributeValue("invname");
      obj[i][4] = this.alarms[i].getAttributeValue("dconsigndate");
    }
    return obj;
  }

  public float[] getBodyWidths()
  {
    return new float[] { 0.15F, 0.15F, 0.15F, 0.15F, 0.15F };
  }

  public String[] getBottom()
  {
    return null;
  }

  public String getTitle()
  {
    return NCLangResOnserver.getInstance().getStrByID("so003", "UPPso003-000000");
  }

  public String[] getTop()
  {
    return null;
  }

  public void setAlarmVO(AlarmVO[] va)
  {
    this.alarms = va;
  }
}