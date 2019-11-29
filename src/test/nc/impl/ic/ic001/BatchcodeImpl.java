package nc.impl.ic.ic001;

import nc.bs.ic.pub.GenMethod;
import nc.itf.ic.service.IICTOQC_Batchcode;
import nc.vo.ic.ic001.BatchcodeVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.pub.session.ClientLink;

public class BatchcodeImpl
  implements IICTOQC_Batchcode
{
  public BatchcodeVO[] queryBatchcode(ConditionVO[] voConds, String pk_corp)
    throws BusinessException
  {
    BatchcodeVO[] voRet = null;
    try {
      BatchcodeDMO dmo = new BatchcodeDMO();
      voRet = dmo.queryBatchcode(voConds, pk_corp);
    } catch (Exception e) {
      GenMethod.throwBusiException(e);
    }
    return voRet;
  }

  public BatchcodeVO[] saveBatchcode(BatchcodeVO[] bvos, ClientLink cl) throws BusinessException {
    BatchcodeVO[] voRet = null;
    try {
      BatchcodeDMO dmo = new BatchcodeDMO();
      voRet = dmo.saveBatchcode(bvos, cl);
    } catch (Exception e) {
      GenMethod.throwBusiException(e);
    }
    return voRet;
  }

  public void checkandSaveBatchcode(BatchcodeVO[] vos, String sUserID, String sCorp)
    throws BusinessException
  {
    try
    {
      BatchcodeDMO dmo = new BatchcodeDMO();
      dmo.checkandsaveQCBatchcode(vos, sUserID, sCorp);
    } catch (Exception e) {
      GenMethod.throwBusiException(e);
    }
  }

  public void checkandSaveBatchcode(AggregatedValueObject voNowBill)
    throws BusinessException
  {
    try
    {
      BatchcodeDMO dmo = new BatchcodeDMO();
      dmo.checkandSaveBatchcode(voNowBill);
    } catch (Exception e) {
      GenMethod.throwBusiException(e);
    }
  }
}