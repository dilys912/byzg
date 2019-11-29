package nc.itf.fi.ejb;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import nc.bs.arap.sql.SqlCreatorTools;
import nc.bs.framework.exception.FrameworkEJBException;
import nc.bs.logging.Logger;
import nc.bs.mw.naming.BeanBase;
import nc.vo.arap.bdcontrastinfo.BdcontrastinfoVO;
import nc.vo.arap.djlx.BillTypeVO;
import nc.vo.arap.djlx.DjLXVO;
import nc.vo.arap.func.FuncResultVO;
import nc.vo.arap.func.Ntb_Id_Bdcontrast;
import nc.vo.arap.func.QueryVO;
import nc.vo.arap.global.ResMessage;
import nc.vo.arap.gyl.AdjuestVO;
import nc.vo.arap.gyl.VerifyParamVO;
import nc.vo.arap.inter.ContractQueryVO;
import nc.vo.arap.inter.CouReVO;
import nc.vo.arap.pub.QryObjectVO;
import nc.vo.arap.pub.QueryStructVO;
import nc.vo.arap.pub.StatValueObject;
import nc.vo.arap.uforeport2.IFuncSubVO;
import nc.vo.bd.access.BddataVO;
import nc.vo.ep.dj.DJZBHeaderVO;
import nc.vo.ep.dj.DJZBVO;
import nc.vo.ep.dj.IFTSReceiverVO;
import nc.vo.fa.outer.IExpenseVO;
import nc.vo.fi.uforeport.ResultVO;
import nc.vo.gl.contrastpub.CConditionVO;
import nc.vo.glpub.IVoAccess;
import nc.vo.ntb.outer.NtbParamVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.sourcebill.LightBillVO;
import nc.vo.so.service.SaleDailyConditionVO;
import nc.vo.so.service.SaleDailyReportVO;
import nc.vo.so.so016.SoBalanceToArVO;

public class FIArapPubServiceEJB_Local extends BeanBase
  implements FIArapPubServiceEJBEjbObject
{
  private FIArapPubServiceEJBEjbBean _getBeanObject()
    throws RemoteException
  {
    return (FIArapPubServiceEJBEjbBean)getEjb();
  }

  public void setOtherSysFlag(String arg0, String arg1, UFBoolean arg2) throws BusinessException {
    beforeCallMethod(200);
    Exception er = null;
    try {
      _getBeanObject().setOtherSysFlag(arg0, arg1, arg2);
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

  public ResMessage accountArapBillContr(DJZBVO arg0) throws BusinessException {
    beforeCallMethod(201);
    Exception er = null;
    ResMessage o = null;
    try {
      o = _getBeanObject().accountArapBillContr(arg0);
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
    return o;
  }
  public ResMessage auditArapBill(DJZBVO arg0) throws BusinessException {
    beforeCallMethod(202);
    Exception er = null;
    ResMessage o = null;
    try {
      o = _getBeanObject().auditArapBill(arg0);
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
    return o;
  }
  public LightBillVO[] getForwardBills(String arg0, String arg1, String arg2) {
    beforeCallMethod(203);
    Exception er = null;
    LightBillVO[] o = null;
    try {
      o = (LightBillVO[])_getBeanObject().getForwardBills(arg0, arg1, arg2);
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
    if (null != er)
    {
      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void sendArapBillMsg(DJZBVO arg0) throws BusinessException {
    beforeCallMethod(204);
    Exception er = null;
    try {
      _getBeanObject().sendArapBillMsg(arg0);
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

  public void sendArapBillDelMsg(DJZBVO arg0) throws BusinessException {
    beforeCallMethod(205);
    Exception er = null;
    try {
      _getBeanObject().sendArapBillDelMsg(arg0);
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
  }

  public ResMessage unAuditArapBill(DJZBVO arg0) throws BusinessException {
    beforeCallMethod(206);
    Exception er = null;
    ResMessage o = null;
    try {
      o = _getBeanObject().unAuditArapBill(arg0);
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
  public ResMessage unYhqrArapBill(DJZBVO arg0) throws BusinessException {
    beforeCallMethod(207);
    Exception er = null;
    ResMessage o = null;
    try {
      o = _getBeanObject().unYhqrArapBill(arg0);
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
  public ResMessage yhqrArapBill(DJZBVO arg0) throws BusinessException {
    beforeCallMethod(208);
    Exception er = null;
    ResMessage o = null;
    try {
      o = _getBeanObject().yhqrArapBill(arg0);
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
  public ResMessage isArapBillDistributes(DJZBVO arg0) throws BusinessException {
    beforeCallMethod(209);
    Exception er = null;
    ResMessage o = null;
    try {
      o = _getBeanObject().isArapBillDistributes(arg0);
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
  public boolean isSysInuse(String arg0, String arg1, UFDate arg2) throws BusinessException {
    beforeCallMethod(210);
    Exception er = null;
    boolean o = false;
    try {
      o = _getBeanObject().isSysInuse(arg0, arg1, arg2);
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
  public void backGoing(AggregatedValueObject arg0, String arg1, String arg2, String arg3) throws BusinessException {
    beforeCallMethod(211);
    Exception er = null;
    try {
      _getBeanObject().backGoing(arg0, arg1, arg2, arg3);
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
  }

  public void backNoState(AggregatedValueObject arg0, String arg1, String arg2, String arg3) throws BusinessException {
    beforeCallMethod(212);
    Exception er = null;
    try {
      _getBeanObject().backNoState(arg0, arg1, arg2, arg3);
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

  public boolean checkGoing(AggregatedValueObject arg0, String arg1, String arg2, String arg3) throws BusinessException {
    beforeCallMethod(213);
    Exception er = null;
    boolean o = false;
    try {
      o = _getBeanObject().checkGoing(arg0, arg1, arg2, arg3);
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
  public boolean checkNoPass(AggregatedValueObject arg0, String arg1, String arg2, String arg3) throws BusinessException {
    beforeCallMethod(214);
    Exception er = null;
    boolean o = false;
    try {
      o = _getBeanObject().checkNoPass(arg0, arg1, arg2, arg3);
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
    return o;
  }
  public boolean checkPass(AggregatedValueObject arg0, String arg1, String arg2, String arg3) throws BusinessException {
    beforeCallMethod(215);
    Exception er = null;
    boolean o = false;
    try {
      o = _getBeanObject().checkPass(arg0, arg1, arg2, arg3);
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
  public void changeARAPBillStatus(String arg0) throws BusinessException {
    beforeCallMethod(216);
    Exception er = null;
    try {
      _getBeanObject().changeARAPBillStatus(arg0);
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
  }

  public void changeARAPBillStatusUnVerify(String arg0, UFDate arg1, String arg2) throws BusinessException {
    beforeCallMethod(217);
    Exception er = null;
    try {
      _getBeanObject().changeARAPBillStatusUnVerify(arg0, arg1, arg2);
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
  }

  public void changeARAPBillStatusVerify(String arg0, UFDate arg1, String arg2, Boolean arg3) throws BusinessException {
    beforeCallMethod(218);
    Exception er = null;
    try {
      _getBeanObject().changeARAPBillStatusVerify(arg0, arg1, arg2, arg3);
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

  public ResMessage auditOneArapBill(DJZBVO arg0) throws BusinessException {
    beforeCallMethod(219);
    Exception er = null;
    ResMessage o = null;
    try {
      o = _getBeanObject().auditOneArapBill(arg0);
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
    return o;
  }
  public ResMessage unAuditOneArapBill(DJZBVO arg0) throws BusinessException {
    beforeCallMethod(220);
    Exception er = null;
    ResMessage o = null;
    try {
      o = _getBeanObject().unAuditOneArapBill(arg0);
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
  public DJZBVO deleteArapBill(DJZBVO arg0) throws BusinessException {
    beforeCallMethod(221);
    Exception er = null;
    DJZBVO o = null;
    try {
      o = _getBeanObject().deleteArapBill(arg0);
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
  public void deleteOutArapBillByPk(String arg0) throws BusinessException {
    beforeCallMethod(222);
    Exception er = null;
    try {
      _getBeanObject().deleteOutArapBillByPk(arg0);
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
  }

  public void deleteOutArapBillByPks(String[] arg0) throws BusinessException {
    beforeCallMethod(223);
    Exception er = null;
    try {
      _getBeanObject().deleteOutArapBillByPks(arg0);
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

  public DJZBHeaderVO[] queryHead(String arg0) throws BusinessException {
    beforeCallMethod(224);
    Exception er = null;
    DJZBHeaderVO[] o = null;
    try {
      o = (DJZBHeaderVO[])_getBeanObject().queryHead(arg0);
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
  public List saveArapBillForSettle(DJZBVO[] arg0) throws BusinessException {
    beforeCallMethod(225);
    Exception er = null;
    List o = null;
    try {
      o = _getBeanObject().saveArapBillForSettle(arg0);
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
  public DJZBVO[] findItemByHead(DJZBHeaderVO[] arg0) throws BusinessException {
    beforeCallMethod(226);
    Exception er = null;
    DJZBVO[] o = null;
    try {
      o = (DJZBVO[])_getBeanObject().findItemByHead(arg0);
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
    return o;
  }
  public void deleteOutBillandfb(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(227);
    Exception er = null;
    try {
      _getBeanObject().deleteOutBillandfb(arg0, arg1);
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
  }

  public void deleteOutBillbyWhere(String arg0) throws BusinessException {
    beforeCallMethod(228);
    Exception er = null;
    try {
      _getBeanObject().deleteOutBillbyWhere(arg0);
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

  public DJZBVO editArapBill(DJZBVO arg0) throws BusinessException {
    beforeCallMethod(229);
    Exception er = null;
    DJZBVO o = null;
    try {
      o = _getBeanObject().editArapBill(arg0);
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
    return o;
  }
  public DJZBVO[] getDjvobySourceHh(String arg0) throws Exception {
    beforeCallMethod(230);
    Exception er = null;
    DJZBVO[] o = null;
    try {
      o = (DJZBVO[])_getBeanObject().getDjvobySourceHh(arg0);
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
      if ((er instanceof Exception)) {
        throw er;
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public DJZBVO[] getDjvobySourcePk(String arg0) throws Exception {
    beforeCallMethod(231);
    Exception er = null;
    DJZBVO[] o = null;
    try {
      o = (DJZBVO[])_getBeanObject().getDjvobySourcePk(arg0);
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
      if ((er instanceof Exception)) {
        throw er;
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public DJZBVO findArapBillByPK(String arg0) throws BusinessException {
    beforeCallMethod(232);
    Exception er = null;
    DJZBVO o = null;
    try {
      o = _getBeanObject().findArapBillByPK(arg0);
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
  public DJZBVO[] findArapBillByPKs(String[] arg0) throws BusinessException {
    beforeCallMethod(233);
    Exception er = null;
    DJZBVO[] o = null;
    try {
      o = (DJZBVO[])_getBeanObject().findArapBillByPKs(arg0);
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
    return o;
  }
  public DJZBVO findArapBillByPKSS(String arg0) throws BusinessException {
    beforeCallMethod(234);
    Exception er = null;
    DJZBVO o = null;
    try {
      o = _getBeanObject().findArapBillByPKSS(arg0);
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
  public DJZBVO findArapBillByPKTB(String arg0) throws BusinessException {
    beforeCallMethod(235);
    Exception er = null;
    DJZBVO o = null;
    try {
      o = _getBeanObject().findArapBillByPKTB(arg0);
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
    return o;
  }
  public void returnArapBillCode(DJZBVO arg0) throws BusinessException {
    beforeCallMethod(236);
    Exception er = null;
    try {
      _getBeanObject().returnArapBillCode(arg0);
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
  }

  public DJZBVO saveArapBill(DJZBVO arg0) throws BusinessException {
    beforeCallMethod(237);
    Exception er = null;
    DJZBVO o = null;
    try {
      o = _getBeanObject().saveArapBill(arg0);
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
  public DJZBVO updateArapBill(DJZBVO arg0) throws BusinessException {
    beforeCallMethod(238);
    Exception er = null;
    DJZBVO o = null;
    try {
      o = _getBeanObject().updateArapBill(arg0);
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
  public UFDate getSysNoSettleFirstDate(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(239);
    Exception er = null;
    UFDate o = null;
    try {
      o = _getBeanObject().getSysNoSettleFirstDate(arg0, arg1);
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
  public void updateArapBillByFTSReceiver(IFTSReceiverVO arg0) throws BusinessException {
    beforeCallMethod(240);
    Exception er = null;
    try {
      _getBeanObject().updateArapBillByFTSReceiver(arg0);
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
  }

  public boolean canChangeFTSReceiver(String arg0) throws BusinessException {
    beforeCallMethod(241);
    Exception er = null;
    boolean o = false;
    try {
      o = _getBeanObject().canChangeFTSReceiver(arg0);
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
    return o;
  }
  public String[] getDjpksbySourcePk(String arg0) throws BusinessException {
    beforeCallMethod(242);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().getDjpksbySourcePk(arg0);
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
  public DjLXVO[] queryAllBillTypeByCorp(String arg0) throws BusinessException {
    beforeCallMethod(243);
    Exception er = null;
    DjLXVO[] o = null;
    try {
      o = (DjLXVO[])_getBeanObject().queryAllBillTypeByCorp(arg0);
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
    return o;
  }
  public void deleteBillType(BillTypeVO arg0) throws BusinessException {
    beforeCallMethod(244);
    Exception er = null;
    try {
      _getBeanObject().deleteBillType(arg0);
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

  public BillTypeVO insertBillType(BillTypeVO arg0) throws BusinessException {
    beforeCallMethod(245);
    Exception er = null;
    BillTypeVO o = null;
    try {
      o = _getBeanObject().insertBillType(arg0);
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
  public DjLXVO getDjlxvoByDjlxbm(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(246);
    Exception er = null;
    DjLXVO o = null;
    try {
      o = _getBeanObject().getDjlxvoByDjlxbm(arg0, arg1);
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
    return o;
  }
  public String getYeByZhangLing(String arg0, String arg1, String arg2, String arg3, String arg4, String arg5, int arg6, int arg7) throws BusinessException {
    beforeCallMethod(247);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().getYeByZhangLing(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
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
  public UFDouble[] getYeByZhangQi(String arg0, String arg1, String arg2, int arg3, int arg4) throws BusinessException {
    beforeCallMethod(248);
    Exception er = null;
    UFDouble[] o = null;
    try {
      o = (UFDouble[])_getBeanObject().getYeByZhangQi(arg0, arg1, arg2, arg3, arg4);
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
    return o;
  }
  public void Adjuest(AdjuestVO[] arg0, String arg1, String arg2, String arg3, String arg4, int arg5, int arg6) throws BusinessException {
    beforeCallMethod(249);
    Exception er = null;
    try {
      _getBeanObject().Adjuest(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
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
  }

  public void Adjuest(Hashtable arg0, String arg1, String arg2, String arg3, int arg4, int arg5) throws BusinessException {
    beforeCallMethod(250);
    Exception er = null;
    try {
      _getBeanObject().Adjuest(arg0, arg1, arg2, arg3, arg4, arg5);
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
  }

  public void unAdjuest(String[] arg0, String arg1) throws BusinessException {
    beforeCallMethod(251);
    Exception er = null;
    try {
      _getBeanObject().unAdjuest(arg0, arg1);
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
  }

  public void unAdjuest(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(252);
    Exception er = null;
    try {
      _getBeanObject().unAdjuest(arg0, arg1);
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
  }

  public void saveEff(DJZBVO arg0) throws BusinessException {
    beforeCallMethod(253);
    Exception er = null;
    try {
      _getBeanObject().saveEff(arg0);
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
  }

  public void deleteEff(DJZBVO arg0) throws BusinessException {
    beforeCallMethod(254);
    Exception er = null;
    try {
      _getBeanObject().deleteEff(arg0);
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
  }

  public void verifyForGYL(VerifyParamVO[] arg0, String arg1, String arg2, String arg3, String arg4) throws BusinessException {
    beforeCallMethod(255);
    Exception er = null;
    try {
      _getBeanObject().verifyForGYL(arg0, arg1, arg2, arg3, arg4);
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

  public void unVerifyForGYL(String arg0) throws BusinessException {
    beforeCallMethod(256);
    Exception er = null;
    try {
      _getBeanObject().unVerifyForGYL(arg0);
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
  }

  public UFBoolean canModify(String arg0) throws BusinessException {
    beforeCallMethod(257);
    Exception er = null;
    UFBoolean o = null;
    try {
      o = _getBeanObject().canModify(arg0);
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
  public void saveAdjust(DJZBVO arg0, AdjuestVO[] arg1, String arg2, String arg3, String arg4, String arg5, int arg6, int arg7) throws BusinessException {
    beforeCallMethod(258);
    Exception er = null;
    try {
      _getBeanObject().saveAdjust(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
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
  }

  public void unSaveAdjust(DJZBVO arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(259);
    Exception er = null;
    try {
      _getBeanObject().unSaveAdjust(arg0, arg1, arg2);
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
  }

  public void verifyForGYLBattch(Hashtable arg0, String arg1, String arg2, String arg3) throws BusinessException {
    beforeCallMethod(260);
    Exception er = null;
    try {
      _getBeanObject().verifyForGYLBattch(arg0, arg1, arg2, arg3);
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
  }

  public void unVerifyForGYLBattch(String[] arg0) throws BusinessException {
    beforeCallMethod(261);
    Exception er = null;
    try {
      _getBeanObject().unVerifyForGYLBattch(arg0);
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
  }

  public void deleteEffForCG(DJZBVO arg0) throws BusinessException {
    beforeCallMethod(262);
    Exception er = null;
    try {
      _getBeanObject().deleteEffForCG(arg0);
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
  }

  public void doException(String arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(263);
    Exception er = null;
    try {
      _getBeanObject().doException(arg0, arg1, arg2);
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
  }

  public void saveEffForCG(DJZBVO arg0) throws BusinessException {
    beforeCallMethod(264);
    Exception er = null;
    try {
      _getBeanObject().saveEffForCG(arg0);
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
  }

  public String getSkBalance(String arg0, String arg1, String arg2, int arg3) throws BusinessException {
    beforeCallMethod(265);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().getSkBalance(arg0, arg1, arg2, arg3);
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
  public IExpenseVO[] queryVouchInfoByCardPK(String arg0) throws BusinessException {
    beforeCallMethod(266);
    Exception er = null;
    IExpenseVO[] o = null;
    try {
      o = (IExpenseVO[])_getBeanObject().queryVouchInfoByCardPK(arg0);
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
  public boolean isExistRecorder(String arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(267);
    Exception er = null;
    boolean o = false;
    try {
      o = _getBeanObject().isExistRecorder(arg0, arg1, arg2);
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
    return o;
  }
  public Hashtable getDjlxbmbyBillPks(String arg0, ArrayList arg1, Hashtable arg2) throws BusinessException {
    beforeCallMethod(268);
    Exception er = null;
    Hashtable o = null;
    try {
      o = _getBeanObject().getDjlxbmbyBillPks(arg0, arg1, arg2);
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
  public HashMap getResultHashMap(HashMap[] arg0) throws BusinessException {
    beforeCallMethod(269);
    Exception er = null;
    HashMap o = null;
    try {
      o = _getBeanObject().getResultHashMap(arg0);
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
  public HashMap queryAllCorp() throws BusinessException {
    beforeCallMethod(270);
    Exception er = null;
    HashMap o = null;
    try {
      o = _getBeanObject().queryAllCorp();
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
    return o;
  }
  public Vector queryDept(Vector arg0) throws BusinessException {
    beforeCallMethod(271);
    Exception er = null;
    Vector o = null;
    try {
      o = _getBeanObject().queryDept(arg0);
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
    return o;
  }
  public FuncResultVO[] queryFuncs(NtbParamVO[] arg0, SqlCreatorTools arg1) throws BusinessException {
    beforeCallMethod(272);
    Exception er = null;
    FuncResultVO[] o = null;
    try {
      o = (FuncResultVO[])_getBeanObject().queryFuncs(arg0, arg1);
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
    return o;
  }
  public Ntb_Id_Bdcontrast queryAllArapIdBdcontrastVO() throws BusinessException {
    beforeCallMethod(273);
    Exception er = null;
    Ntb_Id_Bdcontrast o = null;
    try {
      o = _getBeanObject().queryAllArapIdBdcontrastVO();
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
  public Vector queryFuncByQueryVO(QueryVO[] arg0) throws BusinessException {
    beforeCallMethod(274);
    Exception er = null;
    Vector o = null;
    try {
      o = _getBeanObject().queryFuncByQueryVO(arg0);
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
  public Hashtable getNewCoustomBanlance(String[] arg0) throws BusinessException {
    beforeCallMethod(275);
    Exception er = null;
    Hashtable o = null;
    try {
      o = _getBeanObject().getNewCoustomBanlance(arg0);
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
  public Hashtable getNewCoustomBlcExPh(String[] arg0) throws BusinessException {
    beforeCallMethod(276);
    Exception er = null;
    Hashtable o = null;
    try {
      o = _getBeanObject().getNewCoustomBlcExPh(arg0);
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
  public UFDouble getPeriodAR(UFDate arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(277);
    Exception er = null;
    UFDouble o = null;
    try {
      o = _getBeanObject().getPeriodAR(arg0, arg1, arg2);
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
  public Hashtable getArBillNum(String arg0) throws BusinessException {
    beforeCallMethod(278);
    Exception er = null;
    Hashtable o = null;
    try {
      o = _getBeanObject().getArBillNum(arg0);
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
  public SaleDailyReportVO[] getSaleArapData(SaleDailyConditionVO arg0, int[] arg1) throws BusinessException {
    beforeCallMethod(279);
    Exception er = null;
    SaleDailyReportVO[] o = null;
    try {
      o = (SaleDailyReportVO[])_getBeanObject().getSaleArapData(arg0, arg1);
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
  public String[] getPayDateEndVoucher(String arg0, UFDate arg1, UFDate arg2) throws BusinessException {
    beforeCallMethod(280);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().getPayDateEndVoucher(arg0, arg1, arg2);
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
  public boolean isPoCancle(String[] arg0, String arg1) throws BusinessException {
    beforeCallMethod(281);
    Exception er = null;
    boolean o = false;
    try {
      o = _getBeanObject().isPoCancle(arg0, arg1);
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
  public Hashtable getPoApBelence(ContractQueryVO arg0) throws BusinessException {
    beforeCallMethod(282);
    Exception er = null;
    Hashtable o = null;
    try {
      o = _getBeanObject().getPoApBelence(arg0);
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
  public Hashtable getPoContractPr(ContractQueryVO arg0) throws BusinessException {
    beforeCallMethod(283);
    Exception er = null;
    Hashtable o = null;
    try {
      o = _getBeanObject().getPoContractPr(arg0);
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
  public UFDouble getVonderBanlance(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(284);
    Exception er = null;
    UFDouble o = null;
    try {
      o = _getBeanObject().getVonderBanlance(arg0, arg1);
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
  public CouReVO[] getVendorPay(String arg0) throws BusinessException {
    beforeCallMethod(285);
    Exception er = null;
    CouReVO[] o = null;
    try {
      o = (CouReVO[])_getBeanObject().getVendorPay(arg0);
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
    return o;
  }
  public Hashtable getContractPr(ContractQueryVO arg0) throws BusinessException {
    beforeCallMethod(286);
    Exception er = null;
    Hashtable o = null;
    try {
      o = _getBeanObject().getContractPr(arg0);
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
  public HashMap getPaymentBillNoForPu(String arg0) throws BusinessException {
    beforeCallMethod(287);
    Exception er = null;
    HashMap o = null;
    try {
      o = _getBeanObject().getPaymentBillNoForPu(arg0);
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
    return o;
  }
  public UFDouble getPrePayFund(String arg0, String[] arg1) throws BusinessException {
    beforeCallMethod(288);
    Exception er = null;
    UFDouble o = null;
    try {
      o = _getBeanObject().getPrePayFund(arg0, arg1);
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
  public Double getBalance(QueryStructVO arg0, String arg1) throws BusinessException {
    beforeCallMethod(289);
    Exception er = null;
    Double o = null;
    try {
      o = _getBeanObject().getBalance(arg0, arg1);
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
  public Double getAG(QueryStructVO arg0, String arg1) throws BusinessException {
    beforeCallMethod(290);
    Exception er = null;
    Double o = null;
    try {
      o = _getBeanObject().getAG(arg0, arg1);
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
  public Double getAGBalance(QueryStructVO arg0, String arg1) throws BusinessException {
    beforeCallMethod(291);
    Exception er = null;
    Double o = null;
    try {
      o = _getBeanObject().getAGBalance(arg0, arg1);
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
  public Double getAP(QueryStructVO arg0, String arg1) throws BusinessException {
    beforeCallMethod(292);
    Exception er = null;
    Double o = null;
    try {
      o = _getBeanObject().getAP(arg0, arg1);
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
  public Double getAPBalance(QueryStructVO arg0, String arg1) throws BusinessException {
    beforeCallMethod(293);
    Exception er = null;
    Double o = null;
    try {
      o = _getBeanObject().getAPBalance(arg0, arg1);
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
  public Double getAPM(QueryStructVO arg0, String arg1) throws BusinessException {
    beforeCallMethod(294);
    Exception er = null;
    Double o = null;
    try {
      o = _getBeanObject().getAPM(arg0, arg1);
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
    return o;
  }
  public Double getAPMBalance(QueryStructVO arg0, String arg1) throws BusinessException {
    beforeCallMethod(295);
    Exception er = null;
    Double o = null;
    try {
      o = _getBeanObject().getAPMBalance(arg0, arg1);
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
    return o;
  }
  public Integer getApproveAmount(QueryStructVO arg0) throws BusinessException {
    beforeCallMethod(296);
    Exception er = null;
    Integer o = null;
    try {
      o = _getBeanObject().getApproveAmount(arg0);
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
  public Double getApproveSum(QueryStructVO arg0, String arg1) throws BusinessException {
    beforeCallMethod(297);
    Exception er = null;
    Double o = null;
    try {
      o = _getBeanObject().getApproveSum(arg0, arg1);
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
    return o;
  }
  public Double getAR(QueryStructVO arg0, String arg1) throws BusinessException {
    beforeCallMethod(298);
    Exception er = null;
    Double o = null;
    try {
      o = _getBeanObject().getAR(arg0, arg1);
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
  public Double getARBalance(QueryStructVO arg0, String arg1) throws BusinessException {
    beforeCallMethod(299);
    Exception er = null;
    Double o = null;
    try {
      o = _getBeanObject().getARBalance(arg0, arg1);
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
  public Double getECAG(QueryStructVO arg0, String arg1) throws BusinessException {
    beforeCallMethod(300);
    Exception er = null;
    Double o = null;
    try {
      o = _getBeanObject().getECAG(arg0, arg1);
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
  public Double getECAPM(QueryStructVO arg0, String arg1) throws BusinessException {
    beforeCallMethod(301);
    Exception er = null;
    Double o = null;
    try {
      o = _getBeanObject().getECAPM(arg0, arg1);
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
  public Double getECApproveUsedSum(QueryStructVO arg0, String arg1) throws BusinessException {
    beforeCallMethod(302);
    Exception er = null;
    Double o = null;
    try {
      o = _getBeanObject().getECApproveUsedSum(arg0, arg1);
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
  public Double getECBalance(QueryStructVO arg0, String arg1, String arg2, String arg3, boolean arg4) throws BusinessException {
    beforeCallMethod(303);
    Exception er = null;
    Double o = null;
    try {
      o = _getBeanObject().getECBalance(arg0, arg1, arg2, arg3, arg4);
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
    return o;
  }
  public Double getECPayOutSum(QueryStructVO arg0, String arg1) throws BusinessException {
    beforeCallMethod(304);
    Exception er = null;
    Double o = null;
    try {
      o = _getBeanObject().getECPayOutSum(arg0, arg1);
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
    return o;
  }
  public ResultVO[] queryAllFunc(IFuncSubVO arg0) throws BusinessException {
    beforeCallMethod(305);
    Exception er = null;
    ResultVO[] o = null;
    try {
      o = (ResultVO[])_getBeanObject().queryAllFunc(arg0);
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
    return o;
  }
  public HashMap query(String[] arg0) throws BusinessException {
    beforeCallMethod(306);
    Exception er = null;
    HashMap o = null;
    try {
      o = _getBeanObject().query(arg0);
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
  public QryObjectVO[] getQryObj(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(307);
    Exception er = null;
    QryObjectVO[] o = null;
    try {
      o = (QryObjectVO[])_getBeanObject().getQryObj(arg0, arg1);
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
  public StatValueObject[] getBzMessage() throws BusinessException {
    beforeCallMethod(308);
    Exception er = null;
    StatValueObject[] o = null;
    try {
      o = (StatValueObject[])_getBeanObject().getBzMessage();
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
  public String[] getCustomCode(String[] arg0) throws BusinessException {
    beforeCallMethod(309);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().getCustomCode(arg0);
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
  public String[] getDeptCode(String[] arg0) throws BusinessException {
    beforeCallMethod(310);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().getDeptCode(arg0);
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
  public String[] getEmployeeCode(String[] arg0) throws BusinessException {
    beforeCallMethod(311);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().getEmployeeCode(arg0);
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
    return o;
  }
  public int getMaxCurrLength(String[] arg0) throws BusinessException {
    beforeCallMethod(312);
    Exception er = null;
    int o = 0;
    try {
      o = _getBeanObject().getMaxCurrLength(arg0);
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
    return o;
  }
  public Hashtable getMultiCorpPks(String[] arg0, String[] arg1, String arg2) throws BusinessException {
    beforeCallMethod(313);
    Exception er = null;
    Hashtable o = null;
    try {
      o = _getBeanObject().getMultiCorpPks(arg0, arg1, arg2);
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
  public String[] getRangeNames(String arg0) throws BusinessException {
    beforeCallMethod(314);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().getRangeNames(arg0);
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
    return o;
  }
  public BddataVO[] queryZongGongSi(String arg0) throws BusinessException {
    beforeCallMethod(315);
    Exception er = null;
    BddataVO[] o = null;
    try {
      o = (BddataVO[])_getBeanObject().queryZongGongSi(arg0);
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
    return o;
  }
  public HashMap queryBm(String[] arg0, String arg1) throws BusinessException {
    beforeCallMethod(316);
    Exception er = null;
    HashMap o = null;
    try {
      o = _getBeanObject().queryBm(arg0, arg1);
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
    return o;
  }
  public HashMap queryBz(String[] arg0) throws BusinessException {
    beforeCallMethod(317);
    Exception er = null;
    HashMap o = null;
    try {
      o = _getBeanObject().queryBz(arg0);
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
  public HashMap queryCh(String[] arg0, String arg1) throws BusinessException {
    beforeCallMethod(318);
    Exception er = null;
    HashMap o = null;
    try {
      o = _getBeanObject().queryCh(arg0, arg1);
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
    return o;
  }
  public HashMap queryDwbm(String[] arg0) throws BusinessException {
    beforeCallMethod(319);
    Exception er = null;
    HashMap o = null;
    try {
      o = _getBeanObject().queryDwbm(arg0);
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
    return o;
  }
  public HashMap queryKs(String[] arg0, String arg1, boolean arg2) throws BusinessException {
    beforeCallMethod(320);
    Exception er = null;
    HashMap o = null;
    try {
      o = _getBeanObject().queryKs(arg0, arg1, arg2);
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
    return o;
  }
  public HashMap queryShr(String[] arg0, String arg1) throws BusinessException {
    beforeCallMethod(321);
    Exception er = null;
    HashMap o = null;
    try {
      o = _getBeanObject().queryShr(arg0, arg1);
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
    return o;
  }
  public HashMap querySzxm(String[] arg0, String arg1) throws BusinessException {
    beforeCallMethod(322);
    Exception er = null;
    HashMap o = null;
    try {
      o = _getBeanObject().querySzxm(arg0, arg1);
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

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public HashMap queryXm(String[] arg0, String arg1) throws BusinessException {
    beforeCallMethod(323);
    Exception er = null;
    HashMap o = null;
    try {
      o = _getBeanObject().queryXm(arg0, arg1);
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

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public HashMap queryYwy(String[] arg0, String arg1) throws BusinessException {
    beforeCallMethod(324);
    Exception er = null;
    HashMap o = null;
    try {
      o = _getBeanObject().queryYwy(arg0, arg1);
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
    return o;
  }
  public HashMap queryZh(String[] arg0, String arg1) throws BusinessException {
    beforeCallMethod(325);
    Exception er = null;
    HashMap o = null;
    try {
      o = _getBeanObject().queryZh(arg0, arg1);
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
  public HashMap queryZlfa(String[] arg0, String arg1) throws BusinessException {
    beforeCallMethod(326);
    Exception er = null;
    HashMap o = null;
    try {
      o = _getBeanObject().queryZlfa(arg0, arg1);
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
    return o;
  }
  public String[] convertAccount(String[] arg0, int arg1, String arg2) throws BusinessException {
    beforeCallMethod(327);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().convertAccount(arg0, arg1, arg2);
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
    return o;
  }
  public String[] convertAccountAge(String[] arg0, int arg1, String arg2) throws BusinessException {
    beforeCallMethod(328);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().convertAccountAge(arg0, arg1, arg2);
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
    return o;
  }
  public String[] convertCorps(String[] arg0, int arg1) throws BusinessException {
    beforeCallMethod(329);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().convertCorps(arg0, arg1);
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
  public String[] convertCostSubjs(String[] arg0, int arg1, String arg2) throws BusinessException {
    beforeCallMethod(330);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().convertCostSubjs(arg0, arg1, arg2);
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
  public String[] convertCurrs(String[] arg0, int arg1) throws BusinessException {
    beforeCallMethod(331);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().convertCurrs(arg0, arg1);
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
  public String[] convertCustom(String[] arg0, int arg1, String arg2, boolean arg3) throws BusinessException {
    beforeCallMethod(332);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().convertCustom(arg0, arg1, arg2, arg3);
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
    return o;
  }
  public String[] convertDept(String[] arg0, int arg1, String arg2) throws BusinessException {
    beforeCallMethod(333);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().convertDept(arg0, arg1, arg2);
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
  public String[] convertEmployee(String[] arg0, int arg1, String arg2) throws BusinessException {
    beforeCallMethod(334);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().convertEmployee(arg0, arg1, arg2);
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
  public String[] convertInventory(String[] arg0, int arg1, String arg2) throws BusinessException {
    beforeCallMethod(335);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().convertInventory(arg0, arg1, arg2);
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
  public String[] convertJobs(String[] arg0, int arg1, String arg2) throws BusinessException {
    beforeCallMethod(336);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().convertJobs(arg0, arg1, arg2);
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
  public String[] convertOpr(String arg0, String[] arg1, int arg2) throws BusinessException {
    beforeCallMethod(337);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().convertOpr(arg0, arg1, arg2);
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
  public Hashtable queryCustomByBasPKs(Hashtable arg0, String arg1, boolean arg2) throws BusinessException {
    beforeCallMethod(338);
    Exception er = null;
    Hashtable o = null;
    try {
      o = _getBeanObject().queryCustomByBasPKs(arg0, arg1, arg2);
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
  public void deleteBdcontrastinfoVO(BdcontrastinfoVO arg0) throws BusinessException {
    beforeCallMethod(339);
    Exception er = null;
    try {
      _getBeanObject().deleteBdcontrastinfoVO(arg0);
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
  }

  public BdcontrastinfoVO findBdcontrastinfoVOByPrimaryKey(String arg0) throws BusinessException {
    beforeCallMethod(340);
    Exception er = null;
    BdcontrastinfoVO o = null;
    try {
      o = _getBeanObject().findBdcontrastinfoVOByPrimaryKey(arg0);
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
  public String insertBdcontrastinfoVO(BdcontrastinfoVO arg0) throws BusinessException {
    beforeCallMethod(341);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().insertBdcontrastinfoVO(arg0);
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
  public BdcontrastinfoVO[] queryAllBdcontrastinfoVO(String arg0) throws BusinessException {
    beforeCallMethod(342);
    Exception er = null;
    BdcontrastinfoVO[] o = null;
    try {
      o = (BdcontrastinfoVO[])_getBeanObject().queryAllBdcontrastinfoVO(arg0);
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
    return o;
  }
  public void updateBdcontrastinfoVO(BdcontrastinfoVO arg0) throws BusinessException {
    beforeCallMethod(343);
    Exception er = null;
    try {
      _getBeanObject().updateBdcontrastinfoVO(arg0);
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
  }

  public HashMap getGlVoucherInfoBySourcePks(String[] arg0) throws BusinessException {
    beforeCallMethod(344);
    Exception er = null;
    HashMap o = null;
    try {
      o = _getBeanObject().getGlVoucherInfoBySourcePks(arg0);
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
    return o;
  }
  public void onSoSelectData(SoBalanceToArVO[] arg0, Object arg1) throws BusinessException {
    beforeCallMethod(345);
    Exception er = null;
    try {
      _getBeanObject().onSoSelectData(arg0, arg1);
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
  }

  public void afterChecked(String[] arg0, UFDate arg1, Boolean arg2) throws BusinessException {
    beforeCallMethod(346);
    Exception er = null;
    try {
      _getBeanObject().afterChecked(arg0, arg1, arg2);
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
  }

  public void beforeChecked(String[] arg0, UFDate arg1, Boolean arg2) throws BusinessException {
    beforeCallMethod(347);
    Exception er = null;
    try {
      _getBeanObject().beforeChecked(arg0, arg1, arg2);
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
  }

  public IVoAccess[] queryBalance(CConditionVO arg0, int arg1) throws BusinessException {
    beforeCallMethod(348);
    Exception er = null;
    IVoAccess[] o = null;
    try {
      o = (IVoAccess[])_getBeanObject().queryBalance(arg0, arg1);
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
  public IVoAccess[] queryByConditionVO(CConditionVO arg0, int arg1) throws BusinessException {
    beforeCallMethod(349);
    Exception er = null;
    IVoAccess[] o = null;
    try {
      o = (IVoAccess[])_getBeanObject().queryByConditionVO(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(349, er);
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
  public DJZBVO add(DJZBVO arg0) throws BusinessException {
    beforeCallMethod(350);
    Exception er = null;
    DJZBVO o = null;
    try {
      o = _getBeanObject().add(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(350, er);
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
  public void delete(String arg0) throws BusinessException {
    beforeCallMethod(351);
    Exception er = null;
    try {
      _getBeanObject().delete(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(351, er);
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

  public DJZBVO update(DJZBVO arg0) throws BusinessException {
    beforeCallMethod(352);
    Exception er = null;
    DJZBVO o = null;
    try {
      o = _getBeanObject().update(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(352, er);
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
  public DJZBVO confirm(DJZBVO arg0) throws BusinessException {
    beforeCallMethod(353);
    Exception er = null;
    DJZBVO o = null;
    try {
      o = _getBeanObject().confirm(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(353, er);
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
  public DJZBVO[] queryByID(String[] arg0) throws BusinessException {
    beforeCallMethod(354);
    Exception er = null;
    DJZBVO[] o = null;
    try {
      o = (DJZBVO[])_getBeanObject().queryByID(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(354, er);
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