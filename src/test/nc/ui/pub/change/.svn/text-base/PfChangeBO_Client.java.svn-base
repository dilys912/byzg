package nc.ui.pub.change;

import nc.ui.pf.change.PfUtilUITools;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;

public class PfChangeBO_Client
{
  public static AggregatedValueObject[] pfChangeBillToBillArray(AggregatedValueObject[] vos, String sourceBillType, String destBillType)
    throws BusinessException
  {
    return PfUtilUITools.runChangeDataAry(sourceBillType, destBillType, vos);
  }

  public static AggregatedValueObject pfChangeBillToBill(AggregatedValueObject vo, String sourceBillType, String destBillType)
    throws BusinessException
  {
    return PfUtilUITools.runChangeData(sourceBillType, destBillType, vo);
  }
}