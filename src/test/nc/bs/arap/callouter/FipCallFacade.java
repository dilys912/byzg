package nc.bs.arap.callouter;

import nc.bs.framework.exception.ComponentException;
import nc.bs.logging.Log;
import nc.bs.logging.Logger;
import nc.impl.arap.proxy.Proxy;
import nc.itf.dap.pub.IDapQueryMessage;
import nc.itf.dap.pub.IDapSendMessage;
import nc.itf.dap.pub.IPFBillCopy;
import nc.itf.dmp.pub.IDmpSendMessage;
import nc.vo.dap.out.DapMsgVO;
import nc.vo.dap.out.RetBillVo;
import nc.vo.dap.vouchtemp.RetVoucherVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ExAggregatedVO;

public class FipCallFacade
{
  private boolean DEBUG = false;

  public static void main(String[] args)
  {
  }

  public void copybill(String destBillType, String sourceBillType, String billTypeName, String whereString, String nodeCode)
    throws BusinessException
  {
    Proxy.getIPFBillCopy().copyBill(destBillType, sourceBillType, billTypeName, "", null);
  }

  public void deleteBill(String billType) throws BusinessException {
    Proxy.getIPFBillCopy().deleteBill(billType);
  }

  public RetVoucherVO sendMessage(DapMsgVO dapMsgVo, AggregatedValueObject dataVo)
    throws BusinessException
  {
    try
    {
      if (!this.DEBUG) {
        dapMsgVo.setRequestNewTranscation(false);
        return Proxy.getIDapSendMessage().sendMessage(dapMsgVo, dataVo);
      }
      Log.getInstance(getClass()).debug("sendMessage is over!");
    } catch (ComponentException ex) {
      Logger.debug("没有找到会计平台的ejb,不影响下面的操作");
    }
    return null;
  }

  public void sendMessage_dmp(DapMsgVO msgVO, ExAggregatedVO dataVo)
    throws BusinessException
  {
    try
    {
      if (!this.DEBUG)
        Proxy.getIDmpSendMessage().sendMessage(msgVO, dataVo);
    } catch (ComponentException ex) {
      Logger.debug("没有找到管理会计平台的ejb,不影响下面的操作");
    }
  }

  public boolean isEditBillTypeOrProc(String pkCorp, String pkSys, String pkProc, String pkBusiType, String procMsg)
    throws BusinessException
  {
    if (!this.DEBUG) {
      return Proxy.getIDapSendMessage().isEditBillType(pkCorp, pkSys, pkProc, pkBusiType, procMsg);
    }
    return true;
  }

  public boolean isEditBillTypeOrProc_dmp(String pkCorp, String pkSys, String pkProc, String pkBusiType, String procMsg)
    throws BusinessException
  {
    if (!this.DEBUG)
      return Proxy.getIDmpSendMessage().isEditBillTypeOrProc(pkCorp, pkSys, pkProc, pkBusiType, procMsg);
    return true;
  }

  public RetBillVo[] getPeriodNotCompleteBill(String period, String pk_corp, String pk_sys) throws BusinessException {
    if (!this.DEBUG)
      return Proxy.getIDapQueryMessage().getPeriodNotCompleteBill(period, pk_corp, pk_sys, 0);
    return null;
  }
}