package nc.impl.dap;

import java.util.Hashtable;
import nc.bs.dap.out.DapBO;
import nc.bs.dap.out.DapProcessor;
import nc.bs.dap.out.DapSubFunctionBO;
import nc.bs.dap.out.MessageVO;
import nc.bs.dap.rtvouch.RtVouchException;
import nc.bs.dap.service.DapServiceBO;
import nc.itf.dap.priv.IDap;
import nc.itf.dap.priv.IDapPrvService;
import nc.itf.dap.priv.IDapSubFunction;
import nc.vo.dap.inteface.DetailVO;
import nc.vo.dap.inteface.OperationResultVO;
import nc.vo.dap.inteface.VoucherVO;
import nc.vo.dap.out.BillQueryVoucherVO;
import nc.vo.dap.out.DapMsgVO;
import nc.vo.dap.rtvouch.DapExecTypeVO;
import nc.vo.dap.rtvouch.DapFinMsgVO;
import nc.vo.dap.service.VoucherIndexVO;
import nc.vo.dap.vouchtemp.RetVoucherVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;

public class DapBsServiceImpl
  implements IDap, IDapPrvService, IDapSubFunction
{
  private DapBO m_DapBO = null;
  private DapServiceBO m_DapServiceBO = null;
  private DapSubFunctionBO m_DapSubFunctionBO = null;

  public void cancelVoucher(DapFinMsgVO[] arg0)
    throws BusinessException
  {
    getDapBO().cancelVoucher(arg0);
  }

  public Hashtable getIsQuantity(DetailVO[] arg0)
    throws BusinessException
  {
    return getDapBO().getIsQuantity(arg0);
  }

  public void procMessageQueue(DapFinMsgVO[] msgVos, UFBoolean isThrow)
    throws BusinessException
  {
    getDapBO().procMessageQueue(msgVos, isThrow);
  }

  public void procMessageQueueReflect(DapFinMsgVO[] arg0, boolean arg1)
    throws BusinessException
  {
    getDapBO().procMessageQueueReflect(arg0, arg1);
  }

  public void procReCalculate(DapFinMsgVO[] arg0)
    throws BusinessException
  {
    getDapBO().procReCalculate(arg0);
  }

  public void queryMessageQueueAndProc()
    throws BusinessException
  {
    getDapBO().queryMessageQueueAndProc();
  }

  public RetVoucherVO sendDapMessage(DapMsgVO arg0, AggregatedValueObject arg1)
    throws BusinessException
  {
    MessageVO msgVO = new MessageVO();
    msgVO.setBillDataVO(arg1);
    msgVO.setMessageVO(arg0);
    new DapProcessor().sendMessage(msgVO);
    return null;
  }

  public void procOneMsg_RequiresNew(DapFinMsgVO msgVo, AggregatedValueObject dataVo)
    throws BusinessException
  {
    getDapBO().procOneMsg(msgVo, dataVo);
  }

  public DapMsgVO addMessageQueue_RequiresNew(DapMsgVO inVo, String errMsg)
    throws BusinessException
  {
    return getDapBO().addMessageQueue(inVo, errMsg);
  }

  public VoucherVO createRtVouch_RequiresNew(DapFinMsgVO msgVo, AggregatedValueObject dataVo, boolean isExistMsg, boolean isExistReflect)
    throws BusinessException, RtVouchException
  {
    return getDapBO().createRtVouch(msgVo, dataVo, isExistMsg, isExistReflect);
  }

  public void delReflect_RequiresNew(String pk_finIndex)
    throws BusinessException
  {
    getDapBO().delReflect(pk_finIndex);
  }

  public void saveRtVouchAfterException_RequiresNew(VoucherVO rtvouchVo, String pkFinIndex, String errMsg, int execType, DapFinMsgVO msgVo)
    throws BusinessException
  {
    getDapBO().saveRtVouchAfterException(rtvouchVo, pkFinIndex, errMsg, execType, msgVo);
  }

  public OperationResultVO saveVoucher_RequiresNew(VoucherVO voucherVo, DapFinMsgVO msgVo)
    throws BusinessException, RtVouchException
  {
    return getDapBO().saveVoucher(voucherVo, msgVo);
  }

  public void updateExecType_RequiresNew(String pkMsg)
    throws BusinessException
  {
    getDapBO().updateExecType(pkMsg);
  }

  public void writeErrMsg_RequiresNew(String pkMsg, String strErr, boolean isMessageQueue)
    throws BusinessException
  {
    getDapBO().writeErrMsg(pkMsg, strErr, isMessageQueue);
  }

  public void delMessage_RequiresNew(DapMsgVO inVo)
    throws BusinessException
  {
    getDapBO().delMessage(inVo);
  }

  public RetVoucherVO offsetRed_RequiresNew(DapMsgVO msg)
    throws BusinessException
  {
    return getDapBO().offsetRed(msg);
  }

  public RetVoucherVO procAddMessage_RequiresNew(DapExecTypeVO execVo, DapMsgVO inVo, DapFinMsgVO reflectVo, AggregatedValueObject dataVo, boolean isExistMsg, boolean isExistReflect)
    throws BusinessException
  {
    return getDapBO().procAddMessage(execVo, inVo, reflectVo, dataVo, isExistMsg, isExistReflect);
  }

  public DapMsgVO[] procReCreateMsgs(DapFinMsgVO[] msgVos)
    throws BusinessException
  {
    return getDapBO().procReCreateMsgs(msgVos);
  }

  public VoucherVO[][] getRtVouchAry(String[][] aryRtVouch)
    throws BusinessException
  {
    return getDapBO().getRtVouchAry(aryRtVouch);
  }

  public DapBO getDapBO()
  {
    if (this.m_DapBO == null) {
      this.m_DapBO = new DapBO();
    }
    return this.m_DapBO;
  }

  public DapServiceBO getDapServiceBO()
  {
    if (this.m_DapServiceBO == null) {
      this.m_DapServiceBO = new DapServiceBO();
    }
    return this.m_DapServiceBO;
  }

  public VoucherIndexVO[] queryBookVouchers(BillQueryVoucherVO[] arg0)
    throws BusinessException
  {
    return getDapServiceBO().queryBookVouchers(arg0);
  }

  public DapSubFunctionBO getDapSubFunctionBO()
  {
    if (this.m_DapSubFunctionBO == null) {
      this.m_DapSubFunctionBO = new DapSubFunctionBO();
    }
    return this.m_DapSubFunctionBO;
  }

  public void procCalculate(DapFinMsgVO[] arg0)
    throws BusinessException
  {
    getDapSubFunctionBO().procCalculate(arg0);
  }

  public boolean isEditBillType_RequiresNew(String pkCorp, String pkSys, String pkProc, String pkBusiType, String procMsg)
    throws BusinessException
  {
    return getDapBO().isEditBillTypeOrProc(pkCorp, pkSys, pkProc, pkBusiType, procMsg);
  }
}