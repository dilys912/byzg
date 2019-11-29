package nc.bs.dap.out;

import java.io.PrintStream;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;
import javax.naming.NamingException;
import nc.bs.dap.messageprocess.MessageBuilder;
import nc.bs.dap.outinterface.IVirtualVoucher;
import nc.bs.dap.pubfactory.CallInterface;
import nc.bs.dap.rtvouch.DapFinIndexDMO;
import nc.bs.dap.rtvouch.DapMsgDMO;
import nc.bs.dap.rtvouch.DapRtVouchBO;
import nc.bs.dap.rtvouch.RtVouchDMO;
import nc.bs.dap.rtvouch.RtVouchException;
import nc.bs.dap.rtvouch.RtvouchBDMO;
import nc.bs.dap.voucher.MergeschemeDMO;
import nc.bs.fip.scheduler.impl.AsynInvokeService;
import nc.bs.fip.scheduler.impl.AsynMessagePool;
import nc.bs.fipf.pub.TaskRedirectProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.component.RemoteProcessComponetFactory;
import nc.bs.logging.Log;
import nc.bs.logging.Logger;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.pub.SystemException;
import nc.fi.fip.pub.DapInterfaceFactory;
import nc.itf.dap.priv.IDap;
import nc.itf.dap.pub.IVoucherDelete;
import nc.itf.dap.pub.IVoucherSave;
import nc.itf.fi.pub.Accperiod;
import nc.itf.fi.pub.Currency;
import nc.itf.fi.pub.GLOrgBookAcc;
import nc.itf.fi.pub.KeyLock;
import nc.itf.fi.pub.SysInit;
import nc.vo.bd.b20.CurrtypeVO;
import nc.vo.bd.b54.GlorgbookVO;
import nc.vo.bd.period2.AccperiodmonthVO;
import nc.vo.dap.inteface.DetailVO;
import nc.vo.dap.inteface.IAccountPlat;
import nc.vo.dap.inteface.OperationResultVO;
import nc.vo.dap.inteface.VoucherConvert;
import nc.vo.dap.inteface.VoucherOperateVO;
import nc.vo.dap.inteface.VoucherVO;
import nc.vo.dap.out.AsynMessageVO;
import nc.vo.dap.out.DapMsgVO;
import nc.vo.dap.out.RetBillVo;
import nc.vo.dap.pub.DapBusinessException;
import nc.vo.dap.pub.DapExceptionProcessor;
import nc.vo.dap.pub.DapLoger;
import nc.vo.dap.queryplus.QueryLogVO;
import nc.vo.dap.rtvouch.DapExecTypeVO;
import nc.vo.dap.rtvouch.DapFactorVO;
import nc.vo.dap.rtvouch.DapFinMsgVO;
import nc.vo.dap.voucher.BillTypeMsgAggVO;
import nc.vo.dap.voucher.DapFunctionPlus;
import nc.vo.dap.voucher.MergeschemeHeaderVO;
import nc.vo.dap.voucher.MergeschemeItemVO;
import nc.vo.dap.voucher.MergeschemeVO;
import nc.vo.dap.voucher.MsgAggregatedStruct;
import nc.vo.dap.vouchtemp.RetVoucherVO;
import nc.vo.fip.pub.Translator;
import nc.vo.fipf.pub.DapBOTask;
import nc.vo.fipf.pub.PfComm;
import nc.vo.pf.pub.IExecType;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.pub.billtype.DapsystemVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

public class DapBO
  implements IVirtualVoucher
{
  private OperationResultVO GLRetVo = null;
  private DapFinIndexDMO finindexdmo = null;
  private RtVouchDMO rtvouchdmo = null;
  private RtvouchBDMO rtvouchbdmo = null;
  private DapMsgDMO dapmsgdmo = null;
  private DapDMO dapdmo = null;
  private TaskRedirectProxy m_taskRP;

  private DapDMO getDapDMO()
  {
    if (this.dapdmo == null) {
      try {
        this.dapdmo = new DapDMO();
      } catch (Exception ex) {
        Logger.error(ex);
      }
    }
    return this.dapdmo;
  }
  private DapMsgDMO getDapMsgDMO() {
    if (this.dapmsgdmo == null) {
      try {
        this.dapmsgdmo = new DapMsgDMO();
      } catch (Exception ex) {
        Logger.error(ex);
      }
    }
    return this.dapmsgdmo;
  }
  private RtVouchDMO getRtVouchDMO() {
    if (this.rtvouchdmo == null) {
      try {
        this.rtvouchdmo = new RtVouchDMO();
      } catch (Exception ex) {
        Logger.error(ex);
      }
    }
    return this.rtvouchdmo;
  }

  private RtvouchBDMO getRtvouchBDMO() {
    if (this.rtvouchbdmo == null) {
      try {
        this.rtvouchbdmo = new RtvouchBDMO();
      } catch (Exception ex) {
        Logger.error(ex);
      }
    }
    return this.rtvouchbdmo;
  }
  private DapFinIndexDMO getFinindexDMO() {
    if (this.finindexdmo == null) {
      try {
        this.finindexdmo = new DapFinIndexDMO();
      } catch (Exception ex) {
        Logger.error(ex);
      }
    }
    return this.finindexdmo;
  }

  private RetVoucherVO addMessage(DapMsgVO inVo, AggregatedValueObject dataVo)
    throws BusinessException
  {
    RetVoucherVO retVo = null;
    DapExecTypeVO execVo = null;
    IDap dap = DapInterfaceFactory.getDap();
    boolean isLock = false;
    String pk_msg = null;
    try {
      TaskRedirectProxy trp = new TaskRedirectProxy();
      if (inVo.getDestSystem() < 0)
        throw new DapBusinessException(NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000008"));
      execVo = (DapExecTypeVO)isImmedialtelyRun(inVo);
      RetVoucherVO localRetVoucherVO1;
      if (execVo == null)
      {
        if ((inVo != null) && (!inVo.isRequestNewTranscation()))
          addMessageQueue(inVo, Translator.translate("UPPfidap-000009"));
        else {
          dap.addMessageQueue_RequiresNew(inVo, Translator.translate("UPPfidap-000009"));
        }
        localRetVoucherVO1 = null;
        return localRetVoucherVO1;
      }
      if (execVo.getDisposeQuomodo() == CacheManager.ASYSTRAN)
      {
        localRetVoucherVO1 = AsynCreateRtVoucher(inVo, dataVo, execVo);
        return localRetVoucherVO1;
      }
      if ((execVo.getExecType() == 1) || (execVo.getExecType() == 2) || (execVo.getExecType() == 0))
      {
        DapsystemVO sysVo = null;
        BilltypeVO billVo = null;

        sysVo = trp.querySysType(inVo.getSys());
        Object localObject1;
        if (!sysVo.getIsaccount().booleanValue()) {
          return null;
        }
        billVo = trp.queryProcType(inVo.getProc());
        if (!billVo.getIsaccount().booleanValue()) {
          return null;
        }
        if ((inVo != null) && (!inVo.isRequestNewTranscation()))
          retVo = procAddMessage(execVo, inVo, null, dataVo, false, false);
        else
          retVo = dap.procAddMessage_RequiresNew(execVo, inVo, null, dataVo, false, false);
      }
      else if (execVo.getExecType() != 6)
      {
        if ((inVo != null) && (!inVo.isRequestNewTranscation()))
          addMessageQueue(inVo, null);
        else
          dap.addMessageQueue_RequiresNew(inVo, null);
      }
    }
    catch (Throwable ee) {
      Logger.error("生成凭证过程出现错误！", ee);
      if ((inVo != null) && (!inVo.isRequestNewTranscation()))
        throw new BusinessException(ee);
    }
    finally
    {
      try {
        if (isLock)
        {
          KeyLock.freeKey(pk_msg, "dap", null);
        }
      } catch (Exception ee) {
        DapLoger.loger.error(ee.getMessage(), ee);
      }
    }
    return retVo;
  }

  private RetVoucherVO AsynCreateRtVoucher(DapMsgVO inVo, AggregatedValueObject dataVo, DapExecTypeVO execVo)
    throws BusinessException
  {
    String errMsg = Translator.translate("UPPfidap-001010");
    inVo = addMessageQueue(inVo, errMsg);

    RemoteProcessComponetFactory factory = (RemoteProcessComponetFactory)NCLocator.getInstance().lookup("RemoteProcessComponetFactory");
    if (factory.getThreadScopePostProcess("DapAsyncInvokeService") == null) {
      factory.addThreadScopePostProcess("DapAsyncInvokeService", new AsynInvokeService());
    }
    AsynMessageVO asynMsgVO = new AsynMessageVO();
    asynMsgVO.dataVo = dataVo;
    asynMsgVO.inVo = inVo;
    asynMsgVO.execVo = execVo;
    AsynMessagePool.addAsynMessage(asynMsgVO);
    return null;
  }

  public DapMsgVO addMessageQueue(DapMsgVO inVo, String errMsg)
    throws BusinessException
  {
    try
    {
      inVo.setErrMsg(errMsg);
      return getDapMsgDMO().insert(inVo);
    } catch (Throwable t) {
      DapExceptionProcessor.process(t);
    }
    return null;
  }

  public String addSysMessageQueue(DapMsgVO inVo, String errMsg)
    throws BusinessException
  {
    String rst = null;
    try {
      inVo.setErrMsg(errMsg);
      rst = getDapMsgDMO().insert(inVo).getPrimaryKey();
    } catch (Throwable t) {
      DapExceptionProcessor.process(t);
    }
    return rst;
  }

  private void batchUpdateFlag(Properties p)
    throws Exception
  {
    try
    {
      getFinindexDMO().updateRtVouch(p, 2);
    } catch (Exception e) {
      IDap dap = DapInterfaceFactory.getDap();
      Enumeration enums = p.keys();
      while (enums.hasMoreElements())
        dap.writeErrMsg_RequiresNew((String)enums.nextElement(), e.getMessage(), true);
    }
  }

  public void cancelVoucher(DapFinMsgVO[] msgVos)
    throws BusinessException
  {
    try
    {
      Hashtable delVouchHas = new Hashtable();
      for (int i = 0; i < msgVos.length; i++) {
        String pkVoucher = msgVos[i].getVouchEntry();
        try {
          if (!delVouchHas.containsKey(pkVoucher)) {
            VoucherOperateVO operVO = new VoucherOperateVO();
            operVO.pk_user = msgVos[i].getCancelChecker();
            operVO.pk_vouchers = new String[] { pkVoucher };
            operVO.operDate = new UFDate(msgVos[i].getVoucherDate());

            CallInterface.getInstance().getVoucherDeleteBO(msgVos[i].getCorp(), msgVos[i].getDestSystem()).deleteVoucher(operVO);

            delVouchHas.put(pkVoucher, pkVoucher);
          }
        } catch (Exception ex) {
          DapLoger.loger.error(ex.getMessage(), ex);
          String message = NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000011", null, new String[] { String.valueOf(i + 1) });

          throw new DapBusinessException(message, ex);
        }
      }
    } catch (Throwable t) {
      DapExceptionProcessor.process(t);
    }
  }

  private boolean canMerge(DetailVO rtvb, DetailVO detail, boolean mergeCondition1, boolean mergeCondition2, HashMap conditionMap, int mergeType, boolean isExplation)
  {
    boolean direction1 = (rtvb.getLocaldebitamount() != null) && (rtvb.getLocaldebitamount().doubleValue() != 0.0D);

    boolean direction2 = (detail.getLocaldebitamount() != null) && (detail.getLocaldebitamount().doubleValue() != 0.0D);

    boolean isSame = direction1 == direction2;

    if ((!isSame) && (!mergeCondition2)) {
      return false;
    }
    if ((rtvb.getDirection()) && (mergeType == 2)) {
      return false;
    }

    if ((!rtvb.getDirection()) && (mergeType == 1)) {
      return false;
    }

    if ((!isExplation) && (
      ((rtvb.getExplanation() != null) && (!rtvb.getExplanation().equals(detail.getExplanation()))) || ((rtvb.getExplanation() == null) && (detail.getExplanation() != null))))
    {
      return false;
    }

    if (((rtvb.getPk_accsubj() != null) && (!rtvb.getPk_accsubj().equals(detail.getPk_accsubj()))) || ((rtvb.getAssid() != null) && (!rtvb.getAssid().equals(detail.getAssid()))) || ((rtvb.getPk_currtype() != null) && (!rtvb.getPk_currtype().equals(detail.getPk_currtype()))) || ((rtvb.getExcrate1() != null) && (!rtvb.getExcrate1().equals(detail.getExcrate1()))) || ((rtvb.getExcrate2() != null) && (!rtvb.getExcrate2().equals(detail.getExcrate2()))) || ((rtvb.getCheckstyle() != null) && (!rtvb.getCheckstyle().equals(detail.getCheckstyle()))) || ((rtvb.getCheckno() == null) && (detail.getCheckno() != null) && (detail.getCheckno().trim().length() != 0)) || ((rtvb.getCheckno() != null) && (detail.getCheckno() != null) && (!rtvb.getCheckno().trim().equals(detail.getCheckno().trim()))) || ((rtvb.getCheckno() != null) && (detail.getCheckno() == null) && (rtvb.getCheckno().trim().length() != 0)) || ((rtvb.getCheckdate() != null) && (!rtvb.getCheckdate().equals(detail.getCheckdate()))) || ((rtvb.getFree3() != null) && (!rtvb.getFree3().equals(detail.getFree3()))) || ((rtvb.getPk_innercorp() != null) && (!rtvb.getPk_innercorp().equals(detail.getPk_innercorp()))) || ((rtvb.getTradeNo() != null) && (!rtvb.getTradeNo().equals(detail.getTradeNo()))) || ((rtvb.getTradeDate() != null) && (!rtvb.getTradeDate().equals(detail.getTradeDate()))) || ((rtvb.getPk_Account() != null) && (!rtvb.getPk_Account().equals(detail.getPk_Account()))) || ((rtvb.getPk_Contract() != null) && (!rtvb.getPk_Contract().equals(detail.getPk_Contract()))) || ((rtvb.getStartRestDate() != null) && (!rtvb.getStartRestDate().equals(detail.getStartRestDate()))))
    {
      return false;
    }if (!DapFunctionPlus.canMergeFreeValue(rtvb, detail))
      return false;
    if (((rtvb.getPk_Account() != null) && (!rtvb.getPk_Account().equals(detail.getPk_Account()))) || ((rtvb.getPk_Account() == null) && (detail.getPk_Account() != null)))
    {
      return false;
    }

    for (int i = 0; i < conditionMap.size(); i++) {
      if (conditionMap.containsKey(rtvb.getPk_accsubj())) {
        return !mergeCondition1;
      }
    }
    return mergeCondition1;
  }

  public VoucherVO createRtVouch(DapFinMsgVO msgVo, AggregatedValueObject dataVo, boolean isExistMsg, boolean isExistReflect)
    throws RtVouchException
  {
    VoucherVO retVo = null;
    try {
      long starttime = System.currentTimeMillis();
      DapGetRtVouch rtVouchData = new DapGetRtVouch();
      retVo = rtVouchData.createRtVouch(msgVo, dataVo, isExistMsg, isExistReflect);

      long endtime = System.currentTimeMillis();
      Logger.info("#####XL#####生成实时凭证花费时间" + (endtime - starttime));
    }
    catch (Exception ex) {
      if ((ex instanceof RtVouchException)) {
        throw ((RtVouchException)ex);
      }
      throw new RtVouchException(ex.getMessage(), 1);
    }

    return retVo;
  }

  public void deleteVirtualVoucher(DapFinMsgVO[] msgVO)
    throws BusinessException
  {
    if (msgVO == null)
      throw new DapBusinessException(NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000013"));
    try {
      for (int i = 0; i < msgVO.length; i++) {
        if (((msgVO[i].getSys() != null) && (msgVO[i].getProcMsg() != null) && (msgVO[i].getProc() != null) && (msgVO[i].getCorp() != null)) || (msgVO[i].getPkFinmsg() != null))
        {
          continue;
        }
        throw new DapBusinessException(NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000014"));
      }

      for (int i = 0; i < msgVO.length; i++)
        getFinindexDMO().delete(msgVO[i]);
    }
    catch (Exception ex) {
      throw new DapBusinessException(ex.getMessage());
    }
  }

  public void delMessage(DapMsgVO inVo)
    throws BusinessException
  {
    String errString = null;
    String[] pks = null;
    boolean locka = false;
    try
    {
      if (!isEditBillTypeOrProc(inVo.getCorp(), inVo.getSys(), inVo.getProc(), inVo.getBusiType(), inVo.getProcMsg()))
      {
        errString = NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000015");
        throw new DapBusinessException(errString, new Exception(errString));
      }

      DapMsgVO[] tmpVos = getDapDMO().queryMessage(inVo);
      if (tmpVos != null) {
        Vector v = new Vector();
        for (int i = 0; i < tmpVos.length; i++) {
          if (tmpVos[i].getPrimaryKey() != null) {
            v.add(tmpVos[i].getPrimaryKey());
          }
        }
        if (v.size() > 0) {
          pks = new String[v.size()];
          v.toArray(pks);

          boolean islock = KeyLock.lockKeyArry(pks, "dap", null);
          if (!islock) {
            throw new DapBusinessException(NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000016"));
          }
          locka = true;
        }

        getDapDMO().deleteMessage(tmpVos);
      }

      ProcDelMessage(inVo);
    }
    catch (BusinessException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new DapBusinessException(ex.getMessage() == null ? "" : ex.getMessage(), ex);
    }
    finally
    {
      if (locka)
        try
        {
          KeyLock.freeKeyArray(pks, "dap", null);
        }
        catch (Exception e) {
          DapLoger.loger.error(e.getMessage(), e);
        }
    }
  }

  public void delReflect(String pk_finIndex)
    throws BusinessException
  {
    DapFinMsgVO vo = new DapFinMsgVO();
    vo.setPkFinmsg(pk_finIndex);
    try
    {
      getFinindexDMO().delete(vo);
    } catch (Exception ex) {
      Logger.error(ex);
      if ((ex instanceof DapBusinessException)) {
        throw ((DapBusinessException)ex);
      }
      throw new DapBusinessException(ex.getMessage(), ex);
    }
  }

  protected void delRtVouch(DapFinMsgVO voMsg)
    throws BusinessException
  {
    if (voMsg.getPkRtVouch() == null) {
      return;
    }

    VoucherVO voVouch = new VoucherVO();

    voVouch.setPk_voucher(voMsg.getPkRtVouch());
    try
    {
      getRtvouchBDMO().deleteByFK(voMsg.getPkRtVouch());

      getRtVouchDMO().delete(voVouch);
    } catch (Exception ex) {
      Logger.error(ex);
      if ((ex instanceof DapBusinessException)) {
        throw ((DapBusinessException)ex);
      }
      throw new DapBusinessException(ex.getMessage(), ex);
    }
  }

  private void delRtVoucherAndReflect(DapFinMsgVO voMsg)
    throws BusinessException
  {
    String errString = null;
    try
    {
      delRtVouch(voMsg);

      int retFlag = 1;

      int delint = getFinindexDMO().deleteByVO(voMsg);
      if (delint != retFlag) {
        errString = NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000017");
        throw new DapBusinessException(errString, new Exception(errString));
      }
    } catch (Exception ex) {
      if ((ex instanceof DapBusinessException)) {
        throw ((DapBusinessException)ex);
      }
      throw new DapBusinessException(ex.getMessage(), ex);
    }
  }

  private void delVoucherAndReflect(DapFinMsgVO voMsg)
    throws BusinessException
  {
    try
    {
      if (voMsg.getFlag().intValue() == 4)
      {
        boolean isAfterRedOffset = getDapDMO().isFreeBillAfterRedOffset(voMsg.getCorp(), voMsg.getSys(), voMsg.getProc(), voMsg.getBusiType(), voMsg.getProcMsg());

        if (isAfterRedOffset)
          return;
        VoucherOperateVO operVO = new VoucherOperateVO();
        operVO.pk_user = voMsg.getOperator();
        operVO.operDate = (voMsg.getVoucherDate() == null ? null : new UFDate(voMsg.getVoucherDate()));

        operVO.pk_vouchers = new String[] { voMsg.getVouchEntry() };
        operVO.operatedirection = new Boolean(true);
        CallInterface.getInstance().getVoucherDeleteBO(voMsg.getCorp(), voMsg.getDestSystem()).deleteVoucher(operVO);
      }

      getFinindexDMO().delete(voMsg);
    } catch (Exception ex) {
      Logger.error(ex);
      throw new DapBusinessException(ex.getMessage(), ex);
    }
  }

  private int getCurTypeDigit(String pk_glorg)
  {
    int localCurrTypeDigit = 2;
    try
    {
      String pk_corp = GLOrgBookAcc.getPk_corpByPk_GlOrg(pk_glorg);
      localCurrTypeDigit = Currency.getCurrInfo(Currency.getLocalCurrPK(pk_corp)).getCurrdigit().intValue();
    } catch (Exception ex) {
      System.out.println("取得本币小数位数出错！" + ex.getMessage());
    }
    return localCurrTypeDigit;
  }

  protected AggregatedValueObject getDataByInteface(DapFinMsgVO msgVo)
    throws DapBusinessException
  {
    AggregatedValueObject tmpVo = null;
    try {
      tmpVo = PfComm.queryDataByInterface(msgVo.getProc(), msgVo.getProcMsg());
    }
    catch (Exception ex) {
      if ((ex instanceof DapBusinessException)) {
        throw ((DapBusinessException)ex);
      }
      String message = NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000018");
      throw new DapBusinessException(message + ex.getMessage(), ex);
    }

    return tmpVo;
  }

  private BillTypeMsgAggVO getDataByIntefaceInBulk(DapFinMsgVO[] msgVo)
    throws DapBusinessException
  {
    if ((msgVo == null) || (msgVo.length == 0))
      return null;
    String message;
    try {
      Hashtable hash = new Hashtable();

      for (int i = 0; i < msgVo.length; i++)
      {
        String proc = msgVo[i].getProc();
        String procMsg = msgVo[i].getProcMsg();
        Object o = hash.get(msgVo[i].getProc());

        if (o == null) {
          ArrayList al = new ArrayList();
          al.add(procMsg);
          hash.put(proc, al);
        } else {
          ((ArrayList)o).add(procMsg);
        }

      }

      Object[] procs = hash.keySet().toArray();

      BillTypeMsgAggVO reVO = new BillTypeMsgAggVO();

      for (int i = 0; i < procs.length; i++) {
        Object[] procMsgsTemp = ((ArrayList)hash.get(procs[i].toString())).toArray();

        if (procMsgsTemp == null) {
          continue;
        }
        String[] procMsgs = new String[procMsgsTemp.length];
        for (int j = 0; j < procMsgsTemp.length; j++) {
          procMsgs[j] = procMsgsTemp[j].toString();
        }
        MsgAggregatedStruct[] msgVOs = PfComm.queryDataByInterface(procs[i].toString(), procMsgs);

        if (msgVOs == null) {
          System.out.println("单据类型:" + procs[i].toString() + "没有查询出任何的记录");
        }
        else
        {
          reVO.addBillTypeAndBillVO(procs[i].toString(), msgVOs);
        }
      }
      return reVO;
    }
    catch (Exception ex) {
      if ((ex instanceof DapBusinessException)) {
        throw ((DapBusinessException)ex);
      }
      message = NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000019"); 
      throw new DapBusinessException(message + ex.getMessage(), ex);
    }
  }

  private BillTypeMsgAggVO getDataByIntefaceInBulk_Con(DapFinMsgVO[] msgVo, boolean isThrow)
    throws DapBusinessException
  {
    if ((msgVo == null) || (msgVo.length == 0))
      return null;
    String message;
    try {
      Hashtable hash = new Hashtable();

      for (int i = 0; i < msgVo.length; i++)
      {
        String proc = msgVo[i].getProc();
        String procMsg = msgVo[i].getProcMsg();
        Object o = hash.get(msgVo[i].getProc());

        if (o == null) {
          ArrayList al = new ArrayList();
          al.add(procMsg);
          hash.put(proc, al);
        } else {
          ((ArrayList)o).add(procMsg);
        }

      }

      Object[] procs = hash.keySet().toArray();

      BillTypeMsgAggVO reVO = new BillTypeMsgAggVO();

      for (int i = 0; i < procs.length; i++) {
        try {
          Object[] procMsgsTemp = ((ArrayList)hash.get(procs[i].toString())).toArray();

          if (procMsgsTemp == null) {
            continue;
          }
          String[] procMsgs = new String[procMsgsTemp.length];
          for (int j = 0; j < procMsgsTemp.length; j++) {
            procMsgs[j] = procMsgsTemp[j].toString();
          }
          MsgAggregatedStruct[] msgVOs = PfComm.queryDataByInterface(procs[i].toString(), procMsgs);

          if (isThrow) {
            if ((msgVOs == null) || (msgVOs.length != procMsgs.length)) {
              throw new DapBusinessException(NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000019"));
            }
          }
          else if (msgVOs == null) {
            System.out.println("单据类型:" + procs[i].toString() + "没有查询出任何的记录");

            continue;
          }

          reVO.addBillTypeAndBillVO(procs[i].toString(), msgVOs);
        } catch (Exception exe) {
          Logger.error(exe);
          if (isThrow) {
            throw new DapBusinessException(exe.getMessage());
          }
        }

      }

      return reVO;
    }
    catch (Exception ex)
    {
      if ((ex instanceof DapBusinessException)) {
        throw ((DapBusinessException)ex);
      }
      message = NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000019");
      throw new DapBusinessException(message + ex.getMessage(), ex);
    }
  }

  public MergeschemeVO getDefaMergeScheme(String pk_Corp)
    throws DapBusinessException
  {
    MergeschemeVO retVo = null;
    try {
      retVo = getDapDMO().queryDefaScheme(pk_Corp);
      MergeschemeDMO dmo2 = new MergeschemeDMO();
      retVo.setChildrenVO(dmo2.findItemsForHeader(((MergeschemeHeaderVO)retVo.getParentVO()).getPk_mergescheme()));
    }
    catch (Exception ex)
    {
      Logger.error(ex);
      throw new DapBusinessException(ex.getMessage(), ex);
    }
    return retVo;
  }

  public Hashtable getIsQuantity(DetailVO[] vos)
    throws DapBusinessException
  {
    Hashtable retHas = null;
    try {
      retHas = getDapDMO().getSubjectIsQuantity(vos);
    } catch (Exception ex) {
      Logger.error(ex);
      throw new DapBusinessException(ex.getMessage(), ex);
    }
    return retHas;
  }

  public RetBillVo[] getPeriodNoCompleteBill(String period, String pkCorp)
    throws BusinessException
  {
    try
    {
      String year = period.substring(0, 4);
      String month = period.substring(4);

      AccperiodmonthVO tmpVo = Accperiod.findByYearAndMonth(year, month);
      return getDapDMO().getPeriodNoCompleteBill(tmpVo.getBegindate().toString(), tmpVo.getEnddate().toString(), pkCorp);
    }
    catch (Exception ex) {
      Logger.error(ex);
      throw new DapBusinessException(NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000021") + ex.getMessage(), ex);
    }
  }

  public VoucherVO[][] getRtVouchAry(String[][] AryRtVouch)
    throws DapBusinessException
  {
    try
    {
      return getDapDMO().isExitRtVouch(AryRtVouch);
    } catch (Exception ex) {
      Logger.error(ex);
      throw new DapBusinessException(NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000022") + ex.getMessage(), ex);
    }
  }

  public VoucherVO[] getVoucherId(String billType, String billId)
    throws BusinessException
  {
    VoucherVO[] pkVoucher = null;
    try {
      pkVoucher = getFinindexDMO().queryPkVoucher(billType, billId);
    } catch (Exception ex) {
      Logger.error(ex);
    }
    return pkVoucher;
  }

  public boolean isEditBillTypeOrProc(String pkCorp, String pkSys, String pkProc, String pkBusiType, String procMsg)
    throws BusinessException
  {
    boolean retBool = false;
    try {
      retBool = getDapDMO().isEditBill(pkCorp, pkSys, pkProc, pkBusiType, procMsg);
    }
    catch (Exception ex) {
      Logger.error(ex);
      if ((ex instanceof SQLException)) {
        throw new DapBusinessException(ex.getMessage(), ex);
      }
      throw new DapBusinessException(NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000024") + ex.getMessage(), ex);
    }

    return retBool;
  }

  protected IExecType isImmedialtelyRun(DapMsgVO inVo)
    throws BusinessException
  {
    try
    {
      TaskRedirectProxy trp = new TaskRedirectProxy();
      return trp.queryExcutType(inVo);
    } catch (Exception e) {
      Logger.error(e);
      throw new DapBusinessException(e.getMessage(), e);
    }
  }

  protected boolean isReCalculate(DapFinMsgVO msgVo)
    throws BusinessException
  {
    boolean retBool = false;
    int retInt = 0;
    try {
      retInt = getFinindexDMO().reflectState(msgVo);
      if ((retInt == 0) || (retInt == 2))
      {
        retBool = true;
      }
      else retBool = false; 
    }
    catch (Exception ex)
    {
      Logger.error(ex);
      throw new DapBusinessException(NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000025") + ex.getMessage(), ex);
    }
    return retBool;
  }

  private boolean isReCreate(DapFinMsgVO msgVo)
    throws BusinessException
  {
    boolean retBool = false;
    int retInt = 0;
    try {
      retInt = getFinindexDMO().reflectState(msgVo);
      if (retInt == 0)
        retBool = true;
      else
        retBool = false;
    }
    catch (Exception ex) {
      Logger.error(ex);
      throw new DapBusinessException(NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000026") + ex.getMessage(), ex);
    }
    return retBool;
  }

  private VoucherVO mergeDetailAndRetGLVo(VoucherVO rtHeadVo, String m_pkFinIndex)
    throws RtVouchException
  {
    UFDouble totalDebitMny = new UFDouble(0);

    UFDouble totalCreditMny = new UFDouble(0);

    VoucherVO retVo = new VoucherVO();

    String strRemark = null;

    String strControlFlag = rtHeadVo.getModifyflag().trim();

    DetailVO[] rtVouchbVos = rtHeadVo.getDetails();
    DetailVO[] details = null;
    MergeschemeVO mergeScheme = null;
    if (rtVouchbVos == null) {
      return null;
    }
    try
    {
      String pkCorp = rtVouchbVos[0].getPk_corp();

      mergeScheme = getDefaMergeScheme(pkCorp);
      if (mergeScheme == null) {
        String message = NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000027", null, new String[] { pkCorp });
        throw new Exception(message);
      }

      boolean mergeCondition1 = ((MergeschemeHeaderVO)mergeScheme.getParentVO()).getSubjassflag().booleanValue();

      boolean mergeCondition2 = ((MergeschemeHeaderVO)mergeScheme.getParentVO()).getDireflag().booleanValue();

      int mergeType = ((MergeschemeHeaderVO)mergeScheme.getParentVO()).getMergeType();

      boolean isExplation = ((MergeschemeHeaderVO)mergeScheme.getParentVO()).getExplationflag().booleanValue();

      MergeschemeItemVO[] advancedCondition = (MergeschemeItemVO[])(MergeschemeItemVO[])mergeScheme.getChildrenVO();
      HashMap conditionMap = new HashMap(advancedCondition.length);
      for (int i = 0; i < advancedCondition.length; i++) {
        conditionMap.put(advancedCondition[i].getMergeobject(), advancedCondition[i].getObjectvalue());
      }

      Hashtable subHas = getIsQuantity(rtVouchbVos);

      ArrayList result = new ArrayList();
      for (int i = 0; i < rtVouchbVos.length; i++) {
        DetailVO rtvb = rtVouchbVos[i];
        boolean hasMerged = false;

        for (int j = 0; j < result.size(); j++) {
          DetailVO detail = (DetailVO)result.get(j);
          if (canMerge(rtvb, detail, mergeCondition1, mergeCondition2, conditionMap, mergeType, isExplation)) {
            hasMerged = true;

            detail.setDetailindex(new Integer(detail.getDetailindex().intValue() + 1));

            detail.setDebitquantity(detail.getDebitquantity().add(rtvb.getDebitquantity() == null ? new UFDouble(0) : rtvb.getDebitquantity()));

            detail.setDebitamount(detail.getDebitamount().add(rtvb.getDebitamount() == null ? new UFDouble(0) : rtvb.getDebitamount()));

            detail.setFracdebitamount(detail.getFracdebitamount().add(rtvb.getFracdebitamount() == null ? new UFDouble(0) : rtvb.getFracdebitamount()));

            detail.setLocaldebitamount(detail.getLocaldebitamount().add(rtvb.getLocaldebitamount() == null ? new UFDouble(0) : rtvb.getLocaldebitamount()));

            detail.setCreditquantity(detail.getCreditquantity().add(rtvb.getCreditquantity() == null ? new UFDouble(0) : rtvb.getCreditquantity()));

            detail.setCreditamount(detail.getCreditamount().add(rtvb.getCreditamount() == null ? new UFDouble(0) : rtvb.getCreditamount()));

            detail.setFraccreditamount(detail.getFraccreditamount().add(rtvb.getFraccreditamount() == null ? new UFDouble(0) : rtvb.getFraccreditamount()));

            detail.setLocalcreditamount(detail.getLocalcreditamount().add(rtvb.getLocalcreditamount() == null ? new UFDouble(0) : rtvb.getLocalcreditamount()));

            detail = DetailVO.mergeUserDataVO(detail, rtvb);
            break;
          }
        }

        if (hasMerged)
          continue;
        DetailVO detail = new DetailVO();

        detail.setPk_corp(rtvb.getPk_corp());

        detail.setDetailindex(new Integer(0));

        detail.setPk_accsubj(rtvb.getPk_accsubj());

        detail.setAssid(rtvb.getAssid());

        detail.setExplanation(rtvb.getExplanation());

        detail.setPk_currtype(rtvb.getPk_currtype());

        detail.setOppositesubj(null);

        detail.setPrice(new UFDouble(0));

        detail.setExcrate1(rtvb.getExcrate1());

        detail.setExcrate2(rtvb.getExcrate2());

        detail.setDebitquantity(rtvb.getDebitquantity() == null ? new UFDouble(0) : rtvb.getDebitquantity());

        detail.setDebitamount(rtvb.getDebitamount() == null ? new UFDouble(0) : rtvb.getDebitamount());

        detail.setFracdebitamount(rtvb.getFracdebitamount() == null ? new UFDouble(0) : rtvb.getFracdebitamount());

        detail.setLocaldebitamount(rtvb.getLocaldebitamount() == null ? new UFDouble(0) : rtvb.getLocaldebitamount());

        detail.setCreditquantity(rtvb.getCreditquantity() == null ? new UFDouble(0) : rtvb.getCreditquantity());

        detail.setCreditamount(rtvb.getCreditamount() == null ? new UFDouble(0) : rtvb.getCreditamount());

        detail.setFraccreditamount(rtvb.getFraccreditamount() == null ? new UFDouble(0) : rtvb.getFraccreditamount());

        detail.setLocalcreditamount(rtvb.getLocalcreditamount() == null ? new UFDouble(0) : rtvb.getLocalcreditamount());

        detail.setModifyflag(rtvb.getModifyflag());

        detail.setRecieptclass(null);

        detail.setCheckstyle(rtvb.getCheckstyle());

        detail.setCheckno(rtvb.getCheckno());

        detail.setCheckdate(rtvb.getCheckdate() == null ? null : rtvb.getCheckdate());

        detail.setFree3(rtvb.getFree3() == null ? null : rtvb.getFree3());

        detail.setPk_Account(rtvb.getPk_Account());

        detail.setCoorNo(rtvb.getCoorNo());

        detail.setPk_Contract(rtvb.getPk_Contract());

        detail.setAssiNo(rtvb.getAssiNo());

        detail.setStartRestDate(rtvb.getStartRestDate());

        detail.setM_creditsign(rtvb.getM_creditsign());
        detail.setBankNo(rtvb.getBankNo());
        detail.setPk_glorg(rtvb.getPk_glorg());
        detail.setPk_glbook(rtvb.getPk_glbook());
        detail.setOtheruserdata(rtvb.getOtheruserdata());

        detail = DapProvider.getInstance().createDetailExt(detail, rtvb);

        result.add(detail);
      }

      if (mergeCondition2) {
        for (int i = 0; i < result.size(); i++) {
          DetailVO mergedDetail = (DetailVO)result.get(i);
          UFDouble debit = mergedDetail.getLocaldebitamount().add(mergedDetail.getLocaldebitamount());

          UFDouble credit = mergedDetail.getLocalcreditamount().add(mergedDetail.getLocalcreditamount());

          if ((debit == null) || (credit == null))
            continue;
          if (debit.compareTo(credit) > 0) {
            if (debit.equals(new UFDouble(0)))
              continue;
            mergedDetail.setDebitquantity(mergedDetail.getDebitquantity().sub(mergedDetail.getCreditquantity()));

            mergedDetail.setDebitamount(mergedDetail.getDebitamount().sub(mergedDetail.getCreditamount()));

            mergedDetail.setFracdebitamount(mergedDetail.getFracdebitamount().sub(mergedDetail.getFraccreditamount()));

            mergedDetail.setLocaldebitamount(mergedDetail.getLocaldebitamount().sub(mergedDetail.getLocalcreditamount()));

            mergedDetail.setCreditquantity(new UFDouble(0));

            mergedDetail.setCreditamount(new UFDouble(0));

            mergedDetail.setFraccreditamount(new UFDouble(0));

            mergedDetail.setLocalcreditamount(new UFDouble(0));
          }
          else if (debit.compareTo(credit) < 0) {
            if (credit.equals(new UFDouble(0)))
              continue;
            mergedDetail.setCreditquantity(mergedDetail.getCreditquantity().sub(mergedDetail.getDebitquantity()));

            mergedDetail.setCreditamount(mergedDetail.getCreditamount().sub(mergedDetail.getDebitamount()));

            mergedDetail.setFraccreditamount(mergedDetail.getFraccreditamount().sub(mergedDetail.getFracdebitamount()));

            mergedDetail.setLocalcreditamount(mergedDetail.getLocalcreditamount().sub(mergedDetail.getLocaldebitamount()));

            mergedDetail.setDebitquantity(new UFDouble(0));

            mergedDetail.setDebitamount(new UFDouble(0));

            mergedDetail.setFracdebitamount(new UFDouble(0));

            mergedDetail.setLocaldebitamount(new UFDouble(0));
          }
          else
          {
            result.remove(i);
            i--;
          }
        }

      }

      int priceNum = 0;
      try {
        priceNum = SysInit.getParaInt(pkCorp, "BD504").intValue();
      } catch (Exception ex) {
        Logger.error(ex);
      }
      int digit = getCurTypeDigit(rtHeadVo.getPk_glorg());
      DetailVO[] resultVO = new DetailVO[result.size()];
      details = (DetailVO[])(DetailVO[])result.toArray(resultVO);

      if ((details != null) && (details.length != 0)) {
        if (rtHeadVo.getDestSystem() == 1) {
          digit = getTaskRP().getCurrTypeDigit(details[0].getPk_currtype());
        }
        for (int i = 0; i < details.length; i++)
        {
          if (i == 0) {
            strRemark = details[i].getExplanation();
          }
          details[i].setDetailindex(new Integer(i + 1));

          details[i].setModifyflag(details[i].getModifyflag());

          UFBoolean isQuantity = new UFBoolean((String)subHas.get(details[i].getPk_accsubj()));

          if (isQuantity.booleanValue())
          {
            details[i].setDebitquantity(details[i].getDebitquantity());

            details[i].setCreditquantity(details[i].getCreditquantity());

            if (details[i].getDebitquantity().doubleValue() != 0.0D)
            {
              details[i].setPrice(details[i].getDebitamount() == null ? new UFDouble(0) : details[i].getDebitamount().div(details[i].getDebitquantity(), -priceNum));
            }
            else if (details[i].getCreditquantity().doubleValue() != 0.0D) {
              details[i].setPrice(details[i].getCreditamount() == null ? new UFDouble(0) : details[i].getCreditamount().div(details[i].getCreditquantity(), -priceNum));
            }
            else
            {
              details[i].setPrice(new UFDouble(0));
            }
          }
          else
          {
            details[i].setDebitquantity(new UFDouble(0));

            details[i].setCreditquantity(new UFDouble(0));

            details[i].setPrice(new UFDouble(0));
          }
          if (rtHeadVo.getDestSystem() == 0)
          {
            totalCreditMny = totalCreditMny.add(details[i].getLocalcreditamount().setScale(digit, 4));

            totalDebitMny = totalDebitMny.add(details[i].getLocaldebitamount().setScale(digit, 4));
          }
          else
          {
            if (rtHeadVo.getDestSystem() != 1)
              continue;
            totalCreditMny = totalCreditMny.add(details[i].getCreditamount().setScale(digit, 4));

            totalDebitMny = totalDebitMny.add(details[i].getDebitamount().setScale(digit, 4));
          }

        }

        if (totalCreditMny.doubleValue() > totalDebitMny.doubleValue())
          totalDebitMny = totalCreditMny;
        else {
          totalCreditMny = totalDebitMny;
        }
      }

      retVo.setDetails(details);

      retVo.setAttachment(rtHeadVo.getAttachment());

      retVo.setDiscardflag(new UFBoolean("N"));

      retVo.setDetailmodflag(new UFBoolean(strControlFlag.substring(0, 1)));
      retVo.setModifyflag(strControlFlag);

      retVo.setNo(new Integer(0));

      retVo.setPeriod(rtHeadVo.getPeriod());

      retVo.setYear(rtHeadVo.getYear());

      retVo.setPk_corp(rtHeadVo.getPk_corp());

      retVo.setPk_vouchertype(rtHeadVo.getPk_vouchertype());

      retVo.setVoucherkind(new Integer(0));

      retVo.setPk_prepared(rtHeadVo.getPk_prepared());

      retVo.setPrepareddate(rtHeadVo.getPrepareddate());

      retVo.setPk_system(rtHeadVo.getPk_system());

      retVo.setDestSystem(rtHeadVo.getDestSystem());

      retVo.setPk_billtype(rtHeadVo.getPk_billtype());

      retVo.setAssino(rtHeadVo.getAssino());

      retVo.setExplanation(strRemark);

      retVo.setUserData(new String[] { m_pkFinIndex, "FININDEX" });

      retVo.setSignflag(new UFBoolean(false));
      retVo.setPk_glorg(rtHeadVo.getPk_glorg());
      retVo.setPk_glbook(rtHeadVo.getPk_glbook());
      retVo.setFree1(rtHeadVo.getFree1());
      retVo.setFree2(rtHeadVo.getFree2());
      retVo.setFree3(rtHeadVo.getFree3());
      retVo.setFree4(rtHeadVo.getFree4());
      retVo.setFree5(rtHeadVo.getFree5());
      retVo.setFree6(rtHeadVo.getFree6());
      retVo.setFree7(rtHeadVo.getFree7());
      retVo.setFree8(rtHeadVo.getFree8());
      retVo.setFree9(rtHeadVo.getFree9());
      retVo.setFree10(rtHeadVo.getFree10());
      retVo.setFreevalue1(rtHeadVo.getFreevalue1());
      retVo.setFreevalue2(rtHeadVo.getFreevalue2());
      retVo.setFreevalue3(rtHeadVo.getFreevalue3());
      retVo.setFreevalue4(rtHeadVo.getFreevalue4());
      retVo.setFreevalue5(rtHeadVo.getFreevalue5());
      if (retVo.getDestSystem() == 0)
        retVo = dealGLBalance(retVo, digit);
      else
        retVo = dealFTSBalance(retVo, digit);
    }
    catch (Exception ex) {
      Logger.error(ex);
      if (ex.getMessage() != null) {
        throw new RtVouchException(NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000028") + ex.getMessage(), 3);
      }

      throw new RtVouchException(NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000023"), 3);
    }

    return retVo;
  }

  public RetVoucherVO offsetRed(DapMsgVO msg)
    throws BusinessException
  {
    String proc = msg.getProc();
    String procMsg = msg.getProcMsg();

    if (proc == null)
      throw new DapBusinessException(NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000029"));
    if (procMsg == null) {
      throw new DapBusinessException(NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000030"));
    }
    DapFinMsgVO finIndexVO = getFinindexDMO().querybyTypeandDjPK(proc, procMsg, msg.getPkAccOrg(), msg.getPkAccount());

    if (finIndexVO == null) {
      String message = NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000031", null, new String[] { procMsg });
      throw new DapBusinessException(message);
    }

    if (finIndexVO.getFlag().intValue() < 4) {
      String message = NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000032", null, new String[] { procMsg });
      throw new DapBusinessException(message);
    }

    String pk_voucher = finIndexVO.getVouchEntry();

    if (pk_voucher == null) {
      throw new DapBusinessException(NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000033"));
    }
    VoucherVO voucher = null; VoucherVO redVoucher = null;
    voucher = CallInterface.getInstance().getVoucherQueryBO(msg.getCorp(), msg.getDestSystem()).queryByPks(new String[] { pk_voucher })[0];

    if (voucher.getPk_manager() == null) {
      String message = NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000034", null, new String[] { procMsg });
      throw new DapBusinessException(message);
    }

    if (!finIndexVO.getCorp().equals(msg.getCorp())) {
      throw new DapBusinessException(NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000035"));
    }

    redVoucher = DapPlus.voucherOffsetRed(voucher, msg.getOperator(), msg.getOperatorName(), msg.getBusiDate());

    OperationResultVO[] reSaveVO = CallInterface.getInstance().getVoucherSaveBO(msg.getCorp(), msg.getDestSystem()).saveVoucher(redVoucher);

    OperationResultVO voucherReVO = reSaveVO[0];

    String reVoucherPk = voucherReVO.getPrimaryKey();

    if (reVoucherPk == null) {
      throw new DapBusinessException(NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000036"));
    }
    RetVoucherVO reVo = new RetVoucherVO();
    reVo.setPrimaryKey(reVoucherPk);

    finIndexVO.setOperator(msg.getOperator());
    finIndexVO.setOperatorName(msg.getOperatorName());
    finIndexVO.setBusiDate(msg.getBusiDate());
    finIndexVO.setBillCode(msg.getBillCode());
    finIndexVO.setComment(msg.getComment());
    finIndexVO.setBusiType(msg.getBusiType());
    finIndexVO.setBusiName(msg.getBusiName());
    finIndexVO.setMergeBatch(null);
    finIndexVO.setMergeBatchCode(null);
    finIndexVO.setChecker(null);
    finIndexVO.setCheckerName(null);

    finIndexVO.setErrMsg(null);
    finIndexVO.setFlag(new Integer(4));
    finIndexVO.setPkFinmsg(null);
    finIndexVO.setPkRtVouch(null);
    finIndexVO.setVouchEntry(reVoucherPk);
    finIndexVO.setDestSystem(msg.getDestSystem());
    finIndexVO.setPkAccOrg(msg.getPkAccOrg());
    finIndexVO.setPkAccount(msg.getPkAccount());

    return reVo;
  }

  public RetVoucherVO procAddMessage(DapExecTypeVO execVo, DapMsgVO inVo, DapFinMsgVO reflectVo, AggregatedValueObject dataVo, boolean isExistMsg, boolean isExistReflect)
    throws BusinessException
  {
    String m_pkfinindex = null;

    OperationResultVO GlRetVo = null;

    VoucherVO rtvouchVo = null;

    VoucherVO voucherVo = null;
    String pkRtVouch = null;

    boolean notNewTranscation = (inVo != null) && (!inVo.isRequestNewTranscation()) && (execVo != null) && (execVo.getDisposeQuomodo() == CacheManager.SYSTRAN);

    boolean isGetNumStyle = false;
    boolean isExistMsg_Log = isExistMsg;

    DapFinMsgVO msgVo = null;
    if (inVo != null) {
      isGetNumStyle = false;
      msgVo = DapFinMsgVO.changeFinIndexVO(inVo, new Integer(execVo.getExecType()));
    } else {
      isGetNumStyle = true;
      msgVo = reflectVo;
    }
    m_pkfinindex = msgVo.getPkFinmsg();

    IDap dap = null;
    try
    {
      TaskRedirectProxy trp = new TaskRedirectProxy();

      boolean im = (execVo.getExecType() == 0) || ((execVo.getExecType() == 3) && (isGetNumStyle));

      boolean imp = (im) && (!isExistReflect);

      if (imp) {
        trp.putNewMessage();
      }
      dap = DapInterfaceFactory.getDap();
      if (notNewTranscation)
        rtvouchVo = createRtVouch(msgVo, dataVo, isExistMsg, isExistReflect);
      else {
        rtvouchVo = dap.createRtVouch_RequiresNew(msgVo, dataVo, isExistMsg, isExistReflect);
      }

      if (rtvouchVo == null) {
        return null;
      }
      if ((reflectVo != null) && (!reflectVo.IsLogCreate()) && (!isExistReflect) && (im))
      {
        getDapMsgDMO().delete(DapFinMsgVO.changeMsgVO(reflectVo));
      }

      isExistMsg = false;
      isExistReflect = !imp;

      m_pkfinindex = rtvouchVo.getDeleteclass();

      if (im)
      {
        pkRtVouch = saveRtVouch(rtvouchVo);
        if (imp) {
          msgVo.setPkRtVouch(pkRtVouch);
          msgVo.setFlag(CacheManager.RTVINT);
          m_pkfinindex = getFinindexDMO().insert(msgVo);
        }
        else {
          Properties p = (Properties)CacheManager.getInstance(Thread.currentThread()).getValue(CacheManager.QUEUECOLLFLAG, CacheManager.QUEUECOLLVKEY);

          if (p == null) {
            new DapFinIndexDMO().updateRtVouch(pkRtVouch, m_pkfinindex, null, CacheManager.RTVINT);
          }
          else {
            p.setProperty(m_pkfinindex, pkRtVouch);
          }

        }

      }

      if ((execVo.getExecType() == 2) || ((execVo.getExecType() == 5) && (isGetNumStyle)))
      {
        isExistReflect = false;

        voucherVo = mergeDetailAndRetGLVo(rtvouchVo, m_pkfinindex);

        if ((reflectVo != null) && (reflectVo.IsLogCreate()))
        {
          GlRetVo = saveVoucher(voucherVo, (isGetNumStyle) && (!isExistMsg_Log) ? null : msgVo);
        }
        else if (notNewTranscation)
          GlRetVo = saveVoucher(voucherVo, (isGetNumStyle) && (!isExistMsg_Log) ? null : msgVo);
        else {
          GlRetVo = dap.saveVoucher_RequiresNew(voucherVo, (isGetNumStyle) && (!isExistMsg_Log) ? null : msgVo);
        }

        isExistReflect = true;
        m_pkfinindex = GlRetVo.m_finindex;
      }

    }
    catch (RemoteException ex)
    {
      Throwable thr = ex.detail;
      try {
        if ((thr != null) && ((thr instanceof RtVouchException)))
        {
          switch (((RtVouchException)thr).getErrCode()) {
          case 1:
            if (!isExistReflect) {
              if (isExistMsg)
              {
                if (m_pkfinindex == null) m_pkfinindex = inVo.getPrimaryKey();
                if (notNewTranscation)
                  writeErrMsg(m_pkfinindex, thr.getMessage(), true);
                else {
                  dap.writeErrMsg_RequiresNew(m_pkfinindex, thr.getMessage(), true);
                }

              }
              else if (notNewTranscation) {
                addMessageQueue(inVo, ((RtVouchException)thr).getMessage());
              } else {
                dap.addMessageQueue_RequiresNew(inVo, ((RtVouchException)thr).getMessage());
              }

            }
            else if (notNewTranscation)
              writeErrMsg("#" + m_pkfinindex, thr.getMessage(), false);
            else {
              dap.writeErrMsg_RequiresNew("#" + m_pkfinindex, thr.getMessage(), false);
            }

            break;
          case 3:
            if ((reflectVo != null) && (reflectVo.IsLogCreate())) {
              saveRtVouchAfterException(rtvouchVo, m_pkfinindex, thr.getMessage(), execVo.getExecType(), msgVo);
            }
            else if (notNewTranscation) {
              saveRtVouchAfterException(rtvouchVo, m_pkfinindex, thr.getMessage(), execVo.getExecType(), msgVo);
            }
            else
            {
              dap.saveRtVouchAfterException_RequiresNew(rtvouchVo, m_pkfinindex, thr.getMessage(), execVo.getExecType(), msgVo);
            }

            break;
          case 5:
            int iExecType = execVo.getExecType();
            if ((iExecType != 1) && (iExecType != 4))
              break;
            if (notNewTranscation)
              updateExecType(m_pkfinindex);
            else {
              dap.updateExecType_RequiresNew(m_pkfinindex);
            }
            break;
          case 7:
            if (notNewTranscation) {
              delReflect(m_pkfinindex);
              if (inVo != null) {
                addMessageQueue(inVo, ((RtVouchException)thr).getMessage());
              }

              writeErrMsg(m_pkfinindex, ((RtVouchException)thr).getMessage(), true);
            }
            else {
              dap.delReflect_RequiresNew(m_pkfinindex);
              if (inVo != null) {
                dap.addMessageQueue_RequiresNew(inVo, ((RtVouchException)thr).getMessage());
              }

              dap.writeErrMsg_RequiresNew(m_pkfinindex, ((RtVouchException)thr).getMessage(), true);
            }
          case 2:
          case 4:
          case 6:
          }
        }
      } catch (BusinessException busex) {
        Logger.error(busex);
      }
      throw new BusinessException(ex);
    } catch (Throwable ex) {
      try {
        if ((ex instanceof RtVouchException))
        {
          switch (((RtVouchException)ex).getErrCode()) {
          case 1:
            if (!isExistReflect) {
              if (isExistMsg)
              {
                if (m_pkfinindex == null) m_pkfinindex = inVo.getPrimaryKey();
                if (notNewTranscation)
                  writeErrMsg(m_pkfinindex, ex.getMessage(), true);
                else {
                  dap.writeErrMsg_RequiresNew(m_pkfinindex, ex.getMessage(), true);
                }

              }
              else if (notNewTranscation) {
                addMessageQueue(inVo, ((RtVouchException)ex).getMessage());
              } else {
                dap.addMessageQueue_RequiresNew(inVo, ((RtVouchException)ex).getMessage());
              }

            }
            else if (notNewTranscation)
              writeErrMsg("#" + m_pkfinindex, ex.getMessage(), false);
            else {
              dap.writeErrMsg_RequiresNew("#" + m_pkfinindex, ex.getMessage(), false);
            }

            break;
          case 3:
            if ((reflectVo != null) && (reflectVo.IsLogCreate())) {
              saveRtVouchAfterException(rtvouchVo, m_pkfinindex, ex.getMessage(), execVo.getExecType(), msgVo);
            }
            else if (notNewTranscation)
              saveRtVouchAfterException(rtvouchVo, m_pkfinindex, ex.getMessage(), execVo.getExecType(), msgVo);
            else {
              dap.saveRtVouchAfterException_RequiresNew(rtvouchVo, m_pkfinindex, ex.getMessage(), execVo.getExecType(), msgVo);
            }

            break;
          case 7:
            if (notNewTranscation) {
              delReflect(m_pkfinindex);
              if (inVo != null) {
                addMessageQueue(inVo, ((RtVouchException)ex).getMessage());
              }

              writeErrMsg(m_pkfinindex, ((RtVouchException)ex).getMessage(), true);
            }
            else {
              dap.delReflect_RequiresNew(m_pkfinindex);
              if (inVo != null) {
                dap.addMessageQueue_RequiresNew(inVo, ((RtVouchException)ex).getMessage());
              }

              dap.writeErrMsg_RequiresNew(m_pkfinindex, ((RtVouchException)ex).getMessage(), true);
            }

            break;
          case 5:
            int iExecType = execVo.getExecType();
            if ((iExecType != 1) && (iExecType != 4))
              break;
            if (notNewTranscation)
              updateExecType(m_pkfinindex);
            else
              dap.updateExecType_RequiresNew(m_pkfinindex);
          case 2:
          case 4:
          case 6:
          }
        }
      } catch (BusinessException busiex) {
        Logger.error(busiex);
      }
      throw new BusinessException(ex);
    }
    finally
    {
      try {
        if (dap != null)
        {
          dap = null;
        }
      } catch (Exception ex) {
        Logger.error(ex);
      }
    }
    if (GlRetVo != null) {
      RetVoucherVO retVo = new RetVoucherVO();
      retVo.m_voucherPk = GlRetVo.m_strPK;
      return retVo;
    }
    return null;
  }

  public void procASynMessageQueue(DapBOTask[] msgVos, boolean isThrow)
    throws BusinessException
  {
    Properties p = new Properties();
    CacheManager.getInstance(Thread.currentThread()).putValue(CacheManager.QUEUECOLLFLAG, CacheManager.QUEUECOLLVKEY, p);

    AggregatedValueObject dataVo = null;
    DapMsgVO msgVo = null;
    try {
      boolean b = msgVos[0].isRecovery;
      for (int i = 0; i < msgVos.length; i++) {
        try {
          if (b) {
            msgVo = new DapMsgVO();
            msgVo.setCorp(msgVos[i].getMsgVo().getCorp());
            msgVo.setProc(msgVos[i].getMsgVo().getProc());
            msgVo.setBusiType(msgVos[i].getMsgVo().getBusiType());
            msgVo.setProcMsg(msgVos[i].getMsgVo().getProcMsg());
            msgVo.setDestSystem(msgVos[i].getMsgVo().getDestSystem());

            msgVo.setPkAccOrg(msgVos[i].getMsgVo().getPkAccOrg());
            msgVo.setPkAccount(msgVos[i].getMsgVo().getPkAccount());

            if (!getDapDMO().isExitMessage(msgVo))
              continue;
          }
          else {
            msgVo = msgVos[i].getInVo();
          }

          DapExecTypeVO execVo = null;
          if (b)
            execVo = (DapExecTypeVO)isImmedialtelyRun(msgVo);
          else {
            execVo = msgVos[i].getExcuvo();
          }

          if (execVo == null)
          {
            continue;
          }
          if (execVo.getExecType() == 6)
          {
            continue;
          }
          dataVo = msgVos[i].getDataVo();

          if (b) {
            msgVos[i].getMsgVo().setErrMsg(null);
          }
          if ((isThrow) && (b))
            msgVos[i].getMsgVo().setIsLogCreate(true);
          procAddMessage(execVo, msgVos[i].getInVo(), msgVos[i].getMsgVo(), dataVo, true, false);
        }
        catch (Exception ex) {
          System.out.println("==>>DapBO.procMessageQueue==>>" + ex.getMessage());

          if (isThrow) {
            throw new DapBusinessException(ex.getMessage(), ex);
          }
        }

      }

      if (p.size() != 0)
        batchUpdateFlag(p);
    }
    catch (Exception e) {
      throw new DapBusinessException(e.getMessage(), e);
    }
  }

  private void ProcDelMessage(DapMsgVO inVo)
    throws BusinessException
  {
    try
    {
      DapFinMsgVO msgVo = new DapFinMsgVO();
      msgVo.setCorp(inVo.getCorp());
      msgVo.setSys(inVo.getSys());
      msgVo.setProc(inVo.getProc());
      msgVo.setBusiType(inVo.getBusiType());
      msgVo.setProcMsg(inVo.getProcMsg());
      msgVo.setDestSystem(inVo.getDestSystem());
      msgVo.setPkAccOrg(inVo.getPkAccOrg());
      msgVo.setPkAccount(inVo.getPkAccount());
      DapFinMsgVO[] tmpVos = null;
      tmpVos = queryExecType(msgVo);

      if (tmpVos != null)
        for (int i = 0; i < tmpVos.length; i++) {
          int execType = tmpVos[i].getSign().intValue();
          if ((execType == 0) || (execType == 3))
          {
            delRtVoucherAndReflect(tmpVos[i]);
          }
          else
            delVoucherAndReflect(tmpVos[i]);
        }
    }
    catch (Exception ex)
    {
      if ((ex instanceof DapBusinessException))
        throw ((DapBusinessException)ex);
    }
  }

  public void procMessageQueue(DapFinMsgVO[] msgVos, UFBoolean isThrow)
    throws BusinessException
  {
    Properties p = new Properties();
    CacheManager.getInstance(Thread.currentThread()).putValue(CacheManager.QUEUECOLLFLAG, CacheManager.QUEUECOLLVKEY, p);

    AggregatedValueObject dataVo = null;
    DapMsgVO msgVo = null;
    try {
      BillTypeMsgAggVO btm = getDataByIntefaceInBulk_Con(msgVos, isThrow.booleanValue());
      Hashtable btd = btm.toHashTable();
      for (int i = 0; i < msgVos.length; i++) {
        try {
          msgVo = new DapMsgVO();
          msgVo.setCorp(msgVos[i].getCorp());
          msgVo.setProc(msgVos[i].getProc());
          msgVo.setBusiType(msgVos[i].getBusiType());
          msgVo.setProcMsg(msgVos[i].getProcMsg());
          msgVo.setDestSystem(msgVos[i].getDestSystem());
          msgVo.setPkAccOrg(msgVos[i].getPkAccOrg());
          msgVo.setPkAccount(msgVos[i].getPkAccount());

          if (!getDapDMO().isExitMessage(msgVo))
          {
            continue;
          }
          DapExecTypeVO execVo = null;

          execVo = (DapExecTypeVO)isImmedialtelyRun(msgVo);

          if (execVo == null) {
            p.remove(msgVos[i].getPkFinmsg());
            if (isThrow.booleanValue()) {
              throw new DapBusinessException(NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000037"));
            }
          }
          if (execVo.getExecType() == 6)
          {
            continue;
          }
          dataVo = (AggregatedValueObject)btd.get(msgVos[i].getProcMsg());

          if (dataVo == null) {
            continue;
          }
          msgVos[i].setErrMsg(null);

          if (isThrow.booleanValue())
            msgVos[i].setIsLogCreate(true);
          procAddMessage(execVo, null, msgVos[i], dataVo, true, false);
        }
        catch (Exception ex) {
          if (isThrow.booleanValue()) {
            throw new DapBusinessException(ex.getMessage(), ex);
          }
          Logger.error(ex);
        }

      }

      if (p.size() != 0)
        batchUpdateFlag(p);
    }
    catch (Exception e) {
      throw new DapBusinessException(e.getMessage(), e);
    }
  }

  public void procMessageQueueReflect(DapFinMsgVO[] msgVos, boolean isThrow)
    throws BusinessException
  {
    AggregatedValueObject tmpDataVo = null;
    try {
      Properties p = new Properties();
      CacheManager.getInstance(Thread.currentThread()).putValue(CacheManager.QUEUECOLLFLAG, CacheManager.QUEUECOLLVKEY, p);

      for (int i = 0; i < msgVos.length; i++)
      {
        tmpDataVo = getDataByInteface(msgVos[i]);
        try
        {
          if (!isReCreate(msgVos[i]))
          {
            continue;
          }

          DapExecTypeVO execVo = null;

          DapMsgVO msgVo = new DapMsgVO();

          msgVo.setCorp(msgVos[i].getCorp());
          msgVo.setSys(msgVos[i].getSys());
          msgVo.setProc(msgVos[i].getProc());
          msgVo.setBusiType(msgVos[i].getBusiType());
          msgVo.setPkAccOrg(msgVos[i].getPkAccOrg());
          msgVo.setPkAccount(msgVos[i].getPkAccount());
          execVo = (DapExecTypeVO)isImmedialtelyRun(msgVo);

          if (execVo == null)
          {
            continue;
          }

          if (execVo.getExecType() == 6)
          {
            continue;
          }
          procAddMessage(execVo, null, msgVos[i], tmpDataVo, false, true);
        } catch (Exception ex) {
          if (isThrow) {
            throw new DapBusinessException(ex.getMessage(), ex);
          }
        }

      }

      if (p.size() != 0)
        batchUpdateFlag(p);
    }
    catch (Exception e) {
      throw new DapBusinessException(e.getMessage(), e);
    }
  }

  public void procOneMsg(DapFinMsgVO msgVo, AggregatedValueObject dataVo)
    throws DapBusinessException
  {
    try
    {
      if (!isReCalculate(msgVo)) {
        return;
      }

      if (msgVo.getPkRtVouch() != null) {
        delRtVouch(msgVo);
      }

      DapMsgVO newMsgVo = new DapMsgVO();

      newMsgVo.setCorp(msgVo.getCorp());
      newMsgVo.setSys(msgVo.getSys());
      newMsgVo.setProc(msgVo.getProc());
      newMsgVo.setBusiType(msgVo.getBusiType());
      newMsgVo.setDestSystem(msgVo.getDestSystem());
      newMsgVo.setPkAccOrg(msgVo.getPkAccOrg());
      newMsgVo.setPkAccount(msgVo.getPkAccount());
      DapExecTypeVO execVo = null;
      execVo = (DapExecTypeVO)isImmedialtelyRun(newMsgVo);

      if (execVo == null) {
        return;
      }

      procAddMessage(execVo, null, msgVo, dataVo, false, true);
    } catch (Exception ex) {
      Logger.error(ex);
    }
  }

  public void procReCalculate(DapFinMsgVO[] msgVos)
    throws BusinessException
  {
    if (msgVos[0].isUseEnterRule()) {
      procReCalculate2(msgVos);
      return;
    }
    try
    {
      QueryLogVO qvo = new QueryLogVO();
      qvo.queryClass = 0;
      qvo.destSystem = msgVos[0].getDestSystem();
      qvo.notRtVouch = false;
      msgVos = getFinindexDMO().queryByVO(msgVos, qvo);
    } catch (Exception ex) {
      Logger.error(ex);
    }

    if (msgVos == null) {
      return;
    }
    BillTypeMsgAggVO dataVo = getDataByIntefaceInBulk(msgVos);
    Hashtable htData = dataVo.toHashTable();
    for (int i = 0; i < msgVos.length; i++) {
      Object o = htData.get(msgVos[i].getProcMsg());

      if (o == null) {
        Logger.info("错误::---单据:" + msgVos[i].getProcMsg() + "没有查找出单据数据");
      }
      else {
        AggregatedValueObject dataAggr = (AggregatedValueObject)o;
        try
        {
          DapInterfaceFactory.getDap().procOneMsg_RequiresNew(msgVos[i], dataAggr);
        }
        catch (Exception e) {
          Logger.error(e);
          throw new BusinessException(e.getMessage());
        }
        finally
        {
        }
      }
    }
  }

  public DapMsgVO[] procReCreateMsgs(DapFinMsgVO[] msgVos) throws BusinessException {
    DapMsgVO[] newMsgs = null;
    try
    {
      DapFinMsgVO[] oriMsgs = getReCalculateMsgs(msgVos);
      DapFinMsgVO[] oriFinMsgs = getReCalculateFinMsgs(msgVos);

      DapFinMsgVO[] distMsgs = distinctMsgs(oriMsgs, oriFinMsgs);

      newMsgs = buildMsgs(distMsgs);

      getDapMsgDMO().deleteBatch(oriMsgs);
      if (oriFinMsgs != null) {
        for (int i = 0; i < oriFinMsgs.length; i++) {
          delRtVoucherAndReflect(oriFinMsgs[i]);
        }
      }

      newMsgs = saveMsgs(newMsgs);
    } catch (Exception e) {
      throw new DapBusinessException("", e);
    }
    return newMsgs;
  }

  private void procReCalculate2(DapFinMsgVO[] msgVos)
    throws BusinessException
  {
    if (canReCalculate(msgVos));
    IDap dap = DapInterfaceFactory.getDap();
    try {
      DapMsgVO[] newMsgs = dap.procReCreateMsgs(msgVos);

      if ((newMsgs != null) && (newMsgs.length > 0))
        createVoucher(newMsgs);
    }
    catch (Exception e)
    {
      Logger.error(e);
      throw new DapBusinessException("", e);
    }
    finally
    {
    }
  }

  private boolean canReCalculate(DapFinMsgVO[] msgVos)
    throws BusinessException
  {
    try
    {
      DapFinMsgVO[] oriMsgs = getReCalculateMsgs(msgVos);
      DapFinMsgVO[] oriFinMsgs = getReCalculateFinMsgs(msgVos);
      DapFinMsgVO[] createFinMsgs = getBillCreatFinMsgs(msgVos);

      if (((oriMsgs != null) && (oriMsgs.length > 0)) || ((oriFinMsgs != null) && (oriFinMsgs.length > 0) && (createFinMsgs != null) && (createFinMsgs.length > 0)))
      {
        String errmessage = Translator.translate("UPPfidap-000857");
        throw new DapBusinessException(errmessage);
      }
    } catch (Exception e) {
      Logger.error(e);
      String errmessage = Translator.translate("UPPfidap-000857");
      throw new DapBusinessException(errmessage, e);
    }
    return true;
  }

  private DapFinMsgVO[] getReCalculateFinMsgs(DapFinMsgVO[] msgVos)
    throws SystemException, NamingException, SQLException
  {
    DapFinMsgVO[] reVos = null;
    if (msgVos != null) {
      for (int i = 0; i < msgVos.length; i++) {
        msgVos[i].setPkAccOrg(null);
        msgVos[i].setPkAccount(null);
      }
      QueryLogVO qvo = new QueryLogVO();
      qvo.queryClass = 0;
      qvo.destSystem = msgVos[0].getDestSystem();
      qvo.notRtVouch = false;
      reVos = getFinindexDMO().queryByVO(msgVos, qvo);
    }

    return reVos;
  }

  private DapFinMsgVO[] getBillCreatFinMsgs(DapFinMsgVO[] msgVos)
    throws SystemException, NamingException, SQLException
  {
    DapFinMsgVO[] reVos = null;
    if (msgVos != null) {
      for (int i = 0; i < msgVos.length; i++) {
        msgVos[i].setPkAccOrg(null);
        msgVos[i].setPkAccount(null);
      }
      QueryLogVO qvo = new QueryLogVO();
      qvo.queryClass = 1;
      qvo.destSystem = msgVos[0].getDestSystem();
      qvo.notRtVouch = false;
      reVos = getFinindexDMO().queryByVO(msgVos, qvo);
    }

    return reVos;
  }

  private void createVoucher(DapMsgVO[] msgs)
    throws SystemException, NamingException, SQLException, BusinessException
  {
    DapFinMsgVO[] finmsgs = new DapFinMsgVO[msgs.length];
    for (int i = 0; i < msgs.length; i++) {
      finmsgs[i] = DapFinMsgVO.changeFinIndexVO(msgs[i], null);
      finmsgs[i].setPkFinmsg(msgs[i].getPrimaryKey());
    }
    try
    {
      procMessageQueue(finmsgs, new UFBoolean(true));
      getDapMsgDMO().deleteBatch(msgs);
    } catch (Exception ex) {
      Logger.error(ex);
      throw new BusinessException(ex.getMessage());
    }
  }

  private DapMsgVO[] saveMsgs(DapMsgVO[] newMsgs)
    throws NamingException, SystemException, SQLException
  {
    String errmessage = NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000853");
    for (int i = 0; i < newMsgs.length; i++) {
      newMsgs[i].setErrMsg(errmessage);
      newMsgs[i].setPrimaryKey(getDapMsgDMO().insert(newMsgs[i]).getPrimaryKey());
    }
    return newMsgs;
  }

  private DapMsgVO[] buildMsgs(DapFinMsgVO[] distMsgs)
    throws Exception
  {
    List msgs = new ArrayList();

    BillTypeMsgAggVO dataVo = getDataByIntefaceInBulk(distMsgs);
    Hashtable htData = null;
    if (dataVo != null) {
      htData = dataVo.toHashTable();
    }

    for (int i = 0; i < distMsgs.length; i++)
    {
      AggregatedValueObject vo = (AggregatedValueObject)htData.get(distMsgs[i].getProcMsg());
      DapMsgVO inVo = VoucherConvert.findMsg2Msg(distMsgs[i]);
      MessageBuilder mb = new MessageBuilder();
      DapMsgVO[] dmvs = mb.builderMessage(inVo, vo);

      if (dmvs != null) {
        for (int j = 0; j < dmvs.length; j++) {
          msgs.add(dmvs[j]);
        }
      }
    }

    DapMsgVO[] msgvos = new DapMsgVO[msgs.size()];
    if (msgs.size() > 0) {
      msgs.toArray(msgvos);
    }
    return msgvos;
  }

  private DapFinMsgVO[] distinctMsgs(DapFinMsgVO[] oriMsgs, DapFinMsgVO[] oriFinMsgs)
  {
    List msgs = new ArrayList();
    List keys = new ArrayList();

    if (oriMsgs != null) {
      for (int i = 0; i < oriMsgs.length; i++) {
        String key = oriMsgs[i].getProc() + oriMsgs[i].getProcMsg();
        if (!keys.contains(key)) {
          keys.add(key);
          msgs.add(oriMsgs[i]);
        }
      }
    }
    if (oriFinMsgs != null) {
      for (int i = 0; i < oriFinMsgs.length; i++) {
        String key = oriFinMsgs[i].getProc() + oriFinMsgs[i].getProcMsg();
        if (!keys.contains(key)) {
          keys.add(key);
          msgs.add(oriFinMsgs[i]);
        }
      }
    }
    DapFinMsgVO[] revo = new DapFinMsgVO[msgs.size()];
    if (msgs.size() > 0) {
      msgs.toArray(revo);
    }
    return revo;
  }

  private DapFinMsgVO[] getReCalculateMsgs(DapFinMsgVO[] msgVos)
    throws SystemException, NamingException, SQLException
  {
    List msgs = new ArrayList();

    for (int i = 0; i < msgVos.length; i++) {
      msgVos[i].setPkAccOrg(null);
      msgVos[i].setPkAccount(null);
      DapFinMsgVO[] tmpvos = getDapMsgDMO().query(msgVos[i]);
      if (tmpvos != null) {
        for (int j = 0; j < tmpvos.length; j++) {
          msgs.add(tmpvos[j]);
        }
      }
    }

    DapFinMsgVO[] revos = new DapFinMsgVO[msgs.size()];
    if (msgs.size() > 0) {
      msgs.toArray(revos);
    }
    return revos;
  }

  public DapBOTask[] queryASysMessage(String queueName, int groupid, int exectype)
    throws DapBusinessException
  {
    DapFinMsgVO[] msgVos = null;
    DapBOTask[] dBots = null;
    Vector v = new Vector();
    for (int i = 0; i < IAccountPlat.DESTSYS.length; i++) {
      QueryLogVO qvo = new QueryLogVO();
      qvo.destSystem = IAccountPlat.DESTSYS[i];
      qvo.queuename = queueName;
      qvo.groupid = groupid;
      qvo.sysworktype = 0;
      qvo.asynexectype = exectype;
      qvo.noErrorMsg = true;
      try {
        msgVos = getDapMsgDMO().queryAll(qvo);
        if ((msgVos != null) && (msgVos.length != 0)) {
          BillTypeMsgAggVO dataVo = getDataByIntefaceInBulk(msgVos);
          Hashtable htData = dataVo.toHashTable();
          DapBOTask[] dBot = new DapBOTask[msgVos.length];
          for (int j = 0; j < msgVos.length; j++) {
            dBot[j] = new DapBOTask();
            dBot[j].isRecovery = true;
            dBot[j].setMsgVo(msgVos[j]);
            dBot[j].setDataVo((AggregatedValueObject)htData.get(msgVos[j].getProcMsg()));

            v.add(dBot[j]);
          }
        }
      } catch (Exception ex) {
        Logger.error(ex);
        throw new DapBusinessException(NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000038") + ex.getMessage(), ex);
      }
    }
    if ((v != null) && (v.size() != 0)) {
      dBots = new DapBOTask[v.size()];
      v.copyInto(dBots);
    }
    return dBots;
  }

  private DapFinMsgVO[] queryExecType(DapFinMsgVO inVo)
    throws BusinessException
  {
    DapFinMsgVO[] msgVo = null;
    try {
      QueryLogVO qvo = new QueryLogVO();
      getFinindexDMO().getClass(); qvo.queryClass = 2;
      qvo.notRtVouch = false;
      msgVo = getFinindexDMO().queryByVO(new DapFinMsgVO[] { inVo }, qvo);
    } catch (Exception ex) {
      Logger.error(ex);
      throw new DapBusinessException(NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000039") + ex.getMessage(), ex);
    }
    return msgVo;
  }

  public void queryMessageQueueAndProc()
    throws BusinessException
  {
    for (int i = 0; i < IAccountPlat.DESTSYS.length; i++) {
      QueryLogVO qvo = new QueryLogVO();
      qvo.destSystem = IAccountPlat.DESTSYS[i];
      DapFinMsgVO[] msgVos = null;
      try {
        msgVos = getDapMsgDMO().queryAll(qvo);
      } catch (Exception ex) {
        Logger.error(ex);
        throw new DapBusinessException(NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000038") + ex.getMessage(), ex);
      }

      DapFinMsgVO[] msgQueueVos = null;
      try {
        msgQueueVos = getFinindexDMO().queryMsgQueue(qvo);
      } catch (Exception ex) {
        Logger.error(ex);
        throw new DapBusinessException(NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000040") + ex.getMessage(), ex);
      }

      if (msgVos != null) {
        procMessageQueue(msgVos, new UFBoolean(false));
      }

      if (msgQueueVos != null)
        procMessageQueueReflect(msgQueueVos, false);
    }
  }

  public DapFinMsgVO[] queryVirtualVoucher(DapFinMsgVO[] msgVO)
    throws BusinessException
  {
    QueryLogVO qvo = new QueryLogVO();
    qvo.destSystem = msgVO[0].getDestSystem();
    qvo.queryClass = 4;
    qvo.notRtVouch = true;

    Vector vBillID = new Vector();
    for (int i = 0; i < msgVO.length; i++) {
      if (msgVO[i] != null) {
        vBillID.addElement(msgVO[i].getProcMsg());
        msgVO[i].setProcMsg(null);
      }
    }
    if (vBillID.size() > 0) {
      qvo.billIDs = new String[vBillID.size()];
      vBillID.copyInto(qvo.billIDs);
    }
    try
    {
      return getFinindexDMO().queryByVO(msgVO, qvo); } catch (Exception ex) {
    }
    throw new DapBusinessException(NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000041"));
  }

  public DapFactorVO[] queryGlFactorAttr(String pk_corp, String pkBillType, String pk_glorg, int flag)
    throws BusinessException
  {
    DapFactorVO[] factorVos = null;
    try
    {
      factorVos = getDapDMO().queryGlFactorAttr(pk_corp, pkBillType, pk_glorg, flag);
    } catch (Exception ex) {
      throw new DapBusinessException(NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000041"));
    }
    return factorVos;
  }

  private String saveRtVouch(VoucherVO rtvouchVo)
    throws RtVouchException
  {
    String pkRtVouch = null;
    try {
      VoucherVO[] vo = new DapRtVouchBO().saveVouchers(new VoucherVO[] { rtvouchVo });
      pkRtVouch = vo[0].getPk_voucher();
    } catch (Exception ex) {
      Logger.error("保存实时凭证时出错！", ex);
    }
    return pkRtVouch;
  }

  public void saveRtVouchAfterException(VoucherVO rtvouchVo, String pkFinIndex, String errMsg, int execType, DapFinMsgVO msgVo)
    throws BusinessException
  {
    try
    {
      String pkRtVouch = saveRtVouch(rtvouchVo);

      Integer objExecType = null;
      if ((execType == 2) || (execType == 1))
      {
        objExecType = new Integer(0);
      }
      if ((msgVo != null) && (msgVo.IsLogCreate())) {
        getDapMsgDMO().delete(DapFinMsgVO.changeMsgVO(msgVo));
      }

      if ((pkFinIndex == null) || (pkFinIndex.trim().length() == 0)) {
        msgVo.setFlag(CacheManager.RTVINT);
        pkFinIndex = getFinindexDMO().insert(msgVo);
      }
      getFinindexDMO().updateRtVouch(pkRtVouch, pkFinIndex, objExecType, null);

      writeErrMsg(pkFinIndex, errMsg, false);
    } catch (Exception ex) {
      if ((ex instanceof DapBusinessException)) {
        throw ((DapBusinessException)ex);
      }
      throw new DapBusinessException(ex.getMessage(), ex);
    }
  }

  public OperationResultVO saveVirtualVoucher(VoucherVO voucherVO, DapFinMsgVO finMsgVO)
    throws BusinessException
  {
    try
    {
      getFinindexDMO().insert(finMsgVO);
    } catch (Exception e) {
      throw new DapBusinessException(NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000042") + e.getMessage());
    }

    return null;
  }

  public OperationResultVO[] saveVirtualVouchers(VoucherVO voucherVO, DapFinMsgVO[] finMsgVO)
    throws BusinessException
  {
    if (finMsgVO == null)
      throw new DapBusinessException(NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000043"));
    for (int i = 0; i < finMsgVO.length; i++) {
      saveVirtualVoucher(voucherVO, finMsgVO[i]);
    }
    return null;
  }

  public OperationResultVO saveVoucher(VoucherVO voucherVo, DapFinMsgVO msgVo)
    throws BusinessException, RtVouchException
  {
    String strErr = null;
    String m_pkfinindex = voucherVo.getDeleteclass();
    try {
      boolean b = false;
      if (msgVo != null) {
        msgVo.setFlag(CacheManager.RTVINT);
        m_pkfinindex = getFinindexDMO().insert(msgVo);
        b = true;
        voucherVo.setUserData(new String[] { m_pkfinindex, "FININDEX" });
        this.GLRetVo = new OperationResultVO();
        this.GLRetVo.m_finindex = m_pkfinindex;
      }

      GlorgbookVO glorgbook = getTaskRP().getGlorgbook(voucherVo.getPk_glorg(), voucherVo.getPk_glbook());
      if (glorgbook.isSeal()) {
        String message = NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000854", null, new String[] { glorgbook.getGlorgbookname() });
        throw new Exception(message);
      }
      voucherVo.setPk_glorgbook(glorgbook.getPrimaryKey());
      this.GLRetVo = CallInterface.getInstance().getVoucherSaveBO(voucherVo.getPk_corp(), voucherVo.getDestSystem()).saveVoucher(voucherVo)[0];

      if (b) {
        this.GLRetVo.m_finindex = m_pkfinindex;
      }
    }
    catch (Exception ex)
    {
      Logger.error(ex);
      if (ex.getMessage() != null)
        strErr = NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000044") + ex.getMessage();
      else {
        strErr = NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000044");
      }
      throw new RtVouchException(strErr, 3);
    }
    return this.GLRetVo;
  }

  public RetVoucherVO sendDapMessage(DapMsgVO inVo, AggregatedValueObject vo)
    throws BusinessException
  {
    RetVoucherVO retVo = null;
    DapLoger.loger.debug(NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000046"));
    IDap dap = DapInterfaceFactory.getDap();
    try
    {
      switch (inVo.getMsgType()) {
      case 1:
        try {
          if (inVo.isRequestNewTranscation())
          {
            dap.delMessage_RequiresNew(inVo);
          }
          else {
            delMessage(inVo);
          }

          retVo = new RetVoucherVO();
        } catch (Exception e) {
          retVo = new RetVoucherVO();
          retVo.setOperateResult(RetVoucherVO.OPE_FAILED);
          retVo.errMsg = e.getMessage();
          if ((e instanceof DapBusinessException)) {
            throw ((DapBusinessException)e);
          }
          throw new DapBusinessException(e.getMessage(), new Exception(e.getMessage()));
        }

      case 3:
        dap.offsetRed_RequiresNew(inVo);
        break;
      default:
        MessageBuilder mb = new MessageBuilder();
        if (inVo.getDestSystem() == 0)
        {
          DapMsgVO[] dmvs;
          if ((inVo.getPkAccOrg() == null) && (inVo.getPkAccount() == null))
          {
            dmvs = mb.builderMessage(inVo, vo);
            int len = dmvs.length;
            RetVoucherVO[] rvv = new RetVoucherVO[len];
            for (int i = 0; i < len; i++) {
              rvv[i] = addMessage(dmvs[i], vo);
            }
            if (rvv != null) {
              retVo = rvv[0];
            }

            if (retVo != null) retVo.rvv = rvv; 
          }
          else {
            return addMessage(inVo, vo);
          }
        }
        else
        {
          String pk_corp = inVo.getCorp();
          GlorgbookVO glorgbook = GLOrgBookAcc.getDefaultGlOrgBookVOByPk_EntityOrg(pk_corp);
          inVo.setPkAccOrg(glorgbook.getPk_glorg());
          inVo.setPkAccount(glorgbook.getPk_glbook());
          retVo = addMessage(inVo, vo);
        }
      }
    }
    catch (BusinessException ex)
    {
      throw ex;
    } catch (Exception ex) {
      DapLoger.loger.error(ex.getMessage(), ex);
      if ((ex instanceof DapBusinessException))
        throw ((DapBusinessException)ex);
      throw new DapBusinessException(ex.getMessage(), new Exception(ex.getMessage()));
    }
    finally
    {
    }

    DapLoger.loger.debug(NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000047"));
    return retVo;
  }

  public void updateExecType(String pkMsg)
    throws DapBusinessException
  {
    try
    {
      getFinindexDMO().updateExecType(pkMsg, 2);
    } catch (Exception e) {
      Logger.error(e);
      throw new DapBusinessException(e.getMessage(), e);
    }
  }

  public void writeErrMsg(String pkMsg, String strErr, boolean isMessageQueue)
    throws BusinessException
  {
    if (isMessageQueue)
      try {
        getDapMsgDMO().updateErrMsg(pkMsg, strErr);
      } catch (Exception e) {
        Logger.error(e);
        String errString = NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000048");
        throw new DapBusinessException(errString, e);
      }
    else
      try {
        getFinindexDMO().updateErrMsg(pkMsg, strErr);
      } catch (Exception e) {
        Logger.error(e);
        String errString = NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000049");
        throw new DapBusinessException(errString, e);
      }
  }

  private TaskRedirectProxy getTaskRP()
  {
    if (this.m_taskRP == null) {
      this.m_taskRP = new TaskRedirectProxy();
    }
    return this.m_taskRP;
  }

  private VoucherVO dealGLBalance(VoucherVO newVoucher, int digit)
  {
    if ((newVoucher.getDetails() != null) && (newVoucher.getDetails().length != 0)) {
      UFDouble creditTotal = new UFDouble(0, digit);
      UFDouble debitTotal = new UFDouble(0, digit);
      for (int j = 0; j < newVoucher.getDetails().length; j++) {
        newVoucher.getDetails()[j].setLocalcreditamount(newVoucher.getDetails()[j].getLocalcreditamount().setScale(digit, 4));
        newVoucher.getDetails()[j].setLocaldebitamount(newVoucher.getDetails()[j].getLocaldebitamount().setScale(digit, 4));
        creditTotal = creditTotal.add(newVoucher.getDetails()[j].getLocalcreditamount());
        debitTotal = debitTotal.add(newVoucher.getDetails()[j].getLocaldebitamount());
      }
      DetailVO[] details = newVoucher.getDetails();
      if (creditTotal.doubleValue() != debitTotal.doubleValue()) {
        if (creditTotal.doubleValue() > debitTotal.doubleValue())
        {
          UFDouble banlance = creditTotal.sub(debitTotal, digit);

          for (int k = 0; k < details.length; k++) {
            if (details[k].getLocaldebitamount().doubleValue() > 0.0D) {
              details[k].setLocaldebitamount(details[k].getLocaldebitamount().add(banlance));

              break;
            }
          }
          debitTotal = creditTotal;
        }
        else {
          UFDouble banlance = debitTotal.sub(creditTotal, digit);

          for (int k = 0; k < details.length; k++) {
            if (details[k].getLocalcreditamount().doubleValue() > 0.0D) {
              details[k].setLocalcreditamount(details[k].getLocalcreditamount().add(banlance));

              break;
            }
          }
          creditTotal = debitTotal;
        }
      }
      newVoucher.setDetails(details);
      newVoucher.setTotalcredit(creditTotal.setScale(digit, 4));

      newVoucher.setTotaldebit(debitTotal.setScale(digit, 4));

      newVoucher.setExplanation(newVoucher.getDetails()[0].getExplanation());
    }
    return newVoucher;
  }

  private VoucherVO dealFTSBalance(VoucherVO newVoucher, int digit)
  {
    if ((newVoucher.getDetails() != null) && (newVoucher.getDetails().length != 0))
    {
      UFDouble creditTotal = new UFDouble(0, digit);
      UFDouble debitTotal = new UFDouble(0, digit);
      for (int j = 0; j < newVoucher.getDetails().length; j++) {
        newVoucher.getDetails()[j].setCreditamount(newVoucher.getDetails()[j].getCreditamount().setScale(digit, 4));
        newVoucher.getDetails()[j].setDebitamount(newVoucher.getDetails()[j].getDebitamount().setScale(digit, 4));
        creditTotal = creditTotal.add(newVoucher.getDetails()[j].getCreditamount());
        debitTotal = debitTotal.add(newVoucher.getDetails()[j].getDebitamount());
      }
      DetailVO[] details = newVoucher.getDetails();
      if (creditTotal.doubleValue() != debitTotal.doubleValue()) {
        if (creditTotal.doubleValue() > debitTotal.doubleValue())
        {
          UFDouble banlance = creditTotal.sub(debitTotal, digit);

          for (int k = 0; k < details.length; k++) {
            if (details[k].getDebitamount().doubleValue() > 0.0D) {
              details[k].setDebitamount(details[k].getDebitamount().add(banlance));

              break;
            }
          }
          debitTotal = creditTotal;
        }
        else {
          UFDouble banlance = debitTotal.sub(creditTotal, digit);

          for (int k = 0; k < details.length; k++) {
            if (details[k].getCreditamount().doubleValue() > 0.0D) {
              details[k].setCreditamount(details[k].getCreditamount().add(banlance));

              break;
            }
          }
          creditTotal = debitTotal;
        }
      }
      newVoucher.setDetails(details);
      newVoucher.setTotalcredit(creditTotal.setScale(digit, 4));

      newVoucher.setTotaldebit(debitTotal.setScale(digit, 4));

      newVoucher.setExplanation(newVoucher.getDetails()[0].getExplanation());
    }
    return newVoucher;
  }
}