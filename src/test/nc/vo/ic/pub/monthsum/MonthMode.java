package nc.vo.ic.pub.monthsum;

public class MonthMode
{
  public int m_iMode = -1;

  public String IC_MONTH_RECORD = " ic_month_record ";

  public String IC_MONTH_HAND = " ic_month_hand ";

  public String IC_MONTH_EXEC = " ic_month_exec ";

  public void alterTableName(int iMode)
  {
    this.m_iMode = iMode;
    if (iMode == 1) {
      this.IC_MONTH_RECORD = " ic_month_recordsign ";
      this.IC_MONTH_HAND = " ic_month_handsign ";
      this.IC_MONTH_EXEC = " ic_month_execsign ";
    }
    if (iMode == 0) {
      this.IC_MONTH_RECORD = " ic_month_record ";
      this.IC_MONTH_HAND = " ic_month_hand ";
      this.IC_MONTH_EXEC = " ic_month_exec ";
    }
  }
}