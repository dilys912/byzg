package nc.itf.fi.ejb.dap;

import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.Vector;
import nc.bs.dap.outinterface.IVirtualVoucher;
import nc.bs.dap.rtvouch.RtVouchException;
import nc.bs.framework.exception.FrameworkEJBException;
import nc.bs.logging.Logger;
import nc.bs.mw.naming.BeanBase;
import nc.itf.dap.pub.IBillQueryVoucher;
import nc.itf.dap.pub.IVoucherQuery;
import nc.vo.bd.b54.GlorgbookVO;
import nc.vo.dap.accrule.GlBookVO;
import nc.vo.dap.accrule.GlOrgVO;
import nc.vo.dap.accrule.OrgaccDefVO;
import nc.vo.dap.factor.BillfactorVO;
import nc.vo.dap.factor.FactorVO;
import nc.vo.dap.inteface.DetailVO;
import nc.vo.dap.inteface.OperationResultVO;
import nc.vo.dap.out.BillQueryVoucherVO;
import nc.vo.dap.out.DapMsgVO;
import nc.vo.dap.out.RetBillVo;
import nc.vo.dap.pub.DapBusinessException;
import nc.vo.dap.queryplus.QueryLogVO;
import nc.vo.dap.rtvouch.DapExecTypeVO;
import nc.vo.dap.rtvouch.DapFactorVO;
import nc.vo.dap.rtvouch.DapFinMsgVO;
import nc.vo.dap.service.VoucherIndexVO;
import nc.vo.dap.subjclass.InsubjclassVO;
import nc.vo.dap.subjclass01.InsubjclassfactorVO;
import nc.vo.dap.subjclass02.SubjviewVO;
import nc.vo.dap.voucher.AssistantAccountingVO;
import nc.vo.dap.voucher.BillTypeAndProcessVO;
import nc.vo.dap.voucher.ControlruleVO;
import nc.vo.dap.voucher.MergeschemeVO;
import nc.vo.dap.vouchtemp.RetVoucherVO;
import nc.vo.dap.vouchtemp.ReturnPKVO;
import nc.vo.dap.vouchtemp.VoucherTemplateVO;
import nc.vo.dap.vouchtemp.VouchtempVO;
import nc.vo.fi.uforeport.IufoModuleVO;
import nc.vo.fipf.pub.InitVCParamVO;
import nc.vo.fipf.pub.InitVouControlVO;
import nc.vo.pf.pub.DataPowerStruVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.billtype.DapsystemVO;
import nc.vo.pub.billtype.DefitemVO;
import nc.vo.pub.busitrans.BusiTransVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;

public class DAPPrivateEJB_Local extends BeanBase
  implements DAPPrivateEJBEjbObject
{
  private DAPPrivateEJBEjbBean _getBeanObject()
    throws RemoteException
  {
    return (DAPPrivateEJBEjbBean)getEjb();
  }

  public void deleteBill(String arg0) throws BusinessException {
    beforeCallMethod(200);
    Exception er = null;
    try {
      _getBeanObject().deleteBill(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(200, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
  }

  public void copyBill(String arg0, String arg1, String arg2, String arg3, String arg4) throws BusinessException {
    beforeCallMethod(201);
    Exception er = null;
    try {
      _getBeanObject().copyBill(arg0, arg1, arg2, arg3, arg4);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(201, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
  }

  public void deleteFactor(FactorVO arg0) throws BusinessException {
    beforeCallMethod(202);
    Exception er = null;
    try {
      _getBeanObject().deleteFactor(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(202, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
  }

  public String insertFactor(FactorVO arg0) throws BusinessException {
    beforeCallMethod(203);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().insertFactor(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(203, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void updateFactor(FactorVO arg0) throws BusinessException {
    beforeCallMethod(204);
    Exception er = null;
    try {
      _getBeanObject().updateFactor(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(204, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
  }

  public FactorVO findFactorByPrimaryKey(String arg0) throws BusinessException {
    beforeCallMethod(205);
    Exception er = null;
    FactorVO o = null;
    try {
      o = _getBeanObject().findFactorByPrimaryKey(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(205, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public String[] insertFactorArray(FactorVO[] arg0) throws BusinessException {
    beforeCallMethod(206);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().insertFactorArray(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(206, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public FactorVO[] findFactorsByBtcode(String arg0) throws BusinessException {
    beforeCallMethod(207);
    Exception er = null;
    FactorVO[] o = null;
    try {
      o = (FactorVO[])_getBeanObject().findFactorsByBtcode(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(207, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public String saveFactorVO(FactorVO arg0) throws BusinessException {
    beforeCallMethod(208);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().saveFactorVO(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(208, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public FactorVO[] queryFactorByVO(FactorVO arg0, Boolean arg1) throws BusinessException {
    beforeCallMethod(209);
    Exception er = null;
    FactorVO[] o = null;
    try {
      o = (FactorVO[])_getBeanObject().queryFactorByVO(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(209, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public FactorVO[] queryAllFactor(String arg0) throws BusinessException {
    beforeCallMethod(210);
    Exception er = null;
    FactorVO[] o = null;
    try {
      o = (FactorVO[])_getBeanObject().queryAllFactor(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(210, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public InsubjclassfactorVO[] queryFactorNameVO(String arg0) throws BusinessException {
    beforeCallMethod(211);
    Exception er = null;
    InsubjclassfactorVO[] o = null;
    try {
      o = (InsubjclassfactorVO[])_getBeanObject().queryFactorNameVO(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(211, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void deleteInsubjclassfactor(InsubjclassfactorVO arg0) throws BusinessException {
    beforeCallMethod(212);
    Exception er = null;
    try {
      _getBeanObject().deleteInsubjclassfactor(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(212, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
  }

  public String insertInsubjclassfactor(InsubjclassfactorVO arg0) throws BusinessException {
    beforeCallMethod(213);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().insertInsubjclassfactor(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(213, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void updateInsubjclassfactor(InsubjclassfactorVO arg0) throws BusinessException {
    beforeCallMethod(214);
    Exception er = null;
    try {
      _getBeanObject().updateInsubjclassfactor(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(214, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
  }

  public String[] insertInsubjclassfactorArray(InsubjclassfactorVO[] arg0) throws BusinessException {
    beforeCallMethod(215);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().insertInsubjclassfactorArray(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(215, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public InsubjclassfactorVO[] queryAllInsubjclassfactor(String arg0) throws BusinessException {
    beforeCallMethod(216);
    Exception er = null;
    InsubjclassfactorVO[] o = null;
    try {
      o = (InsubjclassfactorVO[])_getBeanObject().queryAllInsubjclassfactor(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(216, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public InsubjclassfactorVO[] queryInsubjclassfactorByVO(InsubjclassfactorVO arg0, Boolean arg1) throws BusinessException {
    beforeCallMethod(217);
    Exception er = null;
    InsubjclassfactorVO[] o = null;
    try {
      o = (InsubjclassfactorVO[])_getBeanObject().queryInsubjclassfactorByVO(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(217, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void deleteAllFactors(String arg0) throws BusinessException {
    beforeCallMethod(218);
    Exception er = null;
    try {
      _getBeanObject().deleteAllFactors(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(218, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
  }

  public void updateFactorAndSubjview(int[] arg0, int[] arg1, int arg2, InsubjclassVO arg3, InsubjclassfactorVO[] arg4) throws BusinessException {
    beforeCallMethod(219);
    Exception er = null;
    try {
      _getBeanObject().updateFactorAndSubjview(arg0, arg1, arg2, arg3, arg4);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(219, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
  }

  public RetVoucherVO sendMessage(DapMsgVO arg0, AggregatedValueObject arg1) throws BusinessException {
    beforeCallMethod(220);
    Exception er = null;
    RetVoucherVO o = null;
    try {
      o = _getBeanObject().sendMessage(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(220, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public boolean isEditBillTypeOrProc(String arg0, String arg1, String arg2, String arg3, String arg4) throws BusinessException {
    beforeCallMethod(221);
    Exception er = null;
    boolean o = false;
    try {
      o = _getBeanObject().isEditBillTypeOrProc(arg0, arg1, arg2, arg3, arg4);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(221, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public boolean isEditBillType(String arg0, String arg1, String arg2, String arg3, String arg4) throws BusinessException {
    beforeCallMethod(222);
    Exception er = null;
    boolean o = false;
    try {
      o = _getBeanObject().isEditBillType(arg0, arg1, arg2, arg3, arg4);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(222, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void queryMessageQueueAndProc() throws BusinessException {
    beforeCallMethod(223);
    Exception er = null;
    try {
      _getBeanObject().queryMessageQueueAndProc();
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(223, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
  }

  public RetBillVo[] getPeriodNoCompleteBill(String arg0, String arg1, String arg2, int arg3) throws BusinessException {
    beforeCallMethod(224);
    Exception er = null;
    RetBillVo[] o = null;
    try {
      o = (RetBillVo[])_getBeanObject().getPeriodNoCompleteBill(arg0, arg1, arg2, arg3);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(224, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public VoucherIndexVO[] queryBookVouchers(BillQueryVoucherVO[] arg0) throws BusinessException {
    beforeCallMethod(225);
    Exception er = null;
    VoucherIndexVO[] o = null;
    try {
      o = (VoucherIndexVO[])_getBeanObject().queryBookVouchers(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(225, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void updateRtVouchMsg(DapFinMsgVO[] arg0) throws BusinessException {
    beforeCallMethod(226);
    Exception er = null;
    try {
      _getBeanObject().updateRtVouchMsg(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(226, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
  }

  public UFBoolean existRtVoucher(String arg0, String arg1, String arg2, String arg3) throws BusinessException {
    beforeCallMethod(227);
    Exception er = null;
    UFBoolean o = null;
    try {
      o = _getBeanObject().existRtVoucher(arg0, arg1, arg2, arg3);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(227, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void deleteRtVouchMsg(DapFinMsgVO[] arg0) throws BusinessException {
    beforeCallMethod(228);
    Exception er = null;
    try {
      _getBeanObject().deleteRtVouchMsg(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(228, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
  }

  public void makeRtVouch(DapFinMsgVO arg0) throws BusinessException {
    beforeCallMethod(229);
    Exception er = null;
    try {
      _getBeanObject().makeRtVouch(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(229, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
  }

  public DapFinMsgVO[] queryAllRtVouchMsg(QueryLogVO arg0) throws BusinessException {
    beforeCallMethod(230);
    Exception er = null;
    DapFinMsgVO[] o = null;
    try {
      o = (DapFinMsgVO[])_getBeanObject().queryAllRtVouchMsg(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(230, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public DapFinMsgVO[] queryAllVoucherMsg(QueryLogVO arg0) throws BusinessException {
    beforeCallMethod(231);
    Exception er = null;
    DapFinMsgVO[] o = null;
    try {
      o = (DapFinMsgVO[])_getBeanObject().queryAllVoucherMsg(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(231, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public DapFinMsgVO[] queryLog(DapFinMsgVO[] arg0, QueryLogVO arg1) throws BusinessException {
    beforeCallMethod(232);
    Exception er = null;
    DapFinMsgVO[] o = null;
    try {
      o = (DapFinMsgVO[])_getBeanObject().queryLog(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(232, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void deleteInsubjclass(InsubjclassVO[] arg0) throws BusinessException {
    beforeCallMethod(233);
    Exception er = null;
    try {
      _getBeanObject().deleteInsubjclass(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(233, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
  }

  public String insertInsubjclass(InsubjclassVO arg0) throws BusinessException {
    beforeCallMethod(234);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().insertInsubjclass(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(234, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void updateInsubjclass(InsubjclassVO arg0) throws BusinessException {
    beforeCallMethod(235);
    Exception er = null;
    try {
      _getBeanObject().updateInsubjclass(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(235, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
  }

  public InsubjclassVO findInsubjclassByPrimaryKey(String arg0) throws BusinessException {
    beforeCallMethod(236);
    Exception er = null;
    InsubjclassVO o = null;
    try {
      o = _getBeanObject().findInsubjclassByPrimaryKey(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(236, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public String[] insertInsubjclassArray(InsubjclassVO[] arg0) throws BusinessException {
    beforeCallMethod(237);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().insertInsubjclassArray(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(237, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public InsubjclassVO[] queryAllInsubjclass(String arg0) throws BusinessException {
    beforeCallMethod(238);
    Exception er = null;
    InsubjclassVO[] o = null;
    try {
      o = (InsubjclassVO[])_getBeanObject().queryAllInsubjclass(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(238, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public InsubjclassVO[] queryInsubjclassByVO(InsubjclassVO arg0, Boolean arg1) throws BusinessException {
    beforeCallMethod(239);
    Exception er = null;
    InsubjclassVO[] o = null;
    try {
      o = (InsubjclassVO[])_getBeanObject().queryInsubjclassByVO(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(239, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public OrgaccDefVO[] queryOrgaccDefByCorp(String arg0) throws BusinessException {
    beforeCallMethod(240);
    Exception er = null;
    OrgaccDefVO[] o = null;
    try {
      o = (OrgaccDefVO[])_getBeanObject().queryOrgaccDefByCorp(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(240, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void deleteOrgaccDef(OrgaccDefVO arg0) throws BusinessException {
    beforeCallMethod(241);
    Exception er = null;
    try {
      _getBeanObject().deleteOrgaccDef(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(241, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
  }

  public String updateOrgaccDef(OrgaccDefVO arg0) throws BusinessException {
    beforeCallMethod(242);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().updateOrgaccDef(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(242, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void copyForOrgbook(String arg0, String arg1, String arg2, GlorgbookVO[] arg3, UFBoolean arg4) throws BusinessException {
    beforeCallMethod(243);
    Exception er = null;
    try {
      _getBeanObject().copyForOrgbook(arg0, arg1, arg2, arg3, arg4);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(243, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
  }

  public void deleteSubjview(SubjviewVO arg0) throws BusinessException {
    beforeCallMethod(244);
    Exception er = null;
    try {
      _getBeanObject().deleteSubjview(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(244, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
  }

  public String insertSubjview(SubjviewVO arg0) throws BusinessException {
    beforeCallMethod(245);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().insertSubjview(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(245, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void updateSubjview(SubjviewVO arg0) throws BusinessException {
    beforeCallMethod(246);
    Exception er = null;
    try {
      _getBeanObject().updateSubjview(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(246, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
  }

  public SubjviewVO[] querySubjview(String arg0, int arg1) throws BusinessException {
    beforeCallMethod(247);
    Exception er = null;
    SubjviewVO[] o = null;
    try {
      o = (SubjviewVO[])_getBeanObject().querySubjview(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(247, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void updateMovedSubjviewVOs(InsubjclassVO arg0, SubjviewVO[] arg1) throws BusinessException {
    beforeCallMethod(248);
    Exception er = null;
    try {
      _getBeanObject().updateMovedSubjviewVOs(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(248, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
  }

  public GlOrgVO[] queryGlorgs(String arg0) throws BusinessException {
    beforeCallMethod(249);
    Exception er = null;
    GlOrgVO[] o = null;
    try {
      o = (GlOrgVO[])_getBeanObject().queryGlorgs(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(249, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public GlOrgVO[] queryGlorgsByUser(String arg0) throws BusinessException {
    beforeCallMethod(250);
    Exception er = null;
    GlOrgVO[] o = null;
    try {
      o = (GlOrgVO[])_getBeanObject().queryGlorgsByUser(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(250, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public GlOrgVO queryGlorgByPk(String arg0) throws BusinessException {
    beforeCallMethod(251);
    Exception er = null;
    GlOrgVO o = null;
    try {
      o = _getBeanObject().queryGlorgByPk(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(251, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public GlBookVO[] queryGlbooksByOrg(String arg0) throws BusinessException {
    beforeCallMethod(252);
    Exception er = null;
    GlBookVO[] o = null;
    try {
      o = (GlBookVO[])_getBeanObject().queryGlbooksByOrg(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(252, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public GlBookVO queryGlbookByPk(String arg0) throws BusinessException {
    beforeCallMethod(253);
    Exception er = null;
    GlBookVO o = null;
    try {
      o = _getBeanObject().queryGlbookByPk(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(253, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public RetVoucherVO offsetRed(String arg0, String arg1, String arg2, String arg3, UFDate arg4) throws BusinessException {
    beforeCallMethod(254);
    Exception er = null;
    RetVoucherVO o = null;
    try {
      o = _getBeanObject().offsetRed(arg0, arg1, arg2, arg3, arg4);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(254, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void deleteMergeScheme(MergeschemeVO arg0) throws BusinessException {
    beforeCallMethod(255);
    Exception er = null;
    try {
      _getBeanObject().deleteMergeScheme(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(255, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
  }

  public AssistantAccountingVO[] getAssistantAccounting() throws BusinessException {
    beforeCallMethod(256);
    Exception er = null;
    AssistantAccountingVO[] o = null;
    try {
      o = (AssistantAccountingVO[])_getBeanObject().getAssistantAccounting();
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(256, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public DapFinMsgVO[] getBillListItems(DapFinMsgVO[] arg0, QueryLogVO arg1, String arg2, int arg3, String arg4) throws BusinessException {
    beforeCallMethod(257);
    Exception er = null;
    DapFinMsgVO[] o = null;
    try {
      o = (DapFinMsgVO[])_getBeanObject().getBillListItems(arg0, arg1, arg2, arg3, arg4);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(257, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public BillTypeAndProcessVO[] getBillTypeAndProcess(String arg0, String arg1, String[] arg2, String arg3) throws BusinessException {
    beforeCallMethod(258);
    Exception er = null;
    BillTypeAndProcessVO[] o = null;
    try {
      o = (BillTypeAndProcessVO[])_getBeanObject().getBillTypeAndProcess(arg0, arg1, arg2, arg3);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(258, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public ControlruleVO[] getControlRule(String arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(259);
    Exception er = null;
    ControlruleVO[] o = null;
    try {
      o = (ControlruleVO[])_getBeanObject().getControlRule(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(259, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public String getDefaultPkScheme(String arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(260);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().getDefaultPkScheme(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(260, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public MergeschemeVO[] getMergeScheme(String arg0) throws BusinessException {
    beforeCallMethod(261);
    Exception er = null;
    MergeschemeVO[] o = null;
    try {
      o = (MergeschemeVO[])_getBeanObject().getMergeScheme(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(261, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public MergeschemeVO getMergeSchemeByPrimarykey(String arg0) throws BusinessException {
    beforeCallMethod(262);
    Exception er = null;
    MergeschemeVO o = null;
    try {
      o = _getBeanObject().getMergeSchemeByPrimarykey(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(262, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public nc.vo.dap.inteface.VoucherVO[][] getRtVoucher(String[][] arg0) throws BusinessException {
    beforeCallMethod(263);
    Exception er = null;
    nc.vo.dap.inteface.VoucherVO[][] o = (nc.vo.dap.inteface.VoucherVO[][])null;
    try {
      o = (nc.vo.dap.inteface.VoucherVO[][])_getBeanObject().getRtVoucher(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(263, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public InitVouControlVO getSchemeControl(InitVCParamVO arg0) throws BusinessException {
    beforeCallMethod(264);
    Exception er = null;
    InitVouControlVO o = null;
    try {
      o = _getBeanObject().getSchemeControl(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(264, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public RetVoucherVO[] offsetReds(String[] arg0, String arg1, String arg2, String arg3, UFDate arg4) throws BusinessException {
    beforeCallMethod(265);
    Exception er = null;
    RetVoucherVO[] o = null;
    try {
      o = (RetVoucherVO[])_getBeanObject().offsetReds(arg0, arg1, arg2, arg3, arg4);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(265, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public MergeschemeVO queryDefaultScheme(String arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(266);
    Exception er = null;
    MergeschemeVO o = null;
    try {
      o = _getBeanObject().queryDefaultScheme(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(266, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void removeRtVouch(String[][] arg0, int[] arg1) throws BusinessException {
    beforeCallMethod(267);
    Exception er = null;
    try {
      _getBeanObject().removeRtVouch(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(267, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
  }

  public String saveAsMergeScheme(MergeschemeVO arg0) throws BusinessException {
    beforeCallMethod(268);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().saveAsMergeScheme(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(268, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public String[] saveControlRule(ControlruleVO[] arg0) throws BusinessException {
    beforeCallMethod(269);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().saveControlRule(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(269, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void saveDefaultPkScheme(String arg0, String arg1, String arg2, String arg3, String arg4) throws BusinessException {
    beforeCallMethod(270);
    Exception er = null;
    try {
      _getBeanObject().saveDefaultPkScheme(arg0, arg1, arg2, arg3, arg4);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(270, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
  }

  public void saveMergeScheme(MergeschemeVO[] arg0) throws BusinessException {
    beforeCallMethod(271);
    Exception er = null;
    try {
      _getBeanObject().saveMergeScheme(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(271, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
  }

  public void transportVoucher(nc.vo.gl.pubvoucher.VoucherVO[] arg0, String[] arg1) throws BusinessException {
    beforeCallMethod(272);
    Exception er = null;
    try {
      _getBeanObject().transportVoucher(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(272, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
  }

  public Object create(String arg0, String arg1, String arg2, String arg3) throws BusinessException {
    beforeCallMethod(273);
    Exception er = null;
    Object o = null;
    try {
      o = _getBeanObject().create(arg0, arg1, arg2, arg3);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(273, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public nc.bs.gl.pubinterface.IVoucherAudit getGL_IVoucherAudit(String arg0) throws BusinessException {
    beforeCallMethod(274);
    Exception er = null;
    nc.bs.gl.pubinterface.IVoucherAudit o = null;
    try {
      o = _getBeanObject().getGL_IVoucherAudit(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(274, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public nc.bs.gl.pubinterface.IVoucherDelete getGL_IVoucherDelete(String arg0) throws BusinessException {
    beforeCallMethod(275);
    Exception er = null;
    nc.bs.gl.pubinterface.IVoucherDelete o = null;
    try {
      o = _getBeanObject().getGL_IVoucherDelete(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(275, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public nc.bs.gl.pubinterface.IVoucherSave getGL_IVoucherSave(String arg0) throws BusinessException {
    beforeCallMethod(276);
    Exception er = null;
    nc.bs.gl.pubinterface.IVoucherSave o = null;
    try {
      o = _getBeanObject().getGL_IVoucherSave(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(276, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public IBillQueryVoucher getIBillQueryVoucher(String arg0) throws BusinessException {
    beforeCallMethod(277);
    Exception er = null;
    IBillQueryVoucher o = null;
    try {
      o = _getBeanObject().getIBillQueryVoucher(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(277, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public IVirtualVoucher getIVirtualVoucher(String arg0) throws BusinessException {
    beforeCallMethod(278);
    Exception er = null;
    IVirtualVoucher o = null;
    try {
      o = _getBeanObject().getIVirtualVoucher(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(278, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public nc.itf.dap.pub.IVoucherAudit getVoucherAuditBO(String arg0, int arg1) throws BusinessException {
    beforeCallMethod(279);
    Exception er = null;
    nc.itf.dap.pub.IVoucherAudit o = null;
    try {
      o = _getBeanObject().getVoucherAuditBO(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(279, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public nc.itf.dap.pub.IVoucherDelete getVoucherDeleteBO(String arg0, int arg1) throws BusinessException {
    beforeCallMethod(280);
    Exception er = null;
    nc.itf.dap.pub.IVoucherDelete o = null;
    try {
      o = _getBeanObject().getVoucherDeleteBO(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(280, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public IVoucherQuery getVoucherQueryBO(String arg0, int arg1) throws BusinessException {
    beforeCallMethod(281);
    Exception er = null;
    IVoucherQuery o = null;
    try {
      o = _getBeanObject().getVoucherQueryBO(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(281, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public nc.itf.dap.pub.IVoucherSave getVoucherSaveBO(String arg0, int arg1) throws BusinessException {
    beforeCallMethod(282);
    Exception er = null;
    nc.itf.dap.pub.IVoucherSave o = null;
    try {
      o = _getBeanObject().getVoucherSaveBO(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(282, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public BusiTransVO[] createBatch(String arg0, String arg1, String arg2, String arg3) throws BusinessException {
    beforeCallMethod(283);
    Exception er = null;
    BusiTransVO[] o = null;
    try {
      o = (BusiTransVO[])_getBeanObject().createBatch(arg0, arg1, arg2, arg3);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(283, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public IufoModuleVO[] queryAll(String arg0) throws DapBusinessException {
    beforeCallMethod(284);
    Exception er = null;
    IufoModuleVO[] o = null;
    try {
      o = (IufoModuleVO[])_getBeanObject().queryAll(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(284, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof DapBusinessException)) {
        throw ((DapBusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void deleteDapsystem(DapsystemVO arg0) throws BusinessException {
    beforeCallMethod(285);
    Exception er = null;
    try {
      _getBeanObject().deleteDapsystem(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(285, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
  }

  public String insertDapsystem(DapsystemVO arg0) throws BusinessException {
    beforeCallMethod(286);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().insertDapsystem(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(286, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void updateDapsystem(DapsystemVO arg0) throws BusinessException {
    beforeCallMethod(287);
    Exception er = null;
    try {
      _getBeanObject().updateDapsystem(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(287, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
  }

  public DapsystemVO findDapsystemByPrimaryKey(String arg0) throws BusinessException {
    beforeCallMethod(288);
    Exception er = null;
    DapsystemVO o = null;
    try {
      o = _getBeanObject().findDapsystemByPrimaryKey(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(288, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public String[] insertDapsystemArray(DapsystemVO[] arg0) throws BusinessException {
    beforeCallMethod(289);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().insertDapsystemArray(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(289, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public DapsystemVO[] queryAllDapsystem(String arg0) throws BusinessException {
    beforeCallMethod(290);
    Exception er = null;
    DapsystemVO[] o = null;
    try {
      o = (DapsystemVO[])_getBeanObject().queryAllDapsystem(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(290, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public DapsystemVO[] queryAllDapsystemByPower(DataPowerStruVO arg0) throws BusinessException {
    beforeCallMethod(291);
    Exception er = null;
    DapsystemVO[] o = null;
    try {
      o = (DapsystemVO[])_getBeanObject().queryAllDapsystemByPower(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(291, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public DapsystemVO[] queryDapsystemByVO(DapsystemVO arg0, Boolean arg1) throws BusinessException {
    beforeCallMethod(292);
    Exception er = null;
    DapsystemVO[] o = null;
    try {
      o = (DapsystemVO[])_getBeanObject().queryDapsystemByVO(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(292, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public UFBoolean isHasRtVouchByCorpAndYear(String arg0, String arg1, String arg2, String arg3) throws BusinessException {
    beforeCallMethod(293);
    Exception er = null;
    UFBoolean o = null;
    try {
      o = _getBeanObject().isHasRtVouchByCorpAndYear(arg0, arg1, arg2, arg3);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(293, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void UpgradeNewBilltype(String[] arg0) throws BusinessException {
    beforeCallMethod(294);
    Exception er = null;
    try {
      _getBeanObject().UpgradeNewBilltype(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(294, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
  }

  public void deleteVouchtemp(VouchtempVO arg0) throws BusinessException {
    beforeCallMethod(295);
    Exception er = null;
    try {
      _getBeanObject().deleteVouchtemp(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(295, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
  }

  public String insertVouchtemp(VouchtempVO arg0) throws BusinessException {
    beforeCallMethod(296);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().insertVouchtemp(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(296, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void updateVouchtemp(VouchtempVO arg0) throws BusinessException {
    beforeCallMethod(297);
    Exception er = null;
    try {
      _getBeanObject().updateVouchtemp(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(297, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
  }

  public VouchtempVO findVouchtempByPrimaryKey(String arg0) throws BusinessException {
    beforeCallMethod(298);
    Exception er = null;
    VouchtempVO o = null;
    try {
      o = _getBeanObject().findVouchtempByPrimaryKey(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(298, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public String[] insertVouchtempArray(VouchtempVO[] arg0) throws BusinessException {
    beforeCallMethod(299);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().insertVouchtempArray(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(299, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public VouchtempVO[] queryAllVouchtemp(String arg0) throws BusinessException {
    beforeCallMethod(300);
    Exception er = null;
    VouchtempVO[] o = null;
    try {
      o = (VouchtempVO[])_getBeanObject().queryAllVouchtemp(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(300, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public VouchtempVO[] queryVouchtempByVO(VouchtempVO arg0, Boolean arg1) throws BusinessException {
    beforeCallMethod(301);
    Exception er = null;
    VouchtempVO[] o = null;
    try {
      o = (VouchtempVO[])_getBeanObject().queryVouchtempByVO(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(301, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public VoucherTemplateVO queryTempVO(VouchtempVO arg0) throws BusinessException {
    beforeCallMethod(302);
    Exception er = null;
    VoucherTemplateVO o = null;
    try {
      o = _getBeanObject().queryTempVO(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(302, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void copyTempVO(VouchtempVO arg0, VouchtempVO[] arg1, Boolean arg2) throws BusinessException {
    beforeCallMethod(303);
    Exception er = null;
    try {
      _getBeanObject().copyTempVO(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(303, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
  }

  public void copyVouchTemptoCorpsByProc(String arg0, String arg1, String[] arg2, GlorgbookVO[] arg3, int arg4) throws BusinessException {
    beforeCallMethod(304);
    Exception er = null;
    try {
      _getBeanObject().copyVouchTemptoCorpsByProc(arg0, arg1, arg2, arg3, arg4);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(304, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
  }

  public void deleteTempVO(VoucherTemplateVO arg0) throws BusinessException {
    beforeCallMethod(305);
    Exception er = null;
    try {
      _getBeanObject().deleteTempVO(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(305, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
  }

  public DefitemVO[] queryBillitemByBilltype(String arg0, Integer arg1) throws BusinessException {
    beforeCallMethod(306);
    Exception er = null;
    DefitemVO[] o = null;
    try {
      o = (DefitemVO[])_getBeanObject().queryBillitemByBilltype(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(306, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public Vector queryCpyBillType(VouchtempVO arg0) throws BusinessException {
    beforeCallMethod(307);
    Exception er = null;
    Vector o = null;
    try {
      o = _getBeanObject().queryCpyBillType(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(307, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public Vector queryCpyBusiType(VouchtempVO arg0, Integer arg1, int arg2, String arg3, String arg4) throws BusinessException {
    beforeCallMethod(308);
    Exception er = null;
    Vector o = null;
    try {
      o = _getBeanObject().queryCpyBusiType(arg0, arg1, arg2, arg3, arg4);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(308, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public VoucherTemplateVO queryTempVOByCondVO(VouchtempVO arg0) throws BusinessException {
    beforeCallMethod(309);
    Exception er = null;
    VoucherTemplateVO o = null;
    try {
      o = _getBeanObject().queryTempVOByCondVO(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(309, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public ReturnPKVO saveTempVO(VoucherTemplateVO arg0, Vector arg1, Vector arg2) throws BusinessException {
    beforeCallMethod(310);
    Exception er = null;
    ReturnPKVO o = null;
    try {
      o = _getBeanObject().saveTempVO(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(310, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void procCalculate(DapFinMsgVO[] arg0) throws BusinessException {
    beforeCallMethod(311);
    Exception er = null;
    try {
      _getBeanObject().procCalculate(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(311, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
  }

  public void saveAllSubjclass(InsubjclassVO[] arg0, String arg1, String arg2, boolean arg3) throws BusinessException {
    beforeCallMethod(312);
    Exception er = null;
    try {
      _getBeanObject().saveAllSubjclass(arg0, arg1, arg2, arg3);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(312, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
  }

  public BillQueryVoucherVO[] queryVouchers(BillQueryVoucherVO[] arg0) throws BusinessException {
    beforeCallMethod(313);
    Exception er = null;
    BillQueryVoucherVO[] o = null;
    try {
      o = (BillQueryVoucherVO[])_getBeanObject().queryVouchers(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(313, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void indexFileHandler(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(314);
    Exception er = null;
    try {
      _getBeanObject().indexFileHandler(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(314, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
  }

  public void indexHandler(String arg0, String[] arg1) throws BusinessException {
    beforeCallMethod(315);
    Exception er = null;
    try {
      _getBeanObject().indexHandler(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(315, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
  }

  public void cancelVoucher(DapFinMsgVO[] arg0) throws BusinessException {
    beforeCallMethod(316);
    Exception er = null;
    try {
      _getBeanObject().cancelVoucher(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(316, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
  }

  public Hashtable getIsQuantity(DetailVO[] arg0) throws BusinessException {
    beforeCallMethod(317);
    Exception er = null;
    Hashtable o = null;
    try {
      o = _getBeanObject().getIsQuantity(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(317, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void procMessageQueue(DapFinMsgVO[] arg0, UFBoolean arg1) throws BusinessException {
    beforeCallMethod(318);
    Exception er = null;
    try {
      _getBeanObject().procMessageQueue(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(318, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
  }

  public void procMessageQueueReflect(DapFinMsgVO[] arg0, boolean arg1) throws BusinessException {
    beforeCallMethod(319);
    Exception er = null;
    try {
      _getBeanObject().procMessageQueueReflect(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(319, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
  }

  public void procReCalculate(DapFinMsgVO[] arg0) throws BusinessException {
    beforeCallMethod(320);
    Exception er = null;
    try {
      _getBeanObject().procReCalculate(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(320, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
  }

  public void procOneMsg_RequiresNew(DapFinMsgVO arg0, AggregatedValueObject arg1) throws BusinessException {
    beforeCallMethod(321);
    Exception er = null;
    try {
      _getBeanObject().procOneMsg_RequiresNew(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(321, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
  }

  public nc.vo.dap.inteface.VoucherVO createRtVouch_RequiresNew(DapFinMsgVO arg0, AggregatedValueObject arg1, boolean arg2, boolean arg3) throws BusinessException, RtVouchException {
    beforeCallMethod(322);
    Exception er = null;
    nc.vo.dap.inteface.VoucherVO o = null;
    try {
      o = _getBeanObject().createRtVouch_RequiresNew(arg0, arg1, arg2, arg3);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(322, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }
      if ((er instanceof RtVouchException)) {
        throw ((RtVouchException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public OperationResultVO saveVoucher_RequiresNew(nc.vo.dap.inteface.VoucherVO arg0, DapFinMsgVO arg1) throws BusinessException, RtVouchException {
    beforeCallMethod(323);
    Exception er = null;
    OperationResultVO o = null;
    try {
      o = _getBeanObject().saveVoucher_RequiresNew(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(323, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }
      if ((er instanceof RtVouchException)) {
        throw ((RtVouchException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void writeErrMsg_RequiresNew(String arg0, String arg1, boolean arg2) throws BusinessException {
    beforeCallMethod(324);
    Exception er = null;
    try {
      _getBeanObject().writeErrMsg_RequiresNew(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(324, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
  }

  public DapMsgVO addMessageQueue_RequiresNew(DapMsgVO arg0, String arg1) throws BusinessException {
    beforeCallMethod(325);
    Exception er = null;
    DapMsgVO o = null;
    try {
      o = _getBeanObject().addMessageQueue_RequiresNew(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(325, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void saveRtVouchAfterException_RequiresNew(nc.vo.dap.inteface.VoucherVO arg0, String arg1, String arg2, int arg3, DapFinMsgVO arg4) throws BusinessException {
    beforeCallMethod(326);
    Exception er = null;
    try {
      _getBeanObject().saveRtVouchAfterException_RequiresNew(arg0, arg1, arg2, arg3, arg4);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(326, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
  }

  public void delReflect_RequiresNew(String arg0) throws BusinessException {
    beforeCallMethod(327);
    Exception er = null;
    try {
      _getBeanObject().delReflect_RequiresNew(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(327, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
  }

  public void updateExecType_RequiresNew(String arg0) throws BusinessException {
    beforeCallMethod(328);
    Exception er = null;
    try {
      _getBeanObject().updateExecType_RequiresNew(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(328, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
  }

  public RetVoucherVO procAddMessage_RequiresNew(DapExecTypeVO arg0, DapMsgVO arg1, DapFinMsgVO arg2, AggregatedValueObject arg3, boolean arg4, boolean arg5) throws BusinessException {
    beforeCallMethod(329);
    Exception er = null;
    RetVoucherVO o = null;
    try {
      o = _getBeanObject().procAddMessage_RequiresNew(arg0, arg1, arg2, arg3, arg4, arg5);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(329, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public DapMsgVO[] procReCreateMsgs(DapFinMsgVO[] arg0) throws BusinessException {
    beforeCallMethod(330);
    Exception er = null;
    DapMsgVO[] o = null;
    try {
      o = (DapMsgVO[])_getBeanObject().procReCreateMsgs(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(330, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public RetVoucherVO offsetRed_RequiresNew(DapMsgVO arg0) throws BusinessException {
    beforeCallMethod(331);
    Exception er = null;
    RetVoucherVO o = null;
    try {
      o = _getBeanObject().offsetRed_RequiresNew(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(331, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void delMessage_RequiresNew(DapMsgVO arg0) throws BusinessException {
    beforeCallMethod(332);
    Exception er = null;
    try {
      _getBeanObject().delMessage_RequiresNew(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(332, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
  }

  public nc.vo.dap.inteface.VoucherVO[][] getRtVouchAry(String[][] arg0) throws BusinessException {
    beforeCallMethod(333);
    Exception er = null;
    nc.vo.dap.inteface.VoucherVO[][] o = (nc.vo.dap.inteface.VoucherVO[][])null;
    try {
      o = (nc.vo.dap.inteface.VoucherVO[][])_getBeanObject().getRtVouchAry(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(333, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public boolean isEditBillType_RequiresNew(String arg0, String arg1, String arg2, String arg3, String arg4) throws BusinessException {
    beforeCallMethod(334);
    Exception er = null;
    boolean o = false;
    try {
      o = _getBeanObject().isEditBillType_RequiresNew(arg0, arg1, arg2, arg3, arg4);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(334, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public BillQueryVoucherVO[] queryAllVouchers(BillQueryVoucherVO[] arg0) throws BusinessException {
    beforeCallMethod(335);
    Exception er = null;
    BillQueryVoucherVO[] o = null;
    try {
      o = (BillQueryVoucherVO[])_getBeanObject().queryAllVouchers(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(335, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public RetBillVo[] getPeriodNotCompleteBill(String arg0, String arg1, String arg2, int arg3) throws BusinessException {
    beforeCallMethod(336);
    Exception er = null;
    RetBillVo[] o = null;
    try {
      o = (RetBillVo[])_getBeanObject().getPeriodNotCompleteBill(arg0, arg1, arg2, arg3);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(336, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public UFBoolean isHasRtVoucherByPeriod(String arg0, String arg1, String arg2, String arg3) throws BusinessException {
    beforeCallMethod(337);
    Exception er = null;
    UFBoolean o = null;
    try {
      o = _getBeanObject().isHasRtVoucherByPeriod(arg0, arg1, arg2, arg3);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(337, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public DapFactorVO[] getFactorArray(DapMsgVO arg0) throws BusinessException {
    beforeCallMethod(338);
    Exception er = null;
    DapFactorVO[] o = null;
    try {
      o = (DapFactorVO[])_getBeanObject().getFactorArray(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(338, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public DapFactorVO[] getBookRuleFactorArray(DapMsgVO arg0) throws BusinessException {
    beforeCallMethod(339);
    Exception er = null;
    DapFactorVO[] o = null;
    try {
      o = (DapFactorVO[])_getBeanObject().getBookRuleFactorArray(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(339, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public VoucherTemplateVO[] getVouchtempArray(DapMsgVO[] arg0) throws BusinessException {
    beforeCallMethod(340);
    Exception er = null;
    VoucherTemplateVO[] o = null;
    try {
      o = (VoucherTemplateVO[])_getBeanObject().getVouchtempArray(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(340, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public DapMsgVO[] queryNotFinishMessage(DapMsgVO arg0) throws BusinessException {
    beforeCallMethod(341);
    Exception er = null;
    DapMsgVO[] o = null;
    try {
      o = (DapMsgVO[])_getBeanObject().queryNotFinishMessage(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(341, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void deleteBillfactor(BillfactorVO arg0) throws BusinessException {
    beforeCallMethod(342);
    Exception er = null;
    try {
      _getBeanObject().deleteBillfactor(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(342, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
  }

  public String insertBillfactor(BillfactorVO arg0) throws BusinessException {
    beforeCallMethod(343);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().insertBillfactor(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(343, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void updateBillfactor(BillfactorVO arg0) throws BusinessException {
    beforeCallMethod(344);
    Exception er = null;
    try {
      _getBeanObject().updateBillfactor(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(344, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
  }

  public String[] insertBillfactorArray(BillfactorVO[] arg0) throws BusinessException {
    beforeCallMethod(345);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().insertBillfactorArray(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(345, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public BillfactorVO[] queryAllBillfactor(String arg0) throws BusinessException {
    beforeCallMethod(346);
    Exception er = null;
    BillfactorVO[] o = null;
    try {
      o = (BillfactorVO[])_getBeanObject().queryAllBillfactor(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(346, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public BillfactorVO[] queryBillfactorByVO(BillfactorVO arg0, Boolean arg1) throws BusinessException {
    beforeCallMethod(347);
    Exception er = null;
    BillfactorVO[] o = null;
    try {
      o = (BillfactorVO[])_getBeanObject().queryBillfactorByVO(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(347, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public BillfactorVO[] findBillfactorByBtcode(String arg0) throws BusinessException {
    beforeCallMethod(348);
    Exception er = null;
    BillfactorVO[] o = null;
    try {
      o = (BillfactorVO[])_getBeanObject().findBillfactorByBtcode(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(348, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
}