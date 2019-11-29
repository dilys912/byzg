package nc.itf.dap.pub;

import nc.vo.dap.out.DapMsgVO;
import nc.vo.dap.vouchtemp.RetVoucherVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;

public abstract interface IDapSendMessage
{
  public abstract RetVoucherVO sendMessage(DapMsgVO paramDapMsgVO, AggregatedValueObject paramAggregatedValueObject)
    throws BusinessException;

  /** @deprecated */
  public abstract boolean isEditBillTypeOrProc(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
    throws BusinessException;

  public abstract boolean isEditBillType(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
    throws BusinessException;

  public abstract void queryMessageQueueAndProc()
    throws BusinessException;
}